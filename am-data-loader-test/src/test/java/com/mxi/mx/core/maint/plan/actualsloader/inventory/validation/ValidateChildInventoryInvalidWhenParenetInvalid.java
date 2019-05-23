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

import com.mxi.mx.core.maint.plan.actualsloader.inventory.imports.AbstractImportInventory;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.TestConstants;


/**
 * This test suite contains unit tests for Actuals Loader inventory validations for valid child and
 * invalid child inventory
 *
 * @author Alicia Qian
 */
public class ValidateChildInventoryInvalidWhenParenetInvalid extends AbstractImportInventory {

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
   }


   /**
    * This test is to verify valid child inventory import. OPER-22637: Actuals Loader inventory
    * needs a new validation to ensure children are marked as invalid when their parent is invalid
    *
    *
    */

   @Test
   public void testValidChildInventory_22637() throws Exception {

      String lRandom = String.valueOf( getRandom() );
      String lACFTNo = "ACFT-" + lRandom;
      String lParentNo = "PARENT-" + lRandom;
      String lMIDNo = "MID-" + lRandom;
      String lCHILDNo = "CHILD-" + lRandom;

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // create inventory map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      // create inventory of ACFT
      lMapInventory.put( "SERIAL_NO_OEM", "'" + lACFTNo + "'" );
      lMapInventory.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventory.put( "INV_CLASS_CD", "'ACFT'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventory.put( "AC_REG_CD", "'" + lRandom + "'" );
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2017','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // create inventory of TRK parent
      Map<String, String> lMapInventoryChild = new LinkedHashMap<String, String>();
      lMapInventoryChild.put( "PARENT_SERIAL_NO_OEM", "'" + lACFTNo + "'" );
      lMapInventoryChild.put( "PARENT_PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventoryChild.put( "PARENT_MANUFACT_CD", "'10001'" );
      lMapInventoryChild.put( "BOM_PART_CD", "'ACFT-SYS-1-1-TRK-TRK-TRK-PARENT'" );
      lMapInventoryChild.put( "EQP_POS_CD", "'1.1'" );
      lMapInventoryChild.put( "SERIAL_NO_OEM", "'" + lParentNo + "'" );
      lMapInventoryChild.put( "PART_NO_OEM", "'A0000020'" );
      lMapInventoryChild.put( "MANUFACT_CD", "'10001'" );
      lMapInventoryChild.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventoryChild.put( "MANUFACT_DT", "to_date('01/01/2017','mm/dd/yyyy')" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventoryChild ) );

      // create inventory of TRK mid
      lMapInventoryChild.clear();
      lMapInventoryChild.put( "PARENT_SERIAL_NO_OEM", "'" + lACFTNo + "'" );
      lMapInventoryChild.put( "PARENT_PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventoryChild.put( "PARENT_MANUFACT_CD", "'10001'" );
      lMapInventoryChild.put( "BOM_PART_CD", "'ACFT-SYS-1-1-TRK-TRK-TRK-MID'" );
      lMapInventoryChild.put( "EQP_POS_CD", "'1.1.1'" );
      lMapInventoryChild.put( "SERIAL_NO_OEM", "'" + lMIDNo + "'" );
      lMapInventoryChild.put( "PART_NO_OEM", "'A0000021'" );
      lMapInventoryChild.put( "MANUFACT_CD", "'10001'" );
      lMapInventoryChild.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventoryChild.put( "MANUFACT_DT", "to_date('01/01/2017','mm/dd/yyyy')" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventoryChild ) );

      // create inventory of TRK child
      lMapInventoryChild.clear();
      lMapInventoryChild.put( "PARENT_SERIAL_NO_OEM", "'" + lACFTNo + "'" );
      lMapInventoryChild.put( "PARENT_PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventoryChild.put( "PARENT_MANUFACT_CD", "'10001'" );
      lMapInventoryChild.put( "BOM_PART_CD", "'ACFT-SYS-1-1-TRK-TRK-TRK-CHILD'" );
      lMapInventoryChild.put( "EQP_POS_CD", "'1.1.1.1'" );
      lMapInventoryChild.put( "SERIAL_NO_OEM", "'" + lCHILDNo + "'" );
      lMapInventoryChild.put( "PART_NO_OEM", "'A0000022'" );
      lMapInventoryChild.put( "MANUFACT_CD", "'10001'" );
      lMapInventoryChild.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventoryChild.put( "MANUFACT_DT", "to_date('01/01/2017','mm/dd/yyyy')" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventoryChild ) );

      boolean lCompleteAssmblyBool = false;
      runALValidateInventory( lCompleteAssmblyBool );

      VerifyValidation();

   }


   /**
    * This test is to verify valid child inventory import. OPER-22637: Actuals Loader inventory
    * needs a new validation to ensure children are marked as invalid when their parent is invalid
    *
    *
    */

   @Test
   public void testInvalidChildInventory_22637() throws Exception {

      String lRandom = String.valueOf( getRandom() );
      String lACFTNo = "ACFT-" + lRandom;
      String lParentNo = "PARENT-" + lRandom;
      String lMIDNo = "MID-" + lRandom;
      String lCHILDNo = "CHILD-" + lRandom;

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // create inventory map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      // create inventory of ACFT
      lMapInventory.put( "SERIAL_NO_OEM", "'" + lACFTNo + "'" );
      lMapInventory.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventory.put( "INV_CLASS_CD", "'ACFT'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventory.put( "AC_REG_CD", "'" + lRandom + "'" );
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2017','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // create inventory of TRK parent
      Map<String, String> lMapInventoryChild = new LinkedHashMap<String, String>();
      lMapInventoryChild.put( "PARENT_SERIAL_NO_OEM", "'" + lACFTNo + "'" );
      lMapInventoryChild.put( "PARENT_PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventoryChild.put( "PARENT_MANUFACT_CD", "'10001'" );
      lMapInventoryChild.put( "BOM_PART_CD", "'ACFT-SYS-1-1-TRK-TRK-TRK-PARENT'" );
      lMapInventoryChild.put( "EQP_POS_CD", "'1.1'" );
      lMapInventoryChild.put( "SERIAL_NO_OEM", "'" + lParentNo + "'" );
      lMapInventoryChild.put( "PART_NO_OEM", "'A0000090'" );
      lMapInventoryChild.put( "MANUFACT_CD", "'10001'" );
      lMapInventoryChild.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventoryChild.put( "MANUFACT_DT", "to_date('01/01/2017','mm/dd/yyyy')" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventoryChild ) );

      // create inventory of TRK mid
      lMapInventoryChild.clear();
      lMapInventoryChild.put( "PARENT_SERIAL_NO_OEM", "'" + lACFTNo + "'" );
      lMapInventoryChild.put( "PARENT_PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventoryChild.put( "PARENT_MANUFACT_CD", "'10001'" );
      lMapInventoryChild.put( "BOM_PART_CD", "'ACFT-SYS-1-1-TRK-TRK-TRK-MID'" );
      lMapInventoryChild.put( "EQP_POS_CD", "'1.1.1'" );
      lMapInventoryChild.put( "SERIAL_NO_OEM", "'" + lMIDNo + "'" );
      lMapInventoryChild.put( "PART_NO_OEM", "'A0000021'" );
      lMapInventoryChild.put( "MANUFACT_CD", "'10001'" );
      lMapInventoryChild.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventoryChild.put( "MANUFACT_DT", "to_date('01/01/2017','mm/dd/yyyy')" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventoryChild ) );

      // create inventory of TRK child
      lMapInventoryChild.clear();
      lMapInventoryChild.put( "PARENT_SERIAL_NO_OEM", "'" + lACFTNo + "'" );
      lMapInventoryChild.put( "PARENT_PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventoryChild.put( "PARENT_MANUFACT_CD", "'10001'" );
      lMapInventoryChild.put( "BOM_PART_CD", "'ACFT-SYS-1-1-TRK-TRK-TRK-CHILD'" );
      lMapInventoryChild.put( "EQP_POS_CD", "'1.1.1.1'" );
      lMapInventoryChild.put( "SERIAL_NO_OEM", "'" + lCHILDNo + "'" );
      lMapInventoryChild.put( "PART_NO_OEM", "'A0000022'" );
      lMapInventoryChild.put( "MANUFACT_CD", "'10001'" );
      lMapInventoryChild.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventoryChild.put( "MANUFACT_DT", "to_date('01/01/2017','mm/dd/yyyy')" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventoryChild ) );

      boolean lCompleteAssmblyBool = false;
      runALValidateInventory( lCompleteAssmblyBool );

      verifyErrorCodelevel();

   }


   /**
    * This function is to check error(s) for validation table is loaded
    *
    *
    */
   public void VerifyValidation() {

      int lCount = countRowsOfQuery( TestConstants.AL_PROC_INVENTORY_ERROR_CHECK_TABLE_CRITICAL );
      Assert.assertTrue( "There should be no critical error. ", lCount == 0 );

   }


   public void verifyErrorCodelevel() {

      String lqueryerrorSelection = "WITH test as ("
            + "select al_proc_inventory_error.record_id,DL_REF_MESSAGE.SEVERITY_CD "
            + " from al_proc_inventory_error "
            + " inner join DL_REF_MESSAGE on al_proc_inventory_error.error_cd=DL_REF_MESSAGE.result_cd "
            + " where DL_REF_MESSAGE.SEVERITY_CD <> 'WARNING')"
            + " select * from test where test.record_id='";

      String lqueryRecordIds = "WITH test as (" + " select record_id from AL_PROC_INVENTORY "
            + " union all "
            + " select record_id from AL_PROC_INVENTORY_SUB where proc_status_cd not like 'AUTO%') "
            + " select record_id from test";

      // Get record IDs
      String[] iIds = { "RECORD_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      List<ArrayList<String>> llistsRecords = execute( lqueryRecordIds, lfields );

      for ( int i = 0; i < llistsRecords.size(); i++ ) {
         String lid = llistsRecords.get( i ).get( 0 );
         String lquery = lqueryerrorSelection + lid + "'";
         Assert.assertTrue( "The record has error code.", RecordsExist( lquery ) );

      }

   }

}
