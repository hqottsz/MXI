package com.mxi.am.api.resource.sys.refterm.flightreason;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mxi.am.api.helper.Credentials;
import com.mxi.am.api.resource.ObjectMapperFactory;
import com.mxi.am.driver.common.database.DatabaseDriver;
import com.mxi.am.guice.AssetManagementDatabaseDriverProvider;

import io.restassured.RestAssured;
import io.restassured.response.Response;


/**
 * FlightReason API test
 *
 */
public class FlightReasonResourceBeanTest {

   private final String applicationJson = "application/json";
   private final String flightReasonPath = "/amapi/" + FlightReason.PATH;

   private static final String FLIGHT_REASON_CODE_1 = "FLRSON1";
   private static final String DISPLAY_CODE_1 = "REASON 1";
   private static final String DISPLAY_NAME_1 = "Valid FL event reason 1";

   private static final String DISPLAY_DESCRIPTION = "Used for testing only";

   private static final String FLIGHT_REASON_CODE_2 = "FLRSON2";
   private static final String DISPLAY_CODE_2 = "REASON 2";
   private static final String DISPLAY_NAME_2 = "Valid FL event reason 2";

   private static FlightReason flightReason1;
   private static FlightReason flightReason2;

   private static final String INSERT_FLIGHT_REASON =
         "INSERT INTO ref_flight_reason (flight_reason_cd, display_code, display_name, display_desc, display_ord, bitmap_db_id, bitmap_tag, ctrl_db_id ) "
               + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

   private static DatabaseDriver databaseDriver;

   private static ObjectMapper objectMapper;


   @BeforeClass
   public static void setUpClass() throws Exception {
      try {
         RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
         RestAssured.baseURI = "http://".concat( InetAddress.getLocalHost().getHostName() );

         RestAssured.port = 80;

      } catch ( UnknownHostException e ) {
         throw new RuntimeException( "Cannot find local host name." );
      }

      objectMapper = ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();

      constructFlightReasonObjects();
      insertFlightReasonInDb();
   }


   @Test
   public void testSearchFlightReasonSuccessReturns200()
         throws JsonProcessingException, IOException {

      Response response =
            createSearch( 200, Credentials.AUTHENTICATED, applicationJson, applicationJson );
      assertFlightReasonForSearch( response );
   }


   @Test
   public void testSearchFlightReasonUnauthenticatedReturns401()
         throws JsonProcessingException, IOException {

      createSearch( 401, Credentials.UNAUTHENTICATED, applicationJson, applicationJson );
   }


   @Test
   public void testSearchFlightReasonUnauthorizedReturns403()
         throws JsonProcessingException, IOException {
      createSearch( 403, Credentials.UNAUTHORIZED, applicationJson, applicationJson );
   }


   @Test
   public void testGetByFlightReasonCodeSuccessReturns200()
         throws JsonProcessingException, IOException {

      Response response = createGetByFlightReasonCode( 200, Credentials.AUTHENTICATED,
            flightReason2.getReasonCode(), applicationJson, applicationJson );
      assertFlightReasonForGet( flightReason2, response );
   }


   @Test
   public void testGetByFlightReasonCodeUnauthenticatedReturns401()
         throws JsonProcessingException, IOException {

      createGetByFlightReasonCode( 401, Credentials.UNAUTHENTICATED, FLIGHT_REASON_CODE_2,
            applicationJson, applicationJson );
   }


   @Test
   public void testGetByFlightReasonCodeUnauthorizedReturns403()
         throws JsonProcessingException, IOException {

      createGetByFlightReasonCode( 403, Credentials.UNAUTHORIZED, FLIGHT_REASON_CODE_2,
            applicationJson, applicationJson );
   }


   private Response createGetByFlightReasonCode( int statusCode, Credentials security,
         String flightReasonCode, String contentType, String acceptType ) {
      String userName = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().pathParam( "FlightReasonCode", flightReasonCode )
            .auth().preemptive().basic( userName, password ).accept( acceptType )
            .contentType( contentType ).expect().statusCode( statusCode ).when()
            .get( flightReasonPath + "/{FlightReasonCode}" );
      return response;
   }


   private Response createSearch( int statusCode, Credentials security, String contentType,
         String acceptType ) {
      String userName = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().auth().preemptive().basic( userName, password )
            .accept( acceptType ).contentType( contentType ).expect().statusCode( statusCode )
            .when().get( flightReasonPath );
      return response;
   }


   private void assertFlightReasonForSearch( Response actual )
         throws JsonProcessingException, IOException {

      List<FlightReason> flightReasonResultList =
            objectMapper.readValue( actual.getBody().asString(), objectMapper.getTypeFactory()
                  .constructCollectionType( List.class, FlightReason.class ) );

      Assert.assertTrue(
            "Flight reason with code " + flightReason1.getReasonCode() + " was not in the list",
            flightReasonResultList.contains( flightReason1 ) );
      Assert.assertTrue(
            "Flight reason with code " + flightReason2.getReasonCode() + " was not in the list",
            flightReasonResultList.contains( flightReason2 ) );
      Assert.assertEquals( "Flight reason list size is not as expected", 2,
            flightReasonResultList.size() );

   }


   private void assertFlightReasonForGet( FlightReason flightReasonExpected, Response actual )
         throws JsonProcessingException, IOException {

      FlightReason flightReasonActual = objectMapper.readValue( actual.getBody().asString(),
            objectMapper.getTypeFactory().constructType( FlightReason.class ) );
      Assert.assertEquals( flightReasonExpected, flightReasonActual );

   }


   private static void constructFlightReasonObjects() {
      flightReason1 = new FlightReason();
      flightReason1.setReasonCode( FLIGHT_REASON_CODE_1 );
      flightReason1.setDisplayCode( DISPLAY_CODE_1 );
      flightReason1.setDisplayName( DISPLAY_NAME_1 );
      flightReason1.setDisplayDescription( DISPLAY_DESCRIPTION );

      flightReason2 = new FlightReason();
      flightReason2.setReasonCode( FLIGHT_REASON_CODE_2 );
      flightReason2.setDisplayCode( DISPLAY_CODE_2 );
      flightReason2.setDisplayName( DISPLAY_NAME_2 );
      flightReason2.setDisplayDescription( DISPLAY_DESCRIPTION );
   }


   private static void insertFlightReasonInDb() throws Exception {
      databaseDriver = new AssetManagementDatabaseDriverProvider().get();

      databaseDriver.insert( INSERT_FLIGHT_REASON, FLIGHT_REASON_CODE_1, DISPLAY_CODE_1,
            DISPLAY_NAME_1, DISPLAY_DESCRIPTION, 100, 0, 42, 4650 );

      databaseDriver.insert( INSERT_FLIGHT_REASON, FLIGHT_REASON_CODE_2, DISPLAY_CODE_2,
            DISPLAY_NAME_2, DISPLAY_DESCRIPTION, 101, 0, 42, 4650 );

   }

}
