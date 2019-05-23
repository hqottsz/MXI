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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;
import com.mxi.mx.util.ActualsLoaderTest;
import com.mxi.mx.util.StoreProcedureRunner;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.TestConstants;
import com.mxi.mx.util.WhereClause;


/**
 * This test suite contains unit tests for Actuals Loader task validations for Part Numbers and
 * Serial Numbers
 *
 * @author Andrew Bruce
 */

public class ValidateOtherInventoryDetails extends ActualsLoaderTest {

   @Rule
   public TestName testName = new TestName();

   public String iPN_ACFT = "ACFT_ASSY_PN1";
   public String iMANUAL_ACFT = "10001";
   public String iACFTASSMBL_CD = "ACFT_CD1";

   public String iPN_TRK = "A0000006";
   public String iMANUAL_TRK = "11111";

   simpleIDs iPNIDs = null;
   String iBOM_id = null;
   String iBomPartCd = "ACFT-SYS-1-1-TRK-TRK-TRK-MID";
   String iBomPartCdNew = "ACFT-SYS-1-1-TRK-TRK-TRK-mid";


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
      if ( strTCName.contains( "TBD_10202" ) || strTCName.contains( "TBD_10205" ) ) {
         restoreDupPNOEM();

      } else if ( strTCName.contains( "AML_10246" ) ) {

         restoreOWNER_CD();
      } else if ( strTCName.contains( "AML_10210" ) ) {
         restoreINV_LOC();

      } else if ( strTCName.contains( "AML_10308" ) ) {
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
      String strTCName = testName.getMethodName();
      if ( strTCName.contains( "TBD_10202" ) ) {
         dataSetupDupPNOEM();

      } else if ( strTCName.contains( "TBD_10205" ) ) {
         dataSetupDupPNOEM_TRK();

      } else if ( strTCName.contains( "AML_10246" ) ) {
         dataSetupOWNER_CD();
      } else if ( strTCName.contains( "AML_10210" ) ) {
         dataSetupINV_LOC();

      } else if ( strTCName.contains( "AML_10308" ) ) {
         dataSetupBOMPARTAPPL_2();

      }
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
    * <li>Error Code: AML-10005</li>
    * <li>INVENTORY field LOC_CD is mandatory and must be specified.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10005_MissingLocationCd() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'TRK-10005'" );
      lMapInventory.put( "PART_NO_OEM", "'A0000005'" );
      lMapInventory.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );

      // lMapInventory.put( "LOC_CD", "'OPS'" );// field not provided
      // lMapInventory.put( "INV_COND_CD", "'RFI'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "ASSMBL_CD", "'ACFT_CD1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10005_MissingLocationCd", "AML-10005" );
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
    * <li>Error Code: AML-10207</li>
    * <li>INVENTORY field AUTHORITY_CD does not exist in Maintenix ORG_AUTHORiTY table.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10207_UnknownAuthorityCd() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'TRK-10005'" );
      lMapInventory.put( "PART_NO_OEM", "'A0000005'" );
      lMapInventory.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      // lMapInventory.put( "INV_COND_CD", "'RFI'" );
      lMapInventory.put( "AUTHORITY_CD", "'XXMXIXX'" ); // value unknown to Mx
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "ASSMBL_CD", "'ACFT_CD1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10207_UnknownAuthorityCd", "AML-10207" );
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
    * <li>Error Code: AML-10209</li>
    * <li>INVENTORY field LOC_CD does not exist in Maintenix INV_LOC table.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10209_UnknownLocationCd() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'TRK-10005'" );
      lMapInventory.put( "PART_NO_OEM", "'A0000005'" );
      lMapInventory.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'XXOPSXX'" ); // value unknown to Mx
      // lMapInventory.put( "INV_COND_CD", "'RFI'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "ASSMBL_CD", "'ACFT_CD1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10209_UnknownLocationCd", "AML-10209" );
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
    * <li>Error Code: AML-10211</li>
    * <li>INVENTORY field VENDOR_CD does not exist in Maintenix ORG_VENDOR table.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10211_UnknownVendorCd() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'TRK-10005'" );
      lMapInventory.put( "PART_NO_OEM", "'A0000005'" );
      lMapInventory.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      // lMapInventory.put( "INV_COND_CD", "'RFI'" );
      lMapInventory.put( "VENDOR_CD", "'XXMXIXX'" ); // value unknown to Mx
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "ASSMBL_CD", "'ACFT_CD1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10211_UnknownVendorCd", "AML-10211" );
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
    * <li>Error Code: AML-10213</li>
    * <li>INVENTORY field INV_COND_CD does not exist in Maintenix REF_INV_COND table.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10213_UnknownInvConditionCd() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'TRK-10004'" );
      lMapInventory.put( "PART_NO_OEM", "'A0000005'" );
      lMapInventory.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'XXRFIXX'" ); // value unknown in mx
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "ASSMBL_CD", "'ACFT_CD1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10213_UnknownInvConditionCd", "AML-10213" );
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
    * <li>Error Code: AML-10227</li>
    * <li>INVENTORY ASSMBL_CD/BOM_PART_CD combination does not exist in Maintenix Baseline.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10227_UnknownAssmblCdBomPartCd() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'TRK-10005'" );
      lMapInventory.put( "PART_NO_OEM", "'A0000005'" );
      lMapInventory.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      // lMapInventory.put( "INV_COND_CD", "'RFI'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "ASSMBL_CD", "'ACFT_CD1'" );
      lMapInventory.put( "ASSMBL_BOM_CD", "'XUNKX'" ); // value unknown to Mx

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10227_UnknownAssmblCdBomPartCd", "AML-10227" );
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
    * <li>Error Code: AML-10239</li>
    * <li>SUB_INVENTORY field AUTHORITY_CD does not exist in Maintenix ORG_AUTHORiTY table.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10239_UnknownAuthorityCd_Sub() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'SN-10013P'" );
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
      lMapInventorySub.put( "AUTHORITY_CD", "'XXMXIXX'" ); // value unknown to Mx
      lMapInventorySub.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventorySub ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10239_UnknownAuthorityCd_Sub", "AML-10239" );
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
    * <li>Error Code: AML-10241</li>
    * <li>SUB_INVENTORY field VENDOR_CD does not exist in Maintenix ORG_VENDOR table.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10241_UnknownVendorCd_Sub() throws Exception {

      // create task map
      Map<String, String> lInventoryParent = new LinkedHashMap<String, String>();
      lInventoryParent.put( "SERIAL_NO_OEM", "'TT-PARENT'" );
      lInventoryParent.put( "PART_NO_OEM", "'A0000005'" );
      lInventoryParent.put( "INV_CLASS_CD", "'TRK'" );
      lInventoryParent.put( "MANUFACT_CD", "'10001'" );
      lInventoryParent.put( "LOC_CD", "'OPS'" );
      // lInventoryParent.put( "INV_COND_CD", "'RFI'" );
      lInventoryParent.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lInventoryParent.put( "ASSMBL_CD", "'ACFT_CD1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lInventoryParent ) );

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
      lInventoryChild.put( "VENDOR_CD", "'XXMXIXX'" ); // value unknown to Mx
      lInventoryChild.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lInventoryChild ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10241_UnknownVendorCd_Sub", "AML-10241" );
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
    * <li>Error Code: AML-10249</li>
    * <li>ATTACH field BOM_PART_CD does not exist in Maintenix Baseline.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10249_UnknownBOMPartCd_Attach() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'SN-10013P'" );
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
      Map<String, String> lMapInventoryAssy = new LinkedHashMap<String, String>();

      lMapInventoryAssy.put( "SERIAL_NO_OEM", "'SN-10013A'" );
      lMapInventoryAssy.put( "PART_NO_OEM", "'ENG_ASSY_PN1'" );
      lMapInventoryAssy.put( "INV_CLASS_CD", "'ASSY'" );
      lMapInventoryAssy.put( "MANUFACT_CD", "'ABC11'" );
      lMapInventoryAssy.put( "LOC_CD", "'OPS'" );
      // lMapInventoryAssy.put( "INV_COND_CD", "'INSRV'" );
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
      lMapInventorySub.put( "BOM_PART_CD", "'XUNKNX'" ); // value not known in Mx
      lMapInventorySub.put( "EQP_POS_CD", "'1'" );
      lMapInventorySub.put( "INSTALL_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_ATTACH, lMapInventorySub ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10249_UnknownBOMPartCd_Attach", "AML-10249" );
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
    * <li>Error Code: AML-10251</li>
    * <li>ATTACH BOM_PART_CD/EQP_POS_CD combination does not exist in Maintenix Baseline.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10251_UnknownBOMPartCdPosCd_Attach() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'SN-10013P'" );
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
      lMapInventorySub.put( "EQP_POS_CD", "'3'" );
      lMapInventorySub.put( "INSTALL_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_ATTACH, lMapInventorySub ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10251_UnknownBOMPartCdPosCd_Attach", "AML-10251" );
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
    * <li>Error Code: AML-10414</li>
    * <li>Applicability codes must be alphanumeric, containing only 0-9, A-Z, or a-z.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10414_WrongFormatApplCd() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-SN'" );
      lMapInventory.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventory.put( "INV_CLASS_CD", "'ACFT'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "ASSMBL_CD", "'ACFT_CD1'" );
      lMapInventory.put( "APPL_EFF_CD", "'*#/?#@9'" ); // wrong format
      lMapInventory.put( "AC_REG_CD", "'ACREG1'" );
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10414_WrongFormatApplCd", "AML-10414" );
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
    * <li>Error Code: TBD-10007</li>
    * <li>Only ASSY inventory should be in the attach table.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_TBD_10007_NonASSYInAttachTable_Attach() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'SN-10013P'" );
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
      Map<String, String> lMapInventoryAssy = new LinkedHashMap<String, String>();

      lMapInventoryAssy.put( "SERIAL_NO_OEM", "'SN-10013A'" );
      lMapInventoryAssy.put( "PART_NO_OEM", "'A0000001'" );
      lMapInventoryAssy.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventoryAssy.put( "MANUFACT_CD", "'10001'" );
      lMapInventoryAssy.put( "LOC_CD", "'OPS'" );
      // lMapInventoryAssy.put( "INV_COND_CD", "'INSRV'" );
      lMapInventoryAssy.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventoryAssy.put( "ASSMBL_CD", "'ACFT_CD1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventoryAssy ) );

      // create task map
      Map<String, String> lMapInventorySub = new LinkedHashMap<String, String>();

      lMapInventorySub.put( "PARENT_SERIAL_NO_OEM", "'SN-10013P'" );
      lMapInventorySub.put( "PARENT_PART_NO_OEM", "'A0000005'" );
      lMapInventorySub.put( "PARENT_MANUFACT_CD", "'10001'" );
      lMapInventorySub.put( "ATTACH_SERIAL_NO_OEM", "'SN-10013A'" );
      lMapInventorySub.put( "ATTACH_PART_NO_OEM", "'A0000001'" );
      lMapInventorySub.put( "ATTACH_MANUFACT_CD", "'10001'" );
      lMapInventorySub.put( "BOM_PART_CD", "'ACFT-SYS-1-1-TRK-P1'" );
      lMapInventorySub.put( "EQP_POS_CD", "'1'" );
      lMapInventorySub.put( "INSTALL_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_ATTACH, lMapInventorySub ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_TBD_10007_NonASSYInAttachTable_Attach", "TBD-10007" );
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
    * <li>Error Code: AML-10402</li>
    * <li>The ASSMBL_BOM_CD must be a ROOT configuration slot</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10402_AssyNonRootAssemblyCode() throws Exception {

      // create inventory map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ENG-ASSY-123'" );
      lMapInventory.put( "PART_NO_OEM", "'ENG_ASSY_PN1'" );
      lMapInventory.put( "INV_CLASS_CD", "'ENG'" );
      lMapInventory.put( "MANUFACT_CD", "'ABC11'" );
      lMapInventory.put( "ASSMBL_BOM_CD", "'ENG-ASSY'" );
      // lMapInventory.put( "INV_COND_CD", "'RFI'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10402_AssyNonRootAssemblyCode", "10402" );
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
    * <li>Error Code: AML-10439</li>
    * <li>The Configuration Slot identified by BOM_PART_CD/EQP_POS_CD is a sub-component of another
    * TRK Configuration Slot;</li>
    * <li>however there is no record in C_RI_INVENTORY_SUB for this parent TRK Configuration
    * Slot.</li>
    * <li>This record cannot be loaded unless you provide another row in C_RI_INVENTORY_SUB with the
    * parent Configuration Slot's EQP_BOM_PART_CD/EQP_POS_CD</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10439_TrkPartMissingParentTrkPart_sub() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'SN-10013P'" );
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
      Map<String, String> lMapInventorySub = new LinkedHashMap<String, String>();

      lMapInventorySub.put( "PARENT_SERIAL_NO_OEM", "'SN-10013P'" );
      lMapInventorySub.put( "PARENT_PART_NO_OEM", "'A0000005'" ); // field not provided
      lMapInventorySub.put( "PARENT_MANUFACT_CD", "'10001'" );
      lMapInventorySub.put( "BOM_PART_CD", "'ACFT-SYS-1-1-TRK-TRK-TRK-CHILD'" );
      lMapInventorySub.put( "EQP_POS_CD", "'1.1.1.1'" );
      lMapInventorySub.put( "SERIAL_NO_OEM", "'SN-10023C'" );
      lMapInventorySub.put( "PART_NO_OEM", "'A0000022'" );
      lMapInventorySub.put( "MANUFACT_CD", "'10001'" );
      lMapInventorySub.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventorySub.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventorySub ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10439_TrkPartMissingParentTrkPart_sub", "AML-10439" );
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
    * <li>Error Code: TBD-00001</li>
    * <li>The provided PART_NO_OEM/MANUFACT_CD can be installed in multiple Configuration
    * Slots,</li>
    * <li>but no ASSMBL_CD or ASSMBL_BOM_CD was provided.</li>
    * <li>Therefore, one of the Configuration Slots was automatically selected for you.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_TBD_00001_PartOnMultipleConfigSlot() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'SN-10013P'" );
      lMapInventory.put( "PART_NO_OEM", "'A0000015'" );
      lMapInventory.put( "MANUFACT_CD", "'ABC11'" );
      lMapInventory.put( "INV_CLASS_CD", "'TRK'" );
      // lMapInventory.put( "INV_COND_CD", "'RFI'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_TBD_00001_PartOnMultipleConfigSlot", "TBD-00001" );
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
    * <li>Error Code: AML-10510</li>
    * <li>The inventory identified by ATTACH_SERIAL_NO_OEM, ATTACH_PART_NO_OEM, ATTACH_MANUFACT_CD
    * does not exist in C_RI_INVENTORY.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10510_MissingAttachInventoryInInventoryTable_Attach() throws Exception {

      // prepare aircraft inventory map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-SN'" );
      lMapInventory.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventory.put( "INV_CLASS_CD", "'ACFT'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      // lMapInventory.put( "INV_COND_CD", "'INSRV'" );
      lMapInventory.put( "AC_REG_CD", "'ACREG1'" );
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert aircraft inventory usage
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // prepare aircraft inventory usage map
      Map<String, String> lMapParentInventoryUsage = new LinkedHashMap<String, String>();

      lMapParentInventoryUsage.put( "SERIAL_NO_OEM", "'ACFT-SN'" );
      lMapParentInventoryUsage.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapParentInventoryUsage.put( "MANUFACT_CD", "'10001'" );
      lMapParentInventoryUsage.put( "DATA_TYPE_CD", "'HOURS'" );
      lMapParentInventoryUsage.put( "TSN_QT", "0" );
      lMapParentInventoryUsage.put( "TSI_QT", "0" );
      lMapParentInventoryUsage.put( "TSO_QT", "0" );

      // insert aircraft inventory usage
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lMapParentInventoryUsage ) );

      // insert row for each usage parm
      lMapParentInventoryUsage.put( "DATA_TYPE_CD", "'CYCLES'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lMapParentInventoryUsage ) );
      lMapParentInventoryUsage.put( "DATA_TYPE_CD", "'USAGE1'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lMapParentInventoryUsage ) );
      lMapParentInventoryUsage.put( "DATA_TYPE_CD", "'USAGE2'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lMapParentInventoryUsage ) );
      lMapParentInventoryUsage.put( "DATA_TYPE_CD", "'USAGE3'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lMapParentInventoryUsage ) );

      // prepare assembly inventory map
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

      // insert assembly inventory map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventoryAssy ) );

      // prepare assembly inventory usage map
      Map<String, String> lMapAssyInventoryUsage = new LinkedHashMap<String, String>();

      lMapAssyInventoryUsage.put( "SERIAL_NO_OEM", "'SN-10013A'" );
      lMapAssyInventoryUsage.put( "PART_NO_OEM", "'ENG_ASSY_PN1'" );
      lMapAssyInventoryUsage.put( "MANUFACT_CD", "'ABC11'" );
      lMapAssyInventoryUsage.put( "DATA_TYPE_CD", "'EOT'" );
      lMapAssyInventoryUsage.put( "TSN_QT", "0" );
      lMapAssyInventoryUsage.put( "TSI_QT", "0" );
      lMapAssyInventoryUsage.put( "TSO_QT", "0" );

      // insert assembly inventory usage
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lMapAssyInventoryUsage ) );

      // insert row for each usage parm
      lMapAssyInventoryUsage.put( "DATA_TYPE_CD", "'ECYC'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lMapAssyInventoryUsage ) );
      lMapAssyInventoryUsage.put( "DATA_TYPE_CD", "'CYCLES'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lMapAssyInventoryUsage ) );
      lMapAssyInventoryUsage.put( "DATA_TYPE_CD", "'USAGE1'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lMapAssyInventoryUsage ) );
      lMapAssyInventoryUsage.put( "DATA_TYPE_CD", "'USAGE2'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lMapAssyInventoryUsage ) );
      lMapAssyInventoryUsage.put( "DATA_TYPE_CD", "'USAGE3'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lMapAssyInventoryUsage ) );
      lMapAssyInventoryUsage.put( "DATA_TYPE_CD", "'HOURS'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lMapAssyInventoryUsage ) );

      // create attached record map
      Map<String, String> lMapInventorySub = new LinkedHashMap<String, String>();

      lMapInventorySub.put( "PARENT_SERIAL_NO_OEM", "'ACFT-SN'" );
      lMapInventorySub.put( "PARENT_PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventorySub.put( "PARENT_MANUFACT_CD", "'10001'" );
      lMapInventorySub.put( "ATTACH_SERIAL_NO_OEM", "'SN-10013A-WRONG'" ); // incorrect serial
                                                                           // number
      lMapInventorySub.put( "ATTACH_PART_NO_OEM", "'ENG_ASSY_PN1'" );
      lMapInventorySub.put( "ATTACH_MANUFACT_CD", "'ABC11'" );
      lMapInventorySub.put( "BOM_PART_CD", "'ENG-ASSY'" );
      lMapInventorySub.put( "EQP_POS_CD", "'1'" );
      lMapInventorySub.put( "INSTALL_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert attachment record
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_ATTACH, lMapInventorySub ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10510_MissingAttachInventoryInInventoryTable_Attach",
            "AML-10510" );
   }


   @Test
   public void test_AML_10505_Mismatch_ASSMBLCD() throws Exception {

      String lRandom = String.valueOf( getRandom() );

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-" + lRandom + "'" );
      lMapInventory.put( "PART_NO_OEM", "'ACFT_ASSY_PN2'" );
      lMapInventory.put( "INV_CLASS_CD", "'ACFT'" );
      lMapInventory.put( "MANUFACT_CD", "'11111'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      // lMapInventory.put( "INV_COND_CD", "'INSRV'" );
      lMapInventory.put( "AC_REG_CD", "'" + lRandom + "'" );
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      // lMapInventory.put( "FORECAST_MODEL", "'Forecast1'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "ASSMBL_CD", "'NO_PARTS'" );
      lMapInventory.put( "ASSMBL_BOM_CD", "'NO_PARTS'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // run validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // check error code
      checkInventory( testName.getMethodName(), "AML-10505" );
   }


   /**
    * This test is to verify valid error code AML-10515:he BIN_QT field is only used for BATCH
    * inventory and will be ignored for this record.
    *
    *
    */
   @Test
   public void test_AML_10515_TRK_BINQT() throws Exception {

      String lRandom = String.valueOf( getRandom() );

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'TRK-" + lRandom + "'" );
      lMapInventory.put( "PART_NO_OEM", "'A0000010'" );
      lMapInventory.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventory.put( "MANUFACT_CD", "'11111'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      // lMapInventory.put( "INV_COND_CD", "'INSRV'" );
      lMapInventory.put( "AC_REG_CD", "'" + lRandom + "'" );
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "BIN_QT", "'5'" );
      // lMapInventory.put( "FORECAST_MODEL", "'Forecast1'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      // lMapInventory.put( "ASSMBL_CD", "'ACFT_CD1'" );
      // lMapInventory.put( "ASSMBL_BOM_CD", "'ACFT_CD1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // run validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // check error code
      checkInventory( testName.getMethodName(), "AML-10515" );
   }


   /**
    * This test is to verify valid error code AML-10515:he BIN_QT field is only used for BATCH
    * inventory and will be ignored for this record.
    *
    *
    */
   @Test
   public void test_AML_10515_KIT_BINQT() throws Exception {

      String lRandom = String.valueOf( getRandom() );

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'KIT-" + lRandom + "'" );
      lMapInventory.put( "PART_NO_OEM", "'ATKIT'" );
      lMapInventory.put( "INV_CLASS_CD", "'KIT'" );
      lMapInventory.put( "MANUFACT_CD", "'ABC11'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      // lMapInventory.put( "INV_COND_CD", "'INSRV'" );
      lMapInventory.put( "AC_REG_CD", "'" + lRandom + "'" );
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "BIN_QT", "'5'" );
      // lMapInventory.put( "FORECAST_MODEL", "'Forecast1'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "ASSMBL_CD", "'ACFT_CD1'" );
      lMapInventory.put( "ASSMBL_BOM_CD", "'ACFT_CD1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // run validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // check error code
      checkInventory( testName.getMethodName(), "AML-10515" );
   }


   @Test
   public void test_AML_10515_CHW_BINQT() throws Exception {

      String lRandom = String.valueOf( getRandom() );

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-" + lRandom + "'" );
      lMapInventory.put( "PART_NO_OEM", "'ACFT_ASSY_PN2'" );
      lMapInventory.put( "INV_CLASS_CD", "'ACFT'" );
      lMapInventory.put( "MANUFACT_CD", "'11111'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      // lMapInventory.put( "INV_COND_CD", "'INSRV'" );
      lMapInventory.put( "AC_REG_CD", "'" + lRandom + "'" );
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "BIN_QT", "'5'" );
      // lMapInventory.put( "FORECAST_MODEL", "'Forecast1'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "ASSMBL_CD", "'ACFT_CD1'" );
      lMapInventory.put( "ASSMBL_BOM_CD", "'ACFT_CD1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // run validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // check error code
      checkInventory( testName.getMethodName(), "AML-10515" );
   }


   /**
    * This test is to verify valid error code CLD_00003:Cannot load record because the related
    * record in C_RI_INVENTORY_SUB is invalid
    *
    *
    */

   @Test
   public void test_CLD_00003() throws Exception {

      // create task map
      Map<String, String> lInventoryParent = new LinkedHashMap<String, String>();
      lInventoryParent.put( "SERIAL_NO_OEM", "'TT-PARENT'" );
      lInventoryParent.put( "PART_NO_OEM", "'A0000005'" );
      lInventoryParent.put( "INV_CLASS_CD", "'TRK'" );
      lInventoryParent.put( "MANUFACT_CD", "'10001'" );
      lInventoryParent.put( "LOC_CD", "'OPS'" );
      // lInventoryParent.put( "INV_COND_CD", "'RFI'" );
      lInventoryParent.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lInventoryParent.put( "ASSMBL_CD", "'ACFT_CD1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lInventoryParent ) );

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
      lInventoryChild.put( "VENDOR_CD", "'XXMXIXX'" ); // value unknown to Mx
      lInventoryChild.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lInventoryChild ) );

      // run validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // check error code
      checkInventoryErrorCode( testName.getMethodName(), "CLD-00003" );
   }


   /**
    * This test is to verify valid error code DT_00001:The provided MANUFACT_DT cannot be a future
    * date
    *
    *
    */
   @Test
   public void test_DT_00001() throws Exception {

      // create task map
      Map<String, String> lInventoryParent = new LinkedHashMap<String, String>();
      lInventoryParent.put( "SERIAL_NO_OEM", "'TT-PARENT'" );
      lInventoryParent.put( "PART_NO_OEM", "'A0000005'" );
      lInventoryParent.put( "INV_CLASS_CD", "'TRK'" );
      lInventoryParent.put( "MANUFACT_CD", "'10001'" );
      lInventoryParent.put( "LOC_CD", "'OPS'" );
      // lInventoryParent.put( "INV_COND_CD", "'RFI'" );
      lInventoryParent.put( "MANUFACT_DT", "SYSDATE+1" );
      lInventoryParent.put( "ASSMBL_CD", "'ACFT_CD1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lInventoryParent ) );

      // run validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // check error code
      checkInventoryErrorCode( testName.getMethodName(), "DT-00001" );
   }


   /**
    * This test is to verify valid error code DT_00002:The provided RECEIVED_DT cannot be a future
    * date
    *
    *
    */
   @Test
   public void test_DT_00002() throws Exception {

      // create task map
      Map<String, String> lInventoryParent = new LinkedHashMap<String, String>();
      lInventoryParent.put( "SERIAL_NO_OEM", "'TT-PARENT'" );
      lInventoryParent.put( "PART_NO_OEM", "'A0000005'" );
      lInventoryParent.put( "INV_CLASS_CD", "'TRK'" );
      lInventoryParent.put( "MANUFACT_CD", "'10001'" );
      lInventoryParent.put( "LOC_CD", "'OPS'" );
      // lInventoryParent.put( "INV_COND_CD", "'RFI'" );
      lInventoryParent.put( "RECEIVED_DT", "SYSDATE+1" );
      lInventoryParent.put( "ASSMBL_CD", "'ACFT_CD1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lInventoryParent ) );

      // run validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // check error code
      checkInventoryErrorCode( testName.getMethodName(), "DT-00002" );
   }


   /**
    * This test is to verify valid error code DT_00003:The provided SHELF_EXPIRY_DT must be a future
    * date
    *
    *
    */
   @Test
   public void test_DT_00003() throws Exception {

      // create task map
      Map<String, String> lInventoryParent = new LinkedHashMap<String, String>();
      lInventoryParent.put( "SERIAL_NO_OEM", "'TT-PARENT'" );
      lInventoryParent.put( "PART_NO_OEM", "'A0000005'" );
      lInventoryParent.put( "INV_CLASS_CD", "'TRK'" );
      lInventoryParent.put( "MANUFACT_CD", "'10001'" );
      lInventoryParent.put( "LOC_CD", "'OPS'" );
      // lInventoryParent.put( "INV_COND_CD", "'RFI'" );
      lInventoryParent.put( "SHELF_EXPIRY_DT", "SYSDATE-1" );
      lInventoryParent.put( "ASSMBL_CD", "'ACFT_CD1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lInventoryParent ) );

      // run validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // check error code
      checkInventoryErrorCode( testName.getMethodName(), "DT-00003" );
   }


   /**
    * This test is to verify valid error code TBD_00001:The provided PART_NO_OEM/MANUFACT_CD can be
    * installed in multiple Configuration Slots, but no ASSMBL_CD or ASSMBL_BOM_CD was provided.
    * Therefore, one of the Configuration Slots was automatically selected for you.
    *
    *
    */
   @Test
   public void test_TBD_00001() throws Exception {

      // create task map
      Map<String, String> lInventoryParent = new LinkedHashMap<String, String>();
      lInventoryParent.put( "SERIAL_NO_OEM", "'TT-PARENT'" );
      lInventoryParent.put( "PART_NO_OEM", "'A0000015'" );
      lInventoryParent.put( "INV_CLASS_CD", "'TRK'" );
      lInventoryParent.put( "MANUFACT_CD", "'ABC11'" );
      lInventoryParent.put( "LOC_CD", "'OPS'" );
      // lInventoryParent.put( "INV_COND_CD", "'RFI'" );
      lInventoryParent.put( "SHELF_EXPIRY_DT", "SYSDATE+1" );
      // lInventoryParent.put( "ASSMBL_CD", "'ACFT_CD1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lInventoryParent ) );

      // run validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // check error code
      checkInventoryErrorCode( testName.getMethodName(), "TBD-00001" );
   }


   /**
    * This test is to verify valid error code TBD_10202:Mandatory field missing: MANUFACT_CD (only
    * mandatory when the provided PART_NO_OEM exists multiple times in Maintenix)
    *
    *
    */
   @Test
   public void test_TBD_10202() throws Exception {

      // create task map
      Map<String, String> lInventoryParent = new LinkedHashMap<String, String>();
      lInventoryParent.put( "SERIAL_NO_OEM", "'TT-PARENT'" );
      lInventoryParent.put( "PART_NO_OEM", "'" + iPN_ACFT + "'" );
      lInventoryParent.put( "INV_CLASS_CD", "'ACFT'" );
      // lInventoryParent.put( "MANUFACT_CD", "'" + iMANUAL_ACFT + "'" );
      lInventoryParent.put( "LOC_CD", "'OPS'" );
      // lInventoryParent.put( "INV_COND_CD", "'RFI'" );
      lInventoryParent.put( "SHELF_EXPIRY_DT", "SYSDATE+1" );
      lInventoryParent.put( "ASSMBL_CD", "'ACFT_CD1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lInventoryParent ) );

      // run validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // check error code
      checkInventoryErrorCode( testName.getMethodName(), "TBD-10202" );
   }


   /**
    * This test is to verify valid error code TBD_10205:Mandatory field missing: MANUFACT_CD (only
    * mandatory when the provided PART_NO_OEM exists multiple times in Maintenix).
    *
    *
    */

   @Test
   public void test_TBD_10205() throws Exception {

      // create task map
      Map<String, String> lInventoryParent = new LinkedHashMap<String, String>();
      lInventoryParent.put( "SERIAL_NO_OEM", "'TT-PARENT'" );
      lInventoryParent.put( "PART_NO_OEM", "'" + iPN_ACFT + "'" );
      lInventoryParent.put( "INV_CLASS_CD", "'ACFT'" );
      lInventoryParent.put( "MANUFACT_CD", "'" + iMANUAL_ACFT + "'" );
      lInventoryParent.put( "LOC_CD", "'OPS'" );
      // lInventoryParent.put( "INV_COND_CD", "'RFI'" );
      lInventoryParent.put( "SHELF_EXPIRY_DT", "SYSDATE+1" );
      lInventoryParent.put( "ASSMBL_CD", "'ACFT_CD1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lInventoryParent ) );

      // create task map
      Map<String, String> lInventoryChild = new LinkedHashMap<String, String>();
      lInventoryChild.put( "PARENT_SERIAL_NO_OEM", "'TT-PARENT'" );
      lInventoryChild.put( "PARENT_PART_NO_OEM", "'" + iPN_ACFT + "'" );
      lInventoryChild.put( "PARENT_MANUFACT_CD", "'" + iMANUAL_ACFT + "'" );
      lInventoryChild.put( "BOM_PART_CD", "'ACFT-SYS-1-1-TRK-TRK-CHILD'" );
      lInventoryChild.put( "EQP_POS_CD", "'1'" );
      lInventoryChild.put( "SERIAL_NO_OEM", "'TT-CHILD'" );
      lInventoryChild.put( "PART_NO_OEM", "'A0000006'" );
      // lInventoryChild.put( "MANUFACT_CD", "'11111'" );
      lInventoryChild.put( "INV_CLASS_CD", "'TRK'" );
      // lInventoryChild.put( "VENDOR_CD", "'XXMXIXX'" ); // value unknown to Mx
      lInventoryChild.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lInventoryChild ) );

      // // call inventory validation and check result against expectation
      // validateAndCheckInventory( "test_AML_10241_UnknownVendorCd_Sub", "AML-10241" );

      // run validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // check error code
      checkInventoryErrorCode( testName.getMethodName(), "TBD-10205" );
   }


   /**
    * This test is to verify valid error code AML_10210: INVENTORY field LOC_CD exists multiple
    * times in Maintenix INV_LOC table..
    *
    *
    */
   @Test
   public void test_AML_10210() throws Exception {

      int lRandom = getRandom();

      String lSerialNo = "AIRCRAFT" + lRandom;

      // create task map
      Map<String, String> lMapInventoryAcft = new LinkedHashMap<String, String>();
      lMapInventoryAcft.put( "SERIAL_NO_OEM", "'ACFT-" + lSerialNo + "'" );
      lMapInventoryAcft.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventoryAcft.put( "INV_CLASS_CD", "'ACFT'" );
      lMapInventoryAcft.put( "MANUFACT_CD", "'10001'" );;
      lMapInventoryAcft.put( "LOC_CD", "'OPS'" );
      // lMapInventoryAcft.put( "INV_COND_CD", "'RFI'" );
      lMapInventoryAcft.put( "ASSMBL_CD", "'ACFT_CD1'" );
      lMapInventoryAcft.put( "AC_REG_CD", "'" + lRandom + "'" );
      lMapInventoryAcft.put( "INV_OPER_CD", "'NORM'" );
      lMapInventoryAcft.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventoryAcft.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert Aircraft map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventoryAcft ) );
      // run validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // check error code
      checkInventoryErrorCode( testName.getMethodName(), "AML-10210" );
   }


   /**
    * This test is to verify valid error code AML-10246: SUB_INVENTORY field OWNER_CD is not unique
    * in Maintenix INV_OWNER table.
    *
    *
    */
   @Test
   public void test_AML_10246() throws Exception {

      // create inventory map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-SN'" );
      lMapInventory.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventory.put( "INV_CLASS_CD", "'ACFT'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      // lMapInventory.put( "INV_COND_CD", "'INSRV'" );
      lMapInventory.put( "AC_REG_CD", "'ACREG1'" );
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

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
      lMapInventorySub.put( "OWNER_CD", "'AUTO'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventorySub ) );

      // run validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // check error code
      checkInventoryErrorCode( testName.getMethodName(), "AML-10246" );
   }


   /**
    * This test is to verify valid error code AML-10306: The INVENTORY item referenced by ATTACH
    * fields PARENT_SERIAL_NO_OEM/PARENT_PART_NO_OEM/PARENT_MANUFACT_CD did not pass validation in
    * the INVENTORY table. The Parent inventory item must be valid in order for a Sub-Assembly to be
    * attached.
    *
    *
    */
   @Test
   public void test_AML_10306() throws Exception {

      String lRandom = String.valueOf( getRandom() );

      // create inventory map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();
      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-" + lRandom + "'" );
      // lMapInventory.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventory.put( "PART_NO_OEM", "'ACFT_ASSY_PN2'" );
      lMapInventory.put( "INV_CLASS_CD", "'ACFT'" );
      // lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "MANUFACT_CD", "'123456789'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "ASSMBL_CD", "'ACFT_CD1'" );
      // lMapInventory.put( "INV_COND_CD", "'INSRV'" );
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
      // lMapInventoryAttach.put( "PARENT_PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventoryAttach.put( "PARENT_PART_NO_OEM", "'ACFT_ASSY_PN2'" );
      // lMapInventoryAttach.put( "PARENT_MANUFACT_CD", "'10001'" );
      lMapInventoryAttach.put( "PARENT_MANUFACT_CD", "'123456789'" );
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
      checkInventoryErrorCode( testName.getMethodName(), "AML-10306" );
   }


   /**
    * This test is to verify valid error code AML-10308: Cannot load record because the related
    * record in C_RI_INVENTORY is invalid (related using
    * ATTACH_SERIAL_NO_OEM/ATTACH_PART_NO_OEM/ATTACH_MANUFACT_CD)
    *
    *
    */

   @Test
   public void test_AML_10308() throws Exception {

      // create map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-SN'" );
      lMapInventory.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventory.put( "INV_CLASS_CD", "'ACFT'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      // lMapInventory.put( "INV_COND_CD", "'INSRV'" );
      lMapInventory.put( "AC_REG_CD", "'ACREG1'" );
      // lMapInventory.put( "APPL_EFF_CD", "'099'" ); // outside of part group range
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
      lMapInventoryAssy.put( "APPL_EFF_CD", "'099'" );

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
      checkInventoryErrorCode( testName.getMethodName(), "AML-10308" );
   }


   /**
    * This test is to verify valid error code AML-10234: SUB_INVENTORY field BOM_PART_CD is not a
    * unique Configuration Slot on the SUB_INVENTORY item Parent Assembly..
    *
    *
    */
   @Test
   public void test_AML_10234() throws Exception {

      // create task map
      Map<String, String> lInventoryParent = new LinkedHashMap<String, String>();
      lInventoryParent.put( "SERIAL_NO_OEM", "'TTT-PARENT'" );
      lInventoryParent.put( "PART_NO_OEM", "'A0000020'" );
      lInventoryParent.put( "INV_CLASS_CD", "'TRK'" );
      lInventoryParent.put( "MANUFACT_CD", "'10001'" );
      lInventoryParent.put( "LOC_CD", "'OPS'" );
      // lInventoryParent.put( "INV_COND_CD", "'RFI'" );
      lInventoryParent.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lInventoryParent.put( "ASSMBL_CD", "'ACFT_CD1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lInventoryParent ) );

      // create task map
      Map<String, String> lInventoryChild = new LinkedHashMap<String, String>();
      lInventoryChild.put( "PARENT_SERIAL_NO_OEM", "'TTT-PARENT'" );
      lInventoryChild.put( "PARENT_PART_NO_OEM", "'A0000020'" );
      lInventoryChild.put( "PARENT_MANUFACT_CD", "'10001'" );
      lInventoryChild.put( "BOM_PART_CD", "'ACFT-SYS-10-2-DOUBLETRK'" );
      lInventoryChild.put( "EQP_POS_CD", "'1'" );
      lInventoryChild.put( "SERIAL_NO_OEM", "'TTT-DOUBLETRK'" );
      lInventoryChild.put( "PART_NO_OEM", "'T0000001REC'" );
      lInventoryChild.put( "MANUFACT_CD", "'10001'" );
      lInventoryChild.put( "INV_CLASS_CD", "'TRK'" );
      lInventoryChild.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lInventoryChild ) );

      // run validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // check error code
      checkInventoryErrorCode( testName.getMethodName(), "AML-10234" );

   }


   /**
    * This test is to verify valid error code AML-10250: ATTACH field BOM_PART_CD is not unique in
    * Maintenix Baseline
    *
    *
    */

   @Test
   public void test_AML_10250() throws Exception {

      String lRandom = String.valueOf( getRandom() );

      // create inventory map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();
      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-" + lRandom + "'" );
      lMapInventory.put( "PART_NO_OEM", "'ACFT_TEST_PN1'" );
      lMapInventory.put( "INV_CLASS_CD", "'ACFT'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      // lMapInventory.put( "INV_COND_CD", "'INSRV'" );
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
      lMapInventoryAttach.put( "PARENT_PART_NO_OEM", "'ACFT_TEST_PN1'" );
      lMapInventoryAttach.put( "PARENT_MANUFACT_CD", "'10001'" );
      lMapInventoryAttach.put( "ATTACH_SERIAL_NO_OEM", "'ASSY-" + lRandom + "'" );
      lMapInventoryAttach.put( "ATTACH_PART_NO_OEM", "'ENG_ASSY_PN1'" );
      lMapInventoryAttach.put( "ATTACH_MANUFACT_CD", "'ABC11'" );
      lMapInventoryAttach.put( "BOM_PART_CD", "'ENG-TASSY'" );
      lMapInventoryAttach.put( "INSTALL_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_ATTACH, lMapInventoryAttach ) );

      // run validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // check error code
      checkInventoryErrorCode( testName.getMethodName(), "AML-10250" );
   }


   /**
    * This test is to verify valid error code AML-10513: SHELF_EXPIRY_DT must be NULL for KIT
    * inventory. It will be ignored for this record.
    *
    *
    */
   @Test
   public void test_AML_10513() throws Exception {

      // create inventory map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();
      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-KIT1'" );
      lMapInventory.put( "PART_NO_OEM", "'ATKIT'" );
      lMapInventory.put( "INV_CLASS_CD", "'KIT'" );
      lMapInventory.put( "MANUFACT_CD", "'ABC11'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      // lMapInventory.put( "INV_COND_CD", "'RFI'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "SHELF_EXPIRY_DT", "to_date('01/01/2018','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // run validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // check error code
      checkInventoryErrorCode( testName.getMethodName(), "AML-10513" );
   }


   /**
    * This test is to verify valid error code AML-10513: CARRIER_CD must be NULL when inventory
    * provided is KIT. It will be ignored.
    *
    *
    */
   @Test
   public void test_AML_10514() throws Exception {

      // create inventory map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();
      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-KIT1'" );
      lMapInventory.put( "PART_NO_OEM", "'ATKIT'" );
      lMapInventory.put( "INV_CLASS_CD", "'KIT'" );
      lMapInventory.put( "MANUFACT_CD", "'ABC11'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      // lMapInventory.put( "INV_COND_CD", "'RFI'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "CARRIER_CD", "'test'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // run validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // check error code
      checkInventoryErrorCode( testName.getMethodName(), "AML-10514" );
   }


   // ====================================================================================================
   /**
    * This function is to create a record in inv_loc to with inv_type_cd=ops (lower case)
    *
    *
    */

   public void dataSetupINV_LOC() {
      String lQuery =
            "INSERT INTO INV_LOC (LOC_DB_ID, LOC_ID, LOC_TYPE_DB_ID, LOC_TYPE_CD,LOC_CD, LOC_NAME, CITY_NAME) "
                  + "VALUES (4650, (select MAX(LOC_ID)+1 from INV_LOC), 0, 'OPS', 'ops','ops', 'AUTO')";
      executeSQL( lQuery );
   }


   /**
    * This function is to create a record in eqp_part_no to have same part_no_oem but different
    * manufacture_cd for acft
    *
    *
    */
   public void dataSetupDupPNOEM() {

      String lQuery =
            "INSERT INTO EQP_PART_NO (PART_NO_DB_ID, PART_NO_ID, INV_CLASS_DB_ID, INV_CLASS_CD, "
                  + " PART_STATUS_DB_ID, PART_STATUS_CD,MANUFACT_DB_ID, MANUFACT_CD, QTY_UNIT_DB_ID,QTY_UNIT_CD,PART_NO_SDESC, PART_NO_OEM, NOTE) "
                  + " VALUES (4650, part_no_id_seq.nextval, '0', 'ACFT', '0', 'ACTV',4650, '11111','0','EA','Aircraft Part 1', 'ACFT_ASSY_PN1', 'AUTO')";
      executeSQL( lQuery );
   }


   /**
    * This function is to create a record in eqp_part_no to have same part_no_oem but different
    * manufacture_cd for TRK
    *
    *
    */
   public void dataSetupDupPNOEM_TRK() {

      String lQuery =
            "INSERT INTO EQP_PART_NO (PART_NO_DB_ID, PART_NO_ID, INV_CLASS_DB_ID, INV_CLASS_CD, "
                  + " PART_STATUS_DB_ID, PART_STATUS_CD,MANUFACT_DB_ID, MANUFACT_CD, QTY_UNIT_DB_ID,QTY_UNIT_CD,PART_NO_SDESC, PART_NO_OEM, NOTE) "
                  + " VALUES (4650, part_no_id_seq.nextval, '0', 'TRK', '0', 'ACTV',4650, '10001','0','EA','TRK-on-TRK Child', 'A0000006', 'AUTO')";
      executeSQL( lQuery );
   }


   /**
    * This function is to setup duplcate owner_cd
    *
    */

   public void dataSetupOWNER_CD() {
      String lQuery = "insert into INV_OWNER (OWNER_DB_ID, OWNER_ID, OWNER_CD, OWNER_NAME) "
            + " values (4650, owner_id_seq.nextval, 'AUTO', 'AUTO')";
      executeSQL( lQuery );
      executeSQL( lQuery );

   }


   /**
    * This function is create duplicate bom_part_cd on same config slot.
    *
    *
    */
   public void dataSetupBOM_PART_CD( String aBomPartCd, String aNewBomPartCd ) {
      iBOM_id = getBOMID( aBomPartCd );

      String lQuery =
            "insert into eqp_bom_part (BOM_PART_DB_ID, BOM_PART_ID, ASSMBL_DB_ID, ASSMBL_CD,ASSMBL_BOM_ID, INV_CLASS_DB_ID, INV_CLASS_CD, BOM_PART_CD, BOM_PART_NAME) "
                  + " values (4650, bom_part_id_seq.nextval, 4650,'ACFT_CD1', " + iBOM_id
                  + ", 0, 'TRK', '" + aNewBomPartCd + "','" + aBomPartCd + "')";
      executeSQL( lQuery );

   }


   /**
    * This function is called by after class
    *
    *
    */

   public void restoreBOM_PART_CD( String aNewBomPartCd ) {
      String lQuery = "delete from eqp_bom_part where bom_part_cd='" + aNewBomPartCd + "'";
      executeSQL( lQuery );

   }


   /**
    * This function is called by after class
    *
    *
    */

   public void restoreDupPNOEM() {
      String lQuery = "delete from EQP_PART_NO where NOTE='AUTO'";
      executeSQL( lQuery );

      if ( iPNIDs != null ) {
         lQuery = "delete from INV_INV where PART_NO_DB_ID=" + iPNIDs.getNO_DB_ID()
               + " and PART_NO_ID=" + iPNIDs.getNO_ID();
         executeSQL( lQuery );
      }

   }


   /**
    * This function is called by after class
    *
    *
    */

   public void restoreOWNER_CD() {
      String lQuery = "delete from INV_OWNER where OWNER_CD='AUTO'";
      executeSQL( lQuery );

   }


   /**
    * This function is to get assmbl_bom_id given bom_part_cd.
    *
    *
    */
   public String getBOMID( String aBomPartCd ) {

      String[] iIds = { "ASSMBL_BOM_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "BOM_PART_CD", aBomPartCd );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EQP_BOM_PART, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      return llists.get( 0 ).get( 0 );

   }


   /**
    * This function is called by after class
    *
    *
    */
   public void restoreINV_LOC() {

      String lQuery = "delete from INV_LOC where CITY_NAME='AUTO'";
      executeSQL( lQuery );

   }


   /**
    * This function is to update appl_eff_ldesc in eqp_bom_part table for appl_eff test cases.
    *
    *
    */
   public void dataSetupBOMPARTAPPL_2() {
      // String lQuery =
      // "UPDATE eqp_bom_part SET appl_eff_ldesc='001-005' where assmbl_cd='ACFT_CD1' and
      // INV_CLASS_CD='ASSY' and BOM_PART_CD='ENG-ASSY'";
      // executeSQL( lQuery );

      String lQuery2 =
            "UPDATE eqp_bom_part SET appl_eff_ldesc='001-005' where assmbl_cd='ENG_CD1' and assmbl_bom_id=0";
      executeSQL( lQuery2 );

   }


   /**
    * This function is to restore dataon appl_eff_ldesc in eqp_bom_part table for appl_eff test
    * cases.
    *
    *
    */
   public void restoreBOMPARTAPPL_2() {
      // String lQuery =
      // "UPDATE eqp_bom_part SET appl_eff_ldesc=null where assmbl_cd='ACFT_CD1' and
      // INV_CLASS_CD='ASSY' and BOM_PART_CD='ENG-ASSY'";
      // executeSQL( lQuery );

      String lQuery2 =
            "UPDATE eqp_bom_part SET appl_eff_ldesc=null where assmbl_cd='ENG_CD1' and assmbl_bom_id=0";
      executeSQL( lQuery2 );

   }

}
