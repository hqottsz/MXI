
package com.mxi.mx.web.query.task;

import static com.mxi.am.domain.Domain.createWorkPackage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.WorkPackage;
import com.mxi.am.domain.builder.ConfigSlotBuilder;
import com.mxi.am.domain.builder.ConfigSlotPositionBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.PartRequirementDomainBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.TaskKey;


/**
 * This class tests the PreviewReleaseMissingComponentList query.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class PreviewReleaseMissingComponentListTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Tests that the empty position is returned along with its parent inventory and the parent of a
    * JIC task with no work package.
    *
    * @throws Exception
    *            If an error occurs
    */
   @Test
   public void testMissingComponentJicTaskNoWP() throws Exception {
      ConfigSlotKey lHoleConfigSlot = new ConfigSlotBuilder( "HOLE" ).withName( "Hole" ).build();
      ConfigSlotPositionKey lHolePosition =
            new ConfigSlotPositionBuilder().withConfigSlot( lHoleConfigSlot ).build();
      InventoryKey lParentInventory =
            new InventoryBuilder().withDescription( "Parent Inv" ).build();

      TaskKey lReplTask =
            new TaskBuilder().onInventory( lParentInventory ).withTaskClass( RefTaskClassKey.REPL )
                  .withName( "Test Repl" ).withBarcode( "T000001" ).build();

      TaskKey lJicTask = new TaskBuilder().onInventory( lParentInventory )
            .withTaskClass( RefTaskClassKey.JIC ).withParentTask( lReplTask ).build();

      new PartRequirementDomainBuilder( lJicTask ).forPosition( lHolePosition ).build();

      DataSet lResult = executeQuery( lParentInventory, lHolePosition );

      assertEquals( 1, lResult.getRowCount() );

      // move to the first row
      lResult.next();

      assertEquals( "Hole position key", lHolePosition,
            lResult.getKey( ConfigSlotPositionKey.class, "bom_item_position_key" ) );
      assertEquals( "Hole position label", "HOLE (Hole)", lResult.getString( "bom_item_label" ) );
      assertEquals( "Hole position code", lHolePosition.toString(),
            lResult.getString( "eqp_pos_cd" ) );
      assertEquals( "Parent inventory", lParentInventory,
            lResult.getKey( InventoryKey.class, "parent_inv_key" ) );
      assertEquals( "Parent inventory label", "Parent Inv",
            lResult.getString( "parent_inv_sdesc" ) );

      assertEquals( "Task is the REPL", lReplTask, lResult.getKey( TaskKey.class, "task_key" ) );
      assertEquals( "Task description", "Test Repl", lResult.getString( "event_sdesc" ) );
      assertEquals( "Task barcode", "T000001", lResult.getString( "barcode_sdesc" ) );
      assertNull( "Work Package is null", lResult.getKey( TaskKey.class, "check_key" ) );
      assertNull( "Work Package description is null", lResult.getString( "check_desc" ) );
   }


   /**
    * Tests that the empty position is returned along with its parent inventory and the parent of a
    * JIC task with a work package.
    *
    * @throws Exception
    *            If an error occurs
    */
   @Test
   public void testMissingComponentJicTaskWithWP() throws Exception {
      ConfigSlotKey lHoleConfigSlot = new ConfigSlotBuilder( "HOLE" ).withName( "Hole" ).build();
      ConfigSlotPositionKey lHolePosition =
            new ConfigSlotPositionBuilder().withConfigSlot( lHoleConfigSlot ).build();
      InventoryKey lParentInventory =
            new InventoryBuilder().withDescription( "Parent Inv" ).build();

      TaskKey lWPTask = createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {
            aWorkPackage.setName( "Test WP" );
         }
      } );

      TaskKey lReplTask = new TaskBuilder().onInventory( lParentInventory )
            .withTaskClass( RefTaskClassKey.REPL ).withParentTask( lWPTask ).withName( "Test Repl" )
            .withBarcode( "T000001" ).build();

      TaskKey lJicTask = new TaskBuilder().onInventory( lParentInventory )
            .withTaskClass( RefTaskClassKey.JIC ).withParentTask( lReplTask ).build();

      new PartRequirementDomainBuilder( lJicTask ).forPosition( lHolePosition ).build();

      DataSet lResult = executeQuery( lParentInventory, lHolePosition );

      assertEquals( 1, lResult.getRowCount() );

      // move to the first row
      lResult.next();

      assertEquals( "Hole position key", lHolePosition,
            lResult.getKey( ConfigSlotPositionKey.class, "bom_item_position_key" ) );
      assertEquals( "Hole position label", "HOLE (Hole)", lResult.getString( "bom_item_label" ) );
      assertEquals( "Hole position code", lHolePosition.toString(),
            lResult.getString( "eqp_pos_cd" ) );
      assertEquals( "Parent inventory", lParentInventory,
            lResult.getKey( InventoryKey.class, "parent_inv_key" ) );
      assertEquals( "Parent inventory label", "Parent Inv",
            lResult.getString( "parent_inv_sdesc" ) );

      assertEquals( "Task is the REPL", lReplTask, lResult.getKey( TaskKey.class, "task_key" ) );
      assertEquals( "Task description", "Test Repl", lResult.getString( "event_sdesc" ) );
      assertEquals( "Task barcode", "T000001", lResult.getString( "barcode_sdesc" ) );
      assertEquals( "Work Package", lWPTask, lResult.getKey( TaskKey.class, "check_key" ) );
      assertEquals( "Work Package description", "Test WP", lResult.getString( "check_desc" ) );
   }


   /**
    * Tests that the empty position is returned along with its parent inventory and no task or work
    * package.
    *
    * @throws Exception
    *            If an error occurs
    */
   @Test
   public void testMissingComponentNoTask() throws Exception {
      ConfigSlotKey lHoleConfigSlot = new ConfigSlotBuilder( "HOLE" ).withName( "Hole" ).build();
      ConfigSlotPositionKey lHolePosition =
            new ConfigSlotPositionBuilder().withConfigSlot( lHoleConfigSlot ).build();
      InventoryKey lParentInventory =
            new InventoryBuilder().withDescription( "Parent Inv" ).build();

      DataSet lResult = executeQuery( lParentInventory, lHolePosition );

      assertEquals( 1, lResult.getRowCount() );

      // move to the first row
      lResult.next();

      assertEquals( "Hole position key", lHolePosition,
            lResult.getKey( ConfigSlotPositionKey.class, "bom_item_position_key" ) );
      assertEquals( "Hole position label", "HOLE (Hole)", lResult.getString( "bom_item_label" ) );
      assertEquals( "Hole position code", lHolePosition.toString(),
            lResult.getString( "eqp_pos_cd" ) );
      assertEquals( "Parent inventory", lParentInventory,
            lResult.getKey( InventoryKey.class, "parent_inv_key" ) );
      assertEquals( "Parent inventory label", "Parent Inv",
            lResult.getString( "parent_inv_sdesc" ) );

      assertNull( "Task is null", lResult.getKey( TaskKey.class, "task_key" ) );
      assertNull( "Task description is null", lResult.getString( "event_sdesc" ) );
      assertNull( "Task barcode is null", lResult.getString( "barcode_sdesc" ) );
      assertNull( "Work Package is null", lResult.getKey( TaskKey.class, "check_key" ) );
      assertNull( "Work Package description is null", lResult.getString( "check_desc" ) );
   }


   /**
    * Tests that the empty position is returned along with its parent inventory and the REPL task
    * which has the part requirement with no work package.
    *
    * @throws Exception
    *            If an error occurs
    */
   @Test
   public void testMissingComponentReplTaskNoWP() throws Exception {
      ConfigSlotKey lHoleConfigSlot = new ConfigSlotBuilder( "HOLE" ).withName( "Hole" ).build();
      ConfigSlotPositionKey lHolePosition =
            new ConfigSlotPositionBuilder().withConfigSlot( lHoleConfigSlot ).build();
      InventoryKey lParentInventory =
            new InventoryBuilder().withDescription( "Parent Inv" ).build();

      TaskKey lReplTask =
            new TaskBuilder().onInventory( lParentInventory ).withTaskClass( RefTaskClassKey.REPL )
                  .withName( "Test Repl" ).withBarcode( "T000001" ).build();

      new PartRequirementDomainBuilder( lReplTask ).forPosition( lHolePosition ).build();

      DataSet lResult = executeQuery( lParentInventory, lHolePosition );

      assertEquals( 1, lResult.getRowCount() );

      // move to the first row
      lResult.next();

      assertEquals( "Hole position key", lHolePosition,
            lResult.getKey( ConfigSlotPositionKey.class, "bom_item_position_key" ) );
      assertEquals( "Hole position label", "HOLE (Hole)", lResult.getString( "bom_item_label" ) );
      assertEquals( "Hole position code", lHolePosition.toString(),
            lResult.getString( "eqp_pos_cd" ) );
      assertEquals( "Parent inventory", lParentInventory,
            lResult.getKey( InventoryKey.class, "parent_inv_key" ) );
      assertEquals( "Parent inventory label", "Parent Inv",
            lResult.getString( "parent_inv_sdesc" ) );

      assertEquals( "Task is the REPL", lReplTask, lResult.getKey( TaskKey.class, "task_key" ) );
      assertEquals( "Task description", "Test Repl", lResult.getString( "event_sdesc" ) );
      assertEquals( "Task barcode", "T000001", lResult.getString( "barcode_sdesc" ) );
      assertNull( "Work Package is null", lResult.getKey( TaskKey.class, "check_key" ) );
      assertNull( "Work Package description is null", lResult.getString( "check_desc" ) );
   }


   /**
    * Tests that the empty position is returned along with its parent inventory and the REPL task
    * which has the part requirement with a work package.
    *
    * @throws Exception
    *            If an error occurs
    */
   @Test
   public void testMissingComponentReplTaskWithWP() throws Exception {
      ConfigSlotKey lHoleConfigSlot = new ConfigSlotBuilder( "HOLE" ).withName( "Hole" ).build();
      ConfigSlotPositionKey lHolePosition =
            new ConfigSlotPositionBuilder().withConfigSlot( lHoleConfigSlot ).build();
      InventoryKey lParentInventory =
            new InventoryBuilder().withDescription( "Parent Inv" ).build();

      TaskKey lWPTask = createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aBuilder ) {
            aBuilder.setName( "Test WP" );
         }
      } );

      TaskKey lReplTask = new TaskBuilder().onInventory( lParentInventory )
            .withTaskClass( RefTaskClassKey.REPL ).withParentTask( lWPTask ).withName( "Test Repl" )
            .withBarcode( "T000001" ).build();

      new PartRequirementDomainBuilder( lReplTask ).forPosition( lHolePosition ).build();

      DataSet lResult = executeQuery( lParentInventory, lHolePosition );

      assertEquals( 1, lResult.getRowCount() );

      // move to the first row
      lResult.next();

      assertEquals( "Hole position key", lHolePosition,
            lResult.getKey( ConfigSlotPositionKey.class, "bom_item_position_key" ) );
      assertEquals( "Hole position label", "HOLE (Hole)", lResult.getString( "bom_item_label" ) );
      assertEquals( "Hole position code", lHolePosition.toString(),
            lResult.getString( "eqp_pos_cd" ) );
      assertEquals( "Parent inventory", lParentInventory,
            lResult.getKey( InventoryKey.class, "parent_inv_key" ) );
      assertEquals( "Parent inventory label", "Parent Inv",
            lResult.getString( "parent_inv_sdesc" ) );

      assertEquals( "Task is the REPL", lReplTask, lResult.getKey( TaskKey.class, "task_key" ) );
      assertEquals( "Task description", "Test Repl", lResult.getString( "event_sdesc" ) );
      assertEquals( "Task barcode", "T000001", lResult.getString( "barcode_sdesc" ) );
      assertEquals( "Work Package", lWPTask, lResult.getKey( TaskKey.class, "check_key" ) );
      assertEquals( "Work Package description", "Test WP", lResult.getString( "check_desc" ) );
   }


   /**
    * Executes the query under test with the given arguments.
    *
    * @param aParentInventory
    *           The parent inventory
    * @param aConfigSlotPosition
    *           The config slot position of the hole
    *
    * @return The results
    */
   private DataSet executeQuery( InventoryKey aParentInventory,
         ConfigSlotPositionKey aConfigSlotPosition ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aParentInventory, "aParentInvNoDbId", "aParentInvNoId" );
      lArgs.add( aConfigSlotPosition, "aAssmblDbId", "aAssmblCd", "aAssmblBomId", "aAssmblPosId" );

      return MxDataAccess.getInstance()
            .executeQuery( "com.mxi.mx.web.query.task.PreviewReleaseMissingComponentList", lArgs );
   }

}
