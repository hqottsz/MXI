package com.mxi.mx.core.maint.plan.baselineloader.REQ.validations;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.mxi.mx.core.maint.plan.baselineloader.REQ.ReqTests;
import com.mxi.mx.util.TableUtil;


/**
 * This test is to verify req_import.validate_req functionality of staging tables: c_req and
 * c_req_step_skill.
 *
 *
 */
public class ReqStepSkill extends ReqTests {

   @Rule
   public TestName testName = new TestName();


   /**
    * Clean up after each individual test
    *
    */
   @After
   @Override
   public void after() {
      // RestoreData();
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
    * This test is to verify req_import.validate_req functionality of staging tables: c_req and
    * c_req_step_skill. REQ is on TRK of ACFT
    *
    *
    */

   @Test
   public void testACFT_INSP_Sucessful() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
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
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // C_REQ_STEP
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'1'" );
      lReqMap.put( "JOB_STEP_DESC", "'Step 1'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP, lReqMap ) );

      // C_REQ_LABOR
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

      // C_REQ_STEP_SKILL
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'1'" );
      lReqMap.put( "STEP_SKILL_CD", "'" + iLABOR_SKILL_CD_LBR + "'" );
      lReqMap.put( "INSP_BOOL", "'" + iINSP_BOOL_Y + "'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP_SKILL, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // verify no error
      VerifyValidation();

   }


   /**
    * This test is to verify req_import.validate_req functionality of staging tables: c_req and
    * c_req_step_skill. REQ is on TRK of ACFT
    *
    *
    */

   @Test
   public void testACFT_NON_INSP_Sucessful() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
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
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // C_REQ_STEP
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'1'" );
      lReqMap.put( "JOB_STEP_DESC", "'Step 1'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP, lReqMap ) );

      // C_REQ_LABOR
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "LABOUR_SKILL_CD", "'" + iLABOR_SKILL_CD_LBR + "'" );
      lReqMap.put( "MAN_PWR_CT", "'" + iMAN_PWR_CT + "'" );
      lReqMap.put( "SCHED_WORK_HRS", "'" + iSCHED_WORK_HRS + "'" );
      // lReqMap.put( "SCHED_INSP_HRS", "'" + iSCHED_INSP_HRS + "'" );
      lReqMap.put( "SCHED_INSP_HRS", null );
      lReqMap.put( "SCHED_CERT_HRS", "'" + iSCHED_CERT_HRS + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_LABOUR, lReqMap ) );

      // C_REQ_STEP_SKILL
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'1'" );
      lReqMap.put( "STEP_SKILL_CD", "'" + iLABOR_SKILL_CD_LBR + "'" );
      lReqMap.put( "INSP_BOOL", "'" + iINSP_BOOL_N + "'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP_SKILL, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // verify no error
      VerifyValidation();

   }


   /**
    * This test is to verify req_import.validate_req functionality of staging tables: c_req and
    * c_req_step_skill. REQ is on TRK of ACFT
    *
    *
    */
   @Ignore
   @Test
   public void testACFT_NON_INSP_2_Sucessful() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
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
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // C_REQ_STEP
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'1'" );
      lReqMap.put( "JOB_STEP_DESC", "'Step 1'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP, lReqMap ) );

      // C_REQ_LABOR
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "LABOUR_SKILL_CD", "'" + iLABOR_SKILL_CD_LBR + "'" );
      lReqMap.put( "MAN_PWR_CT", "'" + iMAN_PWR_CT + "'" );
      lReqMap.put( "SCHED_WORK_HRS", "'" + iSCHED_WORK_HRS + "'" );
      // lReqMap.put( "SCHED_INSP_HRS", "'" + iSCHED_INSP_HRS + "'" );
      lReqMap.put( "SCHED_INSP_HRS", "'0'" );
      lReqMap.put( "SCHED_CERT_HRS", "'" + iSCHED_CERT_HRS + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_LABOUR, lReqMap ) );

      // C_REQ_STEP_SKILL
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'1'" );
      lReqMap.put( "STEP_SKILL_CD", "'" + iLABOR_SKILL_CD_LBR + "'" );
      // lReqMap.put( "INSP_BOOL", "'" + iINSP_BOOL_N + "'" );
      lReqMap.put( "INSP_BOOL", null );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP_SKILL, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // verify no error
      VerifyValidation();

   }


   /**
    * This test is to verify req_import.validate_req functionality of staging tables: c_req and
    * c_req_step_skill. REQ is on TRK of ACFT
    *
    *
    */

   @Test
   public void testACFT_NON_INSP_3_Sucessful() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
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
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // C_REQ_STEP
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'1'" );
      lReqMap.put( "JOB_STEP_DESC", "'Step 1'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP, lReqMap ) );

      // C_REQ_LABOR
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

      // C_REQ_STEP_SKILL
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'1'" );
      lReqMap.put( "STEP_SKILL_CD", "'" + iLABOR_SKILL_CD_LBR + "'" );
      lReqMap.put( "INSP_BOOL", "'" + iINSP_BOOL_N + "'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP_SKILL, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // verify no error
      VerifyValidation();

   }


   /**
    * This test is to verify req_import.validate_req functionality of staging tables: c_req and
    * c_req_step_skill. REQ is on TRK of ACFT
    *
    *
    */

   @Test
   public void testACFT_NON_INSP_4_Sucessful() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
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
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // C_REQ_STEP
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'1'" );
      lReqMap.put( "JOB_STEP_DESC", "'Step 1'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP, lReqMap ) );

      // C_REQ_LABOR
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

      // C_REQ_STEP_SKILL
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'1'" );
      lReqMap.put( "STEP_SKILL_CD", "'" + iLABOR_SKILL_CD_LBR + "'" );
      // lReqMap.put( "INSP_BOOL", "'" + iINSP_BOOL_N + "'" );
      lReqMap.put( "INSP_BOOL", null );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP_SKILL, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // verify no error
      VerifyValidation();

   }


   /**
    * This test is to verify req_import.validate_req functionality of staging tables: c_req and
    * c_req_step_skill. REQ is on SYS of ENG
    *
    *
    */

   @Test
   public void testENG_INSP_Sucessful() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
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
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // C_REQ_STEP
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iENG_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_3 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'1'" );
      lReqMap.put( "JOB_STEP_DESC", "'Step 1'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP, lReqMap ) );

      // C_REQ_LABOR
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

      // C_REQ_STEP_SKILL
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iENG_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_3 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'1'" );
      lReqMap.put( "STEP_SKILL_CD", "'" + iLABOR_SKILL_CD_LBR + "'" );
      lReqMap.put( "INSP_BOOL", "'" + iINSP_BOOL_Y + "'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP_SKILL, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // verify no error
      VerifyValidation();

   }


   /**
    * This test is to verify req_import.validate_req functionality of staging tables: c_req and
    * c_req_step_skill. REQ is on TRK of ACFT
    *
    *
    */

   @Test
   public void testACFT_INSP_MULTIPLE_STEPS_Sucessful() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
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
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // C_REQ_STEP for step 1
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'1'" );
      lReqMap.put( "JOB_STEP_DESC", "'Step 1'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP, lReqMap ) );

      // C_REQ_STEP for step 2
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'2'" );
      lReqMap.put( "JOB_STEP_DESC", "'Step 2'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP, lReqMap ) );

      // C_REQ_LABOR
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

      // C_REQ_STEP_SKILL for step 1
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
      lReqMap.put( "STEP_SKILL_CD", "'" + iLABOR_SKILL_CD_LBR + "'" );
      lReqMap.put( "INSP_BOOL", "'" + iINSP_BOOL_Y + "'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP_SKILL, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // verify no error
      VerifyValidation();

   }


   /**
    * This test is to verify req_import.validate_req functionality of staging tables: c_req and
    * c_req_step_skill. REQ is on TRK of ACFT
    *
    *
    */

   @Test
   public void testACFT_INSP_MULTIPLE_STEPS_SKILL_Sucessful() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
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
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // C_REQ_STEP for step 1
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'1'" );
      lReqMap.put( "JOB_STEP_DESC", "'Step 1'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP, lReqMap ) );

      // C_REQ_STEP for step 2
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'2'" );
      lReqMap.put( "JOB_STEP_DESC", "'Step 2'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP, lReqMap ) );

      // C_REQ_LABOR for fist skill
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

      // C_REQ_STEP_SKILL for step 1
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

      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // verify no error
      VerifyValidation();

   }


   /**
    * This test is to verify req_import.validate_req functionality of staging tables: c_req and
    * c_req_step_skill. REQ is on TRK of ACFT
    *
    *
    */

   @Test
   public void test_BLREQ_12325_1() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
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
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // C_REQ_STEP
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'1'" );
      lReqMap.put( "JOB_STEP_DESC", "'Step 1'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP, lReqMap ) );

      // C_REQ_LABOR
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

      // C_REQ_STEP_SKILL
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'1'" );
      lReqMap.put( "STEP_SKILL_CD", "'" + iLABOR_SKILL_CD_LBR + "'" );
      lReqMap.put( "INSP_BOOL", "'1'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP_SKILL, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-12315" );

   }


   /**
    * This test is to verify req_import.validate_req functionality of staging tables: c_req and
    * c_req_step_skill. REQ is on TRK of ACFT
    *
    *
    */

   @Test
   public void test_BLREQ_12325_2() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
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
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // C_REQ_STEP
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'1'" );
      lReqMap.put( "JOB_STEP_DESC", "'Step 1'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP, lReqMap ) );

      // C_REQ_LABOR
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "LABOUR_SKILL_CD", "'" + iLABOR_SKILL_CD_LBR + "'" );
      lReqMap.put( "MAN_PWR_CT", "'" + iMAN_PWR_CT + "'" );
      lReqMap.put( "SCHED_WORK_HRS", "'" + iSCHED_WORK_HRS + "'" );
      // lReqMap.put( "SCHED_INSP_HRS", "'" + iSCHED_INSP_HRS + "'" );
      lReqMap.put( "SCHED_INSP_HRS", null );
      lReqMap.put( "SCHED_CERT_HRS", "'" + iSCHED_CERT_HRS + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_LABOUR, lReqMap ) );

      // C_REQ_STEP_SKILL
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'1'" );
      lReqMap.put( "STEP_SKILL_CD", "'" + iLABOR_SKILL_CD_LBR + "'" );
      lReqMap.put( "INSP_BOOL", "'0'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP_SKILL, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-12315" );
   }


   /**
    * This test is to verify error code BLREQ-10706:C_REQ_STEP_SKILL.ASSMBL_CD must be provided.
    *
    */

   @Test
   public void test_BLREQ_10706() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
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
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // C_REQ_STEP
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'1'" );
      lReqMap.put( "JOB_STEP_DESC", "'Step 1'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP, lReqMap ) );

      // C_REQ_LABOR
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

      // C_REQ_STEP_SKILL
      lReqMap.clear();
      // lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ASSMBL_CD", null );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'1'" );
      lReqMap.put( "STEP_SKILL_CD", "'" + iLABOR_SKILL_CD_LBR + "'" );
      lReqMap.put( "INSP_BOOL", "'" + iINSP_BOOL_Y + "'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP_SKILL, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-10706" );

   }


   /**
    * This test is to verify error code BLREQ-10707:C_REQ_STEP_SKILL.REQ_ATA_CD must be provided.
    *
    */

   @Test
   public void test_BLREQ_10707() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
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
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // C_REQ_STEP
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'1'" );
      lReqMap.put( "JOB_STEP_DESC", "'Step 1'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP, lReqMap ) );

      // C_REQ_LABOR
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "LABOUR_SKILL_CD", "'" + iLABOR_SKILL_CD_LBR + "'" );
      lReqMap.put( "MAN_PWR_CT", "'" + iMAN_PWR_CT + "'" );
      lReqMap.put( "SCHED_WORK_HRS", "'" + iSCHED_WORK_HRS + "'" );
      // lReqMap.put( "SCHED_INSP_HRS", "'" + iSCHED_INSP_HRS + "'" );
      lReqMap.put( "SCHED_INSP_HRS", null );
      lReqMap.put( "SCHED_CERT_HRS", "'" + iSCHED_CERT_HRS + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_LABOUR, lReqMap ) );

      // C_REQ_STEP_SKILL
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      // lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_ATA_CD", null );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'1'" );
      lReqMap.put( "STEP_SKILL_CD", "'" + iLABOR_SKILL_CD_LBR + "'" );
      lReqMap.put( "INSP_BOOL", "'" + iINSP_BOOL_N + "'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP_SKILL, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-10707" );

   }


   /**
    * This test is to verify error code BLREQ-10708:C_REQ_STEP_SKILL.REQ_TASK_CD must be provided.
    *
    */

   @Test
   public void test_BLREQ_10708() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
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
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // C_REQ_STEP
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'1'" );
      lReqMap.put( "JOB_STEP_DESC", "'Step 1'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP, lReqMap ) );

      // C_REQ_LABOR
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

      // C_REQ_STEP_SKILL
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      // lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "REQ_TASK_CD", null );
      lReqMap.put( "JOB_STEP_ORD", "'1'" );
      lReqMap.put( "STEP_SKILL_CD", "'" + iLABOR_SKILL_CD_LBR + "'" );
      lReqMap.put( "INSP_BOOL", "'" + iINSP_BOOL_Y + "'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP_SKILL, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-10708" );

   }


   /**
    * This test is to verify error code BLREQ-10709:C_REQ_STEP_SKILL.REQ_STEP_ORD must be provided.
    *
    */

   @Test
   public void test_BLREQ_10709() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
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
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // C_REQ_STEP
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iENG_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_3 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'1'" );
      lReqMap.put( "JOB_STEP_DESC", "'Step 1'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP, lReqMap ) );

      // C_REQ_LABOR
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

      // C_REQ_STEP_SKILL
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iENG_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_3 + "'" );
      // lReqMap.put( "JOB_STEP_ORD", "'1'" );
      lReqMap.put( "JOB_STEP_ORD", null );
      lReqMap.put( "STEP_SKILL_CD", "'" + iLABOR_SKILL_CD_LBR + "'" );
      lReqMap.put( "INSP_BOOL", "'" + iINSP_BOOL_Y + "'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP_SKILL, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-10709" );

   }


   /**
    * This test is to verify error code BLREQ-10710:C_REQ_STEP_SKILL.STEP_SKILL_CD must be provided.
    *
    */

   @Test
   public void test_BLREQ_10710() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
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
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // C_REQ_STEP
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iENG_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_3 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'1'" );
      lReqMap.put( "JOB_STEP_DESC", "'Step 1'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP, lReqMap ) );

      // C_REQ_LABOR
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iENG_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_3 + "'" );
      lReqMap.put( "LABOUR_SKILL_CD", "'" + iLABOR_SKILL_CD_LBR + "'" );
      lReqMap.put( "MAN_PWR_CT", "'" + iMAN_PWR_CT + "'" );
      lReqMap.put( "SCHED_WORK_HRS", "'" + iSCHED_WORK_HRS + "'" );
      // lReqMap.put( "SCHED_INSP_HRS", "'" + iSCHED_INSP_HRS + "'" );
      lReqMap.put( "SCHED_INSP_HRS", null );
      lReqMap.put( "SCHED_CERT_HRS", "'" + iSCHED_CERT_HRS + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_LABOUR, lReqMap ) );

      // C_REQ_STEP_SKILL
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iENG_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_3 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'1'" );
      // lReqMap.put( "STEP_SKILL_CD", "'" + iLABOR_SKILL_CD_LBR + "'" );
      lReqMap.put( "INSP_BOOL", "'" + iINSP_BOOL_N + "'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP_SKILL, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-10710" );

   }


   /**
    * This test is to verify error code BLREQ-10711:C_REQ_STEP_SKILL.ASSMBL_CD / REQ_ATA_CD /
    * REQ_TASK_CD / REQ_STEP_ORD /STEP_SKILL_CD exists multiple times in staging area
    * C_REQ_STEP_SKILL.
    *
    */

   @Test
   public void test_BLREQ_10711() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
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
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // C_REQ_STEP
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'1'" );
      lReqMap.put( "JOB_STEP_DESC", "'Step 1'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP, lReqMap ) );

      // C_REQ_LABOR
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

      // C_REQ_STEP_SKILL for first record
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'1'" );
      lReqMap.put( "STEP_SKILL_CD", "'" + iLABOR_SKILL_CD_LBR + "'" );
      lReqMap.put( "INSP_BOOL", "'" + iINSP_BOOL_Y + "'" );
      // insert first time
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP_SKILL, lReqMap ) );

      // C_REQ_STEP_SKILL
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'1'" );
      lReqMap.put( "STEP_SKILL_CD", "'" + iLABOR_SKILL_CD_LBR + "'" );
      lReqMap.put( "INSP_BOOL", "'" + iINSP_BOOL_N + "'" );
      // insert second time
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP_SKILL, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-10711" );

   }


   /**
    * This test is to verify error code
    * BLREQ-10712:C_REQ_STEP_SKILL.ASSMBL_CD/REQ_ATA_CD/REQ_TASK_CD/REQ_STEP_ORD must exist in
    * staging table C_REQ_STEP.
    *
    */

   @Test
   public void test_BLREQ_10712() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
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
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // C_REQ_STEP
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'1'" );
      lReqMap.put( "JOB_STEP_DESC", "'Step 1'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP, lReqMap ) );

      // C_REQ_LABOR
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

      // C_REQ_STEP_SKILL
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'4'" );
      lReqMap.put( "STEP_SKILL_CD", "'" + iLABOR_SKILL_CD_LBR + "'" );
      lReqMap.put( "INSP_BOOL", "'" + iINSP_BOOL_Y + "'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP_SKILL, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-10712" );

   }


   /**
    * This test is to verify error code
    * BLREQ-10713:C_REQ_STEP_SKILL.ASSMBL_CD/REQ_ATA_CD/REQ_TASK_CD/REQ_STEP_ORD/STEP_SKILL_CD must
    * exist in staging table C_REQ_LABOUR.
    *
    */

   @Test
   public void test_BLREQ_10713() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
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
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // C_REQ_STEP
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'1'" );
      lReqMap.put( "JOB_STEP_DESC", "'Step 1'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP, lReqMap ) );

      // C_REQ_LABOR
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

      // C_REQ_STEP_SKILL
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'1'" );
      // lReqMap.put( "STEP_SKILL_CD", "'" + iLABOR_SKILL_CD_LBR + "'" );
      lReqMap.put( "STEP_SKILL_CD", "'LINE'" );
      lReqMap.put( "INSP_BOOL", "'" + iINSP_BOOL_Y + "'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP_SKILL, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-10713" );

   }


   /**
    * This test is to verify error code BLREQ-10714:When a skill is marked as Requires Independent
    * Inspection, the labor row for the corresponding skill must also be marked as Independent
    * Inspection Required.
    *
    */

   @Test
   public void test_BLREQ_10714() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
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
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // C_REQ_STEP
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'1'" );
      lReqMap.put( "JOB_STEP_DESC", "'Step 1'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP, lReqMap ) );

      // C_REQ_LABOR
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "LABOUR_SKILL_CD", "'" + iLABOR_SKILL_CD_LBR + "'" );
      lReqMap.put( "MAN_PWR_CT", "'" + iMAN_PWR_CT + "'" );
      lReqMap.put( "SCHED_WORK_HRS", "'" + iSCHED_WORK_HRS + "'" );
      // lReqMap.put( "SCHED_INSP_HRS", "'" + iSCHED_INSP_HRS + "'" );
      lReqMap.put( "SCHED_INSP_HRS", null );
      lReqMap.put( "SCHED_CERT_HRS", "'" + iSCHED_CERT_HRS + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_LABOUR, lReqMap ) );

      // C_REQ_STEP_SKILL
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'1'" );
      lReqMap.put( "STEP_SKILL_CD", "'" + iLABOR_SKILL_CD_LBR + "'" );
      lReqMap.put( "INSP_BOOL", "'" + iINSP_BOOL_Y + "'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP_SKILL, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-10714" );

   }


   /**
    * This test is to verify error code BLREQ-10714:When a skill is marked as Requires Independent
    * Inspection, the labor row for the corresponding skill must also be marked as Independent
    * Inspection Required.
    *
    */
   @Ignore
   @Test
   public void test_BLREQ_10714_2() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
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
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // C_REQ_STEP
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'1'" );
      lReqMap.put( "JOB_STEP_DESC", "'Step 1'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP, lReqMap ) );

      // C_REQ_LABOR
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "LABOUR_SKILL_CD", "'" + iLABOR_SKILL_CD_LBR + "'" );
      lReqMap.put( "MAN_PWR_CT", "'" + iMAN_PWR_CT + "'" );
      lReqMap.put( "SCHED_WORK_HRS", "'" + iSCHED_WORK_HRS + "'" );
      // lReqMap.put( "SCHED_INSP_HRS", "'" + iSCHED_INSP_HRS + "'" );
      lReqMap.put( "SCHED_INSP_HRS", "'0'" );
      lReqMap.put( "SCHED_CERT_HRS", "'" + iSCHED_CERT_HRS + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_LABOUR, lReqMap ) );

      // C_REQ_STEP_SKILL
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'1'" );
      lReqMap.put( "STEP_SKILL_CD", "'" + iLABOR_SKILL_CD_LBR + "'" );
      lReqMap.put( "INSP_BOOL", "'" + iINSP_BOOL_Y + "'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP_SKILL, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-10714" );

   }


   /**
    * This test is to verify error code BLREQ-10715:he labor row for each skill on the job card must
    * be marked as Certification Required. Inspection Required.
    *
    */

   @Test
   public void test_BLREQ_10715() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
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
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // C_REQ_STEP
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iENG_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_3 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'1'" );
      lReqMap.put( "JOB_STEP_DESC", "'Step 1'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP, lReqMap ) );

      // C_REQ_LABOR
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iENG_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_3 + "'" );
      lReqMap.put( "LABOUR_SKILL_CD", "'" + iLABOR_SKILL_CD_LBR + "'" );
      lReqMap.put( "MAN_PWR_CT", "'" + iMAN_PWR_CT + "'" );
      lReqMap.put( "SCHED_WORK_HRS", "'" + iSCHED_WORK_HRS + "'" );
      // lReqMap.put( "SCHED_INSP_HRS", "'" + iSCHED_INSP_HRS + "'" );
      // lReqMap.put( "SCHED_CERT_HRS", "'" + iSCHED_CERT_HRS + "'" );
      lReqMap.put( "SCHED_INSP_HRS", null );
      lReqMap.put( "SCHED_CERT_HRS", null );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_LABOUR, lReqMap ) );

      // C_REQ_STEP_SKILL
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iENG_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_3 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'1'" );
      lReqMap.put( "STEP_SKILL_CD", "'" + iLABOR_SKILL_CD_LBR + "'" );
      lReqMap.put( "INSP_BOOL", "'" + iINSP_BOOL_N + "'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP_SKILL, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-10715" );

   }


   /**
    * This test is to verify error code BLREQ-10716:Either all steps on the req must have skills
    * added, or no steps on the req have can have skills added.
    *
    */

   @Test
   public void test_BLREQ_10716_1() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
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
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // C_REQ_STEP first record
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'1'" );
      lReqMap.put( "JOB_STEP_DESC", "'Step 1'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP, lReqMap ) );

      // // C_REQ_STEP second record
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'2'" );
      lReqMap.put( "JOB_STEP_DESC", "'Step 2'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP, lReqMap ) );

      // C_REQ_LABOR
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

      // C_REQ_STEP_SKILL
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'1'" );
      lReqMap.put( "STEP_SKILL_CD", "'" + iLABOR_SKILL_CD_LBR + "'" );
      lReqMap.put( "INSP_BOOL", "'" + iINSP_BOOL_Y + "'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP_SKILL, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-10716" );

   }


   /**
    * This test is to verify error code BLREQ-10716:Either all steps on the req must have skills
    * added, or no steps on the req have can have skills added.
    *
    */

   @Test
   public void test_BLREQ_10716_2() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
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
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // C_REQ_STEP for first record
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iENG_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_3 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'1'" );
      lReqMap.put( "JOB_STEP_DESC", "'Step 1'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP, lReqMap ) );

      // C_REQ_STEP for second record
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iENG_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_3 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'2'" );
      lReqMap.put( "JOB_STEP_DESC", "'Step 2'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP, lReqMap ) );

      // C_REQ_LABOR
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

      // C_REQ_STEP_SKILL
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iENG_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_3 + "'" );
      lReqMap.put( "JOB_STEP_ORD", "'1'" );
      lReqMap.put( "STEP_SKILL_CD", "'" + iLABOR_SKILL_CD_LBR + "'" );
      lReqMap.put( "INSP_BOOL", "'" + iINSP_BOOL_Y + "'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP_SKILL, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-10716" );

   }


   /**
    * This test is to verify error code BLREQ-17600:C_REQ_PART row is invalid because related row is
    * invalid.
    *
    */

   @Test
   public void test_BLREQ_17600() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'SYS-2'" );
      lReqMap.put( "REQ_TASK_CD", "'INVALID'" );
      lReqMap.put( "REQ_TASK_NAME", "'INAVLID'" );
      lReqMap.put( "REQ_TASK_DESC", "'INVALID'" );
      lReqMap.put( "REQ_TASK_REF_SDESC", "'INVALID'" );
      lReqMap.put( "CLASS_CD", "'INVALID'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "APPLICABILITY_DESC", "'" + iAPPLICABILITY_DESC + "'" );
      lReqMap.put( "LAST_DEADLINE_DRIVER_BOOL", "'" + iLAST_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_DRIVER_BOOL", "'" + iSOFT_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL + "'" );
      lReqMap.put( "ENFORCE_WORKSCOPE_ORD_BOOL", "'" + iENFORCE_WORKSCOPE_ORD_BOOL + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // C_REQ_STEP
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'SYS-2'" );
      lReqMap.put( "REQ_TASK_CD", "'INVALID'" );
      lReqMap.put( "JOB_STEP_ORD", "'1'" );
      lReqMap.put( "JOB_STEP_DESC", "'Step 1'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP, lReqMap ) );

      // C_REQ_LABOR
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'SYS-2'" );
      lReqMap.put( "REQ_TASK_CD", "'INVALID'" );
      lReqMap.put( "LABOUR_SKILL_CD", "'" + iLABOR_SKILL_CD_LBR + "'" );
      lReqMap.put( "MAN_PWR_CT", "'" + iMAN_PWR_CT + "'" );
      lReqMap.put( "SCHED_WORK_HRS", "'" + iSCHED_WORK_HRS + "'" );
      lReqMap.put( "SCHED_INSP_HRS", "'" + iSCHED_INSP_HRS + "'" );
      lReqMap.put( "SCHED_CERT_HRS", "'" + iSCHED_CERT_HRS + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_LABOUR, lReqMap ) );

      // C_REQ_STEP_SKILL
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'SYS-2'" );
      lReqMap.put( "REQ_TASK_CD", "'INVALID'" );
      lReqMap.put( "JOB_STEP_ORD", "'1'" );
      lReqMap.put( "STEP_SKILL_CD", "'" + iLABOR_SKILL_CD_LBR + "'" );
      lReqMap.put( "INSP_BOOL", "'" + iINSP_BOOL_Y + "'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STEP_SKILL, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode_CReqStepSkill( "BLREQ-17600" );

   }
}
