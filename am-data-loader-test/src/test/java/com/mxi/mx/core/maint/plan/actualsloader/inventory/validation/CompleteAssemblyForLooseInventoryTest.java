/*
 * Confidential, proprietary and/or trade secret information of Mxi Technologies, Ltd.
 *
 * Copyright 2000-2015 Mxi Technologies, Ltd. All Rights Reserved.
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
 * This test suite contains all test cases for Actuals Loader loading of historic tasks
 *
 * @author Andrew Bruce, Alicia Qian
 */

public class CompleteAssemblyForLooseInventoryTest extends ActualsLoaderTest {

   @Rule
   public TestName testName = new TestName();


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
      clearActualsLoaderTables();
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
    * <li>Insert inventory and sub-inventory to the staging tables</li>
    * <li>Run Inventory Validation</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Error Code: NONE</li>
    * <li>No validation errors are shown if you try to load loose TRK-on-TRK inventory</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    */

   @Test
   public void testTRKonMissingTRKonTRKPassesValidation() throws Exception {

      String lParentNo = "TTT-PARENT";
      String lChildNo = "TTT-CHILD";

      // create task map
      Map<String, String> lInventoryParent = new LinkedHashMap<String, String>();
      lInventoryParent.put( "SERIAL_NO_OEM", "'" + lParentNo + "'" );
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
      Map<String, String> lInventoryChildOfChild = new LinkedHashMap<String, String>();
      lInventoryChildOfChild.put( "PARENT_SERIAL_NO_OEM", "'" + lParentNo + "'" );
      lInventoryChildOfChild.put( "PARENT_PART_NO_OEM", "'A0000020'" );
      lInventoryChildOfChild.put( "PARENT_MANUFACT_CD", "'10001'" );
      lInventoryChildOfChild.put( "BOM_PART_CD", "'ACFT-SYS-1-1-TRK-TRK-TRK-CHILD'" );
      lInventoryChildOfChild.put( "EQP_POS_CD", "'1.1.1.1'" );
      lInventoryChildOfChild.put( "SERIAL_NO_OEM", "'" + lChildNo + "'" );
      lInventoryChildOfChild.put( "PART_NO_OEM", "'A0000022'" );
      lInventoryChildOfChild.put( "MANUFACT_CD", "'10001'" );
      lInventoryChildOfChild.put( "INV_CLASS_CD", "'TRK'" );
      lInventoryChildOfChild.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB,
            lInventoryChildOfChild ) );

      // call inventory validation
      runALValidateInventory();

      verifyInventoryValidation( "PASS" );
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
    * <li>Insert inventory and sub-inventory to the staging tables</li>
    * <li>Run Inventory Validation</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Error Code: NONE</li>
    * <li>No validation errors are shown if you try to load loose TRK-on-TRK inventory</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    */

   @Test
   public void testTRKonTRKonTRKPassesValidation() throws Exception {

      String lParentNo = "TTT-PARENT";
      String lMiddleNo = "TTT-MIDDLE";
      String lChildNo = "TTT-CHILD";

      // create task map
      Map<String, String> lInventoryParent = new LinkedHashMap<String, String>();
      lInventoryParent.put( "SERIAL_NO_OEM", "'" + lParentNo + "'" );
      lInventoryParent.put( "PART_NO_OEM", "'A0000020'" );
      lInventoryParent.put( "INV_CLASS_CD", "'TRK'" );
      lInventoryParent.put( "MANUFACT_CD", "'10001'" );
      lInventoryParent.put( "LOC_CD", "'OPS'" );
      lInventoryParent.put( "INV_COND_CD", "'RFI'" );
      lInventoryParent.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lInventoryParent.put( "ASSMBL_CD", "'ACFT_CD1'" );
      // lInventoryParent.put( "ASSMBL_BOM_CD", "'ACFT-SYS-1-1-TRK-P1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lInventoryParent ) );

      // create task map
      Map<String, String> lInventoryChild = new LinkedHashMap<String, String>();
      lInventoryChild.put( "PARENT_SERIAL_NO_OEM", "'" + lParentNo + "'" );
      lInventoryChild.put( "PARENT_PART_NO_OEM", "'A0000020'" );
      lInventoryChild.put( "PARENT_MANUFACT_CD", "'10001'" );
      lInventoryChild.put( "BOM_PART_CD", "'ACFT-SYS-1-1-TRK-TRK-TRK-MID'" );
      lInventoryChild.put( "EQP_POS_CD", "'1.1.1'" );
      lInventoryChild.put( "SERIAL_NO_OEM", "'" + lMiddleNo + "'" );
      lInventoryChild.put( "PART_NO_OEM", "'A0000021'" );
      lInventoryChild.put( "MANUFACT_CD", "'10001'" );
      lInventoryChild.put( "INV_CLASS_CD", "'TRK'" );
      lInventoryChild.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lInventoryChild ) );

      // create task map
      Map<String, String> lInventoryChildOfChild = new LinkedHashMap<String, String>();
      lInventoryChildOfChild.put( "PARENT_SERIAL_NO_OEM", "'" + lParentNo + "'" );
      lInventoryChildOfChild.put( "PARENT_PART_NO_OEM", "'A0000020'" );
      lInventoryChildOfChild.put( "PARENT_MANUFACT_CD", "'10001'" );
      lInventoryChildOfChild.put( "BOM_PART_CD", "'ACFT-SYS-1-1-TRK-TRK-TRK-CHILD'" );
      lInventoryChildOfChild.put( "EQP_POS_CD", "'1.1.1.1'" );
      lInventoryChildOfChild.put( "SERIAL_NO_OEM", "'" + lChildNo + "'" );
      lInventoryChildOfChild.put( "PART_NO_OEM", "'A0000022'" );
      lInventoryChildOfChild.put( "MANUFACT_CD", "'10001'" );
      lInventoryChildOfChild.put( "INV_CLASS_CD", "'TRK'" );
      lInventoryChildOfChild.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB,
            lInventoryChildOfChild ) );

      // call inventory validation
      boolean lCompleteAssmblyBool = true;
      runALValidateInventory( lCompleteAssmblyBool );

      verifyInventoryValidation( "PASS" );
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
    * <li>Insert inventory and sub-inventory to the staging tables</li>
    * <li>Run Inventory Validation</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Error Code: NONE</li>
    * <li>No validation errors are shown if you try to load loose TRK-on-TRK inventory</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    */
   @Test
   public void testTRKonTRKPassesValidation() throws Exception {

      String lParentNo = "TT-PARENT";
      String lChildNo = "TT-CHILD";

      // create task map
      Map<String, String> lMapInventoryParent = new LinkedHashMap<String, String>();
      lMapInventoryParent.put( "SERIAL_NO_OEM", "'" + lParentNo + "'" );
      lMapInventoryParent.put( "PART_NO_OEM", "'A0000005'" );
      lMapInventoryParent.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventoryParent.put( "MANUFACT_CD", "'10001'" );
      lMapInventoryParent.put( "LOC_CD", "'OPS'" );
      lMapInventoryParent.put( "INV_COND_CD", "'RFI'" );
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

      // usage
      Map<String, String> lChildInventoryUsage = new LinkedHashMap<String, String>();
      lChildInventoryUsage.put( "SERIAL_NO_OEM", "'" + lChildNo + "'" );
      lChildInventoryUsage.put( "PART_NO_OEM", "'A0000006'" );
      lChildInventoryUsage.put( "MANUFACT_CD", "'11111'" );
      lChildInventoryUsage.put( "DATA_TYPE_CD", "'HOURS'" );
      lChildInventoryUsage.put( "TSN_QT", "1" );
      lChildInventoryUsage.put( "TSO_QT", "1" );
      lChildInventoryUsage.put( "TSI_QT", "1" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lChildInventoryUsage ) );

      // insert row for each usage parm
      lChildInventoryUsage.put( "DATA_TYPE_CD", "'CYCLES'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lChildInventoryUsage ) );
      lChildInventoryUsage.put( "DATA_TYPE_CD", "'USAGE1'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lChildInventoryUsage ) );
      lChildInventoryUsage.put( "DATA_TYPE_CD", "'USAGE2'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lChildInventoryUsage ) );
      lChildInventoryUsage.put( "DATA_TYPE_CD", "'USAGE3'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lChildInventoryUsage ) );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventoryChild ) );

      // call inventory validation
      boolean lCompleteAssmblyBool = true;
      runALValidateInventory( lCompleteAssmblyBool );

      verifyInventoryValidation( "PASS" );
   }


   /**
    * This test is to verify valid error code AML-10213:INVENTORY field INV_COND_CD does not exist
    * in Maintenix REF_INV_COND table.
    *
    *
    */

   @Test
   public void test_AML_10213() throws Exception {

      String lParentNo = "TTT-PARENT";
      String lChildNo = "TTT-CHILD";

      // create task map
      Map<String, String> lInventoryParent = new LinkedHashMap<String, String>();
      lInventoryParent.put( "SERIAL_NO_OEM", "'" + lParentNo + "'" );
      lInventoryParent.put( "PART_NO_OEM", "'A0000020'" );
      lInventoryParent.put( "INV_CLASS_CD", "'TRK'" );
      lInventoryParent.put( "MANUFACT_CD", "'10001'" );
      lInventoryParent.put( "LOC_CD", "'OPS'" );
      lInventoryParent.put( "INV_COND_CD", "'XXX'" );
      lInventoryParent.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lInventoryParent.put( "ASSMBL_CD", "'ACFT_CD1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lInventoryParent ) );

      // create task map
      Map<String, String> lInventoryChildOfChild = new LinkedHashMap<String, String>();
      lInventoryChildOfChild.put( "PARENT_SERIAL_NO_OEM", "'" + lParentNo + "'" );
      lInventoryChildOfChild.put( "PARENT_PART_NO_OEM", "'A0000020'" );
      lInventoryChildOfChild.put( "PARENT_MANUFACT_CD", "'10001'" );
      lInventoryChildOfChild.put( "BOM_PART_CD", "'ACFT-SYS-1-1-TRK-TRK-TRK-CHILD'" );
      lInventoryChildOfChild.put( "EQP_POS_CD", "'1.1.1.1'" );
      lInventoryChildOfChild.put( "SERIAL_NO_OEM", "'" + lChildNo + "'" );
      lInventoryChildOfChild.put( "PART_NO_OEM", "'A0000022'" );
      lInventoryChildOfChild.put( "MANUFACT_CD", "'10001'" );
      lInventoryChildOfChild.put( "INV_CLASS_CD", "'TRK'" );
      lInventoryChildOfChild.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB,
            lInventoryChildOfChild ) );

      // run validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // check error c0de
      checkInventoryErrorCode( testName.getMethodName(), "AML-10213" );
   }


   /**
    * This test is to verify valid error code AML-10523:TASK field DATA_TYPE_CD has a Domain Type of
    * Calendar and Usage deadline values are provided
    *
    *
    */

   @Test
   public void test_AML_10532() throws Exception {

      String lParentNo = "TTT-PARENT";
      String lChildNo = "TTT-CHILD";

      // create task map
      Map<String, String> lInventoryParent = new LinkedHashMap<String, String>();
      lInventoryParent.put( "SERIAL_NO_OEM", "'" + lParentNo + "'" );
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
      Map<String, String> lInventoryChildOfChild = new LinkedHashMap<String, String>();
      lInventoryChildOfChild.put( "PARENT_SERIAL_NO_OEM", "'" + lParentNo + "'" );
      lInventoryChildOfChild.put( "PARENT_PART_NO_OEM", "'A0000020'" );
      lInventoryChildOfChild.put( "PARENT_MANUFACT_CD", "'10001'" );
      lInventoryChildOfChild.put( "BOM_PART_CD", "'ACFT-SYS-1-1-TRK-TRK-TRK-CHILD'" );
      lInventoryChildOfChild.put( "EQP_POS_CD", "'1.1.1.1'" );
      lInventoryChildOfChild.put( "SERIAL_NO_OEM", "'" + lChildNo + "'" );
      lInventoryChildOfChild.put( "PART_NO_OEM", "'A0000022'" );
      lInventoryChildOfChild.put( "MANUFACT_CD", "'10001'" );
      lInventoryChildOfChild.put( "INV_CLASS_CD", "'TRK'" );
      lInventoryChildOfChild.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB,
            lInventoryChildOfChild ) );

      // another record
      lInventoryChildOfChild.clear();
      lInventoryChildOfChild.put( "PARENT_SERIAL_NO_OEM", "'" + lParentNo + "'" );
      lInventoryChildOfChild.put( "PARENT_PART_NO_OEM", "'A0000020'" );
      lInventoryChildOfChild.put( "PARENT_MANUFACT_CD", "'10001'" );
      lInventoryChildOfChild.put( "BOM_PART_CD", "'ACFT-SYS-1-1-TRK-TRK-TRK-CHILD'" );
      lInventoryChildOfChild.put( "EQP_POS_CD", "'1.1.1.1'" );
      lInventoryChildOfChild.put( "SERIAL_NO_OEM", "'" + lChildNo + "_2'" );
      lInventoryChildOfChild.put( "PART_NO_OEM", "'A0000022'" );
      lInventoryChildOfChild.put( "MANUFACT_CD", "'10001'" );
      lInventoryChildOfChild.put( "INV_CLASS_CD", "'TRK'" );
      lInventoryChildOfChild.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB,
            lInventoryChildOfChild ) );

      // run validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // check error c0de
      checkInventoryErrorCode( testName.getMethodName(), "AML-10532" );
   }

}
