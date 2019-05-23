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
 * on staging table bl_fail_defer_dead
 *
 *
 * @author ALICIA QIAN
 */
public class ValidateDeferDead extends DeferralReferencesTests {

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
      if ( lstrTCName.contains( "test_BLDEF_00700" ) ) {
         setDataType();

      }

   }


   /**
    * This test is to verify valid error code BLDEF-00180: ASSMBL_CD must be provided in collection
    * table BL_FAIL_DEFER_DEAD.
    *
    *
    */

   @Test
   public void test_BLDEF_00180() {
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

      // bl_fail_defer_dead
      lDeferralRefMap.clear();
      // lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );
      lDeferralRefMap.put( "DATA_TYPE_CD", "'" + iACFT_DATA_TYPE_CD_HOURS + "'" );
      lDeferralRefMap.put( "DEAD_QT", "'" + iACFT_DEAD_QT + "'" );
      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_DEAD, lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check no error code has generated
      CheckErrorCode( "BLDEF-00180" );

   }


   /**
    * This test is to verify valid error code BLDEF-00190: DEFER_REF_NAME must be provided in
    * collection table BL_FAIL_DEFER_DEAD.
    *
    *
    */

   @Test
   public void test_BLDEF_00190() {
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

      // bl_fail_defer_dead
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      // lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ENG + "'" );
      lDeferralRefMap.put( "DATA_TYPE_CD", "'" + iDATA_TYPE_CD_CDY + "'" );
      lDeferralRefMap.put( "DEAD_QT", "'" + iENG_DEAD_QT + "'" );
      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_DEAD, lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check no error code has generated
      CheckErrorCode( "BLDEF-00190" );

   }


   /**
    * This test is to verify valid error code BLDEF-00200: DATA_TYPE_CD must be provided in
    * collection table BL_FAIL_DEFER_DEAD.
    *
    *
    */

   @Test
   public void test_BLDEF_00200() {
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

      // bl_fail_defer_dead
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iAPU_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_APU + "'" );
      // lDeferralRefMap.put( "DATA_TYPE_CD", "'" + iAPU_DATA_TYPE_CD_HOURS + "'" );
      lDeferralRefMap.put( "DEAD_QT", "'" + iAPU_DEAD_QT + "'" );
      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_DEAD, lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check no error code has generated
      CheckErrorCode( "BLDEF-00200" );

   }


   /**
    * This test is to verify valid error code BLDEF-00210: DEAD_QT must be provided in collection
    * table BL_FAIL_DEFER_DEAD.
    *
    *
    */

   @Test
   public void test_BLDEF_00210() {
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

      // bl_fail_defer_dead
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );
      lDeferralRefMap.put( "DATA_TYPE_CD", "'" + iACFT_DATA_TYPE_CD_HOURS + "'" );
      // lDeferralRefMap.put( "DEAD_QT", "'" + iACFT_DEAD_QT + "'" );
      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_DEAD, lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check no error code has generated
      CheckErrorCode( "BLDEF-00210" );

   }


   /**
    * This test is to verify valid error code BLDEF-00360: ASSMBL_CD / DEFER_REF_NAME / DATA_TYPE_CD
    * must be unique in BL_FAIL_DEFER_DEAD staging table.
    *
    *
    */

   @Test
   public void test_BLDEF_00360() {
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

      // bl_fail_defer_dead
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ENG + "'" );
      lDeferralRefMap.put( "DATA_TYPE_CD", "'" + iDATA_TYPE_CD_CDY + "'" );
      lDeferralRefMap.put( "DEAD_QT", "'" + iENG_DEAD_QT + "'" );
      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_DEAD, lDeferralRefMap ) );

      // bl_fail_defer_dead
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ENG + "'" );
      lDeferralRefMap.put( "DATA_TYPE_CD", "'" + iDATA_TYPE_CD_CDY + "'" );
      lDeferralRefMap.put( "DEAD_QT", "'30'" );
      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_DEAD, lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check no error code has generated
      CheckErrorCode( "BLDEF-00360" );

   }


   /**
    * This test is to verify valid error code BLDEF-00570: ASSMBL_CD / DEFER_REF_NAME combination
    * must have an entry in BL_FAIL_DEF_REF staging table.
    *
    *
    */

   @Test
   public void test_BLDEF_00572_1() {
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

      // bl_fail_defer_dead
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      // lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'INVALID'" );
      lDeferralRefMap.put( "DATA_TYPE_CD", "'" + iACFT_DATA_TYPE_CD_HOURS + "'" );
      lDeferralRefMap.put( "DEAD_QT", "'" + iACFT_DEAD_QT + "'" );
      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_DEAD, lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check no error code has generated
      CheckErrorCode( "BLDEF-00572" );

   }


   /**
    * This test is to verify valid error code BLDEF-00570: ASSMBL_CD / DEFER_REF_NAME combination
    * must have an entry in BL_FAIL_DEF_REF staging table.
    *
    *
    */

   @Test
   public void test_BLDEF_00572_2() {
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

      // bl_fail_defer_dead
      lDeferralRefMap.clear();
      // lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "ASSMBL_CD", "'APU_CD1'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );
      lDeferralRefMap.put( "DATA_TYPE_CD", "'" + iACFT_DATA_TYPE_CD_HOURS + "'" );
      lDeferralRefMap.put( "DEAD_QT", "'" + iACFT_DEAD_QT + "'" );
      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_DEAD, lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check no error code has generated
      CheckErrorCode( "BLDEF-00572" );

   }


   /**
    * This test is to verify valid error code BLDEF-00570: ASSMBL_CD / DEFER_REF_NAME combination
    * must have an entry in BL_FAIL_DEF_REF staging table.
    *
    *
    */

   @Test
   public void test_BLDEF_00572_3() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lDeferralRefMap = new LinkedHashMap<>();

      // bl_fail_defer_dead

      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );
      lDeferralRefMap.put( "DATA_TYPE_CD", "'" + iACFT_DATA_TYPE_CD_HOURS + "'" );
      lDeferralRefMap.put( "DEAD_QT", "'" + iACFT_DEAD_QT + "'" );
      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_DEAD, lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check no error code has generated
      CheckErrorCode( "BLDEF-00572" );

   }


   /**
    * This test is to verify valid error code BLDEF-00690: DATA_TYPE_CD must exist in the
    * MIM_DATA_TYPE table.
    *
    *
    */

   @Test
   public void test_BLDEF_00690() {
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

      // bl_fail_defer_dead
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ENG + "'" );
      // lDeferralRefMap.put( "DATA_TYPE_CD", "'" + iENG_DATA_TYPE_CD_HOURS + "'" );
      lDeferralRefMap.put( "DATA_TYPE_CD", "'INVALID'" );
      lDeferralRefMap.put( "DEAD_QT", "'" + iENG_DEAD_QT + "'" );
      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_DEAD, lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check no error code has generated
      CheckErrorCode( "BLDEF-00690" );

   }


   /**
    * This test is to verify valid error code BLDEF-00700: DATA_TYPE_CD must exist only once in the
    * MIM_DATA_TYPE table.
    *
    *
    */

   @Test
   public void test_BLDEF_00700() {
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

      // bl_fail_defer_dead
      // lDeferralRefMap.clear();
      // lDeferralRefMap.put( "ASSMBL_CD", "'" + iAPU_ASSMBLCD + "'" );
      // lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_APU + "'" );
      // lDeferralRefMap.put( "DATA_TYPE_CD", "'" + iDATA_TYPE_CD_CDY + "'" );
      // lDeferralRefMap.put( "DEAD_QT", "'" + iAPU_DEAD_QT + "'" );

      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );
      lDeferralRefMap.put( "DATA_TYPE_CD", "'" + iACFT_DATA_TYPE_CD_HOURS + "'" );
      lDeferralRefMap.put( "DEAD_QT", "'" + iACFT_DEAD_QT + "'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_DEAD, lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check no error code has generated
      CheckErrorCode( "BLDEF-00700" );

   }


   /**
    * This test is to verify valid error code BLDEF-00710: DATA_TYPE_CD must be HOURS, CYCLES or
    * CDY.
    *
    *
    */

   @Test
   public void test_BLDEF_00710() {
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

      // bl_fail_defer_dead
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ENG + "'" );
      // lDeferralRefMap.put( "DATA_TYPE_CD", "'" + iENG_DATA_TYPE_CD_HOURS + "'" );
      lDeferralRefMap.put( "DATA_TYPE_CD", "'EOT'" );
      lDeferralRefMap.put( "DEAD_QT", "'" + iENG_DEAD_QT + "'" );
      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_DEAD, lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check no error code has generated
      CheckErrorCode( "BLDEF-00710" );

   }


   /**
    * This test is to verify valid error code BLDEF-00720: DATA_TYPE_CD must be valid for ASSMBL_CD
    * / CONFIG_SLOT_CD in Maintenix in mim_part_numdata table.
    *
    *
    */

   @Test
   public void test_BLDEF_00720() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lDeferralRefMap = new LinkedHashMap<>();

      // bl_fail_defer_ref record
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );
      // lDeferralRefMap.put( "CONFIG_SLOT_CD", "'" + iACFT_CONFIG_SLOT_CD + "'" );
      lDeferralRefMap.put( "CONFIG_SLOT_CD", "'SYS-10-1'" );
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

      // bl_fail_defer_dead
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );
      // lDeferralRefMap.put( "DATA_TYPE_CD", "'" + iACFT_DATA_TYPE_CD_HOURS + "'" );
      lDeferralRefMap.put( "DATA_TYPE_CD", "'" + iACFT_DATA_TYPE_CD_CYCLES + "'" );
      lDeferralRefMap.put( "DEAD_QT", "'" + iACFT_DEAD_QT + "'" );
      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_DEAD, lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check no error code has generated
      CheckErrorCode( "BLDEF-00720" );

   }


   /**
    * This test is to verify valid error code BLDEF-00730: DEAD_QT cannot be negative.
    *
    *
    */

   @Test
   public void test_BLDEF_00730() {
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

      // bl_fail_defer_dead
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );
      lDeferralRefMap.put( "DATA_TYPE_CD", "'" + iACFT_DATA_TYPE_CD_HOURS + "'" );
      // lDeferralRefMap.put( "DEAD_QT", "'" + iACFT_DEAD_QT + "'" );
      lDeferralRefMap.put( "DEAD_QT", "'-100'" );
      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_DEAD, lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check no error code has generated
      CheckErrorCode( "BLDEF-00730" );

   }


   /**
    * This test is to verify valid error code BLDEF-00930: ASSMBL_CD / DEFER_REF_NAME has not passed
    * all data quality rules in BL_FAIL_DEFER_DEAD.
    *
    *
    */

   @Test
   public void test_BLDEF_00930() {
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

      // bl_fail_defer_dead
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );
      lDeferralRefMap.put( "DATA_TYPE_CD", "'" + iACFT_DATA_TYPE_CD_HOURS + "'" );
      // lDeferralRefMap.put( "DEAD_QT", "'" + iACFT_DEAD_QT + "'" );
      lDeferralRefMap.put( "DEAD_QT", "'-100'" );
      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_DEAD, lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check no error code has generated
      CheckErrorCode( "BLDEF-00930" );

   }


   // ===================================================================================

   /**
    * This data setup function is to be called before test_BLDEF_00700()
    *
    *
    *
    */
   public void setDataType() {

      String lquery = "insert into " + TableUtil.MIM_DATA_TYPE
            + " (DATA_TYPE_DB_ID, DATA_TYPE_ID, ENG_UNIT_DB_ID, ENG_UNIT_CD, DOMAIN_TYPE_DB_ID,"
            + " DOMAIN_TYPE_CD, ENTRY_PREC_QT, DATA_TYPE_CD, DATA_TYPE_SDESC, DATA_TYPE_MDESC, FORECAST_BOOL, UNIVERSAL_BOOL, RSTAT_CD) "
            + " values (4650, 1, 0, 'HOUR', 0, 'US', 2, 'HOURS', 'AUTOTest', 'AutoTest.', 1, 1, 0)";

      executeSQL( lquery );
   }


   /**
    * This function is called by after test method to restore data which has been modified by tests.
    *
    *
    */
   public void restoreData() {
      String lquery =
            "delete from " + TableUtil.MIM_DATA_TYPE + " where DATA_TYPE_SDESC='AUTOTest'";

      executeSQL( lquery );

   }

}
