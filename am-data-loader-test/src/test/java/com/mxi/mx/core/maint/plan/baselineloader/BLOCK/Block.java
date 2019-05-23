package com.mxi.mx.core.maint.plan.baselineloader.BLOCK;

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

import com.mxi.mx.core.maint.plan.datamodels.assmbleInfor;
import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;
import com.mxi.mx.util.BaselineLoaderTest;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.ValidationAndImport;
import com.mxi.mx.util.WhereClause;


/**
 * This test suite contains test cases on validation and import functionality on REQ_IMPORT.
 *
 *
 * @author ALICIA QIAN
 */
public class Block extends BaselineLoaderTest {

   @Rule
   public TestName testName = new TestName();
   ValidationAndImport ivalidationandimport;

   public String iACFT_ASSMBLCD = "ACFT_CD1";
   public String iBLOCK_ATA_CD_TRK = "ACFT-SYS-1-1-TRK-BATCH-PARENT";
   public String iBLOCK_TASK_CD_1 = "ATEST";

   public String iBLOCK_TASK_CD_RECURRING = "B-RECURRING-TEST";

   // C_BLOCK table
   public String iBLOCK_TASK_NAME_1 = "ATESTNAME";
   public String iBLOCK_TASK_DESC_1 = "ATESTDESC";
   public String iBLOCK_TASK_REF_SDESC_1 = "ATESTREFDESC";

   public String iCLASS_CD = "CHECK";
   public String iCLASS_CD_BLOCK = "BLOCK";
   public String iCLASS_CD_COMP = "REQ";
   public String iSUBCLASS_CD = "A-CHECK";
   public String iSUBCLASS_CD_COMP = "TEST2";
   public String iORIGINATOR_CD = "AWL";

   public String iAPPLICABILITY_DESC = "1,5-7";
   public String iRESCHED_FROM_CD = "EXECUTE";
   public String iLAST_DEADLINE_DRIVER_BOOL = "Y";
   public String iLAST_DEADLINE_DRIVER_BOOL_N = "1";
   public String iSOFT_DEADLINE_DRIVER_BOOL = "Y";
   public String iSOFT_DEADLINE_DRIVER_BOOL_N = "1";
   public String iSCHED_FROM_MANUFACT_DT_BOOL = "Y";
   public String iSCHED_FROM_MANUFACT_DT_BOOL_N = "1";
   public String iWORKSCOPE_BOOL = "Y";
   public String iWORKSCOPE_BOOL_N = "1";
   public String iTASK_MUST_REMOVE_CD = "OFFPARENT";
   public String iON_CONDITION_BOOL = "N";
   public String iON_CONDITION_BOOL_N = "0";

   // C_BLOCK_DYNAMIC_DEADLINE, C_BLOCK_DYNAMIC_PART_DEADLINE
   public String iSCHED_DATA_TYPE_CD = "USAGE1";
   public String iSCHED_INITIAL_QT = "10";
   public String iSCHED_INTERVAL_QT = "20";
   public String iSCHED_THRESHOLD_QT = "100";

   public String iPART_NO_OEM_TRK = "A0000010";
   public String iMANUFACT_CD_TRK = "11111";

   public String iTASK_DEP_ACTION_CD = "CRT";
   public simpleIDs iTASK_IDs_15067 = null;
   public simpleIDs iTASK_DEFN_IDs_15067 = null;


   /**
    * Clean up after each individual test
    *
    */
   @After
   @Override
   public void after() {
      RestoreData();
      super.after();
   }


   /**
    * Setup before executing each individual test
    *
    * @throws Exception
    */
   @Before
   @Override
   public void before() throws Exception {

      super.before();
      clearBaselineLoaderTables();

   }


   /**
    * This test is to verify JIRA-15067 on block_import.validate_block functionality Block-dynamic
    * deadline RD. 1. In c_block table, create a block with RESCHED_FROM_CD as null, and in
    * C_BLOCK_DYNAMIC_DEADLINE/C_BLOCK_DYNAMIC_PART_DEADLINE table set SCHED_SCHED_INITIAL_QT and
    * SCHED_INTERVAL_QT are null. It should pass validation.
    *
    */

   public void testOPER15067BD1VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "BLOCK_TASK_NAME", "'" + iBLOCK_TASK_NAME_1 + "'" );
      lReqMap.put( "BLOCK_TASK_DESC", "'" + iBLOCK_TASK_DESC_1 + "'" );
      lReqMap.put( "BLOCK_TASK_REF_SDESC", "'" + iBLOCK_TASK_REF_SDESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "LAST_DEADLINE_DRIVER_BOOL", "'" + iLAST_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_DRIVER_BOOL", "'" + iSOFT_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "TASK_MUST_REMOVE_CD", "'" + iTASK_MUST_REMOVE_CD + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK, lReqMap ) );

      // C_BLOCK_DYNAMIC_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_THRESHOLD_QT", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_DYNAMIC_DEADLINE, lReqMap ) );

      // C_BLOCK_DYNAMIC_PART_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_THRESHOLD_QT", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_DYNAMIC_PART_DEADLINE, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   @Test
   public void testOPER15067BD1IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOPER15067BD1VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs_15067 = null;
      iTASK_DEFN_IDs_15067 = null;

      // Verify task_task table
      iTASK_IDs_15067 = getTaskIds( iBLOCK_TASK_CD_1, iBLOCK_TASK_NAME_1 );
      assmbleInfor lassmblinfor = getassmblInfor( iBLOCK_ATA_CD_TRK );

      iTASK_DEFN_IDs_15067 = verifyTaskTask_15067( iTASK_IDs_15067, iCLASS_CD, lassmblinfor,
            iSUBCLASS_CD, iORIGINATOR_CD, iBLOCK_TASK_CD_1, iBLOCK_TASK_NAME_1, iBLOCK_TASK_DESC_1,
            iBLOCK_TASK_REF_SDESC_1, iLAST_DEADLINE_DRIVER_BOOL_N, iSOFT_DEADLINE_DRIVER_BOOL_N,
            iTASK_MUST_REMOVE_CD, iON_CONDITION_BOOL_N );

      // verify task_DEFN
      String lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTASK_DEFN_IDs_15067.getNO_DB_ID() + " and TASK_DEFN_ID="
            + iTASK_DEFN_IDs_15067.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_sched_rule table
      simpleIDs lTypeIds = getTypeIds( iSCHED_DATA_TYPE_CD );
      verifyTASKSCHEDRULE_15067( iTASK_IDs_15067, lTypeIds, iSCHED_THRESHOLD_QT, null );

      // // verify task_interval
      simpleIDs lPartIds = getPartIds( iPART_NO_OEM_TRK, iMANUFACT_CD_TRK );
      verifyTASKINTERVAL_15067( iTASK_IDs_15067, lTypeIds, lPartIds, iSCHED_THRESHOLD_QT, null );

   }


   @Test
   public void testOPER15067BD2VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "BLOCK_TASK_NAME", "'" + iBLOCK_TASK_NAME_1 + "'" );
      lReqMap.put( "BLOCK_TASK_DESC", "'" + iBLOCK_TASK_DESC_1 + "'" );
      lReqMap.put( "BLOCK_TASK_REF_SDESC", "'" + iBLOCK_TASK_REF_SDESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "LAST_DEADLINE_DRIVER_BOOL", "'" + iLAST_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_DRIVER_BOOL", "'" + iSOFT_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "TASK_MUST_REMOVE_CD", "'" + iTASK_MUST_REMOVE_CD + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK, lReqMap ) );

      // C_BLOCK_DYNAMIC_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_INITIAL_QT", "'" + iSCHED_INITIAL_QT + "'" );
      lReqMap.put( "SCHED_INTERVAL_QT", null );
      lReqMap.put( "SCHED_THRESHOLD_QT", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_DYNAMIC_DEADLINE, lReqMap ) );

      // C_BLOCK_DYNAMIC_PART_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_INITIAL_QT", "'" + iSCHED_INITIAL_QT + "'" );
      lReqMap.put( "SCHED_INTERVAL_QT", null );
      lReqMap.put( "SCHED_THRESHOLD_QT", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_DYNAMIC_PART_DEADLINE, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      validateErrorCode( "CFGBLC-12072" );

   }


   @Test
   public void testOPER15067BD2_2VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "BLOCK_TASK_NAME", "'" + iBLOCK_TASK_NAME_1 + "'" );
      lReqMap.put( "BLOCK_TASK_DESC", "'" + iBLOCK_TASK_DESC_1 + "'" );
      lReqMap.put( "BLOCK_TASK_REF_SDESC", "'" + iBLOCK_TASK_REF_SDESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "LAST_DEADLINE_DRIVER_BOOL", "'" + iLAST_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_DRIVER_BOOL", "'" + iSOFT_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "TASK_MUST_REMOVE_CD", "'" + iTASK_MUST_REMOVE_CD + "'" );
      // lReqMap.put( "RESCHED_FROM_CD", "'" + iRESCHED_FROM_CD + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK, lReqMap ) );

      // C_BLOCK_DYNAMIC_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      // lReqMap.put( "SCHED_INITIAL_QT", null );
      // lReqMap.put( "SCHED_INTERVAL_QT", null );
      // lReqMap.put( "SCHED_THRESHOLD_QT", "'" + iSCHED_THRESHOLD_QT + "'" );

      lReqMap.put( "SCHED_INITIAL_QT", null );
      lReqMap.put( "SCHED_INTERVAL_QT", iSCHED_INTERVAL_QT );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_DYNAMIC_DEADLINE, lReqMap ) );

      // C_BLOCK_DYNAMIC_PART_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      // lReqMap.put( "SCHED_INITIAL_QT", null );
      // lReqMap.put( "SCHED_INTERVAL_QT", null );
      // lReqMap.put( "SCHED_THRESHOLD_QT", "'" + iSCHED_THRESHOLD_QT + "'" );

      lReqMap.put( "SCHED_INITIAL_QT", null );
      lReqMap.put( "SCHED_INTERVAL_QT", iSCHED_INTERVAL_QT );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_DYNAMIC_PART_DEADLINE, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      validateErrorCode( "CFGBLC-12117" );

   }


   public void testOPER15067BD3VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "BLOCK_TASK_NAME", "'" + iBLOCK_TASK_NAME_1 + "'" );
      lReqMap.put( "BLOCK_TASK_DESC", "'" + iBLOCK_TASK_DESC_1 + "'" );
      lReqMap.put( "BLOCK_TASK_REF_SDESC", "'" + iBLOCK_TASK_REF_SDESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "LAST_DEADLINE_DRIVER_BOOL", "'" + iLAST_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_DRIVER_BOOL", "'" + iSOFT_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "TASK_MUST_REMOVE_CD", "'" + iTASK_MUST_REMOVE_CD + "'" );
      lReqMap.put( "RESCHED_FROM_CD", "'" + iRESCHED_FROM_CD + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'T'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   @Test
   public void testOPER15067BD3IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOPER15067BD3VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs_15067 = null;
      iTASK_DEFN_IDs_15067 = null;

      // Verify task_task table
      iTASK_IDs_15067 = getTaskIds( iBLOCK_TASK_CD_1, iBLOCK_TASK_NAME_1 );
      assmbleInfor lassmblinfor = getassmblInfor( iBLOCK_ATA_CD_TRK );

      iTASK_DEFN_IDs_15067 = verifyTaskTask_15067( iTASK_IDs_15067, iCLASS_CD, lassmblinfor,
            iSUBCLASS_CD, iORIGINATOR_CD, iBLOCK_TASK_CD_1, iBLOCK_TASK_NAME_1, iBLOCK_TASK_DESC_1,
            iBLOCK_TASK_REF_SDESC_1, iLAST_DEADLINE_DRIVER_BOOL_N, iSOFT_DEADLINE_DRIVER_BOOL_N,
            iTASK_MUST_REMOVE_CD, iON_CONDITION_BOOL_N );

      // verify task_DEFN
      String lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTASK_DEFN_IDs_15067.getNO_DB_ID() + " and TASK_DEFN_ID="
            + iTASK_DEFN_IDs_15067.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_task_dep table
      verifyTASKTASKDEP_15067( iTASK_IDs_15067, iTASK_DEP_ACTION_CD, iTASK_DEFN_IDs_15067 );

   }


   @Test
   public void testOPER15067BD4_1VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "BLOCK_TASK_NAME", "'" + iBLOCK_TASK_NAME_1 + "'" );
      lReqMap.put( "BLOCK_TASK_DESC", "'" + iBLOCK_TASK_DESC_1 + "'" );
      lReqMap.put( "BLOCK_TASK_REF_SDESC", "'" + iBLOCK_TASK_REF_SDESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "LAST_DEADLINE_DRIVER_BOOL", "'" + iLAST_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_DRIVER_BOOL", "'" + iSOFT_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "TASK_MUST_REMOVE_CD", "'" + iTASK_MUST_REMOVE_CD + "'" );
      lReqMap.put( "RESCHED_FROM_CD", "'" + iRESCHED_FROM_CD + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'Y'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK, lReqMap ) );

      // C_BLOCK_DYNAMIC_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_INITIAL_QT", null );
      lReqMap.put( "SCHED_INTERVAL_QT", null );
      lReqMap.put( "SCHED_THRESHOLD_QT", "'" + iSCHED_THRESHOLD_QT + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_DYNAMIC_DEADLINE, lReqMap ) );

      // C_BLOCK_DYNAMIC_PART_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_INITIAL_QT", null );
      lReqMap.put( "SCHED_INTERVAL_QT", null );
      lReqMap.put( "SCHED_THRESHOLD_QT", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_DYNAMIC_PART_DEADLINE, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // get wrong code
      validateErrorCode( "CFGBLC-12116" );

   }


   @Test
   public void testOPER15067BD4_2VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "BLOCK_TASK_NAME", "'" + iBLOCK_TASK_NAME_1 + "'" );
      lReqMap.put( "BLOCK_TASK_DESC", "'" + iBLOCK_TASK_DESC_1 + "'" );
      lReqMap.put( "BLOCK_TASK_REF_SDESC", "'" + iBLOCK_TASK_REF_SDESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "LAST_DEADLINE_DRIVER_BOOL", "'" + iLAST_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_DRIVER_BOOL", "'" + iSOFT_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "TASK_MUST_REMOVE_CD", "'" + iTASK_MUST_REMOVE_CD + "'" );
      lReqMap.put( "RESCHED_FROM_CD", "'" + iRESCHED_FROM_CD + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK, lReqMap ) );

      // C_BLOCK_DYNAMIC_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      // lReqMap.put( "SCHED_INITIAL_QT", null );
      // lReqMap.put( "SCHED_INTERVAL_QT", null );
      lReqMap.put( "SCHED_THRESHOLD_QT", "'" + iSCHED_THRESHOLD_QT + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_DYNAMIC_DEADLINE, lReqMap ) );

      // C_BLOCK_DYNAMIC_PART_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_INITIAL_QT", null );
      lReqMap.put( "SCHED_INTERVAL_QT", null );
      lReqMap.put( "SCHED_THRESHOLD_QT", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_DYNAMIC_PART_DEADLINE, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // get wrong code
      validateErrorCode( "CFGBLC-12116" );

   }


   @Test
   public void testOPER15067BD4_3VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "BLOCK_TASK_NAME", "'" + iBLOCK_TASK_NAME_1 + "'" );
      lReqMap.put( "BLOCK_TASK_DESC", "'" + iBLOCK_TASK_DESC_1 + "'" );
      lReqMap.put( "BLOCK_TASK_REF_SDESC", "'" + iBLOCK_TASK_REF_SDESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "LAST_DEADLINE_DRIVER_BOOL", "'" + iLAST_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_DRIVER_BOOL", "'" + iSOFT_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "TASK_MUST_REMOVE_CD", "'" + iTASK_MUST_REMOVE_CD + "'" );
      lReqMap.put( "RESCHED_FROM_CD", "'" + iRESCHED_FROM_CD + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'Y'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK, lReqMap ) );

      // C_BLOCK_DYNAMIC_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_INITIAL_QT", null );
      lReqMap.put( "SCHED_INTERVAL_QT", null );
      lReqMap.put( "SCHED_THRESHOLD_QT", "'" + iSCHED_THRESHOLD_QT + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_DYNAMIC_DEADLINE, lReqMap ) );

      // C_BLOCK_DYNAMIC_PART_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      // lReqMap.put( "SCHED_INITIAL_QT", null );
      // lReqMap.put( "SCHED_INTERVAL_QT", null );
      lReqMap.put( "SCHED_THRESHOLD_QT", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_DYNAMIC_PART_DEADLINE, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // get wrong code
      validateErrorCode( "CFGBLC-12116" );

   }


   public void testOPER15067BD5VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "BLOCK_TASK_NAME", "'" + iBLOCK_TASK_NAME_1 + "'" );
      lReqMap.put( "BLOCK_TASK_DESC", "'" + iBLOCK_TASK_DESC_1 + "'" );
      lReqMap.put( "BLOCK_TASK_REF_SDESC", "'" + iBLOCK_TASK_REF_SDESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "LAST_DEADLINE_DRIVER_BOOL", "'" + iLAST_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_DRIVER_BOOL", "'" + iSOFT_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "TASK_MUST_REMOVE_CD", "'" + iTASK_MUST_REMOVE_CD + "'" );
      lReqMap.put( "RESCHED_FROM_CD", "'" + iRESCHED_FROM_CD + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'Y'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK, lReqMap ) );

      // C_BLOCK_DYNAMIC_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_INITIAL_QT", null );
      lReqMap.put( "SCHED_INTERVAL_QT", "'" + iSCHED_INTERVAL_QT + "'" );
      lReqMap.put( "SCHED_THRESHOLD_QT", null );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_DYNAMIC_DEADLINE, lReqMap ) );

      // C_BLOCK_DYNAMIC_PART_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_INITIAL_QT", null );
      lReqMap.put( "SCHED_INTERVAL_QT", "'" + iSCHED_INTERVAL_QT + "'" );
      lReqMap.put( "SCHED_THRESHOLD_QT", null );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_DYNAMIC_PART_DEADLINE, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   @Test
   public void testOPER15067BD5IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOPER15067BD5VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs_15067 = null;
      iTASK_DEFN_IDs_15067 = null;

      // Verify task_task table
      iTASK_IDs_15067 = getTaskIds( iBLOCK_TASK_CD_1, iBLOCK_TASK_NAME_1 );
      assmbleInfor lassmblinfor = getassmblInfor( iBLOCK_ATA_CD_TRK );

      iTASK_DEFN_IDs_15067 = verifyTaskTask_15067( iTASK_IDs_15067, iCLASS_CD, lassmblinfor,
            iSUBCLASS_CD, iORIGINATOR_CD, iBLOCK_TASK_CD_1, iBLOCK_TASK_NAME_1, iBLOCK_TASK_DESC_1,
            iBLOCK_TASK_REF_SDESC_1, iLAST_DEADLINE_DRIVER_BOOL_N, iSOFT_DEADLINE_DRIVER_BOOL_N,
            iTASK_MUST_REMOVE_CD, iON_CONDITION_BOOL_N );

      // verify task_DEFN
      String lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTASK_DEFN_IDs_15067.getNO_DB_ID() + " and TASK_DEFN_ID="
            + iTASK_DEFN_IDs_15067.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_task_dep table
      verifyTASKTASKDEP_15067( iTASK_IDs_15067, iTASK_DEP_ACTION_CD, iTASK_DEFN_IDs_15067 );

      // verify task_sched_rule table
      simpleIDs lTypeIds = getTypeIds( iSCHED_DATA_TYPE_CD );
      verifyTASKSCHEDRULE_15067( iTASK_IDs_15067, lTypeIds, iSCHED_INTERVAL_QT, null );

      // // verify task_interval
      simpleIDs lPartIds = getPartIds( iPART_NO_OEM_TRK, iMANUFACT_CD_TRK );
      verifyTASKINTERVAL_15067( iTASK_IDs_15067, lTypeIds, lPartIds, iSCHED_INTERVAL_QT, null );

   }


   public void testOPER15067BD6VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "BLOCK_TASK_NAME", "'" + iBLOCK_TASK_NAME_1 + "'" );
      lReqMap.put( "BLOCK_TASK_DESC", "'" + iBLOCK_TASK_DESC_1 + "'" );
      lReqMap.put( "BLOCK_TASK_REF_SDESC", "'" + iBLOCK_TASK_REF_SDESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "LAST_DEADLINE_DRIVER_BOOL", "'" + iLAST_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_DRIVER_BOOL", "'" + iSOFT_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "TASK_MUST_REMOVE_CD", "'" + iTASK_MUST_REMOVE_CD + "'" );
      lReqMap.put( "RESCHED_FROM_CD", "'" + iRESCHED_FROM_CD + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'T'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK, lReqMap ) );

      // C_BLOCK_DYNAMIC_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_INITIAL_QT", "'" + iSCHED_INITIAL_QT + "'" );
      lReqMap.put( "SCHED_INTERVAL_QT", "'" + iSCHED_INTERVAL_QT + "'" );
      lReqMap.put( "SCHED_THRESHOLD_QT", null );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_DYNAMIC_DEADLINE, lReqMap ) );

      // C_BLOCK_DYNAMIC_PART_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_INITIAL_QT", "'" + iSCHED_INITIAL_QT + "'" );
      lReqMap.put( "SCHED_INTERVAL_QT", "'" + iSCHED_INTERVAL_QT + "'" );
      lReqMap.put( "SCHED_THRESHOLD_QT", null );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_DYNAMIC_PART_DEADLINE, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   @Test
   public void testOPER15067BD6IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOPER15067BD6VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs_15067 = null;
      iTASK_DEFN_IDs_15067 = null;

      // Verify task_task table
      iTASK_IDs_15067 = getTaskIds( iBLOCK_TASK_CD_1, iBLOCK_TASK_NAME_1 );
      assmbleInfor lassmblinfor = getassmblInfor( iBLOCK_ATA_CD_TRK );

      iTASK_DEFN_IDs_15067 = verifyTaskTask_15067( iTASK_IDs_15067, iCLASS_CD, lassmblinfor,
            iSUBCLASS_CD, iORIGINATOR_CD, iBLOCK_TASK_CD_1, iBLOCK_TASK_NAME_1, iBLOCK_TASK_DESC_1,
            iBLOCK_TASK_REF_SDESC_1, iLAST_DEADLINE_DRIVER_BOOL_N, iSOFT_DEADLINE_DRIVER_BOOL_N,
            iTASK_MUST_REMOVE_CD, iON_CONDITION_BOOL_N );

      // verify task_DEFN
      String lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTASK_DEFN_IDs_15067.getNO_DB_ID() + " and TASK_DEFN_ID="
            + iTASK_DEFN_IDs_15067.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_task_dep table
      verifyTASKTASKDEP_15067( iTASK_IDs_15067, iTASK_DEP_ACTION_CD, iTASK_DEFN_IDs_15067 );

      // verify task_sched_rule table
      simpleIDs lTypeIds = getTypeIds( iSCHED_DATA_TYPE_CD );
      verifyTASKSCHEDRULE_15067( iTASK_IDs_15067, lTypeIds, iSCHED_INTERVAL_QT, iSCHED_INITIAL_QT );

      // verify task_interval
      simpleIDs lPartIds = getPartIds( iPART_NO_OEM_TRK, iMANUFACT_CD_TRK );
      verifyTASKINTERVAL_15067( iTASK_IDs_15067, lTypeIds, lPartIds, iSCHED_INTERVAL_QT,
            iSCHED_INITIAL_QT );

   }


   @Test
   public void testOPER15067BD7VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "BLOCK_TASK_NAME", "'" + iBLOCK_TASK_NAME_1 + "'" );
      lReqMap.put( "BLOCK_TASK_DESC", "'" + iBLOCK_TASK_DESC_1 + "'" );
      lReqMap.put( "BLOCK_TASK_REF_SDESC", "'" + iBLOCK_TASK_REF_SDESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "LAST_DEADLINE_DRIVER_BOOL", "'" + iLAST_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_DRIVER_BOOL", "'" + iSOFT_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "TASK_MUST_REMOVE_CD", "'" + iTASK_MUST_REMOVE_CD + "'" );
      lReqMap.put( "RESCHED_FROM_CD", "'" + iRESCHED_FROM_CD + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'Y'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK, lReqMap ) );

      // C_BLOCK_DYNAMIC_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_INITIAL_QT", "'" + iSCHED_INITIAL_QT + "'" );
      lReqMap.put( "SCHED_INTERVAL_QT", null );
      lReqMap.put( "SCHED_THRESHOLD_QT", null );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_DYNAMIC_DEADLINE, lReqMap ) );

      // C_BLOCK_DYNAMIC_PART_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_INITIAL_QT", "'" + iSCHED_INITIAL_QT + "'" );
      lReqMap.put( "SCHED_INTERVAL_QT", null );
      lReqMap.put( "SCHED_THRESHOLD_QT", null );
      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_DYNAMIC_PART_DEADLINE, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // get wrong code
      validateErrorCode( "CFGBLC-12072" );

   }


   /**
    * This test is to verify OPER-15064 fix.REQs that are on-condition and non-recurring should be
    * loaded as non-unique. on-condition and recurring should be unique.
    *
    */

   public void testOPER_15064_1_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "BLOCK_TASK_NAME", "'" + iBLOCK_TASK_NAME_1 + "'" );
      lReqMap.put( "BLOCK_TASK_DESC", "'" + iBLOCK_TASK_DESC_1 + "'" );
      lReqMap.put( "BLOCK_TASK_REF_SDESC", "'" + iBLOCK_TASK_REF_SDESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "LAST_DEADLINE_DRIVER_BOOL", "'" + iLAST_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_DRIVER_BOOL", "'" + iSOFT_DEADLINE_DRIVER_BOOL + "'" );
      // lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "TASK_MUST_REMOVE_CD", "'" + iTASK_MUST_REMOVE_CD + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'Y'" );
      lReqMap.put( "RESCHED_FROM_CD", "'EXECUTE'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'Y'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify OPER-15064 fix.REQs that are on-condition and non-recurring should be
    * loaded as non-unique. on-condition and recurring should be unique.
    *
    */
   @Test
   public void testOPER_15064_1_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOPER_15064_1_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs_15067 = null;
      iTASK_DEFN_IDs_15067 = null;

      // Verify task_task table
      iTASK_IDs_15067 = getTaskIds( iBLOCK_TASK_CD_1, iBLOCK_TASK_NAME_1 );
      assmbleInfor lassmblinfor = getassmblInfor( iBLOCK_ATA_CD_TRK );

      iTASK_DEFN_IDs_15067 = verifyTaskTask_15067( iTASK_IDs_15067, iCLASS_CD, lassmblinfor,
            iSUBCLASS_CD, iORIGINATOR_CD, iBLOCK_TASK_CD_1, iBLOCK_TASK_NAME_1, iBLOCK_TASK_DESC_1,
            iBLOCK_TASK_REF_SDESC_1, iLAST_DEADLINE_DRIVER_BOOL_N, iSOFT_DEADLINE_DRIVER_BOOL_N,
            iTASK_MUST_REMOVE_CD, "1" );

      verifyTaskTask_15067( iTASK_IDs_15067, iCLASS_CD, lassmblinfor, iSUBCLASS_CD, iORIGINATOR_CD,
            iBLOCK_TASK_CD_1, iBLOCK_TASK_NAME_1, iBLOCK_TASK_DESC_1, iBLOCK_TASK_REF_SDESC_1,
            iLAST_DEADLINE_DRIVER_BOOL_N, iSOFT_DEADLINE_DRIVER_BOOL_N, iTASK_MUST_REMOVE_CD, "1",
            "1" );

      // verify task_DEFN
      String lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTASK_DEFN_IDs_15067.getNO_DB_ID() + " and TASK_DEFN_ID="
            + iTASK_DEFN_IDs_15067.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_task_dep table
      verifyTASKTASKDEP_15067( iTASK_IDs_15067, iTASK_DEP_ACTION_CD, iTASK_DEFN_IDs_15067 );

   }


   /**
    * This test is to verify OPER-15064 fix.REQs that are on-condition and non-recurring should be
    * loaded as non-unique. not on-condition and recurring should be unique.
    *
    */

   public void testOPER_15064_2_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "BLOCK_TASK_NAME", "'" + iBLOCK_TASK_NAME_1 + "'" );
      lReqMap.put( "BLOCK_TASK_DESC", "'" + iBLOCK_TASK_DESC_1 + "'" );
      lReqMap.put( "BLOCK_TASK_REF_SDESC", "'" + iBLOCK_TASK_REF_SDESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "LAST_DEADLINE_DRIVER_BOOL", "'" + iLAST_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_DRIVER_BOOL", "'" + iSOFT_DEADLINE_DRIVER_BOOL + "'" );
      // lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "TASK_MUST_REMOVE_CD", "'" + iTASK_MUST_REMOVE_CD + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'N'" );
      lReqMap.put( "RESCHED_FROM_CD", "'EXECUTE'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'Y'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify OPER-15064 fix.REQs that are on-condition and non-recurring should be
    * loaded as non-unique. not on-condition and recurring should be unique.
    *
    */
   @Test
   public void testOPER_15064_2_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOPER_15064_2_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs_15067 = null;
      iTASK_DEFN_IDs_15067 = null;

      // Verify task_task table
      iTASK_IDs_15067 = getTaskIds( iBLOCK_TASK_CD_1, iBLOCK_TASK_NAME_1 );
      assmbleInfor lassmblinfor = getassmblInfor( iBLOCK_ATA_CD_TRK );

      iTASK_DEFN_IDs_15067 = verifyTaskTask_15067( iTASK_IDs_15067, iCLASS_CD, lassmblinfor,
            iSUBCLASS_CD, iORIGINATOR_CD, iBLOCK_TASK_CD_1, iBLOCK_TASK_NAME_1, iBLOCK_TASK_DESC_1,
            iBLOCK_TASK_REF_SDESC_1, iLAST_DEADLINE_DRIVER_BOOL_N, iSOFT_DEADLINE_DRIVER_BOOL_N,
            iTASK_MUST_REMOVE_CD, "0" );

      verifyTaskTask_15067( iTASK_IDs_15067, iCLASS_CD, lassmblinfor, iSUBCLASS_CD, iORIGINATOR_CD,
            iBLOCK_TASK_CD_1, iBLOCK_TASK_NAME_1, iBLOCK_TASK_DESC_1, iBLOCK_TASK_REF_SDESC_1,
            iLAST_DEADLINE_DRIVER_BOOL_N, iSOFT_DEADLINE_DRIVER_BOOL_N, iTASK_MUST_REMOVE_CD, "0",
            "1" );

      // verify task_DEFN
      String lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTASK_DEFN_IDs_15067.getNO_DB_ID() + " and TASK_DEFN_ID="
            + iTASK_DEFN_IDs_15067.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_task_dep table
      verifyTASKTASKDEP_15067( iTASK_IDs_15067, iTASK_DEP_ACTION_CD, iTASK_DEFN_IDs_15067 );

   }


   /**
    * This test is to verify OPER-15064 fix.REQs that are on-condition and non-recurring should be
    * loaded as non-unique. not on-condition and non-recurring should be unique.
    *
    */

   public void testOPER_15064_3_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "BLOCK_TASK_NAME", "'" + iBLOCK_TASK_NAME_1 + "'" );
      lReqMap.put( "BLOCK_TASK_DESC", "'" + iBLOCK_TASK_DESC_1 + "'" );
      lReqMap.put( "BLOCK_TASK_REF_SDESC", "'" + iBLOCK_TASK_REF_SDESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "LAST_DEADLINE_DRIVER_BOOL", "'" + iLAST_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_DRIVER_BOOL", "'" + iSOFT_DEADLINE_DRIVER_BOOL + "'" );
      // lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "TASK_MUST_REMOVE_CD", "'" + iTASK_MUST_REMOVE_CD + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'N'" );
      lReqMap.put( "RESCHED_FROM_CD", null );
      // lReqMap.put( "RESCHED_FROM_CD", "'EXECUTE'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'F'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify OPER-15064 fix.REQs that are on-condition and non-recurring should be
    * loaded as non-unique. not on-condition and non-recurring should be unique.
    *
    */

   @Test
   public void testOPER_15064_3_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOPER_15064_3_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs_15067 = null;
      iTASK_DEFN_IDs_15067 = null;

      // Verify task_task table
      iTASK_IDs_15067 = getTaskIds( iBLOCK_TASK_CD_1, iBLOCK_TASK_NAME_1 );
      assmbleInfor lassmblinfor = getassmblInfor( iBLOCK_ATA_CD_TRK );

      iTASK_DEFN_IDs_15067 = verifyTaskTask_15067( iTASK_IDs_15067, iCLASS_CD, lassmblinfor,
            iSUBCLASS_CD, iORIGINATOR_CD, iBLOCK_TASK_CD_1, iBLOCK_TASK_NAME_1, iBLOCK_TASK_DESC_1,
            iBLOCK_TASK_REF_SDESC_1, iLAST_DEADLINE_DRIVER_BOOL_N, iSOFT_DEADLINE_DRIVER_BOOL_N,
            iTASK_MUST_REMOVE_CD, "0" );

      verifyTaskTask_15067( iTASK_IDs_15067, iCLASS_CD, lassmblinfor, iSUBCLASS_CD, iORIGINATOR_CD,
            iBLOCK_TASK_CD_1, iBLOCK_TASK_NAME_1, iBLOCK_TASK_DESC_1, iBLOCK_TASK_REF_SDESC_1,
            iLAST_DEADLINE_DRIVER_BOOL_N, iSOFT_DEADLINE_DRIVER_BOOL_N, iTASK_MUST_REMOVE_CD, "0",
            "1" );

      // verify task_DEFN
      String lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTASK_DEFN_IDs_15067.getNO_DB_ID() + " and TASK_DEFN_ID="
            + iTASK_DEFN_IDs_15067.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_task_dep table
      verifyTASKTASKDEP_15067NONEXIST( iTASK_IDs_15067, iTASK_DEP_ACTION_CD, iTASK_DEFN_IDs_15067 );

   }


   /**
    * This test is to verify OPER-15064 fix.REQs that are on-condition and non-recurring should be
    * loaded as non-unique. on-condition and non-recurring should be non-unique.
    *
    */

   public void testOPER_15064_4_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "BLOCK_TASK_NAME", "'" + iBLOCK_TASK_NAME_1 + "'" );
      lReqMap.put( "BLOCK_TASK_DESC", "'" + iBLOCK_TASK_DESC_1 + "'" );
      lReqMap.put( "BLOCK_TASK_REF_SDESC", "'" + iBLOCK_TASK_REF_SDESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "LAST_DEADLINE_DRIVER_BOOL", "'" + iLAST_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_DRIVER_BOOL", "'" + iSOFT_DEADLINE_DRIVER_BOOL + "'" );
      // lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "TASK_MUST_REMOVE_CD", "'" + iTASK_MUST_REMOVE_CD + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'Y'" );
      lReqMap.put( "RESCHED_FROM_CD", null );
      // lReqMap.put( "RESCHED_FROM_CD", "'EXECUTE'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'0'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify OPER-15064 fix.REQs that are on-condition and non-recurring should be
    * loaded as non-unique. on-condition and non-recurring should be non-unique.
    *
    */
   @Test
   public void testOPER_15064_4_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOPER_15064_4_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs_15067 = null;
      iTASK_DEFN_IDs_15067 = null;

      // Verify task_task table
      iTASK_IDs_15067 = getTaskIds( iBLOCK_TASK_CD_1, iBLOCK_TASK_NAME_1 );
      assmbleInfor lassmblinfor = getassmblInfor( iBLOCK_ATA_CD_TRK );

      iTASK_DEFN_IDs_15067 = verifyTaskTask_15067( iTASK_IDs_15067, iCLASS_CD, lassmblinfor,
            iSUBCLASS_CD, iORIGINATOR_CD, iBLOCK_TASK_CD_1, iBLOCK_TASK_NAME_1, iBLOCK_TASK_DESC_1,
            iBLOCK_TASK_REF_SDESC_1, iLAST_DEADLINE_DRIVER_BOOL_N, iSOFT_DEADLINE_DRIVER_BOOL_N,
            iTASK_MUST_REMOVE_CD, "1" );

      verifyTaskTask_15067( iTASK_IDs_15067, iCLASS_CD, lassmblinfor, iSUBCLASS_CD, iORIGINATOR_CD,
            iBLOCK_TASK_CD_1, iBLOCK_TASK_NAME_1, iBLOCK_TASK_DESC_1, iBLOCK_TASK_REF_SDESC_1,
            iLAST_DEADLINE_DRIVER_BOOL_N, iSOFT_DEADLINE_DRIVER_BOOL_N, iTASK_MUST_REMOVE_CD, "1",
            "0" );

      // verify task_DEFN
      String lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTASK_DEFN_IDs_15067.getNO_DB_ID() + " and TASK_DEFN_ID="
            + iTASK_DEFN_IDs_15067.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_task_dep table
      verifyTASKTASKDEP_15067NONEXIST( iTASK_IDs_15067, iTASK_DEP_ACTION_CD, iTASK_DEFN_IDs_15067 );

   }


   // =================================================================================
   /**
    * This function is to verify task_task_dep table for 15067.
    *
    *
    */

   public void verifyTASKTASKDEP_15067( simpleIDs aTaskIds, String aTASK_DEP_ACTION_CD,
         simpleIDs aDEPids ) {
      String[] iIds = { "TASK_DEP_ACTION_CD", "DEP_TASK_DEFN_DB_ID", "DEP_TASK_DEFN_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_DB_ID", aTaskIds.getNO_DB_ID() );
      lArgs.addArguments( "TASK_ID", aTaskIds.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_TASK_DEP, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "TASK_DEP_ACTION_CD",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aTASK_DEP_ACTION_CD ) );
      Assert.assertTrue( "DEP_TASK_DEFN_DB_ID",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( aDEPids.getNO_DB_ID() ) );
      Assert.assertTrue( "DEP_TASK_DEFN_ID",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aDEPids.getNO_ID() ) );

   }


   /**
    * This function is to verify task_task_dep table for 15067.
    *
    *
    */

   public void verifyTASKTASKDEP_15067NONEXIST( simpleIDs aTaskIds, String aTASK_DEP_ACTION_CD,
         simpleIDs aDEPids ) {
      String[] iIds = { "TASK_DEP_ACTION_CD", "DEP_TASK_DEFN_DB_ID", "DEP_TASK_DEFN_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_DB_ID", aTaskIds.getNO_DB_ID() );
      lArgs.addArguments( "TASK_ID", aTaskIds.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_TASK_DEP, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "There should be no record created.", llists.size() == 0 );

   }


   /**
    * This function is to retrieve part no ids.
    *
    *
    */

   public simpleIDs getPartIds( String aPART_NO_OEM, String aMANUFACT_CD ) {

      String[] iIds = { "PART_NO_DB_ID", "PART_NO_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "PART_NO_OEM", aPART_NO_OEM );
      lArgs.addArguments( "MANUFACT_CD", aMANUFACT_CD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EQP_PART_NO, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );
      return lIds;

   }


   /**
    * This function is to verify task_sched_rule table for 15067.
    *
    *
    */

   public void verifyTASKINTERVAL_15067( simpleIDs aTaskIds, simpleIDs aDataTypeIds,
         simpleIDs aPartIds, String aDEF_INTERVAL_QT, String aDEF_INITIAL_QT ) {

      String[] iIds = { "DATA_TYPE_DB_ID", "DATA_TYPE_ID", "PART_NO_DB_ID", "PART_NO_ID",
            "INTERVAL_QT", "INITIAL_QT" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_DB_ID", aTaskIds.getNO_DB_ID() );
      lArgs.addArguments( "TASK_ID", aTaskIds.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_INTERVAL, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "DATA_TYPE_DB_ID",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aDataTypeIds.getNO_DB_ID() ) );
      Assert.assertTrue( "DATA_TYPE_ID",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( aDataTypeIds.getNO_ID() ) );
      Assert.assertTrue( "PART_NO_DB_ID",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aPartIds.getNO_DB_ID() ) );
      Assert.assertTrue( "PART_NO_ID",
            llists.get( 0 ).get( 3 ).equalsIgnoreCase( aPartIds.getNO_ID() ) );
      Assert.assertTrue( "INTERVAL_QT",
            llists.get( 0 ).get( 4 ).equalsIgnoreCase( aDEF_INTERVAL_QT ) );
      if ( aDEF_INITIAL_QT != null ) {
         Assert.assertTrue( "INITIAL_QT",
               llists.get( 0 ).get( 5 ).equalsIgnoreCase( aDEF_INITIAL_QT ) );

      }

   }


   /**
    * This function is to verify task_sched_rule table for 15067.
    *
    *
    */

   public void verifyTASKSCHEDRULE_15067( simpleIDs aTaskIds, simpleIDs aDataTypeIds,
         String aDEF_INTERVAL_QT, String aDEF_INITIAL_QT ) {
      String[] iIds = { "DATA_TYPE_DB_ID", "DATA_TYPE_ID", "DEF_INTERVAL_QT", "DEF_INITIAL_QT" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_DB_ID", aTaskIds.getNO_DB_ID() );
      lArgs.addArguments( "TASK_ID", aTaskIds.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_SCHED_RULE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "DATA_TYPE_DB_ID",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aDataTypeIds.getNO_DB_ID() ) );
      Assert.assertTrue( "DATA_TYPE_ID",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( aDataTypeIds.getNO_ID() ) );
      Assert.assertTrue( "DEF_INTERVAL_QT",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aDEF_INTERVAL_QT ) );
      if ( aDEF_INITIAL_QT != null ) {
         Assert.assertTrue( "DEF_INITIAL_QT",
               llists.get( 0 ).get( 3 ).equalsIgnoreCase( aDEF_INITIAL_QT ) );

      }

   }


   /**
    * This function is to retrieve data type IDs from MIM_DATA_TYPE table.
    *
    *
    */

   public simpleIDs getTypeIds( String aDATA_TYPE_CD ) {
      String[] iIds = { "DATA_TYPE_DB_ID", "DATA_TYPE_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "DATA_TYPE_CD", aDATA_TYPE_CD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.MIM_DATA_TYPE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );
      return lIds;

   }


   /**
    * This function is to verify task_task table for 15067.
    *
    *
    */
   public simpleIDs verifyTaskTask_15067( simpleIDs aTaskIds, String aTASK_CLASS_CD,
         assmbleInfor aassmbleInfor, String aTASK_SUBCLASS_CD, String aTASK_ORIGINATOR_CD,
         String aTaskCD, String aTaskName, String aTASK_LDESC, String aTASK_REF_SDESC,
         String aLAST_SCHED_DEAD_BOOL, String aSOFT_DEADLINE_BOOL, String aTASK_MUST_REMOVE_CD,
         String aON_CONDITION_BOOL ) {

      String[] iIds = { "TASK_DEFN_DB_ID", "TASK_DEFN_ID", "TASK_CLASS_CD", "ASSMBL_DB_ID",
            "ASSMBL_CD", "ASSMBL_BOM_ID", "TASK_SUBCLASS_CD", "TASK_ORIGINATOR_CD", "TASK_CD",
            "TASK_NAME", "TASK_LDESC", "TASK_REF_SDESC", "LAST_SCHED_DEAD_BOOL",
            "SOFT_DEADLINE_BOOL", "TASK_MUST_REMOVE_CD", "ON_CONDITION_BOOL" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_DB_ID", aTaskIds.getNO_DB_ID() );
      lArgs.addArguments( "TASK_ID", aTaskIds.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_TASK, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      Assert.assertTrue( "TASK_CLASS_CD",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aTASK_CLASS_CD ) );
      if ( aassmbleInfor != null ) {
         Assert.assertTrue( "ASSMBL_DB_ID",
               llists.get( 0 ).get( 3 ).equalsIgnoreCase( aassmbleInfor.getASSMBL_DB_ID() ) );
         Assert.assertTrue( "ASSMBL_CD",
               llists.get( 0 ).get( 4 ).equalsIgnoreCase( aassmbleInfor.getASSMBL_CD() ) );
         Assert.assertTrue( "ASSMBL_BOM_ID",
               llists.get( 0 ).get( 5 ).equalsIgnoreCase( aassmbleInfor.getASSMBL_BOM_ID() ) );
      }
      Assert.assertTrue( "TASK_SUBCLASS_CD",
            llists.get( 0 ).get( 6 ).equalsIgnoreCase( aTASK_SUBCLASS_CD ) );
      Assert.assertTrue( "TASK_ORIGINATOR_CD",
            llists.get( 0 ).get( 7 ).equalsIgnoreCase( aTASK_ORIGINATOR_CD ) );
      Assert.assertTrue( "TASK_CD", llists.get( 0 ).get( 8 ).equalsIgnoreCase( aTaskCD ) );
      Assert.assertTrue( "TASK_NAME", llists.get( 0 ).get( 9 ).equalsIgnoreCase( aTaskName ) );
      Assert.assertTrue( "TASK_LDESC", llists.get( 0 ).get( 10 ).equalsIgnoreCase( aTASK_LDESC ) );
      Assert.assertTrue( "TASK_REF_SDESC",
            llists.get( 0 ).get( 11 ).equalsIgnoreCase( aTASK_REF_SDESC ) );
      Assert.assertTrue( "LAST_SCHED_DEAD_BOOL",
            llists.get( 0 ).get( 12 ).equalsIgnoreCase( aLAST_SCHED_DEAD_BOOL ) );
      Assert.assertTrue( "SOFT_DEADLINE_BOOL",
            llists.get( 0 ).get( 13 ).equalsIgnoreCase( aSOFT_DEADLINE_BOOL ) );

      if ( aTASK_MUST_REMOVE_CD != null ) {
         Assert.assertTrue( "TASK_MUST_REMOVE_CD",
               llists.get( 0 ).get( 14 ).equalsIgnoreCase( aTASK_MUST_REMOVE_CD ) );

      }

      if ( aON_CONDITION_BOOL != null ) {
         Assert.assertTrue( "ON_CONDITION_BOOL",
               llists.get( 0 ).get( 15 ).equalsIgnoreCase( aON_CONDITION_BOOL ) );

      }

      return lIds;
   }


   /**
    * This function is to verify task_task table for 15067.
    *
    *
    */
   public simpleIDs verifyTaskTask_15067( simpleIDs aTaskIds, String aTASK_CLASS_CD,
         assmbleInfor aassmbleInfor, String aTASK_SUBCLASS_CD, String aTASK_ORIGINATOR_CD,
         String aTaskCD, String aTaskName, String aTASK_LDESC, String aTASK_REF_SDESC,
         String aLAST_SCHED_DEAD_BOOL, String aSOFT_DEADLINE_BOOL, String aTASK_MUST_REMOVE_CD,
         String aON_CONDITION_BOOL, String aUNIQUE_BOOL ) {

      String[] iIds = { "TASK_DEFN_DB_ID", "TASK_DEFN_ID", "TASK_CLASS_CD", "ASSMBL_DB_ID",
            "ASSMBL_CD", "ASSMBL_BOM_ID", "TASK_SUBCLASS_CD", "TASK_ORIGINATOR_CD", "TASK_CD",
            "TASK_NAME", "TASK_LDESC", "TASK_REF_SDESC", "LAST_SCHED_DEAD_BOOL",
            "SOFT_DEADLINE_BOOL", "TASK_MUST_REMOVE_CD", "ON_CONDITION_BOOL", "UNIQUE_BOOL" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_DB_ID", aTaskIds.getNO_DB_ID() );
      lArgs.addArguments( "TASK_ID", aTaskIds.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_TASK, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      Assert.assertTrue( "TASK_CLASS_CD",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aTASK_CLASS_CD ) );
      if ( aassmbleInfor != null ) {
         Assert.assertTrue( "ASSMBL_DB_ID",
               llists.get( 0 ).get( 3 ).equalsIgnoreCase( aassmbleInfor.getASSMBL_DB_ID() ) );
         Assert.assertTrue( "ASSMBL_CD",
               llists.get( 0 ).get( 4 ).equalsIgnoreCase( aassmbleInfor.getASSMBL_CD() ) );
         Assert.assertTrue( "ASSMBL_BOM_ID",
               llists.get( 0 ).get( 5 ).equalsIgnoreCase( aassmbleInfor.getASSMBL_BOM_ID() ) );
      }
      Assert.assertTrue( "TASK_SUBCLASS_CD",
            llists.get( 0 ).get( 6 ).equalsIgnoreCase( aTASK_SUBCLASS_CD ) );
      Assert.assertTrue( "TASK_ORIGINATOR_CD",
            llists.get( 0 ).get( 7 ).equalsIgnoreCase( aTASK_ORIGINATOR_CD ) );
      Assert.assertTrue( "TASK_CD", llists.get( 0 ).get( 8 ).equalsIgnoreCase( aTaskCD ) );
      Assert.assertTrue( "TASK_NAME", llists.get( 0 ).get( 9 ).equalsIgnoreCase( aTaskName ) );
      Assert.assertTrue( "TASK_LDESC", llists.get( 0 ).get( 10 ).equalsIgnoreCase( aTASK_LDESC ) );
      Assert.assertTrue( "TASK_REF_SDESC",
            llists.get( 0 ).get( 11 ).equalsIgnoreCase( aTASK_REF_SDESC ) );
      Assert.assertTrue( "LAST_SCHED_DEAD_BOOL",
            llists.get( 0 ).get( 12 ).equalsIgnoreCase( aLAST_SCHED_DEAD_BOOL ) );
      Assert.assertTrue( "SOFT_DEADLINE_BOOL",
            llists.get( 0 ).get( 13 ).equalsIgnoreCase( aSOFT_DEADLINE_BOOL ) );

      if ( aTASK_MUST_REMOVE_CD != null ) {
         Assert.assertTrue( "TASK_MUST_REMOVE_CD",
               llists.get( 0 ).get( 14 ).equalsIgnoreCase( aTASK_MUST_REMOVE_CD ) );

      }

      if ( aON_CONDITION_BOOL != null ) {
         Assert.assertTrue( "ON_CONDITION_BOOL",
               llists.get( 0 ).get( 15 ).equalsIgnoreCase( aON_CONDITION_BOOL ) );

      }

      Assert.assertTrue( "UNIQUE_BOOL ",
            llists.get( 0 ).get( 16 ).equalsIgnoreCase( aUNIQUE_BOOL ) );

      return lIds;
   }


   /**
    * This function is to retrieve assemble information from EQP_ASSMBL_BOM table.
    *
    *
    */

   public assmbleInfor getassmblInfor( String aASSMBL_BOM_CD ) {

      String[] iIds = { "ASSMBL_DB_ID", "ASSMBL_CD", "ASSMBL_BOM_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "ASSMBL_BOM_CD", aASSMBL_BOM_CD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EQP_ASSMBL_BOM, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      assmbleInfor lIds = new assmbleInfor( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ),
            llists.get( 0 ).get( 2 ), null );

      return lIds;

   }


   /**
    * This function is to retrieve task IDs from task_task table.
    *
    *
    */
   public simpleIDs getTaskIds( String aTASK_CD, String aTASK_NAME ) {

      String[] iIds = { "TASK_DB_ID", "TASK_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_CD", aTASK_CD );
      lArgs.addArguments( "TASK_NAME", aTASK_NAME );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_TASK, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );
      return lIds;
   }


   /**
    * This function is called by after test method to restore data which has been modified by tests.
    *
    *
    */

   public void RestoreData() {

      String lStrDelete = null;

      if ( iTASK_IDs_15067 != null ) {

         // delete task_sched_rule
         lStrDelete = "delete from " + TableUtil.TASK_SCHED_RULE + "  where TASK_DB_ID="
               + iTASK_IDs_15067.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_15067.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_task_dep
         lStrDelete = "delete from " + TableUtil.TASK_TASK_DEP + "  where TASK_DB_ID="
               + iTASK_IDs_15067.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_15067.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_interval
         lStrDelete = "delete from " + TableUtil.TASK_INTERVAL + "  where TASK_DB_ID="
               + iTASK_IDs_15067.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_15067.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_task
         lStrDelete = "delete from " + TableUtil.TASK_TASK + "  where TASK_DB_ID="
               + iTASK_IDs_15067.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_15067.getNO_ID();
         executeSQL( lStrDelete );

      }

      if ( iTASK_DEFN_IDs_15067 != null ) {
         lStrDelete =
               "delete from TASK_DEFN where TASK_DEFN_DB_ID=" + iTASK_DEFN_IDs_15067.getNO_DB_ID()
                     + " and TASK_DEFN_ID=" + iTASK_DEFN_IDs_15067.getNO_ID();
         executeSQL( lStrDelete );

      }

   }


   /**
    * This function is to validate error code exists.
    *
    *
    */
   public void validateErrorCode( String aCode ) {

      List<String> llist = getErrorCodeList();
      Assert.assertTrue( "Error code exists.", llist.contains( aCode ) );

   }


   /**
    * This function is to retrieve errors code list
    *
    *
    */

   public List<String> getErrorCodeList() {
      String[] iIds = { "RESULT_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      String iQueryString = "select c_block.result_cd " + " from c_block "
            + " inner join DL_REF_MESSAGE on " + " DL_REF_MESSAGE.result_cd=c_block.result_cd "
            + " UNION ALL " + " select c_block_dynamic_deadline.result_cd "
            + " from c_block_dynamic_deadline inner join DL_REF_MESSAGE on "
            + " DL_REF_MESSAGE.result_cd=c_block_dynamic_deadline.result_cd " + " UNION ALL "
            + " select c_proc_block_deadline.result_cd "
            + " from c_proc_block_deadline inner join DL_REF_MESSAGE on "
            + " DL_REF_MESSAGE.result_cd=c_proc_block_deadline.result_cd " + " UNION ALL "
            + " select c_block_dynamic_part_deadline.result_cd "
            + " from c_block_dynamic_part_deadline inner join DL_REF_MESSAGE on "
            + " DL_REF_MESSAGE.result_cd=c_block_dynamic_part_deadline.result_cd";
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      List<String> llistM = new ArrayList<String>();
      for ( int i = 0; i < llists.size(); i++ ) {
         llistM.add( llists.get( i ).get( 0 ) );
      }

      return llistM;

   }


   /**
    * This function is to implement interface ValidationAndImport
    *
    * @param: blnOnlyValidation
    * @param: allornone
    *
    * @return: return code of Int
    *
    */
   public int runValidationAndImport( boolean ablnOnlyValidation, boolean allornone ) {
      int lrtValue = 0;
      ivalidationandimport = new ValidationAndImport() {

         @Override
         public int runValidation( boolean allornone ) {
            int lReturn = 0;
            CallableStatement lPrepareCallJICPART;

            try {

               lPrepareCallJICPART = getConnection()
                     .prepareCall( "BEGIN   block_import.validate_block(on_retcode =>?); END;" );

               lPrepareCallJICPART.registerOutParameter( 1, Types.INTEGER );
               lPrepareCallJICPART.execute();
               commit();
               lReturn = lPrepareCallJICPART.getInt( 1 );
            } catch ( SQLException e ) {
               e.printStackTrace();
            }
            return lReturn;

         }


         @Override
         public int runImport( boolean allornone ) {
            int lReturn = 0;
            CallableStatement lPrepareCallKIT;

            try {

               lPrepareCallKIT = getConnection()
                     .prepareCall( "BEGIN block_import.import_block(on_retcode =>?); END;" );

               lPrepareCallKIT.registerOutParameter( 1, Types.INTEGER );

               lPrepareCallKIT.execute();
               commit();
               lReturn = lPrepareCallKIT.getInt( 1 );
            } catch ( SQLException e ) {
               e.printStackTrace();
            }
            return lReturn;
         }

      };

      lrtValue = ablnOnlyValidation ? ivalidationandimport.runValidation( allornone )
            : ivalidationandimport.runImport( allornone );

      return lrtValue;
   }

}
