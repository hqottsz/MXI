package com.mxi.mx.core.maint.plan.baselineloader;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
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
 * This test suite contains test cases on validation and import functionality of USRLIC_IMPORT
 * package.
 *
 * @author ALICIA QIAN
 */
public class UserLicense extends BaselineLoaderTest {

   @Rule
   public TestName testName = new TestName();

   ValidationAndImport ivalidationandimport;

   public String iUserName1 = "mxi";
   public String iUserName2 = "user1";
   public String iLicCD1 = "AUTOCD1";
   public String iLicCD2 = "AUTOCD2";
   public String iStageReasonCD = "READY";
   public String iHRLICStatus = "ACTV";

   private ArrayList<String> updateTables = new ArrayList<String>();
   {
      updateTables.add(
            "insert into LIC_DEFN (LIC_DB_ID, LIC_ID, CARRIER_DB_ID, CARRIER_ID, LIC_STATUS_DB_ID, LIC_STATUS_CD, LIC_CD) values (4650, LIC_DEFN_ID.nextval, 0, 1001, 0, 'ACTV', 'AUTOCD1')" );
      updateTables.add(
            "insert into LIC_DEFN (LIC_DB_ID, LIC_ID, CARRIER_DB_ID, CARRIER_ID, LIC_STATUS_DB_ID, LIC_STATUS_CD, LIC_CD) values (4650, LIC_DEFN_ID.nextval, 0, 1001, 0, 'ACTV', 'AUTOCD2')" );

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
      RestoreData();
      super.after();
   }


   /**
    * This test is to verify USRLIC_IMPORT validation functionality of staging table c_org_hr_lic.
    * Single user on one license.
    *
    *
    */
   @Test
   public void testSingleUserLicesnse_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lLic = new LinkedHashMap<>();

      lLic.put( "USERNAME", "\'" + iUserName1 + "\'" );
      lLic.put( "LIC_CD", "\'" + iLicCD1 + "\'" );
      // lLic.put( "EFFECT_DT", "\'" + iDate + "\'" );
      lLic.put( "RELEASE_NO", "\'1.0\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ORG_HR_LIC, lLic ) );

      setCurrentDate( iUserName1 );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify USRLIC_IMPORT import functionality of staging table c_org_hr_lic.
    * Single user on one license.
    *
    *
    */
   @Test
   public void testSingleUserLicesnse_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testSingleUserLicesnse_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify ORG_HR_LIC
      simpleIDs lHRIDs = GetHRID( iUserName1 );
      simpleIDs LLicIDs = GetLICID( iLicCD1 );

      VerifyORGHRLIC( iUserName1, iLicCD1, lHRIDs, LLicIDs, "1.0", iStageReasonCD, iHRLICStatus );

   }


   /**
    * This test is to verify USRLIC_IMPORT validation functionality of staging table c_org_hr_lic. 2
    * users on one license.
    *
    *
    */
   @Test
   public void testTwoUsersOneLicesnse_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lLic = new LinkedHashMap<>();

      lLic.put( "USERNAME", "\'" + iUserName1 + "\'" );
      lLic.put( "LIC_CD", "\'" + iLicCD1 + "\'" );
      lLic.put( "RELEASE_NO", "\'1.0\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ORG_HR_LIC, lLic ) );

      setCurrentDate( iUserName1 );

      // Second record
      lLic.clear();
      lLic.put( "USERNAME", "\'" + iUserName2 + "\'" );
      lLic.put( "LIC_CD", "\'" + iLicCD1 + "\'" );
      lLic.put( "RELEASE_NO", "\'1.0\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ORG_HR_LIC, lLic ) );

      setCurrentDate( iUserName2 );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify USRLIC_IMPORT import functionality of staging table c_org_hr_lic. users
    * on one license.
    *
    *
    */
   @Test
   public void testTwoUsersOneLicesnse_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testTwoUsersOneLicesnse_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify ORG_HR_LIC
      simpleIDs lHRIDs = GetHRID( iUserName1 );
      simpleIDs lHRIDs2 = GetHRID( iUserName2 );
      simpleIDs LLicIDs = GetLICID( iLicCD1 );

      VerifyORGHRLIC( iUserName1, iLicCD1, lHRIDs, LLicIDs, "1.0", iStageReasonCD, iHRLICStatus );
      VerifyORGHRLIC( iUserName2, iLicCD1, lHRIDs2, LLicIDs, "1.0", iStageReasonCD, iHRLICStatus );

   }


   /**
    * This test is to verify USRLIC_IMPORT validation functionality of staging table c_org_hr_lic. 2
    * users on 2 license.
    *
    *
    */
   @Test
   public void testTwoUsersTwoLicesnse_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr_lic table
      Map<String, String> lLic = new LinkedHashMap<>();

      lLic.put( "USERNAME", "\'" + iUserName1 + "\'" );
      lLic.put( "LIC_CD", "\'" + iLicCD1 + "\'" );
      lLic.put( "RELEASE_NO", "\'1.0\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ORG_HR_LIC, lLic ) );

      setCurrentDate( iUserName1 );

      // Second record
      lLic.clear();
      lLic.put( "USERNAME", "\'" + iUserName2 + "\'" );
      lLic.put( "LIC_CD", "\'" + iLicCD2 + "\'" );
      lLic.put( "RELEASE_NO", "\'1.0\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ORG_HR_LIC, lLic ) );

      setCurrentDate( iUserName2 );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify USRLIC_IMPORT import functionality of staging table c_org_hr_lic. 2
    * users on 2 license.
    *
    *
    */
   @Test
   public void testTwoUsersTwoLicesnse_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );
      testTwoUsersTwoLicesnse_VALIDATION();

      System.out.println( "Finish validation" );

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify ORG_HR_LIC
      simpleIDs lHRIDs = GetHRID( iUserName1 );
      simpleIDs lHRIDs2 = GetHRID( iUserName2 );
      simpleIDs LLicIDs = GetLICID( iLicCD1 );
      simpleIDs LLicIDs2 = GetLICID( iLicCD2 );

      VerifyORGHRLIC( iUserName1, iLicCD1, lHRIDs, LLicIDs, "1.0", iStageReasonCD, iHRLICStatus );
      VerifyORGHRLIC( iUserName2, iLicCD2, lHRIDs2, LLicIDs2, "1.0", iStageReasonCD, iHRLICStatus );

   }


   // ===========================================================================
   /**
    * This function is to verify ORG_HR_LIC table by given parameter.
    *
    *
    */
   public void VerifyORGHRLIC( String aUserName, String aLICCD, simpleIDs aHRIDs, simpleIDs aLicIDs,
         String aReleaseNo, String aStageReasonCD, String aHRLICStatus ) {

      // ORG_HR_LIC
      String[] iIds = { "HR_LIC_STATUS_CD", "RELEASE_NO", "STAGE_REASON_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "LIC_DB_ID", aLicIDs.getNO_DB_ID() );
      lArgs.addArguments( "LIC_ID", aLicIDs.getNO_ID() );
      lArgs.addArguments( "HR_DB_ID", aHRIDs.getNO_DB_ID() );
      lArgs.addArguments( "HR_ID", aHRIDs.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.ORG_HR_LIC, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "HR_LIC_STATUS_CD",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aHRLICStatus ) );
      Assert.assertTrue( "RELEASE_NO", llists.get( 0 ).get( 1 ).equalsIgnoreCase( aReleaseNo ) );
      Assert.assertTrue( "aStageReasonCD",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aStageReasonCD ) );

      // Verify date
      String lQuery1 = "Select EFFECT_DT from ORG_HR_LIC where HR_DB_ID=" + aHRIDs.getNO_DB_ID()
            + " and HR_ID=" + aHRIDs.getNO_ID() + " and LIC_DB_ID=" + aLicIDs.getNO_DB_ID()
            + " and LIC_ID=" + aLicIDs.getNO_ID();

      java.sql.Date lDate1 = getDateValueFromQuery( lQuery1, "EFFECT_DT" );

      String lQuery2 = "Select EFFECT_DT from c_org_hr_lic where USERNAME='" + aUserName
            + "' and LIC_CD='" + aLICCD + "'";

      java.sql.Date lDate2 = getDateValueFromQuery( lQuery2, "EFFECT_DT" );

      Assert.assertTrue( "Data difference should be 0.", DateDiffInDays( lDate1, lDate2 ) == 0 );

   }


   /**
    * This function is to retrieve LIC IDs from LIC_DEFN by given parameter.
    *
    *
    */
   public simpleIDs GetLICID( String aLicCD ) {

      // LIC_DEFN
      String[] iIds = { "LIC_DB_ID", "LIC_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "LIC_CD", aLicCD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.LIC_DEFN, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      simpleIDs lLicIDs = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lLicIDs;

   }


   /**
    * This function is to retrieve HR IDs from utl_user and ORG_HR by given parameter.
    *
    *
    */
   public simpleIDs GetHRID( String aUserName ) {

      // UTL_USER
      String[] iIds = { "USER_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "USERNAME", aUserName );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.UTL_USER, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      String lID = llists.get( 0 ).get( 0 );

      // ORG_HR
      llists.clear();
      String[] lIdsHR = { "HR_DB_ID", "HR_ID" };
      List<String> lfields2 = new ArrayList<String>( Arrays.asList( lIdsHR ) );

      // Parameters required by the query.
      WhereClause lArgs2 = new WhereClause();
      lArgs2.addArguments( "USER_ID", lID );

      iQueryString = TableUtil.buildTableQuery( TableUtil.ORG_HR, lfields2, lArgs2 );
      llists = execute( iQueryString, lfields2 );

      simpleIDs lHRIDs = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lHRIDs;

   }


   /**
    * This function is called by after test method to restore data which has been modified by tests.
    *
    *
    */

   public void RestoreData() {

      String lStrDelete = null;

      // Delete LIC_DEFN table
      lStrDelete = "delete from " + TableUtil.LIC_DEFN;
      executeSQL( lStrDelete );

      // Delete ORG_HR_LIC table
      lStrDelete = "delete from " + TableUtil.ORG_HR_LIC;
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
                     .prepareCall( "BEGIN usrlic_import.validate_usrlic(on_retcode => ?); END;" );

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
                     .prepareCall( "BEGIN usrlic_import.import_usrlic(on_retcode => ?); END;" );

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


   public void setCurrentDate( String aUserName ) {
      String aUpdateQuery =
            "UPDATE C_ORG_HR_LIC SET EFFECT_DT= (SELECT SYSDATE FROM DUAL) where USERNAME='"
                  + aUserName + "'";
      try {

         PreparedStatement lStatement = getConnection().prepareStatement( aUpdateQuery );
         lStatement.executeUpdate( aUpdateQuery );
         commit();

      } catch ( SQLException e ) {
         e.printStackTrace();
      }
   }

}
