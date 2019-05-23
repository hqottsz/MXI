package com.mxi.mx.core.query.inventorycount;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.ManufacturerKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefQtyUnitKey;


/**
 * This test class ensures that FindInventoryByBarcode.qrx query returns correct data.
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class FindInventoryByBarcodeTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private final static String LOCATION_CODE = "LOCATION_CODE";

   private final static String PART_NO_OEM = "PART_NO_OEM";
   private final static String PART_NO_SDESC = "PART_NO_SDESC";
   private final static String SER = "SER";
   private final static String EA = "EA";
   private final static String MANUFACTURER_CODE = "MANUFACT_CODE";

   private final static String SERIAL_NO_OEM = "SERIAL_NO_OEM";
   private final static String INV_BARCODE = "INV_BARCODE";
   private final static String ANOTHER_INV_BARCODE = "ANOTHER_INV_BARCODE";
   private final static String RFI = "RFI";

   private final static String KIT_SERIAL_NO_OEM = "KIT_SERIAL_NO_OEM";
   private final static String KIT_PART_NO_OEM = "KIT_PART_NO_OEM";
   private final static String ASSY_PART_NO_OEM = "ASSY_PART_NO_OEM";

   private LocationKey iLocation;
   private PartNoKey iPart;
   private InventoryKey iInventory;

   private QuerySet iQuerySet;


   @Test
   public void test_GIVEN_Inventory_WHEN_ExecuteQueryWithAnotherBarcode_THEN_NoInventoryFound() {

      // setup inventory with INV_BARCODE
      createInventory();

      // execute query with ANOTHER_INV_BARCODE
      executeQuery( ANOTHER_INV_BARCODE );

      // no inventory found
      assertNoInventoryFound();
   }


   @Test
   public void test_GIVEN_Inventory_WHEN_ExecuteQueryWithItsBarcode_THEN_InventoryFound() {

      // setup inventory with INV_BARCODE
      createInventory();

      // execute query
      executeQuery( INV_BARCODE );

      // assert that inventory data is retrieved correctly
      assertInventoryDataRetrievedCorrectly( false, false );
   }


   @Test
   public void test_GIVEN_KitSubComponentInventory_WHEN_ExecuteQuery_THEN_InventoryFound() {

      // create an inventory and assign it to kit
      createInventory();
      createKitInventory();

      // execute query
      executeQuery( INV_BARCODE );

      // assert that inventory data is retrieved correctly as a KIT subcomponent
      assertInventoryDataRetrievedCorrectly( true, false );
   }


   @Test
   public void test_GIVEN_EgineSubcomponent_WHEN_ExecuteQuery_THEN_InventoryFound() {

      // create and inventory and assign it to an engine as a sub component
      createInventory();
      createEngine();

      // execute query
      executeQuery( INV_BARCODE );

      // assert that inventory data is retrieved correctly as an ASSY subcomponent
      assertInventoryDataRetrievedCorrectly( false, true );
   }


   private void createInventory() {

      iLocation = Domain.createLocation( location -> {
         location.setCode( LOCATION_CODE );
      } );

      ManufacturerKey manufacture = Domain.createManufacturer( manufacturer -> {
         manufacturer.setCode( MANUFACTURER_CODE );
      } );

      iPart = Domain.createPart( part -> {
         part.setCode( PART_NO_OEM );
         part.setShortDescription( PART_NO_SDESC );
         part.setInventoryClass( RefInvClassKey.SER );
         part.setQtyUnitKey( RefQtyUnitKey.EA );
         part.setManufacturer( manufacture );
      } );

      iInventory = Domain.createSerializedInventory( inventory -> {
         inventory.setPartNumber( iPart );
         inventory.setSerialNumber( SERIAL_NO_OEM );
         inventory.setBarcode( INV_BARCODE );
         inventory.setCondition( RefInvCondKey.RFI );
         inventory.setLocation( iLocation );
      } );
   }


   // create a kit inventory with iInventory as subcomponent
   private void createKitInventory() {

      PartNoKey kitPart = Domain.createPart( part -> {
         part.setPartNoOem( KIT_PART_NO_OEM );
         part.setManufacturer( Domain.createManufacturer() );
         part.setInventoryClass( RefInvClassKey.KIT );
      } );

      Domain.createKitInventory( kit -> {
         kit.setPartNo( kitPart );
         kit.setSerialNo( KIT_SERIAL_NO_OEM );
         kit.addKitContentInventory( iInventory );
         kit.setLocation( iLocation );
      } );
   }


   // create an engine with iInventory as subcomponent
   private void createEngine() {

      PartNoKey enginePart = Domain.createPart( part -> {
         part.setPartNoOem( ASSY_PART_NO_OEM );
         part.setManufacturer( Domain.createManufacturer() );
         part.setInventoryClass( RefInvClassKey.ASSY );
      } );

      Domain.createEngine( engine -> {
         engine.setPartNumber( enginePart );
         engine.addSerialized( iInventory );
         engine.setLocation( iLocation );
      } );
   }


   /**
    * execute query FindInventoryByBarcode.qrx
    *
    * @param aBarcode
    */
   private void executeQuery( String aBarcode ) {

      DataSetArgument args = new DataSetArgument();
      args.add( "aBarcode", aBarcode );

      iQuerySet = QueryExecutor.executeQuery( getClass(), args );
   }


   private void assertNoInventoryFound() {
      assertEquals( 0, iQuerySet.getRowCount() );
   }


   private void assertInventoryDataRetrievedCorrectly( boolean aIsKitSbucomponent,
         boolean aIsAssySubcomponent ) {

      // assert that one row is returned
      assertEquals( 1, iQuerySet.getRowCount() );

      iQuerySet.next();

      // assert that inventory data is correct
      assertEquals( iInventory, iQuerySet.getKey( InventoryKey.class, "inv_key" ) );
      assertEquals( SERIAL_NO_OEM, iQuerySet.getString( "serial_no_oem" ) );
      assertEquals( INV_BARCODE, iQuerySet.getString( "barcode_sdesc" ) );
      assertEquals( RFI, iQuerySet.getString( "inv_cond_cd" ) );

      assertEquals( iLocation, iQuerySet.getKey( LocationKey.class, "loc_key" ) );
      assertEquals( LOCATION_CODE, iQuerySet.getString( "loc_cd" ) );

      assertEquals( iPart, iQuerySet.getKey( PartNoKey.class, "part_no_key" ) );
      assertEquals( PART_NO_OEM, iQuerySet.getString( "part_no_oem" ) );
      assertEquals( PART_NO_SDESC, iQuerySet.getString( "part_no_sdesc" ) );
      assertEquals( SER, iQuerySet.getString( "inv_class_cd" ) );
      assertEquals( EA, iQuerySet.getString( "qty_unit_cd" ) );
      assertEquals( MANUFACTURER_CODE, iQuerySet.getString( "manufact_cd" ) );

      assertEquals( aIsKitSbucomponent, iQuerySet.getBoolean( "is_kit_subcomponent" ) );
      if ( aIsKitSbucomponent ) {
         assertEquals( KIT_SERIAL_NO_OEM, iQuerySet.getString( "kit_serial_no_oem" ) );
      }
      assertEquals( aIsAssySubcomponent, iQuerySet.getBoolean( "is_assy_subcomponent" ) );
   }

}
