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

import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;
import com.mxi.mx.util.BaselineLoaderTest;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.TestConstants;
import com.mxi.mx.util.ValidationAndImport;
import com.mxi.mx.util.WhereClause;


/**
 * This test suite contains test cases on validation and import functionality of
 * c_stock_part_asgn_import package.
 *
 * @author ALICIA QIAN
 */
public class StockPartAssignment extends BaselineLoaderTest {

   @Rule
   public TestName testName = new TestName();

   public ValidationAndImport ivalidationandimport;

   private String iStockCD1 = "AUTOSTOCK1";
   private String iStockCD2 = "AUTOSTOCK2";
   private String iStockCD3 = "AUTOSTOCK3";
   private String iStockCD4 = "AUTOSTOCK4";
   private String iStockCD5 = "AUTOSTOCK5";

   // private String iManufactCd1 = "1234567890";
   // private String iPNOEM1 = "APU_ASSY_PN1";

   private String iManufactCd1 = "ABC11";
   private String iPNOEM1 = "ENG_ASSY_PN1";

   private String iManufactCd2 = "10001";
   private String iPNOEM2 = "A0000001";

   private String iManufactCd3 = "10001";
   private String iPNOEM3 = "A0000001";

   private String iManufactCd4 = "10001";
   private String iPNOEM4 = "A0000009";

   // private String iManufactCd5 = "1234567890";
   // private String iPNOEM5 = "A0000012";

   private String iManufactCd5 = "ABC11";
   private String iPNOEM5 = "T0000005";

   private String iManufactCd6 = "11111";
   private String iPNOEM6 = "A0000014";

   private ArrayList<String> updateTables = new ArrayList<String>();
   {
      updateTables.add(
            "insert into EQP_STOCK_NO (STOCK_NO_DB_ID, STOCK_NO_ID, QTY_UNIT_DB_ID, QTY_UNIT_CD, ABC_CLASS_DB_ID, ABC_CLASS_CD, INV_CLASS_DB_ID, INV_CLASS_CD, PURCH_TYPE_DB_ID, PURCH_TYPE_CD, STOCK_NO_NAME, STOCK_NO_CD) values (4650, EQP_STOCK_NO_ID.NEXTVAL, 0, 'EA', 0, 'A', 0, 'ASSY', 10, 'ACFT', 'AUTOSTOCKNAME1', 'AUTOSTOCK1')" );
      updateTables.add(
            "insert into EQP_STOCK_NO (STOCK_NO_DB_ID, STOCK_NO_ID, QTY_UNIT_DB_ID, QTY_UNIT_CD, ABC_CLASS_DB_ID, ABC_CLASS_CD, INV_CLASS_DB_ID, INV_CLASS_CD, PURCH_TYPE_DB_ID, PURCH_TYPE_CD, STOCK_NO_NAME, STOCK_NO_CD) values (4650, EQP_STOCK_NO_ID.NEXTVAL, 0, 'EA', 0, 'BLKOUT', 0, 'KIT', 10, 'CMNHW', 'AUTOSTOCKNAME2', 'AUTOSTOCK2')" );
      updateTables.add(
            "insert into EQP_STOCK_NO (STOCK_NO_DB_ID, STOCK_NO_ID, QTY_UNIT_DB_ID, QTY_UNIT_CD, ABC_CLASS_DB_ID, ABC_CLASS_CD, INV_CLASS_DB_ID, INV_CLASS_CD, PURCH_TYPE_DB_ID, PURCH_TYPE_CD, STOCK_NO_NAME, STOCK_NO_CD) values (4650, EQP_STOCK_NO_ID.NEXTVAL, 0, 'EA', 0, 'B', 0, 'TRK', 10, 'ENG', 'AUTOSTOCKNAME3', 'AUTOSTOCK3')" );
      updateTables.add(
            "insert into EQP_STOCK_NO (STOCK_NO_DB_ID, STOCK_NO_ID, QTY_UNIT_DB_ID, QTY_UNIT_CD, ABC_CLASS_DB_ID, ABC_CLASS_CD, INV_CLASS_DB_ID, INV_CLASS_CD, PURCH_TYPE_DB_ID, PURCH_TYPE_CD, STOCK_NO_NAME, STOCK_NO_CD) values (4650, EQP_STOCK_NO_ID.NEXTVAL, 0, 'EA', 0, 'C', 0, 'BATCH', 10, 'CONSUM', 'AUTOSTOCKNAME4', 'AUTOSTOCK4')" );
      updateTables.add(
            "insert into EQP_STOCK_NO (STOCK_NO_DB_ID, STOCK_NO_ID, QTY_UNIT_DB_ID, QTY_UNIT_CD, ABC_CLASS_DB_ID, ABC_CLASS_CD, INV_CLASS_DB_ID, INV_CLASS_CD, PURCH_TYPE_DB_ID, PURCH_TYPE_CD, STOCK_NO_NAME, STOCK_NO_CD) values (4650, EQP_STOCK_NO_ID.NEXTVAL, 0, 'EA', 0, 'D', 0, 'SER', 10, 'OFFICE', 'AUTOSTOCKNAME5', 'AUTOSTOCK5')" );
      updateTables.add(
            "update eqp_part_no set stock_no_db_id=null, stock_no_id=null,  stock_pct=null where part_no_oem='APU_ASSY_PN1' and manufact_cd='1234567890'" );
      updateTables.add(
            "update eqp_part_no set stock_no_db_id=null, stock_no_id=null,  stock_pct=null where part_no_oem='A0000001' and manufact_cd='10001'" );
      updateTables.add(
            "update eqp_part_no set stock_no_db_id=null, stock_no_id=null,  stock_pct=null where part_no_oem='A0000009' and manufact_cd='10001'" );
      updateTables.add(
            "update eqp_part_no set stock_no_db_id=null, stock_no_id=null,  stock_pct=null where part_no_oem='A0000012' and manufact_cd='1234567890'" );
      updateTables.add(
            "update eqp_part_no set stock_no_db_id=null, stock_no_id=null,  stock_pct=null where part_no_oem='A0000014' and manufact_cd='11111'" );
      updateTables.add(
            "update eqp_part_no set stock_no_db_id=null, stock_no_id=null,  stock_pct=null where part_no_oem='ENG_ASSY_PN1' and manufact_cd='ABC11'" );
      updateTables.add(
            "update eqp_part_no set stock_no_db_id=null, stock_no_id=null,  stock_pct=null where part_no_oem='T0000005' and manufact_cd='ABC11'" );

   };

   private ArrayList<String> restorTables = new ArrayList<String>();
   {
      updateTables.add(
            "update eqp_part_no set stock_no_db_id=null, stock_no_id=null,  stock_pct=null where part_no_oem='APU_ASSY_PN1' and manufact_cd='1234567890'" );
      updateTables.add(
            "update eqp_part_no set stock_no_db_id=null, stock_no_id=null,  stock_pct=null where part_no_oem='A0000001' and manufact_cd='10001'" );
      updateTables.add(
            "update eqp_part_no set stock_no_db_id=null, stock_no_id=null,  stock_pct=null where part_no_oem='A0000009' and manufact_cd='10001'" );
      updateTables.add(
            "update eqp_part_no set stock_no_db_id=null, stock_no_id=null,  stock_pct=null where part_no_oem='A0000012' and manufact_cd='1234567890'" );
      updateTables.add(
            "update eqp_part_no set stock_no_db_id=null, stock_no_id=null,  stock_pct=null where part_no_oem='A0000014' and manufact_cd='11111'" );
      updateTables.add(
            "update eqp_part_no set stock_no_db_id=null, stock_no_id=null,  stock_pct=null where part_no_oem='ENG_ASSY_PN1' and manufact_cd='ABC11'" );
      updateTables.add(
            "update eqp_part_no set stock_no_db_id=null, stock_no_id=null,  stock_pct=null where part_no_oem='T0000005' and manufact_cd='ABC11'" );

   };


   /**
    * Setup before executing each individual test
    *
    */
   @Before
   @Override
   public void before() throws Exception {

      super.before();
      clearBaselineLoaderTables();
      runSqlsInTable( updateTables );

   }


   /**
    * Clean up after each individual test
    */
   @After
   @Override
   public void after() {
      runSqlsInTable( updateTables );
      RestoreData();
      super.after();
   }


   /**
    * This test is to verify c_stock_part_asgn_import validation functionality of staging table
    * C_ASSIGN_PARTS_TO_STOCK, on INV_CLASS_CD=ASSY
    *
    */
   @Test
   public void testASSYVALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_assign_parts_to_stock table
      Map<String, String> lstockpart = new LinkedHashMap<>();

      lstockpart.put( "STOCK_NO_CD", "\'" + iStockCD1 + "\'" );
      lstockpart.put( "MANUFACT_CD", "\'" + iManufactCd1 + "\'" );
      lstockpart.put( "PART_NO_OEM", "\'" + iPNOEM1 + "\'" );
      lstockpart.put( "STOCK_PCT", "\'1\'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_ASSIGN_PARTS_TO_STOCK, lstockpart ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify c_stock_part_asgn_import import functionality of staging table
    * C_ASSIGN_PARTS_TO_STOCK, on INV_CLASS_CD=ASSY
    *
    */
   @Test
   public void testASSYIMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testASSYVALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify EQP_PART_NO
      simpleIDs lstockIds = GetStockIDs( iStockCD1 );
      verifyEQPPARTNO( iManufactCd1, iPNOEM1, lstockIds, "1" );

   }


   /**
    * This test is to verify c_stock_part_asgn_import validation functionality of staging table
    * C_ASSIGN_PARTS_TO_STOCK, on INV_CLASS_CD=KIT
    *
    */
   @Test
   public void testKITTRKVALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_assign_parts_to_stock table
      Map<String, String> lstockpart = new LinkedHashMap<>();

      lstockpart.put( "STOCK_NO_CD", "\'" + iStockCD2 + "\'" );
      lstockpart.put( "MANUFACT_CD", "\'" + iManufactCd2 + "\'" );
      lstockpart.put( "PART_NO_OEM", "\'" + iPNOEM2 + "\'" );
      lstockpart.put( "STOCK_PCT", "\'1\'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_ASSIGN_PARTS_TO_STOCK, lstockpart ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify c_stock_part_asgn_import import functionality of staging table
    * C_ASSIGN_PARTS_TO_STOCK, on INV_CLASS_CD=KIT will be modified to TRK after import
    *
    */
   @Test
   public void testKITTRKIMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testKITTRKVALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify EQP_PART_NO
      simpleIDs lstockIds = GetStockIDs( iStockCD2 );
      verifyEQPPARTNO( iManufactCd2, iPNOEM2, lstockIds, "1" );

      // Verify EQP_STOCK_NO INV_CLASS_CD updated from KIT to TRK
      verifyEQPSTOCKNO( lstockIds, "TRK" );

   }


   /**
    * This test is to verify c_stock_part_asgn_import validation functionality of staging table
    * C_ASSIGN_PARTS_TO_STOCK, on INV_CLASS_CD=TRK
    *
    */
   @Test
   public void testTRKVALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_assign_parts_to_stock table
      Map<String, String> lstockpart = new LinkedHashMap<>();

      lstockpart.put( "STOCK_NO_CD", "\'" + iStockCD3 + "\'" );
      lstockpart.put( "MANUFACT_CD", "\'" + iManufactCd3 + "\'" );
      lstockpart.put( "PART_NO_OEM", "\'" + iPNOEM3 + "\'" );
      lstockpart.put( "STOCK_PCT", "\'1\'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_ASSIGN_PARTS_TO_STOCK, lstockpart ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify c_stock_part_asgn_import import functionality of staging table
    * C_ASSIGN_PARTS_TO_STOCK, on INV_CLASS_CD=TRK
    *
    */
   @Test
   public void testTRKIMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testTRKVALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify EQP_PART_NO
      simpleIDs lstockIds = GetStockIDs( iStockCD3 );
      verifyEQPPARTNO( iManufactCd3, iPNOEM3, lstockIds, "1" );

   }


   /**
    * This test is to verify c_stock_part_asgn_import validation functionality of staging table
    * C_ASSIGN_PARTS_TO_STOCK, on INV_CLASS_CD=BATCH
    *
    */
   @Test
   public void testBATCHVALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_assign_parts_to_stock table
      Map<String, String> lstockpart = new LinkedHashMap<>();

      lstockpart.put( "STOCK_NO_CD", "\'" + iStockCD4 + "\'" );
      lstockpart.put( "MANUFACT_CD", "\'" + iManufactCd4 + "\'" );
      lstockpart.put( "PART_NO_OEM", "\'" + iPNOEM4 + "\'" );
      lstockpart.put( "STOCK_PCT", "\'1\'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_ASSIGN_PARTS_TO_STOCK, lstockpart ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify c_stock_part_asgn_import import functionality of staging table
    * C_ASSIGN_PARTS_TO_STOCK, on INV_CLASS_CD=Batch
    *
    */
   @Test
   public void testBATCHIMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testBATCHVALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify EQP_PART_NO
      simpleIDs lstockIds = GetStockIDs( iStockCD4 );
      verifyEQPPARTNO( iManufactCd4, iPNOEM4, lstockIds, "1" );

   }


   /**
    * This test is to verify c_stock_part_asgn_import validation functionality of staging table
    * C_ASSIGN_PARTS_TO_STOCK, on INV_CLASS_CD=SER with 2 records
    *
    */
   @Test
   public void testSERVALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_assign_parts_to_stock table
      Map<String, String> lstockpart = new LinkedHashMap<>();

      lstockpart.put( "STOCK_NO_CD", "\'" + iStockCD5 + "\'" );
      lstockpart.put( "MANUFACT_CD", "\'" + iManufactCd5 + "\'" );
      lstockpart.put( "PART_NO_OEM", "\'" + iPNOEM5 + "\'" );
      lstockpart.put( "STOCK_PCT", "\'0.5\'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_ASSIGN_PARTS_TO_STOCK, lstockpart ) );

      // Insert second record
      lstockpart.put( "STOCK_NO_CD", "\'" + iStockCD5 + "\'" );
      lstockpart.put( "MANUFACT_CD", "\'" + iManufactCd6 + "\'" );
      lstockpart.put( "PART_NO_OEM", "\'" + iPNOEM6 + "\'" );
      lstockpart.put( "STOCK_PCT", "\'0.5\'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_ASSIGN_PARTS_TO_STOCK, lstockpart ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify c_stock_part_asgn_import import functionality of staging table
    * C_ASSIGN_PARTS_TO_STOCK, on INV_CLASS_CD=SER with 2 records
    *
    */
   @Test
   public void testSERIMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testSERVALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify EQP_PART_NO
      simpleIDs lstockIds = GetStockIDs( iStockCD5 );
      verifyEQPPARTNO( iManufactCd5, iPNOEM5, lstockIds, "0.5" );
      verifyEQPPARTNO( iManufactCd6, iPNOEM6, lstockIds, "0.5" );

   }


   /**
    * This test is to verify PARTSTOSTCK-12011:C_ASSIGN_PARTS_TO_STOCK.MANUFACT_CD/PART_NO_OEM is
    * currently assigned to a stock code that is not part of the data set being modified. The
    * specified MANUFACT_CD/PART_NO_OEM cannot be unassigned from its stock as it will make its
    * stock part percentage distribution incomplete.
    *
    */
   @Test
   public void testPARTSTOSTCK_12011() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_assign_parts_to_stock table
      Map<String, String> lstockpart = new LinkedHashMap<>();

      // valid record
      lstockpart.put( "STOCK_NO_CD", "\'" + iStockCD3 + "\'" );
      lstockpart.put( "MANUFACT_CD", "\'" + iManufactCd3 + "\'" );
      lstockpart.put( "PART_NO_OEM", "\'" + iPNOEM3 + "\'" );
      lstockpart.put( "STOCK_PCT", "\'0\'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_ASSIGN_PARTS_TO_STOCK, lstockpart ) );

      // invalid record
      lstockpart.clear();
      lstockpart.put( "STOCK_NO_CD", "\'" + iStockCD3 + "\'" );
      lstockpart.put( "MANUFACT_CD", "'1234567890'" );
      lstockpart.put( "PART_NO_OEM", "'A0000008'" );
      lstockpart.put( "STOCK_PCT", "\'1\'" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_ASSIGN_PARTS_TO_STOCK, lstockpart ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) != 1 );

      // Check error
      checkAssignPartsErrorCode( "testPARTSTOSTCK_12011", "PARTSTOSTCK-12011" );

   }


   // =====================================================================

   /**
    * This function is to retrieve Stock IDs for eqp_stock_no table
    *
    *
    */
   public simpleIDs GetStockIDs( String aStockCD ) {

      // EQP_STOCK_NO table
      String[] iIds = { "STOCK_NO_DB_ID", "STOCK_NO_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "STOCK_NO_CD", aStockCD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EQP_STOCK_NO, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lIds;

   }


   /**
    * This function is to verify EQP_PART_NO table given parameters.
    *
    *
    */

   public void verifyEQPPARTNO( String aManufactCd, String aPNOEM, simpleIDs aStockIds,
         String aPCT ) {
      // EQP_STOCK_NO table
      String[] iIds = { "STOCK_NO_DB_ID", "STOCK_NO_ID", "STOCK_PCT" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "MANUFACT_CD", aManufactCd );
      lArgs.addArguments( "PART_NO_OEM", aPNOEM );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EQP_PART_NO, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Verification
      Assert.assertTrue( "STOCK_NO_DB_ID",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aStockIds.getNO_DB_ID() ) );
      Assert.assertTrue( "STOCK_NO_ID",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( aStockIds.getNO_ID() ) );
      Assert.assertTrue( "STOCK_PCT", llists.get( 0 ).get( 2 ).equalsIgnoreCase( aPCT ) );

   }


   /**
    * This function is to verify EQP_Stock_NO table given parameters.
    *
    *
    */
   public void verifyEQPSTOCKNO( simpleIDs aStockIds, String aINVCLASSCD ) {
      // EQP_STOCK_NO table
      String[] iIds = { "INV_CLASS_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "STOCK_NO_DB_ID", aStockIds.getNO_DB_ID() );
      lArgs.addArguments( "STOCK_NO_ID", aStockIds.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EQP_STOCK_NO, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Verification
      Assert.assertTrue( "INV_CLASS_CD", llists.get( 0 ).get( 0 ).equalsIgnoreCase( aINVCLASSCD ) );

   }


   /**
    * Calls check error code
    *
    *
    */
   protected void checkAssignPartsErrorCode( String aTestName, String aValidationCode ) {

      String lquery = TestConstants.C_ASSIGN_PARTS_ERROR_CHECK;

      checkErrorCode( lquery, aTestName, aValidationCode );

   }


   /**
    * This function is called by after test method to restore data which has been modified by tests.
    *
    *
    */

   public void RestoreData() {
      // Delete EQP_STOCK_NO table
      String lStrDeleteEQPSTOCKNO =
            "delete from " + TableUtil.EQP_STOCK_NO + " where STOCK_NO_CD like '%AUTO%'";
      executeSQL( lStrDeleteEQPSTOCKNO );

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
                     "BEGIN c_stock_part_asgn_import.validate_stock_part_asgn(on_retcode => ?); END;" );

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
                     "BEGIN c_stock_part_asgn_import.import_stock_part_asgn(on_retcode => ?); END;" );

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
