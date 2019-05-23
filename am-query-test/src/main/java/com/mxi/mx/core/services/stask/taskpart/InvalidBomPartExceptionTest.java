
package com.mxi.mx.core.services.stask.taskpart;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.AssemblyBuilder;
import com.mxi.am.domain.builder.ConfigSlotBuilder;
import com.mxi.am.domain.builder.ConfigSlotPositionBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.PartGroupDomainBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefAssmblClassKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.services.part.ApplicabilityRange;
import com.mxi.mx.core.table.inv.InvInvTable;


/**
 * Tests the {@link InvalidBomPartException} class.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class InvalidBomPartExceptionTest {

   private static final String APPL_CODE = "1";
   private static final String APPL_RANGE = "100-200";
   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Test to ensure the InvalidBomPartException is thrown when the part group has an inventory
    * class of batch but the part group is NOT on a "common hardware" assembly.
    *
    * <pre>
    *           This test needs the task's inventory to be applicable with the part group.
    *           One way to do this is to not set the applicability code of the inventory
    *           (empty codes are applicable to all ranges).
    * 
    *           This test also needs the part group to NOT be compatible with the task.
    *           One way to do that is to ensure the part group's assembly does not match
    *           the task inventory's assembly.
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void testValidationFailsWhenBatchPartGroupIsNotOnCommonHardwareAssembly()
         throws Exception {
      try {
         executeValidate( RefInvClassKey.BATCH, RefAssmblClassKey.ACFT );
         fail( "Expected InvalidBomPartException" );
      } catch ( InvalidBomPartException e ) {

         // Verify that the exception was thrown and contains the expected message.
         assertEquals( "core.err.30132", e.getMessageKey() );
      }
   }


   /**
    * Test to ensure the InvalidBomPartException is thrown when the part group has an inventory
    * class other than serial or batch.
    *
    * <pre>
    *          This test needs the task's inventory to be applicable with the part group.
    *          One way to do this is to not set the applicability code of the inventory
    *          (empty codes are applicable to all ranges).
    * 
    *          This test also needs the part group to NOT be compatible with the task.
    *          One way to do that is to ensure the part group's assembly does not match
    *          the task inventory's assembly.
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void testValidationFailsWhenPartGroupIsNeitherSerialNorBatch() throws Exception {

      try {
         executeValidate( RefInvClassKey.TRK, RefAssmblClassKey.ACFT );
         fail( "Expected InvalidBomPartException" );
      } catch ( InvalidBomPartException e ) {

         // Verify that the exception was thrown and contains the expected message.
         assertEquals( "core.err.30132", e.getMessageKey() );
      }
   }


   /**
    * Test to ensure the InvalidBomPartException is thrown when the part group has an inventory
    * class of serial but the part group is NOT on a "common hardware" assembly.
    *
    * <pre>
    *          This test needs the task's inventory to be applicable with the part group.
    *          One way to do this is to not set the applicability code of the inventory
    *          (empty codes are applicable to all ranges).
    * 
    *          This test also needs the part group to NOT be compatible with the task.
    *          One way to do that is to ensure the part group's assembly does not match
    *          the task inventory's assembly.
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void testValidationFailsWhenSerialPartGroupIsNotOnCommonHardwareAssembly()
         throws Exception {
      try {
         executeValidate( RefInvClassKey.SER, RefAssmblClassKey.ACFT );
         fail( "Expected InvalidBomPartException" );
      } catch ( InvalidBomPartException e ) {

         // Verify that the exception was thrown and contains the expected message.
         assertEquals( "core.err.30132", e.getMessageKey() );
      }
   }


   /**
    * Test to ensure the InvalidBomPartException is thrown when the inventory of the task is not
    * applicable to the part group of the part requirement.
    *
    * @throws Exception
    */
   @Test
   public void testValidationFailsWhenTaskInvNotApplicableWithPartGroup() throws Exception {

      // Ensure the test applicability code is not applicable to the test range.
      assertFalse( "Problem with test applicability code and range.",
            new ApplicabilityRange( APPL_RANGE ).isApplicable( APPL_CODE ) );

      // Create a config slot position.
      ConfigSlotKey lConfigSlot = new ConfigSlotBuilder( "" ).build();
      ConfigSlotPositionKey lConfigSlotPositionKey =
            new ConfigSlotPositionBuilder().withConfigSlot( lConfigSlot ).build();

      // Create a part group for the config slot that has an applicability range.
      PartGroupKey lPartGroupKey = new PartGroupDomainBuilder( "" ).withConfigSlot( lConfigSlot )
            .withApplicabilityRange( APPL_RANGE ).build();

      // Create an inventory with an applicability code that is outside the part group's
      // applicability range.
      InventoryKey lInventoryKey =
            new InventoryBuilder().withApplicabilityCode( APPL_CODE ).build();

      // Create a task against the inventory.
      TaskKey lTaskKey = new TaskBuilder().onInventory( lInventoryKey ).build();

      try {

         // Execute the test.
         InvalidBomPartException.validate( lTaskKey, lPartGroupKey, lConfigSlotPositionKey );

         fail( "Expected InvalidBomPartException" );
      } catch ( InvalidBomPartException e ) {

         // Verify that the exception was thrown and contains the expected message.
         assertEquals( "core.err.30296", e.getMessageKey() );
      }
   }


   /**
    * Test to ensure the InvalidBomPartException is NOT thrown when the part group has an inventory
    * class of batch and the part group is on a "common hardware" assembly.
    *
    * <pre>
    *         This test needs the task's inventory to be applicable with the part group.
    *         One way to do this is to not set the applicability code of the inventory
    *         (empty codes are applicable to all ranges).
    * 
    *         This test also needs the part group to NOT be compatible with the task.
    *         One way to do that is to ensure the part group's assembly does not match
    *         the task inventory's assembly.
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void testValidationPassesWhenBatchPartGroupIsOnCommonHardwareAssembly() throws Exception {
      try {
         executeValidate( RefInvClassKey.BATCH, RefAssmblClassKey.COMHW );
      } catch ( InvalidBomPartException e ) {
         fail( "InvalidBomPartException unexpected!" );
      }
   }


   /**
    * Test to ensure the InvalidBomPartException is NOT thrown when the part group is compatible
    * with the task.
    *
    * <pre>
    *      This test needs the task's inventory to be applicable with the part group.
    *      One way to do this is to not set the applicability code of the inventory
    *      (empty codes are applicable to all ranges).
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void testValidationPassesWhenPartGroupIsCompatibleWithTask() throws Exception {
      InventoryKey lInventoryKey = new InventoryBuilder().build();

      // Ensure the part group is compatible with the task.
      // One way is to have the task's inventory not have an assembly.
      InvInvTable lInvInvTable = InvInvTable.findByPrimaryKey( lInventoryKey );
      assertNull( "The inventory's assembly must not be set.", lInvInvTable.getAssmbl() );
      assertNull( "The inventory's original assembly must not be set.",
            lInvInvTable.getOrigAssmbl() );

      // Create a task against the inventory.
      TaskKey lTaskKey = new TaskBuilder().onInventory( lInventoryKey ).build();

      // Create a config slot position.
      ConfigSlotKey lConfigSlot = new ConfigSlotBuilder( "" ).build();
      ConfigSlotPositionKey lConfigSlotPositionKey =
            new ConfigSlotPositionBuilder().withConfigSlot( lConfigSlot ).build();

      // Create a part group for the config slot.
      PartGroupKey lPartGroupKey =
            new PartGroupDomainBuilder( "" ).withConfigSlot( lConfigSlot ).build();

      try {

         // Execute the test.
         InvalidBomPartException.validate( lTaskKey, lPartGroupKey, lConfigSlotPositionKey );
      } catch ( InvalidBomPartException e ) {
         fail( "InvalidBomPartException unexpected!" );
      }
   }


   /**
    * Test to ensure the InvalidBomPartException is NOT thrown when the part group has an inventory
    * class of serial and the part group is on a "common hardware" assembly.
    *
    * <pre>
    *         This test needs the task's inventory to be applicable with the part group.
    *         One way to do this is to not set the applicability code of the inventory
    *         (empty codes are applicable to all ranges).
    * 
    *         This test also needs the part group to NOT be compatible with the task.
    *         One way to do that is to ensure the part group's assembly does not match
    *         the task inventory's assembly.
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void testValidationPassesWhenSerialPartGroupIsOnCommonHardwareAssembly()
         throws Exception {
      try {
         executeValidate( RefInvClassKey.SER, RefAssmblClassKey.COMHW );
      } catch ( InvalidBomPartException e ) {
         fail( "InvalidBomPartException unexpected!" );
      }
   }


   /**
    * Test to ensure the InvalidBomPartException is NOT thrown, when it normally would be, because
    * the ALLOW_UNRELATED_PART_GROUP_IN_PART_REQUIREMENT config parameter is set to TRUE. Normally,
    * a batch part group that is not on common hardware would fail validation.
    *
    * <pre>
    *           This test needs the task's inventory to be applicable with the part group.
    *           One way to do this is to not set the applicability code of the inventory
    *           (empty codes are applicable to all ranges).
    * 
    *           This test also needs the part group to NOT be compatible with the task.
    *           One way to do that is to ensure the part group's assembly does not match
    *           the task inventory's assembly.
    * </pre>
    *
    * @throws Exception
    */
   @Test( expected = InvalidBomPartException.class )
   public void testValidationPassesWhenUnrelatedPartGroupIsEnabled() throws Exception {
      executeValidate( RefInvClassKey.BATCH, RefAssmblClassKey.ACFT );
   }


   /**
    * <pre>
    *      Helper method that sets up data and executes InvalidBomPartException.validate().
    *      Any exceptions, including InvalidBomPartException, are bubbled up.
    * 
    *      The provided inventory class is used to create the part group.
    *      The provided assembly class is used to create the config slot's assembly.
    * 
    *      Other notes about data setup:
    *        The task's inventory is made applicable with the part group by not setting
    *        the applicability code of the inventory.
    *        The part group is made to NOT be compatible with the task by having different
    *        assemblies for the part group and the task inventory.
    * </pre>
    *
    * @param aInvClass
    *           the part group's inventory class
    * @param aAssemblyClass
    *           the config slot assembly's class
    *
    * @throws Exception
    */
   private void executeValidate( RefInvClassKey aInvClass, RefAssmblClassKey aAssemblyClass )
         throws Exception {

      // Create an assembly with the provided assembly class
      AssemblyKey lConfigSlotAssemblyKey =
            new AssemblyBuilder( "CSA_CD" ).withClass( aAssemblyClass ).build();

      // Create a config slot on the assembly.
      ConfigSlotKey lConfigSlot =
            new ConfigSlotBuilder( "" ).withRootAssembly( lConfigSlotAssemblyKey ).build();

      // Create a position on the config slot.
      ConfigSlotPositionKey lConfigSlotPositionKey =
            new ConfigSlotPositionBuilder().withConfigSlot( lConfigSlot ).build();

      // Create a part group for the config slot and using the provided inventory class.
      PartGroupKey lPartGroupKey = new PartGroupDomainBuilder( "" ).withConfigSlot( lConfigSlot )
            .withInventoryClass( aInvClass ).build();

      // Create an inventory and set its original assembly (different then the config slot's).
      InventoryKey lInventoryKey = new InventoryBuilder()
            .withOriginalAssembly( new AssemblyBuilder( "IA_CD" ).build() ).build();

      // Create a task against the inventory.
      TaskKey lTaskKey = new TaskBuilder().onInventory( lInventoryKey ).build();

      // Execute the test.
      InvalidBomPartException.validate( lTaskKey, lPartGroupKey, lConfigSlotPositionKey );
   }

}
