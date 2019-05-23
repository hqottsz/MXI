package com.mxi.mx.core.maint.plan.actualsloader.ODF;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.mxi.mx.core.maint.plan.datamodels.invIDs;
import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;
import com.mxi.mx.util.StringUtils;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.TestConstants;


/**
 * This files contains extended tests of ODF based on JIRA tickets
 *
 */
public class FaultsAL_OPER extends Faults {

   @Rule
   public TestName testName = new TestName();

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
   public String iFAIL_SEV_CD_CDL_WND = "CDL-WND";
   public String iFAIL_DEFER_CD = "MEL A";
   public String iFAIL_DEFER_CD_AUTO = "AUTO";
   public String iFAULT_SOURCE_CD = "MECH";
   public String iTASK_PRIORITY_CD = "LOW";
   public String iLOGBOOK_REFERENCE = "logtest";
   public String iDEFER_REF_SDESC = "AUTOMINOR";
   public String iOP_RESTRICTION_LDESC = "AUTORESTRICTIONLDESC";
   public String iDEFER_CD_SDESC = "AUTOCDMINOR";
   public String iFAULT_LOG_TYPE_CD = "TECH";
   public String iFRM_CD = "AUTOFRMCD";


   /**
    * Setup before executing each individual test
    *
    */
   @Before
   @Override
   public void before() throws Exception {

      super.before();
      clearActualsLoaderTables();

   }


   /**
    * Clean up after each individual test
    *
    * @throws Exception
    */
   @After
   @Override
   public void after() throws Exception {
      RestoreData();
      super.after();

   }


   /**
    * This test is to verify OPER-24629: Actuals Loader Open Deferred Faults does not support the
    * new deferral reference functionality. when FAIL_SEV_CD = "MEL", and due_dt, hour date and
    * cycle data are provided, the deadline are calculated based on provided information;
    *
    *
    */
   public void testOPER_24629_1_VALIDATION() {

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

      lFaults.put( "DUE_DT", "SYSDATE+1" );
      lFaults.put( "FOUND_ON_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify OPER-24629: Actuals Loader Open Deferred Faults does not support the
    * new deferral reference functionality. when FAIL_SEV_CD = "MEL", and due_dt, hour date and
    * cycle data are provided, the deadline are calculated based on provided information;
    *
    *
    */

   @Test
   public void testOPER_24629_1_IMPORT() throws ParseException {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testOPER_24629_1_VALIDATION();

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

      // Verify hours sched deadline --note rate is always 1, it is setup in fc_model and fc_rate
      // table.
      int lCurrentUsage = getCurrentUsage( iSN_ACFT, lHoursIds );
      int lDriver1 = verifyEVTSCHEDDEAD( iTSIDs1, lHoursIds, "1", String.valueOf( lCurrentUsage ),
            "1", TestConstants.SCHED_FROM_CD_CUSTOM, String.valueOf( 1 - 2 ), "2" );

      // verify cycles sched deadline --note rate is always 1, it is setup in fc_model and fc_rate
      lCurrentUsage = getCurrentUsage( iSN_ACFT, lCycIds );
      int lDriver2 = verifyEVTSCHEDDEAD( iTSIDs1, lCycIds, "3", String.valueOf( lCurrentUsage ),
            "1", TestConstants.SCHED_FROM_CD_CUSTOM, String.valueOf( 3 - 4 ), "4" );

      // Verify days sched deadline
      verifyEVTSCHEDDEAD_DAY( iTSIDs1, lDayIds, DateAddDays( new Date(), 1 ), "0",
            DateAddDays( new Date(), 0 ) );

      // verify sched driver bool is 1 for earliest deadline
      Assert.assertTrue( "Driver bool should be 1.", lDriver1 == 1 || lDriver2 == 1 );

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

   }


   /**
    * This test is to verify OPER-24629: Actuals Loader Open Deferred Faults does not support the
    * new deferral reference functionality. when FAIL_SEV_CD = "MEL", and due_dt is provided, hour
    * date and cycle data are not provided, the deadline are calculated based on provided
    * information for due_date, but no records for hour and cycle;
    *
    *
    */
   public void testOPER_24629_2_VALIDATION() {

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
      // lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      // lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      // lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );

      lFaults.put( "DUE_DT", "SYSDATE+1" );
      lFaults.put( "FOUND_ON_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify OPER-24629: Actuals Loader Open Deferred Faults does not support the
    * new deferral reference functionality. when FAIL_SEV_CD = "MEL", and due_dt is provided, hour
    * date and cycle data are not provided, the deadline are calculated based on provided
    * information for due_date, but no records for hour and cycle;
    *
    *
    */

   @Test
   public void testOPER_24629_2_IMPORT() throws ParseException {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testOPER_24629_2_VALIDATION();

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
      // ltasklist.add( "NEW HOURS(HOUR) DEADLINE" );
      ltasklist.add( "NEW CDY(DAY) DEADLINE" );
      ltasklist.add( "THE TASK WAS CREATED" );
      // ltasklist.add( "NEW CYCLES(CYCLES) DEADLINE" );
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

      verifyEVTSCHEDDEAD_NOTEXIST( iTSIDs1, lHoursIds );
      verifyEVTSCHEDDEAD_NOTEXIST( iTSIDs1, lCycIds );

      // // Verify hours sched deadline --note rate is always 1, it is setup in fc_model and fc_rate
      // // table.
      // int lCurrentUsage = getCurrentUsage( iSN_ACFT, lHoursIds );
      // int lDriver1 = verifyEVTSCHEDDEAD( iTSIDs1, lHoursIds, "1", String.valueOf( lCurrentUsage
      // ),
      // "1", TestConstants.SCHED_FROM_CD_CUSTOM, String.valueOf( 1 - 2 ), "2" );
      //
      // // verify cycles sched deadline --note rate is always 1, it is setup in fc_model and
      // fc_rate
      // lCurrentUsage = getCurrentUsage( iSN_ACFT, lCycIds );
      // int lDriver2 = verifyEVTSCHEDDEAD( iTSIDs1, lCycIds, "3", String.valueOf( lCurrentUsage ),
      // "1", TestConstants.SCHED_FROM_CD_CUSTOM, String.valueOf( 3 - 4 ), "4" );

      // Verify days sched deadline
      verifyEVTSCHEDDEAD_DAY( iTSIDs1, lDayIds, DateAddDays( new Date(), 1 ), "1",
            DateAddDays( new Date(), 0 ) );

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

   }


   /**
    * This test is to verify OPER-24629: Actuals Loader Open Deferred Faults does not support the
    * new deferral reference functionality. when FAIL_SEV_CD = non "MEL", and due_dt, hour date and
    * cycle data are provided, the deadline are calculated based on provided information;
    *
    *
    */

   public void testOPER_24629_3_VALIDATION() {

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
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD_CDL_WND + "\'" );
      // lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD_AUTO + "\'" );
      lFaults.put( "FAULT_SOURCE_CD", "\'" + iFAULT_SOURCE_CD + "\'" );
      lFaults.put( "TASK_PRIORITY_CD", "\'" + iTASK_PRIORITY_CD + "\'" );
      lFaults.put( "HOURS_DUE_QT", "\'1\'" );
      lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      // lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      // lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );

      lFaults.put( "DUE_DT", "SYSDATE+1" );
      lFaults.put( "FOUND_ON_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify OPER-24629: Actuals Loader Open Deferred Faults does not support the
    * new deferral reference functionality. when FAIL_SEV_CD = non "MEL", and due_dt, hour date and
    * cycle data are provided, the deadline are calculated based on provided information;
    *
    *
    */

   @Test
   public void testOPER_24629_3_IMPORT() throws ParseException {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testOPER_24629_3_VALIDATION();

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
      // ltasklist.add( "NEW HOURS(HOUR) DEADLINE" );
      // ltasklist.add( "NEW CDY(DAY) DEADLINE" );
      ltasklist.add( "THE TASK WAS CREATED" );
      // ltasklist.add( "NEW CYCLES(CYCLES) DEADLINE" );
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

      // Verify hours sched deadline --note rate is always 1, it is setup in fc_model and fc_rate
      // table.
      int lCurrentUsage = getCurrentUsage( iSN_ACFT, lHoursIds );
      int lDriver1 = verifyEVTSCHEDDEAD( iTSIDs1, lHoursIds, "1", String.valueOf( lCurrentUsage ),
            "1", TestConstants.SCHED_FROM_CD_CUSTOM, String.valueOf( 1 - 2 ), "2" );

      // verify cycles sched deadline --note rate is always 1, it is setup in fc_model and fc_rate
      lCurrentUsage = getCurrentUsage( iSN_ACFT, lCycIds );
      int lDriver2 = verifyEVTSCHEDDEAD( iTSIDs1, lCycIds, "3", String.valueOf( lCurrentUsage ),
            "1", TestConstants.SCHED_FROM_CD_CUSTOM, String.valueOf( 3 - 4 ), "4" );

      // Verify days sched deadline
      verifyEVTSCHEDDEAD_DAY( iTSIDs1, lDayIds, DateAddDays( new Date(), 1 ), "0",
            DateAddDays( new Date(), 0 ) );

      // Verify SCHED_STASK table
      simpleIDs lPriorityIds = getPriorityIDs( iTASK_PRIORITY_CD );
      simpleIDs lTaskCDs = getTaskCDs( "CORR" );
      verifySCHEDSTASK( iTSIDs1, iCFIDs1, lPriorityIds, lTaskCDs, lINVIDSTASK.getINVIDs() );

      // verify sched driver bool is 1 for earliest deadline
      Assert.assertTrue( "Driver bool should be 1.", lDriver1 == 1 || lDriver2 == 1 );

      // Verify SD_FAULT
      verifySDFAULT( iCFIDs1, null, iFAULT_SOURCE_CD, iFAIL_SEV_CD_CDL_WND, null,
            iOP_RESTRICTION_LDESC, null, iFAULT_LOG_TYPE_CD, iFRM_CD );

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

   }


   /**
    * This test is to verify OPER-24629: Actuals Loader Open Deferred Faults does not support the
    * new deferral reference functionality. when FAIL_SEV_CD = non "MEL", and due_dt, hour date and
    * cycle data are not provided, the deadline are not calculated;
    *
    *
    */

   public void testOPER_24629_4_VALIDATION() {

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
      lFaults.put( "FAIL_SEV_CD", "\'" + iFAIL_SEV_CD_CDL_WND + "\'" );
      // lFaults.put( "FAIL_DEFER_CD", "\'" + iFAIL_DEFER_CD_AUTO + "\'" );
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

      // lFaults.put( "DUE_DT", "SYSDATE" );
      lFaults.put( "FOUND_ON_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify OPER-24629: Actuals Loader Open Deferred Faults does not support the
    * new deferral reference functionality. when FAIL_SEV_CD = non "MEL", and due_dt, hour date and
    * cycle data are not provided, the deadline are not calculated;
    *
    *
    */

   @Test
   public void testOPER_24629_4_IMPORT() throws ParseException {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testOPER_24629_4_VALIDATION();

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
      // ltasklist.add( "NEW HOURS(HOUR) DEADLINE" );
      // ltasklist.add( "NEW CDY(DAY) DEADLINE" );
      ltasklist.add( "THE TASK WAS CREATED" );
      // ltasklist.add( "NEW CYCLES(CYCLES) DEADLINE" );
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

      verifyEVTSCHEDDEAD_NOTEXIST( iTSIDs1, lHoursIds );
      verifyEVTSCHEDDEAD_NOTEXIST( iTSIDs1, lCycIds );
      verifyEVTSCHEDDEAD_NOTEXIST( iTSIDs1, lDayIds );

      // Verify SCHED_STASK table
      simpleIDs lPriorityIds = getPriorityIDs( iTASK_PRIORITY_CD );
      simpleIDs lTaskCDs = getTaskCDs( "CORR" );
      verifySCHEDSTASK( iTSIDs1, iCFIDs1, lPriorityIds, lTaskCDs, lINVIDSTASK.getINVIDs() );

      // Verify SD_FAULT
      verifySDFAULT( iCFIDs1, null, iFAULT_SOURCE_CD, iFAIL_SEV_CD_CDL_WND, null,
            iOP_RESTRICTION_LDESC, null, iFAULT_LOG_TYPE_CD, iFRM_CD );

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

   }


   /**
    * This test is to verify OPER-24629: Actuals Loader Open Deferred Faults does not support the
    * new deferral reference functionality. when FAIL_SEV_CD = "MEL", and due_dt, hour date and
    * cycle data are provided, the deadline are calculated based on provided information; Fault is
    * on ENG.
    *
    */
   @Test
   public void testOPER_24629_5_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lFaults = new LinkedHashMap<>();

      // lFaults.put( "ASSMBL_CD", "'ENG_CD1'" );
      lFaults.put( "SERIAL_NO_OEM", "'SN000007'" );
      lFaults.put( "PART_NO_OEM", "'ENG_ASSY_PN1'" );
      lFaults.put( "MANUFACT_CD", "'ABC11'" );
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

      lFaults.put( "DUE_DT", "SYSDATE+1" );
      lFaults.put( "FOUND_ON_DT", "SYSDATE" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify OPER-24629: Actuals Loader Open Deferred Faults does not support the
    * new deferral reference functionality. when FAIL_SEV_CD = "MEL", and due_dt, hour date and
    * cycle data are provided, the deadline are calculated based on provided information; Fault is
    * on ENG.
    *
    */

   @Test
   public void testOPER_24629_5_IMPORT() throws ParseException {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testOPER_24629_5_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify EVT_EVENT table
      iCFIDs1 = verifyEVTEVENT( iEventTypeCdFault, iFAULT_SDESC, "CFDEFER", iFAULT_LDESC,
            iLOGBOOK_REFERENCE );

      iTSIDs1 = verifyEVTEVENT( iEventTypeCdTask, iFAULT_SDESC, "ACTV", iFAULT_LDESC, null );

      // Verify EVT_INV table
      invIDs lINVIDSFAULT = verifyEVTINV_ENG( iCFIDs1, iAssmbl_ACFT_CD );
      invIDs lINVIDSTASK = verifyEVTINV_ENG( iTSIDs1, iAssmbl_ACFT_CD );

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

      // Verify hours sched deadline --note rate is always 1, it is setup in fc_model and fc_rate
      // table.
      int lCurrentUsage = getCurrentUsage( iSN_ACFT, lHoursIds );
      int lDriver1 = verifyEVTSCHEDDEAD( iTSIDs1, lHoursIds, "1", String.valueOf( lCurrentUsage ),
            "1", TestConstants.SCHED_FROM_CD_CUSTOM, String.valueOf( 1 - 2 ), "2" );

      // verify cycles sched deadline --note rate is always 1, it is setup in fc_model and fc_rate
      lCurrentUsage = getCurrentUsage( iSN_ACFT, lCycIds );
      int lDriver2 = verifyEVTSCHEDDEAD( iTSIDs1, lCycIds, "3", String.valueOf( lCurrentUsage ),
            "1", TestConstants.SCHED_FROM_CD_CUSTOM, String.valueOf( 3 - 4 ), "4" );

      // Verify days sched deadline
      verifyEVTSCHEDDEAD_DAY( iTSIDs1, lDayIds, DateAddDays( new Date(), 1 ), "0",
            DateAddDays( new Date(), 0 ) );

      // verify sched driver bool is 1 for earliest deadline
      Assert.assertTrue( "Driver bool should be 1.", lDriver1 == 1 || lDriver2 == 1 );

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

   }


   /**
    * This test is to verify OPER-24629: Actuals Loader Open Deferred Faults does not support the
    * new deferral reference functionality. when FAIL_SEV_CD = "MEL", due_dt, hour date and cycle
    * data are not provided, DAY/calendar date is based on ref_fail_defer setup.
    *
    * This is would not pass without new feature has been implemented.
    *
    */
   @Test
   public void testOPER_24629_6_VALIDATION() {

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
      // lFaults.put( "HOURS_INTERVAL_QT", "\'2\'" );
      // lFaults.put( "CYCLES_DUE_QT", "\'3\'" );
      // lFaults.put( "CYCLES_INTERVAL_QT", "\'4\'" );
      lFaults.put( "LOGBOOK_REFERENCE", "\'" + iLOGBOOK_REFERENCE + "\'" );
      lFaults.put( "DEFER_REF_SDESC", "\'" + iDEFER_REF_SDESC + "\'" );
      lFaults.put( "OP_RESTRICTION_LDESC", "\'" + iOP_RESTRICTION_LDESC + "\'" );
      lFaults.put( "DEFER_CD_SDESC", "\'" + iDEFER_CD_SDESC + "\'" );
      lFaults.put( "FAULT_LOG_TYPE_CD", "\'" + iFAULT_LOG_TYPE_CD + "\'" );
      lFaults.put( "FRM_CD", "\'" + iFRM_CD + "\'" );

      // lFaults.put( "DUE_DT", "SYSDATE+1" );
      lFaults.put( "FOUND_ON_DT", "SYSDATE-1" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_OPEN_DEFERRED_FAULT, lFaults ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify OPER-24629: Actuals Loader Open Deferred Faults does not support the
    * new deferral reference functionality. when FAIL_SEV_CD = "MEL", due_dt is not provided, hour
    * date and cycle data are provided, the hours and cycles deadline are calculated based on
    * provided information; calendar date is based on ref_fail_defer setup. This is would not pass
    * without new feature has been implemented.
    *
    */

   @Test
   public void testOPER_24629_6_IMPORT() throws ParseException {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testOPER_24629_6_VALIDATION();

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
      ltasklist.add( "NEW CDY(DAY) DEADLINE" );
      ltasklist.add( "THE TASK WAS CREATED" );
      ltasklist.add( "THE TASK HAS BEEN DEFERRED" );

      verifyEVTSTAGE( iTSIDs1, ltasklist );

      ArrayList<String> lfaultlist = new ArrayList<String>();
      lfaultlist.add( "CORRECTIVE ACTION WAS DEFERRED" );
      lfaultlist.add( "CORRECTIVE ACTION WAS UNASSIGNED" );
      lfaultlist.add( "FAULT WAS CREATED" );

      verifyEVTSTAGE( iCFIDs1, lfaultlist );

      // Verify EVT_SCHED_DEAD
      simpleIDs lDayIds = getMIMType( "CDY" );

      // Verify hours sched deadline --note rate is always 1, it is setup in fc_model and fc_rate
      // table.

      // Verify days sched deadline
      int dayToAdd = getDaysToAdd( iFAIL_DEFER_CD, "High Priority MEL Deviation" );
      verifyEVTSCHEDDEAD_DAY( iTSIDs1, lDayIds, DateAddDays( new Date(), dayToAdd - 1 ), "1",
            DateAddDays( new Date(), -1 ) );

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

   }


   // ====================
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

   }

}
