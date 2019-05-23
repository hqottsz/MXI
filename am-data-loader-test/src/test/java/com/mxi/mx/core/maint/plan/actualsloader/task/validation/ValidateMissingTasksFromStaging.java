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

package com.mxi.mx.core.maint.plan.actualsloader.task.validation;

import java.sql.CallableStatement;
import java.sql.ResultSet;
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

import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;
import com.mxi.mx.util.ActualsLoaderTest;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.WhereClause;


/**
 * This test suite contains all the test cases for the task which are missing from staging table
 *
 * @author Alicia Qian
 */

public class ValidateMissingTasksFromStaging extends ActualsLoaderTest {

   @Rule
   public TestName testName = new TestName();

   // the following IDs need to be delete in after class
   public simpleIDs iEventIDs_1 = null;
   public simpleIDs iEventIDs_2 = null;
   public simpleIDs iTaskIDs_1 = null;
   public simpleIDs iTaskIDs_2 = null;
   public simpleIDs iInvIDs_ACFT = null;
   public simpleIDs iTaskId = null;
   public simpleIDs iTaskId_2 = null;


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


   /*
    * OPER-28040: Actuals Loader missing tasks report is not accounting for part-based tasks that
    * are already staged in c_ri_task. All the missing tasks will be listed in missing task table
    * when task is acft based task including part based tasks
    */

   @Test
   public void testOPER_28040_1() {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'AT_TEST'" );
      lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      // lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
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
      lMapTaskSched.put( "TASK_CD", "'AT_TEST'" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      lMapTaskSched.put( "CUSTOM_START_VALUE", "null" );
      lMapTaskSched.put( "CUSTOM_DUE_VALUE", "null" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks_MISSINGTASK();

      // validate no error (except for waring)
      checkTaskValidation_EXCEPTWARNING( "PASS" );

      // verify there are missing tasks is generated. al_missing_task table;
      String lQuery = "select 1 from " + TableUtil.AL_MISSING_TASK;
      Assert.assertTrue( "Check missing task table should not be empty", RecordsExist( lQuery ) );

      // simpleIDs lInvIds = getInvIDs( "SNAUTOTRK001" );
      // lQuery = "select 1 from " + TableUtil.AL_MISSING_TASK + " where INV_NO_DB_ID='"
      // + lInvIds.getNO_DB_ID() + "' and " + "INV_NO_ID='" + lInvIds.getNO_ID() + "'";
      // Assert.assertFalse( "Check missing task table should not include REQ2 task on SNAUTOTRK001.
      // ",
      // RecordsExist( lQuery ) );

      simpleIDs lTask_Ids = getTaskIDs( "REQ2" );
      lQuery = "select 1 from " + TableUtil.AL_MISSING_TASK + "  where TASK_DB_ID='"
            + lTask_Ids.getNO_DB_ID() + "' and TASK_ID='" + lTask_Ids.getNO_ID() + "'";
      Assert.assertFalse( "Check missing task table should not include REQ2 task on SNAUTOTRK001. ",
            RecordsExist( lQuery ) );

      lTask_Ids = getTaskIDs( "ATTRKT3" );
      lQuery = "select 1 from " + TableUtil.AL_MISSING_TASK + "  where TASK_DB_ID='"
            + lTask_Ids.getNO_DB_ID() + "' and TASK_ID='" + lTask_Ids.getNO_ID() + "'";
      Assert.assertTrue( "Check missing task table should include ATTRKT3 task on SNAUTOTRK001. ",
            RecordsExist( lQuery ) );

   }


   /*
    * OPER-28040: Actuals Loader missing tasks report is not accounting for part-based tasks that
    * are already staged in c_ri_task. All the missing tasks will be listed in missing task table
    * when task is TRK based first time run recurrring task including part based tasks.
    */

   @Test
   public void testOPER_28040_2() {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SNAUTOTRK005'" );
      lMapTask.put( "PART_NO_OEM", "'A0000002'" );
      lMapTask.put( "MANUFACT_CD", "'11111'" );
      lMapTask.put( "TASK_CD", "'MP-TRK-REQ'" );
      lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      // lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      // lMapTask.put( "COMPLETION_DUE_DT", "to_date('2015/01/02','yyyy/mm/dd')" );
      lMapTask.put( "CUSTOM_START_DT", "null" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();

      // first completed record
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SNAUTOTRK005'" );
      lMapTaskSched.put( "PART_NO_OEM", "'A0000002'" );
      lMapTaskSched.put( "MANUFACT_CD", "'11111'" );
      lMapTaskSched.put( "TASK_CD", "'MP-TRK-REQ'" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      lMapTaskSched.put( "CUSTOM_START_VALUE", "null" );
      lMapTaskSched.put( "CUSTOM_DUE_VALUE", "null" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks_MISSINGTASK();

      // validate no error (except for waring)
      checkTaskValidation_EXCEPTWARNING( "PASS" );

      // verify there are missing tasks is generated. al_missing_task table;
      String lQuery = "select 1 from " + TableUtil.AL_MISSING_TASK;
      Assert.assertTrue( "Check missing task table should not be empty", RecordsExist( lQuery ) );

      simpleIDs lInvIds = getInvIDs( "SNAUTOTRK001" );
      lQuery = "select 1 from " + TableUtil.AL_MISSING_TASK + "  where INV_NO_DB_ID='"
            + lInvIds.getNO_DB_ID() + "' and " + "INV_NO_ID='" + lInvIds.getNO_ID() + "'";
      Assert.assertFalse( "Check missing task table should not include REQ2 task on SNAUTOTRK001. ",
            RecordsExist( lQuery ) );

      simpleIDs lTask_Ids = getTaskIDs( "ATTRKT3" );
      lQuery = "select 1 from " + TableUtil.AL_MISSING_TASK + "  where TASK_DB_ID='"
            + lTask_Ids.getNO_DB_ID() + "' and TASK_ID='" + lTask_Ids.getNO_ID() + "'";
      Assert.assertTrue(
            "Check missing task table should not include ATTRKT3 task on SNAUTOTRK001. ",
            RecordsExist( lQuery ) );
   }


   /*
    * OPER-28040: Actuals Loader missing tasks report is not accounting for part-based tasks that
    * are already staged in c_ri_task. All the missing tasks will be listed in missing task table
    * when task is TRK based completed recurring task.including part based tasks.
    */

   @Test
   public void testOPER_28040_3() {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SNAUTOTRK005'" );
      lMapTask.put( "PART_NO_OEM", "'A0000002'" );
      lMapTask.put( "MANUFACT_CD", "'11111'" );
      lMapTask.put( "TASK_CD", "'MP-TRK-REQ'" );
      // lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTask.put( "COMPLETION_DUE_DT", "to_date('2015/01/02','yyyy/mm/dd')" );
      lMapTask.put( "CUSTOM_START_DT", "null" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();

      // first completed record
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SNAUTOTRK005'" );
      lMapTaskSched.put( "PART_NO_OEM", "'A0000002'" );
      lMapTaskSched.put( "MANUFACT_CD", "'11111'" );
      lMapTaskSched.put( "TASK_CD", "'MP-TRK-REQ'" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      lMapTaskSched.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTaskSched.put( "COMPLETION_VALUE", "0" );
      lMapTaskSched.put( "CUSTOM_START_VALUE", "null" );
      lMapTaskSched.put( "CUSTOM_DUE_VALUE", "null" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks_MISSINGTASK();

      // validate no error (except for waring)
      checkTaskValidation_EXCEPTWARNING( "PASS" );

      simpleIDs lInvIds = getInvIDs( "SNAUTOTRK001" );
      String lQuery = "select 1 from " + TableUtil.AL_MISSING_TASK + "  where INV_NO_DB_ID='"
            + lInvIds.getNO_DB_ID() + "' and " + "INV_NO_ID='" + lInvIds.getNO_ID() + "'";
      Assert.assertFalse( "Check missing task table should not include REQ2 task on SNAUTOTRK001. ",
            RecordsExist( lQuery ) );

      simpleIDs lTask_Ids = getTaskIDs( "ATTRKT3" );
      lQuery = "select 1 from " + TableUtil.AL_MISSING_TASK + "  where TASK_DB_ID='"
            + lTask_Ids.getNO_DB_ID() + "' and TASK_ID='" + lTask_Ids.getNO_ID() + "'";
      Assert.assertTrue(
            "Check missing task table should not include ATTRKT3 task on SNAUTOTRK001. ",
            RecordsExist( lQuery ) );

   }


   /*
    * OPER-28040: Actuals Loader missing tasks report is not accounting for part-based tasks that
    * are already staged in c_ri_task. Only part based and config slot task tasks will be listed in
    * missing task table when task is part based task on loose inventory
    */

   @Test
   public void testOPER_28040_4() {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000018'" );
      lMapTask.put( "PART_NO_OEM", "'A0000001'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'ATTRKT2'" );
      lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      // lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      // lMapTask.put( "COMPLETION_DUE_DT", "to_date('2015/01/02','yyyy/mm/dd')" );
      lMapTask.put( "CUSTOM_START_DT", "null" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();

      // first completed record
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SN000018'" );
      lMapTaskSched.put( "PART_NO_OEM", "'A0000001'" );
      lMapTaskSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskSched.put( "TASK_CD", "'ATTRKT2'" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
      lMapTaskSched.put( "CUSTOM_START_VALUE", "null" );
      lMapTaskSched.put( "CUSTOM_DUE_VALUE", "null" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks_MISSINGTASK();

      // validate no error (except for waring)
      checkTaskValidation_EXCEPTWARNING( "PASS" );

      simpleIDs lInvIds = getInvIDs( "SNAUTOTRK001" );
      String lQuery = "select 1 from " + TableUtil.AL_MISSING_TASK + "  where INV_NO_DB_ID='"
            + lInvIds.getNO_DB_ID() + "' and " + "INV_NO_ID='" + lInvIds.getNO_ID() + "'";
      Assert.assertFalse( "Check missing task table should not include REQ2 task on SNAUTOTRK001. ",
            RecordsExist( lQuery ) );

      simpleIDs lTask_Ids = getTaskIDs( "ATTRKT3" );
      lQuery = "select 1 from " + TableUtil.AL_MISSING_TASK + "  where TASK_DB_ID='"
            + lTask_Ids.getNO_DB_ID() + "' and TASK_ID='" + lTask_Ids.getNO_ID() + "'";
      Assert.assertTrue(
            "Check missing task table should not include ATTRKT3 task on SNAUTOTRK001. ",
            RecordsExist( lQuery ) );

   }


   /*
    * This test to verify GBL-00005: There are applicable tasks which have not been provided in
    * C_RI_TASK. These tasks have been saved to the AL_MISSING_TASK table. They can be loaded
    * automatically by setting the property in the runtime.properties file, or you can leave them to
    * be created by the Maintenix Baseline Synchronization job.
    */
   @Test
   public void test_GBL_00005_VALIDATION() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'SYS-REQ-LEADER'" );
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
      lMapTaskSched.put( "TASK_CD", "'SYS-REQ-LEADER'" );
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
      checkTaskValidation_WITHWARNING( "GBL-00005" );
   }

   // ================================================================


   // get the data based on the input data for specific columns
   protected ResultSet getData( String lSerial_No_OEM, String lPart_No_OEM, String lManufact_Cd,
         String lTask_Cd, String aTable ) throws SQLException {

      StringBuilder lTaskQuery = new StringBuilder();
      lTaskQuery.append( "SELECT * FROM " );
      lTaskQuery.append( aTable );
      lTaskQuery.append( " WHERE " );
      lTaskQuery.append( "serial_no_oem = '" + lSerial_No_OEM + "'" );
      lTaskQuery.append( " AND " );
      lTaskQuery.append( "part_no_oem = '" + lPart_No_OEM + "'" );
      lTaskQuery.append( " AND " );
      lTaskQuery.append( "manufact_cd = '" + lManufact_Cd + "'" );
      lTaskQuery.append( " AND " );
      lTaskQuery.append( "task_cd = '" + lTask_Cd + "'" );

      return runQuery( lTaskQuery.toString() );
   }


   /**
    * run the procedure to gather all missing tasks
    *
    * @throws SQLException
    *
    */
   private void extractMissingTasks() throws SQLException {

      CallableStatement lPrepareCall = getConnection().prepareCall(
            "BEGIN mx_al_report_pkg.extract_missing_task(aon_retcode =>?, aov_retmsg =>?); END;" );
      lPrepareCall.registerOutParameter( 1, Types.INTEGER );
      lPrepareCall.registerOutParameter( 2, Types.VARCHAR );
      lPrepareCall.execute();
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
   }

}
