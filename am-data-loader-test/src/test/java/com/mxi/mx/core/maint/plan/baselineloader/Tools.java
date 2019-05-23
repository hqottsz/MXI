package com.mxi.mx.core.maint.plan.baselineloader;

import java.sql.CallableStatement;
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

import com.mxi.mx.core.maint.plan.datamodels.assmbleInfor;
import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;
import com.mxi.mx.util.BaselineLoaderTest;
import com.mxi.mx.util.StringUtils;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.ValidationAndImport;
import com.mxi.mx.util.WhereClause;


/**
 * This test suite contains test cases on validation and import functionality on TOOL_IMPORT.
 *
 * @author ALICIA QIAN
 */
public class Tools extends BaselineLoaderTest {

   @Rule
   public TestName testName = new TestName();
   ValidationAndImport ivalidationandimport;

   String iINV_CLASS_CD_TRK = "TRK";
   String iINV_CLASS_CD_BATCH = "BATCH";
   String iINV_CLASS_CD_SER = "SER";

   String iBOM_CLASS_CD_TSE = "TSE";

   String iPART_GROUP_CD_1 = "PGCD001";
   String iPART_GROUP_NAME_1 = "PGNAME1";
   String iSTD_PART_NO_OEM_1 = "STDPNOEM1";
   String iSTD_PART_MANUFACT_CD_1 = "11111";
   String iPART_NO_SDESC_STD = "STDSDESC1";

   String iPART_NO_SDESC_1 = "PNSDESC1";
   String iPART_NO_OEM_1 = "STDPNOEM1";
   String iMANUFACT_CD_1 = "11111";

   String iPART_NO_SDESC_2 = "PNSDESC2";
   String iPART_NO_OEM_2 = "PNTOOL002";
   String iMANUFACT_CD_2 = "ABC11";

   String iPART_NO_SDESC_3 = "PNSDESC3";
   String iPART_NO_OEM_3 = "STDPNOEM3";
   String iMANUFACT_CD_3 = "11111";

   String iPART_NO_SDESC_4 = "PNSDESC4";
   String iPART_NO_OEM_4 = "PNTOOL004";
   String iMANUFACT_CD_4 = "ABC11";

   String iPART_GROUP_CD_2 = "PGCD002";
   String iPART_GROUP_NAME_2 = "PGNAME2";
   String iSTD_PART_NO_OEM_2 = "STDPNOEM2";
   String iSTD_PART_MANUFACT_CD_2 = "1234567890";
   String iPART_NO_SDESC_STD_2 = "STDSDESC2";

   String iAPPL_EFF_LDESC = "001-003";
   String iSTANDARD_BOOL_Y = "1";
   String iSTANDARD_BOOL_N = "0";

   String iCFG_SLOT_STATUS_CD_ACTV = "ACTV";
   String iPART_STATUS_CD = "ACTV";
   String iPART_USE_CD = "TOOLS";

   simpleIDs iBOM_PART_ID_1 = null;
   simpleIDs iBOM_PART_ID_2 = null;
   simpleIDs iPART_NO_ID_1 = null;
   simpleIDs iPART_NO_ID_2 = null;
   simpleIDs iPART_NO_ID_3 = null;
   simpleIDs iPART_NO_ID_4 = null;
   assmbleInfor iBomINFOR_1 = null;
   assmbleInfor iBomINFOR_2 = null;


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
      reSetIds();

   }


   /**
    * This test is to verify the fix of OPER-18780 on TOOL_IMPORT. The validation error CFGTLF-11010
    * should be raised when one tool has different inv_class_cd in different group. Error on
    * standard record.
    *
    */

   @Test
   public void testOPER18780VALIDATION_case1() {
      String lErrorcode = "CFGTLP-11010";

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lToolMap = new LinkedHashMap<>();

      // C_TOOL_DEF
      insertTable_C_TOOL_DEF_18780_1();

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify error code
      ErrorCodeValidation( iPART_GROUP_CD_2, iPART_NO_OEM_2, lErrorcode );
      ErrorCodeValidation( iPART_GROUP_CD_1, iPART_NO_OEM_2, lErrorcode );
      ErrorCodeValidation( iPART_GROUP_CD_1, iSTD_PART_NO_OEM_1, null );

   }


   /**
    * This test is to verify the fix of OPER-18780 on TOOL_IMPORT. The validation error CFGTLF-11010
    * should be raised when one tool has different inv_class_cd in different group. Error on
    * non-standard record.
    *
    */
   @Test
   public void testOPER18780VALIDATION_case2() {
      String lErrorcode = "CFGTLP-11010";

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lToolMap = new LinkedHashMap<>();

      // C_TOOL_DEF
      insertTable_C_TOOL_DEF_18780_2();

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // verify error code
      ErrorCodeValidation( iPART_GROUP_CD_2, iPART_NO_OEM_2, lErrorcode );
      ErrorCodeValidation( iPART_GROUP_CD_1, iPART_NO_OEM_2, lErrorcode );
      ErrorCodeValidation( iPART_GROUP_CD_1, iSTD_PART_NO_OEM_1, null );
      ErrorCodeValidation( iPART_GROUP_CD_2, iSTD_PART_NO_OEM_2, null );

   }


   /**
    * This test is to verify TOOL_IMPORT validation functionality on inv_class_cd=TRK with provided
    * group information.
    *
    *
    */

   @Test
   public void testTRK_GROUPCD_GROUPNAME_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lToolMap = new LinkedHashMap<>();

      // C_TOOL_DEF
      // first record
      lToolMap.put( "PART_GROUP_CD", "'" + iPART_GROUP_CD_1 + "'" );
      lToolMap.put( "PART_GROUP_NAME", "'" + iPART_GROUP_NAME_1 + "'" );
      lToolMap.put( "STD_PART_NO_OEM", "'" + iSTD_PART_NO_OEM_1 + "'" );
      lToolMap.put( "STD_PART_MANUFACT_CD", "'" + iSTD_PART_MANUFACT_CD_1 + "'" );
      lToolMap.put( "PART_NO_SDESC", "'" + iPART_NO_SDESC_STD + "'" );
      lToolMap.put( "INV_CLASS_CD", "'" + iINV_CLASS_CD_TRK + "'" );
      lToolMap.put( "PART_NO_OEM", "'" + iSTD_PART_NO_OEM_1 + "'" );
      lToolMap.put( "MANUFACT_CD", "'" + iSTD_PART_MANUFACT_CD_1 + "'" );
      lToolMap.put( "APPL_EFF_LDESC", "'" + iAPPL_EFF_LDESC + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_TOOL_DEF, lToolMap ) );

      // second record
      lToolMap.clear();
      lToolMap.put( "PART_GROUP_CD", "'" + iPART_GROUP_CD_1 + "'" );
      lToolMap.put( "PART_GROUP_NAME", "'" + iPART_GROUP_NAME_1 + "'" );
      lToolMap.put( "STD_PART_NO_OEM", "'" + iSTD_PART_NO_OEM_1 + "'" );
      lToolMap.put( "STD_PART_MANUFACT_CD", "'" + iSTD_PART_MANUFACT_CD_1 + "'" );
      lToolMap.put( "PART_NO_SDESC", "'" + iPART_NO_SDESC_2 + "'" );
      lToolMap.put( "INV_CLASS_CD", "'" + iINV_CLASS_CD_TRK + "'" );
      lToolMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_2 + "'" );
      lToolMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_2 + "'" );
      lToolMap.put( "APPL_EFF_LDESC", null );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_TOOL_DEF, lToolMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify TOOL_IMPORT import functionality on inv_class_cd=TRK with provided
    * group information.
    *
    *
    */
   @Test
   public void testTRK_GROUPCD_GROUPNAME_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testTRK_GROUPCD_GROUPNAME_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify EQP_ASSMBL_BOM table
      iBomINFOR_1 = VerifyEQPASSMBLBOM( iINV_CLASS_CD_TRK, iPART_GROUP_CD_1, iPART_GROUP_NAME_1,
            iBOM_CLASS_CD_TSE, iCFG_SLOT_STATUS_CD_ACTV );

      // Verify eqp_bom_part
      iBOM_PART_ID_1 = VerifyEQPBOMPART( iBomINFOR_1, iINV_CLASS_CD_TRK, iPART_GROUP_CD_1,
            iPART_GROUP_NAME_1 );

      // EQP_PART_BASELINE
      iPART_NO_ID_1 = VerifyEQPPARTBASELINE( iBOM_PART_ID_1, iSTANDARD_BOOL_Y, iAPPL_EFF_LDESC );
      iPART_NO_ID_2 = VerifyEQPPARTBASELINE( iBOM_PART_ID_1, iSTANDARD_BOOL_N, null );

      // verify eqp_part_no
      VerifyEQPPARTNO( iPART_NO_ID_1, iINV_CLASS_CD_TRK, iPART_STATUS_CD, iPART_USE_CD,
            iSTD_PART_MANUFACT_CD_1, iPART_NO_SDESC_STD, iSTD_PART_NO_OEM_1 );
      VerifyEQPPARTNO( iPART_NO_ID_2, iINV_CLASS_CD_TRK, iPART_STATUS_CD, iPART_USE_CD,
            iMANUFACT_CD_2, iPART_NO_SDESC_2, iPART_NO_OEM_2 );

      // verify eqp_assmbl_pos
      VerifyEQPASSMBLPOS( iBomINFOR_1, "1", "1" );

   }


   /**
    * This test is to verify TOOL_IMPORT validation functionality on inv_class_cd=BATCH with
    * provided group information.
    *
    *
    */

   @Test
   public void testBATCH_GROUPCD_GROUPNAME_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lToolMap = new LinkedHashMap<>();

      // C_TOOL_DEF
      // first record
      lToolMap.put( "PART_GROUP_CD", "'" + iPART_GROUP_CD_1 + "'" );
      lToolMap.put( "PART_GROUP_NAME", "'" + iPART_GROUP_NAME_1 + "'" );
      lToolMap.put( "STD_PART_NO_OEM", "'" + iSTD_PART_NO_OEM_1 + "'" );
      lToolMap.put( "STD_PART_MANUFACT_CD", "'" + iSTD_PART_MANUFACT_CD_1 + "'" );
      lToolMap.put( "PART_NO_SDESC", "'" + iPART_NO_SDESC_STD + "'" );
      lToolMap.put( "INV_CLASS_CD", "'" + iINV_CLASS_CD_BATCH + "'" );
      lToolMap.put( "PART_NO_OEM", "'" + iSTD_PART_NO_OEM_1 + "'" );
      lToolMap.put( "MANUFACT_CD", "'" + iSTD_PART_MANUFACT_CD_1 + "'" );
      lToolMap.put( "APPL_EFF_LDESC", "'" + iAPPL_EFF_LDESC + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_TOOL_DEF, lToolMap ) );

      // second record
      lToolMap.clear();
      lToolMap.put( "PART_GROUP_CD", "'" + iPART_GROUP_CD_1 + "'" );
      lToolMap.put( "PART_GROUP_NAME", "'" + iPART_GROUP_NAME_1 + "'" );
      lToolMap.put( "STD_PART_NO_OEM", "'" + iSTD_PART_NO_OEM_1 + "'" );
      lToolMap.put( "STD_PART_MANUFACT_CD", "'" + iSTD_PART_MANUFACT_CD_1 + "'" );
      lToolMap.put( "PART_NO_SDESC", "'" + iPART_NO_SDESC_2 + "'" );
      lToolMap.put( "INV_CLASS_CD", "'" + iINV_CLASS_CD_BATCH + "'" );
      lToolMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_2 + "'" );
      lToolMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_2 + "'" );
      lToolMap.put( "APPL_EFF_LDESC", null );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_TOOL_DEF, lToolMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify TOOL_IMPORT import functionality on inv_class_cd=BATCH with provided
    * group information.
    *
    *
    */
   @Test
   public void testBATCH_GROUPCD_GROUPNAME_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testBATCH_GROUPCD_GROUPNAME_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify EQP_ASSMBL_BOM table
      // iBomINFOR_1 = VerifyEQPASSMBLBOM( iINV_CLASS_CD_BATCH, iPART_GROUP_CD_1,
      // iPART_GROUP_NAME_1,
      // iBOM_CLASS_CD_TSE, iCFG_SLOT_STATUS_CD_ACTV );
      iBomINFOR_1 = new assmbleInfor( "4650", iBOM_CLASS_CD_TSE, "0", null );

      // Verify eqp_bom_part
      iBOM_PART_ID_1 = VerifyEQPBOMPART( iBomINFOR_1, iINV_CLASS_CD_BATCH, iPART_GROUP_CD_1,
            iPART_GROUP_NAME_1 );

      // EQP_PART_BASELINE
      iPART_NO_ID_1 = VerifyEQPPARTBASELINE( iBOM_PART_ID_1, iSTANDARD_BOOL_Y, iAPPL_EFF_LDESC );
      iPART_NO_ID_2 = VerifyEQPPARTBASELINE( iBOM_PART_ID_1, iSTANDARD_BOOL_N, null );

      // verify eqp_part_no
      VerifyEQPPARTNO( iPART_NO_ID_1, iINV_CLASS_CD_BATCH, iPART_STATUS_CD, iPART_USE_CD,
            iSTD_PART_MANUFACT_CD_1, iPART_NO_SDESC_STD, iSTD_PART_NO_OEM_1 );
      VerifyEQPPARTNO( iPART_NO_ID_2, iINV_CLASS_CD_BATCH, iPART_STATUS_CD, iPART_USE_CD,
            iMANUFACT_CD_2, iPART_NO_SDESC_2, iPART_NO_OEM_2 );

      // verify eqp_assmbl_pos
      // VerifyEQPASSMBLPOS( iBomINFOR_1, "1", "1" );

   }


   /**
    * This test is to verify TOOL_IMPORT validation functionality on inv_class_cd=SER with provided
    * group information.
    *
    *
    */

   @Test
   public void testSER_GROUPCD_GROUPNAME_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lToolMap = new LinkedHashMap<>();

      // C_TOOL_DEF
      // first record
      lToolMap.put( "PART_GROUP_CD", "'" + iPART_GROUP_CD_2 + "'" );
      lToolMap.put( "PART_GROUP_NAME", "'" + iPART_GROUP_NAME_2 + "'" );
      lToolMap.put( "STD_PART_NO_OEM", "'" + iSTD_PART_NO_OEM_2 + "'" );
      lToolMap.put( "STD_PART_MANUFACT_CD", "'" + iSTD_PART_MANUFACT_CD_2 + "'" );
      lToolMap.put( "PART_NO_SDESC", "'" + iPART_NO_SDESC_STD_2 + "'" );
      lToolMap.put( "INV_CLASS_CD", "'" + iINV_CLASS_CD_SER + "'" );
      lToolMap.put( "PART_NO_OEM", "'" + iSTD_PART_NO_OEM_2 + "'" );
      lToolMap.put( "MANUFACT_CD", "'" + iSTD_PART_MANUFACT_CD_2 + "'" );
      lToolMap.put( "APPL_EFF_LDESC", "'" + iAPPL_EFF_LDESC + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_TOOL_DEF, lToolMap ) );

      // second record
      lToolMap.clear();
      lToolMap.put( "PART_GROUP_CD", "'" + iPART_GROUP_CD_2 + "'" );
      lToolMap.put( "PART_GROUP_NAME", "'" + iPART_GROUP_NAME_2 + "'" );
      lToolMap.put( "STD_PART_NO_OEM", "'" + iSTD_PART_NO_OEM_2 + "'" );
      lToolMap.put( "STD_PART_MANUFACT_CD", "'" + iSTD_PART_MANUFACT_CD_2 + "'" );
      lToolMap.put( "PART_NO_SDESC", "'" + iPART_NO_SDESC_3 + "'" );
      lToolMap.put( "INV_CLASS_CD", "'" + iINV_CLASS_CD_SER + "'" );
      lToolMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_3 + "'" );
      lToolMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_3 + "'" );
      lToolMap.put( "APPL_EFF_LDESC", null );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_TOOL_DEF, lToolMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify TOOL_IMPORT import functionality on inv_class_cd=SER with provided
    * group information.
    *
    *
    */
   @Test
   public void testSER_GROUPCD_GROUPNAME_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testSER_GROUPCD_GROUPNAME_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify EQP_ASSMBL_BOM table
      // iBomINFOR_1 = VerifyEQPASSMBLBOM( iINV_CLASS_CD_TRK, iPART_GROUP_CD_1, iPART_GROUP_NAME_1,
      // iBOM_CLASS_CD_TSE, iCFG_SLOT_STATUS_CD_ACTV );
      iBomINFOR_1 = new assmbleInfor( "4650", iBOM_CLASS_CD_TSE, "0", null );

      // Verify eqp_bom_part
      iBOM_PART_ID_1 = VerifyEQPBOMPART( iBomINFOR_1, iINV_CLASS_CD_SER, iPART_GROUP_CD_2,
            iPART_GROUP_NAME_2 );

      // EQP_PART_BASELINE
      iPART_NO_ID_1 = VerifyEQPPARTBASELINE( iBOM_PART_ID_1, iSTANDARD_BOOL_Y, iAPPL_EFF_LDESC );
      iPART_NO_ID_2 = VerifyEQPPARTBASELINE( iBOM_PART_ID_1, iSTANDARD_BOOL_N, null );

      // verify eqp_part_no
      VerifyEQPPARTNO( iPART_NO_ID_1, iINV_CLASS_CD_SER, iPART_STATUS_CD, iPART_USE_CD,
            iSTD_PART_MANUFACT_CD_2, iPART_NO_SDESC_STD_2, iSTD_PART_NO_OEM_2 );
      VerifyEQPPARTNO( iPART_NO_ID_2, iINV_CLASS_CD_SER, iPART_STATUS_CD, iPART_USE_CD,
            iMANUFACT_CD_3, iPART_NO_SDESC_3, iPART_NO_OEM_3 );

      // verify eqp_assmbl_pos
      // VerifyEQPASSMBLPOS( iBomINFOR_1, "1", "1" );

   }


   /**
    * This test is to verify TOOL_IMPORT validation functionality on inv_class_cd=TRK without group
    * information provided.
    *
    *
    */

   @Test
   public void testTRK_NO_GROUPCD_GROUPNAME_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lToolMap = new LinkedHashMap<>();

      // C_TOOL_DEF
      // first record
      // lToolMap.put( "PART_GROUP_CD", "'" + iPART_GROUP_CD_1 + "'" );
      // lToolMap.put( "PART_GROUP_NAME", "'" + iPART_GROUP_NAME_1 + "'" );
      lToolMap.put( "STD_PART_NO_OEM", "'" + iSTD_PART_NO_OEM_1 + "'" );
      lToolMap.put( "STD_PART_MANUFACT_CD", "'" + iSTD_PART_MANUFACT_CD_1 + "'" );
      lToolMap.put( "PART_NO_SDESC", "'" + iPART_NO_SDESC_STD + "'" );
      lToolMap.put( "INV_CLASS_CD", "'" + iINV_CLASS_CD_TRK + "'" );
      lToolMap.put( "PART_NO_OEM", "'" + iSTD_PART_NO_OEM_1 + "'" );
      lToolMap.put( "MANUFACT_CD", "'" + iSTD_PART_MANUFACT_CD_1 + "'" );
      lToolMap.put( "APPL_EFF_LDESC", "'" + iAPPL_EFF_LDESC + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_TOOL_DEF, lToolMap ) );

      // second record
      lToolMap.clear();
      // lToolMap.put( "PART_GROUP_CD", "'" + iPART_GROUP_CD_1 + "'" );
      // lToolMap.put( "PART_GROUP_NAME", "'" + iPART_GROUP_NAME_1 + "'" );
      lToolMap.put( "STD_PART_NO_OEM", "'" + iSTD_PART_NO_OEM_1 + "'" );
      lToolMap.put( "STD_PART_MANUFACT_CD", "'" + iSTD_PART_MANUFACT_CD_1 + "'" );
      lToolMap.put( "PART_NO_SDESC", "'" + iPART_NO_SDESC_2 + "'" );
      lToolMap.put( "INV_CLASS_CD", "'" + iINV_CLASS_CD_TRK + "'" );
      lToolMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_2 + "'" );
      lToolMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_2 + "'" );
      lToolMap.put( "APPL_EFF_LDESC", null );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_TOOL_DEF, lToolMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify TOOL_IMPORT import functionality on inv_class_cd=TRK with provided
    * group information.
    *
    *
    */
   @Test
   public void testTRK_NO_GROUPCD_GROUPNAME_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testTRK_NO_GROUPCD_GROUPNAME_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify EQP_ASSMBL_BOM table
      iBomINFOR_1 = VerifyEQPASSMBLBOM( iINV_CLASS_CD_TRK,
            iSTD_PART_NO_OEM_1 + "-" + iSTD_PART_MANUFACT_CD_1,
            iSTD_PART_NO_OEM_1 + "-" + iSTD_PART_MANUFACT_CD_1, iBOM_CLASS_CD_TSE,
            iCFG_SLOT_STATUS_CD_ACTV );

      // Verify eqp_bom_part
      iBOM_PART_ID_1 = VerifyEQPBOMPART( iBomINFOR_1, iINV_CLASS_CD_TRK,
            iSTD_PART_NO_OEM_1 + "-" + iSTD_PART_MANUFACT_CD_1,
            iSTD_PART_NO_OEM_1 + "-" + iSTD_PART_MANUFACT_CD_1 );

      // EQP_PART_BASELINE
      iPART_NO_ID_1 = VerifyEQPPARTBASELINE( iBOM_PART_ID_1, iSTANDARD_BOOL_Y, iAPPL_EFF_LDESC );
      iPART_NO_ID_2 = VerifyEQPPARTBASELINE( iBOM_PART_ID_1, iSTANDARD_BOOL_N, null );

      // verify eqp_part_no
      VerifyEQPPARTNO( iPART_NO_ID_1, iINV_CLASS_CD_TRK, iPART_STATUS_CD, iPART_USE_CD,
            iSTD_PART_MANUFACT_CD_1, iPART_NO_SDESC_STD, iSTD_PART_NO_OEM_1 );
      VerifyEQPPARTNO( iPART_NO_ID_2, iINV_CLASS_CD_TRK, iPART_STATUS_CD, iPART_USE_CD,
            iMANUFACT_CD_2, iPART_NO_SDESC_2, iPART_NO_OEM_2 );

      // verify eqp_assmbl_pos
      VerifyEQPASSMBLPOS( iBomINFOR_1, "1", "1" );

   }


   /**
    * This test is to verify TOOL_IMPORT validation functionality on inv_class_cd=BATCH without
    * provided group information.
    *
    *
    */

   @Test
   public void testBATCH_NO_GROUPCD_GROUPNAME_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      Map<String, String> lToolMap = new LinkedHashMap<>();

      // C_TOOL_DEF
      // first record
      // lToolMap.put( "PART_GROUP_CD", "'" + iPART_GROUP_CD_1 + "'" );
      // lToolMap.put( "PART_GROUP_NAME", "'" + iPART_GROUP_NAME_1 + "'" );
      lToolMap.put( "STD_PART_NO_OEM", "'" + iSTD_PART_NO_OEM_2 + "'" );
      lToolMap.put( "STD_PART_MANUFACT_CD", "'" + iSTD_PART_MANUFACT_CD_2 + "'" );
      lToolMap.put( "PART_NO_SDESC", "'" + iPART_NO_SDESC_STD_2 + "'" );
      lToolMap.put( "INV_CLASS_CD", "'" + iINV_CLASS_CD_BATCH + "'" );
      lToolMap.put( "PART_NO_OEM", "'" + iSTD_PART_NO_OEM_2 + "'" );
      lToolMap.put( "MANUFACT_CD", "'" + iSTD_PART_MANUFACT_CD_2 + "'" );
      lToolMap.put( "APPL_EFF_LDESC", "'" + iAPPL_EFF_LDESC + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_TOOL_DEF, lToolMap ) );

      // second record
      lToolMap.clear();
      // lToolMap.put( "PART_GROUP_CD", "'" + iPART_GROUP_CD_1 + "'" );
      // lToolMap.put( "PART_GROUP_NAME", "'" + iPART_GROUP_NAME_1 + "'" );
      lToolMap.put( "STD_PART_NO_OEM", "'" + iSTD_PART_NO_OEM_2 + "'" );
      lToolMap.put( "STD_PART_MANUFACT_CD", "'" + iSTD_PART_MANUFACT_CD_2 + "'" );
      lToolMap.put( "PART_NO_SDESC", "'" + iPART_NO_SDESC_3 + "'" );
      lToolMap.put( "INV_CLASS_CD", "'" + iINV_CLASS_CD_BATCH + "'" );
      lToolMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_3 + "'" );
      lToolMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_3 + "'" );
      lToolMap.put( "APPL_EFF_LDESC", null );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_TOOL_DEF, lToolMap ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify TOOL_IMPORT import functionality on inv_class_cd=BATCH without provided
    * group information.
    *
    *
    */
   @Test
   public void testBATCH_NO_GROUPCD_GROUPNAME_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testBATCH_NO_GROUPCD_GROUPNAME_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify EQP_ASSMBL_BOM table
      // iBomINFOR_1 = VerifyEQPASSMBLBOM( iINV_CLASS_CD_BATCH, iPART_GROUP_CD_1,
      // iPART_GROUP_NAME_1,
      // iBOM_CLASS_CD_TSE, iCFG_SLOT_STATUS_CD_ACTV );
      iBomINFOR_1 = new assmbleInfor( "4650", iBOM_CLASS_CD_TSE, "0", null );

      // Verify eqp_bom_part
      iBOM_PART_ID_1 = VerifyEQPBOMPART( iBomINFOR_1, iINV_CLASS_CD_BATCH,
            iSTD_PART_NO_OEM_2 + "-" + iSTD_PART_MANUFACT_CD_2,
            iSTD_PART_NO_OEM_2 + "-" + iSTD_PART_MANUFACT_CD_2 );

      // EQP_PART_BASELINE
      iPART_NO_ID_1 = VerifyEQPPARTBASELINE( iBOM_PART_ID_1, iSTANDARD_BOOL_Y, iAPPL_EFF_LDESC );
      iPART_NO_ID_2 = VerifyEQPPARTBASELINE( iBOM_PART_ID_1, iSTANDARD_BOOL_N, null );

      // verify eqp_part_no
      VerifyEQPPARTNO( iPART_NO_ID_1, iINV_CLASS_CD_BATCH, iPART_STATUS_CD, iPART_USE_CD,
            iSTD_PART_MANUFACT_CD_2, iPART_NO_SDESC_STD_2, iSTD_PART_NO_OEM_2 );
      VerifyEQPPARTNO( iPART_NO_ID_2, iINV_CLASS_CD_BATCH, iPART_STATUS_CD, iPART_USE_CD,
            iMANUFACT_CD_3, iPART_NO_SDESC_3, iPART_NO_OEM_3 );

      // verify eqp_assmbl_pos
      // VerifyEQPASSMBLPOS( iBomINFOR_1, "1", "1" );

   }


   /**
    * This test is to verify TOOL_IMPORT validation functionality on inv_class_cd=TRK and
    * inv_clasee_cd=SER with group information provided.
    *
    *
    */

   @Test
   public void testTRK_SER_GROUPCD_GROUPNAME_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testTRK_GROUPCD_GROUPNAME_VALIDATION();
      testSER_GROUPCD_GROUPNAME_VALIDATION();

      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );
   }


   /**
    * This test is to verify TOOL_IMPORT import functionality on inv_class_cd=TRK and
    * inv_clasee_cd=SER with group information provided.
    *
    *
    */
   @Test
   public void testTRK_SER_GROUPCD_GROUPNAME_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testTRK_SER_GROUPCD_GROUPNAME_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // =====Verify TRK======================================================================
      // Verify EQP_ASSMBL_BOM table
      iBomINFOR_1 = VerifyEQPASSMBLBOM( iINV_CLASS_CD_TRK, iPART_GROUP_CD_1, iPART_GROUP_NAME_1,
            iBOM_CLASS_CD_TSE, iCFG_SLOT_STATUS_CD_ACTV );

      // Verify eqp_bom_part
      iBOM_PART_ID_1 = VerifyEQPBOMPART( iBomINFOR_1, iINV_CLASS_CD_TRK, iPART_GROUP_CD_1,
            iPART_GROUP_NAME_1 );

      // EQP_PART_BASELINE
      iPART_NO_ID_1 = VerifyEQPPARTBASELINE( iBOM_PART_ID_1, iSTANDARD_BOOL_Y, iAPPL_EFF_LDESC );
      iPART_NO_ID_2 = VerifyEQPPARTBASELINE( iBOM_PART_ID_1, iSTANDARD_BOOL_N, null );

      // verify eqp_part_no
      VerifyEQPPARTNO( iPART_NO_ID_1, iINV_CLASS_CD_TRK, iPART_STATUS_CD, iPART_USE_CD,
            iSTD_PART_MANUFACT_CD_1, iPART_NO_SDESC_STD, iSTD_PART_NO_OEM_1 );
      VerifyEQPPARTNO( iPART_NO_ID_2, iINV_CLASS_CD_TRK, iPART_STATUS_CD, iPART_USE_CD,
            iMANUFACT_CD_2, iPART_NO_SDESC_2, iPART_NO_OEM_2 );

      // verify eqp_assmbl_pos
      VerifyEQPASSMBLPOS( iBomINFOR_1, "1", "1" );

      // =======Verify SER====================================================================
      iBomINFOR_2 = new assmbleInfor( "4650", iBOM_CLASS_CD_TSE, "0", null );

      // Verify eqp_bom_part
      iBOM_PART_ID_2 = VerifyEQPBOMPART( iBomINFOR_2, iINV_CLASS_CD_SER, iPART_GROUP_CD_2,
            iPART_GROUP_NAME_2 );

      // EQP_PART_BASELINE
      iPART_NO_ID_3 = VerifyEQPPARTBASELINE( iBOM_PART_ID_2, iSTANDARD_BOOL_Y, iAPPL_EFF_LDESC );
      iPART_NO_ID_4 = VerifyEQPPARTBASELINE( iBOM_PART_ID_2, iSTANDARD_BOOL_N, null );

      // verify eqp_part_no
      VerifyEQPPARTNO( iPART_NO_ID_3, iINV_CLASS_CD_SER, iPART_STATUS_CD, iPART_USE_CD,
            iSTD_PART_MANUFACT_CD_2, iPART_NO_SDESC_STD_2, iSTD_PART_NO_OEM_2 );
      VerifyEQPPARTNO( iPART_NO_ID_4, iINV_CLASS_CD_SER, iPART_STATUS_CD, iPART_USE_CD,
            iMANUFACT_CD_3, iPART_NO_SDESC_3, iPART_NO_OEM_3 );

   }


   /**
    * This test is to verify TOOL_IMPORT validation functionality on inv_class_cd=TRK and
    * inv_class_cd=BATCH without group information provided
    *
    *
    */

   @Test
   public void testTRK_BATCH_NO_GROUPCD_GROUPNAME_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testTRK_NO_GROUPCD_GROUPNAME_VALIDATION();
      testBATCH_NO_GROUPCD_GROUPNAME_VALIDATION();

      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );
   }


   /**
    * This test is to verify TOOL_IMPORT import functionality on inv_class_cd=TRK and
    * inv_class_cd=BATCH without group information provided
    *
    *
    */

   @Test
   public void testTRK_BATCH_NO_GROUPCD_GROUPNAME_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testTRK_BATCH_NO_GROUPCD_GROUPNAME_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // ===Verify TRK========================================================
      // Verify EQP_ASSMBL_BOM table
      iBomINFOR_1 = VerifyEQPASSMBLBOM( iINV_CLASS_CD_TRK,
            iSTD_PART_NO_OEM_1 + "-" + iSTD_PART_MANUFACT_CD_1,
            iSTD_PART_NO_OEM_1 + "-" + iSTD_PART_MANUFACT_CD_1, iBOM_CLASS_CD_TSE,
            iCFG_SLOT_STATUS_CD_ACTV );

      // Verify eqp_bom_part
      iBOM_PART_ID_1 = VerifyEQPBOMPART( iBomINFOR_1, iINV_CLASS_CD_TRK,
            iSTD_PART_NO_OEM_1 + "-" + iSTD_PART_MANUFACT_CD_1,
            iSTD_PART_NO_OEM_1 + "-" + iSTD_PART_MANUFACT_CD_1 );

      // EQP_PART_BASELINE
      iPART_NO_ID_1 = VerifyEQPPARTBASELINE( iBOM_PART_ID_1, iSTANDARD_BOOL_Y, iAPPL_EFF_LDESC );
      iPART_NO_ID_2 = VerifyEQPPARTBASELINE( iBOM_PART_ID_1, iSTANDARD_BOOL_N, null );

      // verify eqp_part_no
      VerifyEQPPARTNO( iPART_NO_ID_1, iINV_CLASS_CD_TRK, iPART_STATUS_CD, iPART_USE_CD,
            iSTD_PART_MANUFACT_CD_1, iPART_NO_SDESC_STD, iSTD_PART_NO_OEM_1 );
      VerifyEQPPARTNO( iPART_NO_ID_2, iINV_CLASS_CD_TRK, iPART_STATUS_CD, iPART_USE_CD,
            iMANUFACT_CD_2, iPART_NO_SDESC_2, iPART_NO_OEM_2 );

      // verify eqp_assmbl_pos
      VerifyEQPASSMBLPOS( iBomINFOR_1, "1", "1" );

      // =======Verify BATCH===================================================
      iBomINFOR_2 = new assmbleInfor( "4650", iBOM_CLASS_CD_TSE, "0", null );

      // Verify eqp_bom_part
      iBOM_PART_ID_2 = VerifyEQPBOMPART( iBomINFOR_2, iINV_CLASS_CD_BATCH,
            iSTD_PART_NO_OEM_2 + "-" + iSTD_PART_MANUFACT_CD_2,
            iSTD_PART_NO_OEM_2 + "-" + iSTD_PART_MANUFACT_CD_2 );

      // EQP_PART_BASELINE
      iPART_NO_ID_3 = VerifyEQPPARTBASELINE( iBOM_PART_ID_2, iSTANDARD_BOOL_Y, iAPPL_EFF_LDESC );
      iPART_NO_ID_4 = VerifyEQPPARTBASELINE( iBOM_PART_ID_2, iSTANDARD_BOOL_N, null );

      // verify eqp_part_no
      VerifyEQPPARTNO( iPART_NO_ID_3, iINV_CLASS_CD_BATCH, iPART_STATUS_CD, iPART_USE_CD,
            iSTD_PART_MANUFACT_CD_2, iPART_NO_SDESC_STD_2, iSTD_PART_NO_OEM_2 );
      VerifyEQPPARTNO( iPART_NO_ID_4, iINV_CLASS_CD_BATCH, iPART_STATUS_CD, iPART_USE_CD,
            iMANUFACT_CD_3, iPART_NO_SDESC_3, iPART_NO_OEM_3 );

   }


   // ================================================================================================
   /**
    * This function is to verify eqp_assmbl_pos table by given parameters .
    *
    *
    */

   public void VerifyEQPASSMBLPOS( assmbleInfor aAssmblInfor, String aASSMBL_POS_ID,
         String aEQP_POS_CD ) {

      String[] iIds = { "ASSMBL_POS_ID", "EQP_POS_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "ASSMBL_DB_ID", aAssmblInfor.getASSMBL_DB_ID() );
      lArgs.addArguments( "ASSMBL_CD", aAssmblInfor.getASSMBL_CD() );
      lArgs.addArguments( "ASSMBL_BOM_ID", aAssmblInfor.getASSMBL_BOM_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EQP_ASSMBL_POS, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "ASSMBL_POS_ID",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aASSMBL_POS_ID ) );

      Assert.assertTrue( "EQP_POS_CD", llists.get( 0 ).get( 1 ).equalsIgnoreCase( aEQP_POS_CD ) );

   }


   /**
    * This function is to verify eqp_part_no table by given parameters .
    *
    *
    */

   public void VerifyEQPPARTNO( simpleIDs aBOMPARTIds, String aINV_CLASS_CD, String aPART_STATUS_CD,
         String aPART_USE_CD, String aMANUFACT_CD, String aPART_NO_SDESC, String aPART_NO_OEM ) {

      // UTL_USER_ROLE
      String[] iIds = { "INV_CLASS_CD", "PART_STATUS_CD", "PART_USE_CD", "MANUFACT_CD",
            "PART_NO_SDESC", "PART_NO_OEM" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "PART_NO_DB_ID", aBOMPARTIds.getNO_DB_ID() );
      lArgs.addArguments( "PART_NO_ID", aBOMPARTIds.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EQP_PART_NO, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "INV_CLASS_CD",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aINV_CLASS_CD ) );

      Assert.assertTrue( "PART_STATUS_CD",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( aPART_STATUS_CD ) );

      Assert.assertTrue( "PART_USE_CD", llists.get( 0 ).get( 2 ).equalsIgnoreCase( aPART_USE_CD ) );

      Assert.assertTrue( "MANUFACT_CD", llists.get( 0 ).get( 3 ).equalsIgnoreCase( aMANUFACT_CD ) );

      Assert.assertTrue( "PART_NO_SDESC",
            llists.get( 0 ).get( 4 ).equalsIgnoreCase( aPART_NO_SDESC ) );

      Assert.assertTrue( "PART_NO_OEM", llists.get( 0 ).get( 5 ).equalsIgnoreCase( aPART_NO_OEM ) );

   }


   /**
    * This function is to verify eqp_part_baseline table by given parameters .
    *
    *
    */

   public simpleIDs VerifyEQPPARTBASELINE( simpleIDs aBOMPARTIds, String aSTANDARD_BOOL,
         String aAPPL_EFF_LDESC ) {

      String[] iIds = { "PART_NO_DB_ID", "PART_NO_ID", "APPL_EFF_LDESC" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "BOM_PART_DB_ID", aBOMPARTIds.getNO_DB_ID() );
      lArgs.addArguments( "BOM_PART_ID", aBOMPARTIds.getNO_ID() );
      lArgs.addArguments( "STANDARD_BOOL", aSTANDARD_BOOL );

      String iQueryString =
            TableUtil.buildTableQuery( TableUtil.EQP_PART_BASELINE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      simpleIDs lids = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      if ( aAPPL_EFF_LDESC != null ) {
         Assert.assertTrue( "APPL_EFF_LDESC",
               llists.get( 0 ).get( 2 ).equalsIgnoreCase( aAPPL_EFF_LDESC ) );
      } else {
         Assert.assertTrue( "APPL_EFF_LDESC", StringUtils.isBlank( llists.get( 0 ).get( 2 ) ) );
      }

      return lids;

   }


   /**
    * This function is to verify eqp_bom_part table by given parameters .
    *
    *
    */

   public simpleIDs VerifyEQPBOMPART( assmbleInfor aAssmblInfor, String aINV_CLASS_CD,
         String aBOM_PART_CD, String aBOM_PART_NAME ) {

      String[] iIds =
            { "BOM_PART_DB_ID", "BOM_PART_ID", "INV_CLASS_CD", "BOM_PART_CD", "BOM_PART_NAME" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "ASSMBL_DB_ID", aAssmblInfor.getASSMBL_DB_ID() );
      lArgs.addArguments( "ASSMBL_CD", aAssmblInfor.getASSMBL_CD() );
      lArgs.addArguments( "ASSMBL_BOM_ID", aAssmblInfor.getASSMBL_BOM_ID() );
      lArgs.addArguments( "BOM_PART_CD", aBOM_PART_CD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EQP_BOM_PART, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      simpleIDs lids = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      Assert.assertTrue( "INV_CLASS_CD",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aINV_CLASS_CD ) );

      Assert.assertTrue( "BOM_PART_CD", llists.get( 0 ).get( 3 ).equalsIgnoreCase( aBOM_PART_CD ) );

      Assert.assertTrue( "BOM_PART_NAME",
            llists.get( 0 ).get( 4 ).equalsIgnoreCase( aBOM_PART_NAME ) );

      return lids;

   }


   /**
    * This function is to verify eqp_assmbl_bom table by given parameters .
    *
    *
    */

   public assmbleInfor VerifyEQPASSMBLBOM( String aBOM_CLASS_CD, String aASSMBL_BOM_CD,
         String aASSMBL_BOM_NAME, String aASSMBL_CD, String aCFG_SLOT_STATUS_CD ) {

      // UTL_USER_ROLE
      String[] iIds = { "ASSMBL_DB_ID", "ASSMBL_CD", "ASSMBL_BOM_ID", "CFG_SLOT_STATUS_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "BOM_CLASS_CD", aBOM_CLASS_CD );
      lArgs.addArguments( "ASSMBL_BOM_CD", aASSMBL_BOM_CD );
      lArgs.addArguments( "ASSMBL_BOM_NAME", aASSMBL_BOM_NAME );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EQP_ASSMBL_BOM, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      assmbleInfor lassmblInfor = new assmbleInfor( llists.get( 0 ).get( 0 ),
            llists.get( 0 ).get( 1 ), llists.get( 0 ).get( 2 ), null );

      Assert.assertTrue( "ASSMBL_CD", llists.get( 0 ).get( 1 ).equalsIgnoreCase( aASSMBL_CD ) );

      Assert.assertTrue( "CFG_SLOT_STATUS_CD",
            llists.get( 0 ).get( 3 ).equalsIgnoreCase( aCFG_SLOT_STATUS_CD ) );

      return lassmblInfor;

   }


   /**
    * This function is to insert data into C_TOOL_DEF for test case 1
    *
    */
   public void insertTable_C_TOOL_DEF_18780_1() {

      Map<String, String> lToolMap = new LinkedHashMap<>();

      // C_TOOL_DEF
      // first record
      lToolMap.put( "PART_GROUP_CD", "'" + iPART_GROUP_CD_1 + "'" );
      lToolMap.put( "PART_GROUP_NAME", "'" + iPART_GROUP_NAME_1 + "'" );
      lToolMap.put( "STD_PART_NO_OEM", "'" + iSTD_PART_NO_OEM_1 + "'" );
      lToolMap.put( "STD_PART_MANUFACT_CD", "'" + iSTD_PART_MANUFACT_CD_1 + "'" );
      lToolMap.put( "PART_NO_SDESC", "'" + iPART_NO_SDESC_STD + "'" );
      lToolMap.put( "INV_CLASS_CD", "'" + iINV_CLASS_CD_TRK + "'" );
      lToolMap.put( "PART_NO_OEM", "'" + iSTD_PART_NO_OEM_1 + "'" );
      lToolMap.put( "MANUFACT_CD", "'" + iSTD_PART_MANUFACT_CD_1 + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_TOOL_DEF, lToolMap ) );

      // second record
      lToolMap.clear();
      lToolMap.put( "PART_GROUP_CD", "'" + iPART_GROUP_CD_1 + "'" );
      lToolMap.put( "PART_GROUP_NAME", "'" + iPART_GROUP_NAME_1 + "'" );
      lToolMap.put( "STD_PART_NO_OEM", "'" + iSTD_PART_NO_OEM_1 + "'" );
      lToolMap.put( "STD_PART_MANUFACT_CD", "'" + iSTD_PART_MANUFACT_CD_1 + "'" );
      lToolMap.put( "PART_NO_SDESC", "'" + iPART_NO_SDESC_2 + "'" );
      lToolMap.put( "INV_CLASS_CD", "'" + iINV_CLASS_CD_TRK + "'" );
      lToolMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_2 + "'" );
      lToolMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_2 + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_TOOL_DEF, lToolMap ) );

      // third record
      lToolMap.clear();
      lToolMap.put( "PART_GROUP_CD", "'" + iPART_GROUP_CD_2 + "'" );
      lToolMap.put( "PART_GROUP_NAME", "'" + iPART_GROUP_NAME_2 + "'" );
      lToolMap.put( "STD_PART_NO_OEM", "'" + iPART_NO_OEM_2 + "'" );
      lToolMap.put( "STD_PART_MANUFACT_CD", "'" + iMANUFACT_CD_2 + "'" );
      lToolMap.put( "PART_NO_SDESC", "'" + iPART_NO_SDESC_2 + "'" );
      lToolMap.put( "INV_CLASS_CD", "'" + iINV_CLASS_CD_BATCH + "'" );
      lToolMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_2 + "'" );
      lToolMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_2 + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_TOOL_DEF, lToolMap ) );

   }


   /**
    * This function is to insert data into C_TOOL_DEF for test case 2
    *
    */

   public void insertTable_C_TOOL_DEF_18780_2() {

      Map<String, String> lToolMap = new LinkedHashMap<>();

      // C_TOOL_DEF
      // first record
      lToolMap.put( "PART_GROUP_CD", "'" + iPART_GROUP_CD_1 + "'" );
      lToolMap.put( "PART_GROUP_NAME", "'" + iPART_GROUP_NAME_1 + "'" );
      lToolMap.put( "STD_PART_NO_OEM", "'" + iSTD_PART_NO_OEM_1 + "'" );
      lToolMap.put( "STD_PART_MANUFACT_CD", "'" + iSTD_PART_MANUFACT_CD_1 + "'" );
      lToolMap.put( "PART_NO_SDESC", "'" + iPART_NO_SDESC_STD + "'" );
      lToolMap.put( "INV_CLASS_CD", "'" + iINV_CLASS_CD_TRK + "'" );
      lToolMap.put( "PART_NO_OEM", "'" + iSTD_PART_NO_OEM_1 + "'" );
      lToolMap.put( "MANUFACT_CD", "'" + iSTD_PART_MANUFACT_CD_1 + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_TOOL_DEF, lToolMap ) );

      // second record
      lToolMap.clear();
      lToolMap.put( "PART_GROUP_CD", "'" + iPART_GROUP_CD_1 + "'" );
      lToolMap.put( "PART_GROUP_NAME", "'" + iPART_GROUP_NAME_1 + "'" );
      lToolMap.put( "STD_PART_NO_OEM", "'" + iSTD_PART_NO_OEM_1 + "'" );
      lToolMap.put( "STD_PART_MANUFACT_CD", "'" + iSTD_PART_MANUFACT_CD_1 + "'" );
      lToolMap.put( "PART_NO_SDESC", "'" + iPART_NO_SDESC_2 + "'" );
      lToolMap.put( "INV_CLASS_CD", "'" + iINV_CLASS_CD_TRK + "'" );
      lToolMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_2 + "'" );
      lToolMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_2 + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_TOOL_DEF, lToolMap ) );

      // third record
      lToolMap.clear();
      lToolMap.put( "PART_GROUP_CD", "'" + iPART_GROUP_CD_2 + "'" );
      lToolMap.put( "PART_GROUP_NAME", "'" + iPART_GROUP_NAME_2 + "'" );
      lToolMap.put( "STD_PART_NO_OEM", "'" + iPART_NO_OEM_2 + "'" );
      lToolMap.put( "STD_PART_MANUFACT_CD", "'" + iMANUFACT_CD_2 + "'" );
      lToolMap.put( "PART_NO_SDESC", "'" + iPART_NO_SDESC_STD_2 + "'" );
      lToolMap.put( "INV_CLASS_CD", "'" + iINV_CLASS_CD_BATCH + "'" );
      lToolMap.put( "PART_NO_OEM", "'" + iSTD_PART_NO_OEM_2 + "'" );
      lToolMap.put( "MANUFACT_CD", "'" + iSTD_PART_MANUFACT_CD_2 + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_TOOL_DEF, lToolMap ) );

      // forth record
      lToolMap.clear();
      lToolMap.put( "PART_GROUP_CD", "'" + iPART_GROUP_CD_2 + "'" );
      lToolMap.put( "PART_GROUP_NAME", "'" + iPART_GROUP_NAME_2 + "'" );
      lToolMap.put( "STD_PART_NO_OEM", "'" + iPART_NO_OEM_2 + "'" );
      lToolMap.put( "STD_PART_MANUFACT_CD", "'" + iMANUFACT_CD_2 + "'" );
      lToolMap.put( "PART_NO_SDESC", "'" + iPART_NO_SDESC_2 + "'" );
      lToolMap.put( "INV_CLASS_CD", "'" + iINV_CLASS_CD_BATCH + "'" );
      lToolMap.put( "PART_NO_OEM", "'" + iPART_NO_OEM_2 + "'" );
      lToolMap.put( "MANUFACT_CD", "'" + iMANUFACT_CD_2 + "'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_TOOL_DEF, lToolMap ) );

   }


   /**
    * This function is to check given error code presents
    *
    *
    */

   public void ErrorCodeValidation( String aPART_GROUP_CD, String aPART_NO_OEM,
         String aErrorcode ) {

      String[] iIds = { "RESULT_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "PART_GROUP_CD", aPART_GROUP_CD );
      lArgs.addArguments( "PART_NO_OEM", aPART_NO_OEM );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.C_TOOL_DEF, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      if ( aErrorcode != null ) {
         Assert.assertTrue( "RESULT_CD", llists.get( 0 ).get( 0 ).equalsIgnoreCase( aErrorcode ) );
      } else {
         Assert.assertTrue( "RESULT_CD", StringUtils.isBlank( llists.get( 0 ).get( 0 ) ) );

      }

   }


   /**
    * This function is to reset all the IDs.
    *
    *
    */

   public void reSetIds() {
      iBOM_PART_ID_1 = null;
      iBOM_PART_ID_2 = null;
      iPART_NO_ID_1 = null;
      iPART_NO_ID_2 = null;
      iPART_NO_ID_3 = null;
      iPART_NO_ID_4 = null;
      iBomINFOR_1 = null;
      iBomINFOR_2 = null;

   }


   /**
    * This function is called by after test method to restore data which has been modified by tests.
    *
    *
    */

   public void RestoreData() {

      String lStrDelete = null;

      if ( iPART_NO_ID_1 != null ) {

         // delete eqp_part_no
         lStrDelete = "delete from " + TableUtil.EQP_PART_NO + "  where PART_NO_DB_ID="
               + iPART_NO_ID_1.getNO_DB_ID() + " and PART_NO_ID=" + iPART_NO_ID_1.getNO_ID();
         executeSQL( lStrDelete );

         // delete eqp_part_baseline
         lStrDelete = "delete from " + TableUtil.EQP_PART_BASELINE + "  where PART_NO_DB_ID="
               + iPART_NO_ID_1.getNO_DB_ID() + " and PART_NO_ID=" + iPART_NO_ID_1.getNO_ID();
         executeSQL( lStrDelete );
      }

      if ( iPART_NO_ID_2 != null ) {

         // delete eqp_part_no
         lStrDelete = "delete from " + TableUtil.EQP_PART_NO + "  where PART_NO_DB_ID="
               + iPART_NO_ID_2.getNO_DB_ID() + " and PART_NO_ID=" + iPART_NO_ID_2.getNO_ID();
         executeSQL( lStrDelete );

         // delete eqp_part_baseline
         lStrDelete = "delete from " + TableUtil.EQP_PART_BASELINE + "  where PART_NO_DB_ID="
               + iPART_NO_ID_2.getNO_DB_ID() + " and PART_NO_ID=" + iPART_NO_ID_2.getNO_ID();
         executeSQL( lStrDelete );
      }

      if ( iPART_NO_ID_3 != null ) {

         // delete eqp_part_no
         lStrDelete = "delete from " + TableUtil.EQP_PART_NO + "  where PART_NO_DB_ID="
               + iPART_NO_ID_3.getNO_DB_ID() + " and PART_NO_ID=" + iPART_NO_ID_3.getNO_ID();
         executeSQL( lStrDelete );

         // delete eqp_part_baseline
         lStrDelete = "delete from " + TableUtil.EQP_PART_BASELINE + "  where PART_NO_DB_ID="
               + iPART_NO_ID_3.getNO_DB_ID() + " and PART_NO_ID=" + iPART_NO_ID_3.getNO_ID();
         executeSQL( lStrDelete );
      }

      if ( iPART_NO_ID_4 != null ) {

         // delete eqp_part_no
         lStrDelete = "delete from " + TableUtil.EQP_PART_NO + "  where PART_NO_DB_ID="
               + iPART_NO_ID_4.getNO_DB_ID() + " and PART_NO_ID=" + iPART_NO_ID_4.getNO_ID();
         executeSQL( lStrDelete );

         // delete eqp_part_baseline
         lStrDelete = "delete from " + TableUtil.EQP_PART_BASELINE + "  where PART_NO_DB_ID="
               + iPART_NO_ID_4.getNO_DB_ID() + " and PART_NO_ID=" + iPART_NO_ID_4.getNO_ID();
         executeSQL( lStrDelete );
      }

      if ( iBOM_PART_ID_1 != null ) {

         // delete eqp_bom_part
         lStrDelete = "delete from " + TableUtil.EQP_BOM_PART + "  where BOM_PART_DB_ID="
               + iBOM_PART_ID_1.getNO_DB_ID() + " and BOM_PART_ID=" + iBOM_PART_ID_1.getNO_ID();
         executeSQL( lStrDelete );

      }

      if ( iBOM_PART_ID_2 != null ) {

         // delete eqp_bom_part
         lStrDelete = "delete from " + TableUtil.EQP_BOM_PART + "  where BOM_PART_DB_ID="
               + iBOM_PART_ID_2.getNO_DB_ID() + " and BOM_PART_ID=" + iBOM_PART_ID_2.getNO_ID();
         executeSQL( lStrDelete );

      }

      if ( iBomINFOR_1 != null && testName.getMethodName().contains( "TRK" ) ) {

         // delete eqp_assmbl_bom
         lStrDelete = "delete from " + TableUtil.EQP_ASSMBL_BOM + "  where ASSMBL_DB_ID="
               + iBomINFOR_1.getASSMBL_DB_ID() + " and ASSMBL_CD='" + iBomINFOR_1.getASSMBL_CD()
               + "' and ASSMBL_BOM_ID=" + iBomINFOR_1.getASSMBL_BOM_ID();
         executeSQL( lStrDelete );

         // delete eqp_assmbl_pos
         lStrDelete = "delete from " + TableUtil.EQP_ASSMBL_POS + "  where ASSMBL_DB_ID="
               + iBomINFOR_1.getASSMBL_DB_ID() + " and ASSMBL_CD='" + iBomINFOR_1.getASSMBL_CD()
               + "' and ASSMBL_BOM_ID=" + iBomINFOR_1.getASSMBL_BOM_ID();
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
                     .prepareCall( "BEGIN tool_import.validate_tool(on_retcode =>?); END;" );

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
                     .prepareCall( "BEGIN tool_import.import_tool(on_retcode =>?); END;" );

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

}
