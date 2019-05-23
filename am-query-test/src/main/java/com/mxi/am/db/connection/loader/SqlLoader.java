
package com.mxi.am.db.connection.loader;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;


/**
 * Loads data from an sql file.
 */
public final class SqlLoader {

   private SqlLoader() {
      // utility class
   }


   public static void load( Connection aConnection, Class<?> aClass, String aResource ) {
      String lFullSql = ResourceReader.get( aClass, aResource );
      try ( Statement lStatement = aConnection.createStatement() ) {
         try ( Scanner lSqlScanner = new Scanner( lFullSql ) ) {
            lSqlScanner.useDelimiter( ";" );
            while ( lSqlScanner.hasNext() ) {
               String lSql = lSqlScanner.next();
               if ( lSql.trim().isEmpty() ) {
                  continue;
               }

               try {
                  lStatement.execute( lSql );
               } catch ( SQLException e ) {
                  throw new RuntimeException(
                        "Could not execute statement:" + System.lineSeparator() + lSql, e );
               }
            }
         }
      } catch ( SQLException e ) {
         throw new RuntimeException(
               "Could not execute statement:" + System.lineSeparator() + lFullSql, e );
      }
   }
}
