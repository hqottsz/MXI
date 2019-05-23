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
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;


/**
 * Extracts the database installer from the resources
 */
public final class DatabaseInstallerExtractor {

   private DatabaseInstallerExtractor() {
      // utility class
   }


   public static void extract( Path aInstallDir ) {
      try {
         // Delete if it already exists
         if ( Files.exists( aInstallDir ) ) {
            deleteDirectory( aInstallDir );
         }

         // Extract!
         ZipExtractor.unzip( DatabaseInstallerExtractor.class, "am-data-loader.zip", aInstallDir );
      } catch ( IOException e ) {
         throw new RuntimeException( "Could not extract the database installer to " + aInstallDir,
               e );
      }
   }


   private static void deleteDirectory( Path aInstallDir ) throws IOException {
      Files.walkFileTree( aInstallDir, new SimpleFileVisitor<Path>() {

         @Override
         public FileVisitResult visitFile( Path aFile, BasicFileAttributes aAttrs )
               throws IOException {
            Files.delete( aFile );
            return FileVisitResult.CONTINUE;
         }


         @Override
         public FileVisitResult postVisitDirectory( Path aDir, IOException e ) throws IOException {
            Files.delete( aDir );

            return FileVisitResult.CONTINUE;
         }
      } );
   }
}
