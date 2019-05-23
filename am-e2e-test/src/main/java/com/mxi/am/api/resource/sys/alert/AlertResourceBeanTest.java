package com.mxi.am.api.resource.sys.alert;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mxi.am.api.annotation.CSIContractTest;
import com.mxi.am.api.annotation.CSIContractTest.Project;
import com.mxi.am.api.helper.Credentials;
import com.mxi.am.api.resource.ObjectMapperFactory;

import io.restassured.RestAssured;
import io.restassured.response.Response;


public class AlertResourceBeanTest {

   private static final String APPLICATION_JSON = "application/json";
   private static final String ALERT_PATH = "/amapi/" + Alert.PATH;
   private static final String ALERT_PATH_DELETE = "/amapi/" + Alert.PATH + "/delete";

   private static final Integer ALERT_TYPE_ID = 22;
   private static final Integer SECOND_ALERT_TYPE_ID = 2;

   private static final String ALERT_PARAMETER_TYPE = "STRING";
   private static final String ALERT_PARAMETER_VALUE = "MX_CORE_PROCESS_AUTO_RSRV_CONTROLLER";
   private static final String ALERT_PARAMETER_DISPLAY_VALUE =
         "MX_CORE_PROCESS_AUTO_RSRV_CONTROLLER";
   private Alert alert;
   private AlertParameter alertParameter;


   @BeforeClass
   public static void setUpClass() throws JsonParseException, JsonMappingException,
         ClassNotFoundException, IOException, ParseException, SQLException {
      try {
         RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
         RestAssured.baseURI = "http://".concat( InetAddress.getLocalHost().getHostName() );

         RestAssured.port = 80;

      } catch ( UnknownHostException e ) {
         throw new RuntimeException( "Cannot find local host name." );
      }

   }


   @Before
   public void prepareData() throws JsonParseException, JsonMappingException, IOException,
         ParseException, ClassNotFoundException, SQLException {
      // create a alert to be used in tests
      Alert newAlert = buildAlert();
      Response response = create( 200, Credentials.AUTHENTICATED, newAlert, ALERT_PATH );
      alert = constructAlertFromResponse( response.getBody().asString() );

   }


   /**
    *
    * Test get method for success
    *
    * @throws JsonParseException
    * @throws JsonMappingException
    * @throws IOException
    */
   @Test
   public void testGetAlertByIdSuccessReturn200()
         throws JsonParseException, JsonMappingException, IOException {
      Response response = getById( 200, Credentials.AUTHENTICATED, alert.getAlertId() );
      Alert actualAlert = ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper()
            .readValue( response.getBody().asString(), Alert.class );
      Assert.assertEquals( alert, actualAlert );
   }


   /**
    *
    * Test get method for unauthenticated access
    *
    */
   @Test
   public void testAlertGetByIdUnauthenticatedReturns401() {
      Response response = getById( 401, Credentials.UNAUTHENTICATED, alert.getAlertId() );
      Assert.assertEquals( 401, response.getStatusCode() );
   }


   /**
    *
    * Test get method for Unauthorized access
    *
    */
   @Test
   public void testAlertGetByIdUnauthorizedReturns403() {
      Response response = getById( 403, Credentials.UNAUTHORIZED, alert.getAlertId() );
      Assert.assertEquals( 403, response.getStatusCode() );
   }


   /**
    *
    * Test search method for success
    *
    * @throws IOException
    * @throws JsonMappingException
    * @throws JsonParseException
    *
    */
   @Test
   public void testAlertSearchSuccessReturns200()
         throws JsonParseException, JsonMappingException, IOException {
      Response response = search( 200, Credentials.AUTHORIZED );
      assertAlertSearch( response );
   }


   /**
    *
    * Test search method for unauthenticated
    *
    */
   @Test
   public void testAlertSearchUnauthenticatedReturns401() {
      Response response = search( 401, Credentials.UNAUTHENTICATED );
      Assert.assertEquals( 401, response.getStatusCode() );
   }


   /**
    *
    * Test search method for Unauthorized
    *
    */
   @Test
   public void testAlertSearchUnauthorizedReturns403() {
      Response response = search( 403, Credentials.UNAUTHORIZED );
      Assert.assertEquals( 403, response.getStatusCode() );
   }


   /**
    *
    * Test Create method for Success
    *
    */

   @Test
   @CSIContractTest( Project.UPS )
   public void testAlertCreateSuccess200()
         throws JsonParseException, JsonMappingException, IOException {
      Alert alertExpected = buildAlert();
      Response response = create( 200, Credentials.AUTHENTICATED, alertExpected, ALERT_PATH );
      Alert createdAlert = constructAlertFromResponse( response.getBody().asString() );
      assertCreatedAlert( alertExpected, createdAlert );
   }


   /**
    *
    * Test Create method for unauthenticated
    *
    */

   @Test
   public void testCreateAlertUnauthenticatedReturns401() {
      Alert alertExpected = buildAlert();
      Response response = create( 401, Credentials.UNAUTHENTICATED, alertExpected, ALERT_PATH );
      Assert.assertEquals( 401, response.getStatusCode() );
   }


   /**
    *
    * Test create method for Unauthorized
    *
    */
   @Test
   public void testCreateAlertUnauthorizedReturns403() {
      Alert alertExpected = buildAlert();
      Response response = create( 403, Credentials.UNAUTHORIZED, alertExpected, ALERT_PATH );
      Assert.assertEquals( 403, response.getStatusCode() );
   }


   /**
    *
    * Test method for delete
    *
    */
   @Test
   public void testDeleteAlertSuccess204()
         throws JsonParseException, JsonMappingException, IOException {
      Alert alert = builAlertForDelelete();
      Response response = create( 200, Credentials.AUTHENTICATED, alert, ALERT_PATH );
      Alert alertCreated = constructAlertFromResponse( response.getBody().asString() );
      Response responseForDelete = delete( 204, Credentials.AUTHENTICATED, alertCreated );
      Assert.assertEquals( 204, responseForDelete.getStatusCode() );

   }


   /**
    *
    * Test Delete method for unauthenticated
    *
    * @throws IOException
    * @throws JsonMappingException
    * @throws JsonParseException
    *
    */

   @Test
   public void testDeleteAlertUnauthenticatedReturns401()
         throws JsonParseException, JsonMappingException, IOException {
      Alert alert = builAlertForDelelete();
      Response response = create( 200, Credentials.AUTHENTICATED, alert, ALERT_PATH );
      Alert alertCreated = constructAlertFromResponse( response.getBody().asString() );
      Response responseForDelete = delete( 401, Credentials.UNAUTHENTICATED, alertCreated );
      Assert.assertEquals( 401, responseForDelete.getStatusCode() );
   }


   /**
    *
    * Test Delete method for Unauthorized
    *
    * @throws IOException
    * @throws JsonMappingException
    * @throws JsonParseException
    *
    */
   @Test
   public void testDeleteAlertUnauthorizedReturns403()
         throws JsonParseException, JsonMappingException, IOException {
      Alert alert = builAlertForDelelete();
      Response response = create( 200, Credentials.AUTHENTICATED, alert, ALERT_PATH );
      Alert alertCreated = constructAlertFromResponse( response.getBody().asString() );
      Response responseForDelete = delete( 403, Credentials.UNAUTHORIZED, alertCreated );
      Assert.assertEquals( 403, responseForDelete.getStatusCode() );
   }


   private Response delete( int statusCode, Credentials security, Alert alertCreated ) {
      String username = security.getUserName();
      String password = security.getPassword();
      Response response = RestAssured.given()
            .queryParam( AlertSearchParameters.ALERT_TYPE_ID_PARAM, alertCreated.getAlertTypeId() )
            .queryParam( AlertSearchParameters.STATUS_CODE_PARAM,
                  alertCreated.getAlertStatusCode() )
            .auth().preemptive().basic( username, password ).accept( APPLICATION_JSON )
            .contentType( APPLICATION_JSON ).expect().statusCode( statusCode ).when()
            .delete( ALERT_PATH_DELETE );

      return response;

   }


   private void assertCreatedAlert( Alert alertExpected, Alert createdAlert ) {
      Assert.assertEquals( alertExpected.getParameters(), createdAlert.getParameters() );
      Assert.assertEquals( alertExpected.getAlertTypeId(), createdAlert.getAlertTypeId() );
   }


   private Response search( int statusCode, Credentials security ) {
      String username = security.getUserName();
      String password = security.getPassword();
      Response response = RestAssured.given()
            .queryParam( AlertSearchParameters.ALERT_TYPE_ID_PARAM, alert.getAlertTypeId() )
            .queryParam( AlertSearchParameters.STATUS_CODE_PARAM, alert.getAlertStatusCode() )
            .auth().preemptive().basic( username, password ).accept( APPLICATION_JSON )
            .contentType( APPLICATION_JSON ).expect().statusCode( statusCode ).when()
            .get( ALERT_PATH );
      return response;

   }


   private static Response create( int statusCode, Credentials security, Object object,
         String path ) {
      String userName = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().auth().preemptive().basic( userName, password )
            .accept( APPLICATION_JSON ).contentType( APPLICATION_JSON ).body( object ).expect()
            .statusCode( statusCode ).when().post( ALERT_PATH );
      return response;

   }


   private Response getById( int statusCode, Credentials security, Integer alerttId ) {
      String username = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().auth().preemptive().basic( username, password )
            .accept( APPLICATION_JSON ).contentType( APPLICATION_JSON ).expect()
            .statusCode( statusCode ).when().get( ALERT_PATH + "/" + alerttId );

      return response;
   }


   /**
    * Creates a default Alert object
    *
    * @throws IOException
    * @throws JsonMappingException
    * @throws JsonParseException
    */
   public static Alert constructAlertFromResponse( String body )
         throws JsonParseException, JsonMappingException, IOException {
      Alert alertCreated = ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper()
            .readValue( body, Alert.class );
      return alertCreated;

   }


   private void assertAlertSearch( Response actualAlertResponse )
         throws JsonParseException, JsonMappingException, IOException {
      ObjectMapper objectMapper =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      List<Alert> alertList = objectMapper.readValue( actualAlertResponse.getBody().asString(),
            objectMapper.getTypeFactory().constructCollectionType( List.class, Alert.class ) );
      Assert.assertTrue( "The alert was not in the response", alertList.contains( alert ) );
   }


   private Alert buildAlert() {
      Alert alert = new Alert();
      alert.setAlertTypeId( ALERT_TYPE_ID );
      alertParameter = new AlertParameter();
      alertParameter.setDisplayValue( ALERT_PARAMETER_DISPLAY_VALUE );
      alertParameter.setType( ALERT_PARAMETER_TYPE );
      alertParameter.setValue( ALERT_PARAMETER_VALUE );
      List<AlertParameter> alertParameterList = new ArrayList<>();
      alertParameterList.add( alertParameter );
      alert.setParameters( alertParameterList );
      return alert;

   }


   private Alert builAlertForDelelete() {
      Alert alert = new Alert();
      alert.setAlertTypeId( SECOND_ALERT_TYPE_ID );
      alertParameter = new AlertParameter();
      alertParameter.setDisplayValue( ALERT_PARAMETER_DISPLAY_VALUE );
      alertParameter.setType( ALERT_PARAMETER_TYPE );
      alertParameter.setValue( ALERT_PARAMETER_VALUE );
      List<AlertParameter> alertParameterList = new ArrayList<>();
      alertParameterList.add( alertParameter );
      alert.setParameters( alertParameterList );
      return alert;

   }

}
