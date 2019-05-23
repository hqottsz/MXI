package com.mxi.mx.core.maint.plan.baselineloader;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.mxi.mx.util.AbstractDatabaseConnection;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.WhereClause;


/**
 * These Test Cases will verify Happy Path scenarios for importing materials into Maintenix.
 *
 * @author gehyca
 */
public class MaterialsImportPkg extends AbstractDatabaseConnection {

   @Rule
   public TestName testName = new TestName();

   private ArrayList<String> iClearBaselineTables = new ArrayList<String>();
   {
      iClearBaselineTables.add( "delete from BL_ALTERNATE_UOM" );
      iClearBaselineTables.add( "delete from C_PART_NO_MISC_INFO" );
      iClearBaselineTables.add( "delete from C_STOCK_NO" );
      iClearBaselineTables.add( "delete from C_STOCK_LOC" );
      iClearBaselineTables.add( "delete from C_BIN_PART" );
   }

   private ArrayList<String> iLoadPreData = new ArrayList<String>();
   {
      // add record for KITS
      iLoadPreData.add(
            "INSERT INTO eqp_part_no (inv_class_cd,part_status_cd,part_status_db_id,manufact_db_id,manufact_cd,bitmap_db_id,bitmap_tag,part_no_sdesc,part_no_oem,part_no_db_id, part_no_id,QTY_UNIT_DB_ID,QTY_UNIT_CD, Asset_Account_Db_Id,Asset_Account_Id,avg_unit_price,total_qt,total_value,financial_class_db_id,financial_class_cd) VALUES ('KIT','ACTV','0','4650','KITS','0','29','KITS-98736','KIT001','4650','768937','0','EA','4650','100007','0','0','0','10','CONSUMABLE')" );
   }

   private ArrayList<String> iRestoreTables = new ArrayList<String>();
   {
      iRestoreTables.add(
            "update eqp_part_no SET SCRAP_RATE_PCT = NULL, CALC_ABC_CLASS_BOOL = NULL, SOS_BOOL = NULL, PMA_BOOL = NULL, PROCURABLE_BOOL = NULL WHERE SCRAP_RATE_PCT = '75.5'" );
      iRestoreTables.add( "DELETE from INV_LOC_BIN WHERE MIN_QT = '7' AND MAX_QT = '23'" );
      iRestoreTables.add(
            "DELETE from INV_LOC_STOCK WHERE MIN_REORDER_QT = '10' AND STOCK_LOW_ACTN_CD = 'SHIPREQ' AND REORDER_QT = '17' AND ALLOC_PCT = '1'" );
      iRestoreTables.add(
            "DELETE from EQP_STOCK_NO  WHERE (STOCK_NO_CD = 'A0000001' AND STOCK_NO_NAME = '10001')"
                  + "OR (STOCK_NO_CD = 'ENG_ASSY_PN1' AND STOCK_NO_NAME = 'ABC11')"
                  + "OR (STOCK_NO_CD = 'A0000012' AND STOCK_NO_NAME = '1234567890')"
                  + "OR (STOCK_NO_CD = 'ACFT_ASSY_PN1' AND STOCK_NO_NAME = '10001')"
                  + "OR (STOCK_NO_CD = 'A0000009' AND STOCK_NO_NAME = '10001')"
                  + "OR (STOCK_NO_CD = 'KIT001' AND STOCK_NO_NAME = 'KITS')" );
   }

   ArrayList<String> iDeleteKit = new ArrayList<String>();
   {
      iDeleteKit // Note: part_no_id = 768937 is a unique # that I gave it and their will be no
                 // conflicts unless creates these many part_no_ids
            .add( "Delete from eqp_part_no WHERE part_no_sdesc = 'KITS-98736'" );
   }

   ArrayList<String> iDeleteBL_ALTERNATE_UOM = new ArrayList<String>();
   {
      iDeleteBL_ALTERNATE_UOM.add(
            "DELETE from EQP_PART_ALT_UNIT  WHERE QTY_UNIT_CD = 'AY' AND QTY_CONVERT_QT = '1'" );
   }

   String iAirport1CD_24321 = null;
   String iAirport2CD_24321 = null;
   String iHangerCD_24321 = null;
   String iTrackCD_24321 = null;
   String iRepairCD_24321 = null;
   String iShopCD_24321 = null;
   String iStoreCD_24321 = null;
   String iBinCD_24321 = null;
   String iDock1CD_24321 = null;
   String iDock2CD_24321 = null;

   {
      iAirport1CD_24321 = get2000Length( "AUTOAPT1" );
      iAirport2CD_24321 = get2000Length( "AUTOAPT2" );
      iHangerCD_24321 = get2000Length( "AUTOHR" );
      iTrackCD_24321 = get2000Length( "AUTOTR" );
      iRepairCD_24321 = get2000Length( "AUTORP1" );
      iShopCD_24321 = get2000Length( "AUTOSHP" );
      iStoreCD_24321 = get2000Length( "AUTOST1" );
      iBinCD_24321 = get2000Length( "AUTOBIN" );
      iDock1CD_24321 = get2000Length( "AUTODOCK1" );
      iDock2CD_24321 = get2000Length( "AUTODOCK2" );

   }


   @Override
   @Before
   public void before() throws Exception {
      super.before();
      if ( testName.getMethodName().contains( "Kit" ) ) {
         classDataSetup( iLoadPreData );
      }
   }


   @Override
   @After
   public void after() throws Exception {
      classDataSetup( iClearBaselineTables );
      classDataSetup( iRestoreTables );
      if ( testName.getMethodName().contains( "Kit" ) ) {
         classDataSetup( iDeleteKit );
      }
      if ( testName.getMethodName().contains( "BL_ALTERNATE_UOM" ) ) {
         classDataSetup( iDeleteBL_ALTERNATE_UOM );
      }
      super.after();
   }


   /**
    * Test #1.1 This test case will validate and import a Tracked Part using Materials_Import
    * Package.
    *
    */
   @Test
   public void testImportMaterialsForTrkPart() {
      System.out.println(
            "======= Starting:  " + testName.getMethodName() + " ========================" );

      // inserting data into c_part_no_misc_info table
      Map<String, String> lCPartNoMiscInfoMap = new LinkedHashMap<>();

      lCPartNoMiscInfoMap.put( "PART_NO_OEM", "\'A0000001\'" );
      lCPartNoMiscInfoMap.put( "MANUFACT_REF", "\'10001\'" );
      lCPartNoMiscInfoMap.put( "SCRAP_RATE_PCT", "\'75.5\'" );
      lCPartNoMiscInfoMap.put( "ABC_CLASS_CD", "\'B\'" );
      lCPartNoMiscInfoMap.put( "CALC_ABC_CLASS_BOOL", "\'N\'" );
      lCPartNoMiscInfoMap.put( "SOS_BOOL", "\'N\'" );
      lCPartNoMiscInfoMap.put( "PMA_BOOL", "\'N\'" );
      lCPartNoMiscInfoMap.put( "PROCURABLE_BOOL", "\'N\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PART_NO_MISC_INFO,
            lCPartNoMiscInfoMap ) );

      // column has a different title in eqp_part_no
      lCPartNoMiscInfoMap.remove( "MANUFACT_REF" );
      lCPartNoMiscInfoMap.put( "MANUFACT_CD", "\'10001\'" );

      // inserting data into C_Stock_No table
      Map<String, String> lCStockNoMap = new LinkedHashMap<>();

      lCStockNoMap.put( "STOCK_NO_CD", "\'A0000001\'" );
      lCStockNoMap.put( "STOCK_NAME", "\'10001\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STOCK_NO, lCStockNoMap ) );

      // column has a different title in eqp_stock_no
      lCStockNoMap.remove( "STOCK_NAME" );
      lCStockNoMap.put( "STOCK_NO_NAME", "\'10001\'" );

      // inserting data into C_BIN_PART table
      Map<String, String> lCBinPartMap = new LinkedHashMap<>();

      lCBinPartMap.put( "AIRPORT_CD", "\'AIRPORT1\'" );
      lCBinPartMap.put( "STORE_CD", "\'STORE\'" );
      lCBinPartMap.put( "BIN_CD", "\'BIN1-1\'" );
      lCBinPartMap.put( "PART_NO_OEM", "\'A0000001\'" );
      lCBinPartMap.put( "MANUFACT_REF", "\'10001\'" );
      lCBinPartMap.put( "MIN_QT", "\'7\'" );
      lCBinPartMap.put( "MAX_QT", "\'23\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BIN_PART, lCBinPartMap ) );

      // These columns are not used to verify imported item
      lCBinPartMap.remove( "AIRPORT_CD" );
      lCBinPartMap.remove( "STORE_CD" );
      lCBinPartMap.remove( "BIN_CD" );
      lCBinPartMap.remove( "PART_NO_OEM" );
      lCBinPartMap.remove( "MANUFACT_REF" );

      // inserting data into C_STOCK_LOC table
      Map<String, String> lCStockLocMap = new LinkedHashMap<>();

      lCStockLocMap.put( "STOCK_NO_CD", "\'A0000001\'" );
      lCStockLocMap.put( "AIRPORT_CD", "\'AIRPORT1\'" );
      lCStockLocMap.put( "MIN_REORDER_QT", "\'10\'" );
      lCStockLocMap.put( "STOCK_LOW_ACTN_CD", "\'SHIPREQ\'" );
      lCStockLocMap.put( "REORDER_QT", "\'17\'" );
      lCStockLocMap.put( "ALLOC_PCT", "\'1\'" );
      lCStockLocMap.put( "BATCH_SIZE", "\'5\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STOCK_LOC, lCStockLocMap ) );

      // These columns are not used to verify imported item
      lCStockLocMap.remove( "AIRPORT_CD" );
      lCStockLocMap.remove( "STOCK_NO_CD" );

      // run validation
      Assert.assertTrue( "Validation: ", runValidateMaterialsImport() == 1 );

      // run Import
      Assert.assertTrue( "Import: ", runImportMaterialsImport() == 1 );

      // verify Records were imported correctly
      lCPartNoMiscInfoMap = changeYesTo1AndNoto0( lCPartNoMiscInfoMap );
      Assert.assertTrue( "Verify values in EQP_PART_NO: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.EQP_PART_NO, lCPartNoMiscInfoMap ) ) );
      Assert.assertTrue( "Verify values in EQP_STOCK_NO: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.EQP_STOCK_NO, lCStockNoMap ) ) );
      Assert.assertTrue( "Verify values in INV_LOC_BIN: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.INV_LOC_BIN, lCBinPartMap ) ) );
      Assert.assertTrue( "Verify values in INV_LOC_STOCK: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.INV_LOC_STOCK, lCStockLocMap ) ) );
   }


   /**
    * Test #1.2 This test case will validate and import a Tracked Part using Materials_Import
    * Package. The difference between Test 1.1 and 1.2 is c_part_no_misc_info values
    *
    */
   @Test
   public void testImportMaterialsForTrkPartTwo() {
      System.out.println(
            "======= Starting:  " + testName.getMethodName() + " ========================" );

      // inserting data into c_part_no_misc_info table
      Map<String, String> lCPartNoMiscInfoMap = new LinkedHashMap<>();

      lCPartNoMiscInfoMap.put( "PART_NO_OEM", "\'A0000001\'" );
      lCPartNoMiscInfoMap.put( "MANUFACT_REF", "\'10001\'" );
      lCPartNoMiscInfoMap.put( "SCRAP_RATE_PCT", "\'75.5\'" );
      lCPartNoMiscInfoMap.put( "ABC_CLASS_CD", "\'C\'" );
      lCPartNoMiscInfoMap.put( "CALC_ABC_CLASS_BOOL", "\'Y\'" );
      lCPartNoMiscInfoMap.put( "SOS_BOOL", "\'Y\'" );
      lCPartNoMiscInfoMap.put( "PMA_BOOL", "\'Y\'" );
      lCPartNoMiscInfoMap.put( "PROCURABLE_BOOL", "\'Y\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PART_NO_MISC_INFO,
            lCPartNoMiscInfoMap ) );

      // column has a different title in eqp_part_no
      lCPartNoMiscInfoMap.remove( "MANUFACT_REF" );
      lCPartNoMiscInfoMap.put( "MANUFACT_CD", "\'10001\'" );

      // inserting data into C_Stock_No table
      Map<String, String> lCStockNoMap = new LinkedHashMap<>();

      lCStockNoMap.put( "STOCK_NO_CD", "\'A0000001\'" );
      lCStockNoMap.put( "STOCK_NAME", "\'10001\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STOCK_NO, lCStockNoMap ) );

      // column has a different title in eqp_stock_no
      lCStockNoMap.remove( "STOCK_NAME" );
      lCStockNoMap.put( "STOCK_NO_NAME", "\'10001\'" );

      // inserting data into C_BIN_PART table
      Map<String, String> lCBinPartMap = new LinkedHashMap<>();

      lCBinPartMap.put( "AIRPORT_CD", "\'AIRPORT1\'" );
      lCBinPartMap.put( "STORE_CD", "\'STORE\'" );
      lCBinPartMap.put( "BIN_CD", "\'BIN1-1\'" );
      lCBinPartMap.put( "PART_NO_OEM", "\'A0000001\'" );
      lCBinPartMap.put( "MANUFACT_REF", "\'10001\'" );
      lCBinPartMap.put( "MIN_QT", "\'7\'" );
      lCBinPartMap.put( "MAX_QT", "\'23\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BIN_PART, lCBinPartMap ) );

      // These columns are not used to verify imported item
      lCBinPartMap.remove( "AIRPORT_CD" );
      lCBinPartMap.remove( "STORE_CD" );
      lCBinPartMap.remove( "BIN_CD" );
      lCBinPartMap.remove( "PART_NO_OEM" );
      lCBinPartMap.remove( "MANUFACT_REF" );

      // inserting data into C_STOCK_LOC table
      Map<String, String> lCStockLocMap = new LinkedHashMap<>();

      lCStockLocMap.put( "STOCK_NO_CD", "\'A0000001\'" );
      lCStockLocMap.put( "AIRPORT_CD", "\'AIRPORT1\'" );
      lCStockLocMap.put( "MIN_REORDER_QT", "\'10\'" );
      lCStockLocMap.put( "STOCK_LOW_ACTN_CD", "\'SHIPREQ\'" );
      lCStockLocMap.put( "REORDER_QT", "\'17\'" );
      lCStockLocMap.put( "ALLOC_PCT", "\'1\'" );
      lCStockLocMap.put( "BATCH_SIZE", "\'5\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STOCK_LOC, lCStockLocMap ) );

      // These columns are not used to verify imported item
      lCStockLocMap.remove( "AIRPORT_CD" );
      lCStockLocMap.remove( "STOCK_NO_CD" );

      // run validation
      Assert.assertTrue( "Validation: ", runValidateMaterialsImport() == 1 );

      // run Import
      Assert.assertTrue( "Import: ", runImportMaterialsImport() == 1 );

      // verify Records were imported correctly
      lCPartNoMiscInfoMap = changeYesTo1AndNoto0( lCPartNoMiscInfoMap );
      Assert.assertTrue( "Verify values in EQP_PART_NO: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.EQP_PART_NO, lCPartNoMiscInfoMap ) ) );
      Assert.assertTrue( "Verify values in EQP_STOCK_NO: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.EQP_STOCK_NO, lCStockNoMap ) ) );
      Assert.assertTrue( "Verify values in INV_LOC_BIN: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.INV_LOC_BIN, lCBinPartMap ) ) );
      Assert.assertTrue( "Verify values in INV_LOC_STOCK: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.INV_LOC_STOCK, lCStockLocMap ) ) );
   }


   /**
    * Test #2.1 This Test case will validate and import a Aircraft Part using Materials_Import
    * Package.
    *
    */
   @Test
   public void testImportMaterialsForACFTPart() {
      System.out.println(
            "======= Starting:  " + testName.getMethodName() + " ========================" );

      // inserting data into c_part_no_misc_info table
      Map<String, String> lCPartNoMiscInfoMap = new LinkedHashMap<>();

      lCPartNoMiscInfoMap.put( "PART_NO_OEM", "\'ACFT_ASSY_PN1\'" );
      lCPartNoMiscInfoMap.put( "MANUFACT_REF", "\'10001\'" );
      lCPartNoMiscInfoMap.put( "SCRAP_RATE_PCT", "\'75.5\'" );
      lCPartNoMiscInfoMap.put( "ABC_CLASS_CD", "\'B\'" );
      lCPartNoMiscInfoMap.put( "CALC_ABC_CLASS_BOOL", "\'N\'" );
      lCPartNoMiscInfoMap.put( "SOS_BOOL", "\'N\'" );
      lCPartNoMiscInfoMap.put( "PMA_BOOL", "\'N\'" );
      lCPartNoMiscInfoMap.put( "PROCURABLE_BOOL", "\'N\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PART_NO_MISC_INFO,
            lCPartNoMiscInfoMap ) );

      lCPartNoMiscInfoMap.remove( "MANUFACT_REF" );
      lCPartNoMiscInfoMap.put( "MANUFACT_CD", "\'10001\'" );

      // inserting data into C_Stock_No table
      Map<String, String> lCStockNoMap = new LinkedHashMap<>();

      lCStockNoMap.put( "STOCK_NO_CD", "\'ACFT_ASSY_PN1\'" );
      lCStockNoMap.put( "STOCK_NAME", "\'10001\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STOCK_NO, lCStockNoMap ) );

      // column has a different title in eqp_stock_no
      lCStockNoMap.remove( "STOCK_NAME" );
      lCStockNoMap.put( "STOCK_NO_NAME", "\'10001\'" );

      // inserting data into C_BIN_PART table
      Map<String, String> lCBinPartMap = new LinkedHashMap<>();

      lCBinPartMap.put( "AIRPORT_CD", "\'AIRPORT2\'" );
      lCBinPartMap.put( "STORE_CD", "\'STORE\'" );
      lCBinPartMap.put( "BIN_CD", "\'BIN2-1\'" );
      lCBinPartMap.put( "PART_NO_OEM", "\'ACFT_ASSY_PN1\'" );
      lCBinPartMap.put( "MANUFACT_REF", "\'10001\'" );
      lCBinPartMap.put( "MIN_QT", "\'7\'" );
      lCBinPartMap.put( "MAX_QT", "\'23\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BIN_PART, lCBinPartMap ) );

      // These columns are not used to verify imported item
      lCBinPartMap.remove( "AIRPORT_CD" );
      lCBinPartMap.remove( "STORE_CD" );
      lCBinPartMap.remove( "BIN_CD" );
      lCBinPartMap.remove( "PART_NO_OEM" );
      lCBinPartMap.remove( "MANUFACT_REF" );

      // inserting data into C_STOCK_LOC table
      Map<String, String> lCStockLocMap = new LinkedHashMap<>();

      lCStockLocMap.put( "STOCK_NO_CD", "\'ACFT_ASSY_PN1\'" );
      lCStockLocMap.put( "AIRPORT_CD", "\'AIRPORT1\'" );
      lCStockLocMap.put( "MIN_REORDER_QT", "\'10\'" );
      lCStockLocMap.put( "STOCK_LOW_ACTN_CD", "\'SHIPREQ\'" );
      lCStockLocMap.put( "REORDER_QT", "\'17\'" );
      lCStockLocMap.put( "ALLOC_PCT", "\'1\'" );
      lCStockLocMap.put( "BATCH_SIZE", "\'2\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STOCK_LOC, lCStockLocMap ) );

      // These columns are not used to verify imported item
      lCStockLocMap.remove( "AIRPORT_CD" );
      lCStockLocMap.remove( "STOCK_NO_CD" );

      // run validation
      Assert.assertTrue( "Validation: ", runValidateMaterialsImport() == 1 );

      // run Import
      Assert.assertTrue( "Import: ", runImportMaterialsImport() == 1 );

      // verify Records were imported correctly
      lCPartNoMiscInfoMap = changeYesTo1AndNoto0( lCPartNoMiscInfoMap );
      Assert.assertTrue( "Verify values in EQP_PART_NO: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.EQP_PART_NO, lCPartNoMiscInfoMap ) ) );
      Assert.assertTrue( "Verify values in EQP_STOCK_NO: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.EQP_STOCK_NO, lCStockNoMap ) ) );
      Assert.assertTrue( "Verify values in INV_LOC_BIN: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.INV_LOC_BIN, lCBinPartMap ) ) );
      Assert.assertTrue( "Verify values in INV_LOC_STOCK: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.INV_LOC_STOCK, lCStockLocMap ) ) );
   }


   /**
    * Test #2.2 This Test case will validate and import a Aircraft Part using Materials_Import
    * Package. The difference between Test 2.1 and 2.2 is c_part_no_misc_info values
    *
    */
   @Test
   public void testImportMaterialsForACFTPartTwo() {
      System.out.println(
            "======= Starting:  " + testName.getMethodName() + " ========================" );

      // inserting data into c_part_no_misc_info table
      Map<String, String> lCPartNoMiscInfoMap = new LinkedHashMap<>();

      lCPartNoMiscInfoMap.put( "PART_NO_OEM", "\'ACFT_ASSY_PN1\'" );
      lCPartNoMiscInfoMap.put( "MANUFACT_REF", "\'10001\'" );
      lCPartNoMiscInfoMap.put( "SCRAP_RATE_PCT", "\'75.5\'" );
      lCPartNoMiscInfoMap.put( "ABC_CLASS_CD", "\'A\'" );
      lCPartNoMiscInfoMap.put( "CALC_ABC_CLASS_BOOL", "\'Y\'" );
      lCPartNoMiscInfoMap.put( "SOS_BOOL", "\'Y\'" );
      lCPartNoMiscInfoMap.put( "PMA_BOOL", "\'Y\'" );
      lCPartNoMiscInfoMap.put( "PROCURABLE_BOOL", "\'Y\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PART_NO_MISC_INFO,
            lCPartNoMiscInfoMap ) );

      lCPartNoMiscInfoMap.remove( "MANUFACT_REF" );
      lCPartNoMiscInfoMap.put( "MANUFACT_CD", "\'10001\'" );

      // inserting data into C_Stock_No table
      Map<String, String> lCStockNoMap = new LinkedHashMap<>();

      lCStockNoMap.put( "STOCK_NO_CD", "\'ACFT_ASSY_PN1\'" );
      lCStockNoMap.put( "STOCK_NAME", "\'10001\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STOCK_NO, lCStockNoMap ) );

      // column has a different title in eqp_stock_no
      lCStockNoMap.remove( "STOCK_NAME" );
      lCStockNoMap.put( "STOCK_NO_NAME", "\'10001\'" );

      // inserting data into C_BIN_PART table
      Map<String, String> lCBinPartMap = new LinkedHashMap<>();

      lCBinPartMap.put( "AIRPORT_CD", "\'AIRPORT2\'" );
      lCBinPartMap.put( "STORE_CD", "\'STORE\'" );
      lCBinPartMap.put( "BIN_CD", "\'BIN2-1\'" );
      lCBinPartMap.put( "PART_NO_OEM", "\'ACFT_ASSY_PN1\'" );
      lCBinPartMap.put( "MANUFACT_REF", "\'10001\'" );
      lCBinPartMap.put( "MIN_QT", "\'7\'" );
      lCBinPartMap.put( "MAX_QT", "\'23\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BIN_PART, lCBinPartMap ) );

      // These columns are not used to verify imported item
      lCBinPartMap.remove( "AIRPORT_CD" );
      lCBinPartMap.remove( "STORE_CD" );
      lCBinPartMap.remove( "BIN_CD" );
      lCBinPartMap.remove( "PART_NO_OEM" );
      lCBinPartMap.remove( "MANUFACT_REF" );

      // inserting data into C_STOCK_LOC table
      Map<String, String> lCStockLocMap = new LinkedHashMap<>();

      lCStockLocMap.put( "STOCK_NO_CD", "\'ACFT_ASSY_PN1\'" );
      lCStockLocMap.put( "AIRPORT_CD", "\'AIRPORT1\'" );
      lCStockLocMap.put( "MIN_REORDER_QT", "\'10\'" );
      lCStockLocMap.put( "STOCK_LOW_ACTN_CD", "\'SHIPREQ\'" );
      lCStockLocMap.put( "REORDER_QT", "\'17\'" );
      lCStockLocMap.put( "ALLOC_PCT", "\'1\'" );
      lCStockLocMap.put( "BATCH_SIZE", "\'2\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STOCK_LOC, lCStockLocMap ) );

      // These columns are not used to verify imported item
      lCStockLocMap.remove( "AIRPORT_CD" );
      lCStockLocMap.remove( "STOCK_NO_CD" );

      // run validation
      Assert.assertTrue( "Validation: ", runValidateMaterialsImport() == 1 );

      // run Import
      Assert.assertTrue( "Import: ", runImportMaterialsImport() == 1 );

      // verify Records were imported correctly
      lCPartNoMiscInfoMap = changeYesTo1AndNoto0( lCPartNoMiscInfoMap );
      Assert.assertTrue( "Verify values in EQP_PART_NO: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.EQP_PART_NO, lCPartNoMiscInfoMap ) ) );
      Assert.assertTrue( "Verify values in EQP_STOCK_NO: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.EQP_STOCK_NO, lCStockNoMap ) ) );
      Assert.assertTrue( "Verify values in INV_LOC_BIN: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.INV_LOC_BIN, lCBinPartMap ) ) );
      Assert.assertTrue( "Verify values in INV_LOC_STOCK: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.INV_LOC_STOCK, lCStockLocMap ) ) );
   }


   /**
    * Test #3.1 This Test case will validate and import an assembly part using Materials_Import
    * Package.
    *
    */
   @Test
   public void testImportMaterialsForAssyPart() {
      System.out.println(
            "======= Starting:  " + testName.getMethodName() + " ========================" );

      // inserting data into c_part_no_misc_info table
      Map<String, String> lCPartNoMiscInfoMap = new LinkedHashMap<>();

      lCPartNoMiscInfoMap.put( "PART_NO_OEM", "\'ENG_ASSY_PN1\'" );
      lCPartNoMiscInfoMap.put( "MANUFACT_REF", "\'ABC11\'" );
      lCPartNoMiscInfoMap.put( "SCRAP_RATE_PCT", "\'75.5\'" );
      lCPartNoMiscInfoMap.put( "ABC_CLASS_CD", "\'B\'" );
      lCPartNoMiscInfoMap.put( "CALC_ABC_CLASS_BOOL", "\'N\'" );
      lCPartNoMiscInfoMap.put( "SOS_BOOL", "\'N\'" );
      lCPartNoMiscInfoMap.put( "PMA_BOOL", "\'N\'" );
      lCPartNoMiscInfoMap.put( "PROCURABLE_BOOL", "\'N\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PART_NO_MISC_INFO,
            lCPartNoMiscInfoMap ) );

      // column has a different title in eqp_part_no
      lCPartNoMiscInfoMap.remove( "MANUFACT_REF" );
      lCPartNoMiscInfoMap.put( "MANUFACT_CD", "\'ABC11\'" );

      // inserting data into C_Stock_No table
      Map<String, String> lCStockNoMap = new LinkedHashMap<>();

      lCStockNoMap.put( "STOCK_NO_CD", "\'ENG_ASSY_PN1\'" );
      lCStockNoMap.put( "STOCK_NAME", "\'ABC11\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STOCK_NO, lCStockNoMap ) );

      // column has a different title in eqp_stock_no
      lCStockNoMap.remove( "STOCK_NAME" );
      lCStockNoMap.put( "STOCK_NO_NAME", "\'ABC11\'" );

      // inserting data into C_BIN_PART table
      Map<String, String> lCBinPartMap = new LinkedHashMap<>();

      lCBinPartMap.put( "AIRPORT_CD", "\'AIRPORT2\'" );
      lCBinPartMap.put( "STORE_CD", "\'STORE\'" );
      lCBinPartMap.put( "BIN_CD", "\'BIN2-1\'" );
      lCBinPartMap.put( "PART_NO_OEM", "\'ENG_ASSY_PN1\'" );
      lCBinPartMap.put( "MANUFACT_REF", "\'ABC11\'" );
      lCBinPartMap.put( "MIN_QT", "\'7\'" );
      lCBinPartMap.put( "MAX_QT", "\'23\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BIN_PART, lCBinPartMap ) );

      // These columns are not used to verify imported item
      lCBinPartMap.remove( "AIRPORT_CD" );
      lCBinPartMap.remove( "STORE_CD" );
      lCBinPartMap.remove( "BIN_CD" );
      lCBinPartMap.remove( "PART_NO_OEM" );
      lCBinPartMap.remove( "MANUFACT_REF" );

      // inserting data into C_STOCK_LOC table
      Map<String, String> lCStockLocMap = new LinkedHashMap<>();

      lCStockLocMap.put( "STOCK_NO_CD", "\'ENG_ASSY_PN1\'" );
      lCStockLocMap.put( "AIRPORT_CD", "\'AIRPORT2\'" );
      lCStockLocMap.put( "MIN_REORDER_QT", "\'10\'" );
      lCStockLocMap.put( "STOCK_LOW_ACTN_CD", "\'SHIPREQ\'" );
      lCStockLocMap.put( "REORDER_QT", "\'17\'" );
      lCStockLocMap.put( "ALLOC_PCT", "\'1\'" );
      lCStockLocMap.put( "BATCH_SIZE", "\'2\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STOCK_LOC, lCStockLocMap ) );
      // These columns are not used to verify imported item
      lCStockLocMap.remove( "AIRPORT_CD" );
      lCStockLocMap.remove( "STOCK_NO_CD" );

      // run validation
      Assert.assertTrue( "Validation: ", runValidateMaterialsImport() == 1 );

      // run Import
      Assert.assertTrue( "Import: ", runImportMaterialsImport() == 1 );

      // verify Records were imported correctly
      lCPartNoMiscInfoMap = changeYesTo1AndNoto0( lCPartNoMiscInfoMap );
      Assert.assertTrue( "Verify values in EQP_PART_NO: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.EQP_PART_NO, lCPartNoMiscInfoMap ) ) );
      Assert.assertTrue( "Verify values in EQP_STOCK_NO: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.EQP_STOCK_NO, lCStockNoMap ) ) );
      Assert.assertTrue( "Verify values in INV_LOC_BIN: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.INV_LOC_BIN, lCBinPartMap ) ) );
      Assert.assertTrue( "Verify values in INV_LOC_STOCK: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.INV_LOC_STOCK, lCStockLocMap ) ) );
   }


   /**
    * Test #3.2 This Test case will validate and import an assembly part using Materials_Import
    * Package. The difference between Test 3.1 and 3.2 is c_part_no_misc_info Y/N values.
    *
    */
   @Test
   public void testImportMaterialsForAssyPartTwo() {
      System.out.println(
            "======= Starting:  " + testName.getMethodName() + " ========================" );

      // inserting data into c_part_no_misc_info table
      Map<String, String> lCPartNoMiscInfoMap = new LinkedHashMap<>();

      lCPartNoMiscInfoMap.put( "PART_NO_OEM", "\'ENG_ASSY_PN1\'" );
      lCPartNoMiscInfoMap.put( "MANUFACT_REF", "\'ABC11\'" );
      lCPartNoMiscInfoMap.put( "SCRAP_RATE_PCT", "\'75.5\'" );
      lCPartNoMiscInfoMap.put( "ABC_CLASS_CD", "\'C\'" );
      lCPartNoMiscInfoMap.put( "CALC_ABC_CLASS_BOOL", "\'Y\'" );
      lCPartNoMiscInfoMap.put( "SOS_BOOL", "\'Y\'" );
      lCPartNoMiscInfoMap.put( "PMA_BOOL", "\'Y\'" );
      lCPartNoMiscInfoMap.put( "PROCURABLE_BOOL", "\'Y\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PART_NO_MISC_INFO,
            lCPartNoMiscInfoMap ) );

      // column has a different title in eqp_part_no
      lCPartNoMiscInfoMap.remove( "MANUFACT_REF" );
      lCPartNoMiscInfoMap.put( "MANUFACT_CD", "\'ABC11\'" );

      // inserting data into C_Stock_No table
      Map<String, String> lCStockNoMap = new LinkedHashMap<>();

      lCStockNoMap.put( "STOCK_NO_CD", "\'ENG_ASSY_PN1\'" );
      lCStockNoMap.put( "STOCK_NAME", "\'ABC11\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STOCK_NO, lCStockNoMap ) );

      // column has a different title in eqp_stock_no
      lCStockNoMap.remove( "STOCK_NAME" );
      lCStockNoMap.put( "STOCK_NO_NAME", "\'ABC11\'" );

      // inserting data into C_BIN_PART table
      Map<String, String> lCBinPartMap = new LinkedHashMap<>();

      lCBinPartMap.put( "AIRPORT_CD", "\'AIRPORT2\'" );
      lCBinPartMap.put( "STORE_CD", "\'STORE\'" );
      lCBinPartMap.put( "BIN_CD", "\'BIN2-1\'" );
      lCBinPartMap.put( "PART_NO_OEM", "\'ENG_ASSY_PN1\'" );
      lCBinPartMap.put( "MANUFACT_REF", "\'ABC11\'" );
      lCBinPartMap.put( "MIN_QT", "\'7\'" );
      lCBinPartMap.put( "MAX_QT", "\'23\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BIN_PART, lCBinPartMap ) );

      // These columns are not used to verify imported item
      lCBinPartMap.remove( "AIRPORT_CD" );
      lCBinPartMap.remove( "STORE_CD" );
      lCBinPartMap.remove( "BIN_CD" );
      lCBinPartMap.remove( "PART_NO_OEM" );
      lCBinPartMap.remove( "MANUFACT_REF" );

      // inserting data into C_STOCK_LOC table
      Map<String, String> lCStockLocMap = new LinkedHashMap<>();

      lCStockLocMap.put( "STOCK_NO_CD", "\'ENG_ASSY_PN1\'" );
      lCStockLocMap.put( "AIRPORT_CD", "\'AIRPORT2\'" );
      lCStockLocMap.put( "MIN_REORDER_QT", "\'10\'" );
      lCStockLocMap.put( "STOCK_LOW_ACTN_CD", "\'SHIPREQ\'" );
      lCStockLocMap.put( "REORDER_QT", "\'17\'" );
      lCStockLocMap.put( "ALLOC_PCT", "\'1\'" );
      lCStockLocMap.put( "BATCH_SIZE", "\'2\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STOCK_LOC, lCStockLocMap ) );
      // These columns are not used to verify imported item
      lCStockLocMap.remove( "AIRPORT_CD" );
      lCStockLocMap.remove( "STOCK_NO_CD" );

      // run validation
      Assert.assertTrue( "Validation: ", runValidateMaterialsImport() == 1 );

      // run Import
      Assert.assertTrue( "Import: ", runImportMaterialsImport() == 1 );

      // verify Records were imported correctly
      lCPartNoMiscInfoMap = changeYesTo1AndNoto0( lCPartNoMiscInfoMap );
      Assert.assertTrue( "Verify values in EQP_PART_NO: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.EQP_PART_NO, lCPartNoMiscInfoMap ) ) );
      Assert.assertTrue( "Verify values in EQP_STOCK_NO: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.EQP_STOCK_NO, lCStockNoMap ) ) );
      Assert.assertTrue( "Verify values in INV_LOC_BIN: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.INV_LOC_BIN, lCBinPartMap ) ) );
      Assert.assertTrue( "Verify values in INV_LOC_STOCK: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.INV_LOC_STOCK, lCStockLocMap ) ) );
   }


   /**
    * Test #4.1 This test case will validate and import a serialized part using Materials_Import
    * Package.
    *
    */
   @Test
   public void testImportMaterialsForSerPart() {
      System.out.println(
            "======= Starting:  " + testName.getMethodName() + " ========================" );

      // inserting data into c_part_no_misc_info table
      Map<String, String> lCPartNoMiscInfoMap = new LinkedHashMap<>();

      lCPartNoMiscInfoMap.put( "PART_NO_OEM", "\'A0000012\'" );
      lCPartNoMiscInfoMap.put( "MANUFACT_REF", "\'1234567890\'" );
      lCPartNoMiscInfoMap.put( "SCRAP_RATE_PCT", "\'75.5\'" );
      lCPartNoMiscInfoMap.put( "ABC_CLASS_CD", "\'B\'" );
      lCPartNoMiscInfoMap.put( "CALC_ABC_CLASS_BOOL", "\'N\'" );
      lCPartNoMiscInfoMap.put( "SOS_BOOL", "\'N\'" );
      lCPartNoMiscInfoMap.put( "PMA_BOOL", "\'N\'" );
      lCPartNoMiscInfoMap.put( "PROCURABLE_BOOL", "\'N\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PART_NO_MISC_INFO,
            lCPartNoMiscInfoMap ) );

      // column has a different title in eqp_part_no
      lCPartNoMiscInfoMap.remove( "MANUFACT_REF" );
      lCPartNoMiscInfoMap.put( "MANUFACT_CD", "\'1234567890\'" );

      // inserting data into C_Stock_No table
      Map<String, String> lCStockNoMap = new LinkedHashMap<>();

      lCStockNoMap.put( "STOCK_NO_CD", "\'A0000012\'" );
      lCStockNoMap.put( "STOCK_NAME", "\'1234567890\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STOCK_NO, lCStockNoMap ) );

      // column has a different title in eqp_stock_no
      lCStockNoMap.remove( "STOCK_NAME" );
      lCStockNoMap.put( "STOCK_NO_NAME", "\'1234567890\'" );

      // inserting data into C_BIN_PART table
      Map<String, String> lCBinPartMap = new LinkedHashMap<>();

      lCBinPartMap.put( "AIRPORT_CD", "\'AIRPORT2\'" );
      lCBinPartMap.put( "STORE_CD", "\'STORE\'" );
      lCBinPartMap.put( "BIN_CD", "\'BIN2-1\'" );
      lCBinPartMap.put( "PART_NO_OEM", "\'A0000012\'" );
      lCBinPartMap.put( "MANUFACT_REF", "\'1234567890\'" );
      lCBinPartMap.put( "MIN_QT", "\'7\'" );
      lCBinPartMap.put( "MAX_QT", "\'23\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BIN_PART, lCBinPartMap ) );
      // These columns are not used to verify imported item
      lCBinPartMap.remove( "AIRPORT_CD" );
      lCBinPartMap.remove( "STORE_CD" );
      lCBinPartMap.remove( "BIN_CD" );
      lCBinPartMap.remove( "PART_NO_OEM" );
      lCBinPartMap.remove( "MANUFACT_REF" );

      // inserting data into C_STOCK_LOC table
      Map<String, String> lCStockLocMap = new LinkedHashMap<>();

      lCStockLocMap.put( "STOCK_NO_CD", "\'A0000012\'" );
      lCStockLocMap.put( "AIRPORT_CD", "\'AIRPORT2\'" );
      lCStockLocMap.put( "MIN_REORDER_QT", "\'10\'" );
      lCStockLocMap.put( "STOCK_LOW_ACTN_CD", "\'SHIPREQ\'" );
      lCStockLocMap.put( "REORDER_QT", "\'17\'" );
      lCStockLocMap.put( "ALLOC_PCT", "\'1\'" );
      lCStockLocMap.put( "BATCH_SIZE", "\'2\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STOCK_LOC, lCStockLocMap ) );
      // These columns are not used to verify imported item
      lCStockLocMap.remove( "AIRPORT_CD" );
      lCStockLocMap.remove( "STOCK_NO_CD" );

      // run validation
      Assert.assertTrue( "Validation: ", runValidateMaterialsImport() == 1 );

      // run Import
      Assert.assertTrue( "Import: ", runImportMaterialsImport() == 1 );

      // verify Records were imported correctly
      lCPartNoMiscInfoMap = changeYesTo1AndNoto0( lCPartNoMiscInfoMap );
      Assert.assertTrue( "Verify values in EQP_PART_NO: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.EQP_PART_NO, lCPartNoMiscInfoMap ) ) );
      Assert.assertTrue( "Verify values in EQP_STOCK_NO: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.EQP_STOCK_NO, lCStockNoMap ) ) );
      Assert.assertTrue( "Verify values in INV_LOC_BIN: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.INV_LOC_BIN, lCBinPartMap ) ) );
      Assert.assertTrue( "Verify values in INV_LOC_STOCK: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.INV_LOC_STOCK, lCStockLocMap ) ) );
   }


   /**
    * Test #4.2 This test case will validate and import a serialized part using Materials_Import
    * Package. The difference between Test 4.1 and 4.2 is c_part_no_misc_info Y/N values
    *
    */
   @Test
   public void testImportMaterialsForSerPartTwo() {
      System.out.println(
            "======= Starting:  " + testName.getMethodName() + " ========================" );

      // inserting data into c_part_no_misc_info table
      Map<String, String> lCPartNoMiscInfoMap = new LinkedHashMap<>();

      lCPartNoMiscInfoMap.put( "PART_NO_OEM", "\'A0000012\'" );
      lCPartNoMiscInfoMap.put( "MANUFACT_REF", "\'1234567890\'" );
      lCPartNoMiscInfoMap.put( "SCRAP_RATE_PCT", "\'75.5\'" );
      lCPartNoMiscInfoMap.put( "ABC_CLASS_CD", "\'C\'" );
      lCPartNoMiscInfoMap.put( "CALC_ABC_CLASS_BOOL", "\'Y\'" );
      lCPartNoMiscInfoMap.put( "SOS_BOOL", "\'Y\'" );
      lCPartNoMiscInfoMap.put( "PMA_BOOL", "\'Y\'" );
      lCPartNoMiscInfoMap.put( "PROCURABLE_BOOL", "\'Y\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PART_NO_MISC_INFO,
            lCPartNoMiscInfoMap ) );

      // column has a different title in eqp_part_no
      lCPartNoMiscInfoMap.remove( "MANUFACT_REF" );
      lCPartNoMiscInfoMap.put( "MANUFACT_CD", "\'1234567890\'" );

      // inserting data into C_Stock_No table
      Map<String, String> lCStockNoMap = new LinkedHashMap<>();

      lCStockNoMap.put( "STOCK_NO_CD", "\'A0000012\'" );
      lCStockNoMap.put( "STOCK_NAME", "\'1234567890\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STOCK_NO, lCStockNoMap ) );

      // column has a different title in eqp_stock_no
      lCStockNoMap.remove( "STOCK_NAME" );
      lCStockNoMap.put( "STOCK_NO_NAME", "\'1234567890\'" );

      // inserting data into C_BIN_PART table
      Map<String, String> lCBinPartMap = new LinkedHashMap<>();

      lCBinPartMap.put( "AIRPORT_CD", "\'AIRPORT2\'" );
      lCBinPartMap.put( "STORE_CD", "\'STORE\'" );
      lCBinPartMap.put( "BIN_CD", "\'BIN2-1\'" );
      lCBinPartMap.put( "PART_NO_OEM", "\'A0000012\'" );
      lCBinPartMap.put( "MANUFACT_REF", "\'1234567890\'" );
      lCBinPartMap.put( "MIN_QT", "\'7\'" );
      lCBinPartMap.put( "MAX_QT", "\'23\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BIN_PART, lCBinPartMap ) );
      // These columns are not used to verify imported item
      lCBinPartMap.remove( "AIRPORT_CD" );
      lCBinPartMap.remove( "STORE_CD" );
      lCBinPartMap.remove( "BIN_CD" );
      lCBinPartMap.remove( "PART_NO_OEM" );
      lCBinPartMap.remove( "MANUFACT_REF" );

      // inserting data into C_STOCK_LOC table
      Map<String, String> lCStockLocMap = new LinkedHashMap<>();

      lCStockLocMap.put( "STOCK_NO_CD", "\'A0000012\'" );
      lCStockLocMap.put( "AIRPORT_CD", "\'AIRPORT2\'" );
      lCStockLocMap.put( "MIN_REORDER_QT", "\'10\'" );
      lCStockLocMap.put( "STOCK_LOW_ACTN_CD", "\'SHIPREQ\'" );
      lCStockLocMap.put( "REORDER_QT", "\'17\'" );
      lCStockLocMap.put( "ALLOC_PCT", "\'1\'" );
      lCStockLocMap.put( "BATCH_SIZE", "\'2\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STOCK_LOC, lCStockLocMap ) );
      // These columns are not used to verify imported item
      lCStockLocMap.remove( "AIRPORT_CD" );
      lCStockLocMap.remove( "STOCK_NO_CD" );

      // run validation
      Assert.assertTrue( "Validation: ", runValidateMaterialsImport() == 1 );

      // run Import
      Assert.assertTrue( "Import: ", runImportMaterialsImport() == 1 );

      // verify Records were imported correctly
      lCPartNoMiscInfoMap = changeYesTo1AndNoto0( lCPartNoMiscInfoMap );
      Assert.assertTrue( "Verify values in EQP_PART_NO: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.EQP_PART_NO, lCPartNoMiscInfoMap ) ) );
      Assert.assertTrue( "Verify values in EQP_STOCK_NO: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.EQP_STOCK_NO, lCStockNoMap ) ) );
      Assert.assertTrue( "Verify values in INV_LOC_BIN: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.INV_LOC_BIN, lCBinPartMap ) ) );
      Assert.assertTrue( "Verify values in INV_LOC_STOCK: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.INV_LOC_STOCK, lCStockLocMap ) ) );
   }


   /**
    * Test #5.1 This test case will validate and import a batch part using Materials_Import Package.
    *
    */

   @Test
   public void testImportMaterialsForBatchPart() {
      System.out.println(
            "======= Starting:  " + testName.getMethodName() + " ========================" );

      // inserting data into c_part_no_misc_info table
      Map<String, String> lCPartNoMiscInfoMap = new LinkedHashMap<>();

      lCPartNoMiscInfoMap.put( "PART_NO_OEM", "\'A0000009\'" );
      lCPartNoMiscInfoMap.put( "MANUFACT_REF", "\'10001\'" );
      lCPartNoMiscInfoMap.put( "SCRAP_RATE_PCT", "\'75.5\'" );
      lCPartNoMiscInfoMap.put( "ABC_CLASS_CD", "\'C\'" );
      lCPartNoMiscInfoMap.put( "CALC_ABC_CLASS_BOOL", "\'N\'" );
      lCPartNoMiscInfoMap.put( "SOS_BOOL", "\'N\'" );
      lCPartNoMiscInfoMap.put( "PMA_BOOL", "\'N\'" );
      lCPartNoMiscInfoMap.put( "PROCURABLE_BOOL", "\'N\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PART_NO_MISC_INFO,
            lCPartNoMiscInfoMap ) );

      // column has a different title in eqp_part_no
      lCPartNoMiscInfoMap.remove( "MANUFACT_REF" );
      lCPartNoMiscInfoMap.put( "MANUFACT_CD", "\'10001\'" );

      // inserting data into C_Stock_No table
      Map<String, String> lCStockNoMap = new LinkedHashMap<>();

      lCStockNoMap.put( "STOCK_NO_CD", "\'A0000009\'" );
      lCStockNoMap.put( "STOCK_NAME", "\'10001\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STOCK_NO, lCStockNoMap ) );

      // column has a different title in eqp_stock_no
      lCStockNoMap.remove( "STOCK_NAME" );
      lCStockNoMap.put( "STOCK_NO_NAME", "\'10001\'" );

      // inserting data into C_BIN_PART table
      Map<String, String> lCBinPartMap = new LinkedHashMap<>();

      lCBinPartMap.put( "AIRPORT_CD", "\'AIRPORT2\'" );
      lCBinPartMap.put( "STORE_CD", "\'STORE\'" );
      lCBinPartMap.put( "BIN_CD", "\'BIN2-1\'" );
      lCBinPartMap.put( "PART_NO_OEM", "\'A0000009\'" );
      lCBinPartMap.put( "MANUFACT_REF", "\'10001\'" );
      lCBinPartMap.put( "MIN_QT", "\'7\'" );
      lCBinPartMap.put( "MAX_QT", "\'23\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BIN_PART, lCBinPartMap ) );

      // These columns are not used to verify imported item
      lCBinPartMap.remove( "AIRPORT_CD" );
      lCBinPartMap.remove( "STORE_CD" );
      lCBinPartMap.remove( "BIN_CD" );
      lCBinPartMap.remove( "PART_NO_OEM" );
      lCBinPartMap.remove( "MANUFACT_REF" );

      // inserting data into C_STOCK_LOC table
      Map<String, String> lCStockLocMap = new LinkedHashMap<>();

      lCStockLocMap.put( "STOCK_NO_CD", "\'A0000009\'" );
      lCStockLocMap.put( "AIRPORT_CD", "\'AIRPORT2\'" );
      lCStockLocMap.put( "MIN_REORDER_QT", "\'10\'" );
      lCStockLocMap.put( "STOCK_LOW_ACTN_CD", "\'SHIPREQ\'" );
      lCStockLocMap.put( "REORDER_QT", "\'17\'" );
      lCStockLocMap.put( "ALLOC_PCT", "\'1\'" );
      lCStockLocMap.put( "BATCH_SIZE", "\'2\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STOCK_LOC, lCStockLocMap ) );
      // These columns are not used to verify imported item
      lCStockLocMap.remove( "AIRPORT_CD" );
      lCStockLocMap.remove( "STOCK_NO_CD" );

      // run validation
      Assert.assertTrue( "Validation: ", runValidateMaterialsImport() == 1 );

      // run Import
      Assert.assertTrue( "Import: ", runImportMaterialsImport() == 1 );

      // verify Records were imported correctly
      lCPartNoMiscInfoMap = changeYesTo1AndNoto0( lCPartNoMiscInfoMap );
      Assert.assertTrue( "Verify values in EQP_PART_NO: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.EQP_PART_NO, lCPartNoMiscInfoMap ) ) );
      Assert.assertTrue( "Verify values in EQP_STOCK_NO: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.EQP_STOCK_NO, lCStockNoMap ) ) );
      Assert.assertTrue( "Verify values in INV_LOC_BIN: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.INV_LOC_BIN, lCBinPartMap ) ) );
      Assert.assertTrue( "Verify values in INV_LOC_STOCK: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.INV_LOC_STOCK, lCStockLocMap ) ) );
   }


   /**
    * Test #5.2 This test case will validate and import a batch part using Materials_Import Package.
    * The difference between Test 5.1 and 5.2 is c_part_no_misc_info Y/N values.
    */
   @Test
   public void testImportMaterialsForBatchPartTwo() {
      System.out.println(
            "======= Starting:  " + testName.getMethodName() + " ========================" );

      // inserting data into c_part_no_misc_info table
      Map<String, String> lCPartNoMiscInfoMap = new LinkedHashMap<>();

      lCPartNoMiscInfoMap.put( "PART_NO_OEM", "\'A0000009\'" );
      lCPartNoMiscInfoMap.put( "MANUFACT_REF", "\'10001\'" );
      lCPartNoMiscInfoMap.put( "SCRAP_RATE_PCT", "\'75.5\'" );
      lCPartNoMiscInfoMap.put( "ABC_CLASS_CD", "\'B\'" );
      lCPartNoMiscInfoMap.put( "CALC_ABC_CLASS_BOOL", "\'Y\'" );
      lCPartNoMiscInfoMap.put( "SOS_BOOL", "\'Y\'" );
      lCPartNoMiscInfoMap.put( "PMA_BOOL", "\'Y\'" );
      lCPartNoMiscInfoMap.put( "PROCURABLE_BOOL", "\'Y\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PART_NO_MISC_INFO,
            lCPartNoMiscInfoMap ) );

      // column has a different title in eqp_part_no
      lCPartNoMiscInfoMap.remove( "MANUFACT_REF" );
      lCPartNoMiscInfoMap.put( "MANUFACT_CD", "\'10001\'" );

      // inserting data into C_Stock_No table
      Map<String, String> lCStockNoMap = new LinkedHashMap<>();

      lCStockNoMap.put( "STOCK_NO_CD", "\'A0000009\'" );
      lCStockNoMap.put( "STOCK_NAME", "\'10001\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STOCK_NO, lCStockNoMap ) );

      // column has a different title in eqp_stock_no
      lCStockNoMap.remove( "STOCK_NAME" );
      lCStockNoMap.put( "STOCK_NO_NAME", "\'10001\'" );

      // inserting data into C_BIN_PART table
      Map<String, String> lCBinPartMap = new LinkedHashMap<>();

      lCBinPartMap.put( "AIRPORT_CD", "\'AIRPORT2\'" );
      lCBinPartMap.put( "STORE_CD", "\'STORE\'" );
      lCBinPartMap.put( "BIN_CD", "\'BIN2-1\'" );
      lCBinPartMap.put( "PART_NO_OEM", "\'A0000009\'" );
      lCBinPartMap.put( "MANUFACT_REF", "\'10001\'" );
      lCBinPartMap.put( "MIN_QT", "\'7\'" );
      lCBinPartMap.put( "MAX_QT", "\'23\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BIN_PART, lCBinPartMap ) );

      // These columns are not used to verify imported item
      lCBinPartMap.remove( "AIRPORT_CD" );
      lCBinPartMap.remove( "STORE_CD" );
      lCBinPartMap.remove( "BIN_CD" );
      lCBinPartMap.remove( "PART_NO_OEM" );
      lCBinPartMap.remove( "MANUFACT_REF" );

      // inserting data into C_STOCK_LOC table
      Map<String, String> lCStockLocMap = new LinkedHashMap<>();

      lCStockLocMap.put( "STOCK_NO_CD", "\'A0000009\'" );
      lCStockLocMap.put( "AIRPORT_CD", "\'AIRPORT2\'" );
      lCStockLocMap.put( "MIN_REORDER_QT", "\'10\'" );
      lCStockLocMap.put( "STOCK_LOW_ACTN_CD", "\'SHIPREQ\'" );
      lCStockLocMap.put( "REORDER_QT", "\'17\'" );
      lCStockLocMap.put( "ALLOC_PCT", "\'1\'" );
      lCStockLocMap.put( "BATCH_SIZE", "\'2\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STOCK_LOC, lCStockLocMap ) );
      // These columns are not used to verify imported item
      lCStockLocMap.remove( "AIRPORT_CD" );
      lCStockLocMap.remove( "STOCK_NO_CD" );

      // run validation
      Assert.assertTrue( "Validation: ", runValidateMaterialsImport() == 1 );

      // run Import
      Assert.assertTrue( "Import: ", runImportMaterialsImport() == 1 );

      // verify Records were imported correctly

      lCPartNoMiscInfoMap = changeYesTo1AndNoto0( lCPartNoMiscInfoMap );
      Assert.assertTrue( "Verify values in EQP_PART_NO: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.EQP_PART_NO, lCPartNoMiscInfoMap ) ) );
      Assert.assertTrue( "Verify values in EQP_STOCK_NO: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.EQP_STOCK_NO, lCStockNoMap ) ) );
      Assert.assertTrue( "Verify values in INV_LOC_BIN: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.INV_LOC_BIN, lCBinPartMap ) ) );
      Assert.assertTrue( "Verify values in INV_LOC_STOCK: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.INV_LOC_STOCK, lCStockLocMap ) ) );
   }


   /**
    * Test #6.1 This Test case will validate and import a Kit Part using Materials_Import Package.
    *
    */

   @Test
   public void testImportMaterialsForKitPart() {
      System.out.println(
            "======= Starting:  " + testName.getMethodName() + " ========================" );

      // inserting data into c_part_no_misc_info table
      Map<String, String> lCPartNoMiscInfoMap = new LinkedHashMap<>();

      lCPartNoMiscInfoMap.put( "PART_NO_OEM", "\'KIT001\'" );
      lCPartNoMiscInfoMap.put( "MANUFACT_REF", "\'KITS\'" );
      lCPartNoMiscInfoMap.put( "QTY_UNIT_CD", "\'EA\'" );
      lCPartNoMiscInfoMap.put( "RECEIPT_INSP_BOOL", "\'N\'" );
      lCPartNoMiscInfoMap.put( "REPAIR_BOOL", "\'N\'" );
      lCPartNoMiscInfoMap.put( "ETOPS_APPROVED_BOOL", "\'N\'" );
      lCPartNoMiscInfoMap.put( "FINANCIAL_CLASS_CD", "\'KIT\'" );
      lCPartNoMiscInfoMap.put( "SCRAP_RATE_PCT", "\'75.5\'" );
      lCPartNoMiscInfoMap.put( "ABC_CLASS_CD", "\'D\'" );
      lCPartNoMiscInfoMap.put( "CALC_ABC_CLASS_BOOL", "\'Y\'" );
      lCPartNoMiscInfoMap.put( "SOS_BOOL", "\'N\'" );
      lCPartNoMiscInfoMap.put( "PMA_BOOL", "\'Y\'" );
      lCPartNoMiscInfoMap.put( "PROCURABLE_BOOL", "\'Y\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PART_NO_MISC_INFO,
            lCPartNoMiscInfoMap ) );

      // column has a different title in eqp_part_no or column does not exist in it
      lCPartNoMiscInfoMap.remove( "MANUFACT_REF" );
      lCPartNoMiscInfoMap.put( "MANUFACT_CD", "\'KITS\'" );
      lCPartNoMiscInfoMap.remove( "ETOPS_APPROVED_BOOL" );

      // inserting data into C_Stock_No table
      Map<String, String> lCStockNoMap = new LinkedHashMap<>();

      lCStockNoMap.put( "STOCK_NO_CD", "\'KIT001\'" );
      lCStockNoMap.put( "STOCK_NAME", "\'KITS\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STOCK_NO, lCStockNoMap ) );

      // column has a different title in eqp_stock_no
      lCStockNoMap.remove( "STOCK_NAME" );
      lCStockNoMap.put( "STOCK_NO_NAME", "\'KITS\'" );

      // inserting data into C_BIN_PART table
      Map<String, String> lCBinPartMap = new LinkedHashMap<>();

      lCBinPartMap.put( "AIRPORT_CD", "\'AIRPORT1\'" );
      lCBinPartMap.put( "STORE_CD", "\'STORE\'" );
      lCBinPartMap.put( "BIN_CD", "\'BIN1-1\'" );
      lCBinPartMap.put( "PART_NO_OEM", "\'KIT001\'" );
      lCBinPartMap.put( "MANUFACT_REF", "\'KITS\'" );
      lCBinPartMap.put( "MIN_QT", "\'7\'" );
      lCBinPartMap.put( "MAX_QT", "\'23\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BIN_PART, lCBinPartMap ) );

      // These columns are not used to verify imported item
      lCBinPartMap.remove( "AIRPORT_CD" );
      lCBinPartMap.remove( "STORE_CD" );
      lCBinPartMap.remove( "BIN_CD" );
      lCBinPartMap.remove( "PART_NO_OEM" );
      lCBinPartMap.remove( "MANUFACT_REF" );

      // inserting data into C_STOCK_LOC table
      Map<String, String> lCStockLocMap = new LinkedHashMap<>();

      lCStockLocMap.put( "STOCK_NO_CD", "\'KIT001\'" );
      lCStockLocMap.put( "AIRPORT_CD", "\'AIRPORT1\'" );
      lCStockLocMap.put( "MIN_REORDER_QT", "\'10\'" );
      lCStockLocMap.put( "STOCK_LOW_ACTN_CD", "\'SHIPREQ\'" );
      lCStockLocMap.put( "REORDER_QT", "\'17\'" );
      lCStockLocMap.put( "ALLOC_PCT", "\'1\'" );
      lCStockLocMap.put( "BATCH_SIZE", "\'5\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STOCK_LOC, lCStockLocMap ) );

      // These columns are not used to verify imported item
      lCStockLocMap.remove( "AIRPORT_CD" );
      lCStockLocMap.remove( "STOCK_NO_CD" );

      // run validation
      Assert.assertTrue( "Validation: ", runValidateMaterialsImport() == 1 );

      // run Import
      Assert.assertTrue( "Import: ", runImportMaterialsImport() == 1 );

      // verify Records were imported correctly
      lCPartNoMiscInfoMap = changeYesTo1AndNoto0( lCPartNoMiscInfoMap );
      Assert.assertTrue( "Verify values in EQP_PART_NO: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.EQP_PART_NO, lCPartNoMiscInfoMap ) ) );
      Assert.assertTrue( "Verify values in EQP_STOCK_NO: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.EQP_STOCK_NO, lCStockNoMap ) ) );
      Assert.assertTrue( "Verify values in INV_LOC_BIN: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.INV_LOC_BIN, lCBinPartMap ) ) );
      Assert.assertTrue( "Verify values in INV_LOC_STOCK: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.INV_LOC_STOCK, lCStockLocMap ) ) );
   }


   /**
    * Test #6.2 This Test case will validate and import a Kit Part using Materials_Import Package.
    * The only difference between Test 6.1 and 6.2 this values for PMA_BOOL and PROCURABLE_BOOL.
    */
   @Test
   public void testImportMaterialsForKitPartTwo() {
      System.out.println(
            "======= Starting:  " + testName.getMethodName() + " ========================" );

      // inserting data into c_part_no_misc_info table
      Map<String, String> lCPartNoMiscInfoMap = new LinkedHashMap<>();

      lCPartNoMiscInfoMap.put( "PART_NO_OEM", "\'KIT001\'" );
      lCPartNoMiscInfoMap.put( "MANUFACT_REF", "\'KITS\'" );
      lCPartNoMiscInfoMap.put( "SCRAP_RATE_PCT", "\'75.5\'" );
      lCPartNoMiscInfoMap.put( "QTY_UNIT_CD", "\'EA\'" );
      lCPartNoMiscInfoMap.put( "RECEIPT_INSP_BOOL", "\'N\'" );
      lCPartNoMiscInfoMap.put( "REPAIR_BOOL", "\'N\'" );
      lCPartNoMiscInfoMap.put( "ETOPS_APPROVED_BOOL", "\'N\'" );
      lCPartNoMiscInfoMap.put( "FINANCIAL_CLASS_CD", "\'KIT\'" );
      lCPartNoMiscInfoMap.put( "CALC_ABC_CLASS_BOOL", "\'Y\'" );
      lCPartNoMiscInfoMap.put( "SOS_BOOL", "\'N\'" );
      lCPartNoMiscInfoMap.put( "PMA_BOOL", "\'N\'" );
      lCPartNoMiscInfoMap.put( "PROCURABLE_BOOL", "\'N\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PART_NO_MISC_INFO,
            lCPartNoMiscInfoMap ) );

      // column has a different title in eqp_part_no or column does not exist in it
      lCPartNoMiscInfoMap.remove( "MANUFACT_REF" );
      lCPartNoMiscInfoMap.put( "MANUFACT_CD", "\'KITS\'" );
      lCPartNoMiscInfoMap.remove( "ETOPS_APPROVED_BOOL" );

      // inserting data into C_Stock_No table
      Map<String, String> lCStockNoMap = new LinkedHashMap<>();

      lCStockNoMap.put( "STOCK_NO_CD", "\'KIT001\'" );
      lCStockNoMap.put( "STOCK_NAME", "\'KITS\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STOCK_NO, lCStockNoMap ) );

      // column has a different title in eqp_stock_no
      lCStockNoMap.remove( "STOCK_NAME" );
      lCStockNoMap.put( "STOCK_NO_NAME", "\'KITS\'" );

      // inserting data into C_BIN_PART table
      Map<String, String> lCBinPartMap = new LinkedHashMap<>();

      lCBinPartMap.put( "AIRPORT_CD", "\'AIRPORT1\'" );
      lCBinPartMap.put( "STORE_CD", "\'STORE\'" );
      lCBinPartMap.put( "BIN_CD", "\'BIN1-1\'" );
      lCBinPartMap.put( "PART_NO_OEM", "\'KIT001\'" );
      lCBinPartMap.put( "MANUFACT_REF", "\'KITS\'" );
      lCBinPartMap.put( "MIN_QT", "\'7\'" );
      lCBinPartMap.put( "MAX_QT", "\'23\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BIN_PART, lCBinPartMap ) );

      // These columns are not used to verify imported item
      lCBinPartMap.remove( "AIRPORT_CD" );
      lCBinPartMap.remove( "STORE_CD" );
      lCBinPartMap.remove( "BIN_CD" );
      lCBinPartMap.remove( "PART_NO_OEM" );
      lCBinPartMap.remove( "MANUFACT_REF" );

      // inserting data into C_STOCK_LOC table
      Map<String, String> lCStockLocMap = new LinkedHashMap<>();

      lCStockLocMap.put( "STOCK_NO_CD", "\'KIT001\'" );
      lCStockLocMap.put( "AIRPORT_CD", "\'AIRPORT1\'" );
      lCStockLocMap.put( "MIN_REORDER_QT", "\'10\'" );
      lCStockLocMap.put( "STOCK_LOW_ACTN_CD", "\'SHIPREQ\'" );
      lCStockLocMap.put( "REORDER_QT", "\'17\'" );
      lCStockLocMap.put( "ALLOC_PCT", "\'1\'" );
      lCStockLocMap.put( "BATCH_SIZE", "\'5\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STOCK_LOC, lCStockLocMap ) );

      // These columns are not used to verify imported item
      lCStockLocMap.remove( "AIRPORT_CD" );
      lCStockLocMap.remove( "STOCK_NO_CD" );

      // run validation
      Assert.assertTrue( "Validation: ", runValidateMaterialsImport() == 1 );

      // run Import
      Assert.assertTrue( "Import: ", runImportMaterialsImport() == 1 );

      // verify Records were imported correctly
      lCPartNoMiscInfoMap = changeYesTo1AndNoto0( lCPartNoMiscInfoMap );
      Assert.assertTrue( "Verify values in EQP_PART_NO: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.EQP_PART_NO, lCPartNoMiscInfoMap ) ) );
      Assert.assertTrue( "Verify values in EQP_STOCK_NO: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.EQP_STOCK_NO, lCStockNoMap ) ) );
      Assert.assertTrue( "Verify values in INV_LOC_BIN: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.INV_LOC_BIN, lCBinPartMap ) ) );
      Assert.assertTrue( "Verify values in INV_LOC_STOCK: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.INV_LOC_STOCK, lCStockLocMap ) ) );
   }


   /**
    * Test #7.1 This test case will validate and import a BL_ALTERNATE_UOM part using
    * Materials_Import Package.
    *
    */
   @Test
   public void testImportMaterialsBL_ALTERNATE_UOM() {
      System.out.println(
            "======= Starting:  " + testName.getMethodName() + " ========================" );

      // Inserting data into BL_ALTERNATE_UOM
      Map<String, String> lBlAlternateUomMap = new LinkedHashMap<>();

      lBlAlternateUomMap.put( "PART_NO_OEM", "\'A0000009\'" );
      lBlAlternateUomMap.put( "MANUFACT_CD", "\'10001\'" );
      lBlAlternateUomMap.put( "QTY_UNIT_CD", "\'AY\'" );
      lBlAlternateUomMap.put( "CONVERSION_FACTOR", "\'1\'" );
      lBlAlternateUomMap.put( "PROCESSING_NOTE", "\'This is test data for automation test\'" );

      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_ALTERNATE_UOM, lBlAlternateUomMap ) );

      lBlAlternateUomMap.remove( "PART_NO_OEM" );
      lBlAlternateUomMap.remove( "MANUFACT_CD" );
      lBlAlternateUomMap.remove( "PROCESSING_NOTE" );
      // Different name from staging table
      lBlAlternateUomMap.remove( "CONVERSION_FACTOR" );
      lBlAlternateUomMap.put( "QTY_CONVERT_QT", "\'1\'" );

      // inserting data into c_part_no_misc_info table
      Map<String, String> lCPartNoMiscInfoMap = new LinkedHashMap<>();

      lCPartNoMiscInfoMap.put( "PART_NO_OEM", "\'A0000009\'" );
      lCPartNoMiscInfoMap.put( "MANUFACT_REF", "\'10001\'" );
      lCPartNoMiscInfoMap.put( "SCRAP_RATE_PCT", "\'75.5\'" );
      lCPartNoMiscInfoMap.put( "ABC_CLASS_CD", "\'B\'" );
      lCPartNoMiscInfoMap.put( "CALC_ABC_CLASS_BOOL", "\'Y\'" );
      lCPartNoMiscInfoMap.put( "SOS_BOOL", "\'Y\'" );
      lCPartNoMiscInfoMap.put( "PMA_BOOL", "\'Y\'" );
      lCPartNoMiscInfoMap.put( "PROCURABLE_BOOL", "\'Y\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PART_NO_MISC_INFO,
            lCPartNoMiscInfoMap ) );

      // column has a different title in eqp_part_no
      lCPartNoMiscInfoMap.remove( "MANUFACT_REF" );
      lCPartNoMiscInfoMap.put( "MANUFACT_CD", "\'10001\'" );

      // inserting data into C_Stock_No table
      Map<String, String> lCStockNoMap = new LinkedHashMap<>();

      lCStockNoMap.put( "STOCK_NO_CD", "\'A0000009\'" );
      lCStockNoMap.put( "STOCK_NAME", "\'10001\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STOCK_NO, lCStockNoMap ) );

      // column has a different title in eqp_stock_no
      lCStockNoMap.remove( "STOCK_NAME" );
      lCStockNoMap.put( "STOCK_NO_NAME", "\'10001\'" );

      // inserting data into C_BIN_PART table
      Map<String, String> lCBinPartMap = new LinkedHashMap<>();

      lCBinPartMap.put( "AIRPORT_CD", "\'AIRPORT2\'" );
      lCBinPartMap.put( "STORE_CD", "\'STORE\'" );
      lCBinPartMap.put( "BIN_CD", "\'BIN2-1\'" );
      lCBinPartMap.put( "PART_NO_OEM", "\'A0000009\'" );
      lCBinPartMap.put( "MANUFACT_REF", "\'10001\'" );
      lCBinPartMap.put( "MIN_QT", "\'7\'" );
      lCBinPartMap.put( "MAX_QT", "\'23\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BIN_PART, lCBinPartMap ) );

      // These columns are not used to verify imported item
      lCBinPartMap.remove( "AIRPORT_CD" );
      lCBinPartMap.remove( "STORE_CD" );
      lCBinPartMap.remove( "BIN_CD" );
      lCBinPartMap.remove( "PART_NO_OEM" );
      lCBinPartMap.remove( "MANUFACT_REF" );

      // inserting data into C_STOCK_LOC table
      Map<String, String> lCStockLocMap = new LinkedHashMap<>();

      lCStockLocMap.put( "STOCK_NO_CD", "\'A0000009\'" );
      lCStockLocMap.put( "AIRPORT_CD", "\'AIRPORT2\'" );
      lCStockLocMap.put( "MIN_REORDER_QT", "\'10\'" );
      lCStockLocMap.put( "STOCK_LOW_ACTN_CD", "\'SHIPREQ\'" );
      lCStockLocMap.put( "REORDER_QT", "\'17\'" );
      lCStockLocMap.put( "ALLOC_PCT", "\'1\'" );
      lCStockLocMap.put( "BATCH_SIZE", "\'2\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STOCK_LOC, lCStockLocMap ) );
      // These columns are not used to verify imported item
      lCStockLocMap.remove( "AIRPORT_CD" );
      lCStockLocMap.remove( "STOCK_NO_CD" );

      // run validation
      Assert.assertTrue( "Validation: ", runValidateMaterialsImport() == 1 );

      // run Import
      Assert.assertTrue( "Import: ", runImportMaterialsImport() == 1 );

      // verify Records were imported correctly
      lCPartNoMiscInfoMap = changeYesTo1AndNoto0( lCPartNoMiscInfoMap );
      Assert.assertTrue( "Verify values in EQP_PART_NO: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.EQP_PART_NO, lCPartNoMiscInfoMap ) ) );
      Assert.assertTrue( "Verify values in EQP_STOCK_NO: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.EQP_STOCK_NO, lCStockNoMap ) ) );
      Assert.assertTrue( "Verify values in INV_LOC_BIN: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.INV_LOC_BIN, lCBinPartMap ) ) );
      Assert.assertTrue( "Verify values in INV_LOC_STOCK: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.INV_LOC_STOCK, lCStockLocMap ) ) );
      Assert.assertTrue( "Verify values in EQP_PART_ALT_UNIT: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.EQP_PART_ALT_UNIT, lBlAlternateUomMap ) ) );
   }


   /**
    * This test is to verify OPER-24321: The location columns in baseline loader have a shorter
    * length than the location code in Maintenix.  This causes export to fail when a location code
    * exceeds the short length in baseline loader. Since there is no data setup for 2000 length
    * airport code, test uses error code to validate the airport cd can accept 2000 length.
    *
    */
   @Test
   public void testOPER24321_LongAirport_Validation() {
      System.out.println(
            "======= Starting:  " + testName.getMethodName() + " ========================" );

      // inserting data into c_part_no_misc_info table
      Map<String, String> lCPartNoMiscInfoMap = new LinkedHashMap<>();

      lCPartNoMiscInfoMap.put( "PART_NO_OEM", "\'A0000001\'" );
      lCPartNoMiscInfoMap.put( "MANUFACT_REF", "\'10001\'" );
      lCPartNoMiscInfoMap.put( "SCRAP_RATE_PCT", "\'75.5\'" );
      lCPartNoMiscInfoMap.put( "ABC_CLASS_CD", "\'B\'" );
      lCPartNoMiscInfoMap.put( "CALC_ABC_CLASS_BOOL", "\'N\'" );
      lCPartNoMiscInfoMap.put( "SOS_BOOL", "\'N\'" );
      lCPartNoMiscInfoMap.put( "PMA_BOOL", "\'N\'" );
      lCPartNoMiscInfoMap.put( "PROCURABLE_BOOL", "\'N\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PART_NO_MISC_INFO,
            lCPartNoMiscInfoMap ) );

      // column has a different title in eqp_part_no
      lCPartNoMiscInfoMap.remove( "MANUFACT_REF" );
      lCPartNoMiscInfoMap.put( "MANUFACT_CD", "\'10001\'" );

      // inserting data into C_Stock_No table
      Map<String, String> lCStockNoMap = new LinkedHashMap<>();

      lCStockNoMap.put( "STOCK_NO_CD", "\'A0000001\'" );
      lCStockNoMap.put( "STOCK_NAME", "\'10001\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STOCK_NO, lCStockNoMap ) );

      // column has a different title in eqp_stock_no
      lCStockNoMap.remove( "STOCK_NAME" );
      lCStockNoMap.put( "STOCK_NO_NAME", "\'10001\'" );

      // inserting data into C_BIN_PART table
      Map<String, String> lCBinPartMap = new LinkedHashMap<>();

      // lCBinPartMap.put( "AIRPORT_CD", "\'AIRPORT1\'" );
      lCBinPartMap.put( "AIRPORT_CD", "'" + iAirport1CD_24321 + "'" );
      lCBinPartMap.put( "STORE_CD", "\'STORE\'" );
      lCBinPartMap.put( "BIN_CD", "\'BIN1-1\'" );
      lCBinPartMap.put( "PART_NO_OEM", "\'A0000001\'" );
      lCBinPartMap.put( "MANUFACT_REF", "\'10001\'" );
      lCBinPartMap.put( "MIN_QT", "\'7\'" );
      lCBinPartMap.put( "MAX_QT", "\'23\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BIN_PART, lCBinPartMap ) );

      // These columns are not used to verify imported item
      lCBinPartMap.remove( "AIRPORT_CD" );
      lCBinPartMap.remove( "STORE_CD" );
      lCBinPartMap.remove( "BIN_CD" );
      lCBinPartMap.remove( "PART_NO_OEM" );
      lCBinPartMap.remove( "MANUFACT_REF" );

      // inserting data into C_STOCK_LOC table
      Map<String, String> lCStockLocMap = new LinkedHashMap<>();

      lCStockLocMap.put( "STOCK_NO_CD", "\'A0000001\'" );
      // lCStockLocMap.put( "AIRPORT_CD", "\'AIRPORT1\'" );
      lCStockLocMap.put( "AIRPORT_CD", "'" + iAirport1CD_24321 + "'" );
      lCStockLocMap.put( "MIN_REORDER_QT", "\'10\'" );
      lCStockLocMap.put( "STOCK_LOW_ACTN_CD", "\'SHIPREQ\'" );
      lCStockLocMap.put( "REORDER_QT", "\'17\'" );
      lCStockLocMap.put( "ALLOC_PCT", "\'1\'" );
      lCStockLocMap.put( "BATCH_SIZE", "\'5\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STOCK_LOC, lCStockLocMap ) );

      // These columns are not used to verify imported item
      lCStockLocMap.remove( "AIRPORT_CD" );
      lCStockLocMap.remove( "STOCK_NO_CD" );

      // run validation
      Assert.assertTrue( "Validation: ", runValidateMaterialsImport() == -1 );

      // Validate CFGREQ-12154 error
      validateErrorCode( "CFGMAT-12300" );
      validateErrorCode( "CFGMAT-12201" );

   }


   /**
    * This test is to verify OPER-24321: The location columns in baseline loader have a shorter
    * length than the location code in Maintenix.  This causes export to fail when a location code
    * exceeds the short length in baseline loader. Since there is no data setup for 2000 length
    * airport code, test uses error code to validate the airport cd can accept 2000 length.
    *
    */
   @Test
   public void testOPER24321_LongStore_Validation() {
      System.out.println(
            "======= Starting:  " + testName.getMethodName() + " ========================" );

      // inserting data into c_part_no_misc_info table
      Map<String, String> lCPartNoMiscInfoMap = new LinkedHashMap<>();

      lCPartNoMiscInfoMap.put( "PART_NO_OEM", "\'A0000001\'" );
      lCPartNoMiscInfoMap.put( "MANUFACT_REF", "\'10001\'" );
      lCPartNoMiscInfoMap.put( "SCRAP_RATE_PCT", "\'75.5\'" );
      lCPartNoMiscInfoMap.put( "ABC_CLASS_CD", "\'B\'" );
      lCPartNoMiscInfoMap.put( "CALC_ABC_CLASS_BOOL", "\'N\'" );
      lCPartNoMiscInfoMap.put( "SOS_BOOL", "\'N\'" );
      lCPartNoMiscInfoMap.put( "PMA_BOOL", "\'N\'" );
      lCPartNoMiscInfoMap.put( "PROCURABLE_BOOL", "\'N\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PART_NO_MISC_INFO,
            lCPartNoMiscInfoMap ) );

      // column has a different title in eqp_part_no
      lCPartNoMiscInfoMap.remove( "MANUFACT_REF" );
      lCPartNoMiscInfoMap.put( "MANUFACT_CD", "\'10001\'" );

      // inserting data into C_Stock_No table
      Map<String, String> lCStockNoMap = new LinkedHashMap<>();

      lCStockNoMap.put( "STOCK_NO_CD", "\'A0000001\'" );
      lCStockNoMap.put( "STOCK_NAME", "\'10001\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STOCK_NO, lCStockNoMap ) );

      // column has a different title in eqp_stock_no
      lCStockNoMap.remove( "STOCK_NAME" );
      lCStockNoMap.put( "STOCK_NO_NAME", "\'10001\'" );

      // inserting data into C_BIN_PART table
      Map<String, String> lCBinPartMap = new LinkedHashMap<>();

      lCBinPartMap.put( "AIRPORT_CD", "\'AIRPORT1\'" );
      // lCBinPartMap.put( "STORE_CD", "\'STORE\'" );
      lCBinPartMap.put( "STORE_CD", "'" + iStoreCD_24321 + "'" );
      lCBinPartMap.put( "BIN_CD", "\'BIN1-1\'" );
      lCBinPartMap.put( "PART_NO_OEM", "\'A0000001\'" );
      lCBinPartMap.put( "MANUFACT_REF", "\'10001\'" );
      lCBinPartMap.put( "MIN_QT", "\'7\'" );
      lCBinPartMap.put( "MAX_QT", "\'23\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BIN_PART, lCBinPartMap ) );

      // These columns are not used to verify imported item
      lCBinPartMap.remove( "AIRPORT_CD" );
      lCBinPartMap.remove( "STORE_CD" );
      lCBinPartMap.remove( "BIN_CD" );
      lCBinPartMap.remove( "PART_NO_OEM" );
      lCBinPartMap.remove( "MANUFACT_REF" );

      // inserting data into C_STOCK_LOC table
      Map<String, String> lCStockLocMap = new LinkedHashMap<>();

      lCStockLocMap.put( "STOCK_NO_CD", "\'A0000001\'" );
      lCStockLocMap.put( "AIRPORT_CD", "\'AIRPORT1\'" );
      lCStockLocMap.put( "MIN_REORDER_QT", "\'10\'" );
      lCStockLocMap.put( "STOCK_LOW_ACTN_CD", "\'SHIPREQ\'" );
      lCStockLocMap.put( "REORDER_QT", "\'17\'" );
      lCStockLocMap.put( "ALLOC_PCT", "\'1\'" );
      lCStockLocMap.put( "BATCH_SIZE", "\'5\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STOCK_LOC, lCStockLocMap ) );

      // These columns are not used to verify imported item
      lCStockLocMap.remove( "AIRPORT_CD" );
      lCStockLocMap.remove( "STOCK_NO_CD" );

      // run validation
      Assert.assertTrue( "Validation: ", runValidateMaterialsImport() == -1 );

      // Validate CFGREQ-12154 error
      validateErrorCode( "CFGMAT-12301" );
   }


   /**
    * This test is to verify OPER-24321: The location columns in baseline loader have a shorter
    * length than the location code in Maintenix.  This causes export to fail when a location code
    * exceeds the short length in baseline loader. Since there is no data setup for 2000 length
    * airport code, test uses error code to validate the airport cd can accept 2000 length.
    *
    */
   @Test
   public void testOPER24321_LongBin_Validation() {
      System.out.println(
            "======= Starting:  " + testName.getMethodName() + " ========================" );

      // inserting data into c_part_no_misc_info table
      Map<String, String> lCPartNoMiscInfoMap = new LinkedHashMap<>();

      lCPartNoMiscInfoMap.put( "PART_NO_OEM", "\'A0000001\'" );
      lCPartNoMiscInfoMap.put( "MANUFACT_REF", "\'10001\'" );
      lCPartNoMiscInfoMap.put( "SCRAP_RATE_PCT", "\'75.5\'" );
      lCPartNoMiscInfoMap.put( "ABC_CLASS_CD", "\'B\'" );
      lCPartNoMiscInfoMap.put( "CALC_ABC_CLASS_BOOL", "\'N\'" );
      lCPartNoMiscInfoMap.put( "SOS_BOOL", "\'N\'" );
      lCPartNoMiscInfoMap.put( "PMA_BOOL", "\'N\'" );
      lCPartNoMiscInfoMap.put( "PROCURABLE_BOOL", "\'N\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PART_NO_MISC_INFO,
            lCPartNoMiscInfoMap ) );

      // column has a different title in eqp_part_no
      lCPartNoMiscInfoMap.remove( "MANUFACT_REF" );
      lCPartNoMiscInfoMap.put( "MANUFACT_CD", "\'10001\'" );

      // inserting data into C_Stock_No table
      Map<String, String> lCStockNoMap = new LinkedHashMap<>();

      lCStockNoMap.put( "STOCK_NO_CD", "\'A0000001\'" );
      lCStockNoMap.put( "STOCK_NAME", "\'10001\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STOCK_NO, lCStockNoMap ) );

      // column has a different title in eqp_stock_no
      lCStockNoMap.remove( "STOCK_NAME" );
      lCStockNoMap.put( "STOCK_NO_NAME", "\'10001\'" );

      // inserting data into C_BIN_PART table
      Map<String, String> lCBinPartMap = new LinkedHashMap<>();

      lCBinPartMap.put( "AIRPORT_CD", "\'AIRPORT1\'" );
      lCBinPartMap.put( "STORE_CD", "\'STORE\'" );
      lCBinPartMap.put( "BIN_CD", "'" + iBinCD_24321 + "'" );
      // lCBinPartMap.put( "BIN_CD", "\'BIN1-1\'" );
      lCBinPartMap.put( "PART_NO_OEM", "\'A0000001\'" );
      lCBinPartMap.put( "MANUFACT_REF", "\'10001\'" );
      lCBinPartMap.put( "MIN_QT", "\'7\'" );
      lCBinPartMap.put( "MAX_QT", "\'23\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BIN_PART, lCBinPartMap ) );

      // These columns are not used to verify imported item
      lCBinPartMap.remove( "AIRPORT_CD" );
      lCBinPartMap.remove( "STORE_CD" );
      lCBinPartMap.remove( "BIN_CD" );
      lCBinPartMap.remove( "PART_NO_OEM" );
      lCBinPartMap.remove( "MANUFACT_REF" );

      // inserting data into C_STOCK_LOC table
      Map<String, String> lCStockLocMap = new LinkedHashMap<>();

      lCStockLocMap.put( "STOCK_NO_CD", "\'A0000001\'" );
      lCStockLocMap.put( "AIRPORT_CD", "\'AIRPORT1\'" );
      lCStockLocMap.put( "MIN_REORDER_QT", "\'10\'" );
      lCStockLocMap.put( "STOCK_LOW_ACTN_CD", "\'SHIPREQ\'" );
      lCStockLocMap.put( "REORDER_QT", "\'17\'" );
      lCStockLocMap.put( "ALLOC_PCT", "\'1\'" );
      lCStockLocMap.put( "BATCH_SIZE", "\'5\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STOCK_LOC, lCStockLocMap ) );

      // These columns are not used to verify imported item
      lCStockLocMap.remove( "AIRPORT_CD" );
      lCStockLocMap.remove( "STOCK_NO_CD" );

      // run validation
      Assert.assertTrue( "Validation: ", runValidateMaterialsImport() == -1 );

      // Validate CFGREQ-12154 error
      validateErrorCode( "CFGMAT-12302" );
   }


   /**
    * This test is to verify CFGMAT-12317: FINANCIAL_CLASS_CD cannot be repairable or rotable for a
    * part with an inventory class of BATCH. when FINANCIAL_CLASS_CD=REPAIRABLE
    *
    */

   @Test
   public void test_CFGMAT_12317_1() {
      System.out.println(
            "======= Starting:  " + testName.getMethodName() + " ========================" );

      // inserting data into c_part_no_misc_info table
      Map<String, String> lCPartNoMiscInfoMap = new LinkedHashMap<>();

      lCPartNoMiscInfoMap.put( "PART_NO_OEM", "\'A0000009\'" );
      lCPartNoMiscInfoMap.put( "MANUFACT_REF", "\'10001\'" );
      lCPartNoMiscInfoMap.put( "SCRAP_RATE_PCT", "\'75.5\'" );
      lCPartNoMiscInfoMap.put( "ABC_CLASS_CD", "\'C\'" );
      lCPartNoMiscInfoMap.put( "CALC_ABC_CLASS_BOOL", "\'N\'" );
      lCPartNoMiscInfoMap.put( "SOS_BOOL", "\'N\'" );
      lCPartNoMiscInfoMap.put( "PMA_BOOL", "\'N\'" );
      lCPartNoMiscInfoMap.put( "PROCURABLE_BOOL", "\'N\'" );
      lCPartNoMiscInfoMap.put( "FINANCIAL_CLASS_CD", "'REPAIRABLE'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PART_NO_MISC_INFO,
            lCPartNoMiscInfoMap ) );

      // column has a different title in eqp_part_no
      lCPartNoMiscInfoMap.remove( "MANUFACT_REF" );
      lCPartNoMiscInfoMap.put( "MANUFACT_CD", "\'10001\'" );

      // inserting data into C_Stock_No table
      Map<String, String> lCStockNoMap = new LinkedHashMap<>();

      lCStockNoMap.put( "STOCK_NO_CD", "\'A0000009\'" );
      lCStockNoMap.put( "STOCK_NAME", "\'10001\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STOCK_NO, lCStockNoMap ) );

      // column has a different title in eqp_stock_no
      lCStockNoMap.remove( "STOCK_NAME" );
      lCStockNoMap.put( "STOCK_NO_NAME", "\'10001\'" );

      // inserting data into C_BIN_PART table
      Map<String, String> lCBinPartMap = new LinkedHashMap<>();

      lCBinPartMap.put( "AIRPORT_CD", "\'AIRPORT2\'" );
      lCBinPartMap.put( "STORE_CD", "\'STORE\'" );
      lCBinPartMap.put( "BIN_CD", "\'BIN2-1\'" );
      lCBinPartMap.put( "PART_NO_OEM", "\'A0000009\'" );
      lCBinPartMap.put( "MANUFACT_REF", "\'10001\'" );
      lCBinPartMap.put( "MIN_QT", "\'7\'" );
      lCBinPartMap.put( "MAX_QT", "\'23\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BIN_PART, lCBinPartMap ) );

      // These columns are not used to verify imported item
      lCBinPartMap.remove( "AIRPORT_CD" );
      lCBinPartMap.remove( "STORE_CD" );
      lCBinPartMap.remove( "BIN_CD" );
      lCBinPartMap.remove( "PART_NO_OEM" );
      lCBinPartMap.remove( "MANUFACT_REF" );

      // inserting data into C_STOCK_LOC table
      Map<String, String> lCStockLocMap = new LinkedHashMap<>();

      lCStockLocMap.put( "STOCK_NO_CD", "\'A0000009\'" );
      lCStockLocMap.put( "AIRPORT_CD", "\'AIRPORT2\'" );
      lCStockLocMap.put( "MIN_REORDER_QT", "\'10\'" );
      lCStockLocMap.put( "STOCK_LOW_ACTN_CD", "\'SHIPREQ\'" );
      lCStockLocMap.put( "REORDER_QT", "\'17\'" );
      lCStockLocMap.put( "ALLOC_PCT", "\'1\'" );
      lCStockLocMap.put( "BATCH_SIZE", "\'2\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STOCK_LOC, lCStockLocMap ) );
      // These columns are not used to verify imported item
      lCStockLocMap.remove( "AIRPORT_CD" );
      lCStockLocMap.remove( "STOCK_NO_CD" );

      // run validation
      Assert.assertTrue( "Validation: ", runValidateMaterialsImport() == -1 );

      // Validate CFGMAT-12317 error
      validateErrorCode( "CFGMAT-12317" );
   }


   /**
    * This test is to verify CFGMAT-12317: FINANCIAL_CLASS_CD cannot be repairable or rotable for a
    * part with an inventory class of BATCH. when FINANCIAL_CLASS_CD=ROTABLE
    *
    */

   @Test
   public void test_CFGMAT_12317_2() {
      System.out.println(
            "======= Starting:  " + testName.getMethodName() + " ========================" );

      // inserting data into c_part_no_misc_info table
      Map<String, String> lCPartNoMiscInfoMap = new LinkedHashMap<>();

      lCPartNoMiscInfoMap.put( "PART_NO_OEM", "\'A0000009\'" );
      lCPartNoMiscInfoMap.put( "MANUFACT_REF", "\'10001\'" );
      lCPartNoMiscInfoMap.put( "SCRAP_RATE_PCT", "\'75.5\'" );
      lCPartNoMiscInfoMap.put( "ABC_CLASS_CD", "\'C\'" );
      lCPartNoMiscInfoMap.put( "CALC_ABC_CLASS_BOOL", "\'N\'" );
      lCPartNoMiscInfoMap.put( "SOS_BOOL", "\'N\'" );
      lCPartNoMiscInfoMap.put( "PMA_BOOL", "\'N\'" );
      lCPartNoMiscInfoMap.put( "PROCURABLE_BOOL", "\'N\'" );
      lCPartNoMiscInfoMap.put( "FINANCIAL_CLASS_CD", "'ROTABLE'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PART_NO_MISC_INFO,
            lCPartNoMiscInfoMap ) );

      // column has a different title in eqp_part_no
      lCPartNoMiscInfoMap.remove( "MANUFACT_REF" );
      lCPartNoMiscInfoMap.put( "MANUFACT_CD", "\'10001\'" );

      // inserting data into C_Stock_No table
      Map<String, String> lCStockNoMap = new LinkedHashMap<>();

      lCStockNoMap.put( "STOCK_NO_CD", "\'A0000009\'" );
      lCStockNoMap.put( "STOCK_NAME", "\'10001\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STOCK_NO, lCStockNoMap ) );

      // column has a different title in eqp_stock_no
      lCStockNoMap.remove( "STOCK_NAME" );
      lCStockNoMap.put( "STOCK_NO_NAME", "\'10001\'" );

      // inserting data into C_BIN_PART table
      Map<String, String> lCBinPartMap = new LinkedHashMap<>();

      lCBinPartMap.put( "AIRPORT_CD", "\'AIRPORT2\'" );
      lCBinPartMap.put( "STORE_CD", "\'STORE\'" );
      lCBinPartMap.put( "BIN_CD", "\'BIN2-1\'" );
      lCBinPartMap.put( "PART_NO_OEM", "\'A0000009\'" );
      lCBinPartMap.put( "MANUFACT_REF", "\'10001\'" );
      lCBinPartMap.put( "MIN_QT", "\'7\'" );
      lCBinPartMap.put( "MAX_QT", "\'23\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BIN_PART, lCBinPartMap ) );

      // These columns are not used to verify imported item
      lCBinPartMap.remove( "AIRPORT_CD" );
      lCBinPartMap.remove( "STORE_CD" );
      lCBinPartMap.remove( "BIN_CD" );
      lCBinPartMap.remove( "PART_NO_OEM" );
      lCBinPartMap.remove( "MANUFACT_REF" );

      // inserting data into C_STOCK_LOC table
      Map<String, String> lCStockLocMap = new LinkedHashMap<>();

      lCStockLocMap.put( "STOCK_NO_CD", "\'A0000009\'" );
      lCStockLocMap.put( "AIRPORT_CD", "\'AIRPORT2\'" );
      lCStockLocMap.put( "MIN_REORDER_QT", "\'10\'" );
      lCStockLocMap.put( "STOCK_LOW_ACTN_CD", "\'SHIPREQ\'" );
      lCStockLocMap.put( "REORDER_QT", "\'17\'" );
      lCStockLocMap.put( "ALLOC_PCT", "\'1\'" );
      lCStockLocMap.put( "BATCH_SIZE", "\'2\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STOCK_LOC, lCStockLocMap ) );
      // These columns are not used to verify imported item
      lCStockLocMap.remove( "AIRPORT_CD" );
      lCStockLocMap.remove( "STOCK_NO_CD" );

      // run validation
      Assert.assertTrue( "Validation: ", runValidateMaterialsImport() == -1 );

      // Validate CFGMAT-12317 error
      validateErrorCode( "CFGMAT-12317" );
   }
   // ==================================================================================================================================


   /**
    * This function is to get 2000 length string. .
    *
    *
    */
   public String get2000Length( String aHead ) {

      String ls = aHead;
      int length = aHead.length();
      for ( int i = length; i < 2000; i++ ) {
         ls = ls + "A";

      }

      return ls;

   }


   /**
    * This function is to validate error code exists.
    *
    *
    */
   public void validateErrorCode( String aCode ) {

      List<String> llist = getErrorCodeList();
      String lerror_desc = getErrorDetail( aCode );
      Assert.assertTrue( "Error code found " + aCode + ": " + lerror_desc,
            llist.contains( aCode ) );

   }


   /**
    * This function is to retrieve errors code list
    *
    *
    */

   public List<String> getErrorCodeList() {
      String[] iIds = { "RESULT_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      String iQueryString = "select c_bin_part.result_cd " + " from c_bin_part "
            + " inner join DL_REF_MESSAGE on " + " DL_REF_MESSAGE.result_cd=c_bin_part.result_cd "
            + " UNION ALL " + " select c_part_no_misc_info.result_cd "
            + " from c_part_no_misc_info inner join DL_REF_MESSAGE on "
            + " DL_REF_MESSAGE.result_cd=c_part_no_misc_info.result_cd " + " UNION ALL "
            + " select c_stock_loc.result_cd " + " from c_stock_loc inner join DL_REF_MESSAGE on "
            + " DL_REF_MESSAGE.result_cd=c_stock_loc.result_cd " + " UNION ALL "
            + " select c_stock_no.result_cd " + " from c_stock_no inner join DL_REF_MESSAGE on "
            + " DL_REF_MESSAGE.result_cd=c_stock_no.result_cd ";
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      List<String> llistM = new ArrayList<String>();
      for ( int i = 0; i < llists.size(); i++ ) {
         llistM.add( llists.get( i ).get( 0 ) );
      }

      return llistM;

   }


   /**
    * This function is to get detail of error code
    *
    *
    */

   public String getErrorDetail( String aErrorcode ) {

      String[] iIds = { "TECH_DESC" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();

      lArgs.addArguments( "RESULT_CD", aErrorcode );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.DL_REF_MESSAGE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      return llists.get( 0 ).get( 0 );

   }


   /**
    * Run the Materials_Import Validation using a direct call to the plsql using a prepared
    * statement. This does not call any batch files.
    *
    * @returns the Result code for validation
    */
   protected int runValidateMaterialsImport() {
      int lReturn = 0;

      try {
         CallableStatement lPrepareCallInventory = getConnection()
               .prepareCall( "BEGIN materials_import.validate_materials(on_retcode =>  ?); END;" );
         lPrepareCallInventory.registerOutParameter( 1, Types.INTEGER );
         lPrepareCallInventory.execute();

         lReturn = lPrepareCallInventory.getInt( 1 );
         // rollBack();
      } catch ( SQLException e ) {
         e.printStackTrace();
      }
      return lReturn;
   }


   /**
    * Run the Materials_Import Import using a direct call to the plsql using a prepared statement.
    * This does not call any batch files.
    *
    * @returns the Result code for validation
    */
   protected int runImportMaterialsImport() {
      int lReturn = 0;

      try {
         CallableStatement lPrepareCallInventory = getConnection()
               .prepareCall( "BEGIN materials_import.import_materials(on_retcode =>  ?); END;" );
         lPrepareCallInventory.registerOutParameter( 1, Types.INTEGER );
         lPrepareCallInventory.execute();

         lReturn = lPrepareCallInventory.getInt( 1 );
         commit();
      } catch ( SQLException e ) {
         e.printStackTrace();
      }
      return lReturn;
   }


   /**
    * This converts all the "Y" values into 1 and all the "N" values into 0
    *
    * @param all
    *           values going into the C_Part_No_Misc_Info_Map
    * @return updated aDataMap
    */
   private Map<String, String> changeYesTo1AndNoto0( Map<String, String> aDataMap ) {

      for ( Entry<String, String> lEntry : aDataMap.entrySet() ) {
         if ( lEntry.getValue().equalsIgnoreCase( "\'Y\'" ) ) {
            aDataMap.put( lEntry.getKey(), "\'1\'" );
         }
         if ( lEntry.getValue().equalsIgnoreCase( "\'N\'" ) ) {
            aDataMap.put( lEntry.getKey(), "\'0\'" );
         }

      }

      return aDataMap;

   }
}
