package com.mxi.am.api.resource.materials.asset.aircraft;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Arrays;
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
import com.mxi.am.api.helper.Credentials;
import com.mxi.am.api.resource.ObjectMapperFactory;
import com.mxi.am.driver.common.database.DatabaseDriver;
import com.mxi.am.driver.common.database.Result;
import com.mxi.am.guice.AssetManagementDatabaseDriverProvider;

import io.restassured.RestAssured;
import io.restassured.response.Response;


/**
 * e2e Tests - Aircraft API
 *
 */
public class AircraftResourceBeanTest {

   private static final String APPLICATION_JSON = "application/json";
   private static final String AIRCRAFT_API_PATH = "/amapi/" + Aircraft.PATH;
   private final ObjectMapper objectMapper =
         ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();

   private DatabaseDriver driver;

   private static Aircraft aircraftResource;

   private static final String INVENTORY_CONDITION_CODE1 = "RFI";
   private static final String LOCATION_CODE = "OPS";
   private static final String PART_NO = "ACFT_ASSY_PN2";
   private static final String OWNER_CODE = "MXI";
   private static final String FNC_ACCOUNT_CODE = "5";
   private static final String FC_MODEL_DESCRIPTION = "Forecast1";
   private static final String FIN_NUMBER = "FINCODE";
   private static final boolean LOCKED_STATUS = false;

   private static final String UPDATED_FIN_NUMBER = "Fin Number New";
   private static final String UPDATED_OEM_LINE_NUMBER = "Oem LIne Number New";
   private static final String UPDATED_OEM_VARIABLE_NUMBER = "Oem varibale Number New";
   private static final String UPDATED_REGISTRATION_CODE = "67976";

   // Queries to retrieve generated alt_id
   private final String queryInvLoc = "SELECT * FROM inv_loc WHERE loc_cd = ?";
   private final String queryEqpPartNo = "SELECT * FROM eqp_part_no WHERE part_no_oem = ?";
   private final String queryInvOwner = "SELECT * FROM inv_owner WHERE owner_cd = ?";
   private final String queryFncAccount = "SELECT * FROM fnc_account WHERE account_cd = ?";
   private final String queryFcMoel = "SELECT * FROM fc_model WHERE desc_sdesc = ?";

   private String locationId;
   private String partId;
   private String ownerId;
   private String issueToAccountId;
   private String forecastModuleId;


   @BeforeClass
   public static void setUpClass() throws JsonParseException, JsonMappingException, IOException,
         ParseException, ClassNotFoundException, SQLException {
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
   public void setUpData() throws Exception {

      driver = new AssetManagementDatabaseDriverProvider().get();

      Result location = driver.select( queryInvLoc, LOCATION_CODE );
      Result part = driver.select( queryEqpPartNo, PART_NO );
      Result owner = driver.select( queryInvOwner, OWNER_CODE );
      Result fncAccount = driver.select( queryFncAccount, FNC_ACCOUNT_CODE );
      Result fcMoel = driver.select( queryFcMoel, FC_MODEL_DESCRIPTION );

      locationId = location.get( 0 ).getUuidString( "alt_id" );
      partId = part.get( 0 ).getUuidString( "alt_id" );
      ownerId = owner.get( 0 ).getUuidString( "alt_id" );
      issueToAccountId = fncAccount.get( 0 ).getUuidString( "alt_id" );
      forecastModuleId = fcMoel.get( 0 ).getUuidString( "alt_id" );

      prepareData();

   }


   private void prepareData() throws JsonParseException, JsonMappingException, IOException,
         ParseException, ClassNotFoundException, SQLException {
      // create an aircraft to be used in tests
      Aircraft defAircraft = defaultAircraftBuilder();
      Response response = create( 200, Credentials.AUTHENTICATED, defAircraft );
      aircraftResource = constructAircraftFromResponse( response.getBody().asString() );

   }


   private Aircraft defaultAircraftBuilder() {
      Aircraft aircraftObj = new Aircraft();
      aircraftObj.setLocation( locationId );

      String charList = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
      int randomStringLength = 6;
      StringBuffer randStr = new StringBuffer();

      for ( int i = 0; i < randomStringLength; i++ ) {
         int number = ( int ) ( Math.random() * 50 + 1 );
         char ch = charList.charAt( number );
         randStr.append( ch );
      }

      aircraftObj.setRegistrationCode( randStr.toString() );
      aircraftObj.setOwnerId( ownerId );
      aircraftObj.setForecastModelId( forecastModuleId );
      aircraftObj.setInventoryConditionCode( INVENTORY_CONDITION_CODE1 );
      aircraftObj.setIssueToAccountId( issueToAccountId );
      aircraftObj.setPartId( partId );
      aircraftObj.setFinNumber( FIN_NUMBER );
      return aircraftObj;
   }


   private static Aircraft constructAircraftFromResponse( String body )
         throws JsonParseException, JsonMappingException, IOException {
      ObjectMapper objectMapper =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      Aircraft aircraft = objectMapper.readValue( body,
            objectMapper.getTypeFactory().constructType( Aircraft.class ) );
      return aircraft;
   }


   private static Response create( int statusCode, Credentials credentials, Object aircraft ) {
      String userName = credentials.getUserName();
      String password = credentials.getPassword();

      Response response = RestAssured.given().auth().preemptive().basic( userName, password )
            .accept( APPLICATION_JSON ).contentType( APPLICATION_JSON ).body( aircraft ).expect()
            .statusCode( statusCode ).when().post( AIRCRAFT_API_PATH );
      return response;
   }


   private Response getById( int statusCode, Credentials credentials, String id ) {

      String userName = credentials.getUserName();
      String password = credentials.getPassword();

      Response response = RestAssured.given().auth().preemptive().basic( userName, password )
            .accept( APPLICATION_JSON ).contentType( APPLICATION_JSON ).expect()
            .statusCode( statusCode ).when().get( AIRCRAFT_API_PATH + "/" + id );

      return response;

   }


   private Response search( int statusCode, Credentials security ) {
      String username = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given()
            .queryParam( AircraftSearchParameters.BARCODE_PARAM,
                  Arrays.asList( aircraftResource.getBarcode() ) )
            .queryParam( AircraftSearchParameters.FIN_NUMBER_PARAM, Arrays.asList( FIN_NUMBER ) )
            .queryParam( AircraftSearchParameters.LOCKED_STATUS_PARAM, LOCKED_STATUS )
            .queryParam( AircraftSearchParameters.OPERATIONAL_CODE_PARAM,
                  Arrays.asList( aircraftResource.getOperatingStatusCode() ) )
            .queryParam( AircraftSearchParameters.PART_NO_PARAM,
                  Arrays.asList( aircraftResource.getPartId() ) )
            .queryParam( AircraftSearchParameters.REGISTRATION_CODE_PARAM,
                  Arrays.asList( aircraftResource.getRegistrationCode() ) )
            .auth().preemptive().basic( username, password ).accept( APPLICATION_JSON )
            .contentType( APPLICATION_JSON ).expect().statusCode( statusCode ).when()
            .get( AIRCRAFT_API_PATH );

      return response;

   }


   private Response update( int statusCode, Credentials security, String aircraftId,
         Aircraft aircraft ) {
      String userName = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().auth().preemptive().basic( userName, password )
            .accept( APPLICATION_JSON ).contentType( APPLICATION_JSON ).body( aircraft ).expect()
            .statusCode( statusCode ).when().put( AIRCRAFT_API_PATH + "/" + aircraftId );

      return response;
   }


   @Test
   @CSIContractTest( Project.UPS )
   public void testAircraftGetByIdSuccessReturns200()
         throws IOException, ParseException, ClassNotFoundException, SQLException {

      Response response = getById( 200, Credentials.AUTHORIZED, aircraftResource.getId() );

      Aircraft aircraft = response.jsonPath().getObject( "", Aircraft.class );
      Assert.assertEquals( aircraftResource, aircraft );

   }


   @Test
   public void testAircraftGetByIdUnauthenticatedReturns401() {
      Response lResponse = getById( 401, Credentials.UNAUTHENTICATED, aircraftResource.getId() );
      Assert.assertEquals( 401, lResponse.getStatusCode() );
   }


   @Test
   public void testAircraftGetByIdUnauthorizedReturns403() {
      getById( 403, Credentials.UNAUTHORIZED, aircraftResource.getId() );
   }


   @Test
   public void testAircraftSearchSuccessReturns200() throws JsonProcessingException, IOException {
      Response response = search( 200, Credentials.AUTHORIZED );
      List<Aircraft> aircraftList = response.jsonPath().getList( "", Aircraft.class );

      Assert.assertEquals( aircraftResource, aircraftList.get( 0 ) );
   }


   @Test
   public void testAircraftSearchUnauthenticatedReturns401() {
      Response lResponse = search( 401, Credentials.UNAUTHENTICATED );
      Assert.assertEquals( 401, lResponse.getStatusCode() );
   }


   @Test
   public void testAircraftSearchUnauthorizedReturns403() {
      search( 403, Credentials.UNAUTHORIZED );
   }


   @Test
   public void testUpdateAircraftSuccessReturns200() {

      Response response = getById( 200, Credentials.AUTHORIZED, aircraftResource.getId() );
      Aircraft newAircraft = response.jsonPath().getObject( "", Aircraft.class );

      // Set values to update
      newAircraft.setFinNumber( UPDATED_FIN_NUMBER );
      newAircraft.setOemLineNumber( UPDATED_OEM_LINE_NUMBER );
      newAircraft.setOemVariableNumber( UPDATED_OEM_VARIABLE_NUMBER );
      newAircraft.setRegistrationCode( UPDATED_REGISTRATION_CODE );

      response = update( 200, Credentials.AUTHORIZED, newAircraft.getId(), newAircraft );
      Aircraft updatedAircraft = response.jsonPath().getObject( "", Aircraft.class );

      // Set name according to the updated registration code
      newAircraft.setName( updatedAircraft.getName() );
      // Ignore last modified date for the assertion
      newAircraft.setLastModifiedDate( updatedAircraft.getLastModifiedDate() );

      Assert.assertEquals( newAircraft, updatedAircraft );
   }


   @Test
   public void testUpdateAircraftUnauthenticatedReturns401() {

      Response response = getById( 200, Credentials.AUTHORIZED, aircraftResource.getId() );
      Aircraft newAircraft = response.jsonPath().getObject( "", Aircraft.class );

      // Set values to update
      newAircraft.setFinNumber( UPDATED_FIN_NUMBER );
      newAircraft.setOemLineNumber( UPDATED_OEM_LINE_NUMBER );
      newAircraft.setOemVariableNumber( UPDATED_OEM_VARIABLE_NUMBER );
      newAircraft.setRegistrationCode( UPDATED_REGISTRATION_CODE );

      response = update( 401, Credentials.UNAUTHENTICATED, newAircraft.getId(), newAircraft );

   }

}
