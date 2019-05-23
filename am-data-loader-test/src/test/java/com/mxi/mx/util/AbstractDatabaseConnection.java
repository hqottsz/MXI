/*
 * Confidential, proprietary and/or trade secret information of Mxi Technologies, Ltd.
 *
 * Copyright 2000-2015 Mxi Technologies, Ltd. All Rights Reserved.
 *
 * Except as expressly provided by written license signed by a duly appointed officer of Mxi
 * Technologies, Ltd., any disclosure, distribution, reproduction, compilation, modification,
 * creation of derivative works and/or other use of the Mxi source code is strictly prohibited.
 * Inclusion of a copyright notice shall not be taken to indicate that the source code has been
 * published.
 */

package com.mxi.mx.util;

import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.csv.CSVRecord;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import com.mxi.mx.core.maint.plan.datamodels.AssmblBomID;
import com.mxi.mx.core.maint.plan.datamodels.bomPartPN;
import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;


/**
 * This is a collection of methods that provide common DB functionality such as opening a
 * connection, running a query etc ...
 */
public abstract class AbstractDatabaseConnection {

   protected static List<CSVRecord> iList;
   public static final int CONS_DB_ID = 4650;
   public static final String SUBASSY = "SUBASSY";
   public static final String INVCLASSASSY = "ASSY";


   /**
    * Creates a new {@linkplain AbstractDatabaseConnection} object.
    */
   public AbstractDatabaseConnection() {
      super();
   }


   /**
    * Clean up after each individual test
    *
    * @throws Exception
    */
   @After
   public void after() throws Exception {

      closeConnection();
   }


   /**
    * Setup before executing each individual test
    *
    * @throws Exception
    */
   @Before
   public void before() throws Exception {

      openConnection();

   }


   /**
    * Clears all actuals loader tables
    *
    * @throws SQLException
    */
   protected void clearActualsLoaderTables() throws SQLException {
      for ( String lTableName : TableUtil.ACTUALS_LOADER_TABLES ) {
         deleteAllFromTable( lTableName );
      }
   }


   /**
    * Clears all baseline loader tables
    *
    * @throws SQLException
    */
   protected void clearBaselineLoaderTables() throws SQLException {
      for ( String lTableName : TableUtil.BASELINE_LOADER_TABLES ) {
         deleteAllFromTable( lTableName );
      }
   }


   /**
    * Clears all baseline loader tables
    *
    * @throws SQLException
    */
   protected void clearBaselineLoaderTablesRegionD() throws SQLException {
      for ( String lTableName : TableUtil.BASELINE_LOADER_TABLES_REGION_D ) {
         deleteAllFromTable( lTableName );
      }
   }


   /**
    * Clears all maintenix tables which have been modified by tests All the tests records have been
    * specified with "AUTO%"
    *
    * @throws SQLException
    */
   protected void clearBaselineMTables( ArrayList<String> tables ) {
      try {
         for ( String aUpdateQuery : tables ) {
            PreparedStatement lStatement = getConnection().prepareStatement( aUpdateQuery );
            lStatement.executeUpdate( aUpdateQuery );
            commit();
         }
      } catch ( SQLException e ) {
         e.printStackTrace();
      }
   }


   /**
    * Runs query to delete all data from actuals loader and maintenix tables
    *
    * @throws SQLException
    */
   protected void clearMxTestData() throws SQLException {

      deleteFromTableWhere( TableUtil.TASK_TASK, "WHERE task_db_id = 1" );

      deleteFromTableWhere( TableUtil.TASK_DEFN, "WHERE task_defn_db_id = 1" );

      deleteFromTableWhere( TableUtil.SCHED_STASK, "WHERE sched_db_id = 1" );

      deleteFromTableWhere( TableUtil.TASK_SCHED_RULE, "WHERE task_db_id = 1" );

      deleteFromTableWhere( TableUtil.EVT_INV, "WHERE event_db_id = 1" );

      deleteFromTableWhere( TableUtil.EVT_EVENT, "WHERE event_db_id = 1" );
   }


   /**
    * Close the DB Connection
    *
    * @throws Exception
    */
   protected void closeConnection() throws Exception {
      DBConnectionManager.getInstance().closeConnection();
   }


   /**
    * Commit any work on this connection
    * -------------------------------------------------------------------------------------
    *
    * @throws SQLException
    */
   protected void commit() throws SQLException {
      getConnection().commit();
   }


   /**
    * Rollback any work on this connection
    * -------------------------------------------------------------------------------------
    *
    * @throws SQLException
    */
   protected void rollBack() throws SQLException {
      getConnection().rollback();
   }


   /**
    * Deletes data from specified table
    *
    * @param aTableName
    *           Table to be cleared
    *
    * @throws SQLException
    */
   protected void deleteAllFromTable( String aTableName ) throws SQLException {
      String lQuery = "delete from " + aTableName;
      try {
         PreparedStatement lStatement = getConnection().prepareStatement( lQuery );
         lStatement.executeUpdate( lQuery );
         commit();

      } catch ( SQLException e ) {
         e.printStackTrace();
      }
   }


   /**
    * Deletes from specified table with a where clause
    *
    * @param aTableName
    *           Table to be cleared
    * @param aWhere
    *           Where clause
    *
    * @throws SQLException
    */
   protected void deleteFromTableWhere( String aTableName, String aWhere ) throws SQLException {

      System.out.println( "Deleting: " + "DELETE FROM " + aTableName + " " + aWhere );
      runUpdate( "DELETE FROM " + aTableName + " " + aWhere );
   }


   /**
    * Runs query to delete all data from actuals loader and maintenix tables
    *
    * @param aEventDbId
    * @param aEventId
    *
    * @throws SQLException
    */
   protected void deleteMxTestData( int aEventDbId, int aEventId ) throws SQLException {

      // core mx tables
      deleteFromTableWhere( TableUtil.EVT_STAGE,
            "WHERE event_db_id = " + aEventDbId + " AND " + "event_id = " + aEventId );

      deleteFromTableWhere( TableUtil.EVT_INV,
            "WHERE event_db_id = " + aEventDbId + " AND " + "event_id = " + aEventId );

      deleteFromTableWhere( TableUtil.EVT_SCHED_DEAD,
            "WHERE event_db_id = " + aEventDbId + " AND " + "event_id = " + aEventId );

      deleteFromTableWhere( TableUtil.EVT_EVENT_REL,
            "WHERE event_db_id = " + aEventDbId + " AND " + "event_id = " + aEventId );

      deleteFromTableWhere( TableUtil.EVT_EVENT,
            "WHERE event_db_id = " + aEventDbId + " AND " + "event_id = " + aEventId );

      deleteFromTableWhere( TableUtil.SCHED_STASK,
            "WHERE sched_db_id = " + aEventDbId + " AND " + "sched_id = " + aEventId );
   }


   /**
    * Execute the Statement provided. As well, it will perform a commit.
    *
    * @param lStatement
    *
    * @throws SQLException
    */
   protected void executeStatement( PreparedStatement lStatement ) throws SQLException {
      lStatement.execute();

      commit();
   }


   /**
    * Returns the value of the connection property.
    *
    * @return the value of the connection property
    */
   protected Connection getConnection() {
      return DBConnectionManager.getInstance().getConnection();
   }


   protected ResultSet getEventData( int lEventDbId, int lEventId ) throws SQLException {
      StringBuilder lEvtQuery = new StringBuilder();
      lEvtQuery.append( "SELECT * FROM " );
      lEvtQuery.append( TableUtil.EVT_EVENT );
      lEvtQuery.append( " WHERE " );
      lEvtQuery.append( "event_db_id = " + lEventDbId );
      lEvtQuery.append( " AND " );
      lEvtQuery.append( "event_id = " + lEventId );

      return runQuery( lEvtQuery.toString() );
   }


   protected ResultSet getEventRelData( int lEventDbId, int lEventId ) throws SQLException {
      StringBuilder lEvtRelQuery = new StringBuilder();
      lEvtRelQuery.append( "SELECT * FROM " );
      lEvtRelQuery.append( TableUtil.EVT_EVENT_REL );
      lEvtRelQuery.append( " WHERE " );
      lEvtRelQuery.append( "event_db_id = " + lEventDbId );
      lEvtRelQuery.append( " AND " );
      lEvtRelQuery.append( "event_id = " + lEventId );

      return runQuery( lEvtRelQuery.toString() );
   }


   protected ResultSet getSchedDeadData( int lEventDbId, int lEventId ) throws SQLException {
      StringBuilder lEvtSchedDeadQuery = new StringBuilder();
      lEvtSchedDeadQuery.append( " SELECT * FROM " );
      lEvtSchedDeadQuery.append( TableUtil.EVT_SCHED_DEAD );
      lEvtSchedDeadQuery.append( " WHERE " );
      lEvtSchedDeadQuery.append( "event_db_id = " + lEventDbId );
      lEvtSchedDeadQuery.append( " AND " );
      lEvtSchedDeadQuery.append( "event_id = " + lEventId );

      return runQuery( lEvtSchedDeadQuery.toString() );
   }


   protected ResultSet getTaskDefnData( int lTaskDefnDbId, int lTaskDefnId ) throws SQLException {
      StringBuilder lEvtSchedDeadQuery = new StringBuilder();
      lEvtSchedDeadQuery.append( " SELECT * FROM " );
      lEvtSchedDeadQuery.append( TableUtil.TASK_DEFN );
      lEvtSchedDeadQuery.append( " WHERE " );
      lEvtSchedDeadQuery.append( "task_defn_db_id = " + lTaskDefnDbId );
      lEvtSchedDeadQuery.append( " AND " );
      lEvtSchedDeadQuery.append( "task_defn_id = " + lTaskDefnId );

      return runQuery( lEvtSchedDeadQuery.toString() );
   }


   // returns the task id from the database using the task code
   protected String getTaskIdByTaskCode( String aTaskCode ) throws SQLException {

      // Query for checking task was assigned to exactly one parent on the same aircraft
      StringBuilder lQuery = new StringBuilder();
      lQuery.append( "SELECT task_id FROM task_task tt " );
      lQuery.append( "WHERE tt.task_cd = '" + aTaskCode + "'" );

      ResultSet lQueryResultSet = runQuery( lQuery.toString() );

      lQueryResultSet.next();
      return lQueryResultSet.getString( "task_id" );

   }


   // returns the task id from the database using the task code
   protected String getTaskDefnIdByTaskCode( String aTaskCode ) throws SQLException {

      // Query for checking task was assigned to exactly one parent on the same aircraft
      StringBuilder lQuery = new StringBuilder();
      lQuery.append( "SELECT task_defn_id FROM task_task tt " );
      lQuery.append( "WHERE tt.task_cd = '" + aTaskCode + "'" );

      ResultSet lQueryResultSet = runQuery( lQuery.toString() );

      lQueryResultSet.next();
      return lQueryResultSet.getString( "task_defn_id" );

   }


   // returns the inv id from the database using the inv_no_sdesc
   protected String getInvIdByInvNoSdesc( String aACInvNoSdesc, String aInvNoSdesc )
         throws SQLException {

      // Query for checking task was assigned to exactly one parent on the same aircraft
      StringBuilder lQuery = new StringBuilder();
      lQuery.append( "SELECT ii.inv_no_id FROM inv_inv ii " );
      lQuery.append(
            "INNER JOIN inv_inv hii ON ii.h_inv_no_db_id = hii.inv_no_db_id AND ii.h_inv_no_id = hii.inv_no_id " );
      lQuery.append( "WHERE ii.inv_no_sdesc = '" + aInvNoSdesc + "' AND hii.inv_no_sdesc = '"
            + aACInvNoSdesc + "'" );

      ResultSet lQueryResultSet = runQuery( lQuery.toString() );

      lQueryResultSet.next();
      return lQueryResultSet.getString( "inv_no_id" );

   }


   /**
    * Opens database connection
    *
    * @throws Exception
    */
   protected void openConnection() throws Exception {

      DBConnectionManager.getInstance().openConnection();
   }


   /**
    * Run the task Validation using a direct call to the plsql using a prepared statement. This does
    * not call any batch files. This will attempt to complete assemblies (fill non provided inv with
    * inventory having a serial number of 'xxx')
    *
    * @throws SQLException
    */
   protected void runALValidateInventory() throws SQLException {

      runALValidateInventory( true );
   }


   /**
    * Run the task Validation using a direct call to the plsql using a prepared statement. This does
    * not call any batch files.
    *
    * @param aCompleteAssmblyBool
    *           aCompleteAssmblyBool, When set to true, Actuals Loader generates data to fill gaps
    *           (holes) in any assembly on which mandatory tracked inventory items are not
    *           identified. The standard part number of the configuration slot is used, and the
    *           serial number is set to 'XXX'. Set to false to leave any holes for missing mandatory
    *           inventory unfilled.
    *
    * @throws SQLException
    */
   protected void runALValidateInventory( boolean aCompleteAssmblyBool ) throws SQLException {

      CallableStatement lPrepareCallInventory = getConnection().prepareCall(
            "BEGIN mx_al_ctrller_pkg.execute_inv_validation (aiv_complete_assy_bool => ? ,aon_retcode => ?, aov_retmsg => ?); END;" );

      if ( aCompleteAssmblyBool ) {
         lPrepareCallInventory.setString( 1, "FULL" );
      } else {
         lPrepareCallInventory.setString( 1, "NOT" );
      }

      lPrepareCallInventory.registerOutParameter( 2, Types.INTEGER );
      lPrepareCallInventory.registerOutParameter( 3, Types.VARCHAR );

      lPrepareCallInventory.execute();
   }


   /**
    * Run the task Validation using a direct call to the plsql using a prepared statement. This does
    * not call any batch files.
    *
    * @throws SQLException
    */
   protected void runImportInventory() throws SQLException {

      // CallableStatement lPrepareCallKeys = getConnection() .prepareCall(
      // " BEGIN ? := mx_al_inventory_pkg.populate_primary_keys( araw_cycle_log_id => ? ); END; "
      // );

      // lPrepareCallKeys.setString( 1, "0001" );

      // lPrepareCallKeys.executeUpdate(); commit();

      CallableStatement lPrepareCallInventory = getConnection().prepareCall(
            "BEGIN mx_al_ctrller_pkg.execute_inv_import (aon_retcode => ?, aov_retmsg => ?); END;" );
      lPrepareCallInventory.registerOutParameter( 1, Types.INTEGER );
      lPrepareCallInventory.registerOutParameter( 2, Types.VARCHAR );

      lPrepareCallInventory.execute();
   }


   /**
    * Run inventory staging using a direct call to the plsql using a prepared statement. This does
    * not call any batch files.
    *
    * @throws SQLException
    */
   protected void runImportStageInventory() throws SQLException {

      CallableStatement lPrepareCallStageInventory = getConnection()
            .prepareCall( "BEGIN al_stage_inventory (" + "ain_available_thread => ?, "
                  + "ain_acft_limit_per_session => ?, " + "ain_comp_limit_per_session => ?, "
                  + "aiv_complete_assy_bool => ?" + "); END;" );

      lPrepareCallStageInventory.setInt( 1, 4 );
      lPrepareCallStageInventory.setInt( 2, 4 );
      lPrepareCallStageInventory.setInt( 3, 1000 );
      lPrepareCallStageInventory.setObject( 4, 0, Types.VARCHAR );

      lPrepareCallStageInventory.execute();

      commit();
   }


   /**
    * Run inventory staging using a direct call to the plsql using a prepared statement. This does
    * not call any batch files.
    *
    * @param aCompleteAssmblyBool
    *           DOCUMENT_ME
    *
    * @throws SQLException
    */
   protected void runImportStageInventory( boolean aCompleteAssmblyBool ) throws SQLException {

      CallableStatement lStageInventory = getConnection().prepareCall(
            "BEGIN mx_al_ctrller_pkg.execute_inv_import(aiv_complete_assy_bool => ?, aon_retcode => ?, aov_retmsg => ?); END;" );

      if ( aCompleteAssmblyBool ) {
         lStageInventory.setString( 1, "FULL" );
      } else {
         lStageInventory.setString( 1, "NOT" );
      }

      lStageInventory.registerOutParameter( 2, Types.INTEGER );
      lStageInventory.registerOutParameter( 3, Types.VARCHAR );

      lStageInventory.execute();

      commit();
      // int lResult = lStageInventory.getInt( 1 );
      // String lResultmsg = lStageInventory.getString( 2 );
      //
      // if ( lResult != 1 )
      // System.out.println( "Import failed, the result value is: " + lResult + ", Error Message: "
      // + lResultmsg );

   }


   /**
    * Runs the provided insert on the database
    *
    * @param aInsert
    *           Insert to be run
    *
    * @return Returns the number of rows inserted in the database
    *
    * @throws SQLException
    */
   protected Integer runInsert( String aInsert ) {

      Integer lResultCount = 0;

      System.out.println( "Inserting: " + aInsert );

      try {
         PreparedStatement lStatement = getConnection().prepareStatement( aInsert );

         lResultCount = lStatement.executeUpdate( aInsert );

         commit();
      } catch ( SQLException e ) {
         e.printStackTrace();
         Assert.assertTrue( "Insertion Failed", false );

      }

      return lResultCount;
   }


   /**
    * Runs the provided query on the database
    *
    * @param aQuery
    *           Query to be run
    *
    * @return Returns the string query result
    *
    * @throws SQLException
    */
   protected ResultSet runQuery( String aQuery ) throws SQLException {

      System.out.println( "Executing Query: " + aQuery );

      PreparedStatement lStatement = getConnection().prepareStatement( aQuery,
            ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY );

      ResultSet lResultSet = lStatement.executeQuery( aQuery );

      return lResultSet;
   }


   /**
    * Runs the provided query on the database
    *
    * @param aTable
    *           to query on
    *
    * @return the number of rows in the table
    *
    *
    */
   protected int countRowsInEntireTable( String aTable ) {

      String lQuery = "SELECT COUNT(*) FROM " + aTable;

      PreparedStatement lStatement;
      ResultSet lResultSet = null;
      int lRowCount = 0;

      try {
         lStatement = getConnection().prepareStatement( lQuery, ResultSet.TYPE_SCROLL_SENSITIVE,
               ResultSet.CONCUR_READ_ONLY );

         lResultSet = lStatement.executeQuery( lQuery );
         lResultSet.next();
         lRowCount = lResultSet.getInt( "COUNT(*)" );
      } catch ( SQLException e ) {
         e.printStackTrace();
      }

      return lRowCount;
   }


   /**
    * Runs the provided query on the database Note: the "From" must be lowercase
    *
    * @param aQuery
    *           Query to be run
    *
    * @return The number of rows
    *
    *
    */

   protected int countRowsOfQuery( String aQuery ) {

      String lQuery = aQuery.replaceAll( ".*from", "Select Count(*) from" ); // all text before from
      PreparedStatement lStatement;
      ResultSet lResultSet = null;
      int lRtn = 0;
      try {
         lStatement = getConnection().prepareStatement( lQuery, ResultSet.TYPE_SCROLL_SENSITIVE,
               ResultSet.CONCUR_READ_ONLY );

         lResultSet = lStatement.executeQuery( lQuery );
         lResultSet.next();
         lRtn = lResultSet.getInt( "COUNT(*)" );
      } catch ( SQLException e ) {
         e.printStackTrace();
         Assert.assertTrue( "Invalid Count Query: " + aQuery, false );
      }

      return lRtn;
   }


   /**
    * Runs the provided query on the database
    *
    * @param aTable
    *           The table that will be queried
    * @param aWhereClause
    *           The where clause within the query, (Table will be filtered this way)
    *
    * @return Returns the string query result
    *
    * @throws SQLException
    */
   protected int countRowsBasedOnWhereClause( String aTable, String aWhereClause )
         throws SQLException {

      String lQuery = "SELECT COUNT(*) FROM " + aTable + " " + aWhereClause;

      PreparedStatement lStatement = getConnection().prepareStatement( lQuery,
            ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY );

      ResultSet lResultSet = lStatement.executeQuery( lQuery );
      lResultSet.next();

      return lResultSet.getInt( "COUNT(*)" );
   }


   /**
    * Runs the provided query on the database
    *
    * @param aQuery
    *           Query to be run
    *
    * @return Returns true if there is only one record, else false if there is no result or +2
    *         records
    *
    *
    * @throws SQLException
    */
   protected boolean runQueryReturnsOneRow( String aQuery ) {

      System.out.println( "Executing Query: " + aQuery );

      boolean lResult = false;

      PreparedStatement lStatement;
      try {
         lStatement = getConnection().prepareStatement( aQuery, ResultSet.TYPE_SCROLL_SENSITIVE,
               ResultSet.CONCUR_READ_ONLY );

         ResultSet lResultSet = lStatement.executeQuery( aQuery );
         lResultSet.next();
         lResult = lResultSet.isLast();
      } catch ( SQLException e ) {
         e.printStackTrace();
      }

      return lResult;

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
    */
   protected String runQuery( String aQuery, String aColumnName ) {

      String lQueryResult = "";

      System.out.println( "Executing Query: " + aQuery );

      try {
         PreparedStatement lStatement = getConnection().prepareStatement( aQuery,
               ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );

         ResultSet lResultSet = lStatement.executeQuery( aQuery );

         do {
            lResultSet.next();
            lQueryResult += lResultSet.getString( aColumnName ) + ",";
         } while ( !lResultSet.isLast() );
      } catch ( SQLException e ) {
         e.printStackTrace();
         lQueryResult = "ERROR - " + e.getMessage();
      }

      return lQueryResult;
   }


   /**
    * Executes query
    *
    * @param aUpdateQuery
    *           (@link String)
    *
    * @throws SQLException
    */
   protected void runUpdate( String aUpdateQuery ) {
      try {
         PreparedStatement lStatement = getConnection().prepareStatement( aUpdateQuery );
         lStatement.executeUpdate( aUpdateQuery );
         commit();
      } catch ( SQLException e ) {
         e.printStackTrace();
      }

   }


   /**
    * Run the task Validation using a direct call to the plsql using a prepared statement. This does
    * not call any batch files.
    *
    * @throws SQLException
    */
   protected void runValidateMaintenixData() throws SQLException {

      CallableStatement lPrepareCallInventory = getConnection()
            .prepareCall( "begin c_ri_proc_pkg.validate_maintenix_data; commit; end;" );

      lPrepareCallInventory.execute();

      commit();
   }


   /**
    * Run the task Validation using a direct call to the plsql using a prepared statement. This does
    * not call any batch files.
    *
    * @throws SQLException
    */
   protected void runValidateSubInventory() throws SQLException {

      CallableStatement lPrepareCallInventory = getConnection()
            .prepareCall( "begin c_ri_proc_pkg.validate_sub_inventory; commit; end;" );

      lPrepareCallInventory.execute();

      commit();
   }


   /**
    * Run the task Validation using a direct call to the plsql using a prepared statement. This does
    * not call any batch files.
    *
    * @throws SQLException
    */
   protected void runValidateTemplateInventory() throws SQLException {

      CallableStatement lPrepareCallInventory = getConnection().prepareCall(
            "BEGIN mx_al_ctrller_pkg.execute_inv_validation (aiv_complete_assy_bool => ?, ain_gather_stats_bool=> 0, aon_retcode => ?, aov_retmsg => ? ); END;" );

      lPrepareCallInventory.registerOutParameter( 1, Types.INTEGER );
      lPrepareCallInventory.registerOutParameter( 2, Types.VARCHAR );

      lPrepareCallInventory.execute();
   }


   /**
    * Run the task Validation using a direct call to the plsql using a prepared statement. This does
    * not call any batch files.
    *
    * @throws SQLException
    */
   protected void runValidateTemplateTasks() throws SQLException {

      CallableStatement lPrepareCallTask = getConnection().prepareCall(
            "BEGIN mx_al_ctrller_pkg.execute_task_validation (ain_gather_stats_bool => ?, aon_retcode => ?, aov_retmsg => ?); END;" );

      lPrepareCallTask.setInt( 1, 0 );
      lPrepareCallTask.registerOutParameter( 2, Types.INTEGER );
      lPrepareCallTask.registerOutParameter( 3, Types.VARCHAR );

      lPrepareCallTask.execute();
   }


   /**
    * Check 2 arrays of object are equal.
    *
    * @Parametes: Array, Array
    *
    * @return void
    */

   public void checkArraysEqual( ArrayList aArray, ArrayList bArray ) {
      Assert.assertTrue( aArray.size() == bArray.size() );
      boolean go = false;
      int count = aArray.size();

      for ( int i = 0; i < count; i++ ) {
         for ( int j = 0; j < count; j++ ) {
            if ( aArray.get( i ).equals( bArray.get( j ) ) ) {
               go = false;
               break;
            } else {
               go = true;

            }
         }
         Assert.assertFalse( go );
      }

   }


   /**
    * Get first int value from query
    *
    * @return int
    */

   public int getIntValueFromQuery( String strQuery, String aColumn ) {

      ResultSet ResultSetRecords;
      int lReturn = -1;
      try {
         ResultSetRecords = runQuery( strQuery );
         if ( ResultSetRecords.next() ) {
            lReturn = Integer.parseInt( ResultSetRecords.getString( aColumn ) );
         }
      } catch ( SQLException e ) {
         e.printStackTrace();
      }
      return lReturn;
   }


   /**
    *
    * This will call the sequence in the database and will generate a new Id.
    *
    * @param aSequence
    *           - Name of the sequence in the Database
    * @return lId - new Id generated
    */

   protected int getNextValueInSequence( String aSequence ) {
      String lQuery = "Select " + aSequence + ".NEXTVAL from dual";
      int lId = 0;

      try {
         PreparedStatement lStatement = getConnection().prepareStatement( lQuery,
               ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY );
         ResultSet lResultSet = lStatement.executeQuery( lQuery );

         lResultSet.next();
         lId = lResultSet.getInt( "NEXTVAL" );
      } catch ( SQLException e ) {
         e.printStackTrace();
      }
      return lId;

   }


   /**
    * Get first string value from query
    *
    * @return string
    */

   public String getStringValueFromQuery( String strQuery, String strColum ) {

      ResultSet ResultSetRecords;
      String strReturn = null;
      try {
         ResultSetRecords = runQuery( strQuery );
         if ( ResultSetRecords.next() ) {
            strReturn = ResultSetRecords.getString( strColum );
         }
      } catch ( SQLException e ) {
         e.printStackTrace();
         Assert.assertTrue( "getStringValueFromQuery", false );
      }
      return strReturn;
   }


   /**
    * Execute the query.
    */
   public List<ArrayList<String>> execute( String aStrQuery, List<String> lfields ) {

      PreparedStatement lStatement;
      List<ArrayList<String>> louter = new ArrayList<ArrayList<String>>();

      try {
         lStatement = getConnection().prepareStatement( aStrQuery, ResultSet.TYPE_SCROLL_SENSITIVE,
               ResultSet.CONCUR_READ_ONLY );

         ResultSet lResultSet = lStatement.executeQuery( aStrQuery );
         while ( lResultSet.next() ) {
            List<String> iList = new ArrayList<String>();
            for ( int i = 0; i < lfields.size(); i++ ) {
               iList.add( lResultSet.getString( lfields.get( i ) ) );

            }
            louter.add( ( ArrayList<String> ) iList );

         }

      } catch ( SQLException e ) {
         e.printStackTrace();
      }

      return louter;

   }


   /**
    * Execute the query for date.
    */
   public List<ArrayList<java.sql.Date>> execute_date( String aStrQuery, List<String> lfields ) {

      PreparedStatement lStatement;
      List<ArrayList<java.sql.Date>> louter = new ArrayList<ArrayList<java.sql.Date>>();

      try {
         lStatement = getConnection().prepareStatement( aStrQuery, ResultSet.TYPE_SCROLL_SENSITIVE,
               ResultSet.CONCUR_READ_ONLY );

         ResultSet lResultSet = lStatement.executeQuery( aStrQuery );
         while ( lResultSet.next() ) {
            List<java.sql.Date> iList = new ArrayList<java.sql.Date>();
            for ( int i = 0; i < lfields.size(); i++ ) {
               iList.add( lResultSet.getDate( lfields.get( i ) ) );

            }
            louter.add( ( ArrayList<java.sql.Date> ) iList );

         }

      } catch ( SQLException e ) {
         e.printStackTrace();
      }

      return louter;

   }


   /**
    * Execute the query for get IDs.
    */
   public ArrayList<simpleIDs> execute2( String aStrQuery, List<String> lfields ) {

      PreparedStatement lStatement;
      ArrayList<simpleIDs> louter = new ArrayList<simpleIDs>();

      try {
         lStatement = getConnection().prepareStatement( aStrQuery, ResultSet.TYPE_SCROLL_SENSITIVE,
               ResultSet.CONCUR_READ_ONLY );

         ResultSet lResultSet = lStatement.executeQuery( aStrQuery );
         while ( lResultSet.next() ) {
            simpleIDs mycurrentId = new simpleIDs( lResultSet.getString( lfields.get( 0 ) ),
                  lResultSet.getString( lfields.get( 1 ) ) );
            louter.add( mycurrentId );
         }

      } catch ( SQLException e ) {
         e.printStackTrace();
      }

      return louter;

   }


   /**
    * Check whether record(s) exist in the table.
    *
    * @return boolean
    */

   public boolean RecordsExist( String strQuery ) {

      ResultSet lResultSetRecords = null;
      boolean lreturnbool = false;
      try {
         lResultSetRecords = runQuery( strQuery );
         if ( lResultSetRecords == null || lResultSetRecords.first() == false ) {
            lreturnbool = false;
         } else {
            lreturnbool = true;
         }

      } catch ( SQLException e ) {
         e.printStackTrace();
         Assert.assertTrue( "Catch an error in RecordsExist()", false );
      }
      return lreturnbool;
   }


   /**
    * Run all the SQL statements which are saved in the table
    *
    *
    * @throws SQLException
    */
   public void runSqlsInTable( ArrayList<String> tables ) {
      try {
         for ( String aUpdateQuery : tables ) {
            PreparedStatement lStatement = getConnection().prepareStatement( aUpdateQuery );
            lStatement.executeUpdate( aUpdateQuery );
            commit();
         }
      } catch ( SQLException e ) {
         e.printStackTrace();
      }
   }


   /**
    * Get first string value from query
    *
    * @return string
    */

   public java.sql.Date getDateValueFromQuery( String strQuery, String strColum ) {

      ResultSet ResultSetRecords;
      java.sql.Date lReturn = null;
      try {
         ResultSetRecords = runQuery( strQuery );
         if ( ResultSetRecords.next() ) {
            lReturn = ResultSetRecords.getDate( strColum );
         }
      } catch ( SQLException e ) {
         e.printStackTrace();
      }
      return lReturn;
   }


   /**
    * check date difference between date1 and date2
    *
    * @return: day(s) of date difference
    */
   public long DateDiffInDays( java.util.Date d1, java.util.Date d2 ) {

      long diffDays = 0;

      ResultSet ResultSetRecords;

      String Strquery = "SELECT TO_DATE('" + d2 + "', 'YYYY-MM-DD') - TO_DATE('" + d1
            + "', 'YYYY-MM-DD') AS DateDiff FROM dual";

      try {
         ResultSetRecords = runQuery( Strquery.toString() );
         ResultSetRecords.next();
         diffDays = ResultSetRecords.getLong( "DateDiff" );
      } catch ( SQLException e ) {
         e.printStackTrace();

      }

      return diffDays;

   }


   /**
    * Execute any SQL string
    *
    * @return void
    */

   public void executeSQL( String strSQL ) {
      PreparedStatement lStatement;
      try {
         lStatement = getConnection().prepareStatement( strSQL );
         lStatement.executeUpdate( strSQL );
         commit();
      } catch ( SQLException e ) {
         e.printStackTrace();
      }

   }


   /**
    * Execute the query to setup data for each test in the class
    *
    */
   public void classDataSetup( ArrayList<String> tables ) {
      try {
         for ( String aUpdateQuery : tables ) {
            PreparedStatement lStatement = getConnection().prepareStatement( aUpdateQuery );
            lStatement.executeUpdate( aUpdateQuery );
            commit();
         }
      } catch ( SQLException e ) {
         e.printStackTrace();
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
   public boolean checkTaskValidation_cap( String aSN ) {
      String lrecordId = getRecordID( aSN );
      String lquery =
            "Select ERROR_CD from al_proc_tasks_error where record_id='" + lrecordId + "'";
      String lresult = getStringValueFromQuery( lquery, "ERROR_CD" );

      if ( !StringUtils.isBlank( lresult ) ) {
         Assert.assertTrue( "There should not have error:" + getTaskErrors(), false );
      }

      return StringUtils.isBlank( lresult );

   }


   /**
    * This function is to check whether task get error by giving serial_no_oem
    *
    * @throws Exception
    *
    *
    *
    */
   public void checkTaskValidation_EXCEPTWARNING( String aValidationCode ) {
      boolean lFound = false;
      String lerror_desc = null;
      String lerrorMsg = null;

      String lquery = TestConstants.AL_TASK_ERROR_WARNING_CHECK;

      String[] iIds = { "ERROR_CD", "REF_ERROR_DESC" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      List<ArrayList<String>> lresult = execute( lquery, lfields );

      if ( aValidationCode.equalsIgnoreCase( "PASS" ) ) {
         Assert.assertTrue( "NO ERROR", lresult.size() == 0 );

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
   public void checkInventoryValidation( String aValidationCode ) {
      boolean lFound = false;
      String lerror_desc = null;
      String lerrorMsg = null;

      String lquery = TestConstants.AL_INVENTORY_ERROR_CHECK;

      String[] iIds = { "ERROR_CD", "REF_ERROR_DESC" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      List<ArrayList<String>> lresult = execute( lquery, lfields );

      if ( aValidationCode.equalsIgnoreCase( "PASS" ) ) {
         Assert.assertTrue( "NO ERROR", lresult.size() == 0 );

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
   public void checkTaskValidation_WITHWARNING( String aValidationCode ) {
      boolean lFound = false;
      String lerror_desc = null;
      String lerrorMsg = null;

      String lquery = TestConstants.AL_TASK_ERROR_CHECK;

      String[] iIds = { "ERROR_CD", "REF_ERROR_DESC" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      List<ArrayList<String>> lresult = execute( lquery, lfields );

      if ( aValidationCode.equalsIgnoreCase( "PASS" ) ) {
         Assert.assertTrue( "NO ERROR", lresult.size() == 0 );

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


   protected String getTaskErrors() {
      StringBuilder lQueryErrorList = new StringBuilder();
      lQueryErrorList.append( "SELECT distinct(error_cd), DL_REF_MESSAGE.user_desc " );
      lQueryErrorList.append(
            "FROM al_proc_tasks_error JOIN DL_REF_MESSAGE ON al_proc_tasks_error.error_cd = DL_REF_MESSAGE.result_cd " );
      lQueryErrorList.append( "WHERE al_proc_tasks_error.error_cd IS NOT NULL" );

      ResultSet lQueryResults;
      String lResults = "";
      try {
         lQueryResults = runQuery( lQueryErrorList.toString() );

         while ( lQueryResults.next() ) {

            lResults += "[" + lQueryResults.getString( "error_cd" );
            lResults += "-" + lQueryResults.getString( "user_desc" );
            lResults += "]";
            if ( !lQueryResults.isLast() ) {
               lResults += ',';
            }
         }

      } catch ( SQLException e ) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      return lResults;
   }


   /**
    * This function is to retrieve record id from AL_PROC_HIST_TASK
    *
    *
    *
    */
   public String getRecordID( String aSN_OEM ) {
      // MIM_DATA_TYPE table
      String[] iIds = { "RECORD_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "SERIAL_NO_OEM", aSN_OEM );

      String iQueryString =
            TableUtil.buildTableQuery( TableUtil.AL_PROC_HIST_TASK, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      String lId = llists.get( 0 ).get( 0 );

      return lId;
   }


   /**
    * This function is to convert string to sql date by given format.
    *
    *
    *
    */
   public java.sql.Date converStringToSQLDate( String aStrDate, String aformat ) {

      java.sql.Date ldate = null;

      SimpleDateFormat format = new SimpleDateFormat( aformat );
      try {
         ldate = new java.sql.Date( format.parse( aStrDate ).getTime() );
      } catch ( ParseException e ) {
         // TODO Auto-generated catch block
         e.printStackTrace();
         Assert.assertTrue( "convert Date", false );
      }
      return ldate;

   }


   /**
    * This function is to add days by given strign of date
    *
    *
    *
    */
   public Date DateAddDays( String aDate, int days ) {

      Date lDate = null;

      ResultSet ResultSetRecords;

      String Strquery =
            "SELECT TO_DATE('" + aDate + "', 'YYYY-MM-DD')+ " + days + " AS DateAdd FROM dual";

      try {
         ResultSetRecords = runQuery( Strquery.toString() );
         ResultSetRecords.next();
         lDate = ResultSetRecords.getDate( "DateAdd" );
      } catch ( SQLException e ) {
         e.printStackTrace();

      }
      return lDate;
   }


   /**
    *
    * This method runs the query, and returns the ids.
    *
    * @param aQuery
    *           - executes query
    * @param aDbId
    *           - column name of the DB ID
    * @param aId
    *           - column name of the ID
    * @return - Returns the Ids based on the simpleIDs class
    */
   public simpleIDs getIDs( String aQuery, String aDbId, String aId ) {

      ResultSet lResultSet = null;
      String lDbId = null;
      String lId = null;

      try {
         PreparedStatement lStatement = getConnection().prepareStatement( aQuery,
               ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY );

         lResultSet = lStatement.executeQuery( aQuery );
         lResultSet.next();
         lDbId = lResultSet.getString( aDbId );
         lId = lResultSet.getString( aId );
         if ( !lResultSet.isLast() )
            throw new IllegalArgumentException( "This query returns more than one row: " + aQuery );
      } catch ( SQLException e ) {
         e.printStackTrace();
      }

      return new simpleIDs( lDbId, lId );
   }


   /**
    * Runs the provided query on the database
    *
    * @param aTable
    *           Query to be run
    *
    * @return Returns the int query result
    */
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
    *
    * This method runs the query, and returns the Assmbl ids.
    *
    * @param aQuery
    *           - executes query
    * @param aDbId
    *           - column name of the Assmbl_DB_ID
    * @param aCd
    *           - column name of the Assmbl_CD
    * @param aId
    *           - column name of the Assmbl_Bom_ID
    * @return - Returns the Ids based on the simpleIDs class
    */
   public AssmblBomID getIDs( String aQuery, String aDbId, String aCd, String aId ) {

      ResultSet lResultSet = null;
      String lDbId = null;
      String lCd = null;
      String lId = null;

      try {
         PreparedStatement lStatement = getConnection().prepareStatement( aQuery,
               ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY );

         lResultSet = lStatement.executeQuery( aQuery );
         lResultSet.next();
         lDbId = lResultSet.getString( aDbId );
         lCd = lResultSet.getString( aCd );
         lId = lResultSet.getString( aId );
         if ( !lResultSet.isLast() )
            throw new IllegalArgumentException( "This query returns more than one row: " + aQuery );
      } catch ( SQLException e ) {
         e.printStackTrace();
      }

      return new AssmblBomID( lDbId, lCd, lId );
   }


   /**
    * insert/update blob value
    *
    */

   public void insertBlobvalue( String aTable, String aField, String aBlob, String aWhereClause ) {

      String lquery = ( aWhereClause == null ) ? "UPDATE " + aTable + " set " + aField + "=?"
            : "UPDATE " + aTable + " set " + aField + "=? " + aWhereClause;

      try {
         PreparedStatement psInsert = getConnection().prepareStatement( lquery );
         psInsert.setBytes( 1, aBlob.getBytes() );
         psInsert.execute();

      } catch ( SQLException e ) {
         // TODO Auto-generated catch block
         e.printStackTrace();
         Assert.assertTrue( "Insert BLOB failed.", false );
      }

   }


   public String getBlobvalue( String aTable, String aField, String aBlob, String aWhereClause ) {

      // prepare query for checking test results
      String lQuery = "select " + aField + " from " + aTable + " " + aWhereClause;

      try {
         PreparedStatement lStatement = getConnection().prepareStatement( lQuery,
               ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );

         ResultSet lResultSet = lStatement.executeQuery( lQuery );
         lResultSet.next();

         Blob blob = lResultSet.getBlob( aField );
         byte[] bdata = blob.getBytes( 1, ( int ) blob.length() );

         return new String( bdata );

      } catch ( SQLException e ) {
         e.printStackTrace();
         Assert.assertTrue( "retrive BLOB failed.", false );
         e.printStackTrace();
         return null;

      }
   }


   /**
    * Retrieve local db id from mim_local_db
    *
    */

   public String getlocalDBid() {
      return getStringValueFromQuery( "Select DB_ID from " + TableUtil.MIM_LOCAL_DB, "DB_ID" );

   }


   /**
    * This function is to retrieve data type ID by giving paras.
    *
    *
    *
    */
   public simpleIDs getDataTypeIDs( String aDATA_TYPE_CD, String aDATA_TYPE_SDESC,
         String aENG_UNIT_CD, String aDOMAIN_TYPE_CD ) {

      String[] iIds = { "DATA_TYPE_DB_ID", "DATA_TYPE_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "DATA_TYPE_CD", aDATA_TYPE_CD );
      lArgs.addArguments( "DATA_TYPE_SDESC", aDATA_TYPE_SDESC );
      lArgs.addArguments( "ENG_UNIT_CD", aENG_UNIT_CD );
      lArgs.addArguments( "DOMAIN_TYPE_CD", aDOMAIN_TYPE_CD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.MIM_DATA_TYPE, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

      return lIds;
   }


   /**
    * This function is to retrieve carrier IDs from ORG_CARRIER table.
    *
    *
    */
   public simpleIDs getCarrierIds( String aCARRIER_CD ) {

      String[] iIds = { "CARRIER_DB_ID", "CARRIER_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "CARRIER_CD", aCARRIER_CD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.ORG_CARRIER, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      // Get IDs
      simpleIDs lIds = new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );
      return lIds;
   }


   /**
    * This function is to retrieve AssmblBomID from EQP_ASSMBL_BOM table .
    *
    *
    */
   public AssmblBomID getAssmblBomId( String aAssmblCD, String aASSMBL_BOM_CD ) {
      String[] iIds = { "ASSMBL_DB_ID", "ASSMBL_CD", "ASSMBL_BOM_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      // Parameters required by the query.
      WhereClause lArgs = new WhereClause();
      lArgs.addArguments( "ASSMBL_BOM_CD", aASSMBL_BOM_CD );
      lArgs.addArguments( "ASSMBL_CD", aAssmblCD );

      String iQueryString = TableUtil.buildTableQuery( TableUtil.EQP_ASSMBL_BOM, lfields, lArgs );
      List<ArrayList<String>> llists = execute( iQueryString, lfields );

      AssmblBomID lIds = new AssmblBomID( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ),
            llists.get( 0 ).get( 2 ) );

      return lIds;

   }


   /**
    * This function is to retrieve AssmblBomID from EQP_ASSMBL_BOM table .
    *
    *
    */
   public bomPartPN getAssmblINFOR( String aPN_OEM ) {

      String lStr =
            "SELECT eqp_bom_part.bom_part_db_id,eqp_bom_part.bom_part_id,EQP_PART_NO.Part_No_DB_ID, EQP_PART_NO.Part_No_ID from eqp_bom_part \r\n"
                  + "inner join EQP_PART_BASELINE on\r\n"
                  + "EQP_BOM_PART.BOM_PART_DB_ID=EQP_PART_BASELINE.Bom_Part_Db_Id and\r\n"
                  + "EQP_BOM_PART.BOM_PART_ID=EQP_PART_BASELINE.BOM_PART_ID\r\n"
                  + "inner join EQP_PART_NO on\r\n"
                  + "EQP_PART_NO.PART_NO_DB_ID=EQP_PART_BASELINE.PART_NO_DB_ID and\r\n"
                  + "EQP_PART_NO.PART_NO_ID=EQP_PART_BASELINE.PART_NO_ID\r\n"
                  + "INNER JOIN eqp_assmbl_bom ON\r\n"
                  + "eqp_bom_part.assmbl_db_id=eqp_assmbl_bom.assmbl_db_id AND\r\n"
                  + "eqp_bom_part.assmbl_cd=eqp_assmbl_bom.assmbl_cd AND \r\n"
                  + "eqp_bom_part.assmbl_bom_id=eqp_assmbl_bom.assmbl_bom_id\r\n"
                  + "WHERE EQP_PART_NO.Part_No_Oem='" + aPN_OEM + "'";

      String[] iIds = { "bom_part_db_id", "bom_part_id", "Part_No_DB_ID", "Part_No_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( iIds ) );

      List<ArrayList<String>> llists = execute( lStr, lfields );

      bomPartPN lIds = new bomPartPN( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ),
            llists.get( 0 ).get( 2 ), llists.get( 0 ).get( 3 ) );

      return lIds;

   }

}
