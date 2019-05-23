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
 * This test suite contains test cases on validation and import functionality of USER_IMPORT
 * package.
 *
 * @author ALICIA QIAN
 */
public class USERS extends BaselineLoaderTest {

   @Rule
   public TestName testName = new TestName();

   ValidationAndImport ivalidationandimport;

   public String iUserName1 = "AUTOUSER1";
   public String iUserName2 = "AUTOUSER2";
   public String iFirstName = "AUTO";
   public String iMidName1 = "QIAN";
   public String iMidName2 = "HUI";
   public String iLastName1 = "USER1";
   public String iLastName2 = "USER2";
   public String iRole1 = "QC";
   public String iRole2 = "TECHREC";
   public String iOrg1 = "MXI";
   public String iOrg2 = "DEFAULT";
   public String iDept1 = "AIRPORT2-ENG";
   public String iDept2 = "AIRPORT2-MAINT";
   public String iSkill1 = "AET";
   public String iSkill2 = "AVIONICS";
   public String iEmail1 = "123@yahoo.com";
   public String iEmail2 = "456@yahoo.com";
   public String iPayMethodCD = "CONT";
   public String iAuthorityName = "AUTOTEST";
   public String iHRCD1 = "AUTOHR1";
   public String iHRCD2 = "AUTOHR2";
   public String iFlowCD = "BORROW";
   public String iUsrLvlCD = "FINANCE";

   public String iUserID = null;
   public simpleIDs iHRIDs = null;

   public String iUserID2 = null;
   public simpleIDs iHRIDs2 = null;

   private ArrayList<String> updateTables = new ArrayList<String>();
   {
      updateTables.add(
            "insert into org_authority (AUTHORITY_DB_ID, AUTHORITY_ID, AUTHORITY_CD, AUTHORITY_NAME) values (4650, ORG_AUTHORITY_ID.NEXTVAL, 'AUTOTEST', 'AUTOTEST')" );
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
    * This test is to verify USER_IMPORT validation functionality of staging table c_org_hr and
    * c_org_hr_flow_level staging tables. Put multiple options in all the lists.
    *
    *
    */
   @Test
   public void testMultupleOptions_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr table
      Map<String, String> lHR = new LinkedHashMap<>();

      lHR.put( "USERNAME", "\'" + iUserName1 + "\'" );
      lHR.put( "FIRST_NAME", "\'" + iFirstName + "\'" );
      lHR.put( "MIDDLE_NAME", "\'" + iMidName1 + "\'" );
      lHR.put( "LAST_NAME", "\'" + iLastName1 + "\'" );
      lHR.put( "ROLE_LIST", "\'" + iRole1 + "," + iRole2 + "\'" );
      lHR.put( "ORG_LIST", "\'" + iOrg1 + "," + iOrg2 + "\'" );
      lHR.put( "ORG_DEPT_LIST", "\'" + iDept1 + "," + iDept2 + "\'" );
      lHR.put( "SKILL_LIST", "\'" + iSkill1 + "," + iSkill2 + "\'" );
      lHR.put( "ALERT_EMAIL_ADDRESS", "\'" + iEmail1 + "\'" );
      lHR.put( "EMAIL_ADDR", "\'" + iEmail1 + "\'" );
      lHR.put( "PAY_METHOD_CD", "\'" + iPayMethodCD + "\'" );
      lHR.put( "ALL_AUTHORITY_BOOL", "\'Y\'" );
      lHR.put( "ALL_LOCATIONS_BOOL", "\'Y\'" );
      lHR.put( "ACTUAL_HOURLY_COST", "\'25\'" );
      lHR.put( "AUTHORITY_LIST", "\'" + iAuthorityName + "\'" );
      lHR.put( "HR_CD", "\'" + iHRCD1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ORG_HR, lHR ) );

      // c_org_hr_flow_level table
      lHR.clear();
      lHR.put( "USERNAME", "\'" + iUserName1 + "\'" );
      lHR.put( "FLOW_CD", "\'" + iFlowCD + "\'" );
      lHR.put( "USER_LEVEL_CD", "\'" + iUsrLvlCD + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ORG_HR_FLOW_LEVEL, lHR ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify USER_IMPORT import functionality of staging table c_org_hr and
    * c_org_hr_flow_level staging tables. Put multiple options in all the lists.
    *
    *
    */
   @Test
   public void testMultupleOptions_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testMultupleOptions_VALIDATION();

      System.out.println( "Finish validation" );

      iUserID = null;
      iHRIDs = null;

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify UTL_USER table
      simpleIDs lUserIDs =
            VerifyUTLUSER( iUserName1, iFirstName, iMidName1, iLastName1, iEmail1, iEmail1 );
      iUserID = lUserIDs.getNO_ID();

      // Verify ORG_HR
      iHRIDs = VerifyORGHR( iUserID, iHRCD1, iPayMethodCD, "1", "1", "25" );

      // Verify UTL_USER_ROLE
      String lRoleID1 = GetRoleID( iRole1 );
      String lRoleID2 = GetRoleID( iRole2 );
      VerifyUTLUSERROLE( iUserID, lRoleID1, "1", "2" );
      VerifyUTLUSERROLE( iUserID, lRoleID2, "1", "2" );

      // Verify ORG_HR_QUAL
      VerifyORGHRQUAL( iHRIDs, iSkill1 );
      VerifyORGHRQUAL( iHRIDs, iSkill2 );

      // Verify ORG_HR_AUTHORITY
      simpleIDs lAuthIDs = getAuthorityIDs( iAuthorityName );
      VerifyORGHRAUTHORITY( iHRIDs, lAuthIDs, "1" );

      // Verify ORG_DEPT_HR
      simpleIDs lDeptIDs1 = getDeptIDs( iDept1 );
      simpleIDs lDeptIDs2 = getDeptIDs( iDept2 );
      VerifyORGDEPTHR( iHRIDs, lDeptIDs1 );
      VerifyORGDEPTHR( iHRIDs, lDeptIDs2 );

      // Verify ORG_ORG_HR
      simpleIDs lORGIDs1 = getORGIDs( iOrg1 );
      simpleIDs LORGIDs2 = getORGIDs( iOrg2 );
      VerifyORGORGHR( iHRIDs, lORGIDs1 );
      VerifyORGORGHR( iHRIDs, LORGIDs2 );

      // Verify ORG_HR_PO_AUTH_LVL
      String lPOAUTHCD = getPOAUTHLVL_CD( iFlowCD, iUsrLvlCD );
      VerifyORGHRPOAUTHLVL( iHRIDs, lPOAUTHCD );

      // Verify ORG_HR_SUPPLY
      simpleIDs llocIds1 = GetLocSupplyID( lDeptIDs1 );
      simpleIDs llocIds2 = GetLocSupplyID( lDeptIDs2 );
      VerifyORGHRSUPPLY( iHRIDs, iUserID, llocIds1 );
      VerifyORGHRSUPPLY( iHRIDs, iUserID, llocIds2 );

   }


   /**
    * This test is to verify USER_IMPORT validation functionality of staging table c_org_hr and
    * c_org_hr_flow_level staging tables. Single option in all the lists.
    *
    *
    */
   @Test
   public void testSingleOption_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr table
      Map<String, String> lHR = new LinkedHashMap<>();

      lHR.put( "USERNAME", "\'" + iUserName1 + "\'" );
      lHR.put( "FIRST_NAME", "\'" + iFirstName + "\'" );
      lHR.put( "MIDDLE_NAME", "\'" + iMidName1 + "\'" );
      lHR.put( "LAST_NAME", "\'" + iLastName1 + "\'" );
      lHR.put( "ROLE_LIST", "\'" + iRole1 + "\'" );
      lHR.put( "ORG_LIST", "\'" + iOrg1 + "\'" );
      lHR.put( "ORG_DEPT_LIST", "\'" + iDept1 + "\'" );
      lHR.put( "SKILL_LIST", "\'" + iSkill1 + "\'" );
      lHR.put( "ALERT_EMAIL_ADDRESS", "\'" + iEmail1 + "\'" );
      lHR.put( "EMAIL_ADDR", "\'" + iEmail1 + "\'" );
      lHR.put( "PAY_METHOD_CD", "\'" + iPayMethodCD + "\'" );
      lHR.put( "ALL_AUTHORITY_BOOL", "\'Y\'" );
      lHR.put( "ALL_LOCATIONS_BOOL", "\'Y\'" );
      lHR.put( "ACTUAL_HOURLY_COST", "\'25\'" );
      lHR.put( "AUTHORITY_LIST", "\'" + iAuthorityName + "\'" );
      lHR.put( "HR_CD", "\'" + iHRCD1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ORG_HR, lHR ) );

      // c_org_hr_flow_level table
      lHR.clear();
      lHR.put( "USERNAME", "\'" + iUserName1 + "\'" );
      lHR.put( "FLOW_CD", "\'" + iFlowCD + "\'" );
      lHR.put( "USER_LEVEL_CD", "\'" + iUsrLvlCD + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ORG_HR_FLOW_LEVEL, lHR ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify USER_IMPORT import functionality of staging table c_org_hr and
    * c_org_hr_flow_level staging tables. Single option in all the lists.
    *
    *
    */
   @Test
   public void testSingleOption_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testSingleOption_VALIDATION();

      System.out.println( "Finish validation" );

      iUserID = null;
      iHRIDs = null;

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify UTL_USER table
      simpleIDs lUserIDs =
            VerifyUTLUSER( iUserName1, iFirstName, iMidName1, iLastName1, iEmail1, iEmail1 );
      iUserID = lUserIDs.getNO_ID();

      // Verify ORG_HR
      iHRIDs = VerifyORGHR( iUserID, iHRCD1, iPayMethodCD, "1", "1", "25" );

      // Verify UTL_USER_ROLE
      String lRoleID1 = GetRoleID( iRole1 );
      VerifyUTLUSERROLE( iUserID, lRoleID1, "1", "2" );

      // Verify ORG_HR_QUAL
      VerifyORGHRQUAL( iHRIDs, iSkill1 );

      // Verify ORG_HR_AUTHORITY
      simpleIDs lAuthIDs = getAuthorityIDs( iAuthorityName );
      VerifyORGHRAUTHORITY( iHRIDs, lAuthIDs, "1" );

      // Verify ORG_DEPT_HR
      simpleIDs lDeptIDs1 = getDeptIDs( iDept1 );
      VerifyORGDEPTHR( iHRIDs, lDeptIDs1 );

      // Verify ORG_ORG_HR
      simpleIDs lORGIDs1 = getORGIDs( iOrg1 );
      VerifyORGORGHR( iHRIDs, lORGIDs1 );

      // Verify ORG_HR_PO_AUTH_LVL
      String lPOAUTHCD = getPOAUTHLVL_CD( iFlowCD, iUsrLvlCD );
      VerifyORGHRPOAUTHLVL( iHRIDs, lPOAUTHCD );

      // Verify ORG_HR_SUPPLY
      simpleIDs llocIds1 = GetLocSupplyID( lDeptIDs1 );
      VerifyORGHRSUPPLY( iHRIDs, iUserID, llocIds1 );

   }


   /**
    * This test is to verify USER_IMPORT validation functionality of staging table c_org_hr and
    * c_org_hr_flow_level staging tables. Multiple records validation.
    *
    *
    */
   @Test
   public void testMultipleRecords_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr table
      Map<String, String> lHR = new LinkedHashMap<>();

      lHR.put( "USERNAME", "\'" + iUserName1 + "\'" );
      lHR.put( "FIRST_NAME", "\'" + iFirstName + "\'" );
      lHR.put( "MIDDLE_NAME", "\'" + iMidName1 + "\'" );
      lHR.put( "LAST_NAME", "\'" + iLastName1 + "\'" );
      lHR.put( "ROLE_LIST", "\'" + iRole1 + "\'" );
      lHR.put( "ORG_LIST", "\'" + iOrg1 + "\'" );
      lHR.put( "ORG_DEPT_LIST", "\'" + iDept1 + "\'" );
      lHR.put( "SKILL_LIST", "\'" + iSkill1 + "\'" );
      lHR.put( "ALERT_EMAIL_ADDRESS", "\'" + iEmail1 + "\'" );
      lHR.put( "EMAIL_ADDR", "\'" + iEmail1 + "\'" );
      lHR.put( "PAY_METHOD_CD", "\'" + iPayMethodCD + "\'" );
      lHR.put( "ALL_AUTHORITY_BOOL", "\'Y\'" );
      lHR.put( "ALL_LOCATIONS_BOOL", "\'Y\'" );
      lHR.put( "ACTUAL_HOURLY_COST", "\'25\'" );
      lHR.put( "AUTHORITY_LIST", "\'" + iAuthorityName + "\'" );
      lHR.put( "HR_CD", "\'" + iHRCD1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ORG_HR, lHR ) );

      // Second record
      lHR.clear();
      lHR.put( "USERNAME", "\'" + iUserName2 + "\'" );
      lHR.put( "FIRST_NAME", "\'" + iFirstName + "\'" );
      lHR.put( "MIDDLE_NAME", "\'" + iMidName2 + "\'" );
      lHR.put( "LAST_NAME", "\'" + iLastName2 + "\'" );
      lHR.put( "ROLE_LIST", "\'" + iRole2 + "\'" );
      lHR.put( "ORG_LIST", "\'" + iOrg1 + "\'" );
      lHR.put( "ORG_DEPT_LIST", "\'" + iDept2 + "\'" );
      lHR.put( "SKILL_LIST", "\'" + iSkill2 + "\'" );
      lHR.put( "ALERT_EMAIL_ADDRESS", "\'" + iEmail2 + "\'" );
      lHR.put( "EMAIL_ADDR", "\'" + iEmail2 + "\'" );
      lHR.put( "PAY_METHOD_CD", "\'" + iPayMethodCD + "\'" );
      lHR.put( "ALL_AUTHORITY_BOOL", "\'Y\'" );
      lHR.put( "ALL_LOCATIONS_BOOL", "\'Y\'" );
      lHR.put( "ACTUAL_HOURLY_COST", "\'25\'" );
      lHR.put( "AUTHORITY_LIST", "\'" + iAuthorityName + "\'" );
      lHR.put( "HR_CD", "\'" + iHRCD2 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ORG_HR, lHR ) );

      // c_org_hr_flow_level table
      lHR.clear();
      lHR.put( "USERNAME", "\'" + iUserName1 + "\'" );
      lHR.put( "FLOW_CD", "\'" + iFlowCD + "\'" );
      lHR.put( "USER_LEVEL_CD", "\'" + iUsrLvlCD + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ORG_HR_FLOW_LEVEL, lHR ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   /**
    * This test is to verify USER_IMPORT import functionality of staging table c_org_hr and
    * c_org_hr_flow_level staging tables. Multiple records import.
    *
    *
    */

   @Test
   public void testMultipleRecords_IMPORT() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      testMultipleRecords_VALIDATION();

      System.out.println( "Finish validation" );

      iUserID = null;
      iHRIDs = null;
      iUserID2 = null;
      iHRIDs2 = null;

      // run validation
      Assert.assertTrue( runValidationAndImport( false, true ) == 1 );

      // Verify UTL_USER table
      simpleIDs lUserIDs =
            VerifyUTLUSER( iUserName1, iFirstName, iMidName1, iLastName1, iEmail1, iEmail1 );
      iUserID = lUserIDs.getNO_ID();

      simpleIDs lUserIDs2 =
            VerifyUTLUSER( iUserName2, iFirstName, iMidName2, iLastName2, iEmail2, iEmail2 );
      iUserID2 = lUserIDs2.getNO_ID();

      // Verify ORG_HR
      iHRIDs = VerifyORGHR( iUserID, iHRCD1, iPayMethodCD, "1", "1", "25" );
      iHRIDs2 = VerifyORGHR( iUserID2, iHRCD2, iPayMethodCD, "1", "1", "25" );

      // Verify UTL_USER_ROLE
      String lRoleID1 = GetRoleID( iRole1 );
      VerifyUTLUSERROLE( iUserID, lRoleID1, "1", "2" );

      String lRoleID2 = GetRoleID( iRole2 );
      VerifyUTLUSERROLE( iUserID2, lRoleID2, "1", "2" );

      // Verify ORG_HR_QUAL
      VerifyORGHRQUAL( iHRIDs, iSkill1 );
      VerifyORGHRQUAL( iHRIDs2, iSkill2 );

      // Verify ORG_HR_AUTHORITY
      simpleIDs lAuthIDs = getAuthorityIDs( iAuthorityName );
      VerifyORGHRAUTHORITY( iHRIDs, lAuthIDs, "1" );
      VerifyORGHRAUTHORITY( iHRIDs2, lAuthIDs, "1" );

      // Verify ORG_DEPT_HR
      simpleIDs lDeptIDs1 = getDeptIDs( iDept1 );
      VerifyORGDEPTHR( iHRIDs, lDeptIDs1 );

      simpleIDs lDeptIDs2 = getDeptIDs( iDept2 );
      VerifyORGDEPTHR( iHRIDs2, lDeptIDs2 );

      // Verify ORG_ORG_HR
      simpleIDs lORGIDs1 = getORGIDs( iOrg1 );
      VerifyORGORGHR( iHRIDs, lORGIDs1 );
      VerifyORGORGHR( iHRIDs2, lORGIDs1 );

      // Verify ORG_HR_PO_AUTH_LVL
      String lPOAUTHCD = getPOAUTHLVL_CD( iFlowCD, iUsrLvlCD );
      VerifyORGHRPOAUTHLVL( iHRIDs, lPOAUTHCD );

      // Verify ORG_HR_SUPPLY
      simpleIDs llocIds1 = GetLocSupplyID( lDeptIDs1 );
      simpleIDs llocIds2 = GetLocSupplyID( lDeptIDs2 );
      VerifyORGHRSUPPLY( iHRIDs, iUserID, llocIds1 );
      VerifyORGHRSUPPLY( iHRIDs, iUserID, llocIds2 );

   }


   /**
    * This test is to verify CFGUSR-12112: he department list currently has duplicate entries.
    *
    *
    */
   @Test
   public void testCFGUSR_12112_VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // c_org_hr table
      Map<String, String> lHR = new LinkedHashMap<>();

      lHR.put( "USERNAME", "\'" + iUserName1 + "\'" );
      lHR.put( "FIRST_NAME", "\'" + iFirstName + "\'" );
      lHR.put( "MIDDLE_NAME", "\'" + iMidName1 + "\'" );
      lHR.put( "LAST_NAME", "\'" + iLastName1 + "\'" );
      lHR.put( "ROLE_LIST", "\'" + iRole1 + "," + iRole2 + "\'" );
      lHR.put( "ORG_LIST", "\'" + iOrg1 + "," + iOrg2 + "\'" );
      lHR.put( "ORG_DEPT_LIST", "\'" + iDept1 + "," + iDept1 + "\'" );
      lHR.put( "SKILL_LIST", "\'" + iSkill1 + "," + iSkill2 + "\'" );
      lHR.put( "ALERT_EMAIL_ADDRESS", "\'" + iEmail1 + "\'" );
      lHR.put( "EMAIL_ADDR", "\'" + iEmail1 + "\'" );
      lHR.put( "PAY_METHOD_CD", "\'" + iPayMethodCD + "\'" );
      lHR.put( "ALL_AUTHORITY_BOOL", "\'Y\'" );
      lHR.put( "ALL_LOCATIONS_BOOL", "\'Y\'" );
      lHR.put( "ACTUAL_HOURLY_COST", "\'25\'" );
      lHR.put( "AUTHORITY_LIST", "\'" + iAuthorityName + "\'" );
      lHR.put( "HR_CD", "\'" + iHRCD1 + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ORG_HR, lHR ) );

      // c_org_hr_flow_level table
      lHR.clear();
      lHR.put( "USERNAME", "\'" + iUserName1 + "\'" );
      lHR.put( "FLOW_CD", "\'" + iFlowCD + "\'" );
      lHR.put( "USER_LEVEL_CD", "\'" + iUsrLvlCD + "\'" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_ORG_HR_FLOW_LEVEL, lHR ) );

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == -1 );
      checkUserErrorCode( "testCFGUSR_12112_VALIDATION", "CFGUSR-12112" );

   }


   // ==============================================================================================================
   /**
    * This function is to retrieve supply location IDs from inv_loc_dept and INV_LOC by given
    * parameter.
    *
    *
    */
   public simpleIDs GetLocSupplyID( simpleIDs aDeptIDs ) {

      // INV_LOC_DEPT
      String[] iIds = { "LOC_DB_ID", "LOC_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "DEPT_DB_ID", aDeptIDs.getNO_DB_ID() );
      lArgs.addArguments( "DEPT_ID", aDeptIDs.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.INV_LOC_DEPT, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      simpleIDs lIDs = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      // INV_LOC
      llists.clear();
      String[] iIdsSupply = { "SUPPLY_LOC_DB_ID", "SUPPLY_LOC_ID" };
      List<String> lfields2 = new ArrayList<String>( Arrays.asList( iIdsSupply ) );

      // Parameters required by the query.
      WhereClause lArgs2 = new WhereClause();
      lArgs2.addArguments( "LOC_DB_ID", lIDs.getNO_DB_ID() );
      lArgs2.addArguments( "LOC_ID", lIDs.getNO_ID() );

      iQueryString = TableUtil.buildTableQuery( TableUtil.INV_LOC, lfields2, lArgs2 );
      llists = execute( iQueryString, lfields2 );

      simpleIDs lLocIDs = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lLocIDs;

   }


   /**
    * This function is to verify ORG_HR_SUPPLY table by given parameters .
    *
    *
    */
   public void VerifyORGHRSUPPLY( simpleIDs aHRIDs, String aUserID, simpleIDs aLocIds ) {

      String lQuery = "Select 1 from " + TableUtil.ORG_HR_SUPPLY + " Where HR_DB_ID='"
            + aHRIDs.getNO_DB_ID() + "' and HR_ID='" + aHRIDs.getNO_ID() + "' and USER_ID='"
            + aUserID + "' and LOC_DB_ID='" + aLocIds.getNO_DB_ID() + "' and LOC_ID='"
            + aLocIds.getNO_ID() + "'";
      Assert.assertTrue( "Record should exist in table:", RecordsExist( lQuery ) );

   }


   /**
    * This function is to verify ORG_HR_PO_AUTH_LVL table by given parameters .
    *
    *
    */

   public void VerifyORGHRPOAUTHLVL( simpleIDs aHRIDs, String aLVL ) {

      String lQuery = "Select 1 from " + TableUtil.ORG_HR_PO_AUTH_LVL + " Where HR_DB_ID='"
            + aHRIDs.getNO_DB_ID() + "' and HR_ID='" + aHRIDs.getNO_ID() + "' and PO_AUTH_LVL_CD='"
            + aLVL + "'";
      Assert.assertTrue( "Record should exist in table:", RecordsExist( lQuery ) );

   }


   /**
    * This function is to retrieve PO_AUTH_LVL_CD from REF_PO_AUTH_LVL table by given parameters .
    *
    *
    */

   public String getPOAUTHLVL_CD( String aFlowCD, String UsrLvlCD ) {
      // REF_PO_AUTH_LVL
      String[] iIds = { "PO_AUTH_LVL_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "PO_AUTH_FLOW_CD", aFlowCD );
      lArgs.addArguments( "USER_CD", UsrLvlCD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.REF_PO_AUTH_LVL, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      String lID = llists.get( 0 ).get( 0 );
      return lID;

   }


   /**
    * This function is to verify ORG_ORG_HR table by given parameters .
    *
    *
    */

   public void VerifyORGORGHR( simpleIDs aHRIDs, simpleIDs aORGIDs ) {

      String lQuery = "Select 1 from " + TableUtil.ORG_ORG_HR + " Where HR_DB_ID='"
            + aHRIDs.getNO_DB_ID() + "' and HR_ID='" + aHRIDs.getNO_ID() + "' and ORG_DB_ID='"
            + aORGIDs.getNO_DB_ID() + "' and ORG_ID='" + aORGIDs.getNO_ID() + "'";
      Assert.assertTrue( "Record should exist in table:", RecordsExist( lQuery ) );

   }


   /**
    * This function is to retrieve ORG IDs from ORG_ORG table by given parameters .
    *
    *
    */

   public simpleIDs getORGIDs( String aOrg ) {

      // Org_org
      String[] iIds = { "ORG_DB_ID", "ORG_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "ORG_CD", aOrg );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.ORG_ORG, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      simpleIDs lIDs = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );
      return lIDs;

   }


   /**
    * This function is to verify ORG_DEPT_HR table by given parameters .
    *
    *
    */

   public void VerifyORGDEPTHR( simpleIDs aHRIDs, simpleIDs aDeptIDs ) {

      String lQuery = "Select 1 from " + TableUtil.ORG_DEPT_HR + " Where HR_DB_ID='"
            + aHRIDs.getNO_DB_ID() + "' and HR_ID='" + aHRIDs.getNO_ID() + "' and DEPT_DB_ID='"
            + aDeptIDs.getNO_DB_ID() + "' and DEPT_ID='" + aDeptIDs.getNO_ID() + "'";
      Assert.assertTrue( "Record should exist in table:", RecordsExist( lQuery ) );

   }


   /**
    * This function is to retrieve Dept IDs from ORG_WORK_DEPT table by given parameters .
    *
    *
    */
   public simpleIDs getDeptIDs( String aDept ) {

      // ORG_AUTHORITY
      String[] iIds = { "DEPT_DB_ID", "DEPT_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "DEPT_CD", aDept );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.ORG_WORK_DEPT, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      simpleIDs lDeptIDs = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );
      return lDeptIDs;

   }


   /**
    * This function is to verify ORG_HR_AUTHORITY table by given parameters .
    *
    *
    */
   public void VerifyORGHRAUTHORITY( simpleIDs aHRIDs, simpleIDs aAuthIDs, String aNotifyBool ) {

      String[] iIds = { "AUTHORITY_DB_ID", "AUTHORITY_ID", "NOTIFY_BOOL" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "HR_DB_ID", aHRIDs.getNO_DB_ID() );
      lArgs.addArguments( "HR_ID", aHRIDs.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.ORG_HR_AUTHORITY, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      Assert.assertTrue( "AUTHORITY_DB_ID",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aAuthIDs.getNO_DB_ID() ) );
      Assert.assertTrue( "AUTHORITY_ID",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( aAuthIDs.getNO_ID() ) );
      Assert.assertTrue( "NOTIFY_BOOL", llists.get( 0 ).get( 2 ).equalsIgnoreCase( aNotifyBool ) );
   }


   /**
    * This function is to retrieve authority IDs from org_authority table by given parameters .
    *
    *
    */
   public simpleIDs getAuthorityIDs( String aAuthorityName ) {

      // ORG_AUTHORITY
      String[] iIds = { "AUTHORITY_DB_ID", "AUTHORITY_ID", "AUTHORITY_NAME" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "AUTHORITY_NAME", aAuthorityName );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.ORG_AUTHORITY, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      simpleIDs lAuthIDs = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );
      return lAuthIDs;

   }


   /**
    * This function is to verify ORG_HR_QUAL table by given parameters .
    *
    *
    */

   public void VerifyORGHRQUAL( simpleIDs aHRIDs, String aSkill ) {

      String lQuery =
            "Select 1 from " + TableUtil.ORG_HR_QUAL + " Where HR_DB_ID='" + aHRIDs.getNO_DB_ID()
                  + "' and HR_ID='" + aHRIDs.getNO_ID() + "' and LABOUR_SKILL_CD='" + aSkill + "'";
      Assert.assertTrue( "Record should exist in table:", RecordsExist( lQuery ) );

   }


   /**
    * This function is to verify UTL_ROLE table by given parameters .
    *
    *
    */

   public String GetRoleID( String aRole ) {
      // ORG_AUTHORITY
      String[] iIds = { "ROLE_ID", "ROLE_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "ROLE_CD", aRole );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.UTL_ROLE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      String lIDs = llists.get( 0 ).get( 0 );
      return lIDs;

   }


   /**
    * This function is to verify UTL_USER_ROLE table by given parameters .
    *
    *
    */

   public void VerifyUTLUSERROLE( String aUserID, String aRoleID, String aRoleOrder ) {
      VerifyUTLUSERROLE( aUserID, aRoleID, aRoleOrder, null );

   }


   /**
    * This function is to verify UTL_USER_ROLE table by given parameters .
    *
    *
    */

   public void VerifyUTLUSERROLE( String aUserID, String aRoleID, String aRoleOrder1,
         String aRoleOrder2 ) {

      // UTL_USER_ROLE
      String[] iIds = { "USER_ID", "ROLE_ID", "ROLE_ORDER" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "USER_ID", aUserID );
      lArgs.addArguments( "ROLE_ID", aRoleID );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.UTL_USER_ROLE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      if ( aRoleOrder2 != null ) {
         for ( int i = 0; i < llists.size(); i++ ) {
            Assert.assertTrue( "ROLE_ORDER",
                  llists.get( i ).get( 2 ).equalsIgnoreCase( aRoleOrder1 )
                        || llists.get( i ).get( 2 ).equalsIgnoreCase( aRoleOrder2 ) );

         }

      } else {
         Assert.assertTrue( "ROLE_ORDER",
               llists.get( 0 ).get( 2 ).equalsIgnoreCase( aRoleOrder1 ) );
      }

   }


   /**
    * This function is to verify ORG_HR table and retrieve HR IDs by given parameters .
    *
    *
    */
   public simpleIDs VerifyORGHR( String aUserID, String aHRCD, String aPayMethodCD,
         String aAuthorityBool, String aLocationBool, String aActualHourlyCost ) {

      // ORG_HR table
      String[] iIds = { "USER_ID", "HR_DB_ID", "HR_ID", "HR_CD", "PAY_METHOD_CD",
            "ALL_AUTHORITY_BOOL", "ALL_LOCATIONS_BOOL", "ACTUAL_HOURLY_COST" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "USER_ID", aUserID );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.ORG_HR, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      simpleIDs lHRIDs = new simpleIDs( llists.get( 0 ).get( 1 ), llists.get( 0 ).get( 2 ) );
      Assert.assertTrue( "HR_CD", llists.get( 0 ).get( 3 ).equalsIgnoreCase( aHRCD ) );
      Assert.assertTrue( "PAY_METHOD_CD",
            llists.get( 0 ).get( 4 ).equalsIgnoreCase( aPayMethodCD ) );
      Assert.assertTrue( "ALL_AUTHORITY_BOOL",
            llists.get( 0 ).get( 5 ).equalsIgnoreCase( aAuthorityBool ) );
      Assert.assertTrue( "ALL_LOCATIONS_BOOL",
            llists.get( 0 ).get( 6 ).equalsIgnoreCase( aLocationBool ) );
      Assert.assertTrue( "ACTUAL_HOURLY_COST",
            llists.get( 0 ).get( 7 ).equalsIgnoreCase( aActualHourlyCost ) );

      return lHRIDs;

   }


   /**
    * This function is to verify UTL_USER table and retrieve USER ID by given parameters .
    *
    *
    */
   public simpleIDs VerifyUTLUSER( String aUserName, String aFirstName, String aMidName,
         String aLastName, String aAlertEmail, String aEmail ) {

      // UTL_USER table
      String[] iIds = { "USER_ID", "USERNAME", "FIRST_NAME", "MIDDLE_NAME", "LAST_NAME",
            "ALERT_EMAIL_ADDR", "EMAIL_ADDR" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "USERNAME", aUserName );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.UTL_USER, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      simpleIDs lUserIDs = new simpleIDs( null, llists.get( 0 ).get( 0 ) );
      Assert.assertTrue( "FIRST_NAME", llists.get( 0 ).get( 2 ).equalsIgnoreCase( aFirstName ) );
      Assert.assertTrue( "MIDDLE_NAME", llists.get( 0 ).get( 3 ).equalsIgnoreCase( aMidName ) );
      Assert.assertTrue( "LAST_NAME", llists.get( 0 ).get( 4 ).equalsIgnoreCase( aLastName ) );
      Assert.assertTrue( "ALERT_EMAIL_ADDR",
            llists.get( 0 ).get( 5 ).equalsIgnoreCase( aAlertEmail ) );
      Assert.assertTrue( "EMAIL_ADDR", llists.get( 0 ).get( 6 ).equalsIgnoreCase( aEmail ) );

      return lUserIDs;

   }


   /**
    * This function is called by after test method to restore data which has been modified by tests.
    *
    *
    */

   public void RestoreData() {

      String lStrDelete = null;

      if ( iUserID != null && iHRIDs != null ) {

         deleteAll( iUserID, iHRIDs );

      }

      if ( iUserID2 != null && iHRIDs2 != null ) {

         deleteAll( iUserID2, iHRIDs2 );

      }

      // Delete iAuthorityName table
      lStrDelete = "delete from " + TableUtil.ORG_AUTHORITY + " where AUTHORITY_NAME='"
            + iAuthorityName + "'";
      executeSQL( lStrDelete );

   }


   /**
    * This function is called by after test method to restore data which has been modified by tests.
    *
    *
    */

   public void deleteAll( String aUserID, simpleIDs aHRIDs ) {

      String lStrDelete = null;
      // Delete ORG_HR_SUPPLY
      lStrDelete = "delete from " + TableUtil.ORG_HR_SUPPLY + " where USER_ID='" + aUserID + "'";
      executeSQL( lStrDelete );

      // delete ORG_HR_PO_AUTH_LVL
      lStrDelete = "delete from " + TableUtil.ORG_HR_PO_AUTH_LVL + " where HR_DB_ID='"
            + aHRIDs.getNO_DB_ID() + "' and HR_ID='" + aHRIDs.getNO_ID() + "'";
      executeSQL( lStrDelete );

      // Delete ORG_HR_HR
      lStrDelete = "delete from " + TableUtil.ORG_ORG_HR + " where HR_DB_ID='"
            + aHRIDs.getNO_DB_ID() + "' and HR_ID='" + aHRIDs.getNO_ID() + "'";
      executeSQL( lStrDelete );

      // Delete ORG_DEPT_HR
      lStrDelete = "delete from " + TableUtil.ORG_DEPT_HR + " where HR_DB_ID='"
            + aHRIDs.getNO_DB_ID() + "' and HR_ID='" + aHRIDs.getNO_ID() + "'";
      executeSQL( lStrDelete );

      // Delete ORG_HR_AUTHORITY
      lStrDelete = "delete from " + TableUtil.ORG_HR_AUTHORITY + " where HR_DB_ID='"
            + aHRIDs.getNO_DB_ID() + "' and HR_ID='" + aHRIDs.getNO_ID() + "'";
      executeSQL( lStrDelete );

      // Delete ORG_HR_QUAL
      lStrDelete = "delete from " + TableUtil.ORG_HR_QUAL + " where HR_DB_ID='"
            + aHRIDs.getNO_DB_ID() + "' and HR_ID='" + aHRIDs.getNO_ID() + "'";
      executeSQL( lStrDelete );

      // Delete UTL_USER_ROLE
      lStrDelete = "delete from " + TableUtil.UTL_USER_ROLE + " where USER_ID='" + aUserID + "'";
      executeSQL( lStrDelete );

      // Delete ORG_HR
      lStrDelete = "delete from " + TableUtil.ORG_HR + " where HR_DB_ID='" + aHRIDs.getNO_DB_ID()
            + "' and HR_ID='" + aHRIDs.getNO_ID() + "'";
      executeSQL( lStrDelete );

      // Delete UTL_USER
      lStrDelete = "delete from " + TableUtil.UTL_USER + " where USER_ID='" + aUserID + "'";
      executeSQL( lStrDelete );
   }


   /**
    * Calls check sensitivity error code
    *
    *
    */
   protected void checkUserErrorCode( String aTestName, String aValidationCode ) {

      boolean lFound = false;
      String lerror_desc = null;
      String lerrorMsg = null;

      String lquery = TestConstants.BL_USERS_ERROR_CHECK;

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
                     .prepareCall( "BEGIN user_import.validate_user(on_retcode => ?); END;" );

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
                     .prepareCall( "BEGIN user_import.import_user(on_retcode => ?); END;" );

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
