package com.mxi.am.api.resource.materials.tracking.shipment.historynote;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mxi.am.api.helper.Credentials;
import com.mxi.am.api.resource.ObjectMapperFactory;
import com.mxi.am.api.resource.materials.tracking.shipment.Shipment;
import com.mxi.am.api.resource.materials.tracking.shipment.ShipmentLine;
import com.mxi.am.driver.common.database.DatabaseDriver;
import com.mxi.am.driver.common.database.Result;
import com.mxi.am.guice.AssetManagementDatabaseDriverProvider;

import io.restassured.RestAssured;
import io.restassured.response.Response;


/**
 * Shipment History Note API Test
 *
 */
public class ShipmentHistoryNoteResourceBeanTest {

   private static final String APPLICATION_JSON = "application/json";
   private ShipmentHistoryNotePathBuilder shipmentHistoryNotePathBuilder;
   private static final String AMAPI = "/amapi/";
   private static final String SHIPMENT_PATH = "/amapi/" + Shipment.PATH;

   private DatabaseDriver driver;
   private ObjectMapper objectMapper =
         ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
   private static final String USERNAME = "mxi";
   private static final String FROM_LOCATION_CODE = "AIRPORT1/DOCK";
   private static final String TO_LOCATION_CODE = "AIRPORT2/DOCK";
   private static final String PART_NO = "A0000002";

   private static final String USER_QUERY = "SELECT alt_id FROM utl_user WHERE username = ?";
   private static final String LOCATION_QUERY = "SELECT alt_id FROM inv_loc WHERE loc_cd = ?";
   private static final String PART_QUERY = "SELECT alt_id FROM eqp_part_no WHERE part_no_oem = ?";

   // Shipment data
   private Shipment shipment;
   private String shipFromId;
   private static final String PRIORITY = "NORMAL";
   private static final Date SHIP_BY_DATE = new Date();
   private static final String TYPE = "STKTRN";
   private static final String STATUS = "IXPEND";
   private static final BigDecimal QUANTITY = new BigDecimal( "1" );

   // Shipment history note data
   private String shipmentHistoryNoteId;
   private String shipmentId;
   private String userId;
   private String shipToId;
   private String partId;
   private static final String HISTORY_NOTE = "Application Test for Shipment History Note";
   private static final String HISTORY_NOTE_STATUS_CODE = "IXCMPLT";
   private static final String CREATE_API_HISTORY_NOTE =
         "Shipment History Note Create API Application Test";


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

      driver = new AssetManagementDatabaseDriverProvider().get();

      Result userResult = driver.select( USER_QUERY, USERNAME );
      Result fromLocationResult = driver.select( LOCATION_QUERY, FROM_LOCATION_CODE );
      Result toLocationResult = driver.select( LOCATION_QUERY, TO_LOCATION_CODE );
      Result partIdResult = driver.select( PART_QUERY, PART_NO );

      if ( userResult.isEmpty() ) {
         fail( "Could not find the User details of" + USERNAME );
      }

      if ( userResult.getNumberOfRows() > 1 ) {
         fail( "Expecting a single row but found " + userResult.getNumberOfRows() );
      }

      if ( fromLocationResult.isEmpty() ) {
         fail( "Could not find the location with code: " + FROM_LOCATION_CODE );
      }

      if ( fromLocationResult.getNumberOfRows() > 1 ) {
         fail( "Expecting a single row but found " + fromLocationResult.getNumberOfRows() );
      }

      if ( toLocationResult.isEmpty() ) {
         fail( "Could not find the location with code: " + TO_LOCATION_CODE );
      }

      if ( toLocationResult.getNumberOfRows() > 1 ) {
         fail( "Expecting a single row but found " + toLocationResult.getNumberOfRows() );
      }

      if ( partIdResult.isEmpty() ) {
         fail( "Could not find a Part with the Part No: " + PART_NO );
      }

      if ( partIdResult.getNumberOfRows() > 1 ) {
         fail( "Expecting a single row but found " + partIdResult.getNumberOfRows() );
      }

      shipFromId = fromLocationResult.get( 0 ).getUuidString( "alt_id" );
      userId = userResult.get( 0 ).getUuidString( "alt_id" );
      partId = partIdResult.get( 0 ).getUuidString( "alt_id" );
      shipToId = toLocationResult.get( 0 ).getUuidString( "alt_id" );

      shipmentHistoryNotePathBuilder = new ShipmentHistoryNotePathBuilder();
      prepareShipmentData();

   }


   @Test
   public void testGetShipmentHistoryNoteByIdSuccessReturns200()
         throws JsonParseException, JsonMappingException, IOException {
      Response response = get( 200, Credentials.AUTHENTICATED, shipmentHistoryNoteId,
            APPLICATION_JSON, APPLICATION_JSON );
      assertShipmentHistoryNote( shipmentHistoryNoteBuilder(), response );
   }


   @Test
   public void testGetShipmentHistoryNoteByIdUnauthenticatedReturns401() {
      get( 401, Credentials.UNAUTHENTICATED, shipmentHistoryNoteId, APPLICATION_JSON,
            APPLICATION_JSON );
   }


   @Test
   public void testGetShipmentHistoryNoteByIdUnauthorizedReturns403() {
      get( 403, Credentials.UNAUTHORIZED, shipmentHistoryNoteId, APPLICATION_JSON,
            APPLICATION_JSON );
   }


   @Test
   public void testSearchShipmentHistoryNoteSuccessReturns200()
         throws JsonParseException, JsonMappingException, IOException {
      Response actualShipmentHistoryNoteResponse = search( 200, Credentials.AUTHENTICATED,
            shipmentId, APPLICATION_JSON, APPLICATION_JSON );
      List<ShipmentHistoryNote> actualShipmentHistoryNote = objectMapper.readValue(
            actualShipmentHistoryNoteResponse.getBody().asString(), objectMapper.getTypeFactory()
                  .constructCollectionType( List.class, ShipmentHistoryNote.class ) );
      Assert.assertEquals(
            "Expected History Note is not found. " + actualShipmentHistoryNote.size(), 2,
            actualShipmentHistoryNote.size() );
      Assert.assertTrue(
            "Received Shipment History Note List does not contain the created History Note",
            actualShipmentHistoryNote.stream()
                  .anyMatch( t -> t.getNote().equals( HISTORY_NOTE ) ) );
   }


   @Test
   public void testSearchShipmentHistoryNoteUnauthenticatedReturns401() {
      search( 401, Credentials.UNAUTHENTICATED, shipmentId, APPLICATION_JSON, APPLICATION_JSON );
   }


   @Test
   public void testSearchShipmentHistoryNoteUnauthorizedReturns403() {
      search( 403, Credentials.UNAUTHORIZED, shipmentId, APPLICATION_JSON, APPLICATION_JSON );
   }


   @Test
   public void testCreateShipmentHistoryNoteSuccessReturns200()
         throws JsonParseException, JsonMappingException, IOException {
      ShipmentHistoryNote shipmentHistoryNote = shipmentHistoryNoteBuilder();
      shipmentHistoryNote.setNote( CREATE_API_HISTORY_NOTE );
      Response response = create( 200, Credentials.AUTHENTICATED, shipmentHistoryNote,
            APPLICATION_JSON, APPLICATION_JSON );
      assertShipmentHistoryNote( shipmentHistoryNote, response );
   }


   @Test
   public void testCreateShipmentHistoryNoteUnauthenticatedReturns401() {
      ShipmentHistoryNote shipmentHistoryNote = shipmentHistoryNoteBuilder();
      shipmentHistoryNote.setNote( CREATE_API_HISTORY_NOTE );
      create( 401, Credentials.UNAUTHENTICATED, shipmentHistoryNote, APPLICATION_JSON,
            APPLICATION_JSON );
   }


   @Test
   public void testCreateShipmentHistoryNoteUnauthorizedReturns403() {
      ShipmentHistoryNote shipmentHistoryNote = shipmentHistoryNoteBuilder();
      shipmentHistoryNote.setNote( CREATE_API_HISTORY_NOTE );
      create( 403, Credentials.UNAUTHORIZED, shipmentHistoryNote, APPLICATION_JSON,
            APPLICATION_JSON );
   }


   private Response get( int statusCode, Credentials security, String shipmentHistoryNoteId,
         String contentType, String acceptType ) {

      String username = security.getUserName();
      String password = security.getPassword();
      String shipmentHistoryNoteGetPath =
            AMAPI + shipmentHistoryNotePathBuilder.get( shipmentHistoryNoteId ).build();

      Response response = RestAssured.given().auth().preemptive().basic( username, password )
            .accept( acceptType ).contentType( contentType ).expect().statusCode( statusCode )
            .when().get( shipmentHistoryNoteGetPath );
      return response;
   }


   private Response search( int statusCode, Credentials security, String shipmentId,
         String acceptType, String contentType ) {

      String username = security.getUserName();
      String password = security.getPassword();
      ShipmentHistoryNoteSearchParameters shipmentHistoryNoteSearchParameters =
            new ShipmentHistoryNoteSearchParameters();
      shipmentHistoryNoteSearchParameters.setShipmentId( shipmentId );
      String shipmentHistoryNoteSearchPath = AMAPI + shipmentHistoryNotePathBuilder.search()
            .searchBy( shipmentHistoryNoteSearchParameters ).build();

      Response response = RestAssured.given().auth().preemptive().basic( username, password )
            .accept( acceptType ).contentType( contentType ).expect().statusCode( statusCode )
            .when().get( shipmentHistoryNoteSearchPath );
      return response;
   }


   private Response create( int statusCode, Credentials security,
         ShipmentHistoryNote shipmentHistoryNote, String acceptType, String contentType ) {

      String username = security.getUserName();
      String password = security.getPassword();
      String shipmentHistoryNotePostPath = AMAPI + shipmentHistoryNotePathBuilder.post().build();

      Response response = RestAssured.given().auth().preemptive().basic( username, password )
            .accept( acceptType ).contentType( contentType ).body( shipmentHistoryNote ).expect()
            .statusCode( statusCode ).when().post( shipmentHistoryNotePostPath );
      return response;
   }


   private void assertShipmentHistoryNote( ShipmentHistoryNote expectedShipmentHistoryNote,
         Response actualShipmentHistoryNoteResponse )
         throws JsonParseException, JsonMappingException, IOException {

      ShipmentHistoryNote actualShipmentHistoryNote =
            objectMapper.readValue( actualShipmentHistoryNoteResponse.getBody().asString(),
                  objectMapper.getTypeFactory().constructType( ShipmentHistoryNote.class ) );
      expectedShipmentHistoryNote.setId( actualShipmentHistoryNote.getId() );
      expectedShipmentHistoryNote.setDate( actualShipmentHistoryNote.getDate() );

      Assert.assertEquals(
            "Received Shipment History Note is not the same as Expected Shipment History Note for GET API",
            expectedShipmentHistoryNote, actualShipmentHistoryNote );
   }


   public void prepareShipmentData()
         throws JsonParseException, JsonMappingException, IOException, ParseException {

      // Create the Shipment
      shipment = shipmentBuilder();
      Response shipmentResponse = createShipment( 200, Credentials.AUTHENTICATED, shipment,
            APPLICATION_JSON, APPLICATION_JSON );
      Shipment shipmentCreated =
            objectMapper.readValue( shipmentResponse.getBody().asString(), Shipment.class );

      // Then create the Shipment History Note
      shipmentId = shipmentCreated.getId();
      ShipmentHistoryNote shipmentHistoryNote = shipmentHistoryNoteBuilder();
      Response shipmentHistoryNoteresponse = create( 200, Credentials.AUTHENTICATED,
            shipmentHistoryNote, APPLICATION_JSON, APPLICATION_JSON );
      ShipmentHistoryNote shipmentHistoryNoteCreated = objectMapper.readValue(
            shipmentHistoryNoteresponse.getBody().asString(), ShipmentHistoryNote.class );

      shipmentHistoryNoteId = shipmentHistoryNoteCreated.getId();

   }


   private Response createShipment( int statusCode, Credentials security, Shipment shipment,
         String acceptType, String contentType ) {

      String username = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().auth().preemptive().basic( username, password )
            .accept( acceptType ).contentType( contentType ).body( shipment ).expect()
            .statusCode( statusCode ).when().post( SHIPMENT_PATH );
      return response;
   }


   private ShipmentHistoryNote shipmentHistoryNoteBuilder() {
      ShipmentHistoryNote shipmentHistoryNote = new ShipmentHistoryNote();
      shipmentHistoryNote.setShipmentId( shipmentId );
      shipmentHistoryNote.setUserId( userId );
      shipmentHistoryNote.setNote( HISTORY_NOTE );
      shipmentHistoryNote.setStatusCode( HISTORY_NOTE_STATUS_CODE );
      shipmentHistoryNote.setDate( new Date() );

      return shipmentHistoryNote;
   }


   private Shipment shipmentBuilder() throws ParseException {
      Shipment shipment = new Shipment();
      shipment.setShipFromLocationId( shipFromId );
      shipment.setShipToLocationId( shipToId );
      DateFormat dateFormat = new SimpleDateFormat( "yyyy-mm-dd hh:mm:ss" );
      String shipByDateString = dateFormat.format( SHIP_BY_DATE );
      Date ShipByDate = dateFormat.parse( shipByDateString );
      shipment.setShipByDate( ShipByDate );
      shipment.setPriorityCode( PRIORITY );
      shipment.setTypeCode( TYPE );
      shipment.setStatusCode( STATUS );
      shipment.setHistoric( false );
      // Add a shipment line
      List<ShipmentLine> shipmentLines = new ArrayList<ShipmentLine>();
      ShipmentLine shipmentLine = new ShipmentLine();
      shipmentLine.setPartId( partId );
      shipmentLine.setQuantity( QUANTITY );
      shipmentLines.add( shipmentLine );
      shipment.setShipmentLines( shipmentLines );

      return shipment;
   }

}
