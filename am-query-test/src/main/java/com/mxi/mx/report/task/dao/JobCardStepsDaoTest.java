package com.mxi.mx.report.task.dao;

import static com.mxi.am.domain.Domain.createRequirement;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
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
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
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
import com.mxi.mx.core.reports.task.dao.JobCardStepsDao;
import com.mxi.mx.core.reports.task.model.JobCardStepsSubReport;
import com.mxi.mx.core.reports.task.model.StepSkillsSubReport;
import com.mxi.mx.core.services.stask.labour.EditWorkCaptureTO;
import com.mxi.mx.core.services.stask.labour.JobStopTO;
import com.mxi.mx.core.services.stask.labour.LabourStepTO;
import com.mxi.mx.core.services.stask.workcapture.WorkCaptureService;
import com.mxi.mx.core.task.model.StepStatus;


/**
 * Integration tests for {@link JobCardStepsDao}
 */
public final class JobCardStepsDaoTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   private JobCardStepsDao iJobCardStepsDao = new JobCardStepsDao();

   private WorkCaptureService iWorkCaptureService = new WorkCaptureService();

   private static RefLabourSkillKey ENG = RefLabourSkillKey.ENG;

   private static RefLabourSkillKey PILOT = RefLabourSkillKey.PILOT;

   private static boolean HAS_CURRENT_SIGNED_OFF_INFO = true;

   private static boolean HAS_NO_CURRENT_SIGNED_OFF_INFO = false;

   private static boolean HAS_PREV_SIGNED_OFF_INFO = true;

   private static boolean HAS_NO_PREV_SIGNED_OFF_INFO = false;

   private static boolean ONE_STEP = false;

   private static boolean MULTIPLE_STEPS = true;

   private static boolean ONE_SKILL = false;

   private static boolean NO_SKILL = true;

   private static boolean MULTIPLE_SKILLS = true;

   private static String STEP_1_DESC = "Step 1 description";

   private static String STEP_2_DESC = "Step 2 description";

   private static String SIGN_OFF_NOTE_1 = "Sign off step 1";

   private static String SIGN_OFF_NOTE_2 = "Sign off step 2";

   private static String EDIT_SIGN_OFF_NOTE_1 = "Edit Sign off step 1";

   private static String PENDING = "Pending";

   private static String PARTIAL = "Partial";

   private static String COMPLETE = "Complete";

   private static boolean JOB_STOP = true;

   private static boolean NO_JOB_STOP = false;

   private HumanResourceKey iTechnician;

   private TaskKey iActualTask;

   private TaskTaskKey iReqDefn;

   private int iNextSchedStepRevisionNo = 1;

   private int iNextLabourRevisionNo = 1;


   /**
    *
    * Tests a step with multiple step skills
    *
    * DATA SETUP:<br>
    * Step 1 <br>
    * ..... Skill ENG<br>
    * ..... Skill PILOT
    *
    * TEST 1: Sign off the ENG labor row<br>
    * Expected result:<br>
    * Step skill ENG<br>
    * - Highlight note<br>
    * - Highlight status<br>
    * - Highlight signed by<br>
    * Step Skill PILOT<br>
    * - No highlight for any fields.<br>
    *
    * TEST 2: Sign off the PILOT labor row<br>
    * Expected result:<br>
    * Step skill ENG<br>
    * - No highlight for any fields.<br>
    * - Display previously signed by info <br>
    * Step Skill PILOT<br>
    * - Highlight note<br>
    * - Highlight status<br>
    * - Highlight signed by<br>
    *
    * @throws Exception
    */
   @Test
   public void testJobStop_OneStepWithMultipleSkills() throws Exception {

      // DATA SETUP
      createTask( ONE_STEP, MULTIPLE_SKILLS );

      // DATA RETRIEVAL: Get keys
      SchedLabourKey lLabour_ENG = getSchedLabourKey( iActualTask, ENG );
      SchedStepKey lSchedStep = getStepKey( iActualTask, 1 );
      UUID lStepSkillId = getStepSkillId( iReqDefn, ENG );

      // *******************************************************************************
      // TEST CASE : 1
      // Sign off the first labour row ENG with PARTIAL status
      // *******************************************************************************
      captureWork( NO_JOB_STOP, lLabour_ENG, lSchedStep, lStepSkillId, ENG, StepStatus.MXPARTIAL,
            SIGN_OFF_NOTE_1 );

      JobCardStepsSubReport lStepReport = getStepReport( lLabour_ENG );

      // asserts that there are two step skills
      assertEquals( 2, lStepReport.getJobCardStepSkill().size() );

      // assert the step information
      StepReportRecord lExpectedStepResult =
            new StepReportRecord().withStepOrd( 1 ).withStepDescription( STEP_1_DESC )
                  .withStepStatus( PARTIAL ).withIsAdhoc( false ).withInspReq( "No" );

      assertStepRow( lStepReport, lExpectedStepResult );

      // asserts the step skill ENG info
      StepSkillsSubReport lStepSkillReport = lStepReport.getJobCardStepSkill().get( 0 );

      StepSkillReportRecord lExpectedSkillResult =
            new StepSkillReportRecord().withSkillCd( ENG.getCd() ).withSkillStatus( PARTIAL )
                  .hasCurrSignedBy( HAS_CURRENT_SIGNED_OFF_INFO )
                  .hasPrevSignedBy( HAS_NO_PREV_SIGNED_OFF_INFO ).withStatusNote( SIGN_OFF_NOTE_1 )
                  .withNoteHighlightBool( true ).withStatusHighlightBool( true )
                  .withUserHighlightBool( true );

      assertStepSkillRow( lStepSkillReport, lExpectedSkillResult );

      // asserts the step skill PILOT info
      lStepSkillReport = lStepReport.getJobCardStepSkill().get( 1 );

      lExpectedSkillResult = new StepSkillReportRecord().withSkillCd( PILOT.getCd() )
            .withSkillStatus( PENDING ).hasCurrSignedBy( HAS_NO_CURRENT_SIGNED_OFF_INFO )
            .hasPrevSignedBy( HAS_NO_PREV_SIGNED_OFF_INFO ).withStatusNote( null )
            .withNoteHighlightBool( false ).withStatusHighlightBool( false )
            .withUserHighlightBool( false );

      assertStepSkillRow( lStepSkillReport, lExpectedSkillResult );

      // *******************************************************************************
      // TEST CASE : 2
      // Sign off the labour row PILOT with Complete status
      // *******************************************************************************
      SchedLabourKey lLabour_PILOT = getSchedLabourKey( iActualTask, PILOT );
      lStepSkillId = getStepSkillId( iReqDefn, PILOT );

      captureWork( NO_JOB_STOP, lLabour_PILOT, lSchedStep, lStepSkillId, PILOT,
            StepStatus.MXCOMPLETE, SIGN_OFF_NOTE_2 );

      lStepReport = getStepReport( lLabour_PILOT );

      // asserts that there are two step skills
      assertEquals( 2, lStepReport.getJobCardStepSkill().size() );

      // asserts the step info
      lExpectedStepResult =
            new StepReportRecord().withStepOrd( 1 ).withStepDescription( STEP_1_DESC )
                  .withStepStatus( PARTIAL ).withIsAdhoc( false ).withInspReq( "No" );

      assertStepRow( lStepReport, lExpectedStepResult );

      // asserts the step skill ENG info
      lStepSkillReport = lStepReport.getJobCardStepSkill().get( 0 );

      lExpectedSkillResult = new StepSkillReportRecord().withSkillCd( ENG.getCd() )
            .withSkillStatus( PARTIAL ).hasCurrSignedBy( HAS_NO_CURRENT_SIGNED_OFF_INFO )
            .hasPrevSignedBy( HAS_PREV_SIGNED_OFF_INFO ).withStatusNote( null )
            .withNoteHighlightBool( false ).withStatusHighlightBool( false )
            .withUserHighlightBool( false );

      assertStepSkillRow( lStepSkillReport, lExpectedSkillResult );

      // asserts the step skill PILOT info
      lStepSkillReport = lStepReport.getJobCardStepSkill().get( 1 );

      lExpectedSkillResult = new StepSkillReportRecord().withSkillCd( PILOT.getCd() )
            .withSkillStatus( COMPLETE ).hasCurrSignedBy( HAS_CURRENT_SIGNED_OFF_INFO )
            .hasPrevSignedBy( HAS_NO_PREV_SIGNED_OFF_INFO ).withStatusNote( SIGN_OFF_NOTE_2 )
            .withNoteHighlightBool( true ).withStatusHighlightBool( true )
            .withUserHighlightBool( true );

      assertStepSkillRow( lStepSkillReport, lExpectedSkillResult );
   }


   /**
    *
    * Tests multiple steps that have only one step skill
    *
    * DATA SETUP:<br>
    * Step 1 <br>
    * ..... Skill ENG<br>
    * Step 2 <br>
    * ..... Skill PILOT<br>
    *
    * TEST 1: Sign off the ENG labor row for Step 1 with step skill status Partial<br>
    * Expected result:<br>
    * Step skill ENG<br>
    * - Highlight note<br>
    * - Highlight status<br>
    * - Highlight signed by<br>
    * Step Skill PILOT<br>
    * - No highlight for any fields.<br>
    *
    *
    * TEST 2: Sign off the PILOT labor row for Step 2 with step skill remains unchanged (ie
    * Pending)<br>
    * Expected result:<br>
    * Step skill ENG<br>
    * - No highlight for any fields.<br>
    * - Display previously signed by info <br>
    * Step Skill PILOT<br>
    * - Highlight note<br>
    * - Status is not highlighted<br>
    * - Highlight signed by<br>
    *
    * @throws Exception
    */
   @Test
   public void testJobStop_MultipleStepsWithOneSkill() throws Exception {

      // DATA SETUP
      createTask( MULTIPLE_STEPS, ONE_SKILL );

      // DATA RETRIEVAL: Get keys
      SchedLabourKey lLabour_PILOT = getSchedLabourKey( iActualTask, PILOT );
      SchedStepKey lSchedStep = getStepKey( iActualTask, 1 );
      UUID lStepSkillId = getStepSkillId( iReqDefn, PILOT );

      // *******************************************************************************
      // TEST CASE : 1
      // Sign off the first labour row PILOT with PARTIAL status
      // *******************************************************************************
      captureWork( NO_JOB_STOP, lLabour_PILOT, lSchedStep, lStepSkillId, PILOT,
            StepStatus.MXPARTIAL, SIGN_OFF_NOTE_1 );

      List<JobCardStepsSubReport> lStepsSubReports =
            iJobCardStepsDao.getSteps( iActualTask, Arrays.asList( lLabour_PILOT ) );

      // asserts that there are 2 steps
      assertEquals( 2, lStepsSubReports.size() );

      // asserts step 1 info
      JobCardStepsSubReport lStepReport = lStepsSubReports.get( 0 );

      StepReportRecord lExpectedStepResult =
            new StepReportRecord().withStepOrd( 1 ).withStepDescription( STEP_1_DESC )
                  .withStepStatus( PARTIAL ).withIsAdhoc( false ).withInspReq( "No" );

      assertStepRow( lStepReport, lExpectedStepResult );

      // asserts that the step skill PILOT
      StepSkillsSubReport lStepSkillReport = lStepReport.getJobCardStepSkill().get( 0 );

      StepSkillReportRecord lExpectedSkillResult =
            new StepSkillReportRecord().withSkillCd( PILOT.getCd() ).withSkillStatus( PARTIAL )
                  .hasCurrSignedBy( HAS_CURRENT_SIGNED_OFF_INFO )
                  .hasPrevSignedBy( HAS_NO_PREV_SIGNED_OFF_INFO ).withStatusNote( SIGN_OFF_NOTE_1 )
                  .withNoteHighlightBool( true ).withStatusHighlightBool( true )
                  .withUserHighlightBool( true );

      assertStepSkillRow( lStepSkillReport, lExpectedSkillResult );

      // assert steps 2 info
      lStepReport = lStepsSubReports.get( 1 );

      // asserts that the step skill ENG
      lExpectedStepResult =
            new StepReportRecord().withStepOrd( 2 ).withStepDescription( STEP_2_DESC )
                  .withStepStatus( PENDING ).withIsAdhoc( false ).withInspReq( "No" );

      assertStepRow( lStepReport, lExpectedStepResult );

      lStepSkillReport = lStepReport.getJobCardStepSkill().get( 0 );

      lExpectedSkillResult = new StepSkillReportRecord().withSkillCd( ENG.getCd() )
            .withSkillStatus( PENDING ).hasCurrSignedBy( HAS_NO_CURRENT_SIGNED_OFF_INFO )
            .hasPrevSignedBy( HAS_NO_PREV_SIGNED_OFF_INFO ).withStatusNote( null )
            .withNoteHighlightBool( false ).withStatusHighlightBool( false )
            .withUserHighlightBool( false );

      assertStepSkillRow( lStepSkillReport, lExpectedSkillResult );

      // *******************************************************************************
      // TEST CASE : 2
      // Sign off the second labour row ENG with a note and the status remains unchanged
      // *******************************************************************************

      lSchedStep = getStepKey( iActualTask, 2 );
      lStepSkillId = getStepSkillId( iReqDefn, ENG );
      SchedLabourKey lLabour_ENG = getSchedLabourKey( iActualTask, ENG );

      captureWork( NO_JOB_STOP, lLabour_ENG, lSchedStep, lStepSkillId, ENG, StepStatus.MXPENDING,
            SIGN_OFF_NOTE_2 );

      // asserts there are 2 steps
      lStepsSubReports = iJobCardStepsDao.getSteps( iActualTask, Arrays.asList( lLabour_ENG ) );

      assertEquals( 2, lStepsSubReports.size() );

      // asserts step 1 info
      lStepReport = lStepsSubReports.get( 0 );

      lExpectedStepResult =
            new StepReportRecord().withStepOrd( 1 ).withStepDescription( STEP_1_DESC )
                  .withStepStatus( PARTIAL ).withIsAdhoc( false ).withInspReq( "No" );

      assertStepRow( lStepReport, lExpectedStepResult );

      lExpectedSkillResult = new StepSkillReportRecord().withSkillCd( PILOT.getCd() )
            .withSkillStatus( PARTIAL ).hasCurrSignedBy( HAS_NO_CURRENT_SIGNED_OFF_INFO )
            .hasPrevSignedBy( HAS_PREV_SIGNED_OFF_INFO ).withStatusNote( null )
            .withNoteHighlightBool( false ).withStatusHighlightBool( false )
            .withUserHighlightBool( false );

      lStepSkillReport = lStepReport.getJobCardStepSkill().get( 0 );

      assertStepSkillRow( lStepSkillReport, lExpectedSkillResult );

      // asserts step 2 info
      lStepReport = lStepsSubReports.get( 1 );

      // Assert that the note is highlighted and status is not highlighted
      lExpectedStepResult =
            new StepReportRecord().withStepOrd( 2 ).withStepDescription( STEP_2_DESC )
                  .withStepStatus( PENDING ).withIsAdhoc( false ).withInspReq( "No" );

      assertStepRow( lStepReport, lExpectedStepResult );

      lExpectedSkillResult = new StepSkillReportRecord().withSkillCd( ENG.getCd() )
            .withSkillStatus( PENDING ).hasCurrSignedBy( HAS_CURRENT_SIGNED_OFF_INFO )
            .hasPrevSignedBy( HAS_NO_PREV_SIGNED_OFF_INFO ).withStatusNote( SIGN_OFF_NOTE_2 )
            .withNoteHighlightBool( true ).withStatusHighlightBool( false )
            .withUserHighlightBool( true );

      lStepSkillReport = lStepReport.getJobCardStepSkill().get( 0 );

      assertStepSkillRow( lStepSkillReport, lExpectedSkillResult );
   }


   /**
    *
    * Tests edit work capture
    *
    * DATA SETUP:<br>
    * Step 1 <br>
    * ..... Skill PILOT
    *
    * Sign off the PILOT labor row<br>
    * Expected result:<br>
    * Step skill ENG<br>
    * - Highlight note<br>
    * - Highlight status<br>
    * - Highlight signed by<br>
    *
    *
    * Edit the work capture<br>
    * Expected result:<br>
    * Step skill ENG<br>
    * - Note is updated and is highlighted<br>
    * - Highlight status<br>
    * - No highlight signed by<br>
    *
    * @throws Exception
    */
   @Test
   public void testEditWorkCapture() throws Exception {

      // DATA SETUP
      createTask( ONE_STEP, ONE_SKILL );

      // DATA RETRIEVAL: Get keys
      SchedLabourKey lLabour_PILOT = getSchedLabourKey( iActualTask, PILOT );
      SchedStepKey lSchedStep = getStepKey( iActualTask, 1 );
      UUID lStepSkillId = getStepSkillId( iReqDefn, PILOT );

      // DATA SETUP: Sign off step skill PILOT
      captureWork( JOB_STOP, lLabour_PILOT, lSchedStep, lStepSkillId, PILOT, StepStatus.MXPARTIAL,
            SIGN_OFF_NOTE_1 );

      JobCardStepsSubReport lStepReport = getStepReport( lLabour_PILOT );

      // asserts that there is only one step skill
      assertEquals( 1, lStepReport.getJobCardStepSkill().size() );

      // asserts the step info
      StepReportRecord lExpectedStepResult =
            new StepReportRecord().withStepOrd( 1 ).withStepDescription( STEP_1_DESC )
                  .withStepStatus( PARTIAL ).withIsAdhoc( false ).withInspReq( "No" );

      assertStepRow( lStepReport, lExpectedStepResult );

      // asserts the step skill info
      StepSkillsSubReport lStepSkillReport = lStepReport.getJobCardStepSkill().get( 0 );

      StepSkillReportRecord lExpectedSkillResult =
            new StepSkillReportRecord().withSkillCd( PILOT.getCd() ).withSkillStatus( PARTIAL )
                  .hasCurrSignedBy( HAS_CURRENT_SIGNED_OFF_INFO )
                  .hasPrevSignedBy( HAS_NO_PREV_SIGNED_OFF_INFO ).withStatusNote( SIGN_OFF_NOTE_1 )
                  .withNoteHighlightBool( true ).withStatusHighlightBool( true )
                  .withUserHighlightBool( true );

      assertStepSkillRow( lStepSkillReport, lExpectedSkillResult );

      // DATA SETUP: Edit the work capture
      LabourStepTO lLabourStepTO = new LabourStepTO();
      lLabourStepTO.setSchedStep( lSchedStep );
      lLabourStepTO.setNewNote( EDIT_SIGN_OFF_NOTE_1 );
      lLabourStepTO.setLabourStepStatus( StepStatus.MXPARTIAL );
      lLabourStepTO.setSchedStepRevisionNo( iNextSchedStepRevisionNo++ );
      lLabourStepTO.setRevisionNo( iNextLabourRevisionNo++ );
      lLabourStepTO.setStepSkillId( lStepSkillId );

      EditWorkCaptureTO lEditWorkCaptureTO = new EditWorkCaptureTO();
      lEditWorkCaptureTO.addLabourStepTOs( Arrays.asList( lLabourStepTO ) );

      new WorkCaptureService().editWork( lLabour_PILOT, lEditWorkCaptureTO, iTechnician );

      lStepReport = getStepReport( lLabour_PILOT );

      // asserts that the step skill info is updated
      // asserts that the signed by info does not get highlighted in edit work capture mode
      lStepSkillReport = lStepReport.getJobCardStepSkill().get( 0 );

      lExpectedSkillResult = new StepSkillReportRecord().withSkillCd( PILOT.getCd() )
            .withSkillStatus( PARTIAL ).hasCurrSignedBy( HAS_CURRENT_SIGNED_OFF_INFO )
            .hasPrevSignedBy( HAS_NO_PREV_SIGNED_OFF_INFO ).withStatusNote( EDIT_SIGN_OFF_NOTE_1 )
            .withNoteHighlightBool( true ).withStatusHighlightBool( true )
            .withUserHighlightBool( false );

      assertStepSkillRow( lStepSkillReport, lExpectedSkillResult );
   }


   @Test
   public void testStepWithNoSkills() throws Exception {

      createTask( MULTIPLE_STEPS, false, NO_SKILL );

      // *******************************************************************************
      // TEST CASE 1: Sign off Step 1 with Partial status
      // ******************************************************************************

      // DATA RETRIEVAL: Get keys
      SchedLabourKey lLabour_PILOT = getSchedLabourKey( iActualTask, PILOT );
      SchedStepKey lSchedStep_1 = getStepKey( iActualTask, 1 );

      // DATA SETUP: Sign off step 1
      SchedLabourKey lNewSchedLabour = captureWork( JOB_STOP, lLabour_PILOT, lSchedStep_1, null,
            PILOT, StepStatus.MXPARTIAL, SIGN_OFF_NOTE_1 );

      List<JobCardStepsSubReport> lStepReport =
            iJobCardStepsDao.getSteps( iActualTask, Arrays.asList( lLabour_PILOT ) );

      // asserts that there are two steps
      assertEquals( 2, lStepReport.size() );

      // asserts the step 1 info
      StepReportRecord lExpectedStepResult =
            new StepReportRecord().withStepOrd( 1 ).withStepDescription( STEP_1_DESC )
                  .withStepStatus( PARTIAL ).withIsAdhoc( false ).withInspReq( "No" );

      assertStepRow( lStepReport.get( 0 ), lExpectedStepResult );

      StepSkillsSubReport lStepSkillReport = lStepReport.get( 0 ).getJobCardStepSkill().get( 0 );

      StepSkillReportRecord lExpectedSkillResult = new StepSkillReportRecord().withSkillCd( null )
            .withSkillStatus( PARTIAL ).hasCurrSignedBy( HAS_CURRENT_SIGNED_OFF_INFO )
            .hasPrevSignedBy( HAS_NO_PREV_SIGNED_OFF_INFO ).withStatusNote( SIGN_OFF_NOTE_1 )
            .withNoteHighlightBool( true ).withStatusHighlightBool( true )
            .withUserHighlightBool( true );

      assertStepSkillRow( lStepSkillReport, lExpectedSkillResult );

      // asserts the step 2 info
      lExpectedStepResult =
            new StepReportRecord().withStepOrd( 2 ).withStepDescription( STEP_2_DESC )
                  .withStepStatus( PENDING ).withIsAdhoc( false ).withInspReq( "No" );

      assertStepRow( lStepReport.get( 1 ), lExpectedStepResult );

      assertTrue( lStepReport.get( 1 ).getJobCardStepSkill().isEmpty() );

      // *******************************************************************************
      // TEST CASE 2: Sign off Step 1 again with no status change
      // ******************************************************************************

      // DATA SETUP: Sign off step 1
      SchedLabourKey lNewSchedLabour2 = captureWork( JOB_STOP, lNewSchedLabour, lSchedStep_1, null,
            PILOT, StepStatus.MXPARTIAL, SIGN_OFF_NOTE_2 );

      lStepReport = iJobCardStepsDao.getSteps( iActualTask, Arrays.asList( lNewSchedLabour ) );

      // asserts that there are two steps
      assertEquals( 2, lStepReport.size() );

      // asserts the step 1 info
      lExpectedStepResult =
            new StepReportRecord().withStepOrd( 1 ).withStepDescription( STEP_1_DESC )
                  .withStepStatus( PARTIAL ).withIsAdhoc( false ).withInspReq( "No" );

      assertStepRow( lStepReport.get( 0 ), lExpectedStepResult );

      lStepSkillReport = lStepReport.get( 0 ).getJobCardStepSkill().get( 0 );

      lExpectedSkillResult = new StepSkillReportRecord().withSkillCd( null )
            .withSkillStatus( PARTIAL ).hasCurrSignedBy( HAS_CURRENT_SIGNED_OFF_INFO )
            .hasPrevSignedBy( HAS_PREV_SIGNED_OFF_INFO ).withStatusNote( SIGN_OFF_NOTE_2 )
            .withNoteHighlightBool( true ).withStatusHighlightBool( false )
            .withUserHighlightBool( true );

      assertStepSkillRow( lStepSkillReport, lExpectedSkillResult );

      // asserts the step 2 info
      lExpectedStepResult =
            new StepReportRecord().withStepOrd( 2 ).withStepDescription( STEP_2_DESC )
                  .withStepStatus( PENDING ).withIsAdhoc( false ).withInspReq( "No" );

      assertStepRow( lStepReport.get( 1 ), lExpectedStepResult );

      assertTrue( lStepReport.get( 1 ).getJobCardStepSkill().isEmpty() );

      // *******************************************************************************
      // TEST CASE 3: Sign off Step 2 with no status change (ie Pending)
      // *******************************************************************************
      SchedStepKey lSchedStep_2 = getStepKey( iActualTask, 2 );

      // DATA SETUP: Sign off step 1
      SchedLabourKey lNewSchedLabour3 = captureWork( JOB_STOP, lNewSchedLabour2, lSchedStep_2, null,
            PILOT, StepStatus.MXPENDING, SIGN_OFF_NOTE_1 );

      lStepReport = iJobCardStepsDao.getSteps( iActualTask, Arrays.asList( lNewSchedLabour2 ) );

      // asserts that there are two steps
      assertEquals( 2, lStepReport.size() );

      // asserts the step 1 info
      lExpectedStepResult =
            new StepReportRecord().withStepOrd( 1 ).withStepDescription( STEP_1_DESC )
                  .withStepStatus( PARTIAL ).withIsAdhoc( false ).withInspReq( "No" );

      assertStepRow( lStepReport.get( 0 ), lExpectedStepResult );

      lStepSkillReport = lStepReport.get( 0 ).getJobCardStepSkill().get( 0 );

      lExpectedSkillResult = new StepSkillReportRecord().withSkillCd( null )
            .withSkillStatus( PARTIAL ).hasCurrSignedBy( HAS_NO_CURRENT_SIGNED_OFF_INFO )
            .hasPrevSignedBy( HAS_PREV_SIGNED_OFF_INFO ).withStatusNote( null )
            .withNoteHighlightBool( false ).withStatusHighlightBool( false )
            .withUserHighlightBool( false );

      assertStepSkillRow( lStepSkillReport, lExpectedSkillResult );

      // asserts the step 2 info
      lExpectedStepResult =
            new StepReportRecord().withStepOrd( 2 ).withStepDescription( STEP_2_DESC )
                  .withStepStatus( PENDING ).withIsAdhoc( false ).withInspReq( "No" );

      assertStepRow( lStepReport.get( 1 ), lExpectedStepResult );

      lStepSkillReport = lStepReport.get( 1 ).getJobCardStepSkill().get( 0 );

      lExpectedSkillResult = new StepSkillReportRecord().withSkillCd( null )
            .withSkillStatus( PENDING ).hasCurrSignedBy( HAS_CURRENT_SIGNED_OFF_INFO )
            .hasPrevSignedBy( HAS_NO_PREV_SIGNED_OFF_INFO ).withStatusNote( SIGN_OFF_NOTE_1 )
            .withNoteHighlightBool( true ).withStatusHighlightBool( false )
            .withUserHighlightBool( true );

      assertStepSkillRow( lStepSkillReport, lExpectedSkillResult );

      // *******************************************************************************
      // TEST CASE 4: Sign off Step 1 and change status to Complete
      // *******************************************************************************

      // DATA SETUP: Sign off step 1
      captureWork( JOB_STOP, lNewSchedLabour3, lSchedStep_2, null, PILOT, StepStatus.MXCOMPLETE,
            SIGN_OFF_NOTE_2 );

      lStepReport = iJobCardStepsDao.getSteps( iActualTask, Arrays.asList( lNewSchedLabour3 ) );

      // asserts that there are two step
      assertEquals( 2, lStepReport.size() );

      // asserts the step 1 info
      lExpectedStepResult =
            new StepReportRecord().withStepOrd( 1 ).withStepDescription( STEP_1_DESC )
                  .withStepStatus( PARTIAL ).withIsAdhoc( false ).withInspReq( "No" );

      assertStepRow( lStepReport.get( 0 ), lExpectedStepResult );

      lStepSkillReport = lStepReport.get( 0 ).getJobCardStepSkill().get( 0 );

      lExpectedSkillResult = new StepSkillReportRecord().withSkillCd( null )
            .withSkillStatus( PARTIAL ).hasCurrSignedBy( HAS_NO_CURRENT_SIGNED_OFF_INFO )
            .hasPrevSignedBy( HAS_PREV_SIGNED_OFF_INFO ).withStatusNote( null )
            .withNoteHighlightBool( false ).withStatusHighlightBool( false )
            .withUserHighlightBool( false );

      assertStepSkillRow( lStepSkillReport, lExpectedSkillResult );

      // asserts the step 2 info
      lExpectedStepResult =
            new StepReportRecord().withStepOrd( 2 ).withStepDescription( STEP_2_DESC )
                  .withStepStatus( COMPLETE ).withIsAdhoc( false ).withInspReq( "No" );

      assertStepRow( lStepReport.get( 1 ), lExpectedStepResult );

      lStepSkillReport = lStepReport.get( 1 ).getJobCardStepSkill().get( 0 );

      lExpectedSkillResult = new StepSkillReportRecord().withSkillCd( null )
            .withSkillStatus( COMPLETE ).hasCurrSignedBy( HAS_CURRENT_SIGNED_OFF_INFO )
            .hasPrevSignedBy( HAS_PREV_SIGNED_OFF_INFO ).withStatusNote( SIGN_OFF_NOTE_2 )
            .withNoteHighlightBool( true ).withStatusHighlightBool( true )
            .withUserHighlightBool( true );

      assertStepSkillRow( lStepSkillReport, lExpectedSkillResult );

   }


   /**
    *
    * Tests multiple job stops on the same step skill
    *
    * DATA SETUP:<br>
    * Step 1 <br>
    * ..... Skill PILOT<br>
    * ..... Skill ENG
    *
    * TEST 1: Sign off the first PILOT labor row. Add a note but status remains unchanged<br>
    * Expected result:<br>
    * - Highlight note<br>
    * - Status is not highlighted<br>
    * - Highlight signed by<br>
    *
    *
    * TEST 2: Sign off the ENG labor row. Add note only and change status<br>
    * Expected result:<br>
    * - Highlight note<br>
    * - Highlight status<br>
    * - Highlight signed by<br>
    *
    * TEST 3: Sign off the second PILOT labor row. Add note only and status remain unchanged<br>
    * Expected result:<br>
    * - Highlight note<br>
    * - Status is not highlighted<br>
    * - Highlight signed by<br>
    *
    *
    * TEST 4: Sign off the second ENG labor row. Add note only and status remain unchanged<br>
    * Expected result:<br>
    * - Highlight note<br>
    * - Status is not highlighted<br>
    * - Highlight signed by<br>
    *
    * @throws Exception
    */
   @Test
   public void testMultipleJobStopsOnMultipleStepSkills() throws Exception {

      // DATA SETUP
      createTask( ONE_STEP, MULTIPLE_SKILLS );

      // DATA RETRIEVAL: Get keys
      SchedLabourKey lLabour_PILOT = getSchedLabourKey( iActualTask, PILOT );
      SchedStepKey lSchedStep = getStepKey( iActualTask, 1 );
      UUID lStepSkillId_PILOT = getStepSkillId( iReqDefn, PILOT );

      // *******************************************************************************
      // TEST CASE: 1
      // Sign off the first PILOT labor row with no status change.
      // *******************************************************************************

      // DATA SETUP: Job stop a labor row
      SchedLabourKey lNewSchedLabour_PILOT = captureWork( JOB_STOP, lLabour_PILOT, lSchedStep,
            lStepSkillId_PILOT, PILOT, StepStatus.MXPENDING, SIGN_OFF_NOTE_1 );

      JobCardStepsSubReport lStepReport = getStepReport( lLabour_PILOT );

      // assert step skill ENG
      StepSkillsSubReport lStepSkillReport = lStepReport.getJobCardStepSkill().get( 0 );

      StepSkillReportRecord lExpectedSkillResult =
            new StepSkillReportRecord().withSkillCd( ENG.getCd() ).withSkillStatus( PENDING )
                  .hasCurrSignedBy( HAS_NO_CURRENT_SIGNED_OFF_INFO )
                  .hasPrevSignedBy( HAS_NO_PREV_SIGNED_OFF_INFO ).withStatusNote( null )
                  .withNoteHighlightBool( false ).withStatusHighlightBool( false )
                  .withUserHighlightBool( false );

      assertStepSkillRow( lStepSkillReport, lExpectedSkillResult );

      // asserts step skill PILOT
      lStepSkillReport = lStepReport.getJobCardStepSkill().get( 1 );

      lExpectedSkillResult = new StepSkillReportRecord().withSkillCd( PILOT.getCd() )
            .withSkillStatus( PENDING ).hasCurrSignedBy( HAS_CURRENT_SIGNED_OFF_INFO )
            .hasPrevSignedBy( HAS_NO_PREV_SIGNED_OFF_INFO ).withStatusNote( SIGN_OFF_NOTE_1 )
            .withNoteHighlightBool( true ).withStatusHighlightBool( false )
            .withUserHighlightBool( true );

      assertStepSkillRow( lStepSkillReport, lExpectedSkillResult );

      // *******************************************************************************
      // TEST CASE: 2
      // Sign off the first ENG labor row with new status COMPLETE
      // *******************************************************************************

      SchedLabourKey lLabour_ENG = getSchedLabourKey( iActualTask, ENG );
      UUID lStepSkillId_ENG = getStepSkillId( iReqDefn, ENG );

      // DATA SETUP: Job stop a labor row
      SchedLabourKey lNewSchedLabour_ENG = captureWork( JOB_STOP, lLabour_ENG, lSchedStep,
            lStepSkillId_ENG, ENG, StepStatus.MXCOMPLETE, SIGN_OFF_NOTE_1 );

      lStepReport = getStepReport( lLabour_ENG );

      // assert step skill ENG
      lStepSkillReport = lStepReport.getJobCardStepSkill().get( 0 );

      lExpectedSkillResult = new StepSkillReportRecord().withSkillCd( ENG.getCd() )
            .withSkillStatus( COMPLETE ).hasCurrSignedBy( HAS_CURRENT_SIGNED_OFF_INFO )
            .hasPrevSignedBy( HAS_NO_PREV_SIGNED_OFF_INFO ).withStatusNote( SIGN_OFF_NOTE_1 )
            .withNoteHighlightBool( true ).withStatusHighlightBool( true )
            .withUserHighlightBool( true );

      assertStepSkillRow( lStepSkillReport, lExpectedSkillResult );

      // asserts step skill PILOT
      lStepSkillReport = lStepReport.getJobCardStepSkill().get( 1 );

      lExpectedSkillResult = new StepSkillReportRecord().withSkillCd( PILOT.getCd() )
            .withSkillStatus( PENDING ).hasCurrSignedBy( HAS_NO_CURRENT_SIGNED_OFF_INFO )
            .hasPrevSignedBy( HAS_PREV_SIGNED_OFF_INFO ).withStatusNote( null )
            .withNoteHighlightBool( false ).withStatusHighlightBool( false )
            .withUserHighlightBool( false );

      assertStepSkillRow( lStepSkillReport, lExpectedSkillResult );

      // *******************************************************************************
      // TEST CASE: 3
      // Sign off the second PILOT labor row with no status change
      // *******************************************************************************

      // DATA SETUP: Job stop the second labor row of PILOT skill
      captureWork( JOB_STOP, lNewSchedLabour_PILOT, lSchedStep, lStepSkillId_PILOT, PILOT,
            StepStatus.MXPENDING, SIGN_OFF_NOTE_2 );

      lStepReport = getStepReport( lNewSchedLabour_PILOT );

      // asserts the step skill ENG
      lStepSkillReport = lStepReport.getJobCardStepSkill().get( 0 );

      lExpectedSkillResult = new StepSkillReportRecord().withSkillCd( ENG.getCd() )
            .withSkillStatus( COMPLETE ).hasCurrSignedBy( HAS_NO_CURRENT_SIGNED_OFF_INFO )
            .hasPrevSignedBy( HAS_PREV_SIGNED_OFF_INFO ).withStatusNote( null )
            .withNoteHighlightBool( false ).withStatusHighlightBool( false )
            .withUserHighlightBool( false );

      assertStepSkillRow( lStepSkillReport, lExpectedSkillResult );

      // asserts the step skill PILOT
      lStepSkillReport = lStepReport.getJobCardStepSkill().get( 1 );

      lExpectedSkillResult = new StepSkillReportRecord().withSkillCd( PILOT.getCd() )
            .withSkillStatus( PENDING ).hasCurrSignedBy( HAS_CURRENT_SIGNED_OFF_INFO )
            .hasPrevSignedBy( HAS_PREV_SIGNED_OFF_INFO ).withStatusNote( SIGN_OFF_NOTE_2 )
            .withNoteHighlightBool( true ).withStatusHighlightBool( false )
            .withUserHighlightBool( true );

      assertStepSkillRow( lStepSkillReport, lExpectedSkillResult );

      // *******************************************************************************
      // TEST CASE: 4
      // Sign off the second ENG labor row with no status change
      // *******************************************************************************

      // DATA SETUP: Job stop the second labor row for ENG skill
      captureWork( JOB_STOP, lNewSchedLabour_ENG, lSchedStep, lStepSkillId_ENG, ENG,
            StepStatus.MXCOMPLETE, SIGN_OFF_NOTE_2 );

      lStepReport = getStepReport( lNewSchedLabour_ENG );

      // asserts the step skill ENG
      lStepSkillReport = lStepReport.getJobCardStepSkill().get( 0 );

      lExpectedSkillResult = new StepSkillReportRecord().withSkillCd( ENG.getCd() )
            .withSkillStatus( COMPLETE ).hasCurrSignedBy( HAS_CURRENT_SIGNED_OFF_INFO )
            .hasPrevSignedBy( HAS_PREV_SIGNED_OFF_INFO ).withStatusNote( SIGN_OFF_NOTE_2 )
            .withNoteHighlightBool( true ).withStatusHighlightBool( false )
            .withUserHighlightBool( true );

      assertStepSkillRow( lStepSkillReport, lExpectedSkillResult );

      // asserts the step skill PILOT
      lStepSkillReport = lStepReport.getJobCardStepSkill().get( 1 );

      lExpectedSkillResult = new StepSkillReportRecord().withSkillCd( PILOT.getCd() )
            .withSkillStatus( PENDING ).hasCurrSignedBy( HAS_NO_CURRENT_SIGNED_OFF_INFO )
            .hasPrevSignedBy( HAS_PREV_SIGNED_OFF_INFO ).withStatusNote( null )
            .withNoteHighlightBool( false ).withStatusHighlightBool( false )
            .withUserHighlightBool( false );

      assertStepSkillRow( lStepSkillReport, lExpectedSkillResult );
   }


   private SchedLabourKey getSchedLabourKey( TaskKey lTaskKey, RefLabourSkillKey aLabourSkill ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lTaskKey, "sched_db_id", "sched_id" );
      lArgs.add( aLabourSkill, "labour_skill_db_id", "labour_skill_cd" );

      QuerySet lQs = QuerySetFactory.getInstance().executeQuery( "SCHED_LABOUR", lArgs,
            new String[] { "labour_db_id", "labour_id" } );

      lQs.next();

      return lQs.getKey( SchedLabourKey.class, "labour_db_id", "labour_id" );
   }


   private void createTask( boolean aMultipleSteps, boolean aMultipleSkills ) {
      createTask( aMultipleSteps, aMultipleSkills, false );
   }


   private void createTask( boolean aMultipleSteps, boolean aMultipleSkills, boolean aNoSkill ) {

      // DATA SETUP: Create requirement definition with steps that have skills
      iReqDefn = Domain.createRequirementDefinition( ( RequirementDefinition aRefDefnBuilder ) -> {
         aRefDefnBuilder.setExecutable( true );
         aRefDefnBuilder.addStep( ( Step aStepBuilder ) -> {
            aStepBuilder.setDescription( STEP_1_DESC );

            if ( !aNoSkill ) {
               aStepBuilder.addStepSkill( PILOT, false );

               if ( aMultipleSkills ) {
                  aStepBuilder.addStepSkill( ENG, false );
               }
            }
         } );

         if ( aMultipleSteps ) {
            aRefDefnBuilder.addStep( ( Step aStepBuilder ) -> {
               aStepBuilder.setDescription( STEP_2_DESC );

               if ( !aNoSkill ) {
                  aStepBuilder.addStepSkill( ENG, false );
               }
            } );
         }
      } );

      // DATA SETUP: Create an actual task
      iActualTask = createRequirement( ( Requirement aBuilder ) -> {
         aBuilder.setDefinition( iReqDefn );
         aBuilder.addLabour( aLabour -> {
            aLabour.setSkill( PILOT );
            aLabour.setTechnicianRole( tech -> tech.setScheduledHours( 1 ) );
            aLabour.setCertifierRole( cert -> cert.setScheduledHours( 1 ) );
         } );
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

   }


   private void assertStepSkillRow( StepSkillsSubReport aStepSkillReport,
         StepSkillReportRecord aExpectedResult ) {

      assertEquals( aExpectedResult.getSkillCd(), aStepSkillReport.getSkillCd() );
      assertEquals( aExpectedResult.getSkillStatus(), aStepSkillReport.getStepSkillStatus() );

      if ( aExpectedResult.getCurrSignedBy() ) {
         assertFalse( aStepSkillReport.getCurrSignedBy().isEmpty() );
      } else {
         assertTrue( aStepSkillReport.getCurrSignedBy().isEmpty() );
      }

      if ( aExpectedResult.getPrevSignedBy() ) {
         assertFalse( aStepSkillReport.getPrevSignedBy().isEmpty() );
      } else {
         assertTrue( aStepSkillReport.getPrevSignedBy().isEmpty() );
      }

      assertEquals( aExpectedResult.getStatusNote(), aStepSkillReport.getStatusNote() );
      assertEquals( aExpectedResult.getNoteHighlightBool(),
            aStepSkillReport.getNoteHighlightBool() );
      assertEquals( aExpectedResult.getStatusHighlightBool(),
            aStepSkillReport.getStatusHighlightBool() );
   }


   private void assertStepRow( JobCardStepsSubReport aStepReport,
         StepReportRecord aExpectedResult ) {

      assertEquals( String.valueOf( aExpectedResult.getStepOrd() ), aStepReport.getStepOrd() );
      assertEquals( aExpectedResult.getStepDescription(), aStepReport.getStepDescription() );
      assertEquals( aExpectedResult.getStepStatus(), aStepReport.getStepStatus() );
      assertEquals( aExpectedResult.isIsAdhoc(), aStepReport.getIsAdhoc() );
   }


   private SchedStepKey getStepKey( TaskKey aTask, int aStepOrd ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aTask, "sched_db_id", "sched_id" );
      lArgs.add( "step_ord", aStepOrd );

      QuerySet lQs = QuerySetFactory.getInstance().executeQuery( "SCHED_STEP", lArgs,
            new String[] { "sched_db_id", "sched_id", "step_id" } );

      lQs.next();

      return lQs.getKey( SchedStepKey.class, "sched_db_id", "sched_id", "step_id" );
   }


   private UUID getStepSkillId( TaskTaskKey aTaskTask, RefLabourSkillKey aLabourSkill ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aTaskTask, "task_db_id", "task_id" );
      lArgs.add( aLabourSkill, "labour_skill_db_id", "labour_skill_cd" );

      QuerySet lQs = QuerySetFactory.getInstance().executeQuery( "TASK_STEP_SKILL", lArgs,
            new String[] { "step_skill_id" } );

      lQs.next();

      return lQs.getUuid( "step_skill_id" );
   }


   private SchedLabourKey captureWork( boolean aJobStop, SchedLabourKey aSchedLabour,
         SchedStepKey aSchedStep, UUID aStepSkillId, RefLabourSkillKey aLabourSkill,
         StepStatus aStepStatus, String aNote ) throws Exception {

      SchedLabourKey lNewSchedLabour = null;

      LabourStepTO lLabourStepTO = new LabourStepTO();
      lLabourStepTO.setSchedStep( aSchedStep );
      lLabourStepTO.setNewNote( aNote );
      lLabourStepTO.setStepSkillId( aStepSkillId );
      lLabourStepTO.setLabourStepStatus( aStepStatus );
      lLabourStepTO.setSchedStepRevisionNo( iNextSchedStepRevisionNo++ );
      lLabourStepTO.setRevisionNo( iNextLabourRevisionNo++ );

      JobStopTO lTO = new JobStopTO();
      lTO.setTechnician( iTechnician, "HR" );
      lTO.setRemainingHours( 1.0, "remaining hours" );
      lTO.addLabourStepTOs( Arrays.asList( lLabourStepTO ) );
      lTO.setAutoCertifyWork( true );

      iWorkCaptureService.captureWork( aSchedLabour, lTO );

      if ( aJobStop ) {
         lNewSchedLabour = LabourFacade.stop( aSchedLabour, lTO );

      }

      return lNewSchedLabour;
   }


   private JobCardStepsSubReport getStepReport( SchedLabourKey aSchedLabour ) {

      List<JobCardStepsSubReport> lStepsSubReports =
            iJobCardStepsDao.getSteps( iActualTask, Arrays.asList( aSchedLabour ) );

      // asserts that there is only one step
      assertEquals( 1, lStepsSubReports.size() );

      return lStepsSubReports.get( 0 );
   }


   public class StepSkillReportRecord {

      private String iSkillCd;
      private String iSkillStatus;
      private boolean iHasPrevSignedByInfo;
      private boolean iHasCurrSignedByInfo;
      private String iStatusNote;
      private boolean iNoteHighlightBool;
      private boolean iStatusHighlightBool;
      private boolean iUserHighlightBool;


      public String getSkillCd() {
         return iSkillCd;
      }


      public StepSkillReportRecord withSkillCd( String aSkillCd ) {
         iSkillCd = aSkillCd;
         return this;
      }


      public String getSkillStatus() {
         return iSkillStatus;
      }


      public StepSkillReportRecord withSkillStatus( String aSkillStatus ) {
         iSkillStatus = aSkillStatus;
         return this;
      }


      public boolean getNoteHighlightBool() {
         return iNoteHighlightBool;
      }


      public StepSkillReportRecord withNoteHighlightBool( boolean aNoteHighlightBool ) {
         iNoteHighlightBool = aNoteHighlightBool;
         return this;
      }


      public boolean getUserHighlightBool() {
         return iUserHighlightBool;
      }


      public StepSkillReportRecord withUserHighlightBool( boolean aUserHighlightBool ) {
         iUserHighlightBool = aUserHighlightBool;
         return this;
      }


      public boolean getStatusHighlightBool() {
         return iStatusHighlightBool;
      }


      public StepSkillReportRecord withStatusHighlightBool( boolean aStatusHighlightBool ) {
         iStatusHighlightBool = aStatusHighlightBool;
         return this;
      }


      public boolean getPrevSignedBy() {
         return iHasPrevSignedByInfo;
      }


      public StepSkillReportRecord hasPrevSignedBy( boolean aHasPrevSignedByInfo ) {
         iHasPrevSignedByInfo = aHasPrevSignedByInfo;
         return this;
      }


      public boolean getCurrSignedBy() {
         return iHasCurrSignedByInfo;
      }


      public StepSkillReportRecord hasCurrSignedBy( boolean aHasCurrSignedByInfo ) {
         iHasCurrSignedByInfo = aHasCurrSignedByInfo;
         return this;
      }


      public String getStatusNote() {
         return iStatusNote;
      }


      public StepSkillReportRecord withStatusNote( String aStatusNote ) {
         iStatusNote = aStatusNote;
         return this;
      }

   }

   public class StepReportRecord {

      private int iStepOrd;
      private String iStepDescription;
      private String iStepStatus;
      private boolean iIsAdhoc;
      private String iInspReq;


      public StepReportRecord() {
      }


      public int getStepOrd() {
         return iStepOrd;
      }


      public StepReportRecord withStepOrd( int aStepOrd ) {
         iStepOrd = aStepOrd;
         return this;
      }


      public String getStepDescription() {
         return iStepDescription;
      }


      public StepReportRecord withStepDescription( String aStepDescription ) {
         iStepDescription = aStepDescription;
         return this;
      }


      public String getStepStatus() {
         return iStepStatus;
      }


      public StepReportRecord withStepStatus( String aStepStatus ) {
         iStepStatus = aStepStatus;
         return this;
      }


      public boolean isIsAdhoc() {
         return iIsAdhoc;
      }


      public StepReportRecord withIsAdhoc( boolean aIsAdhoc ) {
         iIsAdhoc = aIsAdhoc;
         return this;
      }


      public String getInspReq() {
         return iInspReq;
      }


      public StepReportRecord withInspReq( String aInspReq ) {
         iInspReq = aInspReq;
         return this;
      }
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
