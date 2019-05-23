package com.mxi.mx.core.maint.plan.baselineloader.Vendors;

import java.sql.CallableStatement;
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

import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;
import com.mxi.mx.util.BaselineLoaderTest;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.ValidationAndImport;
import com.mxi.mx.util.WhereClause;


/**
 * This class contains common functions which support validation and import functionality of
 * c_vendor_exchg_import package.
 *
 * @author ALICIA QIAN
 * @edited Geoff Hyde
 */
public class Exchange extends BaselineLoaderTest {

   @Rule
   public TestName testName = new TestName();

   ValidationAndImport ivalidationandimport;

   public String iVENDOR_CD_1 = "10004";
   public String iPART_NO_OEM = "A0000010";
   public String iMANUFACT_CD = "11111";
   public String iVendor_Type_CD = "REPAIR";
   public String iCONTRACT_NUMBER = "CONTRACT001";

   public String iVENDOR_CD_2 = "10003";
   public String iPART_NO_OEM_2 = "A0000007";
   public String iMANUFACT_CD_2 = "11111";

   public String iVENDOR_CD_3 = "10005";
   public String iPART_NO_OEM_3 = "A0000017D";
   public String iMANUFACT_CD_3 = "10001";

   public String iVENDOR_CD_4 = "10002";
   public String iVendor_Type_CD_4 = "PURCHASE";
   public String iVendor_Type_CD_2 = "POOL";
   public String iVendor_Type_CD_3 = "BROKER";

   public simpleIDs iVENDOR_IDs = null;
   public simpleIDs iPNIDs = null;

   public String iErrorCdQuery =
         "select c_vendor_exchange.result_cd, dl_ref_message.tech_desc from "
               + TableUtil.C_VENDOR_EXCHANGE + " inner join dl_ref_message on "
               + " dl_ref_message.result_cd = c_vendor_exchange.result_cd";


   /**
    * Setup before executing each individual test
    *
    */
   @Before
   @Override
   public void before() throws Exception {

      super.before();
      clearBaselineLoaderTablesRegionD();
      iVENDOR_IDs = null;
      if ( testName.getMethodName().contains( "12013" ) )
         runUpdate( "update " + TableUtil.EQP_PART_VENDOR_EXCHG
               + " set pref_bool = 1 where part_no_vendor = '" + iPART_NO_OEM_2 + "'" );

   }


   /**
    * Clean up after each individual test
    */
   @After
   @Override
   public void after() {
      RestoreData();

      if ( testName.getMethodName().contains( "12013" ) ) {

         simpleIDs lPart = getIDs(
               "select part_no_db_id, part_no_id from eqp_part_no where part_no_oem = '"
                     + iPART_NO_OEM_2 + "' and manufact_cd = '" + iMANUFACT_CD_2 + "'",
               "part_no_db_id", "part_no_id" );
         simpleIDs lVendor =
               getIDs( "select vendor_db_id, vendor_id from org_vendor where vendor_cd = '"
                     + iVENDOR_CD_4 + "'", "vendor_db_id", "vendor_id" );
         runUpdate( "update " + TableUtil.EQP_PART_VENDOR_EXCHG
               + " set pref_bool = 0 where part_no_vendor = '" + iPART_NO_OEM_2
               + "' and (part_no_id != " + lPart.getNO_ID() + " or vendor_id != "
               + lVendor.getNO_ID() + ")" );
      }
      if ( testName.getMethodName().contains( "testNewPreferredVendor_IMPORT" ) ) {

         simpleIDs lPart = getIDs(
               "select part_no_db_id, part_no_id from eqp_part_no where part_no_oem = '"
                     + iPART_NO_OEM_2 + "' and manufact_cd = '" + iMANUFACT_CD_2 + "'",
               "part_no_db_id", "part_no_id" );
         simpleIDs lVendor =
               getIDs( "select vendor_db_id, vendor_id from org_vendor where vendor_cd = '"
                     + iVENDOR_CD_4 + "'", "vendor_db_id", "vendor_id" );
         runUpdate( "update " + TableUtil.EQP_PART_VENDOR_EXCHG
               + " set pref_bool = 1 where part_no_vendor = '" + iPART_NO_OEM_2
               + "' and (part_no_id = " + lPart.getNO_ID() + " and vendor_id = "
               + lVendor.getNO_ID() + ")" );
      }
      super.after();
   }


   /**
    * Create a vendor exchange agreement for a part with another vendor and set this one as the new
    * preferred vendor.
    *
    */
   public void testNewPreferredVendor_VALDIDATION() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_VENDOR_REPAIR table
      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_3 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM_2 + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD_2 + "'" );
      lVendors.put( "PREF_BOOL", "'Y'" );
      lVendors.put( "PART_NO_VENDOR", "'" + iVENDOR_CD_3 + "'" );
      lVendors.put( "CONTRACT_NUMBER", "'" + iCONTRACT_NUMBER + "'" );
      lVendors.put( "EXCHANGE_COST", "'100'" );
      lVendors.put( "LEAD_TIME", "'10'" );
      lVendors.put( "RETURN_TIME", "'20'" );
      lVendors.put( "BASE_PRICE", "'200'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_EXCHANGE, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to create vendor exchange part.
    *
    *
    */
   @Test
   public void testNewPreferredVendor_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testNewPreferredVendor_VALDIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // verify EQP_PART_VENDOR_EXCHG
      iPNIDs = getPNId( iPART_NO_OEM_2, iMANUFACT_CD_2 );
      iVENDOR_IDs = getVENDORID( iVENDOR_CD_3, iVendor_Type_CD_3 );
      verifyEQPPARTVENDORREP( iVENDOR_IDs, iPNIDs, "1", iCONTRACT_NUMBER, "10", "100", iVENDOR_CD_3,
            "20", "200", "APPROVED" );

      // checking prefer_bool on existing Purchase Agreement
      simpleIDs lPNIds = getPNId( iPART_NO_OEM_2, iMANUFACT_CD_2 );
      simpleIDs lVENDOR_IDs = getVENDORID( iVENDOR_CD_4, iVendor_Type_CD_4 );
      verifyEQPPARTVENDORREP( lVENDOR_IDs, lPNIds, "0", iCONTRACT_NUMBER, "10", "200",
            iPART_NO_OEM_2, "5", "400", "APPROVED" );
   }


   /**
    * Create a purchase agreement for a part with another vendor and set this one as the another
    * vendor, keep existing vendor as the preferred one.
    *
    */
   public void testNewVendorAgreement_VALIDATION() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_VENDOR_REPAIR table
      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_3 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM_2 + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD_2 + "'" );
      lVendors.put( "PREF_BOOL", "'N'" );
      lVendors.put( "PART_NO_VENDOR", "'" + iVENDOR_CD_3 + "'" );
      lVendors.put( "CONTRACT_NUMBER", "'" + iCONTRACT_NUMBER + "'" );
      lVendors.put( "EXCHANGE_COST", "'100'" );
      lVendors.put( "LEAD_TIME", "'10'" );
      lVendors.put( "RETURN_TIME", "'20'" );
      lVendors.put( "BASE_PRICE", "'200'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_EXCHANGE, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    *
    * Testing Import for testNewVendorAgreement_IMPORT and checking Prefer_bool of existing Purchase
    * Agreement
    */
   @Test
   public void testNewVendorAgreement_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testNewVendorAgreement_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // verify EQP_PART_VENDOR_EXCHG
      iPNIDs = getPNId( iPART_NO_OEM_2, iMANUFACT_CD_2 );
      iVENDOR_IDs = getVENDORID( iVENDOR_CD_3, iVendor_Type_CD_3 );
      verifyEQPPARTVENDORREP( iVENDOR_IDs, iPNIDs, "0", iCONTRACT_NUMBER, "10", "100", iVENDOR_CD_3,
            "20", "200", "APPROVED" );

      // checking prefer_bool on existing Purchase Agreement
      simpleIDs lPNIds = getPNId( iPART_NO_OEM_2, iMANUFACT_CD_2 );
      simpleIDs lVENDOR_IDs = getVENDORID( iVENDOR_CD_4, iVendor_Type_CD_4 );
      verifyEQPPARTVENDORREP( lVENDOR_IDs, lPNIds, "1", iCONTRACT_NUMBER, "10", "200",
            iPART_NO_OEM_2, "5", "400", "APPROVED" );
   }


   /**
    * This test is to create vendor exchange part
    *
    *
    */
   public void test_Vendor_exchange_validation() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_VENDOR_REPAIR table
      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD + "'" );
      lVendors.put( "PREF_BOOL", "'Y'" );
      lVendors.put( "PART_NO_VENDOR", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "CONTRACT_NUMBER", "'" + iCONTRACT_NUMBER + "'" );
      lVendors.put( "EXCHANGE_COST", "'100'" );
      lVendors.put( "LEAD_TIME", "'10'" );
      lVendors.put( "RETURN_TIME", "'20'" );
      lVendors.put( "BASE_PRICE", "'200'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_EXCHANGE, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to create vendor exchange part.
    *
    *
    */
   @Test
   public void test_Vendor_exchange_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      test_Vendor_exchange_validation();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // verify EQP_PART_VENDOR_EXCHG
      iPNIDs = getPNId( iPART_NO_OEM, iMANUFACT_CD );
      iVENDOR_IDs = getVENDORID( iVENDOR_CD_1, iVendor_Type_CD );
      verifyEQPPARTVENDORREP( iVENDOR_IDs, iPNIDs, "1", iCONTRACT_NUMBER, "10", "100", iVENDOR_CD_1,
            "20", "200", "APPROVED" );
   }


   /**
    *
    * test VENXCHG-10001 - VENDOR_CD is mandatory
    *
    */
   @Test
   public void testVENXCHG_10001() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_VENDOR_REPAIR table
      Map<String, String> lVendors = new LinkedHashMap<>();

      // lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD + "'" );
      lVendors.put( "PREF_BOOL", "'Y'" );
      lVendors.put( "PART_NO_VENDOR", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "CONTRACT_NUMBER", "'" + iCONTRACT_NUMBER + "'" );
      lVendors.put( "EXCHANGE_COST", "'100'" );
      lVendors.put( "LEAD_TIME", "'10'" );
      lVendors.put( "RETURN_TIME", "'20'" );
      lVendors.put( "BASE_PRICE", "'200'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_EXCHANGE, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENXCHG-10001" );

   }


   /**
    *
    * test VENXCHG-10002 - MANUFACT_CD is mandatory
    *
    */
   @Test
   public void testVENXCHG_10002() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_VENDOR_REPAIR table
      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      // lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD + "'" );
      lVendors.put( "PREF_BOOL", "'Y'" );
      lVendors.put( "PART_NO_VENDOR", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "CONTRACT_NUMBER", "'" + iCONTRACT_NUMBER + "'" );
      lVendors.put( "EXCHANGE_COST", "'100'" );
      lVendors.put( "LEAD_TIME", "'10'" );
      lVendors.put( "RETURN_TIME", "'20'" );
      lVendors.put( "BASE_PRICE", "'200'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_EXCHANGE, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENXCHG-10002" );

   }


   /**
    *
    * test VENXCHG-10003 - PART_NO_OEM is mandatory
    *
    */
   @Test
   public void testVENXCHG_10003() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_VENDOR_REPAIR table
      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      // lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD + "'" );
      lVendors.put( "PREF_BOOL", "'Y'" );
      lVendors.put( "PART_NO_VENDOR", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "CONTRACT_NUMBER", "'" + iCONTRACT_NUMBER + "'" );
      lVendors.put( "EXCHANGE_COST", "'100'" );
      lVendors.put( "LEAD_TIME", "'10'" );
      lVendors.put( "RETURN_TIME", "'20'" );
      lVendors.put( "BASE_PRICE", "'200'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_EXCHANGE, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENXCHG-10003" );

   }


   /**
    *
    * test VENXCHG-10005 - PREF_BOOL is mandatory
    *
    */
   @Test
   public void testVENXCHG_10005() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_VENDOR_REPAIR table
      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD + "'" );
      // lVendors.put( "PREF_BOOL", "'Y'" );
      lVendors.put( "PART_NO_VENDOR", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "CONTRACT_NUMBER", "'" + iCONTRACT_NUMBER + "'" );
      lVendors.put( "EXCHANGE_COST", "'100'" );
      lVendors.put( "LEAD_TIME", "'10'" );
      lVendors.put( "RETURN_TIME", "'20'" );
      lVendors.put( "BASE_PRICE", "'200'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_EXCHANGE, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENXCHG-10005" );

   }


   /**
    *
    * test VENXCHG-10008 - RETURN_TIME is mandatory
    *
    */
   @Test
   public void testVENXCHG_10008() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_VENDOR_REPAIR table
      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD + "'" );
      lVendors.put( "PREF_BOOL", "'Y'" );
      lVendors.put( "PART_NO_VENDOR", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "CONTRACT_NUMBER", "'" + iCONTRACT_NUMBER + "'" );
      lVendors.put( "EXCHANGE_COST", "'100'" );
      lVendors.put( "LEAD_TIME", "'10'" );
      lVendors.put( "RETURN_TIME", "null" ); // <<<<<<
      lVendors.put( "BASE_PRICE", "'200'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_EXCHANGE, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENXCHG-10008" );

   }


   /**
    *
    * test VENXCHG-10009 - LEAD_TIME is mandatory
    *
    */
   @Test
   public void testVENXCHG_10009() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_VENDOR_REPAIR table
      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD + "'" );
      lVendors.put( "PREF_BOOL", "'Y'" );
      lVendors.put( "PART_NO_VENDOR", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "CONTRACT_NUMBER", "'" + iCONTRACT_NUMBER + "'" );
      lVendors.put( "EXCHANGE_COST", "'100'" );
      lVendors.put( "LEAD_TIME", "null" ); // <<<<
      lVendors.put( "RETURN_TIME", "'20'" );
      lVendors.put( "BASE_PRICE", "'200'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_EXCHANGE, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENXCHG-10009" );

   }


   /**
    *
    * test VENXCHG-11001 - C_VENDOR_EXCHANGE.VENDOR_CODE/MANUFACT_CD/PART_NO_OEM must be unique in
    * the staging table
    *
    */
   @Test
   public void testVENXCHG_11001() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_VENDOR_REPAIR table
      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD + "'" );
      lVendors.put( "PREF_BOOL", "'Y'" );
      lVendors.put( "PART_NO_VENDOR", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "CONTRACT_NUMBER", "'" + iCONTRACT_NUMBER + "'" );
      lVendors.put( "EXCHANGE_COST", "'100'" );
      lVendors.put( "LEAD_TIME", "'10'" );
      lVendors.put( "RETURN_TIME", "'20'" );
      lVendors.put( "BASE_PRICE", "'200'" );

      // insert map #1
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_EXCHANGE, lVendors ) );
      // insert map #2
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_EXCHANGE, lVendors ) );
      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENXCHG-11001" );

   }


   /**
    *
    * test VENXCHG-12001 - VENDOR_CD must exist in Maintenix
    *
    */
   @Test
   public void testVENXCHG_12001() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_VENDOR_REPAIR table
      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'Invalid'" ); // <<<<<<<<<<<<<
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD + "'" );
      lVendors.put( "PREF_BOOL", "'Y'" );
      lVendors.put( "PART_NO_VENDOR", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "CONTRACT_NUMBER", "'" + iCONTRACT_NUMBER + "'" );
      lVendors.put( "EXCHANGE_COST", "'100'" );
      lVendors.put( "LEAD_TIME", "'10'" );
      lVendors.put( "RETURN_TIME", "'20'" );
      lVendors.put( "BASE_PRICE", "'200'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_EXCHANGE, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENXCHG-12001" );

   }


   /**
    *
    * test VENXCHG-12002 - MANUFACT_CD is mandatory
    *
    */
   @Test
   public void testVENXCHG_12002() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_VENDOR_REPAIR table
      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lVendors.put( "MANUFACT_CD", "'Invalid'" ); // <<<<<<<<<
      lVendors.put( "PREF_BOOL", "'Y'" );
      lVendors.put( "PART_NO_VENDOR", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "CONTRACT_NUMBER", "'" + iCONTRACT_NUMBER + "'" );
      lVendors.put( "EXCHANGE_COST", "'100'" );
      lVendors.put( "LEAD_TIME", "'10'" );
      lVendors.put( "RETURN_TIME", "'20'" );
      lVendors.put( "BASE_PRICE", "'200'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_EXCHANGE, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENXCHG-12002" );

   }


   /**
    *
    * test VENXCHG-12003 - PART_NO_OEM is mandatory
    *
    */
   @Test
   public void testVENXCHG_12003() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_VENDOR_REPAIR table
      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'Invalid'" ); // <<<<<<<<<<<<<<<<
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD + "'" );
      lVendors.put( "PREF_BOOL", "'Y'" );
      lVendors.put( "PART_NO_VENDOR", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "CONTRACT_NUMBER", "'" + iCONTRACT_NUMBER + "'" );
      lVendors.put( "EXCHANGE_COST", "'100'" );
      lVendors.put( "LEAD_TIME", "'10'" );
      lVendors.put( "RETURN_TIME", "'20'" );
      lVendors.put( "BASE_PRICE", "'200'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_EXCHANGE, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENXCHG-12003" );

   }


   /**
    *
    * test VENXCHG-12005 -The combination of C_VENDOR_EXCHANGE.MANUFACT_CD/PART_NO_OEM must exist in
    * Maintenix
    *
    */
   @Test
   public void testVENXCHG_12005() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_VENDOR_REPAIR table
      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD_3 + "'" );
      lVendors.put( "PREF_BOOL", "'Y'" );
      lVendors.put( "PART_NO_VENDOR", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "CONTRACT_NUMBER", "'" + iCONTRACT_NUMBER + "'" );
      lVendors.put( "EXCHANGE_COST", "'100'" );
      lVendors.put( "LEAD_TIME", "'10'" );
      lVendors.put( "RETURN_TIME", "'20'" );
      lVendors.put( "BASE_PRICE", "'200'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_EXCHANGE, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENXCHG-12005" );

   }


   /**
    *
    * test VENXCHG-12006 - A single Part Number OEM and Manufacturer Code combination cannot be
    * preferred to more than one Vendor in staging area
    *
    */
   @Test
   public void testVENXCHG_12006() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_VENDOR_REPAIR table
      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD + "'" );
      lVendors.put( "PREF_BOOL", "'Y'" );
      lVendors.put( "PART_NO_VENDOR", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "CONTRACT_NUMBER", "'" + iCONTRACT_NUMBER + "'" );
      lVendors.put( "EXCHANGE_COST", "'100'" );
      lVendors.put( "LEAD_TIME", "'10'" );
      lVendors.put( "RETURN_TIME", "'20'" );
      lVendors.put( "BASE_PRICE", "'200'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_EXCHANGE, lVendors ) );
      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_3 + "'" );
      // insert map insert the same record with different vendor <<<<<<<<<<<<<<<
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_EXCHANGE, lVendors ) );
      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENXCHG-12006" );

   }


   /**
    *
    * test VENXCHG-12007 - There must be a preferred Vendor for the PART_NO_OEM/MANUFACT_CD
    * combination in staging area
    *
    */
   @Test
   public void testVENXCHG_12007() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_VENDOR_REPAIR table
      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD + "'" );
      lVendors.put( "PREF_BOOL", "'N'" ); // <<<<<<
      lVendors.put( "PART_NO_VENDOR", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "CONTRACT_NUMBER", "'" + iCONTRACT_NUMBER + "'" );
      lVendors.put( "EXCHANGE_COST", "'100'" );
      lVendors.put( "LEAD_TIME", "'10'" );
      lVendors.put( "RETURN_TIME", "'20'" );
      lVendors.put( "BASE_PRICE", "'200'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_EXCHANGE, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENXCHG-12007" );

   }


   /**
    *
    * test VENXCHG-12008 - C_VENDOR_EXCHANGE.PREF_BOOL has to have value of either Y or N
    *
    */
   @Test
   public void testVENXCHG_12008() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_VENDOR_REPAIR table
      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD + "'" );
      lVendors.put( "PREF_BOOL", "'K'" ); // <<<<<<<<<<<<
      lVendors.put( "PART_NO_VENDOR", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "CONTRACT_NUMBER", "'" + iCONTRACT_NUMBER + "'" );
      lVendors.put( "EXCHANGE_COST", "'100'" );
      lVendors.put( "LEAD_TIME", "'10'" );
      lVendors.put( "RETURN_TIME", "'20'" );
      lVendors.put( "BASE_PRICE", "'200'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_EXCHANGE, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENXCHG-12008" );

   }


   /**
    *
    * test VENXCHG-12009 - EXCHANGE_COST must be greater than or equal to 0 when provided
    *
    */
   @Test
   public void testVENXCHG_12009() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_VENDOR_REPAIR table
      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD + "'" );
      lVendors.put( "PREF_BOOL", "'Y'" );
      lVendors.put( "PART_NO_VENDOR", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "CONTRACT_NUMBER", "'" + iCONTRACT_NUMBER + "'" );
      lVendors.put( "EXCHANGE_COST", "'-1'" ); // <<<<<<<<<<<
      lVendors.put( "LEAD_TIME", "'10'" );
      lVendors.put( "RETURN_TIME", "'20'" );
      lVendors.put( "BASE_PRICE", "'200'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_EXCHANGE, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENXCHG-12009" );

   }


   /**
    *
    * test VENXCHG-12010 - RETURN_TIME must be greater than or equal to 0
    *
    */
   @Test
   public void testVENXCHG_12010() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_VENDOR_REPAIR table
      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD + "'" );
      lVendors.put( "PREF_BOOL", "'Y'" );
      lVendors.put( "PART_NO_VENDOR", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "CONTRACT_NUMBER", "'" + iCONTRACT_NUMBER + "'" );
      lVendors.put( "EXCHANGE_COST", "'100'" );
      lVendors.put( "LEAD_TIME", "'10'" );
      lVendors.put( "RETURN_TIME", "'-1'" ); // <<<<<<<<<<
      lVendors.put( "BASE_PRICE", "'200'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_EXCHANGE, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENXCHG-12010" );

   }


   /**
    *
    * test VENXCHG-12011 - LEAD_TIME must be greater than or equal to 0
    *
    */
   @Test
   public void testVENXCHG_12011() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_VENDOR_REPAIR table
      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD + "'" );
      lVendors.put( "PREF_BOOL", "'Y'" );
      lVendors.put( "PART_NO_VENDOR", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "CONTRACT_NUMBER", "'" + iCONTRACT_NUMBER + "'" );
      lVendors.put( "EXCHANGE_COST", "'100'" );
      lVendors.put( "LEAD_TIME", "'-1'" ); // <<<<<<<<<<<
      lVendors.put( "RETURN_TIME", "'20'" );
      lVendors.put( "BASE_PRICE", "'200'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_EXCHANGE, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENXCHG-12011" );

   }


   /**
    *
    * test VENXCHG-12012 - BASE_PRICE must be greater than or equal to 0 when provided
    *
    */
   @Test
   public void testVENXCHG_12012() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_VENDOR_REPAIR table
      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD + "'" );
      lVendors.put( "PREF_BOOL", "'Y'" );
      lVendors.put( "PART_NO_VENDOR", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "CONTRACT_NUMBER", "'" + iCONTRACT_NUMBER + "'" );
      lVendors.put( "EXCHANGE_COST", "'100'" );
      lVendors.put( "LEAD_TIME", "'10'" );
      lVendors.put( "RETURN_TIME", "'20'" );
      lVendors.put( "BASE_PRICE", "'-0.1'" ); // <<<<

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_EXCHANGE, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENXCHG-12012" );

   }


   /**
    *
    * test VENXCHG-12013 - This part already has more than one preferred vendor in
    * eqp_part_vendor.pref_bool. Note: When more than one preferred Vendor exists in Maintenix, One
    * of those vendors must be set as non-preferred before any other data can be loaded for that
    * part
    */
   @Test
   public void testVENXCHG_12013() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_VENDOR_REPAIR table
      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_3 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM_2 + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD_2 + "'" );
      lVendors.put( "PREF_BOOL", "'N'" );
      lVendors.put( "CONTRACT_NUMBER", "'" + iCONTRACT_NUMBER + "'" );
      lVendors.put( "EXCHANGE_COST", "'100'" );
      lVendors.put( "LEAD_TIME", "'10'" );
      lVendors.put( "RETURN_TIME", "'20'" );
      lVendors.put( "BASE_PRICE", "'200'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_EXCHANGE, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENXCHG-12013" );

   }


   /**
    *
    * test VENXCHG-12014 - The part number must be procurable in order to be added to the exchange
    * agreement list
    *
    */
   @Test
   public void testVENXCHG_12014() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_VENDOR_REPAIR table
      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + "A0000001" + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD_3 + "'" );
      lVendors.put( "PREF_BOOL", "'Y'" );
      lVendors.put( "PART_NO_VENDOR", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "CONTRACT_NUMBER", "'" + iCONTRACT_NUMBER + "'" );
      lVendors.put( "EXCHANGE_COST", "'100'" );
      lVendors.put( "LEAD_TIME", "'10'" );
      lVendors.put( "RETURN_TIME", "'20'" );
      lVendors.put( "BASE_PRICE", "'200'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_EXCHANGE, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENXCHG-12014" );

   }


   /**
    *
    * test VENXCHG-12015 - There must be a preferred Vendor for the part_no_oem/manufact_cd
    * combination in staging area. Note: Forces the user to specify preferred vendor in staging if
    * they are removing the flag from the currently preferred one
    *
    */
   @Test
   public void testVENXCHG_12015() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_VENDOR_REPAIR table
      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_4 + "'" ); // <<<<<<<<<<<
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM_2 + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD_2 + "'" );
      lVendors.put( "PREF_BOOL", "'N'" ); // <<<<<<<<<
      lVendors.put( "PART_NO_VENDOR", "'" + iPART_NO_OEM_2 + "'" );
      lVendors.put( "CONTRACT_NUMBER", "'" + iCONTRACT_NUMBER + "'" );
      lVendors.put( "EXCHANGE_COST", "'100'" );
      lVendors.put( "LEAD_TIME", "'10'" );
      lVendors.put( "RETURN_TIME", "'20'" );
      lVendors.put( "BASE_PRICE", "'200'" );

      // insert map #1
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_EXCHANGE, lVendors ) );

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_3 + "'" ); // <<<<<<<<<<
      // insert map #2
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_EXCHANGE, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENXCHG-12015" );

   }


   // =====================================================
   /**
    * This function is to verify EQP_PART_VENDOR_EXCHG table
    *
    *
    */
   public void verifyEQPPARTVENDORREP( simpleIDs aVENDORIDs, simpleIDs aPNIDs, String aPREF_BOOL,
         String aCONTRACT_NUMBER, String aLEAD_TIME, String aEXCHANGE_COST, String aPART_NO_VENDOR,
         String aRETURN_TIME, String aBasePrice, String aVendorStatusCd ) {

      String[] iIds = { "PREF_BOOL", "CONTRACT_NUMBER", "LEAD_TIME", "EXCHANGE_COST",
            "PART_NO_VENDOR", "RETURN_TIME", "BASE_PRICE", "VENDOR_STATUS_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "VENDOR_DB_ID", aVENDORIDs.getNO_DB_ID() );
      lArgs.addArguments( "VENDOR_ID", aVENDORIDs.getNO_ID() );
      lArgs.addArguments( "PART_NO_DB_ID", aPNIDs.getNO_DB_ID() );
      lArgs.addArguments( "PART_NO_ID", aPNIDs.getNO_ID() );

      String iQueryString =
            TableUtil.buildTableQuery( TableUtil.EQP_PART_VENDOR_EXCHG, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "PREF_BOOL", llists.get( 0 ).get( 0 ).equalsIgnoreCase( aPREF_BOOL ) );
      Assert.assertTrue( "CONTRACT_NUMBER",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( aCONTRACT_NUMBER ) );
      Assert.assertTrue( "LEAD_TIME", llists.get( 0 ).get( 2 ).equalsIgnoreCase( aLEAD_TIME ) );
      Assert.assertTrue( "EXCHANGE_COST",
            llists.get( 0 ).get( 3 ).equalsIgnoreCase( aEXCHANGE_COST ) );
      Assert.assertTrue( "PART_NO_VENDOR",
            llists.get( 0 ).get( 4 ).equalsIgnoreCase( aPART_NO_VENDOR ) );
      Assert.assertTrue( "RETURN_TIME", llists.get( 0 ).get( 5 ).equalsIgnoreCase( aRETURN_TIME ) );
      Assert.assertTrue( "BASE_PRICE", llists.get( 0 ).get( 6 ).equalsIgnoreCase( aBasePrice ) );
      Assert.assertTrue( "VENDOR_STATUS_CD",
            llists.get( 0 ).get( 7 ).equalsIgnoreCase( aVendorStatusCd ) );
   }


   /**
    * This function is to get vendor ids table
    *
    *
    */
   public simpleIDs getVENDORID( String aVENDOR_CD, String aVENDOR_TYPE_CD ) {

      String[] iIds = { "VENDOR_DB_ID", "VENDOR_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "VENDOR_CD", aVENDOR_CD );
      lArgs.addArguments( "VENDOR_TYPE_CD", aVENDOR_TYPE_CD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.ORG_VENDOR, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      simpleIDs lids = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lids;

   }


   /**
    * This function is called by after test method to restore data which has been modified by tests.
    *
    *
    */
   public void RestoreData() {

      if ( iVENDOR_IDs != null ) {

         // delete EQP_PART_VENDOR_REP
         String lStrDelete = "delete from " + TableUtil.EQP_PART_VENDOR_EXCHG
               + "  where VENDOR_DB_ID=" + iVENDOR_IDs.getNO_DB_ID() + " and VENDOR_ID="
               + iVENDOR_IDs.getNO_ID() + " and PART_NO_DB_ID=" + iPNIDs.getNO_DB_ID()
               + " and PART_NO_ID = " + iPNIDs.getNO_ID();
         executeSQL( lStrDelete );

      }

   }


   /**
    * This function is to implement interface ValidationAndImport
    *
    * @param: blnOnlyValidation
    * @param: allornone
    *
    */
   public int runValidationAndImport( boolean blnOnlyValidation, boolean allornone ) {
      int rtValue = 0;
      ivalidationandimport = new ValidationAndImport() {

         @Override
         public int runValidation( boolean allornone ) {
            int lReturn = 0;

            try {
               CallableStatement lPrepareCallInventory = getConnection().prepareCall(
                     "BEGIN  c_vendor_exchg_import.validate_vendor_exchange(on_retcode => ?); END;" );

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
               CallableStatement lPrepareCallInventory = getConnection().prepareCall(
                     "BEGIN c_vendor_exchg_import.import_vendor_exchange(on_retcode => ?); END;" );

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

      rtValue = blnOnlyValidation ? ivalidationandimport.runValidation( allornone )
            : ivalidationandimport.runImport( allornone );

      return rtValue;
   }

}
