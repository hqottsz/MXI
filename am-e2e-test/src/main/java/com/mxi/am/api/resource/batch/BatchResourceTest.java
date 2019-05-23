package com.mxi.am.api.resource.batch;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.google.common.base.Joiner;
import com.mxi.am.api.helper.Credentials;
import com.mxi.am.api.resource.ObjectMapperFactory;
import com.mxi.am.api.resource.erp.location.Location;
import com.mxi.am.api.resource.erp.location.LocationSearchParameters;

import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.mapper.factory.Jackson2ObjectMapperFactory;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;


public class BatchResourceTest {

   private static final int MULTI_STATUS = 207;
   private static final String COUNTRY_CODE = "0:CAN";
   private static final String STATE_CODE = "AB";
   private static final String NAME_AIRPORT = "Test Airport";
   private static final String NAME_HANGAR = "Test Hangar";
   private static final String NAME_LINE = "Test Line";
   private static final String TYPE_AIRPORT = "AIRPORT";
   private static final String TYPE_HANGAR = "HGR";
   private static final String TYPE_LINE = "LINE";
   private static final String LOC_URI = "erp/location";


   @BeforeClass
   public static void setup() {
      try {

         JacksonJsonProvider lJsonProvider = new JacksonJsonProvider();

         final ObjectMapper sObjectMapper =
               ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
         lJsonProvider.setMapper( sObjectMapper );

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
               "http://".concat( InetAddress.getLocalHost().getHostName() ).concat( "/amapi" );
         RestAssured.port = 80;

      } catch ( UnknownHostException e ) {
         throw new RuntimeException( "Cannot find local host name." );
      }
   }


   @Test
   public void batchWithOneApiCall() throws JsonProcessingException {

      // GIVEN batch with a an api call to create a location
      String lLocationCodeSuffix = String.valueOf( System.currentTimeMillis() );

      Location lNewLocation = new Location.Builder().code( "TA-" + lLocationCodeSuffix )
            .type( TYPE_AIRPORT ).name( NAME_AIRPORT ).isSupply( false ).stateCode( STATE_CODE )
            .countryCode( COUNTRY_CODE ).build();

      BatchBuilder lBatchBuilder = new BatchBuilder();
      lBatchBuilder.request( LOC_URI ).post().body( lNewLocation ).acceptJson().contentJson().add();

      // WHEN the batch api is called
      Response lResponse = post( Batch.PATH, lBatchBuilder.build() );

      // THEN the location api can be queried for the newly created location
      Batch lBatch = lResponse.as( Batch.class );
      String lLocationId =
            lBatch.getApiCalls().get( 0 ).getResponse().getBody( Location.class ).getId();

      lResponse = get( Location.PATH + "/" + lLocationId );

      Assert.assertEquals( lNewLocation.getCode(), lResponse.as( Location.class ).getCode() );
   }


   @Test
   public void batchWithMultipleDependantApiCalls() throws JsonProcessingException {

      // GIVEN batch with multiple api calls to create a location hierarchy (airport->hanger->line)
      String lLocationCodeSuffix = String.valueOf( System.currentTimeMillis() );
      Location lAirport = new Location.Builder().code( "TA-" + lLocationCodeSuffix )
            .type( TYPE_AIRPORT ).name( NAME_AIRPORT ).isSupply( false ).stateCode( STATE_CODE )
            .countryCode( COUNTRY_CODE ).build();

      Location lHanger = new Location.Builder().code( "TH-" + lLocationCodeSuffix )
            .type( TYPE_HANGAR ).name( NAME_HANGAR ).isSupply( false ).parentId( "{result=0:$.id}" )
            .stateCode( STATE_CODE ).countryCode( COUNTRY_CODE ).build();

      Location lLine = new Location.Builder().code( "TL-" + lLocationCodeSuffix ).type( TYPE_LINE )
            .name( NAME_LINE ).isSupply( false ).parentId( "{result=1:$.id}" )
            .stateCode( STATE_CODE ).countryCode( COUNTRY_CODE ).build();

      BatchBuilder lBatchBuilder = new BatchBuilder();
      lBatchBuilder.request( Location.PATH ).post().body( lAirport ).acceptJson().contentJson()
            .add();
      lBatchBuilder.request( Location.PATH ).post().body( lHanger ).acceptJson().contentJson()
            .add();
      lBatchBuilder.request( Location.PATH ).post().body( lLine ).acceptJson().contentJson().add();

      // WHEN the batch api is called
      Response lResponse = post( Batch.PATH, lBatchBuilder.build() );

      // THEN the location api can be queried for the newly created locations
      Batch lBatch = lResponse.as( Batch.class );
      String lAirportLocationId =
            lBatch.getApiCalls().get( 0 ).getResponse().getBody( Location.class ).getId();

      lResponse = get( buildPath( Location.PATH, lAirportLocationId ) );

      Assert.assertEquals( lAirport.getCode(), lResponse.as( Location.class ).getCode() );

      String lHangerLocationId =
            lBatch.getApiCalls().get( 1 ).getResponse().getBody( Location.class ).getId();

      lResponse = get( buildPath( Location.PATH, lHangerLocationId ) );

      Assert.assertEquals( lHanger.getCode(), lResponse.as( Location.class ).getCode() );

      String lLineLocationId =
            lBatch.getApiCalls().get( 1 ).getResponse().getBody( Location.class ).getId();

      lResponse = get( buildPath( Location.PATH, lLineLocationId ) );

      Assert.assertEquals( lHanger.getCode(), lResponse.as( Location.class ).getCode() );
   }


   @Test
   public void batchWithMultipleDependantApiCallsRollsBackOnFailure()
         throws JsonProcessingException {

      // GIVEN batch with multiple api calls to create a location hierarchy (airport->hanger->line)
      String lLocationCodeSuffix = String.valueOf( System.currentTimeMillis() );

      // Country Code and State Code ommited to trigger
      Location lAirport = new Location.Builder().code( "TA-" + lLocationCodeSuffix )
            .type( TYPE_AIRPORT ).name( NAME_AIRPORT ).isSupply( false ).build();

      Location lHanger = new Location.Builder().code( "TH-" + lLocationCodeSuffix )
            .type( TYPE_HANGAR ).name( NAME_HANGAR ).isSupply( false ).parentId( "{result=0:$.id}" )
            .stateCode( STATE_CODE ).countryCode( COUNTRY_CODE ).build();

      Location lLine = new Location.Builder().code( "TL-" + lLocationCodeSuffix ).type( TYPE_LINE )
            .name( NAME_LINE ).isSupply( false ).parentId( "{result=1:$.id}" )
            .stateCode( STATE_CODE ).countryCode( COUNTRY_CODE ).build();

      BatchBuilder lBatchBuilder = new BatchBuilder();
      lBatchBuilder.request( Location.PATH ).post().body( lAirport ).acceptJson().contentJson()
            .add();
      lBatchBuilder.request( Location.PATH ).post().body( lHanger ).acceptJson().contentJson()
            .add();
      lBatchBuilder.request( Location.PATH ).post().body( lLine ).acceptJson().contentJson().add();

      // WHEN the batch api is called
      Response lResponse = post( Batch.PATH, lBatchBuilder.build(), MULTI_STATUS );

      // THEN the location api will return no results for the locations that were attempted to be
      // created in the batch
      MultivaluedHashMap<String, String> lQueryParams = new MultivaluedHashMap<String, String>();
      lQueryParams.put( LocationSearchParameters.PARAM_CODE,
            Arrays.asList( lAirport.getCode(), lHanger.getCode(), lLine.getCode() ) );

      lResponse = get( Location.PATH, lQueryParams );

      Location[] lSearchResults = lResponse.as( Location[].class );

      Assert.assertEquals( 0, lSearchResults.length );

   }


   @Test
   public void batchWithMultipleUrlDependantApiCalls() throws JsonProcessingException {

      // GIVEN a location hierarchy
      String lLocationCodeSuffix = String.valueOf( System.currentTimeMillis() );
      Location lAirport = new Location.Builder().code( "TA-" + lLocationCodeSuffix )
            .type( TYPE_AIRPORT ).name( NAME_AIRPORT ).isSupply( false ).stateCode( STATE_CODE )
            .countryCode( COUNTRY_CODE ).build();

      Location lHanger = new Location.Builder().code( "TH-" + lLocationCodeSuffix )
            .type( TYPE_HANGAR ).name( NAME_HANGAR ).isSupply( false ).parentId( "{result=0:$.id}" )
            .stateCode( STATE_CODE ).countryCode( COUNTRY_CODE ).build();

      Location lLine = new Location.Builder().code( "TL-" + lLocationCodeSuffix ).type( TYPE_LINE )
            .name( NAME_LINE ).isSupply( false ).parentId( "{result=1:$.id}" )
            .stateCode( STATE_CODE ).countryCode( COUNTRY_CODE ).build();

      BatchBuilder lBatchBuilder = new BatchBuilder();
      lBatchBuilder.request( Location.PATH ).post().body( lAirport ).acceptJson().contentJson()
            .add();
      lBatchBuilder.request( Location.PATH ).post().body( lHanger ).acceptJson().contentJson()
            .add();
      lBatchBuilder.request( Location.PATH ).post().body( lLine ).acceptJson().contentJson().add();

      Response lResponse = post( Batch.PATH, lBatchBuilder.build() );

      // WHEN a batch is submitted to return the location hierarchy using path transformations
      lBatchBuilder = new BatchBuilder();
      lBatchBuilder.request( Location.PATH + "?code=" + lAirport.getCode() ).get().acceptJson()
            .add();
      lBatchBuilder.request( Location.PATH + "?code=" + lHanger.getCode() ).get().acceptJson()
            .add();
      lBatchBuilder.request( Location.PATH + "?code=" + lLine.getCode() ).get().acceptJson().add();

      lResponse = post( Batch.PATH, lBatchBuilder.build(), Status.OK.getStatusCode() );

      Batch lBatch = lResponse.as( Batch.class );
      ObjectMapper m = new ObjectMapper();

      Location locAir = m.convertValue(
            lBatch.getApiCalls().get( 0 ).getResponse().getBody().get( 0 ), Location.class );
      Location locHang = m.convertValue(
            lBatch.getApiCalls().get( 1 ).getResponse().getBody().get( 0 ), Location.class );
      Location locLine = m.convertValue(
            lBatch.getApiCalls().get( 2 ).getResponse().getBody().get( 0 ), Location.class );

      // THEN the location hierarchy is returned as expected
      Assert.assertEquals( lAirport.getCode(), locAir.getCode() );
      Assert.assertEquals( lHanger.getCode(), locHang.getCode() );
      Assert.assertEquals( lLine.getCode(), locLine.getCode() );
   }


   private Response post( String aEndpoint, Object aEntity ) {
      return post( aEndpoint, aEntity, Status.OK.getStatusCode() );
   }


   private Response post( String aEndpoint, Object aEntity, int aExpectedStatus ) {

      Response lResponse = RestAssured.given().auth().preemptive()
            .basic( Credentials.AUTHENTICATED.getUserName(),
                  Credentials.AUTHENTICATED.getPassword() )
            .accept( MediaType.APPLICATION_JSON ).contentType( MediaType.APPLICATION_JSON )
            .body( aEntity ).expect().statusCode( aExpectedStatus ).when().post( aEndpoint );
      return lResponse;

   }


   private Response get( String aEndpoint ) {
      return get( aEndpoint, new MultivaluedHashMap<String, String>(), Status.OK.getStatusCode() );
   }


   private Response get( String aEndpoint, MultivaluedMap<String, String> aQueryParams ) {
      return get( aEndpoint, aQueryParams, Status.OK.getStatusCode() );
   }


   private Response get( String aEndpoint, MultivaluedMap<String, String> aQueryParams,
         int aExpectedStatus ) {

      RequestSpecification lRequestSpec = RestAssured.given().auth().preemptive()
            .basic( Credentials.AUTHENTICATED.getUserName(),
                  Credentials.AUTHENTICATED.getPassword() )
            .accept( MediaType.APPLICATION_JSON ).contentType( MediaType.APPLICATION_JSON );

      for ( String lKey : aQueryParams.keySet() ) {
         lRequestSpec.param( lKey, aQueryParams.get( lKey ) );
      }

      return lRequestSpec.expect().statusCode( aExpectedStatus ).when().get( aEndpoint );

   }


   private String buildPath( String... aSegments ) {
      Joiner lJoiner = Joiner.on( "/" );
      return lJoiner.join( aSegments );
   }
}
