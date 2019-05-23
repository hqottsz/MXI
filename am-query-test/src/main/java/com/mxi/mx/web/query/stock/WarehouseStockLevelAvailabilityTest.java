/**
 * Query test for WarehouseStockLevelAvailability.qrx
 *
 */

package com.mxi.mx.web.query.stock;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.InventoryTransferBuilder;
import com.mxi.am.domain.builder.OwnerDomainBuilder;
import com.mxi.am.domain.builder.StockBuilder;
import com.mxi.am.domain.builder.StockLevelBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.InvLocStockKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefXferTypeKey;
import com.mxi.mx.core.key.StockNoKey;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.inv.InvLocStockTable;


@RunWith( BlockJUnit4ClassRunner.class )
public final class WarehouseStockLevelAvailabilityTest {

   @Rule
   public final DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();
   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static final RefQtyUnitKey QTY_UNIT = RefQtyUnitKey.EA;
   private static final double BIN_QTY_WAREHOUSE_INV = 8.0;
   private static final double BIN_QTY_WAREHOUSE_SUBLOC_INV = 5.0;

   private LocationKey iWarehouse;
   private LocationKey iSubWarehouse;

   private OwnerKey iStockLevelOwner;

   private StockNoKey iBatchStockNo;
   private InvLocStockKey iStockLevel;

   private PartNoKey iBatchPartInStore;
   private InventoryKey iBatchInvInWarehouse;


   @Before
   public void setup() throws Exception {

      iWarehouse = Domain.createLocation( location -> {
         location.setType( RefLocTypeKey.SRVSTORE );
         location.setCode( "WAH1" );
      } );

      iBatchStockNo = createStockNo( RefInvClassKey.BATCH );

      iStockLevelOwner = new OwnerDomainBuilder().isDefault().build();

      // Although the stock level ignore the owner, we still set the owner because it is part of
      // primary key. The only difference in warehouse stock level for ignore or non-ignore is that:
      // if ignore, we don't consider the inventory owner when calculating the warehouse quantity.
      iStockLevel = new StockLevelBuilder( iWarehouse, iBatchStockNo, iStockLevelOwner )
            .withIgnoreOwnerBool( true ).build();

      // --------------------------------------------------------------------------------------------------------------
      // Create two inventories in warehouse and its sub location
      // --------------------------------------------------------------------------------------------------------------

      iBatchPartInStore = createPart( iBatchStockNo, RefInvClassKey.BATCH );
      iBatchInvInWarehouse = createInventory( iBatchPartInStore, iWarehouse, RefInvClassKey.BATCH,
            BIN_QTY_WAREHOUSE_INV );

      iSubWarehouse = Domain.createLocation( location -> location.setParent( iWarehouse ) );
      createInventory( iBatchPartInStore, iSubWarehouse, RefInvClassKey.BATCH,
            BIN_QTY_WAREHOUSE_SUBLOC_INV );
   }


   @Test
   public void test_GIVEN_StockLevelWithIgnoreOwnerAsTrue_THEN_OwnerIsIgnored() {

      // In setup() method, stock level IgnoreOwnerBool is set as TRUE; Two RFI inventories with
      // stock level owner in warehouse and its sub location. Now we change one inventory owner to
      // another one.
      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( iBatchInvInWarehouse );
      lInvInv.setOwner( new OwnerDomainBuilder().isDefault().build() );
      lInvInv.update();

      runQueryAndAssertBothInventoriesAreConsidered();
   }


   @Test
   public void test_GIVEN_TwoStockLevels_THEN_TwoRowsReturnedWithCorrectData() {

      // In setup() method, there is already one stock level with two inventories, so we create
      // another one
      new StockLevelBuilder( Domain.createLocation( location -> {
         location.setType( RefLocTypeKey.SRVSTORE );
         location.setCode( "WAH2" );
      } ), iBatchStockNo, iStockLevelOwner ).build();

      QuerySet lQs = runQuery( iBatchStockNo );

      assertEquals( 2, lQs.getRowCount() );

      // the result is ordered by warehouse location code, so the first one is WAH1
      assertQuerySet( lQs, BIN_QTY_WAREHOUSE_INV + BIN_QTY_WAREHOUSE_SUBLOC_INV, 0, 0, 0 );

      // the second stock level has no inventory
      assertQuerySet( lQs, 0, 0, 0, 0 );
   }


   @Test
   public void test_GIVEN_StockLevelWithIgnoreOwnerAsFalse_THEN_OwnerIsConsidered() {

      // In setup() method, stock level IgnoreOwnerBool is set as TRUE; Two RFI inventories with
      // stock level owner in warehouse and its sub location. Now we change IgnoreOwnerBool as FALSE
      // and one inventory owner to another one.
      InvLocStockTable lInvLocStock = InvLocStockTable.findByPrimaryKey( iStockLevel );
      lInvLocStock.setIgnoreOwnerBool( false );
      lInvLocStock.update();

      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( iBatchInvInWarehouse );
      lInvInv.setOwner( new OwnerDomainBuilder().isDefault().build() );
      lInvInv.update();

      runQueryAndAssertOnlyInventoryInSubLocationIsConsidered();
   }


   @Test
   public void test_GIVEN_WarehouseInventoryWithRFI_THEN_TheInventoryIsConsidered() {

      // In setup() method, two RFI inventories are created in warehouse and its sub location. So we
      // don't need other data creation here.

      runQueryAndAssertBothInventoriesAreConsidered();
   }


   @Test
   public void test_GIVEN_WarehouseInventoryWithNonRFI_THEN_TheInventoryIsNotConsidered() {

      // In setup() method, two RFI inventories are created in warehouse and its sub location. So we
      // change one of them to non-RFI.
      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( iBatchInvInWarehouse );
      lInvInv.setInvCond( RefInvCondKey.INSPREQ );
      lInvInv.update();

      runQueryAndAssertOnlyInventoryInSubLocationIsConsidered();
   }


   @Test
   public void test_GIVEN_WarehouseInventoryWithRFIAndIssued_THEN_TheInventoryIsNotConsidered() {

      // In setup() method, two RFI inventories are created in warehouse and its sub location. So we
      // change one of them as issued.
      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( iBatchInvInWarehouse );
      lInvInv.setIssuedBool( true );
      lInvInv.update();

      runQueryAndAssertOnlyInventoryInSubLocationIsConsidered();
   }


   @Test
   public void test_GIVEN_WarehouseInventoryWithRFIAndNotFound_THEN_TheInventoryIsNotConsidered() {

      // In setup() method, two RFI inventories are created in warehouse and its sub location. So we
      // change one of them as not found.
      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( iBatchInvInWarehouse );
      lInvInv.setNotFoundBool( true );
      lInvInv.update();

      runQueryAndAssertOnlyInventoryInSubLocationIsConsidered();
   }


   @Test
   public void test_GIVEN_WarehouseInventoryWithRFIAndNotLoose_THEN_TheInventoryIsNotConsidered() {

      // In setup() method, two RFI inventories are created in warehouse and its sub location. So we
      // change one of them as not loose.
      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( iBatchInvInWarehouse );
      lInvInv.setNhInvNo( new InventoryKey( 1, 1 ) );
      lInvInv.update();

      runQueryAndAssertOnlyInventoryInSubLocationIsConsidered();
   }


   @Test
   public void
         test_GIVEN_WarehouseInventoryWithRFIAndReserved_THEN_TheReservedQuantityNotConsideredAsAvailability() {

      // In setup() method, two RFI inventories are created in warehouse and its sub location. So we
      // change one of them as partially reserved.
      double lReservedQty = 2.0;
      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( iBatchInvInWarehouse );
      lInvInv.setReservedBool( true );
      lInvInv.setReservedQt( lReservedQty );
      lInvInv.update();

      runQueryAndAssertResult( BIN_QTY_WAREHOUSE_INV - lReservedQty + BIN_QTY_WAREHOUSE_SUBLOC_INV,
            lReservedQty, 0, 0 );
   }


   @Test
   public void
         test_GIVEN_WarehouseInventoryWithRFIAndInKit_THEN_TheAvailabilityAndInKitResultIsCorrect() {

      // In setup() method, two RFI inventories are created in warehouse and its sub location. So we
      // change one of them as in-kit inventory.
      Domain.createKitInventory(
            kitInventory -> kitInventory.addKitContentInventory( iBatchInvInWarehouse ) );

      runQueryAndAssertResult( BIN_QTY_WAREHOUSE_SUBLOC_INV, 0, 0, BIN_QTY_WAREHOUSE_INV );
   }


   @Test
   public void test_GIVEN_PendingTransferInventoryToWarehouse_THEN_ThePendingXferResultIsCorrect() {

      testPendingTransfer( iWarehouse, false );
   }


   @Test
   public void
         test_GIVEN_PendingTransferInventoryToWarehouseSubLoc_THEN_ThePendingXferResultIsCorrect() {

      testPendingTransfer( iSubWarehouse, false );
   }


   @Test
   public void test_GIVEN_PendingTransferInKitInventoryToWarehouse_THEN_TheInKitResultIsCorrect() {

      testPendingTransfer( iWarehouse, true );
   }


   @Test
   public void
         test_GIVEN_PendingTransferInKitInventoryToWarehouseSubLoc_THEN_TheInKitResultIsCorrect() {

      testPendingTransfer( iSubWarehouse, true );
   }


   @Test
   public void
         test_GIVEN_PendingTransferWithinTheWarehouse_THEN_TheInventoryIsNotConsideredInPendingXferResult() {

      BigDecimal lXferInInvBinQty = BigDecimal.TEN;

      InventoryKey lInvXferInInv = createInventory( iBatchPartInStore, new LocationKey( 1, 1 ),
            RefInvClassKey.BATCH, lXferInInvBinQty.doubleValue() );

      createTransferBuilder().withInventory( lInvXferInInv ).withQuantity( lXferInInvBinQty )
            .withSource( iWarehouse ).withDestination( iSubWarehouse ).build();

      runQueryAndAssertResult( BIN_QTY_WAREHOUSE_INV + BIN_QTY_WAREHOUSE_SUBLOC_INV, 0, 0, 0 );
   }


   @Test
   public void
         test_GIVEN_WarehouseSerializedInventoryWithRFI_THEN_TheAvialabilityResultIsCorrect() {

      StockNoKey lSerStockNo = createStockNo( RefInvClassKey.SER );

      new StockLevelBuilder( iWarehouse, lSerStockNo, iStockLevelOwner ).withIgnoreOwnerBool( true )
            .build();

      PartNoKey lSerPart = createPart( lSerStockNo, RefInvClassKey.SER );
      createInventory( lSerPart, iWarehouse, RefInvClassKey.SER, null );

      runQueryAndAssertResult( lSerStockNo, 1, 0, 0, 0 );
   }


   /**
    * In setup() method, two RFI inventories are created in warehouse and its sub location. So we
    * create new one at a location outside of the warehouse, and create PEND transfer of this new
    * inventory to the given destination.
    *
    * @param aDestination
    *           the warehouse or its sub location
    * @param aIsInKit
    *           whether the inventory is in-kit or not
    */
   private void testPendingTransfer( LocationKey aDestination, boolean aIsInKit ) {

      BigDecimal lXferInInvBinQty = BigDecimal.TEN;
      BigDecimal lXferQty = BigDecimal.ONE;

      InventoryKey lInvXferInInv = createInventory( iBatchPartInStore, new LocationKey( 1, 1 ),
            RefInvClassKey.BATCH, lXferInInvBinQty.doubleValue() );

      InventoryKey lInvInXfer = lInvXferInInv;
      double lExpectedPendXferQty = lXferQty.doubleValue();

      // In-kit inv cannot be transfered alone; if the kit inv is transfered, its in-kit-inv is not
      // considered when calculate pending xfer quantity, and then to in-kit quantity. In the other
      // words, if a kit inv is in a inbound pending transfer, its contend inventory is not
      // calculated for both pending transfer quantity and in-kit quantity
      double lExpectedInKitQty = 0;

      if ( aIsInKit ) {

         lInvInXfer = Domain.createKitInventory(
               kitInventory -> kitInventory.addKitContentInventory( lInvXferInInv ) );

         // For in-kit inventory transfer, the transfer quantity will be calculated in the in-kit
         // quantity, not the pending transfer quantity.
         lExpectedPendXferQty = 0;
      }

      createTransferBuilder().withInventory( lInvInXfer ).withQuantity( lXferQty )
            .withSource( new LocationKey( 1, 1 ) ).withDestination( aDestination )
            .withQtyUnit( QTY_UNIT ).build();

      runQueryAndAssertResult( BIN_QTY_WAREHOUSE_INV + BIN_QTY_WAREHOUSE_SUBLOC_INV, 0,
            lExpectedPendXferQty, lExpectedInKitQty );
   }


   private InventoryTransferBuilder createTransferBuilder() {
      return new InventoryTransferBuilder().withType( RefXferTypeKey.STKTRN )
            .withStatus( RefEventStatusKey.LXPEND );
   }


   private void runQueryAndAssertBothInventoriesAreConsidered() {
      runQueryAndAssertResult( BIN_QTY_WAREHOUSE_INV + BIN_QTY_WAREHOUSE_SUBLOC_INV, 0, 0, 0 );
   }


   private void runQueryAndAssertOnlyInventoryInSubLocationIsConsidered() {
      runQueryAndAssertResult( BIN_QTY_WAREHOUSE_SUBLOC_INV, 0, 0, 0 );
   }


   private void runQueryAndAssertResult( double aExpectedAvailQty, double aExpectedReservedQty,
         double aExpectedPendXferQty, double aExpectedInKitQty ) {

      runQueryAndAssertResult( iBatchStockNo, aExpectedAvailQty, aExpectedReservedQty,
            aExpectedPendXferQty, aExpectedInKitQty );
   }


   private void runQueryAndAssertResult( StockNoKey aStockNo, double aExpectedAvailQty,
         double aExpectedReservedQty, double aExpectedPendXferQty, double aExpectedInKitQty ) {

      QuerySet lQs = runQuery( aStockNo );

      assertEquals( 1, lQs.getRowCount() );

      assertQuerySet( lQs, aExpectedAvailQty, aExpectedReservedQty, aExpectedPendXferQty,
            aExpectedInKitQty );
   }


   private QuerySet runQuery( StockNoKey aStockNo ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aStockNo, "aStockNoDbId", "aStockNoId" );

      return QueryExecutor.executeQuery( getClass(), lArgs );
   }


   private void assertQuerySet( QuerySet aQs, double aExpectedAvailQty, double aExpectedReservedQty,
         double aExpectedPendXferQty, double aExpectedInKitQty ) {

      aQs.next();

      assertEquals( aExpectedAvailQty, aQs.getDouble( "available_qty" ), 0 );
      assertEquals( aExpectedReservedQty, aQs.getDouble( "reserved_qty" ), 0 );
      assertEquals( aExpectedPendXferQty, aQs.getDouble( "pend_xfer_in_qty" ), 0 );
      assertEquals( aExpectedInKitQty, aQs.getDouble( "in_kit_qty" ), 0 );
   }


   private StockNoKey createStockNo( RefInvClassKey aInventoryClass ) {

      return new StockBuilder().withInvClass( aInventoryClass ).withQtyUnitKey( QTY_UNIT ).build();
   }


   private PartNoKey createPart( StockNoKey aStock, RefInvClassKey aInvClass ) {

      return Domain.createPart( part -> {
         part.setInventoryClass( aInvClass );
         part.setPartStatus( RefPartStatusKey.ACTV );
         part.setQtyUnitKey( QTY_UNIT );
         part.setStockNoKey( aStock );
      } );
   }


   private InventoryKey createInventory( PartNoKey aPart, LocationKey aLocation,
         RefInvClassKey aInvClass, Double aBinQty ) {

      InventoryBuilder lInvBuilder = new InventoryBuilder().withPartNo( aPart ).withSerialNo( "sn" )
            .withClass( aInvClass ).atLocation( aLocation ).withCondition( RefInvCondKey.RFI )
            .withOwner( iStockLevelOwner );

      if ( aBinQty != null ) {
         lInvBuilder = lInvBuilder.withBinQt( aBinQty );
      }

      return lInvBuilder.build();
   }

}
