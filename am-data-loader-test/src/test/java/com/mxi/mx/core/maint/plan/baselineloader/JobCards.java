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

import com.mxi.mx.core.maint.plan.datamodels.assmbJICpart;
import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;
import com.mxi.mx.util.BaselineLoaderTest;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.TestConstants;
import com.mxi.mx.util.ValidationAndImport;
import com.mxi.mx.util.WhereClause;


/**
 * This test suite contains test cases on validation and import functionality on JIC_IMPORT.
 *
 * @author ALICIA QIAN
 */
public class JobCards extends BaselineLoaderTest {

   @Rule
   public TestName testName = new TestName();
   ValidationAndImport ivalidationandimport;

   public String iACFT_ASSMBLCD = "ACFT_CD1";
   public String iENG_ASSMBLCD = "ENG_CD1";
   public String iCOMHW_ASSMBLCD = "COMHW";
   public String iJIC_ATA_CD_TRK = "ACFT-SYS-1-1-TRK-BATCH-PARENT";
   public String iJIC_ATA_CD_SYS = "SYS-1";
   public String iJIC_ATA_CD_SUBASSY = "APU-ASSY";
   public String iJIC_TASK_CD_1 = "ATEST";
   public String iJIC_TASK_CD_2 = "BTEST";
   // c_assmbl_panel
   public String iPANEL_CD_1 = "P4-1-001";
   public String iPANEL_NAME_1 = "P4-1-001N";
   public String iPANEL_LDESC_1 = "P4-1-001L";
   // c_assmbl_zone
   public String iZONE_CD_1 = "ZONE3-4";
   public String iZONE_CD_2 = "ZONE10";
   public String iZONE_NAME_1 = "Aircraft Zone 3-4";
   public String iZONE_NAME_2 = "ENG Zone 10";
   public String iZONE_LDESC_1 = "Aircraft Zone 3-4L";
   public String iZONE_LDESC_2 = "ENG Zone 10L";
   public String iNH_ZONE_CD_1 = "ZONE3";
   public String iNH_ZONE_CD_2 = null;
   public String iHR_LIMIT = "1";

   // c_jic
   public String iJIC_TASK_CLASS_CD = "SRVC";
   public String iJIC_TASK_NAME_1 = "TASKNAME";
   public String iJIC_TASK_NAME_2 = "TASKNAME2";
   public String iJIC_TASK_DESC_1 = "TASKDESC";
   public String iJIC_TASK_DESC_2 = "TASKDESC2";
   public String iJIC_TASK_REF_SDESC_1 = "TASKREFSDESC";
   public String iJIC_TASK_REF_SDESC_2 = "TASKREFSDESC2";
   public String iTASK_ORIGINATOR_CD = "AWL";
   public String iAPPLICABILITY_DESC = "1,5-7";
   public String iSUBCLASS_CD = "LUBE";
   public String iDURATION_HOURS = "3";
   public String iINSTRUCTIONS = "testInstruction";
   public String iEXT_KEY_SDESC = "TestExtKey";
   public String iTASK_PRIORITY_CD_1 = "HIGH";
   public String iTASK_APPL_LDESC = "TestAPLLSDESC";
   public String iTASK_APPL_LDESC_2 = "TestAPLLSDESC2";
   public String iENGINEERING_LDESC = "testENGLDESC";
   public String iENGINEERING_LDESC_2 = "testENGLDESC2";
   public String iWORK_TYPE_LIST_1 = "ELECTRIC";
   public String iWORK_TYPE_LIST_2 = "FUEL";
   // c_jic_ietm_topic
   public String iJIC_IETM_CD_1 = "TEST1";
   public String iJIC_IETM_CD_2 = "TEST2";
   public String iJIC_IETM_CD_3 = "TEST3";
   public String iJIC_IETM_CD_4 = "TEST4";
   public String iJIC_TOPIC_SDESC_1 = "AUTOTEST1-1";
   public String iJIC_TOPIC_SDESC_2 = "AUTOTEST1-2";
   public String iJIC_TOPIC_SDESC_3 = "AUTOTEST1-3";
   public String iJIC_TOPIC_SDESC_4 = "AUTOTEST1-4";
   public String iJIC_IETM_ORD_1 = "1";
   public String iJIC_IETM_ORD_2 = "2";
   public String iJIC_IETM_ORD_3 = "60";
   public String iJIC_IETM_ORD_4 = "25";
   // c_jic_labor
   public String iLABOR_SKILL_CD = "ENG";
   public String iMAN_PWR_CT = "1";
   public String iSCHED_WORK_HRS = "3";
   public String iSCHED_INSP_HRS = "2";
   public String iSCHED_CERT_HRS = "3";
   // c_jic_part
   public String iPART_NO_OEM_1 = "A0000001";
   public String iPART_NO_OEM_2 = "CHW000001";
   public String iMANUFACT_REF_1 = "10001";
   public String iMANUFACT_REF_2 = "11111";
   public String iREQ_QT = "1";
   public String iREQ_QT_2 = "5";
   public String iPOSITION = "1";
   public String iREQ_ACTION_CD = "REQ";
   // c_jic_tool
   public String iPN_Tool_1 = "T0000002";
   public String iPN_Tool_2 = "T0000003";
   public String iMF_Tool_1 = "ABC11";
   public String iMF_Tool_2 = "ABC11";
   public String iREQ_HR = "5";
   // c_jic_step
   public String iJOB_STEP_ORD_1 = "1";
   public String iJOB_STEP_ORD_2 = "2";
   public String iJOB_STEP_DESC_1 = "Step1";
   public String iJOB_STEP_DESC_2 = "Step2";

   public simpleIDs iTaskIDs_1 = null;
   public simpleIDs iTaskDefnIDs_1 = null;

   public simpleIDs iTaskIDs_2 = null;
   public simpleIDs iTaskDefnIDs_2 = null;

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
    * This test is to verify jic_import.validate_jic functionality of staging tables:
    * c_assmbl_panel, c_assmbl_zone,c_jic,c_jic_ietm_topic, c_jic_labor, c_jic_part, c_jic_tool and
    * c_jic_step. JIC is on TRK of ACFT
    *
    *
    */

   @Test
   public void testJICTRKOnACFTVALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      prepareData( iACFT_ASSMBLCD, iJIC_ATA_CD_TRK );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify jic_import.import_jic functionality of staging tables: c_assmbl_panel,
    * c_assmbl_zone,c_jic,c_jic_ietm_topic, c_jic_labor, c_jic_part, c_jic_tool and c_jic_step. JIC
    * is on TRK of ACFT
    *
    *
    */

   @Test
   public void testJICTRKOnACFTIMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testJICTRKOnACFTVALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTaskIDs_1 = null;
      iTaskDefnIDs_1 = null;

      verifyALL( iACFT_ASSMBLCD, iREQ_QT );

   }


   /**
    * This test is to verify jic_import.validate_jic functionality of staging tables:
    * c_assmbl_panel, c_assmbl_zone,c_jic,c_jic_ietm_topic, c_jic_labor, c_jic_part, c_jic_tool and
    * c_jic_step. JIC is on SYS of ACFT
    *
    *
    */

   @Test
   public void testJICSYSOnACFTVALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      prepareData_2( iACFT_ASSMBLCD, iJIC_ATA_CD_SYS );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify jic_import.import_jic functionality of staging tables: c_assmbl_panel,
    * c_assmbl_zone,c_jic,c_jic_ietm_topic, c_jic_labor, c_jic_part, c_jic_tool and c_jic_step. JIC
    * is on SYS of ACFT
    *
    *
    */

   @Test
   public void testJICSYSOnACFTIMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testJICSYSOnACFTVALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTaskIDs_1 = null;
      iTaskDefnIDs_1 = null;

      verifyALL( iACFT_ASSMBLCD, iREQ_QT_2 );

   }


   /**
    * This test is to verify jic_import.validate_jic functionality of staging tables:
    * c_assmbl_panel, c_assmbl_zone,c_jic,c_jic_ietm_topic, c_jic_labor, c_jic_part, c_jic_tool and
    * c_jic_step. JIC is on SUBASSY of ACFT
    *
    *
    */

   @Test
   public void testJICSUBASSYOnACFTVALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      prepareData_2( iACFT_ASSMBLCD, iJIC_ATA_CD_SUBASSY );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify jic_import.import_jic functionality of staging tables: c_assmbl_panel,
    * c_assmbl_zone,c_jic,c_jic_ietm_topic, c_jic_labor, c_jic_part, c_jic_tool and c_jic_step. JIC
    * is on SUBASSY of ACFT
    *
    *
    */

   @Test
   public void testJICSUBASSYOnACFTIMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testJICSUBASSYOnACFTVALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTaskIDs_1 = null;
      iTaskDefnIDs_1 = null;

      verifyALL( iACFT_ASSMBLCD, iREQ_QT_2 );

   }


   /**
    * This test is to verify jic_import.validate_jic functionality of staging tables:
    * c_assmbl_panel, c_assmbl_zone,c_jic,c_jic_ietm_topic, c_jic_labor, c_jic_part, c_jic_tool and
    * c_jic_step. JIC is on ACFT itself
    *
    *
    */

   @Test
   public void testJICOnACFTVALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      prepareData_2( iACFT_ASSMBLCD, iACFT_ASSMBLCD );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify jic_import.import_jic functionality of staging tables: c_assmbl_panel,
    * c_assmbl_zone,c_jic,c_jic_ietm_topic, c_jic_labor, c_jic_part, c_jic_tool and c_jic_step. JIC
    * is on ACFT itself
    *
    *
    */

   @Test
   public void testJICOnACFTIMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testJICOnACFTVALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTaskIDs_1 = null;
      iTaskDefnIDs_1 = null;

      verifyALL( iACFT_ASSMBLCD, iREQ_QT_2 );

   }


   /**
    * This test is to verify jic_import.validate_jic functionality of staging tables:
    * c_assmbl_panel, c_assmbl_zone,c_jic,c_jic_ietm_topic, c_jic_labor, c_jic_part, c_jic_tool and
    * c_jic_step. JIC is on ENG itself
    *
    *
    */

   @Test
   public void testJICOnENGVALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      prepareData_2( iENG_ASSMBLCD, iENG_ASSMBLCD );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify jic_import.import_jic functionality of staging tables: c_assmbl_panel,
    * c_assmbl_zone,c_jic,c_jic_ietm_topic, c_jic_labor, c_jic_part, c_jic_tool and c_jic_step. JIC
    * is on ENG itself
    *
    *
    *
    */

   @Test
   public void testJICOnENGIMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testJICOnENGVALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTaskIDs_1 = null;
      iTaskDefnIDs_1 = null;

      verifyENGALL( iENG_ASSMBLCD, iREQ_QT_2 );

   }


   /**
    * This test is to verify jic_import.validate_jic functionality of staging tables:
    * c_assmbl_panel, c_assmbl_zone,c_jic,c_jic_ietm_topic, c_jic_labor, c_jic_part, c_jic_tool and
    * c_jic_step. JICs are on ACFT itself and TRK
    *
    *
    */

   @Test
   public void testJICMultipleVALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      prepareData_2( iACFT_ASSMBLCD, iACFT_ASSMBLCD );
      // Prepare data on TRK

      // C_JIC
      Map<String, String> lc_JICMap = new LinkedHashMap<>();
      lc_JICMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lc_JICMap.put( "JIC_ATA_CD", "'" + iJIC_ATA_CD_TRK + "'" );
      lc_JICMap.put( "JIC_TASK_CD", "'" + iJIC_TASK_CD_2 + "'" );
      lc_JICMap.put( "JIC_TASK_CLASS_CD", "'" + iJIC_TASK_CLASS_CD + "'" );
      lc_JICMap.put( "JIC_TASK_NAME", "'" + iJIC_TASK_NAME_2 + "'" );
      lc_JICMap.put( "JIC_TASK_DESC", "'" + iJIC_TASK_DESC_2 + "'" );
      lc_JICMap.put( "JIC_TASK_REF_SDESC", "'" + iJIC_TASK_REF_SDESC_2 + "'" );
      lc_JICMap.put( "TASK_ORIGINATOR_CD", "'" + iTASK_ORIGINATOR_CD + "'" );
      lc_JICMap.put( "APPLICABILITY_DESC", "'" + iAPPLICABILITY_DESC + "'" );
      lc_JICMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );
      lc_JICMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );
      lc_JICMap.put( "DURATION_HOURS", "'" + iDURATION_HOURS + "'" );
      lc_JICMap.put( "INSTRUCTIONS", "'" + iINSTRUCTIONS + "'" );
      lc_JICMap.put( "ZONE_LIST", "'" + iZONE_CD_1 + "'" );
      lc_JICMap.put( "PANEL_LIST", "'" + iPANEL_CD_1 + "'" );
      lc_JICMap.put( "EXT_KEY_SDESC", "'" + iEXT_KEY_SDESC + "'" );
      lc_JICMap.put( "TASK_PRIORITY_CD", "'" + iTASK_PRIORITY_CD_1 + "'" );
      lc_JICMap.put( "TASK_APPL_LDESC", "'" + iTASK_APPL_LDESC_2 + "'" );
      lc_JICMap.put( "ENGINEERING_LDESC", "'" + iENGINEERING_LDESC_2 + "'" );
      lc_JICMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC, lc_JICMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify jic_import.import_jic functionality of staging tables: c_assmbl_panel,
    * c_assmbl_zone,c_jic,c_jic_ietm_topic, c_jic_labor, c_jic_part, c_jic_tool and c_jic_step. JICs
    * are on ACFT itself and TRK
    *
    *
    */
   @Test
   public void testJICMultipleIMPORT() {

      System.out.println( "=======Starting: " + testName.getMethodName()
            + " Validation========================" );

      testJICMultipleVALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTaskIDs_1 = null;
      iTaskDefnIDs_1 = null;

      iTaskIDs_2 = null;
      iTaskDefnIDs_2 = null;

      verifyMultipleALL( iACFT_ASSMBLCD, iREQ_QT_2 );

   }


   /**
    * This test is to verify OPER-1128: Configurator exports C_JIC_LABOR.SCHED_INSP_HRS and
    * C_JIC_LABOR.SCHED_CERT_HRS incorrectly. the insp and cert are "0".
    *
    *
    *
    */

   @Test
   public void testOPER_1128_1_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      prepareDataOPER_1128( iACFT_ASSMBLCD, iJIC_ATA_CD_TRK, "0" );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify OPER-1128: Configurator exports C_JIC_LABOR.SCHED_INSP_HRS and
    * C_JIC_LABOR.SCHED_CERT_HRS incorrectly, the insp and cert are "0".
    *
    *
    *
    */

   @Test
   public void testOPER_1128_1_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOPER_1128_1_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTaskIDs_1 = null;
      iTaskDefnIDs_1 = null;

      verifyOPER_1128( iACFT_ASSMBLCD, iREQ_QT, "1", "1" );

   }


   /**
    * This test is to verify OPER-1128: Configurator exports C_JIC_LABOR.SCHED_INSP_HRS and
    * C_JIC_LABOR.SCHED_CERT_HRS incorrectly. the insp and cert are null.
    *
    *
    *
    */

   public void testOPER_1128_2_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      prepareDataOPER_1128( iACFT_ASSMBLCD, iJIC_ATA_CD_TRK, null );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify OPER-1128: Configurator exports C_JIC_LABOR.SCHED_INSP_HRS and
    * C_JIC_LABOR.SCHED_CERT_HRS incorrectly, the insp and cert are null.
    *
    *
    *
    */

   public void testOPER_1128_2_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOPER_1128_2_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTaskIDs_1 = null;
      iTaskDefnIDs_1 = null;

      verifyOPER_1128( iACFT_ASSMBLCD, iREQ_QT, "0", "0" );

   }


   /**
    * This test is to verify OPER-1128: Configurator exports C_JIC_LABOR.SCHED_INSP_HRS and
    * C_JIC_LABOR.SCHED_CERT_HRS incorrectly, the insp_bool and cert_boll are null.
    *
    * @throws SQLException
    *
    *
    *
    */

   @Test
   public void testOPER_1128_2_EXPORT() throws SQLException {

      System.out.println(
            "=======Starting:  " + testName.getMethodName() + " IMPORT========================" );

      testOPER_1128_2_IMPORT();

      System.out.println( "Finish IMPORT" );

      // clear staging table
      clearBaselineLoaderTables();

      // run validation
      Assert.assertTrue( runExport() == 1 );

      // Verify c_jic_labor table
      String lQuery = "select 1 from " + TableUtil.C_JIC_LABOR
            + " where JIC_TASK_CD='ATEST' and SCHED_INSP_HRS IS NULL  and SCHED_CERT_HRS IS NULL ";
      Assert.assertTrue( "Check C_JIC_LABOR table to verify the record exists",
            RecordsExist( lQuery ) );

   }


   /**
    * This test is to verify OPER-30663: Baseline Loader - Job Card using Class with RSTAT_CD !=0
    * does not get failed by validation .
    *
    *
    *
    */

   @Test
   public void testCFGJIC_12225_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      prepareDataOPER_30663( iACFT_ASSMBLCD, iJIC_ATA_CD_TRK, "0" );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      // Check error code
      checkErrorCode( testName.getMethodName(), "CFGJIC-12225" );

   }


   /**
    * This test is to verify that a JIC Step with a description less than the configured length
    * limit passes validation.
    */
   @Test
   public void testJicStepDescriptionValidation_Success() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // Set up the description length limit (config parm).
      //
      // Note: because we are inserting test data into a clob, that test data must not exceed 4000
      // characters (DB limit for a clob).
      int originalLimit = setStepDefinitionDescriptionLimit( STEP_DEFN_DESC_LENGTH_LIMIT );

      // Test step description with a length less than the limit.
      String lStepDescription = StringUtils.repeat( "a", STEP_DEFN_DESC_LENGTH_LIMIT - 1 );

      Map<String, String> lc_JICMap = new LinkedHashMap<>();

      lc_JICMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lc_JICMap.put( "PANEL_CD", "'" + iPANEL_CD_1 + "'" );
      lc_JICMap.put( "ZONE_CD", "'" + iZONE_CD_2 + "'" );
      lc_JICMap.put( "PANEL_NAME", "'" + iPANEL_NAME_1 + "'" );
      lc_JICMap.put( "PANEL_LDESC", "'" + iPANEL_LDESC_1 + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ASSMBL_PANEL, lc_JICMap ) );

      // C_ASSMBL_ZONE table
      lc_JICMap.clear();
      lc_JICMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lc_JICMap.put( "ZONE_CD", "'" + iZONE_CD_2 + "'" );
      lc_JICMap.put( "ZONE_NAME", "'" + iZONE_NAME_2 + "'" );
      lc_JICMap.put( "ZONE_LDESC", "'" + iZONE_LDESC_2 + "'" );
      lc_JICMap.put( "HR_LIMIT", "'" + iHR_LIMIT + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ASSMBL_ZONE, lc_JICMap ) );

      // C_JIC
      lc_JICMap.clear();
      lc_JICMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lc_JICMap.put( "JIC_ATA_CD", "'" + iENG_ASSMBLCD + "'" );
      lc_JICMap.put( "JIC_TASK_CD", "'" + iJIC_TASK_CD_1 + "'" );
      lc_JICMap.put( "JIC_TASK_CLASS_CD", "'" + iJIC_TASK_CLASS_CD + "'" );
      lc_JICMap.put( "JIC_TASK_NAME", "'" + iJIC_TASK_NAME_1 + "'" );
      lc_JICMap.put( "JIC_TASK_DESC", "'" + iJIC_TASK_DESC_1 + "'" );
      lc_JICMap.put( "JIC_TASK_REF_SDESC", "'" + iJIC_TASK_REF_SDESC_1 + "'" );
      lc_JICMap.put( "TASK_ORIGINATOR_CD", "'" + iTASK_ORIGINATOR_CD + "'" );
      lc_JICMap.put( "APPLICABILITY_DESC", "'" + iAPPLICABILITY_DESC + "'" );
      lc_JICMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );
      lc_JICMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );
      lc_JICMap.put( "DURATION_HOURS", "'" + iDURATION_HOURS + "'" );
      lc_JICMap.put( "INSTRUCTIONS", "'" + iINSTRUCTIONS + "'" );
      lc_JICMap.put( "PANEL_LIST", "'" + iPANEL_CD_1 + "'" );
      lc_JICMap.put( "EXT_KEY_SDESC", "'" + iEXT_KEY_SDESC + "'" );
      lc_JICMap.put( "TASK_PRIORITY_CD", "'" + iTASK_PRIORITY_CD_1 + "'" );
      lc_JICMap.put( "TASK_APPL_LDESC", "'" + iTASK_APPL_LDESC + "'" );
      lc_JICMap.put( "ENGINEERING_LDESC", "'" + iENGINEERING_LDESC + "'" );
      lc_JICMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC, lc_JICMap ) );

      // C_JIC_STEP
      lc_JICMap.clear();
      lc_JICMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lc_JICMap.put( "JIC_ATA_CD", "'" + iENG_ASSMBLCD + "'" );
      lc_JICMap.put( "JIC_TASK_CD", "'" + iJIC_TASK_CD_1 + "'" );
      lc_JICMap.put( "JOB_STEP_ORD", "'" + iJOB_STEP_ORD_1 + "'" );
      lc_JICMap.put( "JOB_STEP_DESC", "'" + lStepDescription + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC_STEP, lc_JICMap ) );

      // run validation
      int lReturnCode = runValidationAndImport( true, true );

      setStepDefinitionDescriptionLimit( originalLimit );

      Assert.assertEquals( 1, lReturnCode );

   }


   /**
    * This test is to verify that during validation, a JIC Step with a description greater than the
    * configured length limit results in an error code CFGJIC-10718: "Job Step Description length
    * cannot exceed the length limit".
    */
   @Test
   public void testJicStepDescriptionValidation_CFGJIC_10718() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // Set up the description length limit (config parm).
      //
      // Note: because we are inserting test data into a clob, that test data must not exceed 4000
      // characters (DB limit for a clob).
      int originalLimit = setStepDefinitionDescriptionLimit( STEP_DEFN_DESC_LENGTH_LIMIT );

      // Test step description with a length greater than the limit.
      String lStepDescription = StringUtils.repeat( "a", STEP_DEFN_DESC_LENGTH_LIMIT + 1 );

      Map<String, String> lc_JICMap = new LinkedHashMap<>();

      // C_JIC_STEP
      lc_JICMap.put( "ASSMBL_CD", "'" + iENG_ASSMBLCD + "'" );
      lc_JICMap.put( "JIC_ATA_CD", "'" + iENG_ASSMBLCD + "'" );
      lc_JICMap.put( "JIC_TASK_CD", "'" + iJIC_TASK_CD_1 + "'" );
      lc_JICMap.put( "JOB_STEP_ORD", "'" + iJOB_STEP_ORD_1 + "'" );
      lc_JICMap.put( "JOB_STEP_DESC", "'" + lStepDescription + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC_STEP, lc_JICMap ) );

      // run validation
      int lReturnCode = runValidationAndImport( true, true );

      setStepDefinitionDescriptionLimit( originalLimit );

      // retrieve erroneous data and then assert
      String lErrorCode = "CFGJIC-10718";
      Assert.assertEquals( -1, lReturnCode );
      List<String> lFields = Arrays.asList( FIELD_RESULT_CD );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( FIELD_RESULT_CD, lErrorCode );
      ResultSet lQs = runQuery( TableUtil.buildTableQuery( TableUtil.C_JIC_STEP, lFields, lArgs ) );
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
   public void testJIC_IETM_OPER_30664_VALIDATION() throws Exception {

      Map<String, String> lc_JICMap = new LinkedHashMap<>();

      // C_JIC
      lc_JICMap.clear();
      lc_JICMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lc_JICMap.put( "JIC_ATA_CD", "'" + iJIC_ATA_CD_TRK + "'" );
      lc_JICMap.put( "JIC_TASK_CD", "'" + iJIC_TASK_CD_1 + "'" );
      lc_JICMap.put( "JIC_TASK_CLASS_CD", "'" + iJIC_TASK_CLASS_CD + "'" );
      lc_JICMap.put( "JIC_TASK_NAME", "'" + iJIC_TASK_NAME_1 + "'" );
      lc_JICMap.put( "JIC_TASK_DESC", "'" + iJIC_TASK_DESC_1 + "'" );
      lc_JICMap.put( "DURATION_HOURS", "'" + iDURATION_HOURS + "'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC, lc_JICMap ) );

      // C_JIC_IETM_TOPIC #1
      lc_JICMap.clear();
      lc_JICMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lc_JICMap.put( "JIC_ATA_CD", "'" + iJIC_ATA_CD_TRK + "'" );
      lc_JICMap.put( "JIC_TASK_CD", "'" + iJIC_TASK_CD_1 + "'" );
      lc_JICMap.put( "JIC_IETM_CD", "'" + iJIC_IETM_CD_1 + "'" );
      lc_JICMap.put( "JIC_TOPIC_SDESC", "'" + iJIC_TOPIC_SDESC_1 + "'" );
      lc_JICMap.put( "JIC_IETM_ORD", "'" + iJIC_IETM_ORD_1 + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC_IETM_TOPIC, lc_JICMap ) );

      // C_JIC_IETM_TOPIC #2
      lc_JICMap.clear();
      lc_JICMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lc_JICMap.put( "JIC_ATA_CD", "'" + iJIC_ATA_CD_TRK + "'" );
      lc_JICMap.put( "JIC_TASK_CD", "'" + iJIC_TASK_CD_1 + "'" );
      lc_JICMap.put( "JIC_IETM_CD", "'" + iJIC_IETM_CD_2 + "'" );
      lc_JICMap.put( "JIC_TOPIC_SDESC", "'" + iJIC_TOPIC_SDESC_2 + "'" );
      lc_JICMap.put( "JIC_IETM_ORD", "'" + iJIC_IETM_ORD_2 + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC_IETM_TOPIC, lc_JICMap ) );

      // C_JIC_IETM_TOPIC #3
      lc_JICMap.clear();
      lc_JICMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lc_JICMap.put( "JIC_ATA_CD", "'" + iJIC_ATA_CD_TRK + "'" );
      lc_JICMap.put( "JIC_TASK_CD", "'" + iJIC_TASK_CD_1 + "'" );
      lc_JICMap.put( "JIC_IETM_CD", "'" + iJIC_IETM_CD_3 + "'" );
      lc_JICMap.put( "JIC_TOPIC_SDESC", "'" + iJIC_TOPIC_SDESC_3 + "'" );
      lc_JICMap.put( "JIC_IETM_ORD", "'" + iJIC_IETM_ORD_3 + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC_IETM_TOPIC, lc_JICMap ) );

      // C_COMP_JIC_IETM #4
      lc_JICMap.clear();
      lc_JICMap.put( "ASSMBL_CD", "'" + iACFT_ASSMBLCD + "'" );
      lc_JICMap.put( "JIC_ATA_CD", "'" + iJIC_ATA_CD_TRK + "'" );
      lc_JICMap.put( "JIC_TASK_CD", "'" + iJIC_TASK_CD_1 + "'" );
      lc_JICMap.put( "JIC_IETM_CD", "'" + iJIC_IETM_CD_4 + "'" );
      lc_JICMap.put( "JIC_TOPIC_SDESC", "'" + iJIC_TOPIC_SDESC_4 + "'" );
      lc_JICMap.put( "JIC_IETM_ORD", "'" + iJIC_IETM_ORD_4 + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC_IETM_TOPIC, lc_JICMap ) );

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
   public void testJIC_IETM_OPER_30664_IMPORT() throws Exception {

      System.out.println( "=======Starting: " + testName.getMethodName()
            + " Validation========================" );

      testJIC_IETM_OPER_30664_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      iTaskIDs_1 = null;

      verifyIETMs( iJIC_TASK_CD_1, iJIC_IETM_CD_1, 1 );
      verifyIETMs( iJIC_TASK_CD_1, iJIC_IETM_CD_2, 2 );
      verifyIETMs( iJIC_TASK_CD_1, iJIC_IETM_CD_3, 4 );
      verifyIETMs( iJIC_TASK_CD_1, iJIC_IETM_CD_4, 3 );
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

      if ( iTaskIDs_1 == null ) {
         lQuery =
               "select task_db_id, task_id from task_task where task_cd = '" + aJIC_TASK_CD_1 + "'";
         iTaskIDs_1 = getIDs( lQuery, "TASK_DB_ID", "TASK_ID" );
      }
      lQuery = "select ietm_db_id, ietm_id from ietm_ietm where ietm_ietm.ietm_cd = '"
            + aJIC_IETM_CD_1 + "'";
      lIetm_IDs = getIDs( lQuery, "IETM_DB_ID", "IETM_ID" );

      lQuery =
            "select ietm_ord from task_task_ietm where  task_db_id = '" + iTaskIDs_1.getNO_DB_ID()
                  + "' and task_id = '" + iTaskIDs_1.getNO_ID() + "' and ietm_db_id = '"
                  + lIetm_IDs.getNO_DB_ID() + "' and ietm_id = '" + lIetm_IDs.getNO_ID() + "'";
      Assert.assertTrue( "IETM order value is incorrect.",
            getIntValueFromQuery( lQuery, "IETM_ORD" ) == aIETM_ord );
   }


   /**
    * This function is to insert data into staging tables.
    *
    *
    *
    */
   public void prepareData( String aASSMBL_CD, String aJIC_ATA_CD ) {
      // C_ASSMBL_PANEL table
      Map<String, String> lc_JICMap = new LinkedHashMap<>();

      if ( aASSMBL_CD.contains( "ACFT" ) ) {

         lc_JICMap.put( "ASSMBL_CD", "'" + aASSMBL_CD + "'" );
         lc_JICMap.put( "PANEL_CD", "'" + iPANEL_CD_1 + "'" );
         lc_JICMap.put( "ZONE_CD", "'" + iZONE_CD_1 + "'" );
         lc_JICMap.put( "PANEL_NAME", "'" + iPANEL_NAME_1 + "'" );
         lc_JICMap.put( "PANEL_LDESC", "'" + iPANEL_LDESC_1 + "'" );

         // insert map
         runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ASSMBL_PANEL, lc_JICMap ) );

         // C_ASSMBL_ZONE table
         lc_JICMap.clear();
         lc_JICMap.put( "ASSMBL_CD", "'" + aASSMBL_CD + "'" );
         lc_JICMap.put( "ZONE_CD", "'" + iZONE_CD_1 + "'" );
         lc_JICMap.put( "ZONE_NAME", "'" + iZONE_NAME_1 + "'" );
         lc_JICMap.put( "NH_ZONE_CD", "'" + iNH_ZONE_CD_1 + "'" );
         lc_JICMap.put( "ZONE_LDESC", "'" + iZONE_LDESC_1 + "'" );
         lc_JICMap.put( "HR_LIMIT", "'" + iHR_LIMIT + "'" );

         // insert map
         runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ASSMBL_ZONE, lc_JICMap ) );
      } else {
         lc_JICMap.put( "ASSMBL_CD", "'" + aASSMBL_CD + "'" );
         lc_JICMap.put( "PANEL_CD", "'" + iPANEL_CD_1 + "'" );
         lc_JICMap.put( "ZONE_CD", "'" + iZONE_CD_2 + "'" );
         lc_JICMap.put( "PANEL_NAME", "'" + iPANEL_NAME_1 + "'" );
         lc_JICMap.put( "PANEL_LDESC", "'" + iPANEL_LDESC_1 + "'" );

         // insert map
         runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ASSMBL_PANEL, lc_JICMap ) );

         // C_ASSMBL_ZONE table
         lc_JICMap.clear();
         lc_JICMap.put( "ASSMBL_CD", "'" + aASSMBL_CD + "'" );
         lc_JICMap.put( "ZONE_CD", "'" + iZONE_CD_2 + "'" );
         lc_JICMap.put( "ZONE_NAME", "'" + iZONE_NAME_2 + "'" );
         // lc_JICMap.put( "NH_ZONE_CD", "'" + iNH_ZONE_CD_2 + "'" );
         lc_JICMap.put( "ZONE_LDESC", "'" + iZONE_LDESC_2 + "'" );
         lc_JICMap.put( "HR_LIMIT", "'" + iHR_LIMIT + "'" );

         // insert map
         runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ASSMBL_ZONE, lc_JICMap ) );

      }

      // C_JIC
      lc_JICMap.clear();
      lc_JICMap.put( "ASSMBL_CD", "'" + aASSMBL_CD + "'" );
      lc_JICMap.put( "JIC_ATA_CD", "'" + aJIC_ATA_CD + "'" );
      lc_JICMap.put( "JIC_TASK_CD", "'" + iJIC_TASK_CD_1 + "'" );
      lc_JICMap.put( "JIC_TASK_CLASS_CD", "'" + iJIC_TASK_CLASS_CD + "'" );
      lc_JICMap.put( "JIC_TASK_NAME", "'" + iJIC_TASK_NAME_1 + "'" );
      lc_JICMap.put( "JIC_TASK_DESC", "'" + iJIC_TASK_DESC_1 + "'" );
      lc_JICMap.put( "JIC_TASK_REF_SDESC", "'" + iJIC_TASK_REF_SDESC_1 + "'" );
      lc_JICMap.put( "TASK_ORIGINATOR_CD", "'" + iTASK_ORIGINATOR_CD + "'" );
      lc_JICMap.put( "APPLICABILITY_DESC", "'" + iAPPLICABILITY_DESC + "'" );
      lc_JICMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );
      lc_JICMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );
      lc_JICMap.put( "DURATION_HOURS", "'" + iDURATION_HOURS + "'" );
      lc_JICMap.put( "INSTRUCTIONS", "'" + iINSTRUCTIONS + "'" );
      if ( aASSMBL_CD.contains( "ACFT" ) ) {
         lc_JICMap.put( "ZONE_LIST", "'" + iZONE_CD_1 + "'" );
      } else {
         lc_JICMap.put( "ZONE_LIST", "'" + iZONE_CD_2 + "'" );

      }
      lc_JICMap.put( "PANEL_LIST", "'" + iPANEL_CD_1 + "'" );
      lc_JICMap.put( "EXT_KEY_SDESC", "'" + iEXT_KEY_SDESC + "'" );
      lc_JICMap.put( "TASK_PRIORITY_CD", "'" + iTASK_PRIORITY_CD_1 + "'" );
      lc_JICMap.put( "TASK_APPL_LDESC", "'" + iTASK_APPL_LDESC + "'" );
      lc_JICMap.put( "ENGINEERING_LDESC", "'" + iENGINEERING_LDESC + "'" );
      lc_JICMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC, lc_JICMap ) );

      // C_JIC_IETM_TOPIC
      lc_JICMap.clear();
      lc_JICMap.put( "ASSMBL_CD", "'" + aASSMBL_CD + "'" );
      lc_JICMap.put( "JIC_ATA_CD", "'" + aJIC_ATA_CD + "'" );
      lc_JICMap.put( "JIC_TASK_CD", "'" + iJIC_TASK_CD_1 + "'" );
      lc_JICMap.put( "JIC_IETM_CD", "'" + iJIC_IETM_CD_1 + "'" );
      lc_JICMap.put( "JIC_TOPIC_SDESC", "'" + iJIC_TOPIC_SDESC_1 + "'" );
      lc_JICMap.put( "JIC_IETM_ORD", "'" + iJIC_IETM_ORD_1 + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC_IETM_TOPIC, lc_JICMap ) );

      if ( aASSMBL_CD.contains( "ACFT" ) ) {
         lc_JICMap.clear();
         lc_JICMap.put( "ASSMBL_CD", "'" + aASSMBL_CD + "'" );
         lc_JICMap.put( "JIC_ATA_CD", "'" + aJIC_ATA_CD + "'" );
         lc_JICMap.put( "JIC_TASK_CD", "'" + iJIC_TASK_CD_1 + "'" );
         lc_JICMap.put( "JIC_IETM_CD", "'" + iJIC_IETM_CD_2 + "'" );
         lc_JICMap.put( "JIC_TOPIC_SDESC", "'" + iJIC_TOPIC_SDESC_2 + "'" );
         lc_JICMap.put( "JIC_IETM_ORD", "'" + iJIC_IETM_ORD_2 + "'" );
         runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC_IETM_TOPIC, lc_JICMap ) );
      }

      // C_JIC_LABOR
      lc_JICMap.clear();
      lc_JICMap.put( "ASSMBL_CD", "'" + aASSMBL_CD + "'" );
      lc_JICMap.put( "JIC_ATA_CD", "'" + aJIC_ATA_CD + "'" );
      lc_JICMap.put( "JIC_TASK_CD", "'" + iJIC_TASK_CD_1 + "'" );
      lc_JICMap.put( "LABOR_SKILL_CD", "'" + iLABOR_SKILL_CD + "'" );
      lc_JICMap.put( "MAN_PWR_CT", "'" + iMAN_PWR_CT + "'" );
      lc_JICMap.put( "SCHED_WORK_HRS", "'" + iSCHED_WORK_HRS + "'" );
      lc_JICMap.put( "SCHED_INSP_HRS", "'" + iSCHED_INSP_HRS + "'" );
      lc_JICMap.put( "SCHED_CERT_HRS", "'" + iSCHED_CERT_HRS + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC_LABOR, lc_JICMap ) );

      // C_JIC_PART
      if ( aASSMBL_CD.contains( "ACFT" ) ) {
         lc_JICMap.clear();
         lc_JICMap.put( "ASSMBL_CD", "'" + aASSMBL_CD + "'" );
         lc_JICMap.put( "JIC_ATA_CD", "'" + aJIC_ATA_CD + "'" );
         lc_JICMap.put( "JIC_TASK_CD", "'" + iJIC_TASK_CD_1 + "'" );
         lc_JICMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_1 + "'" );
         lc_JICMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_1 + "'" );
         lc_JICMap.put( "REQ_QT", "'" + iREQ_QT + "'" );
         lc_JICMap.put( "POSITION", "'" + iPOSITION + "'" );
         lc_JICMap.put( "REQ_ACTION_CD", "'" + iREQ_ACTION_CD + "'" );
         runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC_PART, lc_JICMap ) );
      }

      lc_JICMap.clear();
      lc_JICMap.put( "ASSMBL_CD", "'" + aASSMBL_CD + "'" );
      lc_JICMap.put( "JIC_ATA_CD", "'" + aJIC_ATA_CD + "'" );
      lc_JICMap.put( "JIC_TASK_CD", "'" + iJIC_TASK_CD_1 + "'" );
      lc_JICMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_2 + "'" );
      lc_JICMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_2 + "'" );
      lc_JICMap.put( "REQ_QT", "'" + iREQ_QT + "'" );
      lc_JICMap.put( "POSITION", null );
      lc_JICMap.put( "REQ_ACTION_CD", "'" + iREQ_ACTION_CD + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC_PART, lc_JICMap ) );

      // C_JIC_TOOL
      lc_JICMap.clear();
      lc_JICMap.put( "ASSMBL_CD", "'" + aASSMBL_CD + "'" );
      lc_JICMap.put( "JIC_ATA_CD", "'" + aJIC_ATA_CD + "'" );
      lc_JICMap.put( "JIC_TASK_CD", "'" + iJIC_TASK_CD_1 + "'" );
      lc_JICMap.put( "PART_NO_OEM", "'" + iPN_Tool_1 + "'" );
      lc_JICMap.put( "MANUFACT_REF", "'" + iMF_Tool_1 + "'" );
      lc_JICMap.put( "REQ_HR", "'" + iREQ_HR + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC_TOOL, lc_JICMap ) );

      lc_JICMap.clear();
      lc_JICMap.put( "ASSMBL_CD", "'" + aASSMBL_CD + "'" );
      lc_JICMap.put( "JIC_ATA_CD", "'" + aJIC_ATA_CD + "'" );
      lc_JICMap.put( "JIC_TASK_CD", "'" + iJIC_TASK_CD_1 + "'" );
      lc_JICMap.put( "PART_NO_OEM", "'" + iPN_Tool_2 + "'" );
      lc_JICMap.put( "MANUFACT_REF", "'" + iMF_Tool_2 + "'" );
      lc_JICMap.put( "REQ_HR", "'" + iREQ_HR + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC_TOOL, lc_JICMap ) );

      // C_JIC_STEP
      lc_JICMap.clear();
      lc_JICMap.put( "ASSMBL_CD", "'" + aASSMBL_CD + "'" );
      lc_JICMap.put( "JIC_ATA_CD", "'" + aJIC_ATA_CD + "'" );
      lc_JICMap.put( "JIC_TASK_CD", "'" + iJIC_TASK_CD_1 + "'" );
      lc_JICMap.put( "JOB_STEP_ORD", "'" + iJOB_STEP_ORD_1 + "'" );
      lc_JICMap.put( "JOB_STEP_DESC", "'" + iJOB_STEP_DESC_1 + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC_STEP, lc_JICMap ) );

      lc_JICMap.clear();
      lc_JICMap.put( "ASSMBL_CD", "'" + aASSMBL_CD + "'" );
      lc_JICMap.put( "JIC_ATA_CD", "'" + aJIC_ATA_CD + "'" );
      lc_JICMap.put( "JIC_TASK_CD", "'" + iJIC_TASK_CD_1 + "'" );
      lc_JICMap.put( "JOB_STEP_ORD", "'" + iJOB_STEP_ORD_2 + "'" );
      lc_JICMap.put( "JOB_STEP_DESC", "'" + iJOB_STEP_DESC_2 + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC_STEP, lc_JICMap ) );

   }


   /**
    * This function is to insert data into staging tables.
    *
    *
    *
    */
   public void prepareData_2( String aASSMBL_CD, String aJIC_ATA_CD ) {
      // C_ASSMBL_PANEL table
      Map<String, String> lc_JICMap = new LinkedHashMap<>();

      if ( aASSMBL_CD.contains( "ACFT" ) ) {

         lc_JICMap.put( "ASSMBL_CD", "'" + aASSMBL_CD + "'" );
         lc_JICMap.put( "PANEL_CD", "'" + iPANEL_CD_1 + "'" );
         lc_JICMap.put( "ZONE_CD", "'" + iZONE_CD_1 + "'" );
         lc_JICMap.put( "PANEL_NAME", "'" + iPANEL_NAME_1 + "'" );
         lc_JICMap.put( "PANEL_LDESC", "'" + iPANEL_LDESC_1 + "'" );

         // insert map
         runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ASSMBL_PANEL, lc_JICMap ) );

         // C_ASSMBL_ZONE table
         lc_JICMap.clear();
         lc_JICMap.put( "ASSMBL_CD", "'" + aASSMBL_CD + "'" );
         lc_JICMap.put( "ZONE_CD", "'" + iZONE_CD_1 + "'" );
         lc_JICMap.put( "ZONE_NAME", "'" + iZONE_NAME_1 + "'" );
         lc_JICMap.put( "NH_ZONE_CD", "'" + iNH_ZONE_CD_1 + "'" );
         lc_JICMap.put( "ZONE_LDESC", "'" + iZONE_LDESC_1 + "'" );
         lc_JICMap.put( "HR_LIMIT", "'" + iHR_LIMIT + "'" );

         // insert map
         runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ASSMBL_ZONE, lc_JICMap ) );
      } else {
         lc_JICMap.put( "ASSMBL_CD", "'" + aASSMBL_CD + "'" );
         lc_JICMap.put( "PANEL_CD", "'" + iPANEL_CD_1 + "'" );
         lc_JICMap.put( "ZONE_CD", "'" + iZONE_CD_2 + "'" );
         lc_JICMap.put( "PANEL_NAME", "'" + iPANEL_NAME_1 + "'" );
         lc_JICMap.put( "PANEL_LDESC", "'" + iPANEL_LDESC_1 + "'" );

         // insert map
         runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ASSMBL_PANEL, lc_JICMap ) );

         // C_ASSMBL_ZONE table
         lc_JICMap.clear();
         lc_JICMap.put( "ASSMBL_CD", "'" + aASSMBL_CD + "'" );
         lc_JICMap.put( "ZONE_CD", "'" + iZONE_CD_2 + "'" );
         lc_JICMap.put( "ZONE_NAME", "'" + iZONE_NAME_2 + "'" );
         // lc_JICMap.put( "NH_ZONE_CD", "'" + iNH_ZONE_CD_2 + "'" );
         lc_JICMap.put( "ZONE_LDESC", "'" + iZONE_LDESC_2 + "'" );
         lc_JICMap.put( "HR_LIMIT", "'" + iHR_LIMIT + "'" );

         // insert map
         runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ASSMBL_ZONE, lc_JICMap ) );

      }

      // C_JIC
      lc_JICMap.clear();
      lc_JICMap.put( "ASSMBL_CD", "'" + aASSMBL_CD + "'" );
      lc_JICMap.put( "JIC_ATA_CD", "'" + aJIC_ATA_CD + "'" );
      lc_JICMap.put( "JIC_TASK_CD", "'" + iJIC_TASK_CD_1 + "'" );
      lc_JICMap.put( "JIC_TASK_CLASS_CD", "'" + iJIC_TASK_CLASS_CD + "'" );
      lc_JICMap.put( "JIC_TASK_NAME", "'" + iJIC_TASK_NAME_1 + "'" );
      lc_JICMap.put( "JIC_TASK_DESC", "'" + iJIC_TASK_DESC_1 + "'" );
      lc_JICMap.put( "JIC_TASK_REF_SDESC", "'" + iJIC_TASK_REF_SDESC_1 + "'" );
      lc_JICMap.put( "TASK_ORIGINATOR_CD", "'" + iTASK_ORIGINATOR_CD + "'" );
      lc_JICMap.put( "APPLICABILITY_DESC", "'" + iAPPLICABILITY_DESC + "'" );
      lc_JICMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );
      lc_JICMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );
      lc_JICMap.put( "DURATION_HOURS", "'" + iDURATION_HOURS + "'" );
      lc_JICMap.put( "INSTRUCTIONS", "'" + iINSTRUCTIONS + "'" );
      if ( aASSMBL_CD.contains( "ACFT" ) ) {
         lc_JICMap.put( "ZONE_LIST", "'" + iZONE_CD_1 + "'" );
      } else {
         lc_JICMap.put( "ZONE_LIST", "'" + iZONE_CD_2 + "'" );

      }
      lc_JICMap.put( "PANEL_LIST", "'" + iPANEL_CD_1 + "'" );
      lc_JICMap.put( "EXT_KEY_SDESC", "'" + iEXT_KEY_SDESC + "'" );
      lc_JICMap.put( "TASK_PRIORITY_CD", "'" + iTASK_PRIORITY_CD_1 + "'" );
      lc_JICMap.put( "TASK_APPL_LDESC", "'" + iTASK_APPL_LDESC + "'" );
      lc_JICMap.put( "ENGINEERING_LDESC", "'" + iENGINEERING_LDESC + "'" );
      lc_JICMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC, lc_JICMap ) );

      // C_JIC_IETM_TOPIC
      lc_JICMap.clear();
      lc_JICMap.put( "ASSMBL_CD", "'" + aASSMBL_CD + "'" );
      lc_JICMap.put( "JIC_ATA_CD", "'" + aJIC_ATA_CD + "'" );
      lc_JICMap.put( "JIC_TASK_CD", "'" + iJIC_TASK_CD_1 + "'" );
      lc_JICMap.put( "JIC_IETM_CD", "'" + iJIC_IETM_CD_1 + "'" );
      lc_JICMap.put( "JIC_TOPIC_SDESC", "'" + iJIC_TOPIC_SDESC_1 + "'" );
      lc_JICMap.put( "JIC_IETM_ORD", "'" + iJIC_IETM_ORD_1 + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC_IETM_TOPIC, lc_JICMap ) );

      if ( aASSMBL_CD.contains( "ACFT" ) ) {
         lc_JICMap.clear();
         lc_JICMap.put( "ASSMBL_CD", "'" + aASSMBL_CD + "'" );
         lc_JICMap.put( "JIC_ATA_CD", "'" + aJIC_ATA_CD + "'" );
         lc_JICMap.put( "JIC_TASK_CD", "'" + iJIC_TASK_CD_1 + "'" );
         lc_JICMap.put( "JIC_IETM_CD", "'" + iJIC_IETM_CD_2 + "'" );
         lc_JICMap.put( "JIC_TOPIC_SDESC", "'" + iJIC_TOPIC_SDESC_2 + "'" );
         lc_JICMap.put( "JIC_IETM_ORD", "'" + iJIC_IETM_ORD_2 + "'" );
         runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC_IETM_TOPIC, lc_JICMap ) );
      }

      // C_JIC_LABOR
      lc_JICMap.clear();
      lc_JICMap.put( "ASSMBL_CD", "'" + aASSMBL_CD + "'" );
      lc_JICMap.put( "JIC_ATA_CD", "'" + aJIC_ATA_CD + "'" );
      lc_JICMap.put( "JIC_TASK_CD", "'" + iJIC_TASK_CD_1 + "'" );
      lc_JICMap.put( "LABOR_SKILL_CD", "'" + iLABOR_SKILL_CD + "'" );
      lc_JICMap.put( "MAN_PWR_CT", "'" + iMAN_PWR_CT + "'" );
      lc_JICMap.put( "SCHED_WORK_HRS", "'" + iSCHED_WORK_HRS + "'" );
      lc_JICMap.put( "SCHED_INSP_HRS", "'" + iSCHED_INSP_HRS + "'" );
      lc_JICMap.put( "SCHED_CERT_HRS", "'" + iSCHED_CERT_HRS + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC_LABOR, lc_JICMap ) );

      // C_JIC_PART
      if ( aASSMBL_CD.contains( "ACFT" ) ) {
         lc_JICMap.clear();
         lc_JICMap.put( "ASSMBL_CD", "'" + aASSMBL_CD + "'" );
         lc_JICMap.put( "JIC_ATA_CD", "'" + aJIC_ATA_CD + "'" );
         lc_JICMap.put( "JIC_TASK_CD", "'" + iJIC_TASK_CD_1 + "'" );
         lc_JICMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_1 + "'" );
         lc_JICMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_1 + "'" );
         lc_JICMap.put( "REQ_QT", "'" + iREQ_QT + "'" );
         lc_JICMap.put( "POSITION", "'" + iPOSITION + "'" );
         lc_JICMap.put( "REQ_ACTION_CD", "'" + iREQ_ACTION_CD + "'" );
         runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC_PART, lc_JICMap ) );
      }

      lc_JICMap.clear();
      lc_JICMap.put( "ASSMBL_CD", "'" + aASSMBL_CD + "'" );
      lc_JICMap.put( "JIC_ATA_CD", "'" + aJIC_ATA_CD + "'" );
      lc_JICMap.put( "JIC_TASK_CD", "'" + iJIC_TASK_CD_1 + "'" );
      lc_JICMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_2 + "'" );
      lc_JICMap.put( "MANUFACT_REF", "'" + iMANUFACT_REF_2 + "'" );
      lc_JICMap.put( "REQ_QT", "'" + iREQ_QT_2 + "'" );
      lc_JICMap.put( "POSITION", null );
      lc_JICMap.put( "REQ_ACTION_CD", "'" + iREQ_ACTION_CD + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC_PART, lc_JICMap ) );

      // C_JIC_TOOL
      lc_JICMap.clear();
      lc_JICMap.put( "ASSMBL_CD", "'" + aASSMBL_CD + "'" );
      lc_JICMap.put( "JIC_ATA_CD", "'" + aJIC_ATA_CD + "'" );
      lc_JICMap.put( "JIC_TASK_CD", "'" + iJIC_TASK_CD_1 + "'" );
      lc_JICMap.put( "PART_NO_OEM", "'" + iPN_Tool_1 + "'" );
      lc_JICMap.put( "MANUFACT_REF", "'" + iMF_Tool_1 + "'" );
      lc_JICMap.put( "REQ_HR", "'" + iREQ_HR + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC_TOOL, lc_JICMap ) );

      lc_JICMap.clear();
      lc_JICMap.put( "ASSMBL_CD", "'" + aASSMBL_CD + "'" );
      lc_JICMap.put( "JIC_ATA_CD", "'" + aJIC_ATA_CD + "'" );
      lc_JICMap.put( "JIC_TASK_CD", "'" + iJIC_TASK_CD_1 + "'" );
      lc_JICMap.put( "PART_NO_OEM", "'" + iPN_Tool_2 + "'" );
      lc_JICMap.put( "MANUFACT_REF", "'" + iMF_Tool_2 + "'" );
      lc_JICMap.put( "REQ_HR", "'" + iREQ_HR + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC_TOOL, lc_JICMap ) );

      // C_JIC_STEP
      lc_JICMap.clear();
      lc_JICMap.put( "ASSMBL_CD", "'" + aASSMBL_CD + "'" );
      lc_JICMap.put( "JIC_ATA_CD", "'" + aJIC_ATA_CD + "'" );
      lc_JICMap.put( "JIC_TASK_CD", "'" + iJIC_TASK_CD_1 + "'" );
      lc_JICMap.put( "JOB_STEP_ORD", "'" + iJOB_STEP_ORD_1 + "'" );
      lc_JICMap.put( "JOB_STEP_DESC", "'" + iJOB_STEP_DESC_1 + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC_STEP, lc_JICMap ) );

      lc_JICMap.clear();
      lc_JICMap.put( "ASSMBL_CD", "'" + aASSMBL_CD + "'" );
      lc_JICMap.put( "JIC_ATA_CD", "'" + aJIC_ATA_CD + "'" );
      lc_JICMap.put( "JIC_TASK_CD", "'" + iJIC_TASK_CD_1 + "'" );
      lc_JICMap.put( "JOB_STEP_ORD", "'" + iJOB_STEP_ORD_2 + "'" );
      lc_JICMap.put( "JOB_STEP_DESC", "'" + iJOB_STEP_DESC_2 + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC_STEP, lc_JICMap ) );

   }


   /**
    * This function is to insert data into staging tables.
    *
    *
    *
    */
   public void prepareDataOPER_1128( String aASSMBL_CD, String aJIC_ATA_CD, String aINSPandCERT ) {
      // C_ASSMBL_PANEL table
      Map<String, String> lc_JICMap = new LinkedHashMap<>();

      if ( aASSMBL_CD.contains( "ACFT" ) ) {

         lc_JICMap.put( "ASSMBL_CD", "'" + aASSMBL_CD + "'" );
         lc_JICMap.put( "PANEL_CD", "'" + iPANEL_CD_1 + "'" );
         lc_JICMap.put( "ZONE_CD", "'" + iZONE_CD_1 + "'" );
         lc_JICMap.put( "PANEL_NAME", "'" + iPANEL_NAME_1 + "'" );
         lc_JICMap.put( "PANEL_LDESC", "'" + iPANEL_LDESC_1 + "'" );

         // insert map
         runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ASSMBL_PANEL, lc_JICMap ) );

         // C_ASSMBL_ZONE table
         lc_JICMap.clear();
         lc_JICMap.put( "ASSMBL_CD", "'" + aASSMBL_CD + "'" );
         lc_JICMap.put( "ZONE_CD", "'" + iZONE_CD_1 + "'" );
         lc_JICMap.put( "ZONE_NAME", "'" + iZONE_NAME_1 + "'" );
         lc_JICMap.put( "NH_ZONE_CD", "'" + iNH_ZONE_CD_1 + "'" );
         lc_JICMap.put( "ZONE_LDESC", "'" + iZONE_LDESC_1 + "'" );
         lc_JICMap.put( "HR_LIMIT", "'" + iHR_LIMIT + "'" );

         // insert map
         runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ASSMBL_ZONE, lc_JICMap ) );
      } else {
         lc_JICMap.put( "ASSMBL_CD", "'" + aASSMBL_CD + "'" );
         lc_JICMap.put( "PANEL_CD", "'" + iPANEL_CD_1 + "'" );
         lc_JICMap.put( "ZONE_CD", "'" + iZONE_CD_2 + "'" );
         lc_JICMap.put( "PANEL_NAME", "'" + iPANEL_NAME_1 + "'" );
         lc_JICMap.put( "PANEL_LDESC", "'" + iPANEL_LDESC_1 + "'" );

         // insert map
         runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ASSMBL_PANEL, lc_JICMap ) );

         // C_ASSMBL_ZONE table
         lc_JICMap.clear();
         lc_JICMap.put( "ASSMBL_CD", "'" + aASSMBL_CD + "'" );
         lc_JICMap.put( "ZONE_CD", "'" + iZONE_CD_2 + "'" );
         lc_JICMap.put( "ZONE_NAME", "'" + iZONE_NAME_2 + "'" );
         // lc_JICMap.put( "NH_ZONE_CD", "'" + iNH_ZONE_CD_2 + "'" );
         lc_JICMap.put( "ZONE_LDESC", "'" + iZONE_LDESC_2 + "'" );
         lc_JICMap.put( "HR_LIMIT", "'" + iHR_LIMIT + "'" );

         // insert map
         runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ASSMBL_ZONE, lc_JICMap ) );

      }

      // C_JIC
      lc_JICMap.clear();
      lc_JICMap.put( "ASSMBL_CD", "'" + aASSMBL_CD + "'" );
      lc_JICMap.put( "JIC_ATA_CD", "'" + aJIC_ATA_CD + "'" );
      lc_JICMap.put( "JIC_TASK_CD", "'" + iJIC_TASK_CD_1 + "'" );
      lc_JICMap.put( "JIC_TASK_CLASS_CD", "'" + iJIC_TASK_CLASS_CD + "'" );
      lc_JICMap.put( "JIC_TASK_NAME", "'" + iJIC_TASK_NAME_1 + "'" );
      lc_JICMap.put( "JIC_TASK_DESC", "'" + iJIC_TASK_DESC_1 + "'" );
      lc_JICMap.put( "JIC_TASK_REF_SDESC", "'" + iJIC_TASK_REF_SDESC_1 + "'" );
      lc_JICMap.put( "TASK_ORIGINATOR_CD", "'" + iTASK_ORIGINATOR_CD + "'" );
      lc_JICMap.put( "APPLICABILITY_DESC", "'" + iAPPLICABILITY_DESC + "'" );
      lc_JICMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );
      lc_JICMap.put( "SUBCLASS_CD", "'" + iSUBCLASS_CD + "'" );
      lc_JICMap.put( "DURATION_HOURS", "'" + iDURATION_HOURS + "'" );
      lc_JICMap.put( "INSTRUCTIONS", "'" + iINSTRUCTIONS + "'" );
      if ( aASSMBL_CD.contains( "ACFT" ) ) {
         lc_JICMap.put( "ZONE_LIST", "'" + iZONE_CD_1 + "'" );
      } else {
         lc_JICMap.put( "ZONE_LIST", "'" + iZONE_CD_2 + "'" );

      }
      lc_JICMap.put( "PANEL_LIST", "'" + iPANEL_CD_1 + "'" );
      lc_JICMap.put( "EXT_KEY_SDESC", "'" + iEXT_KEY_SDESC + "'" );
      lc_JICMap.put( "TASK_PRIORITY_CD", "'" + iTASK_PRIORITY_CD_1 + "'" );
      lc_JICMap.put( "TASK_APPL_LDESC", "'" + iTASK_APPL_LDESC + "'" );
      lc_JICMap.put( "ENGINEERING_LDESC", "'" + iENGINEERING_LDESC + "'" );
      lc_JICMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC, lc_JICMap ) );

      // C_JIC_LABOR
      lc_JICMap.clear();
      lc_JICMap.put( "ASSMBL_CD", "'" + aASSMBL_CD + "'" );
      lc_JICMap.put( "JIC_ATA_CD", "'" + aJIC_ATA_CD + "'" );
      lc_JICMap.put( "JIC_TASK_CD", "'" + iJIC_TASK_CD_1 + "'" );
      lc_JICMap.put( "LABOR_SKILL_CD", "'" + iLABOR_SKILL_CD + "'" );
      lc_JICMap.put( "MAN_PWR_CT", "'" + iMAN_PWR_CT + "'" );
      lc_JICMap.put( "SCHED_WORK_HRS", "'" + iSCHED_WORK_HRS + "'" );
      if ( aINSPandCERT != null ) {
         lc_JICMap.put( "SCHED_INSP_HRS", "'" + aINSPandCERT + "'" );
         lc_JICMap.put( "SCHED_CERT_HRS", "'" + aINSPandCERT + "'" );
      } else {
         lc_JICMap.put( "SCHED_INSP_HRS", null );
         lc_JICMap.put( "SCHED_CERT_HRS", null );
      }
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC_LABOR, lc_JICMap ) );

   }


   /**
    * This function is to insert data into staging tables.
    *
    *
    *
    */
   public void prepareDataOPER_30663( String aASSMBL_CD, String aJIC_ATA_CD, String aINSPandCERT ) {
      // C_ASSMBL_PANEL table
      Map<String, String> lc_JICMap = new LinkedHashMap<>();

      if ( aASSMBL_CD.contains( "ACFT" ) ) {

         lc_JICMap.put( "ASSMBL_CD", "'" + aASSMBL_CD + "'" );
         lc_JICMap.put( "PANEL_CD", "'" + iPANEL_CD_1 + "'" );
         lc_JICMap.put( "ZONE_CD", "'" + iZONE_CD_1 + "'" );
         lc_JICMap.put( "PANEL_NAME", "'" + iPANEL_NAME_1 + "'" );
         lc_JICMap.put( "PANEL_LDESC", "'" + iPANEL_LDESC_1 + "'" );

         // insert map
         runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ASSMBL_PANEL, lc_JICMap ) );

         // C_ASSMBL_ZONE table
         lc_JICMap.clear();
         lc_JICMap.put( "ASSMBL_CD", "'" + aASSMBL_CD + "'" );
         lc_JICMap.put( "ZONE_CD", "'" + iZONE_CD_1 + "'" );
         lc_JICMap.put( "ZONE_NAME", "'" + iZONE_NAME_1 + "'" );
         lc_JICMap.put( "NH_ZONE_CD", "'" + iNH_ZONE_CD_1 + "'" );
         lc_JICMap.put( "ZONE_LDESC", "'" + iZONE_LDESC_1 + "'" );
         lc_JICMap.put( "HR_LIMIT", "'" + iHR_LIMIT + "'" );

         // insert map
         runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ASSMBL_ZONE, lc_JICMap ) );
      } else {
         lc_JICMap.put( "ASSMBL_CD", "'" + aASSMBL_CD + "'" );
         lc_JICMap.put( "PANEL_CD", "'" + iPANEL_CD_1 + "'" );
         lc_JICMap.put( "ZONE_CD", "'" + iZONE_CD_2 + "'" );
         lc_JICMap.put( "PANEL_NAME", "'" + iPANEL_NAME_1 + "'" );
         lc_JICMap.put( "PANEL_LDESC", "'" + iPANEL_LDESC_1 + "'" );

         // insert map
         runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ASSMBL_PANEL, lc_JICMap ) );

         // C_ASSMBL_ZONE table
         lc_JICMap.clear();
         lc_JICMap.put( "ASSMBL_CD", "'" + aASSMBL_CD + "'" );
         lc_JICMap.put( "ZONE_CD", "'" + iZONE_CD_2 + "'" );
         lc_JICMap.put( "ZONE_NAME", "'" + iZONE_NAME_2 + "'" );
         // lc_JICMap.put( "NH_ZONE_CD", "'" + iNH_ZONE_CD_2 + "'" );
         lc_JICMap.put( "ZONE_LDESC", "'" + iZONE_LDESC_2 + "'" );
         lc_JICMap.put( "HR_LIMIT", "'" + iHR_LIMIT + "'" );

         // insert map
         runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ASSMBL_ZONE, lc_JICMap ) );

      }

      // C_JIC
      lc_JICMap.clear();
      lc_JICMap.put( "ASSMBL_CD", "'" + aASSMBL_CD + "'" );
      lc_JICMap.put( "JIC_ATA_CD", "'" + aJIC_ATA_CD + "'" );
      lc_JICMap.put( "JIC_TASK_CD", "'" + iJIC_TASK_CD_1 + "'" );
      lc_JICMap.put( "JIC_TASK_CLASS_CD", "'AUTO'" );
      lc_JICMap.put( "JIC_TASK_NAME", "'" + iJIC_TASK_NAME_1 + "'" );
      lc_JICMap.put( "JIC_TASK_DESC", "'" + iJIC_TASK_DESC_1 + "'" );
      lc_JICMap.put( "JIC_TASK_REF_SDESC", "'" + iJIC_TASK_REF_SDESC_1 + "'" );
      lc_JICMap.put( "TASK_ORIGINATOR_CD", "'" + iTASK_ORIGINATOR_CD + "'" );
      lc_JICMap.put( "APPLICABILITY_DESC", "'" + iAPPLICABILITY_DESC + "'" );
      lc_JICMap.put( "SUBCLASS_CD", "'TEST4'" );
      lc_JICMap.put( "DURATION_HOURS", "'" + iDURATION_HOURS + "'" );
      lc_JICMap.put( "INSTRUCTIONS", "'" + iINSTRUCTIONS + "'" );
      if ( aASSMBL_CD.contains( "ACFT" ) ) {
         lc_JICMap.put( "ZONE_LIST", "'" + iZONE_CD_1 + "'" );
      } else {
         lc_JICMap.put( "ZONE_LIST", "'" + iZONE_CD_2 + "'" );

      }
      lc_JICMap.put( "PANEL_LIST", "'" + iPANEL_CD_1 + "'" );
      lc_JICMap.put( "EXT_KEY_SDESC", "'" + iEXT_KEY_SDESC + "'" );
      lc_JICMap.put( "TASK_PRIORITY_CD", "'" + iTASK_PRIORITY_CD_1 + "'" );
      lc_JICMap.put( "TASK_APPL_LDESC", "'" + iTASK_APPL_LDESC + "'" );
      lc_JICMap.put( "ENGINEERING_LDESC", "'" + iENGINEERING_LDESC + "'" );
      lc_JICMap.put( "WORK_TYPE_LIST", "'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC, lc_JICMap ) );

      // C_JIC_LABOR
      lc_JICMap.clear();
      lc_JICMap.put( "ASSMBL_CD", "'" + aASSMBL_CD + "'" );
      lc_JICMap.put( "JIC_ATA_CD", "'" + aJIC_ATA_CD + "'" );
      lc_JICMap.put( "JIC_TASK_CD", "'" + iJIC_TASK_CD_1 + "'" );
      lc_JICMap.put( "LABOR_SKILL_CD", "'" + iLABOR_SKILL_CD + "'" );
      lc_JICMap.put( "MAN_PWR_CT", "'" + iMAN_PWR_CT + "'" );
      lc_JICMap.put( "SCHED_WORK_HRS", "'" + iSCHED_WORK_HRS + "'" );
      if ( aINSPandCERT != null ) {
         lc_JICMap.put( "SCHED_INSP_HRS", "'" + aINSPandCERT + "'" );
         lc_JICMap.put( "SCHED_CERT_HRS", "'" + aINSPandCERT + "'" );
      } else {
         lc_JICMap.put( "SCHED_INSP_HRS", null );
         lc_JICMap.put( "SCHED_CERT_HRS", null );
      }
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_JIC_LABOR, lc_JICMap ) );

   }


   /**
    * This function is to verify TASK_TOOL_LIST.
    *
    *
    *
    */

   public void verifyTASKTOOLLIST( simpleIDs aTaskIds, simpleIDs aToolIds, String aSchedHR ) {

      String[] iIds = { "SCHED_HR" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_DB_ID", aTaskIds.getNO_DB_ID() );
      lArgs.addArguments( "TASK_ID", aTaskIds.getNO_ID() );
      lArgs.addArguments( "BOM_PART_DB_ID", aToolIds.getNO_DB_ID() );
      lArgs.addArguments( "BOM_PART_ID", aToolIds.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_TOOL_LIST, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );
      Assert.assertTrue( "SCHED_HR", llists.get( 0 ).get( 0 ).equalsIgnoreCase( aSchedHR ) );

   }


   /**
    * This function is to retrieve tool information from Mxi tables
    *
    *
    */
   public simpleIDs getJicToolInforFromMxi( String aPNOEM, String sMANFT ) {

      String lQueryString =
            "select  eqp_bom_part.bom_part_db_id,eqp_bom_part.bom_part_id from eqp_assmbl_bom "
                  + "inner join eqp_bom_part on "
                  + "eqp_assmbl_bom.assmbl_db_id=eqp_bom_part.assmbl_db_id and "
                  + "eqp_assmbl_bom.assmbl_cd=eqp_bom_part.assmbl_cd and "
                  + "eqp_assmbl_bom.assmbl_bom_id=eqp_bom_part.assmbl_bom_id "
                  + "inner join eqp_assmbl_pos on "
                  + "eqp_assmbl_bom.assmbl_db_id=eqp_assmbl_pos.assmbl_db_id and "
                  + "eqp_assmbl_bom.assmbl_cd=eqp_assmbl_pos.assmbl_cd and "
                  + "eqp_assmbl_bom.assmbl_bom_id=eqp_assmbl_pos.assmbl_bom_id "
                  + "inner join eqp_part_baseline on "
                  + "eqp_bom_part.bom_part_db_id=eqp_part_baseline.bom_part_db_id and "
                  + "eqp_bom_part.bom_part_id=eqp_part_baseline.bom_part_id "
                  + "inner join eqp_part_no on "
                  + "eqp_part_baseline.part_no_db_id=eqp_part_no.part_no_db_id and "
                  + "eqp_part_baseline.part_no_id=eqp_part_no.part_no_id "
                  + "where eqp_part_no.part_no_oem='" + aPNOEM + "' and eqp_part_no.manufact_cd='"
                  + sMANFT + "'";

      String[] iIds = { "BOM_PART_DB_ID", "BOM_PART_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      List<ArrayList<String>> llists = execute( lQueryString, lfields );

      simpleIDs lpartInfor = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lpartInfor;

   }


   /**
    * This function is to verify TASK_PART_LIST.
    *
    *
    *
    */

   public void verifyTASKPARTLIST( simpleIDs aTaskIds, String aAssmblCD, assmbJICpart aPart,
         String aReqQT, String aReqActionCD ) {

      String[] iIds = { "ASSMBL_DB_ID", "ASSMBL_CD", "ASSMBL_BOM_ID", "ASSMBL_POS_ID",
            "BOM_PART_DB_ID", "BOM_PART_ID", "REQ_QT", "REQ_ACTION_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_DB_ID", aTaskIds.getNO_DB_ID() );
      lArgs.addArguments( "TASK_ID", aTaskIds.getNO_ID() );
      lArgs.addArguments( "ASSMBL_CD", aAssmblCD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_PART_LIST, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );
      assmbJICpart lpartInfor = new assmbJICpart( llists.get( 0 ).get( 0 ),
            llists.get( 0 ).get( 1 ), llists.get( 0 ).get( 2 ), llists.get( 0 ).get( 3 ),
            llists.get( 0 ).get( 4 ), llists.get( 0 ).get( 5 ) );

      Assert.assertTrue( "JIC part information should keep the same: ",
            lpartInfor.equals( aPart ) );
      Assert.assertTrue( "REQ_QT", llists.get( 0 ).get( 6 ).equalsIgnoreCase( aReqQT ) );
      Assert.assertTrue( "REQ_ACTION_CD",
            llists.get( 0 ).get( 7 ).equalsIgnoreCase( aReqActionCD ) );

   }


   /**
    * This function is to retrieve jic part information from Mxi tables
    *
    *
    */
   public assmbJICpart getJicPartFromMxi( String aPNOEM, String sMANFT ) {

      String lQueryString =
            "select eqp_assmbl_bom.assmbl_db_id, eqp_assmbl_bom.assmbl_cd, eqp_assmbl_bom.assmbl_bom_id, eqp_assmbl_pos.assmbl_pos_id, "
                  + "eqp_bom_part.bom_part_db_id,eqp_bom_part.bom_part_id from eqp_assmbl_bom "
                  + "inner join eqp_bom_part on "
                  + "eqp_assmbl_bom.assmbl_db_id=eqp_bom_part.assmbl_db_id and "
                  + "eqp_assmbl_bom.assmbl_cd=eqp_bom_part.assmbl_cd and "
                  + "eqp_assmbl_bom.assmbl_bom_id=eqp_bom_part.assmbl_bom_id "
                  + "inner join eqp_assmbl_pos on "
                  + "eqp_assmbl_bom.assmbl_db_id=eqp_assmbl_pos.assmbl_db_id and "
                  + "eqp_assmbl_bom.assmbl_cd=eqp_assmbl_pos.assmbl_cd and "
                  + "eqp_assmbl_bom.assmbl_bom_id=eqp_assmbl_pos.assmbl_bom_id "
                  + "inner join eqp_part_baseline on "
                  + "eqp_bom_part.bom_part_db_id=eqp_part_baseline.bom_part_db_id and "
                  + "eqp_bom_part.bom_part_id=eqp_part_baseline.bom_part_id "
                  + "inner join eqp_part_no on "
                  + "eqp_part_baseline.part_no_db_id=eqp_part_no.part_no_db_id and "
                  + "eqp_part_baseline.part_no_id=eqp_part_no.part_no_id "
                  + "where eqp_part_no.part_no_oem='" + aPNOEM + "' and eqp_part_no.manufact_cd='"
                  + sMANFT + "'";

      String[] iIds = { "ASSMBL_DB_ID", "ASSMBL_CD", "ASSMBL_BOM_ID", "ASSMBL_POS_ID",
            "BOM_PART_DB_ID", "BOM_PART_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      List<ArrayList<String>> llists = execute( lQueryString, lfields );

      assmbJICpart lpartInfor = new assmbJICpart( llists.get( 0 ).get( 0 ),
            llists.get( 0 ).get( 1 ), llists.get( 0 ).get( 2 ), llists.get( 0 ).get( 3 ),
            llists.get( 0 ).get( 4 ), llists.get( 0 ).get( 5 ) );

      return lpartInfor;

   }


   /**
    * This function is to verify TASK_LABOUR_LIST and retrieve task defn IDs table
    *
    *
    */
   public void verifyTASKLABOURLIST( simpleIDs aTaskIds, String aLabourSkillCD, String aManPerCt,
         String aWorkPerfHR, String aInspHR, String aCertHR ) {

      String[] iIds = { "LABOUR_SKILL_CD", "MAN_PWR_CT", "WORK_PERF_HR", "INSP_HR", "CERT_HR" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_DB_ID", aTaskIds.getNO_DB_ID() );
      lArgs.addArguments( "TASK_ID", aTaskIds.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_LABOUR_LIST, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "LABOUR_SKILL_CD",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aLabourSkillCD ) );
      Assert.assertTrue( "MAN_PWR_CT", llists.get( 0 ).get( 1 ).equalsIgnoreCase( aManPerCt ) );
      Assert.assertTrue( "WORK_PERF_HR", llists.get( 0 ).get( 2 ).equalsIgnoreCase( aWorkPerfHR ) );
      Assert.assertTrue( "INSP_HR", llists.get( 0 ).get( 3 ).equalsIgnoreCase( aInspHR ) );
      Assert.assertTrue( "CERT_HR", llists.get( 0 ).get( 4 ).equalsIgnoreCase( aCertHR ) );

   }


   /**
    * This function is to verify TASK_LABOUR_LIST and retrieve task defn IDs table
    *
    *
    */
   public void verifyTASKLABOURLIST_2( simpleIDs aTaskIds, String aLabourSkillCD, String aManPerCt,
         String aWorkPerfHR, String aInspHR, String aCertHR, String aCERT_BOOL,
         String aINSP_BOOL ) {

      String[] iIds = { "LABOUR_SKILL_CD", "MAN_PWR_CT", "WORK_PERF_HR", "INSP_HR", "CERT_HR",
            "CERT_BOOL", "INSP_BOOL" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_DB_ID", aTaskIds.getNO_DB_ID() );
      lArgs.addArguments( "TASK_ID", aTaskIds.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_LABOUR_LIST, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "LABOUR_SKILL_CD",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aLabourSkillCD ) );
      Assert.assertTrue( "MAN_PWR_CT", llists.get( 0 ).get( 1 ).equalsIgnoreCase( aManPerCt ) );
      Assert.assertTrue( "WORK_PERF_HR", llists.get( 0 ).get( 2 ).equalsIgnoreCase( aWorkPerfHR ) );
      Assert.assertTrue( "INSP_HR", llists.get( 0 ).get( 3 ).equalsIgnoreCase( aInspHR ) );
      Assert.assertTrue( "CERT_HR", llists.get( 0 ).get( 4 ).equalsIgnoreCase( aCertHR ) );
      Assert.assertTrue( "CERT_BOOL", llists.get( 0 ).get( 5 ).equalsIgnoreCase( aCERT_BOOL ) );
      Assert.assertTrue( "INSP_BOOL", llists.get( 0 ).get( 6 ).equalsIgnoreCase( aINSP_BOOL ) );

   }


   /**
    * This function is to verify TASK_TASK and retrieve task defn IDs table
    *
    *
    */
   public simpleIDs verifyTASKTASK( simpleIDs aTaskIds, String aTaskClassCD, String aTaskPriorityCD,
         String aAssmblCD, String aTaskSubClassCD, String aTaskOriginatorCD, String aTaskCD,
         String aTaskName, String aTaskLdesc, String aInstructionLdesc, String aTaskApplLdesc,
         String aTaskRefSdesc, String aTaskApplEffLdesc, String aEngineeringLdesc,
         String aExtKeySdesc ) {

      String[] iIds = { "TASK_DEFN_DB_ID", "TASK_DEFN_ID", "TASK_CLASS_CD", "TASK_PRIORITY_CD",
            "ASSMBL_CD", "TASK_SUBCLASS_CD", "TASK_ORIGINATOR_CD", "TASK_CD", "TASK_NAME",
            "TASK_LDESC", "INSTRUCTION_LDESC", "TASK_APPL_LDESC", "TASK_REF_SDESC",
            "TASK_APPL_EFF_LDESC", "ENGINEERING_LDESC", "EXT_KEY_SDESC" };
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
      Assert.assertTrue( "ASSMBL_CD", llists.get( 0 ).get( 4 ).equalsIgnoreCase( aAssmblCD ) );
      Assert.assertTrue( "TASK_SUBCLASS_CD",
            llists.get( 0 ).get( 5 ).equalsIgnoreCase( aTaskSubClassCD ) );
      Assert.assertTrue( "TASK_ORIGINATOR_CD",
            llists.get( 0 ).get( 6 ).equalsIgnoreCase( aTaskOriginatorCD ) );
      Assert.assertTrue( "TASK_CD", llists.get( 0 ).get( 7 ).equalsIgnoreCase( aTaskCD ) );
      Assert.assertTrue( "TASK_NAME", llists.get( 0 ).get( 8 ).equalsIgnoreCase( aTaskName ) );
      Assert.assertTrue( "TASK_LDESC", llists.get( 0 ).get( 9 ).equalsIgnoreCase( aTaskLdesc ) );
      Assert.assertTrue( "INSTRUCTION_LDESC",
            llists.get( 0 ).get( 10 ).equalsIgnoreCase( aInstructionLdesc ) );
      Assert.assertTrue( "TASK_APPL_LDESC",
            llists.get( 0 ).get( 11 ).equalsIgnoreCase( aTaskApplLdesc ) );
      Assert.assertTrue( "TASK_REF_SDESC",
            llists.get( 0 ).get( 12 ).equalsIgnoreCase( aTaskRefSdesc ) );
      Assert.assertTrue( "TASK_APPL_EFF_LDESC",
            llists.get( 0 ).get( 13 ).equalsIgnoreCase( aTaskApplEffLdesc ) );
      Assert.assertTrue( "ENGINEERING_LDESC",
            llists.get( 0 ).get( 14 ).equalsIgnoreCase( aEngineeringLdesc ) );
      Assert.assertTrue( "EXT_KEY_SDESC",
            llists.get( 0 ).get( 15 ).equalsIgnoreCase( aExtKeySdesc ) );

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
    * This function is to verify TASK_PANEL and retrieve task IDs table
    *
    *
    */
   public simpleIDs verifyTASKPANEL( simpleIDs aPanelIds ) {

      String[] iIds = { "TASK_DB_ID", "TASK_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "PANEL_DB_ID", aPanelIds.getNO_DB_ID() );
      lArgs.addArguments( "PANEL_ID", aPanelIds.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_PANEL, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lIds;

   }


   /**
    * This function is to verify TASK_PANEL and retrieve task IDs table
    *
    *
    */
   public List<simpleIDs> verifyTASKPANELMultiple( simpleIDs aPanelIds ) {

      String[] iIds = { "TASK_DB_ID", "TASK_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "PANEL_DB_ID", aPanelIds.getNO_DB_ID() );
      lArgs.addArguments( "PANEL_ID", aPanelIds.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_PANEL, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      List<simpleIDs> llist = new ArrayList<simpleIDs>();
      // Get IDs
      for ( int i = 0; i < llists.size(); i++ ) {
         llist.add( new simpleIDs( llists.get( i ).get( 0 ), llists.get( i ).get( 1 ) ) );

      }

      return llist;

   }


   /**
    * This function is to verify EQP_TASK_ZONE table
    *
    *
    */
   public void verifyEQPTASKZONE( simpleIDs aZoneIds, simpleIDs aNHZoneIds, String aASSMBLCD,
         String aZoneCD, String aSDESC, String aLDESC ) {

      String[] iIds =
            { "NH_ZONE_DB_ID", "NH_ZONE_ID", "ASSMBL_CD", "ZONE_CD", "DESC_SDESC", "DESC_LDESC" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "ZONE_DB_ID", aZoneIds.getNO_DB_ID() );
      lArgs.addArguments( "ZONE_ID", aZoneIds.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EQP_TASK_ZONE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      if ( aNHZoneIds != null ) {
         Assert.assertTrue( "NH_ZONE_DB_ID",
               llists.get( 0 ).get( 0 ).equalsIgnoreCase( aNHZoneIds.getNO_DB_ID() ) );
         Assert.assertTrue( "NH_ZONE_ID",
               llists.get( 0 ).get( 1 ).equalsIgnoreCase( aNHZoneIds.getNO_ID() ) );
      }
      Assert.assertTrue( "ASSMBL_CD", llists.get( 0 ).get( 2 ).equalsIgnoreCase( aASSMBLCD ) );
      Assert.assertTrue( "ZONE_CD", llists.get( 0 ).get( 3 ).equalsIgnoreCase( aZoneCD ) );
      Assert.assertTrue( "DESC_SDESC", llists.get( 0 ).get( 4 ).equalsIgnoreCase( aSDESC ) );
      Assert.assertTrue( "DESC_LDESC", llists.get( 0 ).get( 5 ).equalsIgnoreCase( aLDESC ) );

   }


   /**
    * This function is to verify EQP_TASK_PANEL table
    *
    *
    */
   public simpleIDs verifyEQPTASKPANEL( String aSDESC, String aLDESC, String aASSMBLCD,
         String aPANELCD, simpleIDs aZoneIds ) {

      String[] iIds =
            { "PANEL_DB_ID", "PANEL_ID", "ASSMBL_CD", "PANEL_CD", "ZONE_DB_ID", "ZONE_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "DESC_SDESC", aSDESC );
      lArgs.addArguments( "DESC_LDESC", aLDESC );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EQP_TASK_PANEL, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      Assert.assertTrue( "ASSMBL_CD", llists.get( 0 ).get( 2 ).equalsIgnoreCase( aASSMBLCD ) );
      Assert.assertTrue( "PANEL_CD", llists.get( 0 ).get( 3 ).equalsIgnoreCase( aPANELCD ) );
      Assert.assertTrue( "ZONE_DB_ID",
            llists.get( 0 ).get( 4 ).equalsIgnoreCase( aZoneIds.getNO_DB_ID() ) );
      Assert.assertTrue( "ZONE_DB_ID",
            llists.get( 0 ).get( 5 ).equalsIgnoreCase( aZoneIds.getNO_ID() ) );

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
    * This function is to retrieve Zone IDs.
    *
    *
    */
   public simpleIDs getTaskIDs( String aTaskCD ) {

      String[] iIds = { "TASK_DB_ID", "TASK_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_CD", aTaskCD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_TASK, lfields, lArgs );
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
   public simpleIDs getZoneIDs( String aZONECD ) {

      String[] iIds = { "ZONE_DB_ID", "ZONE_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "ZONE_CD", aZONECD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EQP_TASK_ZONE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lIds;

   }


   /**
    * This function is to verify the Mxi tables updated correctly after import.
    *
    *
    */

   public void verifyALL( String aAssmbl_cd, String aREQQT ) {

      // Verify eqp_task_panel
      simpleIDs lZoneIds = getZoneIDs( iZONE_CD_1 );
      simpleIDs lPanelIds =
            verifyEQPTASKPANEL( iPANEL_NAME_1, iPANEL_LDESC_1, aAssmbl_cd, iPANEL_CD_1, lZoneIds );

      // Verify eqp_task_zone
      simpleIDs lNHZoneIds = getZoneIDs( iNH_ZONE_CD_1 );
      verifyEQPTASKZONE( lZoneIds, lNHZoneIds, aAssmbl_cd, iZONE_CD_1, iZONE_NAME_1,
            iZONE_LDESC_1 );

      // verify task_panel
      iTaskIDs_1 = verifyTASKPANEL( lPanelIds );

      // verify task_zone
      String lQuery = "select 1 from " + TableUtil.TASK_ZONE + " where TASK_DB_ID="
            + iTaskIDs_1.getNO_DB_ID() + " and TASK_ID=" + iTaskIDs_1.getNO_ID()
            + " and ZONE_DB_ID=" + lZoneIds.getNO_DB_ID() + " and ZONE_ID=" + lZoneIds.getNO_ID();
      Assert.assertTrue( "Check task_zone table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_work_type
      verifyTASKWORKTYPE( iTaskIDs_1, iWORK_TYPE_LIST_1, iWORK_TYPE_LIST_2 );

      // verify task_task
      iTaskDefnIDs_1 = verifyTASKTASK( iTaskIDs_1, iJIC_TASK_CLASS_CD, iTASK_PRIORITY_CD_1,
            aAssmbl_cd, iSUBCLASS_CD, iTASK_ORIGINATOR_CD, iJIC_TASK_CD_1, iJIC_TASK_NAME_1,
            iJIC_TASK_DESC_1, iINSTRUCTIONS, iTASK_APPL_LDESC, iJIC_TASK_REF_SDESC_1,
            iAPPLICABILITY_DESC, iENGINEERING_LDESC, iEXT_KEY_SDESC );

      // verify task_DEFN
      lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTaskDefnIDs_1.getNO_DB_ID() + " and TASK_DEFN_ID=" + iTaskDefnIDs_1.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_task_ietm
      // Check first ietm
      simpleIDs lIetmIds_1 = getIetmIDs( iJIC_IETM_CD_1 );
      String lIetmTopicID_1 = getIetmTopicID( lIetmIds_1, iJIC_TOPIC_SDESC_1 );

      lQuery = "select 1 from " + TableUtil.TASK_TASK_IETM + " where TASK_DB_ID="
            + iTaskIDs_1.getNO_DB_ID() + " and TASK_ID=" + iTaskIDs_1.getNO_ID()
            + " and IETM_DB_ID=" + lIetmIds_1.getNO_DB_ID() + " and IETM_ID="
            + lIetmIds_1.getNO_ID() + " and IETM_TOPIC_ID='" + lIetmTopicID_1 + "' and IETM_ORD='"
            + iJIC_IETM_ORD_1 + "'";
      Assert.assertTrue( "Check TASK_TASK_IETM table to verify the first record exists",
            RecordsExist( lQuery ) );

      // Check second ietm
      simpleIDs lIetmIds_2 = getIetmIDs( iJIC_IETM_CD_2 );
      String lIetmTopicID_2 = getIetmTopicID( lIetmIds_2, iJIC_TOPIC_SDESC_2 );

      lQuery = "select 1 from " + TableUtil.TASK_TASK_IETM + " where TASK_DB_ID="
            + iTaskIDs_1.getNO_DB_ID() + " and TASK_ID=" + iTaskIDs_1.getNO_ID()
            + " and IETM_DB_ID=" + lIetmIds_2.getNO_DB_ID() + " and IETM_ID="
            + lIetmIds_2.getNO_ID() + " and IETM_TOPIC_ID='" + lIetmTopicID_2 + "' and IETM_ORD='"
            + iJIC_IETM_ORD_2 + "'";
      Assert.assertTrue( "Check TASK_TASK_IETM table to verify the second record exists",
            RecordsExist( lQuery ) );

      // verify task_labour_list
      verifyTASKLABOURLIST( iTaskIDs_1, iLABOR_SKILL_CD, iMAN_PWR_CT, iSCHED_WORK_HRS,
            iSCHED_INSP_HRS, iSCHED_CERT_HRS );

      // verify task_part_list

      assmbJICpart lassmblInfo_1 = getJicPartFromMxi( iPART_NO_OEM_1, iMANUFACT_REF_1 );
      verifyTASKPARTLIST( iTaskIDs_1, aAssmbl_cd, lassmblInfo_1, iREQ_QT, iREQ_ACTION_CD );

      assmbJICpart lassmblInfo_2 = getJicPartFromMxi( iPART_NO_OEM_2, iMANUFACT_REF_2 );
      lQuery = "select 1 from " + TableUtil.TASK_PART_LIST + " where TASK_DB_ID="
            + iTaskIDs_1.getNO_DB_ID() + " and TASK_ID=" + iTaskIDs_1.getNO_ID()
            + " and BOM_PART_DB_ID=" + lassmblInfo_2.getBOM_PART_DB_ID() + " and BOM_PART_ID="
            + lassmblInfo_2.getBOM_PART_ID() + " and REQ_QT=" + aREQQT + " and REQ_ACTION_CD='"
            + iREQ_ACTION_CD + "'";
      Assert.assertTrue( "Check TASK_PART_LIST table to verify comhw part record exists",
            RecordsExist( lQuery ) );

      // verify task_tool_list
      simpleIDs lTool_1 = getJicToolInforFromMxi( iPN_Tool_1, iMF_Tool_1 );
      verifyTASKTOOLLIST( iTaskIDs_1, lTool_1, iREQ_HR );

      simpleIDs lTool_2 = getJicToolInforFromMxi( iPN_Tool_2, iMF_Tool_2 );
      verifyTASKTOOLLIST( iTaskIDs_1, lTool_2, iREQ_HR );

      // verify task_step
      lQuery = "select 1 from " + TableUtil.TASK_STEP + " where TASK_DB_ID="
            + iTaskIDs_1.getNO_DB_ID() + " and TASK_ID=" + iTaskIDs_1.getNO_ID() + " and STEP_ORD='"
            + iJOB_STEP_ORD_1 + "'";
      Assert.assertTrue( "Check TASK_STEP table to verify the first record exists",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.TASK_STEP + " where TASK_DB_ID="
            + iTaskIDs_1.getNO_DB_ID() + " and TASK_ID=" + iTaskIDs_1.getNO_ID() + " and STEP_ORD='"
            + iJOB_STEP_ORD_2 + "'";
      Assert.assertTrue( "Check TASK_STEP table to verify the second record exists",
            RecordsExist( lQuery ) );
   }


   /**
    * This function is to verify the Mxi tables updated correctly after import.
    *
    *
    */

   public void verifyENGALL( String aAssmbl_cd, String aREQQT ) {

      // Verify eqp_task_panel
      simpleIDs lZoneIds = getZoneIDs( iZONE_CD_2 );
      simpleIDs lPanelIds =
            verifyEQPTASKPANEL( iPANEL_NAME_1, iPANEL_LDESC_1, aAssmbl_cd, iPANEL_CD_1, lZoneIds );

      // Verify eqp_task_zone
      simpleIDs lNHZoneIds = null;
      verifyEQPTASKZONE( lZoneIds, lNHZoneIds, aAssmbl_cd, iZONE_CD_2, iZONE_NAME_2,
            iZONE_LDESC_2 );

      // verify task_panel
      iTaskIDs_1 = verifyTASKPANEL( lPanelIds );

      // verify task_zone
      String lQuery = "select 1 from " + TableUtil.TASK_ZONE + " where TASK_DB_ID="
            + iTaskIDs_1.getNO_DB_ID() + " and TASK_ID=" + iTaskIDs_1.getNO_ID()
            + " and ZONE_DB_ID=" + lZoneIds.getNO_DB_ID() + " and ZONE_ID=" + lZoneIds.getNO_ID();
      Assert.assertTrue( "Check task_zone table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_work_type
      verifyTASKWORKTYPE( iTaskIDs_1, iWORK_TYPE_LIST_1, iWORK_TYPE_LIST_2 );

      // verify task_task
      iTaskDefnIDs_1 = verifyTASKTASK( iTaskIDs_1, iJIC_TASK_CLASS_CD, iTASK_PRIORITY_CD_1,
            aAssmbl_cd, iSUBCLASS_CD, iTASK_ORIGINATOR_CD, iJIC_TASK_CD_1, iJIC_TASK_NAME_1,
            iJIC_TASK_DESC_1, iINSTRUCTIONS, iTASK_APPL_LDESC, iJIC_TASK_REF_SDESC_1,
            iAPPLICABILITY_DESC, iENGINEERING_LDESC, iEXT_KEY_SDESC );

      // verify task_DEFN
      lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTaskDefnIDs_1.getNO_DB_ID() + " and TASK_DEFN_ID=" + iTaskDefnIDs_1.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_task_ietm
      // Check first ietm
      simpleIDs lIetmIds_1 = getIetmIDs( iJIC_IETM_CD_1 );
      String lIetmTopicID_1 = getIetmTopicID( lIetmIds_1, iJIC_TOPIC_SDESC_1 );

      lQuery = "select 1 from " + TableUtil.TASK_TASK_IETM + " where TASK_DB_ID="
            + iTaskIDs_1.getNO_DB_ID() + " and TASK_ID=" + iTaskIDs_1.getNO_ID()
            + " and IETM_DB_ID=" + lIetmIds_1.getNO_DB_ID() + " and IETM_ID="
            + lIetmIds_1.getNO_ID() + " and IETM_TOPIC_ID='" + lIetmTopicID_1 + "' and IETM_ORD='"
            + iJIC_IETM_ORD_1 + "'";
      Assert.assertTrue( "Check TASK_TASK_IETM table to verify the first record exists",
            RecordsExist( lQuery ) );

      // // Check second ietm
      // simpleIDs lIetmIds_2 = getIetmIDs( iJIC_IETM_CD_2 );
      // String lIetmTopicID_2 = getIetmTopicID( lIetmIds_2, iJIC_TOPIC_SDESC_2 );
      //
      // lQuery = "select 1 from " + TableUtil.TASK_TASK_IETM + " where TASK_DB_ID="
      // + iTaskIDs_1.getNO_DB_ID() + " and TASK_ID=" + iTaskIDs_1.getNO_ID()
      // + " and IETM_DB_ID=" + lIetmIds_2.getNO_DB_ID() + " and IETM_ID="
      // + lIetmIds_2.getNO_ID() + " and IETM_TOPIC_ID='" + lIetmTopicID_2 + "' and IETM_ORD='"
      // + iJIC_IETM_ORD_2 + "'";
      // Assert.assertTrue( "Check TASK_TASK_IETM table to verify the second record exists",
      // RecordsExist( lQuery ) );

      // verify task_labour_list
      verifyTASKLABOURLIST( iTaskIDs_1, iLABOR_SKILL_CD, iMAN_PWR_CT, iSCHED_WORK_HRS,
            iSCHED_INSP_HRS, iSCHED_CERT_HRS );

      // verify task_part_list
      //
      // assmbJICpart lassmblInfo_1 = getJicPartFromMxi( iPART_NO_OEM_1, iMANUFACT_REF_1 );
      // verifyTASKPARTLIST( iTaskIDs_1, aAssmbl_cd, lassmblInfo_1, iREQ_QT, iREQ_ACTION_CD );

      assmbJICpart lassmblInfo_2 = getJicPartFromMxi( iPART_NO_OEM_2, iMANUFACT_REF_2 );
      lQuery = "select 1 from " + TableUtil.TASK_PART_LIST + " where TASK_DB_ID="
            + iTaskIDs_1.getNO_DB_ID() + " and TASK_ID=" + iTaskIDs_1.getNO_ID()
            + " and BOM_PART_DB_ID=" + lassmblInfo_2.getBOM_PART_DB_ID() + " and BOM_PART_ID="
            + lassmblInfo_2.getBOM_PART_ID() + " and REQ_QT=" + aREQQT + " and REQ_ACTION_CD='"
            + iREQ_ACTION_CD + "'";
      Assert.assertTrue( "Check TASK_PART_LIST table to verify comhw part record exists",
            RecordsExist( lQuery ) );

      // verify task_tool_list
      simpleIDs lTool_1 = getJicToolInforFromMxi( iPN_Tool_1, iMF_Tool_1 );
      verifyTASKTOOLLIST( iTaskIDs_1, lTool_1, iREQ_HR );

      simpleIDs lTool_2 = getJicToolInforFromMxi( iPN_Tool_2, iMF_Tool_2 );
      verifyTASKTOOLLIST( iTaskIDs_1, lTool_2, iREQ_HR );

      // verify task_step
      lQuery = "select 1 from " + TableUtil.TASK_STEP + " where TASK_DB_ID="
            + iTaskIDs_1.getNO_DB_ID() + " and TASK_ID=" + iTaskIDs_1.getNO_ID() + " and STEP_ORD='"
            + iJOB_STEP_ORD_1 + "'";
      Assert.assertTrue( "Check TASK_STEP table to verify the first record exists",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.TASK_STEP + " where TASK_DB_ID="
            + iTaskIDs_1.getNO_DB_ID() + " and TASK_ID=" + iTaskIDs_1.getNO_ID() + " and STEP_ORD='"
            + iJOB_STEP_ORD_2 + "'";
      Assert.assertTrue( "Check TASK_STEP table to verify the second record exists",
            RecordsExist( lQuery ) );
   }


   /**
    * This function is to verify the Mxi tables updated correctly after import.
    *
    *
    */

   public void verifyMultipleALL( String aAssmbl_cd, String aREQQT ) {

      // Verify eqp_task_panel
      simpleIDs lZoneIds = getZoneIDs( iZONE_CD_1 );
      simpleIDs lPanelIds =
            verifyEQPTASKPANEL( iPANEL_NAME_1, iPANEL_LDESC_1, aAssmbl_cd, iPANEL_CD_1, lZoneIds );

      // Verify eqp_task_zone
      simpleIDs lNHZoneIds = getZoneIDs( iNH_ZONE_CD_1 );
      verifyEQPTASKZONE( lZoneIds, lNHZoneIds, aAssmbl_cd, iZONE_CD_1, iZONE_NAME_1,
            iZONE_LDESC_1 );

      // Get iTaskIDs form task_task table
      iTaskIDs_1 = getTaskIDs( iJIC_TASK_CD_1 );
      iTaskIDs_2 = getTaskIDs( iJIC_TASK_CD_2 );

      // verify task_panel
      List<simpleIDs> iTaskIDs_1_temp = verifyTASKPANELMultiple( lPanelIds );
      Assert.assertTrue( "The task id should be same", iTaskIDs_1_temp.contains( iTaskIDs_1 ) );
      Assert.assertTrue( "The task id should be same", iTaskIDs_1_temp.contains( iTaskIDs_2 ) );

      // verify task_zone
      String lQuery = "select 1 from " + TableUtil.TASK_ZONE + " where TASK_DB_ID="
            + iTaskIDs_1.getNO_DB_ID() + " and TASK_ID=" + iTaskIDs_1.getNO_ID()
            + " and ZONE_DB_ID=" + lZoneIds.getNO_DB_ID() + " and ZONE_ID=" + lZoneIds.getNO_ID();
      Assert.assertTrue( "Check task_zone table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_work_type
      verifyTASKWORKTYPE( iTaskIDs_1, iWORK_TYPE_LIST_1, iWORK_TYPE_LIST_2 );
      verifyTASKWORKTYPE( iTaskIDs_2, iWORK_TYPE_LIST_1, iWORK_TYPE_LIST_2 );

      // verify task_task
      iTaskDefnIDs_1 = verifyTASKTASK( iTaskIDs_1, iJIC_TASK_CLASS_CD, iTASK_PRIORITY_CD_1,
            aAssmbl_cd, iSUBCLASS_CD, iTASK_ORIGINATOR_CD, iJIC_TASK_CD_1, iJIC_TASK_NAME_1,
            iJIC_TASK_DESC_1, iINSTRUCTIONS, iTASK_APPL_LDESC, iJIC_TASK_REF_SDESC_1,
            iAPPLICABILITY_DESC, iENGINEERING_LDESC, iEXT_KEY_SDESC );

      iTaskDefnIDs_2 = verifyTASKTASK( iTaskIDs_2, iJIC_TASK_CLASS_CD, iTASK_PRIORITY_CD_1,
            aAssmbl_cd, iSUBCLASS_CD, iTASK_ORIGINATOR_CD, iJIC_TASK_CD_2, iJIC_TASK_NAME_2,
            iJIC_TASK_DESC_2, iINSTRUCTIONS, iTASK_APPL_LDESC_2, iJIC_TASK_REF_SDESC_2,
            iAPPLICABILITY_DESC, iENGINEERING_LDESC_2, iEXT_KEY_SDESC );

      // verify task_DEFN
      lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTaskDefnIDs_1.getNO_DB_ID() + " and TASK_DEFN_ID=" + iTaskDefnIDs_1.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the firsr record exists",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTaskDefnIDs_2.getNO_DB_ID() + " and TASK_DEFN_ID=" + iTaskDefnIDs_2.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the second record exists",
            RecordsExist( lQuery ) );

      // verify task_task_ietm
      // Check first ietm
      simpleIDs lIetmIds_1 = getIetmIDs( iJIC_IETM_CD_1 );
      String lIetmTopicID_1 = getIetmTopicID( lIetmIds_1, iJIC_TOPIC_SDESC_1 );

      lQuery = "select 1 from " + TableUtil.TASK_TASK_IETM + " where TASK_DB_ID="
            + iTaskIDs_1.getNO_DB_ID() + " and TASK_ID=" + iTaskIDs_1.getNO_ID()
            + " and IETM_DB_ID=" + lIetmIds_1.getNO_DB_ID() + " and IETM_ID="
            + lIetmIds_1.getNO_ID() + " and IETM_TOPIC_ID='" + lIetmTopicID_1 + "' and IETM_ORD='"
            + iJIC_IETM_ORD_1 + "'";
      Assert.assertTrue( "Check TASK_TASK_IETM table to verify the first record exists",
            RecordsExist( lQuery ) );

      // Check second ietm
      simpleIDs lIetmIds_2 = getIetmIDs( iJIC_IETM_CD_2 );
      String lIetmTopicID_2 = getIetmTopicID( lIetmIds_2, iJIC_TOPIC_SDESC_2 );

      lQuery = "select 1 from " + TableUtil.TASK_TASK_IETM + " where TASK_DB_ID="
            + iTaskIDs_1.getNO_DB_ID() + " and TASK_ID=" + iTaskIDs_1.getNO_ID()
            + " and IETM_DB_ID=" + lIetmIds_2.getNO_DB_ID() + " and IETM_ID="
            + lIetmIds_2.getNO_ID() + " and IETM_TOPIC_ID='" + lIetmTopicID_2 + "' and IETM_ORD='"
            + iJIC_IETM_ORD_2 + "'";
      Assert.assertTrue( "Check TASK_TASK_IETM table to verify the second record exists",
            RecordsExist( lQuery ) );

      // verify task_labour_list
      verifyTASKLABOURLIST( iTaskIDs_1, iLABOR_SKILL_CD, iMAN_PWR_CT, iSCHED_WORK_HRS,
            iSCHED_INSP_HRS, iSCHED_CERT_HRS );

      // verify task_part_list

      assmbJICpart lassmblInfo_1 = getJicPartFromMxi( iPART_NO_OEM_1, iMANUFACT_REF_1 );
      verifyTASKPARTLIST( iTaskIDs_1, aAssmbl_cd, lassmblInfo_1, iREQ_QT, iREQ_ACTION_CD );

      assmbJICpart lassmblInfo_2 = getJicPartFromMxi( iPART_NO_OEM_2, iMANUFACT_REF_2 );
      lQuery = "select 1 from " + TableUtil.TASK_PART_LIST + " where TASK_DB_ID="
            + iTaskIDs_1.getNO_DB_ID() + " and TASK_ID=" + iTaskIDs_1.getNO_ID()
            + " and BOM_PART_DB_ID=" + lassmblInfo_2.getBOM_PART_DB_ID() + " and BOM_PART_ID="
            + lassmblInfo_2.getBOM_PART_ID() + " and REQ_QT=" + aREQQT + " and REQ_ACTION_CD='"
            + iREQ_ACTION_CD + "'";
      Assert.assertTrue( "Check TASK_PART_LIST table to verify comhw part record exists",
            RecordsExist( lQuery ) );

      // verify task_tool_list
      simpleIDs lTool_1 = getJicToolInforFromMxi( iPN_Tool_1, iMF_Tool_1 );
      verifyTASKTOOLLIST( iTaskIDs_1, lTool_1, iREQ_HR );

      simpleIDs lTool_2 = getJicToolInforFromMxi( iPN_Tool_2, iMF_Tool_2 );
      verifyTASKTOOLLIST( iTaskIDs_1, lTool_2, iREQ_HR );

      // verify task_step
      lQuery = "select 1 from " + TableUtil.TASK_STEP + " where TASK_DB_ID="
            + iTaskIDs_1.getNO_DB_ID() + " and TASK_ID=" + iTaskIDs_1.getNO_ID() + " and STEP_ORD='"
            + iJOB_STEP_ORD_1 + "'";
      Assert.assertTrue( "Check TASK_STEP table to verify the first record exists",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.TASK_STEP + " where TASK_DB_ID="
            + iTaskIDs_1.getNO_DB_ID() + " and TASK_ID=" + iTaskIDs_1.getNO_ID() + " and STEP_ORD='"
            + iJOB_STEP_ORD_2 + "'";
      Assert.assertTrue( "Check TASK_STEP table to verify the second record exists",
            RecordsExist( lQuery ) );
   }


   /**
    * This function is to verify the Mxi tables updated correctly after import for OPER-1128.
    *
    *
    */

   public void verifyOPER_1128( String aAssmbl_cd, String aREQQT, String aCERT_BOOL,
         String aINSP_BOOL ) {

      // Verify eqp_task_panel
      simpleIDs lZoneIds = getZoneIDs( iZONE_CD_1 );
      simpleIDs lPanelIds =
            verifyEQPTASKPANEL( iPANEL_NAME_1, iPANEL_LDESC_1, aAssmbl_cd, iPANEL_CD_1, lZoneIds );

      // Verify eqp_task_zone
      simpleIDs lNHZoneIds = getZoneIDs( iNH_ZONE_CD_1 );
      verifyEQPTASKZONE( lZoneIds, lNHZoneIds, aAssmbl_cd, iZONE_CD_1, iZONE_NAME_1,
            iZONE_LDESC_1 );

      // verify task_panel
      iTaskIDs_1 = verifyTASKPANEL( lPanelIds );

      // verify task_zone
      String lQuery = "select 1 from " + TableUtil.TASK_ZONE + " where TASK_DB_ID="
            + iTaskIDs_1.getNO_DB_ID() + " and TASK_ID=" + iTaskIDs_1.getNO_ID()
            + " and ZONE_DB_ID=" + lZoneIds.getNO_DB_ID() + " and ZONE_ID=" + lZoneIds.getNO_ID();
      Assert.assertTrue( "Check task_zone table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_work_type
      verifyTASKWORKTYPE( iTaskIDs_1, iWORK_TYPE_LIST_1, iWORK_TYPE_LIST_2 );

      // verify task_task
      iTaskDefnIDs_1 = verifyTASKTASK( iTaskIDs_1, iJIC_TASK_CLASS_CD, iTASK_PRIORITY_CD_1,
            aAssmbl_cd, iSUBCLASS_CD, iTASK_ORIGINATOR_CD, iJIC_TASK_CD_1, iJIC_TASK_NAME_1,
            iJIC_TASK_DESC_1, iINSTRUCTIONS, iTASK_APPL_LDESC, iJIC_TASK_REF_SDESC_1,
            iAPPLICABILITY_DESC, iENGINEERING_LDESC, iEXT_KEY_SDESC );

      // verify task_DEFN
      lQuery = "select 1 from " + TableUtil.TASK_DEFN + " where TASK_DEFN_DB_ID="
            + iTaskDefnIDs_1.getNO_DB_ID() + " and TASK_DEFN_ID=" + iTaskDefnIDs_1.getNO_ID();
      Assert.assertTrue( "Check task_defn table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify task_labour_list
      verifyTASKLABOURLIST_2( iTaskIDs_1, iLABOR_SKILL_CD, iMAN_PWR_CT, iSCHED_WORK_HRS, "0", "0",
            aCERT_BOOL, aINSP_BOOL );

   }


   /**
    * This function is called by after test method to restore data which has been modified by tests.
    *
    *
    */

   public void RestoreData() {

      String lStrDelete = null;

      if ( iTaskIDs_1 != null ) {

         // delete task_step
         lStrDelete = "delete from " + TableUtil.TASK_STEP + "  where TASK_DB_ID="
               + iTaskIDs_1.getNO_DB_ID() + " and TASK_ID=" + iTaskIDs_1.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_tool_loist
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

         // delete task_task_ietm
         lStrDelete = "delete from " + TableUtil.TASK_TASK_IETM + "  where TASK_DB_ID="
               + iTaskIDs_1.getNO_DB_ID() + " and TASK_ID=" + iTaskIDs_1.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_panel
         lStrDelete = "delete from " + TableUtil.TASK_PANEL + "  where TASK_DB_ID="
               + iTaskIDs_1.getNO_DB_ID() + " and TASK_ID=" + iTaskIDs_1.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_zone
         lStrDelete = "delete from " + TableUtil.TASK_ZONE + "  where TASK_DB_ID="
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

      if ( iTaskIDs_2 != null ) {

         // delete task_step
         lStrDelete = "delete from " + TableUtil.TASK_STEP + "  where TASK_DB_ID="
               + iTaskIDs_2.getNO_DB_ID() + " and TASK_ID=" + iTaskIDs_2.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_tool_loist
         lStrDelete = "delete from " + TableUtil.TASK_TOOL_LIST + "  where TASK_DB_ID="
               + iTaskIDs_2.getNO_DB_ID() + " and TASK_ID=" + iTaskIDs_2.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_part_list
         lStrDelete = "delete from " + TableUtil.TASK_PART_LIST + "  where TASK_DB_ID="
               + iTaskIDs_2.getNO_DB_ID() + " and TASK_ID=" + iTaskIDs_2.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_labour_list
         lStrDelete = "delete from " + TableUtil.TASK_LABOUR_LIST + "  where TASK_DB_ID="
               + iTaskIDs_2.getNO_DB_ID() + " and TASK_ID=" + iTaskIDs_2.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_task_ietm
         lStrDelete = "delete from " + TableUtil.TASK_TASK_IETM + "  where TASK_DB_ID="
               + iTaskIDs_2.getNO_DB_ID() + " and TASK_ID=" + iTaskIDs_2.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_panel
         lStrDelete = "delete from " + TableUtil.TASK_PANEL + "  where TASK_DB_ID="
               + iTaskIDs_2.getNO_DB_ID() + " and TASK_ID=" + iTaskIDs_2.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_zone
         lStrDelete = "delete from " + TableUtil.TASK_ZONE + "  where TASK_DB_ID="
               + iTaskIDs_2.getNO_DB_ID() + " and TASK_ID=" + iTaskIDs_2.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_work_type
         lStrDelete = "delete from " + TableUtil.TASK_WORK_TYPE + "  where TASK_DB_ID="
               + iTaskIDs_2.getNO_DB_ID() + " and TASK_ID=" + iTaskIDs_2.getNO_ID();
         executeSQL( lStrDelete );

         // delete task_task
         lStrDelete = "delete from " + TableUtil.TASK_TASK + "  where TASK_DB_ID="
               + iTaskIDs_2.getNO_DB_ID() + " and TASK_ID=" + iTaskIDs_2.getNO_ID();
         executeSQL( lStrDelete );

      }

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

      lStrDelete =
            "delete from " + TableUtil.EQP_TASK_PANEL + " where DESC_SDESC='" + iPANEL_NAME_1 + "'";
      executeSQL( lStrDelete );
      lStrDelete =
            "delete from " + TableUtil.EQP_TASK_ZONE + " where DESC_SDESC='" + iZONE_NAME_1 + "'";
      executeSQL( lStrDelete );
      lStrDelete =
            "delete from " + TableUtil.EQP_TASK_ZONE + " where DESC_SDESC='" + iZONE_NAME_2 + "'";
      executeSQL( lStrDelete );

   }


   /**
    * Calls check JIC error code
    *
    *
    */
   @Override
   protected void checkErrorCode( String aTestName, String aValidationCode ) {

      boolean lFound = false;
      String lerror_desc = null;
      String lerrorMsg = null;

      String lquery = TestConstants.C_JIC_ERROR_CHECK;

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
                     .prepareCall( "BEGIN  jic_import.validate_jic(on_retcode =>?); END;" );

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
                     .prepareCall( "BEGIN jic_import.import_jic(on_retcode =>?); END;" );

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


   // This is to run export JIC functionality

   public int runExport() {
      int lReturn = 0;
      CallableStatement lPrepareCallKIT;

      try {

         lPrepareCallKIT =
               getConnection().prepareCall( "BEGIN jic_import.export_jic(on_retcode =>?); END;" );

         lPrepareCallKIT.registerOutParameter( 1, Types.INTEGER );

         lPrepareCallKIT.execute();
         commit();
         lReturn = lPrepareCallKIT.getInt( 1 );
      } catch ( SQLException e ) {
         e.printStackTrace();
      }
      return lReturn;
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
