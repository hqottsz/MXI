package com.mxi.mx.core.services.shipment;

import static com.mxi.mx.common.utils.DateUtils.addDays;
import static com.mxi.mx.common.utils.DateUtils.floorSecond;
import static com.mxi.mx.core.key.RefEventStatusKey.IXINTR;
import static com.mxi.mx.core.key.RefEventStatusKey.IXPEND;
import static com.mxi.mx.core.key.RefPartTypeKey.COMNHW;
import static com.mxi.mx.core.key.RefShipSegmentStatusKey.INTRANSIT;
import static com.mxi.mx.core.key.RefShipSegmentStatusKey.PENDING;
import static com.mxi.mx.core.key.RefShipSegmentStatusKey.PLAN;
import static com.mxi.mx.core.key.RefShipmentTypeKey.PURCHASE;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.CurrencyBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.OrderBuilder;
import com.mxi.am.domain.builder.OrderLineBuilder;
import com.mxi.am.domain.builder.OwnerDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.PartRequestBuilder;
import com.mxi.am.domain.builder.ShipmentLineBuilder;
import com.mxi.am.domain.builder.VendorBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.am.ee.OperateAsUserRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.i18n.i18n;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.RefAbcClassKey;
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
import com.mxi.mx.core.key.RefShipSegmentStatusKey;
import com.mxi.mx.core.key.RefShipmentTypeKey;
import com.mxi.mx.core.key.ShipSegmentKey;
import com.mxi.mx.core.key.ShipmentKey;
import com.mxi.mx.core.key.ShipmentLineKey;
import com.mxi.mx.core.key.VendorKey;
import com.mxi.mx.core.services.event.stage.ReasonAndNoteTO;
import com.mxi.mx.core.services.inventory.condition.DefaultConditionService;
import com.mxi.mx.core.services.inventory.phys.InspectInventoryTO;
import com.mxi.mx.core.services.inventory.phys.PhysicalInventoryService;
import com.mxi.mx.core.services.inventory.phys.ReturnToVendorTO;
import com.mxi.mx.core.services.order.exception.CannotChangeShippingLocationException;
import com.mxi.mx.core.services.part.PartNoService;
import com.mxi.mx.core.services.shipment.routing.DuplicateRoutingLocationException;
import com.mxi.mx.core.table.evt.EvtEventDao;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.evt.EvtLocTable;
import com.mxi.mx.core.table.evt.EvtStageDao;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.ref.RefQtyUnit;
import com.mxi.mx.core.table.ship.ShipSegmentTable;
import com.mxi.mx.core.table.ship.ShipShipmentLineTable;
import com.mxi.mx.core.unittest.table.evt.EvtEventUtil;
import com.mxi.mx.core.unittest.table.inv.InvInv;
import com.mxi.mx.core.unittest.table.po.PoLine;
import com.mxi.mx.core.unittest.table.req.ReqPart;
import com.mxi.mx.core.unittest.table.ship.ShipShipment;
import com.mxi.mx.core.unittest.table.ship.ShipShipmentLine;
import com.mxi.mx.domain.shipment.Segment;
import com.mxi.mx.repository.shipment.JdbcShipmentRepository;


public final class ShipmentServiceTest {

   private static final BigDecimal BIN_QTY = BigDecimal.TEN;
   private static final String USERNAME_TESTUSER = "testuser";
   private static final int USERID_TESTUSER = 999;
   private static final String FINAL_DESTINATION_CHANGED_MSG = "core.msg.FINAL_DESTINATION_CHANGED";
   private static final String ROUTING_INFORMATION_CHANGED_MSG =
         "core.msg.ROUTING_INFORMATION_CHANGED";
   private static final String OLDSHIPTO = "OLDSHIPTO";
   private static final String OLDREEXPEDITETO = "OLDREEXPEDITETO";
   private static final String FIRSTROUTING = "FIRSTROUTING";
   private static final String SECONDROUTING = "SECONDROUTING";
   private static final String THIRDROUTING = "THIRDROUTING";
   private static final String PO123456 = "PO123456";
   private static final String DOCK = "DOCK";
   private static final String NEWREEXPEDITETO = "NEWREEXPEDITETO";
   private static final String NEWSHIPTO = "NEWSHIPTO";
   private static final RefQtyUnitKey GALLONS = new RefQtyUnitKey( 0, "GAL" );
   private static final int RECEIVEDQTY = 1;
   private static final String POLINEQTYCD = "GAL";
   private static final int ORDERLINENO = 1;
   private static final double SHIPMENTLINEEXPECTEDQTY = 0.2;
   private static final String SHIPMENTLINEQTYCD = "EA";
   private static final BigDecimal CONVERSION_FACTOR = new BigDecimal( 4 );
   private static final Date RECEIVED_DATE = floorSecond( addDays( new Date(), -1 ) );
   private static final Date SHIPMENT_DATE = floorSecond( addDays( RECEIVED_DATE, -1 ) );
   private static final String USER_NOTE = "userNote";
   private static final String SYSTEM_NOTE = null;

   private LocationKey iDockLocation;
   private HumanResourceKey iHr;
   private LocationKey iSupplyLocation;
   private LocationKey iVendorLocation;
   private LocationKey iBrokerLocation;
   private PurchaseOrderLineKey iRepairOrderLine;
   private PartNoKey iBatchPart;
   private VendorKey iVendor;
   private OwnerKey iInventoryOwner;
   private PartNoKey iTrackPart;
   private PurchaseOrderLineKey iPoLine;
   private LocationKey iNewShipToLocation;
   private LocationKey iNewReExpediteToLocation;
   private LocationKey iOldReExpediteToLocation;
   private LocationKey iOldShipToLocation;
   private LocationKey iFirstRoutingLocation;
   private LocationKey iSecondRoutingLocation;
   private LocationKey iThirdRoutingLocation;
   private PurchaseOrderKey iOrder;
   private ShipmentKey iInboundShipmentKey;
   private ShipmentKey iShipment;
   private PurchaseOrderKey iOrderWithReExpediteTo;
   private ShipmentKey iInboundShipmentForOrderWithReExpediteToKey;

   private ShipmentService iShipmentService;

   private EvtEventDao iEvtEventDao;
   private EvtStageDao iEvtStageDao;

   @Rule
   public OperateAsUserRule iOperateAsUserRule =
         new OperateAsUserRule( USERID_TESTUSER, USERNAME_TESTUSER );
   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public final ExpectedException iThrown = ExpectedException.none();


   /**
    * Creates a batch part. Creates a part request for that part. Creates an order with one line for
    * the part request. Creates an inbound shipment for the order. Receives the shipment with
    * default routing condition. Ensures that the received inventory is reserved against the part
    * request and the part request is marked as PRAVAIL.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testThatPartReqMarkedAvailWhenRcvdInvWithRfiRoute() throws Exception {

      // create a new BATCH part number
      PartNoKey lBatchPart = defaultPart().withAverageUnitPrice( BigDecimal.ONE ).build();

      // create a part request
      PartRequestKey lPartRequest = defaultPartRequest();

      // create an order for the part request
      PurchaseOrderKey lOrder = defaultOrder();
      PurchaseOrderLineKey lOrderLine = defaultOrderLine( lOrder, lPartRequest )
            .withAccruedValue( BigDecimal.valueOf( 3.0 ) ).build();

      // create a shipment line for the shipment
      ShipmentLineKey lShipmentLine =
            defaultShipmentLine( iShipment, lOrderLine ).forPart( lBatchPart ).build();

      // setup the shipment receipt
      ReceiveShipmentLineTO lShipmentLineTO = new ReceiveShipmentLineTO( lShipmentLine );
      lShipmentLineTO.setPartNo( lBatchPart );
      lShipmentLineTO.setReceivedQty( 5.0 );

      // receive the shipment
      ReceiveShipmentLineTO[] lReceiveShipmentLines =
            new ReceiveShipmentLineTO[] { lShipmentLineTO };
      ShipmentService.receive( iShipment, new Date(), lReceiveShipmentLines, null, iHr );

      // get the inventory that was received
      InventoryKey lReceivedInventory = new ShipShipmentLine( lShipmentLine ).getInventory();

      // ensure the part request is marked as AVAIL
      new EvtEventUtil( lPartRequest ).assertEventStatus( RefEventStatusKey.PRAVAIL );

      // ensure the inventory is reserved against the part request
      new ReqPart( lPartRequest ).assertInventoryKey( lReceivedInventory );
   }


   /**
    * Creates a batch part. Creates a part request for that part. Creates an order with one line for
    * the part request. Creates an inbound shipment for the order. Receives the shipment with
    * routing condition if INSPREQ. Ensures that the received inventory is reserved against the part
    * request and the part request is marked as PRINSPREQ.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testThatPartReqMarkedInspreqWhenRcvdInvWithInspRoute() throws Exception {

      // create a new BATCH part
      PartNoKey lBatchPart = defaultPart().build();

      // create a part request
      PartRequestKey lPartRequest = defaultPartRequest();

      // create a purchase order for the part request
      PurchaseOrderKey lOrder = defaultOrder();
      PurchaseOrderLineKey lOrderLine = defaultOrderLine( lOrder, lPartRequest ).build();

      // create a shipment line for the shipment
      ShipmentLineKey lShipmentLine =
            defaultShipmentLine( iShipment, lOrderLine ).forPart( lBatchPart ).build();

      // setup the shipment receipt
      ReceiveShipmentLineTO lShipmentLineTO = new ReceiveShipmentLineTO( lShipmentLine );
      lShipmentLineTO.setPartNo( lBatchPart );
      lShipmentLineTO.setReceivedQty( 5.0 );
      lShipmentLineTO.setRouteCond( RefInvCondKey.INSPREQ, "route condition" );

      // receive the shipment
      ReceiveShipmentLineTO[] lReceiveShipmentLines =
            new ReceiveShipmentLineTO[] { lShipmentLineTO };
      ShipmentService.receive( iShipment, new Date(), lReceiveShipmentLines, null, iHr );

      // get the received inventory
      InventoryKey lReceivedInventory = new ShipShipmentLine( lShipmentLine ).getInventory();

      // ensure the part request is marked as AVAIL
      new EvtEventUtil( lPartRequest ).assertEventStatus( RefEventStatusKey.PRINSPREQ );

      // ensure the inventory is reserved against the part request
      new ReqPart( lPartRequest ).assertInventoryKey( lReceivedInventory );
   }


   /**
    * Sets up a quarantine location. Creates a batch part. Creates a part request for that part.
    * Creates an order with one line for the part request. Creates an inbound shipment for the
    * order. Receives the shipment with routing condition of QUAR. Ensures that the received
    * inventory is reserved against the part request and the part request is marked as PRQUAR.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testThatPartReqMarkedQuarWhenRcvdInvWithQuarRoute() throws Exception {

      // build a quarantine location
      Domain.createLocation( loc -> {
         loc.setType( RefLocTypeKey.QUAR );
         loc.setSupplyLocation( iSupplyLocation );
      } );

      // create a new BATCH part
      PartNoKey lBatchPart = defaultPart().withAbcClass( RefAbcClassKey.D ).build();

      // create a part request
      PartRequestKey lPartRequest = defaultPartRequest();

      // create a purchase order for the part request
      PurchaseOrderKey lOrder = defaultOrder();
      PurchaseOrderLineKey lOrderLine = defaultOrderLine( lOrder, lPartRequest ).build();

      // create a shipment line for the shipment
      ShipmentLineKey lShipmentLine =
            defaultShipmentLine( iShipment, lOrderLine ).forPart( lBatchPart ).build();

      // setup the shipment receipt
      ReceiveShipmentLineTO lShipmentLineTO = new ReceiveShipmentLineTO( lShipmentLine );
      lShipmentLineTO.setPartNo( lBatchPart );
      lShipmentLineTO.setReceivedQty( 5.0 );
      lShipmentLineTO.setRouteCond( RefInvCondKey.QUAR, "route condition" );

      // receive the shipment
      ReceiveShipmentLineTO[] lReceiveShipmentLines =
            new ReceiveShipmentLineTO[] { lShipmentLineTO };
      ShipmentService.receive( iShipment, new Date(), lReceiveShipmentLines, null, iHr );

      // get the received inventory
      InventoryKey lReceivedInventory = new ShipShipmentLine( lShipmentLine ).getInventory();

      // ensure the part request is marked as QUAR
      new EvtEventUtil( lPartRequest ).assertEventStatus( RefEventStatusKey.PRQUAR );

      // ensure the inventory is reserved against the part request
      new ReqPart( lPartRequest ).assertInventoryKey( lReceivedInventory );
   }


   /**
    * Sets up a quarantine location. Creates a batch part. Creates a purchase order for that part.
    * Creates an inbound shipment for the order. Receives the shipment with excess quantity. Ensures
    * that the received inventory is split into two batches, with the excess going to QUAR.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testThatExcessBatchIsQuarantinedForShipmentWithPO() throws Exception {

      int lDecimalPlaces = 2;
      int lRoundingMode = BigDecimal.ROUND_HALF_EVEN;

      // create a quantity unit with decimal places
      RefQtyUnit lLiters = RefQtyUnit.create( "L" );
      lLiters.setDecimalPlacesQt( lDecimalPlaces );
      RefQtyUnitKey lLiterUnit = lLiters.insert();

      // set up the numbers so the inventory will be over received
      BigDecimal lOrderQty = BigDecimal.valueOf( 9.99 ).setScale( lDecimalPlaces, lRoundingMode );
      BigDecimal lReceivedQty =
            BigDecimal.valueOf( 12.11 ).setScale( lDecimalPlaces, lRoundingMode );
      // (excess qty = received qty - ordered quantity)
      BigDecimal lExcess = lReceivedQty.subtract( lOrderQty );

      // build a quarantine location
      LocationKey lQuarLocation = Domain.createLocation( loc -> {
         loc.setType( RefLocTypeKey.QUAR );
         loc.setSupplyLocation( iSupplyLocation );
      } );

      // create a new BATCH part
      PartNoKey lBatchPart = new PartNoBuilder().withInventoryClass( RefInvClassKey.BATCH )
            .withStatus( RefPartStatusKey.ACTV ).withFinancialType( RefFinanceTypeKey.CONSUM )
            .withUnitType( lLiterUnit ).withTotalQuantity( BigDecimal.TEN )
            .withTotalValue( BigDecimal.TEN ).build();

      // create a purchase order for the part request
      PurchaseOrderKey lOrder = defaultOrder();
      PurchaseOrderLineKey lOrderLine = new OrderLineBuilder( lOrder ).withUnitType( lLiterUnit )
            .withUnitPrice( BigDecimal.TEN ).withOrderQuantity( lOrderQty )
            .withAccruedValue( BigDecimal.TEN ).forPart( lBatchPart ).build();

      // create a shipment line for the shipment
      ShipmentLineKey lShipmentLine = new ShipmentLineBuilder( iShipment ).forPart( lBatchPart )
            .forOrderLine( lOrderLine ).withExpectedQuantity( lOrderQty.doubleValue() ).build();

      // setup the shipment receipt
      ReceiveShipmentLineTO lShipmentLineTO = new ReceiveShipmentLineTO( lShipmentLine );
      lShipmentLineTO.setPartNo( lBatchPart );
      // order an excess
      lShipmentLineTO.setReceivedQty( lReceivedQty.doubleValue() );
      lShipmentLineTO.setRouteCond( RefInvCondKey.QUAR, "route condition" );

      // receive the shipment
      ReceiveShipmentLineTO[] lReceiveShipmentLines =
            new ReceiveShipmentLineTO[] { lShipmentLineTO };
      ShipmentService.receive( iShipment, new Date(), lReceiveShipmentLines, null, iHr );

      // the received inventory should be in two batches
      InventoryKey[] lInventories = PartNoService.getInventory( lBatchPart );
      assertEquals( "there should be 2 batches", lDecimalPlaces, lInventories.length );

      for ( InventoryKey lInventory : lInventories ) {

         InvInvTable lInvInv = InvInvTable.findByPrimaryKey( lInventory );
         BigDecimal lBinQty = BigDecimal.valueOf( lInvInv.getBinQt() ).setScale( 2, lRoundingMode );

         if ( lQuarLocation.equals( lInvInv.getLocation() ) ) {
            assertEquals( "quarantined qty = excess qty = received qty - ordered qty", lExcess,
                  lBinQty );
         } else {
            assertEquals( lInventory, new ShipShipmentLine( lShipmentLine ).getInventory() );
            assertEquals( "RFI qty = ordered qty", lOrderQty, lBinQty );
         }
      }
   }


   @Test
   public void testCompletedShipmentsDoNotOverrideEstimatedArrivalDate() throws Exception {
      // create a shipment line
      ShipmentKey lShipment = Domain.createShipment( shipmnt -> {
         shipmnt.setType( PURCHASE );
         shipmnt.setShipmentDate( new Date() );
         shipmnt.addShipmentSegment( segment -> {
            segment.setFromLocation( iVendorLocation );
            segment.setToLocation( iDockLocation );
            segment.setStatus( PENDING );
         } );
      } );
      PartNoKey lBatchPart = defaultPart().build();
      PurchaseOrderLineKey lOrderLine = defaultOrderLine();
      ShipmentLineKey lShipmentLine =
            defaultShipmentLine( lShipment, lOrderLine ).forPart( lBatchPart ).build();

      // receive the shipment
      ReceiveShipmentLineTO lShipmentLineTO = new ReceiveShipmentLineTO( lShipmentLine );
      lShipmentLineTO.setPartNo( lBatchPart );
      lShipmentLineTO.setReceivedQty( 5.0 );
      lShipmentLineTO.setRouteCond( RefInvCondKey.INSPREQ, "route condition" );
      ReceiveShipmentLineTO[] lReceiveShipmentLines =
            new ReceiveShipmentLineTO[] { lShipmentLineTO };
      ShipmentService.receive( lShipment, new Date(), lReceiveShipmentLines, null, iHr );
      Date lCompletedDate = getShipmentArrivalDate( lShipment );

      // Act
      ShipmentTO lShipmentTO = new ShipmentTO();
      lShipmentTO.setPriority( RefReqPriorityKey.NORMAL, "" );
      lShipmentTO.setShipmentType( RefShipmentTypeKey.PURCHASE, "" );
      lShipmentTO.setShipFrom( iVendorLocation, "" );
      lShipmentTO.setShipTo( DOCK, "" );
      lShipmentTO.setShipByDate( lCompletedDate, "" );

      lShipmentTO.setEstArrivalDate( null );

      ShipmentService.set( lShipment, lShipmentTO, iHr );

      // Assert
      assertEquals( lCompletedDate, getShipmentArrivalDate( lShipment ) );
   }


   private ShipmentLineBuilder defaultShipmentLine( ShipmentKey aShipment,
         PurchaseOrderLineKey aOrderLine ) {
      ShipmentLineBuilder lBuilder =
            new ShipmentLineBuilder( aShipment ).withExpectedQuantity( 5.0 );
      if ( aOrderLine != null && aOrderLine.isValid() ) {
         return lBuilder.forOrderLine( aOrderLine );
      }
      return lBuilder;
   }


   private ShipmentKey createPendingShipment() {
      return Domain.createShipment( shipment -> {
         shipment.setStatus( RefEventStatusKey.IXPEND );
         shipment.addShipmentSegment( segment -> {
            segment.setFromLocation( iVendorLocation );
            segment.setToLocation( iDockLocation );
            segment.setStatus( RefShipSegmentStatusKey.PENDING );
         } );
      } );
   }


   private PurchaseOrderLineKey defaultOrderLine() {
      PartRequestKey lPartRequest = defaultPartRequest();
      PurchaseOrderKey lOrder = defaultOrder();

      return new OrderLineBuilder( lOrder ).forPartRequest( lPartRequest )
            .withUnitType( RefQtyUnitKey.EA ).withUnitPrice( BigDecimal.TEN )
            .withOrderQuantity( BigDecimal.valueOf( 5.0 ) ).forPart( defaultPart().build() )
            .build();
   }


   private OrderLineBuilder defaultOrderLine( PurchaseOrderKey aOrder,
         PartRequestKey aPartRequest ) {
      return new OrderLineBuilder( aOrder ).forPartRequest( aPartRequest )
            .withUnitType( RefQtyUnitKey.EA ).forPart( defaultPart().build() )
            .withUnitPrice( BigDecimal.TEN ).withOrderQuantity( BigDecimal.valueOf( 5.0 ) );
   }


   private PurchaseOrderKey defaultOrder() {
      return new OrderBuilder().build();
   }


   private PartRequestKey defaultPartRequest() {
      return new PartRequestBuilder().withType( RefReqTypeKey.ADHOC ).withRequestedQuantity( 5.0 )
            .isNeededAt( iSupplyLocation ).build();
   }


   private PartNoBuilder defaultPart() {
      return new PartNoBuilder().withInventoryClass( RefInvClassKey.BATCH )
            .withStatus( RefPartStatusKey.ACTV ).withFinancialType( RefFinanceTypeKey.CONSUM )
            .withUnitType( RefQtyUnitKey.EA ).withTotalQuantity( BigDecimal.ONE )
            .withTotalValue( BigDecimal.ONE );
   }


   /**
    * Gets the shipment estimated arrival date
    *
    * @param aShipmentKey
    *           the shipment key
    * @return the shipment estimated arrival date
    */
   private Date getShipmentArrivalDate( ShipmentKey aShipmentKey ) {
      return iEvtEventDao.findByPrimaryKey( aShipmentKey ).getEventDate();
   }


   private List<ShipmentLineKey> addShipmentLines( ShipmentKey aShipment, PartNoKey aPartNo,
         double aQuantity ) throws MxException {
      return ShipmentLineService.addShipmentLine( aShipment, aPartNo, null, aQuantity, null );
   }


   /**
    * Test the inventory owner is not changed after receive inventory from the vendor.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testOwnerNotChangedAfterReceiveInboundShipmentInRepairOrder() throws Exception {

      // create REPREQ inventory
      InventoryKey lRepairInv = createRepairInventory( iInventoryOwner, iVendorLocation );

      // create a pending inbound shipment for the order
      ShipmentKey lPendingInboundShipment = Domain.createShipment( shipment -> {
         shipment.setType( RefShipmentTypeKey.REPAIR );
         shipment.addShipmentSegment( segment -> {
            segment.setFromLocation( iVendorLocation );
            segment.setToLocation( iDockLocation );
            segment.setStatus( PENDING );
         } );
      } );
      ShipmentLineKey lPendingInboundShipmentLine =
            new ShipmentLineBuilder( lPendingInboundShipment ).forPart( iTrackPart )
                  .forOrderLine( iPoLine ).withExpectedQuantity( 1.0 ).build();

      // setup the shipment receipt
      ReceiveShipmentLineTO lShipmentLineTO =
            new ReceiveShipmentLineTO( lPendingInboundShipmentLine );
      lShipmentLineTO.setPartNo( iTrackPart );
      lShipmentLineTO.setReceivedQty( 1.0 );

      // receive the shipment
      ShipmentService.receive( lPendingInboundShipment, new Date(),
            new ReceiveShipmentLineTO[] { lShipmentLineTO }, null, iHr );

      // ensure the inventory owner is not changed to the one in po line, and ownership type is not
      // changed
      assertOwner( iInventoryOwner, lRepairInv );
   }


   /**
    * Test the inventory owner is not changed after send inventory to the vendor.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testOwnerNotChangedAfterSendOutboundShipmentInRepairOrder() throws Exception {

      // make the owner different than the one in po line
      InventoryKey lRepairInv = createRepairInventory( iInventoryOwner, iDockLocation );

      // create a pending outbound shipment for the order
      ShipmentKey lPendingOutboundShipment = Domain.createShipment( shipment -> {
         shipment.setType( RefShipmentTypeKey.SENDREP );
         shipment.setStatus( IXPEND );
         shipment.setHistorical( false );
         shipment.addShipmentSegment( segment -> {
            segment.setFromLocation( iDockLocation );
            segment.setToLocation( iVendorLocation );
            segment.setStatus( PENDING );
         } );
      } );
      new ShipmentLineBuilder( lPendingOutboundShipment ).forPart( iTrackPart )
            .forInventory( lRepairInv ).forOrderLine( iPoLine ).withExpectedQuantity( 1.0 ).build();

      ShipmentService.send( lPendingOutboundShipment, iHr, new Date(), new Date(), null, null );

      assertOwner( iInventoryOwner, lRepairInv );
   }


   /*
    * Tests that CannotShipToBrokerLocationException is thrown when the ship-to location is a
    * broker's location.
    *
    * @throws Exception if an error occurs
    */
   @Test
   public void testCannotShipToBrokerLocationException() throws Exception {

      // make the owner different than the one in po line
      InventoryKey lRepairInv = createRepairInventory( iInventoryOwner, iDockLocation );

      // create a pending outbound shipment for the order
      ShipmentKey lPendingOutboundShipment = Domain.createShipment( shipment -> {
         shipment.setType( RefShipmentTypeKey.SENDREP );
         shipment.setStatus( RefEventStatusKey.IXPEND );
         shipment.setHistorical( false );
         shipment.addShipmentSegment( segment -> {
            segment.setFromLocation( iDockLocation );
            segment.setToLocation( iBrokerLocation );
            segment.setStatus( RefShipSegmentStatusKey.PENDING );
         } );
      } );
      new ShipmentLineBuilder( lPendingOutboundShipment ).forPart( iTrackPart )
            .forInventory( lRepairInv ).forOrderLine( iPoLine ).withExpectedQuantity( 1.0 ).build();

      iThrown.expect( CannotShipToBrokerLocationException.class );
      ShipmentService.send( lPendingOutboundShipment, iHr, new Date(), new Date(), null, null );
   }


   /**
    * Test HasPendingSENDREPShipmentToVendorException is thrown when marking an inventory as
    * serviceable while it has pending SENDREP shipment to vendor (the return to vendor shipment
    * hasn't been completed yet).
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testHasPendingSENDREPShipmentToVendorException() throws Exception {

      // create INSPREQ inventory
      InventoryKey lReceivedInv =
            new InventoryBuilder().withOwner( new OwnerDomainBuilder().build() )
                  .withOwnershipType( RefOwnerTypeKey.LOCAL ).atLocation( iDockLocation )
                  .withClass( RefInvClassKey.BATCH ).withBinQt( BIN_QTY.doubleValue() )
                  .withCondition( RefInvCondKey.INSPREQ ).withOrderLine( iRepairOrderLine ).build();

      // create a pending outbound shipment for the order
      ShipmentKey lPendingOutboundShipment = Domain.createShipment( shipment -> {
         shipment.setType( RefShipmentTypeKey.SENDREP );
         shipment.setStatus( RefEventStatusKey.IXPEND );
         shipment.setHistorical( false );
         shipment.addShipmentSegment( segment -> {
            segment.setFromLocation( iDockLocation );
            segment.setToLocation( iVendorLocation );
            segment.setStatus( RefShipSegmentStatusKey.PENDING );
         } );
      } );
      new ShipmentLineBuilder( lPendingOutboundShipment ).forInventory( lReceivedInv )
            .forOrderLine( iRepairOrderLine ).withExpectedQuantity( BIN_QTY.doubleValue() ).build();

      try {

         new DefaultConditionService().inspectInventory( lReceivedInv, new InspectInventoryTO(),
               iHr );

         fail( "Expect HasPendingSENDREPShipmentToVendorException." );
      } catch ( HasPendingSENDREPShipmentToVendorException e ) {
         ;
      }

   }


   /**
    * Test the send inventory in completed outbound shipment is archived for repair order if the
    * repair part is receveid from the vendor.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testSentInventoryArchivedAfterReceiveFromVendor() throws Exception {

      Date lManufacturedDate = Calendar.getInstance().getTime();

      // create INREP inventory
      InventoryKey lRepairInv = new InventoryBuilder().withPartNo( iBatchPart )
            .manufacturedOn( lManufacturedDate ).withOwnershipType( RefOwnerTypeKey.LOCAL )
            .atLocation( iVendorLocation ).withClass( RefInvClassKey.BATCH )
            .withBinQt( BIN_QTY.doubleValue() ).withCondition( RefInvCondKey.INREP ).build();

      // create a completed outbound shipment for the order
      ShipmentKey lCompletedOutboundShipment = Domain.createShipment( shipment -> {
         shipment.setType( RefShipmentTypeKey.SENDREP );
         shipment.setStatus( RefEventStatusKey.IXCMPLT );
         shipment.setHistorical( true );
         shipment.addShipmentSegment( segment -> {
            segment.setFromLocation( iDockLocation );
            segment.setToLocation( iVendorLocation );
            segment.setStatus( RefShipSegmentStatusKey.PENDING );
         } );
      } );
      new ShipmentLineBuilder( lCompletedOutboundShipment ).forInventory( lRepairInv )
            .forOrderLine( iRepairOrderLine ).withExpectedQuantity( BIN_QTY.doubleValue() ).build();

      // create a pending inbound shipment for the order
      ShipmentKey lPendingInboundShipment = Domain.createShipment( shipment -> {
         shipment.setType( RefShipmentTypeKey.REPAIR );
         shipment.setStatus( RefEventStatusKey.IXPEND );
         shipment.setHistorical( false );
         shipment.addShipmentSegment( segment -> {
            segment.setFromLocation( iVendorLocation );
            segment.setToLocation( iDockLocation );
            segment.setStatus( RefShipSegmentStatusKey.PENDING );
         } );
      } );
      ShipmentLineKey lPendingInboundShipmentLine =
            new ShipmentLineBuilder( lPendingInboundShipment ).forPart( iBatchPart )
                  .forInventory( lRepairInv ).forOrderLine( iRepairOrderLine )
                  .withExpectedQuantity( BIN_QTY.doubleValue() ).build();

      // setup the shipment receipt
      ReceiveShipmentLineTO lShipmentLineTO =
            new ReceiveShipmentLineTO( lPendingInboundShipmentLine );
      lShipmentLineTO.setPartNo( iBatchPart );
      lShipmentLineTO.setReceivedQty( BIN_QTY.doubleValue() );
      lShipmentLineTO.setInventory( lRepairInv );
      lShipmentLineTO.setManufacturedDate( lManufacturedDate );

      // receive the shipment
      ShipmentService.receive( lPendingInboundShipment, new Date(),
            new ReceiveShipmentLineTO[] { lShipmentLineTO }, null, iHr );

      // ensure the sent inventory is archived
      new InvInv( lRepairInv ).assertCondCd( RefInvCondKey.ARCHIVE.getCd() );
   }


   /**
    * Test the remove shipment line logic to reset the shipment line number sequence if the line
    * being removed is the highest line number in the shipment.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testRemoveShipmentLineResetsShipmentLineNoOrder() throws Exception {
      // create a pending shipment
      ShipmentKey lPendingShipment = createPendingShipment();

      // add 3 shipment lines for tracked part
      List<ShipmentLineKey> lShipmentLines = addShipmentLines( lPendingShipment, iTrackPart, 3.0 );

      // assert that 3 lines were added
      assertEquals( 3, lShipmentLines.size() );

      // WHEN: remove highest shipment line (shipment line number 3)
      ShipmentService.removeShipmentLine( new ShipmentLineKey[] { lShipmentLines.get( 2 ) }, iHr,
            false, false );

      // call add shipment line logic to check newly added shipment line order number is 3 not 4
      List<ShipmentLineKey> lNewShipmentLine =
            addShipmentLines( lPendingShipment, iTrackPart, 1.0 );

      // assert that the shipment line number is 3 not 4, which means the sequence was reset.
      assertShipmentLineNoOrder( 3, lNewShipmentLine.get( 0 ) );
   }


   /**
    * Test the remove shipment line logic does not reset the shipment line number sequence if the
    * line being removed is not the highest line number in the shipment.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testRemoveShipmentLineDoesNotResetShipmentLineNoOrder() throws Exception {
      // create a pending shipment
      ShipmentKey lPendingShipment = createPendingShipment();

      // add 3 shipment lines for tracked part
      List<ShipmentLineKey> lShipmentLines = addShipmentLines( lPendingShipment, iTrackPart, 3.0 );

      // assert that 3 lines were added
      assertEquals( 3, lShipmentLines.size() );

      // WHEN: remove first shipment line number (shipment line number 1)
      ShipmentService.removeShipmentLine( new ShipmentLineKey[] { lShipmentLines.get( 0 ) }, iHr,
            false, false );

      // add 1 more shipment line and check newly added shipment line order number is 4
      List<ShipmentLineKey> lNewShipmentLine =
            addShipmentLines( lPendingShipment, iTrackPart, 1.0 );

      // assert that the shipment line number is 4 now, which means the sequence was not reset after
      // removal of shipment line number 1
      assertShipmentLineNoOrder( 4, lNewShipmentLine.get( 0 ) );
   }


   private void assertShipmentLineNoOrder( int aLineNoOrder, ShipmentLineKey aShipmentLine ) {
      ShipShipmentLineTable lShipShipmentLineTable =
            ShipShipmentLineTable.findByPrimaryKey( aShipmentLine );
      assertEquals( aLineNoOrder, lShipShipmentLineTable.getLineNoOrd() );
   }


   /**
    * Test the returning INSPREQ inventory to vendor.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testReturnInventoryToVendor() throws Exception {

      // create inventory to be returned
      InventoryKey lReturnInv = new InventoryBuilder().withOwnershipType( RefOwnerTypeKey.LOCAL )
            .atLocation( iDockLocation ).withClass( RefInvClassKey.BATCH )
            .withBinQt( BIN_QTY.doubleValue() ).withCondition( RefInvCondKey.RFI )
            .withOrderLine( iRepairOrderLine ).build();

      // create a completed inbound shipment for the order
      ShipmentKey lCompletedInboundShipment = Domain.createShipment( shipment -> {
         shipment.setType( RefShipmentTypeKey.REPAIR );
         shipment.setStatus( RefEventStatusKey.IXCMPLT );
         shipment.setHistorical( true );
         shipment.addShipmentSegment( segment -> {
            segment.setFromLocation( iVendorLocation );
            segment.setToLocation( iDockLocation );
            segment.setStatus( RefShipSegmentStatusKey.PENDING );
         } );
      } );
      new ShipmentLineBuilder( lCompletedInboundShipment ).forInventory( lReturnInv )
            .forOrderLine( iRepairOrderLine ).withExpectedQuantity( BIN_QTY.doubleValue() ).build();

      BigDecimal lPreReceivedQty = new PoLine( iRepairOrderLine ).getReceivedQt();

      ReasonAndNoteTO lRNTO = new ReasonAndNoteTO();
      lRNTO.setAuthorizingHr( iHr );
      lRNTO.setNote( "Note" );

      ReturnToVendorTO lRtnVend = new ReturnToVendorTO();
      lRtnVend.setReturnQuantity( BIN_QTY.doubleValue(), null );
      lRtnVend.setVendor( iVendor );
      lRtnVend.setReasonAndNote( lRNTO, "Reason and Label" );

      // return to vendor the inventory
      new PhysicalInventoryService( lReturnInv ).returnToVendor( lRtnVend, iHr );

      // ensure the return inventory is still INSPREQ
      new InvInv( lReturnInv ).assertCondCd( RefInvCondKey.RFI.getCd() );

      // ensure the po line received quantity is not changed
      new PoLine( iRepairOrderLine ).assertReceivedQt( lPreReceivedQty );

      // ensure a pending SENDREP outbound shipment with the return inventory was created
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lReturnInv, "aInventoryDbId", "aInventoryId" );
      lArgs.add( iDockLocation, "aLocDbId", "aLocId" );

      QuerySet lQs = QuerySetFactory.getInstance()
            .executeQuery( "com.mxi.mx.core.query.shipment.GetShipmentsByInventory", lArgs );

      assertNotNull( lQs );
      assertTrue( lQs.next() );
      new ShipShipment( lQs.getKey( ShipmentKey.class, "evt_event_key" ) )
            .assertShipmentType( RefShipmentTypeKey.SENDREP );
      new EvtEventUtil( lQs.getKey( EventKey.class, "evt_event_key" ) )
            .assertEventStatus( RefEventStatusKey.IXPEND );

   }


   /**
    * Tests when Ship To has been changed in an order which has a pending inbound shipment, the
    * shipping information of the inbound shipment and segment should also be updated and a new
    * history note should be added to the shipment.
    *
    * @throws MxException
    */
   @Test
   public void updateInboundShipmentWhenOrderShippingChanged_changeShipTo() throws MxException {
      // ACT
      // change the Ship To location of the order from iOldShipToLocation to iNewShipToLocation
      iShipmentService.updateInboundShipmentWhenOrderShippingChanged( iOrder, iNewShipToLocation,
            null, iVendorLocation, iHr );

      // ASSERT
      // assert the inbound shipment ship to location has been updated
      assertShipmentShipTo( iInboundShipmentKey, iNewShipToLocation );

      // assert the segment of the shipment has also been updated
      List<Segment> lShipSegmentsAfter =
            new JdbcShipmentRepository().getSegments( iInboundShipmentKey );
      assertEquals( 1, lShipSegmentsAfter.size() );
      assertSegment( lShipSegmentsAfter, 0, iVendorLocation, iNewShipToLocation, PENDING );

      // assert the history note has been added for the shipment
      String lHistoryNote = MessageFormat.format( i18n.get( FINAL_DESTINATION_CHANGED_MSG ),
            OLDSHIPTO, NEWSHIPTO, PO123456 );
      assertHistoryNote( iInboundShipmentKey, lHistoryNote );
   }


   /**
    * Tests when Ship To has been changed and ReExpedite To has been added (same as the previous
    * Ship To location) in an order which has a pending inbound shipment, the shipping information
    * of the inbound shipment and segment should also be updated and a new history note should be
    * added to the shipment.Note that the final destination will not change as the ReExpedite To
    * location is same as the old Ship To location
    *
    * @throws MxException
    */
   @Test
   public void
         updateInboundShipmentWhenOrderShippingChanged_changeShipToAndAddReExpediteToWithSameDestination()
               throws MxException {
      // ACT
      // change the Ship To location of the order from iOldShipToLocation to iNewShipToLocation
      // Add ReExpedite To location same as the previous ship to location
      iShipmentService.updateInboundShipmentWhenOrderShippingChanged( iOrder, iNewShipToLocation,
            iOldShipToLocation, iVendorLocation, iHr );

      // ASSERT
      // assert the inbound shipment ship to location has been updated
      assertShipmentShipTo( iInboundShipmentKey, iNewShipToLocation );

      // assert the segment of the shipment has also been updated
      List<Segment> lShipSegmentsAfter =
            new JdbcShipmentRepository().getSegments( iInboundShipmentKey );
      assertEquals( 2, lShipSegmentsAfter.size() );
      assertSegment( lShipSegmentsAfter, 0, iVendorLocation, iNewShipToLocation, PENDING );
      assertSegment( lShipSegmentsAfter, 1, iNewShipToLocation, iOldShipToLocation, PLAN );

      // assert the history note has been added for the shipment
      String lHistoryNote =
            MessageFormat.format( i18n.get( ROUTING_INFORMATION_CHANGED_MSG ), PO123456 );
      assertHistoryNote( iInboundShipmentKey, lHistoryNote );
   }


   /**
    * Tests when Ship To and ReExpedite To have been edited/added and are the same location in an
    * order which has a pending inbound shipment, the then DuplicateRoutingLocationException will be
    * thrown
    *
    * @throws MxException
    */

   @Test( expected = DuplicateRoutingLocationException.class )
   public void
         updateInboundShipmentWhenOrderShippingChanged_changeShipToAndAddReExpediteToWithSameLocation()
               throws MxException {
      // ACT
      // change the Ship To location of the order from iOldShipToLocation to iNewShipToLocation
      // Add ReExpedite To location to be the same as the new Ship To location
      iShipmentService.updateInboundShipmentWhenOrderShippingChanged( iOrder, iNewShipToLocation,
            iNewShipToLocation, iVendorLocation, iHr );
   }


   /**
    * Tests when Ship To and ReExpedite To has been changed in an order which has a pending inbound
    * shipment, the shipping information of the inbound shipment and segment should also be updated
    * and a new history note should be added to the shipment.
    *
    * @throws MxException
    */
   @Test
   public void updateInboundShipmentWhenOrderShippingChanged_changeShipToAndChangeReExpediteTo()
         throws MxException {
      // ACT
      // change the Ship To location of the order from iOldShipToLocation to iNewShipToLocation
      // change ReExpedite To location from iOldReExpediteToLocation to iNewReExpediteToLocation
      iShipmentService.updateInboundShipmentWhenOrderShippingChanged( iOrderWithReExpediteTo,
            iNewShipToLocation, iNewReExpediteToLocation, iVendorLocation, iHr );

      // ASSERT
      // assert the inbound shipment ship to location has been updated
      assertShipmentShipTo( iInboundShipmentForOrderWithReExpediteToKey, iNewShipToLocation );

      // assert the segment of the shipment has also been updated
      List<Segment> lShipSegmentsAfter =
            new JdbcShipmentRepository().getSegments( iInboundShipmentForOrderWithReExpediteToKey );
      assertEquals( 2, lShipSegmentsAfter.size() );
      assertSegment( lShipSegmentsAfter, 0, iVendorLocation, iNewShipToLocation, PENDING );
      assertSegment( lShipSegmentsAfter, 1, iNewShipToLocation, iNewReExpediteToLocation, PLAN );

      // assert the history note has been added for the shipment
      String lHistoryNote = MessageFormat.format( i18n.get( FINAL_DESTINATION_CHANGED_MSG ),
            OLDREEXPEDITETO, NEWREEXPEDITETO, PO123456 );
      assertHistoryNote( iInboundShipmentForOrderWithReExpediteToKey, lHistoryNote );
   }


   /**
    * Tests when Ship To has been changed and ReExpedito To has been removed in an order which has a
    * pending inbound shipment, the shipping information of the inbound shipment and segment should
    * also be updated and a new history note should be added to the shipment.
    *
    * @throws MxException
    */
   @Test
   public void updateInboundShipmentWhenOrderShippingChanged_changeShipToAndRemoveReExpediteTo()
         throws MxException {
      // ACT
      // change the Ship To location of the order from iOldShipToLocation to iNewShipToLocation
      // Remove the ReExpedite To location
      iShipmentService.updateInboundShipmentWhenOrderShippingChanged( iOrderWithReExpediteTo,
            iNewShipToLocation, null, iVendorLocation, iHr );

      // ASSERT
      // assert the inbound shipment ship to location has been updated
      assertShipmentShipTo( iInboundShipmentForOrderWithReExpediteToKey, iNewShipToLocation );

      // assert the segment of the shipment has also been updated
      List<Segment> lShipSegmentsAfter =
            new JdbcShipmentRepository().getSegments( iInboundShipmentForOrderWithReExpediteToKey );
      assertEquals( 1, lShipSegmentsAfter.size() );
      assertSegment( lShipSegmentsAfter, 0, iVendorLocation, iNewShipToLocation, PENDING );

      // assert the history note has been added for the shipment
      String lHistoryNote = MessageFormat.format( i18n.get( FINAL_DESTINATION_CHANGED_MSG ),
            OLDREEXPEDITETO, NEWSHIPTO, PO123456 );
      assertHistoryNote( iInboundShipmentForOrderWithReExpediteToKey, lHistoryNote );
   }


   /**
    * Tests when Ship To has been changed in an order which has a completed inbound shipment segment
    * ,CannotChangeShippingLocationException will be thrown .
    *
    * @throws MxException
    */
   @Test( expected = CannotChangeShippingLocationException.class )
   public void
         updateInboundShipmentWhenOrderShippingChanged_changeShipTo_withCompletedInboundShipmentSegment()
               throws MxException {
      List<Segment> lShipSegments = new JdbcShipmentRepository().getSegments( iInboundShipmentKey );
      assertEquals( 1, lShipSegments.size() );
      ShipSegmentKey lPendingShipSegmentKeyBefore = lShipSegments.get( 0 ).getSegmentKey();
      ShipSegmentTable lPendingSegmentBefore =
            ShipSegmentTable.findByPrimaryKey( lPendingShipSegmentKeyBefore );
      lPendingSegmentBefore.setStatus( RefShipSegmentStatusKey.COMPLETE );
      lPendingSegmentBefore.update();
      // ACT
      // change the Ship To location of the order from iOldShipToLocation to iNewShipToLocation
      iShipmentService.updateInboundShipmentWhenOrderShippingChanged( iOrder, iNewShipToLocation,
            null, iVendorLocation, iHr );
   }


   /**
    * Tests when attempts to change the ship to of an order which has a completed inbound shipment,
    * an exception will throw up.
    *
    * @throws MxException
    */
   @Test( expected = CannotChangeShippingLocationException.class )
   public void
         updateInboundShipmentWhenOrderShippingChanged_changeShipTo_withCompletedInboundShipment()
               throws MxException {
      // ARRANGE
      PurchaseOrderKey lOrder = Domain.createPurchaseOrder();
      Domain.createShipment( shipment -> {
         shipment.setPurchaseOrder( lOrder );
         shipment.setStatus( RefEventStatusKey.IXCMPLT );
         shipment.addShipmentSegment( segment -> {
            segment.setFromLocation( iVendorLocation );
            segment.setToLocation( iOldShipToLocation );
         } );
      } );

      // ACT
      // change the Ship To location of the order from iOldShipToLocation to iNewShipToLocation
      iShipmentService.updateInboundShipmentWhenOrderShippingChanged( lOrder, iNewShipToLocation,
            null, iVendorLocation, iHr );
   }


   /**
    * Tests when Re-Expedite Ship To Location has been changed in an order which has a pending
    * inbound shipment, the shipping information of the inbound shipment has no change but the
    * segments shipping information have been updated and a new history note should be added to the
    * shipment.
    *
    * @throws MxException
    */
   @Test
   public void updateInboundShipmentWhenOrderShippingChanged_addReExpediteTo() throws MxException {
      // ACT
      // add iNewReExpediteToLocation to be the Re-Expedite To Location of the order
      iShipmentService.updateInboundShipmentWhenOrderShippingChanged( iOrder, iOldShipToLocation,
            iNewReExpediteToLocation, iVendorLocation, iHr );

      // ASSERT
      // assert the inbound shipment ship to location hasn't been updated
      assertShipmentShipTo( iInboundShipmentKey, iOldShipToLocation );

      // assert the two segments shipping information have been updated
      List<Segment> lShipSegments = new JdbcShipmentRepository().getSegments( iInboundShipmentKey );
      assertEquals( 2, lShipSegments.size() );
      assertSegment( lShipSegments, 0, iVendorLocation, iOldShipToLocation, PENDING );
      assertSegment( lShipSegments, 1, iOldShipToLocation, iNewReExpediteToLocation, PLAN );

      // assert the history note has been added for the shipment
      String lHistoryNote = MessageFormat.format( i18n.get( FINAL_DESTINATION_CHANGED_MSG ),
            OLDSHIPTO, NEWREEXPEDITETO, PO123456 );
      assertHistoryNote( iInboundShipmentKey, lHistoryNote );
   }


   /**
    * Tests when Re-Expedite Ship To Location has been changed in an order which has a pending
    * inbound shipment, the shipping information of the inbound shipment has no change but the
    * segments shipping information have been updated and a new history note should be added to the
    * shipment.
    *
    * @throws MxException
    */
   @Test
   public void updateInboundShipmentWhenOrderShippingChanged_changeReExpediteTo()
         throws MxException {
      // ACT
      // change the Re-Expedite To Location from iOldReExpediteToLocation to
      // iNewReExpediteToLocation
      iShipmentService.updateInboundShipmentWhenOrderShippingChanged( iOrderWithReExpediteTo,
            iOldShipToLocation, iNewReExpediteToLocation, iVendorLocation, iHr );

      // ASSERT
      // assert the inbound shipment ship to location hasn't been updated
      assertShipmentShipTo( iInboundShipmentForOrderWithReExpediteToKey, iOldShipToLocation );

      // assert the two segments shipping information have been updated
      List<Segment> lShipSegmentsQsAfter =
            new JdbcShipmentRepository().getSegments( iInboundShipmentForOrderWithReExpediteToKey );
      assertEquals( 2, lShipSegmentsQsAfter.size() );
      assertSegment( lShipSegmentsQsAfter, 0, iVendorLocation, iOldShipToLocation, PENDING );
      assertSegment( lShipSegmentsQsAfter, 1, iOldShipToLocation, iNewReExpediteToLocation, PLAN );

      // assert the history note has been added for the shipment
      String lHistoryNote = MessageFormat.format( i18n.get( FINAL_DESTINATION_CHANGED_MSG ),
            OLDREEXPEDITETO, NEWREEXPEDITETO, PO123456 );
      assertHistoryNote( iInboundShipmentForOrderWithReExpediteToKey, lHistoryNote );
   }


   /**
    * Tests when Re-Expedite Ship To Location has been removed in an order which has a pending
    * inbound shipment, the shipping information of the inbound shipment has no change but the
    * segments shipping information have been updated and a new history note should be added to the
    * shipment.
    *
    * @throws MxException
    */
   @Test
   public void updateInboundShipmentWhenOrderShippingChanged_removeReExpediteTo()
         throws MxException {
      // ACT
      // remove the Re-Expedite To Location of the order
      iShipmentService.updateInboundShipmentWhenOrderShippingChanged( iOrderWithReExpediteTo,
            iOldShipToLocation, null, iVendorLocation, iHr );

      // ASSERT
      // assert the inbound shipment ship to location hasn't been updated
      assertShipmentShipTo( iInboundShipmentForOrderWithReExpediteToKey, iOldShipToLocation );

      // assert the segment shipping information has been updated
      List<Segment> lShipSegmentsAfter =
            new JdbcShipmentRepository().getSegments( iInboundShipmentForOrderWithReExpediteToKey );
      assertEquals( 1, lShipSegmentsAfter.size() );
      assertSegment( lShipSegmentsAfter, 0, iVendorLocation, iOldShipToLocation, PENDING );

      // assert the history note has been added for the shipment
      String lHistoryNote = MessageFormat.format( i18n.get( FINAL_DESTINATION_CHANGED_MSG ),
            OLDREEXPEDITETO, OLDSHIPTO, PO123456 );
      assertHistoryNote( iInboundShipmentForOrderWithReExpediteToKey, lHistoryNote );
   }


   /**
    * Tests ShipTo location has been changed and ReExpedite To location has been added in an order
    * which has a pending inbound shipment, the shipping information of the inbound shipment has the
    * corresponding change, the segments shipping information have been updated and a new history
    * note should be added to the shipment.
    *
    * @throws MxException
    */
   @Test
   public void
         updateInboundShipmentWhenOrderShippingChanged_changeShipToAndAddReExpediteToWithNewDestination()
               throws MxException {
      // ACT
      // change the ShipTo location from iOldShipToLocation to iNewShipToLocation
      // Add a new ReExpediteTo location
      iShipmentService.updateInboundShipmentWhenOrderShippingChanged( iOrder, iNewShipToLocation,
            iNewReExpediteToLocation, iVendorLocation, iHr );

      // ASSERT
      // assert the inbound shipment ship to location has been updated
      assertShipmentShipTo( iInboundShipmentKey, iNewShipToLocation );

      // assert the segment shipping information has been updated
      List<Segment> lShipSegmentsAfter =
            new JdbcShipmentRepository().getSegments( iInboundShipmentKey );
      assertEquals( 2, lShipSegmentsAfter.size() );
      assertSegment( lShipSegmentsAfter, 0, iVendorLocation, iNewShipToLocation, PENDING );
      assertSegment( lShipSegmentsAfter, 1, iNewShipToLocation, iNewReExpediteToLocation, PLAN );

      // assert the history note has been added for the shipment
      String lHistoryNote = MessageFormat.format( i18n.get( FINAL_DESTINATION_CHANGED_MSG ),
            OLDSHIPTO, NEWREEXPEDITETO, PO123456 );
      assertHistoryNote( iInboundShipmentKey, lHistoryNote );
   }


   /**
    * Tests ShipTo location has been changed in an order which has multiple pending inbound
    * shipments, the shipping information of the inbound shipment has the corresponding change, the
    * segments shipping information have been updated and a new history note should be added to the
    * shipment.
    *
    * @throws MxException
    */

   @Test
   public void updateInboundShipmentWhenOrderShippingChanged_changeShipToWhenThereAreMoreSegments()
         throws MxException {
      // ARRANGE
      // Add more routing segments to the inbound shipment
      ShipmentKey lShipment = createMultipleSegments();

      // ACT
      // Change the ShipTo location from iOldShipToLocation to iNewShipToLocation
      iShipmentService.updateInboundShipmentWhenOrderShippingChanged( iOrder, iNewShipToLocation,
            null, iVendorLocation, iHr );

      // ASSERT
      // assert the inbound shipment ship to location has been updated
      assertShipmentShipTo( lShipment, iNewShipToLocation );

      // assert the segment shipping information has been updated and only 1 segment exists and
      // other segments have been deleted
      List<Segment> lShipSegmentsAfter = new JdbcShipmentRepository().getSegments( lShipment );
      assertEquals( 1, lShipSegmentsAfter.size() );
      assertSegment( lShipSegmentsAfter, 0, iVendorLocation, iNewShipToLocation, PENDING );

      // assert the history note has been added for the shipment
      String lHistoryNote = MessageFormat.format( i18n.get( FINAL_DESTINATION_CHANGED_MSG ),
            THIRDROUTING, NEWSHIPTO, PO123456 );
      assertHistoryNote( lShipment, lHistoryNote );
   }


   /**
    *
    * Tests the history note added to shipment if shipment is marked as completed when shipment line
    * has a conversion factor in line
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testShipmentHistoryNotetIfShipmentLineHasConversionFactor() throws Exception {

      // Adds the provided DB record to ref_qty_unit table.
      DataSetArgument args = new DataSetArgument();
      args.add( GALLONS, "qty_unit_db_id", "qty_unit_cd" );
      args.add( "decimal_places_qt", 1 );
      MxDataAccess.getInstance().executeInsert( "ref_qty_unit", args );

      // create part with alternate unit of measure
      PartNoKey lPartNo =
            defaultPart().withAlternateUnitType( GALLONS, new BigDecimal( 4 ) ).build();

      // create order
      PurchaseOrderKey lOrder = new OrderBuilder().withStatus( RefEventStatusKey.POISSUED ).build();

      // create orderline
      PurchaseOrderLineKey lPoLine = Domain.createPurchaseOrderLine( aPoLine -> {
         aPoLine.part( lPartNo );
         aPoLine.orderKey( lOrder );
         aPoLine.unitType( GALLONS );
         aPoLine.orderQuantity( BigDecimal.ONE );
         aPoLine.unitPrice( BigDecimal.TEN );
         aPoLine.accruedValue( BigDecimal.TEN );
      } );

      // create shipment
      iShipment = Domain.createShipment( aShipment -> {
         aShipment.setPurchaseOrder( lOrder );
         aShipment.addShipmentSegment( segment -> {
            segment.setFromLocation( iDockLocation );
            segment.setToLocation( iVendorLocation );
         } );
      } );

      // create shipmentline for above order and part
      ShipmentLineKey lShipmentLineKey = new ShipmentLineBuilder( iShipment )
            .withExpectedQuantity( 1.0 ).forPart( lPartNo ).forOrderLine( lPoLine ).build();

      // setup the shipment receipt
      ReceiveShipmentLineTO lShipmentLineTO = new ReceiveShipmentLineTO( lShipmentLineKey );
      lShipmentLineTO.setPartNo( lPartNo );
      lShipmentLineTO.setReceivedQty( 1.0 );
      lShipmentLineTO.setLineNo( 1 );
      lShipmentLineTO.setSerialNo( "S1" );
      lShipmentLineTO.setRouteCond( RefInvCondKey.RFI, null );

      // receive the shipment
      ShipmentLineService.receive( lShipmentLineTO, iDockLocation, new Date(), false, null, null,
            null, iHr );

      // build history note
      String lHistoryNote = MessageFormat.format(
            i18n.get( "core.msg.SHIPMENT_ALTERNATE_UOM_CHANGE" ), RECEIVEDQTY, SHIPMENTLINEQTYCD,
            ORDERLINENO, SHIPMENTLINEEXPECTEDQTY, POLINEQTYCD, CONVERSION_FACTOR );

      // assert the history note has been added for the shipment
      assertHistoryNote( iShipment, lHistoryNote );
   }


   /**
    * Verify that sending a valid pending shipment is successfully sent (happy path).
    *
    * Note; this test verifies many behaviours that constitute "successfully sent", as it is a
    * replacement of a test from the testingendejb project.
    */
   @Test
   public void sendPendingShipmentIsSuccessfulForValidShipment() throws Exception {

      // Given an inventory to be shipped that is at a known location (not OPS).
      LocationKey inventoryLocation = Domain.createLocation();
      InventoryKey inventory = Domain.createTrackedInventory( trk -> {
         trk.setLocation( inventoryLocation );
         // Required dependency not applicable to the test.
         trk.setPartNumber( Domain.createPart( part -> part.setPartType( COMNHW ) ) );
      } );

      // Given a ship-to location that is a dock.
      LocationKey shipTo = Domain.createLocation( loc -> {
         loc.setType( RefLocTypeKey.DOCK );
      } );

      // Given a valid pending shipment.
      // Valid means: status of pending, ship-to location is a dock, has a ship-from location.
      ShipmentKey shipment = Domain.createShipment( shpmnt -> {
         shpmnt.setStatus( IXPEND );
         shpmnt.addShipmentSegment( segment -> {
            segment.setStatus( PENDING );
            segment.setToLocation( shipTo );
            segment.setFromLocation( inventoryLocation );
         } );
         shpmnt.addShipmentLine( line -> {
            line.inventory( inventory );
         } );
      } );

      // When the shipment is sent.
      ShipmentService.send( shipment, iHr, SHIPMENT_DATE, RECEIVED_DATE, USER_NOTE, SYSTEM_NOTE );

      // Then the shipment is successfully sent.

      // The shipment has the correct status, shipment and received date.
      EvtEventTable evtEventRow = iEvtEventDao.findByPrimaryKey( shipment );
      assertThat( "Unexpected shipment status", evtEventRow.getEventStatus(), is( IXINTR ) );
      assertThat( "Unexpected shipment historical flag", evtEventRow.getHistBool(), is( false ) );
      assertThat( "Unexpected shipment shipment date", evtEventRow.getActualStartDt(),
            is( SHIPMENT_DATE ) );
      assertThat( "Unexpected shipment received date", evtEventRow.getEventDate(),
            is( RECEIVED_DATE ) );
      assertHistoryNote( shipment, USER_NOTE );

      // The shipment segment has the correct status.
      List<Segment> shipmentSegments = new JdbcShipmentRepository().getSegments( shipment );
      assertThat( "Unexpected number of shipment segments", shipmentSegments.size(), is( 1 ) );
      assertSegment( shipmentSegments, 0, inventoryLocation, shipTo, INTRANSIT );

      // The inventory is moved to the OPS location.
      assertThat( "Unexpected inventory location",
            InvInvTable.findByPrimaryKey( inventory ).getLocation(), is( LocationKey.OPS ) );

   }


   /**
    * Verify that sending one of the shipments that is part of a waybill successfully sends all the
    * shipments in that waybill.
    */
   @Test
   public void sendingWaybillShipmentSendsAllShipments() throws Exception {

      // Given a ship-from location.
      LocationKey shipFrom = Domain.createLocation();

      // Given a ship-to location that is a dock.
      LocationKey shipTo = Domain.createLocation( loc -> {
         loc.setType( RefLocTypeKey.DOCK );
      } );

      // Given a pending shipment that is part of a waybill and has a ship-to location.
      String waybillNumber = "noWayBill_wayTed";
      ShipmentKey shipment1 = Domain.createShipment( shpmnt -> {
         shpmnt.setStatus( IXPEND );
         shpmnt.setWaybillNumber( waybillNumber );
         shpmnt.addShipmentSegment( segment -> {
            segment.setStatus( PENDING );
            segment.setToLocation( shipTo );
            segment.setFromLocation( shipFrom );
         } );
         shpmnt.addShipmentLine( line -> {
            line.inventory( Domain.createTrackedInventory( trk -> {
               trk.setLocation( shipFrom );
               // Required dependency not applicable to the test.
               trk.setPartNumber( Domain.createPart( part -> part.setPartType( COMNHW ) ) );
            } ) );
         } );
      } );

      // Given another pending shipment that is part of the same waybill with the same ship-to
      // location.
      ShipmentKey shipment2 = Domain.createShipment( shpmnt -> {
         shpmnt.setStatus( IXPEND );
         shpmnt.setWaybillNumber( waybillNumber );
         shpmnt.addShipmentSegment( segment -> {
            segment.setStatus( PENDING );
            segment.setToLocation( shipTo );
            segment.setFromLocation( shipFrom );
         } );
         shpmnt.addShipmentLine( line -> {
            line.inventory( Domain.createTrackedInventory( trk -> {
               trk.setLocation( shipFrom );
               // Required dependency not applicable to the test.
               trk.setPartNumber( Domain.createPart( part -> part.setPartType( COMNHW ) ) );
            } ) );
         } );
      } );

      // When one of the shipment is sent.
      ShipmentService.send( shipment1, iHr, SHIPMENT_DATE, RECEIVED_DATE, USER_NOTE, SYSTEM_NOTE );

      // Then both of the shipments are successfully sent.
      assertShipmentStatus( "Unexpected status for shipment1", shipment1, IXINTR );
      assertShipmentStatus( "Unexpected status for shipment2", shipment2, IXINTR );
   }


   /**
    * Assert the ship to of the shipment
    *
    * @param aInboundShipmentKey
    *           the inbound shipment key
    * @param aExpectedShipToLocation
    *           the expected ship to location
    */
   private void assertShipmentShipTo( ShipmentKey aInboundShipmentKey,
         LocationKey aExpectedShipToLocation ) {
      EvtLocTable lInboundShipmentShipToLocation =
            EvtLocTable.findByPrimaryKey( aInboundShipmentKey.getEventKey(), 2 );
      assertEquals( aExpectedShipToLocation, lInboundShipmentShipToLocation.getLocation() );
   }


   /**
    * Assert the history note of the shipment
    *
    * @param aInboundShipmentKey
    *           the inbound shipment key
    * @param aExpectedHistoryNote
    *           the expected history note of the shipment
    */
   private void assertHistoryNote( ShipmentKey aInboundShipmentKey, String aExpectedHistoryNote ) {
      DataSet lEventStageTable = iEvtStageDao.getStageSnapshot( aInboundShipmentKey.getEventKey() );
      assertEquals( 1, lEventStageTable.getRowCount() );
      lEventStageTable.next();
      assertEquals( aExpectedHistoryNote, lEventStageTable.getString( "stage_note" ) );
   }


   /**
    * Assert the segment(s) shipping information and status are correct
    *
    * @param aShipmentSegments
    *           the shipment segments query set
    * @param aExpectedShipFromLocation
    *           the expected ship from location
    * @param aExpectedShipToLocation
    *           the expected ship to location
    * @param aExpectedSegmentStatus
    *           the expected segment status
    */
   private void assertSegment( List<Segment> aShipmentSegments, int aRowNumber,
         LocationKey aExpectedShipFromLocation, LocationKey aExpectedShipToLocation,
         RefShipSegmentStatusKey aExpectedSegmentStatus ) {
      ShipSegmentKey lShipSegmentKey = aShipmentSegments.get( aRowNumber ).getSegmentKey();
      ShipSegmentTable lPendingSegmentAfter = ShipSegmentTable.findByPrimaryKey( lShipSegmentKey );
      assertEquals( aExpectedShipFromLocation, lPendingSegmentAfter.getShipFrom() );
      assertEquals( aExpectedShipToLocation, lPendingSegmentAfter.getShipTo() );
      assertEquals( aExpectedSegmentStatus, lPendingSegmentAfter.getStatus() );
   }


   private void assertShipmentStatus( String reason, ShipmentKey shipment,
         RefEventStatusKey expectedStatus ) {

      RefEventStatusKey actualStatus = iEvtEventDao.findByPrimaryKey( shipment ).getEventStatus();
      assertThat( reason, actualStatus, is( expectedStatus ) );
   }


   /**
    * This method is for creating additional three PLAN segments for a shipment
    *
    */
   private ShipmentKey createMultipleSegments() {
      return Domain.createShipment( shipmnt -> {
         shipmnt.setPurchaseOrder( iOrder );
         shipmnt.setStatus( IXPEND );
         shipmnt.addShipmentSegment( segment1 -> {
            segment1.setFromLocation( iVendorLocation );
            segment1.setToLocation( iOldShipToLocation );
            segment1.setStatus( PENDING );
         } );
         shipmnt.addShipmentSegment( segment2 -> {
            segment2.setFromLocation( iOldShipToLocation );
            segment2.setToLocation( iFirstRoutingLocation );
            segment2.setStatus( PLAN );
         } );
         shipmnt.addShipmentSegment( segment3 -> {
            segment3.setFromLocation( iFirstRoutingLocation );
            segment3.setToLocation( iSecondRoutingLocation );
            segment3.setStatus( PLAN );
         } );
         shipmnt.addShipmentSegment( segment4 -> {
            segment4.setFromLocation( iSecondRoutingLocation );
            segment4.setToLocation( iThirdRoutingLocation );
            segment4.setStatus( PLAN );
         } );
      } );
   }


   @Before
   public void loadData() throws Exception {
      iShipmentService = new ShipmentService();

      iEvtEventDao = InjectorContainer.get().getInstance( EvtEventDao.class );
      iEvtStageDao = InjectorContainer.get().getInstance( EvtStageDao.class );

      // create a human resource
      iHr = Domain.createHumanResource( aHr -> aHr.setUser( Domain.createUser( aUser -> {
         aUser.setUsername( USERNAME_TESTUSER );
         aUser.setUserId( USERID_TESTUSER );
      } ) ) );

      // create a vendor location
      iVendorLocation = Domain.createLocation( aVendorLocation -> {
         aVendorLocation.setCode( "TESTVENLOC" );
         aVendorLocation.setType( RefLocTypeKey.VENDOR );
      } );

      // create a vendor location
      iBrokerLocation = Domain.createLocation( loc -> {
         loc.setType( RefLocTypeKey.VENDOR );
         loc.setCode( "TESTBROCKLOC" );
      } );

      // create a supply location
      iSupplyLocation = Domain.createLocation( loc -> {
         loc.setType( RefLocTypeKey.AIRPORT );
         loc.setIsSupplyLocation( true );
      } );

      // create a dock at the supply location
      iDockLocation = Domain.createLocation( loc -> {
         loc.setType( RefLocTypeKey.DOCK );
         loc.setCode( DOCK );
         loc.setSupplyLocation( iSupplyLocation );
      } );

      iOldShipToLocation = Domain.createLocation( loc -> loc.setCode( OLDSHIPTO ) );
      iOldReExpediteToLocation = Domain.createLocation( loc -> loc.setCode( OLDREEXPEDITETO ) );
      iNewShipToLocation = Domain.createLocation( loc -> loc.setCode( NEWSHIPTO ) );
      iNewReExpediteToLocation = Domain.createLocation( loc -> loc.setCode( NEWREEXPEDITETO ) );
      iFirstRoutingLocation = Domain.createLocation( loc -> loc.setCode( FIRSTROUTING ) );
      iSecondRoutingLocation = Domain.createLocation( loc -> loc.setCode( SECONDROUTING ) );
      iThirdRoutingLocation = Domain.createLocation( loc -> loc.setCode( THIRDROUTING ) );

      iBatchPart = new PartNoBuilder().withInventoryClass( RefInvClassKey.BATCH )
            .withRepairBool( true ).withStatus( RefPartStatusKey.ACTV )
            .withFinancialType( RefFinanceTypeKey.CONSUM ).withUnitType( RefQtyUnitKey.EA )
            .withTotalQuantity( BIN_QTY ).withTotalValue( BigDecimal.ONE ).build();

      iTrackPart = new PartNoBuilder().withInventoryClass( RefInvClassKey.TRK )
            .withRepairBool( true ).withStatus( RefPartStatusKey.ACTV )
            .withFinancialType( RefFinanceTypeKey.CONSUM ).withUnitType( RefQtyUnitKey.EA )
            .withAbcClass( RefAbcClassKey.D ).withTotalValue( BigDecimal.ONE ).build();

      iVendor = new VendorBuilder().withCode( "TESTVENDOR" ).atLocation( iVendorLocation ).build();

      new VendorBuilder().withCode( "TESTBROKER" ).atLocation( iBrokerLocation ).asBroker().build();

      iRepairOrderLine =
            new OrderLineBuilder( new OrderBuilder().withOrderType( RefPoTypeKey.REPAIR )
                  .withStatus( RefEventStatusKey.POISSUED ).withVendor( iVendor ).build() )
                        .withLineType( RefPoLineTypeKey.REPAIR ).forPart( iBatchPart )
                        .withUnitType( RefQtyUnitKey.EA ).build();

      // build a default, local owner
      new OwnerDomainBuilder().isDefault().build();

      iInventoryOwner = new OwnerDomainBuilder().isNonLocal().build();

      // build a default currency
      new CurrencyBuilder( "USD" ).isDefault().build();

      iPoLine = createRepairOrderLine();

      iShipment = Domain.createShipment( shipmnt -> {
         shipmnt.setType( PURCHASE );
         shipmnt.addShipmentSegment( segment -> {
            segment.setFromLocation( iVendorLocation );
            segment.setToLocation( iDockLocation );
            segment.setStatus( PENDING );
         } );
      } );

      iOrder = Domain.createPurchaseOrder( aOrder -> aOrder.setOrderNumber( PO123456 ) );

      iOrderWithReExpediteTo = Domain.createPurchaseOrder( lOrder -> {
         lOrder.vendor( iVendor );
         lOrder.shippingTo( iOldShipToLocation );
         lOrder.reexpediteTo( iOldReExpediteToLocation );
         lOrder.setOrderNumber( PO123456 );
      } );

      iInboundShipmentKey = Domain.createShipment( shipmnt -> {
         shipmnt.setPurchaseOrder( iOrder );
         shipmnt.setStatus( IXPEND );
         shipmnt.addShipmentSegment( segment -> {
            segment.setFromLocation( iVendorLocation );
            segment.setToLocation( iOldShipToLocation );
            segment.setStatus( PENDING );
         } );
      } );

      iInboundShipmentForOrderWithReExpediteToKey = Domain.createShipment( lInboundShipment -> {
         lInboundShipment.setPurchaseOrder( iOrderWithReExpediteTo );
         lInboundShipment.addShipmentSegment( segment1 -> {
            segment1.setFromLocation( iVendorLocation );
            segment1.setToLocation( iOldShipToLocation );
            segment1.setStatus( PENDING );
         } );
         lInboundShipment.addShipmentSegment( segment2 -> {
            segment2.setFromLocation( iOldShipToLocation );
            segment2.setToLocation( iOldReExpediteToLocation );
            segment2.setStatus( PLAN );
         } );
      } );

   }


   /**
    * Assert the owner and owner type are the same as expected.
    *
    * @param aExpectedOwner
    *           the expected owner
    * @param aRepairInv
    *           the repair inventory
    */
   private void assertOwner( OwnerKey aExpectedOwner, InventoryKey aRepairInv ) {

      InvInv lInvInv = new InvInv( aRepairInv );
      lInvInv.assertOwner( aExpectedOwner );
      lInvInv.assertOwnerTypeCd( RefOwnerTypeKey.BORROW.getCd() );
   }


   /**
    * Create repair order line.
    *
    * @return the po line
    */
   private PurchaseOrderLineKey createRepairOrderLine() {

      VendorKey lVendor =
            new VendorBuilder().withCode( "TESTVENDOR" ).atLocation( iVendorLocation ).build();

      PurchaseOrderKey lRepairOrder = new OrderBuilder().withOrderType( RefPoTypeKey.REPAIR )
            .withStatus( RefEventStatusKey.POISSUED ).withVendor( lVendor ).build();

      OwnerKey lOriginalOwner = new OwnerDomainBuilder().isDefault().build();

      return new OrderLineBuilder( lRepairOrder ).withLineType( RefPoLineTypeKey.REPAIR )
            .forPart( iTrackPart ).withOwner( lOriginalOwner ).withUnitType( RefQtyUnitKey.EA )
            .build();
   }


   /**
    * Create repair inventory.
    *
    * @param aOwner
    *           the owner
    * @param aLocation
    *           the location
    *
    * @return the repair inventory
    */
   private InventoryKey createRepairInventory( OwnerKey aOwner, LocationKey aLocation ) {
      InventoryKey lRepairInv =
            new InventoryBuilder().withOwner( aOwner ).withOwnershipType( RefOwnerTypeKey.BORROW )
                  .atLocation( aLocation ).withClass( RefInvClassKey.TRK )
                  .withCondition( RefInvCondKey.REPREQ ).withPartNo( iTrackPart ).build();

      return lRepairInv;
   }
}
