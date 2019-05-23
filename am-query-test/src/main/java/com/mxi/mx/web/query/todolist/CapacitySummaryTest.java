
package com.mxi.mx.web.query.todolist;

import java.util.Date;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.db.connection.loader.XmlLoader;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.unittest.MxAssert;
import com.mxi.mx.web.query.location.stationcapacity.StationCapacityData;


/**
 * Tests the query com.mxi.mx.web.query.todolist.CapacitySummary
 *
 * @author jcimino
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class CapacitySummaryTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Tests the query under the following data setup:<br>
    * 1. There are multiple checks assigned to multiple locations on the date<br>
    * 2. There are no capacity errors / warnings
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testQueryMultipleLocationsMultipleChecksNoErrors() throws Exception {

      // execute the query
      DataSet lDs = execute( StationCapacityData.DAY_DT_2, null );

      // There should be 1 row
      MxAssert.assertEquals( "Number of retrieved rows", 2, lDs.getRowCount() );

      // check row contents
      lDs.next();
      testRow( lDs, StationCapacityData.Location.YOW_LINE,
            StationCapacityData.Location.YOW_LINE_CODE,
            StationCapacityData.Location.YOW_LINE_CODE_NAME, StationCapacityData.DAY_DT_2, 2, 1, 1,
            1 );

      // check row contents
      lDs.next();
      testRow( lDs, StationCapacityData.Location.YYZ_LINE,
            StationCapacityData.Location.YYZ_LINE_CODE,
            StationCapacityData.Location.YYZ_LINE_CODE_NAME, StationCapacityData.DAY_DT_2, 2, 1, 1,
            1 );
   }


   /**
    * Tests the query under the following data setup:<br>
    * 1. There are multiple checks assigned to multiple locations on the date<br>
    * 2. There are no capacity errors / warnings<br>
    * 3. We only want to check YOW for capacity (simulates the location being selected on the to do
    * list)
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testQueryMultipleLocationsMultipleChecksOnlyCheckYOWForCapacity() throws Exception {

      // execute the query
      DataSet lDs = execute( StationCapacityData.DAY_DT_2, StationCapacityData.Location.YOW_LINE );

      // There should be 1 row
      MxAssert.assertEquals( "Number of retrieved rows", 1, lDs.getRowCount() );

      // check row contents
      lDs.next();
      testRow( lDs, StationCapacityData.Location.YOW_LINE,
            StationCapacityData.Location.YOW_LINE_CODE,
            StationCapacityData.Location.YOW_LINE_CODE_NAME, StationCapacityData.DAY_DT_2, 2, 1, 1,
            1 );
   }


   /**
    * Tests the query under the following data setup:<br>
    * 1. There is a single check scheduled to YOW on the date<br>
    * 2. There is a labour capacity error
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testQueryOneLocationOneCheckLabourError() throws Exception {

      // assign the part jobcard to the check
      StationCapacityData.assignTaskToCheck(
            StationCapacityData.Task.Part.TASK_WITH_AVAILABLE_PART_ON_DAY_1,
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1,
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1_REQUIREMENT );

      // assign the tool jobcard to the check
      StationCapacityData.assignTaskToCheck(
            StationCapacityData.Task.Tool.TASK_WITH_AVAILABLE_TOOL_ON_DAY_1,
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1,
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1_REQUIREMENT );

      // assign the labour jobcard to the check
      StationCapacityData.assignTaskToCheck(
            StationCapacityData.Task.Labour.TASK_WITH_UNAVAILABLE_LABOUR_ON_DAY_1,
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1,
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1_REQUIREMENT );

      // execute the query
      DataSet lDs = execute( StationCapacityData.DAY_DT_1, null );

      // There should be 1 row
      MxAssert.assertEquals( "Number of retrieved rows", 1, lDs.getRowCount() );

      // check row contents
      lDs.next();
      testRow( lDs, StationCapacityData.Location.YOW_LINE,
            StationCapacityData.Location.YOW_LINE_CODE,
            StationCapacityData.Location.YOW_LINE_CODE_NAME, StationCapacityData.DAY_DT_1, 1, -1, 1,
            1 );

      // unassign the part jobcard from the check
      StationCapacityData.unassignTaskFromCheck(
            StationCapacityData.Task.Part.TASK_WITH_AVAILABLE_BUT_RESERVED_PART_ON_DAY_1 );

      // unassign the tool jobcard from the check
      StationCapacityData.unassignTaskFromCheck(
            StationCapacityData.Task.Tool.TASK_WITH_AVAILABLE_TOOL_ON_DAY_1 );

      // unassign the labour jobcard from the check
      StationCapacityData.unassignTaskFromCheck(
            StationCapacityData.Task.Labour.TASK_WITH_UNAVAILABLE_LABOUR_ON_DAY_1 );
   }


   /**
    * Tests the query under the following data setup:<br>
    * 1. There is a single check scheduled to YOW on the date<br>
    * 2. There is a labour capacity warning
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testQueryOneLocationOneCheckLabourWarning() throws Exception {

      // assign the part jobcard to the check
      StationCapacityData.assignTaskToCheck(
            StationCapacityData.Task.Part.TASK_WITH_AVAILABLE_PART_ON_DAY_1,
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1,
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1_REQUIREMENT );

      // assign the tool jobcard to the check
      StationCapacityData.assignTaskToCheck(
            StationCapacityData.Task.Tool.TASK_WITH_AVAILABLE_TOOL_ON_DAY_1,
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1,
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1_REQUIREMENT );

      // assign the labour jobcard to the check
      StationCapacityData.assignTaskToCheck(
            StationCapacityData.Task.Labour.TASK_WITH_WARNING_LABOUR_ON_DAY_1,
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1,
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1_REQUIREMENT );

      // execute the query
      DataSet lDs = execute( StationCapacityData.DAY_DT_1, null );

      // There should be 1 row
      MxAssert.assertEquals( "Number of retrieved rows", 1, lDs.getRowCount() );

      // check row contents
      lDs.next();
      testRow( lDs, StationCapacityData.Location.YOW_LINE,
            StationCapacityData.Location.YOW_LINE_CODE,
            StationCapacityData.Location.YOW_LINE_CODE_NAME, StationCapacityData.DAY_DT_1, 1, 0, 1,
            1 );

      // unassign the part jobcard from the check
      StationCapacityData.unassignTaskFromCheck(
            StationCapacityData.Task.Part.TASK_WITH_AVAILABLE_BUT_RESERVED_PART_ON_DAY_1 );

      // unassign the tool jobcard from the check
      StationCapacityData.unassignTaskFromCheck(
            StationCapacityData.Task.Tool.TASK_WITH_AVAILABLE_TOOL_ON_DAY_1 );

      // unassign the labour jobcard from the check
      StationCapacityData.unassignTaskFromCheck(
            StationCapacityData.Task.Labour.TASK_WITH_WARNING_LABOUR_ON_DAY_1 );
   }


   /**
    * Tests the query under the following data setup:<br>
    * 1. There is a single check scheduled to YOW on the date<br>
    * 2. There are no capacity errors / warnings
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testQueryOneLocationOneCheckNoErrors() throws Exception {

      // assign the part jobcard to the check
      StationCapacityData.assignTaskToCheck(
            StationCapacityData.Task.Part.TASK_WITH_AVAILABLE_PART_ON_DAY_1,
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1,
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1_REQUIREMENT );

      // assign the tool jobcard to the check
      StationCapacityData.assignTaskToCheck(
            StationCapacityData.Task.Tool.TASK_WITH_AVAILABLE_TOOL_ON_DAY_1,
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1,
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1_REQUIREMENT );

      // assign the labour jobcard to the check
      StationCapacityData.assignTaskToCheck(
            StationCapacityData.Task.Labour.TASK_WITH_AVAILABLE_LABOUR_ON_DAY_1,
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1,
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1_REQUIREMENT );

      // execute the query
      DataSet lDs = execute( StationCapacityData.DAY_DT_1, null );

      // There should be 1 row
      MxAssert.assertEquals( "Number of retrieved rows", 1, lDs.getRowCount() );

      // check row contents
      lDs.next();
      testRow( lDs, StationCapacityData.Location.YOW_LINE,
            StationCapacityData.Location.YOW_LINE_CODE,
            StationCapacityData.Location.YOW_LINE_CODE_NAME, StationCapacityData.DAY_DT_1, 1, 1, 1,
            1 );

      // unassign the part jobcard from the check
      StationCapacityData.unassignTaskFromCheck(
            StationCapacityData.Task.Part.TASK_WITH_AVAILABLE_PART_ON_DAY_1 );

      // unassign the tool jobcard from the check
      StationCapacityData.unassignTaskFromCheck(
            StationCapacityData.Task.Tool.TASK_WITH_AVAILABLE_TOOL_ON_DAY_1 );

      // unassign the labour jobcard from the check
      StationCapacityData.unassignTaskFromCheck(
            StationCapacityData.Task.Labour.TASK_WITH_AVAILABLE_LABOUR_ON_DAY_1 );
   }


   /**
    * Tests the query under the following data setup:<br>
    * 1. There is a single check scheduled to YOW on the date<br>
    * 2. There is a part capacity error
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testQueryOneLocationOneCheckPartError() throws Exception {

      // assign the part jobcard to the check
      StationCapacityData.assignTaskToCheck(
            StationCapacityData.Task.Part.TASK_WITH_AVAILABLE_BUT_RESERVED_PART_ON_DAY_1,
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1,
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1_REQUIREMENT );

      // assign the tool jobcard to the check
      StationCapacityData.assignTaskToCheck(
            StationCapacityData.Task.Tool.TASK_WITH_AVAILABLE_TOOL_ON_DAY_1,
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1,
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1_REQUIREMENT );

      // assign the labour jobcard to the check
      StationCapacityData.assignTaskToCheck(
            StationCapacityData.Task.Labour.TASK_WITH_AVAILABLE_LABOUR_ON_DAY_1,
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1,
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1_REQUIREMENT );

      // execute the query
      DataSet lDs = execute( StationCapacityData.DAY_DT_1, null );

      // There should be 1 row
      MxAssert.assertEquals( "Number of retrieved rows", 1, lDs.getRowCount() );

      // check row contents
      lDs.next();
      testRow( lDs, StationCapacityData.Location.YOW_LINE,
            StationCapacityData.Location.YOW_LINE_CODE,
            StationCapacityData.Location.YOW_LINE_CODE_NAME, StationCapacityData.DAY_DT_1, 1, 1, -1,
            1 );

      // unassign the part jobcard from the check
      StationCapacityData.unassignTaskFromCheck(
            StationCapacityData.Task.Part.TASK_WITH_AVAILABLE_BUT_RESERVED_PART_ON_DAY_1 );

      // unassign the tool jobcard from the check
      StationCapacityData.unassignTaskFromCheck(
            StationCapacityData.Task.Tool.TASK_WITH_AVAILABLE_TOOL_ON_DAY_1 );

      // unassign the labour jobcard from the check
      StationCapacityData.unassignTaskFromCheck(
            StationCapacityData.Task.Labour.TASK_WITH_AVAILABLE_LABOUR_ON_DAY_1 );
   }


   /**
    * Tests the query under the following data setup:<br>
    * 1. There is a single check scheduled to YOW on the date<br>
    * 2. There is a tool capacity error
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testQueryOneLocationOneCheckToolError() throws Exception {

      // assign the part jobcard to the check
      StationCapacityData.assignTaskToCheck(
            StationCapacityData.Task.Part.TASK_WITH_AVAILABLE_PART_ON_DAY_1,
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1,
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1_REQUIREMENT );

      // assign the tool jobcard to the check
      StationCapacityData.assignTaskToCheck(
            StationCapacityData.Task.Tool.TASK_WITH_UNAVAILABLE_TOOL_ON_DAY_1,
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1,
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1_REQUIREMENT );

      // assign the labour jobcard to the check
      StationCapacityData.assignTaskToCheck(
            StationCapacityData.Task.Labour.TASK_WITH_AVAILABLE_LABOUR_ON_DAY_1,
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1,
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1_REQUIREMENT );

      // execute the query
      DataSet lDs = execute( StationCapacityData.DAY_DT_1, null );

      // There should be 1 row
      MxAssert.assertEquals( "Number of retrieved rows", 1, lDs.getRowCount() );

      // check row contents
      lDs.next();
      testRow( lDs, StationCapacityData.Location.YOW_LINE,
            StationCapacityData.Location.YOW_LINE_CODE,
            StationCapacityData.Location.YOW_LINE_CODE_NAME, StationCapacityData.DAY_DT_1, 1, 1, 1,
            -1 );

      // unassign the part jobcard from the check
      StationCapacityData.unassignTaskFromCheck(
            StationCapacityData.Task.Part.TASK_WITH_AVAILABLE_BUT_RESERVED_PART_ON_DAY_1 );

      // unassign the tool jobcard from the check
      StationCapacityData.unassignTaskFromCheck(
            StationCapacityData.Task.Tool.TASK_WITH_UNAVAILABLE_TOOL_ON_DAY_1 );

      // unassign the labour jobcard from the check
      StationCapacityData.unassignTaskFromCheck(
            StationCapacityData.Task.Labour.TASK_WITH_AVAILABLE_LABOUR_ON_DAY_1 );
   }


   /**
    * create the test data
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @BeforeClass
   public static void loadData() throws Exception {
      XmlLoader.load( sDatabaseConnectionRule.getConnection(), CapacitySummaryTest.class,
            StationCapacityData.getDataFile() );
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), CapacitySummaryTest.class );
   }


   /**
    * Execute the query.
    *
    * @param aDate
    *           date
    * @param aLocation
    *           location
    *
    * @return the result
    */
   private DataSet execute( Date aDate, LocationKey aLocation ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aDate", aDate );
      lArgs.add( aLocation, new String[] { "aLocDbId", "aLocId" } );
      lArgs.add( "aCapacityWarningPercent", 0.8 );

      // Execute the query
      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Tests a row in the dataset
    *
    * @param aDs
    *           the dataset
    * @param aLocation
    *           the location
    * @param aLocCd
    *           the location code
    * @param aLocationCdName
    *           the location code / name
    * @param aCapacityDate
    *           the date
    * @param aCheckCount
    *           the check count
    * @param aLabourCapacity
    *           the labour capacity
    * @param aPartCapacity
    *           the part capacity
    * @param aToolCapacity
    *           the tool capacity
    */
   private void testRow( DataSet aDs, LocationKey aLocation, String aLocCd, String aLocationCdName,
         Date aCapacityDate, Integer aCheckCount, Integer aLabourCapacity, Integer aPartCapacity,
         Integer aToolCapacity ) {

      MxAssert.assertEquals( "location_key", aLocation.toString(),
            aDs.getString( "location_key" ) );
      MxAssert.assertEquals( "loc_cd", aLocCd, aDs.getString( "loc_cd" ) );
      MxAssert.assertEquals( "location_cd_name", aLocationCdName,
            aDs.getString( "location_cd_name" ) );
      MxAssert.assertEquals( "capacity_date", aCapacityDate, aDs.getDate( "capacity_date" ) );
      MxAssert.assertEquals( "check_count", aCheckCount, aDs.getInteger( "check_count" ) );
      MxAssert.assertEquals( "labour_capacity", aLabourCapacity,
            aDs.getInteger( "labour_capacity" ) );
      MxAssert.assertEquals( "part_capacity", aPartCapacity, aDs.getInteger( "part_capacity" ) );
      MxAssert.assertEquals( "tool_capacity", aToolCapacity, aDs.getInteger( "tool_capacity" ) );
   }
}
