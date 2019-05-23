
package com.mxi.am.db.connection.loader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;


/**
 * Reads resources.
 */
public final class ResourceReader {

   public static String get( Class<?> aClass, String aResource ) {
      try ( InputStream lInputStream = aClass.getResourceAsStream( aResource ) ) {
         if ( lInputStream == null ) {
            throw new RuntimeException( "Could not load resource: " + aResource );
         }

         try ( Scanner lScanner = new Scanner( lInputStream, StandardCharsets.UTF_8.name() ) ) {
            lScanner.useDelimiter( "\\A" );

            return lScanner.hasNext() ? lScanner.next() : "";
         }
      } catch ( IOException e ) {
         throw new RuntimeException( "Could not load resource: " + aResource, e );
      }
   }
}
