
package com.mxi.mx.core.query.inventory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;


/**
 * Test class to verify the behaviours of the EnqueueInventory.qrx query.
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class EnqueueInventoryTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   /**
    *
    * Verify that a loose inventory is added to the inv_pos_desc_queue when it is valid to be
    * queued.
    *
    */
   @Test
   public void testWhenLooseInv() {

      // With test inventory that is loose and may be queued.
      InventoryKey lTestInv = new InventoryBuilder().withCondition( RefInvCondKey.RFI )
            .withClass( RefInvClassKey.TRK ).withParentInventory( null ).build();

      execute( lTestInv );

      // Verify the inventory is queued.
      assertInvPosDescQueueOnlyContains( Arrays.asList( lTestInv ) );
   }


   /**
    *
    * Verify that a loose inventory and all of its sub-inventory is added to the inv_pos_desc_queue
    * when all of those inventory are valid to be queued.
    *
    */
   @Test
   public void testWhenLooseInvWithSubTree() {

      // With test inventory that is loose and may be queued.
      InventoryKey lTestInv = new InventoryBuilder().withCondition( RefInvCondKey.RFI )
            .withClass( RefInvClassKey.TRK ).withParentInventory( null ).build();

      // With child inventory that may be queued.
      InventoryKey lChildInv = new InventoryBuilder().withParentInventory( lTestInv )
            .withCondition( RefInvCondKey.RFI ).withClass( RefInvClassKey.TRK ).build();

      // With grand child inventory that may be queued.
      InventoryKey lGrandchildInv = new InventoryBuilder().withParentInventory( lChildInv )
            .withCondition( RefInvCondKey.RFI ).withClass( RefInvClassKey.TRK ).build();

      execute( lTestInv );

      // Verify the inventory and all of its sub-inventory are all queued.
      assertInvPosDescQueueOnlyContains( Arrays.asList( lTestInv, lChildInv, lGrandchildInv ) );
   }


   /**
    *
    * Verify that an installed inventory is added to the inv_pos_desc_queue when it is valid to be
    * queued.
    *
    */
   @Test
   public void testWhenInstalledInv() {

      InventoryKey lParentInv = new InventoryBuilder().withCondition( RefInvCondKey.RFI )
            .withClass( RefInvClassKey.TRK ).withParentInventory( null ).build();

      // With test inventory that is installed and may be queued.
      InventoryKey lTestInv = new InventoryBuilder().withCondition( RefInvCondKey.RFI )
            .withClass( RefInvClassKey.TRK ).withParentInventory( lParentInv ).build();

      execute( lTestInv );

      // Verify that the inventory is queued (and the parent is not).
      assertInvPosDescQueueOnlyContains( Arrays.asList( lTestInv ) );
   }


   /**
    *
    * Verify that an installed inventory and all of its sub-inventory are added to the
    * inv_pos_desc_queue, when all of those inventory are valid to be queued.
    *
    */
   @Test
   public void testWhenInstalledInvWithSubTree() {

      InventoryKey lParentInv = new InventoryBuilder().withCondition( RefInvCondKey.RFI )
            .withClass( RefInvClassKey.TRK ).withParentInventory( null ).build();

      // With test inventory that is installed and may be queued.
      InventoryKey lTestInv = new InventoryBuilder().withCondition( RefInvCondKey.RFI )
            .withClass( RefInvClassKey.TRK ).withParentInventory( lParentInv ).build();

      // With child inventory that may be queued.
      InventoryKey lChildInv = new InventoryBuilder().withParentInventory( lTestInv )
            .withCondition( RefInvCondKey.RFI ).withClass( RefInvClassKey.TRK ).build();

      // With grand child inventory that may be queued.
      InventoryKey lGrandchildInv = new InventoryBuilder().withParentInventory( lChildInv )
            .withCondition( RefInvCondKey.RFI ).withClass( RefInvClassKey.TRK ).build();

      execute( lTestInv );

      // Verify the inventory and all of its sub-inventory are all queued.
      assertInvPosDescQueueOnlyContains( Arrays.asList( lTestInv, lChildInv, lGrandchildInv ) );
   }


   /**
    *
    * Verify that an inventory is not added to the inv_pos_desc_queue when it has a condition of
    * scrap.
    *
    */
   @Test
   public void testWhenScrapInv() {

      // With test inventory that has a condition of scrap.
      InventoryKey lTestInv = new InventoryBuilder().withCondition( RefInvCondKey.SCRAP )
            .withClass( RefInvClassKey.TRK ).withParentInventory( null ).build();

      execute( lTestInv );

      // Verify the scrap inventory is not queued.
      assertInvPosDescQueueOnlyContains( null );
   }


   /**
    *
    * Verify that an inventory is not added to the inv_pos_desc_queue when it has a condition of
    * scrap but its sub-inventory is queued when they are valid.
    *
    */
   @Test
   public void testWhenScrapInvWithSubTree() {

      // With test inventory that has a condition of scrap.
      InventoryKey lTestInv = new InventoryBuilder().withCondition( RefInvCondKey.SCRAP )
            .withClass( RefInvClassKey.TRK ).withParentInventory( null ).build();

      // With child inventory that may be queued.
      InventoryKey lChildInv = new InventoryBuilder().withParentInventory( lTestInv )
            .withCondition( RefInvCondKey.RFI ).withClass( RefInvClassKey.TRK ).build();

      // With grand child inventory that may be queued.
      InventoryKey lGrandchildInv = new InventoryBuilder().withParentInventory( lChildInv )
            .withCondition( RefInvCondKey.RFI ).withClass( RefInvClassKey.TRK ).build();

      execute( lTestInv );

      // Verify the scrap inventory is not queued but its valid sub-inventory are.
      assertInvPosDescQueueOnlyContains( Arrays.asList( lChildInv, lGrandchildInv ) );
   }


   /**
    *
    * Verify that an inventory is not added to the inv_pos_desc_queue when it has a inventory class
    * is BATCH.
    *
    */
   @Test
   public void testWhenBatchInv() {

      // With test inventory that has an inventory class of BATCH.
      InventoryKey lTestInv = new InventoryBuilder().withCondition( RefInvCondKey.RFI )
            .withClass( RefInvClassKey.BATCH ).withParentInventory( null ).build();

      execute( lTestInv );

      // Verify the batch inventory is not queued.
      assertInvPosDescQueueOnlyContains( null );
   }


   /**
    *
    * Verify that an inventory is not added to the inv_pos_desc_queue when it has a inventory class
    * is SER.
    *
    */
   @Test
   public void testWhenSerInv() {

      // With test inventory that has an inventory class of SER.
      InventoryKey lTestInv = new InventoryBuilder().withCondition( RefInvCondKey.RFI )
            .withClass( RefInvClassKey.SER ).withParentInventory( null ).build();

      execute( lTestInv );

      // Verify the serial inventory is not queued.
      assertInvPosDescQueueOnlyContains( null );
   }


   /**
    *
    * Verify that an inventory is not added to the inv_pos_desc_queue when it has a inventory class
    * is ACFT.
    *
    */
   @Test
   public void testWhenAcftInv() {

      // With test inventory that has an inventory class of ACFT.
      InventoryKey lTestInv = new InventoryBuilder().withCondition( RefInvCondKey.RFI )
            .withClass( RefInvClassKey.ACFT ).withParentInventory( null ).build();

      execute( lTestInv );

      // Verify the aircraft inventory is not queued.
      assertInvPosDescQueueOnlyContains( null );
   }


   /**
    *
    * Verify that when a sub-inventory is not valid to be added to the queue that it is not added
    * but all the others are.
    *
    */
   @Test
   public void testWhenSubInvIsNotValid() {

      // With test inventory that is loose and may be queued.
      InventoryKey lTestInv = new InventoryBuilder().withCondition( RefInvCondKey.RFI )
            .withClass( RefInvClassKey.TRK ).withParentInventory( null ).build();

      // With child inventory that may NOT be queued, due to it being scrap.
      InventoryKey lChildInv = new InventoryBuilder().withParentInventory( lTestInv )
            .withCondition( RefInvCondKey.SCRAP ).withClass( RefInvClassKey.TRK ).build();

      // With grand child inventory that may be queued.
      InventoryKey lGrandchildInv = new InventoryBuilder().withParentInventory( lChildInv )
            .withCondition( RefInvCondKey.RFI ).withClass( RefInvClassKey.TRK ).build();

      execute( lTestInv );

      // Verify the inventory and all of its valid sub-inventory are all queued, but not the scrap
      // sub-inventory.
      assertInvPosDescQueueOnlyContains( Arrays.asList( lTestInv, lGrandchildInv ) );
   }


   /**
    * Execute the query using the provided inventory key.
    *
    * @param aTestInv
    *           test inventory key
    */
   private void execute( InventoryKey aTestInv ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aTestInv, "aInvNoDbId", "aInvNoId" );

      QueryExecutor.executeUpdate( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Verify that the provided inventory keys (and only those keys) are found in the
    * inv_pos_desc_queue table.
    *
    * @param aExpectedInv
    *           - expected inventory keys
    */
   private void assertInvPosDescQueueOnlyContains( List<InventoryKey> aExpectedInvKeys ) {

      DataSet lDs = MxDataAccess.getInstance().executeQueryTable( "inv_pos_desc_queue", null );

      if ( aExpectedInvKeys == null ) {
         assertEquals( "Expected inv_pos_desc_queue to be empty.", 0, lDs.getRowCount() );
      } else {
         assertEquals( "Unexpected number of rows in inv_pos_desc_queue.", aExpectedInvKeys.size(),
               lDs.getRowCount() );

         while ( lDs.next() ) {
            InventoryKey lInvKey = lDs.getKey( InventoryKey.class, "inv_no_db_id", "inv_no_id" );
            assertTrue( "inv_pos_desc_queue contains an unexpected inv key:" + lInvKey.toString(),
                  aExpectedInvKeys.contains( lInvKey ) );
         }

      }
   }
}
