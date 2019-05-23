package com.mxi.mx.core.maint.plan.baselineloader.BLOCK;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.mxi.mx.core.maint.plan.datamodels.assmbleInfor;
import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;
import com.mxi.mx.util.BlockTest;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.WhereClause;


/**
 * This test suite contains test cases on validation functionality of C_BLOCK_REQ.
 */
public class BlockRequirements extends BlockTest {

   private final String iFOLLOW_ON_REQ_CODE = "aFollowOnReqCode";
   private final String iFOLLOW_ON_REQ_ATA_CODE = "ACFT_CD1";
   private final String iREPREF_REQ_CODE = "aRepRefReqCode";
   private final String iREPREF_REQ_ATA_CODE = "ACFT_CD1";
   private final String iASSEMBLY_CODE = "ACFT_CD1";
   private final String iBLOCK_ATA_CD = "ACFT_CD1";
   private final String iBLOCK_TASK_CD = "aBlockCode";
   private final String iBLOCK_TASK_NAME = "aBlockName";
   private final String iBLOCK_CLASS_CD = "BLOCK";
   private final String iLAST_DEADLINE_DRIVER_BOOL = "Y";
   private final String iSCHED_FROM_MANUFACT_DT_BOOL = "Y";
   private final String iTASK_MUST_REMOVE_CD = "OFFPARENT";
   private final String iON_CONDITION_BOOL = "Y";
   private final String iON_CONDITION_BOOL_Y_number = "1";
   private final String TASK_CLASS_CD_FOLLOW = "FOLLOW";
   private final String TASK_CLASS_CD_REPREF = "REPREF";

   private simpleIDs iTaskDefinitionIDs = null;
   private ArrayList<simpleIDs> iDeleteRequirementIDs = new ArrayList<simpleIDs>();
   private ArrayList<String> iDeleteRequirementCodes = new ArrayList<String>();

   // ==================================================================

   public String iACFT_ASSMBLCD = "ACFT_CD1";
   public String iENG_ASSMBLCD = "ENG_CD1";
   public String iBLOCK_ATA_CD_SYS = "SYS-1-1";
   public String iBLOCK_ATA_CD_TRK = "ACFT-SYS-1-1-TRK-BATCH-PARENT";
   public String iBLOCK_ATA_CD_ACFT = "ACFT_CD1";
   public String iBLOCK_ATA_CD_ENG_TRK = "ENG-SYS-1-1-TRK-BATCH-PARENT";
   public String iBLOCK_TASK_CD_1 = "ATEST";
   public String iBLOCK_TASK_CD_2 = "BTEST";

   // C_BLOCK table
   public String iBLOCK_TASK_NAME_1 = "ATESTNAME";
   public String iBLOCK_TASK_DESC_1 = "ATESTDESC";
   public String iBLOCK_TASK_REF_SDESC_1 = "ATESTREFDESC";
   public String iBLOCK_TASK_NAME_2 = "BTESTNAME";
   public String iBLOCK_TASK_DESC_2 = "BTESTDESC";
   public String iBLOCK_TASK_REF_SDESC_2 = "BTESTREFDESC";
   public String iCLASS_CD = "CHECK";
   public String iCLASS_CD_COMP = "REQ";
   public String iSUBCLASS_CD = "A-CHECK";
   public String iSUBCLASS_CD_COMP = "TEST2";
   public String iORIGINATOR_CD = "AWL";
   public String iAPPLICABILITY_DESC = "1,5-7";
   public String iRESCHED_FROM_CD = "EXECUTE";
   public String iLAST_DEADLINE_DRIVER_BOOL_N = "1";
   public String iSOFT_DEADLINE_DRIVER_BOOL = "Y";
   public String iSOFT_DEADLINE_DRIVER_BOOL_N = "1";
   public String iSCHED_FROM_MANUFACT_DT_BOOL_N = "1";
   public String iWORKSCOPE_BOOL = "Y";
   public String iWORKSCOPE_BOOL_N = "1";
   public String iON_CONDITION_BOOL_N = "N";
   public String iON_CONDITION_BOOL_N_number = "0";
   public String iWORK_TYPE_LIST_1 = "SERVICE";
   public String iWORK_TYPE_LIST_2 = "FUEL";

   // C_BLOCK_REQ
   public String iREQ_ATA_CD = "SYS-1-1";
   public String iREQ_TASK_CD_SYS = "SYS-REQ-2-NRB";
   public String iREQ_TASK_CD_SYS_MLTP = "SYS-REQ-2-NRBC";

   public String iREQ_ATA_CD_TRK = "ACFT-SYS-1-1-TRK-BATCH-PARENT";
   public String iREQ_TASK_CD_TRK = "DUP-TRK";

   public String iREQ_ATA_CD_ACFT = "ACFT_CD1";
   public String iREQ_TASK_CD_ACFT = "AL_BUILD";

   public String iREQ_ATA_CD_ENG = "ENG-SYS-1-1-TRK-BATCH-PARENT";
   public String iREQ_TASK_CD_ENG_TRK = "REQ4";

   public String iSCHED_THRESHOLD_QT = "100";
   public String iSCHED_DATA_TYPE_CD = "HOURS";
   public String iSCHED_DATA_TYPE_CD_ENG = "EOT";

   // public simpleIDs iTASK_IDs = null;
   // public simpleIDs iTASK_DEFN_IDs = null;
   // public simpleIDs iTASK_IDs_2 = null;
   // public simpleIDs iTASK_DEFN_IDs_2 = null;

   @Rule
   public TestName testName = new TestName();
   // ValidationAndImport ivalidationandimport;


   /**
    * Clean up after each individual test
    */
   @After
   @Override
   public void after() {
      deleteRequiredData();
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
    * This test is to verify OPER-24637: A REQ of type FOLLOW cannot be a part of a block. If a REQ
    * of type FOLLOW is added, validation error code 'CFGBLC-20006' is thrown.
    *
    */

   @Test
   public void testOPER24637_Error20006_TASK_CLASS_FOLLOW_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_BLOCK
      lReqMap.put( "ASSMBL_CD", "'" + iASSEMBLY_CODE + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD + "'" );
      lReqMap.put( "BLOCK_TASK_NAME", "'" + iBLOCK_TASK_NAME + "'" );
      lReqMap.put( "CLASS_CD", "'" + iBLOCK_CLASS_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "LAST_DEADLINE_DRIVER_BOOL", "'" + iLAST_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "TASK_MUST_REMOVE_CD", "'" + iTASK_MUST_REMOVE_CD + "'" );
      // Insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK, lReqMap ) );

      // C_BLOCK_REQ
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iASSEMBLY_CODE + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iFOLLOW_ON_REQ_ATA_CODE + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iFOLLOW_ON_REQ_CODE + "'" );

      createRequirement( TASK_CLASS_CD_FOLLOW, iFOLLOW_ON_REQ_CODE, iFOLLOW_ON_REQ_CODE );

      // Insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_REQ, lReqMap ) );

      // Run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // Validate CFGBLC-20006 error
      validateErrorCode( "CFGBLC-20006" );

   }


   /**
    * This test is to verify OPER-24638: A REQ of type REPREF cannot be a part of a block. If a REQ
    * of class REPREF is added, validation error code 'CFGBLC-20007' is thrown.
    *
    */

   @Test
   public void testOPER24638_Error20007_TASK_CLASS_REPREF_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_BLOCK
      lReqMap.put( "ASSMBL_CD", "'" + iASSEMBLY_CODE + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD + "'" );
      lReqMap.put( "BLOCK_TASK_NAME", "'" + iBLOCK_TASK_NAME + "'" );
      lReqMap.put( "CLASS_CD", "'" + iBLOCK_CLASS_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "LAST_DEADLINE_DRIVER_BOOL", "'" + iLAST_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "TASK_MUST_REMOVE_CD", "'" + iTASK_MUST_REMOVE_CD + "'" );
      // Insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK, lReqMap ) );

      // C_BLOCK_REQ
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iASSEMBLY_CODE + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREPREF_REQ_ATA_CODE + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREPREF_REQ_CODE + "'" );
      createRequirement( TASK_CLASS_CD_REPREF, iREPREF_REQ_CODE, iREPREF_REQ_CODE );

      // Insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_REQ, lReqMap ) );

      // Run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // Validate CFGBLC-20007 error
      validateErrorCode( "CFGBLC-20007" );

   }


   /**
    * This test is to verify OPER-30663:Baseline Loader - Job Card using Class with RSTAT_CD !=0
    * does not get failed by validation
    *
    *
    */

   @Test
   public void testCFGBLC_12225_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "BLOCK_TASK_NAME", "'" + iBLOCK_TASK_NAME_1 + "'" );
      lReqMap.put( "BLOCK_TASK_DESC", "'" + iBLOCK_TASK_DESC_1 + "'" );
      lReqMap.put( "BLOCK_TASK_REF_SDESC", "'" + iBLOCK_TASK_REF_SDESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'AUTOB'" );
      lReqMap.put( "SUBCLASS_CD", "'TEST5'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "LAST_DEADLINE_DRIVER_BOOL", "'" + iLAST_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_DRIVER_BOOL", "'" + iSOFT_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "TASK_MUST_REMOVE_CD", "'" + iTASK_MUST_REMOVE_CD + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK, lReqMap ) );

      // C_BLOCK_REQ
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_TRK + "'" );

      // Insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_REQ, lReqMap ) );

      // c_block_standard_deadline
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_HRS_THRESHOLD", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_STANDARD_DEADLINE, lReqMap ) );

      // Run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      // Validate CFGBLC-20006 error
      checkBLOCKErrorCode( testName.getMethodName(), "CFGBLC-12225" );

   }


   /**
    * This test is to verify CFGBLC-12127:C_BLOCK.manual_scheduling_bool must be Y or N if provided
    *
    *
    */

   @Test
   public void testCFGBLC_12127_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_SYS + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "BLOCK_TASK_NAME", "'" + iBLOCK_TASK_NAME_1 + "'" );
      lReqMap.put( "BLOCK_TASK_DESC", "'" + iBLOCK_TASK_DESC_1 + "'" );
      lReqMap.put( "BLOCK_TASK_REF_SDESC", "'" + iBLOCK_TASK_REF_SDESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "LAST_DEADLINE_DRIVER_BOOL", "'" + iLAST_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_DRIVER_BOOL", "'" + iSOFT_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL_N + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      // lReqMap.put( "TASK_MUST_REMOVE_CD", "'" + iTASK_MUST_REMOVE_CD + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );
      lReqMap.put( "MANUAL_SCHEDULING_BOOL", "'K'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK, lReqMap ) );

      // C_BLOCK_REQ
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_SYS + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_SYS + "'" );

      // Insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_REQ, lReqMap ) );

      // c_block_standard_deadline
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_SYS + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_HRS_THRESHOLD", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_STANDARD_DEADLINE, lReqMap ) );

      // Run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      // Validate CFGBLC-12127 error
      checkBLOCKErrorCode( testName.getMethodName(), "CFGBLC-12127" );

   }


   /**
    * This test is to verify CFGBLC-12144:C_BLOCK.RECURRING_TASK_BOOL must be Y or N.
    *
    *
    */

   @Test
   public void testCFGBLC_12144_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_SYS + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "BLOCK_TASK_NAME", "'" + iBLOCK_TASK_NAME_1 + "'" );
      lReqMap.put( "BLOCK_TASK_DESC", "'" + iBLOCK_TASK_DESC_1 + "'" );
      lReqMap.put( "BLOCK_TASK_REF_SDESC", "'" + iBLOCK_TASK_REF_SDESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "LAST_DEADLINE_DRIVER_BOOL", "'" + iLAST_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_DRIVER_BOOL", "'" + iSOFT_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL_N + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      // lReqMap.put( "TASK_MUST_REMOVE_CD", "'" + iTASK_MUST_REMOVE_CD + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'A'" );
      lReqMap.put( "MANUAL_SCHEDULING_BOOL", "'Y'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK, lReqMap ) );

      // C_BLOCK_REQ
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_SYS + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_SYS + "'" );

      // Insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_REQ, lReqMap ) );

      // c_block_standard_deadline
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_SYS + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_HRS_THRESHOLD", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_STANDARD_DEADLINE, lReqMap ) );

      // Run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      // Validate CFGBLC-12127 error
      checkBLOCKErrorCode( testName.getMethodName(), "CFGBLC-12144" );

   }


   /**
    * This test is to verify CFGBLC-10036:C_BLOCK.RECURRING_TASK_BOOL cannot be NULL or consist
    * entirely of spaces.
    *
    *
    */

   @Test
   public void testCFGBLC_10036_1_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_SYS + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "BLOCK_TASK_NAME", "'" + iBLOCK_TASK_NAME_1 + "'" );
      lReqMap.put( "BLOCK_TASK_DESC", "'" + iBLOCK_TASK_DESC_1 + "'" );
      lReqMap.put( "BLOCK_TASK_REF_SDESC", "'" + iBLOCK_TASK_REF_SDESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "LAST_DEADLINE_DRIVER_BOOL", "'" + iLAST_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_DRIVER_BOOL", "'" + iSOFT_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL_N + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      // lReqMap.put( "TASK_MUST_REMOVE_CD", "'" + iTASK_MUST_REMOVE_CD + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "' '" );
      lReqMap.put( "MANUAL_SCHEDULING_BOOL", "'Y'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK, lReqMap ) );

      // C_BLOCK_REQ
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_SYS + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_SYS + "'" );

      // Insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_REQ, lReqMap ) );

      // c_block_standard_deadline
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_SYS + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_HRS_THRESHOLD", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_STANDARD_DEADLINE, lReqMap ) );

      // Run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      // Validate CFGBLC-12127 error
      checkBLOCKErrorCode( testName.getMethodName(), "CFGBLC-10036" );

   }


   /**
    * This test is to verify CFGBLC-10036:C_BLOCK.RECURRING_TASK_BOOL cannot be NULL or consist
    * entirely of spaces.
    *
    *
    */

   @Test
   public void testCFGBLC_10036_2_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_SYS + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "BLOCK_TASK_NAME", "'" + iBLOCK_TASK_NAME_1 + "'" );
      lReqMap.put( "BLOCK_TASK_DESC", "'" + iBLOCK_TASK_DESC_1 + "'" );
      lReqMap.put( "BLOCK_TASK_REF_SDESC", "'" + iBLOCK_TASK_REF_SDESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "LAST_DEADLINE_DRIVER_BOOL", "'" + iLAST_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_DRIVER_BOOL", "'" + iSOFT_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL_N + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      // lReqMap.put( "TASK_MUST_REMOVE_CD", "'" + iTASK_MUST_REMOVE_CD + "'" );
      // lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );
      lReqMap.put( "MANUAL_SCHEDULING_BOOL", "'Y'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK, lReqMap ) );

      // C_BLOCK_REQ
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_SYS + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_SYS + "'" );

      // Insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_REQ, lReqMap ) );

      // c_block_standard_deadline
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_SYS + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_HRS_THRESHOLD", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_STANDARD_DEADLINE, lReqMap ) );

      // Run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      // Validate CFGBLC-12127 error
      checkBLOCKErrorCode( testName.getMethodName(), "CFGBLC-10036" );

   }


   /**
    * This test is to verify CFGBLC-10035:C_BLOCK_DYNAMIC_PART_DEADLINE the part_no_oem/manufact_cd
    * combination does not exist in Maintenix.
    *
    *
    */
   @Test
   public void testCFGBLC_10035_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "BLOCK_TASK_NAME", "'" + iBLOCK_TASK_NAME_1 + "'" );
      lReqMap.put( "BLOCK_TASK_DESC", "'" + iBLOCK_TASK_DESC_1 + "'" );
      lReqMap.put( "BLOCK_TASK_REF_SDESC", "'" + iBLOCK_TASK_REF_SDESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "LAST_DEADLINE_DRIVER_BOOL", "'" + iLAST_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_DRIVER_BOOL", "'" + iSOFT_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "TASK_MUST_REMOVE_CD", "'" + iTASK_MUST_REMOVE_CD + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK, lReqMap ) );

      // C_BLOCK_DYNAMIC_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_THRESHOLD_QT", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_DYNAMIC_DEADLINE, lReqMap ) );

      // C_BLOCK_DYNAMIC_PART_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'A0000010'" );
      lReqMap.put( "MANUFACT_CD", "'1234567890'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_THRESHOLD_QT", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_DYNAMIC_PART_DEADLINE, lReqMap ) );

      // Run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      // Validate CFGBLC-12127 error
      checkBLOCKErrorCode( testName.getMethodName(), "CFGBLC-10035" );

   }


   /**
    * This test is to verify validation functionality on block_import with on sys slot with req and
    *
    */

   @Test
   public void testBLOCKSYS_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_SYS + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "BLOCK_TASK_NAME", "'" + iBLOCK_TASK_NAME_1 + "'" );
      lReqMap.put( "BLOCK_TASK_DESC", "'" + iBLOCK_TASK_DESC_1 + "'" );
      lReqMap.put( "BLOCK_TASK_REF_SDESC", "'" + iBLOCK_TASK_REF_SDESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "LAST_DEADLINE_DRIVER_BOOL", "'" + iLAST_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_DRIVER_BOOL", "'" + iSOFT_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL_N + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      // lReqMap.put( "TASK_MUST_REMOVE_CD", "'" + iTASK_MUST_REMOVE_CD + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK, lReqMap ) );

      // C_BLOCK_REQ
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_SYS + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_SYS + "'" );

      // Insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_REQ, lReqMap ) );

      // c_block_standard_deadline
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_SYS + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_HRS_THRESHOLD", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_STANDARD_DEADLINE, lReqMap ) );

      // Run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify import functionality on block_import with on sys slot with req and
    *
    */
   @Test
   public void testBLOCKSYS_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testBLOCKSYS_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs = null;
      iTASK_DEFN_IDs = null;

      // Verify task_task table
      iTASK_IDs = getTaskIds( iBLOCK_TASK_CD_1, iBLOCK_TASK_NAME_1 );
      assmbleInfor lassmblinfor = getassmblInfor( iBLOCK_ATA_CD_SYS );

      iTASK_DEFN_IDs = verifyTaskTask( iTASK_IDs, iCLASS_CD, lassmblinfor, iSUBCLASS_CD,
            iORIGINATOR_CD, iBLOCK_TASK_CD_1, iBLOCK_TASK_NAME_1, iBLOCK_TASK_DESC_1,
            iBLOCK_TASK_REF_SDESC_1, iLAST_DEADLINE_DRIVER_BOOL_N, iSOFT_DEADLINE_DRIVER_BOOL_N,
            null, iON_CONDITION_BOOL_N_number );

      // verify task_DEFN
      String lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTASK_DEFN_IDs.getNO_DB_ID() + " and TASK_DEFN_ID=" + iTASK_DEFN_IDs.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_work_type
      // check work list 1
      lQuery = "select 1 from " + TableUtil.TASK_WORK_TYPE + " where TASK_DB_ID="
            + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID()
            + " and WORK_TYPE_CD='" + iWORK_TYPE_LIST_1 + "'";
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // check work list 2
      lQuery = "select 1 from " + TableUtil.TASK_WORK_TYPE + " where TASK_DB_ID="
            + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID()
            + " and WORK_TYPE_CD='" + iWORK_TYPE_LIST_2 + "'";
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_sched_rule table
      simpleIDs lTypeIds = getTypeIds( iSCHED_DATA_TYPE_CD );
      verifyTASKSCHEDRULE( iTASK_IDs, lTypeIds, iSCHED_THRESHOLD_QT, null );

      // verify TASK_BLOCK_REQ_MAP
      lQuery =
            "select TASK_DEFN_DB_ID, TASK_DEFN_ID from task_task " + "inner join eqp_assmbl_bom on "
                  + "eqp_assmbl_bom.assmbl_db_id=task_task.assmbl_db_id and "
                  + "eqp_assmbl_bom.assmbl_cd=task_task.assmbl_cd and "
                  + "eqp_assmbl_bom.assmbl_bom_id = task_task.assmbl_bom_id "
                  + "where task_task.task_cd='" + iREQ_TASK_CD_SYS
                  + "' and eqp_assmbl_bom.assmbl_bom_cd='" + iREQ_ATA_CD + "'";
      simpleIDs lREQDefnIDs = getREQDEFNIDs( lQuery );

      verifyTASKBLOCKREQMAP( iTASK_IDs, lREQDefnIDs, "1", null );

   }


   /**
    * This test is to verify validation functionality on block_import with on trk slot with req and
    *
    */

   @Test
   public void testBLOCKTRK_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "BLOCK_TASK_NAME", "'" + iBLOCK_TASK_NAME_1 + "'" );
      lReqMap.put( "BLOCK_TASK_DESC", "'" + iBLOCK_TASK_DESC_1 + "'" );
      lReqMap.put( "BLOCK_TASK_REF_SDESC", "'" + iBLOCK_TASK_REF_SDESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "LAST_DEADLINE_DRIVER_BOOL", "'" + iLAST_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_DRIVER_BOOL", "'" + iSOFT_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "TASK_MUST_REMOVE_CD", "'" + iTASK_MUST_REMOVE_CD + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'F'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK, lReqMap ) );

      // C_BLOCK_REQ
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_TRK + "'" );

      // Insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_REQ, lReqMap ) );

      // c_block_standard_deadline
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_HRS_THRESHOLD", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_STANDARD_DEADLINE, lReqMap ) );

      // Run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify import functionality on block_import with on trk slot with req and
    *
    */
   @Test
   public void testBLOCKTRK_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testBLOCKTRK_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs = null;
      iTASK_DEFN_IDs = null;

      // Verify task_task table
      iTASK_IDs = getTaskIds( iBLOCK_TASK_CD_1, iBLOCK_TASK_NAME_1 );
      assmbleInfor lassmblinfor = getassmblInfor( iBLOCK_ATA_CD_TRK );

      iTASK_DEFN_IDs = verifyTaskTask( iTASK_IDs, iCLASS_CD, lassmblinfor, iSUBCLASS_CD,
            iORIGINATOR_CD, iBLOCK_TASK_CD_1, iBLOCK_TASK_NAME_1, iBLOCK_TASK_DESC_1,
            iBLOCK_TASK_REF_SDESC_1, iLAST_DEADLINE_DRIVER_BOOL_N, iSOFT_DEADLINE_DRIVER_BOOL_N,
            iTASK_MUST_REMOVE_CD, iON_CONDITION_BOOL_Y_number );

      // verify task_DEFN
      String lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTASK_DEFN_IDs.getNO_DB_ID() + " and TASK_DEFN_ID=" + iTASK_DEFN_IDs.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_work_type
      // check work list 1
      lQuery = "select 1 from " + TableUtil.TASK_WORK_TYPE + " where TASK_DB_ID="
            + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID()
            + " and WORK_TYPE_CD='" + iWORK_TYPE_LIST_1 + "'";
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // check work list 2
      lQuery = "select 1 from " + TableUtil.TASK_WORK_TYPE + " where TASK_DB_ID="
            + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID()
            + " and WORK_TYPE_CD='" + iWORK_TYPE_LIST_2 + "'";
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_sched_rule table
      simpleIDs lTypeIds = getTypeIds( iSCHED_DATA_TYPE_CD );
      verifyTASKSCHEDRULE( iTASK_IDs, lTypeIds, iSCHED_THRESHOLD_QT, null );

      // verify TASK_BLOCK_REQ_MAP
      lQuery =
            "select TASK_DEFN_DB_ID, TASK_DEFN_ID from task_task " + "inner join eqp_assmbl_bom on "
                  + "eqp_assmbl_bom.assmbl_db_id=task_task.assmbl_db_id and "
                  + "eqp_assmbl_bom.assmbl_cd=task_task.assmbl_cd and "
                  + "eqp_assmbl_bom.assmbl_bom_id = task_task.assmbl_bom_id "
                  + "where task_task.task_cd='" + iREQ_TASK_CD_TRK
                  + "' and eqp_assmbl_bom.assmbl_bom_cd='" + iREQ_ATA_CD_TRK + "'";
      simpleIDs lREQDefnIDs = getREQDEFNIDs( lQuery );

      verifyTASKBLOCKREQMAP( iTASK_IDs, lREQDefnIDs, "1", null );

   }


   /**
    * This test is to verify validation functionality on block_import with on root/acft slot with
    * req and
    *
    */

   @Test
   public void testBLOCKACFT_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_ACFT + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "BLOCK_TASK_NAME", "'" + iBLOCK_TASK_NAME_1 + "'" );
      lReqMap.put( "BLOCK_TASK_DESC", "'" + iBLOCK_TASK_DESC_1 + "'" );
      lReqMap.put( "BLOCK_TASK_REF_SDESC", "'" + iBLOCK_TASK_REF_SDESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "LAST_DEADLINE_DRIVER_BOOL", "'" + iLAST_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_DRIVER_BOOL", "'" + iSOFT_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL_N + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      // lReqMap.put( "TASK_MUST_REMOVE_CD", "'" + iTASK_MUST_REMOVE_CD + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'0'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK, lReqMap ) );

      // C_BLOCK_REQ
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_ACFT + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_ACFT + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_ACFT + "'" );

      // Insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_REQ, lReqMap ) );

      // c_block_standard_deadline
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_ACFT + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_HRS_THRESHOLD", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_STANDARD_DEADLINE, lReqMap ) );

      // Run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify validation functionality on block_import with on trk slot with req and
    *
    */
   @Test
   public void testBLOCKACFT_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testBLOCKACFT_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs = null;
      iTASK_DEFN_IDs = null;

      // Verify task_task table
      iTASK_IDs = getTaskIds( iBLOCK_TASK_CD_1, iBLOCK_TASK_NAME_1 );
      assmbleInfor lassmblinfor = getassmblInfor( iBLOCK_ATA_CD_ACFT );

      iTASK_DEFN_IDs = verifyTaskTask( iTASK_IDs, iCLASS_CD, lassmblinfor, iSUBCLASS_CD,
            iORIGINATOR_CD, iBLOCK_TASK_CD_1, iBLOCK_TASK_NAME_1, iBLOCK_TASK_DESC_1,
            iBLOCK_TASK_REF_SDESC_1, iLAST_DEADLINE_DRIVER_BOOL_N, iSOFT_DEADLINE_DRIVER_BOOL_N,
            null, iON_CONDITION_BOOL_N_number );

      // verify task_DEFN
      String lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTASK_DEFN_IDs.getNO_DB_ID() + " and TASK_DEFN_ID=" + iTASK_DEFN_IDs.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_work_type
      // check work list 1
      lQuery = "select 1 from " + TableUtil.TASK_WORK_TYPE + " where TASK_DB_ID="
            + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID()
            + " and WORK_TYPE_CD='" + iWORK_TYPE_LIST_1 + "'";
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // check work list 2
      lQuery = "select 1 from " + TableUtil.TASK_WORK_TYPE + " where TASK_DB_ID="
            + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID()
            + " and WORK_TYPE_CD='" + iWORK_TYPE_LIST_2 + "'";
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_sched_rule table
      simpleIDs lTypeIds = getTypeIds( iSCHED_DATA_TYPE_CD );
      verifyTASKSCHEDRULE( iTASK_IDs, lTypeIds, iSCHED_THRESHOLD_QT, null );

      // verify TASK_BLOCK_REQ_MAP
      lQuery =
            "select TASK_DEFN_DB_ID, TASK_DEFN_ID from task_task " + "inner join eqp_assmbl_bom on "
                  + "eqp_assmbl_bom.assmbl_db_id=task_task.assmbl_db_id and "
                  + "eqp_assmbl_bom.assmbl_cd=task_task.assmbl_cd and "
                  + "eqp_assmbl_bom.assmbl_bom_id = task_task.assmbl_bom_id "
                  + "where task_task.task_cd='" + iREQ_TASK_CD_ACFT
                  + "' and eqp_assmbl_bom.assmbl_bom_cd='" + iREQ_ATA_CD_ACFT + "'";
      simpleIDs lREQDefnIDs = getREQDEFNIDs( lQuery );

      verifyTASKBLOCKREQMAP( iTASK_IDs, lREQDefnIDs, "1", null );

   }


   /**
    * This test is to verify validation functionality on block_import with on root/eng slot with req
    * and
    *
    */

   @Test
   public void testBLOCKENGTRK_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_ENG_TRK + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_2 + "'" );
      lReqMap.put( "BLOCK_TASK_NAME", "'" + iBLOCK_TASK_NAME_2 + "'" );
      lReqMap.put( "BLOCK_TASK_DESC", "'" + iBLOCK_TASK_DESC_2 + "'" );
      lReqMap.put( "BLOCK_TASK_REF_SDESC", "'" + iBLOCK_TASK_REF_SDESC_2 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "LAST_DEADLINE_DRIVER_BOOL", "'" + iLAST_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_DRIVER_BOOL", "'" + iSOFT_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "TASK_MUST_REMOVE_CD", "'" + iTASK_MUST_REMOVE_CD + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK, lReqMap ) );

      // C_BLOCK_REQ
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_ENG_TRK + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_2 + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_ENG + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_ENG_TRK + "'" );

      // Insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_REQ, lReqMap ) );

      // c_block_standard_deadline
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_ENG_TRK + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_2 + "'" );
      // lReqMap.put( "SCHED_HRS_THRESHOLD", "'" + iSCHED_THRESHOLD_QT + "'" );
      lReqMap.put( "SCHED_EOT_THRESHOLD", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_STANDARD_DEADLINE, lReqMap ) );

      // Run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify import functionality on block_import with on root/eng slot with req and
    *
    */

   @Test
   public void testBLOCKENGTRK_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testBLOCKENGTRK_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs = null;
      iTASK_DEFN_IDs = null;

      // Verify task_task table
      iTASK_IDs = getTaskIds( iBLOCK_TASK_CD_2, iBLOCK_TASK_NAME_2 );
      assmbleInfor lassmblinfor = getassmblInfor( iBLOCK_ATA_CD_ENG_TRK );

      iTASK_DEFN_IDs = verifyTaskTask( iTASK_IDs, iCLASS_CD, lassmblinfor, iSUBCLASS_CD,
            iORIGINATOR_CD, iBLOCK_TASK_CD_2, iBLOCK_TASK_NAME_2, iBLOCK_TASK_DESC_2,
            iBLOCK_TASK_REF_SDESC_2, iLAST_DEADLINE_DRIVER_BOOL_N, iSOFT_DEADLINE_DRIVER_BOOL_N,
            iTASK_MUST_REMOVE_CD, iON_CONDITION_BOOL_Y_number );

      // verify task_DEFN
      String lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTASK_DEFN_IDs.getNO_DB_ID() + " and TASK_DEFN_ID=" + iTASK_DEFN_IDs.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_work_type
      // check work list 1
      lQuery = "select 1 from " + TableUtil.TASK_WORK_TYPE + " where TASK_DB_ID="
            + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID()
            + " and WORK_TYPE_CD='" + iWORK_TYPE_LIST_1 + "'";
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // check work list 2
      lQuery = "select 1 from " + TableUtil.TASK_WORK_TYPE + " where TASK_DB_ID="
            + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID()
            + " and WORK_TYPE_CD='" + iWORK_TYPE_LIST_2 + "'";
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_sched_rule table
      simpleIDs lTypeIds = getTypeIds( iSCHED_DATA_TYPE_CD_ENG );
      verifyTASKSCHEDRULE( iTASK_IDs, lTypeIds, iSCHED_THRESHOLD_QT, null );

      // verify TASK_BLOCK_REQ_MAP
      lQuery =
            "select TASK_DEFN_DB_ID, TASK_DEFN_ID from task_task " + "inner join eqp_assmbl_bom on "
                  + "eqp_assmbl_bom.assmbl_db_id=task_task.assmbl_db_id and "
                  + "eqp_assmbl_bom.assmbl_cd=task_task.assmbl_cd and "
                  + "eqp_assmbl_bom.assmbl_bom_id = task_task.assmbl_bom_id "
                  + "where task_task.task_cd='" + iREQ_TASK_CD_ENG_TRK
                  + "' and eqp_assmbl_bom.assmbl_bom_cd='" + iREQ_ATA_CD_ENG + "'";
      simpleIDs lREQDefnIDs = getREQDEFNIDs( lQuery );

      verifyTASKBLOCKREQMAP( iTASK_IDs, lREQDefnIDs, "1", null );

   }


   /**
    * This test is to verify validation functionality on block_import with with multiple blocks
    *
    */

   @Test
   public void testBLOCKMultipleRecords_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testBLOCKSYS_VALIDATION();
      testBLOCKENGTRK_VALIDATION();

   }


   /**
    * This test is to verify import functionality on block_import with with multiple blocks
    *
    */

   @Test
   public void testBLOCKMultipleRecords_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testBLOCKMultipleRecords_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs = null;
      iTASK_DEFN_IDs = null;
      iTASK_IDs_2 = null;
      iTASK_DEFN_IDs_2 = null;

      // Verify
      // acft_sys==============================================================================
      // Verify task_task table
      iTASK_IDs = getTaskIds( iBLOCK_TASK_CD_1, iBLOCK_TASK_NAME_1 );
      assmbleInfor lassmblinfor = getassmblInfor( iBLOCK_ATA_CD_SYS );

      iTASK_DEFN_IDs = verifyTaskTask( iTASK_IDs, iCLASS_CD, lassmblinfor, iSUBCLASS_CD,
            iORIGINATOR_CD, iBLOCK_TASK_CD_1, iBLOCK_TASK_NAME_1, iBLOCK_TASK_DESC_1,
            iBLOCK_TASK_REF_SDESC_1, iLAST_DEADLINE_DRIVER_BOOL_N, iSOFT_DEADLINE_DRIVER_BOOL_N,
            null, iON_CONDITION_BOOL_N_number );

      // verify task_DEFN
      String lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTASK_DEFN_IDs.getNO_DB_ID() + " and TASK_DEFN_ID=" + iTASK_DEFN_IDs.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_work_type
      // check work list 1
      lQuery = "select 1 from " + TableUtil.TASK_WORK_TYPE + " where TASK_DB_ID="
            + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID()
            + " and WORK_TYPE_CD='" + iWORK_TYPE_LIST_1 + "'";
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // check work list 2
      lQuery = "select 1 from " + TableUtil.TASK_WORK_TYPE + " where TASK_DB_ID="
            + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID()
            + " and WORK_TYPE_CD='" + iWORK_TYPE_LIST_2 + "'";
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_sched_rule table
      simpleIDs lTypeIds = getTypeIds( iSCHED_DATA_TYPE_CD );
      verifyTASKSCHEDRULE( iTASK_IDs, lTypeIds, iSCHED_THRESHOLD_QT, null );

      // verify TASK_BLOCK_REQ_MAP
      lQuery =
            "select TASK_DEFN_DB_ID, TASK_DEFN_ID from task_task " + "inner join eqp_assmbl_bom on "
                  + "eqp_assmbl_bom.assmbl_db_id=task_task.assmbl_db_id and "
                  + "eqp_assmbl_bom.assmbl_cd=task_task.assmbl_cd and "
                  + "eqp_assmbl_bom.assmbl_bom_id = task_task.assmbl_bom_id "
                  + "where task_task.task_cd='" + iREQ_TASK_CD_SYS
                  + "' and eqp_assmbl_bom.assmbl_bom_cd='" + iREQ_ATA_CD + "'";
      simpleIDs lREQDefnIDs = getREQDEFNIDs( lQuery );

      verifyTASKBLOCKREQMAP( iTASK_IDs, lREQDefnIDs, "1", null );

      // Verify ENG TRK===================================================================
      // Verify task_task table
      iTASK_IDs_2 = getTaskIds( iBLOCK_TASK_CD_2, iBLOCK_TASK_NAME_2 );
      lassmblinfor = getassmblInfor( iBLOCK_ATA_CD_ENG_TRK );

      iTASK_DEFN_IDs_2 = verifyTaskTask( iTASK_IDs_2, iCLASS_CD, lassmblinfor, iSUBCLASS_CD,
            iORIGINATOR_CD, iBLOCK_TASK_CD_2, iBLOCK_TASK_NAME_2, iBLOCK_TASK_DESC_2,
            iBLOCK_TASK_REF_SDESC_2, iLAST_DEADLINE_DRIVER_BOOL_N, iSOFT_DEADLINE_DRIVER_BOOL_N,
            iTASK_MUST_REMOVE_CD, iON_CONDITION_BOOL_Y_number );

      // verify task_DEFN
      lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTASK_DEFN_IDs_2.getNO_DB_ID() + " and TASK_DEFN_ID=" + iTASK_DEFN_IDs_2.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_work_type
      // check work list 1
      lQuery = "select 1 from " + TableUtil.TASK_WORK_TYPE + " where TASK_DB_ID="
            + iTASK_IDs_2.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_2.getNO_ID()
            + " and WORK_TYPE_CD='" + iWORK_TYPE_LIST_1 + "'";
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // check work list 2
      lQuery = "select 1 from " + TableUtil.TASK_WORK_TYPE + " where TASK_DB_ID="
            + iTASK_IDs_2.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_2.getNO_ID()
            + " and WORK_TYPE_CD='" + iWORK_TYPE_LIST_2 + "'";
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_sched_rule table
      lTypeIds = getTypeIds( iSCHED_DATA_TYPE_CD_ENG );
      verifyTASKSCHEDRULE( iTASK_IDs_2, lTypeIds, iSCHED_THRESHOLD_QT, null );

      // verify TASK_BLOCK_REQ_MAP
      lQuery =
            "select TASK_DEFN_DB_ID, TASK_DEFN_ID from task_task " + "inner join eqp_assmbl_bom on "
                  + "eqp_assmbl_bom.assmbl_db_id=task_task.assmbl_db_id and "
                  + "eqp_assmbl_bom.assmbl_cd=task_task.assmbl_cd and "
                  + "eqp_assmbl_bom.assmbl_bom_id = task_task.assmbl_bom_id "
                  + "where task_task.task_cd='" + iREQ_TASK_CD_ENG_TRK
                  + "' and eqp_assmbl_bom.assmbl_bom_cd='" + iREQ_ATA_CD_ENG + "'";
      lREQDefnIDs = getREQDEFNIDs( lQuery );

      verifyTASKBLOCKREQMAP( iTASK_IDs_2, lREQDefnIDs, "1", null );

   }


   /**
    * This test is to verify validation functionality on block_import with on sys slot with more
    * than one req
    *
    */

   @Test
   public void testBLOCKMultipleREQS_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_SYS + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "BLOCK_TASK_NAME", "'" + iBLOCK_TASK_NAME_1 + "'" );
      lReqMap.put( "BLOCK_TASK_DESC", "'" + iBLOCK_TASK_DESC_1 + "'" );
      lReqMap.put( "BLOCK_TASK_REF_SDESC", "'" + iBLOCK_TASK_REF_SDESC_1 + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lReqMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "LAST_DEADLINE_DRIVER_BOOL", "'" + iLAST_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_DRIVER_BOOL", "'" + iSOFT_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL_N + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      // lReqMap.put( "TASK_MUST_REMOVE_CD", "'" + iTASK_MUST_REMOVE_CD + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK, lReqMap ) );

      // C_BLOCK_REQ
      // first req
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_SYS + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_SYS + "'" );

      // Insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_REQ, lReqMap ) );

      // second req
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_SYS + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_SYS_MLTP + "'" );

      // Insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_REQ, lReqMap ) );

      // c_block_standard_deadline
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_SYS + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_HRS_THRESHOLD", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_STANDARD_DEADLINE, lReqMap ) );

      // Run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify import functionality on block_import with on sys slot with more than
    * one req
    *
    */

   @Test
   public void testBLOCKMultipleREQS_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testBLOCKMultipleREQS_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs = null;
      iTASK_DEFN_IDs = null;

      // Verify task_task table
      iTASK_IDs = getTaskIds( iBLOCK_TASK_CD_1, iBLOCK_TASK_NAME_1 );
      assmbleInfor lassmblinfor = getassmblInfor( iBLOCK_ATA_CD_SYS );

      iTASK_DEFN_IDs = verifyTaskTask( iTASK_IDs, iCLASS_CD, lassmblinfor, iSUBCLASS_CD,
            iORIGINATOR_CD, iBLOCK_TASK_CD_1, iBLOCK_TASK_NAME_1, iBLOCK_TASK_DESC_1,
            iBLOCK_TASK_REF_SDESC_1, iLAST_DEADLINE_DRIVER_BOOL_N, iSOFT_DEADLINE_DRIVER_BOOL_N,
            null, iON_CONDITION_BOOL_N_number );

      // verify task_DEFN
      String lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTASK_DEFN_IDs.getNO_DB_ID() + " and TASK_DEFN_ID=" + iTASK_DEFN_IDs.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_work_type
      // check work list 1
      lQuery = "select 1 from " + TableUtil.TASK_WORK_TYPE + " where TASK_DB_ID="
            + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID()
            + " and WORK_TYPE_CD='" + iWORK_TYPE_LIST_1 + "'";
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // check work list 2
      lQuery = "select 1 from " + TableUtil.TASK_WORK_TYPE + " where TASK_DB_ID="
            + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID()
            + " and WORK_TYPE_CD='" + iWORK_TYPE_LIST_2 + "'";
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_sched_rule table
      simpleIDs lTypeIds = getTypeIds( iSCHED_DATA_TYPE_CD );
      verifyTASKSCHEDRULE( iTASK_IDs, lTypeIds, iSCHED_THRESHOLD_QT, null );

      // verify TASK_BLOCK_REQ_MAP
      // first req
      lQuery =
            "select TASK_DEFN_DB_ID, TASK_DEFN_ID from task_task " + "inner join eqp_assmbl_bom on "
                  + "eqp_assmbl_bom.assmbl_db_id=task_task.assmbl_db_id and "
                  + "eqp_assmbl_bom.assmbl_cd=task_task.assmbl_cd and "
                  + "eqp_assmbl_bom.assmbl_bom_id = task_task.assmbl_bom_id "
                  + "where task_task.task_cd='" + iREQ_TASK_CD_SYS
                  + "' and eqp_assmbl_bom.assmbl_bom_cd='" + iREQ_ATA_CD + "'";
      simpleIDs lREQDefnIDs = getREQDEFNIDs( lQuery );

      verifyTASKBLOCKREQMAP( iTASK_IDs, lREQDefnIDs, "1", null );

      // second req
      lQuery =
            "select TASK_DEFN_DB_ID, TASK_DEFN_ID from task_task " + "inner join eqp_assmbl_bom on "
                  + "eqp_assmbl_bom.assmbl_db_id=task_task.assmbl_db_id and "
                  + "eqp_assmbl_bom.assmbl_cd=task_task.assmbl_cd and "
                  + "eqp_assmbl_bom.assmbl_bom_id = task_task.assmbl_bom_id "
                  + "where task_task.task_cd='" + iREQ_TASK_CD_SYS_MLTP
                  + "' and eqp_assmbl_bom.assmbl_bom_cd='" + iREQ_ATA_CD + "'";
      lREQDefnIDs = getREQDEFNIDs( lQuery );

      verifyTASKBLOCKREQMAP( iTASK_IDs, lREQDefnIDs, "1", null );

   }


   /**
    * This test is to verify export functionality on block_import with on sys slot with req and
    *
    * @throws SQLException
    *
    */
   @Test
   public void testBLOCKSYS_EXPORT() throws SQLException {

      System.out.println(
            "=======Starting:  " + testName.getMethodName() + " ========================" );

      testBLOCKSYS_IMPORT();
      clearBaselineLoaderTables();
      runExport();

      // Verify C_BLOCK table
      String lQuery = "select 1 from " + TableUtil.C_BLOCK + " where BLOCK_TASK_CD='"
            + iBLOCK_TASK_CD_1 + "' and RECURRING_TASK_BOOL='" + "N" + "'";
      Assert.assertTrue( "Check C_BLOCK table to verify the record exists",
            RecordsExist( lQuery ) );

      // Verify C_BLOCK_REQ
      lQuery = "select 1 from " + TableUtil.C_BLOCK_REQ + " where BLOCK_TASK_CD='"
            + iBLOCK_TASK_CD_1 + "' and REQ_TASK_CD='" + iREQ_TASK_CD_SYS + "'";
      Assert.assertTrue( "Check C_BLOCK_REQ table to verify the record exists",
            RecordsExist( lQuery ) );

      // Verify C_BLOCK_STANDARD_DEADLINE
      lQuery = "select 1 from " + TableUtil.C_BLOCK_DYNAMIC_DEADLINE + " where BLOCK_TASK_CD='"
            + iBLOCK_TASK_CD_1 + "' and SCHED_THRESHOLD_QT='" + iSCHED_THRESHOLD_QT + "'";
      Assert.assertTrue( "Check C_BLOCK_STANDARD_DEADLINE table to verify the record exists",
            RecordsExist( lQuery ) );
   }


   // ======================================================================================

   /**
    * This function is to validate error code exists.
    */
   private void validateErrorCode( String aCode ) {

      List<String> llist = getErrorCodeList();
      String lerror_desc = getErrorDetail( aCode );
      Assert.assertTrue( "Error code found " + aCode + ": " + lerror_desc,
            llist.contains( aCode ) );

   }


   /**
    * This function is to retrieve errors code list
    */
   private List<String> getErrorCodeList() {
      String[] iIds = { "RESULT_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      String iQueryString = "select c_block.result_cd " + " from c_block "
            + " inner join DL_REF_MESSAGE on " + " DL_REF_MESSAGE.result_cd=c_block.result_cd "
            + " UNION ALL " + " select c_block_req.result_cd "
            + " from c_block_req inner join DL_REF_MESSAGE on "
            + " DL_REF_MESSAGE.result_cd=c_block_req.result_cd ";
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      List<String> llistM = new ArrayList<String>();
      for ( int i = 0; i < llists.size(); i++ ) {
         llistM.add( llists.get( i ).get( 0 ) );
      }

      return llistM;
   }


   /**
    * This function is to get detail of error code
    */
   private String getErrorDetail( String aErrorcode ) {

      String[] iIds = { "TECH_DESC" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();

      lArgs.addArguments( "RESULT_CD", aErrorcode );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.DL_REF_MESSAGE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      return llists.get( 0 ).get( 0 );

   }


   /**
    *
    * This method creates a follow-on requirement in task_task.
    *
    */
   private void createRequirement( String aTaskClassCode, String aTaskCode, String aTaskName ) {

      String iQueryString = null;

      // Create necessary task definitions
      runSqlsInTable( createTaskDefinition );

      // Get new task definition IDs
      iTaskDefinitionIDs = getNewTaskDefinitionIDs();
      iDeleteRequirementIDs.add( iTaskDefinitionIDs );

      // Construct task_task sql string and create new tasks
      String lquery =
            "select ASSMBL_BOM_ID from eqp_bom_part where ASSMBL_CD='ACFT_CD1' and bom_part_cd='ACFT_CD1'";
      String lId = getStringValueFromQuery( lquery, "ASSMBL_BOM_ID" );

      iQueryString =
            "insert into TASK_TASK (TASK_DB_ID, TASK_ID, TASK_DEFN_DB_ID, TASK_DEFN_ID, TASK_CLASS_DB_ID, TASK_CLASS_CD, ASSMBL_DB_ID, "
                  + "ASSMBL_CD, ASSMBL_BOM_ID, TASK_DEF_STATUS_DB_ID, TASK_DEF_STATUS_CD,TASK_CD, TASK_NAME)"
                  + " values (" + CONS_DB_ID + ", TASK_ID_SEQ.nextval, "
                  + iTaskDefinitionIDs.getNO_DB_ID() + ", " + iTaskDefinitionIDs.getNO_ID()
                  + ", 0, '" + aTaskClassCode + "', " + CONS_DB_ID + ", 'ACFT_CD1', " + lId
                  + ", 0, 'ACTV', '" + aTaskCode + "', '" + aTaskName + "')";
      executeSQL( iQueryString );
      iDeleteRequirementCodes.add( aTaskCode );
   }


   /**
    * This method creates a task definition in task_defn.
    */
   private ArrayList<String> createTaskDefinition = new ArrayList<String>();
   {
      createTaskDefinition.add(
            "insert into TASK_DEFN (TASK_DEFN_DB_ID, TASK_DEFN_ID, LAST_REVISION_ORD, NEW_REVISION_BOOL) values (4650, TASK_DEFN_ID.nextval, 1, 1)" );
   };


   /**
    *
    * This method deletes the required data.
    *
    */
   private void deleteRequiredData() {
      String lStrDelete;
      // Delete task_task table
      for ( String aTaskCode : iDeleteRequirementCodes ) {
         lStrDelete = "delete from TASK_TASK where TASK_CD like '%" + aTaskCode + "%'";
         executeSQL( lStrDelete );
      }

      // Delete task_defn table
      for ( simpleIDs aSimpleID : iDeleteRequirementIDs ) {
         lStrDelete = "delete from TASK_DEFN where TASK_DEFN_DB_ID=" + aSimpleID.getNO_DB_ID()
               + " and TASK_DEFN_ID=" + aSimpleID.getNO_ID();
         executeSQL( lStrDelete );
      }
      iDeleteRequirementIDs.clear();
      iDeleteRequirementCodes.clear();

      if ( iTASK_IDs != null ) {

         // delete task_sched_rule
         lStrDelete = "delete from " + TableUtil.TASK_SCHED_RULE + "  where TASK_DB_ID="
               + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_work_type
         lStrDelete = "delete from " + TableUtil.TASK_WORK_TYPE + "  where TASK_DB_ID="
               + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_task_dep
         lStrDelete = "delete from " + TableUtil.TASK_TASK_DEP + "  where TASK_DB_ID="
               + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_block_req_map
         lStrDelete = "delete from " + TableUtil.TASK_BLOCK_REQ_MAP + "  where BLOCK_TASK_DB_ID="
               + iTASK_IDs.getNO_DB_ID() + " and BLOCK_TASK_ID=" + iTASK_IDs.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_sched_rule
         lStrDelete = "delete from " + TableUtil.TASK_SCHED_RULE + "  where TASK_DB_ID="
               + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_interval
         lStrDelete = "delete from " + TableUtil.TASK_INTERVAL + "  where TASK_DB_ID="
               + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_task
         lStrDelete = "delete from " + TableUtil.TASK_TASK + "  where TASK_DB_ID="
               + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID();
         executeSQL( lStrDelete );

      }

      if ( iTASK_DEFN_IDs != null ) {

         lStrDelete = "delete from TASK_DEFN where TASK_DEFN_DB_ID=" + iTASK_DEFN_IDs.getNO_DB_ID()
               + " and TASK_DEFN_ID=" + iTASK_DEFN_IDs.getNO_ID();
         executeSQL( lStrDelete );

      }

      if ( iTASK_IDs_2 != null ) {

         // delete task_sched_rule
         lStrDelete = "delete from " + TableUtil.TASK_SCHED_RULE + "  where TASK_DB_ID="
               + iTASK_IDs_2.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_2.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_work_type
         lStrDelete = "delete from " + TableUtil.TASK_WORK_TYPE + "  where TASK_DB_ID="
               + iTASK_IDs_2.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_2.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_task_dep
         lStrDelete = "delete from " + TableUtil.TASK_TASK_DEP + "  where TASK_DB_ID="
               + iTASK_IDs_2.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_2.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_block_req_map
         lStrDelete = "delete from " + TableUtil.TASK_BLOCK_REQ_MAP + "  where BLOCK_TASK_DB_ID="
               + iTASK_IDs_2.getNO_DB_ID() + " and BLOCK_TASK_ID=" + iTASK_IDs_2.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_sched_rule
         lStrDelete = "delete from " + TableUtil.TASK_SCHED_RULE + "  where TASK_DB_ID="
               + iTASK_IDs_2.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_2.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_interval
         lStrDelete = "delete from " + TableUtil.TASK_INTERVAL + "  where TASK_DB_ID="
               + iTASK_IDs_2.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_2.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_task
         lStrDelete = "delete from " + TableUtil.TASK_TASK + "  where TASK_DB_ID="
               + iTASK_IDs_2.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_2.getNO_ID();
         executeSQL( lStrDelete );

      }

      if ( iTASK_DEFN_IDs_2 != null ) {

         lStrDelete =
               "delete from TASK_DEFN where TASK_DEFN_DB_ID=" + iTASK_DEFN_IDs_2.getNO_DB_ID()
                     + " and TASK_DEFN_ID=" + iTASK_DEFN_IDs_2.getNO_ID();
         executeSQL( lStrDelete );

      }

   }


   /**
    * This method is used to get the new task definition IDs
    *
    */
   private simpleIDs getNewTaskDefinitionIDs() {
      String[] iIds = { "TASK_DEFN_DB_ID", "TASK_DEFN_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      String iQueryString =
            "select TASK_DEFN_DB_ID, TASK_DEFN_ID from task_defn order by CREATION_DT desc";
      List<ArrayList<String>> llists = execute( iQueryString, lfields );
      return new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

   }

}
