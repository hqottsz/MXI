package com.mxi.mx.core.maint.plan.actualsloader.inventory.imports;

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

import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;
import com.mxi.mx.util.StoreProcedureRunner;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.TestConstants;
import com.mxi.mx.util.WhereClause;


/**
 * This test suite contains test cases to import aircraft with applicabilioty
 *
 * @author Alicia Qian
 */
public class ImportApplicableTest extends AbstractImportInventory {

   @Rule
   public TestName testName = new TestName();

   public String iSERIAL_NO_OEM_ACFT = "ACFT00001";
   public String iSERIAL_NO_OEM_ENG = "ENG00001";
   public String iSERIAL_NO_OEM_ENG_TRK = "TRK00001";

   public String iPART_NO_OEM_ACFT = "ACFT_ASSY_PN8";
   public String iPART_NO_OEM_ENG = "ENG_ASSY_PN8";
   public String iPART_NO_OEM_TRK = "E0000100";
   public String iPART_NO_OEM_TRK_2 = "E0000200";
   public String iINV_NO_SDESC_TRK = "PN: E0000100";
   public String iINV_NO_SDESC_TRK_2 = "PN: E0000200";

   public String iINV_CLASS_CD_ACFT = "ACFT";
   public String iINV_CLASS_CD_ENG = "ASSY";
   public String iMANUFACT_CD_ACFT = "10001";
   public String iMANUFACT_CD_ENG = "ABC11";
   public String iMANUFACT_CD_TRK_1 = "ABC11";

   public String iASSMBL_CD_ENG = "ENG_CD9";

   public simpleIDs iINV_Ids = null;


   /**
    * Clean up after each individual test
    *
    * @throws Exception
    */
   @After
   @Override
   public void after() throws Exception {

      // clean up the event data
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
      clearActualsLoaderTables();

      if ( testName.getMethodName().contains( "_False_" ) ) {
         setParmFalse();
      }

   }


   /**
    * This is to validation function of testOPER_27190(); OPER-27190: Actuals loader does not use
    * the validation logic associated with config parm APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY . By
    * default: APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY=True ACFT and ENG have no applicability, but
    * TRK has applicability range=002-005.Expect result: TRK under ENG is created.
    *
    *
    *
    */
   @Test
   public void testOPER_27190_1_Validation() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // ACFT inventory
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();
      lMapInventory.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ACFT + "'" );
      lMapInventory.put( "PART_NO_OEM", "'" + iPART_NO_OEM_ACFT + "'" );
      lMapInventory.put( "INV_CLASS_CD", "'" + iINV_CLASS_CD_ACFT + "'" );
      lMapInventory.put( "MANUFACT_CD", "'" + iMANUFACT_CD_ACFT + "'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventory.put( "AC_REG_CD", "'AUTO-00001'" );
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "FORECAST_MODEL", "'Forecast1'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // ENG inventory
      lMapInventory.clear();
      lMapInventory.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ENG + "'" );
      lMapInventory.put( "PART_NO_OEM", "'" + iPART_NO_OEM_ENG + "'" );
      lMapInventory.put( "INV_CLASS_CD", "'" + iINV_CLASS_CD_ENG + "'" );
      lMapInventory.put( "MANUFACT_CD", "'" + iMANUFACT_CD_ENG + "'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      // lMapInventory.put( "APPL_EFF_CDT", "001" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // ENG attach to ACFT
      Map<String, String> lMapInventoryAttach = new LinkedHashMap<String, String>();
      lMapInventoryAttach.put( "PARENT_SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ACFT + "'" );
      lMapInventoryAttach.put( "PARENT_PART_NO_OEM", "'" + iPART_NO_OEM_ACFT + "'" );
      lMapInventoryAttach.put( "PARENT_MANUFACT_CD", "'" + iMANUFACT_CD_ACFT + "'" );

      lMapInventoryAttach.put( "ATTACH_SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ENG + "'" );
      lMapInventoryAttach.put( "ATTACH_PART_NO_OEM", "'" + iPART_NO_OEM_ENG + "'" );
      lMapInventoryAttach.put( "ATTACH_MANUFACT_CD", "'" + iMANUFACT_CD_ENG + "'" );
      lMapInventoryAttach.put( "BOM_PART_CD", "'ENG-ASSY'" );
      lMapInventoryAttach.put( "EQP_POS_CD", "'1'" );
      lMapInventoryAttach.put( "INSTALL_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      // lMapInventory.put( "APPL_EFF_CDT", "001" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_ATTACH, lMapInventoryAttach ) );

      // TRK inventory
      Map<String, String> lMapInventorySubTrk = new LinkedHashMap<String, String>();
      lMapInventorySubTrk.put( "PARENT_SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ENG + "'" );
      lMapInventorySubTrk.put( "PARENT_PART_NO_OEM", "'" + iPART_NO_OEM_ENG + "'" );
      lMapInventorySubTrk.put( "PARENT_MANUFACT_CD", "'" + iMANUFACT_CD_ENG + "'" );

      lMapInventorySubTrk.put( "BOM_PART_CD", "'ENG-SYS-1-TRK'" );
      lMapInventorySubTrk.put( "EQP_POS_CD", "'1'" );
      lMapInventorySubTrk.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ENG_TRK + "'" );
      lMapInventorySubTrk.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lMapInventorySubTrk.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK_1 + "'" );
      lMapInventorySubTrk.put( "INV_CLASS_CD", "'TRK'" );

      // insert TRK map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventorySubTrk ) );

      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "FULL", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      checkInventoryNoWarnings( "PASS" );

   }


   /**
    * This is to validation function of testOPER_27190(); OPER-27190: Actuals loader does not use
    * the validation logic associated with config parm APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY . By
    * default: APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY=True ACFT and ENG have no applicability, but
    * TRK has applicability range=002-005. Expect result: TRK under ENG is created.
    *
    *
    *
    */
   @Test
   public void testOPER_27190_1_IMPORT() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOPER_27190_1_Validation();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), false, true, "FULL", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // get inv IDs
      iINV_Ids = getInvIds( iSERIAL_NO_OEM_ACFT );

      // Verify TRK under ENG is created
      simpleIDs lPNIDs = getPartNoIds( iPART_NO_OEM_TRK, iMANUFACT_CD_TRK_1 );
      verifyINVINV( iINV_Ids, "TRK", iSERIAL_NO_OEM_ENG_TRK, iASSMBL_CD_ENG, iINV_NO_SDESC_TRK,
            lPNIDs );

   }


   /**
    * This is to validation function of testOPER_27190(); OPER-27190: Actuals loader does not use
    * the validation logic associated with config parm APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY . By
    * default: APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY=False ACFT and ENG have no applicability,
    * but TRK has applicability range=002-005.Expect result: TRK under ENG is created.
    *
    *
    *
    */
   @Test
   public void testOPER_27190_2_False_Validation() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testOPER_27190_1_Validation();
   }


   /**
    * This is to validation function of testOPER_27190(); OPER-27190: Actuals loader does not use
    * the validation logic associated with config parm APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY . By
    * default: APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY=False ACFT and ENG have no applicability,
    * but TRK has applicability range=002-005.Expect result: TRK under ENG is created.
    *
    *
    *
    */
   @Test
   public void testOPER_27190_2_False_IMPORT() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testOPER_27190_1_IMPORT();

   }


   /**
    * This is to validation function of testOPER_27190(); OPER-27190: Actuals loader does not use
    * the validation logic associated with config parm APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY . By
    * default: APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY=True ACFT has no applicability, ENG has
    * applicability=001, but TRK has applicability range=002-005.Expect result: error code
    * (AML-10429) is generated.
    *
    *
    *
    */
   @Test
   public void testOPER_27190_3_Validation() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // ACFT inventory
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();
      lMapInventory.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ACFT + "'" );
      lMapInventory.put( "PART_NO_OEM", "'" + iPART_NO_OEM_ACFT + "'" );
      lMapInventory.put( "INV_CLASS_CD", "'" + iINV_CLASS_CD_ACFT + "'" );
      lMapInventory.put( "MANUFACT_CD", "'" + iMANUFACT_CD_ACFT + "'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventory.put( "AC_REG_CD", "'AUTO-00001'" );
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "FORECAST_MODEL", "'Forecast1'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // ENG inventory
      lMapInventory.clear();
      lMapInventory.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ENG + "'" );
      lMapInventory.put( "PART_NO_OEM", "'" + iPART_NO_OEM_ENG + "'" );
      lMapInventory.put( "INV_CLASS_CD", "'" + iINV_CLASS_CD_ENG + "'" );
      lMapInventory.put( "MANUFACT_CD", "'" + iMANUFACT_CD_ENG + "'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "APPL_EFF_CD", "'001'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // ENG attach to ACFT
      Map<String, String> lMapInventoryAttach = new LinkedHashMap<String, String>();
      lMapInventoryAttach.put( "PARENT_SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ACFT + "'" );
      lMapInventoryAttach.put( "PARENT_PART_NO_OEM", "'" + iPART_NO_OEM_ACFT + "'" );
      lMapInventoryAttach.put( "PARENT_MANUFACT_CD", "'" + iMANUFACT_CD_ACFT + "'" );

      lMapInventoryAttach.put( "ATTACH_SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ENG + "'" );
      lMapInventoryAttach.put( "ATTACH_PART_NO_OEM", "'" + iPART_NO_OEM_ENG + "'" );
      lMapInventoryAttach.put( "ATTACH_MANUFACT_CD", "'" + iMANUFACT_CD_ENG + "'" );
      lMapInventoryAttach.put( "BOM_PART_CD", "'ENG-ASSY'" );
      lMapInventoryAttach.put( "EQP_POS_CD", "'1'" );
      lMapInventoryAttach.put( "INSTALL_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_ATTACH, lMapInventoryAttach ) );

      // TRK inventory
      Map<String, String> lMapInventorySubTrk = new LinkedHashMap<String, String>();
      lMapInventorySubTrk.put( "PARENT_SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ENG + "'" );
      lMapInventorySubTrk.put( "PARENT_PART_NO_OEM", "'" + iPART_NO_OEM_ENG + "'" );
      lMapInventorySubTrk.put( "PARENT_MANUFACT_CD", "'" + iMANUFACT_CD_ENG + "'" );

      lMapInventorySubTrk.put( "BOM_PART_CD", "'ENG-SYS-1-TRK'" );
      lMapInventorySubTrk.put( "EQP_POS_CD", "'1'" );
      lMapInventorySubTrk.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ENG_TRK + "'" );
      lMapInventorySubTrk.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lMapInventorySubTrk.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK_1 + "'" );
      lMapInventorySubTrk.put( "INV_CLASS_CD", "'TRK'" );

      // insert TRK map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventorySubTrk ) );

      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "FULL", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // check error code
      checkInventoryErrorCode( testName.getMethodName(), "AML-10429" );

   }


   /**
    * This is to validation function of testOPER_27190(); OPER-27190: Actuals loader does not use
    * the validation logic associated with config parm APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY . By
    * default: APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY=False ACFT has no applicability, ENG has
    * applicability=001, but TRK has applicability range=002-005.Expect result:TRK should pass
    * validation and TRK is created under ENG.
    *
    *
    *
    */
   @Test
   public void testOPER_27190_4_False_Validation() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // ACFT inventory
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();
      lMapInventory.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ACFT + "'" );
      lMapInventory.put( "PART_NO_OEM", "'" + iPART_NO_OEM_ACFT + "'" );
      lMapInventory.put( "INV_CLASS_CD", "'" + iINV_CLASS_CD_ACFT + "'" );
      lMapInventory.put( "MANUFACT_CD", "'" + iMANUFACT_CD_ACFT + "'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventory.put( "AC_REG_CD", "'AUTO-00001'" );
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "FORECAST_MODEL", "'Forecast1'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // ENG inventory
      lMapInventory.clear();
      lMapInventory.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ENG + "'" );
      lMapInventory.put( "PART_NO_OEM", "'" + iPART_NO_OEM_ENG + "'" );
      lMapInventory.put( "INV_CLASS_CD", "'" + iINV_CLASS_CD_ENG + "'" );
      lMapInventory.put( "MANUFACT_CD", "'" + iMANUFACT_CD_ENG + "'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'INSRV'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "APPL_EFF_CD", "'001'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // ENG attach to ACFT
      Map<String, String> lMapInventoryAttach = new LinkedHashMap<String, String>();
      lMapInventoryAttach.put( "PARENT_SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ACFT + "'" );
      lMapInventoryAttach.put( "PARENT_PART_NO_OEM", "'" + iPART_NO_OEM_ACFT + "'" );
      lMapInventoryAttach.put( "PARENT_MANUFACT_CD", "'" + iMANUFACT_CD_ACFT + "'" );

      lMapInventoryAttach.put( "ATTACH_SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ENG + "'" );
      lMapInventoryAttach.put( "ATTACH_PART_NO_OEM", "'" + iPART_NO_OEM_ENG + "'" );
      lMapInventoryAttach.put( "ATTACH_MANUFACT_CD", "'" + iMANUFACT_CD_ENG + "'" );
      lMapInventoryAttach.put( "BOM_PART_CD", "'ENG-ASSY'" );
      lMapInventoryAttach.put( "EQP_POS_CD", "'1'" );
      lMapInventoryAttach.put( "INSTALL_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_ATTACH, lMapInventoryAttach ) );

      // TRK inventory
      Map<String, String> lMapInventorySubTrk = new LinkedHashMap<String, String>();
      lMapInventorySubTrk.put( "PARENT_SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ENG + "'" );
      lMapInventorySubTrk.put( "PARENT_PART_NO_OEM", "'" + iPART_NO_OEM_ENG + "'" );
      lMapInventorySubTrk.put( "PARENT_MANUFACT_CD", "'" + iMANUFACT_CD_ENG + "'" );

      lMapInventorySubTrk.put( "BOM_PART_CD", "'ENG-SYS-1-TRK'" );
      lMapInventorySubTrk.put( "EQP_POS_CD", "'1'" );
      lMapInventorySubTrk.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ENG_TRK + "'" );
      lMapInventorySubTrk.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lMapInventorySubTrk.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK_1 + "'" );
      lMapInventorySubTrk.put( "INV_CLASS_CD", "'TRK'" );

      // insert TRK map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventorySubTrk ) );

      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "FULL", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // check error code
      // checkInventoryErrorCode( testName.getMethodName(), "AML-10429" );
      checkInventoryNoWarnings( "PASS" );

   }


   /**
    * This is to validation function of testOPER_27190(); OPER-27190: Actuals loader does not use
    * the validation logic associated with config parm APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY . By
    * default: APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY=False ACFT has no applicability, ENG has
    * applicability=001, but TRK has applicability range=002-005.Expect result:TRK should pass
    * validation and TRK is created under ENG.
    *
    *
    *
    */
   @Test
   public void testOPER_27190_4_False_PN_Validation() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // ACFT inventory
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();
      lMapInventory.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ACFT + "'" );
      lMapInventory.put( "PART_NO_OEM", "'" + iPART_NO_OEM_ACFT + "'" );
      lMapInventory.put( "INV_CLASS_CD", "'" + iINV_CLASS_CD_ACFT + "'" );
      lMapInventory.put( "MANUFACT_CD", "'" + iMANUFACT_CD_ACFT + "'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventory.put( "AC_REG_CD", "'AUTO-00001'" );
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "FORECAST_MODEL", "'Forecast1'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // ENG inventory
      lMapInventory.clear();
      lMapInventory.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ENG + "'" );
      lMapInventory.put( "PART_NO_OEM", "'" + iPART_NO_OEM_ENG + "'" );
      lMapInventory.put( "INV_CLASS_CD", "'" + iINV_CLASS_CD_ENG + "'" );
      lMapInventory.put( "MANUFACT_CD", "'" + iMANUFACT_CD_ENG + "'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'INSRV'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "APPL_EFF_CD", "'001'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // ENG attach to ACFT
      Map<String, String> lMapInventoryAttach = new LinkedHashMap<String, String>();
      lMapInventoryAttach.put( "PARENT_SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ACFT + "'" );
      lMapInventoryAttach.put( "PARENT_PART_NO_OEM", "'" + iPART_NO_OEM_ACFT + "'" );
      lMapInventoryAttach.put( "PARENT_MANUFACT_CD", "'" + iMANUFACT_CD_ACFT + "'" );

      lMapInventoryAttach.put( "ATTACH_SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ENG + "'" );
      lMapInventoryAttach.put( "ATTACH_PART_NO_OEM", "'" + iPART_NO_OEM_ENG + "'" );
      lMapInventoryAttach.put( "ATTACH_MANUFACT_CD", "'" + iMANUFACT_CD_ENG + "'" );
      lMapInventoryAttach.put( "BOM_PART_CD", "'ENG-ASSY'" );
      lMapInventoryAttach.put( "EQP_POS_CD", "'1'" );
      lMapInventoryAttach.put( "INSTALL_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_ATTACH, lMapInventoryAttach ) );

      // TRK inventory
      Map<String, String> lMapInventorySubTrk = new LinkedHashMap<String, String>();
      lMapInventorySubTrk.put( "PARENT_SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ENG + "'" );
      lMapInventorySubTrk.put( "PARENT_PART_NO_OEM", "'" + iPART_NO_OEM_ENG + "'" );
      lMapInventorySubTrk.put( "PARENT_MANUFACT_CD", "'" + iMANUFACT_CD_ENG + "'" );

      lMapInventorySubTrk.put( "BOM_PART_CD", "'ENG-SYS-1-TRK2'" );
      lMapInventorySubTrk.put( "EQP_POS_CD", "'1'" );
      lMapInventorySubTrk.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ENG_TRK + "'" );
      lMapInventorySubTrk.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK_2 + "'" );
      lMapInventorySubTrk.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK_1 + "'" );
      lMapInventorySubTrk.put( "INV_CLASS_CD", "'TRK'" );

      // insert TRK map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventorySubTrk ) );

      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "FULL", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // check error code
      // checkInventoryErrorCode( testName.getMethodName(), "AML-10429" );
      checkInventoryNoWarnings( "PASS" );

   }


   /**
    * This is to validation function of testOPER_27190(); OPER-27190: Actuals loader does not use
    * the validation logic associated with config parm APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY . By
    * default: APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY=False ACFT has no applicability, ENG has
    * applicability=001, but TRK has applicability range=002-005.Expect result:TRK should pass
    * validation and TRK is created under ENG.
    *
    *
    *
    */

   @Test
   public void testOPER_27190_4_False_IMPORT() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOPER_27190_4_False_Validation();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), false, true, "FULL", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // get inv IDs
      iINV_Ids = getInvIds( iSERIAL_NO_OEM_ACFT );

      // Verify TRK under ENG is created
      simpleIDs lPNIDs = getPartNoIds( iPART_NO_OEM_TRK, iMANUFACT_CD_TRK_1 );
      verifyINVINV( iINV_Ids, "TRK", iSERIAL_NO_OEM_ENG_TRK, iASSMBL_CD_ENG, iINV_NO_SDESC_TRK,
            lPNIDs );

   }


   /**
    * This is to validation function of testOPER_27190(); OPER-27190: Actuals loader does not use
    * the validation logic associated with config parm APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY . By
    * default: APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY=False ACFT has no applicability, ENG has
    * applicability=001, but TRK has applicability range=002-005.Expect result:TRK should pass
    * validation and TRK is created under ENG.
    *
    *
    *
    */

   @Test
   public void testOPER_27190_4_False_PN_IMPORT() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOPER_27190_4_False_PN_Validation();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), false, true, "FULL", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // get inv IDs
      iINV_Ids = getInvIds( iSERIAL_NO_OEM_ACFT );

      // Verify TRK under ENG is created
      simpleIDs lPNIDs = getPartNoIds( iPART_NO_OEM_TRK_2, iMANUFACT_CD_TRK_1 );
      verifyINVINV( iINV_Ids, "TRK", iSERIAL_NO_OEM_ENG_TRK, iASSMBL_CD_ENG, iINV_NO_SDESC_TRK_2,
            lPNIDs );

   }


   /**
    * This is to validation function of testOPER_27190(); OPER-27190: Actuals loader does not use
    * the validation logic associated with config parm APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY . By
    * default: APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY=True ACFT has applicability=001, ENG has
    * applicability=004, but TRK has applicability range=002-005.Expect result: TRK would be
    * created.
    *
    *
    *
    *
    */
   @Test
   public void testOPER_27190_5_Validation() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // ACFT inventory
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();
      lMapInventory.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ACFT + "'" );
      lMapInventory.put( "PART_NO_OEM", "'" + iPART_NO_OEM_ACFT + "'" );
      lMapInventory.put( "INV_CLASS_CD", "'" + iINV_CLASS_CD_ACFT + "'" );
      lMapInventory.put( "MANUFACT_CD", "'" + iMANUFACT_CD_ACFT + "'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventory.put( "AC_REG_CD", "'AUTO-00001'" );
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "FORECAST_MODEL", "'Forecast1'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "APPL_EFF_CD", "'001'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // ENG inventory
      lMapInventory.clear();
      lMapInventory.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ENG + "'" );
      lMapInventory.put( "PART_NO_OEM", "'" + iPART_NO_OEM_ENG + "'" );
      lMapInventory.put( "INV_CLASS_CD", "'" + iINV_CLASS_CD_ENG + "'" );
      lMapInventory.put( "MANUFACT_CD", "'" + iMANUFACT_CD_ENG + "'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'INSRV'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "APPL_EFF_CD", "'004'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // ENG attach to ACFT
      Map<String, String> lMapInventoryAttach = new LinkedHashMap<String, String>();
      lMapInventoryAttach.put( "PARENT_SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ACFT + "'" );
      lMapInventoryAttach.put( "PARENT_PART_NO_OEM", "'" + iPART_NO_OEM_ACFT + "'" );
      lMapInventoryAttach.put( "PARENT_MANUFACT_CD", "'" + iMANUFACT_CD_ACFT + "'" );

      lMapInventoryAttach.put( "ATTACH_SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ENG + "'" );
      lMapInventoryAttach.put( "ATTACH_PART_NO_OEM", "'" + iPART_NO_OEM_ENG + "'" );
      lMapInventoryAttach.put( "ATTACH_MANUFACT_CD", "'" + iMANUFACT_CD_ENG + "'" );
      lMapInventoryAttach.put( "BOM_PART_CD", "'ENG-ASSY'" );
      lMapInventoryAttach.put( "EQP_POS_CD", "'1'" );
      lMapInventoryAttach.put( "INSTALL_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_ATTACH, lMapInventoryAttach ) );

      // TRK inventory
      Map<String, String> lMapInventorySubTrk = new LinkedHashMap<String, String>();
      lMapInventorySubTrk.put( "PARENT_SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ENG + "'" );
      lMapInventorySubTrk.put( "PARENT_PART_NO_OEM", "'" + iPART_NO_OEM_ENG + "'" );
      lMapInventorySubTrk.put( "PARENT_MANUFACT_CD", "'" + iMANUFACT_CD_ENG + "'" );

      lMapInventorySubTrk.put( "BOM_PART_CD", "'ENG-SYS-1-TRK'" );
      lMapInventorySubTrk.put( "EQP_POS_CD", "'1'" );
      lMapInventorySubTrk.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ENG_TRK + "'" );
      lMapInventorySubTrk.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lMapInventorySubTrk.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK_1 + "'" );
      lMapInventorySubTrk.put( "INV_CLASS_CD", "'TRK'" );

      // insert TRK map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventorySubTrk ) );

      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "FULL", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // check error code
      checkInventoryNoWarnings( "PASS" );

   }


   /**
    * This is to validation function of testOPER_27190(); OPER-27190: Actuals loader does not use
    * the validation logic associated with config parm APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY . By
    * default: APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY=True ACFT has applicability=001, ENG has
    * applicability=004, but TRK has applicability range=002-005.Expect result: TRK under ENG
    * inventory has been generated.
    *
    *
    *
    */

   @Test
   public void testOPER_27190_5_IMPORT() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testOPER_27190_5_Validation();

      // run import
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), false, true, "FULL", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // get inv IDs
      iINV_Ids = getInvIds( iSERIAL_NO_OEM_ACFT );

      // Verify TRK under ENG is created
      simpleIDs lPNIDs = getPartNoIds( iPART_NO_OEM_TRK, iMANUFACT_CD_TRK_1 );
      verifyINVINV( iINV_Ids, "TRK", iSERIAL_NO_OEM_ENG_TRK, iASSMBL_CD_ENG, iINV_NO_SDESC_TRK,
            lPNIDs );

   }


   /**
    * This is to validation function of testOPER_27190(); OPER-27190: Actuals loader does not use
    * the validation logic associated with config parm APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY . By
    * default: APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY=False ACFT has no applicability , ENG has
    * applicability=001, but TRK has applicability range=002-005.Expect result: error code
    * (AML-10429) is generated.
    *
    *
    *
    */
   @Test
   public void testOPER_27190_6_False_Validation() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // ACFT inventory
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();
      lMapInventory.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ACFT + "'" );
      lMapInventory.put( "PART_NO_OEM", "'" + iPART_NO_OEM_ACFT + "'" );
      lMapInventory.put( "INV_CLASS_CD", "'" + iINV_CLASS_CD_ACFT + "'" );
      lMapInventory.put( "MANUFACT_CD", "'" + iMANUFACT_CD_ACFT + "'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventory.put( "AC_REG_CD", "'AUTO-00001'" );
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "FORECAST_MODEL", "'Forecast1'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "APPL_EFF_CD", "'001'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // ENG inventory
      lMapInventory.clear();
      lMapInventory.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ENG + "'" );
      lMapInventory.put( "PART_NO_OEM", "'" + iPART_NO_OEM_ENG + "'" );
      lMapInventory.put( "INV_CLASS_CD", "'" + iINV_CLASS_CD_ENG + "'" );
      lMapInventory.put( "MANUFACT_CD", "'" + iMANUFACT_CD_ENG + "'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'INSRV'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "APPL_EFF_CD", "'004'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // ENG attach to ACFT
      Map<String, String> lMapInventoryAttach = new LinkedHashMap<String, String>();
      lMapInventoryAttach.put( "PARENT_SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ACFT + "'" );
      lMapInventoryAttach.put( "PARENT_PART_NO_OEM", "'" + iPART_NO_OEM_ACFT + "'" );
      lMapInventoryAttach.put( "PARENT_MANUFACT_CD", "'" + iMANUFACT_CD_ACFT + "'" );

      lMapInventoryAttach.put( "ATTACH_SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ENG + "'" );
      lMapInventoryAttach.put( "ATTACH_PART_NO_OEM", "'" + iPART_NO_OEM_ENG + "'" );
      lMapInventoryAttach.put( "ATTACH_MANUFACT_CD", "'" + iMANUFACT_CD_ENG + "'" );
      lMapInventoryAttach.put( "BOM_PART_CD", "'ENG-ASSY'" );
      lMapInventoryAttach.put( "EQP_POS_CD", "'1'" );
      lMapInventoryAttach.put( "INSTALL_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_ATTACH, lMapInventoryAttach ) );

      // TRK inventory
      Map<String, String> lMapInventorySubTrk = new LinkedHashMap<String, String>();
      lMapInventorySubTrk.put( "PARENT_SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ENG + "'" );
      lMapInventorySubTrk.put( "PARENT_PART_NO_OEM", "'" + iPART_NO_OEM_ENG + "'" );
      lMapInventorySubTrk.put( "PARENT_MANUFACT_CD", "'" + iMANUFACT_CD_ENG + "'" );

      lMapInventorySubTrk.put( "BOM_PART_CD", "'ENG-SYS-1-TRK'" );
      lMapInventorySubTrk.put( "EQP_POS_CD", "'1'" );
      lMapInventorySubTrk.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ENG_TRK + "'" );
      lMapInventorySubTrk.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lMapInventorySubTrk.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK_1 + "'" );
      lMapInventorySubTrk.put( "INV_CLASS_CD", "'TRK'" );

      // insert TRK map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventorySubTrk ) );

      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "FULL", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      checkInventoryErrorCode( testName.getMethodName(), "AML-10429" );

   }


   /**
    * This is to validation function of testOPER_27190(); OPER-27190: Actuals loader does not use
    * the validation logic associated with config parm APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY . By
    * default: APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY=False ACFT has no applicability , ENG has
    * applicability=001, but TRK has applicability range=002-005.Expect result: error code
    * (AML-10429) is generated.
    *
    *
    *
    */
   @Test
   public void testOPER_27190_6_False_PN_Validation() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // ACFT inventory
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();
      lMapInventory.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ACFT + "'" );
      lMapInventory.put( "PART_NO_OEM", "'" + iPART_NO_OEM_ACFT + "'" );
      lMapInventory.put( "INV_CLASS_CD", "'" + iINV_CLASS_CD_ACFT + "'" );
      lMapInventory.put( "MANUFACT_CD", "'" + iMANUFACT_CD_ACFT + "'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventory.put( "AC_REG_CD", "'AUTO-00001'" );
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "FORECAST_MODEL", "'Forecast1'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "APPL_EFF_CD", "'001'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // ENG inventory
      lMapInventory.clear();
      lMapInventory.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ENG + "'" );
      lMapInventory.put( "PART_NO_OEM", "'" + iPART_NO_OEM_ENG + "'" );
      lMapInventory.put( "INV_CLASS_CD", "'" + iINV_CLASS_CD_ENG + "'" );
      lMapInventory.put( "MANUFACT_CD", "'" + iMANUFACT_CD_ENG + "'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'INSRV'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "APPL_EFF_CD", "'004'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // ENG attach to ACFT
      Map<String, String> lMapInventoryAttach = new LinkedHashMap<String, String>();
      lMapInventoryAttach.put( "PARENT_SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ACFT + "'" );
      lMapInventoryAttach.put( "PARENT_PART_NO_OEM", "'" + iPART_NO_OEM_ACFT + "'" );
      lMapInventoryAttach.put( "PARENT_MANUFACT_CD", "'" + iMANUFACT_CD_ACFT + "'" );

      lMapInventoryAttach.put( "ATTACH_SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ENG + "'" );
      lMapInventoryAttach.put( "ATTACH_PART_NO_OEM", "'" + iPART_NO_OEM_ENG + "'" );
      lMapInventoryAttach.put( "ATTACH_MANUFACT_CD", "'" + iMANUFACT_CD_ENG + "'" );
      lMapInventoryAttach.put( "BOM_PART_CD", "'ENG-ASSY'" );
      lMapInventoryAttach.put( "EQP_POS_CD", "'1'" );
      lMapInventoryAttach.put( "INSTALL_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_ATTACH, lMapInventoryAttach ) );

      // TRK inventory
      Map<String, String> lMapInventorySubTrk = new LinkedHashMap<String, String>();
      lMapInventorySubTrk.put( "PARENT_SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ENG + "'" );
      lMapInventorySubTrk.put( "PARENT_PART_NO_OEM", "'" + iPART_NO_OEM_ENG + "'" );
      lMapInventorySubTrk.put( "PARENT_MANUFACT_CD", "'" + iMANUFACT_CD_ENG + "'" );

      lMapInventorySubTrk.put( "BOM_PART_CD", "'ENG-SYS-1-TRK2'" );
      lMapInventorySubTrk.put( "EQP_POS_CD", "'1'" );
      lMapInventorySubTrk.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ENG_TRK + "'" );
      lMapInventorySubTrk.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK_2 + "'" );
      lMapInventorySubTrk.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK_1 + "'" );
      lMapInventorySubTrk.put( "INV_CLASS_CD", "'TRK'" );

      // insert TRK map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventorySubTrk ) );

      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "FULL", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      checkInventoryErrorCode( testName.getMethodName(), "AML-10430" );

   }


   /**
    * This is to validation function of testOPER_27190(); OPER-27190: Actuals loader does not use
    * the validation logic associated with config parm APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY . By
    * default: APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY=False ACFT has no applicability , ENG has
    * applicability=001, but TRK has applicability range=002-005.Expect result: error code
    * (AML-10429) is generated.
    *
    *
    *
    */
   @Test
   public void testOPER_27190_6_2_False_Validation() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // ACFT inventory
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();
      lMapInventory.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ACFT + "'" );
      lMapInventory.put( "PART_NO_OEM", "'" + iPART_NO_OEM_ACFT + "'" );
      lMapInventory.put( "INV_CLASS_CD", "'" + iINV_CLASS_CD_ACFT + "'" );
      lMapInventory.put( "MANUFACT_CD", "'" + iMANUFACT_CD_ACFT + "'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventory.put( "AC_REG_CD", "'AUTO-00001'" );
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "FORECAST_MODEL", "'Forecast1'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "APPL_EFF_CD", "'001'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // ENG inventory
      lMapInventory.clear();
      lMapInventory.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ENG + "'" );
      lMapInventory.put( "PART_NO_OEM", "'" + iPART_NO_OEM_ENG + "'" );
      lMapInventory.put( "INV_CLASS_CD", "'" + iINV_CLASS_CD_ENG + "'" );
      lMapInventory.put( "MANUFACT_CD", "'" + iMANUFACT_CD_ENG + "'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'INSRV'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "APPL_EFF_CD", null );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // ENG attach to ACFT
      Map<String, String> lMapInventoryAttach = new LinkedHashMap<String, String>();
      lMapInventoryAttach.put( "PARENT_SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ACFT + "'" );
      lMapInventoryAttach.put( "PARENT_PART_NO_OEM", "'" + iPART_NO_OEM_ACFT + "'" );
      lMapInventoryAttach.put( "PARENT_MANUFACT_CD", "'" + iMANUFACT_CD_ACFT + "'" );

      lMapInventoryAttach.put( "ATTACH_SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ENG + "'" );
      lMapInventoryAttach.put( "ATTACH_PART_NO_OEM", "'" + iPART_NO_OEM_ENG + "'" );
      lMapInventoryAttach.put( "ATTACH_MANUFACT_CD", "'" + iMANUFACT_CD_ENG + "'" );
      lMapInventoryAttach.put( "BOM_PART_CD", "'ENG-ASSY'" );
      lMapInventoryAttach.put( "EQP_POS_CD", "'1'" );
      lMapInventoryAttach.put( "INSTALL_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_ATTACH, lMapInventoryAttach ) );

      // TRK inventory
      Map<String, String> lMapInventorySubTrk = new LinkedHashMap<String, String>();

      // first record
      lMapInventorySubTrk.put( "PARENT_SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ENG + "'" );
      lMapInventorySubTrk.put( "PARENT_PART_NO_OEM", "'" + iPART_NO_OEM_ENG + "'" );
      lMapInventorySubTrk.put( "PARENT_MANUFACT_CD", "'" + iMANUFACT_CD_ENG + "'" );

      lMapInventorySubTrk.put( "BOM_PART_CD", "'ENG-SYS-1-TRK'" );
      lMapInventorySubTrk.put( "EQP_POS_CD", "'1'" );
      lMapInventorySubTrk.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ENG_TRK + "'" );
      lMapInventorySubTrk.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lMapInventorySubTrk.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK_1 + "'" );
      lMapInventorySubTrk.put( "INV_CLASS_CD", "'TRK'" );

      // insert TRK map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventorySubTrk ) );

      // //second record
      // lMapInventorySubTrk.clear();
      // lMapInventorySubTrk.put( "PARENT_SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ENG + "'" );
      // lMapInventorySubTrk.put( "PARENT_PART_NO_OEM", "'" + iPART_NO_OEM_ENG + "'" );
      // lMapInventorySubTrk.put( "PARENT_MANUFACT_CD", "'" + iMANUFACT_CD_ENG + "'" );
      //
      // lMapInventorySubTrk.put( "BOM_PART_CD", "'ENG-SYS-1-TRK'" );
      // lMapInventorySubTrk.put( "EQP_POS_CD", "'1'" );
      // lMapInventorySubTrk.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ENG_TRK + "'" );
      // lMapInventorySubTrk.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      // lMapInventorySubTrk.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK_1 + "'" );
      // lMapInventorySubTrk.put( "INV_CLASS_CD", "'TRK'" );
      //
      // // insert TRK map
      // runInsert(
      // TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventorySubTrk ) );

      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "FULL", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      checkInventoryErrorCode( testName.getMethodName(), "AML-10429" );

   }


   /**
    * This is to validation function of testOPER_27190(); OPER-27190: Actuals loader does not use
    * the validation logic associated with config parm APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY . By
    * default: APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY=False ACFT has applicability=004 , ENG has
    * applicability=004, but TRK has applicability range=002-005, but not in
    * C_RI_INVENTORY_SUB.Expected result:after import, TRK will be loaded as XXX.
    *
    *
    *
    */
   @Test
   public void testOPER_27190_7_False_Validation() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // ACFT inventory
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();
      lMapInventory.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ACFT + "'" );
      lMapInventory.put( "PART_NO_OEM", "'" + iPART_NO_OEM_ACFT + "'" );
      lMapInventory.put( "INV_CLASS_CD", "'" + iINV_CLASS_CD_ACFT + "'" );
      lMapInventory.put( "MANUFACT_CD", "'" + iMANUFACT_CD_ACFT + "'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventory.put( "AC_REG_CD", "'AUTO-00001'" );
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "FORECAST_MODEL", "'Forecast1'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "APPL_EFF_CD", "'004'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // ENG inventory
      lMapInventory.clear();
      lMapInventory.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ENG + "'" );
      lMapInventory.put( "PART_NO_OEM", "'" + iPART_NO_OEM_ENG + "'" );
      lMapInventory.put( "INV_CLASS_CD", "'" + iINV_CLASS_CD_ENG + "'" );
      lMapInventory.put( "MANUFACT_CD", "'" + iMANUFACT_CD_ENG + "'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'INSRV'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "APPL_EFF_CD", "'004'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // ENG attach to ACFT
      Map<String, String> lMapInventoryAttach = new LinkedHashMap<String, String>();
      lMapInventoryAttach.put( "PARENT_SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ACFT + "'" );
      lMapInventoryAttach.put( "PARENT_PART_NO_OEM", "'" + iPART_NO_OEM_ACFT + "'" );
      lMapInventoryAttach.put( "PARENT_MANUFACT_CD", "'" + iMANUFACT_CD_ACFT + "'" );

      lMapInventoryAttach.put( "ATTACH_SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ENG + "'" );
      lMapInventoryAttach.put( "ATTACH_PART_NO_OEM", "'" + iPART_NO_OEM_ENG + "'" );
      lMapInventoryAttach.put( "ATTACH_MANUFACT_CD", "'" + iMANUFACT_CD_ENG + "'" );
      lMapInventoryAttach.put( "BOM_PART_CD", "'ENG-ASSY'" );
      lMapInventoryAttach.put( "EQP_POS_CD", "'1'" );
      lMapInventoryAttach.put( "INSTALL_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_ATTACH, lMapInventoryAttach ) );

      // // TRK inventory
      // Map<String, String> lMapInventorySubTrk = new LinkedHashMap<String, String>();
      // lMapInventorySubTrk.put( "PARENT_SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ENG + "'" );
      // lMapInventorySubTrk.put( "PARENT_PART_NO_OEM", "'" + iPART_NO_OEM_ENG + "'" );
      // lMapInventorySubTrk.put( "PARENT_MANUFACT_CD", "'" + iMANUFACT_CD_ENG + "'" );
      //
      // lMapInventorySubTrk.put( "BOM_PART_CD", "'ENG-SYS-1-TRK'" );
      // lMapInventorySubTrk.put( "EQP_POS_CD", "'1'" );
      // lMapInventorySubTrk.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ENG_TRK + "'" );
      // lMapInventorySubTrk.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      // lMapInventorySubTrk.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK_1 + "'" );
      // lMapInventorySubTrk.put( "INV_CLASS_CD", "'TRK'" );

      // // insert TRK map
      // runInsert(
      // TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventorySubTrk ) );

      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "FULL", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      checkInventoryNoWarnings( "PASS" );

   }


   /**
    * This is to validation function of testOPER_27190(); OPER-27190: Actuals loader does not use
    * the validation logic associated with config parm APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY . By
    * default: APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY=False ACFT has applicability=004 , ENG has
    * applicability=004, but TRK has applicability range=002-005, but not in C_RI_INVENTORY_SUB
    * table. Expected result:after import, TRK will be loaded as XXX.
    *
    *
    *
    */
   @Test
   public void testOPER_27190_7_False_IMPORT() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOPER_27190_7_False_Validation();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), false, true, "FULL", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // get inv IDs
      iINV_Ids = getInvIds( iSERIAL_NO_OEM_ACFT );

      // Verify TRK under ENG is created
      simpleIDs lPNIDs = getPartNoIds( iPART_NO_OEM_TRK, iMANUFACT_CD_TRK_1 );
      verifyINVINV( iINV_Ids, "TRK", "XXX", iASSMBL_CD_ENG, iINV_NO_SDESC_TRK, lPNIDs );

   }


   /**
    * This is to validation function of testOPER_27190(); OPER-27190: Actuals loader does not use
    * the validation logic associated with config parm APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY . By
    * default: APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY=False ACFT has applicability=004 , ENG has
    * applicability=001, but TRK has applicability range=002-005, but not in
    * C_RI_INVENTORY_SUB.Expected result:after import, TRK will be loaded as XXX.
    *
    *
    *
    */
   @Test
   public void testOPER_27190_8_False_Validation() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // ACFT inventory
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();
      lMapInventory.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ACFT + "'" );
      lMapInventory.put( "PART_NO_OEM", "'" + iPART_NO_OEM_ACFT + "'" );
      lMapInventory.put( "INV_CLASS_CD", "'" + iINV_CLASS_CD_ACFT + "'" );
      lMapInventory.put( "MANUFACT_CD", "'" + iMANUFACT_CD_ACFT + "'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventory.put( "AC_REG_CD", "'AUTO-00001'" );
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "FORECAST_MODEL", "'Forecast1'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "APPL_EFF_CD", "'004'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // ENG inventory
      lMapInventory.clear();
      lMapInventory.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ENG + "'" );
      lMapInventory.put( "PART_NO_OEM", "'" + iPART_NO_OEM_ENG + "'" );
      lMapInventory.put( "INV_CLASS_CD", "'" + iINV_CLASS_CD_ENG + "'" );
      lMapInventory.put( "MANUFACT_CD", "'" + iMANUFACT_CD_ENG + "'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'INSRV'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "APPL_EFF_CD", "'001'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // ENG attach to ACFT
      Map<String, String> lMapInventoryAttach = new LinkedHashMap<String, String>();
      lMapInventoryAttach.put( "PARENT_SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ACFT + "'" );
      lMapInventoryAttach.put( "PARENT_PART_NO_OEM", "'" + iPART_NO_OEM_ACFT + "'" );
      lMapInventoryAttach.put( "PARENT_MANUFACT_CD", "'" + iMANUFACT_CD_ACFT + "'" );

      lMapInventoryAttach.put( "ATTACH_SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ENG + "'" );
      lMapInventoryAttach.put( "ATTACH_PART_NO_OEM", "'" + iPART_NO_OEM_ENG + "'" );
      lMapInventoryAttach.put( "ATTACH_MANUFACT_CD", "'" + iMANUFACT_CD_ENG + "'" );
      lMapInventoryAttach.put( "BOM_PART_CD", "'ENG-ASSY'" );
      lMapInventoryAttach.put( "EQP_POS_CD", "'1'" );
      lMapInventoryAttach.put( "INSTALL_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_ATTACH, lMapInventoryAttach ) );

      // // TRK inventory
      // Map<String, String> lMapInventorySubTrk = new LinkedHashMap<String, String>();
      // lMapInventorySubTrk.put( "PARENT_SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ENG + "'" );
      // lMapInventorySubTrk.put( "PARENT_PART_NO_OEM", "'" + iPART_NO_OEM_ENG + "'" );
      // lMapInventorySubTrk.put( "PARENT_MANUFACT_CD", "'" + iMANUFACT_CD_ENG + "'" );
      //
      // lMapInventorySubTrk.put( "BOM_PART_CD", "'ENG-SYS-1-TRK'" );
      // lMapInventorySubTrk.put( "EQP_POS_CD", "'1'" );
      // lMapInventorySubTrk.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ENG_TRK + "'" );
      // lMapInventorySubTrk.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      // lMapInventorySubTrk.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK_1 + "'" );
      // lMapInventorySubTrk.put( "INV_CLASS_CD", "'TRK'" );

      // // insert TRK map
      // runInsert(
      // TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventorySubTrk ) );

      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "FULL", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      checkInventoryNoWarnings( "PASS" );

   }


   /**
    * This is to validation function of testOPER_27190(); OPER-27190: Actuals loader does not use
    * the validation logic associated with config parm APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY . By
    * default: APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY=False ACFT has applicability=004 , ENG has
    * applicability=001, but TRK has applicability range=002-005, but not in
    * C_RI_INVENTORY_SUB.Expected result:after import, TRK will be loaded as XXX.
    *
    *
    *
    */

   @Test
   public void testOPER_27190_8_False_IMPORT() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOPER_27190_8_False_Validation();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), false, true, "FULL", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // get inv IDs
      iINV_Ids = getInvIds( iSERIAL_NO_OEM_ACFT );

      // Verify TRK under ENG is created
      simpleIDs lPNIDs = getPartNoIds( iPART_NO_OEM_TRK, iMANUFACT_CD_TRK_1 );
      verifyINVINV( iINV_Ids, "TRK", "XXX", iASSMBL_CD_ENG, iINV_NO_SDESC_TRK, lPNIDs );
      lPNIDs = getPartNoIds( iPART_NO_OEM_TRK_2, iMANUFACT_CD_TRK_1 );
      verifyINVINV( iINV_Ids, "TRK", "XXX", iASSMBL_CD_ENG, iINV_NO_SDESC_TRK_2, lPNIDs );

   }


   /**
    * This is to validation function of testOPER_27190(); OPER-27190: Actuals loader does not use
    * the validation logic associated with config parm APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY . By
    * default: APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY=False ACFT has applicability=001 , ENG has
    * applicability=004, but TRK has applicability range=002-005, but not in
    * C_RI_INVENTORY_SUB.Expected result:TRK will not be loaded.
    *
    *
    *
    */
   @Test
   public void testOPER_27190_9_False_Validation() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // ACFT inventory
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();
      lMapInventory.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ACFT + "'" );
      lMapInventory.put( "PART_NO_OEM", "'" + iPART_NO_OEM_ACFT + "'" );
      lMapInventory.put( "INV_CLASS_CD", "'" + iINV_CLASS_CD_ACFT + "'" );
      lMapInventory.put( "MANUFACT_CD", "'" + iMANUFACT_CD_ACFT + "'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventory.put( "AC_REG_CD", "'AUTO-00001'" );
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "FORECAST_MODEL", "'Forecast1'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "APPL_EFF_CD", "'001'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // ENG inventory
      lMapInventory.clear();
      lMapInventory.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ENG + "'" );
      lMapInventory.put( "PART_NO_OEM", "'" + iPART_NO_OEM_ENG + "'" );
      lMapInventory.put( "INV_CLASS_CD", "'" + iINV_CLASS_CD_ENG + "'" );
      lMapInventory.put( "MANUFACT_CD", "'" + iMANUFACT_CD_ENG + "'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'INSRV'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "APPL_EFF_CD", "'004'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // ENG attach to ACFT
      Map<String, String> lMapInventoryAttach = new LinkedHashMap<String, String>();
      lMapInventoryAttach.put( "PARENT_SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ACFT + "'" );
      lMapInventoryAttach.put( "PARENT_PART_NO_OEM", "'" + iPART_NO_OEM_ACFT + "'" );
      lMapInventoryAttach.put( "PARENT_MANUFACT_CD", "'" + iMANUFACT_CD_ACFT + "'" );

      lMapInventoryAttach.put( "ATTACH_SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ENG + "'" );
      lMapInventoryAttach.put( "ATTACH_PART_NO_OEM", "'" + iPART_NO_OEM_ENG + "'" );
      lMapInventoryAttach.put( "ATTACH_MANUFACT_CD", "'" + iMANUFACT_CD_ENG + "'" );
      lMapInventoryAttach.put( "BOM_PART_CD", "'ENG-ASSY'" );
      lMapInventoryAttach.put( "EQP_POS_CD", "'1'" );
      lMapInventoryAttach.put( "INSTALL_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_ATTACH, lMapInventoryAttach ) );

      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "FULL", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      checkInventoryNoWarnings( "PASS" );

   }


   /**
    * This is to validation function of testOPER_27190(); OPER-27190: Actuals loader does not use
    * the validation logic associated with config parm APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY . By
    * default: APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY=False ACFT has applicability=001 , ENG has
    * applicability=004, but TRK has applicability range=002-005, but not in
    * C_RI_INVENTORY_SUB.Expected result:TRK will not be loaded.
    *
    *
    *
    */
   @Test
   public void testOPER_27190_9_False_IMPORT() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOPER_27190_9_False_Validation();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), false, true, "FULL", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // get inv IDs
      iINV_Ids = getInvIds( iSERIAL_NO_OEM_ACFT );

      // Verify TRK under ENG is created
      verifyINVINV_NOTRK( iINV_Ids, "TRK", iASSMBL_CD_ENG );

   }


   /**
    * This is to validation function of testOPER_27190(); OPER-27190: Actuals loader does not use
    * the validation logic associated with config parm APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY . By
    * default: APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY=True ACFT has applicability=001, ENG has
    * applicability=004, but TRK has applicability range=002-005.Expect result: TRK would be created
    * as XXX.
    *
    *
    *
    *
    */
   @Test
   public void testOPER_27190_10_Validation() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // ACFT inventory
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();
      lMapInventory.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ACFT + "'" );
      lMapInventory.put( "PART_NO_OEM", "'" + iPART_NO_OEM_ACFT + "'" );
      lMapInventory.put( "INV_CLASS_CD", "'" + iINV_CLASS_CD_ACFT + "'" );
      lMapInventory.put( "MANUFACT_CD", "'" + iMANUFACT_CD_ACFT + "'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventory.put( "AC_REG_CD", "'AUTO-00001'" );
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "FORECAST_MODEL", "'Forecast1'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "APPL_EFF_CD", "'001'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // ENG inventory
      lMapInventory.clear();
      lMapInventory.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ENG + "'" );
      lMapInventory.put( "PART_NO_OEM", "'" + iPART_NO_OEM_ENG + "'" );
      lMapInventory.put( "INV_CLASS_CD", "'" + iINV_CLASS_CD_ENG + "'" );
      lMapInventory.put( "MANUFACT_CD", "'" + iMANUFACT_CD_ENG + "'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'INSRV'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "APPL_EFF_CD", "'004'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // ENG attach to ACFT
      Map<String, String> lMapInventoryAttach = new LinkedHashMap<String, String>();
      lMapInventoryAttach.put( "PARENT_SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ACFT + "'" );
      lMapInventoryAttach.put( "PARENT_PART_NO_OEM", "'" + iPART_NO_OEM_ACFT + "'" );
      lMapInventoryAttach.put( "PARENT_MANUFACT_CD", "'" + iMANUFACT_CD_ACFT + "'" );

      lMapInventoryAttach.put( "ATTACH_SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ENG + "'" );
      lMapInventoryAttach.put( "ATTACH_PART_NO_OEM", "'" + iPART_NO_OEM_ENG + "'" );
      lMapInventoryAttach.put( "ATTACH_MANUFACT_CD", "'" + iMANUFACT_CD_ENG + "'" );
      lMapInventoryAttach.put( "BOM_PART_CD", "'ENG-ASSY'" );
      lMapInventoryAttach.put( "EQP_POS_CD", "'1'" );
      lMapInventoryAttach.put( "INSTALL_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_ATTACH, lMapInventoryAttach ) );

      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "FULL", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // check error code
      checkInventoryNoWarnings( "PASS" );

   }


   /**
    * This is to validation function of testOPER_27190(); OPER-27190: Actuals loader does not use
    * the validation logic associated with config parm APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY . By
    * default: APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY=True ACFT has applicability=001, ENG has
    * applicability=004, but TRK has applicability range=002-005.Expect result: TRK would be created
    * as XXX.
    *
    *
    *
    *
    */
   @Test
   public void testOPER_27190_10_IMPORT() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOPER_27190_10_Validation();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), false, true, "FULL", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // get inv IDs
      iINV_Ids = getInvIds( iSERIAL_NO_OEM_ACFT );

      // Verify TRK under ENG is created
      simpleIDs lPNIDs = getPartNoIds( iPART_NO_OEM_TRK, iMANUFACT_CD_TRK_1 );
      verifyINVINV( iINV_Ids, "TRK", "XXX", iASSMBL_CD_ENG, iINV_NO_SDESC_TRK, lPNIDs );

   }


   /**
    * This is to validation function of testOPER_27190(); OPER-27190: Actuals loader does not use
    * the validation logic associated with config parm APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY . By
    * default: APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY=True ACFT has applicability=004, ENG has
    * applicability=001, but TRK has applicability range=002-005.Expect result: TRK would be created
    * as XXX.
    *
    *
    *
    *
    */
   @Test
   public void testOPER_27190_11_Validation() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // ACFT inventory
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();
      lMapInventory.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ACFT + "'" );
      lMapInventory.put( "PART_NO_OEM", "'" + iPART_NO_OEM_ACFT + "'" );
      lMapInventory.put( "INV_CLASS_CD", "'" + iINV_CLASS_CD_ACFT + "'" );
      lMapInventory.put( "MANUFACT_CD", "'" + iMANUFACT_CD_ACFT + "'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventory.put( "AC_REG_CD", "'AUTO-00001'" );
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "FORECAST_MODEL", "'Forecast1'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "APPL_EFF_CD", "'004'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // ENG inventory
      lMapInventory.clear();
      lMapInventory.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ENG + "'" );
      lMapInventory.put( "PART_NO_OEM", "'" + iPART_NO_OEM_ENG + "'" );
      lMapInventory.put( "INV_CLASS_CD", "'" + iINV_CLASS_CD_ENG + "'" );
      lMapInventory.put( "MANUFACT_CD", "'" + iMANUFACT_CD_ENG + "'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'INSRV'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "APPL_EFF_CD", "'001'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // ENG attach to ACFT
      Map<String, String> lMapInventoryAttach = new LinkedHashMap<String, String>();
      lMapInventoryAttach.put( "PARENT_SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ACFT + "'" );
      lMapInventoryAttach.put( "PARENT_PART_NO_OEM", "'" + iPART_NO_OEM_ACFT + "'" );
      lMapInventoryAttach.put( "PARENT_MANUFACT_CD", "'" + iMANUFACT_CD_ACFT + "'" );

      lMapInventoryAttach.put( "ATTACH_SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ENG + "'" );
      lMapInventoryAttach.put( "ATTACH_PART_NO_OEM", "'" + iPART_NO_OEM_ENG + "'" );
      lMapInventoryAttach.put( "ATTACH_MANUFACT_CD", "'" + iMANUFACT_CD_ENG + "'" );
      lMapInventoryAttach.put( "BOM_PART_CD", "'ENG-ASSY'" );
      lMapInventoryAttach.put( "EQP_POS_CD", "'1'" );
      lMapInventoryAttach.put( "INSTALL_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_ATTACH, lMapInventoryAttach ) );

      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "FULL", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // check error code
      checkInventoryNoWarnings( "PASS" );

   }


   @Test
   public void testOPER_27190_11_IMPORT() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOPER_27190_11_Validation();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), false, true, "FULL", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // get inv IDs
      iINV_Ids = getInvIds( iSERIAL_NO_OEM_ACFT );

      // Verify TRK under ENG is created
      verifyINVINV_NOTRK( iINV_Ids, "TRK", iASSMBL_CD_ENG );

   }


   /**
    * This is to validation function of testOPER_27190(); OPER-27190: Actuals loader does not use
    * the validation logic associated with config parm APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY . By
    * default: APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY=True ACFT has applicability=004, ENG has
    * applicability=004, TRK has applicability range=002-005, but no data in inv_sub table.Expect
    * result: TRK would be created.
    *
    *
    *
    *
    */
   @Test
   public void testOPER_27190_12_Validation() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // ACFT inventory
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();
      lMapInventory.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ACFT + "'" );
      lMapInventory.put( "PART_NO_OEM", "'" + iPART_NO_OEM_ACFT + "'" );
      lMapInventory.put( "INV_CLASS_CD", "'" + iINV_CLASS_CD_ACFT + "'" );
      lMapInventory.put( "MANUFACT_CD", "'" + iMANUFACT_CD_ACFT + "'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventory.put( "AC_REG_CD", "'AUTO-00001'" );
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "FORECAST_MODEL", "'Forecast1'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "APPL_EFF_CD", "'004'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // ENG inventory
      lMapInventory.clear();
      lMapInventory.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ENG + "'" );
      lMapInventory.put( "PART_NO_OEM", "'" + iPART_NO_OEM_ENG + "'" );
      lMapInventory.put( "INV_CLASS_CD", "'" + iINV_CLASS_CD_ENG + "'" );
      lMapInventory.put( "MANUFACT_CD", "'" + iMANUFACT_CD_ENG + "'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'INSRV'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "APPL_EFF_CD", "'004'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // ENG attach to ACFT
      Map<String, String> lMapInventoryAttach = new LinkedHashMap<String, String>();
      lMapInventoryAttach.put( "PARENT_SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ACFT + "'" );
      lMapInventoryAttach.put( "PARENT_PART_NO_OEM", "'" + iPART_NO_OEM_ACFT + "'" );
      lMapInventoryAttach.put( "PARENT_MANUFACT_CD", "'" + iMANUFACT_CD_ACFT + "'" );

      lMapInventoryAttach.put( "ATTACH_SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ENG + "'" );
      lMapInventoryAttach.put( "ATTACH_PART_NO_OEM", "'" + iPART_NO_OEM_ENG + "'" );
      lMapInventoryAttach.put( "ATTACH_MANUFACT_CD", "'" + iMANUFACT_CD_ENG + "'" );
      lMapInventoryAttach.put( "BOM_PART_CD", "'ENG-ASSY'" );
      lMapInventoryAttach.put( "EQP_POS_CD", "'1'" );
      lMapInventoryAttach.put( "INSTALL_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_ATTACH, lMapInventoryAttach ) );

      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "FULL", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // check error code
      checkInventoryNoWarnings( "PASS" );

   }


   /**
    * This is to validation function of testOPER_27190(); OPER-27190: Actuals loader does not use
    * the validation logic associated with config parm APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY . By
    * default: APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY=True ACFT has applicability=004, ENG has
    * applicability=004, TRK has applicability range=002-005, but no data in inv_sub table.Expect
    * result: TRK would be created.
    *
    *
    *
    *
    */

   @Test
   public void testOPER_27190_12_IMPORT() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testOPER_27190_12_Validation();

      // run import
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), false, true, "FULL", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // get inv IDs
      iINV_Ids = getInvIds( iSERIAL_NO_OEM_ACFT );

      // Verify TRK under ENG is created as XXX
      simpleIDs lPNIDs = getPartNoIds( iPART_NO_OEM_TRK, iMANUFACT_CD_TRK_1 );
      verifyINVINV( iINV_Ids, "TRK", "XXX", iASSMBL_CD_ENG, iINV_NO_SDESC_TRK, lPNIDs );

   }


   /**
    * This is to validation function of testOPER_27190(); OPER-27190: Actuals loader does not use
    * the validation logic associated with config parm APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY . By
    * default: APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY=True ACFT applicability=001 and ENG have no
    * applicability, but TRK has applicability range=002-005.Expect result: TRK under ENG is
    * created.
    *
    *
    *
    */
   @Test
   public void testOPER_27190_13_Validation() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // ACFT inventory
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();
      lMapInventory.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ACFT + "'" );
      lMapInventory.put( "PART_NO_OEM", "'" + iPART_NO_OEM_ACFT + "'" );
      lMapInventory.put( "INV_CLASS_CD", "'" + iINV_CLASS_CD_ACFT + "'" );
      lMapInventory.put( "MANUFACT_CD", "'" + iMANUFACT_CD_ACFT + "'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventory.put( "AC_REG_CD", "'AUTO-00001'" );
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "FORECAST_MODEL", "'Forecast1'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "APPL_EFF_CD", "'001'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // ENG inventory
      lMapInventory.clear();
      lMapInventory.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ENG + "'" );
      lMapInventory.put( "PART_NO_OEM", "'" + iPART_NO_OEM_ENG + "'" );
      lMapInventory.put( "INV_CLASS_CD", "'" + iINV_CLASS_CD_ENG + "'" );
      lMapInventory.put( "MANUFACT_CD", "'" + iMANUFACT_CD_ENG + "'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'INSRV'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "APPL_EFF_CD", null );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // ENG attach to ACFT
      Map<String, String> lMapInventoryAttach = new LinkedHashMap<String, String>();
      lMapInventoryAttach.put( "PARENT_SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ACFT + "'" );
      lMapInventoryAttach.put( "PARENT_PART_NO_OEM", "'" + iPART_NO_OEM_ACFT + "'" );
      lMapInventoryAttach.put( "PARENT_MANUFACT_CD", "'" + iMANUFACT_CD_ACFT + "'" );

      lMapInventoryAttach.put( "ATTACH_SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ENG + "'" );
      lMapInventoryAttach.put( "ATTACH_PART_NO_OEM", "'" + iPART_NO_OEM_ENG + "'" );
      lMapInventoryAttach.put( "ATTACH_MANUFACT_CD", "'" + iMANUFACT_CD_ENG + "'" );
      lMapInventoryAttach.put( "BOM_PART_CD", "'ENG-ASSY'" );
      lMapInventoryAttach.put( "EQP_POS_CD", "'1'" );
      lMapInventoryAttach.put( "INSTALL_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_ATTACH, lMapInventoryAttach ) );

      // TRK inventory
      Map<String, String> lMapInventorySubTrk = new LinkedHashMap<String, String>();
      lMapInventorySubTrk.put( "PARENT_SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ENG + "'" );
      lMapInventorySubTrk.put( "PARENT_PART_NO_OEM", "'" + iPART_NO_OEM_ENG + "'" );
      lMapInventorySubTrk.put( "PARENT_MANUFACT_CD", "'" + iMANUFACT_CD_ENG + "'" );

      lMapInventorySubTrk.put( "BOM_PART_CD", "'ENG-SYS-1-TRK'" );
      lMapInventorySubTrk.put( "EQP_POS_CD", "'1'" );
      lMapInventorySubTrk.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ENG_TRK + "'" );
      lMapInventorySubTrk.put( "PART_NO_OEM", "'" + iPART_NO_OEM_TRK + "'" );
      lMapInventorySubTrk.put( "MANUFACT_CD", "'" + iMANUFACT_CD_TRK_1 + "'" );
      lMapInventorySubTrk.put( "INV_CLASS_CD", "'TRK'" );

      // insert TRK map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventorySubTrk ) );

      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "FULL", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      checkInventoryNoWarnings( "PASS" );

   }


   /**
    * This is to validation function of testOPER_27190(); OPER-27190: Actuals loader does not use
    * the validation logic associated with config parm APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY . By
    * default: APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY=True ACFT applicability=001 and ENG have no
    * applicability, but TRK has applicability range=002-005.Expect result: TRK under ENG is
    * created.
    *
    *
    *
    */
   @Test
   public void testOPER_27190_13_IMPORT() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOPER_27190_13_Validation();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), false, true, "FULL", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // get inv IDs
      iINV_Ids = getInvIds( iSERIAL_NO_OEM_ACFT );

      // Verify TRK under ENG is created
      simpleIDs lPNIDs = getPartNoIds( iPART_NO_OEM_TRK, iMANUFACT_CD_TRK_1 );
      verifyINVINV( iINV_Ids, "TRK", iSERIAL_NO_OEM_ENG_TRK, iASSMBL_CD_ENG, iINV_NO_SDESC_TRK,
            lPNIDs );

   }


   // ==================================================================================================

   /**
    * This function is to verify INV_INV table
    *
    *
    */
   public void verifyINVINV( simpleIDs aINVIDs, String aINV_CLASS_CD, String aSERIAL_NO_OEM,
         String aASSMBL_CD, String aINV_NO_SDESC, simpleIDs aPNIDs ) {

      // EVT_INV table
      String[] iIds = { "INV_NO_SDESC" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "H_INV_NO_DB_ID", aINVIDs.getNO_DB_ID() );
      lArgs.addArguments( "H_INV_NO_ID", aINVIDs.getNO_ID() );
      lArgs.addArguments( "INV_CLASS_CD", aINV_CLASS_CD );
      lArgs.addArguments( "SERIAL_NO_OEM", aSERIAL_NO_OEM );
      lArgs.addArguments( "ASSMBL_CD", aASSMBL_CD );
      lArgs.addArguments( "PART_NO_DB_ID", aPNIDs.getNO_DB_ID() );
      lArgs.addArguments( "PART_NO_ID", aPNIDs.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.INV_INV, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "INV_NO_SDESC", llists.get( 0 ).get( 0 ).contains( aINV_NO_SDESC ) );

   }


   /**
    * This function is to verify INV_INV table
    *
    *
    */
   public void verifyINVINV_NOTRK( simpleIDs aINVIDs, String aINV_CLASS_CD, String aASSMBL_CD ) {

      // EVT_INV table
      String[] iIds = { "1" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "H_INV_NO_DB_ID", aINVIDs.getNO_DB_ID() );
      lArgs.addArguments( "H_INV_NO_ID", aINVIDs.getNO_ID() );
      lArgs.addArguments( "INV_CLASS_CD", aINV_CLASS_CD );
      lArgs.addArguments( "ASSMBL_CD", aASSMBL_CD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.INV_INV, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "NO TRK found: ", llists.size() == 0 );

   }


   /**
    * This function is to retrieve inv ids.
    *
    *
    */
   public simpleIDs getInvIds( String aSERIAL_NO_OEM ) {

      // REF_TASK_CLASS table
      String[] iIds = { "INV_NO_DB_ID", "INV_NO_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "SERIAL_NO_OEM", aSERIAL_NO_OEM );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.INV_INV, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lIds;

   }


   /**
    * This function is to retrieve part ids.
    *
    *
    */
   public simpleIDs getPartNoIds( String aPART_NO_OEM, String aMANUFACT_CD ) {

      // REF_TASK_CLASS table
      String[] iIds = { "PART_NO_DB_ID", "PART_NO_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
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
    * This function is called by after test method to restore data which has been modified by tests.
    *
    *
    */

   public void RestoreData() {

      String lStrDelete = null;

      // Delete data which created by test case
      if ( iINV_Ids != null ) {
         // lStrDelete = "delete from " + TableUtil.INV_CURR_USAGE + " where INV_NO_DB_ID='"
         // + iINV_Ids.getNO_DB_ID() + "' and INV_NO_ID='" + iINV_Ids.getNO_ID() + "'";
         // executeSQL( lStrDelete );

         lStrDelete = "delete from " + TableUtil.INV_INV + " where H_INV_NO_DB_ID='"
               + iINV_Ids.getNO_DB_ID() + "' and H_INV_NO_ID='" + iINV_Ids.getNO_ID() + "'";
         executeSQL( lStrDelete );

         lStrDelete = "delete from " + TableUtil.INV_AC_REG + " where INV_NO_DB_ID='"
               + iINV_Ids.getNO_DB_ID() + "' and INV_NO_ID='" + iINV_Ids.getNO_ID() + "'";
         executeSQL( lStrDelete );
      }

      String aUpdateQuery =
            "update UTL_CONFIG_PARM set PARM_VALUE='TRUE' where PARM_NAME='APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY'";

      runUpdate( aUpdateQuery );

   }


   /**
    * This function is to set parm = FALSE in UTL_CONFIG_PARM
    *
    *
    */

   public void setParmFalse() {

      String aUpdateQuery =
            "update UTL_CONFIG_PARM set PARM_VALUE='FALSE' where PARM_NAME='APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY'";

      runUpdate( aUpdateQuery );

   }

}
