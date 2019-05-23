package com.mxi.am.api.resource.sys.refterm.domaintype;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mxi.am.api.helper.Credentials;
import com.mxi.am.api.resource.ObjectMapperFactory;

import io.restassured.RestAssured;
import io.restassured.response.Response;


/**
 * DomainType api e2e test
 *
 */
public class DomainTypeResourceBeanTest {

   private static final String CODE = "CME";
   private static final String DESCRIPTION = "Calendar based domain type";
   private static final String NAME = "Calendar Measurement";

   private static final String APPLICATION_JSON = "application/json";
   private static final String DOMAIN_TYPE_API_PATH = "/amapi/" + DomainType.PATH;
   private final ObjectMapper objectMapper =
         ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();


   @BeforeClass
   public static void setUpClass() {
      try {
         RestAssured.reset();
         RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
         RestAssured.baseURI = "http://".concat( InetAddress.getLocalHost().getHostName() );
         RestAssured.port = 80;

      } catch ( UnknownHostException e ) {
         throw new RuntimeException( "Cannot find local host name." );
      }

   }


   @Test
   public void testGetAllDomainTypeSuccess() throws IOException, ParseException {
      Response response = searchDomainTypes( 200, Credentials.AUTHORIZED );
      List<DomainType> domainTypes = objectMapper.readValue( response.getBody().asString(),
            new TypeReference<List<DomainType>>() {
            } );

      Assert.assertTrue( domainTypes.size() > 1 );
   }


   @Test
   public void testDomainTypeGetByCodeSuccess() throws IOException, ParseException {
      Response response = getByCode( 200, Credentials.AUTHORIZED, CODE );

      DomainType domainType =
            objectMapper.readValue( response.getBody().asString(), DomainType.class );
      Assert.assertTrue( CODE.equals( domainType.getCode() ) );
      Assert.assertTrue( DESCRIPTION.equals( domainType.getDescription() ) );
      Assert.assertTrue( NAME.equals( domainType.getName() ) );
   }


   private Response searchDomainTypes( int statusCode, Credentials security ) {
      String username = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().auth().preemptive().basic( username, password )
            .accept( APPLICATION_JSON ).contentType( APPLICATION_JSON ).expect()
            .statusCode( statusCode ).when().get( DOMAIN_TYPE_API_PATH );
      return response;
   }


   private Response getByCode( int statusCode, Credentials security, String domainTypeCode ) {
      return getByCode( statusCode, security, domainTypeCode, APPLICATION_JSON,
            APPLICATION_JSON );
   }


   private Response getByCode( int statusCode, Credentials security, String domainTypeCode,
         String contentType, String acceptType ) {
      String userName = security.getUserName();
      String password = security.getPassword();

      Response lResponse = RestAssured.given().auth().preemptive().basic( userName, password )
            .accept( acceptType ).contentType( contentType ).expect().statusCode( statusCode )
            .when().get( DOMAIN_TYPE_API_PATH + "/" + domainTypeCode );

      return lResponse;
   }

}
