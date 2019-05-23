package com.mxi.mx.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;

import com.mxi.mx.core.maint.plan.datamodels.assmbleInfor;
import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;


/**
 * DOCUMENT_ME
 *
 */
public class RefDocAndRefDocComponent extends BaselineLoaderTest {

   public String iACFT_ASSMBLCD = "ACFT_CD1";
   public String iENG_ASSMBLCD = "ENG_CD1";
   public String iATA_CD_TRK = "ACFT-SYS-1-1-TRK-BATCH-PARENT";
   public String iATA_CD_SYS = "SYS-1-1";
   public String iATA_CD_ENG_SYS = "ENG-SYS-1";
   public String iATA_CD_APU = "APU-ASSY";
   public String iREF_TASK_CD_1 = "AATEST";
   public String iREF_TASK_CD_2 = "BBTEST";

   // C_REF table
   public String iREF_TASK_NAME_1 = "ATESTNAME";
   public String iREF_TASK_DESC_1 = "ATESTDESC";
   public String iREF_TASK_NAME_2 = "BTESTNAME";
   public String iREF_TASK_DESC_2 = "BTESTDESC";
   public String iCLASS_CD = "AMP";
   public String iSUBCLASS_CD = "TEST3";
   public String iORIGINATOR_CD = "AWL";
   public String iRECEIVED_BY = "mxi";
   public String iISSUED_BY_MANUFACT_CD = "1234567890";
   public String iRESCHED_FROM_CD = "EXECUTE";
   public String iSCHED_FROM_MANUFACT_BOOL = "Y";
   public String iSCHED_TO_LATEST_DEADLINE_BOOL = "Y";
   public String iSCHED_TO_LATEST_DEADLINE_BOOL_Y_number = "1";
   public String iSOFT_DEADLINE_BOOL = "Y";
   public String iSOFT_DEADLINE_BOOL_Y_number = "1";
   public String iMUST_BE_REMOVED_CD = "OFFPARENT";
   public String iNEXT_SHOP_VISIT_BOOL = "N";
   public String iMANUAL_SCHEDULING_BOOL = "N";
   public String iMIN_FORECAST_RANGE = "6";
   public String iON_CONDITION_BOOL = "N";
   public String iON_CONDITION_BOOL_N_number = "0";

   // C_REF_TASK_LINK
   public String iLINK_TYPE_CD = "COMPLIES";
   public String iLINKED_TASK_CD = "AL_TASK";
   public String iLINKED_TASK_NAME = "AL_TASK";
   public String iLINKED_TASK_ATA_CD = "ACFT_CD1";

   public String iLINKED_TASK_CD_AT_TEST = "AT_TEST2";
   public String iLINKED_TASK_NAME_AT_TEST = "AT_TEST2";
   public String iLINKED_TASK_ATA_CD_AT_TEST = "SYS-1";

   public String iLINKED_TASK_CD_REFDOC = "ATTEST";
   public String iLINKED_TASK_NAME_REFDOC = "ATTASKNAME";

   public String iLINKED_TASK_CD_ENG = "ENG-SYS-AL-TASK";
   public String iLINKED_TASK_NAME_ENG = "ENG-SYS-AL-TASK";
   public String iLINKED_TASK_ATA_CD_ENG = "ENG-SYS-1";

   // C-REF_DYNAMIC_DEADLINE
   public String iSCHED_DATA_TYPE_CD = "CYCLES";
   public String iSCHED_DATA_TYPE_CD_HOURS = "HOURS";
   public String iSCHED_DATA_TYPE_CD_EOT = "EOT";
   public String iSCHED_INITIAL_QT = "10";
   public String iSCHED_INTERVAL_QT = "20";
   public String iSCHED_THRESHOLD_QT = "100";
   public String iSCHED_HRS_THRESHOLD = "100";
   public String iSCHED_EOT_THRESHOLD = "100";

   public String iTASK_DEP_ACTION_CD = "CRT";
   public simpleIDs iTASK_IDs_15067 = null;
   public simpleIDs iTASK_DEFN_IDs_15067 = null;

   public simpleIDs iTASK_IDs = null;
   public simpleIDs iTASK_DEFN_IDs = null;
   public simpleIDs iTASK_IDs_2 = null;
   public simpleIDs iTASK_DEFN_IDs_2 = null;

   public String iWORK_TYPE_LIST_1 = "SERVICE";
   public String iWORK_TYPE_LIST_2 = "FUEL";

   public String iPART_NO_OEM_TRK = "A0000010";
   public String iMANUFACT_CD_TRK = "11111";


   /**
    * This function is to validate error code exists.
    *
    *
    */
   public void validateErrorCode( String aCode ) {

      List<String> llist = getErrorCodeList();
      Assert.assertTrue( "Error code exists.", llist.contains( aCode ) );

   }


   /**
    * This function is to retrieve errors code list
    *
    *
    */

   public List<String> getErrorCodeList() {
      String[] iIds = { "RESULT_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      String iQueryString = "select c_ref.result_cd " + " from c_ref "
            + " inner join DL_REF_MESSAGE on " + " DL_REF_MESSAGE.result_cd=c_ref.result_cd "
            + " UNION ALL " + " select c_ref_dynamic_deadline.result_cd "
            + " from c_ref_dynamic_deadline inner join DL_REF_MESSAGE on "
            + " DL_REF_MESSAGE.result_cd=c_ref_dynamic_deadline.result_cd " + " UNION ALL "
            + " select c_proc_ref_deadline.result_cd "
            + " from c_proc_ref_deadline inner join DL_REF_MESSAGE on "
            + " DL_REF_MESSAGE.result_cd=c_proc_ref_deadline.result_cd " + " UNION ALL "
            + " select c_ref_task_link.result_cd "
            + " from c_ref_task_link inner join DL_REF_MESSAGE on "
            + " DL_REF_MESSAGE.result_cd=c_ref_task_link.result_cd";
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      List<String> llistM = new ArrayList<String>();
      for ( int i = 0; i < llists.size(); i++ ) {
         llistM.add( llists.get( i ).get( 0 ) );
      }

      return llistM;

   }


   /**
    * Calls check comp ref error code
    *
    *
    */
   protected void checkCompRefErrorCode( String aTestName, String aValidationCode ) {

      boolean lFound = false;
      String lerror_desc = null;
      String lerrorMsg = null;

      String lquery = TestConstants.BL_COMREF_ERROR_CHECK;

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
    * This function is to verify task_task table.
    *
    *
    */
   public simpleIDs verifyTaskTask( simpleIDs aTaskIds, String aTASK_CLASS_CD,
         assmbleInfor aassmbleInfor, String aTASK_SUBCLASS_CD, String aTASK_ORIGINATOR_CD,
         String aTaskCD, String aTaskName, String aTASK_LDESC, String aTASK_REF_SDESC,
         String aLAST_SCHED_DEAD_BOOL, String aSOFT_DEADLINE_BOOL, String aTASK_MUST_REMOVE_CD,
         String aON_CONDITION_BOOL, String aRECURRING_TASK_BOOL ) {

      String[] iIds =
            { "TASK_DEFN_DB_ID", "TASK_DEFN_ID", "TASK_CLASS_CD", "ASSMBL_DB_ID", "ASSMBL_CD",
                  "ASSMBL_BOM_ID", "TASK_SUBCLASS_CD", "TASK_ORIGINATOR_CD", "TASK_CD", "TASK_NAME",
                  "TASK_LDESC", "TASK_REF_SDESC", "LAST_SCHED_DEAD_BOOL", "SOFT_DEADLINE_BOOL",
                  "TASK_MUST_REMOVE_CD", "ON_CONDITION_BOOL", "RECURRING_TASK_BOOL" };
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
      if ( aTASK_SUBCLASS_CD != null ) {
         Assert.assertTrue( "TASK_SUBCLASS_CD",
               llists.get( 0 ).get( 6 ).equalsIgnoreCase( aTASK_SUBCLASS_CD ) );
      }
      Assert.assertTrue( "TASK_ORIGINATOR_CD",
            llists.get( 0 ).get( 7 ).equalsIgnoreCase( aTASK_ORIGINATOR_CD ) );
      Assert.assertTrue( "TASK_CD", llists.get( 0 ).get( 8 ).equalsIgnoreCase( aTaskCD ) );
      Assert.assertTrue( "TASK_NAME", llists.get( 0 ).get( 9 ).equalsIgnoreCase( aTaskName ) );
      Assert.assertTrue( "TASK_LDESC", llists.get( 0 ).get( 10 ).equalsIgnoreCase( aTASK_LDESC ) );
      if ( aTASK_REF_SDESC != null ) {
         Assert.assertTrue( "TASK_REF_SDESC",
               llists.get( 0 ).get( 11 ).equalsIgnoreCase( aTASK_REF_SDESC ) );
      }
      Assert.assertTrue( "LAST_SCHED_DEAD_BOOL",
            llists.get( 0 ).get( 12 ).equalsIgnoreCase( aLAST_SCHED_DEAD_BOOL ) );
      Assert.assertTrue( "SOFT_DEADLINE_BOOL",
            llists.get( 0 ).get( 13 ).equalsIgnoreCase( aSOFT_DEADLINE_BOOL ) );

      if ( aTASK_MUST_REMOVE_CD != null ) {
         Assert.assertTrue( "TASK_MUST_REMOVE_CD",
               llists.get( 0 ).get( 14 ).equalsIgnoreCase( aTASK_MUST_REMOVE_CD ) );

      }

      if ( aON_CONDITION_BOOL != null ) {
         Assert.assertTrue( "ON_CONDITION_BOOL",
               llists.get( 0 ).get( 15 ).equalsIgnoreCase( aON_CONDITION_BOOL ) );

      }

      Assert.assertTrue( "RECURRING _TASK_BOOL",
            llists.get( 0 ).get( 16 ).equalsIgnoreCase( aRECURRING_TASK_BOOL ) );

      return lIds;
   }


   /**
    * This function is to verify task_ref_doc table.
    *
    *
    */

   public void verifyTASKREFDOC( simpleIDs aTaskIds, simpleIDs aHRIDs, String aMANUFACT_CD,
         String aTASK_DEF_ISSUE_BY_CD, java.sql.Date aRECEIVE_DT, java.sql.Date aISSUE_DT ) {

      String[] iIds =
            { "RECEIVE_BY_HR_DB_ID", "RECEIVE_BY_HR_ID", "MANUFACT_CD", "TASK_DEF_ISSUE_BY_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_DB_ID", aTaskIds.getNO_DB_ID() );
      lArgs.addArguments( "TASK_ID", aTaskIds.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_REF_DOC, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "RECEIVE_BY_HR_DB_ID",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aHRIDs.getNO_DB_ID() ) );
      Assert.assertTrue( "RECEIVE_BY_HR_ID",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( aHRIDs.getNO_ID() ) );
      Assert.assertTrue( "MANUFACT_CD", llists.get( 0 ).get( 2 ).equalsIgnoreCase( aMANUFACT_CD ) );
      if ( aTASK_DEF_ISSUE_BY_CD != null ) {
         Assert.assertTrue( "TASK_DEF_ISSUE_BY_CD",
               llists.get( 0 ).get( 3 ).equalsIgnoreCase( aTASK_DEF_ISSUE_BY_CD ) );

      }

      String[] iDates = { "RECEIVE_DT", "ISSUE_DT" };
      lfields = new ArrayList<String>( Arrays.asList( iDates ) );

      // Parameters required by the query.
      lArgs = new WhereClause();
      lArgs.addArguments( "TASK_DB_ID", aTaskIds.getNO_DB_ID() );
      lArgs.addArguments( "TASK_ID", aTaskIds.getNO_ID() );

      iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_REF_DOC, lfields, lArgs );
      List<ArrayList<java.sql.Date>> lnlists = execute_date( iQueryString, lfields );

      if ( aRECEIVE_DT != null ) {
         Assert.assertTrue( "RECEIVE_DT",
               DateDiffInDays( lnlists.get( 0 ).get( 0 ), aRECEIVE_DT ) == 0L );
      }
      if ( aISSUE_DT != null ) {
         Assert.assertTrue( "ISSUE_DT",
               DateDiffInDays( lnlists.get( 0 ).get( 1 ), aISSUE_DT ) == 0L );

      }

   }


   /**
    * This function is to verify task_task_dep table.
    *
    *
    */

   public void verifyTASKTASkDEP( simpleIDs aTaskIds, simpleIDs aDEPIds,
         String aTASK_DEP_ACTION_CD ) {
      String[] iIds = { "TASK_DEP_ACTION_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_DB_ID", aTaskIds.getNO_DB_ID() );
      lArgs.addArguments( "TASK_ID", aTaskIds.getNO_ID() );
      lArgs.addArguments( "DEP_TASK_DEFN_DB_ID", aDEPIds.getNO_DB_ID() );
      lArgs.addArguments( "DEP_TASK_DEFN_ID", aDEPIds.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_TASK_DEP, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "TASK_DEP_ACTION_CD",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aTASK_DEP_ACTION_CD ) );

   }


   /**
    * This function is to verify task_work_type table
    *
    *
    */
   public void verifyWorkType( simpleIDs aIds, String aType ) {
      String lQuery =
            "select 1 from " + TableUtil.TASK_WORK_TYPE + " where TASK_DB_ID=" + aIds.getNO_DB_ID()
                  + " and TASK_ID=" + aIds.getNO_ID() + " and WORK_TYPE_CD='" + aType + "'";
      Assert.assertTrue( "Check task_work_type table to verify the record exists",
            RecordsExist( lQuery ) );
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

   public void verifyTASKSCHEDRULE( simpleIDs aTaskIds, simpleIDs aDataTypeIds,
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
    * Calls check ref doc error code
    *
    *
    */
   protected void checkUsageErrorCode( String aTestName, String aValidationCode ) {

      boolean lFound = false;
      String lerror_desc = null;
      String lerrorMsg = null;

      String lquery = TestConstants.BL_REF_DOC_ERROR_CHECK;

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
