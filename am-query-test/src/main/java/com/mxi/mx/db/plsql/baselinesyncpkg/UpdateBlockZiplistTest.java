
package com.mxi.mx.db.plsql.baselinesyncpkg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.AssemblyBuilder;
import com.mxi.am.domain.builder.ConfigSlotBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.PartGroupDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.domain.builder.TaskRevisionBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefAssmblClassKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.TaskDefnKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.plsql.StoredProcedureCall;
import com.mxi.mx.core.table.task.TaskTaskTable;


/**
 * Test case for BaselineSyncPkg.updateBlockZiplist() plsql procedure.<br>
 * <br>
 * This is the updateBlockZiplist() procedure that accepts a zip id, a task key, and a highest
 * inventory key.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class UpdateBlockZiplistTest {

   private static final RefTaskClassKey BLOCK_TASK_CLASS = new RefTaskClassKey( 10, "BLOCK" );

   private static final String ACFT_ASSY = "ACFTASSY";
   private static final String ACFT_CS = "ACFT_CS";
   private static final String SYS1_CS = "SYS1_CS";
   private static final String SYS2_CS = "SYS2_CS";
   private static final String SUBASSY1_CS = "SA1_CS";
   private static final String ACFT_PG = "ACFT_PG";
   private static final String SUBASSY1_PG = "SA1_PG";
   private static final String ENG_ASSY = "ENG_ASSY";
   private static final String ENG_CS = "ENG_CS";
   private static final String ENG_PG = "ENG_PG";
   private static final int ZIP_ID = 1;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Given:
    *
    * <ol>
    * <li>- aircraft assembly contains a multi-position sub-assembly config slot</li>
    * <li>- a block revision is defined against that sub-assembly config slot</li>
    * <li>- a requirement revision is defined against that sub-assembly config slot</li>
    * <li>-</li>
    * </ol>
    *
    * Verify that when there exists a block and req definition against a multi-position sub-assembly
    * and inventory are installed into both of the sub-assembly postions, that when
    * updateBlockZiplist() is requested for the block defn and one of the installed inventory the
    * req task are zipped into the block task on the same inventory. i.e. ensure the req tasks do
    * not get zipping into block tasks on other inventory.
    */
   @Test
   public void testBlockDefnAndReqDefnAgainstMultiPosSubassy() {

      // Set up an aircraft assembly with the following configuration:
      //
      // ACFT CS
      // ...+- SYS1 CS
      // ......+- SUBASSY CS [2 pos]
      //
      AssemblyKey lAcftAssy =
            new AssemblyBuilder( ACFT_ASSY ).withClass( RefAssmblClassKey.ACFT ).build();
      ConfigSlotKey lAcftConfigSlot = new ConfigSlotBuilder( ACFT_CS ).withRootAssembly( lAcftAssy )
            .withClass( RefBOMClassKey.ROOT ).build();
      PartGroupKey lAcftPartGroup =
            new PartGroupDomainBuilder( ACFT_PG ).withConfigSlot( lAcftConfigSlot ).build();
      PartNoKey lAcftPart = new PartNoBuilder().isAlternateIn( lAcftPartGroup ).build();

      ConfigSlotKey lSys1ConfigSlot = new ConfigSlotBuilder( SYS1_CS ).withParent( lAcftConfigSlot )
            .withClass( RefBOMClassKey.SYS ).build();

      ConfigSlotKey lSubassy1ConfigSlot =
            new ConfigSlotBuilder( SUBASSY1_CS ).withClass( RefBOMClassKey.SUBASSY )
                  .withParent( lSys1ConfigSlot ).withNumberOfPositions( 2 ).build();
      PartGroupKey lSubassy1PartGroup =
            new PartGroupDomainBuilder( SUBASSY1_PG ).withConfigSlot( lSubassy1ConfigSlot ).build();
      new PartNoBuilder().isAlternateIn( lSubassy1PartGroup ).build();

      //
      // Set up an engine assembly that may be installed in the ACFT SUBASSY CS.
      //
      AssemblyKey lEngAssy =
            new AssemblyBuilder( ENG_ASSY ).withClass( RefAssmblClassKey.ENG ).build();
      ConfigSlotKey lEngConfigSlot = new ConfigSlotBuilder( ENG_CS ).withRootAssembly( lEngAssy )
            .withClass( RefBOMClassKey.ROOT ).build();
      PartGroupKey lEngPartGroup =
            new PartGroupDomainBuilder( ENG_PG ).withConfigSlot( lEngConfigSlot ).build();
      PartNoKey lEngPart = new PartNoBuilder().isAlternateIn( lEngPartGroup ).build();

      //
      // Create a BLOCK and REQ task revision against the ACFT's SUBASSY CS.
      //
      TaskTaskKey lSubassy1ReqRev = new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ )
            .withConfigSlot( lSubassy1ConfigSlot ).build();
      TaskDefnKey lSubassy1ReqDefn =
            TaskTaskTable.findByPrimaryKey( lSubassy1ReqRev ).getTaskDefn();
      TaskTaskKey lSubassy1BlockRev = new TaskRevisionBuilder().withTaskClass( BLOCK_TASK_CLASS )
            .withConfigSlot( lSubassy1ConfigSlot ).mapToRequirement( lSubassy1ReqDefn ).isUnique()
            .build();

      //
      // Create an aircraft and 2 engine inventory, and install the engines on the aircraft.
      //
      InventoryKey lAcftInv = new InventoryBuilder().withPartNo( lAcftPart )
            .withConfigSlotPosition( new ConfigSlotPositionKey( lAcftConfigSlot, 1 ) ).allowSync()
            .build();
      InventoryKey lEngInv1 = new InventoryBuilder().withPartNo( lEngPart )
            .withConfigSlotPosition( new ConfigSlotPositionKey( lSubassy1ConfigSlot, 1 ) )
            .withParentInventory( lAcftInv ).withAssemblyInventory( lAcftInv )
            .withClass( RefInvClassKey.ASSY ).allowSync().build();
      InventoryKey lEngInv2 = new InventoryBuilder().withPartNo( lEngPart )
            .withConfigSlotPosition( new ConfigSlotPositionKey( lSubassy1ConfigSlot, 2 ) )
            .withParentInventory( lAcftInv ).withAssemblyInventory( lAcftInv )
            .withClass( RefInvClassKey.ASSY ).allowSync().build();

      //
      // Create a BLOCK task against one of the installed engines.
      //
      TaskKey lBlockTask1 = new TaskBuilder().withTaskRevision( lSubassy1BlockRev )
            .onInventory( lEngInv1 ).withTaskClass( BLOCK_TASK_CLASS ).build();

      //
      // Create a BLOCK task against the other installed engine.
      //
      new TaskBuilder().withTaskRevision( lSubassy1BlockRev ).onInventory( lEngInv2 )
            .withTaskClass( BLOCK_TASK_CLASS ).build();

      //
      // Execute the Plsql procedure under test.
      //
      updateBlockZiplist( lSubassy1BlockRev, lEngInv1 );

      //
      // Verify that a zip task is created for only the block task against the one engine.
      //
      assertZipTask( ZIP_ID, lBlockTask1, BLOCK_TASK_CLASS.getCd(), lEngInv1 );
      assertEquals( "Unexpected number of rows in zip_task.", 1,
            QuerySetFactory.getInstance().executeQueryTable( "zip_task", null ).getRowCount() );
   }


   /**
    * Verify that when there exists a block and req definition against a single position
    * sub-assembly, that the req task is zipped into the block task on the same inventory.
    */
   @Test
   public void testBlockDefnAndReqDefnAgainstSubassy() {

      // Set up an aircraft assembly with the following configuration:
      //
      // ACFT CS
      // ...+- SYS1 CS
      // ......+- SUBASSY CS
      //
      AssemblyKey lAcftAssy =
            new AssemblyBuilder( ACFT_ASSY ).withClass( RefAssmblClassKey.ACFT ).build();
      ConfigSlotKey lAcftConfigSlot = new ConfigSlotBuilder( ACFT_CS ).withRootAssembly( lAcftAssy )
            .withClass( RefBOMClassKey.ROOT ).build();
      PartGroupKey lAcftPartGroup =
            new PartGroupDomainBuilder( ACFT_PG ).withConfigSlot( lAcftConfigSlot ).build();
      PartNoKey lAcftPart = new PartNoBuilder().isAlternateIn( lAcftPartGroup ).build();

      ConfigSlotKey lSys1ConfigSlot = new ConfigSlotBuilder( SYS1_CS ).withParent( lAcftConfigSlot )
            .withClass( RefBOMClassKey.SYS ).build();

      ConfigSlotKey lSubassy1ConfigSlot =
            new ConfigSlotBuilder( SUBASSY1_CS ).withClass( RefBOMClassKey.SUBASSY )
                  .withParent( lSys1ConfigSlot ).withNumberOfPositions( 1 ).build();
      PartGroupKey lSubassy1PartGroup =
            new PartGroupDomainBuilder( SUBASSY1_PG ).withConfigSlot( lSubassy1ConfigSlot ).build();
      new PartNoBuilder().isAlternateIn( lSubassy1PartGroup ).build();

      //
      // Set up an engine assembly that may be installed in the ACFT SUBASSY CS.
      //
      AssemblyKey lEngAssy =
            new AssemblyBuilder( ENG_ASSY ).withClass( RefAssmblClassKey.ENG ).build();
      ConfigSlotKey lEngConfigSlot = new ConfigSlotBuilder( ENG_CS ).withRootAssembly( lEngAssy )
            .withClass( RefBOMClassKey.ROOT ).build();
      PartGroupKey lEngPartGroup =
            new PartGroupDomainBuilder( ENG_PG ).withConfigSlot( lEngConfigSlot ).build();
      PartNoKey lEngPart = new PartNoBuilder().isAlternateIn( lEngPartGroup ).build();

      //
      // Create a BLOCK and REQ task revision against the ACFT's SUBASSY CS.
      //
      TaskTaskKey lSubassy1ReqRev = new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ )
            .withConfigSlot( lSubassy1ConfigSlot ).build();
      TaskDefnKey lSubassy1ReqDefn =
            TaskTaskTable.findByPrimaryKey( lSubassy1ReqRev ).getTaskDefn();
      TaskTaskKey lSubassy1BlockRev = new TaskRevisionBuilder().withTaskClass( BLOCK_TASK_CLASS )
            .withConfigSlot( lSubassy1ConfigSlot ).mapToRequirement( lSubassy1ReqDefn ).isUnique()
            .build();

      //
      // Create and aircraft and engine inventory, and install the engine on the aircraft.
      //
      InventoryKey lAcftInv = new InventoryBuilder().withPartNo( lAcftPart )
            .withConfigSlotPosition( new ConfigSlotPositionKey( lAcftConfigSlot, 1 ) ).allowSync()
            .build();
      InventoryKey lEngInv = new InventoryBuilder().withPartNo( lEngPart )
            .withConfigSlotPosition( new ConfigSlotPositionKey( lSubassy1ConfigSlot, 1 ) )
            .withParentInventory( lAcftInv ).withAssemblyInventory( lAcftInv )
            .withClass( RefInvClassKey.ASSY ).allowSync().build();

      //
      // Create a BLOCK task against the installed engine.
      //
      TaskKey lBlockTask = new TaskBuilder().withTaskRevision( lSubassy1BlockRev )
            .onInventory( lEngInv ).withTaskClass( BLOCK_TASK_CLASS ).build();

      //
      // Execute the Plsql procedure under test.
      //
      updateBlockZiplist( lSubassy1BlockRev, lAcftInv );

      // Verify that a zip task is created for the block task against the installed engine.
      assertZipTask( ZIP_ID, lBlockTask, BLOCK_TASK_CLASS.getCd(), lEngInv );
      assertEquals( "Unexpected number of rows in zip_task.", 1,
            QuerySetFactory.getInstance().executeQueryTable( "zip_task", null ).getRowCount() );
   }


   /**
    * Verify that when there exists a block definition against one SYS config slot of an aircraft
    * and there exists a requirement definition against another SYS config slot of an aircraft
    * (sibling SYS CSs), that the req task is zipped into the block task.
    */
   @Test
   public void testBlockDefnOnSysAndReqDefnOnSiblingSys() {

      // Set up an aircraft assembly with the following configuration:
      //
      // ACFT CS
      // ...+- SYS1 CS
      // ...+- SYS2 CS
      //
      AssemblyKey lAcftAssy =
            new AssemblyBuilder( ACFT_ASSY ).withClass( RefAssmblClassKey.ACFT ).build();
      ConfigSlotKey lAcftConfigSlot = new ConfigSlotBuilder( ACFT_CS ).withRootAssembly( lAcftAssy )
            .withClass( RefBOMClassKey.ROOT ).build();
      PartGroupKey lAcftPartGroup =
            new PartGroupDomainBuilder( ACFT_PG ).withConfigSlot( lAcftConfigSlot ).build();
      PartNoKey lAcftPart = new PartNoBuilder().isAlternateIn( lAcftPartGroup ).build();

      ConfigSlotKey lSys1ConfigSlot = new ConfigSlotBuilder( SYS1_CS ).withParent( lAcftConfigSlot )
            .withClass( RefBOMClassKey.SYS ).build();
      ConfigSlotKey lSys2ConfigSlot = new ConfigSlotBuilder( SYS2_CS ).withParent( lAcftConfigSlot )
            .withClass( RefBOMClassKey.SYS ).build();

      //
      // Create a REQ task revision against one SYS config slot.
      //
      TaskTaskKey lReqRev = new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ )
            .withConfigSlot( lSys1ConfigSlot ).build();
      TaskDefnKey lReqDefn = TaskTaskTable.findByPrimaryKey( lReqRev ).getTaskDefn();

      //
      // Create a BLOCK task revision against the other SYS config slot.
      //
      TaskTaskKey lBlockRev = new TaskRevisionBuilder().withTaskClass( BLOCK_TASK_CLASS )
            .withConfigSlot( lSys2ConfigSlot ).mapToRequirement( lReqDefn ).isUnique().build();

      //
      // Create an aircraft inventory and inventory for both SYS.
      //
      InventoryKey lAcftInv = new InventoryBuilder().withPartNo( lAcftPart )
            .withConfigSlotPosition( new ConfigSlotPositionKey( lAcftConfigSlot, 1 ) ).allowSync()
            .build();
      new InventoryBuilder()
            .withConfigSlotPosition( new ConfigSlotPositionKey( lSys1ConfigSlot, 1 ) )
            .withParentInventory( lAcftInv ).withAssemblyInventory( lAcftInv )
            .withClass( RefInvClassKey.SYS ).allowSync().build();

      InventoryKey lSys2Inv = new InventoryBuilder()
            .withConfigSlotPosition( new ConfigSlotPositionKey( lSys2ConfigSlot, 1 ) )
            .withParentInventory( lAcftInv ).withAssemblyInventory( lAcftInv )
            .withClass( RefInvClassKey.SYS ).allowSync().build();

      //
      // Create a BLOCK task.
      //
      TaskKey lBlockTask = new TaskBuilder().withTaskRevision( lBlockRev ).onInventory( lSys2Inv )
            .withTaskClass( BLOCK_TASK_CLASS ).build();

      //
      // Execute the Plsql procedure under test.
      //
      updateBlockZiplist( lBlockRev, lAcftInv );

      //
      // Verify that a zip task is created for the block task.
      //
      assertZipTask( ZIP_ID, lBlockTask, BLOCK_TASK_CLASS.getCd(), lAcftInv );
      assertEquals( 1,
            QuerySetFactory.getInstance().executeQueryTable( "zip_task", null ).getRowCount() );
   }


   /**
    * Verfiy that a row exists in the zip_task table with the provided information.
    *
    * @param aZipId
    * @param aTaskKey
    * @param aClassMode
    * @param aAssmblInvKey
    */
   private void assertZipTask( int aZipId, TaskKey aTaskKey, String aClassMode,
         InventoryKey aAssmblInvKey ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "zip_id", aZipId );
      lArgs.add( aTaskKey, "sched_db_id", "sched_id" );
      lArgs.add( "class_mode_cd", aClassMode );
      lArgs.add( aAssmblInvKey, "assmbl_inv_no_db_id", "assmbl_inv_no_id" );

      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "zip_task", lArgs );

      // Verify the zip_task table contains one row for the provided parameters.
      assertFalse( "Expected row in zip_task does not exists.", lQs.isEmpty() );
   }


   /**
    * Execute the stored procedure under test using the provided parameters.
    *
    * @param aBlockRevKey
    *           block task key
    * @param aHighestInvKey
    *           highest inventory key
    */
   private void updateBlockZiplist( TaskTaskKey aBlockRevKey, InventoryKey aHighestInvKey ) {
      StoredProcedureCall lStoredProcedureCall = StoredProcedureCall.getInstance();
      lStoredProcedureCall.updateBlockZipList( ZIP_ID, aBlockRevKey, aHighestInvKey );
   }
}
