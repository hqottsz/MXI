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
 * This test suite contains unit tests for Actuals Loader task validations for Part Numbers and
 * Serial Numbers
 *
 * @author Andrew Bruce
 */

public class ValidateApplicabilities extends ActualsLoaderTest {

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
      String strTCName = testName.getMethodName();
      if ( strTCName.contains( "AML_10434" ) ) {
         restoreBOMPARTAPPL();

      } else if ( strTCName.contains( "AML_10435" ) ) {
         restoreBOMPARTAPPL_2();

      }

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
      clearActualsLoaderTables();
      String strTCName = testName.getMethodName();
      if ( strTCName.contains( "AML_10434" ) ) {
         dataSetupBOMPARTAPPL();

      } else if ( strTCName.contains( "AML_10435" ) ) {
         dataSetupBOMPARTAPPL_2();

      }

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
    * <li>Error Code: AML-10429</li>
    * <li>The provided BOM_PART_CD is not applicable to the Parent Inventory based on the Part
    * Group's Applicability Range and the parent inventory's Applicability Code..</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10429_PartNotApplicableToParentByApplicInPartGroup_Sub() throws Exception {

      // create map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-SN'" );
      lMapInventory.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventory.put( "INV_CLASS_CD", "'ACFT'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventory.put( "AC_REG_CD", "'ACREG1'" );
      lMapInventory.put( "APPL_EFF_CD", "'106'" ); // outside of part group range
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
      lMapInventorySub.put( "RECEIVED_DT", "to_date('01/02/2015','mm/dd/yyyy')" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventorySub ) );

      // create map
      Map<String, String> lInventoryChildUsage = new LinkedHashMap<String, String>();
      lInventoryChildUsage.put( "SERIAL_NO_OEM", "'TRK-001'" );
      lInventoryChildUsage.put( "PART_NO_OEM", "'A0000001REC'" );
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
      validateAndCheckInventory( "test_AML_10429_PartNotApplicableToParentByApplicInPartGroup_Sub",
            "AML-10429" );
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
    * <li>Error Code: AML-10430</li>
    * <li>The provided PART_NO_OEM/MANUFACT_CD is not applicable to the Parent Inventory based on
    * the Part Number's Applicability Range within the Part Group and the Parent Inventory's
    * Applicability Code .</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10430_PartNotApplicableToParentByPartNoApplicInPartGroup_Sub()
         throws Exception {

      // create map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-SN'" );
      lMapInventory.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventory.put( "INV_CLASS_CD", "'ACFT'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventory.put( "AC_REG_CD", "'ACREG1'" );
      lMapInventory.put( "APPL_EFF_CD", "'006'" ); // outside of part group range
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
      lMapInventorySub.put( "PART_NO_OEM", "'A0000001REC'" );
      lMapInventorySub.put( "MANUFACT_CD", "'10001'" );
      lMapInventorySub.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventorySub.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventorySub.put( "RECEIVED_DT", "to_date('01/02/2015','mm/dd/yyyy')" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventorySub ) );

      // create map
      Map<String, String> lInventoryChildUsage = new LinkedHashMap<String, String>();
      lInventoryChildUsage.put( "SERIAL_NO_OEM", "'TRK-001'" );
      lInventoryChildUsage.put( "PART_NO_OEM", "'A0000001REC'" );
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
      validateAndCheckInventory(
            "test_AML_10430_PartNotApplicableToParentByPartNoApplicInPartGroup_Sub", "AML-10430" );
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
    * <li>Error Code: AML-10433</li>
    * <li>The provided PART_NO_OEM/MANUFACT_CD is not applicable to the Parent Inventory based on
    * the Part Number's Applicability Range within the Part Group and the Parent Inventory's
    * Applicability Code.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10433_PartNotApplicableToParentByPartNoApplicInPartGroup_Attach()
         throws Exception {

      // create map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-SN'" );
      lMapInventory.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventory.put( "INV_CLASS_CD", "'ACFT'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventory.put( "AC_REG_CD", "'ACREG1'" );
      lMapInventory.put( "APPL_EFF_CD", "'099'" ); // outside of part group range
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

      // create attachment map
      Map<String, String> lMapInventoryAssy = new LinkedHashMap<String, String>();

      lMapInventoryAssy.put( "SERIAL_NO_OEM", "'SN-10013A'" );
      lMapInventoryAssy.put( "PART_NO_OEM", "'ENG_ASSY_PN2'" );
      lMapInventoryAssy.put( "INV_CLASS_CD", "'ASSY'" );
      lMapInventoryAssy.put( "MANUFACT_CD", "'ABC11'" );
      lMapInventoryAssy.put( "LOC_CD", "'OPS'" );
      lMapInventoryAssy.put( "INV_COND_CD", "'INSRV'" );
      lMapInventoryAssy.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventoryAssy.put( "ASSMBL_CD", "'ACFT_CD1'" );
      lMapInventoryAssy.put( "ASSMBL_BOM_CD", "'ENG-ASSY'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventoryAssy ) );

      // create map
      Map<String, String> lInventoryUsage_Assy = new LinkedHashMap<String, String>();
      lInventoryUsage_Assy.put( "SERIAL_NO_OEM", "'SN-10013A'" );
      lInventoryUsage_Assy.put( "PART_NO_OEM", "'ENG_ASSY_PN2'" );
      lInventoryUsage_Assy.put( "MANUFACT_CD", "'ABC11'" );
      lInventoryUsage_Assy.put( "DATA_TYPE_CD", "'HOURS'" );
      lInventoryUsage_Assy.put( "TSN_QT", "1" );
      lInventoryUsage_Assy.put( "TSO_QT", "1" );
      lInventoryUsage_Assy.put( "TSI_QT", "1" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventoryUsage_Assy ) );

      // insert row for each usage parm
      lInventoryUsage.put( "DATA_TYPE_CD", "'CYCLES'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventoryUsage_Assy ) );
      lInventoryUsage.put( "DATA_TYPE_CD", "'USAGE1'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventoryUsage_Assy ) );
      lInventoryUsage.put( "DATA_TYPE_CD", "'USAGE2'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventoryUsage_Assy ) );
      lInventoryUsage.put( "DATA_TYPE_CD", "'USAGE3'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventoryUsage_Assy ) );
      lInventoryUsage.put( "DATA_TYPE_CD", "'ECYC'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventoryUsage_Assy ) );
      lInventoryUsage.put( "DATA_TYPE_CD", "'EOT'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventoryUsage_Assy ) );

      // create task map
      Map<String, String> lMapInventorySub = new LinkedHashMap<String, String>();

      lMapInventorySub.put( "PARENT_SERIAL_NO_OEM", "'ACFT-SN'" );
      lMapInventorySub.put( "PARENT_PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventorySub.put( "PARENT_MANUFACT_CD", "'10001'" );
      lMapInventorySub.put( "ATTACH_SERIAL_NO_OEM", "'SN-10013A'" );
      lMapInventorySub.put( "ATTACH_PART_NO_OEM", "'ENG_ASSY_PN2'" );
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
      validateAndCheckInventory(
            "test_AML_10433_PartNotApplicableToParentByPartNoApplicInPartGroup_Attach",
            "AML-10433" );
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
    * <li>Error Code: AML-10500</li>
    * <li>The provided APPL_EFF_CD is not applicable to the provided PART_NO_OEM/MANUFACT_CD based
    * on the Applicability Range of the Part Number within the Part Group.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10500_EffectivityCodeNotApplicableByPartNoApplicInPartGroup()
         throws Exception {

      // create map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-SN'" );
      lMapInventory.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventory.put( "INV_CLASS_CD", "'ACFT'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventory.put( "AC_REG_CD", "'ACREG1'" );
      lMapInventory.put( "APPL_EFF_CD", "'099'" ); // outside of part group range
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

      // call inventory validation and check result against expectation
      validateAndCheckInventory(
            "test_AML_10500_EffectivityCodeNotApplicableByPartNoApplicInPartGroup", "AML-10500" );
   }


   /**
    * This test is to verify valid error code AML-10434: The provided APPL_EFF_CD is not applicable
    * to the provided BOM_PART_CD based on the Applicability Range for that Part Group
    *
    *
    */
   @Test
   public void test_AML_10434() throws Exception {

      // create map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      // lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-SN'" );
      lMapInventory.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventory.put( "INV_CLASS_CD", "'ACFT'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventory.put( "AC_REG_CD", "'ACREG1'" );
      lMapInventory.put( "APPL_EFF_CD", "'006'" ); // outside of part group range
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // run validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // check error code
      checkInventoryErrorCode( testName.getMethodName(), "AML-10434" );
   }


   /**
    * This test is to verify valid error code AML-10435: The provided BOM_PART_CD is not applicable
    * to the Parent Inventory based on the Part Group's Applicability Range and the parent
    * inventory's Applicability Code.
    *
    *
    */

   @Test
   public void test_AML_10435() throws Exception {

      // create map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-SN'" );
      lMapInventory.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventory.put( "INV_CLASS_CD", "'ACFT'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventory.put( "AC_REG_CD", "'ACREG1'" );
      lMapInventory.put( "APPL_EFF_CD", "'099'" );
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // create attachment map
      Map<String, String> lMapInventoryAssy = new LinkedHashMap<String, String>();

      lMapInventoryAssy.put( "SERIAL_NO_OEM", "'SN-10013A'" );
      lMapInventoryAssy.put( "PART_NO_OEM", "'ENG_ASSY_PN2'" );
      lMapInventoryAssy.put( "INV_CLASS_CD", "'ASSY'" );
      lMapInventoryAssy.put( "MANUFACT_CD", "'ABC11'" );
      lMapInventoryAssy.put( "LOC_CD", "'OPS'" );
      lMapInventoryAssy.put( "INV_COND_CD", "'INSRV'" );
      lMapInventoryAssy.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventoryAssy.put( "ASSMBL_CD", "'ENG_CD1'" );
      lMapInventoryAssy.put( "ASSMBL_BOM_CD", "'ENG_CD1'" );
      // lMapInventoryAssy.put( "APPL_EFF_CD", "'099'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventoryAssy ) );

      // create task map
      Map<String, String> lMapInventorySub = new LinkedHashMap<String, String>();

      lMapInventorySub.put( "PARENT_SERIAL_NO_OEM", "'ACFT-SN'" );
      lMapInventorySub.put( "PARENT_PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventorySub.put( "PARENT_MANUFACT_CD", "'10001'" );
      lMapInventorySub.put( "ATTACH_SERIAL_NO_OEM", "'SN-10013A'" );
      lMapInventorySub.put( "ATTACH_PART_NO_OEM", "'ENG_ASSY_PN2'" );
      lMapInventorySub.put( "ATTACH_MANUFACT_CD", "'ABC11'" );
      lMapInventorySub.put( "BOM_PART_CD", "'ENG-ASSY'" );
      lMapInventorySub.put( "EQP_POS_CD", "'1'" );
      lMapInventorySub.put( "INSTALL_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_ATTACH, lMapInventorySub ) );

      // run validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // check error code
      checkInventoryErrorCode( testName.getMethodName(), "AML-10435" );
   }


   // ==============================================================================================
   /**
    * This function is to update appl_eff_ldesc in eqp_bom_part table for appl_eff test cases.
    *
    *
    */
   public void dataSetupBOMPARTAPPL() {
      String lQuery =
            "UPDATE eqp_bom_part SET appl_eff_ldesc='001-005' where assmbl_cd='ACFT_CD1' and assmbl_bom_id=0";
      executeSQL( lQuery );

   }


   /**
    * This function is to restore dataon appl_eff_ldesc in eqp_bom_part table for appl_eff test
    * cases.
    *
    *
    */
   public void restoreBOMPARTAPPL() {
      String lQuery =
            "UPDATE eqp_bom_part SET appl_eff_ldesc=null where assmbl_cd='ACFT_CD1' and assmbl_bom_id=0";
      executeSQL( lQuery );

   }


   /**
    * This function is to update appl_eff_ldesc in eqp_bom_part table for appl_eff test cases.
    *
    *
    */
   public void dataSetupBOMPARTAPPL_2() {
      String lQuery =
            "UPDATE eqp_bom_part SET appl_eff_ldesc='001-005' where assmbl_cd='ACFT_CD1' and INV_CLASS_CD='ASSY' and BOM_PART_CD='ENG-ASSY'";
      executeSQL( lQuery );

      // String lQuery2 =
      // "UPDATE eqp_bom_part SET appl_eff_ldesc='001-005' where assmbl_cd='ENG_CD1' and
      // assmbl_bom_id=0";
      // executeSQL( lQuery2 );

   }


   /**
    * This function is to restore dataon appl_eff_ldesc in eqp_bom_part table for appl_eff test
    * cases.
    *
    *
    */
   public void restoreBOMPARTAPPL_2() {
      String lQuery =
            "UPDATE eqp_bom_part SET appl_eff_ldesc=null where assmbl_cd='ACFT_CD1' and INV_CLASS_CD='ASSY' and BOM_PART_CD='ENG-ASSY'";
      executeSQL( lQuery );

      // String lQuery2 =
      // "UPDATE eqp_bom_part SET appl_eff_ldesc=null where assmbl_cd='ENG_CD1' and
      // assmbl_bom_id=0";
      // executeSQL( lQuery2 );

   }

}
