package com.mxi.mx.core.ejb.flighthist.flighthistbean.edithistflight;

import static com.mxi.mx.core.key.DataTypeKey.CYCLES;
import static com.mxi.mx.core.key.DataTypeKey.HOURS;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
import com.mxi.am.domain.Fault;
import com.mxi.am.domain.Location;
import com.mxi.am.domain.builder.AttachmentService;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.servlet.SessionContextFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.ejb.flighthist.FlightHistBean;
import com.mxi.mx.core.flight.model.FlightLegId;
import com.mxi.mx.core.key.AircraftKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.EventInventoryUsageKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefDataSourceKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.UsageDefinitionKey;
import com.mxi.mx.core.key.UsageParmKey;
import com.mxi.mx.core.services.event.inventory.UsageSnapshot;
import com.mxi.mx.core.services.event.usage.CollectedUsageParm;
import com.mxi.mx.core.services.flighthist.FlightInformationTO;
import com.mxi.mx.core.services.flighthist.Measurement;
import com.mxi.mx.core.table.eqp.EqpDataSourceSpec;
import com.mxi.mx.core.table.evt.EvtInvTable;
import com.mxi.mx.core.table.evt.EvtInvUsage;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.org.OrgHr;


/**
 * Tests for {@linkplain FlightHistBean#editHistFlight} that involve updating the snapshot usage TSN
 * of inventory involved in the flight.
 *
 */

public class EditHistFlight_AffectFollowingFaultTest {

   private static final String HR_USERNAME = "HR_USERNAME";
   private static final String FLIGHT = "FLIGHT";
   private static final Measurement[] NO_MEASUREMENTS = new Measurement[] {};

   // current cycle and hour values
   private static final BigDecimal INITIAL_CYCLES = new BigDecimal( 50 );
   private static final BigDecimal INITIAL_HOURS = new BigDecimal( 100 );

   // some deltas
   private static final BigDecimal INITIAL_FLIGHT_DELTA = new BigDecimal( 1 );
   private static final BigDecimal DELTA_VALUE_THREE = new BigDecimal( 3 );

   // the bean under test
   private FlightHistBean iFlightHistBean;

   private HumanResourceKey iHrKey;
   private FlightInformationTO iFlightInfoTO;
   private Date iFlightDate = new Date();

   private InventoryKey iAircraftInvKey;
   private InventoryKey iEngineInvKey;

   private BigDecimal iEventHoursUsage = new BigDecimal( 400.0 );
   private BigDecimal iEventCycleUsage = new BigDecimal( 350.0 );
   private BigDecimal iEventAssemblyHoursUsage = new BigDecimal( 500.0 );
   private BigDecimal iEventAssemblyCycleUsage = new BigDecimal( 650.0 );

   private Date iEventStartDate;
   private Date iEventEndDate;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * <pre>
    * Test: Test that for a fault against aircraft found after the flight, usage snapshot of inventory is updated
    * Given an Aircraft with a historical flight
    * and the aircraft has a fault with found on date after the flight actual arrival date
    * When I edit the usage Deltas of the historical flight
    * Then the TSN usages of the aircraft in the fault usage snapshot is updated by the delta difference
    * </pre>
    */
   @Test
   public void itUpdatesAircraftFaultTsnWhenFlightUsageEdited() throws Exception {

      FlightLegId lFlightLegId = createFlight();
      FaultKey lFault =
            createFaultOfInventory( iAircraftInvKey, DateUtils.addDays( iFlightDate, +10 ) );

      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( iAircraftInvKey, CYCLES, DELTA_VALUE_THREE ),
                  generateFlightUsage( iAircraftInvKey, HOURS, DELTA_VALUE_THREE ),
                  generateFlightUsage( iEngineInvKey, CYCLES, DELTA_VALUE_THREE ),
                  generateFlightUsage( iEngineInvKey, HOURS, DELTA_VALUE_THREE ) };

      iFlightHistBean.editHistFlight( lFlightLegId, iHrKey, iFlightInfoTO, lEditUsageParms,
            NO_MEASUREMENTS );

      BigDecimal lDeltaDiff = DELTA_VALUE_THREE.subtract( INITIAL_FLIGHT_DELTA );

      assertEventTsn( lFault.getEventKey(), iAircraftInvKey, HOURS,
            iEventHoursUsage.add( lDeltaDiff ) );

      assertEventTsn( lFault.getEventKey(), iAircraftInvKey, CYCLES,
            iEventCycleUsage.add( lDeltaDiff ) );

   }


   /**
    * <pre>
    * Test: Test that for a fault against aircraft found before the flight, usage snapshot of inventory is NOT updated
    * Given an Aircraft with a historical flight
    * and the aircraft has a fault with found on date before the flight actual arrival date
    * When I edit the usage Deltas of the historical flight
    * Then the TSN usages of the aircraft in the fault usage snapshot is NOT updated by the delta difference
    * </pre>
    */
   @Test
   public void itDoesNotUpdatePreviousAircraftFaultTsnWhenFlightUsageEdited() throws Exception {

      FlightLegId lFlightLegId = createFlight();
      FaultKey lFault =
            createFaultOfInventory( iAircraftInvKey, DateUtils.addDays( iFlightDate, -10 ) );

      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( iAircraftInvKey, CYCLES, DELTA_VALUE_THREE ),
                  generateFlightUsage( iAircraftInvKey, HOURS, DELTA_VALUE_THREE ),
                  generateFlightUsage( iEngineInvKey, CYCLES, DELTA_VALUE_THREE ),
                  generateFlightUsage( iEngineInvKey, HOURS, DELTA_VALUE_THREE ) };

      iFlightHistBean.editHistFlight( lFlightLegId, iHrKey, iFlightInfoTO, lEditUsageParms,
            NO_MEASUREMENTS );

      assertEventTsn( lFault.getEventKey(), iAircraftInvKey, HOURS, iEventHoursUsage );

      assertEventTsn( lFault.getEventKey(), iAircraftInvKey, CYCLES, iEventCycleUsage );

   }


   /**
    * <pre>
    * Test: Test that for faults against engine found after the flight, usage snapshot of inventory is updated
    * Given an Aircraft with a historical flight
    * and an Engine on the Aircraft
    * and the Engine has two Faults against it that were FOUND AFTER the Historical Flight actual arrival date,
    * When the usage Deltas of the Historical Flight are edited
    * Then the Engine TSN adjustment of those Faults should be updated by the change to the Delta.
    * </pre>
    */
   @Test
   public void itUpdatesTwoEngineFaultsTsnWhenFlightUsageEdited() throws Exception {

      FlightLegId lFlightLegId = createFlight();
      FaultKey lFault1 =
            createFaultOfInventory( iEngineInvKey, DateUtils.addDays( iFlightDate, +10 ) );
      FaultKey lFault2 =
            createFaultOfInventory( iEngineInvKey, DateUtils.addDays( iFlightDate, +11 ) );

      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( iAircraftInvKey, CYCLES, DELTA_VALUE_THREE ),
                  generateFlightUsage( iAircraftInvKey, HOURS, DELTA_VALUE_THREE ),
                  generateFlightUsage( iEngineInvKey, CYCLES, DELTA_VALUE_THREE ),
                  generateFlightUsage( iEngineInvKey, HOURS, DELTA_VALUE_THREE ) };

      iFlightHistBean.editHistFlight( lFlightLegId, iHrKey, iFlightInfoTO, lEditUsageParms,
            NO_MEASUREMENTS );

      BigDecimal lDeltaDiff = DELTA_VALUE_THREE.subtract( INITIAL_FLIGHT_DELTA );

      assertEventTsn( lFault1.getEventKey(), iEngineInvKey, HOURS,
            iEventHoursUsage.add( lDeltaDiff ) );

      assertEventTsn( lFault1.getEventKey(), iEngineInvKey, CYCLES,
            iEventCycleUsage.add( lDeltaDiff ) );

      assertEventTsn( lFault2.getEventKey(), iEngineInvKey, HOURS,
            iEventHoursUsage.add( lDeltaDiff ) );

      assertEventTsn( lFault2.getEventKey(), iEngineInvKey, CYCLES,
            iEventCycleUsage.add( lDeltaDiff ) );

   }


   /**
    * <pre>
    * Test: Test that for a fault against aircraft found before the flight, usage snapshot of inventory is NOT updated
    * Given an Aircraft with a historical flight
    * and an Engine on the Aircraft
    * and the Engine has a fault against it that were FOUND BEFORE the Historical Flight actual arrival date,
    * When the usage Deltas of the Historical Flight are edited
    * Then the Engine TSN adjustment of those Faults should NOT be updated.
    * </pre>
    */
   @Test
   public void itDoesNotUpdatePreviousEngineFaultTsnWhenFlightUsageEdited() throws Exception {

      FlightLegId lFlightLegId = createFlight();
      FaultKey lFault =
            createFaultOfInventory( iEngineInvKey, DateUtils.addDays( iFlightDate, -10 ) );

      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( iAircraftInvKey, CYCLES, DELTA_VALUE_THREE ),
                  generateFlightUsage( iAircraftInvKey, HOURS, DELTA_VALUE_THREE ),
                  generateFlightUsage( iEngineInvKey, CYCLES, DELTA_VALUE_THREE ),
                  generateFlightUsage( iEngineInvKey, HOURS, DELTA_VALUE_THREE ) };

      iFlightHistBean.editHistFlight( lFlightLegId, iHrKey, iFlightInfoTO, lEditUsageParms,
            NO_MEASUREMENTS );

      assertEventTsn( lFault.getEventKey(), iEngineInvKey, HOURS, iEventHoursUsage );

      assertEventTsn( lFault.getEventKey(), iEngineInvKey, CYCLES, iEventCycleUsage );

   }


   /**
    * <pre>
    * Test: Test that for a fault against an engine removed from an aircraft, the fault found after the flight, usage snapshot of inventory is updated
    * Given an Aircraft with a historical flight
    * and an Engine on the Aircraft during the historical flight
    * And the Engine is removed from the aircraft after the flight and installed to another aircraft.
    * And the Engine/Subassembly has one Faults while it is in the new aircraft.
    * When the usage Deltas of the Historical Flight are edited
    * Then the Engine/Subassembly TSN adjustment of those Faults should be updated by the change to the Delta.
    * </pre>
    */
   @Test
   public void itUpdatesFaultTsnOfEngineReinstalledToAnotherAircraftWhenFlightUsageEdited()
         throws Exception {

      FlightLegId lFlightLegId = createFlight();

      InventoryKey lAnotherAircraftInvKey = Domain.createAircraft();

      removeEngineAndReinstallToAnotherAircraft( lAnotherAircraftInvKey );

      FaultKey lFault =
            createFaultOfInventory( iEngineInvKey, DateUtils.addDays( iFlightDate, +10 ) );

      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( iAircraftInvKey, CYCLES, DELTA_VALUE_THREE ),
                  generateFlightUsage( iAircraftInvKey, HOURS, DELTA_VALUE_THREE ),
                  generateFlightUsage( iEngineInvKey, CYCLES, DELTA_VALUE_THREE ),
                  generateFlightUsage( iEngineInvKey, HOURS, DELTA_VALUE_THREE ) };

      iFlightHistBean.editHistFlight( lFlightLegId, iHrKey, iFlightInfoTO, lEditUsageParms,
            NO_MEASUREMENTS );

      BigDecimal lDeltaDiff = DELTA_VALUE_THREE.subtract( INITIAL_FLIGHT_DELTA );

      // tsn of engine shall be updated
      assertEventTsn( lFault.getEventKey(), iEngineInvKey, HOURS,
            iEventHoursUsage.add( lDeltaDiff ) );

      assertEventTsn( lFault.getEventKey(), iEngineInvKey, CYCLES,
            iEventCycleUsage.add( lDeltaDiff ) );

   }


   @Before
   public void setup() {
      iHrKey = new HumanResourceDomainBuilder().withUsername( HR_USERNAME ).build();

      int lUserId = OrgHr.findByPrimaryKey( iHrKey ).getUserId();
      UserParameters.setInstance( lUserId, "LOGIC", new UserParametersFake( lUserId, "LOGIC" ) );

      iEngineInvKey = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.addUsage( HOURS, INITIAL_HOURS );
            aEngine.addUsage( CYCLES, INITIAL_CYCLES );
         }
      } );

      iAircraftInvKey = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( CYCLES, INITIAL_CYCLES );
            aAircraft.addUsage( HOURS, INITIAL_HOURS );
            aAircraft.addEngine( iEngineInvKey );
         }
      } );

      // create flight infoTo
      StringBuilder lFlightName = new StringBuilder().append( FLIGHT )
            .append( new SimpleDateFormat( "SSSS" ).format( new Date() ) );
      Date lActualDepartureDate = DateUtils.addDays( iFlightDate, -2 );
      Date lActualArrivalDate = DateUtils.addHours( lActualDepartureDate, 2 );

      iFlightInfoTO = generateFlightInfoTO( lFlightName, lActualDepartureDate, lActualArrivalDate );

      iFlightHistBean = new FlightHistBean();
      iFlightHistBean.ejbCreate();
      iFlightHistBean.setSessionContext( new SessionContextFake() );
   }


   @After
   public void teardown() {
      int lUserId = OrgHr.findByPrimaryKey( iHrKey ).getUserId();
      UserParameters.setInstance( lUserId, "LOGIC", null );
   }


   private FlightLegId createFlight( InventoryKey aAircraftInvKey,
         FlightInformationTO aFlightInformationTO, BigDecimal aDelta ) throws Exception {

      CollectedUsageParm[] lFlightUsageParms =
            { generateFlightUsage( iAircraftInvKey, CYCLES, aDelta ),
                  generateFlightUsage( iAircraftInvKey, HOURS, aDelta ),
                  generateFlightUsage( iEngineInvKey, CYCLES, aDelta ),
                  generateFlightUsage( iEngineInvKey, HOURS, aDelta ) };

      return iFlightHistBean.createHistFlight( new AircraftKey( aAircraftInvKey ), iHrKey,
            aFlightInformationTO, lFlightUsageParms, NO_MEASUREMENTS );
   }


   private FlightLegId createFlight() throws Exception {

      FlightLegId lLegId = createFlight( iAircraftInvKey, iFlightInfoTO, INITIAL_FLIGHT_DELTA );

      return lLegId;
   }


   private void removeEngineAndReinstallToAnotherAircraft( InventoryKey aAnotherAircraftInvKey ) {

      Set<UsageSnapshot> lUsages = new HashSet<UsageSnapshot>();
      lUsages.add( new UsageSnapshot( iEngineInvKey, HOURS, iEventHoursUsage.doubleValue(),
            iEventHoursUsage.doubleValue(), iEventHoursUsage.doubleValue(),
            iEventAssemblyHoursUsage.doubleValue(), iEventAssemblyHoursUsage.doubleValue() ) );
      lUsages.add( new UsageSnapshot( iEngineInvKey, CYCLES, iEventCycleUsage.doubleValue(),
            iEventCycleUsage.doubleValue(), iEventCycleUsage.doubleValue(),
            iEventAssemblyCycleUsage.doubleValue(), iEventAssemblyCycleUsage.doubleValue() ) );

      // remove engine from aircraft
      AttachmentService.detachInventoryFromAircraft( iAircraftInvKey, iEngineInvKey,
            DateUtils.addDays( iFlightDate, +5 ), lUsages );

      // reinstall engine to new aircraft
      AttachmentService.attachInventoryToAircraft( aAnotherAircraftInvKey, iEngineInvKey,
            DateUtils.addDays( iFlightDate, +7 ), lUsages );

   }


   private CollectedUsageParm generateFlightUsage( InventoryKey aInventoryKey,
         DataTypeKey lDataType, BigDecimal lDelta ) {

      // Create a usage collection to be returned.
      // CollectedUsageParm lUsageParm =
      new CollectedUsageParm( new UsageParmKey( aInventoryKey, lDataType ), lDelta.doubleValue() );

      CollectedUsageParm lUsageParm = new CollectedUsageParm(
            new UsageParmKey( aInventoryKey, lDataType ), lDelta.doubleValue() );

      // Create flight data source specifications.
      EqpDataSourceSpec
            .create( new UsageDefinitionKey( InvInvTable.getAssemblyByInventoryKey( aInventoryKey ),
                  RefDataSourceKey.MXFL ), lDataType );

      return lUsageParm;
   }


   private FlightInformationTO generateFlightInfoTO( StringBuilder aFlightName,
         Date aActualDepartureDate, Date aActualArrivalDate ) {
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

      return new FlightInformationTO( aFlightName.toString(), null, null, null, null, null,
            lDepartureAirport, lArrivalAirport, null, null, null, null, aActualDepartureDate,
            aActualArrivalDate, null, null, false, false );

   }


   private FaultKey createFaultOfInventory( final InventoryKey aInvKey, final Date aFoundOnDate ) {

      FaultKey lFaultKey = Domain.createFault( new DomainConfiguration<Fault>() {

         @Override
         public void configure( Fault aFault ) {
            aFault.setInventory( aInvKey );
            aFault.setFoundOnDate( aFoundOnDate );
            aFault.addUsageSnapshot( new UsageSnapshot( aInvKey, HOURS,
                  iEventHoursUsage.doubleValue(), iEventHoursUsage.doubleValue(),
                  iEventHoursUsage.doubleValue(), iEventAssemblyHoursUsage.doubleValue(),
                  iEventAssemblyHoursUsage.doubleValue() ) );
            aFault.addUsageSnapshot( new UsageSnapshot( aInvKey, CYCLES,
                  iEventCycleUsage.doubleValue(), iEventCycleUsage.doubleValue(),
                  iEventCycleUsage.doubleValue(), iEventAssemblyCycleUsage.doubleValue(),
                  iEventAssemblyCycleUsage.doubleValue() ) );
         }
      } );

      return lFaultKey;
   }


   private void assertEventTsn( EventKey aEventKey, InventoryKey aInvKey, DataTypeKey aDataTypeKey,
         BigDecimal aExpectedTsnValue ) {

      EvtInvTable lEvtInv = EvtInvTable.findByEventAndInventory( aEventKey, aInvKey );
      EvtInvUsage lEvtInvUsage = EvtInvUsage
            .findByPrimaryKey( new EventInventoryUsageKey( lEvtInv.getPk(), aDataTypeKey ) );

      Assert.assertTrue(
            aExpectedTsnValue.compareTo( new BigDecimal( lEvtInvUsage.getTsnQt() ) ) == 0 );

   }
}
