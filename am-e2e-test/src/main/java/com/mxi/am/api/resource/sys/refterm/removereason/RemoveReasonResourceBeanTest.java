package com.mxi.am.api.resource.sys.refterm.removereason;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mxi.am.api.helper.Credentials;
import com.mxi.am.api.resource.ObjectMapperFactory;

import io.restassured.RestAssured;
import io.restassured.response.Response;


/**
 * Remove Reason API test
 *
 */
public class RemoveReasonResourceBeanTest {

   private static ObjectMapper objectMapper;

   private final String applicationJson = "application/json";
   private final String removeReasonPath = "/amapi/" + RemoveReason.PATH;


   @BeforeClass
   public static void setUpClass() {
      try {
         RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
         RestAssured.baseURI = "http://".concat( InetAddress.getLocalHost().getHostName() );

         RestAssured.port = 80;

      } catch ( UnknownHostException e ) {
         throw new RuntimeException( "Cannot find local host name." );
      }

      objectMapper = ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();

   }


   @Test
   public void testSearchRemoveReasonSuccess()
         throws JsonParseException, JsonMappingException, IOException {
      Response response =
            searchRemoveReason( 200, Credentials.AUTHENTICATED, applicationJson, applicationJson );
      try {
         List<RemoveReason> RemoveReasonList =
               objectMapper.readValue( response.getBody().asString(), objectMapper.getTypeFactory()
                     .constructCollectionType( List.class, RemoveReason.class ) );
      } catch ( Exception e ) {
         Assert.fail( "Did not return a list of RemoveReason, instead returned: "
               + response.getBody().asString() );
      }
   }


   @Test
   public void testSearchRemoveReasonUnauthorized() {
      searchRemoveReason( 403, Credentials.UNAUTHORIZED, applicationJson, applicationJson );
   }


   @Test
   public void testSearchRemoveReasonUnauthenticated() {
      searchRemoveReason( 401, Credentials.UNAUTHENTICATED, applicationJson, applicationJson );
   }


   private Response searchRemoveReason( int statusCode, Credentials security, String contentType,
         String acceptType ) {
      String userName = security.getUserName();
      String password = security.getPassword();

      return RestAssured.given().auth().preemptive().basic( userName, password )
            .accept( acceptType ).contentType( contentType ).expect().statusCode( statusCode )
            .when().get( removeReasonPath );
   }

}
