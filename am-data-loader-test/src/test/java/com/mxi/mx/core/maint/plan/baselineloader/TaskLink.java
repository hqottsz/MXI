package com.mxi.mx.core.maint.plan.baselineloader;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;
import com.mxi.mx.core.maint.plan.datamodels.taskIDs;
import com.mxi.mx.util.BaselineLoaderTest;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.ValidationAndImport;
import com.mxi.mx.util.WhereClause;


/**
 * This test suite contains test cases on validation and import functionality on TASK_LINK_IMPORT.
 *
 * @author ALICIA QIAN
 */
public class TaskLink extends BaselineLoaderTest {

   @Rule
   public TestName testName = new TestName();
   ValidationAndImport ivalidationandimport;

   public String iASSMBL_CD_ACFT = "ACFT_CD1";
   public String iIPC_REF_CD_1 = "ACFT-SYS-1-1-TRK-TRK-TRK-PARENT";
   public String iTASK_CD_1 = "REQ2";
   public String iTASK_NAME_1 = "REQ2";

   public String iIPC_REF_CD_2 = "SYS-1-1";
   public String iTASK_CD_2 = "SYS-REQ12";
   public String iTASK_NAME_2 = "System Requirement";

   public String iIPC_REF_CD_3 = "SYS-1-1";
   public String iTASK_CD_3 = "BLOCK1";
   public String iTASK_NAME_3 = "AUTO BLOCK1";

   public String iIPC_REF_CD_4 = "SYS-1-1";
   public String iTASK_CD_4 = "SYS-JIC-1";
   public String iTASK_NAME_4 = "System Job Card 1";

   public String iIPC_REF_CD_5 = "SYS-1-1";
   public String iTASK_CD_5 = "ATREFTWO";
   public String iTASK_NAME_5 = "ATREFTWONAME";

   public String iIPC_REF_CD_6 = "SYS-1-1";
   public String iTASK_CD_6 = "ACFT-SYS-1-1-TRK-BATCH-PARENT-REPL";
   public String iTASK_NAME_6 = "BATCH-on-TRK Parent-REPLACEMENT";

   public String iIPC_REF_CD_7 = "SYS-1-1";
   public String iTASK_CD_7 = "SYS-CORR";
   public String iTASK_NAME_7 = "System Troubleshooting Task Requirement";

   public String iIPC_REF_CD_8 = "SYS-1-1";
   public String iTASK_CD_8 = "SYS-REQ-1-RB 19";
   public String iTASK_NAME_8 = "System Requirement for 1-Time Recurring Block 19";

   public String iDEP_IPC_REF_CD_1 = "APU-ASSY";
   public String iDEP_TASK_CD_1 = "REQ3";
   public String iDEP_TASK_NAME_1 = "REQ3";
   public String iTASK_DEP_ACTION_CD_CRT = "CRT";

   public String iDEP_IPC_REF_CD_2 = "SYS-1-1";
   public String iDEP_TASK_CD_2 = "SYS-REQ13";
   public String iDEP_TASK_NAME_2 = "System Requirement";

   public String iDEP_IPC_REF_CD_3 = "SYS-1-1";
   public String iDEP_TASK_CD_3 = "SYS-JIC-26";
   public String iDEP_TASK_NAME_3 = "System Job Card 26";

   public String iDEP_IPC_REF_CD_4 = "ENG-ASSY";
   public String iDEP_TASK_CD_4 = "BLOCK2";
   public String iDEP_TASK_NAME_4 = "AUTO BLOCK2";

   public String iDEP_IPC_REF_CD_5 = "ACFT-SYS-1-1-TRK-BATCH-PARENT";
   public String iDEP_TASK_CD_5 = "ATREFTEST";
   public String iDEP_TASK_NAME_5 = "ATREFTESTNAME";

   public String iTASK_DEP_ACTION_CD_COMPLETE = "COMPLETE";
   public String iTASK_DEP_ACTION_CD_TERMINATE = "TERMINATE";
   public String iTASK_DEP_ACTION_CD_COMPLIES = "COMPLIES";
   public String iTASK_DEP_ACTION_CD_POSTCRT = "POSTCRT";
   public String iTASK_DEP_ACTION_CD_OPPRTNSTC = "OPPRTNSTC";
   public String iTASK_DEP_ACTION_CD_REPLACES = "REPLACES";

   public simpleIDs iTASK_IDs = null;
   public simpleIDs iTASK_DEFN_IDs = null;
   public simpleIDs iDEP_TASK_IDs = null;
   public simpleIDs iDEP_TASK_DEFN_IDs = null;
   public simpleIDs iTASK_IDs_2 = null;
   public simpleIDs iTASK_DEFN_IDs_2 = null;
   public simpleIDs iDEP_TASK_IDs_2 = null;
   public simpleIDs iDEP_TASK_DEFN_IDs_2 = null;

   public LinkedList<String> iUniqueList = new LinkedList<String>();
   {
      iUniqueList.add( "REQ2" );
      iUniqueList.add( "SYS-REQ12" );
      iUniqueList.add( "BLOCK1" );
      iUniqueList.add( "SYS-JIC-1" );
      iUniqueList.add( "ATREFTWO" );
      iUniqueList.add( "REQ3" );
      iUniqueList.add( "SYS-REQ13" );
      iUniqueList.add( "SYS-JIC-26" );
      iUniqueList.add( "BLOCK2" );
      iUniqueList.add( "ATREFTEST" );
      iUniqueList.add( "ACFT-SYS-1-1-TRK-BATCH-PARENT-REPL" );
      iUniqueList.add( "SYS-REQ-1-RB 19" );
      iUniqueList.add( "SYS-CORR" );

   };

   public LinkedList<String> iUniqueValueList = new LinkedList<String>();


   /**
    * Clean up after each individual test
    *
    */
   @After
   @Override
   public void after() {
      RestoreData();
      String strSQL = null;
      for ( int i = 0; i < iUniqueList.size(); i++ ) {
         strSQL = "update task_task set  unique_bool='" + iUniqueValueList.get( i )
               + "'  WHERE task_cd='" + iUniqueList.get( i ) + "'";
         runUpdate( strSQL );

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
      String strSQL = null;
      for ( int i = 0; i < iUniqueList.size(); i++ ) {
         strSQL = "SELECT unique_bool FROM task_task WHERE task_cd='" + iUniqueList.get( i ) + "'";
         iUniqueValueList.add( i, getStringValueFromQuery( strSQL, "unique_bool" ) );

      }

      String strTCName = testName.getMethodName();
      if ( strTCName.contains( "testBLOCK_RECURRING_REQ_POSTCRT" ) ) {
         strSQL = "update task_task set unique_bool=0 where task_cd='" + iDEP_TASK_CD_4 + "'";
         runUpdate( strSQL );

      } else if ( strTCName.contains( "testREF_RECURRING_REQ_OPPRTNSTC" ) ) {
         strSQL = "update task_task set unique_bool=0 where task_cd='" + iDEP_TASK_CD_5 + "'";
         runUpdate( strSQL );
      }

   }


   /**
    *
    * This is to test validation functionality on TASK_LINK_IMPORT on req<->req on non-recurring
    * basis and CRT status.
    *
    */
   @Test
   public void testNON_RECURRING_REQ_REQ_CRT_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lTaskLinkMap = new LinkedHashMap<>();

      // C_TASK_LINK
      lTaskLinkMap.put( "ASSMBL_CD", "'" + iASSMBL_CD_ACFT + "'" );
      lTaskLinkMap.put( "IPC_REF_CD", "'" + iIPC_REF_CD_1 + "'" );
      lTaskLinkMap.put( "TASK_CD", "'" + iTASK_CD_1 + "'" );
      lTaskLinkMap.put( "DEP_IPC_REF_CD", "'" + iDEP_IPC_REF_CD_1 + "'" );
      lTaskLinkMap.put( "DEP_TASK_CD", "'" + iDEP_TASK_CD_1 + "'" );
      lTaskLinkMap.put( "TASK_DEP_ACTION_CD", "'" + iTASK_DEP_ACTION_CD_CRT + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_TASK_LINK, lTaskLinkMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );
   }


   /**
    *
    * This is to verify import functionality on TASK_LINK_IMPORT on req<->req on non-recurring basis
    * and CRT status.
    *
    */
   @Test
   public void testNON_RECURRING_REQ_REQ_CRT_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testNON_RECURRING_REQ_REQ_CRT_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs = null;
      iTASK_DEFN_IDs = null;
      iTASK_IDs_2 = null;
      iTASK_DEFN_IDs_2 = null;

      // Get task and task defn id for task
      taskIDs ltask_id_1 = getTaskIds( iTASK_CD_1, iTASK_NAME_1 );
      iTASK_DEFN_IDs = ltask_id_1.getTASK_DEFN_IDs();
      iTASK_IDs = ltask_id_1.getTASK_IDs();

      // Get task and task defn id for dep task
      taskIDs ldep_task_id_1 = getTaskIds( iDEP_TASK_CD_1, iDEP_TASK_NAME_1 );
      iDEP_TASK_DEFN_IDs = ldep_task_id_1.getTASK_DEFN_IDs();
      iDEP_TASK_IDs = ldep_task_id_1.getTASK_IDs();

      // verify task_task_dep

      verifyTASKTASkDEP( iTASK_IDs, iDEP_TASK_DEFN_IDs, iTASK_DEP_ACTION_CD_CRT );

   }


   /**
    *
    * This is to test validation functionality on TASK_LINK_IMPORT on req<->req on recurring basis
    * and COMPLETE status.
    *
    */

   @Test
   public void testRECURRING_REQ_REQ_COMPLETE_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lTaskLinkMap = new LinkedHashMap<>();

      // C_TASK_LINK
      lTaskLinkMap.put( "ASSMBL_CD", "'" + iASSMBL_CD_ACFT + "'" );
      lTaskLinkMap.put( "IPC_REF_CD", "'" + iIPC_REF_CD_2 + "'" );
      lTaskLinkMap.put( "TASK_CD", "'" + iTASK_CD_2 + "'" );
      lTaskLinkMap.put( "DEP_IPC_REF_CD", "'" + iDEP_IPC_REF_CD_2 + "'" );
      lTaskLinkMap.put( "DEP_TASK_CD", "'" + iDEP_TASK_CD_2 + "'" );
      lTaskLinkMap.put( "TASK_DEP_ACTION_CD", "'" + iTASK_DEP_ACTION_CD_COMPLETE + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_TASK_LINK, lTaskLinkMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );
   }


   /**
    *
    * This is to test import functionality on TASK_LINK_IMPORT on req<->req on recurring basis and
    * COMPLETE status.
    *
    */
   @Test
   public void testRECURRING_REQ_REQ_COMPLETE_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testRECURRING_REQ_REQ_COMPLETE_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs = null;
      iTASK_DEFN_IDs = null;
      iTASK_IDs_2 = null;
      iTASK_DEFN_IDs_2 = null;

      // Get task and task defn id for task
      taskIDs ltask_id_1 = getTaskIds( iTASK_CD_2, iTASK_NAME_2 );
      iTASK_DEFN_IDs = ltask_id_1.getTASK_DEFN_IDs();
      iTASK_IDs = ltask_id_1.getTASK_IDs();

      // Get task and task defn id for dep task
      taskIDs ldep_task_id_1 = getTaskIds( iDEP_TASK_CD_2, iDEP_TASK_NAME_2 );
      iDEP_TASK_DEFN_IDs = ldep_task_id_1.getTASK_DEFN_IDs();
      iDEP_TASK_IDs = ldep_task_id_1.getTASK_IDs();

      // verify task_task_dep

      verifyTASKTASkDEP( iTASK_IDs, iDEP_TASK_DEFN_IDs, iTASK_DEP_ACTION_CD_COMPLETE );

   }


   /**
    *
    * This is to test validate functionality on TASK_LINK_IMPORT on req<->req on
    * recurring/non-recurring basis and COMPLETE status.
    *
    */
   @Test
   public void testNON_RECURRING_REQ_RECURRING_REQ_TERMINATE_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lTaskLinkMap = new LinkedHashMap<>();

      // C_TASK_LINK
      lTaskLinkMap.put( "ASSMBL_CD", "'" + iASSMBL_CD_ACFT + "'" );
      lTaskLinkMap.put( "IPC_REF_CD", "'" + iIPC_REF_CD_1 + "'" );
      lTaskLinkMap.put( "TASK_CD", "'" + iTASK_CD_1 + "'" );
      lTaskLinkMap.put( "DEP_IPC_REF_CD", "'" + iDEP_IPC_REF_CD_2 + "'" );
      lTaskLinkMap.put( "DEP_TASK_CD", "'" + iDEP_TASK_CD_2 + "'" );
      lTaskLinkMap.put( "TASK_DEP_ACTION_CD", "'" + iTASK_DEP_ACTION_CD_TERMINATE + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_TASK_LINK, lTaskLinkMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );
   }


   /**
    *
    * This is to test import functionality on TASK_LINK_IMPORT on req<->req on
    * recurring/non-recurring basis and COMPLETE status.
    *
    */
   @Test
   public void testNON_RECURRING_REQ_RECURRING_REQ_TERMINATEE_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testNON_RECURRING_REQ_RECURRING_REQ_TERMINATE_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs = null;
      iTASK_DEFN_IDs = null;
      iTASK_IDs_2 = null;
      iTASK_DEFN_IDs_2 = null;

      // Get task and task defn id for task
      taskIDs ltask_id_1 = getTaskIds( iTASK_CD_1, iTASK_NAME_1 );
      iTASK_DEFN_IDs = ltask_id_1.getTASK_DEFN_IDs();
      iTASK_IDs = ltask_id_1.getTASK_IDs();

      // Get task and task defn id for dep task
      taskIDs ldep_task_id_1 = getTaskIds( iDEP_TASK_CD_2, iDEP_TASK_NAME_2 );
      iDEP_TASK_DEFN_IDs = ldep_task_id_1.getTASK_DEFN_IDs();
      iDEP_TASK_IDs = ldep_task_id_1.getTASK_IDs();

      // verify task_task_dep
      verifyTASKTASkDEP( iTASK_IDs, iDEP_TASK_DEFN_IDs, iTASK_DEP_ACTION_CD_TERMINATE );

   }


   /**
    *
    * This is to test validate functionality on TASK_LINK_IMPORT on req->jic on non-recurring basis
    * and COMPLIES status.
    *
    */
   @Test
   public void testNON_RECURRING_REQ_JIC_COMPLIES_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lTaskLinkMap = new LinkedHashMap<>();

      // C_TASK_LINK
      lTaskLinkMap.put( "ASSMBL_CD", "'" + iASSMBL_CD_ACFT + "'" );
      lTaskLinkMap.put( "IPC_REF_CD", "'" + iIPC_REF_CD_1 + "'" );
      lTaskLinkMap.put( "TASK_CD", "'" + iTASK_CD_1 + "'" );
      // lTaskLinkMap.put( "DEP_IPC_REF_CD", "'SYS-1-1'" );
      // lTaskLinkMap.put( "DEP_TASK_CD", "'SYS-JIC-26'" );
      // lTaskLinkMap.put( "TASK_DEP_ACTION_CD", "'COMPLIES'" );

      lTaskLinkMap.put( "DEP_IPC_REF_CD", "'" + iDEP_IPC_REF_CD_3 + "'" );
      lTaskLinkMap.put( "DEP_TASK_CD", "'" + iDEP_TASK_CD_3 + "'" );
      lTaskLinkMap.put( "TASK_DEP_ACTION_CD", "'" + iTASK_DEP_ACTION_CD_COMPLIES + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_TASK_LINK, lTaskLinkMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );
   }


   /**
    *
    * This is to test import functionality on TASK_LINK_IMPORT on req->jic on non-recurring basis
    * and COMPLIES status.
    *
    */

   @Test
   public void testNON_RECURRING_REQ_JIC_COMPLIES_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testNON_RECURRING_REQ_JIC_COMPLIES_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs = null;
      iTASK_DEFN_IDs = null;
      iTASK_IDs_2 = null;
      iTASK_DEFN_IDs_2 = null;

      // Get task and task defn id for task
      taskIDs ltask_id_1 = getTaskIds( iTASK_CD_1, iTASK_NAME_1 );
      iTASK_DEFN_IDs = ltask_id_1.getTASK_DEFN_IDs();
      iTASK_IDs = ltask_id_1.getTASK_IDs();

      // Get task and task defn id for dep task
      taskIDs ldep_task_id_1 = getTaskIds( iDEP_TASK_CD_3, iDEP_TASK_NAME_3 );
      iDEP_TASK_DEFN_IDs = ldep_task_id_1.getTASK_DEFN_IDs();
      iDEP_TASK_IDs = ldep_task_id_1.getTASK_IDs();

      // verify task_task_dep
      verifyTASKTASkDEP( iTASK_IDs, iDEP_TASK_DEFN_IDs, iTASK_DEP_ACTION_CD_COMPLIES );

   }


   /**
    *
    * This is to test validate functionality on TASK_LINK_IMPORT on req<-jic on non-recurring basis
    * and COMPLIES status.
    *
    */
   @Test
   public void testJIC_NON_RECURRING_REQ_COMPLIES_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lTaskLinkMap = new LinkedHashMap<>();

      lTaskLinkMap.put( "ASSMBL_CD", "'" + iASSMBL_CD_ACFT + "'" );
      lTaskLinkMap.put( "IPC_REF_CD", "'" + iDEP_IPC_REF_CD_3 + "'" );
      lTaskLinkMap.put( "TASK_CD", "'" + iDEP_TASK_CD_3 + "'" );

      lTaskLinkMap.put( "DEP_IPC_REF_CD", "'" + iIPC_REF_CD_1 + "'" );
      lTaskLinkMap.put( "DEP_TASK_CD", "'" + iTASK_CD_1 + "'" );
      lTaskLinkMap.put( "TASK_DEP_ACTION_CD", "'" + iTASK_DEP_ACTION_CD_COMPLIES + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_TASK_LINK, lTaskLinkMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );
   }


   /**
    *
    * This is to test import functionality on TASK_LINK_IMPORT on non-recurring req<-jic on COMPLIES
    * status.
    *
    */
   @Test
   public void testJIC_NON_RECURRING_REQ_COMPLIES_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testJIC_NON_RECURRING_REQ_COMPLIES_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs = null;
      iTASK_DEFN_IDs = null;
      iTASK_IDs_2 = null;
      iTASK_DEFN_IDs_2 = null;

      // Get task and task defn id for task
      taskIDs ltask_id_1 = getTaskIds( iDEP_TASK_CD_3, iDEP_TASK_NAME_3 );
      iTASK_DEFN_IDs = ltask_id_1.getTASK_DEFN_IDs();
      iTASK_IDs = ltask_id_1.getTASK_IDs();

      // Get task and task defn id for dep task
      taskIDs ldep_task_id_1 = getTaskIds( iTASK_CD_1, iTASK_NAME_1 );
      iDEP_TASK_DEFN_IDs = ldep_task_id_1.getTASK_DEFN_IDs();
      iDEP_TASK_IDs = ldep_task_id_1.getTASK_IDs();

      // verify task_task_dep
      verifyTASKTASkDEP( iTASK_IDs, iDEP_TASK_DEFN_IDs, iTASK_DEP_ACTION_CD_COMPLIES );

   }


   /**
    *
    * This is to test validate functionality on TASK_LINK_IMPORT on recurring req->block on POSTCRT
    * status.
    *
    */
   @Test
   public void testRECURRING_REQ_BLOCK_POSTCRT_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lTaskLinkMap = new LinkedHashMap<>();

      // C_TASK_LINK
      lTaskLinkMap.put( "ASSMBL_CD", "'" + iASSMBL_CD_ACFT + "'" );
      lTaskLinkMap.put( "IPC_REF_CD", "'" + iIPC_REF_CD_2 + "'" );
      lTaskLinkMap.put( "TASK_CD", "'" + iTASK_CD_2 + "'" );
      lTaskLinkMap.put( "DEP_IPC_REF_CD", "'" + iDEP_IPC_REF_CD_4 + "'" );
      lTaskLinkMap.put( "DEP_TASK_CD", "'" + iDEP_TASK_CD_4 + "'" );
      lTaskLinkMap.put( "TASK_DEP_ACTION_CD", "'" + iTASK_DEP_ACTION_CD_POSTCRT + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_TASK_LINK, lTaskLinkMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );
   }


   /**
    *
    * This is to test import functionality on TASK_LINK_IMPORT on recurring req->block on POSTCRT
    * status.
    *
    */

   @Test
   public void testRECURRING_REQ_BLOCK_POSTCRT_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testRECURRING_REQ_BLOCK_POSTCRT_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs = null;
      iTASK_DEFN_IDs = null;
      iTASK_IDs_2 = null;
      iTASK_DEFN_IDs_2 = null;

      // Get task and task defn id for task
      taskIDs ltask_id_1 = getTaskIds( iTASK_CD_2, iTASK_NAME_2 );
      iTASK_DEFN_IDs = ltask_id_1.getTASK_DEFN_IDs();
      iTASK_IDs = ltask_id_1.getTASK_IDs();

      // Get task and task defn id for dep task
      taskIDs ldep_task_id_1 = getTaskIds( iDEP_TASK_CD_4, iDEP_TASK_NAME_4 );
      iDEP_TASK_DEFN_IDs = ldep_task_id_1.getTASK_DEFN_IDs();
      iDEP_TASK_IDs = ldep_task_id_1.getTASK_IDs();

      // verify task_task_dep
      verifyTASKTASkDEP( iTASK_IDs, iDEP_TASK_DEFN_IDs, iTASK_DEP_ACTION_CD_POSTCRT );

   }


   /**
    *
    * This is to test validate functionality on TASK_LINK_IMPORT on recurring req<-block on POSTCRT
    * status.
    *
    */
   @Test
   public void testBLOCK_RECURRING_REQ_POSTCRT_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lTaskLinkMap = new LinkedHashMap<>();

      // C_TASK_LINK
      lTaskLinkMap.put( "ASSMBL_CD", "'" + iASSMBL_CD_ACFT + "'" );
      lTaskLinkMap.put( "IPC_REF_CD", "'" + iDEP_IPC_REF_CD_4 + "'" );
      lTaskLinkMap.put( "TASK_CD", "'" + iDEP_TASK_CD_4 + "'" );
      lTaskLinkMap.put( "DEP_IPC_REF_CD", "'" + iIPC_REF_CD_2 + "'" );
      lTaskLinkMap.put( "DEP_TASK_CD", "'" + iTASK_CD_2 + "'" );
      lTaskLinkMap.put( "TASK_DEP_ACTION_CD", "'" + iTASK_DEP_ACTION_CD_POSTCRT + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_TASK_LINK, lTaskLinkMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );
   }


   /**
    *
    * This is to test import functionality on TASK_LINK_IMPORT on recurring req<-block on POSTCRT
    * status.
    *
    */

   @Test
   public void testBLOCK_RECURRING_REQ_POSTCRT_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testBLOCK_RECURRING_REQ_POSTCRT_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs = null;
      iTASK_DEFN_IDs = null;
      iTASK_IDs_2 = null;
      iTASK_DEFN_IDs_2 = null;

      // Get task and task defn id for task
      taskIDs ltask_id_1 = getTaskIds( iDEP_TASK_CD_4, iDEP_TASK_NAME_4 );
      iTASK_IDs = ltask_id_1.getTASK_IDs();

      // Get task and task defn id for dep task
      taskIDs ldep_task_id_1 = getTaskIds( iTASK_CD_2, iTASK_NAME_2 );
      iDEP_TASK_DEFN_IDs = ldep_task_id_1.getTASK_DEFN_IDs();
      iDEP_TASK_IDs = ldep_task_id_1.getTASK_IDs();

      // verify task_task_dep
      verifyTASKTASkDEP( iTASK_IDs, iDEP_TASK_DEFN_IDs, iTASK_DEP_ACTION_CD_POSTCRT );

      // OPER-15064
      // Verify unique bool is updated to 1 for req and block task
      VerifyTask_TASK( iTASK_IDs, iDEP_TASK_CD_4, "1" );

   }


   /**
    *
    * This is to test validate functionality on TASK_LINK_IMPORT on recurring req->ref on OPPRTNSTC
    * status.
    *
    */
   @Test
   public void testRECURRING_REQ_REF_OPPRTNSTC_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lTaskLinkMap = new LinkedHashMap<>();

      // C_TASK_LINK
      lTaskLinkMap.put( "ASSMBL_CD", "'" + iASSMBL_CD_ACFT + "'" );
      lTaskLinkMap.put( "IPC_REF_CD", "'" + iIPC_REF_CD_2 + "'" );
      lTaskLinkMap.put( "TASK_CD", "'" + iTASK_CD_2 + "'" );
      lTaskLinkMap.put( "DEP_IPC_REF_CD", "'" + iDEP_IPC_REF_CD_5 + "'" );
      lTaskLinkMap.put( "DEP_TASK_CD", "'" + iDEP_TASK_CD_5 + "'" );
      lTaskLinkMap.put( "TASK_DEP_ACTION_CD", "'" + iTASK_DEP_ACTION_CD_OPPRTNSTC + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_TASK_LINK, lTaskLinkMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );
   }


   /**
    *
    * This is to test import functionality on TASK_LINK_IMPORT on recurring req->ref on OPPRTNSTC
    * status.
    *
    */
   @Test
   public void testRECURRING_REQ_REF_OPPRTNSTC_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testRECURRING_REQ_REF_OPPRTNSTC_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs = null;
      iTASK_DEFN_IDs = null;
      iTASK_IDs_2 = null;
      iTASK_DEFN_IDs_2 = null;

      // Get task and task defn id for task
      taskIDs ltask_id_1 = getTaskIds( iTASK_CD_2, iTASK_NAME_2 );
      iTASK_DEFN_IDs = ltask_id_1.getTASK_DEFN_IDs();
      iTASK_IDs = ltask_id_1.getTASK_IDs();

      // Get task and task defn id for dep task
      taskIDs ldep_task_id_1 = getTaskIds( iDEP_TASK_CD_5, iDEP_TASK_NAME_5 );
      iDEP_TASK_DEFN_IDs = ldep_task_id_1.getTASK_DEFN_IDs();
      iDEP_TASK_IDs = ldep_task_id_1.getTASK_IDs();

      // verify task_task_dep
      verifyTASKTASkDEP( iTASK_IDs, iDEP_TASK_DEFN_IDs, iTASK_DEP_ACTION_CD_OPPRTNSTC );

   }


   /**
    *
    * This is to test validate functionality on TASK_LINK_IMPORT on recurring req<-ref on OPPRTNSTC
    * status.
    *
    */
   @Test
   public void testREF_RECURRING_REQ_OPPRTNSTC_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lTaskLinkMap = new LinkedHashMap<>();

      // C_TASK_LINK
      lTaskLinkMap.put( "ASSMBL_CD", "'" + iASSMBL_CD_ACFT + "'" );
      lTaskLinkMap.put( "IPC_REF_CD", "'" + iDEP_IPC_REF_CD_5 + "'" );
      lTaskLinkMap.put( "TASK_CD", "'" + iDEP_TASK_CD_5 + "'" );
      lTaskLinkMap.put( "DEP_IPC_REF_CD", "'" + iIPC_REF_CD_2 + "'" );
      lTaskLinkMap.put( "DEP_TASK_CD", "'" + iTASK_CD_2 + "'" );
      lTaskLinkMap.put( "TASK_DEP_ACTION_CD", "'" + iTASK_DEP_ACTION_CD_OPPRTNSTC + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_TASK_LINK, lTaskLinkMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    *
    * This is to test validate functionality on TASK_LINK_IMPORT on recurring req<-ref on OPPRTNSTC
    * status.
    *
    */
   @Test
   public void testREF_RECURRING_REQ_OPPRTNSTC_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testREF_RECURRING_REQ_OPPRTNSTC_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs = null;
      iTASK_DEFN_IDs = null;
      iTASK_IDs_2 = null;
      iTASK_DEFN_IDs_2 = null;

      // Get task and task defn id for task
      taskIDs ltask_id_1 = getTaskIds( iDEP_TASK_CD_5, iDEP_TASK_NAME_5 );
      iTASK_DEFN_IDs = ltask_id_1.getTASK_DEFN_IDs();
      iTASK_IDs = ltask_id_1.getTASK_IDs();

      // Get task and task defn id for dep task
      taskIDs ldep_task_id_1 = getTaskIds( iTASK_CD_2, iTASK_NAME_2 );
      iDEP_TASK_DEFN_IDs = ldep_task_id_1.getTASK_DEFN_IDs();
      iDEP_TASK_IDs = ldep_task_id_1.getTASK_IDs();

      // verify task_task_dep
      verifyTASKTASkDEP( iTASK_IDs, iDEP_TASK_DEFN_IDs, iTASK_DEP_ACTION_CD_OPPRTNSTC );

      // OPER-15064
      // Verify unique bool is not updated for non req and non block task
      VerifyTask_TASK( iTASK_IDs, iDEP_TASK_CD_5, "0" );

   }


   /**
    *
    * This is to test validate functionality on TASK_LINK_IMPORT on recurring block->JIC on REPLACES
    * status.
    *
    */
   @Test
   public void testBLOCK_JIC_REPLACES_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lTaskLinkMap = new LinkedHashMap<>();

      // C_TASK_LINK
      lTaskLinkMap.put( "ASSMBL_CD", "'" + iASSMBL_CD_ACFT + "'" );
      lTaskLinkMap.put( "IPC_REF_CD", "'" + iDEP_IPC_REF_CD_4 + "'" );
      lTaskLinkMap.put( "TASK_CD", "'" + iDEP_TASK_CD_4 + "'" );
      lTaskLinkMap.put( "DEP_IPC_REF_CD", "'" + iDEP_IPC_REF_CD_3 + "'" );
      lTaskLinkMap.put( "DEP_TASK_CD", "'" + iDEP_TASK_CD_3 + "'" );
      lTaskLinkMap.put( "TASK_DEP_ACTION_CD", "'" + iTASK_DEP_ACTION_CD_REPLACES + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_TASK_LINK, lTaskLinkMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );
   }


   /**
    *
    * This is to test import functionality on TASK_LINK_IMPORT on recurring block->JIC on REPLACES
    * status.
    *
    */
   @Test
   public void testBLOCK_JIC_REPLACES_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testBLOCK_JIC_REPLACES_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs = null;
      iTASK_DEFN_IDs = null;
      iTASK_IDs_2 = null;
      iTASK_DEFN_IDs_2 = null;

      // Get task and task defn id for task
      taskIDs ltask_id_1 = getTaskIds( iDEP_TASK_CD_4, iDEP_TASK_NAME_4 );
      iTASK_IDs = ltask_id_1.getTASK_IDs();

      // Get task and task defn id for dep task
      taskIDs ldep_task_id_1 = getTaskIds( iDEP_TASK_CD_3, iDEP_TASK_NAME_3 );
      iDEP_TASK_DEFN_IDs = ldep_task_id_1.getTASK_DEFN_IDs();
      iDEP_TASK_IDs = ldep_task_id_1.getTASK_IDs();

      // verify task_task_dep
      verifyTASKTASkDEP( iTASK_IDs, iDEP_TASK_DEFN_IDs, iTASK_DEP_ACTION_CD_REPLACES );

   }


   /**
    *
    * This is to test validate functionality on TASK_LINK_IMPORT on recurring block<-JIC on REPLACES
    * status.
    *
    */
   @Test
   public void testJIC_BLOCK_REPLACES_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lTaskLinkMap = new LinkedHashMap<>();

      // C_TASK_LINK
      lTaskLinkMap.put( "ASSMBL_CD", "'" + iASSMBL_CD_ACFT + "'" );
      lTaskLinkMap.put( "IPC_REF_CD", "'" + iDEP_IPC_REF_CD_3 + "'" );
      lTaskLinkMap.put( "TASK_CD", "'" + iDEP_TASK_CD_3 + "'" );
      lTaskLinkMap.put( "DEP_IPC_REF_CD", "'" + iDEP_IPC_REF_CD_4 + "'" );
      lTaskLinkMap.put( "DEP_TASK_CD", "'" + iDEP_TASK_CD_4 + "'" );
      lTaskLinkMap.put( "TASK_DEP_ACTION_CD", "'" + iTASK_DEP_ACTION_CD_REPLACES + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_TASK_LINK, lTaskLinkMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );
   }


   /**
    *
    * This is to test import functionality on TASK_LINK_IMPORT on recurring block<-JIC on REPLACES
    * status.
    *
    */
   @Test
   public void testJIC_BLOCK_REPLACES_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testJIC_BLOCK_REPLACES_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs = null;
      iTASK_DEFN_IDs = null;
      iTASK_IDs_2 = null;
      iTASK_DEFN_IDs_2 = null;

      // Get task and task defn id for task
      taskIDs ltask_id_1 = getTaskIds( iDEP_TASK_CD_3, iDEP_TASK_NAME_3 );
      iTASK_IDs = ltask_id_1.getTASK_IDs();

      // Get task and task defn id for dep task
      taskIDs ldep_task_id_1 = getTaskIds( iDEP_TASK_CD_4, iDEP_TASK_NAME_4 );
      iDEP_TASK_DEFN_IDs = ldep_task_id_1.getTASK_DEFN_IDs();
      iDEP_TASK_IDs = ldep_task_id_1.getTASK_IDs();

      // verify task_task_dep
      verifyTASKTASkDEP( iTASK_IDs, iDEP_TASK_DEFN_IDs, iTASK_DEP_ACTION_CD_REPLACES );

   }


   /**
    *
    * This is to test validate functionality on TASK_LINK_IMPORT on recurring block->REF on COMPLETE
    * status.
    *
    */
   @Test
   public void testBLOCK_REF_COMPLETE_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lTaskLinkMap = new LinkedHashMap<>();

      // C_TASK_LINK
      lTaskLinkMap.put( "ASSMBL_CD", "'" + iASSMBL_CD_ACFT + "'" );
      lTaskLinkMap.put( "IPC_REF_CD", "'" + iDEP_IPC_REF_CD_4 + "'" );
      lTaskLinkMap.put( "TASK_CD", "'" + iDEP_TASK_CD_4 + "'" );
      lTaskLinkMap.put( "DEP_IPC_REF_CD", "'" + iDEP_IPC_REF_CD_5 + "'" );
      lTaskLinkMap.put( "DEP_TASK_CD", "'" + iDEP_TASK_CD_5 + "'" );
      lTaskLinkMap.put( "TASK_DEP_ACTION_CD", "'" + iTASK_DEP_ACTION_CD_COMPLETE + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_TASK_LINK, lTaskLinkMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );
   }


   /**
    *
    * This is to test import functionality on TASK_LINK_IMPORT on recurring block->REF on COMPLETE
    * status.
    *
    */
   @Test
   public void testBLOCK_REF_COMPLETE_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testBLOCK_REF_COMPLETE_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs = null;
      iTASK_DEFN_IDs = null;
      iTASK_IDs_2 = null;
      iTASK_DEFN_IDs_2 = null;

      // Get task and task defn id for task
      taskIDs ltask_id_1 = getTaskIds( iDEP_TASK_CD_4, iDEP_TASK_NAME_4 );
      iTASK_IDs = ltask_id_1.getTASK_IDs();

      // Get task and task defn id for dep task
      taskIDs ldep_task_id_1 = getTaskIds( iDEP_TASK_CD_5, iDEP_TASK_NAME_5 );
      iDEP_TASK_DEFN_IDs = ldep_task_id_1.getTASK_DEFN_IDs();
      iDEP_TASK_IDs = ldep_task_id_1.getTASK_IDs();

      // verify task_task_dep
      verifyTASKTASkDEP( iTASK_IDs, iDEP_TASK_DEFN_IDs, iTASK_DEP_ACTION_CD_COMPLETE );

   }


   /**
    *
    * This is to test validate functionality on TASK_LINK_IMPORT on recurring block<-REF on COMPLETE
    * status.
    *
    */
   @Test
   public void testREF_BLOCK_COMPLETE_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lTaskLinkMap = new LinkedHashMap<>();

      // C_TASK_LINK
      lTaskLinkMap.put( "ASSMBL_CD", "'" + iASSMBL_CD_ACFT + "'" );
      lTaskLinkMap.put( "IPC_REF_CD", "'" + iDEP_IPC_REF_CD_5 + "'" );
      lTaskLinkMap.put( "TASK_CD", "'" + iDEP_TASK_CD_5 + "'" );
      lTaskLinkMap.put( "DEP_IPC_REF_CD", "'" + iDEP_IPC_REF_CD_4 + "'" );
      lTaskLinkMap.put( "DEP_TASK_CD", "'" + iDEP_TASK_CD_4 + "'" );
      lTaskLinkMap.put( "TASK_DEP_ACTION_CD", "'" + iTASK_DEP_ACTION_CD_COMPLETE + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_TASK_LINK, lTaskLinkMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );
   }


   /**
    *
    * This is to test import functionality on TASK_LINK_IMPORT on recurring block->REF on COMPLETE
    * status.
    *
    */
   @Test
   public void testREF_BLOCK_COMPLETE_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testREF_BLOCK_COMPLETE_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs = null;
      iTASK_DEFN_IDs = null;
      iTASK_IDs_2 = null;
      iTASK_DEFN_IDs_2 = null;

      // Get task and task defn id for task
      taskIDs ltask_id_1 = getTaskIds( iDEP_TASK_CD_5, iDEP_TASK_NAME_5 );
      iTASK_IDs = ltask_id_1.getTASK_IDs();

      // Get task and task defn id for dep task
      taskIDs ldep_task_id_1 = getTaskIds( iDEP_TASK_CD_4, iDEP_TASK_NAME_4 );
      iDEP_TASK_DEFN_IDs = ldep_task_id_1.getTASK_DEFN_IDs();
      iDEP_TASK_IDs = ldep_task_id_1.getTASK_IDs();

      // verify task_task_dep
      verifyTASKTASkDEP( iTASK_IDs, iDEP_TASK_DEFN_IDs, iTASK_DEP_ACTION_CD_COMPLETE );

   }


   /**
    *
    * This is to test validate functionality on TASK_LINK_IMPORT on ref->jic on COMPLIES status.
    *
    */
   @Test
   public void testREF_JIC_COMPLIES_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lTaskLinkMap = new LinkedHashMap<>();

      // C_TASK_LINK
      lTaskLinkMap.put( "ASSMBL_CD", "'" + iASSMBL_CD_ACFT + "'" );
      lTaskLinkMap.put( "IPC_REF_CD", "'" + iDEP_IPC_REF_CD_5 + "'" );
      lTaskLinkMap.put( "TASK_CD", "'" + iDEP_TASK_CD_5 + "'" );

      lTaskLinkMap.put( "DEP_IPC_REF_CD", "'" + iDEP_IPC_REF_CD_3 + "'" );
      lTaskLinkMap.put( "DEP_TASK_CD", "'" + iDEP_TASK_CD_3 + "'" );
      lTaskLinkMap.put( "TASK_DEP_ACTION_CD", "'" + iTASK_DEP_ACTION_CD_COMPLIES + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_TASK_LINK, lTaskLinkMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );
   }


   /**
    *
    * This is to test import functionality on TASK_LINK_IMPORT on ref->jic on COMPLIES status.
    *
    */
   @Test
   public void testREF_JIC_COMPLIES_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testREF_JIC_COMPLIES_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs = null;
      iTASK_DEFN_IDs = null;
      iTASK_IDs_2 = null;
      iTASK_DEFN_IDs_2 = null;

      // Get task and task defn id for task
      taskIDs ltask_id_1 = getTaskIds( iDEP_TASK_CD_5, iDEP_TASK_NAME_5 );
      iTASK_IDs = ltask_id_1.getTASK_IDs();

      // Get task and task defn id for dep task
      taskIDs ldep_task_id_1 = getTaskIds( iDEP_TASK_CD_3, iDEP_TASK_NAME_3 );
      iDEP_TASK_DEFN_IDs = ldep_task_id_1.getTASK_DEFN_IDs();
      iDEP_TASK_IDs = ldep_task_id_1.getTASK_IDs();

      // verify task_task_dep
      verifyTASKTASkDEP( iTASK_IDs, iDEP_TASK_DEFN_IDs, iTASK_DEP_ACTION_CD_COMPLIES );

   }


   /**
    *
    * This is to test validate functionality on TASK_LINK_IMPORT on ref<-jic on COMPLIES status.
    *
    */
   @Test
   public void testJIC_REF_COMPLIES_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lTaskLinkMap = new LinkedHashMap<>();

      // C_TASK_LINK
      lTaskLinkMap.put( "ASSMBL_CD", "'" + iASSMBL_CD_ACFT + "'" );
      lTaskLinkMap.put( "IPC_REF_CD", "'" + iDEP_IPC_REF_CD_3 + "'" );
      lTaskLinkMap.put( "TASK_CD", "'" + iDEP_TASK_CD_3 + "'" );

      lTaskLinkMap.put( "DEP_IPC_REF_CD", "'" + iDEP_IPC_REF_CD_5 + "'" );
      lTaskLinkMap.put( "DEP_TASK_CD", "'" + iDEP_TASK_CD_5 + "'" );
      lTaskLinkMap.put( "TASK_DEP_ACTION_CD", "'" + iTASK_DEP_ACTION_CD_COMPLIES + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_TASK_LINK, lTaskLinkMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );
   }


   /**
    *
    * This is to test import functionality on TASK_LINK_IMPORT on ref<-jic on COMPLIES status.
    *
    */
   @Test
   public void testJIC_REF_COMPLIES_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testJIC_REF_COMPLIES_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs = null;
      iTASK_DEFN_IDs = null;
      iTASK_IDs_2 = null;
      iTASK_DEFN_IDs_2 = null;

      // Get task and task defn id for task
      taskIDs ltask_id_1 = getTaskIds( iDEP_TASK_CD_3, iDEP_TASK_NAME_3 );
      iTASK_IDs = ltask_id_1.getTASK_IDs();

      // Get task and task defn id for dep task
      taskIDs ldep_task_id_1 = getTaskIds( iDEP_TASK_CD_5, iDEP_TASK_NAME_5 );
      iDEP_TASK_DEFN_IDs = ldep_task_id_1.getTASK_DEFN_IDs();
      iDEP_TASK_IDs = ldep_task_id_1.getTASK_IDs();

      // verify task_task_dep
      verifyTASKTASkDEP( iTASK_IDs, iDEP_TASK_DEFN_IDs, iTASK_DEP_ACTION_CD_COMPLIES );

   }


   /**
    *
    * This is to test validate functionality on TASK_LINK_IMPORT on recurring block->block on CRT
    * status.
    *
    */
   @Test
   public void testBLOCK_BLOCK_CRT_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lTaskLinkMap = new LinkedHashMap<>();

      // C_TASK_LINK
      lTaskLinkMap.put( "ASSMBL_CD", "'" + iASSMBL_CD_ACFT + "'" );
      lTaskLinkMap.put( "IPC_REF_CD", "'" + iDEP_IPC_REF_CD_4 + "'" );
      lTaskLinkMap.put( "TASK_CD", "'" + iDEP_TASK_CD_4 + "'" );
      lTaskLinkMap.put( "DEP_IPC_REF_CD", "'" + iIPC_REF_CD_3 + "'" );
      lTaskLinkMap.put( "DEP_TASK_CD", "'" + iTASK_CD_3 + "'" );
      lTaskLinkMap.put( "TASK_DEP_ACTION_CD", "'" + iTASK_DEP_ACTION_CD_CRT + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_TASK_LINK, lTaskLinkMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );
   }


   /**
    *
    * This is to test import functionality on TASK_LINK_IMPORT on recurring block->block on CRT
    * status.
    *
    */
   @Test
   public void testBLOCK_BLOCK_CRT_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testBLOCK_BLOCK_CRT_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs = null;
      iTASK_DEFN_IDs = null;
      iTASK_IDs_2 = null;
      iTASK_DEFN_IDs_2 = null;

      // Get task and task defn id for task
      taskIDs ltask_id_1 = getTaskIds( iDEP_TASK_CD_4, iDEP_TASK_NAME_4 );
      iTASK_IDs = ltask_id_1.getTASK_IDs();

      // Get task and task defn id for dep task
      taskIDs ldep_task_id_1 = getTaskIds( iTASK_CD_3, iTASK_NAME_3 );
      iDEP_TASK_DEFN_IDs = ldep_task_id_1.getTASK_DEFN_IDs();
      iDEP_TASK_IDs = ldep_task_id_1.getTASK_IDs();

      // verify task_task_dep
      verifyTASKTASkDEP( iTASK_IDs, iDEP_TASK_DEFN_IDs, iTASK_DEP_ACTION_CD_CRT );

   }


   /**
    *
    * This is to test validate functionality on TASK_LINK_IMPORT on jic->jic on COMPLETE status.
    *
    */
   @Test
   public void testJIC_JIC_COMPLETE_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lTaskLinkMap = new LinkedHashMap<>();

      // C_TASK_LINK
      lTaskLinkMap.put( "ASSMBL_CD", "'" + iASSMBL_CD_ACFT + "'" );
      lTaskLinkMap.put( "IPC_REF_CD", "'" + iIPC_REF_CD_4 + "'" );
      lTaskLinkMap.put( "TASK_CD", "'" + iTASK_CD_4 + "'" );

      lTaskLinkMap.put( "DEP_IPC_REF_CD", "'" + iDEP_IPC_REF_CD_3 + "'" );
      lTaskLinkMap.put( "DEP_TASK_CD", "'" + iDEP_TASK_CD_3 + "'" );
      lTaskLinkMap.put( "TASK_DEP_ACTION_CD", "'" + iTASK_DEP_ACTION_CD_COMPLETE + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_TASK_LINK, lTaskLinkMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );
   }


   /**
    *
    * This is to test import functionality on TASK_LINK_IMPORT on jic->jic on COMPLETE status.
    *
    */
   @Test
   public void testJIC_JIC_COMPLETE_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testJIC_JIC_COMPLETE_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs = null;
      iTASK_DEFN_IDs = null;
      iTASK_IDs_2 = null;
      iTASK_DEFN_IDs_2 = null;

      // Get task and task defn id for task
      taskIDs ltask_id_1 = getTaskIds( iTASK_CD_4, iTASK_NAME_4 );
      iTASK_IDs = ltask_id_1.getTASK_IDs();

      // Get task and task defn id for dep task
      taskIDs ldep_task_id_1 = getTaskIds( iDEP_TASK_CD_3, iDEP_TASK_NAME_3 );
      iDEP_TASK_DEFN_IDs = ldep_task_id_1.getTASK_DEFN_IDs();
      iDEP_TASK_IDs = ldep_task_id_1.getTASK_IDs();

      // verify task_task_dep
      verifyTASKTASkDEP( iTASK_IDs, iDEP_TASK_DEFN_IDs, iTASK_DEP_ACTION_CD_COMPLETE );

      // OPER-15064
      // Verify unique bool is not updated to 1 for non req and non block task
      VerifyTask_TASK( iTASK_IDs, iTASK_CD_4, "0" );

   }


   /**
    *
    * This is to test validate functionality on TASK_LINK_IMPORT on ref->ref on TERMINATE status.
    *
    */
   @Test
   public void testJIC_JIC_TERMINATE_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lTaskLinkMap = new LinkedHashMap<>();

      // C_TASK_LINK
      lTaskLinkMap.put( "ASSMBL_CD", "'" + iASSMBL_CD_ACFT + "'" );
      lTaskLinkMap.put( "IPC_REF_CD", "'" + iDEP_IPC_REF_CD_5 + "'" );
      lTaskLinkMap.put( "TASK_CD", "'" + iDEP_TASK_CD_5 + "'" );

      lTaskLinkMap.put( "DEP_IPC_REF_CD", "'" + iIPC_REF_CD_5 + "'" );
      lTaskLinkMap.put( "DEP_TASK_CD", "'" + iTASK_CD_5 + "'" );
      lTaskLinkMap.put( "TASK_DEP_ACTION_CD", "'" + iTASK_DEP_ACTION_CD_TERMINATE + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_TASK_LINK, lTaskLinkMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );
   }


   /**
    *
    * This is to test import functionality on TASK_LINK_IMPORT on ref->ref on TERMINATE status.
    *
    */
   @Test
   public void testJIC_JIC_TERMINATE_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testJIC_JIC_TERMINATE_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs = null;
      iTASK_DEFN_IDs = null;
      iTASK_IDs_2 = null;
      iTASK_DEFN_IDs_2 = null;

      // Get task and task defn id for task
      taskIDs ltask_id_1 = getTaskIds( iDEP_TASK_CD_5, iDEP_TASK_NAME_5 );
      iTASK_IDs = ltask_id_1.getTASK_IDs();

      // Get task and task defn id for dep task
      taskIDs ldep_task_id_1 = getTaskIds( iTASK_CD_5, iTASK_NAME_5 );
      iDEP_TASK_DEFN_IDs = ldep_task_id_1.getTASK_DEFN_IDs();
      iDEP_TASK_IDs = ldep_task_id_1.getTASK_IDs();

      // verify task_task_dep
      verifyTASKTASkDEP( iTASK_IDs, iDEP_TASK_DEFN_IDs, iTASK_DEP_ACTION_CD_TERMINATE );

   }


   /**
    *
    * This is to test validate functionality on TASK_LINK_IMPORT on
    * NON_RECURRING_REQ_REQ_CRT_VALIDATION and testBLOCK_REF_COMPLETE_VALIDATION.
    *
    */
   @Test
   public void testMULTIPLE_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testNON_RECURRING_REQ_REQ_CRT_VALIDATION();
      testBLOCK_REF_COMPLETE_VALIDATION();

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );
   }


   /**
    *
    * This is to test validate functionality on TASK_LINK_IMPORT on
    * NON_RECURRING_REQ_REQ_CRT_VALIDATION and testBLOCK_REF_COMPLETE_VALIDATION.
    *
    */
   @Test
   public void testMULTIPLE_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testMULTIPLE_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs = null;
      iTASK_DEFN_IDs = null;
      iTASK_IDs_2 = null;
      iTASK_DEFN_IDs_2 = null;

      // Get task and task defn id for task
      taskIDs ltask_id_1 = getTaskIds( iTASK_CD_1, iTASK_NAME_1 );
      iTASK_DEFN_IDs = ltask_id_1.getTASK_DEFN_IDs();
      iTASK_IDs = ltask_id_1.getTASK_IDs();

      // Get task and task defn id for dep task
      taskIDs ldep_task_id_1 = getTaskIds( iDEP_TASK_CD_1, iDEP_TASK_NAME_1 );
      iDEP_TASK_DEFN_IDs = ldep_task_id_1.getTASK_DEFN_IDs();
      iDEP_TASK_IDs = ldep_task_id_1.getTASK_IDs();

      // verify task_task_dep
      verifyTASKTASkDEP( iTASK_IDs, iDEP_TASK_DEFN_IDs, iTASK_DEP_ACTION_CD_CRT );

      // ==============================================================================
      // Get task and task defn id for task
      taskIDs ltask_id_2 = getTaskIds( iDEP_TASK_CD_4, iDEP_TASK_NAME_4 );
      iTASK_IDs_2 = ltask_id_2.getTASK_IDs();

      // Get task and task defn id for dep task
      taskIDs ldep_task_id_2 = getTaskIds( iDEP_TASK_CD_5, iDEP_TASK_NAME_5 );
      iDEP_TASK_DEFN_IDs_2 = ldep_task_id_2.getTASK_DEFN_IDs();
      iDEP_TASK_IDs_2 = ldep_task_id_1.getTASK_IDs();

      // verify task_task_dep
      verifyTASKTASkDEP( iTASK_IDs_2, iDEP_TASK_DEFN_IDs_2, iTASK_DEP_ACTION_CD_COMPLETE );
   }


   /**
    *
    * This is to test validation functionality on TASK_LINK_IMPORT on req<->req on non-recurring
    * basis and CRT status. Verify unique bool is updated to 1 for req and block task
    *
    */
   @Test
   public void testOPER_15064_REQ_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lTaskLinkMap = new LinkedHashMap<>();

      // C_TASK_LINK
      lTaskLinkMap.put( "ASSMBL_CD", "'" + iASSMBL_CD_ACFT + "'" );
      lTaskLinkMap.put( "IPC_REF_CD", "'" + iIPC_REF_CD_8 + "'" );
      lTaskLinkMap.put( "TASK_CD", "'" + iTASK_CD_8 + "'" );
      lTaskLinkMap.put( "DEP_IPC_REF_CD", "'" + iDEP_IPC_REF_CD_1 + "'" );
      lTaskLinkMap.put( "DEP_TASK_CD", "'" + iDEP_TASK_CD_1 + "'" );
      lTaskLinkMap.put( "TASK_DEP_ACTION_CD", "'" + iTASK_DEP_ACTION_CD_REPLACES + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_TASK_LINK, lTaskLinkMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );
   }


   /**
    *
    * This is to verify import functionality on TASK_LINK_IMPORT on req<->req on non-recurring basis
    * and CRT status. Verify unique bool is updated to 1 for req and block task
    *
    */
   @Test
   public void testOPER_15064_REQ_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOPER_15064_REQ_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs = null;
      iTASK_DEFN_IDs = null;
      iTASK_IDs_2 = null;
      iTASK_DEFN_IDs_2 = null;

      // Get task and task defn id for task
      taskIDs ltask_id_1 = getTaskIds( iTASK_CD_8, iTASK_NAME_8 );
      iTASK_DEFN_IDs = ltask_id_1.getTASK_DEFN_IDs();
      iTASK_IDs = ltask_id_1.getTASK_IDs();

      // Get task and task defn id for dep task
      taskIDs ldep_task_id_1 = getTaskIds( iDEP_TASK_CD_1, iDEP_TASK_NAME_1 );
      iDEP_TASK_DEFN_IDs = ldep_task_id_1.getTASK_DEFN_IDs();
      iDEP_TASK_IDs = ldep_task_id_1.getTASK_IDs();

      // verify task_task_dep
      verifyTASKTASkDEP( iTASK_IDs, iDEP_TASK_DEFN_IDs, iTASK_DEP_ACTION_CD_REPLACES );

      // OPER-15064
      // Verify unique bool is updated to 1 for req and block task
      VerifyTask_TASK( iTASK_IDs, iTASK_CD_8, "1" );

   }


   /**
    *
    * This is to test validation functionality on TASK_LINK_IMPORT on req<->req on non-recurring
    * basis and CRT status. Verify unique bool is not updated to 1 for corr and repl
    *
    */
   @Test
   public void testOPER_15064_COOR_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lTaskLinkMap = new LinkedHashMap<>();

      // C_TASK_LINK
      lTaskLinkMap.put( "ASSMBL_CD", "'" + iASSMBL_CD_ACFT + "'" );
      lTaskLinkMap.put( "IPC_REF_CD", "'" + iIPC_REF_CD_7 + "'" );
      lTaskLinkMap.put( "TASK_CD", "'" + iTASK_CD_7 + "'" );
      lTaskLinkMap.put( "DEP_IPC_REF_CD", "'" + iDEP_IPC_REF_CD_1 + "'" );
      lTaskLinkMap.put( "DEP_TASK_CD", "'" + iDEP_TASK_CD_1 + "'" );
      lTaskLinkMap.put( "TASK_DEP_ACTION_CD", "'" + iTASK_DEP_ACTION_CD_CRT + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_TASK_LINK, lTaskLinkMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );
   }


   /**
    *
    * This is to test validation functionality on TASK_LINK_IMPORT on req<->req on non-recurring
    * basis and CRT status. Verify unique bool is not updated to 1 for corr and repl
    *
    */
   @Test
   public void testOPER_15064_CORR_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOPER_15064_COOR_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs = null;
      iTASK_DEFN_IDs = null;
      iTASK_IDs_2 = null;
      iTASK_DEFN_IDs_2 = null;

      // Get task and task defn id for task
      taskIDs ltask_id_1 = getTaskIds( iTASK_CD_7, iTASK_NAME_7 );
      iTASK_DEFN_IDs = ltask_id_1.getTASK_DEFN_IDs();
      iTASK_IDs = ltask_id_1.getTASK_IDs();

      // Get task and task defn id for dep task
      taskIDs ldep_task_id_1 = getTaskIds( iDEP_TASK_CD_1, iDEP_TASK_NAME_1 );
      iDEP_TASK_DEFN_IDs = ldep_task_id_1.getTASK_DEFN_IDs();
      iDEP_TASK_IDs = ldep_task_id_1.getTASK_IDs();

      // verify task_task_dep
      verifyTASKTASkDEP( iTASK_IDs, iDEP_TASK_DEFN_IDs, iTASK_DEP_ACTION_CD_CRT );

      // OPER-15064
      // Verify unique bool is not updated to 1 for CORR req
      VerifyTask_TASK( iTASK_IDs, iTASK_CD_7, "0" );

   }


   /**
    *
    * This is to test validation functionality on TASK_LINK_IMPORT on req<->req on non-recurring
    * basis and CRT status. Verify unique bool is not updated to 1 for corr and repl
    *
    */
   @Test
   public void testOPER_15064_REPL_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lTaskLinkMap = new LinkedHashMap<>();

      // C_TASK_LINK
      lTaskLinkMap.put( "ASSMBL_CD", "'" + iASSMBL_CD_ACFT + "'" );
      lTaskLinkMap.put( "IPC_REF_CD", "'" + iIPC_REF_CD_6 + "'" );
      lTaskLinkMap.put( "TASK_CD", "'" + iTASK_CD_6 + "'" );
      lTaskLinkMap.put( "DEP_IPC_REF_CD", "'" + iDEP_IPC_REF_CD_1 + "'" );
      lTaskLinkMap.put( "DEP_TASK_CD", "'" + iDEP_TASK_CD_1 + "'" );
      lTaskLinkMap.put( "TASK_DEP_ACTION_CD", "'" + iTASK_DEP_ACTION_CD_CRT + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_TASK_LINK, lTaskLinkMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );
   }


   /**
    *
    * This is to test validation functionality on TASK_LINK_IMPORT on req<->req on non-recurring
    * basis and CRT status. Verify unique bool is not updated to 1 for corr and repl
    *
    */
   @Test
   public void testOPER_15064_REPL_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOPER_15064_REPL_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTASK_IDs = null;
      iTASK_DEFN_IDs = null;
      iTASK_IDs_2 = null;
      iTASK_DEFN_IDs_2 = null;

      // Get task and task defn id for task
      taskIDs ltask_id_1 = getTaskIds( iTASK_CD_6, iTASK_NAME_6 );
      iTASK_DEFN_IDs = ltask_id_1.getTASK_DEFN_IDs();
      iTASK_IDs = ltask_id_1.getTASK_IDs();

      // Get task and task defn id for dep task
      taskIDs ldep_task_id_1 = getTaskIds( iDEP_TASK_CD_1, iDEP_TASK_NAME_1 );
      iDEP_TASK_DEFN_IDs = ldep_task_id_1.getTASK_DEFN_IDs();
      iDEP_TASK_IDs = ldep_task_id_1.getTASK_IDs();

      // verify task_task_dep
      verifyTASKTASkDEP( iTASK_IDs, iDEP_TASK_DEFN_IDs, iTASK_DEP_ACTION_CD_CRT );

      // OPER-15064
      // Verify unique bool is updated to 1 for req and block task
      VerifyTask_TASK( iTASK_IDs, iTASK_CD_6, "0" );

   }


   // ===============================================================================================================

   /**
    * This function is to verify TASK_TASK table
    *
    *
    */
   public void VerifyTask_TASK( simpleIDs aIDs, String aTASK_CD, String aUNIQUE_BOOL ) {

      String[] iIds = { "UNIQUE_BOOL" };

      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_DB_ID", aIDs.getNO_DB_ID() );
      lArgs.addArguments( "TASK_ID", aIDs.getNO_ID() );
      lArgs.addArguments( "TASK_CD", aTASK_CD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_TASK, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "UNIQUE_BOOL ",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aUNIQUE_BOOL ) );

   }


   /**
    * This function is to verify task_task_dep table.
    *
    *
    */

   public void verifyTASKTASkDEP( simpleIDs aTaskIds, simpleIDs aDEPIds,
         String aTASK_DEP_ACTION_CD ) {
      String[] iIds = { "TASK_DEP_ACTION_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_DB_ID", aTaskIds.getNO_DB_ID() );
      lArgs.addArguments( "TASK_ID", aTaskIds.getNO_ID() );
      lArgs.addArguments( "DEP_TASK_DEFN_DB_ID", aDEPIds.getNO_DB_ID() );
      lArgs.addArguments( "DEP_TASK_DEFN_ID", aDEPIds.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_TASK_DEP, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "TASK_DEP_ACTION_CD",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aTASK_DEP_ACTION_CD ) );

   }


   /**
    * This function is to retrieve task IDs from task_task table.
    *
    *
    */
   public taskIDs getTaskIds( String aTASK_CD, String aTASK_NAME ) {

      String[] iIds = { "TASK_DEFN_DB_ID", "TASK_DEFN_ID", "TASK_DB_ID", "TASK_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_CD", aTASK_CD );
      lArgs.addArguments( "TASK_NAME", aTASK_NAME );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_TASK, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      taskIDs lIds =
            new taskIDs( new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) ),
                  new simpleIDs( llists.get( 0 ).get( 2 ), llists.get( 0 ).get( 3 ) ) );
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

         // delete task_task_dep
         lStrDelete = "delete from " + TableUtil.TASK_TASK_DEP + "  where TASK_DB_ID="
               + iTASK_IDs.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs.getNO_ID();
         executeSQL( lStrDelete );

      }

      if ( iTASK_IDs_2 != null ) {

         // delete task_task_dep
         lStrDelete = "delete from " + TableUtil.TASK_TASK_DEP + "  where TASK_DB_ID="
               + iTASK_IDs_2.getNO_DB_ID() + " and TASK_ID=" + iTASK_IDs_2.getNO_ID();
         executeSQL( lStrDelete );

      }

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

               lPrepareCallJICPART = getConnection().prepareCall(
                     "BEGIN  task_link_import.validate_task_link(on_retcode =>?); END;" );

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

               lPrepareCallKIT = getConnection().prepareCall(
                     "BEGIN  task_link_import.import_task_link(on_retcode =>?); END;" );

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
