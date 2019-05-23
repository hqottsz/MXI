package com.mxi.mx.core.maint.plan.actualsloader.task.csv;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import com.mxi.mx.util.ActualsLoaderTest;
import com.mxi.mx.util.DelimiterFileWriter;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.TestConstants;


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

/**
 * *
 * <p>
 * This test suite contain test cases that ensure Tasks and Sched Tasks data in CSV gets loaded into
 * the staging tables.
 * <ul>
 * <li>Here are all fields in C_RI_TASK_sample.CSV which will be tested during the test</li>
 * <ul>
 * <li>SERIAL_NO_OEM</li>
 * <li>PART_NO_OEM</li>
 * <li>MANUFACT_CD</li>
 * <li>TASK_CD</li>
 * <li>COMPLETION_DT</li>
 * <li>COMPLETION_DUE_DT</li>
 * <li>CUSTOM_START_DT</li>
 * <li>CUSTOM_DUE_DT</li>
 * </ul>
 * <li>Here are all fields C_RI_TASK_SCHED_sample.csv which will be tested during the test</li>
 * <ul>
 * <li>SERIAL_NO_OEM</li>
 * <li>PART_NO_OEM</li>
 * <li>MANUFACT_CD</li>
 * <li>TASK_CD</li>
 * <li>COMPLETION_DT</li>
 * <li>SCHED_PARAMETER</li>
 * <li>COMPLETION_VALUE</li>
 * <li>COMPLETION_DUE_VALUE</li>
 * <li>CUSTOM_START_VALUE</li>
 * <li>CUSTOM_DUE_VALUE</li>
 * </ul>
 * </li>
 * </ul>
 * </p>
 * <p>
 * And expects the following
 * <ul>
 * <li>if data in the source csv file and inserted data in staging table does not match, will give
 * break the test</li>
 * </ul>
 * </p>
 */

/**
 *
 * @author Karan Tandon
 *
 */
public class GenerateAndLoadTaskCSV extends ActualsLoaderTest {

   @Test
   public void TesttaskCSVLoading() throws Exception {

      // *** SETUP THE TASK TEST DATA ***
      Map<String, String> lMapTask = new LinkedHashMap<String, String>();
      lMapTask.put( "SERIAL_NO_OEM", "MF4592144" );
      lMapTask.put( "PART_NO_OEM", "CM3245H452" );
      lMapTask.put( "MANUFACT_CD", "BOEING" );
      lMapTask.put( "TASK_CD", "ACFT-SYS-1-1-TRK-P1-REPL" );
      lMapTask.put( "COMPLETION_DT", "03/22/2015" );
      lMapTask.put( "FIRST_TIME_BOOL", "0" );
      lMapTask.put( "COMPLETION_DUE_DT", "04/22/2015" );
      lMapTask.put( "CUSTOM_START_DT", "05/22/2015" );
      lMapTask.put( "CUSTOM_DUE_DT", "06/22/2015" );

      // *** SETUP THE SCHED TASK TEST DATA ***
      Map<String, String> lMapSchedTask = new LinkedHashMap<String, String>();
      lMapSchedTask.put( "SERIAL_NO_OEM", "MF4592144" );
      lMapSchedTask.put( "PART_NO_OEM", "CM3245H452" );
      lMapSchedTask.put( "MANUFACT_CD", "BOEING" );
      lMapSchedTask.put( "TASK_CD", "ACFT-SYS-1-1-TRK-P1-REPL" );
      lMapSchedTask.put( "COMPLETION_DT", "01/01/2015" );
      lMapSchedTask.put( "SCHED_PARAMETER", "1" );
      lMapSchedTask.put( "COMPLETION_VALUE", "1" );
      lMapSchedTask.put( "COMPLETION_DUE_VALUE", "1" );
      lMapSchedTask.put( "CUSTOM_START_VALUE", "1" );
      lMapSchedTask.put( "CUSTOM_DUE_VALUE", "1" );

      // create CSV
      createCSVFile( lMapTask, "C_RI_TASK_SAMPLE.csv" );
      createCSVFile( lMapSchedTask, "C_RI_TASK_SCHED_SAMPLE.csv" );

      // Load CSV data into staging tables
      loadCSVDataFile( "C_RI_TASK_SAMPLE.csv" );
      loadCSVDataFile( "C_RI_TASK_SCHED_SAMPLE.csv" );

      // validating staging table contents
      String lQueryTask = "SELECT * FROM " + TableUtil.C_RI_TASK;
      ResultSet lResultSetTask = runQuery( lQueryTask );
      assertThat( "Query returned no results: ", lResultSetTask != null );
      assertThat( "Expected results but there were none.", lResultSetTask.first() == true );

      String lQuerySchedTask = "SELECT * FROM " + TableUtil.C_RI_TASK_SCHED;
      ResultSet lResultSetSched = runQuery( lQuerySchedTask );
      assertThat( "Query returned no results: ", lQuerySchedTask != null );
      assertThat( "Expected results but there were none.", lResultSetSched.first() == true );

      // get the required date format and use it in date columns comparison
      SimpleDateFormat dt = new SimpleDateFormat( "MM/dd/YYYY" );

      // check if exact data has inserted into C_RI_TASK
      for ( Entry<String, String> lEntry : lMapTask.entrySet() ) {

         String lKey = lEntry.getKey();
         if ( lKey.matches( "COMPLETION_DT|COMPLETION_DUE_DT|CUSTOM_START_DT|CUSTOM_DUE_DT" ) ) {
            Date lActualValue = lResultSetTask.getDate( lEntry.getKey() );
            String lFormatedDate = dt.format( lActualValue );
            assertTrue( "Expected " + lEntry.getValue() + " but got " + lFormatedDate,
                  lFormatedDate.equals( lEntry.getValue() ) );
         } else {
            String lActualValue = lResultSetTask.getString( lEntry.getKey() );
            assertTrue( "Expected " + lEntry.getValue() + " but got " + lActualValue,
                  lActualValue.equals( lEntry.getValue() ) );
         }
      }
      // check if exact data has inserted into C_RI_TASK_SCHED
      for ( Entry<String, String> lEntry : lMapSchedTask.entrySet() ) {

         String lKey = lEntry.getKey();
         if ( lKey.matches( "COMPLETION_DT" ) ) {
            Date lActualValue = lResultSetSched.getDate( lEntry.getKey() );
            String lFormatedDate = dt.format( lActualValue );
            assertTrue( "Expected " + lEntry.getValue() + " but got " + lFormatedDate,
                  lFormatedDate.equals( lEntry.getValue() ) );
         } else {
            String lActualValue = lResultSetSched.getString( lEntry.getKey() );
            assertTrue( "Expected " + lEntry.getValue() + " but got " + lActualValue,
                  lActualValue.equals( lEntry.getValue() ) );
         }
      }
   }


   /**
    *
    * Ensure that one column data with delimiter do not get separated into another column. This
    * method will test on "SERIAL_NO_OEM" column data
    *
    */
   @Test
   public void testDelimiter() throws Exception {

      // *** SETUP THE TASK TEST DATA ***
      Map<String, String> lMapDelimiterCheck = new LinkedHashMap<String, String>();
      lMapDelimiterCheck.put( "SERIAL_NO_OEM", "\"MF,4592144\"" );
      lMapDelimiterCheck.put( "PART_NO_OEM", "CM3245H452" );
      lMapDelimiterCheck.put( "MANUFACT_CD", "BOEING" );
      lMapDelimiterCheck.put( "TASK_CD", "ACFT-SYS-1-1-TRK-P1-REPL" );
      lMapDelimiterCheck.put( "COMPLETION_DT", "03/22/2015" );
      lMapDelimiterCheck.put( "FIRST_TIME_BOOL", "0" );
      lMapDelimiterCheck.put( "COMPLETION_DUE_DT", "04/22/2015" );
      lMapDelimiterCheck.put( "CUSTOM_START_DT", "05/22/2015" );
      lMapDelimiterCheck.put( "CUSTOM_DUE_DT", "06/22/2015" );

      // create CSV
      createCSVFile( lMapDelimiterCheck, "C_RI_TASK_SAMPLE.csv" );

      // Load CSV data into staging tables
      loadCSVDataFile( "C_RI_TASK_SAMPLE.csv" );

      // validating staging table contents
      String lQueryTask = "SELECT * FROM " + TableUtil.C_RI_TASK;
      ResultSet lResultSetTask = runQuery( lQueryTask );
      assertThat( "Query returned no results: ", lResultSetTask != null );
      assertThat( "Expected results but there were none.", lResultSetTask.first() == true );

      // get the required date format and use it in date columns comparison
      SimpleDateFormat dt = new SimpleDateFormat( "MM/dd/YYYY" );

      // check if exact data has inserted into C_RI_TASK
      for ( Entry<String, String> lEntry : lMapDelimiterCheck.entrySet() ) {

         String lKey = lEntry.getKey();
         String lExpectedValue = lEntry.getValue();
         if ( lKey.matches( "COMPLETION_DT|COMPLETION_DUE_DT|CUSTOM_START_DT|CUSTOM_DUE_DT" ) ) {
            Date lActualValue = lResultSetTask.getDate( lEntry.getKey() );
            String lFormatedDate = dt.format( lActualValue );
            assertTrue( "Expected " + lExpectedValue + " but got " + lFormatedDate,
                  lFormatedDate.equals( lExpectedValue ) );
         } else if ( lKey.matches( "SERIAL_NO_OEM" ) ) {
            // since the data has escaped quotes and we expect the data in the database
            // to have the escaped quotes removed, we need to make sure the data we are
            // asserting does not have quotes so we will remove them.
            String lActualValue = lResultSetTask.getString( lEntry.getKey() );
            lExpectedValue = lExpectedValue.substring( 1, lExpectedValue.length() - 1 );
            assertTrue( "Expected " + lExpectedValue + " but got " + lActualValue,
                  lActualValue.equals( lExpectedValue ) );
         } else {
            String lActualValue = lResultSetTask.getString( lEntry.getKey() );
            assertTrue( "Expected " + lExpectedValue + " but got " + lActualValue,
                  lActualValue.equals( lExpectedValue ) );
         }
      }
   }


   /**
    * Create the CSV file under the specified location in the method
    *
    * @throws SQLException
    */
   private void createCSVFile( Map<String, String> aMap, String aFileName ) throws IOException {

      String lDataFolderPath = TestConstants.TASK_DATA_FILE_PATH;
      DelimiterFileWriter lTaskFileWriter =
            new DelimiterFileWriter( ",", lDataFolderPath, aFileName );
      lTaskFileWriter.writeFile( aMap );
   }

}
