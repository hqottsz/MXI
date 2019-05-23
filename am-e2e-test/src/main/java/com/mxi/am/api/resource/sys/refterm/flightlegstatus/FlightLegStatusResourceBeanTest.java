package com.mxi.am.api.resource.sys.refterm.flightlegstatus;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mxi.am.api.helper.Credentials;
import com.mxi.am.api.resource.ObjectMapperFactory;

import io.restassured.RestAssured;
import io.restassured.response.Response;


/**
 * FlightLegStatus API test
 *
 */
public class FlightLegStatusResourceBeanTest {

   private final String iApplicationJson = "application/json";
   private final String iFlightLegStatusPath = "/amapi/" + FlightLegStatus.PATH;

   private static final String FLIGHT_LEG_STATUS_CODE1 = "MXPLAN";
   private static final String STATUS_DISPLAY_CODE1 = "PLAN";
   private static final String DISPLAY_NAME1 = "Planned";
   private static final String DISPLAY_DESCRIPTION1 = "Flight has been planned";

   private static final String FLIGHT_LEG_STATUS_CODE2 = "MXIN";
   private static final String STATUS_DISPLAY_CODE2 = "IN";
   private static final String DISPLAY_NAME2 = "At Gate";
   private static final String DISPLAY_DESCRIPTION2 = "Flight time stops";

   private FlightLegStatus iFlightLegStatus1;
   private FlightLegStatus iFlightLegStatus2;


   @BeforeClass
   public static void setUpClass() {
      try {
         RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
         RestAssured.baseURI = "http://".concat( InetAddress.getLocalHost().getHostName() );

         RestAssured.port = 80;

      } catch ( UnknownHostException e ) {
         throw new RuntimeException( "Cannot find local host name." );
      }

   }


   @Before
   public void setUpData() throws Exception {
      constructFlightLegStatusObject();

   }


   @Test
   public void testSearchFlightLegStatusSuccessReturns200()
         throws JsonProcessingException, IOException {

      Response lResponse =
            createSearch( 200, Credentials.AUTHENTICATED, iApplicationJson, iApplicationJson );
      assertFlightLegStatusForSearch( lResponse );
   }


   @Test
   public void testSearchFlightLegStatusUnauthenticatedReturns401()
         throws JsonProcessingException, IOException {

      createSearch( 401, Credentials.UNAUTHENTICATED, iApplicationJson, iApplicationJson );
   }


   @Test
   public void testSearchFlightLegStatusUnauthorizedReturns403()
         throws JsonProcessingException, IOException {
      createSearch( 403, Credentials.UNAUTHORIZED, iApplicationJson, iApplicationJson );
   }


   @Test
   public void testGetByFlightLegStatusCodeSuccessReturns200()
         throws JsonProcessingException, IOException {

      Response lResponse = createGetByFlightLegStatusCode( 200, Credentials.AUTHENTICATED,
            FLIGHT_LEG_STATUS_CODE2, iApplicationJson, iApplicationJson );
      assertFlightLegStatusForGet( iFlightLegStatus2, lResponse );
   }


   @Test
   public void testGetByFlightLegStatusCodeUnauthenticatedReturns401()
         throws JsonProcessingException, IOException {

      createGetByFlightLegStatusCode( 401, Credentials.UNAUTHENTICATED, FLIGHT_LEG_STATUS_CODE2,
            iApplicationJson, iApplicationJson );
   }


   @Test
   public void testGetByFlightLegStatusCodeUnauthorizedReturns403()
         throws JsonProcessingException, IOException {

      createGetByFlightLegStatusCode( 403, Credentials.UNAUTHORIZED, FLIGHT_LEG_STATUS_CODE2,
            iApplicationJson, iApplicationJson );
   }


   private Response createGetByFlightLegStatusCode( int aStatusCode, Credentials aSecurity,
         String aFlightLegStatusCode, String aContentType, String aAcceptType ) {
      String lUserName = aSecurity.getUserName();
      String lPassword = aSecurity.getPassword();

      Response lResponse =
            RestAssured.given().pathParam( "FlightLegStatusCode", aFlightLegStatusCode ).auth()
                  .preemptive().basic( lUserName, lPassword ).accept( aAcceptType )
                  .contentType( aContentType ).expect().statusCode( aStatusCode ).when()
                  .get( iFlightLegStatusPath + "/{FlightLegStatusCode}" );
      return lResponse;
   }


   private Response createSearch( int aStatusCode, Credentials aSecurity, String aContentType,
         String aAcceptType ) {
      String lUserName = aSecurity.getUserName();
      String lPassword = aSecurity.getPassword();

      Response lResponse = RestAssured.given().auth().preemptive().basic( lUserName, lPassword )
            .accept( aAcceptType ).contentType( aContentType ).expect().statusCode( aStatusCode )
            .when().get( iFlightLegStatusPath );
      return lResponse;
   }


   private void assertFlightLegStatusForSearch( Response aActual )
         throws JsonProcessingException, IOException {
      boolean lIsContains = false;
      ObjectMapper lObjectMapper =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      List<FlightLegStatus> lFlightLegStatusResultList =
            lObjectMapper.readValue( aActual.getBody().asString(), lObjectMapper.getTypeFactory()
                  .constructCollectionType( List.class, FlightLegStatus.class ) );

      lIsContains = lFlightLegStatusResultList.contains( iFlightLegStatus1 );
      lIsContains = lIsContains && lFlightLegStatusResultList.contains( iFlightLegStatus2 );
      Assert.assertEquals( true, lIsContains );
      Assert.assertEquals( lFlightLegStatusResultList.size(), 13 );

   }


   private void assertFlightLegStatusForGet( FlightLegStatus aFlightLegStatusExpected,
         Response aActual ) throws JsonProcessingException, IOException {
      ObjectMapper lObjectMapper =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      FlightLegStatus lFlightLegStatusActual =
            lObjectMapper.readValue( aActual.getBody().asString(),
                  lObjectMapper.getTypeFactory().constructType( FlightLegStatus.class ) );
      Assert.assertEquals( aFlightLegStatusExpected, lFlightLegStatusActual );

   }


   private void constructFlightLegStatusObject() {
      iFlightLegStatus1 = new FlightLegStatus();
      iFlightLegStatus1.setStatusCode( FLIGHT_LEG_STATUS_CODE1 );
      iFlightLegStatus1.setStatusDisplayCode( STATUS_DISPLAY_CODE1 );
      iFlightLegStatus1.setDisplayName( DISPLAY_NAME1 );
      iFlightLegStatus1.setDisplayDescription( DISPLAY_DESCRIPTION1 );

      iFlightLegStatus2 = new FlightLegStatus();
      iFlightLegStatus2.setStatusCode( FLIGHT_LEG_STATUS_CODE2 );
      iFlightLegStatus2.setStatusDisplayCode( STATUS_DISPLAY_CODE2 );
      iFlightLegStatus2.setDisplayName( DISPLAY_NAME2 );
      iFlightLegStatus2.setDisplayDescription( DISPLAY_DESCRIPTION2 );
   }

}
