package com.mxi.mx.web.query.taskdefn;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.BlockDefinition;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.PlanningType;
import com.mxi.am.domain.RequirementDefinition;
import com.mxi.am.domain.builder.TaskRevisionBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.EqpPlanningTypeKey;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.TaskDefnKey;
import com.mxi.mx.core.key.TaskTaskKey;


/**
 * Test case for GetTaskRegularPlanningTypeValues.qrx.
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class GetTaskRegularPlanningTypeValuesTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Test the case where a requirement has a JIC with multiple revisions.
    *
    * <ol>
    * <li>Data Setup</li>
    * <li>Planning Type:</li>
    * <li>Nr Factor: 2.00 Skill: PILOT 70% Skill: ENG 30%</li>
    * <li>JIC Setup:</li>
    * <li>JIC Revision 1 - Skill: LBR, Num of People Required: 1, Work Performed Sched Hrs: 1</li>
    * <li>JIC Revision 2 - Skill: LBR, Num of People Required: 2, Work Performed Sched Hrs: 2</li>
    * <li>JIC Revision 3 - Skill: LBR, Num of People Required: 3, Work Performed Sched Hrs: 3</li>
    * <br>
    * <li>Expected Results:</li>
    * <li>Skill: PILOT, Effort Hr: 3 * 3 * 2 * 0.7 = 12. 6</li>
    * <li>Skill: ENG, Effort Hr: 3 * 3 * 2 * 0.7 = 5.4</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testRequirementWithMultipleJicRevisions() throws Exception {

      // Create a JIC with multiple revisions
      final TaskTaskKey lJicTaskRev1 = Domain.createJobCardDefinition();
      TaskDefnKey lJicDefnKey = new TaskDefnKey( lJicTaskRev1.toString() );

      final TaskTaskKey lJicTaskRev2 =
            new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.JIC ).withRevisionNumber( 2 )
                  .withTaskDefn( lJicDefnKey ).withLabourRequirement( RefLabourSkillKey.LBR, 1, 1 )
                  .build();
      final TaskTaskKey lJicTaskRev3 =
            new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.JIC ).withRevisionNumber( 3 )
                  .withTaskDefn( lJicDefnKey ).withLabourRequirement( RefLabourSkillKey.LBR, 2, 2 )
                  .build();

      final TaskTaskKey lJicTaskRev4 =
            new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.JIC ).withRevisionNumber( 4 )
                  .withTaskDefn( lJicDefnKey ).withLabourRequirement( RefLabourSkillKey.LBR, 3, 3 )
                  .build();

      // Create a Requirement
      final TaskTaskKey lReqTaskKey =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.setRevisionNumber( 1 );
                  aReqDefn.addJobCardDefinition( lJicTaskRev1 );
                  aReqDefn.addJobCardDefinition( lJicTaskRev2 );
                  aReqDefn.addJobCardDefinition( lJicTaskRev3 );
                  aReqDefn.addJobCardDefinition( lJicTaskRev4 );
               }
            } );

      // Create a planning type
      EqpPlanningTypeKey lPlanningTypeKey = createPlanningType();

      // Execute
      DataSet lResultDs = execute( lPlanningTypeKey, lReqTaskKey );

      assertEquals( 2, lResultDs.getRowCount() );

      while ( lResultDs.next() ) {
         if ( RefLabourSkillKey.PILOT.getCd().equals( lResultDs.getString( "labour_skill_cd" ) ) ) {

            assertEquals( "Effort Hr", 12.6, lResultDs.getDouble( "effort_hr" ), 0 );

         } else if ( RefLabourSkillKey.ENG.getCd()
               .equals( lResultDs.getString( "labour_skill_cd" ) ) ) {

            assertEquals( "Effort Hr", 5.4, lResultDs.getDouble( "effort_hr" ), 0 );
         } else {
            fail( "unexpected labour skill" );
         }
      }
   }


   /**
    * Test the case where a block with both executable requirement and non-executable requirement
    *
    * <ol>
    * <li>Data Setup</li>
    * <li>Planning Type:</li>
    * <li>Nr Factor: 2.00 Skill: PILOT 70% Skill: ENG 30%</li>
    * <li>Requirement Setup:</li>
    * <li>Executable req Rev 1 - Skill: LBR, Num of People Required: 5, Work Performed Sched Hrs:
    * 4</li>
    * <li>Executable req Rev 2 - Skill: LBR, Num of People Required: 4, Work Performed Sched Hrs:
    * 1</li>
    * <li>Non-executable req - Skill: LBR, Num of People Required: 2, Work Performed Sched Hrs:
    * 3</li> <br>
    * <li>Expected Results:</li>
    * <li>Skill: PILOT, Effort Hr: 4 * 1 * 2 * 0.7 = 5.6</li>
    * <li>Skill: ENG, Effort Hr: 4 * 1 * 2 * 0.3 = 2.4</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testBlockWithMultipleRequirementTypes() throws Exception {

      // Create an executable requirement
      final TaskTaskKey lExecReqTaskRev1 = new TaskRevisionBuilder()
            .withTaskClass( RefTaskClassKey.REQ ).withRevisionNumber( 1 )
            .withLabourRequirement( RefLabourSkillKey.LBR, 5, 4 ).withWorkscope( true ).build();

      final TaskTaskKey lExecReqTaskRev2 =
            new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ ).withRevisionNumber( 2 )
                  .withLabourRequirement( RefLabourSkillKey.LBR, 4, 1 ).withWorkscope( true )
                  .withTaskDefn( new TaskDefnKey( lExecReqTaskRev1.toString() ) ).build();

      // Create a non-executable requirement.
      // The labour skill added to this requirement should be ignored
      final TaskTaskKey lReqTaskKey = new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ )
            .withRevisionNumber( 1 ).withLabourRequirement( RefLabourSkillKey.LBR, 2, 3 )
            .withWorkscope( false ).build();

      // Create a Block
      final TaskTaskKey lBlockTaskKey =
            Domain.createBlockDefinition( new DomainConfiguration<BlockDefinition>() {

               @Override
               public void configure( BlockDefinition aBlockDefn ) {
                  aBlockDefn.makeUnique();
                  aBlockDefn.addRequirementDefinition( lExecReqTaskRev1 );
                  aBlockDefn.addRequirementDefinition( lExecReqTaskRev2 );
                  aBlockDefn.addRequirementDefinition( lReqTaskKey );
               }
            } );

      // Create a planning type
      EqpPlanningTypeKey lPlanningTypeKey = createPlanningType();

      // Execute
      DataSet lResultDs = execute( lPlanningTypeKey, lBlockTaskKey );

      assertEquals( 2, lResultDs.getRowCount() );

      while ( lResultDs.next() ) {
         if ( RefLabourSkillKey.PILOT.getCd().equals( lResultDs.getString( "labour_skill_cd" ) ) ) {

            assertEquals( "Effort Hr", 5.6, lResultDs.getDouble( "effort_hr" ), 0 );

         } else if ( RefLabourSkillKey.ENG.getCd()
               .equals( lResultDs.getString( "labour_skill_cd" ) ) ) {

            assertEquals( "Effort Hr", 2.4, lResultDs.getDouble( "effort_hr" ), 0 );
         } else {
            fail( "unexpected labour skill" );
         }
      }
   }


   /**
    * Test the case where a block with a requirement that has multiple revisions and non-revision
    * JICs
    *
    * <ol>
    * <li>Data Setup</li>
    * <li>Planning Type:</li>
    * <li>Nr Factor: 2.00 Skill: PILOT 70% Skill: ENG 30%</li>
    * <li>JICs Setup:</li>
    * <li>Jic 1 Rev 1 - Skill: LBR, Num of People Required: 9, Work Performed Sched Hrs: 2</li>
    * <li>Jic 1 Rev 2 - Skill: LBR, Num of People Required: 2, Work Performed Sched Hrs: 2</li>
    * <li>Jic 2 Rev 1 - Skill: LBR, Num of People Required: 2, Work Performed Sched Hrs: 6</li> <br>
    * <li>Expected Results:</li>
    * <li>Skill: PILOT, Effort Hr: (( 2 * 2 ) + ( 2 * 6 ) ) * 2 * 0.7 = 22.4</li>
    * <li>Skill: ENG, Effort Hr: (( 2 * 2 ) + ( 2 * 6 ) ) * 2 * 0.3 = 9.6</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testBlockWithMultipleJics() throws Exception {

      // Create a JIC with revisions
      final TaskTaskKey lJicTaskRev1 = Domain.createJobCardDefinition();
      TaskDefnKey lJicDefnKey = new TaskDefnKey( lJicTaskRev1.toString() );

      final TaskTaskKey lJicTaskRev2 =
            new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.JIC ).withRevisionNumber( 2 )
                  .withTaskDefn( lJicDefnKey ).withLabourRequirement( RefLabourSkillKey.LBR, 9, 2 )
                  .build();
      final TaskTaskKey lJicTaskRev3 =
            new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.JIC ).withRevisionNumber( 3 )
                  .withTaskDefn( lJicDefnKey ).withLabourRequirement( RefLabourSkillKey.LBR, 2, 2 )
                  .build();

      // Create a JIC with no revision
      final TaskTaskKey lJicTask2 = new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.JIC )
            .withLabourRequirement( RefLabourSkillKey.LBR, 2, 6 ).build();

      // Create a Requirement
      final TaskTaskKey lReqTaskKey =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.setRevisionNumber( 1 );
                  aReqDefn.addJobCardDefinition( lJicTaskRev1 );
                  aReqDefn.addJobCardDefinition( lJicTaskRev2 );
                  aReqDefn.addJobCardDefinition( lJicTaskRev3 );
                  aReqDefn.addJobCardDefinition( lJicTask2 );
               }

            } );

      // Create a Block
      final TaskTaskKey lBlockTaskKey =
            Domain.createBlockDefinition( new DomainConfiguration<BlockDefinition>() {

               @Override
               public void configure( BlockDefinition aBlockDefn ) {
                  aBlockDefn.makeUnique();
                  aBlockDefn.addRequirementDefinition( lReqTaskKey );
               }
            } );

      // Create a planning type
      EqpPlanningTypeKey lPlanningTypeKey = createPlanningType();

      // Execute
      DataSet lResultDs = execute( lPlanningTypeKey, lBlockTaskKey );

      assertEquals( 2, lResultDs.getRowCount() );

      while ( lResultDs.next() ) {
         if ( RefLabourSkillKey.PILOT.getCd().equals( lResultDs.getString( "labour_skill_cd" ) ) ) {

            assertEquals( "Effort Hr", 22.4, lResultDs.getDouble( "effort_hr" ), 0 );

         } else if ( RefLabourSkillKey.ENG.getCd()
               .equals( lResultDs.getString( "labour_skill_cd" ) ) ) {

            assertEquals( "Effort Hr", 9.6, lResultDs.getDouble( "effort_hr" ), 0 );
         } else {
            fail( "unexpected labour skill" );
         }
      }
   }


   /**
    * Create a planning type
    */
   private EqpPlanningTypeKey createPlanningType() {

      EqpPlanningTypeKey lPlanningTypeKey =
            Domain.createPlanningType( new DomainConfiguration<PlanningType>() {

               @Override
               public void configure( PlanningType aPlanningType ) {
                  aPlanningType.setNrFactor( 2.00 );
                  aPlanningType.addSkill( RefLabourSkillKey.PILOT, 0.7 );
                  aPlanningType.addSkill( RefLabourSkillKey.ENG, 0.3 );
               }
            } );

      return lPlanningTypeKey;
   }


   /**
    * Execute the query.
    *
    * @param aPlanningType
    *           the planning type
    * @param aTask
    *           the task key
    *
    * @throws Exception
    *            if an error occurs.
    */
   private DataSet execute( EqpPlanningTypeKey aPlanningType, TaskTaskKey aTask ) throws Exception {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aPlanningType, "aPlanningTypeDbId", "aPlanningTypeId" );
      lArgs.add( aTask, "aTaskDbId", "aTaskId" );

      // Return true if the query returns a row, otherwise false.
      return QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }

}
