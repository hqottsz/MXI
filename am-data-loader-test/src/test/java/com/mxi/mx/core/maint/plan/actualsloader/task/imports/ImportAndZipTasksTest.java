/*
 * Confidential, proprietary and/or trade secret information of Mxi Technologies, Ltd.
 *
 * Copyright 2000-2016 Mxi Technologies, Ltd. All Rights Reserved.
 *
 * Except as expressly provided by written license signed by a duly appointed officer of Mxi
 * Technologies, Ltd., any disclosure, distribution, reproduction, compilation, modification,
 * creation of derivative works and/or other use of the Mxi source code is strictly prohibited.
 * Inclusion of a copyright notice shall not be taken to indicate that the source code has been
 * published.
 */

package com.mxi.mx.core.maint.plan.actualsloader.task.imports;

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
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.TestConstants;
import com.mxi.mx.util.ValidationAndImport;
import com.mxi.mx.util.WhereClause;


/**
 * This test suite contains all test cases for Actuals Loader loading of first time tasks
 *
 * @author Alicia Qian
 */

public class ImportAndZipTasksTest extends ActualsLoaderTest {

   @Rule
   public TestName testName = new TestName();
   ValidationAndImport ivalidationandimport;

   public simpleIDs iEventIDs_1 = null;
   public simpleIDs iEventIDs_2 = null;
   public simpleIDs iEventIDs_3 = null;
   public simpleIDs iEventIDs_4 = null;
   public simpleIDs iTaskIDs_1 = null;
   public simpleIDs iTaskIDs_2 = null;
   public simpleIDs iTaskIDs_3 = null;
   public simpleIDs iTaskIDs_4 = null;
   public simpleIDs iTaskId = null;

   // =================================
   public static String iTask_CD_BLOCK_NON_RECURRING = "BLOCK4";
   public static String iTask_CD_BLOCK_NON_RECURRING_REQ1 = "AT2";
   public static String iTask_CD_BLOCK_NON_RECURRING_REQ1_JIC = "AT-JIC-1";
   public static String iTask_CD_BLOCK_NON_RECURRING_sdesc = "BLOCK4 (AUTO BLOCK4)";
   public static String iTask_CD_BLOCK_NON_RECURRING_REQ1_sdesc = "AT2 (AT2)";
   public static String iTask_CD_BLOCK_NON_RECURRING_JIC1_sdesc = "AT-JIC-1 (AT JIC 1)";
   // ====================================
   public static String iTask_CD_BLOCK_NON_RECURRING_2 = "B1N CHAIN NON-RECURRING";
   public static String iTask_CD_BLOCK_NON_RECURRING_REQ1_2 = "SYS-REQ-1-NRBC";
   public static String iTask_CD_BLOCK_NON_RECURRING_REQ1_JIC_2_1 = "SYS-JIC-1";
   public static String iTask_CD_BLOCK_NON_RECURRING_REQ1_JIC_2_2 = "SYS-JIC-2";
   public static String iTask_CD_BLOCK_NON_RECURRING_sdesc_2 =
         "B1N CHAIN NON-RECURRING (Block 1N - Non-Recurring Block Chain)";
   public static String iTask_CD_BLOCK_NON_RECURRING_REQ1_sdesc_2 =
         "SYS-REQ-1-NRBC (System Requirement #1 for Non-Recurring Block Chain)";
   public static String iTask_CD_BLOCK_NON_RECURRING_JIC1_sdesc_2_1 =
         "SYS-JIC-1 (System Job Card 1)";
   public static String iTask_CD_BLOCK_NON_RECURRING_JIC1_sdesc_2_2 =
         "SYS-JIC-2 (System Job Card 2)";
   // =================================
   public static String iTask_CD_BLOCK_RECURRING = "1-TIME RECURRING 2";
   public static String iTask_CD_BLOCK_RECURRING_REQ1 = "SYS-REQ-1-RB 2";
   public static String iTask_CD_BLOCK_RECURRING_REQ1_JIC = "SYS-JIC-2";
   public static String iTask_CD_BLOCK_RECURRING_sdesc =
         "1-TIME RECURRING 2 (One-Time Recurring Block #2)";
   public static String iTask_CD_BLOCK_RECURRING_REQ1_sdesc =
         "SYS-REQ-1-RB 2 (System Requirement for 1-Time Recurring Block 2)";
   public static String iTask_CD_BLOCK_RECURRING_JIC1_sdesc = "SYS-JIC-2 (System Job Card 2)";

   // ===============================
   public static String iTask_CD_BLOCK_RECURRING_2 = "B1R CHAIN RECURRING";
   public static String iTask_CD_BLOCK_RECURRING_REQ1_2 = "SYS-REQ-1-RBC";
   public static String iTask_CD_BLOCK_RECURRING_REQ1_JIC_2_1 = "SYS-JIC-1";
   public static String iTask_CD_BLOCK_RECURRING_REQ1_JIC_2_2 = "SYS-JIC-2";
   public static String iTask_CD_BLOCK_RECURRING_sdesc_2 =
         "B1R CHAIN RECURRING (Block 1R - Recurring Block Chain)";
   public static String iTask_CD_BLOCK_RECURRING_REQ1_sdesc_2 =
         "SYS-REQ-1-RBC (System Requirement #1 for Recurring Block Chain)";
   public static String iTask_CD_BLOCK_RECURRING_JIC1_sdesc_2_1 = "SYS-JIC-1 (System Job Card 1)";
   public static String iTask_CD_BLOCK_RECURRING_JIC1_sdesc_2_2 = "SYS-JIC-2 (System Job Card 2)";

   // ========================================
   public String iEVENT_TYPE_CD = "TS";


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
    * This test is to verify active non recurring block (first time) to be generated correctly.
    *
    *
    */
   public void testNonRecurringFirstTimeBlock_validation() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first block record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000015'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN2'" );
      lMapTask.put( "MANUFACT_CD", "'11111'" );
      lMapTask.put( "TASK_CD", "'" + iTask_CD_BLOCK_NON_RECURRING + "'" );
      lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      // lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      // lMapTask.put( "COMPLETION_DUE_DT", "to_date('2015/01/02','yyyy/mm/dd')" );
      lMapTask.put( "CUSTOM_START_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // second req1 record
      lMapTask.clear();
      lMapTask.put( "SERIAL_NO_OEM", "'SN000015'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN2'" );
      lMapTask.put( "MANUFACT_CD", "'11111'" );
      lMapTask.put( "TASK_CD", "'" + iTask_CD_BLOCK_NON_RECURRING_REQ1 + "'" );
      lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      // lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      // lMapTask.put( "COMPLETION_DUE_DT", "to_date('2015/01/02','yyyy/mm/dd')" );
      lMapTask.put( "CUSTOM_START_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // validate only invalid record got error code
      runValidateTasks();
      checkTaskValidation_EXCEPTWARNING( "PASS" );

   }


   /**
    * This test is to verify active non recurring block (first time) to be generated correctly.
    *
    *
    */
   @Test
   public void testNonRecurringFirstTimeBlock_IMPORT() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testNonRecurringFirstTimeBlock_validation();

      System.out.println( "Finish validation" );

      // run import tasks
      runImportTasks();

      // Get event IDs
      iEventIDs_1 = getEventIDs( iTask_CD_BLOCK_NON_RECURRING_sdesc, TestConstants.STATUS_ACTV );
      iEventIDs_2 =
            getEventIDs( iTask_CD_BLOCK_NON_RECURRING_REQ1_sdesc, TestConstants.STATUS_ACTV );
      iEventIDs_3 =
            getEventIDs( iTask_CD_BLOCK_NON_RECURRING_JIC1_sdesc, TestConstants.STATUS_ACTV );

      // verify evt_event table
      Assert.assertTrue( "check event id 1 is not null", iEventIDs_1 != null );
      Assert.assertTrue( "check event id 2 is not null", iEventIDs_2 != null );
      Assert.assertTrue( "check event id 3 is not null", iEventIDs_3 != null );

      VerifyEvtEvent( iEventIDs_1, iEVENT_TYPE_CD, iEventIDs_1, null );
      VerifyEvtEvent( iEventIDs_2, iEVENT_TYPE_CD, iEventIDs_1, iEventIDs_1 );
      VerifyEvtEvent( iEventIDs_3, iEVENT_TYPE_CD, iEventIDs_1, iEventIDs_2 );

      // Get task ID
      iTaskIDs_1 = getTaskIDs( iTask_CD_BLOCK_NON_RECURRING );
      iTaskIDs_2 = getTaskIDs( iTask_CD_BLOCK_NON_RECURRING_REQ1 );
      iTaskIDs_3 = getTaskIDs( iTask_CD_BLOCK_NON_RECURRING_REQ1_JIC );

      // verify sched_task table
      VerifySchedTask( iEventIDs_1, iEventIDs_1, iTaskIDs_1, TestConstants.TASK_CLASS_CD_BLOCK );
      VerifySchedTask( iEventIDs_2, iEventIDs_1, iTaskIDs_2, TestConstants.TASK_CLASS_CD_REQ );
      VerifySchedTask( iEventIDs_3, iEventIDs_1, iTaskIDs_3, TestConstants.TASK_CLASS_CD_JIC );

      // verify evt_inv
      String lQuery = "select 1 from " + TableUtil.EVT_INV + " where EVENT_DB_ID="
            + iEventIDs_1.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_1.getNO_ID();
      Assert.assertTrue( "Check evt_inv table to verify the record exists: 1",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.EVT_INV + " where EVENT_DB_ID="
            + iEventIDs_2.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_2.getNO_ID();
      Assert.assertTrue( "Check evt_inv table to verify the record exists: 2",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.EVT_INV + " where EVENT_DB_ID="
            + iEventIDs_3.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_3.getNO_ID();
      Assert.assertTrue( "Check evt_inv table to verify the record exists: 3",
            RecordsExist( lQuery ) );

      // verify evt_stage
      VerifyEvtStage( iEventIDs_1, TestConstants.STATUS_ACTV );
      VerifyEvtStage( iEventIDs_2, TestConstants.STATUS_ACTV );
      VerifyEvtStage( iEventIDs_2, TestConstants.STATUS_ASSIGN );
      VerifyEvtStage( iEventIDs_3, TestConstants.STATUS_ACTV );
   }


   /**
    * This test is to verify active non recurring block (first time) without completed sub tasks to
    * be generated correctly. task SYS-REQ-3-NRBC is missing in the block B1N CHAIN NON-RECURRING.
    *
    */
   public void testNonRecurringFirstTimeBlock_MissingSub_validation() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first block record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000015'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN2'" );
      lMapTask.put( "MANUFACT_CD", "'11111'" );
      lMapTask.put( "TASK_CD", "'" + iTask_CD_BLOCK_NON_RECURRING_2 + "'" );
      lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      // lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      // lMapTask.put( "COMPLETION_DUE_DT", "to_date('2015/01/02','yyyy/mm/dd')" );
      lMapTask.put( "CUSTOM_START_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // second req1 record
      lMapTask.clear();
      lMapTask.put( "SERIAL_NO_OEM", "'SN000015'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN2'" );
      lMapTask.put( "MANUFACT_CD", "'11111'" );
      lMapTask.put( "TASK_CD", "'" + iTask_CD_BLOCK_NON_RECURRING_REQ1_2 + "'" );
      lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      // lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      // lMapTask.put( "COMPLETION_DUE_DT", "to_date('2015/01/02','yyyy/mm/dd')" );
      lMapTask.put( "CUSTOM_START_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // validate only invalid record got error code
      runValidateTasks();
      checkTaskValidation_EXCEPTWARNING( "PASS" );

   }


   /**
    * This test is to verify active non recurring block (first time) without completed sub tasks to
    * be generated correctly. task SYS-REQ-3-NRBC is missing in the block B1N CHAIN NON-RECURRING.
    *
    */
   @Test
   public void testNonRecurringFirstTimeBlock_MissingSub_IMPORT() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testNonRecurringFirstTimeBlock_MissingSub_validation();

      System.out.println( "Finish validation" );

      // run import tasks
      runImportTasks();

      // Get event IDs
      iEventIDs_1 = getEventIDs( iTask_CD_BLOCK_NON_RECURRING_sdesc_2, TestConstants.STATUS_ACTV );
      iEventIDs_2 =
            getEventIDs( iTask_CD_BLOCK_NON_RECURRING_REQ1_sdesc_2, TestConstants.STATUS_ACTV );
      iEventIDs_3 =
            getEventIDs( iTask_CD_BLOCK_NON_RECURRING_JIC1_sdesc_2_1, TestConstants.STATUS_ACTV );
      iEventIDs_4 =
            getEventIDs( iTask_CD_BLOCK_NON_RECURRING_JIC1_sdesc_2_2, TestConstants.STATUS_ACTV );

      // verify evt_event table
      Assert.assertTrue( "check event id 1 is not null", iEventIDs_1 != null );
      Assert.assertTrue( "check event id 2 is not null", iEventIDs_2 != null );
      Assert.assertTrue( "check event id 3 is not null", iEventIDs_3 != null );
      Assert.assertTrue( "check event id 4 is not null", iEventIDs_4 != null );

      VerifyEvtEvent( iEventIDs_1, iEVENT_TYPE_CD, iEventIDs_1, null );
      VerifyEvtEvent( iEventIDs_2, iEVENT_TYPE_CD, iEventIDs_1, iEventIDs_1 );
      VerifyEvtEvent( iEventIDs_3, iEVENT_TYPE_CD, iEventIDs_1, iEventIDs_2 );
      VerifyEvtEvent( iEventIDs_4, iEVENT_TYPE_CD, iEventIDs_1, iEventIDs_2 );

      // Get task ID
      iTaskIDs_1 = getTaskIDs( iTask_CD_BLOCK_NON_RECURRING_2 );
      iTaskIDs_2 = getTaskIDs( iTask_CD_BLOCK_NON_RECURRING_REQ1_2 );
      iTaskIDs_3 = getTaskIDs( iTask_CD_BLOCK_NON_RECURRING_REQ1_JIC_2_1 );
      iTaskIDs_4 = getTaskIDs( iTask_CD_BLOCK_NON_RECURRING_REQ1_JIC_2_2 );

      // verify sched_task table
      VerifySchedTask( iEventIDs_1, iEventIDs_1, iTaskIDs_1, TestConstants.TASK_CLASS_CD_BLOCK );
      VerifySchedTask( iEventIDs_2, iEventIDs_1, iTaskIDs_2, TestConstants.TASK_CLASS_CD_REQ );
      VerifySchedTask( iEventIDs_3, iEventIDs_1, iTaskIDs_3, TestConstants.TASK_CLASS_CD_JIC );
      VerifySchedTask( iEventIDs_4, iEventIDs_1, iTaskIDs_4, TestConstants.TASK_CLASS_CD_JIC );

      // verify evt_event_rel
      String lQuery = "select 1 from " + TableUtil.EVT_EVENT_REL + " where EVENT_DB_ID="
            + iEventIDs_1.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_1.getNO_ID();
      Assert.assertTrue( "Check evt_event_rel table to verify the record exists: 1",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.EVT_EVENT_REL + " where EVENT_DB_ID="
            + iEventIDs_2.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_2.getNO_ID();
      Assert.assertTrue( "Check evt_event_rel table to verify the record exists: 2",
            RecordsExist( lQuery ) );

      // verify evt_inv
      lQuery = "select 1 from " + TableUtil.EVT_INV + " where EVENT_DB_ID="
            + iEventIDs_1.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_1.getNO_ID();
      Assert.assertTrue( "Check evt_inv table to verify the record exists: 1",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.EVT_INV + " where EVENT_DB_ID="
            + iEventIDs_2.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_2.getNO_ID();
      Assert.assertTrue( "Check evt_inv table to verify the record exists: 2",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.EVT_INV + " where EVENT_DB_ID="
            + iEventIDs_3.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_3.getNO_ID();
      Assert.assertTrue( "Check evt_inv table to verify the record exists: 3",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.EVT_INV + " where EVENT_DB_ID="
            + iEventIDs_4.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_4.getNO_ID();
      Assert.assertTrue( "Check evt_inv table to verify the record exists: 4",
            RecordsExist( lQuery ) );

      // verify evt_stage
      VerifyEvtStage( iEventIDs_1, TestConstants.STATUS_ACTV );
      VerifyEvtStage( iEventIDs_2, TestConstants.STATUS_ACTV );
      VerifyEvtStage( iEventIDs_2, TestConstants.STATUS_ASSIGN );
      VerifyEvtStage( iEventIDs_3, TestConstants.STATUS_ACTV );
      VerifyEvtStage( iEventIDs_4, TestConstants.STATUS_ACTV );

      // verify evt_sched_dead
      lQuery = "select 1 from " + TableUtil.EVT_SCHED_DEAD + " where EVENT_DB_ID="
            + iEventIDs_1.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_1.getNO_ID();
      Assert.assertTrue( "Check evt_inv table to verify the record exists: 3",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.EVT_SCHED_DEAD + " where EVENT_DB_ID="
            + iEventIDs_2.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_2.getNO_ID();
      Assert.assertTrue( "Check evt_inv table to verify the record exists: 4",
            RecordsExist( lQuery ) );

   }


   /**
    * This test is to verify active recurring block (first time) to be generated correctly.
    *
    *
    */

   public void testRecurringFirstTimeBlock_validation() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first block record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000015'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN2'" );
      lMapTask.put( "MANUFACT_CD", "'11111'" );
      lMapTask.put( "TASK_CD", "'" + iTask_CD_BLOCK_RECURRING + "'" );
      lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      // lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      // lMapTask.put( "COMPLETION_DUE_DT", "to_date('2015/01/02','yyyy/mm/dd')" );
      lMapTask.put( "CUSTOM_START_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // second req1 record
      lMapTask.clear();
      lMapTask.put( "SERIAL_NO_OEM", "'SN000015'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN2'" );
      lMapTask.put( "MANUFACT_CD", "'11111'" );
      lMapTask.put( "TASK_CD", "'" + iTask_CD_BLOCK_RECURRING_REQ1 + "'" );
      lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      // lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      // lMapTask.put( "COMPLETION_DUE_DT", "to_date('2015/01/02','yyyy/mm/dd')" );
      lMapTask.put( "CUSTOM_START_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // validate only invalid record got error code
      runValidateTasks();
      checkTaskValidation_EXCEPTWARNING( "PASS" );

   }


   /**
    * This test is to verify active recurring block (first time) to be generated correctly.
    *
    *
    */
   @Test
   public void testRecurringFirstTimeBlock_IMPORT() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testRecurringFirstTimeBlock_validation();

      System.out.println( "Finish validation" );

      // run import tasks
      runImportTasks();

      // Get event IDs
      iEventIDs_1 = getEventIDs( iTask_CD_BLOCK_RECURRING_sdesc, TestConstants.STATUS_ACTV );
      iEventIDs_2 = getEventIDs( iTask_CD_BLOCK_RECURRING_REQ1_sdesc, TestConstants.STATUS_ACTV );
      iEventIDs_3 = getEventIDs( iTask_CD_BLOCK_RECURRING_JIC1_sdesc, TestConstants.STATUS_ACTV );

      // verify evt_event table
      Assert.assertTrue( "check event id 1 is not null", iEventIDs_1 != null );
      Assert.assertTrue( "check event id 2 is not null", iEventIDs_2 != null );
      Assert.assertTrue( "check event id 3 is not null", iEventIDs_3 != null );

      VerifyEvtEvent( iEventIDs_1, iEVENT_TYPE_CD, iEventIDs_1, null );
      VerifyEvtEvent( iEventIDs_2, iEVENT_TYPE_CD, iEventIDs_1, iEventIDs_1 );
      VerifyEvtEvent( iEventIDs_3, iEVENT_TYPE_CD, iEventIDs_1, iEventIDs_2 );

      // Get task ID
      iTaskIDs_1 = getTaskIDs( iTask_CD_BLOCK_RECURRING );
      iTaskIDs_2 = getTaskIDs( iTask_CD_BLOCK_RECURRING_REQ1 );
      iTaskIDs_3 = getTaskIDs( iTask_CD_BLOCK_RECURRING_REQ1_JIC );

      // verify sched_task table
      VerifySchedTask( iEventIDs_1, iEventIDs_1, iTaskIDs_1, TestConstants.TASK_CLASS_CD_BLOCK );
      VerifySchedTask( iEventIDs_2, iEventIDs_1, iTaskIDs_2, TestConstants.TASK_CLASS_CD_REQ );
      VerifySchedTask( iEventIDs_3, iEventIDs_1, iTaskIDs_3, TestConstants.TASK_CLASS_CD_JIC );

      // verify evt_inv
      String lQuery = "select 1 from " + TableUtil.EVT_INV + " where EVENT_DB_ID="
            + iEventIDs_1.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_1.getNO_ID();
      Assert.assertTrue( "Check evt_inv table to verify the record exists: 1",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.EVT_INV + " where EVENT_DB_ID="
            + iEventIDs_2.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_2.getNO_ID();
      Assert.assertTrue( "Check evt_inv table to verify the record exists: 2",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.EVT_INV + " where EVENT_DB_ID="
            + iEventIDs_3.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_3.getNO_ID();
      Assert.assertTrue( "Check evt_inv table to verify the record exists: 3",
            RecordsExist( lQuery ) );

      // verify evt_event_rel
      lQuery = "select 1 from " + TableUtil.EVT_EVENT_REL + " where EVENT_DB_ID="
            + iEventIDs_1.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_1.getNO_ID();
      Assert.assertTrue( "Check evt_event_rel table to verify the record exists: 1",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.EVT_EVENT_REL + " where EVENT_DB_ID="
            + iEventIDs_2.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_2.getNO_ID();
      Assert.assertTrue( "Check evt_event_rel table to verify the record exists: 2",
            RecordsExist( lQuery ) );

      // verify evt_stage
      VerifyEvtStage( iEventIDs_1, TestConstants.STATUS_ACTV );
      VerifyEvtStage( iEventIDs_2, TestConstants.STATUS_ACTV );
      VerifyEvtStage( iEventIDs_2, TestConstants.STATUS_ASSIGN );
      VerifyEvtStage( iEventIDs_3, TestConstants.STATUS_ACTV );

      // verify evt_sched_dead
      lQuery = "select 1 from " + TableUtil.EVT_SCHED_DEAD + " where EVENT_DB_ID="
            + iEventIDs_1.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_1.getNO_ID();
      Assert.assertTrue( "Check evt_sched_dead table to verify the record exists: 1",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.EVT_SCHED_DEAD + " where EVENT_DB_ID="
            + iEventIDs_2.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_2.getNO_ID();
      Assert.assertTrue( "Check evt_sched_dead table to verify the record exists: 2",
            RecordsExist( lQuery ) );

   }


   /**
    * This test is to verify active recurring block (first time) without completed sub tasks to be
    * generated correctly. task SYS-REQ-3-RBC is missing in the block B1R CHAIN RECURRING.
    *
    */

   public void testRecurringFirstTimeBlock_MissingSub_validation() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first block record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000015'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN2'" );
      lMapTask.put( "MANUFACT_CD", "'11111'" );
      lMapTask.put( "TASK_CD", "'" + iTask_CD_BLOCK_RECURRING_2 + "'" );
      lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      // lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      // lMapTask.put( "COMPLETION_DUE_DT", "to_date('2015/01/02','yyyy/mm/dd')" );
      lMapTask.put( "CUSTOM_START_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // second req1 record
      lMapTask.clear();
      lMapTask.put( "SERIAL_NO_OEM", "'SN000015'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN2'" );
      lMapTask.put( "MANUFACT_CD", "'11111'" );
      lMapTask.put( "TASK_CD", "'" + iTask_CD_BLOCK_RECURRING_REQ1_2 + "'" );
      lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      // lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      // lMapTask.put( "COMPLETION_DUE_DT", "to_date('2015/01/02','yyyy/mm/dd')" );
      lMapTask.put( "CUSTOM_START_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // validate only invalid record got error code
      runValidateTasks();
      checkTaskValidation_EXCEPTWARNING( "PASS" );

   }


   /**
    * This test is to verify active recurring block (first time) without completed sub tasks to be
    * generated correctly. task SYS-REQ-3-RBC is missing in the block B1R CHAIN RECURRING.
    *
    */
   @Test
   public void testRecurringFirstTimeBlock_MissingSub_IMPORT() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testRecurringFirstTimeBlock_MissingSub_validation();

      System.out.println( "Finish validation" );

      // run import tasks
      runImportTasks();

      // Get event IDs
      iEventIDs_1 = getEventIDs( iTask_CD_BLOCK_RECURRING_sdesc_2, TestConstants.STATUS_ACTV );
      iEventIDs_2 = getEventIDs( iTask_CD_BLOCK_RECURRING_REQ1_sdesc_2, TestConstants.STATUS_ACTV );
      iEventIDs_3 =
            getEventIDs( iTask_CD_BLOCK_RECURRING_JIC1_sdesc_2_1, TestConstants.STATUS_ACTV );
      iEventIDs_4 =
            getEventIDs( iTask_CD_BLOCK_RECURRING_JIC1_sdesc_2_2, TestConstants.STATUS_ACTV );

      // verify evt_event table
      Assert.assertTrue( "check event id 1 is not null", iEventIDs_1 != null );
      Assert.assertTrue( "check event id 2 is not null", iEventIDs_2 != null );
      Assert.assertTrue( "check event id 3 is not null", iEventIDs_3 != null );
      Assert.assertTrue( "check event id 4 is not null", iEventIDs_4 != null );

      VerifyEvtEvent( iEventIDs_1, iEVENT_TYPE_CD, iEventIDs_1, null );
      VerifyEvtEvent( iEventIDs_2, iEVENT_TYPE_CD, iEventIDs_1, iEventIDs_1 );
      VerifyEvtEvent( iEventIDs_3, iEVENT_TYPE_CD, iEventIDs_1, iEventIDs_2 );
      VerifyEvtEvent( iEventIDs_4, iEVENT_TYPE_CD, iEventIDs_1, iEventIDs_2 );

      // Get task ID
      iTaskIDs_1 = getTaskIDs( iTask_CD_BLOCK_RECURRING_2 );
      iTaskIDs_2 = getTaskIDs( iTask_CD_BLOCK_RECURRING_REQ1_2 );
      iTaskIDs_3 = getTaskIDs( iTask_CD_BLOCK_RECURRING_REQ1_JIC_2_1 );
      iTaskIDs_4 = getTaskIDs( iTask_CD_BLOCK_RECURRING_REQ1_JIC_2_2 );

      // verify sched_task table
      VerifySchedTask( iEventIDs_1, iEventIDs_1, iTaskIDs_1, TestConstants.TASK_CLASS_CD_BLOCK );
      VerifySchedTask( iEventIDs_2, iEventIDs_1, iTaskIDs_2, TestConstants.TASK_CLASS_CD_REQ );
      VerifySchedTask( iEventIDs_3, iEventIDs_1, iTaskIDs_3, TestConstants.TASK_CLASS_CD_JIC );
      VerifySchedTask( iEventIDs_4, iEventIDs_1, iTaskIDs_4, TestConstants.TASK_CLASS_CD_JIC );

      // verify evt_event_rel
      String lQuery = "select 1 from " + TableUtil.EVT_EVENT_REL + " where EVENT_DB_ID="
            + iEventIDs_1.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_1.getNO_ID();
      Assert.assertTrue( "Check evt_event_rel table to verify the record exists: 1",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.EVT_EVENT_REL + " where EVENT_DB_ID="
            + iEventIDs_2.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_2.getNO_ID();
      Assert.assertTrue( "Check evt_event_rel table to verify the record exists: 2",
            RecordsExist( lQuery ) );

      // verify evt_inv
      lQuery = "select 1 from " + TableUtil.EVT_INV + " where EVENT_DB_ID="
            + iEventIDs_1.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_1.getNO_ID();
      Assert.assertTrue( "Check evt_inv table to verify the record exists: 1",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.EVT_INV + " where EVENT_DB_ID="
            + iEventIDs_2.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_2.getNO_ID();
      Assert.assertTrue( "Check evt_inv table to verify the record exists: 2",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.EVT_INV + " where EVENT_DB_ID="
            + iEventIDs_3.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_3.getNO_ID();
      Assert.assertTrue( "Check evt_inv table to verify the record exists: 3",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.EVT_INV + " where EVENT_DB_ID="
            + iEventIDs_4.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_4.getNO_ID();
      Assert.assertTrue( "Check evt_inv table to verify the record exists: 4",
            RecordsExist( lQuery ) );

      // verify evt_stage
      VerifyEvtStage( iEventIDs_1, TestConstants.STATUS_ACTV );
      VerifyEvtStage( iEventIDs_2, TestConstants.STATUS_ACTV );
      VerifyEvtStage( iEventIDs_2, TestConstants.STATUS_ASSIGN );
      VerifyEvtStage( iEventIDs_3, TestConstants.STATUS_ACTV );
      VerifyEvtStage( iEventIDs_4, TestConstants.STATUS_ACTV );

      // verify evt_sched_dead
      lQuery = "select 1 from " + TableUtil.EVT_SCHED_DEAD + " where EVENT_DB_ID="
            + iEventIDs_1.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_1.getNO_ID();
      Assert.assertTrue( "Check evt_inv table to verify the record exists: 3",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.EVT_SCHED_DEAD + " where EVENT_DB_ID="
            + iEventIDs_2.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_2.getNO_ID();
      Assert.assertTrue( "Check evt_inv table to verify the record exists: 4",
            RecordsExist( lQuery ) );

   }


   // ========================================================================================
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
      lArgs.addArguments( "EVENT_STATUS_CD", aEVENT_STATUS_CD );

      String iQueryString = TableUtil.buildTableQueryOrderBy( TableUtil.EVT_STAGE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "EVENT_STATUS_CD",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aEVENT_STATUS_CD ) );

   }


   /**
    * This function is to verify sched_task table
    *
    *
    */
   public void VerifySchedTask( simpleIDs aIDs, simpleIDs aHIDs, simpleIDs aTaskIDs,
         String aTASK_CLASS_CD ) {

      String[] iIds = { "H_SCHED_DB_ID", "H_SCHED_ID", "TASK_DB_ID", "TASK_ID", "TASK_CLASS_CD", };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "SCHED_DB_ID", aIDs.getNO_DB_ID() );
      lArgs.addArguments( "SCHED_ID", aIDs.getNO_ID() );

      String iQueryString =
            TableUtil.buildTableQueryOrderBy( TableUtil.SCHED_STASK, lfields, lArgs );
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

   }


   /**
    * This function is to verify evt_event table
    *
    *
    */

   public void VerifyEvtEvent( simpleIDs aIDs, String aEVENT_TYPE_CD, simpleIDs aHIDs,
         simpleIDs aNHIDs ) {

      String[] iIds =
            { "EVENT_TYPE_CD", "H_EVENT_DB_ID", "H_EVENT_ID", "NH_EVENT_DB_ID", "NH_EVENT_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "EVENT_DB_ID", aIDs.getNO_DB_ID() );
      lArgs.addArguments( "EVENT_ID", aIDs.getNO_ID() );

      String iQueryString = TableUtil.buildTableQueryOrderBy( TableUtil.EVT_EVENT, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "EVENT_TYPE_CD",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aEVENT_TYPE_CD ) );
      Assert.assertTrue( "H_EVENT_DB_ID",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( aHIDs.getNO_DB_ID() ) );
      Assert.assertTrue( "H_EVENT_ID",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aHIDs.getNO_ID() ) );
      if ( aNHIDs != null ) {
         Assert.assertTrue( "NH_EVENT_DB_ID",
               llists.get( 0 ).get( 3 ).equalsIgnoreCase( aNHIDs.getNO_DB_ID() ) );
         Assert.assertTrue( "NH_EVENT_DB_ID",
               llists.get( 0 ).get( 4 ).equalsIgnoreCase( aNHIDs.getNO_ID() ) );
      }
   }


   /**
    * This function is to retrieve event ID by giving task_cd and task status.
    *
    *
    *
    */
   @Override
   public simpleIDs getEventIDs( String aeventsdesc, String aStatus ) {

      String leventsdesc = aeventsdesc;
      String[] iIds = { "EVENT_DB_ID", "EVENT_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "EVENT_SDESC", leventsdesc );
      lArgs.addArguments( "EVENT_STATUS_CD", aStatus );

      String iQueryString = TableUtil.buildTableQueryOrderBy( TableUtil.EVT_EVENT, lfields, lArgs );
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

         // delete evt_event
         lStrDelete = "delete from " + TableUtil.EVT_EVENT + "  where H_EVENT_DB_ID="
               + iEventIDs_1.getNO_DB_ID() + " and H_EVENT_ID=" + iEventIDs_1.getNO_ID();
         executeSQL( lStrDelete );

         // delete sched_stask
         lStrDelete = "delete from " + TableUtil.SCHED_STASK + "  where H_SCHED_DB_ID="
               + iEventIDs_1.getNO_DB_ID() + " and H_SCHED_ID=" + iEventIDs_1.getNO_ID();
         executeSQL( lStrDelete );

      }

      deleteAllTaskTables( iEventIDs_1 );
      deleteAllTaskTables( iEventIDs_2 );
      deleteAllTaskTables( iEventIDs_3 );
      deleteAllTaskTables( iEventIDs_4 );

   }

}
