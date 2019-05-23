package com.mxi.am.api.resource.sys.refterm.ietmtype;

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
 * e2e test for IetmType Resource Bean
 *
 */
public class IetmTypeResourceBeanTest {

   public static final String IETM_TYPE_CODE = "PDF";
   public static final String IETM_TYPE_NAME = "PDF";
   public static final String IETM_TYPE_DESCRIPTION = "pdf";

   public static final String IETM_TYPE_CODE2 = "AAA";
   public static final String IETM_TYPE_NAME2 = "AAA";
   public static final String IETM_TYPE_DESCRIPTION2 = "aaa";

   private static final String APPLICATION_JSON = "application/json";
   private static final String IETM_TYPE_API_PATH = "/amapi/" + IetmType.PATH;

   private static final int IETM_TYPE_RECORD_COUNT = 3;
   private static final String IETM_TYPE_CODE_DOUBLE = "AAA";


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
   public void testGetIetmTypeByCodeSuccess200()
         throws JsonProcessingException, IOException, ParseException {
      Response response = getIetmTypeByCode( 200, Credentials.AUTHENTICATED, IETM_TYPE_CODE );
      assertIetmTypeForGet( defaultIetmTypeBuilder(), response );
   }


   @Test
   @CSIContractTest( { Project.AFKLM_IMECH } )
   public void testSearchAllIetmTypesSuccess200()
         throws JsonParseException, JsonMappingException, IOException {
      Response response = searchAllIetmTypes( 200, Credentials.AUTHENTICATED );
      assertIetmTypeForSearch( defaultIetmTypeListBuilder(), response );
   }


   @Test
   public void testGetIetmByCodeUnauthorizedFailure403() {
      getIetmTypeByCode( 403, Credentials.UNAUTHORIZED, IETM_TYPE_CODE );
   }


   @Test
   public void testSearchAllIetmTypesUnauthorizedFailure403() {
      searchAllIetmTypes( 403, Credentials.UNAUTHORIZED );
   }


   @Test
   public void testGetIetmTypeByCodeReturnsMultipleResultsFailure500()
         throws AmApiResourceNotFoundException {
      getIetmTypeByCode( 500, Credentials.AUTHENTICATED, IETM_TYPE_CODE_DOUBLE );
   }


   private Response getIetmTypeByCode( int statusCode, Credentials security, String ietmTypeCode ) {
      String userName = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().pathParam( "code", ietmTypeCode ).auth().preemptive()
            .basic( userName, password ).accept( APPLICATION_JSON ).contentType( APPLICATION_JSON )
            .expect().statusCode( statusCode ).when().get( IETM_TYPE_API_PATH + "/{code}" );
      return response;
   }


   private Response searchAllIetmTypes( int statusCode, Credentials security ) {
      String username = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().auth().preemptive().basic( username, password )
            .accept( APPLICATION_JSON ).contentType( APPLICATION_JSON ).expect()
            .statusCode( statusCode ).when().get( IETM_TYPE_API_PATH );
      return response;
   }


   private IetmType defaultIetmTypeBuilder() {
      IetmType ietmType = new IetmType();
      ietmType.setCode( IETM_TYPE_CODE );
      ietmType.setName( IETM_TYPE_NAME );
      ietmType.setDescription( IETM_TYPE_DESCRIPTION );
      return ietmType;
   }


   private List<IetmType> defaultIetmTypeListBuilder() {

      List<IetmType> ietmTypeList = new ArrayList<IetmType>();

      IetmType ietmType1 = new IetmType();
      ietmType1.setCode( IETM_TYPE_CODE );
      ietmType1.setName( IETM_TYPE_NAME );
      ietmType1.setDescription( IETM_TYPE_DESCRIPTION );

      IetmType ietmType2 = new IetmType();
      ietmType2.setCode( IETM_TYPE_CODE2 );
      ietmType2.setName( IETM_TYPE_NAME2 );
      ietmType2.setDescription( IETM_TYPE_DESCRIPTION2 );

      ietmTypeList.add( ietmType1 );
      ietmTypeList.add( ietmType2 );

      return ietmTypeList;
   }


   private void assertIetmTypeForGet( IetmType ietmTypeExpected, Response ietmTypeActual )
         throws JsonProcessingException, IOException {
      ObjectMapper objectMapper =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      IetmType actualIetmType = objectMapper.readValue( ietmTypeActual.getBody().asString(),
            objectMapper.getTypeFactory().constructType( IetmType.class ) );
      Assert.assertEquals( "Incorrect returned Ietm Type : ", ietmTypeExpected, actualIetmType );
   }


   private void assertIetmTypeForSearch( List<IetmType> ietmTypeListExpected,
         Response ietmTypeListActual ) throws JsonProcessingException, IOException {
      ObjectMapper objectMapper =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      List<IetmType> actualIetmTypeList = objectMapper.readValue(
            ietmTypeListActual.getBody().asString(),
            objectMapper.getTypeFactory().constructCollectionType( List.class, IetmType.class ) );
      assertEquals( "Incorrect returned Ietm Type count, expected " + IETM_TYPE_RECORD_COUNT,
            IETM_TYPE_RECORD_COUNT, actualIetmTypeList.size() );
      Assert.assertTrue( "Incorrect returned Ietm Type list",
            actualIetmTypeList.containsAll( ietmTypeListExpected ) );
   }
}
