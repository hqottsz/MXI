package com.mxi.mx.core.maint.plan.baselineloader.ComHW;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;
import com.mxi.mx.util.TableUtil;


/**
 * This test suite contains test cases on validation and import functionality of comhw_import
 * package.
 *
 * @author ALICIA QIAN
 */
public class ComHWImport extends ComHW {

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
      clearBaselineLoaderTables();

   }


   /**
    * Clean up after each individual test
    */
   @After
   @Override
   public void after() {
      RestoreData();
      super.after();
   }


   /**
    * This test is to verify comhw_import validation functionality of staging table c_comhw_def ,
    * inv_class_cd = BATCH
    *
    */

   public void test_BATCH_VALIDATION() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      prepareData( iINV_CLASS_CD_BATCH );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify comhw_import import functionality of staging table c_comhw_def ,
    * inv_class_cd = BATCH
    *
    */
   @Test
   public void test_BATCH_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      test_BATCH_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify EQP_BOM_PART table
      simpleIDs lBomPartIds =
            verifyEQPBOMPART( iPART_GROUP_CD_1, iPART_GROUP_NAME_1, "COMHW", iINV_CLASS_CD_BATCH );

      // Verify eqp_part_no
      simpleIDs lPartNoIds_1 =
            verifyEQPPARTNO( iPART_NO_SDESC, iPART_NO_OEM_1, iINV_CLASS_CD_BATCH, iMANUFACT_CD_1 );
      simpleIDs lPartNoIds_2 =
            verifyEQPPARTNO( iPART_NO_SDESC, iPART_NO_OEM_2, iINV_CLASS_CD_BATCH, iMANUFACT_CD_2 );

      // verify eqp_part_baseline
      verifyEQPPARTBASELINE( lBomPartIds, lPartNoIds_1, iPART_BASELINE_CD, "1", "1",
            iAPPL_EFF_LDESC );
      verifyEQPPARTBASELINE( lBomPartIds, lPartNoIds_2, iPART_BASELINE_CD, "0", "1",
            iAPPL_EFF_LDESC );

   }


   /**
    * This test is to verify comhw_import validation functionality of staging table c_comhw_def ,
    * inv_class_cd = SER
    *
    */

   public void test_SER_VALIDATION() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      prepareData( iINV_CLASS_CD_SER );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify comhw_import import functionality of staging table c_comhw_def ,
    * inv_class_cd = SER
    *
    */
   @Test
   public void test_SER_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      test_SER_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify EQP_BOM_PART table
      simpleIDs lBomPartIds =
            verifyEQPBOMPART( iPART_GROUP_CD_1, iPART_GROUP_NAME_1, "COMHW", iINV_CLASS_CD_SER );

      // Verify eqp_part_no
      simpleIDs lPartNoIds_1 =
            verifyEQPPARTNO( iPART_NO_SDESC, iPART_NO_OEM_1, iINV_CLASS_CD_SER, iMANUFACT_CD_1 );
      simpleIDs lPartNoIds_2 =
            verifyEQPPARTNO( iPART_NO_SDESC, iPART_NO_OEM_2, iINV_CLASS_CD_SER, iMANUFACT_CD_2 );

      // verify eqp_part_baseline
      verifyEQPPARTBASELINE( lBomPartIds, lPartNoIds_1, iPART_BASELINE_CD, "1", "1",
            iAPPL_EFF_LDESC );
      verifyEQPPARTBASELINE( lBomPartIds, lPartNoIds_2, iPART_BASELINE_CD, "0", "1",
            iAPPL_EFF_LDESC );

   }


   /**
    * This test is to verify comhw_import validation functionality of staging table c_comhw_def ,
    * inv_class_cd = KIT
    *
    */

   public void test_KIT_VALIDATION() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      prepareData( iINV_CLASS_CD_KIT );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify comhw_import import functionality of staging table c_comhw_def ,
    * inv_class_cd = KIT
    *
    */
   @Test
   public void test_KIT_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      test_KIT_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify EQP_BOM_PART table
      simpleIDs lBomPartIds =
            verifyEQPBOMPART( iPART_GROUP_CD_1, iPART_GROUP_NAME_1, "COMHW", iINV_CLASS_CD_KIT );

      // Verify eqp_part_no
      simpleIDs lPartNoIds_1 =
            verifyEQPPARTNO( iPART_NO_SDESC, iPART_NO_OEM_1, iINV_CLASS_CD_KIT, iMANUFACT_CD_1 );
      simpleIDs lPartNoIds_2 =
            verifyEQPPARTNO( iPART_NO_SDESC, iPART_NO_OEM_2, iINV_CLASS_CD_KIT, iMANUFACT_CD_2 );

      // verify eqp_part_baseline
      verifyEQPPARTBASELINE( lBomPartIds, lPartNoIds_1, iPART_BASELINE_CD, "1", "1",
            iAPPL_EFF_LDESC );
      verifyEQPPARTBASELINE( lBomPartIds, lPartNoIds_2, iPART_BASELINE_CD, "0", "1",
            iAPPL_EFF_LDESC );

   }


   /**
    * This test is to verify comhw_import validation functionality of staging table c_comhw_def ,
    * inv_class_cd = BATCH, part2 is in group1 and group2
    *
    */

   public void test_PartsInDifferentGroup_VALIDATION() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      prepareData( iINV_CLASS_CD_BATCH );
      prepareData_2( iINV_CLASS_CD_BATCH );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify comhw_import validation functionality of staging table c_comhw_def ,
    * inv_class_cd = BATCH, part2 is in group1 and group2
    *
    */
   @Test
   public void test_PartsInDifferentGroup_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      test_PartsInDifferentGroup_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify Batch
      // Verify EQP_BOM_PART table
      simpleIDs lBomPartIds =
            verifyEQPBOMPART( iPART_GROUP_CD_1, iPART_GROUP_NAME_1, "COMHW", iINV_CLASS_CD_BATCH );

      // Verify eqp_part_no
      simpleIDs lPartNoIds_1 =
            verifyEQPPARTNO( iPART_NO_SDESC, iPART_NO_OEM_1, iINV_CLASS_CD_BATCH, iMANUFACT_CD_1 );
      simpleIDs lPartNoIds_2 =
            verifyEQPPARTNO( iPART_NO_SDESC, iPART_NO_OEM_2, iINV_CLASS_CD_BATCH, iMANUFACT_CD_2 );

      // verify eqp_part_baseline
      verifyEQPPARTBASELINE( lBomPartIds, lPartNoIds_1, iPART_BASELINE_CD, "1", "1",
            iAPPL_EFF_LDESC );
      verifyEQPPARTBASELINE( lBomPartIds, lPartNoIds_2, iPART_BASELINE_CD, "0", "1",
            iAPPL_EFF_LDESC );

      // verify SER
      // Verify EQP_BOM_PART table
      lBomPartIds =
            verifyEQPBOMPART( iPART_GROUP_CD_2, iPART_GROUP_NAME_2, "COMHW", iINV_CLASS_CD_BATCH );

      // verify eqp_part_baseline
      verifyEQPPARTBASELINE( lBomPartIds, lPartNoIds_2, iPART_BASELINE_CD, "1", "1",
            iAPPL_EFF_LDESC );

   }


   /**
    * This test is to verify comhw_import validation functionality of staging table c_comhw_def ,
    * inv_class_cd = BATCH, primary part is existing part in MXI.
    *
    */

   public void test_Exist_PARIMARY_VALIDATION() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      prepareData_3( iINV_CLASS_CD_BATCH );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify comhw_import import functionality of staging table c_comhw_def ,
    * inv_class_cd = BATCH, primary part is existing part in MXI.
    *
    *
    */
   @Test
   public void test_Exist_PARIMARY_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      test_Exist_PARIMARY_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify EQP_BOM_PART table
      simpleIDs lBomPartIds =
            verifyEQPBOMPART( iPART_GROUP_CD_1, iPART_GROUP_NAME_1, "COMHW", iINV_CLASS_CD_BATCH );

      // Verify eqp_part_no
      // simpleIDs lPartNoIds_1 =
      // verifyEQPPARTNO( iPART_NO_SDESC, iPART_NO_OEM_1, iINV_CLASS_CD_BATCH, iMANUFACT_CD_1 );
      simpleIDs lPartNoIds_1 =
            verifyEQPPARTNO( "Common Hardware Part 1", "CHW000001", iINV_CLASS_CD_BATCH, "11111" );

      simpleIDs lPartNoIds_2 =
            verifyEQPPARTNO( iPART_NO_SDESC, iPART_NO_OEM_2, iINV_CLASS_CD_BATCH, iMANUFACT_CD_2 );

      // verify eqp_part_baseline
      verifyEQPPARTBASELINE( lBomPartIds, lPartNoIds_1, iPART_BASELINE_CD, "1", "1",
            iAPPL_EFF_LDESC );
      verifyEQPPARTBASELINE( lBomPartIds, lPartNoIds_2, iPART_BASELINE_CD, "0", "1",
            iAPPL_EFF_LDESC );

   }


   /**
    * This test is to verify comhw_import validation functionality of staging table c_comhw_def ,
    * inv_class_cd = BATCH,non-primary part is existing part in MXI.
    *
    */

   public void test_Exist_NON_PARIMARY_VALIDATION() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      prepareData_4( iINV_CLASS_CD_BATCH );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify comhw_import import functionality of staging table c_comhw_def ,
    * inv_class_cd = BATCH,non-primary part is existing part in MXI.
    *
    *
    */
   @Test
   public void test_Exist_NON_PARIMARY_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      test_Exist_NON_PARIMARY_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify EQP_BOM_PART table
      simpleIDs lBomPartIds =
            verifyEQPBOMPART( iPART_GROUP_CD_1, iPART_GROUP_NAME_1, "COMHW", iINV_CLASS_CD_BATCH );

      // Verify eqp_part_no
      simpleIDs lPartNoIds_1 =
            verifyEQPPARTNO( iPART_NO_SDESC, iPART_NO_OEM_1, iINV_CLASS_CD_BATCH, iMANUFACT_CD_1 );

      simpleIDs lPartNoIds_2 =
            verifyEQPPARTNO( "Common Hardware Part 1", "CHW000001", iINV_CLASS_CD_BATCH, "11111" );

      // simpleIDs lPartNoIds_2 =
      // verifyEQPPARTNO( iPART_NO_SDESC, iPART_NO_OEM_2, iINV_CLASS_CD_BATCH, iMANUFACT_CD_2 );

      // verify eqp_part_baseline
      verifyEQPPARTBASELINE( lBomPartIds, lPartNoIds_1, iPART_BASELINE_CD, "1", "1",
            iAPPL_EFF_LDESC );
      verifyEQPPARTBASELINE( lBomPartIds, lPartNoIds_2, iPART_BASELINE_CD, "0", "1",
            iAPPL_EFF_LDESC );

   }


   // =======================================================================================
   /**
    * This function is called by after test method to restore data which has been modified by tests.
    *
    *
    */
   public void RestoreData() {

      // first sensitivity
      String lStrDelete = "delete from " + TableUtil.EQP_PART_BASELINE + " where PART_BASELINE_CD='"
            + iPART_BASELINE_CD + "'";
      executeSQL( lStrDelete );

      lStrDelete = "delete from " + TableUtil.EQP_BOM_PART
            + " where ASSMBL_CD='COMHW' and BOM_PART_CD like '%ATCHWGP%' and BOM_PART_NAME like '%ATCHWGP%'";
      executeSQL( lStrDelete );

      lStrDelete = "delete from " + TableUtil.EQP_PART_NO + " where PART_NO_SDESC='"
            + iPART_NO_SDESC + "' and PART_NO_OEM like '%AUTOCHP%'";
      executeSQL( lStrDelete );

   }

}
