package com.mxi.mx.core.maint.plan.actualsloader.task.imports;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
 * mx_al_ctrller_pkg.execute_task_import on Task deadline scheduled .
 *
 * @author ALICIA QIAN
 */
public class ImportTaskDeadlineTest extends ActualsLoaderTest {

   @Rule
   public TestName testName = new TestName();
   ValidationAndImport ivalidationandimport;

   // the following IDs need to be delete in after class
   public simpleIDs iEventIDs_1 = null;
   public simpleIDs iEventIDs_2 = null;
   public simpleIDs iTaskIDs_1 = null;
   public simpleIDs iTaskIDs_2 = null;
   public simpleIDs iInvIDs_ACFT = null;
   public simpleIDs iTaskId = null;
   public simpleIDs iTaskId_2 = null;

   public String iEVENT_TYPE_CD = "TS";
   public static String iTask_CD = "AL_APPLIC_RULE";
   public static String iSN = "SN000001";
   public static String iDomain_Type_CD_CA = "CA";
   public static String iENG_UNIT_CD = "DAY";
   public static String iDomain_Type_CD_US = "US";
   public static String iENG_UNIT_CD_US = "HOUR";
   public static String iDATA_TYPE_CD_HOURS = "HOURS";
   public static String iDATA_TYPE_CD_CDY = "CDY";

   public static String iTask_CD_BLOCKCHAIN_1 = "RECURRING BLOCK CHAIN 1";
   public static String iTask_CD_BLOCKCHAIN_2 = "RECURRING BLOCK CHAIN 2";


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

      String strTCName = testName.getMethodName();
      if ( strTCName.contains( "_US_" ) ) {
         dataSetup_us();
      } else if ( strTCName.contains( "_CA_" ) ) {
         dataSetup_ca();

      } else if ( strTCName.contains( "_BLOCKCHAINUS_" ) ) {
         dataSetup_BLKCHN_us();

      } else {
         dataSetup_BLKCHN_ca();

      }
   }


   /**
    * This test is to verify mx_al_ctrller_pkg.execute_task_import functionality of on OPER-23036
    * Task with usage hours get last due if previous task is completed inside window of prefixed and
    * postfixed
    *
    *
    */

   @Test
   public void test_AL_TASK_OPER_23036_LASTDUE_US_IMPORT() {

      int LCompletionDueValue = 90;
      // int lCompletionValue = 95;

      iEventIDs_1 = null;
      iEventIDs_2 = null;
      iTaskIDs_1 = null;

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      test_AL_TASK_OPER_23036_LASTDUE_US_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runImportTasks();

      // Get event IDs
      iEventIDs_1 = getEventIDs( TestConstants.STATUS_COMPLETE, iTask_CD );
      iEventIDs_2 = getEventIDs( TestConstants.STATUS_ACTV, iTask_CD );

      // Validate evt_event
      Assert.assertTrue( "check event id 1 is not null", iEventIDs_1 != null );
      Assert.assertTrue( "check event id 2 is not null", iEventIDs_2 != null );

      VerifyEvtEvent( iEventIDs_1, iEVENT_TYPE_CD, iEventIDs_1, "1" );
      VerifyEvtEvent( iEventIDs_2, iEVENT_TYPE_CD, iEventIDs_2, "0" );

      // Get task ID
      iTaskIDs_1 = getTaskIDs( iTask_CD );
      // get inventory IDs
      simpleIDs iInvIDs_ACFT = getInvIDs( iSN );

      // verify sched_task table
      VerifySchedTask( iEventIDs_1, null, iTaskIDs_1, TestConstants.TASK_CLASS_CD_REQ,
            iInvIDs_ACFT );

      VerifySchedTask( iEventIDs_2, iEventIDs_2, iTaskIDs_1, TestConstants.TASK_CLASS_CD_REQ,
            iInvIDs_ACFT );

      // verify evt_event_rel
      VerifyEvtEventRel( iEventIDs_1, TestConstants.REL_TYPE_CD_DEPT, iEventIDs_2 );
      VerifyEvtEventRel( iEventIDs_2, TestConstants.REL_TYPE_CD_DRVTASK, iEventIDs_2 );

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
      VerifyEvtStage( iEventIDs_1, TestConstants.STATUS_COMPLETE );
      VerifyEvtStage( iEventIDs_2, TestConstants.STATUS_ACTV );

      // verify evt_inv_usage
      lQuery = "select 1 from " + TableUtil.EVT_INV_USAGE + " where EVENT_DB_ID="
            + iEventIDs_1.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_1.getNO_ID();
      Assert.assertTrue( "Check evt_inv_usage table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify evt_sched_dead
      simpleIDs ldatatypeID = getDataTypeIDs( "HOURS", "Flying Hours", "HOUR", "US" );
      int lInterval = getinterval( iTask_CD, ldatatypeID );
      int lCurrentUsage = getCurrentUsage( iSN, ldatatypeID );

      VerifyEvtSchedDead( iEventIDs_1, TestConstants.STATUS_COMPLETE,
            TestConstants.SCHED_FROM_CD_CUSTOM, -999, -999 );
      VerifyEvtSchedDead( iEventIDs_2, TestConstants.STATUS_ACTV,
            TestConstants.SCHED_FROM_CD_LASTDUE, LCompletionDueValue,
            LCompletionDueValue - lCurrentUsage + lInterval );

   }


   /**
    * This test is to verify mx_al_ctrller_pkg.execute_task_import functionality of on OPER-23036
    * Task with usage hours get last end if previous task is completed outside window of prefixed
    * and postfixed
    *
    *
    */

   @Test
   public void test_AL_TASK_OPER_23036_LASTEND_US_IMPORT() {

      // int LCompletionDueValue = 80;
      int lCompletionValue = 95;

      iEventIDs_1 = null;
      iEventIDs_2 = null;
      iTaskIDs_1 = null;

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      test_AL_TASK_OPER_23036_LASTEND_US_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runImportTasks();

      // Get event IDs
      iEventIDs_1 = getEventIDs( TestConstants.STATUS_COMPLETE, iTask_CD );
      iEventIDs_2 = getEventIDs( TestConstants.STATUS_ACTV, iTask_CD );

      // Validate evt_event
      Assert.assertTrue( "check event id 1 is not null", iEventIDs_1 != null );
      Assert.assertTrue( "check event id 2 is not null", iEventIDs_2 != null );

      VerifyEvtEvent( iEventIDs_1, iEVENT_TYPE_CD, iEventIDs_1, "1" );
      VerifyEvtEvent( iEventIDs_2, iEVENT_TYPE_CD, iEventIDs_2, "0" );

      // Get task ID
      iTaskIDs_1 = getTaskIDs( iTask_CD );
      // get inventory IDs
      simpleIDs iInvIDs_ACFT = getInvIDs( iSN );

      // verify sched_task table
      VerifySchedTask( iEventIDs_1, null, iTaskIDs_1, TestConstants.TASK_CLASS_CD_REQ,
            iInvIDs_ACFT );

      VerifySchedTask( iEventIDs_2, iEventIDs_2, iTaskIDs_1, TestConstants.TASK_CLASS_CD_REQ,
            iInvIDs_ACFT );

      // verify evt_event_rel
      VerifyEvtEventRel( iEventIDs_1, TestConstants.REL_TYPE_CD_DEPT, iEventIDs_2 );
      VerifyEvtEventRel( iEventIDs_2, TestConstants.REL_TYPE_CD_DRVTASK, iEventIDs_2 );

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
      VerifyEvtStage( iEventIDs_1, TestConstants.STATUS_COMPLETE );
      VerifyEvtStage( iEventIDs_2, TestConstants.STATUS_ACTV );

      // verify evt_inv_usage
      lQuery = "select 1 from " + TableUtil.EVT_INV_USAGE + " where EVENT_DB_ID="
            + iEventIDs_1.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_1.getNO_ID();
      Assert.assertTrue( "Check evt_inv_usage table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify evt_sched_dead
      simpleIDs ldatatypeID = getDataTypeIDs( "HOURS", "Flying Hours", "HOUR", "US" );
      int lInterval = getinterval( iTask_CD, ldatatypeID );
      int lCurrentUsage = getCurrentUsage( iSN, ldatatypeID );

      VerifyEvtSchedDead( iEventIDs_1, TestConstants.STATUS_COMPLETE,
            TestConstants.SCHED_FROM_CD_CUSTOM, -999, -999 );
      VerifyEvtSchedDead( iEventIDs_2, TestConstants.STATUS_ACTV,
            TestConstants.SCHED_FROM_CD_LASTEND, lCompletionValue,
            lCompletionValue - lCurrentUsage + lInterval );

   }


   /**
    * This test is to verify mx_al_ctrller_pkg.execute_task_import functionality of on OPER-23036
    * Task with CA schedule parameter get last end if previous task is completed inside window of
    * prefixed and postfixed
    *
    *
    */

   @Test
   public void test_AL_TASK_OPER_23036_LASTDUE_CA_IMPORT() {

      // String lCompletionDateS = "2015-01-01";
      String lCompletionDueDateS = "2015-01-02";
      // java.sql.Date lCompletionDate = converStringToSQLDate( lCompletionDateS, "yyyy-MM-dd" );
      java.sql.Date lCompletionDueDate = converStringToSQLDate( lCompletionDueDateS, "yyyy-MM-dd" );

      iEventIDs_1 = null;
      iEventIDs_2 = null;
      iTaskIDs_1 = null;

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      test_AL_TASK_OPER_23036_LASTDUE_CA_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runImportTasks();

      // Get event IDs
      iEventIDs_1 = getEventIDs( TestConstants.STATUS_COMPLETE, iTask_CD );
      iEventIDs_2 = getEventIDs( TestConstants.STATUS_ACTV, iTask_CD );

      // Validate evt_event
      Assert.assertTrue( "check event id 1 is not null", iEventIDs_1 != null );
      Assert.assertTrue( "check event id 2 is not null", iEventIDs_2 != null );

      VerifyEvtEvent( iEventIDs_1, iEVENT_TYPE_CD, iEventIDs_1, "1" );
      VerifyEvtEvent( iEventIDs_2, iEVENT_TYPE_CD, iEventIDs_2, "0" );

      // Get task ID
      iTaskIDs_1 = getTaskIDs( iTask_CD );
      // get inventory IDs
      simpleIDs iInvIDs_ACFT = getInvIDs( iSN );

      // verify sched_task table
      VerifySchedTask( iEventIDs_1, null, iTaskIDs_1, TestConstants.TASK_CLASS_CD_REQ,
            iInvIDs_ACFT );

      VerifySchedTask( iEventIDs_2, iEventIDs_2, iTaskIDs_1, TestConstants.TASK_CLASS_CD_REQ,
            iInvIDs_ACFT );

      // verify evt_event_rel
      VerifyEvtEventRel( iEventIDs_1, TestConstants.REL_TYPE_CD_DEPT, iEventIDs_2 );
      VerifyEvtEventRel( iEventIDs_2, TestConstants.REL_TYPE_CD_DRVTASK, iEventIDs_2 );

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
      VerifyEvtStage( iEventIDs_1, TestConstants.STATUS_COMPLETE );
      VerifyEvtStage( iEventIDs_2, TestConstants.STATUS_ACTV );

      // verify evt_inv_usage
      lQuery = "select 1 from " + TableUtil.EVT_INV_USAGE + " where EVENT_DB_ID="
            + iEventIDs_1.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_1.getNO_ID();
      Assert.assertFalse( "Check evt_inv_usage table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify evt_sched_dead
      int lInterval = getinterval( iTask_CD );
      // int lCurrentUsage = getCurrentUsage( iSN );
      Date lSched_dead_dt = DateAddDays( lCompletionDueDateS, lInterval );

      VerifyEvtSchedDead_CA( iEventIDs_1, TestConstants.STATUS_COMPLETE,
            TestConstants.SCHED_FROM_CD_CUSTOM, null, null );
      VerifyEvtSchedDead_CA( iEventIDs_2, TestConstants.STATUS_ACTV,
            TestConstants.SCHED_FROM_CD_LASTDUE, lCompletionDueDate, lSched_dead_dt );

   }


   /**
    * This test is to verify mx_al_ctrller_pkg.execute_task_import functionality of on OPER-23036
    * Task with CA schedule parameter get last end if previous task is completed outside window of
    * prefixed and postfixed
    *
    *
    */

   @Test
   public void test_AL_TASK_OPER_23036_LASTEND_CA_IMPORT() {

      String lCompletionDateS = "2015-01-01";
      // String lCompletionDueDateS = "2015-01-20";
      java.sql.Date lCompletionDate = converStringToSQLDate( lCompletionDateS, "yyyy-MM-dd" );
      // java.sql.Date lCompletionDueDate = converStringToSQLDate( lCompletionDueDateS, "yyyy-MM-dd"
      // );

      iEventIDs_1 = null;
      iEventIDs_2 = null;
      iTaskIDs_1 = null;

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      test_AL_TASK_OPER_23036_LASTEND_CA_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runImportTasks();

      // Get event IDs
      iEventIDs_1 = getEventIDs( TestConstants.STATUS_COMPLETE, iTask_CD );
      iEventIDs_2 = getEventIDs( TestConstants.STATUS_ACTV, iTask_CD );

      // Validate evt_event
      Assert.assertTrue( "check event id 1 is not null", iEventIDs_1 != null );
      Assert.assertTrue( "check event id 2 is not null", iEventIDs_2 != null );

      VerifyEvtEvent( iEventIDs_1, iEVENT_TYPE_CD, iEventIDs_1, "1" );
      VerifyEvtEvent( iEventIDs_2, iEVENT_TYPE_CD, iEventIDs_2, "0" );

      // Get task ID
      iTaskIDs_1 = getTaskIDs( iTask_CD );
      // get inventory IDs
      simpleIDs iInvIDs_ACFT = getInvIDs( iSN );

      // verify sched_task table
      VerifySchedTask( iEventIDs_1, null, iTaskIDs_1, TestConstants.TASK_CLASS_CD_REQ,
            iInvIDs_ACFT );

      VerifySchedTask( iEventIDs_2, iEventIDs_2, iTaskIDs_1, TestConstants.TASK_CLASS_CD_REQ,
            iInvIDs_ACFT );

      // verify evt_event_rel
      VerifyEvtEventRel( iEventIDs_1, TestConstants.REL_TYPE_CD_DEPT, iEventIDs_2 );
      VerifyEvtEventRel( iEventIDs_2, TestConstants.REL_TYPE_CD_DRVTASK, iEventIDs_2 );

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
      VerifyEvtStage( iEventIDs_1, TestConstants.STATUS_COMPLETE );
      VerifyEvtStage( iEventIDs_2, TestConstants.STATUS_ACTV );

      // verify evt_inv_usage
      lQuery = "select 1 from " + TableUtil.EVT_INV_USAGE + " where EVENT_DB_ID="
            + iEventIDs_1.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_1.getNO_ID();
      Assert.assertFalse( "Check evt_inv_usage table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify evt_sched_dead
      int lInterval = getinterval( iTask_CD );
      // int lCurrentUsage = getCurrentUsage( iSN );
      Date lSched_dead_dt = DateAddDays( lCompletionDateS, lInterval );

      VerifyEvtSchedDead_CA( iEventIDs_1, TestConstants.STATUS_COMPLETE,
            TestConstants.SCHED_FROM_CD_CUSTOM, null, null );
      VerifyEvtSchedDead_CA( iEventIDs_2, TestConstants.STATUS_ACTV,
            TestConstants.SCHED_FROM_CD_LASTEND, lCompletionDate, lSched_dead_dt );

   }


   /**
    * This test is to verify mx_al_ctrller_pkg.execute_task_import functionality of on OPER-23036
    * Task with CA schedule parameter, but with no completion due data will get last end.
    *
    *
    */
   @Test
   public void test_AL_TASK_OPER_23036_HIS_NO_COMPLETION_DUE_DATE_CA_IMPORT() {

      String lCompletionDateS = "2015-01-01";
      java.sql.Date lCompletionDate = converStringToSQLDate( lCompletionDateS, "yyyy-MM-dd" );

      iEventIDs_1 = null;
      iEventIDs_2 = null;
      iTaskIDs_1 = null;

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      test_AL_TASK_OPER_23036_HIS_NO_COMPLETION_DUE_DATE_CA_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runImportTasks();

      // Get event IDs
      iEventIDs_1 = getEventIDs( TestConstants.STATUS_COMPLETE, iTask_CD );
      iEventIDs_2 = getEventIDs( TestConstants.STATUS_ACTV, iTask_CD );

      // Validate evt_event
      Assert.assertTrue( "check event id 1 is not null", iEventIDs_1 != null );
      Assert.assertTrue( "check event id 2 is not null", iEventIDs_2 != null );

      VerifyEvtEvent( iEventIDs_1, iEVENT_TYPE_CD, iEventIDs_1, "1" );
      VerifyEvtEvent( iEventIDs_2, iEVENT_TYPE_CD, iEventIDs_2, "0" );

      // Get task ID
      iTaskIDs_1 = getTaskIDs( iTask_CD );
      // get inventory IDs
      simpleIDs iInvIDs_ACFT = getInvIDs( iSN );

      // verify sched_task table
      VerifySchedTask( iEventIDs_1, null, iTaskIDs_1, TestConstants.TASK_CLASS_CD_REQ,
            iInvIDs_ACFT );

      VerifySchedTask( iEventIDs_2, iEventIDs_2, iTaskIDs_1, TestConstants.TASK_CLASS_CD_REQ,
            iInvIDs_ACFT );

      // verify evt_event_rel
      VerifyEvtEventRel( iEventIDs_1, TestConstants.REL_TYPE_CD_DEPT, iEventIDs_2 );
      VerifyEvtEventRel( iEventIDs_2, TestConstants.REL_TYPE_CD_DRVTASK, iEventIDs_2 );

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
      VerifyEvtStage( iEventIDs_1, TestConstants.STATUS_COMPLETE );
      VerifyEvtStage( iEventIDs_2, TestConstants.STATUS_ACTV );

      // verify evt_inv_usage
      lQuery = "select 1 from " + TableUtil.EVT_INV_USAGE + " where EVENT_DB_ID="
            + iEventIDs_1.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_1.getNO_ID();
      Assert.assertFalse( "Check evt_inv_usage table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify evt_sched_dead
      lQuery = "select 1 from " + TableUtil.EVT_SCHED_DEAD + " where EVENT_DB_ID="
            + iEventIDs_1.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_1.getNO_ID();
      Assert.assertFalse( "Check evt_sched_dead table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify evt_sched_dead
      int lInterval = getinterval( iTask_CD );
      // int lCurrentUsage = getCurrentUsage( iSN );
      Date lSched_dead_dt = DateAddDays( lCompletionDateS, lInterval );

      VerifyEvtSchedDead_CA( iEventIDs_2, TestConstants.STATUS_ACTV,
            TestConstants.SCHED_FROM_CD_LASTEND, lCompletionDate, lSched_dead_dt );

   }


   /**
    * This test is to verify mx_al_ctrller_pkg.execute_task_import functionality of on OPER-23036
    * Task (BLKCHAIN) with usage hours get last due if previous task is completed inside window of
    * prefixed and postfixed
    *
    *
    */

   @Test
   public void test_AL_TASK_OPER_23036_LASTDUE_BLOCKCHAINUS_IMPORT() {

      int LCompletionDueValue = 90;
      // int lCompletionValue = 95;

      iEventIDs_1 = null;
      iEventIDs_2 = null;
      iTaskIDs_1 = null;

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      test_AL_TASK_OPER_23036_LASTDUE_BLOCKCHAINUS_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runImportTasks();

      // Get event IDs
      iEventIDs_1 = getEventIDs( TestConstants.STATUS_COMPLETE, iTask_CD_BLOCKCHAIN_1 );
      iEventIDs_2 = getEventIDs( TestConstants.STATUS_ACTV, iTask_CD_BLOCKCHAIN_2 );

      // Validate evt_event
      Assert.assertTrue( "check event id 1 is not null", iEventIDs_1 != null );
      Assert.assertTrue( "check event id 2 is not null", iEventIDs_2 != null );

      VerifyEvtEvent( iEventIDs_1, iEVENT_TYPE_CD, iEventIDs_1, "1" );
      VerifyEvtEvent( iEventIDs_2, iEVENT_TYPE_CD, iEventIDs_2, "0" );

      // Get task ID
      iTaskIDs_1 = getTaskIDs( iTask_CD_BLOCKCHAIN_1 );
      iTaskIDs_2 = getTaskIDs( iTask_CD_BLOCKCHAIN_2 );
      // get inventory IDs
      simpleIDs iInvIDs_ACFT = getInvIDs( iSN );

      // verify sched_task table
      VerifySchedTask( iEventIDs_1, null, iTaskIDs_1, TestConstants.TASK_CLASS_CD_BLOCK,
            iInvIDs_ACFT );

      VerifySchedTask( iEventIDs_2, iEventIDs_2, iTaskIDs_2, TestConstants.TASK_CLASS_CD_BLOCK,
            iInvIDs_ACFT );

      // verify evt_event_rel
      VerifyEvtEventRel( iEventIDs_1, TestConstants.REL_TYPE_CD_DEPT, iEventIDs_2 );
      VerifyEvtEventRel( iEventIDs_2, TestConstants.REL_TYPE_CD_DRVTASK, iEventIDs_2 );

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
      VerifyEvtStage( iEventIDs_1, TestConstants.STATUS_COMPLETE );
      VerifyEvtStage( iEventIDs_2, TestConstants.STATUS_ACTV );

      // verify evt_inv_usage
      lQuery = "select 1 from " + TableUtil.EVT_INV_USAGE + " where EVENT_DB_ID="
            + iEventIDs_1.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_1.getNO_ID();
      Assert.assertTrue( "Check evt_inv_usage table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify evt_sched_dead
      simpleIDs ldatatypeID = getDataTypeIDs( "HOURS", "Flying Hours", "HOUR", "US" );
      int lInterval = getinterval( iTask_CD_BLOCKCHAIN_1, ldatatypeID );
      int lCurrentUsage = getCurrentUsage( iSN, ldatatypeID );

      VerifyEvtSchedDead( iEventIDs_1, TestConstants.STATUS_COMPLETE,
            TestConstants.SCHED_FROM_CD_CUSTOM, -999, -999 );
      VerifyEvtSchedDead( iEventIDs_2, TestConstants.STATUS_ACTV,
            TestConstants.SCHED_FROM_CD_LASTDUE, LCompletionDueValue,
            LCompletionDueValue - lCurrentUsage + lInterval );
   }


   /**
    * This test is to verify mx_al_ctrller_pkg.execute_task_import functionality of on OPER-23036
    * Task (BLKCHAIN) with CA get last due if previous task is completed inside window of prefixed
    * and postfixed
    *
    *
    */

   @Test
   public void test_AL_TASK_OPER_23036_LASTDUE_BLOCKCHAINCA_IMPORT() {

      // String lCompletionDateS = "2015-01-01";
      String lCompletionDueDateS = "2015-01-02";
      // java.sql.Date lCompletionDate = converStringToSQLDate( lCompletionDateS, "yyyy-MM-dd" );
      java.sql.Date lCompletionDueDate = converStringToSQLDate( lCompletionDueDateS, "yyyy-MM-dd" );

      iEventIDs_1 = null;
      iEventIDs_2 = null;
      iTaskIDs_1 = null;

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      test_AL_TASK_OPER_23036_LASTDUE_BLOCKCHAINCA_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runImportTasks();

      // Get event IDs
      iEventIDs_1 = getEventIDs( TestConstants.STATUS_COMPLETE, iTask_CD_BLOCKCHAIN_1 );
      iEventIDs_2 = getEventIDs( TestConstants.STATUS_ACTV, iTask_CD_BLOCKCHAIN_2 );

      // Validate evt_event
      Assert.assertTrue( "check event id 1 is not null", iEventIDs_1 != null );
      Assert.assertTrue( "check event id 2 is not null", iEventIDs_2 != null );

      VerifyEvtEvent( iEventIDs_1, iEVENT_TYPE_CD, iEventIDs_1, "1" );
      VerifyEvtEvent( iEventIDs_2, iEVENT_TYPE_CD, iEventIDs_2, "0" );

      // Get task ID
      iTaskIDs_1 = getTaskIDs( iTask_CD_BLOCKCHAIN_1 );
      iTaskIDs_2 = getTaskIDs( iTask_CD_BLOCKCHAIN_2 );
      // get inventory IDs
      simpleIDs iInvIDs_ACFT = getInvIDs( iSN );

      // verify sched_task table
      VerifySchedTask( iEventIDs_1, null, iTaskIDs_1, TestConstants.TASK_CLASS_CD_BLOCK,
            iInvIDs_ACFT );

      VerifySchedTask( iEventIDs_2, iEventIDs_2, iTaskIDs_2, TestConstants.TASK_CLASS_CD_BLOCK,
            iInvIDs_ACFT );

      // verify evt_event_rel
      VerifyEvtEventRel( iEventIDs_1, TestConstants.REL_TYPE_CD_DEPT, iEventIDs_2 );
      VerifyEvtEventRel( iEventIDs_2, TestConstants.REL_TYPE_CD_DRVTASK, iEventIDs_2 );

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
      VerifyEvtStage( iEventIDs_1, TestConstants.STATUS_COMPLETE );
      VerifyEvtStage( iEventIDs_2, TestConstants.STATUS_ACTV );

      // verify evt_inv_usage
      lQuery = "select 1 from " + TableUtil.EVT_INV_USAGE + " where EVENT_DB_ID="
            + iEventIDs_1.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_1.getNO_ID();
      Assert.assertFalse( "Check evt_inv_usage table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify evt_sched_dead
      int lInterval = getinterval( iTask_CD_BLOCKCHAIN_1 );
      // int lCurrentUsage = getCurrentUsage( iSN );
      Date lSched_dead_dt = DateAddDays( lCompletionDueDateS, lInterval );

      VerifyEvtSchedDead_CA( iEventIDs_1, TestConstants.STATUS_COMPLETE,
            TestConstants.SCHED_FROM_CD_CUSTOM, null, null );
      VerifyEvtSchedDead_CA( iEventIDs_2, TestConstants.STATUS_ACTV,
            TestConstants.SCHED_FROM_CD_LASTDUE, lCompletionDueDate, lSched_dead_dt );

   }


   /**
    * This test is to verify mx_al_ctrller_pkg.execute_task_import functionality of on OPER-23036
    * Task (BLOCKCHAIN) with usage hours get last due if previous task is completed outside window
    * of prefixed and postfixed
    *
    *
    */

   @Test
   public void test_AL_TASK_OPER_23036_LASTEND_BLOCKCHAINUS_IMPORT() {

      int LCompletionDueValue = 95;
      // int lCompletionValue = 80;

      iEventIDs_1 = null;
      iEventIDs_2 = null;
      iTaskIDs_1 = null;

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      test_AL_TASK_OPER_23036_LASTEND_BLOCKCHAINUS_VALIDATION();
      System.out.println( "Finish validation" );

      // run validation
      runImportTasks();

      // Get event IDs
      iEventIDs_1 = getEventIDs( TestConstants.STATUS_COMPLETE, iTask_CD_BLOCKCHAIN_1 );
      iEventIDs_2 = getEventIDs( TestConstants.STATUS_ACTV, iTask_CD_BLOCKCHAIN_2 );

      // Validate evt_event
      Assert.assertTrue( "check event id 1 is not null", iEventIDs_1 != null );
      Assert.assertTrue( "check event id 2 is not null", iEventIDs_2 != null );

      VerifyEvtEvent( iEventIDs_1, iEVENT_TYPE_CD, iEventIDs_1, "1" );
      VerifyEvtEvent( iEventIDs_2, iEVENT_TYPE_CD, iEventIDs_2, "0" );

      // Get task ID
      iTaskIDs_1 = getTaskIDs( iTask_CD_BLOCKCHAIN_1 );
      iTaskIDs_2 = getTaskIDs( iTask_CD_BLOCKCHAIN_2 );
      // get inventory IDs
      simpleIDs iInvIDs_ACFT = getInvIDs( iSN );

      // verify sched_task table
      VerifySchedTask( iEventIDs_1, null, iTaskIDs_1, TestConstants.TASK_CLASS_CD_BLOCK,
            iInvIDs_ACFT );

      VerifySchedTask( iEventIDs_2, iEventIDs_2, iTaskIDs_2, TestConstants.TASK_CLASS_CD_BLOCK,
            iInvIDs_ACFT );

      // verify evt_event_rel
      VerifyEvtEventRel( iEventIDs_1, TestConstants.REL_TYPE_CD_DEPT, iEventIDs_2 );
      VerifyEvtEventRel( iEventIDs_2, TestConstants.REL_TYPE_CD_DRVTASK, iEventIDs_2 );

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
      VerifyEvtStage( iEventIDs_1, TestConstants.STATUS_COMPLETE );
      VerifyEvtStage( iEventIDs_2, TestConstants.STATUS_ACTV );

      // verify evt_inv_usage
      lQuery = "select 1 from " + TableUtil.EVT_INV_USAGE + " where EVENT_DB_ID="
            + iEventIDs_1.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_1.getNO_ID();
      Assert.assertTrue( "Check evt_inv_usage table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify evt_sched_dead
      simpleIDs ldatatypeID = getDataTypeIDs( "HOURS", "Flying Hours", "HOUR", "US" );
      int lInterval = getinterval( iTask_CD_BLOCKCHAIN_1, ldatatypeID );
      int lCurrentUsage = getCurrentUsage( iSN, ldatatypeID );

      VerifyEvtSchedDead( iEventIDs_1, TestConstants.STATUS_COMPLETE,
            TestConstants.SCHED_FROM_CD_CUSTOM, -999, -999 );
      VerifyEvtSchedDead( iEventIDs_2, TestConstants.STATUS_ACTV,
            TestConstants.SCHED_FROM_CD_LASTEND, LCompletionDueValue,
            LCompletionDueValue - lCurrentUsage + lInterval );

   }


   /**
    * This test is to verify mx_al_ctrller_pkg.execute_task_import functionality of on OPER-23036
    * Task (BLKCHAIN) with CA get last due if previous task is completed outside window of prefixed
    * and postfixed
    *
    *
    */

   @Test
   public void test_AL_TASK_OPER_23036_LASTEND_BLOCKCHAINCA_IMPORT() {

      String lCompletionDateS = "2015-01-01";
      // String lCompletionDueDateS = "2015-02-01";
      java.sql.Date lCompletionDate = converStringToSQLDate( lCompletionDateS, "yyyy-MM-dd" );
      // java.sql.Date lCompletionDueDate = converStringToSQLDate( lCompletionDueDateS, "yyyy-MM-dd"
      // );

      iEventIDs_1 = null;
      iEventIDs_2 = null;
      iTaskIDs_1 = null;

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      test_AL_TASK_OPER_23036_LASTEND_BLOCKCHAINCA_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runImportTasks();

      // Get event IDs
      iEventIDs_1 = getEventIDs( TestConstants.STATUS_COMPLETE, iTask_CD_BLOCKCHAIN_1 );
      iEventIDs_2 = getEventIDs( TestConstants.STATUS_ACTV, iTask_CD_BLOCKCHAIN_2 );

      // Validate evt_event
      Assert.assertTrue( "check event id 1 is not null", iEventIDs_1 != null );
      Assert.assertTrue( "check event id 2 is not null", iEventIDs_2 != null );

      VerifyEvtEvent( iEventIDs_1, iEVENT_TYPE_CD, iEventIDs_1, "1" );
      VerifyEvtEvent( iEventIDs_2, iEVENT_TYPE_CD, iEventIDs_2, "0" );

      // Get task ID
      iTaskIDs_1 = getTaskIDs( iTask_CD_BLOCKCHAIN_1 );
      iTaskIDs_2 = getTaskIDs( iTask_CD_BLOCKCHAIN_2 );
      // get inventory IDs
      simpleIDs iInvIDs_ACFT = getInvIDs( iSN );

      // verify sched_task table
      VerifySchedTask( iEventIDs_1, null, iTaskIDs_1, TestConstants.TASK_CLASS_CD_BLOCK,
            iInvIDs_ACFT );

      VerifySchedTask( iEventIDs_2, iEventIDs_2, iTaskIDs_2, TestConstants.TASK_CLASS_CD_BLOCK,
            iInvIDs_ACFT );

      // verify evt_event_rel
      VerifyEvtEventRel( iEventIDs_1, TestConstants.REL_TYPE_CD_DEPT, iEventIDs_2 );
      VerifyEvtEventRel( iEventIDs_2, TestConstants.REL_TYPE_CD_DRVTASK, iEventIDs_2 );

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
      VerifyEvtStage( iEventIDs_1, TestConstants.STATUS_COMPLETE );
      VerifyEvtStage( iEventIDs_2, TestConstants.STATUS_ACTV );

      // verify evt_inv_usage
      lQuery = "select 1 from " + TableUtil.EVT_INV_USAGE + " where EVENT_DB_ID="
            + iEventIDs_1.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_1.getNO_ID();
      Assert.assertFalse( "Check evt_inv_usage table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify evt_sched_dead
      int lInterval = getinterval( iTask_CD_BLOCKCHAIN_1 );
      // int lCurrentUsage = getCurrentUsage( iSN );
      Date lSched_dead_dt = DateAddDays( lCompletionDateS, lInterval );

      VerifyEvtSchedDead_CA( iEventIDs_1, TestConstants.STATUS_COMPLETE,
            TestConstants.SCHED_FROM_CD_CUSTOM, null, null );
      VerifyEvtSchedDead_CA( iEventIDs_2, TestConstants.STATUS_ACTV,
            TestConstants.SCHED_FROM_CD_LASTEND, lCompletionDate, lSched_dead_dt );

   }


   // ========================================================================================
   /**
    * This function is to retrieve inventory ID by giving Serial number.
    *
    *
    *
    */
   @Override
   public simpleIDs getInvIDs( String aSN ) {

      String[] iIds = { "INV_NO_DB_ID", "INV_NO_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "SERIAL_NO_OEM", aSN );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.INV_INV, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lIds;
   }


   /**
    * This function is to retrieve task ID by giving task_cd.
    *
    *
    *
    */

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
    * This function is to retrieve interval value by giving task_cd
    *
    *
    *
    */

   public int getinterval( String aTask_CD, simpleIDs aDataTypeIds ) {

      simpleIDs ltask_ids = getTaskIDs( aTask_CD );

      String[] iIds = { "DEF_INTERVAL_QT" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_DB_ID", ltask_ids.getNO_DB_ID() );
      lArgs.addArguments( "TASK_ID", ltask_ids.getNO_ID() );
      lArgs.addArguments( "DATA_TYPE_DB_ID", aDataTypeIds.getNO_DB_ID() );
      lArgs.addArguments( "DATA_TYPE_ID", aDataTypeIds.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_SCHED_RULE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      return Integer.parseInt( llists.get( 0 ).get( 0 ) );

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


   /**
    * This function is to retrieve current inventory value by giving serial no
    *
    *
    *
    */

   @Override
   public int getCurrentUsage( String aSN, simpleIDs aDataTypeIds ) {

      simpleIDs linv_ids = getInvIDs( aSN );

      String[] iIds = { "TSN_QT" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "INV_NO_DB_ID", linv_ids.getNO_DB_ID() );
      lArgs.addArguments( "INV_NO_ID", linv_ids.getNO_ID() );
      lArgs.addArguments( "DATA_TYPE_DB_ID", aDataTypeIds.getNO_DB_ID() );
      lArgs.addArguments( "DATA_TYPE_ID", aDataTypeIds.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.INV_CURR_USAGE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      return Integer.parseInt( llists.get( 0 ).get( 0 ) );

   }


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
      Assert.assertTrue( "MAIN_INV_NO_DB_ID",
            llists.get( 0 ).get( 5 ).equalsIgnoreCase( aINVIDs.getNO_DB_ID() ) );
      Assert.assertTrue( "MAIN_INV_NO_ID",
            llists.get( 0 ).get( 6 ).equalsIgnoreCase( aINVIDs.getNO_ID() ) );

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
   public void VerifyEvtStage( simpleIDs aIDs, String aEVENT_STATUS_CD ) {

      String[] iIds = { "EVENT_STATUS_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "EVENT_DB_ID", aIDs.getNO_DB_ID() );
      lArgs.addArguments( "EVENT_ID", aIDs.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EVT_STAGE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "EVENT_STATUS_CD",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aEVENT_STATUS_CD ) );

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
    * This function is to prepare data for test_AL_TASK_OPER_23036_LASTDUE_US_IMPORT
    *
    *
    */
   public void test_AL_TASK_OPER_23036_LASTDUE_US_VALIDATION() {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'" + iTask_CD + "'" );
      // lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
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
      lMapTaskSched.put( "TASK_CD", "'" + iTask_CD + "'" );
      lMapTaskSched.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      lMapTaskSched.put( "COMPLETION_VALUE", "95" );
      lMapTaskSched.put( "COMPLETION_DUE_VALUE", "90" );
      lMapTaskSched.put( "CUSTOM_START_VALUE", "null" );
      lMapTaskSched.put( "CUSTOM_DUE_VALUE", "null" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // validate only invalid record got error code
      Assert.assertTrue( "There should not have error", checkTaskValidation_cap( "SN000001" ) );
   }


   /**
    * This function is to prepare data for test_AL_TASK_OPER_23036_LASTEND_US_IMPORT
    *
    *
    */
   public void test_AL_TASK_OPER_23036_LASTEND_US_VALIDATION() {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'" + iTask_CD + "'" );
      // lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
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
      lMapTaskSched.put( "TASK_CD", "'" + iTask_CD + "'" );
      lMapTaskSched.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      lMapTaskSched.put( "COMPLETION_VALUE", "95" );
      lMapTaskSched.put( "COMPLETION_DUE_VALUE", "80" );
      lMapTaskSched.put( "CUSTOM_START_VALUE", "null" );
      lMapTaskSched.put( "CUSTOM_DUE_VALUE", "null" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // validate only invalid record got error code
      Assert.assertTrue( "There should not have error", checkTaskValidation_cap( "SN000001" ) );
   }


   /**
    * This function is to prepare data for test_AL_TASK_OPER_23036_LASTDUE_CA_IMPORT
    *
    *
    */
   public void test_AL_TASK_OPER_23036_LASTDUE_CA_VALIDATION() {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'" + iTask_CD + "'" );
      // lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTask.put( "COMPLETION_DUE_DT", "to_date('2015/01/02','yyyy/mm/dd')" );
      lMapTask.put( "CUSTOM_START_DT", "null" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // run validation
      runValidateTasks();

      // validate only invalid record got error code
      Assert.assertTrue( "There should not have error", checkTaskValidation_cap( "SN000001" ) );
   }


   /**
    * This function is to prepare data for test_AL_TASK_OPER_23036_LASTEND_CA_IMPORT
    *
    *
    */
   public void test_AL_TASK_OPER_23036_LASTEND_CA_VALIDATION() {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'" + iTask_CD + "'" );
      // lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTask.put( "COMPLETION_DUE_DT", "to_date('2015/01/20','yyyy/mm/dd')" );
      lMapTask.put( "CUSTOM_START_DT", "null" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // run validation
      runValidateTasks();

      // validate only invalid record got error code
      Assert.assertTrue( "There should not have error", checkTaskValidation_cap( "SN000001" ) );
   }


   /**
    * This function is to prepare data for
    * test_AL_TASK_OPER_23036_HIS_NO_COMPLETION_DUE_DATE_CA_IMPORT
    *
    *
    */
   public void test_AL_TASK_OPER_23036_HIS_NO_COMPLETION_DUE_DATE_CA_VALIDATION() {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // valid record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'" + iTask_CD + "'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // run validation
      runValidateTasks();

      // validate only invalid record got error code
      Assert.assertTrue( "There should not have error", checkTaskValidation_cap( "SN000001" ) );
   }


   /**
    * This function is to prepare data for test_AL_TASK_OPER_23036_LASTDUE_US_IMPORT
    *
    *
    */
   public void test_AL_TASK_OPER_23036_LASTDUE_BLOCKCHAINUS_VALIDATION() {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'" + iTask_CD_BLOCKCHAIN_1 + "'" );
      // lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
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
      lMapTaskSched.put( "TASK_CD", "'" + iTask_CD_BLOCKCHAIN_1 + "'" );
      lMapTaskSched.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      lMapTaskSched.put( "COMPLETION_VALUE", "95" );
      lMapTaskSched.put( "COMPLETION_DUE_VALUE", "90" );
      lMapTaskSched.put( "CUSTOM_START_VALUE", "null" );
      lMapTaskSched.put( "CUSTOM_DUE_VALUE", "null" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // validate only invalid record got error code
      Assert.assertTrue( "There should not have error", checkTaskValidation_cap( "SN000001" ) );
   }


   /**
    * This function is to prepare data for test_AL_TASK_OPER_23036_LASTDUE_US_IMPORT
    *
    *
    */
   public void test_AL_TASK_OPER_23036_LASTDUE_BLOCKCHAINCA_VALIDATION() {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'" + iTask_CD_BLOCKCHAIN_1 + "'" );
      // lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTask.put( "COMPLETION_DUE_DT", "to_date('2015/01/02','yyyy/mm/dd')" );
      lMapTask.put( "CUSTOM_START_DT", "null" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // run validation
      runValidateTasks();

      // validate only invalid record got error code
      Assert.assertTrue( "There should not have error", checkTaskValidation_cap( "SN000001" ) );
   }


   /**
    * This function is to prepare data for test_AL_TASK_OPER_23036_LASTEND_US_IMPORT
    *
    *
    */
   public void test_AL_TASK_OPER_23036_LASTEND_BLOCKCHAINUS_VALIDATION() {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'" + iTask_CD_BLOCKCHAIN_1 + "'" );
      // lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      // lMapTask.put( "COMPLETION_DUE_DT", "to_date('2015/02/01','yyyy/mm/dd')" );
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
      lMapTaskSched.put( "TASK_CD", "'" + iTask_CD_BLOCKCHAIN_1 + "'" );
      lMapTaskSched.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      lMapTaskSched.put( "COMPLETION_VALUE", "95" );
      lMapTaskSched.put( "COMPLETION_DUE_VALUE", "80" );
      lMapTaskSched.put( "CUSTOM_START_VALUE", "null" );
      lMapTaskSched.put( "CUSTOM_DUE_VALUE", "null" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // validate only invalid record got error code
      Assert.assertTrue( "There should not have error", checkTaskValidation_cap( "SN000001" ) );
   }


   /**
    * This function is to prepare data for test_AL_TASK_OPER_23036_LASTEND_US_IMPORT
    *
    *
    */
   public void test_AL_TASK_OPER_23036_LASTEND_BLOCKCHAINCA_VALIDATION() {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'" + iTask_CD_BLOCKCHAIN_1 + "'" );
      // lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTask.put( "COMPLETION_DUE_DT", "to_date('2015/02/01','yyyy/mm/dd')" );
      lMapTask.put( "CUSTOM_START_DT", "null" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // run validation
      runValidateTasks();

      // validate only invalid record got error code
      Assert.assertTrue( "There should not have error", checkTaskValidation_cap( "SN000001" ) );
   }


   /**
    * This function is data setup for US
    *
    *
    *
    */
   public void dataSetup_us() {
      iTaskId = getTaskIDs( iTask_CD );

      String lquery =
            "update TASK_SCHED_RULE set DEF_PREFIXED_QT=10, DEF_POSTFIXED_QT=10 where TASK_DB_ID="
                  + iTaskId.getNO_DB_ID() + " and TASK_ID=" + iTaskId.getNO_ID();
      runUpdate( lquery );

   }


   /**
    * This function is data setup for CA
    *
    *
    *
    */
   public void dataSetup_ca() {
      iTaskId = getTaskIDs( iTask_CD );
      simpleIDs lDataType_ca_Day =
            getDataTypeIDs( iDomain_Type_CD_CA, iENG_UNIT_CD, iDATA_TYPE_CD_CDY );

      String lquery =
            "update TASK_SCHED_RULE set DEF_PREFIXED_QT=10, DEF_POSTFIXED_QT=10, DATA_TYPE_DB_ID='"
                  + lDataType_ca_Day.getNO_DB_ID() + "', DATA_TYPE_ID='"
                  + lDataType_ca_Day.getNO_ID() + "' where TASK_DB_ID=" + iTaskId.getNO_DB_ID()
                  + " and TASK_ID=" + iTaskId.getNO_ID();
      runUpdate( lquery );

   }


   /**
    * This function is data setup for US
    *
    *
    *
    */
   public void dataSetup_BLKCHN_us() {
      iTaskId = getTaskIDs( iTask_CD_BLOCKCHAIN_1 );
      simpleIDs lDataType_us_Day =
            getDataTypeIDs( iDomain_Type_CD_US, iENG_UNIT_CD_US, iDATA_TYPE_CD_HOURS );

      String lquery =
            "update TASK_SCHED_RULE set DEF_PREFIXED_QT=10, DEF_POSTFIXED_QT=10, DATA_TYPE_DB_ID='"
                  + lDataType_us_Day.getNO_DB_ID() + "', DATA_TYPE_ID='"
                  + lDataType_us_Day.getNO_ID() + "' where TASK_DB_ID=" + iTaskId.getNO_DB_ID()
                  + " and TASK_ID=" + iTaskId.getNO_ID();
      runUpdate( lquery );

      iTaskId_2 = getTaskIDs( iTask_CD_BLOCKCHAIN_2 );
      lquery =
            "update TASK_SCHED_RULE set DEF_PREFIXED_QT=10, DEF_POSTFIXED_QT=10, DATA_TYPE_DB_ID='"
                  + lDataType_us_Day.getNO_DB_ID() + "', DATA_TYPE_ID='"
                  + lDataType_us_Day.getNO_ID() + "' where TASK_DB_ID=" + iTaskId_2.getNO_DB_ID()
                  + " and TASK_ID=" + iTaskId_2.getNO_ID();
      runUpdate( lquery );

   }


   /**
    * This function is data setup for US
    *
    *
    *
    */
   public void dataSetup_BLKCHN_ca() {

      iTaskId = getTaskIDs( iTask_CD_BLOCKCHAIN_1 );
      simpleIDs lDataType_ca_Day =
            getDataTypeIDs( iDomain_Type_CD_CA, iENG_UNIT_CD, iDATA_TYPE_CD_CDY );

      String lquery =
            "update TASK_SCHED_RULE set DEF_PREFIXED_QT=10, DEF_POSTFIXED_QT=10, DATA_TYPE_DB_ID='"
                  + lDataType_ca_Day.getNO_DB_ID() + "', DATA_TYPE_ID='"
                  + lDataType_ca_Day.getNO_ID() + "' where TASK_DB_ID=" + iTaskId.getNO_DB_ID()
                  + " and TASK_ID=" + iTaskId.getNO_ID();
      runUpdate( lquery );

      simpleIDs iTaskId_2 = getTaskIDs( iTask_CD_BLOCKCHAIN_2 );
      lquery =
            "update TASK_SCHED_RULE set DEF_PREFIXED_QT=10, DEF_POSTFIXED_QT=10, DATA_TYPE_DB_ID='"
                  + lDataType_ca_Day.getNO_DB_ID() + "', DATA_TYPE_ID='"
                  + lDataType_ca_Day.getNO_ID() + "' where TASK_DB_ID=" + iTaskId_2.getNO_DB_ID()
                  + " and TASK_ID=" + iTaskId_2.getNO_ID();
      runUpdate( lquery );

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

      // re-set req back to US
      iTaskId = getTaskIDs( iTask_CD );

      simpleIDs lDataType_us_Day =
            getDataTypeIDs( iDomain_Type_CD_US, iENG_UNIT_CD_US, iDATA_TYPE_CD_HOURS );
      String lquery =
            "update TASK_SCHED_RULE set DEF_PREFIXED_QT=null, DEF_POSTFIXED_QT=null, DATA_TYPE_DB_ID='"
                  + lDataType_us_Day.getNO_DB_ID() + "', DATA_TYPE_ID='"
                  + lDataType_us_Day.getNO_ID() + "' where TASK_DB_ID=" + iTaskId.getNO_DB_ID()
                  + " and TASK_ID=" + iTaskId.getNO_ID();
      runUpdate( lquery );

      // re-set blkchn to US
      iTaskId = getTaskIDs( iTask_CD_BLOCKCHAIN_1 );
      lquery =
            "update TASK_SCHED_RULE set DEF_PREFIXED_QT=null, DEF_POSTFIXED_QT=null, DATA_TYPE_DB_ID='"
                  + lDataType_us_Day.getNO_DB_ID() + "', DATA_TYPE_ID='"
                  + lDataType_us_Day.getNO_ID() + "' where TASK_DB_ID=" + iTaskId.getNO_DB_ID()
                  + " and TASK_ID=" + iTaskId.getNO_ID();
      runUpdate( lquery );

      iTaskId_2 = getTaskIDs( iTask_CD_BLOCKCHAIN_2 );
      lquery =
            "update TASK_SCHED_RULE set DEF_PREFIXED_QT=null, DEF_POSTFIXED_QT=null, DATA_TYPE_DB_ID='"
                  + lDataType_us_Day.getNO_DB_ID() + "', DATA_TYPE_ID='"
                  + lDataType_us_Day.getNO_ID() + "' where TASK_DB_ID=" + iTaskId_2.getNO_DB_ID()
                  + " and TASK_ID=" + iTaskId_2.getNO_ID();
      runUpdate( lquery );
   }

}
