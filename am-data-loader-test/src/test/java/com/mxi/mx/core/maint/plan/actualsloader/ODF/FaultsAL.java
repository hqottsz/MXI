package com.mxi.mx.core.maint.plan.actualsloader.ODF;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
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
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.mxi.mx.core.maint.plan.datamodels.asmInfor;
import com.mxi.mx.core.maint.plan.datamodels.assmblPOS;
import com.mxi.mx.core.maint.plan.datamodels.assmbleInfor;
import com.mxi.mx.core.maint.plan.datamodels.invIDs;
import com.mxi.mx.core.maint.plan.datamodels.invInfor;
import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;
import com.mxi.mx.util.ActualsLoaderTest;
import com.mxi.mx.util.BatchFileManager;
import com.mxi.mx.util.StringUtils;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.TestConstants;
import com.mxi.mx.util.ValidationAndImport;
import com.mxi.mx.util.WhereClause;


/**
 * This test suite contains test cases on validation and import functionality of
 * AL_OPEN_DEFERRED_FAULT_IMPORT package. Actual loader would not support functionality to assign
 * faults on ENG and APU if you are installed on ACFT for now.
 *
 * @author ALICIA QIAN
 */
public class FaultsAL extends ActualsLoaderTest {

   @Rule
   public TestName testName = new TestName();

   ValidationAndImport ivalidationandimport;

   public simpleIDs iTSIDs1 = null;
   public simpleIDs iCFIDs1 = null;
   public simpleIDs iLabourID1 = null;
   public simpleIDs iLabourRoleID1 = null;

   public simpleIDs iTSIDs2 = null;
   public simpleIDs iCFIDs2 = null;
   public simpleIDs iLabourID2 = null;
   public simpleIDs iLabourRoleID2 = null;

   public String iEventTypeCdFault = "CF";
   public String iEventTypeCdTask = "TS";
   public String iREL_TYPE_CDFAULT = "CORRECT";
   public String iREL_TYPE_CDTASK = "DRVTASK";
   public String iACTV = "ACTV";
   public String iLBR = "LBR";
   public String iTECH = "TECH";

   public String iAssmbl_ACFT_CD = "ACFT_CD1";
   // public String iSN_ACFT = "SN000013";
   public String iSN_ACFT = "SN000014";

   public String iPN_OEM_ACFT = "ACFT_ASSY_PN1";
   public String iMANFACT_CD_ACFT = "10001";
   public String iSYS_CD_ACFT = "SYS-1-1";
   public String iFAULT_SDESC = "AUTOFSDESC";
   public String iFAULT_LDESC = "AUTOFAULTLDESC";
   public String iFAIL_SEV_CD = "MEL";
   public String iFAIL_DEFER_CD = "MEL A";
   public String iFAULT_SOURCE_CD = "MECH";
   public String iTASK_PRIORITY_CD = "LOW";
   public String iLOGBOOK_REFERENCE = "logtest";
   public String iDEFER_REF_SDESC = "AUTOMINOR";
   public String iOP_RESTRICTION_LDESC = "AUTORESTRICTIONLDESC";
   public String iDEFER_CD_SDESC = "AUTOCDMINOR";
   public String iFAULT_LOG_TYPE_CD = "TECH";
   public String iFRM_CD = "AUTOFRMCD";

   // TRK part of ACFT
   public String iPN1_PART_NO_OEM = "A0000010";
   public String iPN1_MANUFACT_CD = "11111";
   public String iPN1_IPC_REF_CD = "ACFT-SYS-1-1-TRK-BATCH-PARENT";
   public String iPN1_POS_CD = "1";
   // BATCH part of ACFT
   public String iPN2_PART_NO_OEM = "A0000011";
   public String iPN2_MANUFACT_CD = "ABC11";
   public String iPN2_IPC_REF_CD = "ACFT-SYS-1-1-TRK-BATCH-CHILD";
   public String iPN2_POS_CD = "1";
   // SER part of ACFT
   public String iPN3_PART_NO_OEM = "A0000014";
   public String iPN3_MANUFACT_CD = "11111";
   public String iPN3_IPC_REF_CD = "ACFT-SYS-1-1-TRK-SER-CHILD";
   public String iPN3_POS_CD = "1";

   public String iSN_ACFT2 = "SN000001";
   public String iFAULT_SDESC2 = "AUTOFSDESC2";
   public String iFAULT_LDESC2 = "AUTOFAULTLDESC2";
   public String iLOGBOOK_REFERENCE2 = "logtest2";

   public static String AXON_DOMAIN_EVENT_ENTRY_TABLE = "AXON_DOMAIN_EVENT_ENTRY";


   /**
    * Setup before executing each individual test
    *
    */
   @Before
   @Override
   public void before() throws Exception {

      super.before();
      clearActualsLoaderTables();

      // Clear all IDs
      iTSIDs1 = null;
      iCFIDs1 = null;
      iLabourID1 = null;
      iLabourRoleID1 = null;

      iTSIDs2 = null;
      iCFIDs2 = null;
      iLabourID2 = null;
      iLabourRoleID2 = null;

      String strTCName = testName.getMethodName();
      if ( strTCName.contains( "test_AMLODF_12025" ) ) {
         dataSetupPCT();
      } else if ( strTCName.contains( "test_AMLODF_12026" ) ) {
         dataSetupPOS();
      } else if ( strTCName.contains( "OPER_24353" ) ) {
         dataSetupACFTDup();
      } else if ( strTCName.contains( "test_AMLODF_11011" ) ) {
         dataSetupMACFACTDup();
      } else if ( strTCName.contains( "test_AMLODF_12110" ) ) {
         dataSetupPrecision();
      } else if ( strTCName.contains( "test_AMLODF_12111" ) ) {
         dataSetupPrecision();
      } else if ( strTCName.contains( "testOPER_27627_INVALID" ) ) {
         dataSetForPARM( "DEFAULT_REASON_FOR_REMOVAL", "INVALID" );
      } else if ( strTCName.contains( "testOPER_27627_DUPLICATE" ) ) {
         dataSetForPARM( "DEFAULT_REASON_FOR_REMOVAL", "DUPRSN" );
      }

      // Clear AXON_DOMAIN_EVENT_ENTRY table
      deleteAllFromTable( AXON_DOMAIN_EVENT_ENTRY_TABLE );

   }


   /**
    * Clean up after each individual test
    */
   @After
   @Override
   public void after() {

      try {
         String strTCName = testName.getMethodName();
         if ( strTCName.contains( "OPER_24353" ) ) {
            dataRestoreACFTDup();
         } else if ( strTCName.contains( "test_AMLODF_11011" ) ) {
            dataRestoreMACFACTDup();
         } else if ( strTCName.contains( "testOPER_27627_INVALID" ) ) {
            dataSetForPARM( "DEFAULT_REASON_FOR_REMOVAL", "IMSCHD" );
         } else if ( strTCName.contains( "testOPER_27627_DUPLICATE" ) ) {
            dataSetForPARM( "DEFAULT_REASON_FOR_REMOVAL", "IMSCHD" );
         }
         RestoreData();
         super.after();
      } catch ( Exception e ) {
         e.printStackTrace();
      }
   }


   /**
    * This test is to verify AL_OPEN_DEFERRED_FAULT_IMPORT validation functionality of staging table
    * AL_OPEN_DEFERRED_FAULT_IMPORT. TRK part on ACFT.
    *
    *
    */

   public void testACFTSYSTRK_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );
      lFaults.put( "FOUND_ON_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify AL_OPEN_DEFERRED_FAULT_IMPORT import functionality of staging table
    * AL_OPEN_DEFERRED_FAULT_IMPORT. TRK part on ACFT.
    *
    *
    */
   @Test
   public void testACFTSYSTRK_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testACFTSYSTRK_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify EVT_EVENT table
      iCFIDs1 = verifyEVTEVENT( iEventTypeCdFault, iFAULT_SDESC, "CFDEFER", iFAULT_LDESC,
            iLOGBOOK_REFERENCE );

      iTSIDs1 = verifyEVTEVENT( iEventTypeCdTask, iFAULT_SDESC, "ACTV", iFAULT_LDESC, null );

      // Verify EVT_INV table
      invIDs lINVIDSFAULT = verifyEVTINV( iCFIDs1, iAssmbl_ACFT_CD, "2", "1" );
      invIDs lINVIDSTASK = verifyEVTINV( iTSIDs1, iAssmbl_ACFT_CD, "2", "1" );

      Assert.assertTrue( "Inventory Ids should be equal: ", lINVIDSFAULT.equals( lINVIDSTASK ) );

      // Verify the INV ids exists in INV_INV table
      String lQuery = "select 1 from " + TableUtil.INV_INV + " where INV_NO_DB_ID="
            + lINVIDSTASK.getINVIDs().getNO_DB_ID() + " and INV_NO_ID="
            + lINVIDSTASK.getINVIDs().getNO_ID() + " and ASSMBL_DB_ID="
            + lINVIDSTASK.getASSMBLINVIDs().getNO_DB_ID() + " and ASSMBL_INV_NO_ID="
            + lINVIDSTASK.getASSMBLINVIDs().getNO_ID();
      Assert.assertTrue( "Check inventory table to verify the record exists",
            RecordsExist( lQuery ) );

      // Verify EVT_EVENT_REL
      verifyEVTEVENT_REL( iCFIDs1, iTSIDs1, iREL_TYPE_CDFAULT );
      verifyEVTEVENT_REL( iTSIDs1, iTSIDs1, iREL_TYPE_CDTASK );

      // Verify EVT_STAGE table
      ArrayList<String> ltasklist = new ArrayList<String>();
      ltasklist.add( "UNASSIGNED FROM" );
      ltasklist.add( "NEW HOURS(HOUR) DEADLINE" );
      ltasklist.add( "NEW CDY(DAY) DEADLINE" );
      ltasklist.add( "THE TASK WAS CREATED" );
      ltasklist.add( "NEW CYCLES(CYCLES) DEADLINE" );
      ltasklist.add( "THE TASK HAS BEEN DEFERRED" );

      verifyEVTSTAGE( iTSIDs1, ltasklist );

      ArrayList<String> lfaultlist = new ArrayList<String>();
      lfaultlist.add( "CORRECTIVE ACTION WAS DEFERRED" );
      lfaultlist.add( "CORRECTIVE ACTION WAS UNASSIGNED" );
      lfaultlist.add( "FAULT WAS CREATED" );

      verifyEVTSTAGE( iCFIDs1, lfaultlist );

      // Verify EVT_SCHED_DEAD
      // get Ids of HOUR/CYCLES/DAY in mim_data_type table
      simpleIDs lHoursIds = getMIMType( "HOURS" );
      simpleIDs lCycIds = getMIMType( "CYCLES" );
      simpleIDs lDayIds = getMIMType( "CDY" );
      verifyEVTSCHEDDEAD( iTSIDs1, lHoursIds );
      verifyEVTSCHEDDEAD( iTSIDs1, lCycIds );
      verifyEVTSCHEDDEAD( iTSIDs1, lDayIds );

      // Verify SCHED_STASK table
      simpleIDs lPriorityIds = getPriorityIDs( iTASK_PRIORITY_CD );
      simpleIDs lTaskCDs = getTaskCDs( "CORR" );
      verifySCHEDSTASK( iTSIDs1, iCFIDs1, lPriorityIds, lTaskCDs, lINVIDSTASK.getINVIDs() );

      // Verify SD_FAULT
      verifySDFAULT( iCFIDs1, iFAIL_DEFER_CD, iFAULT_SOURCE_CD, iFAIL_SEV_CD, iDEFER_REF_SDESC,
            iOP_RESTRICTION_LDESC, iDEFER_CD_SDESC, iFAULT_LOG_TYPE_CD, iFRM_CD );

      // Verify sched_labour table
      String iLabor = getParmValue();
      if ( StringUtils.isBlank( iLabor ) ) {
         iLabourID1 = verifySCHEDLABOUR( iTSIDs1, iACTV, iLBR );
      } else if ( !RecordsExist(
            "SELECT 1 FROM  ref_labour_skill WHERE labour_skill_cd='" + iLabor + "'" ) ) {
         iLabourID1 = verifySCHEDLABOUR( iTSIDs1, iACTV, iLBR );

      } else {
         iLabourID1 = verifySCHEDLABOUR( iTSIDs1, iACTV, iLabor );
      }

      // Verify SCHED_LABOUR_ROLE table
      iLabourRoleID1 = verifySCHEDLABOURROLE( iLabourID1, iTECH );

      // Verify SCHED_LABOUR_ROLE_STATUS table
      verifySCHEDLABOURROLESTATUS( iLabourRoleID1, iACTV );

      // Verify SCHED_PART
      asmInfor linfors = getASMINFor( iPN1_PART_NO_OEM, iPN1_MANUFACT_CD );
      verifySCHEDPART( iTSIDs1, linfors, iACTV, "1", "REQ", true );

      // Verify SCHED_INST_PART
      verifySCHEDINSTPART( iTSIDs1, linfors, "1", 1 );

      // Verify SCHED_RMVD_PART
      invInfor lInvInfor = getINVINFor( iPN1_PART_NO_OEM, iPN1_MANUFACT_CD, iSN_ACFT );
      verifySCHEDRMVDPART( iTSIDs1, linfors, "IMSCHD", "1", 1, lInvInfor, true );

      // Verify AXON_DOMAIN_EVENT_ENTRY
      verifyTaskDrivingDeadlineRescheduledEventPublish();
      verifyTaskCreatedEventPublish();

   }


   /**
    * This test is to verify AL_OPEN_DEFERRED_FAULT_IMPORT validation functionality of staging table
    * AL_OPEN_DEFERRED_FAULT_IMPORT. BATCH part on ACFT.
    *
    *
    */

   public void testACFTSYSBATCH_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN2_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN2_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN2_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN2_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'5\'" );
      lFaults.put( "PN1_RMVD_QT", "\'6\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );
      lFaults.put( "FOUND_ON_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify AL_OPEN_DEFERRED_FAULT_IMPORT import functionality of staging table
    * AL_OPEN_DEFERRED_FAULT_IMPORT. BATCH part on ACFT. OPER-21405 fix
    *
    *
    */
   @Test
   public void testACFTSYSBATCH_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testACFTSYSBATCH_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify EVT_EVENT table
      iCFIDs1 = verifyEVTEVENT( iEventTypeCdFault, iFAULT_SDESC, "CFDEFER", iFAULT_LDESC,
            iLOGBOOK_REFERENCE );

      iTSIDs1 = verifyEVTEVENT( iEventTypeCdTask, iFAULT_SDESC, "ACTV", iFAULT_LDESC, null );

      // Verify EVT_INV table
      invIDs lINVIDSFAULT = verifyEVTINV( iCFIDs1, iAssmbl_ACFT_CD, "2", "1" );
      invIDs lINVIDSTASK = verifyEVTINV( iTSIDs1, iAssmbl_ACFT_CD, "2", "1" );

      Assert.assertTrue( "Inventory Ids should be equal: ", lINVIDSFAULT.equals( lINVIDSTASK ) );

      // Verify the INV ids exists in INV_INV table
      String lQuery = "select 1 from " + TableUtil.INV_INV + " where INV_NO_DB_ID="
            + lINVIDSTASK.getINVIDs().getNO_DB_ID() + " and INV_NO_ID="
            + lINVIDSTASK.getINVIDs().getNO_ID() + " and ASSMBL_DB_ID="
            + lINVIDSTASK.getASSMBLINVIDs().getNO_DB_ID() + " and ASSMBL_INV_NO_ID="
            + lINVIDSTASK.getASSMBLINVIDs().getNO_ID();
      Assert.assertTrue( "Check inventory table to verify the record exists",
            RecordsExist( lQuery ) );

      // Verify EVT_EVENT_REL
      verifyEVTEVENT_REL( iCFIDs1, iTSIDs1, iREL_TYPE_CDFAULT );
      verifyEVTEVENT_REL( iTSIDs1, iTSIDs1, iREL_TYPE_CDTASK );

      // Verify EVT_STAGE table
      ArrayList<String> ltasklist = new ArrayList<String>();
      ltasklist.add( "UNASSIGNED FROM" );
      ltasklist.add( "NEW HOURS(HOUR) DEADLINE" );
      ltasklist.add( "NEW CDY(DAY) DEADLINE" );
      ltasklist.add( "THE TASK WAS CREATED" );
      ltasklist.add( "NEW CYCLES(CYCLES) DEADLINE" );
      ltasklist.add( "THE TASK HAS BEEN DEFERRED" );

      verifyEVTSTAGE( iTSIDs1, ltasklist );

      ArrayList<String> lfaultlist = new ArrayList<String>();
      lfaultlist.add( "CORRECTIVE ACTION WAS DEFERRED" );
      lfaultlist.add( "CORRECTIVE ACTION WAS UNASSIGNED" );
      lfaultlist.add( "FAULT WAS CREATED" );

      verifyEVTSTAGE( iCFIDs1, lfaultlist );

      // Verify EVT_SCHED_DEAD
      // get Ids of HOUR/CYCLES/DAY in mim_data_type table
      simpleIDs lHoursIds = getMIMType( "HOURS" );
      simpleIDs lCycIds = getMIMType( "CYCLES" );
      simpleIDs lDayIds = getMIMType( "CDY" );
      verifyEVTSCHEDDEAD( iTSIDs1, lHoursIds );
      verifyEVTSCHEDDEAD( iTSIDs1, lCycIds );
      verifyEVTSCHEDDEAD( iTSIDs1, lDayIds );

      // Verify SCHED_STASK table
      simpleIDs lPriorityIds = getPriorityIDs( iTASK_PRIORITY_CD );
      simpleIDs lTaskCDs = getTaskCDs( "CORR" );
      verifySCHEDSTASK( iTSIDs1, iCFIDs1, lPriorityIds, lTaskCDs, lINVIDSTASK.getINVIDs() );

      // Verify SD_FAULT
      verifySDFAULT( iCFIDs1, iFAIL_DEFER_CD, iFAULT_SOURCE_CD, iFAIL_SEV_CD, iDEFER_REF_SDESC,
            iOP_RESTRICTION_LDESC, iDEFER_CD_SDESC, iFAULT_LOG_TYPE_CD, iFRM_CD );

      // Verify sched_labour table
      String iLabor = getParmValue();
      if ( StringUtils.isBlank( iLabor ) ) {
         iLabourID1 = verifySCHEDLABOUR( iTSIDs1, iACTV, iLBR );
      } else if ( !RecordsExist(
            "SELECT 1 FROM  ref_labour_skill WHERE labour_skill_cd='" + iLabor + "'" ) ) {
         iLabourID1 = verifySCHEDLABOUR( iTSIDs1, iACTV, iLBR );

      } else {
         iLabourID1 = verifySCHEDLABOUR( iTSIDs1, iACTV, iLabor );
      }

      // Verify SCHED_LABOUR_ROLE table
      iLabourRoleID1 = verifySCHEDLABOURROLE( iLabourID1, iTECH );

      // Verify SCHED_LABOUR_ROLE_STATUS table
      verifySCHEDLABOURROLESTATUS( iLabourRoleID1, iACTV );

      // Verify SCHED_PART asmInfor
      asmInfor linfors = getASMINFor( iPN2_PART_NO_OEM, iPN2_MANUFACT_CD );
      linfors.getlNHASMBL().setASSMBL_BOM_ID( "0" );
      verifySCHEDPART( iTSIDs1, linfors, iACTV, "6", "REQ", false ); // information

      // Verify SCHED_INST_PART
      verifySCHEDINSTPART( iTSIDs1, linfors, "5", 1 );

      // OPER-21405 // Verify SCHED_RMVD_PART //
      invInfor lInvInfor = new invInfor();
      verifySCHEDRMVDPART( iTSIDs1, linfors, "IMSCHD", "6", 1, lInvInfor, false );

      // Verify AXON_DOMAIN_EVENT_ENTRY
      verifyTaskDrivingDeadlineRescheduledEventPublish();
      verifyTaskCreatedEventPublish();
   }


   /**
    * This test is to verify AL_OPEN_DEFERRED_FAULT_IMPORT validation functionality of staging table
    * AL_OPEN_DEFERRED_FAULT_IMPORT. SER part on ACFT.
    *
    *
    */

   public void testACFTSYSSER_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN3_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN3_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN3_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN3_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'7\'" );
      lFaults.put( "PN1_RMVD_QT", "\'8\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );
      lFaults.put( "FOUND_ON_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // setCurrentDate( iSN_ACFT );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify AL_OPEN_DEFERRED_FAULT_IMPORT import functionality of staging table
    * AL_OPEN_DEFERRED_FAULT_IMPORT. SER part on ACFT. OPER-21405 fix
    *
    *
    */
   @Test
   public void testACFTSYSSER_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testACFTSYSSER_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify EVT_EVENT table
      iCFIDs1 = verifyEVTEVENT( iEventTypeCdFault, iFAULT_SDESC, "CFDEFER", iFAULT_LDESC,
            iLOGBOOK_REFERENCE );

      iTSIDs1 = verifyEVTEVENT( iEventTypeCdTask, iFAULT_SDESC, "ACTV", iFAULT_LDESC, null );

      // Verify EVT_INV table
      invIDs lINVIDSFAULT = verifyEVTINV( iCFIDs1, iAssmbl_ACFT_CD, "2", "1" );
      invIDs lINVIDSTASK = verifyEVTINV( iTSIDs1, iAssmbl_ACFT_CD, "2", "1" );

      Assert.assertTrue( "Inventory Ids should be equal: ", lINVIDSFAULT.equals( lINVIDSTASK ) );

      // Verify the INV ids exists in INV_INV table
      String lQuery = "select 1 from " + TableUtil.INV_INV + " where INV_NO_DB_ID="
            + lINVIDSTASK.getINVIDs().getNO_DB_ID() + " and INV_NO_ID="
            + lINVIDSTASK.getINVIDs().getNO_ID() + " and ASSMBL_DB_ID="
            + lINVIDSTASK.getASSMBLINVIDs().getNO_DB_ID() + " and ASSMBL_INV_NO_ID="
            + lINVIDSTASK.getASSMBLINVIDs().getNO_ID();
      Assert.assertTrue( "Check inventory table to verify the record exists",
            RecordsExist( lQuery ) );

      // Verify EVT_EVENT_REL
      verifyEVTEVENT_REL( iCFIDs1, iTSIDs1, iREL_TYPE_CDFAULT );
      verifyEVTEVENT_REL( iTSIDs1, iTSIDs1, iREL_TYPE_CDTASK );

      // Verify EVT_STAGE table
      ArrayList<String> ltasklist = new ArrayList<String>();
      ltasklist.add( "UNASSIGNED FROM" );
      ltasklist.add( "NEW HOURS(HOUR) DEADLINE" );
      ltasklist.add( "NEW CDY(DAY) DEADLINE" );
      ltasklist.add( "THE TASK WAS CREATED" );
      ltasklist.add( "NEW CYCLES(CYCLES) DEADLINE" );
      ltasklist.add( "THE TASK HAS BEEN DEFERRED" );

      verifyEVTSTAGE( iTSIDs1, ltasklist );

      ArrayList<String> lfaultlist = new ArrayList<String>();
      lfaultlist.add( "CORRECTIVE ACTION WAS DEFERRED" );
      lfaultlist.add( "CORRECTIVE ACTION WAS UNASSIGNED" );
      lfaultlist.add( "FAULT WAS CREATED" );

      verifyEVTSTAGE( iCFIDs1, lfaultlist );

      // Verify EVT_SCHED_DEAD
      // get Ids of HOUR/CYCLES/DAY in mim_data_type table
      simpleIDs lHoursIds = getMIMType( "HOURS" );
      simpleIDs lCycIds = getMIMType( "CYCLES" );
      simpleIDs lDayIds = getMIMType( "CDY" );
      verifyEVTSCHEDDEAD( iTSIDs1, lHoursIds );
      verifyEVTSCHEDDEAD( iTSIDs1, lCycIds );
      verifyEVTSCHEDDEAD( iTSIDs1, lDayIds );

      // Verify SCHED_STASK table
      simpleIDs lPriorityIds = getPriorityIDs( iTASK_PRIORITY_CD );
      simpleIDs lTaskCDs = getTaskCDs( "CORR" );
      verifySCHEDSTASK( iTSIDs1, iCFIDs1, lPriorityIds, lTaskCDs, lINVIDSTASK.getINVIDs() );

      // Verify SD_FAULT
      verifySDFAULT( iCFIDs1, iFAIL_DEFER_CD, iFAULT_SOURCE_CD, iFAIL_SEV_CD, iDEFER_REF_SDESC,
            iOP_RESTRICTION_LDESC, iDEFER_CD_SDESC, iFAULT_LOG_TYPE_CD, iFRM_CD );

      // Verify sched_labour table
      String iLabor = getParmValue();
      if ( StringUtils.isBlank( iLabor ) ) {
         iLabourID1 = verifySCHEDLABOUR( iTSIDs1, iACTV, iLBR );
      } else if ( !RecordsExist(
            "SELECT 1 FROM  ref_labour_skill WHERE labour_skill_cd='" + iLabor + "'" ) ) {
         iLabourID1 = verifySCHEDLABOUR( iTSIDs1, iACTV, iLBR );

      } else {
         iLabourID1 = verifySCHEDLABOUR( iTSIDs1, iACTV, iLabor );
      }

      // Verify SCHED_LABOUR_ROLE table
      iLabourRoleID1 = verifySCHEDLABOURROLE( iLabourID1, iTECH );

      // Verify SCHED_LABOUR_ROLE_STATUS table
      verifySCHEDLABOURROLESTATUS( iLabourRoleID1, iACTV );

      // Verify SCHED_PART
      asmInfor linfors = getASMINFor( iPN3_PART_NO_OEM, iPN3_MANUFACT_CD );
      linfors.getlNHASMBL().setASSMBL_BOM_ID( "0" );
      verifySCHEDPART( iTSIDs1, linfors, iACTV, "8", "REQ", false );

      // Verify SCHED_INST_PART
      verifySCHEDINSTPART( iTSIDs1, linfors, "1", 7 );

      // // OPER-21405
      // Verify SCHED_RMVD_PART
      // invInfor lInvInfor = getINVINFor( iPN3_PART_NO_OEM, iPN3_MANUFACT_CD, iSN_ACFT );
      invInfor lInvInfor = new invInfor();
      verifySCHEDRMVDPART( iTSIDs1, linfors, "IMSCHD", "1", 8, lInvInfor, false );

      // Verify AXON_DOMAIN_EVENT_ENTRY
      verifyTaskDrivingDeadlineRescheduledEventPublish();
      verifyTaskCreatedEventPublish();
   }


   /**
    * This test is to verify AL_OPEN_DEFERRED_FAULT_IMPORT validation functionality of staging table
    * AL_OPEN_DEFERRED_FAULT_IMPORT. Multiple parts on ACFT.
    *
    *
    */

   public void testACFTMultipleParts_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );
      lFaults.put( "FOUND_ON_DT", "SYSDATE" );
      // PN1
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      // PN2
      lFaults.put( "PN2_PART_NO_OEM", "\'" + iPN2_PART_NO_OEM + "\'" );
      lFaults.put( "PN2_MANUFACT_CD", "\'" + iPN2_MANUFACT_CD + "\'" );
      lFaults.put( "PN2_IPC_REF_CD", "\'" + iPN2_IPC_REF_CD + "\'" );
      lFaults.put( "PN2_POS_CD", "\'" + iPN2_POS_CD + "\'" );
      lFaults.put( "PN2_INST_QT", "\'5\'" );
      lFaults.put( "PN2_RMVD_QT", "\'6\'" );
      // PN3
      lFaults.put( "PN3_PART_NO_OEM", "\'" + iPN3_PART_NO_OEM + "\'" );
      lFaults.put( "PN3_MANUFACT_CD", "\'" + iPN3_MANUFACT_CD + "\'" );
      lFaults.put( "PN3_IPC_REF_CD", "\'" + iPN3_IPC_REF_CD + "\'" );
      lFaults.put( "PN3_POS_CD", "\'" + iPN3_POS_CD + "\'" );
      lFaults.put( "PN3_INST_QT", "\'7\'" );
      lFaults.put( "PN3_RMVD_QT", "\'8\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // setCurrentDate( iSN_ACFT );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify AL_OPEN_DEFERRED_FAULT_IMPORT import functionality of staging table
    * AL_OPEN_DEFERRED_FAULT_IMPORT. SER part on ACFT.
    *
    *
    */
   @Test
   public void testACFTMultipleParts_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testACFTMultipleParts_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify EVT_EVENT table
      iCFIDs1 = verifyEVTEVENT( iEventTypeCdFault, iFAULT_SDESC, "CFDEFER", iFAULT_LDESC,
            iLOGBOOK_REFERENCE );

      iTSIDs1 = verifyEVTEVENT( iEventTypeCdTask, iFAULT_SDESC, "ACTV", iFAULT_LDESC, null );

      // Verify EVT_INV table
      invIDs lINVIDSFAULT = verifyEVTINV( iCFIDs1, iAssmbl_ACFT_CD, "2", "1" );
      invIDs lINVIDSTASK = verifyEVTINV( iTSIDs1, iAssmbl_ACFT_CD, "2", "1" );

      Assert.assertTrue( "Inventory Ids should be equal: ", lINVIDSFAULT.equals( lINVIDSTASK ) );

      // Verify the INV ids exists in INV_INV table
      String lQuery = "select 1 from " + TableUtil.INV_INV + " where INV_NO_DB_ID="
            + lINVIDSTASK.getINVIDs().getNO_DB_ID() + " and INV_NO_ID="
            + lINVIDSTASK.getINVIDs().getNO_ID() + " and ASSMBL_DB_ID="
            + lINVIDSTASK.getASSMBLINVIDs().getNO_DB_ID() + " and ASSMBL_INV_NO_ID="
            + lINVIDSTASK.getASSMBLINVIDs().getNO_ID();
      Assert.assertTrue( "Check inventory table to verify the record exists",
            RecordsExist( lQuery ) );

      // Verify EVT_EVENT_REL
      verifyEVTEVENT_REL( iCFIDs1, iTSIDs1, iREL_TYPE_CDFAULT );
      verifyEVTEVENT_REL( iTSIDs1, iTSIDs1, iREL_TYPE_CDTASK );

      // Verify EVT_STAGE table
      ArrayList<String> ltasklist = new ArrayList<String>();
      ltasklist.add( "UNASSIGNED FROM" );
      ltasklist.add( "NEW HOURS(HOUR) DEADLINE" );
      ltasklist.add( "NEW CDY(DAY) DEADLINE" );
      ltasklist.add( "THE TASK WAS CREATED" );
      ltasklist.add( "NEW CYCLES(CYCLES) DEADLINE" );
      ltasklist.add( "THE TASK HAS BEEN DEFERRED" );

      verifyEVTSTAGE( iTSIDs1, ltasklist );

      ArrayList<String> lfaultlist = new ArrayList<String>();
      lfaultlist.add( "CORRECTIVE ACTION WAS DEFERRED" );
      lfaultlist.add( "CORRECTIVE ACTION WAS UNASSIGNED" );
      lfaultlist.add( "FAULT WAS CREATED" );

      verifyEVTSTAGE( iCFIDs1, lfaultlist );

      // Verify EVT_SCHED_DEAD
      // get Ids of HOUR/CYCLES/DAY in mim_data_type table
      simpleIDs lHoursIds = getMIMType( "HOURS" );
      simpleIDs lCycIds = getMIMType( "CYCLES" );
      simpleIDs lDayIds = getMIMType( "CDY" );
      verifyEVTSCHEDDEAD( iTSIDs1, lHoursIds );
      verifyEVTSCHEDDEAD( iTSIDs1, lCycIds );
      verifyEVTSCHEDDEAD( iTSIDs1, lDayIds );

      // Verify SCHED_STASK table
      simpleIDs lPriorityIds = getPriorityIDs( iTASK_PRIORITY_CD );
      simpleIDs lTaskCDs = getTaskCDs( "CORR" );
      verifySCHEDSTASK( iTSIDs1, iCFIDs1, lPriorityIds, lTaskCDs, lINVIDSTASK.getINVIDs() );

      // Verify SD_FAULT
      verifySDFAULT( iCFIDs1, iFAIL_DEFER_CD, iFAULT_SOURCE_CD, iFAIL_SEV_CD, iDEFER_REF_SDESC,
            iOP_RESTRICTION_LDESC, iDEFER_CD_SDESC, iFAULT_LOG_TYPE_CD, iFRM_CD );

      // Verify sched_labour table
      String iLabor = getParmValue();
      if ( StringUtils.isBlank( iLabor ) ) {
         iLabourID1 = verifySCHEDLABOUR( iTSIDs1, iACTV, iLBR );
      } else if ( !RecordsExist(
            "SELECT 1 FROM  ref_labour_skill WHERE labour_skill_cd='" + iLabor + "'" ) ) {
         iLabourID1 = verifySCHEDLABOUR( iTSIDs1, iACTV, iLBR );

      } else {
         iLabourID1 = verifySCHEDLABOUR( iTSIDs1, iACTV, iLabor );
      }

      // Verify SCHED_LABOUR_ROLE table
      iLabourRoleID1 = verifySCHEDLABOURROLE( iLabourID1, iTECH );

      // Verify SCHED_LABOUR_ROLE_STATUS table
      verifySCHEDLABOURROLESTATUS( iLabourRoleID1, iACTV );

      // Verify SCHED_PART
      asmInfor linfors1 = getASMINFor( iPN1_PART_NO_OEM, iPN1_MANUFACT_CD );
      asmInfor linfors2 = getASMINFor( iPN2_PART_NO_OEM, iPN2_MANUFACT_CD );
      linfors2.getlNHASMBL().setASSMBL_BOM_ID( "0" );
      asmInfor linfors3 = getASMINFor( iPN3_PART_NO_OEM, iPN3_MANUFACT_CD );
      linfors3.getlNHASMBL().setASSMBL_BOM_ID( "0" );
      String lschedID1 = verifySCHEDPART_MultipleParts( iTSIDs1, linfors1, iACTV, "1", "REQ" );
      String lschedID2 = verifySCHEDPART_MultipleParts( iTSIDs1, linfors2, iACTV, "6", "REQ" );
      String lschedID3 = verifySCHEDPART_MultipleParts( iTSIDs1, linfors3, iACTV, "8", "REQ" );

      // Verify SCHED_INST_PART
      verifySCHEDINSTPART_MultipleParts( iTSIDs1, lschedID1, "1", 1 );
      verifySCHEDINSTPART_MultipleParts( iTSIDs1, lschedID2, "5", 1 );
      verifySCHEDINSTPART_MultipleParts( iTSIDs1, lschedID3, "1", 7 );

      // OPER-21405
      // Verify SCHED_RMVD_PART
      invInfor lInvInfor1 = getINVINFor( iPN1_PART_NO_OEM, iPN1_MANUFACT_CD, iSN_ACFT );
      // invInfor lInvInfor2 = getINVINFor( iPN2_PART_NO_OEM, iPN2_MANUFACT_CD, iSN_ACFT );
      // invInfor lInvInfor3 = getINVINFor( iPN3_PART_NO_OEM, iPN3_MANUFACT_CD, iSN_ACFT );
      invInfor lInvInfor2 = new invInfor();
      invInfor lInvInfor3 = new invInfor();
      verifySCHEDRMVDPART( iTSIDs1, lschedID1, "IMSCHD", "1", 1, lInvInfor1, true );
      verifySCHEDRMVDPART( iTSIDs1, lschedID2, "IMSCHD", "6", 1, lInvInfor2, false );
      verifySCHEDRMVDPART( iTSIDs1, lschedID3, "IMSCHD", "1", 8, lInvInfor3, false );

      // Verify AXON_DOMAIN_EVENT_ENTRY
      verifyTaskDrivingDeadlineRescheduledEventPublish();
      verifyTaskCreatedEventPublish();
   }


   /**
    * This test is to verify AL_OPEN_DEFERRED_FAULT_IMPORT validation functionality of staging table
    * AL_OPEN_DEFERRED_FAULT_IMPORT with multiple aircraft. TRK part on ACFT.
    *
    *
    */

   public void testMultipleACFTSYSTRK_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );
      lFaults.put( "FOUND_ON_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // setCurrentDate( iSN_ACFT );

      // Insert second record
      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT2 + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC2 + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC2 + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE2 + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );
      lFaults.put( "FOUND_ON_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify AL_OPEN_DEFERRED_FAULT_IMPORT import functionality of staging table
    * AL_OPEN_DEFERRED_FAULT_IMPORT with multiple aircraft. TRK part on ACFT.
    *
    *
    */
   @Test
   public void testMultipleACFTSYSTRK_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testMultipleACFTSYSTRK_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      verifyImport();

      // Verify AXON_DOMAIN_EVENT_ENTRY
      verifyTaskDrivingDeadlineRescheduledEventPublish();
      verifyTaskCreatedEventPublish();
   }


   /**
    * This test is to verify AL_OPEN_DEFERRED_FAULT_IMPORT validation functionality of staging table
    * AL_OPEN_DEFERRED_FAULT_IMPORT. TRK part on ACFT without ATA_SYS_CD.
    *
    *
    */

   public void testACFT_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      // lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );
      lFaults.put( "FOUND_ON_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify AL_OPEN_DEFERRED_FAULT_IMPORT import functionality of staging table
    * AL_OPEN_DEFERRED_FAULT_IMPORT. TRK part on ACFT without ATA_SYS_CD.
    *
    *
    */
   @Test
   public void testACFT_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testACFT_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify EVT_EVENT table
      iCFIDs1 = verifyEVTEVENT( iEventTypeCdFault, iFAULT_SDESC, "CFDEFER", iFAULT_LDESC,
            iLOGBOOK_REFERENCE );

      iTSIDs1 = verifyEVTEVENT( iEventTypeCdTask, iFAULT_SDESC, "ACTV", iFAULT_LDESC, null );

      // Verify EVT_INV table
      invIDs lINVIDSFAULT = verifyEVTINV( iCFIDs1, iAssmbl_ACFT_CD, "0", "1" );
      invIDs lINVIDSTASK = verifyEVTINV( iTSIDs1, iAssmbl_ACFT_CD, "0", "1" );

      Assert.assertTrue( "Inventory Ids should be equal: ", lINVIDSFAULT.equals( lINVIDSTASK ) );

      // Verify the INV ids exists in INV_INV table
      String lQuery = "select 1 from " + TableUtil.INV_INV + " where INV_NO_DB_ID="
            + lINVIDSTASK.getINVIDs().getNO_DB_ID() + " and INV_NO_ID="
            + lINVIDSTASK.getINVIDs().getNO_ID() + " and ASSMBL_DB_ID="
            + lINVIDSTASK.getASSMBLINVIDs().getNO_DB_ID() + " and ASSMBL_INV_NO_ID="
            + lINVIDSTASK.getASSMBLINVIDs().getNO_ID();
      Assert.assertTrue( "Check inventory table to verify the record exists",
            RecordsExist( lQuery ) );

      // Verify EVT_EVENT_REL
      verifyEVTEVENT_REL( iCFIDs1, iTSIDs1, iREL_TYPE_CDFAULT );
      verifyEVTEVENT_REL( iTSIDs1, iTSIDs1, iREL_TYPE_CDTASK );

      // Verify EVT_STAGE table
      ArrayList<String> ltasklist = new ArrayList<String>();
      ltasklist.add( "UNASSIGNED FROM" );
      ltasklist.add( "NEW HOURS(HOUR) DEADLINE" );
      ltasklist.add( "NEW CDY(DAY) DEADLINE" );
      ltasklist.add( "THE TASK WAS CREATED" );
      ltasklist.add( "NEW CYCLES(CYCLES) DEADLINE" );
      ltasklist.add( "THE TASK HAS BEEN DEFERRED" );

      verifyEVTSTAGE( iTSIDs1, ltasklist );

      ArrayList<String> lfaultlist = new ArrayList<String>();
      lfaultlist.add( "CORRECTIVE ACTION WAS DEFERRED" );
      lfaultlist.add( "CORRECTIVE ACTION WAS UNASSIGNED" );
      lfaultlist.add( "FAULT WAS CREATED" );

      verifyEVTSTAGE( iCFIDs1, lfaultlist );

      // Verify EVT_SCHED_DEAD
      // get Ids of HOUR/CYCLES/DAY in mim_data_type table
      simpleIDs lHoursIds = getMIMType( "HOURS" );
      // simpleIDs lCycIds = getMIMType( "CYCLES" );
      simpleIDs lDayIds = getMIMType( "CDY" );
      verifyEVTSCHEDDEAD( iTSIDs1, lHoursIds );
      // verifyEVTSCHEDDEAD( iTSIDs1, lCycIds );
      verifyEVTSCHEDDEAD( iTSIDs1, lDayIds );

      // Verify SCHED_STASK table
      simpleIDs lPriorityIds = getPriorityIDs( iTASK_PRIORITY_CD );
      simpleIDs lTaskCDs = getTaskCDs( "CORR" );
      verifySCHEDSTASK( iTSIDs1, iCFIDs1, lPriorityIds, lTaskCDs, lINVIDSTASK.getINVIDs() );

      // Verify SD_FAULT
      verifySDFAULT( iCFIDs1, iFAIL_DEFER_CD, iFAULT_SOURCE_CD, iFAIL_SEV_CD, iDEFER_REF_SDESC,
            iOP_RESTRICTION_LDESC, iDEFER_CD_SDESC, iFAULT_LOG_TYPE_CD, iFRM_CD );

      // Verify sched_labour table
      String iLabor = getParmValue();
      if ( StringUtils.isBlank( iLabor ) ) {
         iLabourID1 = verifySCHEDLABOUR( iTSIDs1, iACTV, iLBR );
      } else if ( !RecordsExist(
            "SELECT 1 FROM  ref_labour_skill WHERE labour_skill_cd='" + iLabor + "'" ) ) {
         iLabourID1 = verifySCHEDLABOUR( iTSIDs1, iACTV, iLBR );

      } else {
         iLabourID1 = verifySCHEDLABOUR( iTSIDs1, iACTV, iLabor );
      }

      // Verify SCHED_LABOUR_ROLE table
      iLabourRoleID1 = verifySCHEDLABOURROLE( iLabourID1, iTECH );

      // Verify SCHED_LABOUR_ROLE_STATUS table
      verifySCHEDLABOURROLESTATUS( iLabourRoleID1, iACTV );

      // Verify SCHED_PART
      asmInfor linfors = getASMINFor( iPN1_PART_NO_OEM, iPN1_MANUFACT_CD );
      verifySCHEDPART( iTSIDs1, linfors, iACTV, "1", "REQ", true );

      // Verify SCHED_INST_PART
      verifySCHEDINSTPART( iTSIDs1, linfors, "1", 1 );

      // Verify SCHED_RMVD_PART
      invInfor lInvInfor = getINVINFor( iPN1_PART_NO_OEM, iPN1_MANUFACT_CD, iSN_ACFT );
      verifySCHEDRMVDPART( iTSIDs1, linfors, "IMSCHD", "1", 1, lInvInfor, true );

      // Verify AXON_DOMAIN_EVENT_ENTRY
      verifyTaskDrivingDeadlineRescheduledEventPublish();
      verifyTaskCreatedEventPublish();
   }


   /**
    * This test is to verify OPER-27627: Actuals Loader Faults import uses IMSCHD removal reason
    * instead of taking the value from the config parm. when parm value is invalid.
    *
    *
    */

   public void testOPER_27627_INVALID_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );
      lFaults.put( "FOUND_ON_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify OPER-27627: Actuals Loader Faults import uses IMSCHD removal reason
    * instead of taking the value from the config parm. when parm value is invalid.
    *
    *
    */
   @Test
   public void testOPER_27627_INVALID_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testOPER_27627_INVALID_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify EVT_EVENT table
      iCFIDs1 = verifyEVTEVENT( iEventTypeCdFault, iFAULT_SDESC, "CFDEFER", iFAULT_LDESC,
            iLOGBOOK_REFERENCE );

      iTSIDs1 = verifyEVTEVENT( iEventTypeCdTask, iFAULT_SDESC, "ACTV", iFAULT_LDESC, null );

      // Verify EVT_INV table
      invIDs lINVIDSFAULT = verifyEVTINV( iCFIDs1, iAssmbl_ACFT_CD, "2", "1" );
      invIDs lINVIDSTASK = verifyEVTINV( iTSIDs1, iAssmbl_ACFT_CD, "2", "1" );

      Assert.assertTrue( "Inventory Ids should be equal: ", lINVIDSFAULT.equals( lINVIDSTASK ) );

      // Verify the INV ids exists in INV_INV table
      String lQuery = "select 1 from " + TableUtil.INV_INV + " where INV_NO_DB_ID="
            + lINVIDSTASK.getINVIDs().getNO_DB_ID() + " and INV_NO_ID="
            + lINVIDSTASK.getINVIDs().getNO_ID() + " and ASSMBL_DB_ID="
            + lINVIDSTASK.getASSMBLINVIDs().getNO_DB_ID() + " and ASSMBL_INV_NO_ID="
            + lINVIDSTASK.getASSMBLINVIDs().getNO_ID();
      Assert.assertTrue( "Check inventory table to verify the record exists",
            RecordsExist( lQuery ) );

      // Verify EVT_EVENT_REL
      verifyEVTEVENT_REL( iCFIDs1, iTSIDs1, iREL_TYPE_CDFAULT );
      verifyEVTEVENT_REL( iTSIDs1, iTSIDs1, iREL_TYPE_CDTASK );

      // Verify EVT_STAGE table
      ArrayList<String> ltasklist = new ArrayList<String>();
      ltasklist.add( "UNASSIGNED FROM" );
      ltasklist.add( "NEW HOURS(HOUR) DEADLINE" );
      ltasklist.add( "NEW CDY(DAY) DEADLINE" );
      ltasklist.add( "THE TASK WAS CREATED" );
      ltasklist.add( "NEW CYCLES(CYCLES) DEADLINE" );
      ltasklist.add( "THE TASK HAS BEEN DEFERRED" );

      verifyEVTSTAGE( iTSIDs1, ltasklist );

      ArrayList<String> lfaultlist = new ArrayList<String>();
      lfaultlist.add( "CORRECTIVE ACTION WAS DEFERRED" );
      lfaultlist.add( "CORRECTIVE ACTION WAS UNASSIGNED" );
      lfaultlist.add( "FAULT WAS CREATED" );

      verifyEVTSTAGE( iCFIDs1, lfaultlist );

      // Verify EVT_SCHED_DEAD
      // get Ids of HOUR/CYCLES/DAY in mim_data_type table
      simpleIDs lHoursIds = getMIMType( "HOURS" );
      simpleIDs lCycIds = getMIMType( "CYCLES" );
      simpleIDs lDayIds = getMIMType( "CDY" );
      verifyEVTSCHEDDEAD( iTSIDs1, lHoursIds );
      verifyEVTSCHEDDEAD( iTSIDs1, lCycIds );
      verifyEVTSCHEDDEAD( iTSIDs1, lDayIds );

      // Verify SCHED_STASK table
      simpleIDs lPriorityIds = getPriorityIDs( iTASK_PRIORITY_CD );
      simpleIDs lTaskCDs = getTaskCDs( "CORR" );
      verifySCHEDSTASK( iTSIDs1, iCFIDs1, lPriorityIds, lTaskCDs, lINVIDSTASK.getINVIDs() );

      // Verify SD_FAULT
      verifySDFAULT( iCFIDs1, iFAIL_DEFER_CD, iFAULT_SOURCE_CD, iFAIL_SEV_CD, iDEFER_REF_SDESC,
            iOP_RESTRICTION_LDESC, iDEFER_CD_SDESC, iFAULT_LOG_TYPE_CD, iFRM_CD );

      // Verify sched_labour table
      String iLabor = getParmValue();
      if ( StringUtils.isBlank( iLabor ) ) {
         iLabourID1 = verifySCHEDLABOUR( iTSIDs1, iACTV, iLBR );
      } else if ( !RecordsExist(
            "SELECT 1 FROM  ref_labour_skill WHERE labour_skill_cd='" + iLabor + "'" ) ) {
         iLabourID1 = verifySCHEDLABOUR( iTSIDs1, iACTV, iLBR );

      } else {
         iLabourID1 = verifySCHEDLABOUR( iTSIDs1, iACTV, iLabor );
      }

      // Verify SCHED_LABOUR_ROLE table
      iLabourRoleID1 = verifySCHEDLABOURROLE( iLabourID1, iTECH );

      // Verify SCHED_LABOUR_ROLE_STATUS table
      verifySCHEDLABOURROLESTATUS( iLabourRoleID1, iACTV );

      // Verify SCHED_PART
      asmInfor linfors = getASMINFor( iPN1_PART_NO_OEM, iPN1_MANUFACT_CD );
      verifySCHEDPART( iTSIDs1, linfors, iACTV, "1", "REQ", true );

      // Verify SCHED_INST_PART
      verifySCHEDINSTPART( iTSIDs1, linfors, "1", 1 );

      // Verify SCHED_RMVD_PART
      invInfor lInvInfor = getINVINFor( iPN1_PART_NO_OEM, iPN1_MANUFACT_CD, iSN_ACFT );
      verifySCHEDRMVDPART( iTSIDs1, linfors, "IMSCHD", "1", 1, lInvInfor, true );

      // Verify AXON_DOMAIN_EVENT_ENTRY
      verifyTaskDrivingDeadlineRescheduledEventPublish();
      verifyTaskCreatedEventPublish();
   }


   /**
    * This test is to verify OPER-27627: Actuals Loader Faults import uses IMSCHD removal reason
    * instead of taking the value from the config parm. when parm value is invalid.
    *
    *
    */

   public void testOPER_27627_DUPLICATE_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOPER_27627_INVALID_VALIDATION();

   }


   /**
    * This test is to verify OPER-27627: Actuals Loader Faults import uses IMSCHD removal reason
    * instead of taking the value from the config parm. when parm value is invalid.
    *
    *
    */
   @Test
   public void testOPER_27627_DUPLICATE_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOPER_27627_DUPLICATE_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify EVT_EVENT table
      iCFIDs1 = verifyEVTEVENT( iEventTypeCdFault, iFAULT_SDESC, "CFDEFER", iFAULT_LDESC,
            iLOGBOOK_REFERENCE );

      iTSIDs1 = verifyEVTEVENT( iEventTypeCdTask, iFAULT_SDESC, "ACTV", iFAULT_LDESC, null );

      // Verify EVT_INV table
      invIDs lINVIDSFAULT = verifyEVTINV( iCFIDs1, iAssmbl_ACFT_CD, "2", "1" );
      invIDs lINVIDSTASK = verifyEVTINV( iTSIDs1, iAssmbl_ACFT_CD, "2", "1" );

      Assert.assertTrue( "Inventory Ids should be equal: ", lINVIDSFAULT.equals( lINVIDSTASK ) );

      // Verify the INV ids exists in INV_INV table
      String lQuery = "select 1 from " + TableUtil.INV_INV + " where INV_NO_DB_ID="
            + lINVIDSTASK.getINVIDs().getNO_DB_ID() + " and INV_NO_ID="
            + lINVIDSTASK.getINVIDs().getNO_ID() + " and ASSMBL_DB_ID="
            + lINVIDSTASK.getASSMBLINVIDs().getNO_DB_ID() + " and ASSMBL_INV_NO_ID="
            + lINVIDSTASK.getASSMBLINVIDs().getNO_ID();
      Assert.assertTrue( "Check inventory table to verify the record exists",
            RecordsExist( lQuery ) );

      // Verify EVT_EVENT_REL
      verifyEVTEVENT_REL( iCFIDs1, iTSIDs1, iREL_TYPE_CDFAULT );
      verifyEVTEVENT_REL( iTSIDs1, iTSIDs1, iREL_TYPE_CDTASK );

      // Verify EVT_STAGE table
      ArrayList<String> ltasklist = new ArrayList<String>();
      ltasklist.add( "UNASSIGNED FROM" );
      ltasklist.add( "NEW HOURS(HOUR) DEADLINE" );
      ltasklist.add( "NEW CDY(DAY) DEADLINE" );
      ltasklist.add( "THE TASK WAS CREATED" );
      ltasklist.add( "NEW CYCLES(CYCLES) DEADLINE" );
      ltasklist.add( "THE TASK HAS BEEN DEFERRED" );

      verifyEVTSTAGE( iTSIDs1, ltasklist );

      ArrayList<String> lfaultlist = new ArrayList<String>();
      lfaultlist.add( "CORRECTIVE ACTION WAS DEFERRED" );
      lfaultlist.add( "CORRECTIVE ACTION WAS UNASSIGNED" );
      lfaultlist.add( "FAULT WAS CREATED" );

      verifyEVTSTAGE( iCFIDs1, lfaultlist );

      // Verify EVT_SCHED_DEAD
      // get Ids of HOUR/CYCLES/DAY in mim_data_type table
      simpleIDs lHoursIds = getMIMType( "HOURS" );
      simpleIDs lCycIds = getMIMType( "CYCLES" );
      simpleIDs lDayIds = getMIMType( "CDY" );
      verifyEVTSCHEDDEAD( iTSIDs1, lHoursIds );
      verifyEVTSCHEDDEAD( iTSIDs1, lCycIds );
      verifyEVTSCHEDDEAD( iTSIDs1, lDayIds );

      // Verify SCHED_STASK table
      simpleIDs lPriorityIds = getPriorityIDs( iTASK_PRIORITY_CD );
      simpleIDs lTaskCDs = getTaskCDs( "CORR" );
      verifySCHEDSTASK( iTSIDs1, iCFIDs1, lPriorityIds, lTaskCDs, lINVIDSTASK.getINVIDs() );

      // Verify SD_FAULT
      verifySDFAULT( iCFIDs1, iFAIL_DEFER_CD, iFAULT_SOURCE_CD, iFAIL_SEV_CD, iDEFER_REF_SDESC,
            iOP_RESTRICTION_LDESC, iDEFER_CD_SDESC, iFAULT_LOG_TYPE_CD, iFRM_CD );

      // Verify sched_labour table
      String iLabor = getParmValue();
      if ( StringUtils.isBlank( iLabor ) ) {
         iLabourID1 = verifySCHEDLABOUR( iTSIDs1, iACTV, iLBR );
      } else if ( !RecordsExist(
            "SELECT 1 FROM ref_labour_skill WHERE labour_skill_cd='" + iLabor + "'" ) ) {
         iLabourID1 = verifySCHEDLABOUR( iTSIDs1, iACTV, iLBR );

      } else {
         iLabourID1 = verifySCHEDLABOUR( iTSIDs1, iACTV, iLabor );
      }

      // Verify SCHED_LABOUR_ROLE table
      iLabourRoleID1 = verifySCHEDLABOURROLE( iLabourID1, iTECH );

      // Verify SCHED_LABOUR_ROLE_STATUS table
      verifySCHEDLABOURROLESTATUS( iLabourRoleID1, iACTV );

      // Verify SCHED_PART
      asmInfor linfors = getASMINFor( iPN1_PART_NO_OEM, iPN1_MANUFACT_CD );
      verifySCHEDPART( iTSIDs1, linfors, iACTV, "1", "REQ", true );

      // Verify SCHED_INST_PART
      verifySCHEDINSTPART( iTSIDs1, linfors, "1", 1 );

      // Verify SCHED_RMVD_PART
      invInfor lInvInfor = getINVINFor( iPN1_PART_NO_OEM, iPN1_MANUFACT_CD, iSN_ACFT );
      verifySCHEDRMVDPART( iTSIDs1, linfors, "IMSCHD", "1", 1, lInvInfor, true );

      // Verify AXON_DOMAIN_EVENT_ENTRY
      verifyTaskDrivingDeadlineRescheduledEventPublish();
      verifyTaskCreatedEventPublish();
   }


   /**
    * This test is to verify a.load_odf.bat is to load data from csv file to staging table properly.
    *
    *
    */

   public void testLoadCSV() {

      BatchFileManager lFileMgr = new BatchFileManager();
      lFileMgr.copyFile( TestConstants.TEST_CASE_DATA, TestConstants.ODF_CSV_FILE,
            TestConstants.ODF_BATCH_FILE + "\\data\\" );
      lFileMgr.loadODFViaDataFile( TestConstants.ODF_CSV_FILE );

      // Verify AL_OPEN_DEFERRED_FAULT is loaded
      verifyStagingTable();

   }


   /**
    * This test is to verify b.validate_odf.bat works exactly same way as
    * AL_OPEN_DEFERRED_FAULT_import.validate_open_deferred_fault .
    *
    *
    */

   public void testValidateCSV() {

      testLoadCSV();
      BatchFileManager lFileMgr = new BatchFileManager();
      lFileMgr.validateODFViaBatch();
      verifyValidation();

   }


   /**
    * This test is to verify b.validate_odf.bat works exactly same way as
    * AL_OPEN_DEFERRED_FAULT_import.validate_open_deferred_fault .
    *
    *
    */
   @Test
   public void testImportCSV() {

      testValidateCSV();
      BatchFileManager lFileMgr = new BatchFileManager();
      lFileMgr.importODFViaBatch();

      verifyImport();

      // Verify AXON_DOMAIN_EVENT_ENTRY
      verifyTaskDrivingDeadlineRescheduledEventPublish();
      verifyTaskCreatedEventPublish();
   }


   /**
    * This test is to verify valid error code 10001:C_PROAL_OPEN_DEFERRED_FAULT.serial_no_oem cannot
    * be NULL or consist entirely of spaces.
    *
    *
    */

   @Test
   public void test_AMLODF_10001() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      // lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "SERIAL_NO_OEM", null );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-10001" );

   }


   /**
    * This test is to verify valid error code 10002:C_PROAL_OPEN_DEFERRED_FAULT.part_no_oem cannot
    * be NULL or consist entirely of spaces.
    *
    *
    */

   @Test
   public void test_AMLODF_10002() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      // lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", null );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-10002" );

   }


   /**
    * This test is to verify valid error code 10004:C_PROAL_OPEN_DEFERRED_FAULT.fault_sdesc cannot
    * be NULL or consist entirely of spaces.
    *
    *
    */

   @Test
   public void test_AMLODF_10004() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      // lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_SDESC", null );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-10004" );

   }


   /**
    * This test is to verify valid error code 10005:C_PROAL_OPEN_DEFERRED_FAULT.fail_sev_cd be NULL
    * or consist entirely of spaces.
    *
    *
    */

   @Test
   public void test_AMLODF_10005() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      // lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_SEV_CD", null );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-10005" );

   }


   /**
    * This test is to verify valid error code 10006:C_PROAL_OPEN_DEFERRED_FAULT.defer_ref_sdesc
    * cannot be NULL when fault severity type is MEL.
    *
    *
    */

   @Test
   public void test_AMLODF_10006() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      // lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "DEFER_REF_SDESC", null );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-10006" );

   }

   //
   // /**
   // * This test is to verify valid error code 10007:C_PROAL_OPEN_DEFERRED_FAULT.due_dt, hours
   // * deadline and cycles deadline cannot be NULL when fault severity type is MEL.
   // *
   // *
   // */
   //
   // @Test
   // public void test_AMLODF_10007() {
   //
   // System.out.println( "=======Starting: " + testName.getMethodName()
   // + " Validation========================" );
   //
   // // c_org_hr_lic table
   // Map<String, String> lFaults = new LinkedHashMap<>();
   //
   // lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
   // lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
   // lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
   // lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
   // lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
   // lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
   // lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
   // lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
   // lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
   // lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
   // lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
   // // lFaults.put( "HOURS_DUE_QT", "\'1\'" );
   // // lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
   // // lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
   // // lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
   // lFaults.put( "HOURS_DUE_QT", null );
   // lFaults.put( "HOURS_INTERVAL_QT", null );
   // lFaults.put( "CYCLES_DUE_QT", null );
   // lFaults.put( "CYCLES_INTERVAL_QT", null );
   // lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
   // lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
   // lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
   // lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
   // lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
   // lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
   // lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
   // lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
   // lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
   // lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
   // lFaults.put( "PN1_INST_QT", "\'1\'" );
   // lFaults.put( "PN1_RMVD_QT", "\'1\'" );
   // lFaults.put( "DUE_DT", null );
   //
   // // insert map
   // runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );
   //
   // // run validation
   // Assert.assertTrue( runValidationAndImport( true, true ) != 1 );
   //
   // checkODFErrorCode( testName.getMethodName(), "AMLODF-10007" );
   //
   // }


   /**
    * This test is to verify valid error code 10009:The installed quantity is NULL and will be
    * default to 1. Use 0 to indicate no installation required.
    *
    *
    */

   @Test
   public void test_AMLODF_10009() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      // lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_INST_QT", null );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );
      lFaults.put( "FOUND_ON_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-10009" );

   }


   /**
    * This test is to verify valid error code 10010:C_PROC_ODF_PART_REQUIREMENT.The removed quantity
    * is NULL and will be default to 1. Use 0 to indicate no removal required.
    *
    *
    */

   @Test
   public void test_AMLODF_10010() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      // lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );
      lFaults.put( "FOUND_ON_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-10010" );

   }


   /**
    * This test is to verify valid error code 11000:C_PROAL_OPEN_DEFERRED_FAULT.logbook_reference
    * must be unique across the staging area on different default.
    *
    *
    */

   @Test
   public void test_AMLODF_11000_TC1() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // second record
      // c_org_hr_lic table
      lFaults.clear();
      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "2'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "2'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN2_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN2_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN2_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN2_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'5\'" );
      lFaults.put( "PN1_RMVD_QT", "\'6\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-11000" );

   }


   /**
    * This test is to verify valid error code 11000:C_PROAL_OPEN_DEFERRED_FAULT.logbook_reference
    * must be unique across the staging area on same default with different row of record.
    *
    *
    */

   @Test
   public void test_AMLODF_11000_TC2() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // second record
      // c_org_hr_lic table
      lFaults.clear();
      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN2_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN2_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN2_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN2_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'5\'" );
      lFaults.put( "PN1_RMVD_QT", "\'6\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-11000" );

   }


   /**
    * This test is to verify valid error code 12001:CC_PROAL_OPEN_DEFERRED_FAULT.part_no_oem not
    * found in Maintenix EQP_PART_NO table
    *
    *
    */

   @Test
   public void test_AMLODF_12001() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      // lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "'Invalid'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-12001" );

   }


   /**
    * This test is to verify valid error code 12002:C_PROAL_OPEN_DEFERRED_FAULT.manufact_cd not
    * found in Maintenix EQP_MANUFACT table.
    *
    *
    */

   @Test
   public void test_AMLODF_12002() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      // lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "'Invalid'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-12002" );

   }


   /**
    * This test is to verify valid error code 12003:C_PROAL_OPEN_DEFERRED_FAULT Part Number /
    * Manufacturer combination not found in Maintenix.
    *
    *
    */

   @Test
   public void test_AMLODF_12003() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      // lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "1234567890" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );
      lFaults.put( "FOUND_ON_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-12003" );

   }


   /**
    * This test is to verify valid error code 12005:C_PROAL_OPEN_DEFERRED_FAULT Serial Number not
    * found in Maintenix.
    *
    *
    */

   @Test
   public void test_AMLODF_12005() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      // lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "'Invalid'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-12005" );

   }


   /**
    * This test is to verify valid error code 12006:C_PROAL_OPEN_DEFERRED_FAULT Serial Number / Part
    * Number / Manufacturer combination not found in Maintenix.
    *
    *
    */

   @Test
   public void test_AMLODF_12006() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      // lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "'SN000015'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-12006" );

   }


   /**
    * This test is to verify valid error code 12008:C_PROAL_OPEN_DEFERRED_FAULT.ata_sys_cd not found
    * in Maintenix EQP_ASSMBL_BOM table.
    *
    *
    */

   @Test
   public void test_AMLODF_12010() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      // lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "'ENG-SYS-1-1-TRK-BATCH-PARENT'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-12010" );

   }


   /**
    * This test is to verify valid error code 12011:C_PROAL_OPEN_DEFERRED_FAULT.fail_sev_cd not
    * found in Maintenix REF_FAIL_SEV table.
    *
    *
    */

   @Test
   public void test_AMLODF_12011() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      // lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_SEV_CD", "'Invalid'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-12011" );

   }


   /**
    * This test is to verify valid error code 12012:C_PROAL_OPEN_DEFERRED_FAULT.fail_defer_cd not
    * found in Maintenix REF_FAIL_DEFER table.
    *
    *
    */

   @Test
   public void test_AMLODF_12012() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      // lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "'Invalid'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-12012" );

   }


   /**
    * This test is to verify valid error code 12013:C_PROAL_OPEN_DEFERRED_FAULT.fault_source_cd not
    * found in Maintenix REF_FAULT_SOURCE table.
    *
    *
    */

   @Test
   public void test_AMLODF_12013() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      // lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "'Invalid'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-12013" );

   }


   /**
    * This test is to verify valid error code 12014:C_PROAL_OPEN_DEFERRED_FAULT.task_priority_cd not
    * found in Maintenix REF_TASK_PRIORITY table.
    *
    *
    */

   @Test
   public void test_AMLODF_12014() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      // lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "'Invalid'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-12014" );

   }


   /**
    * This test is to verify valid error code 12016:When
    * C_PROAL_OPEN_DEFERRED_FAULT.hours_interval_qt is present, hours_due_qt must be specified.
    *
    *
    */

   @Test
   public void test_AMLODF_12016() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      // lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-12016" );

   }


   /**
    * This test is to verify valid error code 12017:When C_PROAL_OPEN_DEFERRED_FAULT.hours_due_qt is
    * present, hours_interval_qt must be specified.
    *
    *
    */

   @Test
   public void test_AMLODF_12017() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      // lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-12017" );

   }


   /**
    * This test is to verify valid error code 12018:When
    * C_PROAL_OPEN_DEFERRED_FAULT.cycles_interval_qt is present, cycles_due_qt must be specified.
    *
    *
    */

   @Test
   public void test_AMLODF_12018() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      // lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-12018" );

   }


   /**
    * This test is to verify valid error code 12019:When C_PROAL_OPEN_DEFERRED_FAULT.cycles_due_qt
    * is present, cycles_interval_qt must be specified.
    *
    *
    */

   @Test
   public void test_AMLODF_12019() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      // lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-12019" );

   }


   /**
    * This test is to verify valid error code 12022:C_PROAL_OPEN_DEFERRED_FAULT.defer_ref_sdesc
    * value is only valid for a fault severity type of MEL
    *
    *
    */

   @Test
   public void test_AMLODF_12022() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      // lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_SEV_CD", "'AOG'" );
      // lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "'AUTO'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      // lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      // lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      // lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      // lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // setCurrentDate( iSN_ACFT );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-12022" );

   }


   /**
    * This test is to verify valid error code 12023:C_PROAL_OPEN_DEFERRED_FAULT.fail_defer_cd value
    * is only valid for a fault severity type of MEL.
    *
    *
    */

   @Test
   public void test_AMLODF_12023() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      // lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_SEV_CD", "'AOG'" );
      // lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "'AUTO'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      // lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      // lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      // lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      // lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      // lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // setCurrentDate( iSN_ACFT );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-12023" );

   }


   /**
    * This test is to verify valid error code 12024:C_PROAL_OPEN_DEFERRED_FAULT.defer_cd_sdesc value
    * is only valid a fault severity type of MEL.
    *
    *
    */

   @Test
   public void test_AMLODF_12024() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      // lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_SEV_CD", "'MINOR'" );
      // lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      // lFaults.put( "FAIL_DEFER_CD", "'AUTO'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      // lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      // lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      // lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      // lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      // lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );
      lFaults.put( "FOUND_ON_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-12024" );

   }


   /**
    * This test is to verify valid error code 12027:Fault has HOURS due/interval specified but BOM
    * Item does not have corresponding usage definition in Maintenix table MIM_PART_NUMDATA.
    *
    *
    */

   @Test
   public void test_AMLODF_12027() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      // lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "'SYS-10-3'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      // lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      // lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-12027" );

   }


   /**
    * This test is to verify valid error code 12028:Fault has CYCLES due/interval specified but BOM
    * Item does not have corresponding usage definition in Maintenix table MIM_PART_NUMDATA.
    *
    *
    */

   @Test
   public void test_AMLODF_12028() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      // lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "'SYS-10-3'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      // lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      // lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // setCurrentDate( iSN_ACFT );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-12028" );

   }


   /**
    * This test is to verify valid error code 12029:C_PROAL_OPEN_DEFERRED_FAULT Serial Number / Part
    * Number / Manufacturer is valid but is not an Aircraft or Assembly.
    *
    *
    */

   @Test
   public void test_AMLODF_12029() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      // lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      // lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      // lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );

      lFaults.put( "SERIAL_NO_OEM", "'SNAUTOTRK001'" );
      lFaults.put( "PART_NO_OEM", "'A0000020'" );
      lFaults.put( "MANUFACT_CD", "'10001'" );

      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-12029" );

   }


   /**
    * This test is to verify valid error code 12030:C_PROAL_OPEN_DEFERRED_FAULT Assembly / ATA
    * System Code not found in Maintenix EQP_ASSMBL_BOM table with a BOM_CLASS_CD of SYS..
    *
    *
    */

   @Test
   public void test_AMLODF_12030() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "'APU - ASSY'" );
      lFaults.put( "ATA_SYS_CD", "'ACFT-SYS-1-1-TRK-P1'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-12030" );

   }


   /**
    * This test is to verify valid error code 12031: C_PROC_ODF_PART_REQUIREMENT.pr_part_no_oem not
    * found in Maintenix table EQP_PART_NO table.
    *
    *
    */

   @Test
   public void test_AMLODF_12031() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      // lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "'Test'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // setCurrentDate( iSN_ACFT );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-12031" );

   }


   /**
    * This test is to verify valid error code 12032: C_PROC_ODF_PART_REQUIREMENT.pr_manufact_cd not
    * found in Maintenix database.
    *
    *
    */

   @Test
   public void test_AMLODF_12032() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      // lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "'Test'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // setCurrentDate( iSN_ACFT );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-12032" );

   }


   /**
    * This test is to verify valid error code 12033:C_PROC_ODF_PART_REQUIREMENT pr_part_no_oem /
    * pr_manufact_cd combination not found in Maintenix table EQP_PART_NO.
    *
    *
    */

   @Test
   public void test_AMLODF_12033() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      // lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // setCurrentDate( iSN_ACFT );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-12033" );

   }


   /**
    * This test is to verify valid error code 12034:The PR part number oem is found multiple times
    * in Maintenix. Providing a manufacturer code will resolve this.
    *
    *
    */

   @Test
   public void test_AMLODF_12034() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "'A0000002A'" );
      lFaults.put( "PN1_MANUFACT_CD", null );
      // lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "'ACFT-SYS-1-1-TRK-P2'" );
      lFaults.put( "PN1_POS_CD", "'1'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-12034" );

   }


   /**
    * This test is to verify valid error code 12036:C_PROC_ODF_PART_REQUIREMENT. Assembly / PR Part
    * Number / Pr Manufacture is valid but is not TRK, SER or BATCH.
    *
    *
    */

   @Test
   public void test_AMLODF_12036() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      // lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      // lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-12036" );

   }


   /**
    * This test is to verify valid error code 12037:C_PROC_ODF_PART_REQUIREMENT.PR_IPC_REF_CD not
    * found in Maintenix table EQP_BOM_PART.
    *
    *
    */

   @Test
   public void test_AMLODF_12037() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      // lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "'Test'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // setCurrentDate( iSN_ACFT );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-12037" );

   }


   /**
    * This test is to verify valid error code 12038:C_PROC_ODF_PART_REQUIREMENT: The PR
    * configuration slot code is not found in Maintenix under the fault inventory''s assembly.',
    * 'Affected by ALLOW_UNRELATED_PART_GROUP_IN_PART_REQUIREMENT.
    *
    *
    */

   @Test
   public void test_AMLODF_12038() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      // lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "'TRK-NO-PART'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // setCurrentDate( iSN_ACFT );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-12038" );

   }


   @Test
   public void test_AMLODF_12039() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "'T0000100'" );
      lFaults.put( "PN1_MANUFACT_CD", "'1234567890'" );
      lFaults.put( "PN1_IPC_REF_CD", "'ACFT-SYS-10-2-DOUBLETRK'" );

      // lFaults.put( "PN1_POS_CD", "'COMHW-POS-1'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-12039" );
   }


   /**
    * This test is to verify valid error code 12040:C_PROC_ODF_PART_REQUIREMENT.pr_pos_cd not found
    * in Maintenix table EQP_ASSMBL_POS table.
    *
    *
    */

   @Test
   public void test_AMLODF_12040() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      // lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "'Test'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // setCurrentDate( iSN_ACFT );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-12040" );

   }


   /**
    * This test is to verify valid error code 12041:C_PROC_ODF_PART_REQUIREMENT. The combination of
    * Assembly / pr_ipc_ref_cd / pr_position_cd not found in Maintenix table EQP_ASSMBL_POS.
    *
    *
    */

   @Test
   public void test_AMLODF_12041() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      // lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "'1.1.1.1'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // setCurrentDate( iSN_ACFT );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-12041" );

   }


   /**
    * This test is to verify valid error code 12044:C_PROC_ODF_PART_REQUIREMENT.pr_inst_qt can not
    * be negative.
    *
    *
    */

   @Test
   public void test_AMLODF_12044() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      // lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_INST_QT", "\'-10\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // setCurrentDate( iSN_ACFT );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-12044" );

   }


   /**
    * This test is to verify valid error code 12045:C_PROC_ODF_PART_REQUIREMENT.pr_rmvd_qt can not
    * be negative
    *
    *
    */

   @Test
   public void test_AMLODF_12045() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      // lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'-10\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // setCurrentDate( iSN_ACFT );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-12045" );

   }


   /**
    * This test is to verify valid error code 12046:C_PROC_ODF_PART_REQUIREMENT.pr_inst qt must be 1
    * if PR part has inv_class_cd of TRK.
    *
    *
    */

   @Test
   public void test_AMLODF_12046() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      // lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_INST_QT", "\'10\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );
      lFaults.put( "FOUND_ON_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // setCurrentDate( iSN_ACFT );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-12046" );

   }


   /**
    * This test is to verify valid error code 12047:C_PROC_ODF_PART_REQUIREMENT.Pr_rmvd qt must be 1
    * if PR part has inv_class_cd of TRK.
    *
    *
    */

   @Test
   public void test_AMLODF_12047() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      // lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'10\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );
      lFaults.put( "FOUND_ON_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-12047" );

   }


   /**
    * This test is to verify valid error code 12048:C_PROAL_OPEN_DEFERRED_FAULT.fail_sev_cd and
    * fail_def_cd mapping must exist in Maintenix table REF_FAIL_SEV_DEFER table
    *
    *
    */

   @Test
   public void test_AMLODF_12048() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      // lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_SEV_CD", "'AOG'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-12048" );

   }


   /**
    * This test is to verify valid error code 12049:C_PROAL_OPEN_DEFERRED_FAULT.fault_log_type_cd
    * must exist in Maintenix table REF_FAULT_LOG_TYPE.
    *
    *
    */

   @Test
   public void test_AMLODF_12049() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      // lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "'Invalid'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // setCurrentDate( iSN_ACFT );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-12049" );

   }


   /**
    * This test is to verify valid error code 12060:C_PROAL_OPEN_DEFERRED_FAULT.FAULT_LDESC exceeds
    * the maximum allowed length after appending extra information to fault description.
    *
    *
    */
   @Ignore
   @Test
   public void test_AMLODF_12060() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      String lLdesc = "";

      for ( int i = 0; i < 4000; i++ ) {
         lLdesc = lLdesc + "q";

      }

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      // lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + lLdesc + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );
      lFaults.put( "FOUND_ON_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-12060" );

   }


   /**
    *
    * Test AMLODF-12065: C_PROAL_OPEN_DEFERRED_FAULT.FAIL_DEFER_CD cannot be null when
    * REF_FAIL_SEV.SEV_TYPE_CD for C_PROAL_OPEN_DEFERRED_FAULT.FAIL_SEV_CD is MEL
    *
    */

   @Test
   public void test_AMLODF_12065() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      // lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", null );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // setCurrentDate( iSN_ACFT );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-12065" );

   }


   /**
    *
    * Test AMLODF-12066: defer_cd_sdesc cannot be null for fault severity type of MEL.
    *
    */

   @Test
   public void test_AMLODF_12066() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      // lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", null );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // setCurrentDate( iSN_ACFT );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-12066" );

   }


   /**
    * This test is to verify valid error code 12100:The PR part number cannot be NULL.', null,
    * 'CRITICAL', 'AL_PROC_ODF_PART_REQUIREMENT.
    *
    *
    */

   @Test
   public void test_AMLODF_12100() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      // lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", null );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-12100" );

   }


   /**
    *
    * Test AMLODF-12110: The installed quantity precision does not match the part'' baseline
    * precision..
    *
    */

   @Test
   public void test_AMLODF_12110() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "'A0000009'" );
      lFaults.put( "PN1_MANUFACT_CD", "'10001'" );
      lFaults.put( "PN1_IPC_REF_CD", "'ACFT-SYS-1-1-BATCH'" );
      lFaults.put( "PN1_POS_CD", "'1'" );
      lFaults.put( "PN1_INST_QT", "'1.234'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // setCurrentDate( iSN_ACFT );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-12110" );

   }


   /**
    *
    * Test AMLODF-12111: The removed quantity precision does not match the part'' baseline
    * precision.
    */

   @Test
   public void test_AMLODF_12111() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "'A0000009'" );
      lFaults.put( "PN1_MANUFACT_CD", "'10001'" );
      lFaults.put( "PN1_IPC_REF_CD", "'ACFT-SYS-1-1-BATCH'" );
      lFaults.put( "PN1_POS_CD", "'1'" );
      lFaults.put( "PN1_INST_QT", "'1'" );
      lFaults.put( "PN1_RMVD_QT", "'1.234'" );
      lFaults.put( "DUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // setCurrentDate( iSN_ACFT );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-12111" );

   }


   /**
    * This test is to verify valid error code 12120:Assembly and tracked part requirements for the
    * same configuration slot and position cannot exist one fault.
    *
    *
    */

   @Test
   public void test_AMLODF_12120() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );

      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );

      lFaults.put( "PN2_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN2_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN2_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN2_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN2_INST_QT", "\'1\'" );
      lFaults.put( "PN2_RMVD_QT", "\'1\'" );

      lFaults.put( "DUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-12120" );

   }


   /**
    * This test is to verify valid error code 12200:Could not find a unique configuration slot.
    * Please provide configuration slot code.
    *
    *
    */

   @Test
   public void test_AMLODF_12200() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      // lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      // lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      // lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "'A0000015'" );
      lFaults.put( "PN1_MANUFACT_CD", "'ABC11'" );
      lFaults.put( "PN1_IPC_REF_CD", null );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // setCurrentDate( iSN_ACFT );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-12200" );

   }


   /**
    * This test is to verify valid error code 12201:Could not find a unique position. Please provide
    * position code.
    *
    *
    */

   @Test
   public void test_AMLODF_12201() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      // lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      // lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      // lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "'A0000003'" );
      lFaults.put( "PN1_MANUFACT_CD", "'ABC11'" );
      lFaults.put( "PN1_IPC_REF_CD", "'ACFT-SYS-1-1-TRK-P3'" );
      // lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_POS_CD", null );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-12201" );

   }


   /**
    * This test is to verify valid error code 12203:Configuration slot and part number do not have
    * matching inventory classes.
    *
    *
    */

   @Test
   public void test_AMLODF_12203() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // AL_OPEN_DEFERRED_FAULT table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      // lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      // lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      // lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "'APU_ASSY_PN1'" );
      lFaults.put( "PN1_MANUFACT_CD", "'1234567890'" );
      lFaults.put( "PN1_IPC_REF_CD", "'ACFT-SYS-1-1-TRK-BATCH-PARENT'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-12203" );

   }


   /**
    * This test is to verify valid error code 12204:AMLODF-12204', 'Part number is not found in
    * configuration slot.
    *
    *
    */

   @Test
   public void test_AMLODF_12204() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // AL_OPEN_DEFERRED_FAULT table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      // lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      // lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      // lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "'A0000010'" );
      lFaults.put( "PN1_MANUFACT_CD", "'11111'" );
      lFaults.put( "PN1_IPC_REF_CD", "'ACFT-SYS-1-1-TRK-P2'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-12204" );

   }


   /**
    * This test is to verify AL_OPEN_DEFERRED_FAULT_IMPORT validation functionality of staging table
    * AL_OPEN_DEFERRED_FAULT_IMPORT. BATCH part on ACFT.
    *
    *
    */
   @Test
   public void test_AMLODF_12300() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "'T0000003'" );
      lFaults.put( "PN1_MANUFACT_CD", "'ABC11'" );
      lFaults.put( "PN1_IPC_REF_CD", "'T0000003-ABC11'" );
      lFaults.put( "PN1_POS_CD", "'TSE-POS-1'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-12300" );

   }


   /**
    * This test is to verify valid error code 17002:C_PROAL_OPEN_DEFERRED_FAULT record is invalid
    * because related C_PROC_ODF_PART_REQUIREMENT record is invalid.
    *
    *
    */

   @Test
   public void test_AMLODF_17002() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      // lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "'TRK-NO-PART'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-17002" );

   }


   /**
    * This test is to verify valid error code ???:
    *
    *
    */

   @Test
   public void test_AMLODF_OPER_25814_AOG() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "'AOG'" );
      lFaults.put( "FAIL_DEFER_CD", "'AUTO'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-12070" );

   }


   /**
    * This test is to verify valid error code 10001:C_PROAL_OPEN_DEFERRED_FAULT.serial_no_oem cannot
    * be NULL or consist entirely of spaces.
    *
    *
    */

   @Test
   public void test_AMLODF_OPER_25814_UNKNOWN() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "'UNKNOWN'" );
      lFaults.put( "FAIL_DEFER_CD", "'AUTO'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // setCurrentDate( iSN_ACFT );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-12070" );

   }


   @Test
   public void testOPER_24353_AMLODF_11012_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "'invlid'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      // lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );
      lFaults.put( "FOUND_ON_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-11012" );

   }


   /**
    * This test is to verify valid error code 20000:C_PROAL_OPEN_DEFERRED_FAULT.logbook_reference
    * already exists in Maintenix as EVT_EVENT.doc_ref_sdesc.
    *
    *
    */

   @Test
   public void test_AMLODF_20000() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      // lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "'log2'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );
      lFaults.put( "FOUND_ON_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // setCurrentDate( iSN_ACFT );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-20000" );

   }


   /**
    * This test is to verify valid error code 11011:C_PROC_OPEN_DEFERRED_FAULT.For duplicate part
    * number/ serial number combination exist in Maintenix, please provide manufacturer code
    *
    *
    */

   @Test
   public void test_AMLODF_11011() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "'SN000001'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      // lFaults.put( "MANUFACT_CD", null );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-11011" );

   }


   /**
    * This test is to verify valid error code 12206:Fault severity FAIL_SEV_CD exists more than once
    * in Maintenix REF_FAIL_SEV table.
    *
    *
    */
   @Test
   public void test_AMLODF_12206() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "'AUTOTEST'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );
      lFaults.put( "FOUND_ON_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-12206" );

   }


   /**
    * This test is to verify valid error code 12207:FAIL_DEFER_CD exists more than once in Maintenix
    * REF_FAIL_DEFER table.
    *
    *
    */
   @Test
   public void test_AMLODF_12207() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT + "\'" );
      lFaults.put( "PART_NO_OEM", "\'" + iPN_OEM_ACFT + "\'" );
      lFaults.put( "MANUFACT_CD", "\'" + iMANFACT_CD_ACFT + "\'" );
      lFaults.put( "ATA_SYS_CD", "\'" + iSYS_CD_ACFT + "\'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      // lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "'DFRAUTO'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "PN1_PART_NO_OEM", "\'" + iPN1_PART_NO_OEM + "\'" );
      lFaults.put( "PN1_MANUFACT_CD", "\'" + iPN1_MANUFACT_CD + "\'" );
      lFaults.put( "PN1_IPC_REF_CD", "\'" + iPN1_IPC_REF_CD + "\'" );
      lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "DUE_DT", "SYSDATE" );
      lFaults.put( "FOUND_ON_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkODFErrorCode( testName.getMethodName(), "AMLODF-12207" );

   }


   // ============================================================================================================
   /**
    * This function is to verify after CSV imported or import store procedure has been executed.
    *
    *
    */
   public void verifyImport() {
      // Verify EVT_EVENT table
      iCFIDs1 = verifyEVTEVENT( iEventTypeCdFault, iFAULT_SDESC, "CFDEFER", iFAULT_LDESC,
            iLOGBOOK_REFERENCE );

      iTSIDs1 = verifyEVTEVENT( iEventTypeCdTask, iFAULT_SDESC, "ACTV", iFAULT_LDESC, null );

      iCFIDs2 = verifyEVTEVENT( iEventTypeCdFault, iFAULT_SDESC2, "CFDEFER", iFAULT_LDESC2,
            iLOGBOOK_REFERENCE2 );

      iTSIDs2 = verifyEVTEVENT( iEventTypeCdTask, iFAULT_SDESC2, "ACTV", iFAULT_LDESC2, null );

      // Verify EVT_INV table
      invIDs lINVIDSFAULT = verifyEVTINV( iCFIDs1, iAssmbl_ACFT_CD, "2", "1" );
      invIDs lINVIDSTASK = verifyEVTINV( iTSIDs1, iAssmbl_ACFT_CD, "2", "1" );

      Assert.assertTrue( "Inventory Ids should be equal: ", lINVIDSFAULT.equals( lINVIDSTASK ) );

      invIDs lINVIDSFAULT2 = verifyEVTINV( iCFIDs2, iAssmbl_ACFT_CD, "2", "1" );
      invIDs lINVIDSTASK2 = verifyEVTINV( iTSIDs2, iAssmbl_ACFT_CD, "2", "1" );

      Assert.assertTrue( "Inventory Ids should be equal: ", lINVIDSFAULT2.equals( lINVIDSTASK2 ) );

      // Verify the INV ids exists in INV_INV table
      String lQuery = "select 1 from " + TableUtil.INV_INV + " where INV_NO_DB_ID="
            + lINVIDSTASK.getINVIDs().getNO_DB_ID() + " and INV_NO_ID="
            + lINVIDSTASK.getINVIDs().getNO_ID() + " and ASSMBL_DB_ID="
            + lINVIDSTASK.getASSMBLINVIDs().getNO_DB_ID() + " and ASSMBL_INV_NO_ID="
            + lINVIDSTASK.getASSMBLINVIDs().getNO_ID();
      Assert.assertTrue( "Check inventory table to verify the record exists",
            RecordsExist( lQuery ) );

      String lQuery2 = "select 1 from " + TableUtil.INV_INV + " where INV_NO_DB_ID="
            + lINVIDSTASK2.getINVIDs().getNO_DB_ID() + " and INV_NO_ID="
            + lINVIDSTASK2.getINVIDs().getNO_ID() + " and ASSMBL_DB_ID="
            + lINVIDSTASK2.getASSMBLINVIDs().getNO_DB_ID() + " and ASSMBL_INV_NO_ID="
            + lINVIDSTASK2.getASSMBLINVIDs().getNO_ID();
      Assert.assertTrue( "Check inventory table to verify the record exists",
            RecordsExist( lQuery2 ) );

      // Verify EVT_EVENT_REL
      verifyEVTEVENT_REL( iCFIDs1, iTSIDs1, iREL_TYPE_CDFAULT );
      verifyEVTEVENT_REL( iTSIDs1, iTSIDs1, iREL_TYPE_CDTASK );

      verifyEVTEVENT_REL( iCFIDs2, iTSIDs2, iREL_TYPE_CDFAULT );
      verifyEVTEVENT_REL( iTSIDs2, iTSIDs2, iREL_TYPE_CDTASK );

      // Verify EVT_STAGE table
      ArrayList<String> ltasklist = new ArrayList<String>();
      ltasklist.add( "UNASSIGNED FROM" );
      ltasklist.add( "NEW HOURS(HOUR) DEADLINE" );
      ltasklist.add( "NEW CDY(DAY) DEADLINE" );
      ltasklist.add( "THE TASK WAS CREATED" );
      ltasklist.add( "NEW CYCLES(CYCLES) DEADLINE" );
      ltasklist.add( "THE TASK HAS BEEN DEFERRED" );

      verifyEVTSTAGE( iTSIDs1, ltasklist );
      verifyEVTSTAGE( iTSIDs2, ltasklist );

      ArrayList<String> lfaultlist = new ArrayList<String>();
      lfaultlist.add( "CORRECTIVE ACTION WAS DEFERRED" );
      lfaultlist.add( "CORRECTIVE ACTION WAS UNASSIGNED" );
      lfaultlist.add( "FAULT WAS CREATED" );

      verifyEVTSTAGE( iCFIDs1, lfaultlist );
      verifyEVTSTAGE( iCFIDs2, lfaultlist );

      // Verify EVT_SCHED_DEAD
      // get Ids of HOUR/CYCLES/DAY in mim_data_type table
      simpleIDs lHoursIds = getMIMType( "HOURS" );
      simpleIDs lCycIds = getMIMType( "CYCLES" );
      simpleIDs lDayIds = getMIMType( "CDY" );
      verifyEVTSCHEDDEAD( iTSIDs1, lHoursIds );
      verifyEVTSCHEDDEAD( iTSIDs1, lCycIds );
      verifyEVTSCHEDDEAD( iTSIDs1, lDayIds );

      verifyEVTSCHEDDEAD( iTSIDs2, lHoursIds );
      verifyEVTSCHEDDEAD( iTSIDs2, lCycIds );
      verifyEVTSCHEDDEAD( iTSIDs2, lDayIds );

      // Verify SCHED_STASK table
      simpleIDs lPriorityIds = getPriorityIDs( iTASK_PRIORITY_CD );
      simpleIDs lTaskCDs = getTaskCDs( "CORR" );
      verifySCHEDSTASK( iTSIDs1, iCFIDs1, lPriorityIds, lTaskCDs, lINVIDSTASK.getINVIDs() );
      verifySCHEDSTASK( iTSIDs2, iCFIDs2, lPriorityIds, lTaskCDs, lINVIDSTASK2.getINVIDs() );

      // Verify SD_FAULT
      verifySDFAULT( iCFIDs1, iFAIL_DEFER_CD, iFAULT_SOURCE_CD, iFAIL_SEV_CD, iDEFER_REF_SDESC,
            iOP_RESTRICTION_LDESC, iDEFER_CD_SDESC, iFAULT_LOG_TYPE_CD, iFRM_CD );

      verifySDFAULT( iCFIDs2, iFAIL_DEFER_CD, iFAULT_SOURCE_CD, iFAIL_SEV_CD, iDEFER_REF_SDESC,
            iOP_RESTRICTION_LDESC, iDEFER_CD_SDESC, iFAULT_LOG_TYPE_CD, iFRM_CD );

      // Verify sched_labour table
      String iLabor = getParmValue();
      if ( StringUtils.isBlank( iLabor ) ) {
         iLabourID1 = verifySCHEDLABOUR( iTSIDs1, iACTV, iLBR );
         iLabourID2 = verifySCHEDLABOUR( iTSIDs2, iACTV, iLBR );
      } else if ( !RecordsExist(
            "SELECT 1 FROM  ref_labour_skill WHERE labour_skill_cd='" + iLabor + "'" ) ) {
         iLabourID1 = verifySCHEDLABOUR( iTSIDs1, iACTV, iLBR );
         iLabourID2 = verifySCHEDLABOUR( iTSIDs2, iACTV, iLBR );
      } else {
         iLabourID1 = verifySCHEDLABOUR( iTSIDs1, iACTV, iLabor );
         iLabourID2 = verifySCHEDLABOUR( iTSIDs2, iACTV, iLabor );
      }

      // Verify SCHED_LABOUR_ROLE table
      iLabourRoleID1 = verifySCHEDLABOURROLE( iLabourID1, iTECH );
      iLabourRoleID2 = verifySCHEDLABOURROLE( iLabourID2, iTECH );

      // Verify SCHED_LABOUR_ROLE_STATUS table
      verifySCHEDLABOURROLESTATUS( iLabourRoleID1, iACTV );
      verifySCHEDLABOURROLESTATUS( iLabourRoleID2, iACTV );

      // Verify SCHED_PART
      asmInfor linfors = getASMINFor( iPN1_PART_NO_OEM, iPN1_MANUFACT_CD );
      String lschedID1 = verifySCHEDPART_MultipleParts( iTSIDs1, linfors, iACTV, "1", "REQ" );
      String lschedID2 = verifySCHEDPART_MultipleParts( iTSIDs2, linfors, iACTV, "1", "REQ" );

      // Verify SCHED_INST_PART
      verifySCHEDINSTPART_MultipleParts( iTSIDs1, lschedID1, "1", 1 );
      verifySCHEDINSTPART_MultipleParts( iTSIDs2, lschedID2, "1", 1 );

      // Verify SCHED_RMVD_PART
      invInfor lInvInfor1 = getINVINFor( iPN1_PART_NO_OEM, iPN1_MANUFACT_CD, iSN_ACFT );
      invInfor lInvInfor2 = getINVINFor( iPN1_PART_NO_OEM, iPN1_MANUFACT_CD, iSN_ACFT2 );
      verifySCHEDRMVDPART( iTSIDs1, linfors, "IMSCHD", "1", 1, lInvInfor1, true );
      verifySCHEDRMVDPART( iTSIDs2, linfors, "IMSCHD", "1", 1, lInvInfor2, true );

   }


   /**
    * This function is to check error(s) for validation table is loaded
    *
    *
    */
   public void verifyValidation() {

      int lCount = countRowsOfQuery( TestConstants.ODF_ERROR_CHECK_TABLE );
      Assert.assertTrue( "There should be no error. ", lCount == 0 );

      lCount = countRowsOfQuery( TestConstants.ODF_ERROR_CHECK_MSG );
      Assert.assertTrue( "There should be no error. ", lCount == 0 );

   }


   /**
    * This function is to verify AL_OPEN_DEFERRED_FAULT staging table is loaded
    *
    *
    */

   public void verifyStagingTable() {

      String lQuery = "select 1 from " + TableUtil.AL_OPEN_DEFERRED_FAULT + " where SERIAL_NO_OEM='"
            + iSN_ACFT + "' and LOGBOOK_REFERENCE='" + iLOGBOOK_REFERENCE + "'";
      Assert.assertTrue( "Check AL_OPEN_DEFERRED_FAULT table to verify the record exists",
            RecordsExist( lQuery ) );

      String lQuery2 =
            "select 1 from " + TableUtil.AL_OPEN_DEFERRED_FAULT + " where SERIAL_NO_OEM='"
                  + iSN_ACFT2 + "' and LOGBOOK_REFERENCE='" + iLOGBOOK_REFERENCE2 + "'";
      Assert.assertTrue( "Check AL_OPEN_DEFERRED_FAULT table to verify the record exists",
            RecordsExist( lQuery2 ) );

      int lCount = countRowsInEntireTable( TableUtil.AL_OPEN_DEFERRED_FAULT );
      Assert.assertTrue( "There should be 2 records in AL_OPEN_DEFERRED_FAULT table. ",
            lCount == 2 );

   }


   /**
    * This function is to verify SCHED_RMVD_PART table details part
    *
    *
    */
   public void verifySCHEDRMVDPART( simpleIDs aIDs, String aInfor, String aRMReasonCD,
         String aRMVD_QT, int aNumRecord, invInfor ainvInfor, boolean aCheck ) {

      // SCHED_RMVD_PART table
      String[] iIds =
            { "REMOVE_REASON_CD", "RMVD_QT", "INV_NO_DB_ID", "INV_NO_ID", "SERIAL_NO_OEM" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "SCHED_DB_ID", aIDs.getNO_DB_ID() );
      lArgs.addArguments( "SCHED_ID", aIDs.getNO_ID() );
      lArgs.addArguments( "SCHED_PART_ID", aInfor );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.SCHED_RMVD_PART, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Check # of records is correct
      Assert.assertTrue( "Check number of records display: ", llists.size() == aNumRecord );

      // Check each record
      for ( int i = 0; i < llists.size(); i++ ) {

         Assert.assertTrue( "REMOVE_REASON_CD",
               llists.get( 0 ).get( 0 ).equalsIgnoreCase( aRMReasonCD ) );
         Assert.assertTrue( "RMVD_QT", llists.get( 0 ).get( 1 ).equalsIgnoreCase( aRMVD_QT ) );
         if ( aCheck ) {
            Assert.assertTrue( "INV_NO_DB_ID", llists.get( 0 ).get( 2 )
                  .equalsIgnoreCase( ainvInfor.getiINVIDs().getNO_DB_ID() ) );
            Assert.assertTrue( "INV_NO_ID",
                  llists.get( 0 ).get( 3 ).equalsIgnoreCase( ainvInfor.getiINVIDs().getNO_ID() ) );
            Assert.assertTrue( "SERIAL_NO_OEM",
                  llists.get( 0 ).get( 4 ).equalsIgnoreCase( ainvInfor.getiSerial_no_oem() ) );
         }
      }

   }


   /**
    * This function is to verify SCHED_RMVD_PART table details part
    *
    *
    */
   public void verifySCHEDRMVDPART( simpleIDs aIDs, asmInfor aInfor, String aRMReasonCD,
         String aRMVD_QT, int aNumRecord, invInfor ainvInfor, boolean aCheck ) {

      // SCHED_RMVD_PART table
      String[] iIds =
            { "REMOVE_REASON_CD", "RMVD_QT", "INV_NO_DB_ID", "INV_NO_ID", "SERIAL_NO_OEM" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "SCHED_DB_ID", aIDs.getNO_DB_ID() );
      lArgs.addArguments( "SCHED_ID", aIDs.getNO_ID() );
      lArgs.addArguments( "PART_NO_DB_ID", aInfor.getlPNIDs().getNO_DB_ID() );
      lArgs.addArguments( "PART_NO_ID", aInfor.getlPNIDs().getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.SCHED_RMVD_PART, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Check # of records is correct
      Assert.assertTrue( "Check number of records display: ", llists.size() == aNumRecord );

      // Check each record
      for ( int i = 0; i < llists.size(); i++ ) {

         Assert.assertTrue( "REMOVE_REASON_CD",
               llists.get( 0 ).get( 0 ).equalsIgnoreCase( aRMReasonCD ) );
         Assert.assertTrue( "RMVD_QT", llists.get( 0 ).get( 1 ).equalsIgnoreCase( aRMVD_QT ) );
         if ( aCheck ) {
            Assert.assertTrue( "INV_NO_DB_ID", llists.get( 0 ).get( 2 )
                  .equalsIgnoreCase( ainvInfor.getiINVIDs().getNO_DB_ID() ) );
            Assert.assertTrue( "INV_NO_ID",
                  llists.get( 0 ).get( 3 ).equalsIgnoreCase( ainvInfor.getiINVIDs().getNO_ID() ) );
            Assert.assertTrue( "SERIAL_NO_OEM",
                  llists.get( 0 ).get( 4 ).equalsIgnoreCase( ainvInfor.getiSerial_no_oem() ) );
         }
      }

   }


   /**
    * This function is to verify SCHED_INST_PART table details part
    *
    *
    */
   public void verifySCHEDINSTPART( simpleIDs aIDs, asmInfor aInfor, String aINSTQT,
         int aNumRecord ) {

      // SCHED_INST_PART
      String[] iIds = { "PART_NO_DB_ID", "PART_NO_ID", "INST_QT" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "SCHED_DB_ID", aIDs.getNO_DB_ID() );
      lArgs.addArguments( "SCHED_ID", aIDs.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.SCHED_INST_PART, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Check # of records is correct
      Assert.assertTrue( "Check number of records display: ", llists.size() == aNumRecord );

      // Check each record
      for ( int i = 0; i < llists.size(); i++ ) {
         // Assert.assertTrue( "PART_NO_DB_ID",
         // llists.get( i ).get( 0 ).equalsIgnoreCase( aInfor.getlPNIDs().getNO_DB_ID() ) );
         // Assert.assertTrue( "PART_NO_ID",
         // llists.get( i ).get( 1 ).equalsIgnoreCase( aInfor.getlPNIDs().getNO_ID() ) );
         Assert.assertTrue( "INST_QT", llists.get( i ).get( 2 ).equalsIgnoreCase( aINSTQT ) );
      }

   }


   /**
    * This function is to verify SCHED_INST_PART table
    *
    *
    */
   public void verifySCHEDINSTPART_MultipleParts( simpleIDs aIDs, String aInfor, String aINSTQT,
         int aNumRecord ) {

      // SCHED_INST_PART
      String[] iIds = { "INST_QT" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "SCHED_DB_ID", aIDs.getNO_DB_ID() );
      lArgs.addArguments( "SCHED_ID", aIDs.getNO_ID() );
      lArgs.addArguments( "SCHED_PART_ID", aInfor );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.SCHED_INST_PART, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Check # of records is correct
      Assert.assertTrue( "Check number of records display: ", llists.size() == aNumRecord );

      // Check each record
      for ( int i = 0; i < llists.size(); i++ ) {
         Assert.assertTrue( "INST_QT", llists.get( i ).get( 0 ).equalsIgnoreCase( aINSTQT ) );
      }

   }


   /**
    * This function is to verify SCHED_PART table.
    *
    *
    */
   public void verifySCHEDPART( simpleIDs aIDs, asmInfor aInfor, String aSTATUSCD, String aSCHEDQT,
         String aREQACTCD, boolean aCheck ) {

      // SCHED_PART
      String[] iIds = { "SCHED_BOM_PART_DB_ID", "SCHED_BOM_PART_ID", "SCHED_PART_STATUS_CD",
            "SPEC_PART_NO_DB_ID", "SPEC_PART_NO_ID", "NH_ASSMBL_DB_ID", "NH_ASSMBL_CD",
            "NH_ASSMBL_BOM_ID", "NH_ASSMBL_POS_ID", "ASSMBL_DB_ID", "ASSMBL_CD", "ASSMBL_BOM_ID",
            "ASSMBL_POS_ID", "SCHED_QT", "REQ_ACTION_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "SCHED_DB_ID", aIDs.getNO_DB_ID() );
      lArgs.addArguments( "SCHED_ID", aIDs.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.SCHED_PART, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "SCHED_BOM_PART_DB_ID",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aInfor.getlPGIDs().getNO_DB_ID() ) );
      Assert.assertTrue( "SCHED_BOM_PART_ID",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( aInfor.getlPGIDs().getNO_ID() ) );
      // Assert.assertTrue( "SCHED_PART_STATUS_CD",
      // llists.get( 0 ).get( 2 ).equalsIgnoreCase( aSTATUSCD ) );
      // Assert.assertTrue( "SPEC_PART_NO_DB_ID",
      // llists.get( 0 ).get( 3 ).equalsIgnoreCase( aInfor.getlPNIDs().getNO_DB_ID() ) );
      // Assert.assertTrue( "SPEC_PART_NO_ID",
      // llists.get( 0 ).get( 4 ).equalsIgnoreCase( aInfor.getlPNIDs().getNO_ID() ) );
      Assert.assertTrue( "NH_ASSMBL_DB_ID",
            llists.get( 0 ).get( 5 ).equalsIgnoreCase( aInfor.getlNHASMBL().getASSMBL_DB_ID() ) );
      Assert.assertTrue( "NH_ASSMBL_CD",
            llists.get( 0 ).get( 6 ).equalsIgnoreCase( aInfor.getlNHASMBL().getASSMBL_CD() ) );
      // Assert.assertTrue( "NH_ASSMBL_BOM_ID",
      // llists.get( 0 ).get( 7 ).equalsIgnoreCase( aInfor.getlNHASMBL().getASSMBL_BOM_ID() ) );
      // Assert.assertTrue( "NH_ASSMBL_BOM_POS_ID",
      // llists.get( 0 ).get( 8 ).equalsIgnoreCase( aInfor.getlNHASMBL().getASSMBL_POS_ID() ) );
      if ( aCheck ) {
         Assert.assertTrue( "ASSMBL_DB_ID",
               llists.get( 0 ).get( 9 ).equalsIgnoreCase( aInfor.getlASMBL().getASSMBL_DB_ID() ) );
         Assert.assertTrue( "ASSMBL_CD",
               llists.get( 0 ).get( 10 ).equalsIgnoreCase( aInfor.getlASMBL().getASSMBL_CD() ) );
         Assert.assertTrue( "ASSMBL_BOM_ID", llists.get( 0 ).get( 11 )
               .equalsIgnoreCase( aInfor.getlASMBL().getASSMBL_BOM_ID() ) );
         Assert.assertTrue( "ASSMBL_POS_ID", llists.get( 0 ).get( 12 )
               .equalsIgnoreCase( aInfor.getlASMBL().getASSMBL_POS_ID() ) );
      }
      Assert.assertTrue( "SCHED_QT", llists.get( 0 ).get( 13 ).equalsIgnoreCase( aSCHEDQT ) );
      Assert.assertTrue( "REQ_ACTION_CD", llists.get( 0 ).get( 14 ).equalsIgnoreCase( aREQACTCD ) );

   }


   /**
    * This function is to verify SCHED_PART table.
    *
    *
    */
   public String verifySCHEDPART_MultipleParts( simpleIDs aIDs, asmInfor aInfor, String aSTATUSCD,
         String aSCHEDQT, String aREQACTCD ) {

      // SCHED_PART
      String[] iIds = { "SCHED_PART_STATUS_CD", "SPEC_PART_NO_DB_ID", "SPEC_PART_NO_ID",
            "NH_ASSMBL_DB_ID", "NH_ASSMBL_CD", "NH_ASSMBL_BOM_ID", "NH_ASSMBL_POS_ID", "SCHED_QT",
            "REQ_ACTION_CD", "SCHED_PART_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "SCHED_DB_ID", aIDs.getNO_DB_ID() );
      lArgs.addArguments( "SCHED_ID", aIDs.getNO_ID() );
      lArgs.addArguments( "SCHED_BOM_PART_DB_ID", aInfor.getlPGIDs().getNO_DB_ID() );
      lArgs.addArguments( "SCHED_BOM_PART_ID", aInfor.getlPGIDs().getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.SCHED_PART, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "SCHED_PART_STATUS_CD",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aSTATUSCD ) );
      // Assert.assertTrue( "SPEC_PART_NO_DB_ID",
      // llists.get( 0 ).get( 1 ).equalsIgnoreCase( aInfor.getlPNIDs().getNO_DB_ID() ) );
      // Assert.assertTrue( "SPEC_PART_NO_ID",
      // llists.get( 0 ).get( 2 ).equalsIgnoreCase( aInfor.getlPNIDs().getNO_ID() ) );
      Assert.assertTrue( "NH_ASSMBL_DB_ID",
            llists.get( 0 ).get( 3 ).equalsIgnoreCase( aInfor.getlNHASMBL().getASSMBL_DB_ID() ) );
      Assert.assertTrue( "NH_ASSMBL_CD",
            llists.get( 0 ).get( 4 ).equalsIgnoreCase( aInfor.getlNHASMBL().getASSMBL_CD() ) );
      // Assert.assertTrue( "NH_ASSMBL_BOM_ID",
      // llists.get( 0 ).get( 5 ).equalsIgnoreCase( aInfor.getlNHASMBL().getASSMBL_BOM_ID() ) );
      // Assert.assertTrue( "NH_ASSMBL_BOM_POS_ID",
      // llists.get( 0 ).get( 6 ).equalsIgnoreCase( aInfor.getlNHASMBL().getASSMBL_POS_ID() ) );
      Assert.assertTrue( "SCHED_QT", llists.get( 0 ).get( 7 ).equalsIgnoreCase( aSCHEDQT ) );
      Assert.assertTrue( "REQ_ACTION_CD", llists.get( 0 ).get( 8 ).equalsIgnoreCase( aREQACTCD ) );

      String lschedId = llists.get( 0 ).get( 9 );

      return lschedId;

   }


   /**
    * This function is to retrieve PART's assemble information.
    *
    *
    */
   public asmInfor getASMINFor( String aPART_NO_OEM, String aMANUFACT_CD ) {

      asmInfor lassm = null;

      String lQuery =
            "select eqp_part_no.part_no_db_id, eqp_part_no.part_no_id,eqp_assmbl_bom.nh_assmbl_db_id,eqp_assmbl_bom.nh_assmbl_cd, eqp_assmbl_bom.nh_assmbl_bom_id,eqp_assmbl_pos.nh_assmbl_pos_id,"
                  + " eqp_assmbl_bom.assmbl_db_id, eqp_assmbl_bom.assmbl_cd, eqp_assmbl_bom.assmbl_bom_id, eqp_assmbl_pos.assmbl_pos_id, eqp_part_baseline.bom_part_db_id, eqp_part_baseline.bom_part_id from eqp_part_no"
                  + " inner join eqp_part_baseline on eqp_part_baseline.part_no_db_id=eqp_part_no.part_no_db_id and eqp_part_baseline.part_no_id=eqp_part_no.part_no_id"
                  + " inner join eqp_bom_part on eqp_bom_part.bom_part_db_id=eqp_part_baseline.bom_part_db_id and eqp_bom_part.bom_part_id=eqp_part_baseline.bom_part_id"
                  + " inner join eqp_assmbl_pos on eqp_assmbl_pos.assmbl_db_id=eqp_bom_part.bom_part_db_id and eqp_assmbl_pos.assmbl_cd=eqp_bom_part.assmbl_cd and eqp_assmbl_pos.assmbl_bom_id=eqp_bom_part.assmbl_bom_id"
                  + " inner join eqp_assmbl_bom on eqp_assmbl_bom.assmbl_db_id=eqp_bom_part.assmbl_db_id and eqp_assmbl_bom.assmbl_cd=eqp_bom_part.assmbl_cd and eqp_assmbl_bom.assmbl_bom_id=eqp_bom_part.assmbl_bom_id"
                  + " where eqp_part_no.PART_NO_OEM='" + aPART_NO_OEM
                  + "' and eqp_part_no.MANUFACT_CD='" + aMANUFACT_CD + "'";

      ResultSet assbomResultSetRecords;
      try {
         assbomResultSetRecords = runQuery( lQuery );
         assbomResultSetRecords.next();
         lassm = new asmInfor(
               new simpleIDs( assbomResultSetRecords.getString( "part_no_db_id" ),
                     assbomResultSetRecords.getString( "part_no_id" ) ),
               new assmbleInfor( assbomResultSetRecords.getString( "nh_assmbl_db_id" ),
                     assbomResultSetRecords.getString( "nh_assmbl_cd" ),
                     assbomResultSetRecords.getString( "nh_assmbl_bom_id" ),
                     assbomResultSetRecords.getString( "nh_assmbl_pos_id" ) ),
               new assmbleInfor( assbomResultSetRecords.getString( "assmbl_db_id" ),
                     assbomResultSetRecords.getString( "assmbl_cd" ),
                     assbomResultSetRecords.getString( "assmbl_bom_id" ),
                     assbomResultSetRecords.getString( "assmbl_pos_id" ) ),
               new simpleIDs( assbomResultSetRecords.getString( "bom_part_db_id" ),
                     assbomResultSetRecords.getString( "bom_part_id" ) ) );

      } catch ( SQLException e ) {
         e.printStackTrace();
      }

      return lassm;

   }


   /**
    * This function is to retrieve PART's assemble information.
    *
    *
    */
   public invInfor getINVINFor( String aPART_NO_OEM, String aMANUFACT_CD, String aSerial_NO_OEM ) {

      invInfor lassm = null;

      String lQuery = "select inv1.inv_no_db_id, " + "inv1.inv_no_id, " + "inv1.serial_no_oem "
            + "from eqp_part_no " + "inner join eqp_part_baseline on "
            + "eqp_part_baseline.part_no_db_id=eqp_part_no.part_no_db_id and "
            + "eqp_part_baseline.part_no_id=eqp_part_no.part_no_id " + "inner join eqp_bom_part on "
            + "eqp_bom_part.bom_part_db_id=eqp_part_baseline.bom_part_db_id and "
            + "eqp_bom_part.bom_part_id=eqp_part_baseline.bom_part_id "
            + "inner join eqp_assmbl_pos on "
            + "eqp_assmbl_pos.assmbl_db_id=eqp_bom_part.bom_part_db_id and "
            + "eqp_assmbl_pos.assmbl_cd=eqp_bom_part.assmbl_cd and "
            + "eqp_assmbl_pos.assmbl_bom_id=eqp_bom_part.assmbl_bom_id "
            + "inner join eqp_assmbl_bom on "
            + "eqp_assmbl_bom.assmbl_db_id=eqp_bom_part.assmbl_db_id and "
            + "eqp_assmbl_bom.assmbl_cd=eqp_bom_part.assmbl_cd and "
            + "eqp_assmbl_bom.assmbl_bom_id=eqp_bom_part.assmbl_bom_id "
            + "inner join inv_inv inv1 on " + "inv1.part_no_id=eqp_part_no.part_no_id "
            + "inner join inv_inv inv2 on " + "inv2.inv_no_id=inv1.h_inv_no_id "
            + "where eqp_part_no.PART_NO_OEM='" + aPART_NO_OEM + "' and eqp_part_no.MANUFACT_CD='"
            + aMANUFACT_CD + "' and inv2.serial_no_oem='" + aSerial_NO_OEM + "'";

      ResultSet assbomResultSetRecords;
      try {
         assbomResultSetRecords = runQuery( lQuery );
         assbomResultSetRecords.next();
         lassm = new invInfor(
               new simpleIDs( assbomResultSetRecords.getString( "inv_no_db_id" ),
                     assbomResultSetRecords.getString( "inv_no_id" ) ),
               assbomResultSetRecords.getString( "serial_no_oem" ) );

      } catch ( SQLException e ) {
         e.printStackTrace();
      }

      return lassm;

   }


   /**
    * This function is to verify SCHED_LABOUR_ROLE_STATUS table details part
    *
    *
    */
   public void verifySCHEDLABOURROLESTATUS( simpleIDs aIDs, String aStatusCD ) {

      String ldbid = getlocalDBid();

      // SCHED_LABOUR_ROLE_STATUS table
      String[] iIds = { "LABOUR_ROLE_STATUS_CD", "STATUS_DB_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "LABOUR_ROLE_DB_ID", aIDs.getNO_DB_ID() );
      lArgs.addArguments( "LABOUR_ROLE_ID", aIDs.getNO_ID() );

      String iQueryString =
            TableUtil.buildTableQuery( TableUtil.SCHED_LABOUR_ROLE_STATUS, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "LABOUR_ROLE_STATUS_CD",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aStatusCD ) );
      Assert.assertTrue( "STATUS_DB_ID", llists.get( 0 ).get( 1 ).equalsIgnoreCase( ldbid ) );

   }


   /**
    * This function is to verify SCHED_LABOUR_ROLE table details part
    *
    * @return LABOUUR_ROLE ID
    *
    *
    */
   public simpleIDs verifySCHEDLABOURROLE( simpleIDs aIDs, String aRoleTypeCD ) {

      // SCHED_LABOUR_ROLE table
      String[] iIds = { "LABOUR_ROLE_DB_ID", "LABOUR_ROLE_ID", "LABOUR_ROLE_TYPE_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "LABOUR_DB_ID", aIDs.getNO_DB_ID() );
      lArgs.addArguments( "LABOUR_ID", aIDs.getNO_ID() );

      String iQueryString =
            TableUtil.buildTableQuery( TableUtil.SCHED_LABOUR_ROLE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      Assert.assertTrue( "LABOUR_ROLE_TYPE_CD",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aRoleTypeCD ) );

      return lIds;

   }


   /**
    * This function is to verify SCHED_LABOUR table details part
    *
    * @return LABOUUR ID
    *
    *
    */
   public simpleIDs verifySCHEDLABOUR( simpleIDs aIDs, String aStageCD, String aSkillCD ) {

      String llaborSkillDBid = getStringValueFromQuery(
            "SELECT LABOUR_SKILL_DB_ID FROM  ref_labour_skill WHERE labour_skill_cd='" + aSkillCD
                  + "'",
            "LABOUR_SKILL_DB_ID" );
      // SCHED_LABOUR table
      String[] iIds = { "LABOUR_DB_ID", "LABOUR_ID", "LABOUR_STAGE_CD", "LABOUR_SKILL_CD",
            "LABOUR_SKILL_DB_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "SCHED_DB_ID", aIDs.getNO_DB_ID() );
      lArgs.addArguments( "SCHED_ID", aIDs.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.SCHED_LABOUR, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      Assert.assertTrue( "LABOUR_STAGE_CD", llists.get( 0 ).get( 2 ).equalsIgnoreCase( aStageCD ) );
      Assert.assertTrue( "LABOUR_SKILL_CD", llists.get( 0 ).get( 3 ).equalsIgnoreCase( aSkillCD ) );
      Assert.assertTrue( "LABOUR_SKILL_DB_ID",
            llists.get( 0 ).get( 4 ).equalsIgnoreCase( llaborSkillDBid ) );

      return lIds;

   }


   /**
    * This function is to verify SD_FAULT table details part
    *
    *
    */
   public void verifySDFAULT( simpleIDs aIDs, String aFAIL_DEFER_CD, String aFAULT_SOURCE_CD,
         String aFAIL_SEV_CD, String aDEFER_REF_SDESC, String aOP_RESTRICTION_LDESC,
         String aDEFER_CD_SDESC, String aFAULT_LOG_TYPE_CD, String aFRM_CD ) {

      // SD_FAULT table
      String[] iIds = { "FAIL_DEFER_CD", "FAULT_SOURCE_CD", "FAIL_SEV_CD", "DEFER_REF_SDESC",
            "OP_RESTRICTION_LDESC", "DEFER_CD_SDESC", "FAULT_LOG_TYPE_CD", "FRM_SDESC" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "FAULT_DB_ID", aIDs.getNO_DB_ID() );
      lArgs.addArguments( "FAULT_ID", aIDs.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.SD_FAULT, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "FAIL_DEFER_CD",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aFAIL_DEFER_CD ) );
      Assert.assertTrue( "FAULT_SOURCE_CD",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( aFAULT_SOURCE_CD ) );
      Assert.assertTrue( "FAIL_SEV_CD", llists.get( 0 ).get( 2 ).equalsIgnoreCase( aFAIL_SEV_CD ) );
      Assert.assertTrue( "DEFER_REF_SDESC",
            llists.get( 0 ).get( 3 ).equalsIgnoreCase( aDEFER_REF_SDESC ) );
      Assert.assertTrue( "OP_RESTRICTION_LDESC",
            llists.get( 0 ).get( 4 ).equalsIgnoreCase( aOP_RESTRICTION_LDESC ) );
      Assert.assertTrue( "DEFER_CD_SDESC",
            llists.get( 0 ).get( 5 ).equalsIgnoreCase( aDEFER_CD_SDESC ) );
      Assert.assertTrue( "FAULT_LOG_TYPE_CD",
            llists.get( 0 ).get( 6 ).equalsIgnoreCase( aFAULT_LOG_TYPE_CD ) );
      Assert.assertTrue( "FRM_SDESC", llists.get( 0 ).get( 7 ).equalsIgnoreCase( aFRM_CD ) );

   }


   /**
    * This function is to verify SCHED_STASK table details part
    *
    *
    */
   public void verifySCHEDSTASK( simpleIDs aTSIDs, simpleIDs aCFIDs, simpleIDs aPriorityIds,
         simpleIDs aTasKCDs, simpleIDs aINVIDs ) {

      // SCHED_STASK table
      String[] iIds = { "TASK_PRIORITY_DB_ID", "TASK_PRIORITY_CD", "TASK_CLASS_DB_ID",
            "TASK_CLASS_CD", "MAIN_INV_NO_DB_ID", "MAIN_INV_NO_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "SCHED_DB_ID", aTSIDs.getNO_DB_ID() );
      lArgs.addArguments( "SCHED_ID", aTSIDs.getNO_ID() );
      lArgs.addArguments( "FAULT_DB_ID", aCFIDs.getNO_DB_ID() );
      lArgs.addArguments( "FAULT_ID", aCFIDs.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.SCHED_STASK, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "TASK_PRIORITY_DB_ID",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aPriorityIds.getNO_DB_ID() ) );
      Assert.assertTrue( "TASK_PRIORITY_CD",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( aPriorityIds.getNO_ID() ) );
      Assert.assertTrue( "TASK_CLASS_DB_ID",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aTasKCDs.getNO_DB_ID() ) );
      Assert.assertTrue( "TASK_CLASS_CD",
            llists.get( 0 ).get( 3 ).equalsIgnoreCase( aTasKCDs.getNO_ID() ) );
      Assert.assertTrue( "MAIN_INV_NO_DB_ID",
            llists.get( 0 ).get( 4 ).equalsIgnoreCase( aINVIDs.getNO_DB_ID() ) );
      Assert.assertTrue( "MAIN_INV_NO_ID",
            llists.get( 0 ).get( 5 ).equalsIgnoreCase( aINVIDs.getNO_ID() ) );

   }


   /**
    * This function is to retrieve task class information from ref_task_class table.
    *
    *
    */
   public simpleIDs getTaskCDs( String aTASK_CD ) {

      // REF_TASK_CLASS table
      String[] iIds = { "TASK_CLASS_DB_ID", "TASK_CLASS_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_CLASS_CD", aTASK_CD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.REF_TASK_CLASS, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lIds;

   }


   /**
    * This function is to retrieve task priority information from ref_task_priority table.
    *
    *
    */
   public simpleIDs getPriorityIDs( String aTASK_PRIORITY_CD ) {
      // ref_task_priority table
      String[] iIds = { "TASK_PRIORITY_DB_ID", "TASK_PRIORITY_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_PRIORITY_CD", aTASK_PRIORITY_CD );

      String iQueryString =
            TableUtil.buildTableQuery( TableUtil.REF_TASK_PRIORITY, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lIds;

   }


   /**
    * This function is to verify EVT_SCHED_DEAD table
    *
    *
    */
   public void verifyEVTSCHEDDEAD( simpleIDs aEvtIDs, simpleIDs aTypeIds ) {
      String lQuery = "Select 1 from " + TableUtil.EVT_SCHED_DEAD + " where EVENT_DB_ID="
            + aEvtIDs.getNO_DB_ID() + " and EVENT_ID=" + aEvtIDs.getNO_ID()
            + " and DATA_TYPE_DB_ID=" + aTypeIds.getNO_DB_ID() + " and DATA_TYPE_ID="
            + aTypeIds.getNO_ID();

      Assert.assertTrue( "Check EVT_SCHED_DEAD table to verify the record exists.",
            RecordsExist( lQuery ) );

   }


   /**
    * This function is to retrieve data type information from mim_data_type table.
    *
    *
    */
   public simpleIDs getMIMType( String aType ) {

      // MIM_DATA_TYPE table
      String[] iIds = { "DATA_TYPE_DB_ID", "DATA_TYPE_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "DATA_TYPE_CD", aType );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.MIM_DATA_TYPE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lIds;

   }


   /**
    * This function is to verify EVT_STAGE table
    *
    *
    */
   public void verifyEVTSTAGE( simpleIDs aIDs, List<String> alist ) {
      for ( int i = 0; i < alist.size(); i++ ) {
         String lQuery = "Select 1 from " + TableUtil.EVT_STAGE + " where EVENT_DB_ID="
               + aIDs.getNO_DB_ID() + " and EVENT_ID=" + aIDs.getNO_ID()
               + " and upper(STAGE_NOTE) like '%" + alist.get( i ) + "%'";

         Assert.assertTrue(
               "Check EVT_STAGE table to verify the record: " + alist.get( i ) + " exists.",
               RecordsExist( lQuery ) );
      }

   }


   /**
    * This function is to verify EVT_EVENT_REL table
    *
    *
    */
   public void verifyEVTEVENT_REL( simpleIDs aEvtIDs, simpleIDs aRelIDs, String aRelTypeCD ) {

      String lQuery = "Select 1 from " + TableUtil.EVT_EVENT_REL + " where EVENT_DB_ID="
            + aEvtIDs.getNO_DB_ID() + " and EVENT_ID=" + aEvtIDs.getNO_ID()
            + " and REL_EVENT_DB_ID=" + aRelIDs.getNO_DB_ID() + " and REL_EVENT_ID="
            + aRelIDs.getNO_ID() + " and REL_TYPE_CD='" + aRelTypeCD + "'";

      Assert.assertTrue( "Check EVT_EVENT_REl table to verify the record exists",
            RecordsExist( lQuery ) );
   }


   /**
    * This function is to verify EVT_INV table
    *
    *
    */
   public invIDs verifyEVTINV( simpleIDs aIDs, String sAssmbl_CD, String aBomID, String aPosID ) {

      // EVT_INV table
      String[] iIds = { "INV_NO_DB_ID", "INV_NO_ID", "ASSMBL_INV_NO_DB_ID", "ASSMBL_INV_NO_ID",
            "ASSMBL_CD", "ASSMBL_BOM_ID", "ASSMBL_POS_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "EVENT_DB_ID", aIDs.getNO_DB_ID() );
      lArgs.addArguments( "EVENT_ID", aIDs.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EVT_INV, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      invIDs lIds = new invIDs( new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) ),
            new simpleIDs( llists.get( 0 ).get( 2 ), llists.get( 0 ).get( 3 ) ) );

      Assert.assertTrue( "ASSMBL_CD", llists.get( 0 ).get( 4 ).equalsIgnoreCase( sAssmbl_CD ) );
      Assert.assertTrue( "ASSMBL_BOM_ID", llists.get( 0 ).get( 5 ).equalsIgnoreCase( aBomID ) );
      Assert.assertTrue( "ASSMBL_POS_ID", llists.get( 0 ).get( 6 ).equalsIgnoreCase( aPosID ) );

      return lIds;

   }


   /**
    * This function is to verify EVT_EVENT table
    *
    * @return event ID
    *
    */
   public simpleIDs verifyEVTEVENT( String aEventTypeCd, String aSDESC, String aStatusCD,
         String aLDESC, String aDOCRefSDESC ) {

      // EVT_EVENT table
      String[] iIds =
            { "EVENT_DB_ID", "EVENT_ID", "EVENT_STATUS_CD", "EVENT_LDESC", "DOC_REF_SDESC" };
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
      if ( aDOCRefSDESC != null ) {
         Assert.assertTrue( "DOC_REF_SDESC",
               llists.get( 0 ).get( 4 ).equalsIgnoreCase( aDOCRefSDESC ) );
      }

      return lIds;

   }


   /**
    * This function is to set current date on DUE_DT column of AL_OPEN_DEFERRED_FAULT
    *
    *
    *
    */

   public void setCurrentDate( String aSN ) {
      String aUpdateQuery =
            "UPDATE AL_OPEN_DEFERRED_FAULT SET DUE_DT= (SELECT SYSDATE FROM DUAL) where SERIAL_NO_OEM='"
                  + aSN + "'";
      try {

         PreparedStatement lStatement = getConnection().prepareStatement( aUpdateQuery );
         lStatement.executeUpdate( aUpdateQuery );
         commit();

      } catch ( SQLException e ) {
         e.printStackTrace();
      }
   }


   /**
    * This function is called by before AMLODF-12025 test to setup PCT=2 of SYS-1-1
    *
    *
    */

   public void dataSetupPCT() {
      String aUpdateQuery = "UPDATE eqp_assmbl_bom SET POS_CT= '2' where assmbl_bom_cd='SYS-1-1'";

      try {

         PreparedStatement lStatement = getConnection().prepareStatement( aUpdateQuery );
         lStatement.executeUpdate( aUpdateQuery );
         commit();

      } catch ( SQLException e ) {
         e.printStackTrace();
      }

   }


   /**
    * This function is called by before AMLODF-12110 test to setup precision as 2
    *
    *
    */

   public void dataSetupPrecision() {
      String aUpdateQuery = "UPDATE ref_qty_unit SET decimal_places_qt= '2' where qty_unit_cd='EA'";

      try {

         PreparedStatement lStatement = getConnection().prepareStatement( aUpdateQuery );
         lStatement.executeUpdate( aUpdateQuery );
         commit();

      } catch ( SQLException e ) {
         e.printStackTrace();
      }

   }


   /**
    * This function is called by before AMLODF-12026 test to add one more position record
    *
    *
    */

   public void dataSetupPOS() {
      String lquery =
            "select assmbl_bom_id from  eqp_assmbl_bom where assmbl_bom_cd='SYS-1-1' and assmbl_cd='ACFT_CD1'";
      String lBomID = getStringValueFromQuery( lquery, "ASSMBL_BOM_ID" );
      assmblPOS ldata = getRecordToCopy( "ACFT_CD1", lBomID, "2" );

      lquery =
            "insert into eqp_assmbl_pos (ASSMBL_DB_ID, ASSMBL_CD, ASSMBL_BOM_ID, ASSMBL_POS_ID,EQP_POS_CD, NH_ASSMBL_DB_ID,"
                  + " NH_ASSMBL_CD, NH_ASSMBL_BOM_ID,NH_ASSMBL_POS_ID) "
                  + " values (4650, 'ACFT_CD1','" + ldata.getASSMBL_BOM_ID() + "','2', '"
                  + ldata.getEQP_POS_CD() + "', 4650, '" + ldata.getNH_ASSMBL_CD() + "', '"
                  + ldata.getNH_ASSMBL_BOM_ID() + "', '" + ldata.getNH_ASSMBL_POS_ID() + "')";
      executeSQL( lquery );

   }


   /**
    * This function is to retrive a record in eqp-assmbl_pos table and save it to assmblPOS object.
    *
    *
    */

   public assmblPOS getRecordToCopy( String aASSMBL_CD, String aASSMBL_BOM_ID,
         String aAssmblPOSID ) {

      // MIM_DATA_TYPE table
      String[] iIds = { "EQP_POS_CD", "NH_ASSMBL_CD", "NH_ASSMBL_BOM_ID", "NH_ASSMBL_POS_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "ASSMBL_CD", aASSMBL_CD );
      lArgs.addArguments( "ASSMBL_BOM_ID", aASSMBL_BOM_ID );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EQP_ASSMBL_POS, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      assmblPOS ldata =
            new assmblPOS( aASSMBL_CD, aASSMBL_BOM_ID, aAssmblPOSID, llists.get( 0 ).get( 0 ),
                  llists.get( 0 ).get( 1 ), llists.get( 0 ).get( 2 ), llists.get( 0 ).get( 3 ) );

      return ldata;

   }


   /**
    * This function is to retrive parm value from UTL_CONFIG_PARM for ADHOC_CORRTASK_LABOUR
    *
    *
    */

   public String getParmValue() {
      String lquery =
            "select PARM_VALUE from UTL_CONFIG_PARM where PARM_NAME='ADHOC_CORRTASK_LABOUR'";
      return getStringValueFromQuery( lquery, "PARM_VALUE" );

   }


   /**
    * This function is to set parm value from UTL_CONFIG_PARM
    *
    *
    */
   public void dataSetForPARM( String aPARMNAME, String aPARMVALUE ) {
      String lquery = "update utl_config_parm set PARM_VALUE='" + aPARMVALUE + "' where PARM_NAME='"
            + aPARMNAME + "'";
      runUpdate( lquery );

   }


   /**
    * This function is to setup duplicate ACFT
    *
    *
    */
   public void dataSetupACFTDup() {
      // String lquery =
      // "update inv_inv set serial_no_oem ='sn000013' where inv_no_sdesc='Aircraft Part 1 - LOCK'";
      // runUpdate( lquery );

      String lquery =
            "update eqp_part_no set part_no_oem='ACFT_ASSY_PN1' where part_no_oem='ACFT_ASSY_PN2' and "
                  + " manufact_cd='11111' and part_no_sdesc='Aircraft Part 2'";
      runUpdate( lquery );
   }


   /**
    * This function is to restore duplicate ACFT
    *
    *
    */
   public void dataRestoreACFTDup() {
      // String lquery =
      // "update inv_inv set serial_no_oem ='SN-LOCKED' where inv_no_sdesc='Aircraft Part 1 -
      // LOCK'";
      // runUpdate( lquery );

      String lquery =
            "update eqp_part_no set part_no_oem='ACFT_ASSY_PN2' where part_no_oem='ACFT_ASSY_PN1' and "
                  + " manufact_cd='11111' and part_no_sdesc='Aircraft Part 2'";
      runUpdate( lquery );
   }


   /**
    * This function is to setup duplicate part_no_oem with different manfacture code
    *
    *
    */
   public void dataSetupMACFACTDup() {
      String lquery =
            "update inv_inv set serial_no_oem='SN000001' where serial_no_oem='SN000012' and inv_no_sdesc='Aircraft Part 1 - SCRP'";
      runUpdate( lquery );
      //
      // lquery =
      // "update eqp_part_no set part_no_oem='ACFT_ASSY_PN1' where part_no_oem='ACFT_ASSY_PN2' and
      // manufact_cd='11111' and part_no_sdesc='Aircraft Part 2'";
      // runUpdate( lquery );
   }


   /**
    * This function is to restore duplicate part_no_oem with different manfacture code
    *
    *
    */
   public void dataRestoreMACFACTDup() {
      String lquery =
            "update inv_inv set serial_no_oem='SN000012' where serial_no_oem='SN000001' and inv_no_sdesc='Aircraft Part 1 - SCRP'";
      runUpdate( lquery );

      // lquery =
      // "update eqp_part_no set part_no_oem='ACFT_ASSY_PN2' where part_no_oem='ACFT_ASSY_PN1' and
      // manufact_cd='11111' and part_no_sdesc='Aircraft Part 2'";
      // runUpdate( lquery );
   }


   /**
    * This function is called by after test method to restore data which has been modified by tests.
    *
    *
    */

   public void RestoreData() {

      String lStrDelete = null;

      // Delete sched_part, sched_inst_part and sched_rmvd_part
      if ( iTSIDs1 != null ) {
         lStrDelete = "delete from " + TableUtil.SCHED_RMVD_PART + " where SCHED_DB_ID='"
               + iTSIDs1.getNO_DB_ID() + "' and SCHED_ID='" + iTSIDs1.getNO_ID() + "'";
         executeSQL( lStrDelete );

         lStrDelete = "delete from " + TableUtil.SCHED_INST_PART + " where SCHED_DB_ID='"
               + iTSIDs1.getNO_DB_ID() + "' and SCHED_ID='" + iTSIDs1.getNO_ID() + "'";
         executeSQL( lStrDelete );

         lStrDelete = "delete from " + TableUtil.SCHED_PART + " where SCHED_DB_ID='"
               + iTSIDs1.getNO_DB_ID() + "' and SCHED_ID='" + iTSIDs1.getNO_ID() + "'";
         executeSQL( lStrDelete );

      }

      if ( iTSIDs2 != null ) {
         lStrDelete = "delete from " + TableUtil.SCHED_RMVD_PART + " where SCHED_DB_ID='"
               + iTSIDs2.getNO_DB_ID() + "' and SCHED_ID='" + iTSIDs2.getNO_ID() + "'";
         executeSQL( lStrDelete );

         lStrDelete = "delete from " + TableUtil.SCHED_INST_PART + " where SCHED_DB_ID='"
               + iTSIDs2.getNO_DB_ID() + "' and SCHED_ID='" + iTSIDs2.getNO_ID() + "'";
         executeSQL( lStrDelete );

         lStrDelete = "delete from " + TableUtil.SCHED_PART + " where SCHED_DB_ID='"
               + iTSIDs2.getNO_DB_ID() + "' and SCHED_ID='" + iTSIDs2.getNO_ID() + "'";
         executeSQL( lStrDelete );

      }

      // Delete sched_labour_role_status and sched_labour_role
      if ( iLabourRoleID1 != null ) {
         lStrDelete = "delete from " + TableUtil.SCHED_LABOUR_ROLE_STATUS
               + " where LABOUR_ROLE_DB_ID='" + iLabourRoleID1.getNO_DB_ID()
               + "' and LABOUR_ROLE_ID='" + iLabourRoleID1.getNO_ID() + "'";
         executeSQL( lStrDelete );

         lStrDelete = "delete from " + TableUtil.SCHED_LABOUR_ROLE + " where LABOUR_ROLE_DB_ID='"
               + iLabourRoleID1.getNO_DB_ID() + "' and LABOUR_ROLE_ID='" + iLabourRoleID1.getNO_ID()
               + "'";
         executeSQL( lStrDelete );

      }

      if ( iLabourRoleID2 != null ) {
         lStrDelete = "delete from " + TableUtil.SCHED_LABOUR_ROLE_STATUS
               + " where LABOUR_ROLE_DB_ID='" + iLabourRoleID2.getNO_DB_ID()
               + "' and LABOUR_ROLE_ID='" + iLabourRoleID2.getNO_ID() + "'";
         executeSQL( lStrDelete );

         lStrDelete = "delete from " + TableUtil.SCHED_LABOUR_ROLE + " where LABOUR_ROLE_DB_ID='"
               + iLabourRoleID2.getNO_DB_ID() + "' and LABOUR_ROLE_ID='" + iLabourRoleID2.getNO_ID()
               + "'";
         executeSQL( lStrDelete );

      }

      // Delete sched_labour
      if ( iTSIDs1 != null ) {
         lStrDelete = "delete from " + TableUtil.SCHED_LABOUR + " where SCHED_DB_ID='"
               + iTSIDs1.getNO_DB_ID() + "' and SCHED_ID='" + iTSIDs1.getNO_ID() + "'";
         executeSQL( lStrDelete );
      }

      if ( iTSIDs2 != null ) {
         lStrDelete = "delete from " + TableUtil.SCHED_LABOUR + " where SCHED_DB_ID='"
               + iTSIDs2.getNO_DB_ID() + "' and SCHED_ID='" + iTSIDs2.getNO_ID() + "'";
         executeSQL( lStrDelete );
      }

      // Delete SD_FAULT table
      if ( iCFIDs1 != null ) {
         lStrDelete = "delete from " + TableUtil.SD_FAULT + " where FAULT_DB_ID='"
               + iCFIDs1.getNO_DB_ID() + "' and FAULT_ID='" + iCFIDs1.getNO_ID() + "'";
         executeSQL( lStrDelete );

      }

      if ( iCFIDs2 != null ) {
         lStrDelete = "delete from " + TableUtil.SD_FAULT + " where FAULT_DB_ID='"
               + iCFIDs2.getNO_DB_ID() + "' and FAULT_ID='" + iCFIDs2.getNO_ID() + "'";
         executeSQL( lStrDelete );

      }

      // Delete SCHED_STASK, EVT_SCHED_DEAD,EVT_STAGE(task part), evt_event_rel(task part),
      // evt_inv(task part), evt_event(task part)
      if ( iTSIDs1 != null ) {
         lStrDelete = "delete from " + TableUtil.SCHED_STASK + " where SCHED_DB_ID='"
               + iTSIDs1.getNO_DB_ID() + "' and SCHED_ID='" + iTSIDs1.getNO_ID() + "'";
         executeSQL( lStrDelete );

         lStrDelete = "delete from " + TableUtil.EVT_SCHED_DEAD + " where EVENT_DB_ID='"
               + iTSIDs1.getNO_DB_ID() + "' and EVENT_ID='" + iTSIDs1.getNO_ID() + "'";
         executeSQL( lStrDelete );

         lStrDelete = "delete from " + TableUtil.EVT_STAGE + " where EVENT_DB_ID='"
               + iTSIDs1.getNO_DB_ID() + "' and EVENT_ID='" + iTSIDs1.getNO_ID() + "'";
         executeSQL( lStrDelete );

         lStrDelete = "delete from " + TableUtil.EVT_EVENT_REL + " where EVENT_DB_ID='"
               + iTSIDs1.getNO_DB_ID() + "' and EVENT_ID='" + iTSIDs1.getNO_ID() + "'";
         executeSQL( lStrDelete );

         lStrDelete = "delete from " + TableUtil.EVT_INV + " where EVENT_DB_ID='"
               + iTSIDs1.getNO_DB_ID() + "' and EVENT_ID='" + iTSIDs1.getNO_ID() + "'";
         executeSQL( lStrDelete );

         lStrDelete = "delete from " + TableUtil.EVT_EVENT + " where EVENT_DB_ID='"
               + iTSIDs1.getNO_DB_ID() + "' and EVENT_ID='" + iTSIDs1.getNO_ID() + "'";
         executeSQL( lStrDelete );

      }

      if ( iTSIDs2 != null ) {
         lStrDelete = "delete from " + TableUtil.SCHED_STASK + " where SCHED_DB_ID='"
               + iTSIDs2.getNO_DB_ID() + "' and SCHED_ID='" + iTSIDs2.getNO_ID() + "'";
         executeSQL( lStrDelete );

         lStrDelete = "delete from " + TableUtil.EVT_SCHED_DEAD + " where EVENT_DB_ID='"
               + iTSIDs2.getNO_DB_ID() + "' and EVENT_ID='" + iTSIDs2.getNO_ID() + "'";
         executeSQL( lStrDelete );

         lStrDelete = "delete from " + TableUtil.EVT_STAGE + " where EVENT_DB_ID='"
               + iTSIDs2.getNO_DB_ID() + "' and EVENT_ID='" + iTSIDs2.getNO_ID() + "'";
         executeSQL( lStrDelete );

         lStrDelete = "delete from " + TableUtil.EVT_EVENT_REL + " where EVENT_DB_ID='"
               + iTSIDs2.getNO_DB_ID() + "' and EVENT_ID='" + iTSIDs2.getNO_ID() + "'";
         executeSQL( lStrDelete );

         lStrDelete = "delete from " + TableUtil.EVT_INV + " where EVENT_DB_ID='"
               + iTSIDs2.getNO_DB_ID() + "' and EVENT_ID='" + iTSIDs2.getNO_ID() + "'";
         executeSQL( lStrDelete );

         lStrDelete = "delete from " + TableUtil.EVT_EVENT + " where EVENT_DB_ID='"
               + iTSIDs2.getNO_DB_ID() + "' and EVENT_ID='" + iTSIDs2.getNO_ID() + "'";
         executeSQL( lStrDelete );

      }

      // delete EVT_STAGE(fault part), evt_event_rel(fault part), evt_inv(fault part),
      // evt_event(fault part)

      if ( iCFIDs1 != null ) {
         lStrDelete = "delete from " + TableUtil.EVT_STAGE + " where EVENT_DB_ID='"
               + iCFIDs1.getNO_DB_ID() + "' and EVENT_ID='" + iCFIDs1.getNO_ID() + "'";
         executeSQL( lStrDelete );

         lStrDelete = "delete from " + TableUtil.EVT_EVENT_REL + " where EVENT_DB_ID='"
               + iCFIDs1.getNO_DB_ID() + "' and EVENT_ID='" + iCFIDs1.getNO_ID() + "'";
         executeSQL( lStrDelete );

         lStrDelete = "delete from " + TableUtil.EVT_INV + " where EVENT_DB_ID='"
               + iCFIDs1.getNO_DB_ID() + "' and EVENT_ID='" + iCFIDs1.getNO_ID() + "'";
         executeSQL( lStrDelete );

         lStrDelete = "delete from " + TableUtil.EVT_EVENT + " where EVENT_DB_ID='"
               + iCFIDs1.getNO_DB_ID() + "' and EVENT_ID='" + iCFIDs1.getNO_ID() + "'";
         executeSQL( lStrDelete );

      }

      if ( iCFIDs2 != null ) {
         lStrDelete = "delete from " + TableUtil.EVT_STAGE + " where EVENT_DB_ID='"
               + iCFIDs2.getNO_DB_ID() + "' and EVENT_ID='" + iCFIDs2.getNO_ID() + "'";
         executeSQL( lStrDelete );

         lStrDelete = "delete from " + TableUtil.EVT_EVENT_REL + " where EVENT_DB_ID='"
               + iCFIDs2.getNO_DB_ID() + "' and EVENT_ID='" + iCFIDs2.getNO_ID() + "'";
         executeSQL( lStrDelete );

         lStrDelete = "delete from " + TableUtil.EVT_INV + " where EVENT_DB_ID='"
               + iCFIDs2.getNO_DB_ID() + "' and EVENT_ID='" + iCFIDs2.getNO_ID() + "'";
         executeSQL( lStrDelete );

         lStrDelete = "delete from " + TableUtil.EVT_EVENT + " where EVENT_DB_ID='"
               + iCFIDs2.getNO_DB_ID() + "' and EVENT_ID='" + iCFIDs2.getNO_ID() + "'";
         executeSQL( lStrDelete );

      }

      String aUpdateQuery = "UPDATE eqp_assmbl_bom SET POS_CT= '1' where assmbl_bom_cd='SYS-1-1'";
      executeSQL( aUpdateQuery );

      String lquery =
            "select assmbl_bom_id from  eqp_assmbl_bom where assmbl_bom_cd='SYS-1-1' and assmbl_cd='ACFT_CD1'";
      String lBomID = getStringValueFromQuery( lquery, "ASSMBL_BOM_ID" );

      aUpdateQuery = "delete from " + TableUtil.EQP_ASSMBL_POS
            + " where assmbl_cd='ACFT_CD1' and assmbl_bom_id='" + lBomID + "' and assmbl_pos_id=2";
      executeSQL( aUpdateQuery );

      aUpdateQuery = "UPDATE ref_qty_unit SET decimal_places_qt= '0' where qty_unit_cd='EA'";
      executeSQL( aUpdateQuery );

   }


   /**
    * This function is to implement interface ValidationAndImport
    *
    * @param: blnOnlyValidation
    * @param: allornone
    *
    */
   public int runValidationAndImport( boolean blnOnlyValidation, boolean allornone ) {
      int rtValue = 0;
      ivalidationandimport = new ValidationAndImport() {

         @Override
         public int runValidation( boolean allornone ) {
            int lReturn = 0;
            CallableStatement lPrepareCallInventory = null;

            try {
               if ( allornone ) {
                  lPrepareCallInventory = getConnection().prepareCall(
                        "BEGIN al_open_deferred_fault_pkg.validate(aib_allornone => true, ain_gather_stats_bool => ?,aon_retcode => ?, aov_retmsg =>?); END;" );
               } else {
                  lPrepareCallInventory = getConnection().prepareCall(
                        "BEGIN al_open_deferred_fault_pkg.validate(aib_allornone => false, ain_gather_stats_bool => ?,aon_retcode => ?, aov_retmsg =>?); END;" );

               }

               lPrepareCallInventory.setInt( 1, TestConstants.GATHER_STATS_DEFAULT );
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
         public int runImport( boolean allornone ) {
            int lReturn = 0;

            try {
               CallableStatement lPrepareCallInventory = getConnection().prepareCall(
                     "BEGIN  al_open_deferred_fault_pkg.import(ain_gather_stats_bool => ?,aon_retcode => ?, aov_retmsg =>?); END;" );

               lPrepareCallInventory.setInt( 1, TestConstants.GATHER_STATS_DEFAULT );
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

      rtValue = blnOnlyValidation ? ivalidationandimport.runValidation( allornone )
            : ivalidationandimport.runImport( allornone );

      return rtValue;
   }

}
