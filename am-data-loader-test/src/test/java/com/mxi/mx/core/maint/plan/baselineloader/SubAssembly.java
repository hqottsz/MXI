package com.mxi.mx.core.maint.plan.baselineloader;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
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

import com.mxi.mx.core.maint.plan.datamodels.assmbleInfor;
import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;
import com.mxi.mx.util.BaselineLoaderTest;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.ValidationAndImport;
import com.mxi.mx.util.WhereClause;


/**
 * This test suite contains test cases on validation and import functionality of SUBASSMEMBLY_IMPORT
 * package in SubAssembly area.
 *
 * @author ALICIA QIAN
 */
public class SubAssembly extends BaselineLoaderTest {

   @Rule
   public TestName testName = new TestName();

   public static final String iAPUcd = "APU-ASSY-AUTO";// ACFT_ATA_SYS_CD=ASSMBL_BOM_CD (existing
                                                       // sys slot)
   public static final String iENGcd = "ENG-ASSY-AUTO";// ACFT_ATA_SYS_CD=ASSMBL_BOM_CD (existing
                                                       // sys slot)
   public static final String iACFTcd = "ACFT_CD1";// ACFT_ASSMBL_CD=ASSMBL_CD
   public static final String iENGAssmblcd = "ENG_CD1";// SUBASSY_ASSMBL_CD=ASSMBL_CD of ENG
   public static final String iAPUAssmblcd = "APU_CD1";// SUBASSY_ASSMBL_CD=ASSMBL_CD of APU
   public static final String iENGPOS1 = "ENG1";// POS_NAME_LIST
   public static final String iENGPOS2 = "ENG2";// POS_NAME_LIST
   public static final String iAPUPOS = "APUAT";// POS_NAME_LIST
   public static final String iENGBOMPARTcd = "AT-ACFT-ENG";// BOM_PART_CD=ACFT_SUBASSY_SLOT_CD
   public static final String iENGBOMPARTname = "AT-ENG-NAME";// BOM_PART_NAME=ACFT_SUBASSY_SLOT_NAME
   public static final String iAPUBOMPARTcd = "AT-ACFT-APU";// BOM_PART_CD=ACFT_SUBASSY_SLOT_CD
   public static final String iAPUBOMPARTname = "AT-APU-NAME";// BOM_PART_NAME=ACFT_SUBASSY_SLOT_NAME

   public List<assmbleInfor> iAssembleList = null;
   public simpleIDs iENGBOMPARTIds = null;
   public simpleIDs iAPUBOMPARTIds = null;

   ValidationAndImport ivalidationandimport;


   /**
    * Setup before executing each individual test
    *
    */
   @Before
   @Override
   public void before() throws Exception {

      super.before();
      clearBaselineLoaderTables();
      iAssembleList = setupSYSSlots();

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
    * This test is to verify SUBASSMEMBLY_IMPORT validation functionality of staging table
    * c_acft_subassy on ENG
    *
    */
   @Test
   public void testENG_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_acft_subassy table
      Map<String, String> lcACFTSubASSy = new LinkedHashMap<>();

      //
      lcACFTSubASSy.put( "ACFT_ASSMBL_CD", "\'" + iACFTcd + "\'" );
      lcACFTSubASSy.put( "ACFT_ATA_SYS_CD", "\'" + iENGcd + "\'" );
      lcACFTSubASSy.put( "ACFT_SUBASSY_SLOT_CD", "\'" + iENGBOMPARTcd + "\'" );
      lcACFTSubASSy.put( "SUBASSY_ASSMBL_CD", "\'" + iENGAssmblcd + "\'" );
      lcACFTSubASSy.put( "ACFT_SUBASSY_SLOT_NAME", "\'" + iENGBOMPARTname + "\'" );
      lcACFTSubASSy.put( "POS_CT", "\'2\'" );
      lcACFTSubASSy.put( "POS_NAME_LIST", "\'" + iENGPOS1 + "," + iENGPOS2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ACFT_SUBASSY, lcACFTSubASSy ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify SUBASSMEMBLY_IMPORT import functionality of staging table
    * c_acft_subassy on ENG
    *
    */
   @Test
   public void testENG_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testENG_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify eqp_assmbl_bom
      verifyEQPASSMBLBOM( iENGBOMPARTcd, iENGBOMPARTname, iACFTcd, Integer.toString( CONS_DB_ID ),
            iACFTcd, iAssembleList.get( 0 ).getASSMBL_BOM_ID(), SUBASSY, "2" );

      // Verify eqp_assmbl_pos
      assmbleInfor lassmblInforENG1 = verifyEQPASSMBLPOS( iENGPOS1, iACFTcd,
            Integer.toString( CONS_DB_ID ), iACFTcd, iAssembleList.get( 0 ).getASSMBL_BOM_ID() );

      assmbleInfor lassmblInforENG2 = verifyEQPASSMBLPOS( iENGPOS2, iACFTcd,
            Integer.toString( CONS_DB_ID ), iACFTcd, iAssembleList.get( 0 ).getASSMBL_BOM_ID() );

      // Verify eqp_bom_part
      simpleIDs lBOMPARTIds =
            verifyEQPBOMPART( iENGBOMPARTcd, iENGBOMPARTname, iACFTcd, INVCLASSASSY );

      iENGBOMPARTIds = lBOMPARTIds;

      // Verify eqp_part_baseline
      List<simpleIDs> llist = getPARTNOList( Integer.toString( CONS_DB_ID ), iENGAssmblcd, "0" );
      for ( int i = 0; i < llist.size(); i++ ) {
         verifyEQPPARTBASELINE( lBOMPARTIds.getNO_DB_ID(), lBOMPARTIds.getNO_ID(),
               llist.get( i ).getNO_DB_ID(), llist.get( i ).getNO_ID() );
      }

   }


   /**
    * This test is to verify SUBASSMEMBLY_IMPORT validation functionality of staging table
    * c_acft_subassy on APU
    *
    */
   @Test
   public void testAPU_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_acft_subassy table
      Map<String, String> lcACFTSubASSy = new LinkedHashMap<>();

      //
      lcACFTSubASSy.put( "ACFT_ASSMBL_CD", "\'" + iACFTcd + "\'" );
      lcACFTSubASSy.put( "ACFT_ATA_SYS_CD", "\'" + iAPUcd + "\'" );
      lcACFTSubASSy.put( "ACFT_SUBASSY_SLOT_CD", "\'" + iAPUBOMPARTcd + "\'" );
      lcACFTSubASSy.put( "SUBASSY_ASSMBL_CD", "\'" + iAPUAssmblcd + "\'" );
      lcACFTSubASSy.put( "ACFT_SUBASSY_SLOT_NAME", "\'" + iAPUBOMPARTname + "\'" );
      lcACFTSubASSy.put( "POS_CT", "\'1\'" );
      lcACFTSubASSy.put( "POS_NAME_LIST", "\'" + iAPUPOS + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ACFT_SUBASSY, lcACFTSubASSy ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify SUBASSMEMBLY_IMPORT import functionality of staging table
    * c_acft_subassy on APU
    *
    */

   @Test
   public void testAPU_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testAPU_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify eqp_assmbl_bom
      verifyEQPASSMBLBOM( iAPUBOMPARTcd, iAPUBOMPARTname, iACFTcd, Integer.toString( CONS_DB_ID ),
            iACFTcd, iAssembleList.get( 1 ).getASSMBL_BOM_ID(), SUBASSY, "1" );

      // Verify eqp_assmbl_pos
      assmbleInfor lassmblInforAPU = verifyEQPASSMBLPOS( iAPUPOS, iACFTcd,
            Integer.toString( CONS_DB_ID ), iACFTcd, iAssembleList.get( 1 ).getASSMBL_BOM_ID() );

      // Verify eqp_bom_part
      simpleIDs lBOMPARTIds =
            verifyEQPBOMPART( iAPUBOMPARTcd, iAPUBOMPARTname, iACFTcd, INVCLASSASSY );

      iAPUBOMPARTIds = lBOMPARTIds;

      // Verify eqp_part_baseline
      List<simpleIDs> llist = getPARTNOList( Integer.toString( CONS_DB_ID ), iAPUAssmblcd, "0" );
      for ( int i = 0; i < llist.size(); i++ ) {
         verifyEQPPARTBASELINE( lBOMPARTIds.getNO_DB_ID(), lBOMPARTIds.getNO_ID(),
               llist.get( i ).getNO_DB_ID(), llist.get( i ).getNO_ID() );
      }

   }


   /**
    * This test is to verify SUBASSMEMBLY_IMPORT validation functionality of staging table
    * c_acft_subassy on APU and ENG
    *
    */

   @Test
   public void testAPUENG_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_acft_subassy table for APU
      Map<String, String> lcACFTSubASSy = new LinkedHashMap<>();

      //
      lcACFTSubASSy.put( "ACFT_ASSMBL_CD", "\'" + iACFTcd + "\'" );
      lcACFTSubASSy.put( "ACFT_ATA_SYS_CD", "\'" + iAPUcd + "\'" );
      lcACFTSubASSy.put( "ACFT_SUBASSY_SLOT_CD", "\'" + iAPUBOMPARTcd + "\'" );
      lcACFTSubASSy.put( "SUBASSY_ASSMBL_CD", "\'" + iAPUAssmblcd + "\'" );
      lcACFTSubASSy.put( "ACFT_SUBASSY_SLOT_NAME", "\'" + iAPUBOMPARTname + "\'" );
      lcACFTSubASSy.put( "POS_CT", "\'1\'" );
      lcACFTSubASSy.put( "POS_NAME_LIST", "\'" + iAPUPOS + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ACFT_SUBASSY, lcACFTSubASSy ) );

      // c_acft_subassy table for ENG
      lcACFTSubASSy.clear();
      lcACFTSubASSy.put( "ACFT_ASSMBL_CD", "\'" + iACFTcd + "\'" );
      lcACFTSubASSy.put( "ACFT_ATA_SYS_CD", "\'" + iENGcd + "\'" );
      lcACFTSubASSy.put( "ACFT_SUBASSY_SLOT_CD", "\'" + iENGBOMPARTcd + "\'" );
      lcACFTSubASSy.put( "SUBASSY_ASSMBL_CD", "\'" + iENGAssmblcd + "\'" );
      lcACFTSubASSy.put( "ACFT_SUBASSY_SLOT_NAME", "\'" + iENGBOMPARTname + "\'" );
      lcACFTSubASSy.put( "POS_CT", "\'2\'" );
      lcACFTSubASSy.put( "POS_NAME_LIST", "\'" + iENGPOS1 + "," + iENGPOS2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ACFT_SUBASSY, lcACFTSubASSy ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify SUBASSMEMBLY_IMPORT import functionality of staging table
    * c_acft_subassy on APU and ENG
    *
    */

   @Test
   public void testAPUENG_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testAPUENG_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Validate APU=============================================================
      System.out.println( "===================Start APU validation=========" );

      // Verify eqp_assmbl_bom
      verifyEQPASSMBLBOM( iAPUBOMPARTcd, iAPUBOMPARTname, iACFTcd, Integer.toString( CONS_DB_ID ),
            iACFTcd, iAssembleList.get( 1 ).getASSMBL_BOM_ID(), SUBASSY, "1" );

      // Verify eqp_assmbl_pos
      assmbleInfor lassmblInforAPU = verifyEQPASSMBLPOS( iAPUPOS, iACFTcd,
            Integer.toString( CONS_DB_ID ), iACFTcd, iAssembleList.get( 1 ).getASSMBL_BOM_ID() );

      // Verify eqp_bom_part
      simpleIDs lBOMPARTIds =
            verifyEQPBOMPART( iAPUBOMPARTcd, iAPUBOMPARTname, iACFTcd, INVCLASSASSY );

      iAPUBOMPARTIds = lBOMPARTIds;

      // Verify eqp_part_baseline
      List<simpleIDs> llist = getPARTNOList( Integer.toString( CONS_DB_ID ), iAPUAssmblcd, "0" );
      for ( int i = 0; i < llist.size(); i++ ) {
         verifyEQPPARTBASELINE( lBOMPARTIds.getNO_DB_ID(), lBOMPARTIds.getNO_ID(),
               llist.get( i ).getNO_DB_ID(), llist.get( i ).getNO_ID() );
      }

      // Validate ENG=============================================================
      System.out.println( "===================Start ENG validation=========" );
      // Verify eqp_assmbl_bom
      verifyEQPASSMBLBOM( iENGBOMPARTcd, iENGBOMPARTname, iACFTcd, Integer.toString( CONS_DB_ID ),
            iACFTcd, iAssembleList.get( 0 ).getASSMBL_BOM_ID(), SUBASSY, "2" );

      // Verify eqp_assmbl_pos
      assmbleInfor lassmblInforENG1 = verifyEQPASSMBLPOS( iENGPOS1, iACFTcd,
            Integer.toString( CONS_DB_ID ), iACFTcd, iAssembleList.get( 0 ).getASSMBL_BOM_ID() );

      assmbleInfor lassmblInforENG2 = verifyEQPASSMBLPOS( iENGPOS2, iACFTcd,
            Integer.toString( CONS_DB_ID ), iACFTcd, iAssembleList.get( 0 ).getASSMBL_BOM_ID() );

      // Verify eqp_bom_part
      lBOMPARTIds = verifyEQPBOMPART( iENGBOMPARTcd, iENGBOMPARTname, iACFTcd, INVCLASSASSY );

      iENGBOMPARTIds = lBOMPARTIds;

      // Verify eqp_part_baseline
      llist.clear();
      llist = getPARTNOList( Integer.toString( CONS_DB_ID ), iENGAssmblcd, "0" );
      for ( int i = 0; i < llist.size(); i++ ) {
         verifyEQPPARTBASELINE( lBOMPARTIds.getNO_DB_ID(), lBOMPARTIds.getNO_ID(),
               llist.get( i ).getNO_DB_ID(), llist.get( i ).getNO_ID() );
      }

   }


   // ===================================================================================================================================

   /**
    * This function is to create system slots, one for ENG, the other one for APU
    *
    *
    */
   public List<assmbleInfor> setupSYSSlots() {

      if ( iAssembleList != null ) {
         iAssembleList.clear();
      }

      // add 2 sys slot to acft one for APU, the other one is ENG
      String[] updateTables = new String[2];

      updateTables[0] =
            "insert into EQP_ASSMBL_BOM (ASSMBL_DB_ID, ASSMBL_CD, ASSMBL_BOM_ID, NH_ASSMBL_DB_ID, NH_ASSMBL_CD, NH_ASSMBL_BOM_ID, BOM_CLASS_DB_ID, BOM_CLASS_CD, CFG_SLOT_STATUS_DB_ID, CFG_SLOT_STATUS_CD, LOGCARD_FORM_DB_ID, LOGCARD_FORM_CD, IETM_DB_ID, IETM_ID, IETM_TOPIC_ID, POS_CT, ASSMBL_BOM_CD, ASSMBL_BOM_FUNC_CD, ASSMBL_BOM_ZONE_CD, ASSMBL_BOM_NAME) "
                  + " values (" + CONS_DB_ID + ", '" + iACFTcd
                  + "', (select max(assmbl_bom_id)+1 from eqp_assmbl_bom where assmbl_cd = 'ACFT_CD1'), "
                  + CONS_DB_ID + ", '" + iACFTcd
                  + "', 0, 0, 'SYS', 0, 'ACTV', null, null, null, null, null, 1, '" + iAPUcd
                  + "', null, null, '" + iAPUcd + "')";

      updateTables[1] =
            "insert into EQP_ASSMBL_BOM (ASSMBL_DB_ID, ASSMBL_CD, ASSMBL_BOM_ID, NH_ASSMBL_DB_ID, NH_ASSMBL_CD, NH_ASSMBL_BOM_ID, BOM_CLASS_DB_ID, BOM_CLASS_CD, CFG_SLOT_STATUS_DB_ID, CFG_SLOT_STATUS_CD, LOGCARD_FORM_DB_ID, LOGCARD_FORM_CD, IETM_DB_ID, IETM_ID, IETM_TOPIC_ID, POS_CT, ASSMBL_BOM_CD, ASSMBL_BOM_FUNC_CD, ASSMBL_BOM_ZONE_CD, ASSMBL_BOM_NAME) "
                  + " values (" + CONS_DB_ID + ", '" + iACFTcd
                  + "', (select max(assmbl_bom_id)+1 from eqp_assmbl_bom where assmbl_cd = 'ACFT_CD1'), "
                  + CONS_DB_ID + ", '" + iACFTcd
                  + "', 0, 0, 'SYS', 0, 'ACTV', null, null, null, null, null, 1, '" + iENGcd
                  + "', null, null, '" + iENGcd + "')";

      executeSQL( updateTables[0] );
      executeSQL( updateTables[1] );

      List<assmbleInfor> llist = new ArrayList<assmbleInfor>();
      llist.add( GetAssemblInfor( iACFTcd, iENGcd ) );
      llist.add( GetAssemblInfor( iACFTcd, iAPUcd ) );

      return llist;

   }


   /**
    * This function is to retrieve assembly information by give assmbl_cd and assmbl_bom_cd
    *
    *
    */
   public assmbleInfor GetAssemblInfor( String aASSMBCD, String aASSMBOMCD ) {

      String[] lfieds = { "assmbl_db_id", "assmbl_cd", "assmbl_bom_id" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( lfieds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "assmbl_cd", aASSMBCD );
      lArgs.addArguments( "ASSMBL_BOM_CD", aASSMBOMCD );

      String lQueryString = TableUtil.buildTableQuery( "eqp_assmbl_bom", lfields, lArgs );
      List<ArrayList<String>> llists = execute( lQueryString, lfields );

      // Get ASSEMBLE information
      assmbleInfor lASMBLINF = new assmbleInfor( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ),
            llists.get( 0 ).get( 2 ), null );

      return lASMBLINF;

   }


   /**
    * This function is called by after test method to restore data which has been modified by tests.
    *
    *
    */

   public void RestoreData() {
      // Delete EQP_PART_BASELINE table
      if ( iENGBOMPARTIds != null ) {
         String lStrDeleteEQPPARTBASELINEENG = "delete from EQP_PART_BASELINE where BOM_PART_DB_ID="
               + iENGBOMPARTIds.getNO_DB_ID() + " and BOM_PART_ID=" + iENGBOMPARTIds.getNO_ID();
         executeSQL( lStrDeleteEQPPARTBASELINEENG );
      }

      if ( iAPUBOMPARTIds != null ) {
         String lStrDeleteEQPPARTBASELINEAPU = "delete from EQP_PART_BASELINE where BOM_PART_DB_ID="
               + iAPUBOMPARTIds.getNO_DB_ID() + " and BOM_PART_ID=" + iAPUBOMPARTIds.getNO_ID();
         executeSQL( lStrDeleteEQPPARTBASELINEAPU );
      }

      // Delete EQP_BOM_PART
      if ( iENGBOMPARTIds != null ) {
         String lStrDeleteEQPBOMPARTENG = "delete from EQP_BOM_PART where BOM_PART_DB_ID="
               + iENGBOMPARTIds.getNO_DB_ID() + " and BOM_PART_ID=" + iENGBOMPARTIds.getNO_ID();
         executeSQL( lStrDeleteEQPBOMPARTENG );
      }

      if ( iAPUBOMPARTIds != null ) {
         String lStrDeleteEQPBOMPARTAPU = "delete from EQP_BOM_PART where BOM_PART_DB_ID="
               + iAPUBOMPARTIds.getNO_DB_ID() + " and BOM_PART_ID=" + iAPUBOMPARTIds.getNO_ID();
         executeSQL( lStrDeleteEQPBOMPARTAPU );
      }

      // Delete eqp_assmbl_pos table for ENG
      String lStrDeleteAssmblPosENG = "delete from eqp_assmbl_pos where assmbl_cd='" + iACFTcd
            + "' and EQP_POS_CD='" + iENGPOS1 + "'";
      executeSQL( lStrDeleteAssmblPosENG );
      lStrDeleteAssmblPosENG = "delete from eqp_assmbl_pos where assmbl_cd='" + iACFTcd
            + "' and EQP_POS_CD='" + iENGPOS2 + "'";
      executeSQL( lStrDeleteAssmblPosENG );

      // Delete eqp_assmbl_pos table for APU
      String lStrDeleteAssmblPosAPU = "delete from eqp_assmbl_pos where assmbl_cd='" + iACFTcd
            + "' and EQP_POS_CD='" + iAPUPOS + "'";
      executeSQL( lStrDeleteAssmblPosAPU );

      // Delete eqp_assmbl_bom for ENG
      String lStrDeleteAssmblBOMENG =
            "delete from eqp_assmbl_bom where assmbl_cd='" + iACFTcd + "' and ASSMBL_BOM_CD='"
                  + iENGBOMPARTcd + "' and ASSMBL_BOM_NAME='" + iENGBOMPARTname + "'";
      executeSQL( lStrDeleteAssmblBOMENG );

      // Delete eqp_assmbl_bom for APU
      String lStrDeleteAssmblBOMAPU =
            "delete from eqp_assmbl_bom where assmbl_cd='" + iACFTcd + "' and ASSMBL_BOM_CD='"
                  + iAPUBOMPARTcd + "' and ASSMBL_BOM_NAME='" + iAPUBOMPARTname + "'";
      executeSQL( lStrDeleteAssmblBOMAPU );

      // Delete eqp_assmbl_bom table
      String lStrDeleteSYS = "delete from eqp_assmbl_bom where assmbl_cd='" + iACFTcd
            + "' and ASSMBL_BOM_CD like '%AUTO%'";

      executeSQL( lStrDeleteSYS );

   }


   /**
    * This function is to verify eqp_assmbl_bom table
    *
    *
    */

   public void verifyEQPASSMBLBOM( String aASSMBLBOMCD, String aASSMBLBOMNAME, String aASSMBLCD,
         String aNHASSMBLDbId, String aNHASSMBLCD, String aNHASSMBLBOMID, String aBOMCLASSCD,
         String aPOSCT ) {

      // EQP_ASSMBL_BOM table
      String[] iIds = { "ASSMBL_CD", "NH_ASSMBL_DB_ID", "NH_ASSMBL_CD", "NH_ASSMBL_BOM_ID",
            "BOM_CLASS_CD", "POS_CT", "ASSMBL_BOM_CD", "ASSMBL_BOM_NAME" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "ASSMBL_BOM_CD", aASSMBLBOMCD );
      lArgs.addArguments( "ASSMBL_BOM_NAME", aASSMBLBOMNAME );

      String iQueryString = TableUtil.buildTableQuery( "EQP_ASSMBL_BOM", lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Validation
      Assert.assertTrue( "ASSMBL_CD", llists.get( 0 ).get( 0 ).equalsIgnoreCase( aASSMBLCD ) );
      Assert.assertTrue( "NH_ASSMBL_DB_ID",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( aNHASSMBLDbId ) );
      Assert.assertTrue( "NH_ASSMBL_CD", llists.get( 0 ).get( 2 ).equalsIgnoreCase( aNHASSMBLCD ) );
      Assert.assertTrue( "NH_ASSMBL_BOM_ID",
            llists.get( 0 ).get( 3 ).equalsIgnoreCase( aNHASSMBLBOMID ) );
      Assert.assertTrue( "BOM_CLASS_CD", llists.get( 0 ).get( 4 ).equalsIgnoreCase( aBOMCLASSCD ) );
      Assert.assertTrue( "POS_CT", llists.get( 0 ).get( 5 ).equalsIgnoreCase( aPOSCT ) );
   }


   /**
    * This function is to verify eqp_assmbl_pos table, and return assembly information
    *
    *
    */

   public assmbleInfor verifyEQPASSMBLPOS( String aEQPPOSCD, String aASSMBLCD, String aNHASSMBLDbId,
         String aNHASSMBLCD, String aNHASSMBLBOMID ) {

      assmbleInfor lassmblInfor = null;

      // EQP_ASSMBL_POS table
      String[] iIds = { "ASSMBL_DB_ID", "ASSMBL_CD", "ASSMBL_BOM_ID", "ASSMBL_POS_ID",
            "NH_ASSMBL_DB_ID", "NH_ASSMBL_CD", "NH_ASSMBL_BOM_ID", "EQP_POS_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "EQP_POS_CD", aEQPPOSCD );

      String iQueryString = TableUtil.buildTableQuery( "EQP_ASSMBL_POS", lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Validation
      Assert.assertTrue( "ASSMBL_CD", llists.get( 0 ).get( 1 ).equalsIgnoreCase( aASSMBLCD ) );
      Assert.assertTrue( "NH_ASSMBL_DB_ID",
            llists.get( 0 ).get( 4 ).equalsIgnoreCase( aNHASSMBLDbId ) );
      Assert.assertTrue( "NH_ASSMBL_CD", llists.get( 0 ).get( 5 ).equalsIgnoreCase( aNHASSMBLCD ) );
      Assert.assertTrue( "NH_ASSMBL_BOM_ID",
            llists.get( 0 ).get( 6 ).equalsIgnoreCase( aNHASSMBLBOMID ) );

      lassmblInfor = new assmbleInfor( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ),
            llists.get( 0 ).get( 2 ), llists.get( 0 ).get( 3 ) );

      return lassmblInfor;

   }


   /**
    * This function is to verify eqp_bom_part table, and return part no information
    *
    *
    */
   public simpleIDs verifyEQPBOMPART( String aBOMPARTCD, String aBOMPARTNAME, String aASSMBLCD,
         String aINVCLASSCD ) {

      simpleIDs lPARTIds = null;

      // EQP_ASSMBL_POS table
      String[] iIds = { "BOM_PART_DB_ID", "BOM_PART_ID", "ASSMBL_CD", "INV_CLASS_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "BOM_PART_CD", aBOMPARTCD );
      lArgs.addArguments( "BOM_PART_NAME", aBOMPARTNAME );

      String iQueryString = TableUtil.buildTableQuery( "EQP_BOM_PART", lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Validation
      Assert.assertTrue( "ASSMBL_CD", llists.get( 0 ).get( 2 ).equalsIgnoreCase( aASSMBLCD ) );
      Assert.assertTrue( "INV_CLASS_CD", llists.get( 0 ).get( 3 ).equalsIgnoreCase( aINVCLASSCD ) );

      lPARTIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lPARTIds;

   }


   public List<simpleIDs> getPARTNOList( String aASSMBLDBID, String aASSMBLCD,
         String aASSMBLBOMID ) {

      simpleIDs lPARTIds = null;
      List<simpleIDs> lidList = new ArrayList<simpleIDs>();

      // EQP_ASSMBL_POS table
      String[] iIds = { "BOM_PART_DB_ID", "BOM_PART_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "ASSMBL_DB_ID", aASSMBLDBID );
      lArgs.addArguments( "ASSMBL_CD", aASSMBLCD );
      lArgs.addArguments( "ASSMBL_BOM_ID", aASSMBLBOMID );

      String iQueryString = TableUtil.buildTableQuery( "EQP_BOM_PART", lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get BOM_PART information
      lPARTIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      String[] iPartNoIds = { "PART_NO_DB_ID", "PART_NO_ID" };
      lfields.clear();
      lfields = new ArrayList<String>( Arrays.asList( iPartNoIds ) );

      lArgs = null;
      lArgs = new WhereClause();
      lArgs.addArguments( "BOM_PART_DB_ID", lPARTIds.getNO_DB_ID() );
      lArgs.addArguments( "BOM_PART_ID", lPARTIds.getNO_ID() );

      iQueryString = TableUtil.buildTableQuery( "EQP_PART_BASELINE", lfields, lArgs );
      List<ArrayList<String>> llistPt = execute( iQueryString, lfields );

      for ( int i = 0; i < llistPt.size(); i++ ) {
         lidList.add( i, new simpleIDs( llistPt.get( i ).get( 0 ), llistPt.get( i ).get( 1 ) ) );

      }

      return lidList;

   }


   /**
    * This function is to verify eqp_part_baseline table
    *
    *
    */

   public void verifyEQPPARTBASELINE( String aBOMPARTDBID, String aBOMPARTID, String aPARTNODBID,
         String aPARTNOID ) {

      String lstrQuery = "select 1 from eqp_part_baseline where BOM_PART_DB_ID=" + aBOMPARTDBID
            + " and BOM_PART_ID=" + aBOMPARTID + " and PART_NO_DB_ID=" + aPARTNODBID
            + " and PART_NO_ID=" + aPARTNOID;

      String lresult = getStringValueFromQuery( lstrQuery, "1" );
      Assert.assertTrue( "The record exists", lresult.equals( "1" ) );
   }


   /**
    * This function is to implement interface ValidationAndImport
    *
    * @param: blnOnlyValidation
    * @param: allornone
    *
    */
   public int runValidationAndImport( boolean blnOnlyValidation, boolean allornone ) {
      int rtValue = 0;
      ivalidationandimport = new ValidationAndImport() {

         @Override
         public int runValidation( boolean allornone ) {
            int lReturn = 0;

            try {
               CallableStatement lPrepareCallInventory = getConnection().prepareCall(
                     "BEGIN subassembly_import.validate_subassembly(on_retcode => ?); END;" );

               lPrepareCallInventory.registerOutParameter( 1, Types.INTEGER );
               lPrepareCallInventory.execute();
               commit();
               lReturn = lPrepareCallInventory.getInt( 1 );

            } catch ( SQLException e ) {
               e.printStackTrace();
            }
            return lReturn;

         }


         @Override
         public int runImport( boolean allornone ) {
            int lReturn = 0;

            try {
               CallableStatement lPrepareCallInventory = getConnection().prepareCall(
                     "BEGIN subassembly_import.import_subassembly(on_retcode => ?); END;" );

               lPrepareCallInventory.registerOutParameter( 1, Types.INTEGER );

               lPrepareCallInventory.execute();
               commit();
               lReturn = lPrepareCallInventory.getInt( 1 );

            } catch ( SQLException e ) {
               e.printStackTrace();
            }
            return lReturn;
         }

      };

      rtValue = blnOnlyValidation ? ivalidationandimport.runValidation( allornone )
            : ivalidationandimport.runImport( allornone );

      return rtValue;
   }

}
