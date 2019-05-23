package com.mxi.mx.core.maint.plan.actualsloader.inventory.validation;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.mxi.mx.core.maint.plan.actualsloader.inventory.imports.AbstractImportInventory;
import com.mxi.mx.util.TableUtil;


/**
 * This tests will verify the error codes for incompatibale parts
 *
 */
public class ValidateIncompatible extends AbstractImportInventory {

   @Rule
   public TestName testName = new TestName();

   public String iParmValue = null;


   @After
   @Override
   public void after() throws Exception {

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

   }


   /**
    * This test is to verify AML-10270:identify when two parts are installed on same highest
    * inventory key through mx_al_inventory package are incompatible using eqp_part_compt_def 
    * table
    *
    *
    */

   @Test
   public void testTRK_INCOMPAT_TRK_AML_10270() throws Exception {

      // String lRandom = String.valueOf( getRandom() );
      String lRandom = "000001";

      // create inventory map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();
      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-00001'" );
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

      // sub inventory of TRK1
      Map<String, String> lChild = new LinkedHashMap<String, String>();
      lChild.put( "PARENT_SERIAL_NO_OEM", "'ACFT-00001'" );
      lChild.put( "PARENT_PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lChild.put( "PARENT_MANUFACT_CD", "'10001'" );
      lChild.put( "BOM_PART_CD", "'ACFT-SYS-1-1-TRK-MULTIPART'" );
      lChild.put( "EQP_POS_CD", "'1'" );
      lChild.put( "SERIAL_NO_OEM", "'TRK-ACFT-00001'" );
      lChild.put( "PART_NO_OEM", "'A0000017D'" );
      lChild.put( "MANUFACT_CD", "'10001'" );
      lChild.put( "INV_CLASS_CD", "'TRK'" );
      lChild.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lChild.put( "INSTALL_DT", "to_date('01/01/2016','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lChild ) );

      // sub inventory of TRK2
      lChild.clear();
      lChild.put( "PARENT_SERIAL_NO_OEM", "'ACFT-00001'" );
      lChild.put( "PARENT_PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lChild.put( "PARENT_MANUFACT_CD", "'10001'" );
      lChild.put( "BOM_PART_CD", "'ACFT-SYS-1-1-TRK-SER-PARENT'" );
      lChild.put( "EQP_POS_CD", "'1'" );
      lChild.put( "SERIAL_NO_OEM", "'TRK-ACFT-00001'" );
      lChild.put( "PART_NO_OEM", "'A0000013'" );
      lChild.put( "MANUFACT_CD", "'10001'" );
      lChild.put( "INV_CLASS_CD", "'TRK'" );
      lChild.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lChild.put( "INSTALL_DT", "to_date('01/01/2016','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lChild ) );

      // call inventory validation
      runValidateInv( "FULL" );

      checkInventoryNoWarnings( "AML-10270" );

   }


   /**
    * This test is to verify AML-10270:identify when two parts are installed on same highest
    * inventory key through mx_al_inventory package are incompatible using eqp_part_compt_def 
    * table
    *
    *
    */

   @Test
   public void testTRK_INCOMPAT_ENG_AML_10270() throws Exception {

      // String lRandom = String.valueOf( getRandom() );
      String lRandom = "000001";

      // create inventory map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();
      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-00001'" );
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
      lMapInventoryAssy.put( "SERIAL_NO_OEM", "'ASSY-00001'" );
      lMapInventoryAssy.put( "PART_NO_OEM", "'ENG_ASSY_PN2'" );
      lMapInventoryAssy.put( "INV_CLASS_CD", "'ASSY'" );
      lMapInventoryAssy.put( "MANUFACT_CD", "' ABC11 '" );
      lMapInventoryAssy.put( "LOC_CD", "'OPS'" );
      lMapInventoryAssy.put( "INV_COND_CD", "'INSRV'" );
      lMapInventoryAssy.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventoryAssy ) );

      // create inventory attach map
      Map<String, String> lMapInventoryAttach = new LinkedHashMap<String, String>();
      lMapInventoryAttach.put( "PARENT_SERIAL_NO_OEM", "'ACFT-00001'" );
      lMapInventoryAttach.put( "PARENT_PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventoryAttach.put( "PARENT_MANUFACT_CD", "'10001'" );
      lMapInventoryAttach.put( "ATTACH_SERIAL_NO_OEM", "'ASSY-00001'" );
      lMapInventoryAttach.put( "ATTACH_PART_NO_OEM", "'ENG_ASSY_PN2'" );
      lMapInventoryAttach.put( "ATTACH_MANUFACT_CD", "'ABC11'" );
      lMapInventoryAttach.put( "BOM_PART_CD", "'ENG-ASSY'" );
      lMapInventoryAttach.put( "EQP_POS_CD", "'1'" );
      lMapInventoryAttach.put( "INSTALL_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_ATTACH, lMapInventoryAttach ) );

      // sub inventory of TRK1
      Map<String, String> lChild = new LinkedHashMap<String, String>();
      lChild.put( "PARENT_SERIAL_NO_OEM", "'ACFT-00001'" );
      lChild.put( "PARENT_PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lChild.put( "PARENT_MANUFACT_CD", "'10001'" );
      lChild.put( "BOM_PART_CD", "'ACFT-SYS-1-1-TRK-MULTIPART'" );
      lChild.put( "EQP_POS_CD", "'1'" );
      lChild.put( "SERIAL_NO_OEM", "'TRK-ACFT-00001'" );
      lChild.put( "PART_NO_OEM", "'A0000017D'" );
      lChild.put( "MANUFACT_CD", "'10001'" );
      lChild.put( "INV_CLASS_CD", "'TRK'" );
      lChild.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );
      lChild.put( "INSTALL_DT", "to_date('01/01/2016','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lChild ) );

      // call inventory validation
      runValidateInv( "FULL" );

      checkInventoryNoWarnings( "AML-10270" );

   }

}
