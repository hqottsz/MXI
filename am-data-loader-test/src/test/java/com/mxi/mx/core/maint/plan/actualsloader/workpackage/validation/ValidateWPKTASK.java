package com.mxi.mx.core.maint.plan.actualsloader.workpackage.validation;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.WorkPKGTest;


/**
 * This test suite contains test cases on validation functionality of AL_WORK_PACKAGE_PKG package
 * for error codes on AL_WORK_PACKAGE_TASK table
 *
 * @author ALICIA QIAN
 */
public class ValidateWPKTASK extends WorkPKGTest {

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
      clearActualsLoaderTables();
      DataSetup();

      String strTCName = testName.getMethodName();
      if ( strTCName.contains( "12115" ) ) {
         dataSetup12115();
      } // else if ( strTCName.contains( "20101" ) ) {
        // dataSetup20101();
      else if ( strTCName.contains( "20100" ) ) {
         dataSetup20100();
      } else if ( strTCName.contains( "12118" ) )
         dataSetup12118();
      else if ( strTCName.contains( "20022" ) )
         dataSetup20022();
      else if ( strTCName.contains( "20011" ) )
         dataSetup20011();
      else if ( strTCName.contains( "20103" ) )
         dataSetup20103();
      else if ( strTCName.contains( "20104" ) )
         dataSetup20104();

   }


   /**
    * Clean up after each individual test
    */
   @After
   @Override
   public void after() {
      String strTCName = testName.getMethodName();
      try {
         RestoreData();
         if ( strTCName.contains( "12118" ) )
            runUpdate( "UPDATE inv_inv SET serial_no_oem = 'SNBTH001AT' WHERE inv_no_db_id = "
                  + iDuplcateInvNoIds.getNO_DB_ID() + " AND inv_no_id = "
                  + iDuplcateInvNoIds.getNO_ID() );
         else if ( strTCName.contains( "20022" ) ) {
            runUpdate( "UPDATE eqp_part_no SET part_no_oem = '" + iPART_NO_OEM_TASK_1
                  + "' WHERE part_no_db_id = " + iDuplcatePartNoIds.getNO_DB_ID()
                  + " AND part_no_id = " + iDuplcatePartNoIds.getNO_ID() );
            runUpdate( "UPDATE inv_inv SET serial_no_oem = '" + iSERIAL_NO_OEM_TASk_1
                  + "' WHERE inv_no_db_id = " + iDuplcateInvNoIds.getNO_DB_ID()
                  + " AND inv_no_id = " + iDuplcateInvNoIds.getNO_ID() );
         } else if ( strTCName.contains( "20011" ) )
            runUpdate( "UPDATE inv_inv SET serial_no_oem = '" + "XXX" + "' WHERE inv_no_db_id = "
                  + iDuplcatePartNoIds.getNO_DB_ID() + " AND inv_no_id = "
                  + iDuplcatePartNoIds.getNO_ID() );
         else if ( strTCName.contains( "20103" ) ) {
            UpdateFaultSdesc( iFaultTsIds, iFAULT_SDESC_6 );
            UpdateFaultSdesc( iFaultCfIds, iFAULT_SDESC_6 );
         } else if ( strTCName.contains( "20104" ) )
            UpdateMainInvNoIds( iFaultTsIds, iOriginalValue.getNO_ID() );

         super.after();
      } catch ( Exception e ) {
         e.printStackTrace();
      }
   }


   /**
    * This test is to verify error code ALWPK-10100: AL_WORK_PACKAGE_TASK.wkp_serial_no_oem cannot
    * be null or consist entirely of spaces.
    *
    *
    */
   @Test
   public void test_ALWPK_10100_1() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // AL_WORK_PACKAGE table
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
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_1 );

      // AL_WORK_PACKAGE_TASK
      lWPK.clear();
      // lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );

      lWPK.put( "SERIAL_NO_OEM", "\'" + iSERIAL_NO_OEM_TASk_1 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_TASK_1 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_1 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_1 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-10100" );
   }


   /**
    * This test is to verify error code ALWPK-10100: AL_WORK_PACKAGE_TASK.wkp_serial_no_oem cannot
    * be null or consist entirely of spaces.
    *
    *
    */
   @Test
   public void test_ALWPK_10100_2() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // AL_WORK_PACKAGE table
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
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_1 );

      // AL_WORK_PACKAGE_TASK
      lWPK.clear();
      // lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_SERIAL_NO_OEM", "'     '" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );

      lWPK.put( "SERIAL_NO_OEM", "\'" + iSERIAL_NO_OEM_TASk_1 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_TASK_1 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_1 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_1 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-10100" );
   }


   /**
    * This test is to verify error code ALWPK-10101: AL_WORK_PACKAGE_TASK.wkp_part_no_oem cannot be
    * null or consist entirely of spaces.
    *
    */

   @Test
   public void test_ALWPK_10101_1() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      iEVENT_1 = null;

      // AL_WORK_PACKAGE table
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
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_2 );

      // AL_WORK_PACKAGE_TASK
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_2 + "\'" );
      // lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_2 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_2 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_2 + "\'" );

      lWPK.put( "SERIAL_NO_OEM", "\'" + iSERIAL_NO_OEM_TASk_4 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_TASK_4 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_4 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_4 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-10101" );

   }


   /**
    * This test is to verify error code ALWPK-10101: AL_WORK_PACKAGE_TASK.wkp_part_no_oem cannot be
    * null or consist entirely of spaces.
    *
    */

   @Test
   public void test_ALWPK_10101_2() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      iEVENT_1 = null;

      // AL_WORK_PACKAGE table
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
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_2 );

      // AL_WORK_PACKAGE_TASK
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_2 + "\'" );
      // lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_2 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "'     '" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_2 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_2 + "\'" );

      lWPK.put( "SERIAL_NO_OEM", "\'" + iSERIAL_NO_OEM_TASk_4 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_TASK_4 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_4 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_4 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-10101" );

   }


   /**
    * This test is to verify error code ALWPK-10103:AL_WORK_PACKAGE_TASK.wkp_name cannot be null or
    * consist entirely of spaces.
    *
    *
    */
   @Test
   public void test_ALWPK_10103() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      iEVENT_1 = null;

      // AL_WORK_PACKAGE table
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
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_1 );

      // AL_WORK_PACKAGE_TASK
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      // lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );

      lWPK.put( "SERIAL_NO_OEM", "\'" + iSERIAL_NO_OEM_TASk_2 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_TASK_2 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_2 + "\'" );
      lWPK.put( "FAULT_SDESC", "\'" + iFAULT_SDESC_2 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-10103" );

   }


   /**
    * This test is to verify error code ALWPK-10104:AL_WORK_PACKAGE_TASK.serial_no_oem cannot be
    * null or consist entirely of spaces.
    *
    *
    */
   @Test
   public void test_ALWPK_10104() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      iEVENT_1 = null;

      // AL_WORK_PACKAGE table
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
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_1 );

      // AL_WORK_PACKAGE_TASK
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );

      // lWPK.put( "SERIAL_NO_OEM", "\'" + iSERIAL_NO_OEM_TASk_3 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_TASK_3 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_3 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_3 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-10104" );

   }


   /**
    * This test is to verify error code ALWPK-10105:AL_WORK_PACKAGE_TASK.part_no_oem cannot be null
    * or consist entirely of spaces.
    *
    *
    */
   @Test
   public void test_ALWPK_10105() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      iEVENT_1 = null;

      // AL_WORK_PACKAGE table
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
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_1 );

      // AL_WORK_PACKAGE_TASK
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );

      lWPK.put( "SERIAL_NO_OEM", "\'" + iSERIAL_NO_OEM_TASk_3 + "\'" );
      // lWPK.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_TASK_3 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_3 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_3 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-10105" );

   }


   /**
    * This test is to verify error code ALWPK-10107:AL_WORK_PACKAGE_TASK:A task or a fault must be
    * provided.
    *
    *
    */
   @Test
   public void test_ALWPK_10107_1() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      iEVENT_1 = null;

      // AL_WORK_PACKAGE table
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
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_1 );

      // AL_WORK_PACKAGE_TASK
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );

      lWPK.put( "SERIAL_NO_OEM", "\'" + iSERIAL_NO_OEM_TASk_2 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_TASK_2 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_2 + "\'" );
      // lWPK.put( "FAULT_SDESC", "\'" + iFAULT_SDESC_2 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-10107" );

   }


   /**
    * This test is to verify error code ALWPK-10107:AL_WORK_PACKAGE_TASK:A task or a fault must be
    * provided.
    *
    *
    */
   @Test
   public void test_ALWPK_10107_2() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // AL_WORK_PACKAGE table
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
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_1 );

      // AL_WORK_PACKAGE_TASK
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );

      lWPK.put( "SERIAL_NO_OEM", "\'" + iSERIAL_NO_OEM_TASk_1 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_TASK_1 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_1 + "\'" );
      // lWPK.put( "TASK_CD", "\'" + iTASK_CD_1 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-10107" );
   }


   /**
    * This test is to verify error code
    * ALWPK-12101:AL_WORK_PACKAGE_TASK.wkp_part_no_oem/wkp_manufact_cd/wkp_serial_no_oem/wkp_name
    * not found in the AL_WORK_PACKAGE staging table.
    *
    *
    */
   @Test
   public void test_ALWPK_12101_1() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // AL_WORK_PACKAGE table
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
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_1 );

      // AL_WORK_PACKAGE table
      lWPK.clear();
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
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_2 );

      // AL_WORK_PACKAGE_TASK
      lWPK.clear();
      // lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_2 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );

      lWPK.put( "SERIAL_NO_OEM", "\'" + iSERIAL_NO_OEM_TASk_1 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_TASK_1 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_1 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_1 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-12101" );
   }


   /**
    * This test is to verify error code
    * ALWPK-12101:AL_WORK_PACKAGE_TASK.wkp_part_no_oem/wkp_manufact_cd/wkp_serial_no_oem/wkp_name
    * not found in the AL_WORK_PACKAGE staging table.
    *
    *
    */
   @Test
   public void test_ALWPK_12101_2() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // AL_WORK_PACKAGE table
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
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_1 );

      // AL_WORK_PACKAGE table
      lWPK.clear();
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
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_2 );

      // AL_WORK_PACKAGE_TASK
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      // lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_2 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );

      lWPK.put( "SERIAL_NO_OEM", "\'" + iSERIAL_NO_OEM_TASk_1 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_TASK_1 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_1 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_1 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-12101" );
   }


   /**
    * This test is to verify error code
    * ALWPK-12101:AL_WORK_PACKAGE_TASK.wkp_part_no_oem/wkp_manufact_cd/wkp_serial_no_oem/wkp_name
    * not found in the AL_WORK_PACKAGE staging table.
    *
    *
    */
   @Test
   public void test_ALWPK_12101_3() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // AL_WORK_PACKAGE table
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
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_1 );

      // AL_WORK_PACKAGE table
      lWPK.clear();
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
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_2 );

      // AL_WORK_PACKAGE_TASK
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      // lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_2 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );

      lWPK.put( "SERIAL_NO_OEM", "\'" + iSERIAL_NO_OEM_TASk_1 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_TASK_1 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_1 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_1 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-12101" );
   }


   /**
    * This test is to verify error code
    * ALWPK-12101:AL_WORK_PACKAGE_TASK.wkp_part_no_oem/wkp_manufact_cd/wkp_serial_no_oem/wkp_name
    * not found in the AL_WORK_PACKAGE staging table.
    *
    *
    */
   @Test
   public void test_ALWPK_12101_4() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // AL_WORK_PACKAGE table
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
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_1 );

      // AL_WORK_PACKAGE table
      lWPK.clear();
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
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_2 );

      // AL_WORK_PACKAGE_TASK
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      // lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_2 + "\'" );

      lWPK.put( "SERIAL_NO_OEM", "\'" + iSERIAL_NO_OEM_TASk_1 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_TASK_1 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_1 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_1 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-12101" );
   }


   /**
    * This test is to verify error code
    * ALWPK-12101:AL_WORK_PACKAGE_TASK.wkp_part_no_oem/wkp_manufact_cd/wkp_serial_no_oem/wkp_name
    * not found in the AL_WORK_PACKAGE staging table.
    *
    *
    */
   @Test
   public void test_ALWPK_12101_5() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // AL_WORK_PACKAGE table
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
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_1 );

      // AL_WORK_PACKAGE table
      lWPK.clear();
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
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_2 );

      // AL_WORK_PACKAGE_TASK
      lWPK.clear();
      // lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_2 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_2 + "\'" );
      // lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_2 + "\'" );

      lWPK.put( "SERIAL_NO_OEM", "\'" + iSERIAL_NO_OEM_TASk_1 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_TASK_1 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_1 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_1 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-12101" );
   }


   /**
    * This test is to verify error code
    * ALWPK-12101:AL_WORK_PACKAGE_TASK.wkp_part_no_oem/wkp_manufact_cd/wkp_serial_no_oem/wkp_name
    * not found in the AL_WORK_PACKAGE staging table.
    *
    *
    */
   @Test
   public void test_ALWPK_12101_6() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // AL_WORK_PACKAGE table
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
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_1 );

      // AL_WORK_PACKAGE table
      lWPK.clear();
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
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_2 );

      // AL_WORK_PACKAGE_TASK
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      // lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_2 + "\'" );
      lWPK.put( "WKP_NAME", "'INVALID'" );

      lWPK.put( "SERIAL_NO_OEM", "\'" + iSERIAL_NO_OEM_TASk_1 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_TASK_1 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_1 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_1 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-12101" );
   }


   /**
    * This test is to verify error code ALWPK-12102:AL_WORK_PACKAGE_TASK:serial number is invalid.
    *
    *
    */
   @Test
   public void test_ALWPK_12102() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      iEVENT_1 = null;

      // AL_WORK_PACKAGE table
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
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_1 );

      // AL_WORK_PACKAGE_TASK
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );

      // lWPK.put( "SERIAL_NO_OEM", "\'" + iSERIAL_NO_OEM_TASk_2 + "\'" );
      lWPK.put( "SERIAL_NO_OEM", "'INVALID'" );
      lWPK.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_TASK_2 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_2 + "\'" );
      lWPK.put( "FAULT_SDESC", "\'" + iFAULT_SDESC_2 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-12102" );
   }


   /**
    * This test is to verify error code ALWPK-12103:AL_WORK_PACKAGE_TASK.part_no_oem not found in
    * Maintenix EQP_PART_NO table.
    *
    *
    */
   @Test
   public void test_ALWPK_12103() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      iEVENT_1 = null;

      // AL_WORK_PACKAGE table
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
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_1 );

      // AL_WORK_PACKAGE_TASK
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );

      lWPK.put( "SERIAL_NO_OEM", "\'" + iSERIAL_NO_OEM_TASk_3 + "\'" );
      // lWPK.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_TASK_3 + "\'" );
      lWPK.put( "PART_NO_OEM", "'INVALID'" );
      lWPK.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_3 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_3 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-12103" );

   }


   /**
    * This test is to verify error code ALWPK-12104:AL_WORK_PACKAGE_TASK.part_no_oem not found in
    * Maintenix EQP_PART_NO table.
    *
    *
    */
   @Test
   public void test_ALWPK_12104() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      iEVENT_1 = null;

      // AL_WORK_PACKAGE table
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
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_2 );

      // AL_WORK_PACKAGE_TASK
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_2 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_2 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_2 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_2 + "\'" );

      lWPK.put( "SERIAL_NO_OEM", "\'" + iSERIAL_NO_OEM_TASk_4 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_TASK_4 + "\'" );
      // lWPK.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_4 + "\'" );
      lWPK.put( "MANUFACT_CD", "'INVALID'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_4 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-12104" );

   }


   /**
    * This test is to verify error code ALWPK-12105:AL_WORK_PACKAGE_TASK.part_no_oem/manufact_cd not
    * found in Maintenix EQP_PART_NO table.
    *
    *
    */
   @Test
   public void test_ALWPK_12105() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      iEVENT_1 = null;

      // AL_WORK_PACKAGE table
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
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_1 );

      // AL_WORK_PACKAGE_TASK
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );

      lWPK.put( "SERIAL_NO_OEM", "\'" + iSERIAL_NO_OEM_TASk_2 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_TASK_2 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + "INVALID" + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_4 + "\'" );
      lWPK.put( "FAULT_SDESC", "\'" + iFAULT_SDESC_2 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-12105" );

   }


   /**
    * This test is to verify error code
    * ALWPK-12107:AL_WORK_PACKAGE_TASK.part_no_oem/manufact_cd/serial_no_oem not found in Maintenix
    * INV_INV table.
    *
    *
    */

   @Test
   public void test_ALWPK_12107() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      iEVENT_1 = null;

      // AL_WORK_PACKAGE table
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
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_1 );

      // AL_WORK_PACKAGE_TASK
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );

      // lWPK.put( "SERIAL_NO_OEM", "\'" + iSERIAL_NO_OEM_TASk_3 + "\'" );
      lWPK.put( "SERIAL_NO_OEM", "\'" + iSERIAL_NO_OEM_TASk_2 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_TASK_3 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_3 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_3 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-12107" );
   }


   /**
    * This test is to verify error code ALWPK-12108:AL_WORK_PACKAGE_TASK.task_cd not found in
    * Maintenix TASK_TASK table.
    *
    *
    */
   @Test
   public void test_ALWPK_12108() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      iEVENT_1 = null;

      // AL_WORK_PACKAGE table
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
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_2 );

      // AL_WORK_PACKAGE_TASK
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_2 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_2 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_2 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_2 + "\'" );

      lWPK.put( "SERIAL_NO_OEM", "\'" + iSERIAL_NO_OEM_TASk_4 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_TASK_4 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_4 + "\'" );
      lWPK.put( "TASK_CD", "'INVALID'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-12108" );

   }


   /**
    * This test is to verify error code ALWPK-12110:AL_WORK_PACKAGE_TASK.task_cd has a class_mode of
    * REQ or BLOCK.
    *
    *
    */
   @Test
   public void test_ALWPK_12110_1() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      iEVENT_1 = null;

      // AL_WORK_PACKAGE table
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
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_1 );

      // AL_WORK_PACKAGE_TASK
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "SERIAL_NO_OEM", "\'" + iSERIAL_NO_OEM_TASk_3 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_TASK_3 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_3 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_14 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-12110" );

   }


   /**
    * This test is to verify error code ALWPK-12110:AL_WORK_PACKAGE_TASK.task_cd has a class_mode of
    * JIC.
    *
    *
    */
   @Test
   public void test_ALWPK_12110_2() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      iEVENT_1 = null;

      // AL_WORK_PACKAGE table
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
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_1 );

      // AL_WORK_PACKAGE_TASK
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "SERIAL_NO_OEM", "\'" + iSERIAL_NO_OEM_TASk_3 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_TASK_3 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_3 + "\'" );
      // lWPK.put( "TASK_CD", "\'" + iTASK_CD_3 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_6 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-12110" );

   }


   /**
    * This test is to verify error code ALWPK-12113:AL_WORK_PACKAGE_TASK.fault_sdesc does not exist
    * in Maintenix table (EVT_EVENT).
    *
    *
    */
   @Test
   public void test_ALWPK_12113() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      iEVENT_1 = null;

      // AL_WORK_PACKAGE table
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
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_1 );

      // AL_WORK_PACKAGE_TASK
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );

      lWPK.put( "SERIAL_NO_OEM", "\'" + iSERIAL_NO_OEM_TASk_2 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_TASK_2 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_2 + "\'" );
      // lWPK.put( "FAULT_SDESC", "\'" + iFAULT_SDESC_2 + "\'" );
      lWPK.put( "FAULT_SDESC", "'INVALID'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-12113" );

   }


   /**
    * This test is to verify error code
    * ALWPK-12115:CAL_WORK_PACKAGE_TASK.part_no_oem/manufact_cd/serial_no_oem/fault_sdesc does not
    * exist in Maintenix table (SCHED_STASK).
    *
    *
    */

   @Test
   public void test_ALWPK_12115() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      iEVENT_1 = null;

      // AL_WORK_PACKAGE table
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
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_1 );

      // AL_WORK_PACKAGE_TASK
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );

      lWPK.put( "SERIAL_NO_OEM", "\'" + iSERIAL_NO_OEM_TASk_2 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_TASK_2 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_2 + "\'" );
      lWPK.put( "FAULT_SDESC", "\'" + iFAULT_SDESC_2 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-12115" );
   }


   /**
    * This test is to verify error code
    * ALWPK-12116:AL_WORK_PACKAGE_TASK.serial_no_oem/part_no_oem/manufact_cd/fault_sdesc not found
    * in Maintenix EVT_EVENT table.
    *
    *
    */
   @Test
   public void test_ALWPK_12116() {

      System.out.println( "=======Starting: " + testName.getMethodName()
            + " Validation========================" );

      iEVENT_1 = null;

      // AL_WORK_PACKAGE table
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

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      // AL_WORK_PACKAGE_TASK
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );

      lWPK.put( "SERIAL_NO_OEM", "\'" + iSERIAL_NO_OEM_TASk_2 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_TASK_2 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_2 + "\'" );
      lWPK.put( "FAULT_SDESC", "\'" + iFAULT_SDESC_2_ata + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-12116" );

   }


   /**
    * This test is to verify error code ALWPK-12117:
    * AL_WORK_PACKAGE_TASK.serial_no_oem/part_no_oem/manufact_cd/task_cd not found in Maintenix
    * EVT_EVENT table.
    *
    *
    */
   @Test
   public void test_ALWPK_12117() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // AL_WORK_PACKAGE table
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

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      // AL_WORK_PACKAGE_TASK
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );

      lWPK.put( "SERIAL_NO_OEM", "\'" + iSERIAL_NO_OEM_TASk_1 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_TASK_1 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_1 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_8 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-12117" );
   }


   /**
    * This test is to verify error code ALWPK-12118: AL_WORK_PACKAGE_TASK: Split Batch inventory
    * found for specified task inventory. Split batch inventory not supported
    *
    *
    */
   @Test
   public void test_ALWPK_12118() {

      System.out.println( "=======Starting: " + testName.getMethodName()
            + " Validation========================" );

      // AL_WORK_PACKAGE table
      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_4 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_4 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_4 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_4 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_4 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_4 + "\'" );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" );
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_4 + "\'" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_1 );

      // AL_WORK_PACKAGE_TASK
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_4 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_4 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_4 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_4 + "\'" );

      lWPK.put( "SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_4 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_4 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_4 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_8 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-12118" );
   }


   /**
    * This test is to verify error code ALWPK-12119: AL_WORK_PACKAGE_TASK:Serial Number cannot be
    * XXX.
    *
    *
    */

   @Test
   public void test_ALWPK_12119() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      iEVENT_1 = null;

      // AL_WORK_PACKAGE table
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
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_1 );

      // AL_WORK_PACKAGE_TASK
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );

      // lWPK.put( "SERIAL_NO_OEM", "\'" + iSERIAL_NO_OEM_TASk_1 + "\'" );
      lWPK.put( "SERIAL_NO_OEM", "'XXX'" );
      lWPK.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_TASK_1 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_1 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_1 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-12119" );

   }


   /**
    * This test is to verify error code ALWPK-11100: AL_WORK_PACKAGE_TASK: Serial Number/ Part
    * Number /Manufacturer / Task exists multiple times in the staging area.
    *
    *
    */
   @Test
   public void test_ALWPK_11100() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // AL_WORK_PACKAGE table
      Map<String, String> lWPK = new LinkedHashMap<>();
      // first record
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
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_1 );

      // second record
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_2 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_1 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_1 + "\'" );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" );
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_1 + "\'" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_2 );

      // AL_WORK_PACKAGE_TASK
      // First record
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );

      lWPK.put( "SERIAL_NO_OEM", "\'" + iSERIAL_NO_OEM_TASk_1 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_TASK_1 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_1 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_1 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // second record
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_2 + "\'" );

      lWPK.put( "SERIAL_NO_OEM", "\'" + iSERIAL_NO_OEM_TASk_1 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_TASK_1 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_1 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_1 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-11100" );
   }


   /**
    * This test is to verify error code
    * ALWPK-11101:AL_WORK_PACKAGE_TASK.serial_no_oem/part_no_oem/manufact_cd/fault_sdesc exists
    * multiple times in the staging area.
    *
    *
    */
   @Test
   public void test_ALWPK_11101() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // AL_WORK_PACKAGE table
      Map<String, String> lWPK = new LinkedHashMap<>();

      // first record
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
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_1 );

      // second record
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_2 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_1 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_1 + "\'" );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" );
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_1 + "\'" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_2 );

      // AL_WORK_PACKAGE_TASK
      // first record
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );

      lWPK.put( "SERIAL_NO_OEM", "\'" + iSERIAL_NO_OEM_TASk_2 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_TASK_2 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_2 + "\'" );
      lWPK.put( "FAULT_SDESC", "\'" + iFAULT_SDESC_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // second record
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_2 + "\'" );

      lWPK.put( "SERIAL_NO_OEM", "\'" + iSERIAL_NO_OEM_TASk_2 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_TASK_2 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_2 + "\'" );
      lWPK.put( "FAULT_SDESC", "\'" + iFAULT_SDESC_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-11101" );
   }


   /**
    * This test is to verify error code ALWPK-20100: AL_WORK_PACKAGE_TASK.serial number/
    * part_no_oem/ manufacurer_cd/task is already assigned a nh_event_db_id/nh_event_id in Maintenix
    * table (EVT_EVENT).
    *
    */

   @Test
   public void test_ALWPK_20100() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      iEVENT_1 = null;

      // AL_WORK_PACKAGE table
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
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_2 );

      // AL_WORK_PACKAGE_TASK
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_2 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_2 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_2 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_2 + "\'" );

      lWPK.put( "SERIAL_NO_OEM", "\'" + iSERIAL_NO_OEM_TASk_4 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_TASK_4 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_4 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_4 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-20100" );

   }


   /**
    * This test is to verify error code ALWPK-20101:AL_WORK_PACKAGE_TASK.serial number/ part_no_oem/
    * manufacurer_cd/task is already assigned a nh_event_db_id/nh_event_id in Maintenix table
    * (EVT_EVENT).
    *
    *
    */
   @Test
   public void test_ALWPK_20101() {

      System.out.println( "=======Starting: " + testName.getMethodName()
            + " Validation========================" );

      iEVENT_1 = null;

      // AL_WORK_PACKAGE table
      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1_4 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_1 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_1 + "\'" );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" );
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_1 + "\'" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_1_4 );

      // AL_WORK_PACKAGE_TASK
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1_4 + "\'" );
      lWPK.put( "SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "FAULT_SDESC", "\'" + iFAULT_SDESC_9 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-20101" );

   }


   /**
    *
    * This test is to verify error code ALWPK-20103: Multiple active faults found for this task
    * inventory
    *
    */
   @Test
   public void test_ALWPK_20103() {

      System.out.println( "=======Starting: " + testName.getMethodName()
            + " Validation========================" );

      iEVENT_1 = null;

      // AL_WORK_PACKAGE table
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
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_1 );

      // AL_WORK_PACKAGE_TASK
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );

      lWPK.put( "SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "FAULT_SDESC", "\'" + iFAULT_SDESC_5 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-20103" );

   }


   /**
    *
    * This test is to verify error code ALWPK-20104: Multiple active Task_Cd found for this task
    * inventory
    *
    */
   @Test
   public void test_ALWPK_20104() {

      System.out.println( "=======Starting: " + testName.getMethodName()
            + " Validation========================" );

      iEVENT_1 = null;

      // AL_WORK_PACKAGE table
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
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_1 );

      // AL_WORK_PACKAGE_TASK
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_16 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-20104" );

   }


   /**
    * This test is to verify error code - ALWPK-20004 - Work package task inventory must be
    * installed on the work package inventory. It will also cover ALWPK-12111:
    * AL_WORK_PACKAGE_TASK.wkp_part_no_oem/wkp_manufact_cd/wkp_serial_no_oem and
    * part_no_oem/manufact_cd/serial_no_oem must be against the same root inventory.
    *
    *
    */
   @Test
   public void test_ALWPK_20004() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // AL_WORK_PACKAGE table
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
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_1 );

      // AL_WORK_PACKAGE_TASK
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );

      lWPK.put( "SERIAL_NO_OEM", "\'" + iSERIAL_NO_OEM_TASk_7 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_TASK_7 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_7 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_7 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-20004" );
   }


   // /**
   // *
   // * test ALWPK-20004 - Work package task inventory must be installed on the work package
   // * inventory.
   // *
   // *
   // */
   // @Test
   // public void test_ALWPK_20004() {
   //
   // System.out.println( "=======Starting: " + testName.getMethodName()
   // + " Validation========================" );
   //
   // // AL_WORK_PACKAGE table
   // Map<String, String> lWPK = new LinkedHashMap<>();
   //
   // // first record
   // lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
   // lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
   // lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
   // lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
   // lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_1 + "\'" );
   // lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_1 + "\'" );
   // lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
   // lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" );
   // lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
   // lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_1 + "\'" );
   // lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );
   //
   // // insert map
   // runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );
   //
   // // AL_WORK_PACKAGE_TASK
   // // first record
   // lWPK.clear();
   // lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
   // lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
   // lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
   // lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
   // lWPK.put( "SERIAL_NO_OEM", "\'" + iSERIAL_NO_OEM_10 + "\'" );
   // lWPK.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_10 + "\'" );
   // lWPK.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_10 + "\'" );
   // lWPK.put( "FAULT_SDESC", "\'" + iFAULT_SDESC_7 + "\'" );
   //
   // // insert map
   // runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );
   //
   // // run validation
   // Assert.assertTrue( "Validation Passed when expected an error ",
   // runValidationAndImport( true, true ) == -2 );
   //
   // // Validate error
   // validateErrorCode( "ALWPK-20004" );
   // }

   /**
    * This test is to verify error code ALWPK-20005: AL_WORK_PACKAGE_TASK:FAULT_SDESC must be null
    * when TASK_CD provided and vice versa
    *
    *
    */
   @Test
   public void test_ALWPK_20005() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // AL_WORK_PACKAGE table
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

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      // AL_WORK_PACKAGE_TASK_ATA
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_1 + "\'" );
      lWPK.put( "FAULT_SDESC", "\'" + iFAULT_SDESC_7 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-20005" );
   }


   /**
    *
    * test ALWPK-20008 - This becomes invalid because child work package is invalid
    *
    *
    */
   @Test
   public void test_ALWPK_20006() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // AL_WORK_PACKAGE table
      Map<String, String> lWPK = new LinkedHashMap<>();

      // first record
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
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_1 );

      // AL_WORK_PACKAGE_TASK
      // first record
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "SERIAL_NO_OEM", "\'" + iSERIAL_NO_OEM_TASk_2 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_TASK_2 + "\'" );
      // lWPK.put( "FAULT_SDESC", "\'" + iFAULT_SDESC_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-20006" );
   }


   /**
    *
    * test ALWPK-20008_1 - One child and parent work package becomes invalid because other child is
    * invalid
    *
    *
    */
   @Test
   public void test_ALWPK_20008_1() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // AL_WORK_PACKAGE table
      Map<String, String> lWPK = new LinkedHashMap<>();

      // first record
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
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_1 );

      // AL_WORK_PACKAGE_TASK
      // first record
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "FAULT_SDESC", "\'" + iFAULT_SDESC_6 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // second record
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      // lWPK.put( "SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "FAULT_SDESC", "\'" + iFAULT_SDESC_6 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-20008", "ALWPK-20006" );
   }


   /**
    *
    * test ALWPK-20008_2 - Child becomes invalid because parent work package is invalid
    *
    *
    */
   @Test
   public void test_ALWPK_20008_2() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // AL_WORK_PACKAGE table
      Map<String, String> lWPK = new LinkedHashMap<>();

      // first record
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
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      // setCurrentDate( iWKP_NAME_1 ); // Make's parent invalid

      // AL_WORK_PACKAGE_TASK
      // first record
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "FAULT_SDESC", "\'" + iFAULT_SDESC_6 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-20008" );
   }


   /**
    *
    * test ALWPK-20011 - inventory.
    *
    *
    */
   @Test
   public void test_ALWPK_20011() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // AL_WORK_PACKAGE table
      Map<String, String> lWPK = new LinkedHashMap<>();

      // first record
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
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_1 );

      // AL_WORK_PACKAGE_TASK
      // first record
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "SERIAL_NO_OEM", "\'" + "XXXTESTSN4" + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_TASK_9 + "\'" );
      lWPK.put( "FAULT_SDESC", "\'" + iFAULT_SDESC_7 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-20011" );
   }


   /**
    *
    * test ALWPK-20022 - For inventory duplicate part number and manufacturer code combination exist
    * in Maintenix for provided manufacturer code
    *
    *
    */
   @Test
   public void test_ALWPK_20022() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // AL_WORK_PACKAGE table
      Map<String, String> lWPK = new LinkedHashMap<>();

      // first record
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
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_1 );

      // AL_WORK_PACKAGE_TASK
      // first record
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "SERIAL_NO_OEM", "\'" + iSERIAL_NO_OEM_TASk_2 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_TASK_2 + "\'" );
      // lWPK.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_2 + "\'" );
      lWPK.put( "FAULT_SDESC", "\'" + iFAULT_SDESC_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-20022" );
   }


   // ==========================================================================
   /**
    * This function is data set up for 12115.
    *
    *
    */
   public void dataSetup12115() {
      String lQuery = "select fault_id from sched_stask " + "inner join evt_event on "
            + "evt_event.event_db_id=sched_stask.sched_db_id and "
            + "evt_event.event_id=sched_stask.sched_id "
            + "where evt_event.event_sdesc='TestTRKFAULT'";
      iFaultTRKSchedID = getStringValueFromQuery( lQuery, "fault_id" );

      lQuery = "select sched_db_id, sched_id from sched_stask " + "inner join evt_event on "
            + "evt_event.event_db_id=sched_stask.sched_db_id and "
            + "evt_event.event_id=sched_stask.sched_id "
            + "where evt_event.event_sdesc='TestTRKFAULT'";

      iFaultTRKIDs = getIDs( lQuery, "sched_db_id", "sched_id" );

      iFaultTRKFaultIDNew = String.valueOf( ( Integer.parseInt( iFaultTRKSchedID ) + 1 ) );

      lQuery = "update sched_stask set fault_id=" + iFaultTRKFaultIDNew + " where fault_id="
            + iFaultTRKSchedID;
      executeSQL( lQuery );

   }


   /**
    * This function is data set up for 20101.
    *
    *
    */
   public void dataSetup20101() {
      String lquery =
            "update evt_event set nh_event_db_id=4650, nh_event_id=0 where event_sdesc ='TestTRKFAULT' and event_type_cd='TS'";
      executeSQL( lquery );

   }


   /**
    * This function is data set up for 20100.
    *
    *
    */
   public void dataSetup20100() {
      String lquery =
            "update evt_event set nh_event_db_id=4650, nh_event_id=0 where event_sdesc ='REQ4 (REQ4)' and event_type_cd='TS'";
      executeSQL( lquery );

   }


   /**
    *
    * data setup to create a duplicate active fault_sdesc.
    *
    *
    */
   public void dataSetup20103() {
      String lQuery = "SELECT event_db_id, event_id FROM evt_event WHERE event_sdesc = '"
            + iFAULT_SDESC_6 + "' and event_type_cd = 'TS' and event_status_cd = 'ACTV'";
      iFaultTsIds = getIDs( lQuery, "event_db_id", "event_id" );
      lQuery = "SELECT event_db_id, event_id FROM evt_event WHERE event_sdesc = '" + iFAULT_SDESC_6
            + "' and event_type_cd = 'CF' and event_status_cd = 'CFDEFER'";
      iFaultCfIds = getIDs( lQuery, "event_db_id", "event_id" );
      UpdateFaultSdesc( iFaultTsIds, iFAULT_SDESC_5 );
      UpdateFaultSdesc( iFaultCfIds, iFAULT_SDESC_5 );
   }


   /**
    *
    * Perform evt_event updates to specific records.
    *
    * @param iFaultId
    *           - event Ids to identify the specific record
    * @param aSdesc
    *           - this will be the new event_sdesc
    */
   private void UpdateFaultSdesc( simpleIDs iFaultId, String aSdesc ) {
      String lQuery = "UPDATE evt_event SET event_sdesc = '" + aSdesc + "' WHERE event_db_id = "
            + iFaultId.getNO_DB_ID() + " AND event_id = " + iFaultId.getNO_ID();
      runUpdate( lQuery );
   }


   /**
    * Setup data so there duplicate combination of Serial, Part, and Manufacture
    *
    */
   private void dataSetup20011() {
      String lQuery = "SELECT part_no_db_id, part_no_id FROM eqp_part_no WHERE part_no_oem = '"
            + iPART_NO_OEM_TASK_9 + "' AND manufact_cd = '" + iMANUFACT_CD_9 + "'";
      iDuplcatePartNoIds = getIDs( lQuery, "part_no_db_id", "part_no_id" );
      lQuery = "UPDATE inv_inv SET serial_no_oem = '" + iSERIAL_NO_OEM_TASk_9
            + "' WHERE part_no_db_id = " + iDuplcatePartNoIds.getNO_DB_ID() + " AND part_no_id = "
            + iDuplcatePartNoIds.getNO_ID();
      runUpdate( lQuery );
   }


   /**
    * Setup data so there duplicate combination of Serial and Part with a different Manufacture
    *
    */
   private void dataSetup20022() {
      // setup Serial No
      String lQuery = "SELECT inv_no_db_id, inv_no_id FROM inv_inv WHERE serial_no_oem = '"
            + iSERIAL_NO_OEM_TASk_1 + "'";
      iDuplcateInvNoIds = getIDs( lQuery, "INV_NO_DB_ID", "INV_NO_ID" );
      lQuery = "UPDATE inv_inv SET serial_no_oem = '" + iSERIAL_NO_OEM_TASk_2
            + "' WHERE inv_no_db_id = " + iDuplcateInvNoIds.getNO_DB_ID() + " AND inv_no_id = "
            + iDuplcateInvNoIds.getNO_ID();
      runUpdate( lQuery );
      // setup Part No
      lQuery = "SELECT part_no_db_id, part_no_id FROM eqp_part_no WHERE part_no_oem = '"
            + iPART_NO_OEM_TASK_1 + "'";
      iDuplcatePartNoIds = getIDs( lQuery, "PART_NO_DB_ID", "PART_NO_ID" );
      lQuery = "UPDATE eqp_part_no SET part_no_oem = '" + iPART_NO_OEM_TASK_2
            + "' WHERE part_no_db_id = " + iDuplcatePartNoIds.getNO_DB_ID() + " AND part_no_id = "
            + iDuplcatePartNoIds.getNO_ID();
      runUpdate( lQuery );
   }


   /**
    * Creates duplicate batch parts in the Inv_inv table for ALWKP-12018
    *
    */
   private void dataSetup12118() {
      String lQuery = "SELECT inv_no_db_id, inv_no_id FROM inv_inv WHERE serial_no_oem = '"
            + iSERIAL_NO_OEM_TASk_8 + "'";
      iDuplcateInvNoIds = getIDs( lQuery, "INV_NO_DB_ID", "INV_NO_ID" );
      lQuery = "UPDATE inv_inv SET serial_no_oem = 'SNBATCH1' WHERE inv_no_db_id = "
            + iDuplcateInvNoIds.getNO_DB_ID() + " AND inv_no_id = " + iDuplcateInvNoIds.getNO_ID();
      runUpdate( lQuery );
   }


   /**
    *
    * data setup to create a duplicate active task_cd.
    *
    *
    */
   public void dataSetup20104() {
      // get ids for the record that needs to be updated
      String lQuery = "SELECT ee.event_db_id, ee.event_id FROM evt_event ee "
            + "INNER JOIN sched_stask ss ON " + "ss.sched_db_id = ee.event_db_id AND "
            + "ss.sched_id = ee.event_id " + "INNER JOIN inv_inv ii ON "
            + "ss.main_inv_no_db_id = ii.inv_no_db_id AND " + "ss.main_inv_no_id = ii.inv_no_id "
            + "WHERE ii.serial_no_oem = '" + iSERIAL_NO_OEM_TASk_10 + "' AND ee.event_sdesc LIKE '"
            + iTASK_CD_16 + "%' AND ee.event_status_cd = 'ACTV'";
      iFaultTsIds = getIDs( lQuery, "event_db_id", "event_id" );

      // get the original inv ids
      lQuery = "SELECT inv_no_db_id, inv_no_id FROM inv_inv WHERE serial_no_oem = '"
            + iSERIAL_NO_OEM_TASk_10 + "'";
      iOriginalValue = getIDs( lQuery, "inv_no_db_id", "inv_no_id" );
      // get the new inv ids
      lQuery = "SELECT inv_no_db_id, inv_no_id FROM inv_inv WHERE serial_no_oem = '"
            + iWKP_SERIAL_NO_OEM_1 + "'";
      simpleIDs lNewValue = getIDs( lQuery, "inv_no_db_id", "inv_no_id" );
      // update main_inv_no_ids
      UpdateMainInvNoIds( iFaultTsIds, lNewValue.getNO_ID() );

   }


   /**
    * This will update the Main_Inv_no_id in the sched_stask table. Used to setup data.
    *
    * @param iFaultTsIds
    *           - this is record will be changed
    * @param no_ID
    *           - the new inv_no_id that will be updated
    */
   private void UpdateMainInvNoIds( simpleIDs aSchedIds, String aNewID ) {
      String lQuery = "UPDATE sched_stask SET main_inv_no_id = " + aNewID + " WHERE sched_db_id = "
            + aSchedIds.getNO_DB_ID() + " AND sched_id = " + aSchedIds.getNO_ID();
      runUpdate( lQuery );
   }
}
