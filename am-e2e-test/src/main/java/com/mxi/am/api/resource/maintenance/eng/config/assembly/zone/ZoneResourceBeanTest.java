package com.mxi.am.api.resource.maintenance.eng.config.assembly.zone;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mxi.am.api.helper.Credentials;
import com.mxi.am.api.resource.ObjectMapperFactory;
import com.mxi.am.driver.common.database.DatabaseDriver;
import com.mxi.am.driver.common.database.Result;
import com.mxi.am.guice.AssetManagementDatabaseDriverProvider;

import io.restassured.RestAssured;
import io.restassured.response.Response;


/**
 * Zone API test
 *
 */
public class ZoneResourceBeanTest {

   private final String iApplicationJson = "application/json";
   private final String iZonePath = "/amapi/" + Zone.PATH;

   private String iZoneId;
   private String iZoneCode = "ZONE1";
   private String iAssemblyId;
   private String iAssemblyCode = "ACFT_CD1";
   private String iZoneDescription = "Aircraft Zone 1";

   private DatabaseDriver iDriver;


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
      iDriver = new AssetManagementDatabaseDriverProvider().get();

      Result lZoneResult =
            iDriver.select( "select alt_id from eqp_task_zone where Zone_cd=? and assmbl_cd=?",
                  iZoneCode, iAssemblyCode );

      Result lAssemblyResult =
            iDriver.select( "select alt_id from eqp_assmbl where assmbl_cd=?", iAssemblyCode );

      if ( lZoneResult.isEmpty() ) {
         fail( "Could not find the Zone with code: " + iZoneCode + " and Assembly with code : "
               + iAssemblyCode );
      }

      if ( lAssemblyResult.isEmpty() ) {
         fail( "Could not find the Assembly with code: " + iAssemblyCode );
      }

      iZoneId = lZoneResult.get( 0 ).getUuidString( "alt_id" );
      iAssemblyId = lAssemblyResult.get( 0 ).getUuidString( "alt_id" );
   }


   @Test
   public void testSearchZoneSuccessReturns200() throws JsonProcessingException, IOException {

      Response lResponse = createSearch( 200, Credentials.AUTHENTICATED, iAssemblyId, iZoneCode,
            iApplicationJson, iApplicationJson );
      assertZoneForSearch( defaultZoneBuilder(), lResponse );
   }


   @Test
   public void testSearchZoneUnauthenticatedReturns401()
         throws JsonProcessingException, IOException {

      createSearch( 401, Credentials.UNAUTHENTICATED, iAssemblyId, iZoneCode, iApplicationJson,
            iApplicationJson );
   }


   @Test
   public void testSearchZoneUnauthorizedReturns403() throws JsonProcessingException, IOException {

      createSearch( 403, Credentials.UNAUTHORIZED, iAssemblyId, iZoneCode, iApplicationJson,
            iApplicationJson );
   }


   @Test
   public void testGetByIdZoneSuccessReturns200() throws JsonProcessingException, IOException {

      Response lResponse = createGetById( 200, Credentials.AUTHENTICATED, iZoneId, iApplicationJson,
            iApplicationJson );
      assertZoneForGet( defaultZoneBuilder(), lResponse );
   }


   @Test
   public void testGetByIdZoneUnauthenticatedReturns401()
         throws JsonProcessingException, IOException {

      createGetById( 401, Credentials.UNAUTHENTICATED, iZoneId, iApplicationJson,
            iApplicationJson );
   }


   @Test
   public void testGetByIdZoneUnauthorizedReturns403() throws JsonProcessingException, IOException {

      createGetById( 403, Credentials.UNAUTHORIZED, iZoneId, iApplicationJson, iApplicationJson );
   }


   private Response createGetById( int aStatusCode, Credentials aSecurity, String aZoneUUId,
         String aContentType, String aAcceptType ) {
      String lUserName = aSecurity.getUserName();
      String lPassword = aSecurity.getPassword();

      Response lResponse =
            RestAssured.given().pathParam( "zoneUUId", aZoneUUId ).auth().preemptive()
                  .basic( lUserName, lPassword ).accept( aAcceptType ).contentType( aContentType )
                  .expect().statusCode( aStatusCode ).when().get( iZonePath + "/{zoneUUId}" );
      return lResponse;
   }


   private Response createSearch( int aStatusCode, Credentials aSecurity, String aAssemblyId,
         String aZoneCode, String aContentType, String aAcceptType ) {
      String lUserName = aSecurity.getUserName();
      String lPassword = aSecurity.getPassword();

      Response lResponse =
            RestAssured.given().queryParam( ZoneResource.ASSEMBLY_ID_PARM, aAssemblyId )
                  .queryParam( ZoneResource.ZONE_CODE_PARM, aZoneCode ).auth().preemptive()
                  .basic( lUserName, lPassword ).accept( aAcceptType ).contentType( aContentType )
                  .expect().statusCode( aStatusCode ).when().get( iZonePath );
      return lResponse;
   }


   private void assertZoneForSearch( Zone aZoneExpected, Response aActual )
         throws JsonProcessingException, IOException {
      ObjectMapper lObjectMapper =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      List<Zone> lZoneActual = lObjectMapper.readValue( aActual.getBody().asString(),
            lObjectMapper.getTypeFactory().constructCollectionType( List.class, Zone.class ) );
      Assert.assertEquals( lZoneActual.size(), 1 );
      Assert.assertEquals( aZoneExpected, lZoneActual.get( 0 ) );

   }


   private void assertZoneForGet( Zone aZoneExpected, Response aActual )
         throws JsonProcessingException, IOException {
      ObjectMapper lObjectMapper =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      Zone lZoneActual = lObjectMapper.readValue( aActual.getBody().asString(),
            lObjectMapper.getTypeFactory().constructType( Zone.class ) );
      Assert.assertEquals( aZoneExpected, lZoneActual );

   }


   private Zone defaultZoneBuilder() {
      Zone lZone = new Zone();
      lZone.setAssemblyId( iAssemblyId );
      lZone.setAssemblyCode( iAssemblyCode );
      lZone.setId( iZoneId );
      lZone.setZoneCode( iZoneCode );
      lZone.setZoneDescription( iZoneDescription );
      return lZone;
   }

}
