package com.mxi.mx.core.maint.plan.baselineloader.SENSITIVITY;

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

import com.mxi.mx.core.maint.plan.datamodels.AssmblBomID;
import com.mxi.mx.core.maint.plan.datamodels.bomPartPN;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.WhereClause;


/**
 * This test suite contains test cases on validation and import functionality of
 * BL_SENSITIVITY_IMPORT package.
 *
 * @author ALICIA QIAN
 */
public class SensitivityImport extends Sensitivity {

   @Rule
   public TestName testName = new TestName();


   /**
    * Setup before executing each individual test
    *
    */
   @Before
   @Override
   public void before() throws Exception {

      super.before();
      clearBaselineLoaderTables();

   }


   /**
    * Clean up after each individual test
    */
   @After
   @Override
   public void after() {
      RestoreData();
      super.after();
   }


   /**
    * This test is to verifyBL_SENSITIVITY_IMPORT validation functionality of staging table
    * BL_SENSITIVITY_SYS , on sys slot of acft
    *
    */

   public void BL_SENSITIVITY_SYS_ACFT_VALIDATION() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_assign_parts_to_stock table
      Map<String, String> lSens = new LinkedHashMap<>();

      lSens.put( "ASSMBL_CD", "\'" + iAssmblCD_ACFT + "\'" );
      lSens.put( "CONFIG_SLOT_CD", "\'" + iCONFIG_SLOT_CD_ACFT + "\'" );
      lSens.put( "SENSITIVITY_CD", "\'" + iSENSITIVITY_CD + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_SENSITIVITY_SYS, lSens ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verifyBL_SENSITIVITY_IMPORT import functionality of staging table
    * BL_SENSITIVITY_SYS , on sys slot of acft
    *
    */

   @Test
   public void BL_SENSITIVITY_SYS_ACFT_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      BL_SENSITIVITY_SYS_ACFT_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify EQP_ASSMBL_BOM_SENS table
      AssmblBomID lBomIds = getAssmblBomId( iAssmblCD_ACFT, iCONFIG_SLOT_CD_ACFT );
      VerifyEQP_ASSMBL_BOM_SENS( lBomIds, iSENSITIVITY_CD );

      // Verify EQP_ASSMBL_SENS table
      VerifyEQP_ASSMBL_SENS( lBomIds, iSENSITIVITY_CD );

   }


   /**
    * This test is to verifyBL_SENSITIVITY_IMPORT validation functionality of staging table
    * BL_SENSITIVITY_SYS , on sys slot of eng
    *
    */

   public void BL_SENSITIVITY_SYS_ENG_VALIDATION() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_assign_parts_to_stock table
      Map<String, String> lSens = new LinkedHashMap<>();

      lSens.put( "ASSMBL_CD", "\'" + iAssmblCD_ENG + "\'" );
      lSens.put( "CONFIG_SLOT_CD", "\'" + iCONFIG_SLOT_CD_ENG + "\'" );
      lSens.put( "SENSITIVITY_CD", "\'" + iSENSITIVITY_CD + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_SENSITIVITY_SYS, lSens ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verifyBL_SENSITIVITY_IMPORT import functionality of staging table
    * BL_SENSITIVITY_SYS , on sys slot of eng
    *
    */
   @Test
   public void BL_SENSITIVITY_SYS_ENG_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      BL_SENSITIVITY_SYS_ENG_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify EQP_ASSMBL_BOM_SENS table
      AssmblBomID lBomIds = getAssmblBomId( iAssmblCD_ENG, iCONFIG_SLOT_CD_ENG );
      VerifyEQP_ASSMBL_BOM_SENS( lBomIds, iSENSITIVITY_CD );

      // Verify EQP_ASSMBL_SENS table
      VerifyEQP_ASSMBL_SENS( lBomIds, iSENSITIVITY_CD );

   }


   /**
    * This test is to verifyBL_SENSITIVITY_IMPORT validation functionality of staging table
    * BL_SENSITIVITY_SYS , on sys slots of eng and acft
    *
    */

   public void BL_SENSITIVITY_SYS_multiple_VALIDATION() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      BL_SENSITIVITY_SYS_ACFT_VALIDATION();
      BL_SENSITIVITY_SYS_ENG_VALIDATION();

   }


   /**
    * This test is to verifyBL_SENSITIVITY_IMPORT import functionality of staging table
    * BL_SENSITIVITY_SYS , on sys slots of eng and acft
    *
    */
   @Test
   public void BL_SENSITIVITY_SYS_multiple_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      BL_SENSITIVITY_SYS_multiple_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify EQP_ASSMBL_BOM_SENS table
      AssmblBomID lBomIds_ACFT = getAssmblBomId( iAssmblCD_ACFT, iCONFIG_SLOT_CD_ACFT );
      VerifyEQP_ASSMBL_BOM_SENS( lBomIds_ACFT, iSENSITIVITY_CD );

      AssmblBomID lBomIds_ENG = getAssmblBomId( iAssmblCD_ENG, iCONFIG_SLOT_CD_ENG );
      VerifyEQP_ASSMBL_BOM_SENS( lBomIds_ENG, iSENSITIVITY_CD );

      // Verify EQP_ASSMBL_SENS table
      VerifyEQP_ASSMBL_SENS( lBomIds_ACFT, iSENSITIVITY_CD );
      VerifyEQP_ASSMBL_SENS( lBomIds_ENG, iSENSITIVITY_CD );

   }


   /**
    * This test is to verifyBL_SENSITIVITY_IMPORT validation functionality of staging table
    * BL_SENSITIVITY_PG , on trk slot of acft
    *
    */

   public void BL_SENSITIVITY_PG_TRK_VALIDATION() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_assign_parts_to_stock table
      Map<String, String> lSens = new LinkedHashMap<>();

      lSens.put( "ASSMBL_CD", "\'" + iAssmblCD_ACFT + "\'" );
      lSens.put( "PART_GROUP_CD", "\'" + iPART_GROUP_CD_TRK + "\'" );
      lSens.put( "SENSITIVITY_CD", "\'" + iSENSITIVITY_CD + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_SENSITIVITY_PG, lSens ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verifyBL_SENSITIVITY_IMPORT import functionality of staging table
    * BL_SENSITIVITY_PG , on trk slot of acft
    *
    */
   @Test
   public void BL_SENSITIVITY_PG_TRK_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      BL_SENSITIVITY_PG_TRK_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify EQP_BOM_PART_SENS table
      bomPartPN lBomPartIds = getPartGroup( iAssmblCD_ACFT, iPART_GROUP_CD_TRK );
      VerifyEQP_BOM_PART_SENS( lBomPartIds, iSENSITIVITY_CD );

      // Verify EQP_ASSMBL_SENS table
      AssmblBomID lBomIds = new AssmblBomID( Integer.toString( CONS_DB_ID ), iAssmblCD_ACFT, null );
      VerifyEQP_ASSMBL_SENS( lBomIds, iSENSITIVITY_CD );

   }


   /**
    * This test is to verifyBL_SENSITIVITY_IMPORT validation functionality of staging table
    * BL_SENSITIVITY_PG , on ASSY (APU) slot of acft
    *
    */

   public void BL_SENSITIVITY_PG_ASSY_VALIDATION() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_assign_parts_to_stock table
      Map<String, String> lSens = new LinkedHashMap<>();

      lSens.put( "ASSMBL_CD", "\'" + iAssmblCD_ACFT + "\'" );
      lSens.put( "PART_GROUP_CD", "\'" + iPART_GROUP_CD_ASSY + "\'" );
      lSens.put( "SENSITIVITY_CD", "\'" + iSENSITIVITY_CD_2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_SENSITIVITY_PG, lSens ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verifyBL_SENSITIVITY_IMPORT import functionality of staging table
    * BL_SENSITIVITY_PG , on ASSY (APU) slot of acft
    *
    */
   @Test
   public void BL_SENSITIVITY_PG_ASSY_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      BL_SENSITIVITY_PG_ASSY_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify EQP_BOM_PART_SENS table
      bomPartPN lBomPartIds = getPartGroup( iAssmblCD_ACFT, iPART_GROUP_CD_ASSY );
      VerifyEQP_BOM_PART_SENS( lBomPartIds, iSENSITIVITY_CD_2 );

      // Verify EQP_ASSMBL_SENS table
      AssmblBomID lBomIds = new AssmblBomID( Integer.toString( CONS_DB_ID ), iAssmblCD_ACFT, null );
      VerifyEQP_ASSMBL_SENS( lBomIds, iSENSITIVITY_CD_2 );

   }


   /**
    * This test is to verifyBL_SENSITIVITY_IMPORT validation functionality of staging table
    * BL_SENSITIVITY_PG , on KIT slot of acft
    *
    */

   public void BL_SENSITIVITY_PG_KIT_VALIDATION() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_assign_parts_to_stock table
      Map<String, String> lSens = new LinkedHashMap<>();

      lSens.put( "ASSMBL_CD", "\'" + iAssmblCD_ACFT + "\'" );
      lSens.put( "PART_GROUP_CD", "\'" + iPART_GROUP_CD_KIT + "\'" );
      lSens.put( "SENSITIVITY_CD", "\'" + iSENSITIVITY_CD + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_SENSITIVITY_PG, lSens ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verifyBL_SENSITIVITY_IMPORT import functionality of staging table
    * BL_SENSITIVITY_PG , on KIT slot of acft
    *
    */
   @Test
   public void BL_SENSITIVITY_PG_KIT_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      BL_SENSITIVITY_PG_KIT_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify EQP_BOM_PART_SENS table
      bomPartPN lBomPartIds = getPartGroup( iAssmblCD_ACFT, iPART_GROUP_CD_KIT );
      VerifyEQP_BOM_PART_SENS( lBomPartIds, iSENSITIVITY_CD );

      // Verify EQP_ASSMBL_SENS table
      AssmblBomID lBomIds = new AssmblBomID( Integer.toString( CONS_DB_ID ), iAssmblCD_ACFT, null );
      VerifyEQP_ASSMBL_SENS( lBomIds, iSENSITIVITY_CD );

   }


   /**
    * This test is to verifyBL_SENSITIVITY_IMPORT validation functionality of staging table
    * BL_SENSITIVITY_PG , on BATCH slot of acft
    *
    */

   public void BL_SENSITIVITY_PG_BATCH_VALIDATION() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_assign_parts_to_stock table
      Map<String, String> lSens = new LinkedHashMap<>();

      lSens.put( "ASSMBL_CD", "\'" + iAssmblCD_ACFT + "\'" );
      lSens.put( "PART_GROUP_CD", "\'" + iPART_GROUP_CD_BATCH + "\'" );
      lSens.put( "SENSITIVITY_CD", "\'" + iSENSITIVITY_CD + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_SENSITIVITY_PG, lSens ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verifyBL_SENSITIVITY_IMPORT import functionality of staging table
    * BL_SENSITIVITY_PG , on BATCH slot of acft
    *
    */
   @Test
   public void BL_SENSITIVITY_PG_BATCH_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      BL_SENSITIVITY_PG_BATCH_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify EQP_BOM_PART_SENS table
      bomPartPN lBomPartIds = getPartGroup( iAssmblCD_ACFT, iPART_GROUP_CD_BATCH );
      VerifyEQP_BOM_PART_SENS( lBomPartIds, iSENSITIVITY_CD );

      // Verify EQP_ASSMBL_SENS table
      AssmblBomID lBomIds = new AssmblBomID( Integer.toString( CONS_DB_ID ), iAssmblCD_ACFT, null );
      VerifyEQP_ASSMBL_SENS( lBomIds, iSENSITIVITY_CD );

   }


   /**
    * This test is to verifyBL_SENSITIVITY_IMPORT validation functionality of staging table
    * BL_SENSITIVITY_PG , on SER slot of acft
    *
    */

   public void BL_SENSITIVITY_PG_SER_VALIDATION() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_assign_parts_to_stock table
      Map<String, String> lSens = new LinkedHashMap<>();

      lSens.put( "ASSMBL_CD", "\'" + iAssmblCD_ACFT + "\'" );
      lSens.put( "PART_GROUP_CD", "\'" + iPART_GROUP_CD_SER + "\'" );
      lSens.put( "SENSITIVITY_CD", "\'" + iSENSITIVITY_CD + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.BL_SENSITIVITY_PG, lSens ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verifyBL_SENSITIVITY_IMPORT import functionality of staging table
    * BL_SENSITIVITY_PG , on BATCH slot of acft
    *
    */
   @Test
   public void BL_SENSITIVITY_PG_SER_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      BL_SENSITIVITY_PG_SER_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify EQP_BOM_PART_SENS table
      bomPartPN lBomPartIds = getPartGroup( iAssmblCD_ACFT, iPART_GROUP_CD_SER );
      VerifyEQP_BOM_PART_SENS( lBomPartIds, iSENSITIVITY_CD );

      // Verify EQP_ASSMBL_SENS table
      AssmblBomID lBomIds = new AssmblBomID( Integer.toString( CONS_DB_ID ), iAssmblCD_ACFT, null );
      VerifyEQP_ASSMBL_SENS( lBomIds, iSENSITIVITY_CD );

   }


   /**
    * This test is to verifyBL_SENSITIVITY_IMPORT validation functionality of staging table
    * BL_SENSITIVITY_PG , on trk and apu slots of acft
    *
    */

   public void BL_SENSITIVITY_PG_multiple_VALIDATION() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      BL_SENSITIVITY_PG_TRK_VALIDATION();
      BL_SENSITIVITY_PG_ASSY_VALIDATION();

   }


   /**
    * This test is to verifyBL_SENSITIVITY_IMPORT import functionality of staging table
    * BL_SENSITIVITY_PG , on trk and apu slots of acft
    *
    */
   @Test
   public void BL_SENSITIVITY_PG_multiple_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      BL_SENSITIVITY_PG_multiple_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify EQP_BOM_PART_SENS table
      // verify trk
      bomPartPN lBomPartIds = getPartGroup( iAssmblCD_ACFT, iPART_GROUP_CD_TRK );
      VerifyEQP_BOM_PART_SENS( lBomPartIds, iSENSITIVITY_CD );

      // verify apu
      lBomPartIds = getPartGroup( iAssmblCD_ACFT, iPART_GROUP_CD_ASSY );
      VerifyEQP_BOM_PART_SENS( lBomPartIds, iSENSITIVITY_CD_2 );

      // Verify EQP_ASSMBL_SENS table
      AssmblBomID lBomIds = new AssmblBomID( Integer.toString( CONS_DB_ID ), iAssmblCD_ACFT, null );
      VerifyEQP_ASSMBL_SENS( lBomIds, iSENSITIVITY_CD );

      // verify apu
      lBomIds = new AssmblBomID( Integer.toString( CONS_DB_ID ), iAssmblCD_ACFT, null );
      VerifyEQP_ASSMBL_SENS( lBomIds, iSENSITIVITY_CD_2 );

   }


   /**
    * This test is to verifyBL_SENSITIVITY_IMPORT validation functionality of staging table
    * BL_SENSITIVITY_PG & BL_SENSITIVITY_SYS.
    *
    */

   public void BL_SENSITIVITY_multiple_VALIDATION() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      BL_SENSITIVITY_SYS_ACFT_VALIDATION();
      BL_SENSITIVITY_PG_ASSY_VALIDATION();

   }


   /**
    * This test is to verifyBL_SENSITIVITY_IMPORT import functionality of staging table
    * BL_SENSITIVITY_PG & BL_SENSITIVITY_SYS.
    *
    */
   @Test
   public void BL_SENSITIVITY_multiple_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      BL_SENSITIVITY_multiple_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify acft sys
      // Verify EQP_ASSMBL_BOM_SENS table
      AssmblBomID lBomIds = getAssmblBomId( iAssmblCD_ACFT, iCONFIG_SLOT_CD_ACFT );
      VerifyEQP_ASSMBL_BOM_SENS( lBomIds, iSENSITIVITY_CD );

      // Verify EQP_ASSMBL_SENS table
      VerifyEQP_ASSMBL_SENS( lBomIds, iSENSITIVITY_CD );

      // Verify APU
      // Verify EQP_BOM_PART_SENS table
      bomPartPN lBomPartIds = getPartGroup( iAssmblCD_ACFT, iPART_GROUP_CD_ASSY );
      VerifyEQP_BOM_PART_SENS( lBomPartIds, iSENSITIVITY_CD_2 );

      // Verify EQP_ASSMBL_SENS table
      lBomIds = new AssmblBomID( Integer.toString( CONS_DB_ID ), iAssmblCD_ACFT, null );
      VerifyEQP_ASSMBL_SENS( lBomIds, iSENSITIVITY_CD_2 );

   }


   // ============================================================================

   /**
    * This function is to verify EQP_BOM_PART_SENS by given parameters .
    *
    *
    */

   public void VerifyEQP_BOM_PART_SENS( bomPartPN aIDs, String aSensitivity_cd ) {
      String[] iIds = { "SENSITIVITY_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "BOM_PART_DB_ID", aIDs.getBOM_PART_DB_ID() );
      lArgs.addArguments( "BOM_PART_ID", aIDs.getBOM_PART_ID() );

      String iQueryString =
            TableUtil.buildTableQuery( TableUtil.EQP_BOM_PART_SENS, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "SENSITIVITY_CD",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aSensitivity_cd ) );

   }


   /**
    * This function is to verify EQP_ASSMBL_SENS by given parameters .
    *
    *
    */
   public void VerifyEQP_ASSMBL_SENS( AssmblBomID aIDs, String aSensitivity_cd ) {
      String[] iIds = { "SENSITIVITY_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "ASSMBL_DB_ID", aIDs.getDB_ID() );
      lArgs.addArguments( "ASSMBL_CD", aIDs.getCD() );
      lArgs.addArguments( "SENSITIVITY_CD", aSensitivity_cd );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EQP_ASSMBL_SENS, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "SENSITIVITY_CD",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aSensitivity_cd ) );

   }


   /**
    * This function is to verify EQP_ASSMBL_BOM_SENS by given parameters .
    *
    *
    */
   public void VerifyEQP_ASSMBL_BOM_SENS( AssmblBomID aIDs, String aSensitivity_cd ) {
      String[] iIds = { "SENSITIVITY_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "ASSMBL_DB_ID", aIDs.getDB_ID() );
      lArgs.addArguments( "ASSMBL_CD", aIDs.getCD() );
      lArgs.addArguments( "ASSMBL_BOM_ID", aIDs.getID() );

      String iQueryString =
            TableUtil.buildTableQuery( TableUtil.EQP_ASSMBL_BOM_SENS, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "SENSITIVITY_CD",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aSensitivity_cd ) );

   }


   /**
    * This function is called by after test method to restore data which has been modified by tests.
    *
    *
    */
   public void RestoreData() {

      // first sensitivity
      String lStrDelete = "delete from " + TableUtil.EQP_ASSMBL_BOM_SENS + " where SENSITIVITY_CD='"
            + iSENSITIVITY_CD + "'";
      executeSQL( lStrDelete );

      lStrDelete = "delete from " + TableUtil.EQP_BOM_PART_SENS + " where SENSITIVITY_CD='"
            + iSENSITIVITY_CD + "'";
      executeSQL( lStrDelete );

      lStrDelete = "delete from " + TableUtil.EQP_ASSMBL_SENS + " where SENSITIVITY_CD='"
            + iSENSITIVITY_CD + "'";
      executeSQL( lStrDelete );

      // second sensitivity
      lStrDelete = "delete from " + TableUtil.EQP_ASSMBL_BOM_SENS + " where SENSITIVITY_CD='"
            + iSENSITIVITY_CD_2 + "'";
      executeSQL( lStrDelete );

      lStrDelete = "delete from " + TableUtil.EQP_BOM_PART_SENS + " where SENSITIVITY_CD='"
            + iSENSITIVITY_CD_2 + "'";
      executeSQL( lStrDelete );

      lStrDelete = "delete from " + TableUtil.EQP_ASSMBL_SENS + " where SENSITIVITY_CD='"
            + iSENSITIVITY_CD_2 + "'";
      executeSQL( lStrDelete );
   }

}
