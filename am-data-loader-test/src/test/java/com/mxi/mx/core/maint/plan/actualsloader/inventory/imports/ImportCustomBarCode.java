package com.mxi.mx.core.maint.plan.actualsloader.inventory.imports;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
 * This test suite contains tests on barcode import functionality.
 *
 */
public class ImportCustomBarCode extends AbstractImportInventory {

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

   }


   /**
    * This test is to verify :Barcode is auto created when bar code is not provided. This test case
    * is including ACFT, Attached ENG, loose ENG and SER on ACFT.
    *
    *
    */

   public void testACFT_ENG_SER_AUTOCREATION_VALIDATION() throws Exception {

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
    * This test is to verify :Barcode is auto created when bar code is not provided. This test case
    * is including ACFT, Attached ENG, loose ENG and SER on ACFT.
    *
    *
    */
   @Test
   public void testACFT_ENG_SER_AUTOCREATION_IMPORT() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testACFT_ENG_SER_AUTOCREATION_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runIMPORTInv( "FULL" );

      // Get iINV_Ids of ACFT
      iINV_Ids = verifyINVINVtable( "ACFT-00001", "ACFT" );
      // Get iINV_Ids of eng 1
      iINV_Ids_Assy = verifyINVINVtable( "ASSY-00001", "ASSY" );
      // Get iINV_Ids of eng 2
      iINV_Ids_Assy_2 = verifyINVINVtable( "ASSY-00002", "ASSY" );
      // Get SER_IDs
      simpleIDs lSER_IDs = verifyINVINVtable( "SER0001", "SER" );

      // This is for restoring data.
      iEVENT_Ids_FG_1 =
            getEventIds( iEVENT_TYPE_CD_FG, iEVENT_STATUS_CD_FGINST, iEVENT_SDESC_ASSY_FGINST_1 );

      // Verify barcdoe auto generation
      verifyINVINVtable_Barcode( iINV_Ids, null );
      verifyINVINVtable_Barcode( iINV_Ids_Assy, null );
      verifyINVINVtable_Barcode( iINV_Ids_Assy_2, null );
      verifyINVINVtable_Barcode( lSER_IDs, null );

   }


   /**
    * This test is to verify :Barcode is imported successfully when bar code is provided. This test
    * case is including ACFT, Attached ENG, loose ENG and TRK on ACFT.
    *
    *
    */

   public void testACFT_ENG_TRK_BARCODE_VALIDATION() throws Exception {

      // create inventory map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-00001'" );
      lMapInventory.put( "PART_NO_OEM", "'ACFT_ASSY_PN8'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "AC_REG_CD", "'AC-TEST'" );
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "CUSTOM_BARCODE", "'ABC12345A'" );
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
      lMapInventory.put( "CUSTOM_BARCODE", "'ABC12345B'" );
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
      lMapInventory.put( "CUSTOM_BARCODE", "'ABC12345C'" );
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

      // create sub inv map
      Map<String, String> lMapInventoryChild = new LinkedHashMap<String, String>();
      lMapInventoryChild.put( "PARENT_SERIAL_NO_OEM", "'ACFT-00001'" );
      lMapInventoryChild.put( "PARENT_PART_NO_OEM", "'ACFT_ASSY_PN8'" );
      lMapInventoryChild.put( "PARENT_MANUFACT_CD", "'10001'" );
      // lMapInventoryChild.put( "BOM_PART_CD", "'ACFT-SYS-2-CHILD-TRK-2'" );
      // lMapInventoryChild.put( "EQP_POS_CD", "'1'" );
      lMapInventoryChild.put( "SERIAL_NO_OEM", "'TRK0001'" );
      lMapInventoryChild.put( "PART_NO_OEM", "'A0000220'" );
      lMapInventoryChild.put( "MANUFACT_CD", "'11111'" );
      lMapInventoryChild.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventoryChild.put( "CUSTOM_BARCODE", "'ABC12345D'" );
      lMapInventoryChild.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventoryChild ) );

      // call inventory validation & import
      runValidateInv( "FULL" );
      checkInventoryNoWarnings( "PASS" );

   }


   /**
    * This test is to verify :Barcode is imported successfully when bar code is provided. This test
    * case is including ACFT, Attached ENG, loose ENG and TRK on ACFT.
    *
    *
    */
   @Test
   public void testACFT_ENG_TRK_BARCODE_IMPORT() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testACFT_ENG_TRK_BARCODE_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runIMPORTInv( "FULL" );

      // Get iINV_Ids of ACFT
      iINV_Ids = verifyINVINVtable( "ACFT-00001", "ACFT" );
      // Get iINV_Ids of eng 1
      iINV_Ids_Assy = verifyINVINVtable( "ASSY-00001", "ASSY" );
      // Get iINV_Ids of eng 2
      iINV_Ids_Assy_2 = verifyINVINVtable( "ASSY-00002", "ASSY" );
      // Get SER_IDs
      simpleIDs lTRK_IDs = verifyINVINVtable( "TRK0001", "TRK" );

      // This is for restoring data.
      iEVENT_Ids_FG_1 =
            getEventIds( iEVENT_TYPE_CD_FG, iEVENT_STATUS_CD_FGINST, iEVENT_SDESC_ASSY_FGINST_1 );

      // Verify barcdoe auto generation
      verifyINVINVtable_Barcode( iINV_Ids, "ABC12345A" );
      verifyINVINVtable_Barcode( iINV_Ids_Assy, "ABC12345B" );
      verifyINVINVtable_Barcode( iINV_Ids_Assy_2, "ABC12345C" );
      verifyINVINVtable_Barcode( lTRK_IDs, "ABC12345D" );

   }


   /**
    * This test is to verify :Barcode is imported successfully when provided bar code is same as
    * system auto genetated one.
    *
    *
    */
   @Test
   public void testLOOSE_BATCH_VALIDATION() throws Exception {

      String nextSeq = getNextBARCODESequence();

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();
      lMapInventory.put( "SERIAL_NO_OEM", "'BARCH0001'" );
      lMapInventory.put( "BATCH_NO_OEM", "'BARCH0001'" );
      lMapInventory.put( "PART_NO_OEM", "'CHW000035'" );
      lMapInventory.put( "INV_CLASS_CD", "'BATCH'" );
      lMapInventory.put( "MANUFACT_CD", "'ABC11'" );
      lMapInventory.put( "OWNER_CD", "'MXI'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "BIN_QT", "1" );
      lMapInventory.put( "INV_COND_CD", "'RFI'" );
      lMapInventory.put( "CUSTOM_BARCODE", "'" + nextSeq + "'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // second loose TRK
      lMapInventory.clear();
      lMapInventory.put( "SERIAL_NO_OEM", "'TRK0001'" );
      lMapInventory.put( "PART_NO_OEM", "'A0000220'" );
      lMapInventory.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventory.put( "MANUFACT_CD", "'11111'" );
      lMapInventory.put( "OWNER_CD", "'MXI'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'RFI'" );
      // lMapInventory.put( "CUSTOM_BARCODE", "'"+ nextSeq+"'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // call inventory validation & import
      runValidateInv( "FULL" );
      checkInventoryNoWarnings( "PASS" );

   }


   /**
    * This test is to verify :Barcode is imported successfully when provided bar code is same as
    * system auto genetated one.
    *
    *
    */
   @Test
   public void testLOOSE_BATCH_IMPORT() throws Exception {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testLOOSE_BATCH_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      runIMPORTInv( "FULL" );

      // Get iINV_Ids of ACFT
      iINV_Ids = verifyINVINVtable( "BARCH0001", "BATCH" );
      // Get TRK_IDs
      iINV_ACFT_TRK_Ids = verifyINVINVtable( "TRK0001", "TRK" );

      // get barcode from staging table
      String lquery = "Select CUSTOM_BARCODE from " + TableUtil.C_RI_INVENTORY
            + " where SERIAL_NO_OEM='BARCH0001'";
      String lBarcode = getStringValueFromQuery( lquery, "CUSTOM_BARCODE" );
      // Verify barcdoe auto generation
      verifyINVINVtable_Barcode( iINV_Ids, lBarcode );
      verifyINVINVtable_Barcode( iINV_ACFT_TRK_Ids, null );

   }


   // ================================================================
   /**
    *
    * This will call the sequence in the database and will generate a new BAR CODE which has ending
    * letter as 1 .
    *
    * @param aSequence
    *           - Name of function which generate new bar code
    * @return lId - new Id generated
    */

   public String getNextBARCODESequence() {
      String lBarCode = "";
      do {
         lBarCode = getNextValueInBARCODESequence();

      } while ( lBarCode.charAt( lBarCode.length() - 1 ) != '0' );

      return lBarCode.substring( 0, lBarCode.length() - 1 ) + "1";

   }


   /**
    *
    * This will call the sequence in the database and will generate a new BAR CODE.
    *
    * @return lId - new Id generated
    */

   public String getNextValueInBARCODESequence() {
      String lQuery = "SELECT generate_inv_barcode FROM dual";
      String lId = "";

      try {
         PreparedStatement lStatement = getConnection().prepareStatement( lQuery,
               ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY );
         ResultSet lResultSet = lStatement.executeQuery( lQuery );

         lResultSet.next();
         lId = lResultSet.getString( "generate_inv_barcode" );
      } catch ( SQLException e ) {
         e.printStackTrace();
      }
      return lId;

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


   /**
    * This function is to verify inv_inv table.
    *
    *
    */
   public void verifyINVINVtable_Barcode( simpleIDs aINV_ID, String aBARCODE_SDESC ) {
      // inv_inv table
      String[] iIds = { "BARCODE_SDESC" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "INV_NO_DB_ID", aINV_ID.getNO_DB_ID() );
      lArgs.addArguments( "INV_NO_ID", aINV_ID.getNO_ID() );

      String iQueryString = TableUtil.buildTableQueryOrderBy( TableUtil.INV_INV, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      if ( aBARCODE_SDESC != null ) {
         Assert.assertTrue( "BARCODE_SDESC",
               llists.get( 0 ).get( 0 ).equalsIgnoreCase( aBARCODE_SDESC ) );
      } else {
         Assert.assertTrue( "BARCODE_SDESC", !llists.get( 0 ).get( 0 ).isEmpty() );
      }

   }


   /**
    * This function is called before each tests for setting up id as null
    *
    *
    */
   public void clearIDs() {
      iINV_Ids = null;
      iINV_Ids_Assy = null;
      iINV_Ids_Assy_2 = null;
      iEVENT_Ids_FG_1 = null;
      // iEVENT_Ids_FG_2 = null;
      iINV_ACFT_TRK_Ids = null;

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
      RestoreINVData( iINV_ACFT_TRK_Ids );

      clearEvent( iEVENT_Ids_FG_1 );

   }

}
