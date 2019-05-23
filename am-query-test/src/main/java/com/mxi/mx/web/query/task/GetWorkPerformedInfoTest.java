package com.mxi.mx.web.query.task;

import static com.mxi.mx.core.key.RefEventStatusKey.ACTV;
import static com.mxi.mx.core.key.RefLabourSkillKey.LBR;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
public final class GetWorkPerformedInfoTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   private HumanResourceKey iTechnician;

   private int iNextSchedStepRevisionNo = 1;

   private int iNextLabourRevisionNo = 0;

   private final String FIRST_STEP_DESCRIPTION = "First Step";

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
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQuery() throws Exception {

      // DATA SETUP: Create an aircraft
      InventoryKey lAircraft = Domain.createAircraft();

      // DATA SETUP: Create an actual task
      TaskKey lActualTask = Domain.createAdhocTask( ( aAdhocTaskBuilder ) -> {
         aAdhocTaskBuilder.setInventory( lAircraft );
         aAdhocTaskBuilder.setStatus( ACTV );
         aAdhocTaskBuilder.addStep( FIRST_STEP_DESCRIPTION );
         aAdhocTaskBuilder.addLabour( labour -> {
            labour.setSkill( ENG );
            labour.setTechnicianRole( tech -> tech.setScheduledHours( 1 ) );
         } );
         aAdhocTaskBuilder.addLabour( labour -> {
            labour.setSkill( PILOT );
            labour.setTechnicianRole( tech -> tech.setScheduledHours( 1 ) );
         } );
         aAdhocTaskBuilder.addLabour( labour -> {
            labour.setSkill( LBR );
            labour.setTechnicianRole( tech -> tech.setScheduledHours( 1 ) );
         } );
      } );

      // DATA SETUP: Create a work package and assign the task to it
      Domain.createWorkPackage( ( aWpBuilder ) -> {
         aWpBuilder.addTask( lActualTask );
         aWpBuilder.setStatus( RefEventStatusKey.IN_WORK );
         aWpBuilder.setAircraft( lAircraft );
      } );

      // DATA RETRIEVAL: Get labour id map
      Map<RefLabourSkillKey, SchedLabourKey> lLabourMap = getLabourRequirements( lActualTask );

      SchedStepKey lSchedStep = getStep( FIRST_STEP_DESCRIPTION );

      // DATA SETUP: Capture the first step
      captureWorkAndJobStop( lLabourMap.get( ENG ), lSchedStep, NOTE_1, StepStatus.MXPARTIAL );

      QuerySet lQs = execute( lSchedStep );

      // assert that there's one record
      assertEquals( 1, lQs.getRowCount() );

      lQs.next();

      // assert the return data
      assertEquals( "Partial", lQs.getString( "step_labour_status" ) );
      assertEquals( NOTE_1, lQs.getString( "notes_ldesc" ) );
      assertEquals( "1. " + LAST_NAME + ", " + FIRST_NAME, lQs.getString( "performed_by" ) );
      Date firstJobStepPerformedDate = lQs.getDate( "performed_dt" );
      // Note: to delay the second job stop so that the timestamp is different otherwise the test
      // will be intermittent.
      Date lNow = new Date();

      Calendar lNowTime = Calendar.getInstance();
      lNowTime.setTime( lNow );

      Calendar lLaterTime = Calendar.getInstance();
      lLaterTime.setTime( lNow );
      lLaterTime.add( Calendar.SECOND, 5 );

      while ( lNowTime.getTime().before( lLaterTime.getTime() ) ) {

         if ( lNowTime.getTime().after( lLaterTime.getTime() ) ) {
            break;
         }

         lNowTime.setTime( new Date() );
      }

      // DATA SETUP: Capture the first step the second time
      captureWorkAndJobStop( lLabourMap.get( PILOT ), lSchedStep, NOTE_2, StepStatus.MXCOMPLETE );

      lQs = execute( lSchedStep );

      // asserts that there are two records
      assertEquals( 2, lQs.getRowCount() );

      lQs.first();

      assertEquals( "Partial", lQs.getString( "step_labour_status" ) );
      assertEquals( NOTE_1, lQs.getString( "notes_ldesc" ) );
      assertEquals( "1. " + LAST_NAME + ", " + FIRST_NAME, lQs.getString( "performed_by" ) );

      lQs.next();

      assertEquals( "Complete", lQs.getString( "step_labour_status" ) );
      assertEquals( NOTE_2, lQs.getString( "notes_ldesc" ) );
      assertEquals( "2. " + LAST_NAME + ", " + FIRST_NAME, lQs.getString( "performed_by" ) );

      // DATA SETUP: Edit the first work capture
      LabourStepTO lLabourStepTO = new LabourStepTO();
      lLabourStepTO.setSchedStep( lSchedStep );
      lLabourStepTO.setNewNote( EDIT_NOTE_1 );
      lLabourStepTO.setLabourStepStatus( StepStatus.MXPENDING );
      lLabourStepTO.setSchedStepRevisionNo( iNextSchedStepRevisionNo++ );
      lLabourStepTO.setRevisionNo( iNextLabourRevisionNo++ );

      EditWorkCaptureTO lEditWorkCaptureTO = new EditWorkCaptureTO();
      lEditWorkCaptureTO.addLabourStepTOs( Arrays.asList( lLabourStepTO ) );

      // Note: to delay edit job stop so that the timestamp is different otherwise the test
      // will be intermittent.
      lNow = new Date();

      lNowTime = Calendar.getInstance();
      lNowTime.setTime( lNow );

      lLaterTime = new GregorianCalendar();
      lLaterTime.setTime( lNow );
      lLaterTime.add( Calendar.SECOND, 5 );

      while ( lNowTime.getTime().before( lLaterTime.getTime() ) ) {

         if ( lNowTime.getTime().after( lLaterTime.getTime() ) ) {
            break;
         }

         lNowTime.setTime( new Date() );
      }

      new WorkCaptureService().editWork( lLabourMap.get( ENG ), lEditWorkCaptureTO, iTechnician );

      // Asserts that two records returned
      lQs = execute( lSchedStep );

      assertEquals( 2, lQs.getRowCount() );

      lQs.first();

      // assert the note is updated
      assertEquals( "Pending", lQs.getString( "step_labour_status" ) );
      assertEquals( EDIT_NOTE_1, lQs.getString( "notes_ldesc" ) );
      assertEquals( "1. " + LAST_NAME + ", " + FIRST_NAME, lQs.getString( "performed_by" ) );
      assertTrue(
            "The date when the job stop is edited must be greater than date it was originally done.",
            lQs.getDate( "performed_dt" ).compareTo( firstJobStepPerformedDate ) > 0 );
      lQs.next();

      assertEquals( "Complete", lQs.getString( "step_labour_status" ) );
      assertEquals( NOTE_2, lQs.getString( "notes_ldesc" ) );
      assertEquals( "2. " + LAST_NAME + ", " + FIRST_NAME, lQs.getString( "performed_by" ) );
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

      LabourStepTO lLabourStepTO = new LabourStepTO();
      lLabourStepTO.setSchedStep( aSchedStep );
      lLabourStepTO.setNewNote( aStatusNote );
      lLabourStepTO.setLabourStepStatus( aStepStatus );
      lLabourStepTO.setSchedStepRevisionNo( iNextSchedStepRevisionNo++ );
      lLabourStepTO.setRevisionNo( iNextLabourRevisionNo++ );

      JobStopTO lJobStopTO = new JobStopTO();
      lJobStopTO.setTechnician( iTechnician, "HR" );
      lJobStopTO.addLabourStepTOs( Arrays.asList( lLabourStepTO ) );
      lJobStopTO.setAutoCertifyWork( true );
      lJobStopTO.setRemainingHours( 1.0, "Remaining Hours" );

      new WorkCaptureService().captureWork( aSchedLabour, lJobStopTO );

      SchedLabourKey lNewSchedLabour = LabourFacade.stop( aSchedLabour, lJobStopTO );

      return lNewSchedLabour;
   }


   /**
    * This method executes the query in GetStepStatusForStep.qrx
    *
    * @return The dataset after execution.
    */
   private QuerySet execute( SchedStepKey aSchedStep ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aSchedStep, "aSchedDbId", "aSchedId", "aStepId" );

      return QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   @Before
   public void setUp() {

      // DATA SETUP: Create a human resource key for the current user
      Integer lCurrentUserId = SecurityIdentificationUtils.getInstance().getCurrentUserId();

      // Determine if the current user already has an entry in org_hr or not.
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "user_id", lCurrentUserId );

      QuerySet lUserQs = QuerySetFactory.getInstance().executeQuery( "UTL_USER", lArgs,
            new String[] { "user_id" } );

      if ( !lUserQs.next() ) {
         Domain.createUser( ( aUserBuilder ) -> {
            aUserBuilder.setUserId( lCurrentUserId );
            aUserBuilder.setFirstName( FIRST_NAME );
            aUserBuilder.setLastName( LAST_NAME );
         } );
      }

      QuerySet lQs = QuerySetFactory.getInstance().executeQuery( "ORG_HR", lArgs,
            new String[] { "hr_db_id", "hr_id" } );

      if ( lUserQs.next() ) {
         iTechnician = lQs.getKey( HumanResourceKey.class, "hr_db_id", "hr_id" );

      } else {
         iTechnician = Domain.createHumanResource( ( HumanResource aHrBuilder ) -> {
            aHrBuilder.setUser( new UserKey( lCurrentUserId ) );
         } );
      }

      UserParametersFake lUserParms = new UserParametersFake( lCurrentUserId, "SECURED_RESOURCE" );
      UserParameters.setInstance( lCurrentUserId, "SECURED_RESOURCE", lUserParms );

      // DATA SETUP: Change the config parm value
      GlobalParametersFake lConfigParms = new GlobalParametersFake( ParmTypeEnum.LOGIC.name() );
      lConfigParms.setBoolean( "ENABLE_MULTIPLE_SKILLS_SIGN_OFF_ON_SAME_STEP", true );
      GlobalParameters.setInstance( lConfigParms );
   }

}
