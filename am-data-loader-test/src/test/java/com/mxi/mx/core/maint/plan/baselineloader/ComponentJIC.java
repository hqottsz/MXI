package com.mxi.mx.core.maint.plan.baselineloader;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.mxi.mx.core.maint.plan.datamodels.bomPartPN;
import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;
import com.mxi.mx.util.BaselineLoaderTest;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.TestConstants;
import com.mxi.mx.util.ValidationAndImport;
import com.mxi.mx.util.WhereClause;


/**
 * This test suite contains test cases on validation and import functionality on c_comp_jic_import.
 *
 * @author ALICIA QIAN
 */
public class ComponentJIC extends BaselineLoaderTest {

   @Rule
   public TestName testName = new TestName();
   ValidationAndImport ivalidationandimport;

   public String iJIC_TASK_CD_1 = "ATEST";
   public String iJIC_TASK_CD_2 = "BTEST";

   // c_comp_jic
   public String iJIC_TASK_NAME_1 = "TASKNAME1";
   public String iJIC_TASK_NAME_2 = "TASKNAME2";
   public String iJIC_TASK_DESC_1 = "TASKDESC1";
   public String iJIC_TASK_DESC_2 = "TASKDESC2";
   public String iEXT_REFERENCE = "EXTPREF";
   public String iCLASS_CD = "SRVC";
   public String iSUBCLASS_CD = "LUBE";
   public String iORIGINATOR_CD = "AWL";
   public String iDOC_REFERENCE = "docreference";
   public String iETOPS_SIGNIFICANT_BOOL = "Y";
   public String iAPPLICABILITY_RANGE = "1,5-7";
   public String iAPPLICABILITY_DESC = "applicablityDesc";
   public String iWORK_TYPE_LIST_1 = "ELECTRIC";
   public String iWORK_TYPE_LIST_2 = "FUEL";
   public String iTASK_PRIORITY_CD = "HIGH";
   public String iISSUE_TO_ACCOUNT_CD = "5";
   public String iWORK_INSTRUCTIONS = "testInstruction";
   public String iEST_DURATION_HRS = "3";
   public String iENGINEERING_NOTES = "engineeringnote";

   // c_comp_jic_assigned_part
   public String iPN_TRK = "A0000017B";
   public String iMNFACT_TRK = "10001";

   public String iPN_BATCH = "A0000009";
   public String iMNFACT_BATCH = "10001";

   public String iPN_SER = "A0000012";
   public String iMNFACT_SER = "1234567890";

   public String iPN_ACFT = "ACFT_ASSY_PN1";
   public String iMNFACT_ACFT = "10001";

   // c_comp_jic_labour
   public String iLABOUR_SKILL_CD_1 = "ENG";
   public String iLABOUR_SKILL_CD_2 = "QA";
   public String iNUMBER_PEOPLE_REQ_1 = "10";
   public String iNUMBER_PEOPLE_REQ_2 = "1";
   public String iSCHED_WORK_HRS_1 = "3";
   public String iSCHED_WORK_HRS_2 = "2";
   public String iSCHED_CERT_HRS = "2";
   public String iSCHED_INSP_HRS = "1";

   // c_comp_jic_comhw
   public String iPART_GROUP_CD_1 = "CHW-PG1";
   public String iPART_GROUP_CD_2 = "CHW-PG2";
   public String iPART_REQUEST_QT = "1";
   public String iPN_COMHW_1 = "CHW000001";
   public String iPN_COMHW_2 = "CHW000003";
   public String iMNFACT_COMHW_1 = "11111";
   public String iMNFACT_COMHW_2 = "11111";
   public String iINSTALL_BOOL_1 = "N";
   public String iINSTALL_BOOL_1_N = "0";
   public String iINSTALL_BOOL_2 = "Y";
   public String iINSTALL_BOOL_2_N = "1";
   public String iREMOVE_BOOL_1 = "Y";
   public String iREMOVE_BOOL_1_N = "1";
   public String iREMOVE_BOOL_2 = "N";
   public String iREMOVE_BOOL_2_N = "0";
   public String iREMOVE_REASON_CD_1 = "MOD";
   public String iREMOVE_REASON_CD_2 = null;
   public String iREQUEST_PRIORITY_CD = "NORMAL";
   public String iREQUEST_ACTION_CD = "REQ";
   public String iPART_PROVIDER_TYPE_CD = "CUSTPAID";

   // c_comp_jic_tool
   public String iPART_GROUP_CD_TOOL_1 = "T0000001-ABC11";
   public String iPART_GROUP_CD_TOOL_2 = "T0000003-ABC11";
   public String iSCHED_HRS_1 = "1";
   public String iSCHED_HRS_2 = "2";

   // c_comp_jic_measurement
   public String iMEASUREMENT_ORD_1 = "1";
   public String iMEASUREMENT_ORD_2 = "2";
   public String iMEASUREMENT_CD_1 = "HOUR";
   public String iMEASUREMENT_CD_2 = "CYCLE";

   // c_comp_jic_step
   public String iJOB_STEP_ORD_1 = "1";
   public String iJOB_STEP_ORD_2 = "2";
   public String iJOB_STEP_DESC_1 = "step1";
   public String iJOB_STEP_DESC_2 = "step2";

   // c_comp_jic_ietm
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
   public String iIETM_ORDER_3 = "96";
   public String iIETM_ORDER_4 = "34";

   public simpleIDs iTask_IDs_1 = null;
   public simpleIDs iTask_DefnIDs_1 = null;
   public simpleIDs iTask_IDs_2 = null;
   public simpleIDs iTask_DefnIDs_2 = null;
   public String iDOMAIN_TYPE_CD = "CH";

   private static final int STEP_DEFN_DESC_LENGTH_LIMIT = 100;
   private static final String FIELD_RESULT_CD = "result_cd";


   /**
    * Clean up after each individual test
    *
    */
   @After
   @Override
   public void after() {
      RestoreData();
      super.after();
   }


   /**
    * Setup before executing each individual test
    *
    * @throws Exception
    */
   @Before
   @Override
   public void before() throws Exception {

      super.before();
      clearBaselineLoaderTables();

   }


   /**
    * This test is to verify c_comp_jic_import validation functionality. Component JIC is on TRK.
    *
    *
    */

   @Test
   public void testCFG_COMP_JIC_12225_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_comp_jic
      Map<String, String> lc_JICMap = new LinkedHashMap<>();

      lc_JICMap.put( "JIC_TASK_CD", "'" + iJIC_TASK_CD_1 + "'" );
      lc_JICMap.put( "JIC_TASK_NAME", "'" + iJIC_TASK_NAME_1 + "'" );
      lc_JICMap.put( "JIC_TASK_DESC", "'" + iJIC_TASK_DESC_1 + "'" );
      lc_JICMap.put( "EXT_REFERENCE", "'" + iEXT_REFERENCE + "'" );
      lc_JICMap.put( "CLASS_CD", "'AUTO'" );
      lc_JICMap.put( "SUBCLASS_CD", "'TEST4'" );
      lc_JICMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lc_JICMap.put( "DOC_REFERENCE", "'" + iDOC_REFERENCE + "'" );
      lc_JICMap.put( "ETOPS_SIGNIFICANT_BOOL", "'" + iETOPS_SIGNIFICANT_BOOL + "'" );
      lc_JICMap.put( "APPLICABILITY_RANGE", "'" + iAPPLICABILITY_RANGE + "'" );
      lc_JICMap.put( "APPLICABILITY_DESC", "'" + iAPPLICABILITY_DESC + "'" );
      lc_JICMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lc_JICMap.put( "TASK_PRIORITY_CD", "'" + iTASK_PRIORITY_CD + "'" );
      lc_JICMap.put( "ISSUE_TO_ACCOUNT_CD", "'" + iISSUE_TO_ACCOUNT_CD + "'" );
      lc_JICMap.put( "WORK_INSTRUCTIONS", "'" + iWORK_INSTRUCTIONS + "'" );
      lc_JICMap.put( "EST_DURATION_HRS", "'" + iEST_DURATION_HRS + "'" );
      lc_JICMap.put( "ENGINEERING_NOTES", "'" + iENGINEERING_NOTES + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_JIC, lc_JICMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkCOMPJICErrorCode( testName.getMethodName(), "CFG_COMP_JIC-12225" );

   }


   /**
    * This test is to verify c_comp_jic_import validation functionality. Component JIC is on TRK.
    *
    *
    */

   @Test
   public void testJICTRKVALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_comp_jic
      prepareDataONJIC( iJIC_TASK_CD_1, iJIC_TASK_NAME_1, iJIC_TASK_DESC_1 );

      // c_comp_jic_assigned_part
      prepareDataONJICASSIGNEDPART( iJIC_TASK_CD_1, iPN_TRK, iMNFACT_TRK );

      // c_comp_jic_labour
      prepareDataONJICLABOUR( iJIC_TASK_CD_1, iLABOUR_SKILL_CD_1, iNUMBER_PEOPLE_REQ_1,
            iSCHED_WORK_HRS_1 );
      prepareDataONJICLABOUR( iJIC_TASK_CD_1, iLABOUR_SKILL_CD_2, iNUMBER_PEOPLE_REQ_2,
            iSCHED_WORK_HRS_2 );

      // c_comp_jic_comhw
      prepareDataONJICCOMHW( iJIC_TASK_CD_1, iPART_GROUP_CD_1, iPN_COMHW_1, iMNFACT_COMHW_1,
            iINSTALL_BOOL_1, iREMOVE_BOOL_1, iREMOVE_REASON_CD_1 );
      prepareDataONJICCOMHW( iJIC_TASK_CD_1, iPART_GROUP_CD_2, iPN_COMHW_2, iMNFACT_COMHW_2,
            iINSTALL_BOOL_2, iREMOVE_BOOL_2, iREMOVE_REASON_CD_2 );

      // c_comp_jic_tool
      prepareDataONJICTOOL( iJIC_TASK_CD_1, iPART_GROUP_CD_TOOL_1, iSCHED_HRS_1 );
      prepareDataONJICTOOL( iJIC_TASK_CD_1, iPART_GROUP_CD_TOOL_2, iSCHED_HRS_2 );

      // c_comp_jic_measurement
      prepareDataONJICMEASUREMENT( iJIC_TASK_CD_1, iMEASUREMENT_ORD_1, iMEASUREMENT_CD_1 );
      prepareDataONJICMEASUREMENT( iJIC_TASK_CD_1, iMEASUREMENT_ORD_2, iMEASUREMENT_CD_2 );

      // c_comp_jic_step
      prepareDataONJICSTEP( iJIC_TASK_CD_1, iJOB_STEP_ORD_1, iJOB_STEP_DESC_1 );
      prepareDataONJICSTEP( iJIC_TASK_CD_1, iJOB_STEP_ORD_2, iJOB_STEP_DESC_2 );

      // c_comp_jic_ietm
      prepareDataONJICIETM( iJIC_TASK_CD_1, iIETM_CD_1, iIETM_TOPIC_1, iIETM_ORDER_1 );
      prepareDataONJICIETM( iJIC_TASK_CD_1, iIETM_CD_2, iIETM_TOPIC_2, iIETM_ORDER_2 );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify c_comp_jic_import import functionality. Component JIC is on TRK.
    *
    *
    */
   @Test
   public void testJICTRKIMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testJICTRKVALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTask_IDs_1 = null;
      iTask_DefnIDs_1 = null;

      // get task id from task_labour_list table
      iTask_IDs_1 = getZoneIDs( iLABOUR_SKILL_CD_2, iNUMBER_PEOPLE_REQ_2, iSCHED_WORK_HRS_2,
            iSCHED_CERT_HRS );

      // verify task_part_map
      simpleIDs lPN_Ids = getAccountIDs( iPN_TRK, iMNFACT_TRK );
      verifyTASKPARTMAP( iTask_IDs_1, lPN_Ids );

      // Verify other mxi tables
      verifyALL();

   }


   /**
    * This test is to verify c_comp_jic_import validation functionality. Component JIC is on BATCH.
    *
    *
    */

   @Test
   public void testJICBATCHVALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_comp_jic
      prepareDataONJIC( iJIC_TASK_CD_1, iJIC_TASK_NAME_1, iJIC_TASK_DESC_1 );

      // c_comp_jic_assigned_part
      prepareDataONJICASSIGNEDPART( iJIC_TASK_CD_1, iPN_BATCH, iMNFACT_BATCH );

      // c_comp_jic_labour
      prepareDataONJICLABOUR( iJIC_TASK_CD_1, iLABOUR_SKILL_CD_1, iNUMBER_PEOPLE_REQ_1,
            iSCHED_WORK_HRS_1 );
      prepareDataONJICLABOUR( iJIC_TASK_CD_1, iLABOUR_SKILL_CD_2, iNUMBER_PEOPLE_REQ_2,
            iSCHED_WORK_HRS_2 );

      // c_comp_jic_comhw
      prepareDataONJICCOMHW( iJIC_TASK_CD_1, iPART_GROUP_CD_1, iPN_COMHW_1, iMNFACT_COMHW_1,
            iINSTALL_BOOL_1, iREMOVE_BOOL_1, iREMOVE_REASON_CD_1 );
      prepareDataONJICCOMHW( iJIC_TASK_CD_1, iPART_GROUP_CD_2, iPN_COMHW_2, iMNFACT_COMHW_2,
            iINSTALL_BOOL_2, iREMOVE_BOOL_2, iREMOVE_REASON_CD_2 );

      // c_comp_jic_tool
      prepareDataONJICTOOL( iJIC_TASK_CD_1, iPART_GROUP_CD_TOOL_1, iSCHED_HRS_1 );
      prepareDataONJICTOOL( iJIC_TASK_CD_1, iPART_GROUP_CD_TOOL_2, iSCHED_HRS_2 );

      // c_comp_jic_measurement
      prepareDataONJICMEASUREMENT( iJIC_TASK_CD_1, iMEASUREMENT_ORD_1, iMEASUREMENT_CD_1 );
      prepareDataONJICMEASUREMENT( iJIC_TASK_CD_1, iMEASUREMENT_ORD_2, iMEASUREMENT_CD_2 );

      // c_comp_jic_step
      prepareDataONJICSTEP( iJIC_TASK_CD_1, iJOB_STEP_ORD_1, iJOB_STEP_DESC_1 );
      prepareDataONJICSTEP( iJIC_TASK_CD_1, iJOB_STEP_ORD_2, iJOB_STEP_DESC_2 );

      // c_comp_jic_ietm
      prepareDataONJICIETM( iJIC_TASK_CD_1, iIETM_CD_1, iIETM_TOPIC_1, iIETM_ORDER_1 );
      prepareDataONJICIETM( iJIC_TASK_CD_1, iIETM_CD_2, iIETM_TOPIC_2, iIETM_ORDER_2 );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify c_comp_jic_import import functionality. Component JIC is on BATCH.
    *
    *
    */
   @Test
   public void testJICBATCHIMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testJICBATCHVALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTask_IDs_1 = null;
      iTask_DefnIDs_1 = null;

      // get task id from task_labour_list table
      iTask_IDs_1 = getZoneIDs( iLABOUR_SKILL_CD_2, iNUMBER_PEOPLE_REQ_2, iSCHED_WORK_HRS_2,
            iSCHED_CERT_HRS );

      // verify task_part_map
      simpleIDs lPN_Ids = getAccountIDs( iPN_BATCH, iMNFACT_BATCH );
      verifyTASKPARTMAP( iTask_IDs_1, lPN_Ids );

      // Verify other mxi tables
      verifyALL();
   }


   /**
    * This test is to verify c_comp_jic_import validation functionality. Component JIC is on SER.
    *
    *
    */

   @Test
   public void testJICSERVALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_comp_jic
      prepareDataONJIC( iJIC_TASK_CD_1, iJIC_TASK_NAME_1, iJIC_TASK_DESC_1 );

      // c_comp_jic_assigned_part
      prepareDataONJICASSIGNEDPART( iJIC_TASK_CD_1, iPN_SER, iMNFACT_SER );

      // c_comp_jic_labour
      prepareDataONJICLABOUR( iJIC_TASK_CD_1, iLABOUR_SKILL_CD_1, iNUMBER_PEOPLE_REQ_1,
            iSCHED_WORK_HRS_1 );
      prepareDataONJICLABOUR( iJIC_TASK_CD_1, iLABOUR_SKILL_CD_2, iNUMBER_PEOPLE_REQ_2,
            iSCHED_WORK_HRS_2 );

      // c_comp_jic_comhw
      prepareDataONJICCOMHW( iJIC_TASK_CD_1, iPART_GROUP_CD_1, iPN_COMHW_1, iMNFACT_COMHW_1,
            iINSTALL_BOOL_1, iREMOVE_BOOL_1, iREMOVE_REASON_CD_1 );
      prepareDataONJICCOMHW( iJIC_TASK_CD_1, iPART_GROUP_CD_2, iPN_COMHW_2, iMNFACT_COMHW_2,
            iINSTALL_BOOL_2, iREMOVE_BOOL_2, iREMOVE_REASON_CD_2 );

      // c_comp_jic_tool
      prepareDataONJICTOOL( iJIC_TASK_CD_1, iPART_GROUP_CD_TOOL_1, iSCHED_HRS_1 );
      prepareDataONJICTOOL( iJIC_TASK_CD_1, iPART_GROUP_CD_TOOL_2, iSCHED_HRS_2 );

      // c_comp_jic_measurement
      prepareDataONJICMEASUREMENT( iJIC_TASK_CD_1, iMEASUREMENT_ORD_1, iMEASUREMENT_CD_1 );
      prepareDataONJICMEASUREMENT( iJIC_TASK_CD_1, iMEASUREMENT_ORD_2, iMEASUREMENT_CD_2 );

      // c_comp_jic_step
      prepareDataONJICSTEP( iJIC_TASK_CD_1, iJOB_STEP_ORD_1, iJOB_STEP_DESC_1 );
      prepareDataONJICSTEP( iJIC_TASK_CD_1, iJOB_STEP_ORD_2, iJOB_STEP_DESC_2 );

      // c_comp_jic_ietm
      prepareDataONJICIETM( iJIC_TASK_CD_1, iIETM_CD_1, iIETM_TOPIC_1, iIETM_ORDER_1 );
      prepareDataONJICIETM( iJIC_TASK_CD_1, iIETM_CD_2, iIETM_TOPIC_2, iIETM_ORDER_2 );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify c_comp_jic_import import functionality. Component JIC is on SER.
    *
    *
    */
   @Test
   public void testJICSERIMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testJICSERVALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTask_IDs_1 = null;
      iTask_DefnIDs_1 = null;

      // get task id from task_labour_list table
      iTask_IDs_1 = getZoneIDs( iLABOUR_SKILL_CD_2, iNUMBER_PEOPLE_REQ_2, iSCHED_WORK_HRS_2,
            iSCHED_CERT_HRS );

      // verify task_part_map
      simpleIDs lPN_Ids = getAccountIDs( iPN_SER, iMNFACT_SER );
      verifyTASKPARTMAP( iTask_IDs_1, lPN_Ids );

      // Verify other mxi tables
      verifyALL();
   }


   /**
    * This test is to verify c_comp_jic_import validation functionality. Component JIC is on
    * multiple parts:TRK and SER.
    *
    *
    */

   @Test
   public void testJICMultiplePartsVALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_comp_jic
      prepareDataONJIC( iJIC_TASK_CD_1, iJIC_TASK_NAME_1, iJIC_TASK_DESC_1 );

      // c_comp_jic_assigned_part
      prepareDataONJICASSIGNEDPART( iJIC_TASK_CD_1, iPN_SER, iMNFACT_SER );
      prepareDataONJICASSIGNEDPART( iJIC_TASK_CD_1, iPN_TRK, iMNFACT_TRK );

      // c_comp_jic_labour
      prepareDataONJICLABOUR( iJIC_TASK_CD_1, iLABOUR_SKILL_CD_1, iNUMBER_PEOPLE_REQ_1,
            iSCHED_WORK_HRS_1 );
      prepareDataONJICLABOUR( iJIC_TASK_CD_1, iLABOUR_SKILL_CD_2, iNUMBER_PEOPLE_REQ_2,
            iSCHED_WORK_HRS_2 );

      // c_comp_jic_comhw
      prepareDataONJICCOMHW( iJIC_TASK_CD_1, iPART_GROUP_CD_1, iPN_COMHW_1, iMNFACT_COMHW_1,
            iINSTALL_BOOL_1, iREMOVE_BOOL_1, iREMOVE_REASON_CD_1 );
      prepareDataONJICCOMHW( iJIC_TASK_CD_1, iPART_GROUP_CD_2, iPN_COMHW_2, iMNFACT_COMHW_2,
            iINSTALL_BOOL_2, iREMOVE_BOOL_2, iREMOVE_REASON_CD_2 );

      // c_comp_jic_tool
      prepareDataONJICTOOL( iJIC_TASK_CD_1, iPART_GROUP_CD_TOOL_1, iSCHED_HRS_1 );
      prepareDataONJICTOOL( iJIC_TASK_CD_1, iPART_GROUP_CD_TOOL_2, iSCHED_HRS_2 );

      // c_comp_jic_measurement
      prepareDataONJICMEASUREMENT( iJIC_TASK_CD_1, iMEASUREMENT_ORD_1, iMEASUREMENT_CD_1 );
      prepareDataONJICMEASUREMENT( iJIC_TASK_CD_1, iMEASUREMENT_ORD_2, iMEASUREMENT_CD_2 );

      // c_comp_jic_step
      prepareDataONJICSTEP( iJIC_TASK_CD_1, iJOB_STEP_ORD_1, iJOB_STEP_DESC_1 );
      prepareDataONJICSTEP( iJIC_TASK_CD_1, iJOB_STEP_ORD_2, iJOB_STEP_DESC_2 );

      // c_comp_jic_ietm
      prepareDataONJICIETM( iJIC_TASK_CD_1, iIETM_CD_1, iIETM_TOPIC_1, iIETM_ORDER_1 );
      prepareDataONJICIETM( iJIC_TASK_CD_1, iIETM_CD_2, iIETM_TOPIC_2, iIETM_ORDER_2 );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify c_comp_jic_import import functionality. Component JIC is on multiple
    * parts:TRK and SER.
    *
    *
    */
   @Test
   public void testJICMultiplePartsIMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testJICMultiplePartsVALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTask_IDs_1 = null;
      iTask_DefnIDs_1 = null;

      // get task id from task_labour_list table
      iTask_IDs_1 = getZoneIDs( iLABOUR_SKILL_CD_2, iNUMBER_PEOPLE_REQ_2, iSCHED_WORK_HRS_2,
            iSCHED_CERT_HRS );

      // verify task_part_map
      simpleIDs lPN_Ids = getAccountIDs( iPN_SER, iMNFACT_SER );
      verifyTASKPARTMAP( iTask_IDs_1, lPN_Ids );

      lPN_Ids = getAccountIDs( iPN_TRK, iMNFACT_TRK );
      verifyTASKPARTMAP( iTask_IDs_1, lPN_Ids );

      // Verify other mxi tables
      verifyALL();
   }


   /**
    * This test is to verify c_comp_jic_import validation functionality. Multiple Component JICs are
    * on TRK and SER.
    *
    *
    */

   @Test
   public void testMultipleJICVALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_comp_jic
      prepareDataONJIC( iJIC_TASK_CD_1, iJIC_TASK_NAME_1, iJIC_TASK_DESC_1 );
      prepareDataONJIC( iJIC_TASK_CD_2, iJIC_TASK_NAME_2, iJIC_TASK_DESC_2 );

      // c_comp_jic_assigned_part
      prepareDataONJICASSIGNEDPART( iJIC_TASK_CD_1, iPN_TRK, iMNFACT_TRK );
      prepareDataONJICASSIGNEDPART( iJIC_TASK_CD_2, iPN_SER, iMNFACT_SER );

      // c_comp_jic_labour
      prepareDataONJICLABOUR( iJIC_TASK_CD_1, iLABOUR_SKILL_CD_1, iNUMBER_PEOPLE_REQ_1,
            iSCHED_WORK_HRS_1 );
      prepareDataONJICLABOUR( iJIC_TASK_CD_2, iLABOUR_SKILL_CD_2, iNUMBER_PEOPLE_REQ_2,
            iSCHED_WORK_HRS_2 );

      // c_comp_jic_comhw
      prepareDataONJICCOMHW( iJIC_TASK_CD_1, iPART_GROUP_CD_1, iPN_COMHW_1, iMNFACT_COMHW_1,
            iINSTALL_BOOL_1, iREMOVE_BOOL_1, iREMOVE_REASON_CD_1 );
      prepareDataONJICCOMHW( iJIC_TASK_CD_2, iPART_GROUP_CD_2, iPN_COMHW_2, iMNFACT_COMHW_2,
            iINSTALL_BOOL_2, iREMOVE_BOOL_2, iREMOVE_REASON_CD_2 );

      // c_comp_jic_tool
      prepareDataONJICTOOL( iJIC_TASK_CD_1, iPART_GROUP_CD_TOOL_1, iSCHED_HRS_1 );
      prepareDataONJICTOOL( iJIC_TASK_CD_2, iPART_GROUP_CD_TOOL_2, iSCHED_HRS_2 );

      // c_comp_jic_measurement
      prepareDataONJICMEASUREMENT( iJIC_TASK_CD_1, iMEASUREMENT_ORD_1, iMEASUREMENT_CD_1 );
      prepareDataONJICMEASUREMENT( iJIC_TASK_CD_2, iMEASUREMENT_ORD_1, iMEASUREMENT_CD_2 );

      // c_comp_jic_step
      prepareDataONJICSTEP( iJIC_TASK_CD_1, iJOB_STEP_ORD_1, iJOB_STEP_DESC_1 );
      prepareDataONJICSTEP( iJIC_TASK_CD_2, iJOB_STEP_ORD_2, iJOB_STEP_DESC_2 );

      // c_comp_jic_ietm
      prepareDataONJICIETM( iJIC_TASK_CD_1, iIETM_CD_1, iIETM_TOPIC_1, iIETM_ORDER_1 );
      prepareDataONJICIETM( iJIC_TASK_CD_2, iIETM_CD_2, iIETM_TOPIC_2, iIETM_ORDER_2 );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify c_comp_jic_import import functionality. Multiple Component JICs are on
    * TRK and SER.
    *
    *
    */
   @Test
   public void testMultipleJICIMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testMultipleJICVALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTask_IDs_1 = null;
      iTask_DefnIDs_1 = null;
      iTask_IDs_2 = null;
      iTask_DefnIDs_2 = null;

      // get task id from task_labour_list table
      iTask_IDs_1 = getZoneIDs( iLABOUR_SKILL_CD_1, iNUMBER_PEOPLE_REQ_1, iSCHED_WORK_HRS_1,
            iSCHED_CERT_HRS );

      iTask_IDs_2 = getZoneIDs( iLABOUR_SKILL_CD_2, iNUMBER_PEOPLE_REQ_2, iSCHED_WORK_HRS_2,
            iSCHED_CERT_HRS );

      // verify task_part_map

      simpleIDs lPN_Ids = getAccountIDs( iPN_SER, iMNFACT_SER );
      verifyTASKPARTMAP( iTask_IDs_2, lPN_Ids );

      lPN_Ids = getAccountIDs( iPN_TRK, iMNFACT_TRK );
      verifyTASKPARTMAP( iTask_IDs_1, lPN_Ids );

      // Verify other mxi tables
      // Verify task_work_type
      verifyTASKWORKTYPE( iTask_IDs_1, iWORK_TYPE_LIST_1, iWORK_TYPE_LIST_2 );
      verifyTASKWORKTYPE( iTask_IDs_2, iWORK_TYPE_LIST_1, iWORK_TYPE_LIST_2 );

      // verify task_task table
      simpleIDs lIssueACIds = getAccountIDs( iISSUE_TO_ACCOUNT_CD );

      iTask_DefnIDs_1 = verifyTASKTASK( iTask_IDs_1, iCLASS_CD, iTASK_PRIORITY_CD, iSUBCLASS_CD,
            iORIGINATOR_CD, iJIC_TASK_CD_1, iJIC_TASK_NAME_1, iJIC_TASK_DESC_1, iWORK_INSTRUCTIONS,
            iAPPLICABILITY_DESC, iDOC_REFERENCE, iAPPLICABILITY_RANGE, iENGINEERING_NOTES,
            iEXT_REFERENCE, lIssueACIds );

      iTask_DefnIDs_2 = verifyTASKTASK( iTask_IDs_2, iCLASS_CD, iTASK_PRIORITY_CD, iSUBCLASS_CD,
            iORIGINATOR_CD, iJIC_TASK_CD_2, iJIC_TASK_NAME_2, iJIC_TASK_DESC_2, iWORK_INSTRUCTIONS,
            iAPPLICABILITY_DESC, iDOC_REFERENCE, iAPPLICABILITY_RANGE, iENGINEERING_NOTES,
            iEXT_REFERENCE, lIssueACIds );

      // verify task_defn
      String lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTask_DefnIDs_1.getNO_DB_ID() + " and TASK_DEFN_ID=" + iTask_DefnIDs_1.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTask_DefnIDs_2.getNO_DB_ID() + " and TASK_DEFN_ID=" + iTask_DefnIDs_2.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_task_flags table
      lQuery = "select 1 from " + TableUtil.TASK_TASK_FLAGS + " where TASK_DB_ID="
            + iTask_IDs_1.getNO_DB_ID() + " and TASK_ID=" + iTask_IDs_1.getNO_ID();
      Assert.assertTrue( "Check task_task_flags table to verify the record exists",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.TASK_TASK_FLAGS + " where TASK_DB_ID="
            + iTask_IDs_2.getNO_DB_ID() + " and TASK_ID=" + iTask_IDs_2.getNO_ID();
      Assert.assertTrue( "Check task_task_flags table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_labour_list
      verifyTASKLABOURLIST( iTask_IDs_1, iLABOUR_SKILL_CD_1, iNUMBER_PEOPLE_REQ_1,
            iSCHED_WORK_HRS_1, iSCHED_INSP_HRS, iSCHED_CERT_HRS );

      verifyTASKLABOURLIST( iTask_IDs_2, iLABOUR_SKILL_CD_2, iNUMBER_PEOPLE_REQ_2,
            iSCHED_WORK_HRS_2, iSCHED_INSP_HRS, iSCHED_CERT_HRS );

      // verify task_part_list
      bomPartPN lpart1 = getJicPartFromMxi( iPN_COMHW_1, iMNFACT_COMHW_1 );
      verifyTASKPARTLIST( iTask_IDs_1, lpart1, iREMOVE_BOOL_1_N, iREMOVE_REASON_CD_1,
            iPART_PROVIDER_TYPE_CD, iPART_REQUEST_QT, iREQUEST_ACTION_CD, iINSTALL_BOOL_1_N,
            iREQUEST_PRIORITY_CD );

      bomPartPN lpart2 = getJicPartFromMxi( iPN_COMHW_2, iMNFACT_COMHW_2 );
      verifyTASKPARTLIST( iTask_IDs_2, lpart2, iREMOVE_BOOL_2_N, iREMOVE_REASON_CD_2,
            iPART_PROVIDER_TYPE_CD, iPART_REQUEST_QT, iREQUEST_ACTION_CD, iINSTALL_BOOL_2_N,
            iREQUEST_PRIORITY_CD );

      // verify task_tool_list
      simpleIDs lbomPartIds_1 = getBomPartIds( iPART_GROUP_CD_TOOL_1 );
      verifyTASKTOOLLIST( iTask_IDs_1, lbomPartIds_1, iSCHED_HRS_1 );

      simpleIDs lbomPartIds_2 = getBomPartIds( iPART_GROUP_CD_TOOL_2 );
      verifyTASKTOOLLIST( iTask_IDs_2, lbomPartIds_2, iSCHED_HRS_2 );

      // verify task_parm_data
      String ldataTypeId_1 = getDataTypeIDs( iDOMAIN_TYPE_CD, iMEASUREMENT_CD_1 );
      verifyTASKPARMDATA( iTask_IDs_1, ldataTypeId_1, iMEASUREMENT_ORD_1 );

      String ldataTypeId_2 = getDataTypeIDs( iDOMAIN_TYPE_CD, iMEASUREMENT_CD_2 );
      verifyTASKPARMDATA( iTask_IDs_2, ldataTypeId_2, iMEASUREMENT_ORD_1 );

      // verify task_step
      lQuery = "select 1 from " + TableUtil.TASK_STEP + " where TASK_DB_ID="
            + iTask_IDs_1.getNO_DB_ID() + " and TASK_ID=" + iTask_IDs_1.getNO_ID()
            + " and STEP_ORD='" + iJOB_STEP_ORD_1 + "'";
      Assert.assertTrue( "Check TASK_STEP table to verify the first record exists",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.TASK_STEP + " where TASK_DB_ID="
            + iTask_IDs_2.getNO_DB_ID() + " and TASK_ID=" + iTask_IDs_2.getNO_ID()
            + " and STEP_ORD='" + iJOB_STEP_ORD_1 + "'";
      Assert.assertTrue( "Check TASK_STEP table to verify the second record exists",
            RecordsExist( lQuery ) );

      // verify task_task_ietm
      // Check first ietm
      simpleIDs lIetmIds_1 = getIetmIDs( iIETM_CD_1 );
      String lIetmTopicID_1 = getIetmTopicID( lIetmIds_1, iIETM_TOPIC_1 );

      lQuery = "select 1 from " + TableUtil.TASK_TASK_IETM + " where TASK_DB_ID="
            + iTask_IDs_1.getNO_DB_ID() + " and TASK_ID=" + iTask_IDs_1.getNO_ID()
            + " and IETM_DB_ID=" + lIetmIds_1.getNO_DB_ID() + " and IETM_ID="
            + lIetmIds_1.getNO_ID() + " and IETM_TOPIC_ID='" + lIetmTopicID_1 + "' and IETM_ORD='"
            + iIETM_ORDER_1 + "'";
      Assert.assertTrue( "Check TASK_TASK_IETM table to verify the first record exists",
            RecordsExist( lQuery ) );

      // Check second ietm
      simpleIDs lIetmIds_2 = getIetmIDs( iIETM_CD_2 );
      String lIetmTopicID_2 = getIetmTopicID( lIetmIds_2, iIETM_TOPIC_2 );

      lQuery = "select 1 from " + TableUtil.TASK_TASK_IETM + " where TASK_DB_ID="
            + iTask_IDs_2.getNO_DB_ID() + " and TASK_ID=" + iTask_IDs_2.getNO_ID()
            + " and IETM_DB_ID=" + lIetmIds_2.getNO_DB_ID() + " and IETM_ID="
            + lIetmIds_2.getNO_ID() + " and IETM_TOPIC_ID='" + lIetmTopicID_2
            + "' and IETM_ORD='1'";
      Assert.assertTrue( "Check TASK_TASK_IETM table to verify the second record exists",
            RecordsExist( lQuery ) );

   }


   /**
    * This test is to verify that a Component JIC Step with a description less than the configured
    * length limit passes validation.
    */
   @Test
   public void testCompJicStepDescriptionValidation_Success() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // Set up the description length limit (config parm).
      //
      // Note: because we are inserting test data into a clob, that test data must not exceed 4000
      // characters (DB limit for a clob).
      int originalLimit = setStepDefinitionDescriptionLimit( STEP_DEFN_DESC_LENGTH_LIMIT );

      // Test step description with a length less than the limit.
      String lStepDescription = StringUtils.repeat( "a", STEP_DEFN_DESC_LENGTH_LIMIT - 1 );

      prepareDataONJIC( iJIC_TASK_CD_1, iJIC_TASK_NAME_1, iJIC_TASK_DESC_1 );
      prepareDataONJICASSIGNEDPART( iJIC_TASK_CD_1, iPN_SER, iMNFACT_SER );
      prepareDataONJICSTEP( iJIC_TASK_CD_1, iJOB_STEP_ORD_1, lStepDescription );

      // run validation
      int lReturnCode = runValidationAndImport( true, true );

      setStepDefinitionDescriptionLimit( originalLimit );

      Assert.assertEquals( 1, lReturnCode );
   }


   /**
    * This test is to verify that during validation, a Component JIC Step with a description greater
    * than the configured length limit results in an error code CFG_COMP_JIC_10023: "Job Step
    * Description length cannot exceed the length limit".
    */
   @Test
   public void testCompJicStepDescriptionValidation__CFG_COMP_JIC_10023() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      String lErrorCode = "CFG_COMP_JIC-10023";

      // Set up the description length limit (config parm).
      //
      // Note: because we are inserting test data into a clob, that test data must not exceed 4000
      // characters (DB limit for a clob).
      int originalLimit = setStepDefinitionDescriptionLimit( STEP_DEFN_DESC_LENGTH_LIMIT );

      // Test step description with a length greater than the limit.
      String lStepDescription = StringUtils.repeat( "a", STEP_DEFN_DESC_LENGTH_LIMIT + 1 );

      prepareDataONJIC( iJIC_TASK_CD_1, iJIC_TASK_NAME_1, iJIC_TASK_DESC_1 );
      prepareDataONJICSTEP( iJIC_TASK_CD_1, iJOB_STEP_ORD_1, lStepDescription );

      // run validation
      int lReturnCode = runValidationAndImport( true, true );

      setStepDefinitionDescriptionLimit( originalLimit );

      // retrieve erroneous data and then assert
      Assert.assertEquals( -1, lReturnCode );
      List<String> lFields = Arrays.asList( FIELD_RESULT_CD );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( FIELD_RESULT_CD, lErrorCode );

      ResultSet lQs =
            runQuery( TableUtil.buildTableQuery( TableUtil.C_COMP_JIC_STEP, lFields, lArgs ) );
      lQs.next();
      Assert.assertEquals( "Unexpected error code returned.", lErrorCode,
            lQs.getString( FIELD_RESULT_CD ) );
   }


   /**
    *
    * To test OPER-30664 - this fix will correct the order of IETM assigned to particular task. It
    * will re-assign a sequential number during import.
    *
    * @throws Exception
    */
   @Test
   public void testJIC_COMP_IETM_OPER_30664_VALIDATION() throws Exception {

      Map<String, String> lc_JICMap = new LinkedHashMap<>();

      lc_JICMap.put( "JIC_TASK_CD", "'" + iJIC_TASK_CD_1 + "'" );
      lc_JICMap.put( "JIC_TASK_NAME", "'" + iJIC_TASK_NAME_1 + "'" );
      lc_JICMap.put( "JIC_TASK_DESC", "'" + iJIC_TASK_DESC_1 + "'" );
      lc_JICMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lc_JICMap.put( "ETOPS_SIGNIFICANT_BOOL", "'" + iETOPS_SIGNIFICANT_BOOL + "'" );
      lc_JICMap.put( "EST_DURATION_HRS", "'" + iEST_DURATION_HRS + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_JIC, lc_JICMap ) );

      lc_JICMap.clear();
      prepareDataONJICASSIGNEDPART( iJIC_TASK_CD_1, iPN_TRK, iMNFACT_TRK );

      // C_JIC_IETM_TOPIC #1
      lc_JICMap.clear();
      lc_JICMap.put( "JIC_TASK_CD", "'" + iJIC_TASK_CD_1 + "'" );
      lc_JICMap.put( "IETM_CD", "'" + iIETM_CD_1 + "'" );
      lc_JICMap.put( "IETM_TOPIC", "'" + iIETM_TOPIC_1 + "'" );
      lc_JICMap.put( "IETM_ORDER", "'" + iIETM_ORDER_1 + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_JIC_IETM, lc_JICMap ) );

      // C_JIC_IETM_TOPIC #2
      lc_JICMap.clear();
      lc_JICMap.put( "JIC_TASK_CD", "'" + iJIC_TASK_CD_1 + "'" );
      lc_JICMap.put( "IETM_CD", "'" + iIETM_CD_2 + "'" );
      lc_JICMap.put( "IETM_TOPIC", "'" + iIETM_TOPIC_2 + "'" );
      lc_JICMap.put( "IETM_ORDER", "'" + iIETM_ORDER_2 + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_JIC_IETM, lc_JICMap ) );

      // C_JIC_IETM_TOPIC #3
      lc_JICMap.clear();
      lc_JICMap.put( "JIC_TASK_CD", "'" + iJIC_TASK_CD_1 + "'" );
      lc_JICMap.put( "IETM_CD", "'" + iIETM_CD_3 + "'" );
      lc_JICMap.put( "IETM_TOPIC", "'" + iIETM_TOPIC_3 + "'" );
      lc_JICMap.put( "IETM_ORDER", "'" + iIETM_ORDER_3 + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_JIC_IETM, lc_JICMap ) );

      // C_JIC_IETM_TOPIC #4
      lc_JICMap.clear();
      lc_JICMap.put( "JIC_TASK_CD", "'" + iJIC_TASK_CD_1 + "'" );
      lc_JICMap.put( "IETM_CD", "'" + iIETM_CD_4 + "'" );
      lc_JICMap.put( "IETM_TOPIC", "'" + iIETM_TOPIC_4 + "'" );
      lc_JICMap.put( "IETM_ORDER", "'" + iIETM_ORDER_4 + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_JIC_IETM, lc_JICMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    *
    * To test OPER-30664 - this fix will correct the order of IETM assigned to particular task. It
    * will re-assign a sequential number during import.
    *
    * @throws Exception
    */
   @Test
   public void testJIC_COMP_IETM_OPER_30664_IMPORT() throws Exception {

      System.out.println( "=======Starting: " + testName.getMethodName()
            + " Validation========================" );

      testJIC_COMP_IETM_OPER_30664_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTask_IDs_1 = null;

      verifyIETMs( iJIC_TASK_CD_1, iIETM_CD_1, 1 );
      verifyIETMs( iJIC_TASK_CD_1, iIETM_CD_2, 2 );
      verifyIETMs( iJIC_TASK_CD_1, iIETM_CD_3, 4 );
      verifyIETMs( iJIC_TASK_CD_1, iIETM_CD_4, 3 );
   }


   // ==================================================================

   /**
    * Verify the IETM order number.
    *
    * @param iJIC_TASK_CD_1
    *           - Task it is assigned to
    * @param iJIC_IETM_CD_1
    *           - IETM it is assigned to
    * @param aIETM_ord
    *           - expected number assigned to the particular IETM
    */
   private void verifyIETMs( String aJIC_TASK_CD_1, String aJIC_IETM_CD_1, int aIETM_ord ) {
      String lQuery;
      simpleIDs lIetm_IDs = null;

      if ( iTask_IDs_1 == null ) {
         lQuery =
               "select task_db_id, task_id from task_task where task_cd = '" + aJIC_TASK_CD_1 + "'";
         iTask_IDs_1 = getIDs( lQuery, "TASK_DB_ID", "TASK_ID" );
      }
      lQuery = "select ietm_db_id, ietm_id from ietm_ietm where ietm_ietm.ietm_cd = '"
            + aJIC_IETM_CD_1 + "'";
      lIetm_IDs = getIDs( lQuery, "IETM_DB_ID", "IETM_ID" );

      lQuery =
            "select ietm_ord from task_task_ietm where  task_db_id = '" + iTask_IDs_1.getNO_DB_ID()
                  + "' and task_id = '" + iTask_IDs_1.getNO_ID() + "' and ietm_db_id = '"
                  + lIetm_IDs.getNO_DB_ID() + "' and ietm_id = '" + lIetm_IDs.getNO_ID() + "'";
      Assert.assertTrue( "IETM order value is incorrect.",
            getIntValueFromQuery( lQuery, "IETM_ORD" ) == aIETM_ord );
   }


   /**
    * This function is to insert data into staging table:C_COMP_JIC.
    *
    *
    *
    */
   public void prepareDataONJIC( String aJIC_TASK_CD, String aJIC_TASK_NAME,
         String aJIC_TASK_DESC ) {

      // c_comp_jic
      Map<String, String> lc_JICMap = new LinkedHashMap<>();

      lc_JICMap.put( "JIC_TASK_CD", "'" + aJIC_TASK_CD + "'" );
      lc_JICMap.put( "JIC_TASK_NAME", "'" + aJIC_TASK_NAME + "'" );
      lc_JICMap.put( "JIC_TASK_DESC", "'" + aJIC_TASK_DESC + "'" );
      lc_JICMap.put( "EXT_REFERENCE", "'" + iEXT_REFERENCE + "'" );
      lc_JICMap.put( "CLASS_CD", "'" + iCLASS_CD + "'" );
      lc_JICMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );
      lc_JICMap.put( "ORIGINATOR_CD", "'" + iORIGINATOR_CD + "'" );
      lc_JICMap.put( "DOC_REFERENCE", "'" + iDOC_REFERENCE + "'" );
      lc_JICMap.put( "ETOPS_SIGNIFICANT_BOOL", "'" + iETOPS_SIGNIFICANT_BOOL + "'" );
      lc_JICMap.put( "APPLICABILITY_RANGE", "'" + iAPPLICABILITY_RANGE + "'" );
      lc_JICMap.put( "APPLICABILITY_DESC", "'" + iAPPLICABILITY_DESC + "'" );
      lc_JICMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      lc_JICMap.put( "TASK_PRIORITY_CD", "'" + iTASK_PRIORITY_CD + "'" );
      lc_JICMap.put( "ISSUE_TO_ACCOUNT_CD", "'" + iISSUE_TO_ACCOUNT_CD + "'" );
      lc_JICMap.put( "WORK_INSTRUCTIONS", "'" + iWORK_INSTRUCTIONS + "'" );
      lc_JICMap.put( "EST_DURATION_HRS", "'" + iEST_DURATION_HRS + "'" );
      lc_JICMap.put( "ENGINEERING_NOTES", "'" + iENGINEERING_NOTES + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_JIC, lc_JICMap ) );

      lc_JICMap.clear();

   }


   /**
    * This function is to insert data into staging table:C_COMP_JIC_ASSIGNED_PART.
    *
    *
    *
    */
   public void prepareDataONJICASSIGNEDPART( String aJIC_TASK_CD, String aPN, String aMNFACT ) {

      // c_comp_jic_assigned_part
      Map<String, String> lc_JICMap = new LinkedHashMap<>();

      lc_JICMap.put( "JIC_TASK_CD", "'" + aJIC_TASK_CD + "'" );
      lc_JICMap.put( "PART_NO_OEM", "'" + aPN + "'" );
      lc_JICMap.put( "MANUFACT_CD", "'" + aMNFACT + "'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_COMP_JIC_ASSIGNED_PART, lc_JICMap ) );

      lc_JICMap.clear();

   }


   /**
    * This function is to insert data into staging table:C_COMP_JIC_LABOUR.
    *
    *
    *
    */
   public void prepareDataONJICLABOUR( String aJIC_TASK_CD, String aLABOUR_SKILL_CD,
         String aNUMBER_PEOPLE_REQ, String aSCHED_WORK_HRS ) {

      // c_comp_jic_labour
      Map<String, String> lc_JICMap = new LinkedHashMap<>();

      lc_JICMap.put( "JIC_TASK_CD", "'" + aJIC_TASK_CD + "'" );
      lc_JICMap.put( "LABOUR_SKILL_CD", "'" + aLABOUR_SKILL_CD + "'" );
      lc_JICMap.put( "NUMBER_PEOPLE_REQ", "'" + aNUMBER_PEOPLE_REQ + "'" );
      lc_JICMap.put( "SCHED_WORK_HRS", "'" + aSCHED_WORK_HRS + "'" );
      lc_JICMap.put( "SCHED_CERT_HRS", "'" + iSCHED_CERT_HRS + "'" );
      lc_JICMap.put( "SCHED_INSP_HRS", "'" + iSCHED_INSP_HRS + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_JIC_LABOUR, lc_JICMap ) );

      lc_JICMap.clear();

   }


   /**
    * This function is to insert data into staging table:C_COMP_JIC_COMHW.
    *
    *
    *
    */
   public void prepareDataONJICCOMHW( String aJIC_TASK_CD, String aPART_GROUP_CD,
         String aPART_NO_OEM, String aMANUFACT_CD, String aINSTALL_BOOL, String aREMOVE_BOOL,
         String aREMOVE_REASON_CD ) {

      // c_comp_jic_comhw
      Map<String, String> lc_JICMap = new LinkedHashMap<>();

      lc_JICMap.put( "JIC_TASK_CD", "'" + aJIC_TASK_CD + "'" );
      lc_JICMap.put( "PART_GROUP_CD", "'" + aPART_GROUP_CD + "'" );
      lc_JICMap.put( "PART_REQUEST_QT", "'" + iPART_REQUEST_QT + "'" );
      lc_JICMap.put( "PART_NO_OEM", "'" + aPART_NO_OEM + "'" );
      lc_JICMap.put( "MANUFACT_CD", "'" + aMANUFACT_CD + "'" );
      lc_JICMap.put( "INSTALL_BOOL", "'" + aINSTALL_BOOL + "'" );
      lc_JICMap.put( "REMOVE_BOOL", "'" + aREMOVE_BOOL + "'" );

      if ( aREMOVE_REASON_CD != null ) {
         lc_JICMap.put( "REMOVE_REASON_CD", "'" + aREMOVE_REASON_CD + "'" );
      }

      lc_JICMap.put( "REQUEST_PRIORITY_CD", "'" + iREQUEST_PRIORITY_CD + "'" );
      lc_JICMap.put( "REQUEST_ACTION_CD", "'" + iREQUEST_ACTION_CD + "'" );
      lc_JICMap.put( "PART_PROVIDER_TYPE_CD", "'" + iPART_PROVIDER_TYPE_CD + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_JIC_COMHW, lc_JICMap ) );

      lc_JICMap.clear();

   }


   /**
    * This function is to insert data into staging table:C_COMP_JIC_TOOL.
    *
    *
    *
    */
   public void prepareDataONJICTOOL( String aJIC_TASK_CD, String aPART_GROUP_CD,
         String aSCHED_HRS ) {

      // c_comp_jic_tool
      Map<String, String> lc_JICMap = new LinkedHashMap<>();

      lc_JICMap.put( "JIC_TASK_CD", "'" + aJIC_TASK_CD + "'" );
      lc_JICMap.put( "PART_GROUP_CD", "'" + aPART_GROUP_CD + "'" );
      lc_JICMap.put( "SCHED_HRS", "'" + aSCHED_HRS + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_JIC_TOOL, lc_JICMap ) );

      lc_JICMap.clear();

   }


   /**
    * This function is to insert data into staging table:C_COMP_JIC_MEASUREMENT.
    *
    *
    *
    */
   public void prepareDataONJICMEASUREMENT( String aJIC_TASK_CD, String aMEASUREMENT_ORD,
         String aMEASUREMENT_CD ) {

      // c_comp_jic_measurement
      Map<String, String> lc_JICMap = new LinkedHashMap<>();

      lc_JICMap.put( "JIC_TASK_CD", "'" + aJIC_TASK_CD + "'" );
      lc_JICMap.put( "MEASUREMENT_ORD", "'" + aMEASUREMENT_ORD + "'" );
      lc_JICMap.put( "MEASUREMENT_CD", "'" + aMEASUREMENT_CD + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_JIC_MEASUREMENT, lc_JICMap ) );

      lc_JICMap.clear();

   }


   /**
    * This function is to insert data into staging table:C_COMP_JIC_STEP.
    *
    *
    *
    */
   public void prepareDataONJICSTEP( String aJIC_TASK_CD, String aJOB_STEP_ORD,
         String aJOB_STEP_DESC ) {

      // c_comp_jic_step
      Map<String, String> lc_JICMap = new LinkedHashMap<>();

      lc_JICMap.put( "JIC_TASK_CD", "'" + aJIC_TASK_CD + "'" );
      lc_JICMap.put( "JOB_STEP_ORD", "'" + aJOB_STEP_ORD + "'" );
      lc_JICMap.put( "JOB_STEP_DESC", "'" + aJOB_STEP_DESC + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_JIC_STEP, lc_JICMap ) );

      lc_JICMap.clear();

   }


   /**
    * This function is to insert data into staging table:C_COMP_JIC_IETM.
    *
    *
    *
    */
   public void prepareDataONJICIETM( String aJIC_TASK_CD, String aIETM_CD, String aIETM_TOPIC,
         String aIETM_ORDER ) {

      // c_comp_jic_step
      Map<String, String> lc_JICMap = new LinkedHashMap<>();

      lc_JICMap.put( "JIC_TASK_CD", "'" + aJIC_TASK_CD + "'" );
      lc_JICMap.put( "IETM_CD", "'" + aIETM_CD + "'" );
      lc_JICMap.put( "IETM_TOPIC", "'" + aIETM_TOPIC + "'" );
      lc_JICMap.put( "IETM_ORDER", "'" + aIETM_ORDER + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_COMP_JIC_IETM, lc_JICMap ) );

      lc_JICMap.clear();

   }


   /**
    * This function is to retrieve task IDs from task_labour_list table.
    *
    *
    */
   public simpleIDs getZoneIDs( String aLABOUR_SKILL_CD, String aMAN_PWR_CT, String aWORK_PERF_HR,
         String aCERT_HR ) {

      String[] iIds = { "TASK_DB_ID", "TASK_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "LABOUR_SKILL_CD", aLABOUR_SKILL_CD );
      lArgs.addArguments( "MAN_PWR_CT", aMAN_PWR_CT );
      lArgs.addArguments( "WORK_PERF_HR", aWORK_PERF_HR );
      lArgs.addArguments( "CERT_HR", aCERT_HR );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_LABOUR_LIST, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lIds;

   }


   /**
    * This function is to retrieve account IDs from FNC_ACCOUNT table.
    *
    *
    */
   public simpleIDs getAccountIDs( String aACCOUNT_CD ) {

      String[] iIds = { "ACCOUNT_DB_ID", "ACCOUNT_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "ACCOUNT_CD", aACCOUNT_CD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.FNC_ACCOUNT, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lIds;

   }


   /**
    * This function is to retrieve part_no IDs from EQP_PART_NO table.
    *
    *
    */
   public simpleIDs getAccountIDs( String aPN_OEM, String aMNFACT ) {

      String[] iIds = { "PART_NO_DB_ID", "PART_NO_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "PART_NO_OEM", aPN_OEM );
      lArgs.addArguments( "MANUFACT_CD", aMNFACT );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EQP_PART_NO, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lIds;

   }


   /**
    * This function is to verify TASK_WORK_TYPE table
    *
    *
    */
   public void verifyTASKWORKTYPE( simpleIDs aTaskIDs, String aWorkType1, String aWorkType2 ) {

      String[] iIds = { "WORK_TYPE_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_DB_ID", aTaskIDs.getNO_DB_ID() );
      lArgs.addArguments( "TASK_ID", aTaskIDs.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_WORK_TYPE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "There are should be 2 records: ", llists.size() == 2 );

      for ( int i = 0; i < llists.size(); i++ ) {
         Assert.assertTrue( "Check WORK TYPE CD",
               llists.get( i ).get( 0 ).equalsIgnoreCase( aWorkType1 )
                     || llists.get( i ).get( 0 ).equalsIgnoreCase( aWorkType2 ) );

      }

   }


   /**
    * This function is to verify TASK_TASK and retrieve task defn IDs table
    *
    *
    */
   public simpleIDs verifyTASKTASK( simpleIDs aTaskIds, String aTaskClassCD, String aTaskPriorityCD,
         String aTaskSubClassCD, String aTaskOriginatorCD, String aTaskCD, String aTaskName,
         String aTaskLdesc, String aInstructionLdesc, String aTaskApplLdesc, String aTaskRefSdesc,
         String aTaskApplEffLdesc, String aEngineeringLdesc, String aExtKeySdesc,
         simpleIDs aIssueACIds ) {

      String[] iIds = { "TASK_DEFN_DB_ID", "TASK_DEFN_ID", "TASK_CLASS_CD", "TASK_PRIORITY_CD",
            "TASK_SUBCLASS_CD", "TASK_ORIGINATOR_CD", "TASK_CD", "TASK_NAME", "TASK_LDESC",
            "INSTRUCTION_LDESC", "TASK_APPL_LDESC", "TASK_REF_SDESC", "TASK_APPL_EFF_LDESC",
            "ENGINEERING_LDESC", "EXT_KEY_SDESC", "ISSUE_ACCOUNT_DB_ID", "ISSUE_ACCOUNT_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_DB_ID", aTaskIds.getNO_DB_ID() );
      lArgs.addArguments( "TASK_ID", aTaskIds.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_TASK, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      Assert.assertTrue( "TASK_CLASS_CD",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aTaskClassCD ) );
      Assert.assertTrue( "TASK_PRIORITY_CD",
            llists.get( 0 ).get( 3 ).equalsIgnoreCase( aTaskPriorityCD ) );
      Assert.assertTrue( "TASK_SUBCLASS_CD",
            llists.get( 0 ).get( 4 ).equalsIgnoreCase( aTaskSubClassCD ) );
      Assert.assertTrue( "TASK_ORIGINATOR_CD",
            llists.get( 0 ).get( 5 ).equalsIgnoreCase( aTaskOriginatorCD ) );
      Assert.assertTrue( "TASK_CD", llists.get( 0 ).get( 6 ).equalsIgnoreCase( aTaskCD ) );
      Assert.assertTrue( "TASK_NAME", llists.get( 0 ).get( 7 ).equalsIgnoreCase( aTaskName ) );
      Assert.assertTrue( "TASK_LDESC", llists.get( 0 ).get( 8 ).equalsIgnoreCase( aTaskLdesc ) );
      Assert.assertTrue( "INSTRUCTION_LDESC",
            llists.get( 0 ).get( 9 ).equalsIgnoreCase( aInstructionLdesc ) );
      Assert.assertTrue( "TASK_APPL_LDESC",
            llists.get( 0 ).get( 10 ).equalsIgnoreCase( aTaskApplLdesc ) );
      Assert.assertTrue( "TASK_REF_SDESC",
            llists.get( 0 ).get( 11 ).equalsIgnoreCase( aTaskRefSdesc ) );
      Assert.assertTrue( "TASK_APPL_EFF_LDESC",
            llists.get( 0 ).get( 12 ).equalsIgnoreCase( aTaskApplEffLdesc ) );
      Assert.assertTrue( "ENGINEERING_LDESC",
            llists.get( 0 ).get( 13 ).equalsIgnoreCase( aEngineeringLdesc ) );
      Assert.assertTrue( "EXT_KEY_SDESC",
            llists.get( 0 ).get( 14 ).equalsIgnoreCase( aExtKeySdesc ) );
      Assert.assertTrue( "ISSUE_ACCOUNT_DB_ID",
            llists.get( 0 ).get( 15 ).equalsIgnoreCase( aIssueACIds.getNO_DB_ID() ) );
      Assert.assertTrue( "ISSUE_ACCOUNT_ID",
            llists.get( 0 ).get( 16 ).equalsIgnoreCase( aIssueACIds.getNO_ID() ) );

      return lIds;

   }


   /**
    * This function is to verify TASK_PART_MAP table
    *
    *
    */
   public void verifyTASKPARTMAP( simpleIDs aTaskIds, simpleIDs aPNIDs ) {

      String[] iIds = { "PART_NO_DB_ID", "PART_NO_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_DB_ID", aTaskIds.getNO_DB_ID() );
      lArgs.addArguments( "TASK_ID", aTaskIds.getNO_ID() );
      lArgs.addArguments( "PART_NO_DB_ID", aPNIDs.getNO_DB_ID() );
      lArgs.addArguments( "PART_NO_ID", aPNIDs.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_PART_MAP, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "There sould be at least one record", llists.size() >= 1 );

      // Assert.assertTrue( "PART_NO_ID",
      // llists.get( 0 ).get( 1 ).equalsIgnoreCase( aPNIDs.getNO_ID() ) );

   }


   /**
    * This function is to verify TASK_LABOUR_LIST and retrieve task defn IDs table
    *
    *
    */
   public void verifyTASKLABOURLIST( simpleIDs aTaskIds, String aLabourSkillCD, String aManPerCt,
         String aWorkPerfHR, String aInspHR, String aCertHR ) {

      String[] iIds = { "MAN_PWR_CT", "WORK_PERF_HR", "INSP_HR", "CERT_HR" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_DB_ID", aTaskIds.getNO_DB_ID() );
      lArgs.addArguments( "TASK_ID", aTaskIds.getNO_ID() );
      lArgs.addArguments( "LABOUR_SKILL_CD", aLabourSkillCD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_LABOUR_LIST, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "MAN_PWR_CT", llists.get( 0 ).get( 0 ).equalsIgnoreCase( aManPerCt ) );
      Assert.assertTrue( "WORK_PERF_HR", llists.get( 0 ).get( 1 ).equalsIgnoreCase( aWorkPerfHR ) );
      Assert.assertTrue( "INSP_HR", llists.get( 0 ).get( 2 ).equalsIgnoreCase( aInspHR ) );
      Assert.assertTrue( "CERT_HR", llists.get( 0 ).get( 3 ).equalsIgnoreCase( aCertHR ) );

   }


   /**
    * This function is to retrieve jic part information from Mxi tables
    *
    *
    */
   public bomPartPN getJicPartFromMxi( String aPNOEM, String sMANFT ) {

      String lQueryString =
            "select eqp_bom_part.bom_part_db_id, eqp_bom_part.bom_part_id,eqp_part_no.part_no_db_id, eqp_part_no.part_no_id "
                  + " from eqp_assmbl_bom " + " inner join eqp_assmbl_pos on "
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
                  + "where eqp_part_no.part_no_oem='" + aPNOEM + "' and eqp_part_no.manufact_cd='"
                  + sMANFT + "'";

      String[] iIds = { "BOM_PART_DB_ID", "BOM_PART_ID", "PART_NO_DB_ID", "PART_NO_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      List<ArrayList<String>> llists = execute( lQueryString, lfields );

      bomPartPN lpartInfor = new bomPartPN( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ),
            llists.get( 0 ).get( 2 ), llists.get( 0 ).get( 3 ) );

      return lpartInfor;

   }


   /**
    * This function is to verify task_part_list tables
    *
    *
    */

   public void verifyTASKPARTLIST( simpleIDs aTaskIds, bomPartPN aPart, String aREMOVE_BOOL,
         String aREMOVE_REASON_CD, String aPART_PROVIDER_TYPE_CD, String aREQ_QT,
         String aREQUEST_ACTION_CD, String aINSTALL_BOOL, String aREQUEST_PRIORITY_CD ) {

      String[] iIds = { "REMOVE_BOOL", "REMOVE_REASON_CD", "PART_PROVIDER_TYPE_CD", "REQ_QT",
            "REQ_ACTION_CD", "INSTALL_BOOL", "REQ_PRIORITY_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_DB_ID", aTaskIds.getNO_DB_ID() );
      lArgs.addArguments( "TASK_ID", aTaskIds.getNO_ID() );
      lArgs.addArguments( "BOM_PART_DB_ID", aPart.getBOM_PART_DB_ID() );
      lArgs.addArguments( "BOM_PART_ID", aPart.getBOM_PART_ID() );
      lArgs.addArguments( "SPEC_PART_NO_DB_ID", aPart.getPART_NO_DB_ID() );
      lArgs.addArguments( "SPEC_PART_NO_ID", aPart.getPART_NO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_PART_LIST, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "REMOVE_BOOL", llists.get( 0 ).get( 0 ).equalsIgnoreCase( aREMOVE_BOOL ) );
      if ( aREMOVE_REASON_CD != null ) {
         Assert.assertTrue( "REMOVE_REASON_CD",
               llists.get( 0 ).get( 1 ).equalsIgnoreCase( aREMOVE_REASON_CD ) );
      }
      Assert.assertTrue( "PART_PROVIDER_TYPE_CD",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aPART_PROVIDER_TYPE_CD ) );
      Assert.assertTrue( "REQ_QT", llists.get( 0 ).get( 3 ).equalsIgnoreCase( aREQ_QT ) );
      Assert.assertTrue( "REQ_ACTION_CD",
            llists.get( 0 ).get( 4 ).equalsIgnoreCase( aREQUEST_ACTION_CD ) );
      Assert.assertTrue( "INSTALL_BOOL",
            llists.get( 0 ).get( 5 ).equalsIgnoreCase( aINSTALL_BOOL ) );
      Assert.assertTrue( "REQ_PRIORITY_CD",
            llists.get( 0 ).get( 6 ).equalsIgnoreCase( aREQUEST_PRIORITY_CD ) );

   }


   /**
    * This function is to retrieve bom part Ids from EQP table.
    *
    *
    */
   public simpleIDs getBomPartIds( String aPART_GROUP_CD_TOOL ) {

      String[] iIds = { "BOM_PART_DB_ID", "BOM_PART_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "BOM_PART_CD", aPART_GROUP_CD_TOOL );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EQP_BOM_PART, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lIds;

   }


   /**
    * This function is to verify task_tool_list tables
    *
    *
    */

   public void verifyTASKTOOLLIST( simpleIDs aTaskIds, simpleIDs abomPartIds, String aSCHED_HR ) {

      String[] iIds = { "SCHED_HR" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_DB_ID", aTaskIds.getNO_DB_ID() );
      lArgs.addArguments( "TASK_ID", aTaskIds.getNO_ID() );
      lArgs.addArguments( "BOM_PART_DB_ID", abomPartIds.getNO_DB_ID() );
      lArgs.addArguments( "BOM_PART_ID", abomPartIds.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_TOOL_LIST, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "SCHED_HR", llists.get( 0 ).get( 0 ).equalsIgnoreCase( aSCHED_HR ) );

   }


   /**
    * This function is to retrieve data type id from mim_data_type table.
    *
    *
    */

   public String getDataTypeIDs( String aDOMAIN_TYPE_CD, String aDATA_TYPE_CD ) {

      String[] iIds = { "DATA_TYPE_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "DOMAIN_TYPE_CD", aDOMAIN_TYPE_CD );
      lArgs.addArguments( "DATA_TYPE_CD", aDATA_TYPE_CD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.MIM_DATA_TYPE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      String lId = llists.get( 0 ).get( 0 );

      return lId;

   }


   public void verifyTASKPARMDATA( simpleIDs aTaskIds, String aDATA_TYPE_ID, String aDATA_ORD ) {

      String[] iIds = { "DATA_ORD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_DB_ID", aTaskIds.getNO_DB_ID() );
      lArgs.addArguments( "TASK_ID", aTaskIds.getNO_ID() );
      lArgs.addArguments( "DATA_TYPE_ID", aDATA_TYPE_ID );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_PARM_DATA, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "DATA_ORD", llists.get( 0 ).get( 0 ).equalsIgnoreCase( aDATA_ORD ) );

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
    * Calls check sensitivity error code
    *
    *
    */
   protected void checkCOMPJICErrorCode( String aTestName, String aValidationCode ) {

      boolean lFound = false;
      String lerror_desc = null;
      String lerrorMsg = null;

      String lquery = TestConstants.BL_COMJIC_ERROR_CHECK;

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
    * This function is to verify the Mxi tables updated correctly after import.
    *
    *
    */

   public void verifyALL() {

      // Verify task_work_type
      verifyTASKWORKTYPE( iTask_IDs_1, iWORK_TYPE_LIST_1, iWORK_TYPE_LIST_2 );

      // verify task_task table
      simpleIDs lIssueACIds = getAccountIDs( iISSUE_TO_ACCOUNT_CD );

      iTask_DefnIDs_1 = verifyTASKTASK( iTask_IDs_1, iCLASS_CD, iTASK_PRIORITY_CD, iSUBCLASS_CD,
            iORIGINATOR_CD, iJIC_TASK_CD_1, iJIC_TASK_NAME_1, iJIC_TASK_DESC_1, iWORK_INSTRUCTIONS,
            iAPPLICABILITY_DESC, iDOC_REFERENCE, iAPPLICABILITY_RANGE, iENGINEERING_NOTES,
            iEXT_REFERENCE, lIssueACIds );

      // verify task_defn
      String lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTask_DefnIDs_1.getNO_DB_ID() + " and TASK_DEFN_ID=" + iTask_DefnIDs_1.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_task_flags table
      lQuery = "select 1 from " + TableUtil.TASK_TASK_FLAGS + " where TASK_DB_ID="
            + iTask_IDs_1.getNO_DB_ID() + " and TASK_ID=" + iTask_IDs_1.getNO_ID();
      Assert.assertTrue( "Check task_task_flags table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_labour_list
      verifyTASKLABOURLIST( iTask_IDs_1, iLABOUR_SKILL_CD_1, iNUMBER_PEOPLE_REQ_1,
            iSCHED_WORK_HRS_1, iSCHED_INSP_HRS, iSCHED_CERT_HRS );

      verifyTASKLABOURLIST( iTask_IDs_1, iLABOUR_SKILL_CD_2, iNUMBER_PEOPLE_REQ_2,
            iSCHED_WORK_HRS_2, iSCHED_INSP_HRS, iSCHED_CERT_HRS );

      // verify task_part_list
      bomPartPN lpart1 = getJicPartFromMxi( iPN_COMHW_1, iMNFACT_COMHW_1 );
      verifyTASKPARTLIST( iTask_IDs_1, lpart1, iREMOVE_BOOL_1_N, iREMOVE_REASON_CD_1,
            iPART_PROVIDER_TYPE_CD, iPART_REQUEST_QT, iREQUEST_ACTION_CD, iINSTALL_BOOL_1_N,
            iREQUEST_PRIORITY_CD );

      bomPartPN lpart2 = getJicPartFromMxi( iPN_COMHW_2, iMNFACT_COMHW_2 );
      verifyTASKPARTLIST( iTask_IDs_1, lpart2, iREMOVE_BOOL_2_N, iREMOVE_REASON_CD_2,
            iPART_PROVIDER_TYPE_CD, iPART_REQUEST_QT, iREQUEST_ACTION_CD, iINSTALL_BOOL_2_N,
            iREQUEST_PRIORITY_CD );

      // verify task_tool_list
      simpleIDs lbomPartIds_1 = getBomPartIds( iPART_GROUP_CD_TOOL_1 );
      verifyTASKTOOLLIST( iTask_IDs_1, lbomPartIds_1, iSCHED_HRS_1 );

      simpleIDs lbomPartIds_2 = getBomPartIds( iPART_GROUP_CD_TOOL_2 );
      verifyTASKTOOLLIST( iTask_IDs_1, lbomPartIds_2, iSCHED_HRS_2 );

      // verify task_parm_data
      String ldataTypeId_1 = getDataTypeIDs( iDOMAIN_TYPE_CD, iMEASUREMENT_CD_1 );
      verifyTASKPARMDATA( iTask_IDs_1, ldataTypeId_1, iMEASUREMENT_ORD_1 );

      String ldataTypeId_2 = getDataTypeIDs( iDOMAIN_TYPE_CD, iMEASUREMENT_CD_2 );
      verifyTASKPARMDATA( iTask_IDs_1, ldataTypeId_2, iMEASUREMENT_ORD_2 );

      // verify task_step
      lQuery = "select 1 from " + TableUtil.TASK_STEP + " where TASK_DB_ID="
            + iTask_IDs_1.getNO_DB_ID() + " and TASK_ID=" + iTask_IDs_1.getNO_ID()
            + " and STEP_ORD='" + iJOB_STEP_ORD_1 + "'";
      Assert.assertTrue( "Check TASK_STEP table to verify the first record exists",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.TASK_STEP + " where TASK_DB_ID="
            + iTask_IDs_1.getNO_DB_ID() + " and TASK_ID=" + iTask_IDs_1.getNO_ID()
            + " and STEP_ORD='" + iJOB_STEP_ORD_2 + "'";
      Assert.assertTrue( "Check TASK_STEP table to verify the second record exists",
            RecordsExist( lQuery ) );

      // verify task_task_ietm
      // Check first ietm
      simpleIDs lIetmIds_1 = getIetmIDs( iIETM_CD_1 );
      String lIetmTopicID_1 = getIetmTopicID( lIetmIds_1, iIETM_TOPIC_1 );

      lQuery = "select 1 from " + TableUtil.TASK_TASK_IETM + " where TASK_DB_ID="
            + iTask_IDs_1.getNO_DB_ID() + " and TASK_ID=" + iTask_IDs_1.getNO_ID()
            + " and IETM_DB_ID=" + lIetmIds_1.getNO_DB_ID() + " and IETM_ID="
            + lIetmIds_1.getNO_ID() + " and IETM_TOPIC_ID='" + lIetmTopicID_1 + "' and IETM_ORD='"
            + iIETM_ORDER_1 + "'";
      Assert.assertTrue( "Check TASK_TASK_IETM table to verify the first record exists",
            RecordsExist( lQuery ) );

      // Check second ietm
      simpleIDs lIetmIds_2 = getIetmIDs( iIETM_CD_2 );
      String lIetmTopicID_2 = getIetmTopicID( lIetmIds_2, iIETM_TOPIC_2 );

      lQuery = "select 1 from " + TableUtil.TASK_TASK_IETM + " where TASK_DB_ID="
            + iTask_IDs_1.getNO_DB_ID() + " and TASK_ID=" + iTask_IDs_1.getNO_ID()
            + " and IETM_DB_ID=" + lIetmIds_2.getNO_DB_ID() + " and IETM_ID="
            + lIetmIds_2.getNO_ID() + " and IETM_TOPIC_ID='" + lIetmTopicID_2 + "' and IETM_ORD='"
            + iIETM_ORDER_2 + "'";
      Assert.assertTrue( "Check TASK_TASK_IETM table to verify the second record exists",
            RecordsExist( lQuery ) );

   }


   /**
    * This function is called by after test method to restore data which has been modified by tests.
    *
    *
    */

   public void RestoreData() {

      String lStrDelete = null;

      if ( iTask_IDs_1 != null ) {

         // delete task_task_ietm
         lStrDelete = "delete from " + TableUtil.TASK_TASK_IETM + "  where TASK_DB_ID="
               + iTask_IDs_1.getNO_DB_ID() + " and TASK_ID=" + iTask_IDs_1.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_step
         lStrDelete = "delete from " + TableUtil.TASK_STEP + "  where TASK_DB_ID="
               + iTask_IDs_1.getNO_DB_ID() + " and TASK_ID=" + iTask_IDs_1.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_parm_data
         lStrDelete = "delete from " + TableUtil.TASK_PARM_DATA + "  where TASK_DB_ID="
               + iTask_IDs_1.getNO_DB_ID() + " and TASK_ID=" + iTask_IDs_1.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_tool_list
         lStrDelete = "delete from " + TableUtil.TASK_TOOL_LIST + "  where TASK_DB_ID="
               + iTask_IDs_1.getNO_DB_ID() + " and TASK_ID=" + iTask_IDs_1.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_part_list
         lStrDelete = "delete from " + TableUtil.TASK_PART_LIST + "  where TASK_DB_ID="
               + iTask_IDs_1.getNO_DB_ID() + " and TASK_ID=" + iTask_IDs_1.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_labour_list
         lStrDelete = "delete from " + TableUtil.TASK_LABOUR_LIST + "  where TASK_DB_ID="
               + iTask_IDs_1.getNO_DB_ID() + " and TASK_ID=" + iTask_IDs_1.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_part_map
         lStrDelete = "delete from " + TableUtil.TASK_PART_MAP + "  where TASK_DB_ID="
               + iTask_IDs_1.getNO_DB_ID() + " and TASK_ID=" + iTask_IDs_1.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_task_flags
         lStrDelete = "delete from " + TableUtil.TASK_TASK_FLAGS + "  where TASK_DB_ID="
               + iTask_IDs_1.getNO_DB_ID() + " and TASK_ID=" + iTask_IDs_1.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_work_type
         lStrDelete = "delete from " + TableUtil.TASK_WORK_TYPE + "  where TASK_DB_ID="
               + iTask_IDs_1.getNO_DB_ID() + " and TASK_ID=" + iTask_IDs_1.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_task
         lStrDelete = "delete from " + TableUtil.TASK_TASK + "  where TASK_DB_ID="
               + iTask_IDs_1.getNO_DB_ID() + " and TASK_ID=" + iTask_IDs_1.getNO_ID();
         executeSQL( lStrDelete );

      }

      if ( iTask_IDs_2 != null ) {

         // delete task_task_ietm
         lStrDelete = "delete from " + TableUtil.TASK_TASK_IETM + "  where TASK_DB_ID="
               + iTask_IDs_2.getNO_DB_ID() + " and TASK_ID=" + iTask_IDs_2.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_step
         lStrDelete = "delete from " + TableUtil.TASK_STEP + "  where TASK_DB_ID="
               + iTask_IDs_2.getNO_DB_ID() + " and TASK_ID=" + iTask_IDs_2.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_parm_data
         lStrDelete = "delete from " + TableUtil.TASK_PARM_DATA + "  where TASK_DB_ID="
               + iTask_IDs_2.getNO_DB_ID() + " and TASK_ID=" + iTask_IDs_2.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_tool_list
         lStrDelete = "delete from " + TableUtil.TASK_TOOL_LIST + "  where TASK_DB_ID="
               + iTask_IDs_2.getNO_DB_ID() + " and TASK_ID=" + iTask_IDs_2.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_part_list
         lStrDelete = "delete from " + TableUtil.TASK_PART_LIST + "  where TASK_DB_ID="
               + iTask_IDs_2.getNO_DB_ID() + " and TASK_ID=" + iTask_IDs_2.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_labour_list
         lStrDelete = "delete from " + TableUtil.TASK_LABOUR_LIST + "  where TASK_DB_ID="
               + iTask_IDs_2.getNO_DB_ID() + " and TASK_ID=" + iTask_IDs_2.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_part_map
         lStrDelete = "delete from " + TableUtil.TASK_PART_MAP + "  where TASK_DB_ID="
               + iTask_IDs_2.getNO_DB_ID() + " and TASK_ID=" + iTask_IDs_2.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_task_flags
         lStrDelete = "delete from " + TableUtil.TASK_TASK_FLAGS + "  where TASK_DB_ID="
               + iTask_IDs_2.getNO_DB_ID() + " and TASK_ID=" + iTask_IDs_2.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_work_type
         lStrDelete = "delete from " + TableUtil.TASK_WORK_TYPE + "  where TASK_DB_ID="
               + iTask_IDs_2.getNO_DB_ID() + " and TASK_ID=" + iTask_IDs_2.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_task
         lStrDelete = "delete from " + TableUtil.TASK_TASK + "  where TASK_DB_ID="
               + iTask_IDs_2.getNO_DB_ID() + " and TASK_ID=" + iTask_IDs_2.getNO_ID();
         executeSQL( lStrDelete );

      }

      if ( iTask_DefnIDs_1 != null ) {
         lStrDelete = "delete from TASK_DEFN where TASK_DEFN_DB_ID=" + iTask_DefnIDs_1.getNO_DB_ID()
               + " and TASK_DEFN_ID=" + iTask_DefnIDs_1.getNO_ID();
         executeSQL( lStrDelete );

      }

      if ( iTask_DefnIDs_2 != null ) {
         lStrDelete = "delete from TASK_DEFN where TASK_DEFN_DB_ID=" + iTask_DefnIDs_2.getNO_DB_ID()
               + " and TASK_DEFN_ID=" + iTask_DefnIDs_2.getNO_ID();
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

               lPrepareCallJICPART = getConnection().prepareCall(
                     "BEGIN  c_comp_jic_import.validate_comp_jic(on_retcode =>?); END;" );

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

               lPrepareCallKIT = getConnection().prepareCall(
                     "BEGIN c_comp_jic_import.import_comp_jic(on_retcode =>?); END;" );

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


   private int setStepDefinitionDescriptionLimit( int aLimit ) {

      String lOrigValue = runQuery(
            "SELECT parm_value FROM utl_config_parm WHERE parm_name='JOB_STEP_DEFINITION_DESCRIPTION_LENGTH_LIMIT'",
            "parm_value" );
      lOrigValue = lOrigValue.replace( ",", "" );

      runUpdate( "UPDATE utl_config_parm SET parm_value = '" + aLimit
            + "' WHERE parm_name = 'JOB_STEP_DEFINITION_DESCRIPTION_LENGTH_LIMIT'" );

      return Integer.parseInt( lOrigValue );
   }

}
