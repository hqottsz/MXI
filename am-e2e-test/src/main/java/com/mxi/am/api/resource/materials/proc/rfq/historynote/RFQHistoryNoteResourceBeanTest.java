package com.mxi.am.api.resource.materials.proc.rfq.historynote;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
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
import com.mxi.am.api.helper.Credentials;
import com.mxi.am.api.resource.ObjectMapperFactory;
import com.mxi.am.api.resource.materials.proc.rfq.RFQDefinition;
import com.mxi.am.driver.common.database.DatabaseDriver;
import com.mxi.am.driver.common.database.Result;
import com.mxi.am.guice.AssetManagementDatabaseDriverProvider;

import io.restassured.RestAssured;
import io.restassured.response.Response;


/**
 * Application Tests for RFQ History Note API
 *
 */
public class RFQHistoryNoteResourceBeanTest {

   private final static String APPLICATION_JSON = "application/json";
   private final static String RFQ_HISTORYNOTE_PATH = "/amapi/" + RFQHistoryNote.PATH;
   private final static String RFQ_DEFINITION_PATH = "/amapi/" + RFQDefinition.PATH;

   private final static String NOTE = "Generated task based on a task definition";
   private final static String CUSTOMER_CODE = "CTIRE";
   private final static String RESPOND_BY_DATE = "2007-01-11T05:00:00Z";
   private final static String PURCHASING_CONTACT = "mxi";
   private final static BigDecimal EXCHANGE_RATE = new BigDecimal( 1 );
   private final static String CURRENCY = "USD";

   private RFQHistoryNote defaultHistoryNote;
   private RFQDefinition defaultRfq;
   private DatabaseDriver driver;
   private String rfqId;
   private String userId;


   @BeforeClass
   public static void setUpClass() throws Exception {
      try {
         RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
         RestAssured.baseURI = "http://".concat( InetAddress.getLocalHost().getHostName() );

         RestAssured.port = 80;
      } catch ( UnknownHostException e ) {
         throw new RuntimeException( "Cannot find local host name" );
      }
   }


   @Before
   public void setupData() throws Exception {
      driver = new AssetManagementDatabaseDriverProvider().get();
      // Getting the User Id for the username
      String username = "mxi";
      Result userIdResult =
            driver.select( "select utl_user.alt_id from utl_user where username =?", username );
      if ( userIdResult.isEmpty() ) {
         fail( "Could not find a User with that Username: " + username );
      }
      userId = userIdResult.get( 0 ).getUuidString( "alt_id" );
      prepareDate();
   }


   public void prepareDate()
         throws ParseException, JsonParseException, JsonMappingException, IOException {
      // Create a RFQ definition
      RFQDefinition rfqDefinition = rfqDefinitionBuilder();
      Response responseRFQ = createRFQRequest( 200, Credentials.AUTHENTICATED, rfqDefinition );
      defaultRfq = constructRFQFromResponse( responseRFQ.getBody().asString() );

      // Create a RFQ history Note for the created RFQ
      RFQHistoryNote rfqHistoryNote = rfqHistoryNoteBuilder();
      rfqHistoryNote.setRfqId( defaultRfq.getId() );
      Response responseRFQHitsoryNote =
            createRFQHistoryNoteRequest( 200, Credentials.AUTHENTICATED, rfqHistoryNote );
      defaultHistoryNote =
            constructRFQHistoryNoteFromResponse( responseRFQHitsoryNote.getBody().asString() );
   }


   @Test
   public void testGetRFQHistoryNoteByIDSuccessReturns200()
         throws JsonProcessingException, IOException, ParseException {
      Response response = getByIdRequest( 200, Credentials.AUTHORIZED, defaultHistoryNote.getId() );
      RFQHistoryNote actualRfqHistoryNote =
            constructRFQHistoryNoteFromResponse( response.getBody().asString() );
      Assert.assertEquals( defaultHistoryNote, actualRfqHistoryNote );
   }


   @Test
   public void testGetRFQHistoryNoteByIDUnauthenticatedReturns401()
         throws JsonProcessingException, IOException {
      getByIdRequest( 401, Credentials.UNAUTHENTICATED, defaultHistoryNote.getId() );
   }


   @Test
   public void testGetRFQHistoryNoteByIDUnauthorizedReturns403()
         throws JsonProcessingException, IOException {
      getByIdRequest( 403, Credentials.UNAUTHORIZED, defaultHistoryNote.getId() );
   }


   @Test
   public void testSearchRFQHistoryNoteSuccessReturns200()
         throws JsonParseException, JsonMappingException, IOException {
      Response response = searchRequest( 200, Credentials.AUTHENTICATED,
            RFQHistoryNoteSearchParameters.RFQ_ID_PARAM );
      assertRFQHistoryNoteForSearch( response );
   }


   @Test
   public void testSearchRFQHistoryNoteUnauthenticatedReturns401() {
      searchRequest( 401, Credentials.UNAUTHENTICATED,
            RFQHistoryNoteSearchParameters.RFQ_ID_PARAM );
   }


   @Test
   public void testSearchRFQHistoryNoteUnauthorizedReturns403() {
      searchRequest( 403, Credentials.UNAUTHORIZED, RFQHistoryNoteSearchParameters.RFQ_ID_PARAM );
   }


   @Test
   public void testCreateRFQHistoryNoteSuccess200() throws Exception {

      RFQHistoryNote newRFQHistoryNote = rfqHistoryNoteBuilder();
      newRFQHistoryNote.setRfqId( defaultRfq.getId() );
      Response response =
            createRFQHistoryNoteRequest( 200, Credentials.AUTHENTICATED, newRFQHistoryNote );

      RFQHistoryNote createdRFQHistoryNote =
            constructRFQHistoryNoteFromResponse( response.getBody().asString() );
      assertRFQHistoryNoteForCreate( newRFQHistoryNote, createdRFQHistoryNote );

   }


   @Test
   public void testCreateRFQHistoryNoteWithInvalidAuthentication401()
         throws JsonParseException, JsonMappingException, IOException {
      RFQHistoryNote newRFQHistoryNote = new RFQHistoryNote();
      createRFQHistoryNoteRequest( 401, Credentials.UNAUTHENTICATED, newRFQHistoryNote );
   }


   @Test
   public void testCreateRFQHistoryNoteUnauthorizedReturns403()
         throws JsonParseException, JsonMappingException, IOException {
      RFQHistoryNote newRFQHistoryNote = new RFQHistoryNote();
      createRFQHistoryNoteRequest( 403, Credentials.UNAUTHORIZED, newRFQHistoryNote );
   }


   private Response getByIdRequest( int statusCode, Credentials security, String id ) {

      String username = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().auth().preemptive().basic( username, password )
            .accept( APPLICATION_JSON ).contentType( APPLICATION_JSON ).expect()
            .statusCode( statusCode ).when().get( RFQ_HISTORYNOTE_PATH + "/" + id );
      return response;
   }


   private Response searchRequest( int statusCode, Credentials security, String rfqIdParam ) {
      String username = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().queryParam( rfqIdParam, defaultRfq.getId() ).auth()
            .preemptive().basic( username, password ).accept( APPLICATION_JSON )
            .contentType( APPLICATION_JSON ).expect().statusCode( statusCode ).when()
            .get( RFQ_HISTORYNOTE_PATH );
      return response;
   }


   private Response createRFQHistoryNoteRequest( int statusCode, Credentials security,
         Object rfqHistoryNote ) {
      String userName = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().auth().preemptive().basic( userName, password )
            .accept( APPLICATION_JSON ).contentType( APPLICATION_JSON ).body( rfqHistoryNote )
            .expect().statusCode( statusCode ).when().post( RFQ_HISTORYNOTE_PATH );
      return response;
   }


   private Response createRFQRequest( int statusCode, Credentials security, Object rfq ) {
      String userName = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().auth().preemptive().basic( userName, password )
            .accept( APPLICATION_JSON ).contentType( APPLICATION_JSON ).body( rfq ).expect()
            .statusCode( statusCode ).when().post( RFQ_DEFINITION_PATH );
      return response;
   }


   private void assertRFQHistoryNoteForSearch( Response actualRFQHistoryNote )
         throws JsonParseException, JsonMappingException, IOException {
      boolean isContains = false;
      ObjectMapper objectMapper =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      List<RFQHistoryNote> rfqHistoryNoteActual =
            objectMapper.readValue( actualRFQHistoryNote.getBody().asString(), objectMapper
                  .getTypeFactory().constructCollectionType( List.class, RFQHistoryNote.class ) );
      isContains = rfqHistoryNoteActual.contains( defaultHistoryNote );
      Assert.assertTrue(
            "The RFQHistoryNote with the ID: " + defaultRfq.getId() + "was not in the response",
            isContains );
   }


   private RFQHistoryNote constructRFQHistoryNoteFromResponse( String responseBody )
         throws JsonParseException, JsonMappingException, IOException {
      ObjectMapper objectMapper =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      RFQHistoryNote rfqHistoryNote = objectMapper.readValue( responseBody,
            objectMapper.getTypeFactory().constructType( RFQHistoryNote.class ) );
      return rfqHistoryNote;
   }


   private RFQDefinition constructRFQFromResponse( String responseBody )
         throws JsonParseException, JsonMappingException, IOException {

      ObjectMapper objectMapper =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      RFQDefinition rfqDefinition = objectMapper.readValue( responseBody,
            objectMapper.getTypeFactory().constructType( RFQDefinition.class ) );

      return rfqDefinition;
   }


   private void assertRFQHistoryNoteForCreate( RFQHistoryNote expectedRFQHistoryNote,
         RFQHistoryNote actualRFQHistoryNote )
         throws JsonParseException, JsonMappingException, IOException {

      Assert.assertEquals( expectedRFQHistoryNote.getRfqId(), actualRFQHistoryNote.getRfqId() );
      Assert.assertEquals( expectedRFQHistoryNote.getNote(), actualRFQHistoryNote.getNote() );
      Assert.assertEquals( expectedRFQHistoryNote.getUserId(), actualRFQHistoryNote.getUserId() );
   }


   public String generateBarcode() {
      String barcode = String.valueOf( System.nanoTime() );
      return barcode.substring( 9, barcode.length() );
   }


   private RFQHistoryNote rfqHistoryNoteBuilder() {

      RFQHistoryNote rfqHistoryNote = new RFQHistoryNote();
      rfqHistoryNote.setRfqId( rfqId );
      rfqHistoryNote.setUserId( userId );
      rfqHistoryNote.setNote( NOTE );

      return rfqHistoryNote;
   }


   private RFQDefinition rfqDefinitionBuilder() throws ParseException {

      RFQDefinition rfqDefinition = new RFQDefinition();
      rfqDefinition.setCurrency( CURRENCY );
      rfqDefinition.setExchangeRate( EXCHANGE_RATE );
      rfqDefinition.setRfqNo( generateBarcode() );
      rfqDefinition.setCustomerCode( CUSTOMER_CODE );
      rfqDefinition.setPurchasingContact( PURCHASING_CONTACT );

      Date respondByDate = new SimpleDateFormat( "yyyy-MM-dd" ).parse( RESPOND_BY_DATE );
      rfqDefinition.setRespondByDate( respondByDate );

      return rfqDefinition;
   }
}
