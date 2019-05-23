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
import com.mxi.mx.util.ValidationAndImport;
import com.mxi.mx.util.WhereClause;


/**
 * This test suite contains test cases on validation and import functionality of bl_calc_import PKG.
 *
 * @author ALICIA QIAN
 */
public class CalculatedParameters extends BaselineLoaderTest {

   @Rule
   public TestName testName = new TestName();
   ValidationAndImport ivalidationandimport;

   public String iASSMBL_CD = "APU_CD1";
   public String iASSMBL_CD_ACFT = "ACFT_CD1";
   public String iCALC_PARAMETER_CD = "AD_CYC";
   public String iBASIC_EQUATION = "AUTO_USAGE_CYC.CYC_C";
   public String iCALCULATION_DESCP = "AUTO_USAGE_CYC.CYC_C";

   public String iSYMBOL_1 = "ARG1";
   public String iSYMBOL_factor = "factor";
   public String iSYMBOL_AT_CYC_M = "AT_CYC_M";
   public String iSYMBOL_AT_CYC = "AT_CYC";
   public String iSYMBOL_AT_CYC_AT_CYC_M = "AT_CYC_AT_CYC_M";

   public String iCONSTANT_VALUE_1 = null;
   public String iINPUT_ORDER_1 = "1";
   public String iINPUT_ORDER_2 = "2";
   public String iINPUT_ORDER_3 = "3";
   public String iINPUT_ORDER_4 = "4";

   // public String iSYMBOL_PART_1 = "HOUR";
   public String iSYMBOL_PART_1 = "ARG2";
   public String iPART_GROUP_CD = "APU_CD1";
   public String iPART_NO_OEM = "APU_ASSY_PN1";
   public String iMANFACT_CD = "1234567890";
   public String iCONSTANT_VALUE_PART_1 = "10";

   public String iPART_GROUP_CD_ACFT = "ACFT_CD1";
   public String iPART_NO_OEM_ACFT = "ACFT_ASSY_PN1";
   public String iMANFACT_CD_ACFT = "10001";

   public simpleIDs iCalc_IDs = null;
   public simpleIDs iCalc_IDs_2 = null;


   /**
    * Setup before executing each individual test
    *
    */
   @Before
   @Override
   public void before() throws Exception {

      super.before();
      clearBaselineLoaderTables();
      iCalc_IDs = null;
      iCalc_IDs_2 = null;

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
    *
    * BLCALC-00080: BL_CALC.ASSMBL_CD/CALC_PARAMETER_CD is specified multiple times. assmbl_cd is
    * upper case and lower case.
    */

   @Test
   public void testOPER_24042_BLCALC00080_1_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lblCal = new LinkedHashMap<>();

      // bl_calc table
      lblCal.put( "ASSMBL_CD", "'" + iASSMBL_CD + "'" );
      lblCal.put( "CALC_PARAMETER_CD", "'" + iCALC_PARAMETER_CD + "'" );
      lblCal.put( "BASIC_EQUATION", "'" + iBASIC_EQUATION + "'" );
      lblCal.put( "CALCULATION_DESCRIPTION", "'" + iCALCULATION_DESCP + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_CALC, lblCal ) );

      // bl_calc table
      lblCal.clear();
      lblCal.put( "ASSMBL_CD", "'apu_cd1'" );
      lblCal.put( "CALC_PARAMETER_CD", "'" + iCALC_PARAMETER_CD + "'" );
      lblCal.put( "BASIC_EQUATION", "'" + iBASIC_EQUATION + "'" );
      lblCal.put( "CALCULATION_DESCRIPTION", "'" + iCALCULATION_DESCP + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_CALC, lblCal ) );

      // bl_calc_input table
      lblCal.clear();
      lblCal.put( "ASSMBL_CD", "'" + iASSMBL_CD + "'" );
      lblCal.put( "CALC_PARAMETER_CD", "'" + iCALC_PARAMETER_CD + "'" );
      lblCal.put( "SYMBOL", "'" + iSYMBOL_1 + "'" );
      lblCal.put( "CONSTANT_VALUE", null );
      lblCal.put( "INPUT_ORDER", "'" + iINPUT_ORDER_1 + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_CALC_INPUT, lblCal ) );

      // bl_calc_part_input table
      lblCal.clear();
      lblCal.put( "ASSMBL_CD", "'" + iASSMBL_CD + "'" );
      lblCal.put( "CALC_PARAMETER_CD", "'" + iCALC_PARAMETER_CD + "'" );
      lblCal.put( "SYMBOL", "'" + iSYMBOL_PART_1 + "'" );
      lblCal.put( "PART_GROUP_CD", "'" + iPART_GROUP_CD + "'" );
      lblCal.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lblCal.put( "MANUFACT_CD", "'" + iMANFACT_CD + "'" );
      lblCal.put( "CONSTANT_VALUE", "'" + iCONSTANT_VALUE_PART_1 + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_CALC_PART_INPUT, lblCal ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Validate CFGREQ-12154 error
      validateErrorCode( "BLCALC-00080" );

   }


   /**
    *
    * BLCALC-00080: BL_CALC.ASSMBL_CD/CALC_PARAMETER_CD is specified multiple times.
    * iCALC_PARAMETER_CD is upper case and lower case.
    */

   @Test
   public void testOPER_24042_BLCALC00080_2_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lblCal = new LinkedHashMap<>();

      // bl_calc table
      lblCal.put( "ASSMBL_CD", "'" + iASSMBL_CD + "'" );
      lblCal.put( "CALC_PARAMETER_CD", "'" + iCALC_PARAMETER_CD + "'" );
      lblCal.put( "BASIC_EQUATION", "'" + iBASIC_EQUATION + "'" );
      lblCal.put( "CALCULATION_DESCRIPTION", "'" + iCALCULATION_DESCP + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_CALC, lblCal ) );

      // bl_calc table
      lblCal.clear();
      lblCal.put( "ASSMBL_CD", "'" + iASSMBL_CD + "'" );
      lblCal.put( "CALC_PARAMETER_CD", "'ad_cyc'" );
      lblCal.put( "BASIC_EQUATION", "'" + iBASIC_EQUATION + "'" );
      lblCal.put( "CALCULATION_DESCRIPTION", "'" + iCALCULATION_DESCP + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_CALC, lblCal ) );

      // bl_calc_input table
      lblCal.clear();
      lblCal.put( "ASSMBL_CD", "'" + iASSMBL_CD + "'" );
      lblCal.put( "CALC_PARAMETER_CD", "'" + iCALC_PARAMETER_CD + "'" );
      lblCal.put( "SYMBOL", "'" + iSYMBOL_1 + "'" );
      lblCal.put( "CONSTANT_VALUE", null );
      lblCal.put( "INPUT_ORDER", "'" + iINPUT_ORDER_1 + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_CALC_INPUT, lblCal ) );

      // bl_calc_part_input table
      lblCal.clear();
      lblCal.put( "ASSMBL_CD", "'" + iASSMBL_CD + "'" );
      lblCal.put( "CALC_PARAMETER_CD", "'" + iCALC_PARAMETER_CD + "'" );
      lblCal.put( "SYMBOL", "'" + iSYMBOL_PART_1 + "'" );
      lblCal.put( "PART_GROUP_CD", "'" + iPART_GROUP_CD + "'" );
      lblCal.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lblCal.put( "MANUFACT_CD", "'" + iMANFACT_CD + "'" );
      lblCal.put( "CONSTANT_VALUE", "'" + iCONSTANT_VALUE_PART_1 + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_CALC_PART_INPUT, lblCal ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Validate CFGREQ-12154 error
      validateErrorCode( "BLCALC-00080" );

   }


   /**
    *
    * BLCALC-00080: BL_CALC.ASSMBL_CD/CALC_PARAMETER_CD is specified multiple times.
    *
    */

   @Test
   public void testOPER_24042_BLCALC00080_3_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lblCal = new LinkedHashMap<>();

      // bl_calc table
      lblCal.put( "ASSMBL_CD", "'" + iASSMBL_CD + "'" );
      lblCal.put( "CALC_PARAMETER_CD", "'" + iCALC_PARAMETER_CD + "'" );
      lblCal.put( "BASIC_EQUATION", "'" + iBASIC_EQUATION + "'" );
      lblCal.put( "CALCULATION_DESCRIPTION", "'" + iCALCULATION_DESCP + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_CALC, lblCal ) );

      // bl_calc table

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_CALC, lblCal ) );

      // bl_calc_input table
      lblCal.clear();
      lblCal.put( "ASSMBL_CD", "'" + iASSMBL_CD + "'" );
      lblCal.put( "CALC_PARAMETER_CD", "'" + iCALC_PARAMETER_CD + "'" );
      lblCal.put( "SYMBOL", "'" + iSYMBOL_1 + "'" );
      lblCal.put( "CONSTANT_VALUE", null );
      lblCal.put( "INPUT_ORDER", "'" + iINPUT_ORDER_1 + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_CALC_INPUT, lblCal ) );

      // bl_calc_part_input table
      lblCal.clear();
      lblCal.put( "ASSMBL_CD", "'" + iASSMBL_CD + "'" );
      lblCal.put( "CALC_PARAMETER_CD", "'" + iCALC_PARAMETER_CD + "'" );
      lblCal.put( "SYMBOL", "'" + iSYMBOL_PART_1 + "'" );
      lblCal.put( "PART_GROUP_CD", "'" + iPART_GROUP_CD + "'" );
      lblCal.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lblCal.put( "MANUFACT_CD", "'" + iMANFACT_CD + "'" );
      lblCal.put( "CONSTANT_VALUE", "'" + iCONSTANT_VALUE_PART_1 + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_CALC_PART_INPUT, lblCal ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Validate CFGREQ-12154 error
      validateErrorCode( "BLCALC-00080" );

   }


   /**
    *
    * BLCALC-00240: BL_CALC_INPUT.ASSMBL_CD/CALC_PARAMETER_CD/SYMBOL is specified multiple times.
    * iCALC_PARAMETER_CD is upper case and lower case.
    */

   @Test
   public void testOPER_24042_BLCALC00240_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lblCal = new LinkedHashMap<>();

      // bl_calc table
      lblCal.put( "ASSMBL_CD", "'" + iASSMBL_CD + "'" );
      lblCal.put( "CALC_PARAMETER_CD", "'" + iCALC_PARAMETER_CD + "'" );
      lblCal.put( "BASIC_EQUATION", "'" + iBASIC_EQUATION + "'" );
      lblCal.put( "CALCULATION_DESCRIPTION", "'" + iCALCULATION_DESCP + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_CALC, lblCal ) );

      // bl_calc_input table
      lblCal.clear();
      lblCal.put( "ASSMBL_CD", "'" + iASSMBL_CD + "'" );
      lblCal.put( "CALC_PARAMETER_CD", "'ad_cyc'" );
      lblCal.put( "SYMBOL", "'" + iSYMBOL_1 + "'" );
      lblCal.put( "CONSTANT_VALUE", null );
      lblCal.put( "INPUT_ORDER", "'" + iINPUT_ORDER_1 + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_CALC_INPUT, lblCal ) );

      // bl_calc_input table
      lblCal.clear();
      lblCal.put( "ASSMBL_CD", "'" + iASSMBL_CD + "'" );
      lblCal.put( "CALC_PARAMETER_CD", "'" + iCALC_PARAMETER_CD + "'" );
      lblCal.put( "SYMBOL", "'" + iSYMBOL_1 + "'" );
      lblCal.put( "CONSTANT_VALUE", null );
      lblCal.put( "INPUT_ORDER", "'" + iINPUT_ORDER_2 + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_CALC_INPUT, lblCal ) );

      // bl_calc_part_input table
      lblCal.clear();
      lblCal.put( "ASSMBL_CD", "'" + iASSMBL_CD + "'" );
      lblCal.put( "CALC_PARAMETER_CD", "'" + iCALC_PARAMETER_CD + "'" );
      lblCal.put( "SYMBOL", "'" + iSYMBOL_PART_1 + "'" );
      lblCal.put( "PART_GROUP_CD", "'" + iPART_GROUP_CD + "'" );
      lblCal.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lblCal.put( "MANUFACT_CD", "'" + iMANFACT_CD + "'" );
      lblCal.put( "CONSTANT_VALUE", "'" + iCONSTANT_VALUE_PART_1 + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_CALC_PART_INPUT, lblCal ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Validate CFGREQ-12154 error
      validateErrorCode( "BLCALC-00240" );

   }


   /**
    *
    * BLCALC-00450:
    * BL_CALC_PART_INPUT.ASSMBL_CD/CALC_PARAMETER_CD/SYMBOL/PART_GROUP_CD/PART_NO_OEM/MANUFACT_CD is
    * specified multiple times. assmbl_CD is upper case and lower case.
    */

   @Test
   public void testOPER_24042_BLCALC00450_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lblCal = new LinkedHashMap<>();

      // bl_calc table
      lblCal.put( "ASSMBL_CD", "'" + iASSMBL_CD + "'" );
      lblCal.put( "CALC_PARAMETER_CD", "'" + iCALC_PARAMETER_CD + "'" );
      lblCal.put( "BASIC_EQUATION", "'" + iBASIC_EQUATION + "'" );
      lblCal.put( "CALCULATION_DESCRIPTION", "'" + iCALCULATION_DESCP + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_CALC, lblCal ) );

      // bl_calc_part_input table
      lblCal.clear();
      lblCal.put( "ASSMBL_CD", "'" + iASSMBL_CD + "'" );
      lblCal.put( "CALC_PARAMETER_CD", "'" + iCALC_PARAMETER_CD + "'" );
      lblCal.put( "SYMBOL", "'" + iSYMBOL_PART_1 + "'" );
      lblCal.put( "PART_GROUP_CD", "'" + iPART_GROUP_CD + "'" );
      lblCal.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lblCal.put( "MANUFACT_CD", "'" + iMANFACT_CD + "'" );
      lblCal.put( "CONSTANT_VALUE", "'" + iCONSTANT_VALUE_PART_1 + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_CALC_PART_INPUT, lblCal ) );

      // bl_calc_part_input table
      lblCal.clear();
      lblCal.put( "ASSMBL_CD", "'apu_cd1'" );
      lblCal.put( "CALC_PARAMETER_CD", "'" + iCALC_PARAMETER_CD + "'" );
      lblCal.put( "SYMBOL", "'" + iSYMBOL_PART_1 + "'" );
      lblCal.put( "PART_GROUP_CD", "'" + iPART_GROUP_CD + "'" );
      lblCal.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lblCal.put( "MANUFACT_CD", "'" + iMANFACT_CD + "'" );
      lblCal.put( "CONSTANT_VALUE", "'" + iCONSTANT_VALUE_PART_1 + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_CALC_PART_INPUT, lblCal ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -2 );

      // Validate CFGREQ-12154 error
      validateErrorCode( "BLCALC-00450" );

   }


   /**
    * This test is to validate functionality of bl_calc_import PKG on ACFT.
    *
    *
    */

   @Test
   public void testACFT_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lblCal = new LinkedHashMap<>();

      // bl_calc table
      lblCal.put( "ASSMBL_CD", "'" + iASSMBL_CD_ACFT + "'" );
      lblCal.put( "CALC_PARAMETER_CD", "'" + iCALC_PARAMETER_CD + "'" );
      lblCal.put( "BASIC_EQUATION", "'" + iBASIC_EQUATION + "'" );
      lblCal.put( "CALCULATION_DESCRIPTION", "'" + iCALCULATION_DESCP + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_CALC, lblCal ) );

      // =================================================================================================================================
      // bl_calc_input table
      // first record
      lblCal.clear();
      lblCal.put( "ASSMBL_CD", "'" + iASSMBL_CD_ACFT + "'" );
      lblCal.put( "CALC_PARAMETER_CD", "'" + iCALC_PARAMETER_CD + "'" );
      lblCal.put( "SYMBOL", "'" + iSYMBOL_factor + "'" );
      lblCal.put( "CONSTANT_VALUE", "'5'" );
      lblCal.put( "INPUT_ORDER", "'" + iINPUT_ORDER_1 + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_CALC_INPUT, lblCal ) );

      // bl_calc_input table
      // second record
      lblCal.clear();
      lblCal.put( "ASSMBL_CD", "'" + iASSMBL_CD_ACFT + "'" );
      lblCal.put( "CALC_PARAMETER_CD", "'" + iCALC_PARAMETER_CD + "'" );
      lblCal.put( "SYMBOL", "'" + iSYMBOL_AT_CYC_M + "'" );
      // lblCal.put( "CONSTANT_VALUE", "'5'" );
      lblCal.put( "INPUT_ORDER", "'" + iINPUT_ORDER_2 + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_CALC_INPUT, lblCal ) );

      // bl_calc_input table
      // third record
      lblCal.clear();
      lblCal.put( "ASSMBL_CD", "'" + iASSMBL_CD_ACFT + "'" );
      lblCal.put( "CALC_PARAMETER_CD", "'" + iCALC_PARAMETER_CD + "'" );
      lblCal.put( "SYMBOL", "'" + iSYMBOL_AT_CYC + "'" );
      // lblCal.put( "CONSTANT_VALUE", "'5'" );
      lblCal.put( "INPUT_ORDER", "'" + iINPUT_ORDER_3 + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_CALC_INPUT, lblCal ) );

      // bl_calc_input table
      lblCal.clear();
      lblCal.put( "ASSMBL_CD", "'" + iASSMBL_CD_ACFT + "'" );
      lblCal.put( "CALC_PARAMETER_CD", "'" + iCALC_PARAMETER_CD + "'" );
      lblCal.put( "SYMBOL", "'" + iSYMBOL_AT_CYC_AT_CYC_M + "'" );
      // lblCal.put( "CONSTANT_VALUE", "'5'" );
      lblCal.put( "INPUT_ORDER", "'" + iINPUT_ORDER_4 + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_CALC_INPUT, lblCal ) );

      // ===================================================================================================================

      // bl_calc_part_input table
      lblCal.clear();
      lblCal.put( "ASSMBL_CD", "'" + iASSMBL_CD_ACFT + "'" );
      lblCal.put( "CALC_PARAMETER_CD", "'" + iCALC_PARAMETER_CD + "'" );
      lblCal.put( "SYMBOL", "'" + iSYMBOL_factor + "'" );
      lblCal.put( "PART_GROUP_CD", "'" + iPART_GROUP_CD_ACFT + "'" );
      lblCal.put( "PART_NO_OEM", "'" + iPART_NO_OEM_ACFT + "'" );
      lblCal.put( "MANUFACT_CD", "'" + iMANFACT_CD_ACFT + "'" );
      lblCal.put( "CONSTANT_VALUE", "'" + iCONSTANT_VALUE_PART_1 + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_CALC_PART_INPUT, lblCal ) );

      // bl_calc_part_input table
      lblCal.clear();
      lblCal.put( "ASSMBL_CD", "'" + iASSMBL_CD_ACFT + "'" );
      lblCal.put( "CALC_PARAMETER_CD", "'" + iCALC_PARAMETER_CD + "'" );
      lblCal.put( "SYMBOL", "'" + iSYMBOL_AT_CYC + "'" );
      lblCal.put( "PART_GROUP_CD", "'" + iPART_GROUP_CD_ACFT + "'" );
      lblCal.put( "PART_NO_OEM", "'" + iPART_NO_OEM_ACFT + "'" );
      lblCal.put( "MANUFACT_CD", "'" + iMANFACT_CD_ACFT + "'" );
      lblCal.put( "CONSTANT_VALUE", "'" + iCONSTANT_VALUE_PART_1 + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_CALC_PART_INPUT, lblCal ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to import functionality of bl_calc_import PKG on ACFT.
    *
    *
    */

   @Test
   public void testACFT_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testACFT_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify BL_CALC table
      simpleIDs lDataTypeIds = getDataType( iCALC_PARAMETER_CD, "CYCLES", "Adjusted Cycles" );
      iCalc_IDs =
            verifyMIMCALC( iBASIC_EQUATION, iCALCULATION_DESCP, iASSMBL_CD_ACFT, lDataTypeIds );

      // verify mim_calc_input
      // verify factor
      verifyMIMCALCINPUT( iCalc_IDs, iSYMBOL_factor, null, "1", "5", "1" );
      // verify at_cyc_m
      lDataTypeIds = getDataType( iSYMBOL_AT_CYC_M, "CYCLES", "General Cycles --manual" );
      verifyMIMCALCINPUT( iCalc_IDs, iSYMBOL_AT_CYC_M, lDataTypeIds, "2", null, "0" );
      // verify AT_CYC
      lDataTypeIds = getDataType( iSYMBOL_AT_CYC, "CYCLES", "General Cycles AT CYC--manual" );
      verifyMIMCALCINPUT( iCalc_IDs, iSYMBOL_AT_CYC, lDataTypeIds, "3", null, "0" );
      // verify AT_CYC_AT_CYC_M
      lDataTypeIds = getDataType( iSYMBOL_AT_CYC_AT_CYC_M, "CYCLES", "General Cycles @ CYC M" );
      verifyMIMCALCINPUT( iCalc_IDs, iSYMBOL_AT_CYC_AT_CYC_M, lDataTypeIds, "4", null, "0" );

      // verify mim_part_input table
      bomPartPN lPartInfor =
            getASMINFor( iPART_NO_OEM_ACFT, iMANFACT_CD_ACFT, iPART_GROUP_CD_ACFT );
      verifyMIMPARTINPUT( iCalc_IDs, "1", lPartInfor.getBOM_PART_DB_ID(),
            lPartInfor.getBOM_PART_ID(), lPartInfor.getPART_NO_DB_ID(), lPartInfor.getPART_NO_ID(),
            iCONSTANT_VALUE_PART_1 );

      verifyMIMPARTINPUT( iCalc_IDs, "3", lPartInfor.getBOM_PART_DB_ID(),
            lPartInfor.getBOM_PART_ID(), lPartInfor.getPART_NO_DB_ID(), lPartInfor.getPART_NO_ID(),
            iCONSTANT_VALUE_PART_1 );
   }


   /**
    * This test is to validate functionality of bl_calc_import PKG on APU.
    *
    *
    */

   @Test
   public void testAPU_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lblCal = new LinkedHashMap<>();

      // bl_calc table
      lblCal.put( "ASSMBL_CD", "'" + iASSMBL_CD + "'" );
      lblCal.put( "CALC_PARAMETER_CD", "'" + iCALC_PARAMETER_CD + "'" );
      lblCal.put( "BASIC_EQUATION", "'" + iBASIC_EQUATION + "'" );
      lblCal.put( "CALCULATION_DESCRIPTION", "'" + iCALCULATION_DESCP + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_CALC, lblCal ) );

      // =================================================================================================================================
      // bl_calc_input table
      // first record
      lblCal.clear();
      lblCal.put( "ASSMBL_CD", "'" + iASSMBL_CD + "'" );
      lblCal.put( "CALC_PARAMETER_CD", "'" + iCALC_PARAMETER_CD + "'" );
      lblCal.put( "SYMBOL", "'" + iSYMBOL_factor + "'" );
      lblCal.put( "CONSTANT_VALUE", "'5'" );
      lblCal.put( "INPUT_ORDER", "'" + iINPUT_ORDER_1 + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_CALC_INPUT, lblCal ) );

      // bl_calc_input table
      // second record
      lblCal.clear();
      lblCal.put( "ASSMBL_CD", "'" + iASSMBL_CD + "'" );
      lblCal.put( "CALC_PARAMETER_CD", "'" + iCALC_PARAMETER_CD + "'" );
      lblCal.put( "SYMBOL", "'" + iSYMBOL_AT_CYC_M + "'" );
      // lblCal.put( "CONSTANT_VALUE", "'5'" );
      lblCal.put( "INPUT_ORDER", "'" + iINPUT_ORDER_2 + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_CALC_INPUT, lblCal ) );

      // bl_calc_input table
      // third record
      lblCal.clear();
      lblCal.put( "ASSMBL_CD", "'" + iASSMBL_CD + "'" );
      lblCal.put( "CALC_PARAMETER_CD", "'" + iCALC_PARAMETER_CD + "'" );
      lblCal.put( "SYMBOL", "'" + iSYMBOL_AT_CYC + "'" );
      // lblCal.put( "CONSTANT_VALUE", "'5'" );
      lblCal.put( "INPUT_ORDER", "'" + iINPUT_ORDER_3 + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_CALC_INPUT, lblCal ) );

      // bl_calc_input table
      lblCal.clear();
      lblCal.put( "ASSMBL_CD", "'" + iASSMBL_CD + "'" );
      lblCal.put( "CALC_PARAMETER_CD", "'" + iCALC_PARAMETER_CD + "'" );
      lblCal.put( "SYMBOL", "'" + iSYMBOL_AT_CYC_AT_CYC_M + "'" );
      // lblCal.put( "CONSTANT_VALUE", "'5'" );
      lblCal.put( "INPUT_ORDER", "'" + iINPUT_ORDER_4 + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_CALC_INPUT, lblCal ) );

      // ===================================================================================================================

      // bl_calc_part_input table
      lblCal.clear();
      lblCal.put( "ASSMBL_CD", "'" + iASSMBL_CD + "'" );
      lblCal.put( "CALC_PARAMETER_CD", "'" + iCALC_PARAMETER_CD + "'" );
      lblCal.put( "SYMBOL", "'" + iSYMBOL_factor + "'" );
      lblCal.put( "PART_GROUP_CD", "'" + iPART_GROUP_CD + "'" );
      lblCal.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lblCal.put( "MANUFACT_CD", "'" + iMANFACT_CD + "'" );
      lblCal.put( "CONSTANT_VALUE", "'" + iCONSTANT_VALUE_PART_1 + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_CALC_PART_INPUT, lblCal ) );

      // bl_calc_part_input table
      lblCal.clear();
      lblCal.put( "ASSMBL_CD", "'" + iASSMBL_CD + "'" );
      lblCal.put( "CALC_PARAMETER_CD", "'" + iCALC_PARAMETER_CD + "'" );
      lblCal.put( "SYMBOL", "'" + iSYMBOL_AT_CYC + "'" );
      lblCal.put( "PART_GROUP_CD", "'" + iPART_GROUP_CD + "'" );
      lblCal.put( "PART_NO_OEM", "'" + iPART_NO_OEM + "'" );
      lblCal.put( "MANUFACT_CD", "'" + iMANFACT_CD + "'" );
      lblCal.put( "CONSTANT_VALUE", "'" + iCONSTANT_VALUE_PART_1 + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_CALC_PART_INPUT, lblCal ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to import functionality of bl_calc_import PKG on ACFT.
    *
    *
    */

   @Test
   public void testAPU_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testAPU_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify BL_CALC table
      simpleIDs lDataTypeIds = getDataType( iCALC_PARAMETER_CD, "CYCLES", "Adjusted Cycles" );
      iCalc_IDs = verifyMIMCALC( iBASIC_EQUATION, iCALCULATION_DESCP, iASSMBL_CD, lDataTypeIds );

      // verify mim_calc_input
      // verify factor
      verifyMIMCALCINPUT( iCalc_IDs, iSYMBOL_factor, null, "1", "5", "1" );
      // verify at_cyc_m
      lDataTypeIds = getDataType( iSYMBOL_AT_CYC_M, "CYCLES", "General Cycles --manual" );
      verifyMIMCALCINPUT( iCalc_IDs, iSYMBOL_AT_CYC_M, lDataTypeIds, "2", null, "0" );
      // verify AT_CYC
      lDataTypeIds = getDataType( iSYMBOL_AT_CYC, "CYCLES", "General Cycles AT CYC--manual" );
      verifyMIMCALCINPUT( iCalc_IDs, iSYMBOL_AT_CYC, lDataTypeIds, "3", null, "0" );
      // verify AT_CYC_AT_CYC_M
      lDataTypeIds = getDataType( iSYMBOL_AT_CYC_AT_CYC_M, "CYCLES", "General Cycles @ CYC M" );
      verifyMIMCALCINPUT( iCalc_IDs, iSYMBOL_AT_CYC_AT_CYC_M, lDataTypeIds, "4", null, "0" );

      // verify mim_part_input table
      bomPartPN lPartInfor = getASMINFor( iPART_NO_OEM, iMANFACT_CD, iPART_GROUP_CD );
      verifyMIMPARTINPUT( iCalc_IDs, "1", lPartInfor.getBOM_PART_DB_ID(),
            lPartInfor.getBOM_PART_ID(), lPartInfor.getPART_NO_DB_ID(), lPartInfor.getPART_NO_ID(),
            iCONSTANT_VALUE_PART_1 );

      verifyMIMPARTINPUT( iCalc_IDs, "3", lPartInfor.getBOM_PART_DB_ID(),
            lPartInfor.getBOM_PART_ID(), lPartInfor.getPART_NO_DB_ID(), lPartInfor.getPART_NO_ID(),
            iCONSTANT_VALUE_PART_1 );
   }


   /**
    * This test is to validate functionality of bl_calc_import PKG on multiple records.
    *
    *
    */

   @Test
   public void testMultiple_VALIDATION() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testACFT_VALIDATION();
      testAPU_VALIDATION();

   }


   /**
    * This test is to import functionality of bl_calc_import PKG on ACFT.
    *
    *
    */

   @Test
   public void testMultiple_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testMultiple_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify BL_CALC table
      simpleIDs lDataTypeIds = getDataType( iCALC_PARAMETER_CD, "CYCLES", "Adjusted Cycles" );
      iCalc_IDs =
            verifyMIMCALC( iBASIC_EQUATION, iCALCULATION_DESCP, iASSMBL_CD_ACFT, lDataTypeIds );
      iCalc_IDs_2 = verifyMIMCALC( iBASIC_EQUATION, iCALCULATION_DESCP, iASSMBL_CD, lDataTypeIds );

      // verify mim_calc_input
      // verify factor
      verifyMIMCALCINPUT( iCalc_IDs, iSYMBOL_factor, null, "1", "5", "1" );
      verifyMIMCALCINPUT( iCalc_IDs_2, iSYMBOL_factor, null, "1", "5", "1" );
      // verify at_cyc_m
      lDataTypeIds = getDataType( iSYMBOL_AT_CYC_M, "CYCLES", "General Cycles --manual" );
      verifyMIMCALCINPUT( iCalc_IDs, iSYMBOL_AT_CYC_M, lDataTypeIds, "2", null, "0" );
      verifyMIMCALCINPUT( iCalc_IDs_2, iSYMBOL_AT_CYC_M, lDataTypeIds, "2", null, "0" );
      // verify AT_CYC
      lDataTypeIds = getDataType( iSYMBOL_AT_CYC, "CYCLES", "General Cycles AT CYC--manual" );
      verifyMIMCALCINPUT( iCalc_IDs, iSYMBOL_AT_CYC, lDataTypeIds, "3", null, "0" );
      verifyMIMCALCINPUT( iCalc_IDs_2, iSYMBOL_AT_CYC, lDataTypeIds, "3", null, "0" );
      // verify AT_CYC_AT_CYC_M
      lDataTypeIds = getDataType( iSYMBOL_AT_CYC_AT_CYC_M, "CYCLES", "General Cycles @ CYC M" );
      verifyMIMCALCINPUT( iCalc_IDs, iSYMBOL_AT_CYC_AT_CYC_M, lDataTypeIds, "4", null, "0" );
      verifyMIMCALCINPUT( iCalc_IDs_2, iSYMBOL_AT_CYC_AT_CYC_M, lDataTypeIds, "4", null, "0" );

      // verify mim_part_input table
      // ACFT verification
      bomPartPN lPartInfor =
            getASMINFor( iPART_NO_OEM_ACFT, iMANFACT_CD_ACFT, iPART_GROUP_CD_ACFT );
      verifyMIMPARTINPUT( iCalc_IDs, "1", lPartInfor.getBOM_PART_DB_ID(),
            lPartInfor.getBOM_PART_ID(), lPartInfor.getPART_NO_DB_ID(), lPartInfor.getPART_NO_ID(),
            iCONSTANT_VALUE_PART_1 );

      verifyMIMPARTINPUT( iCalc_IDs, "3", lPartInfor.getBOM_PART_DB_ID(),
            lPartInfor.getBOM_PART_ID(), lPartInfor.getPART_NO_DB_ID(), lPartInfor.getPART_NO_ID(),
            iCONSTANT_VALUE_PART_1 );

      // APU verification
      lPartInfor = getASMINFor( iPART_NO_OEM, iMANFACT_CD, iPART_GROUP_CD );
      verifyMIMPARTINPUT( iCalc_IDs_2, "1", lPartInfor.getBOM_PART_DB_ID(),
            lPartInfor.getBOM_PART_ID(), lPartInfor.getPART_NO_DB_ID(), lPartInfor.getPART_NO_ID(),
            iCONSTANT_VALUE_PART_1 );

      verifyMIMPARTINPUT( iCalc_IDs_2, "3", lPartInfor.getBOM_PART_DB_ID(),
            lPartInfor.getBOM_PART_ID(), lPartInfor.getPART_NO_DB_ID(), lPartInfor.getPART_NO_ID(),
            iCONSTANT_VALUE_PART_1 );
   }

   // ================================================================================================================


   /*
    * This function is to retrieve PART's assemble information.
    *
    *
    */
   public bomPartPN getASMINFor( String aPART_NO_OEM, String aMANUFACT_CD, String aBOMPART_CD ) {

      bomPartPN lassm = null;

      String lQuery =
            "select EQP_BOM_PART.BOM_PART_DB_ID,EQP_BOM_PART.BOM_PART_ID,EQP_PART_NO.PART_NO_DB_ID, EQP_PART_NO.PART_NO_ID from eqp_bom_part "
                  + "inner join EQP_PART_BASELINE on "
                  + "EQP_BOM_PART.BOM_PART_DB_ID=EQP_PART_BASELINE.Bom_Part_Db_Id and "
                  + "EQP_BOM_PART.BOM_PART_ID=EQP_PART_BASELINE.BOM_PART_ID "
                  + "inner join EQP_PART_NO on "
                  + "EQP_PART_NO.PART_NO_DB_ID=EQP_PART_BASELINE.PART_NO_DB_ID and "
                  + "EQP_PART_NO.PART_NO_ID=EQP_PART_BASELINE.PART_NO_ID "
                  + "where EQP_PART_NO.Part_No_Oem='" + aPART_NO_OEM
                  + "' and EQP_PART_NO.Manufact_Cd='" + aMANUFACT_CD
                  + "' and EQP_BOM_PART.Bom_Part_Cd='" + aBOMPART_CD + "'";

      ResultSet assbomResultSetRecords;
      try {
         assbomResultSetRecords = runQuery( lQuery );
         assbomResultSetRecords.next();
         lassm = new bomPartPN( assbomResultSetRecords.getString( "BOM_PART_DB_ID" ),
               assbomResultSetRecords.getString( "BOM_PART_ID" ),
               assbomResultSetRecords.getString( "PART_NO_DB_ID" ),
               assbomResultSetRecords.getString( "PART_NO_ID" ) );

      } catch ( SQLException e ) {
         e.printStackTrace();
      }

      return lassm;

   }


   /**
    * This function is to verify mim_calc table
    *
    *
    */

   public simpleIDs getDataType( String aDATA_TYPE_CD, String aENG_UNIT_CD,
         String aDATA_TYPE_SDESC ) {
      String[] iIds = { "DATA_TYPE_DB_ID", "DATA_TYPE_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "DATA_TYPE_CD", aDATA_TYPE_CD );
      lArgs.addArguments( "ENG_UNIT_CD", aENG_UNIT_CD );
      lArgs.addArguments( "DATA_TYPE_SDESC", aDATA_TYPE_SDESC );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.MIM_DATA_TYPE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lIds;

   }


   /**
    * This function is to verify mim_calc table
    *
    *
    */

   public simpleIDs verifyMIMCALC( String aCALC_MDESC, String aEQN_LDESC, String aASSMBL_CD,
         simpleIDs aDataTypeIds ) {
      String[] iIds = { "CALC_DB_ID", "CALC_ID", "DATA_TYPE_DB_ID", "DATA_TYPE_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "CALC_MDESC", aCALC_MDESC );
      lArgs.addArguments( "EQN_LDESC", aEQN_LDESC );
      lArgs.addArguments( "ASSMBL_CD", aASSMBL_CD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.MIM_CALC, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      Assert.assertTrue( "DATA_TYPE_DB_ID",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aDataTypeIds.getNO_DB_ID() ) );
      Assert.assertTrue( "DATA_TYPE_ID",
            llists.get( 0 ).get( 3 ).equalsIgnoreCase( aDataTypeIds.getNO_ID() ) );

      return lIds;

   }


   /**
    * This function is to verify mim_calc_input table
    *
    *
    */

   public void verifyMIMCALCINPUT( simpleIDs aCALCIds, String aINPUT_CD, simpleIDs aDataTypeIds,
         String aINPUT_ORD, String aINPUT_QT, String aCONSTANT_BOOL ) {
      String[] iIds =
            { "DATA_TYPE_DB_ID", "DATA_TYPE_ID", "INPUT_ORD", "INPUT_QT", "CONSTANT_BOOL" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "CALC_DB_ID", aCALCIds.getNO_DB_ID() );
      lArgs.addArguments( "CALC_ID", aCALCIds.getNO_ID() );
      lArgs.addArguments( "INPUT_CD", aINPUT_CD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.MIM_CALC_INPUT, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      if ( aDataTypeIds != null ) {
         Assert.assertTrue( "DATA_TYPE_DB_ID",
               llists.get( 0 ).get( 0 ).equalsIgnoreCase( aDataTypeIds.getNO_DB_ID() ) );
         Assert.assertTrue( "DATA_TYPE_ID",
               llists.get( 0 ).get( 1 ).equalsIgnoreCase( aDataTypeIds.getNO_ID() ) );
      }

      Assert.assertTrue( "INPUT_ORD", llists.get( 0 ).get( 2 ).equalsIgnoreCase( aINPUT_ORD ) );

      if ( aINPUT_QT != null ) {
         Assert.assertTrue( "INPUT_QT", llists.get( 0 ).get( 3 ).equalsIgnoreCase( aINPUT_QT ) );

      }

      Assert.assertTrue( "CONSTANT_BOOL",
            llists.get( 0 ).get( 4 ).equalsIgnoreCase( aCONSTANT_BOOL ) );

   }


   /**
    * This function is to verify mim_part_input table
    *
    *
    */

   public void verifyMIMPARTINPUT( simpleIDs aCALCIds, String aINPUT_ID, String aBOM_PART_DB_ID,
         String aBOM_PART_ID, String aPART_NO_DB_ID, String aPART_NO_ID, String aINPUT_QT ) {
      String[] iIds =
            { "BOM_PART_DB_ID", "BOM_PART_ID", "PART_NO_DB_ID", "PART_NO_ID", "INPUT_QT" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "CALC_DB_ID", aCALCIds.getNO_DB_ID() );
      lArgs.addArguments( "CALC_ID", aCALCIds.getNO_ID() );
      lArgs.addArguments( "INPUT_ID", aINPUT_ID );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.MIM_PART_INPUT, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "BOM_PART_DB_ID",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aBOM_PART_DB_ID ) );
      Assert.assertTrue( "BOM_PART_ID", llists.get( 0 ).get( 1 ).equalsIgnoreCase( aBOM_PART_ID ) );
      Assert.assertTrue( "PART_NO_DB_ID",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aPART_NO_DB_ID ) );
      Assert.assertTrue( "PART_NO_ID", llists.get( 0 ).get( 3 ).equalsIgnoreCase( aPART_NO_ID ) );
      Assert.assertTrue( "INPUT_QT", llists.get( 0 ).get( 4 ).equalsIgnoreCase( aINPUT_QT ) );
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
    * This function is to retrieve errors code list
    *
    *
    */

   public List<String> getErrorCodeList() {
      String[] iIds = { "RESULT_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      String iQueryString = "select bl_proc_result.result_cd " + " from bl_proc_result "
            + " inner join DL_REF_MESSAGE on "
            + " DL_REF_MESSAGE.result_cd=bl_proc_result.result_cd ";
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      List<String> llistM = new ArrayList<String>();
      for ( int i = 0; i < llists.size(); i++ ) {
         llistM.add( llists.get( i ).get( 0 ) );
      }

      return llistM;

   }


   /**
    * This function is to get detail of error code
    *
    *
    */

   public String getErrorDetail( String aErrorcode ) {

      String[] iIds = { "USER_DESC" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();

      lArgs.addArguments( "RESULT_CD", aErrorcode );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.DL_REF_MESSAGE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      return llists.get( 0 ).get( 0 );

   }


   /**
    * This function is called by after test method to restore data which has been modified by tests.
    *
    *
    */

   public void RestoreData() {

      String lStrDelete = null;

      if ( iCalc_IDs != null ) {

         // delete MIM_CALC
         lStrDelete = "delete from " + TableUtil.MIM_CALC + "  where CALC_DB_ID="
               + iCalc_IDs.getNO_DB_ID() + " and CALC_ID=" + iCalc_IDs.getNO_ID();
         executeSQL( lStrDelete );

         // delete MIM_CALC_INPUT
         lStrDelete = "delete from " + TableUtil.MIM_CALC_INPUT + "  where CALC_DB_ID="
               + iCalc_IDs.getNO_DB_ID() + " and CALC_ID=" + iCalc_IDs.getNO_ID();
         executeSQL( lStrDelete );

         // delete MIM_PART_INPUT
         lStrDelete = "delete from " + TableUtil.MIM_PART_INPUT + "  where CALC_DB_ID="
               + iCalc_IDs.getNO_DB_ID() + " and CALC_ID=" + iCalc_IDs.getNO_ID();
         executeSQL( lStrDelete );

      }

      if ( iCalc_IDs_2 != null ) {

         // delete MIM_CALC
         lStrDelete = "delete from " + TableUtil.MIM_CALC + "  where CALC_DB_ID="
               + iCalc_IDs_2.getNO_DB_ID() + " and CALC_ID=" + iCalc_IDs_2.getNO_ID();
         executeSQL( lStrDelete );

         // delete MIM_CALC_INPUT
         lStrDelete = "delete from " + TableUtil.MIM_CALC_INPUT + "  where CALC_DB_ID="
               + iCalc_IDs_2.getNO_DB_ID() + " and CALC_ID=" + iCalc_IDs_2.getNO_ID();
         executeSQL( lStrDelete );

         // delete MIM_PART_INPUT
         lStrDelete = "delete from " + TableUtil.MIM_PART_INPUT + "  where CALC_DB_ID="
               + iCalc_IDs_2.getNO_DB_ID() + " and CALC_ID=" + iCalc_IDs_2.getNO_ID();
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
            CallableStatement lPrepareCallDeferral;

            try {
               if ( allornone ) {

                  lPrepareCallDeferral = getConnection()
                        .prepareCall( "BEGIN  bl_calc_import.validate(aiv_exist_in_mx =>'STRICT',"
                              + " aib_allornone =>true," + " aon_retcode =>?,"
                              + " aov_retmsg =>?); END;" );
               } else {
                  lPrepareCallDeferral = getConnection().prepareCall(
                        "BEGIN  bl_deferral_references_import.validate(aiv_exist_in_mx =>'STRICT',"
                              + " aib_allornone =>false," + " aon_retcode =>?,"
                              + " aov_retmsg =>?); END;" );

               }

               lPrepareCallDeferral.registerOutParameter( 1, Types.INTEGER );
               lPrepareCallDeferral.registerOutParameter( 2, Types.VARCHAR );
               lPrepareCallDeferral.execute();
               commit();
               lReturn = lPrepareCallDeferral.getInt( 1 );
            } catch ( SQLException e ) {
               e.printStackTrace();
            }
            return lReturn;

         }


         @Override
         public int runImport( boolean allornone ) {
            int lReturn = 0;
            CallableStatement lPrepareCallDeferral;

            try {

               if ( allornone ) {

                  lPrepareCallDeferral = getConnection()
                        .prepareCall( "BEGIN  bl_calc_import.import(aiv_exist_in_mx =>'STRICT',"
                              + " aib_allornone =>true," + " aon_retcode =>?,"
                              + " aov_retmsg =>?); END;" );
               } else {
                  lPrepareCallDeferral = getConnection()
                        .prepareCall( "BEGIN  bl_calc_import.import(aiv_exist_in_mx =>'STRICT',"
                              + " aib_allornone =>false," + " aon_retcode =>?,"
                              + " aov_retmsg =>?); END;" );

               }
               lPrepareCallDeferral.registerOutParameter( 1, Types.INTEGER );
               lPrepareCallDeferral.registerOutParameter( 2, Types.VARCHAR );
               lPrepareCallDeferral.execute();
               commit();
               lReturn = lPrepareCallDeferral.getInt( 1 );
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
}
