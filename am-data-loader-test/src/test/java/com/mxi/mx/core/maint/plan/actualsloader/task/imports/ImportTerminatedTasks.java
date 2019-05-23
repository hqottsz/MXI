package com.mxi.mx.core.maint.plan.actualsloader.task.imports;

import java.sql.Date;
import java.text.SimpleDateFormat;
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

import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;
import com.mxi.mx.util.ActualsLoaderTest;
import com.mxi.mx.util.StringUtils;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.TestConstants;
import com.mxi.mx.util.ValidationAndImport;
import com.mxi.mx.util.WhereClause;


/**
 * This test suite contains test cases on validation and import functionality of
 * mx_al_ctrller_pkg.execute_task_import on terminated tasks.
 *
 * @author ALICIA QIAN
 */
public class ImportTerminatedTasks extends ActualsLoaderTest {

   @Rule
   public TestName testName = new TestName();
   ValidationAndImport ivalidationandimport;

   // the following IDs need to be delete in after class
   public simpleIDs iEventIDs_1 = null;
   public simpleIDs iEventIDs_2 = null;
   public simpleIDs iEventIDs_3 = null;
   public simpleIDs iTaskIDs_1 = null;
   public simpleIDs iTaskIDs_2 = null;
   public simpleIDs iInvIDs_ACFT = null;
   public simpleIDs iTaskId = null;
   public simpleIDs iTaskId_2 = null;

   public String iEVENT_TYPE_CD = "TS";
   public static String iTask_CD = "AL_TASK";
   public static String iTask_CD_non_recurrring = "AT_TEST";
   public static String iTask_CD_TaskLink = "SYS-REQ-LEADER";
   public static String iTask_CD_TaskLink_SDESC = "System Requirement - Leader Task";
   public static String iTask_CD_TaskLink_FOLLOWER = "SYS-REQ-FOLLOWER";
   public static String iSN = "SN000001";
   public static String iDomain_Type_CD_CA = "CA";
   public static String iENG_UNIT_CD = "DAY";
   public static String iDomain_Type_CD_US = "US";
   public static String iENG_UNIT_CD_US = "HOUR";
   public static String iDATA_TYPE_CD_HOURS = "HOURS";
   public static String iDATA_TYPE_CD_CDY = "CDY";

   public static String iTask_CD_BLOCKCHAIN_1 = "RECURRING BLOCK CHAIN 1";
   public static String iTask_CD_BLOCKCHAIN_2 = "RECURRING BLOCK CHAIN 2";

   public static String iSTAGE_NOTE_SYSTEM = "Created by BULK data loading utility.";


   /**
    * Clean up after each individual test
    *
    * @throws Exception
    */
   @Override
   @After
   public void after() throws Exception {

      // clean up the event data
      RestoreData();
      super.after();
   }


   /**
    * Setup before executing each individual test
    *
    * @throws Exception
    */
   @Override
   @Before
   public void before() throws Exception {

      super.before();
      clearActualsLoaderTables();

   }


   /**
    * This function is to prepare data for test_AL_TASK_OPER_23036_LASTDUE_US_IMPORT
    *
    * @throws Exception
    *
    *
    */

   public void testOPER_22639_US_COMPLETE_VALIDATION( String aTask_cd ) throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'" + aTask_cd + "'" );
      // lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTask.put( "TERMINATED_DT", "to_date('2017/01/01','yyyy/mm/dd')" );
      lMapTask.put( "TERMINATED_NOTE", "'AUTO TEST'" );
      // lMapTask.put( "COMPLETION_DUE_DT", "to_date('2015/01/02','yyyy/mm/dd')" );
      lMapTask.put( "CUSTOM_START_DT", "null" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();

      // first completed record
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTaskSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskSched.put( "TASK_CD", "'" + aTask_cd + "'" );
      lMapTaskSched.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      lMapTaskSched.put( "COMPLETION_VALUE", "95" );
      lMapTaskSched.put( "COMPLETION_DUE_VALUE", "90" );
      lMapTaskSched.put( "CUSTOM_START_VALUE", "null" );
      lMapTaskSched.put( "CUSTOM_DUE_VALUE", "null" );
      lMapTaskSched.put( "TERMINATED_VALUE", "99" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // call express validation runner and result checker with expected error code
      checkTaskValidation_EXCEPTWARNING( "PASS" );
   }


   /**
    * This test is to verify importing recurring terminated task.
    *
    * @throws Exception
    *
    *
    */
   @Test
   public void testOPER_22639_COMPLETE_recurring_IMPORT() throws Exception {

      iEventIDs_1 = null;
      iEventIDs_2 = null;
      iEventIDs_3 = null;
      iTaskIDs_1 = null;

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOPER_22639_US_COMPLETE_VALIDATION( iTask_CD );

      System.out.println( "Finish validation" );

      // run validation
      runImportTasks();

      // Get event IDs
      iEventIDs_1 = getEventIDs( TestConstants.STATUS_COMPLETE, iTask_CD );
      iEventIDs_2 = getEventIDs( TestConstants.STATUS_TERMINATE, iTask_CD );

      // Validate evt_event
      Assert.assertTrue( "check event id 1 is not null", iEventIDs_1 != null );
      Assert.assertTrue( "check event id 2 is not null", iEventIDs_2 != null );

      VerifyEvtEvent( iEventIDs_1, iEVENT_TYPE_CD, iEventIDs_1, "1" );
      VerifyEvtEvent( iEventIDs_2, iEVENT_TYPE_CD, iEventIDs_2, "1" );

      // Get task ID
      iTaskIDs_1 = getTaskIDs( iTask_CD );
      // get inventory IDs
      simpleIDs iInvIDs_ACFT = getInvIDs( iSN );

      // verify sched_task table
      VerifySchedTask( iEventIDs_1, null, iTaskIDs_1, TestConstants.TASK_CLASS_CD_REQ,
            iInvIDs_ACFT );

      VerifySchedTask( iEventIDs_2, null, iTaskIDs_1, TestConstants.TASK_CLASS_CD_REQ,
            iInvIDs_ACFT );

      // verify evt_event_rel
      VerifyEvtEventRel( iEventIDs_1, TestConstants.REL_TYPE_CD_DEPT, iEventIDs_2 );

      // verify evt_inv
      String lQuery = "select 1 from " + TableUtil.EVT_INV + " where EVENT_DB_ID="
            + iEventIDs_1.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_1.getNO_ID();
      Assert.assertTrue( "Check evt_inv table to verify the record exists: 1",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.EVT_INV + " where EVENT_DB_ID="
            + iEventIDs_2.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_2.getNO_ID();
      Assert.assertTrue( "Check evt_inv table to verify the record exists: 2",
            RecordsExist( lQuery ) );

      // verify evt_stage
      VerifyEvtStage( iEventIDs_1, TestConstants.STATUS_COMPLETE, iSTAGE_NOTE_SYSTEM, "1" );
      VerifyEvtStage( iEventIDs_2, TestConstants.STATUS_TERMINATE, iSTAGE_NOTE_SYSTEM, "1" );
      VerifyEvtStage( iEventIDs_2, TestConstants.STATUS_TERMINATE, "AUTO TEST", "0" );

      // verify evt_inv_usage
      lQuery = "select 1 from " + TableUtil.EVT_INV_USAGE + " where EVENT_DB_ID="
            + iEventIDs_1.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_1.getNO_ID();
      Assert.assertTrue( "Check evt_inv_usage table to verify the record exists",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.EVT_INV_USAGE + " where EVENT_DB_ID="
            + iEventIDs_2.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_2.getNO_ID();
      Assert.assertTrue( "Check evt_inv_usage table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify evt_sched_dead
      VerifyEvtSchedDead( iEventIDs_1, TestConstants.STATUS_COMPLETE,
            TestConstants.SCHED_FROM_CD_CUSTOM, -999, -999 );

   }


   /**
    * This test is to verify importing non-recurring terminated task.
    *
    * @throws Exception
    *
    *
    */
   @Test
   public void testOPER_22639_COMPLETE_non_recurring_IMPORT() throws Exception {

      iEventIDs_1 = null;
      iEventIDs_2 = null;
      iEventIDs_3 = null;
      iTaskIDs_1 = null;

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOPER_22639_US_COMPLETE_VALIDATION( iTask_CD_non_recurrring );

      System.out.println( "Finish validation" );

      // run validation
      runImportTasks();

      // Get event IDs
      iEventIDs_1 = getEventIDs( TestConstants.STATUS_COMPLETE, iTask_CD_non_recurrring );
      iEventIDs_2 = getEventIDs( TestConstants.STATUS_TERMINATE, iTask_CD_non_recurrring );

      // Validate evt_event
      Assert.assertTrue( "check event id 1 is not null", iEventIDs_1 != null );
      Assert.assertTrue( "check event id 2 is not null", iEventIDs_2 != null );

      VerifyEvtEvent( iEventIDs_1, iEVENT_TYPE_CD, iEventIDs_1, "1" );
      VerifyEvtEvent( iEventIDs_2, iEVENT_TYPE_CD, iEventIDs_2, "1" );

      // Get task ID
      iTaskIDs_1 = getTaskIDs( iTask_CD_non_recurrring );
      // get inventory IDs
      simpleIDs iInvIDs_ACFT = getInvIDs( iSN );

      // verify sched_task table
      VerifySchedTask( iEventIDs_1, null, iTaskIDs_1, TestConstants.TASK_CLASS_CD_REQ,
            iInvIDs_ACFT );

      VerifySchedTask( iEventIDs_2, null, iTaskIDs_1, TestConstants.TASK_CLASS_CD_REQ,
            iInvIDs_ACFT );

      // verify evt_event_rel
      VerifyEvtEventRel( iEventIDs_1, TestConstants.REL_TYPE_CD_DEPT, iEventIDs_2 );

      // verify evt_inv
      String lQuery = "select 1 from " + TableUtil.EVT_INV + " where EVENT_DB_ID="
            + iEventIDs_1.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_1.getNO_ID();
      Assert.assertTrue( "Check evt_inv table to verify the record exists: 1",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.EVT_INV + " where EVENT_DB_ID="
            + iEventIDs_2.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_2.getNO_ID();
      Assert.assertTrue( "Check evt_inv table to verify the record exists: 2",
            RecordsExist( lQuery ) );

      // verify evt_stagenote
      VerifyEvtStage( iEventIDs_1, TestConstants.STATUS_COMPLETE, iSTAGE_NOTE_SYSTEM, "1" );
      VerifyEvtStage( iEventIDs_2, TestConstants.STATUS_TERMINATE, iSTAGE_NOTE_SYSTEM, "1" );
      VerifyEvtStage( iEventIDs_2, TestConstants.STATUS_TERMINATE, "AUTO TEST", "0" );

      // verify evt_inv_usage
      lQuery = "select 1 from " + TableUtil.EVT_INV_USAGE + " where EVENT_DB_ID="
            + iEventIDs_1.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_1.getNO_ID();
      Assert.assertTrue( "Check evt_inv_usage table to verify the record exists",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.EVT_INV_USAGE + " where EVENT_DB_ID="
            + iEventIDs_2.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_2.getNO_ID();
      Assert.assertTrue( "Check evt_inv_usage table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify evt_sched_dead
      lQuery = "select 1 from " + TableUtil.EVT_SCHED_DEAD + " where EVENT_DB_ID="
            + iEventIDs_1.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_1.getNO_ID();
      Assert.assertFalse( "Check evt_sched_dead table to verify the record exists",
            RecordsExist( lQuery ) );

   }


   /**
    * This test is to verify importing terminated task which has task dependency.
    *
    * @throws Exception
    *
    *
    */
   @Test
   public void testOPER_22639_COMPLETE_Task_link_IMPORT() throws Exception {

      iEventIDs_1 = null;
      iEventIDs_2 = null;
      iEventIDs_3 = null;
      iTaskIDs_1 = null;

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOPER_22639_US_COMPLETE_VALIDATION( iTask_CD_TaskLink );

      System.out.println( "Finish validation" );

      // run validation
      runImportTasks();

      // Get event IDs
      iEventIDs_1 = getEventIDs( TestConstants.STATUS_COMPLETE, iTask_CD_TaskLink,
            iTask_CD_TaskLink_SDESC );
      iEventIDs_2 = getEventIDs( TestConstants.STATUS_TERMINATE, iTask_CD_TaskLink,
            iTask_CD_TaskLink_SDESC );

      // Validate evt_event
      Assert.assertTrue( "check event id 1 is not null", iEventIDs_1 != null );
      Assert.assertTrue( "check event id 2 is not null", iEventIDs_2 != null );

      VerifyEvtEvent( iEventIDs_1, iEVENT_TYPE_CD, iEventIDs_1, "1" );
      VerifyEvtEvent( iEventIDs_2, iEVENT_TYPE_CD, iEventIDs_2, "1" );

      // Get task ID
      iTaskIDs_1 = getTaskIDs( iTask_CD_TaskLink );

      // verify sched_task table
      VerifySchedTask( iEventIDs_1, null, iTaskIDs_1, TestConstants.TASK_CLASS_CD_REQ, null );
      VerifySchedTask( iEventIDs_2, null, iTaskIDs_1, TestConstants.TASK_CLASS_CD_REQ, null );

      // verify evt_event_rel
      VerifyEvtEventRel( iEventIDs_1, TestConstants.REL_TYPE_CD_DEPT, iEventIDs_2 );

      // verify evt_inv
      String lQuery = "select 1 from " + TableUtil.EVT_INV + " where EVENT_DB_ID="
            + iEventIDs_1.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_1.getNO_ID();
      Assert.assertTrue( "Check evt_inv table to verify the record exists: 1",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.EVT_INV + " where EVENT_DB_ID="
            + iEventIDs_2.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_2.getNO_ID();
      Assert.assertTrue( "Check evt_inv table to verify the record exists: 2",
            RecordsExist( lQuery ) );

      // verify evt_stage
      VerifyEvtStage( iEventIDs_1, TestConstants.STATUS_COMPLETE, iSTAGE_NOTE_SYSTEM, "1" );
      VerifyEvtStage( iEventIDs_2, TestConstants.STATUS_TERMINATE, iSTAGE_NOTE_SYSTEM, "1" );
      VerifyEvtStage( iEventIDs_2, TestConstants.STATUS_TERMINATE, "AUTO TEST", "0" );

      // verify evt_inv_usage
      lQuery = "select 1 from " + TableUtil.EVT_INV_USAGE + " where EVENT_DB_ID="
            + iEventIDs_1.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_1.getNO_ID();
      Assert.assertTrue( "Check evt_inv_usage table to verify the record exists",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.EVT_INV_USAGE + " where EVENT_DB_ID="
            + iEventIDs_2.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_2.getNO_ID();
      Assert.assertTrue( "Check evt_inv_usage table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify evt_sched_dead
      VerifyEvtSchedDead( iEventIDs_1, TestConstants.STATUS_COMPLETE,
            TestConstants.SCHED_FROM_CD_CUSTOM, -999, -999 );

   }


   public void testOPER_22639_US_FirstTime_VALIDATION( String aTask_cd ) throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'" + aTask_cd + "'" );
      lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      // lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTask.put( "TERMINATED_DT", "to_date('2017/01/01','yyyy/mm/dd')" );
      lMapTask.put( "TERMINATED_NOTE", "'AUTO TEST'" );
      // lMapTask.put( "COMPLETION_DUE_DT", "to_date('2015/01/02','yyyy/mm/dd')" );
      lMapTask.put( "CUSTOM_START_DT", "null" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();

      // first completed record
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTaskSched.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTaskSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskSched.put( "TASK_CD", "'" + aTask_cd + "'" );
      // lMapTaskSched.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      // lMapTaskSched.put( "COMPLETION_VALUE", "95" );
      // lMapTaskSched.put( "COMPLETION_DUE_VALUE", "90" );
      // lMapTaskSched.put( "CUSTOM_START_VALUE", "null" );
      // lMapTaskSched.put( "CUSTOM_DUE_VALUE", "null" );
      lMapTaskSched.put( "TERMINATED_VALUE", "0" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // call express validation runner and result checker with expected error code
      checkTaskValidation_EXCEPTWARNING( "PASS" );
   }


   @Test
   public void testOPER_22639_FirstTime_recurring_IMPORT() throws Exception {

      iEventIDs_1 = null;
      iEventIDs_2 = null;
      iEventIDs_3 = null;
      iTaskIDs_1 = null;

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOPER_22639_US_FirstTime_VALIDATION( iTask_CD );

      System.out.println( "Finish validation" );

      // run validation
      runImportTasks();

      // Get event IDs
      iEventIDs_2 = getEventIDs( TestConstants.STATUS_TERMINATE, iTask_CD );

      // Validate evt_event
      Assert.assertTrue( "check event id 2 is not null", iEventIDs_2 != null );
      VerifyEvtEvent( iEventIDs_2, iEVENT_TYPE_CD, iEventIDs_2, "1" );

      // Get task ID
      iTaskIDs_1 = getTaskIDs( iTask_CD );
      // get inventory IDs
      simpleIDs iInvIDs_ACFT = getInvIDs( iSN );

      // verify sched_task table
      VerifySchedTask( iEventIDs_2, null, iTaskIDs_1, TestConstants.TASK_CLASS_CD_REQ,
            iInvIDs_ACFT );

      // verify evt_event_rel
      String lQuery = "select 1 from " + TableUtil.EVT_EVENT_REL + " where EVENT_DB_ID="
            + iEventIDs_2.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_2.getNO_ID();
      Assert.assertFalse( "Check evt_inv_usage table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify evt_inv
      lQuery = "select 1 from " + TableUtil.EVT_INV + " where EVENT_DB_ID="
            + iEventIDs_2.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_2.getNO_ID();
      Assert.assertTrue( "Check evt_inv table to verify the record exists: 2",
            RecordsExist( lQuery ) );

      // verify evt_stage
      VerifyEvtStage( iEventIDs_2, TestConstants.STATUS_TERMINATE, iSTAGE_NOTE_SYSTEM, "1" );
      VerifyEvtStage( iEventIDs_2, TestConstants.STATUS_TERMINATE, "AUTO TEST", "0" );

      // verify evt_inv_usage
      lQuery = "select 1 from " + TableUtil.EVT_INV_USAGE + " where EVENT_DB_ID="
            + iEventIDs_2.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_2.getNO_ID();
      Assert.assertTrue( "Check evt_inv_usage table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify evt_sched_dead
      lQuery = "select 1 from " + TableUtil.EVT_SCHED_DEAD + " where EVENT_DB_ID="
            + iEventIDs_2.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_2.getNO_ID();
      Assert.assertFalse( "Check evt_inv_usage table to verify the record exists",
            RecordsExist( lQuery ) );

   }


   @Test
   public void testOPER_22639_FirstTime_non_recurring_IMPORT() throws Exception {

      iEventIDs_1 = null;
      iEventIDs_2 = null;
      iEventIDs_3 = null;
      iTaskIDs_1 = null;

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOPER_22639_US_FirstTime_VALIDATION( iTask_CD_non_recurrring );

      System.out.println( "Finish validation" );

      // run validation
      runImportTasks();

      // Get event IDs
      iEventIDs_2 = getEventIDs( TestConstants.STATUS_TERMINATE, iTask_CD_non_recurrring );

      // Validate evt_event
      Assert.assertTrue( "check event id 2 is not null", iEventIDs_2 != null );
      VerifyEvtEvent( iEventIDs_2, iEVENT_TYPE_CD, iEventIDs_2, "1" );

      // Get task ID
      iTaskIDs_1 = getTaskIDs( iTask_CD_non_recurrring );
      // get inventory IDs
      simpleIDs iInvIDs_ACFT = getInvIDs( iSN );

      // verify sched_task table
      VerifySchedTask( iEventIDs_2, null, iTaskIDs_1, TestConstants.TASK_CLASS_CD_REQ,
            iInvIDs_ACFT );

      // verify evt_event_rel
      String lQuery = "select 1 from " + TableUtil.EVT_EVENT_REL + " where EVENT_DB_ID="
            + iEventIDs_2.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_2.getNO_ID();
      Assert.assertFalse( "Check evt_inv_usage table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify evt_inv
      lQuery = "select 1 from " + TableUtil.EVT_INV + " where EVENT_DB_ID="
            + iEventIDs_2.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_2.getNO_ID();
      Assert.assertTrue( "Check evt_inv table to verify the record exists: 2",
            RecordsExist( lQuery ) );

      // verify evt_stage
      VerifyEvtStage( iEventIDs_2, TestConstants.STATUS_TERMINATE, iSTAGE_NOTE_SYSTEM, "1" );
      VerifyEvtStage( iEventIDs_2, TestConstants.STATUS_TERMINATE, "AUTO TEST", "0" );

      // verify evt_inv_usage
      lQuery = "select 1 from " + TableUtil.EVT_INV_USAGE + " where EVENT_DB_ID="
            + iEventIDs_2.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_2.getNO_ID();
      Assert.assertTrue( "Check evt_inv_usage table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify evt_sched_dead
      lQuery = "select 1 from " + TableUtil.EVT_SCHED_DEAD + " where EVENT_DB_ID="
            + iEventIDs_2.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_2.getNO_ID();
      Assert.assertFalse( "Check evt_inv_usage table to verify the record exists",
            RecordsExist( lQuery ) );

   }


   @Test
   public void testOPER_22639_FristTime_Task_link_IMPORT() throws Exception {

      iEventIDs_1 = null;
      iEventIDs_2 = null;
      iEventIDs_3 = null;
      iTaskIDs_1 = null;

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOPER_22639_US_FirstTime_VALIDATION( iTask_CD_TaskLink );

      System.out.println( "Finish validation" );

      // run validation
      runImportTasks();

      // Get event IDs
      iEventIDs_2 = getEventIDs( TestConstants.STATUS_TERMINATE, iTask_CD_TaskLink,
            iTask_CD_TaskLink_SDESC );

      // Validate evt_event
      Assert.assertTrue( "check event id 2 is not null", iEventIDs_2 != null );
      VerifyEvtEvent( iEventIDs_2, iEVENT_TYPE_CD, iEventIDs_2, "1" );

      // Get task ID
      iTaskIDs_1 = getTaskIDs( iTask_CD_TaskLink );

      // verify sched_task table
      VerifySchedTask( iEventIDs_2, null, iTaskIDs_1, TestConstants.TASK_CLASS_CD_REQ, null );

      // verify evt_event_rel
      String lQuery = "select 1 from " + TableUtil.EVT_EVENT_REL + " where EVENT_DB_ID="
            + iEventIDs_2.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_2.getNO_ID();
      Assert.assertFalse( "Check evt_inv_usage table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify evt_inv
      lQuery = "select 1 from " + TableUtil.EVT_INV + " where EVENT_DB_ID="
            + iEventIDs_2.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_2.getNO_ID();
      Assert.assertTrue( "Check evt_inv table to verify the record exists: 2",
            RecordsExist( lQuery ) );

      // verify evt_stage
      VerifyEvtStage( iEventIDs_2, TestConstants.STATUS_TERMINATE, iSTAGE_NOTE_SYSTEM, "1" );
      VerifyEvtStage( iEventIDs_2, TestConstants.STATUS_TERMINATE, "AUTO TEST", "0" );

      // verify evt_inv_usage
      lQuery = "select 1 from " + TableUtil.EVT_INV_USAGE + " where EVENT_DB_ID="
            + iEventIDs_2.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_2.getNO_ID();
      Assert.assertTrue( "Check evt_inv_usage table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify evt_sched_dead
      lQuery = "select 1 from " + TableUtil.EVT_SCHED_DEAD + " where EVENT_DB_ID="
            + iEventIDs_2.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_2.getNO_ID();
      Assert.assertFalse( "Check evt_inv_usage table to verify the record exists",
            RecordsExist( lQuery ) );
   }


   // ========================================================================================

   /**
    * This function is to retrieve task ID by giving task_cd.
    *
    *
    *
    */

   @Override
   public simpleIDs getTaskIDs( String ataskCD ) {

      String[] iIds = { "TASK_DB_ID", "TASK_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_CD", ataskCD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_TASK, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lIds;
   }


   /**
    * This function is to retrieve event ID by giving task_cd and task status.
    *
    *
    *
    */
   @Override
   public simpleIDs getEventIDs( String aStatus, String ataskCD ) {

      String leventsdesc = ataskCD + " (" + ataskCD + ")";
      String[] iIds = { "EVENT_DB_ID", "EVENT_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "EVENT_SDESC", leventsdesc );
      lArgs.addArguments( "EVENT_STATUS_CD", aStatus );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EVT_EVENT, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lIds;
   }


   /**
    * This function is to retrieve event ID by giving task_cd and task status.
    *
    *
    *
    */
   public simpleIDs getEventIDs( String aStatus, String ataskCD, String aSDESC ) {

      String leventsdesc = ataskCD + " (" + aSDESC + ")";
      String[] iIds = { "EVENT_DB_ID", "EVENT_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "EVENT_SDESC", leventsdesc );
      lArgs.addArguments( "EVENT_STATUS_CD", aStatus );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EVT_EVENT, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lIds;
   }


   /**
    * This function is to retrieve interval value by giving task_cd
    *
    *
    *
    */

   public int getinterval( String aTask_CD ) {

      simpleIDs ltask_ids = getTaskIDs( aTask_CD );

      String[] iIds = { "DEF_INTERVAL_QT" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_DB_ID", ltask_ids.getNO_DB_ID() );
      lArgs.addArguments( "TASK_ID", ltask_ids.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_SCHED_RULE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      return Integer.parseInt( llists.get( 0 ).get( 0 ) );

   }

   //
   // /**
   // * This function is to retrieve current inventory value by giving serial no
   // *
   // *
   // *
   // */
   //
   // public int getCurrentUsage( String aSN ) {
   //
   // simpleIDs linv_ids = getInvIDs( aSN );
   //
   // String[] iIds = { "TSN_QT" };
   // List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
   // WhereClause lArgs = new WhereClause();
   // lArgs.addArguments( "INV_NO_DB_ID", linv_ids.getNO_DB_ID() );
   // lArgs.addArguments( "INV_NO_ID", linv_ids.getNO_ID() );
   //
   // String iQueryString = TableUtil.buildTableQuery( TableUtil.INV_CURR_USAGE, lfields, lArgs );
   // List<ArrayList<String>> llists = execute( iQueryString, lfields );
   //
   // return Integer.parseInt( llists.get( 0 ).get( 0 ) );
   //
   // }


   /**
    * This function is to verify evt_event table
    *
    *
    */

   public void VerifyEvtEvent( simpleIDs aIDs, String aEVENT_TYPE_CD, simpleIDs aHIDs,
         String aHIST_BOOL ) {

      String[] iIds = { "EVENT_TYPE_CD", "H_EVENT_DB_ID", "H_EVENT_ID", "HIST_BOOL" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "EVENT_DB_ID", aIDs.getNO_DB_ID() );
      lArgs.addArguments( "EVENT_ID", aIDs.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EVT_EVENT, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "EVENT_TYPE_CD",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aEVENT_TYPE_CD ) );
      Assert.assertTrue( "H_EVENT_DB_ID",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( aHIDs.getNO_DB_ID() ) );
      Assert.assertTrue( "H_EVENT_ID",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aHIDs.getNO_ID() ) );
      Assert.assertTrue( "HIST_BOOL", llists.get( 0 ).get( 3 ).equalsIgnoreCase( aHIST_BOOL ) );
   }


   /**
    * This function is to verify sched_task table
    *
    *
    */
   public void VerifySchedTask( simpleIDs aIDs, simpleIDs aHIDs, simpleIDs aTaskIDs,
         String aTASK_CLASS_CD, simpleIDs aINVIDs ) {

      String[] iIds = { "H_SCHED_DB_ID", "H_SCHED_ID", "TASK_DB_ID", "TASK_ID", "TASK_CLASS_CD",
            "MAIN_INV_NO_DB_ID", "MAIN_INV_NO_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "SCHED_DB_ID", aIDs.getNO_DB_ID() );
      lArgs.addArguments( "SCHED_ID", aIDs.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.SCHED_STASK, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      if ( aHIDs != null ) {
         Assert.assertTrue( "H_SCHED_DB_ID",
               llists.get( 0 ).get( 0 ).equalsIgnoreCase( aHIDs.getNO_DB_ID() ) );
         Assert.assertTrue( "H_SCHED_ID",
               llists.get( 0 ).get( 1 ).equalsIgnoreCase( aHIDs.getNO_ID() ) );
      }
      Assert.assertTrue( "TASK_DB_ID",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aTaskIDs.getNO_DB_ID() ) );
      Assert.assertTrue( "TASK_ID",
            llists.get( 0 ).get( 3 ).equalsIgnoreCase( aTaskIDs.getNO_ID() ) );
      Assert.assertTrue( "TASK_CLASS_CD",
            llists.get( 0 ).get( 4 ).equalsIgnoreCase( aTASK_CLASS_CD ) );
      if ( aINVIDs != null ) {
         Assert.assertTrue( "MAIN_INV_NO_DB_ID",
               llists.get( 0 ).get( 5 ).equalsIgnoreCase( aINVIDs.getNO_DB_ID() ) );
         Assert.assertTrue( "MAIN_INV_NO_ID",
               llists.get( 0 ).get( 6 ).equalsIgnoreCase( aINVIDs.getNO_ID() ) );
      }

   }


   /**
    * This function is to verify evt_event_rel table
    *
    *
    */
   public void VerifyEvtEventRel( simpleIDs aIDs, String aREL_TYPE_CD, simpleIDs aHIDs ) {

      String[] iIds = { "REL_EVENT_DB_ID", "REL_EVENT_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "EVENT_DB_ID", aIDs.getNO_DB_ID() );
      lArgs.addArguments( "EVENT_ID", aIDs.getNO_ID() );
      lArgs.addArguments( "REL_TYPE_CD", aREL_TYPE_CD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EVT_EVENT_REL, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "REL_EVENT_DB_ID",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aHIDs.getNO_DB_ID() ) );
      Assert.assertTrue( "REL_EVENT_ID",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( aHIDs.getNO_ID() ) );

   }


   /**
    * This function is to verify stage table
    *
    *
    */
   public void VerifyEvtStage( simpleIDs aIDs, String aEVENT_STATUS_CD, String aStageNote,
         String aSYSTEM_BOOL ) {

      String[] iIds = { "EVENT_STATUS_CD", "SYSTEM_BOOL" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "EVENT_DB_ID", aIDs.getNO_DB_ID() );
      lArgs.addArguments( "EVENT_ID", aIDs.getNO_ID() );
      lArgs.addArguments( "STAGE_NOTE", aStageNote );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EVT_STAGE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "EVENT_STATUS_CD",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aEVENT_STATUS_CD ) );
      Assert.assertTrue( "SYSTEM_BOOL", llists.get( 0 ).get( 1 ).equalsIgnoreCase( aSYSTEM_BOOL ) );

   }


   /**
    * This function is to verify evt_sched_dead table for US
    *
    *
    */
   public void VerifyEvtSchedDead( simpleIDs aIDs, String aStatus, String aSCHED_FROM_CD,
         int aSTART_QT, int aUSAGE_REM_QT ) {

      String[] iIds = { "SCHED_FROM_CD", "START_QT", "USAGE_REM_QT" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "EVENT_DB_ID", aIDs.getNO_DB_ID() );
      lArgs.addArguments( "EVENT_ID", aIDs.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EVT_SCHED_DEAD, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      if ( aSCHED_FROM_CD != null ) {
         Assert.assertTrue( "SCHED_FROM_CD",
               llists.get( 0 ).get( 0 ).equalsIgnoreCase( aSCHED_FROM_CD ) );
      }

      if ( aStatus.equalsIgnoreCase( TestConstants.STATUS_ACTV ) ) {

         Assert.assertTrue( "START_QT",
               llists.get( 0 ).get( 1 ).equalsIgnoreCase( String.valueOf( aSTART_QT ) ) );
         Assert.assertTrue( "USAGE_REM_QT",
               llists.get( 0 ).get( 2 ).equalsIgnoreCase( String.valueOf( aUSAGE_REM_QT ) ) );
      }

   }


   /**
    * This function is to verify evt_sched_dead table for CA
    *
    *
    */
   public void VerifyEvtSchedDead_CA( simpleIDs aIDs, String aStatus, String aSCHED_FROM_CD,
         Date aSTART_QT, Date aSCHED_DEAD_DT ) {

      String[] iIds = { "SCHED_FROM_CD", "START_QT", "START_DT", "SCHED_DEAD_DT" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "EVENT_DB_ID", aIDs.getNO_DB_ID() );
      lArgs.addArguments( "EVENT_ID", aIDs.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EVT_SCHED_DEAD, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      if ( aSCHED_FROM_CD != null ) {
         Assert.assertTrue( "SCHED_FROM_CD",
               llists.get( 0 ).get( 0 ).equalsIgnoreCase( aSCHED_FROM_CD ) );
      }

      Assert.assertTrue( "START_QT", StringUtils.isBlank( llists.get( 0 ).get( 1 ) ) );

      String pattern = "yyyy-MM-dd";
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat( pattern );

      if ( aStatus.equalsIgnoreCase( TestConstants.STATUS_ACTV ) ) {

         try {
            Assert.assertTrue( "START_QT",
                  DateDiffInDays(
                        new java.sql.Date( simpleDateFormat
                              .parse( llists.get( 0 ).get( 2 ).substring( 0, 10 ) ).getTime() ),
                        aSTART_QT ) == 0 );

            Assert.assertTrue( "SCHED_DEAD_DT",
                  DateDiffInDays(
                        new java.sql.Date( simpleDateFormat
                              .parse( llists.get( 0 ).get( 3 ).substring( 0, 10 ) ).getTime() ),
                        aSCHED_DEAD_DT ) == 0 );
         } catch ( Exception e ) {
            e.printStackTrace();
            Assert.assertTrue( "VerifyEvtSchedDead_CA.", false );
         }
      }

   }


   /**
    * This function is to retrieve data type ids by giving domain type cd and ENG Unit CD
    *
    *
    */
   public simpleIDs getDataTypeIDs( String aDomainTypeCd, String aEngUntiCd, String aDataTypeCd ) {

      String[] iIds = { "DATA_TYPE_DB_ID", "DATA_TYPE_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "DOMAIN_TYPE_CD", aDomainTypeCd );
      lArgs.addArguments( "ENG_UNIT_CD", aEngUntiCd );
      lArgs.addArguments( "DATA_TYPE_CD", aDataTypeCd );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.MIM_DATA_TYPE, lfields, lArgs );
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

      if ( iEventIDs_1 != null ) {

         // delete evt_sched_dead
         lStrDelete = "delete from " + TableUtil.EVT_SCHED_DEAD + "  where EVENT_DB_ID="
               + iEventIDs_1.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_1.getNO_ID();
         executeSQL( lStrDelete );

         // delete evt_inv_usage
         lStrDelete = "delete from " + TableUtil.EVT_INV_USAGE + "  where EVENT_DB_ID="
               + iEventIDs_1.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_1.getNO_ID();
         executeSQL( lStrDelete );

         // delete evt_stage
         lStrDelete = "delete from " + TableUtil.EVT_STAGE + "  where EVENT_DB_ID="
               + iEventIDs_1.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_1.getNO_ID();
         executeSQL( lStrDelete );

         // delete evt_inv
         lStrDelete = "delete from " + TableUtil.EVT_INV + "  where EVENT_DB_ID="
               + iEventIDs_1.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_1.getNO_ID();
         executeSQL( lStrDelete );

         // delete evt_event_rel
         lStrDelete = "delete from " + TableUtil.EVT_EVENT_REL + "  where EVENT_DB_ID="
               + iEventIDs_1.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_1.getNO_ID();
         executeSQL( lStrDelete );

         // delete sched_stask
         lStrDelete = "delete from " + TableUtil.SCHED_STASK + "  where SCHED_DB_ID="
               + iEventIDs_1.getNO_DB_ID() + " and SCHED_ID=" + iEventIDs_1.getNO_ID();
         executeSQL( lStrDelete );

         // delete evt_event
         lStrDelete = "delete from " + TableUtil.EVT_EVENT + "  where EVENT_DB_ID="
               + iEventIDs_1.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_1.getNO_ID();
         executeSQL( lStrDelete );

      }

      if ( iEventIDs_2 != null ) {

         // delete evt_sched_dead
         lStrDelete = "delete from " + TableUtil.EVT_SCHED_DEAD + "  where EVENT_DB_ID="
               + iEventIDs_2.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_2.getNO_ID();
         executeSQL( lStrDelete );

         // delete evt_inv_usage
         lStrDelete = "delete from " + TableUtil.EVT_INV_USAGE + "  where EVENT_DB_ID="
               + iEventIDs_2.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_2.getNO_ID();
         executeSQL( lStrDelete );

         // delete evt_stage
         lStrDelete = "delete from " + TableUtil.EVT_STAGE + "  where EVENT_DB_ID="
               + iEventIDs_2.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_2.getNO_ID();
         executeSQL( lStrDelete );

         // delete evt_inv
         lStrDelete = "delete from " + TableUtil.EVT_INV + "  where EVENT_DB_ID="
               + iEventIDs_2.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_2.getNO_ID();
         executeSQL( lStrDelete );

         // delete evt_event_rel
         lStrDelete = "delete from " + TableUtil.EVT_EVENT_REL + "  where EVENT_DB_ID="
               + iEventIDs_2.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_2.getNO_ID();
         executeSQL( lStrDelete );

         // delete sched_stask
         lStrDelete = "delete from " + TableUtil.SCHED_STASK + "  where SCHED_DB_ID="
               + iEventIDs_2.getNO_DB_ID() + " and SCHED_ID=" + iEventIDs_2.getNO_ID();
         executeSQL( lStrDelete );

         // delete evt_event
         lStrDelete = "delete from " + TableUtil.EVT_EVENT + "  where EVENT_DB_ID="
               + iEventIDs_2.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_2.getNO_ID();
         executeSQL( lStrDelete );

      }

      if ( iEventIDs_3 != null ) {

         // delete evt_sched_dead
         lStrDelete = "delete from " + TableUtil.EVT_SCHED_DEAD + "  where EVENT_DB_ID="
               + iEventIDs_3.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_3.getNO_ID();
         executeSQL( lStrDelete );

         // delete evt_inv_usage
         lStrDelete = "delete from " + TableUtil.EVT_INV_USAGE + "  where EVENT_DB_ID="
               + iEventIDs_3.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_3.getNO_ID();
         executeSQL( lStrDelete );

         // delete evt_stage
         lStrDelete = "delete from " + TableUtil.EVT_STAGE + "  where EVENT_DB_ID="
               + iEventIDs_3.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_3.getNO_ID();
         executeSQL( lStrDelete );

         // delete evt_inv
         lStrDelete = "delete from " + TableUtil.EVT_INV + "  where EVENT_DB_ID="
               + iEventIDs_3.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_3.getNO_ID();
         executeSQL( lStrDelete );

         // delete evt_event_rel
         lStrDelete = "delete from " + TableUtil.EVT_EVENT_REL + "  where EVENT_DB_ID="
               + iEventIDs_3.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_3.getNO_ID();
         executeSQL( lStrDelete );

         // delete sched_stask
         lStrDelete = "delete from " + TableUtil.SCHED_STASK + "  where SCHED_DB_ID="
               + iEventIDs_3.getNO_DB_ID() + " and SCHED_ID=" + iEventIDs_3.getNO_ID();
         executeSQL( lStrDelete );

         // delete evt_event
         lStrDelete = "delete from " + TableUtil.EVT_EVENT + "  where EVENT_DB_ID="
               + iEventIDs_3.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_3.getNO_ID();
         executeSQL( lStrDelete );

      }
   }

}
