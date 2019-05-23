package com.mxi.mx.core.query.bsync;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.builder.AssemblyBuilder;
import com.mxi.am.domain.builder.ConfigSlotBuilder;
import com.mxi.am.domain.builder.ConfigSlotPositionBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.TaskRevisionBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.TaskDefnKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.task.TaskTaskTable;


/**
 * This test ensures that all the related inventories are added to inv_sync_queue after a actual N/A
 * task of a refrence document is marked as error.
 *
 * @author Libin Cai
 * @created April 25, 2014
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class UpdateSyncInvForReqComplyingToRefDocTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   /** The table columns for inventory that needs to be synchronized */
   private static final String[] INV_COLUMNS = new String[] { "inv_no_db_id", "inv_no_id" };

   /** The table where inventory for synchronization gets queues */
   private static final String INV_SYNC_TABLE_NAME = "inv_sync_queue";

   private static final RefTaskClassKey REF = new RefTaskClassKey( 10, "REF" );


   /**
    * When a N/A task that was initialized on an inventory by the reference document, is marked as
    * error, the inventories applicable to the requirement that complies to that reference document,
    * and installed on the same aircraft of the initialized inventory will be put into the inv sync
    * queue.
    */
   @Test
   public void testInventoryAddedToTheInvSyncQueue() {

      // create the aircraft assembly
      AssemblyKey lAcftAssembly = new AssemblyBuilder( "ACFT" ).build();

      // create the aircraft root config slot and position
      ConfigSlotKey lAcftConfigSlot =
            new ConfigSlotBuilder( "ACFT" ).withRootAssembly( lAcftAssembly ).build();
      ConfigSlotPositionKey lAcftPosition =
            new ConfigSlotPositionBuilder().withConfigSlot( lAcftConfigSlot ).build();

      // create the engine system config slot and position
      ConfigSlotKey lEngineSystemSlot =
            new ConfigSlotBuilder( "ENGINE_SYS" ).withRootAssembly( lAcftAssembly )
                  .withParent( lAcftConfigSlot ).withClass( RefBOMClassKey.SYS ).build();
      ConfigSlotPositionKey lEngineSystemPosition = new ConfigSlotPositionBuilder()
            .withConfigSlot( lEngineSystemSlot ).withParentPosition( lAcftPosition ).build();

      // create the aircraft
      InventoryKey lAcft = new InventoryBuilder().withConfigSlotPosition( lAcftPosition )
            .withClass( RefInvClassKey.ACFT ).build();

      // create the engine system
      InventoryKey lEngineSystem = new InventoryBuilder().withClass( RefInvClassKey.SYS )
            .withParentInventory( lAcft ).withAssemblyInventory( lAcft )
            .withConfigSlotPosition( lEngineSystemPosition ).build();

      // create reference document on the engine system config slot
      TaskTaskKey lRefDoc = new TaskRevisionBuilder().withTaskClass( REF ).withTaskCode( "REF" )
            .withConfigSlot( lEngineSystemSlot ).withRevisionNumber( 1 )
            .withStatus( RefTaskDefinitionStatusKey.SUPRSEDE ).build();

      TaskDefnKey lTaskDefn_RefDoc = TaskTaskTable.findByPrimaryKey( lRefDoc ).getTaskDefn();

      // Create another revision of the reference document to make sure lRefDoc is not the last
      // revision. This is used to test the lRefDoc which is initialized as N/A task is not
      // necessarily the last revision.
      new TaskRevisionBuilder().withTaskClass( REF ).withTaskDefn( lTaskDefn_RefDoc )
            .withRevisionNumber( 2 ).withStatus( RefTaskDefinitionStatusKey.ACTV ).build();

      // create requirement on the engine system config slot and complies lRefDoc
      @SuppressWarnings( "unused" )
      TaskTaskKey lComplyReq_1 = new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ )
            .withConfigSlot( lEngineSystemSlot ).withTaskCode( "REQ_1" )
            .compliesWith( lTaskDefn_RefDoc ).build();

      // create requirement on the aircraft root config slot and complies lRefDoc
      @SuppressWarnings( "unused" )
      TaskTaskKey lComplyReq_2 = new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ )
            .withConfigSlot( lAcftConfigSlot ).withTaskCode( "REQ_2" )
            .compliesWith( lTaskDefn_RefDoc ).build();

      InventoryKey lAnother_Acft = new InventoryBuilder().withConfigSlotPosition( lAcftPosition )
            .withClass( RefInvClassKey.ACFT ).build();

      // create the engine system
      InventoryKey lAnother_EngineSystem = new InventoryBuilder().withClass( RefInvClassKey.SYS )
            .withParentInventory( lAnother_Acft ).withAssemblyInventory( lAnother_Acft )
            .withConfigSlotPosition( lEngineSystemPosition ).build();

      // Execute synchronization queue for maintenance program. Assume the lRefDoc was initialized
      // on lEngineSystem inventory as N/A, and this N/A actual task is marked as error.
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lRefDoc, "aRefDocTaskDbId", "aRefDocTaskId" );
      lArgs.add( lEngineSystem, "aRefDocInvNoDbId", "aRefDocInvNoId" );

      QuerySet lQuerySet =
            QuerySetFactory.getInstance().executeQuery( INV_COLUMNS, INV_SYNC_TABLE_NAME, null );

      QueryExecutor.executeUpdate( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // Collect inventory that were added to syncronization queue
      Set<InventoryKey> lInventoryKeys = new HashSet<InventoryKey>();
      lQuerySet =
            QuerySetFactory.getInstance().executeQuery( INV_COLUMNS, INV_SYNC_TABLE_NAME, null );
      while ( lQuerySet.next() ) {
         lInventoryKeys.add( lQuerySet.getKey( InventoryKey.class, INV_COLUMNS ) );
      }

      // assert lAcft and lEngineSystem are added to the queue because the actual task is on this
      // aircraft
      assertEquals( "All related inventory should be added to the synchronization queue.", 2,
            lInventoryKeys.size() );
      assertTrue(
            "The engine system inventory, lEngineSystem applied to the requirement 'lComplyReq_1' "
                  + "should be added to the synchronization queue",
            lInventoryKeys.contains( lEngineSystem ) );
      assertTrue(
            "The aircraft inventory, lAcft applied to the requirement 'lComplyReq_2' should be "
                  + "added to the synchronization queue",
            lInventoryKeys.contains( lAcft ) );

      // assert lAnother_Acft and lAnother_EngineSystem are NOT added to the queue because the
      // actual task is NOT on an inventory installed on this aircraft
      assertFalse( "The engine system inventory, lAnother_EngineSystem should not be added to the "
            + "synchronization queue because the actual task is not on an inventory installed"
            + " on lAnother_Acft.", lInventoryKeys.contains( lAnother_EngineSystem ) );
      assertFalse(
            "The aircraft inventory, lAnother_Acft should not be added to the synchronization queue"
                  + " because the actual task is not on an inventory installed on lAnother_Acft",
            lInventoryKeys.contains( lAnother_Acft ) );
   }
}
