package com.mxi.mx.core.ejb.flighthist.flighthistbean.edithistflight;

import static com.mxi.mx.core.key.DataTypeKey.CYCLES;
import static com.mxi.mx.core.key.DataTypeKey.HOURS;
import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Engine;
import com.mxi.am.domain.Flight;
import com.mxi.am.domain.Location;
import com.mxi.am.domain.Requirement;
import com.mxi.am.domain.SerializedInventory;
import com.mxi.am.domain.System;
import com.mxi.am.domain.TrackedInventory;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.servlet.SessionContextFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.ejb.flighthist.CurrentUsages;
import com.mxi.mx.core.ejb.flighthist.FlightHistBean;
import com.mxi.mx.core.flight.model.FlightLegId;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefDataSourceKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefSchedFromKey;
import com.mxi.mx.core.key.TaskKey;
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
public class EditHistFlight_AffectLatestFlightTest {

   private static final BigDecimal INITIAL_FLIGHT_HOURS = new BigDecimal( 5 );
   private static final BigDecimal INITIAL_FLIGHT_HOURS_TSN = new BigDecimal( 555 );
   private static final BigDecimal INITIAL_FLIGHT_CYCLES = new BigDecimal( 4 );
   private static final BigDecimal INITIAL_FLIGHT_CYCLES_TSN = new BigDecimal( 444 );
   private static final BigDecimal UPDATED_FLIGHT_HOURS = new BigDecimal( 7 );
   private static final BigDecimal AIRCRAFT_CURRENT_HOURS = new BigDecimal( 1000 );
   private static final BigDecimal DEADLINE_HOURS_INTERVAL = new BigDecimal( 100 );

   private static final BigDecimal FLIGHT_DELTA = new BigDecimal( 2 );

   private static final BigDecimal INITIAL_CYCLES = new BigDecimal( 50 );
   private static final BigDecimal INITIAL_HOURS = new BigDecimal( 100 );

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
    * Verify that when latest flight's usage is updated, oos logic will be skipped.
    *
    * Note: the dummy requirement is not actually valid data in client database, it only verify the
    * by pass actually happens
    *
    * @throws Exception
    */
   @Test
   public void itSkipsLogicForLatestFlight() throws Exception {

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

      // Given a dummy requirement with a usage deadline, is against the aircraft, and was
      // initialized
      // after the flight.

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

      editHistFlightUsage( lFlight, UPDATED_FLIGHT_HOURS, lAircraft );

      EvtSchedDeadTable lEvtSchedDead = EvtSchedDeadTable.findByPrimaryKey( lReq, HOURS );
      Double lActualStartValue = lEvtSchedDead.getStartQt();
      Double lActualDueValue = lEvtSchedDead.getDeadlineQt();

      // For the latest flight, the dummy deadline value shall not be updated.
      Assert.assertTrue(
            lDeadlineStartValue.compareTo( new BigDecimal( lActualStartValue ) ) == 0 );
      Assert.assertTrue( lDeadlineDueValue.compareTo( new BigDecimal( lActualDueValue ) ) == 0 );
   }


   /**
    *
    * <pre>
    * Given an Aircraft with at least one Historical Flight,
    * When the Historical Flight is edited,
    * Then the current TSN of all changed Usage Parameters should be updated.
    * </pre>
    *
    */
   @Test
   public void itAdjustsCurrentUsageWhenLatestFlightIsEdited() throws Exception {

      final InventoryKey lTrackedKey =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aTrk ) {
                  aTrk.addUsage( HOURS, INITIAL_HOURS );
               }
            } );

      // Create an engine with a system.
      final InventoryKey lEngineInvKey = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {

            // Engine usage.
            aEngine.addUsage( HOURS, INITIAL_HOURS );

            // System of the engine.
            aEngine.addSystem( "Engine Subsystem" );
            aEngine.addTracked( lTrackedKey );
         }
      } );
      final InventoryKey lSubSysOfEngineInvKey =
            InvUtils.getSystemByName( lEngineInvKey, "Engine Subsystem" );

      // Create aircraft with one system having a sub-system and the engine installed.
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {

            // Aircraft usage.
            aAircraft.addUsage( CYCLES, INITIAL_CYCLES );

            // Fuel system of the aircraft.
            aAircraft.addSystem( new DomainConfiguration<System>() {

               @Override
               public void configure( System aSystem ) {
                  aSystem.setName( "28 - FUEL" );

                  // Subsystem of the fuel system.
                  aSystem.addSubSystem( "Fuel subsystem" );
               }
            } );

            // Engine installed on the aircraft.
            aAircraft.addEngine( lEngineInvKey );
         }
      } );

      final InventoryKey lFuelSysInvKey = InvUtils.getSystemByName( lAircraft, "28 - FUEL" );
      final InventoryKey lSubSysOfFuelSysInvKey =
            InvUtils.getSystemByName( lFuelSysInvKey, "Fuel subsystem" );

      FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( FLIGHT_DATE );
            aFlight.addUsage( lEngineInvKey, HOURS, INITIAL_FLIGHT_HOURS, INITIAL_HOURS );
            aFlight.addUsage( lAircraft, CYCLES, INITIAL_FLIGHT_CYCLES, INITIAL_CYCLES );
         }
      } );

      CollectedUsageParm[] lEditUsageParms = {
            generateFlightUsage( lAircraft, CYCLES, INITIAL_FLIGHT_CYCLES.add( FLIGHT_DELTA ) ),
            generateFlightUsage( lEngineInvKey, HOURS, INITIAL_FLIGHT_HOURS.add( FLIGHT_DELTA ) ) };

      FlightInformationTO lFlightTo =
            generateFlightInfoTO( FLIGHT_NAME, FLIGHT_DATE, FLIGHT_DELTA );

      iFlightHistBean.editHistFlight( lFlight, iHrKey, lFlightTo, lEditUsageParms,
            NO_MEASUREMENTS );

      // Assert current usage has been updated with the edited delta
      final BigDecimal lExpectedAircraftCyclesAfterEdit = INITIAL_CYCLES.add( FLIGHT_DELTA );
      final BigDecimal lExpectedEngineHoursAfterEdit = INITIAL_HOURS.add( FLIGHT_DELTA );

      assertCurrentUsage( lAircraft, CYCLES, lExpectedAircraftCyclesAfterEdit );

      assertCurrentUsage( lFuelSysInvKey, CYCLES, lExpectedAircraftCyclesAfterEdit );

      assertCurrentUsage( lSubSysOfFuelSysInvKey, CYCLES, lExpectedAircraftCyclesAfterEdit );

      assertCurrentUsage( lEngineInvKey, HOURS, lExpectedEngineHoursAfterEdit );

      assertCurrentUsage( lTrackedKey, HOURS, lExpectedEngineHoursAfterEdit );

      assertCurrentUsage( lSubSysOfEngineInvKey, HOURS, lExpectedEngineHoursAfterEdit );

   }


   /**
    *
    * <pre>
    * Given an Aircraft with at least one Historical Flight,
    * And the Aircraft has a SER Inventory attached to it,
    * When the Historical Flight is edited,
    * Then the current TSN of all changed Usage Parameters should be updated.
    * </pre>
    *
    */
   @Test
   public void itAdjustsCurrentUsageOfSerWhenLatestFlightIsEdited() throws Exception {

      final InventoryKey lSerializedKey =
            Domain.createSerializedInventory( new DomainConfiguration<SerializedInventory>() {

               @Override
               public void configure( SerializedInventory aSer ) {
                  aSer.addUsage( CYCLES, INITIAL_CYCLES );
                  aSer.addUsage( HOURS, INITIAL_HOURS );
               }
            } );

      final InventoryKey lAircraftInvKey =
            Domain.createAircraft( new DomainConfiguration<Aircraft>() {

               @Override
               public void configure( Aircraft aAircraft ) {
                  aAircraft.addSerialized( lSerializedKey );
                  aAircraft.addUsage( CYCLES, INITIAL_CYCLES );
                  aAircraft.addUsage( HOURS, INITIAL_HOURS );
               }
            } );

      // clear the assembly key
      InvInvTable lSerInv = InvInvTable.findByPrimaryKey( lSerializedKey );
      lSerInv.setAssmblInvNo( null );
      lSerInv.update();

      FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraftInvKey );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( FLIGHT_DATE );
            aFlight.addUsage( lAircraftInvKey, HOURS, INITIAL_FLIGHT_HOURS, INITIAL_HOURS );
            aFlight.addUsage( lAircraftInvKey, CYCLES, INITIAL_FLIGHT_CYCLES, INITIAL_CYCLES );
         }
      } );

      CollectedUsageParm[] lEditUsageParms = {
            generateFlightUsage( lAircraftInvKey, CYCLES,
                  INITIAL_FLIGHT_CYCLES.add( FLIGHT_DELTA ) ),
            generateFlightUsage( lAircraftInvKey, HOURS,
                  INITIAL_FLIGHT_HOURS.add( FLIGHT_DELTA ) ) };

      FlightInformationTO lFlightTo =
            generateFlightInfoTO( FLIGHT_NAME, FLIGHT_DATE, FLIGHT_DELTA );

      iFlightHistBean.editHistFlight( lFlight, iHrKey, lFlightTo, lEditUsageParms,
            NO_MEASUREMENTS );

      // Assert current usage has been updated with the edited delta
      final BigDecimal lExpectedAircraftCyclesAfterEdit = INITIAL_CYCLES.add( FLIGHT_DELTA );
      final BigDecimal lExpectedEngineHoursAfterEdit = INITIAL_HOURS.add( FLIGHT_DELTA );

      assertCurrentUsage( lAircraftInvKey, CYCLES, lExpectedAircraftCyclesAfterEdit );
      assertCurrentUsage( lAircraftInvKey, HOURS, lExpectedEngineHoursAfterEdit );

      assertCurrentUsage( lSerializedKey, CYCLES, lExpectedAircraftCyclesAfterEdit );
      assertCurrentUsage( lSerializedKey, HOURS, lExpectedEngineHoursAfterEdit );

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


   private void assertCurrentUsage( InventoryKey aInventory, DataTypeKey aDataType,
         BigDecimal aExpectedUsage ) {

      CurrentUsages lCurrentUsage = new CurrentUsages( aInventory );
      assertEquals( "Inventory=" + aInventory + " , data type=" + aDataType + " : unexpected TSN",
            aExpectedUsage, lCurrentUsage.getTsn( aDataType ) );
      assertEquals( "Inventory=" + aInventory + " , data type=" + aDataType + " : unexpected TSO",
            aExpectedUsage, lCurrentUsage.getTso( aDataType ) );
      assertEquals( "Inventory=" + aInventory + " , data type=" + aDataType + " : unexpected TSI",
            aExpectedUsage, lCurrentUsage.getTsi( aDataType ) );
   }

}
