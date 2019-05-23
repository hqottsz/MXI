package com.mxi.mx.common.restassured;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URLEncoder;

import org.junit.rules.ExternalResource;

import com.mxi.am.api.helper.Credentials;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;


/**
 * This rule configures Rest Assured
 */
public class RestAssuredRule extends ExternalResource {

   private static final int STATUS_CODE_LOGIN_SUCCESS = 303;
   private final SessionFilter iSessionFilter = new SessionFilter();
   private String iUsername;
   private String iPassword;


   /**
    * Creates a new {@linkplain RestAssuredRule} object.
    *
    */
   public RestAssuredRule() {
   }


   /**
    *
    * Creates a new {@linkplain RestAssuredRule} object.
    *
    * @param aUsername
    * @param aPassword
    */
   public RestAssuredRule(String aUsername, String aPassword) {
      iUsername = aUsername;
      iPassword = aPassword;
   }


   @Override
   public void before() {
      try {
         RestAssured.reset();
         RestAssured.config = RestAssured.config
               .encoderConfig( RestAssured.config.getEncoderConfig()
                     .appendDefaultContentCharsetToContentTypeIfUndefined( false ) )
               .logConfig( RestAssured.config.getLogConfig()
                     .enableLoggingOfRequestAndResponseIfValidationFails()
                     .enablePrettyPrinting( false ) );
         RestAssured.baseURI = "http://".concat( InetAddress.getLocalHost().getHostName() );
         RestAssured.port = 80;

         // We manually write the authentication here as the default one provided by RestAssured
         // does not work. RestAssured tries adding the j_username and j_password to the URI instead
         // of the form body. This causes the authentication to fail.
         Response lResponse = RestAssured.given().redirects().follow( false )
               .filter( iSessionFilter ).when().contentType( ContentType.URLENC )
               .body( "j_username=" + URLEncoder.encode( iUsername, "UTF-8" ) + "&j_password="
                     + URLEncoder.encode( iPassword, "UTF-8" ) )
               .post( "/maintenix/j_security_check" );

         // Maintenix returns a 303 on a successful login and a 302 on a failed login.
         if ( lResponse.getStatusCode() != STATUS_CODE_LOGIN_SUCCESS ) {
            fail( "Authentication Failed. Username: " + iUsername + " Password: " + iPassword );
         }
      } catch ( IOException e ) {
         throw new RuntimeException( "Cannot setup RestAssured.", e );
      }
   }


   public SessionFilter getSessionFilter() {
      return iSessionFilter;
   }


   public RequestSpecification defaultRequest() {
      return RestAssured.given().filter( getSessionFilter() ).contentType( ContentType.JSON )
            .accept( ContentType.JSON ).log().ifValidationFails().then().log().ifValidationFails()
            .given();
   }


   public void setCredentials( Credentials credentials ) {
      iUsername = credentials.getUserName();
      iPassword = credentials.getPassword();
   }

}
