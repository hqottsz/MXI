package com.mxi.mx.core.query.taskdefn;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.JobCardDefinition;
import com.mxi.am.domain.LabourSkill;
import com.mxi.am.domain.RequirementDefinition;
import com.mxi.am.domain.Step;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.TaskTaskKey;


/**
 * This class tests the query com.mxi.mx.core.query.taskdefn.GetStepsOfTask.qrx
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class GetStepsOfTaskTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static final int JIC_DEFN_REVISION_1 = 1;
   private static final int INDEPENDENT_INSPECTION_REQUIRED_BOOL = 1;
   private static final int INDEPENDENT_INSPECTION_NOT_REQUIRED_BOOL = 0;
   private static final Boolean INDEPENDENT_INSPECTION_REQUIRED = true;
   private static final Boolean INDEPENDENT_INSPECTION_NOT_REQUIRED = false;
   private static final String STEP1_APPLICABILITY_RANGE = "A00-A10";
   private static final String STEP2_APPLICABILITY_RANGE = "A50-A60";
   private static final String STEP1_DESCRIPTION = "STEP1_DESCRIPTION";
   private static final String STEP2_DESCRIPTION = "STEP2_DESCRIPTION";
   private static final String AMT_SKILL = "AMT";
   private static final String INSP_SKILL = "INSP";


   @Test
   public void itReturnsJobCardStepsAndStepDetails() {

      final AssemblyKey lAcftAssembly = Domain.createAircraftAssembly();

      final RefLabourSkillKey lAmtSkill = createLabourSkill( AMT_SKILL );
      final RefLabourSkillKey lInspSkill = createLabourSkill( INSP_SKILL );

      final TaskTaskKey lJicDefn =
            Domain.createJobCardDefinition( new DomainConfiguration<JobCardDefinition>() {

               @Override
               public void configure( JobCardDefinition aBuilder ) {
                  aBuilder.setConfigurationSlot( Domain.readRootConfigurationSlot( lAcftAssembly ) );
                  aBuilder.setRevisionNumber( JIC_DEFN_REVISION_1 );
                  aBuilder.addLabourRequirement( lAmtSkill, BigDecimal.ZERO );
                  aBuilder.addLabourRequirement( lInspSkill, BigDecimal.ZERO );
                  aBuilder.addStep( new DomainConfiguration<Step>() {

                     @Override
                     public void configure( Step aBuilder ) {
                        aBuilder.addStepSkill( lAmtSkill, INDEPENDENT_INSPECTION_REQUIRED );
                        aBuilder.addStepSkill( lInspSkill, INDEPENDENT_INSPECTION_NOT_REQUIRED );
                        aBuilder.setApplicabilityRange( STEP1_APPLICABILITY_RANGE );
                        aBuilder.setDescription( STEP1_DESCRIPTION );
                     }
                  } );
                  aBuilder.addStep( new DomainConfiguration<Step>() {

                     @Override
                     public void configure( Step aBuilder ) {
                        aBuilder.addStepSkill( lAmtSkill, INDEPENDENT_INSPECTION_REQUIRED );
                        aBuilder.setApplicabilityRange( STEP2_APPLICABILITY_RANGE );
                        aBuilder.setDescription( STEP2_DESCRIPTION );
                     }
                  } );
               }
            } );

      QuerySet lQs = execute( lJicDefn );
      assertEquals( "Expected two steps for the job card definition", 2, lQs.getRowCount() );

      // Assert 1st Step
      lQs.next();
      int lExpectedStepOrderForFirstStep = 1;
      String lExpectedFirstStepSkillInspBoolList =
            AMT_SKILL + ":" + INDEPENDENT_INSPECTION_REQUIRED_BOOL + "," + INSP_SKILL + ":"
                  + INDEPENDENT_INSPECTION_NOT_REQUIRED_BOOL;
      assertEquals( "Incorrect step order returned by the query", lExpectedStepOrderForFirstStep,
            lQs.getInt( "step_ord" ) );
      assertEquals( "Incorrect step description returned by the query", STEP1_DESCRIPTION,
            lQs.getString( "step_ldesc" ) );
      assertEquals( "Incorrect applicability range returned by the query",
            STEP1_APPLICABILITY_RANGE, lQs.getString( "appl_range_ldesc" ) );
      assertEquals( "Incorrect Step Skill and Inspection Required Boolean returned by the query",
            lExpectedFirstStepSkillInspBoolList, lQs.getString( "step_skill_insp_bool_list" ) );

      // Assert 2nd Step
      lQs.next();
      int lExpectedStepOrderForSecondStep = 2;
      String lExpectedSecondStepSkillInspBoolList =
            AMT_SKILL + ":" + INDEPENDENT_INSPECTION_REQUIRED_BOOL;
      assertEquals( "Incorrect step order returned by the query", lExpectedStepOrderForSecondStep,
            lQs.getInt( "step_ord" ) );
      assertEquals( "Incorrect step description returned by the query", STEP2_DESCRIPTION,
            lQs.getString( "step_ldesc" ) );
      assertEquals( "Incorrect applicability range returned by the query",
            STEP2_APPLICABILITY_RANGE, lQs.getString( "appl_range_ldesc" ) );
      assertEquals( "Incorrect Step Skill and Inspection Required Boolean returned by the query",
            lExpectedSecondStepSkillInspBoolList, lQs.getString( "step_skill_insp_bool_list" ) );

   }


   @Test
   public void itReturnsExecutableRequirementDefinitionStepsAndStepDetails() {

      final AssemblyKey lAcftAssembly = Domain.createAircraftAssembly();

      final RefLabourSkillKey lAmtSkill = createLabourSkill( AMT_SKILL );
      final RefLabourSkillKey lInspSkill = createLabourSkill( INSP_SKILL );

      final TaskTaskKey lReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aBuilder ) {
                  aBuilder.againstConfigurationSlot( Domain
                        .readRootConfigurationSlot( lAcftAssembly ) );
                  aBuilder.setRevisionNumber( JIC_DEFN_REVISION_1 );
                  aBuilder.addLabourRequirement( lAmtSkill, BigDecimal.ZERO );
                  aBuilder.addLabourRequirement( lInspSkill, BigDecimal.ZERO );
                  aBuilder.addStep( new DomainConfiguration<Step>() {

                     @Override
                     public void configure( Step aBuilder ) {
                        aBuilder.addStepSkill( lAmtSkill, INDEPENDENT_INSPECTION_REQUIRED );
                        aBuilder.addStepSkill( lInspSkill, INDEPENDENT_INSPECTION_NOT_REQUIRED );
                        aBuilder.setApplicabilityRange( STEP1_APPLICABILITY_RANGE );
                        aBuilder.setDescription( STEP1_DESCRIPTION );
                     }
                  } );
                  aBuilder.addStep( new DomainConfiguration<Step>() {

                     @Override
                     public void configure( Step aBuilder ) {
                        aBuilder.addStepSkill( lAmtSkill, INDEPENDENT_INSPECTION_REQUIRED );
                        aBuilder.setApplicabilityRange( STEP2_APPLICABILITY_RANGE );
                        aBuilder.setDescription( STEP2_DESCRIPTION );
                     }
                  } );
               }
            } );

      QuerySet lQs = execute( lReqDefn );
      assertEquals( "Expected two steps for the requirement definition", 2, lQs.getRowCount() );

      // Assert 1st Step
      lQs.next();
      int lExpectedStepOrderForFirstStep = 1;
      String lExpectedFirstStepSkillInspBoolList =
            AMT_SKILL + ":" + INDEPENDENT_INSPECTION_REQUIRED_BOOL + "," + INSP_SKILL + ":"
                  + INDEPENDENT_INSPECTION_NOT_REQUIRED_BOOL;
      assertEquals( "Incorrect step order returned by the query", lExpectedStepOrderForFirstStep,
            lQs.getInt( "step_ord" ) );
      assertEquals( "Incorrect step description returned by the query", STEP1_DESCRIPTION,
            lQs.getString( "step_ldesc" ) );
      assertEquals( "Incorrect applicability range returned by the query",
            STEP1_APPLICABILITY_RANGE, lQs.getString( "appl_range_ldesc" ) );
      assertEquals( "Incorrect Step Skill and Inspection Required Boolean returned by the query",
            lExpectedFirstStepSkillInspBoolList, lQs.getString( "step_skill_insp_bool_list" ) );

      // Assert 2nd Step
      lQs.next();
      int lExpectedStepOrderForSecondStep = 2;
      String lExpectedSecondStepSkillInspBoolList =
            AMT_SKILL + ":" + INDEPENDENT_INSPECTION_REQUIRED_BOOL;
      assertEquals( "Incorrect step order returned by the query", lExpectedStepOrderForSecondStep,
            lQs.getInt( "step_ord" ) );
      assertEquals( "Incorrect step description returned by the query", STEP2_DESCRIPTION,
            lQs.getString( "step_ldesc" ) );
      assertEquals( "Incorrect applicability range returned by the query",
            STEP2_APPLICABILITY_RANGE, lQs.getString( "appl_range_ldesc" ) );
      assertEquals( "Incorrect Step Skill and Inspection Required Boolean returned by the query",
            lExpectedSecondStepSkillInspBoolList, lQs.getString( "step_skill_insp_bool_list" ) );

   }


   private RefLabourSkillKey createLabourSkill( final String aSkillCode ) {
      return Domain.createLabourSkill( new DomainConfiguration<LabourSkill>() {

         @Override
         public void configure( LabourSkill aBuilder ) {
            aBuilder.setCode( aSkillCode );
         }
      } );
   }


   private QuerySet execute( TaskTaskKey aTaskTaskKey ) {
      DataSetArgument lDataSetArgument = new DataSetArgument();
      lDataSetArgument.add( aTaskTaskKey, "aTaskDbId", "aTaskId" );

      return QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lDataSetArgument );
   }
}
