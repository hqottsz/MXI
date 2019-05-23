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

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.mxi.mx.util.ActualsLoaderTest;
import com.mxi.mx.util.TableUtil;


/**
 * This test suite contains unit tests for Actuals Loader task validations for Part Numbers and
 * Serial Numbers
 *
 * @author Andrew Bruce
 */

public class ValidateDuplicates extends ActualsLoaderTest {

   ArrayList<String> updateTables = new ArrayList<String>() {

      {
         add( "UPDATE inv_inv SET SERIAL_NO_OEM='SN0000P1' WHERE INV_NO_DB_ID='4650' and CONFIG_POS_SDESC='ENG-ASSY (1) ->ENG-SYS-1-1-TRK-P1' and INV_NO_SDESC='Tracked Part (PN: E0000001, SN: XXX)'" );

      }
   };

   ArrayList<String> restoreTables = new ArrayList<String>() {

      {
         add( "UPDATE inv_inv SET SERIAL_NO_OEM='XXX' WHERE INV_NO_DB_ID='4650' and CONFIG_POS_SDESC='ENG-ASSY (1) ->ENG-SYS-1-1-TRK-P1' and INV_NO_SDESC='Tracked Part (PN: E0000001, SN: XXX)'" );

      }
   };


   /**
    * Clean up after each individual test
    *
    * @throws Exception
    */
   @Override
   @After
   public void after() throws Exception {

      // clean up the event data
      ClassDataSetup( restoreTables );
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
      ClassDataSetup( updateTables );
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
    * <li>Error Code: AML-10101</li>
    * <li>SERIAL_NO_OEM, PART_NO_OEM, MANUFACT_CD combination is defined multiple times on the same
    * table. This combination must be unique.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10101_DuplicatePartNoSerialNoManufCd_Sub() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'SN-10013P'" );
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
      Map<String, String> lMapInventorySub = new LinkedHashMap<String, String>();

      lMapInventorySub.put( "PARENT_SERIAL_NO_OEM", "'SN-10023P'" );
      lMapInventorySub.put( "PARENT_PART_NO_OEM", "'A0000005'" );
      lMapInventorySub.put( "PARENT_MANUFACT_CD", "'10001'" );
      lMapInventorySub.put( "BOM_PART_CD", "'ACFT-SYS-1-1-TRK-TRK-CHILD'" );
      lMapInventorySub.put( "EQP_POS_CD", "'1'" );
      lMapInventorySub.put( "SERIAL_NO_OEM", "'SN-10023C'" );
      lMapInventorySub.put( "PART_NO_OEM", "'A0000006'" );
      lMapInventorySub.put( "MANUFACT_CD", "'11111'" );
      lMapInventorySub.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventorySub.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventorySub ) );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventorySub ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10101_DuplicatePartNoSerialNoManufCd_Sub", "AML-10101" );
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
    * <li>Error Code: AML-10102</li>
    * <li>AC_REG_CD is present multiple times. This value must be unique.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10102_DuplicateRegistrationCdInTable() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-SN'" );
      lMapInventory.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventory.put( "INV_CLASS_CD", "'ACFT'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventory.put( "AC_REG_CD", "'10102'" ); // duplicate exists in test database
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-SN2'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10102_DuplicateRegistrationCdInTable", "AML-10102" );
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
    * <li>Error Code: AML-10103</li>
    * <li>ATTACH item references a position on a configuration slot that is also referenced by
    * another record. The combination of PARENT_SERIAL_NO_OEM, PARENT_PART_NO_OEM,
    * PARENT_MANUFACT_CD, BOM_PART_CD, and EQP_POS_CD must be unique across both the attachment and
    * subinventory extracts.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10103_TwoAttachmentsIntoOneSlot_Attach() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'SN-10013P'" );
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
      Map<String, String> lMapInventoryAssy = new LinkedHashMap<String, String>();

      lMapInventoryAssy.put( "SERIAL_NO_OEM", "'SN-10013A'" );
      lMapInventoryAssy.put( "PART_NO_OEM", "'ENG_ASSY_PN1'" );
      lMapInventoryAssy.put( "INV_CLASS_CD", "'ASSY'" );
      lMapInventoryAssy.put( "MANUFACT_CD", "'ABC11'" );
      lMapInventoryAssy.put( "LOC_CD", "'OPS'" );
      lMapInventoryAssy.put( "INV_COND_CD", "'INSRV'" );
      lMapInventoryAssy.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventoryAssy.put( "ASSMBL_CD", "'ACFT_CD1'" );
      lMapInventoryAssy.put( "ASSMBL_BOM_CD", "'ENG-ASSY'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventoryAssy ) );

      lMapInventoryAssy.put( "SERIAL_NO_OEM", "'SN-10013B'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventoryAssy ) );

      // create task map
      Map<String, String> lMapInventorySub = new LinkedHashMap<String, String>();

      lMapInventorySub.put( "PARENT_SERIAL_NO_OEM", "'SN-10013P'" );
      lMapInventorySub.put( "PARENT_PART_NO_OEM", "'A0000005'" );
      lMapInventorySub.put( "PARENT_MANUFACT_CD", "'10001'" );
      lMapInventorySub.put( "ATTACH_SERIAL_NO_OEM", "'SN-10013A'" );
      lMapInventorySub.put( "ATTACH_PART_NO_OEM", "'ENG_ASSY_PN1'" );
      lMapInventorySub.put( "ATTACH_MANUFACT_CD", "'ABC11'" );
      lMapInventorySub.put( "BOM_PART_CD", "'ENG-ASSY'" ); // value not known in Mx
      lMapInventorySub.put( "EQP_POS_CD", "'1'" );
      lMapInventorySub.put( "INSTALL_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_ATTACH, lMapInventorySub ) );

      lMapInventorySub.put( "ATTACH_SERIAL_NO_OEM", "'SN-10013B'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_ATTACH, lMapInventorySub ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10103_TwoAttachmentsIntoOneSlot_Attach", "AML-10103" );
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
    * <li>Error Code: AML-10104</li>
    * <li>INVENTORY field SERIAL_NO_OEM is mandatory and must be specified.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10104_DuplicateUsage_Usage() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'SN-10023'" ); // field not provided
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
      Map<String, String> lMapInventoryUsage = new LinkedHashMap<String, String>();

      lMapInventoryUsage.put( "SERIAL_NO_OEM", "'SN-10023'" );
      lMapInventoryUsage.put( "PART_NO_OEM", "'A0000005'" );
      lMapInventoryUsage.put( "MANUFACT_CD", "'10001'" );
      lMapInventoryUsage.put( "DATA_TYPE_CD", "'HOURS'" );
      lMapInventoryUsage.put( "TSN_QT", "0" );
      lMapInventoryUsage.put( "TSI_QT", "0" );
      lMapInventoryUsage.put( "TSO_QT", "0" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lMapInventoryUsage ) );

      // inserted a second time
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lMapInventoryUsage ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10104_DuplicateUsage_Usage", "AML-10104" );
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
    * <li>Error Code: AML-10105</li>
    * <li>ATTACH_SERIAL_NO_OEM, ATTACH_PART_NO_OEM, ATTACH_MANUFACT_CD is present multiple times.
    * This combination of values must be unique.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10105_DuplicateAttachmentRows_Attach() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'SN-10013P'" );
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
      Map<String, String> lMapInventoryAssy = new LinkedHashMap<String, String>();

      lMapInventoryAssy.put( "SERIAL_NO_OEM", "'SN-10013A'" );
      lMapInventoryAssy.put( "PART_NO_OEM", "'ENG_ASSY_PN1'" );
      lMapInventoryAssy.put( "INV_CLASS_CD", "'ASSY'" );
      lMapInventoryAssy.put( "MANUFACT_CD", "'ABC11'" );
      lMapInventoryAssy.put( "LOC_CD", "'OPS'" );
      lMapInventoryAssy.put( "INV_COND_CD", "'INSRV'" );
      lMapInventoryAssy.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventoryAssy.put( "ASSMBL_CD", "'ACFT_CD1'" );
      lMapInventoryAssy.put( "ASSMBL_BOM_CD", "'ENG-ASSY'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventoryAssy ) );

      // create task map
      Map<String, String> lMapInventorySub = new LinkedHashMap<String, String>();

      lMapInventorySub.put( "PARENT_SERIAL_NO_OEM", "'SN-10013P'" );
      lMapInventorySub.put( "PARENT_PART_NO_OEM", "'A0000005'" );
      lMapInventorySub.put( "PARENT_MANUFACT_CD", "'10001'" );
      lMapInventorySub.put( "ATTACH_SERIAL_NO_OEM", "'SN-10013A'" );
      lMapInventorySub.put( "ATTACH_PART_NO_OEM", "'ENG_ASSY_PN1'" );
      lMapInventorySub.put( "ATTACH_MANUFACT_CD", "'ABC11'" );
      lMapInventorySub.put( "BOM_PART_CD", "'ENG-ASSY'" ); // value not known in Mx
      lMapInventorySub.put( "EQP_POS_CD", "'1'" );
      lMapInventorySub.put( "INSTALL_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_ATTACH, lMapInventorySub ) );

      lMapInventorySub.put( "EQP_POS_CD", "'2'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_ATTACH, lMapInventorySub ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10105_DuplicateAttachmentRows_Attach", "AML-10105" );
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
    * <li>Error Code: AML-10106</li>
    * <li>The given aircraft registration cd (ac_reg_cd) already exists in Maintenix.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10106_DuplicateRegistrationCdInMx() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-SN'" );
      lMapInventory.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventory.put( "INV_CLASS_CD", "'ACFT'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'INSRV'" );
      lMapInventory.put( "AC_REG_CD", "'001'" ); // duplicate exists in test database
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10106_DuplicateRegistrationCdInMx", "AML-10106" );
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
    * <li>Error Code: AML-10107</li>
    * <li>SERIAL_NO_OEM, PART_NO_OEM, MANUFACT_CD is present in both INVENTORY and SUB_INVENTORY
    * tables. This combination must be unique.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10107_InventoryInBothTables() throws Exception {

      // create task map
      Map<String, String> lMapInventoryAcft = new LinkedHashMap<String, String>();

      lMapInventoryAcft.put( "SERIAL_NO_OEM", "'ACFT-SN'" );
      lMapInventoryAcft.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventoryAcft.put( "INV_CLASS_CD", "'ACFT'" );
      lMapInventoryAcft.put( "MANUFACT_CD", "'10001'" );
      lMapInventoryAcft.put( "LOC_CD", "'OPS'" );
      lMapInventoryAcft.put( "INV_COND_CD", "'INSRV'" );
      lMapInventoryAcft.put( "AC_REG_CD", "'ACREG1'" );
      lMapInventoryAcft.put( "INV_OPER_CD", "'NORM'" );
      lMapInventoryAcft.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventoryAcft.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventoryAcft ) );

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'CHILD1'" );
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
      Map<String, String> lInventoryChild = new LinkedHashMap<String, String>();

      lInventoryChild.put( "PARENT_SERIAL_NO_OEM", "'ACFT-SN'" );
      lInventoryChild.put( "PARENT_PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lInventoryChild.put( "PARENT_MANUFACT_CD", "'10001'" );
      lInventoryChild.put( "BOM_PART_CD", "'ACFT-SYS-1-1-TRK-TRK-CHILD'" );
      lInventoryChild.put( "EQP_POS_CD", "'1'" );
      lInventoryChild.put( "SERIAL_NO_OEM", "'CHILD1'" );
      lInventoryChild.put( "PART_NO_OEM", "'A0000005'" );
      lInventoryChild.put( "MANUFACT_CD", "'10001'" );
      lInventoryChild.put( "INV_CLASS_CD", "'TRK'" );
      lInventoryChild.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lInventoryChild ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10107_InventoryInBothTables", "AML-10107" );
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
    * <li>Error Code: AML-10108</li>
    * <li>SERIAL_NO_OEM, PART_NO_OEM, MANUFACT_CD, INV_COND_CD, LOC_CD, OWNER_CD combination is
    * defined multiple times on the same table. This combination must be unique.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10108_DuplicateInventory() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'BATCH-00'" );
      lMapInventory.put( "PART_NO_OEM", "'A0000009'" );
      lMapInventory.put( "INV_CLASS_CD", "'BATCH'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'RFI'" );
      lMapInventory.put( "BIN_QT", "1" ); // field not provided
      lMapInventory.put( "OWNER_CD", "'MXI'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "ASSMBL_CD", "'ACFT_CD1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      lMapInventory.put( "BIN_QT", "2" ); // field not provided

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10108_DuplicateInventory", "AML-10108" );
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
    * <li>Error Code: AML-10109</li>
    * <li>SERIAL_NO_OEM, PART_NO_OEM, MANUFACT_CD is present in both INVENTORY and SUB_INVENTORY
    * tables. This combination must be unique.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10109_DuplicateInventoryAndSubInventory() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'SN-10013P'" );
      lMapInventory.put( "PART_NO_OEM", "'A0000005'" );
      lMapInventory.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'RFI'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "ASSMBL_CD", "'ACFT_CD1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      lMapInventory.put( "SERIAL_NO_OEM", "'SN-10023C'" );
      lMapInventory.put( "PART_NO_OEM", "'A0000006'" );
      lMapInventory.put( "MANUFACT_CD", "'11111'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // create task map
      Map<String, String> lMapInventorySub = new LinkedHashMap<String, String>();

      lMapInventorySub.put( "PARENT_SERIAL_NO_OEM", "'SN-10023P'" );
      lMapInventorySub.put( "PARENT_PART_NO_OEM", "'A0000005'" );
      lMapInventorySub.put( "PARENT_MANUFACT_CD", "'10001'" );
      lMapInventorySub.put( "BOM_PART_CD", "'ACFT-SYS-1-1-TRK-TRK-CHILD'" );
      lMapInventorySub.put( "EQP_POS_CD", "'1'" );
      lMapInventorySub.put( "SERIAL_NO_OEM", "'SN-10023C'" );
      lMapInventorySub.put( "PART_NO_OEM", "'A0000006'" );
      lMapInventorySub.put( "MANUFACT_CD", "'11111'" );
      lMapInventorySub.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventorySub.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventorySub ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10109_DuplicateInventoryAndSubInventory", "AML-10109" );
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
    * <li>Error Code: AML-10110</li>
    * <li>SERIAL_NO_OEM, PART_NO_OEM, MANUFACT_CD combination is defined multiple times on the same
    * table. This combination must be unique.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10110_DuplicateInventoryRows() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'SN-10013P'" );
      lMapInventory.put( "PART_NO_OEM", "'A0000005'" );
      lMapInventory.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'RFI'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "ASSMBL_CD", "'ACFT_CD1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10110_DuplicateInventoryRows", "AML-10110" );
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
    * <li>Error Code: AML-10111</li>
    * <li>The provided PARENT_SERIAL_NO_OEM, PARENT_PART_NO_OEM, PARENT_MANUFACT_CD, BOM_PART_CD,
    * and EQP_POS_CD combination cannot appear more than once.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10111_DuplicateSubInventory() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'SN-10013P'" );
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
      Map<String, String> lMapInventorySub = new LinkedHashMap<String, String>();

      lMapInventorySub.put( "PARENT_SERIAL_NO_OEM", "'SN-10013P'" );
      lMapInventorySub.put( "PARENT_PART_NO_OEM", "'A0000005'" );
      lMapInventorySub.put( "PARENT_MANUFACT_CD", "'10001'" );
      lMapInventorySub.put( "BOM_PART_CD", "'ACFT-SYS-1-1-TRK-TRK-CHILD'" );
      lMapInventorySub.put( "EQP_POS_CD", "'1'" );
      lMapInventorySub.put( "SERIAL_NO_OEM", "'SN-10023C1'" );
      lMapInventorySub.put( "PART_NO_OEM", "'A0000006'" );
      lMapInventorySub.put( "MANUFACT_CD", "'11111'" );
      lMapInventorySub.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventorySub.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventorySub ) );

      // update serial number and insert as another row
      lMapInventorySub.put( "SERIAL_NO_OEM", "'SN-10023C2'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventorySub ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10111_DuplicateSubInventory", "AML-10111" );
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
    * <li>Error Code: AML-10262</li>
    * <li>INVENTORY SERIAL_NO_OEM/PART_NO_OEM/MANUFACT_CD combination already exists in
    * Maintenix.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */

   @Test
   public void test_AML_10262_ExistingInventory_Sub() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'E0023'" );
      lMapInventory.put( "PART_NO_OEM", "'ENG_ASSY_PN1'" );
      lMapInventory.put( "INV_CLASS_CD", "'ASSY'" );
      lMapInventory.put( "MANUFACT_CD", "'ABC11'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'RFI'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "ASSMBL_CD", "'ENG_CD1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // create map
      Map<String, String> lInventoryUsage = new LinkedHashMap<String, String>();
      lInventoryUsage.put( "SERIAL_NO_OEM", "'E0023'" );
      lInventoryUsage.put( "PART_NO_OEM", "'ENG_ASSY_PN1'" );
      lInventoryUsage.put( "MANUFACT_CD", "'ABC11'" );
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
      lInventoryUsage.put( "DATA_TYPE_CD", "'ECYC'" );
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE, lInventoryUsage ) );
      lInventoryUsage.put( "DATA_TYPE_CD", "'EOT'" );
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE, lInventoryUsage ) );

      // create task map
      Map<String, String> lMapInventorySub = new LinkedHashMap<String, String>();

      lMapInventorySub.put( "PARENT_SERIAL_NO_OEM", "'E0023'" );
      lMapInventorySub.put( "PARENT_PART_NO_OEM", "'ENG_ASSY_PN1'" );
      lMapInventorySub.put( "PARENT_MANUFACT_CD", "'ABC11'" );
      lMapInventorySub.put( "BOM_PART_CD", "'ENG-SYS-1-1-TRK-P1'" );
      lMapInventorySub.put( "EQP_POS_CD", "'1'" );
      lMapInventorySub.put( "SERIAL_NO_OEM", "'SN0000P1'" );
      lMapInventorySub.put( "PART_NO_OEM", "'E0000001'" );
      lMapInventorySub.put( "MANUFACT_CD", "'10001'" );
      lMapInventorySub.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventorySub.put( "INV_CLASS_CD", "'TRK'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventorySub ) );

      // create map
      Map<String, String> lInventorySubUsage = new LinkedHashMap<String, String>();
      lInventorySubUsage.put( "SERIAL_NO_OEM", "'ESN00979'" );
      lInventorySubUsage.put( "PART_NO_OEM", "'E0000001'" );
      lInventorySubUsage.put( "MANUFACT_CD", "'10001'" );
      lInventorySubUsage.put( "DATA_TYPE_CD", "'HOURS'" );
      lInventorySubUsage.put( "TSN_QT", "1" );
      lInventorySubUsage.put( "TSO_QT", "1" );
      lInventorySubUsage.put( "TSI_QT", "1" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventorySubUsage ) );

      // insert row for each usage parm
      lInventorySubUsage.put( "DATA_TYPE_CD", "'CYCLES'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventorySubUsage ) );
      lInventorySubUsage.put( "DATA_TYPE_CD", "'USAGE1'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventorySubUsage ) );
      lInventorySubUsage.put( "DATA_TYPE_CD", "'USAGE2'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventorySubUsage ) );
      lInventorySubUsage.put( "DATA_TYPE_CD", "'USAGE3'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventorySubUsage ) );
      lInventorySubUsage.put( "DATA_TYPE_CD", "'ECYC'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventorySubUsage ) );
      lInventorySubUsage.put( "DATA_TYPE_CD", "'EOT'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventorySubUsage ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10262_ExistingInventory_Sub", "AML-10262" );
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
    * <li>Error Code: AML-10263</li>
    * <li>INVENTORY SERIAL_NO_OEM/PART_NO_OEM/MANUFACT_CD combination already exists in
    * Maintenix.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10263_ExistingInventory() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'SN000016'" ); // already exists in test database
      lMapInventory.put( "PART_NO_OEM", "'A0000001'" );
      lMapInventory.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'RFI'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "ASSMBL_CD", "'ACFT_CD1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10263_ExistingInventory", "AML-10263" );
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
    * <li>Error Code: AML-10503</li>
    * <li>Maintenix Baseline - The provided PART_NO_OEM/MANUFACT_CD combination exists multiple
    * times in Maintenix. It should be unique and case insensitive</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Ignore
   @Test
   public void test_AML_10503_NonUniquePartNoManufInv() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-10005'" );
      lMapInventory.put( "PART_NO_OEM", "'A0000001A'" );
      lMapInventory.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" ); // duplicate fields in database
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'RFI'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // call inventory validation
      runALValidateInventory();

      // call inventory validation and check result against expectation
      checkInventory( "test_AML_10503_NonUniquePartNoManufInv", "AML-10503" );
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
    * <li>Error Code: AML-10504</li>
    * <li>Maintenix Baseline - The provided PART_NO_OEM/MANUFACT_CD combination exists multiple
    * times in Maintenix. It should be unique and case insensitive</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Ignore
   @Test
   public void test_AML_10504_NonUniquePartNoManufSubInv() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'SN-10013P'" );
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
      Map<String, String> lMapInventorySub = new LinkedHashMap<String, String>();

      lMapInventorySub.put( "PARENT_SERIAL_NO_OEM", "'SN-10013P'" );
      lMapInventorySub.put( "PARENT_PART_NO_OEM", "'A0000005'" );
      lMapInventorySub.put( "PARENT_MANUFACT_CD", "'10001'" );
      lMapInventorySub.put( "BOM_PART_CD", "'ACFT-SYS-1-1-TRK-TRK-CHILD'" );
      lMapInventorySub.put( "EQP_POS_CD", "'1'" );
      lMapInventorySub.put( "SERIAL_NO_OEM", "'SN-10023C'" );
      lMapInventorySub.put( "PART_NO_OEM", "'A0000001A'" );
      lMapInventorySub.put( "MANUFACT_CD", "'10001'" );
      lMapInventorySub.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventorySub.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventorySub ) );

      // call inventory validation
      runALValidateInventory();

      // call inventory validation and check result against expectation
      checkInventory( "test_AML_10504_NonUniquePartNoManufSubInv", "AML-10504" );
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
    * <li>Error Code: TBD-10202</li>
    * <li>Mandatory field missing: MANUFACT_CD (only mandatory when the provided PART_NO_OEM exists
    * multiple times in Maintenix)</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   // OPER-21510, not valid TC.
   @Ignore
   @Test
   public void test_TBD_10202_NonUniquePartNoManufInv() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-10005'" );
      lMapInventory.put( "PART_NO_OEM", "'A0000002A'" );
      lMapInventory.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventory.put( "MANUFACT_CD", "''" ); // duplicate fields in database
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'RFI'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // call inventory validation
      runALValidateInventory();

      // call inventory validation and check result against expectation
      checkInventory( "test_TBD-10202_NonUniquePartNoManufInv", "TBD-10202" );
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
    * <li>Error Code: TBD-10204</li>
    * <li>Mandatory field missing: MANUFACT_CD (only mandatory when the provided PART_NO_OEM exists
    * multiple times in Maintenix)</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   // OPER-21510, not valid TC.
   @Ignore
   @Test
   public void test_TBD_10204_NonUniquePartNoManufSubInv() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'SN-10013P'" );
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
      Map<String, String> lMapInventorySub = new LinkedHashMap<String, String>();

      lMapInventorySub.put( "PARENT_SERIAL_NO_OEM", "'SN-10023P'" );
      lMapInventorySub.put( "PARENT_PART_NO_OEM", "'A0000005'" );
      lMapInventorySub.put( "PARENT_MANUFACT_CD", "'10001'" );
      lMapInventorySub.put( "BOM_PART_CD", "'ACFT-SYS-1-1-TRK-TRK-CHILD'" );
      lMapInventorySub.put( "EQP_POS_CD", "'1'" );
      lMapInventorySub.put( "SERIAL_NO_OEM", "'SN-10023C'" );
      lMapInventorySub.put( "PART_NO_OEM", "'A0000002A'" );
      lMapInventorySub.put( "MANUFACT_CD", "''" );
      lMapInventorySub.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventorySub.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventorySub ) );

      // call inventory validation
      runALValidateInventory();

      // call inventory validation and check result against expectation
      checkInventory( "test_TBD-10204_NonUniquePartNoManufSubInv", "TBD-10204" );
   }


   /**
    * Setup data for the whole class tests
    *
    *
    * @throws SQLException
    */
   protected void ClassDataSetup( ArrayList<String> tables ) {
      try {
         for ( String aUpdateQuery : tables ) {
            PreparedStatement lStatement = getConnection().prepareStatement( aUpdateQuery );
            lStatement.executeUpdate( aUpdateQuery );
            commit();
         }
      } catch ( SQLException e ) {
         e.printStackTrace();
      }
   }
}
