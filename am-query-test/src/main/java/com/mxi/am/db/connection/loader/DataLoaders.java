
package com.mxi.am.db.connection.loader;

import java.sql.Connection;


/**
 * Execute all the data loaders.
 */
public final class DataLoaders {

   /**
    * Each loader provides the facility to load resources with a given file extension.
    */
   enum Loader {
      SQL( ".sql" ) {

         @Override
         public void load( Connection aConnection, Class<?> aClass, String aResource ) {
            SqlLoader.load( aConnection, aClass, aResource );
         }
      },
      XML( ".xml" ) {

         @Override
         public void load( Connection aConnection, Class<?> aClass, String aResource ) {
            XmlLoader.load( aConnection, aClass, aResource );
         }
      },
      YAML( ".yaml" ) {

         @Override
         public void load( Connection aConnection, Class<?> aClass, String aResource ) {
            YamlLoader.load( aConnection, aClass, aResource );
         }
      };

      private final String iSuffix;


      private Loader(String aSuffix) {
         iSuffix = aSuffix;
      }


      public abstract void load( Connection aConnection, Class<?> aClass, String aResource );


      public String getSuffix() {
         return iSuffix;
      }
   }


   private DataLoaders() {
      // utility class
   }


   public static void load( Connection aConnection, Class<?> aClass ) {
      load( aConnection, aClass, aClass.getSimpleName() );
   }


   /**
    * Iterates over the available {@link Loader} implementations to load relevant resources on the
    * provided class's class path. The default expected location for these resource files is in the
    * 'resources' directory with a package name that matches the class's package.
    */
   public static void load( Connection aConnection, Class<?> aClass, String aResourcePrefix ) {
      boolean lResourceLoaded = false;
      Loader[] lLoaders = Loader.values();
      for ( Loader lLoader : lLoaders ) {
         String lResource = aResourcePrefix + lLoader.getSuffix();
         if ( aClass.getResource( lResource ) != null ) {
            lLoader.load( aConnection, aClass, lResource );
            lResourceLoaded = true;
         }
      }
      if ( !lResourceLoaded ) {
         String lLoadersTried = "";
         for ( Loader lLoader : lLoaders ) {
            lLoadersTried += lLoadersTried.isEmpty() ? "'" + lLoader.getSuffix() + "'"
                  : ", '" + lLoader.getSuffix() + "'";
         }
         throw new IllegalArgumentException(
               "No dataloader was able to find a resource file on the classpath of " + aClass
                     + ". Dataloaders looked for the file '" + aResourcePrefix
                     + "' with the following file extensions: " + lLoadersTried + "." );
      }
   }
}
