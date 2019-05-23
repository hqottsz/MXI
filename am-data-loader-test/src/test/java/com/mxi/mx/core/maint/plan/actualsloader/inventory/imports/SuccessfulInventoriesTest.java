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
import com.mxi.mx.util.StoreProcedureRunner;
import com.mxi.mx.util.StringUtils;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.TestConstants;
import com.mxi.mx.util.WhereClause;


/**
 * This test suite contains test cases for successful inventory importing using Actuals Loader
 *
 * @author Alicia Qian
 */
public class SuccessfulInventoriesTest extends AbstractImportInventory {

   @Rule
   public TestName testName = new TestName();
   public simpleIDs iINV_Ids = null;
   public simpleIDs iINV_Ids_Assy = null;
   public simpleIDs iINV_Ids_Assy_2 = null;
   public simpleIDs iINV_ACFT_TRK_Ids = null;
   public simpleIDs iINV_ENG_TRK_Ids = null;

   public simpleIDs iEVENT_Ids_FG = null;
   public simpleIDs iEVENT_Ids_FG_2 = null;
   public simpleIDs iEVENT_Ids_FG_ACFT_TRK = null;
   public simpleIDs iEVENT_Ids_FG_ENG_TRK = null;

   public String iEVENT_TYPE_CD_FG = "FG";
   public String iEVENT_STATUS_CD_FGINST = "FGINST";

   public String iEVENT_SDESC_ASSY_FGINST =
         "[ActualsLoader] Installation of (1) Part Name - Engine (PN: ENG_ASSY_PN1, SN: ASSY-000001)";
   public String iEVENT_SDESC_ACFT_TRK_FGINST =
         "[ActualsLoader] Installation of BATCH-on-TRK Parent (PN: A0000010, SN: TRK-ACFT-00001)";
   public String iEVENT_SDESC_ENG_TRK_FGINST =
         "[ActualsLoader] Installation of BATCH-on-TRK Parent (PN: E0000010, SN: TRK-ENG-00001)";

   public int iBOMPartID_next = 0;
   public String iBOMParID_Origin;

   public String iEVENT_SDESC_ASSY_FGINST_2 =
         "[ActualsLoader] Installation of (1) Part Name - Engine8 (PN: ENG_ASSY_PN8, SN: ASSY-00001)";
   public String iEVENT_SDESC_ASSY_FGINST_3 =
         "[ActualsLoader] Installation of (1) Part Name - Engine8 (PN: ENG_ASSY_PN8, SN: ASSY-00001)";


   /**
    * Clean up after each individual test
    *
    * @throws Exception
    */
   @After
   @Override
   public void after() throws Exception {

      RestoreData();
      String strTCName = testName.getMethodName();
      if ( strTCName.contains( "17471" ) ) {
         restorePart();
      }

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

      String strTCName = testName.getMethodName();
      if ( strTCName.contains( "17471" ) ) {
         setPart();
      }

      iINV_Ids = null;
      iINV_Ids_Assy = null;
      iINV_Ids_Assy_2 = null;
      iINV_ACFT_TRK_Ids = null;
      iINV_ENG_TRK_Ids = null;
      iEVENT_Ids_FG = null;
      iEVENT_Ids_FG_2 = null;
      iEVENT_Ids_FG_ACFT_TRK = null;
      iEVENT_Ids_FG_ENG_TRK = null;
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
    * Note: this test case will test upper case + trim on the manufact_cd will be applied on current
    * data (OPER-21977)
    * <li>Note: this test case will test upper case + trim on the manufact_cd will be applied on
    * current data (OPER-21977)</li>
    * <li>Note : this is to very the fix OPER-20109</li>
    *
    * @throws Exception
    */

   @Test
   public void testAttachedAssemblyImportsSuccessfully() throws Exception {

      // String lRandom = String.valueOf( getRandom() );
      String lRandom = "000001";

      // create inventory map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();
      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-" + lRandom + "'" );
      lMapInventory.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventory.put( "INV_CLASS_CD", "'ACFT'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'REPREQ'" );
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
      // This value has been changed to validate the upper case and trim change for OPER=21977
      lMapInventoryAssy.put( "MANUFACT_CD", "' abC11 '" );
      lMapInventoryAssy.put( "LOC_CD", "'OPS'" );
      lMapInventoryAssy.put( "INV_COND_CD", "'INSRV'" );
      lMapInventoryAssy.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventoryAssy ) );

      // create inventory attach map
      Map<String, String> lMapInventoryAttach = new LinkedHashMap<String, String>();
      lMapInventoryAttach.put( "PARENT_SERIAL_NO_OEM", "'ACFT-" + lRandom + "'" );
      lMapInventoryAttach.put( "PARENT_PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventoryAttach.put( "PARENT_MANUFACT_CD", "'10001'" );
      lMapInventoryAttach.put( "ATTACH_SERIAL_NO_OEM", "'ASSY-" + lRandom + "'" );
      lMapInventoryAttach.put( "ATTACH_PART_NO_OEM", "'ENG_ASSY_PN1'" );
      lMapInventoryAttach.put( "ATTACH_MANUFACT_CD", "'ABC11'" );
      lMapInventoryAttach.put( "BOM_PART_CD", "'ENG-ASSY'" );
      lMapInventoryAttach.put( "EQP_POS_CD", "'1'" );
      lMapInventoryAttach.put( "INSTALL_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_ATTACH, lMapInventoryAttach ) );

      // =======C_RI_INVENTORY_USAGE=====================================================================
      // create inventory usage map
      Map<String, String> lInventoryUsage = new LinkedHashMap<String, String>();
      lInventoryUsage.put( "SERIAL_NO_OEM", "'ACFT-" + lRandom + "'" );
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

      // create inventory usage map
      Map<String, String> lInventoryAssyUsage = new LinkedHashMap<String, String>();
      lInventoryAssyUsage.put( "SERIAL_NO_OEM", "'ASSY-" + lRandom + "'" );
      lInventoryAssyUsage.put( "PART_NO_OEM", "'ENG_ASSY_PN1'" );
      lInventoryAssyUsage.put( "MANUFACT_CD", "'ABC11'" );
      lInventoryAssyUsage.put( "DATA_TYPE_CD", "'HOURS'" );
      lInventoryAssyUsage.put( "TSN_QT", "30" );
      lInventoryAssyUsage.put( "TSO_QT", "20" );
      lInventoryAssyUsage.put( "TSI_QT", "100" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventoryAssyUsage ) );

      // insert row for each usage parm
      lInventoryAssyUsage.put( "DATA_TYPE_CD", "'CYCLES'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventoryAssyUsage ) );
      lInventoryAssyUsage.put( "DATA_TYPE_CD", "'USAGE1'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventoryAssyUsage ) );
      lInventoryAssyUsage.put( "DATA_TYPE_CD", "'USAGE2'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventoryAssyUsage ) );
      lInventoryAssyUsage.put( "DATA_TYPE_CD", "'USAGE3'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventoryAssyUsage ) );
      lInventoryAssyUsage.put( "DATA_TYPE_CD", "'ECYC'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventoryAssyUsage ) );
      lInventoryAssyUsage.put( "DATA_TYPE_CD", "'EOT'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventoryAssyUsage ) );

      // ================================C_RI_SUB==============================================================
      // Add c_ri_sub table for trk of ACFT
      Map<String, String> lChild = new LinkedHashMap<String, String>();
      lChild.put( "PARENT_SERIAL_NO_OEM", "'ACFT-" + lRandom + "'" );
      lChild.put( "PARENT_PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lChild.put( "PARENT_MANUFACT_CD", "'10001'" );
      lChild.put( "BOM_PART_CD", "'ACFT-SYS-1-1-TRK-BATCH-PARENT'" );
      lChild.put( "EQP_POS_CD", "'1'" );
      lChild.put( "SERIAL_NO_OEM", "'TRK-ACFT-00001'" );
      lChild.put( "PART_NO_OEM", "'A0000010'" );
      lChild.put( "MANUFACT_CD", "'11111'" );
      lChild.put( "INV_CLASS_CD", "'TRK'" );
      lChild.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lChild.put( "INSTALL_DT", "to_date('01/01/2016','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lChild ) );

      // add c_ri_sub for trk of eng
      lChild.put( "PARENT_SERIAL_NO_OEM", "'ASSY-" + lRandom + "'" );
      lChild.put( "PARENT_PART_NO_OEM", "'ENG_ASSY_PN1'" );
      lChild.put( "PARENT_MANUFACT_CD", "'ABC11'" );
      lChild.put( "BOM_PART_CD", "'ENG-SYS-1-1-TRK-BATCH-PARENT'" );
      lChild.put( "EQP_POS_CD", "'1'" );
      lChild.put( "SERIAL_NO_OEM", "'TRK-ENG-00001'" );
      lChild.put( "PART_NO_OEM", "'E0000010'" );
      lChild.put( "MANUFACT_CD", "'11111'" );
      lChild.put( "INV_CLASS_CD", "'TRK'" );
      lChild.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lChild.put( "INSTALL_DT", "to_date('01/01/2016','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lChild ) );

      // ==================c_ri_inventory_usage=============================================================
      // create acft TRK usage
      Map<String, String> lInventoryACFTTRKUsage = new LinkedHashMap<String, String>();
      lInventoryAssyUsage.put( "SERIAL_NO_OEM", "'TRK-ACFT-00001'" );
      lInventoryAssyUsage.put( "PART_NO_OEM", "'A0000010'" );
      lInventoryAssyUsage.put( "MANUFACT_CD", "'11111'" );
      lInventoryAssyUsage.put( "DATA_TYPE_CD", "'CYCLES'" );
      lInventoryAssyUsage.put( "TSN_QT", "30" );
      lInventoryAssyUsage.put( "TSO_QT", "20" );
      lInventoryAssyUsage.put( "TSI_QT", "100" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventoryAssyUsage ) );

      // create eng TRK usage
      lInventoryACFTTRKUsage.clear();
      lInventoryAssyUsage.put( "SERIAL_NO_OEM", "'TRK-ENG-00001'" );
      lInventoryAssyUsage.put( "PART_NO_OEM", "'E0000010'" );
      lInventoryAssyUsage.put( "MANUFACT_CD", "'11111'" );
      lInventoryAssyUsage.put( "DATA_TYPE_CD", "'EOT'" );
      lInventoryAssyUsage.put( "TSN_QT", "30" );
      lInventoryAssyUsage.put( "TSO_QT", "20" );
      lInventoryAssyUsage.put( "TSI_QT", "100" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_USAGE,
            lInventoryAssyUsage ) );

      // call inventory validation & import
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "FULL", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      checkInventoryNoWarnings( "PASS" );

      // run import
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), false, true, "FULL", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // verify that import inventory was created as expected
      // checkInventoryDetails( lMapInventory );

      // verify inv_inv table
      iINV_Ids = getInvIds( "ACFT-" + lRandom );
      iINV_Ids_Assy = verifyINVINVtable( "ASSY-" + lRandom, "ASSY" );
      iINV_ACFT_TRK_Ids = getInvIds( "TRK-ACFT-00001" );
      iINV_ENG_TRK_Ids = getInvIds( "TRK-ENG-00001" );

      // get event id
      iEVENT_Ids_FG =
            getEventIds( iEVENT_TYPE_CD_FG, iEVENT_STATUS_CD_FGINST, iEVENT_SDESC_ASSY_FGINST );

      iEVENT_Ids_FG_ACFT_TRK =
            getEventIds( iEVENT_TYPE_CD_FG, iEVENT_STATUS_CD_FGINST, iEVENT_SDESC_ACFT_TRK_FGINST );

      iEVENT_Ids_FG_ENG_TRK =
            getEventIds( iEVENT_TYPE_CD_FG, iEVENT_STATUS_CD_FGINST, iEVENT_SDESC_ENG_TRK_FGINST );

      // verify evt_inv_usage table (not all usage will be verified)
      // Check ENG on cycles and EOT
      simpleIDs ldatatypeIds = getDataTypeIDs( "CYCLES", "Cycles", "CYCLES", "US" );
      verifyEvtInvUsage( iEVENT_Ids_FG, ldatatypeIds, "0", Integer.toString( 30 - 100 ),
            Integer.toString( 20 - 100 ), "0", "0", "0", "0" );

      ldatatypeIds = getDataTypeIDs( "EOT", "Engine Operating Time", "HOUR", "US" );
      verifyEvtInvUsage( iEVENT_Ids_FG, ldatatypeIds, "0", Integer.toString( 30 - 100 ),
            Integer.toString( 20 - 100 ), "0", "0", "0", "0" );

      // Check ACFT TRK on cycles
      ldatatypeIds = getDataTypeIDs( "CYCLES", "Cycles", "CYCLES", "US" );
      verifyEvtInvUsage( iEVENT_Ids_FG_ACFT_TRK, ldatatypeIds, "0", Integer.toString( 30 - 100 ),
            Integer.toString( 20 - 100 ), "0", "0", "0", "0" );

      // Check ENG TRK on EOT
      ldatatypeIds = getDataTypeIDs( "EOT", "Engine Operating Time", "HOUR", "US" );
      verifyEvtInvUsage( iEVENT_Ids_FG_ENG_TRK, ldatatypeIds, "0", Integer.toString( 30 - 100 ),
            Integer.toString( 20 - 100 ), "0", "0", "0", "0" );

   }


   /**
    * This test is to verify the import functionality is successful with warning messeges.
    *
    *
    *
    */
   @Test
   public void testImportsSuccessfullyWithWarning() throws Exception {

      // String lRandom = String.valueOf( getRandom() );

      String lRandom = "000001";

      // create inventory map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();
      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-" + lRandom + "'" );
      lMapInventory.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventory.put( "INV_CLASS_CD", "'ACFT'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'REPREQ'" );
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
      // This value has been changed to validate the upper case and trim change for OPER=21977
      lMapInventoryAssy.put( "MANUFACT_CD", "' abC11 '" );
      lMapInventoryAssy.put( "LOC_CD", "'OPS'" );
      lMapInventoryAssy.put( "INV_COND_CD", "'INSRV'" );
      lMapInventoryAssy.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventoryAssy ) );

      // create inventory attach map
      Map<String, String> lMapInventoryAttach = new LinkedHashMap<String, String>();
      lMapInventoryAttach.put( "PARENT_SERIAL_NO_OEM", "'ACFT-" + lRandom + "'" );
      lMapInventoryAttach.put( "PARENT_PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventoryAttach.put( "PARENT_MANUFACT_CD", "'10001'" );
      lMapInventoryAttach.put( "ATTACH_SERIAL_NO_OEM", "'ASSY-" + lRandom + "'" );
      lMapInventoryAttach.put( "ATTACH_PART_NO_OEM", "'ENG_ASSY_PN1'" );
      lMapInventoryAttach.put( "ATTACH_MANUFACT_CD", "'ABC11'" );
      lMapInventoryAttach.put( "BOM_PART_CD", "'ENG-ASSY'" );
      lMapInventoryAttach.put( "EQP_POS_CD", "'1'" );
      lMapInventoryAttach.put( "INSTALL_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_ATTACH, lMapInventoryAttach ) );

      // call validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // Check there is usage waring
      checkInventoryErrorCode( testName.getMethodName(), "USG-00001" );

      // call import
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), false, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // verify the importing is successful.
      iINV_Ids = verifyINVINVtable( "ACFT-" + lRandom, "ACFT" );
      verifyINVINVtable( "ASSY-" + lRandom, "ASSY" );

      iINV_Ids_Assy = verifyINVINVtable( "ASSY-" + lRandom, "ASSY" );
      // get event id
      iEVENT_Ids_FG =
            getEventIds( iEVENT_TYPE_CD_FG, iEVENT_STATUS_CD_FGINST, iEVENT_SDESC_ASSY_FGINST );

   }


   /**
    * This test is to verify the fix of 26184:Actuals Loader allows non-batch inventory to be loaded
    * with a bin_qt > 1
    *
    *
    *
    */
   @Test
   public void testOPER26184_TRK() throws Exception {

      String lRandom = "100";

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'TRK-" + lRandom + "'" );
      lMapInventory.put( "PART_NO_OEM", "'A0000010'" );
      lMapInventory.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventory.put( "MANUFACT_CD", "'11111'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'RFI'" );
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "BIN_QT", "'5'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // run validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // call import
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), false, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // verify the importing is successful.
      iINV_Ids = verifyINVINVtable( "TRK-" + lRandom, "TRK" );
      verifyINVINVtableBINQT( "TRK-" + lRandom, "TRK", null );
   }


   /**
    * This test is to verify the fix of 26184:Actuals Loader allows non-batch inventory to be loaded
    * with a bin_qt > 1
    *
    *
    *
    */
   @Test
   public void testOPER26184_BATCH() throws Exception {

      String lRandom = "100";

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'BATCH-" + lRandom + "'" );
      lMapInventory.put( "PART_NO_OEM", "'A0000009'" );
      lMapInventory.put( "INV_CLASS_CD", "'BATCH'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'RFI'" );
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "BIN_QT", "'5'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lMapInventory.put( "OWNER_CD", "'ALT'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // run validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // call import
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), false, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      // verify the importing is successful.
      iINV_Ids = verifyINVINVtable( "BATCH-" + lRandom, "BATCH" );
      verifyINVINVtableBINQT( "BATCH-" + lRandom, "BATCH", "5" );
   }


   /**
    * This test is to verify :ACFT with carrier_cd=MXI, ENG1 is empty, ENG2=ATLD. Expected result:
    * ENG will inheritant as MXI, ENG2 is ATLD.
    *
    *
    */

   public void testOPER_16025_VALIDATION() throws Exception {

      // create inventory map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-00001'" );
      lMapInventory.put( "PART_NO_OEM", "'ACFT_ASSY_PN8'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "AC_REG_CD", "'AC-TEST'" );
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "CARRIER_CD", "'MXI'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // insert eng 1
      lMapInventory.clear();
      lMapInventory.put( "SERIAL_NO_OEM", "'ASSY-00001'" );
      lMapInventory.put( "PART_NO_OEM", "'ENG_ASSY_PN8'" );
      lMapInventory.put( "MANUFACT_CD", "'ABC11'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "CARRIER_CD", null );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      // lMapInventory.put( "INV_COND_CD", "'INSRV'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // insert eng 2
      lMapInventory.clear();
      lMapInventory.put( "SERIAL_NO_OEM", "'ASSY-00002'" );
      lMapInventory.put( "PART_NO_OEM", "'ENG_ASSY_PN8'" );
      lMapInventory.put( "MANUFACT_CD", "'ABC11'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "CARRIER_CD", "'ATLD'" );
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

      // import eng 2 attached
      lMapInventoryAttach.clear();
      lMapInventoryAttach.put( "PARENT_SERIAL_NO_OEM", "'ACFT-00001'" );
      lMapInventoryAttach.put( "PARENT_PART_NO_OEM", "'ACFT_ASSY_PN8'" );
      lMapInventoryAttach.put( "PARENT_MANUFACT_CD", "'10001'" );
      lMapInventoryAttach.put( "ATTACH_SERIAL_NO_OEM", "'ASSY-00002'" );
      lMapInventoryAttach.put( "ATTACH_PART_NO_OEM", "'ENG_ASSY_PN8'" );
      lMapInventoryAttach.put( "ATTACH_MANUFACT_CD", "'ABC11'" );
      lMapInventoryAttach.put( "BOM_PART_CD", "'ENG-ASSY'" );
      lMapInventoryAttach.put( "EQP_POS_CD", "'2'" );
      lMapInventoryAttach.put( "INSTALL_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_ATTACH, lMapInventoryAttach ) );

      // call inventory validation & import
      runValidateInv( "FULL" );
      checkInventoryNoWarnings( "PASS" );

   }


   /**
    * This test is to verify :complete ACFT with default cond_cd = INSRV
    *
    *
    */
   @Test
   public void testOPER_16025_IMPORT() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testOPER_16025_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runIMPORTInv( "FULL" );

      // Get iINV_Ids of ACFT
      simpleIDs lMXIcarrierIDs = getCarrierIds( "MXI" );
      simpleIDs lATLDcarrierIDs = getCarrierIds( "ATLD" );

      iINV_Ids = verifyINVINVtable_CARRIER( "ACFT-00001", "ACFT", lMXIcarrierIDs );
      // Get iINV_Ids of eng 1
      iINV_Ids_Assy = verifyINVINVtable_CARRIER( "ASSY-00001", "ASSY", lMXIcarrierIDs );
      // Get iINV_Ids of eng 2
      iINV_Ids_Assy_2 = verifyINVINVtable_CARRIER( "ASSY-00002", "ASSY", lATLDcarrierIDs );

      // get event id
      iEVENT_Ids_FG =
            getEventIds( iEVENT_TYPE_CD_FG, iEVENT_STATUS_CD_FGINST, iEVENT_SDESC_ASSY_FGINST_2 );

      iEVENT_Ids_FG_2 =
            getEventIds( iEVENT_TYPE_CD_FG, iEVENT_STATUS_CD_FGINST, iEVENT_SDESC_ASSY_FGINST_3 );

   }


   /**
    * This test is to verify the fix of 17471:Actuals Loader validation failing if TRK or ASSY part
    * groups are empty.
    *
    *
    *
    */
   @Test
   public void testOPER17471() throws Exception {

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

      // run validation
      runValidateInv( "FULL" );
      checkInventoryNoWarnings( "PASS" );

      // run import
      runIMPORTInv( "FULL" );

      // verify the importing is successful.
      iINV_Ids = verifyINVINVtable( "ACFT-00001", "ACFT" );
      verifyINVINVtableSYS( iINV_Ids, "SYS-1" );
      verifyINVINVtableSYS( iINV_Ids, "SYS-1-ENG" );
      verifyINVINVtableSYS( iINV_Ids, "SYS-2" );

      // verify parts which are should not be imported
      verifyINVINVtableNonExistParts( iINV_Ids, "ACFT-SYS-1-1-TRK-BATCH-PARENT" );
      verifyINVINVtableNonExistParts( iINV_Ids, "ACFT-SYS-1-TRK-1" );
      verifyINVINVtableNonExistParts( iINV_Ids, "ACFT-SYS-2-CHILD-TRK-1" );
      verifyINVINVtableNonExistParts( iINV_Ids, "ACFT-SYS-2-CHILD-TRK-2" );

   }


   // ======================================================================================================================
   /**
    * This function is to verify inv_inv table.
    *
    *
    */
   @Override
   public simpleIDs verifyINVINVtable( String aSERIAL_NO_OEM, String aINV_CLASS_CD ) {
      // inv_inv table
      String[] iIds = { "INV_NO_DB_ID", "INV_NO_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "SERIAL_NO_OEM", aSERIAL_NO_OEM );
      lArgs.addArguments( "INV_CLASS_CD", aINV_CLASS_CD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.INV_INV, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lIds;

   }


   /**
    * This function is to verify inv_inv table on BIN_QT value
    *
    *
    */
   public simpleIDs verifyINVINVtableBINQT( String aSERIAL_NO_OEM, String aINV_CLASS_CD,
         String aBIN_QT ) {
      // inv_inv table
      String[] iIds = { "INV_NO_DB_ID", "INV_NO_ID", "BIN_QT" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "SERIAL_NO_OEM", aSERIAL_NO_OEM );
      lArgs.addArguments( "INV_CLASS_CD", aINV_CLASS_CD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.INV_INV, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      if ( aBIN_QT != null ) {
         assertThat( "BIN_QT", llists.get( 0 ).get( 2 ).equalsIgnoreCase( aBIN_QT ) );
      } else {
         assertThat( "BIN_QT", StringUtils.isBlank( llists.get( 0 ).get( 2 ) ) );
      }

      return lIds;

   }


   /**
    * This function is to verify inv_inv table on SYS SLOT
    *
    *
    */
   public simpleIDs verifyINVINVtableSYS( simpleIDs aH_INV_IDS, String aCONFIG_POS_SDESC ) {
      // inv_inv table
      String[] iIds = { "INV_NO_DB_ID", "INV_NO_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "H_INV_NO_DB_ID", aH_INV_IDS.getNO_DB_ID() );
      lArgs.addArguments( "H_INV_NO_ID", aH_INV_IDS.getNO_ID() );
      lArgs.addArguments( "CONFIG_POS_SDESC", aCONFIG_POS_SDESC );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.INV_INV, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );
      Assert.assertTrue( "INV_NO_IDs should not be empty", lIds != null );

      return lIds;

   }


   /**
    * This function is to verify inv_inv table on missing parts
    *
    *
    */
   public void verifyINVINVtableNonExistParts( simpleIDs aH_INV_IDS, String aCONFIG_POS_SDESC ) {
      // inv_inv table
      String[] iIds = { "INV_NO_DB_ID", "INV_NO_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "H_INV_NO_DB_ID", aH_INV_IDS.getNO_DB_ID() );
      lArgs.addArguments( "H_INV_NO_ID", aH_INV_IDS.getNO_ID() );
      lArgs.addArguments( "CONFIG_POS_SDESC", aCONFIG_POS_SDESC );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.INV_INV, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      Assert.assertTrue( "INV_NO_IDs should not be empty", llists.size() == 0 );

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
    * This function is to retrieve event ids.
    *
    *
    */
   public simpleIDs getEventIds( String aEVENT_TYPE_CD, String aEVENT_STATUS_CD,
         String aEVENT_SDESC ) {

      // REF_TASK_CLASS table
      String[] iIds = { "EVENT_DB_ID", "EVENT_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "EVENT_TYPE_CD", aEVENT_TYPE_CD );
      lArgs.addArguments( "EVENT_STATUS_CD", aEVENT_STATUS_CD );
      lArgs.addArguments( "EVENT_SDESC", aEVENT_SDESC );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EVT_EVENT, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lIds;

   }


   /**
    * This function is to verify evt_inv_usage table;
    *
    *
    */

   public void verifyEvtInvUsage( simpleIDs aEVENTIDs, simpleIDs aDataTypeIds, String aTSI_QT,
         String aTSN_QT, String aTSO_QT, String aAssmbl_TSN_QT, String aASSMBL_TSO_QT,
         String aH_TSN_QT, String aH_TSO_QT ) {

      String[] iIds = { "TSI_QT", "TSN_QT", "TSO_QT", "ASSMBL_TSN_QT", "ASSMBL_TSO_QT", "H_TSN_QT",
            "H_TSO_QT" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "EVENT_DB_ID", aEVENTIDs.getNO_DB_ID() );
      lArgs.addArguments( "EVENT_ID", aEVENTIDs.getNO_ID() );
      lArgs.addArguments( "DATA_TYPE_DB_ID", aDataTypeIds.getNO_DB_ID() );
      lArgs.addArguments( "DATA_TYPE_ID", aDataTypeIds.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EVT_INV_USAGE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "TSI_QT", llists.get( 0 ).get( 0 ).equalsIgnoreCase( aTSI_QT ) );
      Assert.assertTrue( "TSN_QT", llists.get( 0 ).get( 1 ).equalsIgnoreCase( aTSN_QT ) );
      Assert.assertTrue( "TSO_QT", llists.get( 0 ).get( 2 ).equalsIgnoreCase( aTSO_QT ) );
      Assert.assertTrue( "ASSMBL_TSN_QT",
            llists.get( 0 ).get( 3 ).equalsIgnoreCase( aAssmbl_TSN_QT ) );
      Assert.assertTrue( "ASSMBL_TSO_QT",
            llists.get( 0 ).get( 4 ).equalsIgnoreCase( aASSMBL_TSO_QT ) );
      Assert.assertTrue( "H_TSN_QT", llists.get( 0 ).get( 5 ).equalsIgnoreCase( aH_TSN_QT ) );
      Assert.assertTrue( "H_TSO_QT", llists.get( 0 ).get( 6 ).equalsIgnoreCase( aH_TSO_QT ) );

   }


   /**
    * This function is data setup on part for OPER-17471
    *
    *
    */
   public void setPart() {
      // get next seq #
      iBOMPartID_next = getNextValueInSequence( "bom_part_id_seq" );

      // get original part_no_id
      String lquery = "SELECT EQP_PART_BASELINE.bom_part_id FROM EQP_PART_NO "
            + "INNER JOIN   EQP_PART_BASELINE ON "
            + "eqp_part_no.part_no_db_id=EQP_PART_BASELINE.part_no_db_id AND "
            + "eqp_part_no.part_no_id=EQP_PART_BASELINE.part_no_id "
            + "WHERE EQP_PART_NO.Part_No_Oem='A0000220' AND EQP_PART_NO.manufact_cd='11111'";
      iBOMParID_Origin = getStringValueFromQuery( lquery, "BOM_PART_ID" );

      lquery = "UPDATE EQP_PART_BASELINE SET BOM_PART_ID='" + iBOMPartID_next
            + "' WHERE bom_part_id='" + iBOMParID_Origin + "'";

      runUpdate( lquery );

   }


   /**
    * This function is resume data setup on part for OPER-17471
    *
    *
    */
   public void restorePart() {

      String lquery = "UPDATE EQP_PART_BASELINE SET BOM_PART_ID='" + iBOMParID_Origin
            + "' WHERE bom_part_id='" + iBOMPartID_next + "'";

      runUpdate( lquery );

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

         lStrDelete = "delete from " + TableUtil.INV_INV + " where H_INV_NO_DB_ID='"
               + iINV_Ids.getNO_DB_ID() + "' and H_INV_NO_ID='" + iINV_Ids.getNO_ID() + "'";
         executeSQL( lStrDelete );

         lStrDelete = "delete from " + TableUtil.INV_AC_REG + " where INV_NO_DB_ID='"
               + iINV_Ids.getNO_DB_ID() + "' and INV_NO_ID='" + iINV_Ids.getNO_ID() + "'";
         executeSQL( lStrDelete );

      }

      if ( iINV_Ids_Assy != null ) {

         lStrDelete = "delete from " + TableUtil.ACFT_CAP_LEVELS + " where ACFT_NO_DB_ID='"
               + iINV_Ids_Assy.getNO_DB_ID() + "' and ACFT_NO_ID='" + iINV_Ids_Assy.getNO_ID()
               + "'";
         executeSQL( lStrDelete );

      }

      if ( iINV_Ids_Assy_2 != null ) {

         lStrDelete = "delete from " + TableUtil.ACFT_CAP_LEVELS + " where ACFT_NO_DB_ID='"
               + iINV_Ids_Assy_2.getNO_DB_ID() + "' and ACFT_NO_ID='" + iINV_Ids_Assy_2.getNO_ID()
               + "'";
         executeSQL( lStrDelete );

      }

      clearEvent( iEVENT_Ids_FG );
      clearEvent( iEVENT_Ids_FG_2 );
      clearEvent( iEVENT_Ids_FG_ACFT_TRK );
      clearEvent( iEVENT_Ids_FG_ENG_TRK );

   }


   /**
    * This function is to verify inv_inv table.
    *
    *
    */
   public simpleIDs verifyINVINVtable_CARRIER( String aSERIAL_NO_OEM, String aINV_CLASS_CD,
         simpleIDs aCarrierID ) {
      // inv_inv table
      String[] iIds = { "INV_NO_DB_ID", "INV_NO_ID", "CARRIER_DB_ID", "CARRIER_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "SERIAL_NO_OEM", aSERIAL_NO_OEM );
      lArgs.addArguments( "INV_CLASS_CD", aINV_CLASS_CD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.INV_INV, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      // check carrrier
      Assert.assertTrue( "CARRIER_DB_ID:",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aCarrierID.getNO_DB_ID() ) );
      Assert.assertTrue( "CARRIER_DB_ID:",
            llists.get( 0 ).get( 3 ).equalsIgnoreCase( aCarrierID.getNO_ID() ) );

      return lIds;

   }

}
