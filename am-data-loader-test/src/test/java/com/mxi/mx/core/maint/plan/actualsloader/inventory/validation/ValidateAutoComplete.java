package com.mxi.mx.core.maint.plan.actualsloader.inventory.validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;
import com.mxi.mx.util.AutoCompleteUtil;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.WhereClause;


/*-
 * This will verify the auto-complete is working successfully. Not all aircraft parts need to be
 * through the Actuals loader. Depending on the complete_assy_bool setting, Mx can auto-complete the
 * rest of parts, based on the baseline These tests below will verify the result.
 *
 * These test cases perform validation. These tests are generating specific error codes.
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

public class ValidateAutoComplete extends AutoCompleteUtil {

   @Rule
   public TestName testName = new TestName();

   public ArrayList<String> iClearActualsTables = new ArrayList<String>();
   {
      iClearActualsTables.add( "DELETE FROM c_ri_inventory" );
      iClearActualsTables.add( "DELETE FROM c_ri_inventory_sub" );
      iClearActualsTables.add( "DELETE FROM c_ri_inventory_usage" );
      iClearActualsTables.add( "DELETE FROM c_ri_inventory_cap_levels" );
      iClearActualsTables.add( "DELETE FROM c_ri_attach" );
      iClearActualsTables.add( "DELETE FROM Al_PROC_INVENTORY_ERROR" );
   }

   public static String iAssmblCD = "ACFT_CD1";
   public static String iAssmblBomCD = "ACFT-SYS-1-1-TRK-TRK-CHILD";


   @Override
   @Before
   public void before() throws Exception {
      super.before();
      classDataSetup( iClearActualsTables );
      if ( testName.getMethodName().contains( "ParentNotApplicable" ) ) {
         doSetup();
      }
      setupBaseline();
   }


   @After
   @Override
   public void after() throws Exception {
      restoreOriginalValues();
      if ( testName.getMethodName().contains( "ParentNotApplicable" ) ) {
         restoreData();
      }

      classDataSetup( iClearActualsTables );
      super.after();
   }


   /**
    *
    * Attempting to add a TRK (D) part to an obselete SYS(B) slot and non-mandatory TRK(C) parent.
    * with AutoComplete OFF.
    *
    * Expected Result: AML-10439 error should be returned saying that there is no parent TRK part.
    *
    * @throws Exception
    */
   @Test
   public void TestTrkTrkWithCompleteAssyBoolFalse() throws Exception {
      int lRandom = getRandom();
      String lParentSerialNo = "AIRCRAFT" + lRandom;

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
      runALValidateInventory( false );
      checkInventoryNoWarnings( "AML-10439" );

   }


   /*-
   *
   * Attempting to add a TRK (D) part to an obsolete SYS(B) slot and non-mandatory TRK(C) parent.
   * with AutoComplete ON.
   *
   * Expected Result:
   * i.  (A), (B) and (C) get auto-created in inv_inv
   * ii. (D) does not get loaded into inv_inv
   * iii.  Expected Result: AML-10439 error should be returned saying that there is no parent TRK part,
   * because it set to not applicable in the baseline
   *
   * @throws Exception
   */
   @Test
   public void TestTrkTrkWithCompleteAssyBoolTrueParentNotApplicable() throws Exception {
      int lRandom = getRandom();
      String lParentSerialNo = "AIRCRAFT" + lRandom;

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
      lMapInventoryAcft.put( "APPL_EFF_CD", "'A001'" );

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
      lMapInventoryChild.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert TRK map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventoryChild ) );

      System.out.println(
            "******* Starting validation for test " + testName.getMethodName() + " *******" );
      runALValidateInventory( true );
      checkInventoryErrorCode( testName.getMethodName(), "AML-10429" );

   }


   /**
    * This function is to retrieve bom_ids
    *
    *
    */

   public simpleIDs getAssmblBomID() {
      // MIM_DATA_TYPE table
      String[] iIds = { "ASSMBL_CD", "ASSMBL_BOM_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "ASSMBL_CD", iAssmblCD );
      lArgs.addArguments( "ASSMBL_BOM_CD", iAssmblBomCD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EQP_ASSMBL_BOM, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lIds;

   }


   /**
    * This function is called by before TestTrkTrkWithCompleteAssyBoolTrueParentNotApplicable()
    *
    *
    */

   public void doSetup() {
      simpleIDs lIDs = getAssmblBomID();
      String lquery =
            "UPDATE eqp_bom_part SET eqp_bom_part.appl_eff_ldesc = 'A002' WHERE ASSMBL_DB_ID = 4650 AND ASSMBL_CD = 'ACFT_CD1' AND ASSMBL_BOM_ID = '"
                  + lIDs.getNO_ID() + "'";
      runUpdate( lquery );

   }


   /**
    * This function is to data restore for TestTrkTrkWithCompleteAssyBoolTrueParentNotApplicable()
    *
    *
    */

   public void restoreData() {
      simpleIDs lIDs = getAssmblBomID();
      String lquery =
            "UPDATE eqp_bom_part SET eqp_bom_part.appl_eff_ldesc = NULL WHERE ASSMBL_DB_ID = 4650 AND ASSMBL_CD = 'ACFT_CD1' AND ASSMBL_BOM_ID = '"
                  + lIDs.getNO_ID() + "'";
      runUpdate( lquery );

   }

}
