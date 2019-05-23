
package com.mxi.mx.testing;

import java.sql.Connection;
import java.sql.DriverManager;

import com.mxi.mx.common.exception.MxRuntimeException;
import com.mxi.mx.db.connection.DatabaseProperties;
import com.mxi.mx.db.connection.DatabasePropertiesReader;


/**
 * Manages a connection to Oracle query test database
 */
public final class ConnectionProvider {

   /** The database connection. */
   private static Connection sConnection;


   /**
    * Gets a connection to the database.
    *
    * @return a connection to the database.
    */
   public static synchronized Connection getConnection() {
      // If the connection hasn't yet been built, build it
      if ( sConnection != null ) {
         return sConnection;
      }

      // Get the connection properties
      DatabaseProperties lDatabaseProperties = DatabasePropertiesReader.get();
      final String lJdbcDriver = "oracle.jdbc.OracleDriver";
      final String lUsername = lDatabaseProperties.getUsername();
      final String lPassword = lDatabaseProperties.getPassword();
      final String lJdbcUrl = lDatabaseProperties.getJdbcConnectString();

      try {

         // Load the JDBC driver class
         Class.forName( lJdbcDriver );

         // Get a JDBC connection
         sConnection = DriverManager.getConnection( lJdbcUrl, lUsername, lPassword );

         // Disable auto commit
         sConnection.setAutoCommit( false );

         return sConnection;
      } catch ( Exception ex ) {
         throw new MxRuntimeException( ex );
      }
   }
}
