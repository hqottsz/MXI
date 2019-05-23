/*
 * Confidential, proprietary and/or trade secret information of Mxi Technologies, Ltd.
 *
 * Copyright 2000-2016 Mxi Technologies, Ltd. All Rights Reserved.
 *
 * Except as expressly provided by written license signed by a duly appointed officer of Mxi
 * Technologies, Ltd., any disclosure, distribution, reproduction, compilation, modification,
 * creation of derivative works and/or other use of the Mxi source code is strictly prohibited.
 * Inclusion of a copyright notice shall not be taken to indicate that the source code has been
 * published.
 */

package com.mxi.mx.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.After;
import org.junit.Assert;

import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;


/**
 * This test suite contains common test utilities for Actuals Loader tests.
 *
 * @author Andrew Bruce
 */

public abstract class ActualsLoaderTest extends AbstractDatabaseConnection {

   /**
    * Clean up after each individual test
    *
    * @throws Exception
    */
   @After
   @Override
   public void after() throws Exception {

      clearActualsLoaderTables();

      super.after();
   }


   /**
    * Loads source data from CSV file
    *
    * @throws FileNotFoundException
    * @throws IOException
    */
   protected static void loadSourceData() throws FileNotFoundException, IOException {

      // source data filename
      String lFilename = TestConstants.TEST_CASE_DATA + "ActLoaderValidationRulesTestData.csv";

      // Load source data from CSV file
      Reader lFile = new FileReader( lFilename );
      CSVParser lParser = new CSVParser( lFile, CSVFormat.DEFAULT );
      iList = lParser.getRecords();
      lParser.close();
   }


   /**
    * Calls checkInventory defaulting ignore USG errors to true
    *
    * @param aTestName
    *           the test method name
    * @param aValidationCode
    *           Expected validation result
    *
    * @throws Exception
    *            if there is an error
    */
   protected void checkInventory( String aTestName, String aValidationCode ) throws Exception {
      checkInventory( aTestName, aValidationCode, true );
   }


   /**
    * Calls checkInventory error code
    *
    * @param aTestName
    *           the test method name
    * @param aValidationCode
    *           Expected validation result
    *
    * @throws Exception
    *            if there is an error
    */
   protected void checkInventoryErrorCode( String aTestName, String aValidationCode )
         throws Exception {

      boolean lFound = false;
      String lquery = "Select error_cd from Al_PROC_INVENTORY_ERROR";

      String[] iIds = { "ERROR_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      List<ArrayList<String>> lresult = execute( lquery, lfields );

      String lErrors = null;

      for ( int i = 0; i < lresult.size(); i++ ) {
         if ( lresult.get( i ).get( 0 ).equalsIgnoreCase( aValidationCode ) ) {
            lFound = true;
         }

         lErrors = lresult.get( i ).get( 0 ) + ": "
               + getINventoryErrorDetail( lresult.get( i ).get( 0 ) ) + "  " + lErrors;

      }

      String lerror_desc = getINventoryErrorDetail( aValidationCode );
      Assert.assertTrue( "The error codes found- " + lErrors + ": ", lFound == true );

   }


   /**
    * This function is to get detail of error code
    *
    *
    */

   public String getINventoryErrorDetail( String aErrorcode ) {

      String lquery =
            "select 1 from " + TableUtil.DL_REF_MESSAGE + " where RESULT_CD='" + aErrorcode + "'";

      List<ArrayList<String>> llists = null;

      if ( RecordsExist( lquery ) ) {

         String[] iIds = { "USER_DESC" };
         List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
         WhereClause lArgs = new WhereClause();

         lArgs.addArguments( "RESULT_CD", aErrorcode );

         String iQueryString =
               TableUtil.buildTableQuery( TableUtil.DL_REF_MESSAGE, lfields, lArgs );
         llists = execute( iQueryString, lfields );

      }

      lquery =
            "select 1 from " + TableUtil.DL_REF_MESSAGE + " where RESULT_CD='" + aErrorcode + "'";

      if ( RecordsExist( lquery ) ) {

         String[] iIds = { "TECH_DESC" };
         List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
         WhereClause lArgs = new WhereClause();

         lArgs.addArguments( "RESULT_CD", aErrorcode );

         String iQueryString =
               TableUtil.buildTableQuery( TableUtil.DL_REF_MESSAGE, lfields, lArgs );
         llists = execute( iQueryString, lfields );

      }

      return llists.get( 0 ).get( 0 );

   }


   protected void addTaskIdsToMap( String aTaskCode, Map<String, String> aMap )
         throws SQLException {
      String lTaskId = getTaskIdByTaskCode( aTaskCode );
      String lTaskDefnId = getTaskDefnIdByTaskCode( aTaskCode );

      aMap.put( "TASK_DB_ID", "4650" );
      aMap.put( "TASK_ID", lTaskId );
      aMap.put( "TASK_DEFN_DB_ID", "4650" );
      aMap.put( "TASK_DEFN_ID", lTaskDefnId );

   }


   protected void addACInventoryIdsToMap( String aInvNoSdesc, Map<String, String> aMap )
         throws SQLException {
      String lInvId = getInvIdByInvNoSdesc( aInvNoSdesc, aInvNoSdesc );

      aMap.put( "ACFT_NO_DB_ID", "4650" );
      aMap.put( "ACFT_NO_ID", lInvId );

   }


   /**
    * Verifies results of validation, taking into account USG errors
    *
    * @param aTestName
    *           the test method name
    * @param aValidationCode
    *           Expected validation result, or Pass for no errors
    * @param aIgnoreUSGErrors
    *           Ignore USG error messages
    *
    * @throws Exception
    *            if there is an error
    */
   protected void checkInventory( String aTestName, String aValidationCode,
         boolean aIgnoreUSGErrors ) throws Exception {

      int lErrorCount;

      // query for validation results
      if ( aIgnoreUSGErrors ) {
         lErrorCount =
               getCountWithout( "AL_PROC_INVENTORY_ERROR", " where ERROR_CD not like 'USG%'" );
      } else {
         lErrorCount = getCount( "AL_PROC_INVENTORY_ERROR" );
      }

      String lTestResultErrorList = "";

      // if there are any errors query for results
      if ( lErrorCount > 0 ) {
         lTestResultErrorList = getInventoryErrors();
      }

      // compare result to expectation
      if ( aValidationCode == "PASS" ) { // expecting no errors

         // assert that no errors are found
         assertThat( "Expected PASS, but instead found validation errors: " + lTestResultErrorList,
               lErrorCount == 0 );
      } else { // expecting error

         // asserting that expected error was found
         assertThat( "Unexpected validation results. Expected: " + aValidationCode + ". Found: "
               + lTestResultErrorList, lTestResultErrorList.contains( aValidationCode ) );
      }
   }


   /**
    *
    * Verifies results of validation and excluding all warning message.
    *
    * @param aValidationCode
    *           Expected error code, or put "PASS" if it is expected to return no errors
    * @throws Exception
    */
   protected void checkInventoryNoWarnings( String aValidationCode ) throws Exception {

      boolean lFound = false;
      String lerror_desc = null;
      String lerrorMsg = null;

      String lQuery = TestConstants.AL_INVENTORY_ERROR_NO_WARNING_CHECK;

      String[] iIds = { "ERROR_CD", "USER_DESC" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      List<ArrayList<String>> lresult = execute( lQuery, lfields );

      if ( aValidationCode.equalsIgnoreCase( "PASS" ) ) {
         Assert.assertTrue( "There are some errors: ", lresult.size() == 0 );

      } else {

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

   }


   /**
    *
    * Verifies results of validation and excluding all warning message.
    *
    * @param aValidationCode
    *           Expected error code, or put "PASS" if it is expected to return no errors
    * @throws Exception
    */
   protected void checkInventoryWithWarnings( String aValidationCode ) throws Exception {

      boolean lFound = false;
      String lerror_desc = null;
      String lerrorMsg = null;

      String lQuery = TestConstants.AL_INVENTORY_ERROR_CHECK;

      String[] iIds = { "ERROR_CD", "USER_DESC" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      List<ArrayList<String>> lresult = execute( lQuery, lfields );

      if ( aValidationCode.equalsIgnoreCase( "PASS" ) ) {
         Assert.assertTrue( "There are some errors: ", lresult.size() == 0 );

      } else {

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

   }


   /**
    * Compares task validation results with the expected validation result
    *
    * @param aValidationCode
    *           Expected validation error code we are looking to find. PASS indicates that we do not
    *           expect to get any validation errors.
    *
    * @throws Exception
    *            if there is an error
    */
   protected void checkTaskValidation( String aValidationCode ) throws Exception {

      // query for validation results
      int lErrorCount = getCount( "AL_PROC_TASKS_ERROR" );
      String lTestResultErrorList = "";

      // if there are any errors query for results
      if ( lErrorCount > 0 ) {
         lTestResultErrorList = getTaskErrors();
      }

      // compare result to expectation
      if ( aValidationCode.equals( "PASS" ) ) { // expecting no errors

         // assert that no errors are found
         assertThat( "Expected PASS, but instead found validation errors: " + lTestResultErrorList,
               lErrorCount == 0 );
      } else { // expecting error

         // asserting that expected error was found
         assertThat( "Unexpected validation results. Expected: " + aValidationCode + ". Found: "
               + lTestResultErrorList, lTestResultErrorList.contains( aValidationCode ) );
      }
   }


   /**
    * Creates insert script for test data row into AL_HISTORIC_TASK table
    *
    * @param aTaskData
    *           - CSV record that insert script will be created from
    * @param aTestTypeColumnNumber
    *
    * @return String - insert script for AL_HISTORIC_TASK table
    */
   protected String createInsertForC_RI_TASK( CSVRecord aTaskData, int aTestTypeColumnNumber ) {

      // build insert script
      StringBuilder lInsertStatement = new StringBuilder();
      lInsertStatement.append( "INSERT INTO C_RI_TASK ("
            + "serial_no_oem, part_no_oem, manufact_cd, task_cd, completion_dt, completion_due_dt)"
            + " VALUES (" );

      int lDataStartPosition = aTestTypeColumnNumber + 1;

      // pull values to be inserted from record
      lInsertStatement.append( "'" + aTaskData.get( lDataStartPosition ).toString() + "'," );
      lInsertStatement.append( "'" + aTaskData.get( lDataStartPosition + 1 ).toString() + "'," );
      lInsertStatement.append( "'" + aTaskData.get( lDataStartPosition + 2 ).toString() + "'," );
      lInsertStatement.append( "'" + aTaskData.get( lDataStartPosition + 4 ).toString() + "'," );

      // date - completion date
      lInsertStatement.append( "TO_DATE('" + aTaskData.get( lDataStartPosition + 5 ).toString()
            + "', 'yyyy/mm/dd')," );

      // date - due date
      lInsertStatement.append( "TO_DATE('" + aTaskData.get( lDataStartPosition + 7 ).toString()
            + "', 'yyyy/mm/dd'))" );

      System.out.println( lInsertStatement );

      return lInsertStatement.toString();
   }


   /**
    * This function is to retrieve inventory ID by giving Serial number.
    *
    *
    *
    */

   public simpleIDs getInvIDs( String aSN ) {

      String[] iIds = { "INV_NO_DB_ID", "INV_NO_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "SERIAL_NO_OEM", aSN );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.INV_INV, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lIds;
   }


   /**
    * Check validation table results to determine if any validation errors have been found
    *
    * @param aTableName
    *           Query to be run to check for a result
    * @param aKeyName
    *
    * @return Result - Result of validation process for the submitted query
    *
    * @throws Exception
    */
   protected Result errorCheckInventory( String aTableName, String aKeyName ) throws Exception {

      Result lResult = new Result();
      lResult.success();

      // check error count

      String lCountNotStaged = getErrorCount( aTableName );

      if ( Integer.parseInt( lCountNotStaged ) > 0 ) {
         lResult = getError( aTableName, aKeyName );
      }

      return lResult;
   }


   /**
    * Check validation table results to determine if any validation errors have been found
    *
    * @return Result - Result of validation process for the submitted query
    *
    * @throws Exception
    */
   protected Result errorCheckTask() throws Exception {

      Result lErrorCheckResult = new Result();
      lErrorCheckResult.success();
      // prepare query and variables for checking test results

      // query for validation results
      int lErrorCount = getCount( "AL_PROC_TASKS_ERROR" );

      // if there are any errors query for results
      if ( lErrorCount > 0 ) {

         String lTestResultErrorList = getTaskErrors();

         lErrorCheckResult.failed();
         lErrorCheckResult.setMessage( "Validation failed: " + lTestResultErrorList );
      }

      return lErrorCheckResult;
   }


   /**
    * Check validation table results to determine if any validation errors have been found
    *
    * @return Result - Result of validation process for the submitted query
    *
    * @throws Exception
    */
   protected Result errorCheckInventory() throws Exception {

      Result lErrorCheckResult = new Result();
      lErrorCheckResult.success();
      // prepare query and variables for checking test results

      // query for validation results
      int lErrorCount = getCount( "AL_PROC_INVENTORY_ERROR" );

      // if there are any errors query for results
      if ( lErrorCount > 0 ) {

         String lTestResultErrorList = getInventoryErrors();

         lErrorCheckResult.failed();
         lErrorCheckResult.setMessage( "Validation failed: " + lTestResultErrorList );
      }

      return lErrorCheckResult;
   }


   /**
    * Runs the provided query on the database
    *
    * @param aTable
    *           Query to be run
    *
    * @return Returns the string query result
    */
   @Override
   protected int getCount( String aTable ) {

      // prepare query for checking test results
      String lQuery = "select count(*) as \"COUNT\" from " + aTable;

      try {
         PreparedStatement lStatement = getConnection().prepareStatement( lQuery,
               ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );

         ResultSet lResultSet = lStatement.executeQuery( lQuery );
         lResultSet.next();

         return lResultSet.getInt( "COUNT" );
      } catch ( SQLException e ) {
         e.printStackTrace();

         return 0;
      }
   }


   /**
    * Runs the provided query on the database
    *
    * @param aTable
    *           Query to be run
    * @param aWithoutFilter
    *           DOCUMENT_ME
    *
    * @return Returns the string query result
    */
   protected int getCountWithout( String aTable, String aWithoutFilter ) {

      // prepare query for checking test results
      String lQuery = "select count(*) as \"COUNT\" from " + aTable + aWithoutFilter;

      try {
         PreparedStatement lStatement = getConnection().prepareStatement( lQuery,
               ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );

         ResultSet lResultSet = lStatement.executeQuery( lQuery );
         lResultSet.next();

         return lResultSet.getInt( "COUNT" );
      } catch ( SQLException e ) {
         e.printStackTrace();

         return 0;
      }
   }


   protected String getCurrentMxVersion() throws Exception {

      // setup query string
      String lCurrentVersionQuery = "SELECT short_version_name FROM utl_current_version";

      return getQueryResultWhere( lCurrentVersionQuery, "short_version_name" );
   }


   protected String getInventoryErrors() throws Exception {
      StringBuilder lQueryErrorList = new StringBuilder();
      lQueryErrorList.append(
            "SELECT distinct(error_cd), DL_REF_MESSAGE.SEVERITY_CD, DL_REF_MESSAGE.USER_DESC " );
      lQueryErrorList
            .append( "FROM AL_PROC_INVENTORY_ERROR JOIN DL_REF_MESSAGE ON error_cd = RESULT_CD " );
      lQueryErrorList.append( "WHERE error_cd IS NOT NULL" );

      ResultSet lQueryResults = runQuery( lQueryErrorList.toString() );

      String lResults = "";
      while ( lQueryResults.next() ) {

         lResults += "[" + lQueryResults.getString( "error_cd" );
         lResults += "-" + lQueryResults.getString( "SEVERITY_CD" );
         lResults += "-" + lQueryResults.getString( "USER_DESC" );
         lResults += "]";
         if ( !lQueryResults.isLast() ) {
            lResults += ", ";
         }
      }

      return lResults;
   }


   /**
    * Collects inventory record from database
    *
    * @param aSerialNo
    * @param aPartNoOEM
    *
    * @return Returns ResultSet for inventory
    *
    * @throws Exception
    */
   protected ResultSet getInventoryRecord( String aSerialNo, String aPartNoOEM ) throws Exception {
      StringBuilder lQueryInvInvRecord = new StringBuilder();

      lQueryInvInvRecord
            .append( "SELECT i.serial_no_oem, e.part_no_oem, i.inv_class_cd, e.manufact_cd, " );
      lQueryInvInvRecord.append(
            "l.loc_cd, i.inv_cond_cd, i.manufact_dt, i.received_dt, i.batch_no_oem, o.owner_cd, i.bin_qt, e.total_qt, i.reserve_qt " );
      lQueryInvInvRecord.append( "FROM inv_inv i " );
      lQueryInvInvRecord.append( "INNER JOIN eqp_part_no e " );
      lQueryInvInvRecord.append( "ON part_no_oem = " + aPartNoOEM + " " );
      lQueryInvInvRecord.append( "INNER JOIN inv_loc l " );
      lQueryInvInvRecord.append( "ON l.loc_db_id = i.loc_db_id and l.loc_id = i.loc_id " );
      lQueryInvInvRecord.append( "INNER JOIN inv_owner o " );
      lQueryInvInvRecord.append( "ON o.owner_db_id = i.owner_db_id and o.owner_id = i.owner_id " );
      lQueryInvInvRecord.append( "WHERE serial_no_oem = " + aSerialNo );

      ResultSet lQueryResultSetInvInvRecord = runQuery( lQueryInvInvRecord.toString() );

      return lQueryResultSetInvInvRecord;
   }


   /**
    * Runs the provided query on the database
    *
    * @param aQuery
    *           Query to be run
    * @param aColumnName
    *           Database column to be returned
    *
    * @return Returns the string query result
    *
    * @throws Exception
    */
   protected String getQueryResultWhere( String aQuery, String aColumnName ) throws Exception {

      String lQueryResult = "";

      PreparedStatement lStatement = getConnection().prepareStatement( aQuery,
            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );

      ResultSet lResultSet = lStatement.executeQuery( aQuery );
      do {
         lResultSet.next();
         if ( aColumnName.equals( "COUNT" ) ) {
            lQueryResult += ( lResultSet.getString( aColumnName ) );
         } else {
            lQueryResult += ( lResultSet.getString( aColumnName ) + "," );
         }
      } while ( !lResultSet.isLast() );

      return lQueryResult;
   }


   /**
    * Collects inventory record from database
    *
    * @param aSerialNo
    * @param aPartNoOEM
    * @param aParentSerialNo
    * @param aManufacturerCd
    *
    * @return Returns ResultSet for inventory
    *
    * @throws Exception
    */
   protected ResultSet getSubInventoryRecord( String aSerialNo, String aPartNoOEM,
         String aParentSerialNo, String aManufacturerCd ) throws Exception {
      StringBuilder lquery = new StringBuilder();

      lquery.append( "SELECT i.serial_no_oem, e.part_no_oem, i.inv_class_cd, e.manufact_cd, " );
      lquery.append(
            "l.loc_cd, i.inv_cond_cd, i.manufact_dt, i.received_dt, i.batch_no_oem, o.owner_cd, i.bin_qt, " );
      lquery.append( "ip.serial_no_oem AS PARENT_SERIAL_NO_OEM, " );
      lquery.append( "ep.part_no_oem AS PARENT_PART_NO_OEM, " );
      lquery.append( "ep.manufact_cd AS PARENT_MANUFACT_CD, " );
      lquery.append( "b.bom_part_cd " );
      lquery.append( "FROM inv_inv i " );
      lquery.append( "INNER JOIN eqp_part_no e " );
      lquery.append(
            "ON e.part_no_oem = " + aPartNoOEM + " AND e.manufact_cd = " + aManufacturerCd + " " );
      lquery.append( "INNER JOIN inv_loc l " );
      lquery.append( "ON l.loc_db_id = i.loc_db_id AND l.loc_id = i.loc_id " );
      lquery.append( "INNER JOIN inv_owner o " );
      lquery.append( "ON o.owner_db_id = i.owner_db_id AND o.owner_id = i.owner_id " );
      lquery.append( "INNER JOIN inv_inv ip " );
      lquery.append( "ON ip.inv_no_db_id = i.h_inv_no_db_id AND ip.inv_no_id = i.h_inv_no_id " );
      lquery.append( "INNER JOIN eqp_part_no ep " );
      lquery.append( "ON ep.part_no_db_id = ip.part_no_db_id AND ep.part_no_id = ip.part_no_id  " );
      lquery.append( "INNER JOIN eqp_bom_part b " );
      lquery.append( "ON b.bom_part_db_id = i.bom_part_db_id AND b.bom_part_id = i.bom_part_id " );
      lquery.append( "WHERE i.serial_no_oem = " + aSerialNo );
      lquery.append( "AND ip.serial_no_oem = " + aParentSerialNo );

      ResultSet lQueryResultSetInvInvRecord = runQuery( lquery.toString() );

      return lQueryResultSetInvInvRecord;
   }


   /**
    * Collects child current usage from the database
    *
    * @param aParentSerialNo
    * @param aParentPartNo
    * @param aParentManufacturerCd
    * @param aSerialNo
    * @param aPartNo
    * @param aManufacturerCd
    * @param aBomPartCd
    * @param aDataTypeCd
    *
    * @return Returns ResultSet for child current usage
    *
    * @throws Exception
    */
   protected ResultSet getChildCurrentUsage( String aParentSerialNo, String aParentPartNo,
         String aParentManufacturerCd, String aSerialNo, String aPartNo, String aManufacturerCd,
         String aBomPartCd, String aDataTypeCd ) throws Exception {
      StringBuilder lquery = new StringBuilder();

      lquery.append( "SELECT " );
      lquery.append( "parent_inv_inv.serial_no_oem AS parent_serial_no_oem, " );
      lquery.append( "parent_part.part_no_oem      AS parent_part_no_oem, " );
      lquery.append( "parent_part.manufact_cd      AS parent_manufact_cd, " );
      lquery.append( "inv_inv.serial_no_oem, " );
      lquery.append( "eqp_part_no.part_no_oem, " );
      lquery.append( "eqp_part_no.manufact_cd, " );
      lquery.append( "eqp_bom_part.bom_part_cd, " );
      lquery.append( "mim_data_type.data_type_cd, " );
      lquery.append( "inv_curr_usage.tsn_qt, " );
      lquery.append( "inv_curr_usage.tso_qt, " );
      lquery.append( "inv_curr_usage.tsi_qt " );
      lquery.append( "from " );
      lquery.append( "inv_curr_usage " );
      lquery.append( "inner join inv_inv ON " );
      lquery.append( "inv_curr_usage.inv_no_db_id = inv_inv.inv_no_db_id AND " );
      lquery.append( "inv_curr_usage.inv_no_id    = inv_inv.inv_no_id " );

      lquery.append( "inner join inv_inv parent_inv_inv ON " );
      lquery.append( "parent_inv_inv.inv_no_db_id = inv_inv.h_inv_no_db_id AND " );
      lquery.append( "parent_inv_inv.inv_no_id    = inv_inv.h_inv_no_id " );

      lquery.append( "inner join eqp_part_no ON " );
      lquery.append( "inv_inv.part_no_db_id = eqp_part_no.part_no_db_id AND " );
      lquery.append( "inv_inv.part_no_id    = eqp_part_no.part_no_id " );

      lquery.append( "inner join eqp_part_no parent_part ON " );
      lquery.append( "parent_inv_inv.part_no_db_id = parent_part.part_no_db_id AND " );
      lquery.append( "parent_inv_inv.part_no_id    = parent_part.part_no_id " );

      lquery.append( "inner join eqp_bom_part ON " );
      lquery.append( "inv_inv.bom_part_db_id = eqp_bom_part.bom_part_db_id AND " );
      lquery.append( "inv_inv.bom_part_id    = eqp_bom_part.bom_part_id " );

      lquery.append( "inner join mim_data_type ON " );
      lquery.append( "inv_curr_usage.data_type_db_id = mim_data_type.data_type_db_id AND " );
      lquery.append( "inv_curr_usage.data_type_id = mim_data_type.data_type_id " );

      lquery.append( "where " );
      lquery.append( "parent_inv_inv.serial_no_oem = " + aParentSerialNo + " AND " );
      lquery.append( "parent_part.part_no_oem = " + aParentPartNo + " AND " );
      lquery.append( "parent_part.manufact_cd = " + aParentManufacturerCd + " AND " );
      lquery.append( "eqp_part_no.part_no_oem = " + aPartNo + " AND " );
      lquery.append( "eqp_part_no.manufact_cd = " + aManufacturerCd + " AND " );
      lquery.append( "eqp_bom_part.bom_part_cd = " + aBomPartCd + " AND " );
      lquery.append( "mim_data_type.data_type_cd = " + aDataTypeCd );

      ResultSet lQueryResultChildUsage = runQuery( lquery.toString() );

      return lQueryResultChildUsage;
   }


   /**
    * load an Aircraft via the batch file
    *
    * @param aDataFileName
    *
    * @throws Exception
    */
   protected void loadAircraft( String aDataFileName ) throws Exception {
      BatchFileManager lMgr = new BatchFileManager();
      lMgr.loadAircraft( aDataFileName );
   }


   /**
    * load the CSV Data File
    *
    * @param aDataFileName
    *
    * @throws Exception
    */
   protected void loadCSVDataFile( String aDataFileName ) throws Exception {

      BatchFileManager lMgr = new BatchFileManager();
      lMgr.loadTasksViaDataFile( aDataFileName );
   }


   /**
    * Run import process and verify the results
    *
    * @throws Exception
    */
   protected void run_Import() throws Exception {

      BatchFileManager lMgr = new BatchFileManager();
      lMgr.importAircraft();
   }


   /**
    * Core test case logic for running inventory validation thru Actuals Loader, then call method
    * for verifying results of validation
    *
    * @param aTestName
    *           the test method name
    * @param aValidationCode
    *           Expected validation result
    *
    * @throws Exception
    *            if there is an error
    */
   protected void validateAndCheckInventory( String aTestName, String aValidationCode )
         throws Exception {
      System.out.println( "*** Starting validation for test " + aTestName + " ***" );

      // call inventory validation
      runALValidateInventory();

      checkInventory( aTestName, aValidationCode );
   }


   /**
    * Core test case logic for running inventory validation thru Actuals Loader, then call method
    * for verifying results of validation
    *
    * @param aTestName
    *           the test method name
    * @param aValidationCode
    *           Expected validation result
    *
    * @throws Exception
    *            if there is an error
    */
   protected void validateAndCheckInventory_NotComplete( String aTestName, String aValidationCode )
         throws Exception {
      System.out.println( "*** Starting validation for test " + aTestName + " ***" );

      // call inventory validation
      runALValidateInventory( false );

      checkInventory( aTestName, aValidationCode );
   }


   /**
    * Core test case logic for running inventory validation thru Actuals Loader, then call method
    * for verifying results of validation
    *
    * @param aTestName
    *           the test method name
    * @param aValidationCode
    *           Expected validation result
    *
    * @throws Exception
    *            if there is an error
    */
   protected void validateAndCheckInventoryNotIgnoreUSG( String aTestName, String aValidationCode )
         throws Exception {
      System.out.println( "*** Starting validation for test " + aTestName + " ***" );

      // call inventory validation
      runALValidateInventory();

      checkInventory( aTestName, aValidationCode, false );
   }


   /**
    * Core test case logic for validating historic tasks thru Actuals Loader
    *
    * @param aTestName
    *           the test method name
    * @param aValidationCode
    *           Validation error we're expecting to find. Passed along to checkTaskValidation
    *           method.
    *
    * @throws Exception
    *            if there is an error
    */
   protected void validateAndCheckTask( String aTestName, String aValidationCode )
         throws Exception {
      System.out.println( "*** Starting validation for test " + aTestName + " ***" );

      // call validation rule(s)
      runValidateTasks();

      checkTaskValidation( aValidationCode );
   }


   /**
    * Run validate process and verify the results
    *
    * @param aExpectedErrorCode
    *
    * @throws Exception
    */
   protected void verifyInventoryValidation( String aExpectedErrorCode ) throws Exception {

      Result lValidationResult = new Result();

      // check for and collect validation errors
      Result lResult_C_PROC_RI_INVENTORY =
            errorCheckInventory( "C_PROC_RI_INVENTORY", "serial_no_oem" );

      // System.out.println( "1: " + lResult_C_PROC_RI_INVENTORY.getMessage() );

      Result lResult_C_PROC_RI_INVENTORY_SUB =
            errorCheckInventory( "C_PROC_RI_INVENTORY_SUB", "serial_no_oem" );

      // System.out.println( "2: " + lResult_C_PROC_RI_INVENTORY_SUB.getMessage() );

      Result lResult_C_PROC_RI_INVENTORY_USAGE =
            errorCheckInventory( "C_PROC_RI_INVENTORY_USAGE", "serial_no_oem" );
      // System.out.println( "3: " + lResult_C_PROC_RI_INVENTORY_USAGE.getMessage() );

      Result lResult_C_PROC_RI_ATTACH =
            errorCheckInventory( "C_PROC_RI_ATTACH", "attach_serial_no_oem" );
      // System.out.println( "4: " + lResult_C_PROC_RI_ATTACH.getMessage() );

      Result lResult_AL_PROC_TASKS_ERROR = errorCheckTask();
      // System.out.println( "5: " + lResult_AL_PROC_TASKS_ERROR.getMessage() );

      Result lResult_AL_PROC_INVENTORY_ERROR = errorCheckInventory();

      // if we were not expecting validation errors and didn't find any - PASS
      if ( ( aExpectedErrorCode.equals( "PASS" ) ) && lResult_C_PROC_RI_INVENTORY.isSuccess()
            && lResult_C_PROC_RI_INVENTORY_SUB.isSuccess()
            && lResult_C_PROC_RI_INVENTORY_USAGE.isSuccess() && lResult_C_PROC_RI_ATTACH.isSuccess()
            && lResult_AL_PROC_TASKS_ERROR.isSuccess()
            && lResult_AL_PROC_INVENTORY_ERROR.isSuccess() ) {
         lValidationResult.success();
         System.out.println(
               "Validation successful. No validation errors expected and none were encountered." );
      } else {
         String lErrorResults = lResult_C_PROC_RI_INVENTORY.getMessage() + " "
               + lResult_C_PROC_RI_INVENTORY_SUB.getMessage() + " "
               + lResult_C_PROC_RI_INVENTORY_USAGE.getMessage() + " "
               + lResult_C_PROC_RI_ATTACH.getMessage() + " "
               + lResult_AL_PROC_TASKS_ERROR.getMessage() + " "
               + lResult_AL_PROC_INVENTORY_ERROR.getMessage();

         // if we were not expecting validation errors and found some - FAIL
         if ( ( aExpectedErrorCode == "PASS" ) && ( lErrorResults.length() > 5 ) ) {
            lValidationResult.failed();
            lValidationResult.setMessage( "Unexpected validation failure: " + lErrorResults );

            // if we were expecting validation errors and found it - PASS
         } else if ( ( aExpectedErrorCode != "PASS" )
               && ( lErrorResults.contains( aExpectedErrorCode ) ) ) {
            lValidationResult.success();
            lValidationResult
                  .setMessage( "Expected validation failure obtained: " + lErrorResults );

            // if expecting failure but didn't find expected failure code - FAIL
         } else if ( ( aExpectedErrorCode != "PASS" )
               && ( !lErrorResults.contains( aExpectedErrorCode ) ) ) {

            // no error found when one was expected
            if ( lErrorResults.length() <= 5 ) {
               lValidationResult.failed();
               lValidationResult.setMessage(
                     "Did not find any validation errors when the following error was expected: "
                           + aExpectedErrorCode );
               // wrong error was found
            } else {
               lValidationResult.failed();
               lValidationResult.setMessage( "Expected validation error: " + aExpectedErrorCode
                     + ". Instead found failure: " + lErrorResults );
            }
            // if expecting failure but validation passed - FAIL
         } else if ( ( aExpectedErrorCode != "PASS" ) && ( lErrorResults.length() <= 5 ) ) {
            lValidationResult.failed();
            lValidationResult.setMessage( "Did not find expected validation failure: "
                  + aExpectedErrorCode + ". No failures found. " + lErrorResults );
         }
      }

      // assert that validation batch file ran successfully and data passed validation
      assertThat( lValidationResult.getMessage(), lValidationResult.isSuccess() );
   }


   /**
    * Asserts that the contents of the map and result match and reports where they do not
    *
    * @param aResultSet
    * @param aMap
    * @param aColumnName
    *
    * @throws SQLException
    */
   protected void verifyRecordDetail( ResultSet aResultSet, Map<String, String> aMap,
         String aColumnName ) throws SQLException {

      // verify loc_cd
      String lObtainedDetail = aResultSet.getString( aColumnName );
      String lExpectedDetail = aMap.get( aColumnName ).replace( "'", "" );
      assertThat( "Detail for " + aColumnName + " was not as expected. Expected: " + lExpectedDetail
            + " Actual: " + lObtainedDetail, ( lObtainedDetail.equals( lExpectedDetail ) ) );
   }


   /**
    * Asserts that the value and result match and reports where they do not
    *
    * @param aResultSet
    * @param aMap
    * @param aColumnName
    *
    * @throws SQLException
    */
   protected void verifyRecordDetail( ResultSet aResultSet, String aValue, String aColumnName )
         throws SQLException {

      // verify loc_cd
      String lObtainedDetail = aResultSet.getString( aColumnName );
      lObtainedDetail = StringUtils.isBlank( lObtainedDetail ) ? " is null" : lObtainedDetail;
      String lExpectedDetail = StringUtils.isBlank( aValue ) ? " is null" : aValue;
      assertThat( "Detail for " + aColumnName + " was not as expected. Expected: " + lExpectedDetail
            + " Actual: " + lObtainedDetail, ( lObtainedDetail.equals( lExpectedDetail ) ) );
   }


   private Result getError( String aTableName, String aKeyColumn ) throws Exception {

      Result lErrorCheckResult = new Result();

      lErrorCheckResult.failed();
      lErrorCheckResult
            .setMessage( "Validation failed. At least one row did not pass validation. " );

      String lErrorCodeList = getErrorCodeFromProcTable( aTableName, aKeyColumn );

      // overwrite result message with better data if we can
      lErrorCheckResult.setMessage( "Validation failed: " + lErrorCodeList );

      return lErrorCheckResult;
   }


   private String getErrorCodeFromProcTable( String aTableName, String aKeyColumn )
         throws SQLException {

      String lInvResultCd = aTableName + ".result_cd";
      String lMessageResultCd = TableUtil.DL_REF_MESSAGE + ".result_cd";

      StringBuilder lQueryErrorList = new StringBuilder();

      lQueryErrorList
            .append( "SELECT distinct( " + lInvResultCd + " ), tech_desc, " + aKeyColumn + " " );
      lQueryErrorList.append( "FROM " + aTableName + " JOIN " + TableUtil.DL_REF_MESSAGE + " ON "
            + lInvResultCd + " = " + lMessageResultCd + " " );

      lQueryErrorList.append( "WHERE " + lInvResultCd + " IS NOT NULL" );

      ResultSet lQueryResults = runQuery( lQueryErrorList.toString() );

      String lResults = "";
      while ( lQueryResults.next() ) {

         lResults += "[" + lQueryResults.getString( aKeyColumn );
         lResults += " - " + lQueryResults.getString( "result_cd" );
         lResults += " - " + lQueryResults.getString( "tech_desc" );
         lResults += "]";
         if ( !lQueryResults.isLast() ) {
            lResults += ',';
         }
      }

      return lResults;
   }


   private String getErrorCount( String aTableName ) throws Exception {

      // setup query string
      String lCountQuery = "select count(proc_status_cd) AS \"COUNT\" from " + aTableName
            + " where proc_status_cd != 'VALID'";;

      return getQueryResultWhere( lCountQuery, "COUNT" );
   }


   private PreparedStatement getInsertCRITaskStatement( String aSerialNoOem, String aPartNoOem,
         String aManufactureCd, String aTaskCd, Date aCompletionDate, Date aCompletionDueDate,
         Date aCustomStartDate, Date CustomDueDate ) throws SQLException {
      PreparedStatement lPreparedStatement =
            getConnection().prepareStatement( TableUtil.getInsertCRITaskString() );

      lPreparedStatement.setString( 1, aSerialNoOem );
      lPreparedStatement.setString( 2, aPartNoOem );
      lPreparedStatement.setString( 3, aManufactureCd );
      lPreparedStatement.setString( 4, aTaskCd );

      if ( aCompletionDate == null ) {
         lPreparedStatement.setNull( 5, Types.NULL );
      } else {
         lPreparedStatement.setDate( 5, aCompletionDate );
      }

      if ( aCompletionDueDate == null ) {
         lPreparedStatement.setNull( 6, Types.NULL );
      } else {
         lPreparedStatement.setDate( 6, new Date( System.currentTimeMillis() ) );
      }

      if ( aCustomStartDate == null ) {
         lPreparedStatement.setNull( 7, Types.NULL );
      } else {
         lPreparedStatement.setDate( 7, new Date( System.currentTimeMillis() ) );
      }

      if ( CustomDueDate == null ) {
         lPreparedStatement.setNull( 8, Types.NULL );
      } else {
         lPreparedStatement.setDate( 8, new Date( System.currentTimeMillis() ) );
      }

      return lPreparedStatement;
   }


   protected int getRandom() {
      return ( int ) ( ( Math.random() * 50000 ) + 1 );
   }


   /**
    * Calls checkODF error code
    *
    * @param aTestName
    *           the test method name
    * @param aValidationCode
    *           Expected validation result
    *
    * @throws Exception
    *            if there is an error
    */
   protected void checkODFErrorCode( String aTestName, String aValidationCode ) {

      boolean lFound = false;
      String lerror_desc = null;

      String lquery = TestConstants.AL_ODF_ERROR_CHECK;

      String[] iIds = { "RESULT_CD", "USER_DESC" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      List<ArrayList<String>> lresult = execute( lquery, lfields );

      String lerrorList = "";

      for ( int i = 0; i < lresult.size(); i++ ) {
         lerrorList =
               lerrorList + "[" + lresult.get( i ).get( 0 ) + ":" + lresult.get( i ).get( 1 ) + "]";
         if ( lresult.get( i ).get( 0 ).equalsIgnoreCase( aValidationCode ) ) {
            lerror_desc = lresult.get( i ).get( 1 );
            lFound = true;
            break;
         }

      }

      Assert.assertTrue(
            "The error code found- " + aValidationCode + " : " + lerror_desc + lerrorList, lFound );

   }


   /**
    * Calls checkODF error code
    *
    * @param aTestName
    *           the test method name
    * @param aValidationCode
    *           Expected validation result
    *
    * @throws Exception
    *            if there is an error
    */
   protected void checkODFErrorCodeDebug( String aTestName ) {

      boolean lFound = false;
      String lerror_desc = null;

      String lquery = TestConstants.AL_ODF_ERROR_CHECK;

      String[] iIds = { "REF_ERROR_CD", "REF_ERROR_DESC" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      List<ArrayList<String>> lresult = execute( lquery, lfields );

      String lerrorList = "";

      for ( int i = 0; i < lresult.size(); i++ ) {
         lerrorList =
               lerrorList + "[" + lresult.get( i ).get( 0 ) + ":" + lresult.get( i ).get( 1 ) + "]";
      }

      Assert.assertTrue( "The error code found- " + lerror_desc + lerrorList, true );

   }


   /**
    * This function is to retrieve event ID by giving task_cd and task status.
    *
    *
    *
    */
   public simpleIDs getEventIDs( String aStatus, String ataskCD ) {

      String leventsdesc = ataskCD + " (" + ataskCD + ")";
      String[] iIds = { "EVENT_DB_ID", "EVENT_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      String iQueryString =
            "SELECT EVENT_DB_ID, EVENT_ID FROM (SELECT * FROM evt_event WHERE event_status_cd = '"
                  + aStatus + "' and EVENT_SDESC='" + leventsdesc
                  + "' ORDER BY creation_dt DESC) WHERE ROWNUM = 1";

      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lIds;
   }


   /**
    * This function is to retrieve event ID.
    *
    *
    *
    */
   public simpleIDs getEventIDs_2( String aTask_Class_CD ) {

      String[] iIds = { "SCHED_DB_ID", "SCHED_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      String iQueryString =
            "SELECT SCHED_DB_ID, SCHED_ID FROM (SELECT * FROM sched_stask WHERE task_class_cd = '"
                  + aTask_Class_CD + "' and SCHED_DB_ID = 1 ORDER BY creation_dt DESC)";

      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lIds;
   }


   /**
    * This function is to retrieve event ID by giving task_cd and task status.
    *
    *
    *
    */
   public simpleIDs getEventIDs( String aStatus, String ataskCD, String ataskName ) {

      String leventsdesc = ataskCD + " (" + ataskName + ")";
      String[] iIds = { "EVENT_DB_ID", "EVENT_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      String iQueryString =
            "SELECT EVENT_DB_ID, EVENT_ID FROM (SELECT * FROM evt_event WHERE event_status_cd = '"
                  + aStatus + "' and EVENT_SDESC='" + leventsdesc
                  + "' ORDER BY creation_dt DESC) WHERE ROWNUM = 1";

      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lIds;
   }


   /**
    * This function is to retrieve current inventory value by giving serial no
    *
    *
    *
    */

   public int getCurrentUsage( String aSN, simpleIDs aDataTypeIds ) {

      simpleIDs linv_ids = getInvIDs( aSN );

      String[] iIds = { "TSN_QT" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "INV_NO_DB_ID", linv_ids.getNO_DB_ID() );
      lArgs.addArguments( "INV_NO_ID", linv_ids.getNO_ID() );
      lArgs.addArguments( "DATA_TYPE_DB_ID", aDataTypeIds.getNO_DB_ID() );
      lArgs.addArguments( "DATA_TYPE_ID", aDataTypeIds.getNO_ID() );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.INV_CURR_USAGE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      return Integer.parseInt( llists.get( 0 ).get( 0 ) );

   }


   /**
    * This function is to retrieve task ID by giving task_cd.
    *
    *
    *
    */

   public simpleIDs getTaskIDs( String ataskCD ) {

      String[] iIds = { "TASK_DB_ID", "TASK_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "TASK_CD", ataskCD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.TASK_TASK, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lIds;
   }


   /**
    * Run the task Validation using a direct call to the plsql using a prepared statement. This does
    * not call any batch files.
    *
    *
    */
   public void runValidateTasks() {

      Assert.assertTrue( StoreProcedureRunner.Task.runTasksParall( getConnection(), true, true,
            TestConstants.PARALLELISM_DEGREE_DEFAULT, TestConstants.CHUNKSIZE_DEFAULT,
            TestConstants.DATA_CD_DEFAULT, TestConstants.GATHER_STATS_DEFAULT,
            TestConstants.LOGGING_DEFAULT, TestConstants.AUTO_CREATE_TASK_BOOL_DEFAULT ) == 1 );
   }


   /**
    * Run the task Validation using a direct call to the plsql using a prepared statement. This does
    * not call any batch files.
    *
    *
    */
   public void runImportTasks() {

      Assert.assertTrue( StoreProcedureRunner.Task.runTasksParall( getConnection(), false, true,
            TestConstants.PARALLELISM_DEGREE_DEFAULT, TestConstants.CHUNKSIZE_DEFAULT,
            TestConstants.DATA_CD_DEFAULT, TestConstants.GATHER_STATS_DEFAULT,
            TestConstants.LOGGING_DEFAULT, TestConstants.AUTO_CREATE_TASK_BOOL_DEFAULT ) == 1 );
   }


   /**
    * Run the task Validation using a direct call to the plsql using a prepared statement. This does
    * not call any batch files.
    *
    *
    */
   public void runValidateTasks_MISSINGTASK() {

      Assert.assertTrue( StoreProcedureRunner.Task.runTasksParall( getConnection(), true, true,
            TestConstants.PARALLELISM_DEGREE_DEFAULT, TestConstants.CHUNKSIZE_DEFAULT,
            TestConstants.DATA_CD_DEFAULT, TestConstants.GATHER_STATS_DEFAULT,
            TestConstants.LOGGING_DEFAULT, 1 ) == 1 );
   }


   /**
    * Run the task Validation using a direct call to the plsql using a prepared statement. This does
    * not call any batch files.
    *
    *
    */
   public void runImportTasks_MISSINGTASK() {

      Assert.assertTrue( StoreProcedureRunner.Task.runTasksParall( getConnection(), false, true,
            TestConstants.PARALLELISM_DEGREE_DEFAULT, TestConstants.CHUNKSIZE_DEFAULT,
            TestConstants.DATA_CD_DEFAULT, TestConstants.GATHER_STATS_DEFAULT,
            TestConstants.LOGGING_DEFAULT, 1 ) == 1 );
   }


   /**
    * Run task import using a direct call to the plsql using a prepared statement. This does not
    * call any batch files.
    *
    * @param aCycleId
    * @param aGatherStats
    *
    * @throws SQLException
    */

   public void runImportTasks( String aCycleId, int aGatherStats ) throws SQLException {

      int lParallelism = 1; // disable parallelism
      int lLogging = 0;
      int lChunkSize = 10000;

      // create record in table AL_WF_CYCLE_LOG as if task validation has been executed successfully
      CallableStatement lWfCycleLogCall = getConnection().prepareCall(
            " BEGIN mx_al_ctrller_pkg.set_wf_log( airaw_cycle_id => ?, aiv_load_type_cd => 'TASK'"
                  + ", aiv_cycle_status_cd => 'VALIDATED', aidt_cycle_start_dt => SYSDATE, aidt_cycle_end_dt => SYSDATE"
                  + ", aiv_cycle_err_status_cd => NULL, aiv_cycle_err_msg => NULL); END; " );

      lWfCycleLogCall.setString( 1, aCycleId );
      lWfCycleLogCall.executeUpdate();

      commit();

      runImportTasks();
   }


   /**
    * This function is to check whether task get error by giving serial_no_oem
    *
    * @throws Exception
    *
    *
    *
    */
   @Override
   public void checkTaskValidation_EXCEPTWARNING( String aValidationCode ) {
      boolean lFound = false;
      String lerror_desc = null;
      String lerrorMsg = null;

      String lquery = TestConstants.AL_TASK_ERROR_WARNING_CHECK;

      String[] iIds = { "ERROR_CD", "USER_DESC" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      List<ArrayList<String>> lresult = execute( lquery, lfields );

      if ( aValidationCode.equalsIgnoreCase( "PASS" ) ) {
         // Assert.assertTrue( "There are some errors: ", lresult.size() == 0 );
         for ( int i = 0; i < lresult.size(); i++ ) {
            lerrorMsg = lerrorMsg + lresult.get( i ).get( 0 ) + ": " + lresult.get( i ).get( 1 );
         }

      } else if ( aValidationCode.equalsIgnoreCase( "debug" ) ) {
         for ( int i = 0; i < lresult.size(); i++ ) {
            lerrorMsg = lerrorMsg + lresult.get( i ).get( 0 ) + ": " + lresult.get( i ).get( 1 );
         }

         System.out.println( "====: " + lerrorMsg + " ==========" );

      } else {

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

   }


   /**
    * This function is to check whether task get error by giving serial_no_oem
    *
    * @throws Exception
    *
    *
    *
    */
   @Override
   public void checkTaskValidation_WITHWARNING( String aValidationCode ) {
      boolean lFound = false;
      String lerror_desc = null;
      String lerrorMsg = null;

      String lquery = TestConstants.AL_TASK_ERROR_CHECK;

      String[] iIds = { "ERROR_CD", "USER_DESC" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      List<ArrayList<String>> lresult = execute( lquery, lfields );

      if ( aValidationCode.equalsIgnoreCase( "PASS" ) ) {
         Assert.assertTrue( "There are some errors: ", lresult.size() == 0 );

      } else {

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

   }


   public void deleteAllTaskTables( simpleIDs aIDs ) {
      if ( aIDs != null ) {

         // delete evt_sched_dead
         String lStrDelete = "delete from " + TableUtil.EVT_SCHED_DEAD + "  where EVENT_DB_ID="
               + aIDs.getNO_DB_ID() + " and EVENT_ID=" + aIDs.getNO_ID();
         executeSQL( lStrDelete );

         // delete evt_inv_usage
         lStrDelete = "delete from " + TableUtil.EVT_INV_USAGE + "  where EVENT_DB_ID="
               + aIDs.getNO_DB_ID() + " and EVENT_ID=" + aIDs.getNO_ID();
         executeSQL( lStrDelete );

         // delete evt_stage
         lStrDelete = "delete from " + TableUtil.EVT_STAGE + "  where EVENT_DB_ID="
               + aIDs.getNO_DB_ID() + " and EVENT_ID=" + aIDs.getNO_ID();
         executeSQL( lStrDelete );

         // delete evt_inv
         lStrDelete = "delete from " + TableUtil.EVT_INV + "  where EVENT_DB_ID="
               + aIDs.getNO_DB_ID() + " and EVENT_ID=" + aIDs.getNO_ID();
         executeSQL( lStrDelete );

         // delete evt_event_rel
         lStrDelete = "delete from " + TableUtil.EVT_EVENT_REL + "  where EVENT_DB_ID="
               + aIDs.getNO_DB_ID() + " and EVENT_ID=" + aIDs.getNO_ID();
         executeSQL( lStrDelete );

         // delete sched_stask
         lStrDelete = "delete from " + TableUtil.SCHED_STASK + "  where SCHED_DB_ID="
               + aIDs.getNO_DB_ID() + " and SCHED_ID=" + aIDs.getNO_ID();
         executeSQL( lStrDelete );

         // delete evt_event
         lStrDelete = "delete from " + TableUtil.EVT_EVENT + "  where EVENT_DB_ID="
               + aIDs.getNO_DB_ID() + " and EVENT_ID=" + aIDs.getNO_ID();
         executeSQL( lStrDelete );

      }

   }


   /**
    * <<<<<<< HEAD Run the inventory Validation using a direct call to the plsql using a prepared
    * statement. This does not call any batch files.
    *
    *
    */
   public void runValidateInv( String aCompleteBool ) {

      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), true, true, aCompleteBool, TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );
   }


   /**
    * Run the inventory Validation using a direct call to the plsql using a prepared statement. This
    * does not call any batch files.
    *
    *
    */
   public void runIMPORTInv( String aCompleteBool ) {

      Assert.assertTrue( StoreProcedureRunner.Inventory.runInventoryValidationAndImport(
            getConnection(), false, true, aCompleteBool, TestConstants.PARALLELISM_DEGREE_DEFAULT,
            TestConstants.CHUNKSIZE_DEFAULT, TestConstants.GATHER_STATS_DEFAULT ) == 1 );
   }


   /**
    * This function is to add days by given strign of date
    *
    *
    *
    */
   public java.util.Date DateAddDays( java.util.Date aDate, int days ) {

      Calendar c = Calendar.getInstance();
      c.setTime( aDate );
      c.add( Calendar.DATE, days );
      return c.getTime();
   }


   /**
    * This function is to verify if the TaskCreatedEvent is published in the AXON_DOMAIN_EVENT_ENTRY
    * table
    *
    */
   protected void verifyTaskCreatedEventPublish() {

      StringBuilder strBuilder = new StringBuilder();
      strBuilder.append( "Select * from " + TableUtil.AXON_DOMAIN_EVENT_ENTRY_TABLE );
      strBuilder.append( " Where" );
      strBuilder.append( " payloadtype like" );
      strBuilder.append( " '%TaskCreatedEvent%'" );

      try {
         ResultSet result = runQuery( strBuilder.toString() );

         assertTrue( "Exception on publishing task created event.", result.next() );
      } catch ( SQLException e ) {
         e.printStackTrace();
      }

   }


   /**
    * This function is to verify if the TaskAssignedToWorkPackageEvent is published in the
    * AXON_DOMAIN_EVENT_ENTRY table
    *
    */
   protected void verifyTaskAssignedToWorkPackageEventPublish() {

      StringBuilder strBuilder = new StringBuilder();
      strBuilder.append( "Select * from " + TableUtil.AXON_DOMAIN_EVENT_ENTRY_TABLE );
      strBuilder.append( " Where" );
      strBuilder.append( " payloadtype like" );
      strBuilder.append( " '%TaskAssignedToWorkPackageEvent%'" );

      try {
         ResultSet result = runQuery( strBuilder.toString() );

         assertTrue( "Exception on publishing task assigned to work package event.",
               result.next() );
      } catch ( SQLException e ) {
         e.printStackTrace();
      }

   }


   /**
    * This function is to verify if the TaskDrivingDeadlineRescheduledEvent is published in the
    * AXON_DOMAIN_EVENT_ENTRY table
    *
    */
   protected void verifyTaskDrivingDeadlineRescheduledEventPublish() {

      try {
         StringBuilder strBuilder = new StringBuilder();
         strBuilder.append( "Select * from " + TableUtil.AXON_DOMAIN_EVENT_ENTRY_TABLE );
         strBuilder.append( " Where" );
         strBuilder.append( " payloadtype like" );
         strBuilder.append( " '%TaskDrivingDeadlineRescheduledEvent%'" );

         ResultSet result;

         result = runQuery( strBuilder.toString() );

         assertTrue( "Unfortunately, TaskDrivingDeadlineRescheduledEvent was not published",
               result.next() );

      } catch ( SQLException e ) {
         e.printStackTrace();
      }

   }

}
