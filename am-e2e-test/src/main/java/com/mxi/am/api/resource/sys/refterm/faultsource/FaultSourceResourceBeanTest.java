package com.mxi.am.api.resource.sys.refterm.faultsource;

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
 * e2e test for FaultSource Resource Bean
 *
 */
public class FaultSourceResourceBeanTest {

   public static final String FAULT_SOURCE_CODE = "PILOT";
   public static final String FAULT_SOURCE_NAME = "Pilot ";
   public static final String FAULT_SOURCE_DESCRIPTION = "Pilot ";
   public static final String SPEC2K_FAULT_SOURCE_CODE = "PL";

   public static final String FAULT_SOURCE_CODE2 = "MECH";
   public static final String FAULT_SOURCE_NAME2 = "Mechanic";
   public static final String FAULT_SOURCE_DESCRIPTION2 = "Mechanic";
   public static final String SPEC2K_FAULT_SOURCE_CODE2 = "ML";

   public static final String FAULT_SOURCE_CODE3 = "CABIN";
   public static final String FAULT_SOURCE_NAME3 = "Cabin Crew";
   public static final String FAULT_SOURCE_DESCRIPTION3 = "Cabin Crew";
   public static final String SPEC2K_FAULT_SOURCE_CODE3 = "CL";

   private static final String APPLICATION_JSON = "application/json";
   private static final String FAULT_SOURCE_API_PATH = "/amapi/" + FaultSource.PATH;

   private static final int FAULT_SOURCE_RECORD_COUNT = 7;
   private static final String FAULT_SOURCE_CODE_DOUBLE = "MECH";


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
   public void testGetFaultSourceByCodeSuccess200()
         throws JsonProcessingException, IOException, ParseException {
      Response response = getFaultSourceByCode( 200, Credentials.AUTHENTICATED, FAULT_SOURCE_CODE );
      assertFaultSourceForGet( defaultFaultSourceBuilder(), response );
   }


   @Test
   @CSIContractTest( { Project.AFKLM_IMECH } )
   public void testSearchAllFaultSourcesSuccess200()
         throws JsonParseException, JsonMappingException, IOException {
      Response response = searchAllFaultSources( 200, Credentials.AUTHENTICATED );
      assertFaultSourceForSearch( defaultFaultSourceListBuilder(), response );
   }


   @Test
   public void testGetFaultSourceByCodeUnauthorizedFailure403() {
      getFaultSourceByCode( 403, Credentials.UNAUTHORIZED, FAULT_SOURCE_CODE );
   }


   @Test
   public void testSearchAllFaultSourcesUnauthorizedFailure403() {
      searchAllFaultSources( 403, Credentials.UNAUTHORIZED );
   }


   @Test
   public void testGetFaultSourceByCodeReturnsMultipleResultsFailure500()
         throws AmApiResourceNotFoundException {
      getFaultSourceByCode( 500, Credentials.AUTHENTICATED, FAULT_SOURCE_CODE_DOUBLE );
   }


   private Response getFaultSourceByCode( int statusCode, Credentials security,
         String faultSourceCode ) {
      String userName = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().pathParam( "code", faultSourceCode ).auth()
            .preemptive().basic( userName, password ).accept( APPLICATION_JSON )
            .contentType( APPLICATION_JSON ).expect().statusCode( statusCode ).when()
            .get( FAULT_SOURCE_API_PATH + "/{code}" );
      return response;
   }


   private Response searchAllFaultSources( int statusCode, Credentials security ) {
      String username = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().auth().preemptive().basic( username, password )
            .accept( APPLICATION_JSON ).contentType( APPLICATION_JSON ).expect()
            .statusCode( statusCode ).when().get( FAULT_SOURCE_API_PATH );
      return response;
   }


   private FaultSource defaultFaultSourceBuilder() {
      FaultSource faultSource = new FaultSource();
      faultSource.setCode( FAULT_SOURCE_CODE );
      faultSource.setName( FAULT_SOURCE_NAME );
      faultSource.setDescription( FAULT_SOURCE_DESCRIPTION );
      faultSource.setSpec2kCode( SPEC2K_FAULT_SOURCE_CODE );
      return faultSource;
   }


   private List<FaultSource> defaultFaultSourceListBuilder() {

      List<FaultSource> faultSourceList = new ArrayList<FaultSource>();

      FaultSource faultSource1 = new FaultSource();
      faultSource1.setCode( FAULT_SOURCE_CODE );
      faultSource1.setName( FAULT_SOURCE_NAME );
      faultSource1.setDescription( FAULT_SOURCE_DESCRIPTION );
      faultSource1.setSpec2kCode( SPEC2K_FAULT_SOURCE_CODE );

      FaultSource faultSource2 = new FaultSource();
      faultSource2.setCode( FAULT_SOURCE_CODE2 );
      faultSource2.setName( FAULT_SOURCE_NAME2 );
      faultSource2.setDescription( FAULT_SOURCE_DESCRIPTION2 );
      faultSource2.setSpec2kCode( SPEC2K_FAULT_SOURCE_CODE2 );

      FaultSource faultSource3 = new FaultSource();
      faultSource3.setCode( FAULT_SOURCE_CODE3 );
      faultSource3.setName( FAULT_SOURCE_NAME3 );
      faultSource3.setDescription( FAULT_SOURCE_DESCRIPTION3 );
      faultSource3.setSpec2kCode( SPEC2K_FAULT_SOURCE_CODE3 );

      faultSourceList.add( faultSource1 );
      faultSourceList.add( faultSource2 );
      faultSourceList.add( faultSource3 );

      return faultSourceList;
   }


   private void assertFaultSourceForGet( FaultSource faultSourceExpected,
         Response faultSourceActual ) throws JsonProcessingException, IOException {
      ObjectMapper objectMapper =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      FaultSource actualFaultSource =
            objectMapper.readValue( faultSourceActual.getBody().asString(),
                  objectMapper.getTypeFactory().constructType( FaultSource.class ) );
      Assert.assertEquals( "Incorrect returned Fault Source : ", faultSourceExpected,
            actualFaultSource );
   }


   private void assertFaultSourceForSearch( List<FaultSource> faultSourceListExpected,
         Response faultSourceListActual ) throws JsonProcessingException, IOException {
      ObjectMapper objectMapper =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      List<FaultSource> actualFaultSourceList =
            objectMapper.readValue( faultSourceListActual.getBody().asString(), objectMapper
                  .getTypeFactory().constructCollectionType( List.class, FaultSource.class ) );
      assertEquals( "Incorrect returned Fault Source count, expected " + FAULT_SOURCE_RECORD_COUNT,
            FAULT_SOURCE_RECORD_COUNT, actualFaultSourceList.size() );
      Assert.assertTrue( "Incorrect returned Fault Source list",
            actualFaultSourceList.containsAll( faultSourceListExpected ) );
   }

}
