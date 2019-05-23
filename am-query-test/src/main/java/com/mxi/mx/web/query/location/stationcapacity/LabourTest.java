
package com.mxi.mx.web.query.location.stationcapacity;

import java.util.Date;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.XmlLoader;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AircraftKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * Tests the query com.mxi.mx.web.query.location.stationcapacity.Labour
 *
 * @author jcimino
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class LabourTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * <p>
    * Tests the scenario:
    *
    * <ol>
    * <li>Airport YOW has a line capacity shift scheduled on 01-JAN-2006</li>
    * <li>An aircraft is located at YYZ/LINE with a scheduled overnight check overlapping the
    * shift</li>
    * <li>The check has one requirement as the root task under the check.</li>
    * </ol>
    * </p>
    *
    * <p>
    * Expected Outcome: The query should have one row detailing the labour details for the
    * requirement.
    * </p>
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testQuery() throws Exception {

      // Associate loose JIC tasks with labour requirements to a check
      StationCapacityData.assignTaskToCheck(
            StationCapacityData.Task.Labour.TASK_WITH_AVAILABLE_LABOUR_ON_DAY_1,
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1,
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1_REQUIREMENT );
      StationCapacityData.assignTaskToCheck(
            StationCapacityData.Task.Labour.TASK_WITH_UNAVAILABLE_LABOUR_ON_DAY_1,
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1,
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1_REQUIREMENT );

      // Execute the query
      DataSet lDs = execute( StationCapacityData.Location.YOW_LINE, StationCapacityData.DAY_DT_1,
            StationCapacityData.User.HR_1 );

      // Ensure one row is returned - for the single requirement (even if multiple jobcards)
      MxAssert.assertEquals( "Number of retrieved rows", 1, lDs.getRowCount() );

      // Assert that the 1st row
      assertRow( lDs, StationCapacityData.Aircraft.AC_1,
            StationCapacityData.Aircraft.AC_1_DESCRIPTION,
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1,
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1_SDESC,
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1_REQUIREMENT,
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1_REQUIREMENT_SDESC );

      // unassign the jobcards from the requirement
      StationCapacityData.unassignTaskFromCheck(
            StationCapacityData.Task.Labour.TASK_WITH_AVAILABLE_LABOUR_ON_DAY_1 );
      StationCapacityData.unassignTaskFromCheck(
            StationCapacityData.Task.Labour.TASK_WITH_UNAVAILABLE_LABOUR_ON_DAY_1 );
   }


   /**
    * Creates the test data.
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @BeforeClass
   public static void loadData() throws Exception {
      XmlLoader.load( sDatabaseConnectionRule.getConnection(), LabourTest.class,
            new StationCapacityData().getDataFile() );
   }


   /**
    * Asserts that the expected content of a row matches the actual content in the next row of the
    * dataset (ie: this method calls next on the callee's behalf).
    *
    * @param aDataSet
    *           the dataset
    * @param aAircraft
    *           the aircraft
    * @param aAircraftSdesc
    *           the aircraft description
    * @param aCheck
    *           the check
    * @param aCheckSdesc
    *           the check description
    * @param aRootTask
    *           the root task
    * @param aRootTaskSdesc
    *           the root task description
    *
    * @throws Exception
    *            if an error occurs
    */
   private void assertRow( DataSet aDataSet, AircraftKey aAircraft, String aAircraftSdesc,
         TaskKey aCheck, String aCheckSdesc, TaskKey aRootTask, String aRootTaskSdesc )
         throws Exception {
      aDataSet.next();
      MxAssert.assertEquals( "aircraft_key", aAircraft,
            aDataSet.getKey( AircraftKey.class, "aircraft_key" ) );
      MxAssert.assertEquals( "inv_no_sdesc", aAircraftSdesc, aDataSet.getString( "inv_no_sdesc" ) );
      MxAssert.assertEquals( "check_key", aCheck, aDataSet.getKey( TaskKey.class, "check_key" ) );
      MxAssert.assertEquals( "check_sdesc", aCheckSdesc, aDataSet.getString( "check_sdesc" ) );
      MxAssert.assertEquals( "root_task_key", aRootTask,
            aDataSet.getKey( TaskKey.class, "root_task_key" ) );
      MxAssert.assertEquals( "root_task_sdesc", aRootTaskSdesc,
            aDataSet.getString( "root_task_sdesc" ) );
   }


   /**
    * Executes the query.
    *
    * @param aLocation
    *           location
    * @param aDate
    *           date
    * @param aHumanResource
    *           the hr
    *
    * @return the result
    */
   private DataSet execute( LocationKey aLocation, Date aDate, HumanResourceKey aHumanResource ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aLocation, new String[] { "aLocDbId", "aLocId" } );
      lArgs.add( "aDate", aDate );
      lArgs.add( aHumanResource, new String[] { "aHrDbId", "aHrId" } );

      // Execute the query
      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
