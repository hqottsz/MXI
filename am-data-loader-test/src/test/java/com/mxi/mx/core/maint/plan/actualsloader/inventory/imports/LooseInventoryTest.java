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

import static org.hamcrest.MatcherAssert.assertThat;

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

public class LooseInventoryTest extends AbstractImportInventory {

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
    * <li>The BATCH part group is defined in maintenix</li>
    * </ul>
    * </p>
    *
    * <p>
    * The test will:
    *
    * <ul>
    * <li>Insert batch inventory to the processing tables</li>
    * <li>Run Inventory Import</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Error Code: NONE</li>
    * <li>No import errors are shown if you try to load loose BATCH inventory</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    */

   @Test
   public void testBATCHImportsSuccessfully() throws Exception {

      String lBatchNo = "BATCH" + getRandom();

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

      // call inventory validation & import
      validateAndCheckInventory( "testBATCHImportsSuccessfully", "PASS" );
      importInventory();

      // verify that import inventory was created as expected
      checkInventoryDetails( lMapInventory );
   }


   /**
    * <p>
    * Given the Preconditions:
    *
    * <ul>
    * <li>The tool part numbers exists in maintenix</li>
    * <li>The BATCH tool part group is defined in maintenix</li>
    * </ul>
    * </p>
    *
    * <p>
    * The test will:
    *
    * <ul>
    * <li>Insert batch tool inventory to the processing tables</li>
    * <li>Run Inventory Import</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Error Code: NONE</li>
    * <li>No import errors are shown if you try to load loose BATCH tool inventory</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    */

   @Test
   public void testBATCHToolImportsSuccessfully() throws Exception {

      String lBatchNo = "BatchTool" + getRandom();

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();
      lMapInventory.put( "SERIAL_NO_OEM", "'" + lBatchNo + "'" );
      lMapInventory.put( "BATCH_NO_OEM", "'" + lBatchNo + "'" );
      lMapInventory.put( "PART_NO_OEM", "'T0000003'" );
      lMapInventory.put( "INV_CLASS_CD", "'BATCH'" );
      lMapInventory.put( "MANUFACT_CD", "'ABC11'" );
      lMapInventory.put( "OWNER_CD", "'MXI'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "BIN_QT", "10" );
      lMapInventory.put( "INV_COND_CD", "'RFI'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // call inventory validation & import
      validateAndCheckInventory( "testBATCHToolImportsSuccessfully", "PASS" );
      importInventory();

      // verify that import inventory was created as expected
      String lquery =
            "select eqp_part_no.total_qt, eqp_part_no.* from eqp_part_no where part_no_oem = 'T0000003'";
      int lqt = getIntValueFromQuery( lquery, "TOTAL_QT" );
      lMapInventory.put( "TOTAL_QT", Integer.toString( lqt ) );
      // lMapInventory.put( "TOTAL_QT", "10" );
      checkInventoryDetails( lMapInventory );
   }


   /**
    * <p>
    * Given the Preconditions:
    *
    * <ul>
    * <li>The part numbers exists in maintenix</li>
    * </ul>
    * </p>
    *
    * <p>
    * The test will:
    *
    * <ul>
    * <li>Insert inventory to the processing tables</li>
    * <li>Run Inventory Import</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Error Code: NONE</li>
    * <li>No import errors are shown after loading loose TRK, SER, BATCH inventory</li>
    * <li>The quantity of one will be added to the total part quantity</li>
    * <li>If the avg_price_unit is not zero, the total value should increase</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    */

   @Test
   public void testlooseInventoryTotalQtyAndTotal() throws Exception {

      // # TRK inventory
      String lTRKSerialNo = "TRK-MX" + getRandom();

      // create task map
      Map<String, String> lMapInventoryTrk = new LinkedHashMap<String, String>();
      lMapInventoryTrk.put( "SERIAL_NO_OEM", "'" + lTRKSerialNo + "'" );
      lMapInventoryTrk.put( "PART_NO_OEM", "'A0000005'" );
      lMapInventoryTrk.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventoryTrk.put( "MANUFACT_CD", "'10001'" );
      lMapInventoryTrk.put( "LOC_CD", "'OPS'" );
      lMapInventoryTrk.put( "INV_COND_CD", "'RFI'" );
      lMapInventoryTrk.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventoryTrk.put( "ASSMBL_CD", "'ACFT_CD1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventoryTrk ) );

      // create task map
      Map<String, String> lInventoryUsageTrk = new LinkedHashMap<String, String>();
      lInventoryUsageTrk.put( "SERIAL_NO_OEM", "'" + lTRKSerialNo + "'" );
      lInventoryUsageTrk.put( "PART_NO_OEM", "'A0000005'" );
      lInventoryUsageTrk.put( "MANUFACT_CD", "'10001'" );
      lInventoryUsageTrk.put( "DATA_TYPE_CD", "'HOURS'" );
      lInventoryUsageTrk.put( "TSN_QT", "1" );
      lInventoryUsageTrk.put( "TSO_QT", "1" );
      lInventoryUsageTrk.put( "TSI_QT", "1" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventoryUsageTrk ) );

      // insert row for each usage parm
      lInventoryUsageTrk.put( "DATA_TYPE_CD", "'CYCLES'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventoryUsageTrk ) );
      lInventoryUsageTrk.put( "DATA_TYPE_CD", "'USAGE1'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventoryUsageTrk ) );
      lInventoryUsageTrk.put( "DATA_TYPE_CD", "'USAGE2'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventoryUsageTrk ) );
      lInventoryUsageTrk.put( "DATA_TYPE_CD", "'USAGE3'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventoryUsageTrk ) );

      // # SER inventory
      String lSERSerialNo = "SER-MX" + getRandom();

      // create task map
      Map<String, String> lMapInventorySer = new LinkedHashMap<String, String>();
      lMapInventorySer.put( "SERIAL_NO_OEM", "'" + lSERSerialNo + "'" );
      lMapInventorySer.put( "PART_NO_OEM", "'A0000012'" );
      lMapInventorySer.put( "INV_CLASS_CD", "'SER'" );
      lMapInventorySer.put( "MANUFACT_CD", "'1234567890'" );
      lMapInventorySer.put( "LOC_CD", "'OPS'" );
      lMapInventorySer.put( "INV_COND_CD", "'RFI'" );
      lMapInventorySer.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventorySer ) );

      // create task map
      Map<String, String> lInventorySerUsage = new LinkedHashMap<String, String>();
      lInventorySerUsage.put( "SERIAL_NO_OEM", "'" + lSERSerialNo + "'" );
      lInventorySerUsage.put( "PART_NO_OEM", "'A0000012'" );
      lInventorySerUsage.put( "MANUFACT_CD", "'1234567890'" );
      lInventorySerUsage.put( "DATA_TYPE_CD", "'HOURS'" );
      lInventorySerUsage.put( "TSN_QT", "1" );
      lInventorySerUsage.put( "TSO_QT", "1" );
      lInventorySerUsage.put( "TSI_QT", "1" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventorySerUsage ) );

      // insert row for each usage parm
      lInventorySerUsage.put( "DATA_TYPE_CD", "'CYCLES'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventorySerUsage ) );
      lInventorySerUsage.put( "DATA_TYPE_CD", "'USAGE1'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventorySerUsage ) );
      lInventorySerUsage.put( "DATA_TYPE_CD", "'USAGE2'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventorySerUsage ) );
      lInventorySerUsage.put( "DATA_TYPE_CD", "'USAGE3'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventorySerUsage ) );

      // # Batch Inventory
      String lBatchNo = "BATCH-MX" + getRandom();

      // create task map
      Map<String, String> lMapBatchInventory = new LinkedHashMap<String, String>();
      lMapBatchInventory.put( "SERIAL_NO_OEM", "'" + lBatchNo + "'" );
      lMapBatchInventory.put( "BATCH_NO_OEM", "'" + lBatchNo + "'" );
      lMapBatchInventory.put( "PART_NO_OEM", "'A0000009'" );
      lMapBatchInventory.put( "INV_CLASS_CD", "'BATCH'" );
      lMapBatchInventory.put( "MANUFACT_CD", "'10001'" );
      lMapBatchInventory.put( "OWNER_CD", "'MXI'" );
      lMapBatchInventory.put( "LOC_CD", "'OPS'" );
      lMapBatchInventory.put( "BIN_QT", "1" );
      lMapBatchInventory.put( "INV_COND_CD", "'RFI'" );
      lMapBatchInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapBatchInventory ) );

      // call inventory validation & import
      validateAndCheckInventory( "testlooseInventoryTotalQtyAndTotal", "PASS" );

      // collect all initial part total quantity and value before the import
      Map<String, String> lMapPartTrkTotalQtAndVal = getPartTotalQtyAndValue( lMapInventoryTrk );
      Map<String, String> lMapPartSerTotalQtAndVal = getPartTotalQtyAndValue( lMapInventorySer );
      Map<String, String> lMapPartBatchTotalQtAndVal =
            getPartTotalQtyAndValue( lMapBatchInventory );

      // import inventory
      importInventory();

      // assert part total quantity and value
      assertPartTotalQuantityAndValue( lMapPartTrkTotalQtAndVal, lMapPartSerTotalQtAndVal,
            lMapPartBatchTotalQtAndVal );
   }


   /**
    * <p>
    * Given the Preconditions:
    *
    * <ul>
    * <li>The part numbers exists in maintenix</li>
    * <li>The SER configuration slot is defined in maintenix</li>
    * </ul>
    * </p>
    *
    * <p>
    * The test will:
    *
    * <ul>
    * <li>Insert inventory to the processing tables</li>
    * <li>Run Inventory Import</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Error Code: NONE</li>
    * <li>No import errors are shown if you try to load loose SER inventory</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    */

   @Test
   public void testSERImportsSuccessfully() throws Exception {

      String lSERSerialNo = "SER" + getRandom();

      // create task map
      Map<String, String> lMapInventoryParent = new LinkedHashMap<String, String>();
      lMapInventoryParent.put( "SERIAL_NO_OEM", "'" + lSERSerialNo + "'" );
      lMapInventoryParent.put( "PART_NO_OEM", "'A0000012'" );
      lMapInventoryParent.put( "INV_CLASS_CD", "'SER'" );
      lMapInventoryParent.put( "MANUFACT_CD", "'1234567890'" );
      lMapInventoryParent.put( "LOC_CD", "'OPS'" );
      lMapInventoryParent.put( "INV_COND_CD", "'RFI'" );
      lMapInventoryParent.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventoryParent ) );

      // create task map
      Map<String, String> lInventoryUsage = new LinkedHashMap<String, String>();
      lInventoryUsage.put( "SERIAL_NO_OEM", "'" + lSERSerialNo + "'" );
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

      // call inventory validation & import
      validateAndCheckInventory( "testSERImportsSuccessfully", "PASS" );
      importInventory();

      // verify that import inventory was created as expected
      checkInventoryDetails( lMapInventoryParent );
   }


   /**
    * <p>
    * Given the Preconditions:
    *
    * <ul>
    * <li>The part numbers exists in maintenix</li>
    * <li>The TRK configuration slot is defined in maintenix</li>
    * </ul>
    * </p>
    *
    * <p>
    * The test will:
    *
    * <ul>
    * <li>Insert inventory to the processing tables</li>
    * <li>Run Inventory Import</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Error Code: NONE</li>
    * <li>No import errors are shown if you try to load loose TRK inventory</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    */

   @Test
   public void testTRKImportsSuccessfully() throws Exception {

      String lTRKSerialNo = "TRK" + getRandom();

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();
      lMapInventory.put( "SERIAL_NO_OEM", "'" + lTRKSerialNo + "'" );
      lMapInventory.put( "PART_NO_OEM", "'A0000005'" );
      lMapInventory.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'RFI'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "ASSMBL_CD", "'ACFT_CD1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // create task map
      Map<String, String> lInventoryUsage = new LinkedHashMap<String, String>();
      lInventoryUsage.put( "SERIAL_NO_OEM", "'" + lTRKSerialNo + "'" );
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

      // call inventory validation & import
      validateAndCheckInventory( "testTRKImportsSuccessfully", "PASS" );
      importInventory();

      // verify that import inventory was created as expected
      checkInventoryDetails( lMapInventory );
   }


   /**
    * <p>
    * Given the Preconditions:
    *
    * <ul>
    * <li>The tool part numbers exists in maintenix</li>
    * <li>The TRK tool configuration slot is defined in maintenix</li>
    * </ul>
    * </p>
    *
    * <p>
    * The test will:
    *
    * <ul>
    * <li>Insert tool inventory to the processing tables</li>
    * <li>Run Inventory Import</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Error Code: NONE</li>
    * <li>No import errors are shown if you try to load loose TRK tool inventory</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    */

   @Test
   public void testTRKToolImportsSuccessfully() throws Exception {

      String lTRKSerialNo = "TRKTool" + getRandom();

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();
      lMapInventory.put( "SERIAL_NO_OEM", "'" + lTRKSerialNo + "'" );
      lMapInventory.put( "PART_NO_OEM", "'T0000002'" );
      lMapInventory.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventory.put( "MANUFACT_CD", "'ABC11'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'RFI'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "ASSMBL_CD", "'TSE'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // call inventory validation & import
      validateAndCheckInventory( "testTRKToolImportsSuccessfully", "PASS" );
      importInventory();

      // verify that import inventory was created as expected
      String lquery =
            "select eqp_part_no.total_qt, eqp_part_no.* from eqp_part_no where part_no_oem = 'T0000002'";
      int lqt = getIntValueFromQuery( lquery, "TOTAL_QT" );
      lMapInventory.put( "TOTAL_QT", Integer.toString( lqt ) );
      checkInventoryDetails( lMapInventory );
   }


   /**
    * DOCUMENT_ME
    *
    * @param aMapPartTrkTotalQtAndVal
    *           DOCUMENT_ME
    * @param aMapPartSerTotalQtAndVal
    *           DOCUMENT_ME
    * @param aMapPartBatchTotalQtAndVal
    *           DOCUMENT_ME
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   private void assertPartTotalQuantityAndValue( Map<String, String> aMapPartTrkTotalQtAndVal,
         Map<String, String> aMapPartSerTotalQtAndVal,
         Map<String, String> aMapPartBatchTotalQtAndVal ) throws Exception {

      Map<String, String> lMapPartTrkTotalQtAndValImp =
            getPartTotalQtyAndValue( aMapPartTrkTotalQtAndVal );
      Map<String, String> lMapPartSerTotalQtAndValImp =
            getPartTotalQtyAndValue( aMapPartSerTotalQtAndVal );
      Map<String, String> lMapPartBatchTotalQtAndValImp =
            getPartTotalQtyAndValue( aMapPartBatchTotalQtAndVal );

      // assert TRK part quantity increases to one
      assertThat( "Should have an increase of one to the total quantity",
            ( Double.parseDouble( lMapPartTrkTotalQtAndValImp.get( "TOTAL_QT" ) )
                  - Double.parseDouble( aMapPartTrkTotalQtAndVal.get( "TOTAL_QT" ) ) ) == 1.0 );
      if ( Double.parseDouble( aMapPartTrkTotalQtAndVal.get( "AVG_UNIT_PRICE" ) ) > 0 ) {
         assertThat( "The total value should have increased",
               Double.parseDouble( lMapPartTrkTotalQtAndValImp.get( "TOTAL_VALUE" ) ) > Double
                     .parseDouble( aMapPartTrkTotalQtAndVal.get( "TOTAL_VALUE" ) ) );
      } else {
         assertThat( "The total value should have increased",
               Double.parseDouble( lMapPartTrkTotalQtAndValImp.get( "TOTAL_VALUE" ) ) == Double
                     .parseDouble( aMapPartTrkTotalQtAndVal.get( "TOTAL_VALUE" ) ) );
      }

      // assert SER part quantity increases to one
      assertThat( "Should have an increase of one to the total quantity",
            ( Double.parseDouble( lMapPartSerTotalQtAndValImp.get( "TOTAL_QT" ) )
                  - Double.parseDouble( aMapPartSerTotalQtAndVal.get( "TOTAL_QT" ) ) ) == 1.0 );
      if ( Double.parseDouble( aMapPartSerTotalQtAndVal.get( "AVG_UNIT_PRICE" ) ) > 0 ) {
         assertThat( "The total value should have increased",
               Double.parseDouble( lMapPartSerTotalQtAndValImp.get( "TOTAL_VALUE" ) ) > Double
                     .parseDouble( aMapPartSerTotalQtAndVal.get( "TOTAL_VALUE" ) ) );
      } else {
         assertThat( "The total value should have increased",
               Double.parseDouble( lMapPartSerTotalQtAndValImp.get( "TOTAL_VALUE" ) ) == Double
                     .parseDouble( aMapPartSerTotalQtAndVal.get( "TOTAL_VALUE" ) ) );
      }

      // assert BATCH part quantity increases to one
      assertThat( "Should have an increase of one to the total quantity",
            ( Double.parseDouble( lMapPartBatchTotalQtAndValImp.get( "TOTAL_QT" ) )
                  - Double.parseDouble( aMapPartBatchTotalQtAndVal.get( "TOTAL_QT" ) ) ) == 1.0 );
      if ( Double.parseDouble( aMapPartBatchTotalQtAndVal.get( "AVG_UNIT_PRICE" ) ) > 0 ) {
         assertThat( "The total value should have increased",
               Double.parseDouble( lMapPartBatchTotalQtAndValImp.get( "TOTAL_VALUE" ) ) > Double
                     .parseDouble( aMapPartBatchTotalQtAndVal.get( "TOTAL_VALUE" ) ) );
      } else {
         assertThat( "The total value should have increased",
               Double.parseDouble( lMapPartBatchTotalQtAndValImp.get( "TOTAL_VALUE" ) ) == Double
                     .parseDouble( aMapPartBatchTotalQtAndVal.get( "TOTAL_VALUE" ) ) );
      }
   }
}
