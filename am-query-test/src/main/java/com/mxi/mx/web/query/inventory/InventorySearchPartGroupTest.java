
package com.mxi.mx.web.query.inventory;

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
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * Tests the query com.mxi.mx.query.inventory.InventorySearchPartGroup
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class InventorySearchPartGroupTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   /**
    * Tests the search by Part Group query
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testInvalidInputs() throws Exception {

      DataSetArgument lArgs = new DataSetArgument();

      lArgs.addWhereLike( "WHERE_CLAUSE_ASSMBL", "eqp_bom_part.bom_part_cd", "INVALID" );

      // Assembly

      lArgs.addWhereEquals( "WHERE_CLAUSE_ASSMBL", "eqp_assmbl.assmbl_cd", "INVALID" );

      // Config Slot

      lArgs.addWhereLike( "WHERE_CLAUSE_ASSMBL", "eqp_assmbl_bom.assmbl_bom_cd", "INVALID" );

      // Installed/Uninstalled
      lArgs.addWhere( "inv_inv.nh_inv_no_db_id IS NULL" );

      // Include scrapped
      lArgs.addWhereNotEquals( "inv_inv.inv_cond_cd", "SCRAP" );

      // Include archived
      lArgs.addWhereNotEquals( "inv_inv.inv_cond_cd", "ARCHIVE" );

      // Serviceability
      lArgs.addWhereBoolean( "ref_inv_cond.srv_bool", false );

      // Reservation status
      lArgs.addWhereBoolean( "inv_inv.reserved_bool", false );

      // execute the query
      DataSet lInventorysDs = execute( lArgs, 100, 4650, 1, 1 );

      // There should be 0 row
      MxAssert.assertEquals( "Number of retrieved rows", 0, lInventorysDs.getRowCount() );
   }


   /**
    * Tests the search by Part Group query
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testSearchByPartGroup() throws Exception {

      DataSetArgument lArgs = new DataSetArgument();

      // Part Group
      lArgs.addWhereLike( "WHERE_CLAUSE_ASSMBL", "eqp_bom_part.bom_part_cd", "A320" );

      // Assembly

      lArgs.addWhereEquals( "WHERE_CLAUSE_ASSMBL", "eqp_assmbl.assmbl_cd", "A320" );

      // Config Slot

      lArgs.addWhereLike( "WHERE_CLAUSE_ASSMBL", "eqp_assmbl_bom.assmbl_bom_cd", "A320" );

      // Installed/Uninstalled
      lArgs.addWhere( "inv_inv.nh_inv_no_db_id IS NULL" );

      // Include scrapped
      lArgs.addWhereNotEquals( "inv_inv.inv_cond_cd", "SCRAP" );

      // Include archived
      lArgs.addWhereNotEquals( "inv_inv.inv_cond_cd", "ARCHIVE" );

      // Serviceability
      lArgs.addWhereBoolean( "ref_inv_cond.srv_bool", false );

      // Reservation status
      lArgs.add( "aReserved", "UNRESERVED" );
      // execute the query

      DataSet lInventorysDs = execute( lArgs, 100, 4650, 1, 1 );

      // There should be 1 row
      MxAssert.assertEquals( "Number of retrieved rows", 1, lInventorysDs.getRowCount() );

      lInventorysDs.next();
      testRow( lInventorysDs, new InventoryKey( "4650:319275" ), new InventoryKey( "4650:319275" ),
            "Airbus A319/A320 - TESTS", null, null, new InventoryKey( "4650:319275" ),
            "Airbus A319/A320 - TESTS", new PartNoKey( "5000000:1000" ), "A319/A320",
            "Airbus A319/A320", "TESTS THE VALIDATION LOGIC", "INREP",
            new LocationKey( "4650:1011" ), "ATL", new OwnerKey( "4650:10" ), "A319/320",
            new ConfigSlotKey( "5000000:A320:0" ), "A320", "1", "0", "1", null, null );
   }


   /**
    * Tests the query with an unauthorized user
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testUnauthorizedUser() throws Exception {

      DataSetArgument lArgs = new DataSetArgument();

      // execute the query
      DataSet lInventorysDs = execute( lArgs, 100, 4650, 1000000, 1 );

      // There should be no row
      MxAssert.assertEquals( "Number of retrieved rows", 0, lInventorysDs.getRowCount() );
   }


   /**
    * create the test data
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @BeforeClass
   public static void loadData() throws Exception {
      XmlLoader.load( sDatabaseConnectionRule.getConnection(), InventorySearchPartGroupTest.class,
            InventorySearchByTypeData.getDataFile() );
   }


   /**
    * Execute the query
    *
    * @param aArgs
    *           The argument set
    * @param aRowNum
    *           number of rows
    * @param aHrDbId
    *           hr_db_id
    * @param aHrId
    *           hr_id
    * @param aIncludeZeroQty
    *           include zero qty
    *
    * @return the result
    */
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


   /**
    * Tests a row in the dataset
    *
    * @param aDs
    *           The dataset!
    * @param aInventory
    *           The Inventory Key!
    * @param aHInventory
    *           The HInventory Key!
    * @param aHInventoryDesc
    *           The HInventory Desc!
    * @param aNHInventory
    *           The NH Inventory Key!
    * @param aNHInventoryDesc
    *           The NH Invetory Desc!
    * @param aAssmblInventory
    *           The AssmblInventory Key!
    * @param aAssmblInventoryDesc
    *           The AssmblInventory Desc!
    * @param aPart
    *           The PartNo Key!
    * @param aOEMPartNo
    *           The OEMPartNo!
    * @param aPartName
    *           The PartNameCd!
    * @param aOEMSerialNo
    *           The OEMSerialNo!
    * @param aUserCd
    *           The UserCd!
    * @param aLocation
    *           The Location Key!
    * @param aLocationCd
    *           The LocationCd!
    * @param aOwnerKey
    *           The Owner Key!
    * @param aOwnerCd
    *           The Owner Cd!
    * @param aConfigSlotKey
    *           The BomItem Key!
    * @param aBomItemCd
    *           The BomItemCd!
    * @param aEqpPosCd
    *           The EqpPosCd!
    * @param aIssuedBool
    *           The IssuedBool!
    * @param aQty
    *           The Qty!
    * @param aDecimalPlacesQt
    *           The DecimalPlacesQt!
    * @param aQtyUnitCd
    *           The QtyUnitCd!
    */
   private void testRow( DataSet aDs, InventoryKey aInventory, InventoryKey aHInventory,
         String aHInventoryDesc, InventoryKey aNHInventory, String aNHInventoryDesc,
         InventoryKey aAssmblInventory, String aAssmblInventoryDesc, PartNoKey aPart,
         String aOEMPartNo, String aPartName, String aOEMSerialNo, String aUserCd,
         LocationKey aLocation, String aLocationCd, OwnerKey aOwnerKey, String aOwnerCd,
         ConfigSlotKey aConfigSlotKey, String aBomItemCd, String aEqpPosCd, String aIssuedBool,
         String aQty, String aDecimalPlacesQt, String aQtyUnitCd ) {
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
