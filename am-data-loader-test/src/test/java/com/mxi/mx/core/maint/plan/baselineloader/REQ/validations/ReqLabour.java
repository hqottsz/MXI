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
 * This test suite contains test cases on validation REQ_IMPORT package on c_req_labour.
 *
 * @author ALICIA QIAN
 */

public class ReqLabour extends ReqTests {

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
    * c_req_labour.
    *
    *
    */

   @Test
   public void testACFT_TRK_Sucessful() {

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

      // C_REQ_LABOR
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

      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // verify no error
      VerifyValidation();

   }


   /**
    * This test is to verify req_import.validate_req functionality of staging tables: c_req and
    * c_req_labour. REQ is on SYS of ACFT
    *
    *
    */

   @Test
   public void testACFT_SYS_Sucessful() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_2 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_2 + "'" );
      lReqMap.put( "REQ_TASK_DESC", "'" + iREQ_TASK_DESC_2 + "'" );
      lReqMap.put( "REQ_TASK_REF_SDESC", "'" + iREQ_TASK_REF_SDESC_2 + "'" );
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

      // C_REQ_LABOR
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_2 + "'" );
      lReqMap.put( "LABOUR_SKILL_CD", "'" + iLABOR_SKILL_CD + "'" );
      lReqMap.put( "MAN_PWR_CT", "'" + iMAN_PWR_CT + "'" );
      lReqMap.put( "SCHED_WORK_HRS", "'" + iSCHED_WORK_HRS + "'" );
      // lReqMap.put( "SCHED_INSP_HRS", "'" + iSCHED_INSP_HRS + "'" );
      lReqMap.put( "SCHED_CERT_HRS", "'" + iSCHED_CERT_HRS + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_LABOUR, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // verify no error
      VerifyValidation();

   }


   /**
    * This test is to verify req_import.validate_req functionality of staging tables: c_req and
    * c_req_labour. REQ is on SYS of ENG
    *
    *
    */

   @Test
   public void testENG_SYS_Sucessful() {

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

      // C_REQ_LABOR
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

      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // verify no error
      VerifyValidation();

   }


   /**
    * This test is to verify req_import.validate_req functionality of staging tables: c_req and
    * c_req_labour. REQ is on SYS of APU
    *
    *
    */

   @Test
   public void testAPU_SYS_Sucessful() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iAPU_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iAPU_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_4 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_4 + "'" );
      lReqMap.put( "REQ_TASK_DESC", "'" + iREQ_TASK_DESC_4 + "'" );
      lReqMap.put( "REQ_TASK_REF_SDESC", "'" + iREQ_TASK_REF_SDESC_4 + "'" );
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

      // C_REQ_LABOR
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iAPU_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iAPU_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_4 + "'" );
      lReqMap.put( "LABOUR_SKILL_CD", "'" + iLABOR_SKILL_CD + "'" );
      lReqMap.put( "MAN_PWR_CT", "'" + iMAN_PWR_CT + "'" );
      lReqMap.put( "SCHED_WORK_HRS", "'" + iSCHED_WORK_HRS + "'" );
      lReqMap.put( "SCHED_INSP_HRS", "'" + iSCHED_INSP_HRS + "'" );
      lReqMap.put( "SCHED_CERT_HRS", "'" + iSCHED_CERT_HRS + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_LABOUR, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // verify no error
      VerifyValidation();

   }


   /**
    * This test is to verify req_import.validate_req functionality of staging tables: c_req and
    * c_req_labour. REQ is on ENG of ACFT
    *
    *
    */

   @Test
   public void testACFT_ENG_Sucessful() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_ENG + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_5 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_5 + "'" );
      lReqMap.put( "REQ_TASK_DESC", "'" + iREQ_TASK_DESC_5 + "'" );
      lReqMap.put( "REQ_TASK_REF_SDESC", "'" + iREQ_TASK_REF_SDESC_5 + "'" );
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

      // C_REQ_LABOR
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_ENG + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_5 + "'" );
      lReqMap.put( "LABOUR_SKILL_CD", "'" + iLABOR_SKILL_CD + "'" );
      lReqMap.put( "MAN_PWR_CT", "'" + iMAN_PWR_CT + "'" );
      lReqMap.put( "SCHED_WORK_HRS", "'" + iSCHED_WORK_HRS + "'" );
      lReqMap.put( "SCHED_INSP_HRS", "'" + iSCHED_INSP_HRS + "'" );
      lReqMap.put( "SCHED_CERT_HRS", "'" + iSCHED_CERT_HRS + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_LABOUR, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // verify no error
      VerifyValidation();

   }


   /**
    * This test is to verify req_import.validate_req functionality of staging tables: c_req and
    * c_req_labour. REQ is on APU of ACFT
    *
    *
    */

   @Test
   public void testACFT_APU_Sucessful() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_APU + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_6 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_6 + "'" );
      lReqMap.put( "REQ_TASK_DESC", "'" + iREQ_TASK_DESC_6 + "'" );
      lReqMap.put( "REQ_TASK_REF_SDESC", "'" + iREQ_TASK_REF_SDESC_6 + "'" );
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

      // C_REQ_LABOR
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_APU + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_6 + "'" );
      lReqMap.put( "LABOUR_SKILL_CD", "'" + iLABOR_SKILL_CD + "'" );
      lReqMap.put( "MAN_PWR_CT", "'" + iMAN_PWR_CT + "'" );
      lReqMap.put( "SCHED_WORK_HRS", "'" + iSCHED_WORK_HRS + "'" );
      // lReqMap.put( "SCHED_INSP_HRS", "'" + iSCHED_INSP_HRS + "'" );
      // lReqMap.put( "SCHED_CERT_HRS", "'" + iSCHED_CERT_HRS + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_LABOUR, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // verify no error
      VerifyValidation();

   }


   /**
    * This test is to verify req_import.validate_req functionality of staging tables: c_req and
    * c_req_labour. REQ is on APU of ACFT
    *
    *
    */
   @Ignore
   @Test
   public void testACFT_APU_ZERO_INSP_Sucessful() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_APU + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_6 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_6 + "'" );
      lReqMap.put( "REQ_TASK_DESC", "'" + iREQ_TASK_DESC_6 + "'" );
      lReqMap.put( "REQ_TASK_REF_SDESC", "'" + iREQ_TASK_REF_SDESC_6 + "'" );
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

      // C_REQ_LABOR
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_APU + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_6 + "'" );
      lReqMap.put( "LABOUR_SKILL_CD", "'" + iLABOR_SKILL_CD + "'" );
      lReqMap.put( "MAN_PWR_CT", "'" + iMAN_PWR_CT + "'" );
      lReqMap.put( "SCHED_WORK_HRS", "'" + iSCHED_WORK_HRS + "'" );
      lReqMap.put( "SCHED_INSP_HRS", "'0'" );
      // lReqMap.put( "SCHED_CERT_HRS", "'" + iSCHED_CERT_HRS + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_LABOUR, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // verify no error
      VerifyValidation();

   }


   /**
    * This test is to verify req_import.validate_req functionality of staging tables: c_req and
    * c_req_labour. 2 labour's is set to REQ on TRK of ACFT
    *
    *
    */

   @Test
   public void testACFT_TRK_2LABOURS_Sucessful() {

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

      // C_REQ_LABOR
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

      // C_REQ_LABOR
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "LABOUR_SKILL_CD", "'" + iLABOR_SKILL_CD_QA + "'" );
      lReqMap.put( "MAN_PWR_CT", "'" + iMAN_PWR_CT + "'" );
      lReqMap.put( "SCHED_WORK_HRS", "'" + iSCHED_WORK_HRS + "'" );
      lReqMap.put( "SCHED_INSP_HRS", "'" + iSCHED_INSP_HRS + "'" );
      lReqMap.put( "SCHED_CERT_HRS", "'" + iSCHED_CERT_HRS + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_LABOUR, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // verify no error
      VerifyValidation();

   }


   /**
    * This test is to verify error code blREQ-10100: C_REQ_LABOR.ASSMBL_CD must be provided.
    *
    *
    */

   @Test
   public void test_BLREQ_10100() {

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

      // C_REQ_LABOR
      lReqMap.clear();
      // lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "LABOUR_SKILL_CD", "'" + iLABOR_SKILL_CD + "'" );
      lReqMap.put( "MAN_PWR_CT", "'" + iMAN_PWR_CT + "'" );
      lReqMap.put( "SCHED_WORK_HRS", "'" + iSCHED_WORK_HRS + "'" );
      lReqMap.put( "SCHED_INSP_HRS", "'" + iSCHED_INSP_HRS + "'" );
      lReqMap.put( "SCHED_CERT_HRS", "'" + iSCHED_CERT_HRS + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_LABOUR, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-10100" );

   }


   /**
    * This test is to verify error code BLREQ-10101: C_REQ_LABOR.REQ_ATA_CD must be provided.
    *
    *
    */

   @Test
   public void test_BLREQ_10101() {

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

      // C_REQ_LABOR
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      // lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "LABOUR_SKILL_CD", "'" + iLABOR_SKILL_CD + "'" );
      lReqMap.put( "MAN_PWR_CT", "'" + iMAN_PWR_CT + "'" );
      lReqMap.put( "SCHED_WORK_HRS", "'" + iSCHED_WORK_HRS + "'" );
      lReqMap.put( "SCHED_INSP_HRS", "'" + iSCHED_INSP_HRS + "'" );
      lReqMap.put( "SCHED_CERT_HRS", "'" + iSCHED_CERT_HRS + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_LABOUR, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-10101" );

   }


   /**
    * This test is to verify error code BLREQ-10102: C_REQ_LABOR.REQ_TASK_CD must be provided.
    *
    *
    */

   @Test
   public void test_BLREQ_10102() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_2 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_2 + "'" );
      lReqMap.put( "REQ_TASK_DESC", "'" + iREQ_TASK_DESC_2 + "'" );
      lReqMap.put( "REQ_TASK_REF_SDESC", "'" + iREQ_TASK_REF_SDESC_2 + "'" );
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

      // C_REQ_LABOR
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_SYS + "'" );
      // lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_2 + "'" );
      lReqMap.put( "LABOUR_SKILL_CD", "'" + iLABOR_SKILL_CD + "'" );
      lReqMap.put( "MAN_PWR_CT", "'" + iMAN_PWR_CT + "'" );
      lReqMap.put( "SCHED_WORK_HRS", "'" + iSCHED_WORK_HRS + "'" );
      lReqMap.put( "SCHED_INSP_HRS", "'" + iSCHED_INSP_HRS + "'" );
      lReqMap.put( "SCHED_CERT_HRS", "'" + iSCHED_CERT_HRS + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_LABOUR, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-10102" );

   }


   /**
    * This test is to verify error code BLREQ-10103: C_REQ_LABOR.LABOUR_SKILL_CD must be provided.
    *
    *
    */

   @Test
   public void test_BLREQ_10103() {

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

      // C_REQ_LABOR
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iENG_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_3 + "'" );
      // lReqMap.put( "LABOUR_SKILL_CD", "'" + iLABOR_SKILL_CD + "'" );
      lReqMap.put( "MAN_PWR_CT", "'" + iMAN_PWR_CT + "'" );
      lReqMap.put( "SCHED_WORK_HRS", "'" + iSCHED_WORK_HRS + "'" );
      lReqMap.put( "SCHED_INSP_HRS", "'" + iSCHED_INSP_HRS + "'" );
      lReqMap.put( "SCHED_CERT_HRS", "'" + iSCHED_CERT_HRS + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_LABOUR, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-10103" );

   }


   /**
    * This test is to verify error code BLREQ-10104:C_REQ_LABOR.SCHED_WORK_HRS is not provided.
    *
    *
    */

   @Test
   public void test_BLREQ_10104() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iAPU_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iAPU_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_4 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_4 + "'" );
      lReqMap.put( "REQ_TASK_DESC", "'" + iREQ_TASK_DESC_4 + "'" );
      lReqMap.put( "REQ_TASK_REF_SDESC", "'" + iREQ_TASK_REF_SDESC_4 + "'" );
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

      // C_REQ_LABOR
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iAPU_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iAPU_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_4 + "'" );
      lReqMap.put( "LABOUR_SKILL_CD", "'" + iLABOR_SKILL_CD + "'" );
      lReqMap.put( "MAN_PWR_CT", "'" + iMAN_PWR_CT + "'" );
      // lReqMap.put( "SCHED_WORK_HRS", "'" + iSCHED_WORK_HRS + "'" );
      lReqMap.put( "SCHED_INSP_HRS", "'" + iSCHED_INSP_HRS + "'" );
      lReqMap.put( "SCHED_CERT_HRS", "'" + iSCHED_CERT_HRS + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_LABOUR, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-10104" );

   }


   /**
    * This test is to verify error code
    * BLREQ-11100:C_REQ_LABOR.ASSMBL_CD/REQ_ATA_CD/REQ_TASK_CD/LABOUR_SKILL_CD exists multiple times
    * in staging area.
    *
    *
    */

   @Test
   public void test_BLREQ_11100() {

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

      // C_REQ_LABOR
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

      // C_REQ_LABOR
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

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-11100" );

   }


   /**
    * This test is to verify error code
    * BLREQ-12103:C_REQ_LABOR.ASSMBL_CD/REQ_ATA_CD/REQ_TASK_CD/LABOUR_SKILL_CD exists multiple times
    * in staging area.
    *
    *
    */

   @Test
   public void test_BLREQ_12103() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_2 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_2 + "'" );
      lReqMap.put( "REQ_TASK_DESC", "'" + iREQ_TASK_DESC_2 + "'" );
      lReqMap.put( "REQ_TASK_REF_SDESC", "'" + iREQ_TASK_REF_SDESC_2 + "'" );
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

      // C_REQ_LABOR
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'INVALID'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_2 + "'" );
      lReqMap.put( "LABOUR_SKILL_CD", "'" + iLABOR_SKILL_CD + "'" );
      lReqMap.put( "MAN_PWR_CT", "'" + iMAN_PWR_CT + "'" );
      lReqMap.put( "SCHED_WORK_HRS", "'" + iSCHED_WORK_HRS + "'" );
      lReqMap.put( "SCHED_INSP_HRS", "'" + iSCHED_INSP_HRS + "'" );
      lReqMap.put( "SCHED_CERT_HRS", "'" + iSCHED_CERT_HRS + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_LABOUR, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-12103" );

   }


   /**
    * This test is to verify error code BLREQ-12104:C_REQ_LABOR.Labour Skill Code is invalid.
    *
    *
    */

   @Test
   public void test_BLREQ_12104() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_ENG + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_5 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_5 + "'" );
      lReqMap.put( "REQ_TASK_DESC", "'" + iREQ_TASK_DESC_5 + "'" );
      lReqMap.put( "REQ_TASK_REF_SDESC", "'" + iREQ_TASK_REF_SDESC_5 + "'" );
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

      // C_REQ_LABOR
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_ENG + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_5 + "'" );
      lReqMap.put( "LABOUR_SKILL_CD", "'INVALID'" );
      lReqMap.put( "MAN_PWR_CT", "'" + iMAN_PWR_CT + "'" );
      lReqMap.put( "SCHED_WORK_HRS", "'" + iSCHED_WORK_HRS + "'" );
      lReqMap.put( "SCHED_INSP_HRS", "'" + iSCHED_INSP_HRS + "'" );
      lReqMap.put( "SCHED_CERT_HRS", "'" + iSCHED_CERT_HRS + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_LABOUR, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-12104" );

   }


   /**
    * This test is to verify error code BLREQ-12107:C_REQ_LABOR.Labour Skill Code is invalid.
    *
    *
    */
   @Ignore
   @Test
   public void test_BLREQ_12107() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iAPU_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iAPU_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_4 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_4 + "'" );
      lReqMap.put( "REQ_TASK_DESC", "'" + iREQ_TASK_DESC_4 + "'" );
      lReqMap.put( "REQ_TASK_REF_SDESC", "'" + iREQ_TASK_REF_SDESC_4 + "'" );
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

      // C_REQ_LABOR
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iAPU_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iAPU_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_4 + "'" );
      lReqMap.put( "LABOUR_SKILL_CD", "'" + iLABOR_SKILL_CD + "'" );
      lReqMap.put( "MAN_PWR_CT", "'10.1'" );
      lReqMap.put( "SCHED_WORK_HRS", "'" + iSCHED_WORK_HRS + "'" );
      lReqMap.put( "SCHED_INSP_HRS", "'" + iSCHED_INSP_HRS + "'" );
      lReqMap.put( "SCHED_CERT_HRS", "'" + iSCHED_CERT_HRS + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_LABOUR, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-12107" );

   }


   /**
    * This test is to verify error code BLREQ-12108:C_REQ_LABOR.Man Power Count is not valid.
    *
    *
    */

   @Test
   public void test_BLREQ_12108() {

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

      // C_REQ_LABOR
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "LABOUR_SKILL_CD", "'" + iLABOR_SKILL_CD + "'" );
      lReqMap.put( "MAN_PWR_CT", "'-10'" );
      lReqMap.put( "SCHED_WORK_HRS", "'" + iSCHED_WORK_HRS + "'" );
      lReqMap.put( "SCHED_INSP_HRS", "'" + iSCHED_INSP_HRS + "'" );
      lReqMap.put( "SCHED_CERT_HRS", "'" + iSCHED_CERT_HRS + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_LABOUR, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-12108" );

   }


   /**
    * This test is to verify error code BLREQ-12109:C_REQ_LABOR. Scheduled Work Hours is not valid.
    *
    *
    */

   @Test
   public void test_BLREQ_12109() {

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

      // C_REQ_LABOR
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "LABOUR_SKILL_CD", "'" + iLABOR_SKILL_CD + "'" );
      lReqMap.put( "MAN_PWR_CT", "'" + iMAN_PWR_CT + "'" );
      // lReqMap.put( "SCHED_WORK_HRS", "'" + iSCHED_WORK_HRS + "'" );
      lReqMap.put( "SCHED_WORK_HRS", "'-10'" );
      lReqMap.put( "SCHED_INSP_HRS", "'" + iSCHED_INSP_HRS + "'" );
      lReqMap.put( "SCHED_CERT_HRS", "'" + iSCHED_CERT_HRS + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_LABOUR, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-12109" );

   }


   /**
    * This test is to verify error code BLREQ-12110: Scheduled Certification Hours is not valid.
    *
    *
    */

   @Test
   public void test_BLREQ_12110() {

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

      // C_REQ_LABOR
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "LABOUR_SKILL_CD", "'" + iLABOR_SKILL_CD + "'" );
      lReqMap.put( "MAN_PWR_CT", "'" + iMAN_PWR_CT + "'" );
      lReqMap.put( "SCHED_WORK_HRS", "'" + iSCHED_WORK_HRS + "'" );
      lReqMap.put( "SCHED_INSP_HRS", "'" + iSCHED_INSP_HRS + "'" );
      // lReqMap.put( "SCHED_CERT_HRS", "'" + iSCHED_CERT_HRS + "'" );
      lReqMap.put( "SCHED_CERT_HRS", "'-10'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_LABOUR, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-12110" );

   }


   /**
    * This test is to verify error code BLREQ-12111: Scheduled Independent Inspection Hours is not
    * valid.
    *
    *
    */

   @Test
   public void test_BLREQ_12111() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_2 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_2 + "'" );
      lReqMap.put( "REQ_TASK_DESC", "'" + iREQ_TASK_DESC_2 + "'" );
      lReqMap.put( "REQ_TASK_REF_SDESC", "'" + iREQ_TASK_REF_SDESC_2 + "'" );
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

      // C_REQ_LABOR
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_2 + "'" );
      lReqMap.put( "LABOUR_SKILL_CD", "'" + iLABOR_SKILL_CD + "'" );
      lReqMap.put( "MAN_PWR_CT", "'" + iMAN_PWR_CT + "'" );
      lReqMap.put( "SCHED_WORK_HRS", "'" + iSCHED_WORK_HRS + "'" );
      // lReqMap.put( "SCHED_INSP_HRS", "'" + iSCHED_INSP_HRS + "'" );
      lReqMap.put( "SCHED_INSP_HRS", "'-10'" );
      lReqMap.put( "SCHED_CERT_HRS", "'" + iSCHED_CERT_HRS + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_LABOUR, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-12111" );

   }


   /**
    * This test is to verify error code BLREQ-12112: C_REQ_LABOR.SCHED_INSP_HRS is provided, but
    * C_REQ_LABOR.SCHED_CERT_HRS is not provided..
    *
    *
    */

   @Test
   public void test_BLREQ_12112_1() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_2 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_2 + "'" );
      lReqMap.put( "REQ_TASK_DESC", "'" + iREQ_TASK_DESC_2 + "'" );
      lReqMap.put( "REQ_TASK_REF_SDESC", "'" + iREQ_TASK_REF_SDESC_2 + "'" );
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

      // C_REQ_LABOR
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_2 + "'" );
      lReqMap.put( "LABOUR_SKILL_CD", "'" + iLABOR_SKILL_CD + "'" );
      lReqMap.put( "MAN_PWR_CT", "'" + iMAN_PWR_CT + "'" );
      lReqMap.put( "SCHED_WORK_HRS", "'" + iSCHED_WORK_HRS + "'" );
      lReqMap.put( "SCHED_INSP_HRS", "'" + iSCHED_INSP_HRS + "'" );
      // lReqMap.put( "SCHED_CERT_HRS", "'" + iSCHED_CERT_HRS + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_LABOUR, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-12112" );

   }


   /**
    * This test is to verify error code BLREQ-12112: C_REQ_LABOR.SCHED_INSP_HRS is provided, but
    * C_REQ_LABOR.SCHED_CERT_HRS is not provided..
    *
    *
    */

   @Test
   public void test_BLREQ_12112_2() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_2 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_2 + "'" );
      lReqMap.put( "REQ_TASK_DESC", "'" + iREQ_TASK_DESC_2 + "'" );
      lReqMap.put( "REQ_TASK_REF_SDESC", "'" + iREQ_TASK_REF_SDESC_2 + "'" );
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

      // C_REQ_LABOR
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_2 + "'" );
      lReqMap.put( "LABOUR_SKILL_CD", "'" + iLABOR_SKILL_CD + "'" );
      lReqMap.put( "MAN_PWR_CT", "'" + iMAN_PWR_CT + "'" );
      lReqMap.put( "SCHED_WORK_HRS", "'" + iSCHED_WORK_HRS + "'" );
      // lReqMap.put( "SCHED_INSP_HRS", "'" + iSCHED_INSP_HRS + "'" );
      lReqMap.put( "SCHED_INSP_HRS", "'0'" );
      // lReqMap.put( "SCHED_CERT_HRS", "'" + iSCHED_CERT_HRS + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_LABOUR, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-12112" );

   }


   /**
    * This test is to verify error code BLREQ-12112: C_REQ_LABOR.SCHED_INSP_HRS is provided, but
    * C_REQ_LABOR.SCHED_CERT_HRS is not provided..
    *
    *
    */

   @Test
   public void test_BLREQ_17100() {

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

      // C_REQ_LABOR
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'SYS-2'" );
      lReqMap.put( "REQ_TASK_CD", "'INVALID'" );
      lReqMap.put( "LABOUR_SKILL_CD", "'" + iLABOR_SKILL_CD + "'" );
      lReqMap.put( "MAN_PWR_CT", "'" + iMAN_PWR_CT + "'" );
      lReqMap.put( "SCHED_WORK_HRS", "'" + iSCHED_WORK_HRS + "'" );
      lReqMap.put( "SCHED_INSP_HRS", "'" + iSCHED_INSP_HRS + "'" );
      lReqMap.put( "SCHED_CERT_HRS", "'" + iSCHED_CERT_HRS + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_LABOUR, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode_CReqLabour( "BLREQ-17100" );

   }

}
