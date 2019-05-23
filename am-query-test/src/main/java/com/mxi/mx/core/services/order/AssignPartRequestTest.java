
package com.mxi.mx.core.services.order;

import static org.junit.Assert.assertEquals;

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
import com.mxi.am.domain.builder.PartGroupDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.PartRequestBuilder;
import com.mxi.am.domain.builder.ShipmentDomainBuilder;
import com.mxi.am.domain.builder.ShipmentLineBuilder;
import com.mxi.am.domain.builder.VendorBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.RefAccountTypeKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefFinanceTypeKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefPoLineTypeKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefReqTypeKey;
import com.mxi.mx.core.key.RefShipmentTypeKey;
import com.mxi.mx.core.key.ShipmentKey;
import com.mxi.mx.core.key.VendorKey;
import com.mxi.mx.core.services.order.exception.OrderIsHistoricException;
import com.mxi.mx.core.services.order.exception.OrderIsReceivedException;
import com.mxi.mx.core.services.order.exception.PurchaseRequestHasPOException;
import com.mxi.mx.core.services.req.InsufficientOnOrderQuantityException;
import com.mxi.mx.core.services.req.InvalidPartRequestStatusException;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.req.ReqPartTable;


/**
 * This class test the OrderService.assignPartRequest method
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class AssignPartRequestTest {

   private FncAccountKey iAccount;
   private LocationKey iDockLocation;
   private LocationKey iVendorLocation;
   private HumanResourceKey iHr;
   private PartNoKey iPartNoKey;
   private PartNoKey iAlternatePartNoKey;
   private LocationKey iSupplyLocation;
   private VendorKey iVendor;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * This test the case for assigning part request when there is an available on order item to
    * reserve
    *
    * @throws Exception
    *            if any error occurs
    */
   @Test
   public void testAssignPartRequestAvailable() throws Exception {

      // create an issued PO
      PurchaseOrderKey lOrder = createIssuedPurchaseOrder( 2.0 );

      // create a part request for less than the ordered quantity
      PartRequestKey lPartRequest = createPartRequest( 1.0 );

      // assign part request to PO
      new OrderService().assignPartRequest( lOrder, new PartRequestKey[] { lPartRequest }, iAccount,
            iHr );

      // assert part request status is on order
      assertEquals( RefEventStatusKey.PRONORDER,
            EvtEventTable.findByPrimaryKey( lPartRequest.getEventKey() ).getEventStatusCd() );
   }


   /**
    * This tests the case where an on order alternate inventory is available to reserve
    *
    * @throws Exception
    */
   @Test
   public void testAssignPartRequestAvailableAlternate() throws Exception {

      // create an issued PO
      PurchaseOrderKey lOrder = createIssuedPurchaseOrder( 2.0 );

      // create part request on alternate part
      PartRequestKey lPartRequest = new PartRequestBuilder().withStatus( RefEventStatusKey.PROPEN )
            .forPurchasePart( iAlternatePartNoKey ).forSpecifiedPart( iAlternatePartNoKey )
            .withType( RefReqTypeKey.TASK ).withRequestedQuantity( 1.0 )
            .isNeededAt( iSupplyLocation ).build();

      // assign part request to PO
      new OrderService().assignPartRequest( lOrder, new PartRequestKey[] { lPartRequest }, iAccount,
            iHr );

      // assert part request status is on order
      assertEquals( RefEventStatusKey.PRONORDER,
            EvtEventTable.findByPrimaryKey( lPartRequest.getEventKey() ).getEventStatusCd() );
   }


   /**
    *
    * Testing that a part request cannot be assigned to an historic PO.
    *
    * @throws Exception
    */
   @Test( expected = OrderIsHistoricException.class )
   public void testAssignPartRequestToHistoricPO() throws Exception {

      // create an closed PO
      PurchaseOrderKey lOrder =
            new OrderBuilder().withStatus( RefEventStatusKey.POCLOSED ).withVendor( iVendor )
                  .asHistoric().withIssueDate( new Date() ).shippingTo( iDockLocation ).build();
      new OrderLineBuilder( lOrder ).withUnitType( RefQtyUnitKey.EA )
            .withUnitPrice( BigDecimal.TEN ).withLineType( RefPoLineTypeKey.PURCHASE )
            .withOrderQuantity( new BigDecimal( 10.0 ) ).promisedBy( new Date() )
            .forPart( iPartNoKey ).withAccount( iAccount ).build();

      PartRequestKey lPartRequest = createPartRequest( 1.0 );

      // assign part request to PO
      new OrderService().assignPartRequest( lOrder, new PartRequestKey[] { lPartRequest }, iAccount,
            iHr );
      // should throw OrderIsHistoricException
   }


   /**
    *
    * Testing that a part request cannot be assigned to a received PO.
    *
    * @throws Exception
    */
   @Test( expected = OrderIsReceivedException.class )
   public void testAssignPartRequestToReceivedPO() throws Exception {

      // create an received PO
      PurchaseOrderKey lOrder = new OrderBuilder().withStatus( RefEventStatusKey.PORECEIVED )
            .withVendor( iVendor ).withIssueDate( new Date() ).shippingTo( iDockLocation ).build();
      new OrderLineBuilder( lOrder ).withUnitType( RefQtyUnitKey.EA )
            .withUnitPrice( BigDecimal.TEN ).withLineType( RefPoLineTypeKey.PURCHASE )
            .withOrderQuantity( new BigDecimal( 10.0 ) ).promisedBy( new Date() )
            .forPart( iPartNoKey ).withAccount( iAccount ).build();

      PartRequestKey lPartRequest = createPartRequest( 1.0 );

      // assign part request to PO
      new OrderService().assignPartRequest( lOrder, new PartRequestKey[] { lPartRequest }, iAccount,
            iHr );
      // should throw OrderIsReceivedException
   }


   /**
    * This test the case when an on order inventory is already reserved for another part request
    *
    * @throws Exception
    *            if any error occurs
    */
   @Test( expected = InsufficientOnOrderQuantityException.class )
   public void testAssignPartRequestNotAvailable() throws Exception {

      // create an issued PO
      PurchaseOrderKey lOrder = createIssuedPurchaseOrder( 2.0 );

      // create a part request for more than the ordered quantity
      PartRequestKey lPartRequestA = createPartRequest( 3.0 );

      // assert PO is issued
      assertEquals( RefEventStatusKey.POISSUED,
            EvtEventTable.findByPrimaryKey( lOrder.getEventKey() ).getEventStatusCd() );

      // assign part request to PO
      new OrderService().assignPartRequest( lOrder, new PartRequestKey[] { lPartRequestA },
            iAccount, iHr );
      // should throw InsufficientOnOrderQuantityException

   }


   /**
    * This test "stealing" on ordered inventory from a stock request by calling unassigning and
    * assigning like a user will have to do in real life
    *
    * @throws Exception
    *            if any error occurs
    */
   @Test
   public void testStealFromStockRequest() throws Exception {

      // create an issued PO
      PurchaseOrderKey lOrder = createIssuedPurchaseOrder( 2.0 );

      PartRequestKey lStockRequest = new PartRequestBuilder().withStatus( RefEventStatusKey.PROPEN )
            .forPurchasePart( iPartNoKey ).forSpecifiedPart( iPartNoKey )
            .withType( RefReqTypeKey.STOCK ).withRequestedQuantity( 2.0 )
            .isNeededAt( iSupplyLocation ).build();

      // associate stock request with PO
      new OrderService().assignPartRequest( lOrder, new PartRequestKey[] { lStockRequest },
            iAccount, iHr );

      assertEquals( RefEventStatusKey.PRONORDER,
            EvtEventTable.findByPrimaryKey( lStockRequest.getEventKey() ).getEventStatusCd() );

      // create new task part request
      PartRequestKey lPartRequest = createPartRequest( 1.0 );

      // unassign stock part request from PO
      new OrderService().unassignPartRequest( lOrder, new PartRequestKey[] { lStockRequest }, iHr );

      // assert stock part request status
      assertEquals( RefEventStatusKey.PROPEN,
            EvtEventTable.findByPrimaryKey( lStockRequest.getEventKey() ).getEventStatusCd() );

      // assign part request
      new OrderService().assignPartRequest( lOrder, new PartRequestKey[] { lPartRequest }, iAccount,
            iHr );

      assertEquals( RefEventStatusKey.PRONORDER,
            EvtEventTable.findByPrimaryKey( lPartRequest.getEventKey() ).getEventStatusCd() );

   }


   /**
    *
    * Testing that you cannot assign the same part request twice to a PO.
    *
    * @throws Exception
    */
   @Test( expected = PurchaseRequestHasPOException.class )
   public void testCannotAssignPartRequestToPOTwice() throws Exception {

      // create an issued PO
      PurchaseOrderKey lOrder = createIssuedPurchaseOrder( 2.0 );

      // create a part request
      PartRequestKey lPartRequest = new PartRequestBuilder().withStatus( RefEventStatusKey.PROPEN )
            .forPurchasePart( iPartNoKey ).forSpecifiedPart( iPartNoKey )
            .withType( RefReqTypeKey.TASK ).withRequestedQuantity( 2.0 )
            .isNeededAt( iSupplyLocation ).build();

      // assign the part request to PO
      new OrderService().assignPartRequest( lOrder, new PartRequestKey[] { lPartRequest }, iAccount,
            iHr );

      assertEquals( RefEventStatusKey.PRONORDER,
            EvtEventTable.findByPrimaryKey( lPartRequest.getEventKey() ).getEventStatusCd() );

      // try to assign the part request to PO for a second time
      new OrderService().assignPartRequest( lOrder, new PartRequestKey[] { lPartRequest }, iAccount,
            iHr );
      // should throw PurchaseRequestHasPOException
   }


   /**
    *
    * Testing that you cannot assign a part request with an invalid status to a PO.
    *
    * @throws Exception
    */
   @Test( expected = InvalidPartRequestStatusException.class )
   public void testCannotAssignInvalidStatusPartRequestToPO() throws Exception {

      // create an issued PO
      PurchaseOrderKey lOrder = createIssuedPurchaseOrder( 10.0 );

      // create a part request with an invalid status (in this case, PRONHAND)
      PartRequestKey lPartRequest = new PartRequestBuilder()
            .withStatus( RefEventStatusKey.PRONHAND ).forPurchasePart( iPartNoKey )
            .forSpecifiedPart( iPartNoKey ).withType( RefReqTypeKey.TASK )
            .withRequestedQuantity( 2.0 ).isNeededAt( iSupplyLocation ).build();

      // assign part request to PO
      new OrderService().assignPartRequest( lOrder, new PartRequestKey[] { lPartRequest }, iAccount,
            iHr );
      // should throw InvalidPartRequestStatusException
   }


   /**
    *
    * Testing that you can assign a part request to a partially received PO.
    *
    * @throws Exception
    */
   @Test
   public void testCanAssignPartRequestToPartiallyReceivedPO() throws Exception {

      double lOrderedQty = 10.0;
      double lReceivedQty = 7.0;

      // create a partially received PO
      PurchaseOrderKey lOrder = createPartiallyReceivedPurchaseOrder( lOrderedQty, lReceivedQty );

      // create a part request for exactly the on-order amount of the PO after reception
      double lRequestedQty = lOrderedQty - lReceivedQty;
      PartRequestKey lPartRequest = createPartRequest( lRequestedQty );

      // assign part request to PO
      new OrderService().assignPartRequest( lOrder, new PartRequestKey[] { lPartRequest }, iAccount,
            iHr );

      // check post conditions
      assertEquals( RefEventStatusKey.PRONORDER,
            EvtEventTable.findByPrimaryKey( lPartRequest.getEventKey() ).getEventStatusCd() );
      assertEquals( lOrder, ReqPartTable.findByPrimaryKey( lPartRequest ).getPurchaseOrder() );
   }


   /**
    *
    * Testing that you cannot assign a part request to a partially received PO when there is
    * insufficient available on-order quantity on the PO. Given a part request with requested
    * quantity greater than the amount of on-order quantity on a PO after partially receiving, the
    * part request should not be able to reserve the insufficient on-order quantity.
    *
    * @throws Exception
    */
   @Test( expected = InsufficientOnOrderQuantityException.class )
   public void testCannotAssignPartRequestToPartiallyReceivedPOWithInsufficientQuantity()
         throws Exception {

      double lOrderedQty = 10.0;
      double lReceivedQty = 7.0;

      // create a partially received PO
      PurchaseOrderKey lOrder = createPartiallyReceivedPurchaseOrder( lOrderedQty, lReceivedQty );

      // create a part request for more than the on-order amount of the PO after reception
      double lRequestedQty = ( lOrderedQty - lReceivedQty ) + 1.0;
      PartRequestKey lPartRequest = createPartRequest( lRequestedQty );

      // assign part request to PO
      new OrderService().assignPartRequest( lOrder, new PartRequestKey[] { lPartRequest }, iAccount,
            iHr );
   }


   /**
    *
    * Testing that you cannot assign a part request to a partially received PO when there is
    * insufficient available (not reserved) on-order quantity on the PO. Given a part request with
    * requested quantity greater than the amount of on-order quantity on a PO after partially
    * receiving and having another part request reserve some of the on-order quantity, the part
    * request should not be able to reserve the insufficient on-order quantity.
    *
    * @throws Exception
    */
   @Test( expected = InsufficientOnOrderQuantityException.class )
   public void
         testCannotAssignPartRequestToPartiallyReceivedPOWithInsufficientQuantityDueToReservedQuantity()
               throws Exception {

      double lOrderedQty = 10.0;
      double lReceivedQty = 5.0;
      double lRequestedQtyPR1 = 3.0;

      // create a partially received PO
      PurchaseOrderKey lOrder = createPartiallyReceivedPurchaseOrder( lOrderedQty, lReceivedQty );

      // create a part request
      PartRequestKey lPartRequest1 = createPartRequest( lRequestedQtyPR1 );

      // assign part request to PO
      new OrderService().assignPartRequest( lOrder, new PartRequestKey[] { lPartRequest1 },
            iAccount, iHr );

      // assert the part request is on order
      assertEquals( RefEventStatusKey.PRONORDER,
            EvtEventTable.findByPrimaryKey( lPartRequest1.getEventKey() ).getEventStatusCd() );
      assertEquals( lOrder, ReqPartTable.findByPrimaryKey( lPartRequest1 ).getPurchaseOrder() );

      // create a part request that is requesting more than the available on-order quantity
      // ordered quantity - received amount - quantity reserved by lPartRequest1
      double lRequestedQtyPR2 = ( lOrderedQty - lReceivedQty - lRequestedQtyPR1 ) + 1.0;
      PartRequestKey lPartRequest2 = createPartRequest( lRequestedQtyPR2 );

      // assign part request to PO
      new OrderService().assignPartRequest( lOrder, new PartRequestKey[] { lPartRequest2 },
            iAccount, iHr );
      // should throw InsufficientOnOrderQuantityException
   }


   /**
    * Set up the test case.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Before
   public void loadData() throws Exception {
      iHr = new HumanResourceDomainBuilder().build();

      // create a supply location
      iSupplyLocation =
            new LocationDomainBuilder().withType( RefLocTypeKey.AIRPORT ).isSupplyLocation().build();

      // create a dock at the supply location
      iDockLocation = new LocationDomainBuilder().withType( RefLocTypeKey.DOCK )
            .withSupplyLocation( iSupplyLocation ).build();

      iVendorLocation =
            new LocationDomainBuilder().withType( RefLocTypeKey.VENDOR ).withCode( "TESTVENLOC" ).build();

      // build finance account
      iAccount = new AccountBuilder().withType( RefAccountTypeKey.INVASSET )
            .withCode( "TESTACCOUNT" ).isDefault().build();

      // build org vendor
      iVendor = new VendorBuilder().withCode( "TESTVENDOR" ).build();

      // create bom part
      PartGroupKey lPartGroup = new PartGroupDomainBuilder( "MYGROUP" ).build();

      // build part
      iPartNoKey = new PartNoBuilder().withUnitType( RefQtyUnitKey.EA ).withOemPartNo( "123" )
            .withInventoryClass( RefInvClassKey.BATCH )
            .withFinancialType( RefFinanceTypeKey.CONSUM ).withStatus( RefPartStatusKey.ACTV )
            .withRepairBool( false ).withAssetAccount( iAccount ).isAlternateIn( lPartGroup )
            .build();

      // build alternate part
      iAlternatePartNoKey = new PartNoBuilder().withUnitType( RefQtyUnitKey.EA )
            .withOemPartNo( "123-alt" ).withInventoryClass( RefInvClassKey.BATCH )
            .withFinancialType( RefFinanceTypeKey.CONSUM ).withStatus( RefPartStatusKey.ACTV )
            .withRepairBool( false ).withAssetAccount( iAccount ).isAlternateIn( lPartGroup )
            .build();
   }


   /**
    * Create an open part request with the given requested quantity.
    *
    * @param aRequestedQuantity
    * @return part request key
    */
   private PartRequestKey createPartRequest( double aRequestedQuantity ) {
      return new PartRequestBuilder().withStatus( RefEventStatusKey.PROPEN )
            .forPurchasePart( iPartNoKey ).forSpecifiedPart( iPartNoKey )
            .withType( RefReqTypeKey.TASK ).withRequestedQuantity( aRequestedQuantity )
            .isNeededAt( iSupplyLocation ).build();
   }


   /**
    *
    * Create an issued PO for the ordered quantity.
    *
    * @param aOrderedQuantity
    * @return the order key
    */
   private PurchaseOrderKey createIssuedPurchaseOrder( double aOrderedQuantity ) {

      // create PO with POISSUED status
      PurchaseOrderKey lOrder = new OrderBuilder().withStatus( RefEventStatusKey.POISSUED )
            .withVendor( iVendor ).withIssueDate( new Date() ).shippingTo( iDockLocation ).build();
      PurchaseOrderLineKey lOrderLine =
            new OrderLineBuilder( lOrder ).withUnitType( RefQtyUnitKey.EA )
                  .withUnitPrice( BigDecimal.TEN ).withLineType( RefPoLineTypeKey.PURCHASE )
                  .withOrderQuantity( new BigDecimal( aOrderedQuantity ) ).promisedBy( new Date() )
                  .forPart( iPartNoKey ).withAccount( iAccount ).build();

      // and issuing the PO gives us a pending shipment
      ShipmentKey lPendingInboundShipment =
            new ShipmentDomainBuilder().withOrder( lOrder ).fromLocation( iVendorLocation )
                  .toLocation( iDockLocation ).withType( RefShipmentTypeKey.PURCHASE )
                  .withStatus( RefEventStatusKey.IXPEND ).withHistoric( false ).build();
      new ShipmentLineBuilder( lPendingInboundShipment ).forPart( iPartNoKey )
            .forOrderLine( lOrderLine ).withExpectedQuantity( aOrderedQuantity ).build();

      return lOrder;
   }


   /**
    *
    * Create a partially received PO given the ordered quantity and the received quantity.
    *
    * @param aOrderedQuantity
    * @param aReceivedQuantity
    * @return the order key
    */
   private PurchaseOrderKey createPartiallyReceivedPurchaseOrder( double aOrderedQuantity,
         double aReceivedQuantity ) {

      // create PO with POPARTIAL status
      PurchaseOrderKey lOrder = new OrderBuilder().withStatus( RefEventStatusKey.POPARTIAL )
            .withVendor( iVendor ).withIssueDate( new Date() ).shippingTo( iDockLocation ).build();
      PurchaseOrderLineKey lOrderLine =
            new OrderLineBuilder( lOrder ).withUnitType( RefQtyUnitKey.EA )
                  .withUnitPrice( BigDecimal.TEN ).withLineType( RefPoLineTypeKey.PURCHASE )
                  .withOrderQuantity( new BigDecimal( aOrderedQuantity ) ).promisedBy( new Date() )
                  .forPart( iPartNoKey ).withAccount( iAccount ).build();

      // and receiving some of the PO gives us:
      // one completed shipment (for the received qty)
      ShipmentKey lCompleteShipment =
            new ShipmentDomainBuilder().withOrder( lOrder ).fromLocation( iVendorLocation )
                  .toLocation( iDockLocation ).withType( RefShipmentTypeKey.PURCHASE )
                  .withStatus( RefEventStatusKey.IXCMPLT ).withHistoric( true ).build();
      new ShipmentLineBuilder( lCompleteShipment ).forPart( iPartNoKey ).forOrderLine( lOrderLine )
            .withExpectedQuantity( aOrderedQuantity ).withReceivedQuantity( aReceivedQuantity )
            .build();
      // and one pending shipment (for the ordered qty - received qty)
      ShipmentKey lPendingInboundShipment =
            new ShipmentDomainBuilder().withOrder( lOrder ).fromLocation( iVendorLocation )
                  .toLocation( iDockLocation ).withType( RefShipmentTypeKey.PURCHASE )
                  .withStatus( RefEventStatusKey.IXPEND ).withHistoric( false ).build();
      new ShipmentLineBuilder( lPendingInboundShipment ).forPart( iPartNoKey )
            .forOrderLine( lOrderLine ).withExpectedQuantity( aOrderedQuantity - aReceivedQuantity )
            .build();

      return lOrder;
   }

}
