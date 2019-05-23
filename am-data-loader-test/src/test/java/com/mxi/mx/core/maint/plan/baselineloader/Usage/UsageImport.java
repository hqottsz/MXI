package com.mxi.mx.core.maint.plan.baselineloader.Usage;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.mxi.mx.core.maint.plan.datamodels.AssmblBomID;
import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;
import com.mxi.mx.util.TableUtil;


/**
 * This test suite contains test cases on validation and import functionality of usage_import
 * package.
 *
 * @author ALICIA QIAN
 */
public class UsageImport extends Usage {

   @Rule
   public TestName testName = new TestName();


   /**
    * Setup before executing each individual test
    *
    */
   @Before
   @Override
   public void before() throws Exception {

      super.before();
      clearBaselineLoaderTables();

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
    * This test is to verify usage_import validation functionality of staging table c_dyn_usage_def
    * , sys slot= acft&trk; usage=hours
    *
    */

   public void test_ACFT_TRK_Dyn_HOURS_VALIDATION() {
      System.out.println( "=======Starting: " + testName.getMethodName()
            + " Validation========================" );

      // C_DYN_USAGE_DEF table
      Map<String, String> lUsages = new LinkedHashMap<>();

      lUsages.put( "ASSMBL_CD", "\'" + iAssmblCD_ACFT + "\'" );
      lUsages.put( "IPC_REF_CD", "\'" + iAssmblCD_ACFT + "\'" );
      lUsages.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_HOUR + "\'" );
      lUsages.put( "CUST_CALC_BOOL", "'N'" );
      lUsages.put( "EQN_LDESC", null );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_DYN_USAGE_DEF, lUsages ) );

      // TRK slot
      lUsages.clear();
      lUsages.put( "ASSMBL_CD", "\'" + iAssmblCD_ACFT + "\'" );
      lUsages.put( "IPC_REF_CD", "\'" + iIPC_REF_CD_TRK + "\'" );
      lUsages.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_HOUR + "\'" );
      lUsages.put( "CUST_CALC_BOOL", "'N'" );
      lUsages.put( "EQN_LDESC", null );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_DYN_USAGE_DEF, lUsages ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify usage_import import functionality of staging table c_dyn_usage_def ,
    * sys slot= acft&trk; usage=hours
    *
    */
   @Test
   public void test_ACFT_TRK_Dyn_HOURS_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      test_ACFT_TRK_Dyn_HOURS_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify mim_part_numdata table
      simpleIDs ldatatypeIds = getDataTypeIDs( "US", "HOUR", iDATA_TYPE_CD_HOUR );
      AssmblBomID lbomID = getAssmblBomId( iAssmblCD_ACFT, iAssmblCD_ACFT );
      VerifyMIMPARTNUMDATA( iAssmblCD_ACFT, lbomID.getID(), ldatatypeIds, "0", null );

      lbomID = getAssmblBomId( iAssmblCD_ACFT, iIPC_REF_CD_TRK );
      VerifyMIMPARTNUMDATA( iAssmblCD_ACFT, lbomID.getID(), ldatatypeIds, "0", null );

      // Verify eqp_data_source_spec table
      VerifyEQPDATASOURCESPEC( iAssmblCD_ACFT, iDATA_SOURCE_CD, ldatatypeIds );

   }


   /**
    * This test is to verify usage_import validation functionality of staging table c_dyn_usage_def
    * , sys slot= acft&trk;usage=cycles
    *
    */

   public void test_ACFT_TRK_Dyn_CYCLES_VALIDATION() {
      System.out.println( "=======Starting: " + testName.getMethodName()
            + " Validation========================" );

      // C_DYN_USAGE_DEF table
      Map<String, String> lUsages = new LinkedHashMap<>();

      lUsages.put( "ASSMBL_CD", "\'" + iAssmblCD_ACFT + "\'" );
      lUsages.put( "IPC_REF_CD", "\'" + iAssmblCD_ACFT + "\'" );
      lUsages.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_CYCLES + "\'" );
      lUsages.put( "CUST_CALC_BOOL", "'Y'" );
      lUsages.put( "EQN_LDESC", "'AUTOTEST'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_DYN_USAGE_DEF, lUsages ) );

      // TRK slot
      lUsages.clear();
      lUsages.put( "ASSMBL_CD", "\'" + iAssmblCD_ACFT + "\'" );
      lUsages.put( "IPC_REF_CD", "\'" + iIPC_REF_CD_TRK + "\'" );
      lUsages.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_CYCLES + "\'" );
      lUsages.put( "CUST_CALC_BOOL", "'Y'" );
      lUsages.put( "EQN_LDESC", "'AUTOTEST'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_DYN_USAGE_DEF, lUsages ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify usage_import import functionality of staging table c_dyn_usage_def ,
    * sys slot= acft&trk; usage=cycles
    *
    */
   @Test
   public void test_ACFT_TRK_Dyn_CYCLES_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      test_ACFT_TRK_Dyn_CYCLES_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify mim_part_numdata table
      simpleIDs ldatatypeIds = getDataTypeIDs( "US", "CYCLES", iDATA_TYPE_CD_CYCLES );
      AssmblBomID lbomID = getAssmblBomId( iAssmblCD_ACFT, iAssmblCD_ACFT );
      VerifyMIMPARTNUMDATA( iAssmblCD_ACFT, lbomID.getID(), ldatatypeIds, "1", "AUTOTEST" );

      lbomID = getAssmblBomId( iAssmblCD_ACFT, iIPC_REF_CD_TRK );
      VerifyMIMPARTNUMDATA( iAssmblCD_ACFT, lbomID.getID(), ldatatypeIds, "1", "AUTOTEST" );

      // Verify eqp_data_source_spec table
      String lQuery = "select 1 from " + TableUtil.EQP_DATA_SOURCE_SPEC + " where ASSMBL_CD='"
            + iAssmblCD_ACFT + "' and DATA_TYPE_DB_ID=" + ldatatypeIds.getNO_DB_ID()
            + " and DATA_TYPE_ID=" + ldatatypeIds.getNO_ID();
      Assert.assertFalse( "Check eqp_data_source_spec table to verify the record not exists",
            RecordsExist( lQuery ) );

   }


   /**
    * This test is to verify usage_import validation functionality of staging table c_dyn_usage_def
    * , sys slot= acft&subassy; usage=ECYC
    *
    */

   public void test_ACFT_Subassy_ECYC_VALIDATION() {
      System.out.println( "=======Starting: " + testName.getMethodName()
            + " Validation========================" );

      // C_DYN_USAGE_DEF table
      Map<String, String> lUsages = new LinkedHashMap<>();

      lUsages.put( "ASSMBL_CD", "\'" + iAssmblCD_ACFT + "\'" );
      lUsages.put( "IPC_REF_CD", "\'" + iAssmblCD_ACFT + "\'" );
      lUsages.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_ECYC + "\'" );
      lUsages.put( "CUST_CALC_BOOL", "'Y'" );
      lUsages.put( "EQN_LDESC", "'AUTOTEST'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_DYN_USAGE_DEF, lUsages ) );

      // TRK slot
      lUsages.clear();
      lUsages.put( "ASSMBL_CD", "\'" + iAssmblCD_ACFT + "\'" );
      lUsages.put( "IPC_REF_CD", "\'" + iIPC_REF_CD_SUBASSY + "\'" );
      lUsages.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_ECYC + "\'" );
      lUsages.put( "CUST_CALC_BOOL", "'Y'" );
      lUsages.put( "EQN_LDESC", "'AUTOTEST'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_DYN_USAGE_DEF, lUsages ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify usage_import import functionality of staging table c_dyn_usage_def ,
    * sys slot= acft&subassy; usage=ECYC
    *
    */
   @Test
   public void test_ACFT_Subassy_ECYC_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      test_ACFT_Subassy_ECYC_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify mim_part_numdata table
      simpleIDs ldatatypeIds = getDataTypeIDs( "US", "CYCLES", iDATA_TYPE_CD_ECYC );
      AssmblBomID lbomID = getAssmblBomId( iAssmblCD_ACFT, iAssmblCD_ACFT );
      VerifyMIMPARTNUMDATA( iAssmblCD_ACFT, lbomID.getID(), ldatatypeIds, "1", "AUTOTEST" );

      lbomID = getAssmblBomId( iAssmblCD_ACFT, iIPC_REF_CD_SUBASSY );
      VerifyMIMPARTNUMDATA( iAssmblCD_ACFT, lbomID.getID(), ldatatypeIds, "1", "AUTOTEST" );

      // Verify eqp_data_source_spec table
      String lQuery = "select 1 from " + TableUtil.EQP_DATA_SOURCE_SPEC + " where ASSMBL_CD='"
            + iAssmblCD_ACFT + "' and DATA_TYPE_DB_ID=" + ldatatypeIds.getNO_DB_ID()
            + " and DATA_TYPE_ID=" + ldatatypeIds.getNO_ID();
      Assert.assertFalse( "Check eqp_data_source_spec table to verify the record not exists",
            RecordsExist( lQuery ) );

   }


   /**
    * This test is to verify usage_import validation functionality of staging table c_dyn_usage_def
    * , sys slot= eng root; usage=ECYC
    *
    */

   public void test_ENG_Dyn_ECYC_VALIDATION() {
      System.out.println( "=======Starting: " + testName.getMethodName()
            + " Validation========================" );

      // C_DYN_USAGE_DEF table
      Map<String, String> lUsages = new LinkedHashMap<>();

      lUsages.put( "ASSMBL_CD", "\'" + iAssmblCD_ENG + "\'" );
      lUsages.put( "IPC_REF_CD", "\'" + iAssmblCD_ENG + "\'" );
      lUsages.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_ECYC + "\'" );
      lUsages.put( "CUST_CALC_BOOL", "'N'" );
      lUsages.put( "EQN_LDESC", null );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_DYN_USAGE_DEF, lUsages ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify usage_import import functionality of staging table c_dyn_usage_def ,
    * sys slot=eng root; usage=ECYC
    *
    */
   @Test
   public void test_ENG_Dyn_ECYC_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      test_ENG_Dyn_ECYC_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify mim_part_numdata table
      simpleIDs ldatatypeIds = getDataTypeIDs( "US", "CYCLES", iDATA_TYPE_CD_ECYC );
      AssmblBomID lbomID = getAssmblBomId( iAssmblCD_ENG, iAssmblCD_ENG );
      VerifyMIMPARTNUMDATA( iAssmblCD_ENG, lbomID.getID(), ldatatypeIds, "0", null );

      // Verify eqp_data_source_spec table
      VerifyEQPDATASOURCESPEC( iAssmblCD_ENG, iDATA_SOURCE_CD, ldatatypeIds );

   }


   /**
    * This test is to verify usage_import validation functionality of staging table c_dyn_usage_def
    * , sys slot= acft&trk and eng root. usage=cycles&ECYC. The purpose of this test case is to
    * validate functionality on multiple records.
    *
    */

   public void test_Multiple_Dyn_DifferentAssmbly_VALIDATION() {
      System.out.println( "=======Starting: " + testName.getMethodName()
            + " Validation========================" );

      test_ACFT_TRK_Dyn_CYCLES_VALIDATION();
      test_ENG_Dyn_ECYC_VALIDATION();

   }


   /**
    * This test is to verify usage_import validation functionality of staging table c_dyn_usage_def
    * , sys slot= acft&trk and eng root. usage=cycles&ECYC. The purpose of this test case is to
    * validate import functionality on multiple records.
    *
    */
   @Test
   public void test_Multiple_Dyn_DifferentAssmbly_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      test_Multiple_Dyn_DifferentAssmbly_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify acft&trk cycles
      // Verify mim_part_numdata table
      simpleIDs ldatatypeIds = getDataTypeIDs( "US", "CYCLES", iDATA_TYPE_CD_CYCLES );
      AssmblBomID lbomID = getAssmblBomId( iAssmblCD_ACFT, iAssmblCD_ACFT );
      VerifyMIMPARTNUMDATA( iAssmblCD_ACFT, lbomID.getID(), ldatatypeIds, "1", "AUTOTEST" );

      lbomID = getAssmblBomId( iAssmblCD_ACFT, iIPC_REF_CD_TRK );
      VerifyMIMPARTNUMDATA( iAssmblCD_ACFT, lbomID.getID(), ldatatypeIds, "1", "AUTOTEST" );

      // Verify eqp_data_source_spec table
      String lQuery = "select 1 from " + TableUtil.EQP_DATA_SOURCE_SPEC + " where ASSMBL_CD='"
            + iAssmblCD_ACFT + "' and DATA_TYPE_DB_ID=" + ldatatypeIds.getNO_DB_ID()
            + " and DATA_TYPE_ID=" + ldatatypeIds.getNO_ID();
      Assert.assertFalse( "Check eqp_data_source_spec table to verify the record not exists",
            RecordsExist( lQuery ) );

      // Verify eng root ecyc
      // Verify mim_part_numdata table
      ldatatypeIds = getDataTypeIDs( "US", "CYCLES", iDATA_TYPE_CD_ECYC );
      lbomID = getAssmblBomId( iAssmblCD_ENG, iAssmblCD_ENG );
      VerifyMIMPARTNUMDATA( iAssmblCD_ENG, lbomID.getID(), ldatatypeIds, "0", null );

      // Verify eqp_data_source_spec table
      VerifyEQPDATASOURCESPEC( iAssmblCD_ENG, iDATA_SOURCE_CD, ldatatypeIds );

   }


   /**
    * This test is to verify usage_import validation functionality of staging table c_dyn_usage_def
    * , sys slot= acft&trk. usage=cycles&hours. The purpose of this test case is to validate
    * functionality on multiple records.
    *
    */

   public void test_Multiple_Dyn_SameAssmbly_VALIDATION() {
      System.out.println( "=======Starting: " + testName.getMethodName()
            + " Validation========================" );

      test_ACFT_TRK_Dyn_HOURS_VALIDATION();
      test_ACFT_TRK_Dyn_CYCLES_VALIDATION();

   }


   /**
    * This test is to verify usage_import validation functionality of staging table c_dyn_usage_def
    * , sys slot= acft&trk. usage=cycles&hours. The purpose of this test case is to validate import
    * functionality on multiple records.
    *
    */
   @Test
   public void test_Multiple_Dyn_SameAssmbly_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      test_Multiple_Dyn_SameAssmbly_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify acft&trk hours
      // Verify mim_part_numdata table
      simpleIDs ldatatypeIds = getDataTypeIDs( "US", "HOUR", iDATA_TYPE_CD_HOUR );
      AssmblBomID lbomID = getAssmblBomId( iAssmblCD_ACFT, iAssmblCD_ACFT );
      VerifyMIMPARTNUMDATA( iAssmblCD_ACFT, lbomID.getID(), ldatatypeIds, "0", null );

      lbomID = getAssmblBomId( iAssmblCD_ACFT, iIPC_REF_CD_TRK );
      VerifyMIMPARTNUMDATA( iAssmblCD_ACFT, lbomID.getID(), ldatatypeIds, "0", null );

      // Verify eqp_data_source_spec table
      VerifyEQPDATASOURCESPEC( iAssmblCD_ACFT, iDATA_SOURCE_CD, ldatatypeIds );

      // Verify acft&trk cycles
      // Verify mim_part_numdata table
      ldatatypeIds = getDataTypeIDs( "US", "CYCLES", iDATA_TYPE_CD_CYCLES );
      lbomID = getAssmblBomId( iAssmblCD_ACFT, iAssmblCD_ACFT );
      VerifyMIMPARTNUMDATA( iAssmblCD_ACFT, lbomID.getID(), ldatatypeIds, "1", "AUTOTEST" );

      lbomID = getAssmblBomId( iAssmblCD_ACFT, iIPC_REF_CD_TRK );
      VerifyMIMPARTNUMDATA( iAssmblCD_ACFT, lbomID.getID(), ldatatypeIds, "1", "AUTOTEST" );

      // Verify eqp_data_source_spec table
      String lQuery = "select 1 from " + TableUtil.EQP_DATA_SOURCE_SPEC + " where ASSMBL_CD='"
            + iAssmblCD_ACFT + "' and DATA_TYPE_DB_ID=" + ldatatypeIds.getNO_DB_ID()
            + " and DATA_TYPE_ID=" + ldatatypeIds.getNO_ID();
      Assert.assertFalse( "Check eqp_data_source_spec table to verify the record not exists",
            RecordsExist( lQuery ) );

   }


   /**
    * This test is to verify usage_import validation functionality of staging table c_std_usage_def
    * , sys slot= acft&trk; usage=HRS
    *
    */

   public void test_ACFT_TRK_Std_HOURS_VALIDATION() {
      System.out.println( "=======Starting: " + testName.getMethodName()
            + " Validation========================" );

      // C_STD_USAGE_DEF table
      Map<String, String> lUsages = new LinkedHashMap<>();

      lUsages.put( "ASSMBL_CD", "\'" + iAssmblCD_ACFT + "\'" );
      lUsages.put( "IPC_REF_CD", "\'" + iAssmblCD_ACFT + "\'" );
      lUsages.put( "HRS_BOOL", "'Y'" );
      lUsages.put( "CYC_BOOL", "'N'" );
      lUsages.put( "AOT_BOOL", "'N'" );
      lUsages.put( "ACYC_BOOL", "'N'" );
      lUsages.put( "ECYC_BOOL", "'N'" );
      lUsages.put( "EOT_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STD_USAGE_DEF, lUsages ) );

      // TRK slot
      lUsages.clear();
      lUsages.put( "ASSMBL_CD", "\'" + iAssmblCD_ACFT + "\'" );
      lUsages.put( "IPC_REF_CD", "\'" + iIPC_REF_CD_TRK + "\'" );
      lUsages.put( "HRS_BOOL", "'Y'" );
      lUsages.put( "CYC_BOOL", "'N'" );
      lUsages.put( "AOT_BOOL", "'N'" );
      lUsages.put( "ACYC_BOOL", "'N'" );
      lUsages.put( "ECYC_BOOL", "'N'" );
      lUsages.put( "EOT_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STD_USAGE_DEF, lUsages ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify usage_import validation functionality of staging table c_std_usage_def
    * , sys slot= acft&trk; usage=HRS
    *
    */
   @Test
   public void test_ACFT_TRK_Std_HOURS_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      test_ACFT_TRK_Std_HOURS_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify mim_part_numdata table
      simpleIDs ldatatypeIds = getDataTypeIDs( "US", "HOUR", iDATA_TYPE_CD_HOUR );
      AssmblBomID lbomID = getAssmblBomId( iAssmblCD_ACFT, iAssmblCD_ACFT );
      VerifyMIMPARTNUMDATA( iAssmblCD_ACFT, lbomID.getID(), ldatatypeIds, "0", null );

      lbomID = getAssmblBomId( iAssmblCD_ACFT, iIPC_REF_CD_TRK );
      VerifyMIMPARTNUMDATA( iAssmblCD_ACFT, lbomID.getID(), ldatatypeIds, "0", null );

      // Verify eqp_data_source_spec table
      String lQuery = "select 1 from " + TableUtil.EQP_DATA_SOURCE_SPEC + " where ASSMBL_CD='"
            + iAssmblCD_ACFT + "' and DATA_TYPE_DB_ID=" + ldatatypeIds.getNO_DB_ID()
            + " and DATA_TYPE_ID=" + ldatatypeIds.getNO_ID();
      Assert.assertFalse( "Check eqp_data_source_spec table to verify the record not exists",
            RecordsExist( lQuery ) );

   }


   /**
    * This test is to verify usage_import validation functionality of staging table c_std_usage_def
    * , sys slot= acft&subassy; usage=ECYC
    *
    */

   public void test_ACFT_Subassy_Std_ECYC_VALIDATION() {
      System.out.println( "=======Starting: " + testName.getMethodName()
            + " Validation========================" );

      // C_STD_USAGE_DEF table
      Map<String, String> lUsages = new LinkedHashMap<>();

      lUsages.put( "ASSMBL_CD", "\'" + iAssmblCD_ACFT + "\'" );
      lUsages.put( "IPC_REF_CD", "\'" + iAssmblCD_ACFT + "\'" );
      lUsages.put( "HRS_BOOL", "'N'" );
      lUsages.put( "CYC_BOOL", "'N'" );
      lUsages.put( "AOT_BOOL", "'N'" );
      lUsages.put( "ACYC_BOOL", "'N'" );
      lUsages.put( "ECYC_BOOL", "'Y'" );
      lUsages.put( "EOT_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STD_USAGE_DEF, lUsages ) );

      // subassy slot
      lUsages.clear();
      lUsages.put( "ASSMBL_CD", "\'" + iAssmblCD_ACFT + "\'" );
      lUsages.put( "IPC_REF_CD", "\'" + iIPC_REF_CD_SUBASSY + "\'" );
      lUsages.put( "HRS_BOOL", "'N'" );
      lUsages.put( "CYC_BOOL", "'N'" );
      lUsages.put( "AOT_BOOL", "'N'" );
      lUsages.put( "ACYC_BOOL", "'N'" );
      lUsages.put( "ECYC_BOOL", "'Y'" );
      lUsages.put( "EOT_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STD_USAGE_DEF, lUsages ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify usage_import import functionality of staging table c_std_usage_def ,
    * sys slot= acft&subassy; usage=ECYC
    *
    */
   @Test
   public void test_ACFT_Subassy_Std_ECYC_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      test_ACFT_Subassy_Std_ECYC_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify mim_part_numdata table
      simpleIDs ldatatypeIds = getDataTypeIDs( "US", "CYCLES", iDATA_TYPE_CD_ECYC );
      AssmblBomID lbomID = getAssmblBomId( iAssmblCD_ACFT, iIPC_REF_CD_SUBASSY );
      VerifyMIMPARTNUMDATA( iAssmblCD_ACFT, lbomID.getID(), ldatatypeIds, "0", null );

      lbomID = getAssmblBomId( iAssmblCD_ACFT, iIPC_REF_CD_SUBASSY );
      VerifyMIMPARTNUMDATA( iAssmblCD_ACFT, lbomID.getID(), ldatatypeIds, "0", null );

      // Verify eqp_data_source_spec table
      String lQuery = "select 1 from " + TableUtil.EQP_DATA_SOURCE_SPEC + " where ASSMBL_CD='"
            + iAssmblCD_ACFT + "' and DATA_TYPE_DB_ID=" + ldatatypeIds.getNO_DB_ID()
            + " and DATA_TYPE_ID=" + ldatatypeIds.getNO_ID();
      Assert.assertFalse( "Check eqp_data_source_spec table to verify the record not exists",
            RecordsExist( lQuery ) );

   }


   /**
    * This test is to verify usage_import validation functionality of staging table c_std_usage_def
    * , sys slot= eng root; usage=ECYC
    *
    */

   public void test_ENG_Std_ECYC_VALIDATION() {
      System.out.println( "=======Starting: " + testName.getMethodName()
            + " Validation========================" );

      // C_STD_USAGE_DEF table
      Map<String, String> lUsages = new LinkedHashMap<>();

      lUsages.put( "ASSMBL_CD", "\'" + iAssmblCD_ENG + "\'" );
      lUsages.put( "IPC_REF_CD", "\'" + iAssmblCD_ENG + "\'" );
      lUsages.put( "HRS_BOOL", "'N'" );
      lUsages.put( "CYC_BOOL", "'N'" );
      lUsages.put( "AOT_BOOL", "'N'" );
      lUsages.put( "ACYC_BOOL", "'N'" );
      lUsages.put( "ECYC_BOOL", "'Y'" );
      lUsages.put( "EOT_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STD_USAGE_DEF, lUsages ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify usage_import import functionality of staging table c_std_usage_def ,
    * sys slot= eng root; usage=ECYC
    *
    */
   @Test
   public void test_ENG_Std_ECYC_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      test_ENG_Std_ECYC_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify mim_part_numdata table
      simpleIDs ldatatypeIds = getDataTypeIDs( "US", "CYCLES", iDATA_TYPE_CD_ECYC );
      AssmblBomID lbomID = getAssmblBomId( iAssmblCD_ENG, iAssmblCD_ENG );
      VerifyMIMPARTNUMDATA( iAssmblCD_ENG, lbomID.getID(), ldatatypeIds, "0", null );

      lbomID = getAssmblBomId( iAssmblCD_ENG, iAssmblCD_ENG );
      VerifyMIMPARTNUMDATA( iAssmblCD_ENG, lbomID.getID(), ldatatypeIds, "0", null );

      // Verify eqp_data_source_spec table
      String lQuery = "select 1 from " + TableUtil.EQP_DATA_SOURCE_SPEC + " where ASSMBL_CD='"
            + iAssmblCD_ENG + "' and DATA_TYPE_DB_ID=" + ldatatypeIds.getNO_DB_ID()
            + " and DATA_TYPE_ID=" + ldatatypeIds.getNO_ID();
      Assert.assertFalse( "Check eqp_data_source_spec table to verify the record not exists",
            RecordsExist( lQuery ) );

   }


   /**
    * This test is to verify usage_import validation functionality of staging table c_std_usage_def
    * , sys slot= acft&TRK and eng root; usage=HRS and ECYC
    *
    */

   public void test_Multiple_Std_VALIDATION() {

      System.out.println( "=======Starting: " + testName.getMethodName()
            + " Validation========================" );

      test_ACFT_TRK_Std_HOURS_VALIDATION();
      test_ENG_Std_ECYC_VALIDATION();

   }


   /**
    * This test is to verify usage_import import functionality of staging table c_std_usage_def ,
    * sys slot= acft&TRK and eng root; usage=HRS and ECYC
    *
    */

   @Test
   public void test_Multiple_Std_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      test_Multiple_Std_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify HRS record on acft&TRK
      // Verify mim_part_numdata table
      simpleIDs ldatatypeIds = getDataTypeIDs( "US", "HOUR", iDATA_TYPE_CD_HOUR );
      AssmblBomID lbomID = getAssmblBomId( iAssmblCD_ACFT, iAssmblCD_ACFT );
      VerifyMIMPARTNUMDATA( iAssmblCD_ACFT, lbomID.getID(), ldatatypeIds, "0", null );

      lbomID = getAssmblBomId( iAssmblCD_ACFT, iIPC_REF_CD_TRK );
      VerifyMIMPARTNUMDATA( iAssmblCD_ACFT, lbomID.getID(), ldatatypeIds, "0", null );

      // Verify eqp_data_source_spec table
      String lQuery = "select 1 from " + TableUtil.EQP_DATA_SOURCE_SPEC + " where ASSMBL_CD='"
            + iAssmblCD_ACFT + "' and DATA_TYPE_DB_ID=" + ldatatypeIds.getNO_DB_ID()
            + " and DATA_TYPE_ID=" + ldatatypeIds.getNO_ID();
      Assert.assertFalse( "Check eqp_data_source_spec table to verify the record not exists",
            RecordsExist( lQuery ) );

      // verify ecyc on eng root
      // Verify mim_part_numdata table
      ldatatypeIds = getDataTypeIDs( "US", "CYCLES", iDATA_TYPE_CD_ECYC );
      lbomID = getAssmblBomId( iAssmblCD_ENG, iAssmblCD_ENG );
      VerifyMIMPARTNUMDATA( iAssmblCD_ENG, lbomID.getID(), ldatatypeIds, "0", null );

      lbomID = getAssmblBomId( iAssmblCD_ENG, iAssmblCD_ENG );
      VerifyMIMPARTNUMDATA( iAssmblCD_ENG, lbomID.getID(), ldatatypeIds, "0", null );

      // Verify eqp_data_source_spec table
      lQuery = "select 1 from " + TableUtil.EQP_DATA_SOURCE_SPEC + " where ASSMBL_CD='"
            + iAssmblCD_ENG + "' and DATA_TYPE_DB_ID=" + ldatatypeIds.getNO_DB_ID()
            + " and DATA_TYPE_ID=" + ldatatypeIds.getNO_ID();
      Assert.assertFalse( "Check eqp_data_source_spec table to verify the record not exists",
            RecordsExist( lQuery ) );

   }

}
