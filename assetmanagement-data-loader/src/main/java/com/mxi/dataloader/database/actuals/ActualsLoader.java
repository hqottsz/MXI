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
package com.mxi.dataloader.database.actuals;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.mxi.dataloader.api.Loader;
import com.mxi.dataloader.api.LoaderType;
import com.mxi.dataloader.api.settings.BuildDir;
import com.mxi.dataloader.api.settings.ConnectionInfo;
import com.mxi.dataloader.api.settings.Credential;
import com.mxi.idk.command.Command;
import com.mxi.idk.command.CommandExecutor;
import com.mxi.idk.command.Execution;


/**
 * Loads the actuals information into the database
 */
@LoaderType( "dataloader-actuals" )
public class ActualsLoader implements Loader {

   @Inject
   private Logger iLogger;

   @Inject
   private ConnectionInfo iConnectionInfo;

   @Inject
   private Credential iCredential;

   @Inject
   @BuildDir
   private Path iBuildDir;


   /**
    * {@inheritDoc}
    */
   @Override
   public void install() {
      iLogger.info( "Installer already ran on the baseline side" );
   }


   private Path getActualsLoaderDir() {
      return iBuildDir.resolve( "install" );
   }


   /**
    * {@inheritDoc}
    *
    * @throws Exception
    * @throws IOException
    */
   @Override
   public void run( Map<String, String> aSettings ) throws Exception {

      Path lActualsLoaderDir = getActualsLoaderDir();
      for ( String lScript : Arrays.asList( "inventory\\b.validate_inventory.bat",
            "inventory\\c.import_inventory.bat", "task\\b.validate_task.bat",
            "task\\c.import_task.bat", "po\\b.validate_purchase_order.bat",
            "po\\c.import_purchase_order.bat", "odf\\b.validate_odf.bat", "odf\\c.import_odf.bat",
            "workpackage\\b.validate_work_package.bat",
            "workpackage\\c.import_work_package.bat" ) ) {

         Path lScriptToRun = lActualsLoaderDir.resolve( "script_Actuals" ).resolve( lScript );

         Command lCommand = new Command.Builder( lScriptToRun )
               .withWorkingDirectory( lScriptToRun.getParent() ).build();
         try {
            Execution lExecute = new CommandExecutor( iLogger ).execute( lCommand );
            lExecute.redirectOutput( System.out, System.err );
            lExecute.join();
         } catch ( Exception e ) {
            throw new RuntimeException( "Could not execute: " + lScript, e );
         }
         ActualsLoaderResultsHandler lActualsLoaderResultsHandler =
               new ActualsLoaderResultsHandler( iConnectionInfo.getJdbcConnectString(),
                     iCredential.getUsername(), iCredential.getPassword() );

         // checks if a validation failure is found, and throws exception if one is found
         lActualsLoaderResultsHandler.checkValidationResults( lScript );

      }

   }


   @Override
   public void uninstall() {
      iLogger.info( "Uninstaller already ran on the baseline side" );

   }


   private void replacePauseWithExit( Path aCommand ) throws IOException {
      String lFileContents = new String( Files.readAllBytes( aCommand ), StandardCharsets.UTF_8 );
      Files.write( aCommand,
            lFileContents.replaceAll( "pause", "exit /B" ).getBytes( StandardCharsets.UTF_8 ) );
   }
}
