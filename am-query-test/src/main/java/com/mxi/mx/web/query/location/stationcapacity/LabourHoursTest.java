
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
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.sched.SchedStaskTable;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * Tests the query com.mxi.mx.web.query.location.stationcapacity.LabourHours
 *
 * @author jcimino
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class LabourHoursTest {

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
    * <li>The check has two requirements:</li>
    * <ul>
    * <li>Requirement#1 has two jobcards:
    *
    * <ul>
    * <li>one which requires 12 hours of ENG labour</li>
    * <li>one which requires 5 hours of LBR labour</li>
    * </ul>
    * </li>
    * <li>Requirement#2 has one jobcard:
    *
    * <ul>
    * <li>with 9 hours of ENG labour</li>
    * </ul>
    * </li>
    * </ul>
    * </ol>
    * </p>
    *
    * <p>
    * Expected Outcome: The query should have four rows: one row for each labour skill for both the
    * check and the requirement
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testQuery() throws Exception {

      // Create a second requirement on CHECK 1
      EventKey lReq2Event = new EventKey( 4650, 999 );
      EvtEventTable lEvtEvent = EvtEventTable.create( lReq2Event );
      lEvtEvent.setEventSdesc( "requirement 2 on check 1" );
      lEvtEvent.setHEvent( StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1.getEventKey() );
      lEvtEvent.setNhEvent( StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1.getEventKey() );
      lEvtEvent.setHistBool( false );
      lEvtEvent.insert();

      TaskKey lReq2Task = new TaskKey( lReq2Event );
      SchedStaskTable lSchedStask = SchedStaskTable.create( lReq2Task );
      lSchedStask.setTaskClass( RefTaskClassKey.REQ );
      lSchedStask.insert();

      // Associate loose JIC tasks with labour requirements to a check
      StationCapacityData.assignTaskToCheck(
            StationCapacityData.Task.Labour.TASK_WITH_AVAILABLE_LABOUR_ON_DAY_1,
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1,
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1_REQUIREMENT );
      StationCapacityData.assignTaskToCheck(
            StationCapacityData.Task.Labour.TASK_WITH_UNAVAILABLE_LABOUR_ON_DAY_1,
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1,
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1_REQUIREMENT );
      StationCapacityData.assignTaskToCheck(
            StationCapacityData.Task.Labour.TASK_WITH_WARNING_LABOUR_ON_DAY_1,
            StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1, lReq2Task );

      // Execute the query
      DataSet lDs = execute( StationCapacityData.Location.YOW_LINE, StationCapacityData.DAY_DT_1 );

      // Ensure five are returned - the table will have two labour skills for the check, two labour
      // rows for requirement 1 and one labour row for requirement 2

      MxAssert.assertEquals( "Number of retrieved rows", 5, lDs.getRowCount() );

      // Assert that the contents of the 4 rows
      assertRow( lDs, StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1, RefLabourSkillKey.ENG,
            21.0 );
      assertRow( lDs, StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1, RefLabourSkillKey.LBR,
            5.0 );
      assertRow( lDs, StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1_REQUIREMENT,
            RefLabourSkillKey.ENG, 12.0 );
      assertRow( lDs, StationCapacityData.Check.CHECK_ON_AIRCRAFT_1_ON_DAY_1_REQUIREMENT,
            RefLabourSkillKey.LBR, 5.0 );
      assertRow( lDs, lReq2Task, RefLabourSkillKey.ENG, 9.0 );

      // unassign the jobcards from the requirement
      StationCapacityData.unassignTaskFromCheck(
            StationCapacityData.Task.Labour.TASK_WITH_AVAILABLE_LABOUR_ON_DAY_1 );
      StationCapacityData.unassignTaskFromCheck(
            StationCapacityData.Task.Labour.TASK_WITH_UNAVAILABLE_LABOUR_ON_DAY_1 );
      StationCapacityData.unassignTaskFromCheck(
            StationCapacityData.Task.Labour.TASK_WITH_WARNING_LABOUR_ON_DAY_1 );

      // Delete the second requirement
      lSchedStask.delete();
      lEvtEvent.delete();
   }


   /**
    * create the test data
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @BeforeClass
   public static void loadData() throws Exception {
      XmlLoader.load( sDatabaseConnectionRule.getConnection(), LabourHoursTest.class,
            new StationCapacityData().getDataFile() );
   }


   /**
    * Asserts that the expected content of a row mathces the actual content in the next row of the
    * dataset (ie: this method calls next on the callee's behalf).
    *
    * @param aDataSet
    *           the dataset from which to retrieve the actual values
    * @param aTask
    *           the task
    * @param aLabourSkill
    *           the labour skill
    * @param aHours
    *           the scheduled hours for the labour skill of the task
    *
    * @throws Exception
    *            if an error occurs
    */
   private void assertRow( DataSet aDataSet, TaskKey aTask, RefLabourSkillKey aLabourSkill,
         double aHours ) throws Exception {
      aDataSet.next();
      MxAssert.assertEquals( "root_event_db_id", aTask.getDbId(),
            aDataSet.getInt( "root_event_db_id" ) );
      MxAssert.assertEquals( "root_event_id", aTask.getId(), aDataSet.getInt( "root_event_id" ) );
      MxAssert.assertEquals( "labour_skill_db_id", aLabourSkill.getDbId(),
            aDataSet.getInteger( "labour_skill_db_id" ) );
      MxAssert.assertEquals( "labour_skill_cd", aLabourSkill.getCd(),
            aDataSet.getString( "labour_skill_cd" ) );
      MxAssert.assertEquals( "sched_hr", aHours, aDataSet.getDouble( "sched_hr" ) );
   }


   /**
    * Execute the query.
    *
    * @param aLocation
    *           location
    * @param aDate
    *           date
    *
    * @return the result
    */
   private DataSet execute( LocationKey aLocation, Date aDate ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aLocation, new String[] { "aLocDbId", "aLocId" } );
      lArgs.add( "aDate", aDate );

      // Execute the query
      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
