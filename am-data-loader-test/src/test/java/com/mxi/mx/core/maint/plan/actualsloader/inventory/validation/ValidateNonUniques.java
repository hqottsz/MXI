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

public class ValidateNonUniques extends ActualsLoaderTest {

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
    * <li>Error Code: AML-10202</li>
    * <li>INVENTORY MANUFACT_CD is duplicated in Maintenix database (EQP_MANUFACT.MANUFACT_CD).</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */

   // After adding constraint in OPER-15625 that enforces EQP_MANUFACT.MANUFACT_CD to be
   // trimmed and uppercase the AL validation AML-10202 must be deprecated

   @Ignore
   public void test_AML_10202_NonUniqueManufCd() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'TRK-10005'" );
      lMapInventory.put( "PART_NO_OEM", "'A0000003'" );
      lMapInventory.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventory.put( "MANUFACT_CD", "'ABC11'" ); // duplicate fields in database
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'RFI'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // create manufacturer map
      Map<String, String> lMapManufact = new LinkedHashMap<String, String>();

      lMapManufact.put( "MANUFACT_DB_ID", "0" );
      lMapManufact.put( "MANUFACT_CD", "'aBC11'" );
      lMapManufact.put( "MANUFACT_NAME", "'Manufacturer Name X'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( "EQP_MANUFACT", lMapManufact ) );

      // call inventory validation
      runALValidateInventory();

      // insert map
      deleteFromTableWhere( "EQP_MANUFACT", TableUtil.whereFromTableByMap( lMapManufact ) );

      // call inventory validation and check result against expectation
      checkInventory( "test_AML_10202_NonUniqueManufCd", "AML-10202" );
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
    * <li>Error Code: AML-10204</li>
    * <li>INVENTORY SUB MANUFACT_CD is duplicated in Maintenix database
    * (EQP_MANUFACT.MANUFACT_CD).</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */

   // After adding constraint in OPER-15625 that enforces EQP_MANUFACT.MANUFACT_CD to be
   // trimmed and uppercase the AL validation AML-10204 must be deprecated

   @Ignore
   public void test_AML_10204_NonUniqueManufCd_Sub() throws Exception {

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
      Map<String, String> lMapInventorySub = new LinkedHashMap<String, String>();

      lMapInventorySub.put( "PARENT_SERIAL_NO_OEM", "'ACFT-SN'" );
      lMapInventorySub.put( "PARENT_PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventorySub.put( "PARENT_MANUFACT_CD", "'10001'" );
      lMapInventorySub.put( "BOM_PART_CD", "'ACFT-SYS-1-1-TRK-P3'" );
      lMapInventorySub.put( "EQP_POS_CD", "'1'" );
      lMapInventorySub.put( "SERIAL_NO_OEM", "'SN-10023C'" );
      lMapInventorySub.put( "PART_NO_OEM", "'A0000003'" );
      lMapInventorySub.put( "MANUFACT_CD", "'ABC11'" ); // duplicate field in Mx
      lMapInventorySub.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventorySub.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventorySub ) );

      // create manufacturer map
      Map<String, String> lMapManufact = new LinkedHashMap<String, String>();

      lMapManufact.put( "MANUFACT_DB_ID", "0" );
      lMapManufact.put( "MANUFACT_CD", "'aBC11'" );
      lMapManufact.put( "MANUFACT_NAME", "'Manufacturer Name X'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( "EQP_MANUFACT", lMapManufact ) );

      // call inventory validation
      runALValidateInventory();

      // insert map
      deleteFromTableWhere( "EQP_MANUFACT", TableUtil.whereFromTableByMap( lMapManufact ) );

      // call inventory validation and check result against expectation
      checkInventory( "test_AML_10204_NonUniqueManufCd_Sub", "AML-10204" );
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
    * <li>Error Code: AML-10208</li>
    * <li>INVENTORY field AUTHORITY_CD exists multiple times in Maintenix ORG_AUTHORITY table.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10208_NonUniqueAuthorityCd() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'TRK-10005'" );
      lMapInventory.put( "PART_NO_OEM", "'A0000005'" );
      lMapInventory.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventory.put( "AUTHORITY_CD", "'N/A'" ); // duplicate fields in database
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // create authority map
      Map<String, String> lMapAuthority = new LinkedHashMap<String, String>();

      lMapAuthority.put( "AUTHORITY_DB_ID", "30" );
      lMapAuthority.put( "AUTHORITY_ID", "1002" );
      lMapAuthority.put( "AUTHORITY_CD", "'N/A'" );
      lMapAuthority.put( "AUTHORITY_NAME", "'N/A'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( "ORG_AUTHORITY", lMapAuthority ) );

      // call inventory validation
      runALValidateInventory();

      // insert map
      deleteFromTableWhere( "ORG_AUTHORITY", TableUtil.whereFromTableByMap( lMapAuthority ) );

      // call inventory validation and check result against expectation
      checkInventory( "test_AML_10208_NonUniqueAuthorityCd", "AML-10208" );
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
    * <li>Error Code: AML-10212</li>
    * <li>INVENTORY field VENDOR_CD exists multiple times in Maintenix ORG_VENDOR table.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10212_NonUniqueVendorCd() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'TRK-10005'" );
      lMapInventory.put( "PART_NO_OEM", "'A0000005'" );
      lMapInventory.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventory.put( "VENDOR_CD", "'10005'" ); // duplicate fields in database
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // create authority map
      Map<String, String> lMapVendor = new LinkedHashMap<String, String>();

      lMapVendor.put( "VENDOR_DB_ID", "30" );
      lMapVendor.put( "VENDOR_ID", "100005" );
      lMapVendor.put( "VENDOR_CD", "'10005'" );
      lMapVendor.put( "VENDOR_NAME", "'Vendor 5'" );
      lMapVendor.put( "OWNER_DB_ID", "4650" );
      lMapVendor.put( "OWNER_ID", "100006" );
      lMapVendor.put( "VENDOR_TYPE_DB_ID", "0" );
      lMapVendor.put( "VENDOR_TYPE_CD", "'BROKER'" );
      lMapVendor.put( "VENDOR_LOC_DB_ID", "4650" );
      lMapVendor.put( "VENDOR_LOC_ID", "100046" );
      lMapVendor.put( "CURRENCY_DB_ID", "10" );
      lMapVendor.put( "CURRENCY_CD", "'USD'" );
      lMapVendor.put( "VENDOR_APPROVAL_DB_ID", "10" );
      lMapVendor.put( "VENDOR_APPROVAL_CD", "'PURCHASE'" );
      lMapVendor.put( "NO_PRINT_REQ_BOOL", "0" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( "ORG_VENDOR", lMapVendor ) );

      // call inventory validation
      runALValidateInventory();

      // insert map
      deleteFromTableWhere( "ORG_VENDOR", TableUtil.whereFromTableByMap( lMapVendor ) );

      // call inventory validation and check result against expectation
      checkInventory( "test_AML_10212_NonUniqueVendorCd", "AML-10212" );
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
    * <li>Error Code: AML-10214</li>
    * <li>INVENTORY field INV_COND_CD exists multiple times in Maintenix REF_INV_COND table.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10214_NonUniqueInvConidtionCd() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'TRK-10005'" );
      lMapInventory.put( "PART_NO_OEM", "'A0000005'" );
      lMapInventory.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'AUTO'" );
      lMapInventory.put( "VENDOR_CD", "'10005'" ); // duplicate fields in database
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // // call inventory validation
      runALValidateInventory();

      // call inventory validation and check result against expectation
      checkInventory( "test_AML_10214_NonUniqueInvConidtionCd", "AML-10214" );
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
    * <li>Error Code: AML-10216</li>
    * <li>INVENTORY field OWNER_CD exists multiple times in Maintenix INV_OWNER table.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10216_NonUniqueOwnerCd() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'BATCH-00'" );
      lMapInventory.put( "PART_NO_OEM", "'A0000009'" );
      lMapInventory.put( "INV_CLASS_CD", "'BATCH'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'RFI'" );
      lMapInventory.put( "BIN_QT", "1" );
      lMapInventory.put( "OWNER_CD", "'MXI'" ); // non-unique value in mx
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // create authority map
      Map<String, String> lMapInvOwner = new LinkedHashMap<String, String>();

      lMapInvOwner.put( "OWNER_DB_ID", "4650" );
      lMapInvOwner.put( "OWNER_ID", "1002" );
      lMapInvOwner.put( "OWNER_CD", "'MXI'" );
      lMapInvOwner.put( "OWNER_NAME", "'DUPLICATE'" );
      lMapInvOwner.put( "LOCAL_BOOL", "0" );
      lMapInvOwner.put( "DEFAULT_BOOL", "0" );
      lMapInvOwner.put( "ORG_DB_ID", "0" );
      lMapInvOwner.put( "ORG_ID", "1" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( "INV_OWNER", lMapInvOwner ) );

      // call inventory validation
      runALValidateInventory();

      // insert map
      deleteFromTableWhere( "INV_OWNER", TableUtil.whereFromTableByMap( lMapInvOwner ) );

      // call inventory validation and check result against expectation
      checkInventory( "test_AML_10216_NonUniqueOwnerCd", "AML-10216" );
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
    * <li>Error Code: AML-10218</li>
    * <li>INVENTORY field INV_CAPABILITY_CD exists multiple times in Maintenix REF_INV_CAPABILITY
    * table.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10218_NonUniqueInvCapabilityCd() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-SN'" );
      lMapInventory.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventory.put( "INV_CLASS_CD", "'ACFT'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventory.put( "INV_CAPABILITY_CD", "'NORM'" ); // duplicate exists in test database
      lMapInventory.put( "AC_REG_CD", "'10102'" );
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // create authority map
      Map<String, String> lMapInvCapability = new LinkedHashMap<String, String>();

      lMapInvCapability.put( "INV_CAPABILITY_DB_ID", "20" );
      lMapInvCapability.put( "INV_CAPABILITY_CD", "'NORM'" );
      lMapInvCapability.put( "BITMAP_DB_ID", "0" );
      lMapInvCapability.put( "BITMAP_TAG", "95" );
      lMapInvCapability.put( "DESC_SDESC", "'DUP'" );
      lMapInvCapability.put( "DESC_LDESC", "'DUPLICATE'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( "REF_INV_CAPABILITY", lMapInvCapability ) );

      // call inventory validation
      runALValidateInventory();

      // insert map
      deleteFromTableWhere( "REF_INV_CAPABILITY",
            TableUtil.whereFromTableByMap( lMapInvCapability ) );

      // call inventory validation and check result against expectation
      checkInventory( "test_AML_10218_NonUniqueInvCapabilityCd", "AML-10218" );
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
    * <li>Error Code: AML-10220</li>
    * <li>INVENTORY field CARRIER_NAME exists multiple times in Maintenix ORG_CARRIER table.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10220_NonUniqueCarrierCd() throws Exception {

      // only run test against version 8.2, not 8.2-SP1 or later
      if ( getCurrentMxVersion().equals( "8200," ) ) {

         // create task map
         Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

         lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-SN'" );
         lMapInventory.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
         lMapInventory.put( "INV_CLASS_CD", "'ACFT'" );
         lMapInventory.put( "MANUFACT_CD", "'10001'" );
         lMapInventory.put( "LOC_CD", "'OPS'" );
         lMapInventory.put( "INV_COND_CD", "'REPREQ'" );
         lMapInventory.put( "INV_CAPABILITY_CD", "'NORM'" );
         lMapInventory.put( "CARRIER_CD", "'MXI'" ); // non-unique field in database
         lMapInventory.put( "AC_REG_CD", "'10102'" );
         lMapInventory.put( "INV_OPER_CD", "'NORM'" );
         lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
         lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

         // insert map
         runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

         // create authority map
         Map<String, String> lMapOrgCarrier = new LinkedHashMap<String, String>();

         lMapOrgCarrier.put( "CARRIER_DB_ID", "0" );
         lMapOrgCarrier.put( "CARRIER_ID", "1002" );
         lMapOrgCarrier.put( "CARRIER_CD", "'MXI'" );
         lMapOrgCarrier.put( "ORG_DB_ID", "0" );
         lMapOrgCarrier.put( "ORG_ID", "1" );
         lMapOrgCarrier.put( "IATA_CD", "'MXI'" );
         lMapOrgCarrier.put( "ICAO_CD", "'MXI'" );
         lMapOrgCarrier.put( "EXTRN_CTRL_BOOL", "0" );

         // insert map
         runInsert( TableUtil.getInsertForTableByMap( "ORG_CARRIER", lMapOrgCarrier ) );

         // call inventory validation
         runALValidateInventory();

         // insert map
         deleteFromTableWhere( "ORG_CARRIER", TableUtil.whereFromTableByMap( lMapOrgCarrier ) );

         // call inventory validation and check result against expectation
         checkInventory( "test_AML_10220_NonUniqueCarrierCd", "AML-10220" );
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
    * <li>Error Code: AML-10222</li>
    * <li>INVENTORY field COUNTRY_CD exists multiple times in Maintenix REF_COUNTRY table.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10222_NonUniqueCountryCd() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-SN'" );
      lMapInventory.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventory.put( "INV_CLASS_CD", "'ACFT'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventory.put( "INV_CAPABILITY_CD", "'NORM'" );
      lMapInventory.put( "COUNTRY_CD", "'USA'" ); // non-unique field in database
      lMapInventory.put( "AC_REG_CD", "'10102'" );
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // create authority map
      Map<String, String> lMapRefCountry = new LinkedHashMap<String, String>();

      lMapRefCountry.put( "COUNTRY_DB_ID", "20" );
      lMapRefCountry.put( "COUNTRY_CD", "'USA'" );
      lMapRefCountry.put( "BITMAP_DB_ID", "0" );
      lMapRefCountry.put( "BITMAP_TAG", "28" );
      lMapRefCountry.put( "COUNTRY_NAME", "'UNITED STATES'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( "REF_COUNTRY", lMapRefCountry ) );

      // call inventory validation
      runALValidateInventory();

      // insert map
      deleteFromTableWhere( "REF_COUNTRY", TableUtil.whereFromTableByMap( lMapRefCountry ) );

      // call inventory validation and check result against expectation
      checkInventory( "test_AML_10222_NonUniqueCountryCd", "AML-10222" );
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
    * <li>Error Code: AML-10224</li>
    * <li>INVENTORY field REG_BODY_CD exists multiple times in Maintenix REF_REG_BODY table.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10224_NonUniqueRegBodyCd() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-SN'" );
      lMapInventory.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventory.put( "INV_CLASS_CD", "'ACFT'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventory.put( "INV_CAPABILITY_CD", "'NORM'" );
      lMapInventory.put( "REG_BODY_CD", "'FAA'" ); // duplicate exists in test database
      lMapInventory.put( "AC_REG_CD", "'10102'" );
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // create authority map
      Map<String, String> lMapRegBody = new LinkedHashMap<String, String>();

      lMapRegBody.put( "REG_BODY_DB_ID", "20" );
      lMapRegBody.put( "REG_BODY_CD", "'FAA'" );
      lMapRegBody.put( "BITMAP_DB_ID", "0" );
      lMapRegBody.put( "BITMAP_TAG", "28" );
      lMapRegBody.put( "DESC_SDESC", "'DUP'" );
      lMapRegBody.put( "DESC_LDESC", "'DUPLICATE'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( "REF_REG_BODY", lMapRegBody ) );

      // call inventory validation
      runALValidateInventory();

      // insert map
      deleteFromTableWhere( "REF_REG_BODY", TableUtil.whereFromTableByMap( lMapRegBody ) );

      // call inventory validation and check result against expectation
      checkInventory( "test_AML_10224_NonUniqueRegBodyCd", "AML-10224" );
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
    * <li>Error Code: AML-10226</li>
    * <li>INVENTORY field INV_OPER_CD exists multiple times in Maintenix REF_INV_OPER table.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10226_NonUniqueInvOperCd() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-SN'" );
      lMapInventory.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventory.put( "INV_CLASS_CD", "'ACFT'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventory.put( "INV_CAPABILITY_CD", "'NORM'" ); // duplicate exists in test database
      lMapInventory.put( "AC_REG_CD", "'10102'" );
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // create authority map
      Map<String, String> lMapInvOper = new LinkedHashMap<String, String>();

      lMapInvOper.put( "INV_OPER_DB_ID", "20" );
      lMapInvOper.put( "INV_OPER_CD", "'NORM'" );
      lMapInvOper.put( "BITMAP_DB_ID", "0" );
      lMapInvOper.put( "BITMAP_TAG", "1" );
      lMapInvOper.put( "DESC_SDESC", "'DUP'" );
      lMapInvOper.put( "DESC_LDESC", "'DUPLICATE'" );
      lMapInvOper.put( "OPER_ORD", "21" );
      lMapInvOper.put( "AVAIL_BOOL", "1" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( "REF_INV_OPER", lMapInvOper ) );

      // call inventory validation
      runALValidateInventory();

      // insert map
      deleteFromTableWhere( "REF_INV_OPER", TableUtil.whereFromTableByMap( lMapInvOper ) );

      // call inventory validation and check result against expectation
      checkInventory( "test_AML_10226_NonUniqueInvOperCd", "AML-10226" );
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
    * <li>Error Code: AML-10236</li>
    * <li>Maintenix Baseline - The provided EQP_POS_CD exists multiple times in Maintenix for the
    * Part Group identified by BOM_PART_CD. It should be unique.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10236_NonUniqueEqpPosCd_Sub() throws Exception {

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
      lMapInventorySub.put( "EQP_POS_CD", "'1'" ); // duplicate value will be inserted for test
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

      // Get ASSMBL_BOM_ID from eqp_bom_part
      String lquery =
            "select ASSMBL_BOM_ID from eqp_bom_part where ASSMBL_CD='ACFT_CD1' and bom_part_cd='ACFT-SYS-1-1-TRK-P1'";
      String lId = getStringValueFromQuery( lquery, "ASSMBL_BOM_ID" );

      // create duplicate position code map
      Map<String, String> lMapEqpAssmblPos = new LinkedHashMap<String, String>();

      lMapEqpAssmblPos.put( "ASSMBL_DB_ID", "4650" );
      lMapEqpAssmblPos.put( "ASSMBL_CD", "'ACFT_CD1'" );
      lMapEqpAssmblPos.put( "ASSMBL_BOM_ID", "'" + lId + "'" );
      lMapEqpAssmblPos.put( "ASSMBL_POS_ID", "2" ); // makes unique
      lMapEqpAssmblPos.put( "NH_ASSMBL_DB_ID", "4650" );
      lMapEqpAssmblPos.put( "NH_ASSMBL_CD", "'ACFT_CD1'" );
      lMapEqpAssmblPos.put( "NH_ASSMBL_BOM_ID", "2" );
      lMapEqpAssmblPos.put( "NH_ASSMBL_POS_ID", "1" );
      lMapEqpAssmblPos.put( "EQP_POS_CD", "'1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( "EQP_ASSMBL_POS", lMapEqpAssmblPos ) );

      // call inventory validation
      runALValidateInventory();

      // insert map
      deleteFromTableWhere( "EQP_ASSMBL_POS", TableUtil.whereFromTableByMap( lMapEqpAssmblPos ) );

      // call inventory validation and check result against expectation
      checkInventory( "test_AML_10236_NonUniqueEqpPosCd", "AML-10236" );
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
    * <li>Error Code: AML-10238</li>
    * <li>SUB_INVENTORY field INV_CLASS_CD exists multiple times in Maintenix REF_INV_CLASS
    * table.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10238_NonUniqueInvClassCd_Sub() throws Exception {

      // create map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-SN'" );
      lMapInventory.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventory.put( "INV_CLASS_CD", "'ACFT'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" ); // incorrect value to fail parent
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
      lMapInventorySub.put( "INV_CLASS_CD", "'TRK'" ); // duplicate fields in database
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

      // create authority map
      Map<String, String> lMapInvClass = new LinkedHashMap<String, String>();

      lMapInvClass.put( "INV_CLASS_DB_ID", "4650" );
      lMapInvClass.put( "INV_CLASS_CD", "'TRK'" );
      lMapInvClass.put( "BITMAP_DB_ID", "0" );
      lMapInvClass.put( "BITMAP_TAG", "1" );
      lMapInvClass.put( "TRACKED_BOOL", "1" );
      lMapInvClass.put( "TRACEABLE_BIN_BOOL", "0" );
      lMapInvClass.put( "SERIAL_BOOL", "1" );
      lMapInvClass.put( "DESC_SDESC", "'TEST'" );
      lMapInvClass.put( "DESC_LDESC", "'TEST'" );
      lMapInvClass.put( "RECEIPT_INSP_BOOL", "1" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( "REF_INV_CLASS", lMapInvClass ) );

      // call inventory validation
      runALValidateInventory();

      // insert map
      deleteFromTableWhere( "REF_INV_CLASS", TableUtil.whereFromTableByMap( lMapInvClass ) );

      // call inventory validation and check result against expectation
      checkInventory( "test_AML_10238_NonUniqueInvClassCd_Sub", "AML-10238" );
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
    * <li>Error Code: AML-10240</li>
    * <li>SUB_INVENTORY field AUTHORITY_CD exists multiple times in Maintenix ORG_AUTHORITY
    * table.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10240_NonUniqueAuthorityCd_Sub() throws Exception {

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
      lMapInventorySub.put( "AUTHORITY_CD", "'N/A'" ); // duplicate fields in database

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

      // create authority map
      Map<String, String> lMapAuthority = new LinkedHashMap<String, String>();

      lMapAuthority.put( "AUTHORITY_DB_ID", "30" );
      lMapAuthority.put( "AUTHORITY_ID", "1002" );
      lMapAuthority.put( "AUTHORITY_CD", "'N/A'" );
      lMapAuthority.put( "AUTHORITY_NAME", "'N/A'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( "ORG_AUTHORITY", lMapAuthority ) );

      // call inventory validation
      runALValidateInventory();

      // insert map
      deleteFromTableWhere( "ORG_AUTHORITY", TableUtil.whereFromTableByMap( lMapAuthority ) );

      // call inventory validation and check result against expectation
      checkInventory( "test_AML_10240_NonUniqueAuthorityCd_Sub", "AML-10240" );
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
    * <li>Error Code: AML-10242</li>
    * <li>SUB_INVENTORY field VENDOR_CD exists multiple times in Maintenix ORG_VENDOR table.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10242_NonUniqueVendorCd_Sub() throws Exception {

      // create map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-SN'" );
      lMapInventory.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventory.put( "INV_CLASS_CD", "'ACFT'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" ); // incorrect value to fail parent
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
      lMapInventorySub.put( "VENDOR_CD", "'10005'" ); // duplicate fields in database

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

      // create authority map
      Map<String, String> lMapVendor = new LinkedHashMap<String, String>();

      lMapVendor.put( "VENDOR_DB_ID", "30" );
      lMapVendor.put( "VENDOR_ID", "100005" );
      lMapVendor.put( "VENDOR_CD", "'10005'" );
      lMapVendor.put( "VENDOR_NAME", "'Vendor 5'" );
      lMapVendor.put( "OWNER_DB_ID", "4650" );
      lMapVendor.put( "OWNER_ID", "100006" );
      lMapVendor.put( "VENDOR_TYPE_DB_ID", "0" );
      lMapVendor.put( "VENDOR_TYPE_CD", "'BROKER'" );
      lMapVendor.put( "VENDOR_LOC_DB_ID", "4650" );
      lMapVendor.put( "VENDOR_LOC_ID", "100046" );
      lMapVendor.put( "CURRENCY_DB_ID", "10" );
      lMapVendor.put( "CURRENCY_CD", "'USD'" );
      lMapVendor.put( "VENDOR_APPROVAL_DB_ID", "10" );
      lMapVendor.put( "VENDOR_APPROVAL_CD", "'PURCHASE'" );
      lMapVendor.put( "NO_PRINT_REQ_BOOL", "0" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( "ORG_VENDOR", lMapVendor ) );

      // call inventory validation
      runALValidateInventory();

      // insert map
      deleteFromTableWhere( "ORG_VENDOR", TableUtil.whereFromTableByMap( lMapVendor ) );

      // call inventory validation and check result against expectation
      checkInventory( "test_AML_10242_NonUniqueVendorCd_Sub", "AML-10242" );
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
    * <li>Error Code: AML-10248</li>
    * <li>Maintenix Baseline - The provided DATA_TYPE_CD exists multiple times in Maintenix. It
    * should be unique.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10248_NonUniqueDataTypeCd() throws Exception {

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
      lInventoryUsage.put( "DATA_TYPE_CD", "'HOURS'" ); // duplicate value added to database
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

      // create authority map
      Map<String, String> lMapDataType = new LinkedHashMap<String, String>();

      lMapDataType.put( "DATA_TYPE_DB_ID", "0" );
      lMapDataType.put( "DATA_TYPE_ID", "199" );
      lMapDataType.put( "ENG_UNIT_DB_ID", "0" );
      lMapDataType.put( "ENG_UNIT_CD", "'HOUR'" );
      lMapDataType.put( "DOMAIN_TYPE_DB_ID", "0" );
      lMapDataType.put( "DOMAIN_TYPE_CD", "'US'" );
      lMapDataType.put( "ENTRY_PREC_QT", "2" );
      lMapDataType.put( "DATA_TYPE_CD", "'HOURS'" );
      lMapDataType.put( "DATA_TYPE_SDESC", "'TEST'" );
      lMapDataType.put( "DATA_TYPE_MDESC", "'TEST'" );
      lMapDataType.put( "FORECAST_BOOL", "1" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( "MIM_DATA_TYPE", lMapDataType ) );

      // call inventory validation
      runALValidateInventory();

      // insert map
      deleteFromTableWhere( "MIM_DATA_TYPE", TableUtil.whereFromTableByMap( lMapDataType ) );

      // call inventory validation and check result against expectation
      checkInventory( "test_AML_10248_NonUniqueDataTypeCd", "AML-10248" );
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
    * <li>Error Code: AML-10252</li>
    * <li>Maintenix Baseline - The provided EQP_POS_CD exists multiple times in Maintenix for the
    * Part Group identified by BOM_PART_CD. It should be unique.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10252_NonUniqueEqpPosCd_Attach() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'SN-10013P'" );
      lMapInventory.put( "PART_NO_OEM", "'A0000005'" );
      lMapInventory.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2013','mm/dd/yyyy')" );
      lMapInventory.put( "ASSMBL_CD", "'ACFT_CD1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // create map
      Map<String, String> lInventoryUsage = new LinkedHashMap<String, String>();
      lInventoryUsage.put( "SERIAL_NO_OEM", "'SN-10013P'" );
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
      lMapInventorySub.put( "INSTALL_DT", "to_date('01/02/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_ATTACH, lMapInventorySub ) );

      // create map
      Map<String, String> lInventoryChildUsage = new LinkedHashMap<String, String>();
      lInventoryChildUsage.put( "SERIAL_NO_OEM", "'SN-10013A'" );
      lInventoryChildUsage.put( "PART_NO_OEM", "'ENG_ASSY_PN1'" );
      lInventoryChildUsage.put( "MANUFACT_CD", "'ABC11'" );
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

      // Get ASSMBL_BOM_ID from eqp_bom_part
      String lquery =
            "select ASSMBL_BOM_ID from eqp_bom_part where ASSMBL_CD='ACFT_CD1' and bom_part_cd='ENG-ASSY'";
      String lId = getStringValueFromQuery( lquery, "ASSMBL_BOM_ID" );

      // create duplicate position code map
      Map<String, String> lMapEqpAssmblPos = new LinkedHashMap<String, String>();

      lMapEqpAssmblPos.put( "ASSMBL_DB_ID", "4650" );
      lMapEqpAssmblPos.put( "ASSMBL_CD", "'ACFT_CD1'" );
      lMapEqpAssmblPos.put( "ASSMBL_BOM_ID", "'" + lId + "'" ); //
      lMapEqpAssmblPos.put( "ASSMBL_POS_ID", "3" ); // makes unique
      lMapEqpAssmblPos.put( "NH_ASSMBL_DB_ID", "4650" );
      lMapEqpAssmblPos.put( "NH_ASSMBL_CD", "'ACFT_CD1'" );
      lMapEqpAssmblPos.put( "NH_ASSMBL_BOM_ID", "9" );
      lMapEqpAssmblPos.put( "NH_ASSMBL_POS_ID", "1" );
      lMapEqpAssmblPos.put( "EQP_POS_CD", "'1'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( "EQP_ASSMBL_POS", lMapEqpAssmblPos ) );

      // call inventory validation
      runALValidateInventory();

      // insert map
      deleteFromTableWhere( "EQP_ASSMBL_POS", TableUtil.whereFromTableByMap( lMapEqpAssmblPos ) );

      // call inventory validation and check result against expectation
      checkInventory( "test_AML_10252_NonUniqueEqpPosCd_Attach", "AML-10252" );
   }
}
