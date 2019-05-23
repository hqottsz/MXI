
package com.mxi.mx.core.services.order;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.OrderBuilder;
import com.mxi.am.domain.builder.ShipSegmentBuilder;
import com.mxi.am.domain.builder.ShipSegmentMapBuilder;
import com.mxi.am.domain.builder.ShipmentDomainBuilder;
import com.mxi.am.domain.builder.VendorBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPoTypeKey;
import com.mxi.mx.core.key.RefShipSegmentStatusKey;
import com.mxi.mx.core.key.RefShipmentTypeKey;
import com.mxi.mx.core.key.ShipSegmentKey;
import com.mxi.mx.core.key.ShipmentKey;
import com.mxi.mx.core.key.VendorKey;
import com.mxi.mx.core.services.shipment.routing.ShipmentHasCompletedSegmentsException;
import com.mxi.mx.core.unittest.table.evt.EvtEventUtil;


/**
 * This class test the methods in OrderService class.
 *
 * @author Libin Cai
 * @created February 12, 2016
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class CancelAndCloseOrderTest {

   private LocationKey iDockLocation;

   private HumanResourceKey iHr;

   private static final String USERNAME_TESTUSER = "testuser";

   private static final int USERID_TESTUSER = 999;

   private OrderBuilder iIssuedOrderBuilder;

   private LocationKey iSupplyLocation;

   private VendorKey iVendor;

   private LocationKey iVendorLocation;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Test that the purchase order can be cancelled even there is completed inbound segment.
    *
    * @throws Exception
    *            if any error occurs
    */
   @Test
   public void testCancelPurchaseOrder() throws Exception {

      cancelOrderAndAssert( createIssuedPurchaseOrder() );
   }


   /**
    * Test that the purchase order can be closed even there is completed inbound segment.
    *
    * @throws Exception
    *            if any error occurs
    */
   @Test
   public void testClosePurchaseOrder() throws Exception {

      closeOrderAndAssert( createIssuedPurchaseOrder() );
   }


   /**
    * Create issued purchase order.
    *
    * @return the issued purchase order
    */
   private PurchaseOrderKey createIssuedPurchaseOrder() {

      PurchaseOrderKey lIssuedPO =
            iIssuedOrderBuilder.withOrderType( RefPoTypeKey.PURCHASE ).build();

      createPendingInboundShipmentWithCompletedSegment( lIssuedPO );

      return lIssuedPO;
   }


   /**
    * Cancel the order and assert its status.
    *
    * @param aIssuedOrder
    *           the issued order
    * @throws Exception
    *            if an error occurs
    */
   private void cancelOrderAndAssert( PurchaseOrderKey aIssuedOrder ) throws Exception {

      OrderService.cancel( aIssuedOrder, iHr, null, null, null );

      new EvtEventUtil( aIssuedOrder.getEventKey() )
            .assertEventStatus( RefEventStatusKey.POCANCEL );
   }


   /**
    * Close the order and assert its status.
    *
    * @param aIssuedOrder
    *           the issued order
    * @throws Exception
    *            if an error occurs
    */
   private void closeOrderAndAssert( PurchaseOrderKey aIssuedOrder ) throws Exception {

      OrderService.close( aIssuedOrder, iHr, null, null );

      new EvtEventUtil( aIssuedOrder.getEventKey() )
            .assertEventStatus( RefEventStatusKey.POCLOSED );
   }


   /**
    * Test that the exchange order can be cancelled even there is completed inbound segment. Note:
    * There should be no completed outbound segment.
    *
    * @throws Exception
    *            if any error occurs
    */
   @Test
   public void testCancelExchangeOrder() throws Exception {

      cancelOrderAndAssert( createIssuedExchangeOrder( RefShipSegmentStatusKey.PENDING ) );
   }


   /**
    * Test that the exchange order can be closed even there is completed inbound segment. Note:
    * There should be no completed outbound segment.
    *
    * @throws Exception
    *            if any error occurs
    */
   @Test
   public void testCloseExchangeOrder() throws Exception {

      closeOrderAndAssert( createIssuedExchangeOrder( RefShipSegmentStatusKey.PENDING ) );
   }


   /**
    * Test that the exchange order CANNOT be cancelled and ShipmentHasCompletedSegmentsException is
    * thrown if there is a completed outbound segment.
    *
    * @throws Exception
    *            if any error occurs
    */
   @Test
   public void testCloseExchangeOrderWithShipmentHasCompletedSegmentsException() throws Exception {

      try {

         closeOrderAndAssert( createIssuedExchangeOrder( RefShipSegmentStatusKey.COMPLETE ) );

         fail( "Expect ShipmentHasCompletedSegmentsException." );

      } catch ( Exception e ) {
         assertEquals( ShipmentHasCompletedSegmentsException.class, e.getClass() );
      }
   }


   /**
    * Test that the exchange order CANNOT be closed and ShipmentHasCompletedSegmentsException is
    * thrown if there is a completed outbound segment.
    *
    * @throws Exception
    *            if any error occurs
    */
   @Test
   public void testCancelExchangeOrderWithShipmentHasCompletedSegmentsException() throws Exception {

      try {

         cancelOrderAndAssert( createIssuedExchangeOrder( RefShipSegmentStatusKey.COMPLETE ) );

         fail( "Expect ShipmentHasCompletedSegmentsException." );

      } catch ( Exception e ) {
         assertEquals( ShipmentHasCompletedSegmentsException.class, e.getClass() );
      }
   }


   /**
    * Create issued exchange order.
    *
    * @return the issued exchange order
    */
   private PurchaseOrderKey createIssuedExchangeOrder( RefShipSegmentStatusKey aSegmentStatus ) {

      PurchaseOrderKey lIssuedEO =
            iIssuedOrderBuilder.withOrderType( RefPoTypeKey.EXCHANGE ).build();

      createPendingInboundShipmentWithCompletedSegment( lIssuedEO );

      createPendingOutboundShipment( lIssuedEO, aSegmentStatus );

      return lIssuedEO;
   }


   /**
    * create pending outbound shipment with the segments.
    *
    * @param aSegmentStatus
    *           the first segment status
    */
   private void createPendingOutboundShipment( PurchaseOrderKey lOrderKey,
         RefShipSegmentStatusKey aSegmentStatus ) {

      ShipmentKey lPendingOutboundShipment =
            new ShipmentDomainBuilder().withOrder( lOrderKey ).fromLocation( iDockLocation )
                  .toLocation( iVendorLocation ).withType( RefShipmentTypeKey.SENDXCHG )
                  .withStatus( RefEventStatusKey.IXPEND ).withHistoric( false ).build();

      // create two segments
      ShipSegmentKey lOutboundSegment1 = new ShipSegmentBuilder().fromLocation( iDockLocation )
            .toLocation( iSupplyLocation ).withStatus( aSegmentStatus ).build();
      ShipSegmentKey lOutboundSegment2 = new ShipSegmentBuilder().fromLocation( iSupplyLocation )
            .toLocation( iVendorLocation ).withStatus( RefShipSegmentStatusKey.PLAN ).build();

      // create a map to link segments to shipment
      new ShipSegmentMapBuilder().withShipment( lPendingOutboundShipment )
            .withSegment( lOutboundSegment1 ).withOrder( 1 ).build();
      new ShipSegmentMapBuilder().withShipment( lPendingOutboundShipment )
            .withSegment( lOutboundSegment2 ).withOrder( 2 ).build();
   }


   /**
    * Create pending inbound shipment with a compelted segment.
    *
    */
   private void createPendingInboundShipmentWithCompletedSegment( PurchaseOrderKey lOrderKey ) {

      ShipmentKey lPendingInboundShipment =
            new ShipmentDomainBuilder().withOrder( lOrderKey ).fromLocation( iVendorLocation )
                  .toLocation( iDockLocation ).withType( RefShipmentTypeKey.PURCHASE )
                  .withStatus( RefEventStatusKey.IXPEND ).withHistoric( false ).build();

      // create two segments
      ShipSegmentKey lInboundSegment1 = new ShipSegmentBuilder().fromLocation( iVendorLocation )
            .toLocation( iSupplyLocation ).withStatus( RefShipSegmentStatusKey.COMPLETE ).build();
      ShipSegmentKey lInboundSegment2 = new ShipSegmentBuilder().fromLocation( iSupplyLocation )
            .toLocation( iDockLocation ).withStatus( RefShipSegmentStatusKey.PENDING ).build();

      // create a map to link segments to shipment
      new ShipSegmentMapBuilder().withShipment( lPendingInboundShipment )
            .withSegment( lInboundSegment1 ).withOrder( 1 ).build();
      new ShipSegmentMapBuilder().withShipment( lPendingInboundShipment )
            .withSegment( lInboundSegment2 ).withOrder( 2 ).build();
   }


   /**
    * Set up the test case.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Before
   public void loadData() throws Exception {
      iHr = new HumanResourceDomainBuilder().withUsername( USERNAME_TESTUSER )
            .withUserId( USERID_TESTUSER ).build();

      // create a vendor location
      iVendorLocation =
            new LocationDomainBuilder().withType( RefLocTypeKey.VENDOR ).withCode( "TESTVENLOC" ).build();

      // create a supply location
      iSupplyLocation =
            new LocationDomainBuilder().withType( RefLocTypeKey.AIRPORT ).isSupplyLocation().build();

      // create a dock at the supply location
      iDockLocation = new LocationDomainBuilder().withType( RefLocTypeKey.DOCK )
            .withSupplyLocation( iSupplyLocation ).build();

      iVendor = new VendorBuilder().withCode( "TESTVENDOR" ).atLocation( iVendorLocation ).build();

      iIssuedOrderBuilder = new OrderBuilder().withStatus( RefEventStatusKey.POISSUED )
            .withVendor( iVendor ).shippingTo( iDockLocation );
   }
}
