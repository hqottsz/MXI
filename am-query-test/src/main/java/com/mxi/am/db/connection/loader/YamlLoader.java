
package com.mxi.am.db.connection.loader;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;


/**
 * Loads data from a YAML file.
 */
public final class YamlLoader {

   private YamlLoader() {
      // utility class
   }


   public static void load( Connection aConnection, Class<?> aClass, String aResource ) {
      String lYamlFile = ResourceReader.get( aClass, aResource );
      try {
         @SuppressWarnings( "unchecked" )
         List<Map<String, Map<String, String>>> lYamlEntries =
               new ObjectMapper( new YAMLFactory() ).readValue( lYamlFile, List.class );
         for ( Map<String, Map<String, String>> lTableEntry : lYamlEntries ) {
            for ( Entry<String, Map<String, String>> lEntry : lTableEntry.entrySet() ) {
               TableLoader.load( aConnection, lEntry.getKey(), lEntry.getValue() );
            }
         }
      } catch ( IOException e ) {
         throw new RuntimeException( "Could not load yaml file: " + aResource );
      }
   }
}
