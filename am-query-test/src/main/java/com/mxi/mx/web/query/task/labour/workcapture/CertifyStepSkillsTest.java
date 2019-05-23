package com.mxi.mx.web.query.task.labour.workcapture;

import static com.mxi.am.domain.Domain.createRequirement;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.UUID;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.HumanResource;
import com.mxi.am.domain.Requirement;
import com.mxi.am.domain.RequirementDefinition;
import com.mxi.am.domain.Step;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.GlobalParametersFake;
import com.mxi.mx.common.config.ParmTypeEnum;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.facade.stask.labour.LabourFacade;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.SchedLabourKey;
import com.mxi.mx.core.key.SchedStepKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskStepKey;
import com.mxi.mx.core.key.TaskStepSkillKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.key.UserKey;
import com.mxi.mx.core.services.stask.labour.EditWorkCaptureTO;
import com.mxi.mx.core.services.stask.labour.JobStopTO;
import com.mxi.mx.core.services.stask.labour.LabourStepTO;
import com.mxi.mx.core.services.stask.workcapture.WorkCaptureService;
import com.mxi.mx.core.table.task.TaskStepSkillDao;
import com.mxi.mx.core.task.model.StepStatus;


/**
 * This class tests the query com.mxi.mx.web.query.task.labour.workcapture.qrx
 *
 */

public final class CertifyStepSkillsTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   private final static String STEP_NOTE = "Note 1";

   private final static RefLabourSkillKey ENG = RefLabourSkillKey.ENG;

   private final static boolean MULTIPLE_STEPS = true;

   private final static boolean ONE_STEP = false;

   private final static boolean WITH_SKILL = true;

   private final static boolean NO_SKILL = false;

   private static String TECH_NAME = "TECH";

   private int iNextSchedStepRevisionNo = 1;

   private int iNextLabourRevisionNo = 1;

   private HumanResourceKey iTechnician;

   private TaskTaskKey iReqDefn;

   private TaskKey iActualTask;

   private SchedLabourKey iSchedLabour_ENG;

   private SchedStepKey iSchedStep1;

   private SchedStepKey iSchedStep2;

   private UUID iStepSkillId1;

   private UUID iStepSkillId2;


   @Test
   public void testTwoStepSkills() throws Exception {

      // DATA SETUP
      createBaselinedTask( ONE_STEP, WITH_SKILL );

      // DATA SETUP: Capture work without changing step skill status
      SchedLabourKey lNewSchedLabour =
            jobStopLabourRow( iSchedLabour_ENG, STEP_NOTE, StepStatus.MXPENDING );

      // DATA SETUP: Capture work with new step skill status
      jobStopLabourRow( lNewSchedLabour, STEP_NOTE, StepStatus.MXPARTIAL );

      // ************************************************************
      // TEST 1: Certify the first labor row
      //
      // RESULTS:
      // The step skill that was captured by the first labour row:
      // - Status should not be highlighted
      // - Note should be highlighted
      // The step skill that was captured by the second labour row:
      // - Status should not be highlighted
      // - Note should not be highlighted
      // ************************************************************
      QuerySet lQs = execute( iSchedLabour_ENG );

      assertEquals( 2, lQs.getRowCount() );

      while ( lQs.next() ) {

         assertFalse( lQs.getBoolean( "highlight_step_status" ) );

         if ( iSchedLabour_ENG
               .equals( lQs.getKey( SchedLabourKey.class, "labour_db_id", "labour_id" ) ) ) {

            assertTrue( lQs.getBoolean( "highlight_note" ) );
         } else {
            assertFalse( lQs.getBoolean( "highlight_note" ) );
         }
      }

      // ***********************************************************************************************
      // TEST 2: Certify the second labor row
      //
      // RESULTS:
      // The step skill that was captured by the first labour row:
      // - Status should be highlighted (because the second labor row change the status so therefore
      // it should be highlighted. This is used for rowspan in JSP since we do not display the
      // status separately for each labor row)
      // - Note should not be highlighted
      // The step skill that was captured by the second labour row:
      // - Status should be highlighted
      // - Note should be highlighted
      // ***********************************************************************************************
      lQs = execute( lNewSchedLabour );

      assertEquals( 2, lQs.getRowCount() );

      while ( lQs.next() ) {

         assertTrue( lQs.getBoolean( "highlight_step_status" ) );

         if ( iSchedLabour_ENG
               .equals( lQs.getKey( SchedLabourKey.class, "labour_db_id", "labour_id" ) ) ) {

            assertFalse( lQs.getBoolean( "highlight_note" ) );
         } else {
            assertTrue( lQs.getBoolean( "highlight_note" ) );
         }
      }

      // ********************************************************************************************
      // TEST 3: Certify both labour rows
      //
      // RESULTS:
      // The step skill that was captured by the first labour row:
      // - Status should be highlighted (because the second labor row change the status so therefore
      // it should be highlighted. This is used for rowspan in JSP since we do not display the
      // status separately for each labor row)
      // - Note should be highlighted
      // The step skill that was captured by the second labour row:
      // - Status should be highlighted
      // - Note should be highlighted
      // ********************************************************************************************
      lQs = execute( iSchedLabour_ENG, lNewSchedLabour );

      assertEquals( 2, lQs.getRowCount() );

      while ( lQs.next() ) {

         assertTrue( lQs.getBoolean( "highlight_step_status" ) );
         assertTrue( lQs.getBoolean( "highlight_note" ) );
      }
   }


   @Test
   public void testEditStepSkill() throws Exception {
      // DATA SETUP
      createBaselinedTask( MULTIPLE_STEPS, WITH_SKILL );

      // DATA SETUP: Capture work with a new step skill status
      jobStopLabourRow( iSchedLabour_ENG, STEP_NOTE, StepStatus.MXPARTIAL );

      // *****************************************
      // TEST 1: Certify labor row
      //
      // RESULTS:
      // The first step:
      // - Status should be highlighted
      // - Note should be highlighted
      // The second step:
      // - Status should not be highlighted
      // - Note should not be highlighted
      // *****************************************
      QuerySet lQs = execute( iSchedLabour_ENG );

      assertEquals( 2, lQs.getRowCount() );

      while ( lQs.next() ) {

         if ( iSchedStep1
               .equals( lQs.getKey( SchedStepKey.class, "sched_db_id", "sched_id", "step_id" ) ) ) {
            assertTrue( lQs.getBoolean( "highlight_step_status" ) );
            assertTrue( lQs.getBoolean( "highlight_note" ) );
         } else {
            assertFalse( lQs.getBoolean( "highlight_step_status" ) );
            assertFalse( lQs.getBoolean( "highlight_note" ) );
         }
      }

      // DATA SETUP: Edit the first labor row and capture the second step
      LabourStepTO lLabourStepTO = new LabourStepTO();
      lLabourStepTO.setSchedStep( iSchedStep2 );
      lLabourStepTO.setNewNote( "Edit Note" );
      lLabourStepTO.setLabourStepStatus( StepStatus.MXPENDING );
      lLabourStepTO.setSchedStepRevisionNo( iNextSchedStepRevisionNo++ );
      lLabourStepTO.setRevisionNo( iNextLabourRevisionNo++ );
      lLabourStepTO.setStepSkillId( iStepSkillId2 );

      EditWorkCaptureTO lEditWorkCaptureTO = new EditWorkCaptureTO();
      lEditWorkCaptureTO.addLabourStepTOs( Arrays.asList( lLabourStepTO ) );

      new WorkCaptureService().editWork( iSchedLabour_ENG, lEditWorkCaptureTO, iTechnician );

      LabourFacade.signOffLabourReq( iSchedLabour_ENG, null, iTechnician );

      // ***********************************************
      // TEST 2: Certify labor row that has been edited
      //
      // RESULTS:
      // The first step:
      // - Status should be highlighted
      // - Note should be highlighted
      // The second step:
      // - Status should not be highlighted
      // - Note should be highlighted
      // ***********************************************
      lQs = execute( iSchedLabour_ENG );

      assertEquals( 2, lQs.getRowCount() );

      while ( lQs.next() ) {

         if ( iSchedStep1
               .equals( lQs.getKey( SchedStepKey.class, "sched_db_id", "sched_id", "step_id" ) ) ) {
            assertTrue( lQs.getBoolean( "highlight_step_status" ) );
         } else {
            assertFalse( lQs.getBoolean( "highlight_step_status" ) );
         }

         assertTrue( lQs.getBoolean( "highlight_note" ) );
      }
   }


   @Test
   public void testNoStepSkill() throws Exception {

      // DATA SETUP
      createBaselinedTask( ONE_STEP, NO_SKILL );

      // DATA SETUP: Capture work with a new step skill status
      jobStopLabourRow( iSchedLabour_ENG, STEP_NOTE, StepStatus.MXPARTIAL );

      QuerySet lQs = execute( iSchedLabour_ENG );

      assertEquals( 1, lQs.getRowCount() );

      lQs.next();

      assertTrue( lQs.getBoolean( "highlight_step_status" ) );
      assertTrue( lQs.getBoolean( "highlight_note" ) );
   }


   /**
    * Tests that the order sequence is ascending for the notes entered in different work captures
    * for a step.
    */
   @Test
   public void testEditStepSkill_Order() throws Exception {
      // DATA SETUP
      createBaselinedTask( ONE_STEP, WITH_SKILL );

      // DATA SETUP: Capture work with a new step skill status
      SchedLabourKey newLabour =
            jobStopLabourRow( iSchedLabour_ENG, STEP_NOTE, StepStatus.MXPARTIAL );

      jobStopLabourRow( newLabour, STEP_NOTE, StepStatus.MXCOMPLETE );

      QuerySet qs = execute( iSchedLabour_ENG );

      assertEquals( 2, qs.getRowCount() );

      // DATA SETUP: Edit the first labor row
      LabourStepTO labourStepTO = new LabourStepTO();
      labourStepTO.setSchedStep( iSchedStep1 );
      labourStepTO.setNewNote( "Edit Note" );
      labourStepTO.setLabourStepStatus( StepStatus.MXPENDING );
      labourStepTO.setSchedStepRevisionNo( iNextSchedStepRevisionNo++ );
      labourStepTO.setRevisionNo( iNextLabourRevisionNo++ );
      labourStepTO.setStepSkillId( iStepSkillId1 );

      EditWorkCaptureTO editWorkCaptureTO = new EditWorkCaptureTO();
      editWorkCaptureTO.addLabourStepTOs( Arrays.asList( labourStepTO ) );

      new WorkCaptureService().editWork( iSchedLabour_ENG, editWorkCaptureTO, iTechnician );

      LabourFacade.signOffLabourReq( iSchedLabour_ENG, null, iTechnician );

      qs = execute( iSchedLabour_ENG );

      assertEquals( 2, qs.getRowCount() );
      qs.next();
      assertEquals( "1. " + TECH_NAME + ", " + TECH_NAME, qs.getString( "performed_by" ) );
      qs.next();
      assertEquals( "2. " + TECH_NAME + ", " + TECH_NAME, qs.getString( "performed_by" ) );

   }


   /**
    * This method executes the query in CertifyStepSkills.qrx
    *
    * @return The dataset after execution.
    */
   private DataSet execute( SchedLabourKey... aSchedLabour ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( iActualTask, "aSchedDbId", "aSchedId" );
      lArgs.addWhereIn( "WHERE_SELECTED_LABOUR_CLAUSE",
            new String[] { "labour_db_id", "labour_id" }, aSchedLabour );

      return QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   private void createBaselinedTask( boolean aMultipleSteps, boolean aHasSkills ) {

      // DATA SETUP: Create an executable requirement with a step that has a step skill
      iReqDefn = Domain.createRequirementDefinition(
            ( RequirementDefinition aRequirementDefinitionBuilder ) -> {

               aRequirementDefinitionBuilder.setExecutable( true );
               aRequirementDefinitionBuilder.addStep( ( Step aStepBuilder ) -> {

                  aStepBuilder.setDescription( "Step description" );

                  if ( aHasSkills ) {
                     aStepBuilder.addStepSkill( ENG, false );
                  }
               } );

               if ( aMultipleSteps ) {
                  aRequirementDefinitionBuilder.addStep( ( Step aStepBuilder ) -> {

                     aStepBuilder.setDescription( "Step description 2" );

                     if ( aHasSkills ) {
                        aStepBuilder.addStepSkill( ENG, false );
                     }
                  } );
               }
            } );

      // DATA SETUP: Create an actual task
      iActualTask = createRequirement( ( Requirement aBuilder ) -> {
         aBuilder.setDefinition( iReqDefn );
         aBuilder.addLabour( aLabour -> {
            aLabour.setSkill( ENG );
            aLabour.setTechnicianRole( tech -> tech.setScheduledHours( 1 ) );
            aLabour.setCertifierRole( cert -> cert.setScheduledHours( 1 ) );
         } );
      } );

      // DATA SETUP: Create a work package and assign the task to it
      Domain.createWorkPackage( ( aWpBuilder ) -> {
         aWpBuilder.addTask( iActualTask );
         aWpBuilder.setStatus( RefEventStatusKey.IN_WORK );
      } );

      // DATA RETRIEVAL: Get labour row key
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( iActualTask, "sched_db_id", "sched_id" );

      QuerySet lQs = QuerySetFactory.getInstance().executeQuery( "SCHED_LABOUR", lArgs,
            new String[] { "labour_db_id", "labour_id", "labour_skill_db_id", "labour_skill_cd" } );

      assertEquals( 1, lQs.getRowCount() );

      lQs.next();

      iSchedLabour_ENG = lQs.getKey( SchedLabourKey.class, "labour_db_id", "labour_id" );

      // DATA RETRIEVAL: Get the step key
      iSchedStep1 = new SchedStepKey( iActualTask, 1 );

      if ( aMultipleSteps ) {
         iSchedStep2 = new SchedStepKey( iActualTask, 2 );
      }

      // DATA RETRIEVAL: Get the step skill id from the task definition
      if ( aHasSkills ) {
         iStepSkillId1 = InjectorContainer.get().getInstance( TaskStepSkillDao.class )
               .findByPrimaryKey( new TaskStepSkillKey( new TaskStepKey( iReqDefn, 1 ), ENG ) )
               .getAlternateKey();

         if ( aMultipleSteps ) {
            iStepSkillId2 = InjectorContainer.get().getInstance( TaskStepSkillDao.class )
                  .findByPrimaryKey( new TaskStepSkillKey( new TaskStepKey( iReqDefn, 2 ), ENG ) )
                  .getAlternateKey();
         }
      }
   }


   /**
    * Capture steps and job stop the labour row
    *
    */
   private SchedLabourKey jobStopLabourRow( SchedLabourKey aSchedLabour, String aStatusNote,
         StepStatus aStepStatus ) throws Exception {

      LabourStepTO lLabourStepTO = new LabourStepTO();
      lLabourStepTO.setSchedStep( iSchedStep1 );
      lLabourStepTO.setNewNote( aStatusNote );
      lLabourStepTO.setLabourStepStatus( aStepStatus );
      lLabourStepTO.setSchedStepRevisionNo( iNextSchedStepRevisionNo++ );
      lLabourStepTO.setRevisionNo( iNextLabourRevisionNo++ );
      lLabourStepTO.setStepSkillId( iStepSkillId1 );

      JobStopTO lJobStopTO = new JobStopTO();
      lJobStopTO.setTechnician( iTechnician, "HR" );
      lJobStopTO.addLabourStepTOs( Arrays.asList( lLabourStepTO ) );
      lJobStopTO.setAutoCertifyWork( false );
      lJobStopTO.setRemainingHours( 1.0, "Remaining Hours" );

      new WorkCaptureService().captureWork( aSchedLabour, lJobStopTO );

      SchedLabourKey lNewSchedLabour = LabourFacade.stop( aSchedLabour, lJobStopTO );

      return lNewSchedLabour;
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
            aUserBuilder.setFirstName( TECH_NAME );
            aUserBuilder.setLastName( TECH_NAME );
            aUserBuilder.setUsername( TECH_NAME );
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
