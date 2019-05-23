package com.mxi.mx.db.trigger.flight;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.UUID;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.GlobalParametersFake;
import com.mxi.mx.common.config.ParmTypeEnum;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.MxAfterTrigger;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.core.adapter.flight.messages.flight.adapter.coordinator.v1.FlightCoordinator;
import com.mxi.mx.core.adapter.flight.messages.flight.adapter.facade.v1.MxFlightFacade;
import com.mxi.mx.core.adapter.flight.messages.flight.adapter.logic.flightcreator.v1.PlannedFlightCreator;
import com.mxi.mx.core.adapter.flight.tools.CacheException;
import com.mxi.mx.integration.exceptions.IntegrationException;
import com.mxi.xml.xsd.core.flights.flight.x10.FlightsDocument;
import com.mxi.xml.xsd.core.flights.flight.x10.FlightsDocument.Flights;
import com.mxi.xml.xsd.core.flights.flight.x10.FlightsDocument.Flights.Flight.Airports;
import com.mxi.xml.xsd.core.flights.flight.x10.FlightsDocument.Flights.Flight.Dates;
import com.mxi.xml.xsd.core.flights.flight.x10.FlightsDocument.Flights.Flight.FlightAttributes;
import com.mxi.xml.xsd.core.flights.flight.x20.FlightsDocument.Flights.Flight;
import com.mxi.xml.xsd.core.mxdomaintypes.x20.AircraftIdentifier;
import com.mxi.xml.xsd.core.mxdomaintypes.x20.FlightIdentifier;


/**
 * This class tests the flight modified trigger.
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class ModifiedFlightTriggerTest implements MxAfterTrigger<UUID> {

   private static Boolean triggerCalled;
   private static final String EXTERNAL_KEY = "EXT123";
   private static final String FLIGHT_NAME = "FLIGHT123";
   private static final String AIRCRAFT_BARCODE = "I000ACKH";
   private static final String ARRIVAL_AIRPORT = "YOW";
   private static final String DEPARTURE_AIRPORT = "YVR";
   private static final String SCHEDULED_DEPARTURE_DATE = "2020-01-11 10:45:00";
   private static final String SCHEDULED_ARRIVAL_DATE = "2020-01-11 11:30:00";

   // V1
   private FlightCoordinator flightCoordinatorV1;

   // V2
   private com.mxi.mx.core.adapter.flight.messages.flight.adapter.coordinator.v2.FlightCoordinator flightCoordinatorV2;

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule fakeGuiceDaoRule = new InjectionOverrideRule();


   @Before
   public void setUp() throws CacheException {
      DataLoaders.load( databaseConnectionRule.getConnection(), getClass() );
      GlobalParametersFake triggerParams =
            new GlobalParametersFake( ParmTypeEnum.TRIGGER_CACHE.name() );
      GlobalParameters.setInstance( triggerParams );
      flightCoordinatorV1 =
            new FlightCoordinator( new PlannedFlightCreator( new MxFlightFacade() ) );
      flightCoordinatorV2 =
            new com.mxi.mx.core.adapter.flight.messages.flight.adapter.coordinator.v2.FlightCoordinator(
                  new com.mxi.mx.core.adapter.flight.messages.flight.adapter.logic.flightcreator.v2.PlannedFlightCreator(
                        new com.mxi.mx.core.adapter.flight.messages.flight.adapter.facade.v2.MxFlightFacade() ) );
      triggerCalled = false;
   }


   /**
    *
    * Test the V1 Flight Modified Trigger. Test will cancel a flight to invoke the trigger
    *
    * @throws IntegrationException
    */
   @Test
   public void testFlightModifiedV1Trigger() throws IntegrationException {

      // Build Flight Document
      FlightsDocument lFlightsDocument = FlightsDocument.Factory.newInstance();
      Flights lFlights = lFlightsDocument.addNewFlights();
      com.mxi.xml.xsd.core.flights.flight.x10.FlightsDocument.Flights.Flight lFlight =
            lFlights.addNewFlight();
      // Flight
      lFlight.setProcessAsHistoric( false );
      lFlight.setMarkInError( true );

      // Flight Identifier
      FlightIdentifier lFlightIdentifier = lFlight.addNewFlightIdentifier();
      lFlightIdentifier.setExternalKey( EXTERNAL_KEY );

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
      lDates.setScheduledArrivalDate( SCHEDULED_ARRIVAL_DATE );
      lDates.setScheduledDepartureDate( SCHEDULED_DEPARTURE_DATE );

      flightCoordinatorV1.coordinate( lFlightsDocument );
      assertTrue( "MX_FL_MODIFIED trigger was not called", triggerCalled );

   }


   /**
    *
    * Test the V2 Flight Modified Trigger. Test will cancel a flight to invoke the trigger
    *
    * @throws IntegrationException
    */
   @Test
   public void testFlightModifiedV2Trigger() throws IntegrationException {
      // Build Flight Document
      com.mxi.xml.xsd.core.flights.flight.x20.FlightsDocument lFlightsDocument =
            com.mxi.xml.xsd.core.flights.flight.x20.FlightsDocument.Factory.newInstance();
      com.mxi.xml.xsd.core.flights.flight.x20.FlightsDocument.Flights lFlights =
            lFlightsDocument.addNewFlights();
      Flight lFlight = lFlights.addNewFlight();

      // Flight element
      lFlight.setMarkInError( true );

      // Flight Identifier
      com.mxi.xml.xsd.core.flights.flight.x20.FlightsDocument.Flights.Flight.FlightIdentifier lFlightIdentifier =
            lFlight.addNewFlightIdentifier();
      lFlightIdentifier.setExternalKey( EXTERNAL_KEY );

      // Flight Attributes
      com.mxi.xml.xsd.core.flights.flight.x20.FlightsDocument.Flights.Flight.FlightAttributes lFlightAttributes =
            lFlight.addNewFlightAttributes();
      lFlightAttributes.setName( FLIGHT_NAME );

      // Aircraft Identifier
      com.mxi.xml.xsd.core.flights.flight.x20.FlightsDocument.Flights.Flight.AircraftIdentifier lAircraftIdentifier =
            lFlight.addNewAircraftIdentifier();
      lAircraftIdentifier.setBarcode( AIRCRAFT_BARCODE );

      // Airports
      com.mxi.xml.xsd.core.flights.flight.x20.FlightsDocument.Flights.Flight.Airports lAirports =
            lFlight.addNewAirports();
      lAirports.setArrivalAirport( ARRIVAL_AIRPORT );
      lAirports.setDepartureAirport( DEPARTURE_AIRPORT );

      // Dates
      com.mxi.xml.xsd.core.flights.flight.x20.FlightsDocument.Flights.Flight.Dates lDates =
            lFlight.addNewDates();
      lDates.setScheduledDepartureDate( SCHEDULED_DEPARTURE_DATE );
      lDates.setScheduledArrivalDate( SCHEDULED_ARRIVAL_DATE );

      flightCoordinatorV2.coordinate( lFlightsDocument );
      assertTrue( "MX_FL_MODIFIED trigger was not called", triggerCalled );
   }


   /**
    * This method gets called when the trigger is invoked
    */
   @Override
   public void after( UUID flightLegId ) throws TriggerException {
      assertFalse( "Trigger has been called more than once", triggerCalled );
      triggerCalled = true;
   }
}
