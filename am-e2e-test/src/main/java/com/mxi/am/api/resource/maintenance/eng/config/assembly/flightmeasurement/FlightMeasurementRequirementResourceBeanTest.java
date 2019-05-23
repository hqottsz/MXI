package com.mxi.am.api.resource.maintenance.eng.config.assembly.flightmeasurement;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mxi.am.api.helper.Credentials;
import com.mxi.am.api.resource.ObjectMapperFactory;
import com.mxi.am.driver.common.database.DatabaseDriver;
import com.mxi.am.driver.common.database.Result;
import com.mxi.am.guice.AssetManagementDatabaseDriverProvider;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;


/**
 * FlightMeasurementRequirement API test
 *
 */
public class FlightMeasurementRequirementResourceBeanTest {

   private final String applicationJson = "application/json";
   private final String flightMeasurementRequirementPath =
         "/amapi/" + FlightMeasurementRequirement.PATH;
   private static final String DATA_TYPE_CODE1 = "USAGE1";
   private static final String DATA_TYPE_CODE2 = "USAGE2";
   private static final String DATA_TYPE_CODE3 = "USAGE3";
   private static final String DATA_TYPE_CODE4 = "APUH_AT_READING";
   private static final String DATA_TYPE_CODE5 = "MEASUREMENT1";
   private static final String DATA_TYPE_CODE6 = "MEASUREMENT2";
   private static final String INVALID_DATA_TYPE_CD = "TEST1";
   private static final String ENG_UNIT_CODE1 = "COUNTS";
   private static final String ENG_UNIT_CODE2 = "COUNTS";
   private static final String ENG_UNIT_CODE3 = "COUNTS";
   private static final String ENG_UNIT_CODE4 = "HOURS";
   private static final String ENG_UNIT_CODE5 = "COUNTS";
   private static final String ENG_UNIT_CODE6 = "COUNTS";
   private static final String ASSMBL_CD = "ACFT_CD1";
   private static final int ASSMBL_DB_ID = 4650;

   private String altId;

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

      Result assemblyResult = driver.select(
            "select alt_id from eqp_assmbl where eqp_assmbl.assmbl_cd=? and eqp_assmbl.assmbl_db_id=?",
            ASSMBL_CD, ASSMBL_DB_ID );

      if ( assemblyResult.isEmpty() ) {
         fail( "Could not find the FlightMeasurementRequirement with alt_id: " + altId );
      }

      altId = assemblyResult.get( 0 ).getUuidString( "alt_id" );

   }


   @Test
   public void testSearchFlightMeasurementRequirementSuccessReturns200()
         throws JsonProcessingException, IOException, ParseException {

      Response response =
            createSearch( 200, Credentials.AUTHENTICATED, altId, applicationJson, applicationJson );
      assertFlightMeasurementForSearch( buildFlightMeasurementForSearch(), response );
   }


   @Test
   public void testSearchFlightMeasurementRequirementUnauthenticatedReturns401()
         throws JsonProcessingException, IOException {

      createSearch( 401, Credentials.UNAUTHENTICATED, altId, applicationJson, applicationJson );
   }


   @Test
   public void testSearchFlightMeasurementRequirementUnauthorizedReturns403()
         throws JsonProcessingException, IOException {

      createSearch( 403, Credentials.UNAUTHORIZED, altId, applicationJson, applicationJson );
   }


   @Test
   public void testAssignFlightmeasurementSuccess()
         throws JsonProcessingException, IOException, ParseException {
      FlightMeasurementRequirement flightMeasurementRequirement =
            buildFlightMeasurementForAssignMeasurement();

      Response response = post( flightMeasurementRequirementPath, flightMeasurementRequirement,
            Credentials.AUTHENTICATED, 200 );
      assertFlightMeasurementForAssignMeasurement( flightMeasurementRequirement, response );
   }


   @Test
   public void testAssignFlightmeasurementFailure()
         throws JsonProcessingException, IOException, ParseException {
      FlightMeasurementRequirement flightMeasurementRequirement =
            buildFlightMeasurementForAssignMeasurement();
      Measurement measurements = new Measurement();
      measurements.setDataTypeCode( INVALID_DATA_TYPE_CD );
      flightMeasurementRequirement.getMeasurements().add( measurements );
      Response response = post( flightMeasurementRequirementPath, flightMeasurementRequirement,
            Credentials.AUTHENTICATED, 400 );
      Assert.assertTrue(
            response.getBody().asString().contains( "Invalid Data Type code encountered." ) );
   }


   private Response createSearch( int statusCode, Credentials security, String inventoryUUId,
         String contentType, String acceptType ) {
      String userName = security.getUserName();
      String password = security.getPassword();

      Response lResponse = RestAssured.given()
            .queryParam( FlightMeasurementRequirementSearchParameters.ASSEMBLY_ID_PARAM,
                  inventoryUUId )
            .auth().preemptive().basic( userName, password ).accept( acceptType )
            .contentType( contentType ).expect().statusCode( statusCode ).when()
            .get( flightMeasurementRequirementPath );
      return lResponse;

   }


   private Response post( String endpoint, Object entity, Credentials security,
         int expectedStatus ) {
      String userName = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().accept( ContentType.JSON ).auth().preemptive()
            .basic( userName, password ).contentType( ContentType.JSON ).body( entity ).when()
            .post( endpoint ).then().assertThat().statusCode( expectedStatus ).extract().response();

      return response;

   }


   private void assertFlightMeasurementForSearch(
         FlightMeasurementRequirement flightMeasurementRequirementExpected, Response actual )
         throws JsonProcessingException, IOException {

      ObjectMapper objectMapper =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();

      List<FlightMeasurementRequirement> flightMeasurementRequirementsActual =
            objectMapper.readValue( actual.getBody().asString(), objectMapper.getTypeFactory()
                  .constructCollectionType( List.class, FlightMeasurementRequirement.class ) );

      Assert.assertEquals( "More than One results found", 1,
            flightMeasurementRequirementsActual.size() );
      for ( Measurement measurementExpected : flightMeasurementRequirementExpected
            .getMeasurements() ) {
         Assert.assertTrue( flightMeasurementRequirementsActual.get( 0 ).getMeasurements()
               .contains( measurementExpected ) );
      }
      Assert.assertEquals( flightMeasurementRequirementExpected.getAssemblyId(),
            flightMeasurementRequirementsActual.get( 0 ).getAssemblyId() );
   }


   private void assertFlightMeasurementForAssignMeasurement(
         FlightMeasurementRequirement flightMeasurementRequirementExpected, Response actual )
         throws JsonProcessingException, IOException {

      ObjectMapper objectMapper =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      FlightMeasurementRequirement flightMeasurementRequirementActual = objectMapper.readValue(
            actual.getBody().asString(),
            objectMapper.getTypeFactory().constructType( FlightMeasurementRequirement.class ) );

      for ( Measurement measurementExpected : flightMeasurementRequirementExpected
            .getMeasurements() ) {
         Assert.assertTrue( flightMeasurementRequirementActual.getMeasurements()
               .contains( measurementExpected ) );
      }
      Assert.assertEquals( flightMeasurementRequirementExpected.getAssemblyId(),
            flightMeasurementRequirementActual.getAssemblyId() );
      Assert.assertTrue( "Last modified date is not properly updated",
            flightMeasurementRequirementActual.getLastModifiedDate()
                  .compareTo( flightMeasurementRequirementExpected.getLastModifiedDate() ) >= 0 );
   }


   private FlightMeasurementRequirement buildFlightMeasurementForSearch() throws ParseException {
      FlightMeasurementRequirement flightMeasurementRequirement =
            new FlightMeasurementRequirement();
      flightMeasurementRequirement.setAssemblyId( altId );

      List<Measurement> measurementList = new ArrayList<Measurement>();
      Measurement measurermentsObjectOne = new Measurement();
      measurermentsObjectOne.setDataTypeCode( DATA_TYPE_CODE1 );
      measurermentsObjectOne.setEngineeringUnitCode( ENG_UNIT_CODE1 );

      Measurement measurermentsObjectTwo = new Measurement();
      measurermentsObjectTwo.setDataTypeCode( DATA_TYPE_CODE2 );
      measurermentsObjectTwo.setEngineeringUnitCode( ENG_UNIT_CODE2 );

      Measurement measurermentsObjectThree = new Measurement();
      measurermentsObjectThree.setDataTypeCode( DATA_TYPE_CODE3 );
      measurermentsObjectThree.setEngineeringUnitCode( ENG_UNIT_CODE3 );

      Measurement measurermentsObjectFour = new Measurement();
      measurermentsObjectFour.setDataTypeCode( DATA_TYPE_CODE4 );
      measurermentsObjectFour.setEngineeringUnitCode( ENG_UNIT_CODE4 );

      measurementList.add( measurermentsObjectOne );
      measurementList.add( measurermentsObjectTwo );
      measurementList.add( measurermentsObjectThree );
      measurementList.add( measurermentsObjectFour );

      flightMeasurementRequirement.setMeasurements( measurementList );
      return flightMeasurementRequirement;
   }


   private FlightMeasurementRequirement buildFlightMeasurementForAssignMeasurement()
         throws JsonProcessingException, IOException {
      Response response =
            createSearch( 200, Credentials.AUTHENTICATED, altId, applicationJson, applicationJson );
      ObjectMapper lObjectMapper =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      List<FlightMeasurementRequirement> flightMeasurementRequirementsActual =
            lObjectMapper.readValue( response.getBody().asString(), lObjectMapper.getTypeFactory()
                  .constructCollectionType( List.class, FlightMeasurementRequirement.class ) );

      FlightMeasurementRequirement flightMeasurementRequirement =
            new FlightMeasurementRequirement();
      flightMeasurementRequirement.setAssemblyId( altId );
      List<Measurement> measurementList = new ArrayList<Measurement>();
      Measurement measurermentsObjectOne = new Measurement();
      measurermentsObjectOne.setDataTypeCode( DATA_TYPE_CODE5 );
      measurermentsObjectOne.setEngineeringUnitCode( ENG_UNIT_CODE5 );

      Measurement measurermentsObjectTwo = new Measurement();
      measurermentsObjectTwo.setDataTypeCode( DATA_TYPE_CODE6 );
      measurermentsObjectTwo.setEngineeringUnitCode( ENG_UNIT_CODE6 );

      measurementList.add( measurermentsObjectOne );
      measurementList.add( measurermentsObjectTwo );
      flightMeasurementRequirement.setMeasurements( measurementList );

      if ( CollectionUtils.isNotEmpty( flightMeasurementRequirementsActual ) ) {
         flightMeasurementRequirement.setLastModifiedDate(
               flightMeasurementRequirementsActual.get( 0 ).getLastModifiedDate() );
      }
      return flightMeasurementRequirement;
   }

}
