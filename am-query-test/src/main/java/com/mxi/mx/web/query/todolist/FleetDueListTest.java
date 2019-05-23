package com.mxi.mx.web.query.todolist;

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AircraftKey;
import com.mxi.mx.core.key.AuthorityKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.production.aircraft.domain.AircraftAuthorityChangedEvent;
import com.mxi.mx.core.production.fleetduelist.domain.FleetDueListAircraftProjection;
import com.mxi.mx.core.production.fleetduelist.domain.FleetDueListTaskProjection;
import com.mxi.mx.core.production.task.domain.TaskCreatedEvent;


/**
 * Tests the query com.mxi.mx.web.query.todolist.FleetDueList
 */
public final class FleetDueListTest {

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule injectionOverrideRule = new InjectionOverrideRule();

   private FleetDueListAircraftProjection fleetDueListAircraftProjection;
   private FleetDueListTaskProjection fleetDueListTaskProjection;

   private final AircraftKey aircraftKey = new AircraftKey( 4650, 1 );
   private final AircraftKey aircraftKey2 = new AircraftKey( 4650, 2 );
   private final AircraftKey aircraftKey3 = new AircraftKey( 4650, 3 );
   private final AircraftKey aircraftKey4 = new AircraftKey( 4650, 4 );
   private final TaskKey aircraftTaskKey = new TaskKey( 4650, 101 );
   private final TaskKey aircraft2TaskKey = new TaskKey( 4650, 200 );
   private final TaskKey aircraft3TaskKey = new TaskKey( 4650, 501 );
   private final TaskKey aircraft4TaskKey = new TaskKey( 4650, 600 );
   private final AuthorityKey authorityKey = new AuthorityKey( 4650, 1 );


   @Before
   public void setup() {
      DataLoaders.load( databaseConnectionRule.getConnection(), FleetDueListTest.class );

      // Assume that event handlers are properly executed by the Axon framework.
      fleetDueListAircraftProjection = new FleetDueListAircraftProjection();
      fleetDueListTaskProjection = new FleetDueListTaskProjection();

      fleetDueListTaskProjection.on( new TaskCreatedEvent( aircraftTaskKey, null, null,
            aircraftKey.getInventoryKey(), null, null, null, false, false, false, null, 0, true ) );
      fleetDueListTaskProjection
            .on( new TaskCreatedEvent( aircraft2TaskKey, null, null, aircraftKey2.getInventoryKey(),
                  null, null, null, false, false, false, null, 0, true ) );
      fleetDueListTaskProjection
            .on( new TaskCreatedEvent( aircraft3TaskKey, null, null, aircraftKey3.getInventoryKey(),
                  null, null, null, false, false, false, null, 0, true ) );
      fleetDueListTaskProjection
            .on( new TaskCreatedEvent( aircraft4TaskKey, null, null, aircraftKey4.getInventoryKey(),
                  null, null, null, false, false, false, null, 0, true ) );

      fleetDueListAircraftProjection.on( new AircraftAuthorityChangedEvent( aircraftKey,
            authorityKey, new Date(), HumanResourceKey.ADMIN ) );
      fleetDueListAircraftProjection.on( new AircraftAuthorityChangedEvent( aircraftKey2,
            authorityKey, new Date(), HumanResourceKey.ADMIN ) );
      fleetDueListAircraftProjection.on( new AircraftAuthorityChangedEvent( aircraftKey3,
            authorityKey, new Date(), HumanResourceKey.ADMIN ) );
      fleetDueListAircraftProjection.on( new AircraftAuthorityChangedEvent( aircraftKey4,
            authorityKey, new Date(), HumanResourceKey.ADMIN ) );
   }


   /**
    * execute the query
    *
    */
   private DataSet execute( DataSetArgument args ) {

      // Execute the query
      return QueryExecutor.executeQuery( databaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), args );
   }


   /**
    * Tests the query with unassigned tasks and tasks that are assigned to a check
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testQueryIncludeTaskAssignedToACheck() throws Exception {

      // DATA SETUP
      List<String> assemblies = new ArrayList<String>();
      assemblies.add( "A320" );
      List<String> groups = new ArrayList<String>();
      groups.add( "1001" );
      DataSetArgument args = new DataSetArgumentBuilder().assembliesToInclude( assemblies )
            .groupsToInclude( groups ).build();

      // ACT
      DataSet tasksDs = execute( args );

      // sort by event_key ascendant
      tasksDs.addSort( "dsString(event_key)", true );
      tasksDs.filterAndSort();

      // ASSERT
      // There should be 2 rows
      assertEquals( "Number of retrieved rows", 2, tasksDs.getRowCount() );

      // assigned task
      tasksDs.next();
      testRow( tasksDs, new EventKey( "4650:101" ), "aircraft test 1 task", "BARCODE101",
            "aircraft test 1",
            new SimpleDateFormat( "dd-MMM-yyyy hh:mm" ).parse( "5-DEC-2006 01:00" ), "ACTV",
            "MULTIPLE", "aircraft task check", "BARCODE100" );

      // unassigned task
      tasksDs.next();
      testRow( tasksDs, new EventKey( "4650:200" ), "aircraft test 1 unassigned task", "BARCODE200",
            "aircraft test 1",
            new SimpleDateFormat( "dd-MMM-yyyy hh:mm" ).parse( "5-DEC-2006 02:00" ), "ACTV",
            "ADMIN", null, null );
   }


   /**
    * Tests the query with unassigned tasks.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testQueryUnassignedTask() throws Exception {

      // DATA SETUP
      List<String> assemblies = new ArrayList<String>();
      assemblies.add( "A320" );
      List<String> groups = new ArrayList<String>();
      groups.add( "1001" );
      DataSetArgumentBuilder dataSetArgumentBuilder = new DataSetArgumentBuilder();
      DataSetArgument args = dataSetArgumentBuilder.assembliesToInclude( assemblies )
            .groupsToInclude( groups ).includeAssignedToChecks( false ).build();

      // ACT
      DataSet unassignedTasksDs = execute( args );

      // ASSERT
      // There should be 1 row
      assertEquals( "Number of retrieved rows", 1, unassignedTasksDs.getRowCount() );

      // unassigned task
      unassignedTasksDs.next();
      testRow( unassignedTasksDs, new EventKey( "4650:200" ), "aircraft test 1 unassigned task",
            "BARCODE200", "aircraft test 1",
            new SimpleDateFormat( "dd-MMM-yyyy hh:mm" ).parse( "5-DEC-2006 02:00" ), "ACTV",
            "ADMIN", null, null );
   }


   /**
    * Tests the query with an unauthorized user
    *
    */
   @Test
   public void testQueryUnauthorizedUser() {
      int unauthUserId = 100000000;
      // execute the query
      DataSetArgument args = new DataSetArgumentBuilder().hrId( unauthUserId ).build();

      DataSet assignedTasksDs = execute( args );

      // There should be no row
      assertEquals( "Number of retrieved rows", 0, assignedTasksDs.getRowCount() );
   }


   /**
    * Tests the proper amount of rows are returned when selecting an assembly and a group
    *
    */
   @Test
   public void testQueryIncludeOneAssemblyAndOneGroup() {

      // DATA SETUP
      List<String> assemblies = new ArrayList<String>();
      assemblies.add( "A320" );
      List<String> groups = new ArrayList<String>();
      groups.add( "1001" );
      DataSetArgument args = new DataSetArgumentBuilder().assembliesToInclude( assemblies )
            .groupsToInclude( groups ).build();

      // ACT
      DataSet fleetDueListDs = execute( args );

      // ASSERT
      Assert.assertNotNull( fleetDueListDs );
      assertEquals( "Number of retrieved rows", 2, fleetDueListDs.getRowCount() );

   }


   /**
    * Tests the proper amount of rows are returned when selecting multiple assemblies and a group
    *
    */
   @Test
   public void testQueryIncludeMultipleAssembliesAndOneGroup() {

      // DATA SETUP
      List<String> assemblies = new ArrayList<String>();
      assemblies.add( "A320" );
      assemblies.add( "F-2000" );
      List<String> groups = new ArrayList<String>();
      groups.add( "1001" );
      DataSetArgument args = new DataSetArgumentBuilder().assembliesToInclude( assemblies )
            .groupsToInclude( groups ).build();

      // ACT
      DataSet fleetDueListDs = execute( args );

      // ASSERT
      Assert.assertNotNull( fleetDueListDs );
      assertEquals( "Number of retrieved rows", 4, fleetDueListDs.getRowCount() );

   }


   /**
    * Tests the proper amount of rows are returned when selecting multiple assemblies and multiple
    * groups
    *
    */
   @Test
   public void testQueryIncludeMultiAssembliesAndMultiGroups() {

      // DATA SETUP
      List<String> assemblies = new ArrayList<String>();
      assemblies.add( "A320" );
      assemblies.add( "F-2000" );
      List<String> groups = new ArrayList<String>();
      groups.add( "1001" );
      groups.add( "1002" );
      DataSetArgument args = new DataSetArgumentBuilder().assembliesToInclude( assemblies )
            .groupsToInclude( groups ).build();

      // ACT
      DataSet fleetDueListDs = execute( args );

      // ASSERT
      Assert.assertNotNull( fleetDueListDs );
      assertEquals( "Number of retrieved rows", 4, fleetDueListDs.getRowCount() );

   }


   /**
    * Tests the proper amount of rows are returned when selecting no groups
    *
    */
   @Test
   public void testQueryIncludeNoGroups() {

      // DATA SETUP
      DataSetArgument args = new DataSetArgumentBuilder().build();

      // ACT
      DataSet fleetDueListDs = execute( args );

      // ASSERT
      Assert.assertNotNull( fleetDueListDs );
      assertEquals( "Number of retrieved rows", 4, fleetDueListDs.getRowCount() );

   }


   /**
    * Tests a row in the dataset
    *
    * @param ds
    *           The dataset
    * @param event
    *           The event key
    * @param taskName
    *           The task name
    * @param taskId
    *           The task ID
    * @param aircraft
    *           The aircraft
    * @param dueDate
    *           The due date
    * @param taskStatus
    *           The task status
    * @param workType
    *           The work type
    * @param checkName
    *           The check task name
    * @param checkId
    *           The check task id
    */
   private void testRow( DataSet ds, EventKey event, String taskName, String taskId,
         String aircraft, Date dueDate, String taskStatus, String workType, String checkName,
         String checkId ) {
      assertEquals( "event_key", event, new EventKey( ds.getString( "event_key" ) ) );
      assertEquals( "event_sdesc", taskName.toString(), ds.getString( "event_sdesc" ) );
      assertEquals( "barcode_sdesc", taskId.toString(), ds.getString( "barcode_sdesc" ) );
      assertEquals( "inv_no_sdesc", aircraft.toString(), ds.getString( "inv_no_sdesc" ) );
      assertEquals( "sched_dead_dt", dueDate, ds.getDate( "sched_dead_dt" ) );
      assertEquals( "event_status_cd", taskStatus, ds.getString( "event_status_cd" ) );
      assertEquals( "work_type_cd", workType, ds.getString( "work_type_cd" ) );
      assertEquals( "check_sdesc", checkName, ds.getString( "check_sdesc" ) );
      assertEquals( "check_barcode", checkId, ds.getString( "check_barcode" ) );
   }

}
