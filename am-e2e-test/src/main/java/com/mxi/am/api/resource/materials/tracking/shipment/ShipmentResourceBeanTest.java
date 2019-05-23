package com.mxi.am.api.resource.materials.tracking.shipment;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.mxi.am.api.helper.Credentials;
import com.mxi.am.api.resource.ObjectMapperFactory;
import com.mxi.am.driver.common.database.DatabaseDriver;
import com.mxi.am.driver.common.database.Result;
import com.mxi.am.guice.AssetManagementDatabaseDriverProvider;

import io.restassured.RestAssured;
import io.restassured.response.Response;


/**
 * Shipment API test
 *
 */
public class ShipmentResourceBeanTest {

   private static final String APPLICATION_JSON = "application/json";
   private static final String SHIPMENT_PATH = "/amapi/" + Shipment.PATH;

   // Shipment Data
   private static final String PRIORITY = "NORMAL";
   private static final String TYPE = "STKTRN";
   private static final String STATUS = "IXPEND";
   private static final BigDecimal QUANTITY = new BigDecimal( "1" );
   private static final String CARRIER_NAME = "C3456";
   private static final String WAYBILL_SDESC = "B1234";
   private static final Double WEIGHT_QT = 5.0;
   private static final Date SHIP_BY_DATE = new Date();
   private Shipment shipment;
   private String shipmentAltId;
   private String shipFromId; // AIRPORT1/DOCK
   private String shipToId; // AIRPORT2/DOCK
   private String partId;
   private DatabaseDriver driver;


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

      String fromLocationCode = "AIRPORT1/DOCK";
      String toLocationCode = "AIRPORT2/DOCK";
      String partNo = "A0000002";

      Result fromLocationResult =
            driver.select( "SELECT alt_id FROM inv_loc WHERE loc_cd = ?", fromLocationCode );

      Result toLocationResult =
            driver.select( "SELECT alt_id FROM inv_loc WHERE loc_cd = ?", toLocationCode );

      Result partIdResult =
            driver.select( "SELECT alt_id FROM eqp_part_no WHERE part_no_oem = ?", partNo );

      if ( fromLocationResult.isEmpty() ) {
         fail( "Could not find the location with code: " + fromLocationCode );
      }

      if ( fromLocationResult.getNumberOfRows() > 1 ) {
         fail( "Expecting a single row but found " + fromLocationResult.getNumberOfRows() );
      }

      if ( toLocationResult.isEmpty() ) {
         fail( "Could not find the location with code: " + toLocationCode );
      }

      if ( toLocationResult.getNumberOfRows() > 1 ) {
         fail( "Expecting a single row but found " + toLocationResult.getNumberOfRows() );
      }

      if ( partIdResult.isEmpty() ) {
         fail( "Could not find a Part with the Part No: " + partNo );
      }

      shipFromId = fromLocationResult.get( 0 ).getUuidString( "alt_id" );
      shipToId = toLocationResult.get( 0 ).getUuidString( "alt_id" );
      partId = partIdResult.get( 0 ).getUuidString( "alt_id" );

      prepareData();
   }


   @Test
   public void testSearchShipmentSuccessReturns200()
         throws JsonParseException, JsonMappingException, IOException {
      Response response = search( 200, Credentials.AUTHORIZED );
      List<Shipment> shipmentList = response.jsonPath().getList( "", Shipment.class );
      Assert.assertTrue( "The Shipment with ID '" + shipment.getId() + "' was not in the response",
            shipmentList.contains( shipment ) );
   }


   @Test
   public void testShipmentSearchUnauthenticatedReturns401()
         throws JsonParseException, JsonMappingException, IOException {
      search( 401, Credentials.UNAUTHENTICATED );
   }


   @Test
   public void testShipmentSearchUnauthorizedReturns403()
         throws JsonParseException, JsonMappingException, IOException {
      search( 403, Credentials.UNAUTHORIZED );
   }


   @Test
   public void testCreateShipmentSuccessReturns200()
         throws JsonProcessingException, IOException, ParseException {
      shipment = defaultShipmentBuilder();
      Response response = create( 200, Credentials.AUTHENTICATED, shipment );
      assertShipment( shipment, response );
   }


   @Test
   public void testCreateShipmentUnauthorizedReturns403()
         throws JsonParseException, JsonMappingException, IOException {
      create( 403, Credentials.UNAUTHORIZED, shipment );
   }


   @Test
   public void testCreateShipmentUnauthenticatedReturns401()
         throws JsonMappingException, IOException {
      create( 401, Credentials.UNAUTHENTICATED, shipment );
   }


   @Test
   public void testGetByIDSuccessReturns200() throws JsonProcessingException, IOException {
      Response response = getById( 200, Credentials.AUTHORIZED, shipmentAltId );
      assertShipment( shipment, response );
   }


   @Test
   public void testGetByIDNotFoundReturns404() throws JsonProcessingException, IOException {
      String nonExistantId = "C6EA96E0635611E8B095593AA3B86334";
      getById( 404, Credentials.AUTHORIZED, nonExistantId );
   }


   @Test
   public void testGetByIDUnauthenticatedReturns401() throws JsonProcessingException, IOException {
      getById( 401, Credentials.UNAUTHENTICATED, shipmentAltId );
   }


   @Test
   public void testGetByIDUnauthorizedReturns403() throws JsonProcessingException, IOException {
      getById( 403, Credentials.UNAUTHORIZED, shipmentAltId );
   }


   @Test
   public void testUpdateShipmentStatusSuccess200() throws JsonProcessingException, IOException {
      shipment.setStatusCode( "IXCANCEL" );
      shipment.setHistoric( true );
      Response response = updateShipment( 200, Credentials.AUTHENTICATED, shipmentAltId, shipment );
      assertShipment( shipment, response );
   }


   @Test
   public void testUpdateShipmentDetailsSuccess200() throws JsonProcessingException, IOException {
      shipment.setStatusCode( null );
      shipment.setCarrierName( CARRIER_NAME );
      shipment.setWaybillDesc( WAYBILL_SDESC );
      shipment.setWeightQt( WEIGHT_QT );
      Calendar UpdatedDate = Calendar.getInstance();
      UpdatedDate.setTime( SHIP_BY_DATE );
      UpdatedDate.add( Calendar.DATE, 2 );
      shipment.setShipByDate( UpdatedDate.getTime() );
      Response response = updateShipment( 200, Credentials.AUTHENTICATED, shipmentAltId, shipment );
      shipment.setStatusCode( STATUS );
      assertShipment( shipment, response );
   }


   @Test
   public void testUpdateShipmentInvalidStatusProgression400()
         throws JsonProcessingException, IOException {
      updateShipment( 400, Credentials.AUTHENTICATED, shipmentAltId, shipment );
   }


   @Test
   public void testUpdateShipmentUnauthorizedReturns403()
         throws JsonProcessingException, IOException {
      shipment.setStatusCode( "IXCANCEL" );
      updateShipment( 403, Credentials.UNAUTHORIZED, shipmentAltId, shipment );
   }


   @Test
   public void testUpdateShipmentUnauthenticatedReturns401()
         throws JsonProcessingException, IOException {
      shipment.setStatusCode( "IXCANCEL" );
      updateShipment( 401, Credentials.UNAUTHENTICATED, shipmentAltId, shipment );
   }


   private Response getById( int statusCode, Credentials security, String shipmentId ) {
      return getById( statusCode, security, shipmentId, APPLICATION_JSON, APPLICATION_JSON );
   }


   private Response getById( int statusCode, Credentials security, String shipmentId,
         String contentType, String acceptType ) {
      String userName = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().auth().preemptive().basic( userName, password )
            .accept( acceptType ).contentType( contentType ).expect().statusCode( statusCode )
            .when().get( SHIPMENT_PATH + "/" + shipmentId );
      return response;
   }


   private Response search( int statusCode, Credentials security ) {
      return search( statusCode, security, APPLICATION_JSON, APPLICATION_JSON );
   }


   private Response search( int statusCode, Credentials security, String contentType,
         String acceptType ) {
      String username = security.getUserName();
      String password = security.getPassword();
      Response response =
            RestAssured.given().queryParam( ShipmentSearchParameters.PARAM_STATUS, STATUS )
                  .queryParam( ShipmentSearchParameters.PARAM_BARCODE, shipment.getBarcode() )
                  .auth().preemptive().basic( username, password ).accept( acceptType )
                  .contentType( contentType ).expect().statusCode( statusCode ).when()
                  .get( SHIPMENT_PATH );
      return response;
   }


   private Response updateShipment( int statusCode, Credentials security, String shipmentId,
         Shipment shipment ) {
      return updateShipment( statusCode, security, shipmentId, shipment, APPLICATION_JSON,
            APPLICATION_JSON );
   }


   private Response updateShipment( int statusCode, Credentials security, String shipmentId,
         Shipment shipment, String contentType, String acceptType ) {
      String userName = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().auth().preemptive().basic( userName, password )
            .accept( acceptType ).contentType( contentType ).body( shipment ).expect()
            .statusCode( statusCode ).when().put( SHIPMENT_PATH + "/" + shipmentId );

      return response;
   }


   private Response create( int statusCode, Credentials security, Object bodyShipment ) {
      return create( statusCode, security, bodyShipment, APPLICATION_JSON, APPLICATION_JSON );
   }


   private Response create( int statusCode, Credentials security, Object shipment,
         String contentType, String acceptType ) {
      String userName = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().auth().preemptive().basic( userName, password )
            .accept( acceptType ).contentType( contentType ).body( shipment ).expect()
            .statusCode( statusCode ).when().post( SHIPMENT_PATH );
      return response;
   }


   public void prepareData()
         throws JsonParseException, JsonMappingException, IOException, ParseException {

      shipment = defaultShipmentBuilder();
      Response response =
            create( 200, Credentials.AUTHENTICATED, shipment, APPLICATION_JSON, APPLICATION_JSON );

      Shipment shipmentCreated = ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY
            .createObjectMapper().readValue( response.getBody().asString(), Shipment.class );
      shipment.setId( shipmentCreated.getId() );
      shipment.setBarcode( shipmentCreated.getBarcode() );
      shipmentAltId = shipmentCreated.getId();
   }


   private void assertShipment( Shipment shipmentExpected, Response actual )
         throws JsonProcessingException, IOException {

      Shipment shipmentActual = ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY
            .createObjectMapper().readValue( actual.getBody().asString(), Shipment.class );

      shipmentExpected.setId( shipmentActual.getId() );
      shipmentExpected.setBarcode( shipmentActual.getBarcode() );

      Assert.assertEquals( shipmentExpected, shipmentActual );

   }


   private Shipment defaultShipmentBuilder() throws ParseException {
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
