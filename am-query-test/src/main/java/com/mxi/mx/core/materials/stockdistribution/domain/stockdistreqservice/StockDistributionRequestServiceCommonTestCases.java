package com.mxi.mx.core.materials.stockdistribution.domain.stockdistreqservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.StockBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.am.ee.OperateAsUserRule;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.i18n.i18n;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.ManufacturerKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefStockDistReqStatusKey;
import com.mxi.mx.core.key.StockDistReqKey;
import com.mxi.mx.core.key.StockDistReqLogKey;
import com.mxi.mx.core.key.StockNoKey;
import com.mxi.mx.core.key.TransferKey;
import com.mxi.mx.core.key.UserKey;
import com.mxi.mx.core.materials.stockdistribution.domain.PickStockDistReqTO;
import com.mxi.mx.core.materials.stockdistribution.domain.StockDistributionRequestService;
import com.mxi.mx.core.materials.stockdistribution.domain.StockDistributionRequestTO;
import com.mxi.mx.core.materials.stockdistribution.infra.StockDistReqDao;
import com.mxi.mx.core.materials.stockdistribution.infra.StockDistReqLogDao;
import com.mxi.mx.core.materials.stockdistribution.infra.StockDistReqLogTableRow;
import com.mxi.mx.core.materials.stockdistribution.infra.StockDistReqPickedItemDao;
import com.mxi.mx.core.materials.stockdistribution.infra.StockDistReqPickedItemTableRow;
import com.mxi.mx.core.materials.stockdistribution.infra.StockDistReqTableRow;
import com.mxi.mx.core.services.inventory.pick.PickedItem;
import com.mxi.mx.core.services.part.binlevel.BinLevelService;
import com.mxi.mx.core.services.part.binlevel.BinLevelTO;
import com.mxi.mx.core.services.transfer.CreateTransferTO;
import com.mxi.mx.core.services.transfer.InvalidTransferLocationException;
import com.mxi.mx.core.services.transfer.TransferDetailsTO;
import com.mxi.mx.core.services.transfer.TransferService;
import com.mxi.mx.core.table.inv.InvInvTable;


/**
 * This class tests the logic in StockDistributionRequestService class.
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public abstract class StockDistributionRequestServiceCommonTestCases {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public OperateAsUserRule operateAsUser = new OperateAsUserRule( 99999, "currentuser" );


   @Before
   public void setup() {
      Domain.createHumanResource( hr -> {
         hr.setUser( new UserKey( 99999 ) );
      } );
   }


   private static final RefQtyUnitKey STOCK_NO_QTY_UNIT = RefQtyUnitKey.EA;

   private Double iInvQty;
   private Float iNeededQty;

   private StockDistributionRequestService iService;
   private StockDistReqDao iStockDistReqDao;
   private StockDistReqTableRow iStockDistReqTableRow;

   private LocationKey iMainWarehouse;
   private LocationKey iLineStore;
   private OwnerKey iOwner;

   private HumanResourceKey iHr;
   private StockNoKey iStockNo;
   private PartNoKey iPartInStock;
   private InventoryKey iInventoryToPick;

   private StockDistReqKey iStockDistReqKey;
   private LocationKey iLineStoreBinWithoutOwner;
   private LocationKey iLineStoreBinWithOwner;


   public void loadData( RefInvClassKey aInvClass ) {

      RefInvClassKey lInvClass = aInvClass;
      iInvQty = RefInvClassKey.BATCH.equals( lInvClass ) ? 5.0 : 1.0;
      iNeededQty = iInvQty.floatValue();

      // Initialize the service and dao
      iService = new StockDistributionRequestService();
      iStockDistReqDao = InjectorContainer.get().getInstance( StockDistReqDao.class );

      // Create default owner and hr
      iOwner = Domain.createOwner();
      iHr = Domain.createHumanResource();

      // Create line store and its supplier
      iMainWarehouse = Domain.createLocation( location -> {
         location.setType( RefLocTypeKey.SRVSTORE );
         location.setCode( "mainwarehouse" );
      } );
      iLineStore = Domain.createLocation( location -> {
         location.setCode( "linestore" );
         location.setHubLocation( iMainWarehouse );
         location.setSupplyLocation( Domain.createLocation() );
      } );

      // Create stock no and the assigned part.
      iStockNo = new StockBuilder().withInvClass( lInvClass ).withQtyUnitKey( STOCK_NO_QTY_UNIT )
            .build();
      iPartInStock = createPartInStock( lInvClass );

      // Create stock distribution request on the stock and needed location. NOTE: stock level is
      // not required in this test class.
      iStockDistReqKey = Domain.createStockDistReq( distReq -> {
         distReq.setStockNo( iStockNo );
         distReq.setNeededQuantity( iNeededQty );
         distReq.setNeededLocation( iLineStore );
      } );
      iStockDistReqTableRow = iStockDistReqDao.findByPrimaryKey( iStockDistReqKey );

      // Create inventory at the main warehouse to pick
      iInventoryToPick = new InventoryBuilder().atLocation( iMainWarehouse )
            .withPartNo( iPartInStock ).withBinQt( new Double( iInvQty ) ).withClass( lInvClass )
            .withCondition( RefInvCondKey.RFI ).withSerialNo( "SERIAL_NO" ).withOwner( iOwner )
            .build();
   }


   @Test
   public void test_GIVEN_WarehouseWithoutBin_WHEN_Pick_THEN_CreateTransferToWarehouse()
         throws MxException, TriggerException {

      pickItem();

      assertInventoryPicked( iLineStore, RefStockDistReqStatusKey.PICKED );
   }


   @Test
   public void test_GIVEN_InvOwnerMatchBinLevelOwner_WHEN_Pick_THEN_CreateTransferToThatBin()
         throws MxException, TriggerException {

      // Use the inventory owner for bin level
      iLineStoreBinWithOwner = createBinLevelUnderLineStore( iOwner );
      createBinLevelUnderLineStore( null );

      pickItem();

      assertInventoryPicked( iLineStoreBinWithOwner, RefStockDistReqStatusKey.PICKED );
   }


   @Test
   public void
         test_GIVEN_InvOwnerNotMatchBinLevelOwner_WHEN_Pick_THEN_CreateTransferToAnotherBinWithoutOwner()
               throws MxException, TriggerException {

      // Use different owner for bin level
      createBinLevelUnderLineStore( Domain.createOwner() );
      iLineStoreBinWithoutOwner = createBinLevelUnderLineStore( null );

      pickItem();

      assertInventoryPicked( iLineStoreBinWithoutOwner, RefStockDistReqStatusKey.PICKED );
   }


   @Test
   public void
         test_GIVEN_InvOwnerNotMatchAnyBinLevelOwner_WHEN_Pick_THEN_CreateTransferToLineStore()
               throws MxException, TriggerException {

      createBinLevelUnderLineStore( Domain.createOwner() );
      createBinLevelUnderLineStore( Domain.createOwner() );

      pickItem();

      assertInventoryPicked( iLineStore, RefStockDistReqStatusKey.PICKED );
   }


   @Test
   public void
         test_GIVEN_InvQtyLessThanNeededQty_WHEN_Pick_THEN_StockDistReqStatusChangedToINPROGRESS()
               throws MxException, TriggerException {

      // change the needed quantity higher than the inventory quantity
      iStockDistReqTableRow.setNeededQty( iInvQty.floatValue() + 2 );
      iStockDistReqDao.update( iStockDistReqTableRow );

      pickItem();

      assertInventoryPicked( iLineStore, RefStockDistReqStatusKey.INPROGRESS );

   }


   @Test( expected = InvalidTransferLocationException.class )
   public void
         test_GIVEN_InvUnderAnotherSupplyLoc_WHEN_Pick_THEN_ThrowInvalidTransferLocationException()
               throws MxException, TriggerException {

      // create another main warehouse for the inventory's new location
      LocationKey lWarehouse = Domain.createLocation( location -> {
         location.setSupplyLocation( new LocationKey( 1, 1 ) );
      } );

      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( iInventoryToPick );
      lInvInv.setLocation( lWarehouse );
      lInvInv.update();

      pickItem();
   }


   @Test
   public void test_GIVEN_DistReqTransferObject_WHEN_Create_THEN_AllDataSavedToDatabase()
         throws Exception {

      StockDistributionRequestTO lStockDistTO = new StockDistributionRequestTO();
      lStockDistTO.setStockNo( iStockNo );
      lStockDistTO.setNeededQty( iNeededQty );
      lStockDistTO.setNeededLocation( iLineStore );
      lStockDistTO.setIgnoreOwnerBool( false );
      lStockDistTO.setOwner( iOwner );

      // Create the Stock Distribution Request.
      iStockDistReqKey = iService.create( lStockDistTO );

      // assert data in the table for created stock distribution request
      StockDistReqTableRow lTableRow = new StockDistReqTableRow( iStockDistReqKey );

      // Make sure the request id is generated
      assertNotNull( lTableRow.getRequestId() );

      assertEquals( iStockNo, lTableRow.getStockNo() );
      assertEquals( iNeededQty, lTableRow.getNeededQty() );
      assertEquals( iLineStore, lTableRow.getNeededLocation() );
      assertEquals( RefStockDistReqStatusKey.OPEN, lTableRow.getStatus() );
      assertEquals( iOwner, lTableRow.getOwner() );

      // The supplier location is line store's supplier (hub location)
      assertEquals( iMainWarehouse, lTableRow.getSupplierLocation() );

      // The quantity unit is stock no's quantity unit
      assertEquals( STOCK_NO_QTY_UNIT, lTableRow.getQtyUnit() );

      assertHistoryNote( "core.msg.DISTREQ_CREATED",
            "This is an auto-generated stock distribution request." );
   }


   @Test
   public void
         test_GIVEN_OpenStockDistReq_WHEN_Complete_THEN_StatusChangedToCompletedWithHistoryNotes()
               throws Exception {

      iStockDistReqTableRow.setStatus( RefStockDistReqStatusKey.OPEN );
      iStockDistReqDao.update( iStockDistReqTableRow );

      iService.complete( iStockDistReqKey, iHr );

      assertStockDistReqCompleted();
   }


   @Test
   public void
         test_GIVEN_InprogressStockDistReq_WHEN_Complete_THEN_BothStockDistReqAndTransferCompleted()
               throws Exception {

      TransferKey lTransfer = createPickedItemForStockDistReq();

      iService.complete( iStockDistReqKey, iHr );

      assertStockDistReqCompleted();
      assertTransferStatus( lTransfer, "LXCMPLT" );
   }


   @Test
   public void
         test_GIVEN_OpenStockDistReq_WHEN_Cancel_THEN_StatusChangedToCanceledWithHistoryNotes()
               throws Exception {

      iStockDistReqTableRow.setStatus( RefStockDistReqStatusKey.OPEN );
      iStockDistReqDao.update( iStockDistReqTableRow );

      iService.cancel( iStockDistReqKey, iHr );

      assertStockDistReqCanceled();
   }


   @Test
   public void
         test_GIVEN_InprogressStockDistReq_WHEN_Cancel_THEN_BothStockDistReqAndTransferCanceled()
               throws Exception {

      TransferKey lTransfer = createPickedItemForStockDistReq();

      iService.cancel( iStockDistReqKey, iHr );

      assertStockDistReqCanceled();
      assertTransferCanceled( lTransfer );
   }


   private TransferKey createPickedItemForStockDistReq() throws MxException, TriggerException {

      iStockDistReqTableRow.setStatus( RefStockDistReqStatusKey.INPROGRESS );
      iStockDistReqDao.update( iStockDistReqTableRow );

      // Create a pending transfer
      CreateTransferTO lTO = new CreateTransferTO();
      lTO.setTransferToCd( "linestore" );
      lTO.setInventory( iInventoryToPick );
      lTO.setQuantity( new Double( iInvQty ) );
      TransferKey lTransfer = TransferService.create( lTO );

      // link the transfers and the stock distribution request for picking
      Domain.createStockDistReqPickedItem( pickItem -> {
         pickItem.setStockDistReq( iStockDistReqKey );
         pickItem.setTransferKey( lTransfer );
      } );

      return lTransfer;
   }


   private LocationKey createBinLevelUnderLineStore( OwnerKey aOwner ) throws MxException {

      LocationKey lBinLoc = Domain.createLocation( location -> {
         location.setType( RefLocTypeKey.BIN );
         location.setParent( iLineStore );
      } );

      new BinLevelService().add( iPartInStock, aOwner, lBinLoc, new BinLevelTO() );

      return lBinLoc;
   }


   private PartNoKey createPartInStock( RefInvClassKey aInvClassKey ) {

      return new PartNoBuilder().withOemPartNo( "OemPartNo" ).withInventoryClass( aInvClassKey )
            .withStock( iStockNo ).manufacturedBy( new ManufacturerKey( 0, "MXI" ) ).build();
   }


   private void pickItem() throws MxException, TriggerException {

      PickStockDistReqTO lPickTO = new PickStockDistReqTO( iStockDistReqKey );

      PickedItem lPickedItem = new PickedItem( iInventoryToPick, new Double( iInvQty ), null );
      lPickTO.addPickedItem( lPickedItem );

      iService.pickForStockDistReq( lPickTO, iHr );
   }


   private void assertInventoryPicked( LocationKey aTransferLoc,
         RefStockDistReqStatusKey aExpectedStatus ) {

      iStockDistReqDao.refresh( iStockDistReqTableRow );

      // Assert that we get the expected state
      assertEquals( aExpectedStatus, iStockDistReqTableRow.getStatus() );

      StockDistReqPickedItemDao lStockDistReqPickedItemDao =
            InjectorContainer.get().getInstance( StockDistReqPickedItemDao.class );

      // Assert transfer is created
      TransferKey lTransfer = lStockDistReqPickedItemDao.findByDistReqKey( iStockDistReqKey ).now()
            .toArray( new StockDistReqPickedItemTableRow[0] )[0].getTransfer();
      assertNotNull( lTransfer );

      TransferDetailsTO lTransferTO = TransferService.get( lTransfer );
      assertEquals( lTransferTO.getStatus(), "LXPEND" );
      assertEquals( lTransferTO.getQuantity(), Double.valueOf( iInvQty ) );
      assertEquals( lTransferTO.getToLocationKey(), aTransferLoc );
   }


   private void assertStockDistReqCompleted() {

      assertStockDistReqStatus( RefStockDistReqStatusKey.COMPLETED );

      assertHistoryNote( "core.msg.DISTREQ_COMPLETED",
            "The stock distribution request and related transfers are completed." );
   }


   private void assertStockDistReqCanceled() {

      assertStockDistReqStatus( RefStockDistReqStatusKey.CANCELED );

      assertHistoryNote( "core.msg.DISTREQ_CANCELLED",
            "The stock distribution request and related transfers are canceled." );
   }


   private void assertTransferCanceled( TransferKey aTransfer ) {
      assertTransferStatus( aTransfer, "LXCANCEL" );
   }


   private void assertStockDistReqStatus( RefStockDistReqStatusKey aStatus ) {

      iStockDistReqDao.refresh( iStockDistReqTableRow );
      assertEquals( aStatus, iStockDistReqTableRow.getStatus() );
   }


   private void assertTransferStatus( TransferKey aTransfer, String aStatus ) {

      TransferDetailsTO lTransferTO = TransferService.get( aTransfer );
      assertEquals( lTransferTO.getStatus(), aStatus );
   }


   private void assertHistoryNote( String aInternationalizedMsgKey, String aEnglishMessage ) {

      String lInternationalizedMsg = i18n.get( aInternationalizedMsgKey );

      // Make sure the history note is correct
      String[] lKeyColumns = new StockDistReqLogKey( 0, 0, 0 ).getKeyColumnName();

      QuerySet lQs = QuerySetFactory.getInstance().executeQuery( StockDistReqLogDao.TABLE_NAME,
            iStockDistReqKey.getPKWhereArg(), lKeyColumns );

      if ( lQs.next() ) {

         String lSystemNote =
               new StockDistReqLogTableRow( lQs.getKey( StockDistReqLogKey.class, lKeyColumns ) )
                     .getNotes();

         // Make sure the message itself is correct in case the property file is changed by accident
         assertEquals( lInternationalizedMsg, aEnglishMessage );

         // Make sure the internationalization composition of message is correct in case the code is
         // changed by accident
         assertEquals( lInternationalizedMsg, lSystemNote );
      } else {
         Assert.fail( "No history note is created for needed quantity adjustment." );
      }
   }

}
