package com.mxi.mx.core.services.inventory.condition;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import com.mxi.am.domain.builder.EventRelationshipBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.OrderBuilder;
import com.mxi.am.domain.builder.OrderLineBuilder;
import com.mxi.am.domain.builder.OwnerDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.PartRequestBuilder;
import com.mxi.am.domain.builder.SdFaultBuilder;
import com.mxi.am.domain.builder.StockBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.i18n.i18n;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefEventTypeKey;
import com.mxi.mx.core.key.RefFailureSeverityKey;
import com.mxi.mx.core.key.RefFinanceTypeKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefPoTypeKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefRelationTypeKey;
import com.mxi.mx.core.key.RefReqTypeKey;
import com.mxi.mx.core.key.RefStageReasonKey;
import com.mxi.mx.core.key.StockNoKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.services.inventory.phys.InspectInventoryTO;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.unittest.table.evt.EvtEventUtil;
import com.mxi.mx.core.unittest.table.evt.EvtInv;
import com.mxi.mx.core.unittest.table.inv.InvInv;
import com.mxi.mx.core.unittest.table.po.PoHeader;
import com.mxi.mx.core.unittest.table.po.PoLine;


/**
 * InspectAsUnserviceableTest and INInspectAsUnserviceableTest are two test classes which test the
 * method {@link DefaultConditionService().inspectInventory Those test which requires lots of data
 * preparation before the tests, exists in INInspectAsUnserviceableTest and using the existing data.
 *
 * @author kTandon
 */
@RunWith( Theories.class )
public final class InspectAsUnserviceableTest extends AbstractConditionServiceTestCase {

   private OwnerKey iLocalOwner;
   private LocationKey iUSSTGLocation;
   private StockNoKey iStockNo;
   private TaskKey iWorkPackage;
   private FaultKey iFault;
   private static final BigDecimal ORDERQTY = BigDecimal.ONE;
   private static final BigDecimal AUP = BigDecimal.TEN;

   /** Data: Part Request Priorities */
   /**
    * Some tests method only differ by input values. Testing those methods will result in duplicate
    * codes. A theory is a special test method that a special JUnit runner (Theories) executes. To
    * use the runner, annotate your test class with the @RunWith(Theories.class) annotation. The
    * Theories runner executes a theory against several data inputs called data points. A theory is
    * annotated with @Theory, but unlike normal @Test methods, a @Theory method has parameters. In
    * order to fill these parameters with values, the Theories runner uses values of the data points
    * having the same type
    */
   @DataPoint
   public static RefInvClassKey iSerInvClass = RefInvClassKey.SER;
   @DataPoint
   public static RefInvClassKey iBatchInvClass = RefInvClassKey.BATCH;
   @DataPoint
   public static RefPoTypeKey iPurchaseOrder = RefPoTypeKey.PURCHASE;
   @DataPoint
   public static RefPoTypeKey iExhangeOrder = RefPoTypeKey.EXCHANGE;


   /**
    * Tests to ensure that stock part request is completed when inspect as unserviceable for the
    * order
    *
    * @throws Exception
    *            if an unexpected error occurs
    */
   @Theory
   public void testStockPartReqCompleted( RefInvClassKey aClass, RefPoTypeKey aType )
         throws Exception {

      // data setup
      PartRequestKey lPartRequestInventory = createPartRequestWithInventory( RefReqTypeKey.STOCK,
            RefEventStatusKey.PRINSPREQ, aType, aClass );

      // Don't call the inspect with stub here because the stock completing logic is inside the move
      // inventory or change inventory condition service.
      inspectInventory();

      // ensure the part request is marked as complete
      new EvtEventUtil( lPartRequestInventory ).assertEventStatus( RefEventStatusKey.PRCOMPLETE );

   }


   /**
    * Tests to ensure that adhoc part request is not completed(remains open) when inspect as
    * unserviceable for order
    *
    * @throws Exception
    *            if an unexpected error occurs
    */
   @Theory
   public void testAdhocPartReqNotCompleted( RefInvClassKey aClass, RefPoTypeKey aType )
         throws Exception {

      // data setup
      PartRequestKey lPartRequestInventory = createPartRequestWithInventory( RefReqTypeKey.ADHOC,
            RefEventStatusKey.PROPEN, aType, aClass );

      inspectInventoryWithStub();

      // ensure the part request is status is OPEN
      new EvtEventUtil( lPartRequestInventory.getEventKey() )
            .assertEventStatus( RefEventStatusKey.PROPEN );

   }


   /**
    * Inspect inventory with the mock up logic.
    *
    * @throws MxException
    *            if a mx error occurs
    * @throws TriggerException
    *            if a trigger error occurs
    */
   private void inspectInventoryWithStub() throws MxException, TriggerException {
      new DefaultConditionServiceStub().inspectInventory( iInventory, iInspectInventoryTO, iHr );
   }


   /**
    * Tests to ensure that method can be successfully called when a historic AOG fault exists.
    *
    * @throws Exception
    *            if an unexpected error occurs
    */
   @Theory
   public void testNoExceptionforHistoricSevereFaults( RefInvClassKey aClass ) throws Exception {

      getFaultData( RefFailureSeverityKey.AOG, aClass );

      inspectInventory();

      // assert the method successfully executed, and no exception was thrown for
      // an inventory with a severe historic fault
      InvInv lInvInv = new InvInv( iInventory );
      lInvInv.assertCondCd( RefInvCondKey.REPREQ.getCd() );
   }


   /**
    * Tests to ensure that method can be properly called with serialized inventory
    *
    * @throws Exception
    *            if an unexpected error occurs
    */
   @Test
   public void testInspectAsUnserviceableSER() throws Exception {

      PurchaseOrderLineKey lOrderLine = createOrderLine( RefPoTypeKey.PURCHASE );
      createInventory( RefInvClassKey.SER, RefInvCondKey.INSPREQ, iPart, lOrderLine,
            InvInvTable.FinanceStatusCd.NEW );

      // Set values to the object
      iInspectInventoryTO.setAlertTechnicalRecords( true );
      iInspectInventoryTO.setNote( "NOTE" );
      iInspectInventoryTO.setReason( "ACTLFLT" );

      // inspect inventory as unserviceable
      inspectInventory();

      // assert that the location is updated to un-serviceable staging
      InvInv lInvInv = new InvInv( iInventory );
      assertEquals( iUSSTGLocation, lInvInv.getLocation() );
      assertEquals( RefInvCondKey.REPREQ, lInvInv.getInvCond() );

      // get the latest event that was created for the inventory
      EventKey lLatestEvent = EvtInv.getLatestEventForInv( iInventory );
      EvtEventUtil lEvtEvent = new EvtEventUtil( lLatestEvent );

      List<EventKey> lEventKey = EvtInv.getLatestEventsForInv( iInventory, 2 );
      lEvtEvent = new EvtEventUtil( lEventKey.get( 0 ) );
      lEvtEvent.assertEventType( "LX" );
      lEvtEvent.assertEventStatus( RefEventStatusKey.LXCMPLT );

      lEvtEvent = new EvtEventUtil( lEventKey.get( 1 ) );
      lEvtEvent.assertEventType( "AC" );
      lEvtEvent.assertEventStatus( RefEventStatusKey.ACREPREQ );
      lEvtEvent.assertStageReason( new RefStageReasonKey( 0, "ACTLFLT" ) );
      lEvtEvent.assertEventLdesc( "NOTE" );
      lEvtEvent.assertEditorHr( iHr );

   }


   /**
    * Tests to ensure that order received quantity is properly updated
    *
    * @throws Exception
    *            if an unexpected error occurs
    */
   @Theory
   public void testInventoryReceivedOnOrder( RefInvClassKey aClass, RefPoTypeKey aType )
         throws Exception {

      PartNoKey lPartNo = createPartNo( aClass );
      PurchaseOrderLineKey lOrderLine = createOrder( lPartNo, aType );
      iInventory = new InventoryBuilder().withOwner( iLocalOwner ).withPartNo( lPartNo )
            .withFinanceStatusCd( InvInvTable.FinanceStatusCd.NEW ).withClass( aClass )
            .atLocation( iUSSTGLocation ).withCondition( RefInvCondKey.INSPREQ )
            .withOrderLine( lOrderLine ).withBinQt( 1.0 ).isIssued().build();

      iInspectInventoryTO.setQuantity( 1.0 );

      inspectInventory();

      // assert that the location was properly updated
      InvInv lInvInv = new InvInv( iInventory );
      lInvInv.assertCondCd( RefInvCondKey.REPREQ.getCd() );

      // assert the po received quantity was properly updated
      BigDecimal lReceivedQty = PoLine.findByPrimaryKey( lOrderLine ).getReceivedQt();
      assertThat( lReceivedQty, is( BigDecimal.ONE ) );

      // ensure the order is received
      assertThat( EvtEventTable.findByPrimaryKey( lOrderLine.getPurchaseOrderKey().getEventKey() )
            .getEventStatus(), is( RefEventStatusKey.PORECEIVED ) );

      // assert po header last modified date is today
      PoHeader lPoHeader = new PoHeader( lOrderLine.getPurchaseOrderKey() );
      lPoHeader.assertLastModifiedDate( new Date() );

   }


   /**
    * Test set logic for Release Number , Release Date , Release Remarks
    *
    * @throws Exception
    *            if an unexpected error occurs
    */
   @Test
   public void testSetReleaseDetails() throws Exception {

      final String RELEASE_NUMBER = "Release Number 123";
      final String RELEASE_REMARKS = "Release Remarks 123";
      final Date RELEASE_DATE = new Date();

      PartNoKey lPartNo = new PartNoBuilder().withInventoryClass( RefInvClassKey.SER )
            .withStatus( RefPartStatusKey.ACTV ).withAverageUnitPrice( BigDecimal.ZERO )
            .withFinancialType( RefFinanceTypeKey.EXPENSE ).withTotalQuantity( ORDERQTY )
            .withTotalValue( ORDERQTY.multiply( AUP ) ).build();

      // create an inventory
      iInventory = new InventoryBuilder().withOwner( iLocalOwner ).withPartNo( lPartNo )
            .withFinanceStatusCd( InvInvTable.FinanceStatusCd.NEW ).withClass( RefInvClassKey.SER )
            .atLocation( iUSSTGLocation ).withCondition( RefInvCondKey.INSPREQ ).build();

      // Create InspectInventoryTO transfer object and
      // Set values to the object
      iInspectInventoryTO.setAlertTechnicalRecords( true );
      iInspectInventoryTO.setReleaseNumber( RELEASE_NUMBER, "Release Number" );
      iInspectInventoryTO.setReleaseDate( RELEASE_DATE );
      iInspectInventoryTO.setReleaseRemarks( RELEASE_REMARKS, "Release Remarks" );

      // Call inspect as unserviceable
      inspectInventoryWithStub();

      // check audit message for release note
      assertAuditMessages( iInventory, iInspectInventoryTO );

      // assert that the location was properly updated
      InvInv lInvInv = new InvInv( iInventory );
      lInvInv.assertReleaseNumber( RELEASE_NUMBER );
      lInvInv.assertReleaseDate( RELEASE_DATE );
      lInvInv.assertReleaseRemarks( RELEASE_REMARKS );

   }


   /**
    * Tests to ensure that method can be properly called with Batch inventory
    *
    * @throws Exception
    *            if an unexpected error occurs
    */
   @Test
   public void testInspectAsUnserviceableBIN() throws Exception {

      PartNoKey lPartNo = new PartNoBuilder().withInventoryClass( RefInvClassKey.BATCH )
            .withAverageUnitPrice( AUP ).withTotalQuantity( ORDERQTY )
            .withFinancialType( RefFinanceTypeKey.EXPENSE )
            .withTotalValue( ORDERQTY.multiply( AUP ) ).withStatus( RefPartStatusKey.ACTV ).build();
      double binQt = new Double( 10.0 );
      iInventory = new InventoryBuilder().withOwner( iLocalOwner ).withPartNo( lPartNo )
            .withFinanceStatusCd( InvInvTable.FinanceStatusCd.NEW ).withBinQt( binQt )
            .withClass( RefInvClassKey.BATCH ).atLocation( iDockLocation )
            .withCondition( RefInvCondKey.INSPREQ ).build();

      // Set values to the object
      iInspectInventoryTO.setAlertTechnicalRecords( true );
      iInspectInventoryTO.setNote( "NOTE" );
      iInspectInventoryTO.setMode( "MODE_INSPECT_AS_UNSERVICEABLE" );

      inspectInventory();

      InvInv lInvInv = new InvInv( iInventory );
      assertThat( iUSSTGLocation, is( lInvInv.getLocation() ) );
      lInvInv.assertBinQuantity( binQt );
      lInvInv.assertCondCd( RefInvCondKey.REPREQ.getCd() );

      List<EventKey> lEventKey = EvtInv.getLatestEventsForInv( iInventory, 2 );
      EvtEventUtil lEvtEvent = new EvtEventUtil( lEventKey.get( 0 ) );
      lEvtEvent.assertEventType( "LX" );
      lEvtEvent.assertEventStatus( RefEventStatusKey.LXCMPLT );

      lEvtEvent = new EvtEventUtil( lEventKey.get( 1 ) );
      lEvtEvent.assertEventType( "AC" );
      lEvtEvent.assertEventStatus( RefEventStatusKey.ACREPREQ );

   }


   /**
    * make sure that an IC type event is created with the proper audit message
    *
    * @param aInventoryKey
    *           the inventory key
    * @param aInspectInventoryTO
    *           Inspect Inventory TO
    * @throws Exception
    *            if an error occurs
    */
   private void assertAuditMessages( InventoryKey aInventoryKey,
         InspectInventoryTO aInspectInventoryTO ) throws Exception {

      // Obtain all evt_inv rows for this inventory and try to find the new note
      EventKey[] lEvents = EvtInv.getEventsForInventory( aInventoryKey );
      boolean lEventReleaseNumberIsPresent = false;
      boolean lEventReleaseDateIsPresent = false;
      boolean lEventReleaseRemarkIsPresent = false;

      // check audit message for release number
      for ( int i = 0; i < lEvents.length; i++ ) {
         EvtEventUtil lEvent = new EvtEventUtil( lEvents[i] );
         if ( lEvent.getTable().getEventLdesc()
               .contains( i18n.get( "core.msg.EDIT_INVENTORY_RELEASE_NUMBER", null,
                     aInspectInventoryTO.getReleaseNumber() ) ) ) {
            lEvent.assertEventType( RefEventTypeKey.IC.getCd() );
            lEventReleaseNumberIsPresent = true;
         }
         if ( lEvent.getTable().getEventLdesc().contains( i18n.get(
               "core.msg.EDIT_INVENTORY_RELEASE_DATE", null,
               DateUtils.toDefaultDateTimeString( aInspectInventoryTO.getReleaseDate() ) ) ) ) {
            lEvent.assertEventType( RefEventTypeKey.IC.getCd() );
            lEventReleaseDateIsPresent = true;
         }
         if ( lEvent.getTable().getEventLdesc()
               .contains( i18n.get( "core.msg.EDIT_INVENTORY_RELEASE_REMARK", null,
                     aInspectInventoryTO.getReleaseRemarks() ) ) ) {
            lEvent.assertEventType( RefEventTypeKey.IC.getCd() );
            lEventReleaseRemarkIsPresent = true;
         }
      }
      assertTrue( "Event Release Number Is Not Present", lEventReleaseNumberIsPresent );
      assertTrue( "Event Release Date Is Not Present", lEventReleaseDateIsPresent );
      assertTrue( "Event Release Remark Is Not Present", lEventReleaseRemarkIsPresent );
   }


   /**
    * Create InventoryKey.
    *
    * @param aReqtype
    *           the request type
    * @param aRefEventStatus
    *           the event status
    * @param aPoType
    *           the order type
    * @return the created Inventory
    */
   private PartRequestKey createPartRequestWithInventory( RefReqTypeKey aReqType,
         RefEventStatusKey aRefEventStatus, RefPoTypeKey aPoType, RefInvClassKey aClass ) {

      createInventoryWithPartAndOrder( aPoType, aClass );
      PartRequestKey lPartRequest = createPartRequest( aReqType, aRefEventStatus, iInventory );

      return lPartRequest;

   }


   /**
    * Create part request.
    *
    * @param aReqtype
    *           the request type
    * @param aEventStatus
    *           the event status
    * @param aInventory
    *           the inventory
    * @return the created part request
    */
   private PartRequestKey createPartRequest( RefReqTypeKey aReqtype, RefEventStatusKey aEventStatus,
         InventoryKey aInventory ) {
      // create a part request
      PartRequestKey lPartRequest = new PartRequestBuilder().withType( aReqtype )
            .withStatus( aEventStatus ).withReservedInventory( aInventory )
            .withReqStockNo( iStockNo ).isNeededAt( iDockLocation ).build();

      return lPartRequest;

   }


   /**
    * Create part.
    *
    * @param aInvClass
    *           the inventory class code
    * @return the created part no
    */
   private PartNoKey createPartNo( RefInvClassKey aInvClass ) {

      // create a part request
      return new PartNoBuilder().withInventoryClass( aInvClass ).withStatus( RefPartStatusKey.ACTV )
            .withFinancialType( RefFinanceTypeKey.EXPENSE ).withTotalQuantity( ORDERQTY )
            .withTotalValue( ORDERQTY.multiply( AUP ) ).withStock( iStockNo ).build();
   }


   /**
    * Create an order.
    *
    * @param aPartNo
    *           the part number
    * @param aOrderType
    *           the order type
    * @return the created order
    */

   private PurchaseOrderLineKey createOrder( PartNoKey aPartNo, RefPoTypeKey aOrderType ) {

      // create an order
      PurchaseOrderKey lOrder = new OrderBuilder().withOrderType( aOrderType )
            .withStatus( RefEventStatusKey.POISSUED ).build();

      // create an order line
      PurchaseOrderLineKey lOrderLine = new OrderLineBuilder( lOrder ).forPart( aPartNo )
            .withUnitType( RefQtyUnitKey.EA ).withUnitPrice( AUP ).withOrderQuantity( ORDERQTY )
            .withPreInspQty( BigDecimal.ONE ).build();

      return lOrderLine;
   }


   /**
    * Create order line.
    *
    * @param aOrderType
    *           the order type
    * @return the created order line
    */
   private PurchaseOrderLineKey createOrderLine( RefPoTypeKey aOrderType ) {

      // create order
      PurchaseOrderKey lOrder = new OrderBuilder().withOrderType( aOrderType ).build();
      // create order line
      return new OrderLineBuilder( lOrder ).build();

   }


   /**
    * Create an inventory.
    *
    * @param aInvClass
    *           the inventory class code
    * @param aInvCondCd
    *           the inventory condition
    * @param aPartNo
    *           the part number
    * @param aOrderLine
    *           the order line
    * @param aFinanceStatusCd
    *           the finance status code
    * @return the created inventory
    */
   private void createInventory( RefInvClassKey aClass, RefInvCondKey aInvCondCd, PartNoKey aPartNo,
         PurchaseOrderLineKey aOrderLine, InvInvTable.FinanceStatusCd aFinanceStatusCd ) {

      // create an inventory
      iInventory = new InventoryBuilder().withOwner( iLocalOwner ).withPartNo( aPartNo )
            .withFinanceStatusCd( aFinanceStatusCd ).withClass( aClass ).atLocation( iDockLocation )
            .withCondition( aInvCondCd ).withOrderLine( aOrderLine ).isIssued().build();

   }


   /**
    * Set the inventory properties and call inspect inventory logic.
    *
    * @throws Exception
    *            if an error occurs
    */
   private void inspectInventory() throws Exception {

      new DefaultConditionService().inspectInventory( iInventory, iInspectInventoryTO, iHr );
   }


   /**
    * Set fault data
    *
    * @param aFailureSeverity
    *           the Failure Severity
    * @throws Exception
    *            if an error occurs
    */
   private void getFaultData( RefFailureSeverityKey aFailureSeverity, RefInvClassKey aClass ) {

      createInventoryWithPartAndOrder( RefPoTypeKey.PURCHASE, aClass );
      iWorkPackage = new TaskBuilder().onInventory( iInventory ).build();
      iFault = new SdFaultBuilder().withStatus( RefEventStatusKey.CFACTV )
            .withFailureSeverity( aFailureSeverity ).build();
      TaskKey lTask = new TaskBuilder().onInventory( iInventory ).withParentTask( iWorkPackage )
            .withFault( iFault ).build();
      new EventRelationshipBuilder().fromEvent( iFault ).toEvent( lTask )
            .withType( RefRelationTypeKey.CORRECT ).build();

   }


   /**
    * Set inventory with with part and order
    *
    * @param aPoType
    *           the order type
    * @throws Exception
    *            if an error occurs
    */
   private void createInventoryWithPartAndOrder( RefPoTypeKey aPoType, RefInvClassKey aClass ) {

      PartNoKey lPartNo = createPartNo( aClass );
      PurchaseOrderLineKey lOrderLine = createOrder( lPartNo, aPoType );
      createInventory( aClass, RefInvCondKey.INSPREQ, lPartNo, lOrderLine,
            InvInvTable.FinanceStatusCd.NEW );

   }


   @Override
   @Before
   public void loadData() throws Exception {

      super.loadData();

      // change the mode to inspect as UnServiceable
      iInspectInventoryTO.setMode( "MODE_INSPECT_AS_UNSERVICEABLE" );

      // create local owner
      iLocalOwner = new OwnerDomainBuilder().build();

      // create unserviceable staging location
      iUSSTGLocation = new LocationDomainBuilder().withType( RefLocTypeKey.USSTG )
            .withSupplyLocation( iSupplyLoc ).build();

      // create stock
      iStockNo = new StockBuilder().build();
   }

}
