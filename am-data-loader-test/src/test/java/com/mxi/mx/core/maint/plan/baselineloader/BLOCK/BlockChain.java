package com.mxi.mx.core.maint.plan.baselineloader.BLOCK;

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
import com.mxi.mx.util.BlockTest;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.ValidationAndImport;
import com.mxi.mx.util.WhereClause;


/**
 * This test suite contains test cases on validation functionality of C_BLOCK_REQ and
 * c_block_chain_task_details.
 */
public class BlockChain extends BlockTest {

   @Rule
   public TestName testName = new TestName();
   ValidationAndImport ivalidationandimport;

   public String iACFT_ASSMBLCD = "ACFT_CD1";
   public String iBLOCK_ATA_CD_TRK = "ACFT-SYS-1-1-TRK-BATCH-PARENT";
   public String iBLOCK_TASK_CD_1 = "ATEST";
   public String iREQ_ATA_CD_TRK = "ACFT-SYS-1-1-TRK-BATCH-PARENT";
   public String iREQ_TASK_CD_TRK = "DUP-TRK";
   public String iREQ_TASK_CD_TRK_RECURRING = "AT BLCKCHN";

   // public String iBLOCK_TASK_CD_RECURRING = "B-RECURRING-TEST";

   // C_BLOCK table
   public String iBLOCK_TASK_NAME_1 = "ATESTNAME";
   public String iBLOCK_TASK_DESC_1 = "ATESTDESC";
   public String iBLOCK_TASK_REF_SDESC_1 = "ATESTREFDESC";

   // non recurring chain=======================================================
   public String iBLOCK_CHAIN_SDESC_CHAIN_NON_RECURRING = "ACHAIN-NON-RECURRING";
   public String iBLOCK_TASK_CD_NON_RECURRING_1 = "A-NON-RECURRING-TEST1";
   public String iBLOCK_TASK_NAME_CHAIN_NON_RECURRING_1 = "A-NON-RECURRING-TEST1";
   public String iBLOCK_TASK_DESC_CHAIN_NON_RECURRING_1 = "A-NON-RECURRING-TEST1";

   public String iBLOCK_TASK_CD_NON_RECURRING_2 = "A-NON-RECURRING-TEST2";
   public String iBLOCK_TASK_NAME_CHAIN_NON_RECURRING_2 = "A-NON-RECURRING-TEST2";

   public String iBLOCK_TASK_CD_NON_RECURRING_3 = "A-NON-RECURRING-TEST3";
   public String iBLOCK_TASK_NAME_CHAIN_NON_RECURRING_3 = "A-NON-RECURRING-TEST3";

   // recurring chain
   public String iBLOCK_CHAIN_SDESC_CHAIN_RECURRING = "BCHAIN-RECURRING";
   public String iBLOCK_TASK_CD_RECURRING_1 = "B-RECURRING-TEST1";
   public String iBLOCK_TASK_NAME_CHAIN_RECURRING_1 = "B-RECURRING-TEST1";
   public String iBLOCK_TASK_DESC_CHAIN_RECURRING_1 = "B-RECURRING-TEST1";

   public String iBLOCK_TASK_CD_RECURRING_2 = "B-RECURRING-TEST2";
   public String iBLOCK_TASK_NAME_CHAIN_RECURRING_2 = "B-RECURRING-TEST2";

   public String iBLOCK_TASK_CD_RECURRING_3 = "B-RECURRING-TEST3";
   public String iBLOCK_TASK_NAME_CHAIN_RECURRING_3 = "B-RECURRING-TEST3";
   // ==============================================================================

   public String iCLASS_CD = "CHECK";
   public String iCLASS_CD_BLOCK = "BLOCK";
   public String iCLASS_CD_COMP = "REQ";
   public String iSUBCLASS_CD = "A-CHECK";
   public String iSUBCLASS_CD_COMP = "TEST2";
   public String iORIGINATOR_CD = "AWL";

   public String iAPPLICABILITY_DESC = "1,5-7";
   public String iRESCHED_FROM_CD = "EXECUTE";
   public String iLAST_DEADLINE_DRIVER_BOOL = "Y";
   public String iLAST_DEADLINE_DRIVER_BOOL_N = "1";
   public String iSOFT_DEADLINE_DRIVER_BOOL = "Y";
   public String iSOFT_DEADLINE_DRIVER_BOOL_N = "1";
   public String iSCHED_FROM_MANUFACT_DT_BOOL = "Y";
   public String iSCHED_FROM_MANUFACT_DT_BOOL_N = "1";
   public String iWORKSCOPE_BOOL = "Y";
   public String iWORKSCOPE_BOOL_N = "1";
   public String iTASK_MUST_REMOVE_CD = "OFFPARENT";
   public String iON_CONDITION_BOOL_N = "N";
   public String iON_CONDITION_BOOL_N_number = "0";
   public String iON_CONDITION_BOOL_Y = "Y";
   public String iON_CONDITION_BOOL_Y_number = "1";

   public String iSCHED_THRESHOLD_QT = "100";
   public String iSCHED_INTERVAL_QT = "10";
   public String iSCHED_DATA_TYPE_CD = "HOURS";
   public String iSCHED_DATA_TYPE_CD_ENG = "EOT";


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
    * This test is to verify validation functionality on block_import of block chain requirement
    * with start=1, interval=1. total # of block=3.
    *
    */

   @Test
   public void testBLOCK_NON_RECURING_CHAIN_11_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      prepareData_non_recurring();

      // C_BLOCK_REQ
      Map<String, String> lReqMap = new LinkedHashMap<>();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_NON_RECURRING_1 + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_TRK + "'" );
      lReqMap.put( "BLOCK_CHAIN_START", "'1'" );
      lReqMap.put( "BLOCK_CHAIN_INTERVAL", "'1'" );

      // Insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_REQ, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify import functionality on block_import of block chain requirement with
    * start=1, interval=1. total # of block=3.
    *
    */

   @Test
   public void testBLOCK_NON_RECURING_CHAIN_11_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testBLOCK_NON_RECURING_CHAIN_11_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      resetTaskIds();

      verifyNonRecurring();

      // Verify task_block_req_map
      String lQuery =
            "select TASK_DEFN_DB_ID, TASK_DEFN_ID from task_task " + "inner join eqp_assmbl_bom on "
                  + "eqp_assmbl_bom.assmbl_db_id=task_task.assmbl_db_id and "
                  + "eqp_assmbl_bom.assmbl_cd=task_task.assmbl_cd and "
                  + "eqp_assmbl_bom.assmbl_bom_id = task_task.assmbl_bom_id "
                  + "where task_task.task_cd='" + iREQ_TASK_CD_TRK
                  + "' and eqp_assmbl_bom.assmbl_bom_cd='" + iREQ_ATA_CD_TRK + "'";
      simpleIDs lREQDefnIDs = getREQDEFNIDs( lQuery );

      verifyTASKBLOCKREQMAP( iTASK_IDs, lREQDefnIDs, "1", "1" );
      verifyTASKBLOCKREQMAP( iTASK_IDs_2, lREQDefnIDs, "1", "1" );
      verifyTASKBLOCKREQMAP( iTASK_IDs_3, lREQDefnIDs, "1", "1" );

   }


   /**
    * This test is to verify validation functionality on block_import of block chain requirement
    * with start=1, interval=3. total # of block=3.
    *
    */

   @Test
   public void testBLOCK_NON_RECURING_CHAIN_13_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      prepareData_non_recurring();

      // C_BLOCK_REQ
      Map<String, String> lReqMap = new LinkedHashMap<>();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_NON_RECURRING_1 + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_TRK + "'" );
      lReqMap.put( "BLOCK_CHAIN_START", "'1'" );
      lReqMap.put( "BLOCK_CHAIN_INTERVAL", "'3'" );

      // Insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_REQ, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify import functionality on block_import of block chain requirement with
    * start=1, interval=3. total # of block=3.
    *
    */

   @Test
   public void testBLOCK_NON_RECURING_CHAIN_13_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testBLOCK_NON_RECURING_CHAIN_13_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      resetTaskIds();

      verifyNonRecurring();

      // Verify task_block_req_map
      String lQuery =
            "select TASK_DEFN_DB_ID, TASK_DEFN_ID from task_task " + "inner join eqp_assmbl_bom on "
                  + "eqp_assmbl_bom.assmbl_db_id=task_task.assmbl_db_id and "
                  + "eqp_assmbl_bom.assmbl_cd=task_task.assmbl_cd and "
                  + "eqp_assmbl_bom.assmbl_bom_id = task_task.assmbl_bom_id "
                  + "where task_task.task_cd='" + iREQ_TASK_CD_TRK
                  + "' and eqp_assmbl_bom.assmbl_bom_cd='" + iREQ_ATA_CD_TRK + "'";
      simpleIDs lREQDefnIDs = getREQDEFNIDs( lQuery );

      // first task should be display
      verifyTASKBLOCKREQMAP( iTASK_IDs, lREQDefnIDs, "1", "3" );

      // second block would not display
      lQuery = "select 1 from " + TableUtil.TASK_BLOCK_REQ_MAP + " where BLOCK_TASK_DB_ID="
            + iTASK_IDs_2.getNO_DB_ID() + " and BLOCK_TASK_ID=" + iTASK_IDs_2.getNO_ID();
      Assert.assertFalse( "Check task_block_req_map table to verify the record exists",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.TASK_BLOCK_REQ_MAP + " where BLOCK_TASK_DB_ID="
            + iTASK_IDs_3.getNO_DB_ID() + " and BLOCK_TASK_ID=" + iTASK_IDs_3.getNO_ID();
      Assert.assertFalse( "Check  task_block_req_map table to verify the record exists",
            RecordsExist( lQuery ) );

   }


   /**
    * This test is to verify validation functionality on block_import of block chain requirement
    * with start=2, interval=1. total # of block=3.
    *
    */

   @Test
   public void testBLOCK_NON_RECURING_CHAIN_21_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      prepareData_non_recurring();

      // C_BLOCK_REQ
      Map<String, String> lReqMap = new LinkedHashMap<>();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_NON_RECURRING_1 + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_TRK + "'" );
      lReqMap.put( "BLOCK_CHAIN_START", "'2'" );
      lReqMap.put( "BLOCK_CHAIN_INTERVAL", "'1'" );

      // Insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_REQ, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify import functionality on block_import of block chain requirement with
    * start=2, interval=1. total # of block=3.
    *
    */

   @Test
   public void testBLOCK_NON_RECURING_CHAIN_21_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testBLOCK_NON_RECURING_CHAIN_21_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      resetTaskIds();

      verifyNonRecurring();

      // Verify task_block_req_map
      String lQuery =
            "select TASK_DEFN_DB_ID, TASK_DEFN_ID from task_task " + "inner join eqp_assmbl_bom on "
                  + "eqp_assmbl_bom.assmbl_db_id=task_task.assmbl_db_id and "
                  + "eqp_assmbl_bom.assmbl_cd=task_task.assmbl_cd and "
                  + "eqp_assmbl_bom.assmbl_bom_id = task_task.assmbl_bom_id "
                  + "where task_task.task_cd='" + iREQ_TASK_CD_TRK
                  + "' and eqp_assmbl_bom.assmbl_bom_cd='" + iREQ_ATA_CD_TRK + "'";
      simpleIDs lREQDefnIDs = getREQDEFNIDs( lQuery );

      // second and third task should be display
      verifyTASKBLOCKREQMAP( iTASK_IDs_2, lREQDefnIDs, "2", "1" );
      verifyTASKBLOCKREQMAP( iTASK_IDs_3, lREQDefnIDs, "2", "1" );

      // first block would not display
      lQuery = "select 1 from " + TableUtil.TASK_BLOCK_REQ_MAP + " where BLOCK_TASK_DB_ID="
            + iTASK_IDs.getNO_DB_ID() + " and BLOCK_TASK_ID=" + iTASK_IDs.getNO_ID();
      Assert.assertFalse( "Check  task_block_req_map table to verify the record exists",
            RecordsExist( lQuery ) );

   }


   /**
    * This test is to verify validation functionality on block_import of block chain requirement
    * with start=2, interval=3. total # of block=3.
    *
    */

   @Test
   public void testBLOCK_NON_RECURING_CHAIN_23_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      prepareData_non_recurring();

      // C_BLOCK_REQ
      Map<String, String> lReqMap = new LinkedHashMap<>();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_NON_RECURRING_1 + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_TRK + "'" );
      lReqMap.put( "BLOCK_CHAIN_START", "'2'" );
      lReqMap.put( "BLOCK_CHAIN_INTERVAL", "'3'" );

      // Insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_REQ, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify import functionality on block_import of block chain requirement with
    * start=2, interval=3. total # of block=3.
    *
    */

   @Test
   public void testBLOCK_NON_RECURING_CHAIN_23_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testBLOCK_NON_RECURING_CHAIN_23_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      resetTaskIds();

      verifyNonRecurring();

      // Verify task_block_req_map
      String lQuery =
            "select TASK_DEFN_DB_ID, TASK_DEFN_ID from task_task " + "inner join eqp_assmbl_bom on "
                  + "eqp_assmbl_bom.assmbl_db_id=task_task.assmbl_db_id and "
                  + "eqp_assmbl_bom.assmbl_cd=task_task.assmbl_cd and "
                  + "eqp_assmbl_bom.assmbl_bom_id = task_task.assmbl_bom_id "
                  + "where task_task.task_cd='" + iREQ_TASK_CD_TRK
                  + "' and eqp_assmbl_bom.assmbl_bom_cd='" + iREQ_ATA_CD_TRK + "'";
      simpleIDs lREQDefnIDs = getREQDEFNIDs( lQuery );

      // second task should be display
      verifyTASKBLOCKREQMAP( iTASK_IDs_2, lREQDefnIDs, "2", "3" );

      // first and third block would not display
      lQuery = "select 1 from " + TableUtil.TASK_BLOCK_REQ_MAP + " where BLOCK_TASK_DB_ID="
            + iTASK_IDs.getNO_DB_ID() + " and BLOCK_TASK_ID=" + iTASK_IDs.getNO_ID();
      Assert.assertFalse( "Check  task_block_req_map table to verify the record exists",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.TASK_BLOCK_REQ_MAP + " where BLOCK_TASK_DB_ID="
            + iTASK_IDs_3.getNO_DB_ID() + " and BLOCK_TASK_ID=" + iTASK_IDs_3.getNO_ID();
      Assert.assertFalse( "Check  task_block_req_map table to verify the record exists",
            RecordsExist( lQuery ) );
   }


   /**
    * This test is to verify validation functionality on block_import of block chain requirement
    * with start=3, interval=1. total # of block=3.
    *
    */

   @Test
   public void testBLOCK_NON_RECURING_CHAIN_31_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      prepareData_non_recurring();

      // C_BLOCK_REQ
      Map<String, String> lReqMap = new LinkedHashMap<>();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_NON_RECURRING_1 + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_TRK + "'" );
      lReqMap.put( "BLOCK_CHAIN_START", "'3'" );
      lReqMap.put( "BLOCK_CHAIN_INTERVAL", "'1'" );

      // Insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_REQ, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify import functionality on block_import of block chain requirement with
    * start=3, interval=1. total # of block=3.
    *
    */

   @Test
   public void testBLOCK_NON_RECURING_CHAIN_31_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testBLOCK_NON_RECURING_CHAIN_31_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      resetTaskIds();

      verifyNonRecurring();

      // Verify task_block_req_map
      String lQuery =
            "select TASK_DEFN_DB_ID, TASK_DEFN_ID from task_task " + "inner join eqp_assmbl_bom on "
                  + "eqp_assmbl_bom.assmbl_db_id=task_task.assmbl_db_id and "
                  + "eqp_assmbl_bom.assmbl_cd=task_task.assmbl_cd and "
                  + "eqp_assmbl_bom.assmbl_bom_id = task_task.assmbl_bom_id "
                  + "where task_task.task_cd='" + iREQ_TASK_CD_TRK
                  + "' and eqp_assmbl_bom.assmbl_bom_cd='" + iREQ_ATA_CD_TRK + "'";
      simpleIDs lREQDefnIDs = getREQDEFNIDs( lQuery );

      // third block dispaly
      verifyTASKBLOCKREQMAP( iTASK_IDs_3, lREQDefnIDs, "3", "1" );

      // first and second block would not have req
      lQuery = "select 1 from " + TableUtil.TASK_BLOCK_REQ_MAP + " where BLOCK_TASK_DB_ID="
            + iTASK_IDs.getNO_DB_ID() + " and BLOCK_TASK_ID=" + iTASK_IDs.getNO_ID();
      Assert.assertFalse( "Check  task_block_req_map table to verify the record exists",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.TASK_BLOCK_REQ_MAP + " where BLOCK_TASK_DB_ID="
            + iTASK_IDs_2.getNO_DB_ID() + " and BLOCK_TASK_ID=" + iTASK_IDs_2.getNO_ID();
      Assert.assertFalse( "Check  task_block_req_map table to verify the record exists",
            RecordsExist( lQuery ) );
   }


   /**
    * This test is to verify validation functionality on block_import of block chain requirement
    * with start=3, interval=3. total # of block=3.
    *
    */

   @Test
   public void testBLOCK_NON_RECURING_CHAIN_33_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      prepareData_non_recurring();

      // C_BLOCK_REQ
      Map<String, String> lReqMap = new LinkedHashMap<>();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_NON_RECURRING_1 + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_TRK + "'" );
      lReqMap.put( "BLOCK_CHAIN_START", "'3'" );
      lReqMap.put( "BLOCK_CHAIN_INTERVAL", "'3'" );

      // Insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_REQ, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify import functionality on block_import of block chain requirement with
    * start=3, interval=3. total # of block=3.
    *
    */

   @Test
   public void testBLOCK_NON_RECURING_CHAIN_33_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testBLOCK_NON_RECURING_CHAIN_33_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      resetTaskIds();

      verifyNonRecurring();

      // Verify task_block_req_map
      String lQuery =
            "select TASK_DEFN_DB_ID, TASK_DEFN_ID from task_task " + "inner join eqp_assmbl_bom on "
                  + "eqp_assmbl_bom.assmbl_db_id=task_task.assmbl_db_id and "
                  + "eqp_assmbl_bom.assmbl_cd=task_task.assmbl_cd and "
                  + "eqp_assmbl_bom.assmbl_bom_id = task_task.assmbl_bom_id "
                  + "where task_task.task_cd='" + iREQ_TASK_CD_TRK
                  + "' and eqp_assmbl_bom.assmbl_bom_cd='" + iREQ_ATA_CD_TRK + "'";
      simpleIDs lREQDefnIDs = getREQDEFNIDs( lQuery );

      // third block dispaly
      verifyTASKBLOCKREQMAP( iTASK_IDs_3, lREQDefnIDs, "3", "3" );

      // first and second block would not have req
      lQuery = "select 1 from " + TableUtil.TASK_BLOCK_REQ_MAP + " where BLOCK_TASK_DB_ID="
            + iTASK_IDs.getNO_DB_ID() + " and BLOCK_TASK_ID=" + iTASK_IDs.getNO_ID();
      Assert.assertFalse( "Check  task_block_req_map table to verify the record exists",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.TASK_BLOCK_REQ_MAP + " where BLOCK_TASK_DB_ID="
            + iTASK_IDs_2.getNO_DB_ID() + " and BLOCK_TASK_ID=" + iTASK_IDs_2.getNO_ID();
      Assert.assertFalse( "Check  task_block_req_map table to verify the record exists",
            RecordsExist( lQuery ) );
   }


   /**
    * This test is to verify validation functionality on block_import of recurring block chain
    * requirement with start=1, interval=1. total # of block=3.
    *
    */

   @Test
   public void testBLOCK_RECURING_CHAIN_11_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      prepareData_recurring();

      // C_BLOCK_REQ
      Map<String, String> lReqMap = new LinkedHashMap<>();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_RECURRING_1 + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_TRK_RECURRING + "'" );
      lReqMap.put( "BLOCK_CHAIN_START", "'1'" );
      lReqMap.put( "BLOCK_CHAIN_INTERVAL", "'1'" );

      // Insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_REQ, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify import functionality on block_import of recurring block chain
    * requirement with start=1, interval=1. total # of block=3.
    *
    */

   @Test
   public void testBLOCK_RECURING_CHAIN_11_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testBLOCK_RECURING_CHAIN_11_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      resetTaskIds();

      verifyRecurring();

      // Verify task_block_req_map
      String lQuery =
            "select TASK_DEFN_DB_ID, TASK_DEFN_ID from task_task " + "inner join eqp_assmbl_bom on "
                  + "eqp_assmbl_bom.assmbl_db_id=task_task.assmbl_db_id and "
                  + "eqp_assmbl_bom.assmbl_cd=task_task.assmbl_cd and "
                  + "eqp_assmbl_bom.assmbl_bom_id = task_task.assmbl_bom_id "
                  + "where task_task.task_cd='" + iREQ_TASK_CD_TRK_RECURRING
                  + "' and eqp_assmbl_bom.assmbl_bom_cd='" + iREQ_ATA_CD_TRK + "'";
      simpleIDs lREQDefnIDs = getREQDEFNIDs( lQuery );

      verifyTASKBLOCKREQMAP( iTASK_IDs, lREQDefnIDs, "1", "1" );
      verifyTASKBLOCKREQMAP( iTASK_IDs_2, lREQDefnIDs, "1", "1" );
      verifyTASKBLOCKREQMAP( iTASK_IDs_3, lREQDefnIDs, "1", "1" );
   }


   /**
    * This test is to verify validation functionality on block_import of recurring block chain
    * requirement with start=3, interval=1. total # of block=3.
    *
    */

   @Test
   public void testBLOCK_RECURING_CHAIN_13_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      prepareData_recurring();

      // C_BLOCK_REQ
      Map<String, String> lReqMap = new LinkedHashMap<>();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_RECURRING_1 + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_TRK_RECURRING + "'" );
      lReqMap.put( "BLOCK_CHAIN_START", "'1'" );
      lReqMap.put( "BLOCK_CHAIN_INTERVAL", "'3'" );

      // Insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_REQ, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify import functionality on block_import of recurring block chain
    * requirement with start=1, interval=3. total # of block=3.
    *
    */

   @Test
   public void testBLOCK_RECURING_CHAIN_13_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testBLOCK_RECURING_CHAIN_13_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      resetTaskIds();

      verifyRecurring();

      // Verify task_block_req_map
      String lQuery =
            "select TASK_DEFN_DB_ID, TASK_DEFN_ID from task_task " + "inner join eqp_assmbl_bom on "
                  + "eqp_assmbl_bom.assmbl_db_id=task_task.assmbl_db_id and "
                  + "eqp_assmbl_bom.assmbl_cd=task_task.assmbl_cd and "
                  + "eqp_assmbl_bom.assmbl_bom_id = task_task.assmbl_bom_id "
                  + "where task_task.task_cd='" + iREQ_TASK_CD_TRK_RECURRING
                  + "' and eqp_assmbl_bom.assmbl_bom_cd='" + iREQ_ATA_CD_TRK + "'";
      simpleIDs lREQDefnIDs = getREQDEFNIDs( lQuery );

      // first task should be display
      verifyTASKBLOCKREQMAP( iTASK_IDs, lREQDefnIDs, "1", "3" );

      // second block would not display
      lQuery = "select 1 from " + TableUtil.TASK_BLOCK_REQ_MAP + " where BLOCK_TASK_DB_ID="
            + iTASK_IDs_2.getNO_DB_ID() + " and BLOCK_TASK_ID=" + iTASK_IDs_2.getNO_ID();
      Assert.assertFalse( "Check task_block_req_map table to verify the record exists",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.TASK_BLOCK_REQ_MAP + " where BLOCK_TASK_DB_ID="
            + iTASK_IDs_3.getNO_DB_ID() + " and BLOCK_TASK_ID=" + iTASK_IDs_3.getNO_ID();
      Assert.assertFalse( "Check  task_block_req_map table to verify the record exists",
            RecordsExist( lQuery ) );
   }


   /**
    * This test is to verify validation functionality on block_import of recurring block chain
    * requirement with start=2, interval=3. total # of block=3.
    *
    */

   @Test
   public void testBLOCK_RECURING_CHAIN_23_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      prepareData_recurring();

      // C_BLOCK_REQ
      Map<String, String> lReqMap = new LinkedHashMap<>();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_RECURRING_1 + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_TRK_RECURRING + "'" );
      lReqMap.put( "BLOCK_CHAIN_START", "'2'" );
      lReqMap.put( "BLOCK_CHAIN_INTERVAL", "'3'" );

      // Insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_REQ, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify import functionality on block_import of recurring block chain
    * requirement with start=2, interval=3. total # of block=3.
    *
    */

   @Test
   public void testBLOCK_RECURING_CHAIN_23_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testBLOCK_RECURING_CHAIN_23_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      resetTaskIds();

      verifyRecurring();

      // Verify task_block_req_map
      String lQuery =
            "select TASK_DEFN_DB_ID, TASK_DEFN_ID from task_task " + "inner join eqp_assmbl_bom on "
                  + "eqp_assmbl_bom.assmbl_db_id=task_task.assmbl_db_id and "
                  + "eqp_assmbl_bom.assmbl_cd=task_task.assmbl_cd and "
                  + "eqp_assmbl_bom.assmbl_bom_id = task_task.assmbl_bom_id "
                  + "where task_task.task_cd='" + iREQ_TASK_CD_TRK_RECURRING
                  + "' and eqp_assmbl_bom.assmbl_bom_cd='" + iREQ_ATA_CD_TRK + "'";
      simpleIDs lREQDefnIDs = getREQDEFNIDs( lQuery );

      // second task should be display
      verifyTASKBLOCKREQMAP( iTASK_IDs_2, lREQDefnIDs, "2", "3" );

      // first and third block would not display
      lQuery = "select 1 from " + TableUtil.TASK_BLOCK_REQ_MAP + " where BLOCK_TASK_DB_ID="
            + iTASK_IDs.getNO_DB_ID() + " and BLOCK_TASK_ID=" + iTASK_IDs.getNO_ID();
      Assert.assertFalse( "Check  task_block_req_map table to verify the record exists",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.TASK_BLOCK_REQ_MAP + " where BLOCK_TASK_DB_ID="
            + iTASK_IDs_3.getNO_DB_ID() + " and BLOCK_TASK_ID=" + iTASK_IDs_3.getNO_ID();
      Assert.assertFalse( "Check  task_block_req_map table to verify the record exists",
            RecordsExist( lQuery ) );
   }


   /**
    * This test is to verify validation functionality on block_import of recurring block chain
    * requirement with start=2, interval=3. total # of block=3.
    *
    */

   @Test
   public void testBLOCK_RECURING_CHAIN_33_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      prepareData_recurring();

      // C_BLOCK_REQ
      Map<String, String> lReqMap = new LinkedHashMap<>();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_RECURRING_1 + "'" );
      lReqMap.put( "REQ_ATA_CD", "'" + iREQ_ATA_CD_TRK + "'" );
      lReqMap.put( "REQ_TASK_CD", "'" + iREQ_TASK_CD_TRK_RECURRING + "'" );
      lReqMap.put( "BLOCK_CHAIN_START", "'3'" );
      lReqMap.put( "BLOCK_CHAIN_INTERVAL", "'3'" );

      // Insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_REQ, lReqMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify import functionality on block_import of recurring block chain
    * requirement with start=3, interval=3. total # of block=3.
    *
    */

   @Test
   public void testBLOCK_RECURING_CHAIN_33_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testBLOCK_RECURING_CHAIN_33_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      resetTaskIds();

      verifyRecurring();

      // Verify task_block_req_map
      String lQuery =
            "select TASK_DEFN_DB_ID, TASK_DEFN_ID from task_task " + "inner join eqp_assmbl_bom on "
                  + "eqp_assmbl_bom.assmbl_db_id=task_task.assmbl_db_id and "
                  + "eqp_assmbl_bom.assmbl_cd=task_task.assmbl_cd and "
                  + "eqp_assmbl_bom.assmbl_bom_id = task_task.assmbl_bom_id "
                  + "where task_task.task_cd='" + iREQ_TASK_CD_TRK_RECURRING
                  + "' and eqp_assmbl_bom.assmbl_bom_cd='" + iREQ_ATA_CD_TRK + "'";
      simpleIDs lREQDefnIDs = getREQDEFNIDs( lQuery );

      // third block dispaly
      verifyTASKBLOCKREQMAP( iTASK_IDs_3, lREQDefnIDs, "3", "3" );

      // first and second block would not have req
      lQuery = "select 1 from " + TableUtil.TASK_BLOCK_REQ_MAP + " where BLOCK_TASK_DB_ID="
            + iTASK_IDs.getNO_DB_ID() + " and BLOCK_TASK_ID=" + iTASK_IDs.getNO_ID();
      Assert.assertFalse( "Check  task_block_req_map table to verify the record exists",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.TASK_BLOCK_REQ_MAP + " where BLOCK_TASK_DB_ID="
            + iTASK_IDs_2.getNO_DB_ID() + " and BLOCK_TASK_ID=" + iTASK_IDs_2.getNO_ID();
      Assert.assertFalse( "Check  task_block_req_map table to verify the record exists",
            RecordsExist( lQuery ) );
   }


   // =================================================================================

   /**
    * This function is to verify tables of recurring block chain
    *
    *
    */

   public void verifyRecurring() {

      // Verify task_task table
      iTASK_IDs = getTaskIds( iBLOCK_TASK_CD_RECURRING_1, iBLOCK_TASK_NAME_CHAIN_RECURRING_1 );
      assmbleInfor lassmblinfor = getassmblInfor( iBLOCK_ATA_CD_TRK );

      iTASK_DEFN_IDs = verifyTaskTask( iTASK_IDs, iCLASS_CD_BLOCK, lassmblinfor, null,
            iORIGINATOR_CD, iBLOCK_TASK_CD_RECURRING_1, iBLOCK_TASK_NAME_CHAIN_RECURRING_1,
            iBLOCK_TASK_DESC_CHAIN_RECURRING_1, null, iLAST_DEADLINE_DRIVER_BOOL_N,
            iSOFT_DEADLINE_DRIVER_BOOL_N, null, iON_CONDITION_BOOL_N_number,
            iBLOCK_CHAIN_SDESC_CHAIN_RECURRING );

      iTASK_IDs_2 = getTaskIds( iBLOCK_TASK_CD_RECURRING_2, iBLOCK_TASK_NAME_CHAIN_RECURRING_2 );

      iTASK_DEFN_IDs_2 = verifyTaskTask( iTASK_IDs_2, iCLASS_CD_BLOCK, lassmblinfor, null,
            iORIGINATOR_CD, iBLOCK_TASK_CD_RECURRING_2, iBLOCK_TASK_NAME_CHAIN_RECURRING_2,
            iBLOCK_TASK_DESC_CHAIN_RECURRING_1, null, iLAST_DEADLINE_DRIVER_BOOL_N,
            iSOFT_DEADLINE_DRIVER_BOOL_N, null, iON_CONDITION_BOOL_N_number,
            iBLOCK_CHAIN_SDESC_CHAIN_RECURRING );

      iTASK_IDs_3 = getTaskIds( iBLOCK_TASK_CD_RECURRING_3, iBLOCK_TASK_NAME_CHAIN_RECURRING_3 );

      iTASK_DEFN_IDs_3 = verifyTaskTask( iTASK_IDs_3, iCLASS_CD_BLOCK, lassmblinfor, null,
            iORIGINATOR_CD, iBLOCK_TASK_CD_RECURRING_3, iBLOCK_TASK_NAME_CHAIN_RECURRING_3,
            iBLOCK_TASK_DESC_CHAIN_RECURRING_1, null, iLAST_DEADLINE_DRIVER_BOOL_N,
            iSOFT_DEADLINE_DRIVER_BOOL_N, null, iON_CONDITION_BOOL_N_number,
            iBLOCK_CHAIN_SDESC_CHAIN_RECURRING );

      // verify task_DEFN
      String lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTASK_DEFN_IDs.getNO_DB_ID() + " and TASK_DEFN_ID=" + iTASK_DEFN_IDs.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTASK_DEFN_IDs_2.getNO_DB_ID() + " and TASK_DEFN_ID=" + iTASK_DEFN_IDs_2.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTASK_DEFN_IDs_3.getNO_DB_ID() + " and TASK_DEFN_ID=" + iTASK_DEFN_IDs_3.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_task_dep
      verifyTASKTASkDEP( iTASK_IDs, iTASK_DEFN_IDs_2, "CRT" );
      verifyTASKTASkDEP( iTASK_IDs_2, iTASK_DEFN_IDs_3, "CRT" );

      // verify task_sched_rule table
      simpleIDs lTypeIds = getTypeIds( iSCHED_DATA_TYPE_CD );
      verifyTASKSCHEDRULE( iTASK_IDs, lTypeIds, iSCHED_INTERVAL_QT, null );
      verifyTASKSCHEDRULE( iTASK_IDs_2, lTypeIds, iSCHED_INTERVAL_QT, null );
      verifyTASKSCHEDRULE( iTASK_IDs_3, lTypeIds, iSCHED_INTERVAL_QT, null );
   }


   /**
    * This function is to verify tables of non-recurring block chain
    *
    *
    */

   public void verifyNonRecurring() {

      // Verify task_task table
      iTASK_IDs =
            getTaskIds( iBLOCK_TASK_CD_NON_RECURRING_1, iBLOCK_TASK_NAME_CHAIN_NON_RECURRING_1 );
      assmbleInfor lassmblinfor = getassmblInfor( iBLOCK_ATA_CD_TRK );

      iTASK_DEFN_IDs = verifyTaskTask( iTASK_IDs, iCLASS_CD_BLOCK, lassmblinfor, null,
            iORIGINATOR_CD, iBLOCK_TASK_CD_NON_RECURRING_1, iBLOCK_TASK_NAME_CHAIN_NON_RECURRING_1,
            iBLOCK_TASK_DESC_CHAIN_NON_RECURRING_1, null, iLAST_DEADLINE_DRIVER_BOOL_N,
            iSOFT_DEADLINE_DRIVER_BOOL_N, null, iON_CONDITION_BOOL_Y_number,
            iBLOCK_CHAIN_SDESC_CHAIN_NON_RECURRING );

      iTASK_IDs_2 =
            getTaskIds( iBLOCK_TASK_CD_NON_RECURRING_2, iBLOCK_TASK_NAME_CHAIN_NON_RECURRING_2 );

      iTASK_DEFN_IDs_2 = verifyTaskTask( iTASK_IDs_2, iCLASS_CD_BLOCK, lassmblinfor, null,
            iORIGINATOR_CD, iBLOCK_TASK_CD_NON_RECURRING_2, iBLOCK_TASK_NAME_CHAIN_NON_RECURRING_2,
            iBLOCK_TASK_DESC_CHAIN_NON_RECURRING_1, null, iLAST_DEADLINE_DRIVER_BOOL_N,
            iSOFT_DEADLINE_DRIVER_BOOL_N, null, iON_CONDITION_BOOL_Y_number,
            iBLOCK_CHAIN_SDESC_CHAIN_NON_RECURRING );

      iTASK_IDs_3 =
            getTaskIds( iBLOCK_TASK_CD_NON_RECURRING_3, iBLOCK_TASK_NAME_CHAIN_NON_RECURRING_3 );

      iTASK_DEFN_IDs_3 = verifyTaskTask( iTASK_IDs_3, iCLASS_CD_BLOCK, lassmblinfor, null,
            iORIGINATOR_CD, iBLOCK_TASK_CD_NON_RECURRING_3, iBLOCK_TASK_NAME_CHAIN_NON_RECURRING_3,
            iBLOCK_TASK_DESC_CHAIN_NON_RECURRING_1, null, iLAST_DEADLINE_DRIVER_BOOL_N,
            iSOFT_DEADLINE_DRIVER_BOOL_N, null, iON_CONDITION_BOOL_Y_number,
            iBLOCK_CHAIN_SDESC_CHAIN_NON_RECURRING );

      // verify task_DEFN
      String lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTASK_DEFN_IDs.getNO_DB_ID() + " and TASK_DEFN_ID=" + iTASK_DEFN_IDs.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTASK_DEFN_IDs_2.getNO_DB_ID() + " and TASK_DEFN_ID=" + iTASK_DEFN_IDs_2.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTASK_DEFN_IDs_3.getNO_DB_ID() + " and TASK_DEFN_ID=" + iTASK_DEFN_IDs_3.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_task_dep
      verifyTASKTASkDEP( iTASK_IDs, iTASK_DEFN_IDs_2, "CRT" );
      verifyTASKTASkDEP( iTASK_IDs_2, iTASK_DEFN_IDs_3, "CRT" );

      // verify task_sched_rule table
      simpleIDs lTypeIds = getTypeIds( iSCHED_DATA_TYPE_CD );
      verifyTASKSCHEDRULE( iTASK_IDs, lTypeIds, iSCHED_THRESHOLD_QT, null );
      verifyTASKSCHEDRULE( iTASK_IDs_2, lTypeIds, iSCHED_THRESHOLD_QT, null );
      verifyTASKSCHEDRULE( iTASK_IDs_3, lTypeIds, iSCHED_THRESHOLD_QT, null );
   }


   /**
    * This function is to prepare data for recurring block chain
    *
    *
    */

   public void prepareData_recurring() {
      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_RECURRING_1 + "'" );
      lReqMap.put( "BLOCK_TASK_NAME", "'" + iBLOCK_TASK_NAME_CHAIN_RECURRING_1 + "'" );
      lReqMap.put( "BLOCK_TASK_DESC", "'" + iBLOCK_TASK_NAME_CHAIN_RECURRING_1 + "'" );
      lReqMap.put( "BLOCK_CHAIN_SDESC", "'" + iBLOCK_CHAIN_SDESC_CHAIN_RECURRING + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD_BLOCK + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "LAST_DEADLINE_DRIVER_BOOL", "'" + iLAST_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_DRIVER_BOOL", "'" + iSOFT_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL_N + "'" );
      lReqMap.put( "TASK_MUST_REMOVE_CD", "'" + iTASK_MUST_REMOVE_CD + "'" );
      lReqMap.put( "RESCHED_FROM_CD", "'EXECUTE'" );
      lReqMap.put( "RECURRING_TASK_BOOL", "'Y'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK, lReqMap ) );

      // c_block_chain_task_details
      lReqMap.clear();
      lReqMap.put( "BLOCK_CHAIN_ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_CHAIN_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_CHAIN_SDESC", "'" + iBLOCK_CHAIN_SDESC_CHAIN_RECURRING + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_RECURRING_2 + "'" );
      lReqMap.put( "BLOCK_TASK_NAME", "'" + iBLOCK_TASK_NAME_CHAIN_RECURRING_2 + "'" );
      lReqMap.put( "BLOCK_ORD", "'2'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_CHAIN_TASK_DETAILS, lReqMap ) );

      // c_block_chain_task_details
      lReqMap.clear();
      lReqMap.put( "BLOCK_CHAIN_ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_CHAIN_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_CHAIN_SDESC", "'" + iBLOCK_CHAIN_SDESC_CHAIN_RECURRING + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_RECURRING_3 + "'" );
      lReqMap.put( "BLOCK_TASK_NAME", "'" + iBLOCK_TASK_NAME_CHAIN_RECURRING_3 + "'" );
      lReqMap.put( "BLOCK_ORD", "'3'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_CHAIN_TASK_DETAILS, lReqMap ) );

      // c_block_standard_deadline
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_RECURRING_1 + "'" );
      // lReqMap.put( "SCHED_HRS_THRESHOLD", "'" + iSCHED_THRESHOLD_QT + "'" );
      lReqMap.put( "SCHED_HRS_INTERVAL", "'" + iSCHED_INTERVAL_QT + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_STANDARD_DEADLINE, lReqMap ) );

   }


   /**
    * This function is to prepare data for non recurring block chain
    *
    *
    */

   public void prepareData_non_recurring() {

      Map<String, String> lReqMap = new LinkedHashMap<>();

      // C_REQ
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_NON_RECURRING_1 + "'" );
      lReqMap.put( "BLOCK_TASK_NAME", "'" + iBLOCK_TASK_NAME_CHAIN_NON_RECURRING_1 + "'" );
      lReqMap.put( "BLOCK_TASK_DESC", "'" + iBLOCK_TASK_DESC_CHAIN_NON_RECURRING_1 + "'" );
      lReqMap.put( "BLOCK_CHAIN_SDESC", "'" + iBLOCK_CHAIN_SDESC_CHAIN_NON_RECURRING + "'" );
      lReqMap.put( "CLASS_CD", "'" + iCLASS_CD_BLOCK + "'" );
      lReqMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lReqMap.put( "SCHED_FROM_MANUFACT_DT_BOOL", "'" + iSCHED_FROM_MANUFACT_DT_BOOL + "'" );
      lReqMap.put( "LAST_DEADLINE_DRIVER_BOOL", "'" + iLAST_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "SOFT_DEADLINE_DRIVER_BOOL", "'" + iSOFT_DEADLINE_DRIVER_BOOL + "'" );
      lReqMap.put( "ON_CONDITION_BOOL", "'" + iON_CONDITION_BOOL_Y + "'" );
      lReqMap.put( "TASK_MUST_REMOVE_CD", "'" + iTASK_MUST_REMOVE_CD + "'" );
      lReqMap.put( "RESCHED_FROM_CD", null );
      lReqMap.put( "RECURRING_TASK_BOOL", "'N'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK, lReqMap ) );

      // c_block_chain_task_details
      lReqMap.clear();
      lReqMap.put( "BLOCK_CHAIN_ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_CHAIN_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_CHAIN_SDESC", "'" + iBLOCK_CHAIN_SDESC_CHAIN_NON_RECURRING + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_NON_RECURRING_2 + "'" );
      lReqMap.put( "BLOCK_TASK_NAME", "'" + iBLOCK_TASK_NAME_CHAIN_NON_RECURRING_2 + "'" );
      lReqMap.put( "BLOCK_ORD", "'2'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_CHAIN_TASK_DETAILS, lReqMap ) );

      // c_block_chain_task_details
      lReqMap.clear();
      lReqMap.put( "BLOCK_CHAIN_ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_CHAIN_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_CHAIN_SDESC", "'" + iBLOCK_CHAIN_SDESC_CHAIN_NON_RECURRING + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_NON_RECURRING_3 + "'" );
      lReqMap.put( "BLOCK_TASK_NAME", "'" + iBLOCK_TASK_NAME_CHAIN_NON_RECURRING_3 + "'" );
      lReqMap.put( "BLOCK_ORD", "'3'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_CHAIN_TASK_DETAILS, lReqMap ) );

      // c_block_standard_deadline
      lReqMap.clear();
      lReqMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lReqMap.put( "BLOCK_ATA_CD", "'" + iBLOCK_ATA_CD_TRK + "'" );
      lReqMap.put( "BLOCK_TASK_CD", "'" + iBLOCK_TASK_CD_NON_RECURRING_1 + "'" );
      lReqMap.put( "SCHED_HRS_THRESHOLD", "'" + iSCHED_THRESHOLD_QT + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_BLOCK_STANDARD_DEADLINE, lReqMap ) );

   }


   /**
    * This function is to verify task_task_dep table for 15067.
    *
    *
    */

   public void verifyTASKTASKDEP_15067( simpleIDs aTaskIds, String aTASK_DEP_ACTION_CD,
         simpleIDs aDEPids ) {
      String[] iIds = { "TASK_DEP_ACTION_CD", "DEP_TASK_DEFN_DB_ID", "DEP_TASK_DEFN_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_DB_ID", aTaskIds.getNO_DB_ID() );
      lArgs.addArguments( "TASK_ID", aTaskIds.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_TASK_DEP, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "TASK_DEP_ACTION_CD",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aTASK_DEP_ACTION_CD ) );
      Assert.assertTrue( "DEP_TASK_DEFN_DB_ID",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( aDEPids.getNO_DB_ID() ) );
      Assert.assertTrue( "DEP_TASK_DEFN_ID",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aDEPids.getNO_ID() ) );

   }


   /**
    * This function is to retrieve part no ids.
    *
    *
    */

   @Override
   public simpleIDs getPartIds( String aPART_NO_OEM, String aMANUFACT_CD ) {

      String[] iIds = { "PART_NO_DB_ID", "PART_NO_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "PART_NO_OEM", aPART_NO_OEM );
      lArgs.addArguments( "MANUFACT_CD", aMANUFACT_CD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EQP_PART_NO, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );
      return lIds;

   }


   /**
    * This function is to verify task_sched_rule table for 15067.
    *
    *
    */

   public void verifyTASKINTERVAL_15067( simpleIDs aTaskIds, simpleIDs aDataTypeIds,
         simpleIDs aPartIds, String aDEF_INTERVAL_QT, String aDEF_INITIAL_QT ) {

      String[] iIds = { "DATA_TYPE_DB_ID", "DATA_TYPE_ID", "PART_NO_DB_ID", "PART_NO_ID",
            "INTERVAL_QT", "INITIAL_QT" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_DB_ID", aTaskIds.getNO_DB_ID() );
      lArgs.addArguments( "TASK_ID", aTaskIds.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_INTERVAL, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "DATA_TYPE_DB_ID",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aDataTypeIds.getNO_DB_ID() ) );
      Assert.assertTrue( "DATA_TYPE_ID",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( aDataTypeIds.getNO_ID() ) );
      Assert.assertTrue( "PART_NO_DB_ID",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aPartIds.getNO_DB_ID() ) );
      Assert.assertTrue( "PART_NO_ID",
            llists.get( 0 ).get( 3 ).equalsIgnoreCase( aPartIds.getNO_ID() ) );
      Assert.assertTrue( "INTERVAL_QT",
            llists.get( 0 ).get( 4 ).equalsIgnoreCase( aDEF_INTERVAL_QT ) );
      if ( aDEF_INITIAL_QT != null ) {
         Assert.assertTrue( "INITIAL_QT",
               llists.get( 0 ).get( 5 ).equalsIgnoreCase( aDEF_INITIAL_QT ) );

      }

   }


   /**
    * This function is to verify task_sched_rule table for 15067.
    *
    *
    */

   public void verifyTASKSCHEDRULE_15067( simpleIDs aTaskIds, simpleIDs aDataTypeIds,
         String aDEF_INTERVAL_QT, String aDEF_INITIAL_QT ) {
      String[] iIds = { "DATA_TYPE_DB_ID", "DATA_TYPE_ID", "DEF_INTERVAL_QT", "DEF_INITIAL_QT" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_DB_ID", aTaskIds.getNO_DB_ID() );
      lArgs.addArguments( "TASK_ID", aTaskIds.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_SCHED_RULE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "DATA_TYPE_DB_ID",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aDataTypeIds.getNO_DB_ID() ) );
      Assert.assertTrue( "DATA_TYPE_ID",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( aDataTypeIds.getNO_ID() ) );
      Assert.assertTrue( "DEF_INTERVAL_QT",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aDEF_INTERVAL_QT ) );
      if ( aDEF_INITIAL_QT != null ) {
         Assert.assertTrue( "DEF_INITIAL_QT",
               llists.get( 0 ).get( 3 ).equalsIgnoreCase( aDEF_INITIAL_QT ) );

      }

   }


   /**
    * This function is to retrieve data type IDs from MIM_DATA_TYPE table.
    *
    *
    */

   @Override
   public simpleIDs getTypeIds( String aDATA_TYPE_CD ) {
      String[] iIds = { "DATA_TYPE_DB_ID", "DATA_TYPE_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "DATA_TYPE_CD", aDATA_TYPE_CD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.MIM_DATA_TYPE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );
      return lIds;

   }


   /**
    * This function is to verify task_task table for 15067.
    *
    *
    */
   public simpleIDs verifyTaskTask_15067( simpleIDs aTaskIds, String aTASK_CLASS_CD,
         assmbleInfor aassmbleInfor, String aTASK_SUBCLASS_CD, String aTASK_ORIGINATOR_CD,
         String aTaskCD, String aTaskName, String aTASK_LDESC, String aTASK_REF_SDESC,
         String aLAST_SCHED_DEAD_BOOL, String aSOFT_DEADLINE_BOOL, String aTASK_MUST_REMOVE_CD,
         String aON_CONDITION_BOOL ) {

      String[] iIds = { "TASK_DEFN_DB_ID", "TASK_DEFN_ID", "TASK_CLASS_CD", "ASSMBL_DB_ID",
            "ASSMBL_CD", "ASSMBL_BOM_ID", "TASK_SUBCLASS_CD", "TASK_ORIGINATOR_CD", "TASK_CD",
            "TASK_NAME", "TASK_LDESC", "TASK_REF_SDESC", "LAST_SCHED_DEAD_BOOL",
            "SOFT_DEADLINE_BOOL", "TASK_MUST_REMOVE_CD", "ON_CONDITION_BOOL" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_DB_ID", aTaskIds.getNO_DB_ID() );
      lArgs.addArguments( "TASK_ID", aTaskIds.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_TASK, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      Assert.assertTrue( "TASK_CLASS_CD",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aTASK_CLASS_CD ) );
      if ( aassmbleInfor != null ) {
         Assert.assertTrue( "ASSMBL_DB_ID",
               llists.get( 0 ).get( 3 ).equalsIgnoreCase( aassmbleInfor.getASSMBL_DB_ID() ) );
         Assert.assertTrue( "ASSMBL_CD",
               llists.get( 0 ).get( 4 ).equalsIgnoreCase( aassmbleInfor.getASSMBL_CD() ) );
         Assert.assertTrue( "ASSMBL_BOM_ID",
               llists.get( 0 ).get( 5 ).equalsIgnoreCase( aassmbleInfor.getASSMBL_BOM_ID() ) );
      }
      Assert.assertTrue( "TASK_SUBCLASS_CD",
            llists.get( 0 ).get( 6 ).equalsIgnoreCase( aTASK_SUBCLASS_CD ) );
      Assert.assertTrue( "TASK_ORIGINATOR_CD",
            llists.get( 0 ).get( 7 ).equalsIgnoreCase( aTASK_ORIGINATOR_CD ) );
      Assert.assertTrue( "TASK_CD", llists.get( 0 ).get( 8 ).equalsIgnoreCase( aTaskCD ) );
      Assert.assertTrue( "TASK_NAME", llists.get( 0 ).get( 9 ).equalsIgnoreCase( aTaskName ) );
      Assert.assertTrue( "TASK_LDESC", llists.get( 0 ).get( 10 ).equalsIgnoreCase( aTASK_LDESC ) );
      Assert.assertTrue( "TASK_REF_SDESC",
            llists.get( 0 ).get( 11 ).equalsIgnoreCase( aTASK_REF_SDESC ) );
      Assert.assertTrue( "LAST_SCHED_DEAD_BOOL",
            llists.get( 0 ).get( 12 ).equalsIgnoreCase( aLAST_SCHED_DEAD_BOOL ) );
      Assert.assertTrue( "SOFT_DEADLINE_BOOL",
            llists.get( 0 ).get( 13 ).equalsIgnoreCase( aSOFT_DEADLINE_BOOL ) );

      if ( aTASK_MUST_REMOVE_CD != null ) {
         Assert.assertTrue( "TASK_MUST_REMOVE_CD",
               llists.get( 0 ).get( 14 ).equalsIgnoreCase( aTASK_MUST_REMOVE_CD ) );

      }

      if ( aON_CONDITION_BOOL != null ) {
         Assert.assertTrue( "ON_CONDITION_BOOL",
               llists.get( 0 ).get( 15 ).equalsIgnoreCase( aON_CONDITION_BOOL ) );

      }

      return lIds;
   }


   /**
    * This function is to retrieve assemble information from EQP_ASSMBL_BOM table.
    *
    *
    */

   @Override
   public assmbleInfor getassmblInfor( String aASSMBL_BOM_CD ) {

      String[] iIds = { "ASSMBL_DB_ID", "ASSMBL_CD", "ASSMBL_BOM_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "ASSMBL_BOM_CD", aASSMBL_BOM_CD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EQP_ASSMBL_BOM, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      assmbleInfor lIds = new assmbleInfor( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ),
            llists.get( 0 ).get( 2 ), null );

      return lIds;

   }


   /**
    * This function is to retrieve task IDs from task_task table.
    *
    *
    */
   @Override
   public simpleIDs getTaskIds( String aTASK_CD, String aTASK_NAME ) {

      String[] iIds = { "TASK_DB_ID", "TASK_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_CD", aTASK_CD );
      lArgs.addArguments( "TASK_NAME", aTASK_NAME );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_TASK, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );
      return lIds;
   }


   /**
    * This function is called by after test method to restore data which has been modified by tests.
    *
    *
    */

   public void RestoreData() {

      String lStrDelete = null;

      if ( iTASK_IDs != null ) {
         // delete task_block_req_map table
         lStrDelete = "delete from " + TableUtil.TASK_BLOCK_REQ_MAP + "  where BLOCK_TASK_DB_ID="
               + iTASK_IDs.getNO_DB_ID() + " and BLOCK_TASK_ID=" + iTASK_IDs.getNO_ID();
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

         // delete task_block_req_map table
         lStrDelete = "delete from " + TableUtil.TASK_BLOCK_REQ_MAP + "  where BLOCK_TASK_DB_ID="
               + iTASK_IDs_2.getNO_DB_ID() + " and BLOCK_TASK_ID=" + iTASK_IDs_2.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_sched_rule
         lStrDelete = "delete from " + TableUtil.TASK_SCHED_RULE + "  where TASK_DB_ID="
               + iTASK_IDs_2.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_2.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_task_dep
         lStrDelete = "delete from " + TableUtil.TASK_TASK_DEP + "  where TASK_DB_ID="
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

      if ( iTASK_IDs_3 != null ) {

         // delete task_block_req_map table
         lStrDelete = "delete from " + TableUtil.TASK_BLOCK_REQ_MAP + "  where BLOCK_TASK_DB_ID="
               + iTASK_IDs_3.getNO_DB_ID() + " and BLOCK_TASK_ID=" + iTASK_IDs_3.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_sched_rule
         lStrDelete = "delete from " + TableUtil.TASK_SCHED_RULE + "  where TASK_DB_ID="
               + iTASK_IDs_3.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_3.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_task_dep
         lStrDelete = "delete from " + TableUtil.TASK_TASK_DEP + "  where TASK_DB_ID="
               + iTASK_IDs_3.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_3.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_interval
         lStrDelete = "delete from " + TableUtil.TASK_INTERVAL + "  where TASK_DB_ID="
               + iTASK_IDs_3.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_3.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_task
         lStrDelete = "delete from " + TableUtil.TASK_TASK + "  where TASK_DB_ID="
               + iTASK_IDs_3.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_3.getNO_ID();
         executeSQL( lStrDelete );

      }

      if ( iTASK_DEFN_IDs_3 != null ) {
         lStrDelete =
               "delete from TASK_DEFN where TASK_DEFN_DB_ID=" + iTASK_DEFN_IDs_3.getNO_DB_ID()
                     + " and TASK_DEFN_ID=" + iTASK_DEFN_IDs_3.getNO_ID();
         executeSQL( lStrDelete );

      }

   }


   /**
    * This function is to validate error code exists.
    *
    *
    */
   public void validateErrorCode( String aCode ) {

      List<String> llist = getErrorCodeList();
      Assert.assertTrue( "Error code exists.", llist.contains( aCode ) );

   }


   /**
    * This function is to retrieve errors code list
    *
    *
    */

   public List<String> getErrorCodeList() {
      String[] iIds = { "RESULT_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      String iQueryString = "select c_block.result_cd " + " from c_block "
            + " inner join DL_REF_MESSAGE on " + " DL_REF_MESSAGE.result_cd=c_block.result_cd "
            + " UNION ALL " + " select c_block_dynamic_deadline.result_cd "
            + " from c_block_dynamic_deadline inner join DL_REF_MESSAGE on "
            + " DL_REF_MESSAGE.result_cd=c_block_dynamic_deadline.result_cd " + " UNION ALL "
            + " select c_proc_block_deadline.result_cd "
            + " from c_proc_block_deadline inner join DL_REF_MESSAGE on "
            + " DL_REF_MESSAGE.result_cd=c_proc_block_deadline.result_cd " + " UNION ALL "
            + " select c_block_dynamic_part_deadline.result_cd "
            + " from c_block_dynamic_part_deadline inner join DL_REF_MESSAGE on "
            + " DL_REF_MESSAGE.result_cd=c_block_dynamic_part_deadline.result_cd";
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      List<String> llistM = new ArrayList<String>();
      for ( int i = 0; i < llists.size(); i++ ) {
         llistM.add( llists.get( i ).get( 0 ) );
      }

      return llistM;

   }


   public void resetTaskIds() {
      iTASK_IDs = null;
      iTASK_DEFN_IDs = null;
      iTASK_IDs_2 = null;
      iTASK_DEFN_IDs_2 = null;
      iTASK_IDs_3 = null;
      iTASK_DEFN_IDs_3 = null;
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
   @Override
   public int runValidationAndImport( boolean ablnOnlyValidation, boolean allornone ) {
      int lrtValue = 0;
      ivalidationandimport = new ValidationAndImport() {

         @Override
         public int runValidation( boolean allornone ) {
            int lReturn = 0;
            CallableStatement lPrepareCallJICPART;

            try {

               lPrepareCallJICPART = getConnection()
                     .prepareCall( "BEGIN   block_import.validate_block(on_retcode =>?); END;" );

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
                     .prepareCall( "BEGIN block_import.import_block(on_retcode =>?); END;" );

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

}
