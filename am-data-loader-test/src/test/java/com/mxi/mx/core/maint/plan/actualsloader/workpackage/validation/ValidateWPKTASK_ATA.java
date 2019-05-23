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

import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.WorkPKGTest;


/**
 * These have all the unhappy paths related to AL_WORK_PACKAGE_TASK_ATA table.
 *
 * @author Geoff Hyde
 *
 */
public class ValidateWPKTASK_ATA extends WorkPKGTest {

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

      String strTCName = testName.getMethodName();

      if ( strTCName.contains( "20021" ) )
         dataSetup20021();
      else if ( strTCName.contains( "12221" ) )
         dataSetup12221();
      else if ( strTCName.contains( "12216" ) )
         dataSetup12216();
      else if ( strTCName.contains( "20203" ) )
         dataSetup20203();
      else if ( strTCName.contains( "20204" ) )
         dataSetup20204();
   }


   /**
    * Clean up after each individual test
    */
   @After
   @Override
   public void after() {

      try {
         String strTCName = testName.getMethodName();
         if ( strTCName.contains( "12221" ) )
            runUpdate( "UPDATE inv_inv SET serial_no_oem = '" + iSERIAL_NO_OEM_TASk_8
                  + "' WHERE inv_no_db_id = " + iDuplcateInvNoIds.getNO_DB_ID()
                  + " AND inv_no_id = " + iDuplcateInvNoIds.getNO_ID() );
         else if ( strTCName.contains( "20021" ) ) {
            runUpdate( "UPDATE eqp_part_no SET part_no_oem = '" + iPART_NO_OEM_TASK_1
                  + "' WHERE part_no_db_id = " + iDuplcatePartNoIds.getNO_DB_ID()
                  + " AND part_no_id = " + iDuplcatePartNoIds.getNO_ID() );
            runUpdate( "UPDATE inv_inv SET serial_no_oem = '" + iSERIAL_NO_OEM_TASk_1
                  + "' WHERE inv_no_db_id = " + iDuplcateInvNoIds.getNO_DB_ID()
                  + " AND inv_no_id = " + iDuplcateInvNoIds.getNO_ID() );
         } else if ( strTCName.contains( "12216" ) )
            runUpdate( "UPDATE sched_stask SET fault_db_id = " + iOriginalFaultIds.getNO_DB_ID()
                  + ", fault_id = " + iOriginalFaultIds.getNO_ID() + " WHERE sched_db_id = "
                  + iDuplcateInvNoIds.getNO_DB_ID() + " AND sched_id = "
                  + iDuplcateInvNoIds.getNO_ID() );
         else if ( strTCName.contains( "20203" ) ) {
            UpdateFaultSdesc( iFaultTsIds, iFAULT_SDESC_7 );
            UpdateFaultSdesc( iFaultCfIds, iFAULT_SDESC_7 );
         } else if ( strTCName.contains( "20204" ) ) {
            UpdateFaultSdesc( iFaultTsIds, iEventSdesc2 );
            UpdateTaskId( iFaultTsIds, iOrginalTaskId.getNO_ID() );
         }
         super.after();
      } catch ( Exception e ) {
         e.printStackTrace();
      }
   }


   /**
    * This test is to verify error code ALWPK-10200: AL_WORK_PACKAGE_TASK_ATA.wkp_serial_no_oem
    * cannot be null or consist entirely of spaces.
    *
    *
    */
   @Test
   public void test_ALWPK_10200_1() {

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
      // lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_1 + "\'" );
      lWPK.put( "ATA_SYS_CD", "\'" + iATA_SYS_CD_1 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK_ATA, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-10200" );
   }


   /**
    * This test is to verify error code ALWPK-10200: AL_WORK_PACKAGE_TASK_ATA.wkp_serial_no_oem
    * cannot be null or consist entirely of spaces.
    *
    *
    */
   @Test
   public void test_ALWPK_10200_2() {

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
      lWPK.put( "WKP_SERIAL_NO_OEM", "'" + "  " + "'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_1 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK_ATA, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-10200" );
   }


   /**
    * This test is to verify error code ALWPK-10201: AL_WORK_PACKAGE_TASK_ATA.wkp_part_no_oem cannot
    * be null or consist entirely of spaces.
    *
    */
   @Test
   public void test_ALWPK_10201_1() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

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
      lWPK.put( "SCHED_START_DT", "SYSDATE" );
      lWPK.put( "SCHED_END_DT", "SYSDATE+1" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      // AL_WORK_PACKAGE_TASK_ATA
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_2 + "\'" );
      // lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_2 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_2 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_2 + "\'" );
      lWPK.put( "SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_2 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_2 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_2 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_4 + "\'" );
      lWPK.put( "ATA_SYS_CD", "\'" + iATA_SYS_CD_2 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK_ATA, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-10201" );

   }


   /**
    * This test is to verify error code ALWPK-10201: AL_WORK_PACKAGE_TASK_ATA.wkp_part_no_oem cannot
    * be null or consist entirely of spaces.
    *
    */

   @Test
   public void test_ALWPK_10201_2() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // AL_WORK_PACKAGE table
      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_2 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + "  " + "\'" );
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

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      // AL_WORK_PACKAGE_TASK_ATA
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_2 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + "  " + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_2 + "\'" );
      lWPK.put( "SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_2 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_2 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_4 + "\'" );
      lWPK.put( "ATA_SYS_CD", "\'" + iATA_SYS_CD_1 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK_ATA, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-10201" );

   }


   /**
    * This test is to verify error code ALWPK-10203:AL_WORK_PACKAGE_TASK_ATA.wkp_name cannot be null
    * or consist entirely of spaces.
    *
    *
    */
   @Test
   public void test_ALWPK_10203_1() {

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
      // lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "FAULT_SDESC", "\'" + iFAULT_SDESC_2 + "\'" );
      lWPK.put( "ATA_SYS_CD", "\'" + iATA_SYS_CD_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK_ATA, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-10203" );

   }


   /**
    * This test is to verify error code ALWPK-10203:AL_WORK_PACKAGE_TASK_ATA.wkp_name cannot be null
    * or consist entirely of spaces.
    *
    *
    */
   @Test
   public void test_ALWPK_10203_2() {

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
      lWPK.put( "WKP_NAME", "\'" + "   " + "\'" );
      lWPK.put( "SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "FAULT_SDESC", "\'" + iFAULT_SDESC_2 + "\'" );
      lWPK.put( "ATA_SYS_CD", "\'" + iATA_SYS_CD_1 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK_ATA, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-10203" );

   }


   /**
    * This test is to verify error code ALWPK-10204: AL_WORK_PACKAGE_TASK_ATA.serial_no_oem cannot
    * be null or consist entirely of spaces.
    *
    *
    */
   @Test
   public void test_ALWPK_10204_1() {

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
      // lWPK.put( "SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_1 + "\'" );
      lWPK.put( "ATA_SYS_CD", "\'" + iATA_SYS_CD_1 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK_ATA, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-10204" );
   }


   /**
    * This test is to verify error code ALWPK-10204: AL_WORK_PACKAGE_TASK_ATA.serial_no_oem cannot
    * be null or consist entirely of spaces.
    *
    *
    */
   @Test
   public void test_ALWPK_10204_2() {

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
      lWPK.put( "SERIAL_NO_OEM", "\'" + "  " + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_1 + "\'" );
      lWPK.put( "ATA_SYS_CD", "\'" + iATA_SYS_CD_1 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK_ATA, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-10204" );
   }


   /**
    * This test is to verify error code ALWPK-10205: AL_WORK_PACKAGE_TASK_ATA.part_no_oem cannot be
    * null or consist entirely of spaces.
    *
    */
   @Test
   public void test_ALWPK_10205_1() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

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
      lWPK.put( "SCHED_START_DT", "SYSDATE" );
      lWPK.put( "SCHED_END_DT", "SYSDATE+1" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      // AL_WORK_PACKAGE_TASK_ATA
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_2 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_2 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_2 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_2 + "\'" );
      lWPK.put( "SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      // lWPK.put( "PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_4 + "\'" );
      lWPK.put( "ATA_SYS_CD", "\'" + iATA_SYS_CD_1 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK_ATA, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-10205" );

   }


   /**
    * This test is to verify error code ALWPK-10205: AL_WORK_PACKAGE_TASK_ATA.part_no_oem cannot be
    * null or consist entirely of spaces.
    *
    */

   @Test
   public void test_ALWPK_10205_2() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

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
      lWPK.put( "SCHED_START_DT", "SYSDATE" );
      lWPK.put( "SCHED_END_DT", "SYSDATE+1" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      // AL_WORK_PACKAGE_TASK_ATA
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_2 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_2 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_2 + "\'" );
      lWPK.put( "SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + "     " + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_2 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_4 + "\'" );
      lWPK.put( "ATA_SYS_CD", "\'" + iATA_SYS_CD_1 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK_ATA, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-10205" );

   }


   /**
    * This test is to verify error code ALWPK-10207:AL_WORK_PACKAGE_TASK_ATA:A task or a fault must
    * be provided.
    *
    *
    */
   @Test
   public void test_ALWPK_10207_1() {

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
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      // lWPK.put( "FAULT_SDESC", "\'" + iFAULT_SDESC_2 + "\'" );
      // lWPK.put( "TASK_CD", "\'" + iTASK_CD_3 + "\'" );
      lWPK.put( "ATA_SYS_CD", "\'" + iATA_SYS_CD_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK_ATA, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-10207" );

   }


   /**
    * This test is to verify error code ALWPK-10207:AL_WORK_PACKAGE_TASK_ATA:A task or a fault must
    * be provided.
    *
    *
    */
   @Test
   public void test_ALWPK_10207_2() {

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
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "FAULT_SDESC", "\'" + "  " + "\'" );
      lWPK.put( "TASK_CD", "\'" + " " + "\'" );
      lWPK.put( "ATA_SYS_CD", "\'" + iATA_SYS_CD_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK_ATA, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-10207" );
   }


   /**
    * This test is to verify error code ALWPK-10208:AL_WORK_PACKAGE_TASK_ATA:ata_sys_cd cannot be
    * null or consist entirely of spaces
    *
    *
    */
   @Test
   public void test_ALWPK_10208_1() {

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
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "FAULT_SDESC", "\'" + iFAULT_SDESC_2 + "\'" );
      // lWPK.put( "ATA_SYS_CD", "\'" + iATA_SYS_CD_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK_ATA, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-10208" );

   }


   /**
    * This test is to verify error code ALWPK-11200: AL_WORK_PACKAGE_TASK_ATA.serial_no_oem/
    * part_no_oem/manufact_cd/ata_sys_cd/task_cd exists multiple times in the staging area
    *
    */
   @Test
   public void test_ALWPK_11200() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_work_pckkage table
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
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // AL_WORK_PACKAGE_TASK_ATA
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_ata_5 + "\'" );
      lWPK.put( "ATA_SYS_CD", "\'" + iata_sys_cd_1 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK_ATA, lWPK ) );
      // insert map duplicate row
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK_ATA, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-11200" );

   }


   /**
    *
    * This test is to verify error code ALWPK-11201: AL_WORK_PACKAGE_TASK_ATA. serial_no_oem/
    * part_no_oem/manufact_cd/fault_sdesc exists multiple times in the staging area
    *
    *
    */
   @Test
   public void test_ALWPK_11201() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_work_pckkage table
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
      lWPK.put( "SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "FAULT_SDESC", "\'" + iFAULT_SDESC_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // AL_WORK_PACKAGE_TASK_ATA
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "FAULT_SDESC", "\'" + iFAULT_SDESC_2_ata + "\'" );
      lWPK.put( "ATA_SYS_CD", "\'" + iFAULT_SDESC_ats_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK_ATA, lWPK ) );
      // insert map 2nd Duplicate entry
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK_ATA, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-11201" );
   }


   /**
    * This test is to verify error code ALWPK-12200:
    * AL_WORK_PACKAGE_TASK_ATA.wkp_part_no_oem/wkp_manufact_cd/wkp_serial_no_oem/wkp_name not found
    * in staging table (AL_WORK_PACKAGE)
    *
    *
    */
   @Test
   public void test_ALWPK_12200() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

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
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "FAULT_SDESC", "\'" + iFAULT_SDESC_2 + "\'" );
      lWPK.put( "ATA_SYS_CD", "\'" + iATA_SYS_CD_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK_ATA, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-12200" );

   }


   /**
    * This test is to verify error code ALWPK-12203: AL_WORK_PACKAGE_TASK_ATA.ata_sys_cd does not
    * exist in Maintenix table EQP_ASSMBL_BOM.
    *
    *
    */
   @Test
   public void test_ALWPK_12203() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // AL_WORK_PACKAGE table
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

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      // AL_WORK_PACKAGE_TASK_ATA
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1_2 + "\'" );
      lWPK.put( "SERIAL_NO_OEM", "\'" + iSERIAL_NO_OEM_TASk_1 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_TASK_1 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_1 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_ata_5 + "\'" );
      lWPK.put( "ATA_SYS_CD", "\'" + "INVALID" + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK_ATA, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-12203" );

   }


   /**
    * temp test
    *
    * DOCUMENT_ME
    *
    */
   @Test

   public void test_ALWPK_12203_1() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // AL_WORK_PACKAGE table
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

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      // AL_WORK_PACKAGE_TASK_ATA
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1_2 + "\'" );
      lWPK.put( "SERIAL_NO_OEM", "\'" + iSERIAL_NO_OEM_TASk_1 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_TASK_1 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_1 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_ata_5 + "\'" );
      lWPK.put( "ATA_SYS_CD", "\'" + iATA_SYS_CD_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK_ATA, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-12203" );

   }


   /**
    * This test is to verify error code ALWPK-12205:AL_WORK_PACKAGE_TASK_ATA.part_no_oem/manufact_cd
    * not found in Maintenix EQP_PART_NO table.
    *
    *
    */
   @Test
   public void test_ALWPK_12205() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // AL_WORK_PACKAGE table
      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + "INVALID" + "\'" );
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
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "MANUFACT_CD", "'" + "INVALID" + "'" );
      lWPK.put( "FAULT_SDESC", "\'" + iFAULT_SDESC_2 + "\'" );
      lWPK.put( "ATA_SYS_CD", "\'" + iATA_SYS_CD_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK_ATA, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-12205" );
   }


   /**
    * This test is to verify error code ALWPK-12206:AL_WORK_PACKAGE_TASK_ATA.part_no_oem/manufact_cd
    * not found in Maintenix EQP_PART_NO table.
    *
    *
    */
   @Test
   public void test_ALWPK_12206() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // AL_WORK_PACKAGE table
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

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      // AL_WORK_PACKAGE_TASK_ATA
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_2 + "\'" );
      lWPK.put( "FAULT_SDESC", "\'" + iFAULT_SDESC_2 + "\'" );
      lWPK.put( "ATA_SYS_CD", "\'" + iATA_SYS_CD_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK_ATA, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-12206" );
   }


   /**
    * This test is to verify error code ALWPK-12207:
    * AL_WORK_PACKAGE_TASK_ATA.part_no_oem/manufact_cd/serial_no_oem/sys_ata_cd not found in
    * Maintenix INV_INV table
    *
    */
   @Test
   public void test_ALWPK_12207() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // AL_WORK_PACKAGE table
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

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      // AL_WORK_PACKAGE_TASK_ATA
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_2 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "FAULT_SDESC", "\'" + iFAULT_SDESC_2 + "\'" );
      lWPK.put( "ATA_SYS_CD", "\'" + iATA_SYS_CD_2 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK_ATA, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-12207" );
   }


   /**
    * This test is to verify error code ALWPK-12208:AL_WORK_PACKAGE_TASK_ATA.task_cd not found in
    * Maintenix TASK_TASK table.
    *
    *
    */
   @Test
   public void test_ALWPK_12208() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

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
      lWPK.put( "SCHED_START_DT", "SYSDATE" );
      lWPK.put( "SCHED_END_DT", "SYSDATE+1" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      // AL_WORK_PACKAGE_TASK_ATA
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_2 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_2 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_2 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_2 + "\'" );
      lWPK.put( "SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_2 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_2 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_2 + "\'" );
      lWPK.put( "TASK_CD", "'INVALID'" );
      lWPK.put( "ATA_SYS_CD", "\'" + iATA_SYS_CD_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK_ATA, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-12208" );

   }


   /**
    * This test is to verify error code ALWPK-12210:AL_WORK_PACKAGE_TASK_ATA.task_cd has a
    * class_mode of JIC.
    *
    *
    */
   @Test
   public void test_ALWPK_12210() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // AL_WORK_PACKAGE table
      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + "SN000016" + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + "A0000001" + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + "10001" + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_1 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_1 + "\'" );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" );
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_5 + "\'" );
      lWPK.put( "SCHED_START_DT", "SYSDATE" );
      lWPK.put( "SCHED_END_DT", "SYSDATE+1" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      // AL_WORK_PACKAGE_TASK_ATA
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_5 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_5 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_5 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_5 + "\'" );
      lWPK.put( "SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_5 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_5 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_5 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_12 + "\'" );
      lWPK.put( "ATA_SYS_CD", "\'" + iATA_SYS_CD_5 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK_ATA, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-12210" );

   }


   /**
    * This test is to verify error code ALWPK-12211:AL_WORK_PACKAGE_TASK_ATA.
    * wkp_part_no_oem/wkp_manufact_cd/wkp_serial_no_oem and
    * part_no_oem/manufact_cd/serial_no_oem/ata_sys_cd must be against the same root inventory
    *
    *
    */
   @Test
   public void test_ALWPK_12211() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // AL_WORK_PACKAGE table
      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_10 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_10 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_10 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_10 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_10 + "\'" );
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
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_10 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_10 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_10 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_10 + "\'" );
      lWPK.put( "SERIAL_NO_OEM", "\'" + iSERIAL_NO_OEM_10 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_10 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_10 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_15 + "\'" );
      lWPK.put( "ATA_SYS_CD", "\'" + "ENG-SYS-1" + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK_ATA, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-12211" );

   }


   /**
    * This test is to verify error code ALWPK-12213:AL_WORK_PACKAGE_TASK_ATA.fault_sdesc does not
    * exist in Maintenix table (EVT_EVENT).
    *
    *
    */
   @Test
   public void test_ALWPK_12213() {

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
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "FAULT_SDESC", "'INVALID'" );
      lWPK.put( "ATA_SYS_CD", "\'" + iATA_SYS_CD_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK_ATA, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-12213" );

   }


   /**
    * This test is to verify error code ALWPK-12216: AL_WORK_PACKAGE_TASK_ATA.part_no_oem/
    * manufact_cd/ serial_no_oem/ata_sys_cd/fault_sdesc does not exist in Maintenix table
    * (SCHED_STASK)
    *
    *
    */
   @Test
   public void test_ALWPK_12216() {

      System.out.println( "=======Starting: " + testName.getMethodName()
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
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "FAULT_SDESC", "\'" + iFAULT_SDESC_7 + "\'" );
      lWPK.put( "ATA_SYS_CD", "\'" + iATA_SYS_CD_7 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK_ATA, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-12216" );

   }


   /**
    * This test is to verify error code ALWPK-12217: AL_WORK_PACKAGE_TASK_ATA.serial_no_oem is
    * invalid
    *
    *
    */
   @Test
   public void test_ALWPK_12217() {

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
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "SERIAL_NO_OEM", "\'" + "INVALID" + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_1 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK_ATA, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-12217" );
   }


   /**
    * This test is to verify error code ALWPK-10201: AL_WORK_PACKAGE_TASK_ATA.part_no_oem is invalid
    *
    */
   @Test
   public void test_ALWPK_12218() {

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
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + "INVALID" + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_1 + "\'" );
      lWPK.put( "ATA_SYS_CD", "\'" + iATA_SYS_CD_1 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK_ATA, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-12218" );

   }


   /**
    * This test is to verify error code ALWPK-12219: AL_WORK_PACKAGE_TASK_ATA.serial_no_oem/
    * part_no_oem/manufact_cd/ata_sys_cd/task_cd exists multiple times in the staging area
    *
    */
   @Test
   public void test_ALWPK_12219() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // AL_WORK_PACKAGE table
      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_1 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_2 + "\'" );
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
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "FAULT_SDESC", "\'" + iFAULT_SDESC_2 + "\'" );
      lWPK.put( "ATA_SYS_CD", "\'" + iATA_SYS_CD_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK_ATA, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-12219" );
   }


   /**
    * This test is to verify error code ALWPK-12220:
    * C_WORK_PACKAGE_TASK_ATA.serial_no_oem/part_no_oem/manufact_cd/task_cd not found in Maintenix
    * EVT_EVENT table
    *
    */
   @Test
   public void test_ALWPK_12220() {

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
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_11 + "\'" );
      lWPK.put( "ATA_SYS_CD", "\'" + iATA_SYS_CD_5 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK_ATA, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-12220" );// SN000001 ACFT_ASSY_PN1

   }


   /**
    * This test is to verify error code ALWPK-12221: AL_WORK_PACKAGE_TASK: Split Batch inventory
    * found for specified task inventory. Split batch inventory not supported
    *
    *
    */
   @Test
   public void test_ALWPK_12221() {

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
      lWPK.put( "SCHED_START_DT", "SYSDATE" );
      lWPK.put( "SCHED_END_DT", "SYSDATE+1" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

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
      lWPK.put( "ATA_SYS_CD", "\'" + iata_sys_cd_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK_ATA, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-12221" );

   }


   /**
    * This test is to verify error code ALWPK-12222:AL_WORK_PACKAGE_TASK_ATA.Serial_No_Oem cannot be
    * XXX
    *
    */
   @Test
   public void test_ALWPK_12222() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // AL_WORK_PACKAGE table
      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_3 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_3 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_3 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_3 + "\'" );
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
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_3 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_3 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_3 + "\'" );
      lWPK.put( "SERIAL_NO_OEM", "\'" + "XXX" + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "FAULT_SDESC", "\'" + iFAULT_SDESC_3 + "\'" );
      lWPK.put( "ATA_SYS_CD", "\'" + iATA_SYS_CD_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK_ATA, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-12222" );

   }


   /**
    * This test is to verify error code ALWPK-20200:AL_WORK_PACKAGE_TASK_ATA.serial
    * number/part_no_oem/ manufacurer_cd/task_cd/ata_sys_cd/ is already assigned a
    * nh_event_db_id/nh_event_id in Maintenix table (EVT_EVENT)
    *
    */
   @Test
   public void test_ALWPK_20200() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_work_pckkage table
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

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      // AL_WORK_PACKAGE_TASK_ATA
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1_2 + "\'" );
      lWPK.put( "SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_ata_6 + "\'" );
      lWPK.put( "ATA_SYS_CD", "\'" + iata_sys_cd_1 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK_ATA, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-20200" );
   }


   /**
    * This test is to verify error code ALWPK-20200:AL_WORK_PACKAGE_TASK_ATA.serial number/
    * part_no_oem/ manufacurer_cd/ata_sys_cd/fault_sdesc is already assigned a
    * nh_event_db_id/nh_event_id in Maintenix table (EVT_EVENT)
    *
    */
   @Ignore
   @Test
   public void test_ALWPK_20201() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_work_pckkage table
      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_7 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_7 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_3 + "\'" );
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
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_7 + "\'" );
      lWPK.put( "SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "FAULT_SDESC", "\'" + iFAULT_SDESC_7 + "\'" );
      lWPK.put( "ATA_SYS_CD", "\'" + iATA_SYS_CD_7 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK_ATA, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-20201" );

   }


   /**
    * This test is to verify error code ALWPK-20007: AL_WORK_PACKAGE_TASK_ATA:FAULT_SDESC must be
    * null when TASK_CD provided and vice versa
    *
    *
    */
   @Test
   public void test_ALWPK_20007() {

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
      lWPK.put( "FAULT_SDESC", "\'" + iFAULT_SDESC_2 + "\'" );
      lWPK.put( "ATA_SYS_CD", "\'" + iATA_SYS_CD_7 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK_ATA, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-20007" );
   }


   /**
    *
    * This test is to verify error code ALWPK-20203: Multiple active faults found for this task ata
    * inventory
    *
    */
   @Test
   public void test_ALWPK_20203() {

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
      lWPK.put( "FAULT_SDESC", "\'" + iFAULT_SDESC_4 + "\'" );
      lWPK.put( "ATA_SYS_CD", "\'" + iATA_SYS_CD_7 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK_ATA, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-20203" );

   }


   /**
    *
    * This test is to verify error code ALWPK-20204: Multiple active Task_Cd found for this task ata
    * inventory
    *
    */
   @Test
   public void test_ALWPK_20204() {

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
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_ata_5 + "\'" );
      lWPK.put( "ATA_SYS_CD", "\'" + iata_sys_cd_1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK_ATA, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-20204" );

   }


   /**
    *
    * test ALWPK-20021 - For inventory duplicate part number and manufacturer code combination exist
    * in Maintenix for provided manufacturer code
    *
    *
    */
   @Test
   public void test_ALWPK_20021() {

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
      lWPK.put( "ATA_SYS_CD", "\'" + iATA_SYS_CD_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK_ATA, lWPK ) );

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-20021" );
   }


   /**
    * Creates duplicate batch parts in the Inv_inv table for ALWKP-12221
    *
    */
   private void dataSetup12221() {
      String lQuery = "SELECT inv_no_db_id, inv_no_id FROM inv_inv WHERE serial_no_oem = '"
            + iSERIAL_NO_OEM_TASk_8 + "'";
      iDuplcateInvNoIds = getIDs( lQuery, "INV_NO_DB_ID", "INV_NO_ID" );
      lQuery = "UPDATE inv_inv SET serial_no_oem = '" + iWKP_SERIAL_NO_OEM_4
            + "' WHERE inv_no_db_id = " + iDuplcateInvNoIds.getNO_DB_ID() + " AND inv_no_id = "
            + iDuplcateInvNoIds.getNO_ID();
      runUpdate( lQuery );

   }


   /**
    * Creates duplicate batch parts in the Inv_inv table for ALWKP-12216
    */
   private void dataSetup12216() {
      String lQuery = "SELECT event_db_id, event_id FROM evt_event WHERE event_sdesc = '"
            + iFAULT_SDESC_7 + "' and event_type_cd = 'TS'";
      iDuplcateInvNoIds = getIDs( lQuery, "EVENT_DB_ID", "EVENT_ID" );
      lQuery = "SELECT fault_db_id, fault_id FROM sched_stask WHERE sched_db_id = "
            + iDuplcateInvNoIds.getNO_DB_ID() + " AND sched_id = " + iDuplcateInvNoIds.getNO_ID();
      iOriginalFaultIds = getIDs( lQuery, "fault_db_id", "fault_id" );
      lQuery = "UPDATE sched_stask SET fault_db_id = NULL, fault_id = NULL WHERE sched_db_id = "
            + iDuplcateInvNoIds.getNO_DB_ID() + " AND sched_id = " + iDuplcateInvNoIds.getNO_ID();
      runUpdate( lQuery );
   }


   /**
    * Setup data so there duplicate combination of Serial and Part with a different Manufacture
    *
    */
   private void dataSetup20021() {
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
    *
    * data setup to create a duplicate active fault_sdesc.
    *
    *
    */
   public void dataSetup20203() {
      String lQuery = "SELECT event_db_id, event_id FROM evt_event WHERE event_sdesc = '"
            + iFAULT_SDESC_7 + "' and event_type_cd = 'TS' and event_status_cd = 'ACTV'";
      iFaultTsIds = getIDs( lQuery, "event_db_id", "event_id" );
      lQuery = "SELECT event_db_id, event_id FROM evt_event WHERE event_sdesc = '" + iFAULT_SDESC_7
            + "' and event_type_cd = 'CF' and event_status_cd = 'CFDEFER'";
      iFaultCfIds = getIDs( lQuery, "event_db_id", "event_id" );
      UpdateFaultSdesc( iFaultTsIds, iFAULT_SDESC_4 );
      UpdateFaultSdesc( iFaultCfIds, iFAULT_SDESC_4 );
   }


   /**
    *
    * data setup to create a duplicate active task_cd.
    *
    *
    */
   public void dataSetup20204() {
      // get record the needs to be duplicated
      String lQuery = "SELECT event_db_id, event_id FROM evt_event WHERE event_sdesc LIKE '"
            + iTASK_CD_ata_5 + "%' and event_type_cd = 'TS' and event_status_cd = 'ACTV'";
      simpleIDs lDupIds = getIDs( lQuery, "event_db_id", "event_id" );
      // get ids for the record that needs to be updated
      lQuery = "SELECT event_db_id, event_id FROM evt_event WHERE event_sdesc LIKE '"
            + iTASK_CD_ata_6 + "%' and event_type_cd = 'TS' and event_status_cd LIKE 'ACTV'";
      iFaultTsIds = getIDs( lQuery, "event_db_id", "event_id" );
      // get the new SDesc
      lQuery = "SELECT event_sdesc FROM evt_event WHERE event_db_id = " + lDupIds.getNO_DB_ID()
            + " AND event_id = " + lDupIds.getNO_ID();
      String lEventSdesc1 = getStringValueFromQuery( lQuery, "EVENT_SDESC" );
      // get the original SDesc
      lQuery = "SELECT event_sdesc FROM evt_event WHERE event_db_id = " + iFaultTsIds.getNO_DB_ID()
            + " AND event_id = " + iFaultTsIds.getNO_ID();
      iEventSdesc2 = getStringValueFromQuery( lQuery, "EVENT_SDESC" );

      // update Sdesc
      UpdateFaultSdesc( iFaultTsIds, lEventSdesc1 );

      lQuery = "SELECT task_db_id, task_id FROM sched_stask  WHERE sched_db_id = "
            + lDupIds.getNO_DB_ID() + " AND sched_id = " + lDupIds.getNO_ID();
      simpleIDs lDupTaskCdIds = getIDs( lQuery, "task_db_id", "task_id" );

      lQuery = "SELECT task_db_id, task_id FROM sched_stask  WHERE sched_db_id = "
            + iFaultTsIds.getNO_DB_ID() + " AND sched_id = " + iFaultTsIds.getNO_ID();
      iOrginalTaskId = getIDs( lQuery, "task_db_id", "task_id" );

      UpdateTaskId( iFaultTsIds, lDupTaskCdIds.getNO_ID() );

   }


   /**
    *
    * Perform evt_event updates to specific records.
    *
    * @param aEventId
    *           - event Ids to identify the specific record
    * @param aSdesc
    *           - this will be the new event_sdesc
    */
   private void UpdateFaultSdesc( simpleIDs aEventId, String aSdesc ) {
      String lQuery = "UPDATE evt_event SET event_sdesc = '" + aSdesc + "' WHERE event_db_id = "
            + aEventId.getNO_DB_ID() + " AND event_id = " + aEventId.getNO_ID();
      runUpdate( lQuery );
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
   private void UpdateTaskId( simpleIDs aUpdateRecord, String aNewTaskId ) {
      String lQuery = "UPDATE sched_stask SET task_id = '" + aNewTaskId + "' WHERE sched_db_id = "
            + aUpdateRecord.getNO_DB_ID() + " AND sched_id = " + aUpdateRecord.getNO_ID();
      runUpdate( lQuery );
   }
}
