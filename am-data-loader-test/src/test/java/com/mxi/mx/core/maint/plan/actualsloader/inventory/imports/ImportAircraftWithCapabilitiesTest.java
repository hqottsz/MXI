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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;
import com.mxi.mx.util.StoreProcedureRunner;
import com.mxi.mx.util.StringUtils;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.TestConstants;
import com.mxi.mx.util.WhereClause;


/**
 * This test suite contains test cases to import aircraft with capabilities
 *
 * @author Alicia Qian
 */
public class ImportAircraftWithCapabilitiesTest extends AbstractImportInventory {

   @Rule
   public TestName testName = new TestName();

   public String iSERIAL_NO_OEM_ACFT = "ACFT00001";
   public String iSERIAL_NO_OEM_APU = "APU00001";
   public String iCAP_CD_1 = "CAPTEST";
   public String iCAP_CD_2 = "ALAND";
   public String iCAP_CD_3 = "SEATNUM";
   public String iCAP_CD_4 = "WIFI";
   public String iCAP_CD_ANYTHING = "ANYTHING";
   public String iLEVEL_CD_CAPTEST_1 = "NOETOPS";
   public String iLEVEL_CD_CAPTEST_2 = "ETOPS90";
   public String iLEVEL_CD_CAPTEST_3 = "ETOPS120";
   public String iLEVEL_CD_CAPTEST_4 = "ETOPS200";

   public String iLEVEL_CD_ALAND_1 = "CATI";
   public String iLEVEL_CD_ALAND_2 = "CATIII";

   public String iLEVEL_CD_SEATNUM_1 = "122";
   public String iLEVEL_CD_SEATNUM_2 = "175";

   public String iEVENT_SDESC = "Bulk Load Capabilities";
   public String iEVENT_SDESC_2 =
         "[ActualsLoader] Installation of (1) Part Name - Engine (PN: ENG_ASSY_PN1, SN: ASSY-00001)";
   public String iEVENT_TYPE_CD = "CC";
   public String iEVENT_TYPE_CD_FG = "FG";

   public simpleIDs iINV_Ids = null;
   public simpleIDs iINV_Ids_ASSY = null;
   public simpleIDs iINV_Ids_TRK = null;
   public simpleIDs iINV_Ids_SER = null;
   public simpleIDs iEventIDs_1 = null;
   public simpleIDs iEventIDs_2 = null;


   /**
    * Clean up after each individual test
    *
    * @throws Exception
    */
   @After
   @Override
   public void after() throws Exception {

      // clean up the event data
      RestoreData();

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
      iINV_Ids = null;
      iINV_Ids_ASSY = null;
      iINV_Ids_TRK = null;
      iINV_Ids_SER = null;
      iEventIDs_1 = null;
      iEventIDs_2 = null;
   }


   /**
    * This is to validation function of testOPER_25921_ACFT_TWO_CD_IMPORT(); OPER-25921: Actuals
    * Loader Inventory Capability validation is performing slowly.
    *
    *
    *
    */
   public void testOPER_25921_ACFT_TWO_CD_VALIDATION() throws Exception {

      String lRandom = String.valueOf( getRandom() );

      // create inventory map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();
      lMapInventory.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ACFT + "'" );
      lMapInventory.put( "PART_NO_OEM", "'ACFT_ASSY_PN2'" );
      lMapInventory.put( "INV_CLASS_CD", "'ACFT'" );
      lMapInventory.put( "MANUFACT_CD", "'11111'" );
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

      Map<String, String> lMapCapability1 = new LinkedHashMap<String, String>();

      lMapCapability1.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ACFT + "'" );
      lMapCapability1.put( "PART_NO_OEM", "'ACFT_ASSY_PN2'" );
      lMapCapability1.put( "MANUFACT_CD", "'11111'" );
      lMapCapability1.put( "CAP_CD", "'" + iCAP_CD_1 + "'" );
      lMapCapability1.put( "LEVEL_CD", "'" + iLEVEL_CD_CAPTEST_2 + "'" );
      // lMapCapability1.put( "CUSTOM_LEVEL", "1" );
      lMapCapability1.put( "CONFIG_LEVEL_CD", "'" + iLEVEL_CD_CAPTEST_4 + "'" );

      // insert map

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_CAP_LEVELS,
            lMapCapability1 ) );

      Map<String, String> lMapCapability2 = new LinkedHashMap<String, String>();

      lMapCapability2.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ACFT + "'" );
      lMapCapability2.put( "PART_NO_OEM", "'ACFT_ASSY_PN2'" );
      lMapCapability2.put( "MANUFACT_CD", "'11111'" );
      lMapCapability2.put( "CAP_CD", "'" + iCAP_CD_2 + "'" );
      lMapCapability2.put( "LEVEL_CD", "'" + iLEVEL_CD_ALAND_1 + "'" );
      lMapCapability2.put( "CUSTOM_LEVEL", null );
      lMapCapability2.put( "CONFIG_LEVEL_CD", "'" + iLEVEL_CD_ALAND_2 + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_CAP_LEVELS,
            lMapCapability2 ) );

      // run validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      checkInventory( testName.getMethodName(), "PASS" );

   }


   /**
    * This test is to verify fix of OPER-25921 would not change existing functionality of inventory
    * loading. OPER-25921: Actuals Loader Inventory Capability validation is performing slowly.
    *
    *
    *
    */

   @Test
   public void testOPER_25921_ACFT_TWO_CD_IMPORT() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOPER_25921_ACFT_TWO_CD_VALIDATION();

      System.out.println( "Finish validation" );

      iINV_Ids = null;

      // run import
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), false, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      iINV_Ids = getInvIds( iSERIAL_NO_OEM_ACFT );
      verifyAcftCapLevel( iINV_Ids, iCAP_CD_1, iLEVEL_CD_CAPTEST_2, null, iLEVEL_CD_CAPTEST_4 );
      verifyAcftCapLevel( iINV_Ids, iCAP_CD_2, iLEVEL_CD_ALAND_1, null, iLEVEL_CD_ALAND_2 );

      // OPER-27687: add more validation on other cap_cds which inherited from assmbl_cap_level. not
      // all caps have been checked.
      verifyAcftCapLevel( iINV_Ids, iCAP_CD_4, null, null, null );
      verifyAcftCapLevel( iINV_Ids, iCAP_CD_3, null, null, null );

      // Verify evt_event table
      iEventIDs_1 = getEventIDs( iEVENT_TYPE_CD, iEVENT_SDESC );

   }


   /**
    * This is to validation function of testOPER_25921_APU_IMPORT(); OPER-25921: Actuals Loader
    * Inventory Capability validation is performing slowly.
    *
    *
    *
    */

   public void testOPER_25921_APU_VALIDATION() throws Exception {

      String lRandom = String.valueOf( getRandom() );

      // create inventory map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();
      lMapInventory.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_APU + "'" );
      lMapInventory.put( "PART_NO_OEM", "'APU_ASSY_PN1'" );
      lMapInventory.put( "INV_CLASS_CD", "'ASSY'" );
      lMapInventory.put( "MANUFACT_CD", "'1234567890'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'REPREQ'" );
      // lMapInventory.put( "AC_REG_CD", "'" + lRandom + "'" );
      // lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      // lMapInventory.put( "FORECAST_MODEL", "'Forecast1'" );
      // lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // create task map

      Map<String, String> lMapCapability2 = new LinkedHashMap<String, String>();

      lMapCapability2.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_APU + "'" );
      lMapCapability2.put( "PART_NO_OEM", "'APU_ASSY_PN1'" );
      lMapCapability2.put( "MANUFACT_CD", "'1234567890'" );
      lMapCapability2.put( "CAP_CD", "'" + iCAP_CD_1 + "'" );
      lMapCapability2.put( "LEVEL_CD", "'" + iLEVEL_CD_CAPTEST_2 + "'" );
      // lMapCapability2.put( "CUSTOM_LEVEL", "1" );
      lMapCapability2.put( "CONFIG_LEVEL_CD", "'" + iLEVEL_CD_CAPTEST_3 + "'" );
      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_CAP_LEVELS,
            lMapCapability2 ) );

      // run validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      checkInventory( testName.getMethodName(), "PASS" );

   }


   /**
    * This test is to verify fix of OPER-25921 would not change existing functionality of inventory
    * loading. OPER-25921: Actuals Loader Inventory Capability validation is performing slowly. This
    * is should be defect.
    *
    *
    */
   @Ignore
   @Test
   public void testOPER_25921_APU_IMPORT() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOPER_25921_APU_VALIDATION();

      System.out.println( "Finish validation" );

      iINV_Ids = null;

      // run import
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), false, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      iINV_Ids = getInvIds( iSERIAL_NO_OEM_APU );
      verifyAcftCapLevel( iINV_Ids, iCAP_CD_1, iLEVEL_CD_CAPTEST_2, null, iLEVEL_CD_CAPTEST_3 );

   }


   /**
    * This is to validation function of testOPER_25921_SEATNUM_IMPORT(); Verify cap_level validation
    * is skip if cap_cd=SEATNUM. OPER-25921: Actuals Loader Inventory Capability validation is
    * performing slowly.
    *
    *
    *
    */
   public void testOPER_25921_SEATNUM_VALIDATION() throws Exception {

      String lRandom = String.valueOf( getRandom() );

      // create inventory map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();
      lMapInventory.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ACFT + "'" );
      lMapInventory.put( "PART_NO_OEM", "'ACFT_ASSY_PN2'" );
      lMapInventory.put( "INV_CLASS_CD", "'ACFT'" );
      lMapInventory.put( "MANUFACT_CD", "'11111'" );
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

      Map<String, String> lMapCapability1 = new LinkedHashMap<String, String>();

      lMapCapability1.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ACFT + "'" );
      lMapCapability1.put( "PART_NO_OEM", "'ACFT_ASSY_PN2'" );
      lMapCapability1.put( "MANUFACT_CD", "'11111'" );
      lMapCapability1.put( "CAP_CD", "'" + iCAP_CD_3 + "'" );
      // lMapCapability1.put( "LEVEL_CD", "'" + iCAP_CD_ANYTHING + "'" );
      lMapCapability1.put( "CUSTOM_LEVEL", "1" );
      lMapCapability1.put( "CONFIG_LEVEL_CD", "'" + iLEVEL_CD_SEATNUM_2 + "'" );

      // insert map

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_CAP_LEVELS,
            lMapCapability1 ) );

      // run validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      checkInventory( testName.getMethodName(), "PASS" );

   }


   /**
    * This test is to verify fix of OPER-25921 would not change existing functionality of inventory
    * loading. Verify cap_cd=SEATNUM. OPER-25921: Actuals Loader Inventory Capability validation is
    * performing slowly.
    *
    *
    *
    */

   @Test
   public void testOPER_25921_SEATNUM_IMPORT() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOPER_25921_SEATNUM_VALIDATION();

      System.out.println( "Finish validation" );

      iINV_Ids = null;

      // run import
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), false, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      iINV_Ids = getInvIds( iSERIAL_NO_OEM_ACFT );
      verifyAcftCapLevel( iINV_Ids, iCAP_CD_3, null, "1", iLEVEL_CD_SEATNUM_2 );

      // OPER-27687: add more validation on other cap_cds which inherited from assmbl_cap_level. not
      // all caps have been checked.
      verifyAcftCapLevel( iINV_Ids, iCAP_CD_1, null, null, null );
      verifyAcftCapLevel( iINV_Ids, iCAP_CD_2, null, null, null );

      // Verify evt_event table
      iEventIDs_1 = getEventIDs( iEVENT_TYPE_CD, iEVENT_SDESC );

   }


   /**
    * This is to validation function of testOPER_25921_LEVEL_CD_IS_NO_IMPORT(); Verify LEVEL_CD
    * validation is skip if LEVEL_CD=NO. OPER-25921: Actuals Loader Inventory Capability validation
    * is performing slowly.
    *
    *
    *
    */
   public void testOPER_25921_LEVEL_CD_VALIDATION() throws Exception {

      String lRandom = String.valueOf( getRandom() );

      // create inventory map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();
      lMapInventory.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ACFT + "'" );
      lMapInventory.put( "PART_NO_OEM", "'ACFT_ASSY_PN2'" );
      lMapInventory.put( "INV_CLASS_CD", "'ACFT'" );
      lMapInventory.put( "MANUFACT_CD", "'11111'" );
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

      Map<String, String> lMapCapability1 = new LinkedHashMap<String, String>();

      lMapCapability1.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ACFT + "'" );
      lMapCapability1.put( "PART_NO_OEM", "'ACFT_ASSY_PN2'" );
      lMapCapability1.put( "MANUFACT_CD", "'11111'" );
      lMapCapability1.put( "CAP_CD", "'" + iCAP_CD_1 + "'" );
      lMapCapability1.put( "LEVEL_CD", "'" + iLEVEL_CD_CAPTEST_3 + "'" );
      // lMapCapability1.put( "CUSTOM_LEVEL", "1" );
      lMapCapability1.put( "CONFIG_LEVEL_CD", "'" + iLEVEL_CD_CAPTEST_4 + "'" );

      // insert map

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_CAP_LEVELS,
            lMapCapability1 ) );

      // run validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      checkInventory( testName.getMethodName(), "PASS" );

   }


   /**
    * This test is to verify fix of OPER-25921 would not change existing functionality of inventory
    * loading. Verify LEVEL_CD validation is skip if LEVEL_CD=NO. OPER-25921: Actuals Loader
    * Inventory Capability validation is performing slowly.
    *
    *
    *
    */

   @Test
   public void testOPER_25921_LEVEL_CD_IMPORT() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOPER_25921_LEVEL_CD_VALIDATION();

      System.out.println( "Finish validation" );

      iINV_Ids = null;

      // run import
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), false, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      iINV_Ids = getInvIds( iSERIAL_NO_OEM_ACFT );
      verifyAcftCapLevel( iINV_Ids, iCAP_CD_1, iLEVEL_CD_CAPTEST_3, null, iLEVEL_CD_CAPTEST_4 );

      // OPER-27687: add more validation on other cap_cds which inherited from assmbl_cap_level. not
      // all caps have been checked.
      verifyAcftCapLevel( iINV_Ids, iCAP_CD_3, null, null, null );
      verifyAcftCapLevel( iINV_Ids, iCAP_CD_4, null, null, null );

      // Verify evt_event table
      iEventIDs_1 = getEventIDs( iEVENT_TYPE_CD, iEVENT_SDESC );

   }


   /**
    * This test is to verify fix of OPER-27687: if no data in cap_level staging table, the ACFT will
    * inherited the assmbl_cap_level setup.
    *
    *
    *
    */
   public void testOPER_27687_NOCAPDEFINE_VALIDATION() throws Exception {

      String lRandom = String.valueOf( getRandom() );

      // create inventory map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();
      lMapInventory.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ACFT + "'" );
      lMapInventory.put( "PART_NO_OEM", "'ACFT_ASSY_PN2'" );
      lMapInventory.put( "INV_CLASS_CD", "'ACFT'" );
      lMapInventory.put( "MANUFACT_CD", "'11111'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventory.put( "AC_REG_CD", "'" + lRandom + "'" );
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "FORECAST_MODEL", "'Forecast1'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // run validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      checkInventory( testName.getMethodName(), "PASS" );

   }


   /**
    * This test is to verify fix of OPER-27687: if no data in cap_level staging table, the ACFT will
    * inherited the assmbl_cap_level setup.
    *
    *
    *
    */

   @Test
   public void testOPER_27687_NOCAPDEFINE_IMPORT() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOPER_27687_NOCAPDEFINE_VALIDATION();

      System.out.println( "Finish validation" );

      iINV_Ids = null;

      // run import
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), false, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      iINV_Ids = getInvIds( iSERIAL_NO_OEM_ACFT );

      // OPER-27687: add more validation on other cap_cds which inherited from assmbl_cap_level. not
      // all caps have been checked.
      verifyAcftCapLevel( iINV_Ids, iCAP_CD_4, null, null, null );
      verifyAcftCapLevel( iINV_Ids, iCAP_CD_3, null, null, null );

   }


   /**
    * This test is to verify fix of OPER-25921: Actuals Loader is adding non-aircraft inventory to
    * ACFT_CAP_LEVELS
    *
    *
    *
    */
   @Test
   public void testOPER_29794_VALIDATION() throws Exception {

      String lRandom = String.valueOf( getRandom() );

      // create inventory map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();
      lMapInventory.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ACFT + "'" );
      lMapInventory.put( "PART_NO_OEM", "'ACFT_ASSY_PN2'" );
      lMapInventory.put( "INV_CLASS_CD", "'ACFT'" );
      lMapInventory.put( "MANUFACT_CD", "'11111'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventory.put( "AC_REG_CD", "'" + lRandom + "'" );
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "FORECAST_MODEL", "'Forecast1'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // create inventory map for eng
      Map<String, String> lMapInventoryAssy = new LinkedHashMap<String, String>();
      lMapInventoryAssy.put( "SERIAL_NO_OEM", "'ASSY-00001'" );
      lMapInventoryAssy.put( "PART_NO_OEM", "'ENG_ASSY_PN1'" );
      lMapInventoryAssy.put( "INV_CLASS_CD", "'ASSY'" );
      // This value has been changed to validate the upper case and trim change for OPER=21977
      lMapInventoryAssy.put( "MANUFACT_CD", "' ABC11 '" );
      lMapInventoryAssy.put( "LOC_CD", "'OPS'" );
      lMapInventoryAssy.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventoryAssy ) );

      // create inventory map for trk
      Map<String, String> lMapInventoryTRK = new LinkedHashMap<String, String>();
      lMapInventoryTRK.put( "SERIAL_NO_OEM", "'TRK-00001'" );
      lMapInventoryTRK.put( "PART_NO_OEM", "'A0000010'" );
      lMapInventoryTRK.put( "INV_CLASS_CD", "'TRK'" );
      // This value has been changed to validate the upper case and trim change for OPER=21977
      lMapInventoryTRK.put( "MANUFACT_CD", "'11111'" );
      lMapInventoryTRK.put( "LOC_CD", "'OPS'" );
      lMapInventoryTRK.put( "ASSMBL_CD", "'ACFT_CD1'" );
      lMapInventoryTRK.put( "ASSMBL_BOM_CD", "'ACFT-SYS-1-1-TRK-BATCH-PARENT'" );
      lMapInventoryTRK.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventoryTRK ) );

      // create inventory attach map
      Map<String, String> lMapInventoryAttach = new LinkedHashMap<String, String>();
      lMapInventoryAttach.put( "PARENT_SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ACFT + "'" );
      lMapInventoryAttach.put( "PARENT_PART_NO_OEM", "'ACFT_ASSY_PN2'" );
      lMapInventoryAttach.put( "PARENT_MANUFACT_CD", "'11111'" );
      lMapInventoryAttach.put( "ATTACH_SERIAL_NO_OEM", "'ASSY-00001'" );
      lMapInventoryAttach.put( "ATTACH_PART_NO_OEM", "'ENG_ASSY_PN1'" );
      lMapInventoryAttach.put( "ATTACH_MANUFACT_CD", "'ABC11'" );
      lMapInventoryAttach.put( "BOM_PART_CD", "'ENG-ASSY'" );
      lMapInventoryAttach.put( "EQP_POS_CD", "'1'" );
      lMapInventoryAttach.put( "INSTALL_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_ATTACH, lMapInventoryAttach ) );

      // create task map

      Map<String, String> lMapCapability1 = new LinkedHashMap<String, String>();

      lMapCapability1.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ACFT + "'" );
      lMapCapability1.put( "PART_NO_OEM", "'ACFT_ASSY_PN2'" );
      lMapCapability1.put( "MANUFACT_CD", "'11111'" );
      lMapCapability1.put( "CAP_CD", "'" + iCAP_CD_1 + "'" );
      lMapCapability1.put( "LEVEL_CD", "'" + iLEVEL_CD_CAPTEST_2 + "'" );
      // lMapCapability1.put( "CUSTOM_LEVEL", "1" );
      lMapCapability1.put( "CONFIG_LEVEL_CD", "'" + iLEVEL_CD_CAPTEST_4 + "'" );

      // insert map

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_CAP_LEVELS,
            lMapCapability1 ) );

      Map<String, String> lMapCapability2 = new LinkedHashMap<String, String>();

      lMapCapability2.put( "SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ACFT + "'" );
      lMapCapability2.put( "PART_NO_OEM", "'ACFT_ASSY_PN2'" );
      lMapCapability2.put( "MANUFACT_CD", "'11111'" );
      lMapCapability2.put( "CAP_CD", "'" + iCAP_CD_2 + "'" );
      lMapCapability2.put( "LEVEL_CD", "'" + iLEVEL_CD_ALAND_1 + "'" );
      lMapCapability2.put( "CUSTOM_LEVEL", null );
      lMapCapability2.put( "CONFIG_LEVEL_CD", "'" + iLEVEL_CD_ALAND_2 + "'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_CAP_LEVELS,
            lMapCapability2 ) );

      // create sub inv map
      Map<String, String> lMapInventoryChild = new LinkedHashMap<String, String>();
      lMapInventoryChild.put( "PARENT_SERIAL_NO_OEM", "'" + iSERIAL_NO_OEM_ACFT + "'" );
      lMapInventoryChild.put( "PARENT_PART_NO_OEM", "'ACFT_ASSY_PN2'" );
      lMapInventoryChild.put( "PARENT_MANUFACT_CD", "'11111'" );
      lMapInventoryChild.put( "SERIAL_NO_OEM", "'SER0001'" );
      lMapInventoryChild.put( "PART_NO_OEM", "'A0001260'" );
      lMapInventoryChild.put( "MANUFACT_CD", "'11111'" );
      lMapInventoryChild.put( "INV_CLASS_CD", "'SER'" );
      lMapInventoryChild.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventoryChild ) );

      // run validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      checkInventoryNoWarnings( "PASS" );

   }


   /**
    * This test is to verify fix of OPER-25921: Actuals Loader is adding non-aircraft inventory to
    * ACFT_CAP_LEVELS
    *
    *
    *
    */

   @Test
   public void testOPER_29794_IMPORT() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOPER_29794_VALIDATION();

      System.out.println( "Finish validation" );

      iINV_Ids = null;

      // run import
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), false, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // Verify ACFT cap level(s) have been generated.
      iINV_Ids = getInvIds( iSERIAL_NO_OEM_ACFT );
      verifyAcftCapLevel( iINV_Ids, iCAP_CD_1, iLEVEL_CD_CAPTEST_2, null, iLEVEL_CD_CAPTEST_4 );
      verifyAcftCapLevel( iINV_Ids, iCAP_CD_2, iLEVEL_CD_ALAND_1, null, iLEVEL_CD_ALAND_2 );

      // verify eng has no cap level
      iINV_Ids_ASSY = getInvIds( "ASSY-00001" );
      String lQuery = "select 1 from " + TableUtil.ACFT_CAP_LEVELS + " where ACFT_NO_DB_ID="
            + iINV_Ids_ASSY.getNO_DB_ID() + " and ACFT_NO_ID=" + iINV_Ids_ASSY.getNO_ID();
      Assert.assertFalse( "Check acft_cap_levels table to verify the record does not exist: 1",
            RecordsExist( lQuery ) );

      // verify loose TRK has no cap level
      iINV_Ids_TRK = getInvIds( "TRK-00001" );
      lQuery = "select 1 from " + TableUtil.ACFT_CAP_LEVELS + " where ACFT_NO_DB_ID="
            + iINV_Ids_TRK.getNO_DB_ID() + " and ACFT_NO_ID=" + iINV_Ids_TRK.getNO_ID();
      Assert.assertFalse( "Check acft_cap_levels table to verify the record does not exist: 2",
            RecordsExist( lQuery ) );

      // verify loose SER has no cap level
      iINV_Ids_SER = getInvIds( "SER0001" );
      lQuery = "select 1 from " + TableUtil.ACFT_CAP_LEVELS + " where ACFT_NO_DB_ID="
            + iINV_Ids_SER.getNO_DB_ID() + " and ACFT_NO_ID=" + iINV_Ids_SER.getNO_ID();
      Assert.assertFalse( "Check acft_cap_levels table to verify the record does not exist: 3",
            RecordsExist( lQuery ) );

      // OPER-27687: add more validation on other cap_cds which inherited from assmbl_cap_level. not
      // all caps have been checked.
      verifyAcftCapLevel( iINV_Ids, iCAP_CD_4, null, null, null );
      verifyAcftCapLevel( iINV_Ids, iCAP_CD_3, null, null, null );

      // Verify evt_event table
      iEventIDs_1 = getEventIDs( iEVENT_TYPE_CD, iEVENT_SDESC );
      iEventIDs_2 = getEventIDs( iEVENT_TYPE_CD_FG, iEVENT_SDESC_2 );

   }


   // =======================================================================================================
   /**
    * This function is to verify acft_cap_levels table
    *
    *
    */
   public void verifyAcftCapLevel( simpleIDs aIDs, String aCAP_CD, String aLEVEL_CD,
         String aCUSTOM_LEVEL, String aCONFIG_LEVEL_CD ) {

      // EVT_INV table
      String[] iIds = { "LEVEL_CD", "CUSTOM_LEVEL", "CONFIG_LEVEL_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "ACFT_NO_DB_ID", aIDs.getNO_DB_ID() );
      lArgs.addArguments( "ACFT_NO_ID", aIDs.getNO_ID() );
      lArgs.addArguments( "CAP_CD", aCAP_CD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.ACFT_CAP_LEVELS, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      if ( aLEVEL_CD != null ) {
         Assert.assertTrue( "LEVEL_CD", llists.get( 0 ).get( 0 ).equalsIgnoreCase( aLEVEL_CD ) );
      } else {
         Assert.assertTrue( "LEVEL_CD", StringUtils.isBlank( aLEVEL_CD ) );

      }
      if ( aCUSTOM_LEVEL != null ) {
         Assert.assertTrue( "CUSTOM_LEVEL",
               llists.get( 0 ).get( 1 ).equalsIgnoreCase( aCUSTOM_LEVEL ) );
      }

      if ( aCONFIG_LEVEL_CD != null ) {
         Assert.assertTrue( "CONFIG_LEVEL_CD",
               llists.get( 0 ).get( 2 ).equalsIgnoreCase( aCONFIG_LEVEL_CD ) );
      }

   }


   /**
    * This function is to retrieve inv ids.
    *
    *
    */
   public simpleIDs getInvIds( String aSERIAL_NO_OEM ) {

      // REF_TASK_CLASS table
      String[] iIds = { "INV_NO_DB_ID", "INV_NO_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "SERIAL_NO_OEM", aSERIAL_NO_OEM );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.INV_INV, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lIds;

   }


   /**
    * This function is to retrieve event ID by giving event type and sdesc
    *
    *
    *
    */
   @Override
   public simpleIDs getEventIDs( String aEVENT_TYPE_CD, String aEVENT_SDESC ) {

      String[] iIds = { "EVENT_DB_ID", "EVENT_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "EVENT_TYPE_CD", aEVENT_TYPE_CD );
      lArgs.addArguments( "EVENT_SDESC", aEVENT_SDESC );

      String iQueryString = TableUtil.buildTableQueryOrderBy( TableUtil.EVT_EVENT, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lIds;
   }


   /**
    * This function is called by after test method to restore data which has been modified by tests.
    *
    *
    */

   public void RestoreData() {

      String lStrDelete = null;

      // Delete acft_cap_levels data which created by test case
      if ( iINV_Ids != null ) {
         lStrDelete = "delete from " + TableUtil.ACFT_CAP_LEVELS + " where ACFT_NO_DB_ID='"
               + iINV_Ids.getNO_DB_ID() + "' and ACFT_NO_ID='" + iINV_Ids.getNO_ID() + "'";
         executeSQL( lStrDelete );

         lStrDelete = "DELETE inv_curr_usage "
               + "WHERE (inv_no_db_id, inv_no_id) IN (SELECT inv_no_db_id, inv_no_id FROM inv_inv WHERE h_inv_no_id ='"
               + iINV_Ids.getNO_ID() + "')";
         executeSQL( lStrDelete );

         lStrDelete = "delete from " + TableUtil.INV_AC_REG + " where INV_NO_DB_ID='"
               + iINV_Ids.getNO_DB_ID() + "' and INV_NO_ID='" + iINV_Ids.getNO_ID() + "'";
         executeSQL( lStrDelete );

         lStrDelete = "delete from " + TableUtil.INV_INV + " where H_INV_NO_DB_ID='"
               + iINV_Ids.getNO_DB_ID() + "' and H_INV_NO_ID='" + iINV_Ids.getNO_ID() + "'";
         executeSQL( lStrDelete );

      }

      if ( iINV_Ids_ASSY != null ) {
         lStrDelete = "delete from " + TableUtil.INV_INV + " where H_INV_NO_DB_ID='"
               + iINV_Ids_ASSY.getNO_DB_ID() + "' and H_INV_NO_ID='" + iINV_Ids_ASSY.getNO_ID()
               + "'";
         executeSQL( lStrDelete );

         lStrDelete = "delete from " + TableUtil.INV_INV + " where INV_NO_DB_ID='"
               + iINV_Ids_ASSY.getNO_DB_ID() + "' and INV_NO_ID='" + iINV_Ids_ASSY.getNO_ID() + "'";
         executeSQL( lStrDelete );

      }

      if ( iINV_Ids_TRK != null ) {
         lStrDelete = "delete from " + TableUtil.INV_INV + " where H_INV_NO_DB_ID='"
               + iINV_Ids_TRK.getNO_DB_ID() + "' and H_INV_NO_ID='" + iINV_Ids_TRK.getNO_ID() + "'";
         executeSQL( lStrDelete );

         lStrDelete = "delete from " + TableUtil.INV_INV + " where INV_NO_DB_ID='"
               + iINV_Ids_TRK.getNO_DB_ID() + "' and INV_NO_ID='" + iINV_Ids_TRK.getNO_ID() + "'";
         executeSQL( lStrDelete );

      }

      if ( iINV_Ids_SER != null ) {
         lStrDelete = "delete from " + TableUtil.INV_INV + " where H_INV_NO_DB_ID='"
               + iINV_Ids_SER.getNO_DB_ID() + "' and H_INV_NO_ID='" + iINV_Ids_SER.getNO_ID() + "'";
         executeSQL( lStrDelete );

         lStrDelete = "delete from " + TableUtil.INV_INV + " where INV_NO_DB_ID='"
               + iINV_Ids_SER.getNO_DB_ID() + "' and INV_NO_ID='" + iINV_Ids_SER.getNO_ID() + "'";
         executeSQL( lStrDelete );

      }

      clearEvent( iEventIDs_1 );
      clearEvent( iEventIDs_2 );

   }

}
