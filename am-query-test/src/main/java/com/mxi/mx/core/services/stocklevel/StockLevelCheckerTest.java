
package com.mxi.mx.core.services.stocklevel;

import static com.mxi.mx.core.services.inventory.reservation.auto.AbstractAutoReservationTest.getDate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.AccountBuilder;
import com.mxi.am.domain.builder.CurrencyBuilder;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.OrderAuthorizationFlowBuilder;
import com.mxi.am.domain.builder.OrderBuilder;
import com.mxi.am.domain.builder.OrderLineBuilder;
import com.mxi.am.domain.builder.OrgVendorSpec2kCmndBuilder;
import com.mxi.am.domain.builder.OwnerDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.PartRequestBuilder;
import com.mxi.am.domain.builder.PartVendorBuilder;
import com.mxi.am.domain.builder.ShipmentDomainBuilder;
import com.mxi.am.domain.builder.ShipmentLineBuilder;
import com.mxi.am.domain.builder.Spec2kCustBuilder;
import com.mxi.am.domain.builder.StockBuilder;
import com.mxi.am.domain.builder.StockLevelBuilder;
import com.mxi.am.domain.builder.VendorBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.PurchaseOrderAuthKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.RefAccountTypeKey;
import com.mxi.mx.core.key.RefCurrencyKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefFinanceTypeKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefPoAuthLvlStatusKey;
import com.mxi.mx.core.key.RefPoLineTypeKey;
import com.mxi.mx.core.key.RefPoTypeKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefReqTypeKey;
import com.mxi.mx.core.key.RefShipmentTypeKey;
import com.mxi.mx.core.key.RefSpec2kCmndKey;
import com.mxi.mx.core.key.RefSpec2kCustKey;
import com.mxi.mx.core.key.RefStockLowActionKey;
import com.mxi.mx.core.key.RefTermsConditionsKey;
import com.mxi.mx.core.key.RefVendorStatusKey;
import com.mxi.mx.core.key.ShipmentKey;
import com.mxi.mx.core.key.ShipmentLineKey;
import com.mxi.mx.core.key.StockNoKey;
import com.mxi.mx.core.key.VendorKey;
import com.mxi.mx.core.services.shipment.ShipmentService;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.ref.RefQtyUnit;
import com.mxi.mx.core.table.req.ReqPartTable;
import com.mxi.mx.core.unittest.table.evt.EvtEventUtil;
import com.mxi.mx.core.unittest.table.inv.InvInv;
import com.mxi.mx.core.unittest.table.po.PoAuth;
import com.mxi.mx.core.unittest.table.po.PoHeader;
import com.mxi.mx.core.unittest.table.req.ReqPart;
import com.mxi.mx.core.unittest.table.ship.ShipShipment;
import com.mxi.mx.core.unittest.table.ship.ShipShipmentLine;


/**
 * This class tests the StockLevelChecker class. See
 * {@link com.mxi.mx.core.unittest.stock.SKCheckStockLevelsTest} for more tests.
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class StockLevelCheckerTest {

   private static final String SPEC2K_CMND_CD = "S1BOOKED";
   private static final String SPEC2K_CUST_CD = "CTIRE";
   private static final String USERNAME_TESTUSER = "testuser";
   private static final int USERID_TESTUSER = 999;
   private static final String TERMS_AND_CONDITIONS = "T&C";

   private HumanResourceKey iHr;
   private LocationKey iSupplyLocation;
   private LocationKey iSupplierLocation;
   private LocationKey iSupplyDockLocation;
   private LocationKey iSupplierDockLocation;
   private FncAccountKey iAccount;
   private OwnerKey iOwnerKey;
   private VendorKey iVendor;
   private RefQtyUnitKey iInches;
   private RefTermsConditionsKey iVendorTermsConditionsKey;
   private RefSpec2kCustKey iSpec2kCustKey;
   private RefSpec2kCmndKey iRefSpec2kCmndKey;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   /**
    * Tests that stock part requests are properly generated<br>
    * <br>
    * Data Setup<br>
    * <ul>
    * <li>Find or create a stock number for a batch part that does not contain any inventory and has
    * only 1 location with stock level defined</li>
    * <li>Create a batch of 62 at the location where there is a defined stock level</li>
    * <li>Edit the stock levels to be:
    * <ul>
    * <li>Min re-order level = 44</li>
    * <li>Re-order level = 60</li>
    * <li>Max level = 70</li>
    * <li>Reorder quantity = 9</li>
    * <li>Stock low action = POREQ</li>
    * </ul>
    * </ul>
    * Steps
    * <ul>
    * <li>Adjust quantity (-5) from the batch you created in the data setup</li>
    * <li>On the Stock Requests tab of the purchasing to do list, confirm that a stock request was
    * generated with a qty of 9</li>
    * <li>Adjust quantity again (-7)</li>
    * <li>Again on the Stock Requests tab, confirm that a second stock request was generated</li>
    * <li>Select both stock requests on the Stock Requests tab and hit the Create PO For Request
    * button</li>
    * <li>The PO generated should have a total quantity of 18</li>
    * <li>Authorize and Issue the PO Adjust quantity on the inventory again (-10)</li>
    * <li>Check that stock request is created</li>
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testPOREQStockLowAction_NotEnoughStockInventoryAtLocation() throws Exception {

      StockNoKey lStockNo = new StockBuilder().withInvClass( RefInvClassKey.BATCH ).build();

      new StockLevelBuilder( iSupplyLocation, lStockNo, iOwnerKey ).withMinReorderLevel( 44.0 )
            .withReorderQt( 60.0 ).withMaxLevel( 70.0 ).withBatchSize( 9.0 )
            .withStockLowAction( RefStockLowActionKey.POREQ ).build();

      PartNoKey lBatchPart = createBatchPart( lStockNo );
      InventoryKey lBatchInventory = createInventory( lBatchPart, iSupplyLocation, 62.0 );

      // assert inventory has default quantity of 62.0
      InvInv lInvInv = new InvInv( lBatchInventory );
      lInvInv.assertBinQuantity( 62.0 );

      // decrease quantity to 57 which will trigger stock low
      adjustInventoryQuantity( lBatchInventory, 57.0 );
      lInvInv = new InvInv( lBatchInventory );
      lInvInv.assertBinQuantity( 57.0 );

      // call core logic
      List<PartRequestKey> lPartRequests =
            new StockLevelChecker( lStockNo, iSupplyLocation, iHr ).checkStockLevels();

      // assert 1 stock request is created with quantity requested as 9
      assertEquals( 1, lPartRequests.size() );

      PartRequestKey lPartRequestKey1 = lPartRequests.get( 0 );
      ReqPart lReqPart = new ReqPart( lPartRequestKey1 );
      lReqPart.assertReqQt( 9.0 );
      lReqPart.assertReqStockNo( lStockNo );
      lReqPart.assertReqType( RefReqTypeKey.STOCK );
      EvtEventUtil lEvtEvent = new EvtEventUtil( lPartRequests.get( 0 ).getEventKey() );
      lEvtEvent.assertEventStatus( RefEventStatusKey.PRPOREQ );

      // decrease quantity again
      adjustInventoryQuantity( lBatchInventory, 50.0 );
      lInvInv = new InvInv( lBatchInventory );
      lInvInv.assertBinQuantity( 50.0 );

      // check stock levels again
      lPartRequests = new StockLevelChecker( lStockNo, iSupplyLocation, iHr ).checkStockLevels();

      // assert 1 stock request is created with quantity requested as 9
      assertEquals( 1, lPartRequests.size() );

      PartRequestKey lPartRequestKey2 = lPartRequests.get( 0 );
      lReqPart = new ReqPart( lPartRequestKey2 );
      lReqPart.assertReqQt( 9.0 );
      lReqPart.assertReqStockNo( lStockNo );
      lReqPart.assertReqType( RefReqTypeKey.STOCK );
      lEvtEvent = new EvtEventUtil( lPartRequests.get( 0 ).getEventKey() );
      lEvtEvent.assertEventStatus( RefEventStatusKey.PRPOREQ );

      // create po for both part requests
      VendorKey lVendor = new VendorBuilder().withCode( "TESTVENDOR" ).build();

      PurchaseOrderKey lOrder =
            new OrderBuilder().withStatus( RefEventStatusKey.POISSUED ).withVendor( lVendor )
                  .withIssueDate( new Date() ).shippingTo( iSupplyDockLocation ).build();

      // build order line
      PurchaseOrderLineKey lOrderLine = new OrderLineBuilder( lOrder )
            .withUnitType( RefQtyUnitKey.EA ).withUnitPrice( BigDecimal.TEN )
            .withLineType( RefPoLineTypeKey.PURCHASE ).withOrderQuantity( new BigDecimal( 18.0 ) )
            .promisedBy( new Date() ).forPart( lBatchPart ).withAccount( iAccount ).build();

      // assign po line to both part requests
      lReqPart = new ReqPart( lPartRequestKey1 );
      lReqPart.setPOLine( lOrderLine );
      lReqPart.update();

      lReqPart = new ReqPart( lPartRequestKey2 );
      lReqPart.setPOLine( lOrderLine );
      lReqPart.update();

      // adjust quantity again
      adjustInventoryQuantity( lBatchInventory, 40.0 );
      lInvInv = new InvInv( lBatchInventory );
      lInvInv.assertBinQuantity( 40.0 );

      lPartRequests = new StockLevelChecker( lStockNo, iSupplyLocation, iHr ).checkStockLevels();
      assertEquals( 1, lPartRequests.size() );
   }


   /**
    *
    * This test tests checking a stock level when an adhoc stock request fulfilled by a PO with an
    * alternate unit of measure already exists, but the on order quantity is lower than the reorder
    * level of the stock level, so a new stock request should be created.<br>
    * Data setup:<br>
    * <ul>
    * <li>Create a part with a standard unit of measure of IN and an alternate unit of measure of EA
    * where 40 IN = 1 EA</li>
    * <li>Create an adhoc stock request for 80 IN</li>
    * <li>Create a PO for 2 EA, authorize and issue it</li>
    * <li>Fulfill the stock request with the PO's on order inventory</li>
    * <li>Create a POREQ stock level with reorder level = 100 IN and reorder qty = 25 IN</li>
    * </ul>
    * Now check the stock levels.<br>
    * Expected result: A new stock request with quantity 25 IN should be created<br>
    *
    * @throws Exception
    */
   @Test
   public void testPOREQStockLowAction_OneStockRequestFulfilledByPOWithAlternateUnitOfMeasure()
         throws Exception {

      StockNoKey lStockNo = new StockBuilder().withInvClass( RefInvClassKey.BATCH ).build();

      // create a batch part with a standard unit of measure of IN (inches) and an alternate unit of
      // measure of EA
      // conversion factor is 40 IN = 1 EA
      PartNoKey lBatchPart = createBatchPartWithAlternateUnitOfMeasure( lStockNo, iInches,
            RefQtyUnitKey.EA, new BigDecimal( 40 ) );

      // create a stock request for 80 IN
      PartRequestKey lStockRequest = new PartRequestBuilder().withType( RefReqTypeKey.STOCK )
            .withRequestedQuantity( 80.0 ).requiredBy( getDate( 1 ) ).isNeededAt( iSupplyLocation )
            .forSpecifiedPart( lBatchPart ).withStatus( RefEventStatusKey.PRONORDER ).build();

      // create a PO for 2 EA (2 EA = 80 IN)
      // the PO is authorized and issued and associated to the stock request
      PurchaseOrderKey lPO = new OrderBuilder().withOrderType( RefPoTypeKey.PURCHASE )
            .shippingTo( iSupplyDockLocation ).withAuthStatus( RefPoAuthLvlStatusKey.APPROVED )
            .withIssueDate( new Date() ).withStatus( RefEventStatusKey.POISSUED ).build();
      PurchaseOrderLineKey lPOLine = new OrderLineBuilder( lPO ).forPart( lBatchPart )
            .withOrderQuantity( new BigDecimal( 2 ) ).withUnitType( RefQtyUnitKey.EA )
            .forPartRequest( lStockRequest ).build();

      // when the PO is issued, a shipment is created (shipment's unit of measure is always the
      // standard, so in this case IN)
      ShipmentKey lShipment = new ShipmentDomainBuilder().fromLocation( iSupplierDockLocation )
            .toLocation( iSupplyDockLocation ).withStatus( RefEventStatusKey.IXPEND )
            .withType( RefShipmentTypeKey.PURCHASE ).withOrder( lPO ).build();
      new ShipmentLineBuilder( lShipment ).forOrderLine( lPOLine ).forPart( lBatchPart )
            .withExpectedQuantity( 80.0 ).build();

      // create the stock level with reorder level = 100 and reorder qty = 25
      Double lReorderQty = 25.0;
      new StockLevelBuilder( iSupplyLocation, lStockNo, iOwnerKey ).withMinReorderLevel( 0.0 )
            .withReorderQt( 100.0 ).withMaxLevel( 500.0 ).withBatchSize( lReorderQty )
            .withStockLowAction( RefStockLowActionKey.POREQ ).build();

      // call the checkStockLevels method
      List<PartRequestKey> lPartRequests =
            new StockLevelChecker( lStockNo, iSupplyLocation, iHr ).checkStockLevels();

      assertEquals( "one stock request should be generated", 1, lPartRequests.size() );

      // assert request created for the reorder qty
      ReqPartTable lReqPart = ReqPartTable.findByPrimaryKey( lPartRequests.get( 0 ) );
      assertEquals( lReorderQty, new Double( lReqPart.getReqQt() ) );

   }


   /**
    *
    * This test tests checking a stock level when a PO with an alternate unit of measure already
    * exists, but the on order quantity is lower than the reorder level of the stock level, so a new
    * stock request should be created.<br>
    * Data setup:<br>
    * <ul>
    * <li>Create a part with a standard unit of measure of IN and an alternate unit of measure of EA
    * where 40 IN = 1 EA</li>
    * <li>Create a PO for 2 EA, authorize and issue it</li>
    * <li>Create a POREQ stock level with reorder level = 100 IN and reorder qty = 25 IN</li>
    * </ul>
    * Now check the stock levels.<br>
    * Expected result: A new stock request with quantity 25 IN should be created<br>
    *
    * @throws Exception
    */
   @Test
   public void testPOREQStockLowAction_OnePOWithAlternateUnitOfMeasure() throws Exception {

      StockNoKey lStockNo = new StockBuilder().withInvClass( RefInvClassKey.BATCH ).build();

      // create a batch part with a standard unit of measure of IN (inches) and an alternate unit of
      // measure of EA
      // conversion factor is 40 IN = 1 EA
      PartNoKey lBatchPart = createBatchPartWithAlternateUnitOfMeasure( lStockNo, iInches,
            RefQtyUnitKey.EA, new BigDecimal( 40 ) );

      // create a PO for 2 EA (2 EA = 80 IN)
      // the PO is authorized and issued
      PurchaseOrderKey lPO = new OrderBuilder().withOrderType( RefPoTypeKey.PURCHASE )
            .shippingTo( iSupplyDockLocation ).withAuthStatus( RefPoAuthLvlStatusKey.APPROVED )
            .withIssueDate( new Date() ).withStatus( RefEventStatusKey.POISSUED ).build();
      PurchaseOrderLineKey lPOLine = new OrderLineBuilder( lPO ).forPart( lBatchPart )
            .withOrderQuantity( new BigDecimal( 2 ) ).withUnitType( RefQtyUnitKey.EA ).build();

      // when the PO is issued, a shipment is created (shipment's unit of measure is always the
      // standard, so in this case IN)
      ShipmentKey lShipment = new ShipmentDomainBuilder().fromLocation( iSupplierDockLocation )
            .toLocation( iSupplyDockLocation ).withStatus( RefEventStatusKey.IXPEND )
            .withType( RefShipmentTypeKey.PURCHASE ).withOrder( lPO ).build();
      new ShipmentLineBuilder( lShipment ).forOrderLine( lPOLine ).forPart( lBatchPart )
            .withExpectedQuantity( 80.0 ).build();

      // create the stock level with reorder level = 100 and reorder qty = 25
      Double lReorderQty = 25.0;
      new StockLevelBuilder( iSupplyLocation, lStockNo, iOwnerKey ).withMinReorderLevel( 0.0 )
            .withReorderQt( 100.0 ).withMaxLevel( 500.0 ).withBatchSize( lReorderQty )
            .withStockLowAction( RefStockLowActionKey.POREQ ).build();

      // call the checkStockLevels method
      List<PartRequestKey> lPartRequests =
            new StockLevelChecker( lStockNo, iSupplyLocation, iHr ).checkStockLevels();

      assertEquals( "one stock request should be generated", 1, lPartRequests.size() );

      // assert request created for the reorder qty
      ReqPartTable lReqPart = ReqPartTable.findByPrimaryKey( lPartRequests.get( 0 ) );
      assertEquals( lReorderQty, new Double( lReqPart.getReqQt() ) );

   }


   /**
    *
    * This test tests checking a stock level when a PO with an alternate unit of measure already
    * exists, but the on order quantity is lower than the reorder level of the stock level, so a new
    * stock request should be created.<br>
    * Data setup:<br>
    * <ul>
    * <li>Create a part with a standard unit of measure of IN and an alternate unit of measure of EA
    * where 40 IN = 1 EA</li>
    * <li>Create a PO for 2 EA, authorize and issue it</li>
    * <li>Create a POREQ stock level with reorder level = 100 IN and reorder qty = 25 IN</li>
    * </ul>
    * Now check the stock levels.<br>
    * Expected result: A new stock request with quantity 25 IN should be created<br>
    *
    * @throws Exception
    */
   @Test
   public void testPOREQStockLowAction_OneShipment() throws Exception {

      StockNoKey lStockNo = new StockBuilder().withInvClass( RefInvClassKey.BATCH ).build();

      // create a batch part with a standard unit of measure of IN (inches) and an alternate unit of
      // measure of EA
      // conversion factor is 40 IN = 1 EA
      PartNoKey lBatchPart = createBatchPartWithAlternateUnitOfMeasure( lStockNo, iInches,
            RefQtyUnitKey.EA, new BigDecimal( 40 ) );

      // create a pending shipment to the supply location for 80 IN
      ShipmentKey lShipment = new ShipmentDomainBuilder().fromLocation( iSupplierDockLocation )
            .toLocation( iSupplyDockLocation ).withStatus( RefEventStatusKey.IXPEND )
            .withType( RefShipmentTypeKey.STKTRN ).build();
      new ShipmentLineBuilder( lShipment ).forPart( lBatchPart ).withExpectedQuantity( 80.0 )
            .build();

      // create the stock level with reorder level = 100 and reorder qty = 25
      Double lReorderQty = 25.0;
      new StockLevelBuilder( iSupplyLocation, lStockNo, iOwnerKey ).withMinReorderLevel( 0.0 )
            .withReorderQt( 100.0 ).withMaxLevel( 500.0 ).withBatchSize( lReorderQty )
            .withStockLowAction( RefStockLowActionKey.POREQ ).build();

      // call the checkStockLevels method
      List<PartRequestKey> lPartRequests =
            new StockLevelChecker( lStockNo, iSupplyLocation, iHr ).checkStockLevels();

      assertEquals( "one stock request should be generated", 1, lPartRequests.size() );

      // assert request created for the reorder qty
      ReqPartTable lReqPart = ReqPartTable.findByPrimaryKey( lPartRequests.get( 0 ) );
      assertEquals( lReorderQty, new Double( lReqPart.getReqQt() ) );

   }


   /**
    *
    * This test tests checking a stock level when there is already one stock request for the stock
    * which is partially fulfilled by a PO.The received and on order inventory quantity is still
    * less than the reorder level and so another stock request should be generated by the check.<br>
    * Data setup:<br>
    * <ul>
    * <li>Create a part with a standard unit of measure of IN</li>
    * <li>Create an adhoc stock request for 80 IN.</li>
    * <li>Create a PO for 80 IN and link the stock request to it. Authorize and issue the PO.</li>
    * <li>Partially receive the PO by receiving 50 IN. This results in available inventory with qty
    * = 50 IN and on order inventory with qty = 30 IN.</li>
    * <li>Create a POREQ stock level with reorder level = 100 IN and reorder qty = 25 IN</li>
    * </ul>
    * Now check the stock levels.<br>
    * Expected result: A new stock request with quantity 25 IN should be created<br>
    *
    * @throws Exception
    */
   @Test
   public void testPOREQStockLowAction_OneStockRequestFulfilledByPOPartiallyReceived()
         throws Exception {

      StockNoKey lStockNo = new StockBuilder().withInvClass( RefInvClassKey.BATCH ).build();

      // create a batch part with a standard unit of measure of IN (inches) and an alternate unit of
      // measure of EA
      // conversion factor is 40 IN = 1 EA
      PartNoKey lBatchPart = createBatchPartWithAlternateUnitOfMeasure( lStockNo, iInches,
            RefQtyUnitKey.EA, new BigDecimal( 40 ) );

      // create two stock requests - one for 50 IN and one for 30 IN
      // this is to imitate a stock request of 80 IN getting split when 50 IN was received
      PartRequestKey lStockRequest50 = new PartRequestBuilder().withType( RefReqTypeKey.STOCK )
            .withRequestedQuantity( 50.0 ).requiredBy( getDate( 1 ) ).isNeededAt( iSupplyLocation )
            .forSpecifiedPart( lBatchPart ).withStatus( RefEventStatusKey.PRCOMPLETE ).build();
      PartRequestKey lStockRequest30 = new PartRequestBuilder().withType( RefReqTypeKey.STOCK )
            .withRequestedQuantity( 30.0 ).requiredBy( getDate( 1 ) ).isNeededAt( iSupplyLocation )
            .forSpecifiedPart( lBatchPart ).withStatus( RefEventStatusKey.PRONORDER ).build();

      // Create PO for 80 IN that is partially received (received 50 IN, pending 30 IN)
      PurchaseOrderKey lPO = new OrderBuilder().withOrderType( RefPoTypeKey.PURCHASE )
            .shippingTo( iSupplyDockLocation ).withAuthStatus( RefPoAuthLvlStatusKey.APPROVED )
            .withIssueDate( new Date() ).withStatus( RefEventStatusKey.POPARTIAL ).build();
      PurchaseOrderLineKey lPOLine = new OrderLineBuilder( lPO ).forPart( lBatchPart )
            .forPartRequest( lStockRequest30 ).withOrderQuantity( new BigDecimal( 80 ) )
            .withUnitType( iInches ).withReceivedQuantity( new BigDecimal( 50 ) ).build();

      // create the received inventory
      InventoryKey lReceivedInventory = createInventory( lBatchPart, iSupplyDockLocation, 50.0 );

      // create one completed historic shipment that received the 50 IN
      ShipmentKey lShipmentComplete = new ShipmentDomainBuilder().fromLocation( iSupplierDockLocation )
            .toLocation( iSupplyDockLocation ).withStatus( RefEventStatusKey.IXCMPLT )
            .withHistoric( true ).withType( RefShipmentTypeKey.PURCHASE ).withOrder( lPO ).build();
      new ShipmentLineBuilder( lShipmentComplete ).forOrderLine( lPOLine ).forPart( lBatchPart )
            .withExpectedQuantity( 80.0 ).withReceivedQuantity( 50.0 )
            .forInventory( lReceivedInventory ).build();

      // create one pending shipment for 30 IN
      ShipmentKey lShipmentPending = new ShipmentDomainBuilder().fromLocation( iSupplierDockLocation )
            .toLocation( iSupplyDockLocation ).withStatus( RefEventStatusKey.IXPEND )
            .withType( RefShipmentTypeKey.PURCHASE ).withOrder( lPO ).build();
      new ShipmentLineBuilder( lShipmentPending ).forOrderLine( lPOLine ).forPart( lBatchPart )
            .withExpectedQuantity( 30.0 ).build();

      // create the stock level with reorder level = 100 and reorder qty = 25
      Double lReorderQty = 25.0;
      new StockLevelBuilder( iSupplyLocation, lStockNo, iOwnerKey ).withMinReorderLevel( 0.0 )
            .withReorderQt( 100.0 ).withMaxLevel( 500.0 ).withBatchSize( lReorderQty )
            .withStockLowAction( RefStockLowActionKey.POREQ ).build();

      // call the checkStockLevels method
      List<PartRequestKey> lPartRequests =
            new StockLevelChecker( lStockNo, iSupplyLocation, iHr ).checkStockLevels();

      assertEquals( "one stock request should be generated", 1, lPartRequests.size() );

      // assert request created for the reorder qty
      ReqPartTable lReqPart = ReqPartTable.findByPrimaryKey( lPartRequests.get( 0 ) );
      assertEquals( lReorderQty, new Double( lReqPart.getReqQt() ) );

   }


   /**
    *
    * This test tests checking a stock level when there is already two stock requests for the stock
    * which are both being fulfilled by the same PO. No inventory has been received yet, and the
    * on-order quantity is less than the reorder level and so another stock request should be
    * generated by the check.<br>
    * Data setup:<br>
    * <ul>
    * <li>Create a part with a standard unit of measure of IN</li>
    * <li>Create an adhoc stock request for 50 IN.</li>
    * <li>Create another adhoc stock request for 30 IN.</li>
    * <li>Create a PO for 80 IN and link both stock requests to it. Authorize and issue the PO.</li>
    * <li>Create a POREQ stock level with reorder level = 100 IN and reorder qty = 25 IN</li>
    * </ul>
    * Now check the stock levels.<br>
    * Expected result: A new stock request with quantity 25 IN should be created<br>
    *
    * @throws Exception
    */
   @Test
   public void testPOREQStockLowAction_TwoStockRequestsFulfilledByPO() throws Exception {

      StockNoKey lStockNo = new StockBuilder().withInvClass( RefInvClassKey.BATCH ).build();

      // create a batch part with a standard unit of measure of IN (inches) and an alternate unit of
      // measure of EA
      // conversion factor is 40 IN = 1 EA
      PartNoKey lBatchPart = createBatchPartWithAlternateUnitOfMeasure( lStockNo, iInches,
            RefQtyUnitKey.EA, new BigDecimal( 40 ) );

      // create a stock request for 50 IN
      PartRequestKey lStockRequest50 = new PartRequestBuilder().withType( RefReqTypeKey.STOCK )
            .withRequestedQuantity( 50.0 ).requiredBy( getDate( 1 ) ).isNeededAt( iSupplyLocation )
            .forSpecifiedPart( lBatchPart ).withStatus( RefEventStatusKey.PRONORDER ).build();
      // create a stock request for 30 IN
      PartRequestKey lStockRequest30 = new PartRequestBuilder().withType( RefReqTypeKey.STOCK )
            .withRequestedQuantity( 30.0 ).requiredBy( getDate( 1 ) ).isNeededAt( iSupplyLocation )
            .forSpecifiedPart( lBatchPart ).withStatus( RefEventStatusKey.PRONORDER ).build();

      // Create PO for 80 IN
      PurchaseOrderKey lPO = new OrderBuilder().withOrderType( RefPoTypeKey.PURCHASE )
            .shippingTo( iSupplyDockLocation ).withAuthStatus( RefPoAuthLvlStatusKey.APPROVED )
            .withIssueDate( new Date() ).withStatus( RefEventStatusKey.POISSUED ).build();
      PurchaseOrderLineKey lPOLine = new OrderLineBuilder( lPO ).forPart( lBatchPart )
            .forPartRequest( lStockRequest50 ).forPartRequest( lStockRequest30 )
            .withOrderQuantity( new BigDecimal( 80 ) ).withUnitType( iInches ).build();

      // create one pending shipment for 80 IN
      ShipmentKey lShipmentPending = new ShipmentDomainBuilder().fromLocation( iSupplierDockLocation )
            .toLocation( iSupplyDockLocation ).withStatus( RefEventStatusKey.IXPEND )
            .withType( RefShipmentTypeKey.PURCHASE ).withOrder( lPO ).build();
      new ShipmentLineBuilder( lShipmentPending ).forOrderLine( lPOLine ).forPart( lBatchPart )
            .withExpectedQuantity( 80.0 ).build();

      // create the stock level with reorder level = 100 and reorder qty = 25
      Double lReorderQty = 25.0;
      new StockLevelBuilder( iSupplyLocation, lStockNo, iOwnerKey ).withMinReorderLevel( 0.0 )
            .withReorderQt( 100.0 ).withMaxLevel( 500.0 ).withBatchSize( lReorderQty )
            .withStockLowAction( RefStockLowActionKey.POREQ ).build();

      // call the checkStockLevels method
      List<PartRequestKey> lPartRequests =
            new StockLevelChecker( lStockNo, iSupplyLocation, iHr ).checkStockLevels();

      assertEquals( "one stock request should be generated", 1, lPartRequests.size() );

      // assert request created for the reorder qty
      ReqPartTable lReqPart = ReqPartTable.findByPrimaryKey( lPartRequests.get( 0 ) );
      assertEquals( lReorderQty, new Double( lReqPart.getReqQt() ) );

   }


   /**
    *
    * This test tests checking a stock level when there are two POs for the given stock. One PO is
    * open and the other is issued. The total on-order quantity is less than the reorder level and
    * so another stock request should be generated by the check.<br>
    * Data setup:<br>
    * <ul>
    * <li>Create a part with a standard unit of measure of IN and an alternate unit of measure of EA
    * where 40 IN = 1 EA</li>
    * <li>Create a PO for 35 IN. Authorize and issue this PO.</li>
    * <li>Create another PO for 1 EA (1 EA = 40 IN). Authorize and issue this PO.</li>
    * <li>Create a POREQ stock level with reorder level = 100 IN and reorder qty = 25 IN</li>
    * </ul>
    * Now check the stock levels.<br>
    * Expected result: A new stock request with quantity 25 IN should be created<br>
    *
    * @throws Exception
    */
   @Test
   public void testPOREQStockLowAction_TwoPOsWithDifferentUnitOfMeasures() throws Exception {

      StockNoKey lStockNo = new StockBuilder().withInvClass( RefInvClassKey.BATCH ).build();

      // create a batch part with a standard unit of measure of IN (inches) and an alternate unit of
      // measure of EA
      // conversion factor is 40 IN = 1 EA
      PartNoKey lBatchPart = createBatchPartWithAlternateUnitOfMeasure( lStockNo, iInches,
            RefQtyUnitKey.EA, new BigDecimal( 40 ) );

      // Create PO for 35 IN
      PurchaseOrderKey lPOfor35Inches = new OrderBuilder().withOrderType( RefPoTypeKey.PURCHASE )
            .shippingTo( iSupplyDockLocation ).withAuthStatus( RefPoAuthLvlStatusKey.APPROVED )
            .withIssueDate( new Date() ).withStatus( RefEventStatusKey.POISSUED ).build();
      PurchaseOrderLineKey lPOLinefor35Inches =
            new OrderLineBuilder( lPOfor35Inches ).forPart( lBatchPart )
                  .withOrderQuantity( new BigDecimal( 35 ) ).withUnitType( iInches ).build();

      // create one pending shipment for 35 IN (this would happen upon issuing of the PO)
      ShipmentKey lShipmentPendingfor35INches =
            new ShipmentDomainBuilder().fromLocation( iSupplierDockLocation )
                  .toLocation( iSupplyDockLocation ).withStatus( RefEventStatusKey.IXPEND )
                  .withType( RefShipmentTypeKey.PURCHASE ).withOrder( lPOfor35Inches ).build();
      new ShipmentLineBuilder( lShipmentPendingfor35INches ).forOrderLine( lPOLinefor35Inches )
            .forPart( lBatchPart ).withExpectedQuantity( 35.0 ).build();

      // Create another PO for 1 EA
      PurchaseOrderKey lPOfor1EA = new OrderBuilder().withOrderType( RefPoTypeKey.PURCHASE )
            .shippingTo( iSupplyDockLocation ).withAuthStatus( RefPoAuthLvlStatusKey.APPROVED )
            .withIssueDate( new Date() ).withStatus( RefEventStatusKey.POISSUED ).build();
      PurchaseOrderLineKey lPOLinefor1EA = new OrderLineBuilder( lPOfor1EA ).forPart( lBatchPart )
            .withOrderQuantity( new BigDecimal( 1 ) ).withUnitType( RefQtyUnitKey.EA ).build();

      // create one pending shipment for 40 IN for the issued PO (1 EA = 40 IN)
      ShipmentKey lShipmentPendingfor1EA =
            new ShipmentDomainBuilder().fromLocation( iSupplierDockLocation )
                  .toLocation( iSupplyDockLocation ).withStatus( RefEventStatusKey.IXPEND )
                  .withType( RefShipmentTypeKey.PURCHASE ).withOrder( lPOfor1EA ).build();
      new ShipmentLineBuilder( lShipmentPendingfor1EA ).forOrderLine( lPOLinefor1EA )
            .forPart( lBatchPart ).withExpectedQuantity( 40.0 ).build();

      // create the stock level with reorder level = 100 and reorder qty = 25
      Double lReorderQty = 25.0;
      new StockLevelBuilder( iSupplyLocation, lStockNo, iOwnerKey ).withMinReorderLevel( 0.0 )
            .withReorderQt( 100.0 ).withMaxLevel( 500.0 ).withBatchSize( lReorderQty )
            .withStockLowAction( RefStockLowActionKey.POREQ ).build();

      // call the checkStockLevels method
      List<PartRequestKey> lPartRequests =
            new StockLevelChecker( lStockNo, iSupplyLocation, iHr ).checkStockLevels();

      assertEquals( "one stock request should be generated", 1, lPartRequests.size() );

      // assert request created for the reorder qty
      ReqPartTable lReqPart = ReqPartTable.findByPrimaryKey( lPartRequests.get( 0 ) );
      assertEquals( lReorderQty, new Double( lReqPart.getReqQt() ) );

   }


   /**
    * Tests the case where stock low action MANUAL is chosen.<br>
    * No part requests should be generated
    *
    * @exception Exception
    *               if an error occurs.
    */
   @Test
   public void testMANUALStockLowAction() throws Exception {

      // create a stock with MANUAL stock low action
      StockNoKey lStockNo = new StockBuilder().withInvClass( RefInvClassKey.BATCH ).build();
      new StockLevelBuilder( iSupplyLocation, lStockNo, iOwnerKey ).withMinReorderLevel( 5.0 )
            .withReorderQt( 10.0 ).withMaxLevel( 50.0 ).withBatchSize( 10.0 )
            .withStockLowAction( RefStockLowActionKey.MANUAL ).build();

      // create a part and assign part to stock
      createBatchPart( lStockNo );

      // call the checkStockLevels method
      List<PartRequestKey> lPartRequests =
            new StockLevelChecker( lStockNo, iSupplyLocation, iHr ).checkStockLevels();

      // assert that no part requests were created
      assertTrue( lPartRequests.isEmpty() );
   }


   /**
    * Tests the checkStockLevels method for a stock with no assigned parts
    *
    * @exception Exception
    *               if an error occurs.
    */
   @Test
   public void testStockWithNoAssignedParts() throws Exception {

      // create a stock with MANUAL stock low action
      StockNoKey lStockNo = new StockBuilder().withInvClass( RefInvClassKey.BATCH ).build();
      new StockLevelBuilder( iSupplyLocation, lStockNo, iOwnerKey ).withMinReorderLevel( 5.0 )
            .withReorderQt( 10.0 ).withMaxLevel( 50.0 ).withBatchSize( 10.0 )
            .withStockLowAction( RefStockLowActionKey.POREQ ).build();

      // call the checkStockLevels method
      List<PartRequestKey> lPartRequests =
            new StockLevelChecker( lStockNo, iSupplyLocation, iHr ).checkStockLevels();

      // assert that no part requests were created
      assertTrue( lPartRequests.isEmpty() );
   }


   /**
    * GIVEN a stock with one assigned part and auto_create_po and auto_issue_po set to true, WHEN
    * the stock low action is POREQ and a stock low check is done, THEN a part request should be
    * created AND a PO should be attached to it and automatically issued
    *
    * @exception Exception
    *               if an error occurs.
    */
   @Test
   public void testPOREQStockLowAction_StockWithOneAssignedPartAndAutoCreatePOTrue()
         throws Exception {

      // create a stock with POREQ stock low action
      StockNoKey lStockNo = new StockBuilder().withInvClass( RefInvClassKey.BATCH )
            .isAutoCreatePo( true ).isAutoIssuePo( true ).build();

      new StockLevelBuilder( iSupplyLocation, lStockNo, iOwnerKey ).withMinReorderLevel( 2.0 )
            .withReorderQt( 10.0 ).withMaxLevel( 50.0 ).withBatchSize( 10.0 )
            .withStockLowAction( RefStockLowActionKey.POREQ ).build();

      // create a part and assign part to stock
      PartNoKey lPartKey = createBatchPart( lStockNo );

      // assign an approved, preferred vendor to the part
      new PartVendorBuilder( iVendor, lPartKey ).isPreferred( true )
            .withVendorStatus( RefVendorStatusKey.APPROVED ).build();

      // call the checkStockLevels method
      List<PartRequestKey> lPartRequests =
            new StockLevelChecker( lStockNo, iSupplyLocation, iHr ).checkStockLevels();

      // assert part request created
      assertEquals( 1, lPartRequests.size() );

      // assert that the purchase contact, customer code and terms and conditions fields are set
      // when PO is auto created
      PoHeader lAutoCreatedPO = new PoHeader(
            ReqPartTable.findByPrimaryKey( lPartRequests.get( 0 ) ).getPurchaseOrder() );
      lAutoCreatedPO.assertPurchasingContact( HumanResourceKey.MX_PURCHASE );
      lAutoCreatedPO.assertTermsConditions( iVendorTermsConditionsKey );
      lAutoCreatedPO.assertSpec2kCustomer( iSpec2kCustKey );

      // assert PO created, issued and authorized by the system user MXPURCHASE
      assertPOAutoIssuedAndAuthorizedByMxpurchase( lPartRequests.get( 0 ) );
   }


   /**
    *
    * GIVEN a stock with stock low action SHIPREQ and one assigned part with 6 items of inventory at
    * a supplier location, WHEN the inventory quantity is not sufficient, THEN a part request and a
    * shipment will be created for the 6 remote inventory AND a purchase request and a PO created
    * for the remaining part requirement of 4 inventory items
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testSHIPREQStockLowAction_OneAssignedPartAndAutoCreatePOTrue() throws Exception {

      // create a stock with SHIPREQ stock low action
      StockNoKey lStockNo = new StockBuilder().withInvClass( RefInvClassKey.BATCH )
            .isAutoCreatePo( true ).isAutoIssuePo( true ).build();

      new StockLevelBuilder( iSupplyLocation, lStockNo, iOwnerKey ).withMinReorderLevel( 2.0 )
            .withReorderQt( 10.0 ).withMaxLevel( 50.0 ).withBatchSize( 10.0 )
            .withStockLowAction( RefStockLowActionKey.SHIPREQ ).build();

      // create a part and assign part to stock
      PartNoKey lPartKey = createBatchPart( lStockNo );

      // create inventory at the supplier location to iSupplyLocation
      createInventory( lPartKey, iSupplierLocation, 6.0 );

      // assign an approved, preferred vendor to the part
      new PartVendorBuilder( iVendor, lPartKey ).isPreferred( true )
            .withVendorStatus( RefVendorStatusKey.APPROVED ).build();

      // call the checkStockLevels method
      List<PartRequestKey> lPartRequests =
            new StockLevelChecker( lStockNo, iSupplyLocation, iHr ).checkStockLevels();

      // assert part requests created
      assertEquals( 2, lPartRequests.size() );

      // assert shipment created for the remotely available inventory
      ReqPartTable lReqPartShipment = ReqPartTable.findByPrimaryKey( lPartRequests.get( 0 ) );
      ShipmentLineKey lShipmentLineKey = lReqPartShipment.getShipmentLine();
      assertNotNull( lShipmentLineKey );

      // assert PO created, issued and authorized by the system user MXPURCHASE
      assertPOAutoIssuedAndAuthorizedByMxpurchase( lPartRequests.get( 1 ) );
   }


   /**
    * Tests the checkStockLevels method for a stock with no stock level. Method should exit
    * gracefully before creating any Part Requests.
    *
    * @exception Exception
    *               if an error occurs.
    */
   @Test
   public void testStockHasNoLevels() throws Exception {

      // create a stock with MANUAL stock low action
      StockNoKey lStockNo = new StockBuilder().withInvClass( RefInvClassKey.BATCH ).build();

      // create a part and assign part to stock
      createBatchPart( lStockNo );

      // call the checkStockLevels method
      List<PartRequestKey> lPartRequests =
            new StockLevelChecker( lStockNo, iSupplyLocation, iHr ).checkStockLevels();

      // assert that no part requests were created
      assertTrue( lPartRequests.isEmpty() );
   }


   /**
    * Tests the case of a stock with a stock level with stock low action set to SHIPREQ where: <br>
    * <ul>
    * <li>there is no inventory at the stock level location</li>
    * <li>there is enough quantity at the supplier location to bring the stock level back to the
    * reorder level</li>
    * </ul>
    * Outcome: A stock request will be generated and the inventory will be remote reserved from the
    * supplier location. A shipment will be created from the supplier location to the stock level
    * location for the requested quantity.
    *
    * @exception Exception
    *               if an error occurs.
    */
   @Test
   public void testSHIPREQStockLowAction() throws Exception {

      // create a stock with a SHIPREQ stock level
      StockNoKey lStockNo = new StockBuilder().withInvClass( RefInvClassKey.BATCH ).build();

      new StockLevelBuilder( iSupplyLocation, lStockNo, iOwnerKey ).withMinReorderLevel( 2.0 )
            .withReorderQt( 10.0 ).withMaxLevel( 50.0 ).withBatchSize( 4.0 )
            .withStockLowAction( RefStockLowActionKey.SHIPREQ ).build();

      // create a part and assign part to stock
      PartNoKey lPartKey = createBatchPart( lStockNo );

      // create inventory for the part at the supplier location
      // where inventory bin quantity > reorder level
      InventoryKey lInvAtSupplier = createInventory( lPartKey, iSupplierLocation, 50.0 );

      // call the checkStockLevels method
      List<PartRequestKey> lPartRequests =
            new StockLevelChecker( lStockNo, iSupplyLocation, iHr ).checkStockLevels();

      // assert part request created
      assertEquals( 1, lPartRequests.size() );
      PartRequestKey lStockRequest = lPartRequests.get( 0 );

      // assert the part request is properly generated
      ReqPart lReqPart = new ReqPart( lStockRequest );
      lReqPart.assertReqType( RefReqTypeKey.STOCK );
      lReqPart.assertReqStockNo( lStockNo );

      // reorder level is 10, reorder/shipping qty is 4.0
      // we need to request enough to get back above the reorder level, i.e. ( x * 4) > 10
      // assert requested quantity is 12 (3 * 4 = 12 > 10)
      Double lRequestedQty = 12.0;
      lReqPart.assertReqQt( lRequestedQty );

      // assert the request status set to REMOTE
      EvtEventUtil lEvtEvent = new EvtEventUtil( lStockRequest.getEventKey() );
      lEvtEvent.assertEventStatus( RefEventStatusKey.PRREMOTE );

      // assert the supplier inventory is reserved
      assertNotNull( lReqPart.getInventory() );
      lReqPart.assertInventoryKey( lInvAtSupplier );

      // assert shipment created
      ShipmentLineKey lShipmentLineKey = lReqPart.getShipmentLine();
      assertNotNull( lShipmentLineKey );

      // assert the shipment is properly generated
      ShipShipmentLine lShipLine = new ShipShipmentLine( lShipmentLineKey );
      lShipLine.assertExpectQt( lRequestedQty );
      lShipLine.assertInventory( null );

      ShipmentKey lShipmentKey = ShipmentService.getShipment( lShipmentLineKey );
      ShipShipment lShipment = new ShipShipment( lShipmentKey );
      lShipment.assertShipmentType( RefShipmentTypeKey.STKTRN );

      LocationKey lFromLocation = ShipmentService.getShipFromLocation( lShipmentKey );
      assertEquals( lFromLocation, iSupplierDockLocation );

      LocationKey lToLocation = ShipmentService.getShipToLocation( lShipmentKey );
      assertEquals( lToLocation, iSupplyDockLocation );

   }


   /**
    * Tests the case of a stock with a stock level with stock low action set to SHIPREQ where: <br>
    * <ul>
    * <li>there is no inventory at the stock level location</li>
    * <li>there is not enough quantity at the supplier location to bring the stock level back to the
    * reorder level</li>
    * </ul>
    * Outcome: A stock request will be generated for the quantity necessary to bring the stock back
    * to the reorder level. The inventory will be remote reserved from the supplier location, but
    * since there is not enough, this will split the stock request into two requests: REMOTE and
    * POREQ. The REMOTE stock request will have a shipment created from the supplier location to the
    * stock level location.
    *
    * @exception Exception
    *               if an error occurs.
    */
   @Test
   public void testSHIPREQStockLowAction_NotEnoughQuantityAtSupplier() throws Exception {

      // create a stock with a SHIPREQ stock level
      StockNoKey lStockNo = new StockBuilder().withInvClass( RefInvClassKey.BATCH ).build();

      Double lReorderShippingQty = 10.0;
      new StockLevelBuilder( iSupplyLocation, lStockNo, iOwnerKey ).withMinReorderLevel( 2.0 )
            .withReorderQt( 10.0 ).withMaxLevel( 50.0 ).withBatchSize( lReorderShippingQty )
            .withStockLowAction( RefStockLowActionKey.SHIPREQ ).build();

      // create a part and assign part to stock
      PartNoKey lPartKey = createBatchPart( lStockNo );

      // create inventory for the part at the supplier location
      // where inventory bin quantity < reorder level
      Double lBinQty = 6.0;
      InventoryKey lInvAtSupplier = createInventory( lPartKey, iSupplierLocation, lBinQty );

      // call the checkStockLevels method
      List<PartRequestKey> lPartRequests =
            new StockLevelChecker( lStockNo, iSupplyLocation, iHr ).checkStockLevels();

      // assert part request created
      assertEquals( 2, lPartRequests.size() );

      for ( PartRequestKey lPartRequestKey : lPartRequests ) {

         ReqPart lReqPart = new ReqPart( lPartRequestKey );
         lReqPart.assertReqType( RefReqTypeKey.STOCK );
         lReqPart.assertReqStockNo( lStockNo );

         EvtEventUtil lEvtEvent = new EvtEventUtil( lPartRequestKey.getEventKey() );

         if ( lReqPart.getReqQt() == lBinQty ) {

            // assert the supplier inventory is reserved
            assertNotNull( lReqPart.getInventory() );
            lReqPart.assertInventoryKey( lInvAtSupplier );

            // assert the request status is REMOTE
            lEvtEvent.assertEventStatus( RefEventStatusKey.PRREMOTE );

            // assert shipment created
            ShipmentLineKey lShipmentLineKey = lReqPart.getShipmentLine();
            assertNotNull( lShipmentLineKey );

            // assert the shipment is properly generated
            ShipShipmentLine lShipLine = new ShipShipmentLine( lShipmentLineKey );
            lShipLine.assertExpectQt( lBinQty );
            lShipLine.assertInventory( null );

            ShipmentKey lShipmentKey = ShipmentService.getShipment( lShipmentLineKey );
            ShipShipment lShipment = new ShipShipment( lShipmentKey );
            lShipment.assertShipmentType( RefShipmentTypeKey.STKTRN );

            LocationKey lFromLocation = ShipmentService.getShipFromLocation( lShipmentKey );
            assertEquals( lFromLocation, iSupplierDockLocation );

            LocationKey lToLocation = ShipmentService.getShipToLocation( lShipmentKey );
            assertEquals( lToLocation, iSupplyDockLocation );

         } else {
            // there should be one request for the remaining amount that could not be fulfilled, and
            // it should be POREQ
            lReqPart.assertReqQt( lReorderShippingQty - lBinQty );
            lEvtEvent.assertEventStatus( RefEventStatusKey.PRPOREQ );
         }
      }
   }


   /**
    * Tests the case of a stock with a stock level with stock low action set to SHIPREQ where: <br>
    * <ul>
    * <li>there is no inventory at the stock level location</li>
    * <li>there are three batches at the supplier location which contain enough quantity between
    * them to bring the stock level back to the reorder level</li>
    * </ul>
    * Outcome: A stock request will be generated for the quantity necessary to bring the stock back
    * to the reorder level. The inventory will be remote reserved from the supplier location, for
    * each batch, a new stock request will be split off to reserve it. In the end there will be
    * three requests with status REMOTE and each will have a shipment created from the supplier
    * location to the stock level location.
    *
    * @exception Exception
    *               if an error occurs.
    */
   @Test
   public void testSHIPREQStockLowAction_EnoughQuantityWithMultipleBatches() throws Exception {

      // create a stock with a SHIPREQ stock level
      StockNoKey lStockNo = new StockBuilder().withInvClass( RefInvClassKey.BATCH ).build();

      Double lReorderShippingQty = 10.0;
      new StockLevelBuilder( iSupplyLocation, lStockNo, iOwnerKey ).withMinReorderLevel( 2.0 )
            .withReorderQt( 10.0 ).withMaxLevel( 50.0 ).withBatchSize( lReorderShippingQty )
            .withStockLowAction( RefStockLowActionKey.SHIPREQ ).build();

      // create a part and assign part to stock
      PartNoKey lPartKey = createBatchPart( lStockNo );

      // create inventory for the part at the supplier location
      // lBinQtyBatchA + lBinQtyBatchB + lBinQtyBatchC = lReorderShippingQty
      Double lBinQtyBatchA = 6.0;
      Double lBinQtyBatchB = 3.0;
      Double lBinQtyBatchC = 1.0;
      InventoryKey lBatchA = createInventory( lPartKey, iSupplierLocation, lBinQtyBatchA );
      InventoryKey lBatchB = createInventory( lPartKey, iSupplierLocation, lBinQtyBatchB );
      InventoryKey lBatchC = createInventory( lPartKey, iSupplierLocation, lBinQtyBatchC );

      // call the checkStockLevels method
      List<PartRequestKey> lPartRequests =
            new StockLevelChecker( lStockNo, iSupplyLocation, iHr ).checkStockLevels();

      // assert 3 part requests created
      assertEquals( 3, lPartRequests.size() );

      for ( PartRequestKey lPartRequestKey : lPartRequests ) {

         ReqPart lReqPart = new ReqPart( lPartRequestKey );
         lReqPart.assertReqType( RefReqTypeKey.STOCK );
         lReqPart.assertReqStockNo( lStockNo );

         EvtEventUtil lEvtEvent = new EvtEventUtil( lPartRequestKey.getEventKey() );

         // assert the request status is REMOTE
         lEvtEvent.assertEventStatus( RefEventStatusKey.PRREMOTE );

         // assert the supplier inventory is reserved
         assertNotNull( lReqPart.getInventory() );

         // assert shipment created
         ShipmentLineKey lShipmentLineKey = lReqPart.getShipmentLine();
         assertNotNull( lShipmentLineKey );

         // assert the shipment is properly generated
         ShipShipmentLine lShipLine = new ShipShipmentLine( lShipmentLineKey );
         lShipLine.assertInventory( null );

         ShipmentKey lShipmentKey = ShipmentService.getShipment( lShipmentLineKey );
         ShipShipment lShipment = new ShipShipment( lShipmentKey );
         lShipment.assertShipmentType( RefShipmentTypeKey.STKTRN );

         LocationKey lFromLocation = ShipmentService.getShipFromLocation( lShipmentKey );
         assertEquals( lFromLocation, iSupplierDockLocation );

         LocationKey lToLocation = ShipmentService.getShipToLocation( lShipmentKey );
         assertEquals( lToLocation, iSupplyDockLocation );

         // assert the quantities and inventory reserved are correct
         if ( lReqPart.getReqQt() == lBinQtyBatchA ) {
            lReqPart.assertInventoryKey( lBatchA );
            lShipLine.assertExpectQt( lBinQtyBatchA );
         } else if ( lReqPart.getReqQt() == lBinQtyBatchB ) {
            lReqPart.assertInventoryKey( lBatchB );
            lShipLine.assertExpectQt( lBinQtyBatchB );
         } else {
            lReqPart.assertInventoryKey( lBatchC );
            lShipLine.assertExpectQt( lBinQtyBatchC );
         }
      }
   }


   @Before
   public void loadData() throws Exception {

      iHr = new HumanResourceDomainBuilder().withUsername( USERNAME_TESTUSER )
            .withUserId( USERID_TESTUSER ).build();

      // create a supplier location
      iSupplierLocation = new LocationDomainBuilder().withType( RefLocTypeKey.AIRPORT )
            .withCode( "supplier" ).isSupplyLocation().build();
      iSupplierDockLocation = new LocationDomainBuilder().withType( RefLocTypeKey.DOCK )
            .withParent( iSupplierLocation ).withSupplyLocation( iSupplierLocation )
            .isDefaultDock( true ).build();

      // create a supply location linked to the supplier
      iSupplyLocation = new LocationDomainBuilder().withType( RefLocTypeKey.AIRPORT )
            .withCode( "loc" ).isSupplyLocation().withHubLocation( iSupplierLocation ).build();
      iSupplyDockLocation =
            new LocationDomainBuilder().withType( RefLocTypeKey.DOCK ).withParent( iSupplyLocation )
                  .withSupplyLocation( iSupplyLocation ).isDefaultDock( true ).build();

      iOwnerKey = new OwnerDomainBuilder().isDefault().build();

      iAccount = new AccountBuilder().withType( RefAccountTypeKey.INVASSET )
            .withCode( "TESTACCOUNT" ).isDefault().build();

      RefCurrencyKey lCurrency =
            new CurrencyBuilder( "USD" ).isDefault().withExchangeQt( BigDecimal.ONE ).build();

      iVendorTermsConditionsKey = new RefTermsConditionsKey( 0, TERMS_AND_CONDITIONS );

      iSpec2kCustKey = new Spec2kCustBuilder( SPEC2K_CUST_CD ).isDefault().build();

      iVendor = new VendorBuilder().withCode( "TESTVENDOR" ).atLocation( iSupplyLocation )
            .withCurrency( lCurrency ).isNoPrintReq( true )
            .withTermsAndConditions( iVendorTermsConditionsKey ).build();

      iRefSpec2kCmndKey = new RefSpec2kCmndKey( 0, SPEC2K_CMND_CD );

      new OrgVendorSpec2kCmndBuilder( iVendor, 1, iRefSpec2kCmndKey ).build();

      new OrderAuthorizationFlowBuilder( "PURCHASE" ).build();

      RefQtyUnit lInchesQtyUnit = RefQtyUnit.create( "IN" );
      lInchesQtyUnit.setDecimalPlacesQt( 0 );
      iInches = lInchesQtyUnit.insert();

   }


   /**
    * Helper method to adjust the inventory current quantity
    *
    * @param aNewQty
    */
   private void adjustInventoryQuantity( InventoryKey aInventory, Double aNewQty ) {
      InvInvTable lTable = InvInvTable.findByPrimaryKey( aInventory );
      lTable.setBinQt( aNewQty );
      lTable.update();
   }


   private PartNoKey createBatchPart( StockNoKey aStock ) {
      return new PartNoBuilder().withInventoryClass( RefInvClassKey.BATCH ).withRepairBool( true )
            .withStatus( RefPartStatusKey.ACTV ).withFinancialType( RefFinanceTypeKey.CONSUM )
            .withUnitType( RefQtyUnitKey.EA ).withTotalQuantity( new BigDecimal( 100.0 ) )
            .withTotalValue( BigDecimal.ONE ).withAssetAccount( iAccount ).withStock( aStock )
            .build();
   }


   /**
    * Creates a batch part with the given stock, standard unit of measure and alternate unit of
    * measure with the conversion factor.
    *
    * @param aStockNo
    * @param aStandardUnitOfMeasure
    * @param aAlternateUnitOfMeasure
    * @param aConversionFactor
    *           (x) where x standard unit of measure = 1 alternate unit of measure
    * @return
    */
   private PartNoKey createBatchPartWithAlternateUnitOfMeasure( StockNoKey aStockNo,
         RefQtyUnitKey aStandardUnitOfMeasure, RefQtyUnitKey aAlternateUnitOfMeasure,
         BigDecimal aConversionFactor ) {
      return new PartNoBuilder().withInventoryClass( RefInvClassKey.BATCH )
            .withStatus( RefPartStatusKey.ACTV ).withFinancialType( RefFinanceTypeKey.CONSUM )
            .withUnitType( aStandardUnitOfMeasure ).withTotalQuantity( new BigDecimal( 100.0 ) )
            .withTotalValue( BigDecimal.ONE ).withAssetAccount( iAccount ).withStock( aStockNo )
            .withAlternateUnitType( aAlternateUnitOfMeasure, aConversionFactor ).build();
   }


   private InventoryKey createInventory( PartNoKey aPart, LocationKey aLocation, Double aBinQty ) {
      return new InventoryBuilder().withPartNo( aPart ).withSerialNo( "batch-1234" )
            .withClass( RefInvClassKey.BATCH ).atLocation( aLocation ).withOwner( iOwnerKey )
            .withBinQt( aBinQty ).withReserveQt( 0.0 ).withCondition( RefInvCondKey.RFI ).build();
   }


   private void assertPOAutoIssuedAndAuthorizedByMxpurchase( PartRequestKey lPartRequest )
         throws Exception {
      ReqPartTable lReqPartPO = ReqPartTable.findByPrimaryKey( lPartRequest );
      PurchaseOrderKey lPOKey = lReqPartPO.getPurchaseOrder();
      assertNotNull( lPOKey );
      PoAuth lPoAuth = new PoAuth( new PurchaseOrderAuthKey( lPOKey, 1 ) );
      lPoAuth.assertAuthorizingHR( HumanResourceKey.MX_PURCHASE );
      EvtEventUtil lEvtEvent = new EvtEventUtil( lPOKey.getEventKey() );
      lEvtEvent.assertEventStatus( RefEventStatusKey.POISSUED );
      lEvtEvent.assertEditorHr( HumanResourceKey.MX_PURCHASE );
   }

}
