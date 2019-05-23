package com.mxi.mx.core.maint.plan.actualsloader.inventory.validation;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.mxi.mx.util.ActualsLoaderTest;
import com.mxi.mx.util.StoreProcedureRunner;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.TestConstants;


/**
 * This test suite contains test cases on error code of NVL on validation functionality of INVENTORY
 * package.
 *
 * @author ALICIA QIAN
 */
public class ValidateNVLErrors extends ActualsLoaderTest {

   @Rule
   public TestName testName = new TestName();


   /**
    * Clean up after each individual test
    *
    * @throws Exception
    */
   @Override
   @After
   public void after() throws Exception {

      // clean up the event data
      clearMxTestData();

      super.after();
   }


   /**
    * Setup before executing each individual test
    *
    * @throws Exception
    */
   @Override
   @Before
   public void before() throws Exception {

      super.before();
   }


   /**
    * This test is to verify valid error code NVL_00001:The INV_OPER_CD field is only used for
    * Aircraft inventory and will be ignored for this record.
    *
    *
    */
   @Test
   public void test_NVL_00001() throws Exception {

      String lRandom = String.valueOf( getRandom() );

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ENG_ASSY_" + lRandom + "'" );
      lMapInventory.put( "PART_NO_OEM", "'ENG_ASSY_PN2'" );
      lMapInventory.put( "INV_CLASS_CD", "'ASSY'" );
      lMapInventory.put( "MANUFACT_CD", "'ABC11'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventory.put( "APPL_EFF_CD", "'200'" );
      // lMapInventory.put( "AC_REG_CD", "'ACREG1'" ); // field not provided
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      // lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "ASSMBL_CD", "'ENG_CD1'" );
      lMapInventory.put( "ASSMBL_BOM_CD", "'ENG_CD1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // run validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // check error code
      checkInventoryErrorCode( testName.getMethodName(), "NVL-00001" );
   }


   /**
    * This test is to verify valid error code NVL_00013:The ASSMBL_BOM_CD field is only used for
    * Aircraft, Assembly and Tracked inventory and will be ignored for this record
    *
    *
    */
   @Test
   public void test_NVL_00013() throws Exception {

      String lRandom = String.valueOf( getRandom() );

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'COMMHW_" + lRandom + "'" );
      lMapInventory.put( "PART_NO_OEM", "'CHW000002'" );
      lMapInventory.put( "INV_CLASS_CD", "'BATCH'" );
      lMapInventory.put( "MANUFACT_CD", "'11111'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      // lMapInventory.put( "INV_COND_CD", "'INSRV'" );
      // lMapInventory.put( "APPL_EFF_CD", "'200'" );
      // lMapInventory.put( "AC_REG_CD", "'ACREG1'" ); // field not provided
      // lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      // lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "BIN_QT", "'5'" );
      lMapInventory.put( "OWNER_CD", "'BORROW'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "ASSMBL_CD", "'COMHW'" );
      lMapInventory.put( "ASSMBL_BOM_CD", "'Common HardwareCommon Hardware'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // run validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // check error code
      checkInventoryErrorCode( testName.getMethodName(), "NVL-00013" );
   }


   /**
    * This test is to verify valid error code NVL_00012:The ASSMBL_CD field is only used for
    * Aircraft, Assembly and Tracked inventory and will be ignored for this record
    *
    *
    */
   @Test
   public void test_NVL_00012() throws Exception {

      String lRandom = String.valueOf( getRandom() );

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'COMMHW_" + lRandom + "'" );
      lMapInventory.put( "PART_NO_OEM", "'CHW000002'" );
      lMapInventory.put( "INV_CLASS_CD", "'BATCH'" );
      lMapInventory.put( "MANUFACT_CD", "'11111'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      // lMapInventory.put( "INV_COND_CD", "'INSRV'" );
      // lMapInventory.put( "APPL_EFF_CD", "'200'" );
      // lMapInventory.put( "AC_REG_CD", "'ACREG1'" ); // field not provided
      // lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      // lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "BIN_QT", "'5'" );
      lMapInventory.put( "OWNER_CD", "'BORROW'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "ASSMBL_CD", "'COMHW'" );
      lMapInventory.put( "ASSMBL_BOM_CD", "'Common HardwareCommon Hardware'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // run validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // check error code
      checkInventoryErrorCode( testName.getMethodName(), "NVL-00012" );
   }


   /**
    * This test is to verify valid error code NVL_00002:The REG_BODY_CD field is only used for
    * Aircraft inventory and will be ignored for this record
    *
    *
    */

   @Test
   public void test_NVL_00002() throws Exception {

      String lRandom = String.valueOf( getRandom() );

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'COMMHW_" + lRandom + "'" );
      lMapInventory.put( "PART_NO_OEM", "'CHW000002'" );
      lMapInventory.put( "INV_CLASS_CD", "'BATCH'" );
      lMapInventory.put( "MANUFACT_CD", "'11111'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "REG_BODY_CD", "'5'" );
      lMapInventory.put( "OWNER_CD", "'BORROW'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "ASSMBL_CD", "'COMHW'" );
      lMapInventory.put( "ASSMBL_BOM_CD", "'Common HardwareCommon Hardware'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // run validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // check error code
      checkInventoryErrorCode( testName.getMethodName(), "NVL-00002" );
   }


   /**
    * This test is to verify valid error code NVL_00003:The INV_CAPABILITY_CD field is only used for
    * Aircraft inventory and will be ignored for this record
    *
    *
    */

   @Test
   public void test_NVL_00003() throws Exception {

      String lRandom = String.valueOf( getRandom() );

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'COMMHW_" + lRandom + "'" );
      lMapInventory.put( "PART_NO_OEM", "'CHW000002'" );
      lMapInventory.put( "INV_CLASS_CD", "'BATCH'" );
      lMapInventory.put( "MANUFACT_CD", "'11111'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_CAPABILITY_CD", "'5'" );
      lMapInventory.put( "OWNER_CD", "'BORROW'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "ASSMBL_CD", "'COMHW'" );
      lMapInventory.put( "ASSMBL_BOM_CD", "'Common HardwareCommon Hardware'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // run validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // check error code
      checkInventoryErrorCode( testName.getMethodName(), "NVL-00003" );
   }


   /**
    * This test is to verify valid error code NVL_00004:The COUNTRY_CD field is only used for
    * Aircraft inventory and will be ignored for this record
    *
    *
    */
   @Test
   public void test_NVL_00004() throws Exception {

      String lRandom = String.valueOf( getRandom() );

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'COMMHW_" + lRandom + "'" );
      lMapInventory.put( "PART_NO_OEM", "'CHW000002'" );
      lMapInventory.put( "INV_CLASS_CD", "'BATCH'" );
      lMapInventory.put( "MANUFACT_CD", "'11111'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "COUNTRY_CD", "'US'" );
      lMapInventory.put( "OWNER_CD", "'BORROW'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "ASSMBL_CD", "'COMHW'" );
      lMapInventory.put( "ASSMBL_BOM_CD", "'Common HardwareCommon Hardware'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // run validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // check error code
      checkInventoryErrorCode( testName.getMethodName(), "NVL-00004" );
   }


   /**
    * This test is to verify valid error code NVL_00005:The AC_REG_CD field is only used for
    * Aircraft inventory and will be ignored for this record
    *
    *
    */
   @Test
   public void test_NVL_00005() throws Exception {

      String lRandom = String.valueOf( getRandom() );

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'COMMHW_" + lRandom + "'" );
      lMapInventory.put( "PART_NO_OEM", "'CHW000002'" );
      lMapInventory.put( "INV_CLASS_CD", "'BATCH'" );
      lMapInventory.put( "MANUFACT_CD", "'11111'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "AC_REG_CD", "'ACREG1'" );
      lMapInventory.put( "OWNER_CD", "'BORROW'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "ASSMBL_CD", "'COMHW'" );
      lMapInventory.put( "ASSMBL_BOM_CD", "'Common HardwareCommon Hardware'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // run validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // check error code
      checkInventoryErrorCode( testName.getMethodName(), "NVL-00005" );
   }


   /**
    * This test is to verify valid error code NVL_00006:The AIRWORTH_CD field is only used for
    * Aircraft inventory and will be ignored for this record
    *
    *
    */
   @Test
   public void test_NVL_00006() throws Exception {

      String lRandom = String.valueOf( getRandom() );

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'COMMHW_" + lRandom + "'" );
      lMapInventory.put( "PART_NO_OEM", "'CHW000002'" );
      lMapInventory.put( "INV_CLASS_CD", "'BATCH'" );
      lMapInventory.put( "MANUFACT_CD", "'11111'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "AIRWORTH_CD", "'test'" );
      lMapInventory.put( "OWNER_CD", "'BORROW'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "ASSMBL_CD", "'COMHW'" );
      lMapInventory.put( "ASSMBL_BOM_CD", "'Common HardwareCommon Hardware'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // run validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // check error code
      checkInventoryErrorCode( testName.getMethodName(), "NVL-00006" );
   }


   /**
    * This test is to verify valid error code NVL_00007:The VAR_NO_OEM field is only used for
    * Aircraft inventory and will be ignored for this record
    *
    *
    */
   @Test
   public void test_NVL_00007() throws Exception {

      String lRandom = String.valueOf( getRandom() );

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'COMMHW_" + lRandom + "'" );
      lMapInventory.put( "PART_NO_OEM", "'CHW000002'" );
      lMapInventory.put( "INV_CLASS_CD", "'BATCH'" );
      lMapInventory.put( "MANUFACT_CD", "'11111'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "VAR_NO_OEM", "'test'" );
      lMapInventory.put( "OWNER_CD", "'BORROW'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "ASSMBL_CD", "'COMHW'" );
      lMapInventory.put( "ASSMBL_BOM_CD", "'Common HardwareCommon Hardware'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // run validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // check error code
      checkInventoryErrorCode( testName.getMethodName(), "NVL-00007" );
   }


   /**
    * This test is to verify valid error code NVL_00008:The LINE_NO_OEM field is only used for
    * Aircraft inventory and will be ignored for this record
    *
    *
    */
   @Test
   public void test_NVL_00008() throws Exception {

      String lRandom = String.valueOf( getRandom() );

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'COMMHW_" + lRandom + "'" );
      lMapInventory.put( "PART_NO_OEM", "'CHW000002'" );
      lMapInventory.put( "INV_CLASS_CD", "'BATCH'" );
      lMapInventory.put( "MANUFACT_CD", "'11111'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "LINE_NO_OEM", "'test'" );
      lMapInventory.put( "OWNER_CD", "'BORROW'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "ASSMBL_CD", "'COMHW'" );
      lMapInventory.put( "ASSMBL_BOM_CD", "'Common HardwareCommon Hardware'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // run validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // check error code
      checkInventoryErrorCode( testName.getMethodName(), "NVL-00008" );
   }


   /**
    * This test is to verify valid error code NVL_00009:The FIN_NO_CD field is only used for
    * Aircraft inventory and will be ignored for this record
    *
    *
    */
   @Test
   public void test_NVL_00009() throws Exception {

      String lRandom = String.valueOf( getRandom() );

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'COMMHW_" + lRandom + "'" );
      lMapInventory.put( "PART_NO_OEM", "'CHW000002'" );
      lMapInventory.put( "INV_CLASS_CD", "'BATCH'" );
      lMapInventory.put( "MANUFACT_CD", "'11111'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "FIN_NO_CD", "'test'" );
      lMapInventory.put( "OWNER_CD", "'BORROW'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "ASSMBL_CD", "'COMHW'" );
      lMapInventory.put( "ASSMBL_BOM_CD", "'Common HardwareCommon Hardware'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // run validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // check error code
      checkInventoryErrorCode( testName.getMethodName(), "NVL-00009" );
   }


   /**
    * This test is to verify valid error code NVL_00010:The FORECAST_MODEL field is only used for
    * Aircraft inventory and will be ignored for this record
    *
    *
    */
   @Test
   public void test_NVL_00010() throws Exception {

      String lRandom = String.valueOf( getRandom() );

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'COMMHW_" + lRandom + "'" );
      lMapInventory.put( "PART_NO_OEM", "'CHW000002'" );
      lMapInventory.put( "INV_CLASS_CD", "'BATCH'" );
      lMapInventory.put( "MANUFACT_CD", "'11111'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "FORECAST_MODEL", "'test'" );
      lMapInventory.put( "OWNER_CD", "'BORROW'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "ASSMBL_CD", "'COMHW'" );
      lMapInventory.put( "ASSMBL_BOM_CD", "'Common HardwareCommon Hardware'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // run validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // check error code
      checkInventoryErrorCode( testName.getMethodName(), "NVL-00010" );
   }


   /**
    * This test is to verify valid error code NVL_00011:The PRIVATE_BOOL field is only used for
    * Aircraft inventory and will be ignored for this record
    *
    *
    */
   @Test
   public void test_NVL_00011() throws Exception {

      String lRandom = String.valueOf( getRandom() );

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'COMMHW_" + lRandom + "'" );
      lMapInventory.put( "PART_NO_OEM", "'CHW000002'" );
      lMapInventory.put( "INV_CLASS_CD", "'BATCH'" );
      lMapInventory.put( "MANUFACT_CD", "'11111'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "PRIVATE_BOOL", "'1'" );
      lMapInventory.put( "OWNER_CD", "'BORROW'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "ASSMBL_CD", "'COMHW'" );
      lMapInventory.put( "ASSMBL_BOM_CD", "'Common HardwareCommon Hardware'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // run validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // check error code
      checkInventoryErrorCode( testName.getMethodName(), "NVL-00011" );
   }


   /**
    * This test is to verify valid error code NVL_00014:The BOM_PART_CD field is only used for
    * Tracked inventory and will be ignored for this record
    *
    *
    */
   @Test
   public void test_NVL_00014() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-SN'" );
      lMapInventory.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventory.put( "INV_CLASS_CD", "'ACFT'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventory.put( "AC_REG_CD", "'ACREG1'" );
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // create task map
      Map<String, String> lInventoryChild = new LinkedHashMap<String, String>();
      lInventoryChild.put( "PARENT_SERIAL_NO_OEM", "'ACFT-SN'" );
      lInventoryChild.put( "PARENT_PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lInventoryChild.put( "PARENT_MANUFACT_CD", "'10001'" );
      lInventoryChild.put( "BOM_PART_CD", "'ACFT-SYS-1-1-BATCH'" );
      // lInventoryChild.put( "EQP_POS_CD", "'1'" );
      lInventoryChild.put( "SERIAL_NO_OEM", "'TT-CHILD'" );
      lInventoryChild.put( "PART_NO_OEM", "'A0000009'" );
      lInventoryChild.put( "MANUFACT_CD", "'10001'" );
      lInventoryChild.put( "INV_CLASS_CD", "'BATCH'" );
      lInventoryChild.put( "OWNER_CD", "'BORROW'" ); // value unknown in mx
      lInventoryChild.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lInventoryChild ) );

      // run validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // check error code
      checkInventoryErrorCode( testName.getMethodName(), "NVL-00014" );
   }


   /**
    * This test is to verify valid error code NVL_00015:The EQP_POS_CD field is only used for
    * Tracked inventory and will be ignored for this record
    *
    *
    */
   @Test
   public void test_NVL_00015() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-SN'" );
      lMapInventory.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventory.put( "INV_CLASS_CD", "'ACFT'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventory.put( "AC_REG_CD", "'ACREG1'" );
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // create task map
      Map<String, String> lInventoryChild = new LinkedHashMap<String, String>();
      lInventoryChild.put( "PARENT_SERIAL_NO_OEM", "'ACFT-SN'" );
      lInventoryChild.put( "PARENT_PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lInventoryChild.put( "PARENT_MANUFACT_CD", "'10001'" );
      // lInventoryChild.put( "BOM_PART_CD", "'ACFT-SYS-1-1-BATCH'" );
      lInventoryChild.put( "EQP_POS_CD", "'1'" );
      lInventoryChild.put( "SERIAL_NO_OEM", "'TT-CHILD'" );
      lInventoryChild.put( "PART_NO_OEM", "'A0000009'" );
      lInventoryChild.put( "MANUFACT_CD", "'10001'" );
      lInventoryChild.put( "INV_CLASS_CD", "'BATCH'" );
      lInventoryChild.put( "OWNER_CD", "'BORROW'" );
      lInventoryChild.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lInventoryChild ) );

      // run validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // check error code
      checkInventoryErrorCode( testName.getMethodName(), "NVL-00015" );
   }

}
