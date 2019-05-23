package com.mxi.am.db.connection.executor;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.jdom.Document;

import com.mxi.am.db.connection.sql.SQLStatementFactory;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.DatabaseStatement;
import com.mxi.mx.common.dataset.ProcedureStatement;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.dataset.SQLStatement;
import com.mxi.mx.common.exception.MxRuntimeException;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.utils.DataTypeUtils;
import com.mxi.mx.common.utils.IOUtils;
import com.mxi.mx.common.utils.XMLUtils;


/**
 * Executes queries.
 */
public final class QueryExecutor {

   private QueryExecutor() {
      // utility function
   }


   /**
    * Execute the test function.
    *
    * @param aFieldNames
    *           list of fields to be selected
    * @param aFieldWhereArgs
    *           the function arguments
    * @param aConnection
    *
    * @param aPlSqlPackageName
    *           the package name
    *
    * @param aPlSqlFunctionName
    *           the function name
    * @return the DataSet
    *
    * @throws SQLException
    *
    */
   public static DataSet executeTableFunction( List<String> aFieldNames,
         DataSetArgument aFieldWhereArgs, Connection aConnection, String aPlSqlPackageName,
         String aPlSqlFunctionName ) throws SQLException {

      DataSet lDataSet;

      // string to SQL statement
      SQLStatement lStatement = SQLStatementFactory.buildTableFunction( aPlSqlPackageName,
            aPlSqlFunctionName, aFieldNames, aFieldWhereArgs );

      // Prepare the statement
      lStatement.prepare( aConnection );

      // Execute the statement
      lDataSet = lStatement.executeQuery();

      // Close the statement
      lStatement.close();

      // Return the Result Set
      return lDataSet;
   }


   public static QuerySet executeQuery( Class<?> aClass, DataSetArgument aArgs ) {
      return QuerySetFactory.getInstance().executeQuery( getQueryName( aClass ), aArgs );
   }


   public static DataSet executeQuery( Connection aConnection, String aQueryName ) {
      DataSet lDs;
      try {

         // Get the query file, and build a statement for it
         Document lQueryFile = getQueryFile( aQueryName );
         SQLStatement lStatement = new SQLStatement( lQueryFile );

         // Execute the query
         lStatement.prepare( aConnection );

         lDs = lStatement.executeQuery();
         lStatement.close();
      } catch ( Exception ex ) {
         throw new MxRuntimeException( ex );
      }

      return lDs;
   }


   /**
    * DOCUMENT_ME
    *
    * @param aQueryName
    * @param aArgs
    * @return
    * @throws Exception
    */
   public static DataSetArgument executePlsql( Connection aConnection, String aQueryName,
         DataSetArgument aArgs ) throws Exception {
      try {

         // Get the query file, and build a statement for it
         Document lQueryFile = getQueryFile( aQueryName );
         DatabaseStatement lStatement = new ProcedureStatement( lQueryFile );

         // Prepare the statement
         lStatement.prepare( aConnection );

         // Execute the statement
         lStatement.execute( aArgs );

         // Close the statement
         lStatement.close();
      } catch ( Exception ex ) {
         throw new MxRuntimeException( ex );
      }

      // If the procedure has a return value
      if ( aArgs.contains( "aReturn" ) ) {

         // Check the return value; if it's negative, an error occurred
         if ( aArgs.getInteger( "aReturn" ) < 0 ) {

            // Get the error message
            String lErrMsg = checkPlsqlErrors( aConnection );

            throw new Exception( lErrMsg );
         }
      }

      // Return the arguments
      return aArgs;
   }


   public static String executeFunction( Connection aConnection, String aFunctionName,
         String[] aParmOrder, DataSetArgument aArgs ) {

      DataSet lDs;

      try {

         // Get the function name, and build a statement for it
         StringBuffer lQueryStatement = new StringBuffer();
         lQueryStatement.append( "SELECT " );
         lQueryStatement.append( aFunctionName );
         lQueryStatement.append( "( " );

         // Add all the Arguments in order
         for ( int i = 0; i < aParmOrder.length; i++ ) {

            String lParmName = aParmOrder[i];
            String lParmType = aArgs.getDataType( lParmName );
            String lParmValue = aArgs.getString( lParmName );

            // Append the Data
            if ( DataTypeUtils.DATE.equals( lParmType ) ) {
               lQueryStatement.append( "to_date('" ).append( lParmValue )
                     .append( "', 'dd-mm-yyyy hh24:mi:ss')" );
            } else if ( DataTypeUtils.STRING.equals( lParmType ) ) {
               lQueryStatement.append( "'" ).append( lParmValue ).append( "'" );
            } else {
               lQueryStatement.append( lParmValue );
            }

            // Comma delimit if there are more Parameters
            if ( ( i + 1 ) < aParmOrder.length ) {
               lQueryStatement.append( ", " );
            }
         }

         lQueryStatement.append( " ) AS aResult " );
         lQueryStatement.append( "FROM dual" );

         SQLStatement lStatement = new SQLStatement( lQueryStatement.toString() );

         // Prepare the statement
         lStatement.prepare( aConnection );

         // Execute the statement
         lDs = lStatement.executeQuery();

         // Close the statement
         lStatement.close();
      } catch ( Exception ex ) {
         throw new MxRuntimeException( ex );
      }

      // Return the Result
      return lDs.next() ? lDs.getString( "aResult" ) : null;
   }


   public static DataSet executeQuery( Connection aConnection, String aQueryName,
         DataSetArgument aArgs ) {
      DataSet lDs;
      try {

         // Get the query file, and build a statement for it
         Document lQueryFile = getQueryFile( aQueryName );
         SQLStatement lStatement = new SQLStatement( lQueryFile );

         // Replace dynamic from select clauses
         lStatement.insertSelect( aArgs );

         // Replace dynamic from tables
         lStatement.insertFrom( aArgs );

         // Replace dynamic where clauses
         lStatement.insertOrder( null );

         // Replace dynamic where clauses
         lStatement.insertWhere( aArgs );

         // Execute the query
         lStatement.prepare( aConnection );

         lDs = lStatement.executeQuery( aArgs );
         lStatement.close();
      } catch ( Exception ex ) {
         throw new MxRuntimeException( ex );
      }

      return lDs;
   }


   public static int executeUpdate( Connection aConnection, String aQueryName,
         DataSetArgument aArgs ) {
      int lNumRows = -1;
      try {

         // Get the query file, and build a statement for it
         Document lQueryFile = QueryExecutor.getQueryFile( aQueryName );
         SQLStatement lStatement = new SQLStatement( lQueryFile );

         // Replace dynamic from tables
         lStatement.insertFrom( aArgs );

         // Replace dynamic where clauses
         lStatement.insertWhere( aArgs );

         // Execute the query
         lStatement.prepare( aConnection );

         lNumRows = lStatement.executeUpdate( aArgs );
         lStatement.close();
      } catch ( Exception ex ) {
         throw new MxRuntimeException( ex );
      }

      return lNumRows;
   }


   /**
    * Performs error checking in the PL/SQL stored procedures.
    *
    * @return the error message.
    *
    * @exception Exception
    *               if an error occurs.
    */
   private static String checkPlsqlErrors( Connection aConnection ) throws Exception {

      // Build arguments for return
      DataSetArgument lArgs = new DataSetArgument();

      // Get the query input stream
      InputStream lInputStream =
            IOUtils.getQueryFile( "com.mxi.mx.core.query.plsql.AppObjPkgGetMxiError" );

      // Build the JDOM document object
      Document lQueryDoc = XMLUtils.buildJDOMDocument( lInputStream );

      // Close the stream
      lInputStream.close();

      //
      // Execute the procedure.
      //

      // Build the procedure database statement
      DatabaseStatement lStatement = new ProcedureStatement( lQueryDoc );

      // Prepare the statement
      lStatement.prepare( aConnection );

      // Execute the statement
      lStatement.execute( lArgs );

      // Close the statement
      lStatement.close();

      // Extract the return arguments
      return lArgs.getString( "aErrMsg" );
   }


   /**
    * Retrieves the query file that corresponds to the test case. This method assumes that the test
    * class package and name mirror the corresponding query package and name.
    *
    * @return the query file JDOM document.
    *
    * @throws Exception
    *            if an error occurs.
    */
   private static Document getQueryFile( String aQueryName ) throws Exception {
      // Build the JDOM document object
      try ( InputStream lInputStream = IOUtils.getQueryFile( aQueryName ) ) {
         return XMLUtils.buildJDOMDocument( lInputStream );
      }
   }


   public static String getQueryName( Class<?> aClass ) {
      String lTestClassName = aClass.getName();
      return lTestClassName.substring( 0, lTestClassName.lastIndexOf( "Test" ) );
   }


   public static String getFunctionName( Class<?> aClass ) {
      String lTestClassName = aClass.getSimpleName();
      return lTestClassName.substring( 0, lTestClassName.lastIndexOf( "Test" ) );
   }
}
