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
import org.junit.Before;
import org.junit.Test;

import com.mxi.mx.util.ActualsLoaderTest;
import com.mxi.mx.util.TableUtil;


/**
 * This test suite contains all test cases for Actuals Loader loading of historic tasks
 *
 * @author Andrew Bruce
 */

public class LooseInventoryTest extends ActualsLoaderTest {

   /**
    * Clean up after each individual test
    *
    * @throws Exception
    */
   @After
   @Override
   public void after() throws Exception {

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
   }


   /**
    * <p>
    * Given the Preconditions:
    *
    * <ul>
    * <li>The part numbers exists in maintenix</li>
    * <li>The TRK-on-TRK configurations are defined in maintenix</li>
    * </ul>
    * </p>
    *
    * <p>
    * The test will:
    *
    * <ul>
    * <li>SUB_INVENTORY item references a position on a configuration slot that is also referenced
    * by another record. The combination of PARENT_SERIAL_NO_OEM, PARENT_PART_NO_OEM,
    * PARENT_MANUFACT_CD, BOM_PART_CD, and EQP_POS_CD must be unique across both the attachment and
    * subinventory extracts.</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Error Code: AML-10111</li>
    * <li>SUB_INVENTORY field BOM_PART_CD is not a unique Configuration Slot on the SUB_INVENTORY
    * item Parent Assembly.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    */
   @Test
   public void test_AML_10111_MultipleSubInventoryIntoSingleConfigSlot() throws Exception {

      // create task map
      Map<String, String> lInventoryParent = new LinkedHashMap<String, String>();
      lInventoryParent.put( "SERIAL_NO_OEM", "'PARENT1'" );
      lInventoryParent.put( "PART_NO_OEM", "'A0000005'" );
      lInventoryParent.put( "INV_CLASS_CD", "'TRK'" );
      lInventoryParent.put( "MANUFACT_CD", "'10001'" );
      lInventoryParent.put( "LOC_CD", "'OPS'" );
      lInventoryParent.put( "INV_COND_CD", "'RFI'" );
      lInventoryParent.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lInventoryParent.put( "ASSMBL_CD", "'ACFT_CD1'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lInventoryParent ) );

      // create task map
      Map<String, String> lInventoryChild = new LinkedHashMap<String, String>();
      lInventoryChild.put( "PARENT_SERIAL_NO_OEM", "'PARENT1'" );
      lInventoryChild.put( "PARENT_PART_NO_OEM", "'A0000005'" );
      lInventoryChild.put( "PARENT_MANUFACT_CD", "'10001'" );
      lInventoryChild.put( "BOM_PART_CD", "'ACFT-SYS-1-1-TRK-TRK-CHILD'" );
      lInventoryChild.put( "EQP_POS_CD", "'1'" );
      lInventoryChild.put( "SERIAL_NO_OEM", "'CHILD1'" );
      lInventoryChild.put( "PART_NO_OEM", "'A0000006'" );
      lInventoryChild.put( "MANUFACT_CD", "'11111'" );
      lInventoryChild.put( "INV_CLASS_CD", "'TRK'" );
      lInventoryChild.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lInventoryChild ) );

      lInventoryChild.put( "SERIAL_NO_OEM", "'CHILD2'" );
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lInventoryChild ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10111_MultipleSubInventoryIntoSingleConfigSlot",
            "AML-10111" );
   }


   /**
    * <p>
    * Given the Preconditions:
    *
    * <ul>
    * <li>The part numbers exists in maintenix</li>
    * <li>The TRK configuration is defined in maintenix</li>
    * </ul>
    * </p>
    *
    * <p>
    * The test will:
    *
    * <ul>
    * <li>Insert inventory to the staging tables</li>
    * <li>Run Inventory Validation</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Error Code: NONE</li>
    * <li>No validation errors are shown if you try to load loose TRK inventory</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    */

   @Test
   public void testASSYPassesValidation() throws Exception {
      String lASSYNo = "ENG";

      // create task map
      Map<String, String> lInventory = new LinkedHashMap<String, String>();
      lInventory.put( "SERIAL_NO_OEM", "'" + lASSYNo + "'" );
      lInventory.put( "PART_NO_OEM", "'   ENG_ASSY_PN1'" );
      lInventory.put( "INV_CLASS_CD", "'ASSY'" );
      lInventory.put( "MANUFACT_CD", "'ABC11'" );
      lInventory.put( "LOC_CD", "'OPS'" );
      lInventory.put( "INV_COND_CD", "'REPREQ'" );
      lInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lInventory.put( "ASSMBL_CD", "'ENG_CD1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lInventory ) );

      // create task map
      Map<String, String> lInventoryUsage = new LinkedHashMap<String, String>();
      lInventoryUsage.put( "SERIAL_NO_OEM", "'" + lASSYNo + "'" );
      lInventoryUsage.put( "PART_NO_OEM", "'ENG_ASSY_PN1'" );
      lInventoryUsage.put( "MANUFACT_CD", "'ABC11'" );
      lInventoryUsage.put( "DATA_TYPE_CD", "'ECYC'" );
      lInventoryUsage.put( "TSN_QT", "1" );
      lInventoryUsage.put( "TSO_QT", "1" );
      lInventoryUsage.put( "TSI_QT", "1" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE, lInventoryUsage ) );

      // insert row for each usage parm
      lInventoryUsage.put( "DATA_TYPE_CD", "'EOT'" );
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
      lInventoryUsage.put( "DATA_TYPE_CD", "'HOURS'" );
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE, lInventoryUsage ) );
      lInventoryUsage.put( "DATA_TYPE_CD", "'CYCLES'" );
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE, lInventoryUsage ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "testASSYPassesValidation", "PASS" );
   }


   /**
    * <p>
    * Given the Preconditions:
    *
    * <ul>
    * <li>The part numbers exists in maintenix</li>
    * <li>The BATCH configuration is defined in maintenix</li>
    * </ul>
    * </p>
    *
    * <p>
    * The test will:
    *
    * <ul>
    * <li>Insert inventory to the staging tables</li>
    * <li>Run Inventory Validation</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Error Code: NONE</li>
    * <li>No validation errors are shown if you try to load loose BATCH inventory</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    */

   @Test
   public void testBATCHPassesValidation() throws Exception {

      String lBatchNo = "BATCH";

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();
      lMapInventory.put( "SERIAL_NO_OEM", "'" + lBatchNo + "'" );
      lMapInventory.put( "BATCH_NO_OEM", "'" + lBatchNo + "'" );
      lMapInventory.put( "PART_NO_OEM", "'A0000009'" );
      lMapInventory.put( "INV_CLASS_CD", "'BATCH'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "OWNER_CD", "'MXI'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "BIN_QT", "1" );
      lMapInventory.put( "INV_COND_CD", "'RFI'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "testBATCHPassesValidation", "PASS" );
   }


   /**
    * <p>
    * Given the Preconditions:
    *
    * <ul>
    * <li>The part numbers exists in maintenix</li>
    * <li>The TRK-on-TRK configurations are defined in maintenix</li>
    * </ul>
    * </p>
    *
    * <p>
    * The test will:
    *
    * <ul>
    * <li>Insert multiple inventory and sub-inventory to the processing tables</li>
    * <li>Run Inventory Validation by calling the validate_maintenix_data directly</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Error Code: NONE</li>
    * <li>No validation errors are shown if you try to validate multiple loose TRK-on-TRK
    * inventory</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    */
   @Test
   public void testMultipleTRKonTRKPassesValidation() throws Exception {

      // create task map
      Map<String, String> lInventoryParent = new LinkedHashMap<String, String>();
      lInventoryParent.put( "SERIAL_NO_OEM", "'PARENT1'" );
      lInventoryParent.put( "PART_NO_OEM", "'A0000005'" );
      lInventoryParent.put( "INV_CLASS_CD", "'TRK'" );
      lInventoryParent.put( "MANUFACT_CD", "'10001'" );
      lInventoryParent.put( "LOC_CD", "'OPS'" );
      lInventoryParent.put( "INV_COND_CD", "'RFI'" );
      lInventoryParent.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lInventoryParent.put( "ASSMBL_CD", "'ACFT_CD1'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lInventoryParent ) );

      lInventoryParent.put( "SERIAL_NO_OEM", "'PARENT2'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lInventoryParent ) );

      // create task map
      Map<String, String> lInventoryParentUsage = new LinkedHashMap<String, String>();
      lInventoryParentUsage.put( "SERIAL_NO_OEM", "'PARENT1'" );
      lInventoryParentUsage.put( "PART_NO_OEM", "'A0000005'" );
      lInventoryParentUsage.put( "MANUFACT_CD", "'10001'" );
      lInventoryParentUsage.put( "DATA_TYPE_CD", "'HOURS'" );
      lInventoryParentUsage.put( "TSN_QT", "1" );
      lInventoryParentUsage.put( "TSO_QT", "1" );
      lInventoryParentUsage.put( "TSI_QT", "1" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventoryParentUsage ) );

      // insert row for each usage parm
      lInventoryParentUsage.put( "DATA_TYPE_CD", "'CYCLES'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventoryParentUsage ) );
      lInventoryParentUsage.put( "DATA_TYPE_CD", "'USAGE1'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventoryParentUsage ) );
      lInventoryParentUsage.put( "DATA_TYPE_CD", "'USAGE2'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventoryParentUsage ) );
      lInventoryParentUsage.put( "DATA_TYPE_CD", "'USAGE3'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventoryParentUsage ) );

      lInventoryParentUsage.put( "SERIAL_NO_OEM", "'PARENT2'" );
      lInventoryParentUsage.put( "DATA_TYPE_CD", "'HOURS'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventoryParentUsage ) );

      // insert row for each usage parm
      lInventoryParentUsage.put( "DATA_TYPE_CD", "'CYCLES'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventoryParentUsage ) );
      lInventoryParentUsage.put( "DATA_TYPE_CD", "'USAGE1'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventoryParentUsage ) );
      lInventoryParentUsage.put( "DATA_TYPE_CD", "'USAGE2'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventoryParentUsage ) );
      lInventoryParentUsage.put( "DATA_TYPE_CD", "'USAGE3'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventoryParentUsage ) );

      // create task map
      Map<String, String> lInventoryChild = new LinkedHashMap<String, String>();
      lInventoryChild.put( "PARENT_SERIAL_NO_OEM", "'PARENT1'" );
      lInventoryChild.put( "PARENT_PART_NO_OEM", "'A0000005'" );
      lInventoryChild.put( "PARENT_MANUFACT_CD", "'10001'" );
      lInventoryChild.put( "BOM_PART_CD", "'ACFT-SYS-1-1-TRK-TRK-CHILD'" );
      lInventoryChild.put( "EQP_POS_CD", "'1'" );
      lInventoryChild.put( "SERIAL_NO_OEM", "'CHILD1'" );
      lInventoryChild.put( "PART_NO_OEM", "'A0000006'" );
      lInventoryChild.put( "MANUFACT_CD", "'11111'" );
      lInventoryChild.put( "INV_CLASS_CD", "'TRK'" );
      lInventoryChild.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lInventoryChild ) );

      lInventoryChild.put( "PARENT_SERIAL_NO_OEM", "'PARENT2'" );
      lInventoryChild.put( "SERIAL_NO_OEM", "'CHILD2'" );
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lInventoryChild ) );

      // create task map
      Map<String, String> lInventoryChildUsage = new LinkedHashMap<String, String>();
      lInventoryChildUsage.put( "SERIAL_NO_OEM", "'CHILD1'" );
      lInventoryChildUsage.put( "PART_NO_OEM", "'A0000006'" );
      lInventoryChildUsage.put( "MANUFACT_CD", "'11111'" );
      lInventoryChildUsage.put( "DATA_TYPE_CD", "'HOURS'" );
      lInventoryChildUsage.put( "TSN_QT", "1" );
      lInventoryChildUsage.put( "TSO_QT", "1" );
      lInventoryChildUsage.put( "TSI_QT", "1" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventoryChildUsage ) );

      // insert row for each usage parm
      lInventoryChildUsage.put( "DATA_TYPE_CD", "'CYCLES'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventoryChildUsage ) );
      lInventoryChildUsage.put( "DATA_TYPE_CD", "'USAGE1'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventoryChildUsage ) );
      lInventoryChildUsage.put( "DATA_TYPE_CD", "'USAGE2'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventoryChildUsage ) );
      lInventoryChildUsage.put( "DATA_TYPE_CD", "'USAGE3'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventoryChildUsage ) );

      lInventoryChildUsage.put( "SERIAL_NO_OEM", "'CHILD2'" );
      lInventoryChildUsage.put( "DATA_TYPE_CD", "'HOURS'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventoryChildUsage ) );

      // insert row for each usage parm
      lInventoryChildUsage.put( "DATA_TYPE_CD", "'CYCLES'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventoryChildUsage ) );
      lInventoryChildUsage.put( "DATA_TYPE_CD", "'USAGE1'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventoryChildUsage ) );
      lInventoryChildUsage.put( "DATA_TYPE_CD", "'USAGE2'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventoryChildUsage ) );
      lInventoryChildUsage.put( "DATA_TYPE_CD", "'USAGE3'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventoryChildUsage ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "testMultipleTRKonTRKPassesValidation", "PASS" );
   }


   /**
    * <p>
    * Given the Preconditions:
    *
    * <ul>
    * <li>The part numbers exists in maintenix</li>
    * <li>The SER configuration is defined in maintenix</li>
    * </ul>
    * </p>
    *
    * <p>
    * The test will:
    *
    * <ul>
    * <li>Insert inventory to the staging tables</li>
    * <li>Run Inventory Validation</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Error Code: NONE</li>
    * <li>No validation errors are shown if you try to load loose SER inventory</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    */
   @Test
   public void testSERPassesValidation() throws Exception {

      String lSERNo = "SER";

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();
      lMapInventory.put( "SERIAL_NO_OEM", "'" + lSERNo + "'" );
      lMapInventory.put( "PART_NO_OEM", "'A0000012'" );
      lMapInventory.put( "INV_CLASS_CD", "'SER'" );
      lMapInventory.put( "MANUFACT_CD", "'1234567890'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'RFI'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // create task map
      Map<String, String> lInventoryUsage = new LinkedHashMap<String, String>();
      lInventoryUsage.put( "SERIAL_NO_OEM", "'" + lSERNo + "'" );
      lInventoryUsage.put( "PART_NO_OEM", "'A0000012'" );
      lInventoryUsage.put( "MANUFACT_CD", "'1234567890'" );
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

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "testSERPassesValidation", "PASS" );
   }


   /**
    * <p>
    * Given the Preconditions:
    *
    * <ul>
    * <li>The part numbers exists in maintenix</li>
    * <li>The TRK-on-TRK-on-TRK configurations are defined in maintenix</li>
    * </ul>
    * </p>
    *
    * <p>
    * The test will:
    *
    * <ul>
    * <li>Insert inventory and sub-inventory to the processing tables</li>
    * <li>Run Inventory Validation</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Error Code: NONE</li>
    * <li>No validation errors are shown if you try to validate loose TRK-on-TRK inventory</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    */

   @Test
   public void testTRKonTRKonTRKPassesValidation() throws Exception {

      // create task map
      Map<String, String> lInventoryParent = new LinkedHashMap<String, String>();
      lInventoryParent.put( "SERIAL_NO_OEM", "'TTT-PARENT'" );
      lInventoryParent.put( "PART_NO_OEM", "'A0000020'" );
      lInventoryParent.put( "INV_CLASS_CD", "'TRK'" );
      lInventoryParent.put( "MANUFACT_CD", "'10001'" );
      lInventoryParent.put( "LOC_CD", "'OPS'" );
      lInventoryParent.put( "INV_COND_CD", "'RFI'" );
      lInventoryParent.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lInventoryParent.put( "ASSMBL_CD", "'ACFT_CD1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lInventoryParent ) );

      // create task map
      Map<String, String> lInventoryChild = new LinkedHashMap<String, String>();
      lInventoryChild.put( "PARENT_SERIAL_NO_OEM", "'TTT-PARENT'" );
      lInventoryChild.put( "PARENT_PART_NO_OEM", "'A0000020'" );
      lInventoryChild.put( "PARENT_MANUFACT_CD", "'10001'" );
      lInventoryChild.put( "BOM_PART_CD", "'ACFT-SYS-1-1-TRK-TRK-TRK-MID'" );
      lInventoryChild.put( "EQP_POS_CD", "'1.1.1'" );
      lInventoryChild.put( "SERIAL_NO_OEM", "'TTT-MIDDLE'" );
      lInventoryChild.put( "PART_NO_OEM", "'A0000021'" );
      lInventoryChild.put( "MANUFACT_CD", "'10001'" );
      lInventoryChild.put( "INV_CLASS_CD", "'TRK'" );
      lInventoryChild.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lInventoryChild ) );

      // create task map
      Map<String, String> lInventoryChildOfChild = new LinkedHashMap<String, String>();
      lInventoryChildOfChild.put( "PARENT_SERIAL_NO_OEM", "'TTT-PARENT'" );
      lInventoryChildOfChild.put( "PARENT_PART_NO_OEM", "'A0000020'" );
      lInventoryChildOfChild.put( "PARENT_MANUFACT_CD", "'10001'" );
      lInventoryChildOfChild.put( "BOM_PART_CD", "'ACFT-SYS-1-1-TRK-TRK-TRK-CHILD'" );
      lInventoryChildOfChild.put( "EQP_POS_CD", "'1.1.1.1'" );
      lInventoryChildOfChild.put( "SERIAL_NO_OEM", "'TTT-CHILD'" );
      lInventoryChildOfChild.put( "PART_NO_OEM", "'A0000022'" );
      lInventoryChildOfChild.put( "MANUFACT_CD", "'10001'" );
      lInventoryChildOfChild.put( "INV_CLASS_CD", "'TRK'" );
      lInventoryChildOfChild.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB,
            lInventoryChildOfChild ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "testTRKonTRKonTRKPassesValidation", "PASS" );
   }


   /**
    * <p>
    * Given the Preconditions:
    *
    * <ul>
    * <li>The part numbers exists in maintenix</li>
    * <li>The TRK-on-TRK configurations are defined in maintenix</li>
    * </ul>
    * </p>
    *
    * <p>
    * The test will:
    *
    * <ul>
    * <li>Insert inventory and sub-inventory to the processing tables</li>
    * <li>Run Inventory Validation by calling the validate_maintenix_data directly</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Error Code: NONE</li>
    * <li>No validation errors are shown if you try to validate loose TRK-on-TRK inventory</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    */
   @Test
   public void testTRKonTRKPassesValidation() throws Exception {

      // create task map
      Map<String, String> lInventoryParent = new LinkedHashMap<String, String>();
      lInventoryParent.put( "SERIAL_NO_OEM", "'TT-PARENT'" );
      lInventoryParent.put( "PART_NO_OEM", "'A0000005'" );
      lInventoryParent.put( "INV_CLASS_CD", "'TRK'" );
      lInventoryParent.put( "MANUFACT_CD", "'10001'" );
      lInventoryParent.put( "LOC_CD", "'OPS'" );
      lInventoryParent.put( "INV_COND_CD", "'RFI'" );
      lInventoryParent.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lInventoryParent.put( "ASSMBL_CD", "'ACFT_CD1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lInventoryParent ) );

      // create task map
      Map<String, String> lInventoryParentUsage = new LinkedHashMap<String, String>();
      lInventoryParentUsage.put( "SERIAL_NO_OEM", "'TT-PARENT'" );
      lInventoryParentUsage.put( "PART_NO_OEM", "'A0000005'" );
      lInventoryParentUsage.put( "MANUFACT_CD", "'10001'" );
      lInventoryParentUsage.put( "DATA_TYPE_CD", "'HOURS'" );
      lInventoryParentUsage.put( "TSN_QT", "1" );
      lInventoryParentUsage.put( "TSO_QT", "1" );
      lInventoryParentUsage.put( "TSI_QT", "1" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventoryParentUsage ) );

      // insert row for each usage parm
      lInventoryParentUsage.put( "DATA_TYPE_CD", "'CYCLES'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventoryParentUsage ) );
      lInventoryParentUsage.put( "DATA_TYPE_CD", "'USAGE1'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventoryParentUsage ) );
      lInventoryParentUsage.put( "DATA_TYPE_CD", "'USAGE2'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventoryParentUsage ) );
      lInventoryParentUsage.put( "DATA_TYPE_CD", "'USAGE3'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventoryParentUsage ) );

      // create task map
      Map<String, String> lInventoryChild = new LinkedHashMap<String, String>();
      lInventoryChild.put( "PARENT_SERIAL_NO_OEM", "'TT-PARENT'" );
      lInventoryChild.put( "PARENT_PART_NO_OEM", "'A0000005'" );
      lInventoryChild.put( "PARENT_MANUFACT_CD", "'10001'" );
      lInventoryChild.put( "BOM_PART_CD", "'ACFT-SYS-1-1-TRK-TRK-CHILD'" );
      lInventoryChild.put( "EQP_POS_CD", "'1'" );
      lInventoryChild.put( "SERIAL_NO_OEM", "'TT-CHILD'" );
      lInventoryChild.put( "PART_NO_OEM", "'A0000006'" );
      lInventoryChild.put( "MANUFACT_CD", "'11111'" );
      lInventoryChild.put( "INV_CLASS_CD", "'TRK'" );
      lInventoryChild.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lInventoryChild ) );

      // create task map
      Map<String, String> lInventoryChildUsage = new LinkedHashMap<String, String>();
      lInventoryChildUsage.put( "SERIAL_NO_OEM", "'TT-CHILD'" );
      lInventoryChildUsage.put( "PART_NO_OEM", "'A0000006'" );
      lInventoryChildUsage.put( "MANUFACT_CD", "'11111'" );
      lInventoryChildUsage.put( "DATA_TYPE_CD", "'HOURS'" );
      lInventoryChildUsage.put( "TSN_QT", "1" );
      lInventoryChildUsage.put( "TSO_QT", "1" );
      lInventoryChildUsage.put( "TSI_QT", "1" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventoryChildUsage ) );

      // insert row for each usage parm
      lInventoryChildUsage.put( "DATA_TYPE_CD", "'CYCLES'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventoryChildUsage ) );
      lInventoryChildUsage.put( "DATA_TYPE_CD", "'USAGE1'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventoryChildUsage ) );
      lInventoryChildUsage.put( "DATA_TYPE_CD", "'USAGE2'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventoryChildUsage ) );
      lInventoryChildUsage.put( "DATA_TYPE_CD", "'USAGE3'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventoryChildUsage ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "testTRKonTRKPassesValidation", "PASS" );
   }


   /**
    * <p>
    * Given the Preconditions:
    *
    * <ul>
    * <li>The part numbers exists in maintenix</li>
    * <li>The TRK configuration is defined in maintenix</li>
    * </ul>
    * </p>
    *
    * <p>
    * The test will:
    *
    * <ul>
    * <li>Insert inventory to the staging tables</li>
    * <li>Run Inventory Validation</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Error Code: NONE</li>
    * <li>No validation errors are shown if you try to load loose TRK inventory</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    */

   @Test
   public void testTRKPassesValidation() throws Exception {
      String lTRKNo = "TRK";

      // create task map
      Map<String, String> lInventory = new LinkedHashMap<String, String>();
      lInventory.put( "SERIAL_NO_OEM", "'" + lTRKNo + "'" );
      lInventory.put( "PART_NO_OEM", "'A0000005'" );
      lInventory.put( "INV_CLASS_CD", "'TRK'" );
      lInventory.put( "MANUFACT_CD", "'10001'" );
      lInventory.put( "LOC_CD", "'OPS'" );
      lInventory.put( "INV_COND_CD", "'RFI'" );
      lInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lInventory.put( "ASSMBL_CD", "'ACFT_CD1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lInventory ) );

      // create task map
      Map<String, String> lInventoryUsage = new LinkedHashMap<String, String>();
      lInventoryUsage.put( "SERIAL_NO_OEM", "'" + lTRKNo + "'" );
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

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "testTRKPassesValidation", "PASS" );
   }
}
