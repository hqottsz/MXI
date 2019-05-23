package com.mxi.mx.core.maint.plan.actualsloader;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.mxi.mx.util.ActualsLoaderTest;
import com.mxi.mx.util.TableUtil;


/**
 * This test suite contains test cases on validation and import functionality of AL_IOE_ASSMBL
 * package.
 *
 * @author ALICIA QIAN
 */
public class IOE_ASSMBL extends ActualsLoaderTest {

   public String strINVDBID = null;
   public String strINVID = null;

   private ArrayList<String> updateTables = new ArrayList<String>();
   {
      updateTables.add(
            "UPDATE inv_inv SET INV_COND_CD = 'INSRV' WHERE ASSMBL_DB_ID='4650' and INV_CLASS_CD='ACFT' and SERIAL_NO_OEM='SN000013'" );

      // Required for test_AL_IOE_ASSMBL_IMPORT_TRK_ASSY_VALIDATION_02 and
      // test_AL_IOE_ASSMBL_IMPORT_TRK_ASSY_VALIDATION_03
      updateTables.add(
            "UPDATE inv_inv SET SERIAL_NO_OEM = 'SN0000P7' WHERE INV_NO_SDESC = 'SER-on-TRK Parent (PN: E0000013, SN: XXX)' AND CONFIG_POS_SDESC = 'ENG-ASSY (2) ->ENG-SYS-1-1-TRK-SER-PARENT'" );
   };

   private ArrayList<String> restoreTables = new ArrayList<String>();
   {
      restoreTables.add(
            "UPDATE inv_inv SET INV_COND_CD = 'ARCHIVE' WHERE ASSMBL_DB_ID='4650' and INV_CLASS_CD='ACFT' and SERIAL_NO_OEM='SN000013'" );

      restoreTables.add(
            "UPDATE inv_inv SET SERIAL_NO_OEM = 'XXX' WHERE INV_NO_SDESC = 'SER-on-TRK Parent (PN: E0000013, SN: XXX)' AND CONFIG_POS_SDESC = 'ENG-ASSY (2) ->ENG-SYS-1-1-TRK-SER-PARENT'" );
   };


   /**
    * Setup before executing each individual test
    *
    * @throws Exception
    */
   @Before
   @Override
   public void before() throws Exception {
      super.before();
      classDataSetup( updateTables );
      convertTRKSerialNUM();
      clearActualsLoaderTables();
   }


   /**
    * Clean up after each individual test
    *
    * @throws Exception
    */
   @After
   @Override
   public void after() throws Exception {
      classDataSetup( restoreTables );
      resumeTRKSerialNUM();
      super.after();
   }


   /**
    * Document me
    *
    */
   // @Test
   public void test_AL_IOE_ASSMBL_IMPORT_ASSY_ACFT_VALIDATION_00() {
      // AL_IOE_ASSMBL table
      Map<String, String> lAlIoeASSMBL = new LinkedHashMap<>();

      lAlIoeASSMBL.put( "SERIAL_NO_OEM", "\'SN000006\'" );
      lAlIoeASSMBL.put( "PART_NO_OEM", "\'APU_ASSY_PN1\'" );
      lAlIoeASSMBL.put( "MANUFACT_CD", "\'1234567890\'" );
      lAlIoeASSMBL.put( "ASSMBL_BOM_CD", "\'APU-ASSY\'" );
      lAlIoeASSMBL.put( "EQP_POS_CD", "\'1\'" );
      lAlIoeASSMBL.put( "ASSMBL_SERIAL_NO_OEM", "\'SN000001\'" );
      lAlIoeASSMBL.put( "ASSMBL_PART_NO_OEM", "\'ACFT_ASSY_PN1\'" );
      lAlIoeASSMBL.put( "ASSMBL_MANUFACT_CD", "\'10001\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_IOE_ASSMBL, lAlIoeASSMBL ) );
      Assert.assertTrue( runValidateIOEASSMBL( true ) == 1 );

   }


   /**
    * Document me
    *
    */
   @Test
   public void test_AL_IOE_ASSMBL_IMPORT_ASSY_ACFT_IMPORT_00() {

      test_AL_IOE_ASSMBL_IMPORT_ASSY_ACFT_VALIDATION_00();

      // run validations
      Assert.assertTrue( runImportIOEASSMBL( true ) == 1 );

      // check maintenix table with staging table
      runCheckImportValidation( "SN000006" );

   }


   /**
    * Document me
    *
    */
   // @Test
   public void test_AL_IOE_ASSMBL_IMPORT_ASSY_ACFT_VALIDATION_01() {
      // AL_IOE_ASSMBL table
      Map<String, String> lAlIoeASSMBL = new LinkedHashMap<>();

      lAlIoeASSMBL.put( "SERIAL_NO_OEM", "\'SN000006\'" );
      lAlIoeASSMBL.put( "PART_NO_OEM", "\'APU_ASSY_PN1\'" );
      lAlIoeASSMBL.put( "MANUFACT_CD", "\'1234567890\'" );
      lAlIoeASSMBL.put( "ASSMBL_BOM_CD", null );
      lAlIoeASSMBL.put( "EQP_POS_CD", null );
      lAlIoeASSMBL.put( "ASSMBL_SERIAL_NO_OEM", "\'SN000001\'" );
      lAlIoeASSMBL.put( "ASSMBL_PART_NO_OEM", "\'ACFT_ASSY_PN1\'" );
      lAlIoeASSMBL.put( "ASSMBL_MANUFACT_CD", "\'10001\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_IOE_ASSMBL, lAlIoeASSMBL ) );
      Assert.assertTrue( runValidateIOEASSMBL( false ) == 1 );

   }


   /**
    * Document me
    *
    */
   @Test
   public void test_AL_IOE_ASSMBL_IMPORT_ASSY_ACFT_IMPORT_01() {

      test_AL_IOE_ASSMBL_IMPORT_ASSY_ACFT_VALIDATION_01();

      // run validation
      Assert.assertTrue( runImportIOEASSMBL( false ) == 1 );

      // check maintenix table with staging table
      runCheckImportValidation( "SN000006" );
   }


   /**
    * Document me
    *
    */
   // @Test
   public void test_AL_IOE_ASSMBL_IMPORT_ASSY_ACFT_VALIDATION_02() {
      // AL_IOE_ASSMBL table
      Map<String, String> lAlIoeASSMBL = new LinkedHashMap<>();

      lAlIoeASSMBL.put( "SERIAL_NO_OEM", null );
      lAlIoeASSMBL.put( "PART_NO_OEM", null );
      lAlIoeASSMBL.put( "MANUFACT_CD", null );
      lAlIoeASSMBL.put( "ASSMBL_BOM_CD", "\'APU-ASSY\'" );
      lAlIoeASSMBL.put( "EQP_POS_CD", "\'1\'" );
      lAlIoeASSMBL.put( "ASSMBL_SERIAL_NO_OEM", "\'SN000001\'" );
      lAlIoeASSMBL.put( "ASSMBL_PART_NO_OEM", "\'ACFT_ASSY_PN1\'" );
      lAlIoeASSMBL.put( "ASSMBL_MANUFACT_CD", "\'10001\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_IOE_ASSMBL, lAlIoeASSMBL ) );
      Assert.assertTrue( runValidateIOEASSMBL( true ) == 1 );

   }


   /**
    * Document me
    *
    */
   @Test
   public void test_AL_IOE_ASSMBL_IMPORT_ASSY_ACFT_IMPORT_02() {

      test_AL_IOE_ASSMBL_IMPORT_ASSY_ACFT_VALIDATION_02();

      // run validation
      Assert.assertTrue( runImportIOEASSMBL( false ) == 1 );

      // check maintenix table with staging table
      runCheckImportValidation( "SN000006" );

   }


   /**
    * Document me
    *
    */
   // @Test
   public void test_AL_IOE_ASSMBL_IMPORT_ASSY_ACFT_VALIDATION_03() {
      // AL_IOE_ASSMBL table
      Map<String, String> lAlIoeASSMBL = new LinkedHashMap<>();

      lAlIoeASSMBL.put( "SERIAL_NO_OEM", null );
      lAlIoeASSMBL.put( "PART_NO_OEM", "\'APU_ASSY_PN1\'" );
      lAlIoeASSMBL.put( "MANUFACT_CD", "\'1234567890\'" );
      lAlIoeASSMBL.put( "ASSMBL_BOM_CD", "\'APU-ASSY\'" );
      lAlIoeASSMBL.put( "EQP_POS_CD", "\'1\'" );
      lAlIoeASSMBL.put( "ASSMBL_SERIAL_NO_OEM", "\'SN000001\'" );
      lAlIoeASSMBL.put( "ASSMBL_PART_NO_OEM", "\'ACFT_ASSY_PN1\'" );
      lAlIoeASSMBL.put( "ASSMBL_MANUFACT_CD", "\'10001\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_IOE_ASSMBL, lAlIoeASSMBL ) );
      Assert.assertTrue( runValidateIOEASSMBL( true ) == 1 );

   }


   /**
    * Document me
    *
    */
   @Test
   public void test_AL_IOE_ASSMBL_IMPORT_ASSY_ACFT_IMPORT_03() {

      test_AL_IOE_ASSMBL_IMPORT_ASSY_ACFT_VALIDATION_03();

      // run validation
      Assert.assertTrue( runImportIOEASSMBL( false ) == 1 );

      // check maintenix table with staging table
      runCheckImportValidation( "SN000006" );

   }


   /**
    * Document me
    *
    */
   // @Test
   public void test_AL_IOE_ASSMBL_IMPORT_ASSY_ACFT_VALIDATION_04() {
      // AL_IOE_ASSMBL table
      Map<String, String> lAlIoeASSMBL = new LinkedHashMap<>();

      lAlIoeASSMBL.put( "SERIAL_NO_OEM", "\'SN000006\'" );
      lAlIoeASSMBL.put( "PART_NO_OEM", "\'APU_ASSY_PN1\'" );
      lAlIoeASSMBL.put( "MANUFACT_CD", null );
      lAlIoeASSMBL.put( "ASSMBL_BOM_CD", "\'APU-ASSY\'" );
      lAlIoeASSMBL.put( "EQP_POS_CD", "\'1\'" );
      lAlIoeASSMBL.put( "ASSMBL_SERIAL_NO_OEM", "\'SN000001\'" );
      lAlIoeASSMBL.put( "ASSMBL_PART_NO_OEM", "\'ACFT_ASSY_PN1\'" );
      lAlIoeASSMBL.put( "ASSMBL_MANUFACT_CD", "\'10001\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_IOE_ASSMBL, lAlIoeASSMBL ) );
      Assert.assertTrue( runValidateIOEASSMBL( false ) == 1 );

   }


   /**
    * Document me
    *
    */
   @Test
   public void test_AL_IOE_ASSMBL_IMPORT_ASSY_ACFT_IMPORT_04() {

      test_AL_IOE_ASSMBL_IMPORT_ASSY_ACFT_VALIDATION_04();

      // run validation
      Assert.assertTrue( runImportIOEASSMBL( false ) == 1 );

      // check maintenix table with staging table
      runCheckImportValidation( "SN000006" );

   }


   /**
    * Document me
    *
    */
   // @Test
   public void test_AL_IOE_ASSMBL_IMPORT_ASSY_ACFT_VALIDATION_05() {
      // AL_IOE_ASSMBL table
      Map<String, String> lAlIoeASSMBL = new LinkedHashMap<>();

      lAlIoeASSMBL.put( "SERIAL_NO_OEM", "\'SN000006\'" );
      lAlIoeASSMBL.put( "PART_NO_OEM", "\'APU_ASSY_PN1\'" );
      lAlIoeASSMBL.put( "MANUFACT_CD", "\'1234567890\'" );
      lAlIoeASSMBL.put( "ASSMBL_BOM_CD", "\'APU-ASSY\'" );
      lAlIoeASSMBL.put( "EQP_POS_CD", "\'1\'" );
      lAlIoeASSMBL.put( "ASSMBL_SERIAL_NO_OEM", "\'SN000001\'" );
      lAlIoeASSMBL.put( "ASSMBL_PART_NO_OEM", "\'ACFT_ASSY_PN1\'" );
      lAlIoeASSMBL.put( "ASSMBL_MANUFACT_CD", null );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_IOE_ASSMBL, lAlIoeASSMBL ) );
      Assert.assertTrue( runValidateIOEASSMBL( true ) == 1 );

   }


   /**
    * Document me
    *
    */
   @Test
   public void test_AL_IOE_ASSMBL_IMPORT_ASSY_ACFT_IMPORT_05() {

      test_AL_IOE_ASSMBL_IMPORT_ASSY_ACFT_VALIDATION_05();

      // run validation
      Assert.assertTrue( runImportIOEASSMBL( false ) == 1 );

      // check maintenix table with staging table
      runCheckImportValidation( "SN000006" );

   }


   /**
    * Document me
    *
    */
   // @Test
   public void test_AL_IOE_ASSMBL_IMPORT_SER_ACFT_VALIDATION_00() {
      // AL_IOE_ASSMBL table
      Map<String, String> lAlIoeASSMBL = new LinkedHashMap<>();

      lAlIoeASSMBL.put( "SERIAL_NO_OEM", null );
      lAlIoeASSMBL.put( "PART_NO_OEM", null );
      lAlIoeASSMBL.put( "MANUFACT_CD", null );
      lAlIoeASSMBL.put( "ASSMBL_BOM_CD", "\'ACFT-SYS-1-1-TRK-SER-PARENT\'" );
      lAlIoeASSMBL.put( "EQP_POS_CD", "\'1\'" );
      lAlIoeASSMBL.put( "ASSMBL_SERIAL_NO_OEM", "\'SNDUPLICATE\'" );
      lAlIoeASSMBL.put( "ASSMBL_PART_NO_OEM", "\'ACFT_ASSY_PN1\'" );
      lAlIoeASSMBL.put( "ASSMBL_MANUFACT_CD", "\'10001\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_IOE_ASSMBL, lAlIoeASSMBL ) );
      Assert.assertTrue( runValidateIOEASSMBL( true ) == 1 );

   }


   /**
    * Document me
    *
    */
   @Test
   public void test_AL_IOE_ASSMBL_IMPORT_SER_ACFT_IMPORT_00() {

      test_AL_IOE_ASSMBL_IMPORT_SER_ACFT_VALIDATION_00();

      // run validation
      Assert.assertTrue( runImportIOEASSMBL( false ) == 1 );

      // check maintenix table with staging table
      runCheckImportValidation( "XXX" );

   }


   /**
    * Document me
    *
    */
   // @Test
   public void test_AL_IOE_ASSMBL_IMPORT_SER_ACFT_VALIDATION_01() {
      // AL_IOE_ASSMBL table
      Map<String, String> lAlIoeASSMBL = new LinkedHashMap<>();

      lAlIoeASSMBL.put( "SERIAL_NO_OEM", null );
      lAlIoeASSMBL.put( "PART_NO_OEM", null );
      lAlIoeASSMBL.put( "MANUFACT_CD", null );
      lAlIoeASSMBL.put( "ASSMBL_BOM_CD", "\'ACFT-SYS-1-1-TRK-SER-PARENT\'" );
      lAlIoeASSMBL.put( "EQP_POS_CD", "\'1\'" );
      lAlIoeASSMBL.put( "ASSMBL_SERIAL_NO_OEM", "\'SNDUPLICATE\'" );
      lAlIoeASSMBL.put( "ASSMBL_PART_NO_OEM", "\'ACFT_ASSY_PN1\'" );
      lAlIoeASSMBL.put( "ASSMBL_MANUFACT_CD", "\'10001\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_IOE_ASSMBL, lAlIoeASSMBL ) );
      Assert.assertTrue( runValidateIOEASSMBL( false ) == 1 );

   }


   /**
    * Document me
    *
    */
   @Test
   public void test_AL_IOE_ASSMBL_IMPORT_SER_ACFT_IMPORT_01() {

      test_AL_IOE_ASSMBL_IMPORT_SER_ACFT_VALIDATION_01();

      // run validation
      Assert.assertTrue( runImportIOEASSMBL( false ) == 1 );

      // check maintenix table with staging table
      runCheckImportValidation( "XXX" );

   }


   /**
    * Document me defect?
    *
    */
   // @Test
   public void test_AL_IOE_ASSMBL_IMPORT_SER_ACFT_VALIDATION_02() {
      // AL_IOE_ASSMBL table
      Map<String, String> lAlIoeASSMBL = new LinkedHashMap<>();

      lAlIoeASSMBL.put( "SERIAL_NO_OEM", null );
      lAlIoeASSMBL.put( "PART_NO_OEM", "\'A0000013\'" );
      lAlIoeASSMBL.put( "MANUFACT_CD", "\'10001\'" );
      lAlIoeASSMBL.put( "ASSMBL_BOM_CD", "\'ACFT-SYS-1-1-TRK-SER-PARENT\'" );
      lAlIoeASSMBL.put( "EQP_POS_CD", "\'1\'" );
      lAlIoeASSMBL.put( "ASSMBL_SERIAL_NO_OEM", "\'SNDUPLICATE\'" );
      lAlIoeASSMBL.put( "ASSMBL_PART_NO_OEM", "\'ACFT_ASSY_PN1\'" );
      lAlIoeASSMBL.put( "ASSMBL_MANUFACT_CD", "\'10001\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_IOE_ASSMBL, lAlIoeASSMBL ) );
      Assert.assertTrue( runValidateIOEASSMBL( false ) == 1 );
   }


   /**
    * Document me
    *
    */
   @Test
   public void test_AL_IOE_ASSMBL_IMPORT_SER_ACFT_IMPORT_02() {

      test_AL_IOE_ASSMBL_IMPORT_SER_ACFT_VALIDATION_02();

      // run validation
      Assert.assertTrue( runImportIOEASSMBL( false ) == 1 );

      // check maintenix table with staging table
      runCheckImportValidation( "XXX" );

   }


   /**
    * Document me
    *
    */
   // @Test
   public void test_AL_IOE_ASSMBL_IMPORT_SER_ACFT_VALIDATION_03() {
      // AL_IOE_ASSMBL table
      Map<String, String> lAlIoeASSMBL = new LinkedHashMap<>();

      lAlIoeASSMBL.put( "SERIAL_NO_OEM", null );
      lAlIoeASSMBL.put( "PART_NO_OEM", null );
      lAlIoeASSMBL.put( "MANUFACT_CD", null );
      lAlIoeASSMBL.put( "ASSMBL_BOM_CD", "\'ACFT-SYS-1-1-TRK-SER-PARENT\'" );
      lAlIoeASSMBL.put( "EQP_POS_CD", "\'1\'" );
      lAlIoeASSMBL.put( "ASSMBL_SERIAL_NO_OEM", "\'SNDUPLICATE\'" );
      lAlIoeASSMBL.put( "ASSMBL_PART_NO_OEM", "\'ACFT_ASSY_PN1\'" );
      lAlIoeASSMBL.put( "ASSMBL_MANUFACT_CD", null );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_IOE_ASSMBL, lAlIoeASSMBL ) );
      Assert.assertTrue( runValidateIOEASSMBL( false ) == 1 );

   }


   /**
    * Document me
    *
    */
   @Test
   public void test_AL_IOE_ASSMBL_IMPORT_SER_ACFT_IMPORT_03() {

      test_AL_IOE_ASSMBL_IMPORT_SER_ACFT_VALIDATION_03();

      // run validation
      Assert.assertTrue( runImportIOEASSMBL( false ) == 1 );

      // check maintenix table with staging table
      runCheckImportValidation( "XXX" );

   }


   /**
    * Document me//testing
    *
    */
   // @Test
   public void test_AL_IOE_ASSMBL_IMPORT_TRK_ACFT_VALIDATION_00() {

      // AL_IOE_ASSMBL table
      Map<String, String> lAlIoeASSMBL = new LinkedHashMap<>();

      lAlIoeASSMBL.put( "SERIAL_NO_OEM", "\'SNAUTOTRK001\'" );
      lAlIoeASSMBL.put( "PART_NO_OEM", "\'A0000020\'" );
      lAlIoeASSMBL.put( "MANUFACT_CD", "\'10001\'" );
      lAlIoeASSMBL.put( "ASSMBL_BOM_CD", "\'ACFT-SYS-1-1-TRK-TRK-TRK-PARENT\'" );
      lAlIoeASSMBL.put( "EQP_POS_CD", "\'1.1\'" );
      lAlIoeASSMBL.put( "ASSMBL_SERIAL_NO_OEM", "\'SN000001\'" );
      lAlIoeASSMBL.put( "ASSMBL_PART_NO_OEM", "\'ACFT_ASSY_PN1\'" );
      lAlIoeASSMBL.put( "ASSMBL_MANUFACT_CD", "\'10001\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_IOE_ASSMBL, lAlIoeASSMBL ) );
      Assert.assertTrue( runValidateIOEASSMBL( true ) == 1 );

   }


   /**
    * Document me
    *
    */
   @Test
   public void test_AL_IOE_ASSMBL_IMPORT_TRK_ACFT_IMPORT_00() {

      test_AL_IOE_ASSMBL_IMPORT_TRK_ACFT_VALIDATION_00();

      // run validation
      Assert.assertTrue( runImportIOEASSMBL( false ) == 1 );

      // check maintenix table with staging table
      runCheckImportValidation( "SNAUTOTRK001" );

   }


   /**
    * Document me
    *
    */
   // @Test
   public void test_AL_IOE_ASSMBL_IMPORT_TRK_ACFT_VALIDATION_01() {

      // AL_IOE_ASSMBL table
      Map<String, String> lAlIoeASSMBL = new LinkedHashMap<>();

      lAlIoeASSMBL.put( "SERIAL_NO_OEM", "\'SNAUTOTRK001\'" );
      lAlIoeASSMBL.put( "PART_NO_OEM", "\'A0000020\'" );
      lAlIoeASSMBL.put( "MANUFACT_CD", "\'10001\'" );
      lAlIoeASSMBL.put( "ASSMBL_BOM_CD", null );
      lAlIoeASSMBL.put( "EQP_POS_CD", null );
      lAlIoeASSMBL.put( "ASSMBL_SERIAL_NO_OEM", "\'SN000001\'" );
      lAlIoeASSMBL.put( "ASSMBL_PART_NO_OEM", "\'ACFT_ASSY_PN1\'" );
      lAlIoeASSMBL.put( "ASSMBL_MANUFACT_CD", "\'10001\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_IOE_ASSMBL, lAlIoeASSMBL ) );
      Assert.assertTrue( runValidateIOEASSMBL( false ) == 1 );

   }


   /**
    * Document me
    *
    */
   @Test
   public void test_AL_IOE_ASSMBL_IMPORT_TRK_ACFT_IMPORT_01() {

      test_AL_IOE_ASSMBL_IMPORT_TRK_ACFT_VALIDATION_01();

      // run validation
      Assert.assertTrue( runImportIOEASSMBL( false ) == 1 );

      // check maintenix table with staging table
      runCheckImportValidation( "SNAUTOTRK001" );

   }


   /**
    * Document me
    *
    */
   // @Test
   public void test_AL_IOE_ASSMBL_IMPORT_TRK_ACFT_VALIDATION_02() {

      // AL_IOE_ASSMBL table
      Map<String, String> lAlIoeASSMBL = new LinkedHashMap<>();

      lAlIoeASSMBL.put( "SERIAL_NO_OEM", null );
      lAlIoeASSMBL.put( "PART_NO_OEM", null );
      lAlIoeASSMBL.put( "MANUFACT_CD", null );
      lAlIoeASSMBL.put( "ASSMBL_BOM_CD", "\'ACFT-SYS-1-1-TRK-TRK-TRK-PARENT\'" );
      lAlIoeASSMBL.put( "EQP_POS_CD", "\'1.1\'" );
      lAlIoeASSMBL.put( "ASSMBL_SERIAL_NO_OEM", "\'SN000001\'" );
      lAlIoeASSMBL.put( "ASSMBL_PART_NO_OEM", "\'ACFT_ASSY_PN1\'" );
      lAlIoeASSMBL.put( "ASSMBL_MANUFACT_CD", "\'10001\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_IOE_ASSMBL, lAlIoeASSMBL ) );
      Assert.assertTrue( runValidateIOEASSMBL( true ) == 1 );
   }


   /**
    * Document me
    *
    */
   @Test
   public void test_AL_IOE_ASSMBL_IMPORT_TRK_ACFT_IMPORT_02() {

      test_AL_IOE_ASSMBL_IMPORT_TRK_ACFT_VALIDATION_02();

      // run validation
      Assert.assertTrue( runImportIOEASSMBL( false ) == 1 );

      // check maintenix table with staging table
      runCheckImportValidation( "SNAUTOTRK001" );

   }


   /**
    * Document me
    *
    */
   // @Test
   public void test_AL_IOE_ASSMBL_IMPORT_TRK_ACFT_VALIDATION_03() {

      // AL_IOE_ASSMBL table
      Map<String, String> lAlIoeASSMBL = new LinkedHashMap<>();

      lAlIoeASSMBL.put( "SERIAL_NO_OEM", null );
      lAlIoeASSMBL.put( "PART_NO_OEM", "\'A0000020\'" );
      lAlIoeASSMBL.put( "MANUFACT_CD", "\'10001\'" );
      lAlIoeASSMBL.put( "ASSMBL_BOM_CD", "\'ACFT-SYS-1-1-TRK-TRK-TRK-PARENT\'" );
      lAlIoeASSMBL.put( "EQP_POS_CD", "\'1.1\'" );
      lAlIoeASSMBL.put( "ASSMBL_SERIAL_NO_OEM", "\'SN000001\'" );
      lAlIoeASSMBL.put( "ASSMBL_PART_NO_OEM", "\'ACFT_ASSY_PN1\'" );
      lAlIoeASSMBL.put( "ASSMBL_MANUFACT_CD", "\'10001\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_IOE_ASSMBL, lAlIoeASSMBL ) );
      Assert.assertTrue( runValidateIOEASSMBL( true ) == 1 );
   }


   /**
    * Document me
    *
    */
   @Test
   public void test_AL_IOE_ASSMBL_IMPORT_TRK_ACFT_IMPORT_03() {

      test_AL_IOE_ASSMBL_IMPORT_TRK_ACFT_VALIDATION_03();

      // run validation
      Assert.assertTrue( runImportIOEASSMBL( false ) == 1 );

      // check maintenix table with staging table
      runCheckImportValidation( "SNAUTOTRK001" );

   }


   /**
    * Document me
    *
    */
   // @Test
   public void test_AL_IOE_ASSMBL_IMPORT_TRK_ACFT_VALIDATION_04() {

      // AL_IOE_ASSMBL table
      Map<String, String> lAlIoeASSMBL = new LinkedHashMap<>();

      lAlIoeASSMBL.put( "SERIAL_NO_OEM", "\'SNAUTOTRK001\'" );
      lAlIoeASSMBL.put( "PART_NO_OEM", "\'A0000020\'" );
      lAlIoeASSMBL.put( "MANUFACT_CD", null );
      lAlIoeASSMBL.put( "ASSMBL_BOM_CD", "\'ACFT-SYS-1-1-TRK-TRK-TRK-PARENT\'" );
      lAlIoeASSMBL.put( "EQP_POS_CD", "\'1.1\'" );
      lAlIoeASSMBL.put( "ASSMBL_SERIAL_NO_OEM", "\'SN000001\'" );
      lAlIoeASSMBL.put( "ASSMBL_PART_NO_OEM", "\'ACFT_ASSY_PN1\'" );
      lAlIoeASSMBL.put( "ASSMBL_MANUFACT_CD", "\'10001\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_IOE_ASSMBL, lAlIoeASSMBL ) );
      Assert.assertTrue( runValidateIOEASSMBL( true ) == 1 );
   }


   /**
    * Document me
    *
    */
   @Test
   public void test_AL_IOE_ASSMBL_IMPORT_TRK_ACFT_IMPORT_04() {

      test_AL_IOE_ASSMBL_IMPORT_TRK_ACFT_VALIDATION_04();

      // run validation
      Assert.assertTrue( runImportIOEASSMBL( false ) == 1 );

      // check maintenix table with staging table
      runCheckImportValidation( "SNAUTOTRK001" );

   }


   /**
    * Document me
    *
    */
   // @Test
   public void test_AL_IOE_ASSMBL_IMPORT_TRK_ACFT_VALIDATION_05() {

      // AL_IOE_ASSMBL table
      Map<String, String> lAlIoeASSMBL = new LinkedHashMap<>();

      lAlIoeASSMBL.put( "SERIAL_NO_OEM", "\'SNAUTOTRK001\'" );
      lAlIoeASSMBL.put( "PART_NO_OEM", "\'A0000020\'" );
      lAlIoeASSMBL.put( "MANUFACT_CD", "\'10001\'" );
      lAlIoeASSMBL.put( "ASSMBL_BOM_CD", "\'ACFT-SYS-1-1-TRK-TRK-TRK-PARENT\'" );
      lAlIoeASSMBL.put( "EQP_POS_CD", "\'1.1\'" );
      lAlIoeASSMBL.put( "ASSMBL_SERIAL_NO_OEM", "\'SN000001\'" );
      lAlIoeASSMBL.put( "ASSMBL_PART_NO_OEM", "\'ACFT_ASSY_PN1\'" );
      lAlIoeASSMBL.put( "ASSMBL_MANUFACT_CD", null );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_IOE_ASSMBL, lAlIoeASSMBL ) );
      Assert.assertTrue( runValidateIOEASSMBL( false ) == 1 );

   }


   /**
    * Document me
    *
    */
   @Test
   public void test_AL_IOE_ASSMBL_IMPORT_TRK_ACFT_IMPORT_05() {

      test_AL_IOE_ASSMBL_IMPORT_TRK_ACFT_VALIDATION_05();

      // run validation
      Assert.assertTrue( runImportIOEASSMBL( false ) == 1 );

      // check maintenix table with staging table
      runCheckImportValidation( "SNAUTOTRK001" );

   }


   /**
    * Document me
    *
    */
   // @Test
   public void test_AL_IOE_ASSMBL_IMPORT_SER_ASSY_VALIDATION() {
      // to do

   }


   /**
    * Test validation when a Tracked component is linked to an Assembly with no component info.,
    * just config slot.
    */
   // @Test
   public void test_AL_IOE_ASSMBL_IMPORT_TRK_ASSY_VALIDATION_01() {

      // AL_IOE_ASSMBL table
      Map<String, String> lAlIoeASSMBL = new LinkedHashMap<>();

      lAlIoeASSMBL.put( "SERIAL_NO_OEM", null );
      lAlIoeASSMBL.put( "PART_NO_OEM", null );
      lAlIoeASSMBL.put( "MANUFACT_CD", null );
      lAlIoeASSMBL.put( "ASSMBL_BOM_CD", "\'APU-SYS-1-1-TRK-BATCH-PARENT\'" );
      lAlIoeASSMBL.put( "EQP_POS_CD", "\'1\'" );
      lAlIoeASSMBL.put( "ASSMBL_SERIAL_NO_OEM", "\'SN000006\'" );
      lAlIoeASSMBL.put( "ASSMBL_PART_NO_OEM", "\'APU_ASSY_PN1\'" );
      lAlIoeASSMBL.put( "ASSMBL_MANUFACT_CD", "\'1234567890\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_IOE_ASSMBL, lAlIoeASSMBL ) );
      Assert.assertTrue( runValidateIOEASSMBL( false ) == 1 );
   }


   /**
    * Test import when a Tracked component is linked to an Assembly with no component info., just
    * config slot.
    */
   @Test
   public void test_AL_IOE_ASSMBL_IMPORT_TRK_ASSY_IMPORT_01() {

      test_AL_IOE_ASSMBL_IMPORT_TRK_ASSY_VALIDATION_01();

      // run validation on the import procedure
      Assert.assertTrue( runImportIOEASSMBL( false ) == 1 );

      // check maintenix table with staging table
      runCheckImportValidation( "XXX" );
   }


   /**
    * Test validation when a Tracked component is linked to an Assembly Note: This test case will
    * also verify ASSMBL_MANUFACT_CD has been changed to uppercase and trimmed
    */
   // @Test
   public void test_AL_IOE_ASSMBL_IMPORT_TRK_ASSY_VALIDATION_02() {

      // AL_IOE_ASSMBL table
      Map<String, String> lAlIoeASSMBL = new LinkedHashMap<>();

      lAlIoeASSMBL.put( "SERIAL_NO_OEM", "\'SN0000P7\'" );
      lAlIoeASSMBL.put( "PART_NO_OEM", "\'E0000013\'" );
      lAlIoeASSMBL.put( "MANUFACT_CD", "\'10001\'" );
      lAlIoeASSMBL.put( "ASSMBL_BOM_CD", "\'ENG-SYS-1-1-TRK-SER-PARENT\'" );
      lAlIoeASSMBL.put( "EQP_POS_CD", "\'1\'" );
      lAlIoeASSMBL.put( "ASSMBL_SERIAL_NO_OEM", "\'SN000007\'" );
      lAlIoeASSMBL.put( "ASSMBL_PART_NO_OEM", "\'ENG_ASSY_PN1\'" );
      // This value has been changed to validate the upper case and trim change for OPER=21977
      lAlIoeASSMBL.put( "ASSMBL_MANUFACT_CD", "\'aBc11 \'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_IOE_ASSMBL, lAlIoeASSMBL ) );
      Assert.assertTrue( runValidateIOEASSMBL( false ) == 1 );
   }


   /**
    * Test import when a Tracked component is linked to an Assembly
    *
    */
   @Test
   public void test_AL_IOE_ASSMBL_IMPORT_TRK_ASSY_IMPORT_02() {

      test_AL_IOE_ASSMBL_IMPORT_TRK_ASSY_VALIDATION_02();

      // run validation on the import procedure
      Assert.assertTrue( runImportIOEASSMBL( false ) == 1 );

      // check maintenix table with staging table
      runCheckImportValidation( "SN0000P7", "ABC11" );
   }


   /**
    * Test import when a Tracked component is linked to an Assembly with no config. slot entered.
    */
   // @Test
   public void test_AL_IOE_ASSMBL_IMPORT_TRK_ASSY_VALIDATION_03() {

      // AL_IOE_ASSMBL table
      Map<String, String> lAlIoeASSMBL = new LinkedHashMap<>();

      lAlIoeASSMBL.put( "SERIAL_NO_OEM", "\'SN0000P7\'" );
      lAlIoeASSMBL.put( "PART_NO_OEM", "\'E0000013\'" );
      lAlIoeASSMBL.put( "MANUFACT_CD", "\'10001\'" );
      lAlIoeASSMBL.put( "ASSMBL_BOM_CD", null );
      lAlIoeASSMBL.put( "EQP_POS_CD", null );
      lAlIoeASSMBL.put( "ASSMBL_SERIAL_NO_OEM", "\'SN000007\'" );
      lAlIoeASSMBL.put( "ASSMBL_PART_NO_OEM", "\'ENG_ASSY_PN1\'" );
      lAlIoeASSMBL.put( "ASSMBL_MANUFACT_CD", "\'ABC11\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_IOE_ASSMBL, lAlIoeASSMBL ) );
      Assert.assertTrue( runValidateIOEASSMBL( false ) == 1 );
   }


   /**
    * Test import when a Tracked component is linked to an Assembly with no config. slot
    */
   @Test
   public void test_AL_IOE_ASSMBL_IMPORT_TRK_ASSY_IMPORT_03() {

      test_AL_IOE_ASSMBL_IMPORT_TRK_ASSY_VALIDATION_03();

      // run validation on the import procedure
      Assert.assertTrue( runImportIOEASSMBL( false ) == 1 );

      // check maintenix table with staging table
      runCheckImportValidation( "SN0000P7" );
   }


   /**
    * Test Validation when a Serialized component linked to an Assembly with no config slot entered.
    * This test case will also verify ASSMBL_MANUFACT_CD has been changed to uppercase and trimmed
    */
   // @Test
   public void test_AL_IOE_ASSMBL_IMPORT_SER_ASSY_VALIDATION_01() {

      // AL_IOE_ASSMBL table
      Map<String, String> lAlIoeASSMBL = new LinkedHashMap<>();

      lAlIoeASSMBL.put( "SERIAL_NO_OEM", "\'SNENG1000956\'" );
      lAlIoeASSMBL.put( "PART_NO_OEM", "\'PN_E200-1\'" );
      // This value has been changed to validate the upper case and trim change for OPER=21977
      lAlIoeASSMBL.put( "MANUFACT_CD", "\' ABc11 \'" );
      lAlIoeASSMBL.put( "ASSMBL_BOM_CD", null );
      lAlIoeASSMBL.put( "EQP_POS_CD", null );
      lAlIoeASSMBL.put( "ASSMBL_SERIAL_NO_OEM", "\'SNENG1000\'" );
      lAlIoeASSMBL.put( "ASSMBL_PART_NO_OEM", "\'PN_E200\'" );
      lAlIoeASSMBL.put( "ASSMBL_MANUFACT_CD", "\'ABC11\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_IOE_ASSMBL, lAlIoeASSMBL ) );
      Assert.assertTrue( runValidateIOEASSMBL( false ) == 1 );
   }


   /**
    * Test import when a Serialized component is loose inventory linked to an Assembly with no
    * config slot entered
    */
   @Test
   public void test_AL_IOE_ASSMBL_IMPORT_SER_ASSY_IMPORT_01() {

      test_AL_IOE_ASSMBL_IMPORT_SER_ASSY_VALIDATION_01();

      // run validation on the import procedure
      Assert.assertTrue( runImportIOEASSMBL( false ) == 1 );

      // check maintenix table with staging table
      runCheckImportValidation( "SNENG1000956", "ABC11" );
   }


   /**
    * Run the task Validation using a direct call to the plsql using a prepared statement. This does
    * not call any batch files.
    *
    *
    */
   protected int runValidateIOEASSMBL( boolean allornone ) {

      int lReturn = 0;
      CallableStatement lPrepareCallInventory;

      try {
         if ( allornone ) {
            lPrepareCallInventory = getConnection().prepareCall(
                  "BEGIN  al_ioe_assmbl_import.validate(aib_allornone => true, aon_retcode =>?, aov_retmsg =>?); END;" );
         } else {
            lPrepareCallInventory = getConnection().prepareCall(
                  "BEGIN  al_ioe_assmbl_import.validate(aib_allornone => false, aon_retcode =>?, aov_retmsg =>?); END;" );

         }

         lPrepareCallInventory.registerOutParameter( 1, Types.INTEGER );
         lPrepareCallInventory.registerOutParameter( 2, Types.VARCHAR );

         lPrepareCallInventory.execute();
         commit();
         lReturn = lPrepareCallInventory.getInt( 1 );
      } catch ( SQLException e ) {
         e.printStackTrace();
      }
      return lReturn;

   }


   /**
    * Run the task Validation using a direct call to the plsql using a prepared statement. This does
    * not call any batch files.
    *
    *
    */
   protected int runImportIOEASSMBL( boolean allornone ) {
      int lReturn = 0;
      CallableStatement lPrepareCallInventory;

      try {
         if ( allornone ) {
            lPrepareCallInventory = getConnection().prepareCall(
                  "BEGIN   al_ioe_assmbl_import.import(aib_allornone => true, aon_retcode =>?, aov_retmsg =>?); END;" );
         } else {
            lPrepareCallInventory = getConnection().prepareCall(
                  "BEGIN   al_ioe_assmbl_import.import(aib_allornone => false, aon_retcode =>?, aov_retmsg =>?); END;" );

         }

         lPrepareCallInventory.registerOutParameter( 1, Types.INTEGER );
         lPrepareCallInventory.registerOutParameter( 2, Types.VARCHAR );

         lPrepareCallInventory.execute();
         commit();
         lReturn = lPrepareCallInventory.getInt( 1 );
      } catch ( SQLException e ) {
         e.printStackTrace();
      }
      return lReturn;

   }


   /**
    * This method checks that the import was successful by calling other functions that compare each
    * row in staging and maitenix tables in finance area and make sure they are identical through
    * SQL queries
    *
    *
    */
   private void runCheckImportValidation( String aSerialNoOem ) {
      try {
         compareRowIOEASSMBL( aSerialNoOem );
      } catch ( Exception e ) {
         e.printStackTrace();
      }
   }


   /**
    * This method checks that the import was successful by calling other functions that compare each
    * row in staging and maitenix tables in finance area and make sure they are identical through
    * SQL queries
    *
    * @param aSerialNoOem
    *           - Serial_No_OEM expected to be in the database
    * @param aManufactCd
    *           - Manufact_cd expected to be in the database
    *
    */
   private void runCheckImportValidation( String aSerialNoOem, String aManufactCd ) {
      try {
         compareRowIOEASSMBL( aSerialNoOem, aManufactCd );
      } catch ( Exception e ) {
         e.printStackTrace();
      }
   }


   /**
    * compareRowEQPMANUFACTUREMAP will make sure the rows in EQP_MANUFACT are identical to the ones
    * in c_eqp_manufact_map through an SQL query
    *
    * @throws SQLException
    */
   private void compareRowIOEASSMBL( String aSerialNoOem ) throws SQLException {
      String lSerialNoOem = null;

      try {
         String NOdbID =
               StringUtils.chop( getQueryResultWhere( "select INV_NO_DB_ID from inv_inv_oem_assmbl",
                     "INV_NO_DB_ID" ) );

         String NOID = StringUtils.chop(
               getQueryResultWhere( "select INV_NO_ID from inv_inv_oem_assmbl", "INV_NO_ID" ) );

         lSerialNoOem =
               StringUtils.chop( getQueryResultWhere(
                     "select inv_inv.serial_no_oem from inv_inv where inv_inv.inv_no_db_id=\'"
                           + NOdbID + "\'" + "  and inv_inv.inv_no_id=\'" + NOID + "\'",
                     "SERIAL_NO_OEM" ) );

      } catch ( Exception e ) {
         e.printStackTrace();

      }
      Assert.assertTrue( aSerialNoOem.equalsIgnoreCase( lSerialNoOem ) );
   }


   /**
    * compareRowEQPMANUFACTUREMAP will make sure the rows in EQP_MANUFACT are identical to the ones
    * in c_eqp_manufact_map through an SQL query
    *
    * @throws SQLException
    */
   private void compareRowIOEASSMBL( String aSerialNoOem, String aManufactCd ) throws SQLException {
      String lSerialNoOem = null;
      String lManufactCd = null;
      String lAssmblManufactCd = null;

      try {
         String NOdbID =
               StringUtils.chop( getQueryResultWhere( "select INV_NO_DB_ID from inv_inv_oem_assmbl",
                     "INV_NO_DB_ID" ) );

         String NOID = StringUtils.chop(
               getQueryResultWhere( "select INV_NO_ID from inv_inv_oem_assmbl", "INV_NO_ID" ) );

         lSerialNoOem =
               StringUtils.chop( getQueryResultWhere(
                     "select inv_inv.serial_no_oem from inv_inv where inv_inv.inv_no_db_id=\'"
                           + NOdbID + "\'" + "  and inv_inv.inv_no_id=\'" + NOID + "\'",
                     "SERIAL_NO_OEM" ) );
         lManufactCd = StringUtils.chop( getQueryResultWhere(
               "select manufact_cd from al_proc_ioe_assmbl where upper(serial_no_oem) =\'"
                     + aSerialNoOem + "\'",
               "MANUFACT_CD" ) );
         lAssmblManufactCd = StringUtils.chop( getQueryResultWhere(
               "select assmbl_manufact_cd from al_proc_ioe_assmbl where upper(serial_no_oem) =\'"
                     + aSerialNoOem + "\'",
               "ASSMBL_MANUFACT_CD" ) );
      } catch ( Exception e ) {
         e.printStackTrace();
      }

      boolean foundManufact = false;
      if ( aManufactCd.equals( lManufactCd ) || aManufactCd.equals( lAssmblManufactCd ) )
         foundManufact = true;
      else
         foundManufact = false;

      Assert.assertTrue( "Serial_No_OEM is incorrect.", aSerialNoOem.equals( lSerialNoOem ) );
      Assert.assertTrue( "Manufact_cd is incorrect.", foundManufact );
   }


   /**
    * This method will temporally to change serial number of XXX to be AUTOTRK00002 on first record
    * where part_no_oem='A0000010' This change is necessary since in DB, there are too many recodes
    * with same part oem and same serial number
    *
    *
    */

   private void convertTRKSerialNUM() {
      ResultSet ResultSetRecords;
      StringBuilder Strquery = new StringBuilder();
      Strquery.append( "select inv_inv.INV_NO_DB_ID, INV_NO_ID from inv_inv " )
            .append(
                  "inner join eqp_part_no on inv_inv.part_no_db_id=eqp_part_no.part_no_db_id and " )
            .append(
                  "inv_inv.part_no_id=eqp_part_no.part_no_id where eqp_part_no.part_no_oem='A0000010'" );

      try {
         ResultSetRecords = runQuery( Strquery.toString() );

         ResultSetRecords.next();
         strINVDBID = ResultSetRecords.getString( "INV_NO_DB_ID" );
         strINVID = ResultSetRecords.getString( "INV_NO_ID" );
         ResultSetRecords.close();

      } catch ( SQLException e ) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      Strquery.delete( 0, Strquery.length() );

      PreparedStatement lStatement;
      Strquery.append( "UPDATE inv_inv SET SERIAL_NO_OEM='AUTOTRK00002' WHERE INV_NO_DB_ID=" )
            .append( strINVDBID ).append( " and INV_NO_ID= " ).append( strINVID );

      try {
         lStatement = getConnection().prepareStatement( Strquery.toString() );

         lStatement.executeUpdate( Strquery.toString() );
         commit();
      } catch ( SQLException e ) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }


   /**
    * This method will resume TRK serial number back to XXX
    *
    *
    */

   private void resumeTRKSerialNUM() {

      StringBuilder Strquery = new StringBuilder();

      PreparedStatement lStatement;
      Strquery.append( "UPDATE inv_inv SET SERIAL_NO_OEM='XXX' WHERE INV_NO_DB_ID=" )
            .append( strINVDBID ).append( " and INV_NO_ID= " ).append( strINVID );

      try {
         lStatement = getConnection().prepareStatement( Strquery.toString() );

         lStatement.executeUpdate( Strquery.toString() );
         commit();
      } catch ( SQLException e ) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }

}
