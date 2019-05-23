package com.mxi.mx.core.query.plsql.schedstaskpkg;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.SqlLoader;
import com.mxi.am.db.connection.sql.SQLStatementFactory;
import com.mxi.am.db.connection.sql.WhereClause;


/**
 * This test class asserts that the function FindLatestTaskInstance within the SCHED_STASK_PKG
 * operates correctly.
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GenOneSchedTaskTest {

   @Rule
   public final DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public TestName testName = new TestName();


   @Before
   public void loadData() {

      if ( testName.getMethodName()
            .contains( "CreateForecastRecurringTaskWithActivePreviousTask" ) ) {
         SqlLoader.load( iDatabaseConnectionRule.getConnection(), GenOneSchedTaskTest.class,
               "GenOneteForecastRecurringTask.sql" );
      }

      if ( testName.getMethodName().contains( "CreateHistoricRecurringTaskWithoutPreviousTask" ) ) {
         SqlLoader.load( iDatabaseConnectionRule.getConnection(), GenOneSchedTaskTest.class,
               "GenOneHistoricRecurringTask.sql" );
      }

      if ( testName.getMethodName()
            .contains( "CreateNonHistoricForNonRecurringUniqueTaskWithActiveOneExists" ) ) {
         SqlLoader.load( iDatabaseConnectionRule.getConnection(), GenOneSchedTaskTest.class,
               "GenOneNonHistoricForNonRecurringUniqueTask.sql" );
      }

      if ( testName.getMethodName().contains( "testCreateActiveTaskWithCompletePreviousTask" ) ) {
         SqlLoader.load( iDatabaseConnectionRule.getConnection(), GenOneSchedTaskTest.class,
               "GenOneActiveTaskWithCompletePreviousTask.sql" );
      }

      if ( testName.getMethodName()
            .contains( "testCreateActiveRCRTaskWithCompletePreviousTaskID" ) ) {
         SqlLoader.load( iDatabaseConnectionRule.getConnection(), GenOneSchedTaskTest.class,
               "GenOneActiveTaskWithCompletePreviousTaskID.sql" );
      }

   }


   /**
    * <p>
    * Generate non-historical task with non-recurring and Unique setting, and previous active task
    * exists. Exception returned with error code -16 (icn_TaskAlreadyExist).
    * </p>
    */

   @Test
   public void testCreateNonHistoricForNonRecurringUniqueTaskWithActiveOneExists() {
      simpleIDs lInvIds = new simpleIDs( "4650", "1" );
      simpleIDs lTaskIds = new simpleIDs( "4650", "1" );
      simpleIDs lpretaskIDs = new simpleIDs( "-1", "-1" );
      int lan_reasondbid = 10;
      simpleIDs lhrids = new simpleIDs( "0", "3" );

      validationReturn lrtrn = runValidation( null, lInvIds, lTaskIds, lpretaskIDs, null,
            lan_reasondbid, "ReasonCD: Adjust",
            "USERNOTE:CreateNonHistoricForNonRecurringUniqueTaskWithActiveOneExists_ACFT", lhrids,
            true, false, true );

      Assert.assertTrue( "Exception icn_TaskAlreadyExist is raised as -16",
            lrtrn.getReturnCode() == -16 );

   }


   /**
    * <p>
    * Generate historical recurring task without previous task. Operation is successful with return
    * code=1 <b> sched_stask, evt_event, and evt_inv tables need to be verified with correct values
    * have been populated. </b>
    * </p>
    */

   @Test
   public void testCreateHistoricRecurringTaskWithoutPreviousTask() {
      simpleIDs lInvIds = new simpleIDs( "4650", "1" );
      simpleIDs lTaskIds = new simpleIDs( "4650", "1" );
      simpleIDs lpretaskIDs = new simpleIDs( "-1", "-1" );
      int lan_reasondbid = 10;
      simpleIDs lhrids = new simpleIDs( "0", "3" );

      validationReturn lrtrn = runValidation( null, lInvIds, lTaskIds, lpretaskIDs, null,
            lan_reasondbid, "ReasonCD: Adjust", "USERNOTE:ForcastRecurringWithoutPreTask_ACFT",
            lhrids, false, true, true );

      // validate sched_stask table
      String[] ischedIds =
            { "TASK_DB_ID", "TASK_ID", "TASK_CLASS_CD", "MAIN_INV_NO_DB_ID", "MAIN_INV_NO_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( ischedIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "SCHED_DB_ID", lrtrn.getSchedids().getNO_DB_ID() );
      lArgs.addArguments( "SCHED_ID", lrtrn.getSchedids().getNO_ID() );

      String iQueryString = SQLStatementFactory.buildTableQuery( "SCHED_STASK", lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );
      Assert.assertTrue( "TASK_DB_ID", llists.get( 0 ).get( 0 ).equalsIgnoreCase( "4650" ) );
      Assert.assertTrue( "TASK_ID", llists.get( 0 ).get( 1 ).equalsIgnoreCase( "1" ) );
      Assert.assertTrue( "TASK_CLASS_CD", llists.get( 0 ).get( 2 ).equalsIgnoreCase( "REQ" ) );
      Assert.assertTrue( "MAIN_INV_NO_DB_ID", llists.get( 0 ).get( 3 ).equalsIgnoreCase( "4650" ) );
      Assert.assertTrue( "MAIN_INV_NO_ID", llists.get( 0 ).get( 4 ).equalsIgnoreCase( "1" ) );

      // Validation on evt_event table
      lfields.clear();
      llists.clear();
      String[] ievtIds = { "EVENT_STATUS_CD", "EVENT_SDESC" };
      lfields = new ArrayList<String>( Arrays.asList( ievtIds ) );

      lArgs = new WhereClause();
      lArgs.addArguments( "EVENT_DB_ID", lrtrn.getSchedids().getNO_DB_ID() );
      lArgs.addArguments( "EVENT_ID", lrtrn.getSchedids().getNO_ID() );

      iQueryString = SQLStatementFactory.buildTableQuery( "EVT_EVENT", lfields, lArgs );
      llists = execute( iQueryString, lfields );
      Assert.assertTrue( "EVENT_STATUS_CD",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( "COMPLETE" ) );
      Assert.assertTrue( "EVENT_SDESC",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( "preTaskTest (preTaskTest)" ) );

      // validate evt_inv table
      lfields.clear();
      llists.clear();
      String[] ievtInvIds = { "ASSMBL_INV_NO_DB_ID", "ASSMBL_INV_NO_ID", "ASSMBL_DB_ID",
            "ASSMBL_CD", "ASSMBL_BOM_ID", "ASSMBL_POS_ID", "PART_NO_DB_ID", "PART_NO_ID",
            "BOM_PART_DB_ID", "BOM_PART_ID" };
      lfields = new ArrayList<String>( Arrays.asList( ievtInvIds ) );

      lArgs = new WhereClause();
      lArgs.addArguments( "EVENT_DB_ID", lrtrn.getSchedids().getNO_DB_ID() );
      lArgs.addArguments( "EVENT_ID", lrtrn.getSchedids().getNO_ID() );

      iQueryString = SQLStatementFactory.buildTableQuery( "EVT_INV", lfields, lArgs );
      llists = execute( iQueryString, lfields );
      Assert.assertTrue( "ASSMBL_INV_NO_DB_ID",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( "4650" ) );
      Assert.assertTrue( "ASSMBL_INV_NO_ID", llists.get( 0 ).get( 1 ).equalsIgnoreCase( "1" ) );
      Assert.assertTrue( "ASSMBL_DB_ID", llists.get( 0 ).get( 2 ).equalsIgnoreCase( "4650" ) );
      Assert.assertTrue( "ASSMBL_CD", llists.get( 0 ).get( 3 ).equalsIgnoreCase( "ACFT_CD1" ) );
      Assert.assertTrue( "ASSMBL_BOM_ID", llists.get( 0 ).get( 4 ).equalsIgnoreCase( "0" ) );
      Assert.assertTrue( "ASSMBL_POS_ID", llists.get( 0 ).get( 5 ).equalsIgnoreCase( "1" ) );
      Assert.assertTrue( "PART_NO_DB_ID", llists.get( 0 ).get( 6 ).equalsIgnoreCase( "4650" ) );
      Assert.assertTrue( "PART_NO_ID", llists.get( 0 ).get( 7 ).equalsIgnoreCase( "1" ) );
      Assert.assertTrue( "BOM_PART_DB_ID", llists.get( 0 ).get( 8 ).equalsIgnoreCase( "4650" ) );
      Assert.assertTrue( "BOM_PART_ID", llists.get( 0 ).get( 9 ).equalsIgnoreCase( "1" ) );

   }


   /**
    * <p>
    * Generate forecast recurring task with active previous task. Operation is successful with
    * return code=1 <b> sched_stask, evt_event, and evt_inv tables need to be verified with correct
    * values have been populated. </b>
    * </p>
    */

   @Test
   public void testCreateForecastRecurringTaskWithActivePreviousTask() {

      simpleIDs lInvIds = new simpleIDs( "4650", "1" );
      simpleIDs lTaskIds = new simpleIDs( "4650", "2" );
      simpleIDs lpretaskIDs = new simpleIDs( "-1", "-1" );
      int lan_reasondbid = 10;
      simpleIDs lhrids = new simpleIDs( "0", "3" );

      validationReturn lrtrn = runValidation( null, lInvIds, lTaskIds, lpretaskIDs, null,
            lan_reasondbid, "ReasonCD: Adjust",
            "USERNOTE:CreateForcastRecurringTaskWithActivePreviousTask_ACFT", lhrids, false, false,
            true );

      // validate sched_stask table
      String[] ischedIds =
            { "TASK_DB_ID", "TASK_ID", "TASK_CLASS_CD", "MAIN_INV_NO_DB_ID", "MAIN_INV_NO_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( ischedIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "SCHED_DB_ID", lrtrn.getSchedids().getNO_DB_ID() );
      lArgs.addArguments( "SCHED_ID", lrtrn.getSchedids().getNO_ID() );

      String iQueryString = SQLStatementFactory.buildTableQuery( "SCHED_STASK", lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );
      Assert.assertTrue( "TASK_DB_ID", llists.get( 0 ).get( 0 ).equalsIgnoreCase( "4650" ) );
      Assert.assertTrue( "TASK_ID", llists.get( 0 ).get( 1 ).equalsIgnoreCase( "2" ) );
      Assert.assertTrue( "TASK_CLASS_CD", llists.get( 0 ).get( 2 ).equalsIgnoreCase( "REQ" ) );
      Assert.assertTrue( "MAIN_INV_NO_DB_ID", llists.get( 0 ).get( 3 ).equalsIgnoreCase( "4650" ) );
      Assert.assertTrue( "MAIN_INV_NO_ID", llists.get( 0 ).get( 4 ).equalsIgnoreCase( "1" ) );

      // Validation on evt_event table
      lfields.clear();
      llists.clear();
      String[] ievtIds = { "EVENT_STATUS_CD", "EVENT_SDESC" };
      lfields = new ArrayList<String>( Arrays.asList( ievtIds ) );

      lArgs = new WhereClause();
      lArgs.addArguments( "EVENT_DB_ID", lrtrn.getSchedids().getNO_DB_ID() );
      lArgs.addArguments( "EVENT_ID", lrtrn.getSchedids().getNO_ID() );

      iQueryString = SQLStatementFactory.buildTableQuery( "EVT_EVENT", lfields, lArgs );
      llists = execute( iQueryString, lfields );
      Assert.assertTrue( "EVENT_STATUS_CD",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( "FORECAST" ) );
      Assert.assertTrue( "EVENT_SDESC",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( "TaskTest (TaskTest)" ) );

      // validate evt_inv table
      lfields.clear();
      llists.clear();
      String[] ievtInvIds = { "ASSMBL_INV_NO_DB_ID", "ASSMBL_INV_NO_ID", "ASSMBL_DB_ID",
            "ASSMBL_CD", "ASSMBL_BOM_ID", "ASSMBL_POS_ID", "PART_NO_DB_ID", "PART_NO_ID",
            "BOM_PART_DB_ID", "BOM_PART_ID" };
      lfields = new ArrayList<String>( Arrays.asList( ievtInvIds ) );

      lArgs = new WhereClause();
      lArgs.addArguments( "EVENT_DB_ID", lrtrn.getSchedids().getNO_DB_ID() );
      lArgs.addArguments( "EVENT_ID", lrtrn.getSchedids().getNO_ID() );

      iQueryString = SQLStatementFactory.buildTableQuery( "EVT_INV", lfields, lArgs );
      llists = execute( iQueryString, lfields );
      Assert.assertTrue( "ASSMBL_INV_NO_DB_ID",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( "4650" ) );
      Assert.assertTrue( "ASSMBL_INV_NO_ID", llists.get( 0 ).get( 1 ).equalsIgnoreCase( "1" ) );
      Assert.assertTrue( "ASSMBL_DB_ID", llists.get( 0 ).get( 2 ).equalsIgnoreCase( "4650" ) );
      Assert.assertTrue( "ASSMBL_CD", llists.get( 0 ).get( 3 ).equalsIgnoreCase( "ACFT_CD1" ) );
      Assert.assertTrue( "ASSMBL_BOM_ID", llists.get( 0 ).get( 4 ).equalsIgnoreCase( "0" ) );
      Assert.assertTrue( "ASSMBL_POS_ID", llists.get( 0 ).get( 5 ).equalsIgnoreCase( "1" ) );
      Assert.assertTrue( "PART_NO_DB_ID", llists.get( 0 ).get( 6 ).equalsIgnoreCase( "4650" ) );
      Assert.assertTrue( "PART_NO_ID", llists.get( 0 ).get( 7 ).equalsIgnoreCase( "1" ) );
      Assert.assertTrue( "BOM_PART_DB_ID", llists.get( 0 ).get( 8 ).equalsIgnoreCase( "4650" ) );
      Assert.assertTrue( "BOM_PART_ID", llists.get( 0 ).get( 9 ).equalsIgnoreCase( "1" ) );

   }


   /**
    * <p>
    * Generate active recurring task with complete previous task. Operation is successful with
    * return code=1 <b> sched_stask, evt_event, and evt_inv tables need to be verified with correct
    * values have been populated. </b>
    * </p>
    */

   @Test
   public void testCreateActiveTaskWithCompletePreviousTask() {

      simpleIDs lInvIds = new simpleIDs( "4650", "1" );
      simpleIDs lTaskIds = new simpleIDs( "4650", "1" );
      simpleIDs lpretaskIDs = new simpleIDs( "-1", "-1" );
      int lan_reasondbid = 10;
      simpleIDs lhrids = new simpleIDs( "0", "3" );

      validationReturn lrtrn = runValidation( null, lInvIds, lTaskIds, lpretaskIDs, null,
            lan_reasondbid, "ReasonCD: Adjust",
            "USERNOTE:testCreateActiveTaskWithCompletePreviousTask", lhrids, false, false, true );

      // validate sched_stask table
      String[] ischedIds = { "TASK_DB_ID", "TASK_ID", "TASK_CLASS_CD", "MAIN_INV_NO_DB_ID",
            "MAIN_INV_NO_ID", "HIST_BOOL_RO" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( ischedIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "SCHED_DB_ID", lrtrn.getSchedids().getNO_DB_ID() );
      lArgs.addArguments( "SCHED_ID", lrtrn.getSchedids().getNO_ID() );

      String iQueryString = SQLStatementFactory.buildTableQuery( "SCHED_STASK", lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );
      Assert.assertTrue( "TASK_DB_ID", llists.get( 0 ).get( 0 ).equalsIgnoreCase( "4650" ) );
      Assert.assertTrue( "TASK_ID", llists.get( 0 ).get( 1 ).equalsIgnoreCase( "1" ) );
      Assert.assertTrue( "TASK_CLASS_CD", llists.get( 0 ).get( 2 ).equalsIgnoreCase( "JIC" ) );
      Assert.assertTrue( "MAIN_INV_NO_DB_ID", llists.get( 0 ).get( 3 ).equalsIgnoreCase( "4650" ) );
      Assert.assertTrue( "MAIN_INV_NO_ID", llists.get( 0 ).get( 4 ).equalsIgnoreCase( "1" ) );
      Assert.assertTrue( "HIST_BOOL_RO", llists.get( 0 ).get( 5 ).equalsIgnoreCase( "0" ) );

      // Validation on evt_event table
      lfields.clear();
      llists.clear();
      String[] ievtIds = { "EVENT_STATUS_CD", "EVENT_SDESC" };
      lfields = new ArrayList<String>( Arrays.asList( ievtIds ) );

      lArgs = new WhereClause();
      lArgs.addArguments( "EVENT_DB_ID", lrtrn.getSchedids().getNO_DB_ID() );
      lArgs.addArguments( "EVENT_ID", lrtrn.getSchedids().getNO_ID() );

      iQueryString = SQLStatementFactory.buildTableQuery( "EVT_EVENT", lfields, lArgs );
      llists = execute( iQueryString, lfields );
      Assert.assertTrue( "EVENT_STATUS_CD", llists.get( 0 ).get( 0 ).equalsIgnoreCase( "ACTV" ) );
      Assert.assertTrue( "EVENT_SDESC",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( "JICTask (JICTask)" ) );

      // validate evt_inv table
      lfields.clear();
      llists.clear();
      String[] ievtInvIds = { "ASSMBL_INV_NO_DB_ID", "ASSMBL_INV_NO_ID", "ASSMBL_DB_ID",
            "ASSMBL_CD", "ASSMBL_BOM_ID", "ASSMBL_POS_ID", "PART_NO_DB_ID", "PART_NO_ID",
            "BOM_PART_DB_ID", "BOM_PART_ID" };
      lfields = new ArrayList<String>( Arrays.asList( ievtInvIds ) );

      lArgs = new WhereClause();
      lArgs.addArguments( "EVENT_DB_ID", lrtrn.getSchedids().getNO_DB_ID() );
      lArgs.addArguments( "EVENT_ID", lrtrn.getSchedids().getNO_ID() );

      iQueryString = SQLStatementFactory.buildTableQuery( "EVT_INV", lfields, lArgs );
      llists = execute( iQueryString, lfields );
      Assert.assertTrue( "ASSMBL_INV_NO_DB_ID",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( "4650" ) );
      Assert.assertTrue( "ASSMBL_INV_NO_ID", llists.get( 0 ).get( 1 ).equalsIgnoreCase( "1" ) );
      Assert.assertTrue( "ASSMBL_DB_ID", llists.get( 0 ).get( 2 ).equalsIgnoreCase( "4650" ) );
      Assert.assertTrue( "ASSMBL_CD", llists.get( 0 ).get( 3 ).equalsIgnoreCase( "ACFT_CD1" ) );
      Assert.assertTrue( "ASSMBL_BOM_ID", llists.get( 0 ).get( 4 ).equalsIgnoreCase( "0" ) );
      Assert.assertTrue( "ASSMBL_POS_ID", llists.get( 0 ).get( 5 ).equalsIgnoreCase( "1" ) );
      Assert.assertTrue( "PART_NO_DB_ID", llists.get( 0 ).get( 6 ).equalsIgnoreCase( "4650" ) );
      Assert.assertTrue( "PART_NO_ID", llists.get( 0 ).get( 7 ).equalsIgnoreCase( "1" ) );
      Assert.assertTrue( "BOM_PART_DB_ID", llists.get( 0 ).get( 8 ).equalsIgnoreCase( "4650" ) );
      Assert.assertTrue( "BOM_PART_ID", llists.get( 0 ).get( 9 ).equalsIgnoreCase( "1" ) );

   }


   /**
    * <p>
    * Generate active recurring task with complete previous task with different task ID. Operation
    * is successful with return code=1 <b> sched_stask, evt_event, and evt_inv tables need to be
    * verified with correct values have been populated. </b>
    * </p>
    */
   @Test
   public void testCreateActiveRCRTaskWithCompletePreviousTaskID() {

      simpleIDs lInvIds = new simpleIDs( "4650", "1" );
      simpleIDs lTaskIds = new simpleIDs( "4650", "1" );
      simpleIDs lpretaskIDs = new simpleIDs( "4650", "100006" );
      int lan_reasondbid = 10;
      simpleIDs lhrids = new simpleIDs( "0", "3" );

      validationReturn lrtrn = runValidation( null, lInvIds, lTaskIds, lpretaskIDs, null,
            lan_reasondbid, "ReasonCD: Adjust",
            "USERNOTE:CreateForcastRecurringTaskWithActivePreviousTask_ACFT", lhrids, false, false,
            true );

      // validate sched_stask table
      String[] ischedIds = { "TASK_DB_ID", "TASK_ID", "TASK_CLASS_CD", "MAIN_INV_NO_DB_ID",
            "MAIN_INV_NO_ID", "HIST_BOOL_RO" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( ischedIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "SCHED_DB_ID", lrtrn.getSchedids().getNO_DB_ID() );
      lArgs.addArguments( "SCHED_ID", lrtrn.getSchedids().getNO_ID() );

      String iQueryString = SQLStatementFactory.buildTableQuery( "SCHED_STASK", lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );
      Assert.assertTrue( "TASK_DB_ID", llists.get( 0 ).get( 0 ).equalsIgnoreCase( "4650" ) );
      Assert.assertTrue( "TASK_ID", llists.get( 0 ).get( 1 ).equalsIgnoreCase( "1" ) );
      Assert.assertTrue( "TASK_CLASS_CD", llists.get( 0 ).get( 2 ).equalsIgnoreCase( "JIC" ) );
      Assert.assertTrue( "MAIN_INV_NO_DB_ID", llists.get( 0 ).get( 3 ).equalsIgnoreCase( "4650" ) );
      Assert.assertTrue( "MAIN_INV_NO_ID", llists.get( 0 ).get( 4 ).equalsIgnoreCase( "1" ) );
      Assert.assertTrue( "HIST_BOOL_RO", llists.get( 0 ).get( 5 ).equalsIgnoreCase( "0" ) );

      // Validation on evt_event table
      lfields.clear();
      llists.clear();
      String[] ievtIds = { "EVENT_STATUS_CD", "EVENT_SDESC" };
      lfields = new ArrayList<String>( Arrays.asList( ievtIds ) );

      lArgs = new WhereClause();
      lArgs.addArguments( "EVENT_DB_ID", lrtrn.getSchedids().getNO_DB_ID() );
      lArgs.addArguments( "EVENT_ID", lrtrn.getSchedids().getNO_ID() );

      iQueryString = SQLStatementFactory.buildTableQuery( "EVT_EVENT", lfields, lArgs );
      llists = execute( iQueryString, lfields );
      Assert.assertTrue( "EVENT_STATUS_CD", llists.get( 0 ).get( 0 ).equalsIgnoreCase( "ACTV" ) );
      Assert.assertTrue( "EVENT_SDESC",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( "JICTask (JICTask)" ) );

      // validate evt_inv table
      lfields.clear();
      llists.clear();
      String[] ievtInvIds = { "ASSMBL_INV_NO_DB_ID", "ASSMBL_INV_NO_ID", "ASSMBL_DB_ID",
            "ASSMBL_CD", "ASSMBL_BOM_ID", "ASSMBL_POS_ID", "PART_NO_DB_ID", "PART_NO_ID",
            "BOM_PART_DB_ID", "BOM_PART_ID" };
      lfields = new ArrayList<String>( Arrays.asList( ievtInvIds ) );

      lArgs = new WhereClause();
      lArgs.addArguments( "EVENT_DB_ID", lrtrn.getSchedids().getNO_DB_ID() );
      lArgs.addArguments( "EVENT_ID", lrtrn.getSchedids().getNO_ID() );

      iQueryString = SQLStatementFactory.buildTableQuery( "EVT_INV", lfields, lArgs );
      llists = execute( iQueryString, lfields );
      Assert.assertTrue( "ASSMBL_INV_NO_DB_ID",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( "4650" ) );
      Assert.assertTrue( "ASSMBL_INV_NO_ID", llists.get( 0 ).get( 1 ).equalsIgnoreCase( "1" ) );
      Assert.assertTrue( "ASSMBL_DB_ID", llists.get( 0 ).get( 2 ).equalsIgnoreCase( "4650" ) );
      Assert.assertTrue( "ASSMBL_CD", llists.get( 0 ).get( 3 ).equalsIgnoreCase( "ACFT_CD1" ) );
      Assert.assertTrue( "ASSMBL_BOM_ID", llists.get( 0 ).get( 4 ).equalsIgnoreCase( "0" ) );
      Assert.assertTrue( "ASSMBL_POS_ID", llists.get( 0 ).get( 5 ).equalsIgnoreCase( "1" ) );
      Assert.assertTrue( "PART_NO_DB_ID", llists.get( 0 ).get( 6 ).equalsIgnoreCase( "4650" ) );
      Assert.assertTrue( "PART_NO_ID", llists.get( 0 ).get( 7 ).equalsIgnoreCase( "1" ) );
      Assert.assertTrue( "BOM_PART_DB_ID", llists.get( 0 ).get( 8 ).equalsIgnoreCase( "4650" ) );
      Assert.assertTrue( "BOM_PART_ID", llists.get( 0 ).get( 9 ).equalsIgnoreCase( "1" ) );

   }


   /**
    * Execute the query.
    */
   private List<ArrayList<String>> execute( String aStrQuery, List<String> lfields ) {

      PreparedStatement lStatement;
      List<ArrayList<String>> louter = new ArrayList<ArrayList<String>>();

      try {
         lStatement = iDatabaseConnectionRule.getConnection().prepareStatement( aStrQuery,
               ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY );

         ResultSet lResultSet = lStatement.executeQuery( aStrQuery );
         while ( lResultSet.next() ) {
            List<String> iList = new ArrayList<String>();
            for ( int i = 0; i < lfields.size(); i++ ) {
               iList.add( lResultSet.getString( lfields.get( i ) ) );

            }
            louter.add( ( ArrayList<String> ) iList );

         }

      } catch ( SQLException e ) {
         e.printStackTrace();
      }

      return louter;

   }


   /**
    * This function is to run store procedure sched_stask_pkg.genoneschedtask.
    */

   public validationReturn runValidation( simpleIDs eventIDs, simpleIDs invIDs, simpleIDs taskIDs,
         simpleIDs pretaskIDs, Date sqlDate, int an_reasondbid, String an_reasoncd,
         String as_usernote, simpleIDs hrIDs, boolean ab_calledexternally, boolean ab_historic,
         boolean ab_createnatask ) {

      CallableStatement lPrepareCallGenSchedTask;
      validationReturn lReturn = null;
      try {

         // Build CallableStatement String
         StringBuilder strCall = new StringBuilder();
         strCall.append( "BEGIN sched_stask_pkg.genoneschedtask(an_evteventdbid => ?, " )
               .append( "an_evteventid => ?, " + "an_invnodbid => ?, " )
               .append( "an_invnoid => ?, " + "an_taskdbid => ?, " + "an_taskid => ?, " )
               .append( "an_previoustaskdbid => ?, " + "an_previoustaskid => ?, " )
               .append( "ad_completiondate => ?, " + "an_reasondbid => ?, " )
               .append( "an_reasoncd => ?, " + "as_usernote => ?, " + "an_hrdbid => ?, " )
               .append( "an_hrid => ?, " );// + "ab_calledexternally => false, " );

         if ( ab_calledexternally == true ) {
            strCall.append( "ab_calledexternally => true, " );

         } else {
            strCall.append( "ab_calledexternally => false, " );

         }

         if ( ab_historic == true ) {
            strCall.append( "ab_historic => true, " );

         } else {
            strCall.append( "ab_historic => false, " );

         }

         if ( ab_createnatask == true ) {
            strCall.append( "ab_createnatask => true, " );

         } else {
            strCall.append( "ab_createnatask => false, " );

         }

         strCall.append( "on_scheddbid => ?,  on_schedid => ?, " )
               .append( "on_return => ?); End;" );

         // prepare CallableStatement
         lPrepareCallGenSchedTask =
               iDatabaseConnectionRule.getConnection().prepareCall( strCall.toString() );

         // Provide parameters
         if ( eventIDs != null ) {
            lPrepareCallGenSchedTask.setInt( 1, Integer.parseInt( eventIDs.getNO_DB_ID() ) );
            lPrepareCallGenSchedTask.setInt( 2, Integer.parseInt( eventIDs.getNO_ID() ) );
         } else {
            lPrepareCallGenSchedTask.setNull( 1, Types.NULL );
            lPrepareCallGenSchedTask.setNull( 2, Types.NULL );
         }

         if ( invIDs != null ) {
            lPrepareCallGenSchedTask.setInt( 3, Integer.parseInt( invIDs.getNO_DB_ID() ) );
            lPrepareCallGenSchedTask.setInt( 4, Integer.parseInt( invIDs.getNO_ID() ) );
         } else {
            lPrepareCallGenSchedTask.setNull( 3, Types.INTEGER );
            lPrepareCallGenSchedTask.setNull( 4, Types.INTEGER );
         }

         if ( taskIDs != null ) {
            lPrepareCallGenSchedTask.setInt( 5, Integer.parseInt( taskIDs.getNO_DB_ID() ) );
            lPrepareCallGenSchedTask.setInt( 6, Integer.parseInt( taskIDs.getNO_ID() ) );
         } else {
            lPrepareCallGenSchedTask.setNull( 5, Types.INTEGER );
            lPrepareCallGenSchedTask.setNull( 6, Types.INTEGER );

         }

         if ( pretaskIDs != null ) {

            lPrepareCallGenSchedTask.setInt( 7, Integer.parseInt( pretaskIDs.getNO_DB_ID() ) );
            lPrepareCallGenSchedTask.setInt( 8, Integer.parseInt( pretaskIDs.getNO_ID() ) );
         } else {
            lPrepareCallGenSchedTask.setNull( 7, Types.INTEGER );
            lPrepareCallGenSchedTask.setNull( 8, Types.INTEGER );

         }

         if ( sqlDate != null ) {
            lPrepareCallGenSchedTask.setDate( 9, sqlDate );
         } else {
            lPrepareCallGenSchedTask.setNull( 9, Types.NULL );

         }

         lPrepareCallGenSchedTask.setInt( 10, an_reasondbid );
         lPrepareCallGenSchedTask.setString( 11, an_reasoncd );
         lPrepareCallGenSchedTask.setString( 12, as_usernote );

         if ( hrIDs != null ) {

            lPrepareCallGenSchedTask.setInt( 13, Integer.parseInt( hrIDs.getNO_DB_ID() ) );
            lPrepareCallGenSchedTask.setInt( 14, Integer.parseInt( hrIDs.getNO_ID() ) );
         } else {
            lPrepareCallGenSchedTask.setNull( 13, Types.INTEGER );
            lPrepareCallGenSchedTask.setNull( 14, Types.INTEGER );

         }

         lPrepareCallGenSchedTask.registerOutParameter( 15, Types.INTEGER );
         lPrepareCallGenSchedTask.registerOutParameter( 16, Types.INTEGER );
         lPrepareCallGenSchedTask.registerOutParameter( 17, Types.INTEGER );

         // Execute CallableStatement
         lPrepareCallGenSchedTask.execute();
         // commit();

         lReturn = new validationReturn( lPrepareCallGenSchedTask.getInt( 15 ),
               lPrepareCallGenSchedTask.getInt( 16 ), lPrepareCallGenSchedTask.getInt( 17 ) );

      } catch ( SQLException e ) {

         e.printStackTrace();
      }

      return lReturn;

   }


   // Return class for CallableStatement execution
   class validationReturn {

      simpleIDs schedids;
      int returnCode;


      public validationReturn(int scheddbid, int schedid, int returnCode) {
         this.schedids =
               new simpleIDs( Integer.toString( scheddbid ), Integer.toString( schedid ) );
         this.returnCode = returnCode;

      }


      /**
       * Returns the value of the schedids property.
       *
       * @return the value of the schedids property
       */
      public simpleIDs getSchedids() {
         return schedids;
      }


      /**
       * Sets a new value for the schedids property.
       *
       * @param aSchedids
       *           the new value for the schedids property
       */
      public void setSchedids( simpleIDs aSchedids ) {
         schedids = aSchedids;
      }


      /**
       * Returns the value of the returnCode property.
       *
       * @return the value of the returnCode property
       */
      public int getReturnCode() {
         return returnCode;
      }


      /**
       * Sets a new value for the returnCode property.
       *
       * @param aReturnCode
       *           the new value for the returnCode property
       */
      public void setReturnCode( int aReturnCode ) {
         returnCode = aReturnCode;
      }

   }

   class simpleIDs {

      String NO_DB_ID;
      String NO_ID;


      public simpleIDs(String NO_DB_ID, String NO_ID) {
         this.NO_DB_ID = NO_DB_ID;
         this.NO_ID = NO_ID;
      }


      /**
       * Returns the value of the nO_DB_ID property.
       *
       * @return the value of the nO_DB_ID property
       */
      public String getNO_DB_ID() {
         return NO_DB_ID;
      }


      /**
       * Sets a new value for the nO_DB_ID property.
       *
       * @param aNO_DB_ID
       *           the new value for the nO_DB_ID property
       */
      public void setNO_DB_ID( String aNO_DB_ID ) {
         NO_DB_ID = aNO_DB_ID;
      }


      /**
       * Returns the value of the nO_ID property.
       *
       * @return the value of the nO_ID property
       */
      public String getNO_ID() {
         return NO_ID;
      }


      /**
       * Sets a new value for the nO_ID property.
       *
       * @param aNO_ID
       *           the new value for the nO_ID property
       */
      public void setNO_ID( String aNO_ID ) {
         NO_ID = aNO_ID;
      }


      /**
       * {@inheritDoc}
       */
      @Override
      public int hashCode() {
         final int prime = 31;
         int result = 1;
         result = prime * result + ( ( NO_DB_ID == null ) ? 0 : NO_DB_ID.hashCode() );
         result = prime * result + ( ( NO_ID == null ) ? 0 : NO_ID.hashCode() );
         return result;
      }


      /**
       * {@inheritDoc}
       */
      @Override
      public boolean equals( Object obj ) {
         if ( this == obj )
            return true;
         if ( obj == null )
            return false;
         if ( getClass() != obj.getClass() )
            return false;
         simpleIDs other = ( simpleIDs ) obj;
         if ( NO_DB_ID == null ) {
            if ( other.NO_DB_ID != null )
               return false;
         } else if ( !NO_DB_ID.equalsIgnoreCase( other.NO_DB_ID ) )
            return false;
         if ( NO_ID == null ) {
            if ( other.NO_ID != null )
               return false;
         } else if ( !NO_ID.equalsIgnoreCase( other.NO_ID ) )
            return false;
         return true;
      }
   }

}
