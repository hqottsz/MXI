package com.mxi.am.api.resource.maintenance.plan.flight;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mxi.am.api.annotation.CSIContractTest;
import com.mxi.am.api.annotation.CSIContractTest.Project;
import com.mxi.am.api.helper.Credentials;
import com.mxi.am.api.resource.ObjectMapperFactory;
import com.mxi.am.api.resource.maintenance.plan.flight.model.Flight;
import com.mxi.am.driver.common.database.ConvertByteArrayToUUIDString;
import com.mxi.am.driver.common.database.DatabaseDriver;
import com.mxi.am.driver.common.database.Row;
import com.mxi.am.driver.query.FlightQueriesDriver;
import com.mxi.am.driver.query.InventoryInfo;
import com.mxi.am.driver.query.InventoryQueriesDriver;
import com.mxi.am.guice.AssetManagementDatabaseDriverProvider;

import io.restassured.RestAssured;
import io.restassured.response.Response;


/**
 * Flight API Test
 *
 */
public class FlightResourceBeanTest {

   private final String applicationJson = "application/json";
   private final String flightPath = "/amapi/" + Flight.PATH;

   private static final String INSERT_FL_LEG =
         "INSERT INTO fl_leg (leg_id, leg_no, leg_desc, master_flight_no, ext_key, flight_leg_status_cd, flight_type_cd, aircraft_db_id, aircraft_id, departure_loc_db_id, departure_loc_id, departure_gate_cd, sched_departure_dt, actual_departure_dt, off_dt, arrival_loc_db_id, arrival_loc_id, arrival_gate_cd, sched_arrival_dt, actual_arrival_dt, on_dt) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
   private static final String INSERT_FL_REQUIREMENT =
         "INSERT INTO fl_requirement (fl_leg_id, cap_db_id, cap_cd, level_db_id, level_cd) VALUES (?, ?, ?, ?, ?)";

   private static final String flightLegUuid = "9CF19AFAFAF64993BB342C812C4E5EFC";
   private static final String ACTUAL_ARRIVAL_DATE = "08-22-2018 10:00:00";
   private static final String ACTUAL_ARRIVAL_8601 = "2018-08-22T14:00:00Z";
   private static final String ACTUAL_DEPARTURE_DATE = "08-22-2018 05:30:00";
   private static final String ACTUAL_DEPARTURE_8601 = "2018-08-22T09:30:00Z";
   private static final String ARR_LOC_ID = "100010";
   private static final String ARRIVAL_GATE = "A42";
   private static final String DEP_LOC_ID = "100005";
   private static final String DEPARTURE_GATE = "C19";
   private static final String EXTERNAL_REFERENCE_KEY = "0923MCOYUL20000923";
   private static final String FLIGHT_DESCRIPTION = "Test Flight";
   private static final String FLIGHT_NAME = "0923";
   private static final String MASTER_FLIGHT_NUMBER = "0923";
   private static final String OFF_DATE = "08-22-2018 04:45:00";
   private static final String ON_DATE = "08-22-2018 10:15:00";
   private static final String FLIGHT_PATH = "maintenance/plan/flight";
   private static final String SCHED_ARRIVAL_DATE = "08-22-2018 09:30:00";
   private static final String SCHED_DEPARTURE_DATE = "08-22-2018 04:00:00";
   private static final String SCHED_DEPARTURE_8601 = "2018-08-22T08:00:00Z";
   private static final String FLIGHT_STATUS = "MXPLAN";
   private static final String FLIGHT_TYPE = "Test";
   private static final String DATE_FORMAT = "MM-dd-yyyy HH:mm:ss";
   private static final String CAP_REQ_CODE = "ALAND";
   private static final String CAP_REQ_DESCRIPTION = "Autoland";
   private static final String CAP_REQ_LEVEL = "CATIII";
   private static final String CAP_REQ_LEVEL_DESCRIPTION = "Autoland Category 3";
   private static final String AIRCRAFT_SERIAL_NUMBER = "OPER-9088";
   private static final String PAGE = "1";
   private static final String SIZE = "9999";
   private static final String SCHED_DEPARTURE_START_8601 = "2018-08-21T16:00:00Z";
   private static final String SCHED_DEPARTURE_END_8601 = "2018-08-23T15:00:00Z";

   private static DatabaseDriver driver;

   private static InventoryQueriesDriver inventoryQueriesDriver;
   private static FlightQueriesDriver flightQueriesDriver;


   @BeforeClass
   public static void setUpClass() throws ParseException, ClassNotFoundException, SQLException {
      try {
         RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
         RestAssured.baseURI = "http://".concat( InetAddress.getLocalHost().getHostName() );

         RestAssured.port = 80;

      } catch ( UnknownHostException e ) {
         throw new RuntimeException( "Cannot find local host name." );
      }

      inventoryQueriesDriver = new InventoryQueriesDriver();
      flightQueriesDriver = new FlightQueriesDriver();

   }


   @Before
   public void setUpData() throws Exception {
      driver = new AssetManagementDatabaseDriverProvider().get();
      inventoryQueriesDriver.iDatabaseDriver = driver;
      flightQueriesDriver.iDatabaseDriver = driver;
      prepareFlight();
   }


   @After
   public void teardown() {
      flightQueriesDriver.removeFlightData( flightLegUuid );
   }


   @Test
   public void testGetFlightSuccess() throws ParseException {
      Response response =
            get( 200, Credentials.AUTHORIZED, flightLegUuid, applicationJson, applicationJson );
      assertFlight( response.getBody().as( Flight.class ) );
   }


   @Test
   public void testGetFlightUnauthenticated() throws ParseException {
      Response response = get( 401, Credentials.UNAUTHENTICATED, flightLegUuid, applicationJson,
            applicationJson );
   }


   @Test
   public void testGetFlightUnauthorized403() throws ParseException {
      get( 403, Credentials.UNAUTHORIZED, flightLegUuid, applicationJson, applicationJson );
   }


   @Test
   @CSIContractTest( { Project.SWA_FQC, Project.SWA_MOPP } )
   public void testSearchFlightSuccessAllParam()
         throws ParseException, JsonParseException, JsonMappingException, IOException {

      Response response = RestAssured.given()
            .queryParam( FlightSearchParameters.ACTUAL_ARRIVAL_DATE_PARAM, ACTUAL_ARRIVAL_8601 )
            .queryParam( FlightSearchParameters.ACTUAL_DEPART_DATE_PARAM, ACTUAL_DEPARTURE_8601 )
            .queryParam( FlightSearchParameters.DEPART_LOCATION_ID_PARAM,
                  getLocationAltId( DEP_LOC_ID ) )
            .queryParam( FlightSearchParameters.EXTERNAL_KEY_PARAM, EXTERNAL_REFERENCE_KEY )
            .queryParam( FlightSearchParameters.FLIGHT_NAME_PARAM, FLIGHT_NAME )
            .queryParam( FlightSearchParameters.FLIGHT_STATUS_PARAM,
                  Arrays.asList( FLIGHT_STATUS ) )
            .queryParam( FlightSearchParameters.PAGE_PARAM, PAGE )
            .queryParam( FlightSearchParameters.SCHED_DEPART_DATE_PARAM, SCHED_DEPARTURE_8601 )
            .queryParam( FlightSearchParameters.SCHED_DEPART_END_DATE, SCHED_DEPARTURE_END_8601 )
            .queryParam( FlightSearchParameters.SCHED_DEPART_START_DATE,
                  SCHED_DEPARTURE_START_8601 )
            .queryParam( FlightSearchParameters.SIZE_PARAM, SIZE ).auth().preemptive()
            .basic( Credentials.AUTHORIZED.getUserName(), Credentials.AUTHORIZED.getPassword() )
            .accept( applicationJson ).contentType( applicationJson ).expect().statusCode( 200 )
            .when().get( flightPath );

      ObjectMapper objectMapper =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      List<Flight> flights = objectMapper.readValue( response.getBody().asString(),
            objectMapper.getTypeFactory().constructCollectionType( List.class, Flight.class ) );
      flights.get( 0 ).setLastModifiedDate( null );// we are not checking LastModifiedDate in the
                                                   // e2e testing

      Assert.assertEquals( "No Flights returned by search", 1, flights.size() );
      assertFlight( flights.get( 0 ) );
   }


   @Test
   public void testSearchFlightNoParam() throws ParseException {
      Response response = RestAssured.given().auth().preemptive()
            .basic( Credentials.AUTHORIZED.getUserName(), Credentials.AUTHORIZED.getPassword() )
            .accept( applicationJson ).contentType( applicationJson ).expect().statusCode( 412 )
            .when().get( flightPath );
   }


   @Test
   public void testSearchFlightUnauthenticated() throws ParseException {
      Response response = RestAssured.given().auth().preemptive()
            .basic( Credentials.UNAUTHENTICATED.getUserName(),
                  Credentials.AUTHORIZED.getPassword() )
            .accept( applicationJson ).contentType( applicationJson ).expect().statusCode( 401 )
            .when().get( flightPath );
   }


   @Test
   public void testSearchFlightUnauthorized403() throws ParseException {
      RestAssured.given().auth().preemptive()
            .basic( Credentials.UNAUTHORIZED.getUserName(), Credentials.AUTHORIZED.getPassword() )
            .accept( applicationJson ).contentType( applicationJson ).expect().statusCode( 403 )
            .when().get( flightPath );
   }


   private Response get( int statusCode, Credentials security, String flightId, String contentType,
         String acceptType ) {
      String userName = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().auth().preemptive().basic( userName, password )
            .accept( acceptType ).contentType( contentType ).expect().statusCode( statusCode )
            .when().get( flightPath + "/" + flightId );
      return response;
   }


   private static void prepareFlight() throws ParseException {

      InventoryInfo aircraftInfo =
            inventoryQueriesDriver.getInventoryInfoByInventorySerialNo( AIRCRAFT_SERIAL_NUMBER );

      DateFormat format = new SimpleDateFormat( DATE_FORMAT );

      Assert.assertTrue( driver.insert( INSERT_FL_LEG, flightLegUuid, FLIGHT_NAME,
            FLIGHT_DESCRIPTION, MASTER_FLIGHT_NUMBER, EXTERNAL_REFERENCE_KEY, FLIGHT_STATUS,
            FLIGHT_TYPE, aircraftInfo.getDbId(), aircraftInfo.getId(), 4650, DEP_LOC_ID,
            DEPARTURE_GATE, format.parse( SCHED_DEPARTURE_DATE ),
            format.parse( ACTUAL_DEPARTURE_DATE ), format.parse( OFF_DATE ), 4650, ARR_LOC_ID,
            ARRIVAL_GATE, format.parse( SCHED_ARRIVAL_DATE ), format.parse( ACTUAL_ARRIVAL_DATE ),
            format.parse( ON_DATE ) ) == 1 );

      Assert.assertTrue( driver.insert( INSERT_FL_REQUIREMENT, flightLegUuid, 10, CAP_REQ_CODE, 10,
            CAP_REQ_LEVEL ) == 1 );

   }


   private void assertFlight( Flight flight ) throws ParseException {
      DateFormat format = new SimpleDateFormat( DATE_FORMAT );
      String aircraftId =
            inventoryQueriesDriver.getAltIdByAircraftSerialNo( AIRCRAFT_SERIAL_NUMBER );

      Assert.assertEquals( aircraftId, flight.getAircraftId() );
      Assert.assertEquals( format.parse( ACTUAL_ARRIVAL_DATE ), flight.getActualArrivalDate() );
      Assert.assertEquals( format.parse( ACTUAL_DEPARTURE_DATE ), flight.getActualDepartureDate() );
      Assert.assertEquals( getLocationCode( ARR_LOC_ID ), flight.getArrivalAirportCode() );
      Assert.assertEquals( getLocationName( ARR_LOC_ID ), flight.getArrivalAirportName() );
      Assert.assertEquals( ARRIVAL_GATE, flight.getArrivalGate() );
      Assert.assertEquals( getLocationCode( DEP_LOC_ID ), flight.getDepartureAirportCode() );
      Assert.assertEquals( getLocationName( DEP_LOC_ID ), flight.getDepartureAirportName() );
      Assert.assertEquals( DEPARTURE_GATE, flight.getDepartureGate() );
      Assert.assertEquals( EXTERNAL_REFERENCE_KEY, flight.getExtRefKey() );
      Assert.assertEquals( FLIGHT_DESCRIPTION, flight.getFlightDescription() );
      Assert.assertEquals( FLIGHT_NAME, flight.getFlightNo() );
      Assert.assertEquals( flightLegUuid, flight.getId() );
      Assert.assertEquals( MASTER_FLIGHT_NUMBER, flight.getMasterFlightNo() );
      Assert.assertEquals( format.parse( OFF_DATE ), flight.getOffDate() );
      Assert.assertEquals( format.parse( ON_DATE ), flight.getOnDate() );
      Assert.assertEquals( FLIGHT_PATH, flight.getPath() );
      Assert.assertEquals( format.parse( SCHED_ARRIVAL_DATE ), flight.getSchedArrivalDate() );
      Assert.assertEquals( format.parse( SCHED_DEPARTURE_DATE ), flight.getSchedDepartureDate() );
      Assert.assertEquals( FLIGHT_STATUS, flight.getStatus() );
      Assert.assertEquals( FLIGHT_TYPE, flight.getType() );
      Assert.assertEquals( CAP_REQ_CODE, flight.getCapabilityRequirements().get( 0 ).getCode() );
      Assert.assertEquals( CAP_REQ_DESCRIPTION,
            flight.getCapabilityRequirements().get( 0 ).getDescription() );
      Assert.assertEquals( CAP_REQ_LEVEL,
            flight.getCapabilityRequirements().get( 0 ).getLevelCode() );
      Assert.assertEquals( CAP_REQ_LEVEL_DESCRIPTION,
            flight.getCapabilityRequirements().get( 0 ).getLevelDescription() );
   }


   private static String getLocationAltId( String locationId ) {
      Row row = driver.select( "SELECT alt_id FROM inv_loc WHERE loc_id = ?", locationId ).get( 0 );
      return ConvertByteArrayToUUIDString
            .convertByteArrayToUUIDString( ( byte[] ) row.get( "alt_id" ) );
   }


   private static String getLocationCode( String locationId ) {
      Row row = driver.select( "SELECT loc_cd FROM inv_loc WHERE loc_id = ?", locationId ).get( 0 );
      return row.get( "loc_cd" );
   }


   private static String getLocationName( String locationId ) {
      Row row =
            driver.select( "SELECT loc_name FROM inv_loc WHERE loc_id = ?", locationId ).get( 0 );
      return row.get( "loc_name" );
   }
}
