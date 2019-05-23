
package com.mxi.mx.core.services.stocklevel.checker;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.InventoryTransferBuilder;
import com.mxi.am.domain.builder.OwnerDomainBuilder;
import com.mxi.am.domain.builder.StockBuilder;
import com.mxi.am.domain.builder.StockLevelBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.i18n.i18n;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InvLocStockKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefStockDistReqStatusKey;
import com.mxi.mx.core.key.RefStockLowActionKey;
import com.mxi.mx.core.key.StockDistReqKey;
import com.mxi.mx.core.key.StockDistReqLogKey;
import com.mxi.mx.core.key.StockNoKey;
import com.mxi.mx.core.key.TransferKey;
import com.mxi.mx.core.materials.stockdistribution.infra.StockDistReqDao;
import com.mxi.mx.core.materials.stockdistribution.infra.StockDistReqLogDao;
import com.mxi.mx.core.materials.stockdistribution.infra.StockDistReqLogTableRow;
import com.mxi.mx.core.materials.stockdistribution.infra.StockDistReqTableRow;
import com.mxi.mx.core.services.stocklevel.WarehouseStockLevelChecker;
import com.mxi.mx.core.table.eqp.EqpPartNoTable;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.inv.InvLocStockTable;


/**
 * This abstract class contains all common data and test cases to test
 * {@link WarehouseStockLevelChecker}.
 *
 * @author Libin Cai
 * @since February 1, 2019
 */
@RunWith( BlockJUnit4ClassRunner.class )
public abstract class WarehouseStockLevelCheckerCommonTestCases {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   protected static final RefQtyUnitKey QTY_UNIT = RefQtyUnitKey.EA;
   protected static final double MAX_QTY_ZERO = 0;

   // --------------------------------------------------------------------------------------------------------------
   // The field variables to be set by the extended testing class when loading data
   // --------------------------------------------------------------------------------------------------------------

   /** the inventory class for part, stock no and inventory */
   private RefInvClassKey iInvClass;

   /** the total quantity in the warehouse */
   protected double iWareHouseQty;

   /** the inventory quantity in warehouse itself */
   private double iWarehouseInvQty;

   // --------------------------------------------------------------------------------------------------------------
   // The field variables to be initialized in this class
   // --------------------------------------------------------------------------------------------------------------

   /** the variables for stock level */
   private LocationKey iWarehouse;
   private StockNoKey iStockNo;
   private InvLocStockKey iStockLevel;
   private double iReorderQty;

   /** the variables for warehouse and its sub loc */
   private LocationKey iWarehouseSubLoc;
   private PartNoKey iPartInWarehouseSubLoc;
   private PartNoKey iPartInWarehouse;
   // the inventory quantity in warehouse sub-location
   private double iSubInvQty;
   protected InventoryKey iInvInWarehouseSubLoc;

   private List<StockDistReqKey> iStockDistRequests;


   protected void loadData( RefInvClassKey aInvClass, double aWarehouseQty,
         double aWarehouseInvQty ) throws Exception {

      iInvClass = aInvClass;
      iWareHouseQty = aWarehouseQty;
      iWarehouseInvQty = aWarehouseInvQty;

      iReorderQty = iWareHouseQty;
      iSubInvQty = iWareHouseQty - iWarehouseInvQty;

      // --------------------------------------------------------------------------------------------------------------
      // Create a stock level at warehouse
      // --------------------------------------------------------------------------------------------------------------

      iWarehouse = Domain.createLocation( location -> {
         location.setType( RefLocTypeKey.SRVSTORE );
         location.setCode( "loc" );
      } );
      iWarehouseSubLoc = Domain.createLocation( location -> {
         location.setCode( "SUB_WAH" );
         location.setParent( iWarehouse );
      } );

      iStockNo = new StockBuilder().withInvClass( aInvClass ).withQtyUnitKey( QTY_UNIT ).build();

      // Although the stock level ignore the owner, we still set the owner because it is part of
      // primary key. The only difference in warehouse stock level for ignore or non-ignore is that:
      // if ignore, we don't consider the inventory owner when calculating the warehouse quantity.
      iStockLevel = new StockLevelBuilder( iWarehouse, iStockNo,
            new OwnerDomainBuilder().isDefault().build() ).withReorderQt( iReorderQty )
                  .withMaxLevel( MAX_QTY_ZERO ).withStockLowAction( RefStockLowActionKey.DISTREQ )
                  .withIgnoreOwnerBool( true ).build();

      // --------------------------------------------------------------------------------------------------------------
      // Create two inventories in warehouse and its sub location to meet the reorder quantity of
      // the stock level
      // --------------------------------------------------------------------------------------------------------------

      iPartInWarehouseSubLoc = createPart( iStockNo );
      iInvInWarehouseSubLoc =
            createInventory( iPartInWarehouseSubLoc, iWarehouseSubLoc, iSubInvQty );

      iPartInWarehouse = createPart( iStockNo );
      createInventory( iPartInWarehouse, iWarehouse, iWarehouseInvQty );

      // --------------------------------------------------------------------------------------------------------------
      // Make sure no stock low at this moment
      // --------------------------------------------------------------------------------------------------------------

      checkStockLevel();
      assertNoStockDistReqCreated();
   }


   @Test
   public void
         given_NonZeroMaxQuantityInStockLevel_WHEN_ReorderQtyIncreasedLessThanMaxQuantity_THEN_StockUpToMaxQuantity()
               throws Exception {

      // Increase reorder quantity to make stock low
      double lNewReorderQty = iReorderQty + 5;
      changeReorderQty( lNewReorderQty );

      // Change max quantity from 0 to more than the new reorder quantity
      double lNewMaxQty = lNewReorderQty + 2;
      InvLocStockTable lInvLocStoc = InvLocStockTable.findByPrimaryKey( iStockLevel );
      lInvLocStoc.setMaxQt( lNewMaxQty );
      lInvLocStoc.update();

      checkStockLevelAndAssertStockDistReqQuantity( lNewMaxQty, iWareHouseQty );
   }


   @Test
   public void
         given_LowActionMANUALInStockLevel_WHEN_StockLowOccurs_THEN_NoStockDistRequestCreated()
               throws Exception {

      // Change the stock low action to MANUAL for the data preparation
      InvLocStockTable lInvLocStoc = InvLocStockTable.findByPrimaryKey( iStockLevel );
      lInvLocStoc.setStockLowAction( RefStockLowActionKey.MANUAL );
      lInvLocStoc.update();

      unassignPartFromStockNo();

      checkStockLevel();
      assertNoStockDistReqCreated();
   }


   @Test
   public void given_InvInStock_WHEN_UnassignPartFromStock_THEN_StockDistReqCreated()
         throws Exception {

      unassignPartFromStockNo();

      checkStockLevelAndAssertNeededQuantityIsInventoryQuantityInWarehouseSubLoc();
   }


   @Test
   public void given_InventoryInStock_WHEN_ChangeInventoryToNonRFI_THEN_StockDistRequestCreated()
         throws Exception {

      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( iInvInWarehouseSubLoc );
      lInvInv.setInvCond( RefInvCondKey.SCRAP );
      lInvInv.update();

      checkStockLevelAndAssertNeededQuantityIsInventoryQuantityInWarehouseSubLoc();
   }


   @Test
   public void
         given_InventoryInSubLocOfStockWarehouse_WHEN_MoveOutOfWarehouse_THEN_StockDistRequestCreated()
               throws Exception {

      LocationKey lNewLoc = Domain.createLocation( location -> {
         location.setCode( "newloc" );
      } );

      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( iInvInWarehouseSubLoc );
      lInvInv.setLocation( lNewLoc );
      lInvInv.update();

      checkStockLevelAndAssertNeededQuantityIsInventoryQuantityInWarehouseSubLoc();
   }


   @Test
   public void given_InventoryInStock_WHEN_MarkItAsNotFound_THEN_StockDistRequestCreated()
         throws Exception {

      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( iInvInWarehouseSubLoc );
      lInvInv.setNotFoundBool( true );
      lInvInv.update();

      checkStockLevelAndAssertNeededQuantityIsInventoryQuantityInWarehouseSubLoc();
   }


   /**
    * Test scenario:
    *
    * GIVEN: Warehouse available quantity
    *
    * 1. Total inventory quantity: WH_QTY <br>
    * 3. Needed quantity in store distribution request: REQUESTED_QTY <br>
    * 4. Completed transfer quantity in store distribution request: DISTRIBUTED_QTY
    *
    * Warehouse_available_quantity = WH_QTY + REQUESTED_QTY - DISTRIBUTED_QTY
    *
    * WHEN: Increase reorder quantity to NEW_REORDER_QTY
    *
    * THEN: A new store distribution request is created and the needed quantity of the new one is
    *
    * NEW_REORDER_QTY - Warehouse available quantity = <br>
    * NEW_REORDER_QTY - ( WH_QTY + REQUESTED_QTY - DISTRIBUTED_QTY )
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void
         given_StockDistRequestExists_WHEN_CheckStockLow_THEN_TheRequestedQtyAndDistributedQtyAreConsidered()
               throws Exception {

      final double REQUESTED_QTY = 10.0;
      final double DISTRIBUTED_QTY = 8.0;
      final double NEW_REORDER_QTY = 70.0;

      changeReorderQty( NEW_REORDER_QTY );

      // Create a completed store distribution request (its needed quantity will not be considered
      // because it is completed)
      createStockDistReq( 1.0, RefStockDistReqStatusKey.COMPLETED );

      // Create a cancelled store distribution request (its needed quantity will not be considered
      // because it is cancelled)
      createStockDistReq( 2.0, RefStockDistReqStatusKey.CANCELED );

      // Create a store distribution request to stock up
      StockDistReqKey lStockDistReq =
            createStockDistReq( REQUESTED_QTY, RefStockDistReqStatusKey.INPROGRESS );

      // Create a completed transfer in the store distribution request
      TransferKey lTransfer =
            new InventoryTransferBuilder().withQuantity( new BigDecimal( DISTRIBUTED_QTY ) )
                  .withStatus( RefEventStatusKey.LXCMPLT ).withQtyUnit( RefQtyUnitKey.EA ).build();
      Domain.createStockDistReqPickedItem( pojo -> {
         pojo.setStockDistReq( lStockDistReq );
         pojo.setTransferKey( lTransfer );
      } );

      // Make sure new stock dist request is created because there is no unassigned OPEN stock dist
      // request is found
      checkStockLevelAndAssertStockDistReqQuantity( NEW_REORDER_QTY,
            iWareHouseQty + REQUESTED_QTY - DISTRIBUTED_QTY );
   }


   @Test
   public void
         given_OpenUnassignedStockDistRequestExists_WHEN_StockLowOccurs_THEN_TheNeededQuantityIsIncreased()
               throws Exception {

      double lOldStockDistReqNeededQuantity = 10;

      // Create a unassigned OPEN request
      double lNewReorderQuantity = createStockDistReqByStockLow( lOldStockDistReqNeededQuantity );

      // Change the reorder quantity to make stock low occur again
      double lReOrderQuantityIncreased = 2;
      lNewReorderQuantity += lReOrderQuantityIncreased;
      changeReorderQty( lNewReorderQuantity );

      // Make sure no new stock distribution request is created, and the needed quantity of the
      // existing one is increased.
      checkStockLevelAndAssertStockDistReqQuantity( lNewReorderQuantity, iWareHouseQty );

      assertHistoryNote( lOldStockDistReqNeededQuantity,
            lOldStockDistReqNeededQuantity + lReOrderQuantityIncreased );
   }


   @Test
   public void
         given_OpenUnassignedStockDistRequestExists_WHEN_StockHighByNewInvInWarehouse_THEN_TheNeededQuantityIsDecreased()
               throws Exception {
      stockHighTestByNewInventoryIntroduced( iPartInWarehouse, iWarehouse );
   }


   @Test
   public void
         given_OpenUnassignedStockDistRequestExists_WHEN_StockHighByNewInvInWarehouseSubLoc_THEN_TheNeededQuantityIsDecreased()
               throws Exception {
      stockHighTestByNewInventoryIntroduced( iPartInWarehouseSubLoc, iWarehouseSubLoc );
   }


   @Test
   public void given_OpenAssignedStockDistRequestExists_WHEN_StockLow_THEN_NewRequestIsCreated()
         throws Exception {

      double lOldStockDistReqNeededQuantity = 10;

      double lNewReorderQuantity = createStockDistReqByStockLow( lOldStockDistReqNeededQuantity );

      assignStockDistReq();

      // Change the reorder quantity to make stock low occur again
      double lReOrderQuantityIncreased = 2;
      lNewReorderQuantity += lReOrderQuantityIncreased;
      changeReorderQty( lNewReorderQuantity );

      // Make sure new stock distribution request is created, and the needed quantity is
      // lNewReorderQuantity-(WH_QTY+ lOldStockDistReqNeededQuantity)
      checkStockLevelAndAssertStockDistReqQuantity( lNewReorderQuantity,
            iWareHouseQty + lOldStockDistReqNeededQuantity );
   }


   private void checkStockLevelAndAssertNeededQuantityIsInventoryQuantityInWarehouseSubLoc()
         throws MxException, TriggerException {

      // Current warehouse quantity is iInvInWarehouse's bin quantity because
      // iBatchInvInWarehouseSubLoc is moved out
      checkStockLowAndAssertStockDistReqQuantity( iWarehouseInvQty );
   }


   private void stockHighTestByNewInventoryIntroduced( PartNoKey aInvPart,
         LocationKey aInvLocation ) throws MxException, TriggerException {

      double lOldStockDistReqNeededQuantity = 10;

      // Create a unassigned OPEN request
      double lNewReorderQuantity = createStockDistReqByStockLow( lOldStockDistReqNeededQuantity );

      double lBinQtyForNewInv = 1;

      // Create a new inventory in warehouse sub location to simulate the stock high
      createInventory( aInvPart, aInvLocation, lBinQtyForNewInv );

      // Make sure the needed quantity of the existing one is decreased.
      checkStockLevelAndAssertStockDistReqQuantity( lNewReorderQuantity,
            iWareHouseQty + lBinQtyForNewInv );

      assertHistoryNote( lOldStockDistReqNeededQuantity,
            lOldStockDistReqNeededQuantity - lBinQtyForNewInv );
   }


   private double createStockDistReqByStockLow( double aNeededQuantity )
         throws MxException, TriggerException {

      double lNewReorderQuantity = iReorderQty + aNeededQuantity;

      changeReorderQty( lNewReorderQuantity );

      checkStockLevelAndAssertStockDistReqQuantity( lNewReorderQuantity, iWareHouseQty );
      return lNewReorderQuantity;
   }


   private void unassignPartFromStockNo() {
      EqpPartNoTable lPartNoTable = EqpPartNoTable.findByPrimaryKey( iPartInWarehouseSubLoc );
      lPartNoTable.setStockNumber( null );
      lPartNoTable.update();
   }


   private void assignStockDistReq() {

      StockDistReqDao lStockDistReqDao =
            InjectorContainer.get().getInstance( StockDistReqDao.class );

      StockDistReqTableRow lStockDistReqTableRow =
            lStockDistReqDao.findByPrimaryKey( iStockDistRequests.get( 0 ) );

      lStockDistReqTableRow.setAssignedHr( new HumanResourceKey( 1, 1 ) );
      lStockDistReqDao.update( lStockDistReqTableRow );
   }


   private InvLocStockTable changeReorderQty( double aNewReorderQuantity ) {

      // Change the reorder quantity to make stock low occur
      InvLocStockTable lInvLocStoc = InvLocStockTable.findByPrimaryKey( iStockLevel );
      lInvLocStoc.setReorderQt( aNewReorderQuantity );
      lInvLocStoc.update();
      return lInvLocStoc;
   }


   private void assertHistoryNote( Double aOldStockDistReqNeededQuantity,
         Double aNewStockDistReqNeededQuantity ) {

      // Make sure the history note is correct
      String[] lKeyColumns = new StockDistReqLogKey( 0, 0, 0 ).getKeyColumnName();

      QuerySet lQs = QuerySetFactory.getInstance().executeQuery( StockDistReqLogDao.TABLE_NAME,
            iStockDistRequests.get( 0 ).getPKWhereArg(), lKeyColumns );

      // skip the first history note which is for stock dist request creation
      lQs.next();

      if ( lQs.next() ) {

         String lSystemNote =
               new StockDistReqLogTableRow( lQs.getKey( StockDistReqLogKey.class, lKeyColumns ) )
                     .getNotes();

         // Make sure the internationalization composition of message is correct in case the code is
         // changed by accident
         String lQtyUnitCode = " " + QTY_UNIT.getCd();
         String lOldQtyAndUnit = aOldStockDistReqNeededQuantity + lQtyUnitCode;
         String lNewQtyAndUnit = aNewStockDistReqNeededQuantity + lQtyUnitCode;
         assertEquals( i18n.get( "core.msg.DISTREQ_NEEDED_QUANTITY_ADJUSTED", lOldQtyAndUnit,
               lNewQtyAndUnit ), lSystemNote );

         // Make sure the message itself is correct in case the property file is changed by accident
         assertEquals( "The needed quantity for the stock distribution request is adjusted from "
               + lOldQtyAndUnit + " to " + lNewQtyAndUnit + ".", lSystemNote );
      } else {
         Assert.fail( "No history note is created for needed quantity adjustment." );
      }
   }


   private PartNoKey createPart( StockNoKey aStock ) {

      return Domain.createPart( part -> {
         part.setInventoryClass( iInvClass );
         part.setPartStatus( RefPartStatusKey.ACTV );
         part.setQtyUnitKey( QTY_UNIT );
         part.setStockNoKey( aStock );
      } );
   }


   private StockDistReqKey createStockDistReq( double aNeededQty,
         RefStockDistReqStatusKey aStatus ) {

      return Domain.createStockDistReq( pojo -> {
         pojo.setNeededLocation( iStockLevel.getLocationKey() );
         pojo.setStockNo( iStockLevel.getStockNoKey() );
         pojo.setNeededQuantity( new Float( aNeededQty ) );
         pojo.setStatus( aStatus );
         pojo.setQtyUnit( RefQtyUnitKey.EA );
      } );
   }


   private InventoryKey createInventory( PartNoKey aPart, LocationKey aLocation, Double aBinQty ) {

      return new InventoryBuilder().withPartNo( aPart ).withSerialNo( "sn" ).withClass( iInvClass )
            .atLocation( aLocation ).withBinQt( aBinQty ).withCondition( RefInvCondKey.RFI )
            .build();
   }


   /**
    * @param aStockupToQty
    *           the stock level's max quantity or reorder quantity if the max quantity doesn't exist
    * @param aWarehouseQty
    *           the warehouse inventory available quantity plus the on request quantity (the total
    *           of needed quantity of the Not-Completed and Not-Cancelled stock distribution
    *           request)
    */
   private void checkStockLevelAndAssertStockDistReqQuantity( double aStockupToQty,
         double aWarehouseQty ) throws MxException, TriggerException {

      checkStockLevel();
      assertStockDistReqNumber( 1 );

      assertEquals( new Float( aStockupToQty - aWarehouseQty ),
            new StockDistReqTableRow( iStockDistRequests.get( 0 ) ).getNeededQty() );

   }


   private void checkStockLevel() throws MxException, TriggerException {

      // check stock low
      iStockDistRequests =
            new WarehouseStockLevelChecker( iStockNo, iWarehouse, null ).checkStockLevels();

      // when no stock dist request is created, there might be an existing stock dist req changed,
      // e.g. increased needed quantity
      if ( iStockDistRequests.isEmpty() ) {

         String[] lKeyColumns = new StockDistReqKey( 0, 0 ).getKeyColumnName();

         DataSetArgument lArgs = iStockLevel.getStockNoKey().getPKWhereArg();
         lArgs.add( iStockLevel.getLocationKey(), "needed_loc_db_id", "needed_loc_id" );

         QuerySet lQs = QuerySetFactory.getInstance().executeQuery( StockDistReqDao.TABLE_NAME,
               lArgs, lKeyColumns );

         while ( lQs.next() ) {
            iStockDistRequests.add( lQs.getKey( StockDistReqKey.class, lKeyColumns ) );
         }
      }
   }


   private void assertNoStockDistReqCreated() {
      assertStockDistReqNumber( 0 );
   }


   private void assertStockDistReqNumber( int aExpectedNumber ) {
      assertEquals( aExpectedNumber, iStockDistRequests.size() );
   }


   /**
    * @param aWarehouseQty
    *           the warehouse inventory available quantity plus the on request quantity (the total
    *           of needed quantity of the Not-Completed and Not-Cancelled stock distribution
    *           request)
    */
   protected void checkStockLowAndAssertStockDistReqQuantity( double aWarehouseQty )
         throws MxException, TriggerException {
      checkStockLevelAndAssertStockDistReqQuantity( iReorderQty, aWarehouseQty );
   }
}
