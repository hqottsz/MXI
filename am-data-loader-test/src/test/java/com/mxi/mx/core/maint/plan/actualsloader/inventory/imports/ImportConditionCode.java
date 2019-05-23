package com.mxi.mx.core.maint.plan.actualsloader.inventory.imports;

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
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.WhereClause;


/**
 * This test suite contains tests on default/valid condition cd.
 *
 */
public class ImportConditionCode extends AbstractImportInventory {

   @Rule
   public TestName testName = new TestName();

   public simpleIDs iINV_Ids = null;
   public simpleIDs iINV_Ids_Assy = null;
   public simpleIDs iINV_Ids_Assy_2 = null;
   public simpleIDs iINV_ACFT_TRK_Ids = null;
   public simpleIDs iINV_ENG_TRK_Ids = null;

   public simpleIDs iEVENT_Ids_FG_1 = null;
   public simpleIDs iEVENT_Ids_FG_2 = null;
   public simpleIDs iEVENT_Ids_FG_ACFT_TRK = null;
   public simpleIDs iEVENT_Ids_FG_ENG_TRK = null;

   public String iEVENT_TYPE_CD_FG = "FG";
   public String iEVENT_STATUS_CD_FGINST = "FGINST";

   public String iEVENT_SDESC_ASSY_FGINST_1 =
         "[ActualsLoader] Installation of (1) Part Name - Engine8 (PN: ENG_ASSY_PN8, SN: ASSY-00001)";
   public String iEVENT_SDESC_ASSY_FGINST_2 =
         "[ActualsLoader] Installation of (2) Part Name - Engine8 (PN: ENG_ASSY_PN8, SN: ASSY-00002)";


   /**
    * Clean up after each individual test
    *
    * @throws Exception
    */
   @After
   @Override
   public void after() throws Exception {

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
      clearIDs();
      String strTCName = testName.getMethodName();
      if ( strTCName.contains( "WITHPARM" ) ) {
         setPARM();
      }
   }


   /**
    * This test is to verify :complete ACFT with defaut cond_cd = INSRV
    *
    *
    */

   public void testACFT_COMPLETE_DEFAULT_VALUE_VALIDATION() throws Exception {

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

      // insert eng 2
      lMapInventory.clear();
      lMapInventory.put( "SERIAL_NO_OEM", "'ASSY-00002'" );
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

      // create sub inv map
      Map<String, String> lMapInventoryChild = new LinkedHashMap<String, String>();
      lMapInventoryChild.put( "PARENT_SERIAL_NO_OEM", "'ACFT-00001'" );
      lMapInventoryChild.put( "PARENT_PART_NO_OEM", "'ACFT_ASSY_PN8'" );
      lMapInventoryChild.put( "PARENT_MANUFACT_CD", "'10001'" );
      // lMapInventoryChild.put( "BOM_PART_CD", "'ACFT-SYS-2-CHILD-TRK-2'" );
      // lMapInventoryChild.put( "EQP_POS_CD", "'1'" );
      lMapInventoryChild.put( "SERIAL_NO_OEM", "'SER0001'" );
      lMapInventoryChild.put( "PART_NO_OEM", "'A0001260'" );
      lMapInventoryChild.put( "MANUFACT_CD", "'11111'" );
      lMapInventoryChild.put( "INV_CLASS_CD", "'SER'" );
      lMapInventoryChild.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventoryChild ) );

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
   public void testACFT_COMPLETE_DEFAULT_VALUE_IMPORT() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testACFT_COMPLETE_DEFAULT_VALUE_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runIMPORTInv( "FULL" );

      // Get iINV_Ids of ACFT
      iINV_Ids = verifyINVINVtable( "ACFT-00001", "ACFT" );
      // Get iINV_Ids of eng 1
      iINV_Ids_Assy = verifyINVINVtable( "ASSY-00001", "ASSY" );
      // Get iINV_Ids of eng 2
      iINV_Ids_Assy_2 = verifyINVINVtable( "ASSY-00002", "ASSY" );

      VerifyCompleteACFT( "INSRV" );

   }


   /**
    * This test is to verify :complete ACFT with cond_cd = INSRV
    *
    *
    */

   public void testACFT_COMPLETE_INSRV_VALUE_VALIDATION() throws Exception {

      // create inventory map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-00001'" );
      lMapInventory.put( "PART_NO_OEM", "'ACFT_ASSY_PN8'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "AC_REG_CD", "'AC-TEST'" );
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "INV_COND_CD", "'INSRV'" );
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

      // insert eng 2
      lMapInventory.clear();
      lMapInventory.put( "SERIAL_NO_OEM", "'ASSY-00002'" );
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

      // create sub inv map
      Map<String, String> lMapInventoryChild = new LinkedHashMap<String, String>();
      lMapInventoryChild.put( "PARENT_SERIAL_NO_OEM", "'ACFT-00001'" );
      lMapInventoryChild.put( "PARENT_PART_NO_OEM", "'ACFT_ASSY_PN8'" );
      lMapInventoryChild.put( "PARENT_MANUFACT_CD", "'10001'" );
      // lMapInventoryChild.put( "BOM_PART_CD", "'ACFT-SYS-2-CHILD-TRK-2'" );
      // lMapInventoryChild.put( "EQP_POS_CD", "'1'" );
      lMapInventoryChild.put( "SERIAL_NO_OEM", "'SER0001'" );
      lMapInventoryChild.put( "PART_NO_OEM", "'A0001260'" );
      lMapInventoryChild.put( "MANUFACT_CD", "'11111'" );
      lMapInventoryChild.put( "INV_CLASS_CD", "'SER'" );
      lMapInventoryChild.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventoryChild ) );

      // call inventory validation & import
      runValidateInv( "FULL" );
      checkInventoryNoWarnings( "PASS" );

   }


   /**
    * This test is to verify :complete ACFT with cond_cd = INSRV
    *
    *
    */
   @Test
   public void testACFT_COMPLETE_INSRV_VALUE_IMPORT() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testACFT_COMPLETE_INSRV_VALUE_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runIMPORTInv( "FULL" );
      VerifyCompleteACFT( "INSRV" );
   }


   /**
    * This test is to verify :complete ACFT with cond_cd = INSRV
    *
    *
    */

   public void testACFT_COMPLETE_RFI_VALUE_VALIDATION() throws Exception {

      // create inventory map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-00001'" );
      lMapInventory.put( "PART_NO_OEM", "'ACFT_ASSY_PN8'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "AC_REG_CD", "'AC-TEST'" );
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "INV_COND_CD", "'RFI'" );
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

      // insert eng 2
      lMapInventory.clear();
      lMapInventory.put( "SERIAL_NO_OEM", "'ASSY-00002'" );
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

      // create sub inv map
      Map<String, String> lMapInventoryChild = new LinkedHashMap<String, String>();
      lMapInventoryChild.put( "PARENT_SERIAL_NO_OEM", "'ACFT-00001'" );
      lMapInventoryChild.put( "PARENT_PART_NO_OEM", "'ACFT_ASSY_PN8'" );
      lMapInventoryChild.put( "PARENT_MANUFACT_CD", "'10001'" );
      // lMapInventoryChild.put( "BOM_PART_CD", "'ACFT-SYS-2-CHILD-TRK-2'" );
      // lMapInventoryChild.put( "EQP_POS_CD", "'1'" );
      lMapInventoryChild.put( "SERIAL_NO_OEM", "'SER0001'" );
      lMapInventoryChild.put( "PART_NO_OEM", "'A0001260'" );
      lMapInventoryChild.put( "MANUFACT_CD", "'11111'" );
      lMapInventoryChild.put( "INV_CLASS_CD", "'SER'" );
      lMapInventoryChild.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventoryChild ) );

      // call inventory validation & import
      runValidateInv( "FULL" );
      checkInventoryNoWarnings( "PASS" );

   }


   /**
    * This test is to verify :complete ACFT with cond_cd = RFI
    *
    *
    */
   @Test
   public void testACFT_COMPLETE_RFI_VALUE_IMPORT() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testACFT_COMPLETE_RFI_VALUE_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runIMPORTInv( "FULL" );
      VerifyCompleteACFT( "RFI" );
   }


   /**
    * This test is to verify :incomplete ACFT with cond_cd = default =REPREQ
    *
    *
    */

   public void testACFT_INCOMPLETION_DEFAULT_WITHPARM_VALIDATION() throws Exception {

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

      // create sub inv map
      Map<String, String> lMapInventoryChild = new LinkedHashMap<String, String>();
      lMapInventoryChild.put( "PARENT_SERIAL_NO_OEM", "'ACFT-00001'" );
      lMapInventoryChild.put( "PARENT_PART_NO_OEM", "'ACFT_ASSY_PN8'" );
      lMapInventoryChild.put( "PARENT_MANUFACT_CD", "'10001'" );
      // lMapInventoryChild.put( "BOM_PART_CD", "'ACFT-SYS-2-CHILD-TRK-2'" );
      // lMapInventoryChild.put( "EQP_POS_CD", "'1'" );
      lMapInventoryChild.put( "SERIAL_NO_OEM", "'SER0001'" );
      lMapInventoryChild.put( "PART_NO_OEM", "'A0001260'" );
      lMapInventoryChild.put( "MANUFACT_CD", "'11111'" );
      lMapInventoryChild.put( "INV_CLASS_CD", "'SER'" );
      lMapInventoryChild.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventoryChild ) );

      // call inventory validation & import
      runValidateInv( "NOT" );
      checkInventoryNoWarnings( "PASS" );

   }


   /**
    * This test is to verify :incomplete ACFT with cond_cd = default =REPREQ
    *
    */
   @Test
   public void testACFT_INCOMPLETION_DEFAULT_WITHPARM_IMPORT() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testACFT_INCOMPLETION_DEFAULT_WITHPARM_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runIMPORTInv( "NOT" );
      verifyIncompletACFT( "REPREQ" ); // this is the correct value.
   }


   /**
    * This test is to verify :incomplete ACFT with cond_cd = default =REPREQ
    *
    *
    */

   public void testACFT_INCOMPLETION_DEFAULT_NOPARM_VALIDATION() throws Exception {

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
      lMapInventory.put( "INV_COND_CD", "'INSRV'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // insert eng 2
      lMapInventory.clear();
      lMapInventory.put( "SERIAL_NO_OEM", "'ASSY-00002'" );
      lMapInventory.put( "PART_NO_OEM", "'ENG_ASSY_PN8'" );
      lMapInventory.put( "MANUFACT_CD", "'ABC11'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      // lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "INV_COND_CD", "'RFI'" ); // This would not work for old build after
                                                   // import, after import, this should be
                                                   // Overwrite as INSRV
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
      runValidateInv( "NOT" );
      checkInventoryNoWarnings( "PASS" );

   }


   /**
    * This test is to verify :incomplete ACFT with cond_cd = default =REPREQ
    *
    *
    */
   @Test
   public void testACFT_INCOMPLETION_DEFAULT_NOPARM_IMPORT() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testACFT_INCOMPLETION_DEFAULT_NOPARM_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runIMPORTInv( "NOT" );
      // VerifyINCompleteACFTWithENG( "INSRV" ); // before the fix
      VerifyINCompleteACFTWithENG( "REPREQ" ); // this is the correct value.
   }


   /**
    * This test is to verify :loose ENG with cond_cd = default =RFI
    *
    *
    */

   public void testLOOSE_ENG_COMPLETE_DEFAULT_VALIDATION() throws Exception {

      // create inventory map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      // insert eng

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

      // call inventory validation & import
      runValidateInv( "FULL" );
      checkInventoryNoWarnings( "PASS" );

   }


   /**
    * This test is to verify :loose ENG with cond_cd = default =RFI
    *
    *
    */
   @Test
   public void testLOOSE_ENG_COMPLETE_DEFAULT_IMPORT() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testLOOSE_ENG_COMPLETE_DEFAULT_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runIMPORTInv( "FULL" );
      VerifyCOMPLETE_ENG( "RFI" );
   }


   /**
    * This test is to verify :loose ENG with cond_cd =REPREQ
    *
    *
    */

   public void testLOOSE_ENG_COMPLETE_RFI_VALIDATION() throws Exception {

      // create inventory map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      // insert eng

      lMapInventory.put( "SERIAL_NO_OEM", "'ASSY-00001'" );
      lMapInventory.put( "PART_NO_OEM", "'ENG_ASSY_PN8'" );
      lMapInventory.put( "MANUFACT_CD", "'ABC11'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      // lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "INV_COND_CD", "'RFI'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // call inventory validation & import
      runValidateInv( "FULL" );
      checkInventoryNoWarnings( "PASS" );

   }


   /**
    * This test is to verify :loose ENG with cond_cd =REPREQ
    *
    *
    */
   @Test
   public void testLOOSE_ENG_COMPLETE_RFI_IMPORT() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testLOOSE_ENG_COMPLETE_RFI_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runIMPORTInv( "FULL" );
      VerifyCOMPLETE_ENG( "RFI" );
   }


   /**
    * This test is to verify :Incomplete loose ENG with cond_cd = default =RFI
    *
    *
    */

   public void testLOOSE_ENG_INCOMPLETE_DEFAULT_WITHPARM_VALIDATION() throws Exception {

      // create inventory map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      // insert eng
      lMapInventory.put( "SERIAL_NO_OEM", "'ASSY-00001'" );
      lMapInventory.put( "PART_NO_OEM", "'ENG_ASSY_PN8'" );
      lMapInventory.put( "MANUFACT_CD", "'ABC11'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      // lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      // lMapInventory.put( "INV_COND_CD", "'RFI'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // call inventory validation & import
      runValidateInv( "NOT" );
      checkInventoryNoWarnings( "PASS" );

   }


   /**
    * This test is to verify :Incomplete loose ENG with cond_cd = default =RFI
    *
    *
    */
   @Test
   public void testLOOSE_ENG_INCOMPLETE_DEFAULT_WITHPARM_IMPORT() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testLOOSE_ENG_INCOMPLETE_DEFAULT_WITHPARM_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runIMPORTInv( "NOT" );
      VerifyINCOMPLETE_ENG( "RFI" );
   }


   /**
    * This test is to verify :Incomplete loose ENG with cond_cd = default =REPREQ
    *
    *
    */

   public void testLOOSE_ENG_INCOMPLETE_DEFAULT_NOPARM_VALIDATION() throws Exception {

      // create inventory map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      // insert eng
      lMapInventory.put( "SERIAL_NO_OEM", "'ASSY-00001'" );
      lMapInventory.put( "PART_NO_OEM", "'ENG_ASSY_PN8'" );
      lMapInventory.put( "MANUFACT_CD", "'ABC11'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      // lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      // lMapInventory.put( "INV_COND_CD", "'RFI'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // call inventory validation & import
      runValidateInv( "NOT" );
      checkInventoryNoWarnings( "PASS" );

   }


   /**
    * This test is to verify :Incomplete loose ENG with cond_cd = default =REPREQ
    *
    *
    */
   @Test
   public void testLOOSE_ENG_INCOMPLETE_DEFAULT_NOPARM_IMPORT() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testLOOSE_ENG_INCOMPLETE_DEFAULT_NOPARM_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runIMPORTInv( "NOT" );
      // VerifyINCOMPLETE_ENG( "RFI" ); // this need to be fix in new build
      VerifyINCOMPLETE_ENG( "REPREQ" );
   }


   /**
    * This test is to verify :loose TRK with cond_cd = default =RFI
    *
    *
    */

   public void testLOOSE_TRK_DEFAULT_COMPLETE_VALIDATION() throws Exception {

      // create inventory map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      // insert TRK
      lMapInventory.put( "SERIAL_NO_OEM", "'TRK-00001'" );
      lMapInventory.put( "PART_NO_OEM", "'A0000220'" );
      lMapInventory.put( "MANUFACT_CD", "'11111'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      // lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      // lMapInventory.put( "INV_COND_CD", "'INSRV'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // call inventory validation & import
      runValidateInv( "FULL" );
      checkInventoryNoWarnings( "PASS" );

   }


   /**
    * This test is to verify :Incomplete loose ENG with cond_cd = default =RFI
    *
    *
    */
   @Test
   public void testLOOSE_TRK_DEFAULT_COMPLETE_DEFAULT_IMPORT() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testLOOSE_TRK_DEFAULT_COMPLETE_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runIMPORTInv( "FULL" );
      verifyTRKCOMPCOMPLETE( "RFI" );
   }


   /**
    * This test is to verify :loose TRK without PARM cond_cd = default = RREREQ
    *
    *
    */

   public void testLOOSE_TRK_DEFAULT_INCOMPLETE_NOPARM_VALIDATION() throws Exception {

      // create inventory map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      // insert TRK
      lMapInventory.put( "SERIAL_NO_OEM", "'TRK-00001'" );
      lMapInventory.put( "PART_NO_OEM", "'A0000220'" );
      lMapInventory.put( "MANUFACT_CD", "'11111'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      // lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      // lMapInventory.put( "INV_COND_CD", "'INSRV'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // call inventory validation & import
      runValidateInv( "NOT" );
      checkInventoryNoWarnings( "PASS" );

   }


   /**
    * This test is to verify :loose TRK without PARM cond_cd = default = REPREQ
    *
    */
   @Test
   public void testLOOSE_TRK_DEFAULT_INCOMPLETE_NOPARM_IMPORT() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testLOOSE_TRK_DEFAULT_INCOMPLETE_NOPARM_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runIMPORTInv( "NOT" );
      // verifyTRKCOMPINCOMPLETE( "RFI" ); // this will be fix
      verifyTRKCOMPINCOMPLETE( "REPREQ" );
   }


   /**
    * This test is to verify :loose TRK with PARM cond_cd = default = RFI
    *
    *
    */

   public void testLOOSE_TRK_DEFAULT_INCOMPLETE_WITHPARM_VALIDATION() throws Exception {

      // create inventory map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      // insert TRK
      lMapInventory.put( "SERIAL_NO_OEM", "'TRK-00001'" );
      lMapInventory.put( "PART_NO_OEM", "'A0000220'" );
      lMapInventory.put( "MANUFACT_CD", "'11111'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      // lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      // lMapInventory.put( "INV_COND_CD", "'INSRV'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // call inventory validation & import
      runValidateInv( "NOT" );
      checkInventoryNoWarnings( "PASS" );

   }


   /**
    * This test is to verify :loose TRK with PARM cond_cd = default = RFI
    *
    *
    */
   @Test
   public void testLOOSE_TRK_DEFAULT_INCOMPLETE_WITHPARM_IMPORT() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testLOOSE_TRK_DEFAULT_INCOMPLETE_NOPARM_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runIMPORTInv( "NOT" );
      verifyTRKCOMPINCOMPLETE( "RFI" );
   }


   /**
    * This test is to verify :loose TRK with PARM cond_cd = default = RFI
    *
    *
    */

   public void testLOOSE_TRK_REPREQ_INCOMPLETE_WITHPARM_VALIDATION() throws Exception {

      // create inventory map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      // insert TRK
      lMapInventory.put( "SERIAL_NO_OEM", "'TRK-00001'" );
      lMapInventory.put( "PART_NO_OEM", "'A0000220'" );
      lMapInventory.put( "MANUFACT_CD", "'11111'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      // lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // call inventory validation & import
      runValidateInv( "NOT" );
      checkInventoryNoWarnings( "PASS" );

   }


   /**
    * This test is to verify :loose TRK with PARM cond_cd = default = RFI
    *
    *
    */
   @Test
   public void testLOOSE_TRK_REPREQ_INCOMPLETE_WITHPARM_IMPORT() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testLOOSE_TRK_REPREQ_INCOMPLETE_WITHPARM_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runIMPORTInv( "NOT" );
      verifyTRKCOMPINCOMPLETE( "REPREQ" );
   }


   /**
    * This test is to verify :complete ACFT with defaut cond_cd = INSRV
    *
    *
    */

   public void testACFT_COMPLETE_DEFAULT_WITHNONMANDATORY_VALIDATION() throws Exception {

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

      // insert eng 2
      lMapInventory.clear();
      lMapInventory.put( "SERIAL_NO_OEM", "'ASSY-00002'" );
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

      // create sub inv map
      Map<String, String> lMapInventoryChild = new LinkedHashMap<String, String>();
      lMapInventoryChild.put( "PARENT_SERIAL_NO_OEM", "'ACFT-00001'" );
      lMapInventoryChild.put( "PARENT_PART_NO_OEM", "'ACFT_ASSY_PN8'" );
      lMapInventoryChild.put( "PARENT_MANUFACT_CD", "'10001'" );
      lMapInventoryChild.put( "BOM_PART_CD", "'ACFT-SYS-2-CHILD-TRK-2'" );
      lMapInventoryChild.put( "EQP_POS_CD", "'1'" );
      lMapInventoryChild.put( "SERIAL_NO_OEM", "'NONMAND0001'" );
      lMapInventoryChild.put( "PART_NO_OEM", "'A0000240'" );
      lMapInventoryChild.put( "MANUFACT_CD", "'11111'" );
      lMapInventoryChild.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventoryChild.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventoryChild ) );

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
   public void testACFT_COMPLETE_DEFAULT_WITHNONMANDATORY_IMPORT() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testACFT_COMPLETE_DEFAULT_WITHNONMANDATORY_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runIMPORTInv( "FULL" );

      // // Get iINV_Ids of ACFT
      // iINV_Ids = verifyINVINVtable( "ACFT-00001", "ACFT" );
      // // Get iINV_Ids of eng 1
      // iINV_Ids_Assy = verifyINVINVtable( "ASSY-00001", "ASSY" );
      // // Get iINV_Ids of eng 2
      // iINV_Ids_Assy_2 = verifyINVINVtable( "ASSY-00002", "ASSY" );

      VerifyCompleteACFTWithMANDATORY( "INSRV" );

   }


   // =================================================================

   public void VerifyCompleteACFT( String aCOND_CD ) {

      // Get iINV_Ids of ACFT
      iINV_Ids = verifyINVINVtable( "ACFT-00001", "ACFT" );
      // Get iINV_Ids of eng 1
      iINV_Ids_Assy = verifyINVINVtable( "ASSY-00001", "ASSY" );
      // Get iINV_Ids of eng 2
      iINV_Ids_Assy_2 = verifyINVINVtable( "ASSY-00002", "ASSY" );

      // Verify ACFT's complete_bool and inv_cond_cd
      verifyINVINVtable( iINV_Ids, "ACFT_T9", "ACFT-00001", null, "ACFT", "1", aCOND_CD );

      // Verify SYS-1
      verifyINVINVtable( iINV_Ids, "ACFT_T9", "XXX", "SYS-1", "SYS", "1", "INSRV" );

      // Verify SYS-1-ENG
      verifyINVINVtable( iINV_Ids, "ACFT_T9", "XXX", "SYS-1-ENG", "SYS", "1", "INSRV" );

      // Verify SYS-2
      verifyINVINVtable( iINV_Ids, "ACFT_T9", "XXX", "SYS-2", "SYS", "1", "INSRV" );

      // Verify ACFT-SYS-1-1-TRK-BATCH-PARENT
      verifyINVINVtable( iINV_Ids, "ACFT_T9", "XXX", "ACFT-SYS-1-1-TRK-BATCH-PARENT", "TRK", "1",
            "INSRV" );

      // Verify ACFT-SYS-2-CHILD-TRK-1
      verifyINVINVtable( iINV_Ids, "ACFT_T9", "XXX", "ACFT-SYS-2-CHILD-TRK-1", "TRK", "1",
            "INSRV" );

      // Verify ACFT-SYS-2-SER
      String lQuery = "select 1 from " + TableUtil.INV_INV + " where H_INV_NO_DB_ID="
            + iINV_Ids.getNO_DB_ID() + " and H_INV_NO_ID=" + iINV_Ids.getNO_ID()
            + "  and SERIAL_NO_OEM='SER0001' and INV_COND_CD='INSRV' ";
      Assert.assertTrue( "Check inv_inv table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify non-manadatory ACFT-SYS-1-TRK-1 is not in inv_inv
      lQuery = "select 1 from " + TableUtil.INV_INV + " where H_INV_NO_DB_ID="
            + iINV_Ids.getNO_DB_ID() + " and H_INV_NO_ID=" + iINV_Ids.getNO_ID()
            + "  and CONFIG_POS_SDESC='ACFT-SYS-1-TRK-1'";
      Assert.assertFalse( "Check inv_inv table to verify the record not exists",
            RecordsExist( lQuery ) );

      // verify non-mandatory ACFT-SYS-2-CHILD-TRK-2 is not in inv_inv
      lQuery = "select 1 from " + TableUtil.INV_INV + " where H_INV_NO_DB_ID="
            + iINV_Ids.getNO_DB_ID() + " and H_INV_NO_ID=" + iINV_Ids.getNO_ID()
            + "  and CONFIG_POS_SDESC='ACFT-SYS-2-CHILD-TRK-2'";
      Assert.assertFalse( "Check inv_inv table to verify the record not exists",
            RecordsExist( lQuery ) );

      // verify ENG-ASSY
      verifyINVINVtable( iINV_Ids, "ACFT_T9", "ASSY-00001", "ENG-ASSY (1)", "ASSY", "1", "INSRV" );
      verifyINVINVtable( iINV_Ids, "ACFT_T9", "ASSY-00002", "ENG-ASSY (2)", "ASSY", "1", "INSRV" );

      // verify ENG-SYS-1
      verifyINVINVtable( iINV_Ids, "ENG_CD9", "XXX", "ENG-ASSY (1) ->ENG-SYS-1", "SYS", "1",
            "INSRV" );
      verifyINVINVtable( iINV_Ids, "ENG_CD9", "XXX", "ENG-ASSY (2) ->ENG-SYS-1", "SYS", "1",
            "INSRV" );

      // verify ENG-SYS-1-TRK
      verifyINVINVtable( iINV_Ids, "ENG_CD9", "XXX", "ENG-ASSY (1) ->ENG-SYS-1-TRK", "TRK", "1",
            "INSRV" );
      verifyINVINVtable( iINV_Ids, "ENG_CD9", "XXX", "ENG-ASSY (2) ->ENG-SYS-1-TRK", "TRK", "1",
            "INSRV" );

      // verify ENG-SYS-1-TRK2
      verifyINVINVtable( iINV_Ids, "ENG_CD9", "XXX", "ENG-ASSY (1) ->ENG-SYS-1-TRK2", "TRK", "1",
            "INSRV" );
      verifyINVINVtable( iINV_Ids, "ENG_CD9", "XXX", "ENG-ASSY (2) ->ENG-SYS-1-TRK2", "TRK", "1",
            "INSRV" );

      // Verify evt_event for assy installation
      iEVENT_Ids_FG_1 =
            getEventIds( iEVENT_TYPE_CD_FG, iEVENT_STATUS_CD_FGINST, iEVENT_SDESC_ASSY_FGINST_1 );
      iEVENT_Ids_FG_2 =
            getEventIds( iEVENT_TYPE_CD_FG, iEVENT_STATUS_CD_FGINST, iEVENT_SDESC_ASSY_FGINST_2 );

      lQuery = "select 1 from " + TableUtil.EVT_INV + " where EVENT_DB_ID="
            + iEVENT_Ids_FG_1.getNO_DB_ID() + " and EVENT_ID=" + iEVENT_Ids_FG_1.getNO_ID();
      Assert.assertTrue( "Check inv_inv table to verify the record exists",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.EVT_INV + " where EVENT_DB_ID="
            + iEVENT_Ids_FG_2.getNO_DB_ID() + " and EVENT_ID=" + iEVENT_Ids_FG_2.getNO_ID();
      Assert.assertTrue( "Check inv_inv table to verify the record exists",
            RecordsExist( lQuery ) );

   }


   public void VerifyCompleteACFTWithMANDATORY( String aCOND_CD ) {

      // Get iINV_Ids of ACFT
      iINV_Ids = verifyINVINVtable( "ACFT-00001", "ACFT" );
      // Get iINV_Ids of eng 1
      iINV_Ids_Assy = verifyINVINVtable( "ASSY-00001", "ASSY" );
      // Get iINV_Ids of eng 2
      iINV_Ids_Assy_2 = verifyINVINVtable( "ASSY-00002", "ASSY" );

      // Verify ACFT's complete_bool and inv_cond_cd
      verifyINVINVtable( iINV_Ids, "ACFT_T9", "ACFT-00001", null, "ACFT", "1", aCOND_CD );

      // Verify SYS-1
      verifyINVINVtable( iINV_Ids, "ACFT_T9", "XXX", "SYS-1", "SYS", "1", "INSRV" );

      // Verify SYS-1-ENG
      verifyINVINVtable( iINV_Ids, "ACFT_T9", "XXX", "SYS-1-ENG", "SYS", "1", "INSRV" );

      // Verify SYS-2
      verifyINVINVtable( iINV_Ids, "ACFT_T9", "XXX", "SYS-2", "SYS", "1", "INSRV" );

      // Verify ACFT-SYS-1-1-TRK-BATCH-PARENT
      verifyINVINVtable( iINV_Ids, "ACFT_T9", "XXX", "ACFT-SYS-1-1-TRK-BATCH-PARENT", "TRK", "1",
            "INSRV" );

      // Verify ACFT-SYS-2-CHILD-TRK-1
      verifyINVINVtable( iINV_Ids, "ACFT_T9", "XXX", "ACFT-SYS-2-CHILD-TRK-1", "TRK", "1",
            "INSRV" );

      // Verify ACFT-SYS-2-CHILD-TRK-2
      verifyINVINVtable( iINV_Ids, "ACFT_T9", "NONMAND0001", "ACFT-SYS-2-CHILD-TRK-2", "TRK", "1",
            "INSRV" );

      // verify non-manadatory ACFT-SYS-1-TRK-1 is not in inv_inv
      String lQuery = "select 1 from " + TableUtil.INV_INV + " where H_INV_NO_DB_ID="
            + iINV_Ids.getNO_DB_ID() + " and H_INV_NO_ID=" + iINV_Ids.getNO_ID()
            + "  and CONFIG_POS_SDESC='ACFT-SYS-1-TRK-1'";
      Assert.assertFalse( "Check inv_inv table to verify the record not exists",
            RecordsExist( lQuery ) );

      // verify ENG-ASSY
      verifyINVINVtable( iINV_Ids, "ACFT_T9", "ASSY-00001", "ENG-ASSY (1)", "ASSY", "1", "INSRV" );
      verifyINVINVtable( iINV_Ids, "ACFT_T9", "ASSY-00002", "ENG-ASSY (2)", "ASSY", "1", "INSRV" );

      // verify ENG-SYS-1
      verifyINVINVtable( iINV_Ids, "ENG_CD9", "XXX", "ENG-ASSY (1) ->ENG-SYS-1", "SYS", "1",
            "INSRV" );
      verifyINVINVtable( iINV_Ids, "ENG_CD9", "XXX", "ENG-ASSY (2) ->ENG-SYS-1", "SYS", "1",
            "INSRV" );

      // verify ENG-SYS-1-TRK
      verifyINVINVtable( iINV_Ids, "ENG_CD9", "XXX", "ENG-ASSY (1) ->ENG-SYS-1-TRK", "TRK", "1",
            "INSRV" );
      verifyINVINVtable( iINV_Ids, "ENG_CD9", "XXX", "ENG-ASSY (2) ->ENG-SYS-1-TRK", "TRK", "1",
            "INSRV" );

      // verify ENG-SYS-1-TRK2
      verifyINVINVtable( iINV_Ids, "ENG_CD9", "XXX", "ENG-ASSY (1) ->ENG-SYS-1-TRK2", "TRK", "1",
            "INSRV" );
      verifyINVINVtable( iINV_Ids, "ENG_CD9", "XXX", "ENG-ASSY (2) ->ENG-SYS-1-TRK2", "TRK", "1",
            "INSRV" );

      // Verify evt_event for assy installation
      iEVENT_Ids_FG_1 =
            getEventIds( iEVENT_TYPE_CD_FG, iEVENT_STATUS_CD_FGINST, iEVENT_SDESC_ASSY_FGINST_1 );
      iEVENT_Ids_FG_2 =
            getEventIds( iEVENT_TYPE_CD_FG, iEVENT_STATUS_CD_FGINST, iEVENT_SDESC_ASSY_FGINST_2 );

      lQuery = "select 1 from " + TableUtil.EVT_INV + " where EVENT_DB_ID="
            + iEVENT_Ids_FG_1.getNO_DB_ID() + " and EVENT_ID=" + iEVENT_Ids_FG_1.getNO_ID();
      Assert.assertTrue( "Check inv_inv table to verify the record exists",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.EVT_INV + " where EVENT_DB_ID="
            + iEVENT_Ids_FG_2.getNO_DB_ID() + " and EVENT_ID=" + iEVENT_Ids_FG_2.getNO_ID();
      Assert.assertTrue( "Check inv_inv table to verify the record exists",
            RecordsExist( lQuery ) );

   }


   public void verifyIncompletACFT( String aCOND_CD ) {

      // Get iINV_Ids of ACFT
      iINV_Ids = verifyINVINVtable( "ACFT-00001", "ACFT" );

      // Verify ACFT's complete_bool and inv_cond_cd
      verifyINVINVtable( iINV_Ids, "ACFT_T9", "ACFT-00001", null, "ACFT", "0", aCOND_CD );

      // Verify SYS-1
      verifyINVINVtable( iINV_Ids, "ACFT_T9", "XXX", "SYS-1", "SYS", "0", "INSRV" );

      // Verify SYS-1-ENG
      verifyINVINVtable( iINV_Ids, "ACFT_T9", "XXX", "SYS-1-ENG", "SYS", "0", "INSRV" );

      // Verify SYS-2
      verifyINVINVtable( iINV_Ids, "ACFT_T9", "XXX", "SYS-2", "SYS", "0", "INSRV" );

      // Verify ACFT-SYS-2-SER
      String lQuery = "select 1 from " + TableUtil.INV_INV + " where H_INV_NO_DB_ID="
            + iINV_Ids.getNO_DB_ID() + " and H_INV_NO_ID=" + iINV_Ids.getNO_ID()
            + "  and SERIAL_NO_OEM='SER0001' and INV_COND_CD='INSRV' ";
      Assert.assertTrue( "Check inv_inv table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify non-manadatory ACFT-SYS-1-TRK-1 is not in inv_inv
      lQuery = "select 1 from " + TableUtil.INV_INV + " where H_INV_NO_DB_ID="
            + iINV_Ids.getNO_DB_ID() + " and H_INV_NO_ID=" + iINV_Ids.getNO_ID()
            + "  and CONFIG_POS_SDESC='ACFT-SYS-1-TRK-1'";
      Assert.assertFalse( "Check inv_inv table to verify the record not exists",
            RecordsExist( lQuery ) );

      // verify non-mandatory ACFT-SYS-2-CHILD-TRK-2 is not in inv_inv
      lQuery = "select 1 from " + TableUtil.INV_INV + " where H_INV_NO_DB_ID="
            + iINV_Ids.getNO_DB_ID() + " and H_INV_NO_ID=" + iINV_Ids.getNO_ID()
            + "  and CONFIG_POS_SDESC='ACFT-SYS-2-CHILD-TRK-2'";
      Assert.assertFalse( "Check inv_inv table to verify the record not exists",
            RecordsExist( lQuery ) );

      // Verify eng is not in inv_inv table
      lQuery = "select 1 from " + TableUtil.INV_INV + " where H_INV_NO_DB_ID="
            + iINV_Ids.getNO_DB_ID() + " and H_INV_NO_ID=" + iINV_Ids.getNO_ID()
            + "  and CONFIG_POS_SDESC='ENG-ASSY (1)'";
      Assert.assertFalse( "Check inv_inv table to verify the eng record not exists",
            RecordsExist( lQuery ) );

      // verify ACFT-SYS-1-1-TRK-BATCH-PARENT is not in inv_inv table
      lQuery = "select 1 from " + TableUtil.INV_INV + " where H_INV_NO_DB_ID="
            + iINV_Ids.getNO_DB_ID() + " and H_INV_NO_ID=" + iINV_Ids.getNO_ID()
            + "  and CONFIG_POS_SDESC='ACFT-SYS-1-1-TRK-BATCH-PARENT'";
      Assert.assertFalse( "Check inv_inv table to verify the record not exists",
            RecordsExist( lQuery ) );

   }


   public void VerifyINCompleteACFTWithENG( String aCOND_CD ) {

      // Get iINV_Ids of ACFT
      iINV_Ids = verifyINVINVtable( "ACFT-00001", "ACFT" );
      // Get iINV_Ids of eng 1
      iINV_Ids_Assy = verifyINVINVtable( "ASSY-00001", "ASSY" );
      // Get iINV_Ids of eng 2
      iINV_Ids_Assy_2 = verifyINVINVtable( "ASSY-00002", "ASSY" );

      // Verify ACFT's complete_bool and inv_cond_cd
      verifyINVINVtable( iINV_Ids, "ACFT_T9", "ACFT-00001", null, "ACFT", "0", aCOND_CD );

      // Verify SYS-1
      verifyINVINVtable( iINV_Ids, "ACFT_T9", "XXX", "SYS-1", "SYS", "0", "INSRV" );

      // Verify SYS-1-ENG
      verifyINVINVtable( iINV_Ids, "ACFT_T9", "XXX", "SYS-1-ENG", "SYS", "0", "INSRV" );

      // Verify SYS-2
      verifyINVINVtable( iINV_Ids, "ACFT_T9", "XXX", "SYS-2", "SYS", "0", "INSRV" );

      // // Verify ACFT-SYS-1-1-TRK-BATCH-PARENT
      // verifyINVINVtable( iINV_Ids, "ACFT_T9", "XXX", "ACFT-SYS-1-1-TRK-BATCH-PARENT", "TRK", "1",
      // "INSRV" );
      //
      // // Verify ACFT-SYS-2-CHILD-TRK-1
      // verifyINVINVtable( iINV_Ids, "ACFT_T9", "XXX", "ACFT-SYS-2-CHILD-TRK-1", "TRK", "1",
      // "INSRV" );

      // verify non-manadatory ACFT-SYS-1-TRK-1 is not in inv_inv
      String lQuery = "select 1 from " + TableUtil.INV_INV + " where H_INV_NO_DB_ID="
            + iINV_Ids.getNO_DB_ID() + " and H_INV_NO_ID=" + iINV_Ids.getNO_ID()
            + "  and CONFIG_POS_SDESC='ACFT-SYS-1-TRK-1'";
      Assert.assertFalse( "Check inv_inv table to verify the record not exists",
            RecordsExist( lQuery ) );

      // verify non-mandatory ACFT-SYS-2-CHILD-TRK-2 is not in inv_inv
      lQuery = "select 1 from " + TableUtil.INV_INV + " where H_INV_NO_DB_ID="
            + iINV_Ids.getNO_DB_ID() + " and H_INV_NO_ID=" + iINV_Ids.getNO_ID()
            + "  and CONFIG_POS_SDESC='ACFT-SYS-2-CHILD-TRK-2'";
      Assert.assertFalse( "Check inv_inv table to verify the record not exists",
            RecordsExist( lQuery ) );

      // verify ENG-ASSY
      verifyINVINVtable( iINV_Ids, "ACFT_T9", "ASSY-00001", "ENG-ASSY (1)", "ASSY", "0", "INSRV" );
      verifyINVINVtable( iINV_Ids, "ACFT_T9", "ASSY-00002", "ENG-ASSY (2)", "ASSY", "0", "INSRV" );

      // verify ENG-SYS-1
      verifyINVINVtable( iINV_Ids, "ENG_CD9", "XXX", "ENG-ASSY (1) ->ENG-SYS-1", "SYS", "0",
            "INSRV" );
      verifyINVINVtable( iINV_Ids, "ENG_CD9", "XXX", "ENG-ASSY (2) ->ENG-SYS-1", "SYS", "0",
            "INSRV" );

      // verify ENG-SYS-1-TRK is not in the inv-inv table
      lQuery = "select 1 from " + TableUtil.INV_INV + " where H_INV_NO_DB_ID="
            + iINV_Ids.getNO_DB_ID() + " and H_INV_NO_ID=" + iINV_Ids.getNO_ID()
            + "  and CONFIG_POS_SDESC='ENG-ASSY (1) ->ENG-SYS-1-TRK'";
      Assert.assertFalse( "Check inv_inv table to verify the eng record not exists",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.INV_INV + " where H_INV_NO_DB_ID="
            + iINV_Ids.getNO_DB_ID() + " and H_INV_NO_ID=" + iINV_Ids.getNO_ID()
            + "  and CONFIG_POS_SDESC='ENG-ASSY (1) ->ENG-SYS-1-TRK'";
      Assert.assertFalse( "Check inv_inv table to verify the eng record not exists",
            RecordsExist( lQuery ) );

      // // verify ENG-SYS-1-TRK
      // verifyINVINVtable( iINV_Ids, "ENG_CD9", "XXX", "ENG-ASSY (1) ->ENG-SYS-1-TRK", "TRK", "1",
      // "INSRV" );
      // verifyINVINVtable( iINV_Ids, "ENG_CD9", "XXX", "ENG-ASSY (2) ->ENG-SYS-1-TRK", "TRK", "1",
      // "INSRV" );
      //
      // // verify ENG-SYS-1-TRK2
      // verifyINVINVtable( iINV_Ids, "ENG_CD9", "XXX", "ENG-ASSY (1) ->ENG-SYS-1-TRK2", "TRK", "1",
      // "INSRV" );
      // verifyINVINVtable( iINV_Ids, "ENG_CD9", "XXX", "ENG-ASSY (2) ->ENG-SYS-1-TRK2", "TRK", "1",
      // "INSRV" );

      // Verify evt_event for assy installation
      iEVENT_Ids_FG_1 =
            getEventIds( iEVENT_TYPE_CD_FG, iEVENT_STATUS_CD_FGINST, iEVENT_SDESC_ASSY_FGINST_1 );
      iEVENT_Ids_FG_2 =
            getEventIds( iEVENT_TYPE_CD_FG, iEVENT_STATUS_CD_FGINST, iEVENT_SDESC_ASSY_FGINST_2 );

      lQuery = "select 1 from " + TableUtil.EVT_INV + " where EVENT_DB_ID="
            + iEVENT_Ids_FG_1.getNO_DB_ID() + " and EVENT_ID=" + iEVENT_Ids_FG_1.getNO_ID();
      Assert.assertTrue( "Check inv_inv table to verify the record exists",
            RecordsExist( lQuery ) );

      lQuery = "select 1 from " + TableUtil.EVT_INV + " where EVENT_DB_ID="
            + iEVENT_Ids_FG_2.getNO_DB_ID() + " and EVENT_ID=" + iEVENT_Ids_FG_2.getNO_ID();
      Assert.assertTrue( "Check inv_inv table to verify the record exists",
            RecordsExist( lQuery ) );

      // verify ACFT-SYS-1-1-TRK-BATCH-PARENT is not in inv_inv table
      lQuery = "select 1 from " + TableUtil.INV_INV + " where H_INV_NO_DB_ID="
            + iINV_Ids.getNO_DB_ID() + " and H_INV_NO_ID=" + iINV_Ids.getNO_ID()
            + "  and CONFIG_POS_SDESC='ACFT-SYS-1-1-TRK-BATCH-PARENT'";
      Assert.assertFalse( "Check inv_inv table to verify the record not exists",
            RecordsExist( lQuery ) );

   }


   public void VerifyCOMPLETE_ENG( String aCOND_CD ) {

      // Get iINV_Ids of eng 1
      iINV_Ids = verifyINVINVtable( "ASSY-00001", "ASSY" );

      // verify ENG-ASSY
      verifyINVINVtable( iINV_Ids, "ENG_CD9", "ASSY-00001", "ENG_CD9", "ASSY", "1", aCOND_CD );

      // verify ENG-SYS-1
      verifyINVINVtable( iINV_Ids, "ENG_CD9", "XXX", "ENG-SYS-1", "SYS", "1", "INSRV" );

      // verify ENG-SYS-1-TRK
      verifyINVINVtable( iINV_Ids, "ENG_CD9", "XXX", "ENG-SYS-1-TRK", "TRK", "1", "INSRV" );

      // verify ENG-SYS-1-TRK2
      verifyINVINVtable( iINV_Ids, "ENG_CD9", "XXX", "ENG-SYS-1-TRK2", "TRK", "1", "INSRV" );

   }


   public void VerifyINCOMPLETE_ENG( String aCOND_CD ) {

      // Get iINV_Ids of eng 1
      iINV_Ids = verifyINVINVtable( "ASSY-00001", "ASSY" );

      // verify ENG-ASSY
      verifyINVINVtable( iINV_Ids, "ENG_CD9", "ASSY-00001", "ENG_CD9", "ASSY", "0", aCOND_CD );

      // verify ENG-SYS-1
      verifyINVINVtable( iINV_Ids, "ENG_CD9", "XXX", "ENG-SYS-1", "SYS", "0", "INSRV" );

      // Verify ENG-SYS-1-TRK is not in inv_inv table
      String lQuery = "select 1 from " + TableUtil.INV_INV + " where H_INV_NO_DB_ID="
            + iINV_Ids.getNO_DB_ID() + " and H_INV_NO_ID=" + iINV_Ids.getNO_ID()
            + "  and CONFIG_POS_SDESC='ENG-SYS-1-TRK'";
      Assert.assertFalse( "Check inv_inv table to verify the eng record not exists",
            RecordsExist( lQuery ) );

      // Verify ENG-SYS-1-TRK2 is not in inv_inv table
      lQuery = "select 1 from " + TableUtil.INV_INV + " where H_INV_NO_DB_ID="
            + iINV_Ids.getNO_DB_ID() + " and H_INV_NO_ID=" + iINV_Ids.getNO_ID()
            + "  and CONFIG_POS_SDESC='ENG-SYS-1-TRK2'";
      Assert.assertFalse( "Check inv_inv table to verify the eng record not exists",
            RecordsExist( lQuery ) );

   }


   public void verifyTRKCOMPCOMPLETE( String aCOND_CD ) {

      // Get iINV_Ids of TRK
      iINV_Ids = verifyINVINVtable( "TRK-00001", "TRK" );

      // Verify ACFT-SYS-1-1-TRK-BATCH-PARENT
      verifyINVINVtable( iINV_Ids, "ACFT_T9", "TRK-00001", "ACFT-SYS-1-1-TRK-BATCH-PARENT", "TRK",
            "1", aCOND_CD );

      // Verify ACFT-SYS-2-CHILD-TRK-1
      verifyINVINVtable( iINV_Ids, "ACFT_T9", "XXX", "ACFT-SYS-2-CHILD-TRK-1", "TRK", "1",
            "INSRV" );

   }


   public void verifyTRKCOMPINCOMPLETE( String aCOND_CD ) {

      // Get iINV_Ids of TRK
      iINV_Ids = verifyINVINVtable( "TRK-00001", "TRK" );

      // Verify ACFT-SYS-1-1-TRK-BATCH-PARENT
      verifyINVINVtable( iINV_Ids, "ACFT_T9", "TRK-00001", "ACFT-SYS-1-1-TRK-BATCH-PARENT", "TRK",
            "0", aCOND_CD );

      // // Verify ACFT-SYS-2-CHILD-TRK-1 is not int inv-inv table
      String lQuery = "select 1 from " + TableUtil.INV_INV + " where H_INV_NO_DB_ID="
            + iINV_Ids.getNO_DB_ID() + " and H_INV_NO_ID=" + iINV_Ids.getNO_ID()
            + "  and CONFIG_POS_SDESC='ACFT-SYS-2-CHILD-TRK-1'";
      Assert.assertFalse( "Check inv_inv table to verify the eng record not exists",
            RecordsExist( lQuery ) );

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


   public void clearIDs() {
      iINV_Ids = null;
      iINV_Ids_Assy = null;
      iINV_Ids_Assy_2 = null;
      iEVENT_Ids_FG_1 = null;
      iEVENT_Ids_FG_2 = null;
      iEVENT_Ids_FG_ACFT_TRK = null;
      iEVENT_Ids_FG_ENG_TRK = null;

   }


   /**
    * This function is called by after test method to restore data which has been modified by tests.
    *
    *
    */

   public void RestoreData() {

      RestoreINVData( iINV_Ids );
      RestoreINVData( iINV_Ids_Assy );
      RestoreINVData( iINV_Ids_Assy_2 );

      clearEvent( iEVENT_Ids_FG_1 );
      clearEvent( iEVENT_Ids_FG_2 );
      clearEvent( iEVENT_Ids_FG_ACFT_TRK );
      clearEvent( iEVENT_Ids_FG_ENG_TRK );

      RestorePARM();

   }


   public void setPARM() {
      String aUpdateQuery =
            "update UTL_CONFIG_PARM set PARM_VALUE='TRUE' where PARM_NAME='ENABLE_READY_FOR_BUILD'";

      runUpdate( aUpdateQuery );
   }


   public void RestorePARM() {
      String aUpdateQuery =
            "update UTL_CONFIG_PARM set PARM_VALUE='FALSE' where PARM_NAME='ENABLE_READY_FOR_BUILD'";

      runUpdate( aUpdateQuery );
   }

}
