package com.mxi.am.api.resource.sys.refterm.reqpriority;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mxi.am.api.helper.Credentials;
import com.mxi.am.api.resource.ObjectMapperFactory;

import io.restassured.RestAssured;
import io.restassured.response.Response;


/**
 * Application Tests for ReqPriority API
 *
 */
public class ReqPriorityResourceBeanTest {

   private static final String APPLICATION_JSON = "application/json";
   private static final String REQ_PRIORITY_PATH = "/amapi/" + ReqPriority.PATH;

   private static final String REQPRIORITY_NAME = "Aircraft on Ground";
   private static final String REQPRIORITY_CODE = "AOG";
   private static final int REQPRIORITY_ORDER = 10;
   private static final String REQPRIORITY_DESC =
         "Urgent demand since it is currently grounding the aircraft.";

   private static final String INVALID_REQPRIORITY_CODE = "ABC";


   @BeforeClass
   public static void setUpClass() {
      try {
         RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
         RestAssured.baseURI = "http://".concat( InetAddress.getLocalHost().getHostName() );
         RestAssured.port = 80;
      } catch ( UnknownHostException e ) {
         throw new RuntimeException( "Cannot find local host name" );
      }
   }


   @Test
   public void testGetReqPriorityByCodeSuccessReturns200()
         throws JsonParseException, JsonMappingException, IOException {
      Response response =
            createGetByCodeRequest( 200, Credentials.AUTHENTICATED, REQPRIORITY_CODE );
      assertReqPriorityForGet( buildExpectedReqPriority(), response );
   }


   @Test
   public void testGetReqPriorityByCodeInvalidCodeReturns404() {
      createGetByCodeRequest( 404, Credentials.AUTHENTICATED, INVALID_REQPRIORITY_CODE );
   }


   @Test
   public void testGetReqPriorityByCodeUnauthenticatedReturns401() {
      createGetByCodeRequest( 401, Credentials.UNAUTHENTICATED, REQPRIORITY_CODE );
   }


   @Test
   public void testGetReqPriorityByCodeUnauthorizedReturns403() {
      createGetByCodeRequest( 403, Credentials.UNAUTHORIZED, REQPRIORITY_CODE );
   }


   @Test
   public void testSearchReqPrioritySuccessReturns200()
         throws JsonProcessingException, IOException {
      Response response = createSearchRequest( 200, Credentials.AUTHENTICATED );
      assertReqPriorityForSearch( buildExpectedReqPriority(), response );
   }


   @Test
   public void testSearchReqPriorityUnauthenticatedReturns401() {
      createSearchRequest( 401, Credentials.UNAUTHENTICATED );
   }


   @Test
   public void testSearchReqPriorityUnauthorizedReturns403() {
      createSearchRequest( 403, Credentials.UNAUTHORIZED );
   }


   private Response createGetByCodeRequest( int statusCode, Credentials security,
         String reqPriorityCd ) {

      String username = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().auth().preemptive().basic( username, password )
            .accept( APPLICATION_JSON ).contentType( APPLICATION_JSON ).expect()
            .statusCode( statusCode ).when().get( REQ_PRIORITY_PATH + "/" + reqPriorityCd );
      return response;
   }


   private Response createSearchRequest( int statusCode, Credentials security ) {

      String username = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().auth().preemptive().basic( username, password )
            .accept( APPLICATION_JSON ).contentType( APPLICATION_JSON ).expect()
            .statusCode( statusCode ).when().get( REQ_PRIORITY_PATH + "/" );
      return response;
   }


   private void assertReqPriorityForGet( ReqPriority expectedReqPriority,
         Response actualReqPriority ) throws JsonParseException, JsonMappingException, IOException {

      ObjectMapper objectMapper =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      ReqPriority reqPriorityActual =
            objectMapper.readValue( actualReqPriority.getBody().asString(),
                  objectMapper.getTypeFactory().constructType( ReqPriority.class ) );
      Assert.assertEquals( expectedReqPriority, reqPriorityActual );
   }


   private void assertReqPriorityForSearch( ReqPriority expectedReqPriority,
         Response actualReqPriority ) throws JsonProcessingException, IOException {

      ObjectMapper objectMapper =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      List<ReqPriority> reqPrioritiesActual =
            objectMapper.readValue( actualReqPriority.getBody().asString(), objectMapper
                  .getTypeFactory().constructCollectionType( List.class, ReqPriority.class ) );
      Assert.assertTrue( "The response was empty", reqPrioritiesActual.size() > 0 );
      Assert.assertTrue(
            "The Req Priority with code " + expectedReqPriority.getCode()
                  + " was not in the response",
            reqPrioritiesActual.contains( expectedReqPriority ) );

   }


   private ReqPriority buildExpectedReqPriority() {
      ReqPriority reqPriority = new ReqPriority();
      reqPriority.setName( REQPRIORITY_NAME );
      reqPriority.setCode( REQPRIORITY_CODE );
      reqPriority.setDescription( REQPRIORITY_DESC );
      reqPriority.setOrder( REQPRIORITY_ORDER );
      return reqPriority;
   }

}
