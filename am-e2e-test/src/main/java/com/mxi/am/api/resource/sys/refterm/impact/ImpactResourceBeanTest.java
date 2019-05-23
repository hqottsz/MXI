package com.mxi.am.api.resource.sys.refterm.impact;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mxi.am.api.annotation.CSIContractTest;
import com.mxi.am.api.annotation.CSIContractTest.Project;
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.helper.Credentials;
import com.mxi.am.api.resource.ObjectMapperFactory;

import io.restassured.RestAssured;
import io.restassured.response.Response;


/**
 * e2e test for Impact ResourceBean
 *
 */
public class ImpactResourceBeanTest {

   private static final String IMPACT_CODE = "ETOPS";
   public static final String IMPACT_NAME = "ETOPS";
   public static final String IMPACT_DESCRIPTION = "ETOPS Description...";

   private static final String IMPACT_CODE2 = "EL";
   public static final String IMPACT_NAME2 = "Electrical Load";
   public static final String IMPACT_DESCRIPTION2 = "Electrical Load Description...";

   private static final String IMPACT_CODE3 = "WB";
   public static final String IMPACT_NAME3 = "Weight and Balance";
   public static final String IMPACT_DESCRIPTION3 = "Weight and Balance Description...";

   private static final String APPLICATION_JSON = "application/json";
   private static final String IMPACT_API_PATH = "/amapi/" + Impact.PATH;

   private static final int IMPACT_RECORD_COUNT = 4;
   private static final String IMPACT_CODE_DOUBLE = "WB";


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
   @CSIContractTest( { Project.AFKLM_IMECH } )
   public void testGetImpactByCodeSuccess200()
         throws JsonProcessingException, IOException, ParseException {
      Response response = getImpactByCode( 200, Credentials.AUTHENTICATED, IMPACT_CODE );
      assertImpactForGet( defaultImpactBuilder(), response );
   }


   @Test
   @CSIContractTest( { Project.AFKLM_IMECH } )
   public void testSearchAllImpactsSuccess200()
         throws JsonParseException, JsonMappingException, IOException {
      Response response = searchAllImpacts( 200, Credentials.AUTHENTICATED );
      assertImpactForSearch( defaultImpactListBuilder(), response );
   }


   @Test
   public void testGetImpactByCodeUnauthenticatedFailure401() {
      getImpactByCode( 401, Credentials.UNAUTHENTICATED, IMPACT_CODE );
   }


   @Test
   public void testSearchAllImpactsUnauthenticatedFailure401() {
      searchAllImpacts( 401, Credentials.UNAUTHENTICATED );
   }


   @Test
   public void testGetImpactByCodeUnauthorizedFailure403() {
      getImpactByCode( 403, Credentials.UNAUTHORIZED, IMPACT_CODE );
   }


   @Test
   public void testSearchAllImpactsUnauthorizedFailure403() {
      searchAllImpacts( 403, Credentials.UNAUTHORIZED );
   }


   @Test
   public void testGetImpactByCodeReturnsMultipleResultsFailure500()
         throws AmApiResourceNotFoundException {
      getImpactByCode( 500, Credentials.AUTHENTICATED, IMPACT_CODE_DOUBLE );
   }


   private Response getImpactByCode( int statusCode, Credentials security, String impactCode ) {
      String userName = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().pathParam( "code", impactCode ).auth().preemptive()
            .basic( userName, password ).accept( APPLICATION_JSON ).contentType( APPLICATION_JSON )
            .expect().statusCode( statusCode ).when().get( IMPACT_API_PATH + "/{code}" );
      return response;
   }


   private Response searchAllImpacts( int statusCode, Credentials security ) {
      String username = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().auth().preemptive().basic( username, password )
            .accept( APPLICATION_JSON ).contentType( APPLICATION_JSON ).expect()
            .statusCode( statusCode ).when().get( IMPACT_API_PATH );
      return response;
   }


   private Impact defaultImpactBuilder() {
      Impact impact = new Impact();
      impact.setCode( IMPACT_CODE );
      impact.setName( IMPACT_NAME );
      impact.setDescription( IMPACT_DESCRIPTION );
      return impact;
   }


   private List<Impact> defaultImpactListBuilder() {

      List<Impact> impactList = new ArrayList<Impact>();

      Impact impact1 = new Impact();
      impact1.setCode( IMPACT_CODE );
      impact1.setName( IMPACT_NAME );
      impact1.setDescription( IMPACT_DESCRIPTION );

      Impact impact2 = new Impact();
      impact2.setCode( IMPACT_CODE2 );
      impact2.setName( IMPACT_NAME2 );
      impact2.setDescription( IMPACT_DESCRIPTION2 );

      Impact impact3 = new Impact();
      impact3.setCode( IMPACT_CODE3 );
      impact3.setName( IMPACT_NAME3 );
      impact3.setDescription( IMPACT_DESCRIPTION3 );

      impactList.add( impact1 );
      impactList.add( impact2 );
      impactList.add( impact3 );

      return impactList;
   }


   private void assertImpactForGet( Impact impactExpected, Response impactActual )
         throws JsonProcessingException, IOException {
      ObjectMapper objectMapper =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      Impact actualImpact = objectMapper.readValue( impactActual.getBody().asString(),
            objectMapper.getTypeFactory().constructType( Impact.class ) );
      Assert.assertEquals( impactExpected, actualImpact );
   }


   private void assertImpactForSearch( List<Impact> impactListExpected, Response impactListActual )
         throws JsonProcessingException, IOException {
      ObjectMapper objectMapper =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      List<Impact> actualImpactList = objectMapper.readValue( impactListActual.getBody().asString(),
            objectMapper.getTypeFactory().constructCollectionType( List.class, Impact.class ) );
      assertEquals( IMPACT_RECORD_COUNT, actualImpactList.size() );
      Assert.assertTrue( actualImpactList.containsAll( impactListExpected ) );
   }

}
