package com.mxi.mx.core.services.fault;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.CorrectiveTask;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Fault;
import com.mxi.am.domain.Flight;
import com.mxi.am.domain.TrackedInventory;
import com.mxi.am.domain.WorkPackage;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.am.ee.OperateAsUserRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.trigger.TriggerFactory;
import com.mxi.mx.common.trigger.TriggerFactoryStub;
import com.mxi.mx.core.flight.model.FlightLegStatus;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.EventLocationKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.StageKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.plugin.ordernumber.RepairOrderNumberGenerator;
import com.mxi.mx.core.plugin.ordernumber.WorkOrderNumberGenerator;
import com.mxi.mx.core.table.evt.EvtEventDao;
import com.mxi.mx.core.table.evt.EvtLocTable;
import com.mxi.mx.core.table.evt.EvtStageDao;
import com.mxi.mx.core.trigger.MxCoreTriggerType;


public class FaultPackagingServiceIntegrationTest {

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule fakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public OperateAsUserRule operateAsUserRule = new OperateAsUserRule( 0, "SYSTEM" );

   // Object under test
   private FaultPackagingService faultPackagingService;

   // Test Data
   private InventoryKey aircraftWithNoActiveFlight;
   private InventoryKey aircraftWithOnGoingFlightInFlight;
   private InventoryKey aircraftWithOnGoingFlightOnTheGround;
   private InventoryKey aircraftWithNoActiveFlightSubInventory;
   private InventoryKey aircraftWithOnGoingFlightInFlightSubInventory;
   private InventoryKey aircraftWithOnGoingFlightOnTheGroundSubInventory;
   private HumanResourceKey humanResource;
   private TaskKey faultCorrectiveTaskWithNoActiveFlight;
   private TaskKey faultCorrectiveTaskWithOnGoingFlightInFlight;
   private TaskKey faultCorrectiveTaskWithOnGoingFlightOnTheGround;

   private static final Date CURRENT_DATE = new Date();
   private static final Date PAST_DATE = DateUtils.addHours( CURRENT_DATE, -1 );
   private static final Date FUTURE_DATE_1 = DateUtils.addHours( CURRENT_DATE, 1 );
   private static final Date FUTURE_DATE_2 = DateUtils.addHours( CURRENT_DATE, 2 );
   private static final Date FUTURE_DATE_3 = DateUtils.addHours( CURRENT_DATE, 3 );
   private static final LocationKey OTTAWA_AIRPORT = new LocationKey( "4650:200" );
   private static final LocationKey VANCOUVER_AIRPORT = new LocationKey( "4650:300" );
   private static final LocationKey OTTAWA_LINE = new LocationKey( "4650:400" );
   private static final LocationKey CHARLOTTETOWN_AIRPORT = new LocationKey( "4650:113" );
   private static final LocationKey CHARLOTTETOWN_LINE = new LocationKey( "4650:26" );


   @Before
   public void setUp() {
      DataLoaders.load( databaseConnectionRule.getConnection(), getClass() );

      faultPackagingService = new FaultPackagingServiceImpl();

      aircraftWithNoActiveFlight = createAircraft( OTTAWA_AIRPORT );
      aircraftWithOnGoingFlightInFlight = createAircraft( LocationKey.OPS );
      aircraftWithOnGoingFlightOnTheGround = createAircraft( CHARLOTTETOWN_AIRPORT );

      aircraftWithNoActiveFlightSubInventory =
            createInventoryOnAircraft( aircraftWithNoActiveFlight );
      aircraftWithOnGoingFlightInFlightSubInventory =
            createInventoryOnAircraft( aircraftWithOnGoingFlightInFlight );
      aircraftWithOnGoingFlightOnTheGroundSubInventory =
            createInventoryOnAircraft( aircraftWithOnGoingFlightOnTheGround );

      humanResource = Domain.createHumanResource();
      faultCorrectiveTaskWithNoActiveFlight =
            createFault( aircraftWithNoActiveFlightSubInventory, RefEventStatusKey.CFACTV );
      faultCorrectiveTaskWithOnGoingFlightInFlight =
            createFault( aircraftWithOnGoingFlightInFlightSubInventory, RefEventStatusKey.CFACTV );
      faultCorrectiveTaskWithOnGoingFlightOnTheGround = createFault(
            aircraftWithOnGoingFlightOnTheGroundSubInventory, RefEventStatusKey.CFACTV );

      Map<String, Object> triggerMap = new HashMap<>( 2 );
      {
         triggerMap.put( MxCoreTriggerType.WO_NUM_GEN.toString(), new WorkOrderNumberGenerator() );
         triggerMap.put( MxCoreTriggerType.RO_NUM_GEN.toString(),
               new RepairOrderNumberGenerator() );
      }
      TriggerFactory triggerFactoryStub = new TriggerFactoryStub( triggerMap );
      TriggerFactory.setInstance( triggerFactoryStub );
   }


   @Test
   public void packageOpenFaults_withOnGoingFlight_isInFlight_nextScheduled_existingCommittedWP()
         throws Throwable {
      createFlight( aircraftWithNoActiveFlight, FlightLegStatus.MXCMPLT, PAST_DATE, FUTURE_DATE_1,
            OTTAWA_AIRPORT );
      createFlight( aircraftWithNoActiveFlight, FlightLegStatus.MXPLAN, FUTURE_DATE_2,
            FUTURE_DATE_3, VANCOUVER_AIRPORT );

      TaskKey expectedCommittedWorkPackage = createWorkPackage( aircraftWithNoActiveFlight,
            FUTURE_DATE_1, OTTAWA_LINE, RefEventStatusKey.COMMIT );

      Optional<TaskKey> workPackageKey =
            faultPackagingService.packageOpenFaults( aircraftWithNoActiveFlight, humanResource );

      assertTrue( workPackageKey.isPresent() );
      assertEquals( expectedCommittedWorkPackage, workPackageKey.get() );
      assertTaskAssignedToWorkPackage( faultCorrectiveTaskWithNoActiveFlight,
            expectedCommittedWorkPackage );
      assertHistoryNoteAddedForAssignment( faultCorrectiveTaskWithNoActiveFlight );
   }


   @Test
   public void packageOpenFaults_withOnGoingFlight_isInFlight_nextScheduled_existingInWorkWP()
         throws Throwable {
      createFlight( aircraftWithNoActiveFlight, FlightLegStatus.MXOFF, PAST_DATE, FUTURE_DATE_1,
            OTTAWA_AIRPORT );
      createFlight( aircraftWithNoActiveFlight, FlightLegStatus.MXPLAN, FUTURE_DATE_2,
            FUTURE_DATE_3, VANCOUVER_AIRPORT );
      createWorkPackage( aircraftWithNoActiveFlight, FUTURE_DATE_1, OTTAWA_LINE,
            RefEventStatusKey.COMMIT );

      TaskKey expectedInWorkWorkPackage = createWorkPackage( aircraftWithNoActiveFlight, PAST_DATE,
            OTTAWA_LINE, RefEventStatusKey.IN_WORK );

      Optional<TaskKey> workPackageKey =
            faultPackagingService.packageOpenFaults( aircraftWithNoActiveFlight, humanResource );

      assertTrue( workPackageKey.isPresent() );
      assertEquals( expectedInWorkWorkPackage, workPackageKey.get() );
      assertTaskAssignedToWorkPackage( faultCorrectiveTaskWithNoActiveFlight,
            expectedInWorkWorkPackage );
      assertHistoryNoteAddedForAssignment( faultCorrectiveTaskWithNoActiveFlight );
   }


   @Test
   public void packageOpenFaults_withOnGoingFlight_isInFlight_nextScheduled_newWP()
         throws Throwable {
      createFlight( aircraftWithOnGoingFlightInFlight, FlightLegStatus.MXIN, PAST_DATE,
            FUTURE_DATE_1, OTTAWA_AIRPORT );
      createFlight( aircraftWithOnGoingFlightInFlight, FlightLegStatus.MXPLAN, FUTURE_DATE_2,
            FUTURE_DATE_3, VANCOUVER_AIRPORT );

      Optional<TaskKey> workPackageKey = faultPackagingService
            .packageOpenFaults( aircraftWithOnGoingFlightInFlight, humanResource );

      assertTrue( workPackageKey.isPresent() );

      TaskKey createdWorkPackage = workPackageKey.get();
      WorkPackageData retrievedData = getWorkPackageData( createdWorkPackage );

      assertTrue( DateUtils.truncatedEquals( FUTURE_DATE_1, retrievedData.getStartDate(),
            Calendar.SECOND ) );
      assertTrue( DateUtils.truncatedEquals( FUTURE_DATE_2, retrievedData.getEndDate(),
            Calendar.SECOND ) );
      assertEquals( OTTAWA_LINE, retrievedData.getLocationKey() );
      assertTaskAssignedToWorkPackage( faultCorrectiveTaskWithOnGoingFlightInFlight,
            createdWorkPackage );
      assertHistoryNoteAddedForAssignment( faultCorrectiveTaskWithOnGoingFlightInFlight );
   }


   @Test
   public void packageOpenFaults_withOnGoingFlight_isInFlight_noScheduled_existingCommittedWP()
         throws Throwable {
      createFlight( aircraftWithNoActiveFlight, FlightLegStatus.MXOFF, PAST_DATE, FUTURE_DATE_1,
            OTTAWA_AIRPORT );
      TaskKey expectedCommittedWorkPackage = createWorkPackage( aircraftWithNoActiveFlight,
            FUTURE_DATE_1, OTTAWA_LINE, RefEventStatusKey.COMMIT );

      Optional<TaskKey> workPackageKey =
            faultPackagingService.packageOpenFaults( aircraftWithNoActiveFlight, humanResource );

      assertTrue( workPackageKey.isPresent() );
      assertEquals( expectedCommittedWorkPackage, workPackageKey.get() );
      assertTaskAssignedToWorkPackage( faultCorrectiveTaskWithNoActiveFlight,
            expectedCommittedWorkPackage );
      assertHistoryNoteAddedForAssignment( faultCorrectiveTaskWithNoActiveFlight );
   }


   @Test
   public void packageOpenFaults_withOnGoingFlight_isInFlight_noScheduled_existingInWorkWP()
         throws Throwable {
      createFlight( aircraftWithNoActiveFlight, FlightLegStatus.MXDIVERT, PAST_DATE, FUTURE_DATE_1,
            OTTAWA_AIRPORT );
      createWorkPackage( aircraftWithNoActiveFlight, FUTURE_DATE_1, OTTAWA_LINE,
            RefEventStatusKey.COMMIT );

      TaskKey expectedInWorkWorkPackage = createWorkPackage( aircraftWithNoActiveFlight, PAST_DATE,
            OTTAWA_LINE, RefEventStatusKey.IN_WORK );

      Optional<TaskKey> workPackageKey =
            faultPackagingService.packageOpenFaults( aircraftWithNoActiveFlight, humanResource );

      assertTrue( workPackageKey.isPresent() );
      assertEquals( expectedInWorkWorkPackage, workPackageKey.get() );
      assertTaskAssignedToWorkPackage( faultCorrectiveTaskWithNoActiveFlight,
            expectedInWorkWorkPackage );
      assertHistoryNoteAddedForAssignment( faultCorrectiveTaskWithNoActiveFlight );
   }


   @Test
   public void packageOpenFaults_withOnGoingFlight_isInFlight_noScheduled_newWP() throws Throwable {
      createFlight( aircraftWithOnGoingFlightInFlight, FlightLegStatus.MXOFF, PAST_DATE,
            FUTURE_DATE_1, OTTAWA_AIRPORT );

      Optional<TaskKey> workPackageKey = faultPackagingService
            .packageOpenFaults( aircraftWithOnGoingFlightInFlight, humanResource );

      assertTrue( workPackageKey.isPresent() );

      TaskKey createdWorkPackage = workPackageKey.get();
      WorkPackageData retrievedData = getWorkPackageData( createdWorkPackage );
      assertTrue( DateUtils.truncatedEquals( FUTURE_DATE_1, retrievedData.getStartDate(),
            Calendar.SECOND ) );
      assertTrue( DateUtils.truncatedEquals( FUTURE_DATE_1, retrievedData.getEndDate(),
            Calendar.SECOND ) );
      assertEquals( OTTAWA_LINE, retrievedData.getLocationKey() );
      assertTaskAssignedToWorkPackage( faultCorrectiveTaskWithOnGoingFlightInFlight,
            createdWorkPackage );
      assertHistoryNoteAddedForAssignment( faultCorrectiveTaskWithOnGoingFlightInFlight );
   }


   @Test
   public void packageOpenFaults_withOnGoingFlight_isOnGround_noNextScheduled_existingCommittedWP()
         throws Throwable {
      createFlight( aircraftWithOnGoingFlightOnTheGround, FlightLegStatus.MXOFF, PAST_DATE,
            FUTURE_DATE_1, OTTAWA_AIRPORT );
      TaskKey existingWorkPackage = createWorkPackage( aircraftWithOnGoingFlightOnTheGround,
            FUTURE_DATE_1, OTTAWA_LINE, RefEventStatusKey.COMMIT );

      Optional<TaskKey> workPackageKey = faultPackagingService
            .packageOpenFaults( aircraftWithOnGoingFlightOnTheGround, humanResource );

      assertTrue( workPackageKey.isPresent() );
      assertTrue( existingWorkPackage.equals( workPackageKey.get() ) );
   }


   @Test
   public void packageOpenFaults_withOnGoingFlight_isOnGround_nextScheduled_existingInWorkWP()
         throws Throwable {
      createFlight( aircraftWithOnGoingFlightOnTheGround, FlightLegStatus.MXOFF, PAST_DATE,
            FUTURE_DATE_1, OTTAWA_AIRPORT );
      createFlight( aircraftWithOnGoingFlightOnTheGround, FlightLegStatus.MXPLAN, FUTURE_DATE_2,
            FUTURE_DATE_3, VANCOUVER_AIRPORT );

      TaskKey existingWorkPackage = createWorkPackage( aircraftWithOnGoingFlightOnTheGround,
            FUTURE_DATE_1, OTTAWA_LINE, RefEventStatusKey.COMMIT );
      Optional<TaskKey> workPackageKey = faultPackagingService
            .packageOpenFaults( aircraftWithOnGoingFlightOnTheGround, humanResource );

      assertTrue( workPackageKey.isPresent() );
      assertTrue( existingWorkPackage.equals( workPackageKey.get() ) );
   }


   @Test
   public void packageOpenFaults_noActiveFlight_nextScheduled_existingCommittedWP()
         throws Throwable {
      createFlight( aircraftWithNoActiveFlight, FlightLegStatus.MXDELAY, FUTURE_DATE_2,
            FUTURE_DATE_3, VANCOUVER_AIRPORT );

      TaskKey expectedCommittedWorkPackage = createWorkPackage( aircraftWithNoActiveFlight,
            FUTURE_DATE_1, OTTAWA_LINE, RefEventStatusKey.COMMIT );

      Optional<TaskKey> workPackageKey =
            faultPackagingService.packageOpenFaults( aircraftWithNoActiveFlight, humanResource );

      assertTrue( workPackageKey.isPresent() );
      assertEquals( expectedCommittedWorkPackage, workPackageKey.get() );
      assertTaskAssignedToWorkPackage( faultCorrectiveTaskWithNoActiveFlight,
            expectedCommittedWorkPackage );
      assertHistoryNoteAddedForAssignment( faultCorrectiveTaskWithNoActiveFlight );
   }


   @Test
   public void packageOpenFaults_noActiveFlight_nextScheduled_existingInWorkWP() throws Throwable {
      createFlight( aircraftWithNoActiveFlight, FlightLegStatus.MXDELAY, FUTURE_DATE_2,
            FUTURE_DATE_3, VANCOUVER_AIRPORT );
      createWorkPackage( aircraftWithNoActiveFlight, FUTURE_DATE_1, OTTAWA_LINE,
            RefEventStatusKey.COMMIT );

      TaskKey expectedInWorkWorkPackage = createWorkPackage( aircraftWithNoActiveFlight,
            FUTURE_DATE_1, OTTAWA_LINE, RefEventStatusKey.IN_WORK );

      Optional<TaskKey> workPackageKey =
            faultPackagingService.packageOpenFaults( aircraftWithNoActiveFlight, humanResource );

      assertTrue( workPackageKey.isPresent() );
      assertEquals( expectedInWorkWorkPackage, workPackageKey.get() );
      assertTaskAssignedToWorkPackage( faultCorrectiveTaskWithNoActiveFlight,
            expectedInWorkWorkPackage );
      assertHistoryNoteAddedForAssignment( faultCorrectiveTaskWithNoActiveFlight );
   }


   @Test
   public void packageOpenFaults_noActiveFlight_nextScheduled_newWP() throws Throwable {
      createFlight( aircraftWithNoActiveFlight, FlightLegStatus.MXPLAN, FUTURE_DATE_2,
            FUTURE_DATE_3, VANCOUVER_AIRPORT );
      Date beforeDate = DateUtils.addSeconds( new Date(), -1 );

      Optional<TaskKey> workPackageKey =
            faultPackagingService.packageOpenFaults( aircraftWithNoActiveFlight, humanResource );

      assertTrue( workPackageKey.isPresent() );

      TaskKey createdWorkPackage = workPackageKey.get();
      WorkPackageData retrievedData = getWorkPackageData( createdWorkPackage );
      assertTrue( beforeDate.compareTo( retrievedData.getStartDate() ) <= 0 );

      Date afterDate = DateUtils.addSeconds( new Date(), 1 );

      assertTrue( retrievedData.getStartDate().compareTo( afterDate ) <= 0 );
      assertTrue( DateUtils.truncatedEquals( FUTURE_DATE_2, retrievedData.getEndDate(),
            Calendar.SECOND ) );
      assertEquals( OTTAWA_LINE, retrievedData.getLocationKey() );
      assertTaskAssignedToWorkPackage( faultCorrectiveTaskWithNoActiveFlight, createdWorkPackage );
      assertHistoryNoteAddedForAssignment( faultCorrectiveTaskWithNoActiveFlight );
   }


   @Test
   public void packageOpenFaults_noActiveFlight_noScheduled_existingCommittedWP() throws Throwable {
      TaskKey expectedCommittedWorkPackage = createWorkPackage( aircraftWithNoActiveFlight,
            FUTURE_DATE_1, OTTAWA_LINE, RefEventStatusKey.COMMIT );

      Optional<TaskKey> workPackageKey =
            faultPackagingService.packageOpenFaults( aircraftWithNoActiveFlight, humanResource );

      assertTrue( workPackageKey.isPresent() );
      assertEquals( expectedCommittedWorkPackage, workPackageKey.get() );
      assertTaskAssignedToWorkPackage( faultCorrectiveTaskWithNoActiveFlight,
            expectedCommittedWorkPackage );
      assertHistoryNoteAddedForAssignment( faultCorrectiveTaskWithNoActiveFlight );
   }


   @Test
   public void packageOpenFaults_noActiveFlight_noScheduled_existingInWorkWP() throws Throwable {
      createWorkPackage( aircraftWithNoActiveFlight, FUTURE_DATE_1, OTTAWA_LINE,
            RefEventStatusKey.COMMIT );

      TaskKey expectedInWorkWorkPackage = createWorkPackage( aircraftWithNoActiveFlight, PAST_DATE,
            OTTAWA_LINE, RefEventStatusKey.IN_WORK );

      Optional<TaskKey> workPackageKey =
            faultPackagingService.packageOpenFaults( aircraftWithNoActiveFlight, humanResource );

      assertTrue( workPackageKey.isPresent() );
      assertEquals( expectedInWorkWorkPackage, workPackageKey.get() );
      assertTaskAssignedToWorkPackage( faultCorrectiveTaskWithNoActiveFlight,
            expectedInWorkWorkPackage );
      assertHistoryNoteAddedForAssignment( faultCorrectiveTaskWithNoActiveFlight );
   }


   @Test
   public void packageOpenFaults_noActiveFlight_noScheduled_newWP() throws Throwable {
      Date beforeDate = DateUtils.addSeconds( new Date(), -1 );
      Optional<TaskKey> workPackageKey =
            faultPackagingService.packageOpenFaults( aircraftWithNoActiveFlight, humanResource );

      assertTrue( workPackageKey.isPresent() );

      TaskKey createdWorkPackage = workPackageKey.get();
      WorkPackageData retrievedData = getWorkPackageData( createdWorkPackage );
      assertTrue( beforeDate.compareTo( retrievedData.getStartDate() ) <= 0 );

      Date afterDate = DateUtils.addSeconds( new Date(), 1 );

      assertTrue( retrievedData.getStartDate().compareTo( afterDate ) <= 0 );
      assertTrue( DateUtils.truncatedEquals( retrievedData.getStartDate(),
            retrievedData.getEndDate(), Calendar.SECOND ) );
      assertEquals( OTTAWA_LINE, retrievedData.getLocationKey() );
      assertTaskAssignedToWorkPackage( faultCorrectiveTaskWithNoActiveFlight, createdWorkPackage );
      assertHistoryNoteAddedForAssignment( faultCorrectiveTaskWithNoActiveFlight );
   }


   private InventoryKey createAircraft( LocationKey aircraftLocation ) {
      return Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft builder ) {
            builder.setLocation( aircraftLocation );
         }
      } );
   }


   private TaskKey createFault( InventoryKey inventory, RefEventStatusKey status ) {
      final TaskKey correctiveTask =
            Domain.createCorrectiveTask( new DomainConfiguration<CorrectiveTask>() {

               @Override
               public void configure( CorrectiveTask builder ) {
                  builder.setInventory( inventory );
               }

            } );

      Domain.createFault( new DomainConfiguration<Fault>() {

         @Override
         public void configure( Fault builder ) {
            builder.setInventory( inventory );
            builder.setCorrectiveTask( correctiveTask );
            builder.setStatus( status );
         }

      } );
      return correctiveTask;
   }


   private void createFlight( InventoryKey aircraft, FlightLegStatus status, Date departureDate,
         Date arrivalDate, LocationKey arrivalLocation ) {
      Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight builder ) {
            builder.setAircraft( aircraft );
            builder.setDepartureDate( departureDate );
            builder.setStatus( status );
            builder.setArrivalDate( arrivalDate );
            builder.setArrivalLocation( arrivalLocation );
         }
      } );
   }


   private TaskKey createWorkPackage( InventoryKey aircraft, Date scheduledStartDate,
         LocationKey workPackageLocation, RefEventStatusKey refEventStatusKey ) {

      return Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage builder ) {
            builder.setAircraft( aircraft );
            builder.setLocation( workPackageLocation );
            builder.setScheduledStartDate( scheduledStartDate );
            builder.setStatus( refEventStatusKey );
         }
      } );
   }


   /* Retrieves basic work package data from a given key */
   private WorkPackageData getWorkPackageData( TaskKey workPackageKey ) {
      DataSetArgument eventTableArgs = new DataSetArgument();
      {
         eventTableArgs.add( workPackageKey, EvtEventDao.ColumnName.EVENT_DB_ID.name(),
               EvtEventDao.ColumnName.EVENT_ID.name() );
      }
      QuerySet eventQs =
            QuerySetFactory.getInstance().executeQueryTable( EventKey.TABLE_NAME, eventTableArgs );
      DataSetArgument args = new DataSetArgument();
      {
         args.add( workPackageKey, EvtLocTable.ColumnName.EVENT_DB_ID.name(),
               EvtEventDao.ColumnName.EVENT_ID.name() );
      }
      QuerySet locationQs =
            QuerySetFactory.getInstance().executeQueryTable( EventLocationKey.TABLE_NAME, args );

      assertTrue( String.format(
            "Expected to have work package data for TaskKey %s but the event data could not be retrieved.",
            workPackageKey ), eventQs.next() );
      assertTrue( String.format(
            "Expected to have work package data for TaskKey %s but the location data could not be retrieved.",
            workPackageKey ), locationQs.next() );

      return new WorkPackageData( eventQs.getDate( EvtEventDao.ColumnName.SCHED_START_DT.name() ),
            eventQs.getDate( EvtEventDao.ColumnName.SCHED_END_DT.name() ),
            locationQs.getKey( LocationKey.class, EvtLocTable.ColumnName.LOC_DB_ID.name(),
                  EvtLocTable.ColumnName.LOC_ID.name() ) );
   }


   /* Asserts that a task event's highest event is the given work package's task event */
   private void assertTaskAssignedToWorkPackage( TaskKey taskKey, TaskKey workPackageKey ) {
      DataSetArgument args = new DataSetArgument();
      args.add( taskKey, EvtEventDao.ColumnName.EVENT_DB_ID.name(),
            EvtEventDao.ColumnName.EVENT_ID.name() );
      args.add( workPackageKey, EvtEventDao.ColumnName.H_EVENT_DB_ID.name(),
            EvtEventDao.ColumnName.H_EVENT_ID.name() );

      QuerySet results =
            QuerySetFactory.getInstance().executeQueryTable( EventKey.TABLE_NAME, args );

      assertTrue( String.format( "Expected task %s to be assigned to work package %s", taskKey,
            workPackageKey ), results.first() );
   }


   /* Assert the task provided has an assignment history note added */
   private void assertHistoryNoteAddedForAssignment( TaskKey taskKey ) {
      DataSetArgument args = new DataSetArgument();
      args = new DataSetArgument();
      {
         args.add( taskKey, EvtStageDao.COL_EVENT_DB_ID, EvtStageDao.COL_EVENT_ID );
      }
      QuerySet results =
            QuerySetFactory.getInstance().executeQueryTable( StageKey.TABLE_NAME, args );

      boolean hasAssignmentNote = false;
      while ( results.next() ) {
         String historyNote = results.getString( EvtStageDao.COL_STAGE_NOTE );
         if ( historyNote.startsWith( "Assigned to" ) ) {
            hasAssignmentNote = true;
         }
      }

      assertTrue( String.format(
            "Expected task with key %s to have an assignment history note, but it did not.",
            taskKey ), hasAssignmentNote );
   }


   private InventoryKey createInventoryOnAircraft( InventoryKey aircraft ) {
      return Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

         @Override
         public void configure( TrackedInventory trackedInventory ) {
            trackedInventory.setParent( aircraft );
         }

      } );
   }


   private static class WorkPackageData {

      private final Date startDate;
      private final Date endDate;
      private final LocationKey locationKey;


      public WorkPackageData(Date startDate, Date endDate, LocationKey locationKey) {
         this.startDate = startDate;
         this.endDate = endDate;
         this.locationKey = locationKey;
      }


      public Date getStartDate() {
         return startDate;
      }


      public Date getEndDate() {
         return endDate;
      }


      public LocationKey getLocationKey() {
         return locationKey;
      }
   }
}
