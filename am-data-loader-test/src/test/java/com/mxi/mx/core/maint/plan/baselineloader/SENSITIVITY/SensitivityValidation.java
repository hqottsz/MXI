package com.mxi.mx.core.maint.plan.baselineloader.SENSITIVITY;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.mxi.mx.util.TableUtil;


/**
 * This test suite contains test cases of error code validation functionality of
 * BL_SENSITIVITY_IMPORT package.
 *
 * @author ALICIA QIAN
 */
public class SensitivityValidation extends Sensitivity {

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
    * This test is to verify valid error code BLSEN-00020:ASSEMBL_CD must be provided in the
    * BL_SENSITIVITY_SYS table.
    *
    *
    */

   @Test
   public void test_BLSEN_00020() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_assign_parts_to_stock table
      Map<String, String> lSens = new LinkedHashMap<>();

      // lSens.put( "ASSMBL_CD", "\'" + iAssmblCD_ACFT + "\'" );
      lSens.put( "CONFIG_SLOT_CD", "\'" + iCONFIG_SLOT_CD_ACFT + "\'" );
      lSens.put( "SENSITIVITY_CD", "\'" + iSENSITIVITY_CD + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_SENSITIVITY_SYS, lSens ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkSensitivityErrorCode( testName.getMethodName(), "BLSEN-00020" );

   }


   /**
    * This test is to verify valid error code BLSEN-00030:CONFIG_SLOT_CD must be provided in the
    * BL_SENSITIVITY_SYS table
    *
    *
    */

   @Test
   public void test_BLSEN_00030() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_assign_parts_to_stock table
      Map<String, String> lSens = new LinkedHashMap<>();

      lSens.put( "ASSMBL_CD", "\'" + iAssmblCD_ACFT + "\'" );
      // lSens.put( "CONFIG_SLOT_CD", "\'" + iCONFIG_SLOT_CD_ACFT + "\'" );
      lSens.put( "SENSITIVITY_CD", "\'" + iSENSITIVITY_CD + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_SENSITIVITY_SYS, lSens ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkSensitivityErrorCode( testName.getMethodName(), "BLSEN-00030" );

   }


   /**
    * This test is to verify valid error code BLSEN-00040:SENSITIVITY_CD must be provided in the
    * BL_SENSITIVITY_SYS table.
    *
    *
    */

   @Test
   public void test_BLSEN_00040() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_assign_parts_to_stock table
      Map<String, String> lSens = new LinkedHashMap<>();

      lSens.put( "ASSMBL_CD", "\'" + iAssmblCD_ACFT + "\'" );
      lSens.put( "CONFIG_SLOT_CD", "\'" + iCONFIG_SLOT_CD_ACFT + "\'" );
      // lSens.put( "SENSITIVITY_CD", "\'" + iSENSITIVITY_CD + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_SENSITIVITY_SYS, lSens ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkSensitivityErrorCode( testName.getMethodName(), "BLSEN-00040" );

   }


   /**
    * This test is to verify valid error code BLSEN-00210:ASSMBL_CD/CONFIG_SLOT_CD/SENSITIVITY_CD
    * cannot exist more than once in the BL_SENSITIVITY_SYS table.
    *
    *
    */

   @Test
   public void test_BLSEN_00210() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_assign_parts_to_stock table
      Map<String, String> lSens = new LinkedHashMap<>();

      lSens.put( "ASSMBL_CD", "\'" + iAssmblCD_ACFT + "\'" );
      lSens.put( "CONFIG_SLOT_CD", "\'" + iCONFIG_SLOT_CD_ACFT + "\'" );
      lSens.put( "SENSITIVITY_CD", "\'" + iSENSITIVITY_CD + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_SENSITIVITY_SYS, lSens ) );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_SENSITIVITY_SYS, lSens ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkSensitivityErrorCode( testName.getMethodName(), "BLSEN-00210" );

   }


   /**
    * This test is to verify valid error code BLSEN-00220:ASSMBL_CD/CONFIG_SLOT_CD references
    * multiple config slots in the EQP_ASSMBL_BOM table.
    *
    *
    */

   @Test
   public void test_BLSEN_00220() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_assign_parts_to_stock table
      Map<String, String> lSens = new LinkedHashMap<>();

      lSens.put( "ASSMBL_CD", "'ACFT_T3'" );
      lSens.put( "CONFIG_SLOT_CD", "'SYS-1'" );
      lSens.put( "SENSITIVITY_CD", "\'" + iSENSITIVITY_CD + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_SENSITIVITY_SYS, lSens ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkSensitivityErrorCode( testName.getMethodName(), "BLSEN-00220" );

   }


   /**
    * This test is to verify valid error code BLSEN-00230:ASSMBL_CD references multiple assemblies
    * in the EQP_ASSMBL table.
    *
    *
    */

   @Test
   public void test_BLSEN_00230() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_assign_parts_to_stock table
      Map<String, String> lSens = new LinkedHashMap<>();

      lSens.put( "ASSMBL_CD", "'ACFT_T2'" );
      lSens.put( "CONFIG_SLOT_CD", "\'" + iCONFIG_SLOT_CD_ACFT + "\'" );
      lSens.put( "SENSITIVITY_CD", "\'" + iSENSITIVITY_CD + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_SENSITIVITY_SYS, lSens ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkSensitivityErrorCode( testName.getMethodName(), "BLSEN-00230" );

   }


   /**
    * This test is to verify valid error code BLSEN-00240:SENSITIVITY_CD references multiple
    * sensitivities in the REF_SENSITIVITY table.
    *
    *
    */

   @Test
   public void test_BLSEN_00240() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_assign_parts_to_stock table
      Map<String, String> lSens = new LinkedHashMap<>();

      lSens.put( "ASSMBL_CD", "\'" + iAssmblCD_ACFT + "\'" );
      lSens.put( "CONFIG_SLOT_CD", "\'" + iCONFIG_SLOT_CD_ACFT + "\'" );
      lSens.put( "SENSITIVITY_CD", "'AT_SN3'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_SENSITIVITY_SYS, lSens ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkSensitivityErrorCode( testName.getMethodName(), "BLSEN-00240" );

   }


   /**
    * This test is to verify valid error code BLSEN-00250:ASSMBL_CD/CONFIG_SLOT_CD/SENSITIVITY_CD
    * already exists in the EQP_ASSMBL_BOM_SENS table.
    *
    *
    */

   @Test
   public void test_BLSEN_00250() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_assign_parts_to_stock table
      Map<String, String> lSens = new LinkedHashMap<>();

      lSens.put( "ASSMBL_CD", "\'" + iAssmblCD_ACFT + "\'" );
      lSens.put( "CONFIG_SLOT_CD", "\'" + iCONFIG_SLOT_CD_ACFT + "\'" );
      lSens.put( "SENSITIVITY_CD", "\'" + iSENSITIVITY_CD + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_SENSITIVITY_SYS, lSens ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );
      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );
      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkSensitivityErrorCode( testName.getMethodName(), "BLSEN-00250" );

   }


   /**
    * This test is to verify valid error code BLSEN-00310:ASSMBL_CD must refer to an assembly in the
    * EQP_ASSMBL table.
    *
    *
    */

   @Test
   public void test_BLSEN_00310() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_assign_parts_to_stock table
      Map<String, String> lSens = new LinkedHashMap<>();

      lSens.put( "ASSMBL_CD", "'INVALID'" );
      lSens.put( "CONFIG_SLOT_CD", "\'" + iCONFIG_SLOT_CD_ACFT + "\'" );
      lSens.put( "SENSITIVITY_CD", "\'" + iSENSITIVITY_CD + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_SENSITIVITY_SYS, lSens ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkSensitivityErrorCode( testName.getMethodName(), "BLSEN-00310" );

   }


   /**
    * This test is to verify valid error code BLSEN-00320:CONFIG_SLOT_CD must refer to a config slot
    * in the EQP_ASSMBL_BOM table.
    *
    *
    */

   @Test
   public void test_BLSEN_00320() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_assign_parts_to_stock table
      Map<String, String> lSens = new LinkedHashMap<>();

      lSens.put( "ASSMBL_CD", "\'" + iAssmblCD_ACFT + "\'" );
      lSens.put( "CONFIG_SLOT_CD", "'INVALID'" );
      lSens.put( "SENSITIVITY_CD", "\'" + iSENSITIVITY_CD + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_SENSITIVITY_SYS, lSens ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkSensitivityErrorCode( testName.getMethodName(), "BLSEN-00320" );

   }


   /**
    * This test is to verify valid error code BLSEN-00330:CONFIG_SLOT_CD must refer to a config slot
    * with a BOM_CLASS_CD of SYS in the EQP_ASSMBL_BOM table. TRK slot.
    *
    */

   @Test
   public void test_BLSEN_00330_1() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_assign_parts_to_stock table
      Map<String, String> lSens = new LinkedHashMap<>();

      lSens.put( "ASSMBL_CD", "\'" + iAssmblCD_ACFT + "\'" );
      lSens.put( "CONFIG_SLOT_CD", "'ACFT-SYS-1-1-TRK-BATCH-PARENT'" );
      lSens.put( "SENSITIVITY_CD", "\'" + iSENSITIVITY_CD + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_SENSITIVITY_SYS, lSens ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkSensitivityErrorCode( testName.getMethodName(), "BLSEN-00330" );

   }


   /**
    * This test is to verify valid error code BLSEN-00330:CONFIG_SLOT_CD must refer to a config slot
    * with a BOM_CLASS_CD of SYS in the EQP_ASSMBL_BOM table. root slot.
    *
    */

   @Test
   public void test_BLSEN_00330_2() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_assign_parts_to_stock table
      Map<String, String> lSens = new LinkedHashMap<>();

      lSens.put( "ASSMBL_CD", "\'" + iAssmblCD_ACFT + "\'" );
      lSens.put( "CONFIG_SLOT_CD", "'ACFT_CD1'" );
      lSens.put( "SENSITIVITY_CD", "\'" + iSENSITIVITY_CD + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_SENSITIVITY_SYS, lSens ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkSensitivityErrorCode( testName.getMethodName(), "BLSEN-00330" );

   }


   /**
    * This test is to verify valid error code BLSEN-00330:CONFIG_SLOT_CD must refer to a config slot
    * with a BOM_CLASS_CD of SYS in the EQP_ASSMBL_BOM table. subassy.
    *
    */

   @Test
   public void test_BLSEN_00330_3() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_assign_parts_to_stock table
      Map<String, String> lSens = new LinkedHashMap<>();

      lSens.put( "ASSMBL_CD", "\'" + iAssmblCD_ACFT + "\'" );
      lSens.put( "CONFIG_SLOT_CD", "'APU-ASSY'" );
      lSens.put( "SENSITIVITY_CD", "\'" + iSENSITIVITY_CD + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_SENSITIVITY_SYS, lSens ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkSensitivityErrorCode( testName.getMethodName(), "BLSEN-00330" );

   }


   /**
    * This test is to verify valid error code BLSEN-00340:ASSMBL_CD/CONFIG_SLOT_CD/SENSITIVITY_CD
    * cannot exist more than once in the BL_SENSITIVITY_SYS table.
    *
    *
    */

   @Test
   public void test_BLSEN_00340() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_assign_parts_to_stock table
      Map<String, String> lSens = new LinkedHashMap<>();

      lSens.put( "ASSMBL_CD", "\'" + iAssmblCD_ACFT + "\'" );
      lSens.put( "CONFIG_SLOT_CD", "\'" + iCONFIG_SLOT_CD_ACFT + "\'" );
      lSens.put( "SENSITIVITY_CD", "'INVALID'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_SENSITIVITY_SYS, lSens ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkSensitivityErrorCode( testName.getMethodName(), "BLSEN-00340" );

   }


   /**
    * This test is to verify valid error code BLSEN-01010:ASSEMBL_CD must be provided in the
    * BL_SENSITIVITY_PG table.
    *
    *
    */
   @Test
   public void test_BLSEN_01010() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_assign_parts_to_stock table
      Map<String, String> lSens = new LinkedHashMap<>();

      // lSens.put( "ASSMBL_CD", "\'" + iAssmblCD_ACFT + "\'" );
      lSens.put( "PART_GROUP_CD", "\'" + iPART_GROUP_CD_TRK + "\'" );
      lSens.put( "SENSITIVITY_CD", "\'" + iSENSITIVITY_CD + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_SENSITIVITY_PG, lSens ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkSensitivityErrorCode( testName.getMethodName(), "BLSEN-01010" );

   }


   /**
    * This test is to verify valid error code BLSEN-01020:PART_GROUP_CD must be provided in the
    * BL_SENSITIVITY_PG table
    *
    *
    */
   @Test
   public void test_BLSEN_01020() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_assign_parts_to_stock table
      Map<String, String> lSens = new LinkedHashMap<>();

      lSens.put( "ASSMBL_CD", "\'" + iAssmblCD_ACFT + "\'" );
      // lSens.put( "PART_GROUP_CD", "\'" + iPART_GROUP_CD_TRK + "\'" );
      lSens.put( "SENSITIVITY_CD", "\'" + iSENSITIVITY_CD + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_SENSITIVITY_PG, lSens ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkSensitivityErrorCode( testName.getMethodName(), "BLSEN-01020" );

   }


   /**
    * This test is to verify valid error code BLSEN-01030:SENSITIVITY_CD must be provided in the
    * BL_SENSITIVITY_PG table.
    *
    *
    */
   @Test
   public void test_BLSEN_01030() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_assign_parts_to_stock table
      Map<String, String> lSens = new LinkedHashMap<>();

      lSens.put( "ASSMBL_CD", "\'" + iAssmblCD_ACFT + "\'" );
      lSens.put( "PART_GROUP_CD", "\'" + iPART_GROUP_CD_TRK + "\'" );
      // lSens.put( "SENSITIVITY_CD", "\'" + iSENSITIVITY_CD + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_SENSITIVITY_PG, lSens ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkSensitivityErrorCode( testName.getMethodName(), "BLSEN-01030" );

   }


   /**
    * This test is to verify valid error code BLSEN-01210:ASSMBL_CD/PART_GROUP_CD/SENSITIVITY_CD
    * cannot exist more than once in the BL_SENSITIVITY_PG table.
    *
    *
    */
   @Test
   public void test_BLSEN_01210() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_assign_parts_to_stock table
      Map<String, String> lSens = new LinkedHashMap<>();

      lSens.put( "ASSMBL_CD", "\'" + iAssmblCD_ACFT + "\'" );
      lSens.put( "PART_GROUP_CD", "\'" + iPART_GROUP_CD_TRK + "\'" );
      lSens.put( "SENSITIVITY_CD", "\'" + iSENSITIVITY_CD + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_SENSITIVITY_PG, lSens ) );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_SENSITIVITY_PG, lSens ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkSensitivityErrorCode( testName.getMethodName(), "BLSEN-01210" );

   }


   /**
    * This test is to verify valid error code BLSEN-01220:ASSMBL_CD/PART_GROUP_CD/SENSITIVITY_CD
    * cannot exist more than once in the BL_SENSITIVITY_PG table.
    *
    *
    */
   @Test
   public void test_BLSEN_01220() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_assign_parts_to_stock table
      Map<String, String> lSens = new LinkedHashMap<>();

      lSens.put( "ASSMBL_CD", "\'" + iAssmblCD_ACFT + "\'" );
      lSens.put( "PART_GROUP_CD", "'ACFT-SYS-10-2-DOUBLETRK'" );
      lSens.put( "SENSITIVITY_CD", "\'" + iSENSITIVITY_CD + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_SENSITIVITY_PG, lSens ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkSensitivityErrorCode( testName.getMethodName(), "BLSEN-01220" );

   }


   /**
    * This test is to verify valid error code BLSEN-01230:ASSMBL_CD references multiple assemblies
    * in the EQP_ASSMBL table
    *
    *
    */
   @Test
   public void test_BLSEN_01230() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_assign_parts_to_stock table
      Map<String, String> lSens = new LinkedHashMap<>();

      lSens.put( "ASSMBL_CD", "'ACFT_T2'" );
      lSens.put( "PART_GROUP_CD", "\'" + iPART_GROUP_CD_TRK + "\'" );
      lSens.put( "SENSITIVITY_CD", "\'" + iSENSITIVITY_CD + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_SENSITIVITY_PG, lSens ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkSensitivityErrorCode( testName.getMethodName(), "BLSEN-01230" );

   }


   /**
    * This test is to verify valid error code BLSEN-01240:ENSITIVITY_CD references multiple
    * sensitivities in the REF_SENSITIVITY table.
    *
    *
    */
   @Test
   public void test_BLSEN_01240() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_assign_parts_to_stock table
      Map<String, String> lSens = new LinkedHashMap<>();

      lSens.put( "ASSMBL_CD", "\'" + iAssmblCD_ACFT + "\'" );
      lSens.put( "PART_GROUP_CD", "\'" + iPART_GROUP_CD_TRK + "\'" );
      lSens.put( "SENSITIVITY_CD", "'AT_SN3'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_SENSITIVITY_PG, lSens ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkSensitivityErrorCode( testName.getMethodName(), "BLSEN-01240" );

   }


   /**
    * This test is to verify valid error code BLSEN-01250:ASSMBL_CD/PART_GROUP_CD/SENSITIVITY_CD
    * already exists in the EQP_BOM_PART_SENS table.
    *
    *
    */
   @Test
   public void test_BLSEN_01250() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_assign_parts_to_stock table
      Map<String, String> lSens = new LinkedHashMap<>();

      lSens.put( "ASSMBL_CD", "\'" + iAssmblCD_ACFT + "\'" );
      lSens.put( "PART_GROUP_CD", "\'" + iPART_GROUP_CD_TRK + "\'" );
      lSens.put( "SENSITIVITY_CD", "\'" + iSENSITIVITY_CD + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_SENSITIVITY_PG, lSens ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

      // run import
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkSensitivityErrorCode( testName.getMethodName(), "BLSEN-01250" );

   }


   /**
    * This test is to verify valid error code BLSEN-01310:ASSMBL_CD must refer to an assembly in the
    * EQP_ASSMBL table.
    *
    *
    */
   @Test
   public void test_BLSEN_01310() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_assign_parts_to_stock table
      Map<String, String> lSens = new LinkedHashMap<>();

      lSens.put( "ASSMBL_CD", "'INVALID'" );
      lSens.put( "PART_GROUP_CD", "\'" + iPART_GROUP_CD_TRK + "\'" );
      lSens.put( "SENSITIVITY_CD", "\'" + iSENSITIVITY_CD + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_SENSITIVITY_PG, lSens ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkSensitivityErrorCode( testName.getMethodName(), "BLSEN-01310" );

   }


   /**
    * This test is to verify valid error code BLSEN-01320:PART_GROUP_CD must refer to a part group
    * in the EQP_BOM_PART table.
    *
    *
    */
   @Test
   public void test_BLSEN_01320() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_assign_parts_to_stock table
      Map<String, String> lSens = new LinkedHashMap<>();

      lSens.put( "ASSMBL_CD", "\'" + iAssmblCD_ACFT + "\'" );
      lSens.put( "PART_GROUP_CD", "'INVALID'" );
      lSens.put( "SENSITIVITY_CD", "\'" + iSENSITIVITY_CD + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_SENSITIVITY_PG, lSens ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkSensitivityErrorCode( testName.getMethodName(), "BLSEN-01320" );

   }


   /**
    * This test is to verify valid error code BLSEN-01330:PART_GROUP_CD must refer to a part group
    * with an INV_CLASS_CD of TRK, SER, BATCH OR KIT in the EQP_BOM_PART table.
    *
    *
    */
   @Test
   public void test_BLSEN_01330() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_assign_parts_to_stock table
      Map<String, String> lSens = new LinkedHashMap<>();

      lSens.put( "ASSMBL_CD", "\'" + iAssmblCD_ACFT + "\'" );
      // lSens.put( "PART_GROUP_CD", "'APU_ASSY'" );
      lSens.put( "PART_GROUP_CD", "'ACFT_CD1'" );
      lSens.put( "SENSITIVITY_CD", "\'" + iSENSITIVITY_CD + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_SENSITIVITY_PG, lSens ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkSensitivityErrorCode( testName.getMethodName(), "BLSEN-01330" );

   }


   /**
    * This test is to verify valid error code BLSEN-01340:SENSITIVITY_CD must refer to a sensitivity
    * in the REF_SENSITIVITY table.
    *
    *
    */
   @Test
   public void test_BLSEN_01340() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_assign_parts_to_stock table
      Map<String, String> lSens = new LinkedHashMap<>();

      lSens.put( "ASSMBL_CD", "\'" + iAssmblCD_ACFT + "\'" );
      lSens.put( "PART_GROUP_CD", "\'" + iPART_GROUP_CD_TRK + "\'" );
      lSens.put( "SENSITIVITY_CD", "'INVALID'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_SENSITIVITY_PG, lSens ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      checkSensitivityErrorCode( testName.getMethodName(), "BLSEN-01340" );

   }


   // ==========================================================================================================
   /**
    * This function is called by after test method to restore data which has been modified by tests.
    *
    *
    */
   public void RestoreData() {

      // first sensitivity
      String lStrDelete = "delete from " + TableUtil.EQP_ASSMBL_BOM_SENS + " where SENSITIVITY_CD='"
            + iSENSITIVITY_CD + "'";
      executeSQL( lStrDelete );

      lStrDelete = "delete from " + TableUtil.EQP_BOM_PART_SENS + " where SENSITIVITY_CD='"
            + iSENSITIVITY_CD + "'";
      executeSQL( lStrDelete );

      lStrDelete = "delete from " + TableUtil.EQP_ASSMBL_SENS + " where SENSITIVITY_CD='"
            + iSENSITIVITY_CD + "'";
      executeSQL( lStrDelete );

      // second sensitivity
      lStrDelete = "delete from " + TableUtil.EQP_ASSMBL_BOM_SENS + " where SENSITIVITY_CD='"
            + iSENSITIVITY_CD_2 + "'";
      executeSQL( lStrDelete );

      lStrDelete = "delete from " + TableUtil.EQP_BOM_PART_SENS + " where SENSITIVITY_CD='"
            + iSENSITIVITY_CD_2 + "'";
      executeSQL( lStrDelete );

      lStrDelete = "delete from " + TableUtil.EQP_ASSMBL_SENS + " where SENSITIVITY_CD='"
            + iSENSITIVITY_CD_2 + "'";
      executeSQL( lStrDelete );
   }

}
