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
import com.mxi.mx.util.ValidationAndImport;
import com.mxi.mx.util.WhereClause;


/**
 * This test suite contains test cases on validation and import functionality of DEPT_IMPORT
 * package.
 *
 * @author ALICIA QIAN
 */
public class Departments extends BaselineLoaderTest {

   @Rule
   public TestName testName = new TestName();

   ValidationAndImport ivalidationandimport;

   public String iDeptCD1 = "AUTOTEST1";
   public String iDeptCD2 = "AUTOTEST2";
   public String iDeptTypeCd = "ENG";
   public String iDeptSdesc = "Engineering";
   public String iDeptLdesc = "Engineering support to Maintenance";
   public String iAirPort1 = "AIRPORT1";
   public String iAirPort2 = "AIRPORT2";

   String iAirport1CD_24321 = null;
   String iAirport2CD_24321 = null;

   {
      iAirport1CD_24321 = get2000Length( "AUTOAPT1" );

   }


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
    * This test is to verify DEPT_IMPORT validation functionality of staging table c_dept with 1
    * airport
    *
    */
   @Test
   public void testDEPTWITH1AIRPORT_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_DEPT table
      Map<String, String> ldept = new LinkedHashMap<>();

      //
      ldept.put( "DEPT_CD", "\'" + iDeptCD1 + "\'" );
      ldept.put( "DEPT_TYPE_CD", "\'" + iDeptTypeCd + "\'" );
      ldept.put( "DEPT_SDESC", "\'" + iDeptSdesc + "\'" );
      ldept.put( "DEPT_LDESC", "\'" + iDeptLdesc + "\'" );
      ldept.put( "AIRPORT_CD_LIST", "\'" + iAirPort1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_DEPT, ldept ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify DEPT_IMPORT import functionality of staging table c_dept with 1 airport
    *
    */
   @Test
   public void testDEPTWITH1AIRPORT_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testDEPTWITH1AIRPORT_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify ORG_WORK_DEPT
      simpleIDs lDepIds =
            VerifySingleDeptORGWORKDEPT( iDeptCD1, iDeptTypeCd, iDeptSdesc, iDeptLdesc );

      VerifyINVLOCDEPT( lDepIds, iAirPort1 );
   }


   /**
    * This test is to verify DEPT_IMPORT validation functionality of staging table c_dept with 2
    * airports
    *
    */
   @Test
   public void testDEPTWITH2AIRPORTS_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_DEPT table
      Map<String, String> ldept = new LinkedHashMap<>();

      //
      ldept.put( "DEPT_CD", "\'" + iDeptCD1 + "\'" );
      ldept.put( "DEPT_TYPE_CD", "\'" + iDeptTypeCd + "\'" );
      ldept.put( "DEPT_SDESC", "\'" + iDeptSdesc + "\'" );
      ldept.put( "DEPT_LDESC", "\'" + iDeptLdesc + "\'" );
      ldept.put( "AIRPORT_CD_LIST", "\'" + iAirPort1 + "," + iAirPort2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_DEPT, ldept ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify DEPT_IMPORT import functionality of staging table c_dept with 2
    * airports
    *
    */
   @Test
   public void testDEPTWITH2AIRPORTs_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testDEPTWITH2AIRPORTS_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify ORG_WORK_DEPT
      simpleIDs lDepIds =
            VerifySingleDeptORGWORKDEPT( iDeptCD1, iDeptTypeCd, iDeptSdesc, iDeptLdesc );

      VerifyINVLOCDEPT( lDepIds, iAirPort1 );
      VerifyINVLOCDEPT( lDepIds, iAirPort2 );
   }


   /**
    * This test is to verify DEPT_IMPORT validation functionality of staging tables c_dept and
    * C_AIRPORT_DEPT with 1 airport
    *
    */
   @Test
   public void testAIRPORTDEPTWITH1AIRPORT_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_DEPT table
      Map<String, String> ldept = new LinkedHashMap<>();

      ldept.put( "DEPT_CD", "\'" + iDeptCD1 + "\'" );
      ldept.put( "DEPT_TYPE_CD", "\'" + iDeptTypeCd + "\'" );
      ldept.put( "DEPT_SDESC", "\'" + iDeptSdesc + "\'" );
      ldept.put( "DEPT_LDESC", "\'" + iDeptLdesc + "\'" );
      ldept.put( "AIRPORT_CD_LIST", null );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_DEPT, ldept ) );

      // C_AIRPORT_DEPT
      ldept.clear();
      ldept.put( "AIRPORT_CD", "\'" + iAirPort1 + "\'" );
      ldept.put( "DEPT_CD", "\'" + iDeptCD1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_AIRPORT_DEPT, ldept ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify DEPT_IMPORT validation functionality of staging tables c_dept and
    * C_AIRPORT_DEPT with 1 airport
    *
    */
   @Test
   public void testAIRPORTDEPTWITH1AIRPORT_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testAIRPORTDEPTWITH1AIRPORT_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify ORG_WORK_DEPT
      simpleIDs lDepIds =
            VerifySingleDeptORGWORKDEPT( iDeptCD1, iDeptTypeCd, iDeptSdesc, iDeptLdesc );

      VerifyINVLOCDEPT( lDepIds, iAirPort1 );
   }


   /**
    * This test is to verify DEPT_IMPORT validation functionality of staging tables c_dept and
    * C_AIRPORT_DEPT with 2 airports
    *
    */
   @Test
   public void testAIRPORTDEPTWITH2AIRPORTS_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_DEPT table
      Map<String, String> ldept = new LinkedHashMap<>();

      ldept.put( "DEPT_CD", "\'" + iDeptCD1 + "\'" );
      ldept.put( "DEPT_TYPE_CD", "\'" + iDeptTypeCd + "\'" );
      ldept.put( "DEPT_SDESC", "\'" + iDeptSdesc + "\'" );
      ldept.put( "DEPT_LDESC", "\'" + iDeptLdesc + "\'" );
      ldept.put( "AIRPORT_CD_LIST", null );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_DEPT, ldept ) );

      // C_AIRPORT_DEPT
      ldept.clear();
      ldept.put( "AIRPORT_CD", "\'" + iAirPort1 + "\'" );
      ldept.put( "DEPT_CD", "\'" + iDeptCD1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_AIRPORT_DEPT, ldept ) );

      // C_AIRPORT_DEPT record2
      ldept.clear();
      ldept.put( "AIRPORT_CD", "\'" + iAirPort2 + "\'" );
      ldept.put( "DEPT_CD", "\'" + iDeptCD1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_AIRPORT_DEPT, ldept ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify DEPT_IMPORT validation import of staging tables c_dept and
    * C_AIRPORT_DEPT with 2 airports
    *
    */
   @Test
   public void testAIRPORTDEPTWITH2AIRPORTS_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testAIRPORTDEPTWITH2AIRPORTS_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify ORG_WORK_DEPT
      simpleIDs lDepIds =
            VerifySingleDeptORGWORKDEPT( iDeptCD1, iDeptTypeCd, iDeptSdesc, iDeptLdesc );

      VerifyINVLOCDEPT( lDepIds, iAirPort1 );
      VerifyINVLOCDEPT( lDepIds, iAirPort2 );
   }


   /**
    * This test is to verify DEPT_IMPORT validation functionality of staging table c_dept with 2
    * Depts of different airport
    *
    */
   @Test
   public void test2DEPTWITHDIFFERENTAIRPORT_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_DEPT table
      Map<String, String> ldept = new LinkedHashMap<>();

      ldept.put( "DEPT_CD", "\'" + iDeptCD1 + "\'" );
      ldept.put( "DEPT_TYPE_CD", "\'" + iDeptTypeCd + "\'" );
      ldept.put( "DEPT_SDESC", "\'" + iDeptSdesc + "\'" );
      ldept.put( "DEPT_LDESC", "\'" + iDeptLdesc + "\'" );
      ldept.put( "AIRPORT_CD_LIST", "\'" + iAirPort1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_DEPT, ldept ) );

      // C_DEPT table
      ldept.clear();
      ldept.put( "DEPT_CD", "\'" + iDeptCD2 + "\'" );
      ldept.put( "DEPT_TYPE_CD", "\'" + iDeptTypeCd + "\'" );
      ldept.put( "DEPT_SDESC", "\'" + iDeptSdesc + "\'" );
      ldept.put( "DEPT_LDESC", "\'" + iDeptLdesc + "\'" );
      ldept.put( "AIRPORT_CD_LIST", "\'" + iAirPort2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_DEPT, ldept ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify DEPT_IMPORT import functionality of staging table c_dept with 2 Depts
    * of different airport
    *
    */
   @Test
   public void test2DEPTWITHDIFFERENTAIRPORT_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      test2DEPTWITHDIFFERENTAIRPORT_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify ORG_WORK_DEPT
      simpleIDs lDepIds1 =
            VerifySingleDeptORGWORKDEPT( iDeptCD1, iDeptTypeCd, iDeptSdesc, iDeptLdesc );

      simpleIDs lDepIds2 =
            VerifySingleDeptORGWORKDEPT( iDeptCD2, iDeptTypeCd, iDeptSdesc, iDeptLdesc );
      VerifyINVLOCDEPT( lDepIds1, iAirPort1 );
      VerifyINVLOCDEPT( lDepIds2, iAirPort2 );
   }


   /**
    * This test is to verify DEPT_IMPORT validation functionality of staging table c_dept with 2
    * Depts of overlapping airports
    *
    */
   @Test
   public void test2DEPTWITHOVERAPPINGAIRPORT_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_DEPT table
      Map<String, String> ldept = new LinkedHashMap<>();

      ldept.put( "DEPT_CD", "\'" + iDeptCD1 + "\'" );
      ldept.put( "DEPT_TYPE_CD", "\'" + iDeptTypeCd + "\'" );
      ldept.put( "DEPT_SDESC", "\'" + iDeptSdesc + "\'" );
      ldept.put( "DEPT_LDESC", "\'" + iDeptLdesc + "\'" );
      ldept.put( "AIRPORT_CD_LIST", "\'" + iAirPort1 + "," + iAirPort2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_DEPT, ldept ) );

      // C_DEPT table
      ldept.clear();
      ldept.put( "DEPT_CD", "\'" + iDeptCD2 + "\'" );
      ldept.put( "DEPT_TYPE_CD", "\'" + iDeptTypeCd + "\'" );
      ldept.put( "DEPT_SDESC", "\'" + iDeptSdesc + "\'" );
      ldept.put( "DEPT_LDESC", "\'" + iDeptLdesc + "\'" );
      ldept.put( "AIRPORT_CD_LIST", "\'" + iAirPort2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_DEPT, ldept ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify DEPT_IMPORT import functionality of staging table c_dept with 2 Depts
    * of overlapping airports
    *
    */
   @Test
   public void test2DEPTWITHOVERAPPINGAIRPORT_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      test2DEPTWITHOVERAPPINGAIRPORT_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify ORG_WORK_DEPT
      simpleIDs lDepIds1 =
            VerifySingleDeptORGWORKDEPT( iDeptCD1, iDeptTypeCd, iDeptSdesc, iDeptLdesc );

      simpleIDs lDepIds2 =
            VerifySingleDeptORGWORKDEPT( iDeptCD2, iDeptTypeCd, iDeptSdesc, iDeptLdesc );

      VerifyINVLOCDEPT( lDepIds1, iAirPort1, iAirPort2 );
      VerifyINVLOCDEPT( lDepIds2, iAirPort2 );
   }


   /**
    * This test is to verify DEPT_IMPORT validation functionality of staging tables c_dept and
    * C_AIRPORT_DEPT with 2 Depts
    *
    */
   @Test
   public void testAIRPORTDEPTWITH2DEPT_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_DEPT table
      Map<String, String> ldept = new LinkedHashMap<>();

      ldept.put( "DEPT_CD", "\'" + iDeptCD1 + "\'" );
      ldept.put( "DEPT_TYPE_CD", "\'" + iDeptTypeCd + "\'" );
      ldept.put( "DEPT_SDESC", "\'" + iDeptSdesc + "\'" );
      ldept.put( "DEPT_LDESC", "\'" + iDeptLdesc + "\'" );
      ldept.put( "AIRPORT_CD_LIST", null );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_DEPT, ldept ) );

      // C_DEPT table
      ldept.clear();
      ldept.put( "DEPT_CD", "\'" + iDeptCD2 + "\'" );
      ldept.put( "DEPT_TYPE_CD", "\'" + iDeptTypeCd + "\'" );
      ldept.put( "DEPT_SDESC", "\'" + iDeptSdesc + "\'" );
      ldept.put( "DEPT_LDESC", "\'" + iDeptLdesc + "\'" );
      ldept.put( "AIRPORT_CD_LIST", null );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_DEPT, ldept ) );

      // C_AIRPORT_DEPT
      ldept.clear();
      ldept.put( "AIRPORT_CD", "\'" + iAirPort1 + "\'" );
      ldept.put( "DEPT_CD", "\'" + iDeptCD1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_AIRPORT_DEPT, ldept ) );

      // C_AIRPORT_DEPT
      ldept.clear();
      ldept.put( "AIRPORT_CD", "\'" + iAirPort2 + "\'" );
      ldept.put( "DEPT_CD", "\'" + iDeptCD2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_AIRPORT_DEPT, ldept ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );
   }


   /**
    * This test is to verify DEPT_IMPORT import functionality of staging tables c_dept and
    * C_AIRPORT_DEPT with 2 Depts
    *
    */
   @Test
   public void testAIRPORTDEPTWITH2DEPT_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testAIRPORTDEPTWITH2DEPT_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify ORG_WORK_DEPT
      simpleIDs lDepIds1 =
            VerifySingleDeptORGWORKDEPT( iDeptCD1, iDeptTypeCd, iDeptSdesc, iDeptLdesc );

      simpleIDs lDepIds2 =
            VerifySingleDeptORGWORKDEPT( iDeptCD2, iDeptTypeCd, iDeptSdesc, iDeptLdesc );

      VerifyINVLOCDEPT( lDepIds1, iAirPort1 );
      VerifyINVLOCDEPT( lDepIds2, iAirPort2 );
   }


   /**
    * This test is to verify DEPT_IMPORT validation functionality of staging table c_dept with 1
    * airport. Since there is no data setup for 2000 length airport code, test uses error code to
    * validate the airport cd can accept 2000 length.
    *
    */
   @Test
   public void testOPER24321_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // C_DEPT table
      Map<String, String> ldept = new LinkedHashMap<>();

      ldept.put( "DEPT_CD", "\'" + iDeptCD1 + "\'" );
      ldept.put( "DEPT_TYPE_CD", "\'" + iDeptTypeCd + "\'" );
      ldept.put( "DEPT_SDESC", "\'" + iDeptSdesc + "\'" );
      ldept.put( "DEPT_LDESC", "\'" + iDeptLdesc + "\'" );
      ldept.put( "AIRPORT_CD_LIST", null );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_DEPT, ldept ) );

      // C_AIRPORT_DEPT
      ldept.clear();
      ldept.put( "AIRPORT_CD", "\'" + iAirport1CD_24321 + "\'" );
      ldept.put( "DEPT_CD", "\'" + iDeptCD1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_AIRPORT_DEPT, ldept ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );

      // Validate CFGREQ-12154 error
      validateErrorCode( "CFGDPT-17002" );

   }


   // =============================================================================

   /**
    * This function is to validate error code exists.
    *
    *
    */
   public void validateErrorCode( String aCode ) {

      List<String> llist = getErrorCodeList();
      String lerror_desc = getErrorDetail( aCode );
      Assert.assertTrue( "Error code found " + aCode + ": " + lerror_desc,
            llist.contains( aCode ) );

   }


   /**
    * This function is to retrieve errors code list
    *
    *
    */

   public List<String> getErrorCodeList() {
      String[] iIds = { "RESULT_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      String iQueryString = "select c_dept.result_cd " + " from c_dept "
            + " inner join DL_REF_MESSAGE on " + " DL_REF_MESSAGE.result_cd=c_dept.result_cd "
            + " UNION ALL " + " select c_airport_dept.result_cd "
            + " from c_airport_dept inner join DL_REF_MESSAGE on "
            + " DL_REF_MESSAGE.result_cd=c_airport_dept.result_cd ";
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      List<String> llistM = new ArrayList<String>();
      for ( int i = 0; i < llists.size(); i++ ) {
         llistM.add( llists.get( i ).get( 0 ) );
      }

      return llistM;

   }


   /**
    * This function is to get detail of error code
    *
    *
    */

   public String getErrorDetail( String aErrorcode ) {

      String[] iIds = { "TECH_DESC" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();

      lArgs.addArguments( "RESULT_CD", aErrorcode );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.DL_REF_MESSAGE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      return llists.get( 0 ).get( 0 );

   }


   /**
    * This function is to verify inv_lod table
    *
    *
    */

   public void VerifyINVLOCDEPT( simpleIDs aDepIds, String... aAirPorts ) {

      for ( String lairport : aAirPorts ) {
         simpleIDs llocIds = GetLocIDs( lairport );
         String iQueryString = "select 1 from " + TableUtil.INV_LOC_DEPT + " where DEPT_DB_ID="
               + aDepIds.getNO_DB_ID() + " and DEPT_ID=" + aDepIds.getNO_ID() + " and LOC_DB_ID="
               + llocIds.getNO_DB_ID() + " and LOC_ID=" + llocIds.getNO_ID();
         Assert.assertTrue( "LOC of airport: " + lairport + " exist",
               RecordsExist( iQueryString ) );

      }

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
    * This function is to retrive Dept Ids and verify org_work_dept table
    *
    *
    */

   public simpleIDs VerifySingleDeptORGWORKDEPT( String aDeptCD, String aDeptTypeCd,
         String aDescSdesc, String aDescLdesc ) {

      // INV_LOC table
      String[] iIds = { "DEPT_DB_ID", "DEPT_ID", "DEPT_TYPE_CD", "DESC_SDESC", "DESC_LDESC" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "DEPT_CD", aDeptCD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.ORG_WORK_DEPT, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );
      Assert.assertTrue( "DEPT_TYPE_CD", llists.get( 0 ).get( 2 ).equalsIgnoreCase( aDeptTypeCd ) );
      Assert.assertTrue( "DESC_SDESC", llists.get( 0 ).get( 3 ).equalsIgnoreCase( aDescSdesc ) );
      Assert.assertTrue( "DESC_LDESC", llists.get( 0 ).get( 4 ).equalsIgnoreCase( aDescLdesc ) );

      return lIds;

   }


   /**
    * This function is called by after test method to restore data which has been modified by tests.
    *
    *
    */

   public void RestoreData() {

      // Get DEPT IDs for deleting
      String iQueryString = "Select DEPT_DB_ID, DEPT_ID from " + TableUtil.ORG_WORK_DEPT
            + " where DEPT_CD like '%AUTO%'";
      String[] iIds = { "DEPT_DB_ID", "DEPT_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get All IDs which need to be deleted from INV_LOC_ORG
      ArrayList<simpleIDs> lIds = new ArrayList<>();

      for ( int i = 0; i < llists.size(); i++ ) {
         simpleIDs lid = new simpleIDs( llists.get( i ).get( 0 ), llists.get( i ).get( 1 ) );
         lIds.add( lid );

      }

      // Delete INV_LOC_DEPT table
      for ( int i = 0; i < lIds.size(); i++ ) {

         String lStrDelete = "delete from " + TableUtil.INV_LOC_DEPT + " where DEPT_DB_ID="
               + lIds.get( i ).getNO_DB_ID() + " and DEPT_ID=" + lIds.get( i ).getNO_ID();
         executeSQL( lStrDelete );
      }

      // Delete ORG_WORK_DEPT
      String lStrDelete = "delete from " + TableUtil.ORG_WORK_DEPT + " where DEPT_CD like '%AUTO%'";
      executeSQL( lStrDelete );

   }


   /**
    * This function is to get 2000 length string. .
    *
    *
    */
   public String get2000Length( String aHead ) {

      String ls = aHead;
      int length = aHead.length();
      for ( int i = length; i < 2000; i++ ) {
         ls = ls + "A";

      }

      return ls;

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
                     .prepareCall( "BEGIN dept_import.validate_dept(on_retcode => ?); END;" );

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
                     .prepareCall( "BEGIN dept_import.import_dept(on_retcode => ?); END;" );

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
