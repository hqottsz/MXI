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
 * This test suite contains tests on valid/import condition cd for Archive/Scrap
 *
 */
public class ImportConditionCodeExtend extends AbstractImportInventory {

   @Rule
   public TestName testName = new TestName();

   public String iARCHIVE = "ARCHIVE";
   public String iSCRAP = "SCRAP";

   public Map<String, Integer> ltotal_qt = new LinkedHashMap<String, Integer>();

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
      initialTotalQTMap();
      clearActualsLoaderTables();
      clearIDs();

   }


   /**
    * This test is to verify :complete ACFT with cond_cd = ARCHIVE without non-mandatory parts ENG
    * cond_cd =blank
    *
    */
   @Test
   public void testACFT_COMPLETE_ARCHIVE_VALIDATION() throws Exception {

      prepareCompleteACFTData( iARCHIVE, iARCHIVE );

      // call inventory validation & import
      runValidateInv( "FULL" );
      checkInventoryNoWarnings( "PASS" );

   }


   /**
    * This test is to verify :complete ACFT with cond_cd = ARCHIVE without non-mandatory parts ENG
    * cond_cd =blank
    *
    */
   @Test
   public void testACFT_COMPLETE_ARCHIVE_IMPORT() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testACFT_COMPLETE_ARCHIVE_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runIMPORTInv( "FULL" );

      // get loc ids
      simpleIDs lLocIds = getINV_LOC_IDs( iARCHIVE, iARCHIVE, iARCHIVE );

      VerifyCompleteACFTWITHMANDATORY( iARCHIVE, lLocIds );

   }


   /**
    * This test is to verify :complete ACFT with cond_cd = ARCHIVE without non-mandatory parts and
    * eng cond_cd=INSTV, then sub component will be overwrite.
    *
    */
   @Test
   public void testACFT_COMPLETE_ARCHIVE2_VALIDATION() throws Exception {

      prepareCompleteACFTData2( iARCHIVE, iARCHIVE, "INSRV", "OPS" );

      // call inventory validation & import
      runValidateInv( "FULL" );
      checkInventoryNoWarnings( "PASS" );

   }


   /**
    * This test is to verify :complete ACFT with cond_cd = ARCHIVE without non-mandatory parts
    *
    *
    */
   @Test
   public void testACFT_COMPLETE_ARCHIVE2_IMPORT() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testACFT_COMPLETE_ARCHIVE2_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runIMPORTInv( "FULL" );

      // get loc ids
      simpleIDs lLocIds = getINV_LOC_IDs( iARCHIVE, iARCHIVE, iARCHIVE );

      VerifyCompleteACFTWITHMANDATORY2( iARCHIVE, lLocIds );

   }


   /**
    * This test is to verify :complete ACFT with cond_cd = ARCHIVE without non-mandatory parts and
    * eng cond_cd=ARCHIVE
    *
    */
   @Test
   public void testACFT_COMPLETE_ARCHIVE3_VALIDATION() throws Exception {

      prepareCompleteACFTData2( iARCHIVE, iARCHIVE, iARCHIVE, iARCHIVE );

      // call inventory validation & import
      runValidateInv( "FULL" );
      checkInventoryNoWarnings( "PASS" );

   }


   /**
    * This test is to verify :complete ACFT with cond_cd = ARCHIVE without non-mandatory parts eng
    * cond_cd=ARCHIVE
    *
    */
   @Test
   public void testACFT_COMPLETE_ARCHIVE3_IMPORT() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testACFT_COMPLETE_ARCHIVE3_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runIMPORTInv( "FULL" );

      // get loc ids
      simpleIDs lLocIds = getINV_LOC_IDs( iARCHIVE, iARCHIVE, iARCHIVE );

      VerifyCompleteACFTWITHMANDATORY( iARCHIVE, lLocIds );

   }


   /**
    * This test is to verify :complete ACFT with cond_cd = ARCHIVE with non-mandatory parts
    *
    *
    */
   @Test
   public void testACFT_COMPLETE_ARCHIVE_NONMANDATORY_VALIDATION() throws Exception {

      prepareCompleteACFTData_NONMANDATORY( iARCHIVE, iARCHIVE );

      // call inventory validation & import
      runValidateInv( "FULL" );
      checkInventoryNoWarnings( "PASS" );

   }


   /**
    * This test is to verify :complete ACFT with cond_cd = ARCHIVE with non-mandatory parts
    *
    *
    */
   @Test
   public void testACFT_COMPLETE_ARCHIVE_NONMANDATORY_IMPORT() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testACFT_COMPLETE_ARCHIVE_NONMANDATORY_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runIMPORTInv( "FULL" );

      // get loc ids
      simpleIDs lLocIds = getINV_LOC_IDs( iARCHIVE, iARCHIVE, iARCHIVE );

      VerifyCompleteACFTNONMANDATORY( iARCHIVE, lLocIds );

   }


   /**
    * This test is to verify :incomplete ACFT with cond_cd = ARCHIVE with mandatory parts
    *
    *
    */

   @Test
   public void testACFT_INCOMPLETION_ARCHIVE_VALIDATION() throws Exception {

      prepareCompleteACFTData( iARCHIVE, iARCHIVE );

      // call inventory validation & import
      runValidateInv( "NOT" );
      checkInventoryNoWarnings( "PASS" );

   }


   /**
    * This test is to verify :incomplete ACFT with cond_cd = ARCHIVE with mandatory parts
    *
    *
    */
   @Test
   public void testACFT_INCOMPLETION_ARCHIVE_IMPORT() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testACFT_INCOMPLETION_ARCHIVE_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runIMPORTInv( "FULL" );

      // get loc ids
      simpleIDs lLocIds = getINV_LOC_IDs( iARCHIVE, iARCHIVE, iARCHIVE );

      VerifyIncompleteACFT( iARCHIVE, lLocIds );

   }


   /**
    * This test is to verify ： complete loose eng with cond_cd = ARCHIVE with mandatory parts
    *
    *
    */

   @Test
   public void testLOOSE_ENG_COMPLETE_ARCHIVE_VALIDATION() throws Exception {

      // create inventory map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      // insert eng
      lMapInventory.put( "SERIAL_NO_OEM", "'ASSY-00001'" );
      lMapInventory.put( "PART_NO_OEM", "'ENG_ASSY_PN8'" );
      lMapInventory.put( "MANUFACT_CD", "'ABC11'" );
      lMapInventory.put( "LOC_CD", "'" + iARCHIVE + "'" );
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "INV_COND_CD", "'" + iARCHIVE + "'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // call inventory validation & import
      runValidateInv( "FULL" );
      checkInventoryNoWarnings( "PASS" );

   }


   /**
    * This test is to verify ： complete loose eng with cond_cd = ARCHIVE with mandatory parts
    *
    *
    */
   @Test
   public void testLOOSE_ENG_COMPLETE_ARCHIVE_IMPORT() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testLOOSE_ENG_COMPLETE_ARCHIVE_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runIMPORTInv( "FULL" );

      simpleIDs lLocIds = getINV_LOC_IDs( iARCHIVE, iARCHIVE, iARCHIVE );
      VerifyCOMPLETE_ENG( iARCHIVE, lLocIds );
   }


   /**
    * This test is to verify ： complete loose TRK with cond_cd = ARCHIVE with mandatory parts
    *
    *
    */
   @Test
   public void testLOOSE_TRK_COMPLETE_ARCHIVE_VALIDATION() throws Exception {

      // create inventory map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      // insert TRK
      lMapInventory.put( "SERIAL_NO_OEM", "'TRK-00001'" );
      lMapInventory.put( "PART_NO_OEM", "'A0000220'" );
      lMapInventory.put( "MANUFACT_CD", "'11111'" );
      lMapInventory.put( "LOC_CD", "'" + iARCHIVE + "'" );
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "INV_COND_CD", "'" + iARCHIVE + "'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // call inventory validation & import
      runValidateInv( "FULL" );
      checkInventoryNoWarnings( "PASS" );

   }


   /**
    * This test is to verify ： complete loose TRK with cond_cd = ARCHIVE with mandatory parts
    *
    *
    */
   @Test
   public void testLOOSE_TRK_COMPLETE_ARCHIVE_IMPORT() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testLOOSE_TRK_COMPLETE_ARCHIVE_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runIMPORTInv( "FULL" );

      simpleIDs lLocIds = getINV_LOC_IDs( iARCHIVE, iARCHIVE, iARCHIVE );
      verifyTRKCOMPCOMPLETE( iARCHIVE, lLocIds );
   }


   /**
    * This test is to verify ： complete loose SER with cond_cd = ARCHIVE.
    *
    *
    */
   @Test
   public void testLOOSE_SER_COMPLETE_ARCHIVE_VALIDATION() throws Exception {

      // create inventory map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      // insert TRK
      lMapInventory.put( "SERIAL_NO_OEM", "'SER-00001'" );
      lMapInventory.put( "PART_NO_OEM", "'A0001260'" );
      lMapInventory.put( "MANUFACT_CD", "'11111'" );
      lMapInventory.put( "LOC_CD", "'" + iARCHIVE + "'" );
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "INV_COND_CD", "'" + iARCHIVE + "'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // call inventory validation & import
      runValidateInv( "FULL" );
      checkInventoryNoWarnings( "PASS" );

   }


   /**
    * This test is to verify ： complete loose SER with cond_cd = ARCHIVE
    *
    *
    */
   @Test
   public void testLOOSE_SER_COMPLETE_ARCHIVE_IMPORT() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testLOOSE_SER_COMPLETE_ARCHIVE_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runIMPORTInv( "FULL" );

      simpleIDs lLocIds = getINV_LOC_IDs( iARCHIVE, iARCHIVE, iARCHIVE );
      verifySERCOMPCOMPLETE( iARCHIVE, lLocIds );
   }


   /**
    * This test is to verify :complete ACFT with cond_cd = ARCHIVE without non-mandatory parts
    *
    *
    */
   @Test
   public void testACFT_COMPLETE_SCRAP_VALIDATION() throws Exception {

      prepareCompleteACFTData( iSCRAP, iSCRAP );

      // call inventory validation & import
      runValidateInv( "FULL" );
      checkInventoryNoWarnings( "PASS" );

   }


   /**
    * This test is to verify :complete ACFT with cond_cd = SCRAP without non-mandatory parts
    *
    *
    */
   @Test
   public void testACFT_COMPLETE_SCRAP_IMPORT() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testACFT_COMPLETE_SCRAP_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runIMPORTInv( "FULL" );

      // get loc ids
      simpleIDs lLocIds = getINV_LOC_IDs( iSCRAP, iSCRAP, iSCRAP );

      VerifyCompleteACFTWITHMANDATORY( iSCRAP, lLocIds );

   }


   /**
    * This test is to verify :complete ACFT with cond_cd = ARCHIVE without non-mandatory parts eng
    * cond_cd=INSRV
    *
    */
   @Test
   public void testACFT_COMPLETE_SCRAP_2_VALIDATION() throws Exception {

      prepareCompleteACFTData2( iSCRAP, iSCRAP, "INSRV", "OPS" );

      // call inventory validation & import
      runValidateInv( "FULL" );
      checkInventoryNoWarnings( "PASS" );

   }


   /**
    * This test is to verify :complete ACFT with cond_cd = SCRAP without non-mandatory parts
    * cond_cd=INSRV
    *
    */
   @Test
   public void testACFT_COMPLETE_SCRAP_2_IMPORT() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testACFT_COMPLETE_SCRAP_2_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runIMPORTInv( "FULL" );

      // get loc ids
      simpleIDs lLocIds = getINV_LOC_IDs( iSCRAP, iSCRAP, iSCRAP );

      VerifyCompleteACFTWITHMANDATORY2( iSCRAP, lLocIds );

   }


   /**
    * This test is to verify :complete ACFT with cond_cd = ARCHIVE without non-mandatory parts, eng
    * cond_cd=ISCRAP
    *
    */
   @Test
   public void testACFT_COMPLETE_SCRAP_3_VALIDATION() throws Exception {

      prepareCompleteACFTData2( iSCRAP, iSCRAP, iSCRAP, iSCRAP );

      // call inventory validation & import
      runValidateInv( "FULL" );
      checkInventoryNoWarnings( "PASS" );

   }


   /**
    * This test is to verify :complete ACFT with cond_cd = SCRAP without non-mandatory parts ENG
    * cond_cd=ISCRAP
    *
    */
   @Test
   public void testACFT_COMPLETE_SCRAP_3_IMPORT() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testACFT_COMPLETE_SCRAP_3_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runIMPORTInv( "FULL" );

      // get loc ids
      simpleIDs lLocIds = getINV_LOC_IDs( iSCRAP, iSCRAP, iSCRAP );

      VerifyCompleteACFTWITHMANDATORY( iSCRAP, lLocIds );

   }


   /**
    * This test is to verify :complete ACFT with cond_cd = SCRAP with non-mandatory parts
    *
    *
    */
   @Test
   public void testACFT_COMPLETE_SCRAP_NONMANDATORY_VALIDATION() throws Exception {

      prepareCompleteACFTData_NONMANDATORY( iSCRAP, iSCRAP );

      // call inventory validation & import
      runValidateInv( "FULL" );
      checkInventoryNoWarnings( "PASS" );

   }


   /**
    * This test is to verify :complete ACFT with cond_cd = SCRAP with non-mandatory parts
    *
    *
    */
   @Test
   public void testACFT_COMPLETE_SCRAP_NONMANDATORY_IMPORT() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testACFT_COMPLETE_SCRAP_NONMANDATORY_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runIMPORTInv( "FULL" );

      // get loc ids
      simpleIDs lLocIds = getINV_LOC_IDs( iSCRAP, iSCRAP, iSCRAP );
      VerifyCompleteACFTNONMANDATORY( iSCRAP, lLocIds );

   }


   /**
    * This test is to verify :incomplete ACFT with cond_cd = SCRAP with mandatory parts
    *
    *
    */

   @Test
   public void testACFT_INCOMPLETION_SCRAP_VALIDATION() throws Exception {

      prepareCompleteACFTData( iSCRAP, iSCRAP );

      // call inventory validation & import
      runValidateInv( "NOT" );
      checkInventoryNoWarnings( "PASS" );

   }


   /**
    * This test is to verify :incomplete ACFT with cond_cd = SCRAP with mandatory parts
    *
    *
    */
   @Test
   public void testACFT_INCOMPLETION_SCRAP_IMPORT() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testACFT_INCOMPLETION_SCRAP_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runIMPORTInv( "FULL" );

      // get loc ids
      simpleIDs lLocIds = getINV_LOC_IDs( iSCRAP, iSCRAP, iSCRAP );
      VerifyIncompleteACFT( iSCRAP, lLocIds );

   }


   /**
    * This test is to verify ： complete loose eng with cond_cd = SCRAP with mandatory parts
    *
    *
    */

   @Test
   public void testLOOSE_ENG_COMPLETE_SCRAP_VALIDATION() throws Exception {

      // create inventory map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      // insert eng
      lMapInventory.put( "SERIAL_NO_OEM", "'ASSY-00001'" );
      lMapInventory.put( "PART_NO_OEM", "'ENG_ASSY_PN8'" );
      lMapInventory.put( "MANUFACT_CD", "'ABC11'" );
      lMapInventory.put( "LOC_CD", "'" + iSCRAP + "'" );
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "INV_COND_CD", "'" + iSCRAP + "'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // call inventory validation & import
      runValidateInv( "FULL" );
      checkInventoryNoWarnings( "PASS" );

   }


   /**
    * This test is to verify ： complete loose eng with cond_cd = SCRAP with mandatory parts
    *
    *
    */

   @Test
   public void testLOOSE_ENG_COMPLETE_SCRAP_IMPORT() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testLOOSE_ENG_COMPLETE_SCRAP_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runIMPORTInv( "FULL" );

      simpleIDs lLocIds = getINV_LOC_IDs( iSCRAP, iSCRAP, iSCRAP );
      VerifyCOMPLETE_ENG( iSCRAP, lLocIds );
   }


   /**
    * This test is to verify ： complete loose TRK with cond_cd = SCRAP with mandatory parts
    *
    *
    */
   @Test
   public void testLOOSE_TRK_COMPLETE_SCRAP_VALIDATION() throws Exception {

      // create inventory map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      // insert TRK
      lMapInventory.put( "SERIAL_NO_OEM", "'TRK-00001'" );
      lMapInventory.put( "PART_NO_OEM", "'A0000220'" );
      lMapInventory.put( "MANUFACT_CD", "'11111'" );
      lMapInventory.put( "LOC_CD", "'" + iSCRAP + "'" );
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "INV_COND_CD", "'" + iSCRAP + "'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // call inventory validation & import
      runValidateInv( "FULL" );
      checkInventoryNoWarnings( "PASS" );

   }


   /**
    * This test is to verify ： complete loose TRK with cond_cd = SCRAP with mandatory parts
    *
    *
    */
   @Test
   public void testLOOSE_TRK_COMPLETE_SCRAP_IMPORT() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testLOOSE_TRK_COMPLETE_SCRAP_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runIMPORTInv( "FULL" );

      simpleIDs lLocIds = getINV_LOC_IDs( iSCRAP, iSCRAP, iSCRAP );
      verifyTRKCOMPCOMPLETE( iSCRAP, lLocIds );
   }


   /**
    * This test is to verify ： complete loose SER with cond_cd = SCRAP
    *
    *
    */
   @Test
   public void testLOOSE_SER_COMPLETE_SCRAP_VALIDATION() throws Exception {

      // create inventory map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      // insert TRK
      lMapInventory.put( "SERIAL_NO_OEM", "'SER-00001'" );
      lMapInventory.put( "PART_NO_OEM", "'A0001260'" );
      lMapInventory.put( "MANUFACT_CD", "'11111'" );
      lMapInventory.put( "LOC_CD", "'" + iSCRAP + "'" );
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "INV_COND_CD", "'" + iSCRAP + "'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // call inventory validation & import
      runValidateInv( "FULL" );
      checkInventoryNoWarnings( "PASS" );

   }


   /**
    * This test is to verify ： complete loose SER with cond_cd = SCRAP
    *
    *
    */
   @Test
   public void testLOOSE_SER_COMPLETE_SCRAP_IMPORT() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testLOOSE_SER_COMPLETE_SCRAP_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runIMPORTInv( "FULL" );

      simpleIDs lLocIds = getINV_LOC_IDs( iSCRAP, iSCRAP, iSCRAP );
      verifySERCOMPCOMPLETE( iSCRAP, lLocIds );
   }


   // ===========================================================================================

   /**
    * This function is to prepare data for complete acft
    *
    *
    */

   public void prepareCompleteACFTData( String aCond_cd, String aLoc_cd ) {
      // create inventory map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-00001'" );
      lMapInventory.put( "PART_NO_OEM", "'ACFT_ASSY_PN8'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'" + aLoc_cd + "'" );
      lMapInventory.put( "AC_REG_CD", "'AC-TEST'" );
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "INV_COND_CD", "'" + aCond_cd + "'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // insert eng 1
      lMapInventory.clear();
      lMapInventory.put( "SERIAL_NO_OEM", "'ASSY-00001'" );
      lMapInventory.put( "PART_NO_OEM", "'ENG_ASSY_PN8'" );
      lMapInventory.put( "MANUFACT_CD", "'ABC11'" );
      lMapInventory.put( "LOC_CD", "'" + aLoc_cd + "'" );
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
      lMapInventory.put( "LOC_CD", "'" + aLoc_cd + "'" );
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
   }


   /**
    * This function is to prepare data for complete acft
    *
    *
    */

   public void prepareCompleteACFTData2( String aCond_cd, String aLoc_cd, String aCond_cd2,
         String aLoc_cd2 ) {
      // create inventory map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-00001'" );
      lMapInventory.put( "PART_NO_OEM", "'ACFT_ASSY_PN8'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'" + aLoc_cd + "'" );
      lMapInventory.put( "AC_REG_CD", "'AC-TEST'" );
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "INV_COND_CD", "'" + aCond_cd + "'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // insert eng 1
      lMapInventory.clear();
      lMapInventory.put( "SERIAL_NO_OEM", "'ASSY-00001'" );
      lMapInventory.put( "PART_NO_OEM", "'ENG_ASSY_PN8'" );
      lMapInventory.put( "MANUFACT_CD", "'ABC11'" );
      lMapInventory.put( "LOC_CD", "'" + aLoc_cd2 + "'" );
      // lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "INV_COND_CD", "'" + aCond_cd2 + "'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // insert eng 2
      lMapInventory.clear();
      lMapInventory.put( "SERIAL_NO_OEM", "'ASSY-00002'" );
      lMapInventory.put( "PART_NO_OEM", "'ENG_ASSY_PN8'" );
      lMapInventory.put( "MANUFACT_CD", "'ABC11'" );
      lMapInventory.put( "LOC_CD", "'" + aLoc_cd2 + "'" );
      // lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "INV_COND_CD", "'" + aCond_cd2 + "'" );
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
   }


   /**
    * This function is to prepare data for complete acft with non-mandatory part
    *
    *
    */

   public void prepareCompleteACFTData_NONMANDATORY( String aCond_cd, String aLoc_cd ) {
      // create inventory map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-00001'" );
      lMapInventory.put( "PART_NO_OEM", "'ACFT_ASSY_PN8'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'" + aLoc_cd + "'" );
      lMapInventory.put( "AC_REG_CD", "'AC-TEST'" );
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "INV_COND_CD", "'" + aCond_cd + "'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // insert eng 1
      lMapInventory.clear();
      lMapInventory.put( "SERIAL_NO_OEM", "'ASSY-00001'" );
      lMapInventory.put( "PART_NO_OEM", "'ENG_ASSY_PN8'" );
      lMapInventory.put( "MANUFACT_CD", "'ABC11'" );
      lMapInventory.put( "LOC_CD", "'" + aLoc_cd + "'" );
      // lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "INV_COND_CD", "'" + aCond_cd + "'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // insert eng 2
      lMapInventory.clear();
      lMapInventory.put( "SERIAL_NO_OEM", "'ASSY-00002'" );
      lMapInventory.put( "PART_NO_OEM", "'ENG_ASSY_PN8'" );
      lMapInventory.put( "MANUFACT_CD", "'ABC11'" );
      lMapInventory.put( "LOC_CD", "'" + aLoc_cd + "'" );
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

   }


   /**
    * This function is to verify for complete acft
    *
    *
    */

   public void VerifyCompleteACFTWITHMANDATORY( String aCOND_CD, simpleIDs aLOC_IDs ) {

      // Get iINV_Ids of ACFT
      iINV_Ids = verifyINVINVtable( "ACFT-00001", "ACFT" );
      // Get iINV_Ids of eng 1
      iINV_Ids_Assy = verifyINVINVtable( "ASSY-00001", "ASSY" );
      // Get iINV_Ids of eng 2
      iINV_Ids_Assy_2 = verifyINVINVtable( "ASSY-00002", "ASSY" );

      // Verify ACFT's complete_bool and inv_cond_cd
      verifyINVINVtable_extend( iINV_Ids, "ACFT_T9", "ACFT-00001", null, "ACFT", "1", aCOND_CD, "1",
            aLOC_IDs );

      // Verify SYS-1
      verifyINVINVtable_extend( iINV_Ids, "ACFT_T9", "XXX", "SYS-1", "SYS", "1", aCOND_CD, "1",
            aLOC_IDs );

      // Verify SYS-1-ENG
      verifyINVINVtable_extend( iINV_Ids, "ACFT_T9", "XXX", "SYS-1-ENG", "SYS", "1", aCOND_CD, "1",
            aLOC_IDs );

      // Verify SYS-2
      verifyINVINVtable_extend( iINV_Ids, "ACFT_T9", "XXX", "SYS-2", "SYS", "1", aCOND_CD, "1",
            aLOC_IDs );

      // Verify ACFT-SYS-1-1-TRK-BATCH-PARENT
      verifyINVINVtable_extend( iINV_Ids, "ACFT_T9", "XXX", "ACFT-SYS-1-1-TRK-BATCH-PARENT", "TRK",
            "1", aCOND_CD, "1", aLOC_IDs );

      // Verify ACFT-SYS-2-CHILD-TRK-1
      verifyINVINVtable_extend( iINV_Ids, "ACFT_T9", "XXX", "ACFT-SYS-2-CHILD-TRK-1", "TRK", "1",
            aCOND_CD, "1", aLOC_IDs );

      // Verify ACFT-SYS-2-SER
      String lQuery = "select 1 from " + TableUtil.INV_INV + " where H_INV_NO_DB_ID="
            + iINV_Ids.getNO_DB_ID() + " and H_INV_NO_ID=" + iINV_Ids.getNO_ID()
            + "  and SERIAL_NO_OEM='SER0001' and INV_COND_CD='" + aCOND_CD + "'";
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
      verifyINVINVtable_extend( iINV_Ids, "ACFT_T9", "ASSY-00001", "ENG-ASSY (1)", "ASSY", "1",
            aCOND_CD, "1", aLOC_IDs );
      verifyINVINVtable_extend( iINV_Ids, "ACFT_T9", "ASSY-00002", "ENG-ASSY (2)", "ASSY", "1",
            aCOND_CD, "1", aLOC_IDs );

      // verify ENG-SYS-1
      verifyINVINVtable_extend( iINV_Ids, "ENG_CD9", "XXX", "ENG-ASSY (1) ->ENG-SYS-1", "SYS", "1",
            aCOND_CD, "1", aLOC_IDs );
      verifyINVINVtable_extend( iINV_Ids, "ENG_CD9", "XXX", "ENG-ASSY (2) ->ENG-SYS-1", "SYS", "1",
            aCOND_CD, "1", aLOC_IDs );

      // verify ENG-SYS-1-TRK
      verifyINVINVtable_extend( iINV_Ids, "ENG_CD9", "XXX", "ENG-ASSY (1) ->ENG-SYS-1-TRK", "TRK",
            "1", aCOND_CD, "1", aLOC_IDs );
      verifyINVINVtable_extend( iINV_Ids, "ENG_CD9", "XXX", "ENG-ASSY (2) ->ENG-SYS-1-TRK", "TRK",
            "1", aCOND_CD, "1", aLOC_IDs );

      // verify ENG-SYS-1-TRK2
      verifyINVINVtable_extend( iINV_Ids, "ENG_CD9", "XXX", "ENG-ASSY (1) ->ENG-SYS-1-TRK2", "TRK",
            "1", aCOND_CD, "1", aLOC_IDs );
      verifyINVINVtable_extend( iINV_Ids, "ENG_CD9", "XXX", "ENG-ASSY (2) ->ENG-SYS-1-TRK2", "TRK",
            "1", aCOND_CD, "1", aLOC_IDs );

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

      // Verify total_qt will keep same for archive/scrap imports
      CheckTotalQTuntouched();

   }


   /**
    * This function is to verify for complete acft
    *
    *
    */

   public void VerifyCompleteACFTWITHMANDATORY2( String aCOND_CD, simpleIDs aLOC_IDs ) {

      // Get iINV_Ids of ACFT
      iINV_Ids = verifyINVINVtable( "ACFT-00001", "ACFT" );
      // Get iINV_Ids of eng 1
      iINV_Ids_Assy = verifyINVINVtable( "ASSY-00001", "ASSY" );
      // Get iINV_Ids of eng 2
      iINV_Ids_Assy_2 = verifyINVINVtable( "ASSY-00002", "ASSY" );

      // Verify ACFT's complete_bool and inv_cond_cd
      verifyINVINVtable_extend( iINV_Ids, "ACFT_T9", "ACFT-00001", null, "ACFT", "1", aCOND_CD, "1",
            aLOC_IDs );

      // Verify SYS-1
      verifyINVINVtable_extend( iINV_Ids, "ACFT_T9", "XXX", "SYS-1", "SYS", "1", aCOND_CD, "1",
            aLOC_IDs );

      // Verify SYS-1-ENG
      verifyINVINVtable_extend( iINV_Ids, "ACFT_T9", "XXX", "SYS-1-ENG", "SYS", "1", aCOND_CD, "1",
            aLOC_IDs );

      // Verify SYS-2
      verifyINVINVtable_extend( iINV_Ids, "ACFT_T9", "XXX", "SYS-2", "SYS", "1", aCOND_CD, "1",
            aLOC_IDs );

      // Verify ACFT-SYS-1-1-TRK-BATCH-PARENT
      verifyINVINVtable_extend( iINV_Ids, "ACFT_T9", "XXX", "ACFT-SYS-1-1-TRK-BATCH-PARENT", "TRK",
            "1", aCOND_CD, "1", aLOC_IDs );

      // Verify ACFT-SYS-2-CHILD-TRK-1
      verifyINVINVtable_extend( iINV_Ids, "ACFT_T9", "XXX", "ACFT-SYS-2-CHILD-TRK-1", "TRK", "1",
            aCOND_CD, "1", aLOC_IDs );

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
      verifyINVINVtable_extend( iINV_Ids, "ACFT_T9", "ASSY-00001", "ENG-ASSY (1)", "ASSY", "1",
            aCOND_CD, "1", null );
      verifyINVINVtable_extend( iINV_Ids, "ACFT_T9", "ASSY-00002", "ENG-ASSY (2)", "ASSY", "1",
            aCOND_CD, "1", null );

      // verify ENG-SYS-1
      verifyINVINVtable_extend( iINV_Ids, "ENG_CD9", "XXX", "ENG-ASSY (1) ->ENG-SYS-1", "SYS", "1",
            aCOND_CD, "1", null );
      verifyINVINVtable_extend( iINV_Ids, "ENG_CD9", "XXX", "ENG-ASSY (2) ->ENG-SYS-1", "SYS", "1",
            aCOND_CD, "1", null );

      // verify ENG-SYS-1-TRK
      verifyINVINVtable_extend( iINV_Ids, "ENG_CD9", "XXX", "ENG-ASSY (1) ->ENG-SYS-1-TRK", "TRK",
            "1", aCOND_CD, "1", null );
      verifyINVINVtable_extend( iINV_Ids, "ENG_CD9", "XXX", "ENG-ASSY (2) ->ENG-SYS-1-TRK", "TRK",
            "1", aCOND_CD, "1", null );

      // verify ENG-SYS-1-TRK2
      verifyINVINVtable_extend( iINV_Ids, "ENG_CD9", "XXX", "ENG-ASSY (1) ->ENG-SYS-1-TRK2", "TRK",
            "1", aCOND_CD, "1", null );
      verifyINVINVtable_extend( iINV_Ids, "ENG_CD9", "XXX", "ENG-ASSY (2) ->ENG-SYS-1-TRK2", "TRK",
            "1", aCOND_CD, "1", null );

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

      // Verify total_qt will keep same for archive/scrap imports
      CheckTotalQTuntouched();

   }


   /**
    * This function is to verify for complete acft with non mandatory
    *
    *
    */
   public void VerifyCompleteACFTNONMANDATORY( String aCOND_CD, simpleIDs aLOC_IDs ) {

      // Get iINV_Ids of ACFT
      iINV_Ids = verifyINVINVtable( "ACFT-00001", "ACFT" );
      // Get iINV_Ids of eng 1
      iINV_Ids_Assy = verifyINVINVtable( "ASSY-00001", "ASSY" );
      // Get iINV_Ids of eng 2
      iINV_Ids_Assy_2 = verifyINVINVtable( "ASSY-00002", "ASSY" );

      // Verify ACFT's complete_bool and inv_cond_cd
      verifyINVINVtable_extend( iINV_Ids, "ACFT_T9", "ACFT-00001", null, "ACFT", "1", aCOND_CD, "1",
            aLOC_IDs );

      // Verify SYS-1
      verifyINVINVtable_extend( iINV_Ids, "ACFT_T9", "XXX", "SYS-1", "SYS", "1", aCOND_CD, "1",
            aLOC_IDs );

      // Verify SYS-1-ENG
      verifyINVINVtable_extend( iINV_Ids, "ACFT_T9", "XXX", "SYS-1-ENG", "SYS", "1", aCOND_CD, "1",
            aLOC_IDs );

      // Verify SYS-2
      verifyINVINVtable_extend( iINV_Ids, "ACFT_T9", "XXX", "SYS-2", "SYS", "1", aCOND_CD, "1",
            aLOC_IDs );

      // Verify ACFT-SYS-1-1-TRK-BATCH-PARENT
      verifyINVINVtable_extend( iINV_Ids, "ACFT_T9", "XXX", "ACFT-SYS-1-1-TRK-BATCH-PARENT", "TRK",
            "1", aCOND_CD, "1", aLOC_IDs );

      // Verify ACFT-SYS-2-CHILD-TRK-1
      verifyINVINVtable_extend( iINV_Ids, "ACFT_T9", "XXX", "ACFT-SYS-2-CHILD-TRK-1", "TRK", "1",
            aCOND_CD, "1", aLOC_IDs );

      // Verify ACFT-SYS-2-CHILD-TRK-2
      verifyINVINVtable_extend( iINV_Ids, "ACFT_T9", "NONMAND0001", "ACFT-SYS-2-CHILD-TRK-2", "TRK",
            "1", aCOND_CD, "1", aLOC_IDs );

      // verify non-manadatory ACFT-SYS-1-TRK-1 is not in inv_inv
      String lQuery = "select 1 from " + TableUtil.INV_INV + " where H_INV_NO_DB_ID="
            + iINV_Ids.getNO_DB_ID() + " and H_INV_NO_ID=" + iINV_Ids.getNO_ID()
            + "  and CONFIG_POS_SDESC='ACFT-SYS-1-TRK-1'";
      Assert.assertFalse( "Check inv_inv table to verify the record not exists",
            RecordsExist( lQuery ) );

      // // verify non-mandatory ACFT-SYS-2-CHILD-TRK-2 is not in inv_inv
      // lQuery = "select 1 from " + TableUtil.INV_INV + " where H_INV_NO_DB_ID="
      // + iINV_Ids.getNO_DB_ID() + " and H_INV_NO_ID=" + iINV_Ids.getNO_ID()
      // + " and CONFIG_POS_SDESC='ACFT-SYS-2-CHILD-TRK-2'";
      // Assert.assertFalse( "Check inv_inv table to verify the record not exists",
      // RecordsExist( lQuery ) );

      // verify ENG-ASSY
      verifyINVINVtable_extend( iINV_Ids, "ACFT_T9", "ASSY-00001", "ENG-ASSY (1)", "ASSY", "1",
            aCOND_CD, "1", aLOC_IDs );
      verifyINVINVtable_extend( iINV_Ids, "ACFT_T9", "ASSY-00002", "ENG-ASSY (2)", "ASSY", "1",
            aCOND_CD, "1", aLOC_IDs );

      // verify ENG-SYS-1
      verifyINVINVtable_extend( iINV_Ids, "ENG_CD9", "XXX", "ENG-ASSY (1) ->ENG-SYS-1", "SYS", "1",
            aCOND_CD, "1", aLOC_IDs );
      verifyINVINVtable_extend( iINV_Ids, "ENG_CD9", "XXX", "ENG-ASSY (2) ->ENG-SYS-1", "SYS", "1",
            aCOND_CD, "1", aLOC_IDs );

      // verify ENG-SYS-1-TRK
      verifyINVINVtable_extend( iINV_Ids, "ENG_CD9", "XXX", "ENG-ASSY (1) ->ENG-SYS-1-TRK", "TRK",
            "1", aCOND_CD, "1", aLOC_IDs );
      verifyINVINVtable_extend( iINV_Ids, "ENG_CD9", "XXX", "ENG-ASSY (2) ->ENG-SYS-1-TRK", "TRK",
            "1", aCOND_CD, "1", aLOC_IDs );

      // verify ENG-SYS-1-TRK2
      verifyINVINVtable_extend( iINV_Ids, "ENG_CD9", "XXX", "ENG-ASSY (1) ->ENG-SYS-1-TRK2", "TRK",
            "1", aCOND_CD, "1", aLOC_IDs );
      verifyINVINVtable_extend( iINV_Ids, "ENG_CD9", "XXX", "ENG-ASSY (2) ->ENG-SYS-1-TRK2", "TRK",
            "1", aCOND_CD, "1", aLOC_IDs );

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

      // Verify total_qt will keep same for archive/scrap imports
      CheckTotalQTuntouched();

   }


   /**
    * This function is to verify for incomplete acft
    *
    *
    */
   public void VerifyIncompleteACFT( String aCOND_CD, simpleIDs aLOC_IDs ) {

      // Get iINV_Ids of ACFT
      iINV_Ids = verifyINVINVtable( "ACFT-00001", "ACFT" );
      // Get iINV_Ids of eng 1
      iINV_Ids_Assy = verifyINVINVtable( "ASSY-00001", "ASSY" );
      // Get iINV_Ids of eng 2
      iINV_Ids_Assy_2 = verifyINVINVtable( "ASSY-00002", "ASSY" );

      // Verify ACFT's complete_bool and inv_cond_cd
      verifyINVINVtable_extend( iINV_Ids, "ACFT_T9", "ACFT-00001", null, "ACFT", "0", aCOND_CD, "1",
            aLOC_IDs );

      // Verify SYS-1
      verifyINVINVtable_extend( iINV_Ids, "ACFT_T9", "XXX", "SYS-1", "SYS", "0", aCOND_CD, "1",
            aLOC_IDs );

      // Verify SYS-1-ENG
      verifyINVINVtable_extend( iINV_Ids, "ACFT_T9", "XXX", "SYS-1-ENG", "SYS", "0", aCOND_CD, "1",
            aLOC_IDs );

      // Verify SYS-2
      verifyINVINVtable_extend( iINV_Ids, "ACFT_T9", "XXX", "SYS-2", "SYS", "0", aCOND_CD, "1",
            aLOC_IDs );

      // verify ACFT-SYS-1-1-TRK-BATCH-PARENT is not in inv_inv table
      String lQuery = "select 1 from " + TableUtil.INV_INV + " where H_INV_NO_DB_ID="
            + iINV_Ids.getNO_DB_ID() + " and H_INV_NO_ID=" + iINV_Ids.getNO_ID()
            + "  and CONFIG_POS_SDESC='ACFT-SYS-1-1-TRK-BATCH-PARENT'";
      Assert.assertFalse( "Check inv_inv table to verify the record not exists",
            RecordsExist( lQuery ) );

      // Verify ACFT-SYS-2-CHILD-TRK-1 is not in inv_inv table
      lQuery = "select 1 from " + TableUtil.INV_INV + " where H_INV_NO_DB_ID="
            + iINV_Ids.getNO_DB_ID() + " and H_INV_NO_ID=" + iINV_Ids.getNO_ID()
            + "  and CONFIG_POS_SDESC='ACFT-SYS-2-CHILD-TRK-1'";
      Assert.assertFalse( "Check inv_inv table to verify the record not exists",
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
      verifyINVINVtable_extend( iINV_Ids, "ACFT_T9", "ASSY-00001", "ENG-ASSY (1)", "ASSY", "0",
            aCOND_CD, "1", aLOC_IDs );
      verifyINVINVtable_extend( iINV_Ids, "ACFT_T9", "ASSY-00002", "ENG-ASSY (2)", "ASSY", "0",
            aCOND_CD, "1", aLOC_IDs );

      // verify ENG-SYS-1
      verifyINVINVtable_extend( iINV_Ids, "ENG_CD9", "XXX", "ENG-ASSY (1) ->ENG-SYS-1", "SYS", "0",
            aCOND_CD, "1", aLOC_IDs );
      verifyINVINVtable_extend( iINV_Ids, "ENG_CD9", "XXX", "ENG-ASSY (2) ->ENG-SYS-1", "SYS", "0",
            aCOND_CD, "1", aLOC_IDs );

      // Verify ENG-ASSY (1) ->ENG-SYS-1-TRK is not in inv_inv table
      lQuery = "select 1 from " + TableUtil.INV_INV + " where H_INV_NO_DB_ID="
            + iINV_Ids.getNO_DB_ID() + " and H_INV_NO_ID=" + iINV_Ids.getNO_ID()
            + "  and CONFIG_POS_SDESC='ENG-ASSY (1) ->ENG-SYS-1-TRK'";
      Assert.assertFalse( "Check inv_inv table to verify the eng record not exists",
            RecordsExist( lQuery ) );

      // verify ENG-ASSY (2) ->ENG-SYS-1-TRK is not in inv_inv table
      lQuery = "select 1 from " + TableUtil.INV_INV + " where H_INV_NO_DB_ID="
            + iINV_Ids.getNO_DB_ID() + " and H_INV_NO_ID=" + iINV_Ids.getNO_ID()
            + "  and CONFIG_POS_SDESC='ENG-ASSY (2) ->ENG-SYS-1-TRK'";
      Assert.assertFalse( "Check inv_inv table to verify the record not exists",
            RecordsExist( lQuery ) );

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

      // Verify total_qt will keep same for archive/scrap imports
      CheckTotalQTuntouched();

   }


   /**
    * This function is to verify inv_inv table.
    *
    *
    */
   public void verifyINVINVtable_extend( simpleIDs aHIDs, String aAssmbl_CD, String aSERIAL_NO_OEM,
         String aCONFIG_POS_SDESC, String aINV_CLASS_CD, String aComplete_BOOL, String aINV_COND_CD,
         String aLOCKED_BOOL, simpleIDs aLOCIds ) {
      // inv_inv table
      String[] iIds = { "COMPLETE_BOOL", "INV_COND_CD", "LOCKED_BOOL", "LOC_DB_ID", "LOC_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "H_INV_NO_DB_ID", aHIDs.getNO_DB_ID() );
      lArgs.addArguments( "H_INV_NO_ID", aHIDs.getNO_ID() );
      lArgs.addArguments( "ASSMBL_CD", aAssmbl_CD );
      lArgs.addArguments( "SERIAL_NO_OEM", aSERIAL_NO_OEM );
      if ( aCONFIG_POS_SDESC != null )
         lArgs.addArguments( "CONFIG_POS_SDESC", aCONFIG_POS_SDESC );
      lArgs.addArguments( "INV_CLASS_CD", aINV_CLASS_CD );

      String iQueryString = TableUtil.buildTableQueryOrderBy( TableUtil.INV_INV, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "COMPLETE_BOOL",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aComplete_BOOL ) );
      Assert.assertTrue( "INV_COND_CD", llists.get( 0 ).get( 1 ).equalsIgnoreCase( aINV_COND_CD ) );
      Assert.assertTrue( "LOCKED_BOOL", llists.get( 0 ).get( 2 ).equalsIgnoreCase( aLOCKED_BOOL ) );
      if ( aLOCIds != null ) {
         Assert.assertTrue( "LOC_DB_ID",
               llists.get( 0 ).get( 3 ).equalsIgnoreCase( aLOCIds.getNO_DB_ID() ) );
         Assert.assertTrue( "LOC_ID",
               llists.get( 0 ).get( 4 ).equalsIgnoreCase( aLOCIds.getNO_ID() ) );
      }

   }


   /**
    * This function is to verify for incomplete loose eng
    *
    *
    */
   public void VerifyCOMPLETE_ENG( String aCOND_CD, simpleIDs aLOC_IDs ) {

      // Get iINV_Ids of eng 1
      iINV_Ids = verifyINVINVtable( "ASSY-00001", "ASSY" );

      // verify ENG-ASSY
      verifyINVINVtable_extend( iINV_Ids, "ENG_CD9", "ASSY-00001", "ENG_CD9", "ASSY", "1", aCOND_CD,
            "1", aLOC_IDs );

      // verify ENG-SYS-1
      verifyINVINVtable_extend( iINV_Ids, "ENG_CD9", "XXX", "ENG-SYS-1", "SYS", "1", aCOND_CD, "1",
            aLOC_IDs );

      // verify ENG-SYS-1-TRK
      verifyINVINVtable_extend( iINV_Ids, "ENG_CD9", "XXX", "ENG-SYS-1-TRK", "TRK", "1", aCOND_CD,
            "1", aLOC_IDs );

      // verify ENG-SYS-1-TRK2
      verifyINVINVtable_extend( iINV_Ids, "ENG_CD9", "XXX", "ENG-SYS-1-TRK2", "TRK", "1", aCOND_CD,
            "1", aLOC_IDs );

      // Verify total_qt will keep same for archive/scrap imports
      CheckTotalQTuntouched();

   }


   /**
    * This function is to verify for complete trk
    *
    *
    */
   public void verifyTRKCOMPCOMPLETE( String aCOND_CD, simpleIDs aLOC_IDs ) {

      // Get iINV_Ids of TRK
      iINV_Ids = verifyINVINVtable( "TRK-00001", "TRK" );

      // Verify ACFT-SYS-1-1-TRK-BATCH-PARENT
      verifyINVINVtable_extend( iINV_Ids, "ACFT_T9", "TRK-00001", "ACFT-SYS-1-1-TRK-BATCH-PARENT",
            "TRK", "1", aCOND_CD, "1", aLOC_IDs );

      // Verify ACFT-SYS-2-CHILD-TRK-1
      verifyINVINVtable_extend( iINV_Ids, "ACFT_T9", "XXX", "ACFT-SYS-2-CHILD-TRK-1", "TRK", "1",
            aCOND_CD, "1", aLOC_IDs );

      // Verify total_qt will keep same for archive/scrap imports
      CheckTotalQTuntouched();

   }


   /**
    * This function is to verify for complete trk
    *
    *
    */
   public void verifySERCOMPCOMPLETE( String aCOND_CD, simpleIDs aLOC_IDs ) {

      // Get iINV_Ids of SER
      iINV_Ids = verifySERINVINVtable( "SER-00001", "SER", aCOND_CD );
      Assert.assertTrue( "INV_IDs should exist.", iINV_Ids != null );

   }


   /**
    * This function is to initializeTotalQT map
    *
    *
    */
   public void initialTotalQTMap() {
      ltotal_qt.clear();
      String[] lPN_OEM = { "ACFT_ASSY_PN8", "ACFT_ASSY_PN9", "ACFT_ASSY_PNY", "ACFT_ASSY_PNZ",
            "ENG_ASSY_PN8", "ENG_ASSY_PN9", "A0000220", "A0000230", "A0000240", "A0000250" };

      for ( int i = 0; i < lPN_OEM.length; i++ ) {
         String lQuery = "Select total_qt from eqp_part_no where part_no_oem='" + lPN_OEM[i] + "'";
         ltotal_qt.put( lPN_OEM[i], getIntValueFromQuery( lQuery, "TOTAL_QT" ) );
      }

   }


   public void CheckTotalQTuntouched() {
      String[] lPN_OEM = { "ACFT_ASSY_PN8", "ACFT_ASSY_PN9", "ACFT_ASSY_PNY", "ACFT_ASSY_PNZ",
            "ENG_ASSY_PN8", "ENG_ASSY_PN9", "A0000220", "A0000230", "A0000240", "A0000250" };

      for ( int i = 0; i < lPN_OEM.length; i++ ) {
         String lQuery = "Select total_qt from eqp_part_no where part_no_oem='" + lPN_OEM[i] + "'";
         int count = getIntValueFromQuery( lQuery, "TOTAL_QT" );
         int expectcount = ltotal_qt.get( lPN_OEM[i] );
         Assert.assertTrue( lPN_OEM[i], count == expectcount );

      }

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

}
