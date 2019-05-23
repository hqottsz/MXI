
package com.mxi.mx.core.query.order;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;


/**
 * This class tests the com.mxi.mx.core.query.order.GetReceiveShipmentList.qrx query.
 *
 * @author slevert
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetReceiveShipmentListTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetReceiveShipmentListTest.class );
   }


   /** The query execution data set */
   private final PurchaseOrderKey iPO1 = new PurchaseOrderKey( "0:1" );
   private final PurchaseOrderKey iPO2 = new PurchaseOrderKey( "0:2" );
   private final PurchaseOrderKey iPO3 = new PurchaseOrderKey( "0:3" );
   private final PurchaseOrderKey iPO4 = new PurchaseOrderKey( "0:4" );
   private final PurchaseOrderKey iPO5 = new PurchaseOrderKey( "0:5" );
   private final PurchaseOrderKey iPO6 = new PurchaseOrderKey( "0:6" );
   private final PurchaseOrderKey iPO7 = new PurchaseOrderKey( "0:7" );


   /**
    * Test that the query returns no po_lines since there is an inbound shipment against the PO that
    * has the same receive quantity as the order lines order quantity. There are on outbound
    * shipments.
    */
   @Test
   public void testInboundShipmentsNoOutboundShipments() {
      DataSet lDataSet = execute( iPO2 );

      assertFalse( lDataSet.hasNext() );
   }


   /**
    * Test that the query returns no po_lines since there is an inbound shipment against the PO that
    * has the same receive quantity as the order lines order quantity. There are outbound shipments.
    */
   @Test
   public void testInboundShipmentsOutboundShipments() {
      DataSet lDataSet = execute( iPO1 );

      assertFalse( lDataSet.hasNext() );
   }


   /**
    * Test that the query returns no po_lines since there is one inbound shipment with ARCHIVE
    * inventory, and another inbound shipment against the PO has the same receive quantity as the
    * order lines order quantity.
    */
   @Test
   public void testTwoInboundShipmentsForOnePoLine() {
      DataSet lDataSet = execute( iPO7 );

      assertFalse( lDataSet.hasNext() );
   }


   /**
    * Test that the query returns a po_line when an existing inbound shipment that does not have the
    * same receive quantity as the order lines order quantity. There are on outbound shipments.
    */
   @Test
   public void testIncompleteShipmentsReturnsAPOLine() {
      DataSet lDataSet = execute( iPO5 );

      assertTrue( lDataSet.hasNext() );

      lDataSet.next();

      PurchaseOrderLineKey lKey =
            lDataSet.getKey( PurchaseOrderLineKey.class, "purchase_order_line" );

      assertEquals( "0:5:1", lKey.toString() );
   }


   /**
    * Test that the query returns a po_line since there are no inbound or outbound shipments agaist
    * the order line.
    */
   @Test
   public void testNoInboundShipmentsNoOutboundShipments() {
      DataSet lDataSet = execute( iPO4 );

      assertTrue( lDataSet.hasNext() );

      lDataSet.next();

      PurchaseOrderLineKey lKey =
            lDataSet.getKey( PurchaseOrderLineKey.class, "purchase_order_line" );

      assertEquals( "0:4:1", lKey.toString() );
   }


   /**
    * Test that the query returns a po_line since there are no inbound shipment agaist the order
    * line even though there are outbound shipments against the order line.
    */
   @Test
   public void testNoInboundShipmentsOutboundShipments() {
      DataSet lDataSet = execute( iPO3 );

      assertTrue( lDataSet.hasNext() );

      assertEquals( 1, lDataSet.getRowCount() );

      lDataSet.next();

      PurchaseOrderLineKey lKey =
            lDataSet.getKey( PurchaseOrderLineKey.class, "purchase_order_line" );

      assertEquals( "0:3:1", lKey.toString() );
   }


   /**
    * Test that if an order has multiple order lines that have no inbound shipments, each order line
    * key is returned.
    */
   @Test
   public void testOrderHasOrderLinesWithoutInboundShipments() {
      DataSet lDataSet = execute( iPO6 );

      assertTrue( lDataSet.hasNext() );

      assertEquals( 4, lDataSet.getRowCount() );

      lDataSet.next();

      PurchaseOrderLineKey lKey =
            lDataSet.getKey( PurchaseOrderLineKey.class, "purchase_order_line" );

      // Order Line #1
      assertEquals( "0:6:1", lKey.toString() );

      lDataSet.next();

      lKey = lDataSet.getKey( PurchaseOrderLineKey.class, "purchase_order_line" );

      // Order Line #2
      assertEquals( "0:6:2", lKey.toString() );

      lDataSet.next();

      lKey = lDataSet.getKey( PurchaseOrderLineKey.class, "purchase_order_line" );

      // Order Line #3
      assertEquals( "0:6:3", lKey.toString() );

      lDataSet.next();

      lKey = lDataSet.getKey( PurchaseOrderLineKey.class, "purchase_order_line" );

      // Order Line #4
      assertEquals( "0:6:4", lKey.toString() );
   }


   /**
    * Executes the query.
    *
    * @param aKey
    *           organization key.
    *
    * @return {@link DataSet}
    */
   private DataSet execute( PurchaseOrderKey aKey ) {

      // Build the query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aKey, "aPODbId", "aPOId" );

      // Execute the query
      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
