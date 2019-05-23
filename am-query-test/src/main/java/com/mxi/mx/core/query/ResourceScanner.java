
package com.mxi.mx.core.query;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.hamcrest.Matcher;


/**
 * This class allows us to scan resources
 */
public final class ResourceScanner {

   private ResourceScanner() {
      // Do not initialize
   }


   /**
    * Gets all the resources that match the provided file matcher
    *
    * @param aClassLoader
    *           the class loader
    * @param aFileMatcher
    *           the file matcher
    * @return the resources
    * @throws IOException
    */
   public static List<Path> getResources( ClassLoader aClassLoader, Matcher<String> aFileMatcher )
         throws IOException {
      List<Path> lResources = new ArrayList<Path>();
      Enumeration<URL> lClasspathResources = aClassLoader.getResources( "com/mxi" );
      while ( lClasspathResources.hasMoreElements() ) {
         URL lClasspathResource = lClasspathResources.nextElement();
         Path lFile = getResourcePath( lClasspathResource, Paths.get( "../.." ) );
         if ( Files.isDirectory( lFile ) ) {
            lResources.addAll( getDirectoryResources( lFile, aFileMatcher ) );
         } else {
            lResources.addAll( getJarResources( lFile, aFileMatcher ) );
         }
      }

      return lResources;
   }


   /**
    * Gets the resource path for a URL
    *
    * @param aClasspathResource
    *           the URL
    * @param aRelativizeRootDirectory
    *           the relative URL to the root directory
    * @return the resources
    */
   private static Path getResourcePath( URL aClasspathResource, Path aRelativizeRootDirectory ) {
      try {
         // Get path for directory root resource
         return new File( aClasspathResource.toURI() ).toPath().resolve( aRelativizeRootDirectory );
      } catch ( IllegalArgumentException ex ) {
         // Get path for jar root resource
         String lFullFileName = aClasspathResource.getFile();
         lFullFileName = lFullFileName.replaceFirst( "\\.jar\\!.*", ".jar" );
         lFullFileName = lFullFileName.replaceFirst( "file:", "" );
         if ( lFullFileName.startsWith( "/" ) && !lFullFileName.startsWith( "//" ) ) {
            lFullFileName = lFullFileName.substring( 1 );
         }
         return Paths.get( lFullFileName );
      } catch ( URISyntaxException e ) {
         throw new RuntimeException( "Couldn't find root resource", e );
      }
   }


   /**
    * Gets all the resources in the directory
    *
    * @param aDirectory
    *           the directory
    * @param aFileMatcher
    *           the file matcher
    * @return list of paths
    * @throws IOException
    */
   private static List<Path> getDirectoryResources( final Path aDirectory,
         final Matcher<String> aFileMatcher ) throws IOException {
      final List<Path> lResources = new ArrayList<Path>();
      Files.walkFileTree( aDirectory, new SimpleFileVisitor<Path>() {

         @Override
         public FileVisitResult visitFile( Path aFile, BasicFileAttributes aAttrs )
               throws IOException {
            if ( aFileMatcher.matches( aFile.toString() ) ) {
               lResources.add( aDirectory.relativize( aFile ) );
            }
            return FileVisitResult.CONTINUE;
         }
      } );

      return lResources;
   }


   /**
    * Gets all resources within a jar
    *
    * @param aFile
    *           the file
    * @param aFileMatcher
    *           the file matcher
    * @return list of paths
    * @throws IOException
    */
   private static List<Path> getJarResources( final Path aFile, final Matcher<String> aFileMatcher )
         throws IOException {
      final List<Path> lResources = new ArrayList<Path>();
      // Get all Jar file entries, and iterate over them
      try ( JarFile lJarFile = new JarFile( aFile.toFile() ) ) {
         Enumeration<JarEntry> lJarEntries = lJarFile.entries();
         while ( lJarEntries.hasMoreElements() ) {
            String lJarEntry = lJarEntries.nextElement().getName();
            if ( aFileMatcher.matches( lJarEntry ) ) {
               lResources.add( Paths.get( lJarEntry ) );
            }
         }

         return lResources;
      }
   }
}
