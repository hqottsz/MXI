/*
 * Confidential, proprietary and/or trade secret information of Mxi Technologies, Ltd.
 *
 * Copyright 2000-2016 Mxi Technologies, Ltd. All Rights Reserved.
 *
 * Except as expressly provided by written license signed by a duly appointed officer of Mxi
 * Technologies, Ltd., any disclosure, distribution, reproduction, compilation, modification,
 * creation of derivative works and/or other use of the Mxi source code is strictly prohibited.
 * Inclusion of a copyright notice shall not be taken to indicate that the source code has been
 * published.
 */

package com.mxi.mx.core.maint.plan.actualsloader.inventory.imports;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.mxi.mx.util.StoreProcedureRunner;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.TestConstants;


/**
 * This test suite contains unit tests for Actuals Loader task validations for Part Numbers and
 * Serial Numbers
 *
 * @author Andrew Bruce
 */

public class ImportPartGroupConfigSlotMismatch extends AbstractImportInventory {

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
    * <p>
    * Given the Preconditions:
    *
    * <ul>
    * <li>Part number is known. Adding TRK to A loose TRK Part</li>
    * </ul>
    * </p>
    *
    * <p>
    * The test will:
    *
    * <ul>
    * <li>Insert inventory to the staging table</li>
    * <li>Run inventory Validation</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Error Code: None</li>
    * <li>No validation errors are shown if you try to load inventory.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Ignore
   @Test
   public void test_BlankBomPartAndPosCdImportsSuccessfully() throws Exception {

      // create inventory map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'SN-10013PMX'" );
      lMapInventory.put( "PART_NO_OEM", "'A0000005'" );
      lMapInventory.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "ASSMBL_CD", "'ACFT_CD1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // create sub-inventory map
      Map<String, String> lMapInventorySub = new LinkedHashMap<String, String>();

      lMapInventorySub.put( "PARENT_SERIAL_NO_OEM", "'SN-10013PMX'" );
      lMapInventorySub.put( "PARENT_PART_NO_OEM", "'A0000005'" );
      lMapInventorySub.put( "PARENT_MANUFACT_CD", "'10001'" );
      lMapInventorySub.put( "SERIAL_NO_OEM", "'SN-10023C1MX'" );
      lMapInventorySub.put( "PART_NO_OEM", "'A0000006'" );
      lMapInventorySub.put( "MANUFACT_CD", "'11111'" );
      lMapInventorySub.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventorySub.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventorySub ) );

      // create inventory usage map
      Map<String, String> lInventoryUsage = new LinkedHashMap<String, String>();
      lInventoryUsage.put( "SERIAL_NO_OEM", "'SN-10013PMX'" );
      lInventoryUsage.put( "PART_NO_OEM", "'A0000005'" );
      lInventoryUsage.put( "MANUFACT_CD", "'10001'" );
      lInventoryUsage.put( "DATA_TYPE_CD", "'HOURS'" );
      lInventoryUsage.put( "TSN_QT", "1" );
      lInventoryUsage.put( "TSO_QT", "1" );
      lInventoryUsage.put( "TSI_QT", "1" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE, lInventoryUsage ) );

      // insert row for each usage parm
      lInventoryUsage.put( "DATA_TYPE_CD", "'CYCLES'" );
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE, lInventoryUsage ) );
      lInventoryUsage.put( "DATA_TYPE_CD", "'USAGE1'" );
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE, lInventoryUsage ) );
      lInventoryUsage.put( "DATA_TYPE_CD", "'USAGE2'" );
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE, lInventoryUsage ) );
      lInventoryUsage.put( "DATA_TYPE_CD", "'USAGE3'" );
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE, lInventoryUsage ) );

      lInventoryUsage.put( "SERIAL_NO_OEM", "'SN-10023C1MX'" );
      lInventoryUsage.put( "PART_NO_OEM", "'A0000006'" );
      lInventoryUsage.put( "MANUFACT_CD", "'11111'" );
      lInventoryUsage.put( "DATA_TYPE_CD", "'HOURS'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE, lInventoryUsage ) );

      // insert row for each usage parm
      lInventoryUsage.put( "DATA_TYPE_CD", "'CYCLES'" );
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE, lInventoryUsage ) );
      lInventoryUsage.put( "DATA_TYPE_CD", "'USAGE1'" );
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE, lInventoryUsage ) );
      lInventoryUsage.put( "DATA_TYPE_CD", "'USAGE2'" );
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE, lInventoryUsage ) );
      lInventoryUsage.put( "DATA_TYPE_CD", "'USAGE3'" );
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE, lInventoryUsage ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_BlankBomPartAndPosCdImportsSuccessfully", "PASS" );

      importInventory();

      // verify that import inventory was created as expected
      checkInventoryDetails( lMapInventory );
   }


   /**
    * <p>
    * Given the Preconditions:
    *
    * <ul>
    * <li>Part group code and config slot code don't match</li>
    * </ul>
    * </p>
    *
    * <p>
    * The test will:
    *
    * <ul>
    * <li>Insert inventory to the staging table</li>
    * <li>Run inventory Validation</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Error Code: NONE</li>
    * <li>No validation errors are shown if you try to load loose TRK inventory where there is a
    * mismatch</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Ignore
   @Test
   public void test_PartGroupConfigSlotMismatchLooseInventoryImportsSuccessfully()
         throws Exception {

      String lTRKSerialNo = "TRK" + getRandom();

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'" + lTRKSerialNo + "'" );
      lMapInventory.put( "PART_NO_OEM", "'TRK-MISMATCH'" ); // part with mismatch
      lMapInventory.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'RFI'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // call inventory validation & import
      validateAndCheckInventory(
            "test_PartGroupConfigSlotMismatchLooseInventoryImportsSuccessfully", "PASS" );
      importInventory();

      // verify that import inventory was created as expected
      checkInventoryDetails( lMapInventory );
   }


   /**
    * <p>
    * Given the Preconditions:
    *
    * <ul>
    * <li>Part group code and config slot code don't match</li>
    * </ul>
    * </p>
    *
    * <p>
    * The test will:
    *
    * <ul>
    * <li>Insert inventory to the staging table</li>
    * <li>Run inventory Validation</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Error Code: NONE</li>
    * <li>No validation errors are shown if you try to load installed TRK inventory where there is a
    * mismatch</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Ignore
   @Test
   public void test_PartGroupConfigSlotMismatchSubInventoryImportsSuccessfully() throws Exception {

      String lTRKSerialNo = "TRK" + getRandom();
      String lACFTRegCd = "AC" + getRandom();

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-" + lTRKSerialNo + "'" );
      lMapInventory.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventory.put( "INV_CLASS_CD", "'ACFT'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'INSRV'" );
      lMapInventory.put( "AC_REG_CD", "'" + lACFTRegCd + "'" );
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // create task map
      Map<String, String> lInventoryUsage = new LinkedHashMap<String, String>();
      lInventoryUsage.put( "SERIAL_NO_OEM", "'ACFT-" + lTRKSerialNo + "'" );
      lInventoryUsage.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lInventoryUsage.put( "MANUFACT_CD", "'10001'" );
      lInventoryUsage.put( "DATA_TYPE_CD", "'HOURS'" );
      lInventoryUsage.put( "TSN_QT", "1" );
      lInventoryUsage.put( "TSO_QT", "1" );
      lInventoryUsage.put( "TSI_QT", "1" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE, lInventoryUsage ) );

      // insert row for each usage parm
      lInventoryUsage.put( "DATA_TYPE_CD", "'CYCLES'" );
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE, lInventoryUsage ) );
      lInventoryUsage.put( "DATA_TYPE_CD", "'USAGE1'" );
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE, lInventoryUsage ) );
      lInventoryUsage.put( "DATA_TYPE_CD", "'USAGE2'" );
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE, lInventoryUsage ) );
      lInventoryUsage.put( "DATA_TYPE_CD", "'USAGE3'" );
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE, lInventoryUsage ) );

      // create task map
      Map<String, String> lMapInventorySub = new LinkedHashMap<String, String>();

      lMapInventorySub.put( "PARENT_SERIAL_NO_OEM", "'ACFT-" + lTRKSerialNo + "'" );
      lMapInventorySub.put( "PARENT_PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventorySub.put( "PARENT_MANUFACT_CD", "'10001'" );
      lMapInventorySub.put( "BOM_PART_CD", "'MISMATCH'" );
      lMapInventorySub.put( "EQP_POS_CD", "'1.1'" );
      lMapInventorySub.put( "SERIAL_NO_OEM", "'MISMATCH-" + lTRKSerialNo + "'" );
      lMapInventorySub.put( "PART_NO_OEM", "'TRK-MISMATCH'" );
      lMapInventorySub.put( "MANUFACT_CD", "'10001'" );
      lMapInventorySub.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventorySub.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventorySub ) );

      // call inventory validation & import
      validateAndCheckInventory( "test_PartGroupConfigSlotMismatchSubInventoryImportsSuccessfully",
            "PASS" );
      importInventory();

      // verify that import inventory was created as expected
      checkInventoryDetails( lMapInventory );

   }


   /**
    * <p>
    * Given the Preconditions:
    *
    * <ul>
    * <li>Part number is known. Adding ASSY to an ACFT</li>
    * <li>No BOM_PART_CD and EQP_POS_CD are provided</li>
    * </ul>
    * </p>
    *
    * <p>
    * The test will:
    *
    * <ul>
    * <li>Insert inventory to the staging table</li>
    * <li>Run inventory Validation</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Error Code: None</li>
    * <li>No validation errors are shown if you try to load inventory.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Ignore
   @Test
   public void test_BlankBomPartAndPosCdImportsSuccessfullyForAssembly() throws Exception {

      String lRandom = String.valueOf( getRandom() );

      // create inventory map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();
      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-" + lRandom + "'" );
      lMapInventory.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventory.put( "INV_CLASS_CD", "'ACFT'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'INSRV'" );
      lMapInventory.put( "AC_REG_CD", "'" + lRandom + "'" );
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // create inventory map
      Map<String, String> lMapInventoryAssy = new LinkedHashMap<String, String>();
      lMapInventoryAssy.put( "SERIAL_NO_OEM", "'ASSY-" + lRandom + "'" );
      lMapInventoryAssy.put( "PART_NO_OEM", "'ENG_ASSY_PN1'" );
      lMapInventoryAssy.put( "INV_CLASS_CD", "'ASSY'" );
      lMapInventoryAssy.put( "MANUFACT_CD", "'ABC11'" );
      lMapInventoryAssy.put( "LOC_CD", "'OPS'" );
      lMapInventoryAssy.put( "INV_COND_CD", "'INSRV'" );
      lMapInventoryAssy.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventoryAssy ) );

      // create inventory attach map
      Map<String, String> lMapInventoryAttach = new LinkedHashMap<String, String>();
      lMapInventoryAttach.put( "PARENT_SERIAL_NO_OEM", "'ACFT-" + lRandom + "'" );
      lMapInventoryAttach.put( "PARENT_PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventoryAttach.put( "PARENT_MANUFACT_CD", "'10001'" );
      lMapInventoryAttach.put( "ATTACH_SERIAL_NO_OEM", "'ASSY-" + lRandom + "'" );
      lMapInventoryAttach.put( "ATTACH_PART_NO_OEM", "'ENG_ASSY_PN1'" );
      lMapInventoryAttach.put( "ATTACH_MANUFACT_CD", "'ABC11'" );
      lMapInventoryAttach.put( "INSTALL_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_ATTACH, lMapInventoryAttach ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_BlankBomPartAndPosCdImportsSuccessfullyForAssembly",
            "PASS" );

      importInventory();

      // verify that import inventory was created as expected
      checkInventoryDetails( lMapInventory );
   }


   /**
    * This test is to verify valid error code CLD-00005: Cannot load record because the related
    * record in C_RI_ATTACH is invalid (PARENT_SERIAL_NO_OEM, PARENT_PART_NO_OEM,
    * PARENT_MANUFACT_CD)
    *
    *
    */
   @Test
   public void test_CLD_00005() throws Exception {

      String lRandom = String.valueOf( getRandom() );

      // create inventory map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();
      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-" + lRandom + "'" );
      lMapInventory.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventory.put( "INV_CLASS_CD", "'ACFT'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventory.put( "AC_REG_CD", "'" + lRandom + "'" );
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // create inventory map
      Map<String, String> lMapInventoryAssy = new LinkedHashMap<String, String>();
      lMapInventoryAssy.put( "SERIAL_NO_OEM", "'ASSY-" + lRandom + "'" );
      lMapInventoryAssy.put( "PART_NO_OEM", "'ENG_ASSY_PN1'" );
      lMapInventoryAssy.put( "INV_CLASS_CD", "'ASSY'" );
      lMapInventoryAssy.put( "MANUFACT_CD", "'11111'" );
      lMapInventoryAssy.put( "LOC_CD", "'OPS'" );
      lMapInventoryAssy.put( "INV_COND_CD", "'INSRV'" );
      lMapInventoryAssy.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventoryAssy ) );

      // create inventory attach map
      Map<String, String> lMapInventoryAttach = new LinkedHashMap<String, String>();
      lMapInventoryAttach.put( "PARENT_SERIAL_NO_OEM", "'ACFT-" + lRandom + "'" );
      lMapInventoryAttach.put( "PARENT_PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventoryAttach.put( "PARENT_MANUFACT_CD", "'10001'" );
      lMapInventoryAttach.put( "ATTACH_SERIAL_NO_OEM", "'ASSY-" + lRandom + "'" );
      lMapInventoryAttach.put( "ATTACH_PART_NO_OEM", "'ENG_ASSY_PN1'" );
      lMapInventoryAttach.put( "ATTACH_MANUFACT_CD", "'ABC11'" );
      lMapInventoryAttach.put( "INSTALL_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_ATTACH, lMapInventoryAttach ) );

      // run validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // check error code
      checkInventoryErrorCode( testName.getMethodName(), "CLD-00005" );

   }


   /**
    * This test is to verify valid error code CLD-00006: Cannot load record because the related
    * record in C_RI_ATTACH is invalid (ATTACH_SERIAL_NO_OEM, ATTACH_PART_NO_OEM,
    * ATTACH_MANUFACT_CD).
    *
    *
    */
   @Test
   public void test_CLD_00006() throws Exception {

      String lRandom = String.valueOf( getRandom() );

      // create inventory map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();
      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-" + lRandom + "'" );
      lMapInventory.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventory.put( "INV_CLASS_CD", "'ACFT'" );
      lMapInventory.put( "MANUFACT_CD", "'11111'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventory.put( "AC_REG_CD", "'" + lRandom + "'" );
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // create inventory map
      Map<String, String> lMapInventoryAssy = new LinkedHashMap<String, String>();
      lMapInventoryAssy.put( "SERIAL_NO_OEM", "'ASSY-" + lRandom + "'" );
      lMapInventoryAssy.put( "PART_NO_OEM", "'ENG_ASSY_PN1'" );
      lMapInventoryAssy.put( "INV_CLASS_CD", "'ASSY'" );
      lMapInventoryAssy.put( "MANUFACT_CD", "'ABC11'" );
      lMapInventoryAssy.put( "LOC_CD", "'OPS'" );
      lMapInventoryAssy.put( "INV_COND_CD", "'INSRV'" );
      lMapInventoryAssy.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventoryAssy ) );

      // create inventory attach map
      Map<String, String> lMapInventoryAttach = new LinkedHashMap<String, String>();
      lMapInventoryAttach.put( "PARENT_SERIAL_NO_OEM", "'ACFT-" + lRandom + "'" );
      lMapInventoryAttach.put( "PARENT_PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventoryAttach.put( "PARENT_MANUFACT_CD", "'11111'" );
      lMapInventoryAttach.put( "ATTACH_SERIAL_NO_OEM", "'ASSY-" + lRandom + "'" );
      lMapInventoryAttach.put( "ATTACH_PART_NO_OEM", "'ENG_ASSY_PN1'" );
      lMapInventoryAttach.put( "ATTACH_MANUFACT_CD", "'ABC11'" );
      lMapInventoryAttach.put( "INSTALL_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_ATTACH, lMapInventoryAttach ) );

      // run validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // check error code
      checkInventoryErrorCode( testName.getMethodName(), "CLD-00006" );

   }
}
