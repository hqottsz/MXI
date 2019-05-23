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
 * on staging table bl_fail_defer_conflict
 *
 *
 * @author ALICIA QIAN
 */
public class ValidateDeferConflict extends DeferralReferencesTests {

   @Rule
   public TestName testName = new TestName();


   /**
    * Clean up after each individual test
    *
    */
   @After
   @Override
   public void after() {
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
    * This test is to verify valid error code BLDEF-00095: ASSMBL_CD must be provided in collection
    * table BL_FAIL_DEFER_CONFLICT.
    *
    *
    */

   @Test
   public void test_BLDEF_00095() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lDeferralRefMap = new LinkedHashMap<>();

      // bl_fail_defer_ref record
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

      // bl_fail_defer_ref of second record for conflict
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT_CONFLICT + "'" );
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

      // bl_fail_defer_conflict
      lDeferralRefMap.clear();
      // lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );
      lDeferralRefMap.put( "CONFLICT_DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT_CONFLICT + "'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_CONFLICT, lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check no error code has generated
      CheckErrorCode( "BLDEF-00095" );

   }


   /**
    * This test is to verify valid error code BLDEF-00100:DEFER_REF_NAME must be provided in
    * collection table BL_FAIL_DEFER_CONFLICT.
    *
    *
    */

   @Test
   public void test_BLDEF_00100() {
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

      // bl_fail_defer_ref of second record for conflict
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ENG_CONFLICT + "'" );
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

      // bl_fail_defer_conflict
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      // lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ENG + "'" );
      lDeferralRefMap.put( "CONFLICT_DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ENG_CONFLICT + "'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_CONFLICT, lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check no error code has generated
      CheckErrorCode( "BLDEF-00100" );

   }


   /**
    * This test is to verify valid error code BLDEF-00110: CONFLICT_DEFER_REF_NAME must be provided
    * in collection table BL_FAIL_DEFER_CONFLICT.
    *
    *
    */

   @Test
   public void test_BLDEF_00110() {
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

      // bl_fail_defer_ref of second record for conflict
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iAPU_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_APU_CONFLICT + "'" );
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

      // bl_fail_defer_conflict
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iAPU_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_APU + "'" );
      // lDeferralRefMap.put( "CONFLICT_DEFER_REF_NAME", "'" + iDEFER_REF_NAME_APU_CONFLICT + "'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_CONFLICT, lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check no error code has generated
      CheckErrorCode( "BLDEF-00110" );

   }


   /**
    * This test is to verify valid error code BLDEF-00330: ASSMBL_CD / DEFER_REF_NAME /
    * CONFLICT_DEFER_REF_NAME combination has duplicate entries in BL_FAIL_DEFER_CONFLICT staging
    * table.
    *
    *
    */

   @Test
   public void test_BLDEF_00330() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lDeferralRefMap = new LinkedHashMap<>();

      // bl_fail_defer_ref record
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

      // bl_fail_defer_ref of second record for conflict
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT_CONFLICT + "'" );
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

      // bl_fail_defer_conflict
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );
      lDeferralRefMap.put( "CONFLICT_DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT_CONFLICT + "'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_CONFLICT, lDeferralRefMap ) );

      // bl_fail_defer_conflict
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );
      lDeferralRefMap.put( "CONFLICT_DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT_CONFLICT + "'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_CONFLICT, lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check no error code has generated
      CheckErrorCode( "BLDEF-00330" );
      // CheckErrorCode( "BLDEF-00900" );

   }


   /**
    * This test is to verify valid error code BLDEF-00570:ASSMBL_CD / DEFER_REF_NAME combination
    * must have an entry in BL_FAIL_DEF_REF staging table.
    *
    *
    */

   @Test
   public void test_BLDEF_00571_1() {
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

      // bl_fail_defer_ref of second record for conflict
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ENG_CONFLICT + "'" );
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

      // bl_fail_defer_conflict
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      // lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ENG + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'INVALID'" );
      lDeferralRefMap.put( "CONFLICT_DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ENG_CONFLICT + "'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_CONFLICT, lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check no error code has generated
      CheckErrorCode( "BLDEF-00571" );
      // CheckErrorCode( "BLDEF-00900" );

   }


   /**
    * This test is to verify valid error code BLDEF-00570:ASSMBL_CD / DEFER_REF_NAME combination
    * must have an entry in BL_FAIL_DEF_REF staging table.
    *
    *
    */

   @Test
   public void test_BLDEF_00571_2() {
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

      // bl_fail_defer_ref of second record for conflict
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ENG_CONFLICT + "'" );
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

      // bl_fail_defer_conflict
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      // lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ENG + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'DEFERAPU'" );
      lDeferralRefMap.put( "CONFLICT_DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ENG_CONFLICT + "'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_CONFLICT, lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check no error code has generated
      CheckErrorCode( "BLDEF-00571" );
      // CheckErrorCode( "BLDEF-00900" );

   }


   /**
    * This test is to verify valid error code BLDEF-00570:ASSMBL_CD / DEFER_REF_NAME combination
    * must have an entry in BL_FAIL_DEF_REF staging table.
    *
    *
    */

   @Test
   public void test_BLDEF_00571_3() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lDeferralRefMap = new LinkedHashMap<>();

      // bl_fail_defer_ref of second record for conflict
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT_CONFLICT + "'" );
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

      // bl_fail_defer_conflict
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );
      lDeferralRefMap.put( "CONFLICT_DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT_CONFLICT + "'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_CONFLICT, lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check no error code has generated
      CheckErrorCode( "BLDEF-00571" );
      // CheckErrorCode( "BLDEF-00900" );

   }


   /**
    * This test is to verify valid error code BLDEF-00580: ASSMBL_CD / CONFLICT_DEFER_REF_NAME
    * combination must have an entry in BL_FAIL_DEF_REF staging table or in Maintenix in the
    * FAIL_DEFER_REF table.
    *
    *
    */

   @Test
   public void test_BLDEF_00580_1() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lDeferralRefMap = new LinkedHashMap<>();

      // bl_fail_defer_ref record
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

      // bl_fail_defer_ref of second record for conflict
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT_CONFLICT + "'" );
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

      // bl_fail_defer_conflict
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );
      // lDeferralRefMap.put( "CONFLICT_DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT_CONFLICT + "'"
      // );
      lDeferralRefMap.put( "CONFLICT_DEFER_REF_NAME", "'INVALID'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_CONFLICT, lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check no error code has generated
      CheckErrorCode( "BLDEF-00580" );
      // CheckErrorCode( "BLDEF-00900" );

   }


   /**
    * This test is to verify valid error code BLDEF-00580: ASSMBL_CD / CONFLICT_DEFER_REF_NAME
    * combination must have an entry in BL_FAIL_DEF_REF staging table or in Maintenix in the
    * FAIL_DEFER_REF table.
    *
    *
    */

   @Test
   public void test_BLDEF_00580_2() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lDeferralRefMap = new LinkedHashMap<>();

      // bl_fail_defer_ref record
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

      // bl_fail_defer_ref of second record for conflict
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT_CONFLICT + "'" );
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

      // bl_fail_defer_conflict
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT_CONFLICT + "'" );
      lDeferralRefMap.put( "CONFLICT_DEFER_REF_NAME", "'INVALID'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_CONFLICT, lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check no error code has generated
      CheckErrorCode( "BLDEF-00580" );
      // CheckErrorCode( "BLDEF-00900" );

   }


   /**
    * This test is to verify valid error code BLDEF-00595: DEFER_REF_NAME and
    * CONFLICT_DEFER_REF_NAME cannot be the same.
    *
    */

   @Test
   public void test_BLDEF_00595_1() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lDeferralRefMap = new LinkedHashMap<>();

      // bl_fail_defer_ref record
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

      // bl_fail_defer_ref of second record for conflict
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT_CONFLICT + "'" );
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

      // bl_fail_defer_conflict
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );
      // lDeferralRefMap.put( "CONFLICT_DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT_CONFLICT + "'"
      // );
      lDeferralRefMap.put( "CONFLICT_DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_CONFLICT, lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check no error code has generated
      CheckErrorCode( "BLDEF-00595" );
      // CheckErrorCode( "BLDEF-00900" );

   }


   /**
    * This test is to verify valid error code BLDEF-00595: DEFER_REF_NAME and
    * CONFLICT_DEFER_REF_NAME cannot be the same.
    *
    */

   @Test
   public void test_BLDEF_00595_2() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lDeferralRefMap = new LinkedHashMap<>();

      // bl_fail_defer_ref record
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

      // bl_fail_defer_ref of second record for conflict
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT_CONFLICT + "'" );
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

      // bl_fail_defer_conflict
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      // lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT_CONFLICT + "'" );
      lDeferralRefMap.put( "CONFLICT_DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT_CONFLICT + "'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_CONFLICT, lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check no error code has generated
      CheckErrorCode( "BLDEF-00595" );
      // CheckErrorCode( "BLDEF-00900" );

   }


   /**
    * This test is to verify valid error code BLDEF-00900: ASSMBL_CD / DEFER_REF_NAME has not passed
    * all data quality rules in BL_FAIL_DEFER_CONFLICT.
    *
    *
    */

   @Test
   public void test_BLDEF_00900() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lDeferralRefMap = new LinkedHashMap<>();

      // bl_fail_defer_ref record
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

      // bl_fail_defer_ref of second record for conflict
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT_CONFLICT + "'" );
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

      // bl_fail_defer_conflict
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT_CONFLICT + "'" );
      lDeferralRefMap.put( "CONFLICT_DEFER_REF_NAME", "'INVALID'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_CONFLICT, lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check no error code has generated
      CheckErrorCode( "BLDEF-00900" );

   }

}
