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
import com.mxi.mx.util.StringUtils;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.ValidationAndImport;
import com.mxi.mx.util.WhereClause;


/**
 * This class contains common functions which support validation and import functionality of
 * import_vendor package.
 *
 * @author ALICIA QIAN
 */
public class Vendors extends BaselineLoaderTest {

   @Rule
   public TestName testName = new TestName();

   ValidationAndImport ivalidationandimport;

   public String iVENDOR_CD_1 = "AUTOVENDOR1";
   public String iVENDOR_TYPE_CD_BROKER = "BROKER";
   public String iVENDOR_NAME_BROKER = "AUTOBROKER";
   public String iCURRENCY_CD_USD = "USD";
   public String iNO_PRINT_BOOL_TRUE = "Y";
   public String iCONTACT_NAME = "AUTONAME";
   public String iADDRESS_LINE_1 = "AUTOADD";
   public String iCITY_NAME = "AUTO";
   public String iCOUNTRY_CD = "CAN";
   public String iTIMEZONE_CD_EST = "Canada/Eastern";

   public String iORG_CD_1 = "MXI";
   public String iPO_TYPE_CD_1 = "EXCHANGE";
   public String iVENDOR_STATUS_CD_WARNING = "WARNING";
   public String iSERVICE_TYPE_CD = "TEST";
   public String iVENDOR_ACCOUNT_CD = "AUTOACCT";

   public String iLOC_TYPE_CD = "VENDOR";
   public String iLOC_TYPE_SDESC = "Vendor";
   public String icurrency_USD_SDESC = "US Dollars";
   public String iVENDOR_STATUS_CD_WARNING_SDESC = "Warning";
   public String iSERVICE_TYPE_CD_TEST_SDESC = "Testing";

   public simpleIDs iLOC_IDs = null;
   public simpleIDs iVENDOR_IDs = null;
   public simpleIDs iOWNER_IDs = null;


   /**
    * Setup before executing each individual test
    *
    */
   @Before
   @Override
   public void before() throws Exception {

      super.before();
      clearBaselineLoaderTablesRegionD();
      iLOC_IDs = null;
      iVENDOR_IDs = null;
      iOWNER_IDs = null;

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
    * This test is to create vendor
    *
    *
    */
   public void test_Vendor_creation_validation() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_DYN_USAGE_DEF table
      Map<String, String> lVendors = new LinkedHashMap<>();

      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "VENDOR_TYPE_CD", "'" + iVENDOR_TYPE_CD_BROKER + "'" );
      lVendors.put( "VENDOR_NAME", "'" + iVENDOR_NAME_BROKER + "'" );
      lVendors.put( "CURRENCY_CD", "'" + iCURRENCY_CD_USD + "'" );
      lVendors.put( "NO_PRINT_REQ_BOOL", "'" + iNO_PRINT_BOOL_TRUE + "'" );
      lVendors.put( "CONTACT_NAME", "'" + iCONTACT_NAME + "'" );
      lVendors.put( "ADDRESS_PMAIL_1", "'" + iADDRESS_LINE_1 + "'" );
      lVendors.put( "CITY_NAME", "'" + iCITY_NAME + "'" );
      lVendors.put( "COUNTRY_CD", "'" + iCOUNTRY_CD + "'" );
      lVendors.put( "TIMEZONE_CD", "'" + iTIMEZONE_CD_EST + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR, lVendors ) );

      // c_org_org_vendor
      lVendors.clear();
      lVendors.put( "ORG_CD", "'" + iORG_CD_1 + "'" );
      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ORG_ORG_VENDOR, lVendors ) );

      // c_org_vendor_po_type
      lVendors.clear();
      lVendors.put( "ORG_CD", "'" + iORG_CD_1 + "'" );
      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "PO_TYPE_CD", "'" + iPO_TYPE_CD_1 + "'" );
      lVendors.put( "VENDOR_STATUS_CD", "'" + iVENDOR_STATUS_CD_WARNING + "'" );
      lVendors.put( "APPROVAL_EXPIRY_DT", "SYSDATE+100" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ORG_VENDOR_PO_TYPE, lVendors ) );

      // c_org_vendor_service_type
      lVendors.clear();
      lVendors.put( "ORG_CD", "'" + iORG_CD_1 + "'" );
      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "SERVICE_TYPE_CD", "'" + iSERVICE_TYPE_CD + "'" );
      lVendors.put( "VENDOR_STATUS_CD", "'" + iVENDOR_STATUS_CD_WARNING + "'" );
      lVendors.put( "APPROVAL_EXPIRY_DT", "SYSDATE+100" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_ORG_VENDOR_SERVICE_TYPE, lVendors ) );

      // c_org_vendor_account
      lVendors.clear();
      lVendors.put( "VENDOR_CD", "'" + iVENDOR_CD_1 + "'" );
      lVendors.put( "VENDOR_ACCOUNT_CD", "'" + iVENDOR_ACCOUNT_CD + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_VENDOR_ACCOUNT, lVendors ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to create vendor
    *
    *
    */
   @Test
   public void test_Vendor_creation_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      test_Vendor_creation_validation();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // verify inv_loc
      simpleIDs lLOCTYPEIDs = getLOCTYPE( iLOC_TYPE_CD, iLOC_TYPE_SDESC );
      iLOC_IDs = VerifyINV_LOC( iVENDOR_CD_1, iVENDOR_NAME_BROKER, iADDRESS_LINE_1, iCITY_NAME,
            iTIMEZONE_CD_EST, iCOUNTRY_CD, lLOCTYPEIDs, "1" );

      // verify inv_owner
      simpleIDs lORGIds = getORGID( "ADMIN", iORG_CD_1 );
      iOWNER_IDs = VerifyINVOWNER( iVENDOR_CD_1, iVENDOR_NAME_BROKER, lORGIds );

      // Verify ORG_VENDOR table
      simpleIDs lvendorTypeID = getVendorTypeID( iVENDOR_TYPE_CD_BROKER, iVENDOR_TYPE_CD_BROKER );
      simpleIDs lCurrencyID = getCurrencyID( iCURRENCY_CD_USD, icurrency_USD_SDESC );

      iVENDOR_IDs = VerifyORGVENDOR( iVENDOR_CD_1, iVENDOR_NAME_BROKER, iLOC_IDs, lvendorTypeID,
            iOWNER_IDs, lCurrencyID, "1" );

      // verify inv_loc_org table
      String lQuery = "select 1 from " + TableUtil.INV_LOC_ORG + " where LOC_DB_ID="
            + iLOC_IDs.getNO_DB_ID() + " and LOC_ID=" + iLOC_IDs.getNO_ID() + " and ORG_DB_ID="
            + lORGIds.getNO_DB_ID() + " and ORG_ID=" + lORGIds.getNO_ID();
      Assert.assertTrue( "Check inv_loc_org table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify inv_loc_contact table
      lQuery = "select 1 from " + TableUtil.INV_LOC_CONTACT + " where LOC_DB_ID="
            + iLOC_IDs.getNO_DB_ID() + " and LOC_ID=" + iLOC_IDs.getNO_ID() + " and CONTACT_NAME='"
            + iCONTACT_NAME + "'";
      Assert.assertTrue( "Check inv_loc_contact table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify org_org_vendor table
      lQuery = "select 1 from " + TableUtil.ORG_ORG_VENDOR + " where ORG_DB_ID="
            + lORGIds.getNO_DB_ID() + " and ORG_ID=" + lORGIds.getNO_ID() + " and VENDOR_DB_ID="
            + iVENDOR_IDs.getNO_DB_ID() + " and VENDOR_ID=" + iVENDOR_IDs.getNO_ID();
      Assert.assertTrue( "Check org_org_vendor table to verify the record exists",
            RecordsExist( lQuery ) );

      // Verify org_vendor_po_type table
      simpleIDs lpotypeID = getPOTYPEID( iPO_TYPE_CD_1, iPO_TYPE_CD_1 );
      simpleIDs lvendorstatusID =
            getVendorStatusID( iVENDOR_STATUS_CD_WARNING, iVENDOR_STATUS_CD_WARNING_SDESC );
      verifyOrgVendorPOType( lORGIds, iVENDOR_IDs, lpotypeID, lvendorstatusID );

      // verify org_vendor_service_type table
      simpleIDs lserviceID = getServiceTypeID( iSERVICE_TYPE_CD, iSERVICE_TYPE_CD_TEST_SDESC );
      verifyOrgVendorSRVType( lORGIds, iVENDOR_IDs, lserviceID, lvendorstatusID );

      // verify org_vendor_account table
      lQuery = "select 1 from " + TableUtil.ORG_VENDOR_ACCOUNT + " where VENDOR_DB_ID="
            + iVENDOR_IDs.getNO_DB_ID() + " and VENDOR_ID=" + iVENDOR_IDs.getNO_ID()
            + " and ACCOUNT_CD='" + iVENDOR_ACCOUNT_CD + "'";
      Assert.assertTrue( "Check org_vendor_account table to verify the record exists",
            RecordsExist( lQuery ) );

   }


   // =====================================================

   /**
    * This function is to verify ORG_VENDOR_SERVICE_TYPE table
    *
    *
    */
   public void verifyOrgVendorSRVType( simpleIDs aORGIDs, simpleIDs aVENDORIDs,
         simpleIDs aSRVTYPEIDs, simpleIDs aVENDORSTATUSIDs ) {

      String[] iIds = { "APPROVAL_EXPIRY_DT" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "ORG_DB_ID", aORGIDs.getNO_DB_ID() );
      lArgs.addArguments( "ORG_ID", aORGIDs.getNO_ID() );
      lArgs.addArguments( "VENDOR_DB_ID", aVENDORIDs.getNO_DB_ID() );
      lArgs.addArguments( "VENDOR_ID", aVENDORIDs.getNO_ID() );
      lArgs.addArguments( "SERVICE_TYPE_DB_ID", aSRVTYPEIDs.getNO_DB_ID() );
      lArgs.addArguments( "SERVICE_TYPE_CD", aSRVTYPEIDs.getNO_ID() );
      lArgs.addArguments( "VENDOR_STATUS_DB_ID", aVENDORSTATUSIDs.getNO_DB_ID() );
      lArgs.addArguments( "VENDOR_STATUS_CD", aVENDORSTATUSIDs.getNO_ID() );

      String iQueryString =
            TableUtil.buildTableQuery( TableUtil.ORG_VENDOR_SERVICE_TYPE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertFalse( "APPROVAL_EXPIRY_DT", StringUtils.isBlank( llists.get( 0 ).get( 0 ) ) );

   }


   /**
    * This function is to get service type ids table
    *
    *
    */
   public simpleIDs getServiceTypeID( String aSERVICE_TYPE_CD, String aDESC_SDESC ) {

      String[] iIds = { "SERVICE_TYPE_DB_ID", "SERVICE_TYPE_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "SERVICE_TYPE_CD", aSERVICE_TYPE_CD );
      lArgs.addArguments( "DESC_SDESC", aDESC_SDESC );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.REF_SERVICE_TYPE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      simpleIDs lids = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lids;

   }


   /**
    * This function is to verify ORG_VENDOR_PO_TYPE table
    *
    *
    */
   public void verifyOrgVendorPOType( simpleIDs aORGIDs, simpleIDs aVENDORIDs, simpleIDs aPOTYPEIDs,
         simpleIDs aVENDORSTATUSIDs ) {

      String[] iIds = { "APPROVAL_EXPIRY_DT" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "ORG_DB_ID", aORGIDs.getNO_DB_ID() );
      lArgs.addArguments( "ORG_ID", aORGIDs.getNO_ID() );
      lArgs.addArguments( "VENDOR_DB_ID", aVENDORIDs.getNO_DB_ID() );
      lArgs.addArguments( "VENDOR_ID", aVENDORIDs.getNO_ID() );
      lArgs.addArguments( "PO_TYPE_DB_ID", aPOTYPEIDs.getNO_DB_ID() );
      lArgs.addArguments( "PO_TYPE_CD", aPOTYPEIDs.getNO_ID() );
      lArgs.addArguments( "VENDOR_STATUS_DB_ID", aVENDORSTATUSIDs.getNO_DB_ID() );
      lArgs.addArguments( "VENDOR_STATUS_CD", aVENDORSTATUSIDs.getNO_ID() );

      String iQueryString =
            TableUtil.buildTableQuery( TableUtil.ORG_VENDOR_PO_TYPE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertFalse( "APPROVAL_EXPIRY_DT", StringUtils.isBlank( llists.get( 0 ).get( 0 ) ) );

   }


   /**
    * This function is to get vendor status ids table
    *
    *
    */
   public simpleIDs getVendorStatusID( String aVENDOR_STATUS_CD, String aDESC_SDESC ) {

      String[] iIds = { "VENDOR_STATUS_DB_ID", "VENDOR_STATUS_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "VENDOR_STATUS_CD", aVENDOR_STATUS_CD );
      lArgs.addArguments( "DESC_SDESC", aDESC_SDESC );

      String iQueryString =
            TableUtil.buildTableQuery( TableUtil.REF_VENDOR_STATUS, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      simpleIDs lids = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lids;

   }


   /**
    * This function is to get po type ids table
    *
    *
    */
   public simpleIDs getPOTYPEID( String aPO_TYPE_CD, String aDESC_SDESC ) {

      String[] iIds = { "PO_TYPE_DB_ID", "PO_TYPE_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "PO_TYPE_CD", aPO_TYPE_CD );
      lArgs.addArguments( "DESC_SDESC", aDESC_SDESC );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.REF_PO_TYPE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      simpleIDs lids = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lids;

   }


   /**
    * This function is to get vendor ids table
    *
    *
    */
   public simpleIDs getCurrencyID( String aCURRENCY_CD, String aDESC_SDESC ) {

      String[] iIds = { "CURRENCY_DB_ID", "CURRENCY_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "CURRENCY_CD", aCURRENCY_CD );
      lArgs.addArguments( "DESC_SDESC", aDESC_SDESC );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.REF_CURRENCY, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      simpleIDs lids = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lids;

   }


   /**
    * This function is to get vendor ids table
    *
    *
    */
   public simpleIDs getVendorTypeID( String aVENDOR_TYPE_CD, String aDESC_LDESC ) {

      String[] iIds = { "VENDOR_TYPE_DB_ID", "VENDOR_TYPE_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "VENDOR_TYPE_CD", aVENDOR_TYPE_CD );
      lArgs.addArguments( "DESC_SDESC", aDESC_LDESC );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.REF_VENDOR_TYPE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      simpleIDs lids = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lids;

   }


   /**
    * This function is to get org ids table
    *
    *
    */
   public simpleIDs getORGID( String aORG_TYPE_CD, String aORG_CD ) {

      String[] iIds = { "ORG_DB_ID", "ORG_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "ORG_TYPE_CD", aORG_TYPE_CD );
      lArgs.addArguments( "ORG_CD", aORG_CD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.ORG_ORG, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      simpleIDs lids = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lids;

   }


   /**
    * This function is to verify inv_owner table
    *
    *
    */
   public simpleIDs VerifyINVOWNER( String aVENDOR_CD, String aVENDOR_NAME, simpleIDs aORGIDs ) {

      String[] iIds = { "OWNER_DB_ID", "OWNER_ID", "ORG_DB_ID", "ORG_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "OWNER_CD", aVENDOR_CD );
      lArgs.addArguments( "OWNER_NAME", aVENDOR_NAME );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.INV_OWNER, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      simpleIDs lids = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      Assert.assertTrue( "ORG_DB_ID",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aORGIDs.getNO_DB_ID() ) );
      Assert.assertTrue( "ORG_ID",
            llists.get( 0 ).get( 3 ).equalsIgnoreCase( aORGIDs.getNO_ID() ) );

      return lids;

   }


   /**
    * This function is to verify ORG_VENDOR table
    *
    *
    */
   public simpleIDs VerifyORGVENDOR( String aVENDOR_CD, String aVENDOR_NAME, simpleIDs aLocIDs,
         simpleIDs aVENDOR_TYPE_IDs, simpleIDs aOWNERIDs, simpleIDs aCurrencyIDs,
         String aNO_PRINT_REQ_BOOL ) {

      String[] iIds = { "VENDOR_DB_ID", "VENDOR_ID", "VENDOR_TYPE_DB_ID", "VENDOR_TYPE_CD",
            "OWNER_DB_ID", "OWNER_ID", "CURRENCY_DB_ID", "CURRENCY_CD", "NO_PRINT_REQ_BOOL" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "VENDOR_CD", aVENDOR_CD );
      lArgs.addArguments( "VENDOR_NAME", aVENDOR_NAME );
      lArgs.addArguments( "VENDOR_LOC_DB_ID", aLocIDs.getNO_DB_ID() );
      lArgs.addArguments( "VENDOR_LOC_ID", aLocIDs.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.ORG_VENDOR, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      simpleIDs lids = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      Assert.assertTrue( "VENDOR_TYPE_DB_ID",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aVENDOR_TYPE_IDs.getNO_DB_ID() ) );
      Assert.assertTrue( "VENDOR_TYPE_CD",
            llists.get( 0 ).get( 3 ).equalsIgnoreCase( aVENDOR_TYPE_IDs.getNO_ID() ) );
      Assert.assertTrue( "OWNER_DB_ID",
            llists.get( 0 ).get( 4 ).equalsIgnoreCase( aOWNERIDs.getNO_DB_ID() ) );
      Assert.assertTrue( "OWNER_ID",
            llists.get( 0 ).get( 5 ).equalsIgnoreCase( aOWNERIDs.getNO_ID() ) );
      Assert.assertTrue( "CURRENCY_DB_ID",
            llists.get( 0 ).get( 6 ).equalsIgnoreCase( aCurrencyIDs.getNO_DB_ID() ) );
      Assert.assertTrue( "CURRENCY_CD",
            llists.get( 0 ).get( 7 ).equalsIgnoreCase( aCurrencyIDs.getNO_ID() ) );
      Assert.assertTrue( "NO_PRINT_REQ_BOOL",
            llists.get( 0 ).get( 8 ).equalsIgnoreCase( aNO_PRINT_REQ_BOOL ) );

      return lids;

   }


   /**
    * This function is to verify inv_loc table
    *
    *
    */
   public simpleIDs VerifyINV_LOC( String aLOC_CD, String aLOC_NAME, String aADDRESS_PMAIL_1,
         String aCITY_NAME, String aTIMEZONE_CD, String aCOUNTRY_CD, simpleIDs aLOCTYPEIDs,
         String aSUPPLY_BOOL ) {

      String[] iIds = { "LOC_DB_ID", "LOC_ID", "LOC_TYPE_DB_ID", "LOC_TYPE_CD", "SUPPLY_LOC_DB_ID",
            "SUPPLY_LOC_ID", "SUPPLY_BOOL" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "LOC_CD", aLOC_CD );
      lArgs.addArguments( "LOC_NAME", aLOC_NAME );
      lArgs.addArguments( "ADDRESS_PMAIL_1", aADDRESS_PMAIL_1 );
      lArgs.addArguments( "CITY_NAME", aCITY_NAME );
      lArgs.addArguments( "COUNTRY_CD", aCOUNTRY_CD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.INV_LOC, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      simpleIDs lids = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      Assert.assertTrue( "LOC_TYPE_DB_ID",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aLOCTYPEIDs.getNO_DB_ID() ) );
      Assert.assertTrue( "LOC_TYPE_CD",
            llists.get( 0 ).get( 3 ).equalsIgnoreCase( aLOCTYPEIDs.getNO_ID() ) );
      Assert.assertTrue( "SUPPLY_LOC_DB_ID",
            llists.get( 0 ).get( 4 ).equalsIgnoreCase( llists.get( 0 ).get( 0 ) ) );
      Assert.assertTrue( "SUPPLY_LOC_ID",
            llists.get( 0 ).get( 5 ).equalsIgnoreCase( llists.get( 0 ).get( 1 ) ) );

      Assert.assertTrue( "SUPPLY_BOOL", llists.get( 0 ).get( 6 ).equalsIgnoreCase( aSUPPLY_BOOL ) );

      return lids;

   }


   /**
    * This function is to verify inv_loc table
    *
    *
    */
   public simpleIDs getLOCTYPE( String aLOC_TYPE_CD, String aDESC_SDESC ) {

      String[] iIds = { "LOC_TYPE_DB_ID", "LOC_TYPE_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "LOC_TYPE_CD", aLOC_TYPE_CD );
      lArgs.addArguments( "DESC_SDESC", aDESC_SDESC );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.REF_LOC_TYPE, lfields, lArgs );
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
      if ( iLOC_IDs != null ) {

         // delete INV_LOC
         String lStrDelete = "delete from " + TableUtil.INV_LOC + "  where LOC_DB_ID="
               + iLOC_IDs.getNO_DB_ID() + " and LOC_ID=" + iLOC_IDs.getNO_ID();
         executeSQL( lStrDelete );

         // delete INV_LOC_org
         lStrDelete = "delete from " + TableUtil.INV_LOC_ORG + "  where LOC_DB_ID="
               + iLOC_IDs.getNO_DB_ID() + " and LOC_ID=" + iLOC_IDs.getNO_ID();
         executeSQL( lStrDelete );

         // delete INV_LOC_contact
         lStrDelete = "delete from " + TableUtil.INV_LOC_CONTACT + "  where LOC_DB_ID="
               + iLOC_IDs.getNO_DB_ID() + " and LOC_ID=" + iLOC_IDs.getNO_ID();
         executeSQL( lStrDelete );

      }

      if ( iVENDOR_IDs != null ) {

         // delete ORG_VENDOR
         String lStrDelete = "delete from " + TableUtil.ORG_VENDOR + "  where VENDOR_DB_ID="
               + iVENDOR_IDs.getNO_DB_ID() + " and VENDOR_ID=" + iVENDOR_IDs.getNO_ID();
         executeSQL( lStrDelete );

         // delete ORG_ORG_VENDOR
         lStrDelete = "delete from " + TableUtil.ORG_ORG_VENDOR + "  where VENDOR_DB_ID="
               + iVENDOR_IDs.getNO_DB_ID() + " and VENDOR_ID=" + iVENDOR_IDs.getNO_ID();
         executeSQL( lStrDelete );

         // delete ORG_VENDOR_PO_TYPE
         lStrDelete = "delete from " + TableUtil.ORG_VENDOR_PO_TYPE + "  where VENDOR_DB_ID="
               + iVENDOR_IDs.getNO_DB_ID() + " and VENDOR_ID=" + iVENDOR_IDs.getNO_ID();
         executeSQL( lStrDelete );

         // delete ORG_VENDOR_SERVICE_TYPE
         lStrDelete = "delete from " + TableUtil.ORG_VENDOR_SERVICE_TYPE + "  where VENDOR_DB_ID="
               + iVENDOR_IDs.getNO_DB_ID() + " and VENDOR_ID=" + iVENDOR_IDs.getNO_ID();
         executeSQL( lStrDelete );

         // delete ORG_VENDOR_ACCOUNT
         lStrDelete = "delete from " + TableUtil.ORG_VENDOR_ACCOUNT + "  where VENDOR_DB_ID="
               + iVENDOR_IDs.getNO_DB_ID() + " and VENDOR_ID=" + iVENDOR_IDs.getNO_ID();
         executeSQL( lStrDelete );
      }

      if ( iOWNER_IDs != null ) {

         // delete INV_OWNER
         String lStrDelete = "delete from " + TableUtil.INV_OWNER + "  where OWNER_DB_ID="
               + iOWNER_IDs.getNO_DB_ID() + " and OWNER_ID=" + iOWNER_IDs.getNO_ID();
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
               CallableStatement lPrepareCallInventory = getConnection()
                     .prepareCall( "BEGIN vendor_import.validate_vendor(on_retcode => ?); END;" );

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
                     .prepareCall( "BEGIN vendor_import.import_vendor(on_retcode => ?); END;" );

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
