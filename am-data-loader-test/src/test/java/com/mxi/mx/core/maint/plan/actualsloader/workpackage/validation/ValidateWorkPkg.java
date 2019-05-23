package com.mxi.mx.core.maint.plan.actualsloader.workpackage.validation;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.WorkPKGTest;


/**
 * This test case will test all validations
 *
 */
public class ValidateWorkPkg extends WorkPKGTest {

   @Rule
   public TestName testName = new TestName();


   /**
    * Setup before executing each individual test
    *
    * @author Geoff Hyde
    */
   @Before
   @Override
   public void before() throws Exception {

      super.before();
      clearActualsLoaderTables();
      String strTCName = testName.getMethodName();
      if ( strTCName.contains( "12018" ) )
         dataSetup12018();
      else if ( strTCName.contains( "20020" ) )
         dataSetup20020();
      else if ( strTCName.contains( "20003" ) )
         dataSetup20003();
   }


   /**
    * Creates duplicate batch parts in the Inv_inv table for ALWKP-12018
    *
    */
   private void dataSetup12018() {
      String lQuery = "SELECT inv_no_db_id, inv_no_id FROM inv_inv WHERE serial_no_oem = '"
            + iSERIAL_NO_OEM_TASk_8 + "'";
      iDuplcateInvNoIds = getIDs( lQuery, "INV_NO_DB_ID", "INV_NO_ID" );
      lQuery = "UPDATE inv_inv SET serial_no_oem = 'SNBATCH1' WHERE inv_no_db_id = "
            + iDuplcateInvNoIds.getNO_DB_ID() + " AND inv_no_id = " + iDuplcateInvNoIds.getNO_ID();
      runUpdate( lQuery );

   }


   /**
    * Setup data so there duplicate combination of Serial, Part, and Manufacture
    *
    */
   private void dataSetup20003() {
      String lQuery = "SELECT inv_no_db_id, inv_no_id FROM inv_inv WHERE serial_no_oem = '"
            + iWKP_SERIAL_NO_OEM_7 + "'";
      iDuplcateInvNoIds = getIDs( lQuery, "INV_NO_DB_ID", "INV_NO_ID" );
      lQuery = "UPDATE inv_inv SET serial_no_oem = 'SN000014' WHERE inv_no_db_id = "
            + iDuplcateInvNoIds.getNO_DB_ID() + " AND inv_no_id = " + iDuplcateInvNoIds.getNO_ID();
      runUpdate( lQuery );

   }


   /**
    * Setup data so there duplicate combination of Serial and Part with a different Manufacture
    *
    */
   private void dataSetup20020() {

      // setup Serial No
      String lQuery = "SELECT inv_no_db_id, inv_no_id FROM inv_inv WHERE serial_no_oem = '"
            + iWKP_SERIAL_NO_OEM_6 + "'";
      iDuplcateInvNoIds = getIDs( lQuery, "INV_NO_DB_ID", "INV_NO_ID" );
      lQuery = "UPDATE inv_inv SET serial_no_oem = 'SN000014' WHERE inv_no_db_id = "
            + iDuplcateInvNoIds.getNO_DB_ID() + " AND inv_no_id = " + iDuplcateInvNoIds.getNO_ID();
      runUpdate( lQuery );
      // setup Part No
      lQuery = "SELECT part_no_db_id, part_no_id FROM eqp_part_no WHERE part_no_oem = '"
            + iWKP_PART_NO_OEM_6 + "'";
      iDuplcatePartNoIds = getIDs( lQuery, "PART_NO_DB_ID", "PART_NO_ID" );
      lQuery = "UPDATE eqp_part_no SET part_no_oem = 'ACFT_ASSY_PN1' " + "WHERE part_no_db_id = "
            + iDuplcatePartNoIds.getNO_DB_ID() + " AND part_no_id = "
            + iDuplcatePartNoIds.getNO_ID();
      runUpdate( lQuery );
   }


   /**
    * Clean up after each individual test
    *
    * @throws Exception
    */
   @After
   @Override
   public void after() throws Exception {
      String strTCName = testName.getMethodName();
      // restore original values for specific test cases
      if ( iDuplcateInvNoIds != null && iDuplcatePartNoIds != null ) {
         runUpdate( "UPDATE inv_inv SET serial_no_oem = 'SN000006' WHERE inv_no_db_id = "
               + iDuplcateInvNoIds.getNO_DB_ID() + " AND inv_no_id = "
               + iDuplcateInvNoIds.getNO_ID() );
         runUpdate( "UPDATE eqp_part_no SET part_no_oem = 'APU_ASSY_PN1' "
               + "WHERE part_no_db_id = " + iDuplcatePartNoIds.getNO_DB_ID() + " AND part_no_id = "
               + iDuplcatePartNoIds.getNO_ID() );
      }
      if ( iDuplcateInvNoIds != null ) {
         if ( strTCName.contains( "20003" ) )
            runUpdate( "UPDATE inv_inv SET serial_no_oem = 'SN000013' WHERE inv_no_db_id = "
                  + iDuplcateInvNoIds.getNO_DB_ID() + " AND inv_no_id = "
                  + iDuplcateInvNoIds.getNO_ID() );
         else if ( strTCName.contains( "12018" ) )
            runUpdate( "UPDATE inv_inv SET serial_no_oem = 'SNBTH001AT' WHERE inv_no_db_id = "
                  + iDuplcateInvNoIds.getNO_DB_ID() + " AND inv_no_id = "
                  + iDuplcateInvNoIds.getNO_ID() );
      }
      super.after();

   }


   /**
    *
    * test ALWPK-10000 - AL_WORK_PACKAGE: WKP Serial Number must be provided
    *
    */
   @Test
   public void testALWPK_10000_1() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", null );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_1 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_1 + "\'" );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" );
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_1 + "\'" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-10000" );

   }


   /**
    *
    * test ALWPK-10000 - AL_WORK_PACKAGE: WKP Serial Number must be provided
    *
    */
   @Test
   public void testALWPK_10000_2() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "'  ' " );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_1 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_1 + "\'" );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" );
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_1 + "\'" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-10000" );

   }


   /**
    *
    * test ALWPK-10001 - AL_WORK_PACKAGE: Part Number must be provided
    *
    */
   @Test
   public void testALWPK_10001_1() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", null );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_1 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_1 + "\'" );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" );
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_1 + "\'" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-10001" );
   }


   /**
    *
    * test ALWPK-10001 - AL_WORK_PACKAGE: Part Number must be provided
    *
    */
   @Test
   public void testALWPK_10001_2() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "'   '" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_1 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_1 + "\'" );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" );
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_1 + "\'" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-10001" );
   }


   /**
    *
    * test ALWPK-10002 - AL_WORK_PACKAGE: Manufacturer must be provided
    *
    */
   @Test
   @Ignore
   public void testALWPK_10002_1() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", null );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_1 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_1 + "\'" );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" );
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_1 + "\'" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-10002" );
   }


   /**
    *
    * test ALWPK-10002 - AL_WORK_PACKAGE: Manufacturer must be provided
    *
    */
   @Test
   @Ignore
   public void testALWPK_10002_2() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "'  '" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_1 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_1 + "\'" );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" );
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_1 + "\'" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-10002" );
   }


   /**
    *
    * test ALWPK-10003 - AL_WORK_PACKAGE: Work Package Name must be provided
    *
    */
   @Test
   public void testALWPK_10003_1() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", null );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_1 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_1 + "\'" );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" );
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_1 + "\'" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-10003" );
   }


   /**
    *
    * test ALWPK-10003 - AL_WORK_PACKAGE: Work Package Name must be provided
    *
    */
   @Test
   public void testALWPK_10003_2() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "'    '" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_1 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_1 + "\'" );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" );
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_1 + "\'" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-10003" );
   }


   /**
    *
    * test ALWPK-10004 - AL_WORK_PACKAGE: Scheduled Start Date must be provided
    *
    */
   @Test
   public void testALWPK_10004() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_1 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_1 + "\'" );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" );
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_1 + "\'" );
      lWPK.put( "SCHED_START_DT", null );
      lWPK.put( "SCHED_END_DT", "SYSDATE+1" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-10004" );
   }


   /**
    *
    * test ALWPK-10005 -AL_WORK_PACKAGE: Scheduled End Date must be provided
    *
    */
   @Test
   public void testALWPK_10005() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_1 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_1 + "\'" );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" );
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_1 + "\'" );
      lWPK.put( "SCHED_START_DT", "SYSDATE" );
      lWPK.put( "SCHED_END_DT", null );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-10005" );
   }


   /**
    *
    * test ALWPK-10006 - AL_WORK_PACKAGE: Issue To Account must be provided for a component Work
    * Package (not root ACFT)
    *
    */
   @Test
   public void testALWPK_10006() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_2 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_2 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_2 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_2 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_2 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_2 + "\'" );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" );
      lWPK.put( "ISSUE_ACCOUNT_CD", null );
      lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_2 + "\'" );
      lWPK.put( "SCHED_START_DT", "SYSDATE" );
      lWPK.put( "SCHED_END_DT", "SYSDATE+1" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-10006" );
   }


   /**
    *
    * test ALWPK-10007 - AL_WORK_PACKAGE: Scheduled Location must be provided
    *
    */
   @Test
   @Ignore
   public void testALWPK_10007() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_1 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_1 + "\'" );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" );
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", null );
      lWPK.put( "SCHED_START_DT", "SYSDATE" );
      lWPK.put( "SCHED_END_DT", "SYSDATE+1" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-10007" );
   }


   /**
    *
    * test ALWPK-10010 - AL_WORK_PACKAGE: The elements of comma-separated list cannot be empty in
    * WORK_TYPE_LIST
    *
    */
   @Test
   @Ignore
   public void testALWPK_10010() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_1 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_1 + "\'" );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" );
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_2 + "\'" );
      lWPK.put( "SCHED_START_DT", "SYSDATE" );
      lWPK.put( "SCHED_END_DT", "SYSDATE+1" );
      // lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-10010" );
   }


   /**
    *
    * test ALWPK-11000 - AL_WORK_PACKAGE:WKP Serial Number/WKP Part Number/WKP Manufacturer /Work
    * Package Name exists multiple times in the staging area for the root ACFT Inventory
    *
    */
   @Test
   public void testALWPK_11000() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_1 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_1 + "\'" );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" );
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_1 + "\'" );
      lWPK.put( "SCHED_START_DT", "SYSDATE" );
      lWPK.put( "SCHED_END_DT", "SYSDATE+1" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-11000" );
   }


   /**
    *
    * test ALWPK-11001 - AL_WORK_PACKAGE:WKP Serial Number/WKP Part Number/WKP Manufacturer exists
    * multiple times in the staging area. Only one active work package per inventory allowed for NOT
    * root ACFT Inventory
    *
    */
   @Test
   public void testALWPK_11001() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_2 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_2 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_2 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_2 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_2 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_2 + "\'" );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" );
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_2 + "\'" );
      lWPK.put( "SCHED_START_DT", "SYSDATE" );
      lWPK.put( "SCHED_END_DT", "SYSDATE+1" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-11001" );
   }


   /**
    *
    * test ALWPK-12000 - AL_WORK_PACKAGE: WKP Serial Number is invalid
    *
    */
   @Test
   public void testALWPK_12000() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "'INVALID'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_1 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_1 + "\'" );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" );
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_1 + "\'" );
      lWPK.put( "SCHED_START_DT", "SYSDATE" );
      lWPK.put( "SCHED_END_DT", "SYSDATE+1" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-12000" );

   }


   /**
    *
    * test ALWPK-12001 - AL_WORK_PACKAGE: Part Number is invalid
    *
    */
   @Test
   public void testALWPK_12001() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "'INVALID'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_1 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_1 + "\'" );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" );
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_1 + "\'" );
      lWPK.put( "SCHED_START_DT", "SYSDATE" );
      lWPK.put( "SCHED_END_DT", "SYSDATE+1" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-12001" );
   }


   /**
    *
    * test ALWPK-12002 - AL_WORK_PACKAGE: Manufacturer must be provided
    *
    */
   @Test
   public void testALWPK_12002() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "'INVALID'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_1 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_1 + "\'" );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" );
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_1 + "\'" );
      lWPK.put( "SCHED_START_DT", "SYSDATE" );
      lWPK.put( "SCHED_END_DT", "SYSDATE+1" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-12002" );
   }


   /**
    *
    * test ALWPK-12003 - AL_WORK_PACKAGE: WKP_part number/ WKP_manufacturer is invalid
    *
    */
   @Test
   public void testALWPK_12003() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_2 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_1 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_1 + "\'" );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" );
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_1 + "\'" );
      lWPK.put( "SCHED_START_DT", "SYSDATE" );
      lWPK.put( "SCHED_END_DT", "SYSDATE+1" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-12003" );
   }


   /**
    *
    * test ALWPK-12004 - AL_WORK_PACKAGE: WKP_part number/ WKP_manufacturer/ WKP_serial number is
    * invalid
    *
    */
   @Test
   public void testALWPK_12004() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_2 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_1 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_1 + "\'" );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" );
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_1 + "\'" );
      lWPK.put( "SCHED_START_DT", "SYSDATE" );
      lWPK.put( "SCHED_END_DT", "SYSDATE+1" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-12004" );
   }


   /**
    *
    * test ALWPK-12005 - AL_WORK_PACKAGE.work_type_cd not found in Maintenix REF_WORK_TYPE table
    *
    */
   @Test
   public void testALWPK_12005() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_1 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_1 + "\'" );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" );
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_1 + "\'" );
      lWPK.put( "SCHED_START_DT", "SYSDATE" );
      lWPK.put( "SCHED_END_DT", "SYSDATE+1" );
      lWPK.put( "WORK_TYPE_LIST", "\'INVALID," + iWORK_TYPE_LIST_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-12005" );
   }


   /**
    *
    * test ALWPK-12006 - AL_WORK_PACKAGE.sub_type_cd not found in Maintenix REF_TASK_SUBCLASS table
    *
    */
   @Test
   public void testALWPK_12006() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_1 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'INVALID'" );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" );
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_1 + "\'" );
      lWPK.put( "SCHED_START_DT", "SYSDATE" );
      lWPK.put( "SCHED_END_DT", "SYSDATE+1" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-12006" );
   }


   /**
    *
    * test ALWPK-12007 - AL_WORK_PACKAGE.sub_type_cd not found in Maintenix REF_TASK_SUBCLASS table
    * for a task_class_cd of CHECK when a Work Package is on root ACFT
    *
    */
   @Test
   public void testALWPK_12007() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_1 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + "PAINT" + "\'" );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" );
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_1 + "\'" );
      lWPK.put( "SCHED_START_DT", "SYSDATE" );
      lWPK.put( "SCHED_END_DT", "SYSDATE+1" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-12007" );
   }


   /**
    *
    * test ALWPK-12009 - AL_WORK_PACKAGE.heavy_maintenance_bool must be Y or N.
    *
    */
   @Test
   public void testALWPK_12009() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_1 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_1 + "\'" );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "'Q'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" );
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_1 + "\'" );
      lWPK.put( "SCHED_START_DT", "SYSDATE" );
      lWPK.put( "SCHED_END_DT", "SYSDATE+1" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-12009" );
   }


   /**
    *
    * test ALWPK-12010 - AL_WORK_PACKAGE.collection_required _bool must be Y or N.
    */
   @Test
   public void testALWPK_12010() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_1 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_1 + "\'" );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "'Q'" );
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_1 + "\'" );
      lWPK.put( "SCHED_START_DT", "SYSDATE" );
      lWPK.put( "SCHED_END_DT", "SYSDATE+1" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-12010" );
   }


   /**
    *
    * test ALWPK-12011 - AL_WORK_PACKAGE.issue_account_cd not found in Maintenix FNC_ACCOUNT table
    *
    */
   @Test
   public void testALWPK_12011() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_1 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_1 + "\'" );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" );
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'INVALID\'" );
      lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_1 + "\'" );
      lWPK.put( "SCHED_START_DT", "SYSDATE" );
      lWPK.put( "SCHED_END_DT", "SYSDATE+1" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-12011" );
   }


   /**
    *
    * test ALWPK-12012 - AL_WORK_PACKAGE.sched_loc_cd not found in Maintenix INV_LOC table
    *
    */
   @Test
   public void testALWPK_12012() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_1 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_1 + "\'" );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" );
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", "\'" + "INVALID" + "\'" );
      lWPK.put( "SCHED_START_DT", "SYSDATE" );
      lWPK.put( "SCHED_END_DT", "SYSDATE+1" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-12012" );
   }


   /**
    *
    * test ALWPK-12013 - AL_WORK_PACKAGE.Sched_End_Dt must be greater or equal to Scheduled Start
    * Date
    *
    */
   @Test
   public void testALWPK_12013() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_1 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_1 + "\'" );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" );
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_1 + "\'" );
      lWPK.put( "SCHED_START_DT", "SYSDATE" );
      lWPK.put( "SCHED_END_DT", "SYSDATE-1" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-12013" );
   }


   /**
    *
    * test ALWPK-12015 - AL_WORK_PACKAGE.sub_type_cd not found in Maintenix REF_TASK_SUBCLASS table
    * for a task_class_cd of RO when a Work Package is NOT on root ACFT
    *
    *
    */
   @Test
   public void testALWPK_12015() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_2 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_2 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_2 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_2 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_2 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_1 + "\'" ); // wrong sub_type_cd
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" );
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_2 + "\'" );
      lWPK.put( "SCHED_START_DT", "SYSDATE" );
      lWPK.put( "SCHED_END_DT", "SYSDATE+1" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-12015" );
   }


   /**
    *
    * test ALWPK-12016 - AL_WORK_PACKAGE.sched_loc_cd must be of LINE or HANGER type for root ACFT
    * Work Package
    *
    */
   @Test
   public void testALWPK_12016() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_1 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_1 + "\'" );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" );
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_2 + "\'" ); // its the wrong loc_cd
      lWPK.put( "SCHED_START_DT", "SYSDATE" );
      lWPK.put( "SCHED_END_DT", "SYSDATE+1" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-12016" );
   }


   /**
    *
    * test ALWPK-12017 - AL_WORK_PACKAGE.sched_loc_cd must be of SHOP or VENDOR type for a component
    * Work Package (NOT root ACFT Inventory)
    *
    */
   @Test
   public void testALWPK_12017() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_2 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_2 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_2 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_2 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_2 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_2 + "\'" );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" );
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_1 + "\'" ); // its the wrong loc_cd
      lWPK.put( "SCHED_START_DT", "SYSDATE" );
      lWPK.put( "SCHED_END_DT", "SYSDATE+1" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Error", runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-12017" );
   }


   /**
    *
    * test ALWPK-12018 - Duplicate Batch Part exists in Maintenix INV_INV table, Can't identify
    * which batch part it should be linked to.
    *
    */
   @Test
   public void testALWPK_12018() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_4 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_4 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_4 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1_3 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_4 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_4 + "\'" );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" );
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_2 + "\'" ); // its the wrong loc_cd
      lWPK.put( "SCHED_START_DT", "SYSDATE" );
      lWPK.put( "SCHED_END_DT", "SYSDATE+1" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-12018" );
   }


   /**
    *
    * test ALWPK-12019 - AL_WORK_PACKAGE:Work Package Serial Number cannot be XXX
    *
    */
   @Test
   public void testALWPK_12019() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + "XXX" + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_3 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_3 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_3 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_3 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_1 + "\'" );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" );
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_2 + "\'" ); // its the wrong loc_cd
      lWPK.put( "SCHED_START_DT", "SYSDATE" );
      lWPK.put( "SCHED_END_DT", "SYSDATE+1" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-12019" );
   }


   /**
    *
    * test ALWPK-20000 - AL_WORK_PACKAGE.WKP wkp_serial number/wkp_part
    * _no_oem/wkp_manufacurer_cd/wkp name already exists in Maintenix table for root ACFT Inventory
    * and is active (EVT_EVENT)
    *
    *
    */
   @Test
   public void testALWPK_20000() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1_2 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_1 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_1 + "\'" );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" );
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_1 + "\'" );
      lWPK.put( "SCHED_START_DT", "SYSDATE" );
      lWPK.put( "SCHED_END_DT", "SYSDATE+1" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-20000" );
   }


   /**
    *
    * test ALWPK-20001 - AL_WORK_PACKAGE.WKP wkp_serial number/wkp_part _no_oem/wkp_manufacurer_cd
    * already exists in Maintenix table for not root ACFT Inventory and is active (EVT_EVENT)
    *
    *
    */
   @Test
   public void testALWPK_20001() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_3 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_3 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_3 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_3_1 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_2 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_3 + "\'" );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" );
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_3 + "\'" );
      lWPK.put( "SCHED_START_DT", "SYSDATE" );
      lWPK.put( "SCHED_END_DT", "SYSDATE+1" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-20001" );
   }


   /**
    *
    * test ALWPK-20202 - AL_WORK_PACKAGE: Work Type list cannot end in comma
    *
    */
   @Test
   public void testALWPK_20202() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_1 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_1 + "\'" );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" );
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_1 + "\'" );
      lWPK.put( "SCHED_START_DT", "SYSDATE" );
      lWPK.put( "SCHED_END_DT", "SYSDATE+1" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "," + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-20202" );

   }


   /**
    *
    * Test ALWPK-20002: There are more than one manufact_cd with the same serial and part numbers,
    * manufact_cd is required.
    *
    */

   @Test
   public void testALWPK_20020() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_8 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_1 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_1 + "\'" );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" );
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_1 + "\'" );
      lWPK.put( "SCHED_START_DT", "SYSDATE" );
      lWPK.put( "SCHED_END_DT", "SYSDATE+1" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-20020" );

   }


   /**
    *
    * Test ALWPK-20003: There are more than one with the same serial, part numbers, manufact_cd.
    *
    */
   @Test
   public void testALWPK_20003() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_8 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_1 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_1 + "\'" );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" );
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_1 + "\'" );
      lWPK.put( "SCHED_START_DT", "SYSDATE" );
      lWPK.put( "SCHED_END_DT", "SYSDATE+1" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-20003" );

   }

}
