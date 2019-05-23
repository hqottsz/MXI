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
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.mxi.mx.core.maint.plan.datamodels.partInfo;
import com.mxi.mx.core.maint.plan.datamodels.partNo;
import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;
import com.mxi.mx.util.BaselineLoaderTest;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.ValidationAndImport;
import com.mxi.mx.util.WhereClause;


/**
 * This test suite contains test cases on validation and import functionality of ASSMEMBLY_IMPORT
 * package in Assembly Information area.
 *
 * @author ALICIA QIAN
 */

@FixMethodOrder( MethodSorters.NAME_ASCENDING )
public class AssemblyInformation extends BaselineLoaderTest {

   ValidationAndImport validationandimport;

   public simpleIDs iBOM_PART_IDs = null;
   public simpleIDs iPART_NO_IDs_1 = null;
   public simpleIDs iPART_NO_IDs_2 = null;


   /**
    * Setup before executing each individual test
    *
    */
   @Before
   @Override
   public void before() throws Exception {

      super.before();
      clearBaselineLoaderTables();
      iBOM_PART_IDs = null;
      iPART_NO_IDs_1 = null;
      iPART_NO_IDs_2 = null;
      RestoreData();

   }


   /**
    * Clean up after each individual test
    */
   @After
   @Override
   public void after() {
      RestoreData();
      super.after();
   }


   /**
    * This test is to verify ASSMEMBLY_IMPORT validation functionality of staging table
    * c_assmbl_list on ACFT
    *
    */
   @Test
   public void test01_Assembly_IMPORT_ASSMBLIST_ACFT_VALIDATION() {

      // C_Assmbl_List table
      Map<String, String> lCAssmblList = new LinkedHashMap<>();

      //
      lCAssmblList.put( "ASSMBL_CD", "\'ACFT_AT1\'" );
      lCAssmblList.put( "ASSMBL_NAME", "\'ACFT AT ASSMBL\'" );
      lCAssmblList.put( "ASSMBL_CLASS_CD", "\'ACFT\'" );
      lCAssmblList.put( "ETOPS_BOOL", "\'N\'" );
      lCAssmblList.put( "PN1_MANUFACT_CD", "\'10001\'" );
      lCAssmblList.put( "PN1_PART_NO_OEM", "\'ACFT_AT_PN1\'" );
      lCAssmblList.put( "PN1_PART_NO_SDESC", "\'AUTO ACFT 1\'" );
      lCAssmblList.put( "PN2_MANUFACT_CD", "\'10001\'" );
      lCAssmblList.put( "PN2_PART_NO_OEM", "\'ACFT_AT_PN2\'" );
      lCAssmblList.put( "PN2_PART_NO_SDESC", "\'AUTO ACFT 2\'" );
      lCAssmblList.put( "PN3_MANUFACT_CD", "\'11111\'" );
      lCAssmblList.put( "PN3_PART_NO_OEM", "\'ACFT_AT_PN3\'" );
      lCAssmblList.put( "PN3_PART_NO_SDESC", "\'AUTO ACFT 3\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ASSMBL_LIST, lCAssmblList ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );
   }


   /**
    * This test is to verify ASSMEMBLY_IMPORT import functionality of staging table c_assmbl_list on
    * ACFT
    *
    */
   @Test
   public void test02_Assembly_IMPORT_ASSMBLIST_ACFT_IMPORT() {
      test01_Assembly_IMPORT_ASSMBLIST_ACFT_VALIDATION();

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify EQP_ASSMBL table
      CheckImportValidation( "EQP_ASSMBL" );
      // Verify EQP_ASSMBL_BOM table
      CheckImportValidation( "EQP_ASSMBL_BOM" );
      // Verify EQP_ASSMBL_POS table
      CheckImportValidation( "EQP_ASSMBL_POS" );
      // Verify EQP_BOM_PART table
      CheckImportValidation( "EQP_BOM_PART" );
      // Verify EQP_BOM_PART and EQP_PART_BASE_LINE and EQP_PART_NO
      CheckPartsInformation_ACFT();
      // Verify EQP_DATA_SOURCE table
      CheckImportValidation( "EQP_DATA_SOURCE" );

   }


   /**
    * This test is to verify ASSMEMBLY_IMPORT validation functionality of staging table c_ata_sys on
    * ACFT
    *
    */
   @Test
   public void test03_Assembly_IMPORT_ATA_SYS_ACFT_VALIDATION() {

      // c_ata_sys table
      Map<String, String> lCATASYSMap = new LinkedHashMap<>();
      // #1
      lCATASYSMap.put( "ASSMBL_CD", "\'ACFT_AT1\'" );
      lCATASYSMap.put( "ATA_SYS_CD", "\'SYS-ENG\'" );
      lCATASYSMap.put( "NH_ATA_SYS_CD", null );
      lCATASYSMap.put( "ATA_SYS_NAME", "\'AT1 Engine System\'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ATA_SYS, lCATASYSMap ) );

      // #2
      lCATASYSMap.clear();
      lCATASYSMap.put( "ASSMBL_CD", "\'ACFT_AT1\'" );
      lCATASYSMap.put( "ATA_SYS_CD", "\'SYS-APU\'" );
      lCATASYSMap.put( "NH_ATA_SYS_CD", null );
      lCATASYSMap.put( "ATA_SYS_NAME", "\'AT1 APU System\'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ATA_SYS, lCATASYSMap ) );

      // #3
      lCATASYSMap.clear();
      lCATASYSMap.put( "ASSMBL_CD", "\'ACFT_AT1\'" );
      lCATASYSMap.put( "ATA_SYS_CD", "\'SYS-1-2\'" );
      lCATASYSMap.put( "NH_ATA_SYS_CD", "\'SYS-1\'" );
      lCATASYSMap.put( "ATA_SYS_NAME", "\'AT1 System 1-1\'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ATA_SYS, lCATASYSMap ) );

      // #4
      lCATASYSMap.clear();
      lCATASYSMap.put( "ASSMBL_CD", "\'ACFT_AT1\'" );
      lCATASYSMap.put( "ATA_SYS_CD", "\'SYS-2-1\'" );
      lCATASYSMap.put( "NH_ATA_SYS_CD", "\'SYS-2\'" );
      lCATASYSMap.put( "ATA_SYS_NAME", "\'AT1 System 2-1\'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ATA_SYS, lCATASYSMap ) );

      // #5
      lCATASYSMap.clear();
      lCATASYSMap.put( "ASSMBL_CD", "\'ACFT_AT1\'" );
      lCATASYSMap.put( "ATA_SYS_CD", "\'SYS-2\'" );
      lCATASYSMap.put( "NH_ATA_SYS_CD", null );
      lCATASYSMap.put( "ATA_SYS_NAME", "\'AT1 System 2\'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ATA_SYS, lCATASYSMap ) );

      // #6
      lCATASYSMap.clear();
      lCATASYSMap.put( "ASSMBL_CD", "\'ACFT_AT1\'" );
      lCATASYSMap.put( "ATA_SYS_CD", "\'SYS-1-1\'" );
      lCATASYSMap.put( "NH_ATA_SYS_CD", "\'SYS-1\'" );
      lCATASYSMap.put( "ATA_SYS_NAME", "\'AT1 System 1-1\'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ATA_SYS, lCATASYSMap ) );

      // #7
      lCATASYSMap.clear();
      lCATASYSMap.put( "ASSMBL_CD", "\'ACFT_AT1\'" );
      lCATASYSMap.put( "ATA_SYS_CD", "\'SYS-2-2\'" );
      lCATASYSMap.put( "NH_ATA_SYS_CD", "\'SYS-2\'" );
      lCATASYSMap.put( "ATA_SYS_NAME", "\'AT1 System 2-2\'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ATA_SYS, lCATASYSMap ) );

      // #8
      lCATASYSMap.clear();
      lCATASYSMap.put( "ASSMBL_CD", "\'ACFT_AT1\'" );
      lCATASYSMap.put( "ATA_SYS_CD", "\'SYS-2-1-1\'" );
      lCATASYSMap.put( "NH_ATA_SYS_CD", "\'SYS-2-1\'" );
      lCATASYSMap.put( "ATA_SYS_NAME", "\'AT1 System 2-1-1\'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ATA_SYS, lCATASYSMap ) );

      // #9
      lCATASYSMap.clear();
      lCATASYSMap.put( "ASSMBL_CD", "\'ACFT_AT1\'" );
      lCATASYSMap.put( "ATA_SYS_CD", "\'SYS-1\'" );
      lCATASYSMap.put( "NH_ATA_SYS_CD", null );
      lCATASYSMap.put( "ATA_SYS_NAME", "\'AT1 System 1\'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ATA_SYS, lCATASYSMap ) );

      // run validation with c_assmbl_list staging table
      test01_Assembly_IMPORT_ASSMBLIST_ACFT_VALIDATION();

   }


   /**
    * This test is to verify ASSMEMBLY_IMPORT import functionality of staging table c_ata_sys on
    * ACFT
    *
    */
   @Test
   public void test04_Assembly_IMPORT_ATA_SYS_ACFT_IMPORT() {

      test03_Assembly_IMPORT_ATA_SYS_ACFT_VALIDATION();
      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify EQP_ASSMBL_BOM table
      CheckImportValidation( "EQP_ASSMBL_BOM_ATA" );

   }


   /**
    * This test is to verify ASSMEMBLY_IMPORT validation functionality of staging table
    * c_assmbl_list on ACFT without any parts
    *
    */
   @Test
   public void test05_Assembly_IMPORT_ASSMBLIST_NOPARTS_VALIDATION() {

      // C_Assmbl_List table
      Map<String, String> lCAssmblList = new LinkedHashMap<>();

      //
      lCAssmblList.put( "ASSMBL_CD", "\'NOPT_AT1\'" );
      lCAssmblList.put( "ASSMBL_NAME", "\'NO PARTS AT ASSMBL\'" );
      lCAssmblList.put( "ASSMBL_CLASS_CD", "\'ACFT\'" );
      lCAssmblList.put( "ETOPS_BOOL", "\'N\'" );
      lCAssmblList.put( "PN1_MANUFACT_CD", "\'10001\'" );
      lCAssmblList.put( "PN1_PART_NO_OEM", "\'NO_PARTS_AT_PN1\'" );
      lCAssmblList.put( "PN1_PART_NO_SDESC", "\'AUTO NO_PART 1\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ASSMBL_LIST, lCAssmblList ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );
   }


   /**
    * This test is to verify ASSMEMBLY_IMPORT import functionality of staging table c_assmbl_list on
    * ACFT without any parts
    *
    */
   @Test
   public void test06_Assembly_IMPORT_ASSMBLIST_NOPARTS_IMPORT() {
      test05_Assembly_IMPORT_ASSMBLIST_NOPARTS_VALIDATION();

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify EQP_ASSMBL table
      CheckImportValidation( "EQP_ASSMBL" );
      // Verify EQP_ASSMBL_BOM table
      CheckImportValidation( "EQP_ASSMBL_BOM" );
      // Verify EQP_ASSMBL_POS table
      CheckImportValidation( "EQP_ASSMBL_POS" );
      // Verify EQP_BOM_PART table
      CheckImportValidation( "EQP_BOM_PART" );
      // Verify EQP_BOM_PART and EQP_PART_BASE_LINE and EQP_PART_NO
      CheckPartsInformation_NOPARTS();
      // Verify EQP_DATA_SOURCE table
      CheckImportValidation( "EQP_DATA_SOURCE" );

   }


   /**
    * This test is to verify ASSMEMBLY_IMPORT validation functionality of staging table
    * c_assmbl_list on APU
    *
    */
   @Test
   public void test07_Assembly_IMPORT_ASSMBLIST_APU_VALIDATION() {

      // C_Assmbl_List table
      Map<String, String> lCAssmblList = new LinkedHashMap<>();

      //
      lCAssmblList.put( "ASSMBL_CD", "\'APU_AT1\'" );
      lCAssmblList.put( "ASSMBL_NAME", "\'APU AT ASSMBL\'" );
      lCAssmblList.put( "ASSMBL_CLASS_CD", "\'APU\'" );
      lCAssmblList.put( "ETOPS_BOOL", "\'N\'" );
      lCAssmblList.put( "PN1_MANUFACT_CD", "\'1234567890\'" );
      lCAssmblList.put( "PN1_PART_NO_OEM", "\'APU_AT_PN1\'" );
      lCAssmblList.put( "PN1_PART_NO_SDESC", "\'AUTO APU 1\'" );
      lCAssmblList.put( "PN2_MANUFACT_CD", "\'ABC11\'" );
      lCAssmblList.put( "PN2_PART_NO_OEM", "\'APU_AT_PN2\'" );
      lCAssmblList.put( "PN2_PART_NO_SDESC", "\'AUTO APU 2\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ASSMBL_LIST, lCAssmblList ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );
   }


   /**
    * This test is to verify ASSMEMBLY_IMPORT import functionality of staging table c_assmbl_list on
    * APU
    *
    */

   @Test
   public void test08_Assembly_IMPORT_ASSMBLIST_APU_IMPORT() {
      test07_Assembly_IMPORT_ASSMBLIST_APU_VALIDATION();

      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify EQP_ASSMBL table
      CheckImportValidation( "EQP_ASSMBL" );
      // Verify EQP_ASSMBL_BOM table
      CheckImportValidation( "EQP_ASSMBL_BOM" );
      // Verify EQP_ASSMBL_POS table
      CheckImportValidation( "EQP_ASSMBL_POS" );
      // Verify EQP_BOM_PART table
      CheckImportValidation( "EQP_BOM_PART" );
      // Verify EQP_BOM_PART and EQP_PART_BASE_LINE and EQP_PART_NO
      CheckPartsInformation_APU();
      // Verify EQP_DATA_SOURCE table
      CheckImportValidation( "EQP_DATA_SOURCE" );

   }


   /**
    * This test is to verify ASSMEMBLY_IMPORT validation functionality of staging table c_ata_sys on
    * APU
    *
    */
   @Test
   public void test09_Assembly_IMPORT_ATA_SYS_APU_VALIDATION() {

      // c_ata_sys table
      Map<String, String> lCATASYSMap = new LinkedHashMap<>();
      // #1
      lCATASYSMap.put( "ASSMBL_CD", "\'APU_AT1\'" );
      lCATASYSMap.put( "ATA_SYS_CD", "\'APU-SYS-2\'" );
      lCATASYSMap.put( "NH_ATA_SYS_CD", null );
      lCATASYSMap.put( "ATA_SYS_NAME", "\'AT1 APU System 2\'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ATA_SYS, lCATASYSMap ) );

      // #2
      lCATASYSMap.clear();
      lCATASYSMap.put( "ASSMBL_CD", "\'APU_AT1\'" );
      lCATASYSMap.put( "ATA_SYS_CD", "\'APU-SYS-2-2\'" );
      lCATASYSMap.put( "NH_ATA_SYS_CD", "\'APU-SYS-2\'" );
      lCATASYSMap.put( "ATA_SYS_NAME", "\'AT1 APU System 2-2\'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ATA_SYS, lCATASYSMap ) );

      // #3
      lCATASYSMap.clear();
      lCATASYSMap.put( "ASSMBL_CD", "\'APU_AT1\'" );
      lCATASYSMap.put( "ATA_SYS_CD", "\'APU-SYS-1\'" );
      lCATASYSMap.put( "NH_ATA_SYS_CD", null );
      lCATASYSMap.put( "ATA_SYS_NAME", "\'AT1 APU System 1\'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ATA_SYS, lCATASYSMap ) );

      // #4
      lCATASYSMap.clear();
      lCATASYSMap.put( "ASSMBL_CD", "\'APU_AT1\'" );
      lCATASYSMap.put( "ATA_SYS_CD", "\'APU-SYS-2-1-1\'" );
      lCATASYSMap.put( "NH_ATA_SYS_CD", "\'APU-SYS-2-1\'" );
      lCATASYSMap.put( "ATA_SYS_NAME", "\'AT1 APU System 2-1-1\'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ATA_SYS, lCATASYSMap ) );

      // #5
      lCATASYSMap.clear();
      lCATASYSMap.put( "ASSMBL_CD", "\'APU_AT1\'" );
      lCATASYSMap.put( "ATA_SYS_CD", "\'APU-SYS-2-1\'" );
      lCATASYSMap.put( "NH_ATA_SYS_CD", "\'APU-SYS-2\'" );
      lCATASYSMap.put( "ATA_SYS_NAME", "\'AT1 APU System 2-1\'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ATA_SYS, lCATASYSMap ) );

      // #6
      lCATASYSMap.clear();
      lCATASYSMap.put( "ASSMBL_CD", "\'APU_AT1\'" );
      lCATASYSMap.put( "ATA_SYS_CD", "\'APU-SYS-1-2\'" );
      lCATASYSMap.put( "NH_ATA_SYS_CD", "\'APU-SYS-1\'" );
      lCATASYSMap.put( "ATA_SYS_NAME", "\'AT1 APU System 1-1\'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ATA_SYS, lCATASYSMap ) );

      // #7
      lCATASYSMap.clear();
      lCATASYSMap.put( "ASSMBL_CD", "\'APU_AT1\'" );
      lCATASYSMap.put( "ATA_SYS_CD", "\'APU-SYS-1-1\'" );
      lCATASYSMap.put( "NH_ATA_SYS_CD", "\'APU-SYS-1\'" );
      lCATASYSMap.put( "ATA_SYS_NAME", "\'AT1 APU System 1-1\'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ATA_SYS, lCATASYSMap ) );

      // run validation with c_assmbl_list staging table
      test07_Assembly_IMPORT_ASSMBLIST_APU_VALIDATION();

   }


   /**
    * This test is to verify ASSMEMBLY_IMPORT import functionality of staging table c_ata_sys on APU
    *
    */
   @Test
   public void test10_Assembly_IMPORT_ATA_SYS_APU_IMPORT() {

      test09_Assembly_IMPORT_ATA_SYS_APU_VALIDATION();
      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify EQP_ASSMBL_BOM table
      CheckImportValidation( "EQP_ASSMBL_BOM_ATA" );

   }


   /**
    * This test is to verify ASSMEMBLY_IMPORT validation functionality of staging table
    * c_assmbl_list on ENG
    *
    */
   @Test
   public void test11_Assembly_IMPORT_ASSMBLIST_ENG_VALIDATION() {

      // C_Assmbl_List table
      Map<String, String> lCAssmblList = new LinkedHashMap<>();

      //
      lCAssmblList.put( "ASSMBL_CD", "\'ENG_AT1\'" );
      lCAssmblList.put( "ASSMBL_NAME", "\'ENG AT ASSMBL\'" );
      lCAssmblList.put( "ASSMBL_CLASS_CD", "\'ENG\'" );
      lCAssmblList.put( "ETOPS_BOOL", "\'N\'" );
      lCAssmblList.put( "PN1_MANUFACT_CD", "\'ABC11\'" );
      lCAssmblList.put( "PN1_PART_NO_OEM", "\'ENG_AT_PN1\'" );
      lCAssmblList.put( "PN1_PART_NO_SDESC", "\'AUTO ENG 1\'" );
      lCAssmblList.put( "PN2_MANUFACT_CD", "\'11111\'" );
      lCAssmblList.put( "PN2_PART_NO_OEM", "\'ENG_AT_PN2\'" );
      lCAssmblList.put( "PN2_PART_NO_SDESC", "\'AUTO ENG 2\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ASSMBL_LIST, lCAssmblList ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );
   }


   /**
    * This test is to verify ASSMEMBLY_IMPORT import functionality of staging table c_assmbl_list on
    * ENG
    *
    */
   @Test
   public void test12_Assembly_IMPORT_ASSMBLIST_ENG_IMPORT() {

      test11_Assembly_IMPORT_ASSMBLIST_ENG_VALIDATION();

      // run import

      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify EQP_ASSMBL table
      CheckImportValidation( "EQP_ASSMBL" );
      // Verify EQP_ASSMBL_BOM table
      CheckImportValidation( "EQP_ASSMBL_BOM" );
      // Verify EQP_ASSMBL_POS table
      CheckImportValidation( "EQP_ASSMBL_POS" );
      // Verify EQP_BOM_PART table
      CheckImportValidation( "EQP_BOM_PART" );
      // Verify EQP_BOM_PART and EQP_PART_BASE_LINE and EQP_PART_NO
      CheckPartsInformation_ENG();
      // Verify EQP_DATA_SOURCE table
      CheckImportValidation( "EQP_DATA_SOURCE" );

   }


   /**
    * This test is to verify ASSMEMBLY_IMPORT validation functionality of staging table c_ata_sys on
    * ENG
    *
    */
   @Test
   public void test13_Assembly_IMPORT_ATA_SYS_ENG_VALIDATION() {

      // c_ata_sys table
      Map<String, String> lCATASYSMap = new LinkedHashMap<>();
      // #1
      lCATASYSMap.put( "ASSMBL_CD", "\'ENG_AT1\'" );
      lCATASYSMap.put( "ATA_SYS_CD", "\'ENG-SYS-1-1\'" );
      lCATASYSMap.put( "NH_ATA_SYS_CD", "\'ENG-SYS-1\'" );
      lCATASYSMap.put( "ATA_SYS_NAME", "\'AT1 Engine System 1-1\'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ATA_SYS, lCATASYSMap ) );

      // #2
      lCATASYSMap.clear();
      lCATASYSMap.put( "ASSMBL_CD", "\'ENG_AT1\'" );
      lCATASYSMap.put( "ATA_SYS_CD", "\'ENG-SYS-1\'" );
      lCATASYSMap.put( "NH_ATA_SYS_CD", null );
      lCATASYSMap.put( "ATA_SYS_NAME", "\'AT1 Engine System 1\'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ATA_SYS, lCATASYSMap ) );

      // #3
      lCATASYSMap.clear();
      lCATASYSMap.put( "ASSMBL_CD", "\'ENG_AT1\'" );
      lCATASYSMap.put( "ATA_SYS_CD", "\'ENG-SYS-1-2\'" );
      lCATASYSMap.put( "NH_ATA_SYS_CD", "\'ENG-SYS-1\'" );
      lCATASYSMap.put( "ATA_SYS_NAME", "\'AT1 Engine System 1-1\'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ATA_SYS, lCATASYSMap ) );

      // #4
      lCATASYSMap.clear();
      lCATASYSMap.put( "ASSMBL_CD", "\'ENG_AT1\'" );
      lCATASYSMap.put( "ATA_SYS_CD", "\'ENG-SYS-2\'" );
      lCATASYSMap.put( "NH_ATA_SYS_CD", null );
      lCATASYSMap.put( "ATA_SYS_NAME", "\'AT1 Engine System 2\'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ATA_SYS, lCATASYSMap ) );

      // #5
      lCATASYSMap.clear();
      lCATASYSMap.put( "ASSMBL_CD", "\'ENG_AT1\'" );
      lCATASYSMap.put( "ATA_SYS_CD", "\'ENG-SYS-2-1\'" );
      lCATASYSMap.put( "NH_ATA_SYS_CD", "\'ENG-SYS-2\'" );
      lCATASYSMap.put( "ATA_SYS_NAME", "\'AT1 Engine System 2-1\'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ATA_SYS, lCATASYSMap ) );

      // #6
      lCATASYSMap.clear();
      lCATASYSMap.put( "ASSMBL_CD", "\'ENG_AT1\'" );
      lCATASYSMap.put( "ATA_SYS_CD", "\'ENG-SYS-2-2\'" );
      lCATASYSMap.put( "NH_ATA_SYS_CD", "\'ENG-SYS-2\'" );
      lCATASYSMap.put( "ATA_SYS_NAME", "\'AT1 Engine System 2-2\'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ATA_SYS, lCATASYSMap ) );

      // #7
      lCATASYSMap.clear();
      lCATASYSMap.put( "ASSMBL_CD", "\'ENG_AT1\'" );
      lCATASYSMap.put( "ATA_SYS_CD", "\'ENG-SYS-2-1-1\'" );
      lCATASYSMap.put( "NH_ATA_SYS_CD", "\'ENG-SYS-2-1\'" );
      lCATASYSMap.put( "ATA_SYS_NAME", "\'AT1 Engine System 2-1-1\'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ATA_SYS, lCATASYSMap ) );

      // run validation with c_assmbl_list staging table
      test11_Assembly_IMPORT_ASSMBLIST_ENG_VALIDATION();

   }


   /**
    * This test is to verify ASSMEMBLY_IMPORT import functionality of staging table c_ata_sys on ENG
    *
    */
   @Test
   public void test14_Assembly_IMPORT_ATA_SYS_ENG_IMPORT() {

      test13_Assembly_IMPORT_ATA_SYS_ENG_VALIDATION();
      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify EQP_ASSMBL_BOM table
      CheckImportValidation( "EQP_ASSMBL_BOM_ATA" );

   }


   /**
    * This test is to verify ASSMEMBLY_IMPORT validation functionality of staging table
    * c_assmbl_list on ACFT without any parts, with PART_NO_OEM with lower case
    *
    */
   @Test
   public void testOPER_24040_UpperCase_VALIDATION() {

      // C_Assmbl_List table
      Map<String, String> lCAssmblList = new LinkedHashMap<>();

      //
      lCAssmblList.put( "ASSMBL_CD", "\'NOPT_AT1\'" );
      lCAssmblList.put( "ASSMBL_NAME", "\'NO PARTS AT ASSMBL\'" );
      lCAssmblList.put( "ASSMBL_CLASS_CD", "\'ACFT\'" );
      lCAssmblList.put( "ETOPS_BOOL", "\'N\'" );
      lCAssmblList.put( "PN1_MANUFACT_CD", "\'10001\'" );
      lCAssmblList.put( "PN1_PART_NO_OEM", "\'no_parts_at_pn1\'" );
      lCAssmblList.put( "PN1_PART_NO_SDESC", "\'AUTO NO_PART 1\'" );

      lCAssmblList.put( "PN2_MANUFACT_CD", "\'10001\'" );
      lCAssmblList.put( "PN2_PART_NO_OEM", "\'no_parts_at_pn2\'" );
      lCAssmblList.put( "PN2_PART_NO_SDESC", "\'AUTO NO_PART 2\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ASSMBL_LIST, lCAssmblList ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );
   }


   /**
    * This test is to verify ASSMEMBLY_IMPORT import functionality of staging table c_assmbl_list on
    * ACFT without any parts
    *
    */
   @Test
   public void testOPER_24040_UpperCase__IMPORT() {
      testOPER_24040_UpperCase_VALIDATION();

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // verify eqp_assmbl table
      verifyEQPASSMBL( "NOPT_AT1", "ACFT", "NO PARTS AT ASSMBL" );

      // verify eqp_assmbl_bom
      verifyEQPASSMBLBOM( "NOPT_AT1", "ROOT", "NOPT_AT1", "NO PARTS AT ASSMBL" );

      // verify eqp_assmbl_pos
      verifyEQPASSMBLPOS( "NOPT_AT1", "0", "1" );

      // verify eqp_bom_part
      iBOM_PART_IDs = verifyEQPBOMPART( "NOPT_AT1", "ACFT", "NOPT_AT1", "NO PARTS AT ASSMBL" );

      // verify eqp_part_no
      iPART_NO_IDs_1 = verifyEQPPARTNO( "NO_PARTS_AT_PN1", "AUTO NO_PART 1", "ACFT" );
      iPART_NO_IDs_2 = verifyEQPPARTNO( "NO_PARTS_AT_PN2", "AUTO NO_PART 2", "ACFT" );

      // verify eqp_part_baseline
      String lQuery = "select 1 from " + TableUtil.EQP_PART_BASELINE + " where BOM_PART_DB_ID="
            + iBOM_PART_IDs.getNO_DB_ID() + " and BOM_PART_ID=" + iBOM_PART_IDs.getNO_ID()
            + " and PART_NO_DB_ID=" + iPART_NO_IDs_1.getNO_DB_ID() + " and PART_NO_ID="
            + iPART_NO_IDs_1.getNO_ID();
      Assert.assertTrue( "Check eqp_part_baseline table to verify the record exists: 1",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.EQP_PART_BASELINE + " where BOM_PART_DB_ID="
            + iBOM_PART_IDs.getNO_DB_ID() + " and BOM_PART_ID=" + iBOM_PART_IDs.getNO_ID()
            + " and PART_NO_DB_ID=" + iPART_NO_IDs_2.getNO_DB_ID() + " and PART_NO_ID="
            + iPART_NO_IDs_2.getNO_ID();
      Assert.assertTrue( "Check eqp_part_baseline table to verify the record exists: 1",
            RecordsExist( lQuery ) );
      // verify eqp_data_source
      CheckImportValidation( "EQP_DATA_SOURCE" );

   }


   // ////////////////////////////////////////////////////////////////////////////////////

   /**
    * This function is to verify eqp_part_no table.
    *
    *
    */

   public simpleIDs verifyEQPPARTNO( String aPART_NO_OEM, String aPART_NO_SDESC,
         String aINV_CLASS_CD ) {
      String[] iIds = { "PART_NO_DB_ID", "PART_NO_ID", "PART_NO_SDESC", "INV_CLASS_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "PART_NO_OEM", aPART_NO_OEM );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EQP_PART_NO, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      Assert.assertTrue( "PART_NO_SDESC",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aPART_NO_SDESC ) );
      Assert.assertTrue( "INV_CLASS_CD",
            llists.get( 0 ).get( 3 ).equalsIgnoreCase( aINV_CLASS_CD ) );

      return lIds;

   }


   /**
    * This function is to verify eqp_bom_part table.
    *
    *
    */

   public simpleIDs verifyEQPBOMPART( String aAssmbl_CD, String aINV_CLASS_CD, String aBOM_PART_CD,
         String aBOM_PART_NAME ) {
      String[] iIds =
            { "BOM_PART_DB_ID", "BOM_PART_ID", "INV_CLASS_CD", "BOM_PART_CD", "BOM_PART_NAME" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "Assmbl_CD", aAssmbl_CD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EQP_BOM_PART, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      Assert.assertTrue( "INV_CLASS_CD",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aINV_CLASS_CD ) );
      Assert.assertTrue( "BOM_PART_CD", llists.get( 0 ).get( 3 ).equalsIgnoreCase( aBOM_PART_CD ) );
      Assert.assertTrue( "BOM_PART_NAME",
            llists.get( 0 ).get( 4 ).equalsIgnoreCase( aBOM_PART_NAME ) );

      return lIds;

   }


   /**
    * This function is to verify eqp_assmbl_pos table.
    *
    *
    */

   public void verifyEQPASSMBLPOS( String aAssmbl_CD, String aAssmbl_BOM_ID,
         String aASSMBL_POS_ID ) {
      String[] iIds = { "Assmbl_BOM_ID", "ASSMBL_POS_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "Assmbl_CD", aAssmbl_CD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EQP_ASSMBL_POS, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "Assmbl_BOM_ID",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aAssmbl_BOM_ID ) );
      Assert.assertTrue( "ASSMBL_POS_ID",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( aASSMBL_POS_ID ) );

   }


   /**
    * This function is to verify eqp_assmbl table.
    *
    *
    */

   public void verifyEQPASSMBLBOM( String aAssmbl_CD, String aBOM_CLASS_CD, String aASSMBL_BOM_CD,
         String aASSMBL_BOM_NAME ) {
      String[] iIds = { "BOM_CLASS_CD", "ASSMBL_BOM_CD", "ASSMBL_BOM_NAME" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "Assmbl_CD", aAssmbl_CD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EQP_ASSMBL_BOM, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "BOM_CLASS_CD",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aBOM_CLASS_CD ) );
      Assert.assertTrue( "ASSMBL_BOM_CD",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( aASSMBL_BOM_CD ) );
      Assert.assertTrue( "ASSMBL_BOM_NAME",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aASSMBL_BOM_NAME ) );

   }


   /**
    * This function is to verify eqp_assmbl table.
    *
    *
    */

   public void verifyEQPASSMBL( String aAssmbl_CD, String aASSMBL_CLASS_CD, String aASSMBL_NAME ) {
      String[] iIds = { "ASSMBL_CLASS_CD", "ASSMBL_NAME" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "Assmbl_CD", aAssmbl_CD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EQP_ASSMBL, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "ASSMBL_CLASS_CD",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aASSMBL_CLASS_CD ) );
      Assert.assertTrue( "ASSMBL_CLASS_CD",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( aASSMBL_NAME ) );

   }


   /**
    * This function is to validate MXI tables with staging table c_assmbl_list
    *
    * @Parameter: MXI table
    *
    */

   public void CheckImportValidation( String astrTableName ) {

      try {
         switch ( astrTableName ) {
            case "EQP_ASSMBL":
               compareEQPAssmblAndAssmblList();
               break;
            case "EQP_ASSMBL_BOM":
               compareEQPAssmblBomAndAssmblList();
               break;
            case "EQP_ASSMBL_POS":
               compareEQPAssmblPosAndAssmblList();
               break;
            case "EQP_BOM_PART":
               compareEQPBOMPartAndAssmblList( "ACFT" );
               break;
            case "EQP_DATA_SOURCE":
               compareEQPDataSourceAndAssmblList();
               break;
            case "EQP_ASSMBL_BOM_ATA":
               compareEQPAssmblBomAndATAassmblList();
               break;
            default:
               System.out.println( "No table available to compare" );

         }

      } catch ( Exception e ) {
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

            try {
               CallableStatement lPrepareCallInventory = getConnection().prepareCall(
                     "BEGIN assembly_import.validate_assembly(on_retcode => ?); END;" );

               lPrepareCallInventory.registerOutParameter( 1, Types.INTEGER );
               lPrepareCallInventory.execute();
               commit();
               lReturn = lPrepareCallInventory.getInt( 1 );

            } catch ( SQLException e ) {
               e.printStackTrace();
            }
            return lReturn;

         }


         @Override
         public int runImport( boolean allornone ) {
            int lReturn = 0;

            try {
               CallableStatement lPrepareCallInventory = getConnection()
                     .prepareCall( "BEGIN assembly_import.import_assembly(on_retcode => ?); END;" );

               lPrepareCallInventory.registerOutParameter( 1, Types.INTEGER );

               lPrepareCallInventory.execute();
               commit();
               lReturn = lPrepareCallInventory.getInt( 1 );

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
    * This function is called by after test method to restore data which has been modified by tests.
    *
    *
    */

   public void RestoreData() {

      String lStrDataSource = "delete from EQP_DATA_SOURCE where ASSMBL_CD like '%_AT1'";
      String lStrPartNo = "delete from EQP_PART_NO where PART_NO_SDESC like 'AUTO%'";
      String lStrBomPart = "delete from EQP_BOM_PART where ASSMBL_CD like '%_AT1'";
      String lStrAssmblPos = "delete from EQP_ASSMBL_POS where ASSMBL_CD like '%_AT1'";
      String lstrAssmblBom = "delete from EQP_ASSMBL_BOM where ASSMBL_CD like '%_AT1'";
      String lstrAssmbl = "delete from EQP_ASSMBL where ASSMBL_CD like '%_AT1'";

      PreparedStatement lStatement;
      try {
         // Get BOM_PART_DB_ID & BOM_PART_ID of records to be deleted.
         List<String[]> list = new ArrayList<String[]>();
         StringBuilder lstrquery = new StringBuilder();
         ResultSet lResultSetRecords;

         lstrquery.append(
               "select BOM_PART_DB_ID, BOM_PART_ID from EQP_BOM_PART where ASSMBL_CD like '%_AT1'" );
         lResultSetRecords = runQuery( lstrquery.toString() );

         while ( lResultSetRecords.next() ) {
            String[] str = { lResultSetRecords.getString( "BOM_PART_DB_ID" ),
                  lResultSetRecords.getString( "BOM_PART_ID" ) };
            list.add( str );

         }

         // delete data in EQP_DATA_SOURCE table
         lStatement = getConnection().prepareStatement( lStrDataSource );
         lStatement.executeUpdate( lStrDataSource );
         commit();

         // delete data in EQP_PART_NO table
         lStatement = getConnection().prepareStatement( lStrPartNo );
         lStatement.executeUpdate( lStrPartNo );
         commit();

         // Delete data in EQP_PART_BASE_LINE
         for ( String[] strArr : list ) {
            String lStrPartBaseline = "delete from EQP_PART_BASELINE where BOM_PART_DB_ID='"
                  + strArr[0] + "' and BOM_PART_ID=" + strArr[1];
            lStatement = getConnection().prepareStatement( lStrPartBaseline );
            lStatement.executeUpdate( lStrPartBaseline );
            commit();

         }

         // Delete data in EQP_BOM_PART table
         lStatement = getConnection().prepareStatement( lStrBomPart );
         lStatement.executeUpdate( lStrBomPart );
         commit();

         // Delete data in EQP_ASSMBL_POS
         lStatement = getConnection().prepareStatement( lStrAssmblPos );
         lStatement.executeUpdate( lStrAssmblPos );
         commit();

         // Delete data in EQP_ASSMBL_BOM table
         lStatement = getConnection().prepareStatement( lstrAssmblBom );
         lStatement.executeUpdate( lstrAssmblBom );
         commit();

         // Delete data in EQP_ASSMBL
         lStatement = getConnection().prepareStatement( lstrAssmbl );
         lStatement.executeUpdate( lstrAssmbl );
         commit();

      } catch ( SQLException e ) {

         e.printStackTrace();
      }

   }


   /**
    * This function is to verify ACFT parts are imported correctly on EQP_BOM_PART,
    * EQP_PART_BASELINE and EQP_PART_NO tables.
    *
    *
    */

   public void CheckPartsInformation_ACFT() {

      // partNo assmblepartNo;
      StringBuilder bompartquery = new StringBuilder();
      bompartquery.append(
            "select BOM_PART_DB_ID, BOM_PART_ID, INV_CLASS_DB_ID, INV_CLASS_CD from EQP_BOM_PART " );
      bompartquery.append( "where ASSMBL_CD like '%_AT1'" );

      ArrayList<partInfo> assmblePartlist_original = new ArrayList<partInfo>();
      partInfo partinfor1 = new partInfo( "10001", "ACFT_AT_PN1", "AUTO ACFT 1", "ACFT" );
      partInfo partinfor2 = new partInfo( "10001", "ACFT_AT_PN2", "AUTO ACFT 2", "ACFT" );
      partInfo partinfor3 = new partInfo( "11111", "ACFT_AT_PN3", "AUTO ACFT 3", "ACFT" );
      assmblePartlist_original.add( partinfor1 );
      assmblePartlist_original.add( partinfor2 );
      assmblePartlist_original.add( partinfor3 );

      ArrayList<partNo> PartNolist = new ArrayList<partNo>();
      ArrayList<partInfo> assmblePartlist = new ArrayList<partInfo>();

      ResultSet assPartResultSetRecords;
      // ResultSet c_assPartResultSetRecords;
      try {
         // Get BOM_PART information
         assPartResultSetRecords = runQuery( bompartquery.toString() );
         assPartResultSetRecords.next();
         String bompartDbId = assPartResultSetRecords.getString( "BOM_PART_DB_ID" );
         String bompartId = assPartResultSetRecords.getString( "BOM_PART_ID" );

         StringBuilder partNoquery = new StringBuilder();
         partNoquery.append( "select PART_NO_DB_ID, PART_NO_ID from EQP_PART_BASELINE " );
         partNoquery.append( "where BOM_PART_DB_ID =" ).append( bompartDbId );
         partNoquery.append( " and BOM_PART_ID=" ).append( bompartId );
         assPartResultSetRecords.close();

         // Get PART_NO information
         assPartResultSetRecords = runQuery( partNoquery.toString() );
         while ( assPartResultSetRecords.next() ) {
            partNo partno = new partNo( assPartResultSetRecords.getString( "PART_NO_DB_ID" ),
                  assPartResultSetRecords.getString( "PART_NO_ID" ) );
            PartNolist.add( partno );
         }
         assPartResultSetRecords.close();
         int intsize = PartNolist.size();

         // get partno information
         for ( int i = 0; i < intsize; i++ ) {
            partNoquery.delete( 0, partNoquery.length() );
            partNoquery.append(
                  "select MANUFACT_CD, PART_NO_OEM,PART_NO_SDESC, INV_CLASS_CD from EQP_PART_NO where " );
            partNoquery.append( "PART_NO_DB_ID=" ).append( PartNolist.get( i ).getPART_NO_DB_ID() );
            partNoquery.append( " and PART_NO_ID=" ).append( PartNolist.get( i ).getPART_NO_ID() );
            assPartResultSetRecords = runQuery( partNoquery.toString() );
            assPartResultSetRecords.next();
            partInfo partinfor = new partInfo( assPartResultSetRecords.getString( "MANUFACT_CD" ),
                  assPartResultSetRecords.getString( "PART_NO_OEM" ),
                  assPartResultSetRecords.getString( "PART_NO_SDESC" ),
                  assPartResultSetRecords.getString( "INV_CLASS_CD" ) );
            assmblePartlist.add( partinfor );

         }

      } catch ( SQLException e ) {
         e.printStackTrace();
      } finally {
         checkArraysEqual( assmblePartlist_original, assmblePartlist );

      }

   }


   /**
    * This function is to verify ACFT without parts are imported correctly on EQP_BOM_PART,
    * EQP_PART_BASELINE and EQP_PART_NO tables.
    *
    *
    */

   public void CheckPartsInformation_NOPARTS() {

      // partNo assmblepartNo;
      StringBuilder bompartquery = new StringBuilder();
      bompartquery.append(
            "select BOM_PART_DB_ID, BOM_PART_ID, INV_CLASS_DB_ID, INV_CLASS_CD from EQP_BOM_PART " );
      bompartquery.append( "where ASSMBL_CD like '%_AT1'" );

      ArrayList<partInfo> assmblePartlist_original = new ArrayList<partInfo>();
      partInfo partinfor1 = new partInfo( "10001", "NO_PARTS_AT_PN1", "AUTO NO_PART 1", "ACFT" );
      assmblePartlist_original.add( partinfor1 );

      ArrayList<partNo> PartNolist = new ArrayList<partNo>();
      ArrayList<partInfo> assmblePartlist = new ArrayList<partInfo>();

      ResultSet assPartResultSetRecords;
      // ResultSet c_assPartResultSetRecords;
      try {
         // Get BOM_PART information
         assPartResultSetRecords = runQuery( bompartquery.toString() );
         assPartResultSetRecords.next();
         String bompartDbId = assPartResultSetRecords.getString( "BOM_PART_DB_ID" );
         String bompartId = assPartResultSetRecords.getString( "BOM_PART_ID" );

         StringBuilder partNoquery = new StringBuilder();
         partNoquery.append( "select PART_NO_DB_ID, PART_NO_ID from EQP_PART_BASELINE " );
         partNoquery.append( "where BOM_PART_DB_ID =" ).append( bompartDbId );
         partNoquery.append( " and BOM_PART_ID=" ).append( bompartId );
         assPartResultSetRecords.close();

         // Get PART_NO information
         assPartResultSetRecords = runQuery( partNoquery.toString() );
         while ( assPartResultSetRecords.next() ) {
            partNo partno = new partNo( assPartResultSetRecords.getString( "PART_NO_DB_ID" ),
                  assPartResultSetRecords.getString( "PART_NO_ID" ) );
            PartNolist.add( partno );
         }
         assPartResultSetRecords.close();
         int intsize = PartNolist.size();

         // get partno information
         for ( int i = 0; i < intsize; i++ ) {
            partNoquery.delete( 0, partNoquery.length() );
            partNoquery.append(
                  "select MANUFACT_CD, PART_NO_OEM,PART_NO_SDESC, INV_CLASS_CD from EQP_PART_NO where " );
            partNoquery.append( "PART_NO_DB_ID=" ).append( PartNolist.get( i ).getPART_NO_DB_ID() );
            partNoquery.append( " and PART_NO_ID=" ).append( PartNolist.get( i ).getPART_NO_ID() );
            assPartResultSetRecords = runQuery( partNoquery.toString() );
            assPartResultSetRecords.next();
            partInfo partinfor = new partInfo( assPartResultSetRecords.getString( "MANUFACT_CD" ),
                  assPartResultSetRecords.getString( "PART_NO_OEM" ),
                  assPartResultSetRecords.getString( "PART_NO_SDESC" ),
                  assPartResultSetRecords.getString( "INV_CLASS_CD" ) );
            assmblePartlist.add( partinfor );

         }

      } catch ( SQLException e ) {
         e.printStackTrace();
      } finally {
         checkArraysEqual( assmblePartlist_original, assmblePartlist );

      }

   }


   /**
    * This function is to verify ACFT without parts are imported correctly on EQP_BOM_PART,
    * EQP_PART_BASELINE and EQP_PART_NO tables.
    *
    *
    */

   public void CheckPartsInformation_NOPARTS_2PARTS() {

      // partNo assmblepartNo;
      StringBuilder bompartquery = new StringBuilder();
      bompartquery.append(
            "select BOM_PART_DB_ID, BOM_PART_ID, INV_CLASS_DB_ID, INV_CLASS_CD from EQP_BOM_PART " );
      bompartquery.append( "where ASSMBL_CD like '%_AT1'" );

      ArrayList<partInfo> assmblePartlist_original = new ArrayList<partInfo>();
      partInfo partinfor1 = new partInfo( "10001", "NO_PARTS_AT_PN1", "AUTO NO_PART 1", "ACFT" );
      assmblePartlist_original.add( partinfor1 );

      partInfo partinfor2 = new partInfo( "10001", "NO_PARTS_AT_PN2", "AUTO NO_PART 2", "ACFT" );
      assmblePartlist_original.add( partinfor2 );

      ArrayList<partNo> PartNolist = new ArrayList<partNo>();
      ArrayList<partInfo> assmblePartlist = new ArrayList<partInfo>();

      ResultSet assPartResultSetRecords;
      // ResultSet c_assPartResultSetRecords;
      try {
         // Get BOM_PART information
         assPartResultSetRecords = runQuery( bompartquery.toString() );
         assPartResultSetRecords.next();
         String bompartDbId = assPartResultSetRecords.getString( "BOM_PART_DB_ID" );
         String bompartId = assPartResultSetRecords.getString( "BOM_PART_ID" );

         StringBuilder partNoquery = new StringBuilder();
         partNoquery.append( "select PART_NO_DB_ID, PART_NO_ID from EQP_PART_BASELINE " );
         partNoquery.append( "where BOM_PART_DB_ID =" ).append( bompartDbId );
         partNoquery.append( " and BOM_PART_ID=" ).append( bompartId );
         assPartResultSetRecords.close();

         // Get PART_NO information
         assPartResultSetRecords = runQuery( partNoquery.toString() );
         while ( assPartResultSetRecords.next() ) {
            partNo partno = new partNo( assPartResultSetRecords.getString( "PART_NO_DB_ID" ),
                  assPartResultSetRecords.getString( "PART_NO_ID" ) );
            PartNolist.add( partno );
         }
         assPartResultSetRecords.close();
         int intsize = PartNolist.size();

         // get partno information
         for ( int i = 0; i < intsize; i++ ) {
            partNoquery.delete( 0, partNoquery.length() );
            partNoquery.append(
                  "select MANUFACT_CD, PART_NO_OEM,PART_NO_SDESC, INV_CLASS_CD from EQP_PART_NO where " );
            partNoquery.append( "PART_NO_DB_ID=" ).append( PartNolist.get( i ).getPART_NO_DB_ID() );
            partNoquery.append( " and PART_NO_ID=" ).append( PartNolist.get( i ).getPART_NO_ID() );
            assPartResultSetRecords = runQuery( partNoquery.toString() );
            assPartResultSetRecords.next();
            partInfo partinfor = new partInfo( assPartResultSetRecords.getString( "MANUFACT_CD" ),
                  assPartResultSetRecords.getString( "PART_NO_OEM" ),
                  assPartResultSetRecords.getString( "PART_NO_SDESC" ),
                  assPartResultSetRecords.getString( "INV_CLASS_CD" ) );
            assmblePartlist.add( partinfor );

         }

      } catch ( SQLException e ) {
         e.printStackTrace();
      } finally {
         checkArraysEqual( assmblePartlist_original, assmblePartlist );

      }

   }


   /**
    * This function is to verify APU parts are imported correctly on EQP_BOM_PART, EQP_PART_BASELINE
    * and EQP_PART_NO tables.
    *
    *
    */

   public void CheckPartsInformation_APU() {

      // partNo assmblepartNo;
      StringBuilder bompartquery = new StringBuilder();
      bompartquery.append(
            "select BOM_PART_DB_ID, BOM_PART_ID, INV_CLASS_DB_ID, INV_CLASS_CD from EQP_BOM_PART " );
      bompartquery.append( "where ASSMBL_CD like '%_AT1'" );

      ArrayList<partInfo> assmblePartlist_original = new ArrayList<partInfo>();
      partInfo partinfor1 = new partInfo( "1234567890", "APU_AT_PN1", "AUTO APU 1", "ASSY" );
      partInfo partinfor2 = new partInfo( "ABC11", "APU_AT_PN2", "AUTO APU 2", "ASSY" );
      assmblePartlist_original.add( partinfor1 );
      assmblePartlist_original.add( partinfor2 );

      ArrayList<partNo> PartNolist = new ArrayList<partNo>();
      ArrayList<partInfo> assmblePartlist = new ArrayList<partInfo>();

      ResultSet assPartResultSetRecords;
      // ResultSet c_assPartResultSetRecords;
      try {
         // Get BOM_PART information
         assPartResultSetRecords = runQuery( bompartquery.toString() );
         assPartResultSetRecords.next();
         String bompartDbId = assPartResultSetRecords.getString( "BOM_PART_DB_ID" );
         String bompartId = assPartResultSetRecords.getString( "BOM_PART_ID" );

         StringBuilder partNoquery = new StringBuilder();
         partNoquery.append( "select PART_NO_DB_ID, PART_NO_ID from EQP_PART_BASELINE " );
         partNoquery.append( "where BOM_PART_DB_ID =" ).append( bompartDbId );
         partNoquery.append( " and BOM_PART_ID=" ).append( bompartId );
         assPartResultSetRecords.close();

         // Get PART_NO information
         assPartResultSetRecords = runQuery( partNoquery.toString() );
         while ( assPartResultSetRecords.next() ) {
            partNo partno = new partNo( assPartResultSetRecords.getString( "PART_NO_DB_ID" ),
                  assPartResultSetRecords.getString( "PART_NO_ID" ) );
            PartNolist.add( partno );
         }
         assPartResultSetRecords.close();
         int intsize = PartNolist.size();

         // get partno information
         for ( int i = 0; i < intsize; i++ ) {
            partNoquery.delete( 0, partNoquery.length() );
            partNoquery.append(
                  "select MANUFACT_CD, PART_NO_OEM,PART_NO_SDESC, INV_CLASS_CD from EQP_PART_NO where " );
            partNoquery.append( "PART_NO_DB_ID=" ).append( PartNolist.get( i ).getPART_NO_DB_ID() );
            partNoquery.append( " and PART_NO_ID=" ).append( PartNolist.get( i ).getPART_NO_ID() );
            assPartResultSetRecords = runQuery( partNoquery.toString() );
            assPartResultSetRecords.next();
            partInfo partinfor = new partInfo( assPartResultSetRecords.getString( "MANUFACT_CD" ),
                  assPartResultSetRecords.getString( "PART_NO_OEM" ),
                  assPartResultSetRecords.getString( "PART_NO_SDESC" ),
                  assPartResultSetRecords.getString( "INV_CLASS_CD" ) );
            assmblePartlist.add( partinfor );

         }

      } catch ( SQLException e ) {
         e.printStackTrace();
      } finally {
         checkArraysEqual( assmblePartlist_original, assmblePartlist );

      }
   }


   /**
    * This function is to verify ENG without parts are imported correctly on EQP_BOM_PART,
    * EQP_PART_BASELINE and EQP_PART_NO tables.
    *
    *
    */
   public void CheckPartsInformation_ENG() {

      // partNo assmblepartNo;
      StringBuilder bompartquery = new StringBuilder();
      bompartquery.append(
            "select BOM_PART_DB_ID, BOM_PART_ID, INV_CLASS_DB_ID, INV_CLASS_CD from EQP_BOM_PART " );
      bompartquery.append( "where ASSMBL_CD like '%_AT1'" );

      ArrayList<partInfo> assmblePartlist_original = new ArrayList<partInfo>();
      partInfo partinfor1 = new partInfo( "ABC11", "ENG_AT_PN1", "AUTO ENG 1", "ASSY" );
      partInfo partinfor2 = new partInfo( "11111", "ENG_AT_PN2", "AUTO ENG 2", "ASSY" );
      assmblePartlist_original.add( partinfor1 );
      assmblePartlist_original.add( partinfor2 );

      ArrayList<partNo> PartNolist = new ArrayList<partNo>();
      ArrayList<partInfo> assmblePartlist = new ArrayList<partInfo>();

      ResultSet assPartResultSetRecords;
      // ResultSet c_assPartResultSetRecords;
      try {
         // Get BOM_PART information
         assPartResultSetRecords = runQuery( bompartquery.toString() );
         assPartResultSetRecords.next();
         String bompartDbId = assPartResultSetRecords.getString( "BOM_PART_DB_ID" );
         String bompartId = assPartResultSetRecords.getString( "BOM_PART_ID" );

         StringBuilder partNoquery = new StringBuilder();
         partNoquery.append( "select PART_NO_DB_ID, PART_NO_ID from EQP_PART_BASELINE " );
         partNoquery.append( "where BOM_PART_DB_ID =" ).append( bompartDbId );
         partNoquery.append( " and BOM_PART_ID=" ).append( bompartId );
         assPartResultSetRecords.close();

         // Get PART_NO information
         assPartResultSetRecords = runQuery( partNoquery.toString() );
         while ( assPartResultSetRecords.next() ) {
            partNo partno = new partNo( assPartResultSetRecords.getString( "PART_NO_DB_ID" ),
                  assPartResultSetRecords.getString( "PART_NO_ID" ) );
            PartNolist.add( partno );
         }
         assPartResultSetRecords.close();
         int intsize = PartNolist.size();

         // get part no information
         for ( int i = 0; i < intsize; i++ ) {
            partNoquery.delete( 0, partNoquery.length() );
            partNoquery.append(
                  "select MANUFACT_CD, PART_NO_OEM,PART_NO_SDESC, INV_CLASS_CD from EQP_PART_NO where " );
            partNoquery.append( "PART_NO_DB_ID=" ).append( PartNolist.get( i ).getPART_NO_DB_ID() );
            partNoquery.append( " and PART_NO_ID=" ).append( PartNolist.get( i ).getPART_NO_ID() );
            assPartResultSetRecords = runQuery( partNoquery.toString() );
            assPartResultSetRecords.next();
            partInfo partinfor = new partInfo( assPartResultSetRecords.getString( "MANUFACT_CD" ),
                  assPartResultSetRecords.getString( "PART_NO_OEM" ),
                  assPartResultSetRecords.getString( "PART_NO_SDESC" ),
                  assPartResultSetRecords.getString( "INV_CLASS_CD" ) );
            assmblePartlist.add( partinfor );

         }

      } catch ( SQLException e ) {
         e.printStackTrace();
      } finally {
         checkArraysEqual( assmblePartlist_original, assmblePartlist );

      }

   }

}
