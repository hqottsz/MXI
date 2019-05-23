/*
 * Confidential, proprietary and/or trade secret information of Mxi Technologies, Ltd.
 * 
 * Copyright 2000-2016 Mxi Technologies, Ltd. All Rights Reserved.
 * 
 * Except as expressly provided by written license signed by a duly appointed officer of Mxi
 * Technologies, Ltd., any disclosure, distribution, reproduction, compilation, modification,
 * creation of derivative works and/or other use of the Mxi source code is strictly prohibited.
 * Inclusion of a copyright notice shall not be taken to indicate that the source code has been
 * published.
 */
package com.mxi.dataloader.database;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public final class ZipExtractor {

   private ZipExtractor() {
      // utility class
   }


   /**
    * Extracts contents of a zip to the specified destination
    */
   public static void unzip( Path aZipFile, Path aDestinationDirectory ) throws IOException {
      Files.createDirectories( aDestinationDirectory );

      try ( InputStream lFileInputStream = Files.newInputStream( aZipFile ) ) {
         extract( aDestinationDirectory, lFileInputStream );
      }
   }


   /**
    * Extracts contents of a zip (in yourw resources) to the specified destination
    */
   public static void unzip( Class<?> aClass, String aResource, Path aDestinationDirectory )
         throws IOException {
      Files.createDirectories( aDestinationDirectory );

      try ( InputStream lFileInputStream = aClass.getResourceAsStream( aResource ) ) {
         extract( aDestinationDirectory, lFileInputStream );
      }
   }


   private static void extract( Path aDestinationDirectory, InputStream aFileInputStream )
         throws IOException {
      try ( ZipInputStream lZipInputStream = new ZipInputStream( aFileInputStream ) ) {
         ZipEntry lEntry = lZipInputStream.getNextEntry();
         // Iterate over all entries
         while ( lEntry != null ) {
            Path lExtractedZipEntryPath = aDestinationDirectory.resolve( lEntry.getName() );
            if ( lEntry.isDirectory() ) {
               Files.createDirectories( lExtractedZipEntryPath );
            } else {
               Files.createDirectories( lExtractedZipEntryPath.getParent() );
               Files.copy( lZipInputStream, lExtractedZipEntryPath );
            }
            lZipInputStream.closeEntry();
            lEntry = lZipInputStream.getNextEntry();
         }
      }
   }
}
