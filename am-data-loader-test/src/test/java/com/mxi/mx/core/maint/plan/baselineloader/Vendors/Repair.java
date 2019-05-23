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
 * c_vendor_repair_import package.
 *
 * @author ALICIA QIAN
 */
public class Repair extends BaselineLoaderTest {

   @Rule
   public TestName testName = new TestName();

   ValidationAndImport ivalidationandimport;

   public String iVENDOR_CD_1 = "10004";
   public String iPART_NO_OEM = "A0000010";
   public String iMANUFACT_CD = "11111";
   public String iVendor_Type_CD = "REPAIR";
   public String iCONTRACT_NUMBER = "CONTRACT001";
   public String iCONTRACT_NUMBER_MX = "CONTRACT001MX";

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

   public String iErrorCdQuery = "select c_vendor_repair.result_cd, dl_ref_message.tech_desc from "
         + TableUtil.C_VENDOR_REPAIR + " inner join dl_ref_message on "
         + " dl_ref_message.result_cd = c_vendor_repair.result_cd";


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
      iPNIDs = null;
      if ( testName.getMethodName().contains( "12008" ) )
         runUpdate( "update " + TableUtil.EQP_PART_VENDOR_REP
               + " set pref_bool = 1 where contract_number = '" + iCONTRACT_NUMBER_MX + "'" );

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
                     + iVENDOR_CD_4 + "'", "vendor_db_id", "vendor_id" );
         runUpdate( "update " + TableUtil.EQP_PART_VENDOR_REP + " set pref_bool = 0 where "
               + "(part_no_id != " + lPart.getNO_ID() + " or vendor_id != " + lVendor.getNO_ID()
               + ")" );
      }
      if ( testName.getMethodName().contains( "testNewPreferredVendor_IMPORT" ) ) {

         simpleIDs lPart = getIDs(
               "select part_no_db_id, part_no_id from eqp_part_no where part_no_oem = '"
                     + iPART_NO_OEM_2 + "' and manufact_cd = '" + iMANUFACT_CD_2 + "'",
               "part_no_db_id", "part_no_id" );
         simpleIDs lVendor =
               getIDs( "select vendor_db_id, vendor_id from org_vendor where vendor_cd = '"
                     + iVENDOR_CD_4 + "'", "vendor_db_id", "vendor_id" );
         runUpdate( "update " + TableUtil.EQP_PART_VENDOR_REP + " set pref_bool = 1 where "
               + " (part_no_id = " + lPart.getNO_ID() + " and vendor_id = " + lVendor.getNO_ID()
               + ")" );
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
      lVendors.put( "CONTRACT_NUMBER", "'" + iCONTRACT_NUMBER + "'" );
      lVendors.put( "REPAIR_COST", "'2000'" );
      lVendors.put( "LEAD_TIME", "'20'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_REPAIR, lVendors ) );

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
      verifyEQPPARTVENDORREP( iVENDOR_IDs, iPNIDs, "1", iCONTRACT_NUMBER, "20", "2000",
            "APPROVED" );

      // checking prefer_bool on existing Purchase Agreement
      simpleIDs lPNIds = getPNId( iPART_NO_OEM_2, iMANUFACT_CD_2 );
      simpleIDs lVENDOR_IDs = getVENDORID( iVENDOR_CD_4, iVendor_Type_CD_4 );
      verifyEQPPARTVENDORREP( lVENDOR_IDs, lPNIds, "0", iCONTRACT_NUMBER_MX, "20", "2000",
            "APPROVED" );
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
      lVendors.put( "CONTRACT_NUMBER", "'" + iCONTRACT_NUMBER + "'" );
      lVendors.put( "REPAIR_COST", "'100'" );
      lVendors.put( "LEAD_TIME", "'10'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_REPAIR, lVendors ) );

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
      verifyEQPPARTVENDORREP( iVENDOR_IDs, iPNIDs, "0", iCONTRACT_NUMBER, "10", "100", "APPROVED" );

      // checking prefer_bool on existing Purchase Agreement
      simpleIDs lPNIds = getPNId( iPART_NO_OEM_2, iMANUFACT_CD_2 );
      simpleIDs lVENDOR_IDs = getVENDORID( iVENDOR_CD_4, iVendor_Type_CD_4 );
      verifyEQPPARTVENDORREP( lVENDOR_IDs, lPNIds, "1", iCONTRACT_NUMBER_MX, "20", "2000",
            "APPROVED" );
   }


   /**
    * This test is to create vendor purchase
    *
    *
    */

   public void test_Vendor_repair_validation() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_VENDOR_REPAIR table
      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD + "'" );
      lVendors.put( "PREF_BOOL", "'Y'" );
      lVendors.put( "CONTRACT_NUMBER", "'" + iCONTRACT_NUMBER + "'" );
      lVendors.put( "REPAIR_COST", "'100'" );
      lVendors.put( "LEAD_TIME", "'10'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_REPAIR, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to create vendor repair part.
    *
    *
    */
   @Test
   public void test_Vendor_repair_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      test_Vendor_repair_validation();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // verify EQP_PART_VENDOR_REP
      iPNIDs = getPNId( iPART_NO_OEM, iMANUFACT_CD );
      iVENDOR_IDs = getVENDORID( iVENDOR_CD_1, iVendor_Type_CD );
      verifyEQPPARTVENDORREP( iVENDOR_IDs, iPNIDs, "1", iCONTRACT_NUMBER, "10", "100", "APPROVED" );
   }


   /**
    *
    * test VENDREP-10001 - VENDOR_CD is mandatory
    *
    */
   @Test
   public void testVENDREP_10001() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_VENDOR_REPAIR table
      Map<String, String> lVendors = new LinkedHashMap<>();

      // lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD + "'" );
      lVendors.put( "PREF_BOOL", "'Y'" );
      lVendors.put( "CONTRACT_NUMBER", "'" + iCONTRACT_NUMBER + "'" );
      lVendors.put( "REPAIR_COST", "'100'" );
      lVendors.put( "LEAD_TIME", "'10'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_REPAIR, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENDREP-10001" );

   }


   /**
    *
    * test VENDREP-10002 - MANUFACT_CD is mandatory
    *
    */
   @Test
   public void testVENDREP_10002() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_VENDOR_REPAIR table
      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      // lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD + "'" );
      lVendors.put( "PREF_BOOL", "'Y'" );
      lVendors.put( "CONTRACT_NUMBER", "'" + iCONTRACT_NUMBER + "'" );
      lVendors.put( "REPAIR_COST", "'100'" );
      lVendors.put( "LEAD_TIME", "'10'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_REPAIR, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENDREP-10002" );

   }


   /**
    *
    * test VENDREP-10003 - PART_NO_OEM is mandatory
    *
    */
   @Test
   public void testVENDREP_10003() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_VENDOR_REPAIR table
      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      // lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD + "'" );
      lVendors.put( "PREF_BOOL", "'Y'" );
      lVendors.put( "CONTRACT_NUMBER", "'" + iCONTRACT_NUMBER + "'" );
      lVendors.put( "REPAIR_COST", "'100'" );
      lVendors.put( "LEAD_TIME", "'10'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_REPAIR, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENDREP-10003" );

   }


   /**
    *
    * test VENDREP-10004 - PREF_BOOL is mandatory
    *
    */
   @Test
   public void testVENDREP_10004() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_VENDOR_REPAIR table
      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD + "'" );
      lVendors.put( "PREF_BOOL", "null" ); // <<<
      lVendors.put( "CONTRACT_NUMBER", "'" + iCONTRACT_NUMBER + "'" );
      lVendors.put( "REPAIR_COST", "'100'" );
      lVendors.put( "LEAD_TIME", "'10'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_REPAIR, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENDREP-10004" );

   }


   /**
    *
    * test VENDREP-12001 - VENDOR_CD must exist in Maintenix
    *
    */
   @Test
   public void testVENDREP_12001() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_VENDOR_REPAIR table
      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'Invalid'" ); // <<<<<<<<<<<<<
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD + "'" );
      lVendors.put( "PREF_BOOL", "'Y'" );
      lVendors.put( "CONTRACT_NUMBER", "'" + iCONTRACT_NUMBER + "'" );
      lVendors.put( "REPAIR_COST", "'100'" );
      lVendors.put( "LEAD_TIME", "'10'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_REPAIR, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENDREP-12001" );

   }


   /**
    *
    * test VENDREP-12002 - MANUFACT_CD is mandatory
    *
    */
   @Test
   public void testVENDREP_12002() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_VENDOR_REPAIR table
      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lVendors.put( "MANUFACT_CD", "'Invalid'" ); // <<<<<<<<<
      lVendors.put( "PREF_BOOL", "'Y'" );
      lVendors.put( "CONTRACT_NUMBER", "'" + iCONTRACT_NUMBER + "'" );
      lVendors.put( "REPAIR_COST", "'100'" );
      lVendors.put( "LEAD_TIME", "'10'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_REPAIR, lVendors ) );
      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENDREP-12002" );

   }


   /**
    *
    * test VENDREP-12003 - PART_NO_OEM is mandatory
    *
    */
   @Test
   public void testVENDREP_12003() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_VENDOR_REPAIR table
      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'Invalid'" ); // <<<<<<<<<<<<<<<<
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD + "'" );
      lVendors.put( "PREF_BOOL", "'Y'" );
      lVendors.put( "CONTRACT_NUMBER", "'" + iCONTRACT_NUMBER + "'" );
      lVendors.put( "REPAIR_COST", "'100'" );
      lVendors.put( "LEAD_TIME", "'10'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_REPAIR, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENDREP-12003" );

   }


   /**
    *
    * test VENDREP-12004 - The combination of Manufacturer Code and Part Number OEM must exist in
    * Maintenix
    *
    */
   @Test
   public void testVENDREP_12004() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_VENDOR_REPAIR table
      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD_3 + "'" );
      lVendors.put( "PREF_BOOL", "'Y'" );
      lVendors.put( "CONTRACT_NUMBER", "'" + iCONTRACT_NUMBER + "'" );
      lVendors.put( "REPAIR_COST", "'100'" );
      lVendors.put( "LEAD_TIME", "'10'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_REPAIR, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENDREP-12004" );

   }


   /**
    *
    * test VENDREP-12005 - VENDOR_CODE/PART_NO_OEM/MANUFACT_CD combination must be unique
    *
    */
   @Test
   public void testVENDREP_12005() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_VENDOR_REPAIR table
      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD + "'" );
      lVendors.put( "PREF_BOOL", "'Y'" );
      lVendors.put( "CONTRACT_NUMBER", "'" + iCONTRACT_NUMBER + "'" );
      lVendors.put( "REPAIR_COST", "'100'" );
      lVendors.put( "LEAD_TIME", "'10'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_REPAIR, lVendors ) );
      // insert map <<<<<<<<
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_REPAIR, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENDREP-12005" );

   }


   /**
    *
    * test VENDREP-12006 - A single Part Number OEM and Manufacturer Code combination cannot be
    * preferred to more than one Vendor in staging area
    *
    */
   @Test
   public void testVENDREP_12006() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_VENDOR_REPAIR table
      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD + "'" );
      lVendors.put( "PREF_BOOL", "'Y'" );
      lVendors.put( "CONTRACT_NUMBER", "'" + iCONTRACT_NUMBER + "'" );
      lVendors.put( "REPAIR_COST", "'100'" );
      lVendors.put( "LEAD_TIME", "'10'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_REPAIR, lVendors ) );
      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_4 + "'" );
      // insert map insert the same record with different vendor <<<<<<<<<<<<<<<
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_REPAIR, lVendors ) );
      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENDREP-12006" );

   }


   /**
    *
    * test VENDREP-12007 - There must be a preferred Vendor for the PART_NO_OEM/MANUFACT_CD
    * combination in staging area
    *
    */
   @Test
   public void testVENDREP_12007() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_VENDOR_REPAIR table
      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD + "'" );
      lVendors.put( "PREF_BOOL", "'N'" ); // <<<<<<
      lVendors.put( "CONTRACT_NUMBER", "'" + iCONTRACT_NUMBER + "'" );
      lVendors.put( "REPAIR_COST", "'100'" );
      lVendors.put( "LEAD_TIME", "'10'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_REPAIR, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENDREP-12007" );

   }


   /**
    *
    * test VENDREP-12008 - This part already has more than one preferred vendor in
    * eqp_part_vendor.pref_bool. Note: When more than one preferred Vendor exists in Maintenix, One
    * of those vendors must be set as non-preferred before any other data can be loaded for that
    * part
    */
   @Test
   public void testVENDREP_12008() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_VENDOR_REPAIR table
      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_3 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM_2 + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD_2 + "'" );
      lVendors.put( "PREF_BOOL", "'N'" );
      lVendors.put( "CONTRACT_NUMBER", "'" + iCONTRACT_NUMBER + "'" );
      lVendors.put( "REPAIR_COST", "'100'" );
      lVendors.put( "LEAD_TIME", "'10'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_REPAIR, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENDREP-12008" );

   }


   /**
    *
    * test VENDREP-12009 - PREF_BOOL has to have value of either Y or N
    *
    */
   @Test
   public void testVENDREP_12009() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_VENDOR_REPAIR table
      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD + "'" );
      lVendors.put( "PREF_BOOL", "'K'" ); // <<<<<<<<<<<<
      lVendors.put( "CONTRACT_NUMBER", "'" + iCONTRACT_NUMBER + "'" );
      lVendors.put( "REPAIR_COST", "'100'" );
      lVendors.put( "LEAD_TIME", "'10'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_REPAIR, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENDREP-12009" );

   }


   /**
    *
    * test VENDREP-12010 - The Repair Cost must be greater than or equal to zero
    *
    */
   @Test
   public void testVENDREP_12010() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_VENDOR_REPAIR table
      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD + "'" );
      lVendors.put( "PREF_BOOL", "'Y'" );
      lVendors.put( "CONTRACT_NUMBER", "'" + iCONTRACT_NUMBER + "'" );
      lVendors.put( "REPAIR_COST", "'-0.01'" ); // <<<<<<<<<<<
      lVendors.put( "LEAD_TIME", "'10'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_REPAIR, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENDREP-12010" );

   }


   /**
    *
    * test VENDREP-12011 - LEAD_TIME must be greater than or equal to 0
    *
    */
   @Test
   public void testVENDREP_12011() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_VENDOR_REPAIR table
      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD + "'" );
      lVendors.put( "PREF_BOOL", "'Y'" );
      lVendors.put( "CONTRACT_NUMBER", "'" + iCONTRACT_NUMBER + "'" );
      lVendors.put( "REPAIR_COST", "'100'" );
      lVendors.put( "LEAD_TIME", "'-1'" ); // <<<<<<

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_REPAIR, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENDREP-12011" );

   }


   /**
    *
    * test VENDREP-12012 - The part number must be procurable in order to be added to the repair
    * agreement list
    *
    */
   @Test
   public void testVENDREP_12012() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_VENDOR_REPAIR table
      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + "A0000001" + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD_3 + "'" );
      lVendors.put( "PREF_BOOL", "'Y'" );
      lVendors.put( "CONTRACT_NUMBER", "'" + iCONTRACT_NUMBER + "'" );
      lVendors.put( "REPAIR_COST", "'100'" );
      lVendors.put( "LEAD_TIME", "'10'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_REPAIR, lVendors ) );
      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENDREP-12012" );

   }


   /**
    *
    * test VENDREP-12013 - There must be a preferred Vendor for the PART_NO_OEM / MANUFACT_CD
    * combination in the staging area if the existing preferred vendor in Maintenix is being removed
    *
    */
   @Test
   public void testVENDREP_12013() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_VENDOR_REPAIR table
      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_4 + "'" );
      lVendors.put( "PART_NO_OEM", "'" + iPART_NO_OEM_2 + "'" );
      lVendors.put( "MANUFACT_CD", "'" + iMANUFACT_CD_2 + "'" );
      lVendors.put( "PREF_BOOL", "'N'" );
      lVendors.put( "CONTRACT_NUMBER", "'" + iCONTRACT_NUMBER + "'" );
      lVendors.put( "REPAIR_COST", "'100'" );
      lVendors.put( "LEAD_TIME", "'10'" );

      // insert map #1
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_REPAIR, lVendors ) );
      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_2 + "'" );
      // insert map #2, but with new vendor
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_REPAIR, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      checkErrorCode( iErrorCdQuery, "VENDREP-12013" );

   }


   // =====================================================

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
    *
    * This function is to verify EQP_PART_VENDOR_REP table
    *
    * @param aVENDORIDs
    * @param aPNIDs
    * @param aPREF_BOOL
    * @param aCONTRACT_NUMBER
    * @param aLEAD_TIME
    * @param aREPAIR_COST
    */
   public void verifyEQPPARTVENDORREP( simpleIDs aVENDORIDs, simpleIDs aPNIDs, String aPREF_BOOL,
         String aCONTRACT_NUMBER, String aLEAD_TIME, String aREPAIR_COST, String aVendorStatusCd ) {

      String[] iIds =
            { "PREF_BOOL", "CONTRACT_NUMBER", "LEAD_TIME", "REPAIR_COST", "VENDOR_STATUS_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "VENDOR_DB_ID", aVENDORIDs.getNO_DB_ID() );
      lArgs.addArguments( "VENDOR_ID", aVENDORIDs.getNO_ID() );
      lArgs.addArguments( "PART_NO_DB_ID", aPNIDs.getNO_DB_ID() );
      lArgs.addArguments( "PART_NO_ID", aPNIDs.getNO_ID() );

      String iQueryString =
            TableUtil.buildTableQuery( TableUtil.EQP_PART_VENDOR_REP, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "PREF_BOOL", llists.get( 0 ).get( 0 ).equalsIgnoreCase( aPREF_BOOL ) );
      Assert.assertTrue( "CONTRACT_NUMBER",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( aCONTRACT_NUMBER ) );
      Assert.assertTrue( "LEAD_TIME", llists.get( 0 ).get( 2 ).equalsIgnoreCase( aLEAD_TIME ) );
      Assert.assertTrue( "REPAIR_COST", llists.get( 0 ).get( 3 ).equalsIgnoreCase( aREPAIR_COST ) );
      Assert.assertTrue( "VENDOR_STATUS_CD",
            llists.get( 0 ).get( 4 ).equalsIgnoreCase( aVendorStatusCd ) );
   }


   /**
    * This function is called by after test method to restore data which has been modified by tests.
    *
    *
    */
   public void RestoreData() {

      if ( iVENDOR_IDs != null ) {

         // delete EQP_PART_VENDOR_REP
         String lStrDelete = "delete from " + TableUtil.EQP_PART_VENDOR_REP
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
                     "BEGIN c_vendor_repair_import.validate_agreement(on_retcode => ?); END;" );

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
                     "BEGIN c_vendor_repair_import.import_agreement(on_retcode => ?); END;" );

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
