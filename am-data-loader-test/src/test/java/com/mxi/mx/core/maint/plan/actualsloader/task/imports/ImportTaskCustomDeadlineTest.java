package com.mxi.mx.core.maint.plan.actualsloader.task.imports;

import static com.mxi.mx.util.TableUtil.AXON_DOMAIN_EVENT_ENTRY_TABLE;
import static org.junit.Assert.assertTrue;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
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
 * mx_al_ctrller_pkg.execute_task_import on Task custom deadline scheduled .
 *
 * @author ALICIA QIAN
 */
public class ImportTaskCustomDeadlineTest extends ActualsLoaderTest {

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
   public static String iTask_CD = "AL_APPLIC_RULE";
   public static String iTask_CD_8848 = "AT_TEST";
   public static String iTask_CD_8848_2 = "AL_FIRSTTIME_INITIAL_CAL"; // def_initial_qt=200 in db
   public static String iSN = "SN000001";
   public static String iDomain_Type_CD_CA = "CA";
   public static String iENG_UNIT_CD = "DAY";
   public static String iDomain_Type_CD_US = "US";
   public static String iENG_UNIT_CD_US = "HOUR";
   public static String iDATA_TYPE_CD_HOURS = "HOURS";
   public static String iDATA_TYPE_CD_CDY = "CDY";

   public static String iTask_CD_BLOCKCHAIN_1 = "RECURRING BLOCK CHAIN 1";
   public static String iTask_CD_BLOCKCHAIN_2 = "RECURRING BLOCK CHAIN 2";

   public String iInitialQT_Task_CD_8848_2 = null;


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
      iInitialQT_Task_CD_8848_2 = String.valueOf( getintial( iTask_CD_8848_2 ) );

      String strTCName = testName.getMethodName();
      if ( strTCName.contains( "_US_" ) ) {
         dataSetup_us();
      } else if ( strTCName.contains( "_CA_" ) ) {
         dataSetup_ca();

      } else if ( strTCName.contains( "_BLOCKCHAINUS_" ) ) {
         dataSetup_BLKCHN_us();

      } else if ( strTCName.contains( "OPER_8848_NonRecurring_InitialZero" ) ) {
         dataSetup_initialqt( iTask_CD_8848, "0" );

      } else if ( strTCName.contains( "OPER_8848_Recurring_InitialNULL" ) ) {
         dataSetup_initialqt( iTask_CD_8848_2, "null" );

      } else if ( strTCName.contains( "OPER_8848_Recurring_InitialZero" ) ) {
         dataSetup_initialqt( iTask_CD_8848_2, "0" );
      }

      // Clear AXON_DOMAIN_EVENT_ENTRY table
      deleteAllFromTable( AXON_DOMAIN_EVENT_ENTRY_TABLE );

   }


   /**
    * This test is to verify mx_al_ctrller_pkg.execute_task_import functionality of on OPER-26345
    * Task with usage hours only custom start qt is provided. TThe sched_dead_qt=custom start qt+
    * interval
    *
    *
    */

   @Test
   public void test_AL_TASK_OPER_26345_CustomStartOnly_US_IMPORT() {

      int LStartQT = 50;

      iEventIDs_1 = null;
      iEventIDs_2 = null;
      iEventIDs_3 = null;
      iTaskIDs_1 = null;

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      test_AL_TASK_OPER_26345_CustomStartOnly_US_VALIDATION();

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

      // // verify evt_inv_usage
      // lQuery = "select 1 from " + TableUtil.EVT_INV_USAGE + " where EVENT_DB_ID="
      // + iEventIDs_1.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_1.getNO_ID();
      // Assert.assertTrue( "Check evt_inv_usage table to verify the record exists",
      // RecordsExist( lQuery ) );

      // verify evt_sched_dead

      int lInterval = getinterval( iTask_CD );

      VerifyEvtSchedDead( iEventIDs_2, TestConstants.STATUS_ACTV,
            TestConstants.SCHED_FROM_CD_CUSTOM, LStartQT, LStartQT + lInterval, lInterval );

      // Verify AXON_DOMAIN_EVENT_ENTRY
      verifyTaskDrivingDeadlineRescheduledEventPublish();

   }


   /**
    * This test is to verify mx_al_ctrller_pkg.execute_task_import functionality of on OPER-26345
    * Task with usage hours only custom due qt is provided. The sched_dead_qt=custom start qt+
    * interval
    *
    *
    */

   @Test
   public void test_AL_TASK_OPER_26345_CustomDueOnly_US_IMPORT() {

      int lDueValue = 500;

      iEventIDs_1 = null;
      iEventIDs_2 = null;
      iEventIDs_3 = null;
      iTaskIDs_1 = null;

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      test_AL_TASK_OPER_26345_CustomDueOnly_US_VALIDATION();

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
      int lInterval = getinterval( iTask_CD );

      VerifyEvtSchedDead( iEventIDs_2, TestConstants.STATUS_ACTV,
            TestConstants.SCHED_FROM_CD_CUSTOM, lDueValue - lInterval, lDueValue, lInterval );

      // Verify AXON_DOMAIN_EVENT_ENTRY
      verifyTaskDrivingDeadlineRescheduledEventPublish();

   }


   /**
    * This test is to verify mx_al_ctrller_pkg.execute_task_import functionality of on OPER-26345
    * Task with CA only CUSTOM_START_DT is provided. The sched_dead_qt=custom start dt+ interval
    *
    *
    */

   @Test
   public void test_AL_TASK_OPER_26345_CustomStartOnly_CA_IMPORT() {

      String lStartDateS = "2015-01-01";
      // String lCompletionDueDateS = "2015-01-02";
      java.sql.Date lStartDate = converStringToSQLDate( lStartDateS, "yyyy-MM-dd" );
      // java.sql.Date lCompletionDueDate = converStringToSQLDate( lCompletionDueDateS, "yyyy-MM-dd"
      // );

      iEventIDs_1 = null;
      iEventIDs_2 = null;
      iEventIDs_3 = null;
      iTaskIDs_1 = null;

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      test_AL_TASK_OPER_26345_CustomStartOnly_CA_VALIDATION();

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
      Date lSched_dead_dt = DateAddDays( lStartDateS, lInterval );

      VerifyEvtSchedDead_CA( iEventIDs_2, TestConstants.STATUS_ACTV,
            TestConstants.SCHED_FROM_CD_CUSTOM, lStartDate, lSched_dead_dt );

      // Verify AXON_DOMAIN_EVENT_ENTRY
      verifyTaskDrivingDeadlineRescheduledEventPublish();

   }


   /**
    * This test is to verify mx_al_ctrller_pkg.execute_task_import functionality of on OPER-26345
    * Task with CA only CUSTOM_DUE_DT is provided. The sched_dead_qt=custom start dt+ interval
    *
    *
    */

   @Test
   public void test_AL_TASK_OPER_26345_CustomDueOnly_CA_IMPORT() {

      // String lCompletionDateS = "2015-01-01";
      String lCompletionDateS = "2015-01-20";
      // java.sql.Date lCompletionDate = converStringToSQLDate( lCompletionDateS, "yyyy-MM-dd" );
      java.sql.Date lCompletionDate = converStringToSQLDate( lCompletionDateS, "yyyy-MM-dd" );

      iEventIDs_1 = null;
      iEventIDs_2 = null;
      iEventIDs_3 = null;
      iTaskIDs_1 = null;

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      test_AL_TASK_OPER_26345_CustomDueOnly_CA_VALIDATION();

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
      Date lStart_dt = DateAddDays( lCompletionDateS, -lInterval );

      VerifyEvtSchedDead_CA( iEventIDs_2, TestConstants.STATUS_ACTV,
            TestConstants.SCHED_FROM_CD_CUSTOM, lStart_dt, lCompletionDate );

      // Verify AXON_DOMAIN_EVENT_ENTRY
      verifyTaskDrivingDeadlineRescheduledEventPublish();

   }


   /**
    * This test is to verify mx_al_ctrller_pkg.execute_task_import functionality of on OPER-8848
    * when task_sched_rule.def_initial_qt=null,
    * evt_sched_dead.INTERVAL_QT=task_sched_rule.def_interval_qt, task is non-recurring.
    *
    *
    */

   @Test
   public void test_AL_TASK_OPER_8848_NonRecurring_InitialNULL_IMPORT() {

      String lStartDateS = "2015-01-01";
      // String lCompletionDueDateS = "2015-01-02";
      java.sql.Date lStartDate = converStringToSQLDate( lStartDateS, "yyyy-MM-dd" );
      // java.sql.Date lCompletionDueDate = converStringToSQLDate( lCompletionDueDateS, "yyyy-MM-dd"
      // );

      iEventIDs_1 = null;
      iEventIDs_2 = null;
      iEventIDs_3 = null;
      iTaskIDs_1 = null;

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // test_AL_TASK_OPER_26345_CustomStartOnly_CA_VALIDATION();
      test_AL_TASK_OPER_8848_NonRecurring_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runImportTasks();

      // Get event IDs
      // iEventIDs_1 = getEventIDs( TestConstants.STATUS_COMPLETE, iTask_CD_8848 );
      iEventIDs_1 = getEventIDs( TestConstants.STATUS_ACTV, iTask_CD_8848 );

      // Validate evt_event
      Assert.assertTrue( "check event id 1 is not null", iEventIDs_1 != null );

      VerifyEvtEvent( iEventIDs_1, iEVENT_TYPE_CD, iEventIDs_1, "0" );

      // Get task ID
      iTaskIDs_1 = getTaskIDs( iTask_CD_8848 );
      // get inventory IDs
      simpleIDs iInvIDs_ACFT = getInvIDs( iSN );

      // verify sched_task table
      VerifySchedTask( iEventIDs_1, iEventIDs_1, iTaskIDs_1, TestConstants.TASK_CLASS_CD_REQ,
            iInvIDs_ACFT );

      // verify evt_event_rel
      VerifyEvtEventRel( iEventIDs_1, TestConstants.REL_TYPE_CD_DRVTASK, iEventIDs_1 );

      // verify evt_inv
      String lQuery = "select 1 from " + TableUtil.EVT_INV + " where EVENT_DB_ID="
            + iEventIDs_1.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_1.getNO_ID();
      Assert.assertTrue( "Check evt_inv table to verify the record exists: 1",
            RecordsExist( lQuery ) );

      // verify evt_stage
      VerifyEvtStage( iEventIDs_1, TestConstants.STATUS_ACTV );

      // verify evt_inv_usage
      lQuery = "select 1 from " + TableUtil.EVT_INV_USAGE + " where EVENT_DB_ID="
            + iEventIDs_1.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_1.getNO_ID();
      Assert.assertFalse( "Check evt_inv_usage table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify evt_sched_dead
      int lInterval = getinterval( iTask_CD_8848 );
      Assert.assertTrue( "Check interval should be 365", lInterval == 365 );
      // int lCurrentUsage = getCurrentUsage( iSN );
      Date lSched_dead_dt = DateAddDays( lStartDateS, lInterval );

      VerifyEvtSchedDead_CA( iEventIDs_1, TestConstants.STATUS_ACTV,
            TestConstants.SCHED_FROM_CD_CUSTOM, lStartDate, lSched_dead_dt,
            Integer.toString( lInterval ) );

      // Verify AXON_DOMAIN_EVENT_ENTRY
      verifyTaskDrivingDeadlineRescheduledEventPublish();

   }


   /**
    * This test is to verify mx_al_ctrller_pkg.execute_task_import functionality of on OPER-8848
    * when task_sched_rule.def_initial_qt=null,
    * evt_sched_dead.INTERVAL_QT=task_sched_rule.def_interval_qt, task is recurring.
    *
    *
    */

   @Test
   public void test_AL_TASK_OPER_8848_Recurring_InitialNULL_IMPORT() {

      String lStartDateS = "2015-01-01";
      // String lCompletionDueDateS = "2015-01-02";
      java.sql.Date lStartDate = converStringToSQLDate( lStartDateS, "yyyy-MM-dd" );
      // java.sql.Date lCompletionDueDate = converStringToSQLDate( lCompletionDueDateS, "yyyy-MM-dd"
      // );

      iEventIDs_1 = null;
      iEventIDs_2 = null;
      iEventIDs_3 = null;
      iTaskIDs_1 = null;

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // test_AL_TASK_OPER_26345_CustomStartOnly_CA_VALIDATION();
      test_AL_TASK_OPER_8848_Recurring_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runImportTasks();

      // Get event IDs
      // iEventIDs_1 = getEventIDs( TestConstants.STATUS_COMPLETE, iTask_CD_8848 );
      iEventIDs_1 = getEventIDs( TestConstants.STATUS_ACTV, iTask_CD_8848_2 );

      // Validate evt_event
      Assert.assertTrue( "check event id 1 is not null", iEventIDs_1 != null );

      VerifyEvtEvent( iEventIDs_1, iEVENT_TYPE_CD, iEventIDs_1, "0" );

      // Get task ID
      iTaskIDs_1 = getTaskIDs( iTask_CD_8848_2 );
      // get inventory IDs
      simpleIDs iInvIDs_ACFT = getInvIDs( iSN );

      // verify sched_task table
      VerifySchedTask( iEventIDs_1, iEventIDs_1, iTaskIDs_1, TestConstants.TASK_CLASS_CD_REQ,
            iInvIDs_ACFT );

      // verify evt_event_rel
      VerifyEvtEventRel( iEventIDs_1, TestConstants.REL_TYPE_CD_DRVTASK, iEventIDs_1 );

      // verify evt_inv
      String lQuery = "select 1 from " + TableUtil.EVT_INV + " where EVENT_DB_ID="
            + iEventIDs_1.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_1.getNO_ID();
      Assert.assertTrue( "Check evt_inv table to verify the record exists: 1",
            RecordsExist( lQuery ) );

      // verify evt_stage
      VerifyEvtStage( iEventIDs_1, TestConstants.STATUS_ACTV );

      // verify evt_sched_dead
      int lInterval = getinterval( iTask_CD_8848_2 );
      // Assert.assertTrue( "Check interval should be 500", lInterval == 500 );
      // int lCurrentUsage = getCurrentUsage( iSN );
      Date lSched_dead_dt = DateAddDays( lStartDateS, lInterval );

      VerifyEvtSchedDead_CA( iEventIDs_1, TestConstants.STATUS_ACTV,
            TestConstants.SCHED_FROM_CD_CUSTOM, lStartDate, lSched_dead_dt,
            Integer.toString( lInterval ) );

      // Verify AXON_DOMAIN_EVENT_ENTRY
      verifyTaskDrivingDeadlineRescheduledEventPublish();

   }


   /**
    * This test is to verify mx_al_ctrller_pkg.execute_task_import functionality of on OPER-8848
    * when task_sched_rule.def_initial_qt=0,
    * evt_sched_dead.INTERVAL_QT=0=task_sched_rule.def_initial_qt, task is recurring.
    *
    *
    */

   @Test
   public void test_AL_TASK_OPER_8848_Recurring_InitialZero_IMPORT() {

      String lStartDateS = "2015-01-01";
      // String lCompletionDueDateS = "2015-01-02";
      java.sql.Date lStartDate = converStringToSQLDate( lStartDateS, "yyyy-MM-dd" );
      // java.sql.Date lCompletionDueDate = converStringToSQLDate( lCompletionDueDateS, "yyyy-MM-dd"
      // );

      iEventIDs_1 = null;
      iEventIDs_2 = null;
      iEventIDs_3 = null;
      iTaskIDs_1 = null;

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // test_AL_TASK_OPER_26345_CustomStartOnly_CA_VALIDATION();
      test_AL_TASK_OPER_8848_Recurring_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runImportTasks();

      // Get event IDs
      // iEventIDs_1 = getEventIDs( TestConstants.STATUS_COMPLETE, iTask_CD_8848 );
      iEventIDs_1 = getEventIDs( TestConstants.STATUS_ACTV, iTask_CD_8848_2 );

      // Validate evt_event
      Assert.assertTrue( "check event id 1 is not null", iEventIDs_1 != null );

      VerifyEvtEvent( iEventIDs_1, iEVENT_TYPE_CD, iEventIDs_1, "0" );

      // Get task ID
      iTaskIDs_1 = getTaskIDs( iTask_CD_8848_2 );
      // get inventory IDs
      simpleIDs iInvIDs_ACFT = getInvIDs( iSN );

      // verify sched_task table
      VerifySchedTask( iEventIDs_1, iEventIDs_1, iTaskIDs_1, TestConstants.TASK_CLASS_CD_REQ,
            iInvIDs_ACFT );

      // verify evt_event_rel
      VerifyEvtEventRel( iEventIDs_1, TestConstants.REL_TYPE_CD_DRVTASK, iEventIDs_1 );

      // verify evt_inv
      String lQuery = "select 1 from " + TableUtil.EVT_INV + " where EVENT_DB_ID="
            + iEventIDs_1.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_1.getNO_ID();
      Assert.assertTrue( "Check evt_inv table to verify the record exists: 1",
            RecordsExist( lQuery ) );

      // verify evt_stage
      VerifyEvtStage( iEventIDs_1, TestConstants.STATUS_ACTV );

      // verify evt_sched_dead
      int lInterval = getintial( iTask_CD_8848_2 );
      // Assert.assertTrue( "Check interval should be 500", lInterval == 500 );
      // int lCurrentUsage = getCurrentUsage( iSN );
      Date lSched_dead_dt = DateAddDays( lStartDateS, lInterval );

      VerifyEvtSchedDead_CA( iEventIDs_1, TestConstants.STATUS_ACTV,
            TestConstants.SCHED_FROM_CD_CUSTOM, lStartDate, lSched_dead_dt,
            Integer.toString( lInterval ) );

      // Verify AXON_DOMAIN_EVENT_ENTRY
      verifyTaskDrivingDeadlineRescheduledEventPublish();

   }


   /**
    * This test is to verify mx_al_ctrller_pkg.execute_task_import functionality of on OPER-8848
    * when task_sched_rule.def_initial_qt=0,
    * evt_sched_dead.INTERVAL_QT=task_sched_rule.def_interval_qt, task is non-recurring.
    *
    *
    */

   @Test
   public void test_AL_TASK_OPER_8848_NonRecurring_InitialZero_IMPORT() {

      System.out.println(
            "=======Starting:  " + testName.getMethodName() + " ========================" );

      test_AL_TASK_OPER_8848_NonRecurring_InitialNULL_IMPORT();

      // Verify AXON_DOMAIN_EVENT_ENTRY
      verifyTaskDrivingDeadlineRescheduledEventPublish();

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
    * This function is to retrieve initial qt value by giving task_cd
    *
    *
    *
    */

   public int getintial( String aTask_CD ) {

      simpleIDs ltask_ids = getTaskIDs( aTask_CD );

      String[] iIds = { "DEF_INITIAL_QT" };
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

   public int getCurrentUsage( String aSN ) {

      simpleIDs linv_ids = getInvIDs( aSN );

      String[] iIds = { "TSN_QT" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "INV_NO_DB_ID", linv_ids.getNO_DB_ID() );
      lArgs.addArguments( "INV_NO_ID", linv_ids.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.INV_CURR_USAGE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      return Integer.parseInt( llists.get( 0 ).get( 0 ) );

   }


   // /**
   // * This function is to calculate start QT
   // *
   // *
   // *
   // */
   // public int getStartQT( String aStatus, int aCompletionDueValue, int aCompletionValue,
   // int aInterval ) {
   //
   // int lReturnQT = -999;
   //
   // switch ( aStatus ) {
   // case "COMPLETE":
   // lReturnQT = aCompletionDueValue - aInterval;
   // break;
   // case "ACTV":
   // lReturnQT = aCompletionDueValue;
   // break;
   // default:
   // Assert.assertTrue( "Invalid status: " + aStatus, false );
   // }
   //
   // return lReturnQT;
   //
   // }
   //
   //
   // public int getRemQT( String aStatus, int aStartQT, int aCompletionDueValue, int
   // aCompletionValue,
   // int aCurrentUsage, int aInterval ) {
   //
   // int lReturnQT = -999;
   //
   // switch ( aStatus ) {
   // case "COMPLETE":
   // lReturnQT = aCompletionValue - aCurrentUsage;
   // break;
   // case "ACTV":
   // lReturnQT = aStartQT + aInterval - aCurrentUsage;
   // break;
   // default:
   // Assert.assertTrue( "Invalid status: " + aStatus, false );
   // }
   //
   // return lReturnQT;
   //
   // }
   //

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
         int aSTART_QT, int aSCHED_DEAD_QT, int aINTERVAL_QT ) {

      String[] iIds = { "SCHED_FROM_CD", "START_QT", "SCHED_DEAD_QT", "INTERVAL_QT" };
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
         Assert.assertTrue( "SCHED_DEAD_QT",
               llists.get( 0 ).get( 2 ).equalsIgnoreCase( String.valueOf( aSCHED_DEAD_QT ) ) );
         Assert.assertTrue( "INTERVAL_QT",
               llists.get( 0 ).get( 3 ).equalsIgnoreCase( String.valueOf( aINTERVAL_QT ) ) );
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

         } catch ( ParseException e ) {
            e.printStackTrace();
            Assert.assertTrue( "VerifyEvtSchedDead_CA.", false );
         }
      }

   }


   /**
    * This function is to verify evt_sched_dead table for CA
    *
    *
    */
   public void VerifyEvtSchedDead_CA( simpleIDs aIDs, String aStatus, String aSCHED_FROM_CD,
         Date aSTART_QT, Date aSCHED_DEAD_DT, String aINTERVAL_QT ) {

      String[] iIds = { "SCHED_FROM_CD", "START_QT", "START_DT", "SCHED_DEAD_DT", "INTERVAL_QT" };
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

            Assert.assertTrue( "INTERVAL_QT",
                  llists.get( 0 ).get( 4 ).equalsIgnoreCase( aINTERVAL_QT ) );
         } catch ( ParseException e ) {
            e.printStackTrace();
            Assert.assertTrue( "VerifyEvtSchedDead_CA.", false );
         }
      }

   }


   /**
    * This function is to verify if the TaskDrivingDeadlineRescheduledEvent is published in the
    * AXON_DOMAIN_EVENT_ENTRY table
    *
    *
    *
    */
   @Override
   public void verifyTaskDrivingDeadlineRescheduledEventPublish() {

      try {
         StringBuilder strBuilder = new StringBuilder();
         strBuilder.append( "Select * from " + AXON_DOMAIN_EVENT_ENTRY_TABLE );
         strBuilder.append( " Where" );
         strBuilder.append( " payloadtype like" );
         strBuilder.append( " '%TaskDrivingDeadlineRescheduledEvent%'" );

         ResultSet result;

         result = runQuery( strBuilder.toString() );

         assertTrue( "Unfortunately, TaskDrivingDeadlineRescheduledEvent was not published",
               result.next() );

      } catch ( SQLException e ) {
         e.printStackTrace();
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
    * This function is to prepare data for test_test_AL_TASK_OPER_26345_CustomStartOnly_US_IMPORT
    *
    *
    */
   public void test_AL_TASK_OPER_26345_CustomStartOnly_US_VALIDATION() {

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
      lMapTask.put( "CUSTOM_START_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
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
      // lMapTaskSched.put( "COMPLETION_DUE_VALUE", "90" );
      lMapTaskSched.put( "CUSTOM_START_VALUE", "50" );
      lMapTaskSched.put( "CUSTOM_DUE_VALUE", "null" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // validate only invalid record got error code
      Assert.assertTrue( "There should not have error", checkTaskValidation_cap( "SN000001" ) );
   }


   /**
    * This function is to prepare data for test_AL_TASK_OPER_26345_CustomDueOnly_US_IMPORT
    *
    *
    */
   public void test_AL_TASK_OPER_26345_CustomDueOnly_US_VALIDATION() {

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
      // lMapTask.put( "CUSTOM_START_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTask.put( "CUSTOM_DUE_DT", "to_date('2015/01/01','yyyy/mm/dd')" );

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
      // lMapTaskSched.put( "COMPLETION_DUE_VALUE", "90" );
      lMapTaskSched.put( "CUSTOM_START_VALUE", null );
      lMapTaskSched.put( "CUSTOM_DUE_VALUE", "500" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // validate only invalid record got error code
      Assert.assertTrue( "There should not have error", checkTaskValidation_cap( "SN000001" ) );
   }


   /**
    * This function is to prepare data for test_AL_TASK_OPER_26345_CustomStartOnly_CA_IMPORT
    *
    *
    */
   public void test_AL_TASK_OPER_26345_CustomStartOnly_CA_VALIDATION() {

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
      lMapTask.put( "CUSTOM_START_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // run validation
      runValidateTasks();

      // validate only invalid record got error code
      Assert.assertTrue( "There should not have error", checkTaskValidation_cap( "SN000001" ) );
   }


   /**
    * This function is to prepare data for test_AL_TASK_OPER_26345_CustomDueOnly_CA_IMPORT
    *
    *
    */
   public void test_AL_TASK_OPER_26345_CustomDueOnly_CA_VALIDATION() {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'" + iTask_CD + "'" );
      // lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      // lMapTask.put( "COMPLETION_DUE_DT", "to_date('2015/01/20','yyyy/mm/dd')" );
      lMapTask.put( "CUSTOM_START_DT", "null" );
      lMapTask.put( "CUSTOM_DUE_DT", "to_date('2015/01/20','yyyy/mm/dd')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // run validation
      runValidateTasks();

      // validate only invalid record got error code
      Assert.assertTrue( "There should not have error", checkTaskValidation_cap( "SN000001" ) );
   }


   /**
    * This function is to prepare data for test_AL_TASK_OPER_8848_NonRecurring_XXX_IMPORT
    *
    *
    */
   public void test_AL_TASK_OPER_8848_NonRecurring_VALIDATION() {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'" + iTask_CD_8848 + "'" );
      lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      // lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      // lMapTask.put( "COMPLETION_DUE_DT", "to_date('2015/01/02','yyyy/mm/dd')" );
      lMapTask.put( "CUSTOM_START_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // run validation
      runValidateTasks();

      // validate only invalid record got error code
      Assert.assertTrue( "There should not have error", checkTaskValidation_cap( "SN000001" ) );
   }


   /**
    * This function is to prepare data for test_AL_TASK_OPER_8848_Recurring_XXX_IMPORT
    *
    *
    */
   public void test_AL_TASK_OPER_8848_Recurring_VALIDATION() {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'" + iTask_CD_8848_2 + "'" );
      lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      // lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      // lMapTask.put( "COMPLETION_DUE_DT", "to_date('2015/01/02','yyyy/mm/dd')" );
      lMapTask.put( "CUSTOM_START_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
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
    * This function is data setup for update initial_qt as 0
    *
    *
    *
    */
   public void dataSetup_initialqt( String aTask_CD, String aInitialQt ) {

      iTaskId = getTaskIDs( aTask_CD );

      String lquery = "update TASK_SCHED_RULE set def_initial_qt=" + aInitialQt
            + " where TASK_DB_ID=" + iTaskId.getNO_DB_ID() + " and TASK_ID=" + iTaskId.getNO_ID();
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
      //
      // // re-set blkchn to US
      // iTaskId = getTaskIDs( iTask_CD_BLOCKCHAIN_1 );
      // lquery =
      // "update TASK_SCHED_RULE set DEF_PREFIXED_QT=null, DEF_POSTFIXED_QT=null, DATA_TYPE_DB_ID='"
      // + lDataType_us_Day.getNO_DB_ID() + "', DATA_TYPE_ID='"
      // + lDataType_us_Day.getNO_ID() + "' where TASK_DB_ID=" + iTaskId.getNO_DB_ID()
      // + " and TASK_ID=" + iTaskId.getNO_ID();
      // runUpdate( lquery );
      //
      // iTaskId_2 = getTaskIDs( iTask_CD_BLOCKCHAIN_2 );
      // lquery =
      // "update TASK_SCHED_RULE set DEF_PREFIXED_QT=null, DEF_POSTFIXED_QT=null, DATA_TYPE_DB_ID='"
      // + lDataType_us_Day.getNO_DB_ID() + "', DATA_TYPE_ID='"
      // + lDataType_us_Day.getNO_ID() + "' where TASK_DB_ID=" + iTaskId_2.getNO_DB_ID()
      // + " and TASK_ID=" + iTaskId_2.getNO_ID();
      // runUpdate( lquery );

      iTaskId = getTaskIDs( iTask_CD_8848 );
      lquery = "update TASK_SCHED_RULE set def_initial_qt=null where TASK_DB_ID="
            + iTaskId.getNO_DB_ID() + " and TASK_ID=" + iTaskId.getNO_ID();
      runUpdate( lquery );

      if ( iInitialQT_Task_CD_8848_2 != null ) {
         iTaskId = getTaskIDs( iTask_CD_8848_2 );
         lquery = "update TASK_SCHED_RULE set def_initial_qt='" + iInitialQT_Task_CD_8848_2
               + "' where TASK_DB_ID=" + iTaskId.getNO_DB_ID() + " and TASK_ID="
               + iTaskId.getNO_ID();
         runUpdate( lquery );
      }

   }

}
