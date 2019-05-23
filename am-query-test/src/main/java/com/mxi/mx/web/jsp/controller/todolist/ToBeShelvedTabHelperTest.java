
package com.mxi.mx.web.jsp.controller.todolist;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.TransferKey;


/**
 * This class tests the {@link ToBeShelvedTabHelper} class.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class ToBeShelvedTabHelperTest {

   @ClassRule
   public static final DatabaseConnectionRule iDatabaseConnectionRule =
         new DatabaseConnectionRule();


   @BeforeClass
   public static void setUp() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), ToBeShelvedTabHelperTest.class );
   }


   /** Human resource for test data for test case 1. */
   private static final HumanResourceKey HR1 = new HumanResourceKey( 1, 2 );

   /** Human resource for test data for test case 2. */
   private static final HumanResourceKey HR2 = new HumanResourceKey( 2, 2 );

   /** Human resource for test data for test case 3. */
   private static final HumanResourceKey HR3 = new HumanResourceKey( 3, 2 );

   /** Human resource for test data for test case 4. */
   private static final HumanResourceKey HR4 = new HumanResourceKey( 4, 2 );

   /** Human resource for test data for test case 5. */
   private static final HumanResourceKey HR5 = new HumanResourceKey( 5, 2 );

   /** Human resource for test data for test case 6. */
   private static final HumanResourceKey HR6 = new HumanResourceKey( 6, 2 );

   /** Human resource for test data for test case 7. */
   private static final HumanResourceKey HR7 = new HumanResourceKey( 7, 2 );


   /**
    * Tests with data that is expected to return one row with no bin with auto put aways on.
    */
   @Test
   public void testOneInvNoBinAutoPutAways() {
      DataSet lResult = new ToBeShelvedTabHelper( HR2, true ).getToBeShelvedDataSet();

      Assert.assertEquals( "One row returned", 1, lResult.getRowCount() );

      lResult.first();

      assertInventory( lResult, new InventoryKey( 2, 4 ), "INV_DESC_2", "INV_SERIAL_NO_2",
            new PartNoKey( 2, 5 ), "PART_NO_2", "PART_DESC_2", "EA", 1.0, new LocationKey( 2, 3 ),
            "DOCK_2" );

      // there is a put away in the data but it should not be included
      assertNoPutAway( lResult );

      // no bin location for the inventory's part
      assertNoBinLocation( lResult );
   }


   /**
    * Tests with data that is expected to return two rows with the same inventory and no bin with
    * two put aways.
    */
   @Test
   public void testOneInvNoBinTwoManualPutAways() {
      DataSet lResult = new ToBeShelvedTabHelper( HR2, false ).getToBeShelvedDataSet();

      Assert.assertEquals( "Two rows returned", 2, lResult.getRowCount() );

      lResult.next();

      assertInventory( lResult, new InventoryKey( 2, 4 ), "INV_DESC_2", "INV_SERIAL_NO_2",
            new PartNoKey( 2, 5 ), "PART_NO_2", "PART_DESC_2", "EA", 1.0, new LocationKey( 2, 3 ),
            "DOCK_2" );

      assertPutAway( lResult, new TransferKey( 2, 10 ), "XFER_2.1", 1.0, new LocationKey( 2, 6 ),
            "BIN_2" );

      assertNoBinLocation( lResult );

      lResult.next();

      // the same inventory should appear because there are two put aways
      assertInventory( lResult, new InventoryKey( 2, 4 ), "INV_DESC_2", "INV_SERIAL_NO_2",
            new PartNoKey( 2, 5 ), "PART_NO_2", "PART_DESC_2", "EA", 1.0, new LocationKey( 2, 3 ),
            "DOCK_2" );

      assertPutAway( lResult, new TransferKey( 2, 11 ), "XFER_2.2", 1.0, new LocationKey( 2, 6 ),
            "BIN_2" );

      assertNoBinLocation( lResult );
   }


   /**
    * Tests with data that is expected to return one row with a single bin with auto put aways
    * turned on.
    */
   @Test
   public void testOneInvOneBinAutoPutAways() {
      DataSet lResult = new ToBeShelvedTabHelper( HR1, true ).getToBeShelvedDataSet();

      Assert.assertEquals( "One row returned", 1, lResult.getRowCount() );

      lResult.first();

      assertInventory( lResult, new InventoryKey( 1, 4 ), "INV_DESC_1", "INV_SERIAL_NO_1",
            new PartNoKey( 1, 5 ), "PART_NO_1", "PART_DESC_1", "EA", 1.0, new LocationKey( 1, 3 ),
            "DOCK_1" );

      assertNoPutAway( lResult );

      assertBinLocation( lResult, new LocationKey( 1, 6 ), "BIN_1.1", 0.0, null, null );
   }


   /**
    * Tests with data that is expected to return one row with a single bin with one put away.
    */
   @Test
   public void testOneInvOneBinOneManualPutAway() {
      DataSet lResult = new ToBeShelvedTabHelper( HR1, false ).getToBeShelvedDataSet();

      Assert.assertEquals( "One row returned", 1, lResult.getRowCount() );

      lResult.first();

      assertInventory( lResult, new InventoryKey( 1, 4 ), "INV_DESC_1", "INV_SERIAL_NO_1",
            new PartNoKey( 1, 5 ), "PART_NO_1", "PART_DESC_1", "EA", 1.0, new LocationKey( 1, 3 ),
            "DOCK_1" );

      assertPutAway( lResult, new TransferKey( 1, 10 ), "XFER_1", 1.0, new LocationKey( 1, 6 ),
            "BIN_1.1" );

      assertBinLocation( lResult, new LocationKey( 1, 6 ), "BIN_1.1", 0.0, null, null );
   }


   /**
    * Tests with data that is expected to return two rows with two bins with auto put aways turned
    * on.
    */
   @Test
   public void testOneInvTwoBinsAutoPutAways() {
      DataSet lResult = new ToBeShelvedTabHelper( HR3, true ).getToBeShelvedDataSet();

      Assert.assertEquals( "Two rows returned", 2, lResult.getRowCount() );

      lResult.next();

      assertInventory( lResult, new InventoryKey( 3, 4 ), "INV_DESC_3", "INV_SERIAL_NO_3",
            new PartNoKey( 3, 5 ), "PART_NO_3", "PART_DESC_3", "EA", 1.0, new LocationKey( 3, 3 ),
            "DOCK_3" );

      assertNoPutAway( lResult );

      // there are two inventory already put away in this bin
      assertBinLocation( lResult, new LocationKey( 3, 6 ), "BIN_3.1", 2.0, 1.0, 2.0 );

      lResult.next();

      assertInventory( lResult, new InventoryKey( 3, 4 ), "INV_DESC_3", "INV_SERIAL_NO_3",
            new PartNoKey( 3, 5 ), "PART_NO_3", "PART_DESC_3", "EA", 1.0, new LocationKey( 3, 3 ),
            "DOCK_3" );

      assertNoPutAway( lResult );

      assertBinLocation( lResult, new LocationKey( 3, 7 ), "BIN_3.2", 0.0, 3.0, 4.0 );
   }


   /**
    * Tests with data that is expected to return two rows with a two bins with only one put away.
    */
   @Test
   public void testOneInvTwoBinsOneManualPutAway() {
      DataSet lResult = new ToBeShelvedTabHelper( HR3, false ).getToBeShelvedDataSet();

      Assert.assertEquals( "Two rows returned", 2, lResult.getRowCount() );

      lResult.next();

      assertInventory( lResult, new InventoryKey( 3, 4 ), "INV_DESC_3", "INV_SERIAL_NO_3",
            new PartNoKey( 3, 5 ), "PART_NO_3", "PART_DESC_3", "EA", 1.0, new LocationKey( 3, 3 ),
            "DOCK_3" );

      assertPutAway( lResult, new TransferKey( 3, 10 ), "XFER_3", 1.0, new LocationKey( 3, 6 ),
            "BIN_3.1" );

      // there are two inventory already put away
      assertBinLocation( lResult, new LocationKey( 3, 6 ), "BIN_3.1", 2.0, 1.0, 2.0 );

      lResult.next();

      assertInventory( lResult, new InventoryKey( 3, 4 ), "INV_DESC_3", "INV_SERIAL_NO_3",
            new PartNoKey( 3, 5 ), "PART_NO_3", "PART_DESC_3", "EA", 1.0, new LocationKey( 3, 3 ),
            "DOCK_3" );

      // the second row should not have a put away because there is only one
      assertNoPutAway( lResult );

      assertBinLocation( lResult, new LocationKey( 3, 7 ), "BIN_3.2", 0.0, 3.0, 4.0 );
   }


   /**
    * Tests with data that is expected to return two rows with a two bins with two put aways.
    */
   @Test
   public void testOneInvTwoBinsTwoManualPutAways() {
      DataSet lResult = new ToBeShelvedTabHelper( HR5, false ).getToBeShelvedDataSet();

      Assert.assertEquals( "Two rows returned", 2, lResult.getRowCount() );

      lResult.next();

      assertInventory( lResult, new InventoryKey( 5, 4 ), "INV_DESC_5", "INV_SERIAL_NO_5",
            new PartNoKey( 5, 5 ), "PART_NO_5", "PART_DESC_5", "EA", 1.0, new LocationKey( 5, 3 ),
            "DOCK_5" );

      assertPutAway( lResult, new TransferKey( 5, 10 ), "XFER_5.1", 1.0, new LocationKey( 5, 6 ),
            "BIN_5.1" );

      // there is one inventory already put away
      assertBinLocation( lResult, new LocationKey( 5, 6 ), "BIN_5.1", 1.0, 1.0, 2.0 );

      lResult.next();

      assertInventory( lResult, new InventoryKey( 5, 4 ), "INV_DESC_5", "INV_SERIAL_NO_5",
            new PartNoKey( 5, 5 ), "PART_NO_5", "PART_DESC_5", "EA", 1.0, new LocationKey( 5, 3 ),
            "DOCK_5" );

      assertPutAway( lResult, new TransferKey( 5, 11 ), "XFER_5.2", 1.0, new LocationKey( 5, 7 ),
            "BIN_5.2" );

      assertBinLocation( lResult, new LocationKey( 5, 7 ), "BIN_5.2", 1.0, 3.0, 4.0 );
   }


   /**
    * Tests with data that returns two rows for two inventory each with a bin and with auto put
    * aways turned on.
    */
   @Test
   public void testTwoInvTwoBinsAutoPutAways() {
      DataSet lResult = new ToBeShelvedTabHelper( HR4, true ).getToBeShelvedDataSet();

      Assert.assertEquals( "Two rows returned", 2, lResult.getRowCount() );

      lResult.next();

      assertInventory( lResult, new InventoryKey( 4, 4 ), "INV_DESC_4.1", "INV_SERIAL_NO_4.1",
            new PartNoKey( 4, 5 ), "PART_NO_4.1", "PART_DESC_4.1", "EA", 1.0,
            new LocationKey( 4, 3 ), "DOCK_4" );

      assertNoPutAway( lResult );

      assertBinLocation( lResult, new LocationKey( 4, 6 ), "BIN_4.1", 1.0, 1.0, 2.0 );

      lResult.next();

      assertInventory( lResult, new InventoryKey( 4, 13 ), "INV_DESC_4.2", "INV_SERIAL_NO_4.2",
            new PartNoKey( 4, 12 ), "PART_NO_4.2", "PART_DESC_4.2", "EA", 1.0,
            new LocationKey( 4, 3 ), "DOCK_4" );

      assertNoPutAway( lResult );

      assertBinLocation( lResult, new LocationKey( 4, 7 ), "BIN_4.2", 1.0, 3.0, 4.0 );
   }


   /**
    * Tests with data that returns two rows for two inventory each with a bin and a put away.
    */
   @Test
   public void testTwoInvTwoBinsTwoManualPutAways() {
      DataSet lResult = new ToBeShelvedTabHelper( HR4, false ).getToBeShelvedDataSet();

      Assert.assertEquals( "Two rows returned", 2, lResult.getRowCount() );

      lResult.next();

      assertInventory( lResult, new InventoryKey( 4, 4 ), "INV_DESC_4.1", "INV_SERIAL_NO_4.1",
            new PartNoKey( 4, 5 ), "PART_NO_4.1", "PART_DESC_4.1", "EA", 1.0,
            new LocationKey( 4, 3 ), "DOCK_4" );

      assertPutAway( lResult, new TransferKey( 4, 10 ), "XFER_4.1", 1.0, new LocationKey( 4, 6 ),
            "BIN_4.1" );

      assertBinLocation( lResult, new LocationKey( 4, 6 ), "BIN_4.1", 1.0, 1.0, 2.0 );

      lResult.next();

      assertInventory( lResult, new InventoryKey( 4, 13 ), "INV_DESC_4.2", "INV_SERIAL_NO_4.2",
            new PartNoKey( 4, 12 ), "PART_NO_4.2", "PART_DESC_4.2", "EA", 1.0,
            new LocationKey( 4, 3 ), "DOCK_4" );

      assertPutAway( lResult, new TransferKey( 4, 11 ), "XFER_4.2", 1.0, new LocationKey( 4, 7 ),
            "BIN_4.2" );

      assertBinLocation( lResult, new LocationKey( 4, 7 ), "BIN_4.2", 1.0, 3.0, 4.0 );
   }


   /**
    * Tests with data that is expected to return two rows with a single bin with auto put always
    * turned on. one TRK and one ASSY inventories are in RFI condition that will be returned one TRK
    * and one ASSY inventories are in RFB condition that will NOT be returned
    */
   @Test
   public void testTwoTrkAndAssyInvAutoPutAways() {
      DataSet lResult = new ToBeShelvedTabHelper( HR6, true ).getToBeShelvedDataSet();

      Assert.assertEquals( "Two rows returned", 2, lResult.getRowCount() );

      // for first row of TRK inventory in RFI condition
      lResult.first();

      assertInventory( lResult, new InventoryKey( 6, 61 ), "INV_DESC_61", "INV_SERIAL_NO_61",
            new PartNoKey( 6, 61 ), "PART_NO_61", "PART_DESC_61", "EA", 1.0,
            new LocationKey( 6, 3 ), "DOCK_6" );

      // for second row of ASSY inventory in RFI condition
      lResult.next();

      assertInventory( lResult, new InventoryKey( 6, 62 ), "INV_DESC_62", "INV_SERIAL_NO_62",
            new PartNoKey( 6, 62 ), "PART_NO_62", "PART_DESC_62", "EA", 1.0,
            new LocationKey( 6, 3 ), "DOCK_6" );
   }


   /**
    * Tests with data that is expected to return two rows with a single bin with one put away. one
    * TRK and one ASSY inventories are in RFI condition that will be returned one TRK and one ASSY
    * inventories are in RFB condition that will NOT be returned
    */
   @Test
   public void testTwoTrkAndAssyInvManualPutAway() {
      DataSet lResult = new ToBeShelvedTabHelper( HR6, false ).getToBeShelvedDataSet();

      Assert.assertEquals( "Two rows returned", 2, lResult.getRowCount() );

      // for first row of TRK inventory in RFI condition
      lResult.first();

      assertInventory( lResult, new InventoryKey( 6, 61 ), "INV_DESC_61", "INV_SERIAL_NO_61",
            new PartNoKey( 6, 61 ), "PART_NO_61", "PART_DESC_61", "EA", 1.0,
            new LocationKey( 6, 3 ), "DOCK_6" );

      // for second row of ASSY inventory in RFI condition
      lResult.next();

      assertInventory( lResult, new InventoryKey( 6, 62 ), "INV_DESC_62", "INV_SERIAL_NO_62",
            new PartNoKey( 6, 62 ), "PART_NO_62", "PART_DESC_62", "EA", 1.0,
            new LocationKey( 6, 3 ), "DOCK_6" );
   }


   /**
    * Tests with data that is expected to return four rows with two bins with auto put always turned
    * on. one SER inventory with complete_bool TRUE, and one SER inventory with complete_bool FALSE
    * one BATCH inventory with complete_bool TRUE, and one BATCH inventory with complete_bool FALSE
    *
    * ALL the inventories will be retrieved regardless of complete_bool status
    */
   @Test
   public void testFourInvSerAndBatchAutoPutAways() {
      DataSet lResult = new ToBeShelvedTabHelper( HR7, true ).getToBeShelvedDataSet();

      Assert.assertEquals( "Four rows returned", 4, lResult.getRowCount() );

      // for first row
      lResult.next();

      assertInventory( lResult, new InventoryKey( 7, 71 ), "INV_DESC_71", "INV_SERIAL_NO_71",
            new PartNoKey( 7, 71 ), "PART_NO_71", "PART_DESC_71", "EA", 1.0,
            new LocationKey( 7, 3 ), "DOCK_7" );

      // for second row
      lResult.next();

      assertInventory( lResult, new InventoryKey( 7, 72 ), "INV_DESC_72", "INV_SERIAL_NO_72",
            new PartNoKey( 7, 72 ), "PART_NO_72", "PART_DESC_72", "EA", 1.0,
            new LocationKey( 7, 3 ), "DOCK_7" );

      // for third row
      lResult.next();

      assertInventory( lResult, new InventoryKey( 7, 73 ), "INV_DESC_73", "INV_SERIAL_NO_73",
            new PartNoKey( 7, 71 ), "PART_NO_71", "PART_DESC_71", "EA", 1.0,
            new LocationKey( 7, 3 ), "DOCK_7" );

      // for forth row
      lResult.next();

      assertInventory( lResult, new InventoryKey( 7, 74 ), "INV_DESC_74", "INV_SERIAL_NO_74",
            new PartNoKey( 7, 72 ), "PART_NO_72", "PART_DESC_72", "EA", 1.0,
            new LocationKey( 7, 3 ), "DOCK_7" );

   }


   /**
    * Tests with data that is expected to return four rows with a two bins with only one put away.
    * one SER inventory with complete_bool TRUE, and one SER inventory with complete_bool FALSE one
    * BATCH inventory with complete_bool TRUE, and one BATCH inventory with complete_bool FALSE
    *
    * ALL the inventories will be retrieved regardless of complete_bool status
    */
   @Test
   public void testFourSerAndBatchInvOneManualPutAway() {
      DataSet lResult = new ToBeShelvedTabHelper( HR7, false ).getToBeShelvedDataSet();

      Assert.assertEquals( "Four rows returned", 4, lResult.getRowCount() );

      // for first row
      lResult.next();

      assertInventory( lResult, new InventoryKey( 7, 71 ), "INV_DESC_71", "INV_SERIAL_NO_71",
            new PartNoKey( 7, 71 ), "PART_NO_71", "PART_DESC_71", "EA", 1.0,
            new LocationKey( 7, 3 ), "DOCK_7" );

      // for second row
      lResult.next();

      assertInventory( lResult, new InventoryKey( 7, 72 ), "INV_DESC_72", "INV_SERIAL_NO_72",
            new PartNoKey( 7, 72 ), "PART_NO_72", "PART_DESC_72", "EA", 1.0,
            new LocationKey( 7, 3 ), "DOCK_7" );

      // for the third row
      lResult.next();

      assertInventory( lResult, new InventoryKey( 7, 73 ), "INV_DESC_73", "INV_SERIAL_NO_73",
            new PartNoKey( 7, 71 ), "PART_NO_71", "PART_DESC_71", "EA", 1.0,
            new LocationKey( 7, 3 ), "DOCK_7" );

      // for the forth row
      lResult.next();

      assertInventory( lResult, new InventoryKey( 7, 74 ), "INV_DESC_74", "INV_SERIAL_NO_74",
            new PartNoKey( 7, 72 ), "PART_NO_72", "PART_DESC_72", "EA", 1.0,
            new LocationKey( 7, 3 ), "DOCK_7" );

   }


   /**
    * Validates the give dataset has the expected bin location.
    *
    * @param aResult
    *           The dataset
    * @param aBinLocation
    *           The location key
    * @param aBinLocCd
    *           The location code
    * @param aBinQty
    *           The current quantity in the bin
    * @param aMinQty
    *           The specified minimum quantity
    * @param aMaxQty
    *           The specified maximum quantity
    */
   private void assertBinLocation( DataSet aResult, LocationKey aBinLocation, String aBinLocCd,
         double aBinQty, Double aMinQty, Double aMaxQty ) {
      Assert.assertEquals( "bin_location_key", aBinLocation,
            new LocationKey( aResult.getString( "bin_location_key" ) ) );
      Assert.assertEquals( "bin_loc_cd", aBinLocCd, aResult.getString( "bin_loc_cd" ) );
      Assert.assertEquals( "bin_current_qt", aBinQty, aResult.getDouble( "bin_current_qt" ),
            0.001 );

      if ( aMinQty == null ) {
         Assert.assertTrue( "bin_min_qt", aResult.isNull( "bin_min_qt" ) );
      } else {
         Assert.assertEquals( "bin_min_qt", aMinQty, aResult.getDouble( "bin_min_qt" ), 0.001 );
      }

      if ( aMaxQty == null ) {
         Assert.assertTrue( "bin_max_qt", aResult.isNull( "bin_max_qt" ) );
      } else {
         Assert.assertEquals( "bin_max_qt", aMaxQty, aResult.getDouble( "bin_max_qt" ), 0.001 );
      }
   }


   /**
    * Validates the given dataset has the expected inventory details.
    *
    * @param aResult
    *           The dataset
    * @param aInventory
    *           The inventory key
    * @param aInvDescription
    *           The inventory description
    * @param aInvSerialNo
    *           The inventory serial number
    * @param aPartNo
    *           The part key
    * @param aPartNoOem
    *           The part number
    * @param aPartDescription
    *           The part description
    * @param aQtyUnitCd
    *           The quantity unit code
    * @param aInvQty
    *           The quantity of inventory
    * @param aInvLocation
    *           The inventory's location
    * @param aInvLocationCd
    *           The inventory's location code
    */
   private void assertInventory( DataSet aResult, InventoryKey aInventory, String aInvDescription,
         String aInvSerialNo, PartNoKey aPartNo, String aPartNoOem, String aPartDescription,
         String aQtyUnitCd, double aInvQty, LocationKey aInvLocation, String aInvLocationCd ) {
      Assert.assertEquals( "inventory_key", aInventory,
            new InventoryKey( aResult.getString( "inventory_key" ) ) );
      Assert.assertEquals( "inv_no_sdesc", aInvDescription, aResult.getString( "inv_no_sdesc" ) );
      Assert.assertEquals( "serial_no_oem", aInvSerialNo, aResult.getString( "serial_no_oem" ) );
      Assert.assertEquals( "part_no_key", aPartNo,
            new PartNoKey( aResult.getString( "part_no_key" ) ) );
      Assert.assertEquals( "part_no_oem", aPartNoOem, aResult.getString( "part_no_oem" ) );
      Assert.assertEquals( "part_no_sdesc", aPartDescription,
            aResult.getString( "part_no_sdesc" ) );
      Assert.assertEquals( "qty_unit_cd", aQtyUnitCd, aResult.getString( "qty_unit_cd" ) );
      Assert.assertEquals( "inventory_qt", aInvQty, aResult.getDouble( "inventory_qt" ), 0.001 );
      Assert.assertEquals( "location_key", aInvLocation,
            new LocationKey( aResult.getString( "location_key" ) ) );
      Assert.assertEquals( "loc_cd", aInvLocationCd, aResult.getString( "loc_cd" ) );
   }


   /**
    * Asserts that there is no bin location in the given dataset.
    *
    * @param aResult
    *           The dataset
    */
   private void assertNoBinLocation( DataSet aResult ) {
      Assert.assertTrue( "bin_location_key", aResult.isNull( "bin_location_key" ) );
      Assert.assertEquals( "bin_current_qt", 0.0, aResult.getDouble( "bin_current_qt" ), 0.001 );
      Assert.assertTrue( "bin_min_qt", aResult.isNull( "bin_min_qt" ) );
      Assert.assertTrue( "bin_max_qt", aResult.isNull( "bin_max_qt" ) );
   }


   /**
    * Asserts that there is no put away in the given dataset.
    *
    * @param aResult
    *           The dataset
    */
   private void assertNoPutAway( DataSet aResult ) {
      Assert.assertTrue( "putaway_xfer_key", aResult.isNull( "putaway_xfer_key" ) );
      Assert.assertTrue( "putaway_xfer_id", aResult.isNull( "putaway_xfer_id" ) );
      Assert.assertTrue( "putaway_xfer_qt", aResult.isNull( "putaway_xfer_qt" ) );
      Assert.assertTrue( "putaway_location_key", aResult.isNull( "putaway_location_key" ) );
      Assert.assertTrue( "putaway_loc_cd", aResult.isNull( "putaway_loc_cd" ) );
   }


   /**
    * Verifies that the given dataset has the expected put away.
    *
    * @param aResult
    *           The dataset
    * @param aTransfer
    *           The put away key
    * @param aTranferBarcode
    *           The put away barcode
    * @param aTranferQty
    *           The put away quantity
    * @param aBinLocation
    *           The put away location
    * @param aBinLocationCd
    *           The put away location code
    */
   private void assertPutAway( DataSet aResult, TransferKey aTransfer, String aTranferBarcode,
         double aTranferQty, LocationKey aBinLocation, String aBinLocationCd ) {
      Assert.assertEquals( "putaway_xfer_key", aTransfer,
            new TransferKey( aResult.getString( "putaway_xfer_key" ) ) );
      Assert.assertEquals( "putaway_xfer_id", aTranferBarcode,
            aResult.getString( "putaway_xfer_id" ) );
      Assert.assertEquals( "putaway_xfer_qt", aTranferQty, aResult.getDouble( "putaway_xfer_qt" ),
            0.001 );
      Assert.assertEquals( "putaway_location_key", aBinLocation,
            new LocationKey( aResult.getString( "putaway_location_key" ) ) );
      Assert.assertEquals( "putaway_loc_cd", aBinLocationCd,
            aResult.getString( "putaway_loc_cd" ) );
   }

}
