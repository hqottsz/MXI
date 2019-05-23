
package com.mxi.mx.web.query.task;

import static com.mxi.am.domain.Domain.createRequirement;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.List;
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
import com.mxi.mx.core.services.stask.labour.EditWorkCaptureTO;
import com.mxi.mx.core.services.stask.labour.JobStopTO;
import com.mxi.mx.core.services.stask.labour.LabourStepTO;
import com.mxi.mx.core.services.stask.workcapture.WorkCaptureService;
import com.mxi.mx.core.task.model.StepStatus;


/**
 * Integration tests for {@link JobCardStepsDao}
 */
public final class GetAllStepSkills_EditTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   private WorkCaptureService iWorkCaptureService = new WorkCaptureService();

   private static String TECH_NAME = "TECH";

   private static RefLabourSkillKey ENG = RefLabourSkillKey.ENG;

   private static RefLabourSkillKey PILOT = RefLabourSkillKey.PILOT;

   private static boolean ONE_STEP = false;

   private static boolean MULTIPLE_STEPS = true;

   private static boolean NO_SKILL = true;

   private static boolean MULTIPLE_SKILLS = true;

   private static String STEP_1_DESC = "Step 1 description";

   private static String STEP_2_DESC = "Step 2 description";

   private static String SIGN_OFF_NOTE_1_STEP_1 = "Sign off 1 step 1";

   private static String SIGN_OFF_NOTE_2_STEP_1 = "Sign off 2 step 1";

   private static String SIGN_OFF_NOTE_3_STEP_1 = "Sign off 3 step 1";

   private static String EDIT_SIGN_OFF_NOTE_STEP_1 = "Edit Sign off step 1";

   private static String SIGN_OFF_NOTE_1_STEP_2 = "Sign off 1 step 2";

   private static String SIGN_OFF_NOTE_2_STEP_2 = "Sign off 2 step 2";

   private static String EDIT_SIGN_OFF_NOTE_STEP_2 = "Edit Sign off step 2";

   private HumanResourceKey technician;

   private TaskKey actualTask;

   private TaskTaskKey reqDefn;

   private int nextSchedStepRevisionNo = 1;

   private int nextLabourRevisionNo = 1;


   /**
    *
    * Tests edit work capture for baseline task without skills. Two job stops are performed and then
    * the first one is edited.
    */
   @Test
   public void testEditWorkCapture_BaselineTaskWithoutSkills() throws Exception {

      // DATA SETUP
      createTask( MULTIPLE_STEPS, NO_SKILL );

      // DATA RETRIEVAL: Get keys
      SchedLabourKey firstLabourRow = getSchedLabourKey( actualTask, PILOT );
      SchedStepKey schedStep1 = getStepKey( actualTask, 1 );
      SchedStepKey schedStep2 = getStepKey( actualTask, 2 );

      LabourStepTO labourStep1 = new LabourStepTO();
      labourStep1.setSchedStep( schedStep1 );
      labourStep1.setNewNote( SIGN_OFF_NOTE_1_STEP_1 );
      labourStep1.setLabourStepStatus( StepStatus.MXPENDING );

      LabourStepTO labourStep2 = new LabourStepTO();
      labourStep2.setSchedStep( schedStep2 );
      labourStep2.setNewNote( SIGN_OFF_NOTE_1_STEP_2 );
      labourStep2.setLabourStepStatus( StepStatus.MXPENDING );

      // DATA SETUP: Sign off step 1 and 2 first time
      SchedLabourKey secondLabourRow =
            captureWork( firstLabourRow, Arrays.asList( labourStep1, labourStep2 ) );

      labourStep1 = new LabourStepTO();
      labourStep1.setSchedStep( schedStep1 );
      labourStep1.setNewNote( SIGN_OFF_NOTE_2_STEP_1 );
      labourStep1.setLabourStepStatus( StepStatus.MXPARTIAL );

      labourStep2 = new LabourStepTO();
      labourStep2.setSchedStep( schedStep2 );
      labourStep2.setNewNote( SIGN_OFF_NOTE_2_STEP_2 );
      labourStep2.setLabourStepStatus( StepStatus.MXCOMPLETE );

      // DATA SETUP: Sign off step 1 and 2 second time
      captureWork( secondLabourRow, Arrays.asList( labourStep1, labourStep2 ) );

      DataSet ds = execute( actualTask );

      assertEquals( ds.getTotalRowCount(), 4 );

      ds.addSort( "dsString(step_ord)", true );
      ds.addSort( "dsString(ord_id)", true );

      ds.next();
      // because the step 1 and 2 were signed off two times, we should be getting 4 records.
      assertEquals( ds.getInt( "step_ord" ), 1 );
      assertEquals( ds.getInt( "ord_id" ), 1 );
      assertEquals( ds.getString( "notes_ldesc" ), SIGN_OFF_NOTE_1_STEP_1 );
      assertEquals( ds.getString( "performed_by" ), "1. " + TECH_NAME + ", " + TECH_NAME );
      // default_step_skill_status_cd should always return the latest status if labor row is not
      // provided for all records related to step 1
      assertEquals( ds.getString( "default_step_skill_status_cd" ),
            StepStatus.MXPARTIAL.toString() );
      // skill_status returns the step status that was originally selected when the labor row was
      // signed off
      assertEquals( ds.getString( "skill_status" ), StepStatus.MXPENDING.getDisplayName() );
      // because a labor row was not provided (we are not editing), then the
      // current_labour_step_notes should return null
      assertNull( ds.getString( "current_labour_step_notes" ) );

      ds.next();
      assertEquals( ds.getInt( "step_ord" ), 1 );
      assertEquals( ds.getInt( "ord_id" ), 2 );
      assertEquals( ds.getString( "notes_ldesc" ), SIGN_OFF_NOTE_2_STEP_1 );
      assertEquals( ds.getString( "performed_by" ), "2. " + TECH_NAME + ", " + TECH_NAME );
      assertEquals( ds.getString( "default_step_skill_status_cd" ),
            StepStatus.MXPARTIAL.toString() );
      assertEquals( ds.getString( "skill_status" ), StepStatus.MXPARTIAL.getDisplayName() );
      assertNull( ds.getString( "current_labour_step_notes" ) );

      ds.next();
      assertEquals( ds.getInt( "step_ord" ), 2 );
      assertEquals( ds.getInt( "ord_id" ), 1 );
      assertEquals( ds.getString( "notes_ldesc" ), SIGN_OFF_NOTE_1_STEP_2 );
      assertEquals( ds.getString( "performed_by" ), "1. " + TECH_NAME + ", " + TECH_NAME );
      assertEquals( ds.getString( "default_step_skill_status_cd" ),
            StepStatus.MXCOMPLETE.toString() );
      assertEquals( ds.getString( "skill_status" ), StepStatus.MXPENDING.getDisplayName() );
      assertNull( ds.getString( "current_labour_step_notes" ) );

      ds.next();
      assertEquals( ds.getInt( "step_ord" ), 2 );
      assertEquals( ds.getInt( "ord_id" ), 2 );
      assertEquals( ds.getString( "notes_ldesc" ), SIGN_OFF_NOTE_2_STEP_2 );
      assertEquals( ds.getString( "performed_by" ), "2. " + TECH_NAME + ", " + TECH_NAME );
      assertEquals( ds.getString( "default_step_skill_status_cd" ),
            StepStatus.MXCOMPLETE.toString() );
      assertEquals( ds.getString( "skill_status" ), StepStatus.MXCOMPLETE.getDisplayName() );
      assertNull( ds.getString( "current_labour_step_notes" ) );

      // Execute sql to get data for editing the first labor row that was signed off.
      ds = execute( actualTask, firstLabourRow );

      // we still should get 4 records for the two job stops done for step 1 and 2
      assertEquals( ds.getTotalRowCount(), 4 );

      ds.addSort( "dsString(step_ord)", true );
      ds.addSort( "dsString(ord_id)", true );

      ds.next();

      // the different between providing a labor row is that current_labour_step_notes and
      // default_step_skill_status_cd should return the information that was originally provided
      // when the user was doing the job stop for all instances of the same step.
      assertEquals( ds.getInt( "step_ord" ), 1 );
      // should return the status selected when the job step was performed for all instances of this
      // step
      assertEquals( ds.getString( "default_step_skill_status_cd" ),
            StepStatus.MXPENDING.toString() );
      assertEquals( ds.getString( "skill_status" ), StepStatus.MXPENDING.getDisplayName() );
      // should return the notes entered when the job step was performed
      assertEquals( ds.getString( "current_labour_step_notes" ), SIGN_OFF_NOTE_1_STEP_1 );

      ds.next();
      assertEquals( ds.getInt( "step_ord" ), 1 );
      assertEquals( ds.getString( "default_step_skill_status_cd" ),
            StepStatus.MXPENDING.toString() );
      assertEquals( ds.getString( "skill_status" ), StepStatus.MXPARTIAL.getDisplayName() );
      assertEquals( ds.getString( "current_labour_step_notes" ), SIGN_OFF_NOTE_1_STEP_1 );

      ds.next();
      assertEquals( ds.getInt( "step_ord" ), 2 );
      assertEquals( ds.getString( "default_step_skill_status_cd" ),
            StepStatus.MXPENDING.toString() );
      assertEquals( ds.getString( "skill_status" ), StepStatus.MXPENDING.getDisplayName() );
      assertEquals( ds.getString( "current_labour_step_notes" ), SIGN_OFF_NOTE_1_STEP_2 );

      ds.next();
      assertEquals( ds.getInt( "step_ord" ), 2 );
      assertEquals( ds.getString( "default_step_skill_status_cd" ),
            StepStatus.MXPENDING.toString() );
      assertEquals( ds.getString( "skill_status" ), StepStatus.MXCOMPLETE.getDisplayName() );
      assertEquals( ds.getString( "current_labour_step_notes" ), SIGN_OFF_NOTE_1_STEP_2 );

      // DATA SETUP: Edit the first work capture
      LabourStepTO labourStepTO1 = new LabourStepTO();
      labourStepTO1.setSchedStep( schedStep1 );
      labourStepTO1.setNewNote( EDIT_SIGN_OFF_NOTE_STEP_1 );
      labourStepTO1.setLabourStepStatus( StepStatus.MXCOMPLETE );
      labourStepTO1.setSchedStepRevisionNo( nextSchedStepRevisionNo );
      labourStepTO1.setRevisionNo( nextLabourRevisionNo );

      LabourStepTO labourStepTO2 = new LabourStepTO();
      labourStepTO2.setSchedStep( schedStep2 );
      labourStepTO2.setNewNote( EDIT_SIGN_OFF_NOTE_STEP_2 );
      labourStepTO2.setLabourStepStatus( StepStatus.MXPENDING );
      labourStepTO2.setSchedStepRevisionNo( nextSchedStepRevisionNo );
      labourStepTO2.setRevisionNo( nextLabourRevisionNo );

      EditWorkCaptureTO editWorkCaptureTO = new EditWorkCaptureTO();
      editWorkCaptureTO.addLabourStepTOs( Arrays.asList( labourStepTO1, labourStepTO2 ) );

      new WorkCaptureService().editWork( firstLabourRow, editWorkCaptureTO, technician );
      LabourFacade.signOffLabourReq( firstLabourRow, null, technician );

      ds = execute( actualTask );
      // still we should get for rows
      assertEquals( ds.getTotalRowCount(), 4 );

      ds.addSort( "dsString(step_ord)", true );
      ds.addSort( "dsString(ord_id)", true );

      ds.next();
      assertEquals( ds.getInt( "step_ord" ), 1 );
      // check that the notes edited has the new description for step1
      assertEquals( ds.getString( "notes_ldesc" ), EDIT_SIGN_OFF_NOTE_STEP_1 );
      // the status for that job stop at time was modify from pending to complete
      assertEquals( ds.getString( "skill_status" ), StepStatus.MXCOMPLETE.getDisplayName() );

      ds.next();
      assertEquals( ds.getInt( "step_ord" ), 1 );
      assertEquals( ds.getString( "notes_ldesc" ), SIGN_OFF_NOTE_2_STEP_1 );
      assertEquals( ds.getString( "skill_status" ), StepStatus.MXPARTIAL.getDisplayName() );

      ds.next();
      assertEquals( ds.getInt( "step_ord" ), 2 );
      // check that the notes edited has the new description for step2
      assertEquals( ds.getString( "notes_ldesc" ), EDIT_SIGN_OFF_NOTE_STEP_2 );
      assertEquals( ds.getString( "skill_status" ), StepStatus.MXPENDING.getDisplayName() );

      ds.next();
      assertEquals( ds.getInt( "step_ord" ), 2 );
      assertEquals( ds.getString( "notes_ldesc" ), SIGN_OFF_NOTE_2_STEP_2 );
      assertEquals( ds.getString( "skill_status" ), StepStatus.MXCOMPLETE.getDisplayName() );

   }


   /**
    * Test the edit work capture for baseline task with skills. Three job stops are performed and
    * then the first one is edited.
    */
   @Test
   public void testEditWorkCapture_BaselineTaskWithSkills() throws Exception {

      // DATA SETUP
      createTask( ONE_STEP, MULTIPLE_SKILLS );

      // DATA RETRIEVAL: Get keys
      SchedLabourKey firstLabour_ENG = getSchedLabourKey( actualTask, ENG );
      SchedLabourKey labour_PILOT = getSchedLabourKey( actualTask, PILOT );

      SchedStepKey schedStep = getStepKey( actualTask, 1 );
      UUID stepSkillENG = getStepSkillId( reqDefn, ENG );

      // *******************************************************************************
      // Sign off the twice for labor row - ENG and once for PILOT
      // Execute query without labor row id to get the latest status for all steps
      // *******************************************************************************
      SchedLabourKey secondLabour_ENG = captureWork( firstLabour_ENG, schedStep, stepSkillENG, ENG,
            StepStatus.MXPARTIAL, SIGN_OFF_NOTE_1_STEP_1 );

      captureWork( secondLabour_ENG, schedStep, stepSkillENG, ENG, StepStatus.MXCOMPLETE,
            SIGN_OFF_NOTE_2_STEP_1 );

      UUID stepSkillPILOT = getStepSkillId( reqDefn, PILOT );

      captureWork( labour_PILOT, schedStep, stepSkillPILOT, PILOT, StepStatus.MXCOMPLETE,
            SIGN_OFF_NOTE_3_STEP_1 );

      DataSet ds = execute( actualTask );

      assertEquals( ds.getTotalRowCount(), 3 );

      // Steps should be ordered alphabetically by ord_id and skill
      ds.next();
      assertEquals( ds.getInt( "step_ord" ), 1 );
      assertEquals( ds.getInt( "ord_id" ), 1 );
      assertEquals( ds.getString( "notes_ldesc" ), SIGN_OFF_NOTE_1_STEP_1 );
      assertEquals( ds.getString( "performed_by" ), "1. " + TECH_NAME + ", " + TECH_NAME );
      assertEquals( ds.getString( "default_step_skill_status_cd" ),
            StepStatus.MXCOMPLETE.toString() );
      assertEquals( ds.getString( "skill_status" ), StepStatus.MXPARTIAL.getDisplayName() );
      assertNull( ds.getString( "current_labour_step_notes" ) );
      assertEquals( ds.getString( "labour_skill_cd" ), ENG.getCd() );

      ds.next();
      assertEquals( ds.getInt( "step_ord" ), 1 );
      assertEquals( ds.getInt( "ord_id" ), 2 );
      assertEquals( ds.getString( "notes_ldesc" ), SIGN_OFF_NOTE_2_STEP_1 );
      assertEquals( ds.getString( "performed_by" ), "2. " + TECH_NAME + ", " + TECH_NAME );
      assertEquals( ds.getString( "default_step_skill_status_cd" ),
            StepStatus.MXCOMPLETE.toString() );
      assertEquals( ds.getString( "skill_status" ), StepStatus.MXCOMPLETE.getDisplayName() );
      assertNull( ds.getString( "current_labour_step_notes" ) );
      assertEquals( ds.getString( "labour_skill_cd" ), ENG.getCd() );

      ds.next();
      assertEquals( ds.getInt( "step_ord" ), 1 );
      assertEquals( ds.getInt( "ord_id" ), 3 );
      assertEquals( ds.getString( "notes_ldesc" ), SIGN_OFF_NOTE_3_STEP_1 );
      assertEquals( ds.getString( "performed_by" ), "1. " + TECH_NAME + ", " + TECH_NAME );
      assertEquals( ds.getString( "default_step_skill_status_cd" ),
            StepStatus.MXCOMPLETE.toString() );
      assertEquals( ds.getString( "skill_status" ), StepStatus.MXCOMPLETE.getDisplayName() );
      assertNull( ds.getString( "current_labour_step_notes" ) );
      assertEquals( ds.getString( "labour_skill_cd" ), PILOT.getCd() );

      // *******************************************************************************
      // Execute query providing first labor row signed off for editing
      // *******************************************************************************

      ds = execute( actualTask, firstLabour_ENG );

      assertEquals( ds.getTotalRowCount(), 3 );

      ds.next();
      assertEquals( ds.getInt( "step_ord" ), 1 );
      assertEquals( ds.getInt( "ord_id" ), 1 );
      assertEquals( ds.getString( "notes_ldesc" ), SIGN_OFF_NOTE_1_STEP_1 );
      assertEquals( ds.getString( "performed_by" ), "1. " + TECH_NAME + ", " + TECH_NAME );
      assertEquals( ds.getString( "default_step_skill_status_cd" ),
            StepStatus.MXPARTIAL.toString() );
      assertEquals( ds.getString( "skill_status" ), StepStatus.MXPARTIAL.getDisplayName() );
      // all records related to step 1 should return SIGN_OFF_NOTE_1_STEP_1 for
      // current_labour_step_notes
      assertEquals( ds.getString( "current_labour_step_notes" ), SIGN_OFF_NOTE_1_STEP_1 );
      assertEquals( ds.getString( "labour_skill_cd" ), ENG.getCd() );

      ds.next();
      assertEquals( ds.getInt( "step_ord" ), 1 );
      assertEquals( ds.getInt( "ord_id" ), 2 );
      assertEquals( ds.getString( "notes_ldesc" ), SIGN_OFF_NOTE_2_STEP_1 );
      assertEquals( ds.getString( "performed_by" ), "2. " + TECH_NAME + ", " + TECH_NAME );
      assertEquals( ds.getString( "default_step_skill_status_cd" ),
            StepStatus.MXPARTIAL.toString() );
      assertEquals( ds.getString( "skill_status" ), StepStatus.MXCOMPLETE.getDisplayName() );
      // all records related to step 1 should return SIGN_OFF_NOTE_1_STEP_1 for
      // current_labour_step_notes
      assertEquals( ds.getString( "current_labour_step_notes" ), SIGN_OFF_NOTE_1_STEP_1 );
      assertEquals( ds.getString( "labour_skill_cd" ), ENG.getCd() );

      ds.next();
      assertEquals( ds.getInt( "step_ord" ), 1 );
      assertEquals( ds.getInt( "ord_id" ), 3 );
      assertEquals( ds.getString( "notes_ldesc" ), SIGN_OFF_NOTE_3_STEP_1 );
      assertEquals( ds.getString( "performed_by" ), "1. " + TECH_NAME + ", " + TECH_NAME );
      assertEquals( ds.getString( "default_step_skill_status_cd" ),
            StepStatus.MXCOMPLETE.toString() );
      // all records related to step 1 should return SIGN_OFF_NOTE_1_STEP_1 for
      // current_labour_step_notes
      assertEquals( ds.getString( "skill_status" ), StepStatus.MXCOMPLETE.getDisplayName() );
      assertEquals( ds.getString( "current_labour_step_notes" ), SIGN_OFF_NOTE_1_STEP_1 );
      assertEquals( ds.getString( "labour_skill_cd" ), PILOT.getCd() );

      // Edit the first work capture
      LabourStepTO labourStepTO1 = new LabourStepTO();
      labourStepTO1.setSchedStep( schedStep );
      labourStepTO1.setNewNote( EDIT_SIGN_OFF_NOTE_STEP_1 );
      labourStepTO1.setLabourStepStatus( StepStatus.MXPENDING );
      labourStepTO1.setSchedStepRevisionNo( nextSchedStepRevisionNo );
      labourStepTO1.setRevisionNo( nextLabourRevisionNo );
      labourStepTO1.setStepSkillId( stepSkillENG );

      EditWorkCaptureTO editWorkCaptureTO = new EditWorkCaptureTO();
      editWorkCaptureTO.addLabourStepTOs( Arrays.asList( labourStepTO1 ) );

      new WorkCaptureService().editWork( firstLabour_ENG, editWorkCaptureTO, technician );
      LabourFacade.signOffLabourReq( firstLabour_ENG, null, technician );

      ds = execute( actualTask );
      // still we should get for rows
      assertEquals( ds.getTotalRowCount(), 3 );

      ds.next();
      assertEquals( ds.getInt( "ord_id" ), 1 );
      // check that the notes edited has the new description for step1
      assertEquals( ds.getString( "notes_ldesc" ), EDIT_SIGN_OFF_NOTE_STEP_1 );
      // the status for that job stop at time was modify from partial to pending
      assertEquals( ds.getString( "skill_status" ), StepStatus.MXPENDING.getDisplayName() );

      ds.next();
      assertEquals( ds.getInt( "ord_id" ), 2 );
      assertEquals( ds.getString( "notes_ldesc" ), SIGN_OFF_NOTE_2_STEP_1 );
      assertEquals( ds.getString( "skill_status" ), StepStatus.MXCOMPLETE.getDisplayName() );

      ds.next();
      assertEquals( ds.getInt( "ord_id" ), 3 );
      assertEquals( ds.getString( "notes_ldesc" ), SIGN_OFF_NOTE_3_STEP_1 );
      assertEquals( ds.getString( "skill_status" ), StepStatus.MXCOMPLETE.getDisplayName() );

   }


   private SchedLabourKey getSchedLabourKey( TaskKey taskKey, RefLabourSkillKey labourSkill ) {

      DataSetArgument args = new DataSetArgument();
      args.add( taskKey, "sched_db_id", "sched_id" );
      args.add( labourSkill, "labour_skill_db_id", "labour_skill_cd" );

      QuerySet qs = QuerySetFactory.getInstance().executeQuery( "SCHED_LABOUR", args,
            new String[] { "labour_db_id", "labour_id" } );

      qs.next();

      return qs.getKey( SchedLabourKey.class, "labour_db_id", "labour_id" );
   }


   private DataSet execute( TaskKey task ) {
      return execute( task, null );
   }


   /**
    * This method executes the query in GetAllStepSkills.qrx
    *
    *
    * @return The dataset after execution.
    */
   private DataSet execute( TaskKey task, SchedLabourKey schedLabour ) {

      DataSetArgument args = new DataSetArgument();
      args.add( task, "aSchedDbId", "aSchedId" );
      if ( schedLabour != null ) {
         args.add( schedLabour, "aLabourDbId", "aLabourId" );
      }

      return QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            "com.mxi.mx.web.query.task.GetAllStepSkills", args );
   }


   private void createTask( boolean multipleSteps, boolean multipleSkills ) {
      createTask( multipleSteps, multipleSkills, false );
   }


   private void createTask( boolean multipleSteps, boolean multipleSkills, boolean noSkill ) {

      // DATA SETUP: Create requirement definition with steps that have skills
      reqDefn = Domain.createRequirementDefinition( ( RequirementDefinition refDefnBuilder ) -> {
         refDefnBuilder.setExecutable( true );
         refDefnBuilder.addStep( ( Step stepBuilder ) -> {
            stepBuilder.setDescription( STEP_1_DESC );

            if ( !noSkill ) {
               stepBuilder.addStepSkill( PILOT, false );

               if ( multipleSkills ) {
                  stepBuilder.addStepSkill( ENG, false );
               }
            }
         } );

         if ( multipleSteps ) {
            refDefnBuilder.addStep( ( Step stepBuilder ) -> {
               stepBuilder.setDescription( STEP_2_DESC );

               if ( !noSkill ) {
                  stepBuilder.addStepSkill( ENG, false );
               }
            } );
         }
      } );

      // DATA SETUP: Create an actual task
      actualTask = createRequirement( ( Requirement builder ) -> {
         builder.setDefinition( reqDefn );
         builder.addLabour( labour -> {
            labour.setSkill( PILOT );
            labour.setTechnicianRole( tech -> tech.setScheduledHours( 1 ) );
            labour.setCertifierRole( cert -> cert.setScheduledHours( 1 ) );
         } );
         builder.addLabour( labour -> {
            labour.setSkill( ENG );
            labour.setTechnicianRole( tech -> tech.setScheduledHours( 1 ) );
            labour.setCertifierRole( cert -> cert.setScheduledHours( 1 ) );
         } );
      } );

      // DATA SETUP: Create a work package and assign the task to it
      Domain.createWorkPackage( ( wpBuRilder ) -> {
         wpBuRilder.addTask( actualTask );
         wpBuRilder.setStatus( RefEventStatusKey.IN_WORK );
      } );

   }


   private SchedStepKey getStepKey( TaskKey task, int stepOrd ) {
      DataSetArgument args = new DataSetArgument();
      args.add( task, "sched_db_id", "sched_id" );
      args.add( "step_ord", stepOrd );

      QuerySet qs = QuerySetFactory.getInstance().executeQuery( "SCHED_STEP", args,
            new String[] { "sched_db_id", "sched_id", "step_id" } );

      qs.next();

      return qs.getKey( SchedStepKey.class, "sched_db_id", "sched_id", "step_id" );
   }


   private UUID getStepSkillId( TaskTaskKey taskTask, RefLabourSkillKey labourSkill ) {

      DataSetArgument args = new DataSetArgument();
      args.add( taskTask, "task_db_id", "task_id" );
      args.add( labourSkill, "labour_skill_db_id", "labour_skill_cd" );

      QuerySet qs = QuerySetFactory.getInstance().executeQuery( "TASK_STEP_SKILL", args,
            new String[] { "step_skill_id" } );

      qs.next();

      return qs.getUuid( "step_skill_id" );
   }


   private SchedLabourKey captureWork( SchedLabourKey schedLabour, SchedStepKey schedStep,
         UUID stepSkillId, RefLabourSkillKey labourSkill, StepStatus stepStatus, String note )
         throws Exception {

      LabourStepTO labourStepTO = new LabourStepTO();
      labourStepTO.setSchedStep( schedStep );
      labourStepTO.setNewNote( note );
      labourStepTO.setStepSkillId( stepSkillId );
      labourStepTO.setLabourStepStatus( stepStatus );

      return captureWork( schedLabour, Arrays.asList( labourStepTO ) );
   }


   private SchedLabourKey captureWork( SchedLabourKey schedLabour, LabourStepTO labourStep )
         throws Exception {
      return captureWork( schedLabour, Arrays.asList( labourStep ) );
   }


   private SchedLabourKey captureWork( SchedLabourKey schedLabour, List<LabourStepTO> labourSteps )
         throws Exception {

      labourSteps.forEach( ls -> {
         ls.setSchedStepRevisionNo( nextSchedStepRevisionNo++ );
         ls.setRevisionNo( nextLabourRevisionNo++ );
      } );

      JobStopTO jobStopTO = new JobStopTO();
      jobStopTO.setTechnician( technician, "HR" );
      jobStopTO.setRemainingHours( 1.0, "remaining hours" );
      jobStopTO.addLabourStepTOs( labourSteps );
      jobStopTO.setAutoCertifyWork( true );

      iWorkCaptureService.captureWork( schedLabour, jobStopTO );

      SchedLabourKey newSchedLabour = LabourFacade.stop( schedLabour, jobStopTO );

      return newSchedLabour;
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

      UserKey userKey = Domain.createUser( ( userBuilder ) -> {
         userBuilder.setUserId( currentUserId );
         userBuilder.setFirstName( TECH_NAME );
         userBuilder.setLastName( TECH_NAME );
         userBuilder.setUsername( TECH_NAME );
      } );

      QuerySet qs = QuerySetFactory.getInstance().executeQuery( "ORG_HR", args,
            new String[] { "hr_db_id", "hr_id" } );

      if ( userQs.next() ) {
         technician = qs.getKey( HumanResourceKey.class, "hr_db_id", "hr_id" );

      } else {
         technician = Domain.createHumanResource( ( HumanResource aHrBuilder ) -> {
            aHrBuilder.setUser( new UserKey( currentUserId ) );
            aHrBuilder.setUser( userKey );
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
