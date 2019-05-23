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
 * c_vendor_purchase_import package.
 *
 * @author ALICIA QIAN
 */
public class Purchase extends BaselineLoaderTest {

   @Rule
   public TestName testName = new TestName();

   ValidationAndImport ivalidationandimport;

   public String iVENDOR_CD_1 = "10002";
   public String iPART_NO_OEM = "A0000010";
   public String iMANUFACT_CD = "11111";

   public String iVENDOR_CD_2 = "10003";
   public String iPART_NO_OEM_2 = "A0000007";
   public String iMANUFACT_CD_2 = "11111";

   public String iVENDOR_CD_3 = "10005";
   public String iPART_NO_OEM_3 = "A0000017D";
   public String iMANUFACT_CD_3 = "10001";

   public String iVendor_Type_CD = "PURCHASE";
   public String iVendor_Type_CD_2 = "POOL";
   public String iVendor_Type_CD_3 = "BROKER";

   public simpleIDs iVENDOR_IDs = null;
   public simpleIDs iPNIds = null; // part_no_ids

   public String iErrorCdQuery =
         "select c_vendor_purchase.result_cd, dl_ref_message.tech_desc from "
               + TableUtil.C_VENDOR_PURCHASE + " inner join dl_ref_message on "
               + " dl_ref_message.result_cd = c_vendor_purchase.result_cd";


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

      if ( testName.getMethodName().contains( "12008" ) )
         runUpdate( "update eqp_part_vendor set pref_bool = 1 where part_no_vendor = '"
               + iPART_NO_OEM_2 + "'" );
   }


   /**
    * Clean up after each individual test
    */
   @After
   @Override
   public void after() {
      RestoreData();
      if ( testName.getMethodName().contains( "12008" ) ) {

         simpleIDs lPart = getIDs(
               "select part_no_db_id, part_no_id from eqp_part_no where part_no_oem = '"
                     + iPART_NO_OEM_2 + "' and manufact_cd = '" + iMANUFACT_CD_2 + "'",
               "part_no_db_id", "part_no_id" );
         simpleIDs lVendor =
               getIDs( "select vendor_db_id, vendor_id from org_vendor where vendor_cd = '"
                     + iVENDOR_CD_1 + "'", "vendor_db_id", "vendor_id" );
         runUpdate( "update eqp_part_vendor set pref_bool = 0 where part_no_vendor = '"
               + iPART_NO_OEM_2 + "' and (part_no_id != " + lPart.getNO_ID() + " or vendor_id != "
               + lVendor.getNO_ID() + ")" );
      }
      if ( testName.getMethodName().contains( "testNewPreferredVendor_IMPORT" ) ) {

         simpleIDs lPart = getIDs(
               "select part_no_db_id, part_no_id from eqp_part_no where part_no_oem = '"
                     + iPART_NO_OEM_2 + "' and manufact_cd = '" + iMANUFACT_CD_2 + "'",
               "part_no_db_id", "part_no_id" );
         simpleIDs lVendor =
               getIDs( "select vendor_db_id, vendor_id from org_vendor where vendor_cd = '"
                     + iVENDOR_CD_1 + "'", "vendor_db_id", "vendor_id" );
         runUpdate( "update eqp_part_vendor set pref_bool = 1 where part_no_vendor = '"
               + iPART_NO_OEM_2 + "' and (part_no_id = " + lPart.getNO_ID() + " and vendor_id = "
               + lVendor.getNO_ID() + ")" );
      }
      super.after();
   }


   /**
    * Create a purchase agreement for a part with another vendor and set this one as the new
    * preferred vendor.
    *
    */
   public void testNewPreferredVendor_VALDIDATION() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_VENDOR_PURCHASE table
      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_3 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM_2 + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD_2 + "'" );
      lVendors.put( "PREF_BOOL", "'Y'" );
      lVendors.put( "UNIT_PRICE", "'300'" );
      lVendors.put( "QTY_UNIT_CD", "'EA'" );
      lVendors.put( "MIN_ORDER_QT", "'3'" );
      lVendors.put( "LEAD_TIME", "'10'" );
      lVendors.put( "EFFECTIVE_FROM_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_PURCHASE, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    *
    * Testing Import for testNewPreferredVendor_IMPORT and checking Prefer_bool of existing Purchase
    * Agreement
    */
   @Test
   public void testNewPreferredVendor_IMPORT() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testNewPreferredVendor_VALDIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // verify eqp_part_vendor
      iPNIds = getPNId( iPART_NO_OEM_2, iMANUFACT_CD_2 );
      iVENDOR_IDs = getVENDORID( iVENDOR_CD_3, iVendor_Type_CD_3 );
      verifyEQPPARTVENDOR( iVENDOR_IDs, iPNIds, "1", "APPROVED" );

      // verify EQP_PART_VENDOR_PRICE
      verifyEQPPARTVENDORPRICE( iVENDOR_IDs, iPNIds, "300", "EA", "10", "3", "" );

      // checking prefer_bool on existing Purchase Agreement
      simpleIDs lPNIds = getPNId( iPART_NO_OEM_2, iMANUFACT_CD_2 );
      simpleIDs lVENDOR_IDs = getVENDORID( iVENDOR_CD_1, iVendor_Type_CD );
      verifyEQPPARTVENDOR( lVENDOR_IDs, lPNIds, "0", "APPROVED" );
   }


   /**
    * Create a purchase agreement for a part with another vendor and set this one as the another
    * vendor, keep existing vendor as the preferred one.
    *
    */
   public void testNewVendorAgreement_VALIDATION() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_VENDOR_PURCHASE table
      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_2 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM_2 + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD_2 + "'" );
      lVendors.put( "PREF_BOOL", "'N'" );
      lVendors.put( "UNIT_PRICE", "'700'" );
      lVendors.put( "QTY_UNIT_CD", "'EA'" );
      lVendors.put( "MIN_ORDER_QT", "'5'" );
      lVendors.put( "LEAD_TIME", "'100'" );
      lVendors.put( "EFFECTIVE_FROM_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_PURCHASE, lVendors ) );

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

      // verify eqp_part_vendor
      iPNIds = getPNId( iPART_NO_OEM_2, iMANUFACT_CD_2 );
      iVENDOR_IDs = getVENDORID( iVENDOR_CD_2, iVendor_Type_CD_2 );
      verifyEQPPARTVENDOR( iVENDOR_IDs, iPNIds, "0", "APPROVED" );

      // verify EQP_PART_VENDOR_PRICE
      verifyEQPPARTVENDORPRICE( iVENDOR_IDs, iPNIds, "700", "EA", "100", "5", "" );

      // checking prefer_bool on existing Purchase Agreement
      simpleIDs lPNIds = getPNId( iPART_NO_OEM_2, iMANUFACT_CD_2 );
      simpleIDs lVENDOR_IDs = getVENDORID( iVENDOR_CD_1, iVendor_Type_CD );
      verifyEQPPARTVENDOR( lVENDOR_IDs, lPNIds, "1", "APPROVED" );

   }


   /**
    * This test is to create vendor purchase
    *
    *
    */
   public void test_Vendor_purchase_validation() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_VENDOR_PURCHASE table
      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD + "'" );
      lVendors.put( "PREF_BOOL", "'Y'" );
      lVendors.put( "DISCOUNT_PCT", "'0.1'" );
      lVendors.put( "UNIT_PRICE", "'200'" );
      lVendors.put( "QTY_UNIT_CD", "'EA'" );
      lVendors.put( "MIN_ORDER_QT", "'3'" );
      lVendors.put( "LEAD_TIME", "'10'" );
      lVendors.put( "EFFECTIVE_FROM_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_PURCHASE, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to create vendor purchase
    *
    *
    */
   @Test
   public void test_Vendor_purchase_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      test_Vendor_purchase_validation();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // verify eqp_part_vendor
      iPNIds = getPNId( iPART_NO_OEM, iMANUFACT_CD );
      iVENDOR_IDs = getVENDORID( iVENDOR_CD_1, iVendor_Type_CD );
      verifyEQPPARTVENDOR( iVENDOR_IDs, iPNIds, "1", "APPROVED" );

      // verify EQP_PART_VENDOR_PRICE
      verifyEQPPARTVENDORPRICE( iVENDOR_IDs, iPNIds, "200", "EA", "10", "3", "0.1" );

   }


   /**
    *
    * Confirm that there is no obsolete Purchase Agreements (Hist_bool = 1) in the export for
    * OPER-31039
    *
    */
   @Test
   public void testExportHasNoObsoleteAggreements() {
      // Run valdiation and import
      test_Vendor_purchase_IMPORT();

      // Set record to obsolete
      String lQuery = "Update " + TableUtil.EQP_PART_VENDOR_PRICE
            + " set hist_bool = 1 where VENDOR_DB_ID =" + iVENDOR_IDs.getNO_DB_ID()
            + " and VENDOR_ID = " + iVENDOR_IDs.getNO_ID() + " and PART_NO_DB_ID = "
            + iPNIds.getNO_DB_ID() + " and PART_NO_ID = " + iPNIds.getNO_ID();

      // Run Export - Note export will automatically overwrite whatever, records are in
      runExport();

      int lStagingCount = countRowsInEntireTable( TableUtil.C_VENDOR_PURCHASE );
      lQuery = "select count(*) from EQP_PART_VENDOR_PRICE where hist_Bool = '0'";
      int MxTblCount = countRowsOfQuery( lQuery );

      Assert.assertTrue( "The number of expected Purchase Aggreements expected is inccorrect",
            lStagingCount == MxTblCount );

   }


   /**
    *
    * test VENDPARTPURCHAGMT-10001 - The Vendor Code is mandatory
    *
    */
   @Test
   public void test_VENDPARTPURCHAGMT_10001() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_VENDOR_PURCHASE table
      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "null" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD + "'" );
      lVendors.put( "PREF_BOOL", "'1'" ); // <<<<
      lVendors.put( "UNIT_PRICE", "'200'" );
      lVendors.put( "QTY_UNIT_CD", "'EA'" );
      lVendors.put( "MIN_ORDER_QT", "'3'" );
      lVendors.put( "LEAD_TIME", "'10'" );
      lVendors.put( "EFFECTIVE_FROM_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_PURCHASE, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENDPARTPURCHAGMT-10001" );

   }


   /**
    *
    * test VENDPARTPURCHAGMT-10002 - The Manufacturer Code is mandatory
    *
    */
   @Test
   public void test_VENDPARTPURCHAGMT_10002() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_VENDOR_PURCHASE table
      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lVendors.put( "MANUFACT_CD", "null" );
      lVendors.put( "PREF_BOOL", "'Y'" );
      lVendors.put( "UNIT_PRICE", "'200'" );
      lVendors.put( "QTY_UNIT_CD", "'EA'" );
      lVendors.put( "MIN_ORDER_QT", "'3'" );
      lVendors.put( "LEAD_TIME", "'10'" );
      lVendors.put( "EFFECTIVE_FROM_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_PURCHASE, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENDPARTPURCHAGMT-10002" );

   }


   /**
    *
    * test VENDPARTPURCHAGMT-10003 - PART_NO_OEM cannot be null or consist entirely of spaces
    *
    */
   @Test
   public void test_VENDPARTPURCHAGMT_10003() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_VENDOR_PURCHASE table
      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "null" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD + "'" );
      lVendors.put( "PREF_BOOL", "'Y'" );
      lVendors.put( "UNIT_PRICE", "'200'" );
      lVendors.put( "QTY_UNIT_CD", "'EA'" );
      lVendors.put( "MIN_ORDER_QT", "'3'" );
      lVendors.put( "LEAD_TIME", "'10'" );
      lVendors.put( "EFFECTIVE_FROM_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_PURCHASE, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENDPARTPURCHAGMT-10003" );

   }


   /**
    *
    * test VENDPARTPURCHAGMT-10004 - PREFER_BOOL cannot be null
    *
    */
   @Test
   public void test_VENDPARTPURCHAGMT_10004() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD + "'" );
      lVendors.put( "PREF_BOOL", "null" ); // <<<<
      lVendors.put( "UNIT_PRICE", "'200'" );
      lVendors.put( "QTY_UNIT_CD", "'EA'" );
      lVendors.put( "MIN_ORDER_QT", "'3'" );
      lVendors.put( "LEAD_TIME", "'10'" );
      lVendors.put( "EFFECTIVE_FROM_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_PURCHASE, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENDPARTPURCHAGMT-10004" );

   }


   /**
    *
    * test VENDPARTPURCHAGMT-12001 - The Vendor Code must exist in Maintenix
    *
    */
   @Test
   public void test_VENDPARTPURCHAGMT_12001() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'Invalid'" ); // <<<
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD + "'" );
      lVendors.put( "PREF_BOOL", "'Y'" );
      lVendors.put( "UNIT_PRICE", "'200'" );
      lVendors.put( "QTY_UNIT_CD", "'EA'" );
      lVendors.put( "MIN_ORDER_QT", "'3'" );
      lVendors.put( "LEAD_TIME", "'10'" );
      lVendors.put( "EFFECTIVE_FROM_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_PURCHASE, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENDPARTPURCHAGMT-12001" );

   }


   /**
    *
    * test VENDPARTPURCHAGMT-12002 - The Manufacturer Code must exist in Maintenix
    *
    */
   @Test
   public void test_VENDPARTPURCHAGMT_12002() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lVendors.put( "MANUFACT_CD", "'Invalid'" ); // <<<
      lVendors.put( "PREF_BOOL", "'Y'" );
      lVendors.put( "UNIT_PRICE", "'200'" );
      lVendors.put( "QTY_UNIT_CD", "'EA'" );
      lVendors.put( "MIN_ORDER_QT", "'3'" );
      lVendors.put( "LEAD_TIME", "'10'" );
      lVendors.put( "EFFECTIVE_FROM_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_PURCHASE, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENDPARTPURCHAGMT-12002" );

   }


   /**
    *
    * test VENDPARTPURCHAGMT-12003 - The PART_NO_OEM must exist in Maintenix
    *
    */
   @Test
   public void test_VENDPARTPURCHAGMT_12003() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'Invalid'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD + "'" );
      lVendors.put( "PREF_BOOL", "'Y'" );
      lVendors.put( "UNIT_PRICE", "'200'" );
      lVendors.put( "QTY_UNIT_CD", "'EA'" );
      lVendors.put( "MIN_ORDER_QT", "'3'" );
      lVendors.put( "LEAD_TIME", "'10'" );
      lVendors.put( "EFFECTIVE_FROM_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_PURCHASE, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENDPARTPURCHAGMT-12003" );

   }


   /**
    *
    * test VENDPARTPURCHAGMT-12004 - The combination of Manufacturer Code and Part Number OEM must
    * exist in Maintenix
    *
    */
   @Test
   public void test_VENDPARTPURCHAGMT_12004() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM_3 + "'" ); // <<<
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD + "'" );
      lVendors.put( "PREF_BOOL", "'Y'" );
      lVendors.put( "UNIT_PRICE", "'200'" );
      lVendors.put( "QTY_UNIT_CD", "'EA'" );
      lVendors.put( "MIN_ORDER_QT", "'3'" );
      lVendors.put( "LEAD_TIME", "'10'" );
      lVendors.put( "EFFECTIVE_FROM_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_PURCHASE, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENDPARTPURCHAGMT-12004" );

   }


   /**
    *
    * test VENDPARTPURCHAGMT_12006 - There always needs to be a preferred vendor
    *
    */
   @Test
   public void test_VENDPARTPURCHAGMT_12006() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_VENDOR_PURCHASE table
      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD + "'" );
      lVendors.put( "PREF_BOOL", "'Y'" );
      lVendors.put( "UNIT_PRICE", "'200'" );
      lVendors.put( "QTY_UNIT_CD", "'EA'" );
      lVendors.put( "MIN_ORDER_QT", "'3'" );
      lVendors.put( "LEAD_TIME", "'10'" );
      lVendors.put( "EFFECTIVE_FROM_DT", "SYSDATE" );

      // insert map #1
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_PURCHASE, lVendors ) );

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_2 + "'" );

      // insert map #2 insert a 2nd record with a new vendor
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_PURCHASE, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENDPARTPURCHAGMT-12006" );

   }


   /**
    *
    * test VENDPARTPURCHAGMT_12007 - There always needs to be a preferred vendor
    *
    */
   @Test
   public void test_VENDPARTPURCHAGMT_12007() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_VENDOR_PURCHASE table
      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD + "'" );
      lVendors.put( "PREF_BOOL", "'N'" ); // <<<
      lVendors.put( "UNIT_PRICE", "'200'" );
      lVendors.put( "QTY_UNIT_CD", "'EA'" );
      lVendors.put( "MIN_ORDER_QT", "'3'" );
      lVendors.put( "LEAD_TIME", "'10'" );
      lVendors.put( "EFFECTIVE_FROM_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_PURCHASE, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENDPARTPURCHAGMT-12007" );

   }


   /**
    *
    * test VENDPARTPURCHAGMT_12008 - This part already has more than one preferred vendor in
    * eqp_part_vendor.pref_bool. Ensure that each part has exactly one preferred vendor.
    *
    * Note: pre-data setup must fudge the data so this error occur.
    */
   @Test
   public void test_VENDPARTPURCHAGMT_12008() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_VENDOR_PURCHASE table
      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM_2 + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD_2 + "'" );
      lVendors.put( "PREF_BOOL", "'N'" );
      lVendors.put( "UNIT_PRICE", "'200'" );
      lVendors.put( "QTY_UNIT_CD", "'EA'" );
      lVendors.put( "MIN_ORDER_QT", "'3'" );
      lVendors.put( "LEAD_TIME", "'10'" );
      lVendors.put( "EFFECTIVE_FROM_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_PURCHASE, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENDPARTPURCHAGMT-12008" );

   }


   /**
    *
    * test VENDPARTPURCHAGMT_12009 - C_VENDOR_PURCHASE.PREF_BOOL must be Y or N
    *
    */
   @Test
   public void test_VENDPARTPURCHAGMT_12009() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_VENDOR_PURCHASE table
      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD + "'" );
      lVendors.put( "PREF_BOOL", "'1'" ); // <<<<
      lVendors.put( "UNIT_PRICE", "'200'" );
      lVendors.put( "QTY_UNIT_CD", "'EA'" );
      lVendors.put( "MIN_ORDER_QT", "'3'" );
      lVendors.put( "LEAD_TIME", "'10'" );
      lVendors.put( "EFFECTIVE_FROM_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_PURCHASE, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENDPARTPURCHAGMT-12009" );

   }


   /**
    *
    * test VENDPARTPURCHAGMT-12010 - When unit price is specified, must be greater than zero
    *
    */
   @Test
   public void test_VENDPARTPURCHAGMT_12010_1() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD + "'" );
      lVendors.put( "PREF_BOOL", "'Y'" );
      lVendors.put( "UNIT_PRICE", "'0'" ); // <<<
      lVendors.put( "QTY_UNIT_CD", "'EA'" );
      lVendors.put( "MIN_ORDER_QT", "'3'" );
      lVendors.put( "LEAD_TIME", "'10'" );
      lVendors.put( "EFFECTIVE_FROM_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_PURCHASE, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENDPARTPURCHAGMT-12010" );

   }


   /**
    *
    * test VENDPARTPURCHAGMT-12010 - When unit price is specified, must be greater than zero
    *
    */
   @Test
   public void test_VENDPARTPURCHAGMT_12010_2() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD + "'" );
      lVendors.put( "PREF_BOOL", "'Y'" );
      lVendors.put( "UNIT_PRICE", "-1.00" ); // <<<
      lVendors.put( "QTY_UNIT_CD", "'EA'" );
      lVendors.put( "MIN_ORDER_QT", "'3'" );
      lVendors.put( "LEAD_TIME", "'10'" );
      lVendors.put( "EFFECTIVE_FROM_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_PURCHASE, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENDPARTPURCHAGMT-12010" );

   }


   /**
    *
    * test VENDPARTPURCHAGMT-12011 - Unit of measure is mandatory only if unit price is specified.
    *
    */
   @Test
   public void test_VENDPARTPURCHAGMT_12011() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD + "'" );
      lVendors.put( "PREF_BOOL", "'Y'" );
      lVendors.put( "UNIT_PRICE", "'200'" );
      lVendors.put( "QTY_UNIT_CD", "null" ); // <<<
      lVendors.put( "MIN_ORDER_QT", "'3'" );
      lVendors.put( "LEAD_TIME", "'10'" );
      lVendors.put( "EFFECTIVE_FROM_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_PURCHASE, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENDPARTPURCHAGMT-12011" );

   }


   /**
    *
    * test VENDPARTPURCHAGMT-12013 - Minimum order quantity is mandatory only if unit price is
    * specified
    *
    */
   @Test
   public void test_VENDPARTPURCHAGMT_12013() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD + "'" );
      lVendors.put( "PREF_BOOL", "'Y'" );
      lVendors.put( "UNIT_PRICE", "'200'" );
      lVendors.put( "QTY_UNIT_CD", "'EA'" );
      lVendors.put( "MIN_ORDER_QT", "null" ); // <<<
      lVendors.put( "LEAD_TIME", "'10'" );
      lVendors.put( "EFFECTIVE_FROM_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_PURCHASE, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENDPARTPURCHAGMT-12013" );

   }


   /**
    *
    * test VENDPARTPURCHAGMT-12014 - PREFER_BOOL cannot be null
    *
    */
   @Test
   public void test_VENDPARTPURCHAGMT_12014() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD + "'" );
      lVendors.put( "PREF_BOOL", "'Y'" );
      lVendors.put( "UNIT_PRICE", "'200'" );
      lVendors.put( "QTY_UNIT_CD", "'EA'" );
      lVendors.put( "MIN_ORDER_QT", "'3'" );
      lVendors.put( "LEAD_TIME", "null" ); // <<<<
      lVendors.put( "EFFECTIVE_FROM_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_PURCHASE, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENDPARTPURCHAGMT-12014" );

   }


   /**
    *
    * test VENDPARTPURCHAGMT-12015 - Effective from date is mandatory if unit price is specified
    *
    */
   @Test
   public void test_VENDPARTPURCHAGMT_12015() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD + "'" );
      lVendors.put( "PREF_BOOL", "'Y'" );
      lVendors.put( "UNIT_PRICE", "'200'" );
      lVendors.put( "QTY_UNIT_CD", "'EA'" );
      lVendors.put( "MIN_ORDER_QT", "'3'" );
      lVendors.put( "LEAD_TIME", "'10'" );
      lVendors.put( "EFFECTIVE_FROM_DT", "null" ); // <<<

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_PURCHASE, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENDPARTPURCHAGMT-12015" );

   }


   /**
    *
    * test VENDPARTPURCHAGMT-12016 - Discount percentage must be null or between 0 and 1.0
    *
    */
   @Test
   public void test_VENDPARTPURCHAGMT_12016_1() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD + "'" );
      lVendors.put( "PREF_BOOL", "'Y'" );
      lVendors.put( "UNIT_PRICE", "'200'" );
      lVendors.put( "DISCOUNT_PCT", "'-1'" );
      lVendors.put( "QTY_UNIT_CD", "'EA'" );
      lVendors.put( "MIN_ORDER_QT", "'3'" );
      lVendors.put( "LEAD_TIME", "'10'" );
      lVendors.put( "EFFECTIVE_FROM_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_PURCHASE, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENDPARTPURCHAGMT-12016" );

   }


   /**
    *
    * test VENDPARTPURCHAGMT-12016 - Discount percentage must be null or between 0 and 1.0
    *
    */
   @Test
   public void test_VENDPARTPURCHAGMT_12016_2() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD + "'" );
      lVendors.put( "PREF_BOOL", "'Y'" );
      lVendors.put( "UNIT_PRICE", "'200'" );
      lVendors.put( "DISCOUNT_PCT", "'1.1'" );
      lVendors.put( "QTY_UNIT_CD", "'EA'" );
      lVendors.put( "MIN_ORDER_QT", "'3'" );
      lVendors.put( "LEAD_TIME", "'10'" );
      lVendors.put( "EFFECTIVE_FROM_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_PURCHASE, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENDPARTPURCHAGMT-12016" );

   }


   /**
    *
    * test VENDPARTPURCHAGMT-12017 - Effective From date cannot be later than Effective To date
    *
    */
   @Test
   public void test_VENDPARTPURCHAGMT_12017() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD + "'" );
      lVendors.put( "PREF_BOOL", "'Y'" );
      lVendors.put( "UNIT_PRICE", "'200'" );
      lVendors.put( "QTY_UNIT_CD", "'EA'" );
      lVendors.put( "MIN_ORDER_QT", "'3'" );
      lVendors.put( "LEAD_TIME", "'10'" );
      lVendors.put( "EFFECTIVE_FROM_DT", "SYSDATE" );
      lVendors.put( "EFFECTIVE_TO_DT", "SYSDATE-1" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_PURCHASE, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENDPARTPURCHAGMT-12017" );

   }


   /**
    *
    * test VENDPARTPURCHAGMT-12019 - The combination of the following C_VENDOR_PURCHASE columns must
    * be unique: VENDOR_CD, PART_NO_OEM, MANUFACT_CD, UNIT_PRICE, QTY_UNIT_CD, LEAD_TIME,
    * EFFECTIVE_FROM_DT, MIN_ORDER_QT
    *
    */
   @Test
   public void test_VENDPARTPURCHAGMT_12019() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM_2 + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD_2 + "'" );
      lVendors.put( "PREF_BOOL", "'Y'" );
      lVendors.put( "UNIT_PRICE", "'2000.00000'" );
      lVendors.put( "QTY_UNIT_CD", "'EA'" );
      lVendors.put( "MIN_ORDER_QT", "'2'" );
      lVendors.put( "LEAD_TIME", "'30'" );
      lVendors.put( "EFFECTIVE_FROM_DT", "to_date(\'05/01/2019\',\'mm/dd/yyyy\')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_PURCHASE, lVendors ) );
      // insert map <<<
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_PURCHASE, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENDPARTPURCHAGMT-12019" );

   }


   /**
    *
    * test VENDPARTPURCHAGMT_12007 - Given VENDOR_CD, MANUFACT_CD, PART_NO_OEM combination,
    * PART_NO_VENDOR, CONTRACT_NUMBER, PREF_BOOL must have the same values.
    *
    */
   @Test
   public void test_VENDPARTPURCHAGMT_12020() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_VENDOR_PURCHASE table
      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD + "'" );
      lVendors.put( "PREF_BOOL", "'Y'" );
      lVendors.put( "UNIT_PRICE", "'200'" );
      lVendors.put( "QTY_UNIT_CD", "'EA'" );
      lVendors.put( "MIN_ORDER_QT", "'3'" );
      lVendors.put( "LEAD_TIME", "'10'" );
      lVendors.put( "EFFECTIVE_FROM_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_PURCHASE, lVendors ) );
      lVendors.put( "PREF_BOOL", "'N'" ); // second record will have a 'N' value
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_PURCHASE, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENDPARTPURCHAGMT-12020" );

   }


   /**
    *
    * test VENDPARTPURCHAGMT-12021 - When unit of measurement is specified, must exist in Maintenix
    *
    */
   @Test
   public void test_VENDPARTPURCHAGMT_12021() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD + "'" );
      lVendors.put( "PREF_BOOL", "'Y'" );
      lVendors.put( "UNIT_PRICE", "'200'" );
      lVendors.put( "QTY_UNIT_CD", "'INVALID'" ); // <<<
      lVendors.put( "MIN_ORDER_QT", "'3'" );
      lVendors.put( "LEAD_TIME", "'10'" );
      lVendors.put( "EFFECTIVE_FROM_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_PURCHASE, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENDPARTPURCHAGMT-12021" );

   }


   /**
    *
    * test VENDPARTPURCHAGMT-12022 - When unit of measurement is specified, must be a valid one for
    * the defined part
    *
    */
   @Test
   public void test_VENDPARTPURCHAGMT_12022() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD + "'" );
      lVendors.put( "PREF_BOOL", "'Y'" );
      lVendors.put( "UNIT_PRICE", "'200'" );
      lVendors.put( "QTY_UNIT_CD", "'CY'" ); // <<<
      lVendors.put( "MIN_ORDER_QT", "'3'" );
      lVendors.put( "LEAD_TIME", "'10'" );
      lVendors.put( "EFFECTIVE_FROM_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_PURCHASE, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENDPARTPURCHAGMT-12022" );

   }


   /**
    *
    * test VENDPARTPURCHAGMT-12023 - The part number must be procurable in order to be added to the
    * purchasing agreement list
    *
    */
   @Test
   public void test_VENDPARTPURCHAGMT_12023() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + "A0000001" + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD_3 + "'" );
      lVendors.put( "PREF_BOOL", "'Y'" );
      lVendors.put( "UNIT_PRICE", "'200'" );
      lVendors.put( "QTY_UNIT_CD", "'EA'" );
      lVendors.put( "MIN_ORDER_QT", "'3'" );
      lVendors.put( "LEAD_TIME", "'10'" );
      lVendors.put( "EFFECTIVE_FROM_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_PURCHASE, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENDPARTPURCHAGMT-12023" );

   }


   /**
    *
    * test VENDPARTPURCHAGMT-12024 - There must be a preferred Vendor for the
    * part_no_oem/manufact_cd combination in staging area zero
    *
    * Notes: forces the user to specify preferred vendor in staging if they are removing the flag
    * from the currently preferred one
    */
   @Test
   public void test_VENDPARTPURCHAGMT_12024() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM_2 + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD_2 + "'" );
      lVendors.put( "PREF_BOOL", "'N'" );
      lVendors.put( "UNIT_PRICE", "'2000'" );
      lVendors.put( "QTY_UNIT_CD", "'EA'" );
      lVendors.put( "MIN_ORDER_QT", "'2'" );
      lVendors.put( "LEAD_TIME", "'30'" );
      lVendors.put( "EFFECTIVE_FROM_DT", "to_date(\'05/01/2019\',\'mm/dd/yyyy\')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_PURCHASE, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENDPARTPURCHAGMT-12024" );

   }


   /**
    *
    * test VENDPARTPURCHAGMT-12025 - When Lead time is specified, must be greater than zero
    *
    */
   @Test
   public void test_VENDPARTPURCHAGMT_12025() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD + "'" );
      lVendors.put( "PREF_BOOL", "'Y'" );
      lVendors.put( "UNIT_PRICE", "'200'" );
      lVendors.put( "QTY_UNIT_CD", "'EA'" );
      lVendors.put( "MIN_ORDER_QT", "'3'" );
      lVendors.put( "LEAD_TIME", "'0'" ); // <<<<
      lVendors.put( "EFFECTIVE_FROM_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_PURCHASE, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENDPARTPURCHAGMT-12025" );

   }


   /**
    *
    * test VENDPARTPURCHAGMT-12026 - When minimum order quantity is specified, must be greater than
    * zero
    *
    */
   @Test
   public void test_VENDPARTPURCHAGMT_12026() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD + "'" );
      lVendors.put( "PREF_BOOL", "'Y'" );
      lVendors.put( "UNIT_PRICE", "'200'" );
      lVendors.put( "QTY_UNIT_CD", "'EA'" );
      lVendors.put( "MIN_ORDER_QT", "'0'" );
      lVendors.put( "LEAD_TIME", "'10'" );
      lVendors.put( "EFFECTIVE_FROM_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_PURCHASE, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENDPARTPURCHAGMT-12026" );

   }


   // =====================================================

   /**
    * test VENDPARTPURCHAGMT-12027 - An active price agreement is already in place for this vendor,
    * part and quantity. The existing price agreement must first be made obsolete before a new one
    * can be loaded.
    */
   @Test
   public void test_VENDPARTPURCHAGMT_12027() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_VENDOR_PURCHASE table
      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM_2 + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD_2 + "'" );
      lVendors.put( "PREF_BOOL", "'Y'" );
      lVendors.put( "UNIT_PRICE", "'2000'" );
      lVendors.put( "QTY_UNIT_CD", "'EA'" );
      lVendors.put( "MIN_ORDER_QT", "'2'" );
      lVendors.put( "LEAD_TIME", "'30'" );
      lVendors.put( "EFFECTIVE_FROM_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_PURCHASE, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENDPARTPURCHAGMT-12027" );

   }


   /**
    * This function is to verify EQP_PART_VENDOR table
    *
    *
    */
   public void verifyEQPPARTVENDORPRICE( simpleIDs aVENDORIDs, simpleIDs aPNIDs, String aUNIT_PRICE,
         String aQTY_UNIT_CD, String aLEAD_TIME, String aMIN_QT, String aDiscount ) {

      String[] iIds = { "UNIT_PRICE", "QTY_UNIT_CD", "LEAD_TIME", "MIN_ORDER_QT", "DISCOUNT_PCT" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "VENDOR_DB_ID", aVENDORIDs.getNO_DB_ID() );
      lArgs.addArguments( "VENDOR_ID", aVENDORIDs.getNO_ID() );
      lArgs.addArguments( "PART_NO_DB_ID", aPNIDs.getNO_DB_ID() );
      lArgs.addArguments( "PART_NO_ID", aPNIDs.getNO_ID() );

      String iQueryString =
            TableUtil.buildTableQuery( TableUtil.EQP_PART_VENDOR_PRICE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "UNIT_PRICE", llists.get( 0 ).get( 0 ).equalsIgnoreCase( aUNIT_PRICE ) );
      Assert.assertTrue( "QTY_UNIT_CD", llists.get( 0 ).get( 1 ).equalsIgnoreCase( aQTY_UNIT_CD ) );
      Assert.assertTrue( "LEAD_TIME", llists.get( 0 ).get( 2 ).equalsIgnoreCase( aLEAD_TIME ) );
      Assert.assertTrue( "MIN_ORDER_QT", llists.get( 0 ).get( 3 ).equalsIgnoreCase( aMIN_QT ) );
      if ( aDiscount.isEmpty() )
         if ( llists.get( 0 ).get( 4 ) == null )
            Assert.assertTrue( "DISCOUNT_PCT", true );
         else
            Assert.assertTrue( "DISCOUNT_PCT", false );
      else
         Assert.assertTrue( "DISCOUNT_PCT",
               llists.get( 0 ).get( 4 ).equalsIgnoreCase( aDiscount ) );
   }


   /**
    * This function is to verify EQP_PART_VENDOR table
    *
    *
    */
   public void verifyEQPPARTVENDOR( simpleIDs aVENDORIDs, simpleIDs aPNIDs, String aPREF_BOOL,
         String aVendorStatusCd ) {

      String[] iIds = { "PREF_BOOL", "VENDOR_STATUS_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "VENDOR_DB_ID", aVENDORIDs.getNO_DB_ID() );
      lArgs.addArguments( "VENDOR_ID", aVENDORIDs.getNO_ID() );
      lArgs.addArguments( "PART_NO_DB_ID", aPNIDs.getNO_DB_ID() );
      lArgs.addArguments( "PART_NO_ID", aPNIDs.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EQP_PART_VENDOR, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "PREF_BOOL", llists.get( 0 ).get( 0 ).equalsIgnoreCase( aPREF_BOOL ) );
      Assert.assertTrue( "VENDOR_STATUS_CD",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( aVendorStatusCd ) );
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

         // delete eqp_part_vendor
         String lStrDelete = "delete from " + TableUtil.EQP_PART_VENDOR + " where VENDOR_DB_ID="
               + iVENDOR_IDs.getNO_DB_ID() + " and VENDOR_ID=" + iVENDOR_IDs.getNO_ID()
               + " and PART_NO_DB_ID=" + iPNIds.getNO_DB_ID() + " and PART_NO_ID = "
               + iPNIds.getNO_ID();
         executeSQL( lStrDelete );

         // delete eqp_part_vendor_price
         lStrDelete = "delete from " + TableUtil.EQP_PART_VENDOR_PRICE + " where VENDOR_DB_ID="
               + iVENDOR_IDs.getNO_DB_ID() + " and VENDOR_ID=" + iVENDOR_IDs.getNO_ID()
               + " and PART_NO_DB_ID=" + iPNIds.getNO_DB_ID() + " and PART_NO_ID = "
               + iPNIds.getNO_ID();
         executeSQL( lStrDelete );

      }

      //

   }


   public int runExport() {
      int lReturn = 0;

      try {
         CallableStatement lPrepareCallInventory = getConnection().prepareCall(
               "BEGIN c_vendor_purchase_import.export_agreement(on_retcode => ?); END;" );

         lPrepareCallInventory.registerOutParameter( 1, Types.INTEGER );

         lPrepareCallInventory.execute();
         commit();
         lReturn = lPrepareCallInventory.getInt( 1 );

      } catch ( SQLException e ) {
         e.printStackTrace();
      }
      return lReturn;
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
                     "BEGIN c_vendor_purchase_import.validate_agreement(on_retcode => ?); END;" );

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
                     "BEGIN c_vendor_purchase_import.import_agreement(on_retcode => ?); END;" );

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
