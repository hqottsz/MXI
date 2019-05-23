package com.mxi.mx.core.services.stask.workcapture;

import static com.mxi.am.domain.Domain.createRequirement;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Requirement;
import com.mxi.am.domain.RequirementDefinition;
import com.mxi.am.domain.Step;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.RefLabourStageKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.SchedLabourKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.sched.SchedLabourTable;


/**
 * Unit test suite for the {@link LaborRowAssociatesWithStepSkillValidator} class.
 *
 */
public class LaborRowAssociatesWithStepSkillValidatorTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static RefLabourSkillKey ENG = RefLabourSkillKey.ENG;

   private static boolean LABOR_REQUIRES_CERT = true;

   private static boolean LABOR_REQUIRES_INSP = true;

   private static boolean LABOR_NO_CERT = false;

   private static boolean LABOR_NO_INSP = false;

   private static boolean STEP_SKILL_REQUIRES_INSP = true;

   private static boolean STEP_SKILL_NO_INSP = false;

   private TaskTaskKey iReqDefn;

   private TaskKey iActualTask;

   private SchedLabourKey iPrimarySchedLabour;

   private SchedLabourKey iAdditionalSchedLabour;


   /**
    * Test scenario:<br>
    * Validates that the additional labor row can be deleted as long as there is a labor row with
    * the same skill and same requirement as the step skill
    */
   @Test
   public void testDeleteNewlyCreatedLabor() throws MxException {

      // DATA SETUP
      createTask( STEP_SKILL_REQUIRES_INSP, LABOR_NO_CERT, LABOR_NO_INSP );

      // Test 1: Should not be able to delete the primary labor row that require certification and
      // inspection
      try {

         LaborRowAssociatesWithStepSkillValidator.validate( iActualTask, iPrimarySchedLabour );

         fail( "Expected LaborRowAssociatesWithStepSkillException but it was not thrown." );

      } catch ( LaborRowAssociatesWithStepSkillException e ) {
         ;
      }

      // Test 2: Should be able to the additional labor row that is not require certification and
      // inspection
      try {
         LaborRowAssociatesWithStepSkillValidator.validate( iActualTask, iAdditionalSchedLabour );

      } catch ( LaborRowAssociatesWithStepSkillException e ) {

         fail( "LaborRowAssociatesWithStepSkillException should not be thrown." );
      }
   }


   /**
    * Test scenario:<br>
    * Validates that the additional labor row with the same skill and requirement is considered
    * valid
    */
   @Test
   public void testDeleteLaborInitializedByRequirement() throws MxException {

      // DATA SETUP
      createTask( STEP_SKILL_REQUIRES_INSP, LABOR_REQUIRES_CERT, LABOR_REQUIRES_INSP );

      // Test 1: Should be able to delete the labor row that requires certification and inspection
      // since there's another labor row with the same requirements
      try {

         LaborRowAssociatesWithStepSkillValidator.validate( iActualTask, iPrimarySchedLabour );

      } catch ( LaborRowAssociatesWithStepSkillException e ) {

         fail( "LaborRowAssociatesWithStepSkillException should not be thrown." );
      }

      // DATA SETUP: Remove the labour row above in order to test the exception for the next test
      // case
      SchedLabourTable lSchedLabourTable = SchedLabourTable.findByPrimaryKey( iPrimarySchedLabour );
      lSchedLabourTable.delete();

      // Test 2: Should not be able to delete the last labor row that requires certification and
      // inspection
      try {
         LaborRowAssociatesWithStepSkillValidator.validate( iActualTask, iAdditionalSchedLabour );

         fail( "Expect LaborRowAssociatesWithStepSkillException but it was not thrown." );

      } catch ( LaborRowAssociatesWithStepSkillException e ) {
         ;
      }
   }


   /**
    * Test scenario:<br>
    * Validates the labor row that requires inspection is considered as valid even though the step
    * skill does not require inspection
    */
   @Test
   public void testDeleteStepSkillDoesNotRequireInspection() throws MxException {

      // DATA SETUP
      createTask( STEP_SKILL_NO_INSP, LABOR_REQUIRES_CERT, LABOR_REQUIRES_INSP );

      // Test 1: Should be able to delete the labor row that do not require inspection.
      try {

         LaborRowAssociatesWithStepSkillValidator.validate( iActualTask, iPrimarySchedLabour );

      } catch ( LaborRowAssociatesWithStepSkillException e ) {

         fail( "LaborRowAssociatesWithStepSkillException should not be thrown." );
      }

      // DATA SETUP: Remove the labour row above in order to test the exception for the next test
      // case
      SchedLabourTable lSchedLabourTable = SchedLabourTable.findByPrimaryKey( iPrimarySchedLabour );
      lSchedLabourTable.delete();

      // Test2: Should not be able to delete the labor row that requires inspection
      try {

         LaborRowAssociatesWithStepSkillValidator.validate( iActualTask, iAdditionalSchedLabour );

         fail( "Expect LaborRowAssociatesWithStepSkillException but it was not thrown." );

      } catch ( LaborRowAssociatesWithStepSkillException e ) {
         ;
      }
   }


   /**
    * Test scenario:<br>
    * Validates if there are multiple steps that use the same skill but has different requirements
    */
   @Test
   public void testMultipleSteps() throws MxException {

      // DATA SETUP: Create one step skill that requires independent inspection and one step skill
      // that does not required independent inspection
      createTaskWithMultipleSteps( LABOR_REQUIRES_CERT, LABOR_NO_INSP );

      // Test 1: Should not be able to delete the labor row that requires inspection.
      try {

         LaborRowAssociatesWithStepSkillValidator.validate( iActualTask, iPrimarySchedLabour );

         fail( "Expect LaborRowAssociatesWithStepSkillException but it was not thrown." );

      } catch ( LaborRowAssociatesWithStepSkillException e ) {
         ;
      }

      // Test2: Should not be able to delete the labor row that requires inspection
      try {

         LaborRowAssociatesWithStepSkillValidator.validate( iActualTask, iAdditionalSchedLabour );

      } catch ( LaborRowAssociatesWithStepSkillException e ) {

         fail( "LaborRowAssociatesWithStepSkillException should not be thrown." );
      }
   }


   /**
    * Test scenario:<br>
    * Validates that the last active labor row with the same requirement as one of the step skill
    * cannot be deleted even if there's another labor with same requirement but with complete status
    */
   @Test
   public void testCompleteLaborRow() throws MxException {

      // Data Setup
      createTask( STEP_SKILL_NO_INSP, LABOR_REQUIRES_CERT, LABOR_NO_INSP );

      // Test 1: Should be able to delete the additional labour row
      try {

         LaborRowAssociatesWithStepSkillValidator.validate( iActualTask, iAdditionalSchedLabour );

      } catch ( LaborRowAssociatesWithStepSkillException e ) {

         fail( "LaborRowAssociatesWithStepSkillException should not be thrown." );
      }

      // Data Setup: Complete the primary labour row
      SchedLabourTable lSchedLabourTable = SchedLabourTable.findByPrimaryKey( iPrimarySchedLabour );
      lSchedLabourTable.setLabourStage( RefLabourStageKey.COMPLETE );
      lSchedLabourTable.update();

      // Test 2: Should be not be able to delete the additional labour row because this is the last
      // labor row that match the step skill requirements since the first one is already completed
      try {

         LaborRowAssociatesWithStepSkillValidator.validate( iActualTask, iAdditionalSchedLabour );

         fail( "Expect LaborRowAssociatesWithStepSkillException but it was not thrown." );

      } catch ( LaborRowAssociatesWithStepSkillException e ) {
         ;
      }

   }


   /**
    * Test scenario:<br>
    * Validates the labor row with the skill that is not being used by any step
    */
   @Test
   public void testUnusedLaborSkill() throws MxException {

      // DATA SETUP
      createTask( STEP_SKILL_NO_INSP, LABOR_REQUIRES_CERT, LABOR_NO_INSP );

      // DATA SETUP: Create another labor row with a different skill
      SchedLabourTable lSchedLabourTable = SchedLabourTable.create();
      lSchedLabourTable.setTask( iActualTask );
      lSchedLabourTable.setLabourSkill( RefLabourSkillKey.PILOT );
      lSchedLabourTable.setLabourStage( RefLabourStageKey.ACTV );
      lSchedLabourTable.insert();

      // Test 1: Should be able to delete the labor row that does not have the same skill as the
      // step skill
      try {

         LaborRowAssociatesWithStepSkillValidator.validate( iActualTask,
               lSchedLabourTable.getPk() );

      } catch ( LaborRowAssociatesWithStepSkillException e ) {

         fail( "LaborRowAssociatesWithStepSkillException should not be thrown." );
      }
   }


   private void createTask( boolean aMultipleStepSkills, boolean aStepSkillRequireInsp,
         boolean aLaborRequireCert, boolean aLabourRequireInsp ) {

      // DATA SETUP: Create a requirement definition
      iReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.setTaskClass( RefTaskClassKey.REQ );
                  aReqDefn.setExecutable( true );

                  // if creating a multiple step skills, then create one that requires independent
                  // inspection and one that doesn't require
                  if ( aMultipleStepSkills ) {
                     aReqDefn.addStep( ( Step aStepBuilder ) -> {
                        aStepBuilder.addStepSkill( ENG, true );
                     } );
                     aReqDefn.addStep( ( Step aStepBuilder ) -> {
                        aStepBuilder.addStepSkill( ENG, false );
                     } );
                  } else {
                     aReqDefn.addStep( ( Step aStepBuilder ) -> {
                        aStepBuilder.addStepSkill( ENG, aStepSkillRequireInsp );
                     } );
                  }
               }
            } );

      // DATA SETUP: Create an actual task
      iActualTask = createRequirement( ( Requirement aBuilder ) -> {
         aBuilder.setDefinition( iReqDefn );

         // Primary labor requirement that requires certification and inspection
         // Technically this is the one that should be created when a requirement is initialized.
         aBuilder.addLabour( aLabour -> {
            aLabour.setSkill( ENG );
            aLabour.setTechnicianRole( tech -> tech.setScheduledHours( 1 ) );
            aLabour.setCertifierRole( cert -> cert.setScheduledHours( 1 ) );
            aLabour.setInspectorRole( insp -> insp.setScheduledHours( 1 ) );
         } );

         // Additional labor requirement
         if ( aLaborRequireCert ) {

            if ( aLabourRequireInsp ) {

               // Certification and Inspection required
               aBuilder.addLabour( aLabour -> {
                  aLabour.setSkill( ENG );
                  aLabour.setTechnicianRole( tech -> tech.setScheduledHours( 1 ) );
                  aLabour.setCertifierRole( cert -> cert.setScheduledHours( 1 ) );
                  aLabour.setInspectorRole( insp -> insp.setScheduledHours( 1 ) );
               } );
            } else {

               // Certification required but no inspection required
               aBuilder.addLabour( aLabour -> {
                  aLabour.setSkill( ENG );
                  aLabour.setTechnicianRole( tech -> tech.setScheduledHours( 1 ) );
                  aLabour.setCertifierRole( cert -> cert.setScheduledHours( 1 ) );
               } );
            }
         } else {

            // No certification or inspection required
            aBuilder.addLabour( aLabour -> {
               aLabour.setSkill( ENG );
               aLabour.setTechnicianRole( tech -> tech.setScheduledHours( 1 ) );
            } );
         }
      } );

      // DATA RETRIEVAL: Get the labor row
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( iActualTask, "sched_db_id", "sched_id" );
      lArgs.add( ENG, "labour_skill_db_id", "labour_skill_cd" );

      QuerySet lQs = QuerySetFactory.getInstance().executeQuery( "SCHED_LABOUR", lArgs,
            new String[] { "labour_db_id", "labour_id", "insp_bool" } );

      assertEquals( 2, lQs.getRowCount() );

      lQs.next();

      // Assume that iPrimarySchedLabour is the labour row that was created for the task when the
      // requirement is initialized
      // iAdditionalSchedLabour is the labour row that was created manually for the task
      if ( lQs.getBoolean( "insp_bool" ) ) {
         iPrimarySchedLabour = lQs.getKey( SchedLabourKey.class, "labour_db_id", "labour_id" );
      } else {
         iAdditionalSchedLabour = lQs.getKey( SchedLabourKey.class, "labour_db_id", "labour_id" );
      }

      lQs.next();

      if ( iPrimarySchedLabour == null ) {
         iPrimarySchedLabour = lQs.getKey( SchedLabourKey.class, "labour_db_id", "labour_id" );
      } else {
         iAdditionalSchedLabour = lQs.getKey( SchedLabourKey.class, "labour_db_id", "labour_id" );
      }

   }


   private void createTask( boolean aStepSkillRequireInsp, boolean aLaborRequireCert,
         boolean aLabourRequireInsp ) {

      createTask( false, aStepSkillRequireInsp, aLaborRequireCert, aLabourRequireInsp );
   }


   private void createTaskWithMultipleSteps( boolean aLaborRequireCert,
         boolean aLabourRequireInsp ) {
      createTask( true, STEP_SKILL_REQUIRES_INSP, aLaborRequireCert, aLabourRequireInsp );
   }

}
