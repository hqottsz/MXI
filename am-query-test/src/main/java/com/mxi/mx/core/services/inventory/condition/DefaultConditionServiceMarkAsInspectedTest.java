package com.mxi.mx.core.services.inventory.condition;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.OrderBuilder;
import com.mxi.am.domain.builder.OrderLineBuilder;
import com.mxi.am.domain.builder.OwnerDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.PartRequestBuilder;
import com.mxi.am.domain.builder.ShipmentDomainBuilder;
import com.mxi.am.domain.builder.ShipmentLineBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.common.utils.ArrayUtils;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefFinanceTypeKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefOwnerTypeKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefPoLineTypeKey;
import com.mxi.mx.core.key.RefPoTypeKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefReqPriorityKey;
import com.mxi.mx.core.key.RefReqTypeKey;
import com.mxi.mx.core.key.RefShipmentTypeKey;
import com.mxi.mx.core.key.ShipmentKey;
import com.mxi.mx.core.services.order.OrderLineUtility;
import com.mxi.mx.core.table.eqp.EqpPartNoTable;
import com.mxi.mx.core.table.req.ReqPartTable;
import com.mxi.mx.core.unittest.MxAssert;
import com.mxi.mx.core.unittest.table.inv.InvInv;


/**
 * This class tests the
 * {@link DefaultConditionService#markAsInspected(InventoryKey, String, HumanResourceKey)} method.
 *
 * @author dsewell
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class DefaultConditionServiceMarkAsInspectedTest {

   private LocationKey iDockLocation;
   private LocationKey iAirport;

   private HumanResourceKey iHr;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Test that receiving the same inventory during an exchange order resets the ownership to LOCAL
    * when the owner is local.
    *
    * @throws Exception
    *            If an error occurs
    */
   @Test
   public void testThatReceivingSameInventoryInExchResetsOwnershipLocal() throws Exception {

      runWithSameInventoryReceivingAndAssert( new OwnerDomainBuilder().build(),
            RefOwnerTypeKey.LOCAL );
   }


   /**
    * Test that receiving the same inventory during an exchange order resets the ownership to OTHER
    * when the owner is non-local.
    *
    * @throws Exception
    *            If an error occurs
    */
   @Test
   public void testThatReceivingSameInventoryInExchResetsOwnershipOther() throws Exception {

      runWithSameInventoryReceivingAndAssert( new OwnerDomainBuilder().isNonLocal().build(),
            RefOwnerTypeKey.OTHER );
   }


   /**
    * Create data and simulate receiving the same inventory, mark as inspected, then assert the
    * result.
    *
    * @param aOwner
    *           the owner
    * @param aExpectedOwnerType
    *           the expected owner type
    *
    * @throws MxException
    *            if a mx error occurs
    * @throws TriggerException
    *            if a trigger error occurs
    */
   private void runWithSameInventoryReceivingAndAssert( OwnerKey aOwner,
         RefOwnerTypeKey aExpectedOwnerType ) throws MxException, TriggerException {

      final PurchaseOrderLineKey lOrderLine = new OrderLineBuilder(
            new OrderBuilder().withStatus( RefEventStatusKey.POISSUED ).build() )
                  .withLineType( RefPoLineTypeKey.EXCHANGE ).build();

      InventoryKey lInventory =
            new InventoryBuilder().withOwner( aOwner ).withOwnershipType( RefOwnerTypeKey.EXCHRCVD )
                  .atLocation( iDockLocation ).withOrderLine( lOrderLine ).build();

      new DefaultConditionService(
            new DefaultSerializedConditionService(
                  mockOrderLineUtility( lOrderLine, ArrayUtils.asList( lInventory ) ) ),
            null, null, null, null, null ).markAsInspected( lInventory, null, true, iHr );

      InvInv lInvInv = new InvInv( lInventory );
      lInvInv.assertOwner( aOwner );
      lInvInv.assertOwnerTypeCd( aExpectedOwnerType.getCd() );
   }


   /**
    * Mock up the OrderLineUtility service.
    *
    * @param aOrderLine
    *           the order line
    * @param aReturnInventoryList
    *           the return inventory list
    *
    * @return the mocked OrderLineUtility
    */
   private OrderLineUtility mockOrderLineUtility( final PurchaseOrderLineKey aOrderLine,
         final List<InventoryKey> aReturnInventoryList ) {

      Mockery lContext = new Mockery();

      final OrderLineUtility lMockOrderLineUtil = lContext.mock( OrderLineUtility.class );

      lContext.checking( new Expectations() {

         {

            // This creates the situation where the return inventory and the inspected inventory
            // are actually the same. This might be the case when a vendor repairs a part rather
            // than sending an alternate.
            one( lMockOrderLineUtil ).getReturnInventory( aOrderLine );
            will( returnValue( aReturnInventoryList ) );
         }
      } );

      return lMockOrderLineUtil;
   }


   /**
    * This method tests the scenario: When a batch repairable inventory is sent out and received
    * from a vendor on a Repair Order then marked as serviceable, the part total quantity should not
    * be changed. Reason: The inventory which was sent away is archived. The quantity of archived is
    * decreased from the total quantity. However, a new inventory record is created for the received
    * inventory. After mark as serviceable for the new inventory, the inventory bin quantity is
    * added to the part total quantity. the received inventory bin quantity equals to the archived
    * inventory bin quantity.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testThatBatchPartQuantityNotCorruptedAfterMarkReveivedInvAsServiceable()
         throws Exception {

      LocationKey lDocLoc = new LocationDomainBuilder().withType( RefLocTypeKey.DOCK ).build();
      LocationKey lVendorLoc = new LocationDomainBuilder().withType( RefLocTypeKey.VENDOR ).build();
      BigDecimal lRepairQty = BigDecimal.TEN;
      BigDecimal lAUP = BigDecimal.ONE;

      // create a new BATCH part number
      PartNoKey lBatchPart = new PartNoBuilder().withInventoryClass( RefInvClassKey.BATCH )
            .withStatus( RefPartStatusKey.ACTV ).withFinancialType( RefFinanceTypeKey.CONSUM )
            .withRepairBool( true ).withUnitType( RefQtyUnitKey.EA ).withTotalQuantity( lRepairQty )
            .withAverageUnitPrice( lAUP ).withTotalValue( lRepairQty.multiply( lAUP ) ).build();

      PurchaseOrderKey lOrder = new OrderBuilder().withOrderType( RefPoTypeKey.REPAIR )
            .withStatus( RefEventStatusKey.POISSUED ).build();

      PurchaseOrderLineKey lOrderLine = new OrderLineBuilder( lOrder )
            .withUnitType( RefQtyUnitKey.EA ).withUnitPrice( lAUP ).forPart( lBatchPart )
            .withLineType( RefPoLineTypeKey.REPAIR ).withOrderQuantity( lRepairQty ).build();

      InventoryKey lInventorySent =
            new InventoryBuilder().withOwner( new OwnerDomainBuilder().build() )
                  .withPartNo( lBatchPart ).withClass( RefInvClassKey.BATCH )
                  .atLocation( lVendorLoc ).withBinQt( lRepairQty.doubleValue() )
                  .withCondition( RefInvCondKey.INREP ).withOrderLine( lOrderLine ).build();

      InventoryKey lInventoryReceived =
            new InventoryBuilder().withOwner( new OwnerDomainBuilder().build() )
                  .withPartNo( lBatchPart ).withClass( RefInvClassKey.BATCH ).atLocation( lDocLoc )
                  .withBinQt( lRepairQty.doubleValue() ).withCondition( RefInvCondKey.INSPREQ )
                  .withOrderLine( lOrderLine ).build();

      // create outbound shipment for the order
      ShipmentKey lOutboundShipment = new ShipmentDomainBuilder().fromLocation( lDocLoc )
            .toLocation( lVendorLoc ).withType( RefShipmentTypeKey.SENDREP )
            .withStatus( RefEventStatusKey.IXCMPLT ).withHistoric( true ).build();

      // create a shipment line for the outbound shipment
      new ShipmentLineBuilder( lOutboundShipment ).forOrderLine( lOrderLine )
            .forInventory( lInventorySent ).build();

      new DefaultConditionService( null, new DefaultBatchConditionService(), null, null, null,
            null ).markAsInspected( lInventoryReceived, null, true, iHr );

      MxAssert.assertEquals( lRepairQty,
            EqpPartNoTable.findByPrimaryKey( lBatchPart ).getTotalQt() );
   }


   /**
    * Test that inspect as serviceable for serialized part does not break the locked reservation for
    * purchase order part-requests
    *
    * @throws Exception
    *            If an error occurs
    */
   @Test
   public void testThatSERInspectAsServiceableDoesNotBreakLockedRsrvForPOPartRequest()
         throws Exception {

      // create order line
      PurchaseOrderLineKey lOrderLine = createOrderLine( 1.0 );

      // create inventory with INSPREQ condition linked to order line
      InventoryKey lInventory =
            createInventory( RefInvClassKey.SER, lOrderLine ).isReserved().build();

      // create part-request linked with the order line and make it locked reservation
      PartRequestKey lPartRequest = createPartRequest( 1.0, lInventory, lOrderLine );;

      // WHEN: inventory is inspected as serviceable
      new DefaultConditionService(
            new DefaultSerializedConditionService(
                  mockOrderLineUtility( lOrderLine, ArrayUtils.asList( lInventory ) ) ),
            null, null, null, null, null ).markAsInspected( lInventory, null, true, iHr );

      // assert that inventory condition changes to RFI
      InvInv lInvInv = new InvInv( lInventory );
      lInvInv.assertCondCd( RefInvCondKey.RFI.getCd() );
      lInvInv.assertReservedBool( true );

      // assert that part request is still locked
      assertPartRequestLocked( lPartRequest );

   }


   /**
    * Test that inspect as serviceable for batch part (full bin quantity) does not break the locked
    * reservation for purchase order part-requests
    *
    * @throws Exception
    *            If an error occurs
    */
   @Test
   public void testThatBatchInspectAsServiceableDoesNotBreakLockedRsrvForPOPartRequest()
         throws Exception {

      final double QTY = 5.0;

      // create order line
      PurchaseOrderLineKey lOrderLine = createOrderLine( QTY );

      // create inventory with INSPREQ condition linked to order line
      InventoryKey lInventory = createInventory( RefInvClassKey.BATCH, lOrderLine ).withBinQt( QTY )
            .withReserveQt( QTY ).build();

      // create part-request linked with the order line and make it locked reservation
      PartRequestKey lPartRequest = createPartRequest( QTY, lInventory, lOrderLine );

      // WHEN: inventory is inspected as serviceable
      new DefaultConditionService( null, new DefaultBatchConditionService(), null, null, null,
            null ).markAsInspected( lInventory, null, true, iHr );

      // assert that inventory condition changes to RFI
      InvInv lInvInv = new InvInv( lInventory );
      lInvInv.assertCondCd( RefInvCondKey.RFI.getCd() );
      // for batch inventory we check reserve_qt instead of the reserved flag since this flag is not
      // set to true for batch items
      lInvInv.assertReservedBool( false );
      lInvInv.assertReservedQuantity( QTY );

      // assert that part request is still locked
      assertPartRequestLocked( lPartRequest );

   }


   // assert that part request is locked
   private void assertPartRequestLocked( PartRequestKey aPartRequest ) {
      ReqPartTable lReqPartTable = ReqPartTable.findByPrimaryKey( aPartRequest );
      assertTrue( lReqPartTable.getLockReserveBool() );
   }


   // create order line for the part
   private PurchaseOrderLineKey createOrderLine( double aQuantity ) {
      return new OrderLineBuilder(
            new OrderBuilder().withStatus( RefEventStatusKey.POISSUED ).build() )
                  .withLineType( RefPoLineTypeKey.PURCHASE )
                  .withPreInspQty( new BigDecimal( aQuantity ) ).build();
   }


   // create inventory and link with order line
   private InventoryBuilder createInventory( RefInvClassKey aInvClass,
         PurchaseOrderLineKey aOrderLine ) {
      return new InventoryBuilder().withOwner( new OwnerDomainBuilder().build() )
            .withOwnershipType( RefOwnerTypeKey.LOCAL ).atLocation( iDockLocation )
            .withClass( aInvClass ).withOrderLine( aOrderLine )
            .withCondition( RefInvCondKey.INSPREQ );
   }


   // create part request with reserved inventory and link with order line
   private PartRequestKey createPartRequest( double aRequestedQty, InventoryKey aInventory,
         PurchaseOrderLineKey aOrderLine ) {
      return new PartRequestBuilder().withType( RefReqTypeKey.ADHOC )
            .withRequestedQuantity( aRequestedQty ).requiredBy( DateUtils.addDays( new Date(), 2 ) )
            .withPriority( RefReqPriorityKey.NORMAL ).withStatus( RefEventStatusKey.PRINSPREQ )
            .isNeededAt( iAirport ).withReservedInventory( aInventory )
            .forPurchaseLine( aOrderLine ).isLockedReservation().build();
   }


   @Before
   public void loadData() throws Exception {
      iHr = new HumanResourceDomainBuilder().build();

      iAirport = new LocationDomainBuilder().withCode( "AIRPORT1" )
            .withType( RefLocTypeKey.AIRPORT ).isSupplyLocation().build();

      iDockLocation = new LocationDomainBuilder().withType( RefLocTypeKey.DOCK )
            .withParent( iAirport ).withSupplyLocation( iAirport ).build();

   }

}
