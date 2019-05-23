/*
 * Confidential, proprietary and/or trade secret information of Mxi Technologies, Ltd.
 *
 * Copyright 2000-2015 Mxi Technologies, Ltd. All Rights Reserved.
 *
 * Except as expressly provided by written license signed by a duly appointed officer of Mxi
 * Technologies, Ltd., any disclosure, distribution, reproduction, compilation, modification,
 * creation of derivative works and/or other use of the Mxi source code is strictly prohibited.
 * Inclusion of a copyright notice shall not be taken to indicate that the source code has been
 * published.
 */

package com.mxi.mx.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.List;


/**
 * This class manages any call to a batch file.
 *
 * @author Andrew Bruce
 */

public class BatchFileManager {

   /**
    * Run import process and verify the results
    *
    * @throws Exception
    */
   public void importAircraft() throws Exception {

      // run batch script
      runAcftBatchFile( "d.import.bat", "" );
   }


   /**
    * Stage the Data so we can run the batch file
    *
    * @param aDataFileName
    *
    * @return
    *
    * @throws FileNotFoundException
    * @throws IOException
    */
   public Result copyDataToDatafileDir( String aDataFileName )
         throws FileNotFoundException, IOException {
      String lSource = TestConstants.TEST_CASE_DATA + aDataFileName;
      String lDest = TestConstants.ACFT_DATA_FILE_PATH + aDataFileName;

      File lIndex = new File( TestConstants.ACFT_DATA_FILE_PATH );

      Result lStageBatchFileResult = new Result();

      // delete existing files in data folder
      String[] lEntries = lIndex.list();

      if ( ( lEntries != null ) && !( lEntries.length == 0 ) ) {
         for ( String s : lEntries ) {
            File lCurrentFile = new File( lIndex.getPath(), s );
            lCurrentFile.delete();
         }
         lIndex.delete();
      }

      // creates the directory that holds the data file
      new File( TestConstants.ACFT_DATA_FILE_PATH ).mkdirs();

      InputStream lInput = new FileInputStream( lSource );
      OutputStream lOutput = new FileOutputStream( lDest );

      try {

         byte[] lBuf = new byte[1024];
         int lBytesRead;
         while ( ( lBytesRead = lInput.read( lBuf ) ) > 0 ) {
            lOutput.write( lBuf, 0, lBytesRead );
         }
      } catch ( Exception e ) {
         System.out.println( e.toString() );
         lStageBatchFileResult.failed();
         lStageBatchFileResult.setMessage( e.toString() );
      } finally {
         lInput.close();
         lOutput.close();
      }

      return lStageBatchFileResult;
   }


   /**
    * Execute the load Aircraft batch file
    *
    * @param aDataFileName
    *
    *
    * @throws Exception
    *
    */
   public void loadAircraft( String aDataFileName ) throws Exception {
      runAcftBatchFile( "b.load_aircraft.bat", aDataFileName );
   }


   public void loadTasksViaDataFile( String aDataFileName ) throws Exception {
      runTaskBatchFile( "a.load_task.bat", aDataFileName );
   }


   /**
    * Execute the load odf batch file
    *
    * @param aDataFileName
    *
    *
    *
    *
    */
   public void loadODFViaDataFile( String aDataFileName ) {
      runODFBatchFile( "a.load_odf.bat", aDataFileName );
   }


   /**
    * Execute the load work_package batch file
    *
    * @param aDataFileName
    *
    *
    *
    *
    */
   public void loadWORKPACKAGEViaDataFile( String aDataFileName ) {
      runWORKPACKAGEBatchFile( "a.load_work_package.bat", aDataFileName );
   }


   /**
    * Runs specified batch file
    *
    * @param aFileLocation
    *           - Folder path for target batch file
    * @param aBatchFileName
    *           - Name of batch file to be run
    * @param aInputFileName
    *           - Name of file to be passed to batch file as argument
    *
    * @return Result - Result of executing batch file
    * @throws Exception
    */
   private void runAcftBatchFile( String aBatchFileName, String aInputFileName ) throws Exception {

      runBatchFile( TestConstants.ACFT_BATCH_FILE_PATH, aBatchFileName, aInputFileName );

   }


   /**
    * Runs specified batch file
    *
    * @param aFileLocation
    *           - Folder path for target batch file
    * @param aBatchFileName
    *           - Name of batch file to be run
    * @param aInputFileName
    *           - Name of file to be passed to batch file as argument
    *
    * @return Result - Result of executing batch file
    * @throws Exception
    */
   private void runBatchFile( String aPath, String aBatchFileName, String aInputFileName )
         throws Exception {

      List<String> lCmdAndArgs = Arrays.asList( "cmd", "/c", aBatchFileName );

      ProcessBuilder lBuilder = new ProcessBuilder( lCmdAndArgs );

      lBuilder.directory( new File( aPath ).getAbsoluteFile() );

      // re-direct error stream to input stream
      lBuilder.redirectErrorStream( true );

      Process lProcess = lBuilder.start();

      // setup stream reader and writer
      BufferedReader lReader =
            new BufferedReader( new InputStreamReader( lProcess.getInputStream() ) );
      BufferedWriter lWriter =
            new BufferedWriter( new OutputStreamWriter( lProcess.getOutputStream() ) );

      for ( String lLine = lReader.readLine(); lLine != null; lLine = lReader.readLine() ) {
         System.out.println( lLine );

         // when prompted for input
         if ( lLine.contains( "enter the filename" ) ) {

            // put filename into batch file
            lWriter.write( aInputFileName );
            lWriter.newLine();
            lWriter.flush();
            System.out.println( aInputFileName );
         } else if ( lLine.contains( "any key to continue" ) ) {

            // mark process as successful
            System.out.println(
                  "Finished processing " + aBatchFileName + " for file: " + aInputFileName );
            lWriter.write( "\r\n" );
            lWriter.newLine();
            lWriter.flush();

            break;
         }
      }

      lProcess.waitFor();

      if ( lProcess.exitValue() == 0 ) {

         // mark process as successful
         System.out
               .println( "Finished processing " + aBatchFileName + " for file: " + aInputFileName );
      } else {

         // mark process as failure
         throw new Exception( "Processing of " + aBatchFileName + " failed. Exit code: "
               + String.valueOf( lProcess.exitValue() ) );
      }

   }


   private void runTaskBatchFile( String aBatchFileName, String aInputFileName ) throws Exception {
      runBatchFile( TestConstants.TASK_BATCH_FILE, aBatchFileName, aInputFileName );
   }


   /**
    * Run the ODF Batch File
    *
    *
    *
    */
   public void runODFBatchFile( String aBatchFileName, String aInputFileName ) {
      try {
         runBatchFile( TestConstants.ODF_BATCH_FILE, aBatchFileName, aInputFileName );
      } catch ( Exception e ) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }


   /**
    * Run the WORK_PACKAGE Batch File
    *
    *
    *
    */
   public void runWORKPACKAGEBatchFile( String aBatchFileName, String aInputFileName ) {
      try {
         runBatchFile( TestConstants.WPK_BATCH_FILE, aBatchFileName, aInputFileName );
      } catch ( Exception e ) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }


   /**
    * Run the Validate Acft Batch File
    *
    * @throws Exception
    *
    */
   public void validateAcftViaBatch() throws Exception {
      runAcftBatchFile( "c.validate.bat", "" );
   }


   /**
    * Run the Validate ODF Batch File
    *
    *
    *
    */
   public void validateODFViaBatch() {

      runODFBatchFile( "b.validate_odf.bat", "" );
   }


   /**
    * Run the Validate work_package Batch File
    *
    *
    *
    */
   public void validateWORKPACKAGEViaBatch() {

      runWORKPACKAGEBatchFile( "b.validate_work_package.bat", "" );
   }


   /**
    * Run the Import ODF Batch File
    *
    *
    *
    */
   public void importODFViaBatch() {

      runODFBatchFile( "c.import_odf.bat", "" );
   }


   /**
    * Run the Import ODF Batch File
    *
    *
    *
    */
   public void importWORKPACKAGEViaBatch() {

      runWORKPACKAGEBatchFile( "c.import_work_package.bat", "" );
   }


   /**
    * Copy test data file to build folder
    *
    * @param aSourceDir
    * @param aDataFileName
    * @param aDestDir
    *
    * @return Result
    *
    * @throws FileNotFoundException
    * @throws IOException
    */
   public Result copyFile( String aSourceDir, String aDataFileName, String aDestDir )
   // throws FileNotFoundException, IOException
   {
      String lSource = aSourceDir + aDataFileName;
      String lDest = aDestDir + aDataFileName;

      File ldeleteFile = new File( lDest );
      if ( ldeleteFile.exists() ) {
         ldeleteFile.delete();

      }

      Result lStageBatchFileResult = new Result();
      InputStream lInput = null;
      OutputStream lOutput = null;

      try {

         lInput = new FileInputStream( lSource );
         lOutput = new FileOutputStream( lDest );

         byte[] lBuf = new byte[1024];
         int lBytesRead;
         while ( ( lBytesRead = lInput.read( lBuf ) ) > 0 ) {
            lOutput.write( lBuf, 0, lBytesRead );
         }

         lInput.close();
         lOutput.close();

      } catch ( Exception e ) {
         System.out.println( e.toString() );
         lStageBatchFileResult.failed();
         lStageBatchFileResult.setMessage( e.toString() );
      }

      return lStageBatchFileResult;
   }


   /**
    * Execute the load historical usage batch file
    *
    *
    *
    *
    */
   public void loadHistoricalUsageViaDataFile( String aDataFileName ) {
      runHistoricalUsageBatchFile( "a.load_usage.bat", aDataFileName );
   }


   /**
    * Run the load Purchase Order CSV via Batch File
    *
    * @param aCSVFile
    *           - name of the CSV file that will be loaded into the DB
    *
    */
   public void loadPurchaseOrderViaBatch() {

      runPoBatchFile( "a.load_purchase_order.bat", "" );

   }


   /**
    * Run the Historical usage Batch File
    *
    *
    *
    */
   public void runHistoricalUsageBatchFile( String aBatchFileName, String aInputFileName ) {
      try {
         runBatchFile( TestConstants.HISTORICAL_USAGE_BATCH_FILE, aBatchFileName, aInputFileName );
      } catch ( Exception e ) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }


   /**
    * Run the Validate Purchase Order via Batch File
    *
    *
    */
   public void validatePurchaseOrderViaBatch() {

      runPoBatchFile( "b.validate_purchase_order.bat", "" );

   }


   /**
    * Run the Validate historical usage batch File
    *
    *
    *
    */
   public void validateHistoricalUsageViaBatch() {

      runHistoricalUsageBatchFile( "b.validate_usage.bat", "" );
   }


   /**
    * Run the Import Purchase Order via Batch File
    *
    *
    */
   public void importPurchaseOrderViaBatch() {

      runPoBatchFile( "c.import_purchase_order.bat", "" );
   }


   /**
    * Run the import historical usage batch File
    *
    *
    *
    */

   public void importHistoricalUsageViaBatch() {

      runHistoricalUsageBatchFile( "c.import_usage.bat", "" );
   }


   /**
    * Run the ODF Batch File
    *
    * @throws Exception
    *
    */

   public void runPoBatchFile( String aBatchFileName, String aInputFileName ) {
      try {
         runBatchFile( TestConstants.PO_BATCH_FOLDER, aBatchFileName, aInputFileName );
      } catch ( Exception e ) {
         e.printStackTrace();
      }
   }

}
