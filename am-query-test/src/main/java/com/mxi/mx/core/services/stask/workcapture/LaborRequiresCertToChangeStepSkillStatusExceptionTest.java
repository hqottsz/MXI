package com.mxi.mx.core.services.stask.workcapture;

import static com.mxi.am.domain.Domain.createRequirement;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.AdhocTask;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Requirement;
import com.mxi.am.domain.RequirementDefinition;
import com.mxi.am.domain.Step;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.SchedLabourKey;
import com.mxi.mx.core.key.SchedLabourStepKey;
import com.mxi.mx.core.key.SchedStepKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.services.stask.labour.LabourStepTO;
import com.mxi.mx.core.table.sched.SchedLabourStepTable;
import com.mxi.mx.core.task.model.StepStatus;


/**
 * Tests the behaviour of the LaborRequiresCertToChangeStepSkillStatusException.
 */

public final class LaborRequiresCertToChangeStepSkillStatusExceptionTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private SchedStepKey iSchedStep;

   private SchedLabourKey iSchedLabourWithCertRequired_1;

   private SchedLabourKey iSchedLabourWithCertRequired_2;

   private SchedLabourKey iSchedLabourWithNoCert;

   private UUID iStepSkillId;

   private static final RefLabourSkillKey ENG = RefLabourSkillKey.ENG;
   private static final RefLabourSkillKey LBR = RefLabourSkillKey.LBR;

   private final static boolean ADD_LABOUR_WITH_NO_CERT = true;
   private final static boolean NO_ADDITIONAL_LABOUR = false;


   /**
    * Validates that the exception is thrown when a step skill status is changed but the labor row
    * does not require certification for a fault with a repair reference.
    */
   @Test( expected = LaborRequiresCertToChangeStepSkillStatusException.class )
   public void testLabourRowDoesNotRequireCertification_Fault() throws Exception {

      // DATA SETUP
      createFaultWithRepairReference( ADD_LABOUR_WITH_NO_CERT );

      LaborRequiresCertToChangeStepSkillStatusValidator.validate( iSchedLabourWithNoCert,
            getLabourStepTOs() );

   }


   /**
    * Validates that the exception is thrown when a step skill status is changed but the labor row
    * does not require certification
    */
   @Test( expected = LaborRequiresCertToChangeStepSkillStatusException.class )
   public void testLabourRowDoesNotRequireCertification() throws Exception {

      // DATA SETUP
      createTaskWithStepSkills( ADD_LABOUR_WITH_NO_CERT );

      LaborRequiresCertToChangeStepSkillStatusValidator.validate( iSchedLabourWithNoCert,
            getLabourStepTOs() );

   }


   /**
    * Validates a step skill status can be changed because the labor row requires certification
    */
   @Test
   public void testLabourRowRequiresCertification() throws Exception {

      // DATA SETUP
      createTaskWithStepSkills( NO_ADDITIONAL_LABOUR );

      try {
         LaborRequiresCertToChangeStepSkillStatusValidator.validate( iSchedLabourWithCertRequired_1,
               getLabourStepTOs() );

      } catch ( LaborRequiresCertToChangeStepSkillStatusException e ) {

         fail( "LaborRequiresCertToChangeStepSkillStatusException should not be thrown." );
      }
   }


   /**
    * Validates a step skill status can be changed because the labor row requires certification
    */
   @Test
   public void testLabourRowRequiresCertification_Fault() throws Exception {

      // DATA SETUP
      createFaultWithRepairReference( NO_ADDITIONAL_LABOUR );

      try {
         LaborRequiresCertToChangeStepSkillStatusValidator.validate( iSchedLabourWithCertRequired_1,
               getLabourStepTOs() );

      } catch ( LaborRequiresCertToChangeStepSkillStatusException e ) {

         fail( "LaborRequiresCertToChangeStepSkillStatusException should not be thrown." );
      }
   }


   /**
    * Validates that the existing step skill status cannot be changed to a different status because
    * the labor does not require certification
    */
   @Test( expected = LaborRequiresCertToChangeStepSkillStatusException.class )
   public void testLabourRowDoesNotRequireCertification_MultipleSignOff_DifferentStatuses()
         throws Exception {

      // DATA SETUP
      createTaskWithStepSkills( ADD_LABOUR_WITH_NO_CERT );

      // DATA SETUP: Sign off one labour row
      signoffStep( iSchedLabourWithCertRequired_1, StepStatus.MXCOMPLETE );

      LaborRequiresCertToChangeStepSkillStatusValidator.validate( iSchedLabourWithNoCert,
            getLabourStepTOs() );

   }


   /**
    * Validates that it is valid when existing step skill status doesn't change and the labor does
    * not require certification
    */
   @Test
   public void testLabourRowDoesNotRequireCertification_MultipleSignOff_SameStatus()
         throws Exception {

      // DATA SETUP
      createTaskWithStepSkills( ADD_LABOUR_WITH_NO_CERT );

      List<LabourStepTO> lLabourStepTOs = getLabourStepTOs();

      // DATA SETUP: Sign off one labour row
      signoffStep( iSchedLabourWithCertRequired_1, lLabourStepTOs.get( 0 ).getLabourStepStatus() );

      try {
         LaborRequiresCertToChangeStepSkillStatusValidator.validate( iSchedLabourWithNoCert,
               lLabourStepTOs );

      } catch ( LaborRequiresCertToChangeStepSkillStatusException e ) {

         fail( "LaborRequiresCertToChangeStepSkillStatusException should not be thrown." );
      }
   }


   /**
    * Validates that the existing step skill status can be changed to a different status because the
    * labor requires certification
    */
   @Test
   public void testLabourRowRequiresCertification_MultipleSignOff() throws Exception {

      // DATA SETUP
      createTaskWithStepSkills( NO_ADDITIONAL_LABOUR );

      // DATA SETUP: Sign off one labour row
      signoffStep( iSchedLabourWithCertRequired_1, StepStatus.MXCOMPLETE );

      try {
         LaborRequiresCertToChangeStepSkillStatusValidator.validate( iSchedLabourWithCertRequired_2,
               getLabourStepTOs() );

      } catch ( LaborRequiresCertToChangeStepSkillStatusException e ) {

         fail( "LaborRequiresCertToChangeStepSkillStatusException should not be thrown." );
      }
   }


   /**
    * Validates for step without skill, the exception shoudn't be thrown in any case
    */
   @Test
   public void testNoStepSkill() throws Exception {

      // DATA SETUP: Create requirement definition with steps that have skills
      TaskTaskKey lTaskTaskKey =
            Domain.createRequirementDefinition( ( RequirementDefinition aRefDefnBuilder ) -> {
               aRefDefnBuilder.setExecutable( true );
               aRefDefnBuilder.addStep( ( Step aStepBuilder ) -> {
                  aStepBuilder.setDescription( "Step 1" );
               } );
            } );

      // DATA SETUP: Create an actual task
      TaskKey lActualTask = createRequirement( ( Requirement aBuilder ) -> {
         aBuilder.setDefinition( lTaskTaskKey );

         // This labour row associates with the labor from the task definition
         aBuilder.addLabour( aLabour -> {
            aLabour.setSkill( ENG );
            aLabour.setTechnicianRole( tech -> tech.setScheduledHours( 1 ) );
            aLabour.setCertifierRole( cert -> cert.setScheduledHours( 1 ) );
            aLabour.setInspectorRole( insp -> insp.setScheduledHours( 1 ) );
         } );
         aBuilder.addLabour( aLabour -> {
            aLabour.setSkill( ENG );
            aLabour.setTechnicianRole( tech -> tech.setScheduledHours( 1 ) );
         } );

      } );

      getSchedLabour( lActualTask );

      // Test 1: Validate the the step status can be changed with labour row with certification
      // required
      try {
         LaborRequiresCertToChangeStepSkillStatusValidator.validate( iSchedLabourWithCertRequired_1,
               getLabourStepTOs() );

      } catch ( LaborRequiresCertToChangeStepSkillStatusException e ) {

         fail( "LaborRequiresCertToChangeStepSkillStatusException should not be thrown." );
      }

      // Test 2: Validate the the step status can be changed with labour row that does not require
      // certification
      try {
         LaborRequiresCertToChangeStepSkillStatusValidator.validate( iSchedLabourWithNoCert,
               getLabourStepTOs() );

      } catch ( LaborRequiresCertToChangeStepSkillStatusException e ) {

         fail( "CannotChangeStepSkillStatusUnlessCertifyWorkException should not be thrown." );
      }
   }


   /**
    * Validates that for adhoc task the exception shoudn't be thrown in any case
    */
   @Test
   public void testAdhoc() throws Exception {

      // DATA SETUP: Create an adhoc task with 2 labor requirements.
      // One with certification required and one without certification required
      TaskKey lAdhocTask = Domain.createAdhocTask( ( AdhocTask aAdhocTaskBuilder ) -> {
         aAdhocTaskBuilder.addStep( "Step1" );
         aAdhocTaskBuilder.addLabour( labour -> {
            labour.setSkill( ENG );
            labour.setTechnicianRole( tech -> tech.setScheduledHours( 1 ) );
         } );
         aAdhocTaskBuilder.addLabour( labour -> {
            labour.setSkill( ENG );
            labour.setTechnicianRole( tech -> tech.setScheduledHours( 1 ) );
            labour.setCertifierRole( cert -> cert.setScheduledHours( 1 ) );
            labour.setInspectorRole( insp -> insp.setScheduledHours( 1 ) );
         } );
      } );

      getSchedLabour( lAdhocTask );

      // Test 1: Validate the the step status can be changed with labour row with certification
      // required
      try {
         LaborRequiresCertToChangeStepSkillStatusValidator.validate( iSchedLabourWithCertRequired_1,
               getLabourStepTOs() );

      } catch ( LaborRequiresCertToChangeStepSkillStatusException e ) {

         fail( "LaborRequiresCertToChangeStepSkillStatusException should not be thrown." );
      }

      // Test 2: Validate the the step status can be changed with labour row that does not require
      // certification
      try {
         LaborRequiresCertToChangeStepSkillStatusValidator.validate( iSchedLabourWithNoCert,
               getLabourStepTOs() );

      } catch ( LaborRequiresCertToChangeStepSkillStatusException e ) {

         fail( "LaborRequiresCertToChangeStepSkillStatusException should not be thrown." );
      }

   }


   private void createFaultWithRepairReference( boolean addLabourWithNoCert ) {

      // create aircraft
      AssemblyKey aircraftAssemblyKey = Domain.createAircraftAssembly( aircraftAssembly -> {
         aircraftAssembly.setRootConfigurationSlot( rootConfigSlot -> {
            rootConfigSlot.setCode( "ROOT" );
         } );
      } );

      InventoryKey aircraftInventoryKey = Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( aircraftAssemblyKey );
         aircraft.setDescription( "Aircraft" );
         aircraft.setLocation( Domain.createLocation() );
      } );

      // create a repair reference with steps and skills
      TaskTaskKey repairReferenceKey = Domain.createRequirementDefinition( reference -> {
         reference.setTaskClass( RefTaskClassKey.REPREF );
         reference.setTaskName( "Repair Reference" );
         reference.setCode( "REPREF1" );
         reference.setStatus( RefTaskDefinitionStatusKey.ACTV );
         reference.addStep( step -> {
            step.setDescription( "Repair Step 1" );
            step.addStepSkill( ENG, false );
         } );
         reference.addStep( step -> {
            step.setDescription( "Repair Step 2" );
            step.addStepSkill( ENG, false );
         } );
      } );

      // corrective task with labour
      TaskKey correctiveTaskKey = Domain.createCorrectiveTask( task -> {
         task.setInventory( aircraftInventoryKey );
         task.addLabour( labour -> {
            labour.setSkill( ENG );
            labour.setTechnicianRole( tech -> tech.setScheduledHours( 1 ) );
            labour.setCertifierRole( cert -> cert.setScheduledHours( 1 ) );
            labour.setInspectorRole( insp -> insp.setScheduledHours( 1 ) );
         } );
         task.addLabour( labour -> {
            labour.setSkill( LBR );
            labour.setTechnicianRole( tech -> tech.setScheduledHours( 1 ) );
            labour.setCertifierRole( cert -> cert.setScheduledHours( 1 ) );
            labour.setInspectorRole( insp -> insp.setScheduledHours( 1 ) );
         } );

         if ( addLabourWithNoCert ) {
            task.addLabour( aLabour -> {
               aLabour.setSkill( ENG );
               aLabour.setTechnicianRole( tech -> tech.setScheduledHours( 1 ) );
            } );
         }
      } );

      // fault with reference
      FaultKey faultKey = Domain.createFault( fault -> {
         fault.setCorrectiveTask( correctiveTaskKey );
         fault.setCurrentRepairReference( repairReferenceKey );
      } );

      iSchedStep = getSchedStep( correctiveTaskKey );

      // Get Sched Labour key
      getSchedLabour( correctiveTaskKey );

      // Get Task Step Skill key (from task definition side)
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( repairReferenceKey, "task_db_id", "task_id" );
      lArgs.add( ENG, "labour_skill_db_id", "labour_skill_cd" );

      QuerySet lQs = QuerySetFactory.getInstance().executeQuery( "TASK_STEP_SKILL", lArgs,
            new String[] { "step_skill_id" } );

      lQs.next();

      iStepSkillId = lQs.getUuid( "step_skill_id" );

   }


   private void createTaskWithStepSkills( boolean aAddNoCertLabor ) {

      // DATA SETUP: Create requirement definition with steps that have skills
      TaskTaskKey lTaskTaskKey =
            Domain.createRequirementDefinition( ( RequirementDefinition aRefDefnBuilder ) -> {
               aRefDefnBuilder.setExecutable( true );
               aRefDefnBuilder.addStep( ( Step aStepBuilder ) -> {
                  aStepBuilder.setDescription( "Step 1" );
                  aStepBuilder.addStepSkill( ENG, true );
               } );
            } );

      // DATA SETUP: Create an actual task
      TaskKey lActualTask = createRequirement( ( Requirement aBuilder ) -> {
         aBuilder.setDefinition( lTaskTaskKey );

         // This labour row associates with the labor from the task definition
         aBuilder.addLabour( aLabour -> {
            aLabour.setSkill( ENG );
            aLabour.setTechnicianRole( tech -> tech.setScheduledHours( 1 ) );
            aLabour.setCertifierRole( cert -> cert.setScheduledHours( 1 ) );
            aLabour.setInspectorRole( insp -> insp.setScheduledHours( 1 ) );
         } );
         aBuilder.addLabour( aLabour -> {
            aLabour.setSkill( ENG );
            aLabour.setTechnicianRole( tech -> tech.setScheduledHours( 1 ) );
            aLabour.setCertifierRole( cert -> cert.setScheduledHours( 1 ) );
            aLabour.setInspectorRole( insp -> insp.setScheduledHours( 1 ) );
         } );

         if ( aAddNoCertLabor ) {
            aBuilder.addLabour( aLabour -> {
               aLabour.setSkill( ENG );
               aLabour.setTechnicianRole( tech -> tech.setScheduledHours( 1 ) );
            } );
         }

      } );

      iSchedStep = getSchedStep( lActualTask );

      // Get Sched Labour key
      getSchedLabour( lActualTask );

      // Get Task Step Skill key (from task definition side)
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lTaskTaskKey, "task_db_id", "task_id" );
      lArgs.add( ENG, "labour_skill_db_id", "labour_skill_cd" );

      QuerySet lQs = QuerySetFactory.getInstance().executeQuery( "TASK_STEP_SKILL", lArgs,
            new String[] { "step_skill_id" } );

      lQs.next();

      iStepSkillId = lQs.getUuid( "step_skill_id" );
   }


   private SchedStepKey getSchedStep( TaskKey aTask ) {

      // Get Sched Step key (from actual side)
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aTask, "sched_db_id", "sched_id" );

      QuerySet lQs = QuerySetFactory.getInstance().executeQuery( "SCHED_STEP", lArgs,
            new String[] { "sched_db_id", "sched_id", "step_id", "step_ord" } );

      lQs.next();

      return lQs.getKey( SchedStepKey.class, "sched_db_id", "sched_id", "step_id" );
   }


   private void getSchedLabour( TaskKey aTask ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aTask, "sched_db_id", "sched_id" );
      lArgs.add( RefLabourSkillKey.ENG, "labour_skill_db_id", "labour_skill_cd" );

      QuerySet lQs = QuerySetFactory.getInstance().executeQuery( "SCHED_LABOUR", lArgs,
            new String[] { "labour_db_id", "labour_id", "cert_bool" } );

      while ( lQs.next() ) {

         if ( lQs.getBoolean( "cert_bool" ) ) {

            if ( iSchedLabourWithCertRequired_1 == null ) {
               iSchedLabourWithCertRequired_1 =
                     lQs.getKey( SchedLabourKey.class, "labour_db_id", "labour_id" );
            } else {
               iSchedLabourWithCertRequired_2 =
                     lQs.getKey( SchedLabourKey.class, "labour_db_id", "labour_id" );
            }
         } else {
            iSchedLabourWithNoCert =
                  lQs.getKey( SchedLabourKey.class, "labour_db_id", "labour_id" );
         }
      }
   }


   private List<LabourStepTO> getLabourStepTOs() {

      LabourStepTO lLabourStepTO = new LabourStepTO();
      lLabourStepTO.setStepSkillId( iStepSkillId );
      lLabourStepTO.setSchedStep( iSchedStep );
      lLabourStepTO.setLabourStepStatus( StepStatus.MXPARTIAL );

      return Arrays.asList( lLabourStepTO );
   }


   private void signoffStep( SchedLabourKey aSchedLabour, StepStatus aStepStatus ) {

      SchedLabourStepKey lSchedLabourStepKey = new SchedLabourStepKey( aSchedLabour, iSchedStep );
      SchedLabourStepTable lSchedLabourStepTable =
            SchedLabourStepTable.create( lSchedLabourStepKey );
      lSchedLabourStepTable.setStepStatusId( aStepStatus );
      lSchedLabourStepTable.setStepSkillId( iStepSkillId );
      lSchedLabourStepTable.setOrdId( 1 );
      lSchedLabourStepTable.insert();
   }

}
