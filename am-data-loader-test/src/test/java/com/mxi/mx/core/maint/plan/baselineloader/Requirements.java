package com.mxi.mx.core.maint.plan.baselineloader;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
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
import com.mxi.mx.util.ReqAndCompReqTest;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.TestConstants;
import com.mxi.mx.util.ValidationAndImport;
import com.mxi.mx.util.WhereClause;


/**
 * This test suite contains test cases on validation and import functionality on REQ_IMPORT.
 *
 * @author ALICIA QIAN
 */
public class Requirements extends ReqAndCompReqTest {

   @Rule
   public TestName testName = new TestName();
   ValidationAndImport ivalidationandimport;

   private ArrayList<String> updateTables = new ArrayList<String>();
   {
      updateTables.add(
            "UPDATE TASK_TASK SET TASK_DEF_STATUS_CD = 'BUILD' WHERE TASK_CLASS_CD='JIC' and ASSMBL_CD='ACFT_CD1' and TASK_CD='ACFT-SYS-1-1-TRK-BATCH-PARENT-AUTO-JIC'" );
   };

   private ArrayList<String> restoreTables = new ArrayList<String>();
   {
      restoreTables.add(
            "UPDATE TASK_TASK SET TASK_DEF_STATUS_CD = 'ACTV' WHERE TASK_CLASS_CD='JIC' and ASSMBL_CD='ACFT_CD1' and TASK_CD='ACFT-SYS-1-1-TRK-BATCH-PARENT-AUTO-JIC'" );

   };


   /**
    * Clean up after each individual test
    *
    */
   @After
   @Override
   public void after() {
      classDataSetup( restoreTables );
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
      classDataSetup( updateTables );

   }


   /**
    * This test is to verify OPER-24639: when trying to validate a requirement of class REPREF,
    * validation passes.
    *
    */
   @Test
   public void testOPER24639_NoError_REPREF_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // Insert data into C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iREQ_ATA_CD_APU_ROOT + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_APU_ROOT + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_1 + "'" );
      lReqMap.put( "LAST_DEADLINE_DRIVER_BOOL", "'" + iLAST_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_DRIVER_BOOL", "'" + iSOFT_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL + "'" );
      lReqMap.put( "USE_SCHED_FROM_BOOL", "'" + iUSE_SCHED_FROM_BOOL + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL_Y + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD_REPREF + "'" );
      lReqMap.put( "MOC_APPROVAL_BOOL", "'" + iMOC_APPROVAL_BOOL_Y + "'" );
      lReqMap.put( "DAMAGE_RECORD_BOOL", "'" + iDAMAGE_RECORD_BOOL_Y + "'" );
      lReqMap.put( "DAMAGED_COMPONENT_BOOL", "'" + iDAMAGED_COMPONENT_Y + "'" );
      lReqMap.put( "OPS_RESTRICTIONS_LDESC", "'" + iREPREF_OPS_RESTRICTIONS_LDESC + "'" );
      lReqMap.put( "PERF_PENALTIES_LDESC", "'" + iREPREF_PERF_PENALTIES_LDESC + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // Run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // Verify no validation errors
      VerifyValidation();

   }


   /**
    * This test is to verify OPER-24639: When a valid requirement of class REPREF is imported,
    * import passes and correct values are stored in task_rep_ref.
    *
    */
   @Test
   public void testOPER24639_NoError_REPREF_IMPORT() {

      // Run validation
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOPER24639_NoError_REPREF_VALIDATION();

      System.out.println( "Finish validation" );

      // Run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs = getTaskIds( iREQ_TASK_CD_1, iREQ_TASK_NAME_1 );
      List<ArrayList<String>> lTaskRepRefTableData = getTaskRepRefFields( iTASK_IDs );

      // Verify task_rep_ref values
      Assert.assertEquals( "RepRefTable.MOC_APPROVAL_BOOL is incorrect.",
            lTaskRepRefTableData.get( 0 ).get( 0 ), convertYTo1AndNTo0( iMOC_APPROVAL_BOOL_Y ) );
      Assert.assertEquals( "RepRefTable.DAMAGE_RECORD_BOOL is incorrect.",
            lTaskRepRefTableData.get( 0 ).get( 1 ), convertYTo1AndNTo0( iDAMAGE_RECORD_BOOL_Y ) );
      Assert.assertEquals( "RepRefTable.DAMAGED_COMPONENT_BOOL is incorrect.",
            lTaskRepRefTableData.get( 0 ).get( 2 ), convertYTo1AndNTo0( iDAMAGED_COMPONENT_Y ) );
      Assert.assertEquals( "RepRefTable.OPS_RESTRICTIONS_LDESC is incorrect.",
            lTaskRepRefTableData.get( 0 ).get( 3 ), iREPREF_OPS_RESTRICTIONS_LDESC );
      Assert.assertEquals( "RepRefTable.PERF_PENALTIES_LDESC is incorrect.",
            lTaskRepRefTableData.get( 0 ).get( 4 ), iREPREF_PERF_PENALTIES_LDESC );

   }


   /**
    * This test is to verify OPER-24639: A requirement that is of class REPREF cannot have a value
    * of Y in 'DAMAGED_COMPONENT_BOOL' if it has a value of N or blank in 'DAMAGE_RECORD_BOOL'.
    *
    * If 'DAMAGED_COMPONENT_BOOL' is Y and 'DAMAGE_RECORD_BOOL' is N or blank, validation error code
    * 'CFGREQ-12154' is thrown.
    *
    */
   @Test
   public void
         testOPER24639_Error12154_REPREF_DAMAGED_COMPONENT_BOOL_CANNOT_BE_TRUE_UNLESS_DAMAGE_RECORD_TRUE_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // Insert data into C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_ACFT + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_1 + "'" );
      lReqMap.put( "LAST_DEADLINE_DRIVER_BOOL", "'" + iLAST_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_DRIVER_BOOL", "'" + iSOFT_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL_Y + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD_REPREF + "'" );
      lReqMap.put( "DAMAGE_RECORD_BOOL", "''" );
      lReqMap.put( "DAMAGED_COMPONENT_BOOL", "'" + iDAMAGED_COMPONENT_Y + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // Run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // Validate CFGREQ-12154 error
      validateErrorCode( "CFGREQ-12154" );

   }


   /**
    * This test is to verify OPER-24639: A requirement that is of class REPREF has to have a value
    * of Y, N or blank for 'DAMAGED_COMPONENT_BOOL'.
    *
    * If 'DAMAGED_COMPONENT_BOOL' is not Y, N or blank, validation error code 'CFGREQ-12153' is
    * thrown.
    *
    */
   @Test
   public void testOPER24639_Error12153_REPREF_DAMAGED_COMPONENT_BOOL_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // Insert data into C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_ACFT + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_1 + "'" );
      lReqMap.put( "LAST_DEADLINE_DRIVER_BOOL", "'" + iLAST_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_DRIVER_BOOL", "'" + iSOFT_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL_Y + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD_REPREF + "'" );
      lReqMap.put( "DAMAGE_RECORD_BOOL", "'" + iDAMAGE_RECORD_BOOL_Y + "'" );
      lReqMap.put( "DAMAGED_COMPONENT_BOOL", "'" + "H" + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // Run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // Validate CFGREQ-12153 error
      validateErrorCode( "CFGREQ-12153" );

   }


   /**
    * This test is to verify OPER-24639: A requirement that is of class REPREF has to have a value
    * of Y, N or blank for 'DAMAGE_RECORD_BOOL'.
    *
    * If 'DAMAGE_RECORD_BOOL' is not Y, N or blank, validation error code 'CFGREQ-12152' is thrown.
    *
    */
   @Test
   public void testOPER24639_Error12152_REPREF_DAMAGE_RECORD_BOOL_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // Insert data into C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_ACFT + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_1 + "'" );
      lReqMap.put( "LAST_DEADLINE_DRIVER_BOOL", "'" + iLAST_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_DRIVER_BOOL", "'" + iSOFT_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL_Y + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD_REPREF + "'" );
      lReqMap.put( "DAMAGE_RECORD_BOOL", "'" + "H" + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // Run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // Validate CFGREQ-12152 error
      validateErrorCode( "CFGREQ-12152" );

   }


   /**
    * This test is to verify OPER-24639: A requirement that is of class REPREF has to have a value
    * of Y, N or blank for 'MOC_APPROVAL_BOOL'.
    *
    * If 'MOC_APPROVAL_BOOL' is not Y, N or blank, validation error code 'CFGREQ-12151' is thrown.
    *
    */
   @Test
   public void testOPER24639_Error12151_REPREF_MOC_APPROVAL_BOOL_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // Insert data into C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_ACFT + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_1 + "'" );
      lReqMap.put( "LAST_DEADLINE_DRIVER_BOOL", "'" + iLAST_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_DRIVER_BOOL", "'" + iSOFT_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL_Y + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD_REPREF + "'" );
      lReqMap.put( "MOC_APPROVAL_BOOL", "'" + "H" + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // Run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // Validate CFGREQ-12151 error
      validateErrorCode( "CFGREQ-12151" );

   }


   /**
    * This test is to verify OPER-24639: A requirement that is not of class REPREF (FOLLOW in this
    * scenario) has to have all of the following fields set to blank.
    *
    * MOC_APPROVAL_BOOL, DAMAGE_RECORD_BOOL, DAMAGED_COMPONENT_BOOL, OPS_RESTRICTIONS_LDESC,
    * PERF_PENALTIES_LDESC
    *
    * If any of these fields are not blank, validation error code 'CFGREQ-12150' is thrown.
    *
    */
   @Test
   public void testOPER24639_Error12150_REPREF_ATTRIBUTES_ON_NONREPREF_REQUIREMENT_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // Insert data into C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_ACFT + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_1 + "'" );
      lReqMap.put( "LAST_DEADLINE_DRIVER_BOOL", "'" + iLAST_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_DRIVER_BOOL", "'" + iSOFT_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL_Y + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD_FOLLOW + "'" );
      lReqMap.put( "MOC_APPROVAL_BOOL", "'" + iMOC_APPROVAL_BOOL_Y + "'" );
      lReqMap.put( "DAMAGE_RECORD_BOOL", "'" + iDAMAGE_RECORD_BOOL_Y + "'" );
      lReqMap.put( "DAMAGED_COMPONENT_BOOL", "'" + iDAMAGED_COMPONENT_N + "'" );
      lReqMap.put( "OPS_RESTRICTIONS_LDESC", "''" );
      lReqMap.put( "PERF_PENALTIES_LDESC", "''" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // Run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // Validate CFGREQ-12150 error
      validateErrorCode( "CFGREQ-12150" );

   }


   /**
    * This test is to verify req_import.validate_req functionality without recurring task.
    *
    *
    *
    */

   @Test
   public void testACFTTRK_Non_CRT_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_TRK + "'" );
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
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL_2 + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL_2 + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "ENFORCE_WORKSCOPE_ORD_BOOL", "'" + iENFORCE_WORKSCOPE_ORD_BOOL + "'" );
      lReqMap.put( "REQ_INSTRUCTIONS", "'" + iREQ_INSTRUCTIONS + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // C_REQ interchangeable parts
      insertTable_C_REQ_transform();

      // C_REQ_DYNAMIC_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_THRESHOLD_QT", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_DYNAMIC_DEADLINE, lReqMap ) );

      // C_REQ_DYNAMIC_PART_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_THRESHOLD_QT", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_REQ_DYNAMIC_PART_DEADLINE, lReqMap ) );

      // C_REQ_IETM_TOPIC
      insertTable_C_REQ_IETM_TOPIC();

      // C_REQ_JIC
      insertTable_C_REQ_JIC();

      // C_REQ_ADVISORY
      insertTable_C_REQ_ADVISORY();

      // C_REQ_IMPACT
      insertTable_C_REQ_IMPACT();

      // C_REQ_REPL
      insertTable_C_REQ_REPL();

      // C_REQ_PART_TRANSFORM
      insertTable_C_REQ_PART_transform();

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // Verify the validation has no error
      VerifyValidation();

   }


   /**
    * This test is to verify req_import.import_req functionality without recurring task.
    *
    *
    */

   @Test
   public void testACFTTRK_Non_CRT_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testACFTTRK_Non_CRT_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      restoreIDs();

      // Verify task_task table
      // task A
      iTASK_IDs = getTaskIds( iREQ_TASK_CD_1, iREQ_TASK_NAME_1 );
      assmbleInfor lassmblinfor = getassmblInfor( iREQ_ATA_CD_TRK );

      iTASK_DEFN_IDs = verifyTaskTask_15067( iTASK_IDs, iCLASS_CD, lassmblinfor, iSUBCLASS_CD,
            iORIGINATOR_CD, iREQ_TASK_CD_1, iREQ_TASK_NAME_1, iREQ_TASK_DESC_1, iREQ_INSTRUCTIONS,
            iREQ_TASK_REF_SDESC_1, iAPPLICABILITY_DESC, iLAST_DEADLINE_DRIVER_BOOL_N,
            iSOFT_DEADLINE_DRIVER_BOOL_N, iWORKSCOPE_BOOL_2_N, null, null, null, null );

      // task B : repl
      iTASK_IDs_REPL = getTaskIds( iREQ_TASK_CD_2, iREQ_TASK_NAME_2 );
      iTASK_DEFN_IDs_REPL = verifyTaskTask_REPL( iTASK_IDs_REPL, iCLASS_CD_2, lassmblinfor,
            iREQ_TASK_CD_2, iREQ_TASK_NAME_2, iREQ_TASK_DESC_2, iREQ_TASK_REF_SDESC_2 );

      // task C : transform
      iTASK_IDs_TRSFRM = getTaskIds( iREQ_TASK_CD_3, iREQ_TASK_NAME_3 );
      iTASK_DEFN_IDs_TRSFRM = verifyTaskTask_TRSFRM( iTASK_IDs_TRSFRM, iCLASS_CD, lassmblinfor,
            iSUBCLASS_CD, iORIGINATOR_CD, iREQ_TASK_CD_3, iREQ_TASK_NAME_3, iREQ_TASK_DESC_3,
            iREQ_INSTRUCTIONS, iREQ_TASK_REF_SDESC_3, iAPPLICABILITY_DESC,
            iLAST_DEADLINE_DRIVER_BOOL_N, iSOFT_DEADLINE_DRIVER_BOOL_N, iWORKSCOPE_BOOL_2_N, null,
            null, null, null );

      // verify task_DEFN
      // task A
      verifyTaskDEFN( iTASK_DEFN_IDs );

      // task B : repl
      verifyTaskDEFN( iTASK_DEFN_IDs_REPL );

      // task C: transform
      verifyTaskDEFN( iTASK_DEFN_IDs_TRSFRM );

      // verify task_work_type
      verifyWorkType( iTASK_IDs, iWORK_TYPE_LIST_1 );
      verifyWorkType( iTASK_IDs, iWORK_TYPE_LIST_2 );
      verifyWorkType( iTASK_IDs_TRSFRM, iWORK_TYPE_LIST_1 );
      verifyWorkType( iTASK_IDs_TRSFRM, iWORK_TYPE_LIST_2 );

      // verify task_sched_rule table
      simpleIDs lTypeIds = getTypeIds( iSCHED_DATA_TYPE_CD );
      verifyTASKSCHEDRULE_15067( iTASK_IDs, lTypeIds, iSCHED_THRESHOLD_QT, null );

      // verify task_interval
      simpleIDs lPartIds = getPartIds( iPART_NO_OEM_TRK, iMANUFACT_CD_TRK );
      verifyTASKINTERVAL_15067( iTASK_IDs, lTypeIds, lPartIds, iSCHED_THRESHOLD_QT, null );

      // very task_task_ietm
      simpleIDs lietmIds_1 = getIetmIds( iREQ_TOPIC_SDESC_1 );
      verifyTASKTASKIETM( iTASK_IDs, "1", lietmIds_1 );

      simpleIDs lietmIds_2 = getIetmIds( iREQ_TOPIC_SDESC_2 );
      verifyTASKTASKIETM( iTASK_IDs, "2", lietmIds_2 );

      // verify task_jic_req_map
      simpleIDs lJicIds = getReqIds( iJIC_TASK_CD_TRK, "JIC" );
      String lQuery = "select 1 from " + TableUtil.TASK_JIC_REQ_MAP + " where REQ_TASK_DEFN_DB_ID="
            + iTASK_DEFN_IDs.getNO_DB_ID() + " and REQ_TASK_DEFN_ID=" + iTASK_DEFN_IDs.getNO_ID()
            + "and JIC_TASK_DB_ID=" + lJicIds.getNO_DB_ID() + " and JIC_TASK_ID="
            + lJicIds.getNO_ID();
      Assert.assertTrue( "Check TASK_JIC_REQ_MAP table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_advisory
      String lRoleId = getRoleIds( iROLE_CD );
      lQuery = "select 1 from " + TableUtil.TASK_ADVISORY + " where TASK_DB_ID="
            + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID() + " and ROLE_ID="
            + lRoleId + " and TASK_ADVISORY_TYPE_CD='" + iADVSRY_TYPE_CD + "'";
      Assert.assertTrue( "Check TASK_ADVISORY table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_impact
      simpleIDs lImpactIds = getIMPACTIds( iIMPACT_CD );
      lQuery = "select 1 from " + TableUtil.TASK_IMPACT + " where IMPACT_DB_ID="
            + lImpactIds.getNO_DB_ID() + " and IMPACT_CD='" + lImpactIds.getNO_ID() + "'"
            + " and TASK_DB_ID=" + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID();
      Assert.assertTrue( "Check TASK_IMPACT table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_part_transform
      simpleIDs lID_old = getPartNoIds( iPART_NO_OEM_TRK_old, iMANUFACT_CD_TRK_old );
      simpleIDs lID_new = getPartNoIds( iPART_NO_OEM_TRK_new, iMANUFACT_CD_TRK_new );
      verifyTASKPARTTRANSFORM( iTASK_IDs_TRSFRM, lID_old, lID_new );

   }


   /**
    * This test is to verify req_import.validate_req functionality with recurring task.
    *
    *
    *
    */

   @Test
   public void testACFTTRK_CRT_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_TRK + "'" );
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
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "RESCHED_FROM_CD", "'" + iRESCHED_FROM_CD + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL_2 + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL_2 + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "ENFORCE_WORKSCOPE_ORD_BOOL", "'" + iENFORCE_WORKSCOPE_ORD_BOOL + "'" );
      lReqMap.put( "REQ_INSTRUCTIONS", "'" + iREQ_INSTRUCTIONS + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'Y'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // C_REQ interchangeable parts
      insertTable_C_REQ_transform();

      // C_REQ_DYNAMIC_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_INITIAL_QT", "'" + iSCHED_INITIAL_QT + "'" );
      lReqMap.put( "SCHED_INTERVAL_QT", "'" + iSCHED_INTERVAL_QT + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_DYNAMIC_DEADLINE, lReqMap ) );

      // C_REQ_DYNAMIC_PART_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_INITIAL_QT", "'" + iSCHED_INITIAL_QT + "'" );
      lReqMap.put( "SCHED_INTERVAL_QT", "'" + iSCHED_INTERVAL_QT + "'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_REQ_DYNAMIC_PART_DEADLINE, lReqMap ) );

      // C_REQ_IETM_TOPIC
      insertTable_C_REQ_IETM_TOPIC();

      // C_REQ_JIC
      insertTable_C_REQ_JIC();

      // C_REQ_ADVISORY
      insertTable_C_REQ_ADVISORY();

      // C_REQ_IMPACT
      insertTable_C_REQ_IMPACT();

      // C_REQ_REPL
      insertTable_C_REQ_REPL();

      // C_REQ_PART_TRANSFORM
      insertTable_C_REQ_PART_transform();

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // Verify the validation has no error
      VerifyValidation();

   }


   /**
    * This test is to verify req_import.import_req functionality with recurring task.
    *
    *
    */

   @Test
   public void testACFTTRK_CRT_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testACFTTRK_CRT_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      restoreIDs();

      // Verify task_task table
      // task A
      iTASK_IDs = getTaskIds( iREQ_TASK_CD_1, iREQ_TASK_NAME_1 );
      assmbleInfor lassmblinfor = getassmblInfor( iREQ_ATA_CD_TRK );

      iTASK_DEFN_IDs = verifyTaskTask_15067( iTASK_IDs, iCLASS_CD, lassmblinfor, iSUBCLASS_CD,
            iORIGINATOR_CD, iREQ_TASK_CD_1, iREQ_TASK_NAME_1, iREQ_TASK_DESC_1, iREQ_INSTRUCTIONS,
            iREQ_TASK_REF_SDESC_1, iAPPLICABILITY_DESC, iLAST_DEADLINE_DRIVER_BOOL_N,
            iSOFT_DEADLINE_DRIVER_BOOL_N, iWORKSCOPE_BOOL_2_N, null, null, null, null );

      // task B : repl
      iTASK_IDs_REPL = getTaskIds( iREQ_TASK_CD_2, iREQ_TASK_NAME_2 );
      iTASK_DEFN_IDs_REPL = verifyTaskTask_REPL( iTASK_IDs_REPL, iCLASS_CD_2, lassmblinfor,
            iREQ_TASK_CD_2, iREQ_TASK_NAME_2, iREQ_TASK_DESC_2, iREQ_TASK_REF_SDESC_2 );

      // task C : transform
      iTASK_IDs_TRSFRM = getTaskIds( iREQ_TASK_CD_3, iREQ_TASK_NAME_3 );
      iTASK_DEFN_IDs_TRSFRM = verifyTaskTask_TRSFRM( iTASK_IDs_TRSFRM, iCLASS_CD, lassmblinfor,
            iSUBCLASS_CD, iORIGINATOR_CD, iREQ_TASK_CD_3, iREQ_TASK_NAME_3, iREQ_TASK_DESC_3,
            iREQ_INSTRUCTIONS, iREQ_TASK_REF_SDESC_3, iAPPLICABILITY_DESC,
            iLAST_DEADLINE_DRIVER_BOOL_N, iSOFT_DEADLINE_DRIVER_BOOL_N, iWORKSCOPE_BOOL_2_N, null,
            null, null, null );

      // verify task_DEFN
      // Task A
      verifyTaskDEFN( iTASK_DEFN_IDs );

      // task B : repl
      verifyTaskDEFN( iTASK_DEFN_IDs_REPL );

      // task C: transform
      verifyTaskDEFN( iTASK_DEFN_IDs_TRSFRM );

      // verify task_work_type
      verifyWorkType( iTASK_IDs, iWORK_TYPE_LIST_1 );
      verifyWorkType( iTASK_IDs, iWORK_TYPE_LIST_2 );
      verifyWorkType( iTASK_IDs_TRSFRM, iWORK_TYPE_LIST_1 );
      verifyWorkType( iTASK_IDs_TRSFRM, iWORK_TYPE_LIST_2 );

      // Verify task_task_dep
      String lQuery = "select 1 from " + TableUtil.TASK_TASK_DEP + " where TASK_DB_ID="
            + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID()
            + " and TASK_DEP_ACTION_CD='" + iTASK_DEP_ACTION_CD + "'";
      Assert.assertTrue( "Check task_task_dep table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_sched_rule table
      simpleIDs lTypeIds = getTypeIds( iSCHED_DATA_TYPE_CD );
      verifyTASKSCHEDRULE_15067( iTASK_IDs, lTypeIds, iSCHED_INTERVAL_QT, iSCHED_INITIAL_QT );

      // verify task_interval
      simpleIDs lPartIds = getPartIds( iPART_NO_OEM_TRK, iMANUFACT_CD_TRK );
      verifyTASKINTERVAL_15067( iTASK_IDs, lTypeIds, lPartIds, iSCHED_INTERVAL_QT,
            iSCHED_INITIAL_QT );

      // very task_task_ietm
      simpleIDs lietmIds_1 = getIetmIds( iREQ_TOPIC_SDESC_1 );
      verifyTASKTASKIETM( iTASK_IDs, "1", lietmIds_1 );

      simpleIDs lietmIds_2 = getIetmIds( iREQ_TOPIC_SDESC_2 );
      verifyTASKTASKIETM( iTASK_IDs, "2", lietmIds_2 );

      // verify task_jic_req_map
      simpleIDs lJicIds = getReqIds( iJIC_TASK_CD_TRK, "JIC" );
      lQuery = "select 1 from " + TableUtil.TASK_JIC_REQ_MAP + " where REQ_TASK_DEFN_DB_ID="
            + iTASK_DEFN_IDs.getNO_DB_ID() + " and REQ_TASK_DEFN_ID=" + iTASK_DEFN_IDs.getNO_ID()
            + "and JIC_TASK_DB_ID=" + lJicIds.getNO_DB_ID() + " and JIC_TASK_ID="
            + lJicIds.getNO_ID();
      Assert.assertTrue( "Check TASK_JIC_REQ_MAP table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_advisory
      String lRoleId = getRoleIds( iROLE_CD );
      lQuery = "select 1 from " + TableUtil.TASK_ADVISORY + " where TASK_DB_ID="
            + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID() + " and ROLE_ID="
            + lRoleId + " and TASK_ADVISORY_TYPE_CD='" + iADVSRY_TYPE_CD + "'";
      Assert.assertTrue( "Check TASK_ADVISORY table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_impact
      simpleIDs lImpactIds = getIMPACTIds( iIMPACT_CD );
      lQuery = "select 1 from " + TableUtil.TASK_IMPACT + " where IMPACT_DB_ID="
            + lImpactIds.getNO_DB_ID() + " and IMPACT_CD='" + lImpactIds.getNO_ID() + "'"
            + " and TASK_DB_ID=" + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID();
      Assert.assertTrue( "Check TASK_IMPACT table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_part_transform
      simpleIDs lID_old = getPartNoIds( iPART_NO_OEM_TRK_old, iMANUFACT_CD_TRK_old );
      simpleIDs lID_new = getPartNoIds( iPART_NO_OEM_TRK_new, iMANUFACT_CD_TRK_new );
      verifyTASKPARTTRANSFORM( iTASK_IDs_TRSFRM, lID_old, lID_new );

   }


   /**
    * This test is to verify req_import.validate_req functionality with non recurring task using
    * standard deadline.
    *
    *
    */

   @Test
   public void testACFTTRK_Standard_Non_CRT_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_TRK + "'" );
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
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL_2 + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL_2 + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "ENFORCE_WORKSCOPE_ORD_BOOL", "'" + iENFORCE_WORKSCOPE_ORD_BOOL + "'" );
      lReqMap.put( "REQ_INSTRUCTIONS", "'" + iREQ_INSTRUCTIONS + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // C_REQ interchangeable parts
      insertTable_C_REQ_transform();

      // c_req_standard_deadline
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_HRS_THRESHOLD", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STANDARD_DEADLINE, lReqMap ) );

      // C_REQ_IETM_TOPIC
      insertTable_C_REQ_IETM_TOPIC();

      // C_REQ_JIC
      insertTable_C_REQ_JIC();

      // C_REQ_ADVISORY
      insertTable_C_REQ_ADVISORY();

      // C_REQ_IMPACT
      insertTable_C_REQ_IMPACT();

      // C_REQ_REPL
      insertTable_C_REQ_REPL();

      // C_REQ_PART_TRANSFORM
      insertTable_C_REQ_PART_transform();

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // Verify the validation has no error
      VerifyValidation();

   }


   /**
    * This test is to verify req_import.import_req functionality with non recurring task using
    * standard deadline.
    *
    *
    *
    */

   @Test
   public void testACFTTRK_Standard_Non_CRT_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testACFTTRK_Standard_Non_CRT_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      restoreIDs();

      // Verify task_task table
      // Task A
      iTASK_IDs = getTaskIds( iREQ_TASK_CD_1, iREQ_TASK_NAME_1 );
      assmbleInfor lassmblinfor = getassmblInfor( iREQ_ATA_CD_TRK );

      iTASK_DEFN_IDs = verifyTaskTask_15067( iTASK_IDs, iCLASS_CD, lassmblinfor, iSUBCLASS_CD,
            iORIGINATOR_CD, iREQ_TASK_CD_1, iREQ_TASK_NAME_1, iREQ_TASK_DESC_1, iREQ_INSTRUCTIONS,
            iREQ_TASK_REF_SDESC_1, iAPPLICABILITY_DESC, iLAST_DEADLINE_DRIVER_BOOL_N,
            iSOFT_DEADLINE_DRIVER_BOOL_N, iWORKSCOPE_BOOL_2_N, null, null, null, null );

      // task B : repl
      iTASK_IDs_REPL = getTaskIds( iREQ_TASK_CD_2, iREQ_TASK_NAME_2 );
      iTASK_DEFN_IDs_REPL = verifyTaskTask_REPL( iTASK_IDs_REPL, iCLASS_CD_2, lassmblinfor,
            iREQ_TASK_CD_2, iREQ_TASK_NAME_2, iREQ_TASK_DESC_2, iREQ_TASK_REF_SDESC_2 );

      // task C : transform
      iTASK_IDs_TRSFRM = getTaskIds( iREQ_TASK_CD_3, iREQ_TASK_NAME_3 );
      iTASK_DEFN_IDs_TRSFRM = verifyTaskTask_TRSFRM( iTASK_IDs_TRSFRM, iCLASS_CD, lassmblinfor,
            iSUBCLASS_CD, iORIGINATOR_CD, iREQ_TASK_CD_3, iREQ_TASK_NAME_3, iREQ_TASK_DESC_3,
            iREQ_INSTRUCTIONS, iREQ_TASK_REF_SDESC_3, iAPPLICABILITY_DESC,
            iLAST_DEADLINE_DRIVER_BOOL_N, iSOFT_DEADLINE_DRIVER_BOOL_N, iWORKSCOPE_BOOL_2_N, null,
            null, null, null );

      // verify task_DEFN
      // Task A
      verifyTaskDEFN( iTASK_DEFN_IDs );

      // task B : repl
      verifyTaskDEFN( iTASK_DEFN_IDs_REPL );

      // task C: transform
      verifyTaskDEFN( iTASK_DEFN_IDs_TRSFRM );

      // verify task_work_type
      verifyWorkType( iTASK_IDs, iWORK_TYPE_LIST_1 );
      verifyWorkType( iTASK_IDs, iWORK_TYPE_LIST_2 );
      verifyWorkType( iTASK_IDs_TRSFRM, iWORK_TYPE_LIST_1 );
      verifyWorkType( iTASK_IDs_TRSFRM, iWORK_TYPE_LIST_2 );

      // verify task_sched_rule table
      simpleIDs lTypeIds = getTypeIds( iSCHED_DATA_TYPE_CD_HRS );
      verifyTASKSCHEDRULE_15067( iTASK_IDs, lTypeIds, iSCHED_THRESHOLD_QT, null );

      // very task_task_ietm
      simpleIDs lietmIds_1 = getIetmIds( iREQ_TOPIC_SDESC_1 );
      verifyTASKTASKIETM( iTASK_IDs, "1", lietmIds_1 );

      simpleIDs lietmIds_2 = getIetmIds( iREQ_TOPIC_SDESC_2 );
      verifyTASKTASKIETM( iTASK_IDs, "2", lietmIds_2 );

      // verify task_jic_req_map
      simpleIDs lJicIds = getReqIds( iJIC_TASK_CD_TRK, "JIC" );
      String lQuery = "select 1 from " + TableUtil.TASK_JIC_REQ_MAP + " where REQ_TASK_DEFN_DB_ID="
            + iTASK_DEFN_IDs.getNO_DB_ID() + " and REQ_TASK_DEFN_ID=" + iTASK_DEFN_IDs.getNO_ID()
            + "and JIC_TASK_DB_ID=" + lJicIds.getNO_DB_ID() + " and JIC_TASK_ID="
            + lJicIds.getNO_ID();
      Assert.assertTrue( "Check TASK_JIC_REQ_MAP table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_advisory
      String lRoleId = getRoleIds( iROLE_CD );
      lQuery = "select 1 from " + TableUtil.TASK_ADVISORY + " where TASK_DB_ID="
            + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID() + " and ROLE_ID="
            + lRoleId + " and TASK_ADVISORY_TYPE_CD='" + iADVSRY_TYPE_CD + "'";
      Assert.assertTrue( "Check TASK_ADVISORY table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_impact
      simpleIDs lImpactIds = getIMPACTIds( iIMPACT_CD );
      lQuery = "select 1 from " + TableUtil.TASK_IMPACT + " where IMPACT_DB_ID="
            + lImpactIds.getNO_DB_ID() + " and IMPACT_CD='" + lImpactIds.getNO_ID() + "'"
            + " and TASK_DB_ID=" + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID();
      Assert.assertTrue( "Check TASK_IMPACT table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_part_transform
      simpleIDs lID_old = getPartNoIds( iPART_NO_OEM_TRK_old, iMANUFACT_CD_TRK_old );
      simpleIDs lID_new = getPartNoIds( iPART_NO_OEM_TRK_new, iMANUFACT_CD_TRK_new );
      verifyTASKPARTTRANSFORM( iTASK_IDs_TRSFRM, lID_old, lID_new );

   }


   /**
    * This test is to verify req_import.validate_req functionality with recurring task using
    * standard deadline.
    *
    *
    */

   @Test
   public void testACFTTRK_Standard_CRT_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_TRK + "'" );
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
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "RESCHED_FROM_CD", "'" + iRESCHED_FROM_CD + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL_2 + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL_2 + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "ENFORCE_WORKSCOPE_ORD_BOOL", "'" + iENFORCE_WORKSCOPE_ORD_BOOL + "'" );
      lReqMap.put( "REQ_INSTRUCTIONS", "'" + iREQ_INSTRUCTIONS + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'Y'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // C_REQ interchangeable parts
      insertTable_C_REQ_transform();

      // c_req_standard_deadline
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_HRS_INITIAL", "'" + iSCHED_INITIAL_QT + "'" );
      lReqMap.put( "SCHED_HRS_INTERVAL", "'" + iSCHED_INTERVAL_QT + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_STANDARD_DEADLINE, lReqMap ) );

      // C_REQ_IETM_TOPIC
      insertTable_C_REQ_IETM_TOPIC();

      // C_REQ_JIC
      insertTable_C_REQ_JIC();

      // C_REQ_ADVISORY
      insertTable_C_REQ_ADVISORY();

      // C_REQ_IMPACT
      insertTable_C_REQ_IMPACT();

      // C_REQ_REPL
      insertTable_C_REQ_REPL();

      // C_REQ_PART_TRANSFORM
      insertTable_C_REQ_PART_transform();

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // Verify the validation has no error
      VerifyValidation();

   }


   /**
    * This test is to verify req_import.import_req functionality with recurring task using standard
    * deadline.
    *
    *
    */

   @Test
   public void testACFTTRK_Standard_CRT_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testACFTTRK_Standard_CRT_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      restoreIDs();

      // Verify task_task table
      // task A
      iTASK_IDs = getTaskIds( iREQ_TASK_CD_1, iREQ_TASK_NAME_1 );
      assmbleInfor lassmblinfor = getassmblInfor( iREQ_ATA_CD_TRK );

      iTASK_DEFN_IDs = verifyTaskTask_15067( iTASK_IDs, iCLASS_CD, lassmblinfor, iSUBCLASS_CD,
            iORIGINATOR_CD, iREQ_TASK_CD_1, iREQ_TASK_NAME_1, iREQ_TASK_DESC_1, iREQ_INSTRUCTIONS,
            iREQ_TASK_REF_SDESC_1, iAPPLICABILITY_DESC, iLAST_DEADLINE_DRIVER_BOOL_N,
            iSOFT_DEADLINE_DRIVER_BOOL_N, iWORKSCOPE_BOOL_2_N, null, null, null, null );

      // task B : repl
      iTASK_IDs_REPL = getTaskIds( iREQ_TASK_CD_2, iREQ_TASK_NAME_2 );
      iTASK_DEFN_IDs_REPL = verifyTaskTask_REPL( iTASK_IDs_REPL, iCLASS_CD_2, lassmblinfor,
            iREQ_TASK_CD_2, iREQ_TASK_NAME_2, iREQ_TASK_DESC_2, iREQ_TASK_REF_SDESC_2 );

      // task C : transform
      iTASK_IDs_TRSFRM = getTaskIds( iREQ_TASK_CD_3, iREQ_TASK_NAME_3 );
      iTASK_DEFN_IDs_TRSFRM = verifyTaskTask_TRSFRM( iTASK_IDs_TRSFRM, iCLASS_CD, lassmblinfor,
            iSUBCLASS_CD, iORIGINATOR_CD, iREQ_TASK_CD_3, iREQ_TASK_NAME_3, iREQ_TASK_DESC_3,
            iREQ_INSTRUCTIONS, iREQ_TASK_REF_SDESC_3, iAPPLICABILITY_DESC,
            iLAST_DEADLINE_DRIVER_BOOL_N, iSOFT_DEADLINE_DRIVER_BOOL_N, iWORKSCOPE_BOOL_2_N, null,
            null, null, null );

      // verify task_DEFN
      // Task A
      verifyTaskDEFN( iTASK_DEFN_IDs );

      // task B : repl
      verifyTaskDEFN( iTASK_DEFN_IDs_REPL );

      // task C: transform
      verifyTaskDEFN( iTASK_DEFN_IDs_TRSFRM );

      // verify task_work_type
      verifyWorkType( iTASK_IDs, iWORK_TYPE_LIST_1 );
      verifyWorkType( iTASK_IDs, iWORK_TYPE_LIST_2 );
      verifyWorkType( iTASK_IDs_TRSFRM, iWORK_TYPE_LIST_1 );
      verifyWorkType( iTASK_IDs_TRSFRM, iWORK_TYPE_LIST_2 );

      // Verify task_task_dep
      String lQuery = "select 1 from " + TableUtil.TASK_TASK_DEP + " where TASK_DB_ID="
            + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID()
            + " and TASK_DEP_ACTION_CD='" + iTASK_DEP_ACTION_CD + "'";
      Assert.assertTrue( "Check task_task_dep table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_sched_rule table
      simpleIDs lTypeIds = getTypeIds( iSCHED_DATA_TYPE_CD_HRS );
      verifyTASKSCHEDRULE_15067( iTASK_IDs, lTypeIds, iSCHED_INTERVAL_QT, iSCHED_INITIAL_QT );

      // very task_task_ietm
      simpleIDs lietmIds_1 = getIetmIds( iREQ_TOPIC_SDESC_1 );
      verifyTASKTASKIETM( iTASK_IDs, "1", lietmIds_1 );

      simpleIDs lietmIds_2 = getIetmIds( iREQ_TOPIC_SDESC_2 );
      verifyTASKTASKIETM( iTASK_IDs, "2", lietmIds_2 );

      // verify task_jic_req_map
      simpleIDs lJicIds = getReqIds( iJIC_TASK_CD_TRK, "JIC" );
      lQuery = "select 1 from " + TableUtil.TASK_JIC_REQ_MAP + " where REQ_TASK_DEFN_DB_ID="
            + iTASK_DEFN_IDs.getNO_DB_ID() + " and REQ_TASK_DEFN_ID=" + iTASK_DEFN_IDs.getNO_ID()
            + "and JIC_TASK_DB_ID=" + lJicIds.getNO_DB_ID() + " and JIC_TASK_ID="
            + lJicIds.getNO_ID();
      Assert.assertTrue( "Check TASK_JIC_REQ_MAP table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_advisory
      String lRoleId = getRoleIds( iROLE_CD );
      lQuery = "select 1 from " + TableUtil.TASK_ADVISORY + " where TASK_DB_ID="
            + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID() + " and ROLE_ID="
            + lRoleId + " and TASK_ADVISORY_TYPE_CD='" + iADVSRY_TYPE_CD + "'";
      Assert.assertTrue( "Check TASK_ADVISORY table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_impact
      simpleIDs lImpactIds = getIMPACTIds( iIMPACT_CD );
      lQuery = "select 1 from " + TableUtil.TASK_IMPACT + " where IMPACT_DB_ID="
            + lImpactIds.getNO_DB_ID() + " and IMPACT_CD='" + lImpactIds.getNO_ID() + "'"
            + " and TASK_DB_ID=" + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID();
      Assert.assertTrue( "Check TASK_IMPACT table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_part_transform
      simpleIDs lID_old = getPartNoIds( iPART_NO_OEM_TRK_old, iMANUFACT_CD_TRK_old );
      simpleIDs lID_new = getPartNoIds( iPART_NO_OEM_TRK_new, iMANUFACT_CD_TRK_new );
      verifyTASKPARTTRANSFORM( iTASK_IDs_TRSFRM, lID_old, lID_new );

   }


   /**
    * This test is to verify JIRA-15067 on req_import.validate_req functionality
    * Requirements-dynamic deadline RD. 1. In c_req table, create a requirement with RESCHED_FROM_CD
    * as null, and in C_REQ_DYNAMIC_DEADLINE table set SCHED_SCHED_INITIAL_QT and SCHED_INTERVAL_QT
    * are null. It should pass validation.
    *
    */

   @Test
   public void testOPER15067RD1VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_TRK + "'" );
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
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL + "'" );
      lReqMap.put( "REQ_INSTRUCTIONS", "'" + iREQ_INSTRUCTIONS + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // C_REQ_DYNAMIC_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_THRESHOLD_QT", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_DYNAMIC_DEADLINE, lReqMap ) );

      // C_REQ_DYNAMIC_PART_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_THRESHOLD_QT", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_REQ_DYNAMIC_PART_DEADLINE, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify JIRA-15067 on req_import.import_req functionality Requirements-dynamic
    * deadline RD. 1. In c_req table, create a requirement with RESCHED_FROM_CD as null, and in
    * C_REQ_DYNAMIC_DEADLINE table set SCHED_SCHED_INITIAL_QT and SCHED_INTERVAL_QT are null. It
    * should pass validation.
    *
    */

   @Test
   public void testOPER15067RD1IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOPER15067RD1VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs_15067 = null;
      iTASK_DEFN_IDs_15067 = null;

      // Verify task_task table
      iTASK_IDs_15067 = getTaskIds( iREQ_TASK_CD_1, iREQ_TASK_NAME_1 );
      assmbleInfor lassmblinfor = getassmblInfor( iREQ_ATA_CD_TRK );

      iTASK_DEFN_IDs_15067 = verifyTaskTask_15067( iTASK_IDs_15067, iCLASS_CD, lassmblinfor,
            iSUBCLASS_CD, iORIGINATOR_CD, iREQ_TASK_CD_1, iREQ_TASK_NAME_1, iREQ_TASK_DESC_1,
            iREQ_INSTRUCTIONS, iREQ_TASK_REF_SDESC_1, iAPPLICABILITY_DESC,
            iLAST_DEADLINE_DRIVER_BOOL_N, iSOFT_DEADLINE_DRIVER_BOOL_N, iWORKSCOPE_BOOL_N, null,
            null, null, null );

      // verify task_DEFN
      String lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTASK_DEFN_IDs_15067.getNO_DB_ID() + " and TASK_DEFN_ID="
            + iTASK_DEFN_IDs_15067.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_sched_rule table
      simpleIDs lTypeIds = getTypeIds( iSCHED_DATA_TYPE_CD );
      verifyTASKSCHEDRULE_15067( iTASK_IDs_15067, lTypeIds, iSCHED_THRESHOLD_QT, null );

      // verify task_interval
      simpleIDs lPartIds = getPartIds( iPART_NO_OEM_TRK, iMANUFACT_CD_TRK );
      verifyTASKINTERVAL_15067( iTASK_IDs_15067, lTypeIds, lPartIds, iSCHED_THRESHOLD_QT, null );

   }


   /**
    * This test is to verify JIRA-15067 on req_import.validate_req functionality
    * Requirements-dynamic deadline RD.2. In c_req table, create a requirement with RESCHED_FROM_CD
    * as null, and in C_REQ_DYNAMIC_DEADLINE table set either SCHED_INITIAL_QT or SCHED_INTERVAL_QT
    * is not null. It should not pass validation with error code "CFGREQ-12111"
    *
    *
    */

   @Test
   public void testOPER15067RD2() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_TRK + "'" );
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
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL + "'" );
      lReqMap.put( "REQ_INSTRUCTIONS", "'" + iREQ_INSTRUCTIONS + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // C_REQ_DYNAMIC_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_INITIAL_QT", "'" + iSCHED_INITIAL_QT + "'" );
      lReqMap.put( "SCHED_INTERVAL_QT", null );
      lReqMap.put( "SCHED_THRESHOLD_QT", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_DYNAMIC_DEADLINE, lReqMap ) );

      // C_REQ_DYNAMIC_PART_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_INITIAL_QT", "'" + iSCHED_INITIAL_QT + "'" );
      lReqMap.put( "SCHED_INTERVAL_QT", null );
      lReqMap.put( "SCHED_THRESHOLD_QT", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_REQ_DYNAMIC_PART_DEADLINE, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );
      validateErrorCode( "CFGREQ-12111" );
      validateErrorCode( "CFGREQ-10016" );

   }


   /**
    * This test is to verify JIRA-15067 on req_import.validate_req functionality
    * Requirements-dynamic deadline RD.3. In c_req table, create a requirement with RESCHED_FROM_CD
    * as not null, and no entry in C_REQ_DYNAMIC_DEADLINE table. It should pass validation.
    *
    *
    */

   @Test
   public void testOPER15067RD3VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_TRK + "'" );
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
      lReqMap.put( "RESCHED_FROM_CD", "'" + iRESCHED_FROM_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL + "'" );
      lReqMap.put( "REQ_INSTRUCTIONS", "'" + iREQ_INSTRUCTIONS + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'Y'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify JIRA-15067 on req_import.import_req functionality Requirements-dynamic
    * deadline RD.3. In c_req table, create a requirement with RESCHED_FROM_CD as not null, and no
    * entry in C_REQ_DYNAMIC_DEADLINE table. It should pass validation.
    *
    *
    */

   @Test
   public void testOPER15067RD3IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOPER15067RD3VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs_15067 = null;
      iTASK_DEFN_IDs_15067 = null;

      // Verify task_task table
      iTASK_IDs_15067 = getTaskIds( iREQ_TASK_CD_1, iREQ_TASK_NAME_1 );
      assmbleInfor lassmblinfor = getassmblInfor( iREQ_ATA_CD_TRK );

      iTASK_DEFN_IDs_15067 = verifyTaskTask_15067( iTASK_IDs_15067, iCLASS_CD, lassmblinfor,
            iSUBCLASS_CD, iORIGINATOR_CD, iREQ_TASK_CD_1, iREQ_TASK_NAME_1, iREQ_TASK_DESC_1,
            iREQ_INSTRUCTIONS, iREQ_TASK_REF_SDESC_1, iAPPLICABILITY_DESC,
            iLAST_DEADLINE_DRIVER_BOOL_N, iSOFT_DEADLINE_DRIVER_BOOL_N, iWORKSCOPE_BOOL_N, null,
            null, null, null );

      // verify task_DEFN
      String lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTASK_DEFN_IDs_15067.getNO_DB_ID() + " and TASK_DEFN_ID="
            + iTASK_DEFN_IDs_15067.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_task_dep table
      verifyTASKTASKDEP_15067( iTASK_IDs_15067, iTASK_DEP_ACTION_CD, iTASK_DEFN_IDs_15067 );

   }


   /**
    * This test is to verify JIRA-15067 on req_import.validate_req functionality
    * Requirements-dynamic deadline. RD.4. In c_req table, create a requirement with RESCHED_FROM_CD
    * as not null, and in C_REQ_DYNAMIC_DEADLINE table, set SCHED_THRESHOLD_QT as not null and set
    * SCHED_INITIAL_QT and SCHED_INTERVAL_QT are null. It should not pass validation with error code
    * "CFGREQ-12112".
    *
    */
   @Test
   public void testOPER15067RD4() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_TRK + "'" );
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
      lReqMap.put( "RESCHED_FROM_CD", "'" + iRESCHED_FROM_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL + "'" );
      lReqMap.put( "REQ_INSTRUCTIONS", "'" + iREQ_INSTRUCTIONS + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'Y'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // C_REQ_DYNAMIC_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_INITIAL_QT", null );
      lReqMap.put( "SCHED_INTERVAL_QT", null );
      lReqMap.put( "SCHED_THRESHOLD_QT", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_DYNAMIC_DEADLINE, lReqMap ) );

      // C_REQ_DYNAMIC_PART_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_INITIAL_QT", null );
      lReqMap.put( "SCHED_INTERVAL_QT", null );
      lReqMap.put( "SCHED_THRESHOLD_QT", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_REQ_DYNAMIC_PART_DEADLINE, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );
      validateErrorCode( "CFGREQ-12112" );
      validateErrorCode( "CFGREQ-12096" );

   }


   @Test
   public void testOPER15067RD4_1() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_TRK + "'" );
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
      // lReqMap.put( "RESCHED_FROM_CD", "'" + iRESCHED_FROM_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL + "'" );
      lReqMap.put( "REQ_INSTRUCTIONS", "'" + iREQ_INSTRUCTIONS + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // C_REQ_DYNAMIC_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_INITIAL_QT", null );
      lReqMap.put( "SCHED_INTERVAL_QT", "'" + iSCHED_INTERVAL_QT + "'" );
      // lReqMap.put( "SCHED_THRESHOLD_QT", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_DYNAMIC_DEADLINE, lReqMap ) );

      // C_REQ_DYNAMIC_PART_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_INITIAL_QT", null );
      lReqMap.put( "SCHED_INTERVAL_QT", "'" + iSCHED_INTERVAL_QT + "'" );
      // lReqMap.put( "SCHED_THRESHOLD_QT", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_REQ_DYNAMIC_PART_DEADLINE, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );
      validateErrorCode( "CFGREQ-12097" );

   }


   /**
    * This test is to verify JIRA-15067 on req_import.validate_req functionality
    * Requirements-dynamic deadline.RD.5. In c_req table, create a requirement with RESCHED_FROM_CD
    * as not null, and in C_REQ_DYNAMIC_DEADLINE table, set SCHED_THRESHOLD_QT as null, set
    * SCHED_INITIAL_QT is null and SCHED_INTERVAL_QT is not null. It should pass validation.
    *
    */

   @Test
   public void testOPER15067RD5VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_TRK + "'" );
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
      lReqMap.put( "RESCHED_FROM_CD", "'" + iRESCHED_FROM_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL + "'" );
      lReqMap.put( "REQ_INSTRUCTIONS", "'" + iREQ_INSTRUCTIONS + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'Y'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // C_REQ_DYNAMIC_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_INITIAL_QT", null );
      lReqMap.put( "SCHED_INTERVAL_QT", "'" + iSCHED_INTERVAL_QT + "'" );
      lReqMap.put( "SCHED_THRESHOLD_QT", null );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_DYNAMIC_DEADLINE, lReqMap ) );

      // C_REQ_DYNAMIC_PART_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_INITIAL_QT", null );
      lReqMap.put( "SCHED_INTERVAL_QT", "'" + iSCHED_INTERVAL_QT + "'" );
      lReqMap.put( "SCHED_THRESHOLD_QT", null );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_REQ_DYNAMIC_PART_DEADLINE, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify JIRA-15067 on req_import.import_req functionality Requirements-dynamic
    * deadline.RD.5. In c_req table, create a requirement with RESCHED_FROM_CD as not null, and in
    * C_REQ_DYNAMIC_DEADLINE table, set SCHED_THRESHOLD_QT as null, set SCHED_INITIAL_QT is null and
    * SCHED_INTERVAL_QT is not null. It should pass validation.
    *
    */

   @Test
   public void testOPER15067RD5IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOPER15067RD5VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs_15067 = null;
      iTASK_DEFN_IDs_15067 = null;

      // Verify task_task table
      iTASK_IDs_15067 = getTaskIds( iREQ_TASK_CD_1, iREQ_TASK_NAME_1 );
      assmbleInfor lassmblinfor = getassmblInfor( iREQ_ATA_CD_TRK );

      iTASK_DEFN_IDs_15067 = verifyTaskTask_15067( iTASK_IDs_15067, iCLASS_CD, lassmblinfor,
            iSUBCLASS_CD, iORIGINATOR_CD, iREQ_TASK_CD_1, iREQ_TASK_NAME_1, iREQ_TASK_DESC_1,
            iREQ_INSTRUCTIONS, iREQ_TASK_REF_SDESC_1, iAPPLICABILITY_DESC,
            iLAST_DEADLINE_DRIVER_BOOL_N, iSOFT_DEADLINE_DRIVER_BOOL_N, iWORKSCOPE_BOOL_N, null,
            null, null, null );

      // verify task_DEFN
      String lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTASK_DEFN_IDs_15067.getNO_DB_ID() + " and TASK_DEFN_ID="
            + iTASK_DEFN_IDs_15067.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_task_dep table
      verifyTASKTASKDEP_15067( iTASK_IDs_15067, iTASK_DEP_ACTION_CD, iTASK_DEFN_IDs_15067 );

      // verify task_sched_rule table
      simpleIDs lTypeIds = getTypeIds( iSCHED_DATA_TYPE_CD );
      verifyTASKSCHEDRULE_15067( iTASK_IDs_15067, lTypeIds, iSCHED_INTERVAL_QT, null );

      // verify task_interval
      simpleIDs lPartIds = getPartIds( iPART_NO_OEM_TRK, iMANUFACT_CD_TRK );
      verifyTASKINTERVAL_15067( iTASK_IDs_15067, lTypeIds, lPartIds, iSCHED_INTERVAL_QT, null );

   }


   /**
    * This test is to verify JIRA-15067 on req_import.validate_req functionality
    * Requirements-dynamic deadline. RD.6. In c_req table, create a requirement with RESCHED_FROM_CD
    * as not null, and in C_REQ_DYNAMIC_DEADLINE table, set SCHED_THRESHOLD_QT as null, set
    * SCHED_INITIAL_QT is not null and SCHED_INTERVAL_QT is not null. It should pass validation.
    *
    */
   @Test
   public void testOPER15067RD6VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_TRK + "'" );
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
      lReqMap.put( "RESCHED_FROM_CD", "'" + iRESCHED_FROM_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL + "'" );
      lReqMap.put( "REQ_INSTRUCTIONS", "'" + iREQ_INSTRUCTIONS + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'Y'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // C_REQ_DYNAMIC_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_INITIAL_QT", "'" + iSCHED_INITIAL_QT + "'" );
      lReqMap.put( "SCHED_INTERVAL_QT", "'" + iSCHED_INTERVAL_QT + "'" );
      lReqMap.put( "SCHED_THRESHOLD_QT", null );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_DYNAMIC_DEADLINE, lReqMap ) );

      // C_REQ_DYNAMIC_PART_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_INITIAL_QT", "'" + iSCHED_INITIAL_QT + "'" );
      lReqMap.put( "SCHED_INTERVAL_QT", "'" + iSCHED_INTERVAL_QT + "'" );
      lReqMap.put( "SCHED_THRESHOLD_QT", null );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_REQ_DYNAMIC_PART_DEADLINE, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify JIRA-15067 on req_import.import_req functionality Requirements-dynamic
    * deadline. RD.6. In c_req table, create a requirement with RESCHED_FROM_CD as not null, and in
    * C_REQ_DYNAMIC_DEADLINE table, set SCHED_THRESHOLD_QT as null, set SCHED_INITIAL_QT is not null
    * and SCHED_INTERVAL_QT is not null. It should pass validation.
    *
    */

   @Test
   public void testOPER15067RD6IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOPER15067RD6VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs_15067 = null;
      iTASK_DEFN_IDs_15067 = null;

      // Verify task_task table
      iTASK_IDs_15067 = getTaskIds( iREQ_TASK_CD_1, iREQ_TASK_NAME_1 );
      assmbleInfor lassmblinfor = getassmblInfor( iREQ_ATA_CD_TRK );

      iTASK_DEFN_IDs_15067 = verifyTaskTask_15067( iTASK_IDs_15067, iCLASS_CD, lassmblinfor,
            iSUBCLASS_CD, iORIGINATOR_CD, iREQ_TASK_CD_1, iREQ_TASK_NAME_1, iREQ_TASK_DESC_1,
            iREQ_INSTRUCTIONS, iREQ_TASK_REF_SDESC_1, iAPPLICABILITY_DESC,
            iLAST_DEADLINE_DRIVER_BOOL_N, iSOFT_DEADLINE_DRIVER_BOOL_N, iWORKSCOPE_BOOL_N, null,
            null, null, null );

      // verify task_DEFN
      String lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTASK_DEFN_IDs_15067.getNO_DB_ID() + " and TASK_DEFN_ID="
            + iTASK_DEFN_IDs_15067.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_task_dep table
      verifyTASKTASKDEP_15067( iTASK_IDs_15067, iTASK_DEP_ACTION_CD, iTASK_DEFN_IDs_15067 );

      // verify task_sched_rule table
      simpleIDs lTypeIds = getTypeIds( iSCHED_DATA_TYPE_CD );
      verifyTASKSCHEDRULE_15067( iTASK_IDs_15067, lTypeIds, iSCHED_INTERVAL_QT, iSCHED_INITIAL_QT );

      // verify task_interval
      simpleIDs lPartIds = getPartIds( iPART_NO_OEM_TRK, iMANUFACT_CD_TRK );
      verifyTASKINTERVAL_15067( iTASK_IDs_15067, lTypeIds, lPartIds, iSCHED_INTERVAL_QT,
            iSCHED_INITIAL_QT );

   }


   /**
    * This test is to verify JIRA-15067 on req_import.validate_req functionality
    * Requirements-dynamic deadline. RD.7. In c_req table, create a requirement with RESCHED_FROM_CD
    * as not null, and in C_REQ_DYNAMIC_DEADLINE table, set SCHED_THRESHOLD_QT as null, set
    * SCHED_INITIAL_QT is not null and SCHED_INTERVAL_QT is null. It should not pass validation with
    * error code "CFGREQ-10016".
    */

   @Test
   public void testOPER15067RD7() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_TRK + "'" );
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
      lReqMap.put( "RESCHED_FROM_CD", "'" + iRESCHED_FROM_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL + "'" );
      lReqMap.put( "REQ_INSTRUCTIONS", "'" + iREQ_INSTRUCTIONS + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'Y'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // C_REQ_DYNAMIC_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_INITIAL_QT", "'" + iSCHED_INITIAL_QT + "'" );
      lReqMap.put( "SCHED_INTERVAL_QT", null );
      lReqMap.put( "SCHED_THRESHOLD_QT", null );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_DYNAMIC_DEADLINE, lReqMap ) );

      // C_REQ_DYNAMIC_PART_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_INITIAL_QT", "'" + iSCHED_INITIAL_QT + "'" );
      lReqMap.put( "SCHED_INTERVAL_QT", null );
      lReqMap.put( "SCHED_THRESHOLD_QT", null );
      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_REQ_DYNAMIC_PART_DEADLINE, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );
      validateErrorCode( "CFGREQ-10016" );
      validateErrorCode( "CFGREQ-10037" );

   }


   /**
    * This test is to verify OPER-23629: there is no error when req of off wing is applied on TRK
    * config slot.
    *
    *
    *
    */

   @Test
   public void testOPER23629_NoError_OFFWING_TRK_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_TRK + "'" );
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
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL_2 + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL_2 + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "ENFORCE_WORKSCOPE_ORD_BOOL", "'" + iENFORCE_WORKSCOPE_ORD_BOOL + "'" );
      lReqMap.put( "REQ_INSTRUCTIONS", "'" + iREQ_INSTRUCTIONS + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'N'" );
      lReqMap.put( "TASK_MUST_REMOVE_CD", "'" + iTask_MUST_REMOVE_CD_OFFWING + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // Verify the validation has no error
      VerifyValidation();

   }


   /**
    * This test is to verify OPER-23629: there is no error when req of on wing is applied on TRK
    * config slot.
    *
    *
    *
    */

   @Test
   public void testOPER23629_NoError_ONWING_TRK_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_TRK + "'" );
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
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL_2 + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL_2 + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "ENFORCE_WORKSCOPE_ORD_BOOL", "'" + iENFORCE_WORKSCOPE_ORD_BOOL + "'" );
      lReqMap.put( "REQ_INSTRUCTIONS", "'" + iREQ_INSTRUCTIONS + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'N'" );
      lReqMap.put( "TASK_MUST_REMOVE_CD", "'" + iTask_MUST_REMOVE_CD_ONWING + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // Verify the validation has no error
      VerifyValidation();

   }


   /**
    * This test is to verify OPER-23629: there is no error when req of off wing is applied on
    * subAssy-ENG config slot.
    *
    *
    *
    */

   @Test
   public void testOPER23629_NoError_OFFWING_ENG_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_ENG + "'" );
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
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL_2 + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL_2 + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "ENFORCE_WORKSCOPE_ORD_BOOL", "'" + iENFORCE_WORKSCOPE_ORD_BOOL + "'" );
      lReqMap.put( "REQ_INSTRUCTIONS", "'" + iREQ_INSTRUCTIONS + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'N'" );
      lReqMap.put( "TASK_MUST_REMOVE_CD", "'" + iTask_MUST_REMOVE_CD_OFFWING + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // Verify the validation has no error
      VerifyValidation();

   }


   /**
    * This test is to verify OPER-23629: there is no error when req of off wing is applied on
    * subAssy-ENG config slot.
    *
    *
    *
    */

   @Test
   public void testOPER23629_NoError_ONWING_ENG_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_ENG + "'" );
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
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL_2 + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL_2 + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "ENFORCE_WORKSCOPE_ORD_BOOL", "'" + iENFORCE_WORKSCOPE_ORD_BOOL + "'" );
      lReqMap.put( "REQ_INSTRUCTIONS", "'" + iREQ_INSTRUCTIONS + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'N'" );
      lReqMap.put( "TASK_MUST_REMOVE_CD", "'" + iTask_MUST_REMOVE_CD_ONWING + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // Verify the validation has no error
      VerifyValidation();

   }


   /**
    * This test is to verify OPER-23629: there is no error when req of off wing is applied on
    * subAssy-APU config slot.
    *
    *
    *
    */

   @Test
   public void testOPER23629_NoError_OFFWING_APU_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_APU + "'" );
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
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL_2 + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL_2 + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "ENFORCE_WORKSCOPE_ORD_BOOL", "'" + iENFORCE_WORKSCOPE_ORD_BOOL + "'" );
      lReqMap.put( "REQ_INSTRUCTIONS", "'" + iREQ_INSTRUCTIONS + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'N'" );
      lReqMap.put( "TASK_MUST_REMOVE_CD", "'" + iTask_MUST_REMOVE_CD_OFFWING + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // Verify the validation has no error
      VerifyValidation();

   }


   /**
    * This test is to verify OPER-23629: there is no error when req of off wing is applied on
    * subAssy-APU config slot.
    *
    *
    *
    */

   @Test
   public void testOPER23629_NoError_ONWING_APU_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_APU + "'" );
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
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL_2 + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL_2 + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "ENFORCE_WORKSCOPE_ORD_BOOL", "'" + iENFORCE_WORKSCOPE_ORD_BOOL + "'" );
      lReqMap.put( "REQ_INSTRUCTIONS", "'" + iREQ_INSTRUCTIONS + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'N'" );
      lReqMap.put( "TASK_MUST_REMOVE_CD", "'" + iTask_MUST_REMOVE_CD_ONWING + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // Verify the validation has no error
      VerifyValidation();

   }


   /**
    * This test is to verify OPER-23629: there no error when req of off wing is applied on sys
    * config slot.
    *
    *
    *
    */

   @Test
   public void testOPER23629_Error12108_OFFWING_SYS_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_SYS + "'" );
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
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL_2 + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL_2 + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "ENFORCE_WORKSCOPE_ORD_BOOL", "'" + iENFORCE_WORKSCOPE_ORD_BOOL + "'" );
      lReqMap.put( "REQ_INSTRUCTIONS", "'" + iREQ_INSTRUCTIONS + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'N'" );
      lReqMap.put( "TASK_MUST_REMOVE_CD", "'" + iTask_MUST_REMOVE_CD_OFFWING + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );
      validateErrorCode( "CFGREQ-12108" );

   }


   /**
    * This test is to verify OPER-23629: there is no error when req of off wing is applied on sys
    * config slot.
    *
    *
    *
    */

   @Test
   public void testOPER23629_NoError_ONWING_SYS_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_SYS + "'" );
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
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL_2 + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL_2 + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "ENFORCE_WORKSCOPE_ORD_BOOL", "'" + iENFORCE_WORKSCOPE_ORD_BOOL + "'" );
      lReqMap.put( "REQ_INSTRUCTIONS", "'" + iREQ_INSTRUCTIONS + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'N'" );
      lReqMap.put( "TASK_MUST_REMOVE_CD", "'" + iTask_MUST_REMOVE_CD_ONWING + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // Verify the validation has no error
      VerifyValidation();

   }


   /**
    * This test is to verify OPER-23629: there is error when req of off wing is applied on ACFT
    * config slot.
    *
    *
    *
    */

   @Test
   public void testOPER23629_Error12108_OFFWING_ACFT_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_ACFT + "'" );
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
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL_2 + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL_2 + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "ENFORCE_WORKSCOPE_ORD_BOOL", "'" + iENFORCE_WORKSCOPE_ORD_BOOL + "'" );
      lReqMap.put( "REQ_INSTRUCTIONS", "'" + iREQ_INSTRUCTIONS + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'N'" );
      lReqMap.put( "TASK_MUST_REMOVE_CD", "'" + iTask_MUST_REMOVE_CD_OFFWING + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );
      validateErrorCode( "CFGREQ-12108" );

   }


   /**
    * This test is to verify OPER-23629: there is no error when req of off wing is applied on ACFT
    * config slot.
    *
    *
    *
    */

   @Test
   public void testOPER23629_NoError_ONWING_ACFT_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_ACFT + "'" );
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
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL_2 + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL_2 + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "ENFORCE_WORKSCOPE_ORD_BOOL", "'" + iENFORCE_WORKSCOPE_ORD_BOOL + "'" );
      lReqMap.put( "REQ_INSTRUCTIONS", "'" + iREQ_INSTRUCTIONS + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'N'" );
      lReqMap.put( "TASK_MUST_REMOVE_CD", "'" + iTask_MUST_REMOVE_CD_ONWING + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // Verify the validation has no error
      VerifyValidation();

   }


   /**
    * This test is to verify OPER-23629: there is no error when req of off wing is applied on ENG
    * root config slot.
    *
    *
    *
    */

   @Test
   public void testOPER23629_NoError_OFFWING_ENG_ROOT_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      // lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "ASSMBL_CD", "'" + iREQ_ATA_CD_ENG_ROOT + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_ENG_ROOT + "'" );
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
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL_2 + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL_2 + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "ENFORCE_WORKSCOPE_ORD_BOOL", "'" + iENFORCE_WORKSCOPE_ORD_BOOL + "'" );
      lReqMap.put( "REQ_INSTRUCTIONS", "'" + iREQ_INSTRUCTIONS + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'N'" );
      lReqMap.put( "TASK_MUST_REMOVE_CD", "'" + iTask_MUST_REMOVE_CD_OFFWING + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // There should not have any error(s)
      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // Verify the validation has no error
      VerifyValidation();

   }


   /**
    * This test is to verify OPER-23629: there is no error when req of on wing is applied on ENG
    * root config slot.
    *
    *
    *
    */

   @Test
   public void testOPER23629_NoError_ONWING_ENG_ROOT_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iREQ_ATA_CD_ENG_ROOT + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_ENG_ROOT + "'" );
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
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL_2 + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL_2 + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "ENFORCE_WORKSCOPE_ORD_BOOL", "'" + iENFORCE_WORKSCOPE_ORD_BOOL + "'" );
      lReqMap.put( "REQ_INSTRUCTIONS", "'" + iREQ_INSTRUCTIONS + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'N'" );
      lReqMap.put( "TASK_MUST_REMOVE_CD", "'" + iTask_MUST_REMOVE_CD_ONWING + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // There should not have any error(s)
      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // Verify the validation has no error
      VerifyValidation();

   }


   /**
    * This test is to verify OPER-23629: there is no error when req of off wing is applied on APU
    * root config slot.
    *
    *
    *
    */

   @Test
   public void testOPER23629_NoError_OFFWING_APU_ROOT_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      // lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      // lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_ENG_ROOT + "'" );

      lReqMap.put( "ASSMBL_CD", "'" + iREQ_ATA_CD_APU_ROOT + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_APU_ROOT + "'" );
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
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL_2 + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL_2 + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "ENFORCE_WORKSCOPE_ORD_BOOL", "'" + iENFORCE_WORKSCOPE_ORD_BOOL + "'" );
      lReqMap.put( "REQ_INSTRUCTIONS", "'" + iREQ_INSTRUCTIONS + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'N'" );
      lReqMap.put( "TASK_MUST_REMOVE_CD", "'" + iTask_MUST_REMOVE_CD_OFFWING + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // There should not have any error(s)
      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // Verify the validation has no error
      VerifyValidation();

   }


   /**
    * This test is to verify OPER-23629: there is no error when req of on wing is applied on APU
    * root config slot.
    *
    *
    *
    */

   @Test
   public void testOPER23629_NoError_ONWING_APU_ROOT_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iREQ_ATA_CD_APU_ROOT + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_APU_ROOT + "'" );
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
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL_2 + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL_2 + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "ENFORCE_WORKSCOPE_ORD_BOOL", "'" + iENFORCE_WORKSCOPE_ORD_BOOL + "'" );
      lReqMap.put( "REQ_INSTRUCTIONS", "'" + iREQ_INSTRUCTIONS + "'" );
      lReqMap.put( "NEXT_SHOP_VISIT_BOOL", "'N'" );
      lReqMap.put( "TASK_MUST_REMOVE_CD", "'" + iTask_MUST_REMOVE_CD_ONWING + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // There should not have any error(s)
      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // Verify the validation has no error
      VerifyValidation();

   }


   /**
    * This test is to verify OPER-24636: When an invalid character is entered in use_sched_from_bool
    * (not Y, N or blank), validation error code 'CFGREQ-12144' is thrown
    *
    */
   @Test
   public void testOPER24636_Error12144_USE_SCHED_FROM_BOOL_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // Insert data into C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_ACFT + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_1 + "'" );
      lReqMap.put( "LAST_DEADLINE_DRIVER_BOOL", "'" + iLAST_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL_2 + "'" );
      lReqMap.put( "USE_SCHED_FROM_BOOL", "'" + "H" + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // Run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // Validate CFGREQ-12144 error
      validateErrorCode( "CFGREQ-12144" );

   }


   /**
    * This test is to verify OPER-24636: When a valid character is entered in use_sched_from_bool
    * (in this case 'Y'), validation passes.
    *
    */
   @Test
   public void testOPER24636_NoError_USE_SCHED_FROM_BOOL_Y_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // Insert data into C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iREQ_ATA_CD_APU_ROOT + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_APU_ROOT + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_1 + "'" );
      lReqMap.put( "LAST_DEADLINE_DRIVER_BOOL", "'" + iLAST_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_DRIVER_BOOL", "'" + iSOFT_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL_2 + "'" );
      lReqMap.put( "ENFORCE_WORKSCOPE_ORD_BOOL", "'" + iENFORCE_WORKSCOPE_ORD_BOOL + "'" );
      lReqMap.put( "USE_SCHED_FROM_BOOL", "'" + iUSE_SCHED_FROM_BOOL + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // Run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // Verify no validation errors
      VerifyValidation();

   }


   /**
    * This test is to verify OPER-24636: When a valid character is entered in use_sched_from_bool
    * (in this case 'Y'), import passes.
    *
    */
   @Test
   public void testOPER24636_NoError_USE_SCHED_FROM_BOOL_Y_IMPORT() {

      // Run validation
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOPER24636_NoError_USE_SCHED_FROM_BOOL_Y_VALIDATION();

      System.out.println( "Finish validation" );

      // Run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs = getTaskIds( iREQ_TASK_CD_1, iREQ_TASK_NAME_1 );
      Boolean lUseSchedFromBoolValue = getUseSchedFromBool( iTASK_IDs );

      // Verify USE_SCHED_FROM_BOOL is correctly set
      if ( iUSE_SCHED_FROM_BOOL.equals( "Y" ) ) {
         Assert.assertTrue( lUseSchedFromBoolValue );
      } else if ( iUSE_SCHED_FROM_BOOL.equals( "N" ) ) {
         Assert.assertFalse( lUseSchedFromBoolValue );
      }

   }


   /**
    * This test is to verify OPER-24636: When a valid character is entered in use_sched_from_bool
    * (in this case ''), validation passes.
    *
    */
   @Test
   public void testOPER24636_NoError_USE_SCHED_FROM_BOOL_BLANK_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // Insert data into C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iREQ_ATA_CD_APU_ROOT + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_APU_ROOT + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_1 + "'" );
      lReqMap.put( "LAST_DEADLINE_DRIVER_BOOL", "'" + iLAST_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_DRIVER_BOOL", "'" + iSOFT_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL_2 + "'" );
      lReqMap.put( "ENFORCE_WORKSCOPE_ORD_BOOL", "'" + iENFORCE_WORKSCOPE_ORD_BOOL + "'" );
      lReqMap.put( "USE_SCHED_FROM_BOOL", "''" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // Run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // Verify no validation errors
      VerifyValidation();

   }


   /**
    * This test is to verify OPER-24636: When a valid character is entered in use_sched_from_bool
    * (in this case ''), import passes and default value is stored in task_task.
    *
    */
   @Test
   public void testOPER24636_NoError_USE_SCHED_FROM_BOOL_BLANK_IMPORT() {

      // Run validation
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOPER24636_NoError_USE_SCHED_FROM_BOOL_BLANK_VALIDATION();

      System.out.println( "Finish validation" );

      // Run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs = getTaskIds( iREQ_TASK_CD_1, iREQ_TASK_NAME_1 );
      Boolean lUseSchedFromBoolValue = getUseSchedFromBool( iTASK_IDs );

      // Verify USE_SCHED_FROM_BOOL is correctly set
      if ( iUSE_SCHED_FROM_BOOL_DEFAULT_VALUE.equals( "Y" ) ) {
         Assert.assertTrue( lUseSchedFromBoolValue );
      } else if ( iUSE_SCHED_FROM_BOOL_DEFAULT_VALUE.equals( "N" ) ) {
         Assert.assertFalse( lUseSchedFromBoolValue );
      }

   }


   /**
    * This test is to verify OPER-24637: A REQ of type FOLLOW has to be on condition. If
    * ON_CONDITION_BOOL is not Y, validation error code 'CFGREQ-12145' is thrown.
    *
    */
   @Test
   public void testOPER24637_Error12145_ON_CONDITION_BOOL_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // Insert data into C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_ACFT + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_1 + "'" );
      lReqMap.put( "LAST_DEADLINE_DRIVER_BOOL", "'" + iLAST_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_DRIVER_BOOL", "'" + iSOFT_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "REQ_INSTRUCTIONS", "'" + iREQ_INSTRUCTIONS + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD_FOLLOW + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'N'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // Run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // Validate CFGREQ-12145 error
      validateErrorCode( "CFGREQ-12145" );

   }


   /**
    * This test is to verify OPER-24637: A REQ of type FOLLOW has to have all of the following
    * fields set to N or blank:
    *
    * CANCEL_ON_AC_INST_BOOL, CANCEL_ON_ANY_INST_BOOL, CREATE_ON_AC_INST_BOOL,
    * CREATE_ON_ANY_INST_BOOL, CANCEL_ON_AC_RMVL_BOOL, CANCEL_ON_ANY_RMVL_BOOL,
    * CREATE_ON_ANY_RMVL_BOOL, CREATE_ON_AC_RMVL_BOOL
    *
    * If any of these fields are not N or blank, validation error code 'CFGREQ-12146' is thrown.
    *
    */
   @Test
   public void testOPER24637_Error12146_CREATE_CANCEL_ON_INSTALL_REMOVE_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // Insert data into C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_ACFT + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_1 + "'" );
      lReqMap.put( "LAST_DEADLINE_DRIVER_BOOL", "'" + iLAST_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_DRIVER_BOOL", "'" + iSOFT_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "REQ_INSTRUCTIONS", "'" + iREQ_INSTRUCTIONS + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD_FOLLOW + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL_Y + "'" );
      lReqMap.put( "CANCEL_ON_AC_INST_BOOL", "'" + iCANCEL_ON_AC_INST_BOOL_N + "'" );
      lReqMap.put( "CANCEL_ON_ANY_INST_BOOL", "'" + "" + "'" );
      lReqMap.put( "CREATE_ON_AC_INST_BOOL", "'" + "" + "'" );
      lReqMap.put( "CREATE_ON_ANY_INST_BOOL", "'" + "" + "'" );
      lReqMap.put( "CANCEL_ON_AC_RMVL_BOOL", "'" + "" + "'" );
      lReqMap.put( "CANCEL_ON_ANY_RMVL_BOOL", "'" + "" + "'" );
      lReqMap.put( "CREATE_ON_ANY_RMVL_BOOL", "'" + "" + "'" );
      lReqMap.put( "CREATE_ON_AC_RMVL_BOOL", "'" + iCREATE_ON_AC_RMVL_BOOL_Y + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // Run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // Validate CFGREQ-12146 error
      validateErrorCode( "CFGREQ-12146" );

   }


   /**
    * This test is to verify OPER-24638: A REQ of type REPREF has to be on condition. If
    * ON_CONDITION_BOOL is not Y, validation error code 'CFGREQ-12147' is thrown.
    *
    */
   @Test
   public void testOPER24638_Error12147_ON_CONDITION_BOOL_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // Insert data into C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_ACFT + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_1 + "'" );
      lReqMap.put( "LAST_DEADLINE_DRIVER_BOOL", "'" + iLAST_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_DRIVER_BOOL", "'" + iSOFT_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "REQ_INSTRUCTIONS", "'" + iREQ_INSTRUCTIONS + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD_REPREF + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'N'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // Run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // Validate CFGREQ-12147 error
      validateErrorCode( "CFGREQ-12147" );

   }


   /**
    * This test is to verify OPER-24638: A REQ of type REPREF has to have all of the following
    * fields set to N or blank:
    *
    * CANCEL_ON_AC_INST_BOOL, CANCEL_ON_ANY_INST_BOOL, CREATE_ON_AC_INST_BOOL,
    * CREATE_ON_ANY_INST_BOOL, CANCEL_ON_AC_RMVL_BOOL, CANCEL_ON_ANY_RMVL_BOOL,
    * CREATE_ON_ANY_RMVL_BOOL, CREATE_ON_AC_RMVL_BOOL
    *
    * If any of these fields are not N or blank, validation error code 'CFGREQ-12148' is thrown.
    *
    */
   @Test
   public void testOPER24638_Error12148_CREATE_CANCEL_ON_INSTALL_REMOVE_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // Insert data into C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_ACFT + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_1 + "'" );
      lReqMap.put( "LAST_DEADLINE_DRIVER_BOOL", "'" + iLAST_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_DRIVER_BOOL", "'" + iSOFT_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "REQ_INSTRUCTIONS", "'" + iREQ_INSTRUCTIONS + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD_REPREF + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL_Y + "'" );
      lReqMap.put( "CANCEL_ON_AC_INST_BOOL", "'" + iCANCEL_ON_AC_INST_BOOL_N + "'" );
      lReqMap.put( "CANCEL_ON_ANY_INST_BOOL", "'" + "" + "'" );
      lReqMap.put( "CREATE_ON_AC_INST_BOOL", "'" + "" + "'" );
      lReqMap.put( "CREATE_ON_ANY_INST_BOOL", "'" + "" + "'" );
      lReqMap.put( "CANCEL_ON_AC_RMVL_BOOL", "'" + "" + "'" );
      lReqMap.put( "CANCEL_ON_ANY_RMVL_BOOL", "'" + "" + "'" );
      lReqMap.put( "CREATE_ON_ANY_RMVL_BOOL", "'" + "" + "'" );
      lReqMap.put( "CREATE_ON_AC_RMVL_BOOL", "'" + iCREATE_ON_AC_RMVL_BOOL_Y + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // Run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // Validate CFGREQ-12148 error
      validateErrorCode( "CFGREQ-12148" );

   }


   /**
    * This test is to verify OPER-24638: A REQ of type REPREF has to have workscope bool as true. If
    * WORKSCOPE_BOOL is not Y, validation error code 'CFGREQ-12149' is thrown.
    *
    */
   @Test
   public void testOPER24638_Error12149_WORKSCOPE_BOOL_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // Insert data into C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_ACFT + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_1 + "'" );
      lReqMap.put( "LAST_DEADLINE_DRIVER_BOOL", "'" + iLAST_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL_2 + "'" );
      lReqMap.put( "SOFT_DEADLINE_DRIVER_BOOL", "'" + iSOFT_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "REQ_INSTRUCTIONS", "'" + iREQ_INSTRUCTIONS + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD_REPREF + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'Y'" );
      lReqMap.put( "enforce_workscope_ord_bool", "'N'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // Run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // Validate CFGREQ-12149 error
      validateErrorCode( "CFGREQ-12149" );

   }


   /**
    *
    * This test is to verify OPER-25084: There is no error when validating a requirement of class
    * FOLLOW.
    *
    */
   @Test
   public void testOPER25084_PREVENT_MANUAL_INIT_BOOL_FALSE_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // Insert data into map
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_ACFT + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_1 + "'" );
      lReqMap.put( "LAST_DEADLINE_DRIVER_BOOL", "'" + iLAST_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_DRIVER_BOOL", "'" + iSOFT_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL_Y + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD_FOLLOW + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // Insert map into C_REQ
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // Run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    *
    * This test is to verify OPER-25084: When importing a requirement of class FOLLOW, the
    * 'prevent_manual_init_bool' in the table 'task_defn' has a value of 0 (false).
    *
    */
   @Test
   public void testOPER25084_PREVENT_MANUAL_INIT_BOOL_FALSE_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOPER25084_PREVENT_MANUAL_INIT_BOOL_FALSE_VALIDATION();

      System.out.println( "Finish validation" );

      // Run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs = getTaskIds( iREQ_TASK_CD_1, iREQ_TASK_NAME_1 );

      // Verify 'prevent_manual_init_bool' is correctly set
      Assert.assertFalse(
            "The 'prevent_manual_init_bool' should have been set to 0 (false) in the 'task_defn' table.",
            getPreventManualInitBool( iTASK_IDs ) );

   }


   /**
    *
    * This test is to verify OPER-25084: There is no error when validating a requirement of class
    * REPREF.
    *
    */
   @Test
   public void testOPER25084_PREVENT_MANUAL_INIT_BOOL_TRUE_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // Insert data into map
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_ACFT + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_1 + "'" );
      lReqMap.put( "LAST_DEADLINE_DRIVER_BOOL", "'" + iLAST_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_DRIVER_BOOL", "'" + iSOFT_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL_Y + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD_REPREF + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // Insert map into C_REQ
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );

      // Run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    *
    * This test is to verify OPER-25084: When importing a requirement of class REPREF, the
    * 'prevent_manual_init_bool' in the table 'task_defn' has a value of 1 (true).
    *
    */
   @Test
   public void testOPER25084_PREVENT_MANUAL_INIT_BOOL_TRUE_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOPER25084_PREVENT_MANUAL_INIT_BOOL_TRUE_VALIDATION();

      System.out.println( "Finish validation" );

      // Run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs = getTaskIds( iREQ_TASK_CD_1, iREQ_TASK_NAME_1 );

      // Verify 'prevent_manual_init_bool' is correctly set
      Assert.assertTrue(
            "The 'prevent_manual_init_bool' should have been set to 1 (true) in the 'task_defn' table.",
            getPreventManualInitBool( iTASK_IDs ) );

   }


   /**
    *
    * This function is used to determine the value of the column use_sched_from_bool in the
    * task_task table.
    *
    * @param aSimpleID
    *           - Task ID
    * @return - True if use_sched_from_bool is true or false if use_sched_from_bool is false
    *
    */
   private Boolean getUseSchedFromBool( simpleIDs aSimpleID ) {

      WhereClause lWhereClauseArguments = new WhereClause();
      lWhereClauseArguments.addArguments( "TASK_DB_ID", aSimpleID.getNO_DB_ID() );
      lWhereClauseArguments.addArguments( "TASK_ID", aSimpleID.getNO_ID() );

      List<String> lWantedFields = Arrays.asList( "USE_SCHED_FROM_BOOL" );

      String iQueryString =
            TableUtil.buildTableQuery( TableUtil.TASK_TASK, lWantedFields, lWhereClauseArguments );
      List<ArrayList<String>> lExecutedQuery = execute( iQueryString, lWantedFields );

      System.out.println( lExecutedQuery.get( 0 ).get( 0 ) );

      return lExecutedQuery.get( 0 ).get( 0 ).equals( "1" );
   }


   /**
    *
    * This function is used to return all the fields from task_rep_ref.
    *
    * @param aSimpleID
    *           - Task ID
    * @return - Fields from task_rep_ref table
    *
    */
   private List<ArrayList<String>> getTaskRepRefFields( simpleIDs aSimpleID ) {

      WhereClause lWhereClauseArguments = new WhereClause();
      lWhereClauseArguments.addArguments( "TASK_DB_ID", aSimpleID.getNO_DB_ID() );
      lWhereClauseArguments.addArguments( "TASK_ID", aSimpleID.getNO_ID() );

      List<String> lWantedFields = Arrays.asList( "MOC_APPROVAL_BOOL", "DAMAGE_RECORD_BOOL",
            "DAMAGED_COMPONENT_BOOL", "OPS_RESTRICTIONS_LDESC", "PERF_PENALTIES_LDESC" );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_REP_REF, lWantedFields,
            lWhereClauseArguments );
      return execute( iQueryString, lWantedFields );

   }


   /**
    *
    * This method is used to convert the Y/N constants used in the staging tables into 1/0 used in
    * the database.
    *
    * @param aValue
    *           - The value of the constant.
    * @return 1 if the value passed in is Y, 0 if the value passed in is N, and NULL if anything
    *         else.
    */
   private String convertYTo1AndNTo0( String aValue ) {
      if ( aValue.equals( "Y" ) ) {
         return "1";
      } else if ( aValue.equals( "N" ) ) {
         return "0";
      }
      return null;
   }


   /**
    *
    * This function is used to determine the value of the column prevent_manual_init_bool in the
    * task_defn table.
    *
    * @param aSimpleID
    *           - Task ID
    * @return - True if prevent_manual_init_bool is true or false if prevent_manual_init_bool is
    *         false
    *
    */
   private Boolean getPreventManualInitBool( simpleIDs aSimpleID ) {

      WhereClause lWhereClauseArguments = new WhereClause();
      lWhereClauseArguments.addArguments( "TASK_DEFN_DB_ID", aSimpleID.getNO_DB_ID() );
      lWhereClauseArguments.addArguments( "TASK_DEFN_ID", aSimpleID.getNO_ID() );

      List<String> lWantedFields = Arrays.asList( "PREVENT_MANUAL_INIT_BOOL" );

      String iQueryString =
            TableUtil.buildTableQuery( TableUtil.TASK_DEFN, lWantedFields, lWhereClauseArguments );
      List<ArrayList<String>> lExecutedQuery = execute( iQueryString, lWantedFields );

      System.out.println( lExecutedQuery.get( 0 ).get( 0 ) );

      return lExecutedQuery.get( 0 ).get( 0 ).equals( "1" );

   }


   // ======================================
   /**
    * This function is to set all IDs are null.
    *
    *
    */
   public void restoreIDs() {
      iTASK_IDs = null;
      iTASK_DEFN_IDs = null;
      iTASK_IDs_REPL = null;
      iTASK_DEFN_IDs_REPL = null;
      iTASK_IDs_TRSFRM = null;
      iTASK_DEFN_IDs_TRSFRM = null;
   }


   /**
    * This function is to insert data into c_req table with transform data.
    *
    *
    */
   public void insertTable_C_REQ_transform() {

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_Interchangable + "'" );
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
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL_2 + "'" );
      lReqMap.put( "WORKSCOPE_BOOL", "'" + iWORKSCOPE_BOOL_2 + "'" );
      lReqMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lReqMap.put( "ENFORCE_WORKSCOPE_ORD_BOOL", "'" + iENFORCE_WORKSCOPE_ORD_BOOL + "'" );
      lReqMap.put( "REQ_INSTRUCTIONS", "'" + iREQ_INSTRUCTIONS + "'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ, lReqMap ) );
   }


   /**
    * This function is to insert data into c_req_part_transform table.
    *
    *
    */
   public void insertTable_C_REQ_PART_transform() {

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ_PART_TRANSFORM
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "IPC_REF_CD", "'" + iREQ_ATA_CD_Interchangable + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_3 + "'" );
      lReqMap.put( "OLD_PART_NO_OEM", "'" + iPART_NO_OEM_TRK_old + "'" );
      lReqMap.put( "OLD_MANUFACT_CD", "'" + iMANUFACT_CD_TRK_old + "'" );
      lReqMap.put( "NEW_PART_NO_OEM", "'" + iPART_NO_OEM_TRK_new + "'" );
      lReqMap.put( "NEW_MANUFACT_CD", "'" + iMANUFACT_CD_TRK_new + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_PART_TRANSFORM, lReqMap ) );
   }


   /**
    * This function is to insert data into C_REQ_IETM_TOPIC table.
    *
    *
    */

   public void insertTable_C_REQ_IETM_TOPIC() {
      // C_REQ_IETM_TOPIC
      Map<String, String> lReqMap = new LinkedHashMap<>();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "REQ_IETM_CD", "'" + iREQ_IETM_CD_1 + "'" );
      lReqMap.put( "REQ_TOPIC_SDESC", "'" + iREQ_TOPIC_SDESC_1 + "'" );
      lReqMap.put( "REQ_IETM_ORD", "1" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_IETM_TOPIC, lReqMap ) );

      // Second record
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "REQ_IETM_CD", "'" + iREQ_IETM_CD_2 + "'" );
      lReqMap.put( "REQ_TOPIC_SDESC", "'" + iREQ_TOPIC_SDESC_2 + "'" );
      lReqMap.put( "REQ_IETM_ORD", "2" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_IETM_TOPIC, lReqMap ) );

   }


   /**
    * This function is to insert data into C_REQ_JIC table.
    *
    *
    */

   public void insertTable_C_REQ_JIC() {

      // C_REQ_JIC
      Map<String, String> lReqMap = new LinkedHashMap<>();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );

      lReqMap.put( "JIC_ATA_CD", "'" + iJIC_ATA_CD_TRK + "'" );
      lReqMap.put( "JIC_TASK_CD", "'" + iJIC_TASK_CD_TRK + "'" );
      lReqMap.put( "REQ_JIC_ORDER", "1" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_JIC, lReqMap ) );
   }


   /**
    * This function is to insert data into C_REQ_ADVISORY table.
    *
    *
    */

   public void insertTable_C_REQ_ADVISORY() {
      // C_REQ_ADVISORY
      Map<String, String> lReqMap = new LinkedHashMap<>();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );

      lReqMap.put( "ADVSRY_TYPE_CD", "'" + iADVSRY_TYPE_CD + "'" );
      lReqMap.put( "ROLE_CD", "'" + iROLE_CD + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_ADVISORY, lReqMap ) );
   }


   /**
    * This function is to insert data into C_REQ_IMPACT table.
    *
    *
    */
   public void insertTable_C_REQ_IMPACT() {

      // C_REQ_IMPACT
      Map<String, String> lReqMap = new LinkedHashMap<>();

      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );

      lReqMap.put( "IMPACT_CD", "'" + iIMPACT_CD + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_IMPACT, lReqMap ) );
   }


   /**
    * This function is to insert data into C_REQ_REPL table.
    *
    *
    */
   public void insertTable_C_REQ_REPL() {

      // C_REQ_REPL
      Map<String, String> lReqMap = new LinkedHashMap<>();

      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "IPC_REF_CD", "'" + iREQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_2 + "'" );
      lReqMap.put( "REQ_TASK_NAME", "'" + iREQ_TASK_NAME_2 + "'" );
      lReqMap.put( "REQ_TASK_DESC", "'" + iREQ_TASK_DESC_2 + "'" );
      lReqMap.put( "REQ_TASK_REF_SDESC", "'" + iREQ_TASK_REF_SDESC_2 + "'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_REPL, lReqMap ) );
   }


   /**
    * This function is to verify Task_DEFN table
    *
    *
    */

   public void verifyTaskDEFN( simpleIDs aIds ) {

      String lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + aIds.getNO_DB_ID() + " and TASK_DEFN_ID=" + aIds.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );
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
    * This function is called by after test method to restore data which has been modified by tests.
    *
    *
    */

   public void RestoreData() {

      String lStrDelete = null;

      if ( iTASK_IDs_15067 != null ) {

         // delete task_sched_rule
         lStrDelete = "delete from " + TableUtil.TASK_SCHED_RULE + "  where TASK_DB_ID="
               + iTASK_IDs_15067.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_15067.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_task_dep
         lStrDelete = "delete from " + TableUtil.TASK_TASK_DEP + "  where TASK_DB_ID="
               + iTASK_IDs_15067.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_15067.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_interval
         lStrDelete = "delete from " + TableUtil.TASK_INTERVAL + "  where TASK_DB_ID="
               + iTASK_IDs_15067.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_15067.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_task
         lStrDelete = "delete from " + TableUtil.TASK_TASK + "  where TASK_DB_ID="
               + iTASK_IDs_15067.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_15067.getNO_ID();
         executeSQL( lStrDelete );

      }

      if ( iTASK_DEFN_IDs_15067 != null ) {
         lStrDelete =
               "delete from TASK_DEFN where TASK_DEFN_DB_ID=" + iTASK_DEFN_IDs_15067.getNO_DB_ID()
                     + " and TASK_DEFN_ID=" + iTASK_DEFN_IDs_15067.getNO_ID();
         executeSQL( lStrDelete );

      }

      if ( iTASK_IDs != null ) {

         // delete task_impact
         lStrDelete = "delete from " + TableUtil.TASK_IMPACT + "  where TASK_DB_ID="
               + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_advisory
         lStrDelete = "delete from " + TableUtil.TASK_ADVISORY + "  where TASK_DB_ID="
               + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_part_transform
         lStrDelete = "delete from " + TableUtil.TASK_PART_TRANSFORM + "  where TASK_DB_ID="
               + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_task_ietm
         lStrDelete = "delete from " + TableUtil.TASK_TASK_IETM + "  where TASK_DB_ID="
               + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_sched_rule
         lStrDelete = "delete from " + TableUtil.TASK_SCHED_RULE + "  where TASK_DB_ID="
               + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_task_dep
         lStrDelete = "delete from " + TableUtil.TASK_TASK_DEP + "  where TASK_DB_ID="
               + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_interval
         lStrDelete = "delete from " + TableUtil.TASK_INTERVAL + "  where TASK_DB_ID="
               + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_work_type
         lStrDelete = "delete from " + TableUtil.TASK_WORK_TYPE + "  where TASK_DB_ID="
               + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_task
         lStrDelete = "delete from " + TableUtil.TASK_TASK + "  where TASK_DB_ID="
               + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID();
         executeSQL( lStrDelete );

      }

      if ( iTASK_DEFN_IDs != null ) {

         // Delete task_JIC_REQ_MAP
         lStrDelete = "delete from TASK_JIC_REQ_MAP where REQ_TASK_DEFN_DB_ID="
               + iTASK_DEFN_IDs.getNO_DB_ID() + " and REQ_TASK_DEFN_ID="
               + iTASK_DEFN_IDs.getNO_ID();
         executeSQL( lStrDelete );

         // Delete task_defn
         lStrDelete = "delete from TASK_DEFN where TASK_DEFN_DB_ID=" + iTASK_DEFN_IDs.getNO_DB_ID()
               + " and TASK_DEFN_ID=" + iTASK_DEFN_IDs.getNO_ID();
         executeSQL( lStrDelete );

      }
      // =========================================================================================

      if ( iTASK_IDs_REPL != null ) {

         // delete task_task
         lStrDelete = "delete from " + TableUtil.TASK_TASK + "  where TASK_DB_ID="
               + iTASK_IDs_REPL.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_REPL.getNO_ID();
         executeSQL( lStrDelete );

      }

      if ( iTASK_DEFN_IDs_REPL != null ) {

         // Delete task_defn
         lStrDelete =
               "delete from TASK_DEFN where TASK_DEFN_DB_ID=" + iTASK_DEFN_IDs_REPL.getNO_DB_ID()
                     + " and TASK_DEFN_ID=" + iTASK_DEFN_IDs_REPL.getNO_ID();
         executeSQL( lStrDelete );

      }

      // ==========================================================================================
      if ( iTASK_IDs_TRSFRM != null ) {

         // delete task_part_transform
         lStrDelete = "delete from " + TableUtil.TASK_PART_TRANSFORM + "  where TASK_DB_ID="
               + iTASK_IDs_TRSFRM.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_TRSFRM.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_work_type
         lStrDelete = "delete from " + TableUtil.TASK_WORK_TYPE + "  where TASK_DB_ID="
               + iTASK_IDs_TRSFRM.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_TRSFRM.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_task
         lStrDelete = "delete from " + TableUtil.TASK_TASK + "  where TASK_DB_ID="
               + iTASK_IDs_TRSFRM.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_TRSFRM.getNO_ID();
         executeSQL( lStrDelete );

      }

      if ( iTASK_DEFN_IDs_TRSFRM != null ) {

         // Delete task_defn
         lStrDelete =
               "delete from TASK_DEFN where TASK_DEFN_DB_ID=" + iTASK_DEFN_IDs_TRSFRM.getNO_DB_ID()
                     + " and TASK_DEFN_ID=" + iTASK_DEFN_IDs_TRSFRM.getNO_ID();
         executeSQL( lStrDelete );

      }

   }


   /**
    * This function is to check error(s) for validation table is loaded
    *
    *
    */
   public void VerifyValidation() {

      int lCount = countRowsOfQuery( TestConstants.C_REQ_ERROR_CHECK_TABLE );
      Assert.assertTrue( "There should be no error. ", lCount == 0 );

      lCount = countRowsOfQuery( TestConstants.C_REQ_DYNAMIC_DEADLINE_ERROR_CHECK_TABLE );
      Assert.assertTrue( "There should be no error. ", lCount == 0 );

      lCount = countRowsOfQuery( TestConstants.C_REQ_DYNAMIC_PART_DEADLINE_ERROR_CHECK_TABLE );
      Assert.assertTrue( "There should be no error. ", lCount == 0 );

      lCount = countRowsOfQuery( TestConstants.C_REQ_IETM_TOPIC_ERROR_CHECK_TABLE );
      Assert.assertTrue( "There should be no error. ", lCount == 0 );

      lCount = countRowsOfQuery( TestConstants.C_REQ_JIC_ERROR_CHECK_TABLE );
      Assert.assertTrue( "There should be no error. ", lCount == 0 );

      lCount = countRowsOfQuery( TestConstants.C_REQ_PART_TRANSFORM_ERROR_CHECK_TABLE );
      Assert.assertTrue( "There should be no error. ", lCount == 0 );

      lCount = countRowsOfQuery( TestConstants.C_REQ_REPL_ERROR_CHECK_TABLE );
      Assert.assertTrue( "There should be no error. ", lCount == 0 );

      lCount = countRowsOfQuery( TestConstants.C_REQ_STANDARD_DEADLINE_ERROR_CHECK_TABLE );
      Assert.assertTrue( "There should be no error. ", lCount == 0 );

      lCount = countRowsOfQuery( TestConstants.C_REQ_ADVISORY_ERROR_CHECK_TABLE );
      Assert.assertTrue( "There should be no error. ", lCount == 0 );

      lCount = countRowsOfQuery( TestConstants.C_REQ_IMPACT_ERROR_CHECK_TABLE );
      Assert.assertTrue( "There should be no error. ", lCount == 0 );

   }


   /**
    * This function is to implement interface ValidationAndImport
    *
    * @param: blnOnlyValidation
    * @param: allornone
    *
    * @return: return code of Int
    *
    */
   public int runValidationAndImport( boolean ablnOnlyValidation, boolean allornone ) {
      int lrtValue = 0;
      ivalidationandimport = new ValidationAndImport() {

         @Override
         public int runValidation( boolean allornone ) {
            int lReturn = 0;
            CallableStatement lPrepareCallJICPART;

            try {

               lPrepareCallJICPART = getConnection()
                     .prepareCall( "BEGIN  req_import.validate_req(on_retcode =>?); END;" );

               lPrepareCallJICPART.registerOutParameter( 1, Types.INTEGER );
               lPrepareCallJICPART.execute();
               commit();
               lReturn = lPrepareCallJICPART.getInt( 1 );
            } catch ( SQLException e ) {
               e.printStackTrace();
            }
            return lReturn;

         }


         @Override
         public int runImport( boolean allornone ) {
            int lReturn = 0;
            CallableStatement lPrepareCallKIT;

            try {

               lPrepareCallKIT = getConnection()
                     .prepareCall( "BEGIN req_import.import_req(on_retcode =>?); END;" );

               lPrepareCallKIT.registerOutParameter( 1, Types.INTEGER );

               lPrepareCallKIT.execute();
               commit();
               lReturn = lPrepareCallKIT.getInt( 1 );
            } catch ( SQLException e ) {
               e.printStackTrace();
            }
            return lReturn;
         }

      };

      lrtValue = ablnOnlyValidation ? ivalidationandimport.runValidation( allornone )
            : ivalidationandimport.runImport( allornone );

      return lrtValue;
   }


   public void insertTable_C_REQ_DYNAMIC_DEADLINE() {

      Map<String, String> lReqMap = new LinkedHashMap<>();
      // C_REQ_DYNAMIC_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_THRESHOLD_QT", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REQ_DYNAMIC_DEADLINE, lReqMap ) );
   }


   public void insertTable_C_REQ_DYNAMIC_PART_DEADLINE() {

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ_DYNAMIC_PART_DEADLINE
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_1 + "'" );
      lReqMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lReqMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK + "'" );
      lReqMap.put( "SCHED_DATA_TYPE_CD", "'" + iSCHED_DATA_TYPE_CD + "'" );
      lReqMap.put( "SCHED_THRESHOLD_QT", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_REQ_DYNAMIC_PART_DEADLINE, lReqMap ) );
   }


   /**
    *
    * Used to verify the following tables: task_part_transform, task_task, task_DEFN,
    * task_work_type, task_task_ietm, task_jic_req_map, task_sched_rule, task_interval,
    * task_advisory, task_impact
    *
    * This is because the following staging tables were populated: C_REQ_PART_TRANSFORM,
    * C_REQ_IETM_TOPIC, C_REQ_JIC, C_REQ_ADVISORY, C_REQ_IMPACT, C_REQ_REPL
    */
   public void verifyOtherReqTables( simpleIDs aTASK_IDs ) {
      restoreIDs();

      // Verify task_task table
      // task A
      iTASK_IDs = aTASK_IDs;
      assmbleInfor lassmblinfor = getassmblInfor( iREQ_ATA_CD_TRK );

      iTASK_DEFN_IDs = verifyTaskTask_15067( iTASK_IDs, iCLASS_CD, lassmblinfor, iSUBCLASS_CD,
            iORIGINATOR_CD, iREQ_TASK_CD_1, iREQ_TASK_NAME_1, iREQ_TASK_DESC_1, iREQ_INSTRUCTIONS,
            iREQ_TASK_REF_SDESC_1, iAPPLICABILITY_DESC, iLAST_DEADLINE_DRIVER_BOOL_N,
            iSOFT_DEADLINE_DRIVER_BOOL_N, iWORKSCOPE_BOOL_2_N, null, null, null, null );

      // task B : repl
      iTASK_IDs_REPL = getTaskIds( iREQ_TASK_CD_2, iREQ_TASK_NAME_2 );
      iTASK_DEFN_IDs_REPL = verifyTaskTask_REPL( iTASK_IDs_REPL, iCLASS_CD_2, lassmblinfor,
            iREQ_TASK_CD_2, iREQ_TASK_NAME_2, iREQ_TASK_DESC_2, iREQ_TASK_REF_SDESC_2 );

      // task C : transform
      iTASK_IDs_TRSFRM = getTaskIds( iREQ_TASK_CD_3, iREQ_TASK_NAME_3 );
      iTASK_DEFN_IDs_TRSFRM = verifyTaskTask_TRSFRM( iTASK_IDs_TRSFRM, iCLASS_CD, lassmblinfor,
            iSUBCLASS_CD, iORIGINATOR_CD, iREQ_TASK_CD_3, iREQ_TASK_NAME_3, iREQ_TASK_DESC_3,
            iREQ_INSTRUCTIONS, iREQ_TASK_REF_SDESC_3, iAPPLICABILITY_DESC,
            iLAST_DEADLINE_DRIVER_BOOL_N, iSOFT_DEADLINE_DRIVER_BOOL_N, iWORKSCOPE_BOOL_2_N, null,
            null, null, null );

      // verify task_DEFN
      // task A
      verifyTaskDEFN( iTASK_DEFN_IDs );

      // task B : repl
      verifyTaskDEFN( iTASK_DEFN_IDs_REPL );

      // task C: transform
      verifyTaskDEFN( iTASK_DEFN_IDs_TRSFRM );

      // verify task_work_type
      verifyWorkType( iTASK_IDs, iWORK_TYPE_LIST_1 );
      verifyWorkType( iTASK_IDs, iWORK_TYPE_LIST_2 );
      verifyWorkType( iTASK_IDs_TRSFRM, iWORK_TYPE_LIST_1 );
      verifyWorkType( iTASK_IDs_TRSFRM, iWORK_TYPE_LIST_2 );

      // verify task_sched_rule table
      simpleIDs lTypeIds = getTypeIds( "HOURS" );
      verifyTASKSCHEDRULE_15067( iTASK_IDs, lTypeIds, iSCHED_THRESHOLD_QT, null );

      // verify task_interval data from c_req_dynamic_part_deadline
      // simpleIDs lPartIds = getPartIds( iPART_NO_OEM_TRK, iMANUFACT_CD_TRK );
      // verifyTASKINTERVAL_15067( iTASK_IDs, lTypeIds, lPartIds, iSCHED_THRESHOLD_QT, null );

      // very task_task_ietm
      simpleIDs lietmIds_1 = getIetmIds( iREQ_TOPIC_SDESC_1 );
      verifyTASKTASKIETM( iTASK_IDs, "1", lietmIds_1 );

      simpleIDs lietmIds_2 = getIetmIds( iREQ_TOPIC_SDESC_2 );
      verifyTASKTASKIETM( iTASK_IDs, "2", lietmIds_2 );

      // verify task_jic_req_map
      simpleIDs lJicIds = getReqIds( iJIC_TASK_CD_TRK, "JIC" );
      String lQuery = "select 1 from " + TableUtil.TASK_JIC_REQ_MAP + " where REQ_TASK_DEFN_DB_ID="
            + iTASK_DEFN_IDs.getNO_DB_ID() + " and REQ_TASK_DEFN_ID=" + iTASK_DEFN_IDs.getNO_ID()
            + "and JIC_TASK_DB_ID=" + lJicIds.getNO_DB_ID() + " and JIC_TASK_ID="
            + lJicIds.getNO_ID();
      Assert.assertTrue( "Check TASK_JIC_REQ_MAP table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_advisory
      String lRoleId = getRoleIds( iROLE_CD );
      lQuery = "select 1 from " + TableUtil.TASK_ADVISORY + " where TASK_DB_ID="
            + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID() + " and ROLE_ID="
            + lRoleId + " and TASK_ADVISORY_TYPE_CD='" + iADVSRY_TYPE_CD + "'";
      Assert.assertTrue( "Check TASK_ADVISORY table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_impact
      simpleIDs lImpactIds = getIMPACTIds( iIMPACT_CD );
      lQuery = "select 1 from " + TableUtil.TASK_IMPACT + " where IMPACT_DB_ID="
            + lImpactIds.getNO_DB_ID() + " and IMPACT_CD='" + lImpactIds.getNO_ID() + "'"
            + " and TASK_DB_ID=" + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID();
      Assert.assertTrue( "Check TASK_IMPACT table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_part_transform
      simpleIDs lID_old = getPartNoIds( iPART_NO_OEM_TRK_old, iMANUFACT_CD_TRK_old );
      simpleIDs lID_new = getPartNoIds( iPART_NO_OEM_TRK_new, iMANUFACT_CD_TRK_new );
      verifyTASKPARTTRANSFORM( iTASK_IDs_TRSFRM, lID_old, lID_new );
      iTASK_IDs = null;
   }

}
