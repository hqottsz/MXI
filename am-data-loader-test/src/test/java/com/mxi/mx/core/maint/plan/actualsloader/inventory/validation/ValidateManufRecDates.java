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
 * This test suite contains unit tests for Actuals Loader task validations for Part Numbers and
 * Serial Numbers
 *
 * @author Andrew Bruce
 */

public class ValidateManufRecDates extends ActualsLoaderTest {

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
    * <li>Error Code: AML-10035</li>
    * <li>ATTACH field INSTALL_DT is mandatory and must be specified.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10035_MissingAttachInstallDt_Attach() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'SN-10013P'" );
      lMapInventory.put( "PART_NO_OEM", "'A0000005'" );
      lMapInventory.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'REPREQ'" );
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
      lMapInventorySub.put( "BOM_PART_CD", "'ENG-ASSY'" );
      lMapInventorySub.put( "EQP_POS_CD", "'1'" );
      // lMapInventorySub.put( "INSTALL_DT", "to_date('01/01/2015','mm/dd/yyyy')" ); // field not
      // provided

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_ATTACH, lMapInventorySub ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10035_MissingAttachInstallDt_Attach", "AML-10035" );
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
    * <li>Error Code: AML-10046</li>
    * <li>C_RPOC_RI_INVENTORY.received_dt must be set to a date after manufact_dt when both are
    * provided.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10046_RecDateBeforeManufDate() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'TRK-10004'" );
      lMapInventory.put( "PART_NO_OEM", "'A0000005'" );
      lMapInventory.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'RFI'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/02/2015','mm/dd/yyyy')" );
      lMapInventory.put( "RECEIVED_DT", "to_date('01/01/2015','mm/dd/yyyy')" ); // date comes before
                                                                                // manuf date
      lMapInventory.put( "ASSMBL_CD", "'ACFT_CD1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10046_RecDateBeforeManufDate", "AML-10046" );
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
    * <li>Error Code: AML-10047</li>
    * <li>C_RPOC_RI_INVENTORY_SUB.received_dt must be set to a date after manufact_dt when both are
    * provided.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10047_RecDateBeforeManufDate_Sub() throws Exception {

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
      lMapInventorySub.put( "MANUFACT_DT", "to_date('01/02/2015','mm/dd/yyyy')" );
      lMapInventorySub.put( "RECEIVED_DT", "to_date('01/01/2015','mm/dd/yyyy')" ); // date comes
                                                                                   // before

      // manuf date

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventorySub ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10047_RecDateBeforeManufDate_Sub", "AML-10047" );
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
    * <li>Error Code: AML-10048</li>
    * <li>C_PROC_RI_INVENTORY.manufact_dt must be provide with non-null date value for inventory
    * when the part has at least one task definition (REQ or BLOCK) that is scheduled from manuf
    * date</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10048_NullManufDate() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'TRK-10004'" );
      lMapInventory.put( "PART_NO_OEM", "'A0000001'" );
      lMapInventory.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'RFI'" );

      // lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" ); // field not
      // provided
      lMapInventory.put( "ASSMBL_CD", "'ACFT_CD1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10048_NullManufDate", "AML-10048" );
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
    * <li>Error Code: AML-10049</li>
    * <li>C_PROC_RI_INVENTORY.received_dt must be provided with non-null date value for inventory
    * when the part has at least one task definition (REQ or BLOCK) that is scheduled from received
    * date</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */

   @Test
   public void test_AML_10049_NullReceivedDate() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'TRK-10049'" );
      lMapInventory.put( "PART_NO_OEM", "'A0000001REC'" );
      lMapInventory.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'RFI'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // lMapInventory.put( "RECEIVED_DT", "to_date('01/02/2015','mm/dd/yyyy')" ); // field not
      // provided
      lMapInventory.put( "ASSMBL_CD", "'ACFT_CD1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10049_NullReceivedDate", "AML-10049" );
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
    * <li>Error Code: AML-10050</li>
    * <li>C_PROC_RI_INVENTORY_SUB.manufact_dt must be provide with non-null date value for inventory
    * when the part has at least one task definition (REQ or BLOCK) that is scheduled from manuf
    * date</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10050_NullManufDate_Sub() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-10013P'" );
      lMapInventory.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventory.put( "INV_CLASS_CD", "'ACFT'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2014','mm/dd/yyyy')" );
      lMapInventory.put( "ASSMBL_CD", "'ACFT_CD1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // create task map
      Map<String, String> lMapInventorySub = new LinkedHashMap<String, String>();

      lMapInventorySub.put( "PARENT_SERIAL_NO_OEM", "'ACFT-10013P'" );
      lMapInventorySub.put( "PARENT_PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventorySub.put( "PARENT_MANUFACT_CD", "'10001'" );
      lMapInventorySub.put( "BOM_PART_CD", "'ACFT-SYS-1-1-TRK-P1'" );
      lMapInventorySub.put( "EQP_POS_CD", "'1'" );
      lMapInventorySub.put( "SERIAL_NO_OEM", "'SN-10013C'" );
      lMapInventorySub.put( "PART_NO_OEM", "'A0000001'" );
      lMapInventorySub.put( "MANUFACT_CD", "'10001'" );
      lMapInventorySub.put( "INV_CLASS_CD", "'TRK'" );
      // lMapInventorySub.put( "MANUFACT_DT", "to_date('01/02/2015','mm/dd/yyyy')" ); // not
      // provided

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventorySub ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10050_NullManufDate_Sub", "AML-10050" );
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
    * <li>Error Code: AML-10051</li>
    * <li>Mandatory field missing: RECEIVED_DT (only mandatory when the part has at least one REQ or
    * BLOCK that is scheduled from the received date).</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10051_NullRecDate_Sub() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-SN'" );
      lMapInventory.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventory.put( "INV_CLASS_CD", "'ACFT'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventory.put( "AC_REG_CD", "'ACREG1'" );
      lMapInventory.put( "INV_OPER_CD", "'NORM'" ); // field not provided
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // create task map
      Map<String, String> lMapInventorySub = new LinkedHashMap<String, String>();

      lMapInventorySub.put( "PARENT_SERIAL_NO_OEM", "'ACFT-SN'" );
      lMapInventorySub.put( "PARENT_PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventorySub.put( "PARENT_MANUFACT_CD", "'10001'" );
      lMapInventorySub.put( "BOM_PART_CD", "'ACFT-SYS-1-1-TRK-P1'" );
      lMapInventorySub.put( "EQP_POS_CD", "'1'" );
      lMapInventorySub.put( "SERIAL_NO_OEM", "'TRK-10051'" );
      lMapInventorySub.put( "PART_NO_OEM", "'A0000001REC'" );
      lMapInventorySub.put( "MANUFACT_CD", "'10001'" );
      lMapInventorySub.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventorySub.put( "MANUFACT_DT", "to_date('01/02/2015','mm/dd/yyyy')" );
      // lMapInventory.put( "RECEIVED_DT", "to_date('01/01/2015','mm/dd/yyyy')" ); // field not
      // provided

      // manuf date

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventorySub ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_AML_10051_NullRecDate_Sub", "AML-10051" );
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
    * <li>Error Code: DT-00001</li>
    * <li>Manufacture date is in future.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_DT_00001_FutureManufDate() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'TRK-10004'" );
      lMapInventory.put( "PART_NO_OEM", "'A0000005'" );
      lMapInventory.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'RFI'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2099','mm/dd/yyyy')" );
      lMapInventory.put( "ASSMBL_CD", "'ACFT_CD1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_DT_00001_FutureManufDate", "DT-00001" );
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
    * <li>Error Code: DT-00002</li>
    * <li>Received date is in future.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_DT_00002_FutureRecDate() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'TRK-10004'" );
      lMapInventory.put( "PART_NO_OEM", "'A0000005'" );
      lMapInventory.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'RFI'" );
      lMapInventory.put( "RECEIVED_DT", "to_date('01/01/2099','mm/dd/yyyy')" );
      lMapInventory.put( "ASSMBL_CD", "'ACFT_CD1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_DT_00002_FutureRecDate", "DT-00002" );
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
    * <li>Error Code: DT-00003</li>
    * <li>Shelf expiry date is in past.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_DT_00003_PastShelfExpDate() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'TRK-10004'" );
      lMapInventory.put( "PART_NO_OEM", "'A0000005'" );
      lMapInventory.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'RFI'" );
      lMapInventory.put( "SHELF_EXPIRY_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "ASSMBL_CD", "'ACFT_CD1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_DT_00003_PastShelfExpDate", "DT-00003" );
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
    * <li>Error Code: DT-00004</li>
    * <li>Manufacture date is in future.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_DT_00004_FutureManufDate_Sub() throws Exception {

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
      lMapInventorySub.put( "MANUFACT_DT", "to_date('01/02/2099','mm/dd/yyyy')" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventorySub ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_DT_00004_FutureManufDate_Sub", "DT-00004" );
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
    * <li>Error Code: DT-00005</li>
    * <li>Received date is in future.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_DT_00005_FutureRecDate_Sub() throws Exception {

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
      lMapInventorySub.put( "RECEIVED_DT", "to_date('01/02/2099','mm/dd/yyyy')" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventorySub ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_DT_00005_FutureRecDate_Sub", "DT-00005" );
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
    * <li>Error Code: DT-00006</li>
    * <li>Shelf expiry date is in past.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_DT_00006_PastShelfExpDate_Sub() throws Exception {

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
      lMapInventorySub.put( "SHELF_EXPIRY_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventorySub ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_DT_00006_PastShelfExpDate_Sub", "DT-00006" );
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
    * <li>Error Code: DT-00007</li>
    * <li>Install date is in future.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_DT_00007_FutureInstallDate_Sub() throws Exception {

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
      lMapInventorySub.put( "INSTALL_DT", "to_date('01/02/2099','mm/dd/yyyy')" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventorySub ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_DT_00007_FutureInstallDate_Sub", "DT-00007" );
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
    * <li>Error Code: DT-00008</li>
    * <li>Install date is before manufacture date.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_DT_00008_InstallDateBeforeManufDate_Sub() throws Exception {

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
      lMapInventorySub.put( "INSTALL_DT", "to_date('01/02/2014','mm/dd/yyyy')" );
      lMapInventorySub.put( "MANUFACT_DT", "to_date('01/02/2015','mm/dd/yyyy')" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventorySub ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_DT_00008_InstallDateBeforeManufDate_Sub", "DT-00008" );
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
    * <li>Error Code: DT-00009</li>
    * <li>Install date is before received date.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_DT_00009_InstallDateBeforeRecDate_Sub() throws Exception {

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
      lMapInventorySub.put( "INSTALL_DT", "to_date('01/02/2014','mm/dd/yyyy')" );
      lMapInventorySub.put( "RECEIVED_DT", "to_date('01/02/2015','mm/dd/yyyy')" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventorySub ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_DT_00009_InstallDateBeforeRecDate_Sub", "DT-00009" );
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
    * <li>Error Code: DT-00010</li>
    * <li>Install date is in future.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_DT_00010_FutureInstallDate_Attach() throws Exception {

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
      lMapInventoryAssy.put( "INV_COND_CD", "'REPREQ'" );
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
      lMapInventorySub.put( "BOM_PART_CD", "'ENG-ASSY'" );
      lMapInventorySub.put( "EQP_POS_CD", "'1'" );
      lMapInventorySub.put( "INSTALL_DT", "to_date('01/01/2099','mm/dd/yyyy')" ); // date in future

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_ATTACH, lMapInventorySub ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_DT_00010_FutureInstallDate_Attach", "DT-00010" );
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
    * <li>Error Code: DT-00011</li>
    * <li>Install date is before manufacture date.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_DT_00011_InstallDateBeforeManufDate_Attach() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'SN-10013P'" );
      lMapInventory.put( "PART_NO_OEM", "'A0000005'" );
      lMapInventory.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'RFI'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2013','mm/dd/yyyy')" );
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
      lMapInventoryAssy.put( "INV_COND_CD", "'REPREQ'" );
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
      lMapInventorySub.put( "BOM_PART_CD", "'ENG-ASSY'" );
      lMapInventorySub.put( "EQP_POS_CD", "'1'" );
      lMapInventorySub.put( "INSTALL_DT", "to_date('01/01/2014','mm/dd/yyyy')" ); // install date
                                                                                  // before manuf
                                                                                  // date

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_ATTACH, lMapInventorySub ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_DT_00011_InstallDateBeforeManufDate_Attach", "DT-00011" );
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
    * <li>Error Code: DT-00012</li>
    * <li>Install date is before received date.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_DT_00012_InstallDateBeforeRecDate_Attach() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'SN-10013P'" );
      lMapInventory.put( "PART_NO_OEM", "'A0000005'" );
      lMapInventory.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'RFI'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2013','mm/dd/yyyy')" );
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
      lMapInventoryAssy.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventoryAssy.put( "MANUFACT_DT", "to_date('01/01/2013','mm/dd/yyyy')" );
      lMapInventoryAssy.put( "RECEIVED_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
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
      lMapInventorySub.put( "BOM_PART_CD", "'ENG-ASSY'" );
      lMapInventorySub.put( "EQP_POS_CD", "'1'" );
      lMapInventorySub.put( "INSTALL_DT", "to_date('01/01/2014','mm/dd/yyyy')" ); // install date
                                                                                  // before received
                                                                                  // date

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_ATTACH, lMapInventorySub ) );

      // call inventory validation and check result against expectation
      validateAndCheckInventory( "test_DT_00012_InstallDateBeforeRecDate_Attach", "DT-00012" );
   }
}
