package com.mxi.mx.core.maint.plan.baselineloader.Usage;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.mxi.mx.util.TableUtil;


/**
 * This test suite contains test cases of error code validation functionality of usage_import
 * package.
 *
 * @author ALICIA QIAN
 */
public class UsageValidation extends Usage {

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
    * This test is to verify valid error code CFGUSG-00000:Standard and Dynamic usage tables cannot
    * both have values.
    *
    *
    */
   @Test
   public void test_CFGUSG_00000() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
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

      // std record
      lUsages.clear();
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

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkUsageErrorCode( testName.getMethodName(), "CFGUSG-00000" );

   }


   /**
    * This test is to verify valid error code CFGUSG-10000:C_STD_USAGE_DEF.assmbl_cd cannot be null.
    *
    *
    */

   @Test
   public void test_CFGUSG_10000() {
      System.out.println( "=======Starting: " + testName.getMethodName()
            + " Validation========================" );

      // C_STD_USAGE_DEF table
      Map<String, String> lUsages = new LinkedHashMap<>();

      // lUsages.put( "ASSMBL_CD", "\'" + iAssmblCD_ENG + "\'" );
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
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkUsageErrorCode( testName.getMethodName(), "CFGUSG-10000" );
   }


   /**
    * This test is to verify valid error code CFGUSG-10001:C_STD_USAGE_DEF.ipc_ref_cd cannot be null
    *
    *
    */

   @Test
   public void test_CFGUSG_10001() {
      System.out.println( "=======Starting: " + testName.getMethodName()
            + " Validation========================" );

      // C_STD_USAGE_DEF table
      Map<String, String> lUsages = new LinkedHashMap<>();

      lUsages.put( "ASSMBL_CD", "\'" + iAssmblCD_ENG + "\'" );
      // lUsages.put( "IPC_REF_CD", "\'" + iAssmblCD_ENG + "\'" );
      lUsages.put( "HRS_BOOL", "'N'" );
      lUsages.put( "CYC_BOOL", "'N'" );
      lUsages.put( "AOT_BOOL", "'N'" );
      lUsages.put( "ACYC_BOOL", "'N'" );
      lUsages.put( "ECYC_BOOL", "'Y'" );
      lUsages.put( "EOT_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STD_USAGE_DEF, lUsages ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkUsageErrorCode( testName.getMethodName(), "CFGUSG-10001" );
   }


   /**
    * This test is to verify valid error code CFGUSG-10002:C_DYN_USAGE_DEF.assmbl_cd cannot be null.
    *
    *
    */
   @Test
   public void test_CFGUSG_10002() {
      System.out.println( "=======Starting: " + testName.getMethodName()
            + " Validation========================" );

      // C_DYN_USAGE_DEF table
      Map<String, String> lUsages = new LinkedHashMap<>();

      // lUsages.put( "ASSMBL_CD", "\'" + iAssmblCD_ENG + "\'" );
      lUsages.put( "IPC_REF_CD", "\'" + iAssmblCD_ENG + "\'" );
      lUsages.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_ECYC + "\'" );
      lUsages.put( "CUST_CALC_BOOL", "'N'" );
      lUsages.put( "EQN_LDESC", null );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_DYN_USAGE_DEF, lUsages ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkUsageErrorCode( testName.getMethodName(), "CFGUSG-10002" );

   }


   /**
    * This test is to verify valid error code CFGUSG-10003:C_DYN_USAGE_DEF.ipc_ref_cd cannot be
    * NULL.
    *
    *
    */
   @Test
   public void test_CFGUSG_10003() {
      System.out.println( "=======Starting: " + testName.getMethodName()
            + " Validation========================" );

      // C_DYN_USAGE_DEF table
      Map<String, String> lUsages = new LinkedHashMap<>();

      lUsages.put( "ASSMBL_CD", "\'" + iAssmblCD_ENG + "\'" );
      // lUsages.put( "IPC_REF_CD", "\'" + iAssmblCD_ENG + "\'" );
      lUsages.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_ECYC + "\'" );
      lUsages.put( "CUST_CALC_BOOL", "'N'" );
      lUsages.put( "EQN_LDESC", null );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_DYN_USAGE_DEF, lUsages ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkUsageErrorCode( testName.getMethodName(), "CFGUSG-10003" );

   }


   /**
    * This test is to verify valid error code CFGUSG-10004:C_DYN_USAGE_DEF.data_type_cd cannot be
    * NULL.
    *
    *
    */
   @Test
   public void test_CFGUSG_10004() {
      System.out.println( "=======Starting: " + testName.getMethodName()
            + " Validation========================" );

      // C_DYN_USAGE_DEF table
      Map<String, String> lUsages = new LinkedHashMap<>();

      lUsages.put( "ASSMBL_CD", "\'" + iAssmblCD_ENG + "\'" );
      lUsages.put( "IPC_REF_CD", "\'" + iAssmblCD_ENG + "\'" );
      // lUsages.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_ECYC + "\'" );
      lUsages.put( "CUST_CALC_BOOL", "'N'" );
      lUsages.put( "EQN_LDESC", null );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_DYN_USAGE_DEF, lUsages ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkUsageErrorCode( testName.getMethodName(), "CFGUSG-10004" );

   }


   /**
    * This test is to verify valid error code CFGUSG-11000:C_PROC_USAGE_DEF record is not unique.
    *
    *
    */
   @Test
   public void test_CFGUSG_11000() {
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
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_DYN_USAGE_DEF, lUsages ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkUsageErrorCode( testName.getMethodName(), "CFGUSG-11000" );

   }


   /**
    * This test is to verify valid error code CFGUSG-12000:No data types on C_STD_USAGE_DEF record
    * have a positive (Y) value.
    *
    *
    */

   @Test
   public void test_CFGUSG_12000() {
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
      // lUsages.put( "ECYC_BOOL", "'Y'" );
      lUsages.put( "ECYC_BOOL", "'N'" );
      lUsages.put( "EOT_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STD_USAGE_DEF, lUsages ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkUsageErrorCode( testName.getMethodName(), "CFGUSG-12000" );
   }


   /**
    * This test is to verify valid error code CFGUSG-12001:C_PROC_USAGE_DEF.assmbl_cd not found in
    * Maintenix EQP_ASSMBL table.
    *
    *
    */

   @Test
   public void test_CFGUSG_12001() {
      System.out.println( "=======Starting: " + testName.getMethodName()
            + " Validation========================" );

      // C_STD_USAGE_DEF table
      Map<String, String> lUsages = new LinkedHashMap<>();

      // lUsages.put( "ASSMBL_CD", "\'" + iAssmblCD_ENG + "\'" );
      lUsages.put( "ASSMBL_CD", "'INVALID'" );
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
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkUsageErrorCode( testName.getMethodName(), "CFGUSG-12001" );
   }


   /**
    * This test is to verify valid error code CFGUSG-12002:C_PROC_USAGE_DEF.ipc_ref_cd not present
    * in Maintenix as EQP_ASSMBL_BOM.assmbl_bom_cd.
    *
    *
    */

   @Test
   public void test_CFGUSG_12002() {
      System.out.println( "=======Starting: " + testName.getMethodName()
            + " Validation========================" );

      // C_STD_USAGE_DEF table
      Map<String, String> lUsages = new LinkedHashMap<>();

      lUsages.put( "ASSMBL_CD", "\'" + iAssmblCD_ENG + "\'" );
      // lUsages.put( "IPC_REF_CD", "\'" + iAssmblCD_ENG + "\'" );
      lUsages.put( "IPC_REF_CD", "'INVALID'" );
      lUsages.put( "HRS_BOOL", "'N'" );
      lUsages.put( "CYC_BOOL", "'N'" );
      lUsages.put( "AOT_BOOL", "'N'" );
      lUsages.put( "ACYC_BOOL", "'N'" );
      lUsages.put( "ECYC_BOOL", "'Y'" );
      lUsages.put( "EOT_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STD_USAGE_DEF, lUsages ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkUsageErrorCode( testName.getMethodName(), "CFGUSG-12002" );
   }


   /**
    * This test is to verify valid error code CFGUSG-12003:C_PROC_USAGE_DEF Assembly / IPC Ref Code
    * combination is not valid.
    *
    *
    */

   @Test
   public void test_CFGUSG_12003() {
      System.out.println( "=======Starting: " + testName.getMethodName()
            + " Validation========================" );

      // C_STD_USAGE_DEF table
      Map<String, String> lUsages = new LinkedHashMap<>();

      lUsages.put( "ASSMBL_CD", "\'" + iAssmblCD_ENG + "\'" );
      // lUsages.put( "IPC_REF_CD", "\'" + iAssmblCD_ENG + "\'" );
      lUsages.put( "IPC_REF_CD", "'" + iIPC_REF_CD_TRK + "'" );
      lUsages.put( "HRS_BOOL", "'N'" );
      lUsages.put( "CYC_BOOL", "'N'" );
      lUsages.put( "AOT_BOOL", "'N'" );
      lUsages.put( "ACYC_BOOL", "'N'" );
      lUsages.put( "ECYC_BOOL", "'Y'" );
      lUsages.put( "EOT_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STD_USAGE_DEF, lUsages ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkUsageErrorCode( testName.getMethodName(), "CFGUSG-12003" );
   }


   /**
    * This test is to verify valid error code CFGUSG-12004:C_PROC_USAGE_DEF.data_type_cd is not
    * valid.
    *
    *
    */
   @Test
   public void test_CFGUSG_12004() {
      System.out.println( "=======Starting: " + testName.getMethodName()
            + " Validation========================" );

      // C_DYN_USAGE_DEF table
      Map<String, String> lUsages = new LinkedHashMap<>();

      lUsages.put( "ASSMBL_CD", "\'" + iAssmblCD_ENG + "\'" );
      lUsages.put( "IPC_REF_CD", "\'" + iAssmblCD_ENG + "\'" );
      // lUsages.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_ECYC + "\'" );
      lUsages.put( "DATA_TYPE_CD", "'INVALID'" );
      lUsages.put( "CUST_CALC_BOOL", "'N'" );
      lUsages.put( "EQN_LDESC", null );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_DYN_USAGE_DEF, lUsages ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkUsageErrorCode( testName.getMethodName(), "CFGUSG-12004" );

   }


   /**
    * This test is to verify valid error code CFGUSG-12005:C_STD_USAGE_DEF.hrs_bool must be Y or N.
    *
    *
    */

   @Test
   public void test_CFGUSG_12005() {
      System.out.println( "=======Starting: " + testName.getMethodName()
            + " Validation========================" );

      // C_STD_USAGE_DEF table
      Map<String, String> lUsages = new LinkedHashMap<>();

      lUsages.put( "ASSMBL_CD", "\'" + iAssmblCD_ENG + "\'" );
      lUsages.put( "IPC_REF_CD", "\'" + iAssmblCD_ENG + "\'" );
      lUsages.put( "HRS_BOOL", "'X'" );
      lUsages.put( "CYC_BOOL", "'N'" );
      lUsages.put( "AOT_BOOL", "'N'" );
      lUsages.put( "ACYC_BOOL", "'N'" );
      lUsages.put( "ECYC_BOOL", "'Y'" );
      lUsages.put( "EOT_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STD_USAGE_DEF, lUsages ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkUsageErrorCode( testName.getMethodName(), "CFGUSG-12005" );
   }


   /**
    * This test is to verify valid error code CFGUSG-12006:C_STD_USAGE_DEF.cyc_bool must be Y or N.
    *
    *
    */

   @Test
   public void test_CFGUSG_12006() {
      System.out.println( "=======Starting: " + testName.getMethodName()
            + " Validation========================" );

      // C_STD_USAGE_DEF table
      Map<String, String> lUsages = new LinkedHashMap<>();

      lUsages.put( "ASSMBL_CD", "\'" + iAssmblCD_ENG + "\'" );
      lUsages.put( "IPC_REF_CD", "\'" + iAssmblCD_ENG + "\'" );
      lUsages.put( "HRS_BOOL", "'N'" );
      lUsages.put( "CYC_BOOL", "'X'" );
      lUsages.put( "AOT_BOOL", "'N'" );
      lUsages.put( "ACYC_BOOL", "'N'" );
      lUsages.put( "ECYC_BOOL", "'Y'" );
      lUsages.put( "EOT_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STD_USAGE_DEF, lUsages ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkUsageErrorCode( testName.getMethodName(), "CFGUSG-12006" );
   }


   /**
    * This test is to verify valid error code CFGUSG-12007:C_STD_USAGE_DEF.aot_bool must be Y or N.
    *
    *
    */

   @Test
   public void test_CFGUSG_12007() {
      System.out.println( "=======Starting: " + testName.getMethodName()
            + " Validation========================" );

      // C_STD_USAGE_DEF table
      Map<String, String> lUsages = new LinkedHashMap<>();

      lUsages.put( "ASSMBL_CD", "\'" + iAssmblCD_ENG + "\'" );
      lUsages.put( "IPC_REF_CD", "\'" + iAssmblCD_ENG + "\'" );
      lUsages.put( "HRS_BOOL", "'N'" );
      lUsages.put( "CYC_BOOL", "'N'" );
      lUsages.put( "AOT_BOOL", "'X'" );
      lUsages.put( "ACYC_BOOL", "'N'" );
      lUsages.put( "ECYC_BOOL", "'Y'" );
      lUsages.put( "EOT_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STD_USAGE_DEF, lUsages ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkUsageErrorCode( testName.getMethodName(), "CFGUSG-12007" );
   }


   /**
    * This test is to verify valid error code CFGUSG-12008:C_STD_USAGE_DEF.acyc_bool must be Y or N.
    *
    *
    */

   @Test
   public void test_CFGUSG_12008() {
      System.out.println( "=======Starting: " + testName.getMethodName()
            + " Validation========================" );

      // C_STD_USAGE_DEF table
      Map<String, String> lUsages = new LinkedHashMap<>();

      lUsages.put( "ASSMBL_CD", "\'" + iAssmblCD_ENG + "\'" );
      lUsages.put( "IPC_REF_CD", "\'" + iAssmblCD_ENG + "\'" );
      lUsages.put( "HRS_BOOL", "'N'" );
      lUsages.put( "CYC_BOOL", "'N'" );
      lUsages.put( "AOT_BOOL", "'N'" );
      lUsages.put( "ACYC_BOOL", "'X'" );
      lUsages.put( "ECYC_BOOL", "'Y'" );
      lUsages.put( "EOT_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STD_USAGE_DEF, lUsages ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkUsageErrorCode( testName.getMethodName(), "CFGUSG-12008" );
   }


   /**
    * This test is to verify valid error code CFGUSG-12009:C_STD_USAGE_DEF.ecyc_bool must be Y or N.
    *
    *
    */

   @Test
   public void test_CFGUSG_12009() {
      System.out.println( "=======Starting: " + testName.getMethodName()
            + " Validation========================" );

      // C_STD_USAGE_DEF table
      Map<String, String> lUsages = new LinkedHashMap<>();

      lUsages.put( "ASSMBL_CD", "\'" + iAssmblCD_ENG + "\'" );
      lUsages.put( "IPC_REF_CD", "\'" + iAssmblCD_ENG + "\'" );
      lUsages.put( "HRS_BOOL", "'Y'" );
      lUsages.put( "CYC_BOOL", "'N'" );
      lUsages.put( "AOT_BOOL", "'N'" );
      lUsages.put( "ACYC_BOOL", "'N'" );
      lUsages.put( "ECYC_BOOL", "'X'" );
      lUsages.put( "EOT_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STD_USAGE_DEF, lUsages ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkUsageErrorCode( testName.getMethodName(), "CFGUSG-12009" );
   }


   /**
    * This test is to verify valid error code CFGUSG-12010:C_STD_USAGE_DEF.eot_bool must be Y or N.
    *
    *
    */

   @Test
   public void test_CFGUSG_12010() {
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
      lUsages.put( "EOT_BOOL", "'X'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STD_USAGE_DEF, lUsages ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkUsageErrorCode( testName.getMethodName(), "CFGUSG-12010" );
   }


   /**
    * This test is to verify valid error code CFGUSG-12011:When present,
    * C_PROC_USAGE_DEF.cust_calc_bool must be Y or N.
    *
    *
    */
   @Test
   public void test_CFGUSG_12011() {
      System.out.println( "=======Starting: " + testName.getMethodName()
            + " Validation========================" );

      // C_DYN_USAGE_DEF table
      Map<String, String> lUsages = new LinkedHashMap<>();

      lUsages.put( "ASSMBL_CD", "\'" + iAssmblCD_ENG + "\'" );
      lUsages.put( "IPC_REF_CD", "\'" + iAssmblCD_ENG + "\'" );
      lUsages.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_ECYC + "\'" );
      lUsages.put( "CUST_CALC_BOOL", "'X'" );
      lUsages.put( "EQN_LDESC", null );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_DYN_USAGE_DEF, lUsages ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkUsageErrorCode( testName.getMethodName(), "CFGUSG-12011" );

   }


   /**
    * This test is to verify valid error code CFGUSG-12012:When C_PROC_USAGE_DEF.cust_calc_bool is
    * Y, a C_PROC_USAGE_DEF.eqn_ldesc value must be specified.
    *
    *
    */
   @Test
   public void test_CFGUSG_12012() {
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
      // lUsages.put( "EQN_LDESC", "'AUTOTEST'" );
      lUsages.put( "EQN_LDESC", null );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_DYN_USAGE_DEF, lUsages ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkUsageErrorCode( testName.getMethodName(), "CFGUSG-12012" );

   }


   /**
    * This test is to verify valid error code CFGUSG-12013:C_PROC_USAGE_DEF.DATA_TYPE_CD is not
    * present in Maintenix MIM_DATA_TYPE table with a DOMAIN_TYPE_CD of usage (US).
    *
    *
    */
   @Test
   public void test_CFGUSG_12013() {
      System.out.println( "=======Starting: " + testName.getMethodName()
            + " Validation========================" );

      // C_DYN_USAGE_DEF table
      Map<String, String> lUsages = new LinkedHashMap<>();

      lUsages.put( "ASSMBL_CD", "\'" + iAssmblCD_ENG + "\'" );
      lUsages.put( "IPC_REF_CD", "\'" + iAssmblCD_ENG + "\'" );
      // lUsages.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_ECYC + "\'" );
      lUsages.put( "DATA_TYPE_CD", "'CDY'" );
      lUsages.put( "CUST_CALC_BOOL", "'N'" );
      lUsages.put( "EQN_LDESC", null );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_DYN_USAGE_DEF, lUsages ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkUsageErrorCode( testName.getMethodName(), "CFGUSG-12013" );

   }


   /**
    * This test is to verify valid error code CFGUSG-12014:When C_PROC_USAGE_DEF.DATA_TYPE_CD is not
    * present on the ROOT configure slot for this assembly in C_PROC_USAGE_DEF.
    *
    *
    */
   @Test
   public void test_CFGUSG_12014() {
      System.out.println( "=======Starting: " + testName.getMethodName()
            + " Validation========================" );

      // C_DYN_USAGE_DEF table
      Map<String, String> lUsages = new LinkedHashMap<>();

      // TRK slot
      lUsages.put( "ASSMBL_CD", "\'" + iAssmblCD_ACFT + "\'" );
      lUsages.put( "IPC_REF_CD", "\'" + iIPC_REF_CD_SUBASSY + "\'" );
      lUsages.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_ECYC + "\'" );
      lUsages.put( "CUST_CALC_BOOL", "'Y'" );
      lUsages.put( "EQN_LDESC", "'AUTOTEST'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_DYN_USAGE_DEF, lUsages ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkUsageErrorCode( testName.getMethodName(), "CFGUSG-12014" );

   }


   /**
    * This test is to verify valid error code CFGUSG-12015:C_PROC_USAGE_DEF.assmbl_cd does not exist
    * BULK data source record in Maintenix table EQP_DATA_SOURCE
    *
    *
    */
   @Test
   public void test_CFGUSG_12015() {
      System.out.println( "=======Starting: " + testName.getMethodName()
            + " Validation========================" );

      // C_DYN_USAGE_DEF table
      Map<String, String> lUsages = new LinkedHashMap<>();

      lUsages.put( "ASSMBL_CD", "'TSE'" );
      lUsages.put( "IPC_REF_CD", "'TSE'" );
      lUsages.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_ECYC + "\'" );
      lUsages.put( "CUST_CALC_BOOL", "'Y'" );
      lUsages.put( "EQN_LDESC", "'AUTOTEST'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_DYN_USAGE_DEF, lUsages ) );

      // TRK slot
      lUsages.clear();
      lUsages.put( "ASSMBL_CD", "'TSE'" );
      lUsages.put( "IPC_REF_CD", "'TSE'" );
      lUsages.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_ECYC + "\'" );
      lUsages.put( "CUST_CALC_BOOL", "'Y'" );
      lUsages.put( "EQN_LDESC", "'AUTOTEST'" );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkUsageErrorCode( testName.getMethodName(), "CFGUSG-12015" );

   }


   /**
    * This test is to verify valid error code CFGUSG-12016:C_PROC_USAGE_DEF.ASSMBL_CD cannot be
    * Common Hardware or ADMIN
    *
    */
   @Test
   public void test_CFGUSG_12016() {
      System.out.println( "=======Starting: " + testName.getMethodName()
            + " Validation========================" );

      // C_DYN_USAGE_DEF table
      Map<String, String> lUsages = new LinkedHashMap<>();

      lUsages.put( "ASSMBL_CD", "'COMHW'" );
      lUsages.put( "IPC_REF_CD", "'COMHW'" );
      lUsages.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_ECYC + "\'" );
      lUsages.put( "CUST_CALC_BOOL", "'Y'" );
      lUsages.put( "EQN_LDESC", "'AUTOTEST'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_DYN_USAGE_DEF, lUsages ) );

      // TRK slot
      lUsages.clear();
      lUsages.put( "ASSMBL_CD", "'COMHW'" );
      lUsages.put( "IPC_REF_CD", "'COMHW'" );
      lUsages.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_ECYC + "\'" );
      lUsages.put( "CUST_CALC_BOOL", "'Y'" );
      lUsages.put( "EQN_LDESC", "'AUTOTEST'" );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkUsageErrorCode( testName.getMethodName(), "CFGUSG-12016" );

   }


   /**
    * This test is to verify valid error code CFGUSG-17000:C_STD_USAGE_DEF record is invalid because
    * related C_PROC_USAGE_DEF record is invalid.
    *
    */

   @Test
   public void test_CFGUSG_17000() {
      System.out.println( "=======Starting: " + testName.getMethodName()
            + " Validation========================" );

      // C_STD_USAGE_DEF table
      Map<String, String> lUsages = new LinkedHashMap<>();

      lUsages.put( "ASSMBL_CD", "\'" + iAssmblCD_ENG + "\'" );
      // lUsages.put( "IPC_REF_CD", "\'" + iAssmblCD_ENG + "\'" );
      lUsages.put( "IPC_REF_CD", "\'" + iIPC_REF_CD_TRK + "\'" );
      lUsages.put( "HRS_BOOL", "'N'" );
      lUsages.put( "CYC_BOOL", "'N'" );
      lUsages.put( "AOT_BOOL", "'N'" );
      lUsages.put( "ACYC_BOOL", "'N'" );
      lUsages.put( "ECYC_BOOL", "'Y'" );
      lUsages.put( "EOT_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STD_USAGE_DEF, lUsages ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkUsageErrorCode( testName.getMethodName(), "CFGUSG-17000" );
   }


   /**
    * This test is to verify valid error code CFGUSG-17001:C_DYN_USAGE_DEF record is invalid because
    * related C_PROC_USAGE_DEF record is invalid
    *
    *
    */
   @Test
   public void test_CFGUSG_17001() {
      System.out.println( "=======Starting: " + testName.getMethodName()
            + " Validation========================" );

      // C_DYN_USAGE_DEF table
      Map<String, String> lUsages = new LinkedHashMap<>();

      lUsages.put( "ASSMBL_CD", "'TSE'" );
      lUsages.put( "IPC_REF_CD", "'TSE'" );
      lUsages.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_ECYC + "\'" );
      lUsages.put( "CUST_CALC_BOOL", "'Y'" );
      lUsages.put( "EQN_LDESC", "'AUTOTEST'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_DYN_USAGE_DEF, lUsages ) );

      // TRK slot
      lUsages.clear();
      lUsages.put( "ASSMBL_CD", "'TSE'" );
      lUsages.put( "IPC_REF_CD", "'TSE'" );
      lUsages.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_ECYC + "\'" );
      lUsages.put( "CUST_CALC_BOOL", "'Y'" );
      lUsages.put( "EQN_LDESC", "'AUTOTEST'" );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkUsageErrorCode( testName.getMethodName(), "CFGUSG-17001" );

   }


   /**
    * This test is to verify valid error code CFGUSG-20000:C_PROC_USAGE_DEF Assembly / IPC Reference
    * Code / Data Type already present in Maintenix MIM_PART_NUMDATA table.
    *
    */
   @Test
   public void test_CFGUSG_20000() {
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
      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkUsageErrorCode( testName.getMethodName(), "CFGUSG-20000" );

   }

}
