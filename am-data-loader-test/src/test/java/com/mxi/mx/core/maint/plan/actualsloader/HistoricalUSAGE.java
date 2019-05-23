package com.mxi.mx.core.maint.plan.actualsloader;

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

import com.mxi.mx.core.maint.plan.datamodels.inventoryData;
import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;
import com.mxi.mx.core.maint.plan.datamodels.usageInfor;
import com.mxi.mx.util.ActualsLoaderTest;
import com.mxi.mx.util.BatchFileManager;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.TestConstants;
import com.mxi.mx.util.ValidationAndImport;
import com.mxi.mx.util.WhereClause;


/**
 * This test suite contains test cases on validation and import functionality of
 * AL_HISTORICAL_USAGE_IMPORT package.
 *
 * @author ALICIA QIAN
 */
public class HistoricalUSAGE extends ActualsLoaderTest {

   @Rule
   public TestName testName = new TestName();

   ValidationAndImport ivalidationandimport;

   public String iSN_ACFT = "SN000014";
   public String iSN_ENG = "SN000008";
   public String iSN_APU = "SN000006";
   public String iPN_ACFT = "ACFT_ASSY_PN1";
   public String iPN_ENG = "ENG_ASSY_PN1";
   public String iPN_APU = "APU_ASSY_PN1";
   public String iMANUAL_ACFT = "10001";
   // This value has been changed to validate the upper case and trim change for OPER=21977
   public String iMANUAL_ENG = "abc11 ";
   public String iMANUAL_APU = "1234567890";
   public String iDATA_TYPE_CD_1 = "AUTOUSAGE1";
   public String iDATA_TYPE_CD_2 = "AUTOUSAGE2";
   public String iUSAGE_QT_1 = "100";
   public String iUSAGE_QT_2 = "90";
   public String iUSAGE_DELTA_QT_1 = "10";
   public String iUSAGE_DELTA_QT_2 = "90";
   public String iUSAGE_DESC_1 = "AUTOUSAGEDESC1";
   public String iUSAGE_DESC_2 = "AUTOUSAGEDESC2";

   public String iACFTASSMBL_CD = "ACFT_CD1";
   public String iENGASSMBL_BOM_CD = "ENG-ASSY";
   public String iAPUASSMBL_BOM_CD = "APU-ASSY";

   public String iSN_TRK = "SN000016";
   public String iPN_TRK = "A0000001";
   public String iMANUAL_TRK = "10001";

   simpleIDs iPNIDs = null;


   /**
    * Setup before executing each individual test
    *
    */
   @Before
   @Override
   public void before() throws Exception {

      super.before();
      clearActualsLoaderTables();

      String strTCName = testName.getMethodName();
      if ( strTCName.contains( "ALAHU_00220" ) ) {
         dataSetupDataType();
      } else if ( strTCName.contains( "ALAHU_00130" ) ) {
         dataSetupLockInv();

      } else if ( strTCName.contains( "ALAHU_00200" ) ) {
         dataSetupDupPNOEM();
      }

   }


   /**
    * Clean up after each individual test
    */
   @After
   @Override
   public void after() {

      try {
         RestoreData();
         super.after();
      } catch ( Exception e ) {
         e.printStackTrace();
      }
   }


   /**
    * This test is to verify AL_HISTORICAL_USAGE_IMPORT validation functionality of staging table
    * AL_HISTORICAL_USAGE. USAGE on ACFT.
    *
    *
    */

   public void testACFTUSAGE_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // AL_HISTORICAL_USAGE
      Map<String, String> lUsage = new LinkedHashMap<>();

      lUsage.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lUsage.put( "PART_NO_OEM", "\'" + iPN_ACFT + "\'" );
      lUsage.put( "MANUFACT_CD", "\'" + iMANUAL_ACFT + "\'" );
      lUsage.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_1 + "\'" );
      lUsage.put( "USAGE_QT", "\'" + iUSAGE_QT_1 + "\'" );
      lUsage.put( "USAGE_DELTA_QT", "\'" + iUSAGE_DELTA_QT_1 + "\'" );
      lUsage.put( "USAGE_DESC", "\'" + iUSAGE_DESC_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_HISTORICAL_USAGE, lUsage ) );

      lUsage.clear();
      lUsage.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lUsage.put( "PART_NO_OEM", "\'" + iPN_ACFT + "\'" );
      lUsage.put( "MANUFACT_CD", "\'" + iMANUAL_ACFT + "\'" );
      lUsage.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_1 + "\'" );
      lUsage.put( "USAGE_QT", "\'" + iUSAGE_QT_2 + "\'" );
      lUsage.put( "USAGE_DELTA_QT", "\'" + iUSAGE_DELTA_QT_2 + "\'" );
      lUsage.put( "USAGE_DESC", "\'" + iUSAGE_DESC_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_HISTORICAL_USAGE, lUsage ) );

      setCollectionDate( "01/02/2017", iUSAGE_DESC_1 );
      setCollectionDate( "01/03/2016", iUSAGE_DESC_2 );

      // run validation
      Assert.assertTrue( runValidationAndImport( true ) == 1 );

   }


   /**
    * This test is to verify AL_HISTORICAL_USAGE_IMPORT import functionality of staging table
    * AL_HISTORICAL_USAGE. USAGE on ACFT.
    *
    *
    */
   @Test
   public void testACFTUSAGE_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testACFTUSAGE_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false ) == 1 );

      // Verify usg_usage_data
      simpleIDs lInvIds = getInvIDs( iSN_ACFT );
      simpleIDs lDataTypeIds = getDataTypeIDs( iDATA_TYPE_CD_1 );
      usageInfor lUsage1 = new usageInfor( "100", "100", "100", "10", "10", "10" );
      usageInfor lUsage2 = new usageInfor( "90", "90", "90", "90", "90", "90" );
      VerifyUSGUSAGEDATA( iACFTASSMBL_CD, "0", 2, lInvIds, lDataTypeIds, lUsage1, lUsage2 );

      // Verify usg_usage_record
      String lQuery = "Select 1 from USG_USAGE_RECORD where USAGE_DESC='" + iUSAGE_DESC_1
            + "' and INV_NO_DB_ID=" + lInvIds.getNO_DB_ID() + " and INV_NO_ID="
            + lInvIds.getNO_ID();
      Assert.assertTrue( "check " + iUSAGE_DESC_1 + " exist", RecordsExist( lQuery ) );

      lQuery = "Select 1 from USG_USAGE_RECORD where USAGE_DESC='" + iUSAGE_DESC_2
            + "' and INV_NO_DB_ID=" + lInvIds.getNO_DB_ID() + " and INV_NO_ID="
            + lInvIds.getNO_ID();
      Assert.assertTrue( "check " + iUSAGE_DESC_2 + " exist", RecordsExist( lQuery ) );

   }


   /**
    * This test is to verify AL_HISTORICAL_USAGE_IMPORT validation functionality of staging table
    * AL_HISTORICAL_USAGE. USAGE on ENG.
    *
    *
    */

   public void testENGUSAGE_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // AL_HISTORICAL_USAGE
      Map<String, String> lUsage = new LinkedHashMap<>();

      lUsage.put( "SERIAL_NO_OEM", "\'" + iSN_ENG + "\'" );
      lUsage.put( "PART_NO_OEM", "\'" + iPN_ENG + "\'" );
      lUsage.put( "MANUFACT_CD", "\'" + iMANUAL_ENG + "\'" );
      lUsage.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_2 + "\'" );
      lUsage.put( "USAGE_QT", "\'" + iUSAGE_QT_1 + "\'" );
      lUsage.put( "USAGE_DELTA_QT", "\'" + iUSAGE_DELTA_QT_1 + "\'" );
      lUsage.put( "USAGE_DESC", "\'" + iUSAGE_DESC_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_HISTORICAL_USAGE, lUsage ) );

      lUsage.clear();
      lUsage.put( "SERIAL_NO_OEM", "\'" + iSN_ENG + "\'" );
      lUsage.put( "PART_NO_OEM", "\'" + iPN_ENG + "\'" );
      lUsage.put( "MANUFACT_CD", "\'" + iMANUAL_ENG + "\'" );
      lUsage.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_2 + "\'" );
      lUsage.put( "USAGE_QT", "\'" + iUSAGE_QT_2 + "\'" );
      lUsage.put( "USAGE_DELTA_QT", "\'" + iUSAGE_DELTA_QT_2 + "\'" );
      lUsage.put( "USAGE_DESC", "\'" + iUSAGE_DESC_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_HISTORICAL_USAGE, lUsage ) );

      setCollectionDate( "01/02/2017", iUSAGE_DESC_1 );
      setCollectionDate( "01/03/2016", iUSAGE_DESC_2 );

      // run validation
      Assert.assertTrue( runValidationAndImport( true ) == 1 );

   }


   /**
    * This test is to verify AL_HISTORICAL_USAGE_IMPORT import functionality of staging table
    * AL_HISTORICAL_USAGE. USAGE on ENG.
    *
    *
    */
   @Test
   public void testENGUSAGE_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testENGUSAGE_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false ) == 1 );

      // Verify usg_usage_data
      simpleIDs lInvIds = getInvIDs( iSN_ENG );
      simpleIDs lDataTypeIds = getDataTypeIDs( iDATA_TYPE_CD_2 );
      usageInfor lUsage1 = new usageInfor( "100", "100", "100", "10", "10", "10" );
      usageInfor lUsage2 = new usageInfor( "90", "90", "90", "90", "90", "90" );

      String lBOM_ID = getAssmblBOMID( iACFTASSMBL_CD, iENGASSMBL_BOM_CD );
      VerifyUSGUSAGEDATA( iACFTASSMBL_CD, lBOM_ID, 2, lInvIds, lDataTypeIds, lUsage1, lUsage2 );

      // Verify usg_usafe_record
      String lQuery = "Select 1 from USG_USAGE_RECORD where USAGE_DESC='" + iUSAGE_DESC_1
            + "' and INV_NO_DB_ID=" + lInvIds.getNO_DB_ID() + " and INV_NO_ID="
            + lInvIds.getNO_ID();
      Assert.assertTrue( "check " + iUSAGE_DESC_1 + " exist", RecordsExist( lQuery ) );

      lQuery = "Select 1 from USG_USAGE_RECORD where USAGE_DESC='" + iUSAGE_DESC_2
            + "' and INV_NO_DB_ID=" + lInvIds.getNO_DB_ID() + " and INV_NO_ID="
            + lInvIds.getNO_ID();
      Assert.assertTrue( "check " + iUSAGE_DESC_2 + " exist", RecordsExist( lQuery ) );

   }


   /**
    * This test is to verify AL_HISTORICAL_USAGE_IMPORT validation functionality of staging table
    * AL_HISTORICAL_USAGE. USAGE on APU.
    *
    *
    */

   public void testAPUUSAGE_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // AL_HISTORICAL_USAGE
      Map<String, String> lUsage = new LinkedHashMap<>();

      lUsage.put( "SERIAL_NO_OEM", "\'" + iSN_APU + "\'" );
      lUsage.put( "PART_NO_OEM", "\'" + iPN_APU + "\'" );
      lUsage.put( "MANUFACT_CD", "\'" + iMANUAL_APU + "\'" );
      lUsage.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_2 + "\'" );
      lUsage.put( "USAGE_QT", "\'" + iUSAGE_QT_1 + "\'" );
      lUsage.put( "USAGE_DELTA_QT", "\'" + iUSAGE_DELTA_QT_1 + "\'" );
      lUsage.put( "USAGE_DESC", "\'" + iUSAGE_DESC_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_HISTORICAL_USAGE, lUsage ) );

      lUsage.clear();
      lUsage.put( "SERIAL_NO_OEM", "\'" + iSN_APU + "\'" );
      lUsage.put( "PART_NO_OEM", "\'" + iPN_APU + "\'" );
      lUsage.put( "MANUFACT_CD", "\'" + iMANUAL_APU + "\'" );
      lUsage.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_2 + "\'" );
      lUsage.put( "USAGE_QT", "\'" + iUSAGE_QT_2 + "\'" );
      lUsage.put( "USAGE_DELTA_QT", "\'" + iUSAGE_DELTA_QT_2 + "\'" );
      lUsage.put( "USAGE_DESC", "\'" + iUSAGE_DESC_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_HISTORICAL_USAGE, lUsage ) );

      setCollectionDate( "01/02/2017", iUSAGE_DESC_1 );
      setCollectionDate( "01/03/2016", iUSAGE_DESC_2 );

      // run validation
      Assert.assertTrue( runValidationAndImport( true ) == 1 );

   }


   /**
    * This test is to verify AL_HISTORICAL_USAGE_IMPORT import functionality of staging table
    * AL_HISTORICAL_USAGE. USAGE on APU.
    *
    *
    */

   @Test
   public void testAPUUSAGE_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testAPUUSAGE_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false ) == 1 );

      // Verify usg_usage_data
      simpleIDs lInvIds = getInvIDs( iSN_APU );
      simpleIDs lDataTypeIds = getDataTypeIDs( iDATA_TYPE_CD_2 );
      usageInfor lUsage1 = new usageInfor( "100", "100", "100", "10", "10", "10" );
      usageInfor lUsage2 = new usageInfor( "90", "90", "90", "90", "90", "90" );

      String lBOM_ID = getAssmblBOMID( iACFTASSMBL_CD, iAPUASSMBL_BOM_CD );
      VerifyUSGUSAGEDATA( iACFTASSMBL_CD, lBOM_ID, 2, lInvIds, lDataTypeIds, lUsage1, lUsage2 );

      // Verify usg_usafe_record
      String lQuery = "Select 1 from USG_USAGE_RECORD where USAGE_DESC='" + iUSAGE_DESC_1
            + "' and INV_NO_DB_ID=" + lInvIds.getNO_DB_ID() + " and INV_NO_ID="
            + lInvIds.getNO_ID();
      Assert.assertTrue( "check " + iUSAGE_DESC_1 + " exist", RecordsExist( lQuery ) );

      lQuery = "Select 1 from USG_USAGE_RECORD where USAGE_DESC='" + iUSAGE_DESC_2
            + "' and INV_NO_DB_ID=" + lInvIds.getNO_DB_ID() + " and INV_NO_ID="
            + lInvIds.getNO_ID();
      Assert.assertTrue( "check " + iUSAGE_DESC_2 + " exist", RecordsExist( lQuery ) );

   }


   /**
    * This test is to verify AL_HISTORICAL_USAGE_IMPORT validation functionality of staging table
    * AL_HISTORICAL_USAGE. USAGE on ACFT, manufacture_cd is null.
    *
    *
    */

   public void testACFTUSAGE_MANUFACT_NULL_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // AL_HISTORICAL_USAGE
      Map<String, String> lUsage = new LinkedHashMap<>();

      lUsage.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lUsage.put( "PART_NO_OEM", "\'" + iPN_ACFT + "\'" );
      // lUsage.put( "MANUFACT_CD", "\'" + iMANUAL_ACFT + "\'" );
      lUsage.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_1 + "\'" );
      lUsage.put( "USAGE_QT", "\'" + iUSAGE_QT_1 + "\'" );
      lUsage.put( "USAGE_DELTA_QT", "\'" + iUSAGE_DELTA_QT_1 + "\'" );
      lUsage.put( "USAGE_DESC", "\'" + iUSAGE_DESC_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_HISTORICAL_USAGE, lUsage ) );

      lUsage.clear();
      lUsage.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lUsage.put( "PART_NO_OEM", "\'" + iPN_ACFT + "\'" );
      // lUsage.put( "MANUFACT_CD", "\'" + iMANUAL_ACFT + "\'" );
      lUsage.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_1 + "\'" );
      lUsage.put( "USAGE_QT", "\'" + iUSAGE_QT_2 + "\'" );
      lUsage.put( "USAGE_DELTA_QT", "\'" + iUSAGE_DELTA_QT_2 + "\'" );
      lUsage.put( "USAGE_DESC", "\'" + iUSAGE_DESC_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_HISTORICAL_USAGE, lUsage ) );

      setCollectionDate( "01/02/2017", iUSAGE_DESC_1 );
      setCollectionDate( "01/03/2016", iUSAGE_DESC_2 );

      // run validation
      Assert.assertTrue( runValidationAndImport( true ) == 1 );

   }


   /**
    * This test is to verify AL_HISTORICAL_USAGE_IMPORT import functionality of staging table
    * AL_HISTORICAL_USAGE. USAGE on ACFT, manufacture_cd is null.
    *
    *
    */
   @Test
   public void testACFTUSAGE_MANUFACT_NULL_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testACFTUSAGE_MANUFACT_NULL_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false ) == 1 );

      // Verify usg_usage_data
      simpleIDs lInvIds = getInvIDs( iSN_ACFT );
      simpleIDs lDataTypeIds = getDataTypeIDs( iDATA_TYPE_CD_1 );
      usageInfor lUsage1 = new usageInfor( "100", "100", "100", "10", "10", "10" );
      usageInfor lUsage2 = new usageInfor( "90", "90", "90", "90", "90", "90" );
      VerifyUSGUSAGEDATA( iACFTASSMBL_CD, "0", 2, lInvIds, lDataTypeIds, lUsage1, lUsage2 );

      // Verify usg_usafe_record
      String lQuery = "Select 1 from USG_USAGE_RECORD where USAGE_DESC='" + iUSAGE_DESC_1
            + "' and INV_NO_DB_ID=" + lInvIds.getNO_DB_ID() + " and INV_NO_ID="
            + lInvIds.getNO_ID();
      Assert.assertTrue( "check " + iUSAGE_DESC_1 + " exist", RecordsExist( lQuery ) );

      lQuery = "Select 1 from USG_USAGE_RECORD where USAGE_DESC='" + iUSAGE_DESC_2
            + "' and INV_NO_DB_ID=" + lInvIds.getNO_DB_ID() + " and INV_NO_ID="
            + lInvIds.getNO_ID();
      Assert.assertTrue( "check " + iUSAGE_DESC_2 + " exist", RecordsExist( lQuery ) );

   }


   /**
    * This test is to verify AL_HISTORICAL_USAGE_IMPORT validation functionality of staging table
    * AL_HISTORICAL_USAGE. USAGE on ACFT and ENG.
    *
    *
    */

   public void testMultipleUSAGE_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // AL_HISTORICAL_USAGE
      Map<String, String> lUsage = new LinkedHashMap<>();

      lUsage.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lUsage.put( "PART_NO_OEM", "\'" + iPN_ACFT + "\'" );
      lUsage.put( "MANUFACT_CD", "\'" + iMANUAL_ACFT + "\'" );
      lUsage.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_1 + "\'" );
      lUsage.put( "USAGE_QT", "\'" + iUSAGE_QT_1 + "\'" );
      lUsage.put( "USAGE_DELTA_QT", "\'" + iUSAGE_QT_1 + "\'" );
      lUsage.put( "USAGE_DESC", "\'" + iUSAGE_DESC_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_HISTORICAL_USAGE, lUsage ) );

      lUsage.clear();
      lUsage.put( "SERIAL_NO_OEM", "\'" + iSN_ENG + "\'" );
      lUsage.put( "PART_NO_OEM", "\'" + iPN_ENG + "\'" );
      lUsage.put( "MANUFACT_CD", "\'" + iMANUAL_ENG + "\'" );
      lUsage.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_2 + "\'" );
      lUsage.put( "USAGE_QT", "\'" + iUSAGE_QT_1 + "\'" );
      lUsage.put( "USAGE_DELTA_QT", "\'" + iUSAGE_QT_1 + "\'" );
      lUsage.put( "USAGE_DESC", "\'" + iUSAGE_DESC_2 + "\'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_HISTORICAL_USAGE, lUsage ) );

      setCollectionDate( "01/02/2017", iUSAGE_DESC_1 );
      setCollectionDate( "01/03/2016", iUSAGE_DESC_2 );

      // run validation
      Assert.assertTrue( runValidationAndImport( true ) == 1 );

   }


   /**
    * This test is to verify AL_HISTORICAL_USAGE_IMPORT validation functionality of staging table
    * AL_HISTORICAL_USAGE. USAGE on ACFT and ENG.
    *
    *
    */

   @Test
   public void testMultipleUSAGE_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testMultipleUSAGE_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false ) == 1 );

      // Verify usg_usage_data for acft
      simpleIDs lInvIds1 = getInvIDs( iSN_ACFT );
      simpleIDs lDataTypeIds = getDataTypeIDs( iDATA_TYPE_CD_1 );
      usageInfor lUsage1 = new usageInfor( "100", "100", "100", "100", "100", "100" );
      VerifyUSGUSAGEDATA( iACFTASSMBL_CD, "0", 1, lInvIds1, lDataTypeIds, lUsage1, lUsage1 );

      // Verify usg_usage_data for eng
      simpleIDs lInvIds2 = getInvIDs( iSN_ENG );
      lDataTypeIds = getDataTypeIDs( iDATA_TYPE_CD_2 );

      String lBOM_ID = getAssmblBOMID( iACFTASSMBL_CD, iENGASSMBL_BOM_CD );
      VerifyUSGUSAGEDATA( iACFTASSMBL_CD, lBOM_ID, 1, lInvIds2, lDataTypeIds, lUsage1, lUsage1 );

      // Verify usg_usafe_record
      String lQuery = "Select 1 from USG_USAGE_RECORD where USAGE_DESC='" + iUSAGE_DESC_1
            + "' and INV_NO_DB_ID=" + lInvIds1.getNO_DB_ID() + " and INV_NO_ID="
            + lInvIds1.getNO_ID();
      Assert.assertTrue( "check " + iUSAGE_DESC_1 + " exist", RecordsExist( lQuery ) );

      lQuery = "Select 1 from USG_USAGE_RECORD where USAGE_DESC='" + iUSAGE_DESC_2
            + "' and INV_NO_DB_ID=" + lInvIds2.getNO_DB_ID() + " and INV_NO_ID="
            + lInvIds2.getNO_ID();
      Assert.assertTrue( "check " + iUSAGE_DESC_2 + " exist", RecordsExist( lQuery ) );

   }


   /**
    * This test is to verify a.load_usage.bat is to load data from csv file to staging table
    * properly.
    *
    *
    */
   @Test
   public void testLoadCSV() {

      BatchFileManager lFileMgr = new BatchFileManager();
      lFileMgr.copyFile( TestConstants.TEST_CASE_DATA, TestConstants.HISTORICAL_USAGE_CSV_FILE,
            TestConstants.HISTORICAL_USAGE_BATCH_FILE + "\\data\\" );
      lFileMgr.loadHistoricalUsageViaDataFile( TestConstants.HISTORICAL_USAGE_CSV_FILE );

      // Verify AL_HISTORICAL_USAGE is loaded
      VerifyStagingTable();

   }


   /**
    * This test is to verify a.load_usage.bat works exactly same way as
    * c_open_deferred_fault_import.validate_open_deferred_fault .
    *
    *
    */
   @Test
   public void testValidateCSV() {

      testLoadCSV();
      BatchFileManager lFileMgr = new BatchFileManager();
      lFileMgr.validateHistoricalUsageViaBatch();
      VerifyValidation();

   }


   /**
    * This test is to verify a.load_usage.bat works exactly same way as
    * c_open_deferred_fault_import.validate_open_deferred_fault .
    *
    *
    */
   @Test
   public void testImportCSV() {

      testValidateCSV();
      BatchFileManager lFileMgr = new BatchFileManager();
      lFileMgr.importHistoricalUsageViaBatch();

      VerifyImport();

   }


   /**
    * This test is to verify error code ALAHU-00020:SERIAL_NO_OEM must be provided
    *
    */
   @Test
   public void test_ALAHU_00020_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // AL_HISTORICAL_USAGE
      Map<String, String> lUsage = new LinkedHashMap<>();

      // lUsage.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lUsage.put( "PART_NO_OEM", "\'" + iPN_ACFT + "\'" );
      lUsage.put( "MANUFACT_CD", "\'" + iMANUAL_ACFT + "\'" );
      lUsage.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_1 + "\'" );
      lUsage.put( "USAGE_QT", "\'" + iUSAGE_QT_1 + "\'" );
      lUsage.put( "USAGE_DELTA_QT", "\'" + iUSAGE_DELTA_QT_1 + "\'" );
      lUsage.put( "USAGE_DESC", "\'" + iUSAGE_DESC_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_HISTORICAL_USAGE, lUsage ) );

      lUsage.clear();
      // lUsage.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lUsage.put( "PART_NO_OEM", "\'" + iPN_ACFT + "\'" );
      lUsage.put( "MANUFACT_CD", "\'" + iMANUAL_ACFT + "\'" );
      lUsage.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_1 + "\'" );
      lUsage.put( "USAGE_QT", "\'" + iUSAGE_QT_2 + "\'" );
      lUsage.put( "USAGE_DELTA_QT", "\'" + iUSAGE_DELTA_QT_2 + "\'" );
      lUsage.put( "USAGE_DESC", "\'" + iUSAGE_DESC_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_HISTORICAL_USAGE, lUsage ) );

      setCollectionDate( "01/02/2017", iUSAGE_DESC_1 );
      setCollectionDate( "01/03/2016", iUSAGE_DESC_2 );

      // run validation
      Assert.assertTrue( runValidationAndImport( true ) == 1 );
      ErrorCodeValidation( "ALAHU-00020" );

   }


   /**
    * This test is to verify error code ALAHU-00030:PART_NO_OEM must be provided
    *
    *
    */

   @Test
   public void test_ALAHU_00030_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // AL_HISTORICAL_USAGE
      Map<String, String> lUsage = new LinkedHashMap<>();

      lUsage.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      // lUsage.put( "PART_NO_OEM", "\'" + iPN_ACFT + "\'" );
      lUsage.put( "MANUFACT_CD", "\'" + iMANUAL_ACFT + "\'" );
      lUsage.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_1 + "\'" );
      lUsage.put( "USAGE_QT", "\'" + iUSAGE_QT_1 + "\'" );
      lUsage.put( "USAGE_DELTA_QT", "\'" + iUSAGE_DELTA_QT_1 + "\'" );
      lUsage.put( "USAGE_DESC", "\'" + iUSAGE_DESC_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_HISTORICAL_USAGE, lUsage ) );

      lUsage.clear();
      lUsage.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      // lUsage.put( "PART_NO_OEM", "\'" + iPN_ACFT + "\'" );
      lUsage.put( "MANUFACT_CD", "\'" + iMANUAL_ACFT + "\'" );
      lUsage.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_1 + "\'" );
      lUsage.put( "USAGE_QT", "\'" + iUSAGE_QT_2 + "\'" );
      lUsage.put( "USAGE_DELTA_QT", "\'" + iUSAGE_DELTA_QT_2 + "\'" );
      lUsage.put( "USAGE_DESC", "\'" + iUSAGE_DESC_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_HISTORICAL_USAGE, lUsage ) );

      setCollectionDate( "01/02/2017", iUSAGE_DESC_1 );
      setCollectionDate( "01/03/2016", iUSAGE_DESC_2 );

      // run validation
      Assert.assertTrue( runValidationAndImport( true ) == 1 );
      ErrorCodeValidation( "ALAHU-00030" );

   }


   /**
    * This test is to verify error code ALAHU-00040:COLLECTION_DT must be provided
    *
    *
    */

   @Test
   public void test_ALAHU_00040_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // AL_HISTORICAL_USAGE
      Map<String, String> lUsage = new LinkedHashMap<>();

      lUsage.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lUsage.put( "PART_NO_OEM", "\'" + iPN_ACFT + "\'" );
      lUsage.put( "MANUFACT_CD", "\'" + iMANUAL_ACFT + "\'" );
      lUsage.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_1 + "\'" );
      lUsage.put( "USAGE_QT", "\'" + iUSAGE_QT_1 + "\'" );
      lUsage.put( "USAGE_DELTA_QT", "\'" + iUSAGE_DELTA_QT_1 + "\'" );
      lUsage.put( "USAGE_DESC", "\'" + iUSAGE_DESC_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_HISTORICAL_USAGE, lUsage ) );

      lUsage.clear();
      lUsage.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lUsage.put( "PART_NO_OEM", "\'" + iPN_ACFT + "\'" );
      lUsage.put( "MANUFACT_CD", "\'" + iMANUAL_ACFT + "\'" );
      lUsage.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_1 + "\'" );
      lUsage.put( "USAGE_QT", "\'" + iUSAGE_QT_2 + "\'" );
      lUsage.put( "USAGE_DELTA_QT", "\'" + iUSAGE_DELTA_QT_2 + "\'" );
      lUsage.put( "USAGE_DESC", "\'" + iUSAGE_DESC_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_HISTORICAL_USAGE, lUsage ) );

      // setCollectionDate( "01/02/2017", iUSAGE_DESC_1 );
      // setCollectionDate( "01/03/2016", iUSAGE_DESC_2 );

      // run validation
      Assert.assertTrue( runValidationAndImport( true ) == 1 );
      ErrorCodeValidation( "ALAHU-00040" );

   }


   /**
    * This test is to verify error code ALAHU-00050:USAGE_QT must be provided
    *
    *
    */

   @Test
   public void test_ALAHU_00050_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // AL_HISTORICAL_USAGE
      Map<String, String> lUsage = new LinkedHashMap<>();

      lUsage.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lUsage.put( "PART_NO_OEM", "\'" + iPN_ACFT + "\'" );
      lUsage.put( "MANUFACT_CD", "\'" + iMANUAL_ACFT + "\'" );
      lUsage.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_1 + "\'" );
      // lUsage.put( "USAGE_QT", "\'" + iUSAGE_QT_1 + "\'" );
      lUsage.put( "USAGE_DELTA_QT", "\'" + iUSAGE_DELTA_QT_1 + "\'" );
      lUsage.put( "USAGE_DESC", "\'" + iUSAGE_DESC_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_HISTORICAL_USAGE, lUsage ) );

      lUsage.clear();
      lUsage.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lUsage.put( "PART_NO_OEM", "\'" + iPN_ACFT + "\'" );
      lUsage.put( "MANUFACT_CD", "\'" + iMANUAL_ACFT + "\'" );
      lUsage.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_1 + "\'" );
      // lUsage.put( "USAGE_QT", "\'" + iUSAGE_QT_2 + "\'" );
      lUsage.put( "USAGE_DELTA_QT", "\'" + iUSAGE_DELTA_QT_2 + "\'" );
      lUsage.put( "USAGE_DESC", "\'" + iUSAGE_DESC_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_HISTORICAL_USAGE, lUsage ) );

      setCollectionDate( "01/02/2017", iUSAGE_DESC_1 );
      setCollectionDate( "01/03/2016", iUSAGE_DESC_2 );

      // run validation
      Assert.assertTrue( runValidationAndImport( true ) == 1 );
      ErrorCodeValidation( "ALAHU-00050" );

   }


   /**
    * This test is to verify error code ALAHU-00060:USAGE_DELTA_QT must be provided
    *
    *
    */

   @Test
   public void test_ALAHU_00060_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // AL_HISTORICAL_USAGE
      Map<String, String> lUsage = new LinkedHashMap<>();

      lUsage.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lUsage.put( "PART_NO_OEM", "\'" + iPN_ACFT + "\'" );
      lUsage.put( "MANUFACT_CD", "\'" + iMANUAL_ACFT + "\'" );
      lUsage.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_1 + "\'" );
      lUsage.put( "USAGE_QT", "\'" + iUSAGE_QT_1 + "\'" );
      // lUsage.put( "USAGE_DELTA_QT", "\'" + iUSAGE_DELTA_QT_1 + "\'" );
      lUsage.put( "USAGE_DESC", "\'" + iUSAGE_DESC_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_HISTORICAL_USAGE, lUsage ) );

      lUsage.clear();
      lUsage.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lUsage.put( "PART_NO_OEM", "\'" + iPN_ACFT + "\'" );
      lUsage.put( "MANUFACT_CD", "\'" + iMANUAL_ACFT + "\'" );
      lUsage.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_1 + "\'" );
      lUsage.put( "USAGE_QT", "\'" + iUSAGE_QT_2 + "\'" );
      // lUsage.put( "USAGE_DELTA_QT", "\'" + iUSAGE_DELTA_QT_2 + "\'" );
      lUsage.put( "USAGE_DESC", "\'" + iUSAGE_DESC_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_HISTORICAL_USAGE, lUsage ) );

      setCollectionDate( "01/02/2017", iUSAGE_DESC_1 );
      setCollectionDate( "01/03/2016", iUSAGE_DESC_2 );

      // run validation
      Assert.assertTrue( runValidationAndImport( true ) == 1 );
      ErrorCodeValidation( "ALAHU-00060" );

   }


   /**
    * This test is to verify error code ALAHU-00070:Specified Inventory must be ACFT or ASSY
    *
    *
    */
   @Test
   public void test_ALAHU_00070_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // AL_HISTORICAL_USAGE
      Map<String, String> lUsage = new LinkedHashMap<>();

      lUsage.put( "SERIAL_NO_OEM", "\'" + iSN_TRK + "\'" );
      lUsage.put( "PART_NO_OEM", "\'" + iPN_TRK + "\'" );
      lUsage.put( "MANUFACT_CD", "\'" + iMANUAL_TRK + "\'" );
      lUsage.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_1 + "\'" );
      lUsage.put( "USAGE_QT", "\'" + iUSAGE_QT_1 + "\'" );
      lUsage.put( "USAGE_DELTA_QT", "\'" + iUSAGE_DELTA_QT_1 + "\'" );
      lUsage.put( "USAGE_DESC", "\'" + iUSAGE_DESC_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_HISTORICAL_USAGE, lUsage ) );

      setCollectionDate( "01/02/2017", iUSAGE_DESC_1 );

      // run validation
      Assert.assertTrue( runValidationAndImport( true ) == 1 );
      ErrorCodeValidation( "ALAHU-00070" );

   }


   /**
    * This test is to verify error code ALAHU-00080:he other records (rows) corresponding to the
    * inventory in the staging table failed validation. To load this usage for this inventory,
    * please resolve errors in other records for same inventory.
    *
    *
    */

   @Test
   public void test_ALAHU_00080_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // AL_HISTORICAL_USAGE
      Map<String, String> lUsage = new LinkedHashMap<>();

      lUsage.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lUsage.put( "PART_NO_OEM", "\'" + iPN_ACFT + "\'" );
      lUsage.put( "MANUFACT_CD", "\'" + iMANUAL_ACFT + "\'" );
      lUsage.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_1 + "\'" );
      lUsage.put( "USAGE_QT", "\'" + iUSAGE_QT_1 + "\'" );
      lUsage.put( "USAGE_DELTA_QT", "\'" + iUSAGE_DELTA_QT_1 + "\'" );
      lUsage.put( "USAGE_DESC", "\'" + iUSAGE_DESC_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_HISTORICAL_USAGE, lUsage ) );

      // second
      lUsage.clear();
      lUsage.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lUsage.put( "PART_NO_OEM", "\'" + iPN_ACFT + "\'" );
      lUsage.put( "MANUFACT_CD", "\'" + iMANUAL_ACFT + "\'" );
      lUsage.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_1 + "\'" );
      lUsage.put( "USAGE_QT", "\'" + iUSAGE_QT_2 + "\'" );
      lUsage.put( "USAGE_DELTA_QT", "\'" + iUSAGE_DELTA_QT_2 + "\'" );
      lUsage.put( "USAGE_DESC", "\'" + iUSAGE_DESC_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_HISTORICAL_USAGE, lUsage ) );

      // third
      lUsage.clear();
      lUsage.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lUsage.put( "PART_NO_OEM", "\'" + iPN_ACFT + "\'" );
      lUsage.put( "MANUFACT_CD", "\'" + iMANUAL_ACFT + "\'" );
      lUsage.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_1 + "\'" );
      lUsage.put( "USAGE_QT", "\'" + iUSAGE_QT_2 + "\'" );
      lUsage.put( "USAGE_DELTA_QT", "\'" + iUSAGE_DELTA_QT_2 + "\'" );
      lUsage.put( "USAGE_DESC", "\'" + iUSAGE_DESC_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_HISTORICAL_USAGE, lUsage ) );

      setCollectionDate( "01/02/2017", iUSAGE_DESC_1 );
      setCollectionDate( "01/03/2016", iUSAGE_DESC_2 );

      // run validation
      Assert.assertTrue( runValidationAndImport( true ) == 1 );
      ErrorCodeValidation( "ALAHU-00080" );

   }


   /**
    * This test is to verify error code ALAHU-00090:Data type code is not defined in Maintenix
    *
    *
    */

   @Test
   public void test_ALAHU_00090_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // AL_HISTORICAL_USAGE
      Map<String, String> lUsage = new LinkedHashMap<>();

      lUsage.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lUsage.put( "PART_NO_OEM", "\'" + iPN_ACFT + "\'" );
      lUsage.put( "MANUFACT_CD", "\'" + iMANUAL_ACFT + "\'" );
      lUsage.put( "DATA_TYPE_CD", "'test'" );
      lUsage.put( "USAGE_QT", "\'" + iUSAGE_QT_1 + "\'" );
      lUsage.put( "USAGE_DELTA_QT", "\'" + iUSAGE_DELTA_QT_1 + "\'" );
      lUsage.put( "USAGE_DESC", "\'" + iUSAGE_DESC_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_HISTORICAL_USAGE, lUsage ) );

      lUsage.clear();
      lUsage.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lUsage.put( "PART_NO_OEM", "\'" + iPN_ACFT + "\'" );
      lUsage.put( "MANUFACT_CD", "\'" + iMANUAL_ACFT + "\'" );
      lUsage.put( "DATA_TYPE_CD", "'test'" );
      lUsage.put( "USAGE_QT", "\'" + iUSAGE_QT_2 + "\'" );
      lUsage.put( "USAGE_DELTA_QT", "\'" + iUSAGE_DELTA_QT_2 + "\'" );
      lUsage.put( "USAGE_DESC", "\'" + iUSAGE_DESC_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_HISTORICAL_USAGE, lUsage ) );

      setCollectionDate( "01/02/2017", iUSAGE_DESC_1 );
      setCollectionDate( "01/03/2016", iUSAGE_DESC_2 );

      // run validation
      Assert.assertTrue( runValidationAndImport( true ) == 1 );
      ErrorCodeValidation( "ALAHU-00090" );

   }


   /**
    * This test is to verify error code ALAHU-00100:PART_NO_OEM must exist in Maintenix
    *
    *
    */

   @Test
   public void test_ALAHU_00100_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // AL_HISTORICAL_USAGE
      Map<String, String> lUsage = new LinkedHashMap<>();

      lUsage.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lUsage.put( "PART_NO_OEM", "'testNo'" );
      lUsage.put( "MANUFACT_CD", "\'" + iMANUAL_ACFT + "\'" );
      lUsage.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_1 + "\'" );
      lUsage.put( "USAGE_QT", "\'" + iUSAGE_QT_1 + "\'" );
      lUsage.put( "USAGE_DELTA_QT", "\'" + iUSAGE_DELTA_QT_1 + "\'" );
      lUsage.put( "USAGE_DESC", "\'" + iUSAGE_DESC_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_HISTORICAL_USAGE, lUsage ) );

      lUsage.clear();
      lUsage.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lUsage.put( "PART_NO_OEM", "'testNo'" );
      lUsage.put( "MANUFACT_CD", "\'" + iMANUAL_ACFT + "\'" );
      lUsage.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_1 + "\'" );
      lUsage.put( "USAGE_QT", "\'" + iUSAGE_QT_2 + "\'" );
      lUsage.put( "USAGE_DELTA_QT", "\'" + iUSAGE_DELTA_QT_2 + "\'" );
      lUsage.put( "USAGE_DESC", "\'" + iUSAGE_DESC_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_HISTORICAL_USAGE, lUsage ) );

      setCollectionDate( "01/02/2017", iUSAGE_DESC_1 );
      setCollectionDate( "01/03/2016", iUSAGE_DESC_2 );

      // run validation
      Assert.assertTrue( runValidationAndImport( true ) == 1 );
      ErrorCodeValidation( "ALAHU-00100" );

   }


   /**
    * This test is to verify error code ALAHU-00110:PART_NO_OEM / MANUFACT_CD combination must exist
    * in Maintenix
    *
    *
    */
   @Test
   public void test_ALAHU_00110_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // AL_HISTORICAL_USAGE
      Map<String, String> lUsage = new LinkedHashMap<>();

      lUsage.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lUsage.put( "PART_NO_OEM", "\'" + iPN_ACFT + "\'" );
      lUsage.put( "MANUFACT_CD", "\'" + iMANUAL_ENG + "\'" );
      lUsage.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_1 + "\'" );
      lUsage.put( "USAGE_QT", "\'" + iUSAGE_QT_1 + "\'" );
      lUsage.put( "USAGE_DELTA_QT", "\'" + iUSAGE_DELTA_QT_1 + "\'" );
      lUsage.put( "USAGE_DESC", "\'" + iUSAGE_DESC_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_HISTORICAL_USAGE, lUsage ) );

      lUsage.clear();
      lUsage.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lUsage.put( "PART_NO_OEM", "\'" + iPN_ACFT + "\'" );
      lUsage.put( "MANUFACT_CD", "\'" + iMANUAL_ENG + "\'" );
      lUsage.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_1 + "\'" );
      lUsage.put( "USAGE_QT", "\'" + iUSAGE_QT_2 + "\'" );
      lUsage.put( "USAGE_DELTA_QT", "\'" + iUSAGE_DELTA_QT_2 + "\'" );
      lUsage.put( "USAGE_DESC", "\'" + iUSAGE_DESC_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_HISTORICAL_USAGE, lUsage ) );

      setCollectionDate( "01/02/2017", iUSAGE_DESC_1 );
      setCollectionDate( "01/03/2016", iUSAGE_DESC_2 );

      // run validation
      Assert.assertTrue( runValidationAndImport( true ) == 1 );
      ErrorCodeValidation( "ALAHU-00110" );

   }


   /**
    * This test is to verify error code ALAHU-00120:The INVENTORY item referenced by fields
    * SERIAL_NO_OEM/PART_NO_OEM/MANUFACT_CD is not present in Maintenix table OR combination of
    * SERIAL_NO_OEM/PART_NO_OEM/MANUFACT_CD is not valid entry in Maintenix.
    *
    *
    */

   @Test
   public void test_ALAHU_00120_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // AL_HISTORICAL_USAGE
      Map<String, String> lUsage = new LinkedHashMap<>();

      lUsage.put( "SERIAL_NO_OEM", "\'" + iSN_TRK + "\'" );
      lUsage.put( "PART_NO_OEM", "\'" + iPN_ACFT + "\'" );
      lUsage.put( "MANUFACT_CD", "\'" + iMANUAL_ACFT + "\'" );
      lUsage.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_1 + "\'" );
      lUsage.put( "USAGE_QT", "\'" + iUSAGE_QT_1 + "\'" );
      lUsage.put( "USAGE_DELTA_QT", "\'" + iUSAGE_DELTA_QT_1 + "\'" );
      lUsage.put( "USAGE_DESC", "\'" + iUSAGE_DESC_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_HISTORICAL_USAGE, lUsage ) );

      setCollectionDate( "01/02/2017", iUSAGE_DESC_1 );

      // run validation
      Assert.assertTrue( runValidationAndImport( true ) == 1 );
      ErrorCodeValidation( "ALAHU-00120" );

   }


   /**
    * This test is to verify error code ALAHU-00130:Assembly inventory defined by SERIAL_NO_OEM
    * cannot be locked in Maintenix
    *
    *
    */

   @Test
   public void test_ALAHU_00130_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // AL_HISTORICAL_USAGE
      Map<String, String> lUsage = new LinkedHashMap<>();

      lUsage.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lUsage.put( "PART_NO_OEM", "\'" + iPN_ACFT + "\'" );
      lUsage.put( "MANUFACT_CD", "\'" + iMANUAL_ACFT + "\'" );
      lUsage.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_1 + "\'" );
      lUsage.put( "USAGE_QT", "\'" + iUSAGE_QT_1 + "\'" );
      lUsage.put( "USAGE_DELTA_QT", "\'" + iUSAGE_DELTA_QT_1 + "\'" );
      lUsage.put( "USAGE_DESC", "\'" + iUSAGE_DESC_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_HISTORICAL_USAGE, lUsage ) );

      setCollectionDate( "01/02/2017", iUSAGE_DESC_1 );

      // run validation
      Assert.assertTrue( runValidationAndImport( true ) == 1 );
      ErrorCodeValidation( "ALAHU-00130" );

   }


   /**
    * This test is to verify error code ALAHU-00140:COLLECTION_DT cannot be set to a future date
    *
    *
    */
   @Test
   public void test_ALAHU_00140_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // AL_HISTORICAL_USAGE
      Map<String, String> lUsage = new LinkedHashMap<>();

      lUsage.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lUsage.put( "PART_NO_OEM", "\'" + iPN_ACFT + "\'" );
      lUsage.put( "MANUFACT_CD", "\'" + iMANUAL_ACFT + "\'" );
      lUsage.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_1 + "\'" );
      lUsage.put( "USAGE_QT", "\'" + iUSAGE_QT_1 + "\'" );
      lUsage.put( "USAGE_DELTA_QT", "\'" + iUSAGE_DELTA_QT_1 + "\'" );
      lUsage.put( "USAGE_DESC", "\'" + iUSAGE_DESC_1 + "\'" );
      lUsage.put( "COLLECTION_DT", "SYSDATE+1" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_HISTORICAL_USAGE, lUsage ) );

      lUsage.clear();
      lUsage.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lUsage.put( "PART_NO_OEM", "\'" + iPN_ACFT + "\'" );
      lUsage.put( "MANUFACT_CD", "\'" + iMANUAL_ACFT + "\'" );
      lUsage.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_1 + "\'" );
      lUsage.put( "USAGE_QT", "\'" + iUSAGE_QT_2 + "\'" );
      lUsage.put( "USAGE_DELTA_QT", "\'" + iUSAGE_DELTA_QT_2 + "\'" );
      lUsage.put( "USAGE_DESC", "\'" + iUSAGE_DESC_2 + "\'" );
      lUsage.put( "COLLECTION_DT", "SYSDATE+1" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_HISTORICAL_USAGE, lUsage ) );

      // setCollectionDate( "01/02/2017", iUSAGE_DESC_1 );
      // setCollectionDate( "01/03/2016", iUSAGE_DESC_2 );

      // run validation
      Assert.assertTrue( runValidationAndImport( true ) == 1 );
      ErrorCodeValidation( "ALAHU-00140" );

   }


   /**
    * This test is to verify error code ALAHU-00150:Current Usage for the datatype must exist in
    * Maintenix for the specified SERIAL_NO_OEM
    *
    *
    */
   @Test
   public void test_ALAHU_00150_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // AL_HISTORICAL_USAGE
      Map<String, String> lUsage = new LinkedHashMap<>();

      lUsage.put( "SERIAL_NO_OEM", "\'" + iSN_TRK + "\'" );
      lUsage.put( "PART_NO_OEM", "\'" + iPN_TRK + "\'" );
      lUsage.put( "MANUFACT_CD", "\'" + iMANUAL_TRK + "\'" );
      lUsage.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_1 + "\'" );
      lUsage.put( "USAGE_QT", "\'" + iUSAGE_QT_1 + "\'" );
      lUsage.put( "USAGE_DELTA_QT", "\'" + iUSAGE_DELTA_QT_1 + "\'" );
      lUsage.put( "USAGE_DESC", "\'" + iUSAGE_DESC_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_HISTORICAL_USAGE, lUsage ) );

      setCollectionDate( "01/02/2017", iUSAGE_DESC_1 );

      // run validation
      Assert.assertTrue( runValidationAndImport( true ) == 1 );
      ErrorCodeValidation( "ALAHU-00150" );

   }


   /**
    * This test is to verify error code ALAHU-00160:The USAGE_QT minus the USAGE_DELTA_QT must equal
    * to the USAGE_QT of the previous Usage Record defined by
    * SERIAL_NO_OEM/PART_NO_OEM/MANUFACT_CD/DATA_TYPE_CD.
    *
    *
    */

   @Test
   public void test_ALAHU_00160_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // AL_HISTORICAL_USAGE
      Map<String, String> lUsage = new LinkedHashMap<>();

      lUsage.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lUsage.put( "PART_NO_OEM", "\'" + iPN_ACFT + "\'" );
      lUsage.put( "MANUFACT_CD", "\'" + iMANUAL_ACFT + "\'" );
      lUsage.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_1 + "\'" );
      lUsage.put( "USAGE_QT", "\'" + iUSAGE_QT_1 + "\'" );
      lUsage.put( "USAGE_DELTA_QT", "\'" + iUSAGE_DELTA_QT_1 + "\'" );
      lUsage.put( "USAGE_DESC", "\'" + iUSAGE_DESC_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_HISTORICAL_USAGE, lUsage ) );

      lUsage.clear();
      lUsage.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lUsage.put( "PART_NO_OEM", "\'" + iPN_ACFT + "\'" );
      lUsage.put( "MANUFACT_CD", "\'" + iMANUAL_ACFT + "\'" );
      lUsage.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_1 + "\'" );
      lUsage.put( "USAGE_QT", "'70'" );
      lUsage.put( "USAGE_DELTA_QT", "\'" + iUSAGE_DELTA_QT_2 + "\'" );
      lUsage.put( "USAGE_DESC", "\'" + iUSAGE_DESC_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_HISTORICAL_USAGE, lUsage ) );

      setCollectionDate( "01/02/2017", iUSAGE_DESC_1 );
      setCollectionDate( "01/03/2016", iUSAGE_DESC_2 );

      // run validation
      Assert.assertTrue( runValidationAndImport( true ) == 1 );
      ErrorCodeValidation( "ALAHU-00160" );

   }


   /**
    * This test is to verify error code ALAHU-00170:The latest Usage Records USAGE_QT for an
    * Aircraft, defined by SERIAL_NO_OEM, must equal the current usage in Maintenix
    *
    *
    */

   @Test
   public void test_ALAHU_00170_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // AL_HISTORICAL_USAGE
      Map<String, String> lUsage = new LinkedHashMap<>();

      lUsage.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lUsage.put( "PART_NO_OEM", "\'" + iPN_ACFT + "\'" );
      lUsage.put( "MANUFACT_CD", "\'" + iMANUAL_ACFT + "\'" );
      lUsage.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_1 + "\'" );
      lUsage.put( "USAGE_QT", "'500'" );
      lUsage.put( "USAGE_DELTA_QT", "\'" + iUSAGE_DELTA_QT_1 + "\'" );
      lUsage.put( "USAGE_DESC", "\'" + iUSAGE_DESC_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_HISTORICAL_USAGE, lUsage ) );

      lUsage.clear();
      lUsage.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lUsage.put( "PART_NO_OEM", "\'" + iPN_ACFT + "\'" );
      lUsage.put( "MANUFACT_CD", "\'" + iMANUAL_ACFT + "\'" );
      lUsage.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_1 + "\'" );
      lUsage.put( "USAGE_QT", "\'" + iUSAGE_QT_2 + "\'" );
      lUsage.put( "USAGE_DELTA_QT", "\'" + iUSAGE_DELTA_QT_2 + "\'" );
      lUsage.put( "USAGE_DESC", "\'" + iUSAGE_DESC_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_HISTORICAL_USAGE, lUsage ) );

      setCollectionDate( "01/02/2017", iUSAGE_DESC_1 );
      setCollectionDate( "01/03/2016", iUSAGE_DESC_2 );

      // run validation
      Assert.assertTrue( runValidationAndImport( true ) == 1 );
      ErrorCodeValidation( "ALAHU-00170" );

   }


   /**
    * This test is to verify error code ALAHU-00180:The combination of SERIAL_NO_OEM/
    * PART_NO_OEM/MANUFACT_CD/DATA_TYPE_CD/COLLECTION_DT cannot be duplicated in staging area.
    *
    *
    */

   @Test
   public void test_ALAHU_00180_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // AL_HISTORICAL_USAGE
      Map<String, String> lUsage = new LinkedHashMap<>();

      lUsage.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lUsage.put( "PART_NO_OEM", "\'" + iPN_ACFT + "\'" );
      lUsage.put( "MANUFACT_CD", "\'" + iMANUAL_ACFT + "\'" );
      lUsage.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_1 + "\'" );
      lUsage.put( "USAGE_QT", "\'" + iUSAGE_QT_1 + "\'" );
      lUsage.put( "USAGE_DELTA_QT", "\'" + iUSAGE_DELTA_QT_1 + "\'" );
      lUsage.put( "USAGE_DESC", "\'" + iUSAGE_DESC_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_HISTORICAL_USAGE, lUsage ) );

      lUsage.clear();
      lUsage.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lUsage.put( "PART_NO_OEM", "\'" + iPN_ACFT + "\'" );
      lUsage.put( "MANUFACT_CD", "\'" + iMANUAL_ACFT + "\'" );
      lUsage.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_1 + "\'" );
      lUsage.put( "USAGE_QT", "\'" + iUSAGE_QT_2 + "\'" );
      lUsage.put( "USAGE_DELTA_QT", "\'" + iUSAGE_DELTA_QT_2 + "\'" );
      lUsage.put( "USAGE_DESC", "\'" + iUSAGE_DESC_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_HISTORICAL_USAGE, lUsage ) );

      setCollectionDate( "01/02/2017", iUSAGE_DESC_1 );
      setCollectionDate( "01/02/2017", iUSAGE_DESC_2 );

      // run validation
      Assert.assertTrue( runValidationAndImport( true ) == 1 );
      ErrorCodeValidation( "ALAHU-00180" );

   }


   /**
    * This test is to verify error code ALAHU-00190:A usage record already exists in Maintenix for
    * this inventory. Data cannot be loaded if usage already exists.
    *
    *
    */

   @Test
   public void test_ALAHU_00190_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testACFTUSAGE_IMPORT();

      // AL_HISTORICAL_USAGE
      Map<String, String> lUsage = new LinkedHashMap<>();

      lUsage.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lUsage.put( "PART_NO_OEM", "\'" + iPN_ACFT + "\'" );
      lUsage.put( "MANUFACT_CD", "\'" + iMANUAL_ACFT + "\'" );
      lUsage.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_1 + "\'" );
      lUsage.put( "USAGE_QT", "\'" + iUSAGE_QT_1 + "\'" );
      lUsage.put( "USAGE_DELTA_QT", "\'" + iUSAGE_DELTA_QT_1 + "\'" );
      lUsage.put( "USAGE_DESC", "\'" + iUSAGE_DESC_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_HISTORICAL_USAGE, lUsage ) );

      lUsage.clear();
      lUsage.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lUsage.put( "PART_NO_OEM", "\'" + iPN_ACFT + "\'" );
      lUsage.put( "MANUFACT_CD", "\'" + iMANUAL_ACFT + "\'" );
      lUsage.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_1 + "\'" );
      lUsage.put( "USAGE_QT", "\'" + iUSAGE_QT_2 + "\'" );
      lUsage.put( "USAGE_DELTA_QT", "\'" + iUSAGE_DELTA_QT_2 + "\'" );
      lUsage.put( "USAGE_DESC", "\'" + iUSAGE_DESC_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_HISTORICAL_USAGE, lUsage ) );

      setCollectionDate( "01/02/2017", iUSAGE_DESC_1 );
      setCollectionDate( "01/03/2016", iUSAGE_DESC_2 );

      // run validation
      Assert.assertTrue( runValidationAndImport( true ) == 1 );
      ErrorCodeValidation( "ALAHU-00190" );

   }


   /**
    * This test is to verify error code ALAHU-00200:MANUFACT_CD not provided. Duplicate PART_NO_OEM
    * exists in Maintenix.
    *
    *
    */
   @Test
   public void test_ALAHU_00200_VALIDATION() {

      System.out.println( "=======Starting: " + testName.getMethodName()
            + " Validation========================" );

      // AL_HISTORICAL_USAGE
      Map<String, String> lUsage = new LinkedHashMap<>();

      lUsage.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lUsage.put( "PART_NO_OEM", "\'" + iPN_ACFT + "\'" );
      // lUsage.put( "MANUFACT_CD", "\'" + iMANUAL_ACFT + "\'" );
      lUsage.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_1 + "\'" );
      lUsage.put( "USAGE_QT", "\'" + iUSAGE_QT_1 + "\'" );
      lUsage.put( "USAGE_DELTA_QT", "\'" + iUSAGE_DELTA_QT_1 + "\'" );
      lUsage.put( "USAGE_DESC", "\'" + iUSAGE_DESC_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_HISTORICAL_USAGE, lUsage ) );

      setCollectionDate( "01/02/2017", iUSAGE_DESC_1 );

      // run validation
      Assert.assertTrue( runValidationAndImport( true ) == 1 );
      ErrorCodeValidation( "ALAHU-00200" );

   }


   /**
    * This test is to verify error code ALAHU-00210:MANUFACT_CD does not exist in Maintenix
    *
    *
    */

   @Test
   public void test_ALAHU_00210_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // AL_HISTORICAL_USAGE
      Map<String, String> lUsage = new LinkedHashMap<>();

      lUsage.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lUsage.put( "PART_NO_OEM", "\'" + iPN_ACFT + "\'" );
      lUsage.put( "MANUFACT_CD", "'test'" );
      lUsage.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_1 + "\'" );
      lUsage.put( "USAGE_QT", "\'" + iUSAGE_QT_1 + "\'" );
      lUsage.put( "USAGE_DELTA_QT", "\'" + iUSAGE_DELTA_QT_1 + "\'" );
      lUsage.put( "USAGE_DESC", "\'" + iUSAGE_DESC_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_HISTORICAL_USAGE, lUsage ) );

      lUsage.clear();
      lUsage.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lUsage.put( "PART_NO_OEM", "\'" + iPN_ACFT + "\'" );
      lUsage.put( "MANUFACT_CD", "'test'" );
      lUsage.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_1 + "\'" );
      lUsage.put( "USAGE_QT", "\'" + iUSAGE_QT_2 + "\'" );
      lUsage.put( "USAGE_DELTA_QT", "\'" + iUSAGE_DELTA_QT_2 + "\'" );
      lUsage.put( "USAGE_DESC", "\'" + iUSAGE_DESC_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_HISTORICAL_USAGE, lUsage ) );

      setCollectionDate( "01/02/2017", iUSAGE_DESC_1 );
      setCollectionDate( "01/03/2016", iUSAGE_DESC_2 );

      // run validation
      Assert.assertTrue( runValidationAndImport( true ) == 1 );
      ErrorCodeValidation( "ALAHU-00210" );

   }


   /**
    * This test is to verify error code ALAHU-00220:There are duplicate data type codes in Maintenix
    *
    *
    */
   @Test
   public void test_ALAHU_00220_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // AL_HISTORICAL_USAGE
      Map<String, String> lUsage = new LinkedHashMap<>();

      lUsage.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lUsage.put( "PART_NO_OEM", "\'" + iPN_ACFT + "\'" );
      lUsage.put( "MANUFACT_CD", "\'" + iMANUAL_ACFT + "\'" );
      lUsage.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_1 + "\'" );
      lUsage.put( "USAGE_QT", "\'" + iUSAGE_QT_1 + "\'" );
      lUsage.put( "USAGE_DELTA_QT", "\'" + iUSAGE_DELTA_QT_1 + "\'" );
      lUsage.put( "USAGE_DESC", "\'" + iUSAGE_DESC_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_HISTORICAL_USAGE, lUsage ) );

      lUsage.clear();
      lUsage.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lUsage.put( "PART_NO_OEM", "\'" + iPN_ACFT + "\'" );
      lUsage.put( "MANUFACT_CD", "\'" + iMANUAL_ACFT + "\'" );
      lUsage.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_1 + "\'" );
      lUsage.put( "USAGE_QT", "\'" + iUSAGE_QT_2 + "\'" );
      lUsage.put( "USAGE_DELTA_QT", "\'" + iUSAGE_DELTA_QT_2 + "\'" );
      lUsage.put( "USAGE_DESC", "\'" + iUSAGE_DESC_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_HISTORICAL_USAGE, lUsage ) );

      setCollectionDate( "01/02/2017", iUSAGE_DESC_1 );
      setCollectionDate( "01/03/2016", iUSAGE_DESC_2 );

      // run validation
      Assert.assertTrue( runValidationAndImport( true ) == 1 );
      ErrorCodeValidation( "ALAHU-00220" );

   }


   /**
    * This test is to verify error code ALAHU-00230:There are duplicate serial numbers in Maintenix
    * for the part number and manufacture.
    *
    *
    */
   @Test
   public void test_ALAHU_00230_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // AL_HISTORICAL_USAGE
      Map<String, String> lUsage = new LinkedHashMap<>();

      // lUsage.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lUsage.put( "PART_NO_OEM", "\'" + iPN_ACFT + "\'" );
      lUsage.put( "MANUFACT_CD", "\'" + iMANUAL_ACFT + "\'" );
      lUsage.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_1 + "\'" );
      lUsage.put( "USAGE_QT", "\'" + iUSAGE_QT_1 + "\'" );
      lUsage.put( "USAGE_DELTA_QT", "\'" + iUSAGE_DELTA_QT_1 + "\'" );
      lUsage.put( "USAGE_DESC", "\'" + iUSAGE_DESC_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_HISTORICAL_USAGE, lUsage ) );

      lUsage.clear();
      // lUsage.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lUsage.put( "PART_NO_OEM", "\'" + iPN_ACFT + "\'" );
      lUsage.put( "MANUFACT_CD", "\'" + iMANUAL_ACFT + "\'" );
      lUsage.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_1 + "\'" );
      lUsage.put( "USAGE_QT", "\'" + iUSAGE_QT_2 + "\'" );
      lUsage.put( "USAGE_DELTA_QT", "\'" + iUSAGE_DELTA_QT_2 + "\'" );
      lUsage.put( "USAGE_DESC", "\'" + iUSAGE_DESC_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_HISTORICAL_USAGE, lUsage ) );

      setCollectionDate( "01/02/2017", iUSAGE_DESC_1 );
      setCollectionDate( "01/03/2016", iUSAGE_DESC_2 );

      // run validation
      Assert.assertTrue( runValidationAndImport( true ) == 1 );
      ErrorCodeValidation( "ALAHU-00230" );

   }


   /**
    * This test is to verify error code ALAHU-00240:DATA_TYPE_CD must be provided.
    *
    *
    */

   @Test
   public void test_ALAHU_00240_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // AL_HISTORICAL_USAGE
      Map<String, String> lUsage = new LinkedHashMap<>();

      lUsage.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lUsage.put( "PART_NO_OEM", "\'" + iPN_ACFT + "\'" );
      lUsage.put( "MANUFACT_CD", "\'" + iMANUAL_ACFT + "\'" );
      // lUsage.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_1 + "\'" );
      lUsage.put( "USAGE_QT", "\'" + iUSAGE_QT_1 + "\'" );
      lUsage.put( "USAGE_DELTA_QT", "\'" + iUSAGE_DELTA_QT_1 + "\'" );
      lUsage.put( "USAGE_DESC", "\'" + iUSAGE_DESC_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_HISTORICAL_USAGE, lUsage ) );

      lUsage.clear();
      lUsage.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lUsage.put( "PART_NO_OEM", "\'" + iPN_ACFT + "\'" );
      lUsage.put( "MANUFACT_CD", "\'" + iMANUAL_ACFT + "\'" );
      // lUsage.put( "DATA_TYPE_CD", "\'" + iDATA_TYPE_CD_1 + "\'" );
      lUsage.put( "USAGE_QT", "\'" + iUSAGE_QT_2 + "\'" );
      lUsage.put( "USAGE_DELTA_QT", "\'" + iUSAGE_DELTA_QT_2 + "\'" );
      lUsage.put( "USAGE_DESC", "\'" + iUSAGE_DESC_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_HISTORICAL_USAGE, lUsage ) );

      setCollectionDate( "01/02/2017", iUSAGE_DESC_1 );
      setCollectionDate( "01/03/2016", iUSAGE_DESC_2 );

      // run validation
      Assert.assertTrue( runValidationAndImport( true ) == 1 );
      ErrorCodeValidation( "ALAHU-00240" );

   }


   // =============================================================================================
   /**
    * This function is to retrieve assemble bom IDs in MIM_DATA_TYPE table by giving serial No
    *
    *
    */

   public String getAssmblBOMID( String aASSMBL_CD, String aASSMBL_BOM_CD ) {

      String[] iIds = { "ASSMBL_BOM_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "ASSMBL_CD", aASSMBL_CD );
      lArgs.addArguments( "ASSMBL_BOM_CD", aASSMBL_BOM_CD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EQP_ASSMBL_BOM, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      return llists.get( 0 ).get( 0 );

   }


   /**
    * This function is to retrieve Data type IDs in MIM_DATA_TYPE table by giving serial No
    *
    *
    */

   public simpleIDs getDataTypeIDs( String aTypeCD ) {

      String[] iIds = { "DATA_TYPE_DB_ID", "DATA_TYPE_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "DATA_TYPE_CD", aTypeCD );
      lArgs.addArguments( "DATA_TYPE_SDESC", aTypeCD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.MIM_DATA_TYPE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lIds;

   }


   /**
    * This function is to retrieve Inventory IDs in inv_inv table by giving serial No
    *
    *
    */
   @Override
   public simpleIDs getInvIDs( String aSN ) {

      String[] iIds = { "INV_NO_DB_ID", "INV_NO_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "SERIAL_NO_OEM", aSN );
      String iQueryString = TableUtil.buildTableQuery( TableUtil.INV_INV, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lIds;

   }


   /**
    * This function is to verify USG_USAGE_DATA table details part
    *
    *
    */
   public void VerifyUSGUSAGEDATA( String aASSMBL_CD, String aASSMBLBOMID, int aNumRecord,
         simpleIDs aINVIDs, simpleIDs aDataTypeIds, usageInfor aUsage1, usageInfor aUsage2 ) {

      // SCHED_RMVD_PART table
      String[] iIds = { "INV_NO_DB_ID", "INV_NO_ID", "DATA_TYPE_DB_ID", "DATA_TYPE_ID",
            "ASSMBL_INV_NO_DB_ID", "ASSMBL_INV_NO_ID", "TSN_QT", "TSO_QT", "TSI_QT", "TSN_DELTA_QT",
            "TSO_DELTA_QT", "TSI_DELTA_QT" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "ASSMBL_DB_ID", Integer.toString( CONS_DB_ID ) );
      lArgs.addArguments( "ASSMBL_CD", aASSMBL_CD );
      lArgs.addArguments( "ASSMBL_BOM_ID", aASSMBLBOMID );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.USG_USAGE_DATA, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Check # of records is correct
      Assert.assertTrue( "Check number of records display: ", llists.size() == aNumRecord );

      for ( int i = 0; i < llists.size(); i++ ) {
         Assert.assertTrue( "INV_NO_DB_ID",
               llists.get( i ).get( 0 ).equalsIgnoreCase( aINVIDs.getNO_DB_ID() ) );
         Assert.assertTrue( "INV_NO_ID",
               llists.get( i ).get( 1 ).equalsIgnoreCase( aINVIDs.getNO_ID() ) );
         Assert.assertTrue( "DATA_TYPE_DB_ID",
               llists.get( i ).get( 2 ).equalsIgnoreCase( aDataTypeIds.getNO_DB_ID() ) );
         Assert.assertTrue( "DATA_TYPE_ID",
               llists.get( i ).get( 3 ).equalsIgnoreCase( aDataTypeIds.getNO_ID() ) );
         Assert.assertTrue( "ASSMBL_INV_NO_DB_ID",
               llists.get( i ).get( 4 ).equalsIgnoreCase( aINVIDs.getNO_DB_ID() ) );
         Assert.assertTrue( "ASSMBL_INV_NO_ID",
               llists.get( i ).get( 5 ).equalsIgnoreCase( aINVIDs.getNO_ID() ) );

         usageInfor lusage = new usageInfor( llists.get( i ).get( 6 ), llists.get( i ).get( 7 ),
               llists.get( i ).get( 8 ), llists.get( i ).get( 9 ), llists.get( i ).get( 10 ),
               llists.get( i ).get( 11 ) );

         Assert.assertTrue( "Check usage unit",
               lusage.equals( aUsage1 ) || lusage.equals( aUsage2 ) );

      }
   }


   /**
    * This function is to set date on COLLECTION_DT column of AL_HISTORICAL_USAGE
    *
    *
    *
    */
   public void setCollectionDate( String aDate, String aUSAGE_DESC ) {

      String aUpdateQuery = "UPDATE AL_HISTORICAL_USAGE SET COLLECTION_DT= TO_DATE('" + aDate
            + "', 'MM/DD/YYYY') where USAGE_DESC='" + aUSAGE_DESC + "'";
      try {

         PreparedStatement lStatement = getConnection().prepareStatement( aUpdateQuery );
         lStatement.executeUpdate( aUpdateQuery );
         commit();

      } catch ( SQLException e ) {
         e.printStackTrace();
      }

   }


   /**
    * This function is called by after test method to restore data which has been modified by tests.
    *
    *
    */

   public void RestoreData() {

      String lStrDelete = "delete from " + TableUtil.USG_USAGE_RECORD;
      executeSQL( lStrDelete );

      lStrDelete = "delete from " + TableUtil.USG_USAGE_DATA;
      executeSQL( lStrDelete );

      restoreDataType();
      restoreLockInv();
      restoreDupPNOEM();

   }


   public void VerifyImport() {

      // Verify usg_usage_data for acft
      simpleIDs lInvIds1 = getInvIDs( iSN_ACFT );
      simpleIDs lDataTypeIds = getDataTypeIDs( iDATA_TYPE_CD_1 );
      usageInfor lUsage1 = new usageInfor( "100", "100", "100", "100", "100", "100" );
      VerifyUSGUSAGEDATA( iACFTASSMBL_CD, "0", 1, lInvIds1, lDataTypeIds, lUsage1, lUsage1 );

      // Verify usg_usage_data for eng
      simpleIDs lInvIds2 = getInvIDs( iSN_ENG );
      lDataTypeIds = getDataTypeIDs( iDATA_TYPE_CD_2 );

      String lBOM_ID = getAssmblBOMID( iACFTASSMBL_CD, iENGASSMBL_BOM_CD );
      VerifyUSGUSAGEDATA( iACFTASSMBL_CD, lBOM_ID, 1, lInvIds2, lDataTypeIds, lUsage1, lUsage1 );

      // Verify usg_usafe_record
      String lQuery = "Select 1 from USG_USAGE_RECORD where USAGE_DESC='" + iUSAGE_DESC_1
            + "' and INV_NO_DB_ID=" + lInvIds1.getNO_DB_ID() + " and INV_NO_ID="
            + lInvIds1.getNO_ID();
      Assert.assertTrue( "check " + iUSAGE_DESC_1 + " exist", RecordsExist( lQuery ) );

      lQuery = "Select 1 from USG_USAGE_RECORD where USAGE_DESC='" + iUSAGE_DESC_2
            + "' and INV_NO_DB_ID=" + lInvIds2.getNO_DB_ID() + " and INV_NO_ID="
            + lInvIds2.getNO_ID();
      Assert.assertTrue( "check " + iUSAGE_DESC_2 + " exist", RecordsExist( lQuery ) );

   }


   /**
    * This function is to verify AL_HISTORICAL_USAGE staging table is loaded
    *
    *
    */

   public void VerifyStagingTable() {

      String lQuery = "select 1 from " + TableUtil.AL_HISTORICAL_USAGE + " where SERIAL_NO_OEM='"
            + iSN_ACFT + "' and DATA_TYPE_CD='" + iDATA_TYPE_CD_1 + "'";
      Assert.assertTrue( "Check AL_HISTORICAL_USAGE table to verify the record exists",
            RecordsExist( lQuery ) );

      String lQuery2 = "select 1 from " + TableUtil.AL_HISTORICAL_USAGE + " where SERIAL_NO_OEM='"
            + iSN_ENG + "' and DATA_TYPE_CD='" + iDATA_TYPE_CD_2 + "'";
      Assert.assertTrue( "Check AL_HISTORICAL_USAGE table to verify the record exists",
            RecordsExist( lQuery2 ) );

      int lCount = countRowsInEntireTable( TableUtil.AL_HISTORICAL_USAGE );
      Assert.assertTrue( "There should be 2 records in AL_HISTORICAL_USAGE table. ", lCount == 2 );

   }


   /**
    * This function is to check error(s) for validation table is loaded
    *
    *
    */
   public void VerifyValidation() {

      int lCount = countRowsOfQuery( TestConstants.HISTORICAL_USAGE_ERROR_CHECK_TABLE );
      Assert.assertTrue( "There should be no error. ", lCount == 0 );

   }


   /**
    * This function is to check given error code presents
    *
    *
    */

   public void ErrorCodeValidation( String aErrorcode ) {

      boolean lFound = false;

      String[] iIds = { "RESULT_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();

      lArgs.addArguments( "FUNC_AREA_CD", "HISTUSG" );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.AL_PROC_RESULT, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      if ( llists.size() == 0 ) {
         Assert.assertTrue( "There should be some error(s).", false );

      }

      for ( int i = 0; i < llists.size(); i++ ) {
         if ( llists.get( i ).get( 0 ).equalsIgnoreCase( aErrorcode ) ) {
            lFound = true;
            break;
         }

      }

      String lerror_desc = getErrorDetail( aErrorcode );;

      Assert.assertTrue( "Error found- " + aErrorcode + " : " + lerror_desc, lFound );

   }


   /**
    * This function is to get detail of error code
    *
    *
    */

   public String getErrorDetail( String aErrorcode ) {

      String[] iIds = { "USER_DESC" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();

      lArgs.addArguments( "RESULT_CD", aErrorcode );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.DL_REF_MESSAGE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      return llists.get( 0 ).get( 0 );

   }


   /**
    * This function is to create duplicated data type for data setup for error code ALAHU-00220
    */

   public void dataSetupDataType() {

      String lQuery =
            "INSERT INTO mim_data_type ( data_type_db_id, data_type_id, eng_unit_db_id, eng_unit_cd, domain_type_db_id, domain_type_cd, entry_prec_qt, data_type_cd, data_type_sdesc )"
                  + " VALUES( 4650, data_type_id_seq.nextval, 10, 'COUNTS', 0, 'US', 1, 'AUTOUSAGE1', 'AUTOUSAGE1Second' )";
      executeSQL( lQuery );
   }


   /**
    * This function is to create duplicated data type for data setup for error code ALAHU-00130
    */

   public void dataSetupLockInv() {

      String lQuery = "UPDATE inv_inv SET LOCKED_BOOL='1' where inv_inv.serial_no_oem='SN000014'";
      executeSQL( lQuery );
   }


   /**
    * This function is called by restoredata()
    *
    *
    */
   public void restoreDataType() {

      String lQuery = "delete from mim_data_type where DATA_TYPE_SDESC='AUTOUSAGE1Second'";
      executeSQL( lQuery );
   }


   /**
    * This function is called by restoredata()
    *
    *
    */

   public void restoreLockInv() {

      String lQuery = "UPDATE inv_inv SET LOCKED_BOOL='0' where inv_inv.serial_no_oem='SN000014'";
      executeSQL( lQuery );
   }


   public void dataSetupDupPNOEM() {

      String lQuery =
            "INSERT INTO EQP_PART_NO (PART_NO_DB_ID, PART_NO_ID, INV_CLASS_DB_ID, INV_CLASS_CD, "
                  + " PART_STATUS_DB_ID, PART_STATUS_CD,MANUFACT_DB_ID, MANUFACT_CD, QTY_UNIT_DB_ID,QTY_UNIT_CD,PART_NO_SDESC, PART_NO_OEM, NOTE) "
                  + " VALUES (4650, part_no_id_seq.nextval, '0', 'ACFT', '0', 'ACTV',4650, '11111','0','EA','Aircraft Part 1', 'ACFT_ASSY_PN1', 'AUTO')";
      executeSQL( lQuery );
      copyinv();

   }


   /**
    * This function is called by restoredata()
    *
    *
    */

   public void restoreDupPNOEM() {
      String lQuery = "delete from EQP_PART_NO where NOTE='AUTO'";
      executeSQL( lQuery );

      if ( iPNIDs != null ) {
         lQuery = "delete from INV_INV where PART_NO_DB_ID=" + iPNIDs.getNO_DB_ID()
               + " and PART_NO_ID=" + iPNIDs.getNO_ID();
         executeSQL( lQuery );
      }

   }


   /**
    * This function is to retrieve inventory record into inventoryData and create new inventory
    * record with other part no
    *
    *
    */

   public void copyinv() {

      inventoryData lORGINV = getInventory( iSN_ACFT );
      iPNIDs = getPNIDs( iPN_ACFT, "AUTO" );

      String lQuery =
            "INSERT INTO INV_INV (INV_NO_DB_ID, INV_NO_ID,INV_CLASS_DB_ID,INV_CLASS_CD,BOM_PART_DB_ID,BOM_PART_ID,PART_NO_DB_ID,PART_NO_ID,H_INV_NO_DB_ID,H_INV_NO_ID, "
                  + " ASSMBL_INV_NO_DB_ID,ASSMBL_INV_NO_ID, INV_COND_DB_ID,INV_COND_CD,ASSMBL_DB_ID, ASSMBL_CD,ASSMBL_BOM_ID, ASSMBL_POS_ID,ORIG_ASSMBL_DB_ID, ORIG_ASSMBL_CD, "
                  + " INV_NO_SDESC,SERIAL_NO_OEM,FINANCE_STATUS_CD, APPL_EFF_CD,CARRIER_DB_ID,CARRIER_ID) "
                  + " VALUES (" + lORGINV.getINV_NO_DB_ID() + ",inv_no_id_seq.nextval,'"
                  + lORGINV.getINV_CLASS_DB_ID() + "','" + lORGINV.getINV_CLASS_CD() + "','"
                  + lORGINV.getBOM_PART_DB_ID() + "','" + lORGINV.getBOM_PART_ID() + "','"
                  + iPNIDs.getNO_DB_ID() + "','" + iPNIDs.getNO_ID() + "','"
                  + lORGINV.getH_INV_NO_DB_ID() + "','" + lORGINV.getH_INV_NO_ID() + "','"
                  + lORGINV.getASSMBL_INV_NO_DB_ID() + "','" + lORGINV.getASSMBL_INV_NO_ID() + "','"
                  + lORGINV.getINV_COND_DB_ID() + "','" + lORGINV.getINV_COND_CD() + "','"
                  + lORGINV.getASSMBL_DB_ID() + "','" + lORGINV.getASSMBL_CD() + "','"
                  + lORGINV.getASSMBL_BOM_ID() + "','" + lORGINV.getASSMBL_POS_ID() + "','"
                  + lORGINV.getORIG_ASSMBL_DB_ID() + "','" + lORGINV.getORIG_ASSMBL_CD() + "','"
                  + lORGINV.getINV_NO_SDESC() + "','" + lORGINV.getSERIAL_NO_OEM() + "','"
                  + lORGINV.getFINANCE_STATUS_CD() + "','" + lORGINV.getAPPL_EFF_CD() + "','"
                  + lORGINV.getCARRIER_DB_ID() + "','" + lORGINV.getCARRIER_ID() + "')";
      executeSQL( lQuery );

   }


   /**
    * This function is to retrieve inventory record given serial no oem
    *
    *
    */

   public inventoryData getInventory( String aSN ) {

      ResultSet lResultSetRecords;
      inventoryData linvData = null;
      String lQuery = "select * from inv_inv " + " where inv_inv.serial_no_oem='" + aSN + "'";

      try {
         lResultSetRecords = runQuery( lQuery );
         lResultSetRecords.next();
         linvData = new inventoryData( lResultSetRecords.getString( "INV_NO_DB_ID" ),
               lResultSetRecords.getString( "INV_NO_ID" ),
               lResultSetRecords.getString( "INV_CLASS_DB_ID" ),
               lResultSetRecords.getString( "INV_CLASS_CD" ),
               lResultSetRecords.getString( "BOM_PART_DB_ID" ),
               lResultSetRecords.getString( "BOM_PART_ID" ),
               lResultSetRecords.getString( "PART_NO_DB_ID" ),
               lResultSetRecords.getString( "PART_NO_ID" ),
               lResultSetRecords.getString( "H_INV_NO_DB_ID" ),
               lResultSetRecords.getString( "H_INV_NO_ID" ),
               lResultSetRecords.getString( "ASSMBL_INV_NO_DB_ID" ),
               lResultSetRecords.getString( "ASSMBL_INV_NO_ID" ),
               lResultSetRecords.getString( "INV_COND_DB_ID" ),
               lResultSetRecords.getString( "INV_COND_CD" ),
               lResultSetRecords.getString( "ASSMBL_DB_ID" ),
               lResultSetRecords.getString( "ASSMBL_CD" ),
               lResultSetRecords.getString( "ASSMBL_BOM_ID" ),
               lResultSetRecords.getString( "ASSMBL_POS_ID" ),
               lResultSetRecords.getString( "ORIG_ASSMBL_DB_ID" ),
               lResultSetRecords.getString( "ORIG_ASSMBL_CD" ),
               lResultSetRecords.getString( "INV_NO_SDESC" ),
               lResultSetRecords.getString( "SERIAL_NO_OEM" ),
               lResultSetRecords.getString( "FINANCE_STATUS_CD" ),
               lResultSetRecords.getString( "APPL_EFF_CD" ),
               lResultSetRecords.getString( "CARRIER_DB_ID" ),
               lResultSetRecords.getString( "CARRIER_ID" ) );
      } catch ( SQLException e ) {
         e.printStackTrace();
         Assert.assertTrue( "failed on getInventory call", false );

      }

      return linvData;

   }


   /**
    * This function is to retrieve PART NO Ids for just created record. .
    *
    *
    */

   public simpleIDs getPNIDs( String aPN, String aNOTE ) {
      // REF_PO_AUTH_LVL
      String[] iIds = { "PART_NO_DB_ID", "PART_NO_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "PART_NO_OEM", aPN );
      lArgs.addArguments( "NOTE", aNOTE );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EQP_PART_NO, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );
      return lIds;

   }


   /**
    * This function is to implement interface ValidationAndImport
    *
    * @param: blnOnlyValidation
    * @param: allornone
    *
    */
   public int runValidationAndImport( boolean blnOnlyValidation ) {
      int rtValue = 0;
      ivalidationandimport = new ValidationAndImport() {

         @Override
         public int runValidation( boolean allornone ) {
            int lReturn = 0;
            CallableStatement lPrepareCallInventory = null;

            try {
               lPrepareCallInventory = getConnection().prepareCall(
                     "BEGIN al_historical_usage_import.validate(aon_retcode => ?, aov_retmsg =>?); END;" );
               lPrepareCallInventory.registerOutParameter( 1, Types.INTEGER );
               lPrepareCallInventory.registerOutParameter( 2, Types.VARCHAR );
               lPrepareCallInventory.execute();
               commit();
               lReturn = lPrepareCallInventory.getInt( 1 );

            } catch ( SQLException e ) {
               e.printStackTrace();
               Assert.assertTrue( "failed on precedure call", false );
            }
            return lReturn;

         }


         @Override
         public int runImport( boolean allornone ) {
            int lReturn = 0;
            CallableStatement lPrepareCallInventory = null;

            try {
               lPrepareCallInventory = getConnection().prepareCall(
                     "BEGIN al_historical_usage_import.import(aon_retcode => ?, aov_retmsg =>?); END;" );
               lPrepareCallInventory.registerOutParameter( 1, Types.INTEGER );
               lPrepareCallInventory.registerOutParameter( 2, Types.VARCHAR );

               lPrepareCallInventory.execute();
               commit();
               lReturn = lPrepareCallInventory.getInt( 1 );

            } catch ( SQLException e ) {
               e.printStackTrace();
               Assert.assertTrue( "failed on precedure call", false );
            }
            return lReturn;
         }

      };

      rtValue = blnOnlyValidation ? ivalidationandimport.runValidation( true )
            : ivalidationandimport.runImport( true );

      return rtValue;
   }

}
