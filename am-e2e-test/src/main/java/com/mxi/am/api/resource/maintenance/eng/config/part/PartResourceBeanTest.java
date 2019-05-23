package com.mxi.am.api.resource.maintenance.eng.config.part;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
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
 * Application Tests for Part API
 *
 */
public class PartResourceBeanTest {

   private static final String APPLICATION_JSON = "application/json";
   private static final String PART_PATH = "/amapi/" + Part.PATH;

   // Part Data
   private Part part;
   private String partId;
   private String manufactId;
   private String partNoOem;
   private static final String MANUFACT_CD = "11111";
   private static final String STATUS_CD = "BUILD";
   private static final String PART_NAME = "Part 1";
   private static final String CLASS_CD = "TRK";
   private static final String FINANCIAL_CLASS_CD = "EXPENDABLE";
   private static final String FINANCE_ACCOUNT_CD = "5";
   private static final Double SCRAP_RATE = 0.01;
   private static final String ABC_CLASS = "C";
   private static final String UNIT_CD = "EA";
   private static final String UPDATED_STATUS_CD = "ACTV";

   private static final String NONEXISTANT_PART_ID = "C6EA96E0635611E8B095593AA3B86334";

   private DatabaseDriver driver;


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


   @Before
   public void setUpData() throws ClassNotFoundException, SQLException, JsonParseException,
         JsonMappingException, IOException {
      driver = new AssetManagementDatabaseDriverProvider().get();

      Result manufactIdResult = driver.select(
            "select eqp_manufact.alt_id from eqp_manufact where eqp_manufact.manufact_cd=?",
            MANUFACT_CD );

      if ( manufactIdResult.isEmpty() ) {
         fail( "Could not find Manufacturer ID with Manufacturer Code: " + MANUFACT_CD );
      }

      manufactId = manufactIdResult.get( 0 ).getUuidString( "alt_id" );

      prepareData();
   }


   @Test
   public void testGetPartByIdSuccessReturns200()
         throws JsonParseException, JsonMappingException, IOException {
      Response response = getPartById( 200, Credentials.AUTHENTICATED, partId );
      assertPart( part, response );
   }


   @Test
   public void testGetPartByIdUnauthenticatedReturns401()
         throws JsonParseException, JsonMappingException, IOException {
      getPartById( 401, Credentials.UNAUTHENTICATED, partId );
   }


   @Test
   public void testGetPartByIdUnauthorizedReturns403()
         throws JsonParseException, JsonMappingException, IOException {
      getPartById( 403, Credentials.UNAUTHORIZED, partId );
   }


   @Test
   public void testGetPartByIdNotFoundReturns404() {
      getPartById( 404, Credentials.AUTHORIZED, NONEXISTANT_PART_ID );
   }


   @Test
   @CSIContractTest( Project.UPS )
   public void testSearchPartSuccessReturns200()
         throws JsonParseException, JsonMappingException, IOException {
      Response response =
            searchPart( 200, Credentials.AUTHENTICATED, CLASS_CD, MANUFACT_CD, partNoOem );
      assertPartSearch( part, response );
   }


   @Test
   public void testSearchPartUnauthenticatedReturns401()
         throws JsonParseException, JsonMappingException, IOException {
      searchPart( 401, Credentials.UNAUTHENTICATED, CLASS_CD, MANUFACT_CD, partNoOem );
   }


   @Test
   public void testSearchPartUnauthorizedReturns403()
         throws JsonParseException, JsonMappingException, IOException {
      searchPart( 403, Credentials.UNAUTHORIZED, CLASS_CD, MANUFACT_CD, partNoOem );
   }


   @Test
   public void testCreatePartSuccessReturns200()
         throws JsonParseException, JsonMappingException, IOException {
      part = partBuilder();
      Response response = createPart( 200, Credentials.AUTHENTICATED, part );
      assertPart( part, response );
   }


   @Test
   public void testCreatePartUnauthenticatedReturns401()
         throws JsonParseException, JsonMappingException, IOException {
      createPart( 401, Credentials.UNAUTHENTICATED, part );
   }


   @Test
   public void testCreatePartUnauthorizedReturns403()
         throws JsonParseException, JsonMappingException, IOException {
      createPart( 403, Credentials.UNAUTHORIZED, part );
   }


   @Test
   public void testUpdatePartSuccessReturns200()
         throws JsonParseException, JsonMappingException, IOException {
      part.setStatusCode( UPDATED_STATUS_CD );
      Response response = updatePart( 200, Credentials.AUTHENTICATED, partId, part );
      assertPart( part, response );
   }


   @Test
   public void testUpdatePartUnauthenticatedReturns401()
         throws JsonProcessingException, IOException {
      part.setStatusCode( UPDATED_STATUS_CD );
      updatePart( 401, Credentials.UNAUTHENTICATED, partId, part );
   }


   @Test
   public void testUpdatePartUnauthorizedReturns403() throws JsonProcessingException, IOException {
      part.setStatusCode( UPDATED_STATUS_CD );
      updatePart( 403, Credentials.UNAUTHORIZED, partId, part );
   }


   private void assertPart( Part expectedPart, Response actualResponse )
         throws JsonParseException, JsonMappingException, IOException {

      Part partActual = ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper()
            .readValue( actualResponse.getBody().asString(), Part.class );

      expectedPart.setId( partActual.getId() );
      expectedPart.setPartGroupApprovals( partActual.getPartGroupApprovals() );
      expectedPart.setPartGroupIds( partActual.getPartGroupIds() );

      Assert.assertEquals( "Incorrect Part returned: ", expectedPart, partActual );
   }


   private void assertPartSearch( Part expectedPart, Response actualResponse )
         throws JsonParseException, JsonMappingException, IOException {

      ObjectMapper objectMapper =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      List<Part> partsActual = objectMapper.readValue( actualResponse.getBody().asString(),
            objectMapper.getTypeFactory().constructCollectionType( List.class, Part.class ) );
      Assert.assertEquals( "Incorrect number of parts returned: ", 1, partsActual.size() );
      Assert.assertTrue( "The expected Part with OEM Part No " + expectedPart.getOemPartNumber()
            + " was not in the response", partsActual.contains( expectedPart ) );

   }


   private Response createPart( int statusCode, Credentials security, Part part ) {

      String username = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().auth().preemptive().basic( username, password )
            .accept( APPLICATION_JSON ).contentType( APPLICATION_JSON ).body( part ).expect()
            .statusCode( statusCode ).when().post( PART_PATH );
      return response;
   }


   private Response getPartById( int statusCode, Credentials security, String partId ) {

      String username = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().auth().preemptive().basic( username, password )
            .accept( APPLICATION_JSON ).contentType( APPLICATION_JSON ).expect()
            .statusCode( statusCode ).when().get( PART_PATH + "/" + partId );
      return response;
   }


   private Response updatePart( int statusCode, Credentials security, String partId, Part part ) {

      String username = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().auth().preemptive().basic( username, password )
            .accept( APPLICATION_JSON ).contentType( APPLICATION_JSON ).body( part ).expect()
            .statusCode( statusCode ).when().put( PART_PATH + "/" + partId );
      return response;
   }


   private Response searchPart( int statusCode, Credentials security, String classCd,
         String manufactCd, String partNoOem ) {

      String username = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given()
            .queryParam( PartResource.INVENTORY_CLASS_CODE_PARAM, classCd )
            .queryParam( PartResource.MANUFACT_CD_PARAM, manufactCd )
            .queryParam( PartResource.PART_NO_PARAM, partNoOem ).auth().preemptive()
            .basic( username, password ).accept( APPLICATION_JSON ).contentType( APPLICATION_JSON )
            .expect().statusCode( statusCode ).when().get( PART_PATH );
      return response;
   }


   private void prepareData() throws JsonParseException, JsonMappingException, IOException {
      part = partBuilder();
      Response response = createPart( 200, Credentials.AUTHENTICATED, part );

      Part partCreated = ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper()
            .readValue( response.getBody().asString(), Part.class );
      part.setId( partCreated.getId() );
      partId = partCreated.getId();
   }


   private Part partBuilder() {
      Part part = new Part();

      // To generate a random OEM Part No
      String charList = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
      int randomStringLength = 6;
      StringBuffer randStr = new StringBuffer();

      for ( int i = 0; i < randomStringLength; i++ ) {
         int number = ( int ) ( Math.random() * 50 + 1 );
         char ch = charList.charAt( number );
         randStr.append( ch );
         partNoOem = randStr.toString().toUpperCase();
      }

      part.setManufacturerId( manufactId );
      part.setAbcClass( ABC_CLASS );
      part.setClassCode( CLASS_CD );
      part.setFinanceAccountCode( FINANCE_ACCOUNT_CD );
      part.setFinancialClassCode( FINANCIAL_CLASS_CD );
      part.setManufacturerCode( MANUFACT_CD );
      part.setName( PART_NAME );
      part.setOemPartNumber( partNoOem );
      part.setScrapRate( SCRAP_RATE );
      part.setStatusCode( STATUS_CD );
      part.setUnitCode( UNIT_CD );

      return part;
   }
}
