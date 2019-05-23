package com.mxi.mx.core.maint.plan.baselineloader;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;
import com.mxi.mx.util.ReqAndCompReqTest;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.ValidationAndImport;


/**
 * This test suite contains test cases on validation and import functionality on C_COMP_REQ_IMPORT.
 *
 * @author ALICIA QIAN
 */
public class CompRequirements extends ReqAndCompReqTest {

   @Rule
   public TestName testName = new TestName();
   ValidationAndImport ivalidationandimport;


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
    * This test is to verify JIRA-15067 on comp_req_validate_comp_req functionality
    * Requirements-dynamic deadline RD. 1. In c_COMP_req table, create a requirement with
    * RESCHED_FROM_CD as null, and in C_COMP_REQ_DYNAMIC_DEADLINE and C_COMP_REQ_DYN_PART_DEADLINE
    * table set SCHED_SCHED_INITIAL_QT and SCHED_INTERVAL_QT are null. It should pass validation.
    *
    */

   public void testOPER15067RDP1VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_COMP_REQ
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_1 + "'" );
      lReqMap.put( "REQ_TASK_DESC", "'" + iREQ_TASK_DESC_1 + "'" );
      lReqMap.put( "EXT_REFERENCE", "'" + iREQ_TASK_REF_SDESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD_COMP + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD_COMP + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "ETOPS_SIGNIFICANT_BOOL", "'" + iETOPS_SIGNIFICANT_BOOL + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "APPLICABILITY_RANGE", "'" + iAPPLICABILITY_RANGE + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MIN_USAGE_RELEASE_PCT", "'" + iMIN_USAGE_RELEASE_PCT + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE_QT", "'" + iMIN_FORECAST_RANGE_QT + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE_QT", "'" + iMIN_FORECAST_RANGE_QT + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL + "'" );
      lReqMap.put( "EST_DURATION_HRS", "'" + iEST_DURATION_HRS + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "REQ_INSTRUCTIONS", "'" + iREQ_INSTRUCTIONS + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'F'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ, lReqMap ) );

      // C_COMP_REQ_ASSIGNED_PART
      // first record
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_ASSIGNED_PART, lReqMap ) );
      // second record
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_SER + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_SER + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_ASSIGNED_PART, lReqMap ) );

      // C_COMP_REQ_DYNAMIC_DEADLINE
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_THRESHOLD_QT", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_DYNAMIC_DEADLINE, lReqMap ) );

      // C_COMP_REQ_DYN_PART_DEADLINE
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_THRESHOLD_QT", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_DYN_PART_DEADLINE, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify JIRA-15067 on c_comp_req_import.import_comp_req functionality
    * Requirements-dynamic deadline RD. 1. In c_COMP_req table, create a requirement with
    * RESCHED_FROM_CD as null, and in C_COMP_REQ_DYNAMIC_DEADLINE and C_COMP_REQ_DYN_PART_DEADLINE
    * table set SCHED_SCHED_INITIAL_QT and SCHED_INTERVAL_QT are null. It should pass validation.
    *
    */

   @Test
   public void testOPER15067RDP1IMPORT() {

      System.out.println( "=======Starting: " + testName.getMethodName()
            + " Validation========================" );

      testOPER15067RDP1VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs_15067 = null;
      iTASK_DEFN_IDs_15067 = null;

      // Verify task_task table
      iTASK_IDs_15067 = getTaskIds( iREQ_TASK_CD_1, iREQ_TASK_NAME_1 );

      iTASK_DEFN_IDs_15067 = verifyTaskTask_15067( iTASK_IDs_15067, iCLASS_CD_COMP, null,
            iSUBCLASS_CD_COMP, iORIGINATOR_CD, iREQ_TASK_CD_1, iREQ_TASK_NAME_1, iREQ_TASK_DESC_1,
            iREQ_INSTRUCTIONS, null, iAPPLICABILITY_RANGE, iSCHED_TO_LATEST_DEADLINE_BOOL_N,
            iSOFT_DEADLINE_BOOL_N, null, iMUST_BE_REMOVED_CD, iETOPS_SIGNIFICANT_BOOL_N,
            iON_CONDITION_BOOL_N, iEST_DURATION_HRS );

      // verify task_DEFN
      String lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTASK_DEFN_IDs_15067.getNO_DB_ID() + " and TASK_DEFN_ID="
            + iTASK_DEFN_IDs_15067.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_sched_rule table
      simpleIDs lTypeIds = getTypeIds( iSCHED_DATA_TYPE_CD );
      verifyTASKSCHEDRULE_15067( iTASK_IDs_15067, lTypeIds, iSCHED_THRESHOLD_QT, null );

      // verify task_interval
      simpleIDs lPartIds = getPartIds( iPART_NO_OEM_TRK, iMANUFACT_CD_TRK );
      verifyTASKINTERVAL_15067( iTASK_IDs_15067, lTypeIds, lPartIds, iSCHED_THRESHOLD_QT, null );

      // verify task_task_flags
      lQuery = "select 1 from " + TableUtil.TASK_TASK_FLAGS + " where TASK_DB_ID="
            + iTASK_IDs_15067.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_15067.getNO_ID();
      Assert.assertTrue( "Check task_task_flags table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_part_map table
      simpleIDs lPNIDs_TRK = getPartNoIds( iPART_NO_OEM_TRK, iMANUFACT_CD_TRK );
      simpleIDs lPNIDs_SER = getPartNoIds( iPART_NO_OEM_SER, iMANUFACT_CD_SER );

      lQuery = "select 1 from " + TableUtil.TASK_PART_MAP + " where TASK_DB_ID="
            + iTASK_IDs_15067.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_15067.getNO_ID()
            + " and PART_NO_DB_ID=" + lPNIDs_TRK.getNO_DB_ID() + " and PART_NO_ID="
            + lPNIDs_TRK.getNO_ID();
      Assert.assertTrue( "Check task_part_map  table to verify the TRK record exists",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.TASK_PART_MAP + " where TASK_DB_ID="
            + iTASK_IDs_15067.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_15067.getNO_ID()
            + " and PART_NO_DB_ID=" + lPNIDs_SER.getNO_DB_ID() + " and PART_NO_ID="
            + lPNIDs_SER.getNO_ID();
      Assert.assertTrue( "Check task_part_map  table to verify the SER record exists",
            RecordsExist( lQuery ) );

   }


   /**
    * This test is to verify JIRA-15067 on comp_req_validate_comp_req functionality
    * Requirements-dynamic deadline RD. 2. In c_COMP_req table, create a requirement with
    * RESCHED_FROM_CD as null, and in C_COMP_REQ_DYNAMIC_DEADLINE and C_COMP_REQ_DYN_PART_DEADLINE
    * table set set either SCHED_INITIAL_QT or SCHED_INTERVAL_QT is not null. It should not pass
    * validation with error code "CFG_COMP_REQ-10021" and "CFG_COMP_REQ-10301".
    *
    */

   @Test
   public void testOPER15067RDP2VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_COMP_REQ
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_1 + "'" );
      lReqMap.put( "REQ_TASK_DESC", "'" + iREQ_TASK_DESC_1 + "'" );
      lReqMap.put( "EXT_REFERENCE", "'" + iREQ_TASK_REF_SDESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD_COMP + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD_COMP + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "ETOPS_SIGNIFICANT_BOOL", "'" + iETOPS_SIGNIFICANT_BOOL + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "APPLICABILITY_RANGE", "'" + iAPPLICABILITY_RANGE + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MIN_USAGE_RELEASE_PCT", "'" + iMIN_USAGE_RELEASE_PCT + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE_QT", "'" + iMIN_FORECAST_RANGE_QT + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE_QT", "'" + iMIN_FORECAST_RANGE_QT + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL + "'" );
      lReqMap.put( "EST_DURATION_HRS", "'" + iEST_DURATION_HRS + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "REQ_INSTRUCTIONS", "'" + iREQ_INSTRUCTIONS + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'0'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ, lReqMap ) );

      // C_COMP_REQ_ASSIGNED_PART
      // first record
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_ASSIGNED_PART, lReqMap ) );
      // second record
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_SER + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_SER + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_ASSIGNED_PART, lReqMap ) );

      // C_COMP_REQ_DYNAMIC_DEADLINE
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_INITIAL_QT", "'" + iSCHED_INITIAL_QT + "'" );
      lReqMap.put( "SCHED_INTERVAL_QT", null );
      lReqMap.put( "SCHED_THRESHOLD_QT", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_DYNAMIC_DEADLINE, lReqMap ) );

      // C_COMP_REQ_DYN_PART_DEADLINE
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_INITIAL_QT", "'" + iSCHED_INITIAL_QT + "'" );
      lReqMap.put( "SCHED_INTERVAL_QT", null );
      lReqMap.put( "SCHED_THRESHOLD_QT", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_DYN_PART_DEADLINE, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );
      validateErrorCode( "CFG_COMP_REQ-10021" );
      validateErrorCode( "CFG_COMP_REQ-10301" );

   }


   /**
    * This test is to verify JIRA-15067 on c_comp_req_import.validate_comp_req functionality
    * Requirements-dynamic deadline RD. 3. In c_COMP_req table, create a requirement with
    * RESCHED_FROM_CD not null, and no entry in C_COMP_REQ_DYNAMIC_DEADLINE and
    * C_COMP_REQ_DYN_PART_DEADLINE table. It should pass validation
    *
    */

   @Test
   public void testOPER15067RDP3VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_COMP_REQ
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_1 + "'" );
      lReqMap.put( "REQ_TASK_DESC", "'" + iREQ_TASK_DESC_1 + "'" );
      lReqMap.put( "EXT_REFERENCE", "'" + iREQ_TASK_REF_SDESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD_COMP + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD_COMP + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "ETOPS_SIGNIFICANT_BOOL", "'" + iETOPS_SIGNIFICANT_BOOL + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "APPLICABILITY_RANGE", "'" + iAPPLICABILITY_RANGE + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MIN_USAGE_RELEASE_PCT", "'" + iMIN_USAGE_RELEASE_PCT + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE_QT", "'" + iMIN_FORECAST_RANGE_QT + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE_QT", "'" + iMIN_FORECAST_RANGE_QT + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL + "'" );
      lReqMap.put( "EST_DURATION_HRS", "'" + iEST_DURATION_HRS + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "RESCHED_FROM_CD", "'" + iRESCHED_FROM_CD + "'" );
      lReqMap.put( "REQ_INSTRUCTIONS", "'" + iREQ_INSTRUCTIONS + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'T'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ, lReqMap ) );

      // C_COMP_REQ_ASSIGNED_PART
      // first record
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_ASSIGNED_PART, lReqMap ) );
      // second record
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_SER + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_SER + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_ASSIGNED_PART, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify JIRA-15067 on c_comp_req_import.import_comp_req functionality
    * Requirements-dynamic deadline RD. 3. In c_COMP_req table, create a requirement with
    * RESCHED_FROM_CD not null, and no entry in C_COMP_REQ_DYNAMIC_DEADLINE and
    * C_COMP_REQ_DYN_PART_DEADLINE table. It should pass validation
    *
    */

   @Test
   public void testOPER15067RDP3IMPORT() {

      System.out.println( "=======Starting: " + testName.getMethodName()
            + " Validation========================" );

      testOPER15067RDP3VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs_15067 = null;
      iTASK_DEFN_IDs_15067 = null;

      // Verify task_task table
      iTASK_IDs_15067 = getTaskIds( iREQ_TASK_CD_1, iREQ_TASK_NAME_1 );

      iTASK_DEFN_IDs_15067 = verifyTaskTask_15067( iTASK_IDs_15067, iCLASS_CD_COMP, null,
            iSUBCLASS_CD_COMP, iORIGINATOR_CD, iREQ_TASK_CD_1, iREQ_TASK_NAME_1, iREQ_TASK_DESC_1,
            iREQ_INSTRUCTIONS, null, iAPPLICABILITY_RANGE, iSCHED_TO_LATEST_DEADLINE_BOOL_N,
            iSOFT_DEADLINE_BOOL_N, null, iMUST_BE_REMOVED_CD, iETOPS_SIGNIFICANT_BOOL_N,
            iON_CONDITION_BOOL_N, iEST_DURATION_HRS );

      // verify task_DEFN
      String lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTASK_DEFN_IDs_15067.getNO_DB_ID() + " and TASK_DEFN_ID="
            + iTASK_DEFN_IDs_15067.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_task_dep table
      // this is bug
      verifyTASKTASKDEP_15067( iTASK_IDs_15067, iTASK_DEP_ACTION_CD, iTASK_DEFN_IDs_15067 );

      // verify task_task_flags
      lQuery = "select 1 from " + TableUtil.TASK_TASK_FLAGS + " where TASK_DB_ID="
            + iTASK_IDs_15067.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_15067.getNO_ID();
      Assert.assertTrue( "Check task_task_flags table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_part_map table
      simpleIDs lPNIDs_TRK = getPartNoIds( iPART_NO_OEM_TRK, iMANUFACT_CD_TRK );
      simpleIDs lPNIDs_SER = getPartNoIds( iPART_NO_OEM_SER, iMANUFACT_CD_SER );

      lQuery = "select 1 from " + TableUtil.TASK_PART_MAP + " where TASK_DB_ID="
            + iTASK_IDs_15067.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_15067.getNO_ID()
            + " and PART_NO_DB_ID=" + lPNIDs_TRK.getNO_DB_ID() + " and PART_NO_ID="
            + lPNIDs_TRK.getNO_ID();
      Assert.assertTrue( "Check task_part_map  table to verify the TRK record exists",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.TASK_PART_MAP + " where TASK_DB_ID="
            + iTASK_IDs_15067.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_15067.getNO_ID()
            + " and PART_NO_DB_ID=" + lPNIDs_SER.getNO_DB_ID() + " and PART_NO_ID="
            + lPNIDs_SER.getNO_ID();
      Assert.assertTrue( "Check task_part_map  table to verify the SER record exists",
            RecordsExist( lQuery ) );

   }


   /**
    * This test is to verify JIRA-15067 on comp_req_validate_comp_req functionality
    * Requirements-dynamic deadline RD. 4. In c_COMP_req table, create a requirement with
    * RESCHED_FROM_CD is not null, and in C_COMP_REQ_DYNAMIC_DEADLINE and
    * C_COMP_REQ_DYN_PART_DEADLINE table set both SCHED_INITIAL_QT or SCHED_INTERVAL_QT are null. It
    * should not pass validation with error code "CFG_COMP_REQ-10502" and "CFG_COMP_REQ-12410".
    */

   @Test
   public void testOPER15067RDP4VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_COMP_REQ
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_1 + "'" );
      lReqMap.put( "REQ_TASK_DESC", "'" + iREQ_TASK_DESC_1 + "'" );
      lReqMap.put( "EXT_REFERENCE", "'" + iREQ_TASK_REF_SDESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD_COMP + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD_COMP + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "ETOPS_SIGNIFICANT_BOOL", "'" + iETOPS_SIGNIFICANT_BOOL + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "APPLICABILITY_RANGE", "'" + iAPPLICABILITY_RANGE + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MIN_USAGE_RELEASE_PCT", "'" + iMIN_USAGE_RELEASE_PCT + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE_QT", "'" + iMIN_FORECAST_RANGE_QT + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE_QT", "'" + iMIN_FORECAST_RANGE_QT + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL + "'" );
      lReqMap.put( "EST_DURATION_HRS", "'" + iEST_DURATION_HRS + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "RESCHED_FROM_CD", "'" + iRESCHED_FROM_CD + "'" );
      lReqMap.put( "REQ_INSTRUCTIONS", "'" + iREQ_INSTRUCTIONS + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ, lReqMap ) );

      // C_COMP_REQ_ASSIGNED_PART
      // first record
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_ASSIGNED_PART, lReqMap ) );
      // second record
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_SER + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_SER + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_ASSIGNED_PART, lReqMap ) );

      // C_COMP_REQ_DYNAMIC_DEADLINE
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_INITIAL_QT", null );
      lReqMap.put( "SCHED_INTERVAL_QT", null );
      lReqMap.put( "SCHED_THRESHOLD_QT", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_DYNAMIC_DEADLINE, lReqMap ) );

      // C_COMP_REQ_DYN_PART_DEADLINE
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_INITIAL_QT", null );
      lReqMap.put( "SCHED_INTERVAL_QT", null );
      lReqMap.put( "SCHED_THRESHOLD_QT", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_DYN_PART_DEADLINE, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );
      validateErrorCode( "CFG_COMP_REQ-10502" );
      validateErrorCode( "CFG_COMP_REQ-12410" );

   }


   // check
   @Test
   public void testOPER15067RDP4_2VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_COMP_REQ
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_1 + "'" );
      lReqMap.put( "REQ_TASK_DESC", "'" + iREQ_TASK_DESC_1 + "'" );
      lReqMap.put( "EXT_REFERENCE", "'" + iREQ_TASK_REF_SDESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD_COMP + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD_COMP + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "ETOPS_SIGNIFICANT_BOOL", "'" + iETOPS_SIGNIFICANT_BOOL + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "APPLICABILITY_RANGE", "'" + iAPPLICABILITY_RANGE + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MIN_USAGE_RELEASE_PCT", "'" + iMIN_USAGE_RELEASE_PCT + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE_QT", "'" + iMIN_FORECAST_RANGE_QT + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE_QT", "'" + iMIN_FORECAST_RANGE_QT + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL + "'" );
      lReqMap.put( "EST_DURATION_HRS", "'" + iEST_DURATION_HRS + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      // lReqMap.put( "RESCHED_FROM_CD", "'" + iRESCHED_FROM_CD + "'" );
      lReqMap.put( "REQ_INSTRUCTIONS", "'" + iREQ_INSTRUCTIONS + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ, lReqMap ) );

      // C_COMP_REQ_ASSIGNED_PART
      // first record
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_ASSIGNED_PART, lReqMap ) );
      // second record
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_SER + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_SER + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_ASSIGNED_PART, lReqMap ) );

      // C_COMP_REQ_DYNAMIC_DEADLINE
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_INITIAL_QT", null );
      lReqMap.put( "SCHED_INTERVAL_QT", "'" + iSCHED_INTERVAL_QT + "'" );
      // lReqMap.put( "SCHED_THRESHOLD_QT", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_DYNAMIC_DEADLINE, lReqMap ) );

      // C_COMP_REQ_DYN_PART_DEADLINE
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_INITIAL_QT", null );
      lReqMap.put( "SCHED_INTERVAL_QT", "'" + iSCHED_INTERVAL_QT + "'" );
      // lReqMap.put( "SCHED_THRESHOLD_QT", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_DYN_PART_DEADLINE, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );
      validateErrorCode( "CFG_COMP_REQ-10503" );
      validateErrorCode( "CFG_COMP_REQ-12411" );

   }


   /**
    * This test is to verify JIRA-15067 on comp_req_validate_comp_req functionality
    * Requirements-dynamic deadline RD. 5. In c_COMP_req table, create a requirement with
    * RESCHED_FROM_CD is not null, and in C_COMP_REQ_DYNAMIC_DEADLINE and
    * C_COMP_REQ_DYN_PART_DEADLINE table set SCHED_THRESHOLD_QT as null , SCHED_INITIAL_QT is null
    * and SCHED_INTERVAL_QT is not null. It should pass validation.
    *
    */

   @Test
   public void testOPER15067RDP5VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_COMP_REQ
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_1 + "'" );
      lReqMap.put( "REQ_TASK_DESC", "'" + iREQ_TASK_DESC_1 + "'" );
      lReqMap.put( "EXT_REFERENCE", "'" + iREQ_TASK_REF_SDESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD_COMP + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD_COMP + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "ETOPS_SIGNIFICANT_BOOL", "'" + iETOPS_SIGNIFICANT_BOOL + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "APPLICABILITY_RANGE", "'" + iAPPLICABILITY_RANGE + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MIN_USAGE_RELEASE_PCT", "'" + iMIN_USAGE_RELEASE_PCT + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE_QT", "'" + iMIN_FORECAST_RANGE_QT + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE_QT", "'" + iMIN_FORECAST_RANGE_QT + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL + "'" );
      lReqMap.put( "EST_DURATION_HRS", "'" + iEST_DURATION_HRS + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "RESCHED_FROM_CD", "'" + iRESCHED_FROM_CD + "'" );
      lReqMap.put( "REQ_INSTRUCTIONS", "'" + iREQ_INSTRUCTIONS + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'Y'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ, lReqMap ) );

      // C_COMP_REQ_ASSIGNED_PART
      // first record
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_ASSIGNED_PART, lReqMap ) );
      // second record
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_SER + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_SER + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_ASSIGNED_PART, lReqMap ) );

      // C_COMP_REQ_DYNAMIC_DEADLINE
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_INITIAL_QT", null );
      lReqMap.put( "SCHED_INTERVAL_QT", "'" + iSCHED_INTERVAL_QT + "'" );
      lReqMap.put( "SCHED_THRESHOLD_QT", null );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_DYNAMIC_DEADLINE, lReqMap ) );

      // C_COMP_REQ_DYN_PART_DEADLINE
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_INITIAL_QT", null );
      lReqMap.put( "SCHED_INTERVAL_QT", "'" + iSCHED_INTERVAL_QT + "'" );
      lReqMap.put( "SCHED_THRESHOLD_QT", null );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_DYN_PART_DEADLINE, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify JIRA-15067 on c_comp_req_import.import_comp_req functionality
    * Requirements-dynamic deadline RD. 5. In c_COMP_req table, create a requirement with
    * RESCHED_FROM_CD is not null, and in C_COMP_REQ_DYNAMIC_DEADLINE and
    * C_COMP_REQ_DYN_PART_DEADLINE table set SCHED_THRESHOLD_QT as null , SCHED_INITIAL_QT is null
    * and SCHED_INTERVAL_QT is not null. It should pass validation.
    *
    */

   @Test
   public void testOPER15067RDP5IMPORT() {

      System.out.println( "=======Starting: " + testName.getMethodName()
            + " Validation========================" );

      testOPER15067RDP5VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs_15067 = null;
      iTASK_DEFN_IDs_15067 = null;

      // Verify task_task table
      iTASK_IDs_15067 = getTaskIds( iREQ_TASK_CD_1, iREQ_TASK_NAME_1 );

      iTASK_DEFN_IDs_15067 = verifyTaskTask_15067( iTASK_IDs_15067, iCLASS_CD_COMP, null,
            iSUBCLASS_CD_COMP, iORIGINATOR_CD, iREQ_TASK_CD_1, iREQ_TASK_NAME_1, iREQ_TASK_DESC_1,
            iREQ_INSTRUCTIONS, null, iAPPLICABILITY_RANGE, iSCHED_TO_LATEST_DEADLINE_BOOL_N,
            iSOFT_DEADLINE_BOOL_N, null, iMUST_BE_REMOVED_CD, iETOPS_SIGNIFICANT_BOOL_N,
            iON_CONDITION_BOOL_N, iEST_DURATION_HRS );

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

      // verify task_interval
      simpleIDs lPartIds = getPartIds( iPART_NO_OEM_TRK, iMANUFACT_CD_TRK );
      verifyTASKINTERVAL_15067( iTASK_IDs_15067, lTypeIds, lPartIds, iSCHED_INTERVAL_QT, null );

      // verify task_task_flags
      lQuery = "select 1 from " + TableUtil.TASK_TASK_FLAGS + " where TASK_DB_ID="
            + iTASK_IDs_15067.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_15067.getNO_ID();
      Assert.assertTrue( "Check task_task_flags table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_part_map table
      simpleIDs lPNIDs_TRK = getPartNoIds( iPART_NO_OEM_TRK, iMANUFACT_CD_TRK );
      simpleIDs lPNIDs_SER = getPartNoIds( iPART_NO_OEM_SER, iMANUFACT_CD_SER );

      lQuery = "select 1 from " + TableUtil.TASK_PART_MAP + " where TASK_DB_ID="
            + iTASK_IDs_15067.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_15067.getNO_ID()
            + " and PART_NO_DB_ID=" + lPNIDs_TRK.getNO_DB_ID() + " and PART_NO_ID="
            + lPNIDs_TRK.getNO_ID();
      Assert.assertTrue( "Check task_part_map  table to verify the TRK record exists",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.TASK_PART_MAP + " where TASK_DB_ID="
            + iTASK_IDs_15067.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_15067.getNO_ID()
            + " and PART_NO_DB_ID=" + lPNIDs_SER.getNO_DB_ID() + " and PART_NO_ID="
            + lPNIDs_SER.getNO_ID();
      Assert.assertTrue( "Check task_part_map  table to verify the SER record exists",
            RecordsExist( lQuery ) );

   }


   /**
    * This test is to verify JIRA-15067 on comp_req_validate_comp_req functionality
    * Requirements-dynamic deadline RD. 6. In c_COMP_req table, create a requirement with
    * RESCHED_FROM_CD is not null, and in C_COMP_REQ_DYNAMIC_DEADLINE and
    * C_COMP_REQ_DYN_PART_DEADLINE table set SCHED_THRESHOLD_QT as null , SCHED_INITIAL_QT is not
    * null and SCHED_INTERVAL_QT is not null. It should pass validation.
    *
    */

   @Test
   public void testOPER15067RDP6VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_COMP_REQ
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_1 + "'" );
      lReqMap.put( "REQ_TASK_DESC", "'" + iREQ_TASK_DESC_1 + "'" );
      lReqMap.put( "EXT_REFERENCE", "'" + iREQ_TASK_REF_SDESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD_COMP + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD_COMP + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "ETOPS_SIGNIFICANT_BOOL", "'" + iETOPS_SIGNIFICANT_BOOL + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "APPLICABILITY_RANGE", "'" + iAPPLICABILITY_RANGE + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MIN_USAGE_RELEASE_PCT", "'" + iMIN_USAGE_RELEASE_PCT + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE_QT", "'" + iMIN_FORECAST_RANGE_QT + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE_QT", "'" + iMIN_FORECAST_RANGE_QT + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL + "'" );
      lReqMap.put( "EST_DURATION_HRS", "'" + iEST_DURATION_HRS + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "RESCHED_FROM_CD", "'" + iRESCHED_FROM_CD + "'" );
      lReqMap.put( "REQ_INSTRUCTIONS", "'" + iREQ_INSTRUCTIONS + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'Y'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ, lReqMap ) );

      // C_COMP_REQ_ASSIGNED_PART
      // first record
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_ASSIGNED_PART, lReqMap ) );
      // second record
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_SER + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_SER + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_ASSIGNED_PART, lReqMap ) );

      // C_COMP_REQ_DYNAMIC_DEADLINE
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_INITIAL_QT", "'" + iSCHED_INITIAL_QT + "'" );
      lReqMap.put( "SCHED_INTERVAL_QT", "'" + iSCHED_INTERVAL_QT + "'" );
      lReqMap.put( "SCHED_THRESHOLD_QT", null );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_DYNAMIC_DEADLINE, lReqMap ) );

      // C_COMP_REQ_DYN_PART_DEADLINE
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_INITIAL_QT", "'" + iSCHED_INITIAL_QT + "'" );
      lReqMap.put( "SCHED_INTERVAL_QT", "'" + iSCHED_INTERVAL_QT + "'" );
      lReqMap.put( "SCHED_THRESHOLD_QT", null );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_DYN_PART_DEADLINE, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify JIRA-15067 on c_comp_req_import.import_comp_req functionality
    * Requirements-dynamic deadline RD. 6. In c_COMP_req table, create a requirement with
    * RESCHED_FROM_CD is not null, and in C_COMP_REQ_DYNAMIC_DEADLINE and
    * C_COMP_REQ_DYN_PART_DEADLINE table set SCHED_THRESHOLD_QT as null , SCHED_INITIAL_QT is not
    * null and SCHED_INTERVAL_QT is not null. It should pass validation.
    *
    */

   @Test
   public void testOPER15067RDP6IMPORT() {

      System.out.println( "=======Starting: " + testName.getMethodName()
            + " Validation========================" );

      testOPER15067RDP6VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs_15067 = null;
      iTASK_DEFN_IDs_15067 = null;

      // Verify task_task table
      iTASK_IDs_15067 = getTaskIds( iREQ_TASK_CD_1, iREQ_TASK_NAME_1 );

      iTASK_DEFN_IDs_15067 = verifyTaskTask_15067( iTASK_IDs_15067, iCLASS_CD_COMP, null,
            iSUBCLASS_CD_COMP, iORIGINATOR_CD, iREQ_TASK_CD_1, iREQ_TASK_NAME_1, iREQ_TASK_DESC_1,
            iREQ_INSTRUCTIONS, null, iAPPLICABILITY_RANGE, iSCHED_TO_LATEST_DEADLINE_BOOL_N,
            iSOFT_DEADLINE_BOOL_N, null, iMUST_BE_REMOVED_CD, iETOPS_SIGNIFICANT_BOOL_N,
            iON_CONDITION_BOOL_N, iEST_DURATION_HRS );

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

      // verify task_task_flags
      lQuery = "select 1 from " + TableUtil.TASK_TASK_FLAGS + " where TASK_DB_ID="
            + iTASK_IDs_15067.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_15067.getNO_ID();
      Assert.assertTrue( "Check task_task_flags table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_part_map table
      simpleIDs lPNIDs_TRK = getPartNoIds( iPART_NO_OEM_TRK, iMANUFACT_CD_TRK );
      simpleIDs lPNIDs_SER = getPartNoIds( iPART_NO_OEM_SER, iMANUFACT_CD_SER );

      lQuery = "select 1 from " + TableUtil.TASK_PART_MAP + " where TASK_DB_ID="
            + iTASK_IDs_15067.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_15067.getNO_ID()
            + " and PART_NO_DB_ID=" + lPNIDs_TRK.getNO_DB_ID() + " and PART_NO_ID="
            + lPNIDs_TRK.getNO_ID();
      Assert.assertTrue( "Check task_part_map  table to verify the TRK record exists",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.TASK_PART_MAP + " where TASK_DB_ID="
            + iTASK_IDs_15067.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_15067.getNO_ID()
            + " and PART_NO_DB_ID=" + lPNIDs_SER.getNO_DB_ID() + " and PART_NO_ID="
            + lPNIDs_SER.getNO_ID();
      Assert.assertTrue( "Check task_part_map  table to verify the SER record exists",
            RecordsExist( lQuery ) );

   }


   /**
    * This test is to verify JIRA-15067 on comp_req_validate_comp_req functionality
    * Requirements-dynamic deadline RD. 7. In c_COMP_req table, create a requirement with
    * RESCHED_FROM_CD not null, and in C_COMP_REQ_DYNAMIC_DEADLINE and C_COMP_REQ_DYN_PART_DEADLINE
    * table set SCHED_THRESHOLD_QT as null,SCHED_INITIAL_QT is not null and SCHED_INTERVAL_QT is
    * null. It should not pass validation with error code "CFG_COMP_REQ-10021" and
    * "CFG_COMP_REQ-10502".
    *
    */

   @Test
   public void testOPER15067RDP7VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_COMP_REQ
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_1 + "'" );
      lReqMap.put( "REQ_TASK_DESC", "'" + iREQ_TASK_DESC_1 + "'" );
      lReqMap.put( "EXT_REFERENCE", "'" + iREQ_TASK_REF_SDESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD_COMP + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD_COMP + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "ETOPS_SIGNIFICANT_BOOL", "'" + iETOPS_SIGNIFICANT_BOOL + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "APPLICABILITY_RANGE", "'" + iAPPLICABILITY_RANGE + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MIN_USAGE_RELEASE_PCT", "'" + iMIN_USAGE_RELEASE_PCT + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE_QT", "'" + iMIN_FORECAST_RANGE_QT + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE_QT", "'" + iMIN_FORECAST_RANGE_QT + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL + "'" );
      lReqMap.put( "EST_DURATION_HRS", "'" + iEST_DURATION_HRS + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "RESCHED_FROM_CD", "'" + iRESCHED_FROM_CD + "'" );
      lReqMap.put( "REQ_INSTRUCTIONS", "'" + iREQ_INSTRUCTIONS + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'Y'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ, lReqMap ) );

      // C_COMP_REQ_ASSIGNED_PART
      // first record
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_ASSIGNED_PART, lReqMap ) );
      // second record
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_SER + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_SER + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_ASSIGNED_PART, lReqMap ) );

      // C_COMP_REQ_DYNAMIC_DEADLINE
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_INITIAL_QT", "'" + iSCHED_INITIAL_QT + "'" );
      lReqMap.put( "SCHED_INTERVAL_QT", null );
      lReqMap.put( "SCHED_THRESHOLD_QT", null );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_DYNAMIC_DEADLINE, lReqMap ) );

      // C_COMP_REQ_DYN_PART_DEADLINE
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_INITIAL_QT", "'" + iSCHED_INITIAL_QT + "'" );
      lReqMap.put( "SCHED_INTERVAL_QT", null );
      lReqMap.put( "SCHED_THRESHOLD_QT", null );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_DYN_PART_DEADLINE, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );
      validateErrorCode( "CFG_COMP_REQ-10021" );
      validateErrorCode( "CFG_COMP_REQ-10502" );

   }


   /**
    * This test is to verify OPER-24637: A REQ of type FOLLOW cannot be part-based. If a REQ of type
    * FOLLOW is added, validation error code 'CFG_COMP_REQ-17005' is thrown.
    *
    */

   @Test
   public void testOPER24637_Error17005_TASK_CLASS_FOLLOW_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_COMP_REQ
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_1 + "'" );
      lReqMap.put( "REQ_TASK_DESC", "'" + iREQ_TASK_DESC_1 + "'" );
      lReqMap.put( "EXT_REFERENCE", "'" + iREQ_TASK_REF_SDESC_1 + "'" );
      lReqMap.put( "ETOPS_SIGNIFICANT_BOOL", "'" + iETOPS_SIGNIFICANT_BOOL + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MIN_USAGE_RELEASE_PCT", "'" + iMIN_USAGE_RELEASE_PCT + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE_QT", "'" + iMIN_FORECAST_RANGE_QT + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL + "'" );
      lReqMap.put( "EST_DURATION_HRS", "'" + iEST_DURATION_HRS + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL_Y + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "REVIEW_ON_RECEIPT_BOOL", "'" + iREVIEW_ON_RECEIPT_BOOL_N + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD_FOLLOW + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );
      // Insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ, lReqMap ) );

      // C_COMP_REQ_ASSIGNED_PART
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK + "'" );
      // Insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_ASSIGNED_PART, lReqMap ) );

      // Run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // Validate CFG_COMP_REQ-17005 error
      validateErrorCode( "CFG_COMP_REQ-17005" );
   }


   /**
    * This test is to verify OPER-24638: A REQ of type REPREF cannot be part-based. If a REQ of type
    * REPREF is added, validation error code 'CFG_COMP_REQ-17006' is thrown.
    *
    */

   @Test
   public void testOPER24638_Error17006_TASK_CLASS_REPREF_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_COMP_REQ
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_1 + "'" );
      lReqMap.put( "REQ_TASK_DESC", "'" + iREQ_TASK_DESC_1 + "'" );
      lReqMap.put( "EXT_REFERENCE", "'" + iREQ_TASK_REF_SDESC_1 + "'" );
      lReqMap.put( "ETOPS_SIGNIFICANT_BOOL", "'" + iETOPS_SIGNIFICANT_BOOL + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MIN_USAGE_RELEASE_PCT", "'" + iMIN_USAGE_RELEASE_PCT + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE_QT", "'" + iMIN_FORECAST_RANGE_QT + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL + "'" );
      lReqMap.put( "EST_DURATION_HRS", "'" + iEST_DURATION_HRS + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL_Y + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "REVIEW_ON_RECEIPT_BOOL", "'" + iREVIEW_ON_RECEIPT_BOOL_N + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD_REPREF + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );
      // Insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ, lReqMap ) );

      // C_COMP_REQ_ASSIGNED_PART
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK + "'" );
      // Insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_ASSIGNED_PART, lReqMap ) );

      // Run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // Validate CFG_COMP_REQ-17006
      validateErrorCode( "CFG_COMP_REQ-17006" );

   }


   /**
    * This test is to verify c_comp_jic_import validation on tables of c_comp_req, c_com_req_jic,
    * c_comp_req_standard_deadline, c_comp_req_ietm, c_comp_req_advisory and c_comp_req_impact.
    * Assigned parts are SER and TRK.
    *
    */
   @Test
   public void testCOMREQ_SER_TRK_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_COMP_REQ
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_1 + "'" );
      lReqMap.put( "REQ_TASK_DESC", "'" + iREQ_TASK_DESC_1 + "'" );
      lReqMap.put( "EXT_REFERENCE", "'" + iREQ_TASK_REF_SDESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD_COMP + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD_COMP + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "ETOPS_SIGNIFICANT_BOOL", "'" + iETOPS_SIGNIFICANT_BOOL + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "APPLICABILITY_RANGE", "'" + iAPPLICABILITY_RANGE + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MIN_USAGE_RELEASE_PCT", "'" + iMIN_USAGE_RELEASE_PCT + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE_QT", "'" + iMIN_FORECAST_RANGE_QT + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE_QT", "'" + iMIN_FORECAST_RANGE_QT + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL_2 + "'" );
      lReqMap.put( "EST_DURATION_HRS", "'" + iEST_DURATION_HRS + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "ENFORCE_WORKSCOPE_ORD_BOOL", "'" + iENFORCE_WORKSCOPE_ORD_BOOL + "'" );
      lReqMap.put( "REQ_INSTRUCTIONS", "'" + iREQ_INSTRUCTIONS + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ, lReqMap ) );

      // C_COMP_REQ_ASSIGNED_PART
      // first record
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK_COMP + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK_COMP + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_ASSIGNED_PART, lReqMap ) );
      // second record
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_SER_COMP + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_SER_COMP + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_ASSIGNED_PART, lReqMap ) );

      // C_COMP_REQ_JIC
      // first record
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "JIC_TASK_CD", "'" + iJIC_TASK_CD_1 + "'" );
      lReqMap.put( "JIC_TASK_ORD", "'1'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_JIC, lReqMap ) );

      // C_COMP_REQ_STANDARD_DEADLINE
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_HRS_THRESHOLD", "'100'" );
      lReqMap.put( "SCHED_CYC_THRESHOLD", "'10'" );
      lReqMap.put( "SCHED_YEAR_THRESHOLD", "'0'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_STANDARD_DEADLINE, lReqMap ) );

      // C_COMP_REQ__IETM
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "IETM_CD", "'" + iREQ_IETM_CD_1 + "'" );
      lReqMap.put( "IETM_TOPIC", "'" + iREQ_TOPIC_SDESC_1 + "'" );
      lReqMap.put( "IETM_ORDER", "1" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_IETM, lReqMap ) );

      // Second record
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "IETM_CD", "'" + iREQ_IETM_CD_2 + "'" );
      lReqMap.put( "IETM_TOPIC", "'" + iREQ_TOPIC_SDESC_2 + "'" );
      lReqMap.put( "IETM_ORDER", "2" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_IETM, lReqMap ) );

      // C_COMP_REQ_ADVISORY
      // first record
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK_COMP + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK_COMP + "'" );
      lReqMap.put( "ADVSRY_TYPE_CD", "'" + iADVSRY_TYPE_CD + "'" );
      lReqMap.put( "ROLE_CD", "'" + iROLE_CD + "'" );
      lReqMap.put( "ADVSRY_NOTE", "'" + iADVSRY_NOTE + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_ADVISORY, lReqMap ) );

      // Second record
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_SER_COMP + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_SER_COMP + "'" );
      lReqMap.put( "ADVSRY_TYPE_CD", "'" + iADVSRY_TYPE_CD_2 + "'" );
      lReqMap.put( "ROLE_CD", "'" + iROLE_CD + "'" );
      lReqMap.put( "ADVSRY_NOTE", "'" + iADVSRY_NOTE + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_ADVISORY, lReqMap ) );

      // C_COMP_REQ_IMPACT
      // first record
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK_COMP + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK_COMP + "'" );
      lReqMap.put( "IMPACT_CD", "'" + iIMPACT_CD + "'" );
      lReqMap.put( "IMPACT_DESC", "'" + iIMPACT_DESC + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_IMPACT, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify c_comp_jic_import import on tables of c_comp_req, c_com_req_jic,
    * c_comp_req_standard_deadline, c_comp_req_ietm, c_comp_req_advisory and c_comp_req_impact.
    * Assigned parts are SER and TRK.
    *
    */

   @Test
   public void testCOMREQ_SER_TRK_IMPORT() {

      System.out.println( "=======Starting: " + testName.getMethodName()
            + " Validation========================" );

      testCOMREQ_SER_TRK_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs = null;
      iTASK_DEFN_IDs = null;

      // Verify task_task table
      iTASK_IDs = getTaskIds( iREQ_TASK_CD_1, iREQ_TASK_NAME_1 );

      iTASK_DEFN_IDs = verifyTaskTask_15067( iTASK_IDs, iCLASS_CD_COMP, null, iSUBCLASS_CD_COMP,
            iORIGINATOR_CD, iREQ_TASK_CD_1, iREQ_TASK_NAME_1, iREQ_TASK_DESC_1, iREQ_INSTRUCTIONS,
            null, iAPPLICABILITY_RANGE, iSCHED_TO_LATEST_DEADLINE_BOOL_N, iSOFT_DEADLINE_BOOL_N,
            null, iMUST_BE_REMOVED_CD, iETOPS_SIGNIFICANT_BOOL_N, iON_CONDITION_BOOL_N,
            iEST_DURATION_HRS );

      // verify task_DEFN
      String lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTASK_DEFN_IDs.getNO_DB_ID() + " and TASK_DEFN_ID=" + iTASK_DEFN_IDs.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_sched_rule table
      simpleIDs lTypeIdsCYR = getTypeIds( iSCHED_DATA_TYPE_CD_CYR );
      simpleIDs lTypeIdsHRS = getTypeIds( iSCHED_DATA_TYPE_CD_HRS );
      simpleIDs lTypeIdsCYCS = getTypeIds( iSCHED_DATA_TYPE_CD_CYCLES );

      verifyTASKSCHEDRULE( iTASK_IDs, lTypeIdsCYR, "0" );
      verifyTASKSCHEDRULE( iTASK_IDs, lTypeIdsHRS, "100" );
      verifyTASKSCHEDRULE( iTASK_IDs, lTypeIdsCYCS, "10" );

      // verify task_task_flags
      lQuery = "select 1 from " + TableUtil.TASK_TASK_FLAGS + " where TASK_DB_ID="
            + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID();
      Assert.assertTrue( "Check task_task_flags table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_part_map table
      simpleIDs lPNIDs_TRK = getPartNoIds( iPART_NO_OEM_TRK_COMP, iMANUFACT_CD_TRK_COMP );
      simpleIDs lPNIDs_SER = getPartNoIds( iPART_NO_OEM_SER_COMP, iMANUFACT_CD_SER_COMP );

      lQuery = "select 1 from " + TableUtil.TASK_PART_MAP + " where TASK_DB_ID="
            + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID()
            + " and PART_NO_DB_ID=" + lPNIDs_TRK.getNO_DB_ID() + " and PART_NO_ID="
            + lPNIDs_TRK.getNO_ID();
      Assert.assertTrue( "Check task_part_map  table to verify the TRK record exists",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.TASK_PART_MAP + " where TASK_DB_ID="
            + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID()
            + " and PART_NO_DB_ID=" + lPNIDs_SER.getNO_DB_ID() + " and PART_NO_ID="
            + lPNIDs_SER.getNO_ID();
      Assert.assertTrue( "Check task_part_map  table to verify the SER record exists",
            RecordsExist( lQuery ) );

      // verify c_comp_req_jic table
      simpleIDs lJICTASKIDs = getTaskIds( iJIC_TASK_CD_1, iJIC_TASK_NAME_1 );
      lQuery = "select 1 from " + TableUtil.TASK_JIC_REQ_MAP + " where JIC_TASK_DB_ID="
            + lJICTASKIDs.getNO_DB_ID() + " and JIC_TASK_ID=" + lJICTASKIDs.getNO_ID()
            + " and REQ_TASK_DEFN_DB_ID=" + iTASK_DEFN_IDs.getNO_DB_ID() + " and REQ_TASK_DEFN_ID="
            + iTASK_DEFN_IDs.getNO_ID();
      Assert.assertTrue( "Check c_comp_req_jic  table to verify the JIC task record exists",
            RecordsExist( lQuery ) );

      // verify task_task_ietm
      // Check first ietm
      simpleIDs lIetmIds_1 = getIetmIDs( iREQ_IETM_CD_1 );
      String lIetmTopicID_1 = getIetmTopicID( lIetmIds_1, iREQ_TOPIC_SDESC_1 );

      lQuery = "select 1 from " + TableUtil.TASK_TASK_IETM + " where TASK_DB_ID="
            + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID() + " and IETM_DB_ID="
            + lIetmIds_1.getNO_DB_ID() + " and IETM_ID=" + lIetmIds_1.getNO_ID()
            + " and IETM_TOPIC_ID='" + lIetmTopicID_1 + "' and IETM_ORD='" + iIETM_ORDER_1 + "'";
      Assert.assertTrue( "Check TASK_TASK_IETM table to verify the first record exists",
            RecordsExist( lQuery ) );

      // Check second ietm
      simpleIDs lIetmIds_2 = getIetmIDs( iREQ_IETM_CD_2 );
      String lIetmTopicID_2 = getIetmTopicID( lIetmIds_2, iREQ_TOPIC_SDESC_2 );

      lQuery = "select 1 from " + TableUtil.TASK_TASK_IETM + " where TASK_DB_ID="
            + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID() + " and IETM_DB_ID="
            + lIetmIds_2.getNO_DB_ID() + " and IETM_ID=" + lIetmIds_2.getNO_ID()
            + " and IETM_TOPIC_ID='" + lIetmTopicID_2 + "' and IETM_ORD='" + iIETM_ORDER_2 + "'";
      Assert.assertTrue( "Check TASK_TASK_IETM table to verify the second record exists",
            RecordsExist( lQuery ) );

      // verify task_advisory
      lQuery = "select ROLE_ID from utl_role where role_cd='" + iROLE_CD + "'";
      String LRoleID = getStringValueFromQuery( lQuery, "ROLE_ID" );

      // verify first record
      verifyTASKADVISORY( iTASK_IDs, iADVSRY_TYPE_CD, LRoleID, iADVSRY_NOTE );
      // verify second record
      verifyTASKADVISORY( iTASK_IDs, iADVSRY_TYPE_CD_2, LRoleID, iADVSRY_NOTE );

      // verify task_impact
      verifyTASKIMPACT( iTASK_IDs, iIMPACT_CD, iIMPACT_DESC );

   }


   /**
    * This test is to verify c_comp_jic_import validation on tables of c_comp_req, c_com_req_jic,
    * c_comp_req_ietm, c_comp_req_advisory and c_comp_req_impact. Assigned part is BATCH
    *
    */
   @Test
   public void testCOMREQ_BATCH_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_COMP_REQ
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_2 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_2 + "'" );
      lReqMap.put( "REQ_TASK_DESC", "'" + iREQ_TASK_DESC_2 + "'" );
      lReqMap.put( "EXT_REFERENCE", "'" + iREQ_TASK_REF_SDESC_2 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD_COMP + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD_COMP + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "ETOPS_SIGNIFICANT_BOOL", "'" + iETOPS_SIGNIFICANT_BOOL + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL_2 + "'" );
      lReqMap.put( "EST_DURATION_HRS", "'" + iEST_DURATION_HRS + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL_Y + "'" );
      lReqMap.put( "REVIEW_ON_RECEIPT_BOOL", "'" + iREVIEW_ON_RECEIPT_BOOL_N + "'" );
      lReqMap.put( "ENFORCE_WORKSCOPE_ORD_BOOL", "'" + iENFORCE_WORKSCOPE_ORD_BOOL + "'" );
      lReqMap.put( "REQ_INSTRUCTIONS", "'" + iREQ_INSTRUCTIONS + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ, lReqMap ) );

      // C_COMP_REQ_ASSIGNED_PART
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_2 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_BATCH_COMP + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_BATCH_COMP + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_ASSIGNED_PART, lReqMap ) );

      // C_COMP_REQ_JIC
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_2 + "'" );
      lReqMap.put( "JIC_TASK_CD", "'" + iJIC_TASK_CD_2 + "'" );
      lReqMap.put( "JIC_TASK_ORD", "'1'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_JIC, lReqMap ) );

      // C_COMP_REQ__IETM
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_2 + "'" );
      lReqMap.put( "IETM_CD", "'" + iREQ_IETM_CD_1 + "'" );
      lReqMap.put( "IETM_TOPIC", "'" + iREQ_TOPIC_SDESC_1 + "'" );
      lReqMap.put( "IETM_ORDER", "1" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_IETM, lReqMap ) );

      // Second record
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_2 + "'" );
      lReqMap.put( "IETM_CD", "'" + iREQ_IETM_CD_2 + "'" );
      lReqMap.put( "IETM_TOPIC", "'" + iREQ_TOPIC_SDESC_2 + "'" );
      lReqMap.put( "IETM_ORDER", "2" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_IETM, lReqMap ) );

      // C_COMP_REQ_ADVISORY
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_2 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_BATCH_COMP + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_BATCH_COMP + "'" );
      lReqMap.put( "ADVSRY_TYPE_CD", "'" + iADVSRY_TYPE_CD + "'" );
      lReqMap.put( "ROLE_CD", "'" + iROLE_CD + "'" );
      lReqMap.put( "ADVSRY_NOTE", "'" + iADVSRY_NOTE + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_ADVISORY, lReqMap ) );

      // C_COMP_REQ_IMPACT
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_2 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_BATCH_COMP + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_BATCH_COMP + "'" );
      lReqMap.put( "IMPACT_CD", "'" + iIMPACT_CD + "'" );
      lReqMap.put( "IMPACT_DESC", "'" + iIMPACT_DESC + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_IMPACT, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify c_comp_jic_import import on tables of c_comp_req, c_com_req_jic,
    * c_comp_req_ietm, c_comp_req_advisory and c_comp_req_impact. Assigned part is BATCH
    *
    */

   @Test
   public void testCOMREQ_BATCH_IMPORT() {

      System.out.println( "=======Starting: " + testName.getMethodName()
            + " Validation========================" );

      testCOMREQ_BATCH_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs = null;
      iTASK_DEFN_IDs = null;

      // Verify task_task table
      iTASK_IDs = getTaskIds( iREQ_TASK_CD_2, iREQ_TASK_NAME_2 );

      iTASK_DEFN_IDs = verifyTaskTask_15067( iTASK_IDs, iCLASS_CD_COMP, null, iSUBCLASS_CD_COMP,
            iORIGINATOR_CD, iREQ_TASK_CD_2, iREQ_TASK_NAME_2, iREQ_TASK_DESC_2, iREQ_INSTRUCTIONS,
            null, null, null, null, null, null, iETOPS_SIGNIFICANT_BOOL_N,
            iON_CONDITION_BOOL_Y_number, iEST_DURATION_HRS );

      // verify task_DEFN
      String lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTASK_DEFN_IDs.getNO_DB_ID() + " and TASK_DEFN_ID=" + iTASK_DEFN_IDs.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_task_flags
      lQuery = "select 1 from " + TableUtil.TASK_TASK_FLAGS + " where TASK_DB_ID="
            + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID();
      Assert.assertTrue( "Check task_task_flags table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_part_map table
      simpleIDs lPNIDs_BATCH = getPartNoIds( iPART_NO_OEM_BATCH_COMP, iMANUFACT_CD_BATCH_COMP );

      lQuery = "select 1 from " + TableUtil.TASK_PART_MAP + " where TASK_DB_ID="
            + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID()
            + " and PART_NO_DB_ID=" + lPNIDs_BATCH.getNO_DB_ID() + " and PART_NO_ID="
            + lPNIDs_BATCH.getNO_ID();
      Assert.assertTrue( "Check task_part_map  table to verify the TRK record exists",
            RecordsExist( lQuery ) );

      // verify c_comp_req_jic table
      simpleIDs lJICTASKIDs = getTaskIds( iJIC_TASK_CD_2, iJIC_TASK_NAME_2 );
      lQuery = "select 1 from " + TableUtil.TASK_JIC_REQ_MAP + " where JIC_TASK_DB_ID="
            + lJICTASKIDs.getNO_DB_ID() + " and JIC_TASK_ID=" + lJICTASKIDs.getNO_ID()
            + " and REQ_TASK_DEFN_DB_ID=" + iTASK_DEFN_IDs.getNO_DB_ID() + " and REQ_TASK_DEFN_ID="
            + iTASK_DEFN_IDs.getNO_ID();
      Assert.assertTrue( "Check c_comp_req_jic  table to verify the JIC task record exists",
            RecordsExist( lQuery ) );

      // verify task_task_ietm
      // Check first ietm
      simpleIDs lIetmIds_1 = getIetmIDs( iREQ_IETM_CD_1 );
      String lIetmTopicID_1 = getIetmTopicID( lIetmIds_1, iREQ_TOPIC_SDESC_1 );

      lQuery = "select 1 from " + TableUtil.TASK_TASK_IETM + " where TASK_DB_ID="
            + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID() + " and IETM_DB_ID="
            + lIetmIds_1.getNO_DB_ID() + " and IETM_ID=" + lIetmIds_1.getNO_ID()
            + " and IETM_TOPIC_ID='" + lIetmTopicID_1 + "' and IETM_ORD='" + iIETM_ORDER_1 + "'";
      Assert.assertTrue( "Check TASK_TASK_IETM table to verify the first record exists",
            RecordsExist( lQuery ) );

      // Check second ietm
      simpleIDs lIetmIds_2 = getIetmIDs( iREQ_IETM_CD_2 );
      String lIetmTopicID_2 = getIetmTopicID( lIetmIds_2, iREQ_TOPIC_SDESC_2 );

      lQuery = "select 1 from " + TableUtil.TASK_TASK_IETM + " where TASK_DB_ID="
            + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID() + " and IETM_DB_ID="
            + lIetmIds_2.getNO_DB_ID() + " and IETM_ID=" + lIetmIds_2.getNO_ID()
            + " and IETM_TOPIC_ID='" + lIetmTopicID_2 + "' and IETM_ORD='" + iIETM_ORDER_2 + "'";
      Assert.assertTrue( "Check TASK_TASK_IETM table to verify the second record exists",
            RecordsExist( lQuery ) );

      // verify task_advisory
      lQuery = "select ROLE_ID from utl_role where role_cd='" + iROLE_CD + "'";
      String LRoleID = getStringValueFromQuery( lQuery, "ROLE_ID" );

      // verify first record
      verifyTASKADVISORY( iTASK_IDs, iADVSRY_TYPE_CD, LRoleID, iADVSRY_NOTE );

      // verify task_impact
      verifyTASKIMPACT( iTASK_IDs, iIMPACT_CD, iIMPACT_DESC );

   }


   /**
    * This test is to verify c_comp_jic_import import multiple REQs on tables of c_comp_req,
    * c_com_req_jic, c_comp_req_standard_deadline, c_comp_req_ietm, c_comp_req_advisory and
    * c_comp_req_impact.
    *
    *
    */

   @Test
   public void testCOMREQ_MultipleREQs_IMPORT() {

      System.out.println( "=======Starting: " + testName.getMethodName()
            + " Validation========================" );
      testCOMREQ_SER_TRK_VALIDATION();
      testCOMREQ_BATCH_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs = null;
      iTASK_DEFN_IDs = null;

      iTASK_IDs_2 = null;
      iTASK_DEFN_IDs_2 = null;

      // Verify task_task table
      // first task
      iTASK_IDs = getTaskIds( iREQ_TASK_CD_1, iREQ_TASK_NAME_1 );

      iTASK_DEFN_IDs = verifyTaskTask_15067( iTASK_IDs, iCLASS_CD_COMP, null, iSUBCLASS_CD_COMP,
            iORIGINATOR_CD, iREQ_TASK_CD_1, iREQ_TASK_NAME_1, iREQ_TASK_DESC_1, iREQ_INSTRUCTIONS,
            null, iAPPLICABILITY_RANGE, iSCHED_TO_LATEST_DEADLINE_BOOL_N, iSOFT_DEADLINE_BOOL_N,
            null, iMUST_BE_REMOVED_CD, iETOPS_SIGNIFICANT_BOOL_N, iON_CONDITION_BOOL_N,
            iEST_DURATION_HRS );
      // second task
      iTASK_IDs_2 = getTaskIds( iREQ_TASK_CD_2, iREQ_TASK_NAME_2 );

      iTASK_DEFN_IDs_2 = verifyTaskTask_15067( iTASK_IDs_2, iCLASS_CD_COMP, null, iSUBCLASS_CD_COMP,
            iORIGINATOR_CD, iREQ_TASK_CD_2, iREQ_TASK_NAME_2, iREQ_TASK_DESC_2, iREQ_INSTRUCTIONS,
            null, null, null, null, null, null, iETOPS_SIGNIFICANT_BOOL_N,
            iON_CONDITION_BOOL_Y_number, iEST_DURATION_HRS );

      // verify task_DEFN
      // first task
      String lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTASK_DEFN_IDs.getNO_DB_ID() + " and TASK_DEFN_ID=" + iTASK_DEFN_IDs.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // second task
      lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTASK_DEFN_IDs_2.getNO_DB_ID() + " and TASK_DEFN_ID=" + iTASK_DEFN_IDs_2.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_sched_rule table
      simpleIDs lTypeIdsCYR = getTypeIds( iSCHED_DATA_TYPE_CD_CYR );
      simpleIDs lTypeIdsHRS = getTypeIds( iSCHED_DATA_TYPE_CD_HRS );
      simpleIDs lTypeIdsCYCS = getTypeIds( iSCHED_DATA_TYPE_CD_CYCLES );

      verifyTASKSCHEDRULE( iTASK_IDs, lTypeIdsCYR, "0" );
      verifyTASKSCHEDRULE( iTASK_IDs, lTypeIdsHRS, "100" );
      verifyTASKSCHEDRULE( iTASK_IDs, lTypeIdsCYCS, "10" );

      // verify task_task_flags
      // first task
      lQuery = "select 1 from " + TableUtil.TASK_TASK_FLAGS + " where TASK_DB_ID="
            + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID();
      Assert.assertTrue( "Check task_task_flags table to verify the record exists",
            RecordsExist( lQuery ) );

      // second task
      lQuery = "select 1 from " + TableUtil.TASK_TASK_FLAGS + " where TASK_DB_ID="
            + iTASK_IDs_2.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_2.getNO_ID();
      Assert.assertTrue( "Check task_task_flags table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_part_map table
      simpleIDs lPNIDs_TRK = getPartNoIds( iPART_NO_OEM_TRK_COMP, iMANUFACT_CD_TRK_COMP );
      simpleIDs lPNIDs_SER = getPartNoIds( iPART_NO_OEM_SER_COMP, iMANUFACT_CD_SER_COMP );
      simpleIDs lPNIDs_BATCH = getPartNoIds( iPART_NO_OEM_BATCH_COMP, iMANUFACT_CD_BATCH_COMP );

      lQuery = "select 1 from " + TableUtil.TASK_PART_MAP + " where TASK_DB_ID="
            + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID()
            + " and PART_NO_DB_ID=" + lPNIDs_TRK.getNO_DB_ID() + " and PART_NO_ID="
            + lPNIDs_TRK.getNO_ID();
      Assert.assertTrue( "Check task_part_map  table to verify the TRK record exists",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.TASK_PART_MAP + " where TASK_DB_ID="
            + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID()
            + " and PART_NO_DB_ID=" + lPNIDs_SER.getNO_DB_ID() + " and PART_NO_ID="
            + lPNIDs_SER.getNO_ID();
      Assert.assertTrue( "Check task_part_map  table to verify the SER record exists",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.TASK_PART_MAP + " where TASK_DB_ID="
            + iTASK_IDs_2.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_2.getNO_ID()
            + " and PART_NO_DB_ID=" + lPNIDs_BATCH.getNO_DB_ID() + " and PART_NO_ID="
            + lPNIDs_BATCH.getNO_ID();
      Assert.assertTrue( "Check task_part_map  table to verify the BATCH record exists",
            RecordsExist( lQuery ) );

      // verify c_comp_req_jic table
      // first task
      simpleIDs lJICTASKIDs = getTaskIds( iJIC_TASK_CD_1, iJIC_TASK_NAME_1 );
      lQuery = "select 1 from " + TableUtil.TASK_JIC_REQ_MAP + " where JIC_TASK_DB_ID="
            + lJICTASKIDs.getNO_DB_ID() + " and JIC_TASK_ID=" + lJICTASKIDs.getNO_ID()
            + " and REQ_TASK_DEFN_DB_ID=" + iTASK_DEFN_IDs.getNO_DB_ID() + " and REQ_TASK_DEFN_ID="
            + iTASK_DEFN_IDs.getNO_ID();
      Assert.assertTrue( "Check c_comp_req_jic  table to verify the JIC task record exists",
            RecordsExist( lQuery ) );

      // second task
      simpleIDs lJICTASKIDs_2 = getTaskIds( iJIC_TASK_CD_2, iJIC_TASK_NAME_2 );
      lQuery = "select 1 from " + TableUtil.TASK_JIC_REQ_MAP + " where JIC_TASK_DB_ID="
            + lJICTASKIDs_2.getNO_DB_ID() + " and JIC_TASK_ID=" + lJICTASKIDs_2.getNO_ID()
            + " and REQ_TASK_DEFN_DB_ID=" + iTASK_DEFN_IDs_2.getNO_DB_ID()
            + " and REQ_TASK_DEFN_ID=" + iTASK_DEFN_IDs_2.getNO_ID();
      Assert.assertTrue( "Check c_comp_req_jic  table to verify the JIC task record exists",
            RecordsExist( lQuery ) );

      // verify task_task_ietm
      // Check first ietm of first task
      simpleIDs lIetmIds_1 = getIetmIDs( iREQ_IETM_CD_1 );
      String lIetmTopicID_1 = getIetmTopicID( lIetmIds_1, iREQ_TOPIC_SDESC_1 );

      lQuery = "select 1 from " + TableUtil.TASK_TASK_IETM + " where TASK_DB_ID="
            + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID() + " and IETM_DB_ID="
            + lIetmIds_1.getNO_DB_ID() + " and IETM_ID=" + lIetmIds_1.getNO_ID()
            + " and IETM_TOPIC_ID='" + lIetmTopicID_1 + "' and IETM_ORD='" + iIETM_ORDER_1 + "'";
      Assert.assertTrue( "Check TASK_TASK_IETM table to verify the first record exists",
            RecordsExist( lQuery ) );

      // Check second task ietm
      lQuery = "select 1 from " + TableUtil.TASK_TASK_IETM + " where TASK_DB_ID="
            + iTASK_IDs_2.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_2.getNO_ID()
            + " and IETM_DB_ID=" + lIetmIds_1.getNO_DB_ID() + " and IETM_ID="
            + lIetmIds_1.getNO_ID() + " and IETM_TOPIC_ID='" + lIetmTopicID_1 + "' and IETM_ORD='"
            + iIETM_ORDER_1 + "'";
      Assert.assertTrue( "Check TASK_TASK_IETM table to verify the second task first record exists",
            RecordsExist( lQuery ) );

      // Check second ietm of first task
      simpleIDs lIetmIds_2 = getIetmIDs( iREQ_IETM_CD_2 );
      String lIetmTopicID_2 = getIetmTopicID( lIetmIds_2, iREQ_TOPIC_SDESC_2 );

      lQuery = "select 1 from " + TableUtil.TASK_TASK_IETM + " where TASK_DB_ID="
            + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID() + " and IETM_DB_ID="
            + lIetmIds_2.getNO_DB_ID() + " and IETM_ID=" + lIetmIds_2.getNO_ID()
            + " and IETM_TOPIC_ID='" + lIetmTopicID_2 + "' and IETM_ORD='" + iIETM_ORDER_2 + "'";
      Assert.assertTrue( "Check TASK_TASK_IETM table to verify the second record exists",
            RecordsExist( lQuery ) );

      // Check second task ietm
      lQuery = "select 1 from " + TableUtil.TASK_TASK_IETM + " where TASK_DB_ID="
            + iTASK_IDs_2.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_2.getNO_ID()
            + " and IETM_DB_ID=" + lIetmIds_2.getNO_DB_ID() + " and IETM_ID="
            + lIetmIds_2.getNO_ID() + " and IETM_TOPIC_ID='" + lIetmTopicID_2 + "' and IETM_ORD='"
            + iIETM_ORDER_2 + "'";
      Assert.assertTrue(
            "Check TASK_TASK_IETM table to verify the second task second record exists",
            RecordsExist( lQuery ) );

      // ================================

      // verify task_advisory
      lQuery = "select ROLE_ID from utl_role where role_cd='" + iROLE_CD + "'";
      String LRoleID = getStringValueFromQuery( lQuery, "ROLE_ID" );

      // verify first record
      verifyTASKADVISORY( iTASK_IDs, iADVSRY_TYPE_CD, LRoleID, iADVSRY_NOTE );
      // verify second record
      verifyTASKADVISORY( iTASK_IDs, iADVSRY_TYPE_CD_2, LRoleID, iADVSRY_NOTE );

      // verify record of seconf task
      // verify first record
      verifyTASKADVISORY( iTASK_IDs_2, iADVSRY_TYPE_CD, LRoleID, iADVSRY_NOTE );

      // verify task_impact
      // first task
      verifyTASKIMPACT( iTASK_IDs, iIMPACT_CD, iIMPACT_DESC );

      // verify task_impact
      // second task
      verifyTASKIMPACT( iTASK_IDs_2, iIMPACT_CD, iIMPACT_DESC );

   }


   /**
    * This test is to verify export functionality
    *
    * @throws SQLException
    *
    */

   @Test
   public void testEXPORT() throws SQLException {

      System.out.println(
            "=======Starting: " + testName.getMethodName() + " Export========================" );

      testOPER15067RDP1IMPORT();
      clearBaselineLoaderTables();

      // run export
      Assert.assertTrue( runExport() == 1 );

      // verify C_COMP_REQ
      String lQuery = "select 1 from " + TableUtil.C_COMP_REQ + " where REQ_TASK_CD='"
            + iREQ_TASK_CD_1 + "' and MUST_BE_REMOVED_CD='" + iMUST_BE_REMOVED_CD
            + "' and RECURRING_TASK_BOOL='N'";
      Assert.assertTrue( "Check C_COMP_REQ table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify C_COMP_REQ_ASSIGNED_PART
      lQuery = "select 1 from " + TableUtil.C_COMP_REQ_ASSIGNED_PART + " where REQ_TASK_CD='"
            + iREQ_TASK_CD_1 + "' and PART_NO_OEM='" + iPART_NO_OEM_TRK + "' and MANUFACT_CD='"
            + iMANUFACT_CD_TRK + "'";
      Assert.assertTrue( "Check C_COMP_REQ_ASSIGNED_PART table to verify the record exists",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.C_COMP_REQ_ASSIGNED_PART + " where REQ_TASK_CD='"
            + iREQ_TASK_CD_1 + "' and PART_NO_OEM='" + iPART_NO_OEM_SER + "' and MANUFACT_CD='"
            + iMANUFACT_CD_SER + "'";
      Assert.assertTrue( "Check C_COMP_REQ_ASSIGNED_PART table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify C_COMP_REQ_DYNAMIC_DEADLINE
      lQuery = "select 1 from " + TableUtil.C_COMP_REQ_DYN_PART_DEADLINE + " where REQ_TASK_CD='"
            + iREQ_TASK_CD_1 + "' and PART_NO_OEM='" + iPART_NO_OEM_TRK + "' and MANUFACT_CD='"
            + iMANUFACT_CD_TRK + "' and SCHED_DATA_TYPE_CD='" + iSCHED_DATA_TYPE_CD
            + "' and SCHED_THRESHOLD_QT='" + iSCHED_THRESHOLD_QT + "'";
      Assert.assertTrue( "Check C_COMP_REQ_DYNAMIC_DEADLINE table to verify the record exists",
            RecordsExist( lQuery ) );

   }


   /**
    * This test is to verify OPER-15064 fix.REQs that are on-condition and non-recurring should be
    * loaded as non-unique. on-condition and recurring should be unique.
    *
    */

   @Test
   public void testOPER_15064_1_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_COMP_REQ
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_1 + "'" );
      lReqMap.put( "REQ_TASK_DESC", "'" + iREQ_TASK_DESC_1 + "'" );
      lReqMap.put( "EXT_REFERENCE", "'" + iREQ_TASK_REF_SDESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD_COMP + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD_COMP + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "ETOPS_SIGNIFICANT_BOOL", "'" + iETOPS_SIGNIFICANT_BOOL + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "APPLICABILITY_RANGE", "'" + iAPPLICABILITY_RANGE + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MIN_USAGE_RELEASE_PCT", "'" + iMIN_USAGE_RELEASE_PCT + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE_QT", "'" + iMIN_FORECAST_RANGE_QT + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE_QT", "'" + iMIN_FORECAST_RANGE_QT + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL + "'" );
      lReqMap.put( "EST_DURATION_HRS", "'" + iEST_DURATION_HRS + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "REQ_INSTRUCTIONS", "'" + iREQ_INSTRUCTIONS + "'" );

      lReqMap.put( "Review_on_receipt_bool", "'N'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'Y'" );
      lReqMap.put( "RESCHED_FROM_CD", "'EXECUTE'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'Y'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ, lReqMap ) );

      // C_COMP_REQ_ASSIGNED_PART
      // first record
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_ASSIGNED_PART, lReqMap ) );

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
      System.out.println( "=======Starting: " + testName.getMethodName()
            + " Validation========================" );

      testOPER_15064_1_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs_15067 = null;
      iTASK_DEFN_IDs_15067 = null;

      // Verify task_task table
      iTASK_IDs_15067 = getTaskIds( iREQ_TASK_CD_1, iREQ_TASK_NAME_1 );

      iTASK_DEFN_IDs_15067 = verifyTaskTask_15067( iTASK_IDs_15067, iCLASS_CD_COMP, null,
            iSUBCLASS_CD_COMP, iORIGINATOR_CD, iREQ_TASK_CD_1, iREQ_TASK_NAME_1, iREQ_TASK_DESC_1,
            iREQ_INSTRUCTIONS, null, iAPPLICABILITY_RANGE, iSCHED_TO_LATEST_DEADLINE_BOOL_N,
            iSOFT_DEADLINE_BOOL_N, null, iMUST_BE_REMOVED_CD, iETOPS_SIGNIFICANT_BOOL_N, "1",
            iEST_DURATION_HRS );

      verifyTaskTask_15067( iTASK_IDs_15067, iCLASS_CD_COMP, null, iSUBCLASS_CD_COMP,
            iORIGINATOR_CD, iREQ_TASK_CD_1, iREQ_TASK_NAME_1, iREQ_TASK_DESC_1, iREQ_INSTRUCTIONS,
            null, iAPPLICABILITY_RANGE, iSCHED_TO_LATEST_DEADLINE_BOOL_N, iSOFT_DEADLINE_BOOL_N,
            null, iMUST_BE_REMOVED_CD, iETOPS_SIGNIFICANT_BOOL_N, "1", iEST_DURATION_HRS, "1" );

      // verify task_DEFN
      String lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTASK_DEFN_IDs_15067.getNO_DB_ID() + " and TASK_DEFN_ID="
            + iTASK_DEFN_IDs_15067.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_task_flags
      lQuery = "select 1 from " + TableUtil.TASK_TASK_FLAGS + " where TASK_DB_ID="
            + iTASK_IDs_15067.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_15067.getNO_ID();
      Assert.assertTrue( "Check task_task_flags table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_part_map table
      simpleIDs lPNIDs_TRK = getPartNoIds( iPART_NO_OEM_TRK, iMANUFACT_CD_TRK );

      lQuery = "select 1 from " + TableUtil.TASK_PART_MAP + " where TASK_DB_ID="
            + iTASK_IDs_15067.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_15067.getNO_ID()
            + " and PART_NO_DB_ID=" + lPNIDs_TRK.getNO_DB_ID() + " and PART_NO_ID="
            + lPNIDs_TRK.getNO_ID();
      Assert.assertTrue( "Check task_part_map  table to verify the TRK record exists",
            RecordsExist( lQuery ) );

      // verify task_task_dep
      verifyTaskTaskDep( iTASK_IDs_15067, "CRT" );

   }


   /**
    * This test is to verify OPER-15064 fix.REQs that are on-condition and non-recurring should be
    * loaded as non-unique. not on-condition and recurring should be unique.
    *
    */
   @Test
   public void testOPER_15064_2_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_COMP_REQ
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_1 + "'" );
      lReqMap.put( "REQ_TASK_DESC", "'" + iREQ_TASK_DESC_1 + "'" );
      lReqMap.put( "EXT_REFERENCE", "'" + iREQ_TASK_REF_SDESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD_COMP + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD_COMP + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "ETOPS_SIGNIFICANT_BOOL", "'" + iETOPS_SIGNIFICANT_BOOL + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "APPLICABILITY_RANGE", "'" + iAPPLICABILITY_RANGE + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MIN_USAGE_RELEASE_PCT", "'" + iMIN_USAGE_RELEASE_PCT + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE_QT", "'" + iMIN_FORECAST_RANGE_QT + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE_QT", "'" + iMIN_FORECAST_RANGE_QT + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL + "'" );
      lReqMap.put( "EST_DURATION_HRS", "'" + iEST_DURATION_HRS + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "REQ_INSTRUCTIONS", "'" + iREQ_INSTRUCTIONS + "'" );

      // lReqMap.put( "Review_on_receipt_bool", "'N'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'N'" );
      lReqMap.put( "RESCHED_FROM_CD", "'EXECUTE'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'Y'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ, lReqMap ) );

      // C_COMP_REQ_ASSIGNED_PART
      // first record
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_ASSIGNED_PART, lReqMap ) );

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
      System.out.println( "=======Starting: " + testName.getMethodName()
            + " Validation========================" );

      testOPER_15064_2_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs_15067 = null;
      iTASK_DEFN_IDs_15067 = null;

      // Verify task_task table
      iTASK_IDs_15067 = getTaskIds( iREQ_TASK_CD_1, iREQ_TASK_NAME_1 );

      iTASK_DEFN_IDs_15067 = verifyTaskTask_15067( iTASK_IDs_15067, iCLASS_CD_COMP, null,
            iSUBCLASS_CD_COMP, iORIGINATOR_CD, iREQ_TASK_CD_1, iREQ_TASK_NAME_1, iREQ_TASK_DESC_1,
            iREQ_INSTRUCTIONS, null, iAPPLICABILITY_RANGE, iSCHED_TO_LATEST_DEADLINE_BOOL_N,
            iSOFT_DEADLINE_BOOL_N, null, iMUST_BE_REMOVED_CD, iETOPS_SIGNIFICANT_BOOL_N, "0",
            iEST_DURATION_HRS );

      verifyTaskTask_15067( iTASK_IDs_15067, iCLASS_CD_COMP, null, iSUBCLASS_CD_COMP,
            iORIGINATOR_CD, iREQ_TASK_CD_1, iREQ_TASK_NAME_1, iREQ_TASK_DESC_1, iREQ_INSTRUCTIONS,
            null, iAPPLICABILITY_RANGE, iSCHED_TO_LATEST_DEADLINE_BOOL_N, iSOFT_DEADLINE_BOOL_N,
            null, iMUST_BE_REMOVED_CD, iETOPS_SIGNIFICANT_BOOL_N, "0", iEST_DURATION_HRS, "1" );

      // verify task_DEFN
      String lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTASK_DEFN_IDs_15067.getNO_DB_ID() + " and TASK_DEFN_ID="
            + iTASK_DEFN_IDs_15067.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_task_flags
      lQuery = "select 1 from " + TableUtil.TASK_TASK_FLAGS + " where TASK_DB_ID="
            + iTASK_IDs_15067.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_15067.getNO_ID();
      Assert.assertTrue( "Check task_task_flags table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_part_map table
      simpleIDs lPNIDs_TRK = getPartNoIds( iPART_NO_OEM_TRK, iMANUFACT_CD_TRK );

      lQuery = "select 1 from " + TableUtil.TASK_PART_MAP + " where TASK_DB_ID="
            + iTASK_IDs_15067.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_15067.getNO_ID()
            + " and PART_NO_DB_ID=" + lPNIDs_TRK.getNO_DB_ID() + " and PART_NO_ID="
            + lPNIDs_TRK.getNO_ID();
      Assert.assertTrue( "Check task_part_map  table to verify the TRK record exists",
            RecordsExist( lQuery ) );

      // verify task_task_dep
      verifyTaskTaskDep( iTASK_IDs_15067, "CRT" );

   }


   /**
    * This test is to verify OPER-15064 fix.REQs that are on-condition and non-recurring should be
    * loaded as non-unique. not on-condition and non-recurring should be unique.
    *
    */
   @Test
   public void testOPER_15064_3_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_COMP_REQ
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_1 + "'" );
      lReqMap.put( "REQ_TASK_DESC", "'" + iREQ_TASK_DESC_1 + "'" );
      lReqMap.put( "EXT_REFERENCE", "'" + iREQ_TASK_REF_SDESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD_COMP + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD_COMP + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "ETOPS_SIGNIFICANT_BOOL", "'" + iETOPS_SIGNIFICANT_BOOL + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "APPLICABILITY_RANGE", "'" + iAPPLICABILITY_RANGE + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MIN_USAGE_RELEASE_PCT", "'" + iMIN_USAGE_RELEASE_PCT + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE_QT", "'" + iMIN_FORECAST_RANGE_QT + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE_QT", "'" + iMIN_FORECAST_RANGE_QT + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL + "'" );
      lReqMap.put( "EST_DURATION_HRS", "'" + iEST_DURATION_HRS + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "REQ_INSTRUCTIONS", "'" + iREQ_INSTRUCTIONS + "'" );

      // lReqMap.put( "Review_on_receipt_bool", "'N'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'N'" );
      lReqMap.put( "RESCHED_FROM_CD", null );
      // lReqMap.put( "RESCHED_FROM_CD", "'EXECUTE'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ, lReqMap ) );

      // C_COMP_REQ_ASSIGNED_PART
      // first record
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_ASSIGNED_PART, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify OPER-15064 fix.REQs that are on-condition and non-recurring should be
    * loaded as non-unique. not on-condition and recurring should be unique.
    *
    */
   @Test
   public void testOPER_15064_3_IMPORT() {
      System.out.println( "=======Starting: " + testName.getMethodName()
            + " Validation========================" );

      testOPER_15064_3_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs_15067 = null;
      iTASK_DEFN_IDs_15067 = null;

      // Verify task_task table
      iTASK_IDs_15067 = getTaskIds( iREQ_TASK_CD_1, iREQ_TASK_NAME_1 );

      iTASK_DEFN_IDs_15067 = verifyTaskTask_15067( iTASK_IDs_15067, iCLASS_CD_COMP, null,
            iSUBCLASS_CD_COMP, iORIGINATOR_CD, iREQ_TASK_CD_1, iREQ_TASK_NAME_1, iREQ_TASK_DESC_1,
            iREQ_INSTRUCTIONS, null, iAPPLICABILITY_RANGE, iSCHED_TO_LATEST_DEADLINE_BOOL_N,
            iSOFT_DEADLINE_BOOL_N, null, iMUST_BE_REMOVED_CD, iETOPS_SIGNIFICANT_BOOL_N, "0",
            iEST_DURATION_HRS );

      verifyTaskTask_15067( iTASK_IDs_15067, iCLASS_CD_COMP, null, iSUBCLASS_CD_COMP,
            iORIGINATOR_CD, iREQ_TASK_CD_1, iREQ_TASK_NAME_1, iREQ_TASK_DESC_1, iREQ_INSTRUCTIONS,
            null, iAPPLICABILITY_RANGE, iSCHED_TO_LATEST_DEADLINE_BOOL_N, iSOFT_DEADLINE_BOOL_N,
            null, iMUST_BE_REMOVED_CD, iETOPS_SIGNIFICANT_BOOL_N, "0", iEST_DURATION_HRS, "1" );

      // verify task_DEFN
      String lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTASK_DEFN_IDs_15067.getNO_DB_ID() + " and TASK_DEFN_ID="
            + iTASK_DEFN_IDs_15067.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_task_flags
      lQuery = "select 1 from " + TableUtil.TASK_TASK_FLAGS + " where TASK_DB_ID="
            + iTASK_IDs_15067.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_15067.getNO_ID();
      Assert.assertTrue( "Check task_task_flags table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_part_map table
      simpleIDs lPNIDs_TRK = getPartNoIds( iPART_NO_OEM_TRK, iMANUFACT_CD_TRK );

      lQuery = "select 1 from " + TableUtil.TASK_PART_MAP + " where TASK_DB_ID="
            + iTASK_IDs_15067.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_15067.getNO_ID()
            + " and PART_NO_DB_ID=" + lPNIDs_TRK.getNO_DB_ID() + " and PART_NO_ID="
            + lPNIDs_TRK.getNO_ID();
      Assert.assertTrue( "Check task_part_map  table to verify the TRK record exists",
            RecordsExist( lQuery ) );

      // verify task_task_dep
      verifyTaskTaskDepNONExist( iTASK_IDs_15067, "CRT" );

   }


   /**
    * This test is to verify OPER-15064 fix.REQs that are on-condition and non-recurring should be
    * loaded as non-unique. on-condition and non-recurring should be non-unique.
    *
    */
   @Test
   public void testOPER_15064_4_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_COMP_REQ
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_1 + "'" );
      lReqMap.put( "REQ_TASK_DESC", "'" + iREQ_TASK_DESC_1 + "'" );
      lReqMap.put( "EXT_REFERENCE", "'" + iREQ_TASK_REF_SDESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD_COMP + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD_COMP + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "ETOPS_SIGNIFICANT_BOOL", "'" + iETOPS_SIGNIFICANT_BOOL + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "APPLICABILITY_RANGE", "'" + iAPPLICABILITY_RANGE + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MIN_USAGE_RELEASE_PCT", "'" + iMIN_USAGE_RELEASE_PCT + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE_QT", "'" + iMIN_FORECAST_RANGE_QT + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE_QT", "'" + iMIN_FORECAST_RANGE_QT + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL + "'" );
      lReqMap.put( "EST_DURATION_HRS", "'" + iEST_DURATION_HRS + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "REQ_INSTRUCTIONS", "'" + iREQ_INSTRUCTIONS + "'" );

      lReqMap.put( "Review_on_receipt_bool", "'N'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'Y'" );
      lReqMap.put( "RESCHED_FROM_CD", null );
      // lReqMap.put( "RESCHED_FROM_CD", "'EXECUTE'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ, lReqMap ) );

      // C_COMP_REQ_ASSIGNED_PART
      // first record
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_ASSIGNED_PART, lReqMap ) );

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
      System.out.println( "=======Starting: " + testName.getMethodName()
            + " Validation========================" );

      testOPER_15064_4_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs_15067 = null;
      iTASK_DEFN_IDs_15067 = null;

      // Verify task_task table
      iTASK_IDs_15067 = getTaskIds( iREQ_TASK_CD_1, iREQ_TASK_NAME_1 );

      iTASK_DEFN_IDs_15067 = verifyTaskTask_15067( iTASK_IDs_15067, iCLASS_CD_COMP, null,
            iSUBCLASS_CD_COMP, iORIGINATOR_CD, iREQ_TASK_CD_1, iREQ_TASK_NAME_1, iREQ_TASK_DESC_1,
            iREQ_INSTRUCTIONS, null, iAPPLICABILITY_RANGE, iSCHED_TO_LATEST_DEADLINE_BOOL_N,
            iSOFT_DEADLINE_BOOL_N, null, iMUST_BE_REMOVED_CD, iETOPS_SIGNIFICANT_BOOL_N, "1",
            iEST_DURATION_HRS );

      verifyTaskTask_15067( iTASK_IDs_15067, iCLASS_CD_COMP, null, iSUBCLASS_CD_COMP,
            iORIGINATOR_CD, iREQ_TASK_CD_1, iREQ_TASK_NAME_1, iREQ_TASK_DESC_1, iREQ_INSTRUCTIONS,
            null, iAPPLICABILITY_RANGE, iSCHED_TO_LATEST_DEADLINE_BOOL_N, iSOFT_DEADLINE_BOOL_N,
            null, iMUST_BE_REMOVED_CD, iETOPS_SIGNIFICANT_BOOL_N, "1", iEST_DURATION_HRS, "0" );

      // verify task_DEFN
      String lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTASK_DEFN_IDs_15067.getNO_DB_ID() + " and TASK_DEFN_ID="
            + iTASK_DEFN_IDs_15067.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_task_flags
      lQuery = "select 1 from " + TableUtil.TASK_TASK_FLAGS + " where TASK_DB_ID="
            + iTASK_IDs_15067.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_15067.getNO_ID();
      Assert.assertTrue( "Check task_task_flags table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_part_map table
      simpleIDs lPNIDs_TRK = getPartNoIds( iPART_NO_OEM_TRK, iMANUFACT_CD_TRK );

      lQuery = "select 1 from " + TableUtil.TASK_PART_MAP + " where TASK_DB_ID="
            + iTASK_IDs_15067.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_15067.getNO_ID()
            + " and PART_NO_DB_ID=" + lPNIDs_TRK.getNO_DB_ID() + " and PART_NO_ID="
            + lPNIDs_TRK.getNO_ID();
      Assert.assertTrue( "Check task_part_map  table to verify the TRK record exists",
            RecordsExist( lQuery ) );

      // verify task_task_dep
      verifyTaskTaskDepNONExist( iTASK_IDs_15067, "CRT" );

   }


   /**
    * This test is to verify OPER-11705 fix.Baseline loader - cannot load Part-Based Requirements if
    * the part has multiple manufacture codes.
    *
    *
    */

   @Test
   public void testOPER_11705_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_COMP_REQ
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_1 + "'" );
      lReqMap.put( "REQ_TASK_DESC", "'" + iREQ_TASK_DESC_1 + "'" );
      lReqMap.put( "EXT_REFERENCE", "'" + iREQ_TASK_REF_SDESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD_COMP + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD_COMP + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "ETOPS_SIGNIFICANT_BOOL", "'" + iETOPS_SIGNIFICANT_BOOL + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "APPLICABILITY_RANGE", "'" + iAPPLICABILITY_RANGE + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MIN_USAGE_RELEASE_PCT", "'" + iMIN_USAGE_RELEASE_PCT + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE_QT", "'" + iMIN_FORECAST_RANGE_QT + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE_QT", "'" + iMIN_FORECAST_RANGE_QT + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL_2 + "'" );
      lReqMap.put( "EST_DURATION_HRS", "'" + iEST_DURATION_HRS + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "ENFORCE_WORKSCOPE_ORD_BOOL", "'" + iENFORCE_WORKSCOPE_ORD_BOOL + "'" );
      lReqMap.put( "REQ_INSTRUCTIONS", "'" + iREQ_INSTRUCTIONS + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ, lReqMap ) );

      // C_COMP_REQ_ASSIGNED_PART
      // first record
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'A0000007'" );
      lReqMap.put( "MANUFACT_CD", "'ABC11'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_ASSIGNED_PART, lReqMap ) );
      // second record
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_SER_COMP + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_SER_COMP + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_ASSIGNED_PART, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );
   }


   /**
    * This test is to verify OPER-30663: Baseline Loader - Job Card using Class with RSTAT_CD !=0
    * does not get failed by validation
    *
    *
    */

   @Test
   public void testCFG_COMP_REQ_12225_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_COMP_REQ
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_1 + "'" );
      lReqMap.put( "REQ_TASK_DESC", "'" + iREQ_TASK_DESC_1 + "'" );
      lReqMap.put( "EXT_REFERENCE", "'" + iREQ_TASK_REF_SDESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'AUTOREQ'" );
      lReqMap.put( "SUBCLASS_CD", "'TEST7'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "ETOPS_SIGNIFICANT_BOOL", "'" + iETOPS_SIGNIFICANT_BOOL + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "APPLICABILITY_RANGE", "'" + iAPPLICABILITY_RANGE + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MIN_USAGE_RELEASE_PCT", "'" + iMIN_USAGE_RELEASE_PCT + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE_QT", "'" + iMIN_FORECAST_RANGE_QT + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE_QT", "'" + iMIN_FORECAST_RANGE_QT + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL + "'" );
      lReqMap.put( "EST_DURATION_HRS", "'" + iEST_DURATION_HRS + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "REQ_INSTRUCTIONS", "'" + iREQ_INSTRUCTIONS + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ, lReqMap ) );

      // C_COMP_REQ_ASSIGNED_PART
      // first record
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_ASSIGNED_PART, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkCOMPREQErrorCode( testName.getMethodName(), "CFG_COMP_REQ-12225" );

   }


   /**
    * This test is to verify CFG_COMP_REQ-10025: C_COMP_REQ.RECURRING_TASK_BOOL cannot be NULL or
    * consist entirely of spaces.
    *
    */

   @Test
   public void testCFG_COMP_REQ_10025_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_COMP_REQ
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_1 + "'" );
      lReqMap.put( "REQ_TASK_DESC", "'" + iREQ_TASK_DESC_1 + "'" );
      lReqMap.put( "EXT_REFERENCE", "'" + iREQ_TASK_REF_SDESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD_COMP + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD_COMP + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "ETOPS_SIGNIFICANT_BOOL", "'" + iETOPS_SIGNIFICANT_BOOL + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "APPLICABILITY_RANGE", "'" + iAPPLICABILITY_RANGE + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MIN_USAGE_RELEASE_PCT", "'" + iMIN_USAGE_RELEASE_PCT + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE_QT", "'" + iMIN_FORECAST_RANGE_QT + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE_QT", "'" + iMIN_FORECAST_RANGE_QT + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL + "'" );
      lReqMap.put( "EST_DURATION_HRS", "'" + iEST_DURATION_HRS + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "REQ_INSTRUCTIONS", "'" + iREQ_INSTRUCTIONS + "'" );
      // lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ, lReqMap ) );

      // C_COMP_REQ_ASSIGNED_PART
      // first record
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_ASSIGNED_PART, lReqMap ) );
      // second record
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_SER + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_SER + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_ASSIGNED_PART, lReqMap ) );

      // C_COMP_REQ_DYNAMIC_DEADLINE
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_THRESHOLD_QT", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_DYNAMIC_DEADLINE, lReqMap ) );

      // C_COMP_REQ_DYN_PART_DEADLINE
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_THRESHOLD_QT", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_DYN_PART_DEADLINE, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkCOMPREQErrorCode( testName.getMethodName(), "CFG_COMP_REQ-10025" );

   }


   /**
    * This test is to verify CFG_COMP_REQ-12075: C_REQ.RECURRING_TASK_BOOL must be Y (T,1) or N(F,0)
    *
    */

   @Test
   public void testCFG_COMP_REQ_12075_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_COMP_REQ
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_1 + "'" );
      lReqMap.put( "REQ_TASK_DESC", "'" + iREQ_TASK_DESC_1 + "'" );
      lReqMap.put( "EXT_REFERENCE", "'" + iREQ_TASK_REF_SDESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD_COMP + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD_COMP + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "ETOPS_SIGNIFICANT_BOOL", "'" + iETOPS_SIGNIFICANT_BOOL + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "APPLICABILITY_RANGE", "'" + iAPPLICABILITY_RANGE + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MIN_USAGE_RELEASE_PCT", "'" + iMIN_USAGE_RELEASE_PCT + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE_QT", "'" + iMIN_FORECAST_RANGE_QT + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE_QT", "'" + iMIN_FORECAST_RANGE_QT + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL + "'" );
      lReqMap.put( "EST_DURATION_HRS", "'" + iEST_DURATION_HRS + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "REQ_INSTRUCTIONS", "'" + iREQ_INSTRUCTIONS + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'A'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ, lReqMap ) );

      // C_COMP_REQ_ASSIGNED_PART
      // first record
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_ASSIGNED_PART, lReqMap ) );
      // second record
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_SER + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_SER + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_ASSIGNED_PART, lReqMap ) );

      // C_COMP_REQ_DYNAMIC_DEADLINE
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_THRESHOLD_QT", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_DYNAMIC_DEADLINE, lReqMap ) );

      // C_COMP_REQ_DYN_PART_DEADLINE
      lReqMap.clear();
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_THRESHOLD_QT", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_DYN_PART_DEADLINE, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkCOMPREQErrorCode( testName.getMethodName(), "CFG_COMP_REQ-12075" );

   }


   /**
    *
    * To test OPER-30664 - this fix will correct the order of IETM assigned to particular task. It
    * will re-assign a sequential number during import.
    *
    * @throws Exception
    */
   @Test
   public void testREQ_COMP_IETM_OPER_30664_VALIDATION() throws Exception {

      Map<String, String> lc_REQMap = new LinkedHashMap<>();

      lc_REQMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lc_REQMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_1 + "'" );
      lc_REQMap.put( "REQ_TASK_DESC", "'" + iREQ_TASK_DESC_1 + "'" );
      lc_REQMap.put( "CLASS_CD", "'" + iCLASS_CD_COMP + "'" );
      lc_REQMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD_COMP + "'" );
      lc_REQMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lc_REQMap.put( "ETOPS_SIGNIFICANT_BOOL", "'" + iETOPS_SIGNIFICANT_BOOL + "'" );
      lc_REQMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lc_REQMap.put( "APPLICABILITY_RANGE", "'" + iAPPLICABILITY_RANGE + "'" );
      lc_REQMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lc_REQMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lc_REQMap.put( "MIN_USAGE_RELEASE_PCT", "'" + iMIN_USAGE_RELEASE_PCT + "'" );
      lc_REQMap.put( "MIN_FORECAST_RANGE_QT", "'" + iMIN_FORECAST_RANGE_QT + "'" );
      lc_REQMap.put( "MIN_FORECAST_RANGE_QT", "'" + iMIN_FORECAST_RANGE_QT + "'" );
      lc_REQMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL + "'" );
      lc_REQMap.put( "EST_DURATION_HRS", "'" + iEST_DURATION_HRS + "'" );
      lc_REQMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lc_REQMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lc_REQMap.put( "REQ_INSTRUCTIONS", "'" + iREQ_INSTRUCTIONS + "'" );
      lc_REQMap.put( "RECURRING_TASK_BOOL", "'N'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ, lc_REQMap ) );

      lc_REQMap.clear();
      lc_REQMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lc_REQMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK_COMP + "'" );
      lc_REQMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK_COMP + "'" );
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_ASSIGNED_PART, lc_REQMap ) );

      // C_REQ_IETM_TOPIC #1
      lc_REQMap.clear();
      lc_REQMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lc_REQMap.put( "IETM_CD", "'" + iIETM_CD_1 + "'" );
      lc_REQMap.put( "IETM_TOPIC", "'" + iIETM_TOPIC_1 + "'" );
      lc_REQMap.put( "IETM_ORDER", "'" + iIETM_ORDER_1 + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_IETM, lc_REQMap ) );

      // C_REQ_IETM_TOPIC #2
      lc_REQMap.clear();
      lc_REQMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lc_REQMap.put( "IETM_CD", "'" + iIETM_CD_2 + "'" );
      lc_REQMap.put( "IETM_TOPIC", "'" + iIETM_TOPIC_2 + "'" );
      lc_REQMap.put( "IETM_ORDER", "'" + iIETM_ORDER_2 + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_IETM, lc_REQMap ) );

      // C_REQ_IETM_TOPIC #3
      lc_REQMap.clear();
      lc_REQMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lc_REQMap.put( "IETM_CD", "'" + iIETM_CD_3 + "'" );
      lc_REQMap.put( "IETM_TOPIC", "'" + iIETM_TOPIC_3 + "'" );
      lc_REQMap.put( "IETM_ORDER", "'" + iIETM_ORDER_3 + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_IETM, lc_REQMap ) );

      // C_REQ_IETM_TOPIC #4
      lc_REQMap.clear();
      lc_REQMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lc_REQMap.put( "IETM_CD", "'" + iIETM_CD_4 + "'" );
      lc_REQMap.put( "IETM_TOPIC", "'" + iIETM_TOPIC_4 + "'" );
      lc_REQMap.put( "IETM_ORDER", "'" + iIETM_ORDER_4 + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REQ_IETM, lc_REQMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    *
    * To test OPER-30664 - this fix will correct the order of IETM assigned to particular task. It
    * will re-assign a sequential number during import.
    *
    * @throws Exception
    */
   @Test
   public void testREQ_COMP_IETM_OPER_30664_IMPORT() throws Exception {

      System.out.println( "=======Starting: " + testName.getMethodName()
            + " Validation========================" );

      testREQ_COMP_IETM_OPER_30664_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs = null;

      verifyIETMs( iREQ_TASK_CD_1, iIETM_CD_1, 1 );
      verifyIETMs( iREQ_TASK_CD_1, iIETM_CD_2, 2 );
      verifyIETMs( iREQ_TASK_CD_1, iIETM_CD_3, 4 );
      verifyIETMs( iREQ_TASK_CD_1, iIETM_CD_4, 3 );
   }


   // ==================================================================

   /**
    * Verify the IETM order number.
    *
    * @param iREQ_TASK_CD_1
    *           - Task it is assigned to
    * @param iREQ_IETM_CD_1
    *           - IETM it is assigned to
    * @param aIETM_ord
    *           - expected number assigned to the particular IETM
    */
   private void verifyIETMs( String aREQ_TASK_CD_1, String aREQ_IETM_CD_1, int aIETM_ord ) {
      String lQuery;
      simpleIDs lIetm_IDs = null;

      if ( iTASK_IDs == null ) {
         lQuery =
               "select task_db_id, task_id from task_task where task_cd = '" + aREQ_TASK_CD_1 + "'";
         iTASK_IDs = getIDs( lQuery, "TASK_DB_ID", "TASK_ID" );
      }
      lQuery = "select ietm_db_id, ietm_id from ietm_ietm where ietm_ietm.ietm_cd = '"
            + aREQ_IETM_CD_1 + "'";
      lIetm_IDs = getIDs( lQuery, "IETM_DB_ID", "IETM_ID" );

      lQuery = "select ietm_ord from task_task_ietm where  task_db_id = '" + iTASK_IDs.getNO_DB_ID()
            + "' and task_id = '" + iTASK_IDs.getNO_ID() + "' and ietm_db_id = '"
            + lIetm_IDs.getNO_DB_ID() + "' and ietm_id = '" + lIetm_IDs.getNO_ID() + "'";
      Assert.assertTrue( "IETM order value is incorrect.",
            getIntValueFromQuery( lQuery, "IETM_ORD" ) == aIETM_ord );
   }


   /**
    * This function is called by after test method to restore data which has been modified by tests.
    *
    *
    */

   public void RestoreData() {

      String lStrDelete = null;

      if ( iTASK_IDs_15067 != null ) {

         // delete task_part_map
         lStrDelete = "delete from " + TableUtil.TASK_PART_MAP + "  where TASK_DB_ID="
               + iTASK_IDs_15067.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_15067.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_task_flags
         lStrDelete = "delete from " + TableUtil.TASK_TASK_FLAGS + "  where TASK_DB_ID="
               + iTASK_IDs_15067.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_15067.getNO_ID();
         executeSQL( lStrDelete );

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

      if ( iTASK_IDs != null ) {

         // delete task_impact
         lStrDelete = "delete from " + TableUtil.TASK_IMPACT + "  where TASK_DB_ID="
               + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_advisory
         lStrDelete = "delete from " + TableUtil.TASK_ADVISORY + "  where TASK_DB_ID="
               + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_task_ietm
         lStrDelete = "delete from " + TableUtil.TASK_TASK_IETM + "  where TASK_DB_ID="
               + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_part_map
         lStrDelete = "delete from " + TableUtil.TASK_PART_MAP + "  where TASK_DB_ID="
               + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_task_flags
         lStrDelete = "delete from " + TableUtil.TASK_TASK_FLAGS + "  where TASK_DB_ID="
               + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_sched_rule
         lStrDelete = "delete from " + TableUtil.TASK_SCHED_RULE + "  where TASK_DB_ID="
               + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_task_dep
         lStrDelete = "delete from " + TableUtil.TASK_TASK_DEP + "  where TASK_DB_ID="
               + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_interval
         lStrDelete = "delete from " + TableUtil.TASK_INTERVAL + "  where TASK_DB_ID="
               + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_task
         lStrDelete = "delete from " + TableUtil.TASK_TASK + "  where TASK_DB_ID="
               + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID();
         executeSQL( lStrDelete );

      }

      if ( iTASK_DEFN_IDs != null ) {
         // delete task_jic_req_map
         lStrDelete = "delete from " + TableUtil.TASK_JIC_REQ_MAP + "  where REQ_TASK_DEFN_DB_ID="
               + iTASK_DEFN_IDs.getNO_DB_ID() + " and REQ_TASK_DEFN_ID="
               + iTASK_DEFN_IDs.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_defn table
         lStrDelete = "delete from TASK_DEFN where TASK_DEFN_DB_ID=" + iTASK_DEFN_IDs.getNO_DB_ID()
               + " and TASK_DEFN_ID=" + iTASK_DEFN_IDs.getNO_ID();
         executeSQL( lStrDelete );

      }

      if ( iTASK_IDs_2 != null ) {

         // delete task_impact
         lStrDelete = "delete from " + TableUtil.TASK_IMPACT + "  where TASK_DB_ID="
               + iTASK_IDs_2.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_2.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_advisory
         lStrDelete = "delete from " + TableUtil.TASK_ADVISORY + "  where TASK_DB_ID="
               + iTASK_IDs_2.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_2.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_task_ietm
         lStrDelete = "delete from " + TableUtil.TASK_TASK_IETM + "  where TASK_DB_ID="
               + iTASK_IDs_2.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_2.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_part_map
         lStrDelete = "delete from " + TableUtil.TASK_PART_MAP + "  where TASK_DB_ID="
               + iTASK_IDs_2.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_2.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_task_flags
         lStrDelete = "delete from " + TableUtil.TASK_TASK_FLAGS + "  where TASK_DB_ID="
               + iTASK_IDs_2.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_2.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_sched_rule
         lStrDelete = "delete from " + TableUtil.TASK_SCHED_RULE + "  where TASK_DB_ID="
               + iTASK_IDs_2.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_2.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_task_dep
         lStrDelete = "delete from " + TableUtil.TASK_TASK_DEP + "  where TASK_DB_ID="
               + iTASK_IDs_2.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_2.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_interval
         lStrDelete = "delete from " + TableUtil.TASK_INTERVAL + "  where TASK_DB_ID="
               + iTASK_IDs_2.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_2.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_task
         lStrDelete = "delete from " + TableUtil.TASK_TASK + "  where TASK_DB_ID="
               + iTASK_IDs_2.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_2.getNO_ID();
         executeSQL( lStrDelete );

      }

      if ( iTASK_DEFN_IDs_2 != null ) {
         // delete task_jic_req_map
         lStrDelete = "delete from " + TableUtil.TASK_JIC_REQ_MAP + "  where REQ_TASK_DEFN_DB_ID="
               + iTASK_DEFN_IDs_2.getNO_DB_ID() + " and REQ_TASK_DEFN_ID="
               + iTASK_DEFN_IDs_2.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_defn table
         lStrDelete =
               "delete from TASK_DEFN where TASK_DEFN_DB_ID=" + iTASK_DEFN_IDs_2.getNO_DB_ID()
                     + " and TASK_DEFN_ID=" + iTASK_DEFN_IDs_2.getNO_ID();
         executeSQL( lStrDelete );

      }

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

               lPrepareCallJICPART = getConnection().prepareCall(
                     "BEGIN  c_comp_req_import.validate_comp_req(on_retcode =>?); END;" );

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

               lPrepareCallKIT = getConnection().prepareCall(
                     "BEGIN c_comp_req_import.import_comp_req(on_retcode =>?); END;" );

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


   /**
    * This function is to run export
    *
    * @return: return code of Int
    *
    */
   public int runExport() throws SQLException {
      int lReturn = 0;
      CallableStatement lPrepareCallJICPART;

      lPrepareCallJICPART = getConnection().prepareCall(
            "BEGIN  c_comp_req_import.export_comp_req(aib_autodel_stg_data => true, aon_retcode =>?, aov_retmsg =>?); END;" );

      lPrepareCallJICPART.registerOutParameter( 1, Types.INTEGER );
      lPrepareCallJICPART.registerOutParameter( 2, Types.CHAR );
      lPrepareCallJICPART.execute();
      commit();
      lReturn = lPrepareCallJICPART.getInt( 1 );

      return lReturn;

   }

}
