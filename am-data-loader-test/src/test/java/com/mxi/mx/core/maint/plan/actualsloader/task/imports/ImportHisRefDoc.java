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
 * This test suite contains test cases on validation and import functionality of
 * mx_al_ctrller_pkg.execute_task_import on historial ref doc
 *
 * @author ALICIA QIAN
 */
public class ImportHisRefDoc extends ActualsLoaderTest {

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

   public static String iREF_Task_CD = "ATREFT1";
   public static String iREF_Task_NAME = "ATREFT1NAME";

   public static String iSN = "SN000001";

   public static String iSTAGE_NOTE_SYSTEM = "Created by BULK data loading utility. ";


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
    * This function is to prepare data for ref doc task import when completion_dt is provided.
    *
    * @throws Exception
    *
    *
    */
   @Test
   public void testREF_COMPLETION_VALIDATION() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'" + iREF_Task_CD + "'" );
      // lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      lMapTask.put( "COMPLETION_DUE_DT", "to_date('2015/01/02','yyyy/mm/dd')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // run validation
      runValidateTasks();

      // call express validation runner and result checker with expected error code
      checkTaskValidation_EXCEPTWARNING( "PASS" );
   }


   /**
    * This function is to verify ref doc task import when completion_dt is provided.
    *
    * @throws Exception
    *
    *
    */
   @Test
   public void testREF_COMPLETION_IMPORT() throws Exception {

      iEventIDs_1 = null;
      iEventIDs_2 = null;
      iEventIDs_3 = null;
      iTaskIDs_1 = null;

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testREF_COMPLETION_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runImportTasks();

      // Get event IDs
      iEventIDs_1 = getEventIDs( TestConstants.STATUS_COMPLETE, iREF_Task_CD, iREF_Task_NAME );

      // Validate evt_event
      Assert.assertTrue( "check event id 1 is not null", iEventIDs_1 != null );

      // Get task ID
      iTaskIDs_1 = getTaskIDs( iREF_Task_CD );
      // get inventory IDs
      simpleIDs iInvIDs_ACFT = getInvIDs( iSN );

      // verify sched_task table
      VerifySchedTask( iEventIDs_1, null, iTaskIDs_1, TestConstants.TASK_CLASS_CD_AMP,
            iInvIDs_ACFT );

      // verify evt_inv
      String lQuery = "select 1 from " + TableUtil.EVT_INV + " where EVENT_DB_ID="
            + iEventIDs_1.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_1.getNO_ID();
      Assert.assertTrue( "Check evt_inv table to verify the record exists: 1",
            RecordsExist( lQuery ) );

      // verify evt_stage
      VerifyEvtStage( iEventIDs_1, TestConstants.STATUS_COMPLETE, iSTAGE_NOTE_SYSTEM, "1" );

   }


   /**
    * This function is to prepare data for ref doc task import when not applicable bool is provided.
    *
    * @throws Exception
    *
    *
    */
   @Test
   public void testREF_APP_VALIDATION() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SN000001'" );
      lMapTask.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'" + iREF_Task_CD + "'" );
      // lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "NOT_APPLICABLE_BOOL", "'Y'" );
      // lMapTask.put( "COMPLETION_DT", "to_date('2015/01/01','yyyy/mm/dd')" );
      // lMapTask.put( "COMPLETION_DUE_DT", "to_date('2015/01/02','yyyy/mm/dd')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // run validation
      runValidateTasks();

      // call express validation runner and result checker with expected error code
      checkTaskValidation_EXCEPTWARNING( "PASS" );
   }


   /**
    * This function is to verify ref doc task import when not applicable bool is provided.
    *
    * @throws Exception
    *
    *
    */
   @Test
   public void testREF_APP_IMPORT() throws Exception {

      iEventIDs_1 = null;
      iEventIDs_2 = null;
      iEventIDs_3 = null;
      iTaskIDs_1 = null;

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testREF_APP_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runImportTasks();

      // Get event IDs
      iEventIDs_1 = getEventIDs( TestConstants.STATUS_NA, iREF_Task_CD, iREF_Task_NAME );

      // Validate evt_event
      Assert.assertTrue( "check event id 1 is not null", iEventIDs_1 != null );

      // Get task ID
      iTaskIDs_1 = getTaskIDs( iREF_Task_CD );
      // get inventory IDs
      simpleIDs iInvIDs_ACFT = getInvIDs( iSN );

      // verify sched_task table
      VerifySchedTask( iEventIDs_1, null, iTaskIDs_1, TestConstants.TASK_CLASS_CD_AMP,
            iInvIDs_ACFT );

      // verify evt_inv
      String lQuery = "select 1 from " + TableUtil.EVT_INV + " where EVENT_DB_ID="
            + iEventIDs_1.getNO_DB_ID() + " and EVENT_ID=" + iEventIDs_1.getNO_ID();
      Assert.assertTrue( "Check evt_inv table to verify the record exists: 1",
            RecordsExist( lQuery ) );

      // verify evt_stage
      VerifyEvtStage( iEventIDs_1, TestConstants.STATUS_NA, iSTAGE_NOTE_SYSTEM, "0" );

   }


   // =====================================================================================================

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
   }
}
