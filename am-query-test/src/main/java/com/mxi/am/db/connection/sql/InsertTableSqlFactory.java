
package com.mxi.am.db.connection.sql;

import java.util.Map;
import java.util.Map.Entry;

import com.mxi.am.db.connection.format.ColumnValueEscaper;
import com.mxi.am.db.connection.format.ColumnValueEscaper.ColumnType;


/**
 * Builds the insert table SQL.
 */
public class InsertTableSqlFactory {

   private InsertTableSqlFactory() {
      // utility class
   }


   public static String get( String aTableName, Map<String, ? extends Object> aColumnValues ) {
      StringBuilder lBuilder = new StringBuilder();
      lBuilder.append( "INSERT INTO " ).append( aTableName.toUpperCase() ).append( " (" );
      boolean lFirst = true;
      for ( String lColumnName : aColumnValues.keySet() ) {
         if ( !lFirst ) {
            lBuilder.append( ", " );
         }

         lBuilder.append( lColumnName );
         lFirst = false;
      }
      lBuilder.append( ")\n" );

      lBuilder.append( "VALUES(" );
      lFirst = true;
      for ( Entry<String, ? extends Object> lColumnValue : aColumnValues.entrySet() ) {
         if ( !lFirst ) {
            lBuilder.append( ", " );
         }

         ColumnType lColumnType =
               ColumnValueEscaper.getInferredColumnType( aTableName, lColumnValue.getKey() );
         lBuilder.append( lColumnType.escape( lColumnValue.getValue() ) );
         lFirst = false;
      }
      lBuilder.append( ")" );

      return lBuilder.toString();
   }
}
