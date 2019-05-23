package com.mxi.mx.api.partgroups;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

import javax.ws.rs.core.Response.Status;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.mxi.am.api.helper.Credentials;
import com.mxi.mx.api.CorrelationId;
import com.mxi.mx.api.configurationmanager.publish.model.AlternatePartSet;
import com.mxi.mx.api.configurationmanager.publish.model.BasePartGroup;
import com.mxi.mx.api.configurationmanager.publish.model.BatchAlternatePart;
import com.mxi.mx.api.configurationmanager.publish.model.BatchPartGroup;
import com.mxi.mx.api.serialization.mapper.ObjectMapperFactory;

import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.mapper.factory.Jackson2ObjectMapperFactory;
import io.restassured.response.Response;


public class ConfigurationManagerPublishResourceTest {

   private static final BigDecimal BATCH_QUANTITY = new BigDecimal( 1.2 );
   private static final String OTHER_CONDITIONS = "other condition";
   private static final String STANDARD_PART_NUMBER = "API003";
   private static final String STANDARD_PART_MANUFACTURER = "API01";
   private static final String APPLICABILITY_RANGE = "100,200-300";
   private static final String ATA_CODE = "SYS-1";
   private static final String ASSEMBLY_CODE = "ACFTAPI1";
   private static final String NAME = "Part Group 1";
   private static final String CODE = "PG1";

   private static final String EMPTY_STRING = "";

   private static final int HTTP_MULTI_STATUS = 207;
   private static final int HTTP_BAD_REQUEST_ERROR = 400;
   private static final int HTTP_UNPROCESSABLE_ENTITY_STATUS = 422;

   private static final String APPLICATION_JSON = "application/json";
   private static final String APPLICATION_XML = "application/xml";


   @BeforeClass
   public static void setUpClass() {
      try {
         JacksonJsonProvider lJsonProvider = new JacksonJsonProvider();

         final ObjectMapper sObjectMapper = ObjectMapperFactory.buildMapper();
         lJsonProvider.setMapper( sObjectMapper );

         RestAssured.reset();
         RestAssured.config =
               RestAssuredConfig.config().objectMapperConfig( new ObjectMapperConfig()
                     .jackson2ObjectMapperFactory( new Jackson2ObjectMapperFactory() {

                        @Override
                        public ObjectMapper create( @SuppressWarnings( "rawtypes" ) Class aClass,
                              String s ) {
                           return sObjectMapper;
                        }
                     } ) );
         RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
         RestAssured.baseURI =
               "http://".concat( InetAddress.getLocalHost().getHostName() ).concat( "/mxapi" );

         RestAssured.basePath = "/configurationManager/alternateParts/publish";
         RestAssured.port = 80;

      } catch ( UnknownHostException e ) {
         throw new RuntimeException( "Cannot find local host name." );
      }
   }


   @Test
   public void
         publish_partGroupThatDoesNotMeetTheSpecAndPartGroupThatViolatesBusinessLogicAndSuccessfulPartGroupReturns207With400And422And200()
               throws JsonProcessingException, IOException {
      BasePartGroup lSpecViolatingPartGroup = defaultBatchPartGroup().code( null );
      BasePartGroup lBusinessViolatingPartGroup = defaultBatchPartGroup().ataCode( "not existing" );
      BasePartGroup lSuccessfulPartGroup = defaultBatchPartGroup();

      Response lResponse = publish( HTTP_MULTI_STATUS, Credentials.AUTHENTICATED,
            lSpecViolatingPartGroup, lBusinessViolatingPartGroup, lSuccessfulPartGroup, null );

      JsonNode lPartGroupsJson = new ObjectMapper().readTree( lResponse.getBody().asString() );
      assertEquals( 4, lPartGroupsJson.size() );

      JsonNode lJSONPartGroupOne = lPartGroupsJson.get( 0 );
      assertEquals( Status.BAD_REQUEST.getStatusCode(),
            lJSONPartGroupOne.get( "httpStatusCode" ).asInt() );

      JsonNode lJSONPartGroupTwo = lPartGroupsJson.get( 1 );
      assertEquals( HTTP_UNPROCESSABLE_ENTITY_STATUS,
            lJSONPartGroupTwo.get( "httpStatusCode" ).asInt() );

      JsonNode lJSONPartGroupThree = lPartGroupsJson.get( 2 );
      assertEquals( Status.OK.getStatusCode(),
            lJSONPartGroupThree.get( "httpStatusCode" ).asInt() );

      JsonNode lJSONPartGroupFour = lPartGroupsJson.get( 3 );
      assertEquals( Status.BAD_REQUEST.getStatusCode(),
            lJSONPartGroupFour.get( "httpStatusCode" ).asInt() );
   }


   @Test
   public void publish_withSingleCorrelationIdReturns207WithCorrelationIdHeader() {
      String lCorrelationId = "correlationId1";
      Response lResponse = RestAssured.given().auth().preemptive()
            .basic( Credentials.AUTHENTICATED.getUserName(),
                  Credentials.AUTHENTICATED.getPassword() )
            .accept( APPLICATION_JSON ).contentType( APPLICATION_JSON )
            .header( CorrelationId.HEADER, lCorrelationId )
            .body( Arrays.asList( defaultBatchPartGroup() ) ).post();

      assertStatus( lResponse, HTTP_MULTI_STATUS );

      assertEquals( 1, lResponse.getHeaders().getList( CorrelationId.HEADER ).size() );
   }


   @Test
   public void publish_withMultipleCorrelationIdsReturns400() {
      Response lResponse = RestAssured.given().auth().preemptive()
            .basic( Credentials.AUTHENTICATED.getUserName(),
                  Credentials.AUTHENTICATED.getPassword() )
            .accept( APPLICATION_JSON )
            .contentType( APPLICATION_JSON ).headers( CorrelationId.HEADER, "correlationId1",
                  CorrelationId.HEADER, "correlationId2" )
            .body( Arrays.asList( defaultBatchPartGroup() ) ).post();
      assertStatus( lResponse, HTTP_BAD_REQUEST_ERROR );
   }


   @Test
   public void publish_emptyArrayReturns400() {
      publish( 400, Credentials.AUTHENTICATED, "[]" );
   }


   @Test
   public void publish_objectAsRootElementReturns400() {
      String lJsonObject = "{\"Invalid Part Group\": \"Body\"}";
      publish( 400, Credentials.AUTHENTICATED, lJsonObject );
   }


   @Test
   public void publish_emptyPostBodyReturns400() {
      publish( 400, Credentials.AUTHENTICATED, EMPTY_STRING );
   }


   @Test
   public void publish_unauthenticatedReturns401() {
      BatchPartGroup lBatchPartGroup = defaultBatchPartGroup();
      publish( 401, Credentials.UNAUTHENTICATED, lBatchPartGroup );
   }


   @Test
   public void publish_unauthorizedReturns403() {
      BatchPartGroup lBatchPartGroup = defaultBatchPartGroup();
      publish( 403, Credentials.UNAUTHORIZED, lBatchPartGroup );
   }


   @Test
   public void publish_notAcceptableReturns406() {
      BatchPartGroup lBatchPartGroup = defaultBatchPartGroup();
      publish( 406, Credentials.AUTHENTICATED, APPLICATION_JSON, APPLICATION_XML, lBatchPartGroup );
   }


   @Ignore( "This is working externally, but not with RESTAssured" )
   @Test
   public void publish_unsupportedMediaTypeReturns415() {
      BatchPartGroup lBatchPartGroup = defaultBatchPartGroup();
      publish( 415, Credentials.AUTHENTICATED, APPLICATION_XML, APPLICATION_JSON, lBatchPartGroup );
   }


   public void assertStatus( Response aResponse, int aCode ) {
      assertEquals( aResponse.getBody().asString(), aCode, aResponse.getStatusCode() );
   }


   private Response publish( int aStatusCode, Credentials aSecurity, Object... aPartGroup ) {
      return publish( aStatusCode, aSecurity, APPLICATION_JSON, APPLICATION_JSON, aPartGroup );
   }


   private Response publish( int aStatusCode, Credentials aSecurity, String aContentType,
         String aAcceptType, Object... aPartGroup ) {
      String lUserName = aSecurity.getUserName();
      String lPassword = aSecurity.getPassword();

      Response lResponse = RestAssured.given().auth().preemptive().basic( lUserName, lPassword )
            .accept( aAcceptType ).contentType( aContentType )
            .body( aPartGroup == null ? "" : Arrays.asList( aPartGroup ) ).post();
      assertStatus( lResponse, aStatusCode );
      return lResponse;
   }


   private BatchPartGroup defaultBatchPartGroup() {
      BatchAlternatePart lBatchAlternatePart = new BatchAlternatePart();
      lBatchAlternatePart.partManufacturer( STANDARD_PART_MANUFACTURER )
            .partNumber( STANDARD_PART_NUMBER ).hasOtherConditions( true ).approved( true )
            .applicabilityRange( APPLICABILITY_RANGE );
      AlternatePartSet<BatchAlternatePart> lBatchAlternateParts =
            new AlternatePartSet<BatchAlternatePart>();
      lBatchAlternateParts.add( lBatchAlternatePart );

      BatchPartGroup lBatchPartGroup = new BatchPartGroup();
      lBatchPartGroup.quantity( BATCH_QUANTITY ).alternateParts( lBatchAlternateParts ).code( CODE )
            .name( NAME ).assemblyCode( ASSEMBLY_CODE ).ataCode( ATA_CODE )
            .applicabilityRange( APPLICABILITY_RANGE ).lineReplaceableUnit( true )
            .mustRequestSpecificPart( true )
            .standardAlternatePartManufacturer( STANDARD_PART_MANUFACTURER )
            .standardAlternatePartNumber( STANDARD_PART_NUMBER )
            .otherConditions( OTHER_CONDITIONS );
      return lBatchPartGroup;
   }
}
