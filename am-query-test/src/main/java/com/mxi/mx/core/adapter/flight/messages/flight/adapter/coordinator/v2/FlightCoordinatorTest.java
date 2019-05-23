package com.mxi.mx.core.adapter.flight.messages.flight.adapter.coordinator.v2;

import static com.mxi.mx.core.key.DataTypeKey.CYCLES;
import static com.mxi.mx.core.key.DataTypeKey.HOURS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.domain.AccumulatedParameter;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.AircraftAssembly;
import com.mxi.am.domain.ConfigurationSlot;
import com.mxi.am.domain.Deadline;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Engine;
import com.mxi.am.domain.EngineAssembly;
import com.mxi.am.domain.Manufacturer;
import com.mxi.am.domain.Part;
import com.mxi.am.domain.PartGroup;
import com.mxi.am.domain.Requirement;
import com.mxi.am.domain.UsageDefinition;
import com.mxi.am.domain.builder.EventBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.DataSetStatement;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.dataset.SQLStatement;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.adapter.flight.messages.flight.adapter.facade.v2.MxFlightFacade;
import com.mxi.mx.core.adapter.flight.messages.flight.adapter.logic.flightcreator.v2.PlannedFlightCreator;
import com.mxi.mx.core.adapter.flight.tools.CacheRepository;
import com.mxi.mx.core.adapter.flight.tools.DataCache;
import com.mxi.mx.core.flight.model.FlightLegId;
import com.mxi.mx.core.flight.model.FlightLegStatus;
import com.mxi.mx.core.key.AircraftKey;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.ManufacturerKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefEventTypeKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefSchedFromKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.services.flightfl.details.FlightTO;
import com.mxi.mx.core.table.evt.EvtSchedDeadTable;
import com.mxi.mx.core.table.org.OrgHr;
import com.mxi.mx.integration.exceptions.IntegrationException;
import com.mxi.xml.xsd.core.flights.flight.x20.FlightsDocument;
import com.mxi.xml.xsd.core.flights.flight.x20.FlightsDocument.Flights;
import com.mxi.xml.xsd.core.flights.flight.x20.FlightsDocument.Flights.Flight;
import com.mxi.xml.xsd.core.flights.flight.x20.FlightsDocument.Flights.Flight.AircraftIdentifier;
import com.mxi.xml.xsd.core.flights.flight.x20.FlightsDocument.Flights.Flight.AircraftIdentifier.InventoryIdentifier;
import com.mxi.xml.xsd.core.flights.flight.x20.FlightsDocument.Flights.Flight.Airports;
import com.mxi.xml.xsd.core.flights.flight.x20.FlightsDocument.Flights.Flight.Dates;
import com.mxi.xml.xsd.core.flights.flight.x20.FlightsDocument.Flights.Flight.FlightAttributes;
import com.mxi.xml.xsd.core.flights.flight.x20.FlightsDocument.Flights.Flight.FlightAttributes.CapabilityRequirements.CapabilityRequirement;
import com.mxi.xml.xsd.core.flights.flight.x20.FlightsDocument.Flights.Flight.FlightIdentifier;
import com.mxi.xml.xsd.core.flights.flight.x20.FlightsDocument.Flights.Flight.Usages;
import com.mxi.xml.xsd.core.flights.flight.x20.FlightsDocument.Flights.Flight.Usages.Usage;
import com.mxi.xml.xsd.core.flights.flight.x20.FlightsDocument.Flights.Flight.Usages.Usage.InventoryId;
import com.mxi.xml.xsd.core.flights.flight.x20.FlightsDocument.Flights.Flight.Usages.Usage.UsageId;


/**
 * Test class for flight coordinator of Flight API v2
 *
 */
public class FlightCoordinatorTest {

   private static final String FLIGHT_IDENTIFIER = "FLIGHT_17644";
   private static final String FLIGHT_NAME = "FLIGHT_NAME";
   private static final String AIRCRAFT_SERIALNUMBER = "A0017644";
   private static final String ENGINE_SERIALNUMBER = "E0017644";
   private static final String AIRCRAFT_BARCODE = "A0017644";
   private static final String ARRIVAL_AIRPORT = "YOW";
   private static final String DEPARTURE_AIRPORT = "ATL";
   private static final String AIRCRAFT_MANUFACTURER_CD = "MANUFACTURER01";
   private static final String ENGINE_MANUFACTURER_CD = "MANUFACTURER02";
   private static final String CYCLES_USAGE_PARM = "CYCLES";
   private static final String HOURS_USAGE_PARM = "HOURS";
   private static final String USG_USAGE_DATA_TABLE = "USG_USAGE_DATA";
   private static final String LOW_THRUST_RATING_ENGINE = "LOW_THRUST";
   private static final String HIGH_THRUST_RATING_ENGINE = "HIGH_THRUST";
   private static final String FORMER_FLIGHT = "FORMER_FLIGHT";
   private static final String LOW_THRUSTING_CYCLE = "LOW_THRUSTING_CYCLE";
   private static final String HIGH_THRUSTING_CYCLE = "HIGH_THRUSTING_CYCLE";
   private static final String LOW_THRUSTING_HOURS = "LOW_THRUSTING_HOURS";
   private static final String HIGH_THRUSTING_HOURS = "HIGH_THRUSTING_HOURS";
   private static final BigDecimal FLIGHT_HOURS_DELTA = new BigDecimal( 2 );

   private static final BigDecimal FLIGHT_CYCLES_DELTA = new BigDecimal( 1 );

   private static final String AIRCRAFT_CAPABILITY_CD = "ETOPS";
   private static final String AIRCRAFT_CAPABILITY_LEVEL_CD = "ETOPS_90";

   private static final String FLIGHT_EXT_KEY_TO_BE_SWAPPED = "Flight 100";
   private static final String FLIGHT_NAME_TO_BE_SWAPPED = "Flight Name";
   private static final String AIRCRAFT_BARCODE_1 = "Aircraft Barcode 1";
   private static final String AIRCRAFT_BARCODE_2 = "Aircraft Barcode 2";
   private static final String LOC_SYD = "SYD";
   private static final String LOC_BOS = "BOS";
   private static final String LOC_ATL = "ATL";

   private CacheRepository iMockCacheRepository;
   private DataCache iDataTypeCache;
   private HumanResourceKey iFlightHrKey;
   private LocationKey iAirportLocation;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @Before
   public void setup() throws Exception {

      // Set common properties that are required for all tests.
      iFlightHrKey = new HumanResourceKey( "0:3" );
      int lFlightUserId = OrgHr.findByPrimaryKey( iFlightHrKey ).getUserId();
      UserParametersFake lFlightUserParms = new UserParametersFake( lFlightUserId, "LOGIC" );
      lFlightUserParms.setProperty( "MAX_FLIGHT_DURATION_VALUE", null );
      lFlightUserParms.setProperty( "MAX_FLIGHT_DAYS_IN_THE_FUTURE", "101" );
      UserParameters.setInstance( lFlightUserId, "LOGIC", lFlightUserParms );

      // Setup Mocks for Cache used in DataTypeFinder
      iMockCacheRepository = mock( CacheRepository.class );
      iDataTypeCache = mock( DataCache.class );
      CacheRepository.setInstance( iMockCacheRepository );
      when( iMockCacheRepository.getCache( ( String ) notNull() ) ).thenReturn( iDataTypeCache );
      when( iDataTypeCache.getString( HOURS_USAGE_PARM ) )
            .thenReturn( DataTypeKey.HOURS.toValueString() );
      when( iDataTypeCache.getString( CYCLES_USAGE_PARM ) )
            .thenReturn( DataTypeKey.CYCLES.toValueString() );
   }


   @After
   public void teardown() {
      CacheRepository.setInstance( null );
      int lUserId = OrgHr.findByPrimaryKey( iFlightHrKey ).getUserId();
      UserParameters.setInstance( lUserId, "LOGIC", null );
   }


   /**
    * Swap flight from one aircraft to another aircraft
    */
   @Test
   public void itAircraftSwap() throws Exception {

      // DATA SETUP: The departure and arrival date time
      final Date lDepartureDate = new Date();

      final Date lArrivalDate = DateUtils.addHours( lDepartureDate, 2 );

      // DATA SETUP: Create airport location
      LocationKey lLocation_SYD = createAirportLocation( LOC_SYD );

      LocationKey lLocation_BOS = createAirportLocation( LOC_BOS );

      LocationKey lLocation_ATL = createAirportLocation( LOC_ATL );

      // DATA SETUP: Create aircraft
      final InventoryKey lAircraft1 = createAircraft( AIRCRAFT_BARCODE_1 );

      // DATA SETUP: Create 2 historic flights on aircraft1
      createHistoricFlight( lAircraft1, DateUtils.addDays( lDepartureDate, -4 ),
            DateUtils.addDays( lArrivalDate, -4 ) );

      FlightLegId aircraft1_latestCompleteFlight = createHistoricFlight( lAircraft1,
            DateUtils.addDays( lDepartureDate, -2 ), DateUtils.addDays( lArrivalDate, -2 ) );

      // DATA SETUP: Create plan flights on aircraft1
      FlightLegId lAircraft1_firstPlanFlight =
            createPlanFlight( "Flight Name 1", "Flight External Key 1", lLocation_SYD,
                  lLocation_BOS, lAircraft1, lDepartureDate, lArrivalDate );

      FlightLegId lAircraft1_secondPlanFlight = createPlanFlight( "Flight Name 2",
            "Flight External Key 2", lLocation_BOS, lLocation_ATL, lAircraft1,
            DateUtils.addDays( lDepartureDate, 1 ), DateUtils.addDays( lArrivalDate, 1 ) );

      // Asserts the sequence of the flight plan on aircraft1
      assertFlightPlanOnAircraft( lAircraft1, aircraft1_latestCompleteFlight,
            lAircraft1_firstPlanFlight, lAircraft1_secondPlanFlight );

      // DATA SETUP: Create a second aircraft
      final InventoryKey lAircraft2 = createAircraft( AIRCRAFT_BARCODE_2 );

      // DATA SETUP: Create the first plan flight on aircraft2
      Date lSwappedFlightDepartureDate = DateUtils.addHours( lDepartureDate, 5 );
      Date lSwappedFlightArrivalDate = DateUtils.addHours( lArrivalDate, 5 );

      FlightLegId lAircraft2_swappedPlanFlight = createPlanFlight( FLIGHT_NAME_TO_BE_SWAPPED,
            FLIGHT_EXT_KEY_TO_BE_SWAPPED, lLocation_BOS, lLocation_ATL, lAircraft2,
            lSwappedFlightDepartureDate, lSwappedFlightArrivalDate );

      // Asserts the sequence of the flight plan on aircraft2
      assertFlightPlanOnAircraft( lAircraft2, null, lAircraft2_swappedPlanFlight );

      // *******************************************************
      // Swap plan flight from aircraft2 to aircraft1
      // *******************************************************

      // Build Flight Document
      FlightsDocument lFlightsDocument = FlightsDocument.Factory.newInstance();
      Flights lFlights = lFlightsDocument.addNewFlights();
      Flight lFlight = lFlights.addNewFlight();

      // Flight element
      lFlight.setMarkInError( false );

      // Flight Identifier
      FlightIdentifier lFlightIdentifier = lFlight.addNewFlightIdentifier();
      lFlightIdentifier.setExternalKey( FLIGHT_EXT_KEY_TO_BE_SWAPPED );

      // Flight Attributes
      FlightAttributes lFlightAttributes = lFlight.addNewFlightAttributes();
      lFlightAttributes.setName( FLIGHT_NAME_TO_BE_SWAPPED );

      // Aircraft Identifier
      AircraftIdentifier lAircraftIdentifier = lFlight.addNewAircraftIdentifier();
      lAircraftIdentifier.setBarcode( AIRCRAFT_BARCODE_1 );

      // Airports
      Airports lAirports = lFlight.addNewAirports();
      lAirports.setArrivalAirport( LOC_BOS );
      lAirports.setDepartureAirport( LOC_ATL );

      // Dates
      Dates lDates = lFlight.addNewDates();
      lDates.setScheduledDepartureDate( lSwappedFlightDepartureDate.toString() );
      lDates.setScheduledArrivalDate( lSwappedFlightArrivalDate.toString() );

      // Send the swapping flight message
      FlightCoordinator lFlightCoordinator =
            new FlightCoordinator( new PlannedFlightCreator( new MxFlightFacade() ) );

      lFlightCoordinator.coordinate( lFlightsDocument );

      // Asserts the flight plan has been swapped out of aircraft2
      QuerySet lQs = getFlightPlan( lAircraft2 );

      assertEquals( 1, lQs.getRowCount() );

      // A empty flight plan is created at the current aircraft2's location
      lQs.first();

      assertEquals( iAirportLocation, lQs.getKey( LocationKey.class, "loc_db_id", "loc_id" ) );
      assertTrue( lQs.isNull( "dep_leg_id" ) );
      assertTrue( lQs.isNull( "arr_leg_id" ) );

      // Asserts the sequence of the flight plan on aircraft1 after swapping a flight from aircraft2
      assertFlightPlanOnAircraft( lAircraft1, aircraft1_latestCompleteFlight,
            lAircraft1_firstPlanFlight, lAircraft2_swappedPlanFlight, lAircraft1_secondPlanFlight );
   }


   /**
    * Given an aircraft collecting usages,<br />
    * when a historical flight is created using Flight API v2,<br />
    * then a flight usage record of the aircraft is created.
    */
   @Test
   public void itCreatesUsageRecordForFlightAgainstAircraft() throws Exception {
      Date lArrivalDate = new Date();
      Date lDepartureDate = DateUtils.addHours( lArrivalDate, -2 );

      // Required by FlightHistService (getAssemblyUsageDataSet())
      final AssemblyKey lAircraftAssembly = createAircraftAssembly();
      final PartNoKey lAircraftPart = createAircraftPart();

      // Required by Flight Document
      createAirportLocation( ARRIVAL_AIRPORT );
      createAirportLocation( DEPARTURE_AIRPORT );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            // Mandatory properties with respect to the Flight API
            aAircraft.setAssembly( lAircraftAssembly );
            aAircraft.setBarcode( AIRCRAFT_BARCODE );
            aAircraft.setSerialNumber( AIRCRAFT_SERIALNUMBER );
            aAircraft.setPart( lAircraftPart );
            aAircraft.addUsage( HOURS, BigDecimal.TEN );
            aAircraft.addUsage( CYCLES, BigDecimal.TEN );
         }
      } );

      // Build Flight Document
      FlightsDocument lFlightsDocument = FlightsDocument.Factory.newInstance();
      Flights lFlights = lFlightsDocument.addNewFlights();
      Flight lFlight = lFlights.addNewFlight();

      // Flight element
      lFlight.setMarkInError( false );

      // Flight Identifier
      FlightIdentifier lFlightIdentifier = lFlight.addNewFlightIdentifier();
      lFlightIdentifier.setExternalKey( FLIGHT_IDENTIFIER );

      // Flight Attributes
      FlightAttributes lFlightAttributes = lFlight.addNewFlightAttributes();
      lFlightAttributes.setName( FLIGHT_NAME );

      // Aircraft Identifier
      AircraftIdentifier lAircraftIdentifier = lFlight.addNewAircraftIdentifier();
      lAircraftIdentifier.setBarcode( AIRCRAFT_BARCODE );

      // Airports
      Airports lAirports = lFlight.addNewAirports();
      lAirports.setArrivalAirport( ARRIVAL_AIRPORT );
      lAirports.setDepartureAirport( DEPARTURE_AIRPORT );

      // Dates
      Dates lDates = lFlight.addNewDates();
      lDates.setScheduledArrivalDate( lArrivalDate.toString() );
      lDates.setActualArrivalDate( lArrivalDate.toString() );
      lDates.setScheduledDepartureDate( lDepartureDate.toString() );
      lDates.setActualDepartureDate( lDepartureDate.toString() );

      // When
      FlightCoordinator lFlightCoordinator =
            new FlightCoordinator( new PlannedFlightCreator( new MxFlightFacade() ) );
      try {
         lFlightCoordinator.coordinate( lFlightsDocument );

      } catch ( IntegrationException aException ) {
         aException.printStackTrace();
         fail( "Unexpected exception occurred during flight creation" );
      }

      // Then
      Map<DataTypeKey, BigDecimal> lAircraftUsageDeltaMap = readUsageDeltas( lAircraft );

      assertEquals( "Unexpectedly,Aircraft didnt accrue flight cycles", FLIGHT_CYCLES_DELTA,
            lAircraftUsageDeltaMap.get( CYCLES ) );
      assertEquals( "Unexpectedly,Aircraft didnt accrue flight hours", FLIGHT_HOURS_DELTA,
            lAircraftUsageDeltaMap.get( HOURS ) );
   }


   /**
    * Given an aircraft collecting usages with the following configuration:<br />
    * has many aggregated usage parameters on the engine assembly, <br />
    * each aggregated usage parameter has multiple part specific usage parameters<br />
    * the engine has multiple part numbers each corresponding to a different thrust rating each
    * engine part number has an associated part specific usage parameter <br />
    * the engine part number has been changed from a Low-Thrust Rating part number to a High-Thrust
    * Rating part number<br />
    * When a historical flight is created using Flight API v2 after the engine thrust rating was
    * changed. <br />
    * Then a flight usage record of the engine is created having a part specific usage parameter
    * corresponding to the current engine part number (High-Thrust Rating).
    */
   @Test
   public void itCreatesUsageRecordForFlightWithAccumulatedParmBasedOnCurrentPartNumber()
         throws Exception {
      Date lArrivalDate = new Date();
      Date lDepartureDate = DateUtils.addHours( lArrivalDate, -2 );
      Date lPartNumberChangeDate = DateUtils.addDays( lDepartureDate, -1 );

      final PartNoKey lLowThrustRatingPartNo =
            createEnginePartNoWithThrustRating( LOW_THRUST_RATING_ENGINE, null );
      final PartNoKey lHighThrustRatingPartNo =
            createEnginePartNoWithThrustRating( HIGH_THRUST_RATING_ENGINE, null );

      final AssemblyKey lEngineAssembly = createEngineAssemblyWithAccumulatedParameters(
            lLowThrustRatingPartNo, lHighThrustRatingPartNo );

      // Required by FlightHistService (getAssemblyUsageDataSet())
      final AssemblyKey lAircraftAssembly = createAircraftAssembly();
      final PartNoKey lAircraftPart = createAircraftPart();

      final InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.setAssembly( lEngineAssembly );
            aEngine.setPartNumber( lHighThrustRatingPartNo );
            aEngine.addUsage( HOURS, BigDecimal.TEN );
            aEngine.addUsage( CYCLES, BigDecimal.TEN );
         }
      } );

      // Required by Flight Document
      createAirportLocation( ARRIVAL_AIRPORT );
      createAirportLocation( DEPARTURE_AIRPORT );

      Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            // Mandatory properties with respect to the Flight API
            aAircraft.setAssembly( lAircraftAssembly );
            aAircraft.addEngine( lEngine );
            aAircraft.setBarcode( AIRCRAFT_BARCODE );
            aAircraft.setSerialNumber( AIRCRAFT_SERIALNUMBER );
            aAircraft.setPart( lAircraftPart );
            aAircraft.addUsage( HOURS, BigDecimal.TEN );
            aAircraft.addUsage( CYCLES, BigDecimal.TEN );
         }
      } );

      DataTypeKey lLowThrustRatingCycle = Domain.readUsageParameter(
            Domain.readRootConfigurationSlot( lEngineAssembly ), LOW_THRUSTING_CYCLE );

      DataTypeKey lHighThrustRatingCycle = Domain.readUsageParameter(
            Domain.readRootConfigurationSlot( lEngineAssembly ), HIGH_THRUSTING_CYCLE );

      DataTypeKey lLowThrustRatingHours = Domain.readUsageParameter(
            Domain.readRootConfigurationSlot( lEngineAssembly ), LOW_THRUSTING_HOURS );

      DataTypeKey lHighThrustRatingHours = Domain.readUsageParameter(
            Domain.readRootConfigurationSlot( lEngineAssembly ), HIGH_THRUSTING_HOURS );

      createRequirementWithPartNumberChange( lEngine, lLowThrustRatingPartNo,
            lPartNumberChangeDate );

      // Build Flight Document
      FlightsDocument lFlightsDocument = FlightsDocument.Factory.newInstance();
      Flights lFlights = lFlightsDocument.addNewFlights();
      Flight lFlight = lFlights.addNewFlight();

      // Flight element
      lFlight.setMarkInError( false );

      // Flight Identifier
      FlightIdentifier lFlightIdentifier = lFlight.addNewFlightIdentifier();
      lFlightIdentifier.setExternalKey( FLIGHT_IDENTIFIER );

      // Flight Attributes
      FlightAttributes lFlightAttributes = lFlight.addNewFlightAttributes();
      lFlightAttributes.setName( FLIGHT_NAME );

      // Aircraft Identifier
      AircraftIdentifier lAircraftIdentifier = lFlight.addNewAircraftIdentifier();
      lAircraftIdentifier.setBarcode( AIRCRAFT_BARCODE );

      // Airports
      Airports lAirports = lFlight.addNewAirports();
      lAirports.setArrivalAirport( ARRIVAL_AIRPORT );
      lAirports.setDepartureAirport( DEPARTURE_AIRPORT );

      // Dates
      Dates lDates = lFlight.addNewDates();
      lDates.setScheduledArrivalDate( lArrivalDate.toString() );
      lDates.setActualArrivalDate( lArrivalDate.toString() );
      lDates.setScheduledDepartureDate( lDepartureDate.toString() );
      lDates.setActualDepartureDate( lDepartureDate.toString() );

      // When
      FlightCoordinator lFlightCoordinator =
            new FlightCoordinator( new PlannedFlightCreator( new MxFlightFacade() ) );
      try {
         lFlightCoordinator.coordinate( lFlightsDocument );

      } catch ( IntegrationException aException ) {
         aException.printStackTrace();
         fail( "Unexpected exception occurred during flight creation" );
      }

      // Then

      Map<DataTypeKey, BigDecimal> lEngineUsageDeltaMap = readUsageDeltas( lEngine );
      assertEquals( "Unexpectedly,Engine didnt accrue flight cycles", FLIGHT_CYCLES_DELTA,
            lEngineUsageDeltaMap.get( CYCLES ) );
      assertEquals( "High Thrust Rating Part Number Based Engine should accrue flight cycles",
            FLIGHT_CYCLES_DELTA, lEngineUsageDeltaMap.get( lHighThrustRatingCycle ) );
      assertNull( "Low Thrust Rating Part Number Based Engine should not accrue flight cycles",
            lEngineUsageDeltaMap.get( lLowThrustRatingCycle ) );
      assertEquals( "Unexpectedly,Engine didnt accrue flight hours", FLIGHT_HOURS_DELTA,
            lEngineUsageDeltaMap.get( HOURS ) );
      assertEquals( "High Thrust Rating Part Number Based Engine should accrue flight hours",
            FLIGHT_HOURS_DELTA, lEngineUsageDeltaMap.get( lHighThrustRatingHours ) );
      assertNull( "Low Thrust Rating Part Number Based Engine should not accrue flight hours",
            lEngineUsageDeltaMap.get( lLowThrustRatingHours ) );
   }


   /**
    * Given an aircraft collecting usages with the following configuration:<br />
    * has many aggregated usage parameters on the engine assembly, <br />
    * each aggregated usage parameter has multiple accumulated parameters,<br />
    * the engine has multiple part numbers each corresponding to a different thrust rating each
    * engine part number has an associated accumulated parameter, <br />
    * the engine part number has been changed from a Low-Thrust Rating part number to a High-Thrust
    * Rating part number,<br />
    * the aircraft has a historical flight that occurred after the part number change<br />
    * When another historical flight is created prior in time to the part number change using Flight
    * API v2. <br />
    * Then a flight usage record of the engine is created having accumulated parameter corresponding
    * to the Low-Thrust Rating.
    */
   @Test
   public void
         itCreatesUsageRecordForOOSFlightWithAccumulatedParmBasedOnPartNumberBeforePartNumberChange()
               throws Exception {
      final Date lFlightArrivalDate = new Date();
      final Date lFlightDepartureDate = DateUtils.addHours( lFlightArrivalDate, -2 );
      Date lPartNumberChangeDate = DateUtils.addDays( lFlightDepartureDate, -1 );
      Date lAnotherFlightArrivalDate = DateUtils.addDays( lPartNumberChangeDate, -2 );
      Date lAnotherFlightDepartureDate = DateUtils.addHours( lAnotherFlightArrivalDate, -2 );

      final PartNoKey lLowThrustRatingPartNo =
            createEnginePartNoWithThrustRating( LOW_THRUST_RATING_ENGINE, null );
      final PartNoKey lHighThrustRatingPartNo =
            createEnginePartNoWithThrustRating( HIGH_THRUST_RATING_ENGINE, null );

      final AssemblyKey lEngineAssembly = createEngineAssemblyWithAccumulatedParameters(
            lLowThrustRatingPartNo, lHighThrustRatingPartNo );

      // Required by FlightHistService (getAssemblyUsageDataSet())
      final AssemblyKey lAircraftAssembly = createAircraftAssembly();
      final PartNoKey lAircraftPart = createAircraftPart();

      final InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.setAssembly( lEngineAssembly );
            aEngine.setPartNumber( lHighThrustRatingPartNo );
            aEngine.addUsage( HOURS, BigDecimal.TEN );
            aEngine.addUsage( CYCLES, BigDecimal.TEN );
         }
      } );

      // Required by Flight Document
      createAirportLocation( ARRIVAL_AIRPORT );
      createAirportLocation( DEPARTURE_AIRPORT );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            // Mandatory properties with respect to the Flight API
            aAircraft.setAssembly( lAircraftAssembly );
            aAircraft.addEngine( lEngine );
            aAircraft.setBarcode( AIRCRAFT_BARCODE );
            aAircraft.setSerialNumber( AIRCRAFT_SERIALNUMBER );
            aAircraft.setPart( lAircraftPart );
            aAircraft.addUsage( HOURS, BigDecimal.TEN );
            aAircraft.addUsage( CYCLES, BigDecimal.TEN );
         }
      } );

      Domain.createFlight( new DomainConfiguration<com.mxi.am.domain.Flight>() {

         @Override
         public void configure( com.mxi.am.domain.Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.isHistorical();
            aFlight.setDepartureDate( lFlightDepartureDate );
            aFlight.setArrivalDate( lFlightArrivalDate );
         }
      } );

      DataTypeKey lLowThrustRatingCycle = Domain.readUsageParameter(
            Domain.readRootConfigurationSlot( lEngineAssembly ), LOW_THRUSTING_CYCLE );
      DataTypeKey lHighThrustRatingCycle = Domain.readUsageParameter(
            Domain.readRootConfigurationSlot( lEngineAssembly ), HIGH_THRUSTING_CYCLE );
      DataTypeKey lLowThrustRatingHours = Domain.readUsageParameter(
            Domain.readRootConfigurationSlot( lEngineAssembly ), LOW_THRUSTING_HOURS );
      DataTypeKey lHighThrustRatingHours = Domain.readUsageParameter(
            Domain.readRootConfigurationSlot( lEngineAssembly ), HIGH_THRUSTING_HOURS );

      createRequirementWithPartNumberChange( lEngine, lLowThrustRatingPartNo,
            lPartNumberChangeDate );

      // Build Flight Document
      FlightsDocument lFlightsDocument = FlightsDocument.Factory.newInstance();
      Flights lFlights = lFlightsDocument.addNewFlights();
      Flight lFlight = lFlights.addNewFlight();

      // Flight element
      lFlight.setMarkInError( false );

      // Flight Identifier
      FlightIdentifier lFlightIdentifier = lFlight.addNewFlightIdentifier();
      lFlightIdentifier.setExternalKey( FLIGHT_IDENTIFIER );

      // Flight Attributes
      FlightAttributes lFlightAttributes = lFlight.addNewFlightAttributes();
      lFlightAttributes.setName( FLIGHT_NAME );

      // Aircraft Identifier
      AircraftIdentifier lAircraftIdentifier = lFlight.addNewAircraftIdentifier();
      lAircraftIdentifier.setBarcode( AIRCRAFT_BARCODE );

      // Airports
      Airports lAirports = lFlight.addNewAirports();
      lAirports.setArrivalAirport( ARRIVAL_AIRPORT );
      lAirports.setDepartureAirport( DEPARTURE_AIRPORT );

      // Dates
      Dates lDates = lFlight.addNewDates();
      lDates.setScheduledArrivalDate( lAnotherFlightArrivalDate.toString() );
      lDates.setActualArrivalDate( lAnotherFlightArrivalDate.toString() );
      lDates.setScheduledDepartureDate( lAnotherFlightDepartureDate.toString() );
      lDates.setActualDepartureDate( lAnotherFlightDepartureDate.toString() );

      // When
      FlightCoordinator lFlightCoordinator =
            new FlightCoordinator( new PlannedFlightCreator( new MxFlightFacade() ) );
      try {
         lFlightCoordinator.coordinate( lFlightsDocument );

      } catch ( IntegrationException aException ) {
         aException.printStackTrace();
         fail( "Unexpected exception occurred during flight creation" );
      }

      // Then

      Map<DataTypeKey, BigDecimal> lEngineUsageDeltaMap = readUsageDeltas( lEngine );

      assertEquals( "Unexpectedly,Engine didnt accrue flight cycles", FLIGHT_CYCLES_DELTA,
            lEngineUsageDeltaMap.get( CYCLES ) );
      assertEquals( "Low Thrust Rating Part Number Based Engine should accrue flight cycles",
            FLIGHT_CYCLES_DELTA, lEngineUsageDeltaMap.get( lLowThrustRatingCycle ) );
      assertNull( "High Thrust Rating Part Number Based Engine should not accrue flight cycles",
            lEngineUsageDeltaMap.get( lHighThrustRatingCycle ) );
      assertEquals( "Unexpectedly,Engine didnt accrue flight hours", FLIGHT_HOURS_DELTA,
            lEngineUsageDeltaMap.get( HOURS ) );
      assertEquals( "Low Thrust Rating Part Number Based Engine should accrue flight hours",
            FLIGHT_HOURS_DELTA, lEngineUsageDeltaMap.get( lLowThrustRatingHours ) );
      assertNull( "High Thrust Rating Part Number Based Engine should not accrue flight hours",
            lEngineUsageDeltaMap.get( lHighThrustRatingHours ) );
   }


   /**
    * <pre>
    * Given an aircraft collecting usages with the following configuration:
    * Has many aggregated usage parameters on the engine assembly,
    * Each aggregated usage parameter has multiple accumulated parameters,
    * The engine assembly has multiple part numbers each corresponding to a different thrust rating
    * Each engine part number has an associated accumulated parameter,
    * The engine part number has been changed from a Low-Thrust Rating part number to a High-Thrust Rating part number,
    * The aircraft has a historical flight that occurred after the part number change
    * When another historical flight is created prior in time to the part number change using Flight
    * API v2 with aggregated usage specified for the engine.
    * Then the flight usage record is created for the engine containing the usage value against the accumulated parameter
    * corresponding to low thrust rating.
    * </pre>
    */
   @Test
   public void
         itCreatesOOSFlightWithAccumulatedParmBeforeEnginePartNumberChangeWithSpecifiedEngineUsage()
               throws Exception {
      final Date lFlightArrivalDate = new Date();
      final Date lFlightDepartureDate = DateUtils.addHours( lFlightArrivalDate, -2 );
      Date lPartNumberChangeDate = DateUtils.addDays( lFlightDepartureDate, -1 );
      Date lAnotherFlightArrivalDate = DateUtils.addDays( lPartNumberChangeDate, -2 );
      Date lAnotherFlightDepartureDate = DateUtils.addHours( lAnotherFlightArrivalDate, -2 );

      // Required by InventoryFinder getInventoryKey()
      final ManufacturerKey lEngineManufacturer = createManufacturer( ENGINE_MANUFACTURER_CD );

      final PartNoKey lLowThrustRatingPartNo =
            createEnginePartNoWithThrustRating( LOW_THRUST_RATING_ENGINE, lEngineManufacturer );
      final PartNoKey lHighThrustRatingPartNo =
            createEnginePartNoWithThrustRating( HIGH_THRUST_RATING_ENGINE, lEngineManufacturer );

      final AssemblyKey lEngineAssembly = createEngineAssemblyWithAccumulatedParameters(
            lLowThrustRatingPartNo, lHighThrustRatingPartNo );

      // Required by FlightHistService (getAssemblyUsageDataSet())
      final AssemblyKey lAircraftAssembly = createAircraftAssembly();
      final PartNoKey lAircraftPart = createAircraftPart();

      final InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.setAssembly( lEngineAssembly );
            aEngine.setSerialNumber( ENGINE_SERIALNUMBER );
            aEngine.setPartNumber( lHighThrustRatingPartNo );
            aEngine.addUsage( HOURS, BigDecimal.TEN );
            aEngine.addUsage( CYCLES, BigDecimal.TEN );
         }
      } );

      // Required by Flight Document
      createAirportLocation( ARRIVAL_AIRPORT );
      createAirportLocation( DEPARTURE_AIRPORT );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            // Mandatory properties with respect to the Flight API
            aAircraft.setAssembly( lAircraftAssembly );
            aAircraft.addEngine( lEngine );
            aAircraft.setBarcode( AIRCRAFT_BARCODE );
            aAircraft.setSerialNumber( AIRCRAFT_SERIALNUMBER );
            aAircraft.setPart( lAircraftPart );
            aAircraft.addUsage( HOURS, BigDecimal.TEN );
            aAircraft.addUsage( CYCLES, BigDecimal.TEN );
         }
      } );

      Domain.createFlight( new DomainConfiguration<com.mxi.am.domain.Flight>() {

         @Override
         public void configure( com.mxi.am.domain.Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.isHistorical();
            aFlight.setDepartureDate( lFlightDepartureDate );
            aFlight.setArrivalDate( lFlightArrivalDate );
         }

      } );

      DataTypeKey lLowThrustRatingCycle = Domain.readUsageParameter(
            Domain.readRootConfigurationSlot( lEngineAssembly ), LOW_THRUSTING_CYCLE );
      DataTypeKey lHighThrustRatingCycle = Domain.readUsageParameter(
            Domain.readRootConfigurationSlot( lEngineAssembly ), HIGH_THRUSTING_CYCLE );
      DataTypeKey lLowThrustRatingHours = Domain.readUsageParameter(
            Domain.readRootConfigurationSlot( lEngineAssembly ), LOW_THRUSTING_HOURS );
      DataTypeKey lHighThrustRatingHours = Domain.readUsageParameter(
            Domain.readRootConfigurationSlot( lEngineAssembly ), HIGH_THRUSTING_HOURS );

      createRequirementWithPartNumberChange( lEngine, lLowThrustRatingPartNo,
            lPartNumberChangeDate );

      // Build Flight Document
      FlightsDocument lFlightsDocument = FlightsDocument.Factory.newInstance();
      Flights lFlights = lFlightsDocument.addNewFlights();
      Flight lFlight = lFlights.addNewFlight();

      // Flight element
      lFlight.setMarkInError( false );

      // Flight Identifier
      FlightIdentifier lFlightIdentifier = lFlight.addNewFlightIdentifier();
      lFlightIdentifier.setExternalKey( FLIGHT_IDENTIFIER );

      // Flight Attributes
      FlightAttributes lFlightAttributes = lFlight.addNewFlightAttributes();
      lFlightAttributes.setName( FLIGHT_NAME );

      // Aircraft Identifier
      AircraftIdentifier lAircraftIdentifier = lFlight.addNewAircraftIdentifier();
      lAircraftIdentifier.setBarcode( AIRCRAFT_BARCODE );

      // Usages
      BigDecimal lCyclesFlightDelta = new BigDecimal( 2 );
      BigDecimal lHoursFlightDelta = new BigDecimal( 5 );
      Usages lUsages = lFlight.addNewUsages();
      Usage lUsage = lUsages.addNewUsage();
      InventoryId lEngineInventoryId = lUsage.addNewInventoryId();
      lEngineInventoryId.setSerialNumber( ENGINE_SERIALNUMBER );
      UsageId lCyclesUsageId = lUsage.addNewUsageId();
      lCyclesUsageId.setUsageParm( CYCLES_USAGE_PARM );
      lCyclesUsageId.setDelta( lCyclesFlightDelta.doubleValue() );
      UsageId lHoursUsageId = lUsage.addNewUsageId();
      lHoursUsageId.setUsageParm( HOURS_USAGE_PARM );
      lHoursUsageId.setDelta( lHoursFlightDelta.doubleValue() );

      // Airports
      Airports lAirports = lFlight.addNewAirports();
      lAirports.setArrivalAirport( ARRIVAL_AIRPORT );
      lAirports.setDepartureAirport( DEPARTURE_AIRPORT );

      // Dates
      Dates lDates = lFlight.addNewDates();
      lDates.setScheduledArrivalDate( lAnotherFlightArrivalDate.toString() );
      lDates.setActualArrivalDate( lAnotherFlightArrivalDate.toString() );
      lDates.setScheduledDepartureDate( lAnotherFlightDepartureDate.toString() );
      lDates.setActualDepartureDate( lAnotherFlightDepartureDate.toString() );

      // When
      FlightCoordinator lFlightCoordinator =
            new FlightCoordinator( new PlannedFlightCreator( new MxFlightFacade() ) );
      try {
         lFlightCoordinator.coordinate( lFlightsDocument );

      } catch ( IntegrationException aException ) {
         aException.printStackTrace();
         fail( aException.getMessage() );
      }

      // Then
      Map<DataTypeKey, BigDecimal> lEngineUsageDeltaMap = readUsageDeltas( lEngine );
      assertEquals(
            "Unexpectedly,Engine didnt accrue flight usage cycles specified in the flight document",
            lCyclesFlightDelta, lEngineUsageDeltaMap.get( CYCLES ) );
      assertEquals(
            "Low Thrust Rating Part Number Based Engine should accrue flight usage cycles specified in the flight document",
            lCyclesFlightDelta, lEngineUsageDeltaMap.get( lLowThrustRatingCycle ) );
      assertNull( "High Thrust Rating Part Number Based Engine should not accrue flight cycles",
            lEngineUsageDeltaMap.get( lHighThrustRatingCycle ) );
      assertEquals(
            "Unexpectedly,Engine didnt accrue flight usage hours specified in the flight document",
            lHoursFlightDelta, lEngineUsageDeltaMap.get( HOURS ) );
      assertEquals(
            "Low Thrust Rating Part Number Based Engine should accrue flight usage hours specified in the flight document",
            lHoursFlightDelta, lEngineUsageDeltaMap.get( lLowThrustRatingHours ) );
      assertNull( "High Thrust Rating Part Number Based Engine should not accrue flight hours",
            lEngineUsageDeltaMap.get( lHighThrustRatingHours ) );
   }


   /**
    * <pre>
    * Given an aircraft collecting usages with the following configuration:
    * has many aggregated usage parameters on the engine assembly,
    * each aggregated usage parameter has multiple part specific usage parameters
    * the engine has multiple part numbers each corresponding to a different thrust rating each
    * engine part number has an associated part specific usage parameter
    * the engine part number has been changed from a Low-Thrust Rating part number to a High-Thrust
    * Rating part number
    * And the aircraft has a historical flight after the part number change
    * When another historical flight is created prior in time to the already created flight, but
    * after the part number change using Flight API v2.
    * Then the part specific usage parameter in the flight usage record of the engine is added with
    * only usage for engine part no before the IPN event (High-Thrust Rating part number)
    * </pre>
    */
   @Test
   public void itCreatesUsageRecordForOOSFlightWithAccumulatedParmBasedOnCurrentPartNumber()
         throws Exception {
      final Date lFlightArrivalDate = new Date();
      final Date lFlightDepartureDate = DateUtils.addHours( lFlightArrivalDate, -2 );
      Date lAnotherFlightArrivalDate = DateUtils.addDays( lFlightDepartureDate, -2 );
      Date lAnotherFlightDepartureDate = DateUtils.addHours( lAnotherFlightArrivalDate, -2 );
      Date lPartNumberChangeDate = DateUtils.addDays( lAnotherFlightDepartureDate, -1 );

      final PartNoKey lLowThrustRatingPartNo =
            createEnginePartNoWithThrustRating( LOW_THRUST_RATING_ENGINE, null );
      final PartNoKey lHighThrustRatingPartNo =
            createEnginePartNoWithThrustRating( HIGH_THRUST_RATING_ENGINE, null );

      final AssemblyKey lEngineAssembly = createEngineAssemblyWithAccumulatedParameters(
            lLowThrustRatingPartNo, lHighThrustRatingPartNo );

      // Required by FlightHistService (getAssemblyUsageDataSet())
      final AssemblyKey lAircraftAssembly = createAircraftAssembly();
      final PartNoKey lAircraftPart = createAircraftPart();

      final InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.setAssembly( lEngineAssembly );
            aEngine.setPartNumber( lHighThrustRatingPartNo );
            aEngine.addUsage( HOURS, BigDecimal.TEN );
            aEngine.addUsage( CYCLES, BigDecimal.TEN );
         }
      } );

      // Required by Flight Document
      createAirportLocation( ARRIVAL_AIRPORT );
      createAirportLocation( DEPARTURE_AIRPORT );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            // Mandatory properties with respect to the Flight API
            aAircraft.setAssembly( lAircraftAssembly );
            aAircraft.addEngine( lEngine );
            aAircraft.setBarcode( AIRCRAFT_BARCODE );
            aAircraft.setSerialNumber( AIRCRAFT_SERIALNUMBER );
            aAircraft.setPart( lAircraftPart );
            aAircraft.addUsage( HOURS, BigDecimal.TEN );
            aAircraft.addUsage( CYCLES, BigDecimal.TEN );
         }
      } );

      Domain.createFlight( new DomainConfiguration<com.mxi.am.domain.Flight>() {

         @Override
         public void configure( com.mxi.am.domain.Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.isHistorical();
            aFlight.setDepartureDate( lFlightDepartureDate );
            aFlight.setArrivalDate( lFlightArrivalDate );
         }
      } );

      DataTypeKey lLowThrustRatingCycle = Domain.readUsageParameter(
            Domain.readRootConfigurationSlot( lEngineAssembly ), LOW_THRUSTING_CYCLE );
      DataTypeKey lHighThrustRatingCycle = Domain.readUsageParameter(
            Domain.readRootConfigurationSlot( lEngineAssembly ), HIGH_THRUSTING_CYCLE );
      DataTypeKey lLowThrustRatingHours = Domain.readUsageParameter(
            Domain.readRootConfigurationSlot( lEngineAssembly ), LOW_THRUSTING_HOURS );
      DataTypeKey lHighThrustRatingHours = Domain.readUsageParameter(
            Domain.readRootConfigurationSlot( lEngineAssembly ), HIGH_THRUSTING_HOURS );

      createRequirementWithPartNumberChange( lEngine, lLowThrustRatingPartNo,
            lPartNumberChangeDate );

      // Build Flight Document
      FlightsDocument lFlightsDocument = FlightsDocument.Factory.newInstance();
      Flights lFlights = lFlightsDocument.addNewFlights();
      Flight lFlight = lFlights.addNewFlight();

      // Flight element
      lFlight.setMarkInError( false );

      // Flight Identifier
      FlightIdentifier lFlightIdentifier = lFlight.addNewFlightIdentifier();
      lFlightIdentifier.setExternalKey( FLIGHT_IDENTIFIER );

      // Flight Attributes
      FlightAttributes lFlightAttributes = lFlight.addNewFlightAttributes();
      lFlightAttributes.setName( FLIGHT_NAME );

      // Aircraft Identifier
      AircraftIdentifier lAircraftIdentifier = lFlight.addNewAircraftIdentifier();
      lAircraftIdentifier.setBarcode( AIRCRAFT_BARCODE );

      // Airports
      Airports lAirports = lFlight.addNewAirports();
      lAirports.setArrivalAirport( ARRIVAL_AIRPORT );
      lAirports.setDepartureAirport( DEPARTURE_AIRPORT );

      // Dates
      Dates lDates = lFlight.addNewDates();
      lDates.setScheduledArrivalDate( lAnotherFlightArrivalDate.toString() );
      lDates.setActualArrivalDate( lAnotherFlightArrivalDate.toString() );
      lDates.setScheduledDepartureDate( lAnotherFlightDepartureDate.toString() );
      lDates.setActualDepartureDate( lAnotherFlightDepartureDate.toString() );

      // When
      FlightCoordinator lFlightCoordinator =
            new FlightCoordinator( new PlannedFlightCreator( new MxFlightFacade() ) );
      try {
         lFlightCoordinator.coordinate( lFlightsDocument );

      } catch ( IntegrationException aException ) {
         aException.printStackTrace();
         fail( "Unexpected exception occurred during flight creation" );
      }

      // Then

      Map<DataTypeKey, BigDecimal> lEngineUsageDeltaMap = readUsageDeltas( lEngine );

      assertEquals( "Unexpectedly,Engine didnt accrue flight cycles", FLIGHT_CYCLES_DELTA,
            lEngineUsageDeltaMap.get( CYCLES ) );
      assertEquals( "High Thrust Rating Part Number Based Engine should accrue flight cycles",
            FLIGHT_CYCLES_DELTA, lEngineUsageDeltaMap.get( lHighThrustRatingCycle ) );
      assertNull( "Low Thrust Rating Part Number Based Engine should not accrue flight cycles",
            lEngineUsageDeltaMap.get( lLowThrustRatingCycle ) );
      assertEquals( "Unexpectedly,Engine didnt accrue flight hours", FLIGHT_HOURS_DELTA,
            lEngineUsageDeltaMap.get( HOURS ) );
      assertEquals( "High Thrust Rating Part Number Based Engine should accrue flight hours",
            FLIGHT_HOURS_DELTA, lEngineUsageDeltaMap.get( lHighThrustRatingHours ) );
      assertNull( "Low Thrust Rating Part Number Based Engine should not accrue flight hours",
            lEngineUsageDeltaMap.get( lLowThrustRatingHours ) );
   }


   /**
    * This test checks that when a historical flight is added, the usage remaining of a deadline for
    * an active REQ gets recalculated.
    *
    * Given an aircraft with an active REQ task that has a deadline. (This behaviour does not apply
    * to completed or historical REQ tasks.)
    *
    * When an historical flight is created on the aircraft
    *
    * Then the deadline is recalculated
    *
    */
   @Test
   public void itUpdatesTaskUsageRemainingOnFlightCreation() {

      // ARRANGE
      final BigDecimal lCurrentHoursTsn = new BigDecimal( 100 );
      final BigDecimal lDeadlineStartValue = new BigDecimal( 0 );
      final BigDecimal lDeadlineInterval = new BigDecimal( 150 );
      final BigDecimal lDeadlineDueValue = lDeadlineStartValue.add( lDeadlineInterval );
      final BigDecimal lDeadlineUsageRemaining = lDeadlineDueValue.subtract( lCurrentHoursTsn );
      Date lArrivalDate = new Date();
      Date lDepartureDate = DateUtils.addHours( lArrivalDate, -FLIGHT_HOURS_DELTA.intValue() );

      // Required by Flight Document
      createAirportLocation( ARRIVAL_AIRPORT );
      createAirportLocation( DEPARTURE_AIRPORT );

      // Required by FlightHistService (getAssemblyUsageDataSet()) for getting current usage of
      // assembly
      final AssemblyKey lAircraftAssembly = createAircraftAssembly();

      final PartNoKey lAircraftPart = createAircraftPart();

      final InventoryKey lAircraftInvKey =
            Domain.createAircraft( new DomainConfiguration<Aircraft>() {

               @Override
               public void configure( Aircraft aAircraft ) {
                  aAircraft.setAssembly( lAircraftAssembly );
                  aAircraft.addUsage( DataTypeKey.HOURS, lCurrentHoursTsn );
                  aAircraft.setSerialNumber( AIRCRAFT_SERIALNUMBER );
                  aAircraft.setBarcode( AIRCRAFT_BARCODE );
                  aAircraft.setPart( lAircraftPart );
               }
            } );

      TaskKey lTask = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setStatus( RefEventStatusKey.ACTV );
            aReq.setTaskClass( RefTaskClassKey.REQ );
            aReq.setInventory( lAircraftInvKey );
            aReq.addDeadline( new DomainConfiguration<Deadline>() {

               @Override
               public void configure( Deadline aDeadline ) {
                  aDeadline.setDueValue( lDeadlineDueValue );
                  aDeadline.setStartTsn( lDeadlineStartValue );
                  aDeadline.setInterval( lDeadlineInterval );
                  aDeadline.setUsageType( DataTypeKey.HOURS );
                  aDeadline.setUsageRemaining( lDeadlineUsageRemaining );
                  aDeadline.setScheduledFrom( RefSchedFromKey.BIRTH );
               }
            } );
         }
      } );

      // Build Flight Document
      FlightsDocument lFlightsDocument = FlightsDocument.Factory.newInstance();
      Flights lFlights = lFlightsDocument.addNewFlights();
      Flight lFlight = lFlights.addNewFlight();
      // Flight
      lFlight.setMarkInError( false );

      // Flight Identifier
      FlightIdentifier lFlightIdentifier = lFlight.addNewFlightIdentifier();
      lFlightIdentifier.setExternalKey( FLIGHT_IDENTIFIER );

      // Flight Attributes
      FlightAttributes lFlightAttributes = lFlight.addNewFlightAttributes();
      lFlightAttributes.setName( FLIGHT_NAME );

      // Aircraft Identifier
      AircraftIdentifier lAircraftIdentifier = lFlight.addNewAircraftIdentifier();
      InventoryIdentifier lInventoryIdentifier = lAircraftIdentifier.addNewInventoryIdentifier();
      lInventoryIdentifier.setOemSerialNumber( AIRCRAFT_SERIALNUMBER );
      lAircraftIdentifier.setBarcode( AIRCRAFT_BARCODE );

      // Airports
      Airports lAirports = lFlight.addNewAirports();
      lAirports.setArrivalAirport( ARRIVAL_AIRPORT );
      lAirports.setDepartureAirport( DEPARTURE_AIRPORT );

      // Dates
      Dates lDates = lFlight.addNewDates();
      lDates.setScheduledArrivalDate( lArrivalDate.toString() );
      lDates.setActualArrivalDate( lArrivalDate.toString() );
      lDates.setScheduledDepartureDate( lDepartureDate.toString() );
      lDates.setActualDepartureDate( lDepartureDate.toString() );

      // ACT
      FlightCoordinator lFlightCoordinator =
            new FlightCoordinator( new PlannedFlightCreator( new MxFlightFacade() ) );
      try {
         lFlightCoordinator.coordinate( lFlightsDocument );

      } catch ( IntegrationException aException ) {
         fail( "Unexpected exception occurred during flight creation" );
      }

      // ASSERT
      BigDecimal lExpectedUsageRemaining = lDeadlineUsageRemaining.subtract( FLIGHT_HOURS_DELTA );
      EvtSchedDeadTable lEvtSchedDeadTable =
            EvtSchedDeadTable.findByPrimaryKey( lTask, DataTypeKey.HOURS );
      BigDecimal lActualUsageRemaining = new BigDecimal( lEvtSchedDeadTable.getUsageRemaining() );
      assertEquals( "Task's usage wasn't adjusted by flight's delta HOURS", lExpectedUsageRemaining,
            lActualUsageRemaining );

   }


   /**
    * This test checks that when a historical flight is edited, the usage remaining of a deadline
    * for an active REQ gets recalculated.
    *
    * Given an aircraft with an active REQ task that has a deadline.
    *
    * And a historical flight is created on the aircraft
    *
    * When the historical flight is edited using the v2 flight API
    *
    * Then the deadline is recalculated
    *
    */
   @Test
   public void itUpdatesTaskUsageRemainingOnFlightEdit() {

      final BigDecimal lCurrentHoursTsn = new BigDecimal( 100 );
      final BigDecimal lDeadlineStartValue = new BigDecimal( 0 );
      final BigDecimal lDeadlineInterval = new BigDecimal( 150 );
      final BigDecimal lDeadlineDueValue = lDeadlineStartValue.add( lDeadlineInterval );
      final BigDecimal lDeadlineUsageRemaining = lDeadlineDueValue.subtract( lCurrentHoursTsn );
      final Date lArrivalDate = new Date();
      final Date lDepartureDate =
            DateUtils.addHours( lArrivalDate, -FLIGHT_HOURS_DELTA.intValue() );
      Date lNewArrivalDate = DateUtils.addHours( lArrivalDate, FLIGHT_HOURS_DELTA.intValue() );
      BigDecimal lChangeInFlightDeltaHours = FLIGHT_HOURS_DELTA;

      // Required by Flight Document
      final LocationKey lArrivalAirportKey = createAirportLocation( ARRIVAL_AIRPORT );
      final LocationKey lDepartureAirportKey = createAirportLocation( DEPARTURE_AIRPORT );

      // Required by FlightHistService (getAssemblyUsageDataSet()) for getting current usage of
      // assembly
      final AssemblyKey lAircraftAssembly = createAircraftAssembly();

      final PartNoKey lAircraftPart = createAircraftPart();

      final InventoryKey lAircraftInvKey =
            Domain.createAircraft( new DomainConfiguration<Aircraft>() {

               // Set minimum properties of an aircraft
               @Override
               public void configure( Aircraft aAircraft ) {
                  aAircraft.setAssembly( lAircraftAssembly );
                  aAircraft.addUsage( DataTypeKey.HOURS, lCurrentHoursTsn );
                  aAircraft.setSerialNumber( AIRCRAFT_SERIALNUMBER );
                  aAircraft.setBarcode( AIRCRAFT_BARCODE );
                  aAircraft.setPart( lAircraftPart );
               }
            } );

      Domain.createFlight( new DomainConfiguration<com.mxi.am.domain.Flight>() {

         @Override
         public void configure( com.mxi.am.domain.Flight aFlight ) {
            aFlight.setAircraft( lAircraftInvKey );
            aFlight.setArrivalLocation( lArrivalAirportKey );
            aFlight.setDepartureLocation( lDepartureAirportKey );
            aFlight.setHistorical( true );
            aFlight.setStatus( FlightLegStatus.MXCMPLT );
            aFlight.setName( FLIGHT_NAME );
            aFlight.setExternalKey( FLIGHT_IDENTIFIER );
            aFlight.setScheduledDepartureDate( lDepartureDate );
            aFlight.setScheduledArrivalDate( lArrivalDate );
            aFlight.setDepartureDate( lDepartureDate );
            aFlight.setArrivalDate( lArrivalDate );
            aFlight.addUsage( lAircraftInvKey, HOURS, FLIGHT_HOURS_DELTA );
            aFlight.addUsage( lAircraftInvKey, CYCLES, BigDecimal.ZERO );
         }
      } );

      TaskKey lTask = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setStatus( RefEventStatusKey.ACTV );
            aReq.setTaskClass( RefTaskClassKey.REQ );
            aReq.setInventory( lAircraftInvKey );
            aReq.addDeadline( new DomainConfiguration<Deadline>() {

               @Override
               public void configure( Deadline aDeadline ) {
                  aDeadline.setDueValue( lDeadlineDueValue );
                  aDeadline.setStartTsn( lDeadlineStartValue );
                  aDeadline.setInterval( lDeadlineInterval );
                  aDeadline.setUsageType( DataTypeKey.HOURS );
                  aDeadline.setUsageRemaining( lDeadlineUsageRemaining );
                  aDeadline.setScheduledFrom( RefSchedFromKey.BIRTH );
               }
            } );
         }
      } );

      // Build Flight Document
      FlightsDocument lFlightsDocument = FlightsDocument.Factory.newInstance();
      Flights lFlights = lFlightsDocument.addNewFlights();
      Flight lFlight = lFlights.addNewFlight();
      // Flight
      lFlight.setMarkInError( false );

      // Flight Identifier
      FlightIdentifier lFlightIdentifier = lFlight.addNewFlightIdentifier();
      lFlightIdentifier.setExternalKey( FLIGHT_IDENTIFIER );

      // Flight Attributes
      FlightAttributes lFlightAttributes = lFlight.addNewFlightAttributes();
      lFlightAttributes.setName( FLIGHT_NAME );

      // Aircraft Identifier
      AircraftIdentifier lAircraftIdentifier = lFlight.addNewAircraftIdentifier();
      lAircraftIdentifier.setBarcode( AIRCRAFT_BARCODE );

      // Airports
      Airports lAirports = lFlight.addNewAirports();
      lAirports.setArrivalAirport( ARRIVAL_AIRPORT );
      lAirports.setDepartureAirport( DEPARTURE_AIRPORT );

      // Dates
      Dates lDates = lFlight.addNewDates();
      lDates.setScheduledDepartureDate( lDepartureDate.toString() );
      lDates.setScheduledArrivalDate( lNewArrivalDate.toString() );
      lDates.setUpDate( lDepartureDate.toString() );

      // Modifying the ActualArrivalDate from 2 hours to 4 hours
      lDates.setDownDate( lNewArrivalDate.toString() );

      FlightCoordinator lFlightCoordinator =
            new FlightCoordinator( new PlannedFlightCreator( new MxFlightFacade() ) );
      try {
         lFlightCoordinator.coordinate( lFlightsDocument );
      } catch ( IntegrationException aException ) {
         fail( "Unexpected exception occurred during flight edit: " + aException.getMessage() );
      }

      BigDecimal lExpectedUsageRemaining =
            lDeadlineUsageRemaining.subtract( lChangeInFlightDeltaHours );
      EvtSchedDeadTable lEvtSchedDeadTable =
            EvtSchedDeadTable.findByPrimaryKey( lTask, DataTypeKey.HOURS );
      BigDecimal lActualUsageRemaining = new BigDecimal( lEvtSchedDeadTable.getUsageRemaining() );

      assertEquals( "Task's usage wasn't adjusted by flight's delta HOURS", lExpectedUsageRemaining,
            lActualUsageRemaining );

   }


   /**
    * <pre>
    * This test checks that the current usage of an aircraft gets updated with delta provided for
    * usages in flight document for creating a historical flight.
    *
    * Given an aircraft assembly tracking usages
    *
    * And an aircraft with current usages based on the assembly
    *
    * When a historical flight with usage deltas is created for the aircraft
    *
    * Then the flight usage delta gets added to the aircraft's current usage prior to the flight
    * </pre>
    */
   @Test
   public void itCreatesHistoricalFlightWithUsagesInFlightDocument() {

      // ARRANGE
      final BigDecimal lCurrentHoursTsn = new BigDecimal( 100 );
      final BigDecimal lCurrentCyclesTsn = new BigDecimal( 50 );
      Date lArrivalDate = new Date();
      Date lDepartureDate = DateUtils.addHours( lArrivalDate, -FLIGHT_HOURS_DELTA.intValue() );

      // Required by Flight Document
      createAirportLocation( ARRIVAL_AIRPORT );
      createAirportLocation( DEPARTURE_AIRPORT );

      // Required by FlightHistService (getAssemblyUsageDataSet()) for getting current usage of
      // assembly
      final AssemblyKey lAircraftAssembly = createAircraftAssembly();

      // Required by FlightHistService
      final PartNoKey lAircraftPart = createAircraftPart();

      final InventoryKey lAircraftInvKey =
            Domain.createAircraft( new DomainConfiguration<Aircraft>() {

               @Override
               public void configure( Aircraft aAircraft ) {
                  aAircraft.setAssembly( lAircraftAssembly );
                  aAircraft.addUsage( DataTypeKey.HOURS, lCurrentHoursTsn );
                  aAircraft.setSerialNumber( AIRCRAFT_SERIALNUMBER );
                  aAircraft.setBarcode( AIRCRAFT_BARCODE );
                  aAircraft.setPart( lAircraftPart );
                  aAircraft.addUsage( CYCLES, lCurrentCyclesTsn );
               }
            } );

      // Build Flight Document
      FlightsDocument lFlightsDocument = FlightsDocument.Factory.newInstance();
      Flights lFlights = lFlightsDocument.addNewFlights();
      Flight lFlight = lFlights.addNewFlight();
      // Flight
      lFlight.setMarkInError( false );

      // Flight Identifier
      FlightIdentifier lFlightIdentifier = lFlight.addNewFlightIdentifier();
      lFlightIdentifier.setExternalKey( FLIGHT_IDENTIFIER );

      // Flight Attributes
      FlightAttributes lFlightAttributes = lFlight.addNewFlightAttributes();
      lFlightAttributes.setName( FLIGHT_NAME );

      // Aircraft Identifier
      AircraftIdentifier lAircraftIdentifier = lFlight.addNewAircraftIdentifier();
      InventoryIdentifier lInventoryIdentifier = lAircraftIdentifier.addNewInventoryIdentifier();
      lInventoryIdentifier.setOemSerialNumber( AIRCRAFT_SERIALNUMBER );
      lAircraftIdentifier.setBarcode( AIRCRAFT_BARCODE );

      // Usages
      BigDecimal lCyclesFlightDelta = new BigDecimal( 2 );
      BigDecimal lHoursFlightDelta = new BigDecimal( 5 );
      Usages lUsages = lFlight.addNewUsages();
      Usage lUsage = lUsages.addNewUsage();
      UsageId lCyclesUsageId = lUsage.addNewUsageId();
      lCyclesUsageId.setUsageParm( CYCLES_USAGE_PARM );
      lCyclesUsageId.setDelta( lCyclesFlightDelta.doubleValue() );
      UsageId lHoursUsageId = lUsage.addNewUsageId();
      lHoursUsageId.setUsageParm( HOURS_USAGE_PARM );
      lHoursUsageId.setDelta( lHoursFlightDelta.doubleValue() );

      // Airports
      Airports lAirports = lFlight.addNewAirports();
      lAirports.setArrivalAirport( ARRIVAL_AIRPORT );
      lAirports.setDepartureAirport( DEPARTURE_AIRPORT );

      // Dates
      Dates lDates = lFlight.addNewDates();
      lDates.setScheduledArrivalDate( lArrivalDate.toString() );
      lDates.setActualArrivalDate( lArrivalDate.toString() );
      lDates.setScheduledDepartureDate( lDepartureDate.toString() );
      lDates.setActualDepartureDate( lDepartureDate.toString() );

      // ACT
      FlightCoordinator lFlightCoordinator =
            new FlightCoordinator( new PlannedFlightCreator( new MxFlightFacade() ) );
      try {
         lFlightCoordinator.coordinate( lFlightsDocument );
      } catch ( IntegrationException aException ) {
         fail( "Unexpected exception occurred during flight creation" );
      }

      // ASSERT
      BigDecimal lExpectedCurrentCyclesUsage = lCurrentCyclesTsn.add( lCyclesFlightDelta );
      BigDecimal lExpectedCurrentHoursUsage = lCurrentHoursTsn.add( lHoursFlightDelta );
      assertEquals( "Unexpected current cycles usage for aircraft after flight creation",
            lExpectedCurrentCyclesUsage, Domain.readCurrentUsage( lAircraftInvKey ).get( CYCLES ) );
      assertEquals( "Unexpected current hours usage for aircraft after flight creation",
            lExpectedCurrentHoursUsage, Domain.readCurrentUsage( lAircraftInvKey ).get( HOURS ) );

   }


   /**
    * <pre>
    * Given an aircraft collecting usages with the following configuration:
    * Has many aggregated usage parameters on the engine assembly,
    * Each aggregated usage parameter has multiple accumulated parameters,
    * The engine assembly has multiple part numbers each corresponding to a different thrust rating
    * Each engine part number has an associated accumulated parameter,
    * The aircraft has two historical flights
    * When the former of the two flights is edited using Flight API v2 with aggregated usage specified for the engine.
    * Then the flight usage record for the engine's accumulated parameter corresponding to the current thrust
    * rating (High thrust rating) reflects the edited flight usage delta for the engine specified in the flight document
    * </pre>
    */
   @Test
   public void itEditsOOSFlightWithAccumulatedParmForEngineWithSpecifiedEngineUsage()
         throws Exception {
      final Date lLatterFlightArrivalDate = new Date();
      final Date lLatterFlightDepartureDate = DateUtils.addHours( lLatterFlightArrivalDate, -2 );
      final Date lFormerFlightArrivalDate = DateUtils.addDays( lLatterFlightDepartureDate, -2 );
      final Date lFormerFlightDepartureDate = DateUtils.addHours( lFormerFlightArrivalDate, -2 );

      // Required by InventoryFinder getInventoryKey()
      final ManufacturerKey lEngineManufacturer = createManufacturer( ENGINE_MANUFACTURER_CD );

      final PartNoKey lLowThrustRatingPartNo =
            createEnginePartNoWithThrustRating( LOW_THRUST_RATING_ENGINE, lEngineManufacturer );
      final PartNoKey lHighThrustRatingPartNo =
            createEnginePartNoWithThrustRating( HIGH_THRUST_RATING_ENGINE, lEngineManufacturer );

      final AssemblyKey lEngineAssembly = createEngineAssemblyWithAccumulatedParameters(
            lLowThrustRatingPartNo, lHighThrustRatingPartNo );

      // Required by FlightHistService (getAssemblyUsageDataSet())
      final AssemblyKey lAircraftAssembly = createAircraftAssembly();
      final PartNoKey lAircraftPart = createAircraftPart();

      final InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.setAssembly( lEngineAssembly );
            aEngine.setSerialNumber( ENGINE_SERIALNUMBER );
            aEngine.setPartNumber( lHighThrustRatingPartNo );
            aEngine.addUsage( HOURS, BigDecimal.TEN );
            aEngine.addUsage( CYCLES, BigDecimal.TEN );
         }
      } );

      // Required by Flight Document
      final LocationKey lArrivalAirport = createAirportLocation( ARRIVAL_AIRPORT );
      createAirportLocation( DEPARTURE_AIRPORT );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            // Mandatory properties with respect to the Flight API
            aAircraft.setAssembly( lAircraftAssembly );
            aAircraft.addEngine( lEngine );
            aAircraft.setBarcode( AIRCRAFT_BARCODE );
            aAircraft.setSerialNumber( AIRCRAFT_SERIALNUMBER );
            aAircraft.setPart( lAircraftPart );
            aAircraft.addUsage( HOURS, BigDecimal.TEN );
            aAircraft.addUsage( CYCLES, BigDecimal.TEN );
         }
      } );

      final DataTypeKey lLowThrustRatingCycle = Domain.readUsageParameter(
            Domain.readRootConfigurationSlot( lEngineAssembly ), LOW_THRUSTING_CYCLE );
      final DataTypeKey lHighThrustRatingCycle = Domain.readUsageParameter(
            Domain.readRootConfigurationSlot( lEngineAssembly ), HIGH_THRUSTING_CYCLE );
      final DataTypeKey lLowThrustRatingHours = Domain.readUsageParameter(
            Domain.readRootConfigurationSlot( lEngineAssembly ), LOW_THRUSTING_HOURS );
      final DataTypeKey lHighThrustRatingHours = Domain.readUsageParameter(
            Domain.readRootConfigurationSlot( lEngineAssembly ), HIGH_THRUSTING_HOURS );

      // setup mocking for thrust rating related data types
      when( iDataTypeCache.getString( LOW_THRUSTING_CYCLE ) )
            .thenReturn( lLowThrustRatingCycle.toValueString() );
      when( iDataTypeCache.getString( HIGH_THRUSTING_CYCLE ) )
            .thenReturn( lHighThrustRatingCycle.toValueString() );
      when( iDataTypeCache.getString( LOW_THRUSTING_HOURS ) )
            .thenReturn( lLowThrustRatingHours.toValueString() );
      when( iDataTypeCache.getString( HIGH_THRUSTING_HOURS ) )
            .thenReturn( lHighThrustRatingHours.toValueString() );

      Domain.createFlight( new DomainConfiguration<com.mxi.am.domain.Flight>() {

         @Override
         public void configure( com.mxi.am.domain.Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setDepartureDate( lLatterFlightDepartureDate );
            aFlight.setArrivalDate( lLatterFlightArrivalDate );
         }
      } );

      Domain.createFlight( new DomainConfiguration<com.mxi.am.domain.Flight>() {

         @Override
         public void configure( com.mxi.am.domain.Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setName( FLIGHT_NAME );
            aFlight.setHistorical( true );
            aFlight.setStatus( FlightLegStatus.MXCMPLT );
            aFlight.setDepartureDate( lFormerFlightDepartureDate );
            aFlight.setArrivalDate( lFormerFlightArrivalDate );
            aFlight.setExternalKey( FORMER_FLIGHT );
            aFlight.setArrivalLocation( lArrivalAirport );
            aFlight.addUsage( lAircraft, HOURS, BigDecimal.TEN );
            aFlight.addUsage( lEngine, HOURS, BigDecimal.TEN );
            aFlight.addUsage( lAircraft, CYCLES, FLIGHT_CYCLES_DELTA );
            aFlight.addUsage( lEngine, CYCLES, FLIGHT_CYCLES_DELTA );
            aFlight.addUsage( lEngine, lHighThrustRatingCycle, FLIGHT_CYCLES_DELTA );
            aFlight.addUsage( lEngine, lHighThrustRatingHours, BigDecimal.TEN );
         }
      } );

      // Build Flight Document
      FlightsDocument lFlightsDocument = FlightsDocument.Factory.newInstance();
      Flights lFlights = lFlightsDocument.addNewFlights();
      Flight lFlight = lFlights.addNewFlight();

      // Flight element
      lFlight.setMarkInError( false );

      // Flight Identifier
      FlightIdentifier lFlightIdentifier = lFlight.addNewFlightIdentifier();
      lFlightIdentifier.setExternalKey( FORMER_FLIGHT );

      // Flight Attributes
      FlightAttributes lFlightAttributes = lFlight.addNewFlightAttributes();
      lFlightAttributes.setName( FLIGHT_NAME );

      // Aircraft Identifier
      AircraftIdentifier lAircraftIdentifier = lFlight.addNewAircraftIdentifier();
      lAircraftIdentifier.setBarcode( AIRCRAFT_BARCODE );

      // Usages
      BigDecimal lCyclesFlightDelta = new BigDecimal( 2 );
      Usages lUsages = lFlight.addNewUsages();
      Usage lUsage = lUsages.addNewUsage();
      InventoryId lEngineInventoryId = lUsage.addNewInventoryId();
      lEngineInventoryId.setSerialNumber( ENGINE_SERIALNUMBER );
      UsageId lCyclesUsageId = lUsage.addNewUsageId();
      lCyclesUsageId.setUsageParm( CYCLES_USAGE_PARM );
      lCyclesUsageId.setDelta( lCyclesFlightDelta.doubleValue() );

      // Airports
      Airports lAirports = lFlight.addNewAirports();
      lAirports.setArrivalAirport( ARRIVAL_AIRPORT );
      lAirports.setDepartureAirport( DEPARTURE_AIRPORT );

      // Dates
      Dates lDates = lFlight.addNewDates();
      BigDecimal lHoursFlightDelta = new BigDecimal( 2 );
      lDates.setScheduledArrivalDate( lFormerFlightArrivalDate.toString() );
      lDates.setDownDate( lFormerFlightArrivalDate.toString() );
      lDates.setScheduledDepartureDate( lFormerFlightDepartureDate.toString() );
      lDates.setUpDate( lFormerFlightDepartureDate.toString() );

      // When
      FlightCoordinator lFlightCoordinator =
            new FlightCoordinator( new PlannedFlightCreator( new MxFlightFacade() ) );
      try {
         lFlightCoordinator.coordinate( lFlightsDocument );
      } catch ( IntegrationException aException ) {
         aException.printStackTrace();
         fail( "Unexpected exception occurred during flight creation" );
      }

      // Then
      Map<DataTypeKey, BigDecimal> lEngineUsageDeltaMap = readUsageDeltas( lEngine );
      assertEquals(
            "Unexpectedly,Engine didnt accrue flight usage cycles specified in the flight document",
            lCyclesFlightDelta, lEngineUsageDeltaMap.get( CYCLES ) );
      assertEquals(
            "High Thrust Rating Part Number Based Engine should accrue flight usage cycles specified in the flight document",
            lCyclesFlightDelta, lEngineUsageDeltaMap.get( lHighThrustRatingCycle ) );
      assertNull( "Low Thrust Rating Part Number Based Engine should not accrue flight cycles",
            lEngineUsageDeltaMap.get( lLowThrustRatingCycle ) );
      assertEquals(
            "Unexpectedly,Engine didnt accrue flight usage hours specified in the flight document",
            lHoursFlightDelta, lEngineUsageDeltaMap.get( HOURS ) );
      assertEquals(
            "High Thrust Rating Part Number Based Engine should accrue flight usage hours specified in the flight document",
            lHoursFlightDelta, lEngineUsageDeltaMap.get( lHighThrustRatingHours ) );
      assertNull( "Low Thrust Rating Part Number Based Engine should not accrue flight hours",
            lEngineUsageDeltaMap.get( lLowThrustRatingHours ) );
   }


   /**
    * <pre>
    * Given an aircraft collecting usages with the following configuration:
    * Has many aggregated usage parameters on the engine assembly,
    * Each aggregated usage parameter has multiple accumulated parameters,
    * The engine assembly has multiple part numbers each corresponding to a different thrust rating
    * Each engine part number has an associated accumulated parameter,
    * The aircraft has two historical flights
    * When the former of the two flights is edited using Flight API v2.
    * Then the flight usage record is edited for the engine's accumulated parameter
    * corresponding to the current thrust rating (High thrust rating) with the flight usage delta
    * </pre>
    */
   @Test
   public void itEditsOOSFlightWithAccumulatedParmBasedOnPartNumberForEngine() throws Exception {
      final Date lLatterFlightArrivalDate = new Date();
      final Date lLatterFlightDepartureDate = DateUtils.addHours( lLatterFlightArrivalDate, -2 );
      final Date lFormerFlightArrivalDate = DateUtils.addDays( lLatterFlightDepartureDate, -2 );
      final Date lFormerFlightDepartureDate = DateUtils.addHours( lFormerFlightArrivalDate, -2 );

      // Required by InventoryFinder getInventoryKey()
      final ManufacturerKey lEngineManufacturer = createManufacturer( ENGINE_MANUFACTURER_CD );

      final PartNoKey lLowThrustRatingPartNo =
            createEnginePartNoWithThrustRating( LOW_THRUST_RATING_ENGINE, lEngineManufacturer );
      final PartNoKey lHighThrustRatingPartNo =
            createEnginePartNoWithThrustRating( HIGH_THRUST_RATING_ENGINE, lEngineManufacturer );

      final AssemblyKey lEngineAssembly = createEngineAssemblyWithAccumulatedParameters(
            lLowThrustRatingPartNo, lHighThrustRatingPartNo );

      // Required by FlightHistService (getAssemblyUsageDataSet())
      final AssemblyKey lAircraftAssembly = createAircraftAssembly();
      final PartNoKey lAircraftPart = createAircraftPart();

      final InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.setAssembly( lEngineAssembly );
            aEngine.setSerialNumber( ENGINE_SERIALNUMBER );
            aEngine.setPartNumber( lHighThrustRatingPartNo );
            aEngine.addUsage( HOURS, BigDecimal.TEN );
            aEngine.addUsage( CYCLES, BigDecimal.TEN );
         }
      } );

      // Required by Flight Document
      final LocationKey lArrivalAirport = createAirportLocation( ARRIVAL_AIRPORT );
      createAirportLocation( DEPARTURE_AIRPORT );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            // Mandatory properties with respect to the Flight API
            aAircraft.setAssembly( lAircraftAssembly );
            aAircraft.addEngine( lEngine );
            aAircraft.setBarcode( AIRCRAFT_BARCODE );
            aAircraft.setSerialNumber( AIRCRAFT_SERIALNUMBER );
            aAircraft.setPart( lAircraftPart );
            aAircraft.addUsage( HOURS, BigDecimal.TEN );
            aAircraft.addUsage( CYCLES, BigDecimal.TEN );
         }
      } );

      Domain.createFlight( new DomainConfiguration<com.mxi.am.domain.Flight>() {

         @Override
         public void configure( com.mxi.am.domain.Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setDepartureDate( lLatterFlightDepartureDate );
            aFlight.setArrivalDate( lLatterFlightArrivalDate );
         }
      } );

      DataTypeKey lLowThrustRatingCycle = Domain.readUsageParameter(
            Domain.readRootConfigurationSlot( lEngineAssembly ), LOW_THRUSTING_CYCLE );
      final DataTypeKey lHighThrustRatingCycle = Domain.readUsageParameter(
            Domain.readRootConfigurationSlot( lEngineAssembly ), HIGH_THRUSTING_CYCLE );
      DataTypeKey lLowThrustRatingHours = Domain.readUsageParameter(
            Domain.readRootConfigurationSlot( lEngineAssembly ), LOW_THRUSTING_HOURS );
      final DataTypeKey lHighThrustRatingHours = Domain.readUsageParameter(
            Domain.readRootConfigurationSlot( lEngineAssembly ), HIGH_THRUSTING_HOURS );

      // setup mocking for thrust rating related data types
      when( iDataTypeCache.getString( LOW_THRUSTING_CYCLE ) )
            .thenReturn( lLowThrustRatingCycle.toValueString() );
      when( iDataTypeCache.getString( HIGH_THRUSTING_CYCLE ) )
            .thenReturn( lHighThrustRatingCycle.toValueString() );
      when( iDataTypeCache.getString( LOW_THRUSTING_HOURS ) )
            .thenReturn( lLowThrustRatingHours.toValueString() );
      when( iDataTypeCache.getString( HIGH_THRUSTING_HOURS ) )
            .thenReturn( lHighThrustRatingHours.toValueString() );

      Domain.createFlight( new DomainConfiguration<com.mxi.am.domain.Flight>() {

         @Override
         public void configure( com.mxi.am.domain.Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setName( FLIGHT_NAME );
            aFlight.setHistorical( true );
            aFlight.setStatus( FlightLegStatus.MXCMPLT );
            aFlight.setDepartureDate( lFormerFlightDepartureDate );
            aFlight.setArrivalDate( lFormerFlightArrivalDate );
            aFlight.setExternalKey( FORMER_FLIGHT );
            aFlight.setArrivalLocation( lArrivalAirport );
            aFlight.addUsage( lAircraft, HOURS, BigDecimal.TEN );
            aFlight.addUsage( lEngine, HOURS, BigDecimal.TEN );
            aFlight.addUsage( lAircraft, CYCLES, FLIGHT_CYCLES_DELTA );
            aFlight.addUsage( lEngine, CYCLES, FLIGHT_CYCLES_DELTA );
            aFlight.addUsage( lEngine, lHighThrustRatingCycle, FLIGHT_CYCLES_DELTA );
            aFlight.addUsage( lEngine, lHighThrustRatingHours, BigDecimal.TEN );
         }
      } );

      // Build Flight Document
      FlightsDocument lFlightsDocument = FlightsDocument.Factory.newInstance();
      Flights lFlights = lFlightsDocument.addNewFlights();
      Flight lFlight = lFlights.addNewFlight();

      // Flight element
      lFlight.setMarkInError( false );

      // Flight Identifier
      FlightIdentifier lFlightIdentifier = lFlight.addNewFlightIdentifier();
      lFlightIdentifier.setExternalKey( FORMER_FLIGHT );

      // Flight Attributes
      FlightAttributes lFlightAttributes = lFlight.addNewFlightAttributes();
      lFlightAttributes.setName( FLIGHT_NAME );

      // Aircraft Identifier
      AircraftIdentifier lAircraftIdentifier = lFlight.addNewAircraftIdentifier();
      lAircraftIdentifier.setBarcode( AIRCRAFT_BARCODE );

      // Airports
      Airports lAirports = lFlight.addNewAirports();
      lAirports.setArrivalAirport( ARRIVAL_AIRPORT );
      lAirports.setDepartureAirport( DEPARTURE_AIRPORT );

      // Dates
      final Date lEditedFormerFlightArrivalDate = DateUtils.addDays( lFormerFlightArrivalDate, -2 );
      final Date lEditedFormerFlightDepartureDate =
            DateUtils.addHours( lEditedFormerFlightArrivalDate, -4 );
      final BigDecimal lEditedFlightHoursDelta = new BigDecimal( 4 );
      Dates lDates = lFlight.addNewDates();
      lDates.setScheduledArrivalDate( lEditedFormerFlightArrivalDate.toString() );
      lDates.setDownDate( lEditedFormerFlightArrivalDate.toString() );
      lDates.setScheduledDepartureDate( lEditedFormerFlightDepartureDate.toString() );
      lDates.setUpDate( lEditedFormerFlightDepartureDate.toString() );

      // When
      FlightCoordinator lFlightCoordinator =
            new FlightCoordinator( new PlannedFlightCreator( new MxFlightFacade() ) );
      try {
         lFlightCoordinator.coordinate( lFlightsDocument );
      } catch ( IntegrationException aException ) {
         aException.printStackTrace();
         fail( "Unexpected exception occurred during flight creation" );
      }

      // Then
      Map<DataTypeKey, BigDecimal> lEngineUsageDeltaMap = readUsageDeltas( lEngine );
      assertEquals( "Unexpectedly,Engine didnt accrue flight usage cycles based on flight message",
            FLIGHT_CYCLES_DELTA, lEngineUsageDeltaMap.get( CYCLES ) );
      assertEquals(
            "High Thrust Rating Part Number Based Engine should accrue flight usage cycles based on flight message",
            FLIGHT_CYCLES_DELTA, lEngineUsageDeltaMap.get( lHighThrustRatingCycle ) );
      assertNull( "Low Thrust Rating Part Number Based Engine should not accrue flight cycles",
            lEngineUsageDeltaMap.get( lLowThrustRatingCycle ) );
      assertEquals( "Unexpectedly,Engine didnt accrue flight usage hours based on flight message",
            lEditedFlightHoursDelta, lEngineUsageDeltaMap.get( HOURS ) );
      assertEquals(
            "High Thrust Rating Part Number Based Engine should accrue flight usage hours based on flight message",
            lEditedFlightHoursDelta, lEngineUsageDeltaMap.get( lHighThrustRatingHours ) );
      assertNull( "Low Thrust Rating Part Number Based Engine should not accrue flight hours",
            lEngineUsageDeltaMap.get( lLowThrustRatingHours ) );
   }


   /**
    * <pre>
    * Given an aircraft collecting usages with the following configuration:
    * Has many aggregated usage parameters on the engine assembly,
    * Each aggregated usage parameter has multiple accumulated parameters,
    * The engine assembly has multiple part numbers each corresponding to a different thrust rating
    * Each engine part number has an associated accumulated parameter,
    * The aircraft has a historical flight
    * When the flight is edited using Flight API v2.
    * Then the flight usage record is edited for the engine's accumulated parameter
    * corresponding to the current thrust rating (High thrust rating) with the flight usage delta
    * </pre>
    */
   @Test
   public void itEditsUsageRecordForFlightWithAccumulatedParmBasedOnPartNumberForEngine()
         throws Exception {
      final Date lFlightArrivalDate = new Date();
      final Date lFlightDepartureDate = DateUtils.addHours( lFlightArrivalDate, -2 );

      // Required by InventoryFinder getInventoryKey()
      final ManufacturerKey lEngineManufacturer = createManufacturer( ENGINE_MANUFACTURER_CD );

      final PartNoKey lLowThrustRatingPartNo =
            createEnginePartNoWithThrustRating( LOW_THRUST_RATING_ENGINE, lEngineManufacturer );
      final PartNoKey lHighThrustRatingPartNo =
            createEnginePartNoWithThrustRating( HIGH_THRUST_RATING_ENGINE, lEngineManufacturer );

      final AssemblyKey lEngineAssembly = createEngineAssemblyWithAccumulatedParameters(
            lLowThrustRatingPartNo, lHighThrustRatingPartNo );

      // Required by FlightHistService (getAssemblyUsageDataSet())
      final AssemblyKey lAircraftAssembly = createAircraftAssembly();
      final PartNoKey lAircraftPart = createAircraftPart();

      final InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.setAssembly( lEngineAssembly );
            aEngine.setSerialNumber( ENGINE_SERIALNUMBER );
            aEngine.setPartNumber( lHighThrustRatingPartNo );
            aEngine.addUsage( HOURS, BigDecimal.TEN );
            aEngine.addUsage( CYCLES, BigDecimal.TEN );
         }
      } );

      // Required by Flight Document
      final LocationKey lArrivalAirport = createAirportLocation( ARRIVAL_AIRPORT );
      createAirportLocation( DEPARTURE_AIRPORT );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            // Mandatory properties with respect to the Flight API
            aAircraft.setAssembly( lAircraftAssembly );
            aAircraft.addEngine( lEngine );
            aAircraft.setBarcode( AIRCRAFT_BARCODE );
            aAircraft.setSerialNumber( AIRCRAFT_SERIALNUMBER );
            aAircraft.setPart( lAircraftPart );
            aAircraft.addUsage( HOURS, BigDecimal.TEN );
            aAircraft.addUsage( CYCLES, BigDecimal.TEN );
         }
      } );

      DataTypeKey lLowThrustRatingCycle = Domain.readUsageParameter(
            Domain.readRootConfigurationSlot( lEngineAssembly ), LOW_THRUSTING_CYCLE );
      final DataTypeKey lHighThrustRatingCycle = Domain.readUsageParameter(
            Domain.readRootConfigurationSlot( lEngineAssembly ), HIGH_THRUSTING_CYCLE );
      DataTypeKey lLowThrustRatingHours = Domain.readUsageParameter(
            Domain.readRootConfigurationSlot( lEngineAssembly ), LOW_THRUSTING_HOURS );
      final DataTypeKey lHighThrustRatingHours = Domain.readUsageParameter(
            Domain.readRootConfigurationSlot( lEngineAssembly ), HIGH_THRUSTING_HOURS );

      // setup mocking for thrust rating related data types
      when( iDataTypeCache.getString( LOW_THRUSTING_CYCLE ) )
            .thenReturn( lLowThrustRatingCycle.toValueString() );
      when( iDataTypeCache.getString( HIGH_THRUSTING_CYCLE ) )
            .thenReturn( lHighThrustRatingCycle.toValueString() );
      when( iDataTypeCache.getString( LOW_THRUSTING_HOURS ) )
            .thenReturn( lLowThrustRatingHours.toValueString() );
      when( iDataTypeCache.getString( HIGH_THRUSTING_HOURS ) )
            .thenReturn( lHighThrustRatingHours.toValueString() );

      Domain.createFlight( new DomainConfiguration<com.mxi.am.domain.Flight>() {

         @Override
         public void configure( com.mxi.am.domain.Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setName( FLIGHT_NAME );
            aFlight.setHistorical( true );
            aFlight.setStatus( FlightLegStatus.MXCMPLT );
            aFlight.setDepartureDate( lFlightDepartureDate );
            aFlight.setArrivalDate( lFlightArrivalDate );
            aFlight.setExternalKey( FLIGHT_IDENTIFIER );
            aFlight.setArrivalLocation( lArrivalAirport );
            aFlight.addUsage( lAircraft, HOURS, BigDecimal.TEN );
            aFlight.addUsage( lEngine, HOURS, BigDecimal.TEN );
            aFlight.addUsage( lAircraft, CYCLES, FLIGHT_CYCLES_DELTA );
            aFlight.addUsage( lEngine, CYCLES, FLIGHT_CYCLES_DELTA );
            aFlight.addUsage( lEngine, lHighThrustRatingCycle, FLIGHT_CYCLES_DELTA );
            aFlight.addUsage( lEngine, lHighThrustRatingHours, BigDecimal.TEN );
         }
      } );

      // Build Flight Document
      FlightsDocument lFlightsDocument = FlightsDocument.Factory.newInstance();
      Flights lFlights = lFlightsDocument.addNewFlights();
      Flight lFlight = lFlights.addNewFlight();

      // Flight element
      lFlight.setMarkInError( false );

      // Flight Identifier
      FlightIdentifier lFlightIdentifier = lFlight.addNewFlightIdentifier();
      lFlightIdentifier.setExternalKey( FLIGHT_IDENTIFIER );

      // Flight Attributes
      FlightAttributes lFlightAttributes = lFlight.addNewFlightAttributes();
      lFlightAttributes.setName( FLIGHT_NAME );

      // Aircraft Identifier
      AircraftIdentifier lAircraftIdentifier = lFlight.addNewAircraftIdentifier();
      lAircraftIdentifier.setBarcode( AIRCRAFT_BARCODE );

      // Airports
      Airports lAirports = lFlight.addNewAirports();
      lAirports.setArrivalAirport( ARRIVAL_AIRPORT );
      lAirports.setDepartureAirport( DEPARTURE_AIRPORT );

      // Dates
      final Date lEditedFlightArrivalDate = DateUtils.addDays( lFlightArrivalDate, -2 );
      final Date lEditedFlightDepartureDate = DateUtils.addHours( lEditedFlightArrivalDate, -4 );
      final BigDecimal lEditedFlightHoursDelta = new BigDecimal( 4 );
      Dates lDates = lFlight.addNewDates();
      lDates.setScheduledArrivalDate( lEditedFlightArrivalDate.toString() );
      lDates.setScheduledDepartureDate( lEditedFlightDepartureDate.toString() );
      lDates.setUpDate( lEditedFlightDepartureDate.toString() );
      lDates.setDownDate( lEditedFlightArrivalDate.toString() );

      // When
      FlightCoordinator lFlightCoordinator =
            new FlightCoordinator( new PlannedFlightCreator( new MxFlightFacade() ) );
      try {
         lFlightCoordinator.coordinate( lFlightsDocument );
      } catch ( IntegrationException aException ) {
         aException.printStackTrace();
         fail( "Unexpected exception occurred during flight creation" );
      }

      // Then
      Map<DataTypeKey, BigDecimal> lEngineUsageDeltaMap = readUsageDeltas( lEngine );
      assertEquals( "Unexpectedly,Engine didnt accrue flight usage cycles based on flight message",
            FLIGHT_CYCLES_DELTA, lEngineUsageDeltaMap.get( CYCLES ) );
      assertEquals(
            "High Thrust Rating Part Number Based Engine should accrue flight usage cycles based on flight message",
            FLIGHT_CYCLES_DELTA, lEngineUsageDeltaMap.get( lHighThrustRatingCycle ) );
      assertNull( "Low Thrust Rating Part Number Based Engine should not accrue flight cycles",
            lEngineUsageDeltaMap.get( lLowThrustRatingCycle ) );
      assertEquals( "Unexpectedly,Engine didnt accrue flight usage hours based on flight message",
            lEditedFlightHoursDelta, lEngineUsageDeltaMap.get( HOURS ) );
      assertEquals(
            "High Thrust Rating Part Number Based Engine should accrue flight usage hours based on flight message",
            lEditedFlightHoursDelta, lEngineUsageDeltaMap.get( lHighThrustRatingHours ) );
      assertNull( "Low Thrust Rating Part Number Based Engine should not accrue flight hours",
            lEngineUsageDeltaMap.get( lLowThrustRatingHours ) );
   }


   /**
    * <pre>
    * Given an aircraft collecting usages with the following configuration:
    * Has many aggregated usage parameters on the engine assembly,
    * Each aggregated usage parameter has multiple accumulated parameters,
    * The engine assembly has multiple part numbers each corresponding to a different thrust rating
    * Each engine part number has an associated accumulated parameter,
    * The engine part number has been changed from a Low-Thrust Rating part number to a High-Thrust Rating part number,
    * The aircraft has two historical flights with the former flight before and latter flight after the part number change
    * When the former of the two flights is edited using Flight API v2.
    * Then the flight usage record is edited for the engine's accumulated parameter
    * corresponding to the thrust rating before the part number change (Low thrust rating) with the flight usage delta
    * </pre>
    */
   @Test
   public void itEditsOOSFlightWithAccumulatedParmBeforePartNumberChangeForEngine()
         throws Exception {
      final Date lLatterFlightArrivalDate = new Date();
      final Date lLatterFlightDepartureDate = DateUtils.addHours( lLatterFlightArrivalDate, -2 );
      Date lPartNumberChangeDate = DateUtils.addDays( lLatterFlightDepartureDate, -1 );
      final Date lFormerFlightArrivalDate = DateUtils.addDays( lPartNumberChangeDate, -2 );
      final Date lFormerFlightDepartureDate = DateUtils.addHours( lFormerFlightArrivalDate, -2 );

      // Required by InventoryFinder getInventoryKey()
      final ManufacturerKey lEngineManufacturer = createManufacturer( ENGINE_MANUFACTURER_CD );

      final PartNoKey lLowThrustRatingPartNo =
            createEnginePartNoWithThrustRating( LOW_THRUST_RATING_ENGINE, lEngineManufacturer );
      final PartNoKey lHighThrustRatingPartNo =
            createEnginePartNoWithThrustRating( HIGH_THRUST_RATING_ENGINE, lEngineManufacturer );

      final AssemblyKey lEngineAssembly = createEngineAssemblyWithAccumulatedParameters(
            lLowThrustRatingPartNo, lHighThrustRatingPartNo );

      // Required by FlightHistService (getAssemblyUsageDataSet())
      final AssemblyKey lAircraftAssembly = createAircraftAssembly();
      final PartNoKey lAircraftPart = createAircraftPart();

      final InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.setAssembly( lEngineAssembly );
            aEngine.setSerialNumber( ENGINE_SERIALNUMBER );
            aEngine.setPartNumber( lHighThrustRatingPartNo );
            aEngine.addUsage( HOURS, BigDecimal.TEN );
            aEngine.addUsage( CYCLES, BigDecimal.TEN );
         }
      } );

      // Required by Flight Document
      final LocationKey lArrivalAirport = createAirportLocation( ARRIVAL_AIRPORT );
      createAirportLocation( DEPARTURE_AIRPORT );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            // Mandatory properties with respect to the Flight API
            aAircraft.setAssembly( lAircraftAssembly );
            aAircraft.addEngine( lEngine );
            aAircraft.setBarcode( AIRCRAFT_BARCODE );
            aAircraft.setSerialNumber( AIRCRAFT_SERIALNUMBER );
            aAircraft.setPart( lAircraftPart );
            aAircraft.addUsage( HOURS, BigDecimal.TEN );
            aAircraft.addUsage( CYCLES, BigDecimal.TEN );
         }
      } );

      Domain.createFlight( new DomainConfiguration<com.mxi.am.domain.Flight>() {

         @Override
         public void configure( com.mxi.am.domain.Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setDepartureDate( lLatterFlightDepartureDate );
            aFlight.setArrivalDate( lLatterFlightArrivalDate );
         }
      } );

      final DataTypeKey lLowThrustRatingCycle = Domain.readUsageParameter(
            Domain.readRootConfigurationSlot( lEngineAssembly ), LOW_THRUSTING_CYCLE );
      final DataTypeKey lHighThrustRatingCycle = Domain.readUsageParameter(
            Domain.readRootConfigurationSlot( lEngineAssembly ), HIGH_THRUSTING_CYCLE );
      final DataTypeKey lLowThrustRatingHours = Domain.readUsageParameter(
            Domain.readRootConfigurationSlot( lEngineAssembly ), LOW_THRUSTING_HOURS );
      final DataTypeKey lHighThrustRatingHours = Domain.readUsageParameter(
            Domain.readRootConfigurationSlot( lEngineAssembly ), HIGH_THRUSTING_HOURS );

      // setup mocking for thrust rating related data types
      when( iDataTypeCache.getString( LOW_THRUSTING_CYCLE ) )
            .thenReturn( lLowThrustRatingCycle.toValueString() );
      when( iDataTypeCache.getString( HIGH_THRUSTING_CYCLE ) )
            .thenReturn( lHighThrustRatingCycle.toValueString() );
      when( iDataTypeCache.getString( LOW_THRUSTING_HOURS ) )
            .thenReturn( lLowThrustRatingHours.toValueString() );
      when( iDataTypeCache.getString( HIGH_THRUSTING_HOURS ) )
            .thenReturn( lHighThrustRatingHours.toValueString() );

      Domain.createFlight( new DomainConfiguration<com.mxi.am.domain.Flight>() {

         @Override
         public void configure( com.mxi.am.domain.Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setName( FLIGHT_NAME );
            aFlight.setHistorical( true );
            aFlight.setStatus( FlightLegStatus.MXCMPLT );
            aFlight.setDepartureDate( lFormerFlightDepartureDate );
            aFlight.setArrivalDate( lFormerFlightArrivalDate );
            aFlight.setExternalKey( FORMER_FLIGHT );
            aFlight.setArrivalLocation( lArrivalAirport );
            aFlight.addUsage( lAircraft, HOURS, BigDecimal.TEN );
            aFlight.addUsage( lEngine, HOURS, BigDecimal.TEN );
            aFlight.addUsage( lAircraft, CYCLES, FLIGHT_CYCLES_DELTA );
            aFlight.addUsage( lEngine, CYCLES, FLIGHT_CYCLES_DELTA );
            aFlight.addUsage( lEngine, lLowThrustRatingCycle, FLIGHT_CYCLES_DELTA );
            aFlight.addUsage( lEngine, lLowThrustRatingHours, BigDecimal.TEN );
         }
      } );

      createRequirementWithPartNumberChange( lEngine, lLowThrustRatingPartNo,
            lPartNumberChangeDate );

      // Build Flight Document
      FlightsDocument lFlightsDocument = FlightsDocument.Factory.newInstance();
      Flights lFlights = lFlightsDocument.addNewFlights();
      Flight lFlight = lFlights.addNewFlight();

      // Flight element
      lFlight.setMarkInError( false );

      // Flight Identifier
      FlightIdentifier lFlightIdentifier = lFlight.addNewFlightIdentifier();
      lFlightIdentifier.setExternalKey( FORMER_FLIGHT );

      // Flight Attributes
      FlightAttributes lFlightAttributes = lFlight.addNewFlightAttributes();
      lFlightAttributes.setName( FLIGHT_NAME );

      // Aircraft Identifier
      AircraftIdentifier lAircraftIdentifier = lFlight.addNewAircraftIdentifier();
      lAircraftIdentifier.setBarcode( AIRCRAFT_BARCODE );

      // Airports
      Airports lAirports = lFlight.addNewAirports();
      lAirports.setArrivalAirport( ARRIVAL_AIRPORT );
      lAirports.setDepartureAirport( DEPARTURE_AIRPORT );

      // Dates
      final Date lEditedFormerFlightArrivalDate = lFormerFlightArrivalDate;
      final Date lEditedFormerFlightDepartureDate =
            DateUtils.addHours( lEditedFormerFlightArrivalDate, -4 );
      final BigDecimal lEditedFlightHoursDelta = new BigDecimal( 4 );
      Dates lDates = lFlight.addNewDates();
      lDates.setScheduledArrivalDate( lEditedFormerFlightArrivalDate.toString() );
      lDates.setDownDate( lEditedFormerFlightArrivalDate.toString() );
      lDates.setScheduledDepartureDate( lEditedFormerFlightDepartureDate.toString() );
      lDates.setUpDate( lEditedFormerFlightDepartureDate.toString() );

      // When
      FlightCoordinator lFlightCoordinator =
            new FlightCoordinator( new PlannedFlightCreator( new MxFlightFacade() ) );
      try {
         lFlightCoordinator.coordinate( lFlightsDocument );
      } catch ( IntegrationException aException ) {
         aException.printStackTrace();
         fail( "Unexpected exception occurred during flight creation" );
      }

      // Then
      Map<DataTypeKey, BigDecimal> lEngineUsageDeltaMap = readUsageDeltas( lEngine );
      assertEquals( "Unexpectedly,Engine didnt accrue flight usage cycles based on flight message",
            FLIGHT_CYCLES_DELTA, lEngineUsageDeltaMap.get( CYCLES ) );
      assertEquals( "Low Thrust Rating Part Number Based Engine should accrue flight cycles",
            FLIGHT_CYCLES_DELTA, lEngineUsageDeltaMap.get( lLowThrustRatingCycle ) );
      assertNull( "High Thrust Rating Part Number Based Engine should not accrue flight cycles",
            lEngineUsageDeltaMap.get( lHighThrustRatingCycle ) );
      assertEquals( "Unexpectedly,Engine didnt accrue flight usage hours based on flight message",
            lEditedFlightHoursDelta, lEngineUsageDeltaMap.get( HOURS ) );
      assertEquals(
            "Low Thrust Rating Part Number Based Engine should accrue flight usage hours based on flight message",
            lEditedFlightHoursDelta, lEngineUsageDeltaMap.get( lLowThrustRatingHours ) );
      assertNull( "High Thrust Rating Part Number Based Engine should not accrue flight hours",
            lEngineUsageDeltaMap.get( lHighThrustRatingHours ) );
   }


   @Test
   public void itDoesNotUpdateFlightCapabilityRequirementOfACompletedFlight() {
      setupAircraftCapabilities();

      final Date arrivalDate = new Date();
      final Date departureDate = DateUtils.addHours( arrivalDate, -FLIGHT_HOURS_DELTA.intValue() );

      // Required by Flight Document
      final LocationKey arrivalAirportKey = createAirportLocation( ARRIVAL_AIRPORT );
      final LocationKey departureAirportKey = createAirportLocation( DEPARTURE_AIRPORT );

      // Required by FlightHistService (getAssemblyUsageDataSet()) for getting current usage of
      // assembly
      final AssemblyKey aircraftAssembly = createAircraftAssembly();

      final PartNoKey aircraftPart = createAircraftPart();

      final InventoryKey aircraftInvKey = Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( aircraftAssembly );
         aircraft.setSerialNumber( AIRCRAFT_SERIALNUMBER );
         aircraft.setBarcode( AIRCRAFT_BARCODE );
         aircraft.setPart( aircraftPart );

      } );

      // Create a COMPLETE flight
      Domain.createFlight( flight -> {
         flight.setAircraft( aircraftInvKey );
         flight.setArrivalLocation( arrivalAirportKey );
         flight.setDepartureLocation( departureAirportKey );
         flight.setHistorical( true );
         flight.setStatus( FlightLegStatus.MXCMPLT );
         flight.setName( FLIGHT_NAME );
         flight.setExternalKey( FLIGHT_IDENTIFIER );
         flight.setScheduledDepartureDate( departureDate );
         flight.setScheduledArrivalDate( arrivalDate );
         flight.setDepartureDate( departureDate );
         flight.setArrivalDate( arrivalDate );
         flight.addUsage( aircraftInvKey, HOURS, FLIGHT_HOURS_DELTA );
         flight.addUsage( aircraftInvKey, CYCLES, BigDecimal.ZERO );
      } );

      // Build Flight Document
      FlightsDocument flightsDocument = FlightsDocument.Factory.newInstance();
      Flights lFlights = flightsDocument.addNewFlights();
      Flight flight = lFlights.addNewFlight();
      // Flight
      flight.setMarkInError( false );

      // Flight Identifier
      FlightIdentifier flightIdentifier = flight.addNewFlightIdentifier();
      flightIdentifier.setExternalKey( FLIGHT_IDENTIFIER );

      // Flight Attributes
      FlightAttributes flightAttributes = flight.addNewFlightAttributes();
      flightAttributes.setName( FLIGHT_NAME );

      CapabilityRequirement capabilityRequirement =
            flightAttributes.addNewCapabilityRequirements().addNewCapabilityRequirement();
      capabilityRequirement.setCapabilityCode( AIRCRAFT_CAPABILITY_CD );
      capabilityRequirement.setLevelCode( AIRCRAFT_CAPABILITY_LEVEL_CD );

      // Aircraft Identifier
      AircraftIdentifier aircraftIdentifier = flight.addNewAircraftIdentifier();
      aircraftIdentifier.setBarcode( AIRCRAFT_BARCODE );

      // Airports
      Airports airports = flight.addNewAirports();
      airports.setArrivalAirport( ARRIVAL_AIRPORT );
      airports.setDepartureAirport( DEPARTURE_AIRPORT );

      // Dates
      Dates dates = flight.addNewDates();
      dates.setScheduledDepartureDate( departureDate.toString() );
      dates.setActualDepartureDate( departureDate.toString() );
      dates.setScheduledArrivalDate( arrivalDate.toString() );
      dates.setActualArrivalDate( arrivalDate.toString() );

      FlightCoordinator flightCoordinator =
            new FlightCoordinator( new PlannedFlightCreator( new MxFlightFacade() ) );

      try {

         flightCoordinator.coordinate( flightsDocument );

      } catch ( IntegrationException exception ) {
         fail( "Unexpected exception occurred during flight edit: " + exception.getMessage() );
      }

      assertThat( "Unexpectedly was able to add a capability requirement to a completed flight",
            capabilityRequirementsExistForFlight( FLIGHT_IDENTIFIER ), is( false ) );
   }


   @Test
   public void itDoesNotUpdateFlightCapabilityRequirementOfAnOutFlight() {
      setupAircraftCapabilities();

      final Date arrivalDate = new Date();
      final Date departureDate = DateUtils.addHours( arrivalDate, -FLIGHT_HOURS_DELTA.intValue() );

      // Required by Flight Document
      final LocationKey arrivalAirportKey = createAirportLocation( ARRIVAL_AIRPORT );
      final LocationKey departureAirportKey = createAirportLocation( DEPARTURE_AIRPORT );

      // Required by FlightHistService (getAssemblyUsageDataSet()) for getting current usage of
      // assembly
      final AssemblyKey aircraftAssembly = createAircraftAssembly();

      final PartNoKey aircraftPart = createAircraftPart();

      final InventoryKey aircraftInvKey = Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( aircraftAssembly );
         aircraft.setSerialNumber( AIRCRAFT_SERIALNUMBER );
         aircraft.setBarcode( AIRCRAFT_BARCODE );
         aircraft.setPart( aircraftPart );

      } );

      // Create an OUT flight
      Domain.createFlight( flight -> {
         flight.setAircraft( aircraftInvKey );
         flight.setArrivalLocation( arrivalAirportKey );
         flight.setDepartureLocation( departureAirportKey );
         flight.setHistorical( false );
         flight.setStatus( FlightLegStatus.MXOUT );
         flight.setName( FLIGHT_NAME );
         flight.setExternalKey( FLIGHT_IDENTIFIER );
         flight.setScheduledDepartureDate( departureDate );
         flight.setScheduledArrivalDate( arrivalDate );
         flight.setDepartureDate( departureDate );
         // Note that OUT does not have up/down dates, nor an actual arrival date
      } );

      // Build Flight Document
      FlightsDocument flightsDocument = FlightsDocument.Factory.newInstance();
      Flights lFlights = flightsDocument.addNewFlights();
      Flight flight = lFlights.addNewFlight();
      // Flight
      flight.setMarkInError( false );

      // Flight Identifier
      FlightIdentifier flightIdentifier = flight.addNewFlightIdentifier();
      flightIdentifier.setExternalKey( FLIGHT_IDENTIFIER );

      // Flight Attributes
      FlightAttributes flightAttributes = flight.addNewFlightAttributes();
      flightAttributes.setName( FLIGHT_NAME );

      CapabilityRequirement capabilityRequirement =
            flightAttributes.addNewCapabilityRequirements().addNewCapabilityRequirement();
      capabilityRequirement.setCapabilityCode( AIRCRAFT_CAPABILITY_CD );
      capabilityRequirement.setLevelCode( AIRCRAFT_CAPABILITY_LEVEL_CD );

      // Aircraft Identifier
      AircraftIdentifier aircraftIdentifier = flight.addNewAircraftIdentifier();
      aircraftIdentifier.setBarcode( AIRCRAFT_BARCODE );

      // Airports
      Airports airports = flight.addNewAirports();
      airports.setArrivalAirport( ARRIVAL_AIRPORT );
      airports.setDepartureAirport( DEPARTURE_AIRPORT );

      // Dates
      Dates dates = flight.addNewDates();
      dates.setScheduledDepartureDate( departureDate.toString() );
      dates.setActualDepartureDate( departureDate.toString() );
      dates.setScheduledArrivalDate( arrivalDate.toString() );
      // Note that OUT does not have up/down dates, nor an actual arrival date

      FlightCoordinator flightCoordinator =
            new FlightCoordinator( new PlannedFlightCreator( new MxFlightFacade() ) );

      try {

         flightCoordinator.coordinate( flightsDocument );

      } catch ( IntegrationException exception ) {
         fail( "Unexpected exception occurred during flight edit: " + exception.getMessage() );
      }

      assertThat(
            "Unexpectedly was able to add a capability requirement to a flight with a status of OUT",
            capabilityRequirementsExistForFlight( FLIGHT_IDENTIFIER ), is( false ) );
   }


   @Test
   public void itUpdatesFlightCapabilityRequirementOfAPlannedFlight() {
      setupAircraftCapabilities();

      final Date arrivalDate = new Date();
      final Date departureDate = DateUtils.addHours( arrivalDate, -FLIGHT_HOURS_DELTA.intValue() );

      // Required by Flight Document
      final LocationKey arrivalAirportKey = createAirportLocation( ARRIVAL_AIRPORT );
      final LocationKey departureAirportKey = createAirportLocation( DEPARTURE_AIRPORT );

      // Required by FlightHistService (getAssemblyUsageDataSet()) for getting current usage of
      // assembly
      final AssemblyKey aircraftAssembly = createAircraftAssembly();

      final PartNoKey aircraftPart = createAircraftPart();

      final InventoryKey aircraftInvKey = Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( aircraftAssembly );
         aircraft.setSerialNumber( AIRCRAFT_SERIALNUMBER );
         aircraft.setBarcode( AIRCRAFT_BARCODE );
         aircraft.setPart( aircraftPart );

      } );

      // Create a planned flight
      Domain.createFlight( flight -> {
         flight.setAircraft( aircraftInvKey );
         flight.setArrivalLocation( arrivalAirportKey );
         flight.setDepartureLocation( departureAirportKey );
         flight.setHistorical( false );
         flight.setStatus( FlightLegStatus.MXPLAN );
         flight.setName( FLIGHT_NAME );
         flight.setExternalKey( FLIGHT_IDENTIFIER );
         flight.setScheduledDepartureDate( departureDate );
         flight.setScheduledArrivalDate( arrivalDate );
      } );

      // Build Flight Document
      FlightsDocument flightsDocument = FlightsDocument.Factory.newInstance();
      Flights lFlights = flightsDocument.addNewFlights();
      Flight flight = lFlights.addNewFlight();
      // Flight
      flight.setMarkInError( false );

      // Flight Identifier
      FlightIdentifier flightIdentifier = flight.addNewFlightIdentifier();
      flightIdentifier.setExternalKey( FLIGHT_IDENTIFIER );

      // Flight Attributes
      FlightAttributes flightAttributes = flight.addNewFlightAttributes();
      flightAttributes.setName( FLIGHT_NAME );

      CapabilityRequirement capabilityRequirement =
            flightAttributes.addNewCapabilityRequirements().addNewCapabilityRequirement();
      capabilityRequirement.setCapabilityCode( AIRCRAFT_CAPABILITY_CD );
      capabilityRequirement.setLevelCode( AIRCRAFT_CAPABILITY_LEVEL_CD );

      // Aircraft Identifier
      AircraftIdentifier aircraftIdentifier = flight.addNewAircraftIdentifier();
      aircraftIdentifier.setBarcode( AIRCRAFT_BARCODE );

      // Airports
      Airports airports = flight.addNewAirports();
      airports.setArrivalAirport( ARRIVAL_AIRPORT );
      airports.setDepartureAirport( DEPARTURE_AIRPORT );

      // Dates
      Dates dates = flight.addNewDates();
      dates.setScheduledDepartureDate( departureDate.toString() );
      dates.setScheduledArrivalDate( arrivalDate.toString() );

      FlightCoordinator flightCoordinator =
            new FlightCoordinator( new PlannedFlightCreator( new MxFlightFacade() ) );

      try {

         flightCoordinator.coordinate( flightsDocument );

      } catch ( IntegrationException exception ) {
         fail( "Unexpected exception occurred during flight edit: " + exception.getMessage() );
      }

      assertThat( "Unexpectedly was unable to add a capability requirement to a planned flight",
            capabilityRequirementsExistForFlight( FLIGHT_IDENTIFIER ), is( true ) );
   }


   /**
    * Inject aircraft capabilities and levels
    */
   private void setupAircraftCapabilities() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
   }


   private FlightLegId retrieveFlightLegIdByExternalKey( String externalKey ) {

      final DataSetArgument args = new DataSetArgument();
      args.add( "ext_key", externalKey );

      final QuerySet querySet =
            QuerySetFactory.getInstance().executeQuery( new String[] { "leg_id" }, "FL_LEG", args );

      FlightLegId flightLegId = null;

      if ( querySet.next() ) {
         flightLegId = new FlightLegId( querySet.getBytes( "leg_id" ) );
      }

      return flightLegId;
   }


   private boolean capabilityRequirementsExistForFlight( String externalKey ) {
      final DataSetArgument args = new DataSetArgument();
      args.add( "FL_LEG_ID", retrieveFlightLegIdByExternalKey( externalKey ).toUuid() );

      final QuerySet querySet =
            QuerySetFactory.getInstance().executeQueryTable( "FL_REQUIREMENT", args );

      return querySet.next();
   }


   private Map<DataTypeKey, BigDecimal> readUsageDeltas( InventoryKey lInventoryKey ) {
      Map<DataTypeKey, BigDecimal> lUsageDeltaMap = new HashMap<>();
      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( USG_USAGE_DATA_TABLE,
            lInventoryKey.getPKWhereArg() );
      while ( lQs.next() ) {
         BigDecimal lDelta = lQs.getBigDecimal( "TSN_DELTA_QT" );
         lUsageDeltaMap.put( lQs.getKey( DataTypeKey.class, "DATA_TYPE_DB_ID", "DATA_TYPE_ID" ),
               lDelta );
      }
      return lUsageDeltaMap;
   }


   private void createRequirementWithPartNumberChange( final InventoryKey aInventory,
         final PartNoKey aOriginalPartNo, final Date aDate ) {
      Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aBuilder ) {
            aBuilder.setInventory( aInventory );
            aBuilder.setActualStartDate( aDate );
            aBuilder.setActualEndDate( aDate );
         }

      } );

      new EventBuilder().onInventory( aInventory ).withType( RefEventTypeKey.IPN )
            .withActualEndDate( aDate ).withPartNo( aOriginalPartNo ).build();
   }


   private AssemblyKey createEngineAssemblyWithAccumulatedParameters(
         final PartNoKey aLowThrustRatingPartNo, final PartNoKey aHighThrustRatingPartNo ) {
      return Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

         @Override
         public void configure( EngineAssembly aEngineAssembly ) {
            aEngineAssembly
                  .addUsageDefinitionConfiguration( new DomainConfiguration<UsageDefinition>() {

                     @Override
                     public void configure( UsageDefinition aUsageDefinition ) {
                        aUsageDefinition.addUsageParameter( HOURS );
                        aUsageDefinition.addUsageParameter( CYCLES );
                        aUsageDefinition.addAccumulatedParameterConfiguration(
                              new DomainConfiguration<AccumulatedParameter>() {

                                 @Override
                                 public void
                                       configure( AccumulatedParameter aAccumulatedParameter ) {
                                    aAccumulatedParameter.setAggregatedDataType( CYCLES );
                                    aAccumulatedParameter.setCode( LOW_THRUSTING_CYCLE );
                                    aAccumulatedParameter.setPartNoKey( aLowThrustRatingPartNo );
                                 }
                              } );
                        aUsageDefinition.addAccumulatedParameterConfiguration(
                              new DomainConfiguration<AccumulatedParameter>() {

                                 @Override
                                 public void
                                       configure( AccumulatedParameter aAccumulatedParameter ) {
                                    aAccumulatedParameter.setAggregatedDataType( CYCLES );
                                    aAccumulatedParameter.setCode( HIGH_THRUSTING_CYCLE );
                                    aAccumulatedParameter.setPartNoKey( aHighThrustRatingPartNo );
                                 }
                              } );
                        aUsageDefinition.addAccumulatedParameterConfiguration(
                              new DomainConfiguration<AccumulatedParameter>() {

                                 @Override
                                 public void
                                       configure( AccumulatedParameter aAccumulatedParameter ) {
                                    aAccumulatedParameter.setAggregatedDataType( HOURS );
                                    aAccumulatedParameter.setCode( HIGH_THRUSTING_HOURS );
                                    aAccumulatedParameter.setPartNoKey( aHighThrustRatingPartNo );
                                 }
                              } );
                        aUsageDefinition.addAccumulatedParameterConfiguration(
                              new DomainConfiguration<AccumulatedParameter>() {

                                 @Override
                                 public void
                                       configure( AccumulatedParameter aAccumulatedParameter ) {
                                    aAccumulatedParameter.setAggregatedDataType( HOURS );
                                    aAccumulatedParameter.setCode( LOW_THRUSTING_HOURS );
                                    aAccumulatedParameter.setPartNoKey( aLowThrustRatingPartNo );
                                 }
                              } );
                     }
                  } );
            aEngineAssembly.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

               @Override
               public void configure( ConfigurationSlot aConfigSlot ) {
                  aConfigSlot.addUsageParameter( CYCLES );
                  aConfigSlot.addUsageParameter( HOURS );
                  aConfigSlot.addPartGroup( new DomainConfiguration<PartGroup>() {

                     @Override
                     public void configure( PartGroup aPartGroup ) {
                        aPartGroup.addPart( aLowThrustRatingPartNo );
                        aPartGroup.addPart( aHighThrustRatingPartNo );
                     }
                  } );
               }
            } );
         }
      } );
   }


   private PartNoKey createEnginePartNoWithThrustRating( final String aPartNoCode,
         final ManufacturerKey aManufacturer ) {

      return Domain.createPart( new DomainConfiguration<Part>() {

         @Override
         public void configure( Part aPart ) {
            aPart.setInventoryClass( RefInvClassKey.ASSY );
            aPart.setCode( aPartNoCode );
            aPart.setManufacturer( aManufacturer );
         }
      } );

   }


   private LocationKey createAirportLocation( final String aLocationCode ) {
      return Domain.createLocation( aLocation -> {
         aLocation.setType( RefLocTypeKey.AIRPORT );
         aLocation.setCode( aLocationCode );
      } );
   }


   private AssemblyKey createAircraftAssembly() {
      return Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

         @Override
         public void configure( AircraftAssembly aAssembly ) {
            aAssembly.addUsageDefinitionConfiguration( new DomainConfiguration<UsageDefinition>() {

               @Override
               public void configure( UsageDefinition aBuilder ) {
                  aBuilder.addUsageParameter( HOURS );
                  aBuilder.addUsageParameter( CYCLES );
               }
            } );
         }
      } );
   }


   private ManufacturerKey createManufacturer( final String aManufacturerCode ) {
      return Domain.createManufacturer( new DomainConfiguration<Manufacturer>() {

         @Override
         public void configure( Manufacturer aBuilder ) {
            aBuilder.setCode( aManufacturerCode );
         }
      } );
   }


   private PartNoKey createAircraftPart() {
      return Domain.createPart( new DomainConfiguration<Part>() {

         @Override
         public void configure( Part aPart ) {
            aPart.setInventoryClass( RefInvClassKey.ACFT );
            aPart.setManufacturer(
                  Domain.createManufacturer( new DomainConfiguration<Manufacturer>() {

                     // Manufacturer is required by the Flight API
                     @Override
                     public void configure( Manufacturer aManufacturer ) {
                        aManufacturer.setCode( AIRCRAFT_MANUFACTURER_CD );
                     }
                  } ) );
         }
      } );
   }


   private void assertFlightPlanOnAircraft( InventoryKey aAircraft,
         FlightLegId... aExpectedFlightList ) {

      QuerySet lActualFlightQs = getFlightPlan( aAircraft );

      int lActualFlightsFound = lActualFlightQs.getRowCount();

      // Asserts number of flights found matches the expected number of flights
      assertEquals( aExpectedFlightList.length, lActualFlightsFound );

      int lCount = 0;

      lActualFlightQs.beforeFirst();

      while ( lActualFlightQs.next() ) {

         if ( lActualFlightQs.getInt( "flight_plan_ord" ) < lActualFlightsFound ) {

            // Asserts the arrival flight leg
            if ( aExpectedFlightList[lCount] == null ) {
               assertTrue( lActualFlightQs.isNull( "arr_leg_id" ) );
            } else {
               assertEquals( aExpectedFlightList[lCount],
                     lActualFlightQs.getId( FlightLegId.class, "arr_leg_id" ) );
            }

            // Asserts the departure flight leg
            assertEquals( aExpectedFlightList[lCount + 1],
                  lActualFlightQs.getId( FlightLegId.class, "dep_leg_id" ) );

         } else {

            // Asserts the arrival flight leg
            assertEquals( aExpectedFlightList[lCount],
                  lActualFlightQs.getId( FlightLegId.class, "arr_leg_id" ) );

            // The last flight departure leg in a flight plan is always null
            assertTrue( lActualFlightQs.isNull( "dep_leg_id" ) );
         }

         lCount++;
      }
   }


   private InventoryKey createAircraft( String aBarcode ) {

      iAirportLocation = Domain.createLocation();

      final PartNoKey lPartNo = Domain.createPart( aPart -> {
         aPart.setInventoryClass( RefInvClassKey.ACFT );
         aPart.setManufacturer( Domain.createManufacturer() );
      } );

      final InventoryKey aircraft = Domain.createAircraft( aAircraft -> {
         aAircraft.setBarcode( aBarcode );
         aAircraft.setPart( lPartNo );
         aAircraft.setLocation( iAirportLocation );
      } );

      return aircraft;
   }


   private FlightLegId createPlanFlight( String aFlightName, String aExternalKey,
         LocationKey aDepartureAirport, LocationKey aArrivalAirport, InventoryKey aAircraft,
         Date aDepartureDate, Date aArrivalDate ) throws Exception {

      FlightTO lFlightTO = new FlightTO.Builder( aFlightName, aExternalKey, aDepartureAirport,
            aArrivalAirport, iFlightHrKey ).aircraftKey( new AircraftKey( aAircraft ) )
                  .scheduledDepartureDate( aDepartureDate ).scheduledArrivalDate( aArrivalDate )
                  .build();

      FlightLegId lFlightLegId = new MxFlightFacade().createFlight( lFlightTO );

      return lFlightLegId;
   }


   private FlightLegId createHistoricFlight( InventoryKey aAircraft, Date aDepartureDate,
         Date aArrivalDate ) {

      FlightLegId flightLegId = Domain.createFlight( aFlight -> {
         aFlight.setAircraft( aAircraft );
         aFlight.setHistorical( true );
         aFlight.setDepartureDate( aDepartureDate );
         aFlight.setArrivalDate( aArrivalDate );
         aFlight.setStatus( FlightLegStatus.MXCMPLT );
      } );

      return flightLegId;
   }


   private QuerySet getFlightPlan( InventoryKey aAircraft ) {

      String lSqlStatement = "SELECT flight_plan_ord, arr_leg_id, dep_leg_id, loc_db_id, loc_id  "
            + "FROM inv_ac_flight_plan WHERE inv_no_db_id = " + aAircraft.getDbId()
            + " AND inv_no_id = " + aAircraft.getId() + " ORDER BY flight_plan_ord";

      DataSetStatement lStatement = new SQLStatement( lSqlStatement );

      QuerySet lQs = MxDataAccess.getInstance().executeQuery( lStatement, new DataSetArgument() );

      return lQs;
   }
}
