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

package com.mxi.mx.core.maint.plan.actualsloader.inventory.imports;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mxi.mx.util.TableUtil;


/**
 * This test suite contains all test cases for Actuals Loader loading of historic tasks
 *
 * @author Andrew Bruce
 */

public class DoNotCompleteAssemblyForLooseInventoryTest extends AbstractImportInventory {

   /**
    * Clean up after each individual test
    *
    * @throws Exception
    */
   @After
   @Override
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
    * <li>Insert inventory and sub-inventory to the processing tables</li>
    * <li>Run Inventory Import</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Error Code: NONE</li>
    * <li>No import errors are shown if you try to load loose TRK-on-TRK inventory</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    */

   @Test
   public void testTRKonTRKImportsSuccessfully() throws Exception {

      int lRandom = getRandom();

      String lParentSerialNo = "TT-PARENT" + lRandom;
      String lChildSerialNo = "TT-CHILD" + lRandom;

      // create task map
      Map<String, String> lMapInventoryParent = new LinkedHashMap<String, String>();
      lMapInventoryParent.put( "SERIAL_NO_OEM", "'" + lParentSerialNo + "'" );
      lMapInventoryParent.put( "PART_NO_OEM", "'A0000005'" );
      lMapInventoryParent.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventoryParent.put( "MANUFACT_CD", "'10001'" );
      lMapInventoryParent.put( "LOC_CD", "'OPS'" );
      lMapInventoryParent.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventoryParent.put( "ASSMBL_CD", "'ACFT_CD1'" );
      lMapInventoryParent.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventoryParent ) );

      // create task map
      Map<String, String> lInventoryUsage = new LinkedHashMap<String, String>();
      lInventoryUsage.put( "SERIAL_NO_OEM", "'" + lParentSerialNo + "'" );
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

      // create task map
      Map<String, String> lMapInventoryChild = new LinkedHashMap<String, String>();
      lMapInventoryChild.put( "PARENT_SERIAL_NO_OEM", "'" + lParentSerialNo + "'" );
      lMapInventoryChild.put( "PARENT_PART_NO_OEM", "'A0000005'" );
      lMapInventoryChild.put( "PARENT_MANUFACT_CD", "'10001'" );
      lMapInventoryChild.put( "BOM_PART_CD", "'ACFT-SYS-1-1-TRK-TRK-CHILD'" );
      lMapInventoryChild.put( "EQP_POS_CD", "'1'" );
      lMapInventoryChild.put( "SERIAL_NO_OEM", "'" + lChildSerialNo + "'" );
      lMapInventoryChild.put( "PART_NO_OEM", "'A0000006'" );
      lMapInventoryChild.put( "MANUFACT_CD", "'11111'" );
      lMapInventoryChild.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventoryChild.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventoryChild ) );

      // create task map
      Map<String, String> lChildInventoryUsage = new LinkedHashMap<String, String>();
      lChildInventoryUsage.put( "SERIAL_NO_OEM", "'" + lChildSerialNo + "'" );
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

      validateAndCheckInventory_NotComplete( "testTRKonTRKImportsSuccessfully", "PASS" );
      importInventory();

      // verify that import inventory was created as expected
      checkInventoryDetails( lMapInventoryParent );
      lMapInventoryChild.put( "INV_COND_CD", "'INSRV'" );
      checkSubInventoryDetails( lMapInventoryChild );
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
    * <li>Run Inventory Import</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Error Code: NONE</li>
    * <li>No import errors are shown if you try to load loose TRK-on-TRK-on-TRK inventory</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    */

   @Test
   public void testTRKonTRKonTRKImportsSuccessfully() throws Exception {

      int lRandom = getRandom();
      String lParentSerialNo = "TTT-PARENT" + lRandom;
      String lMiddleSerialNo = "TTT-MIDDLE" + lRandom;
      String lChildSerialNo = "TTT-CHILD" + lRandom;

      // create task map
      Map<String, String> lMapInventoryParent = new LinkedHashMap<String, String>();
      lMapInventoryParent.put( "SERIAL_NO_OEM", "'" + lParentSerialNo + "'" );
      lMapInventoryParent.put( "PART_NO_OEM", "'A0000020'" );
      lMapInventoryParent.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventoryParent.put( "MANUFACT_CD", "'10001'" );
      lMapInventoryParent.put( "LOC_CD", "'OPS'" );
      lMapInventoryParent.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventoryParent.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventoryParent.put( "ASSMBL_CD", "'ACFT_CD1'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventoryParent ) );

      // create task map
      Map<String, String> lMapInventoryChild = new LinkedHashMap<String, String>();
      lMapInventoryChild.put( "PARENT_SERIAL_NO_OEM", "'" + lParentSerialNo + "'" );
      lMapInventoryChild.put( "PARENT_PART_NO_OEM", "'A0000020'" );
      lMapInventoryChild.put( "PARENT_MANUFACT_CD", "'10001'" );
      lMapInventoryChild.put( "BOM_PART_CD", "'ACFT-SYS-1-1-TRK-TRK-TRK-MID'" );
      lMapInventoryChild.put( "EQP_POS_CD", "'1.1.1'" );
      lMapInventoryChild.put( "SERIAL_NO_OEM", "'" + lMiddleSerialNo + "'" );
      lMapInventoryChild.put( "PART_NO_OEM", "'A0000021'" );
      lMapInventoryChild.put( "MANUFACT_CD", "'10001'" );
      lMapInventoryChild.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventoryChild.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventoryChild ) );

      // create task map
      Map<String, String> lMapInventoryChildOfChild = new LinkedHashMap<String, String>();
      lMapInventoryChildOfChild.put( "PARENT_SERIAL_NO_OEM", "'" + lParentSerialNo + "'" );
      lMapInventoryChildOfChild.put( "PARENT_PART_NO_OEM", "'A0000020'" );
      lMapInventoryChildOfChild.put( "PARENT_MANUFACT_CD", "'10001'" );
      lMapInventoryChildOfChild.put( "BOM_PART_CD", "'ACFT-SYS-1-1-TRK-TRK-TRK-CHILD'" );
      lMapInventoryChildOfChild.put( "EQP_POS_CD", "'1.1.1.1'" );
      lMapInventoryChildOfChild.put( "SERIAL_NO_OEM", "'" + lChildSerialNo + "'" );
      lMapInventoryChildOfChild.put( "PART_NO_OEM", "'A0000022'" );
      lMapInventoryChildOfChild.put( "MANUFACT_CD", "'10001'" );
      lMapInventoryChildOfChild.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventoryChildOfChild.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap(

            TableUtil.C_RI_INVENTORY_SUB, lMapInventoryChildOfChild ) );

      validateAndCheckInventory_NotComplete( "testTRKonTRKonTRKImportsSuccessfully", "PASS" );
      importInventory();

      // verify that imported inventory was created as expected
      checkInventoryDetails( lMapInventoryParent );
      lMapInventoryChild.put( "INV_COND_CD", "'INSRV'" );
      checkSubInventoryDetails( lMapInventoryChild );
      lMapInventoryChildOfChild.put( "INV_COND_CD", "'INSRV'" );
      checkSubInventoryDetails( lMapInventoryChildOfChild );
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
    * <li>Insert inventory and no sub-inventory to the processing tables</li>
    * <li>Run Inventory Import</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Error Code: NONE</li>
    * <li>No import errors are shown if you try to load loose TRK-on-TRK-on-TRK inventory</li>
    * <li>There are no children inserted into the core tables</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    */

   @Test
   public void testTRKonTRKonTRKWithHoleImportsSuccessfully() throws Exception {

      int lRandom = getRandom();
      String lParentSerialNo = "TTT-PARENT" + lRandom;

      // create task map
      Map<String, String> lParent = new LinkedHashMap<String, String>();
      lParent.put( "SERIAL_NO_OEM", "'" + lParentSerialNo + "'" );
      lParent.put( "PART_NO_OEM", "'A0000020'" );
      lParent.put( "INV_CLASS_CD", "'TRK'" );
      lParent.put( "MANUFACT_CD", "'10001'" );
      lParent.put( "LOC_CD", "'OPS'" );
      lParent.put( "INV_COND_CD", "'REPREQ'" );
      lParent.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lParent.put( "ASSMBL_CD", "'ACFT_CD1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lParent ) );

      validateAndCheckInventory_NotComplete( "testTRKonTRKonTRKWithHoleImportsSuccessfully",
            "PASS" );
      importInventory();

      // verify that imported inventory was created as expected
      checkInventoryDetails( lParent );

      checkInventoryHasRightNumberOfChildren( lParent, 0 );
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
    * <li>Insert inventory and no sub inventory to the processing tables</li>
    * <li>Run Inventory Import</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Error Code: NONE</li>
    * <li>No import errors are shown if you try to load loose TRK-on-TRK inventory</li>
    * <li>There are no children inserted into the core tables</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    */

   @Test
   public void testTRKonTRKWithHoleImportsSuccessfully() throws Exception {

      int lRandom = getRandom();

      String lParentSerialNo = "TT-PARENT" + lRandom;

      // create task map
      Map<String, String> lParent = new LinkedHashMap<String, String>();
      lParent.put( "SERIAL_NO_OEM", "'" + lParentSerialNo + "'" );
      lParent.put( "PART_NO_OEM", "'A0000005'" );
      lParent.put( "INV_CLASS_CD", "'TRK'" );
      lParent.put( "MANUFACT_CD", "'10001'" );
      lParent.put( "LOC_CD", "'OPS'" );
      lParent.put( "INV_COND_CD", "'REPREQ'" );
      lParent.put( "ASSMBL_CD", "'ACFT_CD1'" );
      lParent.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lParent ) );

      // create task map
      Map<String, String> lInventoryUsage = new LinkedHashMap<String, String>();
      lInventoryUsage.put( "SERIAL_NO_OEM", "'" + lParentSerialNo + "'" );
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

      validateAndCheckInventory_NotComplete( "testTRKonTRKonTRKWithHoleImportsSuccessfully",
            "PASS" );
      importInventory();

      // verify that import inventory was created as expected
      checkInventoryDetails( lParent );

      checkInventoryHasRightNumberOfChildren( lParent, 0 );
   }
}
