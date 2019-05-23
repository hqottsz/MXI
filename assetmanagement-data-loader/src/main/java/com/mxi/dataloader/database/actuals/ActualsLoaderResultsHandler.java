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
package com.mxi.dataloader.database.actuals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;


/**
 * Parses Actuals Loader validation results. If failures are found they are logged to console and
 * build is automatically failed.
 */

public class ActualsLoaderResultsHandler {

   final static Logger iLogger = Logger.getLogger( ActualsLoaderResultsHandler.class );

   private Connection iConnection;

   private static final String INVENTORY_QUERY_FILE = "InventoryValidation.sql";
   private static final String TASK_QUERY_FILE = "TaskValidation.sql";
   private static final String FAULT_QUERY_FILE = "FaultValidation.sql";
   private static final String WPKG_QUERY_FILE = "WPKGValidation.sql";


   /**
    * Creates a new {@linkplain ActualsLoaderResultsHandler} object.
    *
    * @param aLogger
    * @throws SQLException
    */
   public ActualsLoaderResultsHandler(String aJdbcConnectString, String aUserName, String aPassword)
         throws SQLException {
      iConnection = DriverManager.getConnection( aJdbcConnectString, aUserName, aPassword );
   }


   public void checkValidationResults( String aEntity ) throws Exception {
      boolean lValidationFailure = false;

      if ( aEntity.contains( "validate_inventory" ) ) {
         lValidationFailure = checkInventory();
         iConnection.close();

         if ( lValidationFailure ) {
            throw new Exception(
                  "*** There were failing Actuals Loader inventory validations. Build failed. ***" );
         }
      }

      if ( aEntity.contains( "validate_task" ) ) {
         lValidationFailure = checkTasks();
         iConnection.close();

         if ( lValidationFailure ) {
            throw new Exception(
                  "*** There were failing Actuals Loader task validations. Build failed. ***" );
         }
      }

      if ( aEntity.contains( "validate_odf" ) ) {
         lValidationFailure = checkFaults();
         iConnection.close();

         if ( lValidationFailure ) {
            throw new Exception(
                  "*** There were failing Actuals Loader odf validations. Build failed. ***" );
         }
      }

      if ( aEntity.contains( "validate_work_package" ) ) {
         lValidationFailure = checkWPKG();
         iConnection.close();

         if ( lValidationFailure ) {
            throw new Exception(
                  "*** There were failing Actuals Loader work package validations. Build failed. ***" );
         }
      }

   }


   private boolean checkInventory() throws Exception {

      iLogger.info( "Processing inventory." );

      PreparedStatement lStatement = null;
      ResultSet lInventoryResultSet = null;
      boolean lFailureFound = false;

      try {
         Class.forName( "oracle.jdbc.OracleDriver" );

         lStatement = iConnection.prepareStatement( getQuery( INVENTORY_QUERY_FILE ) );
         lInventoryResultSet = lStatement.executeQuery();
         StringBuilder lQueryResults = new StringBuilder();

         // if an error is found, print results and toggle error boolean
         if ( lInventoryResultSet.next() ) {

            iLogger.error( "**** Inventory validation Error Found: ****" );
            lFailureFound = true;

            do {
               // collect query results for current row
               lQueryResults.append( "TABLE: " + lInventoryResultSet.getString( "TABLE" ) + ", " );
               lQueryResults.append(
                     "SERIAL_NO_OEM: " + lInventoryResultSet.getString( "SERIAL_NO_OEM" ) + ", " );
               lQueryResults.append(
                     "PART_NO_OEM: " + lInventoryResultSet.getString( "PART_NO_OEM" ) + ", " );
               lQueryResults.append(
                     "MANUFACT_CD: " + lInventoryResultSet.getString( "MANUFACT_CD" ) + ", " );
               lQueryResults
                     .append( "ERROR_CD: " + lInventoryResultSet.getString( "ERROR_CD" ) + ", " );
               lQueryResults.append(
                     "SEVERITY_CD: " + lInventoryResultSet.getString( "SEVERITY_CD" ) + ", " );
               lQueryResults
                     .append( "USER_DESC: " + lInventoryResultSet.getString( "USER_DESC" ) + ";" );
               // log failing row to console
               iLogger.error( lQueryResults );

               // clear StringBuilder for next result
               lQueryResults.setLength( 0 );

            } while ( lInventoryResultSet.next() );
         } else {
            iLogger.info( "No inventory validation errors found." );
            lFailureFound = false;
         }

      } catch ( SQLException e ) {
         iLogger.error( e.toString() );

         // close Statement and ResultSet
      } finally {
         try {
            if ( lStatement != null ) {
               lStatement.close();
            }
         } catch ( Exception e ) {
            iLogger.error( e.toString() );
         }
         try {
            if ( lInventoryResultSet != null ) {
               lInventoryResultSet.close();
            }
         } catch ( Exception e ) {
            iLogger.error( e.toString() );
         }

      }
      return lFailureFound;

   }


   /**
    * This function to validate faults errors by using FaultValidation.sql
    *
    * @return boolean if error(s) occurs.
    *
    *
    *
    */
   private boolean checkFaults() throws Exception {
      iLogger.info( "Processing faults." );

      ResultSet lFaultResultSet = null;
      PreparedStatement lStatement = null;
      boolean lFailureFound = false;

      try {
         Class.forName( "oracle.jdbc.OracleDriver" );

         lStatement = iConnection.prepareStatement( getQuery( FAULT_QUERY_FILE ) );
         lFaultResultSet = lStatement.executeQuery();
         StringBuilder lQueryResults = new StringBuilder();

         // if an error is found, print results and toggle error boolean
         if ( lFaultResultSet.next() ) {

            iLogger.error( "**** Fault validation Error Found: ****" );
            lFailureFound = true;

            do {
               // collect query results for current row
               lQueryResults.append( "TABLE: " + lFaultResultSet.getString( "TABLE" ) + ", " );
               lQueryResults
                     .append( "ASSMBL_CD: " + lFaultResultSet.getString( "ASSMBL_CD" ) + ", " );
               lQueryResults
                     .append( "PART_NO_OEM: " + lFaultResultSet.getString( "PART_NO_OEM" ) + ", " );
               lQueryResults
                     .append( "ATA_SYS_CD: " + lFaultResultSet.getString( "ATA_SYS_CD" ) + ", " );
               lQueryResults
                     .append( "RESULT_CD: " + lFaultResultSet.getString( "RESULT_CD" ) + ", " );
               lQueryResults
                     .append( "USER_DESC: " + lFaultResultSet.getString( "USER_DESC" ) + ";" );

               iLogger.error( lQueryResults );

               // clear StringBuilder for next result
               lQueryResults.setLength( 0 );

            } while ( lFaultResultSet.next() );
         } else {
            iLogger.info( "No fault validation errors found." );
            lFailureFound = false;
         }

      } catch ( SQLException e ) {
         iLogger.error( e.toString() );

         // close Statement and ResultSet
      } finally {
         try {
            if ( lStatement != null ) {
               lStatement.close();
            }
         } catch ( Exception e ) {
            iLogger.error( e.toString() );
         }

         try {
            if ( lFaultResultSet != null ) {
               lFaultResultSet.close();
            }
         } catch ( Exception e ) {
            iLogger.error( e.toString() );
         }
      }
      return lFailureFound;

   }


   private boolean checkTasks() throws Exception {
      iLogger.info( "Processing tasks." );

      ResultSet lTaskResultSet = null;
      PreparedStatement lStatement = null;
      boolean lFailureFound = false;

      try {
         Class.forName( "oracle.jdbc.OracleDriver" );

         lStatement = iConnection.prepareStatement( getQuery( TASK_QUERY_FILE ) );
         lTaskResultSet = lStatement.executeQuery();
         StringBuilder lQueryResults = new StringBuilder();

         // if an error is found, print results and toggle error boolean
         if ( lTaskResultSet.next() ) {

            iLogger.error( "**** Task validation Error Found: ****" );
            lFailureFound = true;

            do {
               // collect query results for current row
               lQueryResults.append( "TABLE: " + lTaskResultSet.getString( "TABLE" ) + ", " );
               lQueryResults.append( "TASK_CD: " + lTaskResultSet.getString( "TASK_CD" ) + ", " );
               lQueryResults.append(
                     "SERIAL_NO_OEM: " + lTaskResultSet.getString( "SERIAL_NO_OEM" ) + ", " );
               lQueryResults
                     .append( "PART_NO_OEM: " + lTaskResultSet.getString( "PART_NO_OEM" ) + ", " );
               lQueryResults
                     .append( "MANUFACT_CD: " + lTaskResultSet.getString( "MANUFACT_CD" ) + ", " );
               lQueryResults.append(
                     "PARAMETER_CD: " + lTaskResultSet.getString( "PARAMETER_CD" ) + ", " );
               lQueryResults.append( "ERROR_CD: " + lTaskResultSet.getString( "ERROR_CD" ) + ", " );
               lQueryResults
                     .append( "SEVERITY_CD: " + lTaskResultSet.getString( "SEVERITY_CD" ) + ", " );
               lQueryResults
                     .append( "USER_DESC: " + lTaskResultSet.getString( "USER_DESC" ) + ";" );

               iLogger.error( lQueryResults );

               // clear StringBuilder for next result
               lQueryResults.setLength( 0 );

            } while ( lTaskResultSet.next() );
         } else {
            iLogger.info( "No task validation errors found." );
            lFailureFound = false;
         }

      } catch ( SQLException e ) {
         iLogger.error( e.toString() );

         // close Statement and ResultSet
      } finally {
         try {
            if ( lStatement != null ) {
               lStatement.close();
            }
         } catch ( Exception e ) {
            iLogger.error( e.toString() );
         }

         try {
            if ( lTaskResultSet != null ) {
               lTaskResultSet.close();
            }
         } catch ( Exception e ) {
            iLogger.error( e.toString() );
         }
      }
      return lFailureFound;

   }


   private boolean checkWPKG() throws Exception {

      iLogger.info( "Processing workpackage." );

      PreparedStatement lStatement = null;
      ResultSet lWPKGResultSet = null;
      boolean lFailureFound = false;

      try {
         Class.forName( "oracle.jdbc.OracleDriver" );

         lStatement = iConnection.prepareStatement( getQuery( WPKG_QUERY_FILE ) );
         lWPKGResultSet = lStatement.executeQuery();
         StringBuilder lQueryResults = new StringBuilder();

         // if an error is found, print results and toggle error boolean
         if ( lWPKGResultSet.next() ) {

            iLogger.error( "**** workpackage validation Error Found: ****" );
            lFailureFound = true;

            do {
               // collect query results for current row
               lQueryResults.append( "TABLE: " + lWPKGResultSet.getString( "TABLE" ) + ", " );
               lQueryResults
                     .append( "RESULT_CD: " + lWPKGResultSet.getString( "RESULT_CD" ) + ", " );
               lQueryResults
                     .append( "USER_DESC: " + lWPKGResultSet.getString( "USER_DESC" ) + ";" );
               // log failing row to console
               iLogger.error( lQueryResults );

               // clear StringBuilder for next result
               lQueryResults.setLength( 0 );

            } while ( lWPKGResultSet.next() );
         } else {
            iLogger.info( "No inventory validation errors found." );
            lFailureFound = false;
         }

      } catch ( SQLException e ) {
         iLogger.error( e.toString() );

         // close Statement and ResultSet
      } finally {
         try {
            if ( lStatement != null ) {
               lStatement.close();
            }
         } catch ( Exception e ) {
            iLogger.error( e.toString() );
         }
         try {
            if ( lWPKGResultSet != null ) {
               lWPKGResultSet.close();
            }
         } catch ( Exception e ) {
            iLogger.error( e.toString() );
         }

      }
      return lFailureFound;

   }


   private String getQuery( String aFileName ) throws Exception {

      String lQuery = "";

      ClassLoader lClassLoader = getClass().getClassLoader();

      try {
         InputStream lInputStream = lClassLoader.getResourceAsStream( aFileName );
         lQuery = readFromInputStream( lInputStream );
         if ( lQuery.length() == 0 ) {
            throw new Exception( "*** " + aFileName + " query could not be loaded. ***" );
         }

      } catch ( IOException e ) {
         e.printStackTrace();
      }

      iLogger.info( "Returning Query: " + lQuery );
      return lQuery.toString();
   }


   private String readFromInputStream( InputStream aInputStream ) throws IOException {
      StringBuilder lResultStringBuilder = new StringBuilder();
      try ( BufferedReader lBr =
            new BufferedReader( new InputStreamReader( aInputStream, StandardCharsets.UTF_8 ) ) ) {
         String lLine;
         while ( ( lLine = lBr.readLine() ) != null ) {
            lResultStringBuilder.append( lLine ).append( "\n" );
         }
      }
      return lResultStringBuilder.toString();
   }

}
