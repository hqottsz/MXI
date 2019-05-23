
package com.mxi.mx.core.query.lpa;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.key.AircraftKey;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefSchedPriorityKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskPriorityKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.inv.InvAcReg;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.sched.SchedStaskTable;


/**
 * This class performs unit testing on the query file with the same package and name.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class GetMaintenanceTasksTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @Before
   public void setUp() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * No Heavy Maintenance Work Packages should be evaluated by the LPA Query. This test sets up a
    * Task Hieriarchy on a Heavy-Maintenance enabled Work Package and verifies that it is not found
    * by the Query.
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testHeavyMaintenanceTasks() throws Exception {

      // SETUP: Data Setup for running the Function
      SchedStaskTable lSchedStask = SchedStaskTable.create( new TaskKey( 8888, 88888 ) );
      lSchedStask.setHeavy( true );
      lSchedStask.update();

      // ACTION: Execute the Query
      execute( DateUtils.parseDefaultDateString( "14-MAY-2006 EST" ), 5 );

      // TEST: Confirm the Data had no results
      assertEquals( 0, iDataSet.getRowCount() );

      // TEARDOWN: Undo the Setup Changes
      lSchedStask.setHeavy( false );
      lSchedStask.update();
   }


   /**
    * Tests that the 'IN WORK' tasks are not returned by the query. If the 'IN WORK' task's status
    * is changed, it is being returned by the query.
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testInWorkTasks() throws Exception {

      TaskKey lInWorkTask = new TaskKey( "4650:10004" );

      // assert that the query returns 1 row
      execute( DateUtils.parseDefaultDateString( "14-MAY-2006 EST" ), 5 );
      assertEquals( 1, iDataSet.getRowCount() );

      // make the IN WORK task ACTV
      EvtEventTable lTable = EvtEventTable.findByPrimaryKey( lInWorkTask );
      assertEquals( RefEventStatusKey.IN_WORK, lTable.getEventStatus() );
      lTable.setStatus( RefEventStatusKey.ACTV );
      lTable.update();

      // assert that the count gets incremented, indicating that the updated 'IN WORK' task is
      // being returned
      execute( DateUtils.parseDefaultDateString( "14-MAY-2006 EST" ), 5 );
      assertEquals( 2, iDataSet.getRowCount() );

      // reset the data back to it's original state
      lTable = EvtEventTable.findByPrimaryKey( lInWorkTask );
      lTable.setStatus( RefEventStatusKey.IN_WORK );
      lTable.update();
   }


   /**
    * No Work Packages on Locked Aircraft should be evaluated by the LPA Query. This test sets up a
    * Task Hieriarchy on a Locked Aircraft and verifies that no Tasks are found by the Query.
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testLockedAircraft() throws Exception {

      // SETUP: Data Setup for running the Function
      InvInvTable lInvInvTable = InvInvTable.findByPrimaryKey( new InventoryKey( 4650, 10000 ) );
      lInvInvTable.setLockedBool( true );
      lInvInvTable.update();

      // ACTION: Execute the Query
      execute( DateUtils.parseDefaultDateString( "14-MAY-2006 EST" ), 5 );

      // TEST: Confirm the Data had no results
      assertEquals( 0, iDataSet.getRowCount() );

      // TEARDOWN: Undo the Setup Changes
      lInvInvTable.setLockedBool( false );
      lInvInvTable.update();
   }


   /**
    * No Work Packages on LPA Disabled Aircraft should be evaluated by the LPA Query. This test sets
    * up a Task Hieriarchy on an LPA Disabled Aircraft and verifies that no Tasks are found by the
    * Query.
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testLPADisabledAircraft() throws Exception {

      // SETUP: Data Setup for running the Function
      InvAcReg lInvAcReg = InvAcReg.findByPrimaryKey( new AircraftKey( 4650, 10000 ) );
      lInvAcReg.setPreventLPABool( true );

      // ACTION: Execute the Query
      execute( DateUtils.parseDefaultDateString( "14-MAY-2006 EST" ), 5 );

      // TEST: Confirm the Data had no results
      assertEquals( 0, iDataSet.getRowCount() );

      // TEARDOWN: Undo the Setup Changes
      lInvAcReg.setPreventLPABool( false );
   }


   /**
    * No LPA Disabled Work Packages should be evaluated by the LPA Query. This test sets up a Task
    * Hieriarchy on an LPA Disabled Work Package and verifies that no Tasks are found by the Query.
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testLPADisabledParentTasks() throws Exception {

      // SETUP: Data Setup for running the Function
      SchedStaskTable lSchedStask = SchedStaskTable.create( new TaskKey( 8888, 88888 ) );
      lSchedStask.setPreventLinePlanningAutomation( true );
      lSchedStask.update();

      // ACTION: Execute the Query
      execute( DateUtils.parseDefaultDateString( "14-MAY-2006 EST" ), 5 );

      // TEST: Confirm the Data had no results
      assertEquals( 0, iDataSet.getRowCount() );

      // TEARDOWN: Undo the Setup Changes
      lSchedStask.setPreventLinePlanningAutomation( false );
      lSchedStask.update();
   }


   /**
    * Tests that the LRP tasks are not returned by the query. If the LRP task's lrp_bool is changed,
    * it is being returned by the query.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testLrpTasks() throws Exception {

      TaskKey lLRPTask = new TaskKey( "4650:10005" );

      // assert that the query returns 1 row
      execute( DateUtils.parseDefaultDateString( "14-MAY-2006 EST" ), 5 );
      assertEquals( 1, iDataSet.getRowCount() );

      // make the LRP task as non LRP.
      SchedStaskTable lTable = SchedStaskTable.findByPrimaryKey( lLRPTask );
      assertEquals( true, lTable.isLRPBool() );
      lTable.setLRPBool( false );
      lTable.update();

      // assert that the count gets incremented, indicating that the updated LRP task is being
      // returned
      execute( DateUtils.parseDefaultDateString( "14-MAY-2006 EST" ), 5 );
      assertEquals( 2, iDataSet.getRowCount() );

      // reset the data back to it's original state
      lTable = SchedStaskTable.findByPrimaryKey( lLRPTask );
      lTable.setLRPBool( true );
      lTable.update();
   }


   /**
    * No Tasks after the Scheduling Window should be evaluated by the LPA Query, unless of High
    * Priority (Priority > 0 ). This test sets up a Task Hieriarchy that is Due after the Scheduling
    * Window and verifies that it is not found by the Query.
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testNoTasksAvailable() throws Exception {

      // SETUP: Data Setup for running the Function
      // Use a Start Date far into the Past

      // ACTION: Execute the Query
      execute( DateUtils.parseDefaultDateString( "14-MAY-1999 EST" ), 5 );

      // TEST: Confirm the Data had no results
      assertEquals( 0, iDataSet.getRowCount() );
   }


   /**
    * No Tasks after the Scheduling Window should be evaluated by the LPA Query, unless of High
    * Priority (Priority > 0 ). This test sets up a Task Hieriarchy that has a child of High
    * Priority and verifies that the Query locates the Tree.
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testNoTasksInSchedWindowSomeHighPriority() throws Exception {

      // SETUP: Data Setup for running the Function
      // Use a Start Date far into the Past

      // TEST: Verify that there are no Tasks in the Scheduling Window
      execute( DateUtils.parseDefaultDateString( "14-MAY-1998 EST" ), 5 );
      assertEquals( 0, iDataSet.getRowCount() );

      // SETUP: Set a Task to High Priority
      SchedStaskTable lSchedStask = SchedStaskTable.create( new TaskKey( 4650, 10002 ) );
      lSchedStask.setTaskPriority( new RefTaskPriorityKey( 4650, "HIGH" ) );
      lSchedStask.update();

      // ACTION: Execute the Query
      execute( DateUtils.parseDefaultDateString( "14-MAY-1998 EST" ), 5 );

      // TEST: Confirm the Data had Tasks
      assertEquals( 1, iDataSet.getRowCount() );
      iDataSet.next();
      testRow( new TaskKey( 4650, 10000 ), "ROOT", new AircraftKey( 4650, 10000 ),
            new AssemblyKey( 4650, "ASCD" ), "Aircraft Name",
            DateUtils.parseDateTimeString( "17-MAY-2006 19:00" ), 5.0,
            new RefTaskPriorityKey( 4650, "HIGH" ), 1, RefSchedPriorityKey.NONE,
            DateUtils.parseDateTimeString( "15-MAY-2006 19:00" ), null, null, null, null, null );

      // TEARDOWN: Undo the Setup Changes
      lSchedStask.setTaskPriority( new RefTaskPriorityKey( 4650, "LOW" ) );
      lSchedStask.update();
   }


   /**
    * Tasks due in the Past should be picked up by the LPA Query. This test sets up a Task Hierarchy
    * due prior to the Scheduling Window and verifies that the Query locates the Tree.
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testOverdueTasks() throws Exception {

      // SETUP: Data Setup for running the Function
      // Use a Start Date far into the Future

      // ACTION: Execute the Query
      execute( DateUtils.parseDefaultDateString( "14-MAY-2020 EST" ), 5 );

      // TEST: Confirm the Data
      assertEquals( 1, iDataSet.getRowCount() );
      iDataSet.next();
      testRow( new TaskKey( 4650, 10000 ), "ROOT", new AircraftKey( 4650, 10000 ),
            new AssemblyKey( 4650, "ASCD" ), "Aircraft Name",
            DateUtils.parseDateTimeString( "17-MAY-2006 19:00" ), 5.0,
            new RefTaskPriorityKey( 4650, "LOW" ), 0, RefSchedPriorityKey.NONE,
            DateUtils.parseDateTimeString( "15-MAY-2006 19:00" ), null, null, null, null, null );
   }


   /**
    * This test verfies that if data is setup, the Query will properly return it.
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testValidAdhocTaskData() throws Exception {

      // SETUP: Data Setup for running the Function
      RefTaskClassKey lClass;
      SchedStaskTable lSchedStask = SchedStaskTable.create( new TaskKey( 4650, 10000 ) );
      lClass = lSchedStask.getTaskClass();
      lSchedStask.setTaskClass( RefTaskClassKey.ADHOC );
      lSchedStask.update();

      // ACTION: Execute the Query
      execute( DateUtils.parseDefaultDateString( "14-MAY-2006 EST" ), 5 );

      // TEST: Confirm the Data had Tasks
      assertEquals( 1, iDataSet.getRowCount() );
      iDataSet.next();
      testRow( new TaskKey( 4650, 10000 ), "ROOT", new AircraftKey( 4650, 10000 ),
            new AssemblyKey( 4650, "ASCD" ), "Aircraft Name",
            DateUtils.parseDateTimeString( "17-MAY-2006 19:00" ), 5.0,
            new RefTaskPriorityKey( 4650, "LOW" ), 0, RefSchedPriorityKey.NONE,
            DateUtils.parseDateTimeString( "15-MAY-2006 19:00" ), null, null, null, null, null );

      // DATA RESET
      lSchedStask = SchedStaskTable.create( new TaskKey( 4650, 10000 ) );
      lSchedStask.setTaskClass( lClass );
      lSchedStask.update();
   }


   /**
    * This test verfies that if data is setup, the Query will properly return it.
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testValidTaskData() throws Exception {

      // SETUP: Data Setup for running the Function
      // Use a Date such that Task Due Dates are coming up

      // ACTION: Execute the Query
      execute( DateUtils.parseDefaultDateString( "14-MAY-2006 EST" ), 5 );

      // TEST: Confirm the Data had Tasks
      assertEquals( 1, iDataSet.getRowCount() );
      iDataSet.next();
      testRow( new TaskKey( 4650, 10000 ), "ROOT", new AircraftKey( 4650, 10000 ),
            new AssemblyKey( 4650, "ASCD" ), "Aircraft Name",
            DateUtils.parseDateTimeString( "17-MAY-2006 19:00" ), 5.0,
            new RefTaskPriorityKey( 4650, "LOW" ), 0, RefSchedPriorityKey.NONE,
            DateUtils.parseDateTimeString( "15-MAY-2006 19:00" ), null, null, null, null, null );
   }


   /**
    * This test verfies that if data is setup, the Query will properly return it.
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testValidTaskDataWithPlanByDate() throws Exception {

      // SETUP: Data Setup for running the Function Use a Date such that Task Plan by date ( in
      // place of due date ) is coming up
      final Date lMay_16_2006_1900 = DateUtils.parseDateTimeString( "16-MAY-2006 19:00" );

      // set the Plan By Date before the Due Date of the task so that the Plan By Date gets
      // retrieve insteed of Due Date
      SchedStaskTable lSchedStask = SchedStaskTable.create( new TaskKey( 4650, 10000 ) );
      lSchedStask.setPlanByDate( lMay_16_2006_1900 );
      lSchedStask.update();

      // ACTION: Execute the Query
      execute( DateUtils.parseDefaultDateString( "14-MAY-2006 EST" ), 5 );

      // TEST: Confirm the Data had Tasks
      assertEquals( 1, iDataSet.getRowCount() );
      iDataSet.next();
      testRow( new TaskKey( 4650, 10000 ), "ROOT", new AircraftKey( 4650, 10000 ),
            new AssemblyKey( 4650, "ASCD" ), "Aircraft Name", lMay_16_2006_1900, 5.0,
            new RefTaskPriorityKey( 4650, "LOW" ), 0, RefSchedPriorityKey.NONE,
            DateUtils.parseDateTimeString( "15-MAY-2006 19:00" ), null, null, null, null, null );
   }


   /**
    * No 'Commit' or 'In Work' Work Packages should be evaluated by the LPA Query. This test sets up
    * a Task Hieriarchy on a 'Commit' and 'In Work' Work Packages and verifies that no Tasks are
    * found by the Query.
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testWorkPackagesInCommitOrInWorkState() throws Exception {

      // SETUP: Data Setup for running the Function
      EvtEventTable lEventTable = EvtEventTable.findByPrimaryKey( new EventKey( 8888, 88888 ) );
      lEventTable.setEventStatus( RefEventStatusKey.COMMIT );
      lEventTable.update();

      // ACTION: Execute the Query
      execute( DateUtils.parseDefaultDateString( "14-MAY-2006 EST" ), 5 );

      // TEST: Confirm the Data had no results
      assertEquals( 0, iDataSet.getRowCount() );

      // SETUP: Data Setup for running the Function
      lEventTable.setEventStatus( RefEventStatusKey.IN_WORK );
      lEventTable.update();

      // ACTION: Execute the Query
      execute( DateUtils.parseDefaultDateString( "14-MAY-2006 EST" ), 5 );

      // TEST: Confirm the Data had no results
      assertEquals( 0, iDataSet.getRowCount() );

      // TEARDOWN: Undo the Setup Changes
      lEventTable.setEventStatus( RefEventStatusKey.ACTV );
      lEventTable.update();
   }


   /**
    * No Work Packages should be returned by the Query. This test makes the existing Task Tree
    * historical and verifies that the Work Packages are not returned by the Query.
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testWorkPackagesOnly() throws Exception {

      // SETUP: Data Setup for running the Function
      EvtEventTable lEventTable = EvtEventTable.findByPrimaryKey( new EventKey( 4650, 10000 ) );
      lEventTable.setHistBool( true );
      lEventTable.update();

      // ACTION: Execute the Query
      execute( DateUtils.parseDefaultDateString( "14-MAY-2006 EST" ), 5 );

      // TEST: Confirm the Data had no results
      assertEquals( 0, iDataSet.getRowCount() );

      // TEARDOWN: Undo the Setup Changes
      lEventTable.setHistBool( false );
      lEventTable.update();
   }


   /**
    * Execute the query.
    *
    * @param aStartDate
    *           The Start Date of the Scheduling Window
    * @param aScheduleRange
    *           The duration in days of the Scheduling Window
    */
   private void execute( Date aStartDate, int aScheduleRange ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aCurrentDate", aStartDate );
      lArgs.add( "aSchedRange", aScheduleRange );

      iDataSet = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Tests a row of the dataset
    *
    * @param aTask
    *           Task PK
    * @param aBarcode
    *           Task's Barcode
    * @param aAircraft
    *           Task's associated aircraft
    * @param aAssembly
    *           Aircraft's Assembly
    * @param aAircraftName
    *           Aircraft's Name
    * @param aDueDate
    *           Task's Due Date
    * @param aDuration
    *           Task's Estimated Duration
    * @param aTaskPriority
    *           Task's Priority
    * @param aTaskPriorityOrd
    *           Priority Ordering
    * @param aSchedPriority
    *           Task's Scheduling Priority
    * @param aPlanYieldDate
    *           Task's Minimum Planning Yield Date
    * @param aPartList
    *           List of Parts needed by the Task
    * @param aToolList
    *           List of Tools needed by the Task
    * @param aLabourList
    *           List of Labour Skills needed by the Task
    * @param aWorkTypeList
    *           List of Work Types needed by the Task
    * @param aSubtreeList
    *           List of all Subtasks to this Task
    */
   private void testRow( TaskKey aTask, String aBarcode, AircraftKey aAircraft,
         AssemblyKey aAssembly, String aAircraftName, Date aDueDate, double aDuration,
         RefTaskPriorityKey aTaskPriority, int aTaskPriorityOrd, RefSchedPriorityKey aSchedPriority,
         Date aPlanYieldDate, String aPartList, String aToolList, String aLabourList,
         String aWorkTypeList, String aSubtreeList ) {

      assertEquals( aTask.toString(), iDataSet.getString( "task_pk" ) );
      assertEquals( aBarcode, iDataSet.getString( "barcode_sdesc" ) );
      assertEquals( aAircraft.toString(), iDataSet.getString( "aircraft_pk" ) );
      assertEquals( aAssembly.toString(), iDataSet.getString( "assmbl_pk" ) );
      assertEquals( aAircraftName, iDataSet.getString( "aircraft_name" ) );
      assertEquals( aDueDate, iDataSet.getDate( "due_date" ) );
      assertEquals( aDuration, iDataSet.getDouble( "duration" ), 0f );
      assertEquals( aTaskPriority.toString(), iDataSet.getString( "task_priority" ) );
      assertEquals( aTaskPriorityOrd, iDataSet.getInt( "task_priority_ord" ) );
      assertEquals( aSchedPriority.toString(), iDataSet.getString( "sched_priority" ) );
      assertEquals( aPlanYieldDate, iDataSet.getDate( "task_plan_yield_date" ) );
      if ( aPartList != null ) {
         assertEquals( aPartList, iDataSet.getString( "part_details_list" ) );
      }

      if ( aToolList != null ) {
         assertEquals( aToolList, iDataSet.getString( "tools_list" ) );
      }

      if ( aLabourList != null ) {
         assertEquals( aLabourList, iDataSet.getString( "est_labour_effort_list" ) );
      }

      if ( aWorkTypeList != null ) {
         assertEquals( aWorkTypeList, iDataSet.getString( "work_types_list" ) );
      }

      if ( aSubtreeList != null ) {
         assertEquals( aSubtreeList, iDataSet.getString( "subtree_list" ) );
      }
   }
}
