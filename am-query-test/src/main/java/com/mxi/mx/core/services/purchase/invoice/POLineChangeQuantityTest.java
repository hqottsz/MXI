package com.mxi.mx.core.services.purchase.invoice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.AccountBuilder;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.OrderBuilder;
import com.mxi.am.domain.builder.OrderLineBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.ShipSegmentBuilder;
import com.mxi.am.domain.builder.ShipSegmentMapBuilder;
import com.mxi.am.domain.builder.ShipmentDomainBuilder;
import com.mxi.am.domain.builder.ShipmentLineBuilder;
import com.mxi.am.domain.builder.VendorBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MandatoryArgumentException;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.exception.NegativeValueException;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.unittest.security.SecurityIdentificationStub;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPoLineTypeKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefShipSegmentStatusKey;
import com.mxi.mx.core.key.RefShipmentTypeKey;
import com.mxi.mx.core.key.ShipSegmentKey;
import com.mxi.mx.core.key.ShipmentKey;
import com.mxi.mx.core.key.ShipmentLineKey;
import com.mxi.mx.core.key.TaxKey;
import com.mxi.mx.core.key.VendorKey;
import com.mxi.mx.core.services.order.EditOrderLineTO;
import com.mxi.mx.core.services.order.OrderService;
import com.mxi.mx.core.table.po.POLineTable;
import com.mxi.mx.core.table.po.PoLineTax;
import com.mxi.mx.core.table.procurement.TaxTable;
import com.mxi.mx.core.table.ship.ShipShipmentLineTable;


/**
 * This UT verifies that the inbound shipment and the change reason code for an issued purchase
 * order line are updated when the order line quantity is decreased or increased respectively. Also
 * if order quantity does not change, shipment quantity does not change.
 *
 * @author gpichetto
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class POLineChangeQuantityTest {

   /** original expected quantity for order line A and B */
   private static final double ORG_QT_LINE_A = 10.0;
   private static final double ORG_QT_LINE_B = 3.0;
   private static final double ORG_QT_LINE_A_SER = 2.0;
   private static final double ORG_QT_LINE_B_SER = 2.0;

   /** historic complete received quantity for order line A and B */
   private static final double HIS_PARTIALLY_RECEIVED_QT_LINE_A = 1.0;
   private static final double HIS_OVER_RECEIVED_QT_LINE_B = 4.0;
   private static final double ONE_FOR_SER = 1.0;

   private static final String ACCOUNT_CD = "TESTACCOUNT";
   private static final String ALT_ACCOUNT_CD = "TESTALTACCOUNT";
   private static final String USERNAME_TESTUSER = "testuser";
   private static final int USERID_TESTUSER = 999;

   private FncAccountKey iAccount;
   private HumanResourceKey iHr;
   private TaxKey iTaxKey;
   private VendorKey iVendor;
   private LocationKey iVendorLocation;
   private LocationKey iDockLocation;
   private LocationKey iReExpediteLocation;

   private PurchaseOrderLineKey iOrderLineA;
   private PurchaseOrderLineKey iOrderLineB;
   private PartNoKey iBatchPartA;
   private PartNoKey iBatchPartB;
   private PartNoKey iSerPart;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Pre-condition: The order status is ISSUED. This test verifies that the change reason code is
    * MXDCRSE and the shipment line expected quantity is decreased after an order line quantity has
    * decreased
    *
    * @throws MxException
    *            if any exception occurs
    * @throws Exception
    *            if any exception occurs
    */
   @Test
   public void testSingleOrderLineQuantityDecreased() throws MxException, Exception { // build
                                                                                      // purchase
      // order with
      // status ISSUED
      PurchaseOrderKey lOrder = createOrderBuilder( RefEventStatusKey.POISSUED ).build();

      PurchaseOrderLineKey lOrderLine = createOrderLineBuilder( lOrder, iBatchPartA,
            RefPoLineTypeKey.PURCHASE, new BigDecimal( ORG_QT_LINE_A ) ).build();
      ShipmentKey lShipment = createInboundShipment( lOrder, RefEventStatusKey.IXPEND ).build();
      ShipmentLineKey lShipmentLine =
            createShipmentLine( lShipment, iBatchPartA, lOrderLine, ORG_QT_LINE_A ).build();

      // decrease original quantity
      EditOrderLineTO lEditOrderLineTo =
            editOrderLineTOSetup( lOrderLine, ACCOUNT_CD, ORG_QT_LINE_A - 7 );

      // call service to update order line
      OrderService.setPOLines( lOrder, new EditOrderLineTO[] { lEditOrderLineTo }, iHr );

      // assert that change reason code is MXDCRSE, since quantity decreased in the order line
      assertEquals( POLineTable.findByPrimaryKey( lOrderLine ).getChangeReasonCd(), "MXDCRSE" );

      // assert that shipment line expected quantity has decreased as well
      assertEquals( ShipShipmentLineTable.findByPrimaryKey( lShipmentLine ).getExpectQt(),
            new Double( ORG_QT_LINE_A - 7 ) );
   }


   /**
    * Pre-condition: The order status is ISSUED. This test verifies that the change reason code is
    * MXINCRSE and the shipment line expected quantity is increased after an order line quantity has
    * increased
    *
    * @throws MxException
    *            if any exception occurs
    * @throws Exception
    *            if any exception occurs
    */
   @Test
   public void testSingleOrderLineQuantityIncreased() throws MxException, Exception {

      PurchaseOrderKey lOrder = createOrderBuilder( RefEventStatusKey.POISSUED ).build();

      PurchaseOrderLineKey lOrderLine = createOrderLineBuilder( lOrder, iBatchPartA,
            RefPoLineTypeKey.PURCHASE, new BigDecimal( ORG_QT_LINE_A ) ).build();
      ShipmentKey lShipment = createInboundShipment( lOrder, RefEventStatusKey.IXPEND ).build();
      ShipmentLineKey lShipmentLine =
            createShipmentLine( lShipment, iBatchPartA, lOrderLine, ORG_QT_LINE_A ).build();

      // increase original quantity
      EditOrderLineTO lEditOrderLineTo =
            editOrderLineTOSetup( lOrderLine, ACCOUNT_CD, ORG_QT_LINE_A + 5 );

      // call service to update order line
      OrderService.setPOLines( lOrder, new EditOrderLineTO[] { lEditOrderLineTo }, iHr );

      // assert that change reason code is MXINCRSE, since quantity increased in the order line
      assertEquals( POLineTable.findByPrimaryKey( lOrderLine ).getChangeReasonCd(), "MXINCRSE" );

      // assert that shipment line expected quantity has increased as well
      assertEquals( ShipShipmentLineTable.findByPrimaryKey( lShipmentLine ).getExpectQt(),
            new Double( ORG_QT_LINE_A + 5 ) );
   }


   /**
    * Pre-condition: The order status is ISSUED and order has re-expedite location.The first leg of
    * the inbound shipment is complete. The second leg of the inbound shipment is in-transit. This
    * test verifies that the shipment line expected quantity is increased on the inbound in-transit
    * shipment after an order line quantity has increased.
    *
    * @throws MxException
    *            if any exception occurs
    * @throws Exception
    *            if any exception occurs
    */
   @Test
   public void testUpdateLineBatchQtyIncreasePOWithReexpditeLocation()
         throws MxException, Exception {

      // create order with reexpedite to location
      PurchaseOrderKey lOrder = createOrderBuilder( RefEventStatusKey.POISSUED )
            .reexpediteTo( iReExpediteLocation ).build();

      // add part order line
      PurchaseOrderLineKey lOrderLine = createOrderLineBuilder( lOrder, iBatchPartA,
            RefPoLineTypeKey.PURCHASE, new BigDecimal( ORG_QT_LINE_A ) ).build();

      // create inbound shipment
      ShipmentKey lShipment = createInboundShipment( lOrder, RefEventStatusKey.IXINTR,
            iDockLocation, iReExpediteLocation ).build();

      ShipmentLineKey lShipmentLine =
            createShipmentLine( lShipment, iBatchPartA, lOrderLine, ORG_QT_LINE_A ).build();

      // create 2 shipment legs, first from vendor to dock location, and then from dock location to
      // re-expedite location
      createShipmentRoutingLegs( lShipment, iVendorLocation, iDockLocation, iReExpediteLocation );

      // increase original quantity
      EditOrderLineTO lEditOrderLineTo =
            editOrderLineTOSetup( lOrderLine, ACCOUNT_CD, ORG_QT_LINE_A + 5 );

      // call service to update order line
      OrderService.setPOLines( lOrder, new EditOrderLineTO[] { lEditOrderLineTo }, iHr );

      // assert that change reason code is MXINCRSE, since quantity increased in the order line
      assertEquals( POLineTable.findByPrimaryKey( lOrderLine ).getChangeReasonCd(), "MXINCRSE" );

      // assert that shipment line expected quantity has increased as well
      assertEquals( ShipShipmentLineTable.findByPrimaryKey( lShipmentLine ).getExpectQt(),
            new Double( ORG_QT_LINE_A + 5 ) );
   }


   /**
    * Pre-condition: The order status is ISSUED and order has re-expedite location.The first leg of
    * the inbound shipment is complete. The second leg of the inbound shipment is in-transit. This
    * test verifies that the extra shipment line added for serialized part on inbound shipment after
    * an order line quantity has increased.
    *
    * @throws MxException
    *            if any exception occurs
    * @throws Exception
    *            if any exception occurs
    */
   @Test
   public void testUpdateLineSerializedQtyIncreasePOWithReexpditeLocation()
         throws MxException, Exception {

      // create order with reexpedite to location
      PurchaseOrderKey lOrder = createOrderBuilder( RefEventStatusKey.POISSUED )
            .reexpediteTo( iReExpediteLocation ).build();

      // add part order line
      PurchaseOrderLineKey lOrderLine = createOrderLineBuilder( lOrder, iSerPart,
            RefPoLineTypeKey.PURCHASE, new BigDecimal( ONE_FOR_SER ) ).build();

      // create inbound shipment
      ShipmentKey lShipment = createInboundShipment( lOrder, RefEventStatusKey.IXINTR,
            iDockLocation, iReExpediteLocation ).build();

      ShipmentLineKey lShipmentLine =
            createShipmentLine( lShipment, iSerPart, lOrderLine, ONE_FOR_SER ).build();

      // create 2 shipment legs, first from vendor to dock location, and then from dock location to
      // re-expedite location
      createShipmentRoutingLegs( lShipment, iVendorLocation, iDockLocation, iReExpediteLocation );

      // increase original quantity to 3
      EditOrderLineTO lEditOrderLineTo = editOrderLineTOSetup( lOrderLine, ACCOUNT_CD, 3 );

      // call service to update order line
      OrderService.setPOLines( lOrder, new EditOrderLineTO[] { lEditOrderLineTo }, iHr );

      // assert that change reason code is MXINCRSE, since quantity increased in the order line
      assertEquals( POLineTable.findByPrimaryKey( lOrderLine ).getChangeReasonCd(), "MXINCRSE" );

      // assert that 2 more shipment line is added for serialized part in the shipment
      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "SHIP_SHIPMENT_LINE",
            lShipment.getPKWhereArg() );

      // Assert there are three shipment lines
      assertEquals( 3, lQs.getRowCount() );

      while ( lQs.next() ) {
         if ( lShipmentLine.equals( new ShipmentLineKey( lQs.getInt( "shipment_line_db_id" ),
               lQs.getInt( "shipment_line_id" ) ) ) ) {
            assertEquals(
                  "The pending inbound shipment line's expected quantity for Ser Part A changed incorrectly",
                  ONE_FOR_SER, lQs.getDouble( "expect_qt" ), 0 );
         } else {
            // Assert a new inbound shipment line is created for serialized part with quantity 1
            assertEquals(
                  "The new pending inbound shipment line's expected quantity for Ser part A is wrong",
                  ONE_FOR_SER, lQs.getDouble( "expect_qt" ), 0 );
         }
      }
   }


   /**
    * Pre-condition: The order status is ISSUED and order has re-expedite location.The first leg of
    * the inbound shipment is complete. The second leg of the inbound shipment is pending. This test
    * verifies that the shipment line are removed for serialized part on inbound shipment after an
    * order line quantity has decreased
    *
    * @throws MxException
    *            if any exception occurs
    * @throws Exception
    *            if any exception occurs
    */
   @Test
   public void testUpdateLineSerializedQtyDecreasePOWithReexpditeLocation()
         throws MxException, Exception {

      // create order with reexpedite to location
      PurchaseOrderKey lOrder = createOrderBuilder( RefEventStatusKey.POISSUED )
            .reexpediteTo( iReExpediteLocation ).build();

      // add part order line
      PurchaseOrderLineKey lOrderLine = createOrderLineBuilder( lOrder, iSerPart,
            RefPoLineTypeKey.PURCHASE, new BigDecimal( ORG_QT_LINE_A_SER ) ).build();

      // create inbound shipment
      ShipmentKey lShipment = createInboundShipment( lOrder, RefEventStatusKey.IXPEND,
            iDockLocation, iReExpediteLocation ).build();

      // create 1 shipment line for serialized part
      createShipmentLine( lShipment, iSerPart, lOrderLine, ONE_FOR_SER ).build();

      // create second shipment line for serialized part
      createShipmentLine( lShipment, iSerPart, lOrderLine, ONE_FOR_SER ).build();

      // create 2 shipment legs, first from vendor to dock location, and then from dock location to
      // re-expedite location
      createShipmentRoutingLegs( lShipment, iVendorLocation, iDockLocation, iReExpediteLocation );

      // decrease original quantity to 1
      EditOrderLineTO lEditOrderLineTo = editOrderLineTOSetup( lOrderLine, ACCOUNT_CD, 1 );

      // call service to update order line
      OrderService.setPOLines( lOrder, new EditOrderLineTO[] { lEditOrderLineTo }, iHr );

      // assert that change reason code is MXINCRSE, since quantity increased in the order line
      assertEquals( POLineTable.findByPrimaryKey( lOrderLine ).getChangeReasonCd(), "MXDCRSE" );

      // assert that shipment line is deleted for serialized part in the shipment
      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "SHIP_SHIPMENT_LINE",
            lShipment.getPKWhereArg() );

      // Assert there is only 1 shipment line now
      assertEquals( 1, lQs.getRowCount() );

   }


   /**
    * Pre-condition: The order status is ISSUED and order has re-expedite location.The first leg of
    * the inbound shipment is complete. The second leg of the inbound shipment is in-transit. This
    * test verifies that you are allowed to update the order line price, and and shipment line
    * expected qty is not changed.
    *
    * @throws MxException
    *            if any exception occurs
    * @throws Exception
    *            if any exception occurs
    */
   @Test
   public void testUpdateLinePricePOWithReexpditeLocation() throws MxException, Exception {

      // create order with reexpedite to location
      PurchaseOrderKey lOrder = createOrderBuilder( RefEventStatusKey.POISSUED )
            .reexpediteTo( iReExpediteLocation ).build();

      // add part order line
      PurchaseOrderLineKey lOrderLine = createOrderLineBuilder( lOrder, iBatchPartA,
            RefPoLineTypeKey.PURCHASE, new BigDecimal( ORG_QT_LINE_A ) ).build();

      // create inbound shipment
      ShipmentKey lShipment = createInboundShipment( lOrder, RefEventStatusKey.IXINTR,
            iDockLocation, iReExpediteLocation ).build();

      ShipmentLineKey lShipmentLine =
            createShipmentLine( lShipment, iBatchPartA, lOrderLine, ORG_QT_LINE_A ).build();

      // create 2 shipment legs, first from vendor to dock location, and then from dock location to
      // re-expedite location
      createShipmentRoutingLegs( lShipment, iVendorLocation, iDockLocation, iReExpediteLocation );

      // update the order line unit price
      EditOrderLineTO lEditOrderLineTo =
            editOrderLineTOSetup( lOrderLine, ACCOUNT_CD, ORG_QT_LINE_A );

      BigDecimal lNewUnitPrice = new BigDecimal( 100.00 );
      lEditOrderLineTo.setUnitPrice( lNewUnitPrice, "unit price" );

      // call service to update order line
      OrderService.setPOLines( lOrder, new EditOrderLineTO[] { lEditOrderLineTo }, iHr );

      // assert that order line unit price is updated
      assertEquals( POLineTable.findByPrimaryKey( lOrderLine ).getUnitPrice(), lNewUnitPrice );

      // assert that shipment line expected quantity has not changed
      assertEquals( ShipShipmentLineTable.findByPrimaryKey( lShipmentLine ).getExpectQt(),
            new Double( ORG_QT_LINE_A ) );
   }


   /**
    * Pre-condition: The order status is ISSUED and order has re-expedite location.The first leg of
    * the inbound shipment is complete. The second leg of the inbound shipment is in-transit. This
    * test verifies that you are allowed to update the order line promised by date, and shipment
    * line expected qty is not changed.
    *
    * @throws MxException
    *            if any exception occurs
    * @throws Exception
    *            if any exception occurs
    */
   @Test
   public void testUpdatePromisedByDatePOWithReexpditeLocation() throws MxException, Exception {

      // create order with reexpedite to location
      PurchaseOrderKey lOrder = createOrderBuilder( RefEventStatusKey.POISSUED )
            .reexpediteTo( iReExpediteLocation ).build();

      // add part order line
      PurchaseOrderLineKey lOrderLine = createOrderLineBuilder( lOrder, iBatchPartA,
            RefPoLineTypeKey.PURCHASE, new BigDecimal( ORG_QT_LINE_A ) ).build();

      // create inbound shipment
      ShipmentKey lShipment = createInboundShipment( lOrder, RefEventStatusKey.IXINTR,
            iDockLocation, iReExpediteLocation ).build();

      ShipmentLineKey lShipmentLine =
            createShipmentLine( lShipment, iBatchPartA, lOrderLine, ORG_QT_LINE_A ).build();

      // create 2 shipment legs, first from vendor to dock location, and then from dock location to
      // re-expedite location
      createShipmentRoutingLegs( lShipment, iVendorLocation, iDockLocation, iReExpediteLocation );

      // update the order line promised by date
      EditOrderLineTO lEditOrderLineTo =
            editOrderLineTOSetup( lOrderLine, ACCOUNT_CD, ORG_QT_LINE_A );
      Date lNewPromisedByDate = DateUtils.addDays( new Date(), 1 );
      lEditOrderLineTo.setPromisedBy( lNewPromisedByDate );

      // call service to update order line
      OrderService.setPOLines( lOrder, new EditOrderLineTO[] { lEditOrderLineTo }, iHr );

      // assert that order line promised by date is updated
      Date lPromisedByDate = POLineTable.findByPrimaryKey( lOrderLine ).getPromisedBy();
      assertTrue( "Dates aren't close enough to each other!",
            Math.abs( lPromisedByDate.getTime() - lNewPromisedByDate.getTime() ) < 1000 );

      // assert that shipment line expected quantity has not changed
      assertEquals( ShipShipmentLineTable.findByPrimaryKey( lShipmentLine ).getExpectQt(),
            new Double( ORG_QT_LINE_A ) );
   }


   /**
    * Pre-condition: The order status is ISSUED and order has re-expedite location.The first leg of
    * the inbound shipment is complete. The second leg of the inbound shipment is pending. This test
    * verifies that the shipment line expected quantity is decreased on inbound shipment after an
    * order line quantity has decreased.
    *
    * @throws MxException
    *            if any exception occurs
    * @throws Exception
    *            if any exception occurs
    */
   @Test
   public void testUpdateLineBatchQtyDecreasePOWithReexpditeLocation()
         throws MxException, Exception {

      // create order with reexpedite to location
      PurchaseOrderKey lOrder = createOrderBuilder( RefEventStatusKey.POISSUED )
            .reexpediteTo( iReExpediteLocation ).build();

      // add part order line
      PurchaseOrderLineKey lOrderLine = createOrderLineBuilder( lOrder, iBatchPartA,
            RefPoLineTypeKey.PURCHASE, new BigDecimal( ORG_QT_LINE_A ) ).build();

      // create inbound shipment
      ShipmentKey lShipment = createInboundShipment( lOrder, RefEventStatusKey.IXPEND,
            iDockLocation, iReExpediteLocation ).build();

      ShipmentLineKey lShipmentLine =
            createShipmentLine( lShipment, iBatchPartA, lOrderLine, ORG_QT_LINE_A ).build();

      // create 2 shipment legs, first from vendor to dock location, and then from dock location to
      // re-expedite location
      createShipmentRoutingLegs( lShipment, iVendorLocation, iDockLocation, iReExpediteLocation );

      // decrease original quantity
      EditOrderLineTO lEditOrderLineTo =
            editOrderLineTOSetup( lOrderLine, ACCOUNT_CD, ORG_QT_LINE_A - 5 );

      // call service to update order line
      OrderService.setPOLines( lOrder, new EditOrderLineTO[] { lEditOrderLineTo }, iHr );

      // assert that change reason code is MXDCRSE, since quantity decreased in the order line
      assertEquals( POLineTable.findByPrimaryKey( lOrderLine ).getChangeReasonCd(), "MXDCRSE" );

      // assert that shipment line expected quantity has decreased as well
      assertEquals( ShipShipmentLineTable.findByPrimaryKey( lShipmentLine ).getExpectQt(),
            new Double( ORG_QT_LINE_A - 5 ) );
   }


   /**
    * Pre-condition: If utl_config_param REAUTHORIZE_PO is ON and after editing the line price, the
    * issued order status will change to OPEN. This test verifies that the change reason code is
    * MXINCRSE and the shipment line expected quantity is increased after an order line quantity has
    * increased even right now this order status is OPEN
    *
    * @throws MxException
    *            if any exception occurs
    * @throws Exception
    *            if any exception occurs
    */
   @Test
   public void testSingleOrderLineQuantityIncreasedForOpenOrder() throws MxException, Exception {

      PurchaseOrderKey lOrder = createOrderBuilder( RefEventStatusKey.POOPEN ).build();

      PurchaseOrderLineKey lOrderLine = createOrderLineBuilder( lOrder, iBatchPartA,
            RefPoLineTypeKey.PURCHASE, new BigDecimal( ORG_QT_LINE_A ) ).build();
      ShipmentKey lShipment = createInboundShipment( lOrder, RefEventStatusKey.IXPEND ).build();
      ShipmentLineKey lShipmentLine =
            createShipmentLine( lShipment, iBatchPartA, lOrderLine, ORG_QT_LINE_A ).build();

      // increase original quantity
      EditOrderLineTO lEditOrderLineTo =
            editOrderLineTOSetup( lOrderLine, ACCOUNT_CD, ORG_QT_LINE_A + 5 );

      // call service to update order line
      OrderService.setPOLines( lOrder, new EditOrderLineTO[] { lEditOrderLineTo }, iHr );

      // assert that change reason code is MXINCRSE, since quantity increased in the order line
      assertEquals( POLineTable.findByPrimaryKey( lOrderLine ).getChangeReasonCd(), "MXINCRSE" );

      // assert that shipment line expected quantity has increased as well
      assertEquals( ShipShipmentLineTable.findByPrimaryKey( lShipmentLine ).getExpectQt(),
            new Double( ORG_QT_LINE_A + 5 ) );
   }


   /**
    * Pre-condition: The order status is ISSUED. This test verifies that if order quantity does not
    * change, then the shipment expected quantity does not change
    *
    * @throws MxException
    *            if any exception occurs
    * @throws Exception
    *            if any exception occurs
    */
   @Test
   public void testSingleOrderLineQuantityNotChanged() throws MxException, Exception {

      PurchaseOrderKey lOrder = createOrderBuilder( RefEventStatusKey.POISSUED ).build();

      PurchaseOrderLineKey lOrderLine = createOrderLineBuilder( lOrder, iBatchPartA,
            RefPoLineTypeKey.PURCHASE, new BigDecimal( ORG_QT_LINE_A ) ).build();
      ShipmentKey lShipment = createInboundShipment( lOrder, RefEventStatusKey.IXPEND ).build();
      ShipmentLineKey lShipmentLine =
            createShipmentLine( lShipment, iBatchPartA, lOrderLine, ORG_QT_LINE_A ).build();

      // use original quantity
      EditOrderLineTO lEditOrderLineTo =
            editOrderLineTOSetup( lOrderLine, ACCOUNT_CD, ORG_QT_LINE_A );

      // call service to update order line
      OrderService.setPOLines( lOrder, new EditOrderLineTO[] { lEditOrderLineTo }, iHr );

      // assert shipment line expected quantity did not change
      assertEquals( ShipShipmentLineTable.findByPrimaryKey( lShipmentLine ).getExpectQt(),
            new Double( ORG_QT_LINE_A ) );
   }


   /**
    * Pre-condition: If utl_config_param REAUTHORIZE_PO is OFF and after editing the line price, the
    * issued order status will change to AUTH. Then issue the order again, the status will change to
    * ISSUED; If utl_config_param REAUTHORIZE_PO is ON and after editing the line price, the issued
    * order status will change to OPEN. Then authorize and issue the order again, the status will
    * change to ISSUED.
    *
    * These two tests are testing no new inbound shipment line created when just edit ISSUED status
    * order line for two scenarios
    *
    * @throws MxException
    *            if any exception occurs
    * @throws Exception
    *            if any exception occurs
    */
   @Test
   public void testMutipleOrderLinesQuantityNotChangeForIssuedOrder()
         throws MxException, Exception {

      testNoNewInboundShipLineCreatedWhenEditAlreadyReceivedLine( RefEventStatusKey.POISSUED );
      testNoNewInboundShipLineCreatedWhenEditPartiallyReceivedLine( RefEventStatusKey.POISSUED );
   }


   /**
    * Pre-condition: No matter utl_config_param REAUTHORIZE_PO is ON or OFF and after partially
    * received the order, the issued order status will change to PARTIAL.
    *
    * These two tests are testing no new inbound shipment line created when just edit PARTIAL status
    * order line for two scenarios
    *
    * @throws MxException
    *            if any exception occurs
    * @throws Exception
    *            if any exception occurs
    */
   @Test
   public void testMutipleOrderLinesQuantityNotChangeForPartialOrder()
         throws MxException, Exception {

      testNoNewInboundShipLineCreatedWhenEditAlreadyReceivedLine( RefEventStatusKey.POPARTIAL );
      testNoNewInboundShipLineCreatedWhenEditPartiallyReceivedLine( RefEventStatusKey.POPARTIAL );
   }


   /**
    * Pre-condition: If utl_config_param REAUTHORIZE_PO is ON and after editing the line price, the
    * issued order status will change to OPEN.
    *
    * These two tests are testing no new inbound shipment line created when just edit OPEN status
    * order line for two scenarios
    *
    * @throws MxException
    *            if any exception occurs
    * @throws Exception
    *            if any exception occurs
    */
   @Test
   public void testMutipleOrderLinesQuantityNotChangeForOpenOrder() throws MxException, Exception {

      testNoNewInboundShipLineCreatedWhenEditAlreadyReceivedLine( RefEventStatusKey.POOPEN );
      testNoNewInboundShipLineCreatedWhenEditPartiallyReceivedLine( RefEventStatusKey.POOPEN );
   }


   /**
    * Pre-condition: If utl_config_param REAUTHORIZE_PO is OFF and after editing order line, the
    * issued order status will change to AUTH.
    *
    * These two tests are testing no new inbound shipment line created when just edit AUTH status
    * order line for two scenarios
    *
    * @throws MxException
    *            if any exception occurs
    * @throws Exception
    *            if any exception occurs
    */
   @Test
   public void testMutipleOrderLinesQuantityNotChangeForAuthOrder() throws MxException, Exception {

      testNoNewInboundShipLineCreatedWhenEditAlreadyReceivedLine( RefEventStatusKey.POAUTH );
      testNoNewInboundShipLineCreatedWhenEditPartiallyReceivedLine( RefEventStatusKey.POAUTH );
   }


   /**
    * Pre-condition: If utl_config_param REAUTHORIZE_PO is OFF and after editing the line price, the
    * issued order status will change to AUTH. Then issue the order again, the status will change to
    * ISSUED; If utl_config_param REAUTHORIZE_PO is ON and after editing the line price, the issued
    * order status will change to OPEN. Then authorize and issue the order again, the status will
    * change to ISSUED.
    *
    * These three tests are testing new inbound shipment line is created or existing inbound
    * shipment line's quantity is increased when increase ISSUED status order line's quantity for
    * three scenarios
    *
    * @throws MxException
    *            if any exception occurs
    * @throws Exception
    *            if any exception occurs
    */
   @Test
   public void testMutipleOrderLinesQuantityIncreaseForIssuedOrder() throws MxException, Exception {

      testNewInboundShipLineCreatedForAlreadyReceivedLineWhenIncreaseItsOrderQty(
            RefEventStatusKey.POISSUED );
      testNewInboundShipLineCreatedForSerOrderLineWhenIncreaseSerQty( RefEventStatusKey.POISSUED );
      testInboundShipLineQtyIncreasedWhenIncreaseBatchOrderLineQty( RefEventStatusKey.POISSUED );
   }


   /**
    * Pre-condition: No matter utl_config_param REAUTHORIZE_PO is ON or OFF and after partially
    * received the order, the issued order status will change to PARTIAL.
    *
    * These three tests are testing new inbound shipment line is created or existing inbound
    * shipment line's quantity is increased when increase PARTIAL status order line's quantity for
    * three scenarios
    *
    * @throws MxException
    *            if any exception occurs
    * @throws Exception
    *            if any exception occurs
    */
   @Test
   public void testMutipleOrderLinesQuantityIncreaseForPartialOrder()
         throws MxException, Exception {

      testNewInboundShipLineCreatedForAlreadyReceivedLineWhenIncreaseItsOrderQty(
            RefEventStatusKey.POPARTIAL );
      testNewInboundShipLineCreatedForSerOrderLineWhenIncreaseSerQty( RefEventStatusKey.POPARTIAL );
      testInboundShipLineQtyIncreasedWhenIncreaseBatchOrderLineQty( RefEventStatusKey.POPARTIAL );
   }


   /**
    * Pre-condition: If utl_config_param REAUTHORIZE_PO is ON and after editing the line price, the
    * issued order status will change to OPEN.
    *
    * These three tests are testing new inbound shipment line is created or existing inbound
    * shipment line's quantity is increased when increase OPEN status order line's quantity for
    * three scenarios
    *
    * @throws MxException
    *            if any exception occurs
    * @throws Exception
    *            if any exception occurs
    */
   @Test
   public void testMutipleOrderLinesQuantityIncreaseForOpenOrder() throws MxException, Exception {

      testNewInboundShipLineCreatedForAlreadyReceivedLineWhenIncreaseItsOrderQty(
            RefEventStatusKey.POOPEN );
      testNewInboundShipLineCreatedForSerOrderLineWhenIncreaseSerQty( RefEventStatusKey.POOPEN );
      testInboundShipLineQtyIncreasedWhenIncreaseBatchOrderLineQty( RefEventStatusKey.POOPEN );
   }


   /**
    * Pre-condition: If utl_config_param REAUTHORIZE_PO is OFF and after editing order line, the
    * issued order status will change to AUTH.
    *
    * These three tests are testing new inbound shipment line is created or existing inbound
    * shipment line's quantity is increased when increase AUTH status order line's quantity for
    * three scenarios
    *
    * @throws MxException
    *            if any exception occurs
    * @throws Exception
    *            if any exception occurs
    */
   @Test
   public void testMutipleOrderLinesQuantityIncreaseForAuthOrder() throws MxException, Exception {

      testNewInboundShipLineCreatedForAlreadyReceivedLineWhenIncreaseItsOrderQty(
            RefEventStatusKey.POAUTH );
      testNewInboundShipLineCreatedForSerOrderLineWhenIncreaseSerQty( RefEventStatusKey.POAUTH );
      testInboundShipLineQtyIncreasedWhenIncreaseBatchOrderLineQty( RefEventStatusKey.POAUTH );
   }


   /**
    * Pre-condition: If utl_config_param REAUTHORIZE_PO is OFF and after editing the line price, the
    * issued order status will change to AUTH. Then issue the order again, the status will change to
    * ISSUED; If utl_config_param REAUTHORIZE_PO is ON and after editing the line price, the issued
    * order status will change to OPEN. Then authorize and issue the order again, the status will
    * change to ISSUED.
    *
    * These two tests are testing no need inbound shipment line is removed or existing inbound
    * shipment line's quantity is decreased when decrease ISSUED status order line's quantity for
    * two scenarios
    *
    * @throws MxException
    *            if any exception occurs
    * @throws Exception
    *            if any exception occurs
    */
   @Test
   public void testMutipleOrderLinesQuantityDecreaseForIssuedOrder() throws MxException, Exception {

      testInboundShipLineRemovedForSerOrderLineWhenDecreaseSerQty( RefEventStatusKey.POISSUED );
      testInboundShipLineQtyDecreasedWhenDecreaseBatchOrderLineQty( RefEventStatusKey.POISSUED );
   }


   /**
    * Pre-condition: No matter utl_config_param REAUTHORIZE_PO is ON or OFF and after partially
    * received the order, the issued order status will change to PARTIAL.
    *
    * These two tests are testing no need inbound shipment line is removed or existing inbound
    * shipment line's quantity is decreased when decrease PARTIAL status order line's quantity for
    * two scenarios
    *
    * @throws MxException
    *            if any exception occurs
    * @throws Exception
    *            if any exception occurs
    */
   @Test
   public void testMutipleOrderLinesQuantityDecreaseForPartialOrder()
         throws MxException, Exception {

      testInboundShipLineRemovedForSerOrderLineWhenDecreaseSerQty( RefEventStatusKey.POPARTIAL );
      testInboundShipLineQtyDecreasedWhenDecreaseBatchOrderLineQty( RefEventStatusKey.POPARTIAL );
   }


   /**
    * Pre-condition: If utl_config_param REAUTHORIZE_PO is ON and after editing the line price, the
    * issued order status will change to OPEN.
    *
    * These two tests are testing no need inbound shipment line is removed or existing inbound
    * shipment line's quantity is decreased when decrease OPEN status order line's quantity for two
    * scenarios
    *
    * @throws MxException
    *            if any exception occurs
    * @throws Exception
    *            if any exception occurs
    */
   @Test
   public void testMutipleOrderLinesQuantityDecreaseForOpenOrder() throws MxException, Exception {

      testInboundShipLineRemovedForSerOrderLineWhenDecreaseSerQty( RefEventStatusKey.POOPEN );
      testInboundShipLineQtyDecreasedWhenDecreaseBatchOrderLineQty( RefEventStatusKey.POOPEN );
   }


   /**
    * Pre-condition: If utl_config_param REAUTHORIZE_PO is OFF and after editing order line, the
    * issued order status will change to AUTH.
    *
    * These two tests are testing no need inbound shipment line is removed or existing inbound
    * shipment line's quantity is decreased when decrease AUTH status order line's quantity for two
    * scenarios
    *
    * @throws MxException
    *            if any exception occurs
    * @throws Exception
    *            if any exception occurs
    */
   @Test
   public void testMutipleOrderLinesQuantityDecreaseForAuthOrder() throws MxException, Exception {

      testInboundShipLineRemovedForSerOrderLineWhenDecreaseSerQty( RefEventStatusKey.POAUTH );
      testInboundShipLineQtyDecreasedWhenDecreaseBatchOrderLineQty( RefEventStatusKey.POAUTH );
   }


   /**
    * Pre-condition: A purchase order with two order lines: One is for batch part A with expected
    * quantity 10; another one is for batch part B with expected quantity 3.
    *
    * There is one complete historic inbound shipment received 1 of part A and 4 of part B. So now
    * the pending inbound shipment just has one line for part A with expected quantity (10-1=)9.
    *
    * And then return 1 of part A to vendor, so there is a pending outbound shipment for it.
    *
    * When Edit Lines to increase the quantity of part B order line to 5.
    *
    * Assert: The pending inbound shipment has a new shipment line added for part B with expected
    * quantity (5-4=)1. The shipment line for part A stays the same quantity as before. And the
    * pending outbound shipment has no change with the returned quantity 1.
    *
    * @param aOrderStatus
    *           the order status
    *
    * @throws MxException
    *            if any exception occurs
    * @throws Exception
    *            if any exception occurs
    */
   @Test
   public void testNoNewOutboundShipLineCreatedWhenNewInboundShipLineCreated()
         throws MxException, Exception {

      /** DATA SETUP **/
      // An exchange order with two lines, one has been received 1/10, another one has been received
      // 4/3
      PurchaseOrderKey lPurchaseOrder = createOrderWithTwoLinesAndHistoricInboundShipment(
            RefEventStatusKey.POISSUED, RefPoLineTypeKey.PURCHASE, iBatchPartB, ORG_QT_LINE_B,
            ORG_QT_LINE_B, HIS_OVER_RECEIVED_QT_LINE_B );
      // Current pending inbound shipment has one line for part A with quantity (10-1=)9
      ShipmentKey lPendInboundShipment =
            createInboundShipment( lPurchaseOrder, RefEventStatusKey.IXPEND ).build();
      double lNewExpectedQtLineA = ORG_QT_LINE_A - HIS_PARTIALLY_RECEIVED_QT_LINE_A;
      ShipmentLineKey lPendInShipmentLineA = createShipmentLine( lPendInboundShipment, iBatchPartA,
            iOrderLineA, lNewExpectedQtLineA ).build();

      // part A with quantity 1 has been returned to vendor, so there is one pending outbound
      // shipment line for it
      ShipmentKey lPendOutboundShipment =
            createOutboundShipment( lPurchaseOrder, RefEventStatusKey.IXPEND ).build();
      ShipmentLineKey lPendOutShipmentLineA =
            createShipmentLine( lPendOutboundShipment, iBatchPartA, iOrderLineA, 1 ).build();

      /** EDIT LINES **/
      // Only increase the quantity of part B line (from 3 to 5)
      EditOrderLineTO lEditOrderLineToA =
            editOrderLineTOSetup( iOrderLineA, ACCOUNT_CD, ORG_QT_LINE_A );
      EditOrderLineTO lEditOrderLineToB =
            editOrderLineTOSetup( iOrderLineB, ACCOUNT_CD, ORG_QT_LINE_B + 2 );

      // call service to update order line
      OrderService.setPOLines( lPurchaseOrder,
            new EditOrderLineTO[] { lEditOrderLineToA, lEditOrderLineToB }, iHr );

      /** ASSERTION **/
      // assert that change reason code is MXINCRSE, since quantity increased in the Batch part B
      // order line
      assertEquals( POLineTable.findByPrimaryKey( iOrderLineA ).getChangeReasonCd(), null );
      assertEquals( POLineTable.findByPrimaryKey( iOrderLineB ).getChangeReasonCd(), "MXINCRSE" );

      // Get all shipment lines of current pending inbound shipment
      QuerySet InlQs = QuerySetFactory.getInstance().executeQueryTable( "SHIP_SHIPMENT_LINE",
            lPendInboundShipment.getPKWhereArg() );

      // Assert there are still two shipment lines
      assertEquals( 2, InlQs.getRowCount() );
      while ( InlQs.next() ) {
         // Assert one of the shipment lines is the one for part A with quantity (original 10 -
         // received 1=)9, same as before editing
         if ( lPendInShipmentLineA.equals( new ShipmentLineKey(
               InlQs.getInt( "shipment_line_db_id" ), InlQs.getInt( "shipment_line_id" ) ) ) ) {
            assertEquals(
                  "The pending inbound shipment line's expected quantity for part A changed incorrectly",
                  lNewExpectedQtLineA, InlQs.getDouble( "expect_qt" ), 0 );
         } else {
            // Assert another one is a new one for part B with quantity (new increased to quantity 5
            // - received 4=)1
            double lNewExpectedQtLineB = ORG_QT_LINE_B + 2 - HIS_OVER_RECEIVED_QT_LINE_B;
            assertEquals( "The new shipment line for part B didn't been added", iBatchPartB,
                  new PartNoKey( InlQs.getInt( "part_no_db_id" ), InlQs.getInt( "part_no_id" ) ) );
            assertEquals( "The new added shipment line for part B's expected quantity is wrong",
                  lNewExpectedQtLineB, InlQs.getDouble( "expect_qt" ), 0 );
         }
      }

      // Get all shipment lines of current pending outbound shipment
      QuerySet OutlQs = QuerySetFactory.getInstance().executeQueryTable( "SHIP_SHIPMENT_LINE",
            lPendOutboundShipment.getPKWhereArg() );

      while ( OutlQs.next() ) {
         // Assert the only shipment line is the one for part A with quantity 1, same as before
         // editing
         assertEquals( "The pending outbound shipment line changed incorrectly",
               lPendOutShipmentLineA, new ShipmentLineKey( OutlQs.getInt( "shipment_line_db_id" ),
                     OutlQs.getInt( "shipment_line_id" ) ) );
         assertEquals(
               "The pending outbound shipment line's expected quantity for part A changed incorrectly",
               1, OutlQs.getDouble( "expect_qt" ), 0 );
      }

   }


   /**
    * Pre-condition: A purchase order with two order lines: One is for batch part A with expected
    * quantity 10; another one is for batch part B with expected quantity 3.
    *
    * There is one complete historic inbound shipment received 1 of part A and 4 of part B. So now
    * the pending inbound shipment just has one line for part A with expected quantity (10-1=)9.
    *
    * When Edit Lines to change the account of the two lines.
    *
    * Assert: The pending inbound shipment line keeps the same quantity. No new shipment line added.
    *
    * @param aOrderStatus
    *           the order status
    *
    * @throws MxException
    *            if any exception occurs
    * @throws Exception
    *            if any exception occurs
    */
   private void testNoNewInboundShipLineCreatedWhenEditAlreadyReceivedLine(
         RefEventStatusKey aOrderStatus ) throws MxException, Exception {

      /** DATA SETUP **/
      // A purchase order with two lines, one has been received 1/10, another one has been received
      // 4/3
      PurchaseOrderKey lPurchaseOrder = createOrderWithTwoLinesAndHistoricInboundShipment(
            aOrderStatus, RefPoLineTypeKey.PURCHASE, iBatchPartB, ORG_QT_LINE_B, ORG_QT_LINE_B,
            HIS_OVER_RECEIVED_QT_LINE_B );
      // Current pending inbound shipment has one line for part A with quantity (10-1=)9
      ShipmentKey lPendInboundShipment =
            createInboundShipment( lPurchaseOrder, RefEventStatusKey.IXPEND ).build();
      double lNewExpectedQtLineA = ORG_QT_LINE_A - HIS_PARTIALLY_RECEIVED_QT_LINE_A;
      ShipmentLineKey lPendShipmentLineA = createShipmentLine( lPendInboundShipment, iBatchPartA,
            iOrderLineA, lNewExpectedQtLineA ).build();

      /** EDIT LINES **/
      // Only change the account for two lines
      EditOrderLineTO lEditOrderLineToA =
            editOrderLineTOSetup( iOrderLineA, ALT_ACCOUNT_CD, ORG_QT_LINE_A );
      EditOrderLineTO lEditOrderLineToB =
            editOrderLineTOSetup( iOrderLineB, ALT_ACCOUNT_CD, ORG_QT_LINE_B );

      // call service to update order line
      OrderService.setPOLines( lPurchaseOrder,
            new EditOrderLineTO[] { lEditOrderLineToA, lEditOrderLineToB }, iHr );

      /** ASSERTION **/
      // assert that there is no change reason code for the two order lines
      assertEquals( POLineTable.findByPrimaryKey( iOrderLineA ).getChangeReasonCd(), null );
      assertEquals( POLineTable.findByPrimaryKey( iOrderLineB ).getChangeReasonCd(), null );

      // Get all shipment lines of current pending inbound shipment
      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "SHIP_SHIPMENT_LINE",
            lPendInboundShipment.getPKWhereArg() );

      if ( lQs.next() ) {
         // Assert the only shipment line is the one for part A with quantity (original 10 -
         // received 1=)9, same as before editing
         assertEquals( "The pending inbound shipment line changed incorrectly", lPendShipmentLineA,
               new ShipmentLineKey( lQs.getInt( "shipment_line_db_id" ),
                     lQs.getInt( "shipment_line_id" ) ) );
         assertEquals(
               "The pending inbound shipment line's expected quantity for part A changed incorrectly",
               lNewExpectedQtLineA, lQs.getDouble( "expect_qt" ), 0 );
      }
      assertFalse( lQs.next() );

   }


   /**
    * Pre-condition: A purchase order with two order lines: One is for batch part A with expected
    * quantity 10; another one is for SER part B with expected quantity 2.
    *
    * There is one complete historic inbound shipment received 9 of part A and 1 of part B. So now
    * the pending inbound shipment has one line for part A with expected quantity (10-9=)1 and one
    * line for part B with expected quantity 1
    *
    * When Edit Lines to change the account of the two lines.
    *
    * Assert: The two pending inbound shipment lines keep the same quantity. No new shipment line
    * added.
    *
    * @param aOrderStatus
    *           the order status
    *
    * @throws MxException
    *            if any exception occurs
    * @throws Exception
    *            if any exception occurs
    */
   private void testNoNewInboundShipLineCreatedWhenEditPartiallyReceivedLine(
         RefEventStatusKey aOrderStatus ) throws MxException, Exception {

      /** DATA SETUP **/
      // A purchase order with two lines, one has been received 9/10, another one has been received
      // 1/2
      PurchaseOrderKey lPurchaseOrder = createOrderWithTwoLinesAndHistoricInboundShipment(
            aOrderStatus, RefPoLineTypeKey.PURCHASE, iSerPart, ORG_QT_LINE_B_SER, ONE_FOR_SER,
            ONE_FOR_SER );
      // Current pending inbound shipment has one line for part A with quantity (10-9=)1 and one
      // line for part B with expected quantity 1 (expected quantity is always 1 for SER part
      // shipment line)
      ShipmentKey lPendInboundShipment =
            createInboundShipment( lPurchaseOrder, RefEventStatusKey.IXPEND ).build();
      double lNewExpectedQtLineA = ORG_QT_LINE_A - HIS_PARTIALLY_RECEIVED_QT_LINE_A;
      ShipmentLineKey lPendShipmentLineA = createShipmentLine( lPendInboundShipment, iBatchPartA,
            iOrderLineA, lNewExpectedQtLineA ).build();
      ShipmentLineKey lPendShipmentLineB =
            createShipmentLine( lPendInboundShipment, iSerPart, iOrderLineB, ONE_FOR_SER ).build();

      /** EDIT LINES **/
      // assert that there is no change reason code for the two order lines
      assertEquals( POLineTable.findByPrimaryKey( iOrderLineA ).getChangeReasonCd(), null );
      assertEquals( POLineTable.findByPrimaryKey( iOrderLineB ).getChangeReasonCd(), null );

      // Only change the account for two lines
      EditOrderLineTO lEditOrderLineToA =
            editOrderLineTOSetup( iOrderLineA, ALT_ACCOUNT_CD, ORG_QT_LINE_A );
      EditOrderLineTO lEditOrderLineToB =
            editOrderLineTOSetup( iOrderLineB, ALT_ACCOUNT_CD, ORG_QT_LINE_B_SER );

      // call service to update order line
      OrderService.setPOLines( lPurchaseOrder,
            new EditOrderLineTO[] { lEditOrderLineToA, lEditOrderLineToB }, iHr );

      /** ASSERTION **/
      // Get all shipment lines of current pending inbound shipment
      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "SHIP_SHIPMENT_LINE",
            lPendInboundShipment.getPKWhereArg() );

      // Assert there are still two shipment lines
      assertEquals( 2, lQs.getRowCount() );
      while ( lQs.next() ) {
         // Assert one of the shipment lines is the one for part A with quantity (original 10 -
         // received 1=)9, same as before editing
         if ( lPendShipmentLineA.equals( new ShipmentLineKey( lQs.getInt( "shipment_line_db_id" ),
               lQs.getInt( "shipment_line_id" ) ) ) ) {
            assertEquals(
                  "The pending inbound shipment line's expected quantity for part A changed incorrectly",
                  lNewExpectedQtLineA, lQs.getDouble( "expect_qt" ), 0 );
         } else {
            // Assert another one of the shipment lines is the one for part C with quantity 1, same
            // as before editing
            assertEquals( "The pending inbound shipment line for part C disappeared incorrectly",
                  lPendShipmentLineB, new ShipmentLineKey( lQs.getInt( "shipment_line_db_id" ),
                        lQs.getInt( "shipment_line_id" ) ) );
            assertEquals(
                  "The pending inbound shipment line for SER part C's expected quantity is wrong",
                  ONE_FOR_SER, lQs.getDouble( "expect_qt" ), 0 );
         }
      }

   }


   /**
    * Pre-condition: An exchange order with two order lines: One is for batch part A with expected
    * quantity 10; another one is for batch part B with expected quantity 3.
    *
    * There is one complete historic inbound shipment received 1 of part A and 4 of part B. So now
    * the pending inbound shipment just has one line for part A with expected quantity (10-1=)9.
    *
    * When Edit Lines to increase the quantity of part B order line to 5.
    *
    * Assert: The pending inbound shipment has a new shipment line added for part B with expected
    * quantity (5-4=)1. The shipment line for part A stays the same quantity as before.
    *
    * @param aOrderStatus
    *           the order status
    *
    * @throws MxException
    *            if any exception occurs
    * @throws Exception
    *            if any exception occurs
    */
   private void testNewInboundShipLineCreatedForAlreadyReceivedLineWhenIncreaseItsOrderQty(
         RefEventStatusKey aOrderStatus ) throws MxException, Exception {

      /** DATA SETUP **/
      // An exchange order with two lines, one has been received 1/10, another one has been received
      // 4/3
      PurchaseOrderKey lExhangeOrder = createOrderWithTwoLinesAndHistoricInboundShipment(
            aOrderStatus, RefPoLineTypeKey.EXCHANGE, iBatchPartB, ORG_QT_LINE_B, ORG_QT_LINE_B,
            HIS_OVER_RECEIVED_QT_LINE_B );
      // Current pending inbound shipment has one line for part A with quantity (10-1=)9
      ShipmentKey lPendInboundShipment =
            createInboundShipment( lExhangeOrder, RefEventStatusKey.IXPEND ).build();
      double lNewExpectedQtLineA = ORG_QT_LINE_A - HIS_PARTIALLY_RECEIVED_QT_LINE_A;
      ShipmentLineKey lPendShipmentLineA = createShipmentLine( lPendInboundShipment, iBatchPartA,
            iOrderLineA, lNewExpectedQtLineA ).build();

      /** EDIT LINES **/
      // Only increase the quantity of part B line (from 3 to 5)
      EditOrderLineTO lEditOrderLineToA =
            editOrderLineTOSetup( iOrderLineA, ACCOUNT_CD, ORG_QT_LINE_A );
      EditOrderLineTO lEditOrderLineToB =
            editOrderLineTOSetup( iOrderLineB, ACCOUNT_CD, ORG_QT_LINE_B + 2 );

      // call service to update order line
      OrderService.setPOLines( lExhangeOrder,
            new EditOrderLineTO[] { lEditOrderLineToA, lEditOrderLineToB }, iHr );

      /** ASSERTION **/
      // assert that change reason code is MXINCRSE, since quantity increased in the Batch part B
      // order line
      assertEquals( POLineTable.findByPrimaryKey( iOrderLineA ).getChangeReasonCd(), null );
      assertEquals( POLineTable.findByPrimaryKey( iOrderLineB ).getChangeReasonCd(), "MXINCRSE" );

      // Get all shipment lines of current pending inbound shipment
      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "SHIP_SHIPMENT_LINE",
            lPendInboundShipment.getPKWhereArg() );

      // Assert there are still two shipment lines
      assertEquals( 2, lQs.getRowCount() );
      while ( lQs.next() ) {
         // Assert one of the shipment lines is the one for part A with quantity (original 10 -
         // received 1=)9, same as before editing
         if ( lPendShipmentLineA.equals( new ShipmentLineKey( lQs.getInt( "shipment_line_db_id" ),
               lQs.getInt( "shipment_line_id" ) ) ) ) {
            assertEquals(
                  "The pending inbound shipment line's expected quantity for part A changed incorrectly",
                  lNewExpectedQtLineA, lQs.getDouble( "expect_qt" ), 0 );
         } else {
            // Assert another one is a new one for part B with quantity (new increased to quantity 5
            // - received 4=)1
            double lNewExpectedQtLineB = ORG_QT_LINE_B + 2 - HIS_OVER_RECEIVED_QT_LINE_B;
            assertEquals( "The new shipment line for part B didn't been added", iBatchPartB,
                  new PartNoKey( lQs.getInt( "part_no_db_id" ), lQs.getInt( "part_no_id" ) ) );
            assertEquals( "The new added shipment line for part B's expected quantity is wrong",
                  lNewExpectedQtLineB, lQs.getDouble( "expect_qt" ), 0 );
         }
      }

   }


   /**
    * Pre-condition: An exchange order with two order lines: One is for batch part A with expected
    * quantity 10; another one is for batch part B with expected quantity 3.
    *
    * There is one complete historic inbound shipment received 1 of part A and 4 of part B. So now
    * the pending inbound shipment just has one line for part A with expected quantity (10-1=)9.
    *
    * When Edit Lines to increase the quantity of part A order line to (10+1=)11.
    *
    * Assert: There is still only one same inbound shipment line for part A, but the expected
    * quantity is increased to (9+1=)10
    *
    * @param aOrderStatus
    *           the order status
    *
    * @throws MxException
    *            if any exception occurs
    * @throws Exception
    *            if any exception occurs
    */
   private void testInboundShipLineQtyIncreasedWhenIncreaseBatchOrderLineQty(
         RefEventStatusKey aOrderStatus ) throws MxException, Exception {

      /** DATA SETUP **/
      // An exchange order with two lines, one has been received 1/10, another one has been received
      // 4/3
      PurchaseOrderKey lExhangeOrder = createOrderWithTwoLinesAndHistoricInboundShipment(
            aOrderStatus, RefPoLineTypeKey.EXCHANGE, iBatchPartB, ORG_QT_LINE_B, ORG_QT_LINE_B,
            HIS_OVER_RECEIVED_QT_LINE_B );
      // Current pending inbound shipment has one line for part A with quantity (10-1=)9
      ShipmentKey lPendInboundShipment =
            createInboundShipment( lExhangeOrder, RefEventStatusKey.IXPEND ).build();
      double lNewExpectedQtLineA = ORG_QT_LINE_A - HIS_PARTIALLY_RECEIVED_QT_LINE_A;
      ShipmentLineKey lPendShipmentLineA = createShipmentLine( lPendInboundShipment, iBatchPartA,
            iOrderLineA, lNewExpectedQtLineA ).build();

      /** EDIT LINES **/
      // Only increase the quantity of part A line (from 10 to 11)
      EditOrderLineTO lEditOrderLineToA =
            editOrderLineTOSetup( iOrderLineA, ACCOUNT_CD, ORG_QT_LINE_A + 1 );
      EditOrderLineTO lEditOrderLineToB =
            editOrderLineTOSetup( iOrderLineB, ACCOUNT_CD, ORG_QT_LINE_B );

      // call service to update order line
      OrderService.setPOLines( lExhangeOrder,
            new EditOrderLineTO[] { lEditOrderLineToA, lEditOrderLineToB }, iHr );

      /** ASSERTION **/
      // assert that change reason code is MXINCRSE, since quantity increased in the first Batch
      // part A order line
      assertEquals( POLineTable.findByPrimaryKey( iOrderLineA ).getChangeReasonCd(), "MXINCRSE" );
      assertEquals( POLineTable.findByPrimaryKey( iOrderLineB ).getChangeReasonCd(), null );

      // Get all shipment lines of current pending inbound shipment
      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "SHIP_SHIPMENT_LINE",
            lPendInboundShipment.getPKWhereArg() );

      while ( lQs.next() ) {
         // Assert the only one shipment line is still the one for part A, but quantity is increased
         // to (9+1=)10
         assertEquals( "The pending inbound shipment line changed incorrectly", lPendShipmentLineA,
               new ShipmentLineKey( lQs.getInt( "shipment_line_db_id" ),
                     lQs.getInt( "shipment_line_id" ) ) );
         assertEquals(
               "The pending inbound shipment line's expected quantity for part A changed incorrectly",
               lNewExpectedQtLineA + 1, lQs.getDouble( "expect_qt" ), 0 );

      }
      assertFalse( lQs.next() );
   }


   /**
    * Pre-condition: A purchase order with two order lines: One is for Batch part A with expected
    * quantity 10; another one is for SER part B with expected quantity 2.
    *
    * There is one complete historic inbound shipment received 9 of part A and 1 of part B. So now
    * the pending inbound shipment has one line for part A with expected quantity (10-9=)1 and one
    * line for part B with expected quantity 1
    *
    * When Edit Lines to increase the quantity of SER part B line to (2+1=)3.
    *
    * Assert: The two pending inbound shipment lines keep the same quantity. And a new inbound
    * shipment line is added for Ser part B.
    *
    * @param aOrderStatus
    *           the order status
    *
    * @throws MxException
    *            if any exception occurs
    * @throws Exception
    *            if any exception occurs
    */
   private void testNewInboundShipLineCreatedForSerOrderLineWhenIncreaseSerQty(
         RefEventStatusKey aOrderStatus ) throws MxException, Exception {

      /** DATA SETUP **/
      // A purchase order with two lines, one has been received 9/10, another one has been received
      // 1/2
      PurchaseOrderKey lPurchaseOrder = createOrderWithTwoLinesAndHistoricInboundShipment(
            aOrderStatus, RefPoLineTypeKey.PURCHASE, iSerPart, ORG_QT_LINE_B_SER, ONE_FOR_SER,
            ONE_FOR_SER );
      // Current pending inbound shipment has one line for part A with quantity (10-9=)1 and one
      // line for part B with expected quantity 1 (expected quantity is always 1 for SER part
      // shipment line)
      ShipmentKey lPendInboundShipment =
            createInboundShipment( lPurchaseOrder, RefEventStatusKey.IXPEND ).build();
      double lNewExpectedQtLineA = ORG_QT_LINE_A - HIS_PARTIALLY_RECEIVED_QT_LINE_A;
      ShipmentLineKey lPendShipmentLineA = createShipmentLine( lPendInboundShipment, iBatchPartA,
            iOrderLineA, lNewExpectedQtLineA ).build();
      ShipmentLineKey lPendShipmentLineB =
            createShipmentLine( lPendInboundShipment, iSerPart, iOrderLineB, ONE_FOR_SER ).build();

      /** EDIT LINES **/
      // Only increase second Ser line order quantity + 1
      EditOrderLineTO lEditOrderLineToA =
            editOrderLineTOSetup( iOrderLineA, ACCOUNT_CD, ORG_QT_LINE_A );
      EditOrderLineTO lEditOrderLineToB =
            editOrderLineTOSetup( iOrderLineB, ACCOUNT_CD, ORG_QT_LINE_B_SER + 1 );

      // call service to update order line
      OrderService.setPOLines( lPurchaseOrder,
            new EditOrderLineTO[] { lEditOrderLineToA, lEditOrderLineToB }, iHr );

      /** ASSERTION **/
      // assert that change reason code is MXINCRSE, since quantity increased in the second Ser part
      // B order line
      assertEquals( POLineTable.findByPrimaryKey( iOrderLineA ).getChangeReasonCd(), null );
      assertEquals( POLineTable.findByPrimaryKey( iOrderLineB ).getChangeReasonCd(), "MXINCRSE" );

      // Get all shipment lines of current pending inbound shipment
      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "SHIP_SHIPMENT_LINE",
            lPendInboundShipment.getPKWhereArg() );

      // Assert there are three shipment lines
      assertEquals( 3, lQs.getRowCount() );
      while ( lQs.next() ) {
         // Assert one of the shipment lines is the one for Batch part A with quantity (original 10
         // - received 1=)9, same as before editing
         if ( lPendShipmentLineA.equals( new ShipmentLineKey( lQs.getInt( "shipment_line_db_id" ),
               lQs.getInt( "shipment_line_id" ) ) ) ) {
            assertEquals(
                  "The pending inbound shipment line's expected quantity for Batch part A changed incorrectly",
                  lNewExpectedQtLineA, lQs.getDouble( "expect_qt" ), 0 );
         } else if ( lPendShipmentLineB.equals( new ShipmentLineKey(
               lQs.getInt( "shipment_line_db_id" ), lQs.getInt( "shipment_line_id" ) ) ) ) {
            // Assert another one of the shipment lines is the one for Ser part B with quantity 1,
            // same as before editing
            assertEquals(
                  "The pending inbound shipment line's expected quantity for Ser part B is wrong",
                  ONE_FOR_SER, lQs.getDouble( "expect_qt" ), 0 );
         } else {
            // Assert a new inbound shipment line is created for part B with quantity 1
            assertEquals( "The new inbound shipment line is not for Ser part B, which is wrong",
                  iSerPart, lQs.getKey( PartNoKey.class, "part_no_db_id", "part_no_id" ) );
            assertEquals(
                  "The new pending inbound shipment line's expected quantity for Ser part B is wrong",
                  ONE_FOR_SER, lQs.getDouble( "expect_qt" ), 0 );
         }
      }
   }


   /**
    * Pre-condition: An exchange order with two order lines: One is for batch part A with expected
    * quantity 10; another one is for batch part B with expected quantity 3.
    *
    * There is one complete historic inbound shipment received 1 of part A and 4 of part B. So now
    * the pending inbound shipment just has one line for part A with expected quantity (10-1=)9.
    *
    * When Edit Lines to increase the quantity of part A order line to (10-1=)9.
    *
    * Assert: There is still only one same inbound shipment line for part A, but the expected
    * quantity is increased to (9-1=)8
    *
    * @param aOrderStatus
    *           the order status
    *
    * @throws MxException
    *            if any exception occurs
    * @throws Exception
    *            if any exception occurs
    */
   private void testInboundShipLineQtyDecreasedWhenDecreaseBatchOrderLineQty(
         RefEventStatusKey aOrderStatus ) throws MxException, Exception {

      /** DATA SETUP **/
      // An exchange order with two lines, one has been received 1/10, another one has been received
      // 4/3
      PurchaseOrderKey lExhangeOrder = createOrderWithTwoLinesAndHistoricInboundShipment(
            aOrderStatus, RefPoLineTypeKey.EXCHANGE, iBatchPartB, ORG_QT_LINE_B, ORG_QT_LINE_B,
            HIS_OVER_RECEIVED_QT_LINE_B );
      // Current pending inbound shipment has one line for part A with quantity (10-1=)9
      ShipmentKey lPendInboundShipment =
            createInboundShipment( lExhangeOrder, RefEventStatusKey.IXPEND ).build();
      double lNewExpectedQtLineA = ORG_QT_LINE_A - HIS_PARTIALLY_RECEIVED_QT_LINE_A;
      ShipmentLineKey lPendShipmentLineA = createShipmentLine( lPendInboundShipment, iBatchPartA,
            iOrderLineA, lNewExpectedQtLineA ).build();

      /** EDIT LINES **/
      // Only decrease the quantity of part A line (from 10 to 9)
      EditOrderLineTO lEditOrderLineToA =
            editOrderLineTOSetup( iOrderLineA, ACCOUNT_CD, ORG_QT_LINE_A - 1 );
      EditOrderLineTO lEditOrderLineToB =
            editOrderLineTOSetup( iOrderLineB, ACCOUNT_CD, ORG_QT_LINE_B );

      // call service to update order line
      OrderService.setPOLines( lExhangeOrder,
            new EditOrderLineTO[] { lEditOrderLineToA, lEditOrderLineToB }, iHr );

      /** ASSERTION **/
      // assert that change reason code is MXDCRSE, since quantity decreased in the first Batch part
      // A order line
      assertEquals( POLineTable.findByPrimaryKey( iOrderLineA ).getChangeReasonCd(), "MXDCRSE" );
      assertEquals( POLineTable.findByPrimaryKey( iOrderLineB ).getChangeReasonCd(), null );

      // Get all shipment lines of current pending inbound shipment
      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "SHIP_SHIPMENT_LINE",
            lPendInboundShipment.getPKWhereArg() );

      while ( lQs.next() ) {
         // Assert the only one shipment line is still the one for part A, but quantity is decreased
         // to (9-1=)8
         assertEquals( "The pending inbound shipment line changed incorrectly", lPendShipmentLineA,
               new ShipmentLineKey( lQs.getInt( "shipment_line_db_id" ),
                     lQs.getInt( "shipment_line_id" ) ) );
         assertEquals(
               "The pending inbound shipment line's expected quantity for part A changed incorrectly",
               lNewExpectedQtLineA - 1, lQs.getDouble( "expect_qt" ), 0 );
      }
      assertFalse( lQs.next() );
   }


   /**
    * Pre-condition: A purchase order with two order lines: One is for Batch part A with expected
    * quantity 10; another one is for SER part B with expected quantity 2.
    *
    * There is one complete historic inbound shipment received 9 of part A and 1 of part B. So now
    * the pending inbound shipment has one line for part A with expected quantity (10-9=)1 and one
    * line for part B with expected quantity 1
    *
    * When Edit Lines to increase the quantity of SER part B line to (2-1=)1.
    *
    * Assert: The pending inbound shipment line for Batch part A has no change, keeps the same
    * quantity. But the inbound shipment line for Ser part B is removed.
    *
    * @param aOrderStatus
    *           the order status
    *
    * @throws MxException
    *            if any exception occurs
    * @throws Exception
    *            if any exception occurs
    */
   private void testInboundShipLineRemovedForSerOrderLineWhenDecreaseSerQty(
         RefEventStatusKey aOrderStatus ) throws MxException, Exception {

      /** DATA SETUP **/
      // A purchase order with two lines, one has been received 9/10, another one has been received
      // 1/2
      PurchaseOrderKey lPurchaseOrder = createOrderWithTwoLinesAndHistoricInboundShipment(
            aOrderStatus, RefPoLineTypeKey.PURCHASE, iSerPart, ORG_QT_LINE_B_SER, ONE_FOR_SER,
            ONE_FOR_SER );
      // Current pending inbound shipment has one line for part A with quantity (10-9=)1 and one
      // line for part B with expected quantity 1 (expected quantity is always 1 for SER part
      // shipment line)
      ShipmentKey lPendInboundShipment =
            createInboundShipment( lPurchaseOrder, RefEventStatusKey.IXPEND ).build();
      double lNewExpectedQtLineA = ORG_QT_LINE_A - HIS_PARTIALLY_RECEIVED_QT_LINE_A;
      ShipmentLineKey lPendShipmentLineA = createShipmentLine( lPendInboundShipment, iBatchPartA,
            iOrderLineA, lNewExpectedQtLineA ).build();
      createShipmentLine( lPendInboundShipment, iSerPart, iOrderLineB, ONE_FOR_SER ).build();

      /** EDIT LINES **/
      // Only decrease Ser line order quantity - 1
      EditOrderLineTO lEditOrderLineToA =
            editOrderLineTOSetup( iOrderLineA, ACCOUNT_CD, ORG_QT_LINE_A );
      EditOrderLineTO lEditOrderLineToB =
            editOrderLineTOSetup( iOrderLineB, ACCOUNT_CD, ORG_QT_LINE_B_SER - 1 );

      // call service to update order line
      OrderService.setPOLines( lPurchaseOrder,
            new EditOrderLineTO[] { lEditOrderLineToA, lEditOrderLineToB }, iHr );

      /** ASSERTION **/
      // assert that change reason code is MXDCRSE, since quantity decreased in the second Ser part
      // B order line
      assertEquals( POLineTable.findByPrimaryKey( iOrderLineA ).getChangeReasonCd(), null );
      assertEquals( POLineTable.findByPrimaryKey( iOrderLineB ).getChangeReasonCd(), "MXDCRSE" );

      // Get all shipment lines of current pending inbound shipment
      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "SHIP_SHIPMENT_LINE",
            lPendInboundShipment.getPKWhereArg() );

      while ( lQs.next() ) {
         // Assert there is only one shipment line and it is the one for Batch part A with quantity
         // (original 10 - received 1=)9, same as before editing
         assertEquals( "The pending inbound shipment line changed incorrectly", lPendShipmentLineA,
               new ShipmentLineKey( lQs.getInt( "shipment_line_db_id" ),
                     lQs.getInt( "shipment_line_id" ) ) );
         assertEquals(
               "The pending inbound shipment line's expected quantity for Batch part A changed incorrectly",
               lNewExpectedQtLineA, lQs.getDouble( "expect_qt" ), 0 );
      }
      assertFalse( lQs.next() );
   }


   /**
    * Create an order with two order lines, and one has a partially received complete inbound
    * shipment history, another one has an over received complete inbound shipment history.
    *
    * @param aOrderStatus
    *           the order status
    * @param aRefPoLineTypeKey
    *           the order line type
    * @param aPart
    *           the part for line B
    * @param aOrderExpectedQt
    *           the expected quantity for order line B
    * @param aShipmentLineExpectedQt
    *           the expected quantity for shipment line
    * @param aReceivedQt
    *           the received quantity for shipment line
    *
    * @return the order
    */

   private PurchaseOrderKey createOrderWithTwoLinesAndHistoricInboundShipment(
         RefEventStatusKey aOrderStatus, RefPoLineTypeKey aRefPoLineTypeKey, PartNoKey aPart,
         double aOrderExpectedQt, double aShipmentLineExpectedQt, double aReceivedQt ) {

      PurchaseOrderKey lOrder = createOrderBuilder( aOrderStatus ).build();

      // two order lines (PreInspQty = received quantity, this PreInspQty is set when
      // ShipmentLineService.receiveShipmentLine())
      iOrderLineA = createOrderLineBuilder( lOrder, iBatchPartA, aRefPoLineTypeKey,
            new BigDecimal( ORG_QT_LINE_A ) )
                  .withPreInspQty( new BigDecimal( HIS_PARTIALLY_RECEIVED_QT_LINE_A ) ).build();
      iOrderLineB = createOrderLineBuilder( lOrder, aPart, aRefPoLineTypeKey,
            new BigDecimal( aOrderExpectedQt ) ).withPreInspQty( new BigDecimal( aReceivedQt ) )
                  .build();

      // one historic complete inbound shipment
      ShipmentKey lCmpltShipment =
            createInboundShipment( lOrder, RefEventStatusKey.IXCMPLT ).withHistoric( true ).build();
      // received 1/10 part A
      createShipmentLine( lCmpltShipment, iBatchPartA, iOrderLineA, ORG_QT_LINE_A )
            .withReceivedQuantity( HIS_PARTIALLY_RECEIVED_QT_LINE_A ).build();
      // over or fully received part B
      createShipmentLine( lCmpltShipment, aPart, iOrderLineB, aShipmentLineExpectedQt )
            .withReceivedQuantity( aReceivedQt ).build();

      return lOrder;
   }


   /**
    * Create a order builder.
    *
    * @param aOrderStatus
    *           The order status
    *
    * @return OrderBuilder the order builder
    */
   private OrderBuilder createOrderBuilder( RefEventStatusKey aOrderStatus ) {
      return new OrderBuilder().withStatus( aOrderStatus ).withVendor( iVendor )
            .shippingTo( iDockLocation ).withIssueDate( new Date() );
   }


   /**
    * Create a part builder with the given class.
    *
    * @param aInventoryClass
    *           the inventory class
    *
    * @return PartNoBuilder the part no builder
    */
   private PartNoBuilder createPartBuilder( RefInvClassKey aInventoryClass ) {
      return new PartNoBuilder().withInventoryClass( aInventoryClass );

   }


   /**
    * Create a order line builder.
    *
    * @param aOrder
    *           the Order
    * @param aPartNo
    *           the Part
    * @param aPoLineType
    *           the Order line type
    * @param aOrderQty
    *           The order quantity
    *
    * @return OrderLineBuilder the order line builder
    */
   private OrderLineBuilder createOrderLineBuilder( PurchaseOrderKey aOrder, PartNoKey aPartNo,
         RefPoLineTypeKey aPoLineType, BigDecimal aOrderQty ) {
      return new OrderLineBuilder( aOrder ).withUnitType( RefQtyUnitKey.EA )
            .withUnitPrice( BigDecimal.TEN ).withLineType( aPoLineType )
            .withOrderQuantity( aOrderQty ).promisedBy( new Date() ).forPart( aPartNo )
            .withAccount( iAccount );
   }


   /**
    * Create an inbound shipment.
    *
    * @param aOrder
    *           the Order
    * @param aOrderStatus
    *           the shipment status
    *
    * @return ShipmentKey the shipment key
    */
   private ShipmentDomainBuilder createInboundShipment( PurchaseOrderKey aOrder,
         RefEventStatusKey aOrderStatus ) {

      return createInboundShipment( aOrder, aOrderStatus, iVendorLocation, iDockLocation );
   }


   /**
    * Create an inbound shipment.
    *
    * @param aOrder
    *           the Order
    * @param aOrderStatus
    *           the shipment status
    *
    * @return ShipmentKey the shipment key
    */
   private ShipmentDomainBuilder createInboundShipment( PurchaseOrderKey aOrder,
         RefEventStatusKey aOrderStatus, LocationKey aShipFromLocation,
         LocationKey aShipToLocation ) {
      return new ShipmentDomainBuilder().withOrder( aOrder ).withStatus( aOrderStatus )
            .fromLocation( aShipFromLocation ).toLocation( aShipToLocation )
            .withType( RefShipmentTypeKey.PURCHASE );
   }


   /**
    * Create an outbound shipment.
    *
    * @param aOrder
    *           the Order
    * @param aOrderStatus
    *           the shipment status
    *
    * @return ShipmentKey the shipment key
    */
   private ShipmentDomainBuilder createOutboundShipment( PurchaseOrderKey aOrder,
         RefEventStatusKey aOrderStatus ) {
      return new ShipmentDomainBuilder().withOrder( aOrder ).withStatus( aOrderStatus )
            .fromLocation( iDockLocation ).toLocation( iVendorLocation );
   }


   /**
    * Create a shipment line.
    *
    * @param aShipment
    *           the shipment
    * @param aPart
    *           the part
    * @param aOrderLine
    *           the order line
    * @param aExpectedQuantity
    *           the expected quantity
    *
    * @return ShipmentLineKey the shipment line key
    */
   private ShipmentLineBuilder createShipmentLine( ShipmentKey aShipment, PartNoKey aPart,
         PurchaseOrderLineKey aOrderLine, double aExpectedQuantity ) {
      return new ShipmentLineBuilder( aShipment ).forPart( aPart ).forOrderLine( aOrderLine )
            .withExpectedQuantity( aExpectedQuantity );
   }


   /**
    * create the shipment routing legs from given locations.
    *
    * @param aShipment
    *           the shipment key
    * @param aVendorLocation
    *           the vendor location
    * @param aShippingLocation
    *           the shipping to location for first leg
    * @param aReExpediteLocation
    *           the re-expedite location for final leg
    */
   private void createShipmentRoutingLegs( ShipmentKey aShipment, LocationKey aVendorLocation,
         LocationKey aShippingLocation, LocationKey aReExpediteLocation ) {
      // create first segment from vendor location to shipping location
      ShipSegmentKey lShipSegment1 = new ShipSegmentBuilder().fromLocation( aVendorLocation )
            .toLocation( aShippingLocation ).withStatus( RefShipSegmentStatusKey.COMPLETE ).build();

      // create second segment from shipping location to reexpedite location
      ShipSegmentKey lShipSegment2 = new ShipSegmentBuilder().fromLocation( aShippingLocation )
            .toLocation( aReExpediteLocation ).withStatus( RefShipSegmentStatusKey.PENDING )
            .build();

      // create a map to link segments to the given shipment
      new ShipSegmentMapBuilder().withShipment( aShipment ).withSegment( lShipSegment1 )
            .withOrder( 1 ).build();

      new ShipSegmentMapBuilder().withShipment( aShipment ).withSegment( lShipSegment2 )
            .withOrder( 2 ).build();
   }


   /**
    * Prepare the EditOrderLineTO.
    *
    *
    * @param aOrderLine
    *           the order line key
    * @param aAccountCode
    *           the account code
    * @param aQuantity
    *           the received quantity
    *
    * @return EditOrderLineTO the transfer objects
    *
    * @throws MandatoryArgumentException
    * @throws NegativeValueException
    */
   private EditOrderLineTO editOrderLineTOSetup( PurchaseOrderLineKey aOrderLine,
         String aAccountCode, double aQuantity )
         throws MandatoryArgumentException, NegativeValueException {

      // build po line tax
      PoLineTax lPoLineTax = PoLineTax.create( aOrderLine, iTaxKey );
      lPoLineTax.insert();

      // update order line - quantity decreased
      POLineTable lPOLineTable = POLineTable.findByPrimaryKey( aOrderLine );

      // build TO
      EditOrderLineTO lEditOrderLineTO = new EditOrderLineTO( aOrderLine );
      lEditOrderLineTO.setUnitPrice( lPOLineTable.getUnitPrice(), "unit price" );
      lEditOrderLineTO.setQuantityUnit( lPOLineTable.getQtyUnit(), "unit type" );
      lEditOrderLineTO.setTax( iTaxKey, BigDecimal.ZERO, "line country tax" );
      lEditOrderLineTO.setAccountCode( aAccountCode, "account code" );
      lEditOrderLineTO.setQuantity( aQuantity, "order_quantity" );

      return lEditOrderLineTO;

   }


   @Before
   public void loadData() throws Exception {

      // create a HR
      iHr = new HumanResourceDomainBuilder().withUsername( USERNAME_TESTUSER )
            .withUserId( USERID_TESTUSER ).build();

      SecurityIdentificationUtils.setInstance( new SecurityIdentificationStub( iHr ) );

      // create two batch parts
      iBatchPartA = createPartBuilder( RefInvClassKey.BATCH ).build();
      iBatchPartB = createPartBuilder( RefInvClassKey.BATCH ).build();
      iSerPart = createPartBuilder( RefInvClassKey.SER ).build();

      // create a dock location
      iDockLocation =
            new LocationDomainBuilder().withCode( "LOC1/DOCK" ).withType( RefLocTypeKey.DOCK ).build();

      // create second dock location
      iReExpediteLocation =
            new LocationDomainBuilder().withCode( "LOC2/DOCK" ).withType( RefLocTypeKey.DOCK ).build();

      // create a vendor location
      iVendorLocation =
            new LocationDomainBuilder().withCode( "VENDOR1" ).withType( RefLocTypeKey.VENDOR ).build();

      // build finance account
      iAccount = new AccountBuilder().withCode( ACCOUNT_CD ).isDefault().build();

      // build alternate finance account
      new AccountBuilder().withCode( ALT_ACCOUNT_CD ).isDefault().build();

      // build org vendor
      iVendor = new VendorBuilder().withCode( "TESTVENDOR" ).build();

      // build tax
      TaxTable lTaxTable = TaxTable.create();
      lTaxTable.setTaxRate( new BigDecimal( 5.0 ) );
      iTaxKey = lTaxTable.insert();

   }
}
