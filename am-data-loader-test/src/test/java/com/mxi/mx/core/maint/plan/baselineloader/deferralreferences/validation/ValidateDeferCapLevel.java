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
 * This will perform validation for bl_fail_defer_cap_level table
 *
 */
public class ValidateDeferCapLevel extends DeferralReferencesTests {

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
    *
    * Test BLDEF-00220 ASSMBL_CD is mandatory
    *
    */
   @Test
   public void testBLDEF_00220_ASSMBL_CD_IsMandatory() {
      System.out.println( "======= Starting: " + testName.getMethodName()
            + " Validation ========================" );

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

      // bl_fail_defer_cap_level
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );
      lDeferralRefMap.put( "ACFT_CAP_CD", "'" + iACFT_CAP_CD + "'" );
      lDeferralRefMap.put( "ACFT_CAP_LEVEL_CD", "'" + iACFT_CAP_LEVEL_CD + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_CAP_LEVEL,
            lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check for error code
      CheckErrorCode( "BLDEF-00220" );
   }


   /**
    *
    * Test BLDEF-00230 DEFER_REF_NAME is mandatory
    *
    */
   @Test
   public void testBLDEF_00230_DEFER_REF_NAME_IsMandatory() {
      System.out.println( "======= Starting: " + testName.getMethodName()
            + " Validation ========================" );

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

      // bl_fail_defer_cap_level
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "ACFT_CAP_CD", "'" + iACFT_CAP_CD + "'" );
      lDeferralRefMap.put( "ACFT_CAP_LEVEL_CD", "'" + iACFT_CAP_LEVEL_CD + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_CAP_LEVEL,
            lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check for error code
      CheckErrorCode( "BLDEF-00230" );
   }


   /**
    *
    * Test BLDEF-00240 ACFT_CAP_CD is mandatory
    *
    */
   @Test
   public void testBLDEF_00240_ACFT_CAP_CD_IsMandatory() {
      System.out.println( "======= Starting: " + testName.getMethodName()
            + " Validation ========================" );

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

      // bl_fail_defer_cap_level
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );
      lDeferralRefMap.put( "ACFT_CAP_LEVEL_CD", "'" + iACFT_CAP_LEVEL_CD + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_CAP_LEVEL,
            lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check for error code
      CheckErrorCode( "BLDEF-00240" );
   }


   /**
    *
    * Test BLDEF-00250 ACFT_CAP_LEVEL_CD is mandatory
    *
    */
   @Test
   public void testBLDEF_00250_ACFT_CAP_LEVEL_CD_IsMandatory() {
      System.out.println( "======= Starting: " + testName.getMethodName()
            + " Validation ========================" );

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

      // bl_fail_defer_cap_level
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );
      lDeferralRefMap.put( "ACFT_CAP_CD", "'" + iACFT_CAP_CD + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_CAP_LEVEL,
            lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check for error code
      CheckErrorCode( "BLDEF-00250" );
   }


   /**
    *
    * Test BLDEF-00370 ASSMBL_CD/ DEFER_REF_NAME/ ACFT_CAP_CD/ ACFT_CAP_LEVEL_CD must be unique in
    * BL_FAIL_DEFER_CAP_LEVEL staging table
    *
    */
   @Test
   public void testBLDEF_00370_DuplicateEntry() {
      System.out.println( "======= Starting: " + testName.getMethodName()
            + " Validation ========================" );

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

      // bl_fail_defer_cap_level
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );
      lDeferralRefMap.put( "ACFT_CAP_CD", "'" + iACFT_CAP_CD + "'" );
      lDeferralRefMap.put( "ACFT_CAP_LEVEL_CD", "'" + iACFT_CAP_LEVEL_CD + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_CAP_LEVEL,
            lDeferralRefMap ) );

      // insert map 2nd time
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_CAP_LEVEL,
            lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check for error code
      CheckErrorCode( "BLDEF-00370" );
   }


   /**
    *
    * Test BLDEF-00570 ASSMBL_CD / DEFER_REF_NAME combination must have an entry in BL_FAIL_DEF_REF
    * staging table
    *
    */
   @Test
   public void testBLDEF_00574_1() {
      System.out.println( "======= Starting: " + testName.getMethodName()
            + " Validation ========================" );

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

      // bl_fail_defer_cap_level
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'INVALID'" );
      lDeferralRefMap.put( "ACFT_CAP_CD", "'" + iACFT_CAP_CD + "'" );
      lDeferralRefMap.put( "ACFT_CAP_LEVEL_CD", "'" + iACFT_CAP_LEVEL_CD + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_CAP_LEVEL,
            lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check for error code
      CheckErrorCode( "BLDEF-00574" );
   }


   /**
    *
    * Test BLDEF-00570 ASSMBL_CD / DEFER_REF_NAME combination must have an entry in BL_FAIL_DEF_REF
    * staging table
    *
    */
   @Test
   public void testBLDEF_00574_2() {
      System.out.println( "======= Starting: " + testName.getMethodName()
            + " Validation ========================" );

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

      // bl_fail_defer_cap_level
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_APU + "'" );
      lDeferralRefMap.put( "ACFT_CAP_CD", "'" + iACFT_CAP_CD + "'" );
      lDeferralRefMap.put( "ACFT_CAP_LEVEL_CD", "'" + iACFT_CAP_LEVEL_CD + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_CAP_LEVEL,
            lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check for error code
      CheckErrorCode( "BLDEF-00574" );
   }


   /**
    *
    * Test BLDEF-00570 ASSMBL_CD / DEFER_REF_NAME combination must have an entry in BL_FAIL_DEF_REF
    * staging table Note this time BL_FAIL_DEF_REF is blank
    */
   @Test
   public void testBLDEF_00574_3() {
      System.out.println( "======= Starting: " + testName.getMethodName()
            + " Validation ========================" );

      Map<String, String> lDeferralRefMap = new LinkedHashMap<>();

      // bl_fail_defer_cap_level
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );
      lDeferralRefMap.put( "ACFT_CAP_CD", "'" + iACFT_CAP_CD + "'" );
      lDeferralRefMap.put( "ACFT_CAP_LEVEL_CD", "'" + iACFT_CAP_LEVEL_CD + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_CAP_LEVEL,
            lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check for error code
      CheckErrorCode( "BLDEF-00574" );
   }


   /**
    *
    * Test BLDEF-00740 ACFT_CAP_CD must exist in the ref_acft_cap table
    */
   @Test
   public void testBLDEF_00740_MustExistInRefAcftCapTable() {
      System.out.println( "======= Starting: " + testName.getMethodName()
            + " Validation ========================" );

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

      // bl_fail_defer_cap_level
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );
      lDeferralRefMap.put( "ACFT_CAP_CD", "'" + "INVALID" + "'" );
      lDeferralRefMap.put( "ACFT_CAP_LEVEL_CD", "'" + iACFT_CAP_LEVEL_CD + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_CAP_LEVEL,
            lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check for error code
      CheckErrorCode( "BLDEF-00740" );
   }


   /**
    *
    * Test BLDEF-00750 ACFT_CAP_CD must exist only once in the ref_acft_cap table
    */
   @Test
   public void testBLDEF_00750_MustExistOnceInRefAcftCapTable() {
      System.out.println( "======= Starting: " + testName.getMethodName()
            + " Validation ========================" );

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

      // bl_fail_defer_cap_level
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );
      lDeferralRefMap.put( "ACFT_CAP_CD", "'" + "AUTOTEST" + "'" );
      lDeferralRefMap.put( "ACFT_CAP_LEVEL_CD", "'" + iACFT_CAP_LEVEL_CD + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_CAP_LEVEL,
            lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check for error code
      CheckErrorCode( "BLDEF-00750" );
   }


   /**
    *
    * Test BLDEF-00760 ACFT_CAP_CD / ACFT_CAP_LEVEL_CD must exist in the ref_acft_cap_level table
    */
   @Test
   public void testBLDEF_00760_MustExistInRefAcftCapTable() {
      System.out.println( "======= Starting: " + testName.getMethodName()
            + " Validation ========================" );

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

      // bl_fail_defer_cap_level
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );
      lDeferralRefMap.put( "ACFT_CAP_CD", "'" + iACFT_CAP_CD + "'" );
      lDeferralRefMap.put( "ACFT_CAP_LEVEL_CD", "'" + "YES" + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_CAP_LEVEL,
            lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check for error code
      CheckErrorCode( "BLDEF-00760" );
   }


   /**
    *
    * Test BLDEF-00760 ACFT_CAP_CD / ACFT_CAP_LEVEL_CD must exist in the ref_acft_cap_level table
    */
   @Test
   public void testBLDEF_00770_MustExistOnceInRefAcftCapTable() {
      System.out.println( "======= Starting: " + testName.getMethodName()
            + " Validation ========================" );

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

      // bl_fail_defer_cap_level
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );
      lDeferralRefMap.put( "ACFT_CAP_CD", "'ALAND'" );
      lDeferralRefMap.put( "ACFT_CAP_LEVEL_CD", "'" + "AT" + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_CAP_LEVEL,
            lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check for error code
      CheckErrorCode( "BLDEF-00770" );
   }


   /**
    *
    * Test BLDEF-00771: ACFT_CAP_CD / ACFT_CAP_LEVEL_CD must be associated with specified assembly
    * in the assmbl_cap_levels table.
    */
   @Test
   public void testBLDEF_00771() {
      System.out.println( "======= Starting: " + testName.getMethodName()
            + " Validation ========================" );

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

      // bl_fail_defer_cap_level
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );
      lDeferralRefMap.put( "ACFT_CAP_CD", "'WIFI'" );
      lDeferralRefMap.put( "ACFT_CAP_LEVEL_CD", "'" + "NO" + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_CAP_LEVEL,
            lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check for error code
      CheckErrorCode( "BLDEF-00771" );
   }


   /**
    *
    * Test BLDEF-00940: Deferral reference has not passed all data quality rules in
    * BL_FAIL_DEFER_CAP_LEVEL.
    *
    */
   @Test
   public void testBLDEF_00940_ASSMBL_CD_IsMandatory() {
      System.out.println( "======= Starting: " + testName.getMethodName()
            + " Validation ========================" );

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

      // bl_fail_defer_cap_level
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );
      lDeferralRefMap.put( "ACFT_CAP_CD", "'" + "INVALID" + "'" );
      lDeferralRefMap.put( "ACFT_CAP_LEVEL_CD", "'" + iACFT_CAP_LEVEL_CD + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_CAP_LEVEL,
            lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check for error code
      CheckErrorCode( "BLDEF-00940" );
   }
}
