package com.mxi.mx.core.materials.stockdistribution.domain;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.InventoryTransferBuilder;
import com.mxi.am.domain.builder.OwnerDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.PartRequestBuilder;
import com.mxi.am.domain.builder.StockBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefReqTypeKey;
import com.mxi.mx.core.key.StockNoKey;
import com.mxi.mx.core.services.inventory.MxMoveInventoryWarning;
import com.mxi.mx.core.services.inventory.MxMoveInventoryWarning.WarningType;
import com.mxi.mx.core.table.inv.InvInvTable;


/**
 * This test class tests all the validations performed in
 * StockDistributionRequestService.validatePickedItem() method
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class StockDistRequestValidatePickedItemTest {

   private static final RefQtyUnitKey QTY_UNIT = RefQtyUnitKey.EA;
   private static final WarningType TYPE_ERROR = MxMoveInventoryWarning.WarningType.ERROR;
   private static final String DISTREQ_BARCODE = "DISTREQ1";

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   private InvInvTable iInvInvTable;
   private List<MxMoveInventoryWarning> iWarnings;


   @Before
   public void loadData() throws Exception {

      // create the airport
      LocationKey lAirport = Domain.createLocation( location -> {
         location.setType( RefLocTypeKey.AIRPORT );
         location.setCode( "airport" );
         location.setIsSupplyLocation( true );
      } );

      // create main warehouse
      LocationKey lMainWarehouse = Domain.createLocation( location -> {
         location.setType( RefLocTypeKey.SRVSTORE );
         location.setCode( "mainwarehouse" );
         location.setSupplyLocation( lAirport );
      } );

      // create line store and make main warehouse as it's supplier
      LocationKey lLineStore = Domain.createLocation( location -> {
         location.setType( RefLocTypeKey.SRVSTORE );
         location.setCode( "linestore" );
         location.setHubLocation( lMainWarehouse );
         location.setSupplyLocation( lAirport );
      } );

      // create stock
      StockNoKey lStock = new StockBuilder().withStockCode( "STK1" ).build();

      // create default owner
      OwnerKey lOwner = new OwnerDomainBuilder().isDefault().build();

      // create a stock distribution request
      Domain.createStockDistReq( pojo -> {
         pojo.setNeededQuantity( new Float( 12.0 ) );
         pojo.setQtyUnit( QTY_UNIT );
         pojo.setStockNo( lStock );
         pojo.setRequestId( DISTREQ_BARCODE );
         pojo.setOwner( lOwner );
         pojo.setNeededLocation( lLineStore );
      } );

      // create part in the stock
      PartNoKey lPart = new PartNoBuilder().withOemPartNo( "P1" ).withUnitType( QTY_UNIT )
            .withStock( lStock ).build();

      iInvInvTable = InvInvTable.findByPrimaryKey(
            new InventoryBuilder().withPartNo( lPart ).withBinQt( 10.0 ).withSerialNo( "SN101" )
                  .withClass( RefInvClassKey.BATCH ).atLocation( lMainWarehouse )
                  .withCondition( RefInvCondKey.RFI ).withOwner( lOwner ).build() );
   }


   @Test
   public void testValidatePickedItemERROR_PartNotAssignedToStock() throws Exception {

      PartNoKey lPart =
            new PartNoBuilder().withOemPartNo( "PART1" ).withShortDescription( "PART1" ).build();

      // set inventory to a part that is not assigned to the stock
      iInvInvTable.setPartNo( lPart );
      iInvInvTable.update();

      // call validation logic
      validatePickedItem( 5.0 );

      assertOneMessageReturned();

      // assert the warning type and message
      assertWarning( TYPE_ERROR, "core.lbl.PART_NO_IS_NOT_ASSIGNED_TO_STOCK_NO",
            "core.msg.ERROR_PART_NO_IS_NOT_ASSIGNED_TO_STOCK_NO",
            "The part no is not assigned to the stock no",
            "You cannot pick item {0} for this stock distribution request because it is not assigned to stock number {1}.<br><br> Confirm that the part number is assigned to the stock number.",
            null );
   }


   @Test
   public void testValidatePickedItemERROR_InvConditionNotRFI() throws Exception {

      // set inventory condition to non-RFI
      iInvInvTable.setInvCond( RefInvCondKey.INSPREQ );
      iInvInvTable.update();

      // call validation logic
      validatePickedItem( 5.0 );

      assertOneMessageReturned();

      // assert the error type and message
      assertWarning( TYPE_ERROR, "core.lbl.INVENTORY_CONDITION_IS_NOT_RFI",
            "core.msg.ERROR_INVENTORY_CONDITION_IS_NOT_RFI", "Inventory condition is not RFI",
            "You cannot pick an item whose inventory condition is not ready for issue (RFI).",
            null );
   }


   @Test
   public void testValidatePickedItemERROR_InvWithDiffOwner() throws Exception {

      // create another owner
      OwnerKey lOwner = new OwnerDomainBuilder().isNonLocal().build();
      iInvInvTable.setOwner( lOwner );
      iInvInvTable.update();

      // call validation logic
      validatePickedItem( 5.0 );

      assertOneMessageReturned();

      // assert the error type and message
      assertWarning( TYPE_ERROR, "core.lbl.INVENTORY_OWNER_DOES_NOT_MATCH",
            "core.msg.ERROR_INVENTORY_OWNER_DOES_NOT_MATCH", "Inventory owner does not match",
            "Inventory Owner does not match the owner for the Stock Distribution Request.", null );
   }


   @Test
   public void testValidatePickedItemERROR_InvAtDiffSupplyLocation() throws Exception {

      // create Inventory at different airport
      LocationKey lDiffLocation = Domain.createLocation( location -> {
         location.setType( RefLocTypeKey.AIRPORT );
         location.setCode( "newloc" );
         location.setIsSupplyLocation( true );
      } );
      iInvInvTable.setLocation( lDiffLocation );
      iInvInvTable.update();

      // call validation logic
      validatePickedItem( 5.0 );

      assertOneMessageReturned();

      // assert the error type and message
      assertWarning( TYPE_ERROR, "core.lbl.INVENTORY_LOCATION_DIFFERENT",
            "core.msg.ERROR_INVENTORY_LOCATION_DIFFERENT",
            "Inventory location is not under same supply location",
            "You cannot pick an item which is located at ''{0}'' because it is not under the same supply location.",
            null );
   }


   /**
    * This tests the formula that calculates the available quantity when locked reservation and/or
    * pending transfer exist.
    *
    * @throws Exception
    */
   @Test
   public void testValidatePickedItemERROR_BatchInvWithBothLockedAndPendingTransfer()
         throws Exception {

      new InventoryTransferBuilder().withInventory( iInvInvTable.getPk() )
            .withQuantity( BigDecimal.ONE ).withStatus( RefEventStatusKey.LXPEND ).build();

      // create part request with locked reservation
      PartRequestKey lPartRequest =
            new PartRequestBuilder().withType( RefReqTypeKey.ADHOC ).withRequestedQuantity( 1.0 )
                  .withReservedInventory( iInvInvTable.getPk() ).isLockedReservation().build();

      // create a pending transfer on the part request with locked reservation
      new InventoryTransferBuilder().withInventory( iInvInvTable.getPk() )
            .withQuantity( BigDecimal.ONE ).withStatus( RefEventStatusKey.LXPEND )
            .withInitEvent( lPartRequest.getEventKey() ).build();

      // Inventory bin_qty=10.0, locked reserved 1.0 with pending transfer, another pending transfer
      // 1.0, so the available is 10.0-1.0-1.0=8.0. Note: At this stage, we have two pending
      // transfers, one is for locked reservation, and another one is for a pure pending transfer.
      // To calculate the availability, we have to remove the duplicate transfer from the locked
      // reservation. The formula is bin_qty-pend_xfer_qty-locked_qty+pend_xfer_for_locked_qty.

      validatePickedItem( 10.0 );

      assertOneMessageReturned();

      assertWarning( TYPE_ERROR,
            "core.lbl.INVENTORY_HAS_PENDING_TRANSFER_AND_OR_LOCKED_RESERVATION",
            "core.msg.ERROR_INVENTORY_HAS_PENDING_TRANSFER_AND_OR_LOCKED_RESERVATION",
            "Inventory has pending transfers and/or locked reservations",
            "Due to pending transfers and/or locked reservations, the available quantity to pick is {0}.",
            null );

      validatePickedItem( 8.0 );
      assertEquals( 0, iWarnings.size() );
   }


   @Test
   public void testValidatePickedItemWARN_PickedMoreThanRequired() throws Exception {

      double lPickedQty = 15.0;

      // set inventory bin quantity more than the picked quantity to make sure there is only one
      // warning for picked more than required.
      iInvInvTable.setBinQt( lPickedQty + 1 );
      iInvInvTable.update();

      // call validation logic
      validatePickedItem( lPickedQty );

      assertOneMessageReturned();

      // assert the warning type and message
      assertWarning( MxMoveInventoryWarning.WarningType.PICKED_QT_MORE_THAN_REQUIRED,
            "core.lbl.PICK_QTY_MORE_THAN_REQUIRED", "core.msg.WARN_PICKED_QTY_MORE_THAN_REQUIRED",
            "Picked quantity is more than required quantity",
            "You have picked more items than the quantity {0} of needed inventory for this stock distribution request.",
            "12.0 " + QTY_UNIT.getCd() );
   }


   /**
    * This tests that if batch inventory is reserved with non-locked reservation, only warning will
    * be shown.
    *
    * @throws Exception
    */
   @Test
   public void testValidatePickedItemWARN_InvWithNonLockedReservation() throws Exception {

      double lPickedQty = 10.0;
      double lReservedQty = 2.0;

      // make the available quantity ( bin_qt - reserved_qt) < picked quantity
      iInvInvTable.setBinQt( lPickedQty );
      iInvInvTable.setReservedQt( lReservedQty );
      iInvInvTable.update();

      // call validation logic
      validatePickedItem( lPickedQty );

      assertOneMessageReturned();
      assertInsufficientQuantity( lPickedQty - lReservedQty + " " + QTY_UNIT.getCd() );
   }


   @Test
   public void testValidatePickedItemWARN_PickedMoreThanAvailable() throws Exception {

      double lPickedQty = 10.0;

      // set inventory bin quantity less than the picked quantity to get the picked more than
      // available warning
      double lBinQty = lPickedQty - 1;
      iInvInvTable.setBinQt( lBinQty );
      iInvInvTable.update();

      validatePickedItem( lPickedQty );

      assertOneMessageReturned();
      assertInsufficientQuantity( lBinQty + " " + QTY_UNIT.getCd() );
   }


   private void validatePickedItem( double aPickedQty ) throws MxException {
      iWarnings = new StockDistributionRequestService().validatePickedItem( DISTREQ_BARCODE,
            aPickedQty, iInvInvTable.getPk() );
   }


   private void assertOneMessageReturned() {
      assertEquals( 1, iWarnings.size() );
   }


   private void assertInsufficientQuantity( String aMessageParm ) {

      assertWarning( MxMoveInventoryWarning.WarningType.INSUFFICIENT_QUANTITY,
            "core.lbl.PICK_QTY_MORE_THAN_AVAILABLE", "core.msg.WARN_PICKED_QTY_MORE_THAN_AVAILABLE",
            "Picked quantity is more than available quantity",
            "You have picked more items than the available quantity {0}.", aMessageParm );
   }


   private void assertWarning( WarningType aWarningType, String aMsgTitleKey, String aMessageKey,
         String aMsgTitle, String aMessage, String aMessageParm ) {

      MxMoveInventoryWarning lWarning = iWarnings.get( 0 );

      assertEquals( aWarningType, lWarning.getType() );
      assertEquals( aMsgTitleKey, lWarning.getMsgTitleKey() );
      assertEquals( aMessageKey, lWarning.getMsgFrameKey() );
      assertEquals( aMsgTitle, lWarning.getMsgTitle() );
      assertEquals( aMessage, lWarning.getMsgFrame() );

      if ( aMessageParm != null ) {
         assertEquals( aMessageParm, lWarning.getParameters()[0].getDisplaySt() );
      }
   }
}
