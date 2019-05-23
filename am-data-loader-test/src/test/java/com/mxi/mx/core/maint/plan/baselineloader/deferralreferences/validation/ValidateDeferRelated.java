package com.mxi.mx.core.maint.plan.baselineloader.deferralreferences.validation;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.mxi.mx.core.maint.plan.baselineloader.deferralreferences.DeferralReferencesTests;
import com.mxi.mx.util.TableUtil;


/**
 * This will perform validation for bl_fail_defer_related table
 */
public class ValidateDeferRelated extends DeferralReferencesTests {

   @Rule
   public TestName testName = new TestName();


   /**
    *
    * This is standard data that is required some of the tests in this class. Two ACFTs are getting
    * loaded into bl_fail_defer_ref table.
    *
    */
   private void LoadACFT_CD1IntoBlFailDeferRefTable() {
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

      // bl_fail_defer_ref of second record for related
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT_RELATED + "'" );
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
   }


   /**
    *
    * This is standard data that is required some of the tests in this class. Two ENGs are getting
    * loaded into bl_fail_defer_ref table.
    *
    */
   private void LoadENG_CD1IntoBlFailDeferRefTable() {
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

      // bl_fail_defer_ref of second record for related
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ENG_RELATED + "'" );
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
   }


   /**
    *
    * This is standard data that is required some of the tests in this class. Two APUs are getting
    * loaded into bl_fail_defer_ref table.
    *
    */
   private void LoadAPU_CD1IntoBlFailDeferRefTable() {
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

      // bl_fail_defer_ref of second record for related
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iAPU_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_APU_RELATED + "'" );
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

   }


   /**
    *
    * test BLDEF-00120 ASSMBL_CD is mandatory
    *
    */
   @Test
   public void testBLDEF_00120_ASSMBL_CD_IsMandatory() {

      System.out.println( "======= Starting: " + testName.getMethodName()
            + " Validation ========================" );

      LoadACFT_CD1IntoBlFailDeferRefTable();

      Map<String, String> lDeferralRelatedMap = new LinkedHashMap<>();

      lDeferralRelatedMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );
      lDeferralRelatedMap.put( "REL_DEFER_REF_NAME",
            "'" + iDEFER_REF_NAME_ACFT_RELATED_MANIX + "'" );

      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_REL, lDeferralRelatedMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check for error code
      CheckErrorCode( "BLDEF-00120" );

   }


   /**
    *
    * test BLDEF-00130 DEFER_REF_NAME is mandatory
    *
    */
   @Test
   public void testBLDEF_00130_DEFER_REF_NAME_IsMandatory() {

      System.out.println( "======= Starting: " + testName.getMethodName()
            + " Validation ========================" );

      LoadENG_CD1IntoBlFailDeferRefTable();

      Map<String, String> lDeferralRelatedMap = new LinkedHashMap<>();

      lDeferralRelatedMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lDeferralRelatedMap.put( "REL_DEFER_REF_NAME",
            "'" + iDEFER_REF_NAME_ENG_RELATED_MANIX + "'" );

      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_REL, lDeferralRelatedMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check for error code
      CheckErrorCode( "BLDEF-00130" );

   }


   /**
    *
    * test BLDEF-00140 REL_DEFER_REF_NAME is mandatory
    *
    */
   @Test
   public void testBLDEF_00140_REL_DEFER_REF_NAME_IsMandatory() {

      System.out.println( "======= Starting: " + testName.getMethodName()
            + " Validation ========================" );

      LoadAPU_CD1IntoBlFailDeferRefTable();

      Map<String, String> lDeferralRelatedMap = new LinkedHashMap<>();

      lDeferralRelatedMap.put( "ASSMBL_CD", "'" + iAPU_ASSMBLCD + "'" );
      lDeferralRelatedMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_APU + "'" );

      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_REL, lDeferralRelatedMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check for error code
      CheckErrorCode( "BLDEF-00140" );

   }


   /**
    *
    * test BLDEF-00340 ASSMBL_CD / DEFER_REF_NAME / REL_DEFER_REF_NAME combination has duplicate
    * entries in BL_FAIL_DEFER_RELATED staging table
    *
    */
   @Test
   public void testBLDEF_00340_DuplicateEntry() {

      System.out.println( "======= Starting: " + testName.getMethodName()
            + " Validation ========================" );

      LoadACFT_CD1IntoBlFailDeferRefTable();

      Map<String, String> lDeferralRelatedMap = new LinkedHashMap<>();

      lDeferralRelatedMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRelatedMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );
      lDeferralRelatedMap.put( "REL_DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT_RELATED + "'" );
      // insert once
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_REL, lDeferralRelatedMap ) );
      // inserts a 2nd time
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_REL, lDeferralRelatedMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check for error code
      CheckErrorCode( "BLDEF-00340" );

   }


   /**
    *
    * Test BLDEF-00570 ASSMBL_CD / DEFER_REF_NAME combination must have an entry in BL_FAIL_DEF_REF
    * staging table
    *
    */
   @Test
   public void testBLDEF_00570_MissingEntry_1() {

      System.out.println( "======= Starting: " + testName.getMethodName()
            + " Validation ========================" );

      LoadACFT_CD1IntoBlFailDeferRefTable();

      Map<String, String> lDeferralRelatedMap = new LinkedHashMap<>();

      lDeferralRelatedMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRelatedMap.put( "DEFER_REF_NAME", "'InvalidEntry'" );
      lDeferralRelatedMap.put( "REL_DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT_RELATED + "'" );
      // insert once
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_REL, lDeferralRelatedMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check for error code
      CheckErrorCode( "BLDEF-00570" );

   }


   /**
    *
    * Test BLDEF-00570 ASSMBL_CD / DEFER_REF_NAME combination must have an entry in BL_FAIL_DEF_REF
    * staging table
    *
    */
   @Test
   public void testBLDEF_00570_MissingEntry_2() {

      System.out.println( "======= Starting: " + testName.getMethodName()
            + " Validation ========================" );

      LoadACFT_CD1IntoBlFailDeferRefTable();

      Map<String, String> lDeferralRelatedMap = new LinkedHashMap<>();

      lDeferralRelatedMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRelatedMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_APU + "'" );
      lDeferralRelatedMap.put( "REL_DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT_RELATED + "'" );
      // insert once
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_REL, lDeferralRelatedMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check for error code
      CheckErrorCode( "BLDEF-00570" );

   }


   /**
    *
    * Test BLDEF-00570 ASSMBL_CD / DEFER_REF_NAME combination must have an entry in BL_FAIL_DEF_REF
    * staging table
    *
    */
   @Test
   public void testBLDEF_00570_MissingEntry_3() {

      System.out.println( "======= Starting: " + testName.getMethodName()
            + " Validation ========================" );

      Map<String, String> lDeferralRelatedMap = new LinkedHashMap<>();

      lDeferralRelatedMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRelatedMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );
      lDeferralRelatedMap.put( "REL_DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT_RELATED + "'" );
      // insert once
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_REL, lDeferralRelatedMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check for error code
      CheckErrorCode( "BLDEF-00570" );

   }


   /**
    *
    * BLDEF-00600 ASSMBL_CD / REL_DEFER_REF_NAME combination must have an entry in BL_FAIL_DEF_REF
    * staging table or in Maintenix in the FAIL_DEFER_REF table.
    *
    */
   @Test
   public void testBLDEF_00600_MustExistInTable_1() {

      System.out.println( "======= Starting: " + testName.getMethodName()
            + " Validation ========================" );

      LoadENG_CD1IntoBlFailDeferRefTable();

      Map<String, String> lDeferralRelatedMap = new LinkedHashMap<>();

      lDeferralRelatedMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lDeferralRelatedMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ENG + "'" );
      lDeferralRelatedMap.put( "REL_DEFER_REF_NAME", "'" + "InvalidEntry" + "'" );

      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_REL, lDeferralRelatedMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check for error code
      CheckErrorCode( "BLDEF-00600" );

   }


   /**
    *
    * BLDEF-00600 ASSMBL_CD / REL_DEFER_REF_NAME combination must have an entry in BL_FAIL_DEF_REF
    * staging table or in Maintenix in the FAIL_DEFER_REF table.
    *
    */
   @Test
   public void testBLDEF_00600_MustExistInTable_2() {

      System.out.println( "======= Starting: " + testName.getMethodName()
            + " Validation ========================" );

      LoadENG_CD1IntoBlFailDeferRefTable();

      Map<String, String> lDeferralRelatedMap = new LinkedHashMap<>();

      lDeferralRelatedMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lDeferralRelatedMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ENG_RELATED + "'" );
      lDeferralRelatedMap.put( "REL_DEFER_REF_NAME", "'" + "InvalidEntry" + "'" );

      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_REL, lDeferralRelatedMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check for error code
      CheckErrorCode( "BLDEF-00600" );

   }


   /**
    * BLDEF-00910, Deferral reference has not passed all data quality rules in
    * BL_FAIL_DEFER_RELATED.
    *
    */
   @Test
   public void testBLDEF_00910_DependncyError() {

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

      // bl_fail_defer_ref of second record for related
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iAPU_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_APU_RELATED + "'" );
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

      Map<String, String> lDeferralRelatedMap = new LinkedHashMap<>();

      lDeferralRelatedMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRelatedMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );
      lDeferralRelatedMap.put( "REL_DEFER_REF_NAME", "'" + iDEFER_REF_NAME_APU_RELATED + "'" );

      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_REL, lDeferralRelatedMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check for error code
      CheckErrorCode( "BLDEF-00910" );

   }


   @Test
   public void testBLDEF_00615_DifferentDeferName_1() {

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

      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT_RELATED + "'" );
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

      Map<String, String> lDeferralRelatedMap = new LinkedHashMap<>();

      lDeferralRelatedMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRelatedMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );
      lDeferralRelatedMap.put( "REL_DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );

      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_REL, lDeferralRelatedMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check for error code
      CheckErrorCode( "BLDEF-00615" );

   }


   @Test
   public void testBLDEF_00615_DifferentDeferName_2() {
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

      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT_RELATED + "'" );
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

      Map<String, String> lDeferralRelatedMap = new LinkedHashMap<>();

      lDeferralRelatedMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRelatedMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT_RELATED + "'" );
      lDeferralRelatedMap.put( "REL_DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT_RELATED + "'" );

      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_REL, lDeferralRelatedMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Check for error code
      CheckErrorCode( "BLDEF-00615" );

   }
}
