package com.mxi.am.api.resource.materials.plan.partrequest;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mxi.am.api.annotation.CSIContractTest;
import com.mxi.am.api.annotation.CSIContractTest.Project;
import com.mxi.am.api.exception.AmApiBusinessException;
import com.mxi.am.api.helper.Credentials;
import com.mxi.am.api.resource.ObjectMapperFactory;
import com.mxi.am.driver.common.database.DatabaseDriver;
import com.mxi.am.driver.common.database.Result;
import com.mxi.am.guice.AssetManagementDatabaseDriverProvider;

import io.restassured.RestAssured;
import io.restassured.response.Response;


/**
 * Part Request API test
 *
 */
public class PartRequestResourceBeanTest {

   private static final String APPLICATION_JSON = "application/json";
   private static final String PART_REQUEST_PATH = "/amapi/" + PartRequest.PATH;

   private static final String REQUEST_PRIORITY = "AOG";
   private static final String NEEDED_QUANTITY = "1";
   private static final String WHERE_NEEDED = "GBLDOCK";
   private static final String ISSUE_TO_ACCOUNT = "5";
   private static final String NEEDED_BY_DATE = "2007-01-21T05:00:00Z";
   private static final String PART_NO_OEM = "APU_ASSY_PN2";

   private static final String INVALID_PART_ALT_ID = "D93802FA3B514D1B9064D8D4B6328D90";
   private static final String NOTE = "Updated part request";

   private DatabaseDriver driver;

   private PartRequest partRequest;
   private String partId;
   private String partRequestAltId;


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
   public void setUpData() throws ClassNotFoundException, SQLException, JsonParseException,
         JsonMappingException, IOException, ParseException {
      driver = new AssetManagementDatabaseDriverProvider().get();

      Result partIdResult =
            driver.select( "select * from eqp_part_no where part_no_oem =?", PART_NO_OEM );

      if ( partIdResult.isEmpty() ) {
         fail( "Could not find Part ID with Part No OEM: " + PART_NO_OEM );
      }

      partId = partIdResult.get( 0 ).getUuidString( "alt_id" );

      prepareData();
   }


   @Test
   public void testCreatePartRequestSuccessReturns200()
         throws JsonProcessingException, IOException, ParseException {
      partRequest = defaultPartRequestBuilder();
      Response response = createPartRequest( 200, Credentials.AUTHENTICATED, partRequest );
      assertPartRequestForCreate( partRequest, response );
   }


   @Test
   public void testCreatePartRequestWithNullPartIdReturns400()
         throws ParseException, AmApiBusinessException {
      partRequest = defaultPartRequestBuilder();
      partRequest.setPartId( null );
      createPartRequest( 400, Credentials.AUTHENTICATED, partRequest );
   }


   @Test
   public void testCreatePartRequestUnauthenticatedReturns401()
         throws JsonParseException, JsonMappingException, IOException {
      createPartRequest( 401, Credentials.UNAUTHENTICATED, partRequest );
   }


   @Test
   public void testCreatePartRequestUnauthorizedReturns403()
         throws JsonParseException, JsonMappingException, IOException {
      createPartRequest( 403, Credentials.UNAUTHORIZED, partRequest );
   }


   @Test
   @CSIContractTest( Project.UPS )
   public void testGetPartRequestByIdSuccessReturns200()
         throws JsonParseException, JsonMappingException, IOException {
      Response response = getPartRequestById( 200, Credentials.AUTHENTICATED, partRequestAltId );
      assertPartRequest( partRequest, response );
   }


   @Test
   public void testGetPartRequestByIdNotFoundReturns404() {
      getPartRequestById( 404, Credentials.AUTHORIZED, INVALID_PART_ALT_ID );
   }


   @Test
   public void testGetPartRequestByIdUnauthenticatedReturns401()
         throws JsonParseException, JsonMappingException, IOException {
      getPartRequestById( 401, Credentials.UNAUTHENTICATED, partRequestAltId );
   }


   @Test
   public void testGetPartRequestByIdUnauthorizedReturns403()
         throws JsonParseException, JsonMappingException, IOException {
      getPartRequestById( 403, Credentials.UNAUTHORIZED, partRequestAltId );
   }


   @Test
   @CSIContractTest( Project.UPS )
   public void testSearchPartRequestSuccessReturns200()
         throws JsonParseException, JsonMappingException, IOException {
      Response response = searchPartRequest( 200, Credentials.AUTHORIZED );
      List<PartRequest> partRequests = response.jsonPath().getList( "", PartRequest.class );
      Assert.assertTrue( "The Part Request with barcode '" + partRequest.getBarcode()
            + "' was not in the response", partRequests.contains( partRequest ) );
   }


   @Test
   public void testSearchPartRequestUnauthenticatedReturns401()
         throws JsonParseException, JsonMappingException, IOException {
      searchPartRequest( 401, Credentials.UNAUTHENTICATED );
   }


   @Test
   public void testSearchPartRequestUnauthorizedReturns403()
         throws JsonParseException, JsonMappingException, IOException {
      searchPartRequest( 403, Credentials.UNAUTHORIZED );
   }


   @Test
   @CSIContractTest( Project.UPS )
   public void testUpdatePartRequestSuccessReturns200()
         throws JsonParseException, JsonMappingException, IOException {
      partRequest.setNote( NOTE );
      Response response =
            updatePartRequest( 200, Credentials.AUTHENTICATED, partRequestAltId, partRequest );
      assertPartRequest( partRequest, response );
   }


   @Test
   public void testUpdatePartRequestWithInvalidIdReturns404()
         throws JsonParseException, JsonMappingException, IOException {
      partRequest.setNote( NOTE );
      updatePartRequest( 404, Credentials.AUTHENTICATED, INVALID_PART_ALT_ID, partRequest );
   }


   @Test
   public void testUpdatePartRequestUnauthenticatedReturns401()
         throws JsonProcessingException, IOException {
      partRequest.setNote( NOTE );
      updatePartRequest( 401, Credentials.UNAUTHENTICATED, partRequestAltId, partRequest );
   }


   @Test
   public void testUpdatePartRequestUnauthorizedReturns403()
         throws JsonProcessingException, IOException {
      partRequest.setNote( NOTE );
      updatePartRequest( 403, Credentials.UNAUTHORIZED, partRequestAltId, partRequest );
   }


   private Response createPartRequest( int statusCode, Credentials security, Object partRequest ) {
      String username = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().auth().preemptive().basic( username, password )
            .accept( APPLICATION_JSON ).contentType( APPLICATION_JSON ).body( partRequest ).expect()
            .statusCode( statusCode ).when().post( PART_REQUEST_PATH );
      return response;
   }


   private Response getPartRequestById( int statusCode, Credentials security,
         String partRequestId ) {

      String username = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().auth().preemptive().basic( username, password )
            .accept( APPLICATION_JSON ).contentType( APPLICATION_JSON ).expect()
            .statusCode( statusCode ).when().get( PART_REQUEST_PATH + "/" + partRequestId );

      return response;
   }


   private Response searchPartRequest( int statusCode, Credentials security ) {
      String username = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given()
            .queryParam( PartRequestSearchParameters.PARAM_BARCODE, partRequest.getBarcode() )
            .auth().preemptive().basic( username, password ).accept( APPLICATION_JSON )
            .contentType( APPLICATION_JSON ).expect().statusCode( statusCode ).when()
            .get( PART_REQUEST_PATH );
      return response;
   }


   private Response updatePartRequest( int statusCode, Credentials security, String partRequestId,
         PartRequest partRequest ) {

      String username = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().auth().preemptive().basic( username, password )
            .accept( APPLICATION_JSON ).contentType( APPLICATION_JSON ).body( partRequest ).expect()
            .statusCode( statusCode ).when().put( PART_REQUEST_PATH + "/" + partRequestId );
      return response;
   }


   private PartRequest defaultPartRequestBuilder() throws ParseException {
      PartRequest partRequest = new PartRequest();
      partRequest.setPartId( partId );
      partRequest.setRequestPriority( REQUEST_PRIORITY );
      partRequest.setNeededQty( NEEDED_QUANTITY );
      partRequest.setWhereNeeded( WHERE_NEEDED );
      partRequest.setIssueToAccount( ISSUE_TO_ACCOUNT );
      Date neededByDate;
      try {
         neededByDate = new SimpleDateFormat( "yyyy-MM-dd" ).parse( NEEDED_BY_DATE );
      } catch ( ParseException e ) {
         throw ( e );
      }
      partRequest.setNeededByDate( neededByDate );

      return partRequest;
   }


   private void assertPartRequestForCreate( PartRequest partRequestExpected, Response actual )
         throws JsonProcessingException, IOException {

      ObjectMapper objectMapper =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      List<PartRequest> partRequestActuals =
            objectMapper.readValue( actual.getBody().asString(), objectMapper.getTypeFactory()
                  .constructCollectionType( List.class, PartRequest.class ) );

      PartRequest partRequestActual = partRequestActuals.get( 0 );

      partRequestExpected.setId( partRequestActual.getId() );
      partRequestExpected.setStatusCode( partRequestActual.getStatusCode() );
      partRequestExpected.setNote( partRequestActual.getNote() );
      partRequestExpected.setRequestType( partRequestActual.getRequestType() );
      partRequestExpected.setMasterId( partRequestActual.getMasterId() );
      partRequestExpected.setBarcode( partRequestActual.getBarcode() );
      partRequestExpected.setUserStatusCode( partRequestActual.getUserStatusCode() );
      partRequestExpected.setRequirementInstalledPartKeyId(
            partRequestActual.getRequirementInstalledPartKeyId() );
      partRequestExpected.setAircraftId( partRequestActual.getAircraftId() );

      Assert.assertEquals( "Incorrect created Part Request: ", partRequestExpected,
            partRequestActual );
   }


   private void assertPartRequest( PartRequest partRequestExpected, Response actual )
         throws JsonProcessingException, IOException {

      PartRequest partRequestActual = ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY
            .createObjectMapper().readValue( actual.getBody().asString(), PartRequest.class );

      partRequestExpected.setId( partRequestActual.getId() );
      partRequestExpected.setStatusCode( partRequestActual.getStatusCode() );
      partRequestExpected.setNote( partRequestActual.getNote() );
      partRequestExpected.setRequestType( partRequestActual.getRequestType() );
      partRequestExpected.setMasterId( partRequestActual.getMasterId() );
      partRequestExpected.setBarcode( partRequestActual.getBarcode() );
      partRequestExpected.setUserStatusCode( partRequestActual.getUserStatusCode() );
      partRequestExpected.setRequirementInstalledPartKeyId(
            partRequestActual.getRequirementInstalledPartKeyId() );
      partRequestExpected.setAircraftId( partRequestActual.getAircraftId() );

      Assert.assertEquals( "Incorrect returned Part Request: ", partRequestExpected,
            partRequestActual );
   }


   private void prepareData()
         throws JsonParseException, JsonMappingException, IOException, ParseException {
      partRequest = defaultPartRequestBuilder();
      Response response = createPartRequest( 200, Credentials.AUTHENTICATED, partRequest );

      ObjectMapper objectMapper =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      List<PartRequest> partRequests =
            objectMapper.readValue( response.getBody().asString(), objectMapper.getTypeFactory()
                  .constructCollectionType( List.class, PartRequest.class ) );

      partRequest = partRequests.get( 0 );
      partRequestAltId = partRequest.getId();
   }
}
