
package com.mxi.mx.core.query.bsync;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.MaintPrgmKey;


/**
 * This test ensures that all the proper inventory are synchronized when a maintenance program is
 * activated. Covers 6 test cases: (New|Update|Remove) x (Only Carrier, Other Carrier). Only Carrier
 * means that OP1 is the only carrier that has the task definition in the latest active maintenance
 * program. Other Carrier means that a latest maintenance program that isn't OP1 has the task
 * definition.
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class UpdateSyncInvByMaintPrgmTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();


   @Before
   public void loadData() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
   }


   /** The table columns for inventory that needs to be synchronized */
   private static final String[] INV_COLUMNS = new String[] { "inv_no_db_id", "inv_no_id" };

   /** The table where inventory for synchronization gets queues */
   private static final String INV_SYNC_TABLE_NAME = "inv_sync_queue";

   /** An inventory with operator OP1 */
   private static final InventoryKey INVENTORY_OP1 = new InventoryKey( 4650, 1 );

   /** An inventory with operator OP2 */
   private static final InventoryKey INVENTORY_OP2 = new InventoryKey( 4650, 2 );

   /** Maintenance Program for Test Case (New, Only Carrier) */
   private static final MaintPrgmKey MAINT_PRGM_NEW_ONLY_CARRIER = new MaintPrgmKey( 4650, 11 );

   /** Maintenance Program for Test Case (New, Other Carrier) */
   private static final MaintPrgmKey MAINT_PRGM_NEW_OTHER_CARRIER = new MaintPrgmKey( 4650, 22 );

   /** Maintenance Program for Test Case (Update, Only Carrier) */
   private static final MaintPrgmKey MAINT_PRGM_UPDATE_ONLY_CARRIER = new MaintPrgmKey( 4650, 32 );

   /** Maintenance Program for Test Case (Update, Other Carrier) */
   private static final MaintPrgmKey MAINT_PRGM_UPDATE_OTHER_CARRIER = new MaintPrgmKey( 4650, 43 );

   /** Maintenance Program for Test Case (Remove, Only Carrier) */
   private static final MaintPrgmKey MAINT_PRGM_REMOVE_ONLY_CARRIER = new MaintPrgmKey( 4650, 52 );

   /** Maintenance Program for Test Case (Remove, Other Carrier) */
   private static final MaintPrgmKey MAINT_PRGM_REMOVE_OTHER_CARRIER = new MaintPrgmKey( 4650, 63 );


   /**
    * When a task is added to a maintenance program and no other carrier maintenance program has the
    * task, all inventory should be cancelled (except on the current maintenance program).
    */
   @Test
   public void testNew_OnlyCarrier() {
      Set<InventoryKey> lInventorySet = executeUpdate( MAINT_PRGM_NEW_ONLY_CARRIER );

      assertEquals( "All inventory should be added to the synchronization queue.", 2,
            lInventorySet.size() );
      assertTrue(
            "The maintenance program's operator's inventory should "
                  + "be added to the synchronization queue",
            lInventorySet.contains( INVENTORY_OP1 ) );
      assertTrue( "All inventory should be added to the synchronization queue",
            lInventorySet.contains( INVENTORY_OP2 ) );
   }


   /**
    * When a task is added to a maintenance program and the task is already on another maintenance
    * program, the inventory that is part of the maintenance program should be created.
    */
   @Test
   public void testNew_OtherCarrier() {
      Set<InventoryKey> lInventorySet = executeUpdate( MAINT_PRGM_NEW_OTHER_CARRIER );

      assertEquals( "Only the maintenance program's operator's inventory should "
            + "be added to the synchronization queue", 1, lInventorySet.size() );
      assertTrue(
            "The maintenance program's operator's inventory should "
                  + "be added to the synchronization queue",
            lInventorySet.contains( INVENTORY_OP1 ) );
   }


   /**
    * When a task is removed on a maintenance program and only the current carrier has the task, all
    * inventory should be have the task created / updated to lastest version.
    */
   @Test
   public void testRemove_OnlyCarrier() {
      Set<InventoryKey> lInventorySet = executeUpdate( MAINT_PRGM_REMOVE_ONLY_CARRIER );

      assertEquals( "All inventory should be added to the synchronization queue.", 2,
            lInventorySet.size() );
      assertTrue(
            "The maintenance program's operator's inventory should "
                  + "be added to the synchronization queue",
            lInventorySet.contains( INVENTORY_OP1 ) );
      assertTrue( "All inventory should be added to the synchronization queue",
            lInventorySet.contains( INVENTORY_OP2 ) );
   }


   /**
    * When a task is removed on a maintenance program and the task is already on another maintenance
    * program, the inventory that is part of the current carrier maintenance program should be
    * removed.
    */
   @Test
   public void testRemove_OtherCarrier() {
      Set<InventoryKey> lInventorySet = executeUpdate( MAINT_PRGM_REMOVE_OTHER_CARRIER );

      assertEquals( "Only the maintenance program's operator's inventory should "
            + "be added to the synchronization queue", 1, lInventorySet.size() );
      assertTrue(
            "The maintenance program's operator's inventory should "
                  + "be added to the synchronization queue",
            lInventorySet.contains( INVENTORY_OP1 ) );
   }


   /**
    * When a task is updated on a maintenance program and only the current carrier has the task, the
    * inventory that is part of the current carrier maintenance program should be updated.
    */
   @Test
   public void testUpdate_OnlyCarrier() {
      Set<InventoryKey> lInventorySet = executeUpdate( MAINT_PRGM_UPDATE_ONLY_CARRIER );

      assertEquals( "Only the maintenance program's operator's inventory should "
            + "be added to the synchronization queue", 1, lInventorySet.size() );
      assertTrue(
            "The maintenance program's operator's inventory should "
                  + "be added to the synchronization queue",
            lInventorySet.contains( INVENTORY_OP1 ) );
   }


   /**
    * When a task is updated on a maintenance program and the task is already on another maintenance
    * program, the inventory that is part of the current carrier maintenance program should be
    * updated.
    */
   @Test
   public void testUpdate_OtherCarrier() {
      Set<InventoryKey> lInventorySet = executeUpdate( MAINT_PRGM_UPDATE_OTHER_CARRIER );

      assertEquals( "Only the maintenance program's operator's inventory should "
            + "be added to the synchronization queue", 1, lInventorySet.size() );
      assertTrue(
            "The maintenance program's operator's inventory should "
                  + "be added to the synchronization queue",
            lInventorySet.contains( INVENTORY_OP1 ) );
   }


   /**
    * Returns the set of inventory keys updated
    *
    * @param aMaintPrgmKey
    *           the maintenance program to update
    *
    * @return set of inventory keys added to the syncronization list
    */
   @SuppressWarnings( "deprecation" )
   protected Set<InventoryKey> executeUpdate( MaintPrgmKey aMaintPrgmKey ) {

      // Execute synchronization queue for maintenance program
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aMaintPrgmKey, "aMaintPrgmDbId", "aMaintPrgmId" );

      QueryExecutor.executeUpdate( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // Collect inventory that were added to syncronization queue
      Set<InventoryKey> lInventoryKeys = new HashSet<InventoryKey>();
      QuerySet lQuerySet =
            MxDataAccess.getInstance().executeQuery( INV_COLUMNS, INV_SYNC_TABLE_NAME, null );
      while ( lQuerySet.next() ) {
         lInventoryKeys.add( lQuerySet.getKey( InventoryKey.class, INV_COLUMNS ) );
      }

      return lInventoryKeys;
   }
}
