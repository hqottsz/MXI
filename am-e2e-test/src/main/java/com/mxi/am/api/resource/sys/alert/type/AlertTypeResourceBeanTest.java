package com.mxi.am.api.resource.sys.alert.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.mxi.am.api.helper.Credentials;
import com.mxi.am.api.resource.ObjectMapperFactory;
import com.mxi.am.driver.common.database.DatabaseDriver;
import com.mxi.am.driver.common.database.Result;
import com.mxi.am.driver.common.database.Row;
import com.mxi.am.guice.AssetManagementDatabaseDriverProvider;

import io.restassured.RestAssured;
import io.restassured.response.Response;


/**
 * AlertType API test
 *
 */
public class AlertTypeResourceBeanTest {

   private static final String NUMBER_BOOL_TRUE = "1";

   private static final Integer ALERT_TYPE_ID = 6;

   private static final String APPLICATION_JSON = "application/json";
   private static final String ALERT_TYPE_PATH = "/amapi/" + AlertType.PATH;

   private static final String ALERT_TYPE_QUERY =
         "SELECT * FROM utl_alert_type WHERE alert_type_id =?";
   private static final String ALERT_TYPE_ROLE_QUERY =
         "SELECT * FROM utl_alert_type_role WHERE alert_type_id = ?";
   private static final String ROLE_QUERY = "SELECT * FROM utl_role WHERE role_id = ?";

   private DatabaseDriver driver;

   private AlertType defaultAlertType;


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
   public void setUpData() throws JsonParseException, JsonMappingException, ClassNotFoundException,
         IOException, ParseException, SQLException {
      driver = new AssetManagementDatabaseDriverProvider().get();

      Result alertTypeResult = driver.select( ALERT_TYPE_QUERY, ALERT_TYPE_ID );

      if ( alertTypeResult.isEmpty() ) {
         fail( "Could not find a Alert Type with the Alert Type Id: " + ALERT_TYPE_ID );
      }

      if ( alertTypeResult.getNumberOfRows() > 1 ) {
         fail( "Multiple Alert Types return with the Alert Type Id: " + ALERT_TYPE_ID );
      }

      Result alertTypeRoleResult = driver.select( ALERT_TYPE_ROLE_QUERY, ALERT_TYPE_ID );

      List<String> alertTypeRoleList = new ArrayList<>();

      for ( Row alertTypeRole : alertTypeRoleResult.getRows() ) {

         String roleId = alertTypeRole.get( "role_id" ).toString();

         Result roleResult = driver.select( ROLE_QUERY, roleId );

         if ( roleResult.isEmpty() ) {
            fail( "Could not find a Role with Role Id: " + roleId );
         }

         if ( roleResult.getNumberOfRows() > 1 ) {
            fail( "Multiple Roles return with the Role Id: " + roleId );
         }

         alertTypeRoleList.add( roleResult.get( 0 ).get( "role_cd" ).toString() );
      }

      defaultAlertType = buildAlertType( alertTypeResult, alertTypeRoleList );

   }


   @Test
   public void testGetAlertTypeByIdSuccessReturn200()
         throws JsonParseException, JsonMappingException, IOException {

      Response response = getById( 200, Credentials.AUTHORIZED, defaultAlertType.getId() );
      AlertType alertTypeActual = constructAlertTypeFromResponse( response.getBody().asString() );

      assertEquals( defaultAlertType, alertTypeActual );
   }


   @Test
   public void testAlertTypeGetByIdUnauthenticatedReturns401() {
      getById( 401, Credentials.UNAUTHENTICATED, defaultAlertType.getId() );
   }


   @Test
   public void testAlertTypeGetByIdUnauthorizedReturns403() {
      getById( 403, Credentials.UNAUTHORIZED, defaultAlertType.getId() );
   }


   private Response getById( int statusCode, Credentials security, Integer alerttId ) {
      String username = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().auth().preemptive().basic( username, password )
            .accept( APPLICATION_JSON ).contentType( APPLICATION_JSON ).expect()
            .statusCode( statusCode ).when().get( ALERT_TYPE_PATH + "/" + alerttId );

      return response;
   }


   public static AlertType constructAlertTypeFromResponse( String body )
         throws JsonParseException, JsonMappingException, IOException {
      AlertType alertTypeCreated = ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY
            .createObjectMapper().readValue( body, AlertType.class );
      return alertTypeCreated;

   }


   private AlertType buildAlertType( Result alertTypeResult, List<String> alertTypeRoleList ) {
      defaultAlertType = new AlertType();
      defaultAlertType.setId(
            Integer.parseInt( alertTypeResult.get( 0 ).get( "alert_type_id" ).toString() ) );
      defaultAlertType.setName( alertTypeResult.get( 0 ).get( "alert_name" ).toString() );
      defaultAlertType.setDescription( alertTypeResult.get( 0 ).get( "alert_ldesc" ) );
      defaultAlertType.setNotifyCode( alertTypeResult.get( 0 ).get( "notify_cd" ) );
      defaultAlertType.setNotifyClass( alertTypeResult.get( 0 ).get( "notify_class" ) );
      defaultAlertType.setCategory( alertTypeResult.get( 0 ).get( "category" ) );
      defaultAlertType.setMessage( alertTypeResult.get( 0 ).get( "message" ) );

      defaultAlertType.setKeyBool(
            NUMBER_BOOL_TRUE.equals( alertTypeResult.get( 0 ).get( "key_bool" ).toString() ) );
      defaultAlertType.setPriority(
            Integer.parseInt( alertTypeResult.get( 0 ).get( "priority" ).toString() ) );
      defaultAlertType
            .setPriorityCalcClass( alertTypeResult.get( 0 ).get( "priority_calc_class" ) );
      defaultAlertType.setActive(
            NUMBER_BOOL_TRUE.equals( alertTypeResult.get( 0 ).get( "active_bool" ).toString() ) );

      defaultAlertType.setRoleCodes( alertTypeRoleList.stream()
            .sorted( ( o1, o2 ) -> o1.compareTo( o2 ) ).collect( Collectors.toList() ) );

      return defaultAlertType;

   }

}
