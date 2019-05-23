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

import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mxi.mx.util.ActualsLoaderTest;
import com.mxi.mx.util.TableUtil;


/**
 * This test suite contains unit tests for Actuals Loader task validations for Part Numbers and
 * Serial Numbers
 *
 * @author Andrew Bruce
 */

public class ValidateForecastModels extends ActualsLoaderTest {

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
    * <li>Error Code: AML-10426</li>
    * <li>There are no forecast models specified in the Maintenix database.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10426_NoForecastModels() throws Exception {

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

      // create map
      Map<String, String> lInventoryChildUsage = new LinkedHashMap<String, String>();
      lInventoryChildUsage.put( "SERIAL_NO_OEM", "'TRK-001'" );
      lInventoryChildUsage.put( "PART_NO_OEM", "'A0000001'" );
      lInventoryChildUsage.put( "MANUFACT_CD", "'10001'" );
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

      // create map
      Map<String, String> lFCModel = new LinkedHashMap<String, String>();
      lFCModel.put( "MODEL_DB_ID", "4650" );
      lFCModel.put( "MODEL_ID", "100000" );
      lFCModel.put( "DESC_SDESC", "'Forecast1'" );
      lFCModel.put( "DEFAULT_BOOL", "1" );

      // insert map
      deleteFromTableWhere( "FC_MODEL", TableUtil.whereFromTableByMap( lFCModel ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10426_NoForecastModels", "AML-10426" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( "FC_MODEL", lFCModel ) );
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
    * <li>Error Code: AML-10427</li>
    * <li>There is no default forecast model specified in the Maintenix database</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10427_NoDefaultForecastModel() throws Exception {

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

      // create map
      Map<String, String> lInventoryChildUsage = new LinkedHashMap<String, String>();
      lInventoryChildUsage.put( "SERIAL_NO_OEM", "'TRK-001'" );
      lInventoryChildUsage.put( "PART_NO_OEM", "'A0000001'" );
      lInventoryChildUsage.put( "MANUFACT_CD", "'10001'" );
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

      // update forecast models to have no unique model identified
      // save existing default for reference
      ResultSet lCurrentDefaultForecaseModelResultSet =
            runQuery( "select MODEL_ID from FC_MODEL where DEFAULT_BOOL = 1" );

      lCurrentDefaultForecaseModelResultSet.next();

      String lCurrentDefaultForecaseModel = lCurrentDefaultForecaseModelResultSet.getString( 1 );

      // remove default marker
      runUpdate( "update FC_MODEL set DEFAULT_BOOL = 0" );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10427_NoDefaultForecastModel", "AML-10427" );

      // replace default marker
      runUpdate( "update FC_MODEL set DEFAULT_BOOL = 1 where MODEL_ID = "
            + lCurrentDefaultForecaseModel );
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
    * <li>Error Code: AML-10428</li>
    * <li>There is more than one default forecast model specified in the Maintenix database</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10428_MultipleDefaultForecastModels() throws Exception {

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

      // create map
      Map<String, String> lInventoryChildUsage = new LinkedHashMap<String, String>();
      lInventoryChildUsage.put( "SERIAL_NO_OEM", "'TRK-001'" );
      lInventoryChildUsage.put( "PART_NO_OEM", "'A0000001'" );
      lInventoryChildUsage.put( "MANUFACT_CD", "'10001'" );
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

      // add new default default for test
      // create map
      Map<String, String> lFCModel = new LinkedHashMap<String, String>();
      lFCModel.put( "MODEL_DB_ID", "4650" );
      lFCModel.put( "MODEL_ID", "100001" );
      lFCModel.put( "DESC_SDESC", "'TEST'" );
      lFCModel.put( "DEFAULT_BOOL", "1" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( "FC_MODEL", lFCModel ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10428_MultipleDefaultForecastModels", "AML-10428" );

      // insert map
      deleteFromTableWhere( "FC_MODEL", TableUtil.whereFromTableByMap( lFCModel ) );
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
    * <li>Error Code: AML-10451</li>
    * <li>Forecast model is not specified in the Maintenix database (FC_MODEL).</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10451_UnknownForecastModel() throws Exception {

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
      lMapInventory.put( "FORECAST_MODEL", "'UNKNOWNFC'" );

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

      // create map
      Map<String, String> lInventoryChildUsage = new LinkedHashMap<String, String>();
      lInventoryChildUsage.put( "SERIAL_NO_OEM", "'TRK-001'" );
      lInventoryChildUsage.put( "PART_NO_OEM", "'A0000001'" );
      lInventoryChildUsage.put( "MANUFACT_CD", "'10001'" );
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
      validateAndCheckInventory( "test_AML_10451_UnknownForecastModel", "AML-10451" );
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
    * <li>Error Code: AML-10452</li>
    * <li>Forecast Model Description is duplicated in Maintenix database (FC_MODEL.desc_sdesc).</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10452_DuplicateForecastModels() throws Exception {

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
      lMapInventory.put( "FORECAST_MODEL", "'Forecast1'" );

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

      // create map
      Map<String, String> lInventoryChildUsage = new LinkedHashMap<String, String>();
      lInventoryChildUsage.put( "SERIAL_NO_OEM", "'TRK-001'" );
      lInventoryChildUsage.put( "PART_NO_OEM", "'A0000001'" );
      lInventoryChildUsage.put( "MANUFACT_CD", "'10001'" );
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

      // add new default default for test
      // create map
      Map<String, String> lFCModel = new LinkedHashMap<String, String>();
      lFCModel.put( "MODEL_DB_ID", "4650" );
      lFCModel.put( "MODEL_ID", "100001" );
      lFCModel.put( "DESC_SDESC", "'Forecast1'" );
      lFCModel.put( "DEFAULT_BOOL", "0" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( "FC_MODEL", lFCModel ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10452_DuplicateForecastModels", "AML-10452" );

      // insert map
      deleteFromTableWhere( "FC_MODEL", TableUtil.whereFromTableByMap( lFCModel ) );
   }
}
