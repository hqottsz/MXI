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
 * c_req_part.
 *
 *
 */
public class ReqPart extends ReqTests {

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
    * c_req_part. REQ is on TRK of ACFT, PART is TRK, non-install
    *
    *
    */

   @Test
   public void testACFT_TRK_TRK_NoINSTALL_Sucessful() {

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

      // C_REQ_PART
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

      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // verify no error
      VerifyValidation();

   }


   /**
    * This test is to verify req_import.validate_req functionality of staging tables: c_req and
    * c_req_part. REQ is on TRK of ACFT, PART is TRK, install
    *
    *
    */

   @Test
   public void testACFT_TRK_TRK_INSTALL_Sucessful() {

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

      // C_REQ_PART
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_1 + "'" );
      lReqMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_1 + "'" );
      lReqMap.put( "REQ_QT", "'" + iREQ_QT + "'" );
      lReqMap.put( "IPC_REF_CD", "'" + iIPC_REF_CD + "'" );
      lReqMap.put( "POSITION", "'" + iPOSITION_1 + "'" );
      // lReqMap.put( "REMOVE_BOOL", "'" + iREMOVE_BOOL_Y + "'" );
      // lReqMap.put( "REMOVE_REASON_CD", "'" + iREMOVE_REASON_CD + "'" );
      // lReqMap.put( "INSTALL_BOOL", "'" + iINSTALL_BOOL_N + "'" );
      // lReqMap.put( "REQ_PRIORITY_CD", "'" + iREQ_PRIORITY_CD + "'" );
      // lReqMap.put( "PART_PROVIDER_TYPE_CD", "'" + iPART_PROVIDER_TYPE_CD + "'" );
      // lReqMap.put( "IS_PART_SPECIFIC_BOOL", "'" + iIS_PART_SPECIFIC_BOOL_Y + "'" );

      lReqMap.put( "REMOVE_BOOL", null );
      lReqMap.put( "REMOVE_REASON_CD", null );
      lReqMap.put( "INSTALL_BOOL", null );
      lReqMap.put( "REQ_PRIORITY_CD", null );
      lReqMap.put( "PART_PROVIDER_TYPE_CD", null );
      lReqMap.put( "IS_PART_SPECIFIC_BOOL", null );

      lReqMap.put( "REQ_ACTION_CD", "'" + iREQ_ACTION_CD_REQ + "'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_PART, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // verify no error
      VerifyValidation();

   }


   /**
    * This test is to verify req_import.validate_req functionality of staging tables: c_req and
    * c_req_part. REQ is on sys of ENG, PART is batch, install
    *
    *
    */

   @Test
   public void testENG_SYS_BATCH_INSTALL_Sucessful() {

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

      // C_REQ_PART
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iENG_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_3 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_2 + "'" );
      lReqMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_2 + "'" );
      lReqMap.put( "REQ_QT", "'" + iREQ_QT + "'" );
      // lReqMap.put( "IPC_REF_CD", "'" + iIPC_REF_CD + "'" );
      // lReqMap.put( "POSITION", "'" + iPOSITION_1 + "'" );
      // lReqMap.put( "REMOVE_BOOL", "'" + iREMOVE_BOOL_Y + "'" );
      // lReqMap.put( "REMOVE_REASON_CD", "'" + iREMOVE_REASON_CD + "'" );
      // lReqMap.put( "INSTALL_BOOL", "'" + iINSTALL_BOOL_N + "'" );
      // lReqMap.put( "REQ_PRIORITY_CD", "'" + iREQ_PRIORITY_CD + "'" );
      // lReqMap.put( "PART_PROVIDER_TYPE_CD", "'" + iPART_PROVIDER_TYPE_CD + "'" );
      // lReqMap.put( "IS_PART_SPECIFIC_BOOL", "'" + iIS_PART_SPECIFIC_BOOL_Y + "'" );

      lReqMap.put( "IPC_REF_CD", null );
      lReqMap.put( "POSITION", null );
      lReqMap.put( "REMOVE_BOOL", null );
      lReqMap.put( "REMOVE_REASON_CD", null );
      lReqMap.put( "INSTALL_BOOL", null );
      lReqMap.put( "REQ_PRIORITY_CD", null );
      lReqMap.put( "PART_PROVIDER_TYPE_CD", null );
      lReqMap.put( "IS_PART_SPECIFIC_BOOL", null );
      lReqMap.put( "REQ_ACTION_CD", "'" + iREQ_ACTION_CD_ASREQ + "'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_PART, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // verify no error
      VerifyValidation();

   }


   /**
    * This test is to verify req_import.validate_req functionality of staging tables: c_req and
    * c_req_part. REQ is on SYS of APU, PART is SER, install
    *
    *
    */

   @Test
   public void testAPU_SYS_SER_INSTALL_Sucessful() {

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

      // C_REQ_PART
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iAPU_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iAPU_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_4 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_3 + "'" );
      lReqMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_3 + "'" );
      lReqMap.put( "REQ_QT", "'" + iREQ_QT + "'" );
      // lReqMap.put( "IPC_REF_CD", "'" + iIPC_REF_CD + "'" );
      // lReqMap.put( "POSITION", "'" + iPOSITION_1 + "'" );
      // lReqMap.put( "REMOVE_BOOL", "'" + iREMOVE_BOOL_Y + "'" );
      // lReqMap.put( "REMOVE_REASON_CD", "'" + iREMOVE_REASON_CD + "'" );
      // lReqMap.put( "INSTALL_BOOL", "'" + iINSTALL_BOOL_N + "'" );
      // lReqMap.put( "REQ_PRIORITY_CD", "'" + iREQ_PRIORITY_CD + "'" );
      // lReqMap.put( "PART_PROVIDER_TYPE_CD", "'" + iPART_PROVIDER_TYPE_CD + "'" );
      // lReqMap.put( "IS_PART_SPECIFIC_BOOL", "'" + iIS_PART_SPECIFIC_BOOL_Y + "'" );

      lReqMap.put( "IPC_REF_CD", null );
      lReqMap.put( "POSITION", null );
      lReqMap.put( "REMOVE_BOOL", null );
      lReqMap.put( "REMOVE_REASON_CD", null );
      lReqMap.put( "INSTALL_BOOL", null );
      lReqMap.put( "REQ_PRIORITY_CD", null );
      lReqMap.put( "PART_PROVIDER_TYPE_CD", null );
      lReqMap.put( "IS_PART_SPECIFIC_BOOL", null );
      lReqMap.put( "REQ_ACTION_CD", "'" + iREQ_ACTION_CD_ASREQ + "'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_PART, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // verify no error
      VerifyValidation();

   }


   /**
    * This test is to verify req_import.validate_req functionality of staging tables: c_req and
    * c_req_part. REQ is on SYS of ACFT, PART is BATCH, install
    *
    *
    */

   @Test
   public void testACFT_SYS_BATCH_INSTALL_Sucessful() {

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

      // C_REQ_PART
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_2 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_4 + "'" );
      lReqMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_4 + "'" );
      lReqMap.put( "REQ_QT", "'" + iREQ_QT_10 + "'" );
      // lReqMap.put( "IPC_REF_CD", "'" + iIPC_REF_CD + "'" );
      // lReqMap.put( "POSITION", "'" + iPOSITION_1 + "'" );
      // lReqMap.put( "REMOVE_BOOL", "'" + iREMOVE_BOOL_Y + "'" );
      // lReqMap.put( "REMOVE_REASON_CD", "'" + iREMOVE_REASON_CD + "'" );
      // lReqMap.put( "INSTALL_BOOL", "'" + iINSTALL_BOOL_N + "'" );
      // lReqMap.put( "REQ_PRIORITY_CD", "'" + iREQ_PRIORITY_CD + "'" );
      // lReqMap.put( "PART_PROVIDER_TYPE_CD", "'" + iPART_PROVIDER_TYPE_CD + "'" );
      // lReqMap.put( "IS_PART_SPECIFIC_BOOL", "'" + iIS_PART_SPECIFIC_BOOL_Y + "'" );

      lReqMap.put( "IPC_REF_CD", null );
      lReqMap.put( "POSITION", null );
      lReqMap.put( "REMOVE_BOOL", null );
      lReqMap.put( "REMOVE_REASON_CD", null );
      lReqMap.put( "INSTALL_BOOL", null );
      lReqMap.put( "REQ_PRIORITY_CD", null );
      lReqMap.put( "PART_PROVIDER_TYPE_CD", null );
      lReqMap.put( "IS_PART_SPECIFIC_BOOL", null );
      lReqMap.put( "REQ_ACTION_CD", "'" + iREQ_ACTION_CD_ASREQ + "'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_PART, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // verify no error
      VerifyValidation();

   }


   /**
    * This test is to verify req_import.validate_req functionality of staging tables: c_req and
    * c_req_part. REQ is on sub_assy of ACFT, PART is ASSY, install
    *
    *
    */

   @Test
   public void testACFT_ASSY_ASSY_INSTALL_Sucessful() {

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

      // C_REQ_PART
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_ENG + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_5 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_5 + "'" );
      lReqMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_5 + "'" );
      lReqMap.put( "REQ_QT", "'" + iREQ_QT + "'" );
      lReqMap.put( "IPC_REF_CD", "'" + iIPC_REF_CD_2 + "'" );
      lReqMap.put( "POSITION", "'" + iPOSITION_1 + "'" );
      // lReqMap.put( "REMOVE_BOOL", "'" + iREMOVE_BOOL_Y + "'" );
      // lReqMap.put( "REMOVE_REASON_CD", "'" + iREMOVE_REASON_CD + "'" );
      // lReqMap.put( "INSTALL_BOOL", "'" + iINSTALL_BOOL_N + "'" );
      // lReqMap.put( "REQ_PRIORITY_CD", "'" + iREQ_PRIORITY_CD + "'" );
      // lReqMap.put( "PART_PROVIDER_TYPE_CD", "'" + iPART_PROVIDER_TYPE_CD + "'" );
      // lReqMap.put( "IS_PART_SPECIFIC_BOOL", "'" + iIS_PART_SPECIFIC_BOOL_Y + "'" );

      lReqMap.put( "REMOVE_BOOL", null );
      lReqMap.put( "REMOVE_REASON_CD", null );
      lReqMap.put( "INSTALL_BOOL", null );
      lReqMap.put( "REQ_PRIORITY_CD", null );
      lReqMap.put( "PART_PROVIDER_TYPE_CD", null );
      lReqMap.put( "IS_PART_SPECIFIC_BOOL", null );
      lReqMap.put( "REQ_ACTION_CD", "'" + iREQ_ACTION_CD_REQ + "'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_PART, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // verify no error
      VerifyValidation();

   }


   /**
    * This test is to verify req_import.validate_req functionality of staging tables: c_req and
    * c_req_part. REQ is on TRK of ACFT, PART is KIT, install
    *
    *
    */

   @Test
   public void testACFT_TRK_KIT_INSTALL_Sucessful() {

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

      // C_REQ_PART
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_6 + "'" );
      lReqMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_6 + "'" );
      lReqMap.put( "REQ_QT", "'" + iREQ_QT_10 + "'" );
      // lReqMap.put( "IPC_REF_CD", "'" + iIPC_REF_CD_2 + "'" );
      // lReqMap.put( "POSITION", "'" + iPOSITION_1 + "'" );
      // lReqMap.put( "REMOVE_BOOL", "'" + iREMOVE_BOOL_Y + "'" );
      // lReqMap.put( "REMOVE_REASON_CD", "'" + iREMOVE_REASON_CD + "'" );
      // lReqMap.put( "INSTALL_BOOL", "'" + iINSTALL_BOOL_N + "'" );
      // lReqMap.put( "REQ_PRIORITY_CD", "'" + iREQ_PRIORITY_CD + "'" );
      // lReqMap.put( "PART_PROVIDER_TYPE_CD", "'" + iPART_PROVIDER_TYPE_CD + "'" );
      // lReqMap.put( "IS_PART_SPECIFIC_BOOL", "'" + iIS_PART_SPECIFIC_BOOL_Y + "'" );

      lReqMap.put( "IPC_REF_CD", null );
      lReqMap.put( "POSITION", null );
      lReqMap.put( "REMOVE_BOOL", null );
      lReqMap.put( "REMOVE_REASON_CD", null );
      lReqMap.put( "INSTALL_BOOL", null );
      lReqMap.put( "REQ_PRIORITY_CD", null );
      lReqMap.put( "PART_PROVIDER_TYPE_CD", null );
      lReqMap.put( "IS_PART_SPECIFIC_BOOL", null );
      lReqMap.put( "REQ_ACTION_CD", "'" + iREQ_ACTION_CD_REQ + "'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_PART, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // verify no error
      VerifyValidation();

   }


   /**
    * This test is to verify req_import.validate_req functionality of staging tables: c_req and
    * c_req_part. REQ is on TRK of ACFT, PART is KIT, install
    *
    *
    */

   @Test
   public void testACFT_TRK_TRK_AllPositions_INSTALL_Sucessful() {

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

      // C_REQ_PART (left)
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_7 + "'" );
      lReqMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_7 + "'" );
      lReqMap.put( "REQ_QT", "'" + iREQ_QT + "'" );
      // lReqMap.put( "IPC_REF_CD", "'" + iIPC_REF_CD_2 + "'" );
      lReqMap.put( "POSITION", "'" + iPOSITION_LEFT + "'" );
      // lReqMap.put( "REMOVE_BOOL", "'" + iREMOVE_BOOL_Y + "'" );
      // lReqMap.put( "REMOVE_REASON_CD", "'" + iREMOVE_REASON_CD + "'" );
      // lReqMap.put( "INSTALL_BOOL", "'" + iINSTALL_BOOL_N + "'" );
      // lReqMap.put( "REQ_PRIORITY_CD", "'" + iREQ_PRIORITY_CD + "'" );
      // lReqMap.put( "PART_PROVIDER_TYPE_CD", "'" + iPART_PROVIDER_TYPE_CD + "'" );
      // lReqMap.put( "IS_PART_SPECIFIC_BOOL", "'" + iIS_PART_SPECIFIC_BOOL_Y + "'" );

      lReqMap.put( "REMOVE_BOOL", null );
      lReqMap.put( "REMOVE_REASON_CD", null );
      lReqMap.put( "INSTALL_BOOL", null );
      lReqMap.put( "REQ_PRIORITY_CD", null );
      lReqMap.put( "PART_PROVIDER_TYPE_CD", null );
      lReqMap.put( "IS_PART_SPECIFIC_BOOL", null );
      lReqMap.put( "REQ_ACTION_CD", "'" + iREQ_ACTION_CD_REQ + "'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_PART, lReqMap ) );

      // C_REQ_PART (center)
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_7 + "'" );
      lReqMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_7 + "'" );
      lReqMap.put( "REQ_QT", "'" + iREQ_QT + "'" );
      // lReqMap.put( "IPC_REF_CD", "'" + iIPC_REF_CD_2 + "'" );
      lReqMap.put( "POSITION", "'" + iPOSITION_CENTER + "'" );
      // lReqMap.put( "REMOVE_BOOL", "'" + iREMOVE_BOOL_Y + "'" );
      // lReqMap.put( "REMOVE_REASON_CD", "'" + iREMOVE_REASON_CD + "'" );
      // lReqMap.put( "INSTALL_BOOL", "'" + iINSTALL_BOOL_N + "'" );
      // lReqMap.put( "REQ_PRIORITY_CD", "'" + iREQ_PRIORITY_CD + "'" );
      // lReqMap.put( "PART_PROVIDER_TYPE_CD", "'" + iPART_PROVIDER_TYPE_CD + "'" );
      // lReqMap.put( "IS_PART_SPECIFIC_BOOL", "'" + iIS_PART_SPECIFIC_BOOL_Y + "'" );
      lReqMap.put( "REQ_ACTION_CD", "'" + iREQ_ACTION_CD_REQ + "'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_PART, lReqMap ) );

      // C_REQ_PART (right)
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_7 + "'" );
      lReqMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_7 + "'" );
      lReqMap.put( "REQ_QT", "'" + iREQ_QT + "'" );
      // lReqMap.put( "IPC_REF_CD", "'" + iIPC_REF_CD_2 + "'" );
      lReqMap.put( "POSITION", "'" + iPOSITION_RIGHT + "'" );
      // lReqMap.put( "REMOVE_BOOL", "'" + iREMOVE_BOOL_Y + "'" );
      // lReqMap.put( "REMOVE_REASON_CD", "'" + iREMOVE_REASON_CD + "'" );
      lReqMap.put( "INSTALL_BOOL", "'" + iINSTALL_BOOL_Y + "'" );
      // lReqMap.put( "REQ_PRIORITY_CD", "'" + iREQ_PRIORITY_CD + "'" );
      // lReqMap.put( "PART_PROVIDER_TYPE_CD", "'" + iPART_PROVIDER_TYPE_CD + "'" );
      // lReqMap.put( "IS_PART_SPECIFIC_BOOL", "'" + iIS_PART_SPECIFIC_BOOL_Y + "'" );
      lReqMap.put( "REQ_ACTION_CD", "'" + iREQ_ACTION_CD_NOREQ + "'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_PART, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // verify no error
      VerifyValidation();

   }


   /**
    * This test is to verify error code BLREQ-10200: C_REQ_PART.ASSMBL_CD must be provided.
    *
    *
    */

   @Test
   public void test_BLREQ_10200() {
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

      // C_REQ_PART
      lReqMap.clear();
      // lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ASSMBL_CD", null );
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

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-10200" );

   }


   /**
    * This test is to verify error code BLREQ-10201: C_REQ_LABOR.ASSMBL_CD must be provided.
    *
    *
    */

   @Test
   public void test_BLREQ_10201() {
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

      // C_REQ_PART
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      // lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_ATA_CD", null );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_2 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_4 + "'" );
      lReqMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_4 + "'" );
      lReqMap.put( "REQ_QT", "'" + iREQ_QT + "'" );
      // lReqMap.put( "IPC_REF_CD", "'" + iIPC_REF_CD + "'" );
      // lReqMap.put( "POSITION", "'" + iPOSITION_1 + "'" );
      // lReqMap.put( "REMOVE_BOOL", "'" + iREMOVE_BOOL_Y + "'" );
      // lReqMap.put( "REMOVE_REASON_CD", "'" + iREMOVE_REASON_CD + "'" );
      // lReqMap.put( "INSTALL_BOOL", "'" + iINSTALL_BOOL_N + "'" );
      // lReqMap.put( "REQ_PRIORITY_CD", "'" + iREQ_PRIORITY_CD + "'" );
      // lReqMap.put( "PART_PROVIDER_TYPE_CD", "'" + iPART_PROVIDER_TYPE_CD + "'" );
      // lReqMap.put( "IS_PART_SPECIFIC_BOOL", "'" + iIS_PART_SPECIFIC_BOOL_Y + "'" );
      lReqMap.put( "IPC_REF_CD", null );
      lReqMap.put( "POSITION", null );
      lReqMap.put( "REMOVE_BOOL", null );
      lReqMap.put( "REMOVE_REASON_CD", null );
      lReqMap.put( "INSTALL_BOOL", null );
      lReqMap.put( "REQ_PRIORITY_CD", null );
      lReqMap.put( "PART_PROVIDER_TYPE_CD", null );
      lReqMap.put( "IS_PART_SPECIFIC_BOOL", null );
      lReqMap.put( "REQ_ACTION_CD", "'" + iREQ_ACTION_CD_ASREQ + "'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_PART, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-10201" );

   }


   /**
    * This test is to verify error code BLREQ-10202: C_REQ_PART. REQ_TASK_CD must be provided.
    *
    *
    */

   @Test
   public void test_BLREQ_10202() {
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

      // C_REQ_PART
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iENG_REQ_ATA_CD_SYS + "'" );
      // lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_3 + "'" );
      lReqMap.put( "REQ_TASK_CD", null );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_2 + "'" );
      lReqMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_2 + "'" );
      lReqMap.put( "REQ_QT", "'" + iREQ_QT + "'" );
      // lReqMap.put( "IPC_REF_CD", "'" + iIPC_REF_CD + "'" );
      // lReqMap.put( "POSITION", "'" + iPOSITION_1 + "'" );
      // lReqMap.put( "REMOVE_BOOL", "'" + iREMOVE_BOOL_Y + "'" );
      // lReqMap.put( "REMOVE_REASON_CD", "'" + iREMOVE_REASON_CD + "'" );
      // lReqMap.put( "INSTALL_BOOL", "'" + iINSTALL_BOOL_N + "'" );
      // lReqMap.put( "REQ_PRIORITY_CD", "'" + iREQ_PRIORITY_CD + "'" );
      // lReqMap.put( "PART_PROVIDER_TYPE_CD", "'" + iPART_PROVIDER_TYPE_CD + "'" );
      // lReqMap.put( "IS_PART_SPECIFIC_BOOL", "'" + iIS_PART_SPECIFIC_BOOL_Y + "'" );

      lReqMap.put( "IPC_REF_CD", null );
      lReqMap.put( "POSITION", null );
      lReqMap.put( "REMOVE_BOOL", null );
      lReqMap.put( "REMOVE_REASON_CD", null );
      lReqMap.put( "INSTALL_BOOL", null );
      lReqMap.put( "REQ_PRIORITY_CD", null );
      lReqMap.put( "PART_PROVIDER_TYPE_CD", null );
      lReqMap.put( "IS_PART_SPECIFIC_BOOL", null );
      lReqMap.put( "REQ_ACTION_CD", "'" + iREQ_ACTION_CD_ASREQ + "'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_PART, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-10202" );

   }


   /**
    * This test is to verify error code BLREQ-10203: C_REQ_PART.PART_NO_OEM must be provided.
    *
    *
    */

   @Test
   public void test_BLREQ_10203() {
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

      // C_REQ_PART
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iAPU_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iAPU_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_4 + "'" );
      // lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_3 + "'" );
      lReqMap.put( "PART_NO_OEM", null );
      lReqMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_3 + "'" );
      lReqMap.put( "REQ_QT", "'" + iREQ_QT + "'" );
      // lReqMap.put( "IPC_REF_CD", "'" + iIPC_REF_CD + "'" );
      // lReqMap.put( "POSITION", "'" + iPOSITION_1 + "'" );
      // lReqMap.put( "REMOVE_BOOL", "'" + iREMOVE_BOOL_Y + "'" );
      // lReqMap.put( "REMOVE_REASON_CD", "'" + iREMOVE_REASON_CD + "'" );
      // lReqMap.put( "INSTALL_BOOL", "'" + iINSTALL_BOOL_N + "'" );
      // lReqMap.put( "REQ_PRIORITY_CD", "'" + iREQ_PRIORITY_CD + "'" );
      // lReqMap.put( "PART_PROVIDER_TYPE_CD", "'" + iPART_PROVIDER_TYPE_CD + "'" );
      // lReqMap.put( "IS_PART_SPECIFIC_BOOL", "'" + iIS_PART_SPECIFIC_BOOL_Y + "'" );

      lReqMap.put( "IPC_REF_CD", null );
      lReqMap.put( "POSITION", null );
      lReqMap.put( "REMOVE_BOOL", null );
      lReqMap.put( "REMOVE_REASON_CD", null );
      lReqMap.put( "INSTALL_BOOL", null );
      lReqMap.put( "REQ_PRIORITY_CD", null );
      lReqMap.put( "PART_PROVIDER_TYPE_CD", null );
      lReqMap.put( "IS_PART_SPECIFIC_BOOL", null );
      lReqMap.put( "REQ_ACTION_CD", "'" + iREQ_ACTION_CD_ASREQ + "'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_PART, lReqMap ) );
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-10203" );

   }


   /**
    * This test is to verify error code BLREQ-10204: C_REQ_PART.MANUFACT_REF must be provided.
    *
    *
    */

   @Test
   public void test_BLREQ_10204() {
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

      // C_REQ_PART
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_1 + "'" );
      // lReqMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_1 + "'" );
      lReqMap.put( "MANUFACT_REF", null );
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

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-10204" );

   }


   /**
    * This test is to verify error code BLREQ-10205: C_REQ_PART.REQ_QT must be provided.
    *
    *
    */

   @Test
   public void test_BLREQ_10205() {
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

      // C_REQ_PART
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_1 + "'" );
      lReqMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_1 + "'" );
      lReqMap.put( "REQ_QT", null );
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

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-10205" );

   }


   /**
    * This test is to verify error code BLREQ-10206: C_REQ_PART.REMOVE_BOOL must be Y when a removal
    * reason is provided.
    *
    *
    */

   @Test
   public void test_BLREQ_10206() {
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

      // C_REQ_PART
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_1 + "'" );
      lReqMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_1 + "'" );
      lReqMap.put( "REQ_QT", "'" + iREQ_QT + "'" );
      lReqMap.put( "IPC_REF_CD", "'" + iIPC_REF_CD + "'" );
      lReqMap.put( "POSITION", "'" + iPOSITION_1 + "'" );
      lReqMap.put( "REMOVE_BOOL", "'" + iREMOVE_BOOL_N + "'" );
      lReqMap.put( "REMOVE_REASON_CD", "'" + iREMOVE_REASON_CD + "'" );
      lReqMap.put( "INSTALL_BOOL", "'" + iINSTALL_BOOL_N + "'" );
      lReqMap.put( "REQ_PRIORITY_CD", "'" + iREQ_PRIORITY_CD + "'" );
      lReqMap.put( "PART_PROVIDER_TYPE_CD", "'" + iPART_PROVIDER_TYPE_CD + "'" );
      lReqMap.put( "IS_PART_SPECIFIC_BOOL", "'" + iIS_PART_SPECIFIC_BOOL_Y + "'" );
      lReqMap.put( "REQ_ACTION_CD", "'" + iREQ_ACTION_CD_NOREQ + "'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_PART, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-10206" );

   }


   /**
    * This test is to verify error code BLREQ-11200:
    * C_REQ_PART.ASSMBL_CD/REQ_ATA_CD/REQ_TASK_CD/PART_NO_OEM/MANUFACT_REF/POSITION exists multiple
    * times in staging area.
    *
    *
    */

   @Test
   public void test_BLREQ_11200() {
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

      // C_REQ_PART
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

      // insert first record
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_PART, lReqMap ) );

      // insert second record
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_PART, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-11200" );

   }


   /**
    * This test is to verify error code BLREQ-12214: C_REQ_PART. Request Quantity cannot be
    * negative.
    *
    *
    */

   @Test
   public void test_BLREQ_12214() {
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

      // C_REQ_PART
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iENG_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_3 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_2 + "'" );
      lReqMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_2 + "'" );
      // lReqMap.put( "REQ_QT", "'" + iREQ_QT + "'" );
      lReqMap.put( "REQ_QT", "'-1'" );
      // lReqMap.put( "IPC_REF_CD", "'" + iIPC_REF_CD + "'" );
      // lReqMap.put( "POSITION", "'" + iPOSITION_1 + "'" );
      // lReqMap.put( "REMOVE_BOOL", "'" + iREMOVE_BOOL_Y + "'" );
      // lReqMap.put( "REMOVE_REASON_CD", "'" + iREMOVE_REASON_CD + "'" );
      // lReqMap.put( "INSTALL_BOOL", "'" + iINSTALL_BOOL_N + "'" );
      // lReqMap.put( "REQ_PRIORITY_CD", "'" + iREQ_PRIORITY_CD + "'" );
      // lReqMap.put( "PART_PROVIDER_TYPE_CD", "'" + iPART_PROVIDER_TYPE_CD + "'" );
      // lReqMap.put( "IS_PART_SPECIFIC_BOOL", "'" + iIS_PART_SPECIFIC_BOOL_Y + "'" );

      lReqMap.put( "IPC_REF_CD", null );
      lReqMap.put( "POSITION", null );
      lReqMap.put( "REMOVE_BOOL", null );
      lReqMap.put( "REMOVE_REASON_CD", null );
      lReqMap.put( "INSTALL_BOOL", null );
      lReqMap.put( "REQ_PRIORITY_CD", null );
      lReqMap.put( "PART_PROVIDER_TYPE_CD", null );
      lReqMap.put( "IS_PART_SPECIFIC_BOOL", null );
      lReqMap.put( "REQ_ACTION_CD", "'" + iREQ_ACTION_CD_ASREQ + "'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_PART, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-12214" );

   }


   /**
    * This test is to verify error code BLREQ-12215:C_REQ_PART.Request Quantity must be 1 for part
    * with inventory class value of `ASSY or `TRK.
    *
    *
    */

   @Test
   public void test_BLREQ_12215_1() {
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

      // C_REQ_PART
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_1 + "'" );
      lReqMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_1 + "'" );
      // lReqMap.put( "REQ_QT", "'" + iREQ_QT + "'" );
      lReqMap.put( "REQ_QT", "'10'" );
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

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-12215" );

   }


   /**
    * This test is to verify error code BLREQ-12215:C_REQ_PART.Request Quantity must be 1 for part
    * with inventory class value of `ASSY or `TRK.
    *
    *
    */

   @Test
   public void test_BLREQ_12215_2() {
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

      // C_REQ_PART
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_ENG + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_5 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_5 + "'" );
      lReqMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_5 + "'" );
      // lReqMap.put( "REQ_QT", "'" + iREQ_QT + "'" );
      lReqMap.put( "REQ_QT", "'10'" );
      lReqMap.put( "IPC_REF_CD", "'" + iIPC_REF_CD_2 + "'" );
      lReqMap.put( "POSITION", "'" + iPOSITION_1 + "'" );
      // lReqMap.put( "REMOVE_BOOL", "'" + iREMOVE_BOOL_Y + "'" );
      // lReqMap.put( "REMOVE_REASON_CD", "'" + iREMOVE_REASON_CD + "'" );
      // lReqMap.put( "INSTALL_BOOL", "'" + iINSTALL_BOOL_N + "'" );
      // lReqMap.put( "REQ_PRIORITY_CD", "'" + iREQ_PRIORITY_CD + "'" );
      // lReqMap.put( "PART_PROVIDER_TYPE_CD", "'" + iPART_PROVIDER_TYPE_CD + "'" );
      // lReqMap.put( "IS_PART_SPECIFIC_BOOL", "'" + iIS_PART_SPECIFIC_BOOL_Y + "'" );

      lReqMap.put( "REMOVE_BOOL", null );
      lReqMap.put( "REMOVE_REASON_CD", null );
      lReqMap.put( "INSTALL_BOOL", null );
      lReqMap.put( "REQ_PRIORITY_CD", null );
      lReqMap.put( "PART_PROVIDER_TYPE_CD", null );
      lReqMap.put( "IS_PART_SPECIFIC_BOOL", null );
      lReqMap.put( "REQ_ACTION_CD", "'" + iREQ_ACTION_CD_REQ + "'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_PART, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-12215" );

   }


   /**
    * This test is to verify error code BLREQ-12216:C_REQ_PART.Request Priority Code is not valid.
    *
    *
    */

   @Test
   public void test_BLREQ_12216() {
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

      // C_REQ_PART
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iENG_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_3 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_2 + "'" );
      lReqMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_2 + "'" );
      lReqMap.put( "REQ_QT", "'" + iREQ_QT + "'" );
      // lReqMap.put( "IPC_REF_CD", "'" + iIPC_REF_CD + "'" );
      // lReqMap.put( "POSITION", "'" + iPOSITION_1 + "'" );
      // lReqMap.put( "REMOVE_BOOL", "'" + iREMOVE_BOOL_Y + "'" );
      // lReqMap.put( "REMOVE_REASON_CD", "'" + iREMOVE_REASON_CD + "'" );
      // lReqMap.put( "INSTALL_BOOL", "'" + iINSTALL_BOOL_N + "'" );
      // lReqMap.put( "REQ_PRIORITY_CD", "'" + iREQ_PRIORITY_CD + "'" );
      // lReqMap.put( "PART_PROVIDER_TYPE_CD", "'" + iPART_PROVIDER_TYPE_CD + "'" );
      // lReqMap.put( "IS_PART_SPECIFIC_BOOL", "'" + iIS_PART_SPECIFIC_BOOL_Y + "'" );

      lReqMap.put( "IPC_REF_CD", null );
      lReqMap.put( "POSITION", null );
      lReqMap.put( "REMOVE_BOOL", null );
      lReqMap.put( "REMOVE_REASON_CD", null );
      lReqMap.put( "INSTALL_BOOL", null );
      lReqMap.put( "REQ_PRIORITY_CD", "'INVALID'" );
      lReqMap.put( "PART_PROVIDER_TYPE_CD", null );
      lReqMap.put( "IS_PART_SPECIFIC_BOOL", null );
      lReqMap.put( "REQ_ACTION_CD", "'" + iREQ_ACTION_CD_ASREQ + "'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_PART, lReqMap ) );
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-12216" );

   }


   /**
    * This test is to verify error code BLREQ-12217:C_REQ_PART.Install Boolean is not valid.
    *
    *
    */

   @Test
   public void test_BLREQ_12217() {
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

      // C_REQ_PART
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iAPU_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iAPU_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_4 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_3 + "'" );
      lReqMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_3 + "'" );
      lReqMap.put( "REQ_QT", "'" + iREQ_QT + "'" );
      // lReqMap.put( "IPC_REF_CD", "'" + iIPC_REF_CD + "'" );
      // lReqMap.put( "POSITION", "'" + iPOSITION_1 + "'" );
      // lReqMap.put( "REMOVE_BOOL", "'" + iREMOVE_BOOL_Y + "'" );
      // lReqMap.put( "REMOVE_REASON_CD", "'" + iREMOVE_REASON_CD + "'" );
      // lReqMap.put( "INSTALL_BOOL", "'" + iINSTALL_BOOL_N + "'" );
      // lReqMap.put( "REQ_PRIORITY_CD", "'" + iREQ_PRIORITY_CD + "'" );
      // lReqMap.put( "PART_PROVIDER_TYPE_CD", "'" + iPART_PROVIDER_TYPE_CD + "'" );
      // lReqMap.put( "IS_PART_SPECIFIC_BOOL", "'" + iIS_PART_SPECIFIC_BOOL_Y + "'" );

      lReqMap.put( "IPC_REF_CD", null );
      lReqMap.put( "POSITION", null );
      lReqMap.put( "REMOVE_BOOL", null );
      lReqMap.put( "REMOVE_REASON_CD", null );
      lReqMap.put( "INSTALL_BOOL", "'1'" );
      lReqMap.put( "REQ_PRIORITY_CD", null );
      lReqMap.put( "PART_PROVIDER_TYPE_CD", null );
      lReqMap.put( "IS_PART_SPECIFIC_BOOL", null );
      lReqMap.put( "REQ_ACTION_CD", "'" + iREQ_ACTION_CD_ASREQ + "'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_PART, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-12217" );

   }


   /**
    * This test is to verify error code BLREQ-12219:C_REQ_PART.Remove Boolean is not valid
    *
    *
    */

   @Test
   public void test_BLREQ_12219() {
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

      // C_REQ_PART
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_1 + "'" );
      lReqMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_1 + "'" );
      lReqMap.put( "REQ_QT", "'" + iREQ_QT + "'" );
      lReqMap.put( "IPC_REF_CD", "'" + iIPC_REF_CD + "'" );
      lReqMap.put( "POSITION", "'" + iPOSITION_1 + "'" );
      // lReqMap.put( "REMOVE_BOOL", "'" + iREMOVE_BOOL_Y + "'" );
      // lReqMap.put( "REMOVE_REASON_CD", "'" + iREMOVE_REASON_CD + "'" );
      // lReqMap.put( "INSTALL_BOOL", "'" + iINSTALL_BOOL_N + "'" );
      // lReqMap.put( "REQ_PRIORITY_CD", "'" + iREQ_PRIORITY_CD + "'" );
      // lReqMap.put( "PART_PROVIDER_TYPE_CD", "'" + iPART_PROVIDER_TYPE_CD + "'" );
      // lReqMap.put( "IS_PART_SPECIFIC_BOOL", "'" + iIS_PART_SPECIFIC_BOOL_Y + "'" );

      lReqMap.put( "REMOVE_BOOL", "'0'" );
      lReqMap.put( "REMOVE_REASON_CD", null );
      lReqMap.put( "INSTALL_BOOL", null );
      lReqMap.put( "REQ_PRIORITY_CD", null );
      lReqMap.put( "PART_PROVIDER_TYPE_CD", null );
      lReqMap.put( "IS_PART_SPECIFIC_BOOL", null );

      lReqMap.put( "REQ_ACTION_CD", "'" + iREQ_ACTION_CD_REQ + "'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_PART, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-12219" );

   }


   /**
    * This test is to verify error code BLREQ-12220:C_REQ_PART.Part Provider type is not valid.
    *
    *
    */

   @Test
   public void test_BLREQ_12220() {
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

      // C_REQ_PART
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
      // lReqMap.put( "PART_PROVIDER_TYPE_CD", "'" + iPART_PROVIDER_TYPE_CD + "'" );
      lReqMap.put( "PART_PROVIDER_TYPE_CD", "'INVALID'" );
      lReqMap.put( "IS_PART_SPECIFIC_BOOL", "'" + iIS_PART_SPECIFIC_BOOL_Y + "'" );
      lReqMap.put( "REQ_ACTION_CD", "'" + iREQ_ACTION_CD_NOREQ + "'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_PART, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-12220" );

   }


   /**
    * This test is to verify error code BLREQ-12221:C_REQ_PART.Request Action Code is not valid.
    *
    *
    */

   @Test
   public void test_BLREQ_12221() {
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

      // C_REQ_PART
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
      lReqMap.put( "INSTALL_BOOL", "'" + iINSTALL_BOOL_Y + "'" );
      lReqMap.put( "REQ_PRIORITY_CD", "'" + iREQ_PRIORITY_CD + "'" );
      lReqMap.put( "PART_PROVIDER_TYPE_CD", "'" + iPART_PROVIDER_TYPE_CD + "'" );
      lReqMap.put( "IS_PART_SPECIFIC_BOOL", "'" + iIS_PART_SPECIFIC_BOOL_Y + "'" );
      // lReqMap.put( "REQ_ACTION_CD", "'" + iREQ_ACTION_CD_ASREQ + "'" );
      lReqMap.put( "REQ_ACTION_CD", "'INVALID'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_PART, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-12221" );

   }


   /**
    * This test is to verify error code BLREQ-12222:C_REQ_PART.Request Action Code cannot be other
    * values than NOREQ when INSTALL_BOOL is set to `N.
    *
    *
    */

   @Test
   public void test_BLREQ_12222() {
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

      // C_REQ_PART
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
      // lReqMap.put( "REQ_ACTION_CD", "'" + iREQ_ACTION_CD_NOREQ + "'" );
      lReqMap.put( "REQ_ACTION_CD", "'" + iREQ_ACTION_CD_ASREQ + "'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_PART, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-12222" );

   }


   /**
    * This test is to verify error code BLREQ-12223:_REQ_PART.Request Action Code must be provided
    * when Install_bool is set to `Y'.
    *
    *
    */

   @Test
   public void test_BLREQ_12223_1() {
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

      // C_REQ_PART
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
      lReqMap.put( "INSTALL_BOOL", "'" + iINSTALL_BOOL_Y + "'" );
      lReqMap.put( "REQ_PRIORITY_CD", "'" + iREQ_PRIORITY_CD + "'" );
      lReqMap.put( "PART_PROVIDER_TYPE_CD", "'" + iPART_PROVIDER_TYPE_CD + "'" );
      lReqMap.put( "IS_PART_SPECIFIC_BOOL", "'" + iIS_PART_SPECIFIC_BOOL_Y + "'" );
      // lReqMap.put( "REQ_ACTION_CD", "'" + iREQ_ACTION_CD_NOREQ + "'" );
      lReqMap.put( "REQ_ACTION_CD", null );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_PART, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-12223" );

   }


   /**
    * This test is to verify error code BLREQ-12223:_REQ_PART.Request Action Code must be provided
    * when Install_bool is set to `Y'.
    *
    *
    */

   @Test
   public void test_BLREQ_12223_2() {
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

      // C_REQ_PART
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
      // lReqMap.put( "INSTALL_BOOL", "'" + iINSTALL_BOOL_Y + "'" );
      lReqMap.put( "INSTALL_BOOL", null );
      lReqMap.put( "REQ_PRIORITY_CD", "'" + iREQ_PRIORITY_CD + "'" );
      lReqMap.put( "PART_PROVIDER_TYPE_CD", "'" + iPART_PROVIDER_TYPE_CD + "'" );
      lReqMap.put( "IS_PART_SPECIFIC_BOOL", "'" + iIS_PART_SPECIFIC_BOOL_Y + "'" );
      // lReqMap.put( "REQ_ACTION_CD", "'" + iREQ_ACTION_CD_NOREQ + "'" );
      lReqMap.put( "REQ_ACTION_CD", null );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_PART, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-12223" );

   }


   /**
    * This test is to verify error code BLREQ-12224:C_REQ_PART.Removal Reason Code is not valid
    *
    *
    */

   @Test
   public void test_BLREQ_12224() {
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

      // C_REQ_PART
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
      // lReqMap.put( "REMOVE_REASON_CD", "'" + iREMOVE_REASON_CD + "'" );
      lReqMap.put( "REMOVE_REASON_CD", "'INVALID'" );
      lReqMap.put( "INSTALL_BOOL", "'" + iINSTALL_BOOL_N + "'" );
      lReqMap.put( "REQ_PRIORITY_CD", "'" + iREQ_PRIORITY_CD + "'" );
      lReqMap.put( "PART_PROVIDER_TYPE_CD", "'" + iPART_PROVIDER_TYPE_CD + "'" );
      lReqMap.put( "IS_PART_SPECIFIC_BOOL", "'" + iIS_PART_SPECIFIC_BOOL_Y + "'" );
      lReqMap.put( "REQ_ACTION_CD", "'" + iREQ_ACTION_CD_NOREQ + "'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_PART, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-12224" );

   }


   /**
    * This test is to verify error code BLREQ-12203:C_REQ_PART.Assembly/ATA Code/REQ Task Code is
    * invalid.
    *
    *
    */

   @Test
   public void test_BLREQ_12203() {
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

      // C_REQ_PART
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'ENG-SYS-2'" );
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
      lReqMap.put( "IS_PART_SPECIFIC_BOOL", null );
      lReqMap.put( "REQ_ACTION_CD", "'" + iREQ_ACTION_CD_ASREQ + "'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_PART, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-12203" );

   }


   /**
    * This test is to verify error code BLREQ-12204:C_REQ_PART.Part Number is invalid.
    *
    *
    */

   @Test
   public void test_BLREQ_12204() {
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

      // C_REQ_PART
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iAPU_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iAPU_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_4 + "'" );
      lReqMap.put( "PART_NO_OEM", "'INVALID'" );
      lReqMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_3 + "'" );
      lReqMap.put( "REQ_QT", "'" + iREQ_QT + "'" );
      lReqMap.put( "IPC_REF_CD", null );
      lReqMap.put( "POSITION", null );
      lReqMap.put( "REMOVE_BOOL", null );
      lReqMap.put( "REMOVE_REASON_CD", null );
      lReqMap.put( "INSTALL_BOOL", null );
      lReqMap.put( "REQ_PRIORITY_CD", null );
      lReqMap.put( "PART_PROVIDER_TYPE_CD", null );
      lReqMap.put( "IS_PART_SPECIFIC_BOOL", null );
      lReqMap.put( "REQ_ACTION_CD", "'" + iREQ_ACTION_CD_ASREQ + "'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_PART, lReqMap ) );
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-12204" );

   }


   /**
    * This test is to verify error code BLREQ-12205:C_REQ_PART.Manufacturer is invalid.
    *
    */

   @Test
   public void test_BLREQ_12205() {
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

      // C_REQ_PART
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_2 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_4 + "'" );
      lReqMap.put( "MANUFACT_REF", "'INVALID'" );
      lReqMap.put( "REQ_QT", "'" + iREQ_QT_10 + "'" );
      lReqMap.put( "IPC_REF_CD", null );
      lReqMap.put( "POSITION", null );
      lReqMap.put( "REMOVE_BOOL", null );
      lReqMap.put( "REMOVE_REASON_CD", null );
      lReqMap.put( "INSTALL_BOOL", null );
      lReqMap.put( "REQ_PRIORITY_CD", null );
      lReqMap.put( "PART_PROVIDER_TYPE_CD", null );
      lReqMap.put( "IS_PART_SPECIFIC_BOOL", null );
      lReqMap.put( "REQ_ACTION_CD", "'" + iREQ_ACTION_CD_ASREQ + "'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_PART, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-12205" );

   }


   /**
    * This test is to verify error code BLREQ-12206:C_REQ_PART.Part Number /Manufacturer is invalid.
    *
    */

   @Test
   public void test_BLREQ_12206() {
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

      // C_REQ_PART
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_1 + "'" );
      lReqMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_3 + "'" );
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

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_PART, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-12206" );

   }


   /**
    * This test is to verify error code BLREQ-12207:C_REQ_PART. Assembly /Part Number / Manufacturer
    * is valid but is not `ASSY or `TRK or `SER or `BATCH.
    *
    */

   @Test
   public void test_BLREQ_12207() {
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

      // C_REQ_PART
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
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

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_PART, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-12207" );

   }


   /**
    * This test is to verify error code BLREQ-12208:C_REQ_PART.IPC Reference Code provided is not
    * valid.
    *
    */

   @Test
   public void test_BLREQ_12208() {
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

      // C_REQ_PART
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_1 + "'" );
      lReqMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_1 + "'" );
      lReqMap.put( "REQ_QT", "'" + iREQ_QT + "'" );
      lReqMap.put( "IPC_REF_CD", "'INVALID'" );
      lReqMap.put( "POSITION", "'" + iPOSITION_1 + "'" );
      lReqMap.put( "REMOVE_BOOL", "'" + iREMOVE_BOOL_Y + "'" );
      lReqMap.put( "REMOVE_REASON_CD", "'" + iREMOVE_REASON_CD + "'" );
      lReqMap.put( "INSTALL_BOOL", "'" + iINSTALL_BOOL_N + "'" );
      lReqMap.put( "REQ_PRIORITY_CD", "'" + iREQ_PRIORITY_CD + "'" );
      lReqMap.put( "PART_PROVIDER_TYPE_CD", "'" + iPART_PROVIDER_TYPE_CD + "'" );
      lReqMap.put( "IS_PART_SPECIFIC_BOOL", "'" + iIS_PART_SPECIFIC_BOOL_Y + "'" );
      lReqMap.put( "REQ_ACTION_CD", "'" + iREQ_ACTION_CD_NOREQ + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_PART, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-12208" );

   }


   /**
    * This test is to verify error code BLREQ-12209:C_REQ_PART. Part_no_oem/manufact_ref/IPC_Ref_CD
    * specified on Assmbl_cd or common hardware assembly does not exist in Maintenix
    *
    */

   @Test
   public void test_BLREQ_12209_1() {
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

      // C_REQ_PART
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iENG_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_3 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_1 + "'" );
      lReqMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_1 + "'" );
      lReqMap.put( "REQ_QT", "'" + iREQ_QT + "'" );
      lReqMap.put( "IPC_REF_CD", "'" + iIPC_REF_CD + "'" );
      lReqMap.put( "POSITION", null );
      lReqMap.put( "REMOVE_BOOL", null );
      lReqMap.put( "REMOVE_REASON_CD", null );
      lReqMap.put( "INSTALL_BOOL", null );
      lReqMap.put( "REQ_PRIORITY_CD", null );
      lReqMap.put( "PART_PROVIDER_TYPE_CD", null );
      lReqMap.put( "IS_PART_SPECIFIC_BOOL", null );
      lReqMap.put( "REQ_ACTION_CD", "'" + iREQ_ACTION_CD_REQ + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_PART, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-12209" );

   }


   /**
    * This test is to verify error code BLREQ-12209:C_REQ_PART. Part_no_oem/manufact_ref/IPC_Ref_CD
    * specified on Assmbl_cd or common hardware assembly does not exist in Maintenix
    *
    */

   @Test
   public void test_BLREQ_12209_2() {
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

      // C_REQ_PART
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_2 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_4 + "'" );
      lReqMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_4 + "'" );
      lReqMap.put( "REQ_QT", "'" + iREQ_QT_10 + "'" );
      lReqMap.put( "IPC_REF_CD", "'" + iIPC_REF_CD_2 + "'" );
      lReqMap.put( "POSITION", null );
      lReqMap.put( "REMOVE_BOOL", null );
      lReqMap.put( "REMOVE_REASON_CD", null );
      lReqMap.put( "INSTALL_BOOL", null );
      lReqMap.put( "REQ_PRIORITY_CD", null );
      lReqMap.put( "PART_PROVIDER_TYPE_CD", null );
      lReqMap.put( "IS_PART_SPECIFIC_BOOL", null );
      lReqMap.put( "REQ_ACTION_CD", "'" + iREQ_ACTION_CD_REQ + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_PART, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-12209" );

   }


   /**
    * This test is to verify error code BLREQ-12210:More than one Maintenix Part Group exists for
    * the a C_REQ_PART.Assmbl_cd or common hardware assembly code/ Part_no_oem / Manufact_ref in
    * C_REQ_PART when C_REQ_PART.IPC_REF_CD not provided.
    *
    */

   @Test
   public void test_BLREQ_12210() {
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

      // C_REQ_PART
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_SYS + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_2 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_8 + "'" );
      lReqMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_8 + "'" );
      lReqMap.put( "REQ_QT", "'" + iREQ_QT + "'" );
      lReqMap.put( "IPC_REF_CD", null );
      lReqMap.put( "POSITION", "'" + iPOSITION_1 + "'" );
      lReqMap.put( "REMOVE_BOOL", null );
      lReqMap.put( "REMOVE_REASON_CD", null );
      lReqMap.put( "INSTALL_BOOL", null );
      lReqMap.put( "REQ_PRIORITY_CD", null );
      lReqMap.put( "PART_PROVIDER_TYPE_CD", null );
      lReqMap.put( "IS_PART_SPECIFIC_BOOL", null );
      lReqMap.put( "REQ_ACTION_CD", "'" + iREQ_ACTION_CD_REQ + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_PART, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-12210" );

   }


   /**
    * This test is to verify error code BLREQ-12211:C_REQ_PART.AssemblyPart_no_oem/Manufacturer
    * /Position provided is not valid for the part with inventory class of `ASSY or `TRK When
    * C_REQ_PART.IPC Ref Code is not provided.
    *
    */

   @Test
   public void test_BLREQ_12211() {
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

      // C_REQ_PART
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_1 + "'" );
      lReqMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_1 + "'" );
      lReqMap.put( "REQ_QT", "'" + iREQ_QT + "'" );
      // lReqMap.put( "IPC_REF_CD", "'" + iIPC_REF_CD + "'" );
      lReqMap.put( "POSITION", "'COMHW-POS-1'" );
      lReqMap.put( "REMOVE_BOOL", "'" + iREMOVE_BOOL_Y + "'" );
      lReqMap.put( "REMOVE_REASON_CD", "'" + iREMOVE_REASON_CD + "'" );
      lReqMap.put( "INSTALL_BOOL", "'" + iINSTALL_BOOL_N + "'" );
      lReqMap.put( "REQ_PRIORITY_CD", "'" + iREQ_PRIORITY_CD + "'" );
      lReqMap.put( "PART_PROVIDER_TYPE_CD", "'" + iPART_PROVIDER_TYPE_CD + "'" );
      lReqMap.put( "IS_PART_SPECIFIC_BOOL", "'" + iIS_PART_SPECIFIC_BOOL_Y + "'" );
      lReqMap.put( "REQ_ACTION_CD", "'" + iREQ_ACTION_CD_NOREQ + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_PART, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-12211" );

   }


   /**
    * This test is to verify error code BLREQ-12212:C__REQ_PART.AssemblyPart_no_oem/Manufacturer/IPC
    * Ref Code/Position provided is not valid for the part with inventory class of `ASSY or `TRK
    * When C_REQ_PART.IPC Ref Code is provided.
    *
    */

   @Test
   public void test_BLREQ_12212() {
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

      // C_REQ_PART
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_1 + "'" );
      lReqMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_1 + "'" );
      lReqMap.put( "REQ_QT", "'" + iREQ_QT + "'" );
      lReqMap.put( "IPC_REF_CD", "'" + iIPC_REF_CD + "'" );
      lReqMap.put( "POSITION", "'COMHW-POS-1'" );
      lReqMap.put( "REMOVE_BOOL", "'" + iREMOVE_BOOL_Y + "'" );
      lReqMap.put( "REMOVE_REASON_CD", "'" + iREMOVE_REASON_CD + "'" );
      lReqMap.put( "INSTALL_BOOL", "'" + iINSTALL_BOOL_N + "'" );
      lReqMap.put( "REQ_PRIORITY_CD", "'" + iREQ_PRIORITY_CD + "'" );
      lReqMap.put( "PART_PROVIDER_TYPE_CD", "'" + iPART_PROVIDER_TYPE_CD + "'" );
      lReqMap.put( "IS_PART_SPECIFIC_BOOL", "'" + iIS_PART_SPECIFIC_BOOL_Y + "'" );
      lReqMap.put( "REQ_ACTION_CD", "'" + iREQ_ACTION_CD_NOREQ + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_PART, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-12212" );

   }


   /**
    * This test is to verify error code BLREQ-12213:More than ONE position exists for the
    * C_REQ_PART.Assmbl_cd/part_no_oem/ manufact_ref / IPC_REF_CD.
    *
    */

   @Test
   public void test_BLREQ_12213() {
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

      // C_REQ_PART
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iACFT_REQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_7 + "'" );
      lReqMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_7 + "'" );
      lReqMap.put( "REQ_QT", "'" + iREQ_QT + "'" );
      lReqMap.put( "IPC_REF_CD", "'" + iIPC_REF_CD_7 + "'" );
      lReqMap.put( "POSITION", null );
      lReqMap.put( "REMOVE_BOOL", "'" + iREMOVE_BOOL_Y + "'" );
      lReqMap.put( "REMOVE_REASON_CD", "'" + iREMOVE_REASON_CD + "'" );
      lReqMap.put( "INSTALL_BOOL", "'" + iINSTALL_BOOL_N + "'" );
      lReqMap.put( "REQ_PRIORITY_CD", "'" + iREQ_PRIORITY_CD + "'" );
      lReqMap.put( "PART_PROVIDER_TYPE_CD", "'" + iPART_PROVIDER_TYPE_CD + "'" );
      lReqMap.put( "IS_PART_SPECIFIC_BOOL", "'" + iIS_PART_SPECIFIC_BOOL_Y + "'" );
      lReqMap.put( "REQ_ACTION_CD", "'" + iREQ_ACTION_CD_NOREQ + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_PART, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode( "BLREQ-12213" );

   }


   /**
    * This test is to verify error code BLREQ-17200:C_REQ_PART row is invalid because related row is
    * invalid.
    *
    */

   @Test
   public void test_BLREQ_17200() {
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

      // C_REQ_PART
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'SYS-2'" );
      lReqMap.put( "REQ_TASK_CD", "'INVALID'" );
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

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_PART, lReqMap ) );

      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify no error
      validateErrorCode_CReqPart( "BLREQ-17200" );

   }
}
