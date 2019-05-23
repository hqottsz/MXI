package com.mxi.mx.web.query.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Requirement;
import com.mxi.am.domain.RequirementDefinition;
import com.mxi.am.domain.Step;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.SchedLabourKey;
import com.mxi.mx.core.key.SchedLabourStepKey;
import com.mxi.mx.core.key.SchedStepKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskStepKey;
import com.mxi.mx.core.key.TaskStepSkillKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.sched.SchedLabourStepTable;
import com.mxi.mx.core.table.sched.SchedStepTable;
import com.mxi.mx.core.table.task.JdbcTaskStepSkillDao;
import com.mxi.mx.core.table.task.TaskStepSkillTable;
import com.mxi.mx.core.task.model.StepStatus;


/**
 * This class tests the query com.mxi.mx.web.query.task.GetTaskDefnSteps.qrx
 *
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetTaskDefnStepsTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   private TaskTaskKey iTaskDefn;

   private RefLabourSkillKey ENG_SKILL = RefLabourSkillKey.ENG;
   private RefLabourSkillKey PILOT_SKILL = RefLabourSkillKey.PILOT;


   /**
    * Tests all steps by labour skill
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testTaskStepsByLabour() throws Exception {

      // DATA SETUP: Create an actual task based on the executable requirement above
      TaskKey lActualTask = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setDefinition( iTaskDefn );
            aReq.addLabour( aLabour -> {
               aLabour.setSkill( ENG_SKILL );
               aLabour.setTechnicianRole( tech -> tech.setScheduledHours( 4 ) );
               aLabour.setCertifierRole( cert -> cert.setScheduledHours( 1 ) );
               aLabour.setInspectorRole( insp -> insp.setScheduledHours( 2 ) );
            } );
         }
      } );

      // RETRIEVE DATA: Get SchedLabourkey
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lActualTask, "sched_db_id", "sched_id" );
      lArgs.add( ENG_SKILL, "labour_skill_db_id", "labour_skill_cd" );

      QuerySet lQs = QuerySetFactory.getInstance().executeQuery( "SCHED_LABOUR", lArgs,
            new String[] { "labour_db_id", "labour_id" } );

      lQs.next();
      SchedLabourKey lSchedLabour = lQs.getKey( SchedLabourKey.class, "labour_db_id", "labour_id" );

      captureStep( lActualTask, lSchedLabour );

      lQs = execute( lActualTask, ENG_SKILL );

      // EXPEXTED: Asserts that there is a step with ENG skill:
      assertEquals( 1, lQs.getRowCount() );

      lQs.next();

      assertTrue( lQs.getBoolean( "has_skills" ) );
      assertEquals( StepStatus.MXPARTIAL.name(), lQs.getString( "step_status_cd" ) );
      assertEquals( "STEP ONE", lQs.getString( "step_ldesc" ) );
      assertEquals( 1, lQs.getInt( "step_id" ) );

      lQs = execute( lActualTask, RefLabourSkillKey.LBR );

      // Expected: Asserts that there is no steps with LBR skill:
      assertEquals( 0, lQs.getRowCount() );
   }


   /**
    * Tests all task's steps
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testAllTaskSteps() throws Exception {

      // DATA SETUP: Create an actual task based on the executable requirement above
      TaskKey lActualTask = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setDefinition( iTaskDefn );
            aReq.addLabour( aLabour -> {
               aLabour.setSkill( ENG_SKILL );
               aLabour.setTechnicianRole( tech -> tech.setScheduledHours( 4 ) );
               aLabour.setCertifierRole( cert -> cert.setScheduledHours( 1 ) );
               aLabour.setInspectorRole( insp -> insp.setScheduledHours( 2 ) );
            } );
         }
      } );

      // RETRIEVE DATA: Get SchedLabourkey
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lActualTask, "sched_db_id", "sched_id" );
      lArgs.add( ENG_SKILL, "labour_skill_db_id", "labour_skill_cd" );

      QuerySet lQs = QuerySetFactory.getInstance().executeQuery( "SCHED_LABOUR", lArgs,
            new String[] { "labour_db_id", "labour_id" } );

      lQs.next();
      SchedLabourKey lSchedLabour = lQs.getKey( SchedLabourKey.class, "labour_db_id", "labour_id" );

      captureStep( lActualTask, lSchedLabour );

      lQs = execute( lActualTask, null );

      // EXPEXTED: Asserts that there is a step :
      assertEquals( 2, lQs.getRowCount() );

      lQs.next();

      assertTrue( lQs.getBoolean( "has_skills" ) );
      assertEquals( StepStatus.MXPARTIAL.name(), lQs.getString( "step_status_cd" ) );
      assertEquals( "STEP ONE", lQs.getString( "step_ldesc" ) );
      assertEquals( 1, lQs.getInt( "step_id" ) );

   }


   private void captureStep( TaskKey aActualTask, SchedLabourKey aSchedLabour ) {

      // Get task skill from the task definition to the link to the actual step
      TaskStepKey lTaskStep = new TaskStepKey( iTaskDefn, 1 );

      TaskStepSkillKey lTaskStepSkill = new TaskStepSkillKey( lTaskStep, ENG_SKILL );

      TaskStepSkillTable lTaskStepSkillTable =
            new JdbcTaskStepSkillDao().findByPrimaryKey( lTaskStepSkill );

      // Get the actual task step
      SchedStepKey lSchedStep = new SchedStepKey( aActualTask, 1 );

      SchedLabourStepKey lSchedLabourStep = new SchedLabourStepKey( aSchedLabour, lSchedStep );

      SchedLabourStepTable lSchedLabourStepTable = SchedLabourStepTable.create( lSchedLabourStep );
      lSchedLabourStepTable.setStepSkillId( lTaskStepSkillTable.getAlternateKey() );
      lSchedLabourStepTable.setStepStatusId( StepStatus.MXPARTIAL );
      lSchedLabourStepTable.setOrdId( 1 );
      lSchedLabourStepTable.insert();

      // Update the step status to MXPARTIAL
      SchedStepTable lSchedStepTable = SchedStepTable.findByPrimaryKey( lSchedStep );
      lSchedStepTable.setStepStatus( StepStatus.MXPARTIAL );
      lSchedStepTable.update();
   }


   /**
    * This method executes the query in GetAllStepSkills.qrx
    *
    *
    * @return The dataset after execution.
    */
   private QuerySet execute( TaskKey aTask, RefLabourSkillKey aSkillKey ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aTask, "aSchedDbId", "aSchedId" );

      if ( aSkillKey != null ) {
         lArgs.add( aSkillKey, "aLabourSkillDbId", "aLabourSkillCd" );
      }

      return QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   @Before
   public void setUp() throws Exception {

      // DATA SETUP: Create an executable requirement with steps with step skills
      iTaskDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               private DomainConfiguration<Step> createStep( final String aStepDesc,
                     final RefLabourSkillKey aSkill ) {
                  return new DomainConfiguration<Step>() {

                     @Override
                     public void configure( Step aStep ) {
                        aStep.setDescription( aStepDesc );
                        aStep.addStepSkill( aSkill, true );
                     }
                  };
               }


               @Override
               public void configure( RequirementDefinition aReqDefinition ) {

                  aReqDefinition.setExecutable( true );
                  aReqDefinition.addStep( createStep( "STEP ONE", ENG_SKILL ) );
                  aReqDefinition.addStep( createStep( "STEP_TWO", PILOT_SKILL ) );
               }
            } );

   }
}
