
package com.mxi.am.driver.common.database;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * Reads the database properties file.
 */
public final class DatabasePropertiesReader {

   private static final String DATABASE_USERNAME = "database.username";
   private static final String DATABASE_PASSWORD = "database.password";
   private static final String DATABASE_HOST = "database.host";
   private static final String DATABASE_PORT = "database.port";
   private static final String DATABASE_SERVICE = "database.service";


   private DatabasePropertiesReader() {
      // utility class
   }


   public static DatabaseProperties get() {
      return get( DatabasePropertiesReader.class, "/database.properties" );
   }


   public static DatabaseProperties get( Class<?> aClass, String aResource ) {
      try ( InputStream lInputStream = aClass.getResourceAsStream( aResource ) ) {
         if ( lInputStream == null ) {
            throw new RuntimeException(
                  "Tried to load a database properties file that does not exist: " + aResource );
         }

         Properties lProperties = new Properties();
         lProperties.load( lInputStream );

         validateDatabaseProperties( lProperties );

         return new DatabaseProperties( lProperties.getProperty( DATABASE_USERNAME ),
               lProperties.getProperty( DATABASE_PASSWORD ),
               lProperties.getProperty( DATABASE_HOST ),
               Integer.valueOf( lProperties.getProperty( DATABASE_PORT ) ),
               lProperties.getProperty( DATABASE_SERVICE ) );
      } catch ( IOException e ) {
         throw new RuntimeException( "Could not load database properties: " + aResource, e );
      }
   }


   private static void validateDatabaseProperties( Properties aProperties ) {
      validateDatabaseProperty( aProperties, DATABASE_SERVICE );
      validateDatabaseProperty( aProperties, DATABASE_USERNAME );
      validateDatabaseProperty( aProperties, DATABASE_PASSWORD );
      validateDatabaseProperty( aProperties, DATABASE_HOST );
      validateDatabaseProperty( aProperties, DATABASE_PORT );
   }


   private static void validateDatabaseProperty( Properties aProperties, String aPropertyName ) {
      if ( aProperties.getProperty( aPropertyName ).isEmpty() ) {
         throw new RuntimeException( "Database property '" + aPropertyName
               + "' in properties file '/database.properties' is unexpectedly empty." );
      }
   }

}
