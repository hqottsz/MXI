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
import com.mxi.mx.util.RefDocAndRefDocComponent;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.ValidationAndImport;


/**
 * This test suite contains test cases on validation and import functionality of
 * C_COMP_REF_DOC_IMPORT package.
 *
 * @author ALICIA QIAN
 */
public class CompRefDoc extends RefDocAndRefDocComponent {

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
    * This test is to verify OPER-30663:Baseline Loader - Job Card using Class with RSTAT_CD !=0
    * does not get failed by validation
    *
    *
    *
    */
   @Test
   public void testCFG_COMP_REF_12225_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_COMP_REF
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "REF_TASK_NAME", "'" + iREF_TASK_NAME_1 + "'" );
      lReqMap.put( "REF_TASK_DESC", "'" + iREF_TASK_DESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'AUTOREF'" );
      lReqMap.put( "SUBCLASS_CD", "'TEST6'" );

      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "RECEIVED_BY", "'" + iRECEIVED_BY + "'" );
      lReqMap.put( "ISSUED_BY_MANUFACT_CD", "'" + iISSUED_BY_MANUFACT_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_BOOL", "'" + iSCHED_FROM_MANUFACT_BOOL + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MANUAL_SCHEDULING_BOOL", "'" + iMANUAL_SCHEDULING_BOOL + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE", "'" + iMIN_FORECAST_RANGE + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "RECEIVED_DT", "SYSDATE" );
      lReqMap.put( "ISSUE_DT", "SYSDATE" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'Y'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REF, lReqMap ) );

      // C_COMP_REF_ASSIGNED_PART
      lReqMap.clear();
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REF_ASSIGNED_PART, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkCompRefErrorCode( testName.getMethodName(), "CFG_COMP_REF-12225" );

   }


   /**
    * This test is to verify CFG_COMP_REF-10025:C_COMP_REF.RECURRING_TASK_BOOL cannot be NULL or
    * consist entirely of spaces.
    *
    *
    *
    */
   @Test
   public void testCFG_COMP_REF_10025_1_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_COMP_REF
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "REF_TASK_NAME", "'" + iREF_TASK_NAME_1 + "'" );
      lReqMap.put( "REF_TASK_DESC", "'" + iREF_TASK_DESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );

      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "RECEIVED_BY", "'" + iRECEIVED_BY + "'" );
      lReqMap.put( "ISSUED_BY_MANUFACT_CD", "'" + iISSUED_BY_MANUFACT_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_BOOL", "'" + iSCHED_FROM_MANUFACT_BOOL + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MANUAL_SCHEDULING_BOOL", "'" + iMANUAL_SCHEDULING_BOOL + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE", "'" + iMIN_FORECAST_RANGE + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "RECEIVED_DT", "SYSDATE" );
      lReqMap.put( "ISSUE_DT", "SYSDATE" );
      lReqMap.put( "RECURRING_TASK_BOOL", "' '" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REF, lReqMap ) );

      // C_COMP_REF_ASSIGNED_PART
      lReqMap.clear();
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REF_ASSIGNED_PART, lReqMap ) );

      // C_COMP_REF_TASK_LINK
      lReqMap.clear();
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "LINK_TYPE_CD", "'" + iLINK_TYPE_CD + "'" );
      lReqMap.put( "LINK_TASK_CD", "'" + iLINKED_TASK_CD_REFDOC + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REF_TASK_LINK, lReqMap ) );

      // C_REF_DYNAMIC_DEADLINE
      lReqMap.clear();
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_THRESHOLD_QT", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REF_DYNAMIC_DEADLINE, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkCompRefErrorCode( testName.getMethodName(), "CFG_COMP_REF-10025" );

   }


   /**
    * This test is to verify CFG_COMP_REF-10025:C_COMP_REF.RECURRING_TASK_BOOL cannot be NULL or
    * consist entirely of spaces.
    *
    *
    *
    */
   @Test
   public void testCFG_COMP_REF_10025_2_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_COMP_REF
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "REF_TASK_NAME", "'" + iREF_TASK_NAME_1 + "'" );
      lReqMap.put( "REF_TASK_DESC", "'" + iREF_TASK_DESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );

      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "RECEIVED_BY", "'" + iRECEIVED_BY + "'" );
      lReqMap.put( "ISSUED_BY_MANUFACT_CD", "'" + iISSUED_BY_MANUFACT_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_BOOL", "'" + iSCHED_FROM_MANUFACT_BOOL + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MANUAL_SCHEDULING_BOOL", "'" + iMANUAL_SCHEDULING_BOOL + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE", "'" + iMIN_FORECAST_RANGE + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "RECEIVED_DT", "SYSDATE" );
      lReqMap.put( "ISSUE_DT", "SYSDATE" );
      lReqMap.put( "RECURRING_TASK_BOOL", "''" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REF, lReqMap ) );

      // C_COMP_REF_ASSIGNED_PART
      lReqMap.clear();
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REF_ASSIGNED_PART, lReqMap ) );

      // C_COMP_REF_TASK_LINK
      lReqMap.clear();
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "LINK_TYPE_CD", "'" + iLINK_TYPE_CD + "'" );
      lReqMap.put( "LINK_TASK_CD", "'" + iLINKED_TASK_CD_REFDOC + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REF_TASK_LINK, lReqMap ) );

      // C_REF_DYNAMIC_DEADLINE
      lReqMap.clear();
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_THRESHOLD_QT", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REF_DYNAMIC_DEADLINE, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkCompRefErrorCode( testName.getMethodName(), "CFG_COMP_REF-10025" );

   }


   /**
    * This test is to verify CFG_COMP_REF-10025:C_COMP_REF.RECURRING_TASK_BOOL cannot be NULL or
    * consist entirely of spaces.
    *
    *
    *
    */
   @Test
   public void testCFG_COMP_REF_10025_3_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_COMP_REF
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "REF_TASK_NAME", "'" + iREF_TASK_NAME_1 + "'" );
      lReqMap.put( "REF_TASK_DESC", "'" + iREF_TASK_DESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );

      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "RECEIVED_BY", "'" + iRECEIVED_BY + "'" );
      lReqMap.put( "ISSUED_BY_MANUFACT_CD", "'" + iISSUED_BY_MANUFACT_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_BOOL", "'" + iSCHED_FROM_MANUFACT_BOOL + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MANUAL_SCHEDULING_BOOL", "'" + iMANUAL_SCHEDULING_BOOL + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE", "'" + iMIN_FORECAST_RANGE + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "RECEIVED_DT", "SYSDATE" );
      lReqMap.put( "ISSUE_DT", "SYSDATE" );
      lReqMap.put( "RECURRING_TASK_BOOL", null );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REF, lReqMap ) );

      // C_COMP_REF_ASSIGNED_PART
      lReqMap.clear();
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REF_ASSIGNED_PART, lReqMap ) );

      // C_COMP_REF_TASK_LINK
      lReqMap.clear();
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "LINK_TYPE_CD", "'" + iLINK_TYPE_CD + "'" );
      lReqMap.put( "LINK_TASK_CD", "'" + iLINKED_TASK_CD_REFDOC + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REF_TASK_LINK, lReqMap ) );

      // C_REF_DYNAMIC_DEADLINE
      lReqMap.clear();
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_THRESHOLD_QT", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REF_DYNAMIC_DEADLINE, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkCompRefErrorCode( testName.getMethodName(), "CFG_COMP_REF-10025" );

   }


   /**
    * This test is to verify CFG_COMP_REF-12075:C_COMP_REF.recurring_task_bool must be Y or N when
    * provided.
    *
    *
    *
    */
   @Test
   public void testCFG_COMP_REF_12075_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_COMP_REF
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "REF_TASK_NAME", "'" + iREF_TASK_NAME_1 + "'" );
      lReqMap.put( "REF_TASK_DESC", "'" + iREF_TASK_DESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );

      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "RECEIVED_BY", "'" + iRECEIVED_BY + "'" );
      lReqMap.put( "ISSUED_BY_MANUFACT_CD", "'" + iISSUED_BY_MANUFACT_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_BOOL", "'" + iSCHED_FROM_MANUFACT_BOOL + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MANUAL_SCHEDULING_BOOL", "'" + iMANUAL_SCHEDULING_BOOL + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE", "'" + iMIN_FORECAST_RANGE + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "RECEIVED_DT", "SYSDATE" );
      lReqMap.put( "ISSUE_DT", "SYSDATE" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'A'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REF, lReqMap ) );

      // C_COMP_REF_ASSIGNED_PART
      lReqMap.clear();
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REF_ASSIGNED_PART, lReqMap ) );

      // C_COMP_REF_TASK_LINK
      lReqMap.clear();
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "LINK_TYPE_CD", "'" + iLINK_TYPE_CD + "'" );
      lReqMap.put( "LINK_TASK_CD", "'" + iLINKED_TASK_CD_REFDOC + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REF_TASK_LINK, lReqMap ) );

      // C_REF_DYNAMIC_DEADLINE
      lReqMap.clear();
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_THRESHOLD_QT", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REF_DYNAMIC_DEADLINE, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkCompRefErrorCode( testName.getMethodName(), "CFG_COMP_REF-12075" );

   }


   /**
    * This test is to verify validation functionality on c_comp_ref_doc_import of non-recurring ref
    * doc on dynamic schedule.
    *
    *
    */
   @Test
   public void testNON_RECURRING_DYNAMIC_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_COMP_REF
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "REF_TASK_NAME", "'" + iREF_TASK_NAME_1 + "'" );
      lReqMap.put( "REF_TASK_DESC", "'" + iREF_TASK_DESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );

      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "RECEIVED_BY", "'" + iRECEIVED_BY + "'" );
      lReqMap.put( "ISSUED_BY_MANUFACT_CD", "'" + iISSUED_BY_MANUFACT_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_BOOL", "'" + iSCHED_FROM_MANUFACT_BOOL + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MANUAL_SCHEDULING_BOOL", "'" + iMANUAL_SCHEDULING_BOOL + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE", "'" + iMIN_FORECAST_RANGE + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "RECEIVED_DT", "SYSDATE" );
      lReqMap.put( "ISSUE_DT", "SYSDATE" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'0'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REF, lReqMap ) );

      // C_COMP_REF_ASSIGNED_PART
      lReqMap.clear();
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REF_ASSIGNED_PART, lReqMap ) );

      // C_COMP_REF_TASK_LINK
      lReqMap.clear();
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "LINK_TYPE_CD", "'" + iLINK_TYPE_CD + "'" );
      lReqMap.put( "LINK_TASK_CD", "'" + iLINKED_TASK_CD_REFDOC + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REF_TASK_LINK, lReqMap ) );

      // C_REF_DYNAMIC_DEADLINE
      lReqMap.clear();
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_THRESHOLD_QT", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REF_DYNAMIC_DEADLINE, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify import functionality on c_comp_ref_doc_import of non-recurring ref doc
    * on dynamic schedule.
    *
    *
    */
   @Test
   public void testNON_RECURRING_DYNAMIC_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testNON_RECURRING_DYNAMIC_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs = null;
      iTASK_DEFN_IDs = null;
      iTASK_IDs_2 = null;
      iTASK_DEFN_IDs_2 = null;

      // Verify task_task table
      iTASK_IDs = getTaskIds( iREF_TASK_CD_1, iREF_TASK_NAME_1 );
      // assmbleInfor lassmblinfor = getassmblInfor( iATA_CD_TRK );

      iTASK_DEFN_IDs = verifyTaskTask( iTASK_IDs, iCLASS_CD, null, iSUBCLASS_CD, iORIGINATOR_CD,
            iREF_TASK_CD_1, iREF_TASK_NAME_1, iREF_TASK_DESC_1, null,
            iSCHED_TO_LATEST_DEADLINE_BOOL_Y_number, iSOFT_DEADLINE_BOOL_Y_number,
            iMUST_BE_REMOVED_CD, iON_CONDITION_BOOL_N_number, "0" );

      // verify task_DEFN
      String lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTASK_DEFN_IDs.getNO_DB_ID() + " and TASK_DEFN_ID=" + iTASK_DEFN_IDs.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_task_flag
      lQuery = "select 1 from " + TableUtil.TASK_TASK_FLAGS + " where TASK_DB_ID="
            + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID();
      Assert.assertTrue( "Check task_task_flags table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_ref_doc table
      // get HR ids
      simpleIDs lHRIDs = getHRIds( iRECEIVED_BY );
      // get receive date and issue date
      lQuery = "SELECT SYSDATE as mydate FROM DUAL";
      java.sql.Date lRCVdate = getDateValueFromQuery( lQuery, "mydate" );
      java.sql.Date lIssuedate = getDateValueFromQuery( lQuery, "mydate" );
      verifyTASKREFDOC( iTASK_IDs, lHRIDs, iISSUED_BY_MANUFACT_CD, "MANUFACT", lRCVdate,
            lIssuedate );

      // verify task_task_dep
      simpleIDs iLink_TASK = getTaskIds( iLINKED_TASK_CD_REFDOC, iLINKED_TASK_NAME_REFDOC );
      verifyTASKTASkDEP( iLink_TASK, iTASK_DEFN_IDs, iLINK_TYPE_CD );

      // verify task_work_type
      verifyWorkType( iTASK_IDs, iWORK_TYPE_LIST_1 );
      verifyWorkType( iTASK_IDs, iWORK_TYPE_LIST_2 );

      // verify task_sched_rule table
      simpleIDs lTypeIds = getTypeIds( iSCHED_DATA_TYPE_CD );
      verifyTASKSCHEDRULE( iTASK_IDs, lTypeIds, iSCHED_THRESHOLD_QT, null );

   }


   /**
    * This test is to verify validation functionality on c_comp_ref_doc_import of non-recurring ref
    * doc on standard schedule.
    *
    *
    */

   public void testNON_RECURRING_STD_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_COMP_REF
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "REF_TASK_NAME", "'" + iREF_TASK_NAME_1 + "'" );
      lReqMap.put( "REF_TASK_DESC", "'" + iREF_TASK_DESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );

      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "RECEIVED_BY", "'" + iRECEIVED_BY + "'" );
      lReqMap.put( "ISSUED_BY_MANUFACT_CD", "'" + iISSUED_BY_MANUFACT_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_BOOL", "'" + iSCHED_FROM_MANUFACT_BOOL + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MANUAL_SCHEDULING_BOOL", "'" + iMANUAL_SCHEDULING_BOOL + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE", "'" + iMIN_FORECAST_RANGE + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "RECEIVED_DT", "SYSDATE" );
      lReqMap.put( "ISSUE_DT", "SYSDATE" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'F'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REF, lReqMap ) );

      // C_COMP_REF_ASSIGNED_PART
      lReqMap.clear();
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REF_ASSIGNED_PART, lReqMap ) );

      // C_COMP_REF_TASK_LINK
      lReqMap.clear();
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "LINK_TYPE_CD", "'" + iLINK_TYPE_CD + "'" );
      lReqMap.put( "LINK_TASK_CD", "'" + iLINKED_TASK_CD_REFDOC + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REF_TASK_LINK, lReqMap ) );

      // C_REF_STANDARD_DEADLINE
      lReqMap.clear();
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_CYC_THRESHOLD", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REF_STANDARD_DEADLINE, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify import functionality on c_comp_ref_doc_import of non-recurring ref doc
    * on standard schedule.
    *
    *
    */
   @Test
   public void testNON_RECURRING_STD_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testNON_RECURRING_STD_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs = null;
      iTASK_DEFN_IDs = null;
      iTASK_IDs_2 = null;
      iTASK_DEFN_IDs_2 = null;

      // Verify task_task table
      iTASK_IDs = getTaskIds( iREF_TASK_CD_1, iREF_TASK_NAME_1 );
      // assmbleInfor lassmblinfor = getassmblInfor( iATA_CD_TRK );

      iTASK_DEFN_IDs = verifyTaskTask( iTASK_IDs, iCLASS_CD, null, iSUBCLASS_CD, iORIGINATOR_CD,
            iREF_TASK_CD_1, iREF_TASK_NAME_1, iREF_TASK_DESC_1, null,
            iSCHED_TO_LATEST_DEADLINE_BOOL_Y_number, iSOFT_DEADLINE_BOOL_Y_number,
            iMUST_BE_REMOVED_CD, iON_CONDITION_BOOL_N_number, "0" );

      // verify task_DEFN
      String lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTASK_DEFN_IDs.getNO_DB_ID() + " and TASK_DEFN_ID=" + iTASK_DEFN_IDs.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_task_flag
      lQuery = "select 1 from " + TableUtil.TASK_TASK_FLAGS + " where TASK_DB_ID="
            + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID();
      Assert.assertTrue( "Check task_task_flags table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_ref_doc table
      // get HR ids
      simpleIDs lHRIDs = getHRIds( iRECEIVED_BY );
      // get receive date and issue date
      lQuery = "SELECT SYSDATE as mydate FROM DUAL";
      java.sql.Date lRCVdate = getDateValueFromQuery( lQuery, "mydate" );
      java.sql.Date lIssuedate = getDateValueFromQuery( lQuery, "mydate" );
      verifyTASKREFDOC( iTASK_IDs, lHRIDs, iISSUED_BY_MANUFACT_CD, "MANUFACT", lRCVdate,
            lIssuedate );

      // verify task_task_dep
      simpleIDs iLink_TASK = getTaskIds( iLINKED_TASK_CD_REFDOC, iLINKED_TASK_NAME_REFDOC );
      verifyTASKTASkDEP( iLink_TASK, iTASK_DEFN_IDs, iLINK_TYPE_CD );

      // verify task_work_type
      verifyWorkType( iTASK_IDs, iWORK_TYPE_LIST_1 );
      verifyWorkType( iTASK_IDs, iWORK_TYPE_LIST_2 );

      // verify task_sched_rule table
      simpleIDs lTypeIds = getTypeIds( iSCHED_DATA_TYPE_CD );
      verifyTASKSCHEDRULE( iTASK_IDs, lTypeIds, iSCHED_THRESHOLD_QT, null );

   }


   /**
    * This test is to verify validation functionality on c_comp_ref_doc_import of recurring ref doc
    * on dynamic schedule.
    *
    *
    */

   public void test_RECURRING_DYNAMIC_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_COMP_REF
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_2 + "'" );
      lReqMap.put( "REF_TASK_NAME", "'" + iREF_TASK_NAME_2 + "'" );
      lReqMap.put( "REF_TASK_DESC", "'" + iREF_TASK_DESC_2 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );

      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "RECEIVED_BY", "'" + iRECEIVED_BY + "'" );
      lReqMap.put( "ISSUED_BY_MANUFACT_CD", "'" + iISSUED_BY_MANUFACT_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_BOOL", "'" + iSCHED_FROM_MANUFACT_BOOL + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "RESCHED_FROM_CD", "'" + iRESCHED_FROM_CD + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MANUAL_SCHEDULING_BOOL", "'" + iMANUAL_SCHEDULING_BOOL + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE", "'" + iMIN_FORECAST_RANGE + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "RECEIVED_DT", "SYSDATE" );
      lReqMap.put( "ISSUE_DT", "SYSDATE" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'Y'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REF, lReqMap ) );

      // C_COMP_REF_ASSIGNED_PART
      lReqMap.clear();
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_2 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REF_ASSIGNED_PART, lReqMap ) );

      // C_COMP_REF_TASK_LINK
      lReqMap.clear();
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_2 + "'" );
      lReqMap.put( "LINK_TYPE_CD", "'" + iLINK_TYPE_CD + "'" );
      lReqMap.put( "LINK_TASK_CD", "'" + iLINKED_TASK_CD_REFDOC + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REF_TASK_LINK, lReqMap ) );

      // C_REF_DYNAMIC_DEADLINE
      lReqMap.clear();
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_2 + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_INTERVAL_QT", "'" + iSCHED_INTERVAL_QT + "'" );
      // lReqMap.put( "SCHED_THRESHOLD_QT", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REF_DYNAMIC_DEADLINE, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify import functionality on c_comp_ref_doc_import of recurring ref doc on
    * dynamic schedule.
    *
    *
    */
   @Test
   public void test_RECURRING_DYNAMIC_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      test_RECURRING_DYNAMIC_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs = null;
      iTASK_DEFN_IDs = null;
      iTASK_IDs_2 = null;
      iTASK_DEFN_IDs_2 = null;

      // Verify task_task table
      iTASK_IDs = getTaskIds( iREF_TASK_CD_2, iREF_TASK_NAME_2 );
      // assmbleInfor lassmblinfor = getassmblInfor( iATA_CD_TRK );

      iTASK_DEFN_IDs = verifyTaskTask( iTASK_IDs, iCLASS_CD, null, iSUBCLASS_CD, iORIGINATOR_CD,
            iREF_TASK_CD_2, iREF_TASK_NAME_2, iREF_TASK_DESC_2, null,
            iSCHED_TO_LATEST_DEADLINE_BOOL_Y_number, iSOFT_DEADLINE_BOOL_Y_number,
            iMUST_BE_REMOVED_CD, iON_CONDITION_BOOL_N_number, "1" );

      // verify task_DEFN
      String lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTASK_DEFN_IDs.getNO_DB_ID() + " and TASK_DEFN_ID=" + iTASK_DEFN_IDs.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_task_flag
      lQuery = "select 1 from " + TableUtil.TASK_TASK_FLAGS + " where TASK_DB_ID="
            + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID();
      Assert.assertTrue( "Check task_task_flags table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_ref_doc table
      // get HR ids
      simpleIDs lHRIDs = getHRIds( iRECEIVED_BY );
      // get receive date and issue date
      lQuery = "SELECT SYSDATE as mydate FROM DUAL";
      java.sql.Date lRCVdate = getDateValueFromQuery( lQuery, "mydate" );
      java.sql.Date lIssuedate = getDateValueFromQuery( lQuery, "mydate" );
      verifyTASKREFDOC( iTASK_IDs, lHRIDs, iISSUED_BY_MANUFACT_CD, "MANUFACT", lRCVdate,
            lIssuedate );

      // verify task_task_dep
      simpleIDs iLink_TASK = getTaskIds( iLINKED_TASK_CD_REFDOC, iLINKED_TASK_NAME_REFDOC );
      verifyTASKTASkDEP( iLink_TASK, iTASK_DEFN_IDs, iLINK_TYPE_CD );

      // verify task_work_type
      verifyWorkType( iTASK_IDs, iWORK_TYPE_LIST_1 );
      verifyWorkType( iTASK_IDs, iWORK_TYPE_LIST_2 );

      // verify task_sched_rule table
      simpleIDs lTypeIds = getTypeIds( iSCHED_DATA_TYPE_CD );
      verifyTASKSCHEDRULE( iTASK_IDs, lTypeIds, iSCHED_INTERVAL_QT, null );

   }


   /**
    * This test is to verify validation functionality on c_comp_ref_doc_import of recurring ref doc
    * on STD schedule.
    *
    *
    */

   public void test_RECURRING_STD_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_COMP_REF
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_2 + "'" );
      lReqMap.put( "REF_TASK_NAME", "'" + iREF_TASK_NAME_2 + "'" );
      lReqMap.put( "REF_TASK_DESC", "'" + iREF_TASK_DESC_2 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );

      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "RECEIVED_BY", "'" + iRECEIVED_BY + "'" );
      lReqMap.put( "ISSUED_BY_MANUFACT_CD", "'" + iISSUED_BY_MANUFACT_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_BOOL", "'" + iSCHED_FROM_MANUFACT_BOOL + "'" );
      lReqMap.put( "SCHED_TO_LATEST_DEADLINE_BOOL", "'" + iSCHED_TO_LATEST_DEADLINE_BOOL + "'" );
      lReqMap.put( "RESCHED_FROM_CD", "'" + iRESCHED_FROM_CD + "'" );
      lReqMap.put( "SOFT_DEADLINE_BOOL", "'" + iSOFT_DEADLINE_BOOL + "'" );
      lReqMap.put( "MUST_BE_REMOVED_CD", "'" + iMUST_BE_REMOVED_CD + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'" + iNEXT_SHOP_VISIT_BOOL + "'" );
      lReqMap.put( "MANUAL_SCHEDULING_BOOL", "'" + iMANUAL_SCHEDULING_BOOL + "'" );
      lReqMap.put( "MIN_FORECAST_RANGE", "'" + iMIN_FORECAST_RANGE + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "RECEIVED_DT", "SYSDATE" );
      lReqMap.put( "ISSUE_DT", "SYSDATE" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REF, lReqMap ) );

      // C_COMP_REF_ASSIGNED_PART
      lReqMap.clear();
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_2 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REF_ASSIGNED_PART, lReqMap ) );

      // C_COMP_REF_TASK_LINK
      lReqMap.clear();
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_2 + "'" );
      lReqMap.put( "LINK_TYPE_CD", "'" + iLINK_TYPE_CD + "'" );
      lReqMap.put( "LINK_TASK_CD", "'" + iLINKED_TASK_CD_REFDOC + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REF_TASK_LINK, lReqMap ) );

      // C_REF_STANDARD_DEADLINE
      lReqMap.clear();
      lReqMap.put( "REF_TASK_CD", "'" + iREF_TASK_CD_2 + "'" );
      lReqMap.put( "SCHED_CYC_INTERVAL", "'" + iSCHED_INTERVAL_QT + "'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_COMP_REF_STANDARD_DEADLINE, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify import functionality on c_comp_ref_doc_import of recurring ref doc on
    * STD schedule.
    *
    *
    */
   @Test
   public void test_RECURRING_STD_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      test_RECURRING_STD_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs = null;
      iTASK_DEFN_IDs = null;
      iTASK_IDs_2 = null;
      iTASK_DEFN_IDs_2 = null;

      // Verify task_task table
      iTASK_IDs = getTaskIds( iREF_TASK_CD_2, iREF_TASK_NAME_2 );
      // assmbleInfor lassmblinfor = getassmblInfor( iATA_CD_TRK );

      iTASK_DEFN_IDs = verifyTaskTask( iTASK_IDs, iCLASS_CD, null, iSUBCLASS_CD, iORIGINATOR_CD,
            iREF_TASK_CD_2, iREF_TASK_NAME_2, iREF_TASK_DESC_2, null,
            iSCHED_TO_LATEST_DEADLINE_BOOL_Y_number, iSOFT_DEADLINE_BOOL_Y_number,
            iMUST_BE_REMOVED_CD, iON_CONDITION_BOOL_N_number, "1" );

      // verify task_DEFN
      String lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTASK_DEFN_IDs.getNO_DB_ID() + " and TASK_DEFN_ID=" + iTASK_DEFN_IDs.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_task_flag
      lQuery = "select 1 from " + TableUtil.TASK_TASK_FLAGS + " where TASK_DB_ID="
            + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID();
      Assert.assertTrue( "Check task_task_flags table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_ref_doc table
      // get HR ids
      simpleIDs lHRIDs = getHRIds( iRECEIVED_BY );
      // get receive date and issue date
      lQuery = "SELECT SYSDATE as mydate FROM DUAL";
      java.sql.Date lRCVdate = getDateValueFromQuery( lQuery, "mydate" );
      java.sql.Date lIssuedate = getDateValueFromQuery( lQuery, "mydate" );
      verifyTASKREFDOC( iTASK_IDs, lHRIDs, iISSUED_BY_MANUFACT_CD, "MANUFACT", lRCVdate,
            lIssuedate );

      // verify task_task_dep
      simpleIDs iLink_TASK = getTaskIds( iLINKED_TASK_CD_REFDOC, iLINKED_TASK_NAME_REFDOC );
      verifyTASKTASkDEP( iLink_TASK, iTASK_DEFN_IDs, iLINK_TYPE_CD );

      // verify task_work_type
      verifyWorkType( iTASK_IDs, iWORK_TYPE_LIST_1 );
      verifyWorkType( iTASK_IDs, iWORK_TYPE_LIST_2 );

      // verify task_sched_rule table
      simpleIDs lTypeIds = getTypeIds( iSCHED_DATA_TYPE_CD );
      verifyTASKSCHEDRULE( iTASK_IDs, lTypeIds, iSCHED_INTERVAL_QT, null );

   }


   /**
    * This test is to verify validation functionality on c_comp_ref_doc_import of recurring ref doc
    * on dyn schedule and non-recurring ref doc on std schedule.
    *
    *
    */

   public void test_Multiple_Records_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testNON_RECURRING_STD_VALIDATION();
      test_RECURRING_DYNAMIC_VALIDATION();

   }


   /**
    * This test is to verify import functionality on c_comp_ref_doc_import of recurring ref doc on
    * dyn schedule and non-recurring ref doc on std schedule.
    *
    *
    */

   @Test
   public void test_Multiple_Records_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      test_Multiple_Records_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs = null;
      iTASK_DEFN_IDs = null;
      iTASK_IDs_2 = null;
      iTASK_DEFN_IDs_2 = null;

      // verify non recurring std
      // Verify task_task table
      iTASK_IDs = getTaskIds( iREF_TASK_CD_1, iREF_TASK_NAME_1 );
      // assmbleInfor lassmblinfor = getassmblInfor( iATA_CD_TRK );

      iTASK_DEFN_IDs = verifyTaskTask( iTASK_IDs, iCLASS_CD, null, iSUBCLASS_CD, iORIGINATOR_CD,
            iREF_TASK_CD_1, iREF_TASK_NAME_1, iREF_TASK_DESC_1, null,
            iSCHED_TO_LATEST_DEADLINE_BOOL_Y_number, iSOFT_DEADLINE_BOOL_Y_number,
            iMUST_BE_REMOVED_CD, iON_CONDITION_BOOL_N_number, "0" );

      // verify task_DEFN
      String lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTASK_DEFN_IDs.getNO_DB_ID() + " and TASK_DEFN_ID=" + iTASK_DEFN_IDs.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_task_flag
      lQuery = "select 1 from " + TableUtil.TASK_TASK_FLAGS + " where TASK_DB_ID="
            + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID();
      Assert.assertTrue( "Check task_task_flags table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_ref_doc table
      // get HR ids
      simpleIDs lHRIDs = getHRIds( iRECEIVED_BY );
      // get receive date and issue date
      lQuery = "SELECT SYSDATE as mydate FROM DUAL";
      java.sql.Date lRCVdate = getDateValueFromQuery( lQuery, "mydate" );
      java.sql.Date lIssuedate = getDateValueFromQuery( lQuery, "mydate" );
      verifyTASKREFDOC( iTASK_IDs, lHRIDs, iISSUED_BY_MANUFACT_CD, "MANUFACT", lRCVdate,
            lIssuedate );

      // verify task_task_dep
      simpleIDs iLink_TASK = getTaskIds( iLINKED_TASK_CD_REFDOC, iLINKED_TASK_NAME_REFDOC );
      verifyTASKTASkDEP( iLink_TASK, iTASK_DEFN_IDs, iLINK_TYPE_CD );

      // verify task_work_type
      verifyWorkType( iTASK_IDs, iWORK_TYPE_LIST_1 );
      verifyWorkType( iTASK_IDs, iWORK_TYPE_LIST_2 );

      // verify task_sched_rule table
      simpleIDs lTypeIds = getTypeIds( iSCHED_DATA_TYPE_CD );
      verifyTASKSCHEDRULE( iTASK_IDs, lTypeIds, iSCHED_THRESHOLD_QT, null );

      // verify recurring
      // dyn==================================================================================
      // Verify task_task table
      iTASK_IDs_2 = getTaskIds( iREF_TASK_CD_2, iREF_TASK_NAME_2 );
      // assmbleInfor lassmblinfor = getassmblInfor( iATA_CD_TRK );

      iTASK_DEFN_IDs_2 = verifyTaskTask( iTASK_IDs_2, iCLASS_CD, null, iSUBCLASS_CD, iORIGINATOR_CD,
            iREF_TASK_CD_2, iREF_TASK_NAME_2, iREF_TASK_DESC_2, null,
            iSCHED_TO_LATEST_DEADLINE_BOOL_Y_number, iSOFT_DEADLINE_BOOL_Y_number,
            iMUST_BE_REMOVED_CD, iON_CONDITION_BOOL_N_number, "1" );

      // verify task_DEFN
      lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTASK_DEFN_IDs_2.getNO_DB_ID() + " and TASK_DEFN_ID=" + iTASK_DEFN_IDs_2.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_task_flag
      lQuery = "select 1 from " + TableUtil.TASK_TASK_FLAGS + " where TASK_DB_ID="
            + iTASK_IDs_2.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_2.getNO_ID();
      Assert.assertTrue( "Check task_task_flags table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_ref_doc table
      // get HR ids
      lHRIDs = getHRIds( iRECEIVED_BY );
      // get receive date and issue date
      lQuery = "SELECT SYSDATE as mydate FROM DUAL";
      lRCVdate = getDateValueFromQuery( lQuery, "mydate" );
      lIssuedate = getDateValueFromQuery( lQuery, "mydate" );
      verifyTASKREFDOC( iTASK_IDs_2, lHRIDs, iISSUED_BY_MANUFACT_CD, "MANUFACT", lRCVdate,
            lIssuedate );

      // verify task_task_dep
      iLink_TASK = getTaskIds( iLINKED_TASK_CD_REFDOC, iLINKED_TASK_NAME_REFDOC );
      verifyTASKTASkDEP( iLink_TASK, iTASK_DEFN_IDs_2, iLINK_TYPE_CD );

      // verify task_work_type
      verifyWorkType( iTASK_IDs_2, iWORK_TYPE_LIST_1 );
      verifyWorkType( iTASK_IDs_2, iWORK_TYPE_LIST_2 );

      // verify task_sched_rule table
      lTypeIds = getTypeIds( iSCHED_DATA_TYPE_CD );
      verifyTASKSCHEDRULE( iTASK_IDs_2, lTypeIds, iSCHED_INTERVAL_QT, null );

   }


   /**
    * This test is to verify export functionality on c_comp_ref_doc_export.
    *
    * @throws SQLException
    *
    */
   @Test
   public void test_EXPORT() throws SQLException {
      System.out.println(
            "=======Starting:  " + testName.getMethodName() + "========================" );

      // load data
      test_RECURRING_STD_IMPORT();
      clearBaselineLoaderTables();

      runEXPORT();

      // verify staging tables
      // check C_COMP_REF
      String lQuery =
            "select 1 from " + TableUtil.C_COMP_REF + " where REF_TASK_CD='" + iREF_TASK_CD_2
                  + "' and REF_TASK_NAME='" + iREF_TASK_NAME_2 + "' and RECURRING_TASK_BOOL='Y'";
      Assert.assertTrue( "Check C_COMP_REF table to verify the record exists",
            RecordsExist( lQuery ) );

      // check C_COMP_REF_ASSIGNED_PART
      lQuery = "select 1 from " + TableUtil.C_COMP_REF_ASSIGNED_PART + " where REF_TASK_CD='"
            + iREF_TASK_CD_2 + "' and PART_NO_OEM ='" + iPART_NO_OEM_TRK + "'";
      Assert.assertTrue( "Check C_COMP_REF_ASSIGNED_PART table to verify the record exists",
            RecordsExist( lQuery ) );

      // check C_COMP_REF_TASK_LINK
      lQuery = "select 1 from " + TableUtil.C_COMP_REF_TASK_LINK + " where REF_TASK_CD='"
            + iREF_TASK_CD_2 + "' and LINK_TYPE_CD ='" + iLINK_TYPE_CD + "'";
      Assert.assertTrue( "Check C_COMP_REF_TASK_LINK table to verify the record exists",
            RecordsExist( lQuery ) );

      // check C_COMP_REF_DYNAMIC_DEADLINE
      lQuery = "select 1 from " + TableUtil.C_COMP_REF_DYNAMIC_DEADLINE + " where REF_TASK_CD='"
            + iREF_TASK_CD_2 + "' and SCHED_INTERVAL_QT ='" + iSCHED_INTERVAL_QT + "'";
      Assert.assertTrue( "Check C_COMP_REF_STANDARD_DEADLINE table to verify the record exists",
            RecordsExist( lQuery ) );

   }


   // =========================================================================================

   /**
    * This function is called by after test method to restore data which has been modified by tests.
    *
    *
    */

   public void RestoreData() {

      String lStrDelete = null;

      if ( iTASK_IDs != null ) {

         // delete task_work_type
         lStrDelete = "delete from " + TableUtil.TASK_WORK_TYPE + "  where TASK_DB_ID="
               + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_REF_DOC
         lStrDelete = "delete from " + TableUtil.TASK_REF_DOC + "  where TASK_DB_ID="
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

         // delete task_task_flag
         lStrDelete = "delete from " + TableUtil.TASK_TASK_FLAGS + "  where TASK_DB_ID="
               + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_task
         lStrDelete = "delete from " + TableUtil.TASK_TASK + "  where TASK_DB_ID="
               + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID();
         executeSQL( lStrDelete );

      }

      if ( iTASK_DEFN_IDs != null ) {
         lStrDelete = "delete from TASK_DEFN where TASK_DEFN_DB_ID=" + iTASK_DEFN_IDs.getNO_DB_ID()
               + " and TASK_DEFN_ID=" + iTASK_DEFN_IDs.getNO_ID();
         executeSQL( lStrDelete );

         lStrDelete =
               "delete from TASK_TASK_DEP where DEP_TASK_DEFN_DB_ID=" + iTASK_DEFN_IDs.getNO_DB_ID()
                     + " and DEP_TASK_DEFN_ID=" + iTASK_DEFN_IDs.getNO_ID();
         executeSQL( lStrDelete );

      }

      if ( iTASK_IDs_2 != null ) {

         // delete task_work_type
         lStrDelete = "delete from " + TableUtil.TASK_WORK_TYPE + "  where TASK_DB_ID="
               + iTASK_IDs_2.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_2.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_REF_DOC
         lStrDelete = "delete from " + TableUtil.TASK_REF_DOC + "  where TASK_DB_ID="
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

         // delete task_task_flag
         lStrDelete = "delete from " + TableUtil.TASK_TASK_FLAGS + "  where TASK_DB_ID="
               + iTASK_IDs_2.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_2.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_task
         lStrDelete = "delete from " + TableUtil.TASK_TASK + "  where TASK_DB_ID="
               + iTASK_IDs_2.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_2.getNO_ID();
         executeSQL( lStrDelete );

      }

      if ( iTASK_DEFN_IDs_2 != null ) {
         lStrDelete =
               "delete from TASK_DEFN where TASK_DEFN_DB_ID=" + iTASK_DEFN_IDs_2.getNO_DB_ID()
                     + " and TASK_DEFN_ID=" + iTASK_DEFN_IDs_2.getNO_ID();
         executeSQL( lStrDelete );

         lStrDelete = "delete from TASK_TASK_DEP where DEP_TASK_DEFN_DB_ID="
               + iTASK_DEFN_IDs_2.getNO_DB_ID() + " and DEP_TASK_DEFN_ID="
               + iTASK_DEFN_IDs_2.getNO_ID();
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
                     "BEGIN  c_comp_ref_doc_import.validate_comp_ref_document(on_retcode =>?); END;" );

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
                     "BEGIN c_comp_ref_doc_import.import_comp_ref_document(on_retcode =>?); END;" );

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
    *
    */
   protected int runEXPORT() {

      int lReturn = 0;
      CallableStatement lPrepareCallKIT;

      try {

         lPrepareCallKIT = getConnection().prepareCall(
               "BEGIN c_comp_ref_doc_import.export_comp_ref_document(aib_autodel_stg_data => false, "
                     + " aon_retcode => ?, aov_retmsg => ?); END;" );

         lPrepareCallKIT.registerOutParameter( 1, Types.INTEGER );
         lPrepareCallKIT.registerOutParameter( 2, Types.VARCHAR );

         lPrepareCallKIT.execute();
         commit();
         lReturn = lPrepareCallKIT.getInt( 1 );
      } catch ( SQLException e ) {
         e.printStackTrace();
      }
      return lReturn;
   }

}
