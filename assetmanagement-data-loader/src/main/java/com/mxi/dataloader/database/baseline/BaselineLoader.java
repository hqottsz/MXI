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
package com.mxi.dataloader.database.baseline;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Properties;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.mxi.dataloader.api.Loader;
import com.mxi.dataloader.api.LoaderType;
import com.mxi.dataloader.api.settings.BuildDir;
import com.mxi.dataloader.api.settings.ConnectionInfo;
import com.mxi.dataloader.api.settings.Credential;
import com.mxi.dataloader.database.DatabaseInstallerExtractor;
import com.mxi.dataloader.database.plsql.PlsqlPlusExecutor;
import com.mxi.dataloader.database.resource.ResourceLoader;
import com.mxi.idk.command.Command;
import com.mxi.idk.command.CommandExecutor;
import com.mxi.idk.command.CommandFailedException;
import com.mxi.idk.command.Execution;


/**
 * Loads the baseline information into the database
 */
@LoaderType( "dataloader-baseline" )
public class BaselineLoader implements Loader {

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
      iLogger.info( "Extract the database installer" );
      DatabaseInstallerExtractor.extract( iBuildDir.resolve( "install" ) );

      iLogger.info( "Installing the data loader" );

      try {
         // Create settings files for the baseline loader installer
         Path lBaselineLoaderDir = getBaselineLoaderDir();
         Path lSettingsPath = lBaselineLoaderDir.resolve( "runtime.properties" );
         saveSettings( lSettingsPath, iConnectionInfo, iCredential );

         // Install baseline loader
         Path lInstaller = lBaselineLoaderDir.resolve( "install" ).resolve( "install-all.bat" );
         replacePauseWithExit( lInstaller );
         Command lCommand = new Command.Builder( lInstaller )
               .withWorkingDirectory( lBaselineLoaderDir.resolve( "install" ) ).build();
         CommandExecutor lExecutor = new CommandExecutor( iLogger );
         Execution lExecution = lExecutor.execute( lCommand );
         try {
            lExecution.redirectOutput( System.out, System.err ).join();
         } catch ( InterruptedException e ) {
            Thread.currentThread().interrupt();
         }
      } catch ( IOException | CommandFailedException e ) {
         throw new RuntimeException( "Could not install baseline loader", e );
      }
   }


   private void saveSettings( Path aSettingsPath, ConnectionInfo aConnectionInfo,
         Credential aCredential ) throws IOException {
      Properties lInstallProperties = new Properties();
      lInstallProperties.setProperty( "oracle.connect.string", aConnectionInfo.toString() );
      lInstallProperties.setProperty( "oracle.connect.server", aConnectionInfo.getServer() );
      lInstallProperties.setProperty( "oracle.connect.port",
            String.valueOf( aConnectionInfo.getPort() ) );
      lInstallProperties.setProperty( "oracle.connect.sid", aConnectionInfo.getSid() );
      lInstallProperties.setProperty( "maintenix.username", aCredential.getUsername() );
      lInstallProperties.setProperty( "maintenix.password", aCredential.getPassword() );
      lInstallProperties.setProperty( "install.failonerror", "true" );
      lInstallProperties.setProperty( "auto.create.task.bool", "false" );
      lInstallProperties.setProperty( "load.db.id", "" );
      lInstallProperties.setProperty( "available.thread", "4" );
      lInstallProperties.setProperty( "acft.limit.per.session", "4" );
      lInstallProperties.setProperty( "comp.limit.per.session", "1000" );
      lInstallProperties.setProperty( "complete.assy.bool", "FULL" );
      lInstallProperties.setProperty( "wkflow.cycle.code", "ACTUALSLOADER" );
      lInstallProperties.setProperty( "blt.wf.poll.frequency", "1" );
      lInstallProperties.setProperty( "aldr.module.name", "Maintenix.ActualsLoader" );
      lInstallProperties.setProperty( "pause.batch.file", "false" );
      lInstallProperties.setProperty( "generate.report", "false" );
      lInstallProperties.setProperty( "debug.level", "INFO" );
      lInstallProperties.setProperty( "parallelism.degree", "1" );
      lInstallProperties.setProperty( "parallelism.chunk.size", "10000" );
      lInstallProperties.setProperty( "gather.stats.bool", "0" );
      lInstallProperties.setProperty( "datafile.to.flat.file", "true" );

      try ( OutputStream lInstallPropertisOs = Files.newOutputStream( aSettingsPath ) ) {
         lInstallProperties.store( lInstallPropertisOs,
               "Generated Settings for Data Loader Installation" );
      }
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void run( Map<String, String> aSettings ) {
      String lValidateImportFile = null;
      PlsqlPlusExecutor lPlsqlPlusExecutor = new PlsqlPlusExecutor( iCredential, iConnectionInfo );
      for ( BaselineModule lModule : BaselineModule.values() ) {
         try {
            if ( lModule.name().contains( "bl_kit" )
                  || lModule.name().contains( "bl_deferral_references" )
                  || lModule.name().contains( "bl_calc_import" )
                  || lModule.name().contains( "bl_part_incompat_import" ) ) {
               lValidateImportFile = "validate_import_kit_deferral_calc_incompat.vm";
            } else {
               lValidateImportFile = "validate_import.vm";
            }
            lPlsqlPlusExecutor.execute( "BaselineValidateImport_" + lModule.name(),
                  ResourceLoader.load( getClass(), lValidateImportFile )
                        .replace( "${package_name}", lModule.name() )
                        .replace( "${validate_method}", lModule.getValidateMethod() )
                        .replace( "${import_method}", lModule.getImportMethod() ) );
         } catch ( RuntimeException e ) {
            throw new RuntimeException( "Could not validate-import: " + lModule.name(), e );
         }
      }
   }


   @Override
   public void uninstall() {
      iLogger.info( "Uninstalling the baseline loader" );
      try {
         // Create settings files for the baseline loader installer
         Path lBaselineLoaderDir = getBaselineLoaderDir();
         Path lSettingsPath = lBaselineLoaderDir.resolve( "build.properties" );
         saveSettings( lSettingsPath, iConnectionInfo, iCredential );

         // Uninstall baseline loader
         Path lUninstaller = lBaselineLoaderDir.resolve( "deinstall" ).resolve( "remove-all.bat" );
         replacePauseWithExit( lUninstaller );

         Command lCommand = new Command.Builder( lUninstaller )
               .withWorkingDirectory( lBaselineLoaderDir.resolve( "deinstall" ) ).build();
         CommandExecutor lExecutor = new CommandExecutor( iLogger );
         Execution lExecution = lExecutor.execute( lCommand );

         lExecution.redirectOutput( System.out, System.err ).join();
      } catch ( IOException | CommandFailedException e ) {
         throw new RuntimeException( "Could not uninstall baseline loader", e );
      } catch ( InterruptedException e ) {
         Thread.currentThread().interrupt();
      }
   }


   private void replacePauseWithExit( Path aCommand ) throws IOException {
      String lFileContents = new String( Files.readAllBytes( aCommand ), StandardCharsets.UTF_8 );
      Files.write( aCommand,
            lFileContents.replaceAll( "pause", "exit /B" ).getBytes( StandardCharsets.UTF_8 ) );
   }


   private Path getBaselineLoaderDir() {
      System.out.println( "iBuildDir=" + iBuildDir.toString() );
      return iBuildDir.resolve( "install" );
   }
}
