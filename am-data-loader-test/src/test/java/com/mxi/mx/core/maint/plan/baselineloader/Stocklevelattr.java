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
 * c_stock_lvl_alloc_import and c_stock_lvl_attr_import package in loc and attr area.
 *
 * @author ALICIA QIAN
 */
public class Stocklevelattr extends BaselineLoaderTest {

   @Rule
   public TestName testName = new TestName();
   ValidationAndImport ivalidationandimportLoc;
   ValidationAndImport ivalidationandimportAttr;

   private String iStockCD1 = "AUTOSTOCK1";
   private String iStockCD2 = "AUTOSTOCK2";

   private String iLocCD1 = "AIRPORT1";
   private String iLocCD2 = "AIRPORT3";
   private String iOwnerCD = "10001";
   private String iOwnerName = "Vendor 1";
   private String iSTOCKLOWACTNCD = "POREQ";

   private String iOwnerCD_LONG = "VERYVERYLONGNAME";
   private String iOwnerName_LONG = "VERYVERYLONGNAME";

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
      // RestoreData();
      runSqlsInTable( updateTables );

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
    * This test is to verify c_stock_lvl_alloc_import validation functionality of staging table
    * c_stock_level_alloc and c_stock_level_attr on INV_CLASS_CD=ASSY
    *
    */
   public void testASSYVALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_stock_level_alloc table
      Map<String, String> lstocklevel = new LinkedHashMap<>();

      lstocklevel.put( "STOCK_NO_CD", "\'" + iStockCD1 + "\'" );
      lstocklevel.put( "LOC_CD", "\'" + iLocCD1 + "\'" );
      lstocklevel.put( "OWNER_CD", "\'" + iOwnerCD + "\'" );
      lstocklevel.put( "WEIGHT_FACTOR_QT", "\'2\'" );
      lstocklevel.put( "ALLOC_PCT", "\'1\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STOCK_LEVEL_ALLOC, lstocklevel ) );

      // c_stock_level_attr table
      lstocklevel.clear();
      lstocklevel.put( "STOCK_NO_CD", "\'" + iStockCD1 + "\'" );
      lstocklevel.put( "LOC_CD", "\'" + iLocCD1 + "\'" );
      lstocklevel.put( "OWNER_CD", "\'" + iOwnerCD + "\'" );
      lstocklevel.put( "MIN_REORDER_QT", "\'1\'" );
      lstocklevel.put( "REORDER_LEVEL", "\'2\'" );
      lstocklevel.put( "MAX_QT", "\'3\'" );
      lstocklevel.put( "STOCK_LOW_ACTN_CD", "\'" + iSTOCKLOWACTNCD + "\'" );
      lstocklevel.put( "REORDER_QT", "\'2\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STOCK_LEVEL_ATTR, lstocklevel ) );

      // run validation
      Assert.assertTrue( runValidationAndImportLoc( true, true ) == 1 );

   }


   /**
    * This test is to verify c_stock_lvl_alloc_import import functionality of staging table
    * c_stock_level_alloc and c_stock_level_attr on INV_CLASS_CD=ASSY and c_stock_lvl_attr_import
    * validation functionality
    *
    */
   @Test
   public void testASSYIMPORT_AttrVALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testASSYVALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImportLoc( false, true ) == 1 );

      // run validation
      Assert.assertTrue( runValidationAndImportAttr( true, true ) == 1 );

   }


   /**
    * This test is to verify c_stock_lvl_alloc_import import functionality of staging table
    * c_stock_level_alloc and c_stock_level_attr on INV_CLASS_CD=ASSY and c_stock_lvl_attr_import
    * import functionality
    *
    */
   @Test
   public void testASSYIMPORT_AttrIMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testASSYIMPORT_AttrVALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImportAttr( false, true ) == 1 );

      // Verify the inv_loc_stock
      simpleIDs llocIds = GetLocIDs( iLocCD1 );
      simpleIDs lStockIds = GetStockIDs( iStockCD1 );
      simpleIDs lOwnerIds = OwnerIDs( iOwnerCD, iOwnerName );
      verifyINVLOCSTOCK( lStockIds, llocIds, lOwnerIds, "2", "1", iSTOCKLOWACTNCD, "1", "3", "2" );

   }


   /**
    * This test is to verify c_stock_lvl_alloc_import validation functionality of staging table
    * c_stock_level_alloc and c_stock_level_attr on INV_CLASS_CD=KIT
    *
    */
   public void testKITVALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_stock_level_alloc table
      Map<String, String> lstocklevel = new LinkedHashMap<>();

      lstocklevel.put( "STOCK_NO_CD", "\'" + iStockCD2 + "\'" );
      lstocklevel.put( "LOC_CD", "\'" + iLocCD2 + "\'" );
      lstocklevel.put( "OWNER_CD", "\'" + iOwnerCD + "\'" );
      lstocklevel.put( "WEIGHT_FACTOR_QT", "\'2\'" );
      lstocklevel.put( "ALLOC_PCT", "\'1\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STOCK_LEVEL_ALLOC, lstocklevel ) );

      // c_stock_level_attr table
      lstocklevel.clear();
      lstocklevel.put( "STOCK_NO_CD", "\'" + iStockCD2 + "\'" );
      lstocklevel.put( "LOC_CD", "\'" + iLocCD2 + "\'" );
      lstocklevel.put( "OWNER_CD", "\'" + iOwnerCD + "\'" );
      lstocklevel.put( "MIN_REORDER_QT", "\'1\'" );
      lstocklevel.put( "REORDER_LEVEL", "\'2\'" );
      lstocklevel.put( "MAX_QT", "\'3\'" );
      lstocklevel.put( "STOCK_LOW_ACTN_CD", "\'" + iSTOCKLOWACTNCD + "\'" );
      lstocklevel.put( "REORDER_QT", "\'2\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STOCK_LEVEL_ATTR, lstocklevel ) );

      // run validation
      Assert.assertTrue( runValidationAndImportLoc( true, true ) == 1 );

   }


   /**
    * This test is to verify c_stock_lvl_alloc_import import functionality of staging table
    * c_stock_level_alloc and c_stock_level_attr on INV_CLASS_CD=KIT and c_stock_lvl_attr_import
    * validation functionality
    *
    */
   @Test
   public void testKITIMPORT_AttrVALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testKITVALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImportLoc( false, true ) == 1 );

      // run validation
      Assert.assertTrue( runValidationAndImportAttr( true, true ) == 1 );

   }


   /**
    * This test is to verify c_stock_lvl_alloc_import import functionality of staging table
    * c_stock_level_alloc and c_stock_level_attr on INV_CLASS_CD=KIT and c_stock_lvl_attr_import
    * import functionality
    *
    */
   @Test
   public void testKITIMPORT_AttrIMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testKITIMPORT_AttrVALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImportAttr( false, true ) == 1 );

      // Verify the inv_loc_stock
      simpleIDs llocIds = GetLocIDs( iLocCD2 );
      simpleIDs lStockIds = GetStockIDs( iStockCD2 );
      simpleIDs lOwnerIds = OwnerIDs( iOwnerCD, iOwnerName );
      verifyINVLOCSTOCK( lStockIds, llocIds, lOwnerIds, "2", "1", iSTOCKLOWACTNCD, "1", "3", "2" );

   }


   // ====================================================================================================

   public void verifyINVLOCSTOCK( simpleIDs aStockIds, simpleIDs alocIds, simpleIDs aOwnerIds,
         String aWeightFactor, String aAllocPCT, String aStockLowActnCD, String aMINRecordQT,
         String aMAXQT, String aRecorderQT ) {

      // INV_LOC_STOCK table
      String[] iIds = { "LOC_DB_ID", "LOC_ID", "OWNER_DB_ID", "OWNER_ID", "WEIGHT_FACTOR_QT",
            "ALLOC_PCT", "STOCK_LOW_ACTN_CD", "MIN_REORDER_QT", "MAX_QT", "REORDER_QT" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "STOCK_NO_DB_ID", aStockIds.getNO_DB_ID() );
      lArgs.addArguments( "STOCK_NO_ID", aStockIds.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.INV_LOC_STOCK, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "LOC_DB_ID",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( alocIds.getNO_DB_ID() ) );
      Assert.assertTrue( "LOC_DB_ID",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( alocIds.getNO_ID() ) );
      Assert.assertTrue( "OWNER_DB_ID",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aOwnerIds.getNO_DB_ID() ) );
      Assert.assertTrue( "OWNER_ID",
            llists.get( 0 ).get( 3 ).equalsIgnoreCase( aOwnerIds.getNO_ID() ) );
      Assert.assertTrue( "WEIGHT_FACTOR_QT",
            llists.get( 0 ).get( 4 ).equalsIgnoreCase( aWeightFactor ) );
      Assert.assertTrue( "ALLOC_PCT", llists.get( 0 ).get( 5 ).equalsIgnoreCase( aAllocPCT ) );
      Assert.assertTrue( "STOCK_LOW_ACTN_CD",
            llists.get( 0 ).get( 6 ).equalsIgnoreCase( aStockLowActnCD ) );
      Assert.assertTrue( "MIN_REORDER_QT",
            llists.get( 0 ).get( 7 ).equalsIgnoreCase( aMINRecordQT ) );
      Assert.assertTrue( "MAX_QT", llists.get( 0 ).get( 8 ).equalsIgnoreCase( aMAXQT ) );
      Assert.assertTrue( "REORDER_QT", llists.get( 0 ).get( 9 ).equalsIgnoreCase( aRecorderQT ) );

   }


   /**
    * This test is to verify the fix OPER-5700: C_STOCK_LEVEL_ATTR and C_STOCK_LEVEL_ALLOC;
    *
    */

   public void test_OPER_5700_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_stock_level_alloc table
      Map<String, String> lstocklevel = new LinkedHashMap<>();

      lstocklevel.put( "STOCK_NO_CD", "\'" + iStockCD1 + "\'" );
      lstocklevel.put( "LOC_CD", "\'" + iLocCD1 + "\'" );
      lstocklevel.put( "OWNER_CD", "\'" + iOwnerCD_LONG + "\'" );
      lstocklevel.put( "WEIGHT_FACTOR_QT", "\'2\'" );
      lstocklevel.put( "ALLOC_PCT", "\'1\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STOCK_LEVEL_ALLOC, lstocklevel ) );

      // c_stock_level_attr table
      lstocklevel.clear();
      lstocklevel.put( "STOCK_NO_CD", "\'" + iStockCD1 + "\'" );
      lstocklevel.put( "LOC_CD", "\'" + iLocCD1 + "\'" );
      lstocklevel.put( "OWNER_CD", "\'" + iOwnerCD_LONG + "\'" );
      lstocklevel.put( "MIN_REORDER_QT", "\'1\'" );
      lstocklevel.put( "REORDER_LEVEL", "\'2\'" );
      lstocklevel.put( "MAX_QT", "\'3\'" );
      lstocklevel.put( "STOCK_LOW_ACTN_CD", "\'" + iSTOCKLOWACTNCD + "\'" );
      lstocklevel.put( "REORDER_QT", "\'2\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STOCK_LEVEL_ATTR, lstocklevel ) );

      // run validation
      Assert.assertTrue( runValidationAndImportLoc( true, true ) == 1 );

   }


   /**
    * This test is to verify the fix OPER-5700: C_STOCK_LEVEL_ATTR and C_STOCK_LEVEL_ALLOC;
    *
    */

   @Test
   public void test_OPER_5700_AttrVALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      test_OPER_5700_VALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImportLoc( false, true ) == 1 );

      // run validation
      Assert.assertTrue( runValidationAndImportAttr( true, true ) == 1 );

   }


   /**
    * This test is to verify the fix OPER-5700: C_STOCK_LEVEL_ATTR and C_STOCK_LEVEL_ALLOC;
    *
    */

   @Test
   public void test_OPER_5700_AttrIMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      test_OPER_5700_AttrVALIDATION();

      System.out.println( "Finish validation" );

      // run import
      Assert.assertTrue( runValidationAndImportAttr( false, true ) == 1 );

      // Verify the inv_loc_stock
      simpleIDs llocIds = GetLocIDs( iLocCD1 );
      simpleIDs lStockIds = GetStockIDs( iStockCD1 );
      simpleIDs lOwnerIds = OwnerIDs( iOwnerCD_LONG, iOwnerName_LONG );
      verifyINVLOCSTOCK( lStockIds, llocIds, lOwnerIds, "2", "1", iSTOCKLOWACTNCD, "1", "3", "2" );

   }


   /**
    * This test is to verify the fix 'STCKLVLATTR-10008: The Max Quantity is mandatory;
    *
    */
   @Test
   public void test_STCKLVLATTR_10008_VALLIDATION() {
      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_stock_level_alloc table
      Map<String, String> lstocklevel = new LinkedHashMap<>();

      lstocklevel.put( "STOCK_NO_CD", "\'" + iStockCD1 + "\'" );
      lstocklevel.put( "LOC_CD", "\'" + iLocCD1 + "\'" );
      lstocklevel.put( "OWNER_CD", "\'" + iOwnerCD_LONG + "\'" );
      lstocklevel.put( "WEIGHT_FACTOR_QT", "\'2\'" );
      lstocklevel.put( "ALLOC_PCT", "\'1\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STOCK_LEVEL_ALLOC, lstocklevel ) );

      // c_stock_level_attr table
      lstocklevel.clear();
      lstocklevel.put( "STOCK_NO_CD", "\'" + iStockCD1 + "\'" );
      lstocklevel.put( "LOC_CD", "\'" + iLocCD1 + "\'" );
      lstocklevel.put( "OWNER_CD", "\'" + iOwnerCD_LONG + "\'" );
      lstocklevel.put( "MIN_REORDER_QT", "\'1\'" );
      lstocklevel.put( "REORDER_LEVEL", "\'2\'" );
      // lstocklevel.put( "MAX_QT", "\'3\'" );
      lstocklevel.put( "STOCK_LOW_ACTN_CD", "\'" + iSTOCKLOWACTNCD + "\'" );
      lstocklevel.put( "REORDER_QT", "\'2\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_STOCK_LEVEL_ATTR, lstocklevel ) );

      // run import
      Assert.assertTrue( runValidationAndImportLoc( false, true ) == 1 );

      // run validation
      Assert.assertTrue( runValidationAndImportAttr( true, true ) != 1 );

      checkStockLevelErrorCode( "test_OPER_16014_VALIDATION", "STCKLVLATTR-10008" );

   }


   // ==================================================================================================================
   /**
    * Calls check sensitivity error code
    *
    *
    */
   protected void checkStockLevelErrorCode( String aTestName, String aValidationCode ) {

      boolean lFound = false;
      String lerror_desc = null;
      String lerrorMsg = null;

      String lquery = TestConstants.BL_STOCKLEVEL_ERROR_CHECK;

      String[] iIds = { "RESULT_CD", "TECH_DESC" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      List<ArrayList<String>> lresult = execute( lquery, lfields );

      for ( int i = 0; i < lresult.size(); i++ ) {
         if ( lresult.get( i ).get( 0 ).equalsIgnoreCase( aValidationCode ) ) {
            lerror_desc = lresult.get( i ).get( 1 );
            lFound = true;
         } else {
            lerrorMsg = lerrorMsg + lresult.get( i ).get( 0 ) + ": " + lresult.get( i ).get( 1 );
         }

      }

      Assert.assertTrue( "The error code not found- " + aValidationCode + " : " + lerror_desc
            + " other error found [" + lerrorMsg + "]", lFound );

   }


   /**
    * This function is to retrieve LOC IDs for INV_LOC table
    *
    *
    */
   public simpleIDs GetLocIDs( String aLocCD ) {

      // EQP_STOCK_NO table
      String[] iIds = { "LOC_DB_ID", "LOC_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "LOC_CD", aLocCD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.INV_LOC, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lIds;

   }


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
    * This function is to retrieve Owner IDs for INV_OWNER table
    *
    *
    */
   public simpleIDs OwnerIDs( String aOwnerCD, String aOwnerName ) {

      // EQP_STOCK_NO table
      String[] iIds = { "OWNER_DB_ID", "OWNER_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "OWNER_CD", aOwnerCD );
      lArgs.addArguments( "OWNER_NAME", aOwnerName );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.INV_OWNER, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lIds;

   }


   /**
    * This function is called by after test method to restore data which has been modified by tests.
    *
    *
    */

   public void RestoreData() {

      // Delete INV_LOC_STOCK table
      String lStrDelete = "delete from " + TableUtil.INV_LOC_STOCK;
      executeSQL( lStrDelete );
      // Delete EQP_STOCK_NO table
      lStrDelete = "delete from " + TableUtil.EQP_STOCK_NO + " where STOCK_NO_CD like '%AUTO%'";
      executeSQL( lStrDelete );

   }


   /**
    * This function is to implement interface ValidationAndImportHeader
    *
    * @param: blnOnlyValidation
    * @param: allornone
    *
    */
   public int runValidationAndImportLoc( boolean blnOnlyValidation, boolean allornone ) {
      int rtValue = 0;
      ivalidationandimportLoc = new ValidationAndImport() {

         @Override
         public int runValidation( boolean allornone ) {
            int lReturn = 0;

            try {
               CallableStatement lPrepareCallInventory = getConnection().prepareCall(
                     "BEGIN  c_stock_lvl_alloc_import.validate_allocation(on_retcode => ?); END;" );

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
                     "BEGIN c_stock_lvl_alloc_import.import_allocation(on_retcode => ?); END;" );

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

      rtValue = blnOnlyValidation ? ivalidationandimportLoc.runValidation( allornone )
            : ivalidationandimportLoc.runImport( allornone );

      return rtValue;
   }


   /**
    * This function is to implement interface ValidationAndImportDetails
    *
    * @param: blnOnlyValidation
    * @param: allornone
    *
    */
   public int runValidationAndImportAttr( boolean blnOnlyValidation, boolean allornone ) {
      int rtValue = 0;
      ivalidationandimportAttr = new ValidationAndImport() {

         @Override
         public int runValidation( boolean allornone ) {
            int lReturn = 0;

            try {
               CallableStatement lPrepareCallInventory = getConnection().prepareCall(
                     "BEGIN c_stock_lvl_attr_import.validate_attribute(on_retcode => ?); END;" );

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
                     "BEGIN c_stock_lvl_attr_import.import_attribute(on_retcode => ?); END;" );

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

      rtValue = blnOnlyValidation ? ivalidationandimportAttr.runValidation( allornone )
            : ivalidationandimportAttr.runImport( allornone );

      return rtValue;
   }

}
