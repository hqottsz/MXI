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
 * This test suite contains unit tests for Actuals Loader task validations for Part Numbers and
 * Serial Numbers
 *
 * @author Andrew Bruce
 */

public class ValidateUsage extends ActualsLoaderTest {

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
    * <li>Part number is known</li>
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
    * <li>Error Code: AML-10406</li>
    * <li>INVENTORY_USAGE item has a usage parameter that is not defined for the associated
    * INVENTORY or SUB_INVENTORY item Configuration Slot.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10406_ParameterNotAssignedToSlot_Usage() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'SN-10023'" ); // field not provided
      lMapInventory.put( "PART_NO_OEM", "'A0000005'" );
      lMapInventory.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      // lMapInventory.put( "INV_COND_CD", "'RFI'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "ASSMBL_CD", "'ACFT_CD1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // create task map
      Map<String, String> lMapInventoryUsage = new LinkedHashMap<String, String>();

      lMapInventoryUsage.put( "SERIAL_NO_OEM", "'SN-10023'" );
      lMapInventoryUsage.put( "PART_NO_OEM", "'A0000005'" );
      lMapInventoryUsage.put( "MANUFACT_CD", "'10001'" );
      lMapInventoryUsage.put( "DATA_TYPE_CD", "'ECYC'" );
      lMapInventoryUsage.put( "TSN_QT", "1" );
      lMapInventoryUsage.put( "TSI_QT", "1" );
      lMapInventoryUsage.put( "TSO_QT", "1" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lMapInventoryUsage ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10406_ParameterNotAssignedToSlot_Usage", "AML-10406" );
   }


   /**
    * <p>
    * Given the Preconditions:
    *
    * <ul>
    * <li>Part number is known</li>
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
    * <li>Error Code: AML-10416</li>
    * <li>INVENTORY_USAGE field TSO_QT must not be negative.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10416_NegativeTSO_Usage() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'SN-10023'" ); // field not provided
      lMapInventory.put( "PART_NO_OEM", "'A0000005'" );
      lMapInventory.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      // lMapInventory.put( "INV_COND_CD", "'RFI'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "ASSMBL_CD", "'ACFT_CD1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // create task map
      Map<String, String> lMapInventoryUsage = new LinkedHashMap<String, String>();

      lMapInventoryUsage.put( "SERIAL_NO_OEM", "'SN-10023'" );
      lMapInventoryUsage.put( "PART_NO_OEM", "'A0000005'" );
      lMapInventoryUsage.put( "MANUFACT_CD", "'10001'" );
      lMapInventoryUsage.put( "DATA_TYPE_CD", "'HOURS'" );
      lMapInventoryUsage.put( "TSN_QT", "0" );
      lMapInventoryUsage.put( "TSI_QT", "0" );
      lMapInventoryUsage.put( "TSO_QT", "-1" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lMapInventoryUsage ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10416_NegativeTSO_Usage", "AML-10416" );
   }


   /**
    * <p>
    * Given the Preconditions:
    *
    * <ul>
    * <li>Part number is known</li>
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
    * <li>Error Code: AML-10417</li>
    * <li>INVENTORY_USAGE field TSN_QT must not be negative.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10417_NegativeTSN_Usage() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'SN-10023'" ); // field not provided
      lMapInventory.put( "PART_NO_OEM", "'A0000005'" );
      lMapInventory.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      // lMapInventory.put( "INV_COND_CD", "'RFI'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "ASSMBL_CD", "'ACFT_CD1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // create task map
      Map<String, String> lMapInventoryUsage = new LinkedHashMap<String, String>();

      lMapInventoryUsage.put( "SERIAL_NO_OEM", "'SN-10023'" );
      lMapInventoryUsage.put( "PART_NO_OEM", "'A0000005'" );
      lMapInventoryUsage.put( "MANUFACT_CD", "'10001'" );
      lMapInventoryUsage.put( "DATA_TYPE_CD", "'HOURS'" );
      lMapInventoryUsage.put( "TSN_QT", "-1" );
      lMapInventoryUsage.put( "TSI_QT", "0" );
      lMapInventoryUsage.put( "TSO_QT", "0" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lMapInventoryUsage ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10417_NegativeTSN_Usage", "AML-10417" );
   }


   /**
    * <p>
    * Given the Preconditions:
    *
    * <ul>
    * <li>Part number is known</li>
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
    * <li>Error Code: AML-10418</li>
    * <li>INVENTORY_USAGE field TSI_QT must not be negative.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10418_NegativeTSI_Usage() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'SN-10023'" ); // field not provided
      lMapInventory.put( "PART_NO_OEM", "'A0000005'" );
      lMapInventory.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      // lMapInventory.put( "INV_COND_CD", "'RFI'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "ASSMBL_CD", "'ACFT_CD1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // create task map
      Map<String, String> lMapInventoryUsage = new LinkedHashMap<String, String>();

      lMapInventoryUsage.put( "SERIAL_NO_OEM", "'SN-10023'" );
      lMapInventoryUsage.put( "PART_NO_OEM", "'A0000005'" );
      lMapInventoryUsage.put( "MANUFACT_CD", "'10001'" );
      lMapInventoryUsage.put( "DATA_TYPE_CD", "'HOURS'" );
      lMapInventoryUsage.put( "TSN_QT", "0" );
      lMapInventoryUsage.put( "TSI_QT", "-1" );
      lMapInventoryUsage.put( "TSO_QT", "0" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lMapInventoryUsage ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10418_NegativeTSI_Usage", "AML-10418" );
   }


   /**
    * <p>
    * Given the Preconditions:
    *
    * <ul>
    * <li>Part number is known</li>
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
    * <li>Error Code: AML-10442</li>
    * <li>C_PROC_RI_INVENTORY_USAGE.TSN_QT must be greater than or equal to TSO_QT.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10442_TSNLessThanTSO_Usage() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'SN-10023'" ); // field not provided
      lMapInventory.put( "PART_NO_OEM", "'A0000005'" );
      lMapInventory.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      // lMapInventory.put( "INV_COND_CD", "'RFI'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "ASSMBL_CD", "'ACFT_CD1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // create task map
      Map<String, String> lMapInventoryUsage = new LinkedHashMap<String, String>();

      lMapInventoryUsage.put( "SERIAL_NO_OEM", "'SN-10023'" );
      lMapInventoryUsage.put( "PART_NO_OEM", "'A0000005'" );
      lMapInventoryUsage.put( "MANUFACT_CD", "'10001'" );
      lMapInventoryUsage.put( "DATA_TYPE_CD", "'HOURS'" );
      lMapInventoryUsage.put( "TSN_QT", "0" );
      lMapInventoryUsage.put( "TSI_QT", "0" );
      lMapInventoryUsage.put( "TSO_QT", "1" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lMapInventoryUsage ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10442_TSNLessThanTSO_Usage", "AML-10442" );
   }


   /**
    * <p>
    * Given the Preconditions:
    *
    * <ul>
    * <li>Part number is known</li>
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
    * <li>Error Code: AML-10508</li>
    * <li>Mandatory field missing : TSN_QT.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10508_MissingTSN_Usage() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'SN-10023'" ); // field not provided
      lMapInventory.put( "PART_NO_OEM", "'A0000005'" );
      lMapInventory.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      // lMapInventory.put( "INV_COND_CD", "'RFI'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "ASSMBL_CD", "'ACFT_CD1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // create task map
      Map<String, String> lMapInventoryUsage = new LinkedHashMap<String, String>();

      lMapInventoryUsage.put( "SERIAL_NO_OEM", "'SN-10023'" );
      lMapInventoryUsage.put( "PART_NO_OEM", "'A0000005'" );
      lMapInventoryUsage.put( "MANUFACT_CD", "'10001'" );
      lMapInventoryUsage.put( "DATA_TYPE_CD", "'HOURS'" );

      // lMapInventoryUsage.put( "TSN_QT", "1" ); // not provided
      lMapInventoryUsage.put( "TSI_QT", "0" );
      lMapInventoryUsage.put( "TSO_QT", "0" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lMapInventoryUsage ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10508_MissingTSN_Usage", "AML-10508" );
   }


   /**
    * <p>
    * Given the Preconditions:
    *
    * <ul>
    * <li>Part number is known</li>
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
    * <li>Error Code: TBD-10247</li>
    * <li>The provided DATA_TYPE_CD is not a usage-type parameter in Maintenix.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_TBD_10247_NonUsageTypeParameter() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'SN-10023'" ); // field not provided
      lMapInventory.put( "PART_NO_OEM", "'A0000005'" );
      lMapInventory.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      // lMapInventory.put( "INV_COND_CD", "'RFI'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "ASSMBL_CD", "'ACFT_CD1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // create task map
      Map<String, String> lMapInventoryUsage = new LinkedHashMap<String, String>();

      lMapInventoryUsage.put( "SERIAL_NO_OEM", "'SN-10023'" );
      lMapInventoryUsage.put( "PART_NO_OEM", "'A0000005'" );
      lMapInventoryUsage.put( "MANUFACT_CD", "'10001'" );
      lMapInventoryUsage.put( "DATA_TYPE_CD", "'CYR'" );
      lMapInventoryUsage.put( "TSN_QT", "1" );
      lMapInventoryUsage.put( "TSI_QT", "1" );
      lMapInventoryUsage.put( "TSO_QT", "1" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lMapInventoryUsage ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_TBD_10247_NonUsageTypeParameter", "TBD-10247" );
   }


   /**
    * <p>
    * Given the Preconditions:
    *
    * <ul>
    * <li>Part number is known</li>
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
    * <li>Error Code: USG-00001</li>
    * <li>Missing data types that apply to this part number.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_USG_00001_MissingUsageParm() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-SN'" );
      lMapInventory.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventory.put( "INV_CLASS_CD", "'ACFT'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventory.put( "AC_REG_CD", "'ACREG1'" ); // field not provided
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventoryNotIgnoreUSG( "test_USG_00001_MissingUsageParm", "USG-00001" );
   }


   /**
    * <p>
    * Given the Preconditions:
    *
    * <ul>
    * <li>Part number is not known</li>
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
    * <li>Error Code: USG-00002</li>
    * <li>Missing data types that apply to this part number.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_USG_00002_MissingUsageParm_Sub() throws Exception {

      // create map
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

      // create map
      Map<String, String> lInventoryUsage = new LinkedHashMap<String, String>();
      lInventoryUsage.put( "SERIAL_NO_OEM", "'ACFT-SN'" );
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

      // create map
      Map<String, String> lMapInventorySub = new LinkedHashMap<String, String>();

      lMapInventorySub.put( "PARENT_SERIAL_NO_OEM", "'ACFT-SN'" );
      lMapInventorySub.put( "PARENT_PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventorySub.put( "PARENT_MANUFACT_CD", "'10001'" );
      lMapInventorySub.put( "BOM_PART_CD", "'ACFT-SYS-1-1-TRK-P1'" );
      lMapInventorySub.put( "EQP_POS_CD", "'1'" );
      lMapInventorySub.put( "SERIAL_NO_OEM", "'TRK-001'" );
      lMapInventorySub.put( "PART_NO_OEM", "'A0000001'" );
      lMapInventorySub.put( "MANUFACT_CD", "'10001'" );
      lMapInventorySub.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventorySub.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventorySub ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventoryNotIgnoreUSG( "test_USG_00002_MissingUsageParm_Sub", "USG-00002" );
   }


   /**
    * This test is to verify valid error code CLD_00002:Cannot load record because the related
    * record in C_RI_INVENTORY_USAGE is invalid
    *
    *
    */

   @Test
   public void test_CLD_00002() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'SN-10023'" ); // field not provided
      lMapInventory.put( "PART_NO_OEM", "'A0000005'" );
      lMapInventory.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      // lMapInventory.put( "INV_COND_CD", "'RFI'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "ASSMBL_CD", "'ACFT_CD1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // create task map
      Map<String, String> lMapInventoryUsage = new LinkedHashMap<String, String>();

      lMapInventoryUsage.put( "SERIAL_NO_OEM", "'SN-10023'" );
      lMapInventoryUsage.put( "PART_NO_OEM", "'A0000005'" );
      lMapInventoryUsage.put( "MANUFACT_CD", "'10001'" );
      // lMapInventoryUsage.put( "DATA_TYPE_CD", "'HOURS'" );
      lMapInventoryUsage.put( "DATA_TYPE_CD", "'CYR'" );
      lMapInventoryUsage.put( "TSN_QT", "0" );
      lMapInventoryUsage.put( "TSI_QT", "0" );
      lMapInventoryUsage.put( "TSO_QT", "0" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lMapInventoryUsage ) );

      // // call inventory validation and check result against expectation
      // validateAndCheckInventory( "test_AML_10416_NegativeTSO_Usage", "AML-10416" );

      // run validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // check error code
      checkInventoryErrorCode( testName.getMethodName(), "CLD-00002" );
   }


   /**
    * This test is to verify valid error code CLD_00001:Cannot load record because the related
    * record in C_RI_INVENTORY_USAGE is invalid
    *
    *
    */
   @Test
   public void test_CLD_00001() throws Exception {

      String lParentNo = "TT-PARENT";
      String lChildNo = "TT-CHILD";

      // create task map
      Map<String, String> lMapInventoryParent = new LinkedHashMap<String, String>();
      lMapInventoryParent.put( "SERIAL_NO_OEM", "'" + lParentNo + "'" );
      lMapInventoryParent.put( "PART_NO_OEM", "'A0000005'" );
      lMapInventoryParent.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventoryParent.put( "MANUFACT_CD", "'10001'" );
      lMapInventoryParent.put( "LOC_CD", "'OPS'" );
      // lMapInventoryParent.put( "INV_COND_CD", "'RFI'" );
      lMapInventoryParent.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventoryParent.put( "ASSMBL_CD", "'ACFT_CD1'" );
      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventoryParent ) );

      // usage
      Map<String, String> lParentInventoryUsage = new LinkedHashMap<String, String>();
      lParentInventoryUsage.put( "SERIAL_NO_OEM", "'" + lParentNo + "'" );
      lParentInventoryUsage.put( "PART_NO_OEM", "'A0000005'" );
      lParentInventoryUsage.put( "MANUFACT_CD", "'10001'" );
      lParentInventoryUsage.put( "DATA_TYPE_CD", "'HOURS'" );
      lParentInventoryUsage.put( "TSN_QT", "1" );
      lParentInventoryUsage.put( "TSO_QT", "1" );
      lParentInventoryUsage.put( "TSI_QT", "1" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lParentInventoryUsage ) );

      // insert row for each usage parm
      lParentInventoryUsage.put( "DATA_TYPE_CD", "'CYCLES'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lParentInventoryUsage ) );
      lParentInventoryUsage.put( "DATA_TYPE_CD", "'USAGE1'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lParentInventoryUsage ) );
      lParentInventoryUsage.put( "DATA_TYPE_CD", "'USAGE2'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lParentInventoryUsage ) );
      lParentInventoryUsage.put( "DATA_TYPE_CD", "'USAGE3'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lParentInventoryUsage ) );

      // create task map
      Map<String, String> lMapInventoryChild = new LinkedHashMap<String, String>();
      lMapInventoryChild.put( "PARENT_SERIAL_NO_OEM", "'" + lParentNo + "'" );
      lMapInventoryChild.put( "PARENT_PART_NO_OEM", "'A0000005'" );
      lMapInventoryChild.put( "PARENT_MANUFACT_CD", "'10001'" );
      lMapInventoryChild.put( "BOM_PART_CD", "'ACFT-SYS-1-1-TRK-TRK-CHILD'" );
      lMapInventoryChild.put( "EQP_POS_CD", "'1'" );
      lMapInventoryChild.put( "SERIAL_NO_OEM", "'" + lChildNo + "'" );
      lMapInventoryChild.put( "PART_NO_OEM", "'A0000006'" );
      lMapInventoryChild.put( "MANUFACT_CD", "'11111'" );
      lMapInventoryChild.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventoryChild.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventoryChild ) );

      // usage
      Map<String, String> lChildInventoryUsage = new LinkedHashMap<String, String>();
      lChildInventoryUsage.put( "SERIAL_NO_OEM", "'" + lChildNo + "'" );
      lChildInventoryUsage.put( "PART_NO_OEM", "'A0000006'" );
      lChildInventoryUsage.put( "MANUFACT_CD", "'11111'" );
      lChildInventoryUsage.put( "DATA_TYPE_CD", "'HOUR'" );
      lChildInventoryUsage.put( "TSN_QT", "1" );
      lChildInventoryUsage.put( "TSO_QT", "1" );
      lChildInventoryUsage.put( "TSI_QT", "1" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lChildInventoryUsage ) );

      // run validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // check error code
      checkInventoryErrorCode( testName.getMethodName(), "CLD-00001" );
   }

}
