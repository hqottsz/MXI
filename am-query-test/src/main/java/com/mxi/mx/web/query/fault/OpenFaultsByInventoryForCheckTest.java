package com.mxi.mx.web.query.fault;

import static com.mxi.am.domain.Domain.createAircraft;
import static com.mxi.mx.common.utils.DateUtils.addDays;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.TaskKey;


/**
 * Unit test for com.mxi.mx.core.query.fault.OpenFaultsbyInventoryForCheck.qrx
 *
 * Note: that it is recognized that this test class only exercises a small subset of the behaviours
 * provided by the query.
 *
 */
public class OpenFaultsByInventoryForCheckTest {

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static final boolean SHOW_UNASSIGNED_FAULTS_ONLY = false;

   private static final boolean NA_HIDE_NON_EXECUTABLE_FAULTS = true;
   private static final String NA_WORK_LOCATION = null;


   /**
    *
    * Verify that the query returns faults that are candidates to be assigned to a work package, in
    * particular ones with a corrective task having no deadlines.
    *
    * <pre>
    * The criteria for the faults being a candidate are:
    *  - they are against the provided aircraft
    *  - they are unassigned
    *  - they have a non-historical corrective task
    *  - the corrective task has no deadlines
    *
    *  Notes:
    *  - The provided window is determined by adding the provided "day count" to the current date.
    *  - The query has two other arguments that are not applicable to this test;
    *       "hide non-executable faults" and "work location"
    * </pre>
    *
    */
   @Test
   public void returnsFaultMeetingMinimumCriteria() {

      // Given a maximum number of days in the future to find faults, the query calls this the
      // "day count".
      int dayCount = 10;

      // Given an aircraft.
      InventoryKey aircraft = createAircraft();

      // Given a non-historical corrective task against the aircraft and the task has no deadlines.
      TaskKey correctiveTask = Domain.createCorrectiveTask( corr -> {
         corr.setInventory( aircraft );
         corr.setHistoricalBool( false );
      } );

      // Given an unassigned fault against the aircraft using the corrective task.
      Domain.createFault( fault -> {
         fault.setInventory( aircraft );
         fault.setCorrectiveTask( correctiveTask );
      } );

      // When the query is executed.
      QuerySet qs = execute( aircraft, dayCount, SHOW_UNASSIGNED_FAULTS_ONLY,
            NA_HIDE_NON_EXECUTABLE_FAULTS, NA_WORK_LOCATION );

      // Then the corrective task is returned.
      // (to be more specific, the event representing the corrective task is returned).
      assertThat( "Unexpected number of rows returned.", qs.getRowCount(), is( 1 ) );
      qs.next();
      EventKey returnedCorrectiveTaskEventKey = qs.getKey( EventKey.class, "event_key" );
      assertThat( "Unexpected event key of the corrective task returned.",
            returnedCorrectiveTaskEventKey, is( correctiveTask.getEventKey() ) );

   }


   /**
    *
    * Verify that the query returns faults that are candidates to be assigned to a work package, in
    * particular ones with a corrective task having a deadline with no due date.
    *
    * <pre>
    * The criteria for the faults being a candidate are:
    *  - they are against the provided aircraft
    *  - they are unassigned
    *  - they have a non-historical corrective task
    *  - the corrective task has a deadline with no due date
    *
    *  Notes:
    *  - The provided window is determined by adding the provided "day count" to the current date.
    *  - The query has two other arguments that are not applicable to this test;
    *       "hide non-executable faults" and "work location"
    * </pre>
    *
    */
   @Test
   public void returnsFaultWithCorrTaskHavingDeadlineWithNoDueDate() {

      // Given a maximum number of days in the future to find faults, the query calls this the
      // "day count".
      int dayCount = 10;

      // Given an aircraft.
      InventoryKey aircraft = createAircraft();

      // Given a non-historical corrective task against the aircraft
      // and the task has a deadline with a due date that is before the max date to find faults.
      TaskKey correctiveTask = Domain.createCorrectiveTask( corr -> {
         corr.setInventory( aircraft );
         corr.setHistoricalBool( false );
         corr.addDeadline( deadline -> {
            deadline.setDueDate( null );
         } );
      } );

      // Given an unassigned fault against the aircraft using the corrective task.
      Domain.createFault( fault -> {
         fault.setInventory( aircraft );
         fault.setCorrectiveTask( correctiveTask );
      } );

      // When the query is executed.
      QuerySet qs = execute( aircraft, dayCount, SHOW_UNASSIGNED_FAULTS_ONLY,
            NA_HIDE_NON_EXECUTABLE_FAULTS, NA_WORK_LOCATION );

      // Then the corrective task is returned.
      // (to be more specific, the event representing the corrective task is returned).
      assertThat( "Unexpected number of rows returned.", qs.getRowCount(), is( 1 ) );
      qs.next();
      EventKey returnedCorrectiveTaskEventKey = qs.getKey( EventKey.class, "event_key" );
      assertThat( "Unexpected event key of the corrective task returned.",
            returnedCorrectiveTaskEventKey, is( correctiveTask.getEventKey() ) );

   }


   /**
    *
    * Verify that the query returns faults that are candidates to be assigned to a work package, in
    * particular ones with a corrective task having a deadline with a due date within the "day
    * count" window.
    *
    * <pre>
    * The criteria for the faults being a candidate are:
    *  - they are against the provided aircraft
    *  - they are unassigned
    *  - they have a non-historical corrective task
    *  - the corrective task has a deadline whose due date is within the provided window
    *
    *  Notes:
    *  - The provided window is determined by adding the provided "day count" to the current date.
    *  - The query has two other arguments that are not applicable to this test;
    *       "hide non-executable faults" and "work location"
    * </pre>
    *
    */
   @Test
   public void returnsFaultWithCorrTaskHavingDeadlineDueWithinWindow() {

      // Given a maximum number of days in the future to find faults, the query calls this the
      // "day count".
      int dayCount = 10;

      // Given an aircraft.
      InventoryKey aircraft = createAircraft();

      // Given a non-historical corrective task against the aircraft
      // and the task has a deadline with a due date that is before the max date to find faults.
      Date today = new Date();
      Date maxDateToFindFaults = addDays( today, dayCount );
      Date dueDate = addDays( maxDateToFindFaults, -1 );

      TaskKey correctiveTask = Domain.createCorrectiveTask( corr -> {
         corr.setInventory( aircraft );
         corr.setHistoricalBool( false );
         corr.addDeadline( deadline -> {
            deadline.setDueDate( dueDate );
         } );
      } );

      // Given an unassigned fault against the aircraft using the corrective task.
      Domain.createFault( fault -> {
         fault.setInventory( aircraft );
         fault.setCorrectiveTask( correctiveTask );
      } );

      // When the query is executed.
      QuerySet qs = execute( aircraft, dayCount, SHOW_UNASSIGNED_FAULTS_ONLY,
            NA_HIDE_NON_EXECUTABLE_FAULTS, NA_WORK_LOCATION );

      // Then the corrective task is returned.
      // (to be more specific, the event representing the corrective task is returned).
      assertThat( "Unexpected number of rows returned.", qs.getRowCount(), is( 1 ) );
      qs.next();
      EventKey returnedCorrectiveTaskEventKey = qs.getKey( EventKey.class, "event_key" );
      assertThat( "Unexpected event key of the corrective task returned.",
            returnedCorrectiveTaskEventKey, is( correctiveTask.getEventKey() ) );

   }


   /**
    *
    * Verify that the query does not return faults that have all the correct criteria except they
    * have a corrective task having a deadline with a due date after the "day count" window.
    *
    * (Refer to #returnsFaultWithCorrTaskHavingDeadlineDueWithinWindow for the correct criteria)
    *
    * <pre>
    * The criteria for the faults being a candidate are:
    *  - they are against the provided aircraft
    *  - they are unassigned
    *  - they have a non-historical corrective task
    *  - the corrective task has a deadline whose due date is before the provided window (*)
    *
    *  Notes:
    *  - The provided window is determined by adding the provided "day count" to the current date.
    *  - The query has two other arguments that are not applicable to this test;
    *       "hide non-executable faults" and "work location"
    * </pre>
    *
    */
   @Test
   public void doesNotReturnFaultsWhoseCorrTaskHasDeadlineWithDueDateAfterWindow() {

      // Given a maximum number of days in the future to find faults, the query calls this the
      // "day count".
      int dayCount = 10;

      // Given an aircraft.
      InventoryKey aircraft = createAircraft();

      // Given a non-historical corrective task against the aircraft
      // and the task has a deadline with a due date that is AFTER the max date to find faults.
      Date today = new Date();
      Date maxDateToFindFaults = addDays( today, dayCount );
      Date dueDate = addDays( maxDateToFindFaults, 1 );

      TaskKey correctiveTask = Domain.createCorrectiveTask( corr -> {
         corr.setInventory( aircraft );
         corr.setHistoricalBool( false );
         corr.addDeadline( deadline -> {
            deadline.setDueDate( dueDate );
         } );
      } );

      // Given an unassigned fault against the aircraft using the corrective task.
      Domain.createFault( fault -> {
         fault.setInventory( aircraft );
         fault.setCorrectiveTask( correctiveTask );
      } );

      // When the query is executed.
      QuerySet qs = execute( aircraft, dayCount, SHOW_UNASSIGNED_FAULTS_ONLY,
            NA_HIDE_NON_EXECUTABLE_FAULTS, NA_WORK_LOCATION );

      // Then the no rows are returned.
      assertThat( "Unexpected number of rows returned.", qs.getRowCount(), is( 0 ) );
   }


   /**
    *
    * Verify that the query does not return faults that have all the correct criteria except they
    * are against another aircraft.
    *
    * (Refer to #returnsFaultMeetingMinimumCriteria for the correct criteria)
    *
    * <pre>
    * The criteria for the faults being a candidate are:
    *  - they are against the provided aircraft (*)
    *  - they are unassigned
    *  - they have a non-historical corrective task with no deadlines
    *
    *  Notes:
    *  - The provided window is determined by adding the provided "day count" to the current date.
    *  - The query has two other arguments that are not applicable to this test;
    *       "hide non-executable faults" and "work location"
    * </pre>
    *
    */
   @Test
   public void doesNotReturnFaultsAgainstOtherAircraft() {

      // Given a maximum number of days in the future to find faults, the query calls this the
      // "day count".
      int dayCount = 10;

      // Given an aircraft.
      InventoryKey aircraft = createAircraft();

      // Given a non-historical corrective task against the aircraft and the task has no deadlines.
      TaskKey correctiveTask = Domain.createCorrectiveTask( corr -> {
         corr.setInventory( aircraft );
         corr.setHistoricalBool( false );
      } );

      // Given an unassigned fault against the aircraft using the corrective task.
      Domain.createFault( fault -> {
         fault.setInventory( aircraft );
         fault.setCorrectiveTask( correctiveTask );
      } );

      // Given another aircraft.
      InventoryKey anotherAircraft = createAircraft();

      // When the query is executed against the other aircraft.
      QuerySet qs = execute( anotherAircraft, dayCount, SHOW_UNASSIGNED_FAULTS_ONLY,
            NA_HIDE_NON_EXECUTABLE_FAULTS, NA_WORK_LOCATION );

      // Then no rows are returned.
      assertThat( "Unexpected number of rows returned.", qs.getRowCount(), is( 0 ) );
   }


   /**
    *
    * Verify that the query does not return faults that have all the correct criteria except they
    * are assigned to a work package.
    *
    * (Refer to #returnsFaultMeetingMinimumCriteria for the correct criteria)
    *
    * <pre>
    * The criteria for the faults being a candidate are:
    *  - they are against the provided aircraft
    *  - they are unassigned (*)
    *  - they have a non-historical corrective task with no deadlines
    *
    *  Notes:
    *  - The provided window is determined by adding the provided "day count" to the current date.
    *  - The query has two other arguments that are not applicable to this test;
    *       "hide non-executable faults" and "work location"
    * </pre>
    *
    */
   @Test
   public void doesNotReturnFaultsAlreadyAssignedToAWorkPackageWhenShowUnassignedFaultsIsTrue() {

      // Given a maximum number of days in the future to find faults, the query calls this the
      // "day count".
      int dayCount = 10;

      // Given an aircraft.
      InventoryKey aircraft = createAircraft();

      // Given a non-historical corrective task against the aircraft and the task has no deadlines.
      TaskKey correctiveTask = Domain.createCorrectiveTask( corr -> {
         corr.setInventory( aircraft );
         corr.setHistoricalBool( false );
      } );

      // Given a fault against the aircraft using the corrective task.
      Domain.createFault( fault -> {
         fault.setInventory( aircraft );
         fault.setCorrectiveTask( correctiveTask );
      } );

      // Given the fault (more accurately, its corrective task) is assigned to a work package.
      Domain.createWorkPackage( wp -> {
         wp.addTask( correctiveTask );
      } );

      // When the query is executed against the other aircraft.
      QuerySet qs = execute( aircraft, dayCount, SHOW_UNASSIGNED_FAULTS_ONLY,
            NA_HIDE_NON_EXECUTABLE_FAULTS, NA_WORK_LOCATION );

      // Then no rows are returned.
      assertThat( "Unexpected number of rows returned.", qs.getRowCount(), is( 0 ) );
   }


   /**
    *
    * Verify that the query does not return faults that have all the correct criteria except they
    * are historical.
    *
    * (Refer to #returnsFaultMeetingMinimumCriteria for the correct criteria)
    *
    * <pre>
    * The criteria for the faults being a candidate are:
    *  - they are against the provided aircraft
    *  - they are unassigned
    *  - they have a non-historical corrective task with no deadlines (*)
    *
    *  Notes:
    *  - The provided window is determined by adding the provided "day count" to the current date.
    *  - The query has two other arguments that are not applicable to this test;
    *       "hide non-executable faults" and "work location"
    * </pre>
    *
    */
   @Test
   public void doesNotReturnHistoricalFaults() {

      // Given a maximum number of days in the future to find faults, the query calls this the
      // "day count".
      int dayCount = 10;

      // Given an aircraft.
      InventoryKey aircraft = createAircraft();

      // Given a historical corrective task against the aircraft and the task has no deadlines.
      TaskKey correctiveTask = Domain.createCorrectiveTask( corr -> {
         corr.setInventory( aircraft );
         corr.setHistoricalBool( true );
      } );

      // Given an unassigned fault against the aircraft using the corrective task.
      Domain.createFault( fault -> {
         fault.setInventory( aircraft );
         fault.setCorrectiveTask( correctiveTask );
      } );

      // When the query is executed against the other aircraft.
      QuerySet qs = execute( aircraft, dayCount, SHOW_UNASSIGNED_FAULTS_ONLY,
            NA_HIDE_NON_EXECUTABLE_FAULTS, NA_WORK_LOCATION );

      // Then no rows are returned.
      assertThat( "Unexpected number of rows returned.", qs.getRowCount(), is( 0 ) );
   }


   private QuerySet execute( InventoryKey aircraft, int dayCount, boolean showAssignedFaults,
         boolean hideNonExecutableFaults, String workLocation ) {

      DataSetArgument args = new DataSetArgument();
      args.add( aircraft, "aInvNoDbId", "aInvNoId" );
      args.add( "aDayCount", dayCount );
      args.add( "aShowAssignedFaults", showAssignedFaults );
      args.add( "aHideNonExecutableFaults", hideNonExecutableFaults );
      args.add( "aWorkLocation", workLocation );

      return QueryExecutor.executeQuery( databaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), args );
   }

}
