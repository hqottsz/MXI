package com.mxi.mx.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;

import com.mxi.mx.core.maint.plan.datamodels.assmbleInfor;
import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;


/**
 * This test suite contains common test utilities for Requirement and component requirement tests
 * and data setup for each test cases.
 *
 * @author Alicia Qian
 */
public class ReqAndCompReqTest extends BaselineLoaderTest {

   public String iACFT_ASSMBLCD = "ACFT_CD1";
   public String iREQ_ATA_CD_TRK = "ACFT-SYS-1-1-TRK-BATCH-PARENT";
   public String iREQ_ATA_CD_Interchangable = "ACFT-SYS-1-1-TRK-INTERCHANGABILITY";
   public String iREQ_TASK_CD_1 = "ATEST";
   public String iREQ_TASK_CD_2 = "BTEST";
   public String iREQ_TASK_CD_3 = "CTEST";

   // C_REQ table, C_COMP_REQ
   public String iREQ_TASK_NAME_1 = "ATESTNAME";
   public String iREQ_TASK_NAME_2 = "BTESTNAME";
   public String iREQ_TASK_NAME_3 = "CTESTNAME";
   public String iREQ_TASK_DESC_1 = "ATESTDESC";
   public String iREQ_TASK_DESC_2 = "BTESTDESC";
   public String iREQ_TASK_DESC_3 = "CTESTDESC";
   public String iREQ_TASK_REF_SDESC_1 = "ATESTREFDESC";
   public String iREQ_TASK_REF_SDESC_2 = "BTESTREFDESC";
   public String iREQ_TASK_REF_SDESC_3 = "CTESTREFDESC";
   public String iCLASS_CD = "MOD";
   public String iCLASS_CD_2 = "REPL";
   public String iCLASS_CD_COMP = "REQ";
   public String iCLASS_CD_FOLLOW = "FOLLOW";
   public String iCLASS_CD_REPREF = "REPREF";
   public String iSUBCLASS_CD = "TEST1";
   public String iSUBCLASS_CD_COMP = "TEST2";
   public String iORIGINATOR_CD = "AWL";
   public String iAPPLICABILITY_DESC = "1,5-7";
   public String iRESCHED_FROM_CD = "EXECUTE";
   public String iLAST_DEADLINE_DRIVER_BOOL = "Y";
   public String iLAST_DEADLINE_DRIVER_BOOL_N = "1";
   public String iSOFT_DEADLINE_DRIVER_BOOL = "Y";
   public String iSOFT_DEADLINE_DRIVER_BOOL_N = "1";
   public String iSCHED_FROM_MANUFACT_DT_BOOL = "Y";
   public String iSCHED_FROM_MANUFACT_DT_BOOL_N = "1";
   public String iWORKSCOPE_BOOL = "Y";
   public String iWORKSCOPE_BOOL_N = "1";
   public String iWORKSCOPE_BOOL_2 = "N";
   public String iWORKSCOPE_BOOL_2_N = "0";
   public String iENFORCE_WORKSCOPE_ORD_BOOL = "Y";
   public String iENFORCE_WORKSCOPE_ORD_BOOL_N = "1";
   public String iREQ_INSTRUCTIONS = "testInstruction";
   public String iUSE_SCHED_FROM_BOOL = "Y";
   public String iUSE_SCHED_FROM_BOOL_DEFAULT_VALUE = "N";
   public String iCANCEL_ON_AC_INST_BOOL_N = "N";
   public String iCREATE_ON_AC_RMVL_BOOL_Y = "Y";
   public String iMOC_APPROVAL_BOOL_Y = "Y";
   public String iDAMAGE_RECORD_BOOL_Y = "Y";
   public String iDAMAGED_COMPONENT_N = "N";
   public String iDAMAGED_COMPONENT_Y = "Y";
   public String iREPREF_OPS_RESTRICTIONS_LDESC = "REPREF OPS Restrictions LDESC";
   public String iREPREF_PERF_PENALTIES_LDESC = "REPREF PERF Penalties LDESC";

   public String iETOPS_SIGNIFICANT_BOOL = "N";
   public String iETOPS_SIGNIFICANT_BOOL_N = "0";
   public String iSCHED_TO_LATEST_DEADLINE_BOOL = "Y";
   public String iSCHED_TO_LATEST_DEADLINE_BOOL_N = "1";
   public String iAPPLICABILITY_RANGE = "1,5-7";
   public String iSOFT_DEADLINE_BOOL = "Y";
   public String iSOFT_DEADLINE_BOOL_N = "1";
   public String iNEXT_SHOP_VISIT_BOOL = "N";
   public String iNEXT_SHOP_VISIT_BOOL_N = "0";
   public String iMIN_USAGE_RELEASE_PCT = "10";
   public String iMIN_FORECAST_RANGE_QT = "2";
   public String iEST_DURATION_HRS = "3";
   public String iON_CONDITION_BOOL = "N";
   public String iON_CONDITION_BOOL_N = "0";
   public String iON_CONDITION_BOOL_Y = "Y";
   public String iON_CONDITION_BOOL_Y_number = "1";
   public String iMUST_BE_REMOVED_CD = "OFFPARENT";
   public String iREVIEW_ON_RECEIPT_BOOL_N = "N";

   // C_COMP_REQ_ASSIGNED_PART, C_REQ_DYNAMIC_DEADLINE and C_REQ_DYNAMIC_PART_DEADLINE
   public String iPART_NO_OEM_SER = "T0000006";
   public String iMANUFACT_CD_SER = "ABC11";

   public String iPART_NO_OEM_SER_COMP = "A0000012";
   public String iMANUFACT_CD_SER_COMP = "1234567890";

   public String iPART_NO_OEM_TRK = "A0000010";
   public String iMANUFACT_CD_TRK = "11111";

   public String iPART_NO_OEM_TRK_COMP = "A0000010";
   public String iMANUFACT_CD_TRK_COMP = "11111";

   public String iPART_NO_OEM_TRK_old = "A0000018A";
   public String iMANUFACT_CD_TRK_old = "11111";
   public String iPART_NO_OEM_TRK_new = "A0000018B";
   public String iMANUFACT_CD_TRK_new = "11111";

   public String iPART_NO_OEM_BATCH_COMP = "CHW000002";
   public String iMANUFACT_CD_BATCH_COMP = "11111";

   // C_REQ_DYNAMIC_DEADLINE and C_REQ_DYNAMIC_PART_DEADLINE
   public String iSCHED_DATA_TYPE_CD = "USAGE1";
   public String iSCHED_INITIAL_QT = "10";
   public String iSCHED_INTERVAL_QT = "20";
   public String iSCHED_THRESHOLD_QT = "100";

   public String iSCHED_DATA_TYPE_CD_HRS = "HOURS";
   public String iSCHED_DATA_TYPE_CD_CYR = "CYR";
   public String iSCHED_DATA_TYPE_CD_CYCLES = "CYCLES";

   public String iTASK_DEP_ACTION_CD = "CRT";

   public simpleIDs iTASK_IDs_15067 = null;
   public simpleIDs iTASK_DEFN_IDs_15067 = null;
   public simpleIDs iTASK_IDs = null;
   public simpleIDs iTASK_DEFN_IDs = null;
   public simpleIDs iTASK_IDs_2 = null;
   public simpleIDs iTASK_DEFN_IDs_2 = null;
   public simpleIDs iTASK_IDs_REPL = null;
   public simpleIDs iTASK_DEFN_IDs_REPL = null;
   public simpleIDs iTASK_IDs_TRSFRM = null;
   public simpleIDs iTASK_DEFN_IDs_TRSFRM = null;

   public String iREQ_IETM_CD_1 = "TEST1";
   public String iREQ_IETM_CD_2 = "TEST2";
   public String iREQ_TOPIC_SDESC_1 = "AUTOTEST1-1";
   public String iREQ_TOPIC_SDESC_2 = "AUTOTEST1-2";

   public String iJIC_ATA_CD_TRK = "ACFT-SYS-1-1-TRK-BATCH-PARENT";
   public String iJIC_TASK_CD_TRK = "ACFT-SYS-1-1-TRK-BATCH-PARENT-AUTO-JIC";

   public String iADVSRY_TYPE_CD = "EXECUTE";
   public String iROLE_CD = "ENG";
   public String iADVSRY_NOTE = "Automation Test";

   public String iADVSRY_TYPE_CD_2 = "FLEET";

   public String iIMPACT_CD = "AUTOTEST";
   public String iIMPACT_DESC = "AUTOTEST";

   public String iWORK_TYPE_LIST_1 = "SERVICE";
   public String iWORK_TYPE_LIST_2 = "FUEL";

   public String iTask_MUST_REMOVE_CD_OFFWING = "OFFWING";
   public String iTask_MUST_REMOVE_CD_ONWING = "NA";
   public String iREQ_ATA_CD_ENG = "ENG-ASSY";
   public String iREQ_ATA_CD_APU = "APU-ASSY";
   public String iREQ_ATA_CD_SYS = "SYS-1-1";
   public String iREQ_ATA_CD_ACFT = "ACFT_CD1";
   public String iREQ_ATA_CD_ENG_ROOT = "ENG_CD1";
   public String iREQ_ATA_CD_APU_ROOT = "APU_CD1";

   public String iJIC_TASK_CD_1 = "ATTEST";
   public String iJIC_TASK_NAME_1 = "ATTASKNAME";
   public String iJIC_TASK_CD_2 = "BTTEST";
   public String iJIC_TASK_NAME_2 = "BTTASKNAME";

   // C_REQ_IETM_TOPIC
   public String iIETM_CD_1 = "TEST1";
   public String iIETM_CD_2 = "TEST2";
   public String iIETM_CD_3 = "TEST3";
   public String iIETM_CD_4 = "TEST4";
   public String iIETM_TOPIC_1 = "AUTOTEST1-1";
   public String iIETM_TOPIC_2 = "AUTOTEST1-2";
   public String iIETM_TOPIC_3 = "AUTOTEST1-3";
   public String iIETM_TOPIC_4 = "AUTOTEST1-4";
   public String iIETM_ORDER_1 = "1";
   public String iIETM_ORDER_2 = "2";
   public String iIETM_ORDER_3 = "60";
   public String iIETM_ORDER_4 = "25";

   // import data
   public simpleIDs iTask_IDs_1 = null;


   /**
    * This function is to validate error code exists.
    *
    *
    */
   public void validateErrorCode( String aCode ) {

      List<String> llist = getErrorCodeList();
      String lerror_desc = getErrorDetail( aCode );
      Assert.assertTrue( "Error code found " + aCode + ": " + lerror_desc,
            llist.contains( aCode ) );

   }


   /**
    * This function is to get detail of error code
    *
    *
    */

   public String getErrorDetail( String aErrorcode ) {

      String[] iIds = { "TECH_DESC" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();

      lArgs.addArguments( "RESULT_CD", aErrorcode );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.DL_REF_MESSAGE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      return llists.get( 0 ).get( 0 );

   }


   /**
    * This function is to retrieve errors code list
    *
    *
    */

   public List<String> getErrorCodeList() {
      String[] iIds = { "RESULT_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      String iQueryString = "select c_req.result_cd " + " from c_req "
            + " inner join DL_REF_MESSAGE on " + " DL_REF_MESSAGE.result_cd=c_req.result_cd "
            + " UNION ALL " + " select c_req_dynamic_deadline.result_cd "
            + " from c_req_dynamic_deadline inner join DL_REF_MESSAGE on "
            + " DL_REF_MESSAGE.result_cd=c_req_dynamic_deadline.result_cd " + " UNION ALL "
            + " select c_proc_req_deadline.result_cd "
            + " from c_proc_req_deadline inner join DL_REF_MESSAGE on "
            + " DL_REF_MESSAGE.result_cd=c_proc_req_deadline.result_cd " + " UNION ALL "
            + " select c_req_dynamic_part_deadline.result_cd "
            + " from c_req_dynamic_part_deadline inner join DL_REF_MESSAGE on "
            + " DL_REF_MESSAGE.result_cd=c_req_dynamic_part_deadline.result_cd " + " UNION ALL "
            + " select c_comp_req.result_cd " + " from c_comp_req inner join DL_REF_MESSAGE on "
            + " DL_REF_MESSAGE.result_cd=c_comp_req.result_cd " + " UNION ALL "
            + " select c_proc_comp_req_deadline.result_cd "
            + " from c_proc_comp_req_deadline inner join DL_REF_MESSAGE on "
            + " DL_REF_MESSAGE.result_cd=c_proc_comp_req_deadline.result_cd" + " UNION ALL "
            + " select c_comp_req_dynamic_deadline.result_cd "
            + " from c_comp_req_dynamic_deadline inner join DL_REF_MESSAGE on "
            + " DL_REF_MESSAGE.result_cd=c_comp_req_dynamic_deadline.result_cd " + " UNION ALL "
            + " select c_comp_req_dyn_part_deadline.result_cd "
            + " from c_comp_req_dyn_part_deadline inner join DL_REF_MESSAGE on "
            + " DL_REF_MESSAGE.result_cd=c_comp_req_dyn_part_deadline.result_cd ";
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      List<String> llistM = new ArrayList<String>();
      for ( int i = 0; i < llists.size(); i++ ) {
         llistM.add( llists.get( i ).get( 0 ) );
      }

      return llistM;

   }


   /**
    * This function is to retrieve part no ids.
    *
    *
    */

   public simpleIDs getPartIds( String aPART_NO_OEM, String aMANUFACT_CD ) {

      String[] iIds = { "PART_NO_DB_ID", "PART_NO_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "PART_NO_OEM", aPART_NO_OEM );
      lArgs.addArguments( "MANUFACT_CD", aMANUFACT_CD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EQP_PART_NO, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );
      return lIds;

   }


   /**
    * This function is to retrieve ietm ids.
    *
    *
    */

   public simpleIDs getIetmIds( String aTOPIC_SDESC ) {

      String[] iIds = { "IETM_ID", "IETM_TOPIC_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TOPIC_SDESC", aTOPIC_SDESC );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.IETM_TOPIC, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );
      return lIds;

   }


   /**
    * This function is to retrieve req ids.
    *
    *
    */

   public simpleIDs getReqIds( String aTASK_CD, String aTASK_CLASS_CD ) {

      String[] iIds = { "TASK_DB_ID", "TASK_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_CLASS_CD", aTASK_CLASS_CD );
      lArgs.addArguments( "TASK_CD", aTASK_CD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_TASK, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );
      return lIds;

   }


   /**
    * This function is to retrieve role id.
    *
    *
    */

   public String getRoleIds( String aRole_CD ) {

      String[] iIds = { "ROLE_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "ROLE_CD", aRole_CD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.UTL_ROLE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      return llists.get( 0 ).get( 0 );

   }


   /**
    * This function is to retrieve impact ids.
    *
    *
    */

   public simpleIDs getIMPACTIds( String aIMPACT_CD ) {

      String[] iIds = { "IMPACT_DB_ID", "IMPACT_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "IMPACT_CD", aIMPACT_CD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.REF_IMPACT, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );
      return lIds;

   }


   /**
    * This function is to retrieve part no IDs from eqp_part_no.
    *
    *
    */

   public simpleIDs getPartNoIds( String aPART_NO_OEM, String aMANUFACT_CD ) {

      String[] iIds = { "PART_NO_DB_ID", "PART_NO_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "PART_NO_OEM", aPART_NO_OEM );
      lArgs.addArguments( "MANUFACT_CD", aMANUFACT_CD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EQP_PART_NO, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );
      return lIds;

   }


   /**
    * This function is to retrieve task IDs from task_task table.
    *
    *
    */
   public simpleIDs getTaskIds( String aTASK_CD, String aTASK_NAME ) {

      String[] iIds = { "TASK_DB_ID", "TASK_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_CD", aTASK_CD );
      lArgs.addArguments( "TASK_NAME", aTASK_NAME );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_TASK, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );
      return lIds;
   }


   /**
    * This function is to retrieve assemble information from EQP_ASSMBL_BOM table.
    *
    *
    */

   public assmbleInfor getassmblInfor( String aASSMBL_BOM_CD ) {

      String[] iIds = { "ASSMBL_DB_ID", "ASSMBL_CD", "ASSMBL_BOM_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "ASSMBL_BOM_CD", aASSMBL_BOM_CD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EQP_ASSMBL_BOM, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      assmbleInfor lIds = new assmbleInfor( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ),
            llists.get( 0 ).get( 2 ), null );

      return lIds;

   }


   /**
    * This function is to verify task_task table for 15067.
    *
    *
    */
   public simpleIDs verifyTaskTask_15067( simpleIDs aTaskIds, String aTASK_CLASS_CD,
         assmbleInfor aassmbleInfor, String aTASK_SUBCLASS_CD, String aTASK_ORIGINATOR_CD,
         String aTaskCD, String aTaskName, String aTASK_LDESC, String aINSTRUCTION_LDESC,
         String aTASK_REF_SDESC, String aTASK_APPL_EFF_LDESC, String aLAST_SCHED_DEAD_BOOL,
         String aSOFT_DEADLINE_BOOL, String aWORKSCOPE_BOOL, String aTASK_MUST_REMOVE_CD,
         String aETOPS_BOOL, String aON_CONDITION_BOOL, String aEST_DURATION_QT ) {

      String[] iIds = { "TASK_DEFN_DB_ID", "TASK_DEFN_ID", "TASK_CLASS_CD", "ASSMBL_DB_ID",
            "ASSMBL_CD", "ASSMBL_BOM_ID", "TASK_SUBCLASS_CD", "TASK_ORIGINATOR_CD", "TASK_CD",
            "TASK_NAME", "TASK_LDESC", "INSTRUCTION_LDESC", "TASK_REF_SDESC", "TASK_APPL_EFF_LDESC",
            "LAST_SCHED_DEAD_BOOL", "SOFT_DEADLINE_BOOL", "WORKSCOPE_BOOL", "TASK_MUST_REMOVE_CD",
            "ETOPS_BOOL", "ON_CONDITION_BOOL", "EST_DURATION_QT" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_DB_ID", aTaskIds.getNO_DB_ID() );
      lArgs.addArguments( "TASK_ID", aTaskIds.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_TASK, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      Assert.assertTrue( "TASK_CLASS_CD",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aTASK_CLASS_CD ) );
      if ( aassmbleInfor != null ) {
         Assert.assertTrue( "ASSMBL_DB_ID",
               llists.get( 0 ).get( 3 ).equalsIgnoreCase( aassmbleInfor.getASSMBL_DB_ID() ) );
         Assert.assertTrue( "ASSMBL_CD",
               llists.get( 0 ).get( 4 ).equalsIgnoreCase( aassmbleInfor.getASSMBL_CD() ) );
         Assert.assertTrue( "ASSMBL_BOM_ID",
               llists.get( 0 ).get( 5 ).equalsIgnoreCase( aassmbleInfor.getASSMBL_BOM_ID() ) );
      }
      Assert.assertTrue( "TASK_SUBCLASS_CD",
            llists.get( 0 ).get( 6 ).equalsIgnoreCase( aTASK_SUBCLASS_CD ) );
      Assert.assertTrue( "TASK_ORIGINATOR_CD",
            llists.get( 0 ).get( 7 ).equalsIgnoreCase( aTASK_ORIGINATOR_CD ) );
      Assert.assertTrue( "TASK_CD", llists.get( 0 ).get( 8 ).equalsIgnoreCase( aTaskCD ) );
      Assert.assertTrue( "TASK_NAME", llists.get( 0 ).get( 9 ).equalsIgnoreCase( aTaskName ) );
      Assert.assertTrue( "TASK_LDESC", llists.get( 0 ).get( 10 ).equalsIgnoreCase( aTASK_LDESC ) );
      Assert.assertTrue( "INSTRUCTION_LDESC",
            llists.get( 0 ).get( 11 ).equalsIgnoreCase( aINSTRUCTION_LDESC ) );
      if ( aTASK_REF_SDESC != null ) {
         Assert.assertTrue( "TASK_REF_SDESC",
               llists.get( 0 ).get( 12 ).equalsIgnoreCase( aTASK_REF_SDESC ) );
      }
      if ( aTASK_APPL_EFF_LDESC != null ) {
         Assert.assertTrue( "TASK_APPL_EFF_LDESC",
               llists.get( 0 ).get( 13 ).equalsIgnoreCase( aTASK_APPL_EFF_LDESC ) );
      }
      if ( aLAST_SCHED_DEAD_BOOL != null ) {
         Assert.assertTrue( "LAST_SCHED_DEAD_BOOL",
               llists.get( 0 ).get( 14 ).equalsIgnoreCase( aLAST_SCHED_DEAD_BOOL ) );
      }

      if ( aSOFT_DEADLINE_BOOL != null ) {
         Assert.assertTrue( "SOFT_DEADLINE_BOOL",
               llists.get( 0 ).get( 15 ).equalsIgnoreCase( aSOFT_DEADLINE_BOOL ) );
      }
      if ( aWORKSCOPE_BOOL != null ) {
         Assert.assertTrue( "WORKSCOPE_BOOL",
               llists.get( 0 ).get( 16 ).equalsIgnoreCase( aWORKSCOPE_BOOL ) );
      }

      if ( aTASK_MUST_REMOVE_CD != null ) {
         Assert.assertTrue( "TASK_MUST_REMOVE_CD",
               llists.get( 0 ).get( 17 ).equalsIgnoreCase( aTASK_MUST_REMOVE_CD ) );

      }

      if ( aETOPS_BOOL != null ) {
         Assert.assertTrue( "ETOPS_BOOL",
               llists.get( 0 ).get( 18 ).equalsIgnoreCase( aETOPS_BOOL ) );

      }

      if ( aON_CONDITION_BOOL != null ) {
         Assert.assertTrue( "ON_CONDITION_BOOL",
               llists.get( 0 ).get( 19 ).equalsIgnoreCase( aON_CONDITION_BOOL ) );

      }

      if ( aEST_DURATION_QT != null ) {
         Assert.assertTrue( "EST_DURATION_QT",
               llists.get( 0 ).get( 20 ).equalsIgnoreCase( aEST_DURATION_QT ) );

      }

      return lIds;
   }


   /**
    * This function is to verify task_task table for 15067.
    *
    *
    */
   public simpleIDs verifyTaskTask_15067( simpleIDs aTaskIds, String aTASK_CLASS_CD,
         assmbleInfor aassmbleInfor, String aTASK_SUBCLASS_CD, String aTASK_ORIGINATOR_CD,
         String aTaskCD, String aTaskName, String aTASK_LDESC, String aINSTRUCTION_LDESC,
         String aTASK_REF_SDESC, String aTASK_APPL_EFF_LDESC, String aLAST_SCHED_DEAD_BOOL,
         String aSOFT_DEADLINE_BOOL, String aWORKSCOPE_BOOL, String aTASK_MUST_REMOVE_CD,
         String aETOPS_BOOL, String aON_CONDITION_BOOL, String aEST_DURATION_QT,
         String aUNIQUE_BOOL ) {

      String[] iIds = { "TASK_DEFN_DB_ID", "TASK_DEFN_ID", "TASK_CLASS_CD", "ASSMBL_DB_ID",
            "ASSMBL_CD", "ASSMBL_BOM_ID", "TASK_SUBCLASS_CD", "TASK_ORIGINATOR_CD", "TASK_CD",
            "TASK_NAME", "TASK_LDESC", "INSTRUCTION_LDESC", "TASK_REF_SDESC", "TASK_APPL_EFF_LDESC",
            "LAST_SCHED_DEAD_BOOL", "SOFT_DEADLINE_BOOL", "WORKSCOPE_BOOL", "TASK_MUST_REMOVE_CD",
            "ETOPS_BOOL", "ON_CONDITION_BOOL", "EST_DURATION_QT", "UNIQUE_BOOL" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_DB_ID", aTaskIds.getNO_DB_ID() );
      lArgs.addArguments( "TASK_ID", aTaskIds.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_TASK, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      Assert.assertTrue( "TASK_CLASS_CD",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aTASK_CLASS_CD ) );
      if ( aassmbleInfor != null ) {
         Assert.assertTrue( "ASSMBL_DB_ID",
               llists.get( 0 ).get( 3 ).equalsIgnoreCase( aassmbleInfor.getASSMBL_DB_ID() ) );
         Assert.assertTrue( "ASSMBL_CD",
               llists.get( 0 ).get( 4 ).equalsIgnoreCase( aassmbleInfor.getASSMBL_CD() ) );
         Assert.assertTrue( "ASSMBL_BOM_ID",
               llists.get( 0 ).get( 5 ).equalsIgnoreCase( aassmbleInfor.getASSMBL_BOM_ID() ) );
      }
      Assert.assertTrue( "TASK_SUBCLASS_CD",
            llists.get( 0 ).get( 6 ).equalsIgnoreCase( aTASK_SUBCLASS_CD ) );
      Assert.assertTrue( "TASK_ORIGINATOR_CD",
            llists.get( 0 ).get( 7 ).equalsIgnoreCase( aTASK_ORIGINATOR_CD ) );
      Assert.assertTrue( "TASK_CD", llists.get( 0 ).get( 8 ).equalsIgnoreCase( aTaskCD ) );
      Assert.assertTrue( "TASK_NAME", llists.get( 0 ).get( 9 ).equalsIgnoreCase( aTaskName ) );
      Assert.assertTrue( "TASK_LDESC", llists.get( 0 ).get( 10 ).equalsIgnoreCase( aTASK_LDESC ) );
      Assert.assertTrue( "INSTRUCTION_LDESC",
            llists.get( 0 ).get( 11 ).equalsIgnoreCase( aINSTRUCTION_LDESC ) );
      if ( aTASK_REF_SDESC != null ) {
         Assert.assertTrue( "TASK_REF_SDESC",
               llists.get( 0 ).get( 12 ).equalsIgnoreCase( aTASK_REF_SDESC ) );
      }
      if ( aTASK_APPL_EFF_LDESC != null ) {
         Assert.assertTrue( "TASK_APPL_EFF_LDESC",
               llists.get( 0 ).get( 13 ).equalsIgnoreCase( aTASK_APPL_EFF_LDESC ) );
      }
      if ( aLAST_SCHED_DEAD_BOOL != null ) {
         Assert.assertTrue( "LAST_SCHED_DEAD_BOOL",
               llists.get( 0 ).get( 14 ).equalsIgnoreCase( aLAST_SCHED_DEAD_BOOL ) );
      }

      if ( aSOFT_DEADLINE_BOOL != null ) {
         Assert.assertTrue( "SOFT_DEADLINE_BOOL",
               llists.get( 0 ).get( 15 ).equalsIgnoreCase( aSOFT_DEADLINE_BOOL ) );
      }
      if ( aWORKSCOPE_BOOL != null ) {
         Assert.assertTrue( "WORKSCOPE_BOOL",
               llists.get( 0 ).get( 16 ).equalsIgnoreCase( aWORKSCOPE_BOOL ) );
      }

      if ( aTASK_MUST_REMOVE_CD != null ) {
         Assert.assertTrue( "TASK_MUST_REMOVE_CD",
               llists.get( 0 ).get( 17 ).equalsIgnoreCase( aTASK_MUST_REMOVE_CD ) );

      }

      if ( aETOPS_BOOL != null ) {
         Assert.assertTrue( "ETOPS_BOOL",
               llists.get( 0 ).get( 18 ).equalsIgnoreCase( aETOPS_BOOL ) );

      }

      if ( aON_CONDITION_BOOL != null ) {
         Assert.assertTrue( "ON_CONDITION_BOOL",
               llists.get( 0 ).get( 19 ).equalsIgnoreCase( aON_CONDITION_BOOL ) );

      }

      if ( aEST_DURATION_QT != null ) {
         Assert.assertTrue( "EST_DURATION_QT",
               llists.get( 0 ).get( 20 ).equalsIgnoreCase( aEST_DURATION_QT ) );

      }

      Assert.assertTrue( "UNIQUE_BOOL",
            llists.get( 0 ).get( 21 ).equalsIgnoreCase( aUNIQUE_BOOL ) );

      return lIds;
   }


   /**
    * This function is to verify task_task table for 15067.
    *
    *
    */
   public simpleIDs verifyTaskTask_REPL( simpleIDs aTaskIds, String aTASK_CLASS_CD,
         assmbleInfor aassmbleInfor, String aTaskCD, String aTaskName, String aTASK_LDESC,
         String aTASK_REF_SDESC ) {

      String[] iIds = { "TASK_DEFN_DB_ID", "TASK_DEFN_ID", "TASK_CLASS_CD", "ASSMBL_DB_ID",
            "ASSMBL_CD", "TASK_CD", "TASK_NAME", "TASK_LDESC", "TASK_REF_SDESC" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_DB_ID", aTaskIds.getNO_DB_ID() );
      lArgs.addArguments( "TASK_ID", aTaskIds.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_TASK, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      Assert.assertTrue( "TASK_CLASS_CD",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aTASK_CLASS_CD ) );
      if ( aassmbleInfor != null ) {
         Assert.assertTrue( "ASSMBL_DB_ID",
               llists.get( 0 ).get( 3 ).equalsIgnoreCase( aassmbleInfor.getASSMBL_DB_ID() ) );
         Assert.assertTrue( "ASSMBL_CD",
               llists.get( 0 ).get( 4 ).equalsIgnoreCase( aassmbleInfor.getASSMBL_CD() ) );
      }
      Assert.assertTrue( "TASK_CD", llists.get( 0 ).get( 5 ).equalsIgnoreCase( aTaskCD ) );
      Assert.assertTrue( "TASK_NAME", llists.get( 0 ).get( 6 ).equalsIgnoreCase( aTaskName ) );
      Assert.assertTrue( "TASK_LDESC", llists.get( 0 ).get( 7 ).equalsIgnoreCase( aTASK_LDESC ) );
      if ( aTASK_REF_SDESC != null ) {
         Assert.assertTrue( "TASK_REF_SDESC",
               llists.get( 0 ).get( 8 ).equalsIgnoreCase( aTASK_REF_SDESC ) );
      }

      return lIds;
   }


   /**
    * This function is to verify task_task table for 15067.
    *
    *
    */
   public simpleIDs verifyTaskTask_TRSFRM( simpleIDs aTaskIds, String aTASK_CLASS_CD,
         assmbleInfor aassmbleInfor, String aTASK_SUBCLASS_CD, String aTASK_ORIGINATOR_CD,
         String aTaskCD, String aTaskName, String aTASK_LDESC, String aINSTRUCTION_LDESC,
         String aTASK_REF_SDESC, String aTASK_APPL_EFF_LDESC, String aLAST_SCHED_DEAD_BOOL,
         String aSOFT_DEADLINE_BOOL, String aWORKSCOPE_BOOL, String aTASK_MUST_REMOVE_CD,
         String aETOPS_BOOL, String aON_CONDITION_BOOL, String aEST_DURATION_QT ) {

      String[] iIds = { "TASK_DEFN_DB_ID", "TASK_DEFN_ID", "TASK_CLASS_CD", "ASSMBL_DB_ID",
            "ASSMBL_CD", "TASK_SUBCLASS_CD", "TASK_ORIGINATOR_CD", "TASK_CD", "TASK_NAME",
            "TASK_LDESC", "INSTRUCTION_LDESC", "TASK_REF_SDESC", "TASK_APPL_EFF_LDESC",
            "LAST_SCHED_DEAD_BOOL", "SOFT_DEADLINE_BOOL", "WORKSCOPE_BOOL", "TASK_MUST_REMOVE_CD",
            "ETOPS_BOOL", "ON_CONDITION_BOOL", "EST_DURATION_QT" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_DB_ID", aTaskIds.getNO_DB_ID() );
      lArgs.addArguments( "TASK_ID", aTaskIds.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_TASK, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      Assert.assertTrue( "TASK_CLASS_CD",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aTASK_CLASS_CD ) );
      if ( aassmbleInfor != null ) {
         Assert.assertTrue( "ASSMBL_DB_ID",
               llists.get( 0 ).get( 3 ).equalsIgnoreCase( aassmbleInfor.getASSMBL_DB_ID() ) );
         Assert.assertTrue( "ASSMBL_CD",
               llists.get( 0 ).get( 4 ).equalsIgnoreCase( aassmbleInfor.getASSMBL_CD() ) );
      }
      Assert.assertTrue( "TASK_SUBCLASS_CD",
            llists.get( 0 ).get( 5 ).equalsIgnoreCase( aTASK_SUBCLASS_CD ) );
      Assert.assertTrue( "TASK_ORIGINATOR_CD",
            llists.get( 0 ).get( 6 ).equalsIgnoreCase( aTASK_ORIGINATOR_CD ) );
      Assert.assertTrue( "TASK_CD", llists.get( 0 ).get( 7 ).equalsIgnoreCase( aTaskCD ) );
      Assert.assertTrue( "TASK_NAME", llists.get( 0 ).get( 8 ).equalsIgnoreCase( aTaskName ) );
      Assert.assertTrue( "TASK_LDESC", llists.get( 0 ).get( 9 ).equalsIgnoreCase( aTASK_LDESC ) );
      Assert.assertTrue( "INSTRUCTION_LDESC",
            llists.get( 0 ).get( 10 ).equalsIgnoreCase( aINSTRUCTION_LDESC ) );
      if ( aTASK_REF_SDESC != null ) {
         Assert.assertTrue( "TASK_REF_SDESC",
               llists.get( 0 ).get( 11 ).equalsIgnoreCase( aTASK_REF_SDESC ) );
      }
      Assert.assertTrue( "TASK_APPL_EFF_LDESC",
            llists.get( 0 ).get( 12 ).equalsIgnoreCase( aTASK_APPL_EFF_LDESC ) );
      Assert.assertTrue( "LAST_SCHED_DEAD_BOOL",
            llists.get( 0 ).get( 13 ).equalsIgnoreCase( aLAST_SCHED_DEAD_BOOL ) );
      Assert.assertTrue( "SOFT_DEADLINE_BOOL",
            llists.get( 0 ).get( 14 ).equalsIgnoreCase( aSOFT_DEADLINE_BOOL ) );
      if ( aWORKSCOPE_BOOL != null ) {
         Assert.assertTrue( "WORKSCOPE_BOOL",
               llists.get( 0 ).get( 15 ).equalsIgnoreCase( aWORKSCOPE_BOOL ) );
      }

      if ( aTASK_MUST_REMOVE_CD != null ) {
         Assert.assertTrue( "TASK_MUST_REMOVE_CD",
               llists.get( 0 ).get( 16 ).equalsIgnoreCase( aTASK_MUST_REMOVE_CD ) );

      }

      if ( aETOPS_BOOL != null ) {
         Assert.assertTrue( "ETOPS_BOOL",
               llists.get( 0 ).get( 17 ).equalsIgnoreCase( aETOPS_BOOL ) );

      }

      if ( aON_CONDITION_BOOL != null ) {
         Assert.assertTrue( "ON_CONDITION_BOOL",
               llists.get( 0 ).get( 18 ).equalsIgnoreCase( aON_CONDITION_BOOL ) );

      }

      if ( aEST_DURATION_QT != null ) {
         Assert.assertTrue( "EST_DURATION_QT",
               llists.get( 0 ).get( 19 ).equalsIgnoreCase( aEST_DURATION_QT ) );

      }

      return lIds;
   }


   /**
    * This function is to verify task_interval table for 15067.
    *
    *
    */

   public void verifyTASKTASKIETM( simpleIDs aTaskIds, String aIetm_ORD, simpleIDs aIetmIds ) {

      String[] iIds = { "IETM_ID", "IETM_TOPIC_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_DB_ID", aTaskIds.getNO_DB_ID() );
      lArgs.addArguments( "TASK_ID", aTaskIds.getNO_ID() );
      lArgs.addArguments( "IETM_ORD", aIetm_ORD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_TASK_IETM, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "IETM_ID",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aIetmIds.getNO_DB_ID() ) );
      Assert.assertTrue( "IETM_TOPIC_ID",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( aIetmIds.getNO_ID() ) );

   }


   /**
    * This function is to retrieve data type IDs from MIM_DATA_TYPE table.
    *
    *
    */

   public simpleIDs getTypeIds( String aDATA_TYPE_CD ) {
      String[] iIds = { "DATA_TYPE_DB_ID", "DATA_TYPE_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "DATA_TYPE_CD", aDATA_TYPE_CD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.MIM_DATA_TYPE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );
      return lIds;

   }


   /**
    * This function is to verify task_sched_rule table for 15067.
    *
    *
    */

   public void verifyTASKSCHEDRULE_15067( simpleIDs aTaskIds, simpleIDs aDataTypeIds,
         String aDEF_INTERVAL_QT, String aDEF_INITIAL_QT ) {
      String[] iIds = { "DATA_TYPE_DB_ID", "DATA_TYPE_ID", "DEF_INTERVAL_QT", "DEF_INITIAL_QT" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_DB_ID", aTaskIds.getNO_DB_ID() );
      lArgs.addArguments( "TASK_ID", aTaskIds.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_SCHED_RULE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "DATA_TYPE_DB_ID",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aDataTypeIds.getNO_DB_ID() ) );
      Assert.assertTrue( "DATA_TYPE_ID",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( aDataTypeIds.getNO_ID() ) );
      Assert.assertTrue( "DEF_INTERVAL_QT",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aDEF_INTERVAL_QT ) );
      if ( aDEF_INITIAL_QT != null ) {
         Assert.assertTrue( "DEF_INITIAL_QT",
               llists.get( 0 ).get( 3 ).equalsIgnoreCase( aDEF_INITIAL_QT ) );

      }

   }


   /**
    * This function is to verify task_sched_rule table.
    *
    *
    */

   public void verifyTASKSCHEDRULE( simpleIDs aTaskIds, simpleIDs aDataTypeIds,
         String aINTERVAL_QT ) {
      String[] iIds = { "DEF_INTERVAL_QT" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_DB_ID", aTaskIds.getNO_DB_ID() );
      lArgs.addArguments( "TASK_ID", aTaskIds.getNO_ID() );
      lArgs.addArguments( "DATA_TYPE_DB_ID", aDataTypeIds.getNO_DB_ID() );
      lArgs.addArguments( "DATA_TYPE_ID", aDataTypeIds.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_SCHED_RULE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "DEF_INTERVAL_QT",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aINTERVAL_QT ) );

   }


   /**
    * This function is to retrieve IETM IDs.
    *
    *
    */
   public simpleIDs getIetmIDs( String aIetmCD ) {

      String[] iIds = { "IETM_DB_ID", "IETM_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "IETM_CD", aIetmCD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.IETM_IETM, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lIds;

   }


   /**
    * This function is to retrieve IETM topic ID.
    *
    *
    */
   public String getIetmTopicID( simpleIDs aIetmIds, String aTopicSdesc ) {

      String[] iIds = { "IETM_TOPIC_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "IETM_DB_ID", aIetmIds.getNO_DB_ID() );
      lArgs.addArguments( "IETM_ID", aIetmIds.getNO_ID() );
      lArgs.addArguments( "TOPIC_SDESC", aTopicSdesc );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.IETM_TOPIC, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      return llists.get( 0 ).get( 0 );

   }


   /**
    * This function is to verify task_interval table for 15067.
    *
    *
    */

   public void verifyTASKINTERVAL_15067( simpleIDs aTaskIds, simpleIDs aDataTypeIds,
         simpleIDs aPartIds, String aDEF_INTERVAL_QT, String aDEF_INITIAL_QT ) {

      String[] iIds = { "DATA_TYPE_DB_ID", "DATA_TYPE_ID", "PART_NO_DB_ID", "PART_NO_ID",
            "INTERVAL_QT", "INITIAL_QT" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_DB_ID", aTaskIds.getNO_DB_ID() );
      lArgs.addArguments( "TASK_ID", aTaskIds.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_INTERVAL, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "DATA_TYPE_DB_ID",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aDataTypeIds.getNO_DB_ID() ) );
      Assert.assertTrue( "DATA_TYPE_ID",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( aDataTypeIds.getNO_ID() ) );
      Assert.assertTrue( "PART_NO_DB_ID",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aPartIds.getNO_DB_ID() ) );
      Assert.assertTrue( "PART_NO_ID",
            llists.get( 0 ).get( 3 ).equalsIgnoreCase( aPartIds.getNO_ID() ) );
      Assert.assertTrue( "INTERVAL_QT",
            llists.get( 0 ).get( 4 ).equalsIgnoreCase( aDEF_INTERVAL_QT ) );
      if ( aDEF_INITIAL_QT != null ) {
         Assert.assertTrue( "INITIAL_QT",
               llists.get( 0 ).get( 5 ).equalsIgnoreCase( aDEF_INITIAL_QT ) );

      }

   }


   /**
    * This function is to verify task_task_dep table for 15067.
    *
    *
    */

   public void verifyTASKTASKDEP_15067( simpleIDs aTaskIds, String aTASK_DEP_ACTION_CD,
         simpleIDs aDEPids ) {
      String[] iIds = { "TASK_DEP_ACTION_CD", "DEP_TASK_DEFN_DB_ID", "DEP_TASK_DEFN_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_DB_ID", aTaskIds.getNO_DB_ID() );
      lArgs.addArguments( "TASK_ID", aTaskIds.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_TASK_DEP, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "TASK_DEP_ACTION_CD",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aTASK_DEP_ACTION_CD ) );
      Assert.assertTrue( "DEP_TASK_DEFN_DB_ID",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( aDEPids.getNO_DB_ID() ) );
      Assert.assertTrue( "DEP_TASK_DEFN_ID",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aDEPids.getNO_ID() ) );

   }


   /**
    * This function is to verify task_part_transform table.
    *
    *
    */

   public void verifyTASKPARTTRANSFORM( simpleIDs aTaskIds, simpleIDs aOLDPNIDs,
         simpleIDs aNEWPNIDs ) {
      String[] iIds =
            { "OLD_PART_NO_DB_ID", "OLD_PART_NO_ID", "NEW_PART_NO_DB_ID", "NEW_PART_NO_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_DB_ID", aTaskIds.getNO_DB_ID() );
      lArgs.addArguments( "TASK_ID", aTaskIds.getNO_ID() );

      String iQueryString =
            TableUtil.buildTableQuery( TableUtil.TASK_PART_TRANSFORM, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "OLD_PART_NO_DB_ID",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aOLDPNIDs.getNO_DB_ID() ) );
      Assert.assertTrue( "OLD_PART_NO_ID",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( aOLDPNIDs.getNO_ID() ) );
      Assert.assertTrue( "NEW_PART_NO_DB_ID",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aNEWPNIDs.getNO_DB_ID() ) );
      Assert.assertTrue( "NEW_PART_NO_ID",
            llists.get( 0 ).get( 3 ).equalsIgnoreCase( aNEWPNIDs.getNO_ID() ) );

   }


   /**
    * This function is to verify task_advisory table.
    *
    *
    */

   public void verifyTASKADVISORY( simpleIDs aTaskIds, String aTASK_ADVISORY_TYPE_CD,
         String aROLE_ID, String aADVISORY_LDESC ) {
      String[] iIds = { "ROLE_ID", "ADVISORY_LDESC" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_DB_ID", aTaskIds.getNO_DB_ID() );
      lArgs.addArguments( "TASK_ID", aTaskIds.getNO_ID() );
      lArgs.addArguments( "TASK_ADVISORY_TYPE_CD", aTASK_ADVISORY_TYPE_CD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_ADVISORY, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "ROLE_ID", llists.get( 0 ).get( 0 ).equalsIgnoreCase( aROLE_ID ) );
      Assert.assertTrue( "ADVISORY_LDESC",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( aADVISORY_LDESC ) );

   }


   /**
    * This function is to verify task_impact table.
    *
    *
    */

   public void verifyTASKIMPACT( simpleIDs aTaskIds, String aIMPACT_CD, String aIMPACT_LDESC ) {
      String[] iIds = { "IMPACT_CD", "IMPACT_LDESC" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_DB_ID", aTaskIds.getNO_DB_ID() );
      lArgs.addArguments( "TASK_ID", aTaskIds.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_IMPACT, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "IMPACT_CD", llists.get( 0 ).get( 0 ).equalsIgnoreCase( aIMPACT_CD ) );
      Assert.assertTrue( "IMPACT_LDESC",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( aIMPACT_LDESC ) );

   }


   /**
    * This function is to verify task_task_dep table
    *
    *
    */
   public void verifyTaskTaskDep( simpleIDs aIds, String aAction_CD ) {
      String lQuery = "select 1 from " + TableUtil.TASK_TASK_DEP + " where TASK_DB_ID="
            + aIds.getNO_DB_ID() + " and TASK_ID=" + aIds.getNO_ID() + " and TASK_DEP_ACTION_CD='"
            + aAction_CD + "'";
      Assert.assertTrue( "Check task_task_dep table to verify the record exists",
            RecordsExist( lQuery ) );
   }


   /**
    * This function is to verify task_task_dep table
    *
    *
    */
   public void verifyTaskTaskDepNONExist( simpleIDs aIds, String aAction_CD ) {
      String lQuery = "select 1 from " + TableUtil.TASK_TASK_DEP + " where TASK_DB_ID="
            + aIds.getNO_DB_ID() + " and TASK_ID=" + aIds.getNO_ID() + " and TASK_DEP_ACTION_CD='"
            + aAction_CD + "'";
      Assert.assertFalse( "Check task_task_dep table to verify the record exists",
            RecordsExist( lQuery ) );
   }


   /**
    * Calls check COMPREQ error code
    *
    *
    */
   protected void checkCOMPREQErrorCode( String aTestName, String aValidationCode ) {

      boolean lFound = false;
      String lerror_desc = null;
      String lerrorMsg = null;

      String lquery = TestConstants.BL_COMPREQ_ERROR_CHECK;

      String[] iIds = { "RESULT_CD", "TECH_DESC" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      List<ArrayList<String>> lresult = execute( lquery, lfields );

      for ( int i = 0; i < lresult.size(); i++ ) {
         if ( lresult.get( i ).get( 0 ).equalsIgnoreCase( aValidationCode ) ) {
            lerror_desc = lresult.get( i ).get( 1 );
            lFound = true;
         } else {
            lerrorMsg = lerrorMsg + lresult.get( i ).get( 0 ) + ": " + lresult.get( i ).get( 1 );
         }

      }

      Assert.assertTrue( "The error code not found- " + aValidationCode + " : " + lerror_desc
            + " other error found [" + lerrorMsg + "]", lFound );

   }

}
