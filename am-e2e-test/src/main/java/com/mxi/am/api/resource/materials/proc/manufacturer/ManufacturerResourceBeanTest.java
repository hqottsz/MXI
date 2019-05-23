package com.mxi.am.api.resource.materials.proc.manufacturer;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mxi.am.api.helper.Credentials;
import com.mxi.am.driver.common.database.DatabaseDriver;
import com.mxi.am.driver.common.database.Result;
import com.mxi.am.guice.AssetManagementDatabaseDriverProvider;

import io.restassured.RestAssured;
import io.restassured.response.Response;


/**
 * e2e test class for the Manufacturer API to test permissions and response codes
 *
 */
public class ManufacturerResourceBeanTest {

   private DatabaseDriver driver;

   private final static String APPLICATION_JSON = "application/json";
   private final static String MANUFACTURER_PATH = "/amapi/" + Manufacturer.PATH;

   private final static String MANUFACT_CD = "00001";
   private final static String MANUFACT_NAME = "Manufacturer Name 1";

   private String manufactId;

   private final String queryEqpManufact = "SELECT * FROM eqp_manufact WHERE manufact_cd = ?";


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

      Result manufact = driver.select( queryEqpManufact, MANUFACT_CD );

      manufactId = manufact.get( 0 ).getUuidString( "alt_id" );

   }


   @Test
   public void testGetByIdSuccess200() {
      getById( 200, Credentials.AUTHENTICATED, manufactId );
   }


   @Test
   public void testGetByNameSuccess200() {
      getByParams( 200, Credentials.AUTHENTICATED, MANUFACT_CD, null );
   }


   @Test
   public void testGetByCodeSuccess200() {
      getByParams( 200, Credentials.AUTHENTICATED, null, MANUFACT_NAME );
   }


   @Test
   public void testGetByNullId400() {
      getById( 400, Credentials.AUTHENTICATED, null );
   }


   @Test
   public void testGetByIdUnauthenticated401() {

      getById( 401, Credentials.UNAUTHENTICATED, manufactId );
   }


   @Test
   public void testGetByIdUnauthorized403() {

      getById( 403, Credentials.UNAUTHORIZED, manufactId );
   }


   private Response getById( int statusCode, Credentials security, String manufactId ) {

      String username = security.getUserName();
      String password = security.getPassword();

      Response lResponse = RestAssured.given().auth().preemptive().basic( username, password )
            .accept( APPLICATION_JSON ).contentType( APPLICATION_JSON ).expect()
            .statusCode( statusCode ).when().get( MANUFACTURER_PATH + "/" + manufactId );

      return lResponse;
   }


   private Response getByParams( int statusCode, Credentials security, String code, String name ) {

      String username = security.getUserName();
      String password = security.getPassword();

      Response lResponse = RestAssured.given().auth().preemptive().basic( username, password )
            .accept( APPLICATION_JSON ).contentType( APPLICATION_JSON ).expect()
            .statusCode( statusCode ).when()
            .get( MANUFACTURER_PATH + "?code=" + code + "&name=" + name );

      return lResponse;
   }
}
