package com.mxi.mx.core.maint.plan.actualsloader.inventory.imports;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.mxi.mx.util.AutoCompleteUtil;
import com.mxi.mx.util.StoreProcedureRunner;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.TestConstants;


/*-
 * This will verify the auto-complete is working successfully. Not all aircraft parts need to be
 * through the Actuals loader. Depending on the complete_assy_bool setting, Mx can auto-complete the
 * rest of parts, based on the baseline. These tests below will verify the result.
 *
 * These test cases perform  importing.  All tests are expected to pass.
 *
 * These test scenarios will be referencing the Part Letters below in the baseline
 *
 * @Before is creating this tree of Data in this order:
 *  SYS (A) (ACTV)
 *   |-> SYS (B) (Inactive)
 *        |-> TRK (C) (non-mandatory)
 *             |-> TRK (D) (mandatory)
 *
 */
public class ImportAutoComplete extends AutoCompleteUtil {

   @Rule
   public TestName testName = new TestName();

   public ArrayList<String> iClearActualsTables = new ArrayList<String>();
   {
      iClearActualsTables.add( "DELETE FROM c_ri_inventory" );
      iClearActualsTables.add( "DELETE FROM c_ri_inventory_sub" );
      iClearActualsTables.add( "DELETE FROM c_ri_inventory_usage" );
      iClearActualsTables.add( "DELETE FROM c_ri_inventory_cap_levels" );
      iClearActualsTables.add( "DELETE FROM c_ri_attach" );
   }


   @Override
   @Before
   public void before() throws Exception {
      super.before();
      clearActualsLoaderTables();
      // classDataSetup( iClearActualsTables );

      String lbomId = getID( "ACFT-SYS-1-1-TRK-SRU" );

      if ( testName.getMethodName().contains( "ParentNotApplicable" ) ) {
         runUpdate(
               "UPDATE eqp_bom_part SET eqp_bom_part.appl_eff_ldesc = 'A002' WHERE ASSMBL_DB_ID = 4650 AND ASSMBL_CD = 'ACFT_CD1' AND ASSMBL_BOM_ID = "
                     + lbomId );
      }
      setupBaseline();
   }


   public String getID( String aCd ) {
      String lquery =
            "Select ASSMBL_BOM_ID from eqp_bom_part where bom_part_cd='ACFT-SYS-1-1-TRK-SRU'";
      String lbomId = getStringValueFromQuery( lquery, "ASSMBL_BOM_ID" );
      return lbomId;

   }


   @After
   @Override
   public void after() throws Exception {
      String lbomId = getID( "ACFT-SYS-1-1-TRK-SRU" );

      restoreOriginalValues();
      if ( testName.getMethodName().contains( "ParentNotApplicable" ) ) {
         runUpdate(
               "UPDATE eqp_bom_part SET eqp_bom_part.appl_eff_ldesc = NULL WHERE ASSMBL_DB_ID = 4650 AND ASSMBL_CD = 'ACFT_CD1' AND ASSMBL_BOM_ID = "
                     + lbomId );
      }
      classDataSetup( iClearActualsTables );
      super.after();
   }


   /*-
    *
    * Test will Import only aircraft with Auto-complete off We expected
    * i. (A) Gets auto-created in inv_inv
    * ii. (B), (C) and (D) do not get auto-created
    * iii. No error messages
    *
    */
   @Test
   public void TestSysWithCompleteAssyBoolFalse() throws Exception {
      int lRandom = getRandom();
      // String lSerialNo = "AIRCRAFT" + lRandom;
      String lSerialNo = "AIRCRAFT001";

      // create Aircraft map
      Map<String, String> lMapInventoryAcft = new LinkedHashMap<String, String>();
      lMapInventoryAcft.put( "SERIAL_NO_OEM", "'" + lSerialNo + "'" );
      lMapInventoryAcft.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventoryAcft.put( "INV_CLASS_CD", "'ACFT'" );
      lMapInventoryAcft.put( "MANUFACT_CD", "'10001'" );;
      lMapInventoryAcft.put( "LOC_CD", "'OPS'" );
      lMapInventoryAcft.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventoryAcft.put( "ASSMBL_CD", "'ACFT_CD1'" );
      lMapInventoryAcft.put( "AC_REG_CD", "'Q" + lRandom + "'" );
      lMapInventoryAcft.put( "INV_OPER_CD", "'NORM'" );
      lMapInventoryAcft.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventoryAcft.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert Aircraft map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventoryAcft ) );

      System.out.println(
            "******* Starting validation for test " + testName.getMethodName() + " *******" );

      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      checkInventoryNoWarnings( "PASS" );

      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), false, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      Assert.assertTrue( "SYS Part A does not exist", doesPartExist( iBlSysAPart ) == true );
      Assert.assertTrue( "SYS Part B exists when it shouldn't",
            doesPartExist( iBlSysBPart ) == false );
      Assert.assertTrue( "TRK Part D exists when it shouldn't",
            doesPartExist( iBlTrkCPart ) == false );
      Assert.assertTrue( "TRK Part C exists when it shouldn't",
            doesPartExist( iBlTrkDPart ) == false );
   }


   /*-
    *
    * Test will Import only aircraft with Auto-complete off We expected
    * i. (A) Gets auto-created in inv_inv
    * ii. (B), (C) and (D) do not get auto-created inv_inv
    * iii. No error messages
    *
    */
   @Test
   public void TestSysWithCompleteAssyBoolTrue() throws Exception {
      int lRandom = getRandom();
      // String lSerialNo = "AIRCRAFT" + lRandom;
      String lSerialNo = "AIRCRAFT002";

      // create Aircraft map0
      Map<String, String> lMapInventoryAcft = new LinkedHashMap<String, String>();
      lMapInventoryAcft.put( "SERIAL_NO_OEM", "'" + lSerialNo + "'" );
      lMapInventoryAcft.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventoryAcft.put( "INV_CLASS_CD", "'ACFT'" );
      lMapInventoryAcft.put( "MANUFACT_CD", "'10001'" );;
      lMapInventoryAcft.put( "LOC_CD", "'OPS'" );
      lMapInventoryAcft.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventoryAcft.put( "ASSMBL_CD", "'ACFT_CD1'" );
      lMapInventoryAcft.put( "AC_REG_CD", "'Q" + lRandom + "'" );
      lMapInventoryAcft.put( "INV_OPER_CD", "'NORM'" );
      lMapInventoryAcft.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventoryAcft.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert Aircraft map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventoryAcft ) );
      System.out.println(
            "******* Starting validation for test " + testName.getMethodName() + " *******" );

      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "FULL", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      checkInventoryNoWarnings( "PASS" );

      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), false, true, "FULL", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      Assert.assertTrue( "SYS Part A does not exist", doesPartExist( iBlSysAPart ) == true );
      Assert.assertTrue( "SYS Part B exists when it shouldn't",
            doesPartExist( iBlSysBPart ) == false );
      Assert.assertTrue( "TRK Part C exists when it shouldn't",
            doesPartExist( iBlTrkCPart ) == false );
      Assert.assertTrue( "TRK Part D exists when it shouldn't",
            doesPartExist( iBlTrkDPart ) == false );
   }


   /*-
   *
   * Test will Import an aircraft with Auto-complete off and TRK(C) i.  We expected:
   *i.   (A) and (B) get auto-created in inv_inv
   *ii.  (C) and (D) do not get auto-created in inv_inv
   *iii. No error messages
   *
   */
   @Test
   public void TestTrkWithCompleteAssyBoolFalse() throws Exception {
      int lRandom = getRandom();
      String lParentSerialNo = "AIRCRAFT003";
      String lAnotherSerialNo = "PART001";

      // create Aircraft map
      Map<String, String> lMapInventoryAcft = new LinkedHashMap<String, String>();
      lMapInventoryAcft.put( "SERIAL_NO_OEM", "'" + lParentSerialNo + "'" );
      lMapInventoryAcft.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventoryAcft.put( "INV_CLASS_CD", "'ACFT'" );
      lMapInventoryAcft.put( "MANUFACT_CD", "'10001'" );;
      lMapInventoryAcft.put( "LOC_CD", "'OPS'" );
      lMapInventoryAcft.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventoryAcft.put( "ASSMBL_CD", "'ACFT_CD1'" );
      lMapInventoryAcft.put( "AC_REG_CD", "'Q" + lRandom + "'" );
      lMapInventoryAcft.put( "INV_OPER_CD", "'NORM'" );
      lMapInventoryAcft.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventoryAcft.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert Aircraft map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventoryAcft ) );

      // create TRK map
      Map<String, String> lMapInventorySubTrk = new LinkedHashMap<String, String>();
      lMapInventorySubTrk.put( "PARENT_SERIAL_NO_OEM", "'" + lParentSerialNo + "'" );
      lMapInventorySubTrk.put( "PARENT_PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventorySubTrk.put( "PARENT_MANUFACT_CD", "'10001'" );
      lMapInventorySubTrk.put( "BOM_PART_CD", "'ACFT-SYS-1-1-TRK-TRK-PARENT'" );
      lMapInventorySubTrk.put( "EQP_POS_CD", "'1'" );
      lMapInventorySubTrk.put( "SERIAL_NO_OEM", "'" + lAnotherSerialNo + "'" );
      lMapInventorySubTrk.put( "PART_NO_OEM", "'A0000005'" );
      lMapInventorySubTrk.put( "MANUFACT_CD", "'10001'" );
      lMapInventorySubTrk.put( "INV_CLASS_CD", "'TRK'" );

      // insert TRK map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventorySubTrk ) );
      System.out.println(
            "******* Starting validation for test " + testName.getMethodName() + " *******" );

      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      checkInventoryNoWarnings( "PASS" );

      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), false, true, "NOT", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      Assert.assertTrue( "SYS Part A does not exist", doesPartExist( iBlSysAPart ) == true );
      Assert.assertTrue( "SYS Part B does not exist", doesPartExist( iBlSysBPart ) == true );
      Assert.assertTrue( "TRK Part C does not exist", doesPartExist( iBlTrkCPart ) == true );
      Assert.assertTrue( "TRK Part D exists when it shouldn't",
            doesPartExist( iBlTrkDPart ) == false );
   }


   /*-
   *
   * Test will Import only aircraft and TRK part (C) with Auto-complete ON.  We expected:
   *i.   (A), (B), and (D) get auto-created in
   *ii.   (C) To be loaded successfully.
   *iii.  No error messages to occur
   *
   */

   @Test
   public void TestTrkWithCompleteAssyBoolTrue() throws Exception {
      int lRandom = getRandom();
      String lParentSerialNo = "AIRCRAFT004";
      String lAnotherSerialNo = "PART002";

      // create Aircraft map
      Map<String, String> lMapInventoryAcft = new LinkedHashMap<String, String>();
      lMapInventoryAcft.put( "SERIAL_NO_OEM", "'" + lParentSerialNo + "'" );
      lMapInventoryAcft.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventoryAcft.put( "INV_CLASS_CD", "'ACFT'" );
      lMapInventoryAcft.put( "MANUFACT_CD", "'10001'" );;
      lMapInventoryAcft.put( "LOC_CD", "'OPS'" );
      lMapInventoryAcft.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventoryAcft.put( "ASSMBL_CD", "'ACFT_CD1'" );
      lMapInventoryAcft.put( "AC_REG_CD", "'Q" + lRandom + "'" );
      lMapInventoryAcft.put( "INV_OPER_CD", "'NORM'" );
      lMapInventoryAcft.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventoryAcft.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert Aircraft map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventoryAcft ) );

      // create TRK map
      Map<String, String> lMapInventorySubTrk = new LinkedHashMap<String, String>();
      lMapInventorySubTrk.put( "PARENT_SERIAL_NO_OEM", "'" + lParentSerialNo + "'" );
      lMapInventorySubTrk.put( "PARENT_PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventorySubTrk.put( "PARENT_MANUFACT_CD", "'10001'" );
      lMapInventorySubTrk.put( "BOM_PART_CD", "'ACFT-SYS-1-1-TRK-TRK-PARENT'" );
      lMapInventorySubTrk.put( "EQP_POS_CD", "'1'" );
      lMapInventorySubTrk.put( "SERIAL_NO_OEM", "'" + lAnotherSerialNo + "'" );
      lMapInventorySubTrk.put( "PART_NO_OEM", "'A0000005'" );
      lMapInventorySubTrk.put( "MANUFACT_CD", "'10001'" );
      lMapInventorySubTrk.put( "INV_CLASS_CD", "'TRK'" );

      // insert TRK map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventorySubTrk ) );
      System.out.println(
            "******* Starting validation for test " + testName.getMethodName() + " *******" );

      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "FULL", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      checkInventoryNoWarnings( "PASS" );

      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), false, true, "FULL", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      Assert.assertTrue( "SYS Part A does not exist", doesPartExist( iBlSysAPart ) == true );
      Assert.assertTrue( "SYS Part B does not exist", doesPartExist( iBlSysBPart ) == true );
      Assert.assertTrue( "TRK Part C does not exist", doesPartExist( iBlTrkCPart ) == true );
      Assert.assertTrue( "TRK Part D does not exist", doesPartExist( iBlTrkDPart ) == true );

   }


   /*-
   *
   * Attempting to add a TRK (D) part to an obselete SYS(B) slot and non-mandatory TRK(C) parent.
   * with AutoComplete ON.
   *
   * Expected Result:
   * i.  (A), (B) and (C) get auto-created in inv_inv
   * ii. (D) does not get loaded into inv_inv
   * iii.   No error messages
   *
   * @throws Exception
   */
   @Test
   public void TestTrkTrkWithCompleteAssyBoolTrue() throws Exception {
      int lRandom = getRandom();
      String lParentSerialNo = "AIRCRAFT005";

      // create Aircraft map
      Map<String, String> lMapInventoryAcft = new LinkedHashMap<String, String>();
      lMapInventoryAcft.put( "SERIAL_NO_OEM", "'" + lParentSerialNo + "'" );
      lMapInventoryAcft.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventoryAcft.put( "INV_CLASS_CD", "'ACFT'" );
      lMapInventoryAcft.put( "MANUFACT_CD", "'10001'" );;
      lMapInventoryAcft.put( "LOC_CD", "'OPS'" );
      lMapInventoryAcft.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventoryAcft.put( "ASSMBL_CD", "'ACFT_CD1'" );
      lMapInventoryAcft.put( "AC_REG_CD", "'Q" + lRandom + "'" );
      lMapInventoryAcft.put( "INV_OPER_CD", "'NORM'" );
      lMapInventoryAcft.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventoryAcft.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert Aircraft map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventoryAcft ) );

      // create TRK child map
      Map<String, String> lMapInventoryChild = new LinkedHashMap<String, String>();
      lMapInventoryChild.put( "PARENT_SERIAL_NO_OEM", "'" + lParentSerialNo + "'" );
      lMapInventoryChild.put( "PARENT_PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventoryChild.put( "PARENT_MANUFACT_CD", "'10001'" );
      lMapInventoryChild.put( "BOM_PART_CD", "'ACFT-SYS-1-1-TRK-TRK-CHILD'" );
      lMapInventoryChild.put( "EQP_POS_CD", "'1'" );
      lMapInventoryChild.put( "SERIAL_NO_OEM", "'XXX'" );
      lMapInventoryChild.put( "PART_NO_OEM", "'A0000006'" );
      lMapInventoryChild.put( "MANUFACT_CD", "'11111'" );
      lMapInventoryChild.put( "INV_CLASS_CD", "'TRK'" );

      // insert TRK map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventoryChild ) );

      System.out.println(
            "******* Starting validation for test " + testName.getMethodName() + " *******" );

      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, "FULL", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      checkInventoryNoWarnings( "PASS" );

      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), false, true, "FULL", TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );

      Assert.assertTrue( "SYS Part A does not exist", doesPartExist( iBlSysAPart ) == true );
      Assert.assertTrue( "SYS Part B does not exist", doesPartExist( iBlSysBPart ) == true );
      Assert.assertTrue( "TRK Part C does not exist", doesPartExist( iBlTrkCPart ) == true );
      Assert.assertTrue( "TRK Part D does not exist", doesPartExist( iBlTrkDPart ) == true );

   }

}
