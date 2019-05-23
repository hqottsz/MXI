package com.mxi.mx.core.maint.plan.baselineloader.REQ.validations;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.mxi.mx.core.maint.plan.baselineloader.REQ.ReqTests;
import com.mxi.mx.util.TableUtil;


/**
 * This test is to verify req_import.validate_req functionality of staging tables: c_req and
 * c_req_tool.
 *
 *
 */
public class ReqTool extends ReqTests {

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
    * c_req_tool. REQ is on TRK of ACFT, REQ_HR is integer in (0,9999.9)
    *
    *
    */

   @Test
   public void testACFT_TRK_HrInteger_Sucessful() {

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

      // C_REQ_TOOL
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
    * This test is to verify req_import.validate_req functionality of staging tables: c_req and
    * c_req_tool. REQ is on BATCH of ENG, REQ_HR is integer in (0,9999.9)
    *
    *
    */

   @Test
   public void testENG_BATCH_HrInteger_Sucessful() {

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

      // C_REQ_TOOL
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
    * This test is to verify req_import.validate_req functionality of staging tables: c_req and
    * c_req_tool. REQ is on SER of ACFT, REQ_HR is 1 decimal in (0,9999.9)
    *
    *
    */

   @Test
   public void testACFT_SER_HrDecimal_1_Sucessful() {

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

      // C_REQ_TOOL
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_APU + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_6 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_11 + "'" );
      lReqMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_11 + "'" );
      lReqMap.put( "REQ_HR", "'" + iREQ_HR_Decimal_1 + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_TOOL, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // verify no error
      VerifyValidation();

   }


   /**
    * This test is to verify req_import.validate_req functionality of staging tables: c_req and
    * c_req_tool. REQ is on SER of ACFT, REQ_HR is 1 zero
    *
    *
    */

   @Test
   public void testACFT_SER_HrZero_Sucessful() {

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

      // C_REQ_TOOL
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_APU + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_6 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_11 + "'" );
      lReqMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_11 + "'" );
      lReqMap.put( "REQ_HR", "'" + iREQ_HR_ZERO + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_TOOL, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // verify no error
      VerifyValidation();

   }


   /**
    * This test is to verify req_import.validate_req functionality of staging tables: c_req and
    * c_req_tool. REQ is on SER of ACFT, REQ_HR is 1 decimal in (0,9999.9)
    *
    *
    */

   @Test
   public void testACFT_SER_HrDecimal_2_Sucessful() {

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

      // C_REQ_TOOL
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_APU + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_6 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_11 + "'" );
      lReqMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_11 + "'" );
      lReqMap.put( "REQ_HR", "'" + iREQ_HR_Decimal_2 + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_TOOL, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // verify no error
      VerifyValidation();

   }


   /**
    * This test is to verify req_import.validate_req functionality of staging tables: c_req and
    * c_req_tool. REQ is on SER of ACFT, REQ_HR is 1 decimal in (0,9999.9)
    *
    *
    */

   @Test
   public void testACFT_SER_HrDecimal_3_Sucessful() {

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

      // C_REQ_TOOL
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_APU + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_6 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_11 + "'" );
      lReqMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_11 + "'" );
      lReqMap.put( "REQ_HR", "'" + iREQ_HR_Decimal_3 + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_TOOL, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // verify no error
      VerifyValidation();

   }


   /**
    * This test is to verify req_import.validate_req functionality of staging tables: c_req and
    * c_req_tool. REQ is on SER of ACFT, REQ_HR is 1 decimal in (0,9999.9)
    *
    *
    */

   @Test
   public void testACFT_SER_HrDecimal_4_Sucessful() {

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

      // C_REQ_TOOL
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_APU + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_6 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_11 + "'" );
      lReqMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_11 + "'" );
      lReqMap.put( "REQ_HR", "'" + iREQ_HR_Decimal_4 + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_TOOL, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // verify no error
      VerifyValidation();

   }


   /**
    * This test is to verify req_import.validate_req functionality of staging tables: c_req and
    * c_req_tool. REQ is on SER of ACFT, REQ_HR is 1 decimal in (0,9999.9)
    *
    *
    */

   @Test
   public void testACFT_SER_HrDecimal_5_Sucessful() {

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

      // C_REQ_TOOL
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_APU + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_6 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_11 + "'" );
      lReqMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_11 + "'" );
      lReqMap.put( "REQ_HR", "'" + iREQ_HR_Decimal_5 + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_TOOL, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // verify no error
      VerifyValidation();

   }


   /**
    * This test is to verify error code BLREQ-10200: C_REQ_TOOL.ASSMBL_CD must be provided.
    *
    *
    */

   @Test
   public void test_BLREQ_10300() {

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

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // C_REQ_TOOL
      lReqMap.clear();
      // lReqMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iENG_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_3 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_9 + "'" );
      lReqMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_9 + "'" );
      lReqMap.put( "REQ_HR", "'" + iREQ_HR_INT + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_TOOL, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-10300" );

   }


   /**
    * This test is to verify error code BLREQ-10201: C_REQ_TOOL.REQ_ATA_CD must be provided.
    *
    *
    */

   @Test
   public void test_BLREQ_10301() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
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

      // C_REQ_TOOL
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      // lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_10 + "'" );
      lReqMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_10 + "'" );
      lReqMap.put( "REQ_HR", "'" + iREQ_HR_INT + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_TOOL, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-10301" );

   }


   /**
    * This test is to verify error code BLREQ-10302: C_REQ_TOOL. REQ_TASK_CD must be provided.
    *
    *
    */

   @Test
   public void test_BLREQ_10302() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
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

      // C_REQ_TOOL
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_APU + "'" );
      // lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_6 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_11 + "'" );
      lReqMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_11 + "'" );
      lReqMap.put( "REQ_HR", "'" + iREQ_HR_INT + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_TOOL, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-10302" );

   }


   /**
    * This test is to verify error code BLREQ-10303: C_REQ_TOOL.PART_NO_OEM must be provided.
    *
    *
    */

   @Test
   public void test_BLREQ_10303() {

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

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // C_REQ_TOOL
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      // lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_9 + "'" );
      lReqMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_9 + "'" );
      lReqMap.put( "REQ_HR", "'" + iREQ_HR_INT + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_TOOL, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-10303" );

   }


   /**
    * This test is to verify error code BLREQ-10304: C_REQ_TOOL.MANUFACT_REF must be provided.
    *
    *
    */

   @Test
   public void test_BLREQ_10304() {

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

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // C_REQ_TOOL
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iENG_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_3 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_10 + "'" );
      // lReqMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_10 + "'" );
      lReqMap.put( "REQ_HR", "'" + iREQ_HR_INT + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_TOOL, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-10304" );

   }


   /**
    * This test is to verify error code BLREQ-12309: C_REQ_TOOL.REQ_HR must be a number between 0
    * and 9999.99.
    *
    *
    */

   @Test
   public void test_BLREQ_12309_1() {

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

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // C_REQ_TOOL
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_APU + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_6 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_11 + "'" );
      lReqMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_11 + "'" );
      lReqMap.put( "REQ_HR", "'-0.9'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_TOOL, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-12309" );

   }


   /**
    * This test is to verify error code BLREQ-12309: C_REQ_TOOL.REQ_HR must be a number between 0
    * and 9999.99.
    *
    *
    */

   @Test
   public void test_BLREQ_12309_2() {

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

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // C_REQ_TOOL
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_APU + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_6 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_11 + "'" );
      lReqMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_11 + "'" );
      lReqMap.put( "REQ_HR", "'-100'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_TOOL, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-12309" );

   }


   /**
    * This test is to verify error code BLREQ-12309: C_REQ_TOOL.REQ_HR must be a number between 0
    * and 9999.99.
    *
    *
    */

   @Test
   public void test_BLREQ_12309_3() {

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

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // C_REQ_TOOL
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_APU + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_6 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_11 + "'" );
      lReqMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_11 + "'" );
      lReqMap.put( "REQ_HR", "'10000'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_TOOL, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-12309" );

   }


   /**
    * This test is to verify error code BLREQ-12310: C_REQ_TOOL.REQ_HR cannot have more than 2
    * digits after decimal point.
    *
    *
    */

   @Test
   public void test_BLREQ_12310() {

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

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // C_REQ_TOOL
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_APU + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_6 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_11 + "'" );
      lReqMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_11 + "'" );
      lReqMap.put( "REQ_HR", "'5.013'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_TOOL, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-12310" );

   }


   /**
    * This test is to verify error code BLREQ-12303: C_REQ_TOOL.Assembly/ATA Code/REQ Task Code is
    * invalid.
    *
    *
    */

   @Test
   public void test_BLREQ_12303() {

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

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // C_REQ_TOOL
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iAPU_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'APU-SYS-1-1-TRK-P1'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_4 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_9 + "'" );
      lReqMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_9 + "'" );
      lReqMap.put( "REQ_HR", "'" + iREQ_HR_INT + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_TOOL, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-12303" );

   }


   /**
    * This test is to verify error code BLREQ-12304: C_REQ_TOOL.Part Number is invalid.
    *
    *
    */

   @Test
   public void test_BLREQ_12304() {

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

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // C_REQ_TOOL
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iENG_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_3 + "'" );
      lReqMap.put( "PART_NO_OEM", "'INVALID'" );
      lReqMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_10 + "'" );
      lReqMap.put( "REQ_HR", "'" + iREQ_HR_INT + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_TOOL, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-12304" );

   }


   /**
    * This test is to verify error code BLREQ-12305: C_REQ_TOOL.Manufacturer is invalid.
    *
    */

   @Test
   public void test_BLREQ_12305() {

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

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // C_REQ_TOOL
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iENG_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_3 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_10 + "'" );
      lReqMap.put( "MANUFACT_REF", "'INVALID'" );
      lReqMap.put( "REQ_HR", "'" + iREQ_HR_INT + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_TOOL, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-12305" );

   }


   /**
    * This test is to verify error code BLREQ-12306: C_REQ_TOOL.Tool Assembly/Part Number
    * /Manufacturer is invalid.
    *
    */

   @Test
   public void test_BLREQ_12306() {

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

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // C_REQ_TOOL
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_APU + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_6 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_11 + "'" );
      lReqMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_1 + "'" );
      lReqMap.put( "REQ_HR", "'" + iREQ_HR_ZERO + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_TOOL, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-12306" );

   }


   /**
    * This test is to verify error code BLREQ-12307: C_REQ_TOOL.Part Number/Manufacturer is valid
    * but not a valid part on Tool assembly.
    *
    */

   @Test
   public void test_BLREQ_12307() {

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

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // C_REQ_TOOL
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_1 + "'" );
      lReqMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_1 + "'" );
      lReqMap.put( "REQ_HR", "'" + iREQ_HR_INT + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_TOOL, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-12307" );

   }


   /**
    * This test is to verify error code BLREQ-12308: More than one Maintenix Part Group exists for
    * the a C_REQ_TOOL.TOOL / Part_no_oem / Manufact_ref in Maintenix.
    *
    */

   @Test
   public void test_BLREQ_12308() {

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

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // C_REQ_TOOL
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_12 + "'" );
      lReqMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_12 + "'" );
      lReqMap.put( "REQ_HR", "'" + iREQ_HR_INT + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_TOOL, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-12308" );

   }


   /**
    * This test is to verify error code BLREQ-12308: More than one Maintenix Part Group exists for
    * the a C_REQ_TOOL.TOOL / Part_no_oem / Manufact_ref in Maintenix.
    *
    */

   @Test
   public void test_BLREQ_17300() {

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
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // C_REQ_TOOL
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'SYS-2'" );
      lReqMap.put( "REQ_TASK_CD", "'INVALID'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_9 + "'" );
      lReqMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_9 + "'" );
      lReqMap.put( "REQ_HR", "'" + iREQ_HR_INT + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_TOOL, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode_CReqTool( "BLREQ-17300" );

   }
}
