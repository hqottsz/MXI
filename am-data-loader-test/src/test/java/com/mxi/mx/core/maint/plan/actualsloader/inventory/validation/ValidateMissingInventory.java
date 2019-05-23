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
 * This test suite contains test cases on validation on missing inventory.
 *
 * @author ALICIA QIAN
 */
public class ValidateMissingInventory extends ActualsLoaderTest {

   @Rule
   public TestName testName = new TestName();


   /**
    * Clean up after each individual test
    *
    * @throws Exception
    */
   @After
   @Override
   public void after() throws Exception {

      String strTCName = testName.getMethodName();
      if ( strTCName.contains( "CHILDNONMANDITORY" ) ) {
         RestoredataSetupCHILD();
      } else if ( strTCName.contains( "PARENTNONMANDITORY" ) ) {
         RestoredataSetupParent();
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
      if ( strTCName.contains( "CHILDNONMANDITORY" ) ) {
         dataSetupCHILD();
      } else if ( strTCName.contains( "PARENTNONMANDITORY" ) ) {
         dataSetupParent();
      }
   }


   /**
    * This test is to verify OPER17973:Missing inventories are generated when inventories of TRK(s)
    * are not listed in c_ri_sub_inventory table.
    *
    *
    */
   @Test
   public void testOPER_17973_MISSINGTRK_FULL_MANDITORY() throws Exception {

      // String lRandom = String.valueOf( getRandom() );
      String lRandom = "MISSINGTRK";

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
      lMapInventoryAssy.put( "MANUFACT_CD", "' ABC11 '" );
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

      // call inventory validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "FULL", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      checkInventoryNoWarnings( "PASS" );

      // Check trk of ACFT is not in missing inventory table
      String lQuery = "select 1 from " + TableUtil.AL_MISSING_INVENTORY
            + " where part_no_oem = 'A0000010' OR config_slot = 'ACFT-SYS-1-1-TRK-BATCH-PARENT' ";
      Assert.assertFalse( "Check AL_MISSING_INVENTORY table to verify the record not exists",
            RecordsExist( lQuery ) );

      // Check trk of ENG is not in missing inventory table
      lQuery = "select 1 from " + TableUtil.AL_MISSING_INVENTORY
            + " where part_no_oem = 'E0000010' OR config_slot = 'ENG-SYS-1-1-TRK-BATCH-PARENT'";
      Assert.assertFalse( "Check AL_MISSING_INVENTORY table to verify the record not exists",
            RecordsExist( lQuery ) );

      // Check other trk(s) are in the missing inventory table
      lQuery = "select 1 from " + TableUtil.AL_MISSING_INVENTORY
            + " WHERE part_no_oem = 'A0000001' OR config_slot = 'ACFT-SYS-1-1-TRK-P1'";
      Assert.assertTrue( "Check AL_MISSING_INVENTORY table to verify the record not exists",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.AL_MISSING_INVENTORY
            + " WHERE part_no_oem = 'E0000001' OR config_slot = 'ENG-SYS-1-1-TRK-P1'";
      Assert.assertTrue( "Check AL_MISSING_INVENTORY table to verify the record not exists",
            RecordsExist( lQuery ) );

      // check ACFT-SYS-1-1-TRK-BATCH-PARENT auto creation is 0
      lQuery =
            "SELECT AUTO_CREATE  FROM AL_TEMP_INVENTORY_SUB WHERE (assmbl_db_id, assmbl_cd, assmbl_bom_id) IN "
                  + "(SELECT assmbl_db_id, assmbl_cd, assmbl_bom_id FROM eqp_assmbl_bom WHERE assmbl_cd='ACFT_CD1' AND assmbl_bom_cd='ACFT-SYS-1-1-TRK-BATCH-PARENT')";
      Assert.assertTrue( "Check auto creation is 0.",
            getIntValueFromQuery( lQuery, "AUTO_CREATE" ) == 0 );

      // check ENG-SYS-1-1-TRK-BATCH-PARENT auto creation is 0
      lQuery =
            "SELECT AUTO_CREATE  FROM AL_TEMP_INVENTORY_SUB WHERE (assmbl_db_id, assmbl_cd, assmbl_bom_id) IN "
                  + "(SELECT assmbl_db_id, assmbl_cd, assmbl_bom_id FROM eqp_assmbl_bom WHERE assmbl_cd='ENG_CD1' AND assmbl_bom_cd='ENG-SYS-1-1-TRK-BATCH-PARENT')";
      Assert.assertTrue( "Check auto creation is 0.",
            getIntValueFromQuery( lQuery, "AUTO_CREATE" ) == 0 );

      // check ACFT-SYS-1-1-TRK-P1 auto creation is 1
      lQuery =
            "SELECT AUTO_CREATE  FROM AL_TEMP_INVENTORY_SUB WHERE (assmbl_db_id, assmbl_cd, assmbl_bom_id) IN "
                  + "(SELECT assmbl_db_id, assmbl_cd, assmbl_bom_id FROM eqp_assmbl_bom WHERE assmbl_cd='ACFT_CD1' AND assmbl_bom_cd='ACFT-SYS-1-1-TRK-P1')";
      Assert.assertTrue( "Check auto creation is 1.",
            getIntValueFromQuery( lQuery, "AUTO_CREATE" ) == 1 );

      // check ENG-SYS-1-1-TRK-P1 auto creation is 1
      lQuery =
            "SELECT AUTO_CREATE  FROM AL_TEMP_INVENTORY_SUB WHERE (assmbl_db_id, assmbl_cd, assmbl_bom_id) IN "
                  + "(SELECT assmbl_db_id, assmbl_cd, assmbl_bom_id FROM eqp_assmbl_bom WHERE assmbl_cd='ENG_CD1' AND assmbl_bom_cd='ENG-SYS-1-1-TRK-P1')";
      Assert.assertTrue( "Check auto creation is 1.",
            getIntValueFromQuery( lQuery, "AUTO_CREATE" ) == 1 );

   }


   /**
    * This test is to verify OPER17973:Missing inventories are not generated when inventories of
    * TRK(s) are not listed in c_ri_sub_inventory table.
    *
    *
    */

   @Test
   public void testOPER_17973_MISSINGTRK_NOTFULL_MANDITORY() throws Exception {

      // String lRandom = String.valueOf( getRandom() );
      String lRandom = "MISSINGTRK";

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
      lMapInventoryAssy.put( "MANUFACT_CD", "' ABC11 '" );
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

      // call inventory validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      checkInventoryNoWarnings( "PASS" );

      // Check trk of ACFT is not in missing inventory table
      // String lQuery = "select 1 from " + TableUtil.AL_MISSING_INVENTORY;
      // Assert.assertFalse( "Check AL_MISSING_INVENTORY table to verify the record not exists",
      // RecordsExist( lQuery ) );

      // Check trk of ACFT is not in missing inventory table
      String lQuery = "select 1 from " + TableUtil.AL_MISSING_INVENTORY
            + " where part_no_oem = 'A0000010' OR config_slot = 'ACFT-SYS-1-1-TRK-BATCH-PARENT' ";
      Assert.assertFalse( "Check AL_MISSING_INVENTORY table to verify the record not exists",
            RecordsExist( lQuery ) );

      // Check trk of ENG is not in missing inventory table
      lQuery = "select 1 from " + TableUtil.AL_MISSING_INVENTORY
            + " where part_no_oem = 'E0000010' OR config_slot = 'ENG-SYS-1-1-TRK-BATCH-PARENT'";
      Assert.assertFalse( "Check AL_MISSING_INVENTORY table to verify the record not exists",
            RecordsExist( lQuery ) );

      // Check other trk(s) are in the missing inventory table
      lQuery = "select 1 from " + TableUtil.AL_MISSING_INVENTORY
            + " WHERE part_no_oem = 'A0000001' OR config_slot = 'ACFT-SYS-1-1-TRK-P1'";
      Assert.assertTrue( "Check AL_MISSING_INVENTORY table to verify the record not exists",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.AL_MISSING_INVENTORY
            + " WHERE part_no_oem = 'E0000001' OR config_slot = 'ENG-SYS-1-1-TRK-P1'";
      Assert.assertTrue( "Check AL_MISSING_INVENTORY table to verify the record not exists",
            RecordsExist( lQuery ) );

      // check ACFT-SYS-1-1-TRK-BATCH-PARENT auto creation is 0
      lQuery =
            "SELECT AUTO_CREATE  FROM AL_TEMP_INVENTORY_SUB WHERE (assmbl_db_id, assmbl_cd, assmbl_bom_id) IN "
                  + "(SELECT assmbl_db_id, assmbl_cd, assmbl_bom_id FROM eqp_assmbl_bom WHERE assmbl_cd='ACFT_CD1' AND assmbl_bom_cd='ACFT-SYS-1-1-TRK-BATCH-PARENT')";
      Assert.assertTrue( "Check auto creation is 0.",
            getIntValueFromQuery( lQuery, "AUTO_CREATE" ) == 0 );

      // check ENG-SYS-1-1-TRK-BATCH-PARENT auto creation is 0
      lQuery =
            "SELECT AUTO_CREATE  FROM AL_TEMP_INVENTORY_SUB WHERE (assmbl_db_id, assmbl_cd, assmbl_bom_id) IN "
                  + "(SELECT assmbl_db_id, assmbl_cd, assmbl_bom_id FROM eqp_assmbl_bom WHERE assmbl_cd='ENG_CD1' AND assmbl_bom_cd='ENG-SYS-1-1-TRK-BATCH-PARENT')";
      Assert.assertTrue( "Check auto creation is 0.",
            getIntValueFromQuery( lQuery, "AUTO_CREATE" ) == 0 );

      // check ACFT-SYS-1-1-TRK-P1 auto creation is 1
      lQuery =
            "SELECT AUTO_CREATE  FROM AL_TEMP_INVENTORY_SUB WHERE (assmbl_db_id, assmbl_cd, assmbl_bom_id) IN "
                  + "(SELECT assmbl_db_id, assmbl_cd, assmbl_bom_id FROM eqp_assmbl_bom WHERE assmbl_cd='ACFT_CD1' AND assmbl_bom_cd='ACFT-SYS-1-1-TRK-P1')";
      Assert.assertTrue( "Check auto creation is -1.",
            getIntValueFromQuery( lQuery, "AUTO_CREATE" ) == 1 );

      // check ENG-SYS-1-1-TRK-P1 auto creation is 1
      lQuery =
            "SELECT AUTO_CREATE  FROM AL_TEMP_INVENTORY_SUB WHERE (assmbl_db_id, assmbl_cd, assmbl_bom_id) IN "
                  + "(SELECT assmbl_db_id, assmbl_cd, assmbl_bom_id FROM eqp_assmbl_bom WHERE assmbl_cd='ENG_CD1' AND assmbl_bom_cd='ENG-SYS-1-1-TRK-P1')";
      Assert.assertTrue( "Check auto creation is 1.",
            getIntValueFromQuery( lQuery, "AUTO_CREATE" ) == 1 );

   }


   /**
    * This test is to verify OPER17973:Missing inventories are not generated when inventories of
    * TRK(s) are not mandatory field.
    *
    *
    */

   @Test
   public void testOPER_17973_MISSINGTRK_FULL_CHILDNONMANDITORY() throws Exception {

      // String lRandom = String.valueOf( getRandom() );
      String lRandom = "MISSINGTRK";

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
      lMapInventoryAssy.put( "MANUFACT_CD", "' ABC11 '" );
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

      // call inventory validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "FULL", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      checkInventoryNoWarnings( "PASS" );

      // Check trk of ACFT is not in missing inventory table
      String lQuery = "select 1 from " + TableUtil.AL_MISSING_INVENTORY
            + " where part_no_oem = 'A0000022' OR config_slot = 'ACFT-SYS-1-1-TRK-TRK-TRK-CHILD' ";
      Assert.assertFalse( "Check AL_MISSING_INVENTORY table to verify the record not exists",
            RecordsExist( lQuery ) );

      // check ACFT-SYS-1-1-TRK-BATCH-PARENT auto creation is 0
      lQuery =
            "SELECT AUTO_CREATE  FROM AL_TEMP_INVENTORY_SUB WHERE (assmbl_db_id, assmbl_cd, assmbl_bom_id) IN "
                  + "(SELECT assmbl_db_id, assmbl_cd, assmbl_bom_id FROM eqp_assmbl_bom WHERE assmbl_cd='ACFT_CD1' AND assmbl_bom_cd='ACFT-SYS-1-1-TRK-TRK-TRK-CHILD')";
      Assert.assertTrue( "Check auto creation is 0.",
            getIntValueFromQuery( lQuery, "AUTO_CREATE" ) == 0 );

   }


   /**
    * This test is to verify OPER17973:Missing inventories are not generated when inventories of
    * TRK(s) are not mandatory field.
    *
    *
    */

   @Test
   public void testOPER_17973_MISSINGTRK_FULL_PARENTNONMANDITORY() throws Exception {

      // String lRandom = String.valueOf( getRandom() );
      String lRandom = "MISSINGTRK";

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

      // call inventory validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "FULL", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      checkInventoryNoWarnings( "PASS" );

      // Check trks with non mandatory of ACFT is not in missing inventory table
      String lQuery = "select 1 from " + TableUtil.AL_MISSING_INVENTORY
            + " where part_no_oem = 'A0000022' OR config_slot = 'ACFT-SYS-1-1-TRK-TRK-TRK-CHILD' ";
      Assert.assertFalse( "Check AL_MISSING_INVENTORY table to verify the record not exists",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.AL_MISSING_INVENTORY
            + " where part_no_oem = 'A0000021' OR config_slot = 'ACFT-SYS-1-1-TRK-TRK-TRK-MID' ";
      Assert.assertFalse( "Check AL_MISSING_INVENTORY table to verify the record not exists",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.AL_MISSING_INVENTORY
            + " where part_no_oem = 'A0000020' OR config_slot = 'ACFT-SYS-1-1-TRK-TRK-TRK-PARENT' ";
      Assert.assertFalse( "Check AL_MISSING_INVENTORY table to verify the record not exists",
            RecordsExist( lQuery ) );

      // check ACFT-SYS-1-1-TRK-TRK-TRK-CHILD auto creation is 0
      lQuery =
            "SELECT AUTO_CREATE  FROM AL_TEMP_INVENTORY_SUB WHERE (assmbl_db_id, assmbl_cd, assmbl_bom_id) IN "
                  + "(SELECT assmbl_db_id, assmbl_cd, assmbl_bom_id FROM eqp_assmbl_bom WHERE assmbl_cd='ACFT_CD1' AND assmbl_bom_cd='ACFT-SYS-1-1-TRK-TRK-TRK-CHILD')";
      Assert.assertTrue( "Check auto creation is 0.",
            getIntValueFromQuery( lQuery, "AUTO_CREATE" ) == 0 );

      // check ACFT-SYS-1-1-TRK-TRK-TRK-MID auto creation is 0
      lQuery =
            "SELECT AUTO_CREATE  FROM AL_TEMP_INVENTORY_SUB WHERE (assmbl_db_id, assmbl_cd, assmbl_bom_id) IN "
                  + "(SELECT assmbl_db_id, assmbl_cd, assmbl_bom_id FROM eqp_assmbl_bom WHERE assmbl_cd='ACFT_CD1' AND assmbl_bom_cd='ACFT-SYS-1-1-TRK-TRK-TRK-MID')";
      Assert.assertTrue( "Check auto creation is 0.",
            getIntValueFromQuery( lQuery, "AUTO_CREATE" ) == 0 );

      // check ACFT-SYS-1-1-TRK-TRK-TRK-PARENT auto creation is 0
      lQuery =
            "SELECT AUTO_CREATE  FROM AL_TEMP_INVENTORY_SUB WHERE (assmbl_db_id, assmbl_cd, assmbl_bom_id) IN "
                  + "(SELECT assmbl_db_id, assmbl_cd, assmbl_bom_id FROM eqp_assmbl_bom WHERE assmbl_cd='ACFT_CD1' AND assmbl_bom_cd='ACFT-SYS-1-1-TRK-TRK-TRK-PARENT')";
      Assert.assertTrue( "Check auto creation is 0.",
            getIntValueFromQuery( lQuery, "AUTO_CREATE" ) == 0 );

   }


   /**
    * This test is to verify OPER17973:Loose inventories is generated in missing inventory table if
    * assmbly information is provided.
    *
    *
    */

   @Test
   public void testOPER_17973_MISSINGTRK_FULL_LOOSEY_MANDITORY() throws Exception {

      // String lRandom = String.valueOf( getRandom() );
      String lRandom = "MISSINGTRK";

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

      // loose TRK
      lMapInventory.clear();
      lMapInventory.put( "SERIAL_NO_OEM", "'TRK-" + lRandom + "'" );
      lMapInventory.put( "PART_NO_OEM", "'A0000020'" );
      lMapInventory.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'RFI'" );
      lMapInventory.put( "ASSMBL_CD", "'ACFT_CD1'" );
      lMapInventory.put( "ASSMBL_BOM_CD", "'ACFT-SYS-1-1-TRK-TRK-TRK-PARENT'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // call inventory validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "FULL", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      checkInventoryNoWarnings( "PASS" );

      // Check trk of ACFT is in missing inventory table
      String lQuery = "select 1 from " + TableUtil.AL_MISSING_INVENTORY
            + " where part_no_oem = 'A0000020' and config_slot = 'ACFT-SYS-1-1-TRK-TRK-TRK-PARENT' and parent_serial_no_oem ='ACFT-MISSINGTRK' ";
      Assert.assertTrue( "Check AL_MISSING_INVENTORY table to verify the record exists",
            RecordsExist( lQuery ) );

      // Check loose part is listed in missing inventory table if assemble information is provided.
      lQuery = "select 1 from " + TableUtil.AL_MISSING_INVENTORY
            + " where parent_part_no_oem = 'A0000020' and config_slot = 'ACFT-SYS-1-1-TRK-TRK-TRK-CHILD'";
      Assert.assertTrue(
            "Check AL_MISSING_INVENTORY table to verify the record not exists for loose TRK.",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.AL_MISSING_INVENTORY
            + " where parent_part_no_oem = 'A0000020' and config_slot = 'ACFT-SYS-1-1-TRK-TRK-TRK-MID'";
      Assert.assertTrue(
            "Check AL_MISSING_INVENTORY table to verify the record not exists for loose TRK.",
            RecordsExist( lQuery ) );

      // check ACFT-SYS-1-1-TRK-BATCH-PARENT auto creation is 1
      lQuery =
            "SELECT AUTO_CREATE  FROM AL_TEMP_INVENTORY_SUB WHERE (assmbl_db_id, assmbl_cd, assmbl_bom_id) IN "
                  + "(SELECT assmbl_db_id, assmbl_cd, assmbl_bom_id FROM eqp_assmbl_bom WHERE assmbl_cd='ACFT_CD1' AND assmbl_bom_cd='ACFT-SYS-1-1-TRK-TRK-TRK-PARENT')";
      Assert.assertTrue( "Check auto creation is 1.",
            getIntValueFromQuery( lQuery, "AUTO_CREATE" ) == 1 );

      // check ACFT-SYS-1-1-TRK-TRK-TRK-MID auto creation is 1 and count must be 1
      lQuery =
            "SELECT count(*) FROM AL_TEMP_INVENTORY_SUB WHERE (assmbl_db_id, assmbl_cd, assmbl_bom_id) IN "
                  + "(SELECT assmbl_db_id, assmbl_cd, assmbl_bom_id FROM eqp_assmbl_bom WHERE assmbl_cd='ACFT_CD1' AND assmbl_bom_cd='ACFT-SYS-1-1-TRK-TRK-TRK-MID')";
      Assert.assertTrue( "Check count  is 1.", getIntValueFromQuery( lQuery, "COUNT(*)" ) == 2 );

      lQuery =
            "SELECT AUTO_CREATE  FROM AL_TEMP_INVENTORY_SUB WHERE (assmbl_db_id, assmbl_cd, assmbl_bom_id) IN "
                  + "(SELECT assmbl_db_id, assmbl_cd, assmbl_bom_id FROM eqp_assmbl_bom WHERE assmbl_cd='ACFT_CD1' AND assmbl_bom_cd='ACFT-SYS-1-1-TRK-TRK-TRK-MID')";
      Assert.assertTrue( "Check auto creation is 1.",
            getIntValueFromQuery( lQuery, "AUTO_CREATE" ) == 1 );

      // check ACFT-SYS-1-1-TRK-TRK-TRK-MID auto creation is 1 and count must be 1
      lQuery =
            "SELECT count(*) FROM AL_TEMP_INVENTORY_SUB WHERE (assmbl_db_id, assmbl_cd, assmbl_bom_id) IN "
                  + "(SELECT assmbl_db_id, assmbl_cd, assmbl_bom_id FROM eqp_assmbl_bom WHERE assmbl_cd='ACFT_CD1' AND assmbl_bom_cd='ACFT-SYS-1-1-TRK-TRK-TRK-CHILD')";
      Assert.assertTrue( "Check count  is 1.", getIntValueFromQuery( lQuery, "COUNT(*)" ) == 2 );

      lQuery =
            "SELECT AUTO_CREATE  FROM AL_TEMP_INVENTORY_SUB WHERE (assmbl_db_id, assmbl_cd, assmbl_bom_id) IN "
                  + "(SELECT assmbl_db_id, assmbl_cd, assmbl_bom_id FROM eqp_assmbl_bom WHERE assmbl_cd='ACFT_CD1' AND assmbl_bom_cd='ACFT-SYS-1-1-TRK-TRK-TRK-CHILD')";
      Assert.assertTrue( "Check auto creation is 1.",
            getIntValueFromQuery( lQuery, "AUTO_CREATE" ) == 1 );

   }


   /**
    * This test is to verify OPER17973:Loose inventories is not generated in missing inventory table
    * if assmbly information isnot provided.
    *
    *
    */

   @Test
   public void testOPER_17973_MISSINGTRK_FULL_LOOSEY_NOASSMBL_MANDITORY() throws Exception {

      // String lRandom = String.valueOf( getRandom() );
      String lRandom = "MISSINGTRK";

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

      // loose TRK
      lMapInventory.clear();
      lMapInventory.put( "SERIAL_NO_OEM", "'TRK-" + lRandom + "'" );
      lMapInventory.put( "PART_NO_OEM", "'A0000020'" );
      lMapInventory.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'RFI'" );
      // lMapInventory.put( "ASSMBL_CD", "'ACFT_CD1'" );
      // lMapInventory.put( "ASSMBL_BOM_CD", "'ACFT-SYS-1-1-TRK-TRK-TRK-PARENT'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // call inventory validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "FULL", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      checkInventoryNoWarnings( "PASS" );

      // Check trk of ACFT is in missing inventory table
      String lQuery = "select 1 from " + TableUtil.AL_MISSING_INVENTORY
            + " where part_no_oem = 'A0000020' and config_slot = 'ACFT-SYS-1-1-TRK-TRK-TRK-PARENT' and parent_serial_no_oem ='ACFT-MISSINGTRK' ";
      Assert.assertTrue( "Check AL_MISSING_INVENTORY table to verify the record exists",
            RecordsExist( lQuery ) );

      // Check loose part is listed in missing inventory table if assemble information is not
      // provided.
      lQuery = "select 1 from " + TableUtil.AL_MISSING_INVENTORY
            + " where parent_part_no_oem = 'A0000020'";
      Assert.assertTrue(
            "Check AL_MISSING_INVENTORY table to verify the record not exists for loose TRK.",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.AL_MISSING_INVENTORY
            + " where parent_part_no_oem = 'A0000020'";
      Assert.assertTrue(
            "Check AL_MISSING_INVENTORY table to verify the record not exists for loose TRK.",
            RecordsExist( lQuery ) );

      // check ACFT-SYS-1-1-TRK-BATCH-PARENT auto creation is 1
      lQuery =
            "SELECT AUTO_CREATE  FROM AL_TEMP_INVENTORY_SUB WHERE (assmbl_db_id, assmbl_cd, assmbl_bom_id) IN "
                  + "(SELECT assmbl_db_id, assmbl_cd, assmbl_bom_id FROM eqp_assmbl_bom WHERE assmbl_cd='ACFT_CD1' AND assmbl_bom_cd='ACFT-SYS-1-1-TRK-TRK-TRK-PARENT')";
      Assert.assertTrue( "Check auto creation is 1.",
            getIntValueFromQuery( lQuery, "AUTO_CREATE" ) == 1 );

      // check ACFT-SYS-1-1-TRK-TRK-TRK-MID auto creation is 1 and count must be >=1
      lQuery =
            "SELECT count(*) FROM AL_TEMP_INVENTORY_SUB WHERE (assmbl_db_id, assmbl_cd, assmbl_bom_id) IN "
                  + "(SELECT assmbl_db_id, assmbl_cd, assmbl_bom_id FROM eqp_assmbl_bom WHERE assmbl_cd='ACFT_CD1' AND assmbl_bom_cd='ACFT-SYS-1-1-TRK-TRK-TRK-MID')";
      Assert.assertTrue( "Check count  is 1.", getIntValueFromQuery( lQuery, "COUNT(*)" ) >= 1 );

      lQuery =
            "SELECT AUTO_CREATE  FROM AL_TEMP_INVENTORY_SUB WHERE (assmbl_db_id, assmbl_cd, assmbl_bom_id) IN "
                  + "(SELECT assmbl_db_id, assmbl_cd, assmbl_bom_id FROM eqp_assmbl_bom WHERE assmbl_cd='ACFT_CD1' AND assmbl_bom_cd='ACFT-SYS-1-1-TRK-TRK-TRK-MID')";
      Assert.assertTrue( "Check auto creation is 1.",
            getIntValueFromQuery( lQuery, "AUTO_CREATE" ) == 1 );

      // check ACFT-SYS-1-1-TRK-TRK-TRK-MID auto creation is 1 and count must be >=1
      lQuery =
            "SELECT count(*) FROM AL_TEMP_INVENTORY_SUB WHERE (assmbl_db_id, assmbl_cd, assmbl_bom_id) IN "
                  + "(SELECT assmbl_db_id, assmbl_cd, assmbl_bom_id FROM eqp_assmbl_bom WHERE assmbl_cd='ACFT_CD1' AND assmbl_bom_cd='ACFT-SYS-1-1-TRK-TRK-TRK-CHILD')";
      Assert.assertTrue( "Check count  is 1.", getIntValueFromQuery( lQuery, "COUNT(*)" ) >= 1 );

      lQuery =
            "SELECT AUTO_CREATE  FROM AL_TEMP_INVENTORY_SUB WHERE (assmbl_db_id, assmbl_cd, assmbl_bom_id) IN "
                  + "(SELECT assmbl_db_id, assmbl_cd, assmbl_bom_id FROM eqp_assmbl_bom WHERE assmbl_cd='ACFT_CD1' AND assmbl_bom_cd='ACFT-SYS-1-1-TRK-TRK-TRK-CHILD')";
      Assert.assertTrue( "Check auto creation is 1.",
            getIntValueFromQuery( lQuery, "AUTO_CREATE" ) == 1 );

   }


   /**
    * This test is to verify OPER17973:Loose inventories is generated in missing inventory table if
    * assemble information is provided.
    *
    *
    */
   @Test
   public void testOPER_17973_MISSINGTRK_NOTFULL_LOOSEY_MANDITORY() throws Exception {

      // String lRandom = String.valueOf( getRandom() );
      String lRandom = "MISSINGTRK";

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

      // loose TRK
      lMapInventory.clear();
      lMapInventory.put( "SERIAL_NO_OEM", "'TRK-" + lRandom + "'" );
      lMapInventory.put( "PART_NO_OEM", "'A0000020'" );
      lMapInventory.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventory.put( "ASSMBL_CD", "'ACFT_CD1'" );
      lMapInventory.put( "ASSMBL_BOM_CD", "'ACFT-SYS-1-1-TRK-TRK-TRK-PARENT'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // call inventory validation
      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      checkInventoryNoWarnings( "PASS" );

      // Check trk of ACFT is in missing inventory table
      String lQuery = "select 1 from " + TableUtil.AL_MISSING_INVENTORY
            + " where part_no_oem = 'A0000020' and config_slot = 'ACFT-SYS-1-1-TRK-TRK-TRK-PARENT' and parent_serial_no_oem ='ACFT-MISSINGTRK' ";
      Assert.assertTrue( "Check AL_MISSING_INVENTORY table to verify the record exists",
            RecordsExist( lQuery ) );

      // Check loose part is listed in missing inventory table if assemble information is provided.
      lQuery = "select 1 from " + TableUtil.AL_MISSING_INVENTORY
            + " where parent_part_no_oem = 'A0000020' and config_slot = 'ACFT-SYS-1-1-TRK-TRK-TRK-CHILD'";
      Assert.assertTrue(
            "Check AL_MISSING_INVENTORY table to verify the record not exists for loose TRK.",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.AL_MISSING_INVENTORY
            + " where parent_part_no_oem = 'A0000020' and config_slot = 'ACFT-SYS-1-1-TRK-TRK-TRK-MID'";
      Assert.assertTrue(
            "Check AL_MISSING_INVENTORY table to verify the record not exists for loose TRK.",
            RecordsExist( lQuery ) );

      // check ACFT-SYS-1-1-TRK-BATCH-PARENT auto creation is 1
      lQuery =
            "SELECT AUTO_CREATE  FROM AL_TEMP_INVENTORY_SUB WHERE (assmbl_db_id, assmbl_cd, assmbl_bom_id) IN "
                  + "(SELECT assmbl_db_id, assmbl_cd, assmbl_bom_id FROM eqp_assmbl_bom WHERE assmbl_cd='ACFT_CD1' AND assmbl_bom_cd='ACFT-SYS-1-1-TRK-TRK-TRK-PARENT')";
      Assert.assertTrue( "Check auto creation is -1.",
            getIntValueFromQuery( lQuery, "AUTO_CREATE" ) == 1 );

      // check ACFT-SYS-1-1-TRK-TRK-TRK-MID auto creation is 1 and count must be 1
      lQuery =
            "SELECT count(*) FROM AL_TEMP_INVENTORY_SUB WHERE (assmbl_db_id, assmbl_cd, assmbl_bom_id) IN "
                  + "(SELECT assmbl_db_id, assmbl_cd, assmbl_bom_id FROM eqp_assmbl_bom WHERE assmbl_cd='ACFT_CD1' AND assmbl_bom_cd='ACFT-SYS-1-1-TRK-TRK-TRK-MID')";
      Assert.assertTrue( "Check count  is 2.", getIntValueFromQuery( lQuery, "COUNT(*)" ) == 2 );

      lQuery =
            "SELECT AUTO_CREATE  FROM AL_TEMP_INVENTORY_SUB WHERE (assmbl_db_id, assmbl_cd, assmbl_bom_id) IN "
                  + "(SELECT assmbl_db_id, assmbl_cd, assmbl_bom_id FROM eqp_assmbl_bom WHERE assmbl_cd='ACFT_CD1' AND assmbl_bom_cd='ACFT-SYS-1-1-TRK-TRK-TRK-MID')";
      Assert.assertTrue( "Check auto creation is -1.",
            getIntValueFromQuery( lQuery, "AUTO_CREATE" ) == 1 );

      // check ACFT-SYS-1-1-TRK-TRK-TRK-MID auto creation is 1 and count must be 2
      lQuery =
            "SELECT count(*) FROM AL_TEMP_INVENTORY_SUB WHERE (assmbl_db_id, assmbl_cd, assmbl_bom_id) IN "
                  + "(SELECT assmbl_db_id, assmbl_cd, assmbl_bom_id FROM eqp_assmbl_bom WHERE assmbl_cd='ACFT_CD1' AND assmbl_bom_cd='ACFT-SYS-1-1-TRK-TRK-TRK-CHILD')";
      Assert.assertTrue( "Check count  is 2.", getIntValueFromQuery( lQuery, "COUNT(*)" ) == 2 );

      lQuery =
            "SELECT AUTO_CREATE  FROM AL_TEMP_INVENTORY_SUB WHERE (assmbl_db_id, assmbl_cd, assmbl_bom_id) IN "
                  + "(SELECT assmbl_db_id, assmbl_cd, assmbl_bom_id FROM eqp_assmbl_bom WHERE assmbl_cd='ACFT_CD1' AND assmbl_bom_cd='ACFT-SYS-1-1-TRK-TRK-TRK-CHILD')";
      Assert.assertTrue( "Check auto creation is -1.",
            getIntValueFromQuery( lQuery, "AUTO_CREATE" ) == 1 );

   }


   /**
    * This function is data setup for child non-mandatory test.
    *
    *
    */

   public void dataSetupCHILD() {
      String lquery =
            "update eqp_assmbl_bom set mandatory_bool ='0' WHERE assmbl_cd='ACFT_CD1' AND assmbl_bom_cd='ACFT-SYS-1-1-TRK-TRK-TRK-CHILD'";
      runUpdate( lquery );

   }


   /**
    * This function is data restore for child non-mandatory test.
    *
    *
    */

   public void RestoredataSetupCHILD() {
      String lquery =
            "update eqp_assmbl_bom set mandatory_bool ='1' WHERE assmbl_cd='ACFT_CD1' AND assmbl_bom_cd='ACFT-SYS-1-1-TRK-TRK-TRK-CHILD'";
      runUpdate( lquery );

   }


   /**
    * This function is data setup for parent non-mandatory test.
    *
    *
    */

   public void dataSetupParent() {
      String lquery =
            "update eqp_assmbl_bom set mandatory_bool ='0' WHERE assmbl_cd='ACFT_CD1' AND assmbl_bom_cd='ACFT-SYS-1-1-TRK-TRK-TRK-PARENT'";
      runUpdate( lquery );

   }


   /**
    * This function is data restore for parent non-mandatory test.
    *
    *
    */

   public void RestoredataSetupParent() {
      String lquery =
            "update eqp_assmbl_bom set mandatory_bool ='1' WHERE assmbl_cd='ACFT_CD1' AND assmbl_bom_cd='ACFT-SYS-1-1-TRK-TRK-TRK-PARENT'";
      runUpdate( lquery );

   }

}
