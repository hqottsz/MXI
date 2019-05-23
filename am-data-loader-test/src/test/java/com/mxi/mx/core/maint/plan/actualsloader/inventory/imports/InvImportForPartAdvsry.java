package com.mxi.mx.core.maint.plan.actualsloader.inventory.imports;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;
import com.mxi.mx.util.ActualsLoaderTest;
import com.mxi.mx.util.TableUtil;


/**
 * OPER-24196 - this will test inventory imported that already exists in eqp_Part_advsry table
 *
 */
public class InvImportForPartAdvsry extends ActualsLoaderTest {

   public static simpleIDs iAdvsryIds = null;
   private static String iAcftSerialNo = null;
   private static String iSerialNoOem = null;
   private simpleIDs iSerialIds = null;
   private final String iPartNoOem = "'A0000001'";
   private final String iSerialRange = "'\"876000\"-\"876999\"'";
   private final String iLotRange = "'\"350\"-\"355\"'";
   private final String iSerialQuery = "inv_inv.serial_no_oem BETWEEN '876000' AND '876999'";
   private final String iLotQuery = "inv_inv.lot_oem_tag BETWEEN '350' AND '355'";
   private simpleIDs iPartIds;

   @Rule
   public TestName testName = new TestName();


   @Override
   @Before
   public void before() throws Exception {
      super.before();
      clearActualsLoaderTables();
      dataSetup();
   }


   @Override
   @After
   public void after() throws Exception {

      cleanImportedData();
      super.after();
   }


   /**
    * Cleans data from the following tables: inv_advsry, inv_inv, eqp_advsry, eqp_part_advsry
    *
    */
   private void cleanImportedData() {
      if ( iSerialIds != null )
         runUpdate( "Delete from inv_advsry where inv_no_id = " + iSerialIds.getNO_ID() );
      if ( iAcftSerialNo != null )
         runUpdate( "Delete from inv_inv where serial_no_oem = " + iAcftSerialNo
               + " OR serial_no_oem = " + iSerialNoOem );
      runUpdate( "Delete from eqp_part_advsry where advsry_id = " + iAdvsryIds.getNO_ID() );
      runUpdate( "Delete from eqp_advsry where advsry_id = " + iAdvsryIds.getNO_ID() );
   }


   /**
    *
    * Needs entries in eqp_advsry, and eqp_part_advsry prior running tests
    *
    */
   public void dataSetup() {
      // Entry for eqp_advsry
      iAdvsryIds = new simpleIDs( "4650",
            String.valueOf( getNextValueInSequence( "eqp_advsry_id_seq" ) ) );
      String lQuery =
            "Select HR_DB_ID, HR_ID from UTL_USER INNER JOIN ORG_HR ON UTL_USER.USER_ID = org_hr.user_id"
                  + " where USERNAME = 'ADMIN'";
      simpleIDs lHrIds = getIDs( lQuery, "HR_DB_ID", "HR_ID" );
      lQuery =
            "INSERT INTO eqp_advsry(advsry_db_id,advsry_id,advsry_name,advsry_dt,advsry_hr_db_id,advsry_hr_id,advsry_type_db_id,advsry_type_cd,advsry_note)"
                  + " VALUES (4650," + iAdvsryIds.getNO_ID() + ",'AUTO_TEST',SYSDATE,"
                  + lHrIds.getNO_DB_ID() + "," + lHrIds.getNO_ID() + ",10,'SUP', 'dataSetup')";
      runUpdate( lQuery );

      // Entry for eqp_part_advsry
      lQuery =
            "SELECT Part_no_db_id, Part_no_id FROM eqp_part_no WHERE part_no_oem = " + iPartNoOem;
      iPartIds = getIDs( lQuery, "PART_NO_DB_ID", "PART_NO_ID" );
      lQuery =
            "INSERT INTO eqp_part_advsry (part_no_db_id, part_no_id, advsry_db_id, advsry_id, active_bool, serial_no_range, lot_no_range) "
                  + " VALUES (" + iPartIds.getNO_DB_ID() + "," + iPartIds.getNO_ID() + ",4650,"
                  + iAdvsryIds.getNO_ID() + ", 1, " + iSerialRange + ", " + iLotRange + ")";
      runUpdate( lQuery );
   }


   /**
    *
    * This will test Serial Number within the Serial No range and it is expected to generate an
    * entry in the inv_advsry table
    *
    * @throws Exception
    */
   @Test
   public void testTrkPartWithinSerialRange() throws Exception {

      System.out.println( "======== Starting " + testName.getMethodName() + " =======" );
      int lRandom = getRandom();

      iAcftSerialNo = "'ACFT-7879AQ'";
      iSerialNoOem = "'876999'";

      // create inv map
      Map<String, String> lMapInventoryAcft = new LinkedHashMap<String, String>();
      lMapInventoryAcft.put( "SERIAL_NO_OEM", iAcftSerialNo );
      lMapInventoryAcft.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventoryAcft.put( "INV_CLASS_CD", "'ACFT'" );
      lMapInventoryAcft.put( "MANUFACT_CD", "'10001'" );;
      lMapInventoryAcft.put( "LOC_CD", "'OPS'" );
      lMapInventoryAcft.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventoryAcft.put( "ASSMBL_CD", "'ACFT_CD1'" );
      lMapInventoryAcft.put( "AC_REG_CD", "'" + lRandom + "'" );
      lMapInventoryAcft.put( "INV_OPER_CD", "'NORM'" );
      lMapInventoryAcft.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventoryAcft.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert Aircraft map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventoryAcft ) );

      Map<String, String> lMapInventoryChild = new LinkedHashMap<String, String>();
      lMapInventoryChild.put( "PARENT_SERIAL_NO_OEM", iAcftSerialNo );
      lMapInventoryChild.put( "PARENT_PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventoryChild.put( "PARENT_MANUFACT_CD", "'10001'" );
      lMapInventoryChild.put( "BOM_PART_CD", "'ACFT-SYS-1-1-TRK-P1'" );
      lMapInventoryChild.put( "EQP_POS_CD", "'1'" );
      lMapInventoryChild.put( "SERIAL_NO_OEM", iSerialNoOem );
      lMapInventoryChild.put( "PART_NO_OEM", iPartNoOem );
      lMapInventoryChild.put( "MANUFACT_CD", "'10001'" );
      lMapInventoryChild.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventoryChild.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert inv map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventoryChild ) );

      runALValidateInventory( false );
      checkInventoryNoWarnings( "PASS" );
      runImportStageInventory( false ); // complete.assy.bool set to 'NOT'

      // Verify an Entry is generated in Inv_advsry
      Assert.assertTrue( "Verify values in INV_ADVSRY: ", verifyInvAdvsry( iSerialNoOem ) == 1 );
   }


   /**
    *
    * This will test Serial Number outside the Serial No range and it is expected to have no entry
    * in the inv_advsry table
    *
    * @throws Exception
    */
   @Test
   public void testTrkPartOutsideSerialRange() throws Exception {

      System.out.println( "======== Starting " + testName.getMethodName() + " =======" );

      int lRandom = getRandom();

      iAcftSerialNo = "'ACFT-7879AQ'";
      iSerialNoOem = "'877000'";

      // create inv map
      Map<String, String> lMapInventoryAcft = new LinkedHashMap<String, String>();
      lMapInventoryAcft.put( "SERIAL_NO_OEM", iAcftSerialNo );
      lMapInventoryAcft.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventoryAcft.put( "INV_CLASS_CD", "'ACFT'" );
      lMapInventoryAcft.put( "MANUFACT_CD", "'10001'" );;
      lMapInventoryAcft.put( "LOC_CD", "'OPS'" );
      lMapInventoryAcft.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventoryAcft.put( "ASSMBL_CD", "'ACFT_CD1'" );
      lMapInventoryAcft.put( "AC_REG_CD", "'" + lRandom + "'" );
      lMapInventoryAcft.put( "INV_OPER_CD", "'NORM'" );
      lMapInventoryAcft.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventoryAcft.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert Aircraft map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventoryAcft ) );

      Map<String, String> lMapInventoryChild = new LinkedHashMap<String, String>();
      lMapInventoryChild.put( "PARENT_SERIAL_NO_OEM", iAcftSerialNo );
      lMapInventoryChild.put( "PARENT_PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventoryChild.put( "PARENT_MANUFACT_CD", "'10001'" );
      lMapInventoryChild.put( "BOM_PART_CD", "'ACFT-SYS-1-1-TRK-P1'" );
      lMapInventoryChild.put( "EQP_POS_CD", "'1'" );
      lMapInventoryChild.put( "SERIAL_NO_OEM", iSerialNoOem );
      lMapInventoryChild.put( "PART_NO_OEM", iPartNoOem );
      lMapInventoryChild.put( "MANUFACT_CD", "'10001'" );
      lMapInventoryChild.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventoryChild.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert inv map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventoryChild ) );

      runALValidateInventory( false );
      checkInventoryNoWarnings( "PASS" );
      runImportStageInventory( false ); // complete.assy.bool set to 'NOT'

      // Verify No Entry is generated in Inv_advsry
      Assert.assertTrue( "Verify values in INV_ADVSRY: ", verifyInvAdvsry( iSerialNoOem ) == 0 );
   }


   /**
    *
    * This will test the Lot_OEM_Tag outside the Lot range and it is expected to have no entry in
    * the inv_advsry table
    *
    * @throws Exception
    */
   @Test
   public void testTrkPartOutsideLotRange() throws Exception {

      System.out.println( "======== Starting " + testName.getMethodName() + " =======" );

      int lRandom = getRandom();

      iAcftSerialNo = "'ACFT-7879AQ'";
      iSerialNoOem = "'888777'";
      String lLotOemTag = "'356'";

      // create inv map
      Map<String, String> lMapInventoryAcft = new LinkedHashMap<String, String>();
      lMapInventoryAcft.put( "SERIAL_NO_OEM", iAcftSerialNo );
      lMapInventoryAcft.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventoryAcft.put( "INV_CLASS_CD", "'ACFT'" );
      lMapInventoryAcft.put( "MANUFACT_CD", "'10001'" );;
      lMapInventoryAcft.put( "LOC_CD", "'OPS'" );
      lMapInventoryAcft.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventoryAcft.put( "ASSMBL_CD", "'ACFT_CD1'" );
      lMapInventoryAcft.put( "AC_REG_CD", "'" + lRandom + "'" );
      lMapInventoryAcft.put( "INV_OPER_CD", "'NORM'" );
      lMapInventoryAcft.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventoryAcft.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert Aircraft map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventoryAcft ) );

      Map<String, String> lMapInventoryChild = new LinkedHashMap<String, String>();
      lMapInventoryChild.put( "PARENT_SERIAL_NO_OEM", iAcftSerialNo );
      lMapInventoryChild.put( "PARENT_PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventoryChild.put( "PARENT_MANUFACT_CD", "'10001'" );
      lMapInventoryChild.put( "BOM_PART_CD", "'ACFT-SYS-1-1-TRK-P1'" );
      lMapInventoryChild.put( "EQP_POS_CD", "'1'" );
      lMapInventoryChild.put( "SERIAL_NO_OEM", iSerialNoOem );
      lMapInventoryChild.put( "PART_NO_OEM", iPartNoOem );
      lMapInventoryChild.put( "MANUFACT_CD", "'10001'" );
      lMapInventoryChild.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventoryChild.put( "LOT_OEM_TAG", lLotOemTag );
      lMapInventoryChild.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert inv map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventoryChild ) );

      runALValidateInventory( false );
      checkInventoryNoWarnings( "PASS" );
      runImportStageInventory( false ); // complete.assy.bool set to 'NOT'

      // Verify No Entry is generated in Inv_advsry
      Assert.assertTrue( "Verify values in INV_ADVSRY: ", verifyInvAdvsry( iSerialNoOem ) == 0 );
   }


   /**
    *
    * This will test the Lot_OEM_Tag within the Lot range and it is expected to generate an entry in
    * the inv_advsry table
    *
    * @throws Exception
    */
   @Test
   public void testTrkPartWithinLotRange() throws Exception {

      System.out.println( "======== Starting " + testName.getMethodName() + " =======" );

      int lRandom = getRandom();

      iAcftSerialNo = "'ACFT-7879AQ'";
      iSerialNoOem = "'888777'";
      String lLotOemTag = "'355'";

      // create inv map
      Map<String, String> lMapInventoryAcft = new LinkedHashMap<String, String>();
      lMapInventoryAcft.put( "SERIAL_NO_OEM", iAcftSerialNo );
      lMapInventoryAcft.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventoryAcft.put( "INV_CLASS_CD", "'ACFT'" );
      lMapInventoryAcft.put( "MANUFACT_CD", "'10001'" );;
      lMapInventoryAcft.put( "LOC_CD", "'OPS'" );
      lMapInventoryAcft.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventoryAcft.put( "ASSMBL_CD", "'ACFT_CD1'" );
      lMapInventoryAcft.put( "AC_REG_CD", "'" + lRandom + "'" );
      lMapInventoryAcft.put( "INV_OPER_CD", "'NORM'" );
      lMapInventoryAcft.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventoryAcft.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert Aircraft map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventoryAcft ) );

      Map<String, String> lMapInventoryChild = new LinkedHashMap<String, String>();
      lMapInventoryChild.put( "PARENT_SERIAL_NO_OEM", iAcftSerialNo );
      lMapInventoryChild.put( "PARENT_PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventoryChild.put( "PARENT_MANUFACT_CD", "'10001'" );
      lMapInventoryChild.put( "BOM_PART_CD", "'ACFT-SYS-1-1-TRK-P1'" );
      lMapInventoryChild.put( "EQP_POS_CD", "'1'" );
      lMapInventoryChild.put( "SERIAL_NO_OEM", iSerialNoOem );
      lMapInventoryChild.put( "PART_NO_OEM", iPartNoOem );
      lMapInventoryChild.put( "MANUFACT_CD", "'10001'" );
      lMapInventoryChild.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventoryChild.put( "LOT_OEM_TAG", lLotOemTag );
      lMapInventoryChild.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert inv map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventoryChild ) );

      runALValidateInventory( false );
      checkInventoryNoWarnings( "PASS" );
      runImportStageInventory( false ); // complete.assy.bool set to 'NOT'

      // Verify No Entry is generated in Inv_advsry
      Assert.assertTrue( "Verify values in INV_ADVSRY: ", verifyInvAdvsry( iSerialNoOem ) == 1 );
   }


   /**
    *
    * Verify the INV_ADVSRY table values
    *
    * @param lSerialNoOem
    *           uses Serial No to uniquely identify the inventory that was imported
    * @return number of rows with particular values populated.
    */
   public int verifyInvAdvsry( String lSerialNoOem ) {

      String lQuery = "Select * from inv_inv where serial_no_oem = " + lSerialNoOem;
      iSerialIds = getIDs( lQuery, "INV_NO_DB_ID", "INV_NO_ID" );

      lQuery = "Select Count(*) from INV_ADVSRY where inv_no_db_id = " + iSerialIds.getNO_DB_ID()
            + " AND inv_no_id = " + iSerialIds.getNO_ID() + " AND ADVSRY_DB_ID = "
            + iAdvsryIds.getNO_DB_ID() + " AND ADVSRY_ID = " + iAdvsryIds.getNO_ID()
            + " And active_bool = 1";
      return countRowsOfQuery( lQuery );
   }

}
