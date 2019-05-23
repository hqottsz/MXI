package com.mxi.mx.core.services.taskdep;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefTaskDepActionKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.services.stask.dependency.TaskDepService;


/**
 * Test class for {@TaskDepSerivce}
 *
 */
public class TaskDepServiceTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();


   /**
    * This test case is testing when getting dependent tasks of a follow-on task under a fault, it
    * will only return those which are under same fault.
    *
    * <pre>
    *    Given a follow-on task definition on an assembly root config slot.
    *    And another follow-on task which has a terminate dependency with the first task on the same assembly config slot.
    *    And an actual task based on the driven follow-on task definition on an aircraft under a fault.
    *    And an actual dependent task based on the same aircraft under the same fault.
    *    And an actual dependent task based on the same aircraft under another fault.
    *    And an actual dependent task based on the same aircraft under no fault.
    *    When get the dependent tasks of the first driven follow-on task.
    *    Then verify it only returns the dependent task under the same fault.
    * </pre>
    */
   @Test
   public void itGetsDependentFollowOnTasksUnderSameFault() {
      final AssemblyKey lAircraftAssembly = Domain.createAircraftAssembly();

      final InventoryKey lAircraft = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssembly( lAircraftAssembly );
      } );

      final TaskTaskKey lTempInspectDefinition =
            Domain.createRequirementDefinition( aRequirementDefinition -> {
               aRequirementDefinition.againstConfigurationSlot(
                     Domain.readRootConfigurationSlot( lAircraftAssembly ) );
            } );

      final TaskTaskKey lPermanentRepairDefinition =
            Domain.createRequirementDefinition( aRequirementDefinition -> {
               aRequirementDefinition.againstConfigurationSlot(
                     Domain.readRootConfigurationSlot( lAircraftAssembly ) );
               aRequirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.TERMINATE,
                     lTempInspectDefinition );
            } );

      final TaskKey lPermanentRepairUnderFault1 = Domain.createRequirement( aRequirement -> {
         aRequirement.setDefinition( lPermanentRepairDefinition );
         aRequirement.setInventory( lAircraft );
      } );

      final TaskKey lTempInspectUnderFault1 = Domain.createRequirement( aRequirement -> {
         aRequirement.setDefinition( lTempInspectDefinition );
         aRequirement.setInventory( lAircraft );
      } );

      final TaskKey lTempInspectUnderFault2 = Domain.createRequirement( aRequirement -> {
         aRequirement.setDefinition( lTempInspectDefinition );
         aRequirement.setInventory( lAircraft );
      } );

      final TaskKey lLooseTempInspect = Domain.createRequirement( aRequirement -> {
         aRequirement.setDefinition( lTempInspectDefinition );
         aRequirement.setInventory( lAircraft );
      } );

      Domain.createFault( aFault -> {
         aFault.addFaultRelatedTask( lPermanentRepairUnderFault1 );
         aFault.addFaultRelatedTask( lTempInspectUnderFault1 );
         aFault.setInventory( lAircraft );
      } );

      Domain.createFault( aFault -> {
         aFault.addFaultRelatedTask( lTempInspectUnderFault2 );
         aFault.setInventory( lAircraft );
      } );

      List<TaskKey> lDependentTasks = TaskDepService.getDependentTask( lPermanentRepairUnderFault1,
            RefTaskDepActionKey.TERMINATE, RefEventStatusKey.ACTV );

      assertTrue( "Temperary inspection under same fault is not returned",
            lDependentTasks.contains( lTempInspectUnderFault1 ) );
      assertFalse( "Temperary inspection under other fault is returned",
            lDependentTasks.contains( lTempInspectUnderFault2 ) );
      assertFalse( "Temperary inspection not under any fault is returned",
            lDependentTasks.contains( lLooseTempInspect ) );

   }


   /**
    * This test case is testing when getting dependent tasks of a follow-on task under no fault, it
    * will only return those which are under no fault.
    *
    * <pre>
    *    Given a follow-on task definition on an assembly root config slot.
    *    And another follow-on task which has a terminate dependency with the first task on the same assembly config slot.
    *    And an actual task based on the driven follow-on task definition on an aircraft under no fault.
    *    And an actual dependent task based on the same aircraft under no fault.
    *    And an actual dependent task based on the same aircraft under the a fault.
    *    When get the dependent tasks of the first driven follow-on task.
    *    Then verify it only returns the dependent task under the no fault.
    * </pre>
    */
   @Test
   public void itGetsDependentTasksNotUnderAnyFaults() {
      final AssemblyKey lAircraftAssembly = Domain.createAircraftAssembly();

      final InventoryKey lAircraft = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssembly( lAircraftAssembly );
      } );

      final TaskTaskKey lTempInspectDefinition =
            Domain.createRequirementDefinition( aRequirementDefinition -> {
               aRequirementDefinition.againstConfigurationSlot(
                     Domain.readRootConfigurationSlot( lAircraftAssembly ) );
            } );

      final TaskTaskKey lPermanentRepairDefinition =
            Domain.createRequirementDefinition( aRequirementDefinition -> {
               aRequirementDefinition.againstConfigurationSlot(
                     Domain.readRootConfigurationSlot( lAircraftAssembly ) );
               aRequirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.TERMINATE,
                     lTempInspectDefinition );
            } );

      final TaskKey lPermanentRepairUnderNoFault = Domain.createRequirement( aRequirement -> {
         aRequirement.setDefinition( lPermanentRepairDefinition );
         aRequirement.setInventory( lAircraft );
      } );

      final TaskKey lTempInspectUnderFault = Domain.createRequirement( aRequirement -> {
         aRequirement.setDefinition( lTempInspectDefinition );
         aRequirement.setInventory( lAircraft );
      } );

      final TaskKey lLooseTempInspect = Domain.createRequirement( aRequirement -> {
         aRequirement.setDefinition( lTempInspectDefinition );
         aRequirement.setInventory( lAircraft );
      } );

      Domain.createFault( aFault -> {
         aFault.addFaultRelatedTask( lTempInspectUnderFault );
         aFault.setInventory( lAircraft );
      } );

      List<TaskKey> lDependentTasks = TaskDepService.getDependentTask( lPermanentRepairUnderNoFault,
            RefTaskDepActionKey.TERMINATE, RefEventStatusKey.ACTV );

      assertTrue( "Loose Temperary inspection is not returned",
            lDependentTasks.contains( lLooseTempInspect ) );

      assertFalse( "Temperary inspection under fault is returned",
            lDependentTasks.contains( lTempInspectUnderFault ) );
   }
}
