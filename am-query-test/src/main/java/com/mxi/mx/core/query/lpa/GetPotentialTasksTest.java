
package com.mxi.mx.core.query.lpa;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.key.AircraftKey;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.EventDeadlineKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefSchedPriorityKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskPriorityKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.evt.EvtSchedDeadTable;
import com.mxi.mx.core.table.inv.InvAcReg;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.sched.SchedStaskTable;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * Tests the query com.mxi.mx.core.query.lpa.GetPotentialTasks
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class GetPotentialTasksTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @Before
   public void setUp() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Execute the query.
    *
    * @param aDate
    *           The current date
    * @param aScheduleRange
    *           The scheduling range
    * @param aAircraftDbId
    *           inv_db_id
    * @param aAircraftId
    *           inv_id
    */
   private void execute( Date aDate, Integer aScheduleRange, Integer aAircraftDbId,
         Integer aAircraftId ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aCurrentDate", aDate );
      lArgs.add( "aScheduleRange", aScheduleRange );
      lArgs.add( "aAircraftDbId", aAircraftDbId );
      lArgs.add( "aAircraftId", aAircraftId );

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
      iDataSet.addSort( "task_pk", true );
      iDataSet.filterAndSort();
   }


   /**
    * This method ensures that no tasks are found by the query, if the tasks are on Heavy
    * Maintenance Work Packages
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testHeavyMaintenanceTasks() throws Exception {

      // SETUP: Set the check to heavy maintenance
      SchedStaskTable lSchedStask1 = SchedStaskTable.findByPrimaryKey( new TaskKey( 4650, 100 ) );
      lSchedStask1.setHeavy( true );
      lSchedStask1.update();

      // Set the loose tasks to heavy maintenance
      SchedStaskTable lSchedStask2 = SchedStaskTable.findByPrimaryKey( new TaskKey( 4650, 400 ) );
      lSchedStask2.setHeavy( true );
      lSchedStask2.update();

      SchedStaskTable lSchedStask3 = SchedStaskTable.findByPrimaryKey( new TaskKey( 4650, 500 ) );
      lSchedStask3.setHeavy( true );
      lSchedStask3.update();

      // ACTION: Execute the Query
      execute( DateUtils.parseDateTimeString( "15-MAY-2007 01:00" ), 5, 4650, 100 );

      // TEST: Confirm the Data had no results
      assertEquals( 0, iDataSet.getRowCount() );

      // TEARDOWN: Undo the Setup Changes
      lSchedStask1.setHeavy( false );
      lSchedStask1.update();

      lSchedStask2.setHeavy( false );
      lSchedStask2.update();

      lSchedStask3.setHeavy( false );
      lSchedStask3.update();
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
      TaskKey lInWorkTask = new TaskKey( "4650:600" );

      // assert that the query returns 4 row
      execute( DateUtils.parseDateTimeString( "15-MAY-2007 01:00" ), 5, 4650, 100 );
      assertEquals( 4, iDataSet.getRowCount() );

      // make the IN WORK task ACTV
      EvtEventTable lTable = EvtEventTable.findByPrimaryKey( lInWorkTask );
      assertEquals( RefEventStatusKey.IN_WORK, lTable.getEventStatus() );
      lTable.setStatus( RefEventStatusKey.ACTV );
      lTable.update();

      // assert that the count gets incremented, indicating that the updated 'IN WORK' task is
      // being returned
      execute( DateUtils.parseDateTimeString( "15-MAY-2007 01:00" ), 5, 4650, 100 );
      assertEquals( 5, iDataSet.getRowCount() );

      // reset the data back to it's original state
      lTable = EvtEventTable.findByPrimaryKey( lInWorkTask );
      lTable.setStatus( RefEventStatusKey.IN_WORK );
      lTable.update();
   }


   /**
    * This method ensures that no tasks are found by the query, if the aircraft is locked
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testLockedAircraft() throws Exception {

      // SETUP: Data Setup for running the Function
      InvInvTable lInvInvTable = InvInvTable.findByPrimaryKey( new InventoryKey( 4650, 100 ) );
      lInvInvTable.setLockedBool( true );
      lInvInvTable.update();

      // ACTION: Execute the Query
      execute( DateUtils.parseDateTimeString( "15-MAY-2007 01:00" ), 5, 4650, 100 );

      // TEST: Confirm the Data had no results
      assertEquals( 0, iDataSet.getRowCount() );

      // TEARDOWN: Undo the Setup Changes
      lInvInvTable.setLockedBool( false );
      lInvInvTable.update();
   }


   /**
    * This method ensures that no tasks are found by the query, if the aircraft is LPA Disabled
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testLPADisabledAircraft() throws Exception {

      // SETUP: Data Setup for running the Function
      InvAcReg lInvAcReg = InvAcReg.findByPrimaryKey( new AircraftKey( 4650, 100 ) );
      lInvAcReg.setPreventLPABool( true );

      // ACTION: Execute the Query
      execute( DateUtils.parseDateTimeString( "15-MAY-2007 01:00" ), 5, 4650, 100 );

      // TEST: Confirm the Data had no results
      assertEquals( 0, iDataSet.getRowCount() );

      // TEARDOWN: Undo the Setup Changes
      lInvAcReg.setPreventLPABool( false );
   }


   /**
    * This method ensures that no tasks are found by the query, if the parent task is LPA Disabled
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testLPADisabledParentTasks() throws Exception {

      // SETUP: Data Setup for running the Function
      SchedStaskTable lSchedStask = SchedStaskTable.findByPrimaryKey( new TaskKey( 4650, 100 ) );
      lSchedStask.setPreventLinePlanningAutomation( true );
      lSchedStask.update();

      // ACTION: Execute the Query
      execute( DateUtils.parseDateTimeString( "15-MAY-2007 01:00" ), 5, 4650, 100 );

      // TEST: Confirm the Data had only 2 loose tasks
      assertEquals( 2, iDataSet.getRowCount() );

      iDataSet.addSort( "sched_id", true );
      iDataSet.filterAndSort();

      iDataSet.next();
      testRow( new TaskKey( 4650, 400 ), "LOOSETASK", new AircraftKey( 4650, 100 ),
            new AssemblyKey( 4650, "ASCD" ), "Aircraft", RefSchedPriorityKey.NONE, null,
            new RefTaskPriorityKey( 4650, "LOW" ), 0, 0, "REQ", null, null, 0.0, null, null, null,
            null, null );

      iDataSet.next();
      testRow( new TaskKey( 4650, 500 ), "LOOSETASK", new AircraftKey( 4650, 100 ),
            new AssemblyKey( 4650, "ASCD" ), "Aircraft", RefSchedPriorityKey.NONE,
            DateUtils.parseDateTimeString( "26-MAY-2007 19:45" ),
            new RefTaskPriorityKey( 4650, "LOW" ), 0, 0, "REQ", null, null, 0.0, null, null, null,
            null, null );

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

      TaskKey lLRPTask = new TaskKey( "4650:700" );

      // assert that the query returns 4 rows
      execute( DateUtils.parseDateTimeString( "15-MAY-2007 01:00" ), 5, 4650, 100 );
      assertEquals( 4, iDataSet.getRowCount() );

      // make the LRP task as non LRP.
      SchedStaskTable lTable = SchedStaskTable.findByPrimaryKey( lLRPTask );
      assertEquals( true, lTable.isLRPBool() );
      lTable.setLRPBool( false );
      lTable.update();

      // assert that the count gets incremented, indicating that the updated LRP task is being
      // returned
      execute( DateUtils.parseDateTimeString( "15-MAY-2007 01:00" ), 5, 4650, 100 );
      assertEquals( 5, iDataSet.getRowCount() );

      // reset the data back to it's original state
      lTable = SchedStaskTable.findByPrimaryKey( lLRPTask );
      lTable.setLRPBool( true );
      lTable.update();
   }


   /**
    * This method ensures that only tasks with no due date are found. Tasks that due before or
    * within the Scheduling Window will not be returned by the query
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testNoTopUpTasksDueAfterSchedWindow() throws Exception {

      // SETUP: Data Setup for running the Function
      EvtSchedDeadTable lEvtSchedDead =
            EvtSchedDeadTable.findByPrimaryKey( new EventDeadlineKey( "4650:102:0:21" ) );
      lEvtSchedDead.setDeadlineDate( DateUtils.parseDateTimeString( "15-MAY-2007 10:00" ) );
      lEvtSchedDead.update();

      EvtSchedDeadTable lEvtSchedDead2 =
            EvtSchedDeadTable.findByPrimaryKey( new EventDeadlineKey( "4650:500:0:23" ) );
      lEvtSchedDead2.setDeadlineDate( DateUtils.parseDateTimeString( "15-MAY-2007 10:00" ) );
      lEvtSchedDead2.update();

      // ACTION: Execute the Query
      execute( DateUtils.parseDateTimeString( "15-MAY-2007 01:00" ), 5, 4650, 100 );

      // TEST: Confirm the Data had only 2 tasks with no deadline
      assertEquals( 2, iDataSet.getRowCount() );

      // loose task with no deadline
      iDataSet.next();

      testRow( new TaskKey( 4650, 101 ), "ASSIGNEDTASK101", new AircraftKey( 4650, 100 ),
            new AssemblyKey( 4650, "ASCD" ), "Aircraft", RefSchedPriorityKey.NONE, null, // no
                                                                                         // deadline
            new RefTaskPriorityKey( 4650, "LOW" ), 0, 4650, "REQ", "CHECK",
            DateUtils.parseDateTimeString( "15-MAY-2007 01:00" ), 0.0, null, null, null, null,
            null );

      // assigned task with no deadline
      iDataSet.next();

      testRow( new TaskKey( 4650, 400 ), "LOOSETASK", new AircraftKey( 4650, 100 ),
            new AssemblyKey( 4650, "ASCD" ), "Aircraft", RefSchedPriorityKey.NONE, null, // no
                                                                                         // deadline
            new RefTaskPriorityKey( 4650, "LOW" ), 0, 0, "REQ", null, null, 0.0, null, null, null,
            null, null );

      // TEARDOWN: Undo the Setup Changes
      lEvtSchedDead.setDeadlineDate( DateUtils.parseDateTimeString( "25-MAY-2007 21:30" ) );
      lEvtSchedDead.update();

      lEvtSchedDead2.setDeadlineDate( DateUtils.parseDateTimeString( "26-MAY-2007 19:45" ) );
      lEvtSchedDead2.update();
   }


   /**
    * This method ensures that no tasks are found by the query, if the task priority > 0
    *
    * @throws Exception
    *            If an error occurs
    */
   @Test
   public void testNoTopUpTasksWithHighPriority() throws Exception {

      // SETUP: Set a Task to High Priority
      SchedStaskTable lSchedStask1 = SchedStaskTable.findByPrimaryKey( new TaskKey( 4650, 101 ) );
      lSchedStask1.setTaskPriority( new RefTaskPriorityKey( 4650, "HIGH" ) );
      lSchedStask1.update();

      SchedStaskTable lSchedStask2 = SchedStaskTable.findByPrimaryKey( new TaskKey( 4650, 102 ) );
      lSchedStask2.setTaskPriority( new RefTaskPriorityKey( 4650, "HIGH" ) );
      lSchedStask2.update();

      SchedStaskTable lSchedStask3 = SchedStaskTable.findByPrimaryKey( new TaskKey( 4650, 400 ) );
      lSchedStask3.setTaskPriority( new RefTaskPriorityKey( 4650, "HIGH" ) );
      lSchedStask3.update();

      SchedStaskTable lSchedStask4 = SchedStaskTable.findByPrimaryKey( new TaskKey( 4650, 500 ) );
      lSchedStask4.setTaskPriority( new RefTaskPriorityKey( 4650, "HIGH" ) );
      lSchedStask4.update();

      // ACTION: Execute the Query
      execute( DateUtils.parseDateTimeString( "15-MAY-2007 01:00" ), 5, 4650, 100 );

      // TEST: Confirm the Data had no results
      assertEquals( 0, iDataSet.getRowCount() );

      // TEARDOWN: Undo the setup changes
      lSchedStask1.setTaskPriority( new RefTaskPriorityKey( 4650, "LOW" ) );
      lSchedStask1.update();

      lSchedStask2.setTaskPriority( new RefTaskPriorityKey( 4650, "LOW" ) );
      lSchedStask2.update();

      lSchedStask3.setTaskPriority( new RefTaskPriorityKey( 4650, "LOW" ) );
      lSchedStask3.update();

      lSchedStask4.setTaskPriority( new RefTaskPriorityKey( 4650, "LOW" ) );
      lSchedStask4.update();
   }


   /**
    * Tests that the query returns the proper data
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQuery() throws Exception {

      execute( DateUtils.parseDateTimeString( "15-MAY-2007 01:00" ), 5, 4650, 100 );

      // There should be 4 rows
      MxAssert.assertEquals( "Number of retrieved rows", 4, iDataSet.getRowCount() );

      // assigned task with no deadline
      iDataSet.next();
      testRow( new TaskKey( 4650, 101 ), "ASSIGNEDTASK101", new AircraftKey( 4650, 100 ),
            new AssemblyKey( 4650, "ASCD" ), "Aircraft", RefSchedPriorityKey.NONE, null,
            new RefTaskPriorityKey( 4650, "LOW" ), 0, 4650, "REQ", "CHECK",
            DateUtils.parseDateTimeString( "15-MAY-2007 01:00" ), 0.0, null, null, null, null,
            null );

      // assign task with deadline
      iDataSet.next();
      testRow( new TaskKey( 4650, 102 ), "ASSIGNEDTASK102", new AircraftKey( 4650, 100 ),
            new AssemblyKey( 4650, "ASCD" ), "Aircraft", RefSchedPriorityKey.NONE,
            DateUtils.parseDateTimeString( "25-MAY-2007 21:30" ),
            new RefTaskPriorityKey( 4650, "LOW" ), 0, 4650, "REQ", "CHECK",
            DateUtils.parseDateTimeString( "15-MAY-2007 01:00" ), 0.0, null, null, null, null,
            null );

      // loose task with no deadline
      iDataSet.next();
      testRow( new TaskKey( 4650, 400 ), "LOOSETASK", new AircraftKey( 4650, 100 ),
            new AssemblyKey( 4650, "ASCD" ), "Aircraft", RefSchedPriorityKey.NONE, null,
            new RefTaskPriorityKey( 4650, "LOW" ), 0, 0, "REQ", null, null, 0.0, null, null, null,
            null, null );

      iDataSet.next();
      testRow( new TaskKey( 4650, 500 ), "LOOSETASK", new AircraftKey( 4650, 100 ),
            new AssemblyKey( 4650, "ASCD" ), "Aircraft", RefSchedPriorityKey.NONE,
            DateUtils.parseDateTimeString( "26-MAY-2007 19:45" ),
            new RefTaskPriorityKey( 4650, "LOW" ), 0, 0, "REQ", null, null, 0.0, null, null, null,
            null, null );
   }


   /**
    * Tests that the query returns the proper data for Block Tasks
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQueryAdHocs() throws Exception {

      // SETUP: Change all the Tasks to Blocks
      List<RefTaskClassKey> lClasses = new ArrayList<RefTaskClassKey>();
      SchedStaskTable lSchedStask = SchedStaskTable.create( new TaskKey( 4650, 101 ) );
      lClasses.add( lSchedStask.getTaskClass() );
      lSchedStask.setTaskClass( RefTaskClassKey.ADHOC );
      lSchedStask.update();
      lSchedStask = SchedStaskTable.create( new TaskKey( 4650, 102 ) );
      lClasses.add( lSchedStask.getTaskClass() );
      lSchedStask.setTaskClass( RefTaskClassKey.ADHOC );
      lSchedStask.update();
      lSchedStask = SchedStaskTable.create( new TaskKey( 4650, 400 ) );
      lClasses.add( lSchedStask.getTaskClass() );
      lSchedStask.setTaskClass( RefTaskClassKey.ADHOC );
      lSchedStask.update();
      lSchedStask = SchedStaskTable.create( new TaskKey( 4650, 500 ) );
      lClasses.add( lSchedStask.getTaskClass() );
      lSchedStask.setTaskClass( RefTaskClassKey.ADHOC );
      lSchedStask.update();

      execute( DateUtils.parseDateTimeString( "15-MAY-2007 01:00" ), 5, 4650, 100 );

      // There should be 4 rows
      MxAssert.assertEquals( "Number of retrieved rows", 4, iDataSet.getRowCount() );

      // Set them all back to REQ
      lSchedStask = SchedStaskTable.create( new TaskKey( 4650, 101 ) );
      lSchedStask.setTaskClass( lClasses.get( 0 ) );
      lSchedStask.update();
      lSchedStask = SchedStaskTable.create( new TaskKey( 4650, 102 ) );
      lSchedStask.setTaskClass( lClasses.get( 1 ) );
      lSchedStask.update();
      lSchedStask = SchedStaskTable.create( new TaskKey( 4650, 400 ) );
      lSchedStask.setTaskClass( lClasses.get( 2 ) );
      lSchedStask.update();
      lSchedStask = SchedStaskTable.create( new TaskKey( 4650, 500 ) );
      lSchedStask.setTaskClass( lClasses.get( 3 ) );
      lSchedStask.update();
   }


   /**
    * This method ensures that task falls outside a task yield, will not be returned by the query.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testTopUpTasksOutsidePlanningYield() throws Exception {

      // SETUP: Data Setup for running the Function
      EvtSchedDeadTable lEvtSchedDead =
            EvtSchedDeadTable.findByPrimaryKey( new EventDeadlineKey( "4650:500:0:23" ) );
      lEvtSchedDead.setIntervalQt( 0.0 );
      lEvtSchedDead.update();

      // ACTION: Execute the Query
      execute( DateUtils.parseDateTimeString( "15-MAY-2007 01:00" ), 5, 4650, 100 );

      // TEST: Confirm the Data had only 3 tasks. Top-up task fall outsidte planning yield should
      // not be found
      assertEquals( 3, iDataSet.getRowCount() );

      iDataSet.addSort( "sched_id", true );

      // assigned task with no deadline
      iDataSet.next();
      testRow( new TaskKey( 4650, 101 ), "ASSIGNEDTASK101", new AircraftKey( 4650, 100 ),
            new AssemblyKey( 4650, "ASCD" ), "Aircraft", RefSchedPriorityKey.NONE, null,
            new RefTaskPriorityKey( 4650, "LOW" ), 0, 4650, "REQ", "CHECK",
            DateUtils.parseDateTimeString( "15-MAY-2007 01:00" ), 0.0, null, null, null, null,
            null );

      // assign task with deadline
      iDataSet.next();
      testRow( new TaskKey( 4650, 102 ), "ASSIGNEDTASK102", new AircraftKey( 4650, 100 ),
            new AssemblyKey( 4650, "ASCD" ), "Aircraft", RefSchedPriorityKey.NONE,
            DateUtils.parseDateTimeString( "25-MAY-2007 21:30" ),
            new RefTaskPriorityKey( 4650, "LOW" ), 0, 4650, "REQ", "CHECK",
            DateUtils.parseDateTimeString( "15-MAY-2007 01:00" ), 0.0, null, null, null, null,
            null );

      // loose task with no deadline
      iDataSet.next();
      testRow( new TaskKey( 4650, 400 ), "LOOSETASK", new AircraftKey( 4650, 100 ),
            new AssemblyKey( 4650, "ASCD" ), "Aircraft", RefSchedPriorityKey.NONE, null,
            new RefTaskPriorityKey( 4650, "LOW" ), 0, 0, "REQ", null, null, 0.0, null, null, null,
            null, null );

      // TEARDOWN: Undo the setup changes
      lEvtSchedDead.setIntervalQt( 15000.0 );
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
    * @param aSchedPriority
    *           Task's Scheduling Priority
    * @param aDueDate
    *           Task's Due Date
    * @param aTaskPriority
    *           Task's Priority
    * @param aTaskPriorityOrd
    *           Priority Ordering
    * @param aNextHighestEventDbId
    *           The next highest event
    * @param aClassModeCd
    *           Class Mode cd
    * @param aNextHighestTaskClassCd
    *           Next highest class mode cd
    * @param aHighestSchedStartGdt
    *           Highest Sched start date
    * @param aDuration
    *           Task's Estimated Duration
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
         AssemblyKey aAssembly, String aAircraftName, RefSchedPriorityKey aSchedPriority,
         Date aDueDate, RefTaskPriorityKey aTaskPriority, int aTaskPriorityOrd,
         int aNextHighestEventDbId, String aClassModeCd, String aNextHighestTaskClassCd,
         Date aHighestSchedStartGdt, double aDuration, String aPartList, String aToolList,
         String aLabourList, String aWorkTypeList, String aSubtreeList ) {

      MxAssert.assertEquals( aTask.toString(), iDataSet.getString( "task_pk" ) );
      MxAssert.assertEquals( aBarcode, iDataSet.getString( "barcode_sdesc" ) );
      MxAssert.assertEquals( aAircraft.toString(), iDataSet.getString( "aircraft_pk" ) );
      MxAssert.assertEquals( aAssembly.toString(), iDataSet.getString( "assmbl_pk" ) );
      MxAssert.assertEquals( aAircraftName, iDataSet.getString( "aircraft_name" ) );
      MxAssert.assertEquals( aSchedPriority.toString(), iDataSet.getString( "sched_priority" ) );
      MxAssert.assertEquals( aDueDate, iDataSet.getDate( "due_date" ) );
      MxAssert.assertEquals( aTaskPriority.toString(), iDataSet.getString( "task_priority" ) );
      MxAssert.assertEquals( aTaskPriorityOrd, iDataSet.getInt( "task_priority_ord" ) );

      // Loose Task Specific values
      MxAssert.assertEquals( aNextHighestEventDbId, iDataSet.getInt( "nh_event_db_id" ) );
      MxAssert.assertEquals( aClassModeCd, iDataSet.getString( "class_mode_cd" ) );

      // Non Loose Task Specific values
      MxAssert.assertEquals( aNextHighestTaskClassCd, iDataSet.getString( "nh_task_class_cd" ) );
      MxAssert.assertEquals( aHighestSchedStartGdt, iDataSet.getDate( "h_sched_start_gdt" ) );

      MxAssert.assertEquals( aDuration, iDataSet.getDouble( "duration" ) );

      if ( aPartList != null ) {
         MxAssert.assertEquals( aPartList, iDataSet.getString( "part_details_list" ) );
      }

      if ( aToolList != null ) {
         MxAssert.assertEquals( aToolList, iDataSet.getString( "tools_list" ) );
      }

      if ( aLabourList != null ) {
         MxAssert.assertEquals( aLabourList, iDataSet.getString( "est_labour_effort_list" ) );
      }

      if ( aWorkTypeList != null ) {
         MxAssert.assertEquals( aWorkTypeList, iDataSet.getString( "work_types_list" ) );
      }

      if ( aSubtreeList != null ) {
         MxAssert.assertEquals( aSubtreeList, iDataSet.getString( "subtree_list" ) );
      }
   }
}
