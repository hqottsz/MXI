package com.mxi.mx.core.maint.plan.baselineloader;

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
 * This test suite contains test cases on validation and import functionality of
 * C_STOCK_HEADER_IMPORT and C_STOCK_DETAILS_IMPORT package in stock header and details area.
 *
 * @author ALICIA QIAN
 */
public class Locations extends BaselineLoaderTest {

   @Rule
   public TestName testName = new TestName();

   String iAirport1CD = "AUTOAPT1";
   String iAirport2CD = "AUTOAPT2";
   String iAirport1NAME = "AUTOAPT1";
   String iAirport2NAME = "AUTOAPT2";
   String iAirport1Address1 = "APT1Address1";
   String iAirport1Address2 = "APT1Address2";
   String iAirport2Address1 = "APT2Address1";
   String iAirport2Address2 = "APT2Address2";
   String iCityName = "any city";
   String iStateCD = "HI";
   String iCountryCD = "USA";
   String iZipCD = "any zip";
   String iTimeZoneCD = "America/Fort_Wayne";

   String iStoreCD = "AUTOST1";
   String iStoreName = "AUTOSTNM";
   String iBinCD = "AUTOBIN";
   String iBinName = "AUTOBINNM";

   String iDock1CD = "AUTODOCK1";
   String iDock2CD = "AUTODOCK2";
   String iDock1NAME = "AUTODOCKNM1";
   String iDock2NAME = "AUTODOCKNM2";

   String iRepairCD = "AUTORP1";
   String iShopCD = "AUTOSHP";
   String iRepairName = "AUTORPNM";
   String iShopName = "AUTOSHPNM";

   String iLocTypeCD = "AIRPORT";
   String iLineLocTypeCD = "LINE";
   String iQUARLocTypeCD = "QUAR";
   String iSRVSTGLocTypeCD = "SRVSTG";
   String iUSSTGLocTypeCD = "USSTG";
   String iDOCKLocTypeCD = "DOCK";
   String iREPAIRLocTypeCD = "REPAIR";
   String iSHOPLocTypeCD = "SHOP";
   String iSRVSTORELocTypeCD = "SRVSTORE";
   String iBINLocTypeCD = "BIN";

   String iLineLOCName = "Line Maintenance";
   String iQuarLOCName = "Quarantine";
   String iSRVSTGLOCName = "Serviceable Staging";
   String iUSSTGLOCName = "Unserviceable Staging";

   String iHangerCD = "AUTOHR";
   String iHangerName = "AUTOHRNM";
   String iTrackCD = "AUTOTR";
   String iTrackName = "AUTOTRNM";
   String iHGRLocTypeCD = "HGR";
   String iTRKLocTypeCD = "TRACK";

   ValidationAndImport ivalidationandimport;

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
      iAirport1CD_24321 = get666Length( "AUTOAPT1" );
      iAirport2CD_24321 = get2000Length( "AUTOAPT2" );
      iHangerCD_24321 = get2000Length( "AUTOHR" );
      iTrackCD_24321 = get2000Length( "AUTOTR" );
      iRepairCD_24321 = get2000Length( "AUTORP1" );
      iShopCD_24321 = get2000Length( "AUTOSHP" );
      iStoreCD_24321 = get666Length( "AUTOST1" );
      iBinCD_24321 = get666Length( "AUTOBIN" );
      iDock1CD_24321 = get2000Length( "AUTODOCK1" );
      iDock2CD_24321 = get2000Length( "AUTODOCK2" );

   }


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


   @Test
   public void testVALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_loc_airport table
      Map<String, String> lloc = new LinkedHashMap<>();

      // airport1
      lloc.put( "AIRPORT_CD", "\'" + iAirport1CD + "\'" );
      lloc.put( "AIRPORT_NAME", "\'" + iAirport1NAME + "\'" );
      lloc.put( "HUB_AIRPORT_CD", "\'" + iAirport2CD + "\'" );
      lloc.put( "SHIPPING_TIME", "\'100\'" );
      lloc.put( "INBOUND_FLIGHTS_QT", "\'0\'" );
      lloc.put( "ADDRESS_PMAIL_1", "\'" + iAirport1Address1 + "\'" );
      lloc.put( "ADDRESS_PMAIL_2", "\'" + iAirport1Address2 + "\'" );
      lloc.put( "CITY_NAME", "\'" + iCityName + "\'" );
      lloc.put( "STATE_CD", "\'" + iStateCD + "\'" );
      lloc.put( "COUNTRY_CD", "\'" + iCountryCD + "\'" );
      lloc.put( "ZIP_CD", "\'" + iZipCD + "\'" );
      lloc.put( "TIMEZONE_CD", "\'" + iTimeZoneCD + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_LOC_AIRPORT, lloc ) );

      // airport2
      lloc.clear();
      lloc.put( "AIRPORT_CD", "\'" + iAirport2CD + "\'" );
      lloc.put( "AIRPORT_NAME", "\'" + iAirport2NAME + "\'" );
      lloc.put( "HUB_AIRPORT_CD", null );
      lloc.put( "SHIPPING_TIME", null );
      lloc.put( "INBOUND_FLIGHTS_QT", "\'0\'" );
      lloc.put( "ADDRESS_PMAIL_1", "\'" + iAirport2Address1 + "\'" );
      lloc.put( "ADDRESS_PMAIL_2", "\'" + iAirport2Address2 + "\'" );
      lloc.put( "CITY_NAME", "\'" + iCityName + "\'" );
      lloc.put( "STATE_CD", "\'" + iStateCD + "\'" );
      lloc.put( "COUNTRY_CD", "\'" + iCountryCD + "\'" );
      lloc.put( "ZIP_CD", "\'" + iZipCD + "\'" );
      lloc.put( "TIMEZONE_CD", "\'" + iTimeZoneCD + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_LOC_AIRPORT, lloc ) );

      // c_loc_bin table
      lloc.clear();
      lloc.put( "AIRPORT_CD", "\'" + iAirport1CD + "\'" );
      lloc.put( "STORE_CD", "\'" + iStoreCD + "\'" );
      lloc.put( "BIN_CD", "\'" + iBinCD + "\'" );
      lloc.put( "BIN_NAME", "\'" + iBinName + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_LOC_BIN, lloc ) );

      // c_loc_dock table
      // airport1
      lloc.clear();
      lloc.put( "AIRPORT_CD", "\'" + iAirport1CD + "\'" );
      lloc.put( "DOCK_CD", "\'" + iDock1CD + "\'" );
      lloc.put( "DOCK_NAME", "\'" + iDock1NAME + "\'" );
      lloc.put( "DEFAULT_DOCK_BOOL", "\'Y\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_LOC_DOCK, lloc ) );

      // airport2
      lloc.clear();
      lloc.put( "AIRPORT_CD", "\'" + iAirport2CD + "\'" );
      lloc.put( "DOCK_CD", "\'" + iDock2CD + "\'" );
      lloc.put( "DOCK_NAME", "\'" + iDock2NAME + "\'" );
      lloc.put( "DEFAULT_DOCK_BOOL", "\'Y\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_LOC_DOCK, lloc ) );

      // c_loc_repair
      lloc.clear();
      lloc.put( "AIRPORT_CD", "\'" + iAirport1CD + "\'" );
      lloc.put( "REPAIR_CD", "\'" + iRepairCD + "\'" );
      lloc.put( "SHOP_CD", "\'" + iShopCD + "\'" );
      lloc.put( "REPAIR_NAME", "\'" + iRepairName + "\'" );
      lloc.put( "SHOP_NAME", "\'" + iShopName + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_LOC_REPAIR, lloc ) );

      // c_loc_store
      lloc.clear();
      lloc.put( "AIRPORT_CD", "\'" + iAirport1CD + "\'" );
      lloc.put( "STORE_CD", "\'" + iStoreCD + "\'" );
      lloc.put( "STORE_NAME", "\'" + iStoreName + "\'" );
      lloc.put( "SRVSTORE_BOOL", "\'Y\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_LOC_STORE, lloc ) );

      // c_loc_track table
      lloc.clear();
      lloc.put( "AIRPORT_CD", "\'" + iAirport1CD + "\'" );
      lloc.put( "HANGER_CD", "\'" + iHangerCD + "\'" );
      lloc.put( "TRACK_CD", "\'" + iTrackCD + "\'" );
      lloc.put( "HANGER_NAME", "\'" + iHangerName + "\'" );
      lloc.put( "TRACK_NAME", "\'" + iTrackName + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_LOC_TRACK, lloc ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify OPER-24321: The location columns in baseline loader have a shorter
    * length than the location code in Maintenix.  This causes export to fail when a location code
    * exceeds the short length in baseline loader.
    *
    */

   @Test
   public void testOPER24321_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_loc_airport table
      Map<String, String> lloc = new LinkedHashMap<>();

      // airport1
      lloc.put( "AIRPORT_CD", "\'" + iAirport1CD_24321 + "\'" );
      lloc.put( "AIRPORT_NAME", "\'" + iAirport1NAME + "\'" );
      lloc.put( "HUB_AIRPORT_CD", "\'" + iAirport2CD_24321 + "\'" );
      lloc.put( "SHIPPING_TIME", "\'100\'" );
      lloc.put( "INBOUND_FLIGHTS_QT", "\'0\'" );
      lloc.put( "ADDRESS_PMAIL_1", "\'" + iAirport1Address1 + "\'" );
      lloc.put( "ADDRESS_PMAIL_2", "\'" + iAirport1Address2 + "\'" );
      lloc.put( "CITY_NAME", "\'" + iCityName + "\'" );
      lloc.put( "STATE_CD", "\'" + iStateCD + "\'" );
      lloc.put( "COUNTRY_CD", "\'" + iCountryCD + "\'" );
      lloc.put( "ZIP_CD", "\'" + iZipCD + "\'" );
      lloc.put( "TIMEZONE_CD", "\'" + iTimeZoneCD + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_LOC_AIRPORT, lloc ) );

      // airport2
      lloc.clear();
      lloc.put( "AIRPORT_CD", "\'" + iAirport2CD_24321 + "\'" );
      lloc.put( "AIRPORT_NAME", "\'" + iAirport2NAME + "\'" );
      lloc.put( "HUB_AIRPORT_CD", null );
      lloc.put( "SHIPPING_TIME", null );
      lloc.put( "INBOUND_FLIGHTS_QT", "\'0\'" );
      lloc.put( "ADDRESS_PMAIL_1", "\'" + iAirport2Address1 + "\'" );
      lloc.put( "ADDRESS_PMAIL_2", "\'" + iAirport2Address2 + "\'" );
      lloc.put( "CITY_NAME", "\'" + iCityName + "\'" );
      lloc.put( "STATE_CD", "\'" + iStateCD + "\'" );
      lloc.put( "COUNTRY_CD", "\'" + iCountryCD + "\'" );
      lloc.put( "ZIP_CD", "\'" + iZipCD + "\'" );
      lloc.put( "TIMEZONE_CD", "\'" + iTimeZoneCD + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_LOC_AIRPORT, lloc ) );

      // c_loc_bin table
      lloc.clear();
      lloc.put( "AIRPORT_CD", "\'" + iAirport1CD_24321 + "\'" );
      lloc.put( "STORE_CD", "\'" + iStoreCD_24321 + "\'" );
      lloc.put( "BIN_CD", "\'" + iBinCD_24321 + "\'" );
      lloc.put( "BIN_NAME", "\'" + iBinName + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_LOC_BIN, lloc ) );

      // c_loc_dock table
      // airport1
      lloc.clear();
      lloc.put( "AIRPORT_CD", "\'" + iAirport1CD_24321 + "\'" );
      lloc.put( "DOCK_CD", "\'" + iDock1CD_24321 + "\'" );
      lloc.put( "DOCK_NAME", "\'" + iDock1NAME + "\'" );
      lloc.put( "DEFAULT_DOCK_BOOL", "\'Y\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_LOC_DOCK, lloc ) );

      // airport2
      lloc.clear();
      lloc.put( "AIRPORT_CD", "\'" + iAirport2CD_24321 + "\'" );
      lloc.put( "DOCK_CD", "\'" + iDock2CD_24321 + "\'" );
      lloc.put( "DOCK_NAME", "\'" + iDock2NAME + "\'" );
      lloc.put( "DEFAULT_DOCK_BOOL", "\'Y\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_LOC_DOCK, lloc ) );

      // c_loc_repair
      lloc.clear();
      lloc.put( "AIRPORT_CD", "\'" + iAirport1CD_24321 + "\'" );
      lloc.put( "REPAIR_CD", "\'" + iRepairCD_24321 + "\'" );
      lloc.put( "SHOP_CD", "\'" + iShopCD_24321 + "\'" );
      lloc.put( "REPAIR_NAME", "\'" + iRepairName + "\'" );
      lloc.put( "SHOP_NAME", "\'" + iShopName + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_LOC_REPAIR, lloc ) );

      // c_loc_store
      lloc.clear();
      lloc.put( "AIRPORT_CD", "\'" + iAirport1CD_24321 + "\'" );
      lloc.put( "STORE_CD", "\'" + iStoreCD_24321 + "\'" );
      lloc.put( "STORE_NAME", "\'" + iStoreName + "\'" );
      lloc.put( "SRVSTORE_BOOL", "\'Y\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_LOC_STORE, lloc ) );

      // c_loc_track table
      lloc.clear();
      int length = iAirport1CD_24321.length();
      lloc.put( "AIRPORT_CD", "\'" + iAirport1CD_24321 + "\'" );
      lloc.put( "HANGER_CD", "\'" + iHangerCD_24321 + "\'" );
      lloc.put( "TRACK_CD", "\'" + iTrackCD_24321 + "\'" );
      lloc.put( "HANGER_NAME", "\'" + iHangerName + "\'" );
      lloc.put( "TRACK_NAME", "\'" + iTrackName + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_LOC_TRACK, lloc ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify C_STOCK_HEADER_IMPORT validation functionality of staging table
    * c_stock_header on INV_CLASS_CD=TRK
    *
    */
   @Test
   public void testIMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testVALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify inv_loc table for airport2
      simpleIDs lAirport2Ids = verifyINVLOC( iLocTypeCD, iAirport2CD, null, "1", null, null,
            iCountryCD, iStateCD, "0", null, iAirport2NAME, iAirport2Address1, iAirport2Address2,
            iCityName, iZipCD, iTimeZoneCD );
      // Check INV_LOC_ORG for airport2
      String lcheck = "Select 1 from INV_LOC_ORG where LOC_DB_ID=" + lAirport2Ids.getNO_DB_ID()
            + " and LOC_ID=" + lAirport2Ids.getNO_ID() + " and rownum=1";
      Assert.assertTrue( "Airport 2 is in INV_LOC_ORG", RecordsExist( lcheck ) == true );

      // Verify inv_loc table for airport1
      simpleIDs lAirport1Ids = verifyINVLOC( iLocTypeCD, iAirport1CD, null, "1", lAirport2Ids,
            "100", iCountryCD, iStateCD, "0", null, iAirport1NAME, iAirport1Address1,
            iAirport1Address2, iCityName, iZipCD, iTimeZoneCD );
      // Check INV_LOC_ORG for airport1
      lcheck = "Select 1 from INV_LOC_ORG where LOC_DB_ID=" + lAirport1Ids.getNO_DB_ID()
            + " and LOC_ID=" + lAirport1Ids.getNO_ID() + " and rownum=1";
      Assert.assertTrue( "Airport 1 is in INV_LOC_ORG", RecordsExist( lcheck ) == true );

      // Check LINE of airport1
      simpleIDs lLineAirport1Ids = verifyINVLOC( iLineLocTypeCD, iAirport1CD + "/" + iLineLocTypeCD,
            lAirport1Ids, "0", null, null, iCountryCD, iStateCD, null, lAirport1Ids, iLineLOCName,
            null, null, iCityName, iZipCD, iTimeZoneCD );
      // Check INV_LOC_ORG for airport1/Line
      lcheck = "Select 1 from INV_LOC_ORG where LOC_DB_ID=" + lLineAirport1Ids.getNO_DB_ID()
            + " and LOC_ID=" + lLineAirport1Ids.getNO_ID() + " and rownum=1";
      Assert.assertTrue( "Line of Airport 1 is in INV_LOC_ORG", RecordsExist( lcheck ) == true );

      // Check LINE of airport2
      simpleIDs lLineAirport2Ids = verifyINVLOC( iLineLocTypeCD, iAirport2CD + "/" + iLineLocTypeCD,
            lAirport2Ids, "0", null, null, iCountryCD, iStateCD, null, lAirport2Ids, iLineLOCName,
            null, null, iCityName, iZipCD, iTimeZoneCD );
      // Check INV_LOC_ORG for airport2/Line
      lcheck = "Select 1 from INV_LOC_ORG where LOC_DB_ID=" + lLineAirport2Ids.getNO_DB_ID()
            + " and LOC_ID=" + lLineAirport2Ids.getNO_ID() + " and rownum=1";
      Assert.assertTrue( "Line of Airport 2 is in INV_LOC_ORG", RecordsExist( lcheck ) == true );

      // Check QUAR of airport1
      simpleIDs lQuarAirport1Ids = verifyINVLOC( iQUARLocTypeCD, iAirport1CD + "/" + iQUARLocTypeCD,
            lAirport1Ids, "0", null, null, iCountryCD, iStateCD, null, lAirport1Ids, iQuarLOCName,
            null, null, iCityName, iZipCD, iTimeZoneCD );
      // Check INV_LOC_ORG for airport1/QUAR
      lcheck = "Select 1 from INV_LOC_ORG where LOC_DB_ID=" + lQuarAirport1Ids.getNO_DB_ID()
            + " and LOC_ID=" + lQuarAirport1Ids.getNO_ID() + " and rownum=1";
      Assert.assertTrue( "Quar of Airport 1 is in INV_LOC_ORG", RecordsExist( lcheck ) == true );

      // Check QUAR of airport2
      simpleIDs lQuarAirport2Ids = verifyINVLOC( iQUARLocTypeCD, iAirport2CD + "/" + iQUARLocTypeCD,
            lAirport2Ids, "0", null, null, iCountryCD, iStateCD, null, lAirport2Ids, iQuarLOCName,
            null, null, iCityName, iZipCD, iTimeZoneCD );
      // Check INV_LOC_ORG for airport2/QUAR
      lcheck = "Select 1 from INV_LOC_ORG where LOC_DB_ID=" + lQuarAirport2Ids.getNO_DB_ID()
            + " and LOC_ID=" + lQuarAirport2Ids.getNO_ID() + " and rownum=1";
      Assert.assertTrue( "Quar of Airport 2 is in INV_LOC_ORG", RecordsExist( lcheck ) == true );

      // Check SRVSTG of airport1
      simpleIDs lSRVSTGAirport1Ids =
            verifyINVLOC( iSRVSTGLocTypeCD, iAirport1CD + "/" + iSRVSTGLocTypeCD, lAirport1Ids, "0",
                  null, null, iCountryCD, iStateCD, null, lAirport1Ids, iSRVSTGLOCName, null, null,
                  iCityName, iZipCD, iTimeZoneCD );
      // Check INV_LOC_ORG for airport1/SRVSTG
      lcheck = "Select 1 from INV_LOC_ORG where LOC_DB_ID=" + lSRVSTGAirport1Ids.getNO_DB_ID()
            + " and LOC_ID=" + lSRVSTGAirport1Ids.getNO_ID() + " and rownum=1";
      Assert.assertTrue( "SRVSTG of Airport 1 is in INV_LOC_ORG", RecordsExist( lcheck ) == true );

      // Check SRVSTG of airport2
      simpleIDs lSRVSTGAirport2Ids =
            verifyINVLOC( iSRVSTGLocTypeCD, iAirport2CD + "/" + iSRVSTGLocTypeCD, lAirport2Ids, "0",
                  null, null, iCountryCD, iStateCD, null, lAirport2Ids, iSRVSTGLOCName, null, null,
                  iCityName, iZipCD, iTimeZoneCD );
      // Check INV_LOC_ORG for airport2/SRVSTG
      lcheck = "Select 1 from INV_LOC_ORG where LOC_DB_ID=" + lSRVSTGAirport2Ids.getNO_DB_ID()
            + " and LOC_ID=" + lSRVSTGAirport2Ids.getNO_ID() + " and rownum=1";
      Assert.assertTrue( "SRVSTG of Airport 2 is in INV_LOC_ORG", RecordsExist( lcheck ) == true );

      // Check USSTG of airport1
      simpleIDs lUSSTGAirport1Ids =
            verifyINVLOC( iUSSTGLocTypeCD, iAirport1CD + "/" + iUSSTGLocTypeCD, lAirport1Ids, "0",
                  null, null, iCountryCD, iStateCD, null, lAirport1Ids, iUSSTGLOCName, null, null,
                  iCityName, iZipCD, iTimeZoneCD );
      // Check INV_LOC_ORG for airport1/USSTG
      lcheck = "Select 1 from INV_LOC_ORG where LOC_DB_ID=" + lUSSTGAirport1Ids.getNO_DB_ID()
            + " and LOC_ID=" + lUSSTGAirport1Ids.getNO_ID() + " and rownum=1";
      Assert.assertTrue( "USSTG of Airport 1 is in INV_LOC_ORG", RecordsExist( lcheck ) == true );

      // Check USSTG of airport2
      simpleIDs lUSSTGAirport2Ids =
            verifyINVLOC( iUSSTGLocTypeCD, iAirport2CD + "/" + iUSSTGLocTypeCD, lAirport2Ids, "0",
                  null, null, iCountryCD, iStateCD, null, lAirport2Ids, iUSSTGLOCName, null, null,
                  iCityName, iZipCD, iTimeZoneCD );
      // Check INV_LOC_ORG for airport2/USSTG
      lcheck = "Select 1 from INV_LOC_ORG where LOC_DB_ID=" + lUSSTGAirport2Ids.getNO_DB_ID()
            + " and LOC_ID=" + lUSSTGAirport2Ids.getNO_ID() + " and rownum=1";
      Assert.assertTrue( "USSTG of Airport 2 is in INV_LOC_ORG", RecordsExist( lcheck ) == true );

      // Check DOCK of airport1
      simpleIDs lDockAirport1Ids = verifyINVLOC( iDOCKLocTypeCD, iAirport1CD + "/" + iDock1CD,
            lAirport1Ids, "0", null, null, iCountryCD, iStateCD, null, lAirport1Ids, iDock1NAME,
            null, null, iCityName, iZipCD, iTimeZoneCD, "1" );
      // Check INV_LOC_ORG for airport1/DOCK
      lcheck = "Select 1 from INV_LOC_ORG where LOC_DB_ID=" + lDockAirport1Ids.getNO_DB_ID()
            + " and LOC_ID=" + lDockAirport1Ids.getNO_ID() + " and rownum=1";
      Assert.assertTrue( "DOCK of Airport 1 is in INV_LOC_ORG", RecordsExist( lcheck ) == true );

      // Check DOCK of airport2
      simpleIDs lDockAirport2Ids = verifyINVLOC( iDOCKLocTypeCD, iAirport2CD + "/" + iDock2CD,
            lAirport2Ids, "0", null, null, iCountryCD, iStateCD, null, lAirport2Ids, iDock2NAME,
            null, null, iCityName, iZipCD, iTimeZoneCD, "1" );
      // Check INV_LOC_ORG for airport2/Dock
      lcheck = "Select 1 from INV_LOC_ORG where LOC_DB_ID=" + lDockAirport2Ids.getNO_DB_ID()
            + " and LOC_ID=" + lDockAirport2Ids.getNO_ID() + " and rownum=1";
      Assert.assertTrue( "DOCK of Airport 2 is in INV_LOC_ORG", RecordsExist( lcheck ) == true );

      // Check Repair of airport1
      simpleIDs lRepairAirport1Ids = verifyINVLOC( iREPAIRLocTypeCD, iAirport1CD + "/" + iRepairCD,
            lAirport1Ids, "0", null, null, iCountryCD, iStateCD, null, lAirport1Ids, iRepairName,
            null, null, iCityName, iZipCD, iTimeZoneCD, "0" );
      // Check INV_LOC_ORG for airport1/Repair
      lcheck = "Select 1 from INV_LOC_ORG where LOC_DB_ID=" + lRepairAirport1Ids.getNO_DB_ID()
            + " and LOC_ID=" + lRepairAirport1Ids.getNO_ID() + " and rownum=1";
      Assert.assertTrue( "Repair of Airport 1 is in INV_LOC_ORG", RecordsExist( lcheck ) == true );

      // Check SHOP of airport1
      simpleIDs lShopAirport1Ids =
            verifyINVLOC( iSHOPLocTypeCD, iAirport1CD + "/" + iRepairCD + "/" + iShopCD,
                  lAirport1Ids, "0", null, null, iCountryCD, iStateCD, null, lRepairAirport1Ids,
                  iShopName, null, null, iCityName, iZipCD, iTimeZoneCD, "0" );
      // Check INV_LOC_ORG for airport1/Shop
      lcheck = "Select 1 from INV_LOC_ORG where LOC_DB_ID=" + lShopAirport1Ids.getNO_DB_ID()
            + " and LOC_ID=" + lShopAirport1Ids.getNO_ID() + " and rownum=1";
      Assert.assertTrue( "Shop of Airport 1 is in INV_LOC_ORG", RecordsExist( lcheck ) == true );

      // Check SRVSTORE of airport1
      simpleIDs lSrvstoreAirport1Ids = verifyINVLOC( iSRVSTORELocTypeCD,
            iAirport1CD + "/" + iStoreCD, lAirport1Ids, "0", null, null, iCountryCD, iStateCD, null,
            lAirport1Ids, iStoreName, null, null, iCityName, iZipCD, iTimeZoneCD, "0" );
      // Check INV_LOC_ORG for airport1/SRVSTORE
      lcheck = "Select 1 from INV_LOC_ORG where LOC_DB_ID=" + lSrvstoreAirport1Ids.getNO_DB_ID()
            + " and LOC_ID=" + lSrvstoreAirport1Ids.getNO_ID() + " and rownum=1";
      Assert.assertTrue( "SRVSTORE of Airport 1 is in INV_LOC_ORG",
            RecordsExist( lcheck ) == true );

      // Check Bin of airport1
      simpleIDs lBinAirport1Ids =
            verifyINVLOC( iBINLocTypeCD, iAirport1CD + "/" + iStoreCD + "/" + iBinCD, lAirport1Ids,
                  "0", null, null, iCountryCD, iStateCD, null, lSrvstoreAirport1Ids, iBinName, null,
                  null, iCityName, iZipCD, iTimeZoneCD, "0" );
      // Check INV_LOC_ORG for airport1/Bin
      lcheck = "Select 1 from INV_LOC_ORG where LOC_DB_ID=" + lBinAirport1Ids.getNO_DB_ID()
            + " and LOC_ID=" + lBinAirport1Ids.getNO_ID() + " and rownum=1";
      Assert.assertTrue( "Binport 1 is in INV_LOC_ORG", RecordsExist( lcheck ) == true );

      // Check HGR of airport1
      simpleIDs lHGRAirport1Ids = verifyINVLOC( iHGRLocTypeCD, iAirport1CD + "/" + iHangerCD,
            lAirport1Ids, "0", null, null, iCountryCD, iStateCD, null, lAirport1Ids, iHangerName,
            null, null, iCityName, iZipCD, iTimeZoneCD, "0" );
      // Check INV_LOC_ORG for airport1/Bin
      lcheck = "Select 1 from INV_LOC_ORG where LOC_DB_ID=" + lHGRAirport1Ids.getNO_DB_ID()
            + " and LOC_ID=" + lHGRAirport1Ids.getNO_ID() + " and rownum=1";
      Assert.assertTrue( "HGR 1 is in INV_LOC_ORG", RecordsExist( lcheck ) == true );

      // Check TRACK of airport1
      simpleIDs lTRKAirport1Ids =
            verifyINVLOC( iTRKLocTypeCD, iAirport1CD + "/" + iHangerCD + "/" + iTrackCD,
                  lAirport1Ids, "0", null, null, iCountryCD, iStateCD, null, lHGRAirport1Ids,
                  iTrackName, null, null, iCityName, iZipCD, iTimeZoneCD, "0" );
      // Check INV_LOC_ORG for airport1/Bin
      lcheck = "Select 1 from INV_LOC_ORG where LOC_DB_ID=" + lTRKAirport1Ids.getNO_DB_ID()
            + " and LOC_ID=" + lTRKAirport1Ids.getNO_ID() + " and rownum=1";
      Assert.assertTrue( "Track 1 is in INV_LOC_ORG", RecordsExist( lcheck ) == true );

   }


   // =================================================================================================
   /**
    * This function is to verify inv_loc table
    *
    */

   public simpleIDs verifyINVLOC( String aLOCTYPECD, String aLOCCD, simpleIDs aSUPPLYLOC,
         String aSUPPLYBOOL, simpleIDs aHUBLOC, String aSHIPPINGTIME, String aCOUNTRYCD,
         String aSTATECD, String aINBOUNDFLIGHTQT, simpleIDs aNHLOC, String aLOCNAME,
         String aADDRESSPMAIL1, String aADDRESSPMAIL2, String aCITYNAME, String aZIPCD,
         String aTIMEZONECD ) {
      return verifyINVLOC( aLOCTYPECD, aLOCCD, aSUPPLYLOC, aSUPPLYBOOL, aHUBLOC, aSHIPPINGTIME,
            aCOUNTRYCD, aSTATECD, aINBOUNDFLIGHTQT, aNHLOC, aLOCNAME, aADDRESSPMAIL1,
            aADDRESSPMAIL2, aCITYNAME, aZIPCD, aTIMEZONECD, null );

   }


   /**
    * This function is to verify inv_loc table
    *
    */
   public simpleIDs verifyINVLOC( String aLOCTYPECD, String aLOCCD, simpleIDs aSUPPLYLOC,
         String aSUPPLYBOOL, simpleIDs aHUBLOC, String aSHIPPINGTIME, String aCOUNTRYCD,
         String aSTATECD, String aINBOUNDFLIGHTQT, simpleIDs aNHLOC, String aLOCNAME,
         String aADDRESSPMAIL1, String aADDRESSPMAIL2, String aCITYNAME, String aZIPCD,
         String aTIMEZONECD, String aDEFAULTDOCKBOOL ) {

      // INV_LOC table
      String[] iIds = { "LOC_DB_ID", "LOC_ID", "SUPPLY_LOC_DB_ID", "SUPPLY_LOC_ID", "SUPPLY_BOOL",
            "HUB_LOC_DB_ID", "HUB_LOC_ID", "SHIPPING_TIME", "COUNTRY_CD", "STATE_CD",
            "INBOUND_FLIGHTS_QT", "NH_LOC_DB_ID", "NH_LOC_ID", "LOC_NAME", "ADDRESS_PMAIL_1",
            "ADDRESS_PMAIL_2", "CITY_NAME", "ZIP_CD", "TIMEZONE_CD", "DEFAULT_DOCK_BOOL" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "LOC_TYPE_CD", aLOCTYPECD );
      lArgs.addArguments( "LOC_CD", aLOCCD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.INV_LOC, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Verification
      simpleIDs llocIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );
      if ( aSUPPLYLOC == null ) {
         Assert.assertTrue( "SUPPLY_LOC_DB_ID",
               llists.get( 0 ).get( 2 ).equalsIgnoreCase( llocIds.getNO_DB_ID() ) );
         Assert.assertTrue( "SUPPLY_LOC_ID",
               llists.get( 0 ).get( 3 ).equalsIgnoreCase( llocIds.getNO_ID() ) );
      } else {
         Assert.assertTrue( "SUPPLY_LOC_DB_ID",
               llists.get( 0 ).get( 2 ).equalsIgnoreCase( aSUPPLYLOC.getNO_DB_ID() ) );
         Assert.assertTrue( "SUPPLY_LOC_ID",
               llists.get( 0 ).get( 3 ).equalsIgnoreCase( aSUPPLYLOC.getNO_ID() ) );

      }

      Assert.assertTrue( "SUPPLY_BOOL", llists.get( 0 ).get( 4 ).equalsIgnoreCase( aSUPPLYBOOL ) );

      if ( aHUBLOC != null ) {
         Assert.assertTrue( "HUB_LOC_DB_ID",
               llists.get( 0 ).get( 5 ).equalsIgnoreCase( aHUBLOC.getNO_DB_ID() ) );
         Assert.assertTrue( "HUB_LOC_ID",
               llists.get( 0 ).get( 6 ).equalsIgnoreCase( aHUBLOC.getNO_ID() ) );

      }

      if ( aSHIPPINGTIME != null ) {
         Assert.assertTrue( "SHIPPING_TIME",
               llists.get( 0 ).get( 7 ).equalsIgnoreCase( aSHIPPINGTIME ) );

      }

      Assert.assertTrue( "COUNTRY_CD", llists.get( 0 ).get( 8 ).equalsIgnoreCase( aCOUNTRYCD ) );
      Assert.assertTrue( "STATE_CD", llists.get( 0 ).get( 9 ).equalsIgnoreCase( aSTATECD ) );

      if ( aINBOUNDFLIGHTQT != null ) {
         Assert.assertTrue( "INBOUND_FLIGHTS_QT",
               llists.get( 0 ).get( 10 ).equalsIgnoreCase( aINBOUNDFLIGHTQT ) );
      }

      if ( aNHLOC != null ) {
         Assert.assertTrue( "NH_LOC_DB_ID",
               llists.get( 0 ).get( 11 ).equalsIgnoreCase( aNHLOC.getNO_DB_ID() ) );
         Assert.assertTrue( "NH_LOC_ID",
               llists.get( 0 ).get( 12 ).equalsIgnoreCase( aNHLOC.getNO_ID() ) );
      }

      Assert.assertTrue( "LOC_NAME", llists.get( 0 ).get( 13 ).equalsIgnoreCase( aLOCNAME ) );

      if ( aADDRESSPMAIL1 != null ) {
         Assert.assertTrue( "ADDRESS_PMAIL_1",
               llists.get( 0 ).get( 14 ).equalsIgnoreCase( aADDRESSPMAIL1 ) );
      }

      if ( aADDRESSPMAIL2 != null ) {
         Assert.assertTrue( "ADDRESS_PMAIL_2",
               llists.get( 0 ).get( 15 ).equalsIgnoreCase( aADDRESSPMAIL2 ) );
      }
      Assert.assertTrue( "CITY_NAME", llists.get( 0 ).get( 16 ).equalsIgnoreCase( aCITYNAME ) );
      Assert.assertTrue( "ZIP_CD", llists.get( 0 ).get( 17 ).equalsIgnoreCase( aZIPCD ) );
      Assert.assertTrue( "TIMEZONE_CD", llists.get( 0 ).get( 18 ).equalsIgnoreCase( aTIMEZONECD ) );

      if ( aDEFAULTDOCKBOOL != null ) {
         Assert.assertTrue( "DEFAULT_DOCK_BOOL",
               llists.get( 0 ).get( 19 ).equalsIgnoreCase( aDEFAULTDOCKBOOL ) );
      }

      return llocIds;

   }


   /**
    * This function is to get string with 2000 length
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
    * This function is to get string with 666 length
    *
    */
   public String get666Length( String aHead ) {

      String ls = aHead;
      int length = aHead.length();
      for ( int i = length; i < 666; i++ ) {
         ls = ls + "A";

      }

      return ls;

   }


   /**
    * This function is called by after test method to restore data which has been modified by tests.
    *
    *
    */

   public void RestoreData() {

      // INV_LOC table
      String[] iIds = { "LOC_DB_ID", "LOC_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "CITY_NAME", iCityName );
      lArgs.addArguments( "ZIP_CD", iZipCD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.INV_LOC, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get All IDs which need to be deleted from INV_LOC_ORG
      ArrayList<simpleIDs> lIds = new ArrayList<>();

      for ( int i = 0; i < llists.size(); i++ ) {
         simpleIDs lid = new simpleIDs( llists.get( i ).get( 0 ), llists.get( i ).get( 1 ) );
         lIds.add( lid );

      }

      // Delete records from INV_LOC_ORG
      for ( int i = 0; i < lIds.size(); i++ ) {
         String lstrToDelete = "delete from " + TableUtil.INV_LOC_ORG + " where LOC_DB_ID="
               + lIds.get( i ).getNO_DB_ID() + " and LOC_ID=" + lIds.get( i ).getNO_ID();
         executeSQL( lstrToDelete );
      }

      String lstr = "delete from " + TableUtil.INV_LOC + " where CITY_NAME='" + iCityName
            + "' and ZIP_CD='" + iZipCD + "'";
      executeSQL( lstr );

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
                     "BEGIN location_import.validate_location(on_retcode => ?); END;" );

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
                     .prepareCall( "BEGIN location_import.import_location(on_retcode => ?); END;" );

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
