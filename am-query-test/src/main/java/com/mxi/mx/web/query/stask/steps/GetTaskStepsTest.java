package com.mxi.mx.web.query.stask.steps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.LabourRequirement;
import com.mxi.am.domain.RequirementDefinition;
import com.mxi.am.domain.Step;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.task.model.StepStatus;


/**
 * This class tests the query com.mxi.mx.core.query.stask.steps.GetTaskSteps.qrx. The GetTaskSteps
 * query gets steps (adhoc and baseline) for the provided task. This query is based on
 * GetTaskDefnSteps.qrx and modified to return adhoc steps too. Faults may have both type of steps
 * or multiple sets of baseline steps if multiple repair references are selected. Returns all steps
 * for the task unless a skill is provided. Then returns only steps with the provided skill.
 *
 */

public final class GetTaskStepsTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   private TaskTaskKey taskDefinitionWithoutSkillsKey;
   private TaskTaskKey taskDefinitionWithSkillsKey;

   private RefLabourSkillKey ENG_SKILL = RefLabourSkillKey.ENG;
   private RefLabourSkillKey PILOT_SKILL = RefLabourSkillKey.PILOT;
   private RefLabourSkillKey SKILL_DOES_NOT_EXIST = RefLabourSkillKey.LBR;


   /**
    * Test all steps are returned for baseline task with skills when no skill is provided.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testbaselineTask_withStepsAndSkills_noSkillProvided() throws Exception {

      // ASSEMBLE
      // Create an actual task based on the executable requirement.
      // The requirement definition has two labour rows and two steps, each with a skill.
      TaskKey lActualTask = Domain.createRequirement( req -> {
         req.setDefinition( taskDefinitionWithSkillsKey );
      } );

      // ACT
      QuerySet lQs = execute( lActualTask, null );

      // ASSERT
      assertTrue( lQs.next() );
      assertEquals( 2, lQs.getRowCount() );
      assertTrue( lQs.getBoolean( "has_skills" ) );
      assertEquals( StepStatus.MXPENDING.name(), lQs.getString( "step_status_cd" ) );
      assertEquals( "STEP ONE", lQs.getString( "step_ldesc" ) );
      assertEquals( null, lQs.getString( "adhoc_step_ldesc" ) );
      assertEquals( 1, lQs.getInt( "step_id" ) );

   }


   /**
    * Test all steps are returned for baseline task with no skills when no skill is provided.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testbaselineTask_withStepsAndNoSkills_noSkillProvided() throws Exception {

      // ASSEMBLE
      // Create an actual task based on the executable requirement.
      // The requirement definition has two labour rows and two steps, each with a skill.
      TaskKey lActualTask = Domain.createRequirement( req -> {
         req.setDefinition( taskDefinitionWithoutSkillsKey );
      } );

      // ACT
      QuerySet lQs = execute( lActualTask, null );

      // ASSERT
      assertTrue( lQs.next() );
      assertEquals( 2, lQs.getRowCount() );
      assertTrue( !lQs.getBoolean( "has_skills" ) );
      assertEquals( StepStatus.MXPENDING.name(), lQs.getString( "step_status_cd" ) );
      assertEquals( "STEP ONE WITH NO SKILLS", lQs.getString( "step_ldesc" ) );
      assertEquals( null, lQs.getString( "adhoc_step_ldesc" ) );
      assertEquals( 1, lQs.getInt( "step_id" ) );

   }


   /**
    * Test correct steps are returned for an adhoc task.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testAdhocTask() throws Exception {

      // ASSEMBLE
      // Create an adhoc task with a step.
      TaskKey adhocTask = Domain.createAdhocTask( task -> {
         task.addStep( "ADHOC TASK STEP ONE" );
      } );

      // ACT
      QuerySet lQs = execute( adhocTask, null );

      // ASSERT
      assertTrue( lQs.next() );
      assertEquals( 1, lQs.getRowCount() );
      assertTrue( !lQs.getBoolean( "has_skills" ) );
      assertEquals( StepStatus.MXPENDING.name(), lQs.getString( "step_status_cd" ) );
      assertEquals( null, lQs.getString( "step_ldesc" ) );
      assertEquals( "ADHOC TASK STEP ONE", lQs.getString( "adhoc_step_ldesc" ) );
      assertEquals( 1, lQs.getInt( "step_id" ) );

   }


   /**
    * This method executes the query GetTaskSteps
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
            "com.mxi.mx.core.query.stask.steps.GetTaskSteps", lArgs );
   }


   @Before
   public void setUp() throws Exception {

      // Create a reusable, executable requirement task definition with labour, steps and step
      // skills
      taskDefinitionWithSkillsKey =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               private DomainConfiguration<Step> createStep( final String description,
                     final RefLabourSkillKey skillKey ) {
                  return new DomainConfiguration<Step>() {

                     @Override
                     public void configure( Step aStep ) {
                        aStep.setDescription( description );
                        aStep.addStepSkill( skillKey, true );
                     }
                  };
               }


               @Override
               public void configure( RequirementDefinition reqDefinition ) {

                  reqDefinition.setExecutable( true );
                  reqDefinition.addLabourRequirement(
                        new LabourRequirement( ENG_SKILL, BigDecimal.TEN, BigDecimal.ONE, null ) );
                  reqDefinition.addLabourRequirement( new LabourRequirement( PILOT_SKILL,
                        BigDecimal.TEN, BigDecimal.ONE, null ) );
                  reqDefinition.addStep( createStep( "STEP ONE", ENG_SKILL ) );
                  reqDefinition.addStep( createStep( "STEP_TWO", PILOT_SKILL ) );
               }
            } );

      // Create a reusable, executable requirement task definition with labour, steps and no step
      // skills
      taskDefinitionWithoutSkillsKey =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition reqDefinition ) {

                  reqDefinition.setExecutable( true );
                  reqDefinition.addLabourRequirement(
                        new LabourRequirement( ENG_SKILL, BigDecimal.TEN, BigDecimal.ONE, null ) );
                  reqDefinition.addLabourRequirement( new LabourRequirement( PILOT_SKILL,
                        BigDecimal.TEN, BigDecimal.ONE, null ) );
                  reqDefinition.addStep( step -> {
                     step.setDescription( "STEP ONE WITH NO SKILLS" );
                  } );
                  reqDefinition.addStep( step -> {
                     step.setDescription( "STEP TWO WITH NO SKILLS" );
                  } );
               }
            } );

   }
}
