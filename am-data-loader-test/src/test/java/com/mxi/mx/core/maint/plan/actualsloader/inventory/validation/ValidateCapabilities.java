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
 * This test suite contains unit tests for Aircraft capability validations
 *
 * @author Hong Zheng
 */

public class ValidateCapabilities extends ActualsLoaderTest {

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
    * This test is to verify valid error code AML-10036:Mandatory field missing: SERIAL_NO_OEM.
    *
    *
    */
   @Test
   public void test_AML_10036() throws Exception {

      // create C_RI_INVENTORY map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-SER1'" );
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

      // create C_RI_INVENTORY_CAP_LEVELS map
      Map<String, String> lMapCapability = new LinkedHashMap<String, String>();

      // lMapCapability.put( "SERIAL_NO_OEM", "'ACFT-SER1'" );
      lMapCapability.put( "SERIAL_NO_OEM", null );
      lMapCapability.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapCapability.put( "MANUFACT_CD", "'10001'" );
      lMapCapability.put( "CAP_CD", "'ETOPS'" );
      lMapCapability.put( "LEVEL_CD", "'ETOPS_90'" );
      lMapCapability.put( "CONFIG_LEVEL_CD", "'ETOPS_90'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_CAP_LEVELS,
            lMapCapability ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10036", "AML-10036" );
   }


   /**
    * This test is to verify valid error code AML-10037:Mandatory field missing: PART_NO_OEM.
    *
    *
    */
   @Test
   public void test_AML_10037() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-SER1'" );
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

      // create task map
      Map<String, String> lMapCapability = new LinkedHashMap<String, String>();

      lMapCapability.put( "SERIAL_NO_OEM", "'ACFT-SER1'" );
      // lMapCapability.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapCapability.put( "PART_NO_OEM", null );
      lMapCapability.put( "MANUFACT_CD", "'10001'" );
      lMapCapability.put( "CAP_CD", "'ETOPS'" );
      lMapCapability.put( "LEVEL_CD", "'ETOPS_90'" );
      lMapCapability.put( "CONFIG_LEVEL_CD", "'ETOPS_90'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_CAP_LEVELS,
            lMapCapability ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10037", "AML-10037" );
   }


   /**
    * This test is to verify valid error code AML-10038:Mandatory field missing: CAP_CD.
    *
    *
    */
   @Test
   public void test_AML_10038() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-SER1'" );
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

      // create task map
      Map<String, String> lMapCapability = new LinkedHashMap<String, String>();

      lMapCapability.put( "SERIAL_NO_OEM", "'ACFT-SER1'" );
      lMapCapability.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapCapability.put( "MANUFACT_CD", "'10001'" );
      // lMapCapability.put( "CAP_CD", "'ETOPS'" );
      lMapCapability.put( "CAP_CD", null );
      lMapCapability.put( "LEVEL_CD", "'ETOPS_90'" );
      lMapCapability.put( "CONFIG_LEVEL_CD", "'ETOPS_90'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_CAP_LEVELS,
            lMapCapability ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10038", "AML-10038" );
   }


   /**
    * <p>
    * Given the Preconditions:
    *
    * <ul>
    * <li>There are two rows of similar capabilities</li>
    * </ul>
    * </p>
    *
    * <p>
    * The test will:
    *
    * <ul>
    * <li>Insert aircraft and its capabilities to the staging table</li>
    * <li>Run inventory Validation</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Error Code: AML-10112</li>
    * <li>The provided SERIAL_NO_OEM, PART_NO_OEM, MANUFACT_CD, CAP_CD combination cannot appear
    * more than once</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10112_DuplicateCapabilities() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-SER1'" );
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

      // create task map
      Map<String, String> lMapCapability = new LinkedHashMap<String, String>();

      lMapCapability.put( "SERIAL_NO_OEM", "'ACFT-SER1'" );
      lMapCapability.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapCapability.put( "MANUFACT_CD", "'10001'" );
      lMapCapability.put( "CAP_CD", "'ETOPS'" );
      lMapCapability.put( "LEVEL_CD", "'ETOPS_90'" );
      lMapCapability.put( "CONFIG_LEVEL_CD", "'ETOPS_90'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_CAP_LEVELS,
            lMapCapability ) );

      lMapCapability = new LinkedHashMap<String, String>();

      lMapCapability.put( "SERIAL_NO_OEM", "'ACFT-SER1'" );
      lMapCapability.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapCapability.put( "MANUFACT_CD", "'10001'" );
      lMapCapability.put( "CAP_CD", "'ETOPS'" ); // duplicate
      lMapCapability.put( "LEVEL_CD", "'ETOPS_90'" );
      lMapCapability.put( "CONFIG_LEVEL_CD", "'ETOPS_90'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_CAP_LEVELS,
            lMapCapability ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10112_DuplicateCapabilities", "AML-10112" );
   }


   /**
    * <p>
    * Given the Preconditions:
    *
    * <ul>
    * <li>There is wrong capability</li>
    * </ul>
    * </p>
    *
    * <p>
    * The test will:
    *
    * <ul>
    * <li>Insert aircraft and its capabilities to the staging table</li>
    * <li>Run inventory Validation</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Error Code: AML-10253</li>
    * <li>The provided CAP_CD does not exist in Maintenix Baseline.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10253_WrongCapability() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-SER1'" );
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

      // create task map
      Map<String, String> lMapCapability = new LinkedHashMap<String, String>();

      lMapCapability.put( "SERIAL_NO_OEM", "'ACFT-SER1'" );
      lMapCapability.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapCapability.put( "MANUFACT_CD", "'10001'" );
      lMapCapability.put( "CAP_CD", "'WINGLET'" );// wrong value
      lMapCapability.put( "LEVEL_CD", "'Regular'" );
      lMapCapability.put( "CONFIG_LEVEL_CD", "'Regular'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_CAP_LEVELS,
            lMapCapability ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10253_WrongCapability", "AML-10253" );
   }


   /**
    * <p>
    * Given the Preconditions:
    *
    * <ul>
    * <li>There is wrong current capability level</li>
    * </ul>
    * </p>
    *
    * <p>
    * The test will:
    *
    * <ul>
    * <li>Insert aircraft and its capabilities to the staging table</li>
    * <li>Run inventory Validation</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Error Code: AML-10254</li>
    * <li>The provided LEVEL_CD does not exist in Maintenix Baseline.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10254_WrongCurrentCapabilityLevel() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-SER1'" );
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
      Map<String, String> lMapCapability = new LinkedHashMap<String, String>();

      lMapCapability.put( "SERIAL_NO_OEM", "'ACFT-SER1'" );
      lMapCapability.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapCapability.put( "MANUFACT_CD", "'10001'" );
      lMapCapability.put( "CAP_CD", "'ETOPS'" );
      lMapCapability.put( "LEVEL_CD", "'NO_ETOPS'" );// wrong value
      lMapCapability.put( "CONFIG_LEVEL_CD", "'ETOPS_90'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_CAP_LEVELS,
            lMapCapability ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10254_WrongCurrentCapabilityLevel", "AML-10254" );
   }


   /**
    * <p>
    * Given the Preconditions:
    *
    * <ul>
    * <li>There is wrong configured capability level</li>
    * </ul>
    * </p>
    *
    * <p>
    * The test will:
    *
    * <ul>
    * <li>Insert aircraft and its capabilities to the staging table</li>
    * <li>Run inventory Validation</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Error Code: AML-10255</li>
    * <li>The provided CONFIG_LEVEL_CD does not exist in Maintenix Baseline.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10255_WrongConfiguredCapabilityLevel() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-SER1'" );
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
      Map<String, String> lMapCapability = new LinkedHashMap<String, String>();

      lMapCapability.put( "SERIAL_NO_OEM", "'ACFT-SER1'" );
      lMapCapability.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapCapability.put( "MANUFACT_CD", "'10001'" );
      lMapCapability.put( "CAP_CD", "'ETOPS'" );
      lMapCapability.put( "LEVEL_CD", "'ETOPS_90'" );
      lMapCapability.put( "CONFIG_LEVEL_CD", "'NO_ETOPS'" );// wrong value

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_CAP_LEVELS,
            lMapCapability ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10255_WrongConfiguredCapabilityLevel", "AML-10255" );
   }


   /**
    * This test is to verify valid error code AML-10256:Mandatory field missing: SERIAL_NO_OEM.
    *
    *
    */
   @Test
   public void test_AML_10256() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-SER1'" );
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
      Map<String, String> lMapCapability = new LinkedHashMap<String, String>();

      lMapCapability.put( "SERIAL_NO_OEM", "'ACFT-SER1'" );
      lMapCapability.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapCapability.put( "MANUFACT_CD", "'10001'" );
      lMapCapability.put( "CAP_CD", "'INVALID'" );
      lMapCapability.put( "LEVEL_CD", "'ETOPS_90'" );// wrong value
      lMapCapability.put( "CONFIG_LEVEL_CD", "'ETOPS_90'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_CAP_LEVELS,
            lMapCapability ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10256", "AML-10256" );
   }


   /**
    * <p>
    * Given the Preconditions:
    *
    * <ul>
    * <li>There is a aircraft with correct capability but with wrong part number</li>
    * </ul>
    * </p>
    *
    * <p>
    * The test will:
    *
    * <ul>
    * <li>Insert aircraft and its capabilities to the staging table</li>
    * <li>Run inventory Validation</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Error Code: AML-10305</li>
    * <li>Cannot load record because the related record in C_RI_INVENTORY is invalid.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10305_CorrectCapabilityButInvalidPartNumber() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-SER1'" );
      lMapInventory.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );// wrong part number
      lMapInventory.put( "INV_CLASS_CD", "'ACFT'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'REPREQ'" );

      lMapInventory.put( "AC_REG_CD", "'ACREG1'" );
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'invalid'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // create task map
      Map<String, String> lMapCapability = new LinkedHashMap<String, String>();

      lMapCapability.put( "SERIAL_NO_OEM", "'ACFT-SER1'" );
      lMapCapability.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );// wrong part number
      lMapCapability.put( "MANUFACT_CD", "'10001'" );
      lMapCapability.put( "CAP_CD", "'ETOPS'" );
      lMapCapability.put( "LEVEL_CD", "'ETOPS120'" );
      lMapCapability.put( "CONFIG_LEVEL_CD", "'ETOPS120'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_CAP_LEVELS,
            lMapCapability ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10305", "AML-10305" );
   }


   /**
    * <p>
    * Given the Preconditions:
    *
    * <ul>
    * <li>There is a capability with blank configured capability level and non-blank current
    * capability level</li>
    * </ul>
    * </p>
    *
    * <p>
    * The test will:
    *
    * <ul>
    * <li>Insert aircraft and its capabilities to the staging table</li>
    * <li>Run inventory Validation</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Error Code: AML-10453</li>
    * <li>The provided current capability level must be blank because the provided configured
    * capability level is blank.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10453_BlankConfiguredLevelButNonBlankCurrentLevel() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-SER1'" );
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
      Map<String, String> lMapCapability = new LinkedHashMap<String, String>();

      lMapCapability.put( "SERIAL_NO_OEM", "'ACFT-SER1'" );
      lMapCapability.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapCapability.put( "MANUFACT_CD", "'10001'" );
      lMapCapability.put( "CAP_CD", "'ETOPS'" );
      lMapCapability.put( "LEVEL_CD", "'ETOPS_90'" );
      lMapCapability.put( "CONFIG_LEVEL_CD", "''" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_CAP_LEVELS,
            lMapCapability ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10453_BlankConfiguredLevelButNonBlankCurrentLevel",
            "AML-10453" );
   }


   /**
    * <p>
    * Given the Preconditions:
    *
    * <ul>
    * <li>There is a capability with configured capability level=NO and current capability level=YES
    * </li>
    * </ul>
    * </p>
    *
    * <p>
    * The test will:
    *
    * <ul>
    * <li>Insert aircraft and its capabilities to the staging table</li>
    * <li>Run inventory Validation</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Error Code: AML-10454</li>
    * <li>The provided current capability level must be blank or NO because the provided configured
    * capability level is set to a boolean value of NO.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10454_ConfiguredLevelNoButCurrentLevelYes() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-SER1'" );
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
      Map<String, String> lMapCapability = new LinkedHashMap<String, String>();

      lMapCapability.put( "SERIAL_NO_OEM", "'ACFT-SER1'" );
      lMapCapability.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapCapability.put( "MANUFACT_CD", "'10001'" );
      lMapCapability.put( "CAP_CD", "'WIFI'" );
      lMapCapability.put( "LEVEL_CD", "'INVALID'" );
      lMapCapability.put( "CONFIG_LEVEL_CD", "'NO'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_CAP_LEVELS,
            lMapCapability ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10454_ConfiguredLevelNoButCurrentLevelYes",
            "AML-10454" );
   }


   /**
    * <p>
    * Given the Preconditions:
    *
    * <ul>
    * <li>There is a capability with configured capability level=YES and current capability
    * level=1</li>
    * </ul>
    * </p>
    *
    * <p>
    * The test will:
    *
    * <ul>
    * <li>Insert aircraft and its capabilities to the staging table</li>
    * <li>Run inventory Validation</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Error Code: AML-10454</li>
    * <li>The provided current capability level must be blank, NO or YES because the provided
    * configured capability level is set to a boolean value of YES.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10455_ConfiguredLevelYesButCurrentLevelOutOfRange() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-SER1'" );
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
      Map<String, String> lMapCapability = new LinkedHashMap<String, String>();

      lMapCapability.put( "SERIAL_NO_OEM", "'ACFT-SER1'" );
      lMapCapability.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapCapability.put( "MANUFACT_CD", "'10001'" );
      lMapCapability.put( "CAP_CD", "'WIFI'" );
      lMapCapability.put( "LEVEL_CD", "'YES'" );
      lMapCapability.put( "CONFIG_LEVEL_CD", "'INVALID'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_CAP_LEVELS,
            lMapCapability ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10455_ConfiguredLevelYesButCurrentLevelOutOfRange",
            "AML-10455" );
   }


   /**
    * <p>
    * Given the Preconditions:
    *
    * <ul>
    * <li>There is a capability with current capability level > configured capability level</li>
    * </ul>
    * </p>
    *
    * <p>
    * The test will:
    *
    * <ul>
    * <li>Insert aircraft and its capabilities to the staging table</li>
    * <li>Run inventory Validation</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Error Code: AML-10456</li>
    * <li>The provided current capability level is larger than the provided configured level which
    * is a multi-select value.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10456_CurrentLevelLargeThanConfiguredLevel() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-SER1'" );
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
      Map<String, String> lMapCapability = new LinkedHashMap<String, String>();

      lMapCapability.put( "SERIAL_NO_OEM", "'ACFT-SER1'" );
      lMapCapability.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapCapability.put( "MANUFACT_CD", "'10001'" );
      lMapCapability.put( "CAP_CD", "'ETOPS'" );
      lMapCapability.put( "LEVEL_CD", "'ETOPS120'" );
      lMapCapability.put( "CONFIG_LEVEL_CD", "'ETOPS_90'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_CAP_LEVELS,
            lMapCapability ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10456_CurrentLevelLargeThanConfiguredLevel",
            "AML-10456" );
   }


   /**
    * Error code AML-10457: The provided current capability code exists more than once in Maintenix
    */
   @Test
   public void test_AML_10457_DuplicateCapCD() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-SER1'" );
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
      Map<String, String> lMapCapability = new LinkedHashMap<String, String>();

      lMapCapability.put( "SERIAL_NO_OEM", "'ACFT-SER1'" );
      lMapCapability.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapCapability.put( "MANUFACT_CD", "'10001'" );
      lMapCapability.put( "CAP_CD", "'AUTOTEST'" );
      lMapCapability.put( "LEVEL_CD", "'ETOPS_90'" );
      lMapCapability.put( "CONFIG_LEVEL_CD", "'ETOPS_90'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_CAP_LEVELS,
            lMapCapability ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10457_DuplicateCapCD", "AML-10457" );
   }


   /**
    * Error code AML-10458: he provided current capability level code exists more than once in
    * Maintenix
    */
   @Test
   public void test_AML_10458_DuplicateCapLevel() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-SER1'" );
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
      Map<String, String> lMapCapability = new LinkedHashMap<String, String>();

      lMapCapability.put( "SERIAL_NO_OEM", "'ACFT-SER1'" );
      lMapCapability.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapCapability.put( "MANUFACT_CD", "'10001'" );
      lMapCapability.put( "CAP_CD", "'ALAND'" );
      lMapCapability.put( "LEVEL_CD", "'AT'" );
      lMapCapability.put( "CONFIG_LEVEL_CD", "'AT'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_CAP_LEVELS,
            lMapCapability ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10458_DuplicateCapLevel", "AML-10458" );
   }


   /**
    * This test is to verify valid error code AML-10459:The provided configured capability level
    * code exists more than once in Maintenix.
    *
    *
    */
   @Test
   public void test_AML_10459() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-SER1'" );
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
      Map<String, String> lMapCapability = new LinkedHashMap<String, String>();

      lMapCapability.put( "SERIAL_NO_OEM", "'ACFT-SER1'" );
      lMapCapability.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapCapability.put( "MANUFACT_CD", "'10001'" );
      lMapCapability.put( "CAP_CD", "'ALAND'" );
      lMapCapability.put( "LEVEL_CD", "'AT'" );// wrong value
      lMapCapability.put( "CONFIG_LEVEL_CD", "'AT'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_CAP_LEVELS,
            lMapCapability ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10459", "AML-10459" );
   }


   /**
    * This test is to verify valid error code AML-10460:The CUSTOM_LEVEL field value should be
    * provided only for the SEATNUM capability code
    *
    *
    */
   @Test
   public void test_AML_10460() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-SER1'" );
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
      Map<String, String> lMapCapability = new LinkedHashMap<String, String>();

      lMapCapability.put( "SERIAL_NO_OEM", "'ACFT-SER1'" );
      lMapCapability.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapCapability.put( "MANUFACT_CD", "'10001'" );
      lMapCapability.put( "CAP_CD", "'ETOPS'" );
      lMapCapability.put( "LEVEL_CD", "'ETOPS_90'" );// wrong value
      lMapCapability.put( "CONFIG_LEVEL_CD", "'ETOPS_90'" );
      lMapCapability.put( "CUSTOM_LEVEL", "'100'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_CAP_LEVELS,
            lMapCapability ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10460", "AML-10460" );
   }


   /**
    * This test is to verify valid error code AML-10461:The LEVEL_CD field value must be blank for
    * the SEATNUM capability code.
    *
    *
    */
   @Test
   public void test_AML_10461() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-SER1'" );
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
      Map<String, String> lMapCapability = new LinkedHashMap<String, String>();

      lMapCapability.put( "SERIAL_NO_OEM", "'ACFT-SER1'" );
      lMapCapability.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapCapability.put( "MANUFACT_CD", "'10001'" );
      lMapCapability.put( "CAP_CD", "'SEATNUM'" );
      lMapCapability.put( "LEVEL_CD", "'175'" );
      lMapCapability.put( "CONFIG_LEVEL_CD", "'175'" );
      lMapCapability.put( "CUSTOM_LEVEL", "'100'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_CAP_LEVELS,
            lMapCapability ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10461", "AML-10461" );
   }


   /**
    * This test is to verify valid error code AML-10462:If the CUSTOM_LEVEL field is provided for
    * the SEATNUM capability code then it must be a positive integer.
    *
    *
    */
   @Test
   public void test_AML_10462() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-SER1'" );
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
      Map<String, String> lMapCapability = new LinkedHashMap<String, String>();

      lMapCapability.put( "SERIAL_NO_OEM", "'ACFT-SER1'" );
      lMapCapability.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapCapability.put( "MANUFACT_CD", "'10001'" );
      lMapCapability.put( "CAP_CD", "'SEATNUM'" );
      // lMapCapability.put( "LEVEL_CD", "'175'" );
      lMapCapability.put( "LEVEL_CD", null );
      lMapCapability.put( "CONFIG_LEVEL_CD", "'175'" );

      lMapCapability.put( "CUSTOM_LEVEL", "'-100'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_CAP_LEVELS,
            lMapCapability ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10462", "AML-10462" );
   }


   /**
    * This test is to verify valid error code AML-10463:If configured capability level code is
    * provided for the SEATNUM capability code then it must be a positive integer.
    *
    *
    */
   @Test
   public void test_AML_10463() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-SER1'" );
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
      Map<String, String> lMapCapability = new LinkedHashMap<String, String>();

      lMapCapability.put( "SERIAL_NO_OEM", "'ACFT-SER1'" );
      lMapCapability.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapCapability.put( "MANUFACT_CD", "'10001'" );
      lMapCapability.put( "CAP_CD", "'SEATNUM'" );
      // lMapCapability.put( "LEVEL_CD", "'175'" );
      lMapCapability.put( "LEVEL_CD", null );
      lMapCapability.put( "CONFIG_LEVEL_CD", "'-175'" );

      lMapCapability.put( "CUSTOM_LEVEL", "'100'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_CAP_LEVELS,
            lMapCapability ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10463", "AML-10463" );
   }


   /**
    * This test is to verify valid error code AML-10464:If the CUSTOM_LEVEL field is provided for
    * the SEATNUM capability code then it should not exceed the configured capability level code.
    *
    *
    */
   @Test
   public void test_AML_10464() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-SER1'" );
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
      Map<String, String> lMapCapability = new LinkedHashMap<String, String>();

      lMapCapability.put( "SERIAL_NO_OEM", "'ACFT-SER1'" );
      lMapCapability.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapCapability.put( "MANUFACT_CD", "'10001'" );
      lMapCapability.put( "CAP_CD", "'SEATNUM'" );
      // lMapCapability.put( "LEVEL_CD", "'175'" );
      lMapCapability.put( "LEVEL_CD", null );
      lMapCapability.put( "CONFIG_LEVEL_CD", "'175'" );

      lMapCapability.put( "CUSTOM_LEVEL", "'500'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_CAP_LEVELS,
            lMapCapability ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10464", "AML-10464" );
   }


   /**
    * This test is to verify valid error code AML-10511: The Inventory identified by
    * SERIAL_NO_OEM/PART_NO_OEM/MANUFACT_CD does not exist in C_RI_INVENTORY. Every Inventory must
    * have a record in C_RI_INVENTORY.
    *
    *
    */

   @Test
   public void test_AML_10511_InvalidSN() throws Exception {

      String lRandom = String.valueOf( getRandom() );

      // create inventory map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();
      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-" + lRandom + "'" );
      lMapInventory.put( "PART_NO_OEM", "'ACFT_ASSY_PN2'" );
      lMapInventory.put( "INV_CLASS_CD", "'ACFT'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventory.put( "AC_REG_CD", "'" + lRandom + "'" );
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "FORECAST_MODEL", "'Forecast1'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // create task map

      Map<String, String> lMapCapability = new LinkedHashMap<String, String>();

      lMapCapability.put( "SERIAL_NO_OEM", "'ACFT-" + lRandom + "'" );
      lMapCapability.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapCapability.put( "MANUFACT_CD", "'10001'" );
      lMapCapability.put( "CAP_CD", "'ETOPS'" );
      lMapCapability.put( "LEVEL_CD", "'ETOPS_90'" );
      lMapCapability.put( "CONFIG_LEVEL_CD", "'ETOPS_90'" );

      // insert map

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_CAP_LEVELS,
            lMapCapability ) );

      lMapCapability = new LinkedHashMap<String, String>();

      lMapCapability.put( "SERIAL_NO_OEM", "'ACFT-" + lRandom + "'" );
      lMapCapability.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapCapability.put( "MANUFACT_CD", "'10001'" );
      lMapCapability.put( "CAP_CD", "'WIFI'" );
      lMapCapability.put( "LEVEL_CD", "'NO'" );
      lMapCapability.put( "CONFIG_LEVEL_CD", "'YES'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_CAP_LEVELS,
            lMapCapability ) );

      // run validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // check error code
      checkInventoryErrorCode( testName.getMethodName(), "AML-10511" );
   }


   /**
    * This test is to verify valid error code AML-10512: INV_CLASS_CD must not be INSRV, INREP,
    * REPREQ when inventory provided is KIT.
    *
    *
    */
   @Test
   public void test_AML_10512() throws Exception {

      // create inventory map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();
      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-KIT1'" );
      lMapInventory.put( "PART_NO_OEM", "'ATKIT'" );
      lMapInventory.put( "INV_CLASS_CD", "'KIT'" );
      lMapInventory.put( "MANUFACT_CD", "'ABC11'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // create task map
      Map<String, String> lMapCapability = new LinkedHashMap<String, String>();

      lMapCapability.put( "SERIAL_NO_OEM", "'ACFT-KIT1'" );
      lMapCapability.put( "PART_NO_OEM", "'ATKIT'" );
      lMapCapability.put( "MANUFACT_CD", "'ABC11'" );
      lMapCapability.put( "CAP_CD", "'ETOPS'" );
      lMapCapability.put( "LEVEL_CD", "'ETOPS_90'" );
      lMapCapability.put( "CONFIG_LEVEL_CD", "'ETOPS_90'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_CAP_LEVELS,
            lMapCapability ) );

      // run validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // check error code
      checkInventoryErrorCode( testName.getMethodName(), "AML-10512" );
   }


   /**
    * This test is to verify valid error code AML-XXX:
    *
    *
    */
   @Test
   public void test_AML_10533_LOOSE_TRK() throws Exception {

      // create inventory map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      // insert trk
      lMapInventory.put( "SERIAL_NO_OEM", "'TRK-00001'" );
      lMapInventory.put( "PART_NO_OEM", "'A0000220'" );
      lMapInventory.put( "MANUFACT_CD", "'11111'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      // lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "INV_COND_CD", null );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // create C_RI_INVENTORY_CAP_LEVELS map
      Map<String, String> lMapCapability = new LinkedHashMap<String, String>();

      lMapCapability.put( "SERIAL_NO_OEM", "'TRK-00001'" );
      lMapCapability.put( "PART_NO_OEM", "'A0000220'" );
      lMapCapability.put( "MANUFACT_CD", "'11111'" );
      lMapCapability.put( "CAP_CD", "'ETOPS'" );
      lMapCapability.put( "LEVEL_CD", "'ETOPS_90'" );
      lMapCapability.put( "CONFIG_LEVEL_CD", "'ETOPS_90'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_CAP_LEVELS,
            lMapCapability ) );

      // call inventory validation
      runValidateInv( "FULL" );
      checkInventoryNoWarnings( "AML-10533" );

   }


   /**
    * This test is to verify valid error code AML-XXX:
    *
    *
    */
   @Test
   public void test_AML_10533_SUB_TRK() throws Exception {

      // create C_RI_INVENTORY map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-TRK1'" );
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

      // create TRK child map
      Map<String, String> lMapInventoryChild = new LinkedHashMap<String, String>();
      lMapInventoryChild.put( "PARENT_SERIAL_NO_OEM", "'ACFT-TRK1'" );
      lMapInventoryChild.put( "PARENT_PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventoryChild.put( "PARENT_MANUFACT_CD", "'10001'" );
      lMapInventoryChild.put( "BOM_PART_CD", "'ACFT-SYS-1-1-TRK-TRK-CHILD'" );
      lMapInventoryChild.put( "EQP_POS_CD", "'1'" );
      lMapInventoryChild.put( "SERIAL_NO_OEM", "'TRK-00001'" );
      lMapInventoryChild.put( "PART_NO_OEM", "'A0000006'" );
      lMapInventoryChild.put( "MANUFACT_CD", "'11111'" );
      lMapInventoryChild.put( "INV_CLASS_CD", "'TRK'" );

      // insert TRK map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventoryChild ) );

      // create C_RI_INVENTORY_CAP_LEVELS map
      Map<String, String> lMapCapability = new LinkedHashMap<String, String>();

      lMapCapability.put( "SERIAL_NO_OEM", "'TRK-00001'" );
      lMapCapability.put( "PART_NO_OEM", "'A0000220'" );
      lMapCapability.put( "MANUFACT_CD", "'11111'" );
      lMapCapability.put( "CAP_CD", "'ETOPS'" );
      lMapCapability.put( "LEVEL_CD", "'ETOPS_90'" );
      lMapCapability.put( "CONFIG_LEVEL_CD", "'ETOPS_90'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_CAP_LEVELS,
            lMapCapability ) );

      // call inventory validation
      runValidateInv( "FULL" );
      checkInventoryNoWarnings( "AML-10533" );
   }


   /**
    * This test is to verify valid error code AML-XXX:
    *
    *
    */
   @Test
   public void test_AML_10533_BATCH() throws Exception {

      // create inventory map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      // insert trk
      lMapInventory.put( "SERIAL_NO_OEM", "'BATCH-00001'" );
      lMapInventory.put( "PART_NO_OEM", "'A0000011'" );
      lMapInventory.put( "MANUFACT_CD", "'ABC11'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      // lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "INV_COND_CD", null );
      lMapInventory.put( "OWNER_CD", "'MXI'" );
      lMapInventory.put( "BIN_QT", "'5'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // create C_RI_INVENTORY_CAP_LEVELS map
      Map<String, String> lMapCapability = new LinkedHashMap<String, String>();

      lMapCapability.put( "SERIAL_NO_OEM", "'BATCH-00001'" );
      lMapCapability.put( "PART_NO_OEM", "'A0000220'" );
      lMapCapability.put( "MANUFACT_CD", "'ABC11'" );
      lMapCapability.put( "CAP_CD", "'ETOPS'" );
      lMapCapability.put( "LEVEL_CD", "'ETOPS_90'" );
      lMapCapability.put( "CONFIG_LEVEL_CD", "'ETOPS_90'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_CAP_LEVELS,
            lMapCapability ) );

      // call inventory validation
      runValidateInv( "FULL" );
      checkInventoryNoWarnings( "AML-10533" );

   }


   /**
    * This test is to verify valid error code AML-XXX:
    *
    *
    */
   @Test
   public void test_AML_10533_SER() throws Exception {

      // create inventory map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      // insert trk
      lMapInventory.put( "SERIAL_NO_OEM", "'SER-00001'" );
      lMapInventory.put( "PART_NO_OEM", "'E0000012'" );
      lMapInventory.put( "MANUFACT_CD", "'1234567890'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      // lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "INV_COND_CD", null );
      // lMapInventory.put( "OWNER_CD", "'MXI'" );
      // lMapInventory.put( "BIN_QT", "'5'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // create C_RI_INVENTORY_CAP_LEVELS map
      Map<String, String> lMapCapability = new LinkedHashMap<String, String>();

      lMapCapability.put( "SERIAL_NO_OEM", "'SER-00001'" );
      lMapCapability.put( "PART_NO_OEM", "'E0000012'" );
      lMapCapability.put( "MANUFACT_CD", "'1234567890'" );
      lMapCapability.put( "CAP_CD", "'ETOPS'" );
      lMapCapability.put( "LEVEL_CD", "'ETOPS_90'" );
      lMapCapability.put( "CONFIG_LEVEL_CD", "'ETOPS_90'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_CAP_LEVELS,
            lMapCapability ) );

      // call inventory validation
      runValidateInv( "FULL" );
      checkInventoryNoWarnings( "AML-10533" );

   }


   /**
    * This test is to verify valid error code AML-XXX:
    *
    *
    */
   @Test
   public void test_AML_10533_LOOSE_ENG() throws Exception {

      // create inventory map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      // insert eng
      lMapInventory.clear();
      lMapInventory.put( "SERIAL_NO_OEM", "'ASSY-00001'" );
      lMapInventory.put( "PART_NO_OEM", "'ENG_ASSY_PN8'" );
      lMapInventory.put( "MANUFACT_CD", "'ABC11'" );
      lMapInventory.put( "LOC_CD", "'SCRAP'" );
      // lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "INV_COND_CD", "'SCRAP'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // create C_RI_INVENTORY_CAP_LEVELS map
      Map<String, String> lMapCapability = new LinkedHashMap<String, String>();

      lMapCapability.put( "SERIAL_NO_OEM", "'ASSY-00001'" );
      lMapInventory.put( "PART_NO_OEM", "'ENG_ASSY_PN8'" );
      lMapInventory.put( "MANUFACT_CD", "'ABC11'" );
      lMapCapability.put( "CAP_CD", "'ETOPS'" );
      lMapCapability.put( "LEVEL_CD", "'ETOPS_90'" );
      lMapCapability.put( "CONFIG_LEVEL_CD", "'ETOPS_90'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_CAP_LEVELS,
            lMapCapability ) );

      // call inventory validation
      runValidateInv( "FULL" );
      checkInventoryNoWarnings( "AML-10533" );
   }


   /**
    * This test is to verify valid error code AML-XXX:
    *
    *
    */
   @Test
   public void test_AML_10533_ATTACHED_ENG() throws Exception {

      // create inventory map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-00001'" );
      lMapInventory.put( "PART_NO_OEM", "'ACFT_ASSY_PN8'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "AC_REG_CD", "'AC-TEST'" );
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      // lMapInventory.put( "INV_COND_CD", "'INSRV'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // insert eng 1
      lMapInventory.clear();
      lMapInventory.put( "SERIAL_NO_OEM", "'ASSY-00001'" );
      lMapInventory.put( "PART_NO_OEM", "'ENG_ASSY_PN8'" );
      lMapInventory.put( "MANUFACT_CD", "'ABC11'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      // lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      // lMapInventory.put( "INV_COND_CD", "'INSRV'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // import eng 1 attached
      Map<String, String> lMapInventoryAttach = new LinkedHashMap<String, String>();
      lMapInventoryAttach.put( "PARENT_SERIAL_NO_OEM", "'ACFT-00001'" );
      lMapInventoryAttach.put( "PARENT_PART_NO_OEM", "'ACFT_ASSY_PN8'" );
      lMapInventoryAttach.put( "PARENT_MANUFACT_CD", "'10001'" );
      lMapInventoryAttach.put( "ATTACH_SERIAL_NO_OEM", "'ASSY-00001'" );
      lMapInventoryAttach.put( "ATTACH_PART_NO_OEM", "'ENG_ASSY_PN8'" );
      lMapInventoryAttach.put( "ATTACH_MANUFACT_CD", "'ABC11'" );
      lMapInventoryAttach.put( "BOM_PART_CD", "'ENG-ASSY'" );
      lMapInventoryAttach.put( "EQP_POS_CD", "'1'" );
      lMapInventoryAttach.put( "INSTALL_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_ATTACH, lMapInventoryAttach ) );

      // create C_RI_INVENTORY_CAP_LEVELS map
      Map<String, String> lMapCapability = new LinkedHashMap<String, String>();

      lMapCapability.put( "SERIAL_NO_OEM", "'ASSY-00001'" );
      lMapInventory.put( "PART_NO_OEM", "'ENG_ASSY_PN8'" );
      lMapInventory.put( "MANUFACT_CD", "'ABC11'" );
      lMapCapability.put( "CAP_CD", "'ETOPS'" );
      lMapCapability.put( "LEVEL_CD", "'ETOPS_90'" );
      lMapCapability.put( "CONFIG_LEVEL_CD", "'ETOPS_90'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_CAP_LEVELS,
            lMapCapability ) );

      // call inventory validation
      runValidateInv( "FULL" );
      checkInventoryNoWarnings( "AML-10533" );
   }

}
