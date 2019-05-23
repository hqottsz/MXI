
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
 * Tests the query com.mxi.mx.web.query.location.stationcapacity.Tasks
 *
 * @author jcimino
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class TasksTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Task class modes codes
    */
   public enum ClassModeCode {
      BLOCK, REQ, JIC
   }

   /**
    * Work type codes
    */
   public enum WorkTypeCode {
      TURN, SERVICE
   }

   /**
    * Task priority codes
    */
   public enum TaskPriorityCode {
      HIGH, LOW, TEST
   }

   /**
    * Schedule priority codes
    */
   public enum SchedulePriorityCode {
      NONE, LOW, HIGH, OD, TEST
   }


   /**
    * Tests the query
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
      MxAssert.assertEquals( "Number of retrieved rows", 2, lDs.getRowCount() );

      // Test the rows
      lDs.setRowNumber( 1 );
      assertRow( lDs, StationCapacityData.Aircraft.AC_1,
            StationCapacityData.Aircraft.AC_1_DESCRIPTION, null,
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1,
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1_SDESC, null, null, null, null,
            null, null, null, null, ClassModeCode.BLOCK, null, false );
      lDs.setRowNumber( 2 );
      assertRow( lDs, StationCapacityData.Aircraft.AC_1,
            StationCapacityData.Aircraft.AC_1_DESCRIPTION,
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1,
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1_REQUIREMENT,
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1_REQUIREMENT_SDESC, null, null,
            null, null, null, null, null, null, ClassModeCode.REQ, null, false );

      // unassign the jobcards from the requirement
      StationCapacityData.unassignTaskFromCheck(
            StationCapacityData.Task.Labour.TASK_WITH_AVAILABLE_LABOUR_ON_DAY_1 );
      StationCapacityData.unassignTaskFromCheck(
            StationCapacityData.Task.Labour.TASK_WITH_UNAVAILABLE_LABOUR_ON_DAY_1 );
   }


   /**
    * Create the test data.
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @BeforeClass
   public static void loadData() throws Exception {
      XmlLoader.load( sDatabaseConnectionRule.getConnection(), TasksTest.class,
            new StationCapacityData().getDataFile() );
   }


   /**
    * Asserts that the expected content of a row mathces the actual content in the next row of the
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
    * @param aRootTask
    *           the root task
    * @param aRootTaskSdesc
    *           the root task description
    * @param aWorkType
    *           the work type
    * @param aTaskPriority
    *           the task priority
    * @param aSchedulePriority
    *           the schedule priority
    * @param aDeviationQt
    *           the deadline date deviation in days
    * @param aUsageRemainingQt
    *           the usage remaining
    * @param aDeadlineDate
    *           the deadline date
    * @param aDomainType
    *           the usage deadline domain type
    * @param aEngineeringUnit
    *           the usage deadline unit
    * @param aClassMode
    *           the class mode
    * @param aMultiplierQt
    *           the multiplier for the deadline deviation
    * @param aHasAuthority
    *           true if the user has authority over the aircraft, false otherwise
    *
    * @throws Exception
    *            if an error occurs
    */
   private void assertRow( DataSet aDataSet, AircraftKey aAircraft, String aAircraftSdesc,
         TaskKey aCheck, TaskKey aRootTask, String aRootTaskSdesc, WorkTypeCode aWorkType,
         TaskPriorityCode aTaskPriority, SchedulePriorityCode aSchedulePriority,
         Double aDeviationQt, Double aUsageRemainingQt, Date aDeadlineDate, String aDomainType,
         String aEngineeringUnit, ClassModeCode aClassMode, Double aMultiplierQt,
         boolean aHasAuthority ) throws Exception {
      MxAssert.assertEquals( "aircraft_key", aAircraft,
            aDataSet.getKey( AircraftKey.class, "aircraft_key" ) );
      MxAssert.assertEquals( "inv_no_sdesc", aAircraftSdesc, aDataSet.getString( "inv_no_sdesc" ) );
      MxAssert.assertEquals( "check_key", aCheck, aDataSet.getKey( TaskKey.class, "check_key" ) );
      MxAssert.assertEquals( "root_task_key", aRootTask,
            aDataSet.getKey( TaskKey.class, "root_task_key" ) );
      MxAssert.assertEquals( "work_type_cd", aWorkType, aDataSet.getString( "work_type_cd" ) );
      MxAssert.assertEquals( "task_priority_cd", aTaskPriority,
            aDataSet.getString( "task_priority_cd" ) );
      MxAssert.assertEquals( "sched_priority_cd", aSchedulePriority,
            aDataSet.getString( "sched_priority_cd" ) );

      MxAssert.assertEquals( "deviation_qt", aDeviationQt,
            aDataSet.getDoubleObj( "deviation_qt" ) );
      MxAssert.assertEquals( "usage_rem_qt", aUsageRemainingQt,
            aDataSet.getDoubleObj( "usage_rem_qt" ) );
      MxAssert.assertEquals( "sched_dead_dt", aDeadlineDate, aDataSet.getDate( "sched_dead_dt" ) );

      MxAssert.assertEquals( "domain_type_cd", aDomainType,
            aDataSet.getString( "domain_type_cd" ) );
      MxAssert.assertEquals( "eng_unit_cd", aEngineeringUnit, aDataSet.getString( "eng_unit_cd" ) );
      MxAssert.assertEquals( "eng_unit_mult_qt", aMultiplierQt,
            aDataSet.getDoubleObj( "eng_unit_mult_qt" ) );
      MxAssert.assertEquals( "class_mode_cd", aClassMode, aDataSet.getString( "class_mode_cd" ) );
   }


   /**
    * Execute the query.
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
