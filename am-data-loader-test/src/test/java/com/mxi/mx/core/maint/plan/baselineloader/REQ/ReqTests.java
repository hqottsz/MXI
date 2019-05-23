package com.mxi.mx.core.maint.plan.baselineloader.REQ;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;

import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;
import com.mxi.mx.util.BaselineLoaderTest;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.TestConstants;
import com.mxi.mx.util.ValidationAndImport;
import com.mxi.mx.util.WhereClause;


/**
 * This test suite contains common test utilities for REQ tests
 *
 *
 * @author Alicia Qian
 */
public class ReqTests extends BaselineLoaderTest {

   public ValidationAndImport ivalidationandimport;

   // C_REQ table
   public String iACFT_ASSMBLCD = "ACFT_CD1";
   public String iENG_ASSMBLCD = "ENG_CD1";
   public String iAPU_ASSMBLCD = "APU_CD1";

   // C_REQ table
   public String iACFT_REQ_ATA_CD_TRK = "ACFT-SYS-1-1-TRK-BATCH-PARENT";
   public String iACFT_REQ_ATA_CD_SYS = "SYS-1-1";
   public String iENG_REQ_ATA_CD_SYS = "ENG-SYS-1";
   public String iAPU_REQ_ATA_CD_SYS = "APU-SYS-1";
   public String iACFT_REQ_ATA_CD_ENG = "ENG-ASSY";
   public String iACFT_REQ_ATA_CD_APU = "APU-ASSY";
   public String iACFT_REQ_ATA_CD_SYS_EXIST = "SYS-2";

   // C_REQ table
   public String iREQ_TASK_CD_1 = "NEWATEST";
   public String iREQ_TASK_CD_1_2 = "ATEST";
   public String iREQ_TASK_CD_2 = "BTEST";
   public String iREQ_TASK_CD_3 = "CTEST";
   public String iREQ_TASK_CD_4 = "DTEST";
   public String iREQ_TASK_CD_5 = "FTEST";
   public String iREQ_TASK_CD_6 = "GTEST";
   public String iREQ_TASK_CD_EXIST = "ATTEST";

   // C_REQ table
   public String iREQ_TASK_NAME_1 = "ATESTNAME";
   public String iREQ_TASK_NAME_2 = "BTESTNAME";
   public String iREQ_TASK_NAME_3 = "CTESTNAME";
   public String iREQ_TASK_NAME_4 = "DTESTNAME";
   public String iREQ_TASK_NAME_5 = "FTESTNAME";
   public String iREQ_TASK_NAME_6 = "GTESTNAME";
   public String iREQ_TASK_NAME_EXIST = "ATTESTNAME";

   // C_REQ table
   public String iREQ_TASK_DESC_1 = "ATESTDESC";
   public String iREQ_TASK_DESC_2 = "BTESTDESC";
   public String iREQ_TASK_DESC_3 = "CTESTDESC";
   public String iREQ_TASK_DESC_4 = "DTESTDESC";
   public String iREQ_TASK_DESC_5 = "FTESTDESC";
   public String iREQ_TASK_DESC_6 = "GTESTDESC";
   public String iREQ_TASK_DESC_EXIST = "ATTESTDESC";

   // C_REQ table
   public String iREQ_TASK_REF_SDESC_1 = "ATESTREFDESC";
   public String iREQ_TASK_REF_SDESC_2 = "BTESTREFDESC";
   public String iREQ_TASK_REF_SDESC_3 = "CTESTREFDESC";
   public String iREQ_TASK_REF_SDESC_4 = "DTESTREFDESC";
   public String iREQ_TASK_REF_SDESC_5 = "FTESTREFDESC";
   public String iREQ_TASK_REF_SDESC_6 = "GTESTREFDESC";
   public String iREQ_TASK_REF_SDESC_EXIST = "ATTESTDESC";

   // C_REQ table
   public String iCLASS_CD = "MOD";
   public String iCLASS_CD_REF = "AMP";
   public String iSUBCLASS_CD = "TEST1";
   public String iORIGINATOR_CD = "AWL";
   public String iAPPLICABILITY_DESC = "1,5-7";
   public String iLAST_DEADLINE_DRIVER_BOOL = "Y";
   public String iLAST_DEADLINE_DRIVER_BOOL_N = "1";
   public String iSOFT_DEADLINE_DRIVER_BOOL = "Y";
   public String iSOFT_DEADLINE_DRIVER_BOOL_N = "1";
   public String iSCHED_FROM_MANUFACT_DT_BOOL = "Y";
   public String iSCHED_FROM_MANUFACT_DT_BOOL_N = "1";
   public String iWORKSCOPE_BOOL = "N";
   public String iWORKSCOPE_BOOL_N = "0";
   public String iENFORCE_WORKSCOPE_ORD_BOOL = "Y";
   public String iENFORCE_WORKSCOPE_ORD_BOOL_N = "1";

   public String iREQ_INSTRUCTIONS = "testInstruction";

   public String iWORK_TYPE_LIST_1 = "SERVICE";
   public String iWORK_TYPE_LIST_2 = "FUEL";

   // C_REQ_LABOUR
   public String iLABOR_SKILL_CD = "ENG";
   public String iLABOR_SKILL_CD_QA = "QA";
   public String iLABOR_SKILL_CD_LBR = "LBR";
   public String iMAN_PWR_CT = "1";
   public String iSCHED_WORK_HRS = "3";
   public String iSCHED_INSP_HRS = "2";
   public String iSCHED_CERT_HRS = "3";

   // C_REQ_PART
   public String iPART_NO_OEM_1 = "A0000001";
   public String iPART_NO_OEM_2 = "E0000009";
   public String iPART_NO_OEM_3 = "AP0000012";
   public String iPART_NO_OEM_4 = "CHW000039";
   public String iPART_NO_OEM_5 = "ENG_ASSY_PN1";
   public String iPART_NO_OEM_6 = "ATKIT";
   public String iPART_NO_OEM_7 = "A0000003";
   public String iPART_NO_OEM_8 = "T0000100";
   public String iPART_NO_OEM_9 = "T0000001";
   public String iPART_NO_OEM_10 = "T0000003";
   public String iPART_NO_OEM_11 = "T0000006";
   public String iPART_NO_OEM_12 = "AT000001";

   // C_REQ_PART
   public String iMANUFACT_REF_1 = "10001";
   public String iMANUFACT_REF_2 = "10001";
   public String iMANUFACT_REF_3 = "1234567890";
   public String iMANUFACT_REF_4 = "ABC11";
   public String iMANUFACT_REF_5 = "ABC11";
   public String iMANUFACT_REF_6 = "ABC11";
   public String iMANUFACT_REF_7 = "ABC11";
   public String iMANUFACT_REF_8 = "1234567890";
   public String iMANUFACT_REF_9 = "ABC11";
   public String iMANUFACT_REF_10 = "ABC11";
   public String iMANUFACT_REF_11 = "ABC11";
   public String iMANUFACT_REF_12 = "ABC11";

   // C_REQ_PART
   public String iIPC_REF_CD = "ACFT-SYS-1-1-TRK-P1";
   public String iIPC_REF_CD_2 = "ENG-ASSY";
   public String iIPC_REF_CD_4 = "CHW-PG6";
   public String iIPC_REF_CD_7 = "ACFT-SYS-1-1-TRK-P3";
   public String iREQ_QT = "1";
   public String iREQ_QT_10 = "10";
   public String iPOSITION_1 = "1";
   public String iPOSITION_LEFT = "LEFT";
   public String iPOSITION_CENTER = "CENTER";
   public String iPOSITION_RIGHT = "RIGHT";
   public String iPOSITION_CHW = "COMHW-POS-1";

   public String iREMOVE_BOOL_Y = "Y";
   public String iREMOVE_BOOL_N = "N";
   public String iREMOVE_BOOL_Y_NUMBER = "1";
   public String iREMOVE_BOOL_N_NUMBER = "0";
   public String iREMOVE_REASON_CD = "T/X-OVHL";
   public String iINSTALL_BOOL_Y = "Y";
   public String iINSTALL_BOOL_N = "N";
   public String iINSTALL_BOOL_Y_NUMBER = "1";
   public String iINSTALL_BOOL_N_NUMBER = "0";
   public String iREQ_PRIORITY_CD = "NORMAL";
   public String iPART_PROVIDER_TYPE_CD = "CUSTPAID";
   public String iIS_PART_SPECIFIC_BOOL_Y = "Y";

   // C_REQ_PART
   public String iREQ_ACTION_CD_REQ = "REQ";
   public String iREQ_ACTION_CD_NOREQ = "NOREQ";
   public String iREQ_ACTION_CD_ASREQ = "ASREQ";

   // C_REQ_TOOL
   public String iREQ_HR_INT = "5";
   public String iREQ_HR_Decimal_1 = "5.2";
   public String iREQ_HR_Decimal_2 = "9999.90";
   public String iREQ_HR_Decimal_3 = "5.0";
   public String iREQ_HR_Decimal_4 = "5.23";
   public String iREQ_HR_Decimal_5 = "9999.99";
   public String iREQ_HR_ZERO = "0";

   // C_REQ_IETM_TOPIC
   public String iREQ_IETM_CD_1 = "TEST1";
   public String iREQ_IETM_CD_2 = "TEST2";
   public String iREQ_IETM_CD_3 = "TEST3";
   public String iREQ_IETM_CD_4 = "TEST4";
   public String iREQ_TOPIC_SDESC_1 = "AUTOTEST1-1";
   public String iREQ_TOPIC_SDESC_2 = "AUTOTEST1-2";
   public String iREQ_TOPIC_SDESC_3 = "AUTOTEST1-3";
   public String iREQ_TOPIC_SDESC_4 = "AUTOTEST1-4";
   public String iREQ_IETM_ORD_1 = "1";
   public String iREQ_IETM_ORD_2 = "2";
   public String iREQ_IETM_ORD_3 = "60";
   public String iREQ_IETM_ORD_4 = "25";

   public String iSCHED_THRESHOLD_QT = "100";

   // C_REQ_STEP_SKILL
   public String iINSP_BOOL_Y = "Y";
   public String iINSP_BOOL_N = "N";

   // import data
   public simpleIDs iTaskIDs_1 = null;
   public simpleIDs iTaskDefnIDs_1 = null;

   public simpleIDs iTaskIDs_2 = null;
   public simpleIDs iTaskDefnIDs_2 = null;


   /**
    * This function is to check error(s) for validation table is loaded
    *
    *
    */
   public void VerifyValidation() {

      int lCount = countRowsOfQuery( TestConstants.C_REQ_ERROR_CHECK_TABLE );
      Assert.assertTrue( "There should be no error in C_REQ. ", lCount == 0 );

      lCount = countRowsOfQuery( TestConstants.C_REQ_DYNAMIC_DEADLINE_ERROR_CHECK_TABLE );
      Assert.assertTrue( "There should be no error in C_REQ_DYNAMIC_DEADLINE. ", lCount == 0 );

      lCount = countRowsOfQuery( TestConstants.C_REQ_DYNAMIC_PART_DEADLINE_ERROR_CHECK_TABLE );
      Assert.assertTrue( "There should be no error in C_REQ_DYNAMIC_PART_DEADLINE. ", lCount == 0 );

      lCount = countRowsOfQuery( TestConstants.C_REQ_IETM_TOPIC_ERROR_CHECK_TABLE );
      Assert.assertTrue( "There should be no error in C_REQ_IETM_TOPIC. ", lCount == 0 );

      lCount = countRowsOfQuery( TestConstants.C_REQ_JIC_ERROR_CHECK_TABLE );
      Assert.assertTrue( "There should be no error in C_REQ_JIC. ", lCount == 0 );

      lCount = countRowsOfQuery( TestConstants.C_REQ_PART_TRANSFORM_ERROR_CHECK_TABLE );
      Assert.assertTrue( "There should be no error in C_REQ_PART_TRANSFORM. ", lCount == 0 );

      lCount = countRowsOfQuery( TestConstants.C_REQ_REPL_ERROR_CHECK_TABLE );
      Assert.assertTrue( "There should be no error in C_REQ_REPL. ", lCount == 0 );

      lCount = countRowsOfQuery( TestConstants.C_REQ_STANDARD_DEADLINE_ERROR_CHECK_TABLE );
      Assert.assertTrue( "There should be no error in C_REQ_STANDARD_DEADLINE. ", lCount == 0 );

      lCount = countRowsOfQuery( TestConstants.C_REQ_ADVISORY_ERROR_CHECK_TABLE );
      Assert.assertTrue( "There should be no error in C_REQ_ADVISORY. ", lCount == 0 );

      lCount = countRowsOfQuery( TestConstants.C_REQ_IMPACT_ERROR_CHECK_TABLE );
      Assert.assertTrue( "There should be no error in C_REQ_IMPACT. ", lCount == 0 );

      lCount = countRowsOfQuery( TestConstants.C_REQ_LABOUR_ERROR_CHECK_TABLE );
      Assert.assertTrue( "There should be no error in C_REQ_LABOUR. ", lCount == 0 );

      lCount = countRowsOfQuery( TestConstants.C_REQ_PART_ERROR_CHECK_TABLE );
      Assert.assertTrue( "There should be no error in C_REQ_PART. ", lCount == 0 );

      lCount = countRowsOfQuery( TestConstants.C_REQ_TOOL_ERROR_CHECK_TABLE );
      Assert.assertTrue( "There should be no error in C_REQ_TOOL . ", lCount == 0 );

      lCount = countRowsOfQuery( TestConstants.C_REQ_STEP_ERROR_CHECK_TABLE );
      Assert.assertTrue( "There should be no error in C_REQ_STEP. ", lCount == 0 );

      lCount = countRowsOfQuery( TestConstants.C_REQ_STEP_SKILL_ERROR_CHECK_TABLE );
      Assert.assertTrue( "There should be no error in C_REQ_STEP_SKILL. ", lCount == 0 );
   }


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
    * This function is to validate error code exists.
    *
    *
    */
   public void validateErrorCode_CReqLabour( String aCode ) {

      List<String> llist = getErrorCodeList_CReqLabour();
      String lerror_desc = getErrorDetail( aCode );
      Assert.assertTrue( "Error code found " + aCode + ": " + lerror_desc,
            llist.contains( aCode ) );

   }


   /**
    * This function is to validate error code exists.
    *
    *
    */
   public void validateErrorCode_CReqTool( String aCode ) {

      List<String> llist = getErrorCodeList_CReqTool();
      String lerror_desc = getErrorDetail( aCode );
      Assert.assertTrue( "Error code found " + aCode + ": " + lerror_desc,
            llist.contains( aCode ) );

   }


   /**
    * This function is to validate error code exists.
    *
    *
    */
   public void validateErrorCode_CReqStep( String aCode ) {

      List<String> llist = getErrorCodeList_CReqStep();
      String lerror_desc = getErrorDetail( aCode );
      Assert.assertTrue( "Error code found " + aCode + ": " + lerror_desc,
            llist.contains( aCode ) );

   }


   /**
    * This function is to validate error code exists.
    *
    *
    */
   public void validateErrorCode_CReqPart( String aCode ) {

      List<String> llist = getErrorCodeList_CReqPart();
      String lerror_desc = getErrorDetail( aCode );
      Assert.assertTrue( "Error code found " + aCode + ": " + lerror_desc,
            llist.contains( aCode ) );

   }


   /**
    * This function is to validate error code exists.
    *
    *
    */
   public void validateErrorCode_CReqStepSkill( String aCode ) {

      List<String> llist = getErrorCodeList_CReqStepSkill();
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
            + " DL_REF_MESSAGE.result_cd=c_comp_req_dyn_part_deadline.result_cd " + " UNION ALL "
            + " select c_req_labour.result_cd " + " from c_req_labour inner join DL_REF_MESSAGE on "
            + " DL_REF_MESSAGE.result_cd=c_req_labour.result_cd " + " UNION ALL "
            + " select c_req_part.result_cd " + " from c_req_part inner join DL_REF_MESSAGE on "
            + " DL_REF_MESSAGE.result_cd=c_req_part.result_cd " + " UNION ALL "
            + " select c_req_tool.result_cd " + " from c_req_tool inner join DL_REF_MESSAGE on "
            + " DL_REF_MESSAGE.result_cd=c_req_tool.result_cd " + " UNION ALL "
            + " select c_req_step.result_cd " + " from c_req_step inner join DL_REF_MESSAGE on "
            + " DL_REF_MESSAGE.result_cd=c_req_step.result_cd " + " UNION ALL "
            + " select c_req_step_skill.result_cd "
            + " from c_req_step_skill inner join DL_REF_MESSAGE on "
            + " DL_REF_MESSAGE.result_cd=c_req_step_skill.result_cd " + "UNION ALL "
            + " select c_req_jic.result_cd from c_req_jic inner join DL_REF_MESSAGE on  DL_REF_MESSAGE.result_cd=c_req_jic.result_cd "
            + " UNION ALL " + " select c_req_impact.result_cd "
            + " from c_req_impact inner join DL_REF_MESSAGE on "
            + " DL_REF_MESSAGE.result_cd=c_req_impact.result_cd";

      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      List<String> llistM = new ArrayList<String>();
      for ( int i = 0; i < llists.size(); i++ ) {
         llistM.add( llists.get( i ).get( 0 ) );
      }

      return llistM;

   }


   /**
    * This function is to retrieve errors code list
    *
    *
    */

   public List<String> getErrorCodeList_CReqLabour() {
      String[] iIds = { "RESULT_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      String iQueryString = "select c_req_labour.result_cd " + " from c_req_labour "
            + " inner join DL_REF_MESSAGE on "
            + " DL_REF_MESSAGE.result_cd=c_req_labour.result_cd ";
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      List<String> llistM = new ArrayList<String>();
      for ( int i = 0; i < llists.size(); i++ ) {
         llistM.add( llists.get( i ).get( 0 ) );
      }

      return llistM;

   }


   /**
    * This function is to retrieve errors code list
    *
    *
    */

   public List<String> getErrorCodeList_CReqTool() {
      String[] iIds = { "RESULT_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      String iQueryString = "select c_req_tool.result_cd " + " from c_req_tool "
            + " inner join DL_REF_MESSAGE on " + " DL_REF_MESSAGE.result_cd=c_req_tool.result_cd ";
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      List<String> llistM = new ArrayList<String>();
      for ( int i = 0; i < llists.size(); i++ ) {
         llistM.add( llists.get( i ).get( 0 ) );
      }

      return llistM;

   }


   /**
    * This function is to retrieve errors code list
    *
    *
    */

   public List<String> getErrorCodeList_CReqStep() {
      String[] iIds = { "RESULT_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      String iQueryString = "select c_req_step.result_cd " + " from c_req_step "
            + " inner join DL_REF_MESSAGE on " + " DL_REF_MESSAGE.result_cd=c_req_step.result_cd ";
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      List<String> llistM = new ArrayList<String>();
      for ( int i = 0; i < llists.size(); i++ ) {
         llistM.add( llists.get( i ).get( 0 ) );
      }

      return llistM;

   }


   /**
    * This function is to retrieve errors code list
    *
    *
    */

   public List<String> getErrorCodeList_CReqPart() {
      String[] iIds = { "RESULT_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      String iQueryString = "select c_req_part.result_cd " + " from c_req_part "
            + " inner join DL_REF_MESSAGE on " + " DL_REF_MESSAGE.result_cd=c_req_part.result_cd ";
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      List<String> llistM = new ArrayList<String>();
      for ( int i = 0; i < llists.size(); i++ ) {
         llistM.add( llists.get( i ).get( 0 ) );
      }

      return llistM;

   }


   /**
    * This function is to retrieve errors code list
    *
    *
    */

   public List<String> getErrorCodeList_CReqStepSkill() {
      String[] iIds = { "RESULT_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      String iQueryString = "select c_req_step_skill.result_cd " + " from c_req_step_skill "
            + " inner join DL_REF_MESSAGE on "
            + " DL_REF_MESSAGE.result_cd=c_req_step_skill.result_cd ";
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      List<String> llistM = new ArrayList<String>();
      for ( int i = 0; i < llists.size(); i++ ) {
         llistM.add( llists.get( i ).get( 0 ) );
      }

      return llistM;

   }


   /**
    * This function is called by after test method to restore data which has been modified by tests.
    *
    *
    */

   public void RestoreData() {

      String lStrDelete = null;

      if ( iTaskIDs_1 != null ) {

         // delete task_task
         lStrDelete = "delete from " + TableUtil.TASK_TASK_DEP + "  where TASK_DB_ID="
               + iTaskIDs_1.getNO_DB_ID() + " and TASK_ID=" + iTaskIDs_1.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_step_skill
         lStrDelete = "delete from " + TableUtil.TASK_STEP_SKILL + "  where TASK_DB_ID="
               + iTaskIDs_1.getNO_DB_ID() + " and TASK_ID=" + iTaskIDs_1.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_step
         lStrDelete = "delete from " + TableUtil.TASK_STEP + "  where TASK_DB_ID="
               + iTaskIDs_1.getNO_DB_ID() + " and TASK_ID=" + iTaskIDs_1.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_tool_list
         lStrDelete = "delete from " + TableUtil.TASK_TOOL_LIST + "  where TASK_DB_ID="
               + iTaskIDs_1.getNO_DB_ID() + " and TASK_ID=" + iTaskIDs_1.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_part_list
         lStrDelete = "delete from " + TableUtil.TASK_PART_LIST + "  where TASK_DB_ID="
               + iTaskIDs_1.getNO_DB_ID() + " and TASK_ID=" + iTaskIDs_1.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_labour_list
         lStrDelete = "delete from " + TableUtil.TASK_LABOUR_LIST + "  where TASK_DB_ID="
               + iTaskIDs_1.getNO_DB_ID() + " and TASK_ID=" + iTaskIDs_1.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_work_type
         lStrDelete = "delete from " + TableUtil.TASK_WORK_TYPE + "  where TASK_DB_ID="
               + iTaskIDs_1.getNO_DB_ID() + " and TASK_ID=" + iTaskIDs_1.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_task
         lStrDelete = "delete from " + TableUtil.TASK_TASK + "  where TASK_DB_ID="
               + iTaskIDs_1.getNO_DB_ID() + " and TASK_ID=" + iTaskIDs_1.getNO_ID();
         executeSQL( lStrDelete );

      }

      // if ( iTaskIDs_2 != null ) {
      //
      // // delete task_step
      // lStrDelete = "delete from " + TableUtil.TASK_STEP + " where TASK_DB_ID="
      // + iTaskIDs_2.getNO_DB_ID() + " and TASK_ID=" + iTaskIDs_2.getNO_ID();
      // executeSQL( lStrDelete );
      //
      // // delete task_tool_list
      // lStrDelete = "delete from " + TableUtil.TASK_TOOL_LIST + " where TASK_DB_ID="
      // + iTaskIDs_2.getNO_DB_ID() + " and TASK_ID=" + iTaskIDs_2.getNO_ID();
      // executeSQL( lStrDelete );
      //
      // // delete task_part_list
      // lStrDelete = "delete from " + TableUtil.TASK_PART_LIST + " where TASK_DB_ID="
      // + iTaskIDs_2.getNO_DB_ID() + " and TASK_ID=" + iTaskIDs_2.getNO_ID();
      // executeSQL( lStrDelete );
      //
      // // delete task_labour_list
      // lStrDelete = "delete from " + TableUtil.TASK_LABOUR_LIST + " where TASK_DB_ID="
      // + iTaskIDs_2.getNO_DB_ID() + " and TASK_ID=" + iTaskIDs_2.getNO_ID();
      // executeSQL( lStrDelete );
      //
      // // delete task_work_type
      // lStrDelete = "delete from " + TableUtil.TASK_WORK_TYPE + " where TASK_DB_ID="
      // + iTaskIDs_2.getNO_DB_ID() + " and TASK_ID=" + iTaskIDs_2.getNO_ID();
      // executeSQL( lStrDelete );
      //
      // // delete task_task
      // lStrDelete = "delete from " + TableUtil.TASK_TASK + " where TASK_DB_ID="
      // + iTaskIDs_2.getNO_DB_ID() + " and TASK_ID=" + iTaskIDs_2.getNO_ID();
      // executeSQL( lStrDelete );
      //
      // }

      if ( iTaskDefnIDs_1 != null ) {
         lStrDelete = "delete from TASK_DEFN where TASK_DEFN_DB_ID=" + iTaskDefnIDs_1.getNO_DB_ID()
               + " and TASK_DEFN_ID=" + iTaskDefnIDs_1.getNO_ID();
         executeSQL( lStrDelete );

      }

      if ( iTaskDefnIDs_2 != null ) {
         lStrDelete = "delete from TASK_DEFN where TASK_DEFN_DB_ID=" + iTaskDefnIDs_2.getNO_DB_ID()
               + " and TASK_DEFN_ID=" + iTaskDefnIDs_2.getNO_ID();
         executeSQL( lStrDelete );

      }

   }


   /**
    * This function is to implement interface ValidationAndImport
    *
    * @param: blnOnlyValidation
    * @param: allornone
    *
    * @return: return code of Int
    *
    */
   public int runValidationAndImport( boolean ablnOnlyValidation, boolean allornone ) {
      int lrtValue = 0;
      ivalidationandimport = new ValidationAndImport() {

         @Override
         public int runValidation( boolean allornone ) {
            int lReturn = 0;
            CallableStatement lPrepareCallJICPART;

            try {

               lPrepareCallJICPART = getConnection()
                     .prepareCall( "BEGIN  req_import.validate_req(on_retcode =>?); END;" );

               lPrepareCallJICPART.registerOutParameter( 1, Types.INTEGER );
               lPrepareCallJICPART.execute();
               commit();
               lReturn = lPrepareCallJICPART.getInt( 1 );
            } catch ( SQLException e ) {
               e.printStackTrace();
            }
            return lReturn;

         }


         @Override
         public int runImport( boolean allornone ) {
            int lReturn = 0;
            CallableStatement lPrepareCallKIT;

            try {

               lPrepareCallKIT = getConnection()
                     .prepareCall( "BEGIN req_import.import_req(on_retcode =>?); END;" );

               lPrepareCallKIT.registerOutParameter( 1, Types.INTEGER );

               lPrepareCallKIT.execute();
               commit();
               lReturn = lPrepareCallKIT.getInt( 1 );
            } catch ( SQLException e ) {
               e.printStackTrace();
            }
            return lReturn;
         }

      };

      lrtValue = ablnOnlyValidation ? ivalidationandimport.runValidation( allornone )
            : ivalidationandimport.runImport( allornone );

      return lrtValue;
   }


   /**
    * This function is to verify functionality of export
    *
    *
    * @return: return code of Int
    * @throws SQLException
    *
    */
   public int runExport() throws SQLException {
      int lReturn = 0;
      CallableStatement lPrepareCallJICPART;

      lPrepareCallJICPART =
            getConnection().prepareCall( "BEGIN  req_import.export_req(on_retcode =>?); END;" );

      lPrepareCallJICPART.registerOutParameter( 1, Types.INTEGER );
      lPrepareCallJICPART.execute();
      commit();
      lReturn = lPrepareCallJICPART.getInt( 1 );

      return lReturn;

   }

}
