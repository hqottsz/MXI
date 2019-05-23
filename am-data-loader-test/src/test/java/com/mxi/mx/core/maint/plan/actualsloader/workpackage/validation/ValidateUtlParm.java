package com.mxi.mx.core.maint.plan.actualsloader.workpackage.validation;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.WorkPKGTest;


/**
 * This test suite contains test cases on validation functionality of AL_WORK_PACKAGE_PKG package of
 * error code on UTL CONFIG PARM table
 *
 * @author ALICIA QIAN
 */
public class ValidateUtlParm extends WorkPKGTest {

   @Rule
   public TestName testName = new TestName();


   /**
    * Setup before executing each individual test
    *
    */
   @Before
   @Override
   public void before() throws Exception {

      super.before();
      clearActualsLoaderTables();
      DataSetup();

      String strTCName = testName.getMethodName();
      if ( strTCName.contains( "00001" ) ) {
         dataSetup00001();
      } else if ( strTCName.contains( "00002" ) ) {
         dataSetup00002();
      } else if ( strTCName.contains( "00003" ) ) {
         dataSetup00003();
      } else if ( strTCName.contains( "00004" ) ) {
         dataSetup00004();
      } else if ( strTCName.contains( "00005" ) ) {
         dataSetup00005();
      } else if ( strTCName.contains( "00006" ) ) {
         dataSetup00006();
      }

   }


   /**
    * Clean up after each individual test
    */
   @After
   @Override
   public void after() {

      try {
         RestoreData();
         RestoreLocal();
         super.after();
      } catch ( Exception e ) {
         e.printStackTrace();
      }
   }


   /**
    * This test is to verify error code ALWPK-00001: UTL_CONFIG_PARM is missing a PARM_NAME =
    * BLANK_CHECK_SIGNATURE.
    *
    *
    */
   @Test
   public void test_ALWPK_00001() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      prepareData();

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-00001" );

   }


   /**
    * This test is to verify error code ALWPK-00002: UTL_CONFIG_PARM.parm_value of
    * BLANK_CHECK_SIGNATURE cannot be null or consist entirely of spaces.
    *
    *
    */
   @Test
   public void test_ALWPK_00002() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      prepareData();

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-00002" );

   }


   /**
    * This test is to verify error code ALWPK-00003: UTL_CONFIG_PARM.parm_value of
    * BLANK_CHECK_SIGNATURE does not exists in maintenix table REF_LABOUR_SKILL.
    *
    *
    */
   @Test
   public void test_ALWPK_00003() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      prepareData();

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-00003" );

   }


   /**
    * This test is to verify error code ALWPK-00004: UTL_CONFIG_PARM.parm_value of
    * BLANK_CHECK_SIGNATURE does not exists in maintenix table REF_LABOUR_SKILL.
    *
    *
    */
   @Test
   public void test_ALWPK_00004() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      prepareData();

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-00004" );

   }


   /**
    * This test is to verify error code ALWPK-00005: UTL_CONFIG_PARM.parm_value of
    * BLANK_RO_SIGNATURE cannot be null or consist entirely of spaces.
    *
    *
    */
   @Test
   public void test_ALWPK_00005() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      prepareData();

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-00005" );
   }


   /**
    * This test is to verify error code ALWPK-00006:UTL_CONFIG_PARM.parm_value of BLANK_RO_SIGNATURE
    * does not exists in maintenix table REF_LABOUR_SKILL.
    *
    *
    */
   @Test
   public void test_ALWPK_00006() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      prepareData();

      // run validation
      Assert.assertTrue( "Validation Passed when expected an error ",
            runValidationAndImport( true, true ) == -2 );

      // Validate error
      validateErrorCode( "ALWPK-00006" );

   }


   // ================================================================================================

   /**
    * This function is prepare data for all the test cases in this class
    *
    *
    */
   public void prepareData() {
      iEVENT_1 = null;

      // c_work_pckkage table
      Map<String, String> lWPK = new LinkedHashMap<>();

      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "WKP_DESC", "\'" + iWKP_DESC_1 + "\'" );
      lWPK.put( "SUB_TYPE_CD", "\'" + iSUB_TYPE_CD_1 + "\'" );
      lWPK.put( "HEAVY_MAINTENANCE_BOOL", "\'" + iHEAVY_MAINTENANCE_BOOL + "\'" );
      lWPK.put( "COLLECTION_REQUIRED_BOOL", "\'" + iCOLLECTION_REQUIRED_BOOL + "\'" );
      lWPK.put( "ISSUE_ACCOUNT_CD", "\'" + iISSUE_ACCOUNT_CD + "\'" );
      lWPK.put( "SCHED_LOC_CD", "\'" + iSCHED_LOC_CD_1 + "\'" );
      lWPK.put( "WORK_TYPE_LIST", "\'" + iWORK_TYPE_LIST_1 + "," + iWORK_TYPE_LIST_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE, lWPK ) );

      setCurrentDate( iWKP_NAME_1 );

      // AL_WORK_PACKAGE_TASK
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "SERIAL_NO_OEM", "\'" + iSERIAL_NO_OEM_TASk_1 + "\'" );
      lWPK.put( "PART_NO_OEM", "\'" + iPART_NO_OEM_TASK_1 + "\'" );
      lWPK.put( "MANUFACT_CD", "\'" + iMANUFACT_CD_1 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_1 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK, lWPK ) );

      // AL_WORK_PACKAGE_TASK_ATA
      lWPK.clear();
      lWPK.put( "WKP_SERIAL_NO_OEM", "\'" + iWKP_SERIAL_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_PART_NO_OEM", "\'" + iWKP_PART_NO_OEM_1 + "\'" );
      lWPK.put( "WKP_MANUFACT_CD", "\'" + iWKP_MANUFACT_CD_1 + "\'" );
      lWPK.put( "WKP_NAME", "\'" + iWKP_NAME_1 + "\'" );
      lWPK.put( "TASK_CD", "\'" + iTASK_CD_ata_1 + "\'" );
      lWPK.put( "ATA_SYS_CD", "\'" + iata_sys_cd_1 + "\'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.AL_WORK_PACKAGE_TASK_ATA, lWPK ) );

   }


   /**
    * This function is data setup for test test_ALWPK_00001
    *
    *
    */
   public void dataSetup00001() {
      String lquery =
            "update utl_config_parm set parm_name='BLANK_CHECK_SIGNATURE_AT' where parm_name='BLANK_CHECK_SIGNATURE'";
      executeSQL( lquery );
   }


   /**
    * This function is data setup for test test_ALWPK_00002
    *
    *
    */
   public void dataSetup00002() {
      String lquery =
            "update utl_config_parm set parm_value=null where parm_name='BLANK_CHECK_SIGNATURE'";
      executeSQL( lquery );
   }


   /**
    * This function is data setup for test test_ALWPK_00003
    *
    *
    */
   public void dataSetup00003() {
      String lquery =
            "update utl_config_parm set parm_value='INVALID' where parm_name='BLANK_CHECK_SIGNATURE'";
      executeSQL( lquery );
   }


   /**
    * This function is data setup for test test_ALWPK_00004
    *
    *
    */
   public void dataSetup00004() {
      String lquery =
            "update utl_config_parm set parm_name='BLANK_RO_SIGNATURE_AT' where parm_name='BLANK_RO_SIGNATURE'";
      executeSQL( lquery );
   }


   /**
    * This function is data setup for test test_ALWPK_00005
    *
    *
    */
   public void dataSetup00005() {
      String lquery =
            "update utl_config_parm set parm_value='  ' where parm_name='BLANK_RO_SIGNATURE'";
      executeSQL( lquery );
   }


   /**
    * This function is data setup for test test_ALWPK_00006
    *
    *
    */
   public void dataSetup00006() {
      String lquery =
            "update utl_config_parm set parm_value='INVALID' where parm_name='BLANK_RO_SIGNATURE'";
      executeSQL( lquery );
   }


   /**
    * This function is called by after test method to restore data which has been modified by tests.
    *
    *
    */
   public void RestoreLocal() {
      String lquery =
            "update utl_config_parm set parm_name='BLANK_CHECK_SIGNATURE' where parm_name='BLANK_CHECK_SIGNATURE_AT'";
      executeSQL( lquery );

      lquery =
            "update utl_config_parm set parm_value='AET,INSP' where parm_name='BLANK_CHECK_SIGNATURE'";
      executeSQL( lquery );

      lquery =
            "update utl_config_parm set parm_name='BLANK_RO_SIGNATURE' where parm_name='BLANK_RO_SIGNATURE_AT'";
      executeSQL( lquery );

      lquery =
            "update utl_config_parm set parm_value='AET,INSP' where parm_name='BLANK_RO_SIGNATURE'";
      executeSQL( lquery );

   }

}
