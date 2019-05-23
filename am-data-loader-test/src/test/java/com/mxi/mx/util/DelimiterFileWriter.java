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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;


/**
 * This class is create the CSV file in specific location by passing header, records and file name.
 *
 * @author Karan Tandon
 */
public class DelimiterFileWriter {

   private FileWriter iFileWriter;
   private String iPath;
   private String iFileName;
   private String iDelimiter;


   public DelimiterFileWriter(String aDelimiter, String aPath, String aFileName)
         throws IOException {
      iPath = aPath;
      iFileName = aFileName;
      iDelimiter = aDelimiter;
   }


   public void writeFile( List<String> aHeader, List<String> aRecord ) throws IOException {

      new File( iPath ).mkdirs();
      iFileWriter = new FileWriter( iPath + iFileName );
      writeLine( aHeader );
      writeLine( aRecord );
      iFileWriter.close();
   }


   // Map keys to the values
   public void writeFile( Map<String, String> aMap ) throws IOException {

      new File( iPath ).mkdirs();
      iFileWriter = new FileWriter( iPath + iFileName );
      writeLine( new ArrayList<String>( aMap.keySet() ) );
      writeLine( new ArrayList<String>( aMap.values() ) );
      iFileWriter.close();
   }


   private void writeLine( List<String> aRecord ) throws IOException {

      String lRecord = convertArrayToCSVString( aRecord );
      iFileWriter.write( lRecord );
      // separate headers and records
      iFileWriter.append( '\n' );
   }


   /**
    * This method converts array into string
    *
    * @param aHeader
    * @return
    */
   private String convertArrayToCSVString( List<String> aHeader ) {

      String lResult = "";
      for ( String lElement : aHeader ) {
         lResult += lElement + iDelimiter;
      }

      int length = lResult.length();
      lResult = lResult.substring( 0, length - 1 );

      return lResult;
   }
}
