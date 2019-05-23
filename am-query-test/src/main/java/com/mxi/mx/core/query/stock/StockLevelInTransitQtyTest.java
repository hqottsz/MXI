
package com.mxi.mx.core.query.stock;

import static com.mxi.mx.testing.matchers.MxMatchers.assertThat;
import static org.hamcrest.Matchers.is;

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
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.ShipmentKey;
import com.mxi.mx.core.key.StockNoKey;
import com.mxi.mx.core.table.evt.EvtEventTable;


/**
 * Ensures that the StockLevelInTransitQty query returns the right results
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class StockLevelInTransitQtyTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @Before
   public void setUp() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
   }


   private static final Integer INTRANSIT_QUANTITY = 5;
   private static final Integer ONORDER_QUANTITY = 3;

   private static final StockNoKey INTRANSIT_STOCK = new StockNoKey( 4650, 1 );
   private static final StockNoKey MULTISEGMENT_STOCK = new StockNoKey( 4650, 4 );
   private static final StockNoKey MULTISEGMENT_STOCK_SHIPPED_THROUGH_LOCATION =
         new StockNoKey( 4650, 5 );
   private static final StockNoKey MULTIPLE_SHIPMENTS = new StockNoKey( 4650, 6 );
   private static final StockNoKey MULTIPLE_SHIPMENT_LINES = new StockNoKey( 4650, 7 );
   private static final StockNoKey MULTIPLE_PARTS = new StockNoKey( 4650, 8 );
   private static final StockNoKey SHIPMENT_WITH_NON_RFI_INV = new StockNoKey( 4650, 9 );
   private static final LocationKey STOCK_LOCATION = new LocationKey( 4650, 1 );
   private static final OwnerKey STOCK_OWNER = new OwnerKey( 4650, 1 );
   private static final ShipmentKey HISTORIC_SHIPMENT = new ShipmentKey( 4650, 62 );
   private static final Integer HISTORIC_SHIPMENT_QUANTITY = 2;
   private static final Integer NON_RFI_INVENTORY_QUANTITY = 2;
   private static final Integer ZERO_WHEN_MORE_ON_ORDER_THAN_IN_TRANSIT = 0;
   private static final Integer ZERO_WHEN_SHIPPED_THROUGH_LOCATION = 0;


   /**
    * Ensures in transit quantity is calculated properly when multiple part numbers shipped
    */
   @Test
   public void testInTransit_With_Differing_Parts() {
      Integer lInTransitQuantity = getInTransitQuantity( MULTIPLE_PARTS );
      assertThat( lInTransitQuantity, is( INTRANSIT_QUANTITY ) );
   }


   /**
    * Ensures in transit quantity is calculated properly when multiple shipments and one is historic
    */
   @Test
   public void testInTransit_With_Historic_Shipment() {
      setShipmentToHistoric( HISTORIC_SHIPMENT );

      Integer lInTransitQuantity = getInTransitQuantity( MULTIPLE_SHIPMENTS );
      assertThat( lInTransitQuantity, is( INTRANSIT_QUANTITY - HISTORIC_SHIPMENT_QUANTITY ) );
   }


   /**
    * Ensures in transit quantity is calculated properly when multiple shipment lines
    */
   @Test
   public void testInTransit_With_Multiple_Shipment_Lines() {
      Integer lInTransitQuantity = getInTransitQuantity( MULTIPLE_SHIPMENT_LINES );
      assertThat( lInTransitQuantity, is( INTRANSIT_QUANTITY ) );
   }


   /**
    * Ensures in transit quantity is calculated properly when multiple shipments
    */
   @Test
   public void testInTransit_With_Multiple_Shipments() {
      Integer lInTransitQuantity = getInTransitQuantity( MULTIPLE_SHIPMENTS );
      assertThat( lInTransitQuantity, is( INTRANSIT_QUANTITY ) );
   }


   /**
    * Ensures in transit quantity is calculated properly
    */
   @Test
   public void testInTransit_With_NoOnOrder() {
      Integer lInTransitQuantity = getInTransitQuantity( INTRANSIT_STOCK );
      assertThat( lInTransitQuantity, is( INTRANSIT_QUANTITY ) );
   }


   /**
    * Ensures that parts are considered in transit if shipment's final segment is shipped to the
    * stock level location
    */
   @Test
   public void testMultiSegment() {
      Integer lInTransitQuantity = getInTransitQuantity( MULTISEGMENT_STOCK );
      assertThat( lInTransitQuantity, is( INTRANSIT_QUANTITY ) );
   }


   /**
    * Ensures that parts are not considered in transit if shipment passes through stock level
    * location
    */
   @Test
   public void testMultiSegment_Routed_Through_StockLevelLocation() {
      Integer lInTransitQuantity =
            getInTransitQuantity( MULTISEGMENT_STOCK_SHIPPED_THROUGH_LOCATION );
      assertThat( lInTransitQuantity, is( ZERO_WHEN_SHIPPED_THROUGH_LOCATION ) );
   }


   /**
    * Ensures in transit quantity does not count shipments with picked, non-RFI inventory
    */
   @Test
   public void testShipment_With_Picked_NonRfi_Inventory() {
      Integer lInTransitQuantity = getInTransitQuantity( SHIPMENT_WITH_NON_RFI_INV );
      assertThat( lInTransitQuantity, is( INTRANSIT_QUANTITY - NON_RFI_INVENTORY_QUANTITY ) );
   }


   /**
    * Obtains the number of parts that are in transit
    *
    * @param aStock
    *           the stock number
    *
    * @return the number of in transit parts
    */
   private int getInTransitQuantity( StockNoKey aStock ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aStock, "aStockNoDbId", "aStockNoId" );
      lArgs.add( STOCK_LOCATION, "aLocDbId", "aLocId" );
      lArgs.add( STOCK_OWNER, "aOwnerDbId", "aOwnerId" );

      QuerySet lQs = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
      lQs.first();

      return lQs.getInt( "in_transit_qty" );
   }


   /**
    * Set the provided shipment to be historic.
    *
    * @param aShipment
    */
   private void setShipmentToHistoric( ShipmentKey aShipment ) {
      EvtEventTable lEventTable = EvtEventTable.findByPrimaryKey( aShipment );
      lEventTable.setHistBool( true );
      lEventTable.update();
   }
}
