package com.mxi.am.api.resource.materials.proc.rfq;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mxi.am.api.helper.Credentials;
import com.mxi.am.api.resource.ObjectMapperFactory;
import com.mxi.am.driver.common.database.DatabaseDriver;
import com.mxi.am.driver.common.database.Result;
import com.mxi.am.guice.AssetManagementDatabaseDriverProvider;

import io.restassured.RestAssured;
import io.restassured.response.Response;


/**
 * E2E Tests - RFQ API
 *
 */
public class RFQResourceBeanTest {

   private static final String CURRENCY_CODE = "USD";
   private static final String CUSTOMER_CODE = "CTIRE";
   private static final BigDecimal EXCHANGE_RATE = new BigDecimal( "9" );
   private static final String PURCHASING_CONTACT = "mxi";
   private static final String STATUS = "RFQOPEN";
   private static final String EFFECTIVE_TO_DATE = "2018-07-09T04:00:00Z";
   private static final BigDecimal UNIT_PRICE = new BigDecimal( "1" );
   private static final BigDecimal LINE_PRICE = new BigDecimal( "1" );
   private static final String STATUS_SENT = "RFQSENT";
   private static final String VENDOR_CODE = "10001";
   private static final String PART_NO = "A0000002";
   private static final String UPDATED_PRIORITY_CODE = "NORMAL";
   private static final BigDecimal UPDATED_QUANTITY = new BigDecimal( "15" );
   private static final String UPDATED_UNIT_CODE = "BOX";
   private static final String UPDATED_VENDOR_NOTE = "updated vendor note";

   private static final String APPLICATION_JSON = "application/json";
   private static final String RFQ_API_PATH = "/amapi/" + RFQDefinition.PATH;
   ObjectMapper objectMapper =
         ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();

   private DatabaseDriver driver;
   private RFQDefinition rfqDefinition;
   private String vendorId;
   private String partId;
   private String purchasingContactId;


   @BeforeClass
   public static void setUpClass() throws ClassNotFoundException, SQLException, JsonParseException,
         JsonMappingException, IOException, ParseException {
      try {
         RestAssured.reset();
         RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
         RestAssured.baseURI = "http://".concat( InetAddress.getLocalHost().getHostName() );
         RestAssured.port = 80;
      } catch ( UnknownHostException e ) {
         throw new RuntimeException( "Cannot find local host name." );
      }
   }


   @Before
   public void setUpData() throws JsonParseException, JsonMappingException, ClassNotFoundException,
         IOException, ParseException, SQLException {
      driver = new AssetManagementDatabaseDriverProvider().get();

      Result purchasingContactIdResult = driver
            .select( "select utl_user.alt_id from utl_user where username =?", PURCHASING_CONTACT );
      if ( purchasingContactIdResult.isEmpty() ) {
         fail( "Could not find a User with the Username: " + PURCHASING_CONTACT );
      }
      purchasingContactId = purchasingContactIdResult.get( 0 ).getUuidString( "alt_id" );

      Result partIdResult = driver
            .select( "SELECT eqp_part_no.alt_id FROM eqp_part_no WHERE part_no_oem = ?", PART_NO );
      if ( partIdResult.isEmpty() ) {
         fail( "Could not find a Part with the Part No: " + PART_NO );
      }
      partId = partIdResult.get( 0 ).getUuidString( "alt_id" );

      Result vendorIdResult = driver
            .select( "SELECT org_vendor.alt_id FROM org_vendor WHERE vendor_cd = ?", VENDOR_CODE );
      if ( vendorIdResult.isEmpty() ) {
         fail( "Could not find a Vendor with the Vendor Code: " + VENDOR_CODE );
      }
      vendorId = vendorIdResult.get( 0 ).getUuidString( "alt_id" );

      prepareData();
   }


   private void prepareData()
         throws ParseException, JsonParseException, JsonMappingException, IOException {
      // create a RFQ to be used in tests
      RFQDefinition rfqDefinitionObject = defaultRFQDefinition();
      Response response = create( 200, Credentials.AUTHENTICATED, rfqDefinitionObject );
      rfqDefinition = constructRFQFromResponse( response.getBody().asString() );
   }


   @Test
   public void testRFQGetByIdSuccessReturns200()
         throws JsonParseException, JsonMappingException, IOException {
      Response response = getById( 200, Credentials.AUTHORIZED, rfqDefinition.getId() );
      RFQDefinition actualRFQDefinition = constructRFQFromResponse( response.getBody().asString() );
      Assert.assertEquals( rfqDefinition, actualRFQDefinition );
   }


   @Test
   public void testRFQGetByIdUnauthenticatedReturns401() {
      getById( 401, Credentials.UNAUTHENTICATED, rfqDefinition.getId() );
   }


   @Test
   public void testREQGetByIdUnauthorizedReturns403() {
      getById( 403, Credentials.UNAUTHORIZED, rfqDefinition.getId() );
   }


   @Test
   public void testRFQSearchSuccessReturns200()
         throws JsonParseException, JsonMappingException, IOException {
      Response response = search( 200, Credentials.AUTHORIZED );
      List<RFQDefinition> rfqDefinitionList =
            constructRFQListFromResponse( response.getBody().asString() );
      Assert.assertTrue(
            "The RFQ Definition with ID '" + rfqDefinition.getId() + "' was not in the response",
            rfqDefinitionList.contains( rfqDefinition ) );
   }


   @Test
   public void testRFQSearchUnauthenticatedReturns401() {
      search( 401, Credentials.UNAUTHENTICATED );
   }


   @Test
   public void testRFQSearchUnauthorizedReturns403() {
      search( 403, Credentials.UNAUTHORIZED );
   }


   @Test
   public void testCreateRFQSuccess200()
         throws JsonParseException, JsonMappingException, IOException {
      RFQDefinition rfqDefinition = newRFQDefinition();
      Response response = create( 200, Credentials.AUTHENTICATED, rfqDefinition );
      RFQDefinition createdRFQDefinition =
            constructRFQFromResponse( response.getBody().asString() );
      rfqDefinition.setId( createdRFQDefinition.getId() );
      Assert.assertEquals( rfqDefinition, createdRFQDefinition );
   }


   @Test
   public void testCreateRFQUnauthenticatedReturns401() {
      RFQDefinition newRFQDefinition = new RFQDefinition();
      create( 401, Credentials.UNAUTHENTICATED, newRFQDefinition );
   }


   @Test
   public void testCreateRFQUnauthorizedReturns403() {
      RFQDefinition newRFQDefinition = new RFQDefinition();
      create( 403, Credentials.UNAUTHORIZED, newRFQDefinition );
   }


   @Test
   public void testUpdateRFQSuccess200()
         throws JsonParseException, JsonMappingException, IOException, ParseException {
      Response response = getById( 200, Credentials.AUTHORIZED, rfqDefinition.getId() );
      RFQDefinition newRFQDefinition = constructRFQFromResponse( response.getBody().asString() );
      Date effectiveToDate;
      try {
         effectiveToDate = new SimpleDateFormat( "yyyy-MM-dd" ).parse( EFFECTIVE_TO_DATE );
      } catch ( ParseException exception ) {
         throw ( exception );
      }
      newRFQDefinition.getRfqVendors().get( 0 ).getRFQLineVendor().get( 0 )
            .setEffectiveToDate( effectiveToDate );
      newRFQDefinition.getRfqVendors().get( 0 ).getRFQLineVendor().get( 0 )
            .setUnitPrice( UNIT_PRICE );
      newRFQDefinition.getRfqVendors().get( 0 ).getRFQLineVendor().get( 0 )
            .setLinePrice( LINE_PRICE );
      newRFQDefinition.getRfqVendors().get( 0 ).setCurrencyCode( CURRENCY_CODE );

      // Set values to update
      RFQLine rfqLine = newRFQDefinition.getRfqLines().get( 0 );
      rfqLine.setRfqLineId( rfqDefinition.getRfqLines().get( 0 ).getRfqLineId() );
      rfqLine.setPriorityCode( UPDATED_PRIORITY_CODE );
      rfqLine.setRfqQuantity( UPDATED_QUANTITY );
      rfqLine.setUnitCode( UPDATED_UNIT_CODE );
      rfqLine.setVendorNote( UPDATED_VENDOR_NOTE );

      Response updateResponse =
            update( 200, newRFQDefinition.getId(), newRFQDefinition, Credentials.AUTHENTICATED );
      RFQDefinition updatedRFQDefinition =
            constructRFQFromResponse( updateResponse.getBody().asString() );
      Assert.assertEquals( newRFQDefinition, updatedRFQDefinition );
   }


   @Test
   public void testUpdateRFQUnauthenticatedReturns401() {
      RFQDefinition updateDetails = new RFQDefinition();
      update( 401, rfqDefinition.getId(), updateDetails, Credentials.UNAUTHENTICATED );
   }


   @Test
   public void testUpdateRFQUnauthorizedReturns403() {
      RFQDefinition updateDetails = new RFQDefinition();
      update( 403, rfqDefinition.getId(), updateDetails, Credentials.UNAUTHORIZED );
   }


   @Test
   public void testSendRFQSuccess200()
         throws JsonParseException, JsonMappingException, IOException {
      Response response = send( 200, rfqDefinition.getId(), Credentials.AUTHENTICATED );
      RFQDefinition rfqDefinition = constructRFQFromResponse( response.getBody().asString() );
      Assert.assertEquals( STATUS_SENT, rfqDefinition.getStatus() );
   }


   @Test
   public void testSendRFQUnauthenticatedReturns401() {
      send( 401, rfqDefinition.getId(), Credentials.UNAUTHENTICATED );
   }


   @Test
   public void testSendUnauthorizedReturns403() {
      send( 403, rfqDefinition.getId(), Credentials.UNAUTHORIZED );
   }


   /**
    * Get a RFQ by Id using the RFQ API
    *
    * @param statusCode
    * @param credentials
    * @param id
    * @return
    */
   private Response getById( int statusCode, Credentials credentials, String id ) {
      String userName = credentials.getUserName();
      String password = credentials.getPassword();
      Response response = RestAssured.given().auth().preemptive().basic( userName, password )
            .accept( APPLICATION_JSON ).contentType( APPLICATION_JSON ).expect()
            .statusCode( statusCode ).when().get( RFQ_API_PATH + "/" + id );
      return response;
   }


   /**
    * Get a RFQ by RFQDefinition search parameters using the RFQ API
    *
    * @param statusCode
    * @param credentials
    * @param id
    * @return
    */
   private Response search( int statusCode, Credentials credentials ) {
      String username = credentials.getUserName();
      String password = credentials.getPassword();
      Response response = RestAssured.given()
            .queryParam( RFQDefinitionSearchParameters.QTR_PARAM, rfqDefinition.getRfqNo() )
            .queryParam( RFQDefinitionSearchParameters.LINE_PARAM, true )
            .queryParam( RFQDefinitionSearchParameters.VENDOR_PARAM, true )
            .queryParam( RFQDefinitionSearchParameters.LINE_VENDOR_PARAM, true ).auth().preemptive()
            .basic( username, password ).accept( APPLICATION_JSON ).contentType( APPLICATION_JSON )
            .expect().statusCode( statusCode ).when().get( RFQ_API_PATH );
      return response;
   }


   /**
    * Create a RFQ using the RFQ API
    *
    * @param statusCode
    * @param credentials
    * @param rfqDefinition
    * @return
    */
   private Response create( int statusCode, Credentials credentials, Object rfqDefinition ) {
      String userName = credentials.getUserName();
      String password = credentials.getPassword();
      Response response = RestAssured.given().auth().preemptive().basic( userName, password )
            .accept( APPLICATION_JSON ).contentType( APPLICATION_JSON ).body( rfqDefinition )
            .expect().statusCode( statusCode ).when().post( RFQ_API_PATH );
      return response;
   }


   /**
    * Update a RFQ using the RFQ API
    *
    * @param statusCode
    * @param id
    * @param rfqDefinition
    * @param credentials
    * @return
    */
   private Response update( int statusCode, String id, Object rfqDefinition,
         Credentials credentials ) {
      String userName = credentials.getUserName();
      String password = credentials.getPassword();
      Response response = RestAssured.given().auth().preemptive().basic( userName, password )
            .accept( APPLICATION_JSON ).contentType( APPLICATION_JSON ).body( rfqDefinition )
            .expect().statusCode( statusCode ).when().put( RFQ_API_PATH + "/" + id );
      return response;
   }


   /**
    * Send a RFQ using the RFQ API
    *
    * @param statusCode
    * @param id
    * @param credentials
    * @return
    */
   private Response send( int statusCode, String id, Credentials credentials ) {
      String userName = credentials.getUserName();
      String password = credentials.getPassword();
      Response response = RestAssured.given().auth().preemptive().basic( userName, password )
            .accept( APPLICATION_JSON ).contentType( APPLICATION_JSON ).expect()
            .statusCode( statusCode ).when().put( RFQ_API_PATH + "/send/" + id );
      return response;
   }


   private RFQDefinition constructRFQFromResponse( String body )
         throws JsonParseException, JsonMappingException, IOException {
      RFQDefinition rfqDefinition = objectMapper.readValue( body,
            objectMapper.getTypeFactory().constructType( RFQDefinition.class ) );
      return rfqDefinition;
   }


   private List<RFQDefinition> constructRFQListFromResponse( String body )
         throws JsonParseException, JsonMappingException, IOException {
      List<RFQDefinition> rfqDefinitionList = objectMapper.readValue( body, objectMapper
            .getTypeFactory().constructCollectionType( List.class, RFQDefinition.class ) );
      return rfqDefinitionList;
   }


   /**
    * Create a default RFQ object
    *
    * @return
    * @throws ParseException
    */
   private RFQDefinition defaultRFQDefinition() throws ParseException {
      RFQDefinition rfqDefinition = newRFQDefinition();
      rfqDefinition.setRfqNo( generateRFQNo() );

      // Add RFQ lines
      List<RFQLine> rfqLines = new ArrayList<>();
      RFQLine rfqLine = new RFQLine();
      rfqLine.setPartId( partId );
      rfqLines.add( rfqLine );
      rfqDefinition.setRfqLines( rfqLines );

      // Add RFQ vendors
      RFQVendor rfqVendor = new RFQVendor();
      rfqVendor.setVendorId( vendorId );

      // Add RFQ line vendor
      List<RFQLineVendor> rfqLineVendors = new ArrayList<>();
      RFQLineVendor rfqLineVendor = new RFQLineVendor();
      rfqLineVendor.setQuotePartId( partId );
      rfqLineVendor.setReqPartId( partId );
      Date effectiveToDate;
      try {
         effectiveToDate = new SimpleDateFormat( "yyyy-MM-dd" ).parse( EFFECTIVE_TO_DATE );
      } catch ( ParseException exception ) {
         throw ( exception );
      }
      rfqLineVendor.setEffectiveToDate( effectiveToDate );
      rfqLineVendors.add( rfqLineVendor );
      rfqVendor.setRFQLineVendor( rfqLineVendors );
      List<RFQVendor> rfqVendors = new ArrayList<>();
      rfqVendors.add( rfqVendor );
      rfqDefinition.setRfqVendors( rfqVendors );

      return rfqDefinition;
   }


   /**
    * Create a new RFQ object to be used for POST
    *
    * @return
    */
   private RFQDefinition newRFQDefinition() {
      RFQDefinition rfqDefinition = new RFQDefinition();
      rfqDefinition.setRfqNo( generateRFQNo() );
      rfqDefinition.setCurrency( CURRENCY_CODE );
      rfqDefinition.setCustomerCode( CUSTOMER_CODE );
      rfqDefinition.setExchangeRate( EXCHANGE_RATE );
      rfqDefinition.setPurchasingContact( PURCHASING_CONTACT );
      rfqDefinition.setPurchasingContactId( purchasingContactId );
      rfqDefinition.setStatus( STATUS );
      return rfqDefinition;
   }


   private String generateRFQNo() {
      String rfqNo = RandomStringUtils.random( 20, false, true );
      return rfqNo;
   }

}
