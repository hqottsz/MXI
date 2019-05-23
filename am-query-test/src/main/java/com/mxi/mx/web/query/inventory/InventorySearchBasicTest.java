package com.mxi.mx.web.query.inventory;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.XmlLoader;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.ManufacturerKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * Tests the query com.mxi.mx.query.inventory.InventorySearchBasic
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class InventorySearchBasicTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   private static final int HR_DB_ID = 4650;
   private static final int HR_ID = 1;
   private static final int NUMBER_OF_ROWS = 100;
   private static final int INCLUDE_ZERO_QTY = 1;


   @Test
   public void execute_invalidOEMPartNo() throws Exception {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.addWhereLike( "eqp_part_no.part_no_oem", "INVALID" );

      // execute the query
      DataSet lInventorysDs = execute( lArgs, 100, 4650, 1, 1 );

      assertEquals( "Number of retrieved rows", 0, lInventorysDs.getRowCount() );
   }


   @Test
   public void execute_invalidManufacturer() throws Exception {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.addWhereEquals( "eqp_manufact.manufact_cd", "INVALID" );

      // execute the query
      DataSet lInventorysDs = execute( lArgs, 100, 4650, 1, 1 );

      assertEquals( "Number of retrieved rows", 0, lInventorysDs.getRowCount() );
   }


   @Test
   public void execute_invalidSerialNo() throws Exception {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.addWhereLike( "inv_inv.serial_no_oem", "INVALID" );
      lArgs.addWhereLike( "eqp_part_no.part_no_oem", "INVALID" );

      // execute the query
      DataSet lInventorysDs = execute( lArgs, 100, 4650, 1, 1 );

      assertEquals( "Number of retrieved rows", 0, lInventorysDs.getRowCount() );
   }


   @Test
   public void execute_searchByOEMPartNo() throws Exception {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.addWhereLike( "eqp_part_no.part_no_oem", "D52554-2" );
      lArgs.addWhere( "inv_inv.nh_inv_no_db_id IS NULL" );
      lArgs.addWhereNotEquals( "inv_inv.inv_cond_cd", "SCRAP" );
      lArgs.addWhereNotEquals( "inv_inv.inv_cond_cd", "ARCHIVE" );
      lArgs.addWhere( "( inv_inv.bin_qt IS NULL OR inv_inv.bin_qt > 0 )" );
      lArgs.addWhereBoolean( "ref_inv_cond.srv_bool", true );
      lArgs.add( "aReserved", "UNRESERVED" );

      // execute the query
      DataSet lInventorysDs = execute( lArgs, 100, 4650, 1, 1 );

      assertEquals( "Number of retrieved rows", 1, lInventorysDs.getRowCount() );

      lInventorysDs.next();

      assertDataWithinARow( lInventorysDs, new InventoryKey( "4650:295015" ),
            new InventoryKey( "4650:295015" ), "Rear Landing Gear Shaft - Test SER-1", null, null,
            null, null, new PartNoKey( "4300:2367" ), "D52554-2", "Rear Landing Gear Shaft",
            new ManufacturerKey( "100:DASSAULT" ), "DASSAULT", "Test SER-1", "RFI",
            new LocationKey( "4650:100069" ), "ORD/SRVSTORE/BIN-1", new OwnerKey( "4650:10" ),
            "A319/320", new ConfigSlotKey( "8000002:APU G20:24" ), "7024M", "1.1", "0", "1", null,
            null );
   }


   /**
    * Inventory with key 4650:295021 is part of the kit inventory with key 4650:295020.
    */
   @Test
   public void execute_searchValidateKitInfo() throws Exception {
      DataSetArgument lArgs = new DataSetArgument();
      {
         lArgs.addWhere( "inv_inv.inv_no_db_id = 4650" );
         lArgs.addWhere( "inv_inv.inv_no_id = 295021" );
      }
      DataSet lInventorysDs = execute( lArgs, 100, 4650, 1, 1 );

      assertEquals( "Number of retrieved rows", 1, lInventorysDs.getRowCount() );

      lInventorysDs.next();

      MxAssert.assertEquals( "kit_inventory_key", "4650:295020",
            lInventorysDs.getString( "kit_inventory_key" ) );
      MxAssert.assertEquals( "kit_inventory", "Test Kit",
            lInventorysDs.getString( "kit_inventory" ) );
   }


   @Test
   public void execute_searchByOEMPartNoManufacturer() throws Exception {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.addWhereLike( "eqp_part_no.part_no_oem", "DUPLICATE-PN" );
      lArgs.addWhereEquals( "eqp_manufact.manufact_cd", "BOEING" );
      lArgs.addWhere( "inv_inv.nh_inv_no_db_id IS NULL" );
      lArgs.addWhereNotEquals( "inv_inv.inv_cond_cd", "SCRAP" );
      lArgs.addWhereNotEquals( "inv_inv.inv_cond_cd", "ARCHIVE" );
      lArgs.addWhere( "( inv_inv.bin_qt IS NULL OR inv_inv.bin_qt > 0 )" );
      lArgs.addWhereBoolean( "ref_inv_cond.srv_bool", true );
      lArgs.add( "aReserved", "UNRESERVED" );

      // execute the query
      DataSet lInventorysDs = execute( lArgs, 100, 4650, 1, 1 );

      assertEquals( "Number of retrieved rows", 1, lInventorysDs.getRowCount() );

      lInventorysDs.next();

      assertDataWithinARow( lInventorysDs, new InventoryKey( "4650:295017" ),
            new InventoryKey( "4650:295015" ), "Rear Landing Gear Shaft - Test SER-1", null, null,
            null, null, new PartNoKey( "4300:2500" ), "DUPLICATE-PN", "Rear Landing Gear Shaft",
            new ManufacturerKey( "200:BOEING" ), "BOEING", "Test SER-1", "RFI",
            new LocationKey( "4650:100069" ), "ORD/SRVSTORE/BIN-1", new OwnerKey( "4650:10" ),
            "A319/320", new ConfigSlotKey( "8000002:APU G20:24" ), "7024M", "1.1", "0", "1", null,
            null );
   }


   @Test
   public void execute_searchBySerialNo() throws Exception {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.addWhereLike( "inv_inv.serial_no_oem", "Test SER-1" );
      lArgs.addWhereLike( "eqp_part_no.part_no_oem", "D52554-2" );

      // execute the query
      DataSet lInventorysDs = execute( lArgs, 100, 4650, 1, 1 );

      assertEquals( "Number of retrieved rows", 1, lInventorysDs.getRowCount() );

      lInventorysDs.next();

      assertDataWithinARow( lInventorysDs, new InventoryKey( "4650:295015" ),
            new InventoryKey( "4650:295015" ), "Rear Landing Gear Shaft - Test SER-1", null, null,
            null, null, new PartNoKey( "4300:2367" ), "D52554-2", "Rear Landing Gear Shaft",
            new ManufacturerKey( "100:DASSAULT" ), "DASSAULT", "Test SER-1", "RFI",
            new LocationKey( "4650:100069" ), "ORD/SRVSTORE/BIN-1", new OwnerKey( "4650:10" ),
            "A319/320", new ConfigSlotKey( "8000002:APU G20:24" ), "7024M", "1.1", "0", "1", null,
            null );
   }


   @Test
   public void execute_searchBySerialNo_withPartName() throws Exception {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.addWhereLike( "inv_inv.serial_no_oem", "SN-2ABC" );
      lArgs.addWhereLike( "eqp_part_no.part_no_sdesc", "Part ABC for testing" );

      // execute the query
      DataSet lResult = execute( lArgs, NUMBER_OF_ROWS, HR_DB_ID, HR_ID, INCLUDE_ZERO_QTY );

      assertEquals( "Number of retrieved rows", 1, lResult.getRowCount() );
   }


   @Test
   public void execute_unauthorizedUser() throws Exception {

      DataSetArgument lArgs = new DataSetArgument();

      // execute the query
      DataSet lInventorysDs = execute( lArgs, 100, 4650, 1000000, 1 );

      assertEquals( "Number of retrieved rows", 0, lInventorysDs.getRowCount() );
   }


   @Test
   public void execute_searchByOwner_withPartName() throws Exception {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.addWhereLike( "inv_owner.owner_cd", "A319/320" );
      lArgs.addWhereLike( "eqp_part_no.part_no_sdesc", "Part ABC for testing" );

      DataSet lResult = execute( lArgs, NUMBER_OF_ROWS, HR_DB_ID, HR_ID, INCLUDE_ZERO_QTY );

      assertEquals( "Number of retrieved rows: ", 1, lResult.getRowCount() );
   }


   @Test
   public void execute_searchByPartNumber_withPartName() throws Exception {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.addWhereLike( "eqp_part_no.part_no_oem", "PN-TEST-ABC" );
      lArgs.addWhereLike( "eqp_part_no.part_no_sdesc", "Part ABC for testing" );

      DataSet lResult = execute( lArgs, NUMBER_OF_ROWS, HR_DB_ID, HR_ID, INCLUDE_ZERO_QTY );

      assertEquals( "Number of retrieved rows: ", 1, lResult.getRowCount() );
   }


   @BeforeClass
   public static void loadData() throws Exception {
      XmlLoader.load( sDatabaseConnectionRule.getConnection(), InventorySearchBasicTest.class,
            InventorySearchByTypeData.getDataFile() );
   }


   private DataSet execute( DataSetArgument aArgs, Integer aRowNum, Integer aHrDbId, Integer aHrId,
         Integer aIncludeZeroQty ) {

      // Build query arguments
      aArgs.add( "aRowNum", aRowNum );
      aArgs.add( "aHrDbId", aHrDbId );
      aArgs.add( "aHrId", aHrId );
      aArgs.add( "aIncludeZeroQty", aIncludeZeroQty );

      // Execute the query
      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), aArgs );
   }


   private void assertDataWithinARow( DataSet aDs, InventoryKey aInventory,
         InventoryKey aHInventory, String aHInventoryDesc, InventoryKey aNHInventory,
         String aNHInventoryDesc, InventoryKey aAssmblInventory, String aAssmblInventoryDesc,
         PartNoKey aPart, String aOEMPartNo, String aPartName, ManufacturerKey aManufacturer,
         String aManufacturerCd, String aOEMSerialNo, String aUserCd, LocationKey aLocation,
         String aLocationCd, OwnerKey aOwnerKey, String aOwnerCd, ConfigSlotKey aConfigSlotKey,
         String aBomItemCd, String aEqpPosCd, String aIssuedBool, String aQty,
         String aDecimalPlacesQt, String aQtyUnitCd ) {

      MxAssert.assertEquals( "inventory_key", aInventory,
            new InventoryKey( aDs.getString( "inventory_key" ) ) );
      MxAssert.assertEquals( "h_inventory_key", aHInventory,
            new InventoryKey( aDs.getString( "h_inventory_key" ) ) );
      MxAssert.assertEquals( "h_inventory", aHInventoryDesc, aDs.getString( "h_inventory" ) );
      if ( aDs.getString( "nh_inventory_key" ) == null ) {
         MxAssert.assertEquals( "nh_inventory_key", aNHInventory, null );
      } else {
         MxAssert.assertEquals( "nh_inventory_key", aNHInventory,
               new InventoryKey( aDs.getString( "nh_inventory_key" ) ) );
      }

      MxAssert.assertEquals( "nh_inventory", aNHInventoryDesc, aDs.getString( "nh_inventory" ) );
      if ( aDs.getString( "assmbl_inventory_key" ) == null ) {
         MxAssert.assertEquals( "assmbl_inventory_key", aAssmblInventory, null );
      } else {
         MxAssert.assertEquals( "assmbl_inventory_key", aAssmblInventory,
               new InventoryKey( aDs.getString( "assmbl_inventory_key" ) ) );
      }

      MxAssert.assertEquals( "assmbl_inventory", aAssmblInventoryDesc,
            aDs.getString( "assmbl_inventory" ) );

      MxAssert.assertEquals( "part_no_key", aPart,
            new PartNoKey( aDs.getString( "part_no_key" ) ) );
      MxAssert.assertEquals( "part_no_oem", aOEMPartNo, aDs.getString( "part_no_oem" ) );
      MxAssert.assertEquals( "part_no_sdesc", aPartName, aDs.getString( "part_no_sdesc" ) );
      MxAssert.assertEquals( "manufact_cd", aManufacturerCd, aDs.getString( "manufact_cd" ) );
      MxAssert.assertEquals( "manufact_key", aManufacturer,
            new ManufacturerKey( aDs.getString( "manufact_key" ) ) );
      MxAssert.assertEquals( "serial_no_oem", aOEMSerialNo, aDs.getString( "serial_no_oem" ) );
      MxAssert.assertEquals( "user_inv_cond_cd", aUserCd, aDs.getString( "user_inv_cond_cd" ) );
      MxAssert.assertEquals( "loc_key", aLocation, new LocationKey( aDs.getString( "loc_key" ) ) );
      MxAssert.assertEquals( "loc_sdesc", aLocationCd, aDs.getString( "loc_cd" ) );

      if ( aDs.getString( "owner_key" ) == null ) {
         MxAssert.assertEquals( "owner_key", aOwnerKey, null );
      } else {
         MxAssert.assertEquals( "owner_key", aOwnerKey,
               new OwnerKey( aDs.getString( "owner_key" ) ) );
      }

      MxAssert.assertEquals( "owner_cd", aOwnerCd, aDs.getString( "owner_cd" ) );
      MxAssert.assertEquals( "bom_item_key", aConfigSlotKey,
            new ConfigSlotKey( aDs.getString( "bom_item_key" ) ) );
      MxAssert.assertEquals( "assmbl_bom_cd", aBomItemCd, aDs.getString( "assmbl_bom_cd" ) );
      MxAssert.assertEquals( "eqp_pos_cd", aEqpPosCd, aDs.getString( "eqp_pos_cd" ) );
      MxAssert.assertEquals( "issued_bool", aIssuedBool, aDs.getString( "issued_bool" ) );
      MxAssert.assertEquals( "qty", aQty, aDs.getString( "qty" ) );
      MxAssert.assertEquals( "decimal_places_qt", aDecimalPlacesQt,
            aDs.getString( "decimal_places_qt" ) );
      MxAssert.assertEquals( "qty_unit_cd", aQtyUnitCd, aDs.getString( "qty_unit_cd" ) );
   }
}
