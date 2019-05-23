
package com.mxi.mx.web.query.location.stationcapacity;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AircraftKey;
import com.mxi.mx.core.key.EventDeadlineKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * Tests the query com.mxi.mx.web.query.location.stationcapacity.UnassignedTask
 *
 * @author jcimino
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class UnassignedTaskTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   /** DOCUMENT ME! */
   public static final EventDeadlineKey LEFT_ENGINE_TASK_DEADLINE_KEY =
         new EventDeadlineKey( 4650, 100, 0, 1 );

   /** DOCUMENT ME! */
   public static final EventDeadlineKey AIRCRAFT_COMPONENT_TASK_DEADLINE_KEY =
         new EventDeadlineKey( 4650, 102, 0, 10 );

   /** DOCUMENT ME! */
   public static final TaskKey RIGHT_ENGINE_TASK = new TaskKey( 4650, 101 );


   /**
    * Tests the query looking 1 day ahead
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testOneTaskWithNoDueDate() throws Exception {

      // Set due dates for both tasks below to 5 days ahead
      setDueDate( LEFT_ENGINE_TASK_DEADLINE_KEY, getDueDate( 5 ) );
      setDueDate( AIRCRAFT_COMPONENT_TASK_DEADLINE_KEY, getDueDate( 5 ) );

      // execute the query
      DataSet lDs = execute( StationCapacityData.Aircraft.AC_1, 1, StationCapacityData.Location.YOW,
            StationCapacityData.DAY_DT_1 );

      // There should be 1 rows
      MxAssert.assertEquals( "Number of retrieved rows", 1, lDs.getRowCount() );

      // test the row
      lDs.next();
      testRightEngineTask( lDs );

      setDueDate( LEFT_ENGINE_TASK_DEADLINE_KEY, null );
      setDueDate( AIRCRAFT_COMPONENT_TASK_DEADLINE_KEY, null );
   }


   /**
    * Tests the query looking 1 day ahead with no part capacity error
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testTaskWithoutPartCapacityError() throws Exception {

      // Set due dates for both tasks below to 5 days ahead
      setDueDate( LEFT_ENGINE_TASK_DEADLINE_KEY, getDueDate( 5 ) );
      setDueDate( AIRCRAFT_COMPONENT_TASK_DEADLINE_KEY, getDueDate( 5 ) );

      // assign the part jobcard to the task
      setParent( StationCapacityData.Task.Part.TASK_WITH_AVAILABLE_PART_ON_DAY_1,
            RIGHT_ENGINE_TASK );

      // execute the query
      DataSet lDs = execute( StationCapacityData.Aircraft.AC_1, 1, StationCapacityData.Location.YOW,
            StationCapacityData.DAY_DT_1 );

      // There should be 1 rows
      MxAssert.assertEquals( "Number of retrieved rows", 1, lDs.getRowCount() );

      // test the row
      lDs.next();
      testRightEngineTask( lDs, 1, 1 );

      // unassign the part jobcard to the task
      setParent( StationCapacityData.Task.Part.TASK_WITH_AVAILABLE_PART_ON_DAY_1, null );

      setDueDate( LEFT_ENGINE_TASK_DEADLINE_KEY, null );
      setDueDate( AIRCRAFT_COMPONENT_TASK_DEADLINE_KEY, null );
   }


   /**
    * Tests the query looking 1 day ahead with no tool capacity error
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testTaskWithoutToolCapacityError() throws Exception {

      // Set due dates for both tasks below to 5 days ahead
      setDueDate( LEFT_ENGINE_TASK_DEADLINE_KEY, getDueDate( 5 ) );
      setDueDate( AIRCRAFT_COMPONENT_TASK_DEADLINE_KEY, getDueDate( 5 ) );

      // assign the tool jobcard to the task
      setParent( StationCapacityData.Task.Tool.TASK_WITH_AVAILABLE_TOOL_ON_DAY_1,
            RIGHT_ENGINE_TASK );

      // execute the query
      DataSet lDs = execute( StationCapacityData.Aircraft.AC_1, 1, StationCapacityData.Location.YOW,
            StationCapacityData.DAY_DT_1 );

      // There should be 1 rows
      MxAssert.assertEquals( "Number of retrieved rows", 1, lDs.getRowCount() );

      // test the row
      lDs.next();
      testRightEngineTask( lDs, 1, 1 );

      // unassign the tool jobcard to the task
      setParent( StationCapacityData.Task.Tool.TASK_WITH_AVAILABLE_TOOL_ON_DAY_1, null );

      setDueDate( LEFT_ENGINE_TASK_DEADLINE_KEY, null );
      setDueDate( AIRCRAFT_COMPONENT_TASK_DEADLINE_KEY, null );
   }


   /**
    * Tests the query looking 1 day ahead with a part capacity error
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testTaskWithPartCapacityError() throws Exception {

      // Set due dates for both tasks below to 5 days ahead
      setDueDate( LEFT_ENGINE_TASK_DEADLINE_KEY, getDueDate( 5 ) );
      setDueDate( AIRCRAFT_COMPONENT_TASK_DEADLINE_KEY, getDueDate( 5 ) );

      // assign the part jobcard to the task
      setParent( StationCapacityData.Task.Part.TASK_WITH_AVAILABLE_BUT_RESERVED_PART_ON_DAY_1,
            RIGHT_ENGINE_TASK );

      // execute the query
      DataSet lDs = execute( StationCapacityData.Aircraft.AC_1, 1, StationCapacityData.Location.YOW,
            StationCapacityData.DAY_DT_1 );

      // There should be 1 rows
      MxAssert.assertEquals( "Number of retrieved rows", 1, lDs.getRowCount() );

      // test the row
      lDs.next();
      testRightEngineTask( lDs, 1, -1 );

      // unassign the part jobcard to the task
      setParent( StationCapacityData.Task.Part.TASK_WITH_AVAILABLE_BUT_RESERVED_PART_ON_DAY_1,
            null );

      setDueDate( LEFT_ENGINE_TASK_DEADLINE_KEY, null );
      setDueDate( AIRCRAFT_COMPONENT_TASK_DEADLINE_KEY, null );
   }


   /**
    * Tests the query looking 1 day ahead with a tool capacity error
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testTaskWithToolCapacityError() throws Exception {

      // Set due dates for both tasks below to 5 days ahead
      setDueDate( LEFT_ENGINE_TASK_DEADLINE_KEY, getDueDate( 5 ) );
      setDueDate( AIRCRAFT_COMPONENT_TASK_DEADLINE_KEY, getDueDate( 5 ) );

      // assign the tool jobcard to the task
      setParent( StationCapacityData.Task.Tool.TASK_WITH_UNAVAILABLE_TOOL_ON_DAY_1,
            RIGHT_ENGINE_TASK );

      // execute the query
      DataSet lDs = execute( StationCapacityData.Aircraft.AC_1, 1, StationCapacityData.Location.YOW,
            StationCapacityData.DAY_DT_1 );

      // There should be 1 rows
      MxAssert.assertEquals( "Number of retrieved rows", 1, lDs.getRowCount() );

      // test the row
      lDs.next();
      testRightEngineTask( lDs, -1, 1 );

      // unassign the tool jobcard to the task
      setParent( StationCapacityData.Task.Tool.TASK_WITH_UNAVAILABLE_TOOL_ON_DAY_1, null );

      setDueDate( LEFT_ENGINE_TASK_DEADLINE_KEY, null );
      setDueDate( AIRCRAFT_COMPONENT_TASK_DEADLINE_KEY, null );
   }


   /**
    * Tests the query looking 10 day ahead
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testThreeTasksTwoWithDueDates() throws Exception {

      // set the due date
      setDueDate( LEFT_ENGINE_TASK_DEADLINE_KEY, getDueDate( 5 ) );
      setDueDate( AIRCRAFT_COMPONENT_TASK_DEADLINE_KEY, getDueDate( 10 ) );

      // execute the query
      DataSet lDs = execute( StationCapacityData.Aircraft.AC_1, 10,
            StationCapacityData.Location.YOW, StationCapacityData.DAY_DT_1 );

      // There should be 3 rows
      MxAssert.assertEquals( "Number of retrieved rows", 3, lDs.getRowCount() );

      // test the row
      lDs.next();
      testLeftEngineTask( lDs, 5 );

      // test the row
      lDs.next();
      testAircraftTask( lDs, 10 );

      // test the row
      lDs.next();
      testRightEngineTask( lDs );

      // set the due date
      setDueDate( LEFT_ENGINE_TASK_DEADLINE_KEY, null );
      setDueDate( AIRCRAFT_COMPONENT_TASK_DEADLINE_KEY, null );
   }


   /**
    * Tests the query looking 5 day ahead
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testTwoTasksOneWithDueDate() throws Exception {

      // set the due date
      setDueDate( LEFT_ENGINE_TASK_DEADLINE_KEY, getDueDate( 5 ) );
      setDueDate( AIRCRAFT_COMPONENT_TASK_DEADLINE_KEY, getDueDate( 10 ) );

      // execute the query
      DataSet lDs = execute( StationCapacityData.Aircraft.AC_1, 5, StationCapacityData.Location.YOW,
            StationCapacityData.DAY_DT_1 );

      // There should be 2 rows
      MxAssert.assertEquals( "Number of retrieved rows", 2, lDs.getRowCount() );

      // test the row
      lDs.next();
      testLeftEngineTask( lDs, 5 );

      // test the row
      lDs.next();
      testRightEngineTask( lDs );

      // set the due date
      setDueDate( LEFT_ENGINE_TASK_DEADLINE_KEY, null );
      setDueDate( AIRCRAFT_COMPONENT_TASK_DEADLINE_KEY, null );
   }


   /**
    * create the test data
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @BeforeClass
   public static void loadData() throws Exception {
      XmlLoader.load( sDatabaseConnectionRule.getConnection(), UnassignedTaskTest.class,
            new StationCapacityData().getDataFile() );
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), UnassignedTaskTest.class );
   }


   /**
    * Execute the query.
    *
    * @param aAircraft
    *           aircraft
    * @param aDayCount
    *           day count
    * @param aLocation
    *           location
    * @param aDate
    *           date
    *
    * @return the result
    */
   private DataSet execute( AircraftKey aAircraft, int aDayCount, LocationKey aLocation,
         Date aDate ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aAircraft, new String[] { "aInvDbId", "aInvId" } );
      lArgs.add( "aDayCount", aDayCount );
      lArgs.add( aLocation, new String[] { "aSupplyLocDbId", "aSupplyLocId" } );
      lArgs.add( "aDate", aDate );

      // Execute the query
      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Returns the value of the due date property.
    *
    * @param aDayCount
    *           the day count
    *
    * @return the value of the due date property.
    */
   private Date getDueDate( int aDayCount ) {

      Calendar lDueDate = GregorianCalendar.getInstance();
      lDueDate.add( Calendar.DAY_OF_MONTH, aDayCount );
      lDueDate.set( GregorianCalendar.HOUR_OF_DAY, 0 );
      lDueDate.set( GregorianCalendar.MINUTE, 0 );
      lDueDate.set( GregorianCalendar.SECOND, 0 );
      lDueDate.set( GregorianCalendar.MILLISECOND, 0 );

      return lDueDate.getTime();
   }


   /**
    * sets the due date
    *
    * @param aEventDeadline
    *           the pk
    * @param aDueDate
    *           the due date
    */
   private void setDueDate( EventDeadlineKey aEventDeadline, Date aDueDate ) {

      DataSetArgument lSetArgs = new DataSetArgument();
      lSetArgs.add( "sched_dead_dt", aDueDate );

      // set the due date to 5 days from now for the first task
      MxDataAccess.getInstance().executeUpdate( "evt_sched_dead", lSetArgs,
            aEventDeadline.getPKWhereArg() );
   }


   /**
    * Sets a new value for the parent property.
    *
    * @param aTask
    *           the new value for the parent property.
    * @param aParent
    *           the new value for the parent property.
    */
   private void setParent( TaskKey aTask, TaskKey aParent ) {
      EvtEventTable lEvtEvent = EvtEventTable.findByPrimaryKey( aTask.getEventKey() );
      lEvtEvent.setHEvent( ( aParent == null ) ? null : aParent.getEventKey() );
      lEvtEvent.setNhEvent( ( aParent == null ) ? null : aParent.getEventKey() );
      lEvtEvent.update();
   }


   /**
    * Tests the aircraft task
    *
    * @param aDs
    *           the dataset
    * @param aDayCount
    *           DOCUMENT ME!
    */
   private void testAircraftTask( DataSet aDs, int aDayCount ) {
      testRow( aDs, new TaskKey( 4650, 102 ), "aircraft component task", "HIGH", 10.0, 10.0, 20.0,
            getDueDate( aDayCount ), "US", "CYCLES", 1.0, null, new InventoryKey( 4650, 104 ),
            "aircraft component", null, null, null, null, 1, 1 );
   }


   /**
    * Tests the left engine task
    *
    * @param aDs
    *           the dataset
    * @param aDayCount
    *           the day count
    */
   private void testLeftEngineTask( DataSet aDs, int aDayCount ) {
      testRow( aDs, new TaskKey( 4650, 100 ), "left engine component task", "HIGH", 5.0, 5.0, 10.0,
            getDueDate( aDayCount ), "US", "HOUR", 0.041667, null, new InventoryKey( 4650, 102 ),
            "left engine component", new InventoryKey( 4650, 100 ), "left engine", "LOW", "ENGINE",
            1, 1 );
   }


   /**
    * Tests the right engine task
    *
    * @param aDs
    *           the dataset
    */
   private void testRightEngineTask( DataSet aDs ) {
      testRightEngineTask( aDs, 1, 1 );
   }


   /**
    * Tests the right engine task
    *
    * @param aDs
    *           the dataset
    * @param aToolCapacity
    *           the tool capacity
    * @param aPartCapacity
    *           the part capacity
    */
   private void testRightEngineTask( DataSet aDs, Integer aToolCapacity, Integer aPartCapacity ) {
      testRow( aDs, RIGHT_ENGINE_TASK, "right engine component task", null, null, null, null, null,
            null, null, null, "Off Wing", new InventoryKey( 4650, 103 ), "right engine component",
            new InventoryKey( 4650, 101 ), "right engine", "HIGH", "ENGINE", aToolCapacity,
            aPartCapacity );
   }


   /**
    * Tests a row in the dataset
    *
    * @param aDs
    *           the dataset
    * @param aTaskKey
    *           the task key
    * @param aEventSdesc
    *           the task description
    * @param aSchedPriorityCd
    *           the scheduled priority
    * @param aDeviationQt
    *           the deviation
    * @param aPredDaysRemQt
    *           the predicted days remaining
    * @param aUsageRemQt
    *           the usage remaining
    * @param aSchedDeadDt
    *           the due date
    * @param aDomainTypeCd
    *           the domain type
    * @param aEngUnitCd
    *           the engineering unit
    * @param aEngUnitMultQt
    *           the engineering unit multiplier
    * @param aTaskMustRemoveUserCd
    *           the must be removed indicator
    * @param aInventoryKey
    *           the inventory
    * @param aInvNoSdesc
    *           the inventory description
    * @param aParentAssemblyInvKey
    *           the parent assembly
    * @param aParentAssemblyInvSdesc
    *           the parent assembly description
    * @param aTaskPriorityCd
    *           the task priority
    * @param aWorkTypeCd
    *           the task work type
    * @param aToolsCapacity
    *           the tool capacity check
    * @param aPartsCapacity
    *           the part capacity check
    */
   private void testRow( DataSet aDs, TaskKey aTaskKey, String aEventSdesc, String aSchedPriorityCd,
         Double aDeviationQt, Double aPredDaysRemQt, Double aUsageRemQt, Date aSchedDeadDt,
         String aDomainTypeCd, String aEngUnitCd, Double aEngUnitMultQt,
         String aTaskMustRemoveUserCd, InventoryKey aInventoryKey, String aInvNoSdesc,
         InventoryKey aParentAssemblyInvKey, String aParentAssemblyInvSdesc, String aTaskPriorityCd,
         String aWorkTypeCd, Integer aToolsCapacity, Integer aPartsCapacity ) {

      MxAssert.assertEquals( "task_key", aTaskKey.toString(), aDs.getString( "task_key" ) );
      MxAssert.assertEquals( "event_sdesc", aEventSdesc, aDs.getString( "event_sdesc" ) );
      MxAssert.assertEquals( "sched_priority_cd", aSchedPriorityCd,
            aDs.getString( "sched_priority_cd" ) );
      MxAssert.assertEquals( "deviation_qt", aDeviationQt, aDs.getDoubleObj( "deviation_qt" ) );
      MxAssert.assertEquals( "usage_rem_qt", aUsageRemQt, aDs.getDoubleObj( "usage_rem_qt" ) );
      MxAssert.assertEquals( "sched_dead_dt", aSchedDeadDt, aDs.getDate( "sched_dead_dt" ) );
      MxAssert.assertEquals( "domain_type_cd", aDomainTypeCd, aDs.getString( "domain_type_cd" ) );
      MxAssert.assertEquals( "eng_unit_cd", aEngUnitCd, aDs.getString( "eng_unit_cd" ) );
      MxAssert.assertEquals( "eng_unit_mult_qt", aEngUnitMultQt,
            aDs.getDoubleObj( "eng_unit_mult_qt" ) );
      MxAssert.assertEquals( "must_remove_user_cd", aTaskMustRemoveUserCd,
            aDs.getString( "must_remove_user_cd" ) );
      MxAssert.assertEquals( "inventory_key", aInventoryKey.toString(),
            aDs.getString( "inventory_key" ) );
      MxAssert.assertEquals( "inv_no_sdesc", aInvNoSdesc, aDs.getString( "inv_no_sdesc" ) );
      MxAssert.assertEquals( "parent_assembly_inv_key",
            ( aParentAssemblyInvKey == null ) ? null : aParentAssemblyInvKey.toString(),
            aDs.getString( "parent_assembly_inv_key" ) );
      MxAssert.assertEquals( "parent_assembly_inv_sdesc", aParentAssemblyInvSdesc,
            aDs.getString( "parent_assembly_inv_sdesc" ) );
      MxAssert.assertEquals( "task_priority_cd", aTaskPriorityCd,
            aDs.getString( "task_priority_cd" ) );
      MxAssert.assertEquals( "work_type_cd", aWorkTypeCd, aDs.getString( "work_type_cd" ) );
      MxAssert.assertEquals( "tools_capacity", aToolsCapacity, aDs.getInteger( "tools_capacity" ) );
      MxAssert.assertEquals( "parts_capacity", aPartsCapacity, aDs.getInteger( "parts_capacity" ) );
   }
}
