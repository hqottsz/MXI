package com.mxi.mx.core.query.inventorycount;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.am.ee.OperateAsUserRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.UserKey;
import com.mxi.mx.core.services.part.binlevel.BinLevelService;
import com.mxi.mx.core.services.part.binlevel.BinLevelTO;
import com.mxi.mx.core.table.eqp.EqpPartNoTable;
import com.mxi.mx.core.table.inv.InvInvTable;


/**
 * This test class ensures that GetInventoryCountByBin.qrx query returns correct data.
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class GetInventoryCountByBinTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public OperateAsUserRule operateAsUser = new OperateAsUserRule( 99999, "currentuser" );

   private final static String PART_NO_OEM = "PART_NO_OEM";
   private final static String BIN_LEVEL_PART_NO_OEM = "BIN_LEVEL_PART_NO_OEM";
   private final static String KIT_PART_NO_OEM = "KIT_PART_NO_OEM";
   private final static String ASSY_PART_NO_OEM = "ASSY_PART_NO_OEM";
   private final static String SERIAL_NO_OEM = "SERIAL_NO_OEM";

   private final static String LOCATION_CODE = "LOCATION_CODE";

   private LocationKey iBinLocation;
   private PartNoKey iBinLevelPart;
   private PartNoKey iPart;
   private InventoryKey iInventory;

   private PartNoKey iKitPart;
   private InventoryKey iKitInventory;

   private PartNoKey iEnginePart;
   private InventoryKey iEngine;

   private QuerySet iQuerySet;


   @Before
   public void setup() {
      iBinLocation = createBinWithInventory( LOCATION_CODE );

      Domain.createHumanResource( hr -> {
         hr.setUser( new UserKey( 99999 ) );
      } );
   }


   @Test
   public void test_GIVEN_InvalidBinCode_WHEN_ExecuteQuery_THEN_NoDataReturned() {

      // execute query against a non-matching bin
      executeQuery( new LocationKey( 100, 100 ) );

      // no row returned
      assertNoDataReturned();
   }


   @Test
   public void test_GIVEN_BinLoc_WHEN_ExecuteQuery_THEN_OneRowReturned() {

      removePartAndInv();

      // execute query
      executeQuery( iBinLocation );

      // expect one row with bin location
      List<ExpectedRow> expectedRows = new ArrayList<ExpectedRow>();
      expectedRows.add( new ExpectedRow( iBinLocation, null, null ) );

      assertRightDataAreReturned( expectedRows );
   }


   @Test
   public void test_GIVEN_binLocWithBinLevel_WHEN_ExecuteQuery_THEN_OneRowReturned()
         throws MxException {

      removePartAndInv();

      // create a bin level at iBinLocation
      createPartAndBinLevel();

      // execute query
      executeQuery( iBinLocation );

      // expect one row with bin location, part no
      List<ExpectedRow> expectedRows = new ArrayList<ExpectedRow>();
      expectedRows.add( new ExpectedRow( iBinLocation, iBinLevelPart, null ) );

      assertRightDataAreReturned( expectedRows );
   }


   @Test
   public void test_GIVEN_binLocWithInv_WHEN_ExecuteQuery_THEN_TwoRecordsReturned() {

      // execute query
      executeQuery( iBinLocation );

      // expect two rows: one with bin location, part no, and inventory while another with bin
      // location
      List<ExpectedRow> expectedRows = new ArrayList<ExpectedRow>();
      expectedRows.add( new ExpectedRow( iBinLocation, null, null ) );
      expectedRows.add( new ExpectedRow( iBinLocation, iPart, iInventory ) );

      assertRightDataAreReturned( expectedRows );
   }


   @Test
   public void test_GIVEN_binLocWithBinLevelAndInv_WHEN_ExecuteQuery_THEN_TwoRecordsReturned()
         throws MxException {

      // create bin level and inventory at iBinLocation
      createPartAndBinLevel();

      // execute query
      executeQuery( iBinLocation );

      // expect two rows: one with bin location, part no while another with bin
      // location, part no, and inventory
      List<ExpectedRow> expectedRows = new ArrayList<ExpectedRow>();
      expectedRows.add( new ExpectedRow( iBinLocation, iBinLevelPart, null ) );
      expectedRows.add( new ExpectedRow( iBinLocation, iPart, iInventory ) );

      assertRightDataAreReturned( expectedRows );
   }


   @Test
   public void test_GIVEN_binLocWithKitInv_WHEN_ExecuteQuery_THEN_TwoRecordReturned() {

      // create two inventory: kit inventory and kit content inventory
      createKitInventory( iBinLocation );

      // execute query
      executeQuery( iBinLocation );

      // expect that row with kit content inventory will be excluded
      List<ExpectedRow> expectedRows = new ArrayList<ExpectedRow>();
      expectedRows.add( new ExpectedRow( iBinLocation, null, null ) );
      expectedRows.add( new ExpectedRow( iBinLocation, iKitPart, iKitInventory ) );

      assertRightDataAreReturned( expectedRows );
   }


   @Test
   public void
         test_GIVEN_binLocWithBinLevelForKitContentInv_WHEN_ExecuteQuery_THEN_TwoRecordReturned()
               throws MxException {

      // add bin level with part for kit content inventory
      addBinLevel( iPart );

      // create kit inventory with kit content inventory
      createKitInventory( iBinLocation );

      // execute query
      executeQuery( iBinLocation );

      // expect that row with kit content inventory will be excluded while row for bin level
      // will be retrieved
      List<ExpectedRow> expectedRows = new ArrayList<ExpectedRow>();
      expectedRows.add( new ExpectedRow( iBinLocation, iKitPart, iKitInventory ) );
      expectedRows.add( new ExpectedRow( iBinLocation, iPart, null ) );

      assertRightDataAreReturned( expectedRows );
   }


   @Test
   public void
         test_GIVEN_binLocWithKitInvAndAnotherInv_WHEN_ExecuteQuery_THEN_ThreeRecordsReturnedWithRightOrder() {

      // create two inventory: kit inventory and kit content inventory
      createKitInventory( iBinLocation );

      InventoryKey anotherInventory = Domain.createSerializedInventory( inventory -> {
         inventory.setPartNumber( iPart );
         inventory.setSerialNumber( SERIAL_NO_OEM );
         inventory.setLocation( iBinLocation );
      } );

      // execute query
      executeQuery( iBinLocation );

      // expect three rows in the right order by location, part no oem, and manufacture code
      List<ExpectedRow> expectedRows = new ArrayList<ExpectedRow>();
      expectedRows.add( new ExpectedRow( iBinLocation, null, null ) );
      expectedRows.add( new ExpectedRow( iBinLocation, iKitPart, iKitInventory ) );
      expectedRows.add( new ExpectedRow( iBinLocation, iPart, anotherInventory ) );

      assertRightDataAreReturned( expectedRows );
   }


   @Test
   public void test_GIVEN_binLocWithEgine_WHEN_ExecuteQuery_THEN_TwoRecordReturned() {

      // create two inventory: engine and it's sub component inventory
      createEngine( iBinLocation );

      // execute query
      executeQuery( iBinLocation );

      // expect that row with engine subcomponent inventory will be excluded
      List<ExpectedRow> expectedRows = new ArrayList<ExpectedRow>();
      expectedRows.add( new ExpectedRow( iBinLocation, null, null ) );
      expectedRows.add( new ExpectedRow( iBinLocation, iEnginePart, iEngine ) );

      assertRightDataAreReturned( expectedRows );
   }


   private void removePartAndInv() {
      EqpPartNoTable.findByPrimaryKey( iPart ).delete();
      InvInvTable.findByPrimaryKey( iInventory ).delete();
   }


   // create part and inventory at specified location
   private void createPartAndInventory( LocationKey aLocation ) {

      iPart = Domain.createPart( part -> {
         part.setInventoryClass( RefInvClassKey.SER );
         part.setCode( PART_NO_OEM );
         part.setManufacturer( Domain.createManufacturer() );
      } );

      iInventory = Domain.createSerializedInventory( inventory -> {
         inventory.setPartNumber( iPart );
         inventory.setSerialNumber( SERIAL_NO_OEM );
         inventory.setLocation( aLocation );
      } );
   }


   // create part and bin level for the part at iBinLocation
   private void createPartAndBinLevel() throws MxException {
      iBinLevelPart = Domain.createPart( part -> {
         part.setInventoryClass( RefInvClassKey.SER );
         part.setCode( BIN_LEVEL_PART_NO_OEM );
         part.setManufacturer( Domain.createManufacturer() );
      } );

      new BinLevelService().add( iBinLevelPart, null, iBinLocation, new BinLevelTO() );
   }


   private LocationKey createBinWithInventory( String aLocationCode ) {

      LocationKey lBinLoc = Domain.createLocation( location -> {
         location.setCode( aLocationCode );
         location.setType( RefLocTypeKey.BIN );
      } );

      createPartAndInventory( lBinLoc );

      return lBinLoc;
   }


   // create kit inventory at specified location
   private void createKitInventory( LocationKey aLocation ) {
      iKitPart = Domain.createPart( part -> {
         part.setPartNoOem( KIT_PART_NO_OEM );
         part.setManufacturer( Domain.createManufacturer() );
         part.setInventoryClass( RefInvClassKey.KIT );
      } );
      iKitInventory = Domain.createKitInventory( kit -> {
         kit.setPartNo( iKitPart );
         kit.addKitContentInventory( iInventory );
         kit.setLocation( aLocation );
      } );
   }


   // create engine ( an ASSY inventory ) at specified location
   private void createEngine( LocationKey aLocation ) {
      iEnginePart = Domain.createPart( part -> {
         part.setPartNoOem( ASSY_PART_NO_OEM );
         part.setManufacturer( Domain.createManufacturer() );
         part.setInventoryClass( RefInvClassKey.ASSY );
      } );
      iEngine = Domain.createEngine( engine -> {
         engine.setPartNumber( iEnginePart );
         engine.addSerialized( iInventory );
         engine.setLocation( aLocation );
      } );
   }


   private void addBinLevel( PartNoKey aPart ) throws MxException {
      new BinLevelService().add( aPart, null, iBinLocation, new BinLevelTO() );
   }


   /**
    * execute query GetInventoryCountByBin.qrx
    *
    * @param binKey
    */
   private void executeQuery( LocationKey binKey ) {

      DataSetArgument args = new DataSetArgument();
      args.add( binKey, "aBinLocDbId", "aBinLocId" );

      iQuerySet = QueryExecutor.executeQuery( getClass(), args );
   }


   private void assertNoDataReturned() {
      assertEquals( 0, iQuerySet.getRowCount() );
   }


   private void assertRightDataAreReturned( List<ExpectedRow> aExpectedRows ) {

      // assert that total number of rows returned is as expected
      assertEquals( aExpectedRows.size(), iQuerySet.getRowCount() );

      // assert each row is retrieved as expected
      for ( ExpectedRow expectedRow : aExpectedRows ) {
         iQuerySet.next();

         assertEquals( expectedRow.getLocation(),
               iQuerySet.getKey( LocationKey.class, "loc_key" ) );
         assertEquals( expectedRow.getPart(), iQuerySet.getKey( PartNoKey.class, "part_no_key" ) );
         assertEquals( expectedRow.getInventory(),
               iQuerySet.getKey( InventoryKey.class, "inv_key" ) );
      }

   }


   private class ExpectedRow {

      LocationKey location;
      PartNoKey part;
      InventoryKey inventory;


      public ExpectedRow(LocationKey location, PartNoKey part, InventoryKey inventory) {
         this.location = location;
         this.part = part;
         this.inventory = inventory;
      }


      /**
       * Returns the value of the location property.
       *
       * @return the value of the location property
       */
      public LocationKey getLocation() {
         return location;
      }


      /**
       * Returns the value of the part property.
       *
       * @return the value of the part property
       */
      public PartNoKey getPart() {
         return part;
      }


      /**
       * Returns the value of the inventory property.
       *
       * @return the value of the inventory property
       */
      public InventoryKey getInventory() {
         return inventory;
      }
   }
}
