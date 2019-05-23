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
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.mxi.mx.core.maint.plan.datamodels.asmInfor;
import com.mxi.mx.core.maint.plan.datamodels.assmbleInfor;
import com.mxi.mx.core.maint.plan.datamodels.invIDs;
import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;
import com.mxi.mx.util.ActualsLoaderTest;
import com.mxi.mx.util.StringUtils;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.TestConstants;
import com.mxi.mx.util.ValidationAndImport;
import com.mxi.mx.util.WhereClause;


/**
 * This test suite contains test cases on validation and import functionality of
 * C_OPEN_DEFERRED_FAULT_IMPORT package. Actual loader would not support functionality to assign
 * faults on ENG and APU if you are installed on ACFT for now.
 *
 * @author ALICIA QIAN
 */
public class ValidationODF extends ActualsLoaderTest {

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
   public String iSN_ACFT = "SN000013";
   public String iPN_OEM_ACFT = "ACFT_ASSY_PN1";
   public String iMANFACT_CD_ACFT = "10001";
   public String iSYS_CD_ACFT = "SYS-1-1";
   public String iFAULT_SDESC = "AUTOFSDESC";
   public String iFAULT_LDESC = "AUTOFAULTLDESC";
   public String iFAIL_SEV_CD = "MEL";
   public String iFAIL_DEFER_CD = "MEL A";
   public String iFAIL_DEFER_CD_C = "MEL C";
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

   public String iParmValue = null;


   /**
    * Setup before executing each individual test
    *
    */
   @Override
   @Before

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
      if ( strTCName.contains( "25139_NULLVALUE" ) ) {
         iParmValue = getParmValue();
         dataSetupNull();
      } else if ( strTCName.contains( "testOPER_25139_EmptyValue" ) ) {
         iParmValue = getParmValue();
         dataSetupEMPTY();
      } else if ( strTCName.contains( "testOPER_25139_NONLBRValue" ) ) {
         iParmValue = getParmValue();
         dataSetupAFT();

      } else if ( strTCName.contains( "testOPER_25844" ) ) {
         dataSetupUNRELATED_PART_GROUP();

      }

   }


   /**
    * Clean up after each individual test
    */
   @After
   @Override
   public void after() {

      try {
         String strTCName = testName.getMethodName();
         if ( strTCName.contains( "25139" ) ) {
            dataRestoreParmValue();
         } else if ( strTCName.contains( "25844" ) ) {
            dataRestoreUNRELATED_PART_GROUP();

         }

         RestoreData();

         super.after();
      } catch ( Exception e ) {
         e.printStackTrace();
      }
   }


   /**
    * This test is to verify OPER25139:Labour skill specified for open deferred faults are being
    * ignored. All ODFs are created with LBR skill.
    *
    *
    */

   public void testOPER_25139_VALIDATION() {

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
      lFaults.put( "FOUND_ON_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      setCurrentDate( iSN_ACFT );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify OPER25139:Labour skill specified for open deferred faults are being
    * ignored. All ODFs are created with LBR skill.
    *
    *
    */
   @Test
   public void testOPER_25139_EmptyValue() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testOPER_25139_VALIDATION();

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
      // verifyEVTEVENT_REL( iTSIDs1, iTSIDs1, iREL_TYPE_CDTASK );

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
      iLabourID1 = verifySCHEDLABOUR( iTSIDs1, iACTV, iLBR );

      // Verify SCHED_LABOUR_ROLE table
      iLabourRoleID1 = verifySCHEDLABOURROLE( iLabourID1, iTECH );

      // Verify SCHED_LABOUR_ROLE_STATUS table
      verifySCHEDLABOURROLESTATUS( iLabourRoleID1, iACTV );

   }


   /**
    * This test is to verify OPER25139:Labour skill specified for open deferred faults are being
    * ignored. All ODFs are created with LBR skill.
    *
    *
    */
   @Test
   public void testOPER_25139_NONLBRValue() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testOPER_25139_VALIDATION();

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
      // verifyEVTEVENT_REL( iTSIDs1, iTSIDs1, iREL_TYPE_CDTASK );

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
      iLabourID1 = verifySCHEDLABOUR( iTSIDs1, iACTV, "AFT" );

      // Verify SCHED_LABOUR_ROLE table
      iLabourRoleID1 = verifySCHEDLABOURROLE( iLabourID1, iTECH );

      // Verify SCHED_LABOUR_ROLE_STATUS table
      verifySCHEDLABOURROLESTATUS( iLabourRoleID1, iACTV );

   }


   /**
    * This test is to verify OPER25142:Labor Actuals Loader Creates Open Deferred Faults with "AMU
    * AMU" added to the description
    *
    *
    */
   public void testOPER_25142_VALIDATION() {

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
      lFaults.put( "FOUND_ON_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      setCurrentDate( iSN_ACFT );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify OPER25142:Labour Actuals Loader Creates Open Deferred Faults with "AMU
    * AMU" added to the description
    *
    *
    */

   @Test
   public void testOPER_25142_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testOPER_25142_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify EVT_EVENT table
      iCFIDs1 = verifyEVTEVENT( iEventTypeCdFault, iFAULT_SDESC, "CFDEFER", iFAULT_LDESC,
            iLOGBOOK_REFERENCE );

      iTSIDs1 = verifyEVTEVENT( iEventTypeCdTask, iFAULT_SDESC, "ACTV", iFAULT_LDESC, null );

      // OPER-25142
      verifyEVTEVENT( iEventTypeCdFault, iFAULT_SDESC, "CFDEFER", "ActualsLoader",
            iLOGBOOK_REFERENCE );

      verifyEVTEVENT( iEventTypeCdTask, iFAULT_SDESC, "ACTV", "ActualsLoader", null );

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
      // verifyEVTEVENT_REL( iTSIDs1, iTSIDs1, iREL_TYPE_CDTASK );

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
      } else {
         iLabourID1 = verifySCHEDLABOUR( iTSIDs1, iACTV, iLabor );
      }

      // Verify SCHED_LABOUR_ROLE table
      iLabourRoleID1 = verifySCHEDLABOURROLE( iLabourID1, iTECH );

      // Verify SCHED_LABOUR_ROLE_STATUS table
      verifySCHEDLABOURROLESTATUS( iLabourRoleID1, iACTV );

   }


   /**
    * This test is to verify OPER-25844:Actuals Loader - Validation Preventing Part Requirements on
    * Task with Different Assembly
    *
    *
    */

   @Test
   public void testOPER_25844() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "\'" + iAssmbl_ACFT_CD + "\'" );
      lFaults.put( "SERIAL_NO_OEM", "\'" + iSN_ACFT2 + "\'" );
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
      // lFaults.put( "PN1_POS_CD", "\'" + iPN1_POS_CD + "\'" );

      lFaults.put( "PN1_PART_NO_OEM", "'E0000016'" );
      lFaults.put( "PN1_MANUFACT_CD", "'1234567890'" );
      lFaults.put( "PN1_IPC_REF_CD", "'ENG-SYS-2-1-1-TRK'" );
      lFaults.put( "PN1_POS_CD", "'1'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "FOUND_ON_DT", "SYSDATE" );
      lFaults.put( "DUE_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      int lresult = runValidationAndImport( true, true );
      checkODFErrorCodeDebug( testName.getMethodName() );
      Assert.assertTrue( "The actual validation result is " + Integer.toString( lresult ),
            lresult == 1 );

   }


   /**
    * This test is to verify OPER-15445:AFaults loaded with Actuals Loader have incorrectly set
    * interval when they have a custom due date. This is the case when FOUND_DT is provided.
    * Inteval_qt will be calculated by due date-start date (foundon_dt).
    *
    *
    *
    */

   @Test
   public void testOPER15445_FOUND_DT_VALIDATION() {

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
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD_C + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'2\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'5\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'10\'" );
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
      lFaults.put( "DUE_DT", "sysdate+5" );
      lFaults.put( "FOUND_ON_DT", "sysdate" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify OPER-15445:AFaults loaded with Actuals Loader have incorrectly set
    * interval when they have a custom due date. This is the case when FOUND_DT is provided.
    * Inteval_qt will be calculated by due date-start date (foundon_dt).
    *
    *
    *
    */
   @Test
   public void testOPER15445_FOUND_DT_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testOPER15445_FOUND_DT_VALIDATION();

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
      // verifyEVTEVENT_REL( iTSIDs1, iTSIDs1, iREL_TYPE_CDTASK );

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
      lQuery = "SELECT SYSDATE+5 as duedate FROM DUAL";
      java.sql.Date lduedate = getDateValueFromQuery( lQuery, "duedate" );

      lQuery = "SELECT SYSDATE as startdate FROM DUAL";
      java.sql.Date lstartdate = getDateValueFromQuery( lQuery, "startdate" );

      simpleIDs lHoursIds = getMIMType( "HOURS" );
      simpleIDs lCycIds = getMIMType( "CYCLES" );
      simpleIDs lDayIds = getMIMType( "CDY" );
      verifyEVTSCHEDDEAD_DATE_QT( iTSIDs1, lHoursIds, null, null, "-3", "5" );
      verifyEVTSCHEDDEAD_DATE_QT( iTSIDs1, lCycIds, null, null, "6", "4" );
      verifyEVTSCHEDDEAD_DATE_QT( iTSIDs1, lDayIds, lduedate, lstartdate, null, "5" );

      // Verify SCHED_STASK table
      simpleIDs lPriorityIds = getPriorityIDs( iTASK_PRIORITY_CD );
      simpleIDs lTaskCDs = getTaskCDs( "CORR" );
      verifySCHEDSTASK( iTSIDs1, iCFIDs1, lPriorityIds, lTaskCDs, lINVIDSTASK.getINVIDs() );

      // Verify SD_FAULT
      verifySDFAULT( iCFIDs1, iFAIL_DEFER_CD_C, iFAULT_SOURCE_CD, iFAIL_SEV_CD, iDEFER_REF_SDESC,
            iOP_RESTRICTION_LDESC, iDEFER_CD_SDESC, iFAULT_LOG_TYPE_CD, iFRM_CD );

      // Verify sched_labour table
      String iLabor = getParmValue();
      if ( StringUtils.isBlank( iLabor ) ) {
         iLabourID1 = verifySCHEDLABOUR( iTSIDs1, iACTV, iLBR );
      } else {
         iLabourID1 = verifySCHEDLABOUR( iTSIDs1, iACTV, iLabor );
      }

      // Verify SCHED_LABOUR_ROLE table
      iLabourRoleID1 = verifySCHEDLABOURROLE( iLabourID1, iTECH );

      // Verify SCHED_LABOUR_ROLE_STATUS table
      verifySCHEDLABOURROLESTATUS( iLabourRoleID1, iACTV );

      // // Verify SCHED_PART
      // asmInfor linfors = getASMINFor( iPN1_PART_NO_OEM, iPN1_MANUFACT_CD );
      // verifySCHEDPART( iTSIDs1, linfors, iACTV, "1", "REQ", true );
      //
      // // Verify SCHED_INST_PART
      // verifySCHEDINSTPART( iTSIDs1, linfors, "1", 1 );
      //
      // // Verify SCHED_RMVD_PART
      // verifySCHEDRMVDPART( iTSIDs1, linfors, "IMSCHD" );

   }


   @Test
   public void testOPER_24508_VALIDATION() {

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
      lFaults.put( "PN1_PART_NO_OEM", "'CHW000002'" );
      lFaults.put( "PN1_MANUFACT_CD", "'11111'" );
      lFaults.put( "PN1_IPC_REF_CD", "'CHW-PG1'" );
      lFaults.put( "PN1_POS_CD", "'COMHW-POS-1'" );
      lFaults.put( "PN1_INST_QT", "\'1\'" );
      lFaults.put( "PN1_RMVD_QT", "\'1\'" );
      lFaults.put( "FOUND_ON_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      setCurrentDate( iSN_ACFT );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify OPER-24492:A Faults can be created on uninstalled engine.
    *
    *
    *
    */
   @Test
   public void testOPER_24492_TC1_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "'APU_CD1'" );
      lFaults.put( "SERIAL_NO_OEM", "'SN000006-2'" );
      lFaults.put( "PART_NO_OEM", "'APU_ASSY_PN1'" );
      lFaults.put( "MANUFACT_CD", "'1234567890'" );
      lFaults.put( "ATA_SYS_CD", "'APU-SYS-1'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", null );
      lFaults.put( "HOURS_INTERVAL_QT", null );
      lFaults.put( "CYCLES_DUE_QT", null );
      lFaults.put( "CYCLES_INTERVAL_QT", null );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "DUE_DT", "sysdate+5" );
      lFaults.put( "FOUND_ON_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );
   }


   /**
    * This test is to verify OPER-24492:A Faults can be created on installed engine with installed
    * APU
    *
    *
    *
    */
   @Test
   public void testOPER_24492_TC2_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      lFaults.put( "ASSMBL_CD", "'APU_CD1'" );
      lFaults.put( "SERIAL_NO_OEM", "'SN000006'" );
      lFaults.put( "PART_NO_OEM", "'APU_ASSY_PN1'" );
      lFaults.put( "MANUFACT_CD", "'1234567890'" );
      // lFaults.put( "ATA_SYS_CD", "'APU-SYS-1'" );
      lFaults.put( "FAULT_SDESC", "\'" + iFAULT_SDESC + "\'" );
      lFaults.put( "FAULT_LDESC", "\'" + iFAULT_LDESC + "\'" );
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", null );
      lFaults.put( "HOURS_INTERVAL_QT", null );
      lFaults.put( "CYCLES_DUE_QT", null );
      lFaults.put( "CYCLES_INTERVAL_QT", null );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );
      lFaults.put( "DUE_DT", "sysdate+5" );
      lFaults.put( "FOUND_ON_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );
   }


   /**
    * This test is to verify AL_OPEN_DEFERRED_FAULT_IMPORT validation functionality of staging table
    * AL_OPEN_DEFERRED_FAULT_IMPORT. TRK part on ACFT without ATA_SYS_CD and it is non-mel
    *
    *
    */
   @Test
   public void testACFT_NONMEL_VALIDATION() {

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
      lFaults.put( "FAIL_SEV_CD", "'MINOR'" );
      // lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD + "\'" );
      // lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      // lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      // lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      // lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      // lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      // lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      // lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
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
    * This test is to verify AL_OPEN_DEFERRED_FAULT_IMPORT validation functionality of staging table
    * AL_OPEN_DEFERRED_FAULT_IMPORT. TRK part on ACFT. several cds are in lower case
    *
    *
    */
   @Test
   public void testACFTSYSTRK_LOWERCASE_VALIDATION() {

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
      lFaults.put( "FAIL_SEV_CD", "'mel'" );
      lFaults.put( "FAIL_DEFER_CD", "'mel a'" );
      lFaults.put( "FAULT_SOURCE_CD", "'mech'" );
      lFaults.put( "TASK_PRIORITY_CD", "'low'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'tech'" );
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
      ltasklist.add( "Unassigned from" );
      ltasklist.add( "new HOURS(HOUR) deadline" );
      ltasklist.add( "new CDY(DAY) deadline" );
      ltasklist.add( "The Task was Created" );
      ltasklist.add( "new CYCLES(CYCLES) deadline" );
      ltasklist.add( "The task has been deferred" );

      verifyEVTSTAGE( iTSIDs1, ltasklist );
      verifyEVTSTAGE( iTSIDs2, ltasklist );

      ArrayList<String> lfaultlist = new ArrayList<String>();
      lfaultlist.add( "Corrective action was deferred" );
      lfaultlist.add( "Corrective action was unassigned" );
      lfaultlist.add( "Fault was Created" );

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
      verifySCHEDPART_MultipleParts( iTSIDs1, linfors, iACTV, "1", "REQ" );
      verifySCHEDPART_MultipleParts( iTSIDs2, linfors, iACTV, "1", "REQ" );

      // Verify SCHED_INST_PART
      verifySCHEDINSTPART_MultipleParts( iTSIDs1, linfors, "1", 1 );
      verifySCHEDINSTPART_MultipleParts( iTSIDs2, linfors, "1", 1 );

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
    * This function is to verify C_OPEN_DEFERRED_FAULT staging table is loaded
    *
    *
    */

   public void verifyStagingTable() {

      String lQuery = "select 1 from " + TableUtil.AL_OPEN_DEFERRED_FAULT + " where SERIAL_NO_OEM='"
            + iSN_ACFT + "' and LOGBOOK_REFERENCE='" + iLOGBOOK_REFERENCE + "'";
      Assert.assertTrue( "Check C_OPEN_DEFERRED_FAULT table to verify the record exists",
            RecordsExist( lQuery ) );

      String lQuery2 =
            "select 1 from " + TableUtil.AL_OPEN_DEFERRED_FAULT + " where SERIAL_NO_OEM='"
                  + iSN_ACFT2 + "' and LOGBOOK_REFERENCE='" + iLOGBOOK_REFERENCE2 + "'";
      Assert.assertTrue( "Check C_OPEN_DEFERRED_FAULT table to verify the record exists",
            RecordsExist( lQuery2 ) );

      int lCount = countRowsInEntireTable( TableUtil.AL_OPEN_DEFERRED_FAULT );
      Assert.assertTrue( "There should be 2 records in C_OPEN_DEFERRED_FAULT table. ",
            lCount == 2 );

   }


   /**
    * This function is to verify SCHED_RMVD_PART table details part
    *
    *
    */
   public void verifySCHEDRMVDPART( simpleIDs aIDs, asmInfor aInfor, String aRMReasonCD ) {

      // SCHED_RMVD_PART table
      String[] iIds = { "PART_NO_DB_ID", "PART_NO_ID", "REMOVE_REASON_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "SCHED_DB_ID", aIDs.getNO_DB_ID() );
      lArgs.addArguments( "SCHED_ID", aIDs.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.SCHED_RMVD_PART, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "PART_NO_DB_ID",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aInfor.getlPNIDs().getNO_DB_ID() ) );
      Assert.assertTrue( "PART_NO_ID",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( aInfor.getlPNIDs().getNO_ID() ) );
      Assert.assertTrue( "REMOVE_REASON_CD",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aRMReasonCD ) );

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
         Assert.assertTrue( "PART_NO_DB_ID",
               llists.get( i ).get( 0 ).equalsIgnoreCase( aInfor.getlPNIDs().getNO_DB_ID() ) );
         Assert.assertTrue( "PART_NO_ID",
               llists.get( i ).get( 1 ).equalsIgnoreCase( aInfor.getlPNIDs().getNO_ID() ) );
         Assert.assertTrue( "INST_QT", llists.get( i ).get( 2 ).equalsIgnoreCase( aINSTQT ) );
      }

   }


   /**
    * This function is to verify SCHED_INST_PART table
    *
    *
    */
   public void verifySCHEDINSTPART_MultipleParts( simpleIDs aIDs, asmInfor aInfor, String aINSTQT,
         int aNumRecord ) {

      // SCHED_INST_PART
      String[] iIds = { "INST_QT" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "SCHED_DB_ID", aIDs.getNO_DB_ID() );
      lArgs.addArguments( "SCHED_ID", aIDs.getNO_ID() );
      lArgs.addArguments( "PART_NO_DB_ID", aInfor.getlPNIDs().getNO_DB_ID() );
      lArgs.addArguments( "PART_NO_ID", aInfor.getlPNIDs().getNO_ID() );

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

      Assert.assertTrue( "NH_ASSMBL_DB_ID",
            llists.get( 0 ).get( 5 ).equalsIgnoreCase( aInfor.getlNHASMBL().getASSMBL_DB_ID() ) );
      Assert.assertTrue( "NH_ASSMBL_CD",
            llists.get( 0 ).get( 6 ).equalsIgnoreCase( aInfor.getlNHASMBL().getASSMBL_CD() ) );

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
   public void verifySCHEDPART_MultipleParts( simpleIDs aIDs, asmInfor aInfor, String aSTATUSCD,
         String aSCHEDQT, String aREQACTCD ) {

      // SCHED_PART
      String[] iIds = { "SCHED_PART_STATUS_CD", "SPEC_PART_NO_DB_ID", "SPEC_PART_NO_ID",
            "NH_ASSMBL_DB_ID", "NH_ASSMBL_CD", "NH_ASSMBL_BOM_ID", "NH_ASSMBL_POS_ID", "SCHED_QT",
            "REQ_ACTION_CD" };
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
      Assert.assertTrue( "SPEC_PART_NO_DB_ID",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( aInfor.getlPNIDs().getNO_DB_ID() ) );
      Assert.assertTrue( "SPEC_PART_NO_ID",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aInfor.getlPNIDs().getNO_ID() ) );
      Assert.assertTrue( "NH_ASSMBL_DB_ID",
            llists.get( 0 ).get( 3 ).equalsIgnoreCase( aInfor.getlNHASMBL().getASSMBL_DB_ID() ) );
      Assert.assertTrue( "NH_ASSMBL_CD",
            llists.get( 0 ).get( 4 ).equalsIgnoreCase( aInfor.getlNHASMBL().getASSMBL_CD() ) );
      Assert.assertTrue( "NH_ASSMBL_BOM_ID",
            llists.get( 0 ).get( 5 ).equalsIgnoreCase( aInfor.getlNHASMBL().getASSMBL_BOM_ID() ) );
      Assert.assertTrue( "NH_ASSMBL_BOM_POS_ID",
            llists.get( 0 ).get( 6 ).equalsIgnoreCase( aInfor.getlNHASMBL().getASSMBL_POS_ID() ) );
      Assert.assertTrue( "SCHED_QT", llists.get( 0 ).get( 7 ).equalsIgnoreCase( aSCHEDQT ) );
      Assert.assertTrue( "REQ_ACTION_CD", llists.get( 0 ).get( 8 ).equalsIgnoreCase( aREQACTCD ) );

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
    * This function is to verify SCHED_LABOUR_ROLE_STATUS table details part
    *
    *
    */
   public void verifySCHEDLABOURROLESTATUS( simpleIDs aIDs, String aStatusCD ) {

      // SCHED_LABOUR_ROLE_STATUS table
      String[] iIds = { "LABOUR_ROLE_STATUS_CD" };
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

      // SCHED_LABOUR table
      String[] iIds = { "LABOUR_DB_ID", "LABOUR_ID", "LABOUR_STAGE_CD", "LABOUR_SKILL_CD" };
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
    * This function is to verify EVT_SCHED_DEAD table with start data/qt, interval day/qt
    *
    *
    */
   public void verifyEVTSCHEDDEAD_DATE_QT( simpleIDs aEvtIDs, simpleIDs aTypeIds,
         java.sql.Date aSCHED_DEAD_DT, java.sql.Date aSTART_DT, String aSTART_QT,
         String aINTERVAL_QT ) {
      // EVT_SCHED_DEAD table
      String[] iIds = { "START_QT", "INTERVAL_QT" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "EVENT_DB_ID", aEvtIDs.getNO_DB_ID() );
      lArgs.addArguments( "EVENT_ID", aEvtIDs.getNO_ID() );
      lArgs.addArguments( "DATA_TYPE_DB_ID", aTypeIds.getNO_DB_ID() );
      lArgs.addArguments( "DATA_TYPE_ID", aTypeIds.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EVT_SCHED_DEAD, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      if ( aSTART_QT != null ) {
         Assert.assertTrue( "START_QT", llists.get( 0 ).get( 0 ).equalsIgnoreCase( aSTART_QT ) );
      }
      if ( aINTERVAL_QT != null ) {
         Assert.assertTrue( "INTERVAL_QT",
               llists.get( 0 ).get( 1 ).equalsIgnoreCase( aINTERVAL_QT ) );

      }

      String[] iDates = { "SCHED_DEAD_DT", "START_DT" };
      lfields = new ArrayList<String>( Arrays.asList( iDates ) );

      // Parameters required by the query.
      lArgs = new WhereClause();
      lArgs.addArguments( "EVENT_DB_ID", aEvtIDs.getNO_DB_ID() );
      lArgs.addArguments( "EVENT_ID", aEvtIDs.getNO_ID() );
      lArgs.addArguments( "DATA_TYPE_DB_ID", aTypeIds.getNO_DB_ID() );
      lArgs.addArguments( "DATA_TYPE_ID", aTypeIds.getNO_ID() );

      iQueryString = TableUtil.buildTableQuery( TableUtil.EVT_SCHED_DEAD, lfields, lArgs );
      List<ArrayList<java.sql.Date>> lnlists = execute_date( iQueryString, lfields );

      if ( aSCHED_DEAD_DT != null ) {
         Assert.assertTrue( "SCHED_DEAD_DT",
               DateDiffInDays( lnlists.get( 0 ).get( 0 ), aSCHED_DEAD_DT ) == 0L );
      }
      if ( aSTART_DT != null ) {
         Assert.assertTrue( "START_DT",
               DateDiffInDays( lnlists.get( 0 ).get( 1 ), aSTART_DT ) == 0L );

      }

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
      Assert.assertTrue( "EVENT_LDESC", llists.get( 0 ).get( 3 ).contains( aLDESC ) );
      if ( aDOCRefSDESC != null ) {
         Assert.assertTrue( "DOC_REF_SDESC",
               llists.get( 0 ).get( 4 ).equalsIgnoreCase( aDOCRefSDESC ) );
      }

      return lIds;

   }


   /**
    * This function is to set current date on DUE_DT column of c_open_deferred_fault
    *
    *
    *
    */

   public void setCurrentDate( String aSN ) {
      String aUpdateQuery =
            "UPDATE al_open_deferred_fault SET DUE_DT= (SELECT SYSDATE FROM DUAL) where SERIAL_NO_OEM='"
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

   public void dataSetupNull() {
      String aUpdateQuery =
            "update UTL_CONFIG_PARM set PARM_VALUE=null where PARM_NAME='ADHOC_CORRTASK_LABOUR'";

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

   public void dataSetupEMPTY() {
      String aUpdateQuery =
            "update UTL_CONFIG_PARM set PARM_VALUE='   ' where PARM_NAME='ADHOC_CORRTASK_LABOUR'";

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

   public void dataSetupAFT() {
      String aUpdateQuery =
            "update UTL_CONFIG_PARM set PARM_VALUE='AFT' where PARM_NAME='ADHOC_CORRTASK_LABOUR'";

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

   public void dataRestoreParmValue() {
      String aUpdateQuery = "update UTL_CONFIG_PARM set PARM_VALUE='" + iParmValue
            + "' where PARM_NAME='ADHOC_CORRTASK_LABOUR'";

      try {

         PreparedStatement lStatement = getConnection().prepareStatement( aUpdateQuery );
         lStatement.executeUpdate( aUpdateQuery );
         commit();

      } catch ( SQLException e ) {
         e.printStackTrace();
      }

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
    * This function is to set ALLOW_UNRELATED_PART_GROUP_IN_PART_REQUIREMENT=TRUE
    *
    *
    */

   public void dataSetupUNRELATED_PART_GROUP() {
      String aUpdateQuery =
            "update UTL_CONFIG_PARM set PARM_VALUE='TRUE' where PARM_NAME='ALLOW_UNRELATED_PART_GROUP_IN_PART_REQUIREMENT'";

      runUpdate( aUpdateQuery );

      String aquery =
            "select PARM_VALUE from UTL_CONFIG_PARM where PARM_NAME='ALLOW_UNRELATED_PART_GROUP_IN_PART_REQUIREMENT'";
      String lstr = getStringValueFromQuery( aquery, "PARM_VALUE" );
      Assert.assertTrue( lstr.equalsIgnoreCase( "TRUE" ) );

   }


   /**
    * This function is to restore ALLOW_UNRELATED_PART_GROUP_IN_PART_REQUIREMENT=False
    *
    *
    */

   public void dataRestoreUNRELATED_PART_GROUP() {
      String aUpdateQuery =
            "update UTL_CONFIG_PARM set PARM_VALUE='FALSE' where PARM_NAME='ALLOW_UNRELATED_PART_GROUP_IN_PART_REQUIREMENT'";

      try {

         PreparedStatement lStatement = getConnection().prepareStatement( aUpdateQuery );
         lStatement.executeUpdate( aUpdateQuery );
         commit();

      } catch ( SQLException e ) {
         e.printStackTrace();
      }

   }


   /**
    * This function is to setup duplicate ACFT
    *
    *
    */
   public void dataSetupACFTDup() {
      String lquery =
            "update inv_inv set serial_no_oem ='sn000013' where inv_no_sdesc='Aircraft Part 1 - LOCK'";
      runUpdate( lquery );
   }


   /**
    * This function is to restore duplicate ACFT
    *
    *
    */
   public void dataRestoreACFTDup() {
      String lquery =
            "update inv_inv set serial_no_oem ='SN-LOCKED' where inv_no_sdesc='Aircraft Part 1 - LOCK'";
      runUpdate( lquery );
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

      aUpdateQuery = "delete from " + TableUtil.EQP_ASSMBL_POS
            + " where assmbl_cd='ACFT_CD1' and assmbl_bom_id=2 and assmbl_pos_id=2";
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
               String msg = lPrepareCallInventory.getString( 3 );
               Assert.assertTrue( "ODF Validtion return msg: [" + msg + "]", true );

            } catch ( SQLException e ) {
               e.printStackTrace();
               Assert.assertTrue( "ODF Validtion failed", false );
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
