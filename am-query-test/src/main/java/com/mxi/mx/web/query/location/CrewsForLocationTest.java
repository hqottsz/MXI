package com.mxi.mx.web.query.location;

import static com.mxi.am.domain.Domain.createHumanResource;
import static com.mxi.am.domain.Domain.createLocation;
import static com.mxi.mx.core.key.RefEventStatusKey.COMMIT;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.DepartmentKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.TaskKey;


/**
 * Integration unit test for CrewsForLocation.qrx
 *
 */
public class CrewsForLocationTest {

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule injectionOverrideRule = new InjectionOverrideRule();

   private static final String CREW_CODE1 = "EDCREW001";
   private static final String CREW_CODE2 = "EDCREW002";


   /**
    * Verify that crew available to be assigned to tasks when planning a shift for a scheduled work
    * package. (PlanShift - Can assigned one crew to Task).
    */

   @Test
   public void returnAssignCrewOnTask() throws Exception {

      // Given the human resource, but it is not related to this scenario.
      HumanResourceKey authHr = createHumanResource();

      // Given a location final
      LocationKey location = createLocation();

      InventoryKey aircraft = Domain.createAircraft();

      // Given crews are associated with same location.
      DepartmentKey crew1 = Domain.createCrew( crew -> {
         crew.setCode( CREW_CODE1 );
         crew.setName( CREW_CODE1 );
         crew.setLocations( location );
      } );

      DepartmentKey crew2 = Domain.createCrew( crew -> {
         crew.setCode( CREW_CODE2 );
         crew.setName( CREW_CODE2 );
         crew.setLocations( location );
      } );

      // Given a adhoc task with a aircraft.
      TaskKey adhocTask1 = Domain.createAdhocTask( task -> {
         task.setInventory( aircraft );
         task.setCrew( crew1 );
      } );

      TaskKey adhocTask2 = Domain.createAdhocTask( task -> {
         task.setInventory( aircraft );
         task.setCrew( crew1 );
      } );

      // Given schedule work package with the tasks assigned.
      Domain.createWorkPackage( wp -> {
         wp.setLocation( location );
         wp.setScheduledStartDate( new Date() );
         wp.setScheduledEndDate( new Date() );
         wp.addTask( adhocTask1 );
         wp.addTask( adhocTask2 );
      } );

      // When the query is executed.
      QuerySet lQs = executeQuery( location, adhocTask1 );

      // Asserts that 2 crews are returned.
      assertEquals( "Unexpected number of rows returned.", 2, lQs.getRowCount() );

      // Then number of assigned tasks of crew1 and crew2 returned.
      List<DepartmentKey> actualCrews = new ArrayList<>();
      while ( lQs.next() ) {
         actualCrews.add( lQs.getKey( DepartmentKey.class, "dept_key" ) );
      }
      assertThat( "Unexpected crews returned.", actualCrews, containsInAnyOrder( crew1, crew2 ) );
   }


   /**
    * Verify that crew available to be assigned to a committed work package (Work Package Details -
    * Can assigned multiple crews to work package).
    */
   @Test
   public void returnAssignCrewToWorkPackage() throws Exception {

      // Given the crews location.
      final LocationKey location = createLocation();

      // Given 2 crews are associated with same location.
      DepartmentKey crew1 = Domain.createCrew( crew -> {
         crew.setCode( CREW_CODE1 );
         crew.setName( CREW_CODE1 );
         crew.setLocations( location );
      } );

      DepartmentKey crew2 = Domain.createCrew( crew -> {
         crew.setCode( CREW_CODE2 );
         crew.setName( CREW_CODE2 );
         crew.setLocations( location );
      } );

      // Given 2 workpackage1 and workpackage2
      TaskKey workPackage1 = Domain.createWorkPackage( wp -> {
         wp.setStatus( COMMIT );
         wp.setLocation( location );
         wp.setScheduledStartDate( new Date() );
         wp.addCrew( crew1 );
         wp.addCrew( crew2 );
      } );

      Domain.createWorkPackage( wp -> {
         wp.setStatus( COMMIT );
         wp.setLocation( location );
         wp.setScheduledStartDate( new Date() );
         wp.addCrew( crew1 );
      } );

      // When the query is executed.
      QuerySet lQs = executeQuery( location, workPackage1 );

      // Asserts that 2 crews are returned.
      assertEquals( 2, lQs.getRowCount() );

      // Then number of assigned_task of crew1 and crew2 returned.
      while ( lQs.next() ) {
         if ( crew1.equals( lQs.getKey( DepartmentKey.class, "dept_key" ) ) ) {
            assertEquals( 2, lQs.getInt( "assigned_tasks" ) );
         } else if ( crew2.equals( lQs.getKey( DepartmentKey.class, "dept_key" ) ) ) {
            assertEquals( 1, lQs.getInt( "assigned_tasks" ) );
         } else {
            fail( "Neither crew1 and crew2 is in data set" );
         }
      }
   }


   private QuerySet executeQuery( LocationKey location, TaskKey task ) {

      DataSetArgument args = new DataSetArgument();
      args.add( location, "aLocDbId", "aLocId" );
      args.add( task, "aTaskDbId", "aTaskId" );
      args.add( "aHours", 8.0 );
      return QueryExecutor.executeQuery( databaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), args );
   }

}
