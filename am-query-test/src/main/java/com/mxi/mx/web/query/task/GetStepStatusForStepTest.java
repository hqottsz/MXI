package com.mxi.mx.web.query.task;

import static com.mxi.am.domain.Domain.createRequirement;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
import com.mxi.am.domain.User;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.GlobalParametersFake;
import com.mxi.mx.common.config.ParmTypeEnum;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MxException;
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
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.key.UserKey;
import com.mxi.mx.core.services.stask.labour.JobStopTO;
import com.mxi.mx.core.services.stask.labour.LabourStepTO;
import com.mxi.mx.core.services.stask.workcapture.WorkCaptureService;
import com.mxi.mx.core.task.model.StepStatus;


/**
 * This class tests the query com.mxi.mx.web.query.task.GetStepStatusForStep.qrx
 *
 */
public final class GetStepStatusForStepTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   private final static RefLabourSkillKey ENG = RefLabourSkillKey.ENG;

   private final static RefLabourSkillKey PILOT = RefLabourSkillKey.PILOT;

   private final static boolean FIELD_ENABLED = true;

   private final static boolean FIELD_DISABLED = false;

   private final static boolean WITH_SKILLS = true;

   private final static boolean NO_SKILLS = false;

   private HumanResourceKey iTechnician;

   private TaskTaskKey iReqDefn;

   private TaskKey iActualTask;

   private SchedStepKey iSchedStep_1;

   private UUID iStepSkill_ENG;

   private SchedLabourKey iLabour_ENG;

   private int iNextSchedStepRevisionNo = 1;

   private int iNextLabourRevisionNo = 0;


   /**
    * Tests the "Mark Step As Not Applicable" dropdown option is enabled for skill with PARTIAL
    * status and is disabled for skill with PENDING status
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testMarkStepAsNotApplicableVisiblity_PartialAndPending() throws Exception {

      // DATA SETUP
      createTask( WITH_SKILLS );

      // DATA SETUP: Sign off ENG skill with PARTIAL status
      JobStopTO lTO = createJobStopTO( iSchedStep_1, iStepSkill_ENG, StepStatus.MXPARTIAL );

      new WorkCaptureService().captureWork( iLabour_ENG, lTO );

      // Asserts the "Mark Step As Not Applicable" option is enabled since it has MXPARTIAL status
      QuerySet lQs = execute( iSchedStep_1, ENG );

      assertRow( lQs, FIELD_ENABLED );

      // Asserts the "Mark Step As Not Applicable" option is disabled for PILOT skill since it has
      // MXPENDING status
      lQs = execute( iSchedStep_1, PILOT );

      assertRow( lQs, FIELD_DISABLED );
   }


   /**
    * Tests the "Mark Step As Not Applicable" dropdown option is enabled for skill with COMPLETE
    * status and is disabled for skill with PENDING status
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testMarkStepAsNotApplicableVisiblity_CompleteAndPending() throws Exception {

      // DATA SETUP
      createTask( WITH_SKILLS );

      // DATA SETUP: Sign off ENG skill with PARTIAL status
      JobStopTO lTO = createJobStopTO( iSchedStep_1, iStepSkill_ENG, StepStatus.MXCOMPLETE );

      new WorkCaptureService().captureWork( iLabour_ENG, lTO );

      // Asserts the "Mark Step As Not Applicable" option is enabled for ENG since it has MXPARTIAL
      // status
      QuerySet lQs = execute( iSchedStep_1, ENG );

      assertRow( lQs, FIELD_ENABLED );

      // Asserts the "Mark Step As Not Applicable" option is disabled for PILOT skill since it has
      // MXPENDING status
      lQs = execute( iSchedStep_1, PILOT );

      assertRow( lQs, FIELD_DISABLED );
   }


   /**
    * Tests the "Mark Step As Not Applicable" dropdown option is enabled for skill with the latest
    * status of PENDING
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testMarkStepAsNotApplicableVisiblity_AllPending() throws Exception {

      // DATA SETUP
      createTask( WITH_SKILLS );

      // DATA SETUP: Sign off ENG skill with PARTIAL status
      SchedLabourKey lSchedLabour1 =
            captureWorkAndJobStop( iLabour_ENG, iStepSkill_ENG, StepStatus.MXPARTIAL );

      // DATA SETUP: Sign off ENG skill again with COMPLETE status
      SchedLabourKey lSchedLabour2 =
            captureWorkAndJobStop( lSchedLabour1, iStepSkill_ENG, StepStatus.MXCOMPLETE );

      // DATA SETUP: Sign off ENG skill again with PENDING status
      captureWorkAndJobStop( lSchedLabour2, iStepSkill_ENG, StepStatus.MXPENDING );

      // DATA SETUP: Sign of PILOT skill with PENDING status
      UUID lStepSkill_PILOT = getStepSkill( iReqDefn, PILOT );
      SchedLabourKey lLabour_PILOT = getLabourRequirement( iActualTask, PILOT );

      captureWorkAndJobStop( lLabour_PILOT, lStepSkill_PILOT, StepStatus.MXPENDING );

      // Asserts the "Mark Step As Not Applicable" option is enabled for ENG skill since all skills
      // have MXPENDING status
      QuerySet lQs = execute( iSchedStep_1, ENG );

      assertRow( lQs, FIELD_ENABLED );

      // Asserts the "Mark Step As Not Applicable" option is enabled for PILOT skill since all
      // skills have MXPENDING status
      lQs = execute( iSchedStep_1, PILOT );

      assertRow( lQs, FIELD_ENABLED );
   }


   /**
    * Tests the "Mark Step As Not Applicable" dropdown option is enabled for skill with the latest
    * status of PENDING
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testMarkStepAsNotApplicableVisiblity_CompleteAndPartial() throws Exception {

      // DATA SETUP
      createTask( WITH_SKILLS );

      // DATA SETUP: Sign off ENG skill with PARTIAL status
      captureWorkAndJobStop( iLabour_ENG, iStepSkill_ENG, StepStatus.MXPARTIAL );

      // DATA SETUP: Sign of PILOT skill with PENDING status
      UUID lStepSkill_PILOT = getStepSkill( iReqDefn, PILOT );
      SchedLabourKey lLabour_PILOT = getLabourRequirement( iActualTask, PILOT );

      captureWorkAndJobStop( lLabour_PILOT, lStepSkill_PILOT, StepStatus.MXCOMPLETE );

      // Asserts the "Mark Step As Not Applicable" option is enabled for ENG skill since all skills
      // have MXPENDING status
      QuerySet lQs = execute( iSchedStep_1, ENG );

      assertRow( lQs, FIELD_DISABLED );

      // Asserts the "Mark Step As Not Applicable" option is enabled for PILOT skill since all
      // skills have MXPENDING status
      lQs = execute( iSchedStep_1, PILOT );

      assertRow( lQs, FIELD_DISABLED );
   }


   /**
    * Tests the "Mark Step As Not Applicable" dropdown option is enabled for steps without skills
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testMarkStepAsNotApplicableVisiblity_NoSkills() throws Exception {

      // DATA SETUP
      createTask( NO_SKILLS );

      // DATA SETUP: Sign off ENG skill with PARTIAL status
      JobStopTO lTO = createJobStopTO( iSchedStep_1, null, StepStatus.MXCOMPLETE );

      new WorkCaptureService().captureWork( iLabour_ENG, lTO );

      // Asserts the "Mark Step As Not Applicable" option is enabled
      QuerySet lQs = execute( iSchedStep_1, null );

      assertRow( lQs, FIELD_ENABLED );

      // Asserts the "Mark Step As Not Applicable" option is enabled
      SchedStepKey lSchedStep_2 = new SchedStepKey( iActualTask, 2 );

      lQs = execute( lSchedStep_2, null );

      assertRow( lQs, FIELD_ENABLED );
   }


   /**
    * Tests the "Mark Step As Not Applicable" dropdown option is not visible when the config parm is
    * set to FALSE
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testEnableMarkJobCardStepNotApplicableConfigParm() throws Exception {

      // DATA SETUP
      createTask( WITH_SKILLS );

      // DATA SETUP: Sign off ENG skill with PARTIAL status
      JobStopTO lTO = createJobStopTO( iSchedStep_1, iStepSkill_ENG, StepStatus.MXCOMPLETE );

      new WorkCaptureService().captureWork( iLabour_ENG, lTO );

      // Asserts the "Mark Step As Not Applicable" option is enabled for ENG since it has MXCOMPLETE
      // status
      QuerySet lQs = execute( iSchedStep_1, ENG );

      assertRow( lQs, FIELD_ENABLED );

      // Asserts the "Mark Step As Not Applicable" option is disabled for ENG since it has MXPENDING
      // status
      lQs = execute( iSchedStep_1, PILOT );

      assertRow( lQs, FIELD_DISABLED );

      // DATA SETUP: Change the config parm false
      GlobalParametersFake lConfigParms = new GlobalParametersFake( ParmTypeEnum.MXWEB.name() );
      lConfigParms.setString( "ENABLE_MARK_JOB_CARD_STEP_NOT_APPLICABLE", "FALSE" );
      GlobalParameters.setInstance( lConfigParms );

      // Asserts the "Mark Step As Not Applicable" option is enabled for ENG since it has MXPARTIAL
      // status
      lQs = execute( iSchedStep_1, ENG );

      assertEquals( 3, lQs.getRowCount() );

      while ( lQs.next() ) {
         if ( StepStatus.MXNA.toString().equals( lQs.getString( "ref_step_status_key" ) ) ) {
            fail( "MXNA option should not be visible." );
         }
      }

      // Asserts the "Mark Step As Not Applicable" option is disabled for PILOT skill since it has
      // MXPENDING status
      lQs = execute( iSchedStep_1, PILOT );

      assertEquals( 3, lQs.getRowCount() );

      while ( lQs.next() ) {
         if ( StepStatus.MXNA.toString().equals( lQs.getString( "ref_step_status_key" ) ) ) {
            fail( "MXNA option should not be visible." );
         }
      }
   }


   /**
    * Capture steps and job stop the labour row
    *
    */
   private SchedLabourKey captureWorkAndJobStop( SchedLabourKey aSchedLabour, UUID aStepSkillId,
         StepStatus aStepStatus ) throws Exception {

      JobStopTO lTO = createJobStopTO( iSchedStep_1, aStepSkillId, aStepStatus );

      new WorkCaptureService().captureWork( aSchedLabour, lTO );
      SchedLabourKey lNewSchedLabour = LabourFacade.stop( aSchedLabour, lTO );

      return lNewSchedLabour;
   }


   /**
    * Asserts the visibility of the dropdown option
    *
    */
   private void assertRow( QuerySet lQs, boolean aEnabled ) {

      boolean lFound = false;

      assertEquals( 4, lQs.getRowCount() );

      while ( lQs.next() ) {
         if ( StepStatus.MXNA.toString().equals( lQs.getString( "ref_step_status_key" ) ) ) {

            if ( aEnabled ) {

               assertFalse( lQs.getBoolean( "disabledField" ) );
            } else {
               assertTrue( lQs.getBoolean( "disabledField" ) );
            }

            lFound = true;
         }
      }

      if ( !lFound ) {
         fail( "Expect to see MXNA option in the dropdown list." );
      }
   }


   /**
    * Create the JobStopTO transfer object
    *
    * @return The JobStopTO transfer object
    */
   private JobStopTO createJobStopTO( SchedStepKey aSchedStep, UUID aStepSkillUuid,
         StepStatus aStepStatus ) throws MxException {

      LabourStepTO lLabourStepTO = new LabourStepTO();
      lLabourStepTO.setSchedStep( aSchedStep );
      lLabourStepTO.setNewNote( "Signoff step" );
      lLabourStepTO.setStepSkillId( aStepSkillUuid );
      lLabourStepTO.setLabourStepStatus( aStepStatus );
      lLabourStepTO.setSchedStepRevisionNo( iNextSchedStepRevisionNo++ );
      lLabourStepTO.setRevisionNo( iNextLabourRevisionNo++ );

      JobStopTO lJobStopTO = new JobStopTO();
      lJobStopTO.setTechnician( iTechnician, "HR" );
      lJobStopTO.addLabourStepTOs( Arrays.asList( lLabourStepTO ) );
      lJobStopTO.setAutoCertifyWork( true );
      lJobStopTO.setRemainingHours( 1.0, "Remaining Hours" );

      return lJobStopTO;
   }


   /**
    * Retrieves the labour requirement
    *
    * @return The labour key
    */
   private SchedLabourKey getLabourRequirement( TaskKey aTaskKey, RefLabourSkillKey aLabourSkill ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aTaskKey, "sched_db_id", "sched_id" );
      lArgs.add( aLabourSkill, "labour_skill_db_id", "labour_skill_cd" );

      QuerySet lQs = QuerySetFactory.getInstance().executeQuery( "SCHED_LABOUR", lArgs,
            new String[] { "labour_db_id", "labour_id" } );

      lQs.next();

      return lQs.getKey( SchedLabourKey.class, "labour_db_id", "labour_id" );
   }


   /**
    * Retrieves the step skill id from the task definition
    *
    * @return The step skill id
    */
   private UUID getStepSkill( TaskTaskKey aTaskTask, RefLabourSkillKey aLabourSkill ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aTaskTask, "task_db_id", "task_id" );
      lArgs.add( aLabourSkill, "labour_skill_db_id", "labour_skill_cd" );

      QuerySet lQs = QuerySetFactory.getInstance().executeQuery( "TASK_STEP_SKILL", lArgs,
            new String[] { "step_skill_id" } );

      lQs.next();

      return lQs.getUuid( "step_skill_id" );
   }


   /**
    * Data setup
    */
   private void createTask( boolean aIsWithSkills ) {

      // DATA SETUP: Create requirement definition
      iReqDefn = Domain.createRequirementDefinition( ( RequirementDefinition aRefDefnBuilder ) -> {
         aRefDefnBuilder.setExecutable( true );
         aRefDefnBuilder.addStep( ( Step aStepBuilder ) -> {
            aStepBuilder.setDescription( "Step 1" );

            if ( aIsWithSkills ) {
               aStepBuilder.addStepSkill( ENG, false );
               aStepBuilder.addStepSkill( PILOT, false );
            }
         } );
         aRefDefnBuilder.addStep( ( Step aStepBuilder ) -> {
            aStepBuilder.setDescription( "Step 2" );
         } );
      } );

      // DATA SETUP: Create an actual task
      iActualTask = createRequirement( ( Requirement aBuilder ) -> {
         aBuilder.setDefinition( iReqDefn );
         aBuilder.addLabour( aLabour -> {
            aLabour.setSkill( ENG );
            aLabour.setTechnicianRole( tech -> tech.setScheduledHours( 1 ) );
            aLabour.setCertifierRole( cert -> cert.setScheduledHours( 1 ) );
         } );
         aBuilder.addLabour( aLabour -> {
            aLabour.setSkill( PILOT );
            aLabour.setTechnicianRole( tech -> tech.setScheduledHours( 1 ) );
            aLabour.setCertifierRole( cert -> cert.setScheduledHours( 1 ) );
         } );
      } );

      // DATA SETUP: Create a work package and assign the task to it
      Domain.createWorkPackage( ( aWpBuilder ) -> {
         aWpBuilder.addTask( iActualTask );
         aWpBuilder.setStatus( RefEventStatusKey.IN_WORK );
      } );

      iSchedStep_1 = new SchedStepKey( iActualTask, 1 );

      if ( aIsWithSkills ) {
         iStepSkill_ENG = getStepSkill( iReqDefn, ENG );
      }

      iLabour_ENG = getLabourRequirement( iActualTask, ENG );
   }


   /**
    * This method executes the query in GetStepStatusForStep.qrx
    *
    * @return The dataset after execution.
    */
   private QuerySet execute( SchedStepKey aSchedStep, RefLabourSkillKey aLabourSkill ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aSchedStep, "aSchedDbId", "aSchedId", "aStepId" );
      lArgs.add( aLabourSkill, "aLabourSkillDbId", "aLabourSkillCd" );
      lArgs.add( "aDisplayName", "Mark Step As Not Applicable" );

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

      QuerySet lQs = QuerySetFactory.getInstance().executeQuery( "ORG_HR", lArgs,
            new String[] { "hr_db_id", "hr_id" } );

      if ( !lQs.next() ) {
         Domain.createHumanResource( ( HumanResource aHrBuilder ) -> {
            aHrBuilder.setUser( new UserKey( lCurrentUserId ) );
         } );
      }

      // DATA SETUP: Create a technician
      UserKey lTechnicianUser = Domain.createUser( ( User aUserBuilder ) -> {
         aUserBuilder.setUserId( 10000000 );
      } );

      iTechnician = Domain.createHumanResource( ( HumanResource aHrBuilder ) -> {
         aHrBuilder.setUser( lTechnicianUser );
      } );

      UserParametersFake lUserParms =
            new UserParametersFake( lTechnicianUser.getId(), "SECURED_RESOURCE" );
      UserParameters.setInstance( lTechnicianUser.getId(), "SECURED_RESOURCE", lUserParms );

      // DATA SETUP: Change the config parm value
      GlobalParametersFake lConfigParms = new GlobalParametersFake( ParmTypeEnum.LOGIC.name() );
      lConfigParms.setBoolean( "ENABLE_MULTIPLE_SKILLS_SIGN_OFF_ON_SAME_STEP", true );
      GlobalParameters.setInstance( lConfigParms );
   }
}
