package com.mxi.mx.core.services.shipment;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.ShipmentDomainBuilder;
import com.mxi.am.domain.builder.ShipmentLineBuilder;
import com.mxi.am.domain.builder.StockBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.exception.MandatoryArgumentException;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.exception.PositiveValueException;
import com.mxi.mx.common.i18n.i18n;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.ManufacturerKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefShipmentTypeKey;
import com.mxi.mx.core.key.ShipmentKey;
import com.mxi.mx.core.key.ShipmentLineKey;
import com.mxi.mx.core.key.StockNoKey;
import com.mxi.mx.core.services.inventory.pick.DuplicatePickedItemException;
import com.mxi.mx.core.services.inventory.pick.PickedItem;
import com.mxi.mx.core.services.inventory.pick.UnexpectedPickedItemException;
import com.mxi.mx.core.table.ship.ShipShipmentLineTable;
import com.mxi.mx.core.unittest.table.evt.EvtStage;


/**
 * This class is for testing ShipmentService logic related to picking items for shipment
 *
 * @author sufelk
 */

@RunWith( BlockJUnit4ClassRunner.class )
public class ShipmentServicePickItemTest {

   private static final String USERNAME = "testuser";
   private static final int USERID = 999;

   private static final String STOCK_NAME = "STOCKNAME";
   private static final String PART_A_IN_STOCK = "PART_A";
   private static final String PART_B_IN_STOCK = "PART_B";
   private static final String PART_NOT_IN_STOCK = "PART_C";
   private static final RefInvClassKey SERIAL = RefInvClassKey.SER;
   private static final RefInvClassKey BATCH = RefInvClassKey.BATCH;
   private static final String SERIAL_NO = "14129200001";
   private static final Double BIN_QTY = 5.0;
   private static final Double EXPECTED_QTY = 8.0;
   private static final String MP_KEY_SDESC = "qwerty";

   private static final String VENDOR_LOCATION = "YOW";
   private static final String DOCK_LOCATION = "CMB";

   private static final ManufacturerKey MANUFACTURER = new ManufacturerKey( 0, "MXI" );

   private HumanResourceKey iHr;
   private StockNoKey iStock;
   private PartNoKey iPartAInStock;
   private PartNoKey iPartBInStock;
   private ShipmentKey iShipment;
   private LocationKey iVendorLocation;
   private LocationKey iDockLocation;
   private InventoryKey iPartBInventory;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    *
    * GIVEN two serialized parts in the same stock, with a non-MPI generated shipment line for one
    * of the parts, WHEN inventory of the other part is picked, THEN the shipment line inventory
    * should be null.
    *
    * @throws TriggerException
    *            when a trigger method fails
    * @throws MxException
    *            when an unexpected exception occurs
    *
    */
   @Test
   public void testSerializedStockAlternatePickedForNonMpiGeneratedShipmentLine()
         throws MxException, TriggerException {

      loadDataForInventoryClass( SERIAL );

      // Create a shipment line for iPartAInStock
      ShipmentLineKey lNonMpiShipmentLine =
            new ShipmentLineBuilder( iShipment ).forPart( iPartAInStock ).build();

      // Assert that picking stock alternates is not allowed for shipment lines that are not
      // generated by MPI
      runMethodAndAssertInventoryNotPicked( lNonMpiShipmentLine );

   }


   /**
    *
    * GIVEN two serialized parts in the same stock, with an MPI generated shipment line for one of
    * the parts, WHEN inventory of the other part is picked, THEN the shipment line inventory should
    * be picked and the part number on the shipment line should be changed. Additionally, a history
    * note should also be added representing the part change.
    *
    * @throws TriggerException
    *            when a trigger method fails
    * @throws MxException
    *            when an unexpected exception occurs
    *
    */
   @Test
   public void testSerializedStockAlternatePickedForMpiGeneratedShipmentLine()
         throws MxException, TriggerException {

      loadDataForInventoryClass( SERIAL );

      // Create a shipment line for iPartAInStock
      ShipmentLineKey lMpiGeneratedShipmentLine =
            new ShipmentLineBuilder( iShipment ).forPart( iPartAInStock )
                  .withExpectedQuantity( BIN_QTY ).withMpKeySdesc( MP_KEY_SDESC ).build();

      // Pick iPartBInventory
      PickShipmentTO lPickTO = setupPickShipmentTO();
      ShipmentService.pickShipment( lPickTO, iHr );

      assertInventoryPickedAndPartNoChanged( lMpiGeneratedShipmentLine );

   }


   /**
    *
    * GIVEN two serialized parts that are not in the same stock, with an MPI generated shipment line
    * for one of the parts, WHEN inventory of the other part is picked, THEN the shipment line
    * inventory should be null.
    *
    * @throws TriggerException
    *            when a trigger method fails
    * @throws MxException
    *            when an unexpected exception occurs
    *
    */
   @Test
   public void testRandomSerializedPartPickedForMpiGeneratedShipmentLine()
         throws MxException, TriggerException {

      loadDataForInventoryClass( SERIAL );

      // create a part that is not in iStock
      PartNoKey lPartNotInStock = new PartNoBuilder().withOemPartNo( PART_NOT_IN_STOCK )
            .withInventoryClass( SERIAL ).build();

      // Create a shipment line for lPartNotInStock
      ShipmentLineKey lMpiGeneratedShipmentLine = new ShipmentLineBuilder( iShipment )
            .forPart( lPartNotInStock ).withMpKeySdesc( MP_KEY_SDESC ).build();

      // Inventory that is not in the same stock should not be picked for MPI generated shipment
      // lines.
      runMethodAndAssertInventoryNotPicked( lMpiGeneratedShipmentLine );

   }


   /**
    *
    * GIVEN two batch parts that are in the same stock, with a MPI generated shipment line for one
    * of the parts, WHEN inventory of the other part is picked with a qty less than the shipment
    * line qty, THEN the shipment line should be split AND the MP_KEY_SDESC should be copied to the
    * new line. The history note of the MPI generated shipment line split will also be checked
    * against the expected output. Along with the history note representing the part change.
    *
    * @throws TriggerException
    *            when a trigger method fails
    * @throws MxException
    *            when an unexpected exception occurs
    *
    */
   @Test
   public void testMpiGeneratedShipmentLineSplitWhenPickingBatchInvWithInsufficientQty()
         throws MxException, TriggerException {

      loadDataForInventoryClass( BATCH );

      // Create a shipment line for iPartAInStock
      ShipmentLineKey lMpiGeneratedShipmentLine =
            new ShipmentLineBuilder( iShipment ).forPart( iPartAInStock )
                  .withExpectedQuantity( EXPECTED_QTY ).withMpKeySdesc( MP_KEY_SDESC ).build();

      // Pick iPartBInventory
      PickShipmentTO lPickTO = setupPickShipmentTO();
      ShipmentService.pickShipment( lPickTO, iHr );

      // Assert that inventory has been picked and part number has been updated
      assertInventoryPickedAndPartNoChanged( lMpiGeneratedShipmentLine );

      // Assert that a new line has been added
      ShipmentLineKey[] lShipmentLines = ShipmentService.getShipmentLines( iShipment );
      assertEquals( 2, lShipmentLines.length );

      ShipShipmentLineTable lOldShipmentLineTable =
            ShipShipmentLineTable.findByPrimaryKey( lShipmentLines[0] );
      ShipShipmentLineTable lNewShipmentLineTable =
            ShipShipmentLineTable.findByPrimaryKey( lShipmentLines[1] );

      // Assert that the MP_KEY has been copied to the new line
      assertEquals( MP_KEY_SDESC,
            ShipmentLineService.getShipmentLineMpKeySdesc( lNewShipmentLineTable.getPk() ) );

      // Assert that the part number of the new line has not been updated
      assertEquals( iPartAInStock, lNewShipmentLineTable.getPartNo() );

      EvtStage lShipmentEventStage = new EvtStage( iShipment.getEventKey() );

      // Assert if the shipment line split history note is added properly
      lShipmentEventStage.assertStageNote( 1,
            i18n.get( "core.msg.SHIPMENT_PICK_ITEMS_SPLIT", lOldShipmentLineTable.getLineNoOrd(),
                  lNewShipmentLineTable.getLineNoOrd(), EXPECTED_QTY - BIN_QTY ) );

      // Assert if the alternate part change history note is added properly
      lShipmentEventStage.assertStageNote( 2,
            i18n.get(
                  "core.msg.SHIPMENT_LINE_STOCK_ALTERNATE_PICKED", ShipShipmentLineTable
                        .findByPrimaryKey( lMpiGeneratedShipmentLine ).getLineNoOrd(),
                  PART_A_IN_STOCK, PART_B_IN_STOCK ) );
   }


   private void loadDataForInventoryClass( RefInvClassKey aInvClassKey ) {

      // Create a human resource
      iHr = new HumanResourceDomainBuilder().withUsername( USERNAME ).withUserId( USERID ).build();

      // Create locations
      iVendorLocation = new LocationDomainBuilder().withType( RefLocTypeKey.VENDOR )
            .withCode( VENDOR_LOCATION ).build();
      iDockLocation = new LocationDomainBuilder().withType( RefLocTypeKey.DOCK ).withCode( DOCK_LOCATION )
            .withSupplyLocation( new LocationDomainBuilder().withType( RefLocTypeKey.AIRPORT )
                  .isSupplyLocation().build() )
            .build();

      // Create two parts in the same stock
      iStock = new StockBuilder().withInvClass( aInvClassKey ).withStockName( STOCK_NAME ).build();
      iPartAInStock = createPartInStock( PART_A_IN_STOCK, aInvClassKey );
      iPartBInStock = createPartInStock( PART_B_IN_STOCK, aInvClassKey );

      // Create inventory for iPartBInStock
      iPartBInventory = new InventoryBuilder().atLocation( iVendorLocation )
            .withPartNo( iPartBInStock ).withBinQt( BIN_QTY ).withCondition( RefInvCondKey.RFI )
            .withSerialNo( SERIAL_NO ).build();

      // Create a shipment for iPartAInStock
      iShipment = new ShipmentDomainBuilder().fromLocation( iVendorLocation ).toLocation( iDockLocation )
            .withType( RefShipmentTypeKey.PURCHASE ).build();

   }


   private PickShipmentTO setupPickShipmentTO() throws InvalidShipmentStatusException,
         MandatoryArgumentException, PositiveValueException, DuplicatePickedItemException {

      PickShipmentTO lPickTO = new PickShipmentTO( iShipment );

      // Pick BIN_QTY number of items from iPartBInventory
      PickedItem lPickedItem = new PickedItem( iPartBInventory, BIN_QTY, null );
      lPickTO.addPickedItem( lPickedItem );

      return lPickTO;

   }


   private void runMethodAndAssertInventoryNotPicked( ShipmentLineKey aShipmentLine )
         throws MxException, TriggerException {

      // Create a PickShipment transfer object to pick iPartBInventory
      PickShipmentTO lPickTO = setupPickShipmentTO();

      try {

         ShipmentService.pickShipment( lPickTO, iHr );
         Assert.fail( "Expected UnexpectedPickedItemException." );

      } catch ( UnexpectedPickedItemException e ) {
         // The UnexpectedPickedItemException should be thrown when trying to pick an invalid
         // inventory item, so no other validation is necessary since the assertion for the
         // exception is already in the try block.
      }

   }


   private void assertInventoryPickedAndPartNoChanged( ShipmentLineKey aShipmentLine ) {

      assertEquals( iPartBInventory,
            ShipShipmentLineTable.findByPrimaryKey( aShipmentLine ).getInventory() );

      assertEquals( iPartBInStock,
            ShipShipmentLineTable.findByPrimaryKey( aShipmentLine ).getPartNo() );
   }


   private PartNoKey createPartInStock( String aOemPartNo, RefInvClassKey aInvClassKey ) {
      return new PartNoBuilder().withOemPartNo( aOemPartNo ).withInventoryClass( aInvClassKey )
            .withStock( iStock ).manufacturedBy( MANUFACTURER ).build();
   }

}