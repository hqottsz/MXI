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

import com.mxi.mx.util.BaselineLoaderTest;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.ValidationAndImport;
import com.mxi.mx.util.WhereClause;


/**
 * This test suite contains test cases on validation and import functionality of PO_FLOW_IMPORT
 * package.
 *
 * @author ALICIA QIAN
 */
public class POAuthFlow extends BaselineLoaderTest {

   @Rule
   public TestName testName = new TestName();

   ValidationAndImport ivalidationandimport;

   public String iFlowCD1 = "AUTO1";
   public String iFlowCD2 = "AUTO2";
   // public String iFlowCDSys = "BLKOUT";
   public String iFlowCDSys = "REPAIR";
   public String iPOTypeCD1 = "EXCHANGE";
   public String iPOTypeCD2 = "REPAIR";
   public String iFlowSdesc = "ATSDESC";
   public String iFlowLdesc = "ATLDESC";
   public String iUserLvlCD1 = "POMGER";
   public String iUserLvlCD2 = "CEO";
   public String iUserLvlCD3 = "AUTOU1";
   public String iUserLvlCD4 = "AUTOU2";
   public String iLvlSdesc = "ATLSDESC";
   public String iLvlLdesc = "ATLLDESC";


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
    * This test is to verify PO_FLOW_IMPORT validation functionality of staging table c_ref_flow and
    * c_ref_flow_level staging tables. Create new ref flow and create new flow level for existing
    * flow separately.
    *
    */
   @Test
   public void testFowLevelSeperately_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_ref_flow table
      Map<String, String> lPOFlow = new LinkedHashMap<>();

      lPOFlow.put( "FLOW_CD", "\'" + iFlowCD1 + "\'" );
      lPOFlow.put( "PO_TYPE_CD", "\'" + iPOTypeCD1 + "\'" );
      lPOFlow.put( "FLOW_SDESC", "\'" + iFlowSdesc + "\'" );
      lPOFlow.put( "FLOW_LDESC", "\'" + iFlowLdesc + "\'" );
      lPOFlow.put( "DEFAULT_BOOL", "\'Y\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_FLOW, lPOFlow ) );

      // c_ref_flow_level
      lPOFlow.clear();
      lPOFlow.put( "FLOW_CD", "\'" + iFlowCDSys + "\'" );
      lPOFlow.put( "USER_LEVEL_CD", "\'" + iUserLvlCD3 + "\'" );
      lPOFlow.put( "LEVEL_SDESC", "\'" + iLvlSdesc + "\'" );
      lPOFlow.put( "LEVEL_LDESC", "\'" + iLvlLdesc + "\'" );
      lPOFlow.put( "LIMIT_PRICE", "\'5\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_FLOW_LEVEL, lPOFlow ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify PO_FLOW_IMPORT import functionality of staging table c_ref_flow and
    * c_ref_flow_level staging tables Create new ref flow and create new flow level for existing
    * flow separately.
    *
    */
   @Test
   public void testFowLevelSeperately_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testFowLevelSeperately_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify REF_PO_AUTH_FLOW
      VerifyREFPOAUTHFLOW( iFlowCD1, iPOTypeCD1, iFlowSdesc, iFlowLdesc, "1" );
      // Verify REF_PO_AUTH_LVL
      VerifyREFPOAUTHLVL( iFlowCDSys + "01", iFlowCDSys, iUserLvlCD3, iLvlSdesc, iLvlLdesc, "5" );
   }


   /**
    * This test is to verify PO_FLOW_IMPORT validation functionality of staging table c_ref_flow and
    * c_ref_flow_level staging tables. Create new ref flow and create new flow level for it.
    *
    */
   @Test
   public void testFowLevel_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_ref_flow table
      Map<String, String> lPOFlow = new LinkedHashMap<>();

      lPOFlow.put( "FLOW_CD", "\'" + iFlowCD1 + "\'" );
      lPOFlow.put( "PO_TYPE_CD", "\'" + iPOTypeCD1 + "\'" );
      lPOFlow.put( "FLOW_SDESC", "\'" + iFlowSdesc + "\'" );
      lPOFlow.put( "FLOW_LDESC", "\'" + iFlowLdesc + "\'" );
      lPOFlow.put( "DEFAULT_BOOL", "\'Y\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_FLOW, lPOFlow ) );

      // c_ref_flow_level
      lPOFlow.clear();
      lPOFlow.put( "FLOW_CD", "\'" + iFlowCD1 + "\'" );
      lPOFlow.put( "USER_LEVEL_CD", "\'" + iUserLvlCD1 + "\'" );
      lPOFlow.put( "LEVEL_SDESC", "\'" + iLvlSdesc + "\'" );
      lPOFlow.put( "LEVEL_LDESC", "\'" + iLvlLdesc + "\'" );
      lPOFlow.put( "LIMIT_PRICE", "\'5\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_FLOW_LEVEL, lPOFlow ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify PO_FLOW_IMPORT import functionality of staging table c_ref_flow and
    * c_ref_flow_level staging tables. Create new ref flow and create new flow level for it.
    *
    */
   @Test
   public void testFowLevel_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testFowLevel_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify REF_PO_AUTH_FLOW
      VerifyREFPOAUTHFLOW( iFlowCD1, iPOTypeCD1, iFlowSdesc, iFlowLdesc, "1" );
      // Verify REF_PO_AUTH_LVL
      VerifyREFPOAUTHLVL( iFlowCD1 + "01", iFlowCD1, iUserLvlCD1, iLvlSdesc, iLvlLdesc, "5" );
   }


   /**
    * This test is to verify PO_FLOW_IMPORT validation functionality of staging table c_ref_flow and
    * c_ref_flow_level staging tables.Create multiple new ref flow with same PO_TYPE_CD and create
    * new flow level for existing flow separately.
    *
    */
   @Test
   public void testFowLevelMultipleRecordWithSamePOType_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_ref_flow table
      Map<String, String> lPOFlow = new LinkedHashMap<>();

      lPOFlow.put( "FLOW_CD", "\'" + iFlowCD1 + "\'" );
      lPOFlow.put( "PO_TYPE_CD", "\'" + iPOTypeCD1 + "\'" );
      lPOFlow.put( "FLOW_SDESC", "\'" + iFlowSdesc + "\'" );
      lPOFlow.put( "FLOW_LDESC", "\'" + iFlowLdesc + "\'" );
      lPOFlow.put( "DEFAULT_BOOL", "\'Y\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_FLOW, lPOFlow ) );

      // Second record
      lPOFlow.clear();
      lPOFlow.put( "FLOW_CD", "\'" + iFlowCD2 + "\'" );
      lPOFlow.put( "PO_TYPE_CD", "\'" + iPOTypeCD1 + "\'" );
      lPOFlow.put( "FLOW_SDESC", "\'" + iFlowSdesc + "\'" );
      lPOFlow.put( "FLOW_LDESC", "\'" + iFlowLdesc + "\'" );
      lPOFlow.put( "DEFAULT_BOOL", "\'N\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_FLOW, lPOFlow ) );

      // c_ref_flow_level
      lPOFlow.clear();
      lPOFlow.put( "FLOW_CD", "\'" + iFlowCDSys + "\'" );
      lPOFlow.put( "USER_LEVEL_CD", "\'" + iUserLvlCD3 + "\'" );
      lPOFlow.put( "LEVEL_SDESC", "\'" + iLvlSdesc + "\'" );
      lPOFlow.put( "LEVEL_LDESC", "\'" + iLvlLdesc + "\'" );
      lPOFlow.put( "LIMIT_PRICE", "\'5\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_FLOW_LEVEL, lPOFlow ) );

      // second record
      lPOFlow.clear();
      lPOFlow.put( "FLOW_CD", "\'" + iFlowCDSys + "\'" );
      lPOFlow.put( "USER_LEVEL_CD", "\'" + iUserLvlCD4 + "\'" );
      lPOFlow.put( "LEVEL_SDESC", "\'" + iLvlSdesc + "\'" );
      lPOFlow.put( "LEVEL_LDESC", "\'" + iLvlLdesc + "\'" );
      lPOFlow.put( "LIMIT_PRICE", "\'5\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_FLOW_LEVEL, lPOFlow ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify PO_FLOW_IMPORT import functionality of staging table c_ref_flow and
    * c_ref_flow_level staging tables. Create multiple new ref flow with same PO_TYPE_CD and create
    * new flow level for existing flow separately.
    */
   @Test
   public void testFowLevelMultipleRecordWithSamePOType_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testFowLevelMultipleRecordWithSamePOType_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify REF_PO_AUTH_FLOW
      VerifyREFPOAUTHFLOW( iFlowCD1, iPOTypeCD1, iFlowSdesc, iFlowLdesc, "1" );
      VerifyREFPOAUTHFLOW( iFlowCD2, iPOTypeCD1, iFlowSdesc, iFlowLdesc, "0" );
      // Verify REF_PO_AUTH_LVL
      VerifyREFPOAUTHLVL( iFlowCDSys + "01", iFlowCDSys, iUserLvlCD3, iUserLvlCD4, iLvlSdesc,
            iLvlLdesc, "5" );
      VerifyREFPOAUTHLVL( iFlowCDSys + "02", iFlowCDSys, iUserLvlCD4, iUserLvlCD3, iLvlSdesc,
            iLvlLdesc, "5" );
   }


   /**
    * This test is to verify PO_FLOW_IMPORT validation functionality of staging table c_ref_flow and
    * c_ref_flow_level staging tables.Create multiple new ref flow with different PO_TYPE_CD and
    * create new flow level for existing flow separately.
    *
    */
   @Test
   public void testFowLevelMultipleRecord_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_ref_flow table
      Map<String, String> lPOFlow = new LinkedHashMap<>();

      lPOFlow.put( "FLOW_CD", "\'" + iFlowCD1 + "\'" );
      lPOFlow.put( "PO_TYPE_CD", "\'" + iPOTypeCD1 + "\'" );
      lPOFlow.put( "FLOW_SDESC", "\'" + iFlowSdesc + "\'" );
      lPOFlow.put( "FLOW_LDESC", "\'" + iFlowLdesc + "\'" );
      lPOFlow.put( "DEFAULT_BOOL", "\'Y\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_FLOW, lPOFlow ) );

      // Second record
      lPOFlow.clear();
      lPOFlow.put( "FLOW_CD", "\'" + iFlowCD2 + "\'" );
      lPOFlow.put( "PO_TYPE_CD", "\'" + iPOTypeCD2 + "\'" );
      lPOFlow.put( "FLOW_SDESC", "\'" + iFlowSdesc + "\'" );
      lPOFlow.put( "FLOW_LDESC", "\'" + iFlowLdesc + "\'" );
      lPOFlow.put( "DEFAULT_BOOL", "\'Y\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_FLOW, lPOFlow ) );

      // c_ref_flow_level
      lPOFlow.clear();
      lPOFlow.put( "FLOW_CD", "\'" + iFlowCDSys + "\'" );
      lPOFlow.put( "USER_LEVEL_CD", "\'" + iUserLvlCD3 + "\'" );
      lPOFlow.put( "LEVEL_SDESC", "\'" + iLvlSdesc + "\'" );
      lPOFlow.put( "LEVEL_LDESC", "\'" + iLvlLdesc + "\'" );
      lPOFlow.put( "LIMIT_PRICE", "\'5\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_FLOW_LEVEL, lPOFlow ) );

      // second record
      lPOFlow.clear();
      lPOFlow.put( "FLOW_CD", "\'" + iFlowCDSys + "\'" );
      lPOFlow.put( "USER_LEVEL_CD", "\'" + iUserLvlCD4 + "\'" );
      lPOFlow.put( "LEVEL_SDESC", "\'" + iLvlSdesc + "\'" );
      lPOFlow.put( "LEVEL_LDESC", "\'" + iLvlLdesc + "\'" );
      lPOFlow.put( "LIMIT_PRICE", "\'5\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_REF_FLOW_LEVEL, lPOFlow ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify PO_FLOW_IMPORT import functionality of staging table c_ref_flow and
    * c_ref_flow_level staging tables. Create multiple new ref flow with different PO_TYPE_CD and
    * create new flow level for existing flow separately.
    */
   @Test
   public void testFowLevelMultipleRecord_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testFowLevelMultipleRecord_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify REF_PO_AUTH_FLOW
      VerifyREFPOAUTHFLOW( iFlowCD1, iPOTypeCD1, iFlowSdesc, iFlowLdesc, "1" );
      VerifyREFPOAUTHFLOW( iFlowCD2, iPOTypeCD2, iFlowSdesc, iFlowLdesc, "1" );
      // Verify REF_PO_AUTH_LVL
      VerifyREFPOAUTHLVL( iFlowCDSys + "01", iFlowCDSys, iUserLvlCD3, iUserLvlCD4, iLvlSdesc,
            iLvlLdesc, "5" );
      VerifyREFPOAUTHLVL( iFlowCDSys + "02", iFlowCDSys, iUserLvlCD4, iUserLvlCD3, iLvlSdesc,
            iLvlLdesc, "5" );
   }


   // ==============================================================================================
   /**
    * This function is to verify REF_PO_AUTH_FLOW by given parameters .
    *
    *
    */
   public void VerifyREFPOAUTHFLOW( String aFlowCD, String aPOTypeCD, String aFlowSdesc,
         String aFlowLdesc, String aDefaultBool ) {

      // REF_PO_AUTH_FLOW
      String[] iIds = { "PO_TYPE_CD", "DESC_SDESC", "DESC_LDESC", "DEFAULT_BOOL" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "PO_AUTH_FLOW_CD", aFlowCD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.REF_PO_AUTH_FLOW, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "PO_TYPE_CD", llists.get( 0 ).get( 0 ).equalsIgnoreCase( aPOTypeCD ) );
      Assert.assertTrue( "DESC_SDESC", llists.get( 0 ).get( 1 ).equalsIgnoreCase( aFlowSdesc ) );
      Assert.assertTrue( "DESC_LDESC", llists.get( 0 ).get( 2 ).equalsIgnoreCase( aFlowLdesc ) );
      Assert.assertTrue( "DEFAULT_BOOL",
            llists.get( 0 ).get( 3 ).equalsIgnoreCase( aDefaultBool ) );

   }


   /**
    * This function is to verify REF_PO_AUTH_LEVEL by given parameters .
    *
    *
    */
   public void VerifyREFPOAUTHLVL( String aFlowLvlCD, String aFlowCD, String aUserLvlCD,
         String aLvlSdesc, String aLvlLdesc, String aLimitPrice ) {

      // REF_PO_AUTH_FLOW
      String[] iIds = { "PO_AUTH_FLOW_CD", "USER_CD", "DESC_SDESC", "DESC_LDESC", "LIMIT_PRICE" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "PO_AUTH_LVL_CD", aFlowLvlCD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.REF_PO_AUTH_LVL, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "PO_AUTH_FLOW_CD", llists.get( 0 ).get( 0 ).equalsIgnoreCase( aFlowCD ) );
      Assert.assertTrue( "USER_CD", llists.get( 0 ).get( 1 ).equalsIgnoreCase( aUserLvlCD ) );
      Assert.assertTrue( "DESC_SDESC", llists.get( 0 ).get( 2 ).equalsIgnoreCase( aLvlSdesc ) );
      Assert.assertTrue( "DESC_LDESC", llists.get( 0 ).get( 3 ).equalsIgnoreCase( aLvlLdesc ) );
      Assert.assertTrue( "LIMIT_PRICE", llists.get( 0 ).get( 4 ).equalsIgnoreCase( aLimitPrice ) );

   }


   /**
    * This function is to verify REF_PO_AUTH_LEVEL by given parameters .
    *
    *
    */
   public void VerifyREFPOAUTHLVL( String aFlowLvlCD, String aFlowCD, String aUserLvlCD1,
         String aUserLvlCD2, String aLvlSdesc, String aLvlLdesc, String aLimitPrice ) {

      // REF_PO_AUTH_FLOW
      String[] iIds = { "PO_AUTH_FLOW_CD", "USER_CD", "DESC_SDESC", "DESC_LDESC", "LIMIT_PRICE" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "PO_AUTH_LVL_CD", aFlowLvlCD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.REF_PO_AUTH_LVL, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "PO_AUTH_FLOW_CD", llists.get( 0 ).get( 0 ).equalsIgnoreCase( aFlowCD ) );
      Assert.assertTrue( "USER_CD", ( llists.get( 0 ).get( 1 ).equalsIgnoreCase( aUserLvlCD1 )
            || llists.get( 0 ).get( 1 ).equalsIgnoreCase( aUserLvlCD2 ) ) );
      Assert.assertTrue( "DESC_SDESC", llists.get( 0 ).get( 2 ).equalsIgnoreCase( aLvlSdesc ) );
      Assert.assertTrue( "DESC_LDESC", llists.get( 0 ).get( 3 ).equalsIgnoreCase( aLvlLdesc ) );
      Assert.assertTrue( "LIMIT_PRICE", llists.get( 0 ).get( 4 ).equalsIgnoreCase( aLimitPrice ) );

   }


   /**
    * This function is called by after test method to restore data which has been modified by tests.
    *
    *
    */

   public void RestoreData() {

      // Delete REF_PO_AUTH_LVL table
      String lStrDelete = "delete from " + TableUtil.REF_PO_AUTH_LVL + " where DESC_SDESC='"
            + iLvlSdesc + "' and DESC_LDESC='" + iLvlLdesc + "'";
      executeSQL( lStrDelete );

      // delete REF_PO_AUTH_FLOW table
      lStrDelete = "delete from " + TableUtil.REF_PO_AUTH_FLOW + " where DESC_SDESC='" + iFlowSdesc
            + "' and DESC_LDESC='" + iFlowLdesc + "'";
      executeSQL( lStrDelete );

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
               CallableStatement lPrepareCallInventory = getConnection()
                     .prepareCall( "BEGIN po_flow_import.validate_po_flow(on_retcode => ?); END;" );

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
               CallableStatement lPrepareCallInventory = getConnection()
                     .prepareCall( "BEGIN po_flow_import.import_po_flow(on_retcode => ?); END;" );

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
