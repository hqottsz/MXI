package com.mxi.mx.core.services.order;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.AccountBuilder;
import com.mxi.am.domain.builder.CurrencyBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.OrderBuilder;
import com.mxi.am.domain.builder.OrderLineBuilder;
import com.mxi.am.domain.builder.PartGroupDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.PartRequestBuilder;
import com.mxi.am.domain.builder.ShipmentDomainBuilder;
import com.mxi.am.domain.builder.ShipmentLineBuilder;
import com.mxi.am.domain.builder.StockBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.domain.builder.VendorBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MandatoryArgumentException;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.exception.NegativeValueException;
import com.mxi.mx.common.i18n.i18n;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.common.unittest.security.SecurityIdentificationStub;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.EventLocationKey;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OrgKey;
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
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefPoAuthLvlStatusKey;
import com.mxi.mx.core.key.RefPoLineTypeKey;
import com.mxi.mx.core.key.RefPoTypeKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefReqPriorityKey;
import com.mxi.mx.core.key.RefShipSegmentStatusKey;
import com.mxi.mx.core.key.RefShipmentTypeKey;
import com.mxi.mx.core.key.RefStageReasonKey;
import com.mxi.mx.core.key.ShipmentKey;
import com.mxi.mx.core.key.ShipmentLineKey;
import com.mxi.mx.core.key.StageKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.VendorKey;
import com.mxi.mx.core.services.event.HistoricRecordException;
import com.mxi.mx.core.services.event.InvalidStatusException;
import com.mxi.mx.core.services.order.exception.CannotChangeVendorException;
import com.mxi.mx.core.services.order.exception.CannotEditVendorInformationException;
import com.mxi.mx.core.services.order.exception.CantCancelOrderWithRcvdInvWaitingForInspException;
import com.mxi.mx.core.services.shipment.InvalidLocationTypeException;
import com.mxi.mx.core.services.shipment.ShipmentService;
import com.mxi.mx.core.table.evt.EvtEventDao;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.evt.EvtLocTable;
import com.mxi.mx.core.table.evt.EvtStageTable;
import com.mxi.mx.core.table.evt.JdbcEvtEventDao;
import com.mxi.mx.core.table.inv.InvLocTable;
import com.mxi.mx.core.table.po.POLineTable;
import com.mxi.mx.core.table.po.PoAuthTable;
import com.mxi.mx.core.table.po.PoHeaderTable;
import com.mxi.mx.core.table.req.ReqPartTable;
import com.mxi.mx.core.table.ship.ShipSegmentTable;
import com.mxi.mx.core.table.ship.ShipShipmentLineTable;
import com.mxi.mx.core.unittest.table.evt.EvtStage;
import com.mxi.mx.domain.shipment.Segment;
import com.mxi.mx.repository.shipment.JdbcShipmentRepository;


/**
 * Ensures that all {@link OrderService} methods work as expected
 */
public class OrderServiceTest {

   private static final String PO_LINE_CLEAR_REEXPEDITE_MESSAGE =
         "core.msg.PO_LINE_CLEAR_REEXPEDITE_MESSAGE";
   private static final String PO_LINE_CHANGE_REEXPEDITE_MESSAGE =
         "core.msg.PO_LINE_CHANGE_REEXPEDITE_MESSAGE";
   private static final String PO_LINE_CHANGE_DESTINATION_MESSAGE =
         "core.msg.PO_LINE_CHANGE_DESTINATION_MESSAGE";
   private OrderService iOrderService;
   private static final String PO_NUMBER = "PO-222";
   private static final String VENDOR_LOCATION = "vendorloc";
   private static final String DOCK_LOCATION = "LOC/DOCK";
   private static final String USERNAME_TESTUSER = "testuser";
   private static final int USERID_TESTUSER = 999;
   private static final String TASK_BARCODE = "ABCD";

   private static final String PART_GROUP = "PART_GRP";
   private static final String STOCK_NAME = "STCK01";
   private static final String PART_IN_STOCK = "P1_STCK1";
   private static final String PART_IN_STOCK_AND_PART_GROUP = "P2_STCK_PG";
   private static final String PART_IN_PART_GROUP = "P3_PG";
   private static final String MP_KEY_SDESC = "NotNull";
   private static final RefInvClassKey SERIALIZED = RefInvClassKey.SER;
   private static final String OLDSHIPTO = "OLDSHIPTO";
   private static final String OLDREEXPEDITETO = "OLDREEXPEDITETO";
   private static final String NEWSHIPTO = "NEWSHIPTO";
   private static final String NEWREEXPEDITETO = "NEWREEXPEDITETO";

   private HumanResourceKey iHr;
   private VendorKey iVendor;
   private LocationKey iDockLocation;
   private LocationKey iVendorLocation;
   private PurchaseOrderKey iOrder;
   private HumanResourceKey iHrWithHighLevelAuth;
   private LocationKey iOldShipToLocation;
   private LocationKey iOldReExpediteToLocation;
   private LocationKey iNewShipToLocation;
   private LocationKey iNewReExpediteToLocation;
   private RefCurrencyKey iCurrency;
   private VendorKey iAnotherVendor;
   private LocationKey iAnotherVendorLocation;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private ShipmentService iMockShipmentService;

   private OrderService iOrderServiceWithMockShipmentService;


   @Before
   public void loadData() throws Exception {
      iOrderService = new OrderService();
      iMockShipmentService = Mockito.mock( ShipmentService.class );
      iOrderServiceWithMockShipmentService =
            new OrderService( null, null, null, iMockShipmentService );

      // create a HR
      iHr = Domain.createHumanResource(
            aHumanResource -> aHumanResource.setUser( Domain.createUser( aUser -> {
               aUser.setUsername( USERNAME_TESTUSER );
               aUser.setUserId( USERID_TESTUSER );
            } ) ) );

      SecurityIdentificationUtils.setInstance( new SecurityIdentificationStub( iHr ) );

      // create a vendor location
      iVendorLocation = Domain.createLocation( aVendorLocation -> {
         aVendorLocation.setCode( VENDOR_LOCATION );
         aVendorLocation.setType( RefLocTypeKey.VENDOR );
      } );
      iAnotherVendorLocation = Domain.createLocation( aVendorLocation -> {
         aVendorLocation.setType( RefLocTypeKey.VENDOR );
      } );

      // build org vendor
      iVendor = new VendorBuilder().withCode( "TESTVENDOR" ).atLocation( iVendorLocation ).build();
      // build another vendor
      iAnotherVendor = new VendorBuilder().withCode( "ANOTHERVENDOR" )
            .atLocation( iAnotherVendorLocation ).build();

      // create a dock location
      iDockLocation = new LocationDomainBuilder().withCode( DOCK_LOCATION )
            .withType( RefLocTypeKey.DOCK ).build();

      iOldShipToLocation = Domain.createLocation( aOldShipToLocation -> {
         aOldShipToLocation.setCode( OLDSHIPTO );
         aOldShipToLocation.setType( RefLocTypeKey.DOCK );
      } );

      iOldReExpediteToLocation =
            Domain.createLocation( aReExpediteOldShipToLocation -> aReExpediteOldShipToLocation
                  .setCode( OLDREEXPEDITETO ) );
      iNewShipToLocation = Domain.createLocation( aNewShipToLocation -> {
         aNewShipToLocation.setCode( NEWSHIPTO );
         aNewShipToLocation.setType( RefLocTypeKey.DOCK );
      } );
      iNewReExpediteToLocation = Domain.createLocation( aNewReExpediteToLocation -> {
         aNewReExpediteToLocation.setCode( NEWREEXPEDITETO );
         aNewReExpediteToLocation.setType( RefLocTypeKey.DOCK );
      } );

      iCurrency = new CurrencyBuilder( "currency" ).build();
   }


   /**
    * make sure the exception is thrown if user is trying to create PO and provides ship-to location
    * as vendor location
    *
    * @throws Exception
    */
   @Test
   public void testInvalidLocationTypeExceptionWhenCreatePOWithShipToAsVendor() throws Exception {
      try {

         OrderDetailsTO lPODetailsTO = new OrderDetailsTO();
         lPODetailsTO.setPriority( RefReqPriorityKey.NORMAL );
         lPODetailsTO.setOrganization( new OrgKey( 0, 1 ) );
         lPODetailsTO.setPurchasingContact( USERNAME_TESTUSER );
         lPODetailsTO.setShipToLocation( VENDOR_LOCATION );
         lPODetailsTO.setVendor( iVendor );
         lPODetailsTO.setCurrency( new RefCurrencyKey( 10, "USD" ) );
         lPODetailsTO.setExchangeRate( BigDecimal.ONE );
         lPODetailsTO.setPoType( RefPoTypeKey.PURCHASE );

         OrderService.create( lPODetailsTO, "", iHr );
         fail( "Expected InvalidLocationTypeException" );

      } catch ( InvalidLocationTypeException e ) {

         // Verify that the exception was thrown and contains the expected message.
         assertEquals( "core.err.33325", e.getMessageKey() );

      }

   }


   /**
    * make sure the exception is thrown if user is trying to edit PO and provides ship-to location
    * as vendor location
    *
    * @throws Exception
    */
   @Test
   public void testInvalidLocationTypeExceptionWhenEditPOWithShipToAsVendor() throws Exception {
      try {

         // create order
         PurchaseOrderKey lPurchaseOrderKey = createOrder().withStatus( RefEventStatusKey.POAUTH )
               .shippingTo( iDockLocation ).build();

         // create the transfer object for edit PO
         EditOrderDetailsTO lEditPODetailsTO = new EditOrderDetailsTO();
         lEditPODetailsTO.setShipToLocation( VENDOR_LOCATION );

         // edit the PO
         iOrderService.set( lPurchaseOrderKey, lEditPODetailsTO, iHr );
         fail( "Expected InvalidLocationTypeException" );

      } catch ( InvalidLocationTypeException e ) {

         // Verify that the exception was thrown and contains the expected message.
         assertEquals( "core.err.33325", e.getMessageKey() );

      }

   }


   /**
    * make sure the exception is thrown if user is trying to edit PO and provides ReExpedite-To
    * location as vendor location
    *
    * @throws Exception
    */
   @Test
   public void testInvalidLocationTypeExceptionWhenEditPOWithReexpediteToAsVendor()
         throws Exception {
      try {

         // create order
         PurchaseOrderKey lPurchaseOrderKey = createOrder().withStatus( RefEventStatusKey.POAUTH )
               .shippingTo( iDockLocation ).build();

         // create the transfer object for edit PO
         EditOrderDetailsTO lEditPODetailsTO = new EditOrderDetailsTO();
         lEditPODetailsTO.setShipToLocation( DOCK_LOCATION );
         lEditPODetailsTO.setReExpediteToLocation( VENDOR_LOCATION, "Re-expedite To" );

         // edit the PO
         iOrderService.set( lPurchaseOrderKey, lEditPODetailsTO, iHr );
         fail( "Expected InvalidLocationTypeException" );

      } catch ( InvalidLocationTypeException e ) {

         // Verify that the exception was thrown and contains the expected message.
         assertEquals( "core.err.33325", e.getMessageKey() );

      }

   }


   /**
    * This tests that after you remove order line from the PO which has been issued and has a
    * pending shipment, and then the shipment lines are removed from the shipment and the shipment
    * line no sequence is reset to 1.
    *
    * @throws Exception
    */
   @Test
   public void testRemovePOLinesResetsShipmentLineNoSequence() throws Exception {
      // create a new SER part
      PartNoKey lPart = new PartNoBuilder().withInventoryClass( RefInvClassKey.SER )
            .withStatus( RefPartStatusKey.ACTV ).build();

      // create a purchase order and order line
      PurchaseOrderKey lOrder = createOrder().withStatus( RefEventStatusKey.POISSUED )
            .shippingTo( iDockLocation ).withIssueDate( new Date() ).build();
      PurchaseOrderLineKey lOrderLine = new OrderLineBuilder( lOrder )
            .withUnitPrice( BigDecimal.TEN ).withOrderQuantity( new BigDecimal( 3.0 ) ).build();

      // create shipment
      ShipmentKey lShipment =
            new ShipmentDomainBuilder().fromLocation( iVendorLocation ).toLocation( iDockLocation )
                  .withType( RefShipmentTypeKey.PURCHASE ).withOrder( lOrder ).build();

      // create 3 shipment lines for the shipment
      addShipmentLines( lShipment, lPart, 3, lOrderLine, 1.0 );

      // manually set the shipment line no sequence to 4, since shipment builder does not set this
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "context_key", lShipment.toString() );
      lArgs.add( "sequence_cd", "SHIPMENT_LINE_ID_SEQ" );
      lArgs.add( "next_value", "4" );
      MxDataAccess.getInstance().executeInsert( "utl_context_sequence", lArgs );

      // WHEN: remove PO lines
      OrderService.removePOLines( lOrder, new PurchaseOrderLineKey[] { lOrderLine }, iHr );

      // make sure the shipment line no sequence is reset to 1 after removal of PO line
      DataSetArgument lQArgs = new DataSetArgument();
      lQArgs.add( "context_key", lShipment.toString() );
      QuerySet lQs =
            QuerySetFactory.getInstance().executeQueryTable( "utl_context_sequence", lQArgs );
      assertEquals( "Expected 1 row", 1, lQs.getRowCount() );
      lQs.next();
      assertEquals( 1, lQs.getInt( "next_value" ) );
   }


   /**
    *
    * Test if the repair order lines removing functionality is working as expected. Repair order
    * lines can only be removed when the shipment is not sent. Remove repair order lines button will
    * be disabled otherwise.
    *
    * @throws TriggerException
    * @throws MxException
    *
    */
   @Test
   public void testRemoveRepairOrderLines() throws MxException, TriggerException {
      PartNoKey partKey = new PartNoBuilder().withInventoryClass( RefInvClassKey.SER )
            .withStatus( RefPartStatusKey.ACTV ).build();

      PurchaseOrderKey purchaseOrderKey = new OrderBuilder().withOrderType( RefPoTypeKey.REPAIR )
            .withVendor( iVendor ).shippingTo( iDockLocation ).withIssueDate( new Date() ).build();

      TaskKey taskKey1 = new TaskBuilder().withScheduledStart( new Date() )
            .withScheduledEnd( new Date() ).atLocation( iVendorLocation ).build();
      TaskKey taskKey2 = new TaskBuilder().withScheduledStart( new Date() )
            .withScheduledEnd( new Date() ).atLocation( iVendorLocation ).build();

      PurchaseOrderLineKey orderLineKey1 = new OrderLineBuilder( purchaseOrderKey )
            .withUnitPrice( BigDecimal.TEN ).withOrderQuantity( new BigDecimal( 3.0 ) )
            .withLineType( RefPoLineTypeKey.REPAIR ).withTask( taskKey1 ).build();
      new OrderLineBuilder( purchaseOrderKey ).withUnitPrice( BigDecimal.TEN )
            .withOrderQuantity( new BigDecimal( 2.0 ) ).withLineType( RefPoLineTypeKey.REPAIR )
            .withTask( taskKey2 ).build();

      OrderService.removePOLines( purchaseOrderKey, new PurchaseOrderLineKey[] { orderLineKey1 },
            iHr );

      boolean isDeleted = POLineTable.findByPrimaryKey( orderLineKey1 ).isDeleted();

      assertTrue( isDeleted );

      EventKey eventKey = new EventKey( taskKey1.getDbId(), taskKey1.getId() );

      DataSetArgument evtLocArgs = new DataSetArgument();
      evtLocArgs.add( eventKey, "event_db_id", "event_id" );
      QuerySet querySet = QuerySetFactory.getInstance().executeQuery( "evt_loc", evtLocArgs,
            "event_db_id", "event_id", "event_loc_id" );

      assertEquals( 0, querySet.getRowCount() );

      EvtEventTable evtEventTable = new JdbcEvtEventDao().findByPrimaryKey( eventKey );

      assertNull( evtEventTable.getSchedStartDt() );
      assertNull( evtEventTable.getSchedStartGdt() );
      assertNull( evtEventTable.getSchedEndDt() );
      assertNull( evtEventTable.getSchedEndGdt() );
   }


   private void addShipmentLines( ShipmentKey aShipment, PartNoKey aPart, int aShipmentLinesNumber,
         PurchaseOrderLineKey aOrderLine, double aExpectedQty ) {
      // add shipment lines
      for ( int i = 0; i < aShipmentLinesNumber; i++ ) {
         new ShipmentLineBuilder( aShipment ).forPart( aPart ).forOrderLine( aOrderLine )
               .withExpectedQuantity( aExpectedQty ).build();
      }
   }


   /**
    * make sure the exception is thrown if user is trying to create PO and provides ReExpedite-To
    * location as vendor location
    *
    * @throws Exception
    */
   @Test
   public void testInvalidLocationTypeExceptionWhenCreatePOWithReexpediteToAsVendor()
         throws Exception {
      try {

         OrderDetailsTO lPODetailsTO = new OrderDetailsTO();
         lPODetailsTO.setPriority( RefReqPriorityKey.NORMAL );
         lPODetailsTO.setOrganization( new OrgKey( 0, 1 ) );
         lPODetailsTO.setPurchasingContact( USERNAME_TESTUSER );
         lPODetailsTO.setShipToLocation( DOCK_LOCATION );
         lPODetailsTO.setReExpediteToLocation( VENDOR_LOCATION, "Re-expedite To" );
         lPODetailsTO.setVendor( iVendor );
         lPODetailsTO.setCurrency( new RefCurrencyKey( 10, "USD" ) );
         lPODetailsTO.setExchangeRate( BigDecimal.ONE );
         lPODetailsTO.setPoType( RefPoTypeKey.PURCHASE );

         OrderService.create( lPODetailsTO, "", iHr );
         fail( "Expected InvalidLocationTypeException" );

      } catch ( InvalidLocationTypeException e ) {

         // Verify that the exception was thrown and contains the expected message.
         assertEquals( "core.err.33325", e.getMessageKey() );

      }

   }


   /**
    * Tests that the CannotEditVendorInformationException is thrown when the user attempts to modify
    * vendor information on a repair order, where the repair order has already been issued to the
    * vendor.
    *
    * @throws Exception
    *            if an unexpected error occurs
    */
   @Test
   public void testCannotEditVendorInformationException() throws Exception {

      // create HR user
      HumanResourceKey lHr = Domain.createHumanResource();

      // create vendor
      VendorKey lVendor = new VendorBuilder().build();

      // create broker
      VendorKey lBroker = new VendorBuilder().asBroker().build();

      // create repair order
      PurchaseOrderKey lRepairOrder = new OrderBuilder().withBroker( lBroker ).withVendor( lVendor )
            .withOrderType( RefPoTypeKey.REPAIR ).withDescription( PO_NUMBER )
            .withStatus( RefEventStatusKey.POISSUED ).build();

      // set issued date
      PoHeaderTable lPOHeaderTable = PoHeaderTable.findByPrimaryKey( lRepairOrder );
      lPOHeaderTable.setIssuedDt( new Date() );
      lPOHeaderTable.update();

      // create TO
      EditOrderDetailsTO lEditPODetailsTO = new EditOrderDetailsTO();
      lEditPODetailsTO.setPONumber( PO_NUMBER );

      try {
         // execute OrderService.set()
         iOrderService.set( lRepairOrder, lEditPODetailsTO, lHr );

         // if this line executes, the exception wasn't thrown
         Assert.fail( "Expected CannotEditVendorInformationException" );

      } catch ( CannotEditVendorInformationException lException ) {
         // an exception is expected
      }

   }


   /**
    * Tests that the OrderService.reject() method will set all authorization levels for the PO in
    * po_auth table to historic, so that Request Authorization button will be displayed on Order
    * Details page.
    *
    * @throws Exception
    *            if an unexpected error occurs
    */
   @Test
   public void rejectOrderShouldSetAllAuthorizationLevelsToHistoric() throws Exception {

      // Given: order data prepared
      setupOrderData();

      // When: OrderService.reject() is executed
      iOrderService.reject( iOrder, iHrWithHighLevelAuth, null );

      // Then: assert that all authorization levels are set to historic
      assertFalse( hasNonHistoricAuthorizationLevel() );

   }


   /**
    *
    * GIVEN a part request is attached to a MPI generated PO line, WHEN the part number is changed
    * to an alternate stock part, but the changed part is not in the same part group of the
    * requested part in the part request, THEN the attached PR should be detached and a history note
    * should be added.
    *
    * @throws TriggerException
    *            if a trigger error occurs
    * @throws MxException
    *            if a mx error occurs
    *
    */
   @Test
   public void testPartNumberChangedInMPIGeneratedPurchaseOrderLines()
         throws MxException, TriggerException {

      // create two parts in one part group, but only part is in a stock
      PartNoKey lPartInStockAndPartGroup = createPartInStock( PART_IN_STOCK_AND_PART_GROUP );
      PartNoKey lPartInPartGroup = new PartNoBuilder().withInventoryClass( SERIALIZED )
            .withOemPartNo( PART_IN_PART_GROUP ).build();
      new PartGroupDomainBuilder( PART_GROUP ).withPartGroupName( PART_GROUP )
            .withInventoryClass( SERIALIZED ).withPartNo( lPartInStockAndPartGroup )
            .withPartNo( lPartInPartGroup ).build();

      // build a Purchase Order and its MPI generated order line for lPartInStockAndPartGroup
      PurchaseOrderKey lPO = createOrder().build();

      PurchaseOrderLineKey lMPIGeneratedPOLineWithPR =
            createOrderLine( lPO, lPartInStockAndPartGroup ).withMpKeySdesc( MP_KEY_SDESC ).build();

      // build a new part request for the PO line
      PartRequestKey lPartRequest =
            new PartRequestBuilder().forPurchaseLine( lMPIGeneratedPOLineWithPR ).build();

      // create new shipment for the created purchase order
      ShipmentKey lShipment = createShipment( lPO );

      // create shipment line for the created shipment
      ShipmentLineKey lShipmentLine =
            createShipmentLine( lPartInStockAndPartGroup, lMPIGeneratedPOLineWithPR, lShipment );

      // create a to-be-changed part which is in same stock of lPartInStockAndPartGroup, but
      // different part group, then update the order line
      PartNoKey lPartInStock = createPartInStock( PART_IN_STOCK );
      changePartNumber( lPartInStock, lPO, lMPIGeneratedPOLineWithPR );

      // assert that the PO line part number has changed
      assertEquals( lPartInStock,
            POLineTable.findByPrimaryKey( lMPIGeneratedPOLineWithPR ).getPartNo() );

      // assert that the shipment line part number has changed
      assertEquals( lPartInStock,
            ShipShipmentLineTable.findByPrimaryKey( lShipmentLine ).getPartNo() );

      // assert that a history note is added representing the part change
      EvtStage lPartReqStatus = new EvtStage( lPO.getEventKey() );
      lPartReqStatus.assertStageNote( i18n.get( "core.msg.PO_LINE_CHANGE_PART",
            lMPIGeneratedPOLineWithPR.getLineId(), PART_IN_STOCK_AND_PART_GROUP, PART_IN_STOCK ) );

      // assert that the part request has been detached from the purchase order
      assertEquals( null, ReqPartTable.findByPrimaryKey( lPartRequest ).getPurchaseOrder() );
   }


   /**
    *
    * GIVEN a shipment line attached to a PO line, WHEN the part number is changed in the PO line,
    * THEN the part number of the shipment line should be updated.
    *
    *
    * @throws TriggerException
    *            if a trigger error occurs
    * @throws MxException
    *            if a mx error occurs
    *
    */
   @Test
   public void testPartNumberChangedInShipmentLines() throws MxException, TriggerException {

      // create a part
      PartNoKey lPart = new PartNoBuilder().withInventoryClass( SERIALIZED ).build();

      // create a purchase order
      PurchaseOrderKey lOrder = createOrder().build();

      // create a PO line for the created purchase order
      PurchaseOrderLineKey lPoLine = createOrderLine( lOrder, lPart ).build();

      // create new shipment for the created purchase order
      ShipmentKey lShipment = createShipment( lOrder );

      // create shipment line for the created shipment
      ShipmentLineKey lShipmentLine = createShipmentLine( lPart, lPoLine, lShipment );

      // create a part to be changed with previously created part
      PartNoKey lNewPart = new PartNoBuilder().withInventoryClass( SERIALIZED ).build();

      changePartNumber( lNewPart, lOrder, lPoLine );

      // assert that the PO line part number has changed
      assertEquals( lNewPart, POLineTable.findByPrimaryKey( lPoLine ).getPartNo() );

      // assert that the shipment line part number has changed
      assertEquals( lNewPart, ShipShipmentLineTable.findByPrimaryKey( lShipmentLine ).getPartNo() );
   }


   /**
    *
    * Test that change contact for non-historic purchase order should update contact hr, and add
    * history note for the event
    *
    * @throws HistoricRecordException
    */
   @Test
   public void testChangeContactForNonHistoricPO() throws HistoricRecordException {

      // Given a purchase order, and new contact hr
      HumanResourceKey lContactHr = Domain.createHumanResource();
      PurchaseOrderKey lPurchaseOrder = new OrderBuilder().withStatus( RefEventStatusKey.POOPEN )
            .withContactHR( lContactHr ).build();

      HumanResourceKey lNewContactHr = Domain.createHumanResource();

      String lSystemNote = "The purchase contact for the order was changed";

      // When call change contact method
      OrderService.changeContact( lPurchaseOrder, lNewContactHr, lNewContactHr );

      // Then contact hr should be updated and system history note should be added
      PoHeaderTable lPOTable = PoHeaderTable.findByPrimaryKey( lPurchaseOrder );
      EvtStageTable lPOStageTable =
            EvtStageTable.findByPrimaryKey( new StageKey( lPurchaseOrder.getEventKey(), 1 ) );

      assertEquals( lNewContactHr, lPOTable.getContactHr() );
      Assert.assertTrue( lPOStageTable.getStageNote().contains( lSystemNote ) );

   }


   /**
    *
    * Test that change contact for historic purchase order should throw HistoricRecordException
    *
    * @throws HistoricRecordException
    */
   @Test( expected = HistoricRecordException.class )
   public void testChangeContactForHistoricPO() throws HistoricRecordException {

      // Given a purchase order, new contact hr
      HumanResourceKey lContactHr = Domain.createHumanResource();
      PurchaseOrderKey lPurchaseOrder = new OrderBuilder().withStatus( RefEventStatusKey.POCLOSED )
            .asHistoric().withContactHR( lContactHr ).build();

      HumanResourceKey lNewContactHr = Domain.createHumanResource();

      // When call change contact method
      OrderService.changeContact( lPurchaseOrder, lNewContactHr, lNewContactHr );

      // Then HistoricRecordException should be thrown
   }


   private ShipmentKey createShipment( PurchaseOrderKey aOrder ) {
      return new ShipmentDomainBuilder().withOrder( aOrder ).withStatus( RefEventStatusKey.IXPEND )
            .fromLocation( iVendorLocation ).toLocation( iDockLocation )
            .withType( RefShipmentTypeKey.PURCHASE ).build();
   }


   private ShipmentLineKey createShipmentLine( PartNoKey aPart, PurchaseOrderLineKey aPoLine,
         ShipmentKey aShipment ) {
      return new ShipmentLineBuilder( aShipment ).forPart( aPart ).forOrderLine( aPoLine ).build();
   }


   /**
    * Tests when Ship To has been changed in an order, the update of the shipment(segments) will be
    * triggered({@link #com.mxi.mx.core.services.shipment.ShipmentServiceTest} is testing the
    * updating functionality) and a new history note should be added to the order.
    *
    * @throws Exception
    */
   @Test
   public void set_changeShipTo() throws Exception {
      // ARRANGE
      PurchaseOrderKey lOrder = createOrder( iOldShipToLocation, null );
      EditOrderDetailsTO lEditOrderDetailsTO = createEditOrderDetailsTO( NEWSHIPTO, null );

      // ACT
      iOrderServiceWithMockShipmentService.set( lOrder, lEditOrderDetailsTO, iHr );

      // ASSERT
      // verify the update method was called
      verify( iMockShipmentService ).updateInboundShipmentWhenOrderShippingChanged( lOrder,
            iNewShipToLocation, null, iVendorLocation, iHr );
      // assert a new history note of this change has been added
      String lMessage = i18n.get( PO_LINE_CHANGE_DESTINATION_MESSAGE,
            InvLocTable.findByPrimaryKey( iOldShipToLocation ).getLocCd(),
            InvLocTable.findByPrimaryKey( iNewShipToLocation ).getLocCd() );
      assertHistoryNote( lOrder, lMessage );
   }


   /**
    * Tests when Re-Expedite To location has been added in an order, the update of the
    * shipment(segments) will be
    * triggered({@link #com.mxi.mx.core.services.shipment.ShipmentServiceTest} is testing the
    * updating functionality) and a new history note should be added to the order.
    *
    * @throws Exception
    */
   @Test
   public void set_addReExpediteTo() throws Exception {
      // ARRANGE
      PurchaseOrderKey lOrder = createOrder( iOldShipToLocation, null );
      EditOrderDetailsTO lEditOrderDetailsTO =
            createEditOrderDetailsTO( OLDSHIPTO, NEWREEXPEDITETO );

      // ACT
      iOrderServiceWithMockShipmentService.set( lOrder, lEditOrderDetailsTO, iHr );

      // ASSERT
      // verify the update method was called
      verify( iMockShipmentService ).updateInboundShipmentWhenOrderShippingChanged( lOrder,
            iOldShipToLocation, iNewReExpediteToLocation, iVendorLocation, iHr );
      // assert a new history note of this change has been added
      String lMessage = i18n.get( PO_LINE_CHANGE_REEXPEDITE_MESSAGE,
            InvLocTable.findByPrimaryKey( iNewReExpediteToLocation ).getLocCd() );
      assertHistoryNote( lOrder, lMessage );
   }


   /**
    * Tests when Re-Expedite To location has been changed in an order, the update of the
    * shipment(segments) will be
    * triggered({@link #com.mxi.mx.core.services.shipment.ShipmentServiceTest} is testing the
    * updating functionality) and a new history note should be added to the order.
    *
    * @throws Exception
    */
   @Test
   public void set_changeReExpediteTo() throws Exception {
      // ARRANGE
      PurchaseOrderKey lOrder = createOrder( iOldShipToLocation, iOldReExpediteToLocation );
      EditOrderDetailsTO lEditOrderDetailsTO =
            createEditOrderDetailsTO( OLDSHIPTO, NEWREEXPEDITETO );

      // ACT
      iOrderServiceWithMockShipmentService.set( lOrder, lEditOrderDetailsTO, iHr );

      // ASSERT
      // verify the update method was called
      verify( iMockShipmentService ).updateInboundShipmentWhenOrderShippingChanged( lOrder,
            iOldShipToLocation, iNewReExpediteToLocation, iVendorLocation, iHr );
      // assert a new history note of this change has been added
      String lMessage = i18n.get( PO_LINE_CHANGE_REEXPEDITE_MESSAGE,
            InvLocTable.findByPrimaryKey( iNewReExpediteToLocation ).getLocCd() );
      assertHistoryNote( lOrder, lMessage );
   }


   /**
    * Tests when Re-Expedite To location has been removed in an order, the update of the
    * shipment(segments) will be
    * triggered({@link #com.mxi.mx.core.services.shipment.ShipmentServiceTest} is testing the
    * updating functionality) and a new history note should be added to the order.
    *
    * @throws Exception
    */
   @Test
   public void set_removeReExpediteTo() throws Exception {
      // ARRANGE
      PurchaseOrderKey lOrder = createOrder( iOldShipToLocation, iOldReExpediteToLocation );
      EditOrderDetailsTO lEditOrderDetailsTO = createEditOrderDetailsTO( OLDSHIPTO, null );

      // ACT
      iOrderServiceWithMockShipmentService.set( lOrder, lEditOrderDetailsTO, iHr );

      // ASSERT
      // verify the update method was called
      verify( iMockShipmentService ).updateInboundShipmentWhenOrderShippingChanged( lOrder,
            iOldShipToLocation, null, iVendorLocation, iHr );
      // assert a new history note of this change has been added
      String lMessage = i18n.get( PO_LINE_CLEAR_REEXPEDITE_MESSAGE );
      assertHistoryNote( lOrder, lMessage );
   }


   /**
    * Tests when Ship To has been changed in an order and Re-Expedite To Location has been added,
    * the update of the shipment(segments) will be
    * triggered({@link #com.mxi.mx.core.services.shipment.ShipmentServiceTest} is testing the
    * updating functionality) and two new history notes should have been added to the order.
    *
    * @throws Exception
    */
   @Test
   public void set_changeShipToAndAddReExpediteTo() throws Exception {
      // ARRANGE
      PurchaseOrderKey lOrder = createOrder( iOldShipToLocation, null );
      EditOrderDetailsTO lEditOrderDetailsTO =
            createEditOrderDetailsTO( NEWSHIPTO, NEWREEXPEDITETO );

      // ACT
      iOrderServiceWithMockShipmentService.set( lOrder, lEditOrderDetailsTO, iHr );

      // ASSERT
      // verify the update method was called
      verify( iMockShipmentService ).updateInboundShipmentWhenOrderShippingChanged( lOrder,
            iNewShipToLocation, iNewReExpediteToLocation, iVendorLocation, iHr );
      // assert new history notes for this change have been added
      String[] lMessages = new String[2];
      lMessages[0] = i18n.get( PO_LINE_CHANGE_DESTINATION_MESSAGE,
            InvLocTable.findByPrimaryKey( iOldShipToLocation ).getLocCd(),
            InvLocTable.findByPrimaryKey( iNewShipToLocation ).getLocCd() );
      lMessages[1] = i18n.get( PO_LINE_CHANGE_REEXPEDITE_MESSAGE,
            InvLocTable.findByPrimaryKey( iNewReExpediteToLocation ).getLocCd() );

      assertMultipleHistoryNotes( lOrder, lMessages, 2 );
   }


   /**
    * Tests when Ship To has been changed in an order and Re-Expedite To Location has been changed,
    * the update of the shipment(segments) will be
    * triggered({@link #com.mxi.mx.core.services.shipment.ShipmentServiceTest} is testing the
    * updating functionality) and two new history notes should have been added to the order.
    *
    * @throws Exception
    */
   @Test
   public void set_changeShipToAndChangeReExpediteTo() throws Exception {
      // ARRANGE
      PurchaseOrderKey lOrder = createOrder( iOldShipToLocation, iOldReExpediteToLocation );
      EditOrderDetailsTO lEditOrderDetailsTO =
            createEditOrderDetailsTO( NEWSHIPTO, NEWREEXPEDITETO );

      // ACT
      iOrderServiceWithMockShipmentService.set( lOrder, lEditOrderDetailsTO, iHr );

      // ASSERT
      // verify the update method was called
      verify( iMockShipmentService ).updateInboundShipmentWhenOrderShippingChanged( lOrder,
            iNewShipToLocation, iNewReExpediteToLocation, iVendorLocation, iHr );
      // assert new history notes for this change have been added
      String[] lMessages = new String[2];
      lMessages[0] = i18n.get( PO_LINE_CHANGE_DESTINATION_MESSAGE,
            InvLocTable.findByPrimaryKey( iOldShipToLocation ).getLocCd(),
            InvLocTable.findByPrimaryKey( iNewShipToLocation ).getLocCd() );
      lMessages[1] = i18n.get( PO_LINE_CHANGE_REEXPEDITE_MESSAGE,
            InvLocTable.findByPrimaryKey( iNewReExpediteToLocation ).getLocCd() );

      assertMultipleHistoryNotes( lOrder, lMessages, 2 );
   }


   /**
    * Tests when Ship To has been changed in an order and Re-Expedite To Location has been Removed,
    * the update of the shipment(segments) will be
    * triggered({@link #com.mxi.mx.core.services.shipment.ShipmentServiceTest} is testing the
    * updating functionality) and two new history notes should have been added to the order.
    *
    * @throws Exception
    */
   @Test
   public void set_changeShipToAndRemoveReExpediteTo() throws Exception {
      // ARRANGE
      PurchaseOrderKey lOrder = createOrder( iOldShipToLocation, iOldReExpediteToLocation );
      EditOrderDetailsTO lEditOrderDetailsTO = createEditOrderDetailsTO( NEWSHIPTO, null );

      // ACT
      iOrderServiceWithMockShipmentService.set( lOrder, lEditOrderDetailsTO, iHr );

      // ASSERT
      // verify the update method was called
      verify( iMockShipmentService ).updateInboundShipmentWhenOrderShippingChanged( lOrder,
            iNewShipToLocation, null, iVendorLocation, iHr );
      // assert new history notes for this change have been added
      String[] lMessages = new String[2];
      lMessages[0] = i18n.get( PO_LINE_CHANGE_DESTINATION_MESSAGE,
            InvLocTable.findByPrimaryKey( iOldShipToLocation ).getLocCd(),
            InvLocTable.findByPrimaryKey( iNewShipToLocation ).getLocCd() );
      lMessages[1] = i18n.get( PO_LINE_CLEAR_REEXPEDITE_MESSAGE );

      assertMultipleHistoryNotes( lOrder, lMessages, 2 );
   }


   /**
    * test the scenario: successfully update the field "Ship From" of both inbound shipment and its
    * first segment in routing for the purchase order
    *
    * @throws CannotChangeVendorException
    */
   @Test
   public void updateShipFrom() throws CannotChangeVendorException {
      PurchaseOrderKey lOrder = Domain.createPurchaseOrder();
      ShipmentKey lInboundShipment = Domain.createShipment( lShipment -> {
         lShipment.setPurchaseOrder( lOrder );
         lShipment.addShipmentSegment( segment -> {
            segment.setFromLocation( iVendorLocation );
            segment.setToLocation( iDockLocation );
            segment.setStatus( RefShipSegmentStatusKey.PENDING );
         } );
      } );

      ShipmentKey lOutboundShipment = Domain.createShipment( lShipment -> {
         lShipment.setPurchaseOrder( lOrder );
         lShipment.addShipmentSegment( segment -> {
            segment.setFromLocation( iDockLocation );
            segment.setToLocation( iVendorLocation );
            segment.setStatus( RefShipSegmentStatusKey.PENDING );
         } );
      } );

      // update PO with new vendor
      iOrderService.updateShipFrom( lOrder, iAnotherVendor );

      // assert inbound shipment and segment have been updated, outbound shipment has no change
      LocationKey lInboundShipmentShipFromLocation = EvtLocTable
            .findByPrimaryKey( new EventLocationKey( lInboundShipment.getEventKey(), 1 ) )
            .getLocation();
      LocationKey lInboundShipmentShipToLocation = EvtLocTable
            .findByPrimaryKey( new EventLocationKey( lInboundShipment.getEventKey(), 2 ) )
            .getLocation();
      LocationKey lOutboundShipmentShipFromLocation = EvtLocTable
            .findByPrimaryKey( new EventLocationKey( lOutboundShipment.getEventKey(), 1 ) )
            .getLocation();
      LocationKey lOutboundShipmentShipToLocation = EvtLocTable
            .findByPrimaryKey( new EventLocationKey( lOutboundShipment.getEventKey(), 2 ) )
            .getLocation();

      List<Segment> lInboundShipSegments =
            new JdbcShipmentRepository().getSegments( lInboundShipment );
      assertEquals( 1, lInboundShipSegments.size() );
      ShipSegmentTable lInboundShipSegmentTable =
            ShipSegmentTable.findByPrimaryKey( lInboundShipSegments.get( 0 ).getSegmentKey() );
      LocationKey lInboundSegmentShipFromLocation = lInboundShipSegmentTable.getShipFrom();
      LocationKey lInboundSegmentShipToLocation = lInboundShipSegmentTable.getShipTo();

      assertEquals( iAnotherVendorLocation, lInboundShipmentShipFromLocation );
      assertEquals( iDockLocation, lInboundShipmentShipToLocation );
      assertEquals( iAnotherVendorLocation, lInboundSegmentShipFromLocation );
      assertEquals( iDockLocation, lInboundSegmentShipToLocation );
      assertEquals( iDockLocation, lOutboundShipmentShipFromLocation );
      assertEquals( iVendorLocation, lOutboundShipmentShipToLocation );
   }


   /**
    * test the scenario: throw a specific exception if inbound shipment is already sent from vendor
    *
    * @throws CannotChangeVendorException
    */
   @Test( expected = CannotChangeVendorException.class )
   public void updateShipFrom_inboundShipmentIsCompleted() throws CannotChangeVendorException {
      PurchaseOrderKey lOrder = Domain.createPurchaseOrder();
      Domain.createShipment( lShipment -> {
         lShipment.setPurchaseOrder( lOrder );
         lShipment.setStatus( RefEventStatusKey.IXCMPLT );
         lShipment.addShipmentSegment( segment -> {
            segment.setFromLocation( iVendorLocation );
            segment.setToLocation( iDockLocation );
         } );
      } );

      // update PO with new vendor
      iOrderService.updateShipFrom( lOrder, iAnotherVendor );
   }


   /**
    * Create an order
    *
    * @param aShipToLocation
    *           the ship to location
    * @param aReExpediteToLocation
    *           the re-expedite to location
    * @return the order key
    */
   private PurchaseOrderKey createOrder( LocationKey aShipToLocation,
         LocationKey aReExpediteToLocation ) {
      return Domain.createPurchaseOrder( aOrder -> {
         aOrder.vendor( iVendor );
         aOrder.shippingTo( aShipToLocation );
         aOrder.setOrganization( Domain.createOrganization() );
         aOrder.exchangeRate( BigDecimal.ONE );
         aOrder.status( RefEventStatusKey.POISSUED );
         aOrder.usingCurrency( iCurrency );
         aOrder.reexpediteTo( aReExpediteToLocation );
      } );
   }


   /**
    * Assert the history note of the order
    *
    * @param aOrderKey
    *           the order key
    * @param aExpectedHistoryNote
    *           the expected history note of the order
    */
   private void assertHistoryNote( PurchaseOrderKey aOrderKey, String aExpectedHistoryNote ) {
      DataSet lEventStageTable = EvtStageTable.getStageSnapshot( aOrderKey.getEventKey() );
      assertEquals( 1, lEventStageTable.getRowCount() );
      lEventStageTable.next();
      assertEquals( aExpectedHistoryNote, lEventStageTable.getString( "stage_note" ) );
   }


   /**
    * Assert multiple history notes of the order
    *
    * @param aOrderKey
    *           the order key
    * @param aExpectedHistoryNotes
    *           the expected history note of the order
    * @param aExpectedCount
    *           the row count of history notes
    */
   private void assertMultipleHistoryNotes( PurchaseOrderKey aOrderKey,
         String[] aExpectedHistoryNotes, int aExpectedCount ) {
      DataSet lEventStageTable = EvtStageTable.getStageSnapshot( aOrderKey.getEventKey() );
      assertEquals( aExpectedCount, lEventStageTable.getRowCount() );
      for ( String lExpectedHistoryNote : aExpectedHistoryNotes ) {
         lEventStageTable.next();
         assertEquals( lExpectedHistoryNote, lEventStageTable.getString( "stage_note" ) );
      }
   }


   /**
    * Prepare the EditOrderDetailsTO
    *
    * @param aNewShipToLocationCode
    *           the new ship to location
    * @param aNewReExpediteToLocationCode
    *           the new re-expedite to location
    * @return the EditOrderDetailsTO
    * @throws Exception
    */
   private EditOrderDetailsTO createEditOrderDetailsTO( String aNewShipToLocationCode,
         String aNewReExpediteToLocationCode ) throws Exception {
      EditOrderDetailsTO lEditOrderDetailsTO = new EditOrderDetailsTO();
      lEditOrderDetailsTO.setVendor( iVendor );
      lEditOrderDetailsTO.setExchangeRate( BigDecimal.ONE );
      lEditOrderDetailsTO.setCurrency( iCurrency );
      lEditOrderDetailsTO.setShipToLocation( aNewShipToLocationCode );
      lEditOrderDetailsTO.setReExpediteToLocation( aNewReExpediteToLocationCode, null );
      return lEditOrderDetailsTO;
   }


   /**
    * Build the transfer object,update part number and call the setPOLines method
    *
    * @param lNewPartKey
    *           PartNoKey to be updated
    * @param lPO
    *           Purchase order key
    * @param lPOLine
    *           Purchase order line key
    * @param lPOLineTable
    *           The PO line table
    * @throws MandatoryArgumentException
    * @throws NegativeValueException
    * @throws MxException
    * @throws TriggerException
    */
   private void changePartNumber( PartNoKey lNewPartKey, PurchaseOrderKey lPO,
         PurchaseOrderLineKey lPOLine )
         throws MandatoryArgumentException, NegativeValueException, MxException, TriggerException {

      POLineTable lPOLineTable = POLineTable.findByPrimaryKey( lPOLine );

      EditOrderLineTO lEditOrderLineTO = new EditOrderLineTO( lPOLine );
      lEditOrderLineTO.setUnitPrice( lPOLineTable.getUnitPrice(), "unit price" );
      lEditOrderLineTO.setQuantityUnit( lPOLineTable.getQtyUnit(), "quantity unit" );
      lEditOrderLineTO.setQuantity( lPOLineTable.getOrderQt().doubleValue(), "order quantity" );
      lEditOrderLineTO.setPart( lNewPartKey, PART_IN_STOCK );
      OrderService.setPOLines( lPO, new EditOrderLineTO[] { lEditOrderLineTO }, iHr );

   }


   private PartNoKey createPartInStock( String aOemPartNo ) {
      return new PartNoBuilder()
            .withInventoryClass( SERIALIZED ).withStock( new StockBuilder()
                  .withInvClass( SERIALIZED ).withStockName( STOCK_NAME ).build() )
            .withOemPartNo( aOemPartNo ).build();
   }


   /**
    * Set up order data for test
    *
    * @throws Exception
    *            If an error occurs.
    */
   private void setupOrderData() throws Exception {
      // create HR with low level of authorization
      HumanResourceKey lHrWithLowLevelAuth = Domain.createHumanResource();

      // create HR with high level of authorization
      iHrWithHighLevelAuth = Domain.createHumanResource();

      // create a supply location
      LocationKey lSupplyLocation = new LocationDomainBuilder().withType( RefLocTypeKey.AIRPORT )
            .isSupplyLocation().build();

      // build purchase order with status OPEN
      iOrder = new OrderBuilder().withVendor( iVendor ).withIssueDate( new Date() )
            .shippingTo( lSupplyLocation ).withStatus( RefEventStatusKey.POOPEN ).build();

      // build finance account
      FncAccountKey lAccount = new AccountBuilder().withType( RefAccountTypeKey.INVASSET )
            .withCode( "TESTACCOUNT" ).isDefault().build();

      // build part
      PartNoKey lPartNoKey = new PartNoBuilder().withOemPartNo( "SER_PART" )
            .withShortDescription( "SER part for testing" ).withInventoryClass( RefInvClassKey.SER )
            .withFinancialType( RefFinanceTypeKey.CONSUM ).withUnitType( RefQtyUnitKey.EA )
            .withRepairBool( false ).withAssetAccount( lAccount )
            .withStatus( RefPartStatusKey.ACTV ).build();

      // build order line
      createOrderLine( iOrder, lPartNoKey ).build();

      // build order authorization approved
      PoAuthTable lPoAuthTable = PoAuthTable.create( iOrder );
      lPoAuthTable.setAuthHr( lHrWithLowLevelAuth );
      lPoAuthTable.setAuthDate( new Date() );
      lPoAuthTable.setPoAuthLvlStatus( RefPoAuthLvlStatusKey.APPROVED );
      PurchaseOrderAuthKey lOrderAuthLowLevelHrKey = lPoAuthTable.insert();

      // requested
      lPoAuthTable = PoAuthTable.create( iOrder );
      lPoAuthTable.setAuthHr( iHrWithHighLevelAuth );
      lPoAuthTable.setPoAuthLvlStatus( RefPoAuthLvlStatusKey.REQUESTED );
      PurchaseOrderAuthKey lOrderAuthHighLevelHrKey = lPoAuthTable.insert();

      EvtEventDao lEvtEventDao = InjectorContainer.get().getInstance( EvtEventDao.class );
      // pre-assumptions
      // 1 - order status is POOPEN
      assertEquals( RefEventStatusKey.POOPEN,
            lEvtEventDao.findByPrimaryKey( iOrder.getEventKey() ).getEventStatusCd() );

      // 2 - order has been requested for authorization
      assertEquals( PoAuthTable.findByPrimaryKey( lOrderAuthLowLevelHrKey ).getAuthLvlStatusKey(),
            RefPoAuthLvlStatusKey.APPROVED );
      assertFalse( PoAuthTable.findByPrimaryKey( lOrderAuthLowLevelHrKey ).isHistoric() );
      assertEquals( PoAuthTable.findByPrimaryKey( lOrderAuthHighLevelHrKey ).getAuthLvlStatusKey(),
            RefPoAuthLvlStatusKey.REQUESTED );

   }


   /**
    * test that getPOByTask() returns the correct PO and both cancel and active PO types
    *
    * @throws Exception
    */
   @Test
   public void testGetPOByTaskActiveTask() throws Exception {

      TaskKey lTaskKey = new TaskBuilder().withBarcode( TASK_BARCODE )
            .withStatus( RefEventStatusKey.ACTV ).withWorkOrderNumber( PO_NUMBER ).build();

      // create order
      PurchaseOrderKey lPurchaseOrderKey =
            new OrderBuilder().withStatus( RefEventStatusKey.POOPEN ).withVendor( iVendor )
                  .shippingTo( iDockLocation ).withOrderType( RefPoTypeKey.REPAIR ).build();

      new OrderLineBuilder( lPurchaseOrderKey ).withTask( lTaskKey )
            .withOrderQuantity( new BigDecimal( 1 ) ).build();
      assertEquals( lPurchaseOrderKey, OrderService.getPOByTask( lTaskKey, true ) );

      TaskKey lTaskKey2 = new TaskBuilder().withBarcode( TASK_BARCODE )
            .withStatus( RefEventStatusKey.CANCEL ).withWorkOrderNumber( PO_NUMBER ).build();

      // create order
      PurchaseOrderKey lPurchaseOrderKey2 =
            new OrderBuilder().withStatus( RefEventStatusKey.POCANCEL ).withVendor( iVendor )
                  .shippingTo( iDockLocation ).withOrderType( RefPoTypeKey.REPAIR ).build();

      new OrderLineBuilder( lPurchaseOrderKey2 ).withTask( lTaskKey2 )
            .withOrderQuantity( new BigDecimal( 1 ) ).build();
      assertEquals( lPurchaseOrderKey2, OrderService.getPOByTask( lTaskKey2, true ) );
   }


   /**
    * test that getPOByTask() does not return cancelled PO's when the include cancelled argument is
    * set to false
    *
    * @throws Exception
    */
   @Test
   public void testGetPOByTaskDisableIncludeCancelTask() throws Exception {

      TaskKey lTaskKey = new TaskBuilder().withBarcode( TASK_BARCODE )
            .withStatus( RefEventStatusKey.ACTV ).withWorkOrderNumber( PO_NUMBER ).build();

      // create order
      PurchaseOrderKey lPurchaseOrderKey =
            new OrderBuilder().withStatus( RefEventStatusKey.POOPEN ).withVendor( iVendor )
                  .shippingTo( iDockLocation ).withOrderType( RefPoTypeKey.REPAIR ).build();

      new OrderLineBuilder( lPurchaseOrderKey ).withTask( lTaskKey )
            .withOrderQuantity( new BigDecimal( 1 ) ).build();
      assertEquals( lPurchaseOrderKey, OrderService.getPOByTask( lTaskKey, false ) );

      TaskKey lTaskKey2 =
            new TaskBuilder().withBarcode( TASK_BARCODE ).withStatus( RefEventStatusKey.CANCEL )
                  .asHistoric( RefEventStatusKey.CANCEL ).withWorkOrderNumber( PO_NUMBER ).build();

      // create order
      PurchaseOrderKey lPurchaseOrderKey2 = new OrderBuilder()
            .withStatus( RefEventStatusKey.POCANCEL ).asHistoric().withVendor( iVendor )
            .shippingTo( iDockLocation ).withOrderType( RefPoTypeKey.REPAIR ).build();

      new OrderLineBuilder( lPurchaseOrderKey2 ).withTask( lTaskKey2 )
            .withOrderQuantity( new BigDecimal( 1 ) ).build();
      assertEquals( null, OrderService.getPOByTask( lTaskKey2, false ) );
   }


   @Test( expected = CantCancelOrderWithRcvdInvWaitingForInspException.class )
   public void testCancel_partLineWithNonZeroPreInspectionQuantity()
         throws MxException, TriggerException {
      PurchaseOrderKey order = createOrder().withStatus( RefEventStatusKey.POISSUED )
            .shippingTo( iDockLocation ).withIssueDate( new Date() ).build();
      PartNoKey part = createPartInStock( PART_IN_STOCK );

      new OrderLineBuilder( order ).withUnitPrice( BigDecimal.TEN ).forPart( part )
            .withPreInspQty( BigDecimal.ONE ).withOrderQuantity( new BigDecimal( 3.0 ) ).build();

      String note = "";
      String systemNote = "";

      OrderService.cancel( order, iHr, RefStageReasonKey.OBSOLETE, note, systemNote );
   }


   /**
    * Asserts that no exception is thrown.
    */
   @Test
   public void testCancel_partLineWithZeroPreInspectionQuantity()
         throws MxException, TriggerException {
      PurchaseOrderKey order = createOrder().withStatus( RefEventStatusKey.POISSUED )
            .shippingTo( iDockLocation ).withIssueDate( new Date() ).build();
      PartNoKey part = createPartInStock( PART_IN_STOCK );

      new OrderLineBuilder( order ).withUnitPrice( BigDecimal.TEN ).forPart( part )
            .withPreInspQty( BigDecimal.ZERO ).withOrderQuantity( new BigDecimal( 3.0 ) ).build();

      String note = "";
      String systemNote = "";

      OrderService.cancel( order, iHr, RefStageReasonKey.OBSOLETE, note, systemNote );
   }


   /**
    * Asserts that no exception is thrown.
    */
   @Test
   public void testCancel_miscellaneousLineWithNonZeroPreInspectionQuantityLine()
         throws MxException, TriggerException {
      PurchaseOrderKey order = createOrder().withStatus( RefEventStatusKey.POISSUED )
            .shippingTo( iDockLocation ).withIssueDate( new Date() ).build();
      PartNoKey part = createPartInStock( PART_IN_STOCK );

      new OrderLineBuilder( order ).withUnitPrice( BigDecimal.TEN )
            .withPreInspQty( BigDecimal.ZERO ).forPart( part )
            .withOrderQuantity( new BigDecimal( 3.0 ) ).build();
      new OrderLineBuilder( order ).withUnitPrice( BigDecimal.TEN )
            .withLineType( RefPoLineTypeKey.MISC ).withPreInspQty( BigDecimal.ONE )
            .withOrderQuantity( new BigDecimal( 3.0 ) ).build();

      String note = "";
      String systemNote = "";

      OrderService.cancel( order, iHr, RefStageReasonKey.OBSOLETE, note, systemNote );
   }


   /**
    * Asserts that no exception is thrown.
    */
   @Test
   public void testCancel_miscellaneousLineWithZeroPreInspectionQuantityLine()
         throws MxException, TriggerException {
      PurchaseOrderKey order = createOrder().withStatus( RefEventStatusKey.POISSUED )
            .shippingTo( iDockLocation ).withIssueDate( new Date() ).build();
      PartNoKey part = createPartInStock( PART_IN_STOCK );

      new OrderLineBuilder( order ).withUnitPrice( BigDecimal.TEN )
            .withPreInspQty( BigDecimal.ZERO ).forPart( part )
            .withOrderQuantity( new BigDecimal( 3.0 ) ).build();
      new OrderLineBuilder( order ).withUnitPrice( BigDecimal.TEN )
            .withLineType( RefPoLineTypeKey.MISC ).withPreInspQty( BigDecimal.ZERO )
            .withOrderQuantity( new BigDecimal( 3.0 ) ).build();

      String note = "";
      String systemNote = "";

      OrderService.cancel( order, iHr, RefStageReasonKey.OBSOLETE, note, systemNote );
   }


   /**
    * Asserts that no exception is thrown... {@link RefEventStatusKey#PORECEIVED} is a valid status
    * when all of a purchase order's lines are miscellaneous (i.e. it's still legal to cancel a
    * purchase order in this scenario).
    */
   @Test
   public void testCancel_poReceivedAllMiscellaneousLines() throws MxException, TriggerException {
      PurchaseOrderKey order = createOrder().withStatus( RefEventStatusKey.PORECEIVED )
            .shippingTo( iDockLocation ).withIssueDate( new Date() ).build();

      new OrderLineBuilder( order ).withUnitPrice( BigDecimal.TEN )
            .withLineType( RefPoLineTypeKey.MISC ).withPreInspQty( BigDecimal.ZERO )
            .withOrderQuantity( new BigDecimal( 3.0 ) ).build();
      new OrderLineBuilder( order ).withUnitPrice( BigDecimal.TEN )
            .withLineType( RefPoLineTypeKey.MISC ).withPreInspQty( BigDecimal.ZERO )
            .withOrderQuantity( new BigDecimal( 3.0 ) ).build();

      String note = "";
      String systemNote = "";

      OrderService.cancel( order, iHr, RefStageReasonKey.OBSOLETE, note, systemNote );
   }


   /**
    * {@link RefEventStatusKey#PORECEIVED} is not a valid status when there is at least one part
    * line in a purchase order (i.e. the order can no longer be cancelled).
    */
   @Test( expected = InvalidStatusException.class )
   public void testCancel_poReceivedNotAllMiscellaneousLines()
         throws MxException, TriggerException {
      PurchaseOrderKey order = createOrder().withStatus( RefEventStatusKey.PORECEIVED )
            .shippingTo( iDockLocation ).withIssueDate( new Date() ).build();
      PartNoKey part = createPartInStock( PART_IN_STOCK );

      new OrderLineBuilder( order ).withUnitPrice( BigDecimal.TEN )
            .withPreInspQty( BigDecimal.ZERO ).forPart( part )
            .withOrderQuantity( new BigDecimal( 3.0 ) ).build();
      new OrderLineBuilder( order ).withUnitPrice( BigDecimal.TEN )
            .withLineType( RefPoLineTypeKey.MISC ).withPreInspQty( BigDecimal.ZERO )
            .withOrderQuantity( new BigDecimal( 3.0 ) ).build();

      String note = "";
      String systemNote = "";

      OrderService.cancel( order, iHr, RefStageReasonKey.OBSOLETE, note, systemNote );
   }


   /**
    * Asserts that no exception is thrown.
    */
   @Test
   public void testCancel_validStatus() throws MxException, TriggerException {
      PurchaseOrderKey order = createOrder().withStatus( RefEventStatusKey.POISSUED )
            .shippingTo( iDockLocation ).withIssueDate( new Date() ).build();
      PartNoKey part = createPartInStock( PART_IN_STOCK );

      new OrderLineBuilder( order ).withUnitPrice( BigDecimal.TEN )
            .withPreInspQty( BigDecimal.ZERO ).forPart( part )
            .withOrderQuantity( new BigDecimal( 3.0 ) ).build();
      new OrderLineBuilder( order ).withUnitPrice( BigDecimal.TEN )
            .withLineType( RefPoLineTypeKey.MISC ).withPreInspQty( BigDecimal.ZERO )
            .withOrderQuantity( new BigDecimal( 3.0 ) ).build();

      String note = "";
      String systemNote = "";

      OrderService.cancel( order, iHr, RefStageReasonKey.OBSOLETE, note, systemNote );
   }


   @Test( expected = InvalidStatusException.class )
   public void testCancel_notPoReceivedInvalidStatus() throws MxException, TriggerException {
      PurchaseOrderKey order = createOrder().withStatus( RefEventStatusKey.POCLOSED )
            .shippingTo( iDockLocation ).withIssueDate( new Date() ).build();
      PartNoKey part = createPartInStock( PART_IN_STOCK );

      new OrderLineBuilder( order ).withUnitPrice( BigDecimal.TEN )
            .withPreInspQty( BigDecimal.ZERO ).forPart( part )
            .withOrderQuantity( new BigDecimal( 3.0 ) ).build();
      new OrderLineBuilder( order ).withUnitPrice( BigDecimal.TEN )
            .withLineType( RefPoLineTypeKey.MISC ).withPreInspQty( BigDecimal.ZERO )
            .withOrderQuantity( new BigDecimal( 3.0 ) ).build();

      String note = "";
      String systemNote = "";

      OrderService.cancel( order, iHr, RefStageReasonKey.OBSOLETE, note, systemNote );
   }


   private OrderBuilder createOrder() {
      return new OrderBuilder().withOrderType( RefPoTypeKey.PURCHASE ).withVendor( iVendor );
   }


   /**
    * Creates an OrderLine builder
    *
    * @param aOrderKey
    *           the order key
    * @param lPartNoKey
    *           the part key
    * @return
    */
   private OrderLineBuilder createOrderLine( PurchaseOrderKey aOrderKey, PartNoKey aPartNoKey ) {
      return new OrderLineBuilder( aOrderKey ).withLineType( RefPoLineTypeKey.PURCHASE )
            .forPart( aPartNoKey ).withUnitType( RefQtyUnitKey.EA ).withUnitPrice( BigDecimal.TEN )
            .withOrderQuantity( BigDecimal.TEN ).promisedBy( new Date() );
   }


   /**
    *
    * Return true if any row from po_auth table for the PO is non-historic (active)
    *
    * @return
    */
   private boolean hasNonHistoricAuthorizationLevel() {
      DataSetArgument lWhereArgs = new DataSetArgument();
      lWhereArgs.add( iOrder.getPKWhereArg() );
      lWhereArgs.add( "hist_bool", false );

      QuerySet lDs = QuerySetFactory.getInstance().executeQuery( new String[] { "ROWID" },
            "po_auth", lWhereArgs );

      return lDs.next();
   }
}
