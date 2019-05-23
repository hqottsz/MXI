package com.mxi.am.api.resource.maintenance.exec.task.measurement;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.mxi.am.api.helper.Credentials;

import io.restassured.RestAssured;


/**
 * e2e test class for the Task Measurement API.
 *
 */
public class MeasurementResourceBeanTest {

   private final static String APPLICATION_JSON = "application/json";
   private final static String MEASUREMENT_PATH = "/amapi/" + Measurement.PATH;


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


   @Test
   public void testGetByIdUnauthenticated401() {

      searchByParameters( 401, Credentials.UNAUTHENTICATED, new MeasurementSearchParameters() );
   }


   @Test
   public void testGetByIdUnauthorized403() {

      searchByParameters( 403, Credentials.UNAUTHORIZED, new MeasurementSearchParameters() );
   }


   private void searchByParameters( int statusCode, Credentials security,
         MeasurementSearchParameters parameters ) {
      String username = security.getUserName();
      String password = security.getPassword();

      RestAssured.given()
            .queryParam( MeasurementSearchParameters.TASK_BARCODE_PARAM,
                  parameters.getTaskBarcode() )
            .queryParam( MeasurementSearchParameters.TASK_ID_PARAM, parameters.getTaskId() ).auth()
            .preemptive().basic( username, password ).accept( APPLICATION_JSON )
            .contentType( APPLICATION_JSON ).expect().statusCode( statusCode ).when()
            .get( MEASUREMENT_PATH );
   }
}
