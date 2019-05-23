package com.mxi.mx.util;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;

import com.mxi.mx.core.maint.plan.datamodels.AssmblBomID;
import com.mxi.mx.core.maint.plan.datamodels.inventoryData;
import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;


/**
 * This test suite contains common test utilities for actual loader: WORK PACKAGE tests and data
 * setup/clean up for each test cases.
 *
 * @author Alicia Qian
 */
public class WorkPKGTest extends ActualsLoaderTest {

   public ValidationAndImport ivalidationandimport;

   // variables in AL_WORK_PACKAGE
   public String iWKP_SERIAL_NO_OEM_1 = "SN000001";
   public String iWKP_PART_NO_OEM_1 = "ACFT_ASSY_PN1";
   public String iWKP_MANUFACT_CD_1 = "10001";
   public String iWKP_NAME_1 = "AUTOPKGNAME1";
   public String iWKP_NAME_1_2 = "AUTOTASKWKPKG1";
   public String iWKP_NAME_1_3 = "AUTOFAULTWKPKG1";
   public String iWKP_NAME_1_4 = "AUTOFAULTWKPKG2";
   public String iWKP_NAME_1_5 = "AUTOPKGNAME5";
   public String iWKP_DESC_1 = "AUTOPKGDESC1";
   public String iSUB_TYPE_CD_1 = "A-CHECK";
   public String iHEAVY_MAINTENANCE_BOOL = "N";
   public String iCOLLECTION_REQUIRED_BOOL = "N";
   public String iISSUE_ACCOUNT_CD = "2";
   public String iSCHED_LOC_CD_1 = "AIRPORT1/LINE";
   public String iSCHED_LOC_CD_9 = "AIRPORT1/REPAIR/SHOP1";
   public String iWORK_TYPE_LIST_1 = "FUEL";
   public String iWORK_TYPE_LIST_2 = "ELECTRIC";
   public String iATA_SYS_CD_1 = "SYS-1";

   public String iWKP_SERIAL_NO_OEM_2 = "SN000008";
   public String iWKP_PART_NO_OEM_2 = "ENG_ASSY_PN1";
   public String iWKP_MANUFACT_CD_2 = "ABC11";
   public String iWKP_NAME_2 = "AUTOPKGNAME2";
   public String iWKP_DESC_2 = "AUTOPKGDESC2";
   public String iSUB_TYPE_CD_2 = "CR";
   public String iSCHED_LOC_CD_2 = "AIRPORT1/REPAIR/SHOP1";
   public String iATA_SYS_CD_2 = "SYS-2-1";

   public String iWKP_SERIAL_NO_OEM_3 = "SNAUTOTRK001";
   public String iWKP_PART_NO_OEM_3 = "A0000020";
   public String iWKP_MANUFACT_CD_3 = "10001";
   public String iWKP_NAME_3 = "AUTOPKGNAME3";
   public String iWKP_NAME_3_1 = "AUTOTRK_WKPKG1";
   public String iWKP_DESC_3 = "AUTOPKGDESC3";
   public String iSUB_TYPE_CD_3 = "CR";
   public String iSCHED_LOC_CD_3 = "AIRPORT1/REPAIR/SHOP1";

   public String iWKP_SERIAL_NO_OEM_4 = "SNBATCH1";
   public String iWKP_PART_NO_OEM_4 = "A0000009";
   public String iWKP_MANUFACT_CD_4 = "10001";
   public String iWKP_NAME_4 = "AUTOPKGNAME4";
   public String iWKP_DESC_4 = "AUTOPKGDESC4";
   public String iSUB_TYPE_CD_4 = "CR";
   public String iSCHED_LOC_CD_4 = "AIRPORT1/REPAIR/SHOP1";

   public String iWKP_SERIAL_NO_OEM_5 = "SN000016";
   public String iWKP_PART_NO_OEM_5 = "A0000001";
   public String iWKP_MANUFACT_CD_5 = "10001";
   public String iWKP_NAME_5 = "AUTOPKGNAME1";
   public String iSUB_TYPE_CD_5 = "CR";
   public String iSCHED_LOC_CD_5 = "AIRPORT1/HGR1/TRK1";
   public String iSCHED_LOC_CD_5_2 = "AIRPORT1/HGR1";
   public String iATA_SYS_CD_5 = "SYS-1-1";

   public String iWKP_SERIAL_NO_OEM_6 = "SN000006";
   public String iWKP_PART_NO_OEM_6 = "APU_ASSY_PN1";
   public String iWKP_MANUFACT_CD_6 = "1234567890";
   public String iWKP_NAME_6 = "AUTOPKGNAME2";
   public String iSUB_TYPE_CD_6 = "CR";
   public String iSCHED_LOC_CD_6 = "AIRPORT1/REPAIR/SHOP1";
   public String iATA_SYS_CD_6 = "APU-ASSY";

   public String iWKP_SERIAL_NO_OEM_7 = "SN000013";
   public String iWKP_NAME_7 = "AUTOWKPKG_FAULT";
   public String iWKP_DESC_7 = "AUTOWKPKG_FAULTDESC";
   public String iFAULT_SDESC_7 = "TRK_FAULT";
   public String iATA_SYS_CD_7 = "SYS-2-1-1";

   public String iWKP_SERIAL_NO_OEM_8 = "SN000014";

   public String iWKP_SERIAL_NO_OEM_9 = "SNAUTOSER001";
   public String iWKP_PART_NO_OEM_9 = "A0000012";
   public String iWKP_MANUFACT_CD_9 = "1234567890";
   public String iWKP_NAME_9 = "AUTOPKGNAME9";
   public String iWKP_DESC_9 = "AUTOPKGDESC9";

   // variables in AL_WORK_PACKAGE
   public String iWKP_SERIAL_NO_OEM_10 = "SNAFT737";
   public String iWKP_PART_NO_OEM_10 = "ACFT_ASSY_PN1";
   public String iWKP_MANUFACT_CD_10 = "10001";
   public String iWKP_NAME_10 = "AUTOPKGNAME1";
   public String iWKP_DESC_10 = "AUTOPKGDESC1";
   public String iSUB_TYPE_CD_10 = "A-CHECK";
   public String iATA_SYS_CD_10 = "ENG-SYS-1";
   public String iSERIAL_NO_OEM_10 = "SNENG7372";
   public String iPART_NO_OEM_10 = "ENG_ASSY_PN1";
   public String iMANUFACT_CD_10 = "ABC11";
   public String iTASK_CD_15 = "ENG-SYS-AL-TASK";

   // Variables in AL_WORK_PACKAGE_TASK
   public String iSERIAL_NO_OEM_TASk_1 = "SNAUTOTRK001";
   public String iSERIAL_NO_OEM_TASk_2 = "SNAUTOTRK002";
   public String iSERIAL_NO_OEM_TASk_3 = "SN000006";
   public String iSERIAL_NO_OEM_TASk_4 = "SNAUTOASSYTRK001";
   public String iSERIAL_NO_OEM_TASk_7 = "SNENG1000";
   public String iSERIAL_NO_OEM_TASk_8 = "SNBTH001AT";
   public String iSERIAL_NO_OEM_TASk_9 = "XXXTESTSN4";
   public String iSERIAL_NO_OEM_TASk_10 = "SN000014";

   public String iPART_NO_OEM_TASK_1 = "A0000020";
   public String iPART_NO_OEM_TASK_2 = "A0000016";
   public String iPART_NO_OEM_TASK_3 = "APU_ASSY_PN1";
   public String iPART_NO_OEM_TASK_4 = "E0000010";
   public String iPART_NO_OEM_TASK_7 = "PN_E200";
   public String iPART_NO_OEM_TASK_8 = "A0000009";
   public String iPART_NO_OEM_TASK_9 = "A0000002";

   public String iMANUFACT_CD_1 = "10001";
   public String iMANUFACT_CD_2 = "1234567890";
   public String iMANUFACT_CD_3 = "1234567890";
   public String iMANUFACT_CD_4 = "11111";
   public String iMANUFACT_CD_7 = "ABC11";
   public String iMANUFACT_CD_8 = "10001";
   public String iMANUFACT_CD_9 = "11111";

   public String iTASK_CD_1 = "REQ2";
   public String iTASK_CD_3 = "REQ3";
   public String iTASK_CD_4 = "REQ4";
   public String iTASK_CD_5 = "TRK-MOD";
   public String iTASK_CD_6 = "APU-ASSY-JIC-AT";
   public String iTASK_CD_7 = "ENG787-TASK-AT";
   public String iTASK_CD_8 = "ATBATCHT2";
   public String iTASK_CD_9 = "WKPSER1";
   public String iTASK_CD_10 = "SYS-REQ1";
   public String iTASK_CD_11 = "SYS-REQ-NR";
   public String iTASK_CD_12 = "SYS-JIC-26";
   public String iTASK_CD_13 = "AL_TASK";
   public String iTASK_CD_14 = "SYS-REP-2";
   public String iTASK_CD_16 = "AL_TASK_DUPLICATE";

   public String iFAULT_SDESC_2 = "TestTRKFAULT";
   public String iFAULT_SDESC_2_ata = "TestSERFAULT";
   public String iFAULT_SDESC_3 = "TESTCORRACFT";
   public String iFAULT_SDESC_4 = "TRK_FAULT2";
   public String iFAULT_SDESC_5 = "TRK_FAULT3";
   public String iFAULT_SDESC_6 = "TRK_FAULT4";
   public String iFAULT_SDESC_8 = "TRK_FAULT5";
   public String iFAULT_SDESC_9 = "TRK_FAULT6";

   public simpleIDs iFaultTRKIDs = null;
   public String iFaultTRKSchedID = null;
   public String iFaultTRKFaultIDNew = null;

   public String iNoteString1 = "The Work package has been created";
   public String iNoteString2 = "Scheduled: new location = AIRPORT1/LINE(Line Maintenance)";
   public String iNoteString3 =
         "Scheduled: new location = AIRPORT1/REPAIR/SHOP1(Airport 1 -  Repair - Shop 1)";
   public String iNoteString4 =
         "Scheduled: new location = AIRPORT1/HGR1/TRK1(Airport 1 - Hangar 1 - Track 1)";

   public String iTaskClassCD = "CHECK";
   public String iTaskClassCD_2 = "RO";
   public String iSkill1 = "INSP";
   public String iSkill2 = "AET";

   // variables in AL_WORK_PACKAGE_TASK_ATA
   public String iata_sys_cd_1 = "SYS-1-1";
   public String iata_sys_cd_2 = "SYS-1-1";
   public String iata_sys_cd_3 = "ENG-ASSY";
   public String iata_sys_cd_4 = "APU-ASSY";
   public String iata_sys_cd_5 = "ENG-SYS-2";
   public String iata_sys_cd_6 = "SYS-2-1-1";

   public String iTASK_CD_ata_1 = "BLOCK1";
   public String iTASK_CD_ata_2 = "BLOCK2";
   public String iTASK_CD_ata_4 = "BLOCK3";
   public String iTASK_CD_ata_5 = "SYS-REQ-1-RB 3";
   public String iTASK_CD_ata_6 = "SYS-REQ-1-RB 4";

   public String iFAULT_SDESC_ats_2 = "SYS-10-1";

   public simpleIDs iOriginalInv_id = null;
   public simpleIDs iAssmblBom_id = null;
   public simpleIDs iFaultTS_id = null;
   public simpleIDs iFaultCF_id = null;
   public simpleIDs iFaultTsIds = null;
   public simpleIDs iFaultCfIds = null;
   public simpleIDs iEVENT_1 = null;
   public simpleIDs iEVENT_2 = null;
   public simpleIDs iAccount_id = null;
   public simpleIDs iHEvent_id_REQ2 = null;
   public simpleIDs iHEvent_id_REQ3 = null;
   public simpleIDs iHEvent_id_REQ4 = null;
   public simpleIDs iHEvent_id_BLOCK1 = null;
   public simpleIDs iHEvent_id_BLOCK2 = null;
   public simpleIDs iHEvent_id_BLOCK3 = null;
   public simpleIDs iHEVENTTSTestTRKFAULT = null;
   public simpleIDs iHEVENTCFTestTRKFAULT = null;
   public simpleIDs iHEVENTTSTestSERFAULT = null;
   public simpleIDs iHEVENTCFTestSERFAULT = null;
   public simpleIDs iHEvent_id_WPKSER1 = null;
   public String iEventSdesc2 = null;
   public simpleIDs iOrginalTaskId = null;

   public simpleIDs iEngineIds = null;
   public AssmblBomID iEngineAssmbl = null;
   public simpleIDs iEngineSysIds = null;
   public List<ArrayList<String>> iOrginalEnvInv = null;
   public simpleIDs iOriginalFaultIds = null;
   public simpleIDs iOriginalValue = null;
   public simpleIDs iDuplcateInvNoIds = null; // used to create the a duplicate Serial No.
   public simpleIDs iDuplcatePartNoIds = null; // used to create the a duplicate Part No.
   public simpleIDs iOrigMainInvID = null;
   public String iBlock2TaskID = null;

   // Nested Task_CDs
   public String iTaskCd1 = "SYS-REQ-1-RB 3 (System Requirement for 1-Time Recurring Block 3)";
   public String iTaskCd2 = "SYS-JIC-3 (System Job Card 3)";
   public String iTaskCd3 = "AL_TASK (AL_TASK)";
   public simpleIDs iEvtTopTaskCd = null;
   public simpleIDs iEvtMidTaskCd = null;
   public simpleIDs iEvtLowTaskCd = null;

   public String iAT_JIC_1_EVENT_SDESC = "AT-JIC-1 (AT JIC 1)";
   public String iAT_JIC_2_EVENT_SDESC = "AT-JIC-2 (AT JIC 2)";
   public String iBLOCK4_EVENT_SDESC = "BLOCK4 (AUTO BLOCK4)";
   public String iAT2_EVENT_SDESC = "AT2 (AT2)";
   public String iAT3_EVENT_SDESC = "AT3 (AT3)";
   public String iAT_JIC_1_EVENT_LDESC = "AT JIC 1";
   public String iAT_JIC_2_EVENT_LDESC = "AT JIC 2";
   public String iBLOCK4_EVENT_LDESC = "AUTO BLOCK4";
   public String iAT2_EVENT_LDESC = "";
   public String iAT3_EVENT_LDESC = "";

   public simpleIDs iEVENT_BLOCK = null;
   public simpleIDs iEVENT_REQ = null;
   public simpleIDs iEVENT_JIC = null;

   public simpleIDs iEVENT_REQ_2 = null;
   public simpleIDs iEVENT_JIC_2 = null;

   private ArrayList<String> updateTables = new ArrayList<String>();
   {
      updateTables.add(
            "UPDATE evt_event SET EVENT_STATUS_CD = 'ACTV' WHERE EVENT_SDESC in ('REQ2 (REQ2)', 'BLOCK1 (AUTO BLOCK1)', 'REQ3 (REQ3)', 'BLOCK2 (AUTO BLOCK2)','REQ4 (REQ4)', 'BLOCK3 (AUTO BLOCK3)','SYS-REQ-1-RB 3 (System Requirement for 1-Time Recurring Block 3)' )" );
      updateTables.add(
            "UPDATE evt_event SET EVENT_STATUS_CD = 'CFDEFER' WHERE EVENT_TYPE_CD='CF' and EVENT_SDESC in ('TestTRKFAULT', 'TestSERFAULT')" );
      updateTables.add(
            "UPDATE evt_event SET EVENT_STATUS_CD = 'ACTV' WHERE EVENT_TYPE_CD='TS' and EVENT_SDESC in ('TestTRKFAULT', 'TestSERFAULT')" );
   };

   private ArrayList<String> restoreTables = new ArrayList<String>();
   {
      restoreTables.add(
            "UPDATE evt_event SET EVENT_STATUS_CD = 'COMPLETE', NH_EVENT_DB_ID=null, NH_EVENT_ID=null WHERE EVENT_SDESC in ('REQ2 (REQ2)', 'BLOCK1 (AUTO BLOCK1)','REQ3 (REQ3)', 'BLOCK2 (AUTO BLOCK2)', 'REQ4 (REQ4)', 'BLOCK3 (AUTO BLOCK3)')" );
      restoreTables.add(
            "UPDATE evt_event SET NH_EVENT_DB_ID=null, NH_EVENT_ID=null WHERE EVENT_SDESC in ('TestTRKFAULT', 'TestSERFAULT', 'TRK_FAULT5', 'ATBATCHT2 (ATBATCHT2)','AL_TASK (AL_TASK)')" );
      restoreTables.add(
            "UPDATE evt_event SET NH_EVENT_DB_ID=null, NH_EVENT_ID=null WHERE EVENT_SDESC in ('SYS-REQ-1-RB 3 (System Requirement for 1-Time Recurring Block 3)')" );
   };


   /**
    * This function is to verify after CSV imported or import store procedure has been executed.
    *
    *
    */
   public void VerifyImport() {

      iEVENT_1 = null;
      iEVENT_2 = null;
      // Verify EVT_EVENT table
      iAccount_id = getAccountIds( iISSUE_ACCOUNT_CD );
      iEVENT_1 = verifyEVTEVENT( "TS", iWKP_NAME_1, "ACTV", iWKP_DESC_1, iAccount_id );
      iEVENT_2 = verifyEVTEVENT( "TS", iWKP_NAME_2, "ACTV", iWKP_DESC_2, iAccount_id );

      // Verify EVT_INV table
      inventoryData linvData1 = verifyEVTINV( iEVENT_1 );
      inventoryData linvData2 = verifyEVTINV( iEVENT_2 );

      // Verify EVT_STAGE table
      VerifyEVTSTAGE( iEVENT_1, iNoteString1, iNoteString2 );
      VerifyEVTSTAGE( iEVENT_2, iNoteString1, iNoteString3 );

      // Verify SCHED_STASK table
      VerifySCHEDSTASK( iEVENT_1, iTaskClassCD, iSUB_TYPE_CD_1, linvData1 );
      VerifySCHEDSTASK( iEVENT_2, iTaskClassCD_2, iSUB_TYPE_CD_2, linvData2 );

      // Verify evt_loc
      simpleIDs lLocIds = getLocIds( iSCHED_LOC_CD_1 );
      VerifyEVTLOC( iEVENT_1, lLocIds );

      lLocIds = getLocIds( iSCHED_LOC_CD_2 );
      VerifyEVTLOC( iEVENT_2, lLocIds );

      // Verify sched_work_type
      VerifySCHEDWORKTYPE( iEVENT_1, iWORK_TYPE_LIST_1, iWORK_TYPE_LIST_2 );
      VerifySCHEDWORKTYPE( iEVENT_2, iWORK_TYPE_LIST_1, iWORK_TYPE_LIST_2 );

      // Verify sched_wp
      VerifySCHEDWP( iEVENT_1 );
      VerifySCHEDWP( iEVENT_2 );

      // Verify sched_wp_sign_req
      VerifySCHEDWPSIGNREQ( iEVENT_1, iSkill1, iSkill2 );
      VerifySCHEDWPSIGNREQ( iEVENT_2, iSkill1, iSkill2 );

      // Verify evt_event_rel
      VerifyEvtEventRel( iEVENT_1, "DRVTASK" );
   }


   /**
    * Verify Nested Task Codes
    *
    * @param aWkPg
    *           - Name of the work package
    * @param aTtaskCd1
    *           - Top Level task
    * @param aTaskCd2
    *           - Second level task
    * @param aTaskCd3
    *           - third level task
    */
   protected void VerifyTaskCds( String aWkPg, String aTaskCd1, String aTaskCd2, String aTaskCd3 ) {

      String lQuery =
            "SELECT event_db_id, event_id FROM evt_event WHERE event_sdesc = '" + aWkPg + "'";
      simpleIDs lWkPkgIds = getIDs( lQuery, "event_db_id", "event_id" );
      lQuery = "Select * from evt_event WHERE h_event_db_id = " + lWkPkgIds.getNO_DB_ID()
            + " AND h_event_id = " + lWkPkgIds.getNO_ID() + " AND event_sdesc in ('" + aWkPg + "','"
            + aTaskCd1 + "','" + aTaskCd2 + "','" + aTaskCd3 + "')";
      Assert.assertTrue( "Not all Task event entries are pointing to Work Package. ",
            countRowsOfQuery( lQuery ) == 4 );
      lQuery = "Select * from evt_event WHERE nh_event_db_id = " + lWkPkgIds.getNO_DB_ID()
            + " AND nh_event_id = " + lWkPkgIds.getNO_ID() + " AND event_sdesc = '" + aTaskCd1
            + "'";
      Assert.assertTrue( "Top-level Task event entries are not pointing to Work Package. ",
            countRowsOfQuery( lQuery ) == 1 );
      lQuery = "Select * from evt_event WHERE nh_event_db_id = " + iEvtTopTaskCd.getNO_DB_ID()
            + " AND nh_event_id = " + iEvtTopTaskCd.getNO_ID() + " AND event_sdesc = '" + aTaskCd2
            + "'";
      Assert.assertTrue( "Third-level Task event entries are not pointing to Second-Level Task. ",
            countRowsOfQuery( lQuery ) == 1 );
      lQuery = "Select * from evt_event WHERE nh_event_db_id = " + iEvtMidTaskCd.getNO_DB_ID()
            + " AND nh_event_id = " + iEvtMidTaskCd.getNO_ID() + " AND event_sdesc = '" + aTaskCd3
            + "'";
      Assert.assertTrue( "Second level Task event entries are not pointing to Top Level Task. ",
            countRowsOfQuery( lQuery ) == 1 );
   }


   /**
    * This function is to check error(s) for validation table is loaded
    *
    *
    */
   public void VerifyValidation() {

      int lCount = countRowsOfQuery( TestConstants.WPK_ERROR_CHECK_TABLE );
      Assert.assertTrue( "There should be no error. ", lCount == 0 );

   }


   /**
    * This function is to verify AL_WORK_PACKAGE, AL_WORK_PACKAGE_TASK and _WORK_PACKAGE_TASK_ATA
    * staging tables are loaded.
    *
    *
    */

   public void VerifyStagingTable() {

      int lCount = countRowsInEntireTable( TableUtil.AL_WORK_PACKAGE );
      Assert.assertTrue( "There should be 2 records in AL_WORK_PACKAGE table. ", lCount == 2 );

      lCount = countRowsInEntireTable( TableUtil.AL_WORK_PACKAGE_TASK );
      Assert.assertTrue( "There should be 3 records in AL_WORK_PACKAGE_TASK table. ", lCount == 3 );

      lCount = countRowsInEntireTable( TableUtil.AL_WORK_PACKAGE_TASK_ATA );
      Assert.assertTrue( "There should be 2 records in AL_WORK_PACKAGE_TASK table. ", lCount == 2 );

   }


   /**
    * This function is to verify sched_wp_sign_req table
    *
    *
    */
   public void VerifySCHEDWPSIGNREQ( simpleIDs aIDs, String aSkill1, String aSkill2 ) {

      String[] iIds = { "LABOUR_SKILL_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "SCHED_DB_ID", aIDs.getNO_DB_ID() );
      lArgs.addArguments( "SCHED_ID", aIDs.getNO_ID() );

      String iQueryString =
            TableUtil.buildTableQuery( TableUtil.SCHED_WP_SIGN_REQ, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "Check there should be 2 records for the event: ", llists.size() == 2 );

      for ( int i = 0; i < llists.size(); i++ )
         Assert.assertTrue( "Check work type is listed. ",
               llists.get( i ).get( 0 ).contains( aSkill1 )
                     || llists.get( i ).get( 0 ).contains( aSkill2 ) );

   }


   /**
    * This function is to verify sched_wp table
    *
    *
    */

   public void VerifySCHEDWP( simpleIDs aIDs ) {

      String lQuery = "select 1 from " + TableUtil.SCHED_WP + " where SCHED_DB_ID="
            + aIDs.getNO_DB_ID() + " and SCHED_ID=" + aIDs.getNO_ID();
      Assert.assertTrue( "Check inventory table to verify the record exists",
            RecordsExist( lQuery ) );
   }


   /**
    * This function is to verify sched_work_type table
    *
    *
    */
   public void VerifySCHEDWORKTYPE( simpleIDs aIDs, String aWorkType1, String aWorkType2 ) {

      String[] iIds = { "WORK_TYPE_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "SCHED_DB_ID", aIDs.getNO_DB_ID() );
      lArgs.addArguments( "SCHED_ID", aIDs.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.SCHED_WORK_TYPE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "Check there should be 2 records for the event: ", llists.size() == 2 );

      for ( int i = 0; i < llists.size(); i++ )
         Assert.assertTrue( "Check work type is listed. ",
               llists.get( i ).get( 0 ).contains( aWorkType1 )
                     || llists.get( i ).get( 0 ).contains( aWorkType2 ) );

   }


   public void VerifyEVTLOC( simpleIDs aIDs, simpleIDs aLocIDs ) {

      String[] iIds = { "LOC_DB_ID", "LOC_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "EVENT_DB_ID", aIDs.getNO_DB_ID() );
      lArgs.addArguments( "EVENT_ID", aIDs.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EVT_LOC, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      if ( aLocIDs.getNO_ID() != null ) {
         Assert.assertTrue( "LOC_DB_ID",
               llists.get( 0 ).get( 0 ).equalsIgnoreCase( aLocIDs.getNO_DB_ID() ) );
         Assert.assertTrue( "LOC_ID",
               llists.get( 0 ).get( 1 ).equalsIgnoreCase( aLocIDs.getNO_ID() ) );
      } else
         Assert.assertTrue( "Evt_Loc rows should not have been created", llists.size() == 0 );

   }


   /**
    * This function is to retrieve Location IDs by giving loc-cd
    *
    * @return Loc IDs
    *
    */
   public simpleIDs getLocIds( String aLocCd ) {

      String[] iIds = { "LOC_DB_ID", "LOC_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "LOC_CD", aLocCd );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.INV_LOC, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lIds;
   }


   /**
    * This function is to verify SCHED_STASK table details part
    *
    *
    */
   public void VerifySCHEDSTASK( simpleIDs aIDs, String aTaskClassCD, String aTASKSubClassCD,
         inventoryData aINVENT ) {

      // SCHED_STASK table
      String[] iIds = { "TASK_CLASS_CD", "TASK_SUBCLASS_CD", "ORIG_PART_NO_DB_ID",
            "ORIG_PART_NO_ID", "MAIN_INV_NO_DB_ID", "MAIN_INV_NO_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "SCHED_DB_ID", aIDs.getNO_DB_ID() );
      lArgs.addArguments( "SCHED_ID", aIDs.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.SCHED_STASK, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "TASK_CLASS_CD",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aTaskClassCD ) );
      if ( aTASKSubClassCD != null ) {
         Assert.assertTrue( "TASK_SUBCLASS_CD",
               llists.get( 0 ).get( 1 ).equalsIgnoreCase( aTASKSubClassCD ) );
      }
      Assert.assertTrue( "ORIG_PART_NO_DB_ID",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( Integer.toString( CONS_DB_ID ) ) );
      Assert.assertTrue( "ORG_PART_NO_ID",
            llists.get( 0 ).get( 3 ).equalsIgnoreCase( aINVENT.getPART_NO_ID() ) );
      Assert.assertTrue( "MAIN_INV_NO_DB_ID",
            llists.get( 0 ).get( 4 ).equalsIgnoreCase( Integer.toString( CONS_DB_ID ) ) );
      Assert.assertTrue( "MAIN_INV_NO_ID",
            llists.get( 0 ).get( 5 ).equalsIgnoreCase( aINVENT.getINV_NO_ID() ) );

   }


   /**
    * This function is to verify EVT_STAGE table
    *
    *
    */
   public void VerifyEVTSTAGE( simpleIDs aIDs, String aNote1, String aNote2 ) {
      String[] iIds = { "STAGE_NOTE" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "EVENT_DB_ID", aIDs.getNO_DB_ID() );
      lArgs.addArguments( "EVENT_ID", aIDs.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EVT_STAGE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "Check there should be 2 records for the event: ", llists.size() == 2 );

      for ( int i = 0; i < llists.size(); i++ )
         Assert.assertTrue( "Check stage note is listed. ",
               llists.get( i ).get( 0 ).contains( aNote1 )
                     || llists.get( i ).get( 0 ).contains( aNote2 ) );

   }


   /**
    * This function is to retrieve/verify inventory data from EVT_INV table
    *
    *
    */
   public inventoryData verifyEVTINV( simpleIDs aIDs ) {
      // EVT_INV table
      String[] iIds =
            { "INV_NO_ID", "ASSMBL_BOM_ID", "ASSMBL_POS_ID", "Part_NO_ID", "BOM_PART_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "EVENT_DB_ID", aIDs.getNO_DB_ID() );
      lArgs.addArguments( "EVENT_ID", aIDs.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EVT_INV, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      inventoryData linvData =
            new inventoryData( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ),
                  llists.get( 0 ).get( 2 ), llists.get( 0 ).get( 3 ), llists.get( 0 ).get( 4 ) );

      String lQuery = "select 1 from " + TableUtil.INV_INV + " where INV_NO_ID="
            + linvData.getINV_NO_ID() + " and ASSMBL_BOM_ID=" + linvData.getASSMBL_BOM_ID()
            + " and ASSMBL_POS_ID=" + linvData.getASSMBL_POS_ID() + " and PART_NO_ID="
            + linvData.getPART_NO_ID() + " and BOM_PART_ID=" + linvData.getBOM_PART_ID();
      Assert.assertTrue( "Check inventory table to verify the record exists",
            RecordsExist( lQuery ) );

      return linvData;
   }


   /**
    * This function is to retrieve/verify inventory data from EVT_INV table
    *
    *
    */
   public inventoryData verifyEVTINVforBatach( simpleIDs aIDs ) {
      // EVT_INV table
      String[] iIds =
            { "INV_NO_ID", "ASSMBL_BOM_ID", "ASSMBL_POS_ID", "Part_NO_ID", "BOM_PART_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "EVENT_DB_ID", aIDs.getNO_DB_ID() );
      lArgs.addArguments( "EVENT_ID", aIDs.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EVT_INV, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      inventoryData linvData =
            new inventoryData( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ),
                  llists.get( 0 ).get( 2 ), llists.get( 0 ).get( 3 ), llists.get( 0 ).get( 4 ) );

      String lQuery = "select 1 from " + TableUtil.INV_INV + " where INV_NO_ID="
            + linvData.getINV_NO_ID() + " and ASSMBL_BOM_ID is " + linvData.getASSMBL_BOM_ID()
            + " and ASSMBL_POS_ID is " + linvData.getASSMBL_POS_ID() + " and PART_NO_ID="
            + linvData.getPART_NO_ID() + " and BOM_PART_ID is " + linvData.getBOM_PART_ID();
      Assert.assertTrue( "Check inventory table to verify the record exists",
            RecordsExist( lQuery ) );

      return linvData;

   }


   /**
    * This function is to retrieve/verify inventory data from EVT_INV table for comp req
    *
    *
    */
   public inventoryData verifyEVTINV_COMPREQ( simpleIDs aIDs ) {
      // EVT_INV table
      String[] iIds = { "INV_NO_ID", "Part_NO_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "EVENT_DB_ID", aIDs.getNO_DB_ID() );
      lArgs.addArguments( "EVENT_ID", aIDs.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EVT_INV, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      String lINV_NO_ID = llists.get( 0 ).get( 0 );
      String lPart_NO_ID = llists.get( 0 ).get( 1 );

      String lQuery = "select 1 from " + TableUtil.INV_INV + " where INV_NO_ID='" + lINV_NO_ID
            + "' and PART_NO_ID='" + lPart_NO_ID + "'";
      Assert.assertTrue( "Check inventory table to verify the record exists",
            RecordsExist( lQuery ) );

      inventoryData linvData = new inventoryData( llists.get( 0 ).get( 0 ), null, null,
            llists.get( 0 ).get( 1 ), null );

      return linvData;

   }


   /**
    * This function is to retrieve account IDs by giving account CD
    *
    * @return event ID
    *
    */
   public simpleIDs getAccountIds( String aAccountCd ) {

      String[] iIds = { "ACCOUNT_DB_ID", "ACCOUNT_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "ACCOUNT_CD", aAccountCd );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.FNC_ACCOUNT, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lIds;
   }


   /**
    * This function is to verify EVT_EVENT table
    *
    * @return event ID
    *
    */
   public simpleIDs verifyEVTEVENT( String aEventTypeCd, String aSDESC, String aStatusCD,
         String aLDESC, simpleIDs aACIDs ) {

      // EVT_EVENT table
      String[] iIds = { "EVENT_DB_ID", "EVENT_ID", "EVENT_STATUS_CD", "EVENT_LDESC",
            "ACCOUNT_DB_ID", "ACCOUNT_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "EVENT_TYPE_CD", aEventTypeCd );
      lArgs.addArguments( "EVENT_SDESC", aSDESC );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EVT_EVENT, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      Assert.assertTrue( "EVENT_STATUS_CD",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aStatusCD ) );
      Assert.assertTrue( "EVENT_EVENT_LDESC", llists.get( 0 ).get( 3 ).contains( aLDESC ) );
      Assert.assertTrue( "ACCOUNT_DB_ID",
            llists.get( 0 ).get( 4 ).equalsIgnoreCase( aACIDs.getNO_DB_ID() ) );
      Assert.assertTrue( "ACCOUNT_ID",
            llists.get( 0 ).get( 5 ).equalsIgnoreCase( aACIDs.getNO_ID() ) );

      return lIds;

   }


   /**
    * This function is to verify EVT_EVENT table
    *
    * @return event ID
    *
    */
   public simpleIDs verifyEVTEVENT_nested( String aEventTypeCd, String aSDESC,
         simpleIDs aH_EVENTids, String aStatusCD, String aLDESC, simpleIDs aNH_EVENTids ) {

      // EVT_EVENT table
      String[] iIds = { "EVENT_DB_ID", "EVENT_ID", "EVENT_STATUS_CD", "EVENT_LDESC",
            "NH_EVENT_DB_ID", "NH_EVENT_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "EVENT_TYPE_CD", aEventTypeCd );
      lArgs.addArguments( "EVENT_SDESC", aSDESC );
      lArgs.addArguments( "H_EVENT_DB_ID", aH_EVENTids.getNO_DB_ID() );
      lArgs.addArguments( "H_EVENT_ID", aH_EVENTids.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EVT_EVENT, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      Assert.assertTrue( "EVENT_STATUS_CD",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aStatusCD ) );
      if ( aLDESC != null ) {
         Assert.assertTrue( "EVENT_LDESC", llists.get( 0 ).get( 3 ).contains( aLDESC ) );
      }
      Assert.assertTrue( "NH_EVENT_DB_ID",
            llists.get( 0 ).get( 4 ).equalsIgnoreCase( aNH_EVENTids.getNO_DB_ID() ) );
      Assert.assertTrue( "NH_EVENT_ID",
            llists.get( 0 ).get( 5 ).equalsIgnoreCase( aNH_EVENTids.getNO_ID() ) );

      return lIds;

   }


   /**
    * This function is to verify evt_event_rel table
    *
    *
    */
   public void VerifyEvtEventRel( simpleIDs aIDs, String aREL_TYPE_CD ) {

      String[] iIds = { "REL_TYPE_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "EVENT_DB_ID", aIDs.getNO_DB_ID() );
      lArgs.addArguments( "EVENT_ID", aIDs.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EVT_EVENT_REL, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "REL_TYPE_CD", llists.get( 0 ).get( 0 ).equalsIgnoreCase( aREL_TYPE_CD ) );

   }


   /**
    * This function is to set current date on DUE_DT column of c_open_deferred_fault
    *
    *
    *
    */

   public void setCurrentDate( String aWKPNAME ) {
      String aUpdateQuery =
            "UPDATE AL_WORK_PACKAGE SET SCHED_START_DT= (SELECT SYSDATE FROM DUAL),SCHED_END_DT= (SELECT SYSDATE FROM DUAL)  where WKP_NAME='"
                  + aWKPNAME + "'";
      try {

         PreparedStatement lStatement = getConnection().prepareStatement( aUpdateQuery );
         lStatement.executeUpdate( aUpdateQuery );
         commit();

      } catch ( SQLException e ) {
         e.printStackTrace();
      }
   }


   /**
    * This function is called by before test method to setup data.
    *
    *
    */

   public void DataSetup() {
      classDataSetup( updateTables );
      iFaultCF_id = getEvtIDs( "CF", "CFDEFER", "TestTRKFAULT" );
      iFaultTS_id = getEvtIDs( "TS", "ACTV", "TestTRKFAULT" );

      // =================Get h_event_id information
      iHEvent_id_REQ2 = getEvtIDs( "TS", "ACTV", "REQ2 (REQ2)" );
      iHEvent_id_BLOCK1 = getEvtIDs( "TS", "ACTV", "BLOCK1 (AUTO BLOCK1)" );
      iHEvent_id_REQ3 = getEvtIDs( "TS", "ACTV", "REQ3 (REQ3)" );
      iHEvent_id_BLOCK2 = getEvtIDs( "TS", "ACTV", "BLOCK2 (AUTO BLOCK2)" );
      iHEvent_id_REQ4 = getEvtIDs( "TS", "ACTV", "REQ4 (REQ4)" );
      iHEvent_id_BLOCK3 = getEvtIDs( "TS", "ACTV", "BLOCK3 (AUTO BLOCK3)" );
      iHEVENTTSTestTRKFAULT = iFaultTS_id;
      iHEVENTCFTestTRKFAULT = iFaultCF_id;
      iHEVENTTSTestSERFAULT = getEvtIDs( "TS", "ACTV", "TestSERFAULT" );
      iHEVENTCFTestSERFAULT = getEvtIDs( "CF", "CFDEFER", "TestSERFAULT" );
      iHEvent_id_WPKSER1 = getEvtIDs( "TS", "ACTV", "WKPSER1 (WKPSER1)" );
      // ===============
      iOriginalInv_id = getInvIds( iFaultCF_id );
      iAssmblBom_id = getAssmblBomIds( iFaultCF_id );
      updateEvtINV( iFaultCF_id );

      // Update Block2_ID
      String lQuery =
            "SELECT inv_no_db_id, inv_no_id  FROM inv_inv WHERE inv_no_sdesc = 'ENG-SYS-2 - Engine System 2' AND config_pos_sdesc = 'ENG-ASSY (2) ->ENG-SYS-2'";
      simpleIDs lNewValue = getIDs( lQuery, "inv_no_db_id", "inv_no_id" );
      lQuery = "SELECT inv_no_db_id, inv_no_id FROM inv_inv WHERE serial_no_oem = 'SN000008'";
      iOrigMainInvID = getIDs( lQuery, "inv_no_db_id", "inv_no_id" );
      lQuery = "SELECT task_id FROM task_task WHERE task_cd = '" + iTASK_CD_ata_2 + "'";
      iBlock2TaskID = getStringValueFromQuery( lQuery, "task_id" );
      lQuery = "UPDATE sched_stask SET main_inv_no_id = '" + lNewValue.getNO_ID()
            + "' where sched_stask.task_id = '" + iBlock2TaskID + "'";
      runUpdate( lQuery );

   }


   /**
    * This function is called by after test method to restore data which has been modified by tests.
    *
    *
    */

   public void RestoreData() {

      classDataSetup( restoreTables );
      restoreEvtINV( iFaultCF_id );

      String lStrUpdate = null;
      String lStrDelete = null;

      // Restore the original value for Block2
      runUpdate( "UPDATE sched_stask SET main_inv_no_id = '" + iOrigMainInvID.getNO_ID()
            + "' where task_id = " + iBlock2TaskID );
      // Restore h_event_ids
      if ( iEVENT_1 != null || iEVENT_2 != null ) {
         lStrUpdate = "update evt_event SET  H_EVENT_DB_ID = " + iHEvent_id_REQ2.getNO_DB_ID()
               + ", H_EVENT_ID = " + iHEvent_id_REQ2.getNO_ID() + "  WHERE EVENT_DB_ID= "
               + iHEvent_id_REQ2.getNO_DB_ID() + " and EVENT_ID= " + iHEvent_id_REQ2.getNO_ID();
         executeSQL( lStrUpdate );

         lStrUpdate = "update evt_event SET  H_EVENT_DB_ID = " + iHEvent_id_BLOCK1.getNO_DB_ID()
               + ", H_EVENT_ID = " + iHEvent_id_BLOCK1.getNO_ID() + "  WHERE EVENT_DB_ID= "
               + iHEvent_id_BLOCK1.getNO_DB_ID() + " and EVENT_ID= " + iHEvent_id_BLOCK1.getNO_ID();
         executeSQL( lStrUpdate );

         lStrUpdate = "update evt_event SET  H_EVENT_DB_ID = " + iHEvent_id_REQ3.getNO_DB_ID()
               + ", H_EVENT_ID = " + iHEvent_id_REQ3.getNO_ID() + "  WHERE EVENT_DB_ID= "
               + iHEvent_id_REQ3.getNO_DB_ID() + " and EVENT_ID= " + iHEvent_id_REQ3.getNO_ID();
         executeSQL( lStrUpdate );

         lStrUpdate = "update evt_event SET  H_EVENT_DB_ID = " + iHEvent_id_BLOCK2.getNO_DB_ID()
               + ", H_EVENT_ID = " + iHEvent_id_BLOCK2.getNO_ID() + "  WHERE EVENT_DB_ID= "
               + iHEvent_id_BLOCK2.getNO_DB_ID() + " and EVENT_ID= " + iHEvent_id_BLOCK2.getNO_ID();
         executeSQL( lStrUpdate );

         lStrUpdate = "update evt_event SET  H_EVENT_DB_ID = " + iHEvent_id_REQ4.getNO_DB_ID()
               + ", H_EVENT_ID = " + iHEvent_id_REQ4.getNO_ID() + "  WHERE EVENT_DB_ID= "
               + iHEvent_id_REQ4.getNO_DB_ID() + " and EVENT_ID= " + iHEvent_id_REQ4.getNO_ID();
         executeSQL( lStrUpdate );

         lStrUpdate = "update evt_event SET  H_EVENT_DB_ID = " + iHEvent_id_BLOCK3.getNO_DB_ID()
               + ", H_EVENT_ID = " + iHEvent_id_BLOCK3.getNO_ID() + "  WHERE EVENT_DB_ID= "
               + iHEvent_id_BLOCK3.getNO_DB_ID() + " and EVENT_ID= " + iHEvent_id_BLOCK3.getNO_ID();
         executeSQL( lStrUpdate );

         lStrUpdate = "update evt_event SET  H_EVENT_DB_ID = " + iHEVENTTSTestTRKFAULT.getNO_DB_ID()
               + ", H_EVENT_ID = " + iHEVENTTSTestTRKFAULT.getNO_ID() + "  WHERE EVENT_DB_ID= "
               + iHEVENTTSTestTRKFAULT.getNO_DB_ID() + " and EVENT_ID= "
               + iHEVENTTSTestTRKFAULT.getNO_ID();
         executeSQL( lStrUpdate );

         lStrUpdate = "update evt_event SET  H_EVENT_DB_ID = " + iHEVENTCFTestTRKFAULT.getNO_DB_ID()
               + ", H_EVENT_ID = " + iHEVENTCFTestTRKFAULT.getNO_ID() + "  WHERE EVENT_DB_ID= "
               + iHEVENTCFTestTRKFAULT.getNO_DB_ID() + " and EVENT_ID= "
               + iHEVENTCFTestTRKFAULT.getNO_ID();
         executeSQL( lStrUpdate );

         lStrUpdate = "update evt_event SET  H_EVENT_DB_ID = " + iHEVENTTSTestSERFAULT.getNO_DB_ID()
               + ", H_EVENT_ID = " + iHEVENTTSTestSERFAULT.getNO_ID() + "  WHERE EVENT_DB_ID= "
               + iHEVENTTSTestSERFAULT.getNO_DB_ID() + " and EVENT_ID= "
               + iHEVENTTSTestSERFAULT.getNO_ID();
         executeSQL( lStrUpdate );

         lStrUpdate = "update evt_event SET  H_EVENT_DB_ID = " + iHEVENTCFTestSERFAULT.getNO_DB_ID()
               + ", H_EVENT_ID = " + iHEVENTCFTestSERFAULT.getNO_ID() + "  WHERE EVENT_DB_ID= "
               + iHEVENTCFTestSERFAULT.getNO_DB_ID() + " and EVENT_ID= "
               + iHEVENTCFTestSERFAULT.getNO_ID();
         executeSQL( lStrUpdate );

         lStrUpdate = "update evt_event SET  H_EVENT_DB_ID = " + iHEvent_id_WPKSER1.getNO_DB_ID()
               + ", H_EVENT_ID = " + iHEvent_id_WPKSER1.getNO_ID() + "  WHERE EVENT_DB_ID= "
               + iHEvent_id_WPKSER1.getNO_DB_ID() + " and EVENT_ID= "
               + iHEvent_id_WPKSER1.getNO_ID();
         executeSQL( lStrUpdate );

      }

      if ( iEVENT_1 != null ) {
         // Delete sched_wp_sign_req
         lStrDelete = "delete from " + TableUtil.SCHED_WP_SIGN_REQ + " where SCHED_DB_ID='"
               + iEVENT_1.getNO_DB_ID() + "' and SCHED_ID='" + iEVENT_1.getNO_ID() + "'";
         executeSQL( lStrDelete );

         // Delete sched_wp
         lStrDelete = "delete from " + TableUtil.SCHED_WP + " where SCHED_DB_ID='"
               + iEVENT_1.getNO_DB_ID() + "' and SCHED_ID='" + iEVENT_1.getNO_ID() + "'";
         executeSQL( lStrDelete );

         // Delete sched_work_type
         lStrDelete = "delete from " + TableUtil.SCHED_WORK_TYPE + " where SCHED_DB_ID='"
               + iEVENT_1.getNO_DB_ID() + "' and SCHED_ID='" + iEVENT_1.getNO_ID() + "'";
         executeSQL( lStrDelete );

         // Delete evt_loc
         lStrDelete = "delete from " + TableUtil.EVT_LOC + " where EVENT_DB_ID='"
               + iEVENT_1.getNO_DB_ID() + "' and EVENT_ID='" + iEVENT_1.getNO_ID() + "'";
         executeSQL( lStrDelete );

         // Delete sched_stask
         lStrDelete = "delete from " + TableUtil.SCHED_STASK + " where SCHED_DB_ID='"
               + iEVENT_1.getNO_DB_ID() + "' and SCHED_ID='" + iEVENT_1.getNO_ID() + "'";
         executeSQL( lStrDelete );

         // Delete evt_stage
         lStrDelete = "delete from " + TableUtil.EVT_STAGE + " where EVENT_DB_ID='"
               + iEVENT_1.getNO_DB_ID() + "' and EVENT_ID='" + iEVENT_1.getNO_ID() + "'";
         executeSQL( lStrDelete );

         // Delete evt_inv
         lStrDelete = "delete from " + TableUtil.EVT_INV + " where EVENT_DB_ID='"
               + iEVENT_1.getNO_DB_ID() + "' and EVENT_ID='" + iEVENT_1.getNO_ID() + "'";
         executeSQL( lStrDelete );

         // Delete evt_event
         lStrDelete = "delete from " + TableUtil.EVT_EVENT + " where EVENT_DB_ID='"
               + iEVENT_1.getNO_DB_ID() + "' and EVENT_ID='" + iEVENT_1.getNO_ID() + "'";
         executeSQL( lStrDelete );

         // Delete evt_event_rel
         lStrDelete = "delete from " + TableUtil.EVT_EVENT_REL + " where EVENT_DB_ID='"
               + iEVENT_1.getNO_DB_ID() + "' and EVENT_ID='" + iEVENT_1.getNO_ID() + "'";
         executeSQL( lStrDelete );
      }

      if ( iEVENT_2 != null ) {
         // Delete sched_wp_sign_req
         lStrDelete = "delete from " + TableUtil.SCHED_WP_SIGN_REQ + " where SCHED_DB_ID='"
               + iEVENT_2.getNO_DB_ID() + "' and SCHED_ID='" + iEVENT_2.getNO_ID() + "'";
         executeSQL( lStrDelete );

         // Delete sched_wp
         lStrDelete = "delete from " + TableUtil.SCHED_WP + " where SCHED_DB_ID='"
               + iEVENT_2.getNO_DB_ID() + "' and SCHED_ID='" + iEVENT_2.getNO_ID() + "'";
         executeSQL( lStrDelete );

         // Delete sched_work_type
         lStrDelete = "delete from " + TableUtil.SCHED_WORK_TYPE + " where SCHED_DB_ID='"
               + iEVENT_2.getNO_DB_ID() + "' and SCHED_ID='" + iEVENT_2.getNO_ID() + "'";
         executeSQL( lStrDelete );

         // Delete evt_loc
         lStrDelete = "delete from " + TableUtil.EVT_LOC + " where EVENT_DB_ID='"
               + iEVENT_2.getNO_DB_ID() + "' and EVENT_ID='" + iEVENT_2.getNO_ID() + "'";
         executeSQL( lStrDelete );

         // Delete sched_stask
         lStrDelete = "delete from " + TableUtil.SCHED_STASK + " where SCHED_DB_ID='"
               + iEVENT_2.getNO_DB_ID() + "' and SCHED_ID='" + iEVENT_2.getNO_ID() + "'";
         executeSQL( lStrDelete );

         // Delete evt_stage
         lStrDelete = "delete from " + TableUtil.EVT_STAGE + " where EVENT_DB_ID='"
               + iEVENT_2.getNO_DB_ID() + "' and EVENT_ID='" + iEVENT_2.getNO_ID() + "'";
         executeSQL( lStrDelete );

         // Delete evt_inv
         lStrDelete = "delete from " + TableUtil.EVT_INV + " where EVENT_DB_ID='"
               + iEVENT_2.getNO_DB_ID() + "' and EVENT_ID='" + iEVENT_2.getNO_ID() + "'";
         executeSQL( lStrDelete );

         // Delete evt_event
         lStrDelete = "delete from " + TableUtil.EVT_EVENT + " where EVENT_DB_ID='"
               + iEVENT_2.getNO_DB_ID() + "' and EVENT_ID='" + iEVENT_2.getNO_ID() + "'";
         executeSQL( lStrDelete );
      }

      if ( iFaultTRKSchedID != null ) {
         String lquery =
               "update sched_stask set fault_id=" + iFaultTRKSchedID + " where sched_db_id="
                     + iFaultTRKIDs.getNO_DB_ID() + " and sched_id = " + iFaultTRKIDs.getNO_ID();
         executeSQL( lquery );
         iFaultTRKSchedID = null;
      }

      String lquery =
            "update evt_event set nh_event_db_id=null, nh_event_id=null where event_sdesc ='TestTRKFAULT' and event_type_cd='TS'";
      executeSQL( lquery );

      lquery =
            "update evt_event set nh_event_db_id=null, nh_event_id=null where event_sdesc ='REQ4 (REQ4)' and event_type_cd='TS'";
      executeSQL( lquery );

      lquery =
            "update evt_event set nh_event_db_id=null, nh_event_id=null where event_sdesc ='WKPSER1 (WKPSER1)' and event_type_cd='TS'";
      executeSQL( lquery );

      if ( iEVENT_BLOCK != null ) {
         lquery = "update evt_event set nh_event_db_id=null, nh_event_id=null, h_event_db_id='"
               + iEVENT_BLOCK.getNO_DB_ID() + "', h_event_id='" + iEVENT_BLOCK.getNO_ID() + "'"
               + "  where event_db_id ='" + iEVENT_BLOCK.getNO_DB_ID() + "' and event_id='"
               + iEVENT_BLOCK.getNO_ID() + "'";
         executeSQL( lquery );

         if ( iEVENT_REQ != null ) {
            lquery = "update evt_event set h_event_db_id='" + iEVENT_BLOCK.getNO_DB_ID()
                  + "', h_event_id='" + iEVENT_BLOCK.getNO_ID() + "'" + "  where event_db_id ='"
                  + iEVENT_REQ.getNO_DB_ID() + "' and event_id='" + iEVENT_REQ.getNO_ID() + "'";
            executeSQL( lquery );

         }

         if ( iEVENT_JIC != null ) {
            lquery = "update evt_event set h_event_db_id='" + iEVENT_BLOCK.getNO_DB_ID()
                  + "', h_event_id='" + iEVENT_BLOCK.getNO_ID() + "'" + "  where event_db_id ='"
                  + iEVENT_JIC.getNO_DB_ID() + "' and event_id='" + iEVENT_JIC.getNO_ID() + "'";
            executeSQL( lquery );
         }

      }

      if ( iEVENT_REQ_2 != null ) {
         lquery = "update evt_event set nh_event_db_id=null, nh_event_id=null, h_event_db_id='"
               + iEVENT_REQ_2.getNO_DB_ID() + "', h_event_id='" + iEVENT_REQ_2.getNO_ID() + "'"
               + "  where event_db_id ='" + iEVENT_REQ_2.getNO_DB_ID() + "' and event_id='"
               + iEVENT_REQ_2.getNO_ID() + "'";
         executeSQL( lquery );

         if ( iEVENT_JIC_2 != null ) {
            lquery = "update evt_event set h_event_db_id='" + iEVENT_REQ_2.getNO_DB_ID()
                  + "', h_event_id='" + iEVENT_REQ_2.getNO_ID() + "'" + "  where event_db_id ='"
                  + iEVENT_JIC_2.getNO_DB_ID() + "' and event_id='" + iEVENT_JIC_2.getNO_ID() + "'";
            executeSQL( lquery );
         }

      }

      lquery =
            "UPDATE evt_event SET nh_event_db_id = NULL, nh_event_id = NULL,h_event_db_id = event_db_id,"
                  + " h_event_id = event_id WHERE (nh_event_db_id, nh_event_id) NOT IN (SELECT event_db_id, event_id FROM evt_event) "
                  + " AND (event_db_id, event_id) IN (SELECT sched_db_id, sched_id FROM sched_stask WHERE task_class_cd IN "
                  + " (SELECT task_class_cd FROM ref_task_class WHERE class_mode_cd IN ('BLOCK','REQ')))";
      executeSQL( lquery );

   }


   /**
    * This function is to retrieve evt information from evt_event table.
    *
    *
    */
   public simpleIDs getEvtIDs( String aEventTYPECD, String aEVENTSTATUSCD, String aEVENTSDESC ) {

      // evt_event table
      String[] iIds = { "EVENT_DB_ID", "EVENT_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "EVENT_TYPE_CD", aEventTYPECD );
      lArgs.addArguments( "EVENT_STATUS_CD", aEVENTSTATUSCD );
      lArgs.addArguments( "EVENT_SDESC", aEVENTSDESC );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EVT_EVENT, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lIds;

   }


   /**
    * This function is to retrieve inv information from evt_inv table for given event id.
    *
    *
    */
   public simpleIDs getInvIds( simpleIDs aID ) {

      // evt_event table
      String[] iIds = { "INV_NO_DB_ID", "INV_NO_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "EVENT_DB_ID", aID.getNO_DB_ID() );
      lArgs.addArguments( "EVENT_ID", aID.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EVT_INV, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lIds;

   }


   /**
    * This function is to retrieve assmbl_bom_id and assmbl_pos_id from evt_inv table for given
    * event id.
    *
    *
    */
   public simpleIDs getAssmblBomIds( simpleIDs aID ) {

      // evt_event table
      String[] iIds = { "ASSMBL_BOM_ID", "ASSMBL_POS_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "EVENT_DB_ID", aID.getNO_DB_ID() );
      lArgs.addArguments( "EVENT_ID", aID.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EVT_INV, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lIds;

   }


   /**
    * This function is to retrieve inv information from evt_inv table for given event id.
    *
    *
    */
   public simpleIDs getOriginalINVIds() {

      simpleIDs lEvtId = getEvtIDs( "CF", "CFDEFER", "TestTRKFAULT" );
      simpleIDs lINVId = getInvIds( lEvtId );
      return lINVId;

   }


   public inventoryData getInventory( String aSN ) {

      ResultSet lResultSetRecords;
      inventoryData linvData = null;
      String lQuery =
            "select inv_inv.inv_no_id, eqp_assmbl_bom.assmbl_bom_id, eqp_assmbl_pos.assmbl_pos_id,eqp_part_no.part_no_id,eqp_bom_part.bom_part_id from eqp_assmbl_bom"
                  + " inner join eqp_assmbl_pos on "
                  + " eqp_assmbl_bom.assmbl_db_id=eqp_assmbl_pos.assmbl_db_id and "
                  + " eqp_assmbl_bom.assmbl_cd=eqp_assmbl_pos.assmbl_cd and "
                  + " eqp_assmbl_bom.assmbl_bom_id=eqp_assmbl_pos.assmbl_bom_id "
                  + " inner join eqp_bom_part on "
                  + " eqp_assmbl_bom.assmbl_db_id=eqp_bom_part.assmbl_db_id and "
                  + " eqp_assmbl_bom.assmbl_cd=eqp_bom_part.assmbl_cd and "
                  + " eqp_assmbl_bom.assmbl_bom_id=eqp_bom_part.assmbl_bom_id "
                  + " inner join eqp_part_baseline on "
                  + " eqp_part_baseline.bom_part_db_id=eqp_bom_part.bom_part_db_id and "
                  + " eqp_part_baseline.bom_part_id=eqp_bom_part.bom_part_id "
                  + " inner join eqp_part_no on "
                  + " eqp_part_no.part_no_db_id=eqp_part_baseline.part_no_db_id and "
                  + " eqp_part_no.part_no_id=eqp_part_baseline.part_no_id "
                  + " inner join inv_inv on "
                  + " inv_inv.bom_part_db_id=eqp_bom_part.bom_part_db_id and "
                  + " inv_inv.bom_part_id=eqp_bom_part.bom_part_id and "
                  + " inv_inv.part_no_db_id=eqp_part_no.part_no_db_id and "
                  + " inv_inv.part_no_id=eqp_part_no.part_no_id " + " where inv_inv.serial_no_oem='"
                  + aSN + "'";

      try {
         lResultSetRecords = runQuery( lQuery );
         lResultSetRecords.next();
         linvData = new inventoryData( lResultSetRecords.getString( "INV_NO_ID" ),
               lResultSetRecords.getString( "ASSMBL_BOM_ID" ),
               lResultSetRecords.getString( "ASSMBL_POS_ID" ),
               lResultSetRecords.getString( "PART_NO_ID" ),
               lResultSetRecords.getString( "BOM_PART_ID" ) );
      } catch ( SQLException e ) {
         e.printStackTrace();

      }

      return linvData;

   }


   public void updateEvtINV( simpleIDs aEvtId ) {

      inventoryData linvData = getInventory( iSERIAL_NO_OEM_TASk_2 );
      ResultSet lResultSetRecords;

      String lQuery = "update evt_inv set inv_no_id = " + linvData.getINV_NO_ID()
            + ", assmbl_bom_id = " + linvData.getASSMBL_BOM_ID() + ", assmbl_pos_id = "
            + linvData.getASSMBL_POS_ID() + ", part_no_db_id = " + CONS_DB_ID + ", part_no_id = "
            + linvData.getPART_NO_ID() + ", bom_part_db_id = " + CONS_DB_ID + ", bom_part_id = "
            + linvData.getBOM_PART_ID() + " where event_db_id= " + aEvtId.getNO_DB_ID()
            + " and event_id = " + aEvtId.getNO_ID();

      executeSQL( lQuery );

   }


   public void restoreEvtINV( simpleIDs aEvtId ) {

      String lQuery = "update evt_inv set inv_no_id = " + iOriginalInv_id.getNO_ID()
            + ", assmbl_bom_id = " + iAssmblBom_id.getNO_DB_ID() + ", assmbl_pos_id = "
            + iAssmblBom_id.getNO_ID()
            + ",  part_no_db_id = null,part_no_id = null, bom_part_db_id = null,  bom_part_id = null "
            + " where event_db_id= " + aEvtId.getNO_DB_ID() + " and event_id = "
            + aEvtId.getNO_ID();

      executeSQL( lQuery );

   }


   /**
    * This function is to validate error code exists.
    *
    *
    */
   public void validateErrorCode( String aCode ) {

      String llist = getErrorCodeList();
      String lerror_desc = getErrorDetail( aCode );
      Assert.assertTrue(
            "Expected Error: " + aCode + " - " + lerror_desc + ",\n Actual Error(s): " + llist,
            llist.contains( aCode ) );

      // if we expect there will be no uncaught exceptions
      Assert.assertFalse(
            "Expected Error: ALWPK-20009: No uncaught exceptions,\n Actual Error(s): " + llist,
            llist.contains( "ALWPK-20009" ) );
   }


   /**
    * This function is to validate error code exists. check for two error codes
    *
    */
   public void validateErrorCode( String aCode, String aCode2 ) {

      String llist = getErrorCodeList();
      String lerror_desc = getErrorDetail( aCode );
      Assert.assertTrue(
            "Expected Error: " + aCode + " - " + lerror_desc + ",\n Actual Error(s): " + llist,
            llist.contains( aCode ) );
      Assert.assertTrue(
            "Expected Error: " + aCode2 + " - " + lerror_desc + ",\n Actual Error(s): " + llist,
            llist.contains( aCode2 ) );
      // if we expect there will be no uncaught exceptions
      Assert.assertFalse(
            "Expected Error: ALWPK-20009: No uncaught exceptions,\n Actual Error(s): " + llist,
            llist.contains( "ALWPK-20009" ) );
   }


   /**
    * This function is to retrieve errors code list
    *
    * @throws SQLException
    *
    */

   public String getErrorCodeList() {
      String[] iIds = { "RESULT_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      String lQueryString =
            "SELECT distinct(al_proc_result.result_cd), DL_REF_MESSAGE.severity_cd, DL_REF_MESSAGE.user_desc "
                  + " FROM al_proc_result " + " inner join DL_REF_MESSAGE on "
                  + " DL_REF_MESSAGE.RESULT_CD = AL_PROC_RESULT.RESULT_CD"
                  + " WHERE AL_PROC_RESULT.FUNC_AREA_CD = 'WRKPKG' ";

      String lResults = "";

      try {
         ResultSet lQueryResults = runQuery( lQueryString );

         while ( lQueryResults.next() ) {

            lResults += "[" + lQueryResults.getString( "result_cd" );
            lResults += "-" + lQueryResults.getString( "severity_cd" );
            lResults += "-" + lQueryResults.getString( "user_desc" );
            lResults += "]";
            if ( !lQueryResults.isLast() )
               lResults += ", ";

         }
      } catch ( SQLException e ) {
         e.printStackTrace();
      }
      return lResults;

   }


   protected String getListOfErrors() throws Exception {
      StringBuilder lQueryErrorList = new StringBuilder();
      lQueryErrorList.append(
            "SELECT distinct(error_cd), DL_REF_MESSAGE.severity_cd, DL_REF_MESSAGE.user_desc " );
      lQueryErrorList.append(
            "FROM AL_PROC_INVENTORY_ERROR JOIN DL_REF_MESSAGE ON AL_PROC_INVENTORY_ERROR.error_cd = DL_REF_MESSAGE.result_cd " );
      lQueryErrorList.append( "WHERE error_cd IS NOT NULL" );

      ResultSet lQueryResults = runQuery( lQueryErrorList.toString() );

      String lResults = "";
      while ( lQueryResults.next() ) {

         lResults += "[" + lQueryResults.getString( "error_cd" );
         lResults += "-" + lQueryResults.getString( "severity_cd" );
         lResults += "-" + lQueryResults.getString( "user_desc" );
         lResults += "]";
         if ( !lQueryResults.isLast() ) {
            lResults += ", ";
         }
      }
      return lResults;
   }


   /**
    * This function is to get detail of error code
    *
    *
    */

   public String getErrorDetail( String aErrorcode ) {

      String[] iIds = { "USER_DESC" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();

      lArgs.addArguments( "RESULT_CD", aErrorcode );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.DL_REF_MESSAGE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      return llists.get( 0 ).get( 0 );

   }


   /**
    * This function is to implement interface ValidationAndImport
    *
    * @param: blnOnlyValidation
    * @param: allornone
    *
    */
   public int runValidationAndImport( boolean blnOnlyValidation, boolean aAllOrNone ) {
      int rtValue = 0;
      ivalidationandimport = new ValidationAndImport() {

         @Override
         public int runValidation( boolean aAllOrNone ) {
            int lReturn = 0;
            CallableStatement lPrepareCallInventory;

            try {

               if ( aAllOrNone )
                  lPrepareCallInventory = getConnection().prepareCall(
                        "BEGIN al_work_package_pkg.validate(aib_allornone => true,  ain_gather_stats_bool => ?, aon_retcode => ?, aov_retmsg => ?); END;" );
               else
                  lPrepareCallInventory = getConnection().prepareCall(
                        "BEGIN al_work_package_pkg.validate(aib_allornone => false,  ain_gather_stats_bool => ?, aon_retcode => ?, aov_retmsg => ?); END;" );

               lPrepareCallInventory.setInt( 1, 0 );
               lPrepareCallInventory.registerOutParameter( 2, Types.INTEGER );
               lPrepareCallInventory.registerOutParameter( 3, Types.VARCHAR );
               lPrepareCallInventory.execute();
               commit();

               lReturn = lPrepareCallInventory.getInt( 2 );

            } catch ( SQLException e ) {
               e.printStackTrace();
            }
            return lReturn;

         }


         @Override
         public int runImport( boolean aAllOrNone ) {
            int lReturn = 0;

            CallableStatement lPrepareCallInventory;

            try {
               lPrepareCallInventory = getConnection()
                     .prepareCall( "BEGIN  al_work_package_pkg.import( ?, ?, ?); END;" );

               lPrepareCallInventory.setInt( 1, 0 );
               lPrepareCallInventory.registerOutParameter( 2, Types.INTEGER );
               lPrepareCallInventory.registerOutParameter( 3, Types.VARCHAR );

               lPrepareCallInventory.execute();
               commit();
               lReturn = lPrepareCallInventory.getInt( 2 );

            } catch ( SQLException e ) {
               e.printStackTrace();
            }
            return lReturn;
         }

      };

      rtValue = blnOnlyValidation ? ivalidationandimport.runValidation( aAllOrNone )
            : ivalidationandimport.runImport( aAllOrNone );

      return rtValue;
   }

}
