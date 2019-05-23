package com.mxi.mx.core.materials.stockdistribution.domain;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.InventoryTransferBuilder;
import com.mxi.am.domain.builder.OwnerDomainBuilder;
import com.mxi.am.domain.builder.StockBuilder;
import com.mxi.am.domain.builder.StockLevelBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.i18n.i18n;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.core.key.InvLocStockKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefStockDistReqStatusKey;
import com.mxi.mx.core.key.RefStockLowActionKey;
import com.mxi.mx.core.key.StockDistReqKey;
import com.mxi.mx.core.key.StockNoKey;
import com.mxi.mx.core.key.TransferKey;
import com.mxi.mx.core.materials.stockdistribution.infra.StockDistReqDao;
import com.mxi.mx.core.materials.stockdistribution.infra.StockDistReqLogDao;
import com.mxi.mx.core.services.transfer.CreateTransferTO;
import com.mxi.mx.core.services.transfer.TransferService;
import com.mxi.mx.core.table.evt.EvtEventDao;
import com.mxi.mx.core.table.inv.InvLocTable;


/**
 * This class tests the logic of stock distribution request status change when completing and
 * canceling its transfer.
 *
 * @author Libin Cai
 * @created November 8, 2018
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class StockDistRequestStatusChangeTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   private static final double REORDER_QTY = 60.0;
   private static final RefQtyUnitKey QTY_UNIT = RefQtyUnitKey.EA;
   private static final double NEEDED_QTY = 5.0;
   private static final double PEND_XFER_QTY = 1.0;

   private LocationKey iLineWarehouse;
   private StockNoKey iBatchStockNo;
   private StockDistReqKey iStockDistReq;
   private InvLocStockKey iStockLevel;
   private TransferKey iTransfer;


   @Before
   public void loadData() throws Exception {

      iLineWarehouse = Domain.createLocation( location -> {
         location.setType( RefLocTypeKey.SRVSTORE );
         location.setCode( "loc" );
      } );
      iBatchStockNo = new StockBuilder().withInvClass( RefInvClassKey.BATCH )
            .withQtyUnitKey( QTY_UNIT ).build();

      iStockLevel = new StockLevelBuilder( iLineWarehouse, iBatchStockNo,
            new OwnerDomainBuilder().isDefault().build() ).withReorderQt( REORDER_QTY )
                  .withMaxLevel( 0.0 ).withStockLowAction( RefStockLowActionKey.DISTREQ )
                  .withIgnoreOwnerBool( true ).build();
   }


   /**
    * Change from INPROGRESS to OPEN
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void
         test_GIVEN_InprogressStockDistRequest_WHEN_CancelTheLastTransfer_THEN_TheRequestStatusChangeToOPEN()
               throws Exception {

      createStockDistReqWithPendingXfer( RefStockDistReqStatusKey.INPROGRESS, PEND_XFER_QTY );

      cancelTransfer();

      assertResult( RefStockDistReqStatusKey.OPEN, "core.msg.DISTREQ_CHANGED_TO_OPEN" );
   }


   /**
    * Change from PICKED to INPROGRESS
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void
         test_GIVEN_PickedStockDistRequest_WHEN_CancelSomeTransfer_THEN_TheRequestStatusChangeToINPROGRESS()
               throws Exception {

      createStockDistReqWithPendingXfer( RefStockDistReqStatusKey.PICKED, PEND_XFER_QTY );

      // Create one more transfer to make the request as fully picked
      createPickedItemWithTransfer( NEEDED_QTY - PEND_XFER_QTY );

      cancelTransfer();

      assertResult( RefStockDistReqStatusKey.INPROGRESS, "core.msg.DISTREQ_CHANGED_TO_INPROGRESS" );
   }


   /**
    * Change from PICKED to OPEN
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void
         test_GIVEN_PickedStockDistRequest_WHEN_CancelAllTransfer_THEN_TheRequestStatusChangeToOPEN()
               throws Exception {

      createStockDistReqWithPendingXfer( RefStockDistReqStatusKey.PICKED, NEEDED_QTY );

      cancelTransfer();

      assertResult( RefStockDistReqStatusKey.OPEN, "core.msg.DISTREQ_CHANGED_TO_OPEN" );
   }


   /**
    * Change from PICKED to COMPLETED
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void
         test_GIVEN_PickedStockDistRequest_WHEN_CompleteAllTransfer_THEN_TheRequestStatusChangeToCOMPLETED()
               throws Exception {

      createStockDistReq( RefStockDistReqStatusKey.PICKED );

      PartNoKey lPart = Domain.createPart( pojo -> {
         pojo.setQtyUnitKey( QTY_UNIT );
         pojo.setStockNoKey( iBatchStockNo );
      } );

      InventoryKey lInventory = Domain.createBatchInventory( pojo -> {
         pojo.setPartNumber( lPart );
         pojo.setBinQt( NEEDED_QTY );
         pojo.setLocation( Domain.createLocation( location -> {
            location.setType( RefLocTypeKey.SRVSTORE );
            location.setCode( "locMain" );
         } ) );
      } );

      // Create transfer
      CreateTransferTO lTO = new CreateTransferTO();
      lTO.setTransferToCd( InvLocTable.findByPrimaryKey( iLineWarehouse ).getLocCd() );
      lTO.setInventory( lInventory );
      lTO.setQuantity( NEEDED_QTY );
      iTransfer = TransferService.create( lTO );

      createPickedItem( iTransfer );

      clearLog();
      TransferService.complete( iTransfer, null, null, false, false, false, null );

      assertResult( RefStockDistReqStatusKey.COMPLETED, "core.msg.DISTREQ_CHANGED_TO_COMPLETED" );
   }


   private void createStockDistReqWithPendingXfer( RefStockDistReqStatusKey aStatus,
         double aXferQty ) {

      createStockDistReq( aStatus );

      iTransfer = createPickedItemWithTransfer( aXferQty );
   }


   private void createStockDistReq( RefStockDistReqStatusKey aStatus ) {

      iStockDistReq = Domain.createStockDistReq( pojo -> {
         pojo.setNeededLocation( iStockLevel.getLocationKey() );
         pojo.setStockNo( iStockLevel.getStockNoKey() );
         pojo.setOwner( iStockLevel.getOwnerkey() );
         pojo.setNeededQuantity( new Float( NEEDED_QTY ) );
         pojo.setStatus( aStatus );
         pojo.setQtyUnit( RefQtyUnitKey.EA );
      } );
   }


   private TransferKey createPickedItemWithTransfer( double aXferQty ) {

      TransferKey lTransfer =
            new InventoryTransferBuilder().withQuantity( new BigDecimal( aXferQty ) )
                  .withStatus( RefEventStatusKey.LXPEND ).withQtyUnit( RefQtyUnitKey.EA ).build();

      createPickedItem( lTransfer );

      return lTransfer;
   }


   private void createPickedItem( TransferKey aTransfer ) {
      Domain.createStockDistReqPickedItem( pojo -> {
         pojo.setStockDistReq( iStockDistReq );
         pojo.setTransferKey( aTransfer );
      } );
   }


   private void cancelTransfer() throws TriggerException {

      clearLog();
      TransferService.cancel( iTransfer, null, null, null, null );
   }


   private void clearLog() {

      // clear the log of stock dist request
      MxDataAccess.getInstance().executeDelete( StockDistReqLogDao.TABLE_NAME,
            iStockDistReq.getPKWhereArg() );
   }


   private void assertResult( RefStockDistReqStatusKey aExpectedStatus, String aExpectedNote ) {

      // assert the status is changed
      assertEquals( aExpectedStatus, InjectorContainer.get().getInstance( StockDistReqDao.class )
            .findByPrimaryKey( iStockDistReq ).getStatus() );

      // assert status change event is logged
      final String NOTE_COLUMN = StockDistReqLogDao.ColumnName.NOTES.toString();

      QuerySet lQs = QuerySetFactory.getInstance().executeQuery( StockDistReqLogDao.TABLE_NAME,
            iStockDistReq.getPKWhereArg(), NOTE_COLUMN );

      assertEquals( 1, lQs.getRowCount() );

      lQs.next();
      assertEquals(
            i18n.get( aExpectedNote,
                  InjectorContainer.get().getInstance( EvtEventDao.class )
                        .findByPrimaryKey( iTransfer.getEventKey() ).getEventSdesc() ),
            lQs.getString( NOTE_COLUMN ) );
   }

}
