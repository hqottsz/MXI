package com.mxi.mx.core.ejb.flighthist.flighthistbean.edithistflight;

import static com.mxi.mx.core.key.DataTypeKey.HOURS;
import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Engine;
import com.mxi.am.domain.Fault;
import com.mxi.am.domain.Flight;
import com.mxi.am.domain.InstallationRecord;
import com.mxi.am.domain.Location;
import com.mxi.am.domain.RemovalRecord;
import com.mxi.am.domain.Requirement;
import com.mxi.am.domain.RequirementDefinition;
import com.mxi.am.domain.SerializedInventory;
import com.mxi.am.domain.TrackedInventory;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.servlet.SessionContextFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.ejb.flighthist.FlightHistBean;
import com.mxi.mx.core.flight.model.FlightLegId;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefDataSourceKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefSchedFromKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.key.UsageDefinitionKey;
import com.mxi.mx.core.key.UsageParmKey;
import com.mxi.mx.core.services.event.usage.CollectedUsageParm;
import com.mxi.mx.core.services.flighthist.FlightInformationTO;
import com.mxi.mx.core.services.flighthist.Measurement;
import com.mxi.mx.core.services.inventory.InvUtils;
import com.mxi.mx.core.table.eqp.EqpDataSourceSpec;
import com.mxi.mx.core.table.evt.EvtSchedDeadTable;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.org.OrgHr;


/**
 * Tests for {@linkplain FlightHistBean#editHistFlight} that involve updating the usage deltas of a
 * historical flight and their effects on deadlines of tasks against the aircraft and all
 * sub-components installed during that flight.
 *
 */
public class EditHistFlight_AffectTaskDeadlineTest {

   private static final BigDecimal INITIAL_FLIGHT_HOURS = new BigDecimal( 5 );
   private static final BigDecimal INITIAL_FLIGHT_HOURS_TSN = new BigDecimal( 555 );
   private static final BigDecimal UPDATED_FLIGHT_HOURS = new BigDecimal( 7 );
   private static final BigDecimal AIRCRAFT_CURRENT_HOURS = new BigDecimal( 1000 );
   private static final BigDecimal COMPONENT_CURRENT_HOURS = new BigDecimal( 300 );
   private static final BigDecimal DEADLINE_HOURS_INTERVAL = new BigDecimal( 100 );

   // Not relevant to tests.
   private static final String HR_USERNAME = "HR_USERNAME";
   private static final Measurement[] NO_MEASUREMENTS = new Measurement[] {};
   private static final String FLIGHT_NAME = "FLIGHT_NAME";
   private static final Date FLIGHT_DATE = DateUtils.addDays( new Date(), -200 );

   // The bean under test.
   private FlightHistBean iFlightHistBean;
   private HumanResourceKey iHrKey;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    *
    * Verify that when a flight's usage is updated and there exists a task, against the aircraft,
    * that was initialized AFTER the flight that the task's deadline information is modified with
    * the difference in the modified flight usage.
    *
    * Note: to determine if a task is initialized after the flight, the deadline start value is
    * compared to the flight usage value.
    *
    * @throws Exception
    */
   @Test
   public void itAdjustsDeadlinesOnAircraftTaskThatWasInitializedAfterEditedFlight()
         throws Exception {

      // Given an aircraft collecting usage.
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( HOURS, AIRCRAFT_CURRENT_HOURS );
         }
      } );

      // Given a flight for the aircraft that accrued usage.
      FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( FLIGHT_DATE );
            aFlight.addUsage( lAircraft, HOURS, INITIAL_FLIGHT_HOURS, INITIAL_FLIGHT_HOURS_TSN );
         }
      } );

      Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( DateUtils.addDays( FLIGHT_DATE, 1 ) );
            aFlight.addUsage( lAircraft, HOURS, INITIAL_FLIGHT_HOURS,
                  INITIAL_FLIGHT_HOURS_TSN.add( INITIAL_FLIGHT_HOURS ) );
         }
      } );

      // Given a requirement with a usage deadline, is against the aircraft, and was initialized
      // after the flight.
      //
      // Note: The task is considered initialized after the flight when its deadline start value is
      // greater than the flight usage value.

      final BigDecimal lDeadlineStartValue = INITIAL_FLIGHT_HOURS_TSN.add( TEN );
      final BigDecimal lDeadlineDueValue = lDeadlineStartValue.add( ONE );

      TaskKey lReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setInventory( lAircraft );
            aReq.addUsageDeadline( HOURS, RefSchedFromKey.EFFECTIV, lDeadlineStartValue,
                  DEADLINE_HOURS_INTERVAL, lDeadlineDueValue );
         }
      } );

      // When the flight is edited and the usage is modified.
      editHistFlightUsage( lFlight, UPDATED_FLIGHT_HOURS, lAircraft );

      // Then the task's deadline start value and due value are adjusted by the difference of the
      // deltas.
      BigDecimal lUsageDifference = UPDATED_FLIGHT_HOURS.subtract( INITIAL_FLIGHT_HOURS );

      Double lExpectedStartValue = lDeadlineStartValue.add( lUsageDifference ).doubleValue();
      Double lExpectedDueValue = lDeadlineDueValue.add( lUsageDifference ).doubleValue();

      EvtSchedDeadTable lEvtSchedDead = EvtSchedDeadTable.findByPrimaryKey( lReq, HOURS );
      Double lActualStartValue = lEvtSchedDead.getStartQt();
      Double lActualDueValue = lEvtSchedDead.getDeadlineQt();

      assertEquals( "Unexpected deadline start value.", lExpectedStartValue, lActualStartValue );
      assertEquals( "Unexpected deadline due value.", lExpectedDueValue, lActualDueValue );
   }


   /**
    *
    * Verify that when a flight's usage is updated and there exists a task, against the aircraft,
    * that was initialized BEFORE the flight that the task's deadline information is NOT modified.
    *
    * Note: to determine if a task is initialized before the flight, the deadline start value is
    * compared to the flight usage value.
    *
    * @throws Exception
    */
   @Test
   public void itDoesNotAdjustDeadlinesOnAircraftTaskThatWasInitializedBeforeEditedFlight()
         throws Exception {

      // Given an aircraft.
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( HOURS, AIRCRAFT_CURRENT_HOURS );
         }
      } );

      // Given a flight for the aircraft that accrued usage.
      FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( FLIGHT_DATE );
            aFlight.addUsage( lAircraft, HOURS, INITIAL_FLIGHT_HOURS, INITIAL_FLIGHT_HOURS_TSN );
         }
      } );

      Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( DateUtils.addDays( FLIGHT_DATE, 1 ) );
            aFlight.addUsage( lAircraft, HOURS, INITIAL_FLIGHT_HOURS,
                  INITIAL_FLIGHT_HOURS_TSN.add( INITIAL_FLIGHT_HOURS ) );
         }
      } );

      // Given a requirement with a usage deadline, is against the aircraft, and was initialized
      // before the flight.
      //
      // Note: The task is considered initialized before the flight when its deadline start value is
      // less than the flight usage value.

      final BigDecimal lDeadlineStartValue = INITIAL_FLIGHT_HOURS_TSN.subtract( TEN );
      final BigDecimal lDeadlineDueValue = lDeadlineStartValue.add( ONE );

      TaskKey lReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setInventory( lAircraft );
            aReq.addUsageDeadline( HOURS, RefSchedFromKey.EFFECTIV, lDeadlineStartValue,
                  DEADLINE_HOURS_INTERVAL, lDeadlineDueValue );
         }
      } );

      // When the flight is edited and the usage is modified.
      editHistFlightUsage( lFlight, UPDATED_FLIGHT_HOURS, lAircraft );

      // Then the task's deadline start value and due value are NOT adjusted.
      EvtSchedDeadTable lEvtSchedDead = EvtSchedDeadTable.findByPrimaryKey( lReq, HOURS );
      BigDecimal lActualStartValue = new BigDecimal( lEvtSchedDead.getStartQt() );
      BigDecimal lActualDueValue = new BigDecimal( lEvtSchedDead.getDeadlineQt() );

      assertEquals( "Unexpected deadline start value.", lDeadlineStartValue, lActualStartValue );
      assertEquals( "Unexpected deadline due value.", lDeadlineDueValue, lActualDueValue );
   }


   /**
    *
    * Verify that when a flight's usage is updated and there exists a task, which is against an
    * engine on that flight, that was initialized AFTER the flight that the task's deadline
    * information is modified with the difference in the modified flight usage.
    *
    * Note: to determine if a task is initialized after the flight, the deadline start value is
    * compared to the flight usage value.
    *
    * @throws Exception
    */
   @Test
   public void itAdjustsDeadlinesOnEngineTaskThatWasInitializedAfterEditedFlight()
         throws Exception {

      // Given an aircraft collecting usage.
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( HOURS, AIRCRAFT_CURRENT_HOURS );
         }
      } );

      // Given an engine collecting same usage.
      // In this test case, the engine is still on the aircraft
      final InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.addUsage( HOURS, AIRCRAFT_CURRENT_HOURS );
            aEngine.setParent( lAircraft );
         }
      } );

      // Given a flight that accrued usage for the aircraft and the engine.
      // (that was installed at the time of the flight)
      FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( FLIGHT_DATE );
            aFlight.addUsage( lAircraft, HOURS, INITIAL_FLIGHT_HOURS, INITIAL_FLIGHT_HOURS_TSN );
            aFlight.addUsage( lEngine, HOURS, INITIAL_FLIGHT_HOURS, INITIAL_FLIGHT_HOURS_TSN );
         }
      } );

      Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( DateUtils.addDays( FLIGHT_DATE, 1 ) );
            aFlight.addUsage( lAircraft, HOURS, INITIAL_FLIGHT_HOURS,
                  INITIAL_FLIGHT_HOURS_TSN.add( INITIAL_FLIGHT_HOURS ) );
         }
      } );

      // Given a requirement with a usage deadline, is against the engine, and was initialized after
      // the flight.
      //
      // Note: The task is considered initialized after the flight when its deadline start value is
      // greater than the flight usage value.

      final BigDecimal lDeadlineStartValue = INITIAL_FLIGHT_HOURS_TSN.add( TEN );
      final BigDecimal lDeadlineDueValue = lDeadlineStartValue.add( ONE );

      TaskKey lReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setInventory( lEngine );
            aReq.addUsageDeadline( HOURS, RefSchedFromKey.EFFECTIV, lDeadlineStartValue,
                  DEADLINE_HOURS_INTERVAL, lDeadlineDueValue );
         }
      } );

      // When the flight is edited and the usage is modified.
      editHistFlightUsage( lFlight, UPDATED_FLIGHT_HOURS, lAircraft, lEngine );

      // Then the task's deadline start and due values are adjusted by the difference of the deltas.
      BigDecimal lUsageDifference = UPDATED_FLIGHT_HOURS.subtract( INITIAL_FLIGHT_HOURS );

      Double lExpectedStartValue = lDeadlineStartValue.add( lUsageDifference ).doubleValue();
      Double lExpectedDueValue = lDeadlineDueValue.add( lUsageDifference ).doubleValue();

      EvtSchedDeadTable lEvtSchedDead = EvtSchedDeadTable.findByPrimaryKey( lReq, HOURS );
      Double lActualStartValue = lEvtSchedDead.getStartQt();
      Double lActualDueValue = lEvtSchedDead.getDeadlineQt();

      assertEquals( "Unexpected deadline start value.", lExpectedStartValue, lActualStartValue );
      assertEquals( "Unexpected deadline due value.", lExpectedDueValue, lActualDueValue );
   }


   /**
    *
    * Verify that when a flight's usage is updated and there exists a task, which is against an
    * engine on that flight, that was initialized BEFORE the flight that the task's deadline
    * information is NOT modified.
    *
    * Note: to determine if a task is initialized before the flight, the deadline start value is
    * compared to the flight usage value.
    *
    * @throws Exception
    */
   @Test
   public void itDoesNotAdjustDeadlinesOnEngineTaskThatWasInitializedBeforeEditedFlight()
         throws Exception {

      // Given an aircraft collecting usage.
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( HOURS, AIRCRAFT_CURRENT_HOURS );
         }
      } );

      // Given an engine collecting same usage.
      // (it need not currently be installed and an aircraft)
      final InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.addUsage( HOURS, AIRCRAFT_CURRENT_HOURS );
         }
      } );

      // Given an engine collecting same usage.

      // Given a flight that accrued usage for the aircraft and the engine that was installed.
      FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( FLIGHT_DATE );
            aFlight.addUsage( lAircraft, HOURS, INITIAL_FLIGHT_HOURS, INITIAL_FLIGHT_HOURS_TSN );
            aFlight.addUsage( lEngine, HOURS, INITIAL_FLIGHT_HOURS, INITIAL_FLIGHT_HOURS_TSN );
         }
      } );

      Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( DateUtils.addDays( FLIGHT_DATE, 1 ) );
            aFlight.addUsage( lAircraft, HOURS, INITIAL_FLIGHT_HOURS,
                  INITIAL_FLIGHT_HOURS_TSN.add( INITIAL_FLIGHT_HOURS ) );
         }
      } );

      // Given a requirement with a usage deadline, is against the engine, and was initialized
      // before the flight.
      //
      // Note: The task is considered initialized before the flight when its deadline start value is
      // less than the flight usage value.

      final BigDecimal lDeadlineStartValue = INITIAL_FLIGHT_HOURS_TSN.subtract( TEN );
      final BigDecimal lDeadlineDueValue = lDeadlineStartValue.add( ONE );

      TaskKey lReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setInventory( lEngine );
            aReq.addUsageDeadline( HOURS, RefSchedFromKey.EFFECTIV, lDeadlineStartValue,
                  DEADLINE_HOURS_INTERVAL, lDeadlineDueValue );
         }
      } );

      // When the flight is edited and the usage is modified.
      editHistFlightUsage( lFlight, UPDATED_FLIGHT_HOURS, lAircraft, lEngine );

      // Then the task's deadline start value and due value are not modified.
      EvtSchedDeadTable lEvtSchedDead = EvtSchedDeadTable.findByPrimaryKey( lReq, HOURS );
      BigDecimal lActualStartValue = new BigDecimal( lEvtSchedDead.getStartQt() );
      BigDecimal lActualDueValue = new BigDecimal( lEvtSchedDead.getDeadlineQt() );

      assertEquals( "Unexpected deadline start value.", lDeadlineStartValue, lActualStartValue );
      assertEquals( "Unexpected deadline due value.", lDeadlineDueValue, lActualDueValue );
   }


   /**
    * <pre>
    * Given an Aircraft with a Historical Flight,
    * And the Aircraft has a System Inventory attached to it,
    * And the System Inventory has a completed Task with a Deadline that starts AFTER the flight,
    * And the Deadline is based on a Usage Parameter,
    * When the Historical Flight is edited,
    * And the Delta for that Usage Parameter is modified,
    * Then the Deadline Start and Due quantities should be updated by the change to the Delta.
    * </pre>
    */
   @Test
   public void itAdjustsDeadlinesOnSystemTaskThatWasInitializedAfterEditedFlight()
         throws Exception {

      // Given an aircraft collecting usage and an system collecting usage.
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( HOURS, AIRCRAFT_CURRENT_HOURS );
            aAircraft.addSystem( "28 - FUEL" );
         }
      } );

      final InventoryKey lFuelSysInvKey = InvUtils.getSystemByName( lAircraft, "28 - FUEL" );

      // Given a flight that accrued usage for the aircraft
      FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( FLIGHT_DATE );
            aFlight.addUsage( lAircraft, HOURS, INITIAL_FLIGHT_HOURS, INITIAL_FLIGHT_HOURS_TSN );
         }
      } );

      Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( DateUtils.addDays( FLIGHT_DATE, 1 ) );
            aFlight.addUsage( lAircraft, HOURS, INITIAL_FLIGHT_HOURS,
                  INITIAL_FLIGHT_HOURS_TSN.add( INITIAL_FLIGHT_HOURS ) );
         }
      } );

      // Given a requirement with a usage deadline, is against the system, and was initialized after
      // the flight.
      //
      // Note: The task is considered initialized after the flight when its deadline start value is
      // greater than the flight usage value.

      final BigDecimal lDeadlineStartValue = INITIAL_FLIGHT_HOURS_TSN.add( TEN );
      final BigDecimal lDeadlineDueValue = lDeadlineStartValue.add( ONE );

      TaskKey lReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setInventory( lFuelSysInvKey );
            aReq.setStatus( RefEventStatusKey.COMPLETE );
            aReq.addUsageDeadline( HOURS, RefSchedFromKey.EFFECTIV, lDeadlineStartValue,
                  DEADLINE_HOURS_INTERVAL, lDeadlineDueValue );
         }
      } );

      // When the flight is edited and the usage is modified.
      editHistFlightUsage( lFlight, UPDATED_FLIGHT_HOURS, lAircraft );

      // Then the task's deadline start and due values are adjusted by the difference of the deltas.
      BigDecimal lUsageDifference = UPDATED_FLIGHT_HOURS.subtract( INITIAL_FLIGHT_HOURS );

      Double lExpectedStartValue = lDeadlineStartValue.add( lUsageDifference ).doubleValue();
      Double lExpectedDueValue = lDeadlineDueValue.add( lUsageDifference ).doubleValue();

      EvtSchedDeadTable lEvtSchedDead = EvtSchedDeadTable.findByPrimaryKey( lReq, HOURS );
      Double lActualStartValue = lEvtSchedDead.getStartQt();
      Double lActualDueValue = lEvtSchedDead.getDeadlineQt();

      assertEquals( "Unexpected deadline start value.", lExpectedStartValue, lActualStartValue );
      assertEquals( "Unexpected deadline due value.", lExpectedDueValue, lActualDueValue );
   }


   /**
    * <pre>
    * Given an Aircraft with a Historical Flight,
    * And the Aircraft has a System Inventory attached to it,
    * And the System Inventory has an active Task with a Deadline that starts BEFORE the flight
    * And the Deadline is based on a Usage Parameter,
    * When the Historical Flight is edited,
    * And the Delta for that Usage Parameter is modified,
    * Then the Deadline Start and Due quantities should NOT be updated.
    * </pre>
    */
   @Test
   public void itDoesNotAdjustDeadlinesOnSystemTaskThatWasInitializedBeforeEditedFlight()
         throws Exception {

      // Given an aircraft collecting usage.
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( HOURS, AIRCRAFT_CURRENT_HOURS );
            aAircraft.addSystem( "28 - FUEL" );
         }
      } );

      final InventoryKey lFuelSysInvKey = InvUtils.getSystemByName( lAircraft, "28 - FUEL" );

      // Given a flight that accrued usage for the aircraft
      FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( FLIGHT_DATE );
            aFlight.addUsage( lAircraft, HOURS, INITIAL_FLIGHT_HOURS, INITIAL_FLIGHT_HOURS_TSN );
         }
      } );

      Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( DateUtils.addDays( FLIGHT_DATE, 1 ) );
            aFlight.addUsage( lAircraft, HOURS, INITIAL_FLIGHT_HOURS,
                  INITIAL_FLIGHT_HOURS_TSN.add( INITIAL_FLIGHT_HOURS ) );
         }
      } );

      // Given a requirement with a usage deadline, is against the system, and was initialized
      // before the flight.
      //
      // Note: The task is considered initialized before the flight when its deadline start value is
      // less than the flight usage value.

      final BigDecimal lDeadlineStartValue = INITIAL_FLIGHT_HOURS_TSN.subtract( TEN );
      final BigDecimal lDeadlineDueValue = lDeadlineStartValue.add( ONE );

      TaskKey lReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setInventory( lFuelSysInvKey );
            aReq.addUsageDeadline( HOURS, RefSchedFromKey.EFFECTIV, lDeadlineStartValue,
                  DEADLINE_HOURS_INTERVAL, lDeadlineDueValue );
         }
      } );

      // When the flight is edited and the usage is modified.
      editHistFlightUsage( lFlight, UPDATED_FLIGHT_HOURS, lAircraft );

      // Then the task's deadline start value and due value are not modified.
      EvtSchedDeadTable lEvtSchedDead = EvtSchedDeadTable.findByPrimaryKey( lReq, HOURS );
      BigDecimal lActualStartValue = new BigDecimal( lEvtSchedDead.getStartQt() );
      BigDecimal lActualDueValue = new BigDecimal( lEvtSchedDead.getDeadlineQt() );

      assertEquals( "Unexpected deadline start value.", lDeadlineStartValue, lActualStartValue );
      assertEquals( "Unexpected deadline due value.", lDeadlineDueValue, lActualDueValue );
   }


   /**
    * <pre>
    * Given an Aircraft with a Historical Flight,
    * And the Aircraft has a Trk Inventory attached to it,
    * And the Trk Inventory has an active Task with a Deadline that starts BEFORE the flight
    * And the Deadline is based on a Usage Parameter,
    * When the Historical Flight is edited,
    * And the Delta for that Usage Parameter is modified,
    * Then the Deadline Start and Due quantities should NOT be updated.
    * </pre>
    */
   @Test
   public void itDoesNotAdjustDeadlinesOnTrkTaskThatWasInitializedBeforeEditedFlight()
         throws Exception {

      final InventoryKey lTrackedKey =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aTrk ) {
                  aTrk.addUsage( HOURS, COMPONENT_CURRENT_HOURS );
               }
            } );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( HOURS, AIRCRAFT_CURRENT_HOURS );
            aAircraft.addTracked( lTrackedKey );
         }
      } );

      FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( FLIGHT_DATE );
            aFlight.addUsage( lAircraft, HOURS, INITIAL_FLIGHT_HOURS, INITIAL_FLIGHT_HOURS_TSN );
         }
      } );

      Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( DateUtils.addDays( FLIGHT_DATE, 1 ) );
            aFlight.addUsage( lAircraft, HOURS, INITIAL_FLIGHT_HOURS,
                  INITIAL_FLIGHT_HOURS_TSN.add( INITIAL_FLIGHT_HOURS ) );
         }
      } );

      // In order for the TRK to have a completed task with a deadline that starts after the flight,
      // the deadline's usage start quantity must be greater than the usage TSN quantity of the TRK
      // inventory at the time of the flight.
      //
      // If there are no usage adjustments for the TRK between the time of the flight and now, then
      // the current usage will equal the usage at the time of the flight.
      final BigDecimal lTrkHoursTsnAtTimeOfFlight = COMPONENT_CURRENT_HOURS;
      final BigDecimal lDeadlineStartValue = lTrkHoursTsnAtTimeOfFlight.subtract( TEN );
      final BigDecimal lDeadlineDueValue = lDeadlineStartValue.add( ONE );

      TaskKey lReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setInventory( lTrackedKey );
            aReq.addUsageDeadline( HOURS, RefSchedFromKey.EFFECTIV, lDeadlineStartValue,
                  DEADLINE_HOURS_INTERVAL, lDeadlineDueValue );
         }
      } );

      // When the flight is edited and the usage is modified.
      editHistFlightUsage( lFlight, UPDATED_FLIGHT_HOURS, lAircraft );

      // Then the task's deadline start value and due value are not modified.
      EvtSchedDeadTable lEvtSchedDead = EvtSchedDeadTable.findByPrimaryKey( lReq, HOURS );
      BigDecimal lActualStartValue = new BigDecimal( lEvtSchedDead.getStartQt() );
      BigDecimal lActualDueValue = new BigDecimal( lEvtSchedDead.getDeadlineQt() );

      assertEquals( "Unexpected deadline start value.", lDeadlineStartValue, lActualStartValue );
      assertEquals( "Unexpected deadline due value.", lDeadlineDueValue, lActualDueValue );
   }


   /**
    * <pre>
    * Given an Aircraft with a Historical Flight,
    * And the Aircraft has a Trk Inventory attached to it,
    * And the Trk Inventory has a completed Task with a Deadline that starts AFTER the flight,
    * And the Deadline is based on a Usage Parameter,
    * When the Historical Flight is edited,
    * And the Delta for that Usage Parameter is modified,
    * Then the Deadline Start and Due quantities should be updated by the change to the Delta.
    * </pre>
    */
   @Test
   public void itAdjustsDeadlinesOnTrkTaskThatWasInitializedAfterEditedFlight() throws Exception {

      final InventoryKey lTrackedKey =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aTrk ) {
                  aTrk.addUsage( HOURS, COMPONENT_CURRENT_HOURS );
               }
            } );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( HOURS, AIRCRAFT_CURRENT_HOURS );
            aAircraft.addTracked( lTrackedKey );
         }
      } );

      FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( FLIGHT_DATE );
            aFlight.addUsage( lAircraft, HOURS, INITIAL_FLIGHT_HOURS, INITIAL_FLIGHT_HOURS_TSN );
         }
      } );

      // Create a later flight in order to trigger out of sequence logic
      Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( DateUtils.addDays( FLIGHT_DATE, 1 ) );
            aFlight.addUsage( lAircraft, HOURS, INITIAL_FLIGHT_HOURS,
                  INITIAL_FLIGHT_HOURS_TSN.add( INITIAL_FLIGHT_HOURS ) );
         }
      } );

      // In order for the TRK to have a completed task with a deadline that starts after the flight,
      // the deadline's usage start quantity must be greater than the usage TSN quantity of the TRK
      // inventory at the time of the flight.
      //
      // If there are no usage adjustments for the TRK between the time of the flight and now, then
      // the current usage will equal the usage at the time of the flight.
      final BigDecimal lTrkHoursTsnAtTimeOfFlight = COMPONENT_CURRENT_HOURS;
      final BigDecimal lDeadlineStartValue = lTrkHoursTsnAtTimeOfFlight.add( TEN );
      final BigDecimal lDeadlineDueValue = lDeadlineStartValue.add( ONE );

      TaskKey lReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setInventory( lTrackedKey );
            aReq.setStatus( RefEventStatusKey.COMPLETE );
            aReq.setActualEndDate( DateUtils.addHours( FLIGHT_DATE, 1 ) );
            aReq.addUsageDeadline( HOURS, RefSchedFromKey.EFFECTIV, lDeadlineStartValue,
                  DEADLINE_HOURS_INTERVAL, lDeadlineDueValue );
         }
      } );

      // When the flight is edited and the usage is modified.
      editHistFlightUsage( lFlight, UPDATED_FLIGHT_HOURS, lAircraft );

      // Then the task's deadline start and due values are adjusted by the difference of the deltas.
      BigDecimal lUsageDifference = UPDATED_FLIGHT_HOURS.subtract( INITIAL_FLIGHT_HOURS );

      Double lExpectedStartValue = lDeadlineStartValue.add( lUsageDifference ).doubleValue();
      Double lExpectedDueValue = lDeadlineDueValue.add( lUsageDifference ).doubleValue();

      EvtSchedDeadTable lEvtSchedDead = EvtSchedDeadTable.findByPrimaryKey( lReq, HOURS );
      Double lActualStartValue = lEvtSchedDead.getStartQt();
      Double lActualDueValue = lEvtSchedDead.getDeadlineQt();

      assertEquals( "Unexpected deadline start value.", lExpectedStartValue, lActualStartValue );
      assertEquals( "Unexpected deadline due value.", lExpectedDueValue, lActualDueValue );
   }


   /**
    *
    * Verify that the deadline of a historical task against a loose TRK is updated when the usage of
    * a flight, which occurred prior to the task and on which the TRK flew, is updated.
    *
    * Note: to determine if a task is initialized after the flight, the deadline start value is
    * compared to the flight usage value.
    *
    * <pre>
    * Given an aircraft with a historical flight,
    * And a loose TRK inventory that was attached at the time of the flight,
    * And there is a completed task against the TRK inventory whose deadline start is AFTER the flight,
    * And the deadline is based on a usage parameter,
    * When the historical flight is edited,
    * And the delta for that usage parameter is modified,
    * Then the deadline start and due quantities are updated by the change to the delta.
    * </pre>
    */
   @Test
   public void itAdjustsDeadlinesOnLooseTrkTaskThatWasInitializedAfterEditedFlight()
         throws Exception {

      // Given an aircraft with a historical flight.
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( HOURS, AIRCRAFT_CURRENT_HOURS );
         }
      } );

      FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( FLIGHT_DATE );
            aFlight.addUsage( lAircraft, HOURS, INITIAL_FLIGHT_HOURS, INITIAL_FLIGHT_HOURS_TSN );
         }
      } );

      Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( DateUtils.addDays( FLIGHT_DATE, 1 ) );
            aFlight.addUsage( lAircraft, HOURS, INITIAL_FLIGHT_HOURS,
                  INITIAL_FLIGHT_HOURS_TSN.add( INITIAL_FLIGHT_HOURS ) );
         }
      } );

      // Given a loose TRK inventory that was attached at the time of the flight.
      // (TRK has not acquired any other usage since its removal)
      final Date lInstallDatePriorToFlight = DateUtils.addDays( FLIGHT_DATE, -20 );
      final Date lRemovalDateAfterToFlight = DateUtils.addDays( FLIGHT_DATE, 10 );

      final InventoryKey lTrackedKey =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aTrk ) {
                  aTrk.addUsage( HOURS, COMPONENT_CURRENT_HOURS );
                  aTrk.addInstallationRecord( new DomainConfiguration<InstallationRecord>() {

                     @Override
                     public void configure( InstallationRecord aInstallRec ) {
                        aInstallRec.setInstallationDate( lInstallDatePriorToFlight );
                        aInstallRec.setHighest( lAircraft );
                        aInstallRec.setParent( lAircraft );
                        aInstallRec.setAssembly( lAircraft );
                     }
                  } );
                  aTrk.addRemovalRecord( new DomainConfiguration<RemovalRecord>() {

                     @Override
                     public void configure( RemovalRecord aRemoveRec ) {
                        aRemoveRec.setRemovalDate( lRemovalDateAfterToFlight );
                        aRemoveRec.setHighest( lAircraft );
                        aRemoveRec.setParent( lAircraft );
                        aRemoveRec.setAssembly( lAircraft );
                     }
                  } );
               }
            } );

      // Given there is a completed task against the loose TRK inventory whose deadline start is
      // AFTER the flight (and after the removal).

      // In order for the TRK to have a completed task with a deadline that starts after the flight,
      // the deadline's usage start quantity must be greater than the usage TSN quantity of the TRK
      // inventory at the time of the flight.
      //
      // If there are no usage adjustments for the TRK between the time of the flight and now, then
      // the current usage will equal the usage at the time of the flight.
      final BigDecimal lTrkHoursTsnAtTimeOfFlight = COMPONENT_CURRENT_HOURS;
      final BigDecimal lDeadlineStartValue = lTrkHoursTsnAtTimeOfFlight;
      final BigDecimal lDeadlineDueValue = lDeadlineStartValue.add( DEADLINE_HOURS_INTERVAL );

      TaskKey lReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setInventory( lTrackedKey );
            aReq.setStatus( RefEventStatusKey.COMPLETE );
            aReq.setActualEndDate( DateUtils.addDays( FLIGHT_DATE, 11 ) );
            aReq.addUsageDeadline( HOURS, RefSchedFromKey.EFFECTIV, lTrkHoursTsnAtTimeOfFlight,
                  DEADLINE_HOURS_INTERVAL, lDeadlineDueValue );
         }
      } );

      // When the flight is edited and the usage is modified.
      editHistFlightUsage( lFlight, UPDATED_FLIGHT_HOURS, lAircraft );

      // Then the task's deadline start and due values are adjusted by the difference of the deltas.
      BigDecimal lUsageDifference = UPDATED_FLIGHT_HOURS.subtract( INITIAL_FLIGHT_HOURS );

      Double lExpectedStartValue = lTrkHoursTsnAtTimeOfFlight.add( lUsageDifference ).doubleValue();
      Double lExpectedDueValue = lDeadlineDueValue.add( lUsageDifference ).doubleValue();

      EvtSchedDeadTable lEvtSchedDead = EvtSchedDeadTable.findByPrimaryKey( lReq, HOURS );
      Double lActualStartValue = lEvtSchedDead.getStartQt();
      Double lActualDueValue = lEvtSchedDead.getDeadlineQt();

      assertEquals( "Unexpected deadline start value.", lExpectedStartValue, lActualStartValue );
      assertEquals( "Unexpected deadline due value.", lExpectedDueValue, lActualDueValue );
   }


   /**
    *
    * Verify that the deadline of a task against a loose TRK is updated when the usage of a flight,
    * which occurred prior to the task becoming loose, is updated.
    *
    * <pre>
    * Given an aircraft with a historical flight,
    * And a loose TRK inventory that was attached at the time of the flight,
    * And the deadline is based on a usage parameter,
    * When the historical flight is created/edited,
    * And the delta for that usage parameter is modified,
    * Then a realtime deadline update work item is created for this inventory
    * </pre>
    */
   @Test
   public void itAdjustsDeadlinesOnLooseTrkTaskThatWasInitializedBeforeEditedFlight()
         throws Exception {

      // Given an aircraft with a historical flight.
      final InventoryKey lAircraft = Domain.createAircraft( aAircraft -> {
         aAircraft.addUsage( HOURS, AIRCRAFT_CURRENT_HOURS );
      } );

      FlightLegId lFlight = Domain.createFlight( aFlight -> {
         aFlight.setAircraft( lAircraft );
         aFlight.setHistorical( true );
         aFlight.setArrivalDate( FLIGHT_DATE );
         aFlight.addUsage( lAircraft, HOURS, INITIAL_FLIGHT_HOURS, INITIAL_FLIGHT_HOURS_TSN );
      } );

      Domain.createFlight( aFlight -> {
         aFlight.setAircraft( lAircraft );
         aFlight.setHistorical( true );
         aFlight.setArrivalDate( DateUtils.addDays( FLIGHT_DATE, 1 ) );
         aFlight.addUsage( lAircraft, HOURS, INITIAL_FLIGHT_HOURS,
               INITIAL_FLIGHT_HOURS_TSN.add( INITIAL_FLIGHT_HOURS ) );
      } );

      // Given a loose TRK inventory that was attached at the time of the flight.
      // (TRK has not acquired any other usage since its removal)
      final Date lInstallDatePriorToFlight = DateUtils.addDays( FLIGHT_DATE, -20 );
      final Date lRemovalDateAfterToFlight = DateUtils.addDays( FLIGHT_DATE, 10 );

      final InventoryKey lTrackedKey = Domain.createTrackedInventory( aTrk -> {
         aTrk.addUsage( HOURS, COMPONENT_CURRENT_HOURS );
         aTrk.addInstallationRecord( aInstallRec -> {
            aInstallRec.setInstallationDate( lInstallDatePriorToFlight );
            aInstallRec.setHighest( lAircraft );
            aInstallRec.setParent( lAircraft );
            aInstallRec.setAssembly( lAircraft );
         } );
         aTrk.addRemovalRecord( aRemoveRec -> {
            aRemoveRec.setRemovalDate( lRemovalDateAfterToFlight );
            aRemoveRec.setHighest( lAircraft );
            aRemoveRec.setParent( lAircraft );
            aRemoveRec.setAssembly( lAircraft );
         } );

      } );

      final BigDecimal lTrkHoursTsnAtTimeOfFlight = COMPONENT_CURRENT_HOURS;
      final BigDecimal lDeadlineStartValue = lTrkHoursTsnAtTimeOfFlight.subtract( TEN );
      final BigDecimal lDeadlineDueValue = lDeadlineStartValue.add( ONE );

      TaskKey lReq = Domain.createRequirement( aReq -> {
         aReq.setInventory( lTrackedKey );
         aReq.setStatus( RefEventStatusKey.COMPLETE );
         aReq.addUsageDeadline( HOURS, RefSchedFromKey.EFFECTIV, lTrkHoursTsnAtTimeOfFlight,
               DEADLINE_HOURS_INTERVAL, lDeadlineDueValue );
      } );

      // When the flight is edited and the usage is modified.
      editHistFlightUsage( lFlight, UPDATED_FLIGHT_HOURS, lAircraft );

      // Then a work item will be created to update the inventory realtime deadlines
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.addWhere( "type = 'REAL_TIME_INVENTORY_DEADLINE_UPDATE'" );
      QuerySet lWorkItsmQs =
            QuerySetFactory.getInstance().executeQuery( "utl_work_item", lArgs, "key" );

      assertEquals( 1, lWorkItsmQs.getRowCount() );
      lWorkItsmQs.next();
      String lWorkItemData = lWorkItsmQs.getString( "key" );

      // Ensure that our TRK loose inventory has a work item to be processed (which will cause the
      // deadline to be updated)
      assertTrue( lWorkItemData.contains( lTrackedKey.getDbId() + ":" + lTrackedKey.getId() ) );

   }


   /**
    * <pre>
    * Given an Aircraft with a Historical Flight,
    * And the Aircraft has a SER Inventory attached to it,
    * And the SER Inventory has an active Task with a Deadline that starts AFTER the flight,
    * And the Deadline is based on a Usage Parameter,
    * When the Historical Flight is edited,
    * And the Delta for that Usage Parameter is modified,
    * Then the Deadline Start and Due quantities should be updated by the change to the Delta.
    * </pre>
    */
   @Test
   public void itAdjustsDeadlinesOnSerTaskThatWasInitializedAfterEditedFlight() throws Exception {

      final InventoryKey lSerializedKey =
            Domain.createSerializedInventory( new DomainConfiguration<SerializedInventory>() {

               @Override
               public void configure( SerializedInventory aSer ) {
                  aSer.addUsage( HOURS, COMPONENT_CURRENT_HOURS );
               }
            } );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( HOURS, AIRCRAFT_CURRENT_HOURS );
            aAircraft.addSerialized( lSerializedKey );
         }
      } );

      FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( FLIGHT_DATE );
            aFlight.addUsage( lAircraft, HOURS, INITIAL_FLIGHT_HOURS, INITIAL_FLIGHT_HOURS_TSN );
         }
      } );

      Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( DateUtils.addDays( FLIGHT_DATE, 1 ) );
            aFlight.addUsage( lAircraft, HOURS, INITIAL_FLIGHT_HOURS,
                  INITIAL_FLIGHT_HOURS_TSN.add( INITIAL_FLIGHT_HOURS ) );
         }
      } );

      final BigDecimal lDeadlineStartValue =
            COMPONENT_CURRENT_HOURS.subtract( INITIAL_FLIGHT_HOURS ).add( TEN );
      final BigDecimal lDeadlineDueValue = lDeadlineStartValue.add( ONE );

      TaskKey lReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setInventory( lSerializedKey );
            aReq.setStatus( RefEventStatusKey.ACTV );
            aReq.addUsageDeadline( HOURS, RefSchedFromKey.EFFECTIV, lDeadlineStartValue,
                  DEADLINE_HOURS_INTERVAL, lDeadlineDueValue );
         }
      } );

      // When the flight is edited and the usage is modified.
      editHistFlightUsage( lFlight, UPDATED_FLIGHT_HOURS, lAircraft );

      // Then the task's deadline start and due values are adjusted by the difference of the deltas.
      BigDecimal lUsageDifference = UPDATED_FLIGHT_HOURS.subtract( INITIAL_FLIGHT_HOURS );

      Double lExpectedStartValue = lDeadlineStartValue.add( lUsageDifference ).doubleValue();
      Double lExpectedDueValue = lDeadlineDueValue.add( lUsageDifference ).doubleValue();

      EvtSchedDeadTable lEvtSchedDead = EvtSchedDeadTable.findByPrimaryKey( lReq, HOURS );
      Double lActualStartValue = lEvtSchedDead.getStartQt();
      Double lActualDueValue = lEvtSchedDead.getDeadlineQt();

      assertEquals( "Unexpected deadline start value.", lExpectedStartValue, lActualStartValue );
      assertEquals( "Unexpected deadline due value.", lExpectedDueValue, lActualDueValue );
   }


   /**
    * <pre>
    * Given an Aircraft with a Historical Flight,
    * And the Aircraft has a Task with a Deadline that starts at the same time as the flight,
    * The task started after the flight date (The previous task completed after the flight)
    * And the Deadline is based on a Usage Parameter,
    * When the Historical Flight is edited,
    * And the Delta for that Usage Parameter is modified,
    * Then the Deadline Start and Due quantities should be updated by the change to the Delta.
    * </pre>
    */
   @Test
   public void itAdjustsDeadlinesOnAircraftTaskThatWasInitializedAtSameTimeAsTheEditedFlight()
         throws Exception {

      // Given an aircraft collecting usage.
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( HOURS, AIRCRAFT_CURRENT_HOURS );
         }
      } );

      // Given a flight for the aircraft that accrued usage.
      FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( FLIGHT_DATE );
            aFlight.addUsage( lAircraft, HOURS, INITIAL_FLIGHT_HOURS, INITIAL_FLIGHT_HOURS_TSN );
         }
      } );

      Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( DateUtils.addDays( FLIGHT_DATE, 1 ) );
            aFlight.addUsage( lAircraft, HOURS, INITIAL_FLIGHT_HOURS,
                  INITIAL_FLIGHT_HOURS_TSN.add( INITIAL_FLIGHT_HOURS ) );
         }
      } );

      // Given a requirement with a usage deadline, is against the aircraft, and was initialized at
      // the same time as the flight.
      //
      // Note: The task is considered initialized at the same time as the flight when its deadline
      // start value is the same as the flight usage tsn value.
      final BigDecimal lDeadlineStartValue = INITIAL_FLIGHT_HOURS_TSN;
      final BigDecimal lDeadlineDueValue = lDeadlineStartValue.add( ONE );

      final TaskKey lPreviousReq =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement aReq ) {
                  aReq.setInventory( lAircraft );
                  aReq.setStatus( RefEventStatusKey.COMPLETE );
                  aReq.setActualEndDate( DateUtils.addDays( FLIGHT_DATE, 2 ) );
               }
            } );

      TaskKey lReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setInventory( lAircraft );
            aReq.setPreviousTask( lPreviousReq );
            aReq.addUsageDeadline( HOURS, RefSchedFromKey.LASTEND, lDeadlineStartValue,
                  DEADLINE_HOURS_INTERVAL, lDeadlineDueValue );
         }
      } );

      // When the flight is edited and the usage is modified.
      editHistFlightUsage( lFlight, UPDATED_FLIGHT_HOURS, lAircraft );

      // Then the task's deadline start value and due value are adjusted by the difference of the
      // deltas.
      BigDecimal lUsageDifference = UPDATED_FLIGHT_HOURS.subtract( INITIAL_FLIGHT_HOURS );

      Double lExpectedStartValue = lDeadlineStartValue.add( lUsageDifference ).doubleValue();
      Double lExpectedDueValue = lDeadlineDueValue.add( lUsageDifference ).doubleValue();

      EvtSchedDeadTable lEvtSchedDead = EvtSchedDeadTable.findByPrimaryKey( lReq, HOURS );
      Double lActualStartValue = lEvtSchedDead.getStartQt();
      Double lActualDueValue = lEvtSchedDead.getDeadlineQt();

      assertEquals( "Unexpected deadline start value.", lExpectedStartValue, lActualStartValue );
      assertEquals( "Unexpected deadline due value.", lExpectedDueValue, lActualDueValue );
   }


   /**
    * <pre>
    * Given an Aircraft with a Historical Flight,
    * And the Aircraft has a Task with a Deadline that starts at the same time as the flight,
    * The task started before the flight date (The previous task completed before the flight)
    * And the Deadline is based on a Usage Parameter,
    * When the Historical Flight is edited,
    * And the Delta for that Usage Parameter is modified,
    * Then the Deadline Start and Due quantities should NOT be updated by the change to the Delta.
    * </pre>
    */
   @Test
   public void
         itDoesNotAdjustDeadlinesOnAircraftTaskThatWasInitializedAtSameTimeAsTheEditedFlightButPreviousTaskCompletedBeforeTheFlight()
               throws Exception {

      // Given an aircraft collecting usage.
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( HOURS, AIRCRAFT_CURRENT_HOURS );
         }
      } );

      // Given a flight for the aircraft that accrued usage.
      FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( FLIGHT_DATE );
            aFlight.addUsage( lAircraft, HOURS, INITIAL_FLIGHT_HOURS, INITIAL_FLIGHT_HOURS_TSN );
         }
      } );

      Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( DateUtils.addDays( FLIGHT_DATE, 1 ) );
            aFlight.addUsage( lAircraft, HOURS, INITIAL_FLIGHT_HOURS,
                  INITIAL_FLIGHT_HOURS_TSN.add( INITIAL_FLIGHT_HOURS ) );
         }
      } );

      // Given a requirement with a usage deadline, is against the aircraft, and was initialized at
      // the same time as the flight.
      //
      // Note: The task is considered initialized at the same time as the flight when its deadline
      // start value is the same as the flight usage tsn value.
      final BigDecimal lDeadlineStartValue = INITIAL_FLIGHT_HOURS_TSN;
      final BigDecimal lDeadlineDueValue = lDeadlineStartValue.add( ONE );

      final TaskKey lPreviousReq =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement aReq ) {
                  aReq.setInventory( lAircraft );
                  aReq.setStatus( RefEventStatusKey.COMPLETE );
                  aReq.setActualEndDate( DateUtils.addDays( FLIGHT_DATE, -2 ) );
               }
            } );

      TaskKey lReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setInventory( lAircraft );
            aReq.setPreviousTask( lPreviousReq );
            aReq.addUsageDeadline( HOURS, RefSchedFromKey.LASTEND, lDeadlineStartValue,
                  DEADLINE_HOURS_INTERVAL, lDeadlineDueValue );
         }
      } );

      // When the flight is edited and the usage is modified.
      editHistFlightUsage( lFlight, UPDATED_FLIGHT_HOURS, lAircraft );

      Double lExpectedStartValue = lDeadlineStartValue.doubleValue();
      Double lExpectedDueValue = lDeadlineDueValue.doubleValue();

      EvtSchedDeadTable lEvtSchedDead = EvtSchedDeadTable.findByPrimaryKey( lReq, HOURS );
      Double lActualStartValue = lEvtSchedDead.getStartQt();
      Double lActualDueValue = lEvtSchedDead.getDeadlineQt();

      assertEquals( "Unexpected deadline start value.", lExpectedStartValue, lActualStartValue );
      assertEquals( "Unexpected deadline due value.", lExpectedDueValue, lActualDueValue );
   }


   /**
    * <pre>
    * Given an Aircraft with a Historical Flight,
    * And the Aircraft has a Trk Inventory attached to it,
    * And the Trk Inventory has a completed Task with a Deadline that starts at the same time as the flight,
    * And the Deadline is based on a Usage Parameter,
    * When the Historical Flight is edited,
    * And the Delta for that Usage Parameter is modified,
    * Then the Deadline Start and Due quantities should be updated by the change to the Delta.
    * </pre>
    */
   @Test
   public void itAdjustsDeadlinesOnTrkTaskThatWasInitializedAtSameTimeAsTheEditedFlight()
         throws Exception {

      final InventoryKey lTrackedKey =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aTrk ) {
                  aTrk.addUsage( HOURS, COMPONENT_CURRENT_HOURS );
               }
            } );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( HOURS, AIRCRAFT_CURRENT_HOURS );
            aAircraft.addTracked( lTrackedKey );
         }
      } );

      FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( FLIGHT_DATE );
            aFlight.addUsage( lAircraft, HOURS, INITIAL_FLIGHT_HOURS, INITIAL_FLIGHT_HOURS_TSN );
         }
      } );

      // we need the extra flight so we are not editing the latest flight, which will do short-cut
      Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( DateUtils.addDays( FLIGHT_DATE, 1 ) );
            aFlight.addUsage( lAircraft, HOURS, INITIAL_FLIGHT_HOURS,
                  INITIAL_FLIGHT_HOURS_TSN.add( INITIAL_FLIGHT_HOURS ) );
         }
      } );

      // In order for the TRK to have a completed task with a deadline that starts at the same time
      // as the flight, the deadline's usage start quantity must equal the usage TSN quantity of the
      // TRK inventory at the time of the flight.
      //
      // There is second flight for the aircraft between the time of the flight and now, then
      // the current usage - flight usage is the usage at the time of the flight.
      final BigDecimal lTrkHoursTsnAtTimeOfFlight =
            COMPONENT_CURRENT_HOURS.subtract( INITIAL_FLIGHT_HOURS );
      final BigDecimal lDeadlineStartValue = lTrkHoursTsnAtTimeOfFlight;
      final BigDecimal lDeadlineDueValue = lDeadlineStartValue.add( ONE );

      final TaskKey lPreviousReq =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement aReq ) {
                  aReq.setInventory( lTrackedKey );
                  aReq.setStatus( RefEventStatusKey.COMPLETE );
                  aReq.setActualEndDate( DateUtils.addHours( FLIGHT_DATE, 1 ) );
               }
            } );

      TaskKey lReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setInventory( lTrackedKey );
            aReq.setPreviousTask( lPreviousReq );
            aReq.setStatus( RefEventStatusKey.COMPLETE );
            aReq.setActualEndDate( DateUtils.addHours( FLIGHT_DATE, 12 ) );
            aReq.addUsageDeadline( HOURS, RefSchedFromKey.LASTEND, lDeadlineStartValue,
                  DEADLINE_HOURS_INTERVAL, lDeadlineDueValue );
         }
      } );

      // When the flight is edited and the usage is modified.
      editHistFlightUsage( lFlight, UPDATED_FLIGHT_HOURS, lAircraft );

      // Then the task's deadline start and due values are adjusted by the difference of the deltas.
      BigDecimal lUsageDifference = UPDATED_FLIGHT_HOURS.subtract( INITIAL_FLIGHT_HOURS );

      Double lExpectedStartValue = lDeadlineStartValue.add( lUsageDifference ).doubleValue();
      Double lExpectedDueValue = lDeadlineDueValue.add( lUsageDifference ).doubleValue();

      EvtSchedDeadTable lEvtSchedDead = EvtSchedDeadTable.findByPrimaryKey( lReq, HOURS );
      Double lActualStartValue = lEvtSchedDead.getStartQt();
      Double lActualDueValue = lEvtSchedDead.getDeadlineQt();

      assertEquals( "Unexpected deadline start value.", lExpectedStartValue, lActualStartValue );
      assertEquals( "Unexpected deadline due value.", lExpectedDueValue, lActualDueValue );
   }


   /**
    * <pre>
    * Given an Aircraft with a Historical Flight,
    * And the Aircraft has a Trk Inventory attached to it,
    * And the Trk Inventory has a completed Task with a Deadline that starts at the same time as the flight,
    * The task started before the flight date (The previous task completed before the flight)
    * And the Deadline is based on a Usage Parameter,
    * When the Historical Flight is edited,
    * And the Delta for that Usage Parameter is modified,
    * Then the Deadline Start and Due quantities should NOT be updated by the change to the Delta.
    * </pre>
    */
   @Test
   public void
         itDoesNotAdjustDeadlinesOnTrkTaskThatWasInitializedAtSameTimeAsTheEditedFlightButPreviousTaskCompletedBeforeTheFlight()
               throws Exception {

      final InventoryKey lTrackedKey =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aTrk ) {
                  aTrk.addUsage( HOURS, COMPONENT_CURRENT_HOURS );
               }
            } );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( HOURS, AIRCRAFT_CURRENT_HOURS );
            aAircraft.addTracked( lTrackedKey );
         }
      } );

      FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( FLIGHT_DATE );
            aFlight.addUsage( lAircraft, HOURS, INITIAL_FLIGHT_HOURS, INITIAL_FLIGHT_HOURS_TSN );
         }
      } );

      // we need the extra flight so we are not editing the latest flight, which will do short-cut
      Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( DateUtils.addDays( FLIGHT_DATE, 1 ) );
            aFlight.addUsage( lAircraft, HOURS, INITIAL_FLIGHT_HOURS,
                  INITIAL_FLIGHT_HOURS_TSN.add( INITIAL_FLIGHT_HOURS ) );
         }
      } );

      // In order for the TRK to have a completed task with a deadline that starts at the same time
      // as the flight, the deadline's usage start quantity must equal the usage TSN quantity of the
      // TRK inventory at the time of the flight.
      //
      // There is second flight for the aircraft between the time of the flight and now, then
      // the current usage - flight usage is the usage at the time of the flight.
      final BigDecimal lTrkHoursTsnAtTimeOfFlight =
            COMPONENT_CURRENT_HOURS.subtract( INITIAL_FLIGHT_HOURS );
      final BigDecimal lDeadlineStartValue = lTrkHoursTsnAtTimeOfFlight;
      final BigDecimal lDeadlineDueValue = lDeadlineStartValue.add( ONE );

      final TaskKey lPreviousReq =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement aReq ) {
                  aReq.setInventory( lAircraft );
                  aReq.setStatus( RefEventStatusKey.COMPLETE );
                  aReq.setActualEndDate( DateUtils.addHours( FLIGHT_DATE, -1 ) );
               }
            } );

      TaskKey lReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setInventory( lTrackedKey );
            aReq.setPreviousTask( lPreviousReq );
            aReq.setStatus( RefEventStatusKey.COMPLETE );
            aReq.addUsageDeadline( HOURS, RefSchedFromKey.LASTEND, lDeadlineStartValue,
                  DEADLINE_HOURS_INTERVAL, lDeadlineDueValue );
         }
      } );

      // When the flight is edited and the usage is modified.
      editHistFlightUsage( lFlight, UPDATED_FLIGHT_HOURS, lAircraft );

      Double lExpectedStartValue = lDeadlineStartValue.doubleValue();
      Double lExpectedDueValue = lDeadlineDueValue.doubleValue();

      EvtSchedDeadTable lEvtSchedDead = EvtSchedDeadTable.findByPrimaryKey( lReq, HOURS );
      Double lActualStartValue = lEvtSchedDead.getStartQt();
      Double lActualDueValue = lEvtSchedDead.getDeadlineQt();

      assertEquals( "Unexpected deadline start value.", lExpectedStartValue, lActualStartValue );
      assertEquals( "Unexpected deadline due value.", lExpectedDueValue, lActualDueValue );
   }


   /**
    * <pre>
    * Given an Aircraft with a Historical Flight,
    * And the Aircraft has a Trk Inventory attached to it,
    * And the Trk Inventory has a completed Task with a Deadline that starts at the same time as the flight,
    * The task effective before the flight date (The task definition effective date before the flight)
    * And the Deadline is based on a Usage Parameter,
    * When the Historical Flight is edited,
    * And the Delta for that Usage Parameter is modified,
    * Then the Deadline Start and Due quantities should NOT be updated by the change to the Delta.
    * </pre>
    */
   @Test
   public void
         itDoesNotAdjustDeadlinesOnTrkTaskThatWasInitializedAtSameTimeAsTheEditedFlightButEffectiveBeforeTheFlight()
               throws Exception {

      final InventoryKey lTrackedKey =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aTrk ) {
                  aTrk.addUsage( HOURS, COMPONENT_CURRENT_HOURS );
               }
            } );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( HOURS, AIRCRAFT_CURRENT_HOURS );
            aAircraft.addTracked( lTrackedKey );
         }
      } );

      FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( FLIGHT_DATE );
            aFlight.addUsage( lAircraft, HOURS, INITIAL_FLIGHT_HOURS, INITIAL_FLIGHT_HOURS_TSN );
         }
      } );

      // we need the extra flight so we are not editing the latest flight, which will do short-cut
      Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( DateUtils.addDays( FLIGHT_DATE, 1 ) );
            aFlight.addUsage( lAircraft, HOURS, INITIAL_FLIGHT_HOURS,
                  INITIAL_FLIGHT_HOURS_TSN.add( INITIAL_FLIGHT_HOURS ) );
         }
      } );

      // In order for the TRK to have a completed task with a deadline that starts at the same time
      // as the flight, the deadline's usage start quantity must equal the usage TSN quantity of the
      // TRK inventory at the time of the flight.
      //
      // There is second flight for the aircraft between the time of the flight and now, then
      // the current usage - flight usage is the usage at the time of the flight.
      final BigDecimal lTrkHoursTsnAtTimeOfFlight =
            COMPONENT_CURRENT_HOURS.subtract( INITIAL_FLIGHT_HOURS );
      final BigDecimal lDeadlineStartValue = lTrkHoursTsnAtTimeOfFlight;
      final BigDecimal lDeadlineDueValue = lDeadlineStartValue.add( ONE );

      final TaskTaskKey lReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.setOnCondition( true );
                  aReqDefn.setScheduledFromEffectiveDate( DateUtils.addHours( FLIGHT_DATE, -3 ) );
                  aReqDefn.setRecurring( false );
               }
            } );

      TaskKey lReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setInventory( lTrackedKey );
            aReq.setDefinition( lReqDefn );
            aReq.setStatus( RefEventStatusKey.COMPLETE );
            aReq.addUsageDeadline( HOURS, RefSchedFromKey.EFFECTIV, lDeadlineStartValue,
                  DEADLINE_HOURS_INTERVAL, lDeadlineDueValue );
         }
      } );

      // When the flight is edited and the usage is modified.
      editHistFlightUsage( lFlight, UPDATED_FLIGHT_HOURS, lAircraft );

      Double lExpectedStartValue = lDeadlineStartValue.doubleValue();
      Double lExpectedDueValue = lDeadlineDueValue.doubleValue();

      EvtSchedDeadTable lEvtSchedDead = EvtSchedDeadTable.findByPrimaryKey( lReq, HOURS );
      Double lActualStartValue = lEvtSchedDead.getStartQt();
      Double lActualDueValue = lEvtSchedDead.getDeadlineQt();

      assertEquals( "Unexpected deadline start value.", lExpectedStartValue, lActualStartValue );
      assertEquals( "Unexpected deadline due value.", lExpectedDueValue, lActualDueValue );
   }


   /**
    * <pre>
    * Given an Aircraft with a Historical Flight,
    * And the Aircraft has a Trk Inventory attached to it,
    * And the Trk Inventory has a completed Task with a Deadline that starts at the birth,
    * And the Deadline is based on a Usage Parameter,
    * When the Historical Flight is edited,
    * And the Delta for that Usage Parameter is modified,
    * Then the Deadline Start and Due quantities should NOT be updated by the change to the Delta.
    * </pre>
    */
   @Test
   public void itDoesNotAdjustDeadlinesOnTrkTaskThatWasInitializedFromBirth() throws Exception {

      final InventoryKey lTrackedKey =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aTrk ) {
                  aTrk.addUsage( HOURS, COMPONENT_CURRENT_HOURS );
               }
            } );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( HOURS, AIRCRAFT_CURRENT_HOURS );
            aAircraft.addTracked( lTrackedKey );
         }
      } );

      FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( FLIGHT_DATE );
            aFlight.addUsage( lAircraft, HOURS, INITIAL_FLIGHT_HOURS, INITIAL_FLIGHT_HOURS_TSN );
         }
      } );

      // we need the extra flight so we are not editing the latest flight, which will do short-cut
      Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( DateUtils.addDays( FLIGHT_DATE, 1 ) );
            aFlight.addUsage( lAircraft, HOURS, INITIAL_FLIGHT_HOURS,
                  INITIAL_FLIGHT_HOURS_TSN.add( INITIAL_FLIGHT_HOURS ) );
         }
      } );

      // In order for the TRK to have a completed task with a deadline that starts at the same time
      // as the flight, the deadline's usage start quantity must equal the usage TSN quantity of the
      // TRK inventory at the time of the flight.
      //
      // There is second flight for the aircraft between the time of the flight and now, then
      // the current usage - flight usage is the usage at the time of the flight.
      final BigDecimal lTrkHoursTsnAtTimeOfFlight = COMPONENT_CURRENT_HOURS;
      final BigDecimal lDeadlineStartValue = lTrkHoursTsnAtTimeOfFlight.add( TEN );
      final BigDecimal lDeadlineDueValue = lDeadlineStartValue.add( ONE );

      TaskKey lReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setInventory( lTrackedKey );
            aReq.setStatus( RefEventStatusKey.COMPLETE );
            aReq.addUsageDeadline( HOURS, RefSchedFromKey.BIRTH, lDeadlineStartValue,
                  DEADLINE_HOURS_INTERVAL, lDeadlineDueValue );
         }
      } );

      // When the flight is edited and the usage is modified.
      editHistFlightUsage( lFlight, UPDATED_FLIGHT_HOURS, lAircraft );

      Double lExpectedStartValue = lDeadlineStartValue.doubleValue();
      Double lExpectedDueValue = lDeadlineDueValue.doubleValue();

      EvtSchedDeadTable lEvtSchedDead = EvtSchedDeadTable.findByPrimaryKey( lReq, HOURS );
      Double lActualStartValue = lEvtSchedDead.getStartQt();
      Double lActualDueValue = lEvtSchedDead.getDeadlineQt();

      assertEquals( "Unexpected deadline start value.", lExpectedStartValue, lActualStartValue );
      assertEquals( "Unexpected deadline due value.", lExpectedDueValue, lActualDueValue );
   }


   /**
    * <pre>
    * Given an Aircraft with a Historical Flight,
    * And the Aircraft has a Trk Inventory attached to it,
    * And the Trk Inventory has a completed Task with a Deadline that starts at the same time as the flight,
    * The task effective after the flight date (The task definition effective date after the flight)
    * And the Deadline is based on a Usage Parameter,
    * When the Historical Flight is edited,
    * And the Delta for that Usage Parameter is modified,
    * Then the Deadline Start and Due quantities should be updated by the change to the Delta.
    * </pre>
    */
   @Test
   public void
         itAdjustsDeadlinesOnTrkTaskThatWasInitializedAtSameTimeAsTheEditedFlightButEffectiveAfterTheFlight()
               throws Exception {

      final InventoryKey lTrackedKey =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aTrk ) {
                  aTrk.addUsage( HOURS, COMPONENT_CURRENT_HOURS );
               }
            } );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( HOURS, AIRCRAFT_CURRENT_HOURS );
            aAircraft.addTracked( lTrackedKey );
         }
      } );

      FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( FLIGHT_DATE );
            aFlight.addUsage( lAircraft, HOURS, INITIAL_FLIGHT_HOURS, INITIAL_FLIGHT_HOURS_TSN );
         }
      } );

      // we need the extra flight so we are not editing the latest flight, which will do short-cut
      Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( DateUtils.addDays( FLIGHT_DATE, 1 ) );
            aFlight.addUsage( lAircraft, HOURS, INITIAL_FLIGHT_HOURS,
                  INITIAL_FLIGHT_HOURS_TSN.add( INITIAL_FLIGHT_HOURS ) );
         }
      } );

      // In order for the TRK to have a completed task with a deadline that starts at the same time
      // as the flight, the deadline's usage start quantity must equal the usage TSN quantity of the
      // TRK inventory at the time of the flight.
      //
      // There is second flight for the aircraft between the time of the flight and now, then
      // the current usage - flight usage is the usage at the time of the flight.
      final BigDecimal lTrkHoursTsnAtTimeOfFlight =
            COMPONENT_CURRENT_HOURS.subtract( INITIAL_FLIGHT_HOURS );
      final BigDecimal lDeadlineStartValue = lTrkHoursTsnAtTimeOfFlight;
      final BigDecimal lDeadlineDueValue = lDeadlineStartValue.add( ONE );

      final TaskTaskKey lReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.setOnCondition( true );
                  aReqDefn.setScheduledFromEffectiveDate( DateUtils.addHours( FLIGHT_DATE, 3 ) );
                  aReqDefn.setRecurring( false );
               }
            } );

      TaskKey lReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setInventory( lTrackedKey );
            aReq.setDefinition( lReqDefn );
            aReq.setStatus( RefEventStatusKey.COMPLETE );
            aReq.setActualEndDate( DateUtils.addHours( FLIGHT_DATE, 1 ) );
            aReq.addUsageDeadline( HOURS, RefSchedFromKey.EFFECTIV, lDeadlineStartValue,
                  DEADLINE_HOURS_INTERVAL, lDeadlineDueValue );
         }
      } );

      // When the flight is edited and the usage is modified.
      editHistFlightUsage( lFlight, UPDATED_FLIGHT_HOURS, lAircraft );

      // Then the task's deadline start and due values are adjusted by the difference of the deltas.
      BigDecimal lUsageDifference = UPDATED_FLIGHT_HOURS.subtract( INITIAL_FLIGHT_HOURS );

      Double lExpectedStartValue = lDeadlineStartValue.add( lUsageDifference ).doubleValue();
      Double lExpectedDueValue = lDeadlineDueValue.add( lUsageDifference ).doubleValue();

      EvtSchedDeadTable lEvtSchedDead = EvtSchedDeadTable.findByPrimaryKey( lReq, HOURS );
      Double lActualStartValue = lEvtSchedDead.getStartQt();
      Double lActualDueValue = lEvtSchedDead.getDeadlineQt();

      assertEquals( "Unexpected deadline start value.", lExpectedStartValue, lActualStartValue );
      assertEquals( "Unexpected deadline due value.", lExpectedDueValue, lActualDueValue );
   }


   /**
    * <pre>
    * Given an Aircraft with a Historical Flight,
    * And the Aircraft has a completed Task with a Deadline schedule type CUSTOM,
    * The task is not a correction task for a fault
    * And the Deadline is based on a Usage Parameter,
    * When the Historical Flight is edited,
    * And the Delta for that Usage Parameter is modified,
    * Then the Deadline Start and Due quantities should NOT be updated by the change to the Delta.
    * </pre>
    */
   @Test
   public void itDoesNotAdjustCustomDeadlinesOnNonFaultTask() throws Exception {

      // Given an aircraft.
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( HOURS, AIRCRAFT_CURRENT_HOURS );
         }
      } );

      // Given a flight for the aircraft that accrued usage.
      FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( FLIGHT_DATE );
            aFlight.addUsage( lAircraft, HOURS, INITIAL_FLIGHT_HOURS, INITIAL_FLIGHT_HOURS_TSN );
         }
      } );

      Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( DateUtils.addDays( FLIGHT_DATE, 1 ) );
            aFlight.addUsage( lAircraft, HOURS, INITIAL_FLIGHT_HOURS,
                  INITIAL_FLIGHT_HOURS_TSN.add( INITIAL_FLIGHT_HOURS ) );
         }
      } );

      // Given a requirement with a usage deadline, is against the aircraft, and was initialized
      // before the flight.
      //
      // Note: The task is considered initialized before the flight when its deadline start value is
      // less than the flight usage value.

      final BigDecimal lDeadlineStartValue = INITIAL_FLIGHT_HOURS_TSN.add( TEN );
      final BigDecimal lDeadlineDueValue = lDeadlineStartValue.add( ONE );

      TaskKey lReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setInventory( lAircraft );
            aReq.addUsageDeadline( HOURS, RefSchedFromKey.CUSTOM, lDeadlineStartValue,
                  DEADLINE_HOURS_INTERVAL, lDeadlineDueValue );
         }
      } );

      // When the flight is edited and the usage is modified.
      editHistFlightUsage( lFlight, UPDATED_FLIGHT_HOURS, lAircraft );

      // Then the task's deadline start value and due value are NOT adjusted.
      EvtSchedDeadTable lEvtSchedDead = EvtSchedDeadTable.findByPrimaryKey( lReq, HOURS );
      BigDecimal lActualStartValue = new BigDecimal( lEvtSchedDead.getStartQt() );
      BigDecimal lActualDueValue = new BigDecimal( lEvtSchedDead.getDeadlineQt() );

      assertEquals( "Unexpected deadline start value.", lDeadlineStartValue, lActualStartValue );
      assertEquals( "Unexpected deadline due value.", lDeadlineDueValue, lActualDueValue );
   }


   /**
    * <pre>
    * Given an Aircraft with a Historical Flight,
    * And the Aircraft has a completed Task with a Deadline schedule type CUSTOM,
    * The task is a correction task for a fault
    * And the Deadline is based on a Usage Parameter,
    * When the Historical Flight is edited,
    * And the Delta for that Usage Parameter is modified,
    * Then the Deadline Start and Due quantities should be updated by the change to the Delta.
    * </pre>
    */
   @Test
   public void itAdjustCustomDeadlinesOnFaultTask() throws Exception {

      // Given an aircraft.
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( HOURS, AIRCRAFT_CURRENT_HOURS );
         }
      } );

      // Given a flight for the aircraft that accrued usage.
      FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( FLIGHT_DATE );
            aFlight.addUsage( lAircraft, HOURS, INITIAL_FLIGHT_HOURS, INITIAL_FLIGHT_HOURS_TSN );
         }
      } );

      Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( DateUtils.addDays( FLIGHT_DATE, 1 ) );
            aFlight.addUsage( lAircraft, HOURS, INITIAL_FLIGHT_HOURS,
                  INITIAL_FLIGHT_HOURS_TSN.add( INITIAL_FLIGHT_HOURS ) );
         }
      } );

      // Given a requirement with a usage deadline, is against the aircraft, and was initialized
      // before the flight.
      //
      // Note: The task is considered initialized before the flight when its deadline start value is
      // less than the flight usage value.

      final BigDecimal lDeadlineStartValue = INITIAL_FLIGHT_HOURS_TSN.add( TEN );
      final BigDecimal lDeadlineDueValue = lDeadlineStartValue.add( ONE );

      final TaskKey lReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setInventory( lAircraft );
            aReq.addUsageDeadline( HOURS, RefSchedFromKey.CUSTOM, lDeadlineStartValue,
                  DEADLINE_HOURS_INTERVAL, lDeadlineDueValue );
         }
      } );

      FaultKey lFaultKey = Domain.createFault( new DomainConfiguration<Fault>() {

         @Override
         public void configure( Fault aFault ) {
            aFault.setInventory( lAircraft );
            aFault.setFoundOnDate( FLIGHT_DATE );
            aFault.setCorrectiveTask( lReq );
         }
      } );

      // When the flight is edited and the usage is modified.
      editHistFlightUsage( lFlight, UPDATED_FLIGHT_HOURS, lAircraft );

      BigDecimal lUsageDifference = UPDATED_FLIGHT_HOURS.subtract( INITIAL_FLIGHT_HOURS );

      Double lExpectedStartValue = lDeadlineStartValue.add( lUsageDifference ).doubleValue();
      Double lExpectedDueValue = lDeadlineDueValue.add( lUsageDifference ).doubleValue();

      EvtSchedDeadTable lEvtSchedDead = EvtSchedDeadTable.findByPrimaryKey( lReq, HOURS );
      Double lActualStartValue = lEvtSchedDead.getStartQt();
      Double lActualDueValue = lEvtSchedDead.getDeadlineQt();

      assertEquals( "Unexpected deadline start value.", lExpectedStartValue, lActualStartValue );
      assertEquals( "Unexpected deadline due value.", lExpectedDueValue, lActualDueValue );
   }


   /**
    * <pre>
    * Given an Aircraft with a Historical Flight,
    * And the Aircraft has a completed Task with a Deadline schedule type CUSTOM,
    * The task is a correction task for a fault
    * The task deadline initialized at the same time as the flight
    * The fault found after the flight
    * And the Deadline is based on a Usage Parameter,
    * When the Historical Flight is edited,
    * And the Delta for that Usage Parameter is modified,
    * Then the Deadline Start and Due quantities should be updated by the change to the Delta.
    * </pre>
    */
   @Test
   public void
         itAdjustCustomDeadlinesOnFaultTaskInitializedAtTheSameTimeAsTheEditedFlightButFaultFoundAfterTheFlight()
               throws Exception {

      // Given an aircraft.
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( HOURS, AIRCRAFT_CURRENT_HOURS );
         }
      } );

      // Given a flight for the aircraft that accrued usage.
      FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( FLIGHT_DATE );
            aFlight.addUsage( lAircraft, HOURS, INITIAL_FLIGHT_HOURS, INITIAL_FLIGHT_HOURS_TSN );
         }
      } );

      Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( DateUtils.addDays( FLIGHT_DATE, 1 ) );
            aFlight.addUsage( lAircraft, HOURS, INITIAL_FLIGHT_HOURS,
                  INITIAL_FLIGHT_HOURS_TSN.add( INITIAL_FLIGHT_HOURS ) );
         }
      } );

      // Given a requirement with a usage deadline, is against the aircraft, and was initialized
      // before the flight.
      //
      // Note: The task is considered initialized at the same time as the flight when its deadline
      // start value is the same as the flight usage tsn value.
      final BigDecimal lDeadlineStartValue = INITIAL_FLIGHT_HOURS_TSN;
      final BigDecimal lDeadlineDueValue = lDeadlineStartValue.add( ONE );

      final TaskKey lReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setInventory( lAircraft );
            aReq.addUsageDeadline( HOURS, RefSchedFromKey.CUSTOM, lDeadlineStartValue,
                  DEADLINE_HOURS_INTERVAL, lDeadlineDueValue );
         }
      } );

      FaultKey lFaultKey = Domain.createFault( new DomainConfiguration<Fault>() {

         @Override
         public void configure( Fault aFault ) {
            aFault.setInventory( lAircraft );
            aFault.setFoundOnDate( DateUtils.addDays( FLIGHT_DATE, 2 ) );
            aFault.setCorrectiveTask( lReq );
         }
      } );

      // When the flight is edited and the usage is modified.
      editHistFlightUsage( lFlight, UPDATED_FLIGHT_HOURS, lAircraft );

      BigDecimal lUsageDifference = UPDATED_FLIGHT_HOURS.subtract( INITIAL_FLIGHT_HOURS );

      Double lExpectedStartValue = lDeadlineStartValue.add( lUsageDifference ).doubleValue();
      Double lExpectedDueValue = lDeadlineDueValue.add( lUsageDifference ).doubleValue();

      EvtSchedDeadTable lEvtSchedDead = EvtSchedDeadTable.findByPrimaryKey( lReq, HOURS );
      Double lActualStartValue = lEvtSchedDead.getStartQt();
      Double lActualDueValue = lEvtSchedDead.getDeadlineQt();

      assertEquals( "Unexpected deadline start value.", lExpectedStartValue, lActualStartValue );
      assertEquals( "Unexpected deadline due value.", lExpectedDueValue, lActualDueValue );
   }


   /**
    * <pre>
    * Given an Aircraft with a Historical Flight,
    * And the Aircraft has a completed Task with a Deadline schedule type CUSTOM,
    * The task is a correction task for a fault
    * The task deadline initialized at the same time as the flight
    * The fault found before the flight
    * And the Deadline is based on a Usage Parameter,
    * When the Historical Flight is edited,
    * And the Delta for that Usage Parameter is modified,
    * Then the Deadline Start and Due quantities should Not be updated by the change to the Delta.
    * </pre>
    */
   @Test
   public void
         itDoesNotAdjustCustomDeadlinesOnFaultTaskInitializedAtTheSameTimeAsTheEditedFlightButFaultFoundBeforeTheFlight()
               throws Exception {

      // Given an aircraft.
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( HOURS, AIRCRAFT_CURRENT_HOURS );
         }
      } );

      // Given a flight for the aircraft that accrued usage.
      FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( FLIGHT_DATE );
            aFlight.addUsage( lAircraft, HOURS, INITIAL_FLIGHT_HOURS, INITIAL_FLIGHT_HOURS_TSN );
         }
      } );

      Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( DateUtils.addDays( FLIGHT_DATE, 1 ) );
            aFlight.addUsage( lAircraft, HOURS, INITIAL_FLIGHT_HOURS,
                  INITIAL_FLIGHT_HOURS_TSN.add( INITIAL_FLIGHT_HOURS ) );
         }
      } );

      // Given a requirement with a usage deadline, is against the aircraft, and was initialized
      // before the flight.
      //
      // Note: The task is considered initialized at the same time as the flight when its deadline
      // start value is the same as the flight usage tsn value.
      final BigDecimal lDeadlineStartValue = INITIAL_FLIGHT_HOURS_TSN;
      final BigDecimal lDeadlineDueValue = lDeadlineStartValue.add( ONE );

      final TaskKey lReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setInventory( lAircraft );
            aReq.addUsageDeadline( HOURS, RefSchedFromKey.CUSTOM, lDeadlineStartValue,
                  DEADLINE_HOURS_INTERVAL, lDeadlineDueValue );
         }
      } );

      FaultKey lFaultKey = Domain.createFault( new DomainConfiguration<Fault>() {

         @Override
         public void configure( Fault aFault ) {
            aFault.setInventory( lAircraft );
            aFault.setFoundOnDate( DateUtils.addDays( FLIGHT_DATE, -2 ) );
            aFault.setCorrectiveTask( lReq );
         }
      } );

      // When the flight is edited and the usage is modified.
      editHistFlightUsage( lFlight, UPDATED_FLIGHT_HOURS, lAircraft );

      Double lExpectedStartValue = lDeadlineStartValue.doubleValue();
      Double lExpectedDueValue = lDeadlineDueValue.doubleValue();

      EvtSchedDeadTable lEvtSchedDead = EvtSchedDeadTable.findByPrimaryKey( lReq, HOURS );
      Double lActualStartValue = lEvtSchedDead.getStartQt();
      Double lActualDueValue = lEvtSchedDead.getDeadlineQt();

      assertEquals( "Unexpected deadline start value.", lExpectedStartValue, lActualStartValue );
      assertEquals( "Unexpected deadline due value.", lExpectedDueValue, lActualDueValue );
   }


   @Before
   public void setup() {
      iHrKey = new HumanResourceDomainBuilder().withUsername( HR_USERNAME ).build();

      int lUserId = OrgHr.findByPrimaryKey( iHrKey ).getUserId();
      UserParameters.setInstance( lUserId, "LOGIC", new UserParametersFake( lUserId, "LOGIC" ) );

      iFlightHistBean = new FlightHistBean();
      iFlightHistBean.ejbCreate();
      iFlightHistBean.setSessionContext( new SessionContextFake() );

   }


   @After
   public void tearDown() {
      int lUserId = OrgHr.findByPrimaryKey( iHrKey ).getUserId();
      UserParameters.setInstance( lUserId, "LOGIC", null );
   }


   private void editHistFlightUsage( FlightLegId aFlight, BigDecimal aUpdatedFlightHours,
         InventoryKey... aInventoryKeys ) throws Exception {

      FlightInformationTO lUpdatedFlightTo =
            generateFlightInfoTO( FLIGHT_NAME, FLIGHT_DATE, UPDATED_FLIGHT_HOURS );

      List<CollectedUsageParm> lUpdatedFlightUsages =
            new ArrayList<CollectedUsageParm>( aInventoryKeys.length );
      for ( InventoryKey lInventoryKey : aInventoryKeys ) {
         lUpdatedFlightUsages
               .add( generateFlightUsage( lInventoryKey, HOURS, aUpdatedFlightHours ) );
      } ;

      iFlightHistBean.editHistFlight( aFlight, iHrKey, lUpdatedFlightTo,
            lUpdatedFlightUsages.toArray( new CollectedUsageParm[] {} ), NO_MEASUREMENTS );
   }


   private FlightInformationTO generateFlightInfoTO( String aName, Date aArrivalDate,
         BigDecimal aHours ) {

      LocationKey lDepartureAirport = Domain.createLocation( new DomainConfiguration<Location>() {

         @Override
         public void configure( Location aLocation ) {
            aLocation.setType( RefLocTypeKey.AIRPORT );
         }
      } );
      LocationKey lArrivalAirport = Domain.createLocation( new DomainConfiguration<Location>() {

         @Override
         public void configure( Location aLocation ) {
            aLocation.setType( RefLocTypeKey.AIRPORT );
         }
      } );

      Date lDepartureDate =
            DateUtils.addHours( aArrivalDate, aHours.multiply( new BigDecimal( -1 ) ).intValue() );

      return new FlightInformationTO( aName, null, null, null, null, null, lDepartureAirport,
            lArrivalAirport, null, null, null, null, lDepartureDate, aArrivalDate, null, null,
            false, false );
   }


   private CollectedUsageParm generateFlightUsage( InventoryKey aInventory, DataTypeKey lDataType,
         BigDecimal lDelta ) {

      // Create a usage collection to be returned.
      CollectedUsageParm lUsageParm = new CollectedUsageParm(
            new UsageParmKey( aInventory, lDataType ), lDelta.doubleValue() );

      // Create flight data source specifications.
      EqpDataSourceSpec
            .create( new UsageDefinitionKey( InvInvTable.getAssemblyByInventoryKey( aInventory ),
                  RefDataSourceKey.MXFL ), lDataType );

      return lUsageParm;
   }

}
