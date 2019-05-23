package com.mxi.mx.core.maint.plan.baselineloader;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.mxi.mx.core.maint.plan.datamodels.partNo;
import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;
import com.mxi.mx.util.BaselineLoaderTest;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.TestConstants;
import com.mxi.mx.util.ValidationAndImport;
import com.mxi.mx.util.WhereClause;


/**
 * This test suite contains test cases on validation and import functionality on Kits area.
 *
 * @author ALICIA QIAN
 */
public class Kit extends BaselineLoaderTest {

   @Rule
   public TestName testName = new TestName();

   // Assembly records are created in data setup
   public String strBomPartIdToDelete1 = null;
   public String strPartNoIdToDelete1 = null;

   // Kit records are created in test_Kit_KitContent_NoConfigSlot_IMPORT
   public String strBomPartIdToDelete2 = null;
   public String strPartNoIdToDelete2 = null;

   // Kit records are created in test_Kit_ACFTSYSSUBASSYConfigSlot_KitContent_ACFTSYSTRK_IMPORT
   public String strBomPartIdToDelete3 = null;
   public String strPartNoIdToDelete3 = null;
   public String strPartNoIdAssmbly = null;

   // Kit records are created in test_Kit_ACFTSYSSUBASSYConfigSlot_KitContent_Multiple_IMPORT
   public String strBomPartIdToDelete4 = null;
   public String strPartNoIdToDelete4 = null;
   String strPartNoIdAssmbly1 = null;
   String strPartNoIdAssmbly2 = null;
   String strPartNoIdAssmbly3 = null;

   public String iKIT_GROUP_ID1 = null;
   public String iKIT_GROUP_ID2 = null;
   public String iKIT_GROUP_ID3 = null;

   public ArrayList<simpleIDs> iGroupIDs = null;
   public ArrayList<simpleIDs> iINSTALLGroupIDs = null;

   ValidationAndImport validationandimport;

   // @SuppressWarnings( "serial" )
   // ArrayList<String> updateTables = new ArrayList<String>() {
   //
   // {
   // add( "delete from EQP_KIT_PART_MAP" );
   // add( "delete from EQP_KIT_PART_GROUP_MAP" );
   // add( "delete from EQP_KIT_PART_GROUPS" );
   // add( "delete from EQP_INSTALL_KIT_MAP" );
   // add( "delete from EQP_INSTALL_KIT_PART_MAP" );
   //
   // }
   // };


   /**
    * Clean up after each individual test
    *
    * @throws Exception
    */
   @After
   @Override
   public void after() {
      cleanupTables();
      String strTCName = testName.getMethodName();
      if ( strTCName.equals( "test_Kit_KitContent_NoConfigSlot_VALIDATION" )
            || strTCName.equals( "test_Kit_KitContent_NoConfigSlot_IMPORT" )
            || strTCName
                  .equals( "test_Kit_ACFTSYSSUBASSYConfigSlot_KitContent_Multiple_VALIDATION" )
            || strTCName.equals( "test_Kit_ACFTSYSSUBASSYConfigSlot_KitContent_Multiple_IMPORT" )
            || strTCName.equals( "test_Kit_KitInstall_NoConfigSlot_VALIDATION" )
            || strTCName.equals( "test_Kit_KitInstall_NoConfigSlot_IMPORT" ) ) {
         AssmblydataCleanup();
      }

      dataCleanup();
      super.after();
   }


   /**
    * Setup before executing each individual test
    *
    * @throws Exception
    */
   @Before
   @Override
   public void before() throws Exception {

      super.before();
      clearBaselineLoaderTables();
      cleanupTables();
      String strTCName = testName.getMethodName();
      if ( strTCName.equals( "test_Kit_KitContent_NoConfigSlot_VALIDATION" )
            || strTCName.equals( "test_Kit_KitContent_NoConfigSlot_IMPORT" )
            || strTCName
                  .equals( "test_Kit_ACFTSYSSUBASSYConfigSlot_KitContent_Multiple_VALIDATION" )
            || strTCName.equals( "test_Kit_ACFTSYSSUBASSYConfigSlot_KitContent_Multiple_IMPORT" )
            || strTCName.equals( "test_Kit_KitInstall_NoConfigSlot_VALIDATION" )
            || strTCName.equals( "test_Kit_KitInstall_NoConfigSlot_IMPORT" ) ) {
         AssmblydataCleanup();
         dataSetup();
      }

   }


   /**
    * This is positive test case for kit import validation functionality by loading data into
    * staging table and then check whether error code(s) has(have) been generated. Kit configuration
    * slot:ACFT-SYS-SUBASSY Kit content configuration slot: ACFT-SYS-TRK
    *
    */
   @Test
   public void test_Kit_ACFTSYSSUBASSYConfigSlot_KitContent_ACFTSYSTRK_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // BL_KIT table
      Map<String, String> BLKITMap = new LinkedHashMap<>();

      BLKITMap.put( "KIT_ASSMBL_CD", "\'ACFT_CD1\'" );
      BLKITMap.put( "KIT_CONFIG_SLOT_CD", "\'APU-ASSY\'" );
      BLKITMap.put( "KIT_PART_GROUP_CD", "\'AUTOKITGROUP\'" );
      BLKITMap.put( "KIT_PART_GROUP_NAME", "\'AUTOKITGROUP\'" );
      BLKITMap.put( "KIT_PART_GROUP_APPL_RANGE", "\'1914453,1914454\'" );
      BLKITMap.put( "KIT_PART_NO_OEM", "\'AUTOKIT\'" );
      BLKITMap.put( "KIT_MANUFACT_CD", "\'ABC11\'" );
      BLKITMap.put( "KIT_NAME", "\'AUTOKIT\'" );
      BLKITMap.put( "KIT_DESCRIPTION", "\'AUTOKIT\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_KIT, BLKITMap ) );

      // BL_KIT_CONTENT table
      Map<String, String> BLKITCONTENTMap = new LinkedHashMap<>();

      BLKITCONTENTMap.put( "KIT_PART_NO_OEM", "\'AUTOKIT\'" );
      BLKITCONTENTMap.put( "KIT_MANUFACT_CD", "\'ABC11\'" );
      BLKITCONTENTMap.put( "PART_NO_OEM", "\'A0000010\'" );
      BLKITCONTENTMap.put( "MANUFACT_CD", "\'11111\'" );
      BLKITCONTENTMap.put( "ASSMBL_CD", "\'ACFT_CD1\'" );
      BLKITCONTENTMap.put( "PART_GROUP_CD", "\'ACFT-SYS-1-1-TRK-BATCH-PARENT\'" );
      BLKITCONTENTMap.put( "PART_QTY", "\'1\'" );
      BLKITCONTENTMap.put( "VALUE_PCT", "\'100\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_KIT_CONTENT, BLKITCONTENTMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This is positive test case for kit import import functionality by loading data into staging
    * table and then check: 1. whether error code(s) has(have) been generated. 2. kit is uploaded
    * into EQP_BOM_PART 3. kit is uploaded into EQP_PART_NO 4. Check data in EQP_KIT_PART_MAP,
    * EQP_KIT_PART_GROUP_MAP,and EQP_KIT_PART_GROUPS are correct.
    *
    * Kit configuration slot:ACFT-SYS-SUBASSY Kit content configuration slot: ACFT-SYS-TRK
    *
    */

   @Test
   public void test_Kit_ACFTSYSSUBASSYConfigSlot_KitContent_ACFTSYSTRK_IMPORT() {

      System.out.println(
            "=======Starting:  " + testName.getMethodName() + " ========================" );

      test_Kit_ACFTSYSSUBASSYConfigSlot_KitContent_ACFTSYSTRK_VALIDATION();
      System.out.println( "Finish validation" );

      iGroupIDs = null;
      iINSTALLGroupIDs = null;
      String lKIT_PART_NO_ID = null;

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      String sqlQuery =
            "select BOM_PART_ID from EQP_BOM_PART where BOM_PART_NAME like '%AUTOKITGROUP%' and BOM_PART_DB_ID="
                  + CONS_DB_ID;
      strBomPartIdToDelete3 = getStringValueFromQuery( sqlQuery, "BOM_PART_ID" );

      sqlQuery =
            "select PART_NO_ID  from eqp_part_no where PART_NO_OEM='AUTOKIT' and PART_NO_DB_ID="
                  + CONS_DB_ID;
      strPartNoIdToDelete3 = getStringValueFromQuery( sqlQuery, "PART_NO_ID" );
      lKIT_PART_NO_ID = strPartNoIdToDelete3;

      // check eqp_part_baseline
      String strBPN = getBomPart( strPartNoIdToDelete3 );
      Assert.assertTrue( strBPN.equalsIgnoreCase( strBomPartIdToDelete3 ) );

      sqlQuery =
            "select PART_NO_ID from eqp_part_no where PART_NO_OEM='A0000010' and Manufact_cd='11111' and PART_NO_DB_ID="
                  + CONS_DB_ID;
      strPartNoIdAssmbly = getStringValueFromQuery( sqlQuery, "PART_NO_ID" );

      ArrayList<String> list = new ArrayList<>( Arrays.asList( "ACFT_CD1",
            "ACFT-SYS-1-1-TRK-BATCH-PARENT", "BATCH-on-TRK Parent", "TRK" ) );
      CheckImportValidation( "EQP_BOM_PART_2", "ACFT_CD1", strBomPartIdToDelete3, list );
      CheckImportValidation( "EQP_PART_NO", "", "", list );
      CheckImportValidation( "EQP_KIT_PART_GROUP_MAP_2", "", "", list );

      // check appl range is updated in EQP_BOM_PART
      checkApplRange( "1914453,1914454", strBomPartIdToDelete3 );

      // check kit_qt and value_pct
      strBPN = getBomPart( strPartNoIdAssmbly );
      checkQTandPCT( strBPN, "1", "1" );

      iGroupIDs = getGroupId( lKIT_PART_NO_ID );

   }


   /**
    * This is positive test case for kit import validation functionality by loading data into
    * staging table and then check whether error code(s) has(have) been generated. Kit configuration
    * slot:APU-SYS (inv_class_cd=BATCH) Kit content configuration slot: APU-SYS (inv_class_cd=SER)
    *
    */

   @Test
   public void test_Kit_APUSYSBATCHConfigSlot_KitContent_APUSYSSER_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " validation========================" );

      // BL_KIT table
      Map<String, String> BLKITMap = new LinkedHashMap<>();

      BLKITMap.put( "KIT_ASSMBL_CD", "\'APU_CD1\'" );
      BLKITMap.put( "KIT_CONFIG_SLOT_CD", "\'APU-SYS-1-1\'" );
      BLKITMap.put( "KIT_PART_GROUP_CD", "\'AUTOKITGROUP\'" );
      BLKITMap.put( "KIT_PART_GROUP_NAME", "\'AUTOKITGROUP\'" );
      BLKITMap.put( "KIT_PART_GROUP_APPL_RANGE", "\'1914453,1914454\'" );
      BLKITMap.put( "KIT_PART_NO_OEM", "\'AUTOKIT\'" );
      BLKITMap.put( "KIT_MANUFACT_CD", "\'ABC11\'" );
      BLKITMap.put( "KIT_NAME", "\'AUTOKIT\'" );
      BLKITMap.put( "KIT_DESCRIPTION", "\'AUTOKIT\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_KIT, BLKITMap ) );

      // BL_KIT_CONTENT table
      Map<String, String> BLKITCONTENTMap = new LinkedHashMap<>();

      BLKITCONTENTMap.put( "KIT_PART_NO_OEM", "\'AUTOKIT\'" );
      BLKITCONTENTMap.put( "KIT_MANUFACT_CD", "\'ABC11\'" );
      BLKITCONTENTMap.put( "PART_NO_OEM", "\'AP0000012\'" );
      BLKITCONTENTMap.put( "MANUFACT_CD", "\'1234567890\'" );
      BLKITCONTENTMap.put( "ASSMBL_CD", "\'APU_CD1\'" );
      BLKITCONTENTMap.put( "PART_GROUP_CD", "\'APU-SYS-1-1-SER\'" );
      BLKITCONTENTMap.put( "PART_QTY", "\'1\'" );
      BLKITCONTENTMap.put( "VALUE_PCT", "\'100\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_KIT_CONTENT, BLKITCONTENTMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This is positive test case for kit import import functionality by loading data into staging
    * table and then check: 1. whether error code(s) has(have) been generated. 2. kit is uploaded
    * into EQP_BOM_PART 3. kit is uploaded into EQP_PART_NO 4. Check data in EQP_KIT_PART_MAP,
    * EQP_KIT_PART_GROUP_MAP,and EQP_KIT_PART_GROUPS are correct.
    *
    * Kit configuration slot:APU-SYS (inv_class_cd=BATCH) Kit content configuration slot: APUT-SYS
    * (inv_class_cd=SER)
    *
    */

   @Test
   public void test_Kit_APUSYSBATCHConfigSlot_KitContent_APUSYSSER_IMPORT() {

      System.out.println(
            "=======Starting:  " + testName.getMethodName() + " ========================" );

      test_Kit_APUSYSBATCHConfigSlot_KitContent_APUSYSSER_VALIDATION();
      System.out.println( "Finishing validation" );

      iGroupIDs = null;
      iINSTALLGroupIDs = null;
      String lKIT_PART_NO_ID = null;

      // run import
      Assert.assertTrue( runValidationAndImport( false, false ) == 1 );

      String sqlQuery =
            "select BOM_PART_ID from EQP_BOM_PART where BOM_PART_NAME like '%AUTOKITGROUP%' and BOM_PART_DB_ID="
                  + CONS_DB_ID;
      strBomPartIdToDelete3 = getStringValueFromQuery( sqlQuery, "BOM_PART_ID" );

      sqlQuery =
            "select PART_NO_ID  from eqp_part_no where PART_NO_OEM='AUTOKIT' and PART_NO_DB_ID="
                  + CONS_DB_ID;
      strPartNoIdToDelete3 = getStringValueFromQuery( sqlQuery, "PART_NO_ID" );
      lKIT_PART_NO_ID = strPartNoIdToDelete3;

      // check eqp_part_baseline
      String strBPN = getBomPart( strPartNoIdToDelete3 );
      Assert.assertTrue( strBPN.equalsIgnoreCase( strBomPartIdToDelete3 ) );

      sqlQuery =
            "select PART_NO_ID from eqp_part_no where PART_NO_OEM='AP0000012' and Manufact_cd='1234567890'";
      strPartNoIdAssmbly = getStringValueFromQuery( sqlQuery, "PART_NO_ID" );
      ArrayList<String> list = new ArrayList<>(
            Arrays.asList( "APU_CD1", "APU-SYS-1-1-SER", "Serialized Part", "SER" ) );

      CheckImportValidation( "EQP_BOM_PART_2", "APU_CD1", strBomPartIdToDelete3, list );
      CheckImportValidation( "EQP_PART_NO", "", "", list );
      CheckImportValidation( "EQP_KIT_PART_GROUP_MAP_2", "", "", list );

      // check appl range is updated in EQP_BOM_PART
      checkApplRange( "1914453,1914454", strBomPartIdToDelete3 );

      // check kit_qt and value_pct
      strBPN = getBomPart( strPartNoIdAssmbly );
      checkQTandPCT( strBPN, "1", "1" );

      // Get Group IDs
      iGroupIDs = getGroupId( lKIT_PART_NO_ID );

   }


   /**
    * This is positive test case for kit import validation functionality by loading data into
    * staging table and then check whether error code(s) has(have) been generated. Kit configuration
    * slot:ENG-SYS (inv_class_cd=BATCH) Kit content configuration slot: ENG-ASSY (inv_class_cd=TRK)
    *
    */

   @Test
   public void test_Kit_ENGSYSBATCHConfigSlot_KitContent_ENGASSYTRK_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // BL_KIT table
      Map<String, String> BLKITMap = new LinkedHashMap<>();

      BLKITMap.put( "KIT_ASSMBL_CD", "\'ENG_CD1\'" );
      BLKITMap.put( "KIT_CONFIG_SLOT_CD", "\'ENG-SYS-1-1\'" );
      BLKITMap.put( "KIT_PART_GROUP_CD", "\'AUTOKITGROUP\'" );
      BLKITMap.put( "KIT_PART_GROUP_NAME", "\'AUTOKITGROUP\'" );
      BLKITMap.put( "KIT_PART_GROUP_APPL_RANGE", null );
      BLKITMap.put( "KIT_PART_NO_OEM", "\'AUTOKIT\'" );
      BLKITMap.put( "KIT_MANUFACT_CD", "\'ABC11\'" );
      BLKITMap.put( "KIT_NAME", "\'AUTOKIT\'" );
      BLKITMap.put( "KIT_DESCRIPTION", "\'AUTOKIT\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_KIT, BLKITMap ) );

      // BL_KIT_CONTENT table
      Map<String, String> BLKITCONTENTMap = new LinkedHashMap<>();

      BLKITCONTENTMap.put( "KIT_PART_NO_OEM", "\'AUTOKIT\'" );
      BLKITCONTENTMap.put( "KIT_MANUFACT_CD", "\'ABC11\'" );
      BLKITCONTENTMap.put( "PART_NO_OEM", "\'E0000007\'" );
      BLKITCONTENTMap.put( "MANUFACT_CD", "\'ABC11\'" );
      BLKITCONTENTMap.put( "ASSMBL_CD", "\'ENG_CD1\'" );
      BLKITCONTENTMap.put( "PART_GROUP_CD", "\'ENG-SYS-1-1-TRK-SRU\'" );
      BLKITCONTENTMap.put( "PART_QTY", "\'1\'" );
      BLKITCONTENTMap.put( "VALUE_PCT", "\'100\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_KIT_CONTENT, BLKITCONTENTMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );
   }


   /**
    * This is positive test case for kit import import functionality by loading data into staging
    * table and then check: 1. whether error code(s) has(have) been generated. 2. kit is uploaded
    * into EQP_BOM_PART 3. kit is uploaded into EQP_PART_NO 4. Check data in EQP_KIT_PART_MAP,
    * EQP_KIT_PART_GROUP_MAP,and EQP_KIT_PART_GROUPS are correct.
    *
    * Kit configuration slot:ENG-SYS Kit content configuration slot: ENG-ASSY
    *
    */

   @Test
   public void test_Kit_ENGSYSBATCHConfigSlot_KitContent_ENGASSYTRK_IMPORT() {

      System.out.println(
            "=======Starting:  " + testName.getMethodName() + " ========================" );

      test_Kit_ENGSYSBATCHConfigSlot_KitContent_ENGASSYTRK_VALIDATION();
      System.out.println( "Finish validation" );

      iGroupIDs = null;
      iINSTALLGroupIDs = null;
      String lKIT_PART_NO_ID = null;

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      String sqlQuery =
            "select BOM_PART_ID from EQP_BOM_PART where BOM_PART_NAME like '%AUTOKITGROUP%' and BOM_PART_DB_ID="
                  + CONS_DB_ID;
      strBomPartIdToDelete3 = getStringValueFromQuery( sqlQuery, "BOM_PART_ID" );

      sqlQuery =
            "select PART_NO_ID  from eqp_part_no where PART_NO_OEM='AUTOKIT' and PART_NO_DB_ID="
                  + CONS_DB_ID;
      strPartNoIdToDelete3 = getStringValueFromQuery( sqlQuery, "PART_NO_ID" );
      lKIT_PART_NO_ID = strPartNoIdToDelete3;

      // check eqp_part_baseline
      String strBPN = getBomPart( strPartNoIdToDelete3 );
      Assert.assertTrue( strBPN.equalsIgnoreCase( strBomPartIdToDelete3 ) );

      sqlQuery =
            "select PART_NO_ID from eqp_part_no where PART_NO_OEM='E0000007' and Manufact_cd='ABC11' and PART_NO_DB_ID="
                  + CONS_DB_ID;
      strPartNoIdAssmbly = getStringValueFromQuery( sqlQuery, "PART_NO_ID" );
      ArrayList<String> list = new ArrayList<>(
            Arrays.asList( "ENG_CD1", "ENG-SYS-1-1-TRK-SRU", "Tracked SRU", "TRK" ) );

      CheckImportValidation( "EQP_BOM_PART_2", "ENG_CD1", strBomPartIdToDelete3, list );
      CheckImportValidation( "EQP_PART_NO", "", "", list );
      CheckImportValidation( "EQP_KIT_PART_GROUP_MAP_2", "", "", list );

      // check appl range is updated in EQP_BOM_PART
      checkApplRange( null, strBomPartIdToDelete3 );

      // check kit_qt and value_pct
      strBPN = getBomPart( strPartNoIdAssmbly );
      checkQTandPCT( strBPN, "1", "1" );

      // Get Group Ids
      iGroupIDs = getGroupId( lKIT_PART_NO_ID );

   }


   /**
    * This is positive test case for kit import validation functionality by loading data into
    * staging table and then check whether error code(s) has(have) been generated. Kit configuration
    * slot:NA Kit content configuration slot: COMHW-SYS (inv_class_cd=BATCH)
    *
    */

   @Test
   public void test_Kit_KitContent_NoConfigSlot_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " validation========================" );

      // BL_KIT table
      Map<String, String> BLKITMap = new LinkedHashMap<>();

      BLKITMap.put( "KIT_ASSMBL_CD", null );
      BLKITMap.put( "KIT_CONFIG_SLOT_CD", null );
      BLKITMap.put( "KIT_PART_GROUP_CD", null );
      BLKITMap.put( "KIT_PART_GROUP_NAME", null );
      BLKITMap.put( "KIT_PART_GROUP_APPL_RANGE", null );
      BLKITMap.put( "KIT_PART_NO_OEM", "\'AUTOKIT\'" );
      BLKITMap.put( "KIT_MANUFACT_CD", "\'11111\'" );
      BLKITMap.put( "KIT_NAME", "\'AUTOKIT\'" );
      BLKITMap.put( "KIT_DESCRIPTION", null );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_KIT, BLKITMap ) );

      // BL_KIT_CONTENT table
      Map<String, String> BLKITCONTENTMap = new LinkedHashMap<>();

      BLKITCONTENTMap.put( "KIT_PART_NO_OEM", "\'AUTOKIT\'" );
      BLKITCONTENTMap.put( "KIT_MANUFACT_CD", "\'11111\'" );
      BLKITCONTENTMap.put( "PART_NO_OEM", "\'AUTOTEST\'" );
      BLKITCONTENTMap.put( "MANUFACT_CD", "\'11111\'" );
      BLKITCONTENTMap.put( "ASSMBL_CD", "\'COMHW\'" );
      BLKITCONTENTMap.put( "PART_GROUP_CD", "\'CHW-AUTO-PG1\'" );
      BLKITCONTENTMap.put( "PART_QTY", "\'1\'" );
      BLKITCONTENTMap.put( "VALUE_PCT", "\'100\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_KIT_CONTENT, BLKITCONTENTMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This is positive test case for kit import import functionality by loading data into staging
    * table and then check: 1. whether error code(s) has(have) been generated. 2. kit is uploaded
    * into EQP_BOM_PART 3. kit is uploaded into EQP_PART_NO 4. Check data in EQP_KIT_PART_MAP,
    * EQP_KIT_PART_GROUP_MAP,and EQP_KIT_PART_GROUPS are correct.
    *
    * Kit configuration slot:NA Kit content configuration slot: COMHW-SYS (inv_class_cd=BATCH)
    *
    */

   @Test
   public void test_Kit_KitContent_NoConfigSlot_IMPORT() {

      System.out.println(
            "=======Starting:  " + testName.getMethodName() + " ========================" );

      test_Kit_KitContent_NoConfigSlot_VALIDATION();

      System.out.println( "Finish validation" );

      iGroupIDs = null;
      iINSTALLGroupIDs = null;
      String lKIT_PART_NO_ID = null;

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );
      ArrayList<String> list =
            new ArrayList<>( Arrays.asList( "COMHW", "CHW-AUTO-PG1", "CHW-AUTO-PG1", "BATCH" ) );

      CheckImportValidation( "EQP_BOM_PART", "COMHW", "0", list );
      CheckImportValidation( "EQP_PART_NO", "", "", list );

      String sqlQuery =
            "select BOM_PART_ID from EQP_BOM_PART where BOM_PART_NAME like '%AUTOKIT%' and BOM_PART_DB_ID="
                  + CONS_DB_ID;
      strBomPartIdToDelete2 = getStringValueFromQuery( sqlQuery, "BOM_PART_ID" );

      sqlQuery =
            "select PART_NO_ID  from eqp_part_no where PART_NO_OEM='AUTOKIT' and PART_NO_DB_ID="
                  + CONS_DB_ID;
      strPartNoIdToDelete2 = getStringValueFromQuery( sqlQuery, "PART_NO_ID" );
      lKIT_PART_NO_ID = strPartNoIdToDelete2;

      // check eqp_part_baseline
      String strBPN = getBomPart( strPartNoIdToDelete2 );
      Assert.assertTrue( strBPN.equalsIgnoreCase( strBomPartIdToDelete2 ) );

      CheckImportValidation( "EQP_KIT_PART_GROUP_MAP", "", "", list );

      // check appl range is updated in EQP_BOM_PART
      checkApplRange( null, strBomPartIdToDelete2 );

      // Get Group Ids
      iGroupIDs = getGroupId( lKIT_PART_NO_ID );

   }


   /**
    * This is positive test case for kit import validation functionality by loading data into
    * staging table with multiple contents and then check whether error code(s) has(have) been
    * generated. Kit configuration slot: ACFT-SYS-SUBASSY Kit content configuration slot:
    * ACFT-SYS-TRK, ACFT-SYS-TRK and COMHW-SYS
    *
    */

   @Test
   public void test_Kit_ACFTSYSSUBASSYConfigSlot_KitContent_Multiple_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // BL_KIT table
      Map<String, String> BLKITMap = new LinkedHashMap<>();

      BLKITMap.put( "KIT_ASSMBL_CD", "\'ACFT_CD1\'" );
      BLKITMap.put( "KIT_CONFIG_SLOT_CD", "\'APU-ASSY\'" );
      BLKITMap.put( "KIT_PART_GROUP_CD", "\'AUTOKITGROUP\'" );
      BLKITMap.put( "KIT_PART_GROUP_NAME", "\'AUTOKITGROUP\'" );
      BLKITMap.put( "KIT_PART_GROUP_APPL_RANGE", "\'1914453,1914454\'" );
      BLKITMap.put( "KIT_PART_NO_OEM", "\'AUTOKIT\'" );
      BLKITMap.put( "KIT_MANUFACT_CD", "\'11111\'" );
      BLKITMap.put( "KIT_NAME", "\'AUTOKIT\'" );
      BLKITMap.put( "KIT_DESCRIPTION", "\'AUTOKIT\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_KIT, BLKITMap ) );

      // BL_KIT_CONTENT table
      Map<String, String> BLKITCONTENTMap = new LinkedHashMap<>();

      BLKITCONTENTMap.put( "KIT_PART_NO_OEM", "\'AUTOKIT\'" );
      BLKITCONTENTMap.put( "KIT_MANUFACT_CD", "\'11111\'" );
      BLKITCONTENTMap.put( "PART_NO_OEM", "\'A0000010\'" );
      BLKITCONTENTMap.put( "MANUFACT_CD", "\'11111\'" );
      BLKITCONTENTMap.put( "ASSMBL_CD", "\'ACFT_CD1\'" );
      BLKITCONTENTMap.put( "PART_GROUP_CD", "\'ACFT-SYS-1-1-TRK-BATCH-PARENT\'" );
      BLKITCONTENTMap.put( "PART_QTY", "\'1\'" );
      BLKITCONTENTMap.put( "VALUE_PCT", "\'30\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_KIT_CONTENT, BLKITCONTENTMap ) );

      // BL_KIT_CONTENT table
      BLKITCONTENTMap.clear();
      BLKITCONTENTMap.put( "KIT_PART_NO_OEM", "\'AUTOKIT\'" );
      BLKITCONTENTMap.put( "KIT_MANUFACT_CD", "\'11111\'" );
      BLKITCONTENTMap.put( "PART_NO_OEM", "\'A0000018A\'" );
      BLKITCONTENTMap.put( "MANUFACT_CD", "\'11111\'" );
      BLKITCONTENTMap.put( "ASSMBL_CD", "\'ACFT_CD1\'" );
      BLKITCONTENTMap.put( "PART_GROUP_CD", "\'ACFT-SYS-1-1-TRK-INTERCHANGABILITY\'" );
      BLKITCONTENTMap.put( "PART_QTY", "\'1\'" );
      BLKITCONTENTMap.put( "VALUE_PCT", "\'30\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_KIT_CONTENT, BLKITCONTENTMap ) );

      // BL_KIT_CONTENT table
      BLKITCONTENTMap.clear();
      BLKITCONTENTMap.put( "KIT_PART_NO_OEM", "\'AUTOKIT\'" );
      BLKITCONTENTMap.put( "KIT_MANUFACT_CD", "\'11111\'" );
      BLKITCONTENTMap.put( "PART_NO_OEM", "\'AUTOTEST\'" );
      BLKITCONTENTMap.put( "MANUFACT_CD", "\'11111\'" );
      BLKITCONTENTMap.put( "ASSMBL_CD", "\'COMHW\'" );
      BLKITCONTENTMap.put( "PART_GROUP_CD", "\'CHW-AUTO-PG1\'" );
      BLKITCONTENTMap.put( "PART_QTY", "\'1\'" );
      BLKITCONTENTMap.put( "VALUE_PCT", "\'40\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_KIT_CONTENT, BLKITCONTENTMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This is positive test case for kit import validation functionality by loading data into
    * staging table with multiple contents and then check: 1. whether error code(s) has(have) been
    * generated. 2. kit is uploaded into EQP_BOM_PART 3. kit is uploaded into EQP_PART_NO 4. Check
    * data in EQP_KIT_PART_MAP, EQP_KIT_PART_GROUP_MAP,and EQP_KIT_PART_GROUPS are correct.
    *
    * Kit configuration slot: ACFT-SYS-SUBASSY Kit content configuration slot: ACFT-SYS-TRK,
    * ACFT-SYS-TRK and COMHW-SYS
    *
    */

   @Test
   public void test_Kit_ACFTSYSSUBASSYConfigSlot_KitContent_Multiple_IMPORT() {

      System.out.println(
            "=======Starting:  " + testName.getMethodName() + " ========================" );

      test_Kit_ACFTSYSSUBASSYConfigSlot_KitContent_Multiple_VALIDATION();

      System.out.println( "Finish validation" );

      iGroupIDs = null;
      iINSTALLGroupIDs = null;
      String lKIT_PART_NO_ID = null;

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify EQPKitPartGroupMap tables
      String sqlQuery =
            "select BOM_PART_ID from EQP_BOM_PART where BOM_PART_NAME like '%AUTOKITGROUP%' and BOM_PART_DB_ID="
                  + CONS_DB_ID;
      strBomPartIdToDelete4 = getStringValueFromQuery( sqlQuery, "BOM_PART_ID" );

      sqlQuery =
            "select PART_NO_ID  from eqp_part_no where PART_NO_OEM='AUTOKIT' and PART_NO_DB_ID="
                  + CONS_DB_ID;
      strPartNoIdToDelete4 = getStringValueFromQuery( sqlQuery, "PART_NO_ID" );
      lKIT_PART_NO_ID = strPartNoIdToDelete4;

      // check eqp_part_baseline
      String strBPN = getBomPart( strPartNoIdToDelete4 );
      Assert.assertTrue( strBPN.equalsIgnoreCase( strBomPartIdToDelete4 ) );

      sqlQuery =
            "select PART_NO_ID from eqp_part_no where PART_NO_OEM='A0000010' and Manufact_cd='11111' and PART_NO_DB_ID="
                  + CONS_DB_ID;
      strPartNoIdAssmbly1 = getStringValueFromQuery( sqlQuery, "PART_NO_ID" );
      partNo pn1 = new partNo( Integer.toString( CONS_DB_ID ), strPartNoIdAssmbly1 );

      sqlQuery =
            "select PART_NO_ID from eqp_part_no where PART_NO_OEM='A0000018A' and Manufact_cd='11111' and PART_NO_DB_ID="
                  + CONS_DB_ID;
      strPartNoIdAssmbly2 = getStringValueFromQuery( sqlQuery, "PART_NO_ID" );
      partNo pn2 = new partNo( Integer.toString( CONS_DB_ID ), strPartNoIdAssmbly2 );

      sqlQuery =
            "select PART_NO_ID from eqp_part_no where PART_NO_OEM='AUTOTEST' and Manufact_cd='11111' and PART_NO_DB_ID="
                  + CONS_DB_ID;
      strPartNoIdAssmbly3 = getStringValueFromQuery( sqlQuery, "PART_NO_ID" );
      partNo pn3 = new partNo( Integer.toString( CONS_DB_ID ), strPartNoIdAssmbly3 );

      // verify eqp_bom_part and eqp_kit_part_groups table
      kitBomPart kit1 = new kitBomPart( "ACFT_CD1", "ACFT-SYS-1-1-TRK-BATCH-PARENT",
            "BATCH-on-TRK Parent", "TRK", 1, 0.3 );
      kitBomPart kit2 = new kitBomPart( "COMHW", "CHW-AUTO-PG1", "CHW-AUTO-PG1", "BATCH", 1, 0.4 );
      kitBomPart kit3 = new kitBomPart( "ACFT_CD1", "ACFT-SYS-1-1-TRK-INTERCHANGABILITY",
            "Tracked Part with Multiple Interchangeable Part Numbers", "TRK", 1, 0.3 );

      ArrayList<kitBomPart> ExpectedList = new ArrayList<kitBomPart>();
      ExpectedList.add( kit1 );
      ExpectedList.add( kit2 );
      ExpectedList.add( kit3 );

      ArrayList<kitBomPart> ActualList = new ArrayList<kitBomPart>();
      String strSql =
            "select Assmbl_CD, BOM_PART_CD, BOM_PART_NAME, INV_CLASS_CD, eqp_kit_part_groups.KIT_QT, eqp_kit_part_groups.VALUE_PCT from eqp_bom_part "
                  + "inner join eqp_kit_part_groups on "
                  + "eqp_kit_part_groups.bom_part_db_id=eqp_bom_part.bom_part_db_id and "
                  + "eqp_kit_part_groups.bom_part_id=eqp_bom_part.bom_part_id";
      ResultSet assbomResultSetRecords = null;
      try {
         assbomResultSetRecords = runQuery( strSql );
         while ( assbomResultSetRecords.next() ) {
            kitBomPart assmblKit = new kitBomPart( assbomResultSetRecords.getString( "ASSMBL_CD" ),
                  assbomResultSetRecords.getString( "BOM_PART_CD" ),
                  assbomResultSetRecords.getString( "BOM_PART_NAME" ),
                  assbomResultSetRecords.getString( "INV_CLASS_CD" ),
                  assbomResultSetRecords.getInt( "KIT_QT" ),
                  assbomResultSetRecords.getDouble( "VALUE_PCT" ) );

            ActualList.add( assmblKit );

         }
      } catch ( SQLException e ) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } finally {
         checkArraysEqual( ExpectedList, ActualList );

      }

      // verify eqp_kit_part_groups and eqp_bom_part tables
      ArrayList<partNo> ExpectedPNList = new ArrayList<partNo>();
      ExpectedPNList.add( pn1 );
      ExpectedPNList.add( pn2 );
      ExpectedPNList.add( pn3 );
      ArrayList<partNo> ActualPNList = new ArrayList<partNo>();

      strSql = "select PART_NO_DB_ID, PART_NO_ID from eqp_kit_part_map "
            + "inner join eqp_kit_part_group_map on " + "eqp_kit_part_map.eqp_kit_part_group_id="
            + "eqp_kit_part_group_map.eqp_kit_part_group_id "
            + "where eqp_kit_part_group_map.kit_part_no_id=" + strPartNoIdToDelete4;

      try {
         assbomResultSetRecords.close();
         assbomResultSetRecords = runQuery( strSql );
         while ( assbomResultSetRecords.next() ) {
            partNo pn = new partNo( assbomResultSetRecords.getString( "PART_NO_DB_ID" ),
                  assbomResultSetRecords.getString( "PART_NO_ID" ) );

            ActualPNList.add( pn );

         }
      } catch ( SQLException e ) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } finally {
         checkArraysEqual( ExpectedPNList, ActualPNList );

      }

      // Get eqo_kit_part_group Ids
      iGroupIDs = getGroupId( lKIT_PART_NO_ID );

      // check appl range is updated in EQP_BOM_PART
      checkApplRange( "1914453,1914454", strBomPartIdToDelete4 );

   }


   /**
    * This is positive test case for kit import validation functionality by loading data into
    * staging table and then check whether error code(s) has(have) been generated. Kit configuration
    * slot:NA Kit content configuration slot: COMHW-SYS Kit install configuration slot: ACFT-SYS-SYS
    * (inv_class_cd:BATCH)
    *
    */

   @Test
   public void test_Kit_KitInstall_NoConfigSlot_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " validation========================" );

      // BL_KIT table
      Map<String, String> BLKITMap = new LinkedHashMap<>();

      BLKITMap.put( "KIT_ASSMBL_CD", null );
      BLKITMap.put( "KIT_CONFIG_SLOT_CD", null );
      BLKITMap.put( "KIT_PART_GROUP_CD", null );
      BLKITMap.put( "KIT_PART_GROUP_NAME", null );
      BLKITMap.put( "KIT_PART_GROUP_APPL_RANGE", null );
      BLKITMap.put( "KIT_PART_NO_OEM", "\'AUTOKIT\'" );
      BLKITMap.put( "KIT_MANUFACT_CD", "\'11111\'" );
      BLKITMap.put( "KIT_NAME", "\'AUTOKIT\'" );
      BLKITMap.put( "KIT_DESCRIPTION", null );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_KIT, BLKITMap ) );

      // BL_KIT_CONTENT table
      Map<String, String> BLKITCONTENTMap = new LinkedHashMap<>();

      BLKITCONTENTMap.put( "KIT_PART_NO_OEM", "\'AUTOKIT\'" );
      BLKITCONTENTMap.put( "KIT_MANUFACT_CD", "\'11111\'" );
      BLKITCONTENTMap.put( "PART_NO_OEM", "\'AUTOTEST\'" );
      BLKITCONTENTMap.put( "MANUFACT_CD", "\'11111\'" );
      BLKITCONTENTMap.put( "ASSMBL_CD", "\'COMHW\'" );
      BLKITCONTENTMap.put( "PART_GROUP_CD", "\'CHW-AUTO-PG1\'" );
      BLKITCONTENTMap.put( "PART_QTY", "\'1\'" );
      BLKITCONTENTMap.put( "VALUE_PCT", "\'100\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_KIT_CONTENT, BLKITCONTENTMap ) );

      // bl_install_kit table
      Map<String, String> BLKITInstallMap = new LinkedHashMap<>();

      BLKITInstallMap.put( "KIT_PART_NO_OEM", "\'AUTOKIT\'" );
      BLKITInstallMap.put( "KIT_MANUFACT_CD", "\'11111\'" );
      BLKITInstallMap.put( "INSTALL_ASSMBL_CD", "\'ACFT_CD1\'" );
      BLKITInstallMap.put( "INSTALL_PART_GROUP_CD", "\'ACFT-SYS-1-1-TRK-BATCH-CHILD\'" );
      BLKITInstallMap.put( "INSTALL_PART_NO_OEM", "\'A0000011\'" );
      BLKITInstallMap.put( "INSTALL_MANUFACT_CD", "\'ABC11\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_INSTALL_KIT, BLKITInstallMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This is positive test case for kit import validation functionality by loading data into
    * staging table and then check whether error code(s) has(have) been generated. Kit configuration
    * slot:NA Kit content configuration slot: COMHW-SYS Kit install configuration slot: ACFT-SYS-SYS
    * (inv_class_cd:BATCH)
    *
    */

   @Test
   public void test_Kit_KitInstall_NoConfigSlot_IMPORT() {

      System.out.println(
            "=======Starting:  " + testName.getMethodName() + " ===========================" );

      test_Kit_KitInstall_NoConfigSlot_VALIDATION();

      System.out.println( "Finish validation" );

      iGroupIDs = null;
      iINSTALLGroupIDs = null;

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      installMapValidation( "A0000011", "ABC11" );

   }


   /**
    * This is positive test case for kit import validation functionality by loading data into
    * staging table and then check whether error code(s) has(have) been generated. Kit configuration
    * slot:ACFT-SYS-SUBASSY, Kit content configuration slot: ACFT-SYS-TRK, Kit install configuration
    * slot: ACFT-SYS-SYS (inv_class_cd:BATCH)
    *
    */
   @Test
   public void test_Kit_ACFTSYSSUBASSYConfigSlot_KitInstall_ACFTSYSBATCH_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // BL_KIT table
      Map<String, String> BLKITMap = new LinkedHashMap<>();

      BLKITMap.put( "KIT_ASSMBL_CD", "\'ACFT_CD1\'" );
      BLKITMap.put( "KIT_CONFIG_SLOT_CD", "\'APU-ASSY\'" );
      BLKITMap.put( "KIT_PART_GROUP_CD", "\'AUTOKITGROUP\'" );
      BLKITMap.put( "KIT_PART_GROUP_NAME", "\'AUTOKITGROUP\'" );
      BLKITMap.put( "KIT_PART_GROUP_APPL_RANGE", "\'1914453,1914454\'" );
      BLKITMap.put( "KIT_PART_NO_OEM", "\'AUTOKIT\'" );
      BLKITMap.put( "KIT_MANUFACT_CD", "\'ABC11\'" );
      BLKITMap.put( "KIT_NAME", "\'AUTOKIT\'" );
      BLKITMap.put( "KIT_DESCRIPTION", "\'AUTOKIT\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_KIT, BLKITMap ) );

      // BL_KIT_CONTENT table
      Map<String, String> BLKITCONTENTMap = new LinkedHashMap<>();

      BLKITCONTENTMap.put( "KIT_PART_NO_OEM", "\'AUTOKIT\'" );
      BLKITCONTENTMap.put( "KIT_MANUFACT_CD", "\'ABC11\'" );
      BLKITCONTENTMap.put( "PART_NO_OEM", "\'A0000010\'" );
      BLKITCONTENTMap.put( "MANUFACT_CD", "\'11111\'" );
      BLKITCONTENTMap.put( "ASSMBL_CD", "\'ACFT_CD1\'" );
      BLKITCONTENTMap.put( "PART_GROUP_CD", "\'ACFT-SYS-1-1-TRK-BATCH-PARENT\'" );
      BLKITCONTENTMap.put( "PART_QTY", "\'1\'" );
      BLKITCONTENTMap.put( "VALUE_PCT", "\'100\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_KIT_CONTENT, BLKITCONTENTMap ) );

      // bl_install_kit table
      Map<String, String> BLKITInstallMap = new LinkedHashMap<>();

      BLKITInstallMap.put( "KIT_PART_NO_OEM", "\'AUTOKIT\'" );
      BLKITInstallMap.put( "KIT_MANUFACT_CD", "\'ABC11\'" );
      BLKITInstallMap.put( "INSTALL_ASSMBL_CD", "\'ACFT_CD1\'" );
      BLKITInstallMap.put( "INSTALL_PART_GROUP_CD", "\'ACFT-SYS-1-1-TRK-BATCH-CHILD\'" );
      BLKITInstallMap.put( "INSTALL_PART_NO_OEM", "\'A0000011\'" );
      BLKITInstallMap.put( "INSTALL_MANUFACT_CD", "\'ABC11\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_INSTALL_KIT, BLKITInstallMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This is positive test case for kit import validation functionality by loading data into
    * staging table and then check whether error code(s) has(have) been generated. Kit configuration
    * slot:ACFT-SYS-SUBASSY, Kit content configuration slot: ACFT-SYS-TRK, Kit install configuration
    * slot: ACFT-SYS-SYS (inv_class_cd:BATCH)
    *
    */
   @Test
   public void test_Kit_ACFTSYSSUBASSYConfigSlot_KitInstall_ACFTSYSBATCH_IMPORT() {

      System.out.println(
            "=======Starting:  " + testName.getMethodName() + " ===========================" );

      test_Kit_ACFTSYSSUBASSYConfigSlot_KitInstall_ACFTSYSBATCH_VALIDATION();

      System.out.println( "Finish validation" );

      iGroupIDs = null;
      iINSTALLGroupIDs = null;

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      installMapValidation( "A0000011", "ABC11" );
   }


   /**
    * This is positive test case for kit import validation functionality by loading data into
    * staging table and then check whether error code(s) has(have) been generated. Kit configuration
    * slot:APU-SYS (inv_class_cd=BATCH) Kit content configuration slot: APU-SYS (inv_class_cd=SER)
    * Kit install configuration slot: APU-SYS (inv_class_cd=SER)
    *
    */

   @Test
   public void test_Kit_APUSYSBATCHConfigSlot_KitInstall_APUSYSSER_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " validation========================" );

      // BL_KIT table
      Map<String, String> BLKITMap = new LinkedHashMap<>();

      BLKITMap.put( "KIT_ASSMBL_CD", "\'APU_CD1\'" );
      BLKITMap.put( "KIT_CONFIG_SLOT_CD", "\'APU-SYS-1-1\'" );
      BLKITMap.put( "KIT_PART_GROUP_CD", "\'AUTOKITGROUP\'" );
      BLKITMap.put( "KIT_PART_GROUP_NAME", "\'AUTOKITGROUP\'" );
      BLKITMap.put( "KIT_PART_GROUP_APPL_RANGE", "\'1914453,1914454\'" );
      BLKITMap.put( "KIT_PART_NO_OEM", "\'AUTOKIT\'" );
      BLKITMap.put( "KIT_MANUFACT_CD", "\'ABC11\'" );
      BLKITMap.put( "KIT_NAME", "\'AUTOKIT\'" );
      BLKITMap.put( "KIT_DESCRIPTION", "\'AUTOKIT\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_KIT, BLKITMap ) );

      // BL_KIT_CONTENT table
      Map<String, String> BLKITCONTENTMap = new LinkedHashMap<>();

      BLKITCONTENTMap.put( "KIT_PART_NO_OEM", "\'AUTOKIT\'" );
      BLKITCONTENTMap.put( "KIT_MANUFACT_CD", "\'ABC11\'" );
      BLKITCONTENTMap.put( "PART_NO_OEM", "\'AP0000012\'" );
      BLKITCONTENTMap.put( "MANUFACT_CD", "\'1234567890\'" );
      BLKITCONTENTMap.put( "ASSMBL_CD", "\'APU_CD1\'" );
      BLKITCONTENTMap.put( "PART_GROUP_CD", "\'APU-SYS-1-1-SER\'" );
      BLKITCONTENTMap.put( "PART_QTY", "\'1\'" );
      BLKITCONTENTMap.put( "VALUE_PCT", "\'100\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_KIT_CONTENT, BLKITCONTENTMap ) );

      // bl_install_kit table
      Map<String, String> BLKITInstallMap = new LinkedHashMap<>();

      BLKITInstallMap.put( "KIT_PART_NO_OEM", "\'AUTOKIT\'" );
      BLKITInstallMap.put( "KIT_MANUFACT_CD", "\'ABC11\'" );
      BLKITInstallMap.put( "INSTALL_ASSMBL_CD", "\'APU_CD1\'" );
      BLKITInstallMap.put( "INSTALL_PART_GROUP_CD", "\'APU-SYS-1-1-TRK-SER-CHILD\'" );
      BLKITInstallMap.put( "INSTALL_PART_NO_OEM", "\'AP0000014\'" );
      BLKITInstallMap.put( "INSTALL_MANUFACT_CD", "\'11111\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_INSTALL_KIT, BLKITInstallMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This is positive test case for kit import validation functionality by loading data into
    * staging table and then check whether error code(s) has(have) been generated. Kit configuration
    * slot:APU-SYS (inv_class_cd=BATCH) Kit content configuration slot: APU-SYS (inv_class_cd=SER)
    * Kit install configuration slot: APU-SYS (inv_class_cd=SER)
    *
    */

   @Test
   public void test_Kit_APUSYSBATCHConfigSlot_KitInstall_APUSYSSER_IMPORT() {

      System.out.println(
            "=======Starting:  " + testName.getMethodName() + "===============================" );

      test_Kit_APUSYSBATCHConfigSlot_KitInstall_APUSYSSER_VALIDATION();

      System.out.println( "Finish validation" );

      iGroupIDs = null;
      iINSTALLGroupIDs = null;

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      installMapValidation( "AP0000014", "11111" );

   }


   /**
    * This is positive test case for kit import validation functionality by loading data into
    * staging table and then check whether error code(s) has(have) been generated. Kit configuration
    * slot:ENG-SYS (inv_class_cd=BATCH) Kit content configuration slot: ENG-ASSY (inv_class_cd=TRK)
    * Kit install configuration slot: ENG (inv_class_cd=ASSY)
    *
    */

   @Test
   public void test_Kit_ENGSYSBATCHConfigSlot_KitInstall_ENGASSY_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + "validation ==============================" );

      // BL_KIT table
      Map<String, String> BLKITMap = new LinkedHashMap<>();

      BLKITMap.put( "KIT_ASSMBL_CD", "\'ENG_CD1\'" );
      BLKITMap.put( "KIT_CONFIG_SLOT_CD", "\'ENG-SYS-1-1\'" );
      BLKITMap.put( "KIT_PART_GROUP_CD", "\'AUTOKITGROUP\'" );
      BLKITMap.put( "KIT_PART_GROUP_NAME", "\'AUTOKITGROUP\'" );
      BLKITMap.put( "KIT_PART_GROUP_APPL_RANGE", null );
      BLKITMap.put( "KIT_PART_NO_OEM", "\'AUTOKIT\'" );
      BLKITMap.put( "KIT_MANUFACT_CD", "\'ABC11\'" );
      BLKITMap.put( "KIT_NAME", "\'AUTOKIT\'" );
      BLKITMap.put( "KIT_DESCRIPTION", "\'AUTOKIT\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_KIT, BLKITMap ) );

      // BL_KIT_CONTENT table
      Map<String, String> BLKITCONTENTMap = new LinkedHashMap<>();

      BLKITCONTENTMap.put( "KIT_PART_NO_OEM", "\'AUTOKIT\'" );
      BLKITCONTENTMap.put( "KIT_MANUFACT_CD", "\'ABC11\'" );
      BLKITCONTENTMap.put( "PART_NO_OEM", "\'E0000007\'" );
      BLKITCONTENTMap.put( "MANUFACT_CD", "\'ABC11\'" );
      BLKITCONTENTMap.put( "ASSMBL_CD", "\'ENG_CD1\'" );
      BLKITCONTENTMap.put( "PART_GROUP_CD", "\'ENG-SYS-1-1-TRK-SRU\'" );
      BLKITCONTENTMap.put( "PART_QTY", "\'1\'" );
      BLKITCONTENTMap.put( "VALUE_PCT", "\'100\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_KIT_CONTENT, BLKITCONTENTMap ) );

      // bl_install_kit table
      Map<String, String> BLKITInstallMap = new LinkedHashMap<>();

      BLKITInstallMap.put( "KIT_PART_NO_OEM", "\'AUTOKIT\'" );
      BLKITInstallMap.put( "KIT_MANUFACT_CD", "\'ABC11\'" );
      BLKITInstallMap.put( "INSTALL_ASSMBL_CD", "\'ENG_CD1\'" );
      BLKITInstallMap.put( "INSTALL_PART_GROUP_CD", "\'ENG_CD1\'" );
      BLKITInstallMap.put( "INSTALL_PART_NO_OEM", "\'ENG_ASSY_PN2\'" );
      BLKITInstallMap.put( "INSTALL_MANUFACT_CD", "\'ABC11\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_INSTALL_KIT, BLKITInstallMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );
   }


   /**
    * This is positive test case for kit import validation functionality by loading data into
    * staging table and then check whether error code(s) has(have) been generated. Kit configuration
    * slot:ENG-SYS (inv_class_cd=BATCH) Kit content configuration slot: ENG-ASSY (inv_class_cd=TRK)
    * Kit install configuration slot: ENG (inv_class_cd=ASSY)
    *
    */

   @Test
   public void test_Kit_ENGSYSBATCHConfigSlot_KitInstall_ENGASSY_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + "====================================" );

      test_Kit_ENGSYSBATCHConfigSlot_KitInstall_ENGASSY_VALIDATION();

      System.out.println( "Finish validation" );

      iGroupIDs = null;
      iINSTALLGroupIDs = null;

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      installMapValidation( "ENG_ASSY_PN2", "ABC11" );

   }


   /**
    * This is test case is to verify OPER-26368 fix on validation: Baseline Loader - Kit Content
    * does not properly Validate and Import alternate Parts. If two or more parts in the
    * BL_KIT_CONTENT table belong to the same part group, then they should be treated as
    * alternates.  This means that only one entry should be added to EQP_KIT_PART_GROUPS for the set
    * of alternates, and all of those parts should point back to that eqp_kit_part_group_db_id/id
    * from the EQP_KIT_PART_MAP table.
    *
    */
   @Test
   public void test_OPER26368_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + "validation ==============================" );

      // BL_KIT table
      Map<String, String> BLKITMap = new LinkedHashMap<>();

      BLKITMap.put( "KIT_ASSMBL_CD", "\'ACFT_CD1\'" );
      BLKITMap.put( "KIT_CONFIG_SLOT_CD", "\'APU-ASSY\'" );
      BLKITMap.put( "KIT_PART_GROUP_CD", "\'AUTOKITGROUP\'" );
      BLKITMap.put( "KIT_PART_GROUP_NAME", "\'AUTOKITGROUP\'" );
      BLKITMap.put( "KIT_PART_GROUP_APPL_RANGE", null );
      BLKITMap.put( "KIT_PART_NO_OEM", "\'AUTOKIT\'" );
      BLKITMap.put( "KIT_MANUFACT_CD", "\'ABC11\'" );
      BLKITMap.put( "KIT_NAME", "\'AUTOKIT\'" );
      BLKITMap.put( "KIT_DESCRIPTION", "\'AUTOKIT\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_KIT, BLKITMap ) );

      // BL_KIT_CONTENT table
      Map<String, String> BLKITCONTENTMap = new LinkedHashMap<>();
      // First record
      BLKITCONTENTMap.put( "KIT_PART_NO_OEM", "\'AUTOKIT\'" );
      BLKITCONTENTMap.put( "KIT_MANUFACT_CD", "\'ABC11\'" );
      BLKITCONTENTMap.put( "PART_NO_OEM", "\'A0000018A\'" );
      BLKITCONTENTMap.put( "MANUFACT_CD", "\'11111\'" );
      BLKITCONTENTMap.put( "ASSMBL_CD", "\'ACFT_CD1\'" );
      BLKITCONTENTMap.put( "PART_GROUP_CD", "\'ACFT-SYS-1-1-TRK-INTERCHANGABILITY\'" );
      BLKITCONTENTMap.put( "PART_QTY", "\'4\'" );
      BLKITCONTENTMap.put( "VALUE_PCT", "\'100\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_KIT_CONTENT, BLKITCONTENTMap ) );

      // second record
      BLKITCONTENTMap.clear();
      BLKITCONTENTMap.put( "KIT_PART_NO_OEM", "\'AUTOKIT\'" );
      BLKITCONTENTMap.put( "KIT_MANUFACT_CD", "\'ABC11\'" );
      BLKITCONTENTMap.put( "PART_NO_OEM", "\'A0000018B\'" );
      BLKITCONTENTMap.put( "MANUFACT_CD", "\'11111\'" );
      BLKITCONTENTMap.put( "ASSMBL_CD", "\'ACFT_CD1\'" );
      BLKITCONTENTMap.put( "PART_GROUP_CD", "\'ACFT-SYS-1-1-TRK-INTERCHANGABILITY\'" );
      BLKITCONTENTMap.put( "PART_QTY", "\'4\'" );
      BLKITCONTENTMap.put( "VALUE_PCT", "\'100\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_KIT_CONTENT, BLKITCONTENTMap ) );

      // third record
      BLKITCONTENTMap.clear();
      BLKITCONTENTMap.put( "KIT_PART_NO_OEM", "\'AUTOKIT\'" );
      BLKITCONTENTMap.put( "KIT_MANUFACT_CD", "\'ABC11\'" );
      BLKITCONTENTMap.put( "PART_NO_OEM", "\'A0000018C\'" );
      BLKITCONTENTMap.put( "MANUFACT_CD", "\'11111\'" );
      BLKITCONTENTMap.put( "ASSMBL_CD", "\'ACFT_CD1\'" );
      BLKITCONTENTMap.put( "PART_GROUP_CD", "\'ACFT-SYS-1-1-TRK-INTERCHANGABILITY\'" );
      BLKITCONTENTMap.put( "PART_QTY", "\'4\'" );
      BLKITCONTENTMap.put( "VALUE_PCT", "\'100\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_KIT_CONTENT, BLKITCONTENTMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );
   }


   /**
    * This is test case is to verify OPER-26368 fix on import: Baseline Loader - Kit Content does
    * not properly Validate and Import alternate Parts. If two or more parts in the BL_KIT_CONTENT
    * table belong to the same part group, then they should be treated as alternates.  This means
    * that only one entry should be added to EQP_KIT_PART_GROUPS for the set of alternates, and all
    * of those parts should point back to that eqp_kit_part_group_db_id/id from the EQP_KIT_PART_MAP
    * table.
    *
    */
   @Test
   public void test_OPER26368_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + "====================================" );

      test_OPER26368_VALIDATION();

      System.out.println( "Finish validation" );

      iGroupIDs = null;
      iINSTALLGroupIDs = null;
      String lKIT_PART_NO_ID = null;

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Get EQP_KIT_PART_GROUP_ID
      String sqlQuery =
            "select PART_NO_ID  from eqp_part_no where PART_NO_OEM='AUTOKIT' and PART_NO_DB_ID="
                  + CONS_DB_ID;
      strPartNoIdToDelete4 = getStringValueFromQuery( sqlQuery, "PART_NO_ID" );
      lKIT_PART_NO_ID = strPartNoIdToDelete4;
      iGroupIDs = getGroupId( lKIT_PART_NO_ID );

      // get part no of 3 parts
      sqlQuery =
            "select PART_NO_ID from eqp_part_no where PART_NO_OEM='A0000018A' and Manufact_cd='11111' and PART_NO_DB_ID="
                  + CONS_DB_ID;
      strPartNoIdAssmbly1 = getStringValueFromQuery( sqlQuery, "PART_NO_ID" );
      partNo pn1 = new partNo( Integer.toString( CONS_DB_ID ), strPartNoIdAssmbly1 );

      sqlQuery =
            "select PART_NO_ID from eqp_part_no where PART_NO_OEM='A0000018B' and Manufact_cd='11111' and PART_NO_DB_ID="
                  + CONS_DB_ID;
      strPartNoIdAssmbly2 = getStringValueFromQuery( sqlQuery, "PART_NO_ID" );
      partNo pn2 = new partNo( Integer.toString( CONS_DB_ID ), strPartNoIdAssmbly2 );

      sqlQuery =
            "select PART_NO_ID from eqp_part_no where PART_NO_OEM='A0000018C' and Manufact_cd='11111' and PART_NO_DB_ID="
                  + CONS_DB_ID;
      strPartNoIdAssmbly3 = getStringValueFromQuery( sqlQuery, "PART_NO_ID" );
      partNo pn3 = new partNo( Integer.toString( CONS_DB_ID ), strPartNoIdAssmbly3 );

      // Get BOM_PART_ID for 3 parts
      String lBOMPARTID = getBOMPARTIDs( "ACFT_CD1", "ACFT-SYS-1-1-TRK-INTERCHANGABILITY" );

      // Verify eqp_kit_part_map table
      // verify pn1 exist
      String lQuery =
            "select 1 from " + TableUtil.EQP_KIT_PART_MAP + " where EQP_KIT_PART_GROUP_DB_ID="
                  + iGroupIDs.get( 0 ).getNO_DB_ID() + " and EQP_KIT_PART_GROUP_ID="
                  + iGroupIDs.get( 0 ).getNO_ID() + " and PART_NO_ID='" + pn1.getPART_NO_ID() + "'";
      Assert.assertTrue( "Check eqp_kit_part_map table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify pn2 exist
      lQuery = "select 1 from " + TableUtil.EQP_KIT_PART_MAP + " where EQP_KIT_PART_GROUP_DB_ID="
            + iGroupIDs.get( 0 ).getNO_DB_ID() + " and EQP_KIT_PART_GROUP_ID="
            + iGroupIDs.get( 0 ).getNO_ID() + " and PART_NO_ID='" + pn2.getPART_NO_ID() + "'";
      Assert.assertTrue( "Check eqp_kit_part_map table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify pn3 exist
      lQuery = "select 1 from " + TableUtil.EQP_KIT_PART_MAP + " where EQP_KIT_PART_GROUP_DB_ID="
            + iGroupIDs.get( 0 ).getNO_DB_ID() + " and EQP_KIT_PART_GROUP_ID="
            + iGroupIDs.get( 0 ).getNO_ID() + " and PART_NO_ID='" + pn3.getPART_NO_ID() + "'";
      Assert.assertTrue( "Check eqp_kit_part_map table to verify the record exists",
            RecordsExist( lQuery ) );

      // Verify EQP_KIT_PART_GROUP_MAP
      lQuery =
            "select 1 from " + TableUtil.EQP_KIT_PART_GROUP_MAP + " where EQP_KIT_PART_GROUP_DB_ID="
                  + iGroupIDs.get( 0 ).getNO_DB_ID() + " and EQP_KIT_PART_GROUP_ID="
                  + iGroupIDs.get( 0 ).getNO_ID() + " and KIT_PART_NO_ID='" + lKIT_PART_NO_ID + "'";
      Assert.assertTrue( "Check eqp_kit_part_group_map table to verify the record exists",
            RecordsExist( lQuery ) );

      // Verify EQP_KIT_PART_GROUPS
      verifyEQPKITPARTGROUPS( iGroupIDs.get( 0 ), lBOMPARTID, "4", "1", 1 );

   }


   /**
    * This isto verify fix of OPER-27480: The tool should behave the same way as the GUI and tools
    * should be permitted. Change the validation so that its assmbl_class_cd in ('COMHW','TSE').
    * This test is on comhw.
    */
   @Test
   public void testOPER_27480_1_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // BL_KIT table
      Map<String, String> BLKITMap = new LinkedHashMap<>();

      BLKITMap.put( "KIT_ASSMBL_CD", "\'ACFT_CD1\'" );
      BLKITMap.put( "KIT_CONFIG_SLOT_CD", "'SYS-1-1'" );
      BLKITMap.put( "KIT_PART_GROUP_CD", "\'AUTOKITGROUP\'" );
      BLKITMap.put( "KIT_PART_GROUP_NAME", "\'AUTOKITGROUP\'" );
      BLKITMap.put( "KIT_PART_GROUP_APPL_RANGE", "\'1914453,1914454\'" );
      BLKITMap.put( "KIT_PART_NO_OEM", "\'AUTOKIT\'" );
      BLKITMap.put( "KIT_MANUFACT_CD", "\'ABC11\'" );
      BLKITMap.put( "KIT_NAME", "\'AUTOKIT\'" );
      BLKITMap.put( "KIT_DESCRIPTION", "\'AUTOKIT\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_KIT, BLKITMap ) );

      // BL_KIT_CONTENT table
      Map<String, String> BLKITCONTENTMap = new LinkedHashMap<>();

      BLKITCONTENTMap.put( "KIT_PART_NO_OEM", "\'AUTOKIT\'" );
      BLKITCONTENTMap.put( "KIT_MANUFACT_CD", "\'ABC11\'" );
      BLKITCONTENTMap.put( "PART_NO_OEM", "\'CHW000007\'" );
      BLKITCONTENTMap.put( "MANUFACT_CD", "\'ABC11\'" );
      BLKITCONTENTMap.put( "ASSMBL_CD", "\'COMHW\'" );
      BLKITCONTENTMap.put( "PART_GROUP_CD", "\'CHW-PG3\'" );
      BLKITCONTENTMap.put( "PART_QTY", "\'1\'" );
      BLKITCONTENTMap.put( "VALUE_PCT", "\'100\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_KIT_CONTENT, BLKITCONTENTMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This isto verify fix of OPER-27480: The tool should behave the same way as the GUI and tools
    * should be permitted. Change the validation so that its assmbl_class_cd in ('COMHW','TSE').
    * This test is on comhw.
    *
    */
   @Test
   public void testOPER_27480_1_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + "====================================" );

      testOPER_27480_1_VALIDATION();

      System.out.println( "Finish validation" );

      iGroupIDs = null;
      iINSTALLGroupIDs = null;
      String lKIT_PART_NO_ID = null;

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Get EQP_KIT_PART_GROUP_ID
      String sqlQuery =
            "select PART_NO_ID  from eqp_part_no where PART_NO_OEM='AUTOKIT' and PART_NO_DB_ID="
                  + CONS_DB_ID;
      strPartNoIdToDelete4 = getStringValueFromQuery( sqlQuery, "PART_NO_ID" );
      lKIT_PART_NO_ID = strPartNoIdToDelete4;
      iGroupIDs = getGroupId( lKIT_PART_NO_ID );

      // get part no of 3 parts
      sqlQuery =
            "select PART_NO_ID from eqp_part_no where PART_NO_OEM='CHW000007' and Manufact_cd='ABC11' and PART_NO_DB_ID="
                  + CONS_DB_ID;
      strPartNoIdAssmbly1 = getStringValueFromQuery( sqlQuery, "PART_NO_ID" );
      partNo pn1 = new partNo( Integer.toString( CONS_DB_ID ), strPartNoIdAssmbly1 );

      // Get BOM_PART_ID for 3 parts
      String lBOMPARTID = getBOMPARTIDs( "COMHW", "CHW-PG3" );

      // Verify eqp_kit_part_map table
      // verify pn1 exist
      String lQuery =
            "select 1 from " + TableUtil.EQP_KIT_PART_MAP + " where EQP_KIT_PART_GROUP_DB_ID="
                  + iGroupIDs.get( 0 ).getNO_DB_ID() + " and EQP_KIT_PART_GROUP_ID="
                  + iGroupIDs.get( 0 ).getNO_ID() + " and PART_NO_ID='" + pn1.getPART_NO_ID() + "'";
      Assert.assertTrue( "Check eqp_kit_part_map table to verify the record exists",
            RecordsExist( lQuery ) );

      // Verify EQP_KIT_PART_GROUP_MAP
      lQuery =
            "select 1 from " + TableUtil.EQP_KIT_PART_GROUP_MAP + " where EQP_KIT_PART_GROUP_DB_ID="
                  + iGroupIDs.get( 0 ).getNO_DB_ID() + " and EQP_KIT_PART_GROUP_ID="
                  + iGroupIDs.get( 0 ).getNO_ID() + " and KIT_PART_NO_ID='" + lKIT_PART_NO_ID + "'";
      Assert.assertTrue( "Check eqp_kit_part_group_map table to verify the record exists",
            RecordsExist( lQuery ) );

      // Verify EQP_KIT_PART_GROUPS
      verifyEQPKITPARTGROUPS( iGroupIDs.get( 0 ), lBOMPARTID, "1", "1", 1 );

   }


   /**
    * This isto verify fix of OPER-27480: The tool should behave the same way as the GUI and tools
    * should be permitted. Change the validation so that its assmbl_class_cd in ('COMHW','TSE').
    * This test is on TSE.
    */
   @Test
   public void testOPER_27480_2_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // BL_KIT table
      Map<String, String> BLKITMap = new LinkedHashMap<>();

      BLKITMap.put( "KIT_ASSMBL_CD", "\'ACFT_CD1\'" );
      BLKITMap.put( "KIT_CONFIG_SLOT_CD", "'SYS-1-1'" );
      BLKITMap.put( "KIT_PART_GROUP_CD", "\'AUTOKITGROUP\'" );
      BLKITMap.put( "KIT_PART_GROUP_NAME", "\'AUTOKITGROUP\'" );
      BLKITMap.put( "KIT_PART_GROUP_APPL_RANGE", "\'1914453,1914454\'" );
      BLKITMap.put( "KIT_PART_NO_OEM", "\'AUTOKIT\'" );
      BLKITMap.put( "KIT_MANUFACT_CD", "\'ABC11\'" );
      BLKITMap.put( "KIT_NAME", "\'AUTOKIT\'" );
      BLKITMap.put( "KIT_DESCRIPTION", "\'AUTOKIT\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_KIT, BLKITMap ) );

      // BL_KIT_CONTENT table
      Map<String, String> BLKITCONTENTMap = new LinkedHashMap<>();

      BLKITCONTENTMap.put( "KIT_PART_NO_OEM", "\'AUTOKIT\'" );
      BLKITCONTENTMap.put( "KIT_MANUFACT_CD", "\'ABC11\'" );
      BLKITCONTENTMap.put( "PART_NO_OEM", "\'AT000001\'" );
      BLKITCONTENTMap.put( "MANUFACT_CD", "\'ABC11\'" );
      BLKITCONTENTMap.put( "ASSMBL_CD", "\'TSE\'" );
      BLKITCONTENTMap.put( "PART_GROUP_CD", "\'ATGROUP2\'" );
      BLKITCONTENTMap.put( "PART_QTY", "\'1\'" );
      BLKITCONTENTMap.put( "VALUE_PCT", "\'100\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_KIT_CONTENT, BLKITCONTENTMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This isto verify fix of OPER-27480: The tool should behave the same way as the GUI and tools
    * should be permitted. Change the validation so that its assmbl_class_cd in ('COMHW','TSE').
    * This test is on TSE.
    */

   @Test
   public void testOPER_27480_2_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + "====================================" );

      testOPER_27480_2_VALIDATION();

      System.out.println( "Finish validation" );

      iGroupIDs = null;
      iINSTALLGroupIDs = null;
      String lKIT_PART_NO_ID = null;

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Get EQP_KIT_PART_GROUP_ID
      String sqlQuery =
            "select PART_NO_ID  from eqp_part_no where PART_NO_OEM='AUTOKIT' and PART_NO_DB_ID="
                  + CONS_DB_ID;
      strPartNoIdToDelete4 = getStringValueFromQuery( sqlQuery, "PART_NO_ID" );
      lKIT_PART_NO_ID = strPartNoIdToDelete4;
      iGroupIDs = getGroupId( lKIT_PART_NO_ID );

      // get part no of 3 parts
      sqlQuery =
            "select PART_NO_ID from eqp_part_no where PART_NO_OEM='AT000001' and Manufact_cd='ABC11' and PART_NO_DB_ID="
                  + CONS_DB_ID;
      strPartNoIdAssmbly1 = getStringValueFromQuery( sqlQuery, "PART_NO_ID" );
      partNo pn1 = new partNo( Integer.toString( CONS_DB_ID ), strPartNoIdAssmbly1 );

      // Get BOM_PART_ID for 3 parts
      String lBOMPARTID = getBOMPARTIDs( "TSE", "ATGROUP2" );

      // Verify eqp_kit_part_map table
      // verify pn1 exist
      String lQuery =
            "select 1 from " + TableUtil.EQP_KIT_PART_MAP + " where EQP_KIT_PART_GROUP_DB_ID="
                  + iGroupIDs.get( 0 ).getNO_DB_ID() + " and EQP_KIT_PART_GROUP_ID="
                  + iGroupIDs.get( 0 ).getNO_ID() + " and PART_NO_ID='" + pn1.getPART_NO_ID() + "'";
      Assert.assertTrue( "Check eqp_kit_part_map table to verify the record exists",
            RecordsExist( lQuery ) );

      // Verify EQP_KIT_PART_GROUP_MAP
      lQuery =
            "select 1 from " + TableUtil.EQP_KIT_PART_GROUP_MAP + " where EQP_KIT_PART_GROUP_DB_ID="
                  + iGroupIDs.get( 0 ).getNO_DB_ID() + " and EQP_KIT_PART_GROUP_ID="
                  + iGroupIDs.get( 0 ).getNO_ID() + " and KIT_PART_NO_ID='" + lKIT_PART_NO_ID + "'";
      Assert.assertTrue( "Check eqp_kit_part_group_map table to verify the record exists",
            RecordsExist( lQuery ) );

      // Verify EQP_KIT_PART_GROUPS
      verifyEQPKITPARTGROUPS( iGroupIDs.get( 0 ), lBOMPARTID, "1", "1", 1 );

   }


   /**
    * This isto verify fix of OPER-27461: Kits Validation fails with an error if kits are assigned
    * to multiple part groups on different assemblies. Error code = BLKIT-00070
    *
    */
   @Test
   public void testOPER_27461_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // BL_KIT table
      Map<String, String> BLKITMap = new LinkedHashMap<>();

      BLKITMap.put( "KIT_ASSMBL_CD", "\'ACFT_CD1\'" );
      BLKITMap.put( "KIT_CONFIG_SLOT_CD", "\'APU-ASSY\'" );
      BLKITMap.put( "KIT_PART_GROUP_CD", "\'AUTOKITGROUP\'" );
      BLKITMap.put( "KIT_PART_GROUP_NAME", "\'AUTOKITGROUP\'" );
      BLKITMap.put( "KIT_PART_GROUP_APPL_RANGE", "\'1914453,1914454\'" );
      BLKITMap.put( "KIT_PART_NO_OEM", "\'AUTOKIT\'" );
      BLKITMap.put( "KIT_MANUFACT_CD", "\'ABC11\'" );
      BLKITMap.put( "KIT_NAME", "\'AUTOKIT\'" );
      BLKITMap.put( "KIT_DESCRIPTION", "\'AUTOKIT\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_KIT, BLKITMap ) );

      // second record
      BLKITMap.clear();
      BLKITMap.put( "KIT_ASSMBL_CD", "\'ENG_CD1\'" );
      BLKITMap.put( "KIT_CONFIG_SLOT_CD", "\'ENG-SYS-1-1\'" );
      BLKITMap.put( "KIT_PART_GROUP_CD", "\'AUTOKITGROUP2\'" );
      BLKITMap.put( "KIT_PART_GROUP_NAME", "\'AUTOKITGROUP2\'" );
      BLKITMap.put( "KIT_PART_GROUP_APPL_RANGE", "\'1914453,1914454\'" );
      BLKITMap.put( "KIT_PART_NO_OEM", "\'AUTOKIT\'" );
      BLKITMap.put( "KIT_MANUFACT_CD", "\'ABC11\'" );
      BLKITMap.put( "KIT_NAME", "\'AUTOKIT2\'" );
      BLKITMap.put( "KIT_DESCRIPTION", "\'AUTOKIT2\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_KIT, BLKITMap ) );

      // BL_KIT_CONTENT table
      Map<String, String> BLKITCONTENTMap = new LinkedHashMap<>();

      BLKITCONTENTMap.put( "KIT_PART_NO_OEM", "\'AUTOKIT\'" );
      BLKITCONTENTMap.put( "KIT_MANUFACT_CD", "\'ABC11\'" );
      BLKITCONTENTMap.put( "PART_NO_OEM", "\'A0000010\'" );
      BLKITCONTENTMap.put( "MANUFACT_CD", "\'11111\'" );
      BLKITCONTENTMap.put( "ASSMBL_CD", "\'ACFT_CD1\'" );
      BLKITCONTENTMap.put( "PART_GROUP_CD", "\'ACFT-SYS-1-1-TRK-BATCH-PARENT\'" );
      BLKITCONTENTMap.put( "PART_QTY", "\'1\'" );
      BLKITCONTENTMap.put( "VALUE_PCT", "\'100\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_KIT_CONTENT, BLKITCONTENTMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );
      checkUsageErrorCode( testName.getMethodName(), "BLKIT-00070" );

   }


   /**
    * This isto verify fix of OPER-27425: BL Kit Content validation does not allow Value Pct to be 0
    * when possible in UI.
    *
    */
   @Test
   public void testOPER_27425_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // BL_KIT table
      Map<String, String> BLKITMap = new LinkedHashMap<>();

      BLKITMap.put( "KIT_ASSMBL_CD", "\'ACFT_CD1\'" );
      BLKITMap.put( "KIT_CONFIG_SLOT_CD", "\'APU-ASSY\'" );
      BLKITMap.put( "KIT_PART_GROUP_CD", "\'AUTOKITGROUP\'" );
      BLKITMap.put( "KIT_PART_GROUP_NAME", "\'AUTOKITGROUP\'" );
      BLKITMap.put( "KIT_PART_GROUP_APPL_RANGE", "\'1914453,1914454\'" );
      BLKITMap.put( "KIT_PART_NO_OEM", "\'AUTOKIT\'" );
      BLKITMap.put( "KIT_MANUFACT_CD", "\'ABC11\'" );
      BLKITMap.put( "KIT_NAME", "\'AUTOKIT\'" );
      BLKITMap.put( "KIT_DESCRIPTION", "\'AUTOKIT\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_KIT, BLKITMap ) );

      // BL_KIT_CONTENT table
      Map<String, String> BLKITCONTENTMap = new LinkedHashMap<>();

      BLKITCONTENTMap.put( "KIT_PART_NO_OEM", "\'AUTOKIT\'" );
      BLKITCONTENTMap.put( "KIT_MANUFACT_CD", "\'ABC11\'" );
      BLKITCONTENTMap.put( "PART_NO_OEM", "\'A0000010\'" );
      BLKITCONTENTMap.put( "MANUFACT_CD", "\'11111\'" );
      BLKITCONTENTMap.put( "ASSMBL_CD", "\'ACFT_CD1\'" );
      BLKITCONTENTMap.put( "PART_GROUP_CD", "\'ACFT-SYS-1-1-TRK-BATCH-PARENT\'" );
      BLKITCONTENTMap.put( "PART_QTY", "\'1\'" );
      BLKITCONTENTMap.put( "VALUE_PCT", "\'0\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_KIT_CONTENT, BLKITCONTENTMap ) );

      // second records
      BLKITCONTENTMap.clear();
      BLKITCONTENTMap.put( "KIT_PART_NO_OEM", "\'AUTOKIT\'" );
      BLKITCONTENTMap.put( "KIT_MANUFACT_CD", "\'ABC11\'" );
      BLKITCONTENTMap.put( "PART_NO_OEM", "\'A0000002'" );
      BLKITCONTENTMap.put( "MANUFACT_CD", "\'11111\'" );
      BLKITCONTENTMap.put( "ASSMBL_CD", "\'ACFT_CD1\'" );
      BLKITCONTENTMap.put( "PART_GROUP_CD", "\'ACFT-SYS-1-1-TRK-P2\'" );
      BLKITCONTENTMap.put( "PART_QTY", "\'1\'" );
      BLKITCONTENTMap.put( "VALUE_PCT", "\'100\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_KIT_CONTENT, BLKITCONTENTMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   @Test
   public void testOPER_27425_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + "====================================" );

      testOPER_27425_VALIDATION();

      System.out.println( "Finish validation" );

      iGroupIDs = null;
      iINSTALLGroupIDs = null;
      String lKIT_PART_NO_ID = null;

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Get EQP_KIT_PART_GROUP_ID
      String sqlQuery =
            "select PART_NO_ID  from eqp_part_no where PART_NO_OEM='AUTOKIT' and PART_NO_DB_ID="
                  + CONS_DB_ID;
      strPartNoIdToDelete4 = getStringValueFromQuery( sqlQuery, "PART_NO_ID" );
      lKIT_PART_NO_ID = strPartNoIdToDelete4;
      iGroupIDs = getGroupId( lKIT_PART_NO_ID );

      // get part no of 2 parts
      sqlQuery =
            "select PART_NO_ID from eqp_part_no where PART_NO_OEM='A0000010' and Manufact_cd='11111' and PART_NO_DB_ID="
                  + CONS_DB_ID;
      strPartNoIdAssmbly1 = getStringValueFromQuery( sqlQuery, "PART_NO_ID" );
      partNo pn1 = new partNo( Integer.toString( CONS_DB_ID ), strPartNoIdAssmbly1 );

      sqlQuery =
            "select PART_NO_ID from eqp_part_no where PART_NO_OEM='A0000002' and Manufact_cd='11111' and PART_NO_DB_ID="
                  + CONS_DB_ID;
      strPartNoIdAssmbly2 = getStringValueFromQuery( sqlQuery, "PART_NO_ID" );
      partNo pn2 = new partNo( Integer.toString( CONS_DB_ID ), strPartNoIdAssmbly2 );

      // Get BOM_PART_ID for 2 parts
      String lBOMPARTID1 = getBOMPARTIDs( "ACFT_CD1", "ACFT-SYS-1-1-TRK-BATCH-PARENT" );
      String lBOMPARTID2 = getBOMPARTIDs( "ACFT_CD1", "ACFT-SYS-1-1-TRK-P2" );

      // Verify eqp_kit_part_map table
      // verify pn1 exist
      // regroup iGroupIDs
      String lgrpId1 = getGRPIDs( pn1.getPART_NO_ID(), iGroupIDs );
      String lgrpId2 = getGRPIDs( pn2.getPART_NO_ID(), iGroupIDs );

      iGroupIDs.get( 0 ).setNO_ID( lgrpId1 );
      iGroupIDs.get( 1 ).setNO_ID( lgrpId2 );

      String lQuery =
            "select 1 from " + TableUtil.EQP_KIT_PART_MAP + " where EQP_KIT_PART_GROUP_DB_ID="
                  + iGroupIDs.get( 0 ).getNO_DB_ID() + " and EQP_KIT_PART_GROUP_ID="
                  + iGroupIDs.get( 0 ).getNO_ID() + " and PART_NO_ID='" + pn1.getPART_NO_ID() + "'";
      Assert.assertTrue( "Check eqp_kit_part_map table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify pn2 exist
      lQuery = "select 1 from " + TableUtil.EQP_KIT_PART_MAP + " where EQP_KIT_PART_GROUP_DB_ID="
            + iGroupIDs.get( 1 ).getNO_DB_ID() + " and EQP_KIT_PART_GROUP_ID="
            + iGroupIDs.get( 1 ).getNO_ID() + " and PART_NO_ID='" + pn2.getPART_NO_ID() + "'";
      Assert.assertTrue( "Check eqp_kit_part_map table to verify the record exists",
            RecordsExist( lQuery ) );

      // Verify EQP_KIT_PART_GROUP_MAP
      lQuery =
            "select 1 from " + TableUtil.EQP_KIT_PART_GROUP_MAP + " where EQP_KIT_PART_GROUP_DB_ID="
                  + iGroupIDs.get( 0 ).getNO_DB_ID() + " and EQP_KIT_PART_GROUP_ID="
                  + iGroupIDs.get( 0 ).getNO_ID() + " and KIT_PART_NO_ID='" + lKIT_PART_NO_ID + "'";
      Assert.assertTrue( "Check eqp_kit_part_group_map table to verify the record exists",
            RecordsExist( lQuery ) );

      lQuery =
            "select 1 from " + TableUtil.EQP_KIT_PART_GROUP_MAP + " where EQP_KIT_PART_GROUP_DB_ID="
                  + iGroupIDs.get( 1 ).getNO_DB_ID() + " and EQP_KIT_PART_GROUP_ID="
                  + iGroupIDs.get( 1 ).getNO_ID() + " and KIT_PART_NO_ID='" + lKIT_PART_NO_ID + "'";
      Assert.assertTrue( "Check eqp_kit_part_group_map table to verify the record exists",
            RecordsExist( lQuery ) );

      // Verify EQP_KIT_PART_GROUPS
      verifyEQPKITPARTGROUPS( iGroupIDs.get( 0 ), lBOMPARTID1, "1", "0", 1 );
      verifyEQPKITPARTGROUPS( iGroupIDs.get( 1 ), lBOMPARTID2, "1", "1", 1 );

   }


   // ////////////////////////////////////////////////////////////

   /**
    * This function is to retrieve INSTALL_KIT_MAP IDs
    *
    *
    */
   public ArrayList<simpleIDs> getINSTALLGroupId( String aKIT_PART_NO_ID ) {

      String[] iIds = { "EQP_INSTALL_KIT_MAP_DB_ID", "EQP_INSTALL_KIT_MAP_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "KIT_PART_NO_DB_ID", Integer.toString( CONS_DB_ID ) );
      lArgs.addArguments( "KIT_PART_NO_ID", aKIT_PART_NO_ID );

      String iQueryString =
            TableUtil.buildTableQuery( TableUtil.EQP_INSTALL_KIT_MAP, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      ArrayList<simpleIDs> lIds = new ArrayList<simpleIDs>();
      for ( int i = 0; i < llists.size(); i++ ) {
         lIds.add( new simpleIDs( llists.get( i ).get( 0 ), llists.get( i ).get( 1 ) ) );

      }
      return lIds;
   }


   /**
    * This function is to retrieve KIT_PART_GROUP IDs
    *
    *
    */

   public ArrayList<simpleIDs> getGroupId( String aKIT_PART_NO_ID ) {

      String[] iIds = { "EQP_KIT_PART_GROUP_DB_ID", "EQP_KIT_PART_GROUP_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "KIT_PART_NO_DB_ID", Integer.toString( CONS_DB_ID ) );
      lArgs.addArguments( "KIT_PART_NO_ID", aKIT_PART_NO_ID );

      String iQueryString =
            TableUtil.buildTableQuery( TableUtil.EQP_KIT_PART_GROUP_MAP, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      ArrayList<simpleIDs> lIds = new ArrayList<simpleIDs>();
      for ( int i = 0; i < llists.size(); i++ ) {
         lIds.add( new simpleIDs( llists.get( i ).get( 0 ), llists.get( i ).get( 1 ) ) );

      }
      return lIds;
   }


   /**
    * This function is to retrieve eqp_kit_part_group_id to regroup. This is only working on fix
    * group with 2 groups.
    *
    *
    */
   public String getGRPIDs( String apn, ArrayList<simpleIDs> aGroupIDs ) {

      String lquery = "SELECT eqp_kit_part_group_id FROM eqp_kit_part_map " + "WHERE part_no_id="
            + apn + " AND eqp_kit_part_group_id IN ('" + aGroupIDs.get( 0 ).getNO_ID() + "', '"
            + aGroupIDs.get( 1 ).getNO_ID() + "')";
      return getStringValueFromQuery( lquery, "eqp_kit_part_group_id" );

   }


   /**
    * This function is to validate MXI tables with staging table c_assmbl_list
    *
    *
    */

   public void CheckImportValidation( String strTableName, String strASSMBLCD,
         String strASSMBLBOMID, List<String> Linfor ) {

      try {
         switch ( strTableName ) {

            case "EQP_BOM_PART":
               compareEQPBOMPartAndBLKIT( strASSMBLCD, strASSMBLBOMID, "KIT" );
               break;
            case "EQP_BOM_PART_2":
               compareEQPBOMPartAndBLKIT2( strASSMBLCD, strASSMBLBOMID, "KIT" );
               break;
            case "EQP_PART_NO":
               compareEQPPartNoAndBLKIT( "KIT" );
               break;
            case "EQP_KIT_PART_GROUP_MAP":
               compareEQPKitPartGroupMap( strPartNoIdToDelete1, strPartNoIdToDelete2, Linfor );
               break;
            case "EQP_KIT_PART_GROUP_MAP_2":
               compareEQPKitPartGroupMap( strPartNoIdAssmbly, strPartNoIdToDelete3, Linfor );
               break;
            default:
               System.out.println( "No table available to compare" );

         }

      } catch ( Exception e ) {
         e.printStackTrace();
      }

   }


   /**
    * This function is to retrieve BOM PART IDs.
    *
    *
    */
   public String getBOMPARTIDs( String aASSMBL_CD, String aBOM_PART_CD ) {

      String[] iIds = { "BOM_PART_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "ASSMBL_CD", aASSMBL_CD );
      lArgs.addArguments( "BOM_PART_CD", aBOM_PART_CD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EQP_BOM_PART, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      return llists.get( 0 ).get( 0 );

   }


   /**
    * This function is to verify eqp_kit_part_groups table by given parameters.
    *
    *
    */
   public void verifyEQPKITPARTGROUPS( simpleIDs aGroupIDs, String aBOMPARTID, String aKIT_QT,
         String aVALUE_PCT, int aRowNum ) {
      String[] iIds = { "KIT_QT", "VALUE_PCT" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "EQP_KIT_PART_GROUP_DB_ID", aGroupIDs.getNO_DB_ID() );
      lArgs.addArguments( "EQP_KIT_PART_GROUP_ID", aGroupIDs.getNO_ID() );
      lArgs.addArguments( "BOM_PART_ID", aBOMPARTID );

      String iQueryString =
            TableUtil.buildTableQuery( TableUtil.EQP_KIT_PART_GROUPS, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "Row number must match the value provided.", llists.size() == aRowNum );
      Assert.assertTrue( "KIT_QT", llists.get( 0 ).get( 0 ).equalsIgnoreCase( aKIT_QT ) );
      Assert.assertTrue( "VALUE_PCT", llists.get( 0 ).get( 1 ).equalsIgnoreCase( aVALUE_PCT ) );

   }


   /**
    * This function is to verify eqp_kit_part_groups table by given parameters.
    *
    *
    */
   public void verifyEQPKITPARTGROUPS2( simpleIDs aGroupIDs, String aBOMPARTID, String aKIT_QT,
         String aVALUE_PCT, int aRowNum ) {
      String[] iIds = { "KIT_QT", "VALUE_PCT" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "EQP_KIT_PART_GROUP_DB_ID", aGroupIDs.getNO_DB_ID() );
      lArgs.addArguments( "EQP_KIT_PART_GROUP_ID", aGroupIDs.getNO_ID() );
      lArgs.addArguments( "BOM_PART_ID", aBOMPARTID );

      String iQueryString =
            TableUtil.buildTableQuery( TableUtil.EQP_KIT_PART_GROUPS, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "Row number must match the value provided.", llists.size() == aRowNum );
      Assert.assertTrue( "KIT_QT", llists.get( 0 ).get( 0 ).equalsIgnoreCase( aKIT_QT ) );
      Assert.assertTrue( "VALUE_PCT", llists.get( 0 ).get( 1 ).equalsIgnoreCase( aVALUE_PCT ) );

   }


   /**
    * This function is to be called before and after test cases.
    *
    *
    */

   public void cleanupTables() {
      if ( iGroupIDs != null ) {
         for ( int i = 0; i < iGroupIDs.size(); i++ ) {
            String lquery = "delete from " + TableUtil.EQP_KIT_PART_MAP
                  + " where EQP_KIT_PART_GROUP_DB_ID='" + iGroupIDs.get( i ).getNO_DB_ID()
                  + "' and EQP_KIT_PART_GROUP_ID='" + iGroupIDs.get( i ).getNO_ID() + "'";
            executeSQL( lquery );

            lquery = "delete from " + TableUtil.EQP_KIT_PART_GROUP_MAP
                  + " where EQP_KIT_PART_GROUP_DB_ID='" + iGroupIDs.get( i ).getNO_DB_ID()
                  + "' and EQP_KIT_PART_GROUP_ID='" + iGroupIDs.get( i ).getNO_ID() + "'";
            executeSQL( lquery );

            lquery = "delete from " + TableUtil.EQP_KIT_PART_GROUPS
                  + " where EQP_KIT_PART_GROUP_DB_ID='" + iGroupIDs.get( i ).getNO_DB_ID()
                  + "' and EQP_KIT_PART_GROUP_ID='" + iGroupIDs.get( i ).getNO_ID() + "'";
            executeSQL( lquery );
         }

      }

      if ( iINSTALLGroupIDs != null ) {
         for ( int i = 0; i < iINSTALLGroupIDs.size(); i++ ) {
            String lquery = "delete from " + TableUtil.EQP_INSTALL_KIT_MAP
                  + " where EQP_INSTALL_KIT_MAP_DB_ID='" + iINSTALLGroupIDs.get( i ).getNO_DB_ID()
                  + "' and EQP_INSTALL_KIT_MAP_ID='" + iINSTALLGroupIDs.get( i ).getNO_ID() + "'";
            executeSQL( lquery );

            lquery = "delete from " + TableUtil.EQP_INSTALL_KIT_PART_MAP
                  + " where EQP_INSTALL_KIT_MAP_DB_ID='" + iINSTALLGroupIDs.get( i ).getNO_DB_ID()
                  + "' and EQP_INSTALL_KIT_MAP_ID='" + iINSTALLGroupIDs.get( i ).getNO_ID() + "'";
            executeSQL( lquery );

         }
      }

   }


   /**
    * This function is setup data for kit import The main purpose of this step of data setup is to
    * create a configuration slot COMHW-SYS for kit.
    *
    *
    */

   public void dataSetup() {

      System.out.println( "========Starting data setup ===========" );

      StringBuilder StrModifiedquery = new StringBuilder();

      // Get max (assmbl_bom_id)
      int strAssmblBomIdNext = getIntValueFromQuery(
            "select max(assmbl_bom_id) from eqp_assmbl_bom where assmbl_cd='COMHW'",
            "MAX(ASSMBL_BOM_ID)" ) + 1;

      // Get Bom Class Db Id
      int strBomClassDbId = getIntValueFromQuery(
            "select BOM_CLASS_DB_ID from eqp_assmbl_bom where assmbl_cd='COMHW'",
            "BOM_CLASS_DB_ID" );

      // insert comhw-sys in eqp_assmbl_bom
      createRecordInEQPAssmblBom( "COMHW", Integer.toString( strAssmblBomIdNext ),
            Integer.toString( strBomClassDbId ), "SYS", "1", "AUTOTEST CHW SYS",
            "AUTOTEST CHW SYS" );

      // Get max(BOM_PART_ID) from eqp_bom_part
      int strBomPartIdNext =
            getIntValueFromQuery( "select max(BOM_PART_ID) from eqp_bom_part", "MAX(BOM_PART_ID)" )
                  + 1;
      strBomPartIdToDelete1 = Integer.toString( strBomPartIdNext );

      // Get INV_CLASS_DB_ID
      int strInvClassDbId =
            getIntValueFromQuery( "select INV_CLASS_DB_ID from eqp_bom_part", "INV_CLASS_DB_ID" );

      // create comhw-sys bom part in eqp_bom_part table
      createRecordInEQPBOMPART( Integer.toString( strBomPartIdNext ), "COMHW",
            Integer.toString( strAssmblBomIdNext ), Integer.toString( strBomClassDbId ),
            Integer.toString( strInvClassDbId ), "BATCH", "CHW-AUTO-PG1", "CHW-AUTO-PG1", "1" );

      // Get part no id of next
      StrModifiedquery.delete( 0, StrModifiedquery.length() );
      int strPartNoIdNext =
            getIntValueFromQuery( "select max(PART_NO_ID) from eqp_part_no", "MAX(PART_NO_ID)" )
                  + 1;
      strPartNoIdToDelete1 = Integer.toString( strPartNoIdNext );

      // create a part in EQP_PART_NO table
      createRecordInEQPPARTNO( Integer.toString( strPartNoIdNext ),
            Integer.toString( strInvClassDbId ), "COMHW", "11111", "AUTOTEST", "AUTOTEST" );

      // create a link in EQP_PART_BASELINE table with part group and part no
      createRecordInEQPPARTBASELINE( Integer.toString( strBomPartIdNext ),
            Integer.toString( strPartNoIdNext ) );

   }


   /**
    * This function is clean up for data setup which created a configuration slot COMHW-SYS for kit.
    *
    */

   public void AssmblydataCleanup() {
      System.out.println( "========Starting AssmblydataCleanup===========" );
      String sqlQuery = null;
      PreparedStatement lStatement;
      String strPartNoIdToDelete = null;
      String strBomPartIdToDelete = null;

      try {
         // Clean up KIT
         // Get BOM_PART_ID and PART_NO_ID for KIT to delete
         sqlQuery =
               "select PART_NO_ID  from eqp_part_no where PART_NO_OEM='AUTOTEST' and PART_NO_DB_ID="
                     + CONS_DB_ID;
         strPartNoIdToDelete = getStringValueFromQuery( sqlQuery, "PART_NO_ID" );

         if ( strPartNoIdToDelete != null && !strPartNoIdToDelete.isEmpty() ) {
            sqlQuery = "select BOM_PART_ID from eqp_part_baseline where part_No_id="
                  + strPartNoIdToDelete;
            strBomPartIdToDelete = getStringValueFromQuery( sqlQuery, "BOM_PART_ID" );
         }

         // delete records from eqp_part_no
         if ( strPartNoIdToDelete != null && !strPartNoIdToDelete.isEmpty() ) {
            sqlQuery = "delete from eqp_part_no where PART_NO_ID=" + strPartNoIdToDelete
                  + " and PART_NO_DB_ID=" + CONS_DB_ID;
            lStatement = getConnection().prepareStatement( sqlQuery );
            lStatement.executeUpdate( sqlQuery );
            commit();

         }

         // delete records from eqp_part_baseline
         if ( strBomPartIdToDelete != null && !strBomPartIdToDelete.isEmpty() ) {
            sqlQuery = "delete from EQP_PART_BASELINE where BOM_PART_ID=" + strBomPartIdToDelete
                  + " and BOM_PART_DB_ID=" + CONS_DB_ID;
            lStatement = getConnection().prepareStatement( sqlQuery );
            lStatement.executeUpdate( sqlQuery );
            commit();

         }

         // delete records from EQP_BOM_PART
         sqlQuery = "delete EQP_BOM_PART where BOM_PART_NAME like '%AUTOKIT%' and BOM_PART_DB_ID="
               + CONS_DB_ID;
         lStatement = getConnection().prepareStatement( sqlQuery );
         lStatement.executeUpdate( sqlQuery );

         // delete records from EQP_ASSMBL_BOM
         sqlQuery = "delete EQP_ASSMBL_BOM where ASSMBL_BOM_CD like 'AUTOTEST CHW SYS'";
         lStatement = getConnection().prepareStatement( sqlQuery );
         lStatement.executeUpdate( sqlQuery );

         // Get BOM_PART_ID
         strBomPartIdToDelete = null;
         sqlQuery = "select BOM_PART_ID from eqp_bom_part where BOM_PART_CD='CHW-AUTO-PG1'";
         strBomPartIdToDelete = getStringValueFromQuery( sqlQuery, "BOM_PART_ID" );

         // Get PART_NO_ID
         strPartNoIdToDelete = null;
         if ( strBomPartIdToDelete != null && !strBomPartIdToDelete.isEmpty() ) {
            sqlQuery = "select PART_NO_ID from eqp_part_baseline where bom_part_id="
                  + strBomPartIdToDelete;
            strPartNoIdToDelete = getStringValueFromQuery( sqlQuery, "PART_NO_ID" );
         }

         // delete records from eqp_part_no
         if ( strPartNoIdToDelete != null && !strPartNoIdToDelete.isEmpty() ) {
            sqlQuery = "delete from eqp_part_no where PART_NO_ID=" + strPartNoIdToDelete
                  + " and PART_NO_DB_ID=" + CONS_DB_ID;
            lStatement = getConnection().prepareStatement( sqlQuery );
            lStatement.executeUpdate( sqlQuery );
            commit();

         }
         // delete records from eqp_part_baseline
         if ( strBomPartIdToDelete != null && !strBomPartIdToDelete.isEmpty() ) {
            sqlQuery = "delete from EQP_PART_BASELINE where BOM_PART_ID=" + strBomPartIdToDelete
                  + " and BOM_PART_DB_ID=" + CONS_DB_ID;
            lStatement = getConnection().prepareStatement( sqlQuery );
            lStatement.executeUpdate( sqlQuery );
            commit();

         }

         // delete records from EQP_BOM_PART
         if ( strBomPartIdToDelete != null && !strBomPartIdToDelete.isEmpty() ) {
            sqlQuery = "delete from eqp_bom_part where bom_part_id=" + strBomPartIdToDelete
                  + " and BOM_PART_DB_ID=" + CONS_DB_ID;
            lStatement = getConnection().prepareStatement( sqlQuery );
            lStatement.executeUpdate( sqlQuery );
            commit();

         }

      } catch ( SQLException e ) {
         e.printStackTrace();
      }

   }


   /**
    * This function is clean up for every TC. Since after each test, kit will be generate in
    * EQP_BOM_PART, eqp_part_no, EQP_BOM_PART and eqp_part_baseline tables, this setup will clean up
    * all the created data and resume DB.
    *
    */

   public void dataCleanup() {
      System.out.println( "========Starting cleanup ===========" );
      String sqlQuery = null;
      PreparedStatement lStatement;

      try {
         // Clean up KIT
         // Get BOM_PART_ID and PART_NO_ID for KIT to delete
         sqlQuery =
               "select BOM_PART_ID from EQP_BOM_PART where BOM_PART_NAME like '%AUTOKIT%' and BOM_PART_DB_ID="
                     + CONS_DB_ID;
         String strBomPartIdToDelete = getStringValueFromQuery( sqlQuery, "BOM_PART_ID" );

         sqlQuery =
               "select PART_NO_ID  from eqp_part_no where PART_NO_OEM like '%AUTOKIT%' and PART_NO_DB_ID="
                     + CONS_DB_ID;
         String strPartNoIdToDelete = getStringValueFromQuery( sqlQuery, "PART_NO_ID" );

         // delete records from eqp_part_no
         if ( strPartNoIdToDelete != null && !strPartNoIdToDelete.isEmpty() ) {
            sqlQuery = "delete from eqp_part_no where PART_NO_ID=" + strPartNoIdToDelete
                  + " and PART_NO_DB_ID=" + CONS_DB_ID;
            lStatement = getConnection().prepareStatement( sqlQuery );
            lStatement.executeUpdate( sqlQuery );
            commit();

         }

         // delete records from eqp_part_baseline
         if ( strBomPartIdToDelete != null && !strBomPartIdToDelete.isEmpty() ) {
            sqlQuery = "select * from EQP_PART_BASELINE where BOM_PART_ID=" + strBomPartIdToDelete
                  + " and BOM_PART_DB_ID=" + CONS_DB_ID;
            lStatement = getConnection().prepareStatement( sqlQuery );
            lStatement.executeUpdate( sqlQuery );
            commit();

         }

         // delete records from EQP_BOM_PART
         sqlQuery = "delete EQP_BOM_PART where BOM_PART_NAME like '%AUTOKIT%'";
         lStatement = getConnection().prepareStatement( sqlQuery );
         lStatement.executeUpdate( sqlQuery );

      } catch ( SQLException e ) {
         e.printStackTrace();
      }

   }


   /**
    * This function is to implement interface ValidationAndImport
    *
    * @param: blnOnlyValidation
    *
    */
   public int runValidationAndImport( boolean blnOnlyValidation, boolean allornone ) {
      int rtValue = 0;
      validationandimport = new ValidationAndImport() {

         @Override
         public int runValidation( boolean allornone ) {
            int lReturn = 0;
            CallableStatement lPrepareCallKIT;

            try {
               if ( allornone ) {
                  lPrepareCallKIT = getConnection().prepareCall(
                        "BEGIN  bl_kit_import.validate(aiv_exist_in_mx => 'STRICT', aib_allornone => true, aon_retcode =>?, aov_retmsg =>?); END;" );
               } else {
                  lPrepareCallKIT = getConnection().prepareCall(
                        "BEGIN  bl_kit_import.validate(aiv_exist_in_mx => 'STRICT',aib_allornone => false, aon_retcode =>?, aov_retmsg =>?); END;" );

               }

               lPrepareCallKIT.registerOutParameter( 1, Types.INTEGER );
               lPrepareCallKIT.registerOutParameter( 2, Types.VARCHAR );
               lPrepareCallKIT.execute();
               commit();
               lReturn = lPrepareCallKIT.getInt( 1 );
            } catch ( SQLException e ) {
               e.printStackTrace();
            }
            return lReturn;

         }


         @Override
         public int runImport( boolean allornone ) {
            int lReturn = 0;
            CallableStatement lPrepareCallKIT;

            try {
               if ( allornone ) {
                  lPrepareCallKIT = getConnection().prepareCall(
                        "BEGIN  bl_kit_import.import(aiv_exist_in_mx => 'STRICT', aib_allornone => true, aon_retcode =>?, aov_retmsg =>?); END;" );
               } else {
                  lPrepareCallKIT = getConnection().prepareCall(
                        "BEGIN  bl_kit_import.import(aiv_exist_in_mx => 'STRICT',aib_allornone => false, aon_retcode =>?, aov_retmsg =>?); END;" );

               }

               lPrepareCallKIT.registerOutParameter( 1, Types.INTEGER );
               lPrepareCallKIT.registerOutParameter( 2, Types.VARCHAR );
               lPrepareCallKIT.execute();
               commit();
               lReturn = lPrepareCallKIT.getInt( 1 );
            } catch ( SQLException e ) {
               e.printStackTrace();
            }
            return lReturn;
         }

      };

      rtValue = blnOnlyValidation ? validationandimport.runValidation( allornone )
            : validationandimport.runImport( allornone );

      return rtValue;
   }


   /**
    * Check EQP_KIT_PART_MAP table
    *
    * @return void
    */

   public void compareEQPKitPartGroupMap( String strAmblyPartNoID, String strKitPartNo,
         List<String> linfor ) {
      String strBomPArtID = compareEQPKitPartGroupMap_part( strAmblyPartNoID, strKitPartNo );

      compareEQPBOMPartAndExpected( strBomPArtID, 1, linfor.get( 0 ), linfor.get( 1 ),
            linfor.get( 2 ), linfor.get( 3 ) );

   }


   /**
    * Check EQP_KIT_PART_MAP table
    *
    * @return void
    */

   public String compareEQPKitPartGroupMap_part( String strAmblyPartNoID, String strKitPartNo ) {

      // Check record in EQP_KIT_PART_MAP table
      String sqlQuery = "select EQP_KIT_PART_GROUP_ID from EQP_KIT_PART_MAP where PART_NO_ID="
            + strAmblyPartNoID;
      String strAssmblyGroupID =
            getStringValueFromQuery( sqlQuery, "EQP_KIT_PART_GROUP_ID" ).trim();
      Assert.assertTrue( strAssmblyGroupID != null && !strAssmblyGroupID.isEmpty() );

      // Check record in EQP_KIT_PART_GROUP_MAP table
      sqlQuery = "select EQP_KIT_PART_GROUP_ID from EQP_KIT_PART_GROUP_MAP where KIT_PART_NO_ID="
            + strKitPartNo;
      String strKitGroupID = getStringValueFromQuery( sqlQuery, "EQP_KIT_PART_GROUP_ID" ).trim();
      Assert.assertTrue( strKitGroupID != null && !strKitGroupID.isEmpty() );

      // Check group ids are same in EQP_KIT_PART_MAP and EQP_KIT_PART_GROUP_MAP tables
      Assert.assertTrue( strAssmblyGroupID.equalsIgnoreCase( strKitGroupID ) );

      // Get BOM_PART_ID
      sqlQuery = "select BOM_PART_ID from EQP_KIT_PART_GROUPS where EQP_KIT_PART_GROUP_ID="
            + strKitGroupID;
      String strBomPArtID = getStringValueFromQuery( sqlQuery, "BOM_PART_ID" ).trim();
      Assert.assertTrue( strBomPArtID != null && !strBomPArtID.isEmpty() );

      return strBomPArtID;

   }


   /**
    * Validation of eqp_install_kit, eqp_install_kit_part_map tables
    *
    * @return void
    */

   public void installMapValidation( String PartNoOem, String ManuCD ) {

      String lKIT_PART_NO_ID = null;

      // Verify eqp_install_kit, eqp_install_kit_part_map tables
      String sqlQuery =
            "select BOM_PART_ID from EQP_BOM_PART where BOM_PART_NAME like '%AUTOKITGROUP%'";
      // String strBomPartId = getStringValueFromQuery( sqlQuery, "BOM_PART_ID" );

      sqlQuery = "select PART_NO_ID  from eqp_part_no where PART_NO_OEM='AUTOKIT'";
      String strPartNoId = getStringValueFromQuery( sqlQuery, "PART_NO_ID" );
      lKIT_PART_NO_ID = strPartNoId;

      sqlQuery = "select PART_NO_ID from eqp_part_no where PART_NO_OEM='" + PartNoOem
            + "' and Manufact_cd='" + ManuCD + "'";
      strPartNoIdAssmbly = getStringValueFromQuery( sqlQuery, "PART_NO_ID" );
      // partNo pn = new partNo( Integer.toString( CONS_DB_ID ), strPartNoIdAssmbly );

      // Verify the kit map group are equal
      sqlQuery = "select EQP_INSTALL_KIT_MAP_ID from EQP_INSTALL_KIT_MAP where KIT_PART_NO_DB_ID="
            + Integer.toString( CONS_DB_ID ) + " and KIT_PART_NO_ID=" + strPartNoId;
      String strMapID = getStringValueFromQuery( sqlQuery, "EQP_INSTALL_KIT_MAP_ID" );
      partNo kitmapinfor = new partNo( Integer.toString( CONS_DB_ID ), strMapID );

      sqlQuery = "select EQP_INSTALL_KIT_MAP_ID from EQP_INSTALL_KIT_PART_MAP where PART_NO_DB_ID="
            + Integer.toString( CONS_DB_ID ) + " and PART_NO_ID=" + strPartNoIdAssmbly;
      strMapID = getStringValueFromQuery( sqlQuery, "EQP_INSTALL_KIT_MAP_ID" );
      partNo kitpartmapinfor = new partNo( Integer.toString( CONS_DB_ID ), strMapID );

      Assert.assertTrue( kitmapinfor.equals( kitpartmapinfor ) );

      // Get Group Ids
      iGroupIDs = getGroupId( lKIT_PART_NO_ID );
      iINSTALLGroupIDs = getINSTALLGroupId( lKIT_PART_NO_ID );

   }


   /**
    * Check appl_range value
    *
    * @return void
    */

   public void checkApplRange( String strRange, String strBOMPARTID ) {

      if ( strRange != null ) {
         String sqlQuery = "Select APPL_EFF_LDESC from EQP_BOM_PART where BOM_PART_DB_ID="
               + CONS_DB_ID + " and BOM_PART_ID=" + strBOMPARTID;
         String strRangeDB = getStringValueFromQuery( sqlQuery, "APPL_EFF_LDESC" ).trim();

         Assert.assertTrue( "checkApplRange:", strRangeDB.equalsIgnoreCase( strRange ) );
      }

   }


   /**
    * Check QT and PCT value
    *
    * @return void
    */

   public void checkQTandPCT( String strBomPartId, String strQT, String strPCT ) {

      // Check QT value
      String sqlQuery = "Select KIT_QT from EQP_KIT_PART_GROUPS where BOM_PART_DB_ID=" + CONS_DB_ID
            + " and BOM_PART_ID=" + strBomPartId;
      String strQTDB = Integer.toString( getIntValueFromQuery( sqlQuery, "KIT_QT" ) );
      Assert.assertTrue( "KIT_QT:", strQTDB.equalsIgnoreCase( strQT ) );

      // Check PCT value
      sqlQuery = "Select VALUE_PCT from EQP_KIT_PART_GROUPS where BOM_PART_DB_ID=" + CONS_DB_ID
            + " and BOM_PART_ID=" + strBomPartId;
      String strPCTDB = getDecimalValueFromQuery( sqlQuery, "VALUE_PCT" ).toString();
      Assert.assertTrue( "VALUE_PCT:", strPCTDB.equalsIgnoreCase( strPCT ) );

   }


   /**
    * Calls check KIT error code
    *
    *
    */
   protected void checkUsageErrorCode( String aTestName, String aValidationCode ) {

      boolean lFound = false;
      String lerror_desc = null;
      String lerrorMsg = null;

      String lquery = TestConstants.BL_KIT_ERROR_CHECK;

      String[] iIds = { "RESULT_CD", "USER_DESC" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      List<ArrayList<String>> lresult = execute( lquery, lfields );

      for ( int i = 0; i < lresult.size(); i++ ) {
         if ( lresult.get( i ).get( 0 ).equalsIgnoreCase( aValidationCode ) ) {
            lerror_desc = lresult.get( i ).get( 1 );
            lFound = true;
         } else {
            lerrorMsg = lerrorMsg + lresult.get( i ).get( 0 ) + ": " + lresult.get( i ).get( 1 );
         }

      }

      Assert.assertTrue( "The error code not found- " + aValidationCode + " : " + lerror_desc
            + " other error found [" + lerrorMsg + "]", lFound );

   }


   /**
    * This class is inner class to hold data when user query db to verify kit content is uploaded
    * correctly.
    *
    */

   public class kitBomPart {

      String assmbl_cd;
      String bom_part_cd;
      String bom_part_name;
      String inv_class_cd;
      int kit_qt;
      double value_pct;


      public kitBomPart(String assmbl_cd, String bom_part_cd, String bom_part_name,
            String inv_class_cd, int kit_qt, double value_pct) {
         this.assmbl_cd = assmbl_cd;
         this.bom_part_cd = bom_part_cd;
         this.bom_part_name = bom_part_name;
         this.inv_class_cd = inv_class_cd;
         this.kit_qt = kit_qt;
         this.value_pct = value_pct;
      }


      /**
       * {@inheritDoc}
       */
      @Override
      public int hashCode() {
         final int prime = 31;
         int result = 1;
         result = prime * result + getOuterType().hashCode();
         result = prime * result + ( ( assmbl_cd == null ) ? 0 : assmbl_cd.hashCode() );
         result = prime * result + ( ( bom_part_cd == null ) ? 0 : bom_part_cd.hashCode() );
         result = prime * result + ( ( bom_part_name == null ) ? 0 : bom_part_name.hashCode() );
         result = prime * result + ( ( inv_class_cd == null ) ? 0 : inv_class_cd.hashCode() );
         result = prime * result + kit_qt;
         long temp;
         temp = Double.doubleToLongBits( value_pct );
         result = prime * result + ( int ) ( temp ^ ( temp >>> 32 ) );
         return result;
      }


      /**
       * {@inheritDoc}
       */
      @Override
      public boolean equals( Object obj ) {
         if ( this == obj )
            return true;
         if ( obj == null )
            return false;
         if ( getClass() != obj.getClass() )
            return false;
         kitBomPart other = ( kitBomPart ) obj;
         if ( !getOuterType().equals( other.getOuterType() ) )
            return false;
         if ( assmbl_cd == null ) {
            if ( other.assmbl_cd != null )
               return false;
         } else if ( !assmbl_cd.equalsIgnoreCase( other.assmbl_cd ) )
            return false;
         if ( bom_part_cd == null ) {
            if ( other.bom_part_cd != null )
               return false;
         } else if ( !bom_part_cd.equalsIgnoreCase( other.bom_part_cd ) )
            return false;
         if ( bom_part_name == null ) {
            if ( other.bom_part_name != null )
               return false;
         } else if ( !bom_part_name.equalsIgnoreCase( other.bom_part_name ) )
            return false;
         if ( inv_class_cd == null ) {
            if ( other.inv_class_cd != null )
               return false;
         } else if ( !inv_class_cd.equalsIgnoreCase( other.inv_class_cd ) )
            return false;
         if ( kit_qt != other.kit_qt )
            return false;
         if ( Double.doubleToLongBits( value_pct ) != Double.doubleToLongBits( other.value_pct ) )
            return false;
         return true;
      }


      private Kit getOuterType() {
         return Kit.this;
      }
   }

}
