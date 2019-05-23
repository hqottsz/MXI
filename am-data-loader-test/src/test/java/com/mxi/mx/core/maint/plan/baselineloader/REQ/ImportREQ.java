package com.mxi.mx.core.maint.plan.baselineloader.REQ;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.mxi.mx.core.maint.plan.baselineloader.Requirements;
import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;
import com.mxi.mx.core.maint.plan.datamodels.taskIDs;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.WhereClause;


/**
 * This test suite contains test cases on validation and import functionality of REQ_IMPORT package
 *
 * @author ALICIA QIAN
 */

public class ImportREQ extends ReqTests {

   @Rule
   public TestName testName = new TestName();

   public String iStep_LDESC_step1 = "Auto ACFT step 1";
   public String iStep_LDESC_step2 = "Auto ACFT step 2";
   // public Requirements iVerifyMore = new Requirements();
   public Requirements iVerifyMore = null;


   /**
    * Clean up after each individual test
    *
    */
   @After
   @Override
   public void after() {
      RestoreData();
      String strTCName = testName.getMethodName();
      if ( strTCName.contains( "_AllReq_" ) ) {
         String lQuery =
               "UPDATE task_task SET task_def_status_cd = 'ACTV' WHERE TASK_CD = 'ACFT-SYS-1-1-TRK-BATCH-PARENT-AUTO-JIC' AND instruction_ldesc = 'Used for C_REQ Import test'";
         runUpdate( lQuery );
         lQuery = "DELETE FROM TASK_TASK WHERE task_cd IN ('ATEST','BTEST','CTEST')";
         runUpdate( lQuery );
      }
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
      String strTCName = testName.getMethodName();
      if ( strTCName.contains( "_AllReq_" ) ) {
         String lQuery =
               "UPDATE task_task SET task_def_status_cd = 'BUILD' WHERE TASK_CD = 'ACFT-SYS-1-1-TRK-BATCH-PARENT-AUTO-JIC'";
         runUpdate( lQuery );
         iVerifyMore.RestoreData(); // This will perform clean-up other C_Req data tables
      }

   }


   /**
    * This test is to verify req_import.validate_req functionality of staging tables: c_req,
    * c_req_labour, c_req_part, c_req_tool, c_req_step and c_req_step_skill. REQ is on TRK of ACFT
    *
    *
    */

   public void testACFT_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ==========================================================================
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_1 + "'" );
      lReqMap.put( "REQ_TASK_DESC", "'" + iREQ_TASK_DESC_1 + "'" );
      lReqMap.put( "REQ_TASK_REF_SDESC", "'" + iREQ_TASK_REF_SDESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "APPLICABILITY_DESC", "'" + iAPPLICABILITY_DESC + "'" );
      lReqMap.put( "LAST_DEADLINE_DRIVER_BOOL", "'" + iLAST_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_DRIVER_BOOL", "'" + iSOFT_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL + "'" );
      lReqMap.put( "ENFORCE_WORKSCOPE_ORD_BOOL", "'" + iENFORCE_WORKSCOPE_ORD_BOOL + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'0'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // C_REQ_STEP for step 1==========================================================
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'1'" );
      lReqMap.put( "JOB_STEP_DESC", "'" + iStep_LDESC_step1 + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP, lReqMap ) );

      // C_REQ_STEP for step 2
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'2'" );
      lReqMap.put( "JOB_STEP_DESC", "'" + iStep_LDESC_step2 + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP, lReqMap ) );

      // C_REQ_LABOR for fist skill========================================================
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "LABOUR_SKILL_CD", "'" + iLABOR_SKILL_CD_LBR + "'" );
      lReqMap.put( "MAN_PWR_CT", "'" + iMAN_PWR_CT + "'" );
      lReqMap.put( "SCHED_WORK_HRS", "'" + iSCHED_WORK_HRS + "'" );
      lReqMap.put( "SCHED_INSP_HRS", "'" + iSCHED_INSP_HRS + "'" );
      lReqMap.put( "SCHED_CERT_HRS", "'" + iSCHED_CERT_HRS + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_LABOUR, lReqMap ) );

      // C_REQ_LABOR for second skill
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "LABOUR_SKILL_CD", "'" + iLABOR_SKILL_CD + "'" );
      lReqMap.put( "MAN_PWR_CT", "'" + iMAN_PWR_CT + "'" );
      lReqMap.put( "SCHED_WORK_HRS", "'" + iSCHED_WORK_HRS + "'" );
      lReqMap.put( "SCHED_INSP_HRS", "'" + iSCHED_INSP_HRS + "'" );
      lReqMap.put( "SCHED_CERT_HRS", "'" + iSCHED_CERT_HRS + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_LABOUR, lReqMap ) );

      // C_REQ_STEP_SKILL for step 1=======================================================
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'1'" );
      lReqMap.put( "STEP_SKILL_CD", "'" + iLABOR_SKILL_CD_LBR + "'" );
      lReqMap.put( "INSP_BOOL", "'" + iINSP_BOOL_Y + "'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP_SKILL, lReqMap ) );

      // C_REQ_STEP_SKILL for step 2
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'2'" );
      lReqMap.put( "STEP_SKILL_CD", "'" + iLABOR_SKILL_CD + "'" );
      lReqMap.put( "INSP_BOOL", "'" + iINSP_BOOL_Y + "'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP_SKILL, lReqMap ) );

      // C_REQ_PART==========================================================================
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_1 + "'" );
      lReqMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_1 + "'" );
      lReqMap.put( "REQ_QT", "'" + iREQ_QT + "'" );
      lReqMap.put( "IPC_REF_CD", "'" + iIPC_REF_CD + "'" );
      lReqMap.put( "POSITION", "'" + iPOSITION_1 + "'" );
      lReqMap.put( "REMOVE_BOOL", "'" + iREMOVE_BOOL_Y + "'" );
      lReqMap.put( "REMOVE_REASON_CD", "'" + iREMOVE_REASON_CD + "'" );
      lReqMap.put( "INSTALL_BOOL", "'" + iINSTALL_BOOL_N + "'" );
      lReqMap.put( "REQ_PRIORITY_CD", "'" + iREQ_PRIORITY_CD + "'" );
      lReqMap.put( "PART_PROVIDER_TYPE_CD", "'" + iPART_PROVIDER_TYPE_CD + "'" );
      lReqMap.put( "IS_PART_SPECIFIC_BOOL", "'" + iIS_PART_SPECIFIC_BOOL_Y + "'" );
      lReqMap.put( "REQ_ACTION_CD", "'" + iREQ_ACTION_CD_NOREQ + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_PART, lReqMap ) );

      // C_REQ_TOOL========================================================================
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_10 + "'" );
      lReqMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_10 + "'" );
      lReqMap.put( "REQ_HR", "'" + iREQ_HR_INT + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_TOOL, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // verify no error
      VerifyValidation();

   }


   /**
    * This test is to verify req_import.validate_req functionality of staging tables: c_req,
    * c_req_labour, c_req_part, c_req_tool, c_req_step and c_req_step_skill. REQ is on sys of eng
    *
    *
    */

   public void testENG_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ==========================================================================
      lReqMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iENG_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_3 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_3 + "'" );
      lReqMap.put( "REQ_TASK_DESC", "'" + iREQ_TASK_DESC_3 + "'" );
      lReqMap.put( "REQ_TASK_REF_SDESC", "'" + iREQ_TASK_REF_SDESC_3 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "APPLICABILITY_DESC", "'" + iAPPLICABILITY_DESC + "'" );
      lReqMap.put( "LAST_DEADLINE_DRIVER_BOOL", "'" + iLAST_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_DRIVER_BOOL", "'" + iSOFT_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL + "'" );
      lReqMap.put( "ENFORCE_WORKSCOPE_ORD_BOOL", "'" + iENFORCE_WORKSCOPE_ORD_BOOL + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'F'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // C_REQ_STEP for step 1==========================================================
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iENG_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_3 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'1'" );
      lReqMap.put( "JOB_STEP_DESC", "'" + iStep_LDESC_step1 + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP, lReqMap ) );

      // C_REQ_STEP for step 2
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iENG_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_3 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'2'" );
      lReqMap.put( "JOB_STEP_DESC", "'" + iStep_LDESC_step2 + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP, lReqMap ) );

      // C_REQ_LABOR for fist skill========================================================
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iENG_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_3 + "'" );
      lReqMap.put( "LABOUR_SKILL_CD", "'" + iLABOR_SKILL_CD_LBR + "'" );
      lReqMap.put( "MAN_PWR_CT", "'" + iMAN_PWR_CT + "'" );
      lReqMap.put( "SCHED_WORK_HRS", "'" + iSCHED_WORK_HRS + "'" );
      lReqMap.put( "SCHED_INSP_HRS", "'" + iSCHED_INSP_HRS + "'" );
      lReqMap.put( "SCHED_CERT_HRS", "'" + iSCHED_CERT_HRS + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_LABOUR, lReqMap ) );

      // C_REQ_LABOR for second skill
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iENG_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_3 + "'" );
      lReqMap.put( "LABOUR_SKILL_CD", "'" + iLABOR_SKILL_CD + "'" );
      lReqMap.put( "MAN_PWR_CT", "'" + iMAN_PWR_CT + "'" );
      lReqMap.put( "SCHED_WORK_HRS", "'" + iSCHED_WORK_HRS + "'" );
      lReqMap.put( "SCHED_INSP_HRS", "'" + iSCHED_INSP_HRS + "'" );
      lReqMap.put( "SCHED_CERT_HRS", "'" + iSCHED_CERT_HRS + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_LABOUR, lReqMap ) );

      // C_REQ_STEP_SKILL for step 1=======================================================
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iENG_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_3 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'1'" );
      lReqMap.put( "STEP_SKILL_CD", "'" + iLABOR_SKILL_CD_LBR + "'" );
      lReqMap.put( "INSP_BOOL", "'" + iINSP_BOOL_Y + "'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP_SKILL, lReqMap ) );

      // C_REQ_STEP_SKILL for step 2
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iENG_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_3 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'2'" );
      lReqMap.put( "STEP_SKILL_CD", "'" + iLABOR_SKILL_CD + "'" );
      lReqMap.put( "INSP_BOOL", "'" + iINSP_BOOL_Y + "'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP_SKILL, lReqMap ) );

      // C_REQ_PART==========================================================================
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iENG_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_3 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_2 + "'" );
      lReqMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_2 + "'" );
      lReqMap.put( "REQ_QT", "'" + iREQ_QT + "'" );
      lReqMap.put( "IPC_REF_CD", null );
      lReqMap.put( "POSITION", null );
      lReqMap.put( "REMOVE_BOOL", null );
      lReqMap.put( "REMOVE_REASON_CD", null );
      lReqMap.put( "INSTALL_BOOL", null );
      lReqMap.put( "REQ_PRIORITY_CD", null );
      lReqMap.put( "PART_PROVIDER_TYPE_CD", null );
      lReqMap.put( "IS_PART_SPECIFIC_BOOL", "'" + iIS_PART_SPECIFIC_BOOL_Y + "'" );
      lReqMap.put( "REQ_ACTION_CD", "'" + iREQ_ACTION_CD_ASREQ + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_PART, lReqMap ) );

      // C_REQ_TOOL========================================================================
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iENG_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_3 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_10 + "'" );
      lReqMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_10 + "'" );
      lReqMap.put( "REQ_HR", "'" + iREQ_HR_INT + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_TOOL, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // verify no error
      VerifyValidation();

   }


   /**
    * This test is to verify req_import.validate_req functionality of staging tables: c_req,
    * c_req_labour, c_req_part, c_req_tool, c_req_step and c_req_step_skill. Multiple records.
    *
    *
    */

   public void testMULTIPLE_RECORDS_VALIDATION() {

      testACFT_VALIDATION();
      testENG_VALIDATION();

      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify req_import.validate_req functionality of staging tables: c_req,
    * c_req_labour, c_req_part, c_req_tool, c_req_step and c_req_step_skill. REQ is on TRK of ACFT
    *
    *
    */

   @Test
   public void testACFT_IMPORT() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testACFT_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTaskIDs_1 = null;
      iTaskDefnIDs_1 = null;

      // verify task_task
      String lquery = "select assmbl_bom_id from eqp_assmbl_bom where assmbl_bom_cd='"
            + iACFT_REQ_ATA_CD_TRK + "'";
      String assmblBomId = getStringValueFromQuery( lquery, "ASSMBL_BOM_ID" );
      taskIDs IDS = VerifyTask_TASK( iREQ_TASK_CD_1, iREQ_TASK_NAME_1, iCLASS_CD, iACFT_ASSMBLCD,
            assmblBomId, iSUBCLASS_CD, iORIGINATOR_CD, iAPPLICABILITY_DESC );

      iTaskDefnIDs_1 = IDS.getTASK_DEFN_IDs();
      iTaskIDs_1 = IDS.getTASK_IDs();

      // verify task_DEFN
      lquery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTaskDefnIDs_1.getNO_DB_ID() + " and TASK_DEFN_ID=" + iTaskDefnIDs_1.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lquery ) );

      // verify task_work_type
      verifyWorkType( iTaskIDs_1, iWORK_TYPE_LIST_1 );
      verifyWorkType( iTaskIDs_1, iWORK_TYPE_LIST_2 );

      // verify task_labour_list
      verifyTASKLABOURLIST( iTaskIDs_1, iLABOR_SKILL_CD_LBR, iMAN_PWR_CT, iSCHED_WORK_HRS,
            iSCHED_INSP_HRS, iSCHED_CERT_HRS );
      verifyTASKLABOURLIST( iTaskIDs_1, iLABOR_SKILL_CD, iMAN_PWR_CT, iSCHED_WORK_HRS,
            iSCHED_INSP_HRS, iSCHED_CERT_HRS );

      // verify task_part_list
      lquery = "select assmbl_bom_id from eqp_assmbl_bom where assmbl_bom_cd='" + iIPC_REF_CD + "'";
      String lPartAssmblBomId = getStringValueFromQuery( lquery, "ASSMBL_BOM_ID" );

      lquery = "select part_no_db_id, part_no_id from eqp_part_no where part_no_oem='"
            + iPART_NO_OEM_1 + "'";
      simpleIDs lPartNoIds = getIDs( lquery, "part_no_db_id", "part_no_id" );

      lquery = "select BOM_PART_DB_ID, BOM_PART_ID from eqp_part_baseline "
            + "inner join EQP_PART_NO on "
            + "EQP_PART_NO.PART_NO_DB_ID=EQP_PART_BASELINE.PART_NO_DB_ID and "
            + "EQP_PART_NO.PART_NO_ID=EQP_PART_BASELINE.PART_NO_ID "
            + "where EQP_PART_NO.Part_No_Oem='" + iPART_NO_OEM_1 + "'";
      simpleIDs lBOMPartNoIds = getIDs( lquery, "BOM_PART_DB_ID", "BOM_PART_ID" );

      verifyTASKPARTLIST( iTaskIDs_1, iACFT_ASSMBLCD, lPartAssmblBomId, lPartNoIds,
            iREMOVE_REASON_CD, iPART_PROVIDER_TYPE_CD, iREQ_QT, iREMOVE_BOOL_Y_NUMBER,
            iINSTALL_BOOL_N_NUMBER, iREQ_ACTION_CD_NOREQ, iREQ_PRIORITY_CD, lBOMPartNoIds );

      // verify task_step
      VerifyTask_Step( iTaskIDs_1, "1", iStep_LDESC_step1 );
      VerifyTask_Step( iTaskIDs_1, "2", iStep_LDESC_step2 );

      // verify task_step_skill table
      lquery = "select 1 from " + TableUtil.TASK_STEP_SKILL + " where TASK_DB_ID="
            + iTaskIDs_1.getNO_DB_ID() + " and TASK_ID=" + iTaskIDs_1.getNO_ID()
            + " and LABOUR_SKILL_CD='" + iLABOR_SKILL_CD + "'";
      Assert.assertTrue( "Check task_step_skill table to verify the ENG record exists",
            RecordsExist( lquery ) );

      lquery = "select 1 from " + TableUtil.TASK_STEP_SKILL + " where TASK_DB_ID="
            + iTaskIDs_1.getNO_DB_ID() + " and TASK_ID=" + iTaskIDs_1.getNO_ID()
            + " and LABOUR_SKILL_CD='" + iLABOR_SKILL_CD_LBR + "'";
      Assert.assertTrue( "Check task_step_skill table to verify the LBR record exists",
            RecordsExist( lquery ) );

      // Verify task_tool_list
      lquery = "select BOM_PART_ID from EQP_PART_BASELINE " + " inner join EQP_PART_NO on "
            + " EQP_PART_NO.PART_NO_DB_ID=EQP_PART_BASELINE.PART_NO_DB_ID and "
            + " EQP_PART_NO.PART_NO_ID=EQP_PART_BASELINE.PART_NO_ID "
            + " where EQP_PART_NO.Part_No_Oem='" + iPART_NO_OEM_10
            + "' and EQP_PART_NO.Manufact_Cd='" + iMANUFACT_REF_10 + "'";
      lPartAssmblBomId = getStringValueFromQuery( lquery, "BOM_PART_ID" );

      VerifyTask_Tool( iTaskIDs_1, lPartAssmblBomId, iREQ_HR_INT );

   }


   /**
    * This test is to verify req_import.validate_req functionality of staging tables: c_req,
    * c_req_labour, c_req_part, c_req_tool, c_req_step and c_req_step_skill. REQ is on SYS of ENG
    *
    *
    */

   @Test
   public void testENG_IMPORT() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testENG_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTaskIDs_1 = null;
      iTaskDefnIDs_1 = null;

      // verify task_task
      String lquery = "select assmbl_bom_id from eqp_assmbl_bom where assmbl_bom_cd='"
            + iENG_REQ_ATA_CD_SYS + "'";
      String assmblBomId = getStringValueFromQuery( lquery, "ASSMBL_BOM_ID" );
      taskIDs IDS = VerifyTask_TASK( iREQ_TASK_CD_3, iREQ_TASK_NAME_3, iCLASS_CD, iENG_ASSMBLCD,
            assmblBomId, iSUBCLASS_CD, iORIGINATOR_CD, iAPPLICABILITY_DESC );

      iTaskDefnIDs_1 = IDS.getTASK_DEFN_IDs();
      iTaskIDs_1 = IDS.getTASK_IDs();

      // verify task_DEFN
      lquery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTaskDefnIDs_1.getNO_DB_ID() + " and TASK_DEFN_ID=" + iTaskDefnIDs_1.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lquery ) );

      // verify task_work_type
      verifyWorkType( iTaskIDs_1, iWORK_TYPE_LIST_1 );
      verifyWorkType( iTaskIDs_1, iWORK_TYPE_LIST_2 );

      // verify task_labour_list
      verifyTASKLABOURLIST( iTaskIDs_1, iLABOR_SKILL_CD_LBR, iMAN_PWR_CT, iSCHED_WORK_HRS,
            iSCHED_INSP_HRS, iSCHED_CERT_HRS );
      verifyTASKLABOURLIST( iTaskIDs_1, iLABOR_SKILL_CD, iMAN_PWR_CT, iSCHED_WORK_HRS,
            iSCHED_INSP_HRS, iSCHED_CERT_HRS );

      // verify task_part_list
      lquery = "select assmbl_bom_id from eqp_assmbl_bom where assmbl_bom_cd='" + iIPC_REF_CD + "'";
      String lPartAssmblBomId = getStringValueFromQuery( lquery, "ASSMBL_BOM_ID" );

      lquery = "select part_no_db_id, part_no_id from eqp_part_no where part_no_oem='"
            + iPART_NO_OEM_2 + "'";
      simpleIDs lPartNoIds = getIDs( lquery, "part_no_db_id", "part_no_id" );

      lquery = "select BOM_PART_DB_ID, BOM_PART_ID from eqp_part_baseline "
            + "inner join EQP_PART_NO on "
            + "EQP_PART_NO.PART_NO_DB_ID=EQP_PART_BASELINE.PART_NO_DB_ID and "
            + "EQP_PART_NO.PART_NO_ID=EQP_PART_BASELINE.PART_NO_ID "
            + "where EQP_PART_NO.Part_No_Oem='" + iPART_NO_OEM_2 + "'";
      simpleIDs lBOMPartNoIds = getIDs( lquery, "BOM_PART_DB_ID", "BOM_PART_ID" );

      verifyTASKPARTLISTBATCH( iTaskIDs_1, lPartNoIds, lBOMPartNoIds );

      // verify task_step
      VerifyTask_Step( iTaskIDs_1, "1", iStep_LDESC_step1 );
      VerifyTask_Step( iTaskIDs_1, "2", iStep_LDESC_step2 );

      // verify task_step_skill table
      lquery = "select 1 from " + TableUtil.TASK_STEP_SKILL + " where TASK_DB_ID="
            + iTaskIDs_1.getNO_DB_ID() + " and TASK_ID=" + iTaskIDs_1.getNO_ID()
            + " and LABOUR_SKILL_CD='" + iLABOR_SKILL_CD + "'";
      Assert.assertTrue( "Check task_step_skill table to verify the ENG record exists",
            RecordsExist( lquery ) );

      lquery = "select 1 from " + TableUtil.TASK_STEP_SKILL + " where TASK_DB_ID="
            + iTaskIDs_1.getNO_DB_ID() + " and TASK_ID=" + iTaskIDs_1.getNO_ID()
            + " and LABOUR_SKILL_CD='" + iLABOR_SKILL_CD_LBR + "'";
      Assert.assertTrue( "Check task_step_skill table to verify the LBR record exists",
            RecordsExist( lquery ) );

      // Verify task_tool_list
      lquery = "select BOM_PART_ID from EQP_PART_BASELINE " + " inner join EQP_PART_NO on "
            + " EQP_PART_NO.PART_NO_DB_ID=EQP_PART_BASELINE.PART_NO_DB_ID and "
            + " EQP_PART_NO.PART_NO_ID=EQP_PART_BASELINE.PART_NO_ID "
            + " where EQP_PART_NO.Part_No_Oem='" + iPART_NO_OEM_10
            + "' and EQP_PART_NO.Manufact_Cd='" + iMANUFACT_REF_10 + "'";
      lPartAssmblBomId = getStringValueFromQuery( lquery, "BOM_PART_ID" );

      VerifyTask_Tool( iTaskIDs_1, lPartAssmblBomId, iREQ_HR_INT );

   }


   /**
    * This test is to verify req_import.validate_req functionality of staging tables: c_req,
    * c_req_labour, c_req_part, c_req_tool, c_req_step and c_req_step_skill. REQ is on TRK of ACFT
    *
    * Ignore this test case since it did not clean up data base after test.
    */
   @Ignore
   @Test
   public void testACFT_AllReq_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ==========================================================================
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1_2 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_1 + "'" );
      lReqMap.put( "REQ_TASK_DESC", "'" + iREQ_TASK_DESC_1 + "'" );
      lReqMap.put( "REQ_TASK_REF_SDESC", "'" + iREQ_TASK_REF_SDESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "APPLICABILITY_DESC", "'" + iAPPLICABILITY_DESC + "'" );
      lReqMap.put( "LAST_DEADLINE_DRIVER_BOOL", "'" + iLAST_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_DRIVER_BOOL", "'" + iSOFT_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL + "'" );
      lReqMap.put( "ENFORCE_WORKSCOPE_ORD_BOOL", "'" + iENFORCE_WORKSCOPE_ORD_BOOL + "'" );
      lReqMap.put( "REQ_INSTRUCTIONS", "'" + iREQ_INSTRUCTIONS + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // C_REQ_STEP for step 1==========================================================
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1_2 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'1'" );
      lReqMap.put( "JOB_STEP_DESC", "'" + iStep_LDESC_step1 + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP, lReqMap ) );

      // C_REQ_STEP for step 2
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1_2 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'2'" );
      lReqMap.put( "JOB_STEP_DESC", "'" + iStep_LDESC_step2 + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP, lReqMap ) );

      // C_REQ_LABOR for fist skill========================================================
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1_2 + "'" );
      lReqMap.put( "LABOUR_SKILL_CD", "'" + iLABOR_SKILL_CD_LBR + "'" );
      lReqMap.put( "MAN_PWR_CT", "'" + iMAN_PWR_CT + "'" );
      lReqMap.put( "SCHED_WORK_HRS", "'" + iSCHED_WORK_HRS + "'" );
      lReqMap.put( "SCHED_INSP_HRS", "'" + iSCHED_INSP_HRS + "'" );
      lReqMap.put( "SCHED_CERT_HRS", "'" + iSCHED_CERT_HRS + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_LABOUR, lReqMap ) );

      // C_REQ_LABOR for second skill
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1_2 + "'" );
      lReqMap.put( "LABOUR_SKILL_CD", "'" + iLABOR_SKILL_CD + "'" );
      lReqMap.put( "MAN_PWR_CT", "'" + iMAN_PWR_CT + "'" );
      lReqMap.put( "SCHED_WORK_HRS", "'" + iSCHED_WORK_HRS + "'" );
      lReqMap.put( "SCHED_INSP_HRS", "'" + iSCHED_INSP_HRS + "'" );
      lReqMap.put( "SCHED_CERT_HRS", "'" + iSCHED_CERT_HRS + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_LABOUR, lReqMap ) );

      // C_REQ_STEP_SKILL for step 1=======================================================
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1_2 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'1'" );
      lReqMap.put( "STEP_SKILL_CD", "'" + iLABOR_SKILL_CD_LBR + "'" );
      lReqMap.put( "INSP_BOOL", "'" + iINSP_BOOL_Y + "'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP_SKILL, lReqMap ) );

      // C_REQ_STEP_SKILL for step 2
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1_2 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'2'" );
      lReqMap.put( "STEP_SKILL_CD", "'" + iLABOR_SKILL_CD + "'" );
      lReqMap.put( "INSP_BOOL", "'" + iINSP_BOOL_Y + "'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP_SKILL, lReqMap ) );

      // C_REQ_PART==========================================================================
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1_2 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_1 + "'" );
      lReqMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_1 + "'" );
      lReqMap.put( "REQ_QT", "'" + iREQ_QT + "'" );
      lReqMap.put( "IPC_REF_CD", "'" + iIPC_REF_CD + "'" );
      lReqMap.put( "POSITION", "'" + iPOSITION_1 + "'" );
      lReqMap.put( "REMOVE_BOOL", "'" + iREMOVE_BOOL_Y + "'" );
      lReqMap.put( "REMOVE_REASON_CD", "'" + iREMOVE_REASON_CD + "'" );
      lReqMap.put( "INSTALL_BOOL", "'" + iINSTALL_BOOL_N + "'" );
      lReqMap.put( "REQ_PRIORITY_CD", "'" + iREQ_PRIORITY_CD + "'" );
      lReqMap.put( "PART_PROVIDER_TYPE_CD", "'" + iPART_PROVIDER_TYPE_CD + "'" );
      lReqMap.put( "IS_PART_SPECIFIC_BOOL", "'" + iIS_PART_SPECIFIC_BOOL_Y + "'" );
      lReqMap.put( "REQ_ACTION_CD", "'" + iREQ_ACTION_CD_NOREQ + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_PART, lReqMap ) );

      // C_REQ_TOOL========================================================================
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1_2 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_10 + "'" );
      lReqMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_10 + "'" );
      lReqMap.put( "REQ_HR", "'" + iREQ_HR_INT + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_TOOL, lReqMap ) );

      // c_req_standard_deadline
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1_2 + "'" );
      lReqMap.put( "SCHED_HRS_THRESHOLD", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STANDARD_DEADLINE, lReqMap ) );

      iVerifyMore.insertTable_C_REQ_transform();
      iVerifyMore.insertTable_C_REQ_PART_transform();
      iVerifyMore.insertTable_C_REQ_IETM_TOPIC();
      iVerifyMore.insertTable_C_REQ_JIC();
      iVerifyMore.insertTable_C_REQ_ADVISORY();
      iVerifyMore.insertTable_C_REQ_IMPACT();
      iVerifyMore.insertTable_C_REQ_REPL();

      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // verify no error
      VerifyValidation();

   }


   /**
    * This test is to verify req_import.validate_req functionality of staging tables: c_req,
    * c_req_labour, c_req_part, c_req_tool, c_req_step and c_req_step_skill. REQ is on TRK of ACFT
    *
    * * Ignore this test case since it did not clean up data base after test.
    */
   @Ignore
   @Test
   public void testACFT_AllReq_IMPORT() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testACFT_AllReq_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTaskIDs_1 = null;
      iTaskDefnIDs_1 = null;

      // verify task_task
      String lquery = "select assmbl_bom_id from eqp_assmbl_bom where assmbl_bom_cd='"
            + iACFT_REQ_ATA_CD_TRK + "'";
      String assmblBomId = getStringValueFromQuery( lquery, "ASSMBL_BOM_ID" );
      taskIDs IDS = VerifyTask_TASK( iREQ_TASK_CD_1_2, iREQ_TASK_NAME_1, iCLASS_CD, iACFT_ASSMBLCD,
            assmblBomId, iSUBCLASS_CD, iORIGINATOR_CD, iAPPLICABILITY_DESC );

      iTaskDefnIDs_1 = IDS.getTASK_DEFN_IDs();
      iTaskIDs_1 = IDS.getTASK_IDs();

      // verify task_DEFN
      lquery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTaskDefnIDs_1.getNO_DB_ID() + " and TASK_DEFN_ID=" + iTaskDefnIDs_1.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lquery ) );

      // verify task_work_type
      verifyWorkType( iTaskIDs_1, iWORK_TYPE_LIST_1 );
      verifyWorkType( iTaskIDs_1, iWORK_TYPE_LIST_2 );

      // verify task_labour_list
      verifyTASKLABOURLIST( iTaskIDs_1, iLABOR_SKILL_CD_LBR, iMAN_PWR_CT, iSCHED_WORK_HRS,
            iSCHED_INSP_HRS, iSCHED_CERT_HRS );
      verifyTASKLABOURLIST( iTaskIDs_1, iLABOR_SKILL_CD, iMAN_PWR_CT, iSCHED_WORK_HRS,
            iSCHED_INSP_HRS, iSCHED_CERT_HRS );

      // verify task_part_list
      lquery = "select assmbl_bom_id from eqp_assmbl_bom where assmbl_bom_cd='" + iIPC_REF_CD + "'";
      String lPartAssmblBomId = getStringValueFromQuery( lquery, "ASSMBL_BOM_ID" );

      lquery = "select part_no_db_id, part_no_id from eqp_part_no where part_no_oem='"
            + iPART_NO_OEM_1 + "'";
      simpleIDs lPartNoIds = getIDs( lquery, "part_no_db_id", "part_no_id" );

      lquery = "select BOM_PART_DB_ID, BOM_PART_ID from eqp_part_baseline "
            + "inner join EQP_PART_NO on "
            + "EQP_PART_NO.PART_NO_DB_ID=EQP_PART_BASELINE.PART_NO_DB_ID and "
            + "EQP_PART_NO.PART_NO_ID=EQP_PART_BASELINE.PART_NO_ID "
            + "where EQP_PART_NO.Part_No_Oem='" + iPART_NO_OEM_1 + "'";
      simpleIDs lBOMPartNoIds = getIDs( lquery, "BOM_PART_DB_ID", "BOM_PART_ID" );

      verifyTASKPARTLIST( iTaskIDs_1, iACFT_ASSMBLCD, lPartAssmblBomId, lPartNoIds,
            iREMOVE_REASON_CD, iPART_PROVIDER_TYPE_CD, iREQ_QT, iREMOVE_BOOL_Y_NUMBER,
            iINSTALL_BOOL_N_NUMBER, iREQ_ACTION_CD_NOREQ, iREQ_PRIORITY_CD, lBOMPartNoIds );

      // verify task_step
      VerifyTask_Step( iTaskIDs_1, "1", iStep_LDESC_step1 );
      VerifyTask_Step( iTaskIDs_1, "2", iStep_LDESC_step2 );

      // verify task_step_skill table
      lquery = "select 1 from " + TableUtil.TASK_STEP_SKILL + " where TASK_DB_ID="
            + iTaskIDs_1.getNO_DB_ID() + " and TASK_ID=" + iTaskIDs_1.getNO_ID()
            + " and LABOUR_SKILL_CD='" + iLABOR_SKILL_CD + "'";
      Assert.assertTrue( "Check task_step_skill table to verify the ENG record exists",
            RecordsExist( lquery ) );

      lquery = "select 1 from " + TableUtil.TASK_STEP_SKILL + " where TASK_DB_ID="
            + iTaskIDs_1.getNO_DB_ID() + " and TASK_ID=" + iTaskIDs_1.getNO_ID()
            + " and LABOUR_SKILL_CD='" + iLABOR_SKILL_CD_LBR + "'";
      Assert.assertTrue( "Check task_step_skill table to verify the LBR record exists",
            RecordsExist( lquery ) );

      // Verify task_tool_list
      lquery = "select BOM_PART_ID from EQP_PART_BASELINE " + " inner join EQP_PART_NO on "
            + " EQP_PART_NO.PART_NO_DB_ID=EQP_PART_BASELINE.PART_NO_DB_ID and "
            + " EQP_PART_NO.PART_NO_ID=EQP_PART_BASELINE.PART_NO_ID "
            + " where EQP_PART_NO.Part_No_Oem='" + iPART_NO_OEM_10
            + "' and EQP_PART_NO.Manufact_Cd='" + iMANUFACT_REF_10 + "'";
      lPartAssmblBomId = getStringValueFromQuery( lquery, "BOM_PART_ID" );

      VerifyTask_Tool( iTaskIDs_1, lPartAssmblBomId, iREQ_HR_INT );

      iVerifyMore.verifyOtherReqTables( iTaskIDs_1 );

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

      // C_REQ==========================================================================
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_1 + "'" );
      lReqMap.put( "REQ_TASK_DESC", "'" + iREQ_TASK_DESC_1 + "'" );
      lReqMap.put( "REQ_TASK_REF_SDESC", "'" + iREQ_TASK_REF_SDESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "APPLICABILITY_DESC", "'" + iAPPLICABILITY_DESC + "'" );
      lReqMap.put( "LAST_DEADLINE_DRIVER_BOOL", "'" + iLAST_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_DRIVER_BOOL", "'" + iSOFT_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL + "'" );
      lReqMap.put( "ENFORCE_WORKSCOPE_ORD_BOOL", "'" + iENFORCE_WORKSCOPE_ORD_BOOL + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'Y'" );
      lReqMap.put( "RESCHED_FROM_CD", "'EXECUTE'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'T'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // verify no error
      VerifyValidation();

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

      iTaskIDs_1 = null;
      iTaskDefnIDs_1 = null;

      // verify task_task
      String lquery = "select assmbl_bom_id from eqp_assmbl_bom where assmbl_bom_cd='"
            + iACFT_REQ_ATA_CD_TRK + "'";
      String assmblBomId = getStringValueFromQuery( lquery, "ASSMBL_BOM_ID" );
      taskIDs IDS = VerifyTask_TASK( iREQ_TASK_CD_1, iREQ_TASK_NAME_1, iCLASS_CD, iACFT_ASSMBLCD,
            assmblBomId, iSUBCLASS_CD, iORIGINATOR_CD, iAPPLICABILITY_DESC, "1" );

      iTaskDefnIDs_1 = IDS.getTASK_DEFN_IDs();
      iTaskIDs_1 = IDS.getTASK_IDs();

      // verify task_DEFN
      lquery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTaskDefnIDs_1.getNO_DB_ID() + " and TASK_DEFN_ID=" + iTaskDefnIDs_1.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lquery ) );

      // verify task_work_type
      verifyWorkType( iTaskIDs_1, iWORK_TYPE_LIST_1 );
      verifyWorkType( iTaskIDs_1, iWORK_TYPE_LIST_2 );

      // verify task_task_dep
      verifyTaskTaskDep( iTaskIDs_1, "CRT" );

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

      // C_REQ==========================================================================
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_1 + "'" );
      lReqMap.put( "REQ_TASK_DESC", "'" + iREQ_TASK_DESC_1 + "'" );
      lReqMap.put( "REQ_TASK_REF_SDESC", "'" + iREQ_TASK_REF_SDESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "APPLICABILITY_DESC", "'" + iAPPLICABILITY_DESC + "'" );
      lReqMap.put( "LAST_DEADLINE_DRIVER_BOOL", "'" + iLAST_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_DRIVER_BOOL", "'" + iSOFT_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL + "'" );
      lReqMap.put( "ENFORCE_WORKSCOPE_ORD_BOOL", "'" + iENFORCE_WORKSCOPE_ORD_BOOL + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'N'" );
      lReqMap.put( "RESCHED_FROM_CD", "'EXECUTE'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // verify no error
      VerifyValidation();

   }


   /**
    * This test is to verify OPER-15064 fix.REQs that are on-condition and non-recurring should be
    * loaded as non-unique.not on-condition and recurring should be unique.
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

      iTaskIDs_1 = null;
      iTaskDefnIDs_1 = null;

      // verify task_task
      String lquery = "select assmbl_bom_id from eqp_assmbl_bom where assmbl_bom_cd='"
            + iACFT_REQ_ATA_CD_TRK + "'";
      String assmblBomId = getStringValueFromQuery( lquery, "ASSMBL_BOM_ID" );
      taskIDs IDS = VerifyTask_TASK( iREQ_TASK_CD_1, iREQ_TASK_NAME_1, iCLASS_CD, iACFT_ASSMBLCD,
            assmblBomId, iSUBCLASS_CD, iORIGINATOR_CD, iAPPLICABILITY_DESC, "1" );

      iTaskDefnIDs_1 = IDS.getTASK_DEFN_IDs();
      iTaskIDs_1 = IDS.getTASK_IDs();

      // verify task_DEFN
      lquery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTaskDefnIDs_1.getNO_DB_ID() + " and TASK_DEFN_ID=" + iTaskDefnIDs_1.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lquery ) );

      // verify task_work_type
      verifyWorkType( iTaskIDs_1, iWORK_TYPE_LIST_1 );
      verifyWorkType( iTaskIDs_1, iWORK_TYPE_LIST_2 );

      // verify task_task_dep
      verifyTaskTaskDep( iTaskIDs_1, "CRT" );

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

      // C_REQ==========================================================================
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_1 + "'" );
      lReqMap.put( "REQ_TASK_DESC", "'" + iREQ_TASK_DESC_1 + "'" );
      lReqMap.put( "REQ_TASK_REF_SDESC", "'" + iREQ_TASK_REF_SDESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "APPLICABILITY_DESC", "'" + iAPPLICABILITY_DESC + "'" );
      lReqMap.put( "LAST_DEADLINE_DRIVER_BOOL", "'" + iLAST_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_DRIVER_BOOL", "'" + iSOFT_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL + "'" );
      lReqMap.put( "ENFORCE_WORKSCOPE_ORD_BOOL", "'" + iENFORCE_WORKSCOPE_ORD_BOOL + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'N'" );
      // lReqMap.put( "RESCHED_FROM_CD", "'EXECUTE'" );
      lReqMap.put( "RESCHED_FROM_CD", null );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // verify no error
      VerifyValidation();

   }


   /**
    * This test is to verify OPER-15064 fix.REQs that are on-condition and non-recurring should be
    * loaded as non-unique. not on-condition and no-recurring should be unique.
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

      iTaskIDs_1 = null;
      iTaskDefnIDs_1 = null;

      // verify task_task
      String lquery = "select assmbl_bom_id from eqp_assmbl_bom where assmbl_bom_cd='"
            + iACFT_REQ_ATA_CD_TRK + "'";
      String assmblBomId = getStringValueFromQuery( lquery, "ASSMBL_BOM_ID" );
      taskIDs IDS = VerifyTask_TASK( iREQ_TASK_CD_1, iREQ_TASK_NAME_1, iCLASS_CD, iACFT_ASSMBLCD,
            assmblBomId, iSUBCLASS_CD, iORIGINATOR_CD, iAPPLICABILITY_DESC, "1" );

      iTaskDefnIDs_1 = IDS.getTASK_DEFN_IDs();
      iTaskIDs_1 = IDS.getTASK_IDs();

      // verify task_DEFN
      lquery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTaskDefnIDs_1.getNO_DB_ID() + " and TASK_DEFN_ID=" + iTaskDefnIDs_1.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lquery ) );

      // verify task_work_type
      verifyWorkType( iTaskIDs_1, iWORK_TYPE_LIST_1 );
      verifyWorkType( iTaskIDs_1, iWORK_TYPE_LIST_2 );

      // verify task_task_dep
      verifyTaskTaskDepNONExist( iTaskIDs_1, "CRT" );

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

      // C_REQ==========================================================================
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_1 + "'" );
      lReqMap.put( "REQ_TASK_DESC", "'" + iREQ_TASK_DESC_1 + "'" );
      lReqMap.put( "REQ_TASK_REF_SDESC", "'" + iREQ_TASK_REF_SDESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "APPLICABILITY_DESC", "'" + iAPPLICABILITY_DESC + "'" );
      lReqMap.put( "LAST_DEADLINE_DRIVER_BOOL", "'" + iLAST_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_DRIVER_BOOL", "'" + iSOFT_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL + "'" );
      lReqMap.put( "ENFORCE_WORKSCOPE_ORD_BOOL", "'" + iENFORCE_WORKSCOPE_ORD_BOOL + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'Y'" );
      // lReqMap.put( "RESCHED_FROM_CD", "'EXECUTE'" );
      lReqMap.put( "RESCHED_FROM_CD", null );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // verify no error
      VerifyValidation();

   }


   @Test
   public void testOPER_15064_4_IMPORT() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOPER_15064_4_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTaskIDs_1 = null;
      iTaskDefnIDs_1 = null;

      // verify task_task
      String lquery = "select assmbl_bom_id from eqp_assmbl_bom where assmbl_bom_cd='"
            + iACFT_REQ_ATA_CD_TRK + "'";
      String assmblBomId = getStringValueFromQuery( lquery, "ASSMBL_BOM_ID" );
      taskIDs IDS = VerifyTask_TASK( iREQ_TASK_CD_1, iREQ_TASK_NAME_1, iCLASS_CD, iACFT_ASSMBLCD,
            assmblBomId, iSUBCLASS_CD, iORIGINATOR_CD, iAPPLICABILITY_DESC );

      iTaskDefnIDs_1 = IDS.getTASK_DEFN_IDs();
      iTaskIDs_1 = IDS.getTASK_IDs();

      VerifyTask_TASK( iREQ_TASK_CD_1, iREQ_TASK_NAME_1, iCLASS_CD, iACFT_ASSMBLCD, assmblBomId,
            iSUBCLASS_CD, iORIGINATOR_CD, iAPPLICABILITY_DESC, "0" );

      // verify task_DEFN
      lquery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTaskDefnIDs_1.getNO_DB_ID() + " and TASK_DEFN_ID=" + iTaskDefnIDs_1.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lquery ) );

      // verify task_work_type
      verifyWorkType( iTaskIDs_1, iWORK_TYPE_LIST_1 );
      verifyWorkType( iTaskIDs_1, iWORK_TYPE_LIST_2 );

      // verify task_task_dep
      verifyTaskTaskDepNONExist( iTaskIDs_1, "CRT" );

   }


   /**
    * This test is to verify req_import.validate_req functionality of staging tables: c_req,
    * c_req_labour, c_req_part, c_req_tool, c_req_step and c_req_step_skill. REQ is on TRK of ACFT
    *
    * @throws SQLException
    *
    *
    */

   @Test
   public void testACFT_EXPORT() throws SQLException {
      System.out.println(
            "=======Starting:  " + testName.getMethodName() + " ========================" );

      testACFT_IMPORT();
      clearBaselineLoaderTables();

      // run validation
      Assert.assertTrue( runExport() == 1 );

      // Verify c_req
      String lQuery = "select 1 from " + TableUtil.C_REQ + " where REQ_TASK_CD='" + iREQ_TASK_CD_1
            + "' and RECURRING_TASK_BOOL='" + "N" + "'";
      Assert.assertTrue( "Check C_REQ table to verify the record exists", RecordsExist( lQuery ) );

      // Verify C_REQ_STEP
      lQuery = "select 1 from " + TableUtil.C_REQ_STEP + " where REQ_TASK_CD='" + iREQ_TASK_CD_1
            + "' and JOB_STEP_ORD='" + 1 + "'";
      Assert.assertTrue( "Check C_REQ_STEP table to verify the record exists",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.C_REQ_STEP + " where REQ_TASK_CD='" + iREQ_TASK_CD_1
            + "' and JOB_STEP_ORD='" + 2 + "'";
      Assert.assertTrue( "Check C_REQ_STEP table to verify the record exists",
            RecordsExist( lQuery ) );

      // Verify C_REQ_LABOUR
      lQuery = "select 1 from " + TableUtil.C_REQ_LABOUR + " where REQ_TASK_CD='" + iREQ_TASK_CD_1
            + "' and LABOUR_SKILL_CD='" + iLABOR_SKILL_CD_LBR + "'";
      Assert.assertTrue( "Check C_REQ_LABOUR table to verify the record exists",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.C_REQ_LABOUR + " where REQ_TASK_CD='" + iREQ_TASK_CD_1
            + "' and LABOUR_SKILL_CD='" + iLABOR_SKILL_CD + "'";
      Assert.assertTrue( "Check C_REQ_LABOUR table to verify the record exists",
            RecordsExist( lQuery ) );

      // Verify C_REQ_STEP_SKILL
      lQuery = "select 1 from " + TableUtil.C_REQ_STEP_SKILL + " where REQ_TASK_CD='"
            + iREQ_TASK_CD_1 + "' and STEP_SKILL_CD='" + iLABOR_SKILL_CD_LBR
            + "' and JOB_STEP_ORD='" + 1 + "'";
      Assert.assertTrue( "Check C_REQ_STEP_SKILL table to verify the record exists",
            RecordsExist( lQuery ) );

      lQuery =
            "select 1 from " + TableUtil.C_REQ_STEP_SKILL + " where REQ_TASK_CD='" + iREQ_TASK_CD_1
                  + "' and STEP_SKILL_CD='" + iLABOR_SKILL_CD + "' and JOB_STEP_ORD='" + 2 + "'";
      Assert.assertTrue( "Check C_REQ_STEP_SKILL table to verify the record exists",
            RecordsExist( lQuery ) );

      // Verify c_req_part
      lQuery = "select 1 from " + TableUtil.C_REQ_PART + " where PART_NO_OEM='" + iPART_NO_OEM_1
            + "' and REQ_ACTION_CD='" + iREQ_ACTION_CD_NOREQ + "' and REQ_QT='" + iREQ_QT + "'";
      Assert.assertTrue( "Check C_REQ_PART, table to verify the record exists",
            RecordsExist( lQuery ) );

      // Verify C_REQ_TOOL
      lQuery = "select 1 from " + TableUtil.C_REQ_TOOL + " where PART_NO_OEM='" + iPART_NO_OEM_10
            + "' and MANUFACT_REF='" + iMANUFACT_REF_10 + "' and REQ_HR='" + iREQ_HR_INT + "'";
      Assert.assertTrue( "Check C_REQ_TOOL table to verify the record exists",
            RecordsExist( lQuery ) );

   }


   /**
    *
    * To test OPER-30664 - this fix will correct the order of IETM assigned to particular task. It
    * will re-assign a sequential number during import.
    *
    * @throws Exception
    */
   @Test
   public void testREQ_IETM_OPER_30664_VALIDATION() throws Exception {

      Map<String, String> lc_REQMap = new LinkedHashMap<>();

      // C_REQ
      lc_REQMap.clear();
      lc_REQMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lc_REQMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lc_REQMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lc_REQMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lc_REQMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_1 + "'" );
      lc_REQMap.put( "REQ_TASK_DESC", "'" + iREQ_TASK_DESC_1 + "'" );
      lc_REQMap.put( "LAST_DEADLINE_DRIVER_BOOL", "'" + iLAST_DEADLINE_DRIVER_BOOL + "'" );
      lc_REQMap.put( "SOFT_DEADLINE_DRIVER_BOOL", "'" + iSOFT_DEADLINE_DRIVER_BOOL + "'" );
      lc_REQMap.put( "RECURRING_TASK_BOOL", "'" + "N" + "'" );
      lc_REQMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL + "'" );
      lc_REQMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lc_REQMap.put( "ENFORCE_WORKSCOPE_ORD_BOOL", "'" + iENFORCE_WORKSCOPE_ORD_BOOL + "'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lc_REQMap ) );

      // C_REQ_IETM_TOPIC #1
      lc_REQMap.clear();
      lc_REQMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lc_REQMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lc_REQMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lc_REQMap.put( "REQ_IETM_CD", "'" + iREQ_IETM_CD_1 + "'" );
      lc_REQMap.put( "REQ_TOPIC_SDESC", "'" + iREQ_TOPIC_SDESC_1 + "'" );
      lc_REQMap.put( "REQ_IETM_ORD", "'" + iREQ_IETM_ORD_1 + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_IETM_TOPIC, lc_REQMap ) );

      // C_REQ_IETM_TOPIC #2
      lc_REQMap.clear();
      lc_REQMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lc_REQMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lc_REQMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lc_REQMap.put( "REQ_IETM_CD", "'" + iREQ_IETM_CD_2 + "'" );
      lc_REQMap.put( "REQ_TOPIC_SDESC", "'" + iREQ_TOPIC_SDESC_2 + "'" );
      lc_REQMap.put( "REQ_IETM_ORD", "'" + iREQ_IETM_ORD_2 + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_IETM_TOPIC, lc_REQMap ) );

      // C_REQ_IETM_TOPIC #3
      lc_REQMap.clear();
      lc_REQMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lc_REQMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lc_REQMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lc_REQMap.put( "REQ_IETM_CD", "'" + iREQ_IETM_CD_3 + "'" );
      lc_REQMap.put( "REQ_TOPIC_SDESC", "'" + iREQ_TOPIC_SDESC_3 + "'" );
      lc_REQMap.put( "REQ_IETM_ORD", "'" + iREQ_IETM_ORD_3 + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_IETM_TOPIC, lc_REQMap ) );

      // C_REQ_IETM_TOPIC #4
      lc_REQMap.clear();
      lc_REQMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lc_REQMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lc_REQMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lc_REQMap.put( "REQ_IETM_CD", "'" + iREQ_IETM_CD_4 + "'" );
      lc_REQMap.put( "REQ_TOPIC_SDESC", "'" + iREQ_TOPIC_SDESC_4 + "'" );
      lc_REQMap.put( "REQ_IETM_ORD", "'" + iREQ_IETM_ORD_4 + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_IETM_TOPIC, lc_REQMap ) );

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
   public void testREQ_IETM_OPER_30664_IMPORT() throws Exception {

      System.out.println( "=======Starting: " + testName.getMethodName()
            + " Validation========================" );

      testREQ_IETM_OPER_30664_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTaskIDs_1 = null;

      verifyIETMs( iREQ_TASK_CD_1, iREQ_IETM_CD_1, 1 );
      verifyIETMs( iREQ_TASK_CD_1, iREQ_IETM_CD_2, 2 );
      verifyIETMs( iREQ_TASK_CD_1, iREQ_IETM_CD_3, 4 );
      verifyIETMs( iREQ_TASK_CD_1, iREQ_IETM_CD_4, 3 );
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

      if ( iTaskIDs_1 == null ) {
         lQuery =
               "select task_db_id, task_id from task_task where task_cd = '" + aREQ_TASK_CD_1 + "'";
         iTaskIDs_1 = getIDs( lQuery, "TASK_DB_ID", "TASK_ID" );
      }
      lQuery = "select ietm_db_id, ietm_id from ietm_ietm where ietm_ietm.ietm_cd = '"
            + aREQ_IETM_CD_1 + "'";
      lIetm_IDs = getIDs( lQuery, "IETM_DB_ID", "IETM_ID" );

      lQuery =
            "select ietm_ord from task_task_ietm where  task_db_id = '" + iTaskIDs_1.getNO_DB_ID()
                  + "' and task_id = '" + iTaskIDs_1.getNO_ID() + "' and ietm_db_id = '"
                  + lIetm_IDs.getNO_DB_ID() + "' and ietm_id = '" + lIetm_IDs.getNO_ID() + "'";
      Assert.assertTrue( "IETM order value is incorrect.",
            getIntValueFromQuery( lQuery, "IETM_ORD" ) == aIETM_ord );
   }


   /**
    * This function is to verify TASK_TASK table
    *
    *
    */
   public taskIDs VerifyTask_TASK( String aTASK_CD, String aTASK_NAME, String aTask_CLASS_CD,
         String aASSMBL_CD, String aASSMBL_BOM_ID, String aTASK_SUBCLASS_CD,
         String aTASK_ORIGINATOR_CD, String aTASK_APPL_LDESC ) {

      String[] iIds = { "TASK_DEFN_DB_ID", "TASK_DEFN_ID", "TASK_DB_ID", "TASK_ID", "TASK_CLASS_CD",
            "ASSMBL_CD", "ASSMBL_BOM_ID", "TASK_SUBCLASS_CD", "TASK_ORIGINATOR_CD",
            "TASK_APPL_EFF_LDESC" };

      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_CD", aTASK_CD );
      lArgs.addArguments( "TASK_NAME", aTASK_NAME );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_TASK, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      taskIDs lIds =
            new taskIDs( new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) ),
                  new simpleIDs( llists.get( 0 ).get( 2 ), llists.get( 0 ).get( 3 ) ) );

      Assert.assertTrue( "TASK_CLASS_CD",
            llists.get( 0 ).get( 4 ).equalsIgnoreCase( aTask_CLASS_CD ) );
      Assert.assertTrue( "ASSMBL_CD", llists.get( 0 ).get( 5 ).equalsIgnoreCase( aASSMBL_CD ) );
      Assert.assertTrue( "ASSMBL_BOM_ID ",
            llists.get( 0 ).get( 6 ).equalsIgnoreCase( aASSMBL_BOM_ID ) );
      Assert.assertTrue( "TASK_SUBCLASS_CD ",
            llists.get( 0 ).get( 7 ).equalsIgnoreCase( aTASK_SUBCLASS_CD ) );
      Assert.assertTrue( "TASK_ORIGINATOR_CD ",
            llists.get( 0 ).get( 8 ).equalsIgnoreCase( aTASK_ORIGINATOR_CD ) );
      Assert.assertTrue( "TASK_APPL_EFF_LDESC ",
            llists.get( 0 ).get( 9 ).equalsIgnoreCase( aTASK_APPL_LDESC ) );

      return lIds;
   }


   /**
    * This function is to verify TASK_TASK table
    *
    *
    */
   public taskIDs VerifyTask_TASK( String aTASK_CD, String aTASK_NAME, String aTask_CLASS_CD,
         String aASSMBL_CD, String aASSMBL_BOM_ID, String aTASK_SUBCLASS_CD,
         String aTASK_ORIGINATOR_CD, String aTASK_APPL_LDESC, String aUNIQUE_BOOL ) {

      String[] iIds = { "TASK_DEFN_DB_ID", "TASK_DEFN_ID", "TASK_DB_ID", "TASK_ID", "TASK_CLASS_CD",
            "ASSMBL_CD", "ASSMBL_BOM_ID", "TASK_SUBCLASS_CD", "TASK_ORIGINATOR_CD",
            "TASK_APPL_EFF_LDESC", "UNIQUE_BOOL" };

      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_CD", aTASK_CD );
      lArgs.addArguments( "TASK_NAME", aTASK_NAME );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_TASK, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      taskIDs lIds =
            new taskIDs( new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) ),
                  new simpleIDs( llists.get( 0 ).get( 2 ), llists.get( 0 ).get( 3 ) ) );

      Assert.assertTrue( "TASK_CLASS_CD",
            llists.get( 0 ).get( 4 ).equalsIgnoreCase( aTask_CLASS_CD ) );
      Assert.assertTrue( "ASSMBL_CD", llists.get( 0 ).get( 5 ).equalsIgnoreCase( aASSMBL_CD ) );
      Assert.assertTrue( "ASSMBL_BOM_ID ",
            llists.get( 0 ).get( 6 ).equalsIgnoreCase( aASSMBL_BOM_ID ) );
      Assert.assertTrue( "TASK_SUBCLASS_CD ",
            llists.get( 0 ).get( 7 ).equalsIgnoreCase( aTASK_SUBCLASS_CD ) );
      Assert.assertTrue( "TASK_ORIGINATOR_CD ",
            llists.get( 0 ).get( 8 ).equalsIgnoreCase( aTASK_ORIGINATOR_CD ) );
      Assert.assertTrue( "TASK_APPL_EFF_LDESC ",
            llists.get( 0 ).get( 9 ).equalsIgnoreCase( aTASK_APPL_LDESC ) );
      Assert.assertTrue( "UNIQUE_BOOL ",
            llists.get( 0 ).get( 10 ).equalsIgnoreCase( aUNIQUE_BOOL ) );

      return lIds;
   }


   /**
    * This function is to verify TASK_LABOUR_LIST and retrieve task defn IDs table
    *
    *
    */
   public void verifyTASKLABOURLIST( simpleIDs aTaskIds, String aLabourSkillCD, String aManPerCt,
         String aWorkPerfHR, String aInspHR, String aCertHR ) {

      String[] iIds = { "LABOUR_SKILL_CD", "MAN_PWR_CT", "WORK_PERF_HR", "INSP_HR", "CERT_HR" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_DB_ID", aTaskIds.getNO_DB_ID() );
      lArgs.addArguments( "TASK_ID", aTaskIds.getNO_ID() );
      lArgs.addArguments( "LABOUR_SKILL_CD", aLabourSkillCD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_LABOUR_LIST, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "LABOUR_SKILL_CD",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aLabourSkillCD ) );
      Assert.assertTrue( "MAN_PWR_CT", llists.get( 0 ).get( 1 ).equalsIgnoreCase( aManPerCt ) );
      Assert.assertTrue( "WORK_PERF_HR", llists.get( 0 ).get( 2 ).equalsIgnoreCase( aWorkPerfHR ) );
      Assert.assertTrue( "INSP_HR", llists.get( 0 ).get( 3 ).equalsIgnoreCase( aInspHR ) );
      Assert.assertTrue( "CERT_HR", llists.get( 0 ).get( 4 ).equalsIgnoreCase( aCertHR ) );

   }


   /**
    * This function is to verify TASK_STEP table
    *
    *
    */

   public void VerifyTask_Step( simpleIDs aTaskIds, String aSTEP_ORD, String aSTEP_LDESC ) {

      String[] iIds = { "STEP_LDESC" };

      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_DB_ID", aTaskIds.getNO_DB_ID() );
      lArgs.addArguments( "TASK_ID", aTaskIds.getNO_ID() );
      lArgs.addArguments( "STEP_ORD", aSTEP_ORD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_STEP, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "STEP_LDESC", llists.get( 0 ).get( 0 ).equalsIgnoreCase( aSTEP_LDESC ) );
   }


   /**
    * This function is to verify TASK_PART_LIST table
    *
    *
    */
   public void verifyTASKPARTLIST( simpleIDs aTaskIds, String aASSMBL_CD, String aASSMBL_BOM_ID,
         simpleIDs aPART_NO_IDs, String aREMOVE_REASON_CD, String aPART_PROVIDER_TYPE_CD,
         String aREQ_QT, String aREMOVE_BOOL, String aINSTALL_BOOL, String aREQ_ACTION_CD,
         String aREQ_PRIORITY_CD, simpleIDs aBOMPartNoIds ) {

      String[] iIds = { "ASSMBL_CD", "ASSMBL_BOM_ID", "SPEC_PART_NO_DB_ID", "SPEC_PART_NO_ID",
            "REMOVE_REASON_CD", "PART_PROVIDER_TYPE_CD", "REQ_QT", "REMOVE_BOOL", "INSTALL_BOOL",
            "REQ_ACTION_CD", "REQ_PRIORITY_CD", "BOM_PART_DB_ID", "BOM_PART_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_DB_ID", aTaskIds.getNO_DB_ID() );
      lArgs.addArguments( "TASK_ID", aTaskIds.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_PART_LIST, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "ASSMBL_CD", llists.get( 0 ).get( 0 ).equalsIgnoreCase( aASSMBL_CD ) );
      Assert.assertTrue( "ASSMBL_BOM_ID",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( aASSMBL_BOM_ID ) );
      Assert.assertTrue( "SPEC_PART_NO_DB_ID",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aPART_NO_IDs.getNO_DB_ID() ) );
      Assert.assertTrue( "SPEC_PART_NO_ID",
            llists.get( 0 ).get( 3 ).equalsIgnoreCase( aPART_NO_IDs.getNO_ID() ) );
      Assert.assertTrue( "REMOVE_REASON_CD",
            llists.get( 0 ).get( 4 ).equalsIgnoreCase( aREMOVE_REASON_CD ) );
      Assert.assertTrue( "PART_PROVIDE_TYPE_CD",
            llists.get( 0 ).get( 5 ).equalsIgnoreCase( aPART_PROVIDER_TYPE_CD ) );
      Assert.assertTrue( "REQ_QT", llists.get( 0 ).get( 6 ).equalsIgnoreCase( aREQ_QT ) );
      Assert.assertTrue( "REMOVE_BOOL", llists.get( 0 ).get( 7 ).equalsIgnoreCase( aREMOVE_BOOL ) );
      Assert.assertTrue( "INSTALL_BOOL",
            llists.get( 0 ).get( 8 ).equalsIgnoreCase( aINSTALL_BOOL ) );
      Assert.assertTrue( "REQ_ACTION_CD",
            llists.get( 0 ).get( 9 ).equalsIgnoreCase( aREQ_ACTION_CD ) );
      Assert.assertTrue( "REQ_PRIORITY_CD",
            llists.get( 0 ).get( 10 ).equalsIgnoreCase( aREQ_PRIORITY_CD ) );
      Assert.assertTrue( "REQ_PRIORITY_CD",
            llists.get( 0 ).get( 10 ).equalsIgnoreCase( aREQ_PRIORITY_CD ) );
      Assert.assertTrue( "BOM_PART_DB_ID",
            llists.get( 0 ).get( 11 ).equalsIgnoreCase( aBOMPartNoIds.getNO_DB_ID() ) );
      Assert.assertTrue( "BOM_PART_ID",
            llists.get( 0 ).get( 12 ).equalsIgnoreCase( aBOMPartNoIds.getNO_ID() ) );

   }


   /**
    * This function is to verify TASK_PART_LIST table
    *
    *
    */
   public void verifyTASKPARTLISTBATCH( simpleIDs aTaskIds, simpleIDs aPART_NO_IDs,
         simpleIDs aBOMPartNoIds ) {

      String[] iIds = { "SPEC_PART_NO_DB_ID", "SPEC_PART_NO_ID", "BOM_PART_DB_ID", "BOM_PART_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_DB_ID", aTaskIds.getNO_DB_ID() );
      lArgs.addArguments( "TASK_ID", aTaskIds.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_PART_LIST, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "SPEC_PART_NO_DB_ID",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aPART_NO_IDs.getNO_DB_ID() ) );
      Assert.assertTrue( "SPEC_PART_NO_ID",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( aPART_NO_IDs.getNO_ID() ) );
      Assert.assertTrue( "BOM_PART_DB_ID",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aBOMPartNoIds.getNO_DB_ID() ) );
      Assert.assertTrue( "BOM_PART_ID",
            llists.get( 0 ).get( 3 ).equalsIgnoreCase( aBOMPartNoIds.getNO_ID() ) );

   }


   /**
    * This function is to verify task_work_type table
    *
    *
    */
   public void verifyWorkType( simpleIDs aIds, String aType ) {
      String lQuery =
            "select 1 from " + TableUtil.TASK_WORK_TYPE + " where TASK_DB_ID=" + aIds.getNO_DB_ID()
                  + " and TASK_ID=" + aIds.getNO_ID() + " and WORK_TYPE_CD='" + aType + "'";
      Assert.assertTrue( "Check task_work_type table to verify the record exists",
            RecordsExist( lQuery ) );
   }


   /**
    * This function is to verify task_task_dep table
    *
    *
    */
   public void verifyTaskTaskDep( simpleIDs aIds, String aAction_CD ) {
      String lQuery = "select 1 from " + TableUtil.TASK_TASK_DEP + " where TASK_DB_ID="
            + aIds.getNO_DB_ID() + " and TASK_ID=" + aIds.getNO_ID() + " and TASK_DEP_ACTION_CD='"
            + aAction_CD + "'";
      Assert.assertTrue( "Check task_task_dep table to verify the record exists",
            RecordsExist( lQuery ) );
   }


   /**
    * This function is to verify task_task_dep table
    *
    *
    */
   public void verifyTaskTaskDepNONExist( simpleIDs aIds, String aAction_CD ) {
      String lQuery = "select 1 from " + TableUtil.TASK_TASK_DEP + " where TASK_DB_ID="
            + aIds.getNO_DB_ID() + " and TASK_ID=" + aIds.getNO_ID() + " and TASK_DEP_ACTION_CD='"
            + aAction_CD + "'";
      Assert.assertFalse( "Check task_task_dep table to verify the record exists",
            RecordsExist( lQuery ) );
   }


   /**
    * This function is to verify TASK_TOOL table
    *
    *
    */
   public void VerifyTask_Tool( simpleIDs aTaskIds, String aBOM_PART_ID, String aSCHED_HR ) {

      String[] iIds = { "BOM_PART_ID", "SCHED_HR" };

      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_DB_ID", aTaskIds.getNO_DB_ID() );
      lArgs.addArguments( "TASK_ID", aTaskIds.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_TOOL_LIST, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "BOM_PART_ID", llists.get( 0 ).get( 0 ).equalsIgnoreCase( aBOM_PART_ID ) );
      Assert.assertTrue( "SCHED_HR", llists.get( 0 ).get( 1 ).equalsIgnoreCase( aSCHED_HR ) );
   }

}
