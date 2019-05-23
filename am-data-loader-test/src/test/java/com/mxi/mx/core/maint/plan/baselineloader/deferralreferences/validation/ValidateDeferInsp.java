package com.mxi.mx.core.maint.plan.baselineloader.deferralreferences.validation;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.mxi.mx.core.maint.plan.baselineloader.deferralreferences.DeferralReferencesTests;
import com.mxi.mx.util.TableUtil;


/**
 * This test suite contains test cases on validation error code functionality on Deferral References
 * on staging table bl_fail_defer_insp
 *
 *
 * @author ALICIA QIAN
 */
public class ValidateDeferInsp extends DeferralReferencesTests {

   @Rule
   public TestName testName = new TestName();


   /**
    * Clean up after each individual test
    *
    */
   @After
   @Override
   public void after() {
      restoreData();
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
      String lstrTCName = testName.getMethodName();
      if ( lstrTCName.contains( "test_BLDEF_00640" ) ) {
         setJICTask();

      }

      if ( lstrTCName.contains( "test_BLDEF_00650" ) ) {
         setTaskNoCondition();

      }

      if ( lstrTCName.contains( "test_BLDEF_00660" ) ) {
         setTaskNoRecurring();

      }

      if ( lstrTCName.contains( "test_BLDEF_00670" ) ) {
         setTaskStatus();

      }

   }


   /**
    * This test is to verify valid error code BLDEF-00150: ASSMBL_CD must be provided in collection
    * table BL_FAIL_DEFER_INSP.
    *
    *
    */

   @Test
   public void test_BLDEF_00150() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lDeferralRefMap = new LinkedHashMap<>();

      // bl_fail_defer_ref of first record
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );
      lDeferralRefMap.put( "CONFIG_SLOT_CD", "'" + iACFT_CONFIG_SLOT_CD + "'" );
      lDeferralRefMap.put( "OPERATOR_CD_LIST", "'" + iOPERATOR_CD_LIST + "'" );
      lDeferralRefMap.put( "FAIL_SEV_CD", "'" + iFAIL_SEV_CD + "'" );
      lDeferralRefMap.put( "FAIL_DEFER_CD", "'" + iFAIL_DEFER_CD + "'" );
      lDeferralRefMap.put( "DEFER_REF_LDESC", "'" + iDEFER_REF_LDESC + "'" );
      lDeferralRefMap.put( "DEFER_REF_STATUS_CD", "'" + iDEFER_REF_STATUS_CD + "'" );
      lDeferralRefMap.put( "INST_SYSTEMS_QT", "'" + iINST_SYSTEMS_QT + "'" );
      lDeferralRefMap.put( "DISPATCH_SYSTEMS_QT", "'" + iDISPATCH_SYSTEMS_QT + "'" );
      lDeferralRefMap.put( "APPL_LDESC", "'" + iAPPL_LDESC + "'" );
      lDeferralRefMap.put( "OPER_RESTRICTIONS_LDESC", "'" + iOPER_RESTRICTIONS_LDESC + "'" );
      lDeferralRefMap.put( "PERF_PENALTIES_LDESC", "'" + iPERF_PENALTIES_LDESC + "'" );
      lDeferralRefMap.put( "MAINT_ACTIONS_LDESC", "'" + iMAINT_ACTIONS_LDESC + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_REF, lDeferralRefMap ) );

      // bl_fail_defer_insp;
      lDeferralRefMap.clear();
      // lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );
      lDeferralRefMap.put( "INSP_REQ_CONFIG_SLOT", null );
      lDeferralRefMap.put( "INSP_REQ_TASK_CD", "'" + iACFT_INSP_REQ_TASK_CD_1 + "'" );
      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_INSP, lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check no error code has generated
      CheckErrorCode( "BLDEF-00150" );

   }


   /**
    * This test is to verify valid error code BLDEF-00160: DEFER_REF_NAME must be provided in
    * collection table BL_FAIL_DEFER_INSP.
    *
    *
    */
   @Test
   public void test_BLDEF_00160() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lDeferralRefMap = new LinkedHashMap<>();

      // bl_fail_defer_ref of first record
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ENG + "'" );
      lDeferralRefMap.put( "CONFIG_SLOT_CD", "'" + iENG_CONFIG_SLOT_CD + "'" );
      lDeferralRefMap.put( "OPERATOR_CD_LIST", "'" + iOPERATOR_CD_LIST + "'" );
      lDeferralRefMap.put( "FAIL_SEV_CD", "'" + iFAIL_SEV_CD + "'" );
      lDeferralRefMap.put( "FAIL_DEFER_CD", "'" + iFAIL_DEFER_CD + "'" );
      lDeferralRefMap.put( "DEFER_REF_LDESC", "'" + iDEFER_REF_LDESC + "'" );
      lDeferralRefMap.put( "DEFER_REF_STATUS_CD", "'" + iDEFER_REF_STATUS_CD + "'" );
      lDeferralRefMap.put( "INST_SYSTEMS_QT", "'" + iINST_SYSTEMS_QT + "'" );
      lDeferralRefMap.put( "DISPATCH_SYSTEMS_QT", "'" + iDISPATCH_SYSTEMS_QT + "'" );
      lDeferralRefMap.put( "APPL_LDESC", "'" + iAPPL_LDESC + "'" );
      lDeferralRefMap.put( "OPER_RESTRICTIONS_LDESC", "'" + iOPER_RESTRICTIONS_LDESC + "'" );
      lDeferralRefMap.put( "PERF_PENALTIES_LDESC", "'" + iPERF_PENALTIES_LDESC + "'" );
      lDeferralRefMap.put( "MAINT_ACTIONS_LDESC", "'" + iMAINT_ACTIONS_LDESC + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_REF, lDeferralRefMap ) );

      // bl_fail_defer_insp;
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      // lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ENG + "'" );
      lDeferralRefMap.put( "INSP_REQ_CONFIG_SLOT", null );
      lDeferralRefMap.put( "INSP_REQ_TASK_CD", "'" + iENG_INSP_REQ_TASK_CD + "'" );
      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_INSP, lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check no error code has generated
      CheckErrorCode( "BLDEF-00160" );

   }


   /**
    * This test is to verify valid error code BLDEF-00170: INSP_REQ_TASK_CD must be provided in
    * collection table BL_FAIL_DEFER_INSP.
    *
    *
    */
   @Test
   public void test_BLDEF_00170() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lDeferralRefMap = new LinkedHashMap<>();

      // bl_fail_defer_ref of first record
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iAPU_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_APU + "'" );
      lDeferralRefMap.put( "CONFIG_SLOT_CD", "'" + iAPU_CONFIG_SLOT_CD + "'" );
      lDeferralRefMap.put( "OPERATOR_CD_LIST", "'" + iOPERATOR_CD_LIST + "'" );
      lDeferralRefMap.put( "FAIL_SEV_CD", "'" + iFAIL_SEV_CD + "'" );
      lDeferralRefMap.put( "FAIL_DEFER_CD", "'" + iFAIL_DEFER_CD + "'" );
      lDeferralRefMap.put( "DEFER_REF_LDESC", "'" + iDEFER_REF_LDESC + "'" );
      lDeferralRefMap.put( "DEFER_REF_STATUS_CD", "'" + iDEFER_REF_STATUS_CD + "'" );
      lDeferralRefMap.put( "INST_SYSTEMS_QT", "'" + iINST_SYSTEMS_QT + "'" );
      lDeferralRefMap.put( "DISPATCH_SYSTEMS_QT", "'" + iDISPATCH_SYSTEMS_QT + "'" );
      lDeferralRefMap.put( "APPL_LDESC", "'" + iAPPL_LDESC + "'" );
      lDeferralRefMap.put( "OPER_RESTRICTIONS_LDESC", "'" + iOPER_RESTRICTIONS_LDESC + "'" );
      lDeferralRefMap.put( "PERF_PENALTIES_LDESC", "'" + iPERF_PENALTIES_LDESC + "'" );
      lDeferralRefMap.put( "MAINT_ACTIONS_LDESC", "'" + iMAINT_ACTIONS_LDESC + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_REF, lDeferralRefMap ) );

      // bl_fail_defer_insp;
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iAPU_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_APU + "'" );
      lDeferralRefMap.put( "INSP_REQ_CONFIG_SLOT", null );
      // lDeferralRefMap.put( "INSP_REQ_TASK_CD", "'" + iAPU_INSP_REQ_TASK_CD + "'" );
      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_INSP, lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check no error code has generated
      CheckErrorCode( "BLDEF-00170" );

   }


   /**
    * This test is to verify valid error code BLDEF-00350: ASSMBL_CD / DEFER_REF_NAME /
    * INSP_REQ_TASK_CD combination has duplicate entries in BL_FAIL_DEFER_INSP staging table.
    *
    *
    */

   @Test
   public void test_BLDEF_00350() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lDeferralRefMap = new LinkedHashMap<>();

      // bl_fail_defer_ref of first record
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );
      lDeferralRefMap.put( "CONFIG_SLOT_CD", "'" + iACFT_CONFIG_SLOT_CD + "'" );
      lDeferralRefMap.put( "OPERATOR_CD_LIST", "'" + iOPERATOR_CD_LIST + "'" );
      lDeferralRefMap.put( "FAIL_SEV_CD", "'" + iFAIL_SEV_CD + "'" );
      lDeferralRefMap.put( "FAIL_DEFER_CD", "'" + iFAIL_DEFER_CD + "'" );
      lDeferralRefMap.put( "DEFER_REF_LDESC", "'" + iDEFER_REF_LDESC + "'" );
      lDeferralRefMap.put( "DEFER_REF_STATUS_CD", "'" + iDEFER_REF_STATUS_CD + "'" );
      lDeferralRefMap.put( "INST_SYSTEMS_QT", "'" + iINST_SYSTEMS_QT + "'" );
      lDeferralRefMap.put( "DISPATCH_SYSTEMS_QT", "'" + iDISPATCH_SYSTEMS_QT + "'" );
      lDeferralRefMap.put( "APPL_LDESC", "'" + iAPPL_LDESC + "'" );
      lDeferralRefMap.put( "OPER_RESTRICTIONS_LDESC", "'" + iOPER_RESTRICTIONS_LDESC + "'" );
      lDeferralRefMap.put( "PERF_PENALTIES_LDESC", "'" + iPERF_PENALTIES_LDESC + "'" );
      lDeferralRefMap.put( "MAINT_ACTIONS_LDESC", "'" + iMAINT_ACTIONS_LDESC + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_REF, lDeferralRefMap ) );

      // bl_fail_defer_insp;
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );
      lDeferralRefMap.put( "INSP_REQ_CONFIG_SLOT", null );
      lDeferralRefMap.put( "INSP_REQ_TASK_CD", "'" + iACFT_INSP_REQ_TASK_CD_1 + "'" );
      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_INSP, lDeferralRefMap ) );

      // bl_fail_defer_insp;
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );
      lDeferralRefMap.put( "INSP_REQ_CONFIG_SLOT", null );
      lDeferralRefMap.put( "INSP_REQ_TASK_CD", "'" + iACFT_INSP_REQ_TASK_CD_1 + "'" );
      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_INSP, lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check no error code has generated
      CheckErrorCode( "BLDEF-00350" );
      // CheckErrorCode( "BLDEF-00920" );

   }


   /**
    * This test is to verify valid error code BLDEF-00570: ASSMBL_CD / DEFER_REF_NAME combination
    * must have an entry in BL_FAIL_DEF_REF staging table.
    *
    *
    */

   @Test
   public void test_BLDEF_00573_1() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lDeferralRefMap = new LinkedHashMap<>();

      // bl_fail_defer_ref of first record
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );
      lDeferralRefMap.put( "CONFIG_SLOT_CD", "'" + iACFT_CONFIG_SLOT_CD + "'" );
      lDeferralRefMap.put( "OPERATOR_CD_LIST", "'" + iOPERATOR_CD_LIST + "'" );
      lDeferralRefMap.put( "FAIL_SEV_CD", "'" + iFAIL_SEV_CD + "'" );
      lDeferralRefMap.put( "FAIL_DEFER_CD", "'" + iFAIL_DEFER_CD + "'" );
      lDeferralRefMap.put( "DEFER_REF_LDESC", "'" + iDEFER_REF_LDESC + "'" );
      lDeferralRefMap.put( "DEFER_REF_STATUS_CD", "'" + iDEFER_REF_STATUS_CD + "'" );
      lDeferralRefMap.put( "INST_SYSTEMS_QT", "'" + iINST_SYSTEMS_QT + "'" );
      lDeferralRefMap.put( "DISPATCH_SYSTEMS_QT", "'" + iDISPATCH_SYSTEMS_QT + "'" );
      lDeferralRefMap.put( "APPL_LDESC", "'" + iAPPL_LDESC + "'" );
      lDeferralRefMap.put( "OPER_RESTRICTIONS_LDESC", "'" + iOPER_RESTRICTIONS_LDESC + "'" );
      lDeferralRefMap.put( "PERF_PENALTIES_LDESC", "'" + iPERF_PENALTIES_LDESC + "'" );
      lDeferralRefMap.put( "MAINT_ACTIONS_LDESC", "'" + iMAINT_ACTIONS_LDESC + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_REF, lDeferralRefMap ) );

      // bl_fail_defer_insp;
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      // lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'INVALID'" );
      lDeferralRefMap.put( "INSP_REQ_CONFIG_SLOT", null );
      lDeferralRefMap.put( "INSP_REQ_TASK_CD", "'" + iACFT_INSP_REQ_TASK_CD_1 + "'" );
      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_INSP, lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check no error code has generated
      CheckErrorCode( "BLDEF-00573" );
      // CheckErrorCode( "BLDEF-00920" );

   }


   /**
    * This test is to verify valid error code BLDEF-00570: ASSMBL_CD / DEFER_REF_NAME combination
    * must have an entry in BL_FAIL_DEF_REF staging table.
    *
    *
    */

   @Test
   public void test_BLDEF_00573_2() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lDeferralRefMap = new LinkedHashMap<>();

      // bl_fail_defer_ref of first record
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );
      lDeferralRefMap.put( "CONFIG_SLOT_CD", "'" + iACFT_CONFIG_SLOT_CD + "'" );
      lDeferralRefMap.put( "OPERATOR_CD_LIST", "'" + iOPERATOR_CD_LIST + "'" );
      lDeferralRefMap.put( "FAIL_SEV_CD", "'" + iFAIL_SEV_CD + "'" );
      lDeferralRefMap.put( "FAIL_DEFER_CD", "'" + iFAIL_DEFER_CD + "'" );
      lDeferralRefMap.put( "DEFER_REF_LDESC", "'" + iDEFER_REF_LDESC + "'" );
      lDeferralRefMap.put( "DEFER_REF_STATUS_CD", "'" + iDEFER_REF_STATUS_CD + "'" );
      lDeferralRefMap.put( "INST_SYSTEMS_QT", "'" + iINST_SYSTEMS_QT + "'" );
      lDeferralRefMap.put( "DISPATCH_SYSTEMS_QT", "'" + iDISPATCH_SYSTEMS_QT + "'" );
      lDeferralRefMap.put( "APPL_LDESC", "'" + iAPPL_LDESC + "'" );
      lDeferralRefMap.put( "OPER_RESTRICTIONS_LDESC", "'" + iOPER_RESTRICTIONS_LDESC + "'" );
      lDeferralRefMap.put( "PERF_PENALTIES_LDESC", "'" + iPERF_PENALTIES_LDESC + "'" );
      lDeferralRefMap.put( "MAINT_ACTIONS_LDESC", "'" + iMAINT_ACTIONS_LDESC + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_REF, lDeferralRefMap ) );

      // bl_fail_defer_insp;
      lDeferralRefMap.clear();
      // lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );
      lDeferralRefMap.put( "INSP_REQ_CONFIG_SLOT", null );
      lDeferralRefMap.put( "INSP_REQ_TASK_CD", "'" + iACFT_INSP_REQ_TASK_CD_1 + "'" );
      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_INSP, lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check no error code has generated
      CheckErrorCode( "BLDEF-00573" );
      // CheckErrorCode( "BLDEF-00920" );

   }


   /**
    * This test is to verify valid error code BLDEF-00570: ASSMBL_CD / DEFER_REF_NAME combination
    * must have an entry in BL_FAIL_DEF_REF staging table.
    *
    *
    */

   @Test
   public void test_BLDEF_00573_3() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lDeferralRefMap = new LinkedHashMap<>();

      // bl_fail_defer_insp;

      lDeferralRefMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ENG + "'" );
      lDeferralRefMap.put( "INSP_REQ_CONFIG_SLOT", null );
      lDeferralRefMap.put( "INSP_REQ_TASK_CD", "'" + iENG_INSP_REQ_TASK_CD + "'" );
      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_INSP, lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check no error code has generated
      CheckErrorCode( "BLDEF-00573" );
      // CheckErrorCode( "BLDEF-00920" );

   }


   /**
    * This test is to verify valid error code BLDEF-00620: DEFER_REF_NAME must be provided in
    * collection table BL_FAIL_DEFER_INSP.
    *
    *
    */
   @Test
   public void test_BLDEF_00620_1() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lDeferralRefMap = new LinkedHashMap<>();

      // bl_fail_defer_ref of first record
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ENG + "'" );
      lDeferralRefMap.put( "CONFIG_SLOT_CD", "'" + iENG_CONFIG_SLOT_CD + "'" );
      lDeferralRefMap.put( "OPERATOR_CD_LIST", "'" + iOPERATOR_CD_LIST + "'" );
      lDeferralRefMap.put( "FAIL_SEV_CD", "'" + iFAIL_SEV_CD + "'" );
      lDeferralRefMap.put( "FAIL_DEFER_CD", "'" + iFAIL_DEFER_CD + "'" );
      lDeferralRefMap.put( "DEFER_REF_LDESC", "'" + iDEFER_REF_LDESC + "'" );
      lDeferralRefMap.put( "DEFER_REF_STATUS_CD", "'" + iDEFER_REF_STATUS_CD + "'" );
      lDeferralRefMap.put( "INST_SYSTEMS_QT", "'" + iINST_SYSTEMS_QT + "'" );
      lDeferralRefMap.put( "DISPATCH_SYSTEMS_QT", "'" + iDISPATCH_SYSTEMS_QT + "'" );
      lDeferralRefMap.put( "APPL_LDESC", "'" + iAPPL_LDESC + "'" );
      lDeferralRefMap.put( "OPER_RESTRICTIONS_LDESC", "'" + iOPER_RESTRICTIONS_LDESC + "'" );
      lDeferralRefMap.put( "PERF_PENALTIES_LDESC", "'" + iPERF_PENALTIES_LDESC + "'" );
      lDeferralRefMap.put( "MAINT_ACTIONS_LDESC", "'" + iMAINT_ACTIONS_LDESC + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_REF, lDeferralRefMap ) );

      // bl_fail_defer_insp;
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ENG + "'" );
      lDeferralRefMap.put( "INSP_REQ_CONFIG_SLOT", null );
      // lDeferralRefMap.put( "INSP_REQ_TASK_CD", "'" + iENG_INSP_REQ_TASK_CD + "'" );
      lDeferralRefMap.put( "INSP_REQ_TASK_CD", "'INVALID'" );
      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_INSP, lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check no error code has generated
      CheckErrorCode( "BLDEF-00620" );
      // CheckErrorCode( "BLDEF-00920" );

   }


   /**
    * This test is to verify valid error code BLDEF-00620: DEFER_REF_NAME must be provided in
    * collection table BL_FAIL_DEFER_INSP.
    *
    *
    */
   @Test
   public void test_BLDEF_00620_2() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lDeferralRefMap = new LinkedHashMap<>();

      // bl_fail_defer_ref of first record
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ENG + "'" );
      lDeferralRefMap.put( "CONFIG_SLOT_CD", "'" + iENG_CONFIG_SLOT_CD + "'" );
      lDeferralRefMap.put( "OPERATOR_CD_LIST", "'" + iOPERATOR_CD_LIST + "'" );
      lDeferralRefMap.put( "FAIL_SEV_CD", "'" + iFAIL_SEV_CD + "'" );
      lDeferralRefMap.put( "FAIL_DEFER_CD", "'" + iFAIL_DEFER_CD + "'" );
      lDeferralRefMap.put( "DEFER_REF_LDESC", "'" + iDEFER_REF_LDESC + "'" );
      lDeferralRefMap.put( "DEFER_REF_STATUS_CD", "'" + iDEFER_REF_STATUS_CD + "'" );
      lDeferralRefMap.put( "INST_SYSTEMS_QT", "'" + iINST_SYSTEMS_QT + "'" );
      lDeferralRefMap.put( "DISPATCH_SYSTEMS_QT", "'" + iDISPATCH_SYSTEMS_QT + "'" );
      lDeferralRefMap.put( "APPL_LDESC", "'" + iAPPL_LDESC + "'" );
      lDeferralRefMap.put( "OPER_RESTRICTIONS_LDESC", "'" + iOPER_RESTRICTIONS_LDESC + "'" );
      lDeferralRefMap.put( "PERF_PENALTIES_LDESC", "'" + iPERF_PENALTIES_LDESC + "'" );
      lDeferralRefMap.put( "MAINT_ACTIONS_LDESC", "'" + iMAINT_ACTIONS_LDESC + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_REF, lDeferralRefMap ) );

      // bl_fail_defer_insp;
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ENG + "'" );
      lDeferralRefMap.put( "INSP_REQ_CONFIG_SLOT", null );
      // lDeferralRefMap.put( "INSP_REQ_TASK_CD", "'" + iENG_INSP_REQ_TASK_CD + "'" );
      lDeferralRefMap.put( "INSP_REQ_TASK_CD", "'" + iACFT_INSP_REQ_TASK_CD_1 + "'" );
      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_INSP, lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check no error code has generated
      CheckErrorCode( "BLDEF-00620" );
      // CheckErrorCode( "BLDEF-00920" );

   }


   /**
    * This test is to verify valid error code BLDEF-00630: INSP_REQ_CONFIG_SLOT must be specified
    * when the INSP_REQ_TASK_CD is not unique on the assembly.
    *
    *
    */

   @Test
   public void test_BLDEF_00630() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lDeferralRefMap = new LinkedHashMap<>();

      // bl_fail_defer_ref of first record
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );
      lDeferralRefMap.put( "CONFIG_SLOT_CD", "'" + iACFT_CONFIG_SLOT_CD + "'" );
      lDeferralRefMap.put( "OPERATOR_CD_LIST", "'" + iOPERATOR_CD_LIST + "'" );
      lDeferralRefMap.put( "FAIL_SEV_CD", "'" + iFAIL_SEV_CD + "'" );
      lDeferralRefMap.put( "FAIL_DEFER_CD", "'" + iFAIL_DEFER_CD + "'" );
      lDeferralRefMap.put( "DEFER_REF_LDESC", "'" + iDEFER_REF_LDESC + "'" );
      lDeferralRefMap.put( "DEFER_REF_STATUS_CD", "'" + iDEFER_REF_STATUS_CD + "'" );
      lDeferralRefMap.put( "INST_SYSTEMS_QT", "'" + iINST_SYSTEMS_QT + "'" );
      lDeferralRefMap.put( "DISPATCH_SYSTEMS_QT", "'" + iDISPATCH_SYSTEMS_QT + "'" );
      lDeferralRefMap.put( "APPL_LDESC", "'" + iAPPL_LDESC + "'" );
      lDeferralRefMap.put( "OPER_RESTRICTIONS_LDESC", "'" + iOPER_RESTRICTIONS_LDESC + "'" );
      lDeferralRefMap.put( "PERF_PENALTIES_LDESC", "'" + iPERF_PENALTIES_LDESC + "'" );
      lDeferralRefMap.put( "MAINT_ACTIONS_LDESC", "'" + iMAINT_ACTIONS_LDESC + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_REF, lDeferralRefMap ) );

      // bl_fail_defer_insp;
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );
      lDeferralRefMap.put( "INSP_REQ_CONFIG_SLOT", null );
      // lDeferralRefMap.put( "INSP_REQ_TASK_CD", "'" + iACFT_INSP_REQ_TASK_CD_1 + "'" );
      lDeferralRefMap.put( "INSP_REQ_TASK_CD", "'" + iACFT_INSP_REQ_TASK_CD_2 + "'" );
      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_INSP, lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check no error code has generated
      CheckErrorCode( "BLDEF-00630" );
      // CheckErrorCode( "BLDEF-00920" );

   }


   /**
    * This test is to verify valid error code BLDEF-00640: INSP_REQ_TASK_CD must be a requirement
    * (REQ class_mode_cd).
    *
    *
    */

   @Test
   public void test_BLDEF_00640() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lDeferralRefMap = new LinkedHashMap<>();

      // bl_fail_defer_ref of first record
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );
      lDeferralRefMap.put( "CONFIG_SLOT_CD", "'" + iACFT_CONFIG_SLOT_CD + "'" );
      lDeferralRefMap.put( "OPERATOR_CD_LIST", "'" + iOPERATOR_CD_LIST + "'" );
      lDeferralRefMap.put( "FAIL_SEV_CD", "'" + iFAIL_SEV_CD + "'" );
      lDeferralRefMap.put( "FAIL_DEFER_CD", "'" + iFAIL_DEFER_CD + "'" );
      lDeferralRefMap.put( "DEFER_REF_LDESC", "'" + iDEFER_REF_LDESC + "'" );
      lDeferralRefMap.put( "DEFER_REF_STATUS_CD", "'" + iDEFER_REF_STATUS_CD + "'" );
      lDeferralRefMap.put( "INST_SYSTEMS_QT", "'" + iINST_SYSTEMS_QT + "'" );
      lDeferralRefMap.put( "DISPATCH_SYSTEMS_QT", "'" + iDISPATCH_SYSTEMS_QT + "'" );
      lDeferralRefMap.put( "APPL_LDESC", "'" + iAPPL_LDESC + "'" );
      lDeferralRefMap.put( "OPER_RESTRICTIONS_LDESC", "'" + iOPER_RESTRICTIONS_LDESC + "'" );
      lDeferralRefMap.put( "PERF_PENALTIES_LDESC", "'" + iPERF_PENALTIES_LDESC + "'" );
      lDeferralRefMap.put( "MAINT_ACTIONS_LDESC", "'" + iMAINT_ACTIONS_LDESC + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_REF, lDeferralRefMap ) );

      // bl_fail_defer_insp;
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );
      lDeferralRefMap.put( "INSP_REQ_CONFIG_SLOT", null );
      // lDeferralRefMap.put( "INSP_REQ_TASK_CD", "'" + iACFT_INSP_REQ_TASK_CD_1 + "'" );
      lDeferralRefMap.put( "INSP_REQ_TASK_CD", "'" + iACFT_INSP_JIC_TASK_CD_1 + "'" );
      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_INSP, lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check no error code has generated
      CheckErrorCode( "BLDEF-00640" );
      // CheckErrorCode( "BLDEF-00920" );

   }


   /**
    * This test is to verify valid error code BLDEF-00650: INSP_REQ_TASK_CD must be an ON CONDITION
    * task.
    *
    *
    */

   @Test
   public void test_BLDEF_00650() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lDeferralRefMap = new LinkedHashMap<>();

      // bl_fail_defer_ref of first record
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );
      lDeferralRefMap.put( "CONFIG_SLOT_CD", "'" + iACFT_CONFIG_SLOT_CD + "'" );
      lDeferralRefMap.put( "OPERATOR_CD_LIST", "'" + iOPERATOR_CD_LIST + "'" );
      lDeferralRefMap.put( "FAIL_SEV_CD", "'" + iFAIL_SEV_CD + "'" );
      lDeferralRefMap.put( "FAIL_DEFER_CD", "'" + iFAIL_DEFER_CD + "'" );
      lDeferralRefMap.put( "DEFER_REF_LDESC", "'" + iDEFER_REF_LDESC + "'" );
      lDeferralRefMap.put( "DEFER_REF_STATUS_CD", "'" + iDEFER_REF_STATUS_CD + "'" );
      lDeferralRefMap.put( "INST_SYSTEMS_QT", "'" + iINST_SYSTEMS_QT + "'" );
      lDeferralRefMap.put( "DISPATCH_SYSTEMS_QT", "'" + iDISPATCH_SYSTEMS_QT + "'" );
      lDeferralRefMap.put( "APPL_LDESC", "'" + iAPPL_LDESC + "'" );
      lDeferralRefMap.put( "OPER_RESTRICTIONS_LDESC", "'" + iOPER_RESTRICTIONS_LDESC + "'" );
      lDeferralRefMap.put( "PERF_PENALTIES_LDESC", "'" + iPERF_PENALTIES_LDESC + "'" );
      lDeferralRefMap.put( "MAINT_ACTIONS_LDESC", "'" + iMAINT_ACTIONS_LDESC + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_REF, lDeferralRefMap ) );

      // bl_fail_defer_insp;
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );
      lDeferralRefMap.put( "INSP_REQ_CONFIG_SLOT", null );
      lDeferralRefMap.put( "INSP_REQ_TASK_CD", "'" + iACFT_INSP_REQ_TASK_CD_1 + "'" );
      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_INSP, lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check no error code has generated
      CheckErrorCode( "BLDEF-00650" );
      // CheckErrorCode( "BLDEF-00920" );

   }


   /**
    * This test is to verify valid error code BLDEF-00660: INSP_REQ_TASK_CD must be a recurring
    * task.
    *
    *
    */

   @Test
   public void test_BLDEF_00660() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lDeferralRefMap = new LinkedHashMap<>();

      // bl_fail_defer_ref of first record
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );
      lDeferralRefMap.put( "CONFIG_SLOT_CD", "'" + iACFT_CONFIG_SLOT_CD + "'" );
      lDeferralRefMap.put( "OPERATOR_CD_LIST", "'" + iOPERATOR_CD_LIST + "'" );
      lDeferralRefMap.put( "FAIL_SEV_CD", "'" + iFAIL_SEV_CD + "'" );
      lDeferralRefMap.put( "FAIL_DEFER_CD", "'" + iFAIL_DEFER_CD + "'" );
      lDeferralRefMap.put( "DEFER_REF_LDESC", "'" + iDEFER_REF_LDESC + "'" );
      lDeferralRefMap.put( "DEFER_REF_STATUS_CD", "'" + iDEFER_REF_STATUS_CD + "'" );
      lDeferralRefMap.put( "INST_SYSTEMS_QT", "'" + iINST_SYSTEMS_QT + "'" );
      lDeferralRefMap.put( "DISPATCH_SYSTEMS_QT", "'" + iDISPATCH_SYSTEMS_QT + "'" );
      lDeferralRefMap.put( "APPL_LDESC", "'" + iAPPL_LDESC + "'" );
      lDeferralRefMap.put( "OPER_RESTRICTIONS_LDESC", "'" + iOPER_RESTRICTIONS_LDESC + "'" );
      lDeferralRefMap.put( "PERF_PENALTIES_LDESC", "'" + iPERF_PENALTIES_LDESC + "'" );
      lDeferralRefMap.put( "MAINT_ACTIONS_LDESC", "'" + iMAINT_ACTIONS_LDESC + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_REF, lDeferralRefMap ) );

      // bl_fail_defer_insp;
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );
      lDeferralRefMap.put( "INSP_REQ_CONFIG_SLOT", null );
      lDeferralRefMap.put( "INSP_REQ_TASK_CD", "'" + iACFT_INSP_REQ_TASK_CD_1 + "'" );
      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_INSP, lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check no error code has generated
      CheckErrorCode( "BLDEF-00660" );
      // CheckErrorCode( "BLDEF-00920" );

   }


   /**
    * This test is to verify valid error code BLDEF-00670: INSP_REQ_TASK_CD must be in ACTIVE or
    * BUILD status.
    *
    *
    */

   @Test
   public void test_BLDEF_00670() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lDeferralRefMap = new LinkedHashMap<>();

      // bl_fail_defer_ref of first record
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );
      lDeferralRefMap.put( "CONFIG_SLOT_CD", "'" + iACFT_CONFIG_SLOT_CD + "'" );
      lDeferralRefMap.put( "OPERATOR_CD_LIST", "'" + iOPERATOR_CD_LIST + "'" );
      lDeferralRefMap.put( "FAIL_SEV_CD", "'" + iFAIL_SEV_CD + "'" );
      lDeferralRefMap.put( "FAIL_DEFER_CD", "'" + iFAIL_DEFER_CD + "'" );
      lDeferralRefMap.put( "DEFER_REF_LDESC", "'" + iDEFER_REF_LDESC + "'" );
      lDeferralRefMap.put( "DEFER_REF_STATUS_CD", "'" + iDEFER_REF_STATUS_CD + "'" );
      lDeferralRefMap.put( "INST_SYSTEMS_QT", "'" + iINST_SYSTEMS_QT + "'" );
      lDeferralRefMap.put( "DISPATCH_SYSTEMS_QT", "'" + iDISPATCH_SYSTEMS_QT + "'" );
      lDeferralRefMap.put( "APPL_LDESC", "'" + iAPPL_LDESC + "'" );
      lDeferralRefMap.put( "OPER_RESTRICTIONS_LDESC", "'" + iOPER_RESTRICTIONS_LDESC + "'" );
      lDeferralRefMap.put( "PERF_PENALTIES_LDESC", "'" + iPERF_PENALTIES_LDESC + "'" );
      lDeferralRefMap.put( "MAINT_ACTIONS_LDESC", "'" + iMAINT_ACTIONS_LDESC + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_REF, lDeferralRefMap ) );

      // bl_fail_defer_insp;
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );
      lDeferralRefMap.put( "INSP_REQ_CONFIG_SLOT", null );
      lDeferralRefMap.put( "INSP_REQ_TASK_CD", "'" + iACFT_INSP_REQ_TASK_CD_1 + "'" );
      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_INSP, lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check no error code has generated
      CheckErrorCode( "BLDEF-00670" );
      // CheckErrorCode( "BLDEF-00920" );

   }


   /**
    * This test is to verify valid error code BLDEF-00920: DEFER_REF_NAME and INSP_REQ_TASK_CD must
    * be on the same ASSMBL_CD.
    *
    *
    */
   @Test
   public void test_BLDEF_00920() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lDeferralRefMap = new LinkedHashMap<>();

      // bl_fail_defer_ref of first record
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ENG + "'" );
      lDeferralRefMap.put( "CONFIG_SLOT_CD", "'" + iENG_CONFIG_SLOT_CD + "'" );
      lDeferralRefMap.put( "OPERATOR_CD_LIST", "'" + iOPERATOR_CD_LIST + "'" );
      lDeferralRefMap.put( "FAIL_SEV_CD", "'" + iFAIL_SEV_CD + "'" );
      lDeferralRefMap.put( "FAIL_DEFER_CD", "'" + iFAIL_DEFER_CD + "'" );
      lDeferralRefMap.put( "DEFER_REF_LDESC", "'" + iDEFER_REF_LDESC + "'" );
      lDeferralRefMap.put( "DEFER_REF_STATUS_CD", "'" + iDEFER_REF_STATUS_CD + "'" );
      lDeferralRefMap.put( "INST_SYSTEMS_QT", "'" + iINST_SYSTEMS_QT + "'" );
      lDeferralRefMap.put( "DISPATCH_SYSTEMS_QT", "'" + iDISPATCH_SYSTEMS_QT + "'" );
      lDeferralRefMap.put( "APPL_LDESC", "'" + iAPPL_LDESC + "'" );
      lDeferralRefMap.put( "OPER_RESTRICTIONS_LDESC", "'" + iOPER_RESTRICTIONS_LDESC + "'" );
      lDeferralRefMap.put( "PERF_PENALTIES_LDESC", "'" + iPERF_PENALTIES_LDESC + "'" );
      lDeferralRefMap.put( "MAINT_ACTIONS_LDESC", "'" + iMAINT_ACTIONS_LDESC + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_REF, lDeferralRefMap ) );

      // bl_fail_defer_insp;
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ENG + "'" );
      lDeferralRefMap.put( "INSP_REQ_CONFIG_SLOT", null );
      // lDeferralRefMap.put( "INSP_REQ_TASK_CD", "'" + iENG_INSP_REQ_TASK_CD + "'" );
      lDeferralRefMap.put( "INSP_REQ_TASK_CD", "'" + iAPU_INSP_REQ_TASK_CD + "'" );
      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_INSP, lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check no error code has generated
      CheckErrorCode( "BLDEF-00920" );

   }


   // ===========================================================

   /**
    * This data setup function is to be called before test_BLDEF_00640()
    *
    *
    *
    */
   public void setJICTask() {
      String lquery = "UPDATE " + TableUtil.TASK_TASK
            + " SET recurring_task_bool = 1, on_condition_bool = 1, task_def_status_cd='ACTV' "
            + "where assmbl_cd='ACFT_CD1' and task_class_cd='JIC' and task_cd='"
            + iACFT_INSP_JIC_TASK_CD_1 + "'";

      executeSQL( lquery );
   }


   /**
    * This data setup function is to be called before test_BLDEF_00650()
    *
    *
    *
    */
   public void setTaskNoCondition() {

      String lquery = "UPDATE " + TableUtil.TASK_TASK
            + " SET recurring_task_bool = 1, on_condition_bool = 0, task_def_status_cd='ACTV' "
            + "where assmbl_cd='ACFT_CD1' and task_class_cd='REQ' and task_cd='"
            + iACFT_INSP_REQ_TASK_CD_1 + "'";

      executeSQL( lquery );

   }


   /**
    * This data setup function is to be called before test_BLDEF_00660()
    *
    *
    *
    */
   public void setTaskNoRecurring() {

      String lquery = "UPDATE " + TableUtil.TASK_TASK
            + " SET recurring_task_bool = 0, on_condition_bool = 1, task_def_status_cd='ACTV' "
            + "where assmbl_cd='ACFT_CD1' and task_class_cd='REQ' and task_cd='"
            + iACFT_INSP_REQ_TASK_CD_1 + "'";

      executeSQL( lquery );

   }


   /**
    * This data setup function is to be called before test_BLDEF_00670()
    *
    *
    *
    */

   public void setTaskStatus() {

      String lquery = "UPDATE " + TableUtil.TASK_TASK
            + " SET recurring_task_bool = 1, on_condition_bool = 1, task_def_status_cd='REVISION' "
            + "where assmbl_cd='ACFT_CD1' and task_class_cd='REQ' and task_cd='"
            + iACFT_INSP_REQ_TASK_CD_1 + "'";

      executeSQL( lquery );

   }


   /**
    * This function is called by after test method to restore data which has been modified by tests.
    *
    *
    */
   public void restoreData() {
      String lquery = "UPDATE " + TableUtil.TASK_TASK
            + " SET recurring_task_bool = 0, on_condition_bool = 0, task_def_status_cd='ACTV' "
            + "where assmbl_cd='ACFT_CD1' and task_class_cd='JIC' and task_cd='"
            + iACFT_INSP_JIC_TASK_CD_1 + "'";
      executeSQL( lquery );

      lquery = "UPDATE " + TableUtil.TASK_TASK
            + " SET recurring_task_bool = 1, on_condition_bool = 1, task_def_status_cd='ACTV' "
            + "where assmbl_cd='ACFT_CD1' and task_class_cd='REQ' and task_cd='"
            + iACFT_INSP_REQ_TASK_CD_1 + "'";
      executeSQL( lquery );

   }

}
