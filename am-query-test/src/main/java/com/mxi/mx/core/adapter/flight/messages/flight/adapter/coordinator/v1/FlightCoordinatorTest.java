package com.mxi.mx.core.adapter.flight.messages.flight.adapter.coordinator.v1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.AircraftAssembly;
import com.mxi.am.domain.Deadline;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Location;
import com.mxi.am.domain.Manufacturer;
import com.mxi.am.domain.Part;
import com.mxi.am.domain.Requirement;
import com.mxi.am.domain.UsageDefinition;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.adapter.flight.messages.flight.adapter.facade.v1.MxFlightFacade;
import com.mxi.mx.core.adapter.flight.messages.flight.adapter.logic.flightcreator.v1.PlannedFlightCreator;
import com.mxi.mx.core.adapter.flight.tools.CacheRepository;
import com.mxi.mx.core.adapter.flight.tools.DataCache;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefSchedFromKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.table.evt.EvtSchedDeadTable;
import com.mxi.mx.core.table.org.OrgHr;
import com.mxi.mx.integration.exceptions.IntegrationException;
import com.mxi.xml.xsd.core.flights.flight.x10.FlightsDocument;
import com.mxi.xml.xsd.core.flights.flight.x10.FlightsDocument.Flights;
import com.mxi.xml.xsd.core.flights.flight.x10.FlightsDocument.Flights.Flight;
import com.mxi.xml.xsd.core.flights.flight.x10.FlightsDocument.Flights.Flight.Airports;
import com.mxi.xml.xsd.core.flights.flight.x10.FlightsDocument.Flights.Flight.Dates;
import com.mxi.xml.xsd.core.flights.flight.x10.FlightsDocument.Flights.Flight.FlightAttributes;
import com.mxi.xml.xsd.core.mxdomaintypes.x20.AircraftIdentifier;
import com.mxi.xml.xsd.core.mxdomaintypes.x20.FlightIdentifier;
import com.mxi.xml.xsd.core.mxdomaintypes.x20.UsageIdentifier;
import com.mxi.xml.xsd.core.mxdomaintypes.x20.UsageIdentifier.Value;
import com.mxi.xml.xsd.core.mxdomaintypes.x20.UsageType;
import com.mxi.xml.xsd.core.mxdomaintypes.x20.UsageType.Usage;


/**
 * Test class for flight coordinator of Flight API v1
 *
 */
public class FlightCoordinatorTest {

   private static final String FLIGHT_IDENTIFIER = "FLIGHT_11685";
   private static final String FLIGHT_NAME = "FLIGHT_NAME";
   private static final String AIRCRAFT_BARCODE = "I0011685";
   private static final String ARRIVAL_AIRPORT = "YOW";
   private static final String DEPARTURE_AIRPORT = "ATL";
   private static final String MANUFACTURER_CD = "MANUFACTURER01";
   private static final String CYCLES_USAGE_PARM = "CYCLES";
   private static final String HOURS_USAGE_PARM = "HOURS";

   private static final BigDecimal FLIGHT_HOURS_DELTA = new BigDecimal( 2 );

   private CacheRepository iMockCacheRepository;
   private DataCache iDataTypeCache;
   private HumanResourceKey iFlightHrKey;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @Before
   public void setup() throws Exception {

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
      when( iDataTypeCache.getString( HOURS_USAGE_PARM ) ).thenReturn(
            DataTypeKey.HOURS.toValueString() );
      when( iDataTypeCache.getString( CYCLES_USAGE_PARM ) ).thenReturn(
            DataTypeKey.CYCLES.toValueString() );

   }


   @After
   public void teardown() {
      CacheRepository.setInstance( null );
      int lUserId = OrgHr.findByPrimaryKey( iFlightHrKey ).getUserId();
      UserParameters.setInstance( lUserId, "LOGIC", null );
   }


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

      Domain.createLocation( new DomainConfiguration<Location>() {

         @Override
         public void configure( Location aLocation ) {
            aLocation.setType( RefLocTypeKey.AIRPORT );
            aLocation.setCode( ARRIVAL_AIRPORT );
         }
      } );

      Domain.createLocation( new DomainConfiguration<Location>() {

         @Override
         public void configure( Location aLocation ) {

            aLocation.setType( RefLocTypeKey.AIRPORT );
            aLocation.setCode( DEPARTURE_AIRPORT );
         }
      } );

      final PartNoKey lAircraftPart = Domain.createPart( new DomainConfiguration<Part>() {

         @Override
         public void configure( Part aPart ) {
            aPart.setInventoryClass( RefInvClassKey.ACFT );
            aPart.setManufacturer( Domain
                  .createManufacturer( new DomainConfiguration<Manufacturer>() {

                     @Override
                     public void configure( Manufacturer aManufacturer ) {
                        aManufacturer.setCode( MANUFACTURER_CD );
                     }
                  } ) );
         }
      } );

      final InventoryKey lAircraftInvKey =
            Domain.createAircraft( new DomainConfiguration<Aircraft>() {

               @Override
               public void configure( Aircraft aAircraft ) {
                  aAircraft.addUsage( DataTypeKey.HOURS, lCurrentHoursTsn );
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
      com.mxi.xml.xsd.core.flights.flight.x10.FlightsDocument.Flights.Flight lFlight =
            lFlights.addNewFlight();
      // Flight
      lFlight.setProcessAsHistoric( false );
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
      System.out.println( lFlightsDocument.toString() );

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
      final AssemblyKey lAircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aAssembly ) {
                  aAssembly
                        .addUsageDefinitionConfiguration( new DomainConfiguration<UsageDefinition>() {

                           @Override
                           public void configure( UsageDefinition aBuilder ) {
                              aBuilder.addUsageParameter( DataTypeKey.HOURS );
                              aBuilder.addUsageParameter( DataTypeKey.CYCLES );
                           }

                        } );
               }
            } );

      // Required by FlightHistService
      final PartNoKey lAircraftPart = Domain.createPart( new DomainConfiguration<Part>() {

         @Override
         public void configure( Part aPart ) {
            aPart.setInventoryClass( RefInvClassKey.ACFT );
            aPart.setManufacturer( Domain
                  .createManufacturer( new DomainConfiguration<Manufacturer>() {

                     @Override
                     public void configure( Manufacturer aManufacturer ) {
                        aManufacturer.setCode( MANUFACTURER_CD );
                     }
                  } ) );
         }
      } );

      final InventoryKey lAircraftInvKey =
            Domain.createAircraft( new DomainConfiguration<Aircraft>() {

               @Override
               public void configure( Aircraft aAircraft ) {
                  aAircraft.setAssembly( lAircraftAssembly );
                  aAircraft.addUsage( DataTypeKey.HOURS, lCurrentHoursTsn );
                  aAircraft.setBarcode( AIRCRAFT_BARCODE );
                  aAircraft.setPart( lAircraftPart );
                  aAircraft.addUsage( DataTypeKey.CYCLES, lCurrentCyclesTsn );
               }
            } );

      // Build Flight Document
      FlightsDocument lFlightsDocument = FlightsDocument.Factory.newInstance();
      Flights lFlights = lFlightsDocument.addNewFlights();
      Flight lFlight = lFlights.addNewFlight();
      // Flight
      lFlight.setMarkInError( false );
      lFlight.setProcessAsHistoric( true );

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
      UsageType lUsageType = lFlight.addNewUsages();
      Usage lUsage = lUsageType.addNewUsage();
      UsageIdentifier lCyclesUsageId = lUsage.addNewUsageId();
      lCyclesUsageId.setUsageParm( CYCLES_USAGE_PARM );
      Value lCyclesUsageDeltaValue = lCyclesUsageId.addNewValue();
      lCyclesUsageDeltaValue.setDelta( lCyclesFlightDelta.doubleValue() );
      UsageIdentifier lHoursUsageId = lUsage.addNewUsageId();
      lHoursUsageId.setUsageParm( HOURS_USAGE_PARM );
      Value lHoursUsageDeltaValue = lHoursUsageId.addNewValue();
      lHoursUsageDeltaValue.setDelta( lHoursFlightDelta.doubleValue() );

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
            lExpectedCurrentCyclesUsage,
            Domain.readCurrentUsage( lAircraftInvKey ).get( DataTypeKey.CYCLES ) );
      assertEquals( "Unexpected current hours usage for aircraft after flight creation",
            lExpectedCurrentHoursUsage,
            Domain.readCurrentUsage( lAircraftInvKey ).get( DataTypeKey.HOURS ) );
   }


   private LocationKey createAirportLocation( final String aLocationCode ) {
      return Domain.createLocation( new DomainConfiguration<Location>() {

         @Override
         public void configure( Location aLocation ) {
            aLocation.setType( RefLocTypeKey.AIRPORT );
            aLocation.setCode( aLocationCode );
         }
      } );
   }

}
