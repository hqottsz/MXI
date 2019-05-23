package com.mxi.mx.web.query.task.labour;

import static com.mxi.mx.core.key.RefEventStatusKey.ACTV;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.HumanResource;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.GlobalParametersFake;
import com.mxi.mx.common.config.ParmTypeEnum;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.facade.stask.labour.LabourFacade;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.SchedLabourKey;
import com.mxi.mx.core.key.SchedStepKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.UserKey;
import com.mxi.mx.core.services.stask.labour.EditWorkCaptureTO;
import com.mxi.mx.core.services.stask.labour.JobStopTO;
import com.mxi.mx.core.services.stask.labour.LabourStepTO;
import com.mxi.mx.core.services.stask.workcapture.WorkCaptureService;
import com.mxi.mx.core.task.model.StepStatus;


/**
 * This class tests the query com.mxi.mx.web.query.task.GetWorkPerformedInfo.qrx
 *
 */
public final class GetWorkCaptureStepsTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   private HumanResourceKey technician;

   private int nextSchedStepRevisionNo = 1;

   private int nextLabourRevisionNo = 0;

   private final String FIRST_STEP_DESCRIPTION = "First Step";

   private final String SECOND_STEP_DESCRIPTION = "Second Step";

   private final static RefLabourSkillKey LBR = RefLabourSkillKey.LBR;

   private final static RefLabourSkillKey ENG = RefLabourSkillKey.ENG;

   private final static RefLabourSkillKey PILOT = RefLabourSkillKey.PILOT;

   private final static String FIRST_NAME = "First Name";

   private final static String LAST_NAME = "Last Name";

   private final String NOTE_1 = "Note 1";

   private final String NOTE_2 = "Note 2";

   private final String EDIT_NOTE_1 = "Note 1 Edit";


   /**
    * Tests that the query returns task labour steps
    * 
    */
   @Test
   public void testQuery() throws Exception {

      // DATA SETUP: Create an aircraft
      InventoryKey aircraft = Domain.createAircraft();

      // DATA SETUP: Create an actual task
      TaskKey actualTask = Domain.createAdhocTask( ( adhocTaskBuilder ) -> {
         adhocTaskBuilder.setInventory( aircraft );
         adhocTaskBuilder.setStatus( ACTV );
         adhocTaskBuilder.addStep( FIRST_STEP_DESCRIPTION );
         adhocTaskBuilder.addStep( SECOND_STEP_DESCRIPTION );

         adhocTaskBuilder.addLabour( labour -> {
            labour.setSkill( LBR );
            labour.setTechnicianRole( tech -> tech.setScheduledHours( 1 ) );
         } );

      } );

      // DATA SETUP: Create a work package and assign the task to it
      Domain.createWorkPackage( ( wpBuilder ) -> {
         wpBuilder.addTask( actualTask );
         wpBuilder.setStatus( RefEventStatusKey.IN_WORK );
         wpBuilder.setAircraft( aircraft );
      } );

      // DATA RETRIEVAL: Get labour id map
      Map<RefLabourSkillKey, SchedLabourKey> labourMap = getLabourRequirements( actualTask );

      SchedStepKey schedStep1 = getStep( FIRST_STEP_DESCRIPTION );
      SchedLabourKey schedLabourKey = labourMap.get( LBR );
      // DATA SETUP: Capture the first step
      schedLabourKey =
            captureWorkAndJobStop( schedLabourKey, schedStep1, NOTE_1, StepStatus.MXPENDING );

      SchedStepKey schedStep2 = getStep( SECOND_STEP_DESCRIPTION );
      // DATA SETUP: Capture the first step the second time
      schedLabourKey =
            captureWorkAndJobStop( schedLabourKey, schedStep2, NOTE_1, StepStatus.MXPARTIAL );

      schedLabourKey =
            captureWorkAndJobStop( schedLabourKey, schedStep1, NOTE_2, StepStatus.MXCOMPLETE );

      schedLabourKey =
            captureWorkAndJobStop( schedLabourKey, schedStep2, NOTE_2, StepStatus.MXCOMPLETE );

      QuerySet qs = execute( actualTask, null );

      assertEquals( 4, qs.getRowCount() );

      qs.first();

      // assert the note is updated
      assertEquals( StepStatus.MXPENDING.getDisplayName(), qs.getString( "skill_status" ) );
      assertEquals( NOTE_1, qs.getString( "notes_ldesc" ) );
      assertEquals( "1. " + LAST_NAME + ", " + FIRST_NAME, qs.getString( "performed_by" ) );
      assertEquals( StepStatus.MXCOMPLETE.toString(), qs.getString( "default_step_status" ) );
      assertNull( qs.getString( "current_labour_step_notes" ) );

      qs.next();

      assertEquals( StepStatus.MXCOMPLETE.getDisplayName(), qs.getString( "skill_status" ) );
      assertEquals( Integer.valueOf( 1 ), qs.getInteger( "step_ord" ) );
      assertEquals( NOTE_2, qs.getString( "notes_ldesc" ) );
      assertEquals( "2. " + LAST_NAME + ", " + FIRST_NAME, qs.getString( "performed_by" ) );
      assertEquals( StepStatus.MXCOMPLETE.toString(), qs.getString( "default_step_status" ) );
      assertNull( qs.getString( "current_labour_step_notes" ) );

      qs.next();

      assertEquals( StepStatus.MXPARTIAL.getDisplayName(), qs.getString( "skill_status" ) );
      assertEquals( Integer.valueOf( 2 ), qs.getInteger( "step_ord" ) );
      assertEquals( NOTE_1, qs.getString( "notes_ldesc" ) );
      assertEquals( "1. " + LAST_NAME + ", " + FIRST_NAME, qs.getString( "performed_by" ) );
      assertEquals( StepStatus.MXCOMPLETE.toString(), qs.getString( "default_step_status" ) );
      assertNull( qs.getString( "current_labour_step_notes" ) );

      qs.next();

      assertEquals( StepStatus.MXCOMPLETE.getDisplayName(), qs.getString( "skill_status" ) );
      assertEquals( Integer.valueOf( 2 ), qs.getInteger( "step_ord" ) );
      assertEquals( NOTE_2, qs.getString( "notes_ldesc" ) );
      assertEquals( "2. " + LAST_NAME + ", " + FIRST_NAME, qs.getString( "performed_by" ) );
      assertEquals( StepStatus.MXCOMPLETE.toString(), qs.getString( "default_step_status" ) );
      assertNull( qs.getString( "current_labour_step_notes" ) );

   }


   /**
    * Tests that the query returns the note and status that was originally entered for a given work
    * capture (Labour row)
    */
   @Test
   public void testQuery_gettingLabourRowForEditing() throws Exception {

      // DATA SETUP: Create an aircraft
      InventoryKey aircraft = Domain.createAircraft();

      // DATA SETUP: Create an actual task
      TaskKey actualTask = Domain.createAdhocTask( ( adhocTaskBuilder ) -> {
         adhocTaskBuilder.setInventory( aircraft );
         adhocTaskBuilder.setStatus( ACTV );
         adhocTaskBuilder.addStep( FIRST_STEP_DESCRIPTION );
         adhocTaskBuilder.addLabour( labour -> {
            labour.setSkill( ENG );
            labour.setTechnicianRole( tech -> tech.setScheduledHours( 1 ) );
         } );
         adhocTaskBuilder.addLabour( labour -> {
            labour.setSkill( PILOT );
            labour.setTechnicianRole( tech -> tech.setScheduledHours( 1 ) );
         } );

      } );

      // DATA SETUP: Create a work package and assign the task to it
      Domain.createWorkPackage( ( wpBuilder ) -> {
         wpBuilder.addTask( actualTask );
         wpBuilder.setStatus( RefEventStatusKey.IN_WORK );
         wpBuilder.setAircraft( aircraft );
      } );

      // DATA RETRIEVAL: Get labour id map
      Map<RefLabourSkillKey, SchedLabourKey> labourMap = getLabourRequirements( actualTask );

      SchedStepKey schedStep = getStep( FIRST_STEP_DESCRIPTION );
      SchedLabourKey firstLabourKey = labourMap.get( ENG );
      // DATA SETUP: Capture the first step
      captureWorkAndJobStop( firstLabourKey, schedStep, NOTE_1, StepStatus.MXPARTIAL );
      // DATA SETUP: Capture the first step the second time
      captureWorkAndJobStop( labourMap.get( PILOT ), schedStep, NOTE_2, StepStatus.MXCOMPLETE );

      // Asserts that two records returned. In this case we are passing as a parameter
      // firstLabourKey, which tells the sql to bring the note a status that was selected when the
      // work capture was done.
      QuerySet qs = execute( actualTask, firstLabourKey );

      assertEquals( 2, qs.getRowCount() );

      qs.first();

      // assert the note is updated
      assertEquals( "Partial", qs.getString( "skill_status" ) );
      assertEquals( NOTE_1, qs.getString( "notes_ldesc" ) );
      assertEquals( "1. " + LAST_NAME + ", " + FIRST_NAME, qs.getString( "performed_by" ) );
      // even though the current status of the step is complete, default_step_status returns
      // 'partial' because originally that was the status when the work capture was done
      assertEquals( StepStatus.MXPARTIAL.toString(), qs.getString( "default_step_status" ) );
      // because we are trying to edit the first work capture, we should return the note that was
      // entered at that point
      assertEquals( NOTE_1, qs.getString( "current_labour_step_notes" ) );

      qs.next();

      assertEquals( "Complete", qs.getString( "skill_status" ) );
      assertEquals( Integer.valueOf( 1 ), qs.getInteger( "step_ord" ) );
      assertEquals( NOTE_2, qs.getString( "notes_ldesc" ) );
      assertEquals( "2. " + LAST_NAME + ", " + FIRST_NAME, qs.getString( "performed_by" ) );
      // status and notes for current_labour_step_notes and default_step_status will be the same for
      // all instances of work captures done for step 1
      assertEquals( NOTE_1, qs.getString( "current_labour_step_notes" ) );
      assertEquals( StepStatus.MXPARTIAL.toString(), qs.getString( "default_step_status" ) );
   }


   /**
    * Test that notes and dates are updated properly after a labour row has been edited
    */
   @Test
   public void testQuery_editAdhocStep() throws Exception {

      // DATA SETUP: Create an aircraft
      InventoryKey aircraft = Domain.createAircraft();

      // DATA SETUP: Create an actual task
      TaskKey actualTask = Domain.createAdhocTask( ( adhocTaskBuilder ) -> {
         adhocTaskBuilder.setInventory( aircraft );
         adhocTaskBuilder.setStatus( ACTV );
         adhocTaskBuilder.addStep( FIRST_STEP_DESCRIPTION );
         adhocTaskBuilder.addLabour( labour -> {
            labour.setSkill( ENG );
            labour.setTechnicianRole( tech -> tech.setScheduledHours( 1 ) );
         } );
         adhocTaskBuilder.addLabour( labour -> {
            labour.setSkill( PILOT );
            labour.setTechnicianRole( tech -> tech.setScheduledHours( 1 ) );
         } );

      } );

      // DATA SETUP: Create a work package and assign the task to it
      Domain.createWorkPackage( ( wpBuilder ) -> {
         wpBuilder.addTask( actualTask );
         wpBuilder.setStatus( RefEventStatusKey.IN_WORK );
         wpBuilder.setAircraft( aircraft );
      } );

      // DATA RETRIEVAL: Get labour id map
      Map<RefLabourSkillKey, SchedLabourKey> labourMap = getLabourRequirements( actualTask );

      SchedStepKey schedStep = getStep( FIRST_STEP_DESCRIPTION );

      // DATA SETUP: Capture the first step
      captureWorkAndJobStop( labourMap.get( ENG ), schedStep, NOTE_1, StepStatus.MXPARTIAL );
      QuerySet qs = execute( actualTask, null );
      qs.next();
      Date firstJobStepPerformedDate = qs.getDate( "performed_dt" );

      // DATA SETUP: Capture the first step the second time
      captureWorkAndJobStop( labourMap.get( PILOT ), schedStep, NOTE_2, StepStatus.MXCOMPLETE );

      // DATA SETUP: Edit the first work capture
      LabourStepTO labourStepTO = new LabourStepTO();
      labourStepTO.setSchedStep( schedStep );
      labourStepTO.setNewNote( EDIT_NOTE_1 );
      labourStepTO.setLabourStepStatus( StepStatus.MXPENDING );
      labourStepTO.setSchedStepRevisionNo( nextSchedStepRevisionNo++ );
      labourStepTO.setRevisionNo( nextLabourRevisionNo++ );

      EditWorkCaptureTO editWorkCaptureTO = new EditWorkCaptureTO();
      editWorkCaptureTO.addLabourStepTOs( Arrays.asList( labourStepTO ) );
      delay();
      new WorkCaptureService().editWork( labourMap.get( ENG ), editWorkCaptureTO, technician );
      LabourFacade.signOffLabourReq( labourMap.get( ENG ), null, technician );

      // Asserts that two records returned
      qs = execute( actualTask, null );

      assertEquals( 2, qs.getRowCount() );

      qs.first();

      // assert the note is updated
      assertEquals( "Pending", qs.getString( "skill_status" ) );
      assertEquals( EDIT_NOTE_1, qs.getString( "notes_ldesc" ) );
      assertEquals( Integer.valueOf( 1 ), qs.getInteger( "step_ord" ) );
      assertEquals( "1. " + LAST_NAME + ", " + FIRST_NAME, qs.getString( "performed_by" ) );
      assertTrue(
            "The date when the job stop is edited must be greater than date it was originally done.",
            qs.getDate( "performed_dt" ).compareTo( firstJobStepPerformedDate ) > 0 );
      assertNull( qs.getString( "current_labour_step_notes" ) );
      assertEquals( StepStatus.MXCOMPLETE.toString(), qs.getString( "default_step_status" ) );
      qs.next();

      assertEquals( "Complete", qs.getString( "skill_status" ) );
      assertEquals( Integer.valueOf( 1 ), qs.getInteger( "step_ord" ) );
      assertEquals( NOTE_2, qs.getString( "notes_ldesc" ) );
      assertEquals( "2. " + LAST_NAME + ", " + FIRST_NAME, qs.getString( "performed_by" ) );
      assertNull( qs.getString( "current_labour_step_notes" ) );
      assertEquals( StepStatus.MXCOMPLETE.toString(), qs.getString( "default_step_status" ) );
   }


   private void delay() {
      try {
         Thread.sleep( 5 * 1000 );
      } catch ( InterruptedException e ) {
      }
   }


   private SchedStepKey getStep( String aStepDescription ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "step_ldesc", aStepDescription );

      QuerySet lQs = QuerySetFactory.getInstance().executeQuery( "SCHED_STEP", lArgs,
            new String[] { "sched_db_id", "sched_id", "step_id" } );

      lQs.next();

      return lQs.getKey( SchedStepKey.class, "sched_db_id", "sched_id", "step_id" );
   }


   private Map<RefLabourSkillKey, SchedLabourKey> getLabourRequirements( TaskKey aTaskKey ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aTaskKey, "sched_db_id", "sched_id" );

      QuerySet lQs = QuerySetFactory.getInstance().executeQuery( "SCHED_LABOUR", lArgs,
            new String[] { "labour_db_id", "labour_id", "labour_skill_db_id", "labour_skill_cd" } );

      Map<RefLabourSkillKey, SchedLabourKey> lLabourMap =
            new HashMap<RefLabourSkillKey, SchedLabourKey>();

      while ( lQs.next() ) {
         lLabourMap.put(
               lQs.getKey( RefLabourSkillKey.class, "labour_skill_db_id", "labour_skill_cd" ),
               lQs.getKey( SchedLabourKey.class, "labour_db_id", "labour_id" ) );
      }

      return lLabourMap;
   }


   /**
    * Capture steps and job stop the labour row
    *
    */
   private SchedLabourKey captureWorkAndJobStop( SchedLabourKey aSchedLabour,
         SchedStepKey aSchedStep, String aStatusNote, StepStatus aStepStatus ) throws Exception {

      LabourStepTO labourStepTO = new LabourStepTO();
      labourStepTO.setSchedStep( aSchedStep );
      labourStepTO.setNewNote( aStatusNote );
      labourStepTO.setLabourStepStatus( aStepStatus );
      labourStepTO.setSchedStepRevisionNo( nextSchedStepRevisionNo++ );
      labourStepTO.setRevisionNo( nextLabourRevisionNo++ );

      JobStopTO jobStopTO = new JobStopTO();
      jobStopTO.setTechnician( technician, "HR" );
      jobStopTO.addLabourStepTOs( Arrays.asList( labourStepTO ) );
      jobStopTO.setAutoCertifyWork( true );
      jobStopTO.setRemainingHours( 1.0, "Remaining Hours" );

      new WorkCaptureService().captureWork( aSchedLabour, jobStopTO );

      SchedLabourKey lNewSchedLabour = LabourFacade.stop( aSchedLabour, jobStopTO );

      return lNewSchedLabour;
   }


   /**
    * This method executes the query in GetStepStatusForStep.qrx
    *
    * @return The dataset after execution.
    */
   private QuerySet execute( TaskKey taskKey, SchedLabourKey schedLabourKey ) {

      DataSetArgument args = new DataSetArgument();
      args.add( taskKey, "aTaskDbId", "aTaskId" );
      args.add( schedLabourKey, "aLabourDbId", "aLabourId" );

      return QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), args );
   }


   @Before
   public void setUp() {

      // DATA SETUP: Create a human resource key for the current user
      Integer currentUserId = SecurityIdentificationUtils.getInstance().getCurrentUserId();

      // Determine if the current user already has an entry in org_hr or not.
      DataSetArgument args = new DataSetArgument();
      args.add( "user_id", currentUserId );

      QuerySet userQs = QuerySetFactory.getInstance().executeQuery( "UTL_USER", args,
            new String[] { "user_id" } );

      if ( !userQs.next() ) {
         Domain.createUser( ( userBuilder ) -> {
            userBuilder.setUserId( currentUserId );
            userBuilder.setFirstName( FIRST_NAME );
            userBuilder.setLastName( LAST_NAME );
         } );
      }

      QuerySet qs = QuerySetFactory.getInstance().executeQuery( "ORG_HR", args,
            new String[] { "hr_db_id", "hr_id" } );

      if ( userQs.next() ) {
         technician = qs.getKey( HumanResourceKey.class, "hr_db_id", "hr_id" );

      } else {
         technician = Domain.createHumanResource( ( HumanResource aHrBuilder ) -> {
            aHrBuilder.setUser( new UserKey( currentUserId ) );
         } );
      }

      UserParametersFake userParms = new UserParametersFake( currentUserId, "SECURED_RESOURCE" );
      UserParameters.setInstance( currentUserId, "SECURED_RESOURCE", userParms );

      // DATA SETUP: Change the config parm value
      GlobalParametersFake configParms = new GlobalParametersFake( ParmTypeEnum.LOGIC.name() );
      configParms.setBoolean( "ENABLE_MULTIPLE_SKILLS_SIGN_OFF_ON_SAME_STEP", true );
      GlobalParameters.setInstance( configParms );
   }

}
