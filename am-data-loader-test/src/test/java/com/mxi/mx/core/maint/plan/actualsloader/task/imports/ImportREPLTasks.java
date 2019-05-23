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

import com.mxi.mx.core.maint.plan.datamodels.AssmblBomID;
import com.mxi.mx.core.maint.plan.datamodels.bomPartPN;
import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;
import com.mxi.mx.util.ActualsLoaderTest;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.TestConstants;
import com.mxi.mx.util.ValidationAndImport;
import com.mxi.mx.util.WhereClause;


/**
 * This test suite contains test cases on validation and import functionality of
 * mx_al_ctrller_pkg.execute_task_import on REPL tasks.
 *
 * @author ALICIA QIAN
 */
public class ImportREPLTasks extends ActualsLoaderTest {

   @Rule
   public TestName testName = new TestName();
   ValidationAndImport ivalidationandimport;

   public List<ArrayList<String>> iTask_resume_LRU_NOREPL_List = null;
   public List<ArrayList<String>> iTask_resume_SRU_NOREPL_OFFWING_List = null;
   public List<ArrayList<String>> iTask_resume_SRU_NOREPL_OFFPARENT_List = null;
   public List<simpleIDs> iTaskIDs = null;
   public List<simpleIDs> iLabourIDs = null;
   public List<simpleIDs> iLabourRoleListIDs = null;
   public String iTask_OFFWING_NOREPL = "REPL_TEST1";
   public String iTask_OFFPARENT_NOREPL = "REPL_TEST2";
   public String iTask_LRU_REPL = "MP-TRK-REQ";
   public String iTask_OFFWING_NOREPL_SRU = "REPL_TEST4";
   public String iTask_OFFPARENT_NOREPL_SRU = "REPL_TEST3";
   public String iTask_OFFWING_NOLRU = "REPL_TEST5";
   public String iTask_OFFPARENT_NOLRU_NOREPL = "REPL_TEST6";
   public String iTask_OFFPARENT_NOLRU_REPL = "REPL_TEST8";
   public String iEVENT_TYPE_CD_TS = "TS";
   public String iEVENT_TYPE_CD_PR = "PR";

   public simpleIDs iEventIDs_OFFWING_OFFPARENT = null;
   public simpleIDs iEventIDs_REPL_1 = null;
   public simpleIDs iEventIDs_REPL_2 = null;
   public simpleIDs iEventIDs_WKP = null;
   public simpleIDs iEventIDs_PART_REQ = null;
   public simpleIDs iEventIDs_PART_REQ_2 = null;
   public simpleIDs iLabourRoleIds = null;
   public simpleIDs iLabourRoleIds2 = null;

   public String iEVENT_SDESC_1 = "REPL_TEST1 (REPL_TEST_OFFWING)".toUpperCase();
   public String iEVENT_SDESC_2 =
         "Replace TRK-on-TRK-on-TRK Parent (PN: A0000020, SN: SNAUTOTRK001)".toUpperCase();
   public String iEVENT_SDESC_3 = "REPL_TEST2 (REPL_TEST_OFFPARENT)".toUpperCase();
   public String iEVENT_SDESC_4 =
         "MP-TRK-REQ (Multi-Position Tracked Config Slot Requirement)".toUpperCase();
   public String iEVENT_SDESC_5 =
         "ACFT-SYS-1-1-TRK-P2-REPL (2 Position Tracked Part-REPLACEMENT)".toUpperCase();
   public String iEVENT_SDESC_6 = "REPL_TEST4 (REPL_TEST_OFFWING4)".toUpperCase();
   public String iEVENT_SDESC_7 = "REPL_TEST6 (REPL_TEST_OFFPARENT6)".toUpperCase();
   public String iEVENT_SDESC_8 =
         "ENG-SYS-1-1-TRK-TRK-CHILD-REPL (TRK-on-TRK Child-REPLACEMENT)".toUpperCase();
   public String iEVENT_SDESC_9 = "REPL_TEST5 (REPL_TEST_OFFWING5)".toUpperCase();
   public String iEVENT_SDESC_10 = "REPL_TEST3 (REPL_TEST_OFFPARENT3)".toUpperCase();
   public String iEVENT_SDESC_11 = "REPL_CHILD (REPL_CHILD_TEST)".toUpperCase();
   public String iEVENT_SDESC_12 = "REPL_MID (REPL_MID_TEST)".toUpperCase();
   public String iEVENT_SDESC_13 = "REPL_MID (REPL_MID_TEST)".toUpperCase();
   public String iEVENT_SDESC_14 =
         "Replace TRK-on-TRK-on-TRK Child (PN: A0000022, SN: SNAUTOTRK006)".toUpperCase();
   public String iEVENT_SDESC_15 = "REPL_TEST3 (REPL_TEST_OFFPARENT3)".toUpperCase();
   public String iEVENT_SDESC_16 = "ENG-ASSY-REPL (Engine Sub-Assembly-REPLACEMENT)".toUpperCase();
   public String iEVENT_SDESC_17 =
         "ENG-SYS-1-1-TRK-TRK-CHILD-REPL (TRK-on-TRK Child-REPLACEMENT)".toUpperCase();
   public String iEVENT_SDESC_18 =
         "Repair (1) 2 Position Tracked Part (PN: A0000002, SN: SNAUTOTRK005)".toUpperCase();
   public String iEVENT_SDESC_19 =
         "Replace TRK-on-TRK-on-TRK Middle (PN: A0000021, SN: XXX)".toUpperCase();
   public String iEVENT_SDESC_20 =
         "Repair (2) Part Name - Engine (PN: ENG_ASSY_PN1, SN: SN000008)".toUpperCase();
   public String iEVENT_SDESC_21 =
         "Repair TRK-on-TRK-on-TRK Parent (PN: A0000020, SN: SNAUTOTRK001)".toUpperCase();
   public String iEVENT_SDESC_22 =
         "Repair Part Name - Engine (PN: ENG_ASSY_PN1, SN: SN000207)".toUpperCase();

   public String iExisting_WPK = "AUTOTRK_WKPKG1";
   public String iASSMBL_CD = "ACFT_CD1";
   public String iASSMBL_CD_ENG = "ENG_CD1";
   public String iASSMBL_BOM_CD_SYS = "SYS-1-1";
   public String iASSMBL_BOM_CD_TRK_PARENT = "ACFT-SYS-1-1-TRK-TRK-TRK-PARENT";
   public String iASSMBL_BOM_CD_TRK_P2 = "ACFT-SYS-1-1-TRK-P2";
   public String iASSMBL_BOM_CD_TRK_P3 = "ACFT-SYS-1-1-TRK-TRK-TRK-MID";
   public String iASSMBL_BOM_CD_TRK_P4 = "ACFT-SYS-1-1-TRK-TRK-TRK-CHILD";
   public String iASSMBL_BOM_CD_ENG_TRK_P1 = "ENG-SYS-1-1-TRK-TRK-PARENT";
   public String iASSMBL_BOM_CD_ENG_TRK_P2 = "ENG-SYS-1-1-TRK-TRK-CHILD";


   /**
    * Clean up after each individual test
    *
    * @throws Exception
    */
   @Override
   @After
   public void after() throws Exception {

      String strTCName = testName.getMethodName();
      // clean up the event data
      RestoreData();
      if ( strTCName.contains( "OFFWING_LRU_REPL" ) ) {
         dataSetup_RESTORE_LRU_REPL( "NA" );

      } else if ( strTCName.contains( "OFFPARENT_LRU_REPL" ) ) {
         dataSetup_RESTORE_LRU_REPL( "NA" );

      } else if ( strTCName.contains( "testOFFWING_NOLRU_REPL" ) ) {
         dataSetup_NOLRU( "1" );
      } else if ( strTCName.contains( "testOFFPARENT_NOLRU_REPL" ) ) {
         dataSetup_NOLRU( "1" );
      }

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
      setupDataBase();
      emptyList();

      String strTCName = testName.getMethodName();
      if ( strTCName.contains( "LRU_NOREPL_WITHPKG" ) ) {
         dataSetup_LRU_NOREPL();
      } else if ( strTCName.contains( "LRU_NOREPL_NOPKG" ) ) {
         dataSetup_LRU_NOREPL();
         updateWKPStatus();
      } else if ( strTCName.contains( "OFFWING_LRU_REPL" ) ) {
         dataSetup_RESTORE_LRU_REPL( "OFFWING" );

      } else if ( strTCName.contains( "OFFPARENT_LRU_REPL" ) ) {
         dataSetup_RESTORE_LRU_REPL( "OFFPARENT" );

      } else if ( strTCName.contains( "OFFWING_SRU_NOREPL" ) ) {
         dataSetup_SRU_NOREPL_OFFWING();
      } else if ( strTCName.contains( "OFFPARENT_SRU_NOREPL" ) ) {
         dataSetup_SRU_NOREPL_OFFWING();
         dataSetup_SRU_NOREPL_OFFPARENT();

      } else if ( strTCName.contains( "testOFFWING_NOLRU_REPL" ) ) {
         dataSetup_NOLRU( "0" );
      } else if ( strTCName.contains( "testOFFPARENT_NOLRU_REPL" ) ) {
         dataSetup_NOLRU( "0" );
      }

   }


   /**
    * This test is to test offwing task on LRU config slot without REPL task defined on the parent
    * of LRU (1.1, 1.2), there is existing wkp.
    *
    * @throws Exception
    *
    *
    */
   public void testOFFWING_LRU_NOREPL_VALIDATION() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SNAUTOTRK001'" );
      lMapTask.put( "PART_NO_OEM", "'A0000020'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'" + iTask_OFFWING_NOREPL + "'" );
      lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "CUSTOM_START_DT", "null" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();

      // first completed record
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SNAUTOTRK001'" );
      lMapTaskSched.put( "PART_NO_OEM", "'A0000020'" );
      lMapTaskSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskSched.put( "TASK_CD", "'" + iTask_OFFWING_NOREPL + "'" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // call express validation runner and result checker with expected error code
      checkTaskValidation_EXCEPTWARNING( "PASS" );
   }


   /**
    * This test is to test offwing task on LRU config slot without REPL task defined on the parent
    * of LRU (1.1, 1.2), there is existing wkp. There should be no part req for the
    *
    *
    *
    */
   @Test
   public void testOFFWING_LRU_NOREPL_WITHPKG_IMPORT() throws Exception {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOFFWING_LRU_NOREPL_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runImportTasks();

      // get all needed information
      bomPartPN lPARTINFOR = getPartIDs( "SNAUTOTRK001" );
      simpleIDs lwkpIDs = getEventIDs( TestConstants.STATUS_ACTV, iExisting_WPK, "NONE" );
      simpleIDs iTaskIDs_1 = getTaskIDs( iTask_OFFWING_NOREPL );

      // Get event IDs
      iEventIDs_OFFWING_OFFPARENT = getEventIDs( TestConstants.STATUS_ACTV, iEVENT_SDESC_1, "O/D" );
      iEventIDs_REPL_1 = getEventIDs( TestConstants.STATUS_ACTV, iEVENT_SDESC_2, "O/D" );

      // Validate evt_event
      Assert.assertTrue( "check event id 1 is not null", iEventIDs_OFFWING_OFFPARENT != null );
      Assert.assertTrue( "check event id 2 is not null", iEventIDs_REPL_1 != null );

      // Verify evt_event
      VerifyEvtEvent( iEventIDs_OFFWING_OFFPARENT, iEVENT_TYPE_CD_TS, lwkpIDs,
            TestConstants.STATUS_ACTV );
      VerifyEvtEvent( iEventIDs_REPL_1, iEVENT_TYPE_CD_TS, iEventIDs_REPL_1,
            TestConstants.STATUS_ACTV );

      // verify sched_task table
      VerifySchedTask( iEventIDs_OFFWING_OFFPARENT, iEventIDs_OFFWING_OFFPARENT, iTaskIDs_1,
            TestConstants.TASK_CLASS_CD_REQ );
      VerifySchedTask( iEventIDs_REPL_1, iEventIDs_REPL_1, null,
            TestConstants.TASK_CLASS_CD_ADHOC );

      // verify evt_event_rel
      VerifyEvtEventRel( lwkpIDs, TestConstants.REL_TYPE_CD_DRVTASK, iEventIDs_OFFWING_OFFPARENT );
      VerifyEvtEventRel( iEventIDs_REPL_1, TestConstants.REL_TYPE_CD_WORMVL, lwkpIDs );
      VerifyEvtEventRel( iEventIDs_REPL_1, TestConstants.REL_TYPE_CD_DRVTASK, iEventIDs_REPL_1 );
      VerifyEvtEventRel( iEventIDs_OFFWING_OFFPARENT, TestConstants.REL_TYPE_CD_DRVTASK,
            iEventIDs_OFFWING_OFFPARENT );

      // verify evt_inv
      // VerifyEvtINV( iEventIDs_OFFWING_OFFPARENT, iLRU_BOM_ID.getCD(), iLRU_BOM_ID.getID() );
      // VerifyEvtINV( iEventIDs_REPL_1, iSYS_BOM_ID.getCD(), iSYS_BOM_ID.getID() );

      // verify evt_stage
      VerifyEvtStage( iEventIDs_OFFWING_OFFPARENT, TestConstants.STATUS_ACTV );
      VerifyEvtStage( iEventIDs_REPL_1, TestConstants.STATUS_ACTV );

      // verify evt_sched_dead
      simpleIDs ldatatypeID = getDataTypeIDs( "CDY", "Calendar Day", "DAY", "CA" );
      String lsched_dead_dt = VerifyEvtSchedDead( iEventIDs_OFFWING_OFFPARENT, ldatatypeID,
            TestConstants.SCHED_FROM_CD_BIRTH, null );
      VerifyEvtSchedDead( iEventIDs_REPL_1, ldatatypeID, TestConstants.SCHED_FROM_CD_CUSTOM,
            lsched_dead_dt );

      // verify sched_part table
      VerifySched_Part( iEventIDs_REPL_1, lPARTINFOR, TestConstants.STATUS_ACTV, "REQ" );

      // verify sched_inst_part
      String lQuery = "select 1 from " + TableUtil.SCHED_INST_PART + " where SCHED_DB_ID="
            + iEventIDs_REPL_1.getNO_DB_ID() + " and SCHED_ID=" + iEventIDs_REPL_1.getNO_ID();
      Assert.assertTrue( "Check SCHED_INST_PART table to verify the record exists: 1",
            RecordsExist( lQuery ) );

      // verify sched_rmvd_part table
      VerifySchedRmvdPart( iEventIDs_REPL_1, lPARTINFOR, "SNAUTOTRK001" );

      // verify sched_labour
      simpleIDs lLabourIds =
            VerifySchedLabour( iEventIDs_REPL_1, TestConstants.STATUS_ACTV, "LBR" );

      // Verify sched_labour_role
      iLabourRoleIds = VerifySchedLabourRole( lLabourIds, "TECH", "TECH" );

      // Verify sched_labour_role_status
      lQuery = "select 1 from " + TableUtil.SCHED_LABOUR_ROLE_STATUS + " where LABOUR_ROLE_DB_ID="
            + iLabourRoleIds.getNO_DB_ID() + " and LABOUR_ROLE_ID=" + iLabourRoleIds.getNO_ID()
            + " and LABOUR_ROLE_STATUS_CD='ACTV'";
      Assert.assertTrue( "Check sched_labour_role_status table to verify the record exists: 2",
            RecordsExist( lQuery ) );

      // Verify sched_wp

      // Verify sched_wp_sign_req

      // Verify sched_stask_flags
      lQuery = "select 1 from " + TableUtil.SCHED_STASK_FLAGS + " where SCHED_DB_ID="
            + iEventIDs_OFFWING_OFFPARENT.getNO_DB_ID() + " and SCHED_ID="
            + iEventIDs_OFFWING_OFFPARENT.getNO_ID();
      Assert.assertTrue( "Check SCHED_STASK_FLAGS table to verify the record exists: 3",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.SCHED_STASK_FLAGS + " where SCHED_DB_ID="
            + iEventIDs_REPL_1.getNO_DB_ID() + " and SCHED_ID=" + iEventIDs_REPL_1.getNO_ID();
      Assert.assertTrue( "Check SCHED_STASK_FLAGS table to verify the record exists: 4",
            RecordsExist( lQuery ) );

   }


   /**
    * This test is to test offwing task on LRU config slot without REPL task defined on the parent
    * of LRU (1.1, 1.2), there is no existing wkp.
    *
    *
    *
    */
   @Test
   public void testOFFWING_LRU_NOREPL_NOPKG_IMPORT() throws Exception {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOFFWING_LRU_NOREPL_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runImportTasks();

      // get all needed information
      bomPartPN lPARTINFOR = getPartIDs( "SNAUTOTRK001" );
      simpleIDs iTaskIDs_1 = getTaskIDs( iTask_OFFWING_NOREPL );

      // Get event IDs
      iEventIDs_OFFWING_OFFPARENT = getEventIDs( TestConstants.STATUS_ACTV, iEVENT_SDESC_1, "O/D" );
      iEventIDs_REPL_1 = getEventIDs( TestConstants.STATUS_ACTV, iEVENT_SDESC_2, "O/D" );
      iEventIDs_WKP = getEventIDs( TestConstants.STATUS_ACTV, iEVENT_SDESC_21, "NONE" );

      // Validate evt_event
      Assert.assertTrue( "check event id 1 is not null", iEventIDs_OFFWING_OFFPARENT != null );
      Assert.assertTrue( "check event id 2 is not null", iEventIDs_REPL_1 != null );
      Assert.assertTrue( "check event id 4 is not null", iEventIDs_WKP != null );

      // Verify evt_event
      VerifyEvtEvent( iEventIDs_OFFWING_OFFPARENT, iEVENT_TYPE_CD_TS, iEventIDs_WKP,
            TestConstants.STATUS_ACTV );
      VerifyEvtEvent( iEventIDs_REPL_1, iEVENT_TYPE_CD_TS, iEventIDs_REPL_1,
            TestConstants.STATUS_ACTV );
      VerifyEvtEvent( iEventIDs_WKP, iEVENT_TYPE_CD_TS, iEventIDs_WKP, TestConstants.STATUS_ACTV );

      // verify sched_task table
      VerifySchedTask( iEventIDs_OFFWING_OFFPARENT, iEventIDs_OFFWING_OFFPARENT, iTaskIDs_1,
            TestConstants.TASK_CLASS_CD_REQ );
      VerifySchedTask( iEventIDs_REPL_1, iEventIDs_REPL_1, null,
            TestConstants.TASK_CLASS_CD_ADHOC );
      VerifySchedTask( iEventIDs_WKP, null, null, TestConstants.TASK_CLASS_CD_RO );

      // verify evt_event_rel
      VerifyEvtEventRel( iEventIDs_WKP, TestConstants.REL_TYPE_CD_DRVTASK,
            iEventIDs_OFFWING_OFFPARENT );
      VerifyEvtEventRel( iEventIDs_REPL_1, TestConstants.REL_TYPE_CD_WORMVL, iEventIDs_WKP );
      VerifyEvtEventRel( iEventIDs_REPL_1, TestConstants.REL_TYPE_CD_DRVTASK, iEventIDs_REPL_1 );
      VerifyEvtEventRel( iEventIDs_OFFWING_OFFPARENT, TestConstants.REL_TYPE_CD_DRVTASK,
            iEventIDs_OFFWING_OFFPARENT );

      // // verify evt_inv
      // VerifyEvtINV( iEventIDs_OFFWING_OFFPARENT, iLRU_BOM_ID.getCD(), iLRU_BOM_ID.getID() );
      // VerifyEvtINV( iEventIDs_WKP, iLRU_BOM_ID.getCD(), iLRU_BOM_ID.getID() );
      // VerifyEvtINV( iEventIDs_REPL_1, iSYS_BOM_ID.getCD(), iSYS_BOM_ID.getID() );
      //
      // verify evt_stage
      VerifyEvtStage( iEventIDs_OFFWING_OFFPARENT, TestConstants.STATUS_ACTV );
      VerifyEvtStage( iEventIDs_WKP, TestConstants.STATUS_ACTV );
      VerifyEvtStage( iEventIDs_REPL_1, TestConstants.STATUS_ACTV );

      // verify evt_sched_dead
      simpleIDs ldatatypeID = getDataTypeIDs( "CDY", "Calendar Day", "DAY", "CA" );
      String lsched_dead_dt = VerifyEvtSchedDead( iEventIDs_OFFWING_OFFPARENT, ldatatypeID,
            TestConstants.SCHED_FROM_CD_BIRTH, null );
      VerifyEvtSchedDead( iEventIDs_REPL_1, ldatatypeID, TestConstants.SCHED_FROM_CD_CUSTOM,
            lsched_dead_dt );

      // verify sched_part table
      VerifySched_Part( iEventIDs_REPL_1, lPARTINFOR, TestConstants.STATUS_ACTV, "REQ" );

      // verify sched_inst_part
      String lQuery = "select 1 from " + TableUtil.SCHED_INST_PART + " where SCHED_DB_ID="
            + iEventIDs_REPL_1.getNO_DB_ID() + " and SCHED_ID=" + iEventIDs_REPL_1.getNO_ID();
      Assert.assertTrue( "Check SCHED_INST_PART table to verify the record exists: 1",
            RecordsExist( lQuery ) );

      // verify sched_rmvd_part table
      VerifySchedRmvdPart( iEventIDs_REPL_1, lPARTINFOR, "SNAUTOTRK001" );

      // verify sched_labour
      simpleIDs lLabourIds =
            VerifySchedLabour( iEventIDs_REPL_1, TestConstants.STATUS_ACTV, "LBR" );

      // Verify sched_labour_role
      iLabourRoleIds = VerifySchedLabourRole( lLabourIds, "TECH", "TECH" );

      // Verify sched_labour_role_status
      lQuery = "select 1 from " + TableUtil.SCHED_LABOUR_ROLE_STATUS + " where LABOUR_ROLE_DB_ID="
            + iLabourRoleIds.getNO_DB_ID() + " and LABOUR_ROLE_ID=" + iLabourRoleIds.getNO_ID()
            + " and LABOUR_ROLE_STATUS_CD='ACTV'";
      Assert.assertTrue( "Check sched_labour_role_status table to verify the record exists: 2",
            RecordsExist( lQuery ) );

      // Verify sched_wp
      lQuery = "select 1 from " + TableUtil.SCHED_WP + " where SCHED_DB_ID="
            + iEventIDs_WKP.getNO_DB_ID() + " and SCHED_ID=" + iEventIDs_WKP.getNO_ID();
      Assert.assertTrue( "Check sched_wp table to verify the record exists: 3",
            RecordsExist( lQuery ) );

      // Verify sched_wp_sign_req
      VerifySchedWpSignReq( iEventIDs_WKP, "AET", TestConstants.STATUS_ACTV );
      VerifySchedWpSignReq( iEventIDs_WKP, "INSP", TestConstants.STATUS_ACTV );

      // Verify sched_stask_flags
      lQuery = "select 1 from " + TableUtil.SCHED_STASK_FLAGS + " where SCHED_DB_ID="
            + iEventIDs_OFFWING_OFFPARENT.getNO_DB_ID() + " and SCHED_ID="
            + iEventIDs_OFFWING_OFFPARENT.getNO_ID();
      Assert.assertTrue( "Check SCHED_STASK_FLAGS table to verify the record exists: 4",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.SCHED_STASK_FLAGS + " where SCHED_DB_ID="
            + iEventIDs_REPL_1.getNO_DB_ID() + " and SCHED_ID=" + iEventIDs_REPL_1.getNO_ID();
      Assert.assertTrue( "Check SCHED_STASK_FLAGS table to verify the record exists: 5",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.SCHED_STASK_FLAGS + " where SCHED_DB_ID="
            + iEventIDs_WKP.getNO_DB_ID() + " and SCHED_ID=" + iEventIDs_WKP.getNO_ID();
      Assert.assertTrue( "Check SCHED_STASK_FLAGS table to verify the record exists: 5",
            RecordsExist( lQuery ) );

   }


   /**
    * This test is to test offparent task on LRU config slot without REPL task defined on the parent
    * of LRU (1.1, 1.2), , there is existing wkp.
    *
    * @throws Exception
    *
    *
    */

   public void testOFFPARENT_LRU_NOREPL_WITHPKG_VALIDATION() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SNAUTOTRK001'" );
      lMapTask.put( "PART_NO_OEM", "'A0000020'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'" + iTask_OFFPARENT_NOREPL + "'" );
      lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "CUSTOM_START_DT", "null" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();

      // first completed record
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SNAUTOTRK001'" );
      lMapTaskSched.put( "PART_NO_OEM", "'A0000020'" );
      lMapTaskSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskSched.put( "TASK_CD", "'" + iTask_OFFPARENT_NOREPL + "'" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // call express validation runner and result checker with expected error code
      checkTaskValidation_EXCEPTWARNING( "PASS" );
   }


   /**
    * This test is to test offparent task on LRU config slot without REPL task defined on the parent
    * of LRU (1.1, 1.2), , there is existing wkp.
    *
    * @throws Exception
    *
    */
   @Test
   public void testOFFPARENT_LRU_NOREPL_WITHPKG_IMPORT() throws Exception {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOFFPARENT_LRU_NOREPL_WITHPKG_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runImportTasks();

      // get all needed information
      bomPartPN lPARTINFOR = getPartIDs( "SNAUTOTRK001" );
      simpleIDs lwkpIDs = getEventIDs( TestConstants.STATUS_ACTV, iExisting_WPK, "NONE" );
      simpleIDs iTaskIDs_1 = getTaskIDs( iTask_OFFPARENT_NOREPL );

      // Get event IDs
      iEventIDs_OFFWING_OFFPARENT = getEventIDs( TestConstants.STATUS_ACTV, iEVENT_SDESC_3, "O/D" );
      iEventIDs_REPL_1 = getEventIDs( TestConstants.STATUS_ACTV, iEVENT_SDESC_2, "O/D" );

      // Validate evt_event
      Assert.assertTrue( "check event id 1 is not null", iEventIDs_OFFWING_OFFPARENT != null );
      Assert.assertTrue( "check event id 2 is not null", iEventIDs_REPL_1 != null );

      // Verify evt_event
      VerifyEvtEvent( iEventIDs_OFFWING_OFFPARENT, iEVENT_TYPE_CD_TS, lwkpIDs,
            TestConstants.STATUS_ACTV );
      VerifyEvtEvent( iEventIDs_REPL_1, iEVENT_TYPE_CD_TS, iEventIDs_REPL_1,
            TestConstants.STATUS_ACTV );

      // verify sched_task table
      VerifySchedTask( iEventIDs_OFFWING_OFFPARENT, iEventIDs_OFFWING_OFFPARENT, iTaskIDs_1,
            TestConstants.TASK_CLASS_CD_REQ );
      VerifySchedTask( iEventIDs_REPL_1, iEventIDs_REPL_1, null,
            TestConstants.TASK_CLASS_CD_ADHOC );

      // verify evt_event_rel
      VerifyEvtEventRel( lwkpIDs, TestConstants.REL_TYPE_CD_DRVTASK, iEventIDs_OFFWING_OFFPARENT );
      VerifyEvtEventRel( iEventIDs_REPL_1, TestConstants.REL_TYPE_CD_WORMVL, lwkpIDs );
      VerifyEvtEventRel( iEventIDs_REPL_1, TestConstants.REL_TYPE_CD_DRVTASK, iEventIDs_REPL_1 );
      VerifyEvtEventRel( iEventIDs_OFFWING_OFFPARENT, TestConstants.REL_TYPE_CD_DRVTASK,
            iEventIDs_OFFWING_OFFPARENT );

      // // verify evt_inv
      // VerifyEvtINV( iEventIDs_OFFWING_OFFPARENT, iLRU_BOM_ID.getCD(), iLRU_BOM_ID.getID() );
      // VerifyEvtINV( iEventIDs_REPL_1, iSYS_BOM_ID.getCD(), iSYS_BOM_ID.getID() );
      //
      // verify evt_stage
      VerifyEvtStage( iEventIDs_OFFWING_OFFPARENT, TestConstants.STATUS_ACTV );
      VerifyEvtStage( iEventIDs_REPL_1, TestConstants.STATUS_ACTV );

      // verify evt_sched_dead
      simpleIDs ldatatypeID = getDataTypeIDs( "CDY", "Calendar Day", "DAY", "CA" );
      String lsched_dead_dt = VerifyEvtSchedDead( iEventIDs_OFFWING_OFFPARENT, ldatatypeID,
            TestConstants.SCHED_FROM_CD_BIRTH, null );
      VerifyEvtSchedDead( iEventIDs_REPL_1, ldatatypeID, TestConstants.SCHED_FROM_CD_CUSTOM,
            lsched_dead_dt );

      // verify sched_part table
      VerifySched_Part( iEventIDs_REPL_1, lPARTINFOR, TestConstants.STATUS_ACTV, "REQ" );

      // verify sched_inst_part
      String lQuery = "select 1 from " + TableUtil.SCHED_INST_PART + " where SCHED_DB_ID="
            + iEventIDs_REPL_1.getNO_DB_ID() + " and SCHED_ID=" + iEventIDs_REPL_1.getNO_ID();
      Assert.assertTrue( "Check SCHED_INST_PART table to verify the record exists: 1",
            RecordsExist( lQuery ) );

      // verify sched_rmvd_part table
      VerifySchedRmvdPart( iEventIDs_REPL_1, lPARTINFOR, "SNAUTOTRK001" );

      // verify sched_labour
      simpleIDs lLabourIds =
            VerifySchedLabour( iEventIDs_REPL_1, TestConstants.STATUS_ACTV, "LBR" );

      // Verify sched_labour_role
      iLabourRoleIds = VerifySchedLabourRole( lLabourIds, "TECH", "TECH" );

      // Verify sched_labour_role_status
      lQuery = "select 1 from " + TableUtil.SCHED_LABOUR_ROLE_STATUS + " where LABOUR_ROLE_DB_ID="
            + iLabourRoleIds.getNO_DB_ID() + " and LABOUR_ROLE_ID=" + iLabourRoleIds.getNO_ID()
            + " and LABOUR_ROLE_STATUS_CD='ACTV'";
      Assert.assertTrue( "Check sched_labour_role_status table to verify the record exists: 2",
            RecordsExist( lQuery ) );

      // Verify sched_wp

      // Verify sched_wp_sign_req

      // Verify sched_stask_flags
      lQuery = "select 1 from " + TableUtil.SCHED_STASK_FLAGS + " where SCHED_DB_ID="
            + iEventIDs_OFFWING_OFFPARENT.getNO_DB_ID() + " and SCHED_ID="
            + iEventIDs_OFFWING_OFFPARENT.getNO_ID();
      Assert.assertTrue( "Check SCHED_STASK_FLAGS table to verify the record exists: 3",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.SCHED_STASK_FLAGS + " where SCHED_DB_ID="
            + iEventIDs_REPL_1.getNO_DB_ID() + " and SCHED_ID=" + iEventIDs_REPL_1.getNO_ID();
      Assert.assertTrue( "Check SCHED_STASK_FLAGS table to verify the record exists: 4",
            RecordsExist( lQuery ) );
   }


   /**
    * This test is to test offwing task on LRU config slot with REPL task defined on the parent of
    * LRU, repl task=ACFT-SYS-1-1-TRK-P2-REPL (1.1, 1.2), there is no existing wkpkg.
    *
    * @throws Exception
    *
    *
    */

   public void testOFFWING_LRU_REPL_NOPKG_VALIDATION() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SNAUTOTRK005'" );
      lMapTask.put( "PART_NO_OEM", "'A0000002'" );
      lMapTask.put( "MANUFACT_CD", "'11111'" );
      lMapTask.put( "TASK_CD", "'" + iTask_LRU_REPL + "'" );
      lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
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
      lMapTaskSched.put( "TASK_CD", "'" + iTask_LRU_REPL + "'" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // call express validation runner and result checker with expected error code
      checkTaskValidation_EXCEPTWARNING( "PASS" );
   }


   /**
    * This test is to test offwing task on LRU config slot with REPL task defined on the parent of
    * LRU, repl task=ACFT-SYS-1-1-TRK-P2-REPL (1.1, 1.2), there is no existing wkpkg.
    *
    * @throws Exception
    *
    */
   @Test
   public void testOFFWING_LRU_REPL_NOPKG_IMPORT() throws Exception {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOFFWING_LRU_REPL_NOPKG_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runImportTasks();

      // get all needed information
      bomPartPN lPARTINFOR = getPartIDs( "SNAUTOTRK005" );
      simpleIDs iTaskIDs_1 = getTaskIDs( iTask_LRU_REPL );

      // Get event IDs
      iEventIDs_OFFWING_OFFPARENT = getEventIDs( TestConstants.STATUS_ACTV, iEVENT_SDESC_4, "O/D" );
      iEventIDs_REPL_1 = getEventIDs( TestConstants.STATUS_ACTV, iEVENT_SDESC_5, "O/D" );
      iEventIDs_WKP = getEventIDs( TestConstants.STATUS_ACTV, iEVENT_SDESC_18, "NONE" );
      simpleIDs iEventIDs_SRV = getEventIDs( TestConstants.STATUS_ACTV,
            "ACFT-SYS-1-1-TRK-P2-JIC (ACFT-SYS-1-1-TRK-P2-JIC)", "NONE" );

      // Validate evt_event
      Assert.assertTrue( "check event id 1 is not null", iEventIDs_OFFWING_OFFPARENT != null );
      Assert.assertTrue( "check event id 2 is not null", iEventIDs_REPL_1 != null );
      Assert.assertTrue( "check event id 4 is not null", iEventIDs_WKP != null );

      // Verify evt_event
      VerifyEvtEvent( iEventIDs_OFFWING_OFFPARENT, iEVENT_TYPE_CD_TS, iEventIDs_WKP,
            TestConstants.STATUS_ACTV );
      VerifyEvtEvent( iEventIDs_REPL_1, iEVENT_TYPE_CD_TS, iEventIDs_REPL_1,
            TestConstants.STATUS_ACTV );
      VerifyEvtEvent( iEventIDs_WKP, iEVENT_TYPE_CD_TS, iEventIDs_WKP, TestConstants.STATUS_ACTV );

      // verify sched_task table
      VerifySchedTask( iEventIDs_OFFWING_OFFPARENT, iEventIDs_OFFWING_OFFPARENT, iTaskIDs_1,
            TestConstants.TASK_CLASS_CD_REQ );
      VerifySchedTask( iEventIDs_REPL_1, iEventIDs_REPL_1, null, TestConstants.TASK_CLASS_CD_REPL );
      VerifySchedTask( iEventIDs_WKP, null, null, TestConstants.TASK_CLASS_CD_RO );

      // verify evt_event_rel
      VerifyEvtEventRel( iEventIDs_WKP, TestConstants.REL_TYPE_CD_DRVTASK,
            iEventIDs_OFFWING_OFFPARENT );
      VerifyEvtEventRel( iEventIDs_REPL_1, TestConstants.REL_TYPE_CD_WORMVL, iEventIDs_WKP );
      VerifyEvtEventRel( iEventIDs_REPL_1, TestConstants.REL_TYPE_CD_DRVTASK, iEventIDs_REPL_1 );
      VerifyEvtEventRel( iEventIDs_OFFWING_OFFPARENT, TestConstants.REL_TYPE_CD_DRVTASK,
            iEventIDs_OFFWING_OFFPARENT );

      // // verify evt_inv
      // VerifyEvtINV( iEventIDs_OFFWING_OFFPARENT, iLRU_BOM_ID.getCD(), iLRU_BOM_ID.getID() );
      // VerifyEvtINV( iEventIDs_WKP, iLRU_BOM_ID.getCD(), iLRU_BOM_ID.getID() );
      // VerifyEvtINV( iEventIDs_REPL_1, iSYS_BOM_ID.getCD(), iSYS_BOM_ID.getID() );
      //
      // verify evt_stage
      VerifyEvtStage( iEventIDs_OFFWING_OFFPARENT, TestConstants.STATUS_ACTV );
      VerifyEvtStage( iEventIDs_WKP, TestConstants.STATUS_ACTV );
      VerifyEvtStage( iEventIDs_REPL_1, TestConstants.STATUS_ACTV );

      // verify evt_sched_dead
      simpleIDs ldatatypeID = getDataTypeIDs( "CDY", "Calendar Day", "DAY", "CA" );
      String lsched_dead_dt = VerifyEvtSchedDead( iEventIDs_OFFWING_OFFPARENT, ldatatypeID,
            TestConstants.SCHED_FROM_CD_BIRTH, null );
      VerifyEvtSchedDead( iEventIDs_REPL_1, ldatatypeID, TestConstants.SCHED_FROM_CD_CUSTOM,
            lsched_dead_dt );

      // verify sched_part table
      VerifySched_Part( iEventIDs_SRV, lPARTINFOR, TestConstants.STATUS_ACTV, "REQ" );

      // verify sched_inst_part
      String lQuery = "select 1 from " + TableUtil.SCHED_INST_PART + " where SCHED_DB_ID="
            + iEventIDs_SRV.getNO_DB_ID() + " and SCHED_ID=" + iEventIDs_SRV.getNO_ID();
      Assert.assertTrue( "Check SCHED_INST_PART table to verify the record exists: 1",
            RecordsExist( lQuery ) );

      // verify sched_rmvd_part table
      VerifySchedRmvdPart( iEventIDs_SRV, lPARTINFOR, "SNAUTOTRK005" );

      // verify sched_labour
      simpleIDs lLabourIds = VerifySchedLabour( iEventIDs_SRV, TestConstants.STATUS_ACTV, "LBR" );

      // Verify sched_labour_role
      iLabourRoleIds = VerifySchedLabourRole( lLabourIds, "TECH", "TECH" );

      // Verify sched_labour_role_status
      lQuery = "select 1 from " + TableUtil.SCHED_LABOUR_ROLE_STATUS + " where LABOUR_ROLE_DB_ID="
            + iLabourRoleIds.getNO_DB_ID() + " and LABOUR_ROLE_ID=" + iLabourRoleIds.getNO_ID()
            + " and LABOUR_ROLE_STATUS_CD='ACTV'";
      Assert.assertTrue( "Check sched_labour_role_status table to verify the record exists: 2",
            RecordsExist( lQuery ) );

      // Verify sched_wp
      lQuery = "select 1 from " + TableUtil.SCHED_WP + " where SCHED_DB_ID="
            + iEventIDs_WKP.getNO_DB_ID() + " and SCHED_ID=" + iEventIDs_WKP.getNO_ID();
      Assert.assertTrue( "Check sched_wp table to verify the record exists: 3",
            RecordsExist( lQuery ) );

      // Verify sched_wp_sign_req
      VerifySchedWpSignReq( iEventIDs_WKP, "AET", TestConstants.STATUS_ACTV );
      VerifySchedWpSignReq( iEventIDs_WKP, "INSP", TestConstants.STATUS_ACTV );

      // Verify sched_stask_flags
      lQuery = "select 1 from " + TableUtil.SCHED_STASK_FLAGS + " where SCHED_DB_ID="
            + iEventIDs_OFFWING_OFFPARENT.getNO_DB_ID() + " and SCHED_ID="
            + iEventIDs_OFFWING_OFFPARENT.getNO_ID();
      Assert.assertTrue( "Check SCHED_STASK_FLAGS table to verify the record exists: 4",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.SCHED_STASK_FLAGS + " where SCHED_DB_ID="
            + iEventIDs_REPL_1.getNO_DB_ID() + " and SCHED_ID=" + iEventIDs_REPL_1.getNO_ID();
      Assert.assertTrue( "Check SCHED_STASK_FLAGS table to verify the record exists: 5",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.SCHED_STASK_FLAGS + " where SCHED_DB_ID="
            + iEventIDs_WKP.getNO_DB_ID() + " and SCHED_ID=" + iEventIDs_WKP.getNO_ID();
      Assert.assertTrue( "Check SCHED_STASK_FLAGS table to verify the record exists: 5",
            RecordsExist( lQuery ) );

   }


   /**
    * This test is to test offparent task on LRU config slot with REPL task defined on the parent of
    * LRU, repl task=ACFT-SYS-1-1-TRK-P2-REPL (1.1, 1.2)
    *
    * @throws Exception
    *
    */
   public void testOFFPARENT_LRU_REPL_VALIDATION() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SNAUTOTRK005'" );
      lMapTask.put( "PART_NO_OEM", "'A0000002'" );
      lMapTask.put( "MANUFACT_CD", "'11111'" );
      lMapTask.put( "TASK_CD", "'" + iTask_LRU_REPL + "'" );
      lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
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
      lMapTaskSched.put( "TASK_CD", "'" + iTask_LRU_REPL + "'" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // call express validation runner and result checker with expected error code
      checkTaskValidation_EXCEPTWARNING( "PASS" );
   }


   /**
    * This test is to test offwing task on LRU config slot with REPL task defined on the parent of
    * LRU, repl task=ACFT-SYS-1-1-TRK-P2-REPL (1.1, 1.2), there is no existing wkpkg.
    *
    * @throws Exception
    *
    */
   @Test
   public void testOFFPARENT_LRU_REPL_IMPORT() throws Exception {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOFFPARENT_LRU_REPL_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runImportTasks();

      // get all needed information
      bomPartPN lPARTINFOR = getPartIDs( "SNAUTOTRK005" );
      simpleIDs iTaskIDs_1 = getTaskIDs( iTask_LRU_REPL );

      // Get event IDs
      iEventIDs_OFFWING_OFFPARENT = getEventIDs( TestConstants.STATUS_ACTV, iEVENT_SDESC_4, "O/D" );
      iEventIDs_REPL_1 = getEventIDs( TestConstants.STATUS_ACTV, iEVENT_SDESC_5, "O/D" );
      iEventIDs_WKP = getEventIDs( TestConstants.STATUS_ACTV, iEVENT_SDESC_18, "NONE" );
      simpleIDs iEventIDs_SRV = getEventIDs( TestConstants.STATUS_ACTV,
            "ACFT-SYS-1-1-TRK-P2-JIC (ACFT-SYS-1-1-TRK-P2-JIC)", "NONE" );

      // Validate evt_event
      Assert.assertTrue( "check event id 1 is not null", iEventIDs_OFFWING_OFFPARENT != null );
      Assert.assertTrue( "check event id 2 is not null", iEventIDs_REPL_1 != null );
      Assert.assertTrue( "check event id 4 is not null", iEventIDs_WKP != null );

      // Verify evt_event
      VerifyEvtEvent( iEventIDs_OFFWING_OFFPARENT, iEVENT_TYPE_CD_TS, iEventIDs_WKP,
            TestConstants.STATUS_ACTV );
      VerifyEvtEvent( iEventIDs_REPL_1, iEVENT_TYPE_CD_TS, iEventIDs_REPL_1,
            TestConstants.STATUS_ACTV );
      VerifyEvtEvent( iEventIDs_WKP, iEVENT_TYPE_CD_TS, null, TestConstants.STATUS_ACTV );

      // verify sched_task table
      VerifySchedTask( iEventIDs_OFFWING_OFFPARENT, iEventIDs_OFFWING_OFFPARENT, iTaskIDs_1,
            TestConstants.TASK_CLASS_CD_REQ );
      VerifySchedTask( iEventIDs_REPL_1, iEventIDs_REPL_1, null, TestConstants.TASK_CLASS_CD_REPL );
      VerifySchedTask( iEventIDs_WKP, null, null, TestConstants.TASK_CLASS_CD_RO );

      // verify evt_event_rel
      VerifyEvtEventRel( iEventIDs_WKP, TestConstants.REL_TYPE_CD_DRVTASK,
            iEventIDs_OFFWING_OFFPARENT );
      VerifyEvtEventRel( iEventIDs_REPL_1, TestConstants.REL_TYPE_CD_WORMVL, iEventIDs_WKP );
      VerifyEvtEventRel( iEventIDs_REPL_1, TestConstants.REL_TYPE_CD_DRVTASK, iEventIDs_REPL_1 );
      VerifyEvtEventRel( iEventIDs_OFFWING_OFFPARENT, TestConstants.REL_TYPE_CD_DRVTASK,
            iEventIDs_OFFWING_OFFPARENT );

      // // verify evt_inv
      // VerifyEvtINV( iEventIDs_OFFWING_OFFPARENT, iLRU_BOM_ID.getCD(), iLRU_BOM_ID.getID() );
      // VerifyEvtINV( iEventIDs_WKP, iLRU_BOM_ID.getCD(), iLRU_BOM_ID.getID() );
      // VerifyEvtINV( iEventIDs_REPL_1, iSYS_BOM_ID.getCD(), iSYS_BOM_ID.getID() );

      // verify evt_stage
      VerifyEvtStage( iEventIDs_OFFWING_OFFPARENT, TestConstants.STATUS_ACTV );
      VerifyEvtStage( iEventIDs_WKP, TestConstants.STATUS_ACTV );
      VerifyEvtStage( iEventIDs_REPL_1, TestConstants.STATUS_ACTV );

      // verify evt_sched_dead
      simpleIDs ldatatypeID = getDataTypeIDs( "CDY", "Calendar Day", "DAY", "CA" );
      String lsched_dead_dt = VerifyEvtSchedDead( iEventIDs_OFFWING_OFFPARENT, ldatatypeID,
            TestConstants.SCHED_FROM_CD_BIRTH, null );
      VerifyEvtSchedDead( iEventIDs_REPL_1, ldatatypeID, TestConstants.SCHED_FROM_CD_CUSTOM,
            lsched_dead_dt );

      // verify sched_part table
      VerifySched_Part( iEventIDs_SRV, lPARTINFOR, TestConstants.STATUS_ACTV, "REQ" );

      // verify sched_inst_part
      String lQuery = "select 1 from " + TableUtil.SCHED_INST_PART + " where SCHED_DB_ID="
            + iEventIDs_SRV.getNO_DB_ID() + " and SCHED_ID=" + iEventIDs_SRV.getNO_ID();
      Assert.assertTrue( "Check SCHED_INST_PART table to verify the record exists: 1",
            RecordsExist( lQuery ) );

      // verify sched_rmvd_part table
      VerifySchedRmvdPart( iEventIDs_SRV, lPARTINFOR, "SNAUTOTRK005" );

      // verify sched_labour
      simpleIDs lLabourIds = VerifySchedLabour( iEventIDs_SRV, TestConstants.STATUS_ACTV, "LBR" );

      // Verify sched_labour_role
      iLabourRoleIds = VerifySchedLabourRole( lLabourIds, "TECH", "TECH" );

      // Verify sched_labour_role_status
      lQuery = "select 1 from " + TableUtil.SCHED_LABOUR_ROLE_STATUS + " where LABOUR_ROLE_DB_ID="
            + iLabourRoleIds.getNO_DB_ID() + " and LABOUR_ROLE_ID=" + iLabourRoleIds.getNO_ID()
            + " and LABOUR_ROLE_STATUS_CD='ACTV'";
      Assert.assertTrue( "Check sched_labour_role_status table to verify the record exists: 2",
            RecordsExist( lQuery ) );

      // Verify sched_wp
      lQuery = "select 1 from " + TableUtil.SCHED_WP + " where SCHED_DB_ID="
            + iEventIDs_WKP.getNO_DB_ID() + " and SCHED_ID=" + iEventIDs_WKP.getNO_ID();
      Assert.assertTrue( "Check sched_wp table to verify the record exists: 3",
            RecordsExist( lQuery ) );

      // Verify sched_wp_sign_req
      VerifySchedWpSignReq( iEventIDs_WKP, "AET", TestConstants.STATUS_ACTV );
      VerifySchedWpSignReq( iEventIDs_WKP, "INSP", TestConstants.STATUS_ACTV );

      // Verify sched_stask_flags
      lQuery = "select 1 from " + TableUtil.SCHED_STASK_FLAGS + " where SCHED_DB_ID="
            + iEventIDs_OFFWING_OFFPARENT.getNO_DB_ID() + " and SCHED_ID="
            + iEventIDs_OFFWING_OFFPARENT.getNO_ID();
      Assert.assertTrue( "Check SCHED_STASK_FLAGS table to verify the record exists: 4",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.SCHED_STASK_FLAGS + " where SCHED_DB_ID="
            + iEventIDs_REPL_1.getNO_DB_ID() + " and SCHED_ID=" + iEventIDs_REPL_1.getNO_ID();
      Assert.assertTrue( "Check SCHED_STASK_FLAGS table to verify the record exists: 5",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.SCHED_STASK_FLAGS + " where SCHED_DB_ID="
            + iEventIDs_WKP.getNO_DB_ID() + " and SCHED_ID=" + iEventIDs_WKP.getNO_ID();
      Assert.assertTrue( "Check SCHED_STASK_FLAGS table to verify the record exists: 5",
            RecordsExist( lQuery ) );

   }


   /**
    * This test is to test offwing task on SRU config slot without REPL task defined on the parent
    * of LRU (2.1)
    *
    * @throws Exception
    *
    */
   public void testOFFWING_SRU_NOREPL_VALIDATION() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SNAUTOTRK006'" );
      lMapTask.put( "PART_NO_OEM", "'A0000022'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'" + iTask_OFFWING_NOREPL_SRU + "'" );
      lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "CUSTOM_START_DT", "null" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();

      // first completed record
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SNAUTOTRK006'" );
      lMapTaskSched.put( "PART_NO_OEM", "'A0000022'" );
      lMapTaskSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskSched.put( "TASK_CD", "'" + iTask_OFFWING_NOREPL_SRU + "'" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // call express validation runner and result checker with expected error code
      checkTaskValidation_EXCEPTWARNING( "PASS" );
   }


   /**
    * This test is to test offwing task on SRU config slot without REPL task defined on the parent
    * of LRU (2.1), there is no existing REPL task defined.
    *
    * @throws Exception
    *
    *
    */
   @Test
   public void testOFFWING_SRU_NOREPL_IMPORT() throws Exception {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOFFWING_SRU_NOREPL_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runImportTasks();

      // get all needed information
      bomPartPN lPARTINFOR2 = getAssmblINFOR( "A0000021" );
      simpleIDs iTaskIDs_1 = getTaskIDs( iTask_OFFWING_NOREPL_SRU );

      // Get event IDs
      iEventIDs_OFFWING_OFFPARENT = getEventIDs_2( "REQ" );
      iEventIDs_REPL_1 = getEventIDs_2( "ADHOC" );
      iEventIDs_WKP = getEventIDs_2( "RO" );

      // Validate evt_event
      Assert.assertTrue( "check event id 1 is not null", iEventIDs_OFFWING_OFFPARENT != null );
      Assert.assertTrue( "check event id 2 is not null", iEventIDs_REPL_1 != null );
      Assert.assertTrue( "check event id 4 is not null", iEventIDs_WKP != null );

      // Verify evt_event
      VerifyEvtEvent( iEventIDs_OFFWING_OFFPARENT, iEVENT_TYPE_CD_TS, iEventIDs_WKP,
            TestConstants.STATUS_ACTV );
      VerifyEvtEvent( iEventIDs_REPL_1, iEVENT_TYPE_CD_TS, iEventIDs_REPL_1,
            TestConstants.STATUS_ACTV );
      VerifyEvtEvent( iEventIDs_WKP, iEVENT_TYPE_CD_TS, iEventIDs_WKP, TestConstants.STATUS_ACTV );

      // verify sched_task table
      VerifySchedTask( iEventIDs_OFFWING_OFFPARENT, iEventIDs_OFFWING_OFFPARENT, iTaskIDs_1,
            TestConstants.TASK_CLASS_CD_REQ );
      VerifySchedTask( iEventIDs_REPL_1, iEventIDs_REPL_1, null,
            TestConstants.TASK_CLASS_CD_ADHOC );
      VerifySchedTask( iEventIDs_WKP, null, null, TestConstants.TASK_CLASS_CD_RO );

      // verify evt_event_rel
      VerifyEvtEventRel( iEventIDs_REPL_1, TestConstants.REL_TYPE_CD_WORMVL, iEventIDs_WKP );

      // verify evt_inv
      // VerifyEvtINV( iEventIDs_OFFWING_OFFPARENT, iLRU_BOM_ID.getCD(), iLRU_BOM_ID.getID() );
      // VerifyEvtINV( iEventIDs_WKP, iLRU_BOM_ID.getCD(), iLRU_BOM_ID.getID() );
      // VerifyEvtINV( iEventIDs_REPL_1, iSYS_BOM_ID.getCD(), iSYS_BOM_ID.getID() );

      // verify evt_stage
      VerifyEvtStage( iEventIDs_OFFWING_OFFPARENT, TestConstants.STATUS_ACTV );
      VerifyEvtStage( iEventIDs_WKP, TestConstants.STATUS_ACTV );
      VerifyEvtStage( iEventIDs_REPL_1, TestConstants.STATUS_ACTV );

      // verify evt_sched_dead

      // verify sched_part table
      VerifySched_Part( iEventIDs_REPL_1, lPARTINFOR2, TestConstants.STATUS_ACTV, "REQ" );

      // verify sched_inst_part
      String lQuery = "select 1 from " + TableUtil.SCHED_INST_PART + " where SCHED_DB_ID="
            + iEventIDs_REPL_1.getNO_DB_ID() + " and SCHED_ID=" + iEventIDs_REPL_1.getNO_ID();
      Assert.assertTrue( "Check SCHED_INST_PART table to verify the record exists: 1",
            RecordsExist( lQuery ) );

      // verify sched_rmvd_part table
      VerifySchedRmvdPart( iEventIDs_REPL_1, lPARTINFOR2, "XXX" );

      // verify sched_labour
      simpleIDs lLabourIds =
            VerifySchedLabour( iEventIDs_REPL_1, TestConstants.STATUS_ACTV, "LBR" );

      // Verify sched_labour_role
      iLabourRoleIds = VerifySchedLabourRole( lLabourIds, "TECH", "TECH" );

      // Verify sched_labour_role_status
      lQuery = "select 1 from " + TableUtil.SCHED_LABOUR_ROLE_STATUS + " where LABOUR_ROLE_DB_ID="
            + iLabourRoleIds.getNO_DB_ID() + " and LABOUR_ROLE_ID=" + iLabourRoleIds.getNO_ID()
            + " and LABOUR_ROLE_STATUS_CD='ACTV'";
      Assert.assertTrue( "Check sched_labour_role_status table to verify the record exists: 2",
            RecordsExist( lQuery ) );

      // Verify sched_wp
      lQuery = "select 1 from " + TableUtil.SCHED_WP + " where SCHED_DB_ID="
            + iEventIDs_WKP.getNO_DB_ID() + " and SCHED_ID=" + iEventIDs_WKP.getNO_ID();
      Assert.assertTrue( "Check sched_wp table to verify the record exists: 3",
            RecordsExist( lQuery ) );

      // Verify sched_wp_sign_req
      VerifySchedWpSignReq( iEventIDs_WKP, "AET", TestConstants.STATUS_ACTV );
      VerifySchedWpSignReq( iEventIDs_WKP, "INSP", TestConstants.STATUS_ACTV );

      // Verify sched_stask_flags
      lQuery = "select 1 from " + TableUtil.SCHED_STASK_FLAGS + " where SCHED_DB_ID="
            + iEventIDs_OFFWING_OFFPARENT.getNO_DB_ID() + " and SCHED_ID="
            + iEventIDs_OFFWING_OFFPARENT.getNO_ID();
      Assert.assertTrue( "Check SCHED_STASK_FLAGS table to verify the record exists: 4",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.SCHED_STASK_FLAGS + " where SCHED_DB_ID="
            + iEventIDs_REPL_1.getNO_DB_ID() + " and SCHED_ID=" + iEventIDs_REPL_1.getNO_ID();
      Assert.assertTrue( "Check SCHED_STASK_FLAGS table to verify the record exists: 5",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.SCHED_STASK_FLAGS + " where SCHED_DB_ID="
            + iEventIDs_WKP.getNO_DB_ID() + " and SCHED_ID=" + iEventIDs_WKP.getNO_ID();
      Assert.assertTrue( "Check SCHED_STASK_FLAGS table to verify the record exists: 5",
            RecordsExist( lQuery ) );

   }


   /**
    * This test is to test offparent task on SRU config slot without REPL task defined on the parent
    * of LRU (2.1)
    *
    * @throws Exception
    *
    *
    */

   public void testOFFPARENT_SRU_NOREPL_VALIDATION() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SNAUTOTRK006'" );
      lMapTask.put( "PART_NO_OEM", "'A0000022'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'" + iTask_OFFPARENT_NOREPL_SRU + "'" );
      lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "CUSTOM_START_DT", "null" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();

      // first completed record
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SNAUTOTRK006'" );
      lMapTaskSched.put( "PART_NO_OEM", "'A0000022'" );
      lMapTaskSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskSched.put( "TASK_CD", "'" + iTask_OFFPARENT_NOREPL_SRU + "'" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // call express validation runner and result checker with expected error code
      checkTaskValidation_EXCEPTWARNING( "PASS" );
   }


   /**
    * This test is to test offparent task on SRU config slot without REPL task defined on the parent
    * of LRU (2.1)
    *
    * @throws Exception
    *
    *
    *
    */

   @Test
   public void testOFFPARENT_SRU_NOREPL_IMPORT() throws Exception {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOFFPARENT_SRU_NOREPL_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runImportTasks();

      // get all needed information
      bomPartPN lPARTINFOR = getPartIDs( "SNAUTOTRK006" );
      bomPartPN lPARTINFOR2 = getAssmblINFOR( "A0000021" );
      simpleIDs iTaskIDs_1 = getTaskIDs( iTask_OFFPARENT_NOREPL_SRU );

      // Get event IDs
      iEventIDs_OFFWING_OFFPARENT =
            getEventIDs( TestConstants.STATUS_ACTV, iEVENT_SDESC_10, "NONE" );
      iEventIDs_WKP = iEventIDs_WKP = getEventIDs_2( "RO" );
      iEventIDs_REPL_1 =
            getEventIDs3( TestConstants.STATUS_ACTV, iEVENT_SDESC_19, "NONE", iEventIDs_WKP ); // off
                                                                                               // wing
      iEventIDs_REPL_2 = getEventIDs2( TestConstants.STATUS_ACTV, iEVENT_SDESC_14, iEventIDs_WKP ); // off
                                                                                                    // parent

      // Validate evt_event
      Assert.assertTrue( "check event id 1 is not null", iEventIDs_OFFWING_OFFPARENT != null );
      Assert.assertTrue( "check event id 2 is not null", iEventIDs_REPL_1 != null );
      Assert.assertTrue( "check event id 2 is not null", iEventIDs_REPL_2 != null );
      Assert.assertTrue( "check event id 4 is not null", iEventIDs_WKP != null );

      // Verify evt_event
      VerifyEvtEvent( iEventIDs_OFFWING_OFFPARENT, iEVENT_TYPE_CD_TS, iEventIDs_WKP,
            TestConstants.STATUS_ACTV );
      VerifyEvtEvent( iEventIDs_REPL_1, iEVENT_TYPE_CD_TS, iEventIDs_REPL_1,
            TestConstants.STATUS_ACTV );
      VerifyEvtEvent( iEventIDs_REPL_2, iEVENT_TYPE_CD_TS, iEventIDs_WKP,
            TestConstants.STATUS_ACTV );

      // verify sched_task table
      VerifySchedTask( iEventIDs_OFFWING_OFFPARENT, iEventIDs_OFFWING_OFFPARENT, iTaskIDs_1,
            TestConstants.TASK_CLASS_CD_REQ );
      VerifySchedTask( iEventIDs_REPL_1, iEventIDs_REPL_1, null,
            TestConstants.TASK_CLASS_CD_ADHOC );
      VerifySchedTask( iEventIDs_REPL_2, iEventIDs_REPL_2, null,
            TestConstants.TASK_CLASS_CD_ADHOC );
      VerifySchedTask( iEventIDs_WKP, null, null, TestConstants.TASK_CLASS_CD_RO );

      // verify evt_event_rel
      VerifyEvtEventRel( iEventIDs_REPL_1, TestConstants.REL_TYPE_CD_WORMVL, iEventIDs_WKP );

      // // verify evt_inv
      // // VerifyEvtINV( iEventIDs_OFFWING_OFFPARENT, iASSMBL_CD, iPART_BOM_ID.getID() );
      // // VerifyEvtINV( iEventIDs_WKP, iASSMBL_CD, iOFFPARENT_BOM_ID.getID() );
      // // VerifyEvtINV( iEventIDs_REPL_1, iASSMBL_CD, iOFFWING_BOM_ID.getID() );
      // // VerifyEvtINV( iEventIDs_REPL_2, iASSMBL_CD, iOFFPARENT_BOM_ID.getID() );
      //
      // verify evt_stage
      VerifyEvtStage( iEventIDs_OFFWING_OFFPARENT, TestConstants.STATUS_ACTV );
      VerifyEvtStage( iEventIDs_WKP, TestConstants.STATUS_ACTV );
      VerifyEvtStage( iEventIDs_REPL_1, TestConstants.STATUS_ACTV );
      VerifyEvtStage( iEventIDs_REPL_2, TestConstants.STATUS_ACTV );

      // verify evt_sched_dead

      // verify sched_part table
      VerifySched_Part( iEventIDs_REPL_1, lPARTINFOR2, TestConstants.STATUS_ACTV, "REQ" );
      VerifySched_Part( iEventIDs_REPL_2, lPARTINFOR, TestConstants.STATUS_ACTV, "REQ" );

      // verify sched_inst_part
      String lQuery = "select 1 from " + TableUtil.SCHED_INST_PART + " where SCHED_DB_ID="
            + iEventIDs_REPL_1.getNO_DB_ID() + " and SCHED_ID=" + iEventIDs_REPL_1.getNO_ID();
      Assert.assertTrue( "Check SCHED_INST_PART table to verify the record exists: 1",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.SCHED_INST_PART + " where SCHED_DB_ID="
            + iEventIDs_REPL_2.getNO_DB_ID() + " and SCHED_ID=" + iEventIDs_REPL_2.getNO_ID();
      Assert.assertTrue( "Check SCHED_INST_PART table to verify the record exists: 2",
            RecordsExist( lQuery ) );

      // verify sched_rmvd_part table
      VerifySchedRmvdPart( iEventIDs_REPL_1, lPARTINFOR2, "XXX" );
      VerifySchedRmvdPart( iEventIDs_REPL_2, lPARTINFOR, "SNAUTOTRK006" );

      // verify sched_labour
      simpleIDs lLabourIds =
            VerifySchedLabour( iEventIDs_REPL_1, TestConstants.STATUS_ACTV, "LBR" );
      simpleIDs lLabourIds2 =
            VerifySchedLabour( iEventIDs_REPL_2, TestConstants.STATUS_ACTV, "LBR" );

      // Verify sched_labour_role
      iLabourRoleIds = VerifySchedLabourRole( lLabourIds, "TECH", "TECH" );
      iLabourRoleIds2 = VerifySchedLabourRole( lLabourIds2, "TECH", "TECH" );

      // Verify sched_labour_role_status
      lQuery = "select 1 from " + TableUtil.SCHED_LABOUR_ROLE_STATUS + " where LABOUR_ROLE_DB_ID="
            + iLabourRoleIds.getNO_DB_ID() + " and LABOUR_ROLE_ID=" + iLabourRoleIds.getNO_ID()
            + " and LABOUR_ROLE_STATUS_CD='ACTV'";
      Assert.assertTrue( "Check sched_labour_role_status table to verify the record exists: 2",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.SCHED_LABOUR_ROLE_STATUS + " where LABOUR_ROLE_DB_ID="
            + iLabourRoleIds2.getNO_DB_ID() + " and LABOUR_ROLE_ID=" + iLabourRoleIds2.getNO_ID()
            + " and LABOUR_ROLE_STATUS_CD='ACTV'";
      Assert.assertTrue( "Check sched_labour_role_status table to verify the record exists: 2",
            RecordsExist( lQuery ) );

      // Verify sched_wp
      lQuery = "select 1 from " + TableUtil.SCHED_WP + " where SCHED_DB_ID="
            + iEventIDs_WKP.getNO_DB_ID() + " and SCHED_ID=" + iEventIDs_WKP.getNO_ID();
      Assert.assertTrue( "Check sched_wp table to verify the record exists: 3",
            RecordsExist( lQuery ) );

      // Verify sched_wp_sign_req
      VerifySchedWpSignReq( iEventIDs_WKP, "AET", TestConstants.STATUS_ACTV );
      VerifySchedWpSignReq( iEventIDs_WKP, "INSP", TestConstants.STATUS_ACTV );

      // Verify sched_stask_flags
      lQuery = "select 1 from " + TableUtil.SCHED_STASK_FLAGS + " where SCHED_DB_ID="
            + iEventIDs_OFFWING_OFFPARENT.getNO_DB_ID() + " and SCHED_ID="
            + iEventIDs_OFFWING_OFFPARENT.getNO_ID();
      Assert.assertTrue( "Check SCHED_STASK_FLAGS table to verify the record exists: 4",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.SCHED_STASK_FLAGS + " where SCHED_DB_ID="
            + iEventIDs_REPL_1.getNO_DB_ID() + " and SCHED_ID=" + iEventIDs_REPL_1.getNO_ID();
      Assert.assertTrue( "Check SCHED_STASK_FLAGS table to verify the record exists: 5",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.SCHED_STASK_FLAGS + " where SCHED_DB_ID="
            + iEventIDs_REPL_2.getNO_DB_ID() + " and SCHED_ID=" + iEventIDs_REPL_2.getNO_ID();
      Assert.assertTrue( "Check SCHED_STASK_FLAGS table to verify the record exists: 5",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.SCHED_STASK_FLAGS + " where SCHED_DB_ID="
            + iEventIDs_WKP.getNO_DB_ID() + " and SCHED_ID=" + iEventIDs_WKP.getNO_ID();
      Assert.assertTrue( "Check SCHED_STASK_FLAGS table to verify the record exists: 5",
            RecordsExist( lQuery ) );

   }


   /**
    * This test is to test offwing task on SRU config slot with REPL task defined on the parent of
    * LRU (2.1)
    *
    * @throws Exception
    *
    */
   public void testOFFWING_SRU_REPL_VALIDATION() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SNAUTOTRK006'" );
      lMapTask.put( "PART_NO_OEM", "'A0000022'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'" + iTask_OFFWING_NOREPL_SRU + "'" );
      lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "CUSTOM_START_DT", "null" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();

      // first completed record
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SNAUTOTRK006'" );
      lMapTaskSched.put( "PART_NO_OEM", "'A0000022'" );
      lMapTaskSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskSched.put( "TASK_CD", "'" + iTask_OFFWING_NOREPL_SRU + "'" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // call express validation runner and result checker with expected error code
      checkTaskValidation_EXCEPTWARNING( "PASS" );
   }


   /**
    * This test is to test offwing task on SRU config slot with REPL task defined on the parent of
    * LRU (2.1)
    *
    * @throws Exception
    */

   @Test
   public void testOFFWING_SRU_REPL_IMPORT() throws Exception {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOFFWING_SRU_REPL_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runImportTasks();

      // get all needed information
      simpleIDs iTaskIDs_1 = getTaskIDs( iTask_OFFWING_NOREPL_SRU );

      // Get event IDs
      iEventIDs_OFFWING_OFFPARENT =
            getEventIDs( TestConstants.STATUS_ACTV, iEVENT_SDESC_6, "NONE" );
      iEventIDs_WKP = iEventIDs_WKP = getEventIDs_2( "RO" );
      iEventIDs_REPL_1 =
            getEventIDs3( TestConstants.STATUS_ACTV, iEVENT_SDESC_12, "NONE", iEventIDs_WKP ); // off
                                                                                               // wing

      // Validate evt_event
      Assert.assertTrue( "check event id 1 is not null", iEventIDs_OFFWING_OFFPARENT != null );
      Assert.assertTrue( "check event id 2 is not null", iEventIDs_REPL_1 != null );
      Assert.assertTrue( "check event id 3 is not null", iEventIDs_WKP != null );

      // Verify evt_event
      VerifyEvtEvent( iEventIDs_OFFWING_OFFPARENT, iEVENT_TYPE_CD_TS, iEventIDs_WKP,
            TestConstants.STATUS_ACTV );
      VerifyEvtEvent( iEventIDs_REPL_1, iEVENT_TYPE_CD_TS, iEventIDs_REPL_1,
            TestConstants.STATUS_ACTV );
      VerifyEvtEvent( iEventIDs_WKP, iEVENT_TYPE_CD_TS, iEventIDs_WKP, TestConstants.STATUS_ACTV );

      // verify sched_task table
      VerifySchedTask( iEventIDs_OFFWING_OFFPARENT, iEventIDs_OFFWING_OFFPARENT, iTaskIDs_1,
            TestConstants.TASK_CLASS_CD_REQ );
      VerifySchedTask( iEventIDs_REPL_1, iEventIDs_REPL_1, null, TestConstants.TASK_CLASS_CD_REPL );
      VerifySchedTask( iEventIDs_WKP, null, null, TestConstants.TASK_CLASS_CD_RO );

      // verify evt_event_rel
      VerifyEvtEventRel( iEventIDs_REPL_1, TestConstants.REL_TYPE_CD_WORMVL, iEventIDs_WKP );

      // // verify evt_inv
      // VerifyEvtINV( iEventIDs_OFFWING_OFFPARENT, iASSMBL_CD, iPART_BOM_ID.getID() );
      // VerifyEvtINV( iEventIDs_WKP, iASSMBL_CD, iOFFPARENT_BOM_ID.getID() );
      // VerifyEvtINV( iEventIDs_REPL_1, iASSMBL_CD, iOFFWING_BOM_ID.getID() );

      // verify evt_stage
      VerifyEvtStage( iEventIDs_OFFWING_OFFPARENT, TestConstants.STATUS_ACTV );
      VerifyEvtStage( iEventIDs_WKP, TestConstants.STATUS_ACTV );
      VerifyEvtStage( iEventIDs_REPL_1, TestConstants.STATUS_ACTV );

      // verify evt_sched_dead

      // verify sched_part table

      // verify sched_inst_part

      // verify sched_rmvd_part table

      // verify sched_labour

      // Verify sched_labour_role

      // Verify sched_labour_role_status

      // Verify sched_wp
      String lQuery = "select 1 from " + TableUtil.SCHED_WP + " where SCHED_DB_ID="
            + iEventIDs_WKP.getNO_DB_ID() + " and SCHED_ID=" + iEventIDs_WKP.getNO_ID();
      Assert.assertTrue( "Check sched_wp table to verify the record exists: 3",
            RecordsExist( lQuery ) );

      // Verify sched_wp_sign_req
      VerifySchedWpSignReq( iEventIDs_WKP, "AET", TestConstants.STATUS_ACTV );
      VerifySchedWpSignReq( iEventIDs_WKP, "INSP", TestConstants.STATUS_ACTV );

      // Verify sched_stask_flags
      lQuery = "select 1 from " + TableUtil.SCHED_STASK_FLAGS + " where SCHED_DB_ID="
            + iEventIDs_OFFWING_OFFPARENT.getNO_DB_ID() + " and SCHED_ID="
            + iEventIDs_OFFWING_OFFPARENT.getNO_ID();
      Assert.assertTrue( "Check SCHED_STASK_FLAGS table to verify the record exists: 4",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.SCHED_STASK_FLAGS + " where SCHED_DB_ID="
            + iEventIDs_REPL_1.getNO_DB_ID() + " and SCHED_ID=" + iEventIDs_REPL_1.getNO_ID();
      Assert.assertTrue( "Check SCHED_STASK_FLAGS table to verify the record exists: 5",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.SCHED_STASK_FLAGS + " where SCHED_DB_ID="
            + iEventIDs_WKP.getNO_DB_ID() + " and SCHED_ID=" + iEventIDs_WKP.getNO_ID();
      Assert.assertTrue( "Check SCHED_STASK_FLAGS table to verify the record exists: 5",
            RecordsExist( lQuery ) );

   }


   /**
    * This test is to test offparent task on SRU config slot with REPL task defined on the parent of
    * LRU (2.1)
    *
    * @throws Exception
    *
    */
   public void testOFFPARENT_SRU_REPL_VALIDATION() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SNAUTOTRK006'" );
      lMapTask.put( "PART_NO_OEM", "'A0000022'" );
      lMapTask.put( "MANUFACT_CD", "'10001'" );
      lMapTask.put( "TASK_CD", "'" + iTask_OFFPARENT_NOREPL_SRU + "'" );
      lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "CUSTOM_START_DT", "null" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();

      // first completed record
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SNAUTOTRK006'" );
      lMapTaskSched.put( "PART_NO_OEM", "'A0000022'" );
      lMapTaskSched.put( "MANUFACT_CD", "'10001'" );
      lMapTaskSched.put( "TASK_CD", "'" + iTask_OFFPARENT_NOREPL_SRU + "'" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // call express validation runner and result checker with expected error code
      checkTaskValidation_EXCEPTWARNING( "PASS" );
   }


   /**
    * This test is to test offparent task on SRU config slot with REPL task defined on the parent of
    * LRU (2.1)
    *
    * @throws Exception
    *
    */
   @Test
   // @Ignore
   public void testOFFPARENT_SRU_REPL_IMPORT() throws Exception {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOFFPARENT_SRU_REPL_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runImportTasks();

      // get all needed information
      bomPartPN lPARTINFOR = getPartIDs( "SNAUTOTRK006" );
      simpleIDs iTaskIDs_1 = getTaskIDs( iTask_OFFPARENT_NOREPL_SRU );

      // Get event IDs
      iEventIDs_OFFWING_OFFPARENT =
            getEventIDs( TestConstants.STATUS_ACTV, iEVENT_SDESC_10, "NONE" );
      iEventIDs_WKP = iEventIDs_WKP = getEventIDs_2( "RO" );
      iEventIDs_REPL_1 =
            getEventIDs3( TestConstants.STATUS_ACTV, iEVENT_SDESC_12, "NONE", iEventIDs_WKP ); // off
                                                                                               // wing
      iEventIDs_REPL_2 = getEventIDs2( TestConstants.STATUS_ACTV, iEVENT_SDESC_11, iEventIDs_WKP ); // off
                                                                                                    // parent

      // Validate evt_event
      Assert.assertTrue( "check event id 1 is not null", iEventIDs_OFFWING_OFFPARENT != null );
      Assert.assertTrue( "check event id 2 is not null", iEventIDs_REPL_1 != null );
      Assert.assertTrue( "check event id 3 is not null", iEventIDs_REPL_2 != null );
      Assert.assertTrue( "check event id 4 is not null", iEventIDs_WKP != null );

      // Verify evt_event
      VerifyEvtEvent( iEventIDs_OFFWING_OFFPARENT, iEVENT_TYPE_CD_TS, iEventIDs_WKP,
            TestConstants.STATUS_ACTV );
      VerifyEvtEvent( iEventIDs_REPL_1, iEVENT_TYPE_CD_TS, iEventIDs_REPL_1,
            TestConstants.STATUS_ACTV );
      VerifyEvtEvent( iEventIDs_REPL_2, iEVENT_TYPE_CD_TS, iEventIDs_WKP,
            TestConstants.STATUS_ACTV );

      // verify sched_task table
      VerifySchedTask( iEventIDs_OFFWING_OFFPARENT, iEventIDs_OFFWING_OFFPARENT, iTaskIDs_1,
            TestConstants.TASK_CLASS_CD_REQ );
      VerifySchedTask( iEventIDs_REPL_1, iEventIDs_REPL_1, null, TestConstants.TASK_CLASS_CD_REPL );
      VerifySchedTask( iEventIDs_REPL_2, iEventIDs_REPL_2, null, TestConstants.TASK_CLASS_CD_REPL );
      VerifySchedTask( iEventIDs_WKP, null, null, TestConstants.TASK_CLASS_CD_RO );

      // verify evt_event_rel
      VerifyEvtEventRel( iEventIDs_REPL_1, TestConstants.REL_TYPE_CD_WORMVL, iEventIDs_WKP );

      // // verify evt_inv
      // VerifyEvtINV( iEventIDs_OFFWING_OFFPARENT, iASSMBL_CD, iPART_BOM_ID.getID() );
      // // VerifyEvtINV( iEventIDs_WKP, iASSMBL_CD, iOFFPARENT_BOM_ID.getID() );
      // // VerifyEvtINV( iEventIDs_REPL_1, iASSMBL_CD, iOFFWING_BOM_ID.getID() );
      // // VerifyEvtINV( iEventIDs_REPL_2, iASSMBL_CD, iOFFPARENT_BOM_ID.getID() );
      //
      // verify evt_stage
      VerifyEvtStage( iEventIDs_OFFWING_OFFPARENT, TestConstants.STATUS_ACTV );
      VerifyEvtStage( iEventIDs_WKP, TestConstants.STATUS_ACTV );
      VerifyEvtStage( iEventIDs_REPL_1, TestConstants.STATUS_ACTV );
      VerifyEvtStage( iEventIDs_REPL_2, TestConstants.STATUS_ACTV );

      // verify evt_sched_dead

      // verify sched_part table
      // VerifySched_Part( iEventIDs_REPL_2, lPARTINFOR, TestConstants.STATUS_ACTV, "REQ" );

      // verify sched_inst_part
      // String lQuery = "select 1 from " + TableUtil.SCHED_INST_PART + " where SCHED_DB_ID="
      // + iEventIDs_REPL_2.getNO_DB_ID() + " and SCHED_ID=" + iEventIDs_REPL_2.getNO_ID();
      // Assert.assertTrue( "Check SCHED_INST_PART table to verify the record exists: 2",
      // RecordsExist( lQuery ) );

      // verify sched_rmvd_part table
      // VerifySchedRmvdPart( iEventIDs_REPL_2, lPARTINFOR, "SNAUTOTRK006" );

      // verify sched_labour
      // simpleIDs lLabourIds2 =
      // VerifySchedLabour( iEventIDs_REPL_2, TestConstants.STATUS_ACTV, "LBR" );

      // Verify sched_labour_role
      // iLabourRoleIds2 = VerifySchedLabourRole( lLabourIds2, "TECH", "TECH" );

      // Verify sched_labour_role_status
      // lQuery = "select 1 from " + TableUtil.SCHED_LABOUR_ROLE_STATUS + " where
      // LABOUR_ROLE_DB_ID="
      // + iLabourRoleIds2.getNO_DB_ID() + " and LABOUR_ROLE_ID=" + iLabourRoleIds2.getNO_ID()
      // + " and LABOUR_ROLE_STATUS_CD='ACTV'";
      // Assert.assertTrue( "Check sched_labour_role_status table to verify the record exists: 2",
      // RecordsExist( lQuery ) );

      // Verify sched_wp
      String lQuery = "select 1 from " + TableUtil.SCHED_WP + " where SCHED_DB_ID="
            + iEventIDs_WKP.getNO_DB_ID() + " and SCHED_ID=" + iEventIDs_WKP.getNO_ID();
      Assert.assertTrue( "Check sched_wp table to verify the record exists: 3",
            RecordsExist( lQuery ) );

      // Verify sched_wp_sign_req
      VerifySchedWpSignReq( iEventIDs_WKP, "AET", TestConstants.STATUS_ACTV );
      VerifySchedWpSignReq( iEventIDs_WKP, "INSP", TestConstants.STATUS_ACTV );

      // Verify sched_stask_flags
      lQuery = "select 1 from " + TableUtil.SCHED_STASK_FLAGS + " where SCHED_DB_ID="
            + iEventIDs_OFFWING_OFFPARENT.getNO_DB_ID() + " and SCHED_ID="
            + iEventIDs_OFFWING_OFFPARENT.getNO_ID();
      Assert.assertTrue( "Check SCHED_STASK_FLAGS table to verify the record exists: 1",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.SCHED_STASK_FLAGS + " where SCHED_DB_ID="
            + iEventIDs_REPL_1.getNO_DB_ID() + " and SCHED_ID=" + iEventIDs_REPL_1.getNO_ID();
      Assert.assertTrue( "Check SCHED_STASK_FLAGS table to verify the record exists: 2",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.SCHED_STASK_FLAGS + " where SCHED_DB_ID="
            + iEventIDs_REPL_2.getNO_DB_ID() + " and SCHED_ID=" + iEventIDs_REPL_2.getNO_ID();
      Assert.assertTrue( "Check SCHED_STASK_FLAGS table to verify the record exists: 3",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.SCHED_STASK_FLAGS + " where SCHED_DB_ID="
            + iEventIDs_WKP.getNO_DB_ID() + " and SCHED_ID=" + iEventIDs_WKP.getNO_ID();
      Assert.assertTrue( "Check SCHED_STASK_FLAGS table to verify the record exists: 4",
            RecordsExist( lQuery ) );

   }


   /**
    * This test is to test offwing task on SRU config slot ON ENG with NO LRU config slot before of
    * SRU (2.1, 2.2)
    *
    * @throws Exception
    *
    */
   public void testOFFWING_ENG_NOLRU_VALIDATION() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SNAUTOASSYTRK003'" );
      lMapTask.put( "PART_NO_OEM", "'E0000006'" );
      lMapTask.put( "MANUFACT_CD", "'11111'" );
      lMapTask.put( "TASK_CD", "'" + iTask_OFFWING_NOLRU + "'" );
      lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "CUSTOM_START_DT", "null" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();

      // first completed record
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SNAUTOASSYTRK003'" );
      lMapTaskSched.put( "PART_NO_OEM", "'E0000006'" );
      lMapTaskSched.put( "MANUFACT_CD", "'11111'" );
      lMapTaskSched.put( "TASK_CD", "'" + iTask_OFFWING_NOLRU + "'" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // call express validation runner and result checker with expected error code
      checkTaskValidation_EXCEPTWARNING( "PASS" );
   }


   /**
    * This test is to test offwing task on SRU config slot ON ENG with NO LRU config slot before of
    * SRU (2.1, 2.2)
    *
    * @throws Exception
    *
    */
   @Test
   public void testOFFWING_ENG_NOLRU_IMPORT() throws Exception {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOFFWING_ENG_NOLRU_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runImportTasks();

      // get all needed information
      bomPartPN lPARTINFOR = getPartIDs( "SN000007" );
      simpleIDs iTaskIDs_1 = getTaskIDs( iTask_OFFWING_NOLRU );

      // Get event IDs
      iEventIDs_OFFWING_OFFPARENT =
            getEventIDs( TestConstants.STATUS_ACTV, iEVENT_SDESC_9, "NONE" );
      iEventIDs_REPL_1 = getEventIDs( TestConstants.STATUS_ACTV, iEVENT_SDESC_16, "NONE" );
      iEventIDs_WKP = getEventIDs( TestConstants.STATUS_ACTV, iEVENT_SDESC_20, "NONE" );
      simpleIDs iEventIDs_SRV =
            getEventIDs( TestConstants.STATUS_ACTV, "ENG-ASSY-JIC (ENG-ASSY-JIC)", "NONE" );

      // Validate evt_event
      Assert.assertTrue( "check event id 1 is not null", iEventIDs_OFFWING_OFFPARENT != null );
      Assert.assertTrue( "check event id 2 is not null", iEventIDs_REPL_1 != null );
      Assert.assertTrue( "check event id 3 is not null", iEventIDs_WKP != null );

      // Verify evt_event
      VerifyEvtEvent( iEventIDs_OFFWING_OFFPARENT, iEVENT_TYPE_CD_TS, iEventIDs_WKP,
            TestConstants.STATUS_ACTV );
      VerifyEvtEvent( iEventIDs_REPL_1, iEVENT_TYPE_CD_TS, iEventIDs_REPL_1,
            TestConstants.STATUS_ACTV );
      VerifyEvtEvent( iEventIDs_WKP, iEVENT_TYPE_CD_TS, null, TestConstants.STATUS_ACTV );

      // verify sched_task table
      VerifySchedTask( iEventIDs_OFFWING_OFFPARENT, iEventIDs_OFFWING_OFFPARENT, iTaskIDs_1,
            TestConstants.TASK_CLASS_CD_REQ );
      VerifySchedTask( iEventIDs_REPL_1, iEventIDs_REPL_1, null, TestConstants.TASK_CLASS_CD_REPL );
      VerifySchedTask( iEventIDs_WKP, null, null, TestConstants.TASK_CLASS_CD_RO );

      // verify evt_event_rel
      VerifyEvtEventRel( iEventIDs_REPL_1, TestConstants.REL_TYPE_CD_WORMVL, iEventIDs_WKP );

      // // verify evt_inv
      // VerifyEvtINV( iEventIDs_OFFWING_OFFPARENT, iLRU_BOM_ID.getCD(), iLRU_BOM_ID.getID() );
      // VerifyEvtINV( iEventIDs_WKP, iLRU_BOM_ID.getCD(), iLRU_BOM_ID.getID() );
      // VerifyEvtINV( iEventIDs_REPL_1, iSYS_BOM_ID.getCD(), iSYS_BOM_ID.getID() );

      // verify evt_stage
      VerifyEvtStage( iEventIDs_OFFWING_OFFPARENT, TestConstants.STATUS_ACTV );
      VerifyEvtStage( iEventIDs_WKP, TestConstants.STATUS_ACTV );
      VerifyEvtStage( iEventIDs_REPL_1, TestConstants.STATUS_ACTV );

      // verify evt_sched_dead

      // verify sched_part table
      VerifySched_Part( iEventIDs_SRV, lPARTINFOR, TestConstants.STATUS_ACTV, "REQ" );

      // verify sched_inst_part
      String lQuery = "select 1 from " + TableUtil.SCHED_INST_PART + " where SCHED_DB_ID="
            + iEventIDs_SRV.getNO_DB_ID() + " and SCHED_ID=" + iEventIDs_SRV.getNO_ID();
      Assert.assertTrue( "Check SCHED_INST_PART table to verify the record exists: 1",
            RecordsExist( lQuery ) );

      // verify sched_rmvd_part table
      VerifySchedRmvdPart( iEventIDs_SRV, lPARTINFOR, "SN000008" );

      // verify sched_labour
      simpleIDs lLabourIds = VerifySchedLabour( iEventIDs_SRV, TestConstants.STATUS_ACTV, "LBR" );

      // Verify sched_labour_role
      iLabourRoleIds = VerifySchedLabourRole( lLabourIds, "TECH", "TECH" );

      // Verify sched_labour_role_status
      lQuery = "select 1 from " + TableUtil.SCHED_LABOUR_ROLE_STATUS + " where LABOUR_ROLE_DB_ID="
            + iLabourRoleIds.getNO_DB_ID() + " and LABOUR_ROLE_ID=" + iLabourRoleIds.getNO_ID()
            + " and LABOUR_ROLE_STATUS_CD='ACTV'";
      Assert.assertTrue( "Check sched_labour_role_status table to verify the record exists: 2",
            RecordsExist( lQuery ) );

      // Verify sched_wp
      lQuery = "select 1 from " + TableUtil.SCHED_WP + " where SCHED_DB_ID="
            + iEventIDs_WKP.getNO_DB_ID() + " and SCHED_ID=" + iEventIDs_WKP.getNO_ID();
      Assert.assertTrue( "Check sched_wp table to verify the record exists: 3",
            RecordsExist( lQuery ) );

      // Verify sched_wp_sign_req
      VerifySchedWpSignReq( iEventIDs_WKP, "AET", TestConstants.STATUS_ACTV );
      VerifySchedWpSignReq( iEventIDs_WKP, "INSP", TestConstants.STATUS_ACTV );

      // Verify sched_stask_flags
      lQuery = "select 1 from " + TableUtil.SCHED_STASK_FLAGS + " where SCHED_DB_ID="
            + iEventIDs_OFFWING_OFFPARENT.getNO_DB_ID() + " and SCHED_ID="
            + iEventIDs_OFFWING_OFFPARENT.getNO_ID();
      Assert.assertTrue( "Check SCHED_STASK_FLAGS table to verify the record exists: 4",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.SCHED_STASK_FLAGS + " where SCHED_DB_ID="
            + iEventIDs_REPL_1.getNO_DB_ID() + " and SCHED_ID=" + iEventIDs_REPL_1.getNO_ID();
      Assert.assertTrue( "Check SCHED_STASK_FLAGS table to verify the record exists: 5",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.SCHED_STASK_FLAGS + " where SCHED_DB_ID="
            + iEventIDs_WKP.getNO_DB_ID() + " and SCHED_ID=" + iEventIDs_WKP.getNO_ID();
      Assert.assertTrue( "Check SCHED_STASK_FLAGS table to verify the record exists: 5",
            RecordsExist( lQuery ) );

   }


   /**
    * This test is to test offparent task on SRU config slot with NO LRU config slot before of LRU,
    * N0 REPL task has defined in baseline. (2.1, 2.2)
    *
    * @throws Exception
    *
    *
    */
   public void testOFFPARENT_ENG_NOLRU_VALIDATION() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SNAUTOASSYTRK003'" );
      lMapTask.put( "PART_NO_OEM", "'E0000006'" );
      lMapTask.put( "MANUFACT_CD", "'11111'" );
      lMapTask.put( "TASK_CD", "'" + iTask_OFFPARENT_NOLRU_NOREPL + "'" );
      lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "CUSTOM_START_DT", "null" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();

      // first completed record
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SNAUTOASSYTRK003'" );
      lMapTaskSched.put( "PART_NO_OEM", "'E0000006'" );
      lMapTaskSched.put( "MANUFACT_CD", "'11111'" );
      lMapTaskSched.put( "TASK_CD", "'" + iTask_OFFPARENT_NOLRU_NOREPL + "'" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // call express validation runner and result checker with expected error code
      checkTaskValidation_EXCEPTWARNING( "PASS" );
   }


   /**
    * This test is to test offparent task on SRU config slot with NO LRU config slot before of LRU,
    * N0 REPL task has defined in baseline. (2.1, 2.2)
    *
    * @throws Exception
    *
    */

   @Test
   public void testOFFPARENT_ENG_NOLRU_IMPORT() throws Exception {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOFFPARENT_ENG_NOLRU_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runImportTasks();

      // get all needed information
      bomPartPN lPARTINFOR = getPartIDs( "SNAUTOASSYTRK003" );
      bomPartPN lPARTINFOR_2 = getPartIDs( "SN000007" );
      simpleIDs iTaskIDs_1 = getTaskIDs( iTask_OFFPARENT_NOLRU_NOREPL );

      // Get event IDs
      iEventIDs_OFFWING_OFFPARENT =
            getEventIDs( TestConstants.STATUS_ACTV, iEVENT_SDESC_7, "NONE" );
      iEventIDs_REPL_1 = getEventIDs( TestConstants.STATUS_ACTV, iEVENT_SDESC_16, "NONE" ); // off
                                                                                            // wing
      iEventIDs_REPL_2 = getEventIDs( TestConstants.STATUS_ACTV, iEVENT_SDESC_17, "NONE" ); // off
                                                                                            // parent
      iEventIDs_WKP = getEventIDs( TestConstants.STATUS_ACTV, iEVENT_SDESC_20, "NONE" );
      simpleIDs iEventIDs_SRV =
            getEventIDs( TestConstants.STATUS_ACTV, "ENG-ASSY-JIC (ENG-ASSY-JIC)", "NONE" );
      simpleIDs iEventIDs_SRV_2 = getEventIDs( TestConstants.STATUS_ACTV,
            "ENG-SYS-1-1-TRK-TRK-CHILD-JIC (ENG-SYS-1-1-TRK-TRK-CHILD-JIC)", "NONE" );

      // Validate evt_event
      Assert.assertTrue( "check event id 1 is not null", iEventIDs_OFFWING_OFFPARENT != null );
      Assert.assertTrue( "check event id 2 is not null", iEventIDs_REPL_1 != null );
      Assert.assertTrue( "check event id 2 is not null", iEventIDs_REPL_2 != null );
      Assert.assertTrue( "check event id 4 is not null", iEventIDs_WKP != null );

      // Verify evt_event
      VerifyEvtEvent( iEventIDs_OFFWING_OFFPARENT, iEVENT_TYPE_CD_TS, iEventIDs_WKP,
            TestConstants.STATUS_ACTV );
      VerifyEvtEvent( iEventIDs_REPL_1, iEVENT_TYPE_CD_TS, iEventIDs_REPL_1,
            TestConstants.STATUS_ACTV );
      VerifyEvtEvent( iEventIDs_REPL_2, iEVENT_TYPE_CD_TS, iEventIDs_WKP,
            TestConstants.STATUS_ACTV );
      VerifyEvtEvent( iEventIDs_WKP, iEVENT_TYPE_CD_TS, null, TestConstants.STATUS_ACTV );

      // verify sched_task table
      VerifySchedTask( iEventIDs_OFFWING_OFFPARENT, iEventIDs_OFFWING_OFFPARENT, iTaskIDs_1,
            TestConstants.TASK_CLASS_CD_REQ );
      VerifySchedTask( iEventIDs_REPL_1, iEventIDs_REPL_1, null, TestConstants.TASK_CLASS_CD_REPL );
      VerifySchedTask( iEventIDs_REPL_2, iEventIDs_REPL_2, null, TestConstants.TASK_CLASS_CD_REPL );
      VerifySchedTask( iEventIDs_WKP, null, null, TestConstants.TASK_CLASS_CD_RO );

      // verify evt_event_rel
      VerifyEvtEventRel( iEventIDs_REPL_1, TestConstants.REL_TYPE_CD_WORMVL, iEventIDs_WKP );

      // // verify evt_inv
      // VerifyEvtINV( iEventIDs_OFFWING_OFFPARENT, iLRU_BOM_ID.getCD(), iLRU_BOM_ID.getID() );
      // VerifyEvtINV( iEventIDs_WKP, iLRU_BOM_ID.getCD(), iLRU_BOM_ID.getID() );
      // VerifyEvtINV( iEventIDs_REPL_1, iSYS_BOM_ID.getCD(), iSYS_BOM_ID.getID() );

      // verify evt_stage
      VerifyEvtStage( iEventIDs_OFFWING_OFFPARENT, TestConstants.STATUS_ACTV );
      VerifyEvtStage( iEventIDs_WKP, TestConstants.STATUS_ACTV );
      VerifyEvtStage( iEventIDs_REPL_1, TestConstants.STATUS_ACTV );
      VerifyEvtStage( iEventIDs_REPL_2, TestConstants.STATUS_ACTV );

      // verify evt_sched_dead

      // verify sched_part table
      VerifySched_Part( iEventIDs_SRV_2, lPARTINFOR, TestConstants.STATUS_ACTV, "REQ" );
      VerifySched_Part( iEventIDs_SRV, lPARTINFOR_2, TestConstants.STATUS_ACTV, "REQ" );

      // verify sched_inst_part
      String lQuery = "select 1 from " + TableUtil.SCHED_INST_PART + " where SCHED_DB_ID="
            + iEventIDs_SRV_2.getNO_DB_ID() + " and SCHED_ID=" + iEventIDs_SRV_2.getNO_ID();
      Assert.assertTrue( "Check SCHED_INST_PART table to verify the record exists: 1",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.SCHED_INST_PART + " where SCHED_DB_ID="
            + iEventIDs_SRV.getNO_DB_ID() + " and SCHED_ID=" + iEventIDs_SRV.getNO_ID();
      Assert.assertTrue( "Check SCHED_INST_PART table to verify the record exists: 1",
            RecordsExist( lQuery ) );

      // verify sched_rmvd_part table
      VerifySchedRmvdPart( iEventIDs_SRV_2, lPARTINFOR, "SNAUTOASSYTRK003" );
      VerifySchedRmvdPart( iEventIDs_SRV, lPARTINFOR_2, "SN000008" );

      // verify sched_labour
      simpleIDs lLabourIds = VerifySchedLabour( iEventIDs_SRV_2, TestConstants.STATUS_ACTV, "LBR" );

      // Verify sched_labour_role
      iLabourRoleIds = VerifySchedLabourRole( lLabourIds, "TECH", "TECH" );

      // Verify sched_labour_role_status
      lQuery = "select 1 from " + TableUtil.SCHED_LABOUR_ROLE_STATUS + " where LABOUR_ROLE_DB_ID="
            + iLabourRoleIds.getNO_DB_ID() + " and LABOUR_ROLE_ID=" + iLabourRoleIds.getNO_ID()
            + " and LABOUR_ROLE_STATUS_CD='ACTV'";
      Assert.assertTrue( "Check sched_labour_role_status table to verify the record exists: 2",
            RecordsExist( lQuery ) );

      // Verify sched_wp
      lQuery = "select 1 from " + TableUtil.SCHED_WP + " where SCHED_DB_ID="
            + iEventIDs_WKP.getNO_DB_ID() + " and SCHED_ID=" + iEventIDs_WKP.getNO_ID();
      Assert.assertTrue( "Check sched_wp table to verify the record exists: 3",
            RecordsExist( lQuery ) );

      // Verify sched_wp_sign_req
      VerifySchedWpSignReq( iEventIDs_WKP, "AET", TestConstants.STATUS_ACTV );
      VerifySchedWpSignReq( iEventIDs_WKP, "INSP", TestConstants.STATUS_ACTV );

      // Verify sched_stask_flags
      lQuery = "select 1 from " + TableUtil.SCHED_STASK_FLAGS + " where SCHED_DB_ID="
            + iEventIDs_OFFWING_OFFPARENT.getNO_DB_ID() + " and SCHED_ID="
            + iEventIDs_OFFWING_OFFPARENT.getNO_ID();
      Assert.assertTrue( "Check SCHED_STASK_FLAGS table to verify the record exists: 1",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.SCHED_STASK_FLAGS + " where SCHED_DB_ID="
            + iEventIDs_REPL_1.getNO_DB_ID() + " and SCHED_ID=" + iEventIDs_REPL_1.getNO_ID();
      Assert.assertTrue( "Check SCHED_STASK_FLAGS table to verify the record exists: 2",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.SCHED_STASK_FLAGS + " where SCHED_DB_ID="
            + iEventIDs_REPL_2.getNO_DB_ID() + " and SCHED_ID=" + iEventIDs_REPL_2.getNO_ID();
      Assert.assertTrue( "Check SCHED_STASK_FLAGS table to verify the record exists: 3",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.SCHED_STASK_FLAGS + " where SCHED_DB_ID="
            + iEventIDs_WKP.getNO_DB_ID() + " and SCHED_ID=" + iEventIDs_WKP.getNO_ID();
      Assert.assertTrue( "Check SCHED_STASK_FLAGS table to verify the record exists: 4",
            RecordsExist( lQuery ) );

   }

   //
   // /**
   // * This test is to test offparent task on SRU config slot with NO LRU config slot before of
   // LRU,
   // * REPL task has defined in baseline. (4.1, 4.2)
   // *
   // * @throws Exception
   // *
   // *
   // */
   // @Test
   // public void testOFFPARENT_NOLRU_REPL_VALIDATION() throws Exception {
   //
   // // create task map
   // Map<String, String> lMapTask = new LinkedHashMap<String, String>();
   //
   // // first completed record
   // lMapTask.put( "SERIAL_NO_OEM", "'SNAUTOASSYTRK004'" );
   // lMapTask.put( "PART_NO_OEM", "'E0001006'" );
   // lMapTask.put( "MANUFACT_CD", "'11111'" );
   // lMapTask.put( "TASK_CD", "'" + iTask_OFFPARENT_NOLRU_REPL + "'" );
   // lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
   // lMapTask.put( "CUSTOM_START_DT", "null" );
   // lMapTask.put( "CUSTOM_DUE_DT", "null" );
   //
   // // insert map
   // runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );
   //
   // // create task map
   // Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();
   //
   // // first completed record
   // lMapTaskSched.put( "SERIAL_NO_OEM", "'SNAUTOASSYTRK004'" );
   // lMapTaskSched.put( "PART_NO_OEM", "'E0001006'" );
   // lMapTaskSched.put( "MANUFACT_CD", "'11111'" );
   // lMapTaskSched.put( "TASK_CD", "'" + iTask_OFFPARENT_NOLRU_REPL + "'" );
   // lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );
   //
   // // insert map
   // runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );
   //
   // // run validation
   // runValidateTasks();
   //
   // // call express validation runner and result checker with expected error code
   // validateAndCheckTask( testName.getMethodName(), "PASS" );
   // }


   /**
    * This test is to test offparent task on SRU config slot with NO LRU config slot before of SRU,
    * N0 REPL task has defined in baseline. (4.1, 4.2)
    *
    * @throws Exception
    *
    *
    */

   public void testOFFPARENT_LOOSEENG_LRU_REPL_VALIDATION() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SNAUTOASSYTRK005'" );
      lMapTask.put( "PART_NO_OEM", "'E0000006'" );
      lMapTask.put( "MANUFACT_CD", "'11111'" );
      lMapTask.put( "TASK_CD", "'" + iTask_OFFPARENT_NOLRU_NOREPL + "'" );
      lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "CUSTOM_START_DT", "null" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();

      // first completed record
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SNAUTOASSYTRK005'" );
      lMapTaskSched.put( "PART_NO_OEM", "'E0000006'" );
      lMapTaskSched.put( "MANUFACT_CD", "'11111'" );
      lMapTaskSched.put( "TASK_CD", "'" + iTask_OFFPARENT_NOLRU_NOREPL + "'" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // call express validation runner and result checker with expected error code
      // validateAndCheckTask( testName.getMethodName(), "PASS" );
      checkTaskValidation_EXCEPTWARNING( "PASS" );
   }


   /**
    * This test is to test offparent task on SRU config slot with NO LRU config slot before of SRU,
    * REPL task has defined in baseline. (2.1, 2.2) passed
    *
    * @throws Exception
    *
    *
    *
    */
   @Test
   public void testOFFPARENT_LOOSEENG_LRU_REPL_IMPORT() throws Exception {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOFFPARENT_LOOSEENG_LRU_REPL_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runImportTasks();

      // get all needed information
      bomPartPN lPARTINFOR = getPartIDs( "SNAUTOASSYTRK005" );
      simpleIDs iTaskIDs_1 = getTaskIDs( iTask_OFFPARENT_NOLRU_NOREPL );
      simpleIDs iInvIDs_ACFT = getInvIDs( "SNAUTOASSYTRK005" );
      AssmblBomID iPARENT_BOM_ID = getAssmblBomId( iASSMBL_CD_ENG, iASSMBL_BOM_CD_ENG_TRK_P1 );
      AssmblBomID iSRU_BOM_ID = getAssmblBomId( iASSMBL_CD_ENG, iASSMBL_BOM_CD_ENG_TRK_P2 );

      // Get event IDs
      iEventIDs_OFFWING_OFFPARENT =
            getEventIDs( TestConstants.STATUS_ACTV, iEVENT_SDESC_7, "NONE" );
      iEventIDs_REPL_1 = getEventIDs( TestConstants.STATUS_ACTV, iEVENT_SDESC_8, "NONE" );
      simpleIDs iEventIDs_SRV = getEventIDs( TestConstants.STATUS_ACTV,
            "ENG-SYS-1-1-TRK-TRK-CHILD-JIC (ENG-SYS-1-1-TRK-TRK-CHILD-JIC)", "NONE" );
      iEventIDs_WKP = getEventIDs( TestConstants.STATUS_ACTV, iEVENT_SDESC_22, "NONE" );

      // Validate evt_event
      Assert.assertTrue( "check event id 1 is not null", iEventIDs_OFFWING_OFFPARENT != null );
      Assert.assertTrue( "check event id 2 is not null", iEventIDs_REPL_1 != null );
      Assert.assertTrue( "check event id 3 is not null", iEventIDs_SRV != null );
      Assert.assertTrue( "check event id 4 is not null", iEventIDs_WKP != null );

      // Verify evt_event
      VerifyEvtEvent( iEventIDs_OFFWING_OFFPARENT, iEVENT_TYPE_CD_TS, iEventIDs_WKP,
            TestConstants.STATUS_ACTV );
      VerifyEvtEvent( iEventIDs_REPL_1, iEVENT_TYPE_CD_TS, iEventIDs_WKP,
            TestConstants.STATUS_ACTV );
      VerifyEvtEvent( iEventIDs_SRV, iEVENT_TYPE_CD_TS, iEventIDs_WKP, TestConstants.STATUS_ACTV );
      VerifyEvtEvent( iEventIDs_WKP, iEVENT_TYPE_CD_TS, iEventIDs_WKP, TestConstants.STATUS_ACTV );

      // verify sched_task table
      VerifySchedTask( iEventIDs_OFFWING_OFFPARENT, iEventIDs_OFFWING_OFFPARENT, iTaskIDs_1,
            TestConstants.TASK_CLASS_CD_REQ );
      VerifySchedTask( iEventIDs_REPL_1, iEventIDs_REPL_1, null, TestConstants.TASK_CLASS_CD_REPL );
      VerifySchedTask( iEventIDs_SRV, iEventIDs_REPL_1, null, TestConstants.TASK_CLASS_CD_SRVC );
      VerifySchedTask( iEventIDs_WKP, null, null, TestConstants.TASK_CLASS_CD_RO );

      // verify evt_event_rel

      // // verify evt_inv
      // VerifyEvtINV( iEventIDs_OFFWING_OFFPARENT, iSRU_BOM_ID.getCD(), iSRU_BOM_ID.getID() );
      // VerifyEvtINV( iEventIDs_REPL_1, iPARENT_BOM_ID.getCD(), iPARENT_BOM_ID.getID() );

      // verify evt_stage
      VerifyEvtStage( iEventIDs_OFFWING_OFFPARENT, TestConstants.STATUS_ACTV );
      VerifyEvtStage( iEventIDs_REPL_1, TestConstants.STATUS_ACTV );
      VerifyEvtStage( iEventIDs_SRV, TestConstants.STATUS_ACTV );
      VerifyEvtStage( iEventIDs_WKP, TestConstants.STATUS_ACTV );

      // verify sched_part table
      VerifySched_Part( iEventIDs_SRV, lPARTINFOR, TestConstants.STATUS_ACTV, "REQ" );

      // verify sched_inst_part
      String lQuery = "select 1 from " + TableUtil.SCHED_INST_PART + " where SCHED_DB_ID="
            + iEventIDs_SRV.getNO_DB_ID() + " and SCHED_ID=" + iEventIDs_SRV.getNO_ID();
      Assert.assertTrue( "Check SCHED_INST_PART table to verify the record exists: 1",
            RecordsExist( lQuery ) );

      // verify sched_rmvd_part table
      VerifySchedRmvdPart( iEventIDs_SRV, lPARTINFOR, "SNAUTOASSYTRK005" );

      // verify sched_labour
      simpleIDs lLabourIds = VerifySchedLabour( iEventIDs_SRV, TestConstants.STATUS_ACTV, "LBR" );

      // Verify sched_labour_role
      iLabourRoleIds = VerifySchedLabourRole( lLabourIds, "TECH", "TECH" );

      // Verify sched_labour_role_status
      lQuery = "select 1 from " + TableUtil.SCHED_LABOUR_ROLE_STATUS + " where LABOUR_ROLE_DB_ID="
            + iLabourRoleIds.getNO_DB_ID() + " and LABOUR_ROLE_ID=" + iLabourRoleIds.getNO_ID()
            + " and LABOUR_ROLE_STATUS_CD='ACTV'";
      Assert.assertTrue( "Check sched_labour_role_status table to verify the record exists: 2",
            RecordsExist( lQuery ) );

      // Verify sched_wp
      lQuery = "select 1 from " + TableUtil.SCHED_WP + " where SCHED_DB_ID="
            + iEventIDs_WKP.getNO_DB_ID() + " and SCHED_ID=" + iEventIDs_WKP.getNO_ID();
      Assert.assertTrue( "Check sched_wp table to verify the record exists: 3",
            RecordsExist( lQuery ) );

      // Verify sched_wp_sign_req
      VerifySchedWpSignReq( iEventIDs_WKP, "AET", TestConstants.STATUS_ACTV );
      VerifySchedWpSignReq( iEventIDs_WKP, "INSP", TestConstants.STATUS_ACTV );

      // Verify sched_stask_flags
      lQuery = "select 1 from " + TableUtil.SCHED_STASK_FLAGS + " where SCHED_DB_ID="
            + iEventIDs_OFFWING_OFFPARENT.getNO_DB_ID() + " and SCHED_ID="
            + iEventIDs_OFFWING_OFFPARENT.getNO_ID();
      Assert.assertTrue( "Check SCHED_STASK_FLAGS table to verify the record exists: 1",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.SCHED_STASK_FLAGS + " where SCHED_DB_ID="
            + iEventIDs_WKP.getNO_DB_ID() + " and SCHED_ID=" + iEventIDs_WKP.getNO_ID();
      Assert.assertTrue( "Check SCHED_STASK_FLAGS table to verify the record exists: 4",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.SCHED_STASK_FLAGS + " where SCHED_DB_ID="
            + iEventIDs_REPL_1.getNO_DB_ID() + " and SCHED_ID=" + iEventIDs_REPL_1.getNO_ID();
      Assert.assertTrue( "Check SCHED_STASK_FLAGS table to verify the record exists: 5",
            RecordsExist( lQuery ) );

   }


   /**
    * This test is to test offwing task on SRU config slot with NO LRU config slot before of SRU, N0
    * REPL task has defined in baseline. (4.1, 4.2) passed
    *
    * @throws Exception
    *
    *
    */

   public void testOFFWING_NOLRU_REPL_VALIDATION() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SNAUTOASSYTRK011'" );
      lMapTask.put( "PART_NO_OEM", "'A0002550'" );
      lMapTask.put( "MANUFACT_CD", "'11111'" );
      lMapTask.put( "TASK_CD", "'REPL_TEST9'" );
      lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "CUSTOM_START_DT", "null" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();

      // first completed record
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SNAUTOASSYTRK011'" );
      lMapTaskSched.put( "PART_NO_OEM", "'A0002550'" );
      lMapTaskSched.put( "MANUFACT_CD", "'11111'" );
      lMapTaskSched.put( "TASK_CD", "'REPL_TEST9'" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // call express validation runner and result checker with expected error code
      checkTaskValidation_EXCEPTWARNING( "pass" );
   }


   /**
    * This test is to test offwing task on SRU config slot with NO LRU config slot before of SRU,
    * REPL task has defined in baseline. (4.1, 4.2)
    *
    * @throws Exception
    *
    */
   @Test
   public void testOFFWING_NOLRU_REPL_IMPORT() throws Exception {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOFFWING_NOLRU_REPL_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runImportTasks();

      // get all needed information
      bomPartPN lPARTINFOR = getPartIDs( "SNAUTOASSYTRK011" );
      simpleIDs iTaskIDs_1 = getTaskIDs( "REPL_TEST9" );

      // Get event IDs
      iEventIDs_OFFWING_OFFPARENT =
            getEventIDs( TestConstants.STATUS_ACTV, "REPL_TEST9 (REPL_TEST9_OFFWING)", "NONE" );

      // Validate evt_event
      Assert.assertTrue( "check event id 1 is not null", iEventIDs_OFFWING_OFFPARENT != null );

      // Verify evt_event
      VerifyEvtEvent( iEventIDs_OFFWING_OFFPARENT, iEVENT_TYPE_CD_TS, iEventIDs_OFFWING_OFFPARENT,
            TestConstants.STATUS_ACTV );

      // verify sched_task table
      VerifySchedTask( iEventIDs_OFFWING_OFFPARENT, iEventIDs_OFFWING_OFFPARENT, iTaskIDs_1,
            TestConstants.TASK_CLASS_CD_REQ );

      // verify evt_stage
      VerifyEvtStage( iEventIDs_OFFWING_OFFPARENT, TestConstants.STATUS_ACTV );

      // Verify sched_stask_flags
      String lQuery = "select 1 from " + TableUtil.SCHED_STASK_FLAGS + " where SCHED_DB_ID="
            + iEventIDs_OFFWING_OFFPARENT.getNO_DB_ID() + " and SCHED_ID="
            + iEventIDs_OFFWING_OFFPARENT.getNO_ID();
      Assert.assertTrue( "Check SCHED_STASK_FLAGS table to verify the record exists: 4",
            RecordsExist( lQuery ) );

   }


   /**
    * This test is to test offwing task on SRU config slot with NO LRU config slot before of SRU, N0
    * REPL task has defined in baseline. (4.1, 4.2) passed
    *
    * @throws Exception
    *
    *
    */
   @Test
   public void testOFFPARENT_NOLRU_REPL_VALIDATION() throws Exception {

      // create task map
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();

      // first completed record
      lMapTask.put( "SERIAL_NO_OEM", "'SNAUTOASSYTRK011'" );
      lMapTask.put( "PART_NO_OEM", "'A0002550'" );
      lMapTask.put( "MANUFACT_CD", "'11111'" );
      lMapTask.put( "TASK_CD", "'REPL_TEST10'" );
      lMapTask.put( "FIRST_TIME_BOOL", "'Y'" );
      lMapTask.put( "CUSTOM_START_DT", "null" );
      lMapTask.put( "CUSTOM_DUE_DT", "null" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK, lMapTask ) );

      // create task map
      Map<String, String> lMapTaskSched = new LinkedHashMap<String, String>();

      // first completed record
      lMapTaskSched.put( "SERIAL_NO_OEM", "'SNAUTOASSYTRK011'" );
      lMapTaskSched.put( "PART_NO_OEM", "'A0002550'" );
      lMapTaskSched.put( "MANUFACT_CD", "'11111'" );
      lMapTaskSched.put( "TASK_CD", "'REPL_TEST10'" );
      lMapTaskSched.put( "SCHED_PARAMETER", "'HOURS'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_TASK_SCHED, lMapTaskSched ) );

      // run validation
      runValidateTasks();

      // call express validation runner and result checker with expected error code
      checkTaskValidation_EXCEPTWARNING( "pass" );
   }


   /**
    * This test is to test offwing task on SRU config slot with NO LRU config slot before of SRU,
    * REPL task has defined in baseline. (4.1, 4.2)
    *
    * @throws Exception
    *
    */
   @Test
   public void testOFFPARENT_NOLRU_REPL_IMPORT() throws Exception {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOFFPARENT_NOLRU_REPL_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runImportTasks();

      // get all needed information
      bomPartPN lPARTINFOR = getPartIDs( "SNAUTOASSYTRK011" );
      simpleIDs iTaskIDs_1 = getTaskIDs( "REPL_TEST10" );

      // Get event IDs
      iEventIDs_OFFWING_OFFPARENT =
            getEventIDs( TestConstants.STATUS_ACTV, "REPL_TEST10 (REPL_TEST10_OFFPARENT)", "NONE" );

      // Validate evt_event
      Assert.assertTrue( "check event id 1 is not null", iEventIDs_OFFWING_OFFPARENT != null );

      // Verify evt_event
      VerifyEvtEvent( iEventIDs_OFFWING_OFFPARENT, iEVENT_TYPE_CD_TS, iEventIDs_OFFWING_OFFPARENT,
            TestConstants.STATUS_ACTV );

      // verify sched_task table
      VerifySchedTask( iEventIDs_OFFWING_OFFPARENT, iEventIDs_OFFWING_OFFPARENT, iTaskIDs_1,
            TestConstants.TASK_CLASS_CD_REQ );

      // verify evt_stage
      VerifyEvtStage( iEventIDs_OFFWING_OFFPARENT, TestConstants.STATUS_ACTV );

      // Verify sched_stask_flags
      String lQuery = "select 1 from " + TableUtil.SCHED_STASK_FLAGS + " where SCHED_DB_ID="
            + iEventIDs_OFFWING_OFFPARENT.getNO_DB_ID() + " and SCHED_ID="
            + iEventIDs_OFFWING_OFFPARENT.getNO_ID();
      Assert.assertTrue( "Check SCHED_STASK_FLAGS table to verify the record exists: 4",
            RecordsExist( lQuery ) );

   }


   // ===================================================================

   /**
    * This function is to verify evt_sched_dead table for US
    *
    *
    */
   public void VerifySchedWpSignReq( simpleIDs aIDs, String aLABOUR_SKILL_CD,
         String aWP_SIGN_REQ_STATUS_CD ) {

      String[] iIds = { "WP_SIGN_REQ_STATUS_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "SCHED_DB_ID", aIDs.getNO_DB_ID() );
      lArgs.addArguments( "SCHED_ID", aIDs.getNO_ID() );
      lArgs.addArguments( "LABOUR_SKILL_CD", aLABOUR_SKILL_CD );

      String iQueryString =
            TableUtil.buildTableQuery( TableUtil.SCHED_WP_SIGN_REQ, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "WP_SIGN_REQ_STATUS_CD",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aWP_SIGN_REQ_STATUS_CD ) );

   }


   /**
    * This function is to verify evt_sched_dead table for US
    *
    *
    */
   public String VerifyEvtSchedDead( simpleIDs aIDs, simpleIDs aDATA_TYPE, String aSCHED_FROM_CD,
         String aSCHED_DEAD_DT ) {

      String[] iIds = { "SCHED_FROM_CD", "SCHED_DEAD_DT" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "EVENT_DB_ID", aIDs.getNO_DB_ID() );
      lArgs.addArguments( "EVENT_ID", aIDs.getNO_ID() );
      lArgs.addArguments( "DATA_TYPE_DB_ID", aDATA_TYPE.getNO_DB_ID() );
      lArgs.addArguments( "DATA_TYPE_ID", aDATA_TYPE.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EVT_SCHED_DEAD, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      if ( aSCHED_FROM_CD != null ) {
         Assert.assertTrue( "SCHED_FROM_CD",
               llists.get( 0 ).get( 0 ).equalsIgnoreCase( aSCHED_FROM_CD ) );
      }

      if ( aSCHED_DEAD_DT != null ) {
         Assert.assertTrue( "SCHED_DEAD_DT",
               llists.get( 0 ).get( 1 ).equalsIgnoreCase( aSCHED_DEAD_DT ) );
      }

      return llists.get( 0 ).get( 1 );
   }


   /**
    * This function is to verify sched_labour_role table
    *
    *
    */
   public simpleIDs VerifySchedLabourRole( simpleIDs aIDs, String aLABOUR_ROLE_TYPE_CD,
         String aLABOUR_ROLE_TPE_CD ) {

      String[] iIds = { "LABOUR_ROLE_DB_ID", "LABOUR_ROLE_ID", "LABOUR_ROLE_TYPE_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "LABOUR_DB_ID", aIDs.getNO_DB_ID() );
      lArgs.addArguments( "LABOUR_ID", aIDs.getNO_ID() );
      lArgs.addArguments( "LABOUR_ROLE_TYPE_CD", aLABOUR_ROLE_TPE_CD );

      String iQueryString =
            TableUtil.buildTableQuery( TableUtil.SCHED_LABOUR_ROLE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      Assert.assertTrue( "LABOUR_ROLE_TYPE_CD",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aLABOUR_ROLE_TYPE_CD ) );

      return lIds;
   }


   /**
    * This function is to verify sched_labour table
    *
    *
    */
   public simpleIDs VerifySchedLabour( simpleIDs aIDs, String aLABOUR_STAGE_CD,
         String aLABOUR_SKILL_CD ) {

      String[] iIds = { "LABOUR_DB_ID", "LABOUR_ID", "LABOUR_STAGE_CD", "LABOUR_SKILL_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "SCHED_DB_ID", aIDs.getNO_DB_ID() );
      lArgs.addArguments( "SCHED_ID", aIDs.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.SCHED_LABOUR, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      Assert.assertTrue( "LABOUR_STAGE_CD",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aLABOUR_STAGE_CD ) );
      Assert.assertTrue( "LABOUR_SKILL_CD",
            llists.get( 0 ).get( 3 ).equalsIgnoreCase( aLABOUR_SKILL_CD ) );

      return lIds;
   }


   /**
    * This function is to verify sched_rmvd_part table
    *
    *
    */
   public void VerifySchedRmvdPart( simpleIDs aIDs, bomPartPN aPartInfor, String aSERIAL_NO_OEM ) {

      String[] iIds = { "SERIAL_NO_OEM" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "SCHED_DB_ID", aIDs.getNO_DB_ID() );
      lArgs.addArguments( "SCHED_ID", aIDs.getNO_ID() );
      lArgs.addArguments( "PART_NO_DB_ID", aPartInfor.getPART_NO_DB_ID() );
      lArgs.addArguments( "PART_NO_ID", aPartInfor.getPART_NO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.SCHED_RMVD_PART, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      if ( aSERIAL_NO_OEM != null ) {
         Assert.assertTrue( "SERIAL_NO_OEM",
               llists.get( 0 ).get( 0 ).equalsIgnoreCase( aSERIAL_NO_OEM ) );
      }
   }


   /**
    * This function is to verify sched_part table
    *
    *
    */
   public void VerifySched_Part( simpleIDs aIDs, bomPartPN aPartInfor, String aEVENT_STATUS_CD,
         String aREQ_ACTION_CD ) {

      String[] iIds = { "SCHED_PART_STATUS_CD", "REQ_ACTION_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "SCHED_DB_ID", aIDs.getNO_DB_ID() );
      lArgs.addArguments( "SCHED_ID", aIDs.getNO_ID() );
      lArgs.addArguments( "SCHED_BOM_PART_DB_ID", aPartInfor.getBOM_PART_DB_ID() );
      lArgs.addArguments( "SCHED_BOM_PART_ID", aPartInfor.getBOM_PART_ID() );
      // lArgs.addArguments( "SPEC_PART_NO_DB_ID", aPartInfor.getPART_NO_DB_ID() );
      // lArgs.addArguments( "SPEC_PART_NO_ID", aPartInfor.getPART_NO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.SCHED_PART, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "SCHED_PART_STATUS_CD",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aEVENT_STATUS_CD ) );
      Assert.assertTrue( "REQ_ACTION_CD",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( aREQ_ACTION_CD ) );
   }


   /**
    * This function is to verify sched_part table
    *
    *
    */
   public void VerifySched_Part( simpleIDs aIDs, String aEVENT_STATUS_CD, String aREQ_ACTION_CD ) {

      String[] iIds = { "SCHED_PART_STATUS_CD", "REQ_ACTION_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "SCHED_DB_ID", aIDs.getNO_DB_ID() );
      lArgs.addArguments( "SCHED_ID", aIDs.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.SCHED_PART, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "SCHED_PART_STATUS_CD",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aEVENT_STATUS_CD ) );
      Assert.assertTrue( "REQ_ACTION_CD",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( aREQ_ACTION_CD ) );
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
    * This function is to verify evt_inv table
    *
    *
    */
   public void VerifyEvtINV( simpleIDs aIDs, String aASSMBL_CD, String aASSMBL_BOM_ID ) {

      String[] iIds = { "ASSMBL_CD", "ASSMBL_BOM_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "EVENT_DB_ID", aIDs.getNO_DB_ID() );
      lArgs.addArguments( "EVENT_ID", aIDs.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EVT_INV, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "ASSMBL_CD", llists.get( 0 ).get( 0 ).equalsIgnoreCase( aASSMBL_CD ) );
      Assert.assertTrue( "ASSMBL_BOM_ID",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( aASSMBL_BOM_ID ) );
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
    * This function is to verify sched_task table
    *
    *
    */
   public void VerifySchedTask( simpleIDs aIDs, simpleIDs aHIDs, simpleIDs aTaskIDs,
         String aTASK_CLASS_CD ) {

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

      if ( aTaskIDs != null ) {
         Assert.assertTrue( "TASK_DB_ID",
               llists.get( 0 ).get( 2 ).equalsIgnoreCase( aTaskIDs.getNO_DB_ID() ) );
         Assert.assertTrue( "TASK_ID",
               llists.get( 0 ).get( 3 ).equalsIgnoreCase( aTaskIDs.getNO_ID() ) );
      }
      Assert.assertTrue( "TASK_CLASS_CD",
            llists.get( 0 ).get( 4 ).equalsIgnoreCase( aTASK_CLASS_CD ) );
   }


   /**
    * This function is to verify evt_event table
    *
    *
    */

   public void VerifyEvtEvent( simpleIDs aIDs, String aEVENT_TYPE_CD, simpleIDs aHIDs,
         String aEVENT_STATUS_CD ) {

      String[] iIds = { "EVENT_TYPE_CD", "H_EVENT_DB_ID", "H_EVENT_ID", "EVENT_STATUS_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "EVENT_DB_ID", aIDs.getNO_DB_ID() );
      lArgs.addArguments( "EVENT_ID", aIDs.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EVT_EVENT, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "EVENT_TYPE_CD",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aEVENT_TYPE_CD ) );
      if ( aHIDs != null ) {
         Assert.assertTrue( "H_EVENT_DB_ID",
               llists.get( 0 ).get( 1 ).equalsIgnoreCase( aHIDs.getNO_DB_ID() ) );
         Assert.assertTrue( "H_EVENT_ID",
               llists.get( 0 ).get( 2 ).equalsIgnoreCase( aHIDs.getNO_ID() ) );
      }
      Assert.assertTrue( "EVENT_STATUS_CD",
            llists.get( 0 ).get( 3 ).equalsIgnoreCase( aEVENT_STATUS_CD ) );
   }


   /**
    * This function is to retrieve partNO and partGP ID by giving serial_no_oem
    *
    *
    *
    */
   public bomPartPN getPartIDs( String aSerial_NO_OEM ) {

      String[] iIds = { "BOM_PART_DB_ID", "BOM_PART_ID", "PART_NO_DB_ID", "PART_NO_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "SERIAL_NO_OEM", aSerial_NO_OEM );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.INV_INV, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      bomPartPN lIds = new bomPartPN( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ),
            llists.get( 0 ).get( 2 ), llists.get( 0 ).get( 3 ) );

      return lIds;
   }


   /**
    * This function is to retrieve event ID by giving event_sdesc and task status.
    *
    *
    *
    */
   @Override
   public simpleIDs getEventIDs( String aStatus, String aEVENT_SDESC, String aSCHED_PRIORITY_CD ) {

      String[] iIds = { "EVENT_DB_ID", "EVENT_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      String iQueryString =
            "SELECT EVENT_DB_ID, EVENT_ID FROM (SELECT * FROM evt_event WHERE event_status_cd = '"
                  + aStatus + "' and trim(upper(EVENT_SDESC))='" + aEVENT_SDESC
                  + "' and SCHED_PRIORITY_CD ='" + aSCHED_PRIORITY_CD
                  + "' ORDER BY creation_dt DESC) WHERE ROWNUM = 1";

      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lIds;
   }


   /**
    * This function is to retrieve event ID by giving event_sdesc and task status.
    *
    *
    *
    */
   public simpleIDs getEventIDs3( String aStatus, String aEVENT_SDESC, String aSCHED_PRIORITY_CD,
         simpleIDs aWKPIDs ) {

      String[] iIds = { "EVENT_DB_ID", "EVENT_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      String iQueryString =
            "SELECT EVENT_DB_ID, EVENT_ID FROM (SELECT * FROM evt_event WHERE event_status_cd = '"
                  + aStatus + "' and trim(upper(EVENT_SDESC))='" + aEVENT_SDESC
                  + "' and SCHED_PRIORITY_CD ='" + aSCHED_PRIORITY_CD + "' and H_EVENT_ID<>"
                  + aWKPIDs.getNO_ID() + " ORDER BY creation_dt DESC) WHERE ROWNUM = 1";

      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lIds;
   }


   /**
    * This function is to retrieve event ID by giving event_sdesc and task status.
    *
    *
    *
    */

   public simpleIDs getEventIDs2( String aStatus, String aEVENT_SDESC ) {

      String[] iIds = { "EVENT_DB_ID", "EVENT_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      String iQueryString =
            "SELECT EVENT_DB_ID, EVENT_ID FROM (SELECT * FROM evt_event WHERE event_status_cd = '"
                  + aStatus + "' and EVENT_ID = H_EVENT_ID and trim(upper(EVENT_SDESC))='"
                  + aEVENT_SDESC + "' ORDER BY creation_dt DESC) WHERE ROWNUM = 1";

      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lIds;
   }


   /**
    * This function is to retrieve event ID by giving event_sdesc and task status.
    *
    *
    *
    */

   public simpleIDs getEventIDs2( String aStatus, String aEVENT_SDESC, simpleIDs aWKPID ) {

      String[] iIds = { "EVENT_DB_ID", "EVENT_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      String iQueryString =
            "SELECT EVENT_DB_ID, EVENT_ID FROM (SELECT * FROM evt_event WHERE event_status_cd = '"
                  + aStatus + "' and H_EVENT_ID = '" + aWKPID.getNO_ID()
                  + "' and trim(upper(EVENT_SDESC))='" + aEVENT_SDESC
                  + "' ORDER BY creation_dt DESC) WHERE ROWNUM = 1";

      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lIds;
   }


   public void dataSetup_LRU_NOREPL() {

      String lquery =
            "SELECT task_cd FROM task_task WHERE assmbl_cd='ACFT_CD1' AND Assmbl_bom_id=(SELECT Assmbl_bom_id FROM eqp_assmbl_bom WHERE assmbl_cd ='ACFT_CD1' AND ASSMBL_BOM_CD='SYS-1-1') "
                  + " AND repl_assmbl_bom_id=(SELECT Assmbl_bom_id FROM eqp_assmbl_bom WHERE assmbl_cd ='ACFT_CD1' AND ASSMBL_BOM_CD='ACFT-SYS-1-1-TRK-TRK-TRK-PARENT')";

      if ( RecordsExist( lquery ) ) {
         ArrayList<String> mycolumns = new ArrayList<String>();
         mycolumns.add( "TASK_CD" );

         iTask_resume_LRU_NOREPL_List = execute( lquery, mycolumns );
         for ( int i = 0; i < iTask_resume_LRU_NOREPL_List.size(); i++ ) {
            lquery = "UPDATE task_task set repl_assmbl_bom_id=null  WHERE assmbl_cd='ACFT_CD1' AND "
                  + " AND Assmbl_bom_id=(SELECT Assmbl_bom_id FROM eqp_assmbl_bom WHERE assmbl_cd ='ACFT_CD1' AND ASSMBL_BOM_CD='SYS-1-1')"
                  + " AND task_cd='" + iTask_resume_LRU_NOREPL_List.get( i ).get( 0 ) + "'";
            runUpdate( lquery );
         }
      }

      lquery =
            "UPDATE evt_event SET EVENT_STATUS_CD='COMPLETE', HIST_BOOL='1' WHERE event_sdesc='AUTOWKPKG_FAULT'";
      runUpdate( lquery );

   }


   public void dataSetup_SRU_NOREPL_OFFWING() {

      String lquery =
            "SELECT task_cd FROM task_task WHERE assmbl_cd='ACFT_CD1' AND Assmbl_bom_id=(SELECT Assmbl_bom_id FROM eqp_assmbl_bom WHERE assmbl_cd ='ACFT_CD1' AND ASSMBL_BOM_CD='ACFT-SYS-1-1-TRK-TRK-TRK-PARENT') "
                  + " AND repl_assmbl_bom_id=(SELECT Assmbl_bom_id FROM eqp_assmbl_bom WHERE assmbl_cd ='ACFT_CD1' AND ASSMBL_BOM_CD='ACFT-SYS-1-1-TRK-TRK-TRK-MID')";

      if ( RecordsExist( lquery ) ) {
         ArrayList<String> mycolumns = new ArrayList<String>();
         mycolumns.add( "TASK_CD" );

         iTask_resume_SRU_NOREPL_OFFWING_List = execute( lquery, mycolumns );
         for ( int i = 0; i < iTask_resume_SRU_NOREPL_OFFWING_List.size(); i++ ) {
            lquery = "UPDATE task_task set repl_assmbl_bom_id=null  WHERE assmbl_cd='ACFT_CD1' AND "
                  + " Assmbl_bom_id=(SELECT Assmbl_bom_id FROM eqp_assmbl_bom WHERE assmbl_cd ='ACFT_CD1' AND ASSMBL_BOM_CD='ACFT-SYS-1-1-TRK-TRK-TRK-PARENT')"
                  + " AND task_cd='" + iTask_resume_SRU_NOREPL_OFFWING_List.get( i ).get( 0 ) + "'";
            runUpdate( lquery );
         }
      }
   }


   public void dataSetup_SRU_NOREPL_OFFPARENT() {

      String lquery =
            "SELECT task_cd FROM task_task WHERE assmbl_cd='ACFT_CD1' AND Assmbl_bom_id=(SELECT Assmbl_bom_id FROM eqp_assmbl_bom WHERE assmbl_cd ='ACFT_CD1' AND ASSMBL_BOM_CD='ACFT-SYS-1-1-TRK-TRK-TRK-MID') "
                  + " AND repl_assmbl_bom_id=(SELECT Assmbl_bom_id FROM eqp_assmbl_bom WHERE assmbl_cd ='ACFT_CD1' AND ASSMBL_BOM_CD='ACFT-SYS-1-1-TRK-TRK-TRK-CHILD')";

      if ( RecordsExist( lquery ) ) {
         ArrayList<String> mycolumns = new ArrayList<String>();
         mycolumns.add( "TASK_CD" );

         iTask_resume_SRU_NOREPL_OFFPARENT_List = execute( lquery, mycolumns );
         for ( int i = 0; i < iTask_resume_SRU_NOREPL_OFFPARENT_List.size(); i++ ) {
            lquery = "UPDATE task_task set repl_assmbl_bom_id=null  WHERE assmbl_cd='ACFT_CD1' AND "
                  + " Assmbl_bom_id=(SELECT Assmbl_bom_id FROM eqp_assmbl_bom WHERE assmbl_cd ='ACFT_CD1' AND ASSMBL_BOM_CD='ACFT-SYS-1-1-TRK-TRK-TRK-MID')"
                  + " AND task_cd='" + iTask_resume_SRU_NOREPL_OFFPARENT_List.get( i ).get( 0 )
                  + "'";
            runUpdate( lquery );
         }
      }
   }


   public void dataSetup_RESTORE_LRU_REPL( String aValue ) {
      String lquery = "UPDATE task_task set TASK_MUST_REMOVE_CD='" + aValue
            + "' where assmbl_cd='ACFT_CD1' and Assmbl_bom_id=(SELECT Assmbl_bom_id FROM eqp_assmbl_bom WHERE assmbl_cd ='ACFT_CD1' AND ASSMBL_BOM_CD='ACFT-SYS-1-1-TRK-P2') "
            + " and task_cd='" + iTask_LRU_REPL + "'";
      runUpdate( lquery );

   }


   public void dataSetup_NOLRU( String aValue ) {
      String lquery = "UPDATE eqp_bom_part set LRU_BOOL='" + aValue
            + "' where assmbl_cd='ACFT_11' and BOM_PART_CD in ('ACFT-SYS-1-TRK-1', 'ACFT-SYS-1-TRK-1-1')";
      runUpdate( lquery );

   }


   public void updateWKPStatus() {
      String lquery =
            "UPDATE evt_event SET EVENT_STATUS_CD='COMPLETE', HIST_BOOL='1' WHERE event_sdesc='AUTOTRK_WKPKG1'";
      runUpdate( lquery );

      lquery =
            "UPDATE evt_event SET EVENT_STATUS_CD='COMPLETE', HIST_BOOL='1' WHERE event_sdesc='AUTOWKPKG_FAULT'";
      runUpdate( lquery );

   }


   public void emptyList() {
      iTask_resume_LRU_NOREPL_List = null;
      iTask_resume_SRU_NOREPL_OFFWING_List = null;
      iTask_resume_SRU_NOREPL_OFFPARENT_List = null;
      iTaskIDs = null;
      iLabourIDs = null;
      iLabourRoleListIDs = null;
      iEventIDs_OFFWING_OFFPARENT = null;
      iEventIDs_REPL_1 = null;
      iEventIDs_REPL_2 = null;
      iEventIDs_PART_REQ = null;
      iEventIDs_PART_REQ_2 = null;
      iEventIDs_WKP = null;
      iLabourRoleIds = null;
      iLabourRoleIds2 = null;

   }


   public void setupDataBase() {
      String lquery = "UPDATE mim_local_db SET DB_ID=1 WHERE DB_ID=4650";
      runUpdate( lquery );
   }


   public void RestoreData() {

      if ( iTask_resume_LRU_NOREPL_List != null ) {
         for ( int i = 0; i < iTask_resume_LRU_NOREPL_List.size(); i++ ) {
            String lquery =
                  "UPDATE task_task set repl_assmbl_bom_id=(SELECT Assmbl_bom_id FROM eqp_assmbl_bom WHERE assmbl_cd ='ACFT_CD1' AND ASSMBL_BOM_CD='ACFT-SYS-1-1-TRK-TRK-TRK-PARENT')"
                        + " where assmbl_cd='ACFT_CD1' and AND Assmbl_bom_id=(SELECT Assmbl_bom_id FROM eqp_assmbl_bom WHERE assmbl_cd ='ACFT_CD1' AND ASSMBL_BOM_CD='SYS-1-1')"
                        + " and task_cd='" + iTask_resume_LRU_NOREPL_List.get( i ).get( 0 ) + "'";
            runUpdate( lquery );
         }
      }

      if ( iTask_resume_SRU_NOREPL_OFFWING_List != null ) {
         for ( int i = 0; i < iTask_resume_SRU_NOREPL_OFFWING_List.size(); i++ ) {
            String lquery =
                  "UPDATE task_task set repl_assmbl_bom_id=(SELECT Assmbl_bom_id FROM eqp_assmbl_bom WHERE assmbl_cd ='ACFT_CD1' AND ASSMBL_BOM_CD='ACFT-SYS-1-1-TRK-TRK-TRK-MID')"
                        + " where assmbl_cd='ACFT_CD1' and Assmbl_bom_id=(SELECT Assmbl_bom_id FROM eqp_assmbl_bom WHERE assmbl_cd ='ACFT_CD1' AND ASSMBL_BOM_CD='ACFT-SYS-1-1-TRK-TRK-TRK-PARENT')"
                        + " and task_cd='" + iTask_resume_SRU_NOREPL_OFFWING_List.get( i ).get( 0 )
                        + "'";
            runUpdate( lquery );
         }
      }

      if ( iTask_resume_SRU_NOREPL_OFFPARENT_List != null ) {
         for ( int i = 0; i < iTask_resume_SRU_NOREPL_OFFPARENT_List.size(); i++ ) {
            String lquery =
                  "UPDATE task_task set repl_assmbl_bom_id=(SELECT Assmbl_bom_id FROM eqp_assmbl_bom WHERE assmbl_cd ='ACFT_CD1' AND ASSMBL_BOM_CD='ACFT-SYS-1-1-TRK-TRK-TRK-CHILD')"
                        + " where assmbl_cd='ACFT_CD1' and Assmbl_bom_id=(SELECT Assmbl_bom_id FROM eqp_assmbl_bom WHERE assmbl_cd ='ACFT_CD1' AND ASSMBL_BOM_CD='ACFT-SYS-1-1-TRK-TRK-TRK-MID')"
                        + " and task_cd='"
                        + iTask_resume_SRU_NOREPL_OFFPARENT_List.get( i ).get( 0 ) + "'";
            runUpdate( lquery );
         }
      }

      String lquery =
            "UPDATE evt_event SET EVENT_STATUS_CD='ACTV', HIST_BOOL='0' WHERE event_sdesc='AUTOTRK_WKPKG1'";
      runUpdate( lquery );

      lquery =
            "UPDATE evt_event SET EVENT_STATUS_CD='ACTV', HIST_BOOL='0' WHERE event_sdesc='AUTOWKPKG_FAULT'";
      runUpdate( lquery );

      // clearEvent( iEventIDs_OFFWING_OFFPARENT );
      // clearEvent( iEventIDs_REPL_1 );
      // clearEvent( iEventIDs_REPL_2 );
      // clearEvent( iEventIDs_WKP );
      // clearEvent( iEventIDs_PART_REQ );
      // clearLabour( iLabourRoleIds );

      clearEvent2();
      clearLabour2();

      // Restore DB
      lquery = "UPDATE mim_local_db SET DB_ID=4650 WHERE DB_ID=1";
      runUpdate( lquery );

   }


   public void clearEvent2() {

      // delete evt_sched_dead
      String lStrDelete = "delete from " + TableUtil.EVT_SCHED_DEAD + "  where EVENT_DB_ID=1";
      executeSQL( lStrDelete );

      // delete evt_inv_usage
      lStrDelete = "delete from " + TableUtil.EVT_INV_USAGE + "  where EVENT_DB_ID=1";
      executeSQL( lStrDelete );

      // delete evt_stage
      lStrDelete = "delete from " + TableUtil.EVT_STAGE + "  where EVENT_DB_ID=1";
      executeSQL( lStrDelete );

      // delete evt_inv
      lStrDelete = "delete from " + TableUtil.EVT_INV + "  where EVENT_DB_ID=1";
      executeSQL( lStrDelete );

      // delete evt_event_rel
      lStrDelete = "delete from " + TableUtil.EVT_EVENT_REL + "  where EVENT_DB_ID=1";
      executeSQL( lStrDelete );

      lStrDelete = "delete from " + TableUtil.EVT_EVENT_REL + "  where REL_EVENT_DB_ID=1";
      executeSQL( lStrDelete );

      // delete sched_stask
      lStrDelete = "delete from " + TableUtil.SCHED_STASK + "  where SCHED_DB_ID=1";
      executeSQL( lStrDelete );

      // delete evt_event
      lStrDelete = "delete from " + TableUtil.EVT_EVENT + "  where EVENT_DB_ID=1";
      executeSQL( lStrDelete );

      // delete evt_event
      lStrDelete = "delete from " + TableUtil.EVT_EVENT + "  where EVENT_DB_ID=1";
      executeSQL( lStrDelete );

      // delete sched_part
      lStrDelete = "delete from " + TableUtil.SCHED_PART + "  where SCHED_DB_ID=1";
      executeSQL( lStrDelete );

      // delete sched_inst_part
      lStrDelete = "delete from " + TableUtil.SCHED_INST_PART + "  where SCHED_DB_ID=1";
      executeSQL( lStrDelete );

      // delete sched_rmvd_part
      lStrDelete = "delete from " + TableUtil.SCHED_RMVD_PART + "  where SCHED_DB_ID=1";
      executeSQL( lStrDelete );

      // delete sched_labour
      lStrDelete = "delete from " + TableUtil.SCHED_LABOUR + "  where SCHED_DB_ID=1";
      executeSQL( lStrDelete );

      // delete sched_wp
      lStrDelete = "delete from " + TableUtil.SCHED_WP + "  where SCHED_DB_ID=1";
      executeSQL( lStrDelete );

      // delete sched_wp_sign_req
      lStrDelete = "delete from " + TableUtil.SCHED_WP_SIGN_REQ + "  where SCHED_DB_ID=1";
      executeSQL( lStrDelete );

      // delete sched_stask_flags
      lStrDelete = "delete from " + TableUtil.SCHED_STASK_FLAGS + "  where SCHED_DB_ID=1";
      executeSQL( lStrDelete );

      // delete req_part
      lStrDelete = "delete from " + TableUtil.REQ_PART + "  where REQ_PART_DB_ID=1";
      executeSQL( lStrDelete );

   }


   public void clearEvent( simpleIDs aEvent_ID ) {

      if ( aEvent_ID != null ) {

         // delete evt_sched_dead
         String lStrDelete = "delete from " + TableUtil.EVT_SCHED_DEAD + "  where EVENT_DB_ID="
               + aEvent_ID.getNO_DB_ID() + " and EVENT_ID=" + aEvent_ID.getNO_ID();
         executeSQL( lStrDelete );

         // delete evt_inv_usage
         lStrDelete = "delete from " + TableUtil.EVT_INV_USAGE + "  where EVENT_DB_ID="
               + aEvent_ID.getNO_DB_ID() + " and EVENT_ID=" + aEvent_ID.getNO_ID();
         executeSQL( lStrDelete );

         // delete evt_stage
         lStrDelete = "delete from " + TableUtil.EVT_STAGE + "  where EVENT_DB_ID="
               + aEvent_ID.getNO_DB_ID() + " and EVENT_ID=" + aEvent_ID.getNO_ID();
         executeSQL( lStrDelete );

         // delete evt_inv
         lStrDelete = "delete from " + TableUtil.EVT_INV + "  where EVENT_DB_ID="
               + aEvent_ID.getNO_DB_ID() + " and EVENT_ID=" + aEvent_ID.getNO_ID();
         executeSQL( lStrDelete );

         // delete evt_event_rel
         lStrDelete = "delete from " + TableUtil.EVT_EVENT_REL + "  where EVENT_DB_ID="
               + aEvent_ID.getNO_DB_ID() + " and EVENT_ID=" + aEvent_ID.getNO_ID();
         executeSQL( lStrDelete );

         // delete sched_stask
         lStrDelete = "delete from " + TableUtil.SCHED_STASK + "  where SCHED_DB_ID="
               + aEvent_ID.getNO_DB_ID() + " and SCHED_ID=" + aEvent_ID.getNO_ID();
         executeSQL( lStrDelete );

         // delete evt_event
         lStrDelete = "delete from " + TableUtil.EVT_EVENT + "  where EVENT_DB_ID="
               + aEvent_ID.getNO_DB_ID() + " and EVENT_ID=" + aEvent_ID.getNO_ID();
         executeSQL( lStrDelete );

         // delete evt_event
         lStrDelete = "delete from " + TableUtil.EVT_EVENT + "  where EVENT_DB_ID="
               + aEvent_ID.getNO_DB_ID() + " and EVENT_ID=" + aEvent_ID.getNO_ID();
         executeSQL( lStrDelete );

         // delete sched_part
         lStrDelete = "delete from " + TableUtil.SCHED_PART + "  where SCHED_DB_ID="
               + aEvent_ID.getNO_DB_ID() + " and SCHED_ID=" + aEvent_ID.getNO_ID();
         executeSQL( lStrDelete );

         // delete sched_inst_part
         lStrDelete = "delete from " + TableUtil.SCHED_INST_PART + "  where SCHED_DB_ID="
               + aEvent_ID.getNO_DB_ID() + " and SCHED_ID=" + aEvent_ID.getNO_ID();
         executeSQL( lStrDelete );

         // delete sched_rmvd_part
         lStrDelete = "delete from " + TableUtil.SCHED_RMVD_PART + "  where SCHED_DB_ID="
               + aEvent_ID.getNO_DB_ID() + " and SCHED_ID=" + aEvent_ID.getNO_ID();
         executeSQL( lStrDelete );

         // delete sched_labour
         lStrDelete = "delete from " + TableUtil.SCHED_LABOUR + "  where SCHED_DB_ID="
               + aEvent_ID.getNO_DB_ID() + " and SCHED_ID=" + aEvent_ID.getNO_ID();
         executeSQL( lStrDelete );

         // delete sched_wp
         lStrDelete = "delete from " + TableUtil.SCHED_WP + "  where SCHED_DB_ID="
               + aEvent_ID.getNO_DB_ID() + " and SCHED_ID=" + aEvent_ID.getNO_ID();
         executeSQL( lStrDelete );

         // delete sched_wp_sign_req
         lStrDelete = "delete from " + TableUtil.SCHED_WP_SIGN_REQ + "  where SCHED_DB_ID="
               + aEvent_ID.getNO_DB_ID() + " and SCHED_ID=" + aEvent_ID.getNO_ID();
         executeSQL( lStrDelete );

         // delete sched_stask_flags
         lStrDelete = "delete from " + TableUtil.SCHED_STASK_FLAGS + "  where SCHED_DB_ID="
               + aEvent_ID.getNO_DB_ID() + " and SCHED_ID=" + aEvent_ID.getNO_ID();
         executeSQL( lStrDelete );

         // delete req_part
         lStrDelete = "delete from " + TableUtil.REQ_PART + "  where REQ_PART_DB_ID="
               + aEvent_ID.getNO_DB_ID() + " and REQ_PART_ID=" + aEvent_ID.getNO_ID();
         executeSQL( lStrDelete );
      }
   }


   public void clearLabour( simpleIDs aLabour_role_ID ) {

      if ( aLabour_role_ID != null ) {

         // delete sched_labour_role
         String lStrDelete = "delete from " + TableUtil.SCHED_LABOUR_ROLE
               + "  where LABOUR_ROLE_DB_ID=" + aLabour_role_ID.getNO_DB_ID()
               + " and LABOUR_ROLE_ID=" + aLabour_role_ID.getNO_ID();
         executeSQL( lStrDelete );

         // delete sched_labour_role_status
         lStrDelete = "delete from " + TableUtil.SCHED_LABOUR_ROLE_STATUS
               + "  where LABOUR_ROLE_DB_ID=" + aLabour_role_ID.getNO_DB_ID()
               + " and LABOUR_ROLE_ID=" + aLabour_role_ID.getNO_ID();
         executeSQL( lStrDelete );
      }
   }


   public void clearLabour2() {

      // delete sched_labour_role
      String lStrDelete =
            "delete from " + TableUtil.SCHED_LABOUR_ROLE + "  where LABOUR_ROLE_DB_ID=1";
      executeSQL( lStrDelete );

      // delete sched_labour_role_status
      lStrDelete =
            "delete from " + TableUtil.SCHED_LABOUR_ROLE_STATUS + "  where LABOUR_ROLE_DB_ID=1";
      executeSQL( lStrDelete );

   }


   public ArrayList<simpleIDs> GetAllEventIDs( simpleIDs aH_EVENT_ID ) {

      String[] iIds = { "EVENT_DB_ID", "EVENT_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "H_EVENT_DB_ID", aH_EVENT_ID.getNO_DB_ID() );
      lArgs.addArguments( "H_EVENT_ID", aH_EVENT_ID.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EVT_EVENT, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      ArrayList<simpleIDs> myList = new ArrayList<simpleIDs>();

      for ( int i = 0; i < llists.size(); i++ ) {
         myList.add( new simpleIDs( llists.get( i ).get( 0 ), llists.get( i ).get( 1 ) ) );
      }

      return myList;
   }


   public ArrayList<simpleIDs> GetLabourIDs( simpleIDs aH_EVENT_ID ) {
      String lQUERY =
            "SELECT LABOUR_DB_ID, LABOUR_ID FROM sched_labour WHERE (sched_db_id,sched_id) IN (SELECT event_db_id, event_id FROM evt_event WHERE H_EVENT_DB_ID='"
                  + aH_EVENT_ID.getNO_DB_ID() + "' AND H_EVENT_ID='" + aH_EVENT_ID.getNO_ID()
                  + "')";
      return execute2( lQUERY, Arrays.asList( "LABOUR_DB_ID", "LABOUR_ID" ) );
   }


   public ArrayList<simpleIDs> GetLabourRoleList( List<simpleIDs> aLabourId ) {
      String lQUERY =
            "SELECT LABOUR_ROLE_DB_ID, LABOUR_ROLE_ID FROM sched_labour_role WHERE (labour_db_id,labour_id) IN (";

      for ( simpleIDs myId : aLabourId ) {
         lQUERY = lQUERY + "(" + myId.getNO_DB_ID() + "," + myId.getNO_ID() + "),";
      }
      lQUERY = lQUERY.substring( 0, lQUERY.length() - 1 ) + ")";

      return execute2( lQUERY, Arrays.asList( "LABOUR_ROLE_DB_ID", "LABOUR_ROLE_ID" ) );
   }

}
