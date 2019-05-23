package com.mxi.am.db.connection.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.SQLStatement;
import com.mxi.mx.common.utils.DataTypeUtils;
import com.mxi.mx.common.utils.StringUtils;


/**
 * This class manage the building of SQL statement
 *
 * @author mgabua
 */
public class SQLStatementFactory {

   /**
    * Construct insert statement
    *
    * @param aTableName
    *           the table name
    * @param aDataMap
    *           data map (field and value)
    *
    * @return proper form of insert statement
    *
    */
   public static String buildInsertToTable( String aTableName, Map<String, String> aDataMap ) {
      StringBuffer lColumns = new StringBuffer();
      StringBuffer lValues = new StringBuffer();

      for ( Entry<String, String> lDataMap : aDataMap.entrySet() ) {
         lColumns.append( lDataMap.getKey() ).append( ',' );
         lValues.append( lDataMap.getValue() ).append( ',' );
      }

      StringBuilder lInsertStatement = new StringBuilder();
      lInsertStatement.append( "INSERT INTO " + aTableName + " (" );
      lInsertStatement.append( StringUtils.stripCharFromEnd( lColumns.toString(), ',' ) + ")" );
      lInsertStatement
            .append( " VALUES (" + StringUtils.stripCharFromEnd( lValues.toString(), ',' ) + ")" );

      return lInsertStatement.toString();
   }


   /**
    * Builds a table function query
    *
    * @param aPackageName
    *           the database package name
    * @param aFunctionName
    *           the function name in a package
    * @param aFieldNames
    *           list of fields to be selected
    * @param aFieldWhereArgs
    *           the function arguments.
    *
    * @return the result of the function.
    *
    */

   public static SQLStatement buildTableFunction( String aPackageName, String aFunctionName,
         List<String> aFieldNames, DataSetArgument aFieldWhereArgs ) {

      // start constructing SQL query string
      StringBuffer lQueryStatement = new StringBuffer();
      lQueryStatement.append( "SELECT " );

      // Flatten the fields name to be selected to comma delimited
      if ( aFieldNames == null ) {
         lQueryStatement.append( " * " );
      } else {
         lQueryStatement.append(
               StringUtils.toDelimitedString( ", ", aFieldNames ).replaceAll( "^\\[|\\]$", "" ) );
      }

      // get the table function name based on package and class names
      lQueryStatement.append( " FROM " );
      lQueryStatement.append( "TABLE (" + aPackageName + "." + aFunctionName + "(" );

      // Add all the Arguments in order
      Boolean lIsFirst = true;
      for ( Entry<String, Object> aFieldWhereArg : aFieldWhereArgs.entrySet() ) {

         Object lParmValue = aFieldWhereArg.getValue();
         String lParmType = lParmValue.getClass().getSimpleName();

         // will add comma
         if ( !lIsFirst ) {
            lQueryStatement.append( "," );
         }

         // Append the formatted data
         lQueryStatement.append( formatParmValue( lParmType.toUpperCase(), lParmValue ) );
         lIsFirst = false;
      }

      // closing statements
      lQueryStatement.append( "))" );

      return new SQLStatement( lQueryStatement.toString() );

   }


   /**
    * Builds a table function query returning all columns
    *
    * @param aPackageName
    *           the database package name
    * @param aFunctionName
    *           the function name in a package
    * @param aFieldWhereArgs
    *           the function arguments.
    *
    * @return the result of the function.
    *
    */
   public static SQLStatement buildTableFunction( String aPackageName, String aFunctionName,
         DataSetArgument aFieldWhereArgs ) {
      return buildTableFunction( aPackageName, aFunctionName, null, aFieldWhereArgs );
   }


   /**
    * Format value based on data type
    *
    * @param aParmType
    *           the data type
    * @param aParmValue
    *           the actual value
    *
    * @return string formatted value
    *
    */
   private static String formatParmValue( String aParmType, Object aParmValue ) {

      String lParmValue = null;

      if ( DataTypeUtils.DATE.equals( aParmType ) ) {
         lParmValue = "to_date('" + aParmValue + "', 'dd-mm-yyyy hh24:mi:ss')";
      } else if ( DataTypeUtils.STRING.equalsIgnoreCase( aParmType ) ) {
         lParmValue = "'" + aParmValue + "'";
      } else {
         lParmValue = aParmValue.toString();
      }

      return lParmValue;

   }


   /**
    * Builds a table function query
    *
    * @param aTableName
    *           the table name
    * @param aFieldNames
    *           list of fields to be selected
    * @param aFieldWhereArgs
    *           the where clause
    *
    * @return the result of query string.
    *
    */

   public static String buildTableQuery( String aTableName, List<String> aFieldNames,
         WhereClause aFieldWhereArgs ) {

      // start constructing SQL query string
      StringBuffer lQueryStatement = new StringBuffer();
      lQueryStatement.append( "SELECT " );

      // Flatten the fields name to be selected to comma delimited
      if ( aFieldNames == null ) {
         lQueryStatement.append( " * " );
      } else {
         lQueryStatement.append(
               StringUtils.toDelimitedString( ", ", aFieldNames ).replaceAll( "^\\[|\\]$", "" ) );
      }

      // get the table function name based on package and class names
      lQueryStatement.append( " FROM " );
      lQueryStatement.append( aTableName + " where " );

      // Add all the Arguments in order
      Boolean lIsFirst = true;
      for ( Entry<String, String> aFieldWhereArg : aFieldWhereArgs.entrySet() ) {

         String lParmField = aFieldWhereArg.getKey();
         String lParmValue = aFieldWhereArg.getValue();

         // will add comma
         if ( !lIsFirst ) {
            lQueryStatement.append( " and " );
         }

         // Append the formatted data
         lQueryStatement.append( lParmField.toUpperCase() + "=\'" + lParmValue + "\'" );
         lIsFirst = false;
      }

      return lQueryStatement.toString();

   }


   /**
    * Execute the query.
    */
   public List<ArrayList<String>> execute( Connection aConn, String aStrQuery,
         List<String> lfields ) {

      PreparedStatement lStatement;
      List<ArrayList<String>> louter = new ArrayList<ArrayList<String>>();

      try {
         lStatement = aConn.prepareStatement( aStrQuery, ResultSet.TYPE_SCROLL_SENSITIVE,
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

}
