package com.mxi.mx.core.services.stask.workcapture;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.HumanResource;
import com.mxi.am.domain.Labour;
import com.mxi.am.domain.LabourRequirement;
import com.mxi.am.domain.Organization;
import com.mxi.am.domain.Requirement;
import com.mxi.am.domain.RequirementDefinition;
import com.mxi.am.domain.Step;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.RefLabourRoleStatusKey;
import com.mxi.mx.core.key.RefLabourRoleTypeKey;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.RefOrgTypeKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.SchedLabourKey;
import com.mxi.mx.core.key.SchedLabourStepKey;
import com.mxi.mx.core.key.SchedStepKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskStepKey;
import com.mxi.mx.core.key.TaskStepSkillKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.key.UserKey;
import com.mxi.mx.core.services.stask.labour.LabourStepTO;
import com.mxi.mx.core.table.sched.SchedLabourRoleStatusAccessor;
import com.mxi.mx.core.table.sched.SchedLabourRoleStatusTable;
import com.mxi.mx.core.table.sched.SchedLabourStepTable;
import com.mxi.mx.core.table.sched.SchedStepTable;
import com.mxi.mx.core.table.task.JdbcTaskStepSkillDao;
import com.mxi.mx.core.table.task.TaskStepSkillTable;
import com.mxi.mx.core.task.model.StepStatus;
import com.mxi.mx.testing.DataRecordUtil;


/**
 * Unit test suite for the {@link BaselinedTaskLaborStepSkillCompleteByUserValidator} class. A
 * single user cannot set a skill status to complete or partial for a skill on a particular step if
 * they have already set a skill status to complete or partial on that step for a different skill.
 * An informative error message is presented to the user if they attempt to do so
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class BaselinedTaskLaborStepSkillCompleteByUserValidatorTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();
   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();
   private LabourRequirement iLabourReq_PILOT;
   private LabourRequirement iLabourReq_ENG;
   private TaskTaskKey iReqDefn;
   private HumanResourceKey iTechnician1;
   private HumanResourceKey iTechnician2;
   private DomainConfiguration<Labour> iLabour_PILOT;
   private DomainConfiguration<Labour> iLabour_ENG;


   private void createRequirement() {

      // DATA SETUP: Create a requirement definition with 2 steps that has 2 skills
      iReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.setTaskClass( RefTaskClassKey.REQ );
                  aReqDefn.setExecutable( true );
                  aReqDefn.addLabourRequirement( iLabourReq_PILOT );
                  aReqDefn.addLabourRequirement( iLabourReq_ENG );

                  aReqDefn.addStep( new DomainConfiguration<Step>() {

                     @Override
                     public void configure( Step aStep ) {

                        aStep.addStepSkill( iLabourReq_PILOT.getLabourSkill(), true );
                        aStep.addStepSkill( iLabourReq_ENG.getLabourSkill(), true );
                     }

                  } );

                  aReqDefn.addStep( new DomainConfiguration<Step>() {

                     @Override
                     public void configure( Step aStep ) {

                        aStep.addStepSkill( iLabourReq_PILOT.getLabourSkill(), true );
                        aStep.addStepSkill( iLabourReq_ENG.getLabourSkill(), true );
                     }

                  } );
               }
            } );

   }


   @Before
   public void setUp() {

      iLabourReq_PILOT = new LabourRequirement( RefLabourSkillKey.PILOT, new BigDecimal( 2 ),
            new BigDecimal( 2 ), null );

      iLabourReq_ENG = new LabourRequirement( RefLabourSkillKey.ENG, new BigDecimal( 2 ),
            new BigDecimal( 1 ), new BigDecimal( 1 ) );

      // Query requires task definition to be associated with an organization and have an
      // organization type
      final OrgKey lOrgkey = Domain.createOrganization( new DomainConfiguration<Organization>() {

         @Override
         public void configure( Organization aBuilder ) {
            aBuilder.setType( RefOrgTypeKey.MRO );
         }
      } );

      final UserKey lUser1 = Domain.createUser();
      iTechnician1 = Domain.createHumanResource( new DomainConfiguration<HumanResource>() {

         @Override
         public void configure( HumanResource aBuilder ) {
            aBuilder.setOrganization( lOrgkey );
            aBuilder.setUser( lUser1 );
         }
      } );

      final UserKey lUser2 = Domain.createUser();
      iTechnician2 = Domain.createHumanResource( new DomainConfiguration<HumanResource>() {

         @Override
         public void configure( HumanResource aBuilder ) {
            aBuilder.setOrganization( lOrgkey );
            aBuilder.setUser( lUser2 );
         }
      } );

      iLabour_PILOT = new DomainConfiguration<Labour>() {

         @Override
         public void configure( Labour aLabour ) {
            aLabour.setSkill( RefLabourSkillKey.PILOT );
            aLabour.setTechnicianRole( tech -> tech.setScheduledHours( 2 ) );
         }

      };

      iLabour_ENG = new DomainConfiguration<Labour>() {

         @Override
         public void configure( Labour aLabour ) {
            aLabour.setSkill( RefLabourSkillKey.ENG );
            aLabour.setTechnicianRole( tech -> tech.setScheduledHours( 2 ) );
         }

      };

   }


   /**
    * Ensure the validator does not throw a UserCannotCompleteDifferentStepSkillsException when the
    * when a user is captured a step skill for the step that has another skill already completed (
    * COMPLETE, PARTIAL) by another user.
    *
    */
   @Test
   public void testBaselinedTaskSkillUserCanComplete() throws MxException {

      /*************************************************************
       * Scenario 1: a user is captured a step skill for the step that has another skill already
       * completed ( COMPLETE, PARTIAL) by another user.
       *********************************************/
      // DATA SETUP: Create requirement definition
      createRequirement();

      // DATA SETUP: Create an actual task
      TaskKey lActualTask = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aRequirement ) {
            aRequirement.setDefinition( iReqDefn );
            aRequirement.addLabour( iLabour_PILOT );

            // Labors for ENG skill:
            aRequirement.addLabour( iLabour_ENG );

         }
      } );

      // RETRIEVE DATA: Get SchedLabourkey
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lActualTask, "sched_db_id", "sched_id" );
      lArgs.add( RefLabourSkillKey.PILOT, "labour_skill_db_id", "labour_skill_cd" );

      QuerySet lQs = QuerySetFactory.getInstance().executeQuery( "SCHED_LABOUR", lArgs,
            new String[] { "labour_db_id", "labour_id" } );

      // capture the first step
      lQs.next();
      SchedLabourKey lSchedLabour = lQs.getKey( SchedLabourKey.class, "labour_db_id", "labour_id" );

      captureStep( lActualTask, lSchedLabour, 1, 1, StepStatus.MXPARTIAL, RefLabourSkillKey.PILOT );

      signOffLabour( RefLabourRoleTypeKey.TECH, lSchedLabour, iTechnician1 );

      try {

         // DATA SETUP: Sign off labor row with ENG skill with job step skill ENG for the task
         LabourStepTO lLabourStepTO = new LabourStepTO();
         SchedStepKey lSchedStep = new SchedStepKey( lActualTask, 1 );
         lLabourStepTO.setSchedStep( lSchedStep );
         lLabourStepTO.setLabourStepStatus( StepStatus.MXPARTIAL );
         BaselinedTaskLaborStepSkillCompleteByUserValidator.validate( iTechnician2,
               RefLabourSkillKey.ENG, Arrays.asList( lLabourStepTO ) );

      } catch ( UserCannotCompleteDifferentStepSkillsException e ) {
         fail( "Expected no UserCannotCompleteDifferentStepSkillsException but it was thrown." );
      }

      /*************************************************************
       * Scenario 2: a user can complete different skill on the different step:
       *********************************************/

      try {

         // DATA SETUP: Sign off labor row with ENG skill with job step skill ENG for the task
         LabourStepTO lLabourStepTO = new LabourStepTO();
         lLabourStepTO.setSchedStep( new SchedStepKey( lActualTask, 2 ) );
         lLabourStepTO.setLabourStepStatus( StepStatus.MXPARTIAL );
         BaselinedTaskLaborStepSkillCompleteByUserValidator.validate( iTechnician1,
               RefLabourSkillKey.ENG, Arrays.asList( lLabourStepTO ) );

      } catch ( UserCannotCompleteDifferentStepSkillsException e ) {
         fail( "Expected no UserCannotCompleteDifferentStepSkillsException but it was thrown." );
      }

   }


   /**
    * Ensure the validator does not throw a UserCannotCompleteDifferentStepSkillsException when the
    * when a user is captured a step skill for the step that has another skill already completed (
    * COMPLETE, PARTIAL) by another user.
    *
    */
   @Test
   public void testBaselinedTaskSkillUserCanComplete_MultipleSignOn() throws MxException {

      // DATA SETUP: Create requirement definition
      createRequirement();

      // DATA SETUP: Create an actual task
      TaskKey lActualTask = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aRequirement ) {
            aRequirement.setDefinition( iReqDefn );
            aRequirement.addLabour( iLabour_PILOT );

            // Labors for ENG skill:
            aRequirement.addLabour( iLabour_ENG );
            aRequirement.addLabour( iLabour_ENG );

         }
      } );
      // RETRIEVE DATA: Get SchedLabourkey
      DataSetArgument lArgs = new DataSetArgument();

      /*************************************************************
       * Scenario 1: Multiple sign offs are for the same skill on the same step: a user can complete
       * different skill on the same step if the step current status is Pending
       *********************************************/

      lArgs = new DataSetArgument();
      lArgs.add( lActualTask, "sched_db_id", "sched_id" );
      lArgs.add( RefLabourSkillKey.ENG, "labour_skill_db_id", "labour_skill_cd" );

      QuerySet lQs = QuerySetFactory.getInstance().executeQuery( "SCHED_LABOUR", lArgs,
            new String[] { "labour_db_id", "labour_id" } );

      // capture the first step
      lQs.next();
      SchedLabourKey lSchedLabour = lQs.getKey( SchedLabourKey.class, "labour_db_id", "labour_id" );

      captureStep( lActualTask, lSchedLabour, 1, 1, StepStatus.MXPARTIAL, RefLabourSkillKey.ENG );

      signOffLabour( RefLabourRoleTypeKey.TECH, lSchedLabour, iTechnician1 );

      // Set the latest status for step skill to PENDING:
      lQs.next();
      lSchedLabour = lQs.getKey( SchedLabourKey.class, "labour_db_id", "labour_id" );
      captureStep( lActualTask, lSchedLabour, 1, 2, StepStatus.MXPENDING, RefLabourSkillKey.ENG );

      signOffLabour( RefLabourRoleTypeKey.TECH, lSchedLabour, iTechnician1 );

      try {

         // DATA SETUP: Sign off labor row with ENG skill with job step skill ENG for the task
         LabourStepTO lLabourStepTO = new LabourStepTO();
         lLabourStepTO.setSchedStep( new SchedStepKey( lActualTask, 1 ) );
         lLabourStepTO.setLabourStepStatus( StepStatus.MXPARTIAL );
         BaselinedTaskLaborStepSkillCompleteByUserValidator.validate( iTechnician1,
               RefLabourSkillKey.PILOT, Arrays.asList( lLabourStepTO ) );

      } catch ( UserCannotCompleteDifferentStepSkillsException e ) {
         fail( "Expected no UserCannotCompleteDifferentStepSkillsException but it was thrown." );
      }

   }


   /**
    * Ensure the validator throws a UserCannotCompleteDifferentStepSkillsException when the when a
    * user is captured a step skill for the step that has another skill already completed (
    * COMPLETE, PARTIAL) by this user.The user cannot complete
    *
    */
   @Test
   public void testBaselinedTaskSkillUserCannotComplete() throws MxException {

      // DATA SETUP: Create requirement definition
      createRequirement();

      // DATA SETUP: Create an actual task
      TaskKey lActualTask = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aRequirement ) {
            aRequirement.setDefinition( iReqDefn );
            aRequirement.addLabour( iLabour_PILOT );
            aRequirement.addLabour( iLabour_ENG );
         }
      } );

      // RETRIEVE DATA: Get SchedLabourkey
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lActualTask, "sched_db_id", "sched_id" );
      lArgs.add( RefLabourSkillKey.PILOT, "labour_skill_db_id", "labour_skill_cd" );

      QuerySet lQs = QuerySetFactory.getInstance().executeQuery( "SCHED_LABOUR", lArgs,
            new String[] { "labour_db_id", "labour_id" } );
      assertEquals( 1, lQs.getRowCount() );

      // capture the first step
      lQs.next();
      SchedLabourKey lSchedLabour = lQs.getKey( SchedLabourKey.class, "labour_db_id", "labour_id" );

      lArgs = new DataSetArgument();
      lArgs.add( lActualTask, "sched_db_id", "sched_id" );
      lArgs.add( RefLabourSkillKey.ENG, "labour_skill_db_id", "labour_skill_cd" );

      lQs = QuerySetFactory.getInstance().executeQuery( "SCHED_LABOUR", lArgs,
            new String[] { "labour_db_id", "labour_id" } );

      assertEquals( 1, lQs.getRowCount() );

      SchedStepKey lSchedStepKey = captureStep( lActualTask, lSchedLabour, 1, 1,
            StepStatus.MXCOMPLETE, RefLabourSkillKey.PILOT );

      signOffLabour( RefLabourRoleTypeKey.TECH, lSchedLabour, iTechnician1 );

      try {

         // DATA SETUP: Sign off labor row with ENG skill with job step skill ENG for the task
         LabourStepTO lLabourStepTO = new LabourStepTO();
         lLabourStepTO.setSchedStep( lSchedStepKey );
         lLabourStepTO.setLabourStepStatus( StepStatus.MXPARTIAL );
         BaselinedTaskLaborStepSkillCompleteByUserValidator.validate( iTechnician1,
               RefLabourSkillKey.ENG, Arrays.asList( lLabourStepTO ) );

         fail( "Expected UserCannotCompleteDifferentStepSkillsException but it was not thrown." );

      } catch ( UserCannotCompleteDifferentStepSkillsException e ) {
         ;
      }
   }


   protected void debug() throws AssertionError {
      DataRecordUtil.setConnection( iDatabaseConnectionRule.getConnection() );

      DataRecordUtil.debugTable( "sched_labour_step" );
      DataRecordUtil.debugTable( "ref_step_status" );
      DataRecordUtil.debugTable( "sched_step" );
      DataRecordUtil.debugTable( "task_step_skill" );
      DataRecordUtil.debugTable( "sched_labour_role" );
      DataRecordUtil.debugTable( "org_hr" );
      DataRecordUtil.debugTable( "utl_user" );

   }


   private void signOffLabour( RefLabourRoleTypeKey aLabourRoleType, SchedLabourKey aSchedLabour,
         HumanResourceKey aHr ) {

      SchedLabourRoleStatusAccessor lStatus =
            SchedLabourRoleStatusTable.findByForeignKey( aSchedLabour, aLabourRoleType );
      lStatus.setHr( aHr );
      lStatus.setLabourRoleStatus( RefLabourRoleStatusKey.COMPLETE );
      lStatus.update();

   }


   private SchedStepKey captureStep( TaskKey aActualTask, SchedLabourKey aSchedLabour, int aStepOrd,
         int aOrdId, StepStatus aStepSkillStatus, RefLabourSkillKey aStepSkill ) {

      // Get task skill from the task definition to the link to the actual step
      TaskStepKey lTaskStep = new TaskStepKey( iReqDefn, aStepOrd );

      TaskStepSkillKey lTaskStepSkill = new TaskStepSkillKey( lTaskStep, aStepSkill );

      TaskStepSkillTable lTaskStepSkillTable =
            new JdbcTaskStepSkillDao().findByPrimaryKey( lTaskStepSkill );

      // Get the actual task step
      SchedStepKey lSchedStep = new SchedStepKey( aActualTask, aStepOrd );

      SchedLabourStepKey lSchedLabourStep = new SchedLabourStepKey( aSchedLabour, lSchedStep );

      SchedLabourStepTable lSchedLabourStepTable = SchedLabourStepTable.create( lSchedLabourStep );
      lSchedLabourStepTable.setStepSkillId( lTaskStepSkillTable.getAlternateKey() );
      lSchedLabourStepTable.setStepStatusId( aStepSkillStatus );
      lSchedLabourStepTable.setOrdId( aOrdId );
      lSchedLabourStepTable.insert();

      // Update the step status
      SchedStepTable lSchedStepTable = SchedStepTable.findByPrimaryKey( lSchedStep );
      lSchedStepTable.setStepStatus( aStepSkillStatus );
      lSchedStepTable.update();

      return lSchedStep;
   }
}
