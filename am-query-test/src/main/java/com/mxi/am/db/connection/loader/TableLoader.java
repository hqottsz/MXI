
package com.mxi.am.db.connection.loader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import com.mxi.am.db.connection.sql.InsertTableSqlFactory;


/**
 * Loads a single row into the table.
 */
public final class TableLoader {

   private TableLoader() {
      // utility class
   }


   public static void load( Connection aConnection, String aTableName,
         Map<String, ? extends Object> aColumnValues ) {
      String lSql = InsertTableSqlFactory.get( aTableName, aColumnValues );
      try ( PreparedStatement lStatement = aConnection.prepareStatement( lSql ) ) {
         lStatement.execute();
      } catch ( SQLException e ) {
         throw new RuntimeException(
               "Could not load table: " + aTableName + " with " + aColumnValues, e );
      }
   }
}
