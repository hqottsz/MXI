package com.mxi.mx.core.maint.plan.baselineloader.deferralreferences;

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


/**
 * This test suite contains test cases on validation and import functionality on Deferral
 * References.
 *
 * @author ALICIA QIAN
 */
public class Importdeferralreferences extends DeferralReferencesTests {

   @Rule
   public TestName testName = new TestName();


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
    * This test is to verify validation of deferral references of ACFT staging tables:
    * bl_fail_defer_ref, bl_fail_defer_conflict, bl_fail_defer_related, bl_fail_defer_insp,
    * bl_fail_defer_dead and bl_fail_defer_cap_level
    *
    *
    */

   public void testACFT_DEFER_VALIDATION() {

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
      lDeferralRefMap.put( "MOC_APPROVAL_BOOL", "'1'" );

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
      lDeferralRefMap.put( "MOC_APPROVAL_BOOL", "'0'" );

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

      // bl_fail_defer_related
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );
      lDeferralRefMap.put( "REL_DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT_RELATED + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_REL, lDeferralRefMap ) );

      // bl_fail_defer_insp;
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );
      lDeferralRefMap.put( "INSP_REQ_CONFIG_SLOT", null );
      lDeferralRefMap.put( "INSP_REQ_TASK_CD", "'" + iACFT_INSP_REQ_TASK_CD_1 + "'" );
      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_INSP, lDeferralRefMap ) );

      // bl_fail_defer_dead
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );
      lDeferralRefMap.put( "DATA_TYPE_CD", "'" + iACFT_DATA_TYPE_CD_HOURS + "'" );
      lDeferralRefMap.put( "DEAD_QT", "'" + iACFT_DEAD_QT + "'" );
      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_DEAD, lDeferralRefMap ) );

      // bl_fail_defer_cap_level
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );
      lDeferralRefMap.put( "ACFT_CAP_CD", "'" + iACFT_CAP_CD + "'" );
      lDeferralRefMap.put( "ACFT_CAP_LEVEL_CD", "'" + iACFT_CAP_LEVEL_CD + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_CAP_LEVEL,
            lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // Check error code
      CheckErrorCode( "PASS" );

   }


   /**
    * This test is to verify validation of deferral references of ENG staging tables:
    * bl_fail_defer_ref, bl_fail_defer_conflict, bl_fail_defer_related, bl_fail_defer_insp,
    * bl_fail_defer_dead and bl_fail_defer_cap_level
    *
    *
    */

   public void testENG_DEFER_VALIDATION() {

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
      lDeferralRefMap.put( "MOC_APPROVAL_BOOL", "'Y'" );

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
      lDeferralRefMap.put( "MOC_APPROVAL_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_REF, lDeferralRefMap ) );

      // bl_fail_defer_conflict
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ENG + "'" );
      lDeferralRefMap.put( "CONFLICT_DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ENG_CONFLICT + "'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_CONFLICT, lDeferralRefMap ) );

      // bl_fail_defer_related
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ENG + "'" );
      lDeferralRefMap.put( "REL_DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ENG_RELATED + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_REL, lDeferralRefMap ) );

      // bl_fail_defer_insp;
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ENG + "'" );
      lDeferralRefMap.put( "INSP_REQ_CONFIG_SLOT", null );
      lDeferralRefMap.put( "INSP_REQ_TASK_CD", "'" + iENG_INSP_REQ_TASK_CD + "'" );
      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_INSP, lDeferralRefMap ) );

      // bl_fail_defer_dead
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ENG + "'" );
      lDeferralRefMap.put( "DATA_TYPE_CD", "'" + iDATA_TYPE_CD_CDY + "'" );
      lDeferralRefMap.put( "DEAD_QT", "'" + iENG_DEAD_QT + "'" );
      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_DEAD, lDeferralRefMap ) );

      // bl_fail_defer_cap_level
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ENG + "'" );
      lDeferralRefMap.put( "ACFT_CAP_CD", "'" + iACFT_CAP_CD + "'" );
      lDeferralRefMap.put( "ACFT_CAP_LEVEL_CD", "'" + iACFT_CAP_LEVEL_CD + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_CAP_LEVEL,
            lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // Check error code
      CheckErrorCode( "PASS" );

   }


   /**
    * This test is to verify validation of deferral references of APU staging tables:
    * bl_fail_defer_ref, bl_fail_defer_conflict, bl_fail_defer_related, bl_fail_defer_insp,
    * bl_fail_defer_dead and bl_fail_defer_cap_level
    *
    *
    */

   public void testAPU_DEFER_VALIDATION() {

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
      lDeferralRefMap.put( "MOC_APPROVAL_BOOL", "'T'" );

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
      lDeferralRefMap.put( "MOC_APPROVAL_BOOL", "'F'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_REF, lDeferralRefMap ) );

      // bl_fail_defer_conflict
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iAPU_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_APU + "'" );
      lDeferralRefMap.put( "CONFLICT_DEFER_REF_NAME", "'" + iDEFER_REF_NAME_APU_CONFLICT + "'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_CONFLICT, lDeferralRefMap ) );

      // bl_fail_defer_related
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iAPU_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_APU + "'" );
      lDeferralRefMap.put( "REL_DEFER_REF_NAME", "'" + iDEFER_REF_NAME_APU_RELATED + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_REL, lDeferralRefMap ) );

      // bl_fail_defer_insp;
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iAPU_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_APU + "'" );
      lDeferralRefMap.put( "INSP_REQ_CONFIG_SLOT", null );
      lDeferralRefMap.put( "INSP_REQ_TASK_CD", "'" + iAPU_INSP_REQ_TASK_CD + "'" );
      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_INSP, lDeferralRefMap ) );

      // bl_fail_defer_dead
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iAPU_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_APU + "'" );
      lDeferralRefMap.put( "DATA_TYPE_CD", "'" + iDATA_TYPE_CD_CDY + "'" );
      lDeferralRefMap.put( "DEAD_QT", "'" + iAPU_DEAD_QT + "'" );
      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_DEAD, lDeferralRefMap ) );

      // bl_fail_defer_cap_level
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iAPU_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_APU + "'" );
      lDeferralRefMap.put( "ACFT_CAP_CD", "'" + iACFT_CAP_CD + "'" );
      lDeferralRefMap.put( "ACFT_CAP_LEVEL_CD", "'" + iACFT_CAP_LEVEL_CD + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_CAP_LEVEL,
            lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // Check error code
      CheckErrorCode( "PASS" );

   }


   /**
    * This test is to verify import functionality of deferral references of ACFT MX tables:
    * fail_defer_ref, fail_defer_conflict, fail_defer_rel, fail_defer_insp, fail_defer_dead and
    * fail_defer_cap_level
    *
    *
    */

   @Test
   public void testACFT_DEFER_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testACFT_DEFER_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Cleanup all IDs
      setIDsNull();

      // verify fail_defer_ref
      ALT_ASSMBL_BOM_ID = getAltBomID( iACFT_ASSMBLCD, iACFT_CONFIG_SLOT_CD );
      ID_REF = check_fail_defer_ref_table( iDEFER_REF_LDESC, iDEFER_REF_NAME_ACFT, iACFT_ASSMBLCD,
            iFAIL_SEV_CD, iFAIL_DEFER_CD, iDEFER_REF_STATUS_CD, iINST_SYSTEMS_QT,
            iDISPATCH_SYSTEMS_QT, iAPPL_LDESC, iOPER_RESTRICTIONS_LDESC, iMAINT_ACTIONS_LDESC,
            iPERF_PENALTIES_LDESC, ALT_ASSMBL_BOM_ID, "1" );
      ID_ALT = getALT_ID( ID_REF );

      ID_REF_CONFLICT = check_fail_defer_ref_table( iDEFER_REF_LDESC, iDEFER_REF_NAME_ACFT_CONFLICT,
            iACFT_ASSMBLCD, iFAIL_SEV_CD, iFAIL_DEFER_CD, iDEFER_REF_STATUS_CD, iINST_SYSTEMS_QT,
            iDISPATCH_SYSTEMS_QT, iAPPL_LDESC, iOPER_RESTRICTIONS_LDESC, iMAINT_ACTIONS_LDESC,
            iPERF_PENALTIES_LDESC, ALT_ASSMBL_BOM_ID, "1" );
      ID_ALT_CONFLICT = getALT_ID( ID_REF_CONFLICT );

      ID_REF_RELATED = check_fail_defer_ref_table( iDEFER_REF_LDESC, iDEFER_REF_NAME_ACFT_RELATED,
            iACFT_ASSMBLCD, iFAIL_SEV_CD, iFAIL_DEFER_CD, iDEFER_REF_STATUS_CD, iINST_SYSTEMS_QT,
            iDISPATCH_SYSTEMS_QT, iAPPL_LDESC, iOPER_RESTRICTIONS_LDESC, iMAINT_ACTIONS_LDESC,
            iPERF_PENALTIES_LDESC, ALT_ASSMBL_BOM_ID, "0" );
      ID_ALT_RELATED = getALT_ID( ID_REF_RELATED );

      // Verify fail_defer_carrier
      simpleIDs carrierID_1 =
            getCarrierIds( iOPERATOR_CD_LIST_1, iOPERATOR_CD_LIST_1, iOPERATOR_CD_LIST_1 );
      simpleIDs carrierID_2 =
            getCarrierIds( iOPERATOR_CD_LIST_2, iOPERATOR_CD_LIST_1, iOPERATOR_CD_LIST_1 );

      // check MXI exists in table
      String lQuery = "select 1 from " + TableUtil.FAIL_DEFER_CARRIER
            + " where FAIL_DEFER_REF_DB_ID=" + ID_REF.getNO_DB_ID() + " and FAIL_DEFER_REF_ID="
            + ID_REF.getNO_ID() + " and CARRIER_DB_ID=" + carrierID_1.getNO_DB_ID()
            + " and CARRIER_ID=" + carrierID_1.getNO_ID();
      Assert.assertTrue( "Check fail_defer_carrier table to verify the record exists",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.FAIL_DEFER_CARRIER + " where FAIL_DEFER_REF_DB_ID="
            + ID_REF_CONFLICT.getNO_DB_ID() + " and FAIL_DEFER_REF_ID=" + ID_REF_CONFLICT.getNO_ID()
            + " and CARRIER_DB_ID=" + carrierID_1.getNO_DB_ID() + " and CARRIER_ID="
            + carrierID_1.getNO_ID();
      Assert.assertTrue( "Check fail_defer_carrier table to verify the record exists",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.FAIL_DEFER_CARRIER + " where FAIL_DEFER_REF_DB_ID="
            + ID_REF_RELATED.getNO_DB_ID() + " and FAIL_DEFER_REF_ID=" + ID_REF_RELATED.getNO_ID()
            + " and CARRIER_DB_ID=" + carrierID_1.getNO_DB_ID() + " and CARRIER_ID="
            + carrierID_1.getNO_ID();
      Assert.assertTrue( "Check fail_defer_carrier table to verify the record exists",
            RecordsExist( lQuery ) );

      // check ATLD exists in table
      lQuery = "select 1 from " + TableUtil.FAIL_DEFER_CARRIER + " where FAIL_DEFER_REF_DB_ID="
            + ID_REF.getNO_DB_ID() + " and FAIL_DEFER_REF_ID=" + ID_REF.getNO_ID()
            + " and CARRIER_DB_ID=" + carrierID_2.getNO_DB_ID() + " and CARRIER_ID="
            + carrierID_2.getNO_ID();
      Assert.assertTrue( "Check fail_defer_carrier table to verify the record exists",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.FAIL_DEFER_CARRIER + " where FAIL_DEFER_REF_DB_ID="
            + ID_REF_CONFLICT.getNO_DB_ID() + " and FAIL_DEFER_REF_ID=" + ID_REF_CONFLICT.getNO_ID()
            + " and CARRIER_DB_ID=" + carrierID_2.getNO_DB_ID() + " and CARRIER_ID="
            + carrierID_2.getNO_ID();
      Assert.assertTrue( "Check fail_defer_carrier table to verify the record exists",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.FAIL_DEFER_CARRIER + " where FAIL_DEFER_REF_DB_ID="
            + ID_REF_RELATED.getNO_DB_ID() + " and FAIL_DEFER_REF_ID=" + ID_REF_RELATED.getNO_ID()
            + " and CARRIER_DB_ID=" + carrierID_2.getNO_DB_ID() + " and CARRIER_ID="
            + carrierID_2.getNO_ID();
      Assert.assertTrue( "Check fail_defer_carrier table to verify the record exists",
            RecordsExist( lQuery ) );

      // Check fail_defer_ref_rel_def
      lQuery = "select 1 from " + TableUtil.FAIL_DEFER_REF_REL_DEF + " where FAIL_DEFER_REF_ID='"
            + ID_ALT + "' and REL_FAIL_DEFER_REF_ID='" + ID_ALT_RELATED + "'";

      Assert.assertTrue( "Check FAIL_DEFER_REF_REL_DEF table to verify the record exists",
            RecordsExist( lQuery ) );

      // Check fail_defer_ref_conflict_def
      lQuery =
            "select 1 from " + TableUtil.FAIL_DEFER_REF_CONFLICT_DEF + " where FAIL_DEFER_REF_ID='"
                  + ID_ALT + "' and CONFLICT_FAIL_DEFER_REF_ID='" + ID_ALT_CONFLICT + "'";

      Assert.assertTrue( "Check fail_defer_ref_conflict_def table to verify the record exists",
            RecordsExist( lQuery ) );

      // check fail_defer_ref_dead
      simpleIDs ldataTypeIds = getDataTypeIDs( "US", "HOUR", iACFT_DATA_TYPE_CD_HOURS );
      check_fail_defer_ref_dead( ID_ALT, ldataTypeIds, iACFT_DEAD_QT );

      // check fail_defer_ref_task_defn
      String ltaskAlt = getAltTASKID( iACFT_INSP_REQ_TASK_CD_1 );
      lQuery = "select 1 from " + TableUtil.FAIL_DEFER_REF_TASK_DEFN + " where FAIL_DEFER_REF_ID='"
            + ID_ALT + "' and TASK_DEFN_ID='" + ltaskAlt + "'";

      Assert.assertTrue( "Check fail_defer_ref_task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // check fail_defer_ref_degrad_cap
      check_fail_defer_ref_degrad_cap( ID_ALT, iACFT_CAP_CD, iACFT_CAP_LEVEL_CD );

   }


   /**
    * This test is to verify import functionality of deferral references of ENG MX tables:
    * fail_defer_ref, fail_defer_conflict, fail_defer_rel, fail_defer_insp, fail_defer_dead and
    * fail_defer_cap_level
    *
    *
    */
   @Test
   public void testENG_DEFER_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testENG_DEFER_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Cleanup all IDs
      setIDsNull();

      // verify fail_defer_ref
      ALT_ASSMBL_BOM_ID = getAltBomID( iENG_ASSMBLCD, iENG_CONFIG_SLOT_CD );
      ID_REF = check_fail_defer_ref_table( iDEFER_REF_LDESC, iDEFER_REF_NAME_ENG, iENG_ASSMBLCD,
            iFAIL_SEV_CD, iFAIL_DEFER_CD, iDEFER_REF_STATUS_CD, iINST_SYSTEMS_QT,
            iDISPATCH_SYSTEMS_QT, iAPPL_LDESC, iOPER_RESTRICTIONS_LDESC, iMAINT_ACTIONS_LDESC,
            iPERF_PENALTIES_LDESC, ALT_ASSMBL_BOM_ID, "1" );
      ID_ALT = getALT_ID( ID_REF );

      ID_REF_CONFLICT = check_fail_defer_ref_table( iDEFER_REF_LDESC, iDEFER_REF_NAME_ENG_CONFLICT,
            iENG_ASSMBLCD, iFAIL_SEV_CD, iFAIL_DEFER_CD, iDEFER_REF_STATUS_CD, iINST_SYSTEMS_QT,
            iDISPATCH_SYSTEMS_QT, iAPPL_LDESC, iOPER_RESTRICTIONS_LDESC, iMAINT_ACTIONS_LDESC,
            iPERF_PENALTIES_LDESC, ALT_ASSMBL_BOM_ID, "1" );
      ID_ALT_CONFLICT = getALT_ID( ID_REF_CONFLICT );

      ID_REF_RELATED = check_fail_defer_ref_table( iDEFER_REF_LDESC, iDEFER_REF_NAME_ENG_RELATED,
            iENG_ASSMBLCD, iFAIL_SEV_CD, iFAIL_DEFER_CD, iDEFER_REF_STATUS_CD, iINST_SYSTEMS_QT,
            iDISPATCH_SYSTEMS_QT, iAPPL_LDESC, iOPER_RESTRICTIONS_LDESC, iMAINT_ACTIONS_LDESC,
            iPERF_PENALTIES_LDESC, ALT_ASSMBL_BOM_ID, "0" );
      ID_ALT_RELATED = getALT_ID( ID_REF_RELATED );

      // Verify fail_defer_carrier
      simpleIDs carrierID_1 =
            getCarrierIds( iOPERATOR_CD_LIST_1, iOPERATOR_CD_LIST_1, iOPERATOR_CD_LIST_1 );
      simpleIDs carrierID_2 =
            getCarrierIds( iOPERATOR_CD_LIST_2, iOPERATOR_CD_LIST_1, iOPERATOR_CD_LIST_1 );

      // check MXI exists in table
      String lQuery = "select 1 from " + TableUtil.FAIL_DEFER_CARRIER
            + " where FAIL_DEFER_REF_DB_ID=" + ID_REF.getNO_DB_ID() + " and FAIL_DEFER_REF_ID="
            + ID_REF.getNO_ID() + " and CARRIER_DB_ID=" + carrierID_1.getNO_DB_ID()
            + " and CARRIER_ID=" + carrierID_1.getNO_ID();
      Assert.assertTrue( "Check fail_defer_carrier table to verify the record exists",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.FAIL_DEFER_CARRIER + " where FAIL_DEFER_REF_DB_ID="
            + ID_REF_CONFLICT.getNO_DB_ID() + " and FAIL_DEFER_REF_ID=" + ID_REF_CONFLICT.getNO_ID()
            + " and CARRIER_DB_ID=" + carrierID_1.getNO_DB_ID() + " and CARRIER_ID="
            + carrierID_1.getNO_ID();
      Assert.assertTrue( "Check fail_defer_carrier table to verify the record exists",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.FAIL_DEFER_CARRIER + " where FAIL_DEFER_REF_DB_ID="
            + ID_REF_RELATED.getNO_DB_ID() + " and FAIL_DEFER_REF_ID=" + ID_REF_RELATED.getNO_ID()
            + " and CARRIER_DB_ID=" + carrierID_1.getNO_DB_ID() + " and CARRIER_ID="
            + carrierID_1.getNO_ID();
      Assert.assertTrue( "Check fail_defer_carrier table to verify the record exists",
            RecordsExist( lQuery ) );

      // check ATLD exists in table
      lQuery = "select 1 from " + TableUtil.FAIL_DEFER_CARRIER + " where FAIL_DEFER_REF_DB_ID="
            + ID_REF.getNO_DB_ID() + " and FAIL_DEFER_REF_ID=" + ID_REF.getNO_ID()
            + " and CARRIER_DB_ID=" + carrierID_2.getNO_DB_ID() + " and CARRIER_ID="
            + carrierID_2.getNO_ID();
      Assert.assertTrue( "Check fail_defer_carrier table to verify the record exists",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.FAIL_DEFER_CARRIER + " where FAIL_DEFER_REF_DB_ID="
            + ID_REF_CONFLICT.getNO_DB_ID() + " and FAIL_DEFER_REF_ID=" + ID_REF_CONFLICT.getNO_ID()
            + " and CARRIER_DB_ID=" + carrierID_2.getNO_DB_ID() + " and CARRIER_ID="
            + carrierID_2.getNO_ID();
      Assert.assertTrue( "Check fail_defer_carrier table to verify the record exists",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.FAIL_DEFER_CARRIER + " where FAIL_DEFER_REF_DB_ID="
            + ID_REF_RELATED.getNO_DB_ID() + " and FAIL_DEFER_REF_ID=" + ID_REF_RELATED.getNO_ID()
            + " and CARRIER_DB_ID=" + carrierID_2.getNO_DB_ID() + " and CARRIER_ID="
            + carrierID_2.getNO_ID();
      Assert.assertTrue( "Check fail_defer_carrier table to verify the record exists",
            RecordsExist( lQuery ) );

      // Check fail_defer_ref_rel_def
      lQuery = "select 1 from " + TableUtil.FAIL_DEFER_REF_REL_DEF + " where FAIL_DEFER_REF_ID='"
            + ID_ALT + "' and REL_FAIL_DEFER_REF_ID='" + ID_ALT_RELATED + "'";

      Assert.assertTrue( "Check FAIL_DEFER_REF_REL_DEF table to verify the record exists",
            RecordsExist( lQuery ) );

      // Check fail_defer_ref_conflict_def
      lQuery =
            "select 1 from " + TableUtil.FAIL_DEFER_REF_CONFLICT_DEF + " where FAIL_DEFER_REF_ID='"
                  + ID_ALT + "' and CONFLICT_FAIL_DEFER_REF_ID='" + ID_ALT_CONFLICT + "'";

      Assert.assertTrue( "Check fail_defer_ref_conflict_def table to verify the record exists",
            RecordsExist( lQuery ) );

      // check fail_defer_ref_dead
      simpleIDs ldataTypeIds = getDataTypeIDs( "CA", "DAY", iDATA_TYPE_CD_CDY );
      check_fail_defer_ref_dead( ID_ALT, ldataTypeIds, iACFT_DEAD_QT );

      // check fail_defer_ref_task_defn
      String ltaskAlt = getAltTASKID( iENG_INSP_REQ_TASK_CD );
      lQuery = "select 1 from " + TableUtil.FAIL_DEFER_REF_TASK_DEFN + " where FAIL_DEFER_REF_ID='"
            + ID_ALT + "' and TASK_DEFN_ID='" + ltaskAlt + "'";

      Assert.assertTrue( "Check fail_defer_ref_task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // check fail_defer_ref_degrad_cap
      check_fail_defer_ref_degrad_cap( ID_ALT, iACFT_CAP_CD, iACFT_CAP_LEVEL_CD );

   }


   /**
    * This test is to verify import functionality of deferral references of APU MX tables:
    * fail_defer_ref, fail_defer_conflict, fail_defer_rel, fail_defer_insp, fail_defer_dead and
    * fail_defer_cap_level
    *
    *
    */

   @Test
   public void testAPU_DEFER_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testAPU_DEFER_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Cleanup all IDs
      setIDsNull();

      // verify fail_defer_ref
      ALT_ASSMBL_BOM_ID = getAltBomID( iAPU_ASSMBLCD, iAPU_CONFIG_SLOT_CD );
      ID_REF = check_fail_defer_ref_table( iDEFER_REF_LDESC, iDEFER_REF_NAME_APU, iAPU_ASSMBLCD,
            iFAIL_SEV_CD, iFAIL_DEFER_CD, iDEFER_REF_STATUS_CD, iINST_SYSTEMS_QT,
            iDISPATCH_SYSTEMS_QT, iAPPL_LDESC, iOPER_RESTRICTIONS_LDESC, iMAINT_ACTIONS_LDESC,
            iPERF_PENALTIES_LDESC, ALT_ASSMBL_BOM_ID, "1" );
      ID_ALT = getALT_ID( ID_REF );

      ID_REF_CONFLICT = check_fail_defer_ref_table( iDEFER_REF_LDESC, iDEFER_REF_NAME_APU_CONFLICT,
            iAPU_ASSMBLCD, iFAIL_SEV_CD, iFAIL_DEFER_CD, iDEFER_REF_STATUS_CD, iINST_SYSTEMS_QT,
            iDISPATCH_SYSTEMS_QT, iAPPL_LDESC, iOPER_RESTRICTIONS_LDESC, iMAINT_ACTIONS_LDESC,
            iPERF_PENALTIES_LDESC, ALT_ASSMBL_BOM_ID, "1" );
      ID_ALT_CONFLICT = getALT_ID( ID_REF_CONFLICT );

      ID_REF_RELATED = check_fail_defer_ref_table( iDEFER_REF_LDESC, iDEFER_REF_NAME_APU_RELATED,
            iAPU_ASSMBLCD, iFAIL_SEV_CD, iFAIL_DEFER_CD, iDEFER_REF_STATUS_CD, iINST_SYSTEMS_QT,
            iDISPATCH_SYSTEMS_QT, iAPPL_LDESC, iOPER_RESTRICTIONS_LDESC, iMAINT_ACTIONS_LDESC,
            iPERF_PENALTIES_LDESC, ALT_ASSMBL_BOM_ID, "0" );
      ID_ALT_RELATED = getALT_ID( ID_REF_RELATED );

      // Verify fail_defer_carrier
      simpleIDs carrierID_1 =
            getCarrierIds( iOPERATOR_CD_LIST_1, iOPERATOR_CD_LIST_1, iOPERATOR_CD_LIST_1 );
      simpleIDs carrierID_2 =
            getCarrierIds( iOPERATOR_CD_LIST_2, iOPERATOR_CD_LIST_1, iOPERATOR_CD_LIST_1 );

      // check MXI exists in table
      String lQuery = "select 1 from " + TableUtil.FAIL_DEFER_CARRIER
            + " where FAIL_DEFER_REF_DB_ID=" + ID_REF.getNO_DB_ID() + " and FAIL_DEFER_REF_ID="
            + ID_REF.getNO_ID() + " and CARRIER_DB_ID=" + carrierID_1.getNO_DB_ID()
            + " and CARRIER_ID=" + carrierID_1.getNO_ID();
      Assert.assertTrue( "Check fail_defer_carrier table to verify the record exists",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.FAIL_DEFER_CARRIER + " where FAIL_DEFER_REF_DB_ID="
            + ID_REF_CONFLICT.getNO_DB_ID() + " and FAIL_DEFER_REF_ID=" + ID_REF_CONFLICT.getNO_ID()
            + " and CARRIER_DB_ID=" + carrierID_1.getNO_DB_ID() + " and CARRIER_ID="
            + carrierID_1.getNO_ID();
      Assert.assertTrue( "Check fail_defer_carrier table to verify the record exists",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.FAIL_DEFER_CARRIER + " where FAIL_DEFER_REF_DB_ID="
            + ID_REF_RELATED.getNO_DB_ID() + " and FAIL_DEFER_REF_ID=" + ID_REF_RELATED.getNO_ID()
            + " and CARRIER_DB_ID=" + carrierID_1.getNO_DB_ID() + " and CARRIER_ID="
            + carrierID_1.getNO_ID();
      Assert.assertTrue( "Check fail_defer_carrier table to verify the record exists",
            RecordsExist( lQuery ) );

      // check ATLD exists in table
      lQuery = "select 1 from " + TableUtil.FAIL_DEFER_CARRIER + " where FAIL_DEFER_REF_DB_ID="
            + ID_REF.getNO_DB_ID() + " and FAIL_DEFER_REF_ID=" + ID_REF.getNO_ID()
            + " and CARRIER_DB_ID=" + carrierID_2.getNO_DB_ID() + " and CARRIER_ID="
            + carrierID_2.getNO_ID();
      Assert.assertTrue( "Check fail_defer_carrier table to verify the record exists",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.FAIL_DEFER_CARRIER + " where FAIL_DEFER_REF_DB_ID="
            + ID_REF_CONFLICT.getNO_DB_ID() + " and FAIL_DEFER_REF_ID=" + ID_REF_CONFLICT.getNO_ID()
            + " and CARRIER_DB_ID=" + carrierID_2.getNO_DB_ID() + " and CARRIER_ID="
            + carrierID_2.getNO_ID();
      Assert.assertTrue( "Check fail_defer_carrier table to verify the record exists",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.FAIL_DEFER_CARRIER + " where FAIL_DEFER_REF_DB_ID="
            + ID_REF_RELATED.getNO_DB_ID() + " and FAIL_DEFER_REF_ID=" + ID_REF_RELATED.getNO_ID()
            + " and CARRIER_DB_ID=" + carrierID_2.getNO_DB_ID() + " and CARRIER_ID="
            + carrierID_2.getNO_ID();
      Assert.assertTrue( "Check fail_defer_carrier table to verify the record exists",
            RecordsExist( lQuery ) );

      // Check fail_defer_ref_rel_def
      lQuery = "select 1 from " + TableUtil.FAIL_DEFER_REF_REL_DEF + " where FAIL_DEFER_REF_ID='"
            + ID_ALT + "' and REL_FAIL_DEFER_REF_ID='" + ID_ALT_RELATED + "'";
      Assert.assertTrue( "Check FAIL_DEFER_REF_REL_DEF table to verify the record exists",
            RecordsExist( lQuery ) );

      // Check fail_defer_ref_conflict_def
      lQuery =
            "select 1 from " + TableUtil.FAIL_DEFER_REF_CONFLICT_DEF + " where FAIL_DEFER_REF_ID='"
                  + ID_ALT + "' and CONFLICT_FAIL_DEFER_REF_ID='" + ID_ALT_CONFLICT + "'";

      Assert.assertTrue( "Check fail_defer_ref_conflict_def table to verify the record exists",
            RecordsExist( lQuery ) );

      // check fail_defer_ref_dead
      simpleIDs ldataTypeIds = getDataTypeIDs( "CA", "DAY", iDATA_TYPE_CD_CDY );
      check_fail_defer_ref_dead( ID_ALT, ldataTypeIds, iACFT_DEAD_QT );

      // check fail_defer_ref_task_defn
      String ltaskAlt = getAltTASKID( iAPU_INSP_REQ_TASK_CD );
      lQuery = "select 1 from " + TableUtil.FAIL_DEFER_REF_TASK_DEFN + " where FAIL_DEFER_REF_ID='"
            + ID_ALT + "' and TASK_DEFN_ID='" + ltaskAlt + "'";

      Assert.assertTrue( "Check fail_defer_ref_task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // check fail_defer_ref_degrad_cap
      check_fail_defer_ref_degrad_cap( ID_ALT, iACFT_CAP_CD, iACFT_CAP_LEVEL_CD );

   }


   /**
    * This test is to verify validation of deferral references of ACFT staging tables:
    * bl_fail_defer_ref, bl_fail_defer_conflict, bl_fail_defer_related, bl_fail_defer_insp,
    * bl_fail_defer_dead and bl_fail_defer_cap_level This test case is same as
    * testACFT_DEFER_VALIDATION(), but with lower cases.
    *
    *
    */

   public void testACFT_DEFER_LOWERCASE_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lDeferralRefMap = new LinkedHashMap<>();

      // bl_fail_defer_ref of first record
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'deferacft'" );
      lDeferralRefMap.put( "CONFIG_SLOT_CD", "'" + iACFT_CONFIG_SLOT_CD + "'" );
      lDeferralRefMap.put( "OPERATOR_CD_LIST", "'" + iOPERATOR_CD_LIST + "'" );
      lDeferralRefMap.put( "FAIL_SEV_CD", "'" + iFAIL_SEV_CD + "'" );
      lDeferralRefMap.put( "FAIL_DEFER_CD", "'" + iFAIL_DEFER_CD + "'" );
      lDeferralRefMap.put( "DEFER_REF_LDESC", "'auto defer test'" );
      lDeferralRefMap.put( "DEFER_REF_STATUS_CD", "'" + iDEFER_REF_STATUS_CD + "'" );
      lDeferralRefMap.put( "INST_SYSTEMS_QT", "'" + iINST_SYSTEMS_QT + "'" );
      lDeferralRefMap.put( "DISPATCH_SYSTEMS_QT", "'" + iDISPATCH_SYSTEMS_QT + "'" );
      lDeferralRefMap.put( "APPL_LDESC", "'" + iAPPL_LDESC + "'" );
      lDeferralRefMap.put( "OPER_RESTRICTIONS_LDESC", "'auto restriction test'" );
      lDeferralRefMap.put( "PERF_PENALTIES_LDESC", "'auto perf test'" );
      lDeferralRefMap.put( "MAINT_ACTIONS_LDESC", "'auto action test'" );

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

      // bl_fail_defer_conflict
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'deferacft'" );
      lDeferralRefMap.put( "CONFLICT_DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT_CONFLICT + "'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_CONFLICT, lDeferralRefMap ) );

      // bl_fail_defer_related
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'deferacft'" );
      lDeferralRefMap.put( "REL_DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT_RELATED + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_REL, lDeferralRefMap ) );

      // bl_fail_defer_insp;
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );
      lDeferralRefMap.put( "INSP_REQ_CONFIG_SLOT", null );
      lDeferralRefMap.put( "INSP_REQ_TASK_CD", "'" + iACFT_INSP_REQ_TASK_CD_1 + "'" );
      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_INSP, lDeferralRefMap ) );

      // bl_fail_defer_dead
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'deferacft'" );
      lDeferralRefMap.put( "DATA_TYPE_CD", "'" + iACFT_DATA_TYPE_CD_HOURS + "'" );
      lDeferralRefMap.put( "DEAD_QT", "'" + iACFT_DEAD_QT + "'" );
      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_DEAD, lDeferralRefMap ) );

      // bl_fail_defer_cap_level
      lDeferralRefMap.clear();
      lDeferralRefMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lDeferralRefMap.put( "DEFER_REF_NAME", "'" + iDEFER_REF_NAME_ACFT + "'" );
      lDeferralRefMap.put( "ACFT_CAP_CD", "'" + iACFT_CAP_CD + "'" );
      lDeferralRefMap.put( "ACFT_CAP_LEVEL_CD", "'" + iACFT_CAP_LEVEL_CD + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_CAP_LEVEL,
            lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // Check error code
      CheckErrorCode( "PASS" );

   }


   /**
    * This test is to verify import functionality of deferral references of ACFT MX tables:
    * fail_defer_ref, fail_defer_conflict, fail_defer_rel, fail_defer_insp, fail_defer_dead and
    * fail_defer_cap_level This test case is same as testACFT_DEFER_IMPORT() with lower case.
    *
    *
    */

   @Test
   public void testACFT_DEFER_LOWERCASE_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testACFT_DEFER_LOWERCASE_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Cleanup all IDs
      setIDsNull();

      // verify fail_defer_ref
      ALT_ASSMBL_BOM_ID = getAltBomID( iACFT_ASSMBLCD, iACFT_CONFIG_SLOT_CD );
      ID_REF = check_fail_defer_ref_table( "auto defer test", "deferacft", iACFT_ASSMBLCD,
            iFAIL_SEV_CD, iFAIL_DEFER_CD, iDEFER_REF_STATUS_CD, iINST_SYSTEMS_QT,
            iDISPATCH_SYSTEMS_QT, iAPPL_LDESC, "auto restriction test", "auto action test",
            "auto perf test", ALT_ASSMBL_BOM_ID, "1" );
      ID_ALT = getALT_ID( ID_REF );

      ID_REF_CONFLICT = check_fail_defer_ref_table( iDEFER_REF_LDESC, iDEFER_REF_NAME_ACFT_CONFLICT,
            iACFT_ASSMBLCD, iFAIL_SEV_CD, iFAIL_DEFER_CD, iDEFER_REF_STATUS_CD, iINST_SYSTEMS_QT,
            iDISPATCH_SYSTEMS_QT, iAPPL_LDESC, iOPER_RESTRICTIONS_LDESC, iMAINT_ACTIONS_LDESC,
            iPERF_PENALTIES_LDESC, ALT_ASSMBL_BOM_ID, "1" );
      ID_ALT_CONFLICT = getALT_ID( ID_REF_CONFLICT );

      ID_REF_RELATED = check_fail_defer_ref_table( iDEFER_REF_LDESC, iDEFER_REF_NAME_ACFT_RELATED,
            iACFT_ASSMBLCD, iFAIL_SEV_CD, iFAIL_DEFER_CD, iDEFER_REF_STATUS_CD, iINST_SYSTEMS_QT,
            iDISPATCH_SYSTEMS_QT, iAPPL_LDESC, iOPER_RESTRICTIONS_LDESC, iMAINT_ACTIONS_LDESC,
            iPERF_PENALTIES_LDESC, ALT_ASSMBL_BOM_ID, "1" );
      ID_ALT_RELATED = getALT_ID( ID_REF_RELATED );

      // Verify fail_defer_carrier
      simpleIDs carrierID_1 =
            getCarrierIds( iOPERATOR_CD_LIST_1, iOPERATOR_CD_LIST_1, iOPERATOR_CD_LIST_1 );
      simpleIDs carrierID_2 =
            getCarrierIds( iOPERATOR_CD_LIST_2, iOPERATOR_CD_LIST_1, iOPERATOR_CD_LIST_1 );

      // check MXI exists in table
      String lQuery = "select 1 from " + TableUtil.FAIL_DEFER_CARRIER
            + " where FAIL_DEFER_REF_DB_ID=" + ID_REF.getNO_DB_ID() + " and FAIL_DEFER_REF_ID="
            + ID_REF.getNO_ID() + " and CARRIER_DB_ID=" + carrierID_1.getNO_DB_ID()
            + " and CARRIER_ID=" + carrierID_1.getNO_ID();
      Assert.assertTrue( "Check fail_defer_carrier table to verify the record exists",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.FAIL_DEFER_CARRIER + " where FAIL_DEFER_REF_DB_ID="
            + ID_REF_CONFLICT.getNO_DB_ID() + " and FAIL_DEFER_REF_ID=" + ID_REF_CONFLICT.getNO_ID()
            + " and CARRIER_DB_ID=" + carrierID_1.getNO_DB_ID() + " and CARRIER_ID="
            + carrierID_1.getNO_ID();
      Assert.assertTrue( "Check fail_defer_carrier table to verify the record exists",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.FAIL_DEFER_CARRIER + " where FAIL_DEFER_REF_DB_ID="
            + ID_REF_RELATED.getNO_DB_ID() + " and FAIL_DEFER_REF_ID=" + ID_REF_RELATED.getNO_ID()
            + " and CARRIER_DB_ID=" + carrierID_1.getNO_DB_ID() + " and CARRIER_ID="
            + carrierID_1.getNO_ID();
      Assert.assertTrue( "Check fail_defer_carrier table to verify the record exists",
            RecordsExist( lQuery ) );

      // check ATLD exists in table
      lQuery = "select 1 from " + TableUtil.FAIL_DEFER_CARRIER + " where FAIL_DEFER_REF_DB_ID="
            + ID_REF.getNO_DB_ID() + " and FAIL_DEFER_REF_ID=" + ID_REF.getNO_ID()
            + " and CARRIER_DB_ID=" + carrierID_2.getNO_DB_ID() + " and CARRIER_ID="
            + carrierID_2.getNO_ID();
      Assert.assertTrue( "Check fail_defer_carrier table to verify the record exists",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.FAIL_DEFER_CARRIER + " where FAIL_DEFER_REF_DB_ID="
            + ID_REF_CONFLICT.getNO_DB_ID() + " and FAIL_DEFER_REF_ID=" + ID_REF_CONFLICT.getNO_ID()
            + " and CARRIER_DB_ID=" + carrierID_2.getNO_DB_ID() + " and CARRIER_ID="
            + carrierID_2.getNO_ID();
      Assert.assertTrue( "Check fail_defer_carrier table to verify the record exists",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.FAIL_DEFER_CARRIER + " where FAIL_DEFER_REF_DB_ID="
            + ID_REF_RELATED.getNO_DB_ID() + " and FAIL_DEFER_REF_ID=" + ID_REF_RELATED.getNO_ID()
            + " and CARRIER_DB_ID=" + carrierID_2.getNO_DB_ID() + " and CARRIER_ID="
            + carrierID_2.getNO_ID();
      Assert.assertTrue( "Check fail_defer_carrier table to verify the record exists",
            RecordsExist( lQuery ) );

      // Check fail_defer_ref_rel_def
      lQuery = "select 1 from " + TableUtil.FAIL_DEFER_REF_REL_DEF + " where FAIL_DEFER_REF_ID='"
            + ID_ALT + "' and REL_FAIL_DEFER_REF_ID='" + ID_ALT_RELATED + "'";

      Assert.assertTrue( "Check FAIL_DEFER_REF_REL_DEF table to verify the record exists",
            RecordsExist( lQuery ) );

      // Check fail_defer_ref_conflict_def
      lQuery =
            "select 1 from " + TableUtil.FAIL_DEFER_REF_CONFLICT_DEF + " where FAIL_DEFER_REF_ID='"
                  + ID_ALT + "' and CONFLICT_FAIL_DEFER_REF_ID='" + ID_ALT_CONFLICT + "'";

      Assert.assertTrue( "Check fail_defer_ref_conflict_def table to verify the record exists",
            RecordsExist( lQuery ) );

      // check fail_defer_ref_dead
      simpleIDs ldataTypeIds = getDataTypeIDs( "US", "HOUR", iACFT_DATA_TYPE_CD_HOURS );
      check_fail_defer_ref_dead( ID_ALT, ldataTypeIds, iACFT_DEAD_QT );

      // check fail_defer_ref_task_defn
      String ltaskAlt = getAltTASKID( iACFT_INSP_REQ_TASK_CD_1 );
      lQuery = "select 1 from " + TableUtil.FAIL_DEFER_REF_TASK_DEFN + " where FAIL_DEFER_REF_ID='"
            + ID_ALT + "' and TASK_DEFN_ID='" + ltaskAlt + "'";

      Assert.assertTrue( "Check fail_defer_ref_task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // check fail_defer_ref_degrad_cap
      check_fail_defer_ref_degrad_cap( ID_ALT, iACFT_CAP_CD, iACFT_CAP_LEVEL_CD );

   }


   /**
    * This test is to verify validation of deferral references of ACFT staging tables:
    * bl_fail_defer_ref, bl_fail_defer_conflict, bl_fail_defer_related, bl_fail_defer_insp,
    * bl_fail_defer_dead and bl_fail_defer_cap_level Values of DEFER_REF_STATUS_CD is not provided.
    *
    * INST_SYSTEMS_QT and DISPATCH_SYSTEMS_QT are mandatory fields and value should be >=-1;
    *
    *
    */

   public void testACFT_DEFER_DEFAULT_VALIDATION() {

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
      // lDeferralRefMap.put( "DEFER_REF_STATUS_CD", "'" + iDEFER_REF_STATUS_CD + "'" );
      lDeferralRefMap.put( "INST_SYSTEMS_QT", "'-1'" );
      lDeferralRefMap.put( "DISPATCH_SYSTEMS_QT", "'-1'" );
      lDeferralRefMap.put( "APPL_LDESC", "'" + iAPPL_LDESC + "'" );
      lDeferralRefMap.put( "OPER_RESTRICTIONS_LDESC", "'" + iOPER_RESTRICTIONS_LDESC + "'" );
      lDeferralRefMap.put( "PERF_PENALTIES_LDESC", "'" + iPERF_PENALTIES_LDESC + "'" );
      lDeferralRefMap.put( "MAINT_ACTIONS_LDESC", "'" + iMAINT_ACTIONS_LDESC + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_REF, lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // Check error code
      CheckErrorCode( "PASS" );

   }


   @Test
   public void testACFT_DEFER_DEFAULT_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testACFT_DEFER_DEFAULT_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Cleanup all IDs
      setIDsNull();

      // verify fail_defer_ref
      ALT_ASSMBL_BOM_ID = getAltBomID( iACFT_ASSMBLCD, iACFT_CONFIG_SLOT_CD );
      ID_REF = check_fail_defer_ref_table( iDEFER_REF_LDESC, iDEFER_REF_NAME_ACFT, iACFT_ASSMBLCD,
            iFAIL_SEV_CD, iFAIL_DEFER_CD, iDEFER_REF_STATUS_CD, "-1", "-1", iAPPL_LDESC,
            iOPER_RESTRICTIONS_LDESC, iMAINT_ACTIONS_LDESC, iPERF_PENALTIES_LDESC,
            ALT_ASSMBL_BOM_ID, "1" );
      ID_ALT = getALT_ID( ID_REF );

      // Verify fail_defer_carrier
      simpleIDs carrierID_1 =
            getCarrierIds( iOPERATOR_CD_LIST_1, iOPERATOR_CD_LIST_1, iOPERATOR_CD_LIST_1 );
      simpleIDs carrierID_2 =
            getCarrierIds( iOPERATOR_CD_LIST_2, iOPERATOR_CD_LIST_1, iOPERATOR_CD_LIST_1 );

      // check MXI exists in table
      String lQuery = "select 1 from " + TableUtil.FAIL_DEFER_CARRIER
            + " where FAIL_DEFER_REF_DB_ID=" + ID_REF.getNO_DB_ID() + " and FAIL_DEFER_REF_ID="
            + ID_REF.getNO_ID() + " and CARRIER_DB_ID=" + carrierID_1.getNO_DB_ID()
            + " and CARRIER_ID=" + carrierID_1.getNO_ID();
      Assert.assertTrue( "Check fail_defer_carrier table to verify the record exists",
            RecordsExist( lQuery ) );

      // check ATLD exists in table
      lQuery = "select 1 from " + TableUtil.FAIL_DEFER_CARRIER + " where FAIL_DEFER_REF_DB_ID="
            + ID_REF.getNO_DB_ID() + " and FAIL_DEFER_REF_ID=" + ID_REF.getNO_ID()
            + " and CARRIER_DB_ID=" + carrierID_2.getNO_DB_ID() + " and CARRIER_ID="
            + carrierID_2.getNO_ID();
      Assert.assertTrue( "Check fail_defer_carrier table to verify the record exists",
            RecordsExist( lQuery ) );

   }


   /**
    * This test is to verify validation of deferral references of ACFT staging tables:
    * bl_fail_defer_ref, bl_fail_defer_conflict, bl_fail_defer_related, bl_fail_defer_insp,
    * bl_fail_defer_dead and bl_fail_defer_cap_level Values of DEFER_REF_STATUS_CD is not provided.
    *
    * INST_SYSTEMS_QT and DISPATCH_SYSTEMS_QT are mandatory fields and value should be >=-1;
    *
    *
    */

   public void testACFT_DEFER_DEFAULT_2_VALIDATION() {

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
      // lDeferralRefMap.put( "DEFER_REF_STATUS_CD", "'" + iDEFER_REF_STATUS_CD + "'" );
      lDeferralRefMap.put( "INST_SYSTEMS_QT", "'0'" );
      lDeferralRefMap.put( "DISPATCH_SYSTEMS_QT", "'-1'" );
      lDeferralRefMap.put( "APPL_LDESC", "'" + iAPPL_LDESC + "'" );
      lDeferralRefMap.put( "OPER_RESTRICTIONS_LDESC", "'" + iOPER_RESTRICTIONS_LDESC + "'" );
      lDeferralRefMap.put( "PERF_PENALTIES_LDESC", "'" + iPERF_PENALTIES_LDESC + "'" );
      lDeferralRefMap.put( "MAINT_ACTIONS_LDESC", "'" + iMAINT_ACTIONS_LDESC + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_REF, lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // Check error code
      CheckErrorCode( "PASS" );

   }


   @Test
   public void testACFT_DEFER_DEFAULT_2_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testACFT_DEFER_DEFAULT_2_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Cleanup all IDs
      setIDsNull();

      // verify fail_defer_ref
      ALT_ASSMBL_BOM_ID = getAltBomID( iACFT_ASSMBLCD, iACFT_CONFIG_SLOT_CD );
      ID_REF = check_fail_defer_ref_table( iDEFER_REF_LDESC, iDEFER_REF_NAME_ACFT, iACFT_ASSMBLCD,
            iFAIL_SEV_CD, iFAIL_DEFER_CD, iDEFER_REF_STATUS_CD, "0", "-1", iAPPL_LDESC,
            iOPER_RESTRICTIONS_LDESC, iMAINT_ACTIONS_LDESC, iPERF_PENALTIES_LDESC,
            ALT_ASSMBL_BOM_ID, "1" );
      ID_ALT = getALT_ID( ID_REF );

      // Verify fail_defer_carrier
      simpleIDs carrierID_1 =
            getCarrierIds( iOPERATOR_CD_LIST_1, iOPERATOR_CD_LIST_1, iOPERATOR_CD_LIST_1 );
      simpleIDs carrierID_2 =
            getCarrierIds( iOPERATOR_CD_LIST_2, iOPERATOR_CD_LIST_1, iOPERATOR_CD_LIST_1 );

      // check MXI exists in table
      String lQuery = "select 1 from " + TableUtil.FAIL_DEFER_CARRIER
            + " where FAIL_DEFER_REF_DB_ID=" + ID_REF.getNO_DB_ID() + " and FAIL_DEFER_REF_ID="
            + ID_REF.getNO_ID() + " and CARRIER_DB_ID=" + carrierID_1.getNO_DB_ID()
            + " and CARRIER_ID=" + carrierID_1.getNO_ID();
      Assert.assertTrue( "Check fail_defer_carrier table to verify the record exists",
            RecordsExist( lQuery ) );

      // check ATLD exists in table
      lQuery = "select 1 from " + TableUtil.FAIL_DEFER_CARRIER + " where FAIL_DEFER_REF_DB_ID="
            + ID_REF.getNO_DB_ID() + " and FAIL_DEFER_REF_ID=" + ID_REF.getNO_ID()
            + " and CARRIER_DB_ID=" + carrierID_2.getNO_DB_ID() + " and CARRIER_ID="
            + carrierID_2.getNO_ID();
      Assert.assertTrue( "Check fail_defer_carrier table to verify the record exists",
            RecordsExist( lQuery ) );

   }


   /**
    * This test is to verify validation of deferral references of ACFT staging tables:
    * bl_fail_defer_ref, bl_fail_defer_conflict, bl_fail_defer_related, bl_fail_defer_insp,
    * bl_fail_defer_dead and bl_fail_defer_cap_level Values of DEFER_REF_STATUS_CD is not provided.
    *
    * INST_SYSTEMS_QT and DISPATCH_SYSTEMS_QT are mandatory fields and value should be >=-1;
    *
    *
    */

   public void testACFT_DEFER_DEFAULT_3_VALIDATION() {

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
      // lDeferralRefMap.put( "DEFER_REF_STATUS_CD", "'" + iDEFER_REF_STATUS_CD + "'" );
      lDeferralRefMap.put( "INST_SYSTEMS_QT", "'-1'" );
      lDeferralRefMap.put( "DISPATCH_SYSTEMS_QT", "'100'" );
      lDeferralRefMap.put( "APPL_LDESC", "'" + iAPPL_LDESC + "'" );
      lDeferralRefMap.put( "OPER_RESTRICTIONS_LDESC", "'" + iOPER_RESTRICTIONS_LDESC + "'" );
      lDeferralRefMap.put( "PERF_PENALTIES_LDESC", "'" + iPERF_PENALTIES_LDESC + "'" );
      lDeferralRefMap.put( "MAINT_ACTIONS_LDESC", "'" + iMAINT_ACTIONS_LDESC + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_FAIL_DEFER_REF, lDeferralRefMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // Check error code
      CheckErrorCode( "PASS" );

   }


   @Test
   public void testACFT_DEFER_DEFAULT_3_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testACFT_DEFER_DEFAULT_3_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Cleanup all IDs
      setIDsNull();

      // verify fail_defer_ref
      ALT_ASSMBL_BOM_ID = getAltBomID( iACFT_ASSMBLCD, iACFT_CONFIG_SLOT_CD );
      ID_REF = check_fail_defer_ref_table( iDEFER_REF_LDESC, iDEFER_REF_NAME_ACFT, iACFT_ASSMBLCD,
            iFAIL_SEV_CD, iFAIL_DEFER_CD, iDEFER_REF_STATUS_CD, "-1", "100", iAPPL_LDESC,
            iOPER_RESTRICTIONS_LDESC, iMAINT_ACTIONS_LDESC, iPERF_PENALTIES_LDESC,
            ALT_ASSMBL_BOM_ID, "1" );
      ID_ALT = getALT_ID( ID_REF );

      // Verify fail_defer_carrier
      simpleIDs carrierID_1 =
            getCarrierIds( iOPERATOR_CD_LIST_1, iOPERATOR_CD_LIST_1, iOPERATOR_CD_LIST_1 );
      simpleIDs carrierID_2 =
            getCarrierIds( iOPERATOR_CD_LIST_2, iOPERATOR_CD_LIST_1, iOPERATOR_CD_LIST_1 );

      // check MXI exists in table
      String lQuery = "select 1 from " + TableUtil.FAIL_DEFER_CARRIER
            + " where FAIL_DEFER_REF_DB_ID=" + ID_REF.getNO_DB_ID() + " and FAIL_DEFER_REF_ID="
            + ID_REF.getNO_ID() + " and CARRIER_DB_ID=" + carrierID_1.getNO_DB_ID()
            + " and CARRIER_ID=" + carrierID_1.getNO_ID();
      Assert.assertTrue( "Check fail_defer_carrier table to verify the record exists",
            RecordsExist( lQuery ) );

      // check ATLD exists in table
      lQuery = "select 1 from " + TableUtil.FAIL_DEFER_CARRIER + " where FAIL_DEFER_REF_DB_ID="
            + ID_REF.getNO_DB_ID() + " and FAIL_DEFER_REF_ID=" + ID_REF.getNO_ID()
            + " and CARRIER_DB_ID=" + carrierID_2.getNO_DB_ID() + " and CARRIER_ID="
            + carrierID_2.getNO_ID();
      Assert.assertTrue( "Check fail_defer_carrier table to verify the record exists",
            RecordsExist( lQuery ) );

   }

}
