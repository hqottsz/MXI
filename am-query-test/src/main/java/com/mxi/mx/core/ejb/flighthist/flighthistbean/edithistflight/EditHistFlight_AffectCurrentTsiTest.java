package com.mxi.mx.core.ejb.flighthist.flighthistbean.edithistflight;

import static com.mxi.mx.core.key.DataTypeKey.CYCLES;
import static com.mxi.mx.core.key.DataTypeKey.HOURS;

import java.math.BigDecimal;
import java.util.Arrays;
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
import com.mxi.am.domain.InstallationRecord;
import com.mxi.am.domain.Location;
import com.mxi.am.domain.RemovalRecord;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.servlet.SessionContextFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.ejb.flighthist.CurrentUsages;
import com.mxi.mx.core.ejb.flighthist.FlightHistBean;
import com.mxi.mx.core.flight.dao.JdbcFlightLegDao;
import com.mxi.mx.core.flight.model.FlightLegId;
import com.mxi.mx.core.key.AircraftKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefDataSourceKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.UsageDefinitionKey;
import com.mxi.mx.core.key.UsageParmKey;
import com.mxi.mx.core.services.event.usage.CollectedUsageParm;
import com.mxi.mx.core.services.flighthist.FlightHistService;
import com.mxi.mx.core.services.flighthist.FlightInformationTO;
import com.mxi.mx.core.services.flighthist.Measurement;
import com.mxi.mx.core.table.eqp.EqpDataSourceSpec;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.usage.service.UsageService;
import com.mxi.mx.core.usage.service.UsageUtils;


/**
 * Tests for {@linkplain FlightHistBean#editHistFlight} that involve updating the current TSO of
 * inventory involved in the flight.
 *
 */
public class EditHistFlight_AffectCurrentTsiTest {

   private static final String FLIGHT_NAME = "FLIGHT_NAME";
   private static final Measurement[] NO_MEASUREMENTS = new Measurement[] {};
   private static final int USER_ID = 1234;

   private static final BigDecimal INITIAL_CYCLES = new BigDecimal( 50 );
   private static final BigDecimal INITIAL_HOURS = new BigDecimal( 100 );
   private static final BigDecimal FLIGHT_HOURS_DELTA = new BigDecimal( 5 );
   private static final BigDecimal FLIGHT_CYCLES_DELTA = new BigDecimal( 6 );
   private static final BigDecimal EDITED_FLIGHT_HOURS_DELTA = new BigDecimal( 7 );
   private static final BigDecimal EDITED_FLIGHT_CYCLES_DELTA = new BigDecimal( 8 );

   // Create an installation date far enough in the past to test with dates after it.
   private static final Date ENGINE_INSTALL_DATE = DateUtils.addDays( new Date(), -100 );
   private static final Date DATE_AFTER_ENGINE_INSTALL =
         DateUtils.addDays( ENGINE_INSTALL_DATE, 5 );

   private HumanResourceKey iHrKey;
   private FlightHistService iFlightHistService;

   // The bean under test.
   private FlightHistBean iFlightHistBean;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    *
    * <pre>
    * Given an aircraft collecting usage
    *   And a historical flight against the aircraft
    *  When the usage deltas of the historical flight are edited
    *  Then the current TSI of the aircraft are updated by the delta difference
    * </pre>
    *
    */
   @Test
   public void itAdjustsAircraftCurrentTsiWhenFlightUsagesAreEdited() throws Exception {

      // Given an aircraft collecting usage.
      InventoryKey lAircraftInvKey = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( CYCLES, INITIAL_CYCLES );
            aAircraft.addUsage( HOURS, INITIAL_HOURS );
         }
      } );

      // Given a historical flight against the aircraft.
      Date lFlightDate = DateUtils.addDays( new Date(), -2 );
      FlightInformationTO lFlightInfoTO = generateFlightInfoTO( lFlightDate, FLIGHT_HOURS_DELTA );
      CollectedUsageParm[] lFlightUsages =
            generateFlightUsages( lAircraftInvKey, FLIGHT_CYCLES_DELTA, FLIGHT_HOURS_DELTA );
      FlightLegId lFlightId = iFlightHistService.createHistFlight(
            new AircraftKey( lAircraftInvKey ), iHrKey, lFlightInfoTO,
            new UsageUtils().convertToUsageDeltaArray( lFlightUsages ), NO_MEASUREMENTS );

      // Get the aircraft usage values after the flight.
      CurrentUsages lPostFlightUsages = new CurrentUsages( lAircraftInvKey );

      // When the usage deltas of the historical flight are edited.
      CollectedUsageParm[] lEditedUsageParms =
            { generateFlightUsage( lAircraftInvKey, CYCLES, EDITED_FLIGHT_CYCLES_DELTA ),
                  generateFlightUsage( lAircraftInvKey, HOURS, EDITED_FLIGHT_HOURS_DELTA ) };
      iFlightHistBean.editHistFlight( lFlightId, iHrKey, lFlightInfoTO, lEditedUsageParms,
            NO_MEASUREMENTS );

      // Then the current TSI of the aircraft are updated by the delta difference.
      BigDecimal lCyclesDeltaDiff = EDITED_FLIGHT_CYCLES_DELTA.subtract( FLIGHT_CYCLES_DELTA );
      BigDecimal lHoursDeltaDiff = EDITED_FLIGHT_HOURS_DELTA.subtract( FLIGHT_HOURS_DELTA );
      BigDecimal lExpectedCyclesTsi = lPostFlightUsages.getTsi( CYCLES ).add( lCyclesDeltaDiff );
      BigDecimal lExpectedHoursTsi = lPostFlightUsages.getTsi( HOURS ).add( lHoursDeltaDiff );

      CurrentUsages lCurrentUsages = new CurrentUsages( lAircraftInvKey );

      Assert.assertEquals( "Unexpected aircraft TSI value for CYCLES.", lExpectedCyclesTsi,
            lCurrentUsages.getTsi( CYCLES ) );
      Assert.assertEquals( "Unexpected aircraft TSI value for HOURS.", lExpectedHoursTsi,
            lCurrentUsages.getTsi( HOURS ) );

   }


   /**
    * <pre>
    * Given an aircraft collecting usage
    * And a sub-assembly installed on the aircraft collecting the same usage
    * And a historical flight against the aircraft AFTER install
    * When the usage deltas of the historical flight are edited
    * Then the current TSI of the sub-assembly are updated by the delta difference
    * </pre>
    */
   @Test
   public void itAdjustsSubassyCurrentTsiWhenEditUsagesOfFlightAfterInstall() throws Exception {

      // Given an aircraft collecting usage and a sub-assembly installed on the aircraft collecting
      // the same usage and installed at a known date.
      final InventoryKey lEngineInvKey = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.addUsage( CYCLES, INITIAL_CYCLES );
            aEngine.addUsage( HOURS, INITIAL_HOURS );
            aEngine.addInstallationRecord( new DomainConfiguration<InstallationRecord>() {

               @Override
               public void configure( InstallationRecord aBuilder ) {
                  aBuilder.setInstallationDate( ENGINE_INSTALL_DATE );
               }
            } );
         }
      } );
      final InventoryKey lAircraftInvKey =
            Domain.createAircraft( new DomainConfiguration<Aircraft>() {

               @Override
               public void configure( Aircraft aAircraft ) {
                  aAircraft.addUsage( CYCLES, INITIAL_CYCLES );
                  aAircraft.addUsage( HOURS, INITIAL_HOURS );
                  aAircraft.addEngine( lEngineInvKey );
               }
            } );

      // Given a historical flight against the aircraft AFTER install.
      Date lFlightDate = DATE_AFTER_ENGINE_INSTALL;
      FlightInformationTO lFlightInfoTO = generateFlightInfoTO( lFlightDate, FLIGHT_HOURS_DELTA );
      CollectedUsageParm[] lFlightUsages = generateFlightUsages( lAircraftInvKey, lEngineInvKey,
            FLIGHT_CYCLES_DELTA, FLIGHT_HOURS_DELTA );
      FlightLegId lFlightId = iFlightHistService.createHistFlight(
            new AircraftKey( lAircraftInvKey ), iHrKey, lFlightInfoTO,
            new UsageUtils().convertToUsageDeltaArray( lFlightUsages ), NO_MEASUREMENTS );

      // Get the engine usage values after the flight.
      CurrentUsages lPostFlightUsages = new CurrentUsages( lEngineInvKey );

      // When the usage deltas of the historical flight are edited.
      CollectedUsageParm[] lEditedUsageParms =
            { generateFlightUsage( lAircraftInvKey, CYCLES, EDITED_FLIGHT_CYCLES_DELTA ),
                  generateFlightUsage( lAircraftInvKey, HOURS, EDITED_FLIGHT_HOURS_DELTA ),
                  generateFlightUsage( lEngineInvKey, CYCLES, EDITED_FLIGHT_CYCLES_DELTA ),
                  generateFlightUsage( lEngineInvKey, HOURS, EDITED_FLIGHT_HOURS_DELTA ) };
      iFlightHistBean.editHistFlight( lFlightId, iHrKey, lFlightInfoTO, lEditedUsageParms,
            NO_MEASUREMENTS );

      // Then the current TSI of the sub-assembly are updated by the delta difference.
      BigDecimal lCyclesDeltaDiff = EDITED_FLIGHT_CYCLES_DELTA.subtract( FLIGHT_CYCLES_DELTA );
      BigDecimal lHoursDeltaDiff = EDITED_FLIGHT_HOURS_DELTA.subtract( FLIGHT_HOURS_DELTA );
      BigDecimal lExpectedCyclesTsi = lPostFlightUsages.getTsi( CYCLES ).add( lCyclesDeltaDiff );
      BigDecimal lExpectedHoursTsi = lPostFlightUsages.getTsi( HOURS ).add( lHoursDeltaDiff );

      CurrentUsages lCurrentUsage = new CurrentUsages( lEngineInvKey );

      Assert.assertEquals( "Unexpected aircraft TSI value for CYCLES.", lExpectedCyclesTsi,
            lCurrentUsage.getTsi( CYCLES ) );
      Assert.assertEquals( "Unexpected aircraft TSI value for HOURS.", lExpectedHoursTsi,
            lCurrentUsage.getTsi( HOURS ) );
   }


   /**
    * <pre>
    * Given an aircraft collecting usage
    * And a loose sub-assembly that was previously installed on the aircraft
    * And a historical flight against the aircraft BEFORE the removal
    * When the usage deltas of the historical flight are edited
    * Then the current TSI of the sub-assembly are updated by the delta difference
    * </pre>
    */
   @Test
   public void itAdjustsSubassyCurrentTsiWhenEditUsagesOfFlightBeforeRemoval() throws Exception {

      final Date lEngineInstallDate = ENGINE_INSTALL_DATE;
      final Date lEngineRemovalDate = DateUtils.addDays( lEngineInstallDate, 5 );

      // Given an aircraft collecting usage.
      final InventoryKey lAircraftInvKey =
            Domain.createAircraft( new DomainConfiguration<Aircraft>() {

               @Override
               public void configure( Aircraft aAircraft ) {
                  aAircraft.addUsage( CYCLES, INITIAL_CYCLES );
                  aAircraft.addUsage( HOURS, INITIAL_HOURS );
               }
            } );

      // Given a loose sub-assembly that was previously installed on the aircraft.
      final InventoryKey lEngineInvKey = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.addUsage( CYCLES, INITIAL_CYCLES );
            aEngine.addUsage( HOURS, INITIAL_HOURS );
            aEngine.addInstallationRecord( new DomainConfiguration<InstallationRecord>() {

               @Override
               public void configure( InstallationRecord aInstallRecord ) {
                  aInstallRecord.setInstallationDate( lEngineInstallDate );
                  aInstallRecord.setHighest( lAircraftInvKey );
                  aInstallRecord.setAssembly( lAircraftInvKey );
                  aInstallRecord.setParent( lAircraftInvKey );
               }
            } );
            aEngine.addRemovalRecord( new DomainConfiguration<RemovalRecord>() {

               @Override
               public void configure( RemovalRecord aRemovalRecord ) {
                  aRemovalRecord.setRemovalDate( lEngineRemovalDate );
                  aRemovalRecord.setPosition( new ConfigSlotPositionKey( 0, "ASSY", 0, 0 ) );
                  aRemovalRecord.setHighest( lAircraftInvKey );
                  aRemovalRecord.setAssembly( lAircraftInvKey );
                  aRemovalRecord.setParent( lAircraftInvKey );

               }
            } );
         }
      } );

      // Given a historical flight against the aircraft BEFORE the removal.
      final Date lFlightDate = DateUtils.addDays( lEngineRemovalDate, -1 );
      FlightInformationTO lFlightInfoTO = generateFlightInfoTO( lFlightDate, FLIGHT_HOURS_DELTA );
      CollectedUsageParm[] lFlightUsages = generateFlightUsages( lAircraftInvKey, lEngineInvKey,
            FLIGHT_CYCLES_DELTA, FLIGHT_HOURS_DELTA );
      FlightLegId lFlightId = iFlightHistService.createHistFlight(
            new AircraftKey( lAircraftInvKey ), iHrKey, lFlightInfoTO,
            new UsageUtils().convertToUsageDeltaArray( lFlightUsages ), NO_MEASUREMENTS );

      // Get the engine usage values after the flight.
      CurrentUsages lPostFlightUsages = new CurrentUsages( lEngineInvKey );

      // When the usage deltas of the historical flight are edited.
      CollectedUsageParm[] lEditedUsageParms =
            { generateFlightUsage( lAircraftInvKey, CYCLES, EDITED_FLIGHT_CYCLES_DELTA ),
                  generateFlightUsage( lAircraftInvKey, HOURS, EDITED_FLIGHT_HOURS_DELTA ),
                  generateFlightUsage( lEngineInvKey, CYCLES, EDITED_FLIGHT_CYCLES_DELTA ),
                  generateFlightUsage( lEngineInvKey, HOURS, EDITED_FLIGHT_HOURS_DELTA ) };
      iFlightHistBean.editHistFlight( lFlightId, iHrKey, lFlightInfoTO, lEditedUsageParms,
            NO_MEASUREMENTS );

      // Then the current TSI of the sub-assembly are updated by the delta difference.
      BigDecimal lCyclesDeltaDiff = EDITED_FLIGHT_CYCLES_DELTA.subtract( FLIGHT_CYCLES_DELTA );
      BigDecimal lHoursDeltaDiff = EDITED_FLIGHT_HOURS_DELTA.subtract( FLIGHT_HOURS_DELTA );
      BigDecimal lExpectedCyclesTsi = lPostFlightUsages.getTsi( CYCLES ).add( lCyclesDeltaDiff );
      BigDecimal lExpectedHoursTsi = lPostFlightUsages.getTsi( HOURS ).add( lHoursDeltaDiff );

      CurrentUsages lCurrentUsage = new CurrentUsages( lEngineInvKey );

      Assert.assertEquals( "Unexpected aircraft TSI value for CYCLES.", lExpectedCyclesTsi,
            lCurrentUsage.getTsi( CYCLES ) );
      Assert.assertEquals( "Unexpected aircraft TSI value for HOURS.", lExpectedHoursTsi,
            lCurrentUsage.getTsi( HOURS ) );

   }


   /**
    * <pre>
    * Given an aircraft collecting usage
    * And a reinstalled sub-assembly that was previously removed from the aircraft
    * And a historical flight against the aircraft BEFORE the removal
    * When the usage deltas of the historical flight are edited
    * Then the current TSI of the sub-assembly are not updated
    * </pre>
    */
   @Test
   public void itDoesNotAdjustReinstalledSubassyCurrentTsiWhenEditUsagesOfFlightBeforeRemoval()
         throws Exception {

      final Date lEngineInstallDate = ENGINE_INSTALL_DATE;
      final Date lEngineRemovalDate = DateUtils.addDays( lEngineInstallDate, 5 );
      final Date lEngineReinstallDate = DateUtils.addDays( lEngineRemovalDate, 5 );

      // Given an aircraft collecting usage.
      final InventoryKey lAircraftInvKey =
            Domain.createAircraft( new DomainConfiguration<Aircraft>() {

               @Override
               public void configure( Aircraft aAircraft ) {
                  aAircraft.addUsage( CYCLES, INITIAL_CYCLES );
                  aAircraft.addUsage( HOURS, INITIAL_HOURS );
               }
            } );

      // Given a reinstalled sub-assembly that was previously removed from the aircraft.
      final InventoryKey lEngineInvKey = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.addUsage( CYCLES, INITIAL_CYCLES );
            aEngine.addUsage( HOURS, INITIAL_HOURS );
            aEngine.setParent( lAircraftInvKey );
            aEngine.addInstallationRecord( new DomainConfiguration<InstallationRecord>() {

               @Override
               public void configure( InstallationRecord aInstallRecord ) {
                  aInstallRecord.setInstallationDate( lEngineInstallDate );
                  aInstallRecord.setAssembly( lAircraftInvKey );
                  aInstallRecord.setHighest( lAircraftInvKey );
                  aInstallRecord.setParent( lAircraftInvKey );
               }
            } );
            aEngine.addRemovalRecord( new DomainConfiguration<RemovalRecord>() {

               @Override
               public void configure( RemovalRecord aRemovalRecord ) {
                  aRemovalRecord.setRemovalDate( lEngineRemovalDate );
                  aRemovalRecord.setPosition( new ConfigSlotPositionKey( 0, "ASSY", 0, 0 ) );
                  aRemovalRecord.setAssembly( lAircraftInvKey );
                  aRemovalRecord.setHighest( lAircraftInvKey );
                  aRemovalRecord.setParent( lAircraftInvKey );
               }
            } );
            aEngine.addInstallationRecord( new DomainConfiguration<InstallationRecord>() {

               @Override
               public void configure( InstallationRecord aInstallRecord ) {
                  aInstallRecord.setInstallationDate( lEngineReinstallDate );
                  aInstallRecord.setAssembly( lAircraftInvKey );
                  aInstallRecord.setHighest( lAircraftInvKey );
                  aInstallRecord.setParent( lAircraftInvKey );
               }
            } );
         }
      } );

      // Given a historical flight against the aircraft BEFORE the removal.
      final Date lFlightDate = DateUtils.addDays( lEngineRemovalDate, -1 );
      FlightInformationTO lFlightInfoTO = generateFlightInfoTO( lFlightDate, FLIGHT_HOURS_DELTA );
      CollectedUsageParm[] lFlightUsages = generateFlightUsages( lAircraftInvKey, lEngineInvKey,
            FLIGHT_CYCLES_DELTA, FLIGHT_HOURS_DELTA );
      FlightLegId lFlightId = iFlightHistService.createHistFlight(
            new AircraftKey( lAircraftInvKey ), iHrKey, lFlightInfoTO,
            new UsageUtils().convertToUsageDeltaArray( lFlightUsages ), NO_MEASUREMENTS );

      // Get the engine usage values after the flight.
      CurrentUsages lPostFlightUsages = new CurrentUsages( lEngineInvKey );

      // When the usage deltas of the historical flight are edited.
      CollectedUsageParm[] lEditedUsageParms =
            { generateFlightUsage( lAircraftInvKey, CYCLES, EDITED_FLIGHT_CYCLES_DELTA ),
                  generateFlightUsage( lAircraftInvKey, HOURS, EDITED_FLIGHT_HOURS_DELTA ),
                  generateFlightUsage( lEngineInvKey, CYCLES, EDITED_FLIGHT_CYCLES_DELTA ),
                  generateFlightUsage( lEngineInvKey, HOURS, EDITED_FLIGHT_HOURS_DELTA ) };
      iFlightHistBean.editHistFlight( lFlightId, iHrKey, lFlightInfoTO, lEditedUsageParms,
            NO_MEASUREMENTS );

      // Then the current TSI of the sub-assembly are not updated.
      CurrentUsages lCurrentUsage = new CurrentUsages( lEngineInvKey );

      Assert.assertEquals( "Unexpected aircraft TSI value for CYCLES.",
            lPostFlightUsages.getTsi( CYCLES ), lCurrentUsage.getTsi( CYCLES ) );
      Assert.assertEquals( "Unexpected aircraft TSI value for HOURS.",
            lPostFlightUsages.getTsi( HOURS ), lCurrentUsage.getTsi( HOURS ) );

   }


   @Before
   public void setup() {

      iFlightHistBean = new FlightHistBean();
      iFlightHistBean.ejbCreate();
      iFlightHistBean.setSessionContext( new SessionContextFake() );

      iHrKey = new HumanResourceDomainBuilder().withUserId( USER_ID ).build();
      iFlightHistService = new FlightHistService( new JdbcFlightLegDao(), new UsageService() );

      // iUserLogicParameters = ;
      UserParameters.setInstance( USER_ID, "LOGIC", new UserParametersFake( USER_ID, "LOGIC" ) );

   }


   @After
   public void teardown() {
      UserParameters.setInstance( USER_ID, "LOGIC", null );
   }


   private FlightInformationTO generateFlightInfoTO( Date aFlightDate, BigDecimal aFlightHours ) {

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

      Date lActualDepartureDate = aFlightDate;
      Date lActualArrivalDate = DateUtils.addHours( lActualDepartureDate, aFlightHours.intValue() );

      return new FlightInformationTO( FLIGHT_NAME, null, null, null, null, null, lDepartureAirport,
            lArrivalAirport, null, null, null, null, lActualDepartureDate, lActualArrivalDate, null,
            null, false, false );

   }


   private CollectedUsageParm[] generateFlightUsages( InventoryKey aAircraftInvKey,
         BigDecimal aFlightCycles, BigDecimal aFlightHours ) {
      Set<CollectedUsageParm> lFlightUsages = new HashSet<CollectedUsageParm>();
      lFlightUsages.add( generateFlightUsage( aAircraftInvKey, CYCLES, aFlightCycles ) );
      lFlightUsages.add( generateFlightUsage( aAircraftInvKey, HOURS, aFlightHours ) );

      return lFlightUsages.toArray( new CollectedUsageParm[0] );
   }


   private CollectedUsageParm[] generateFlightUsages( InventoryKey aAircraftInvKey,
         InventoryKey aEngineInvKey, BigDecimal aFlightCycles, BigDecimal aFlightHours ) {
      Set<CollectedUsageParm> lFlightUsages = new HashSet<CollectedUsageParm>(
            Arrays.asList( generateFlightUsages( aAircraftInvKey, aFlightCycles, aFlightHours ) ) );
      lFlightUsages.add( generateFlightUsage( aEngineInvKey, CYCLES, aFlightCycles ) );
      lFlightUsages.add( generateFlightUsage( aEngineInvKey, HOURS, aFlightHours ) );

      return lFlightUsages.toArray( new CollectedUsageParm[0] );
   }


   private CollectedUsageParm generateFlightUsage( InventoryKey aInventoryKey,
         DataTypeKey lDataType, BigDecimal lDelta ) {

      // Create a usage collection to be returned.
      CollectedUsageParm lUsageParm = new CollectedUsageParm(
            new UsageParmKey( aInventoryKey, lDataType ), lDelta.doubleValue() );

      // Create flight data source specifications.
      EqpDataSourceSpec
            .create( new UsageDefinitionKey( InvInvTable.getAssemblyByInventoryKey( aInventoryKey ),
                  RefDataSourceKey.MXFL ), lDataType );

      return lUsageParm;
   }

}
