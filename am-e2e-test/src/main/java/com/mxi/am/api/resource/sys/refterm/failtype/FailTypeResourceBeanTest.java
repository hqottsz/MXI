package com.mxi.am.api.resource.sys.refterm.failtype;

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
 * e2e test for FailType Resource Bean
 *
 */
public class FailTypeResourceBeanTest {

   public static final String FAIL_TYPE_CODE = "A";
   public static final String FAIL_TYPE_NAME = "Bent, Buckled, Distorted, Twisted";
   public static final String FAIL_TYPE_DESCRIPTION = "Bent, Buckled, Distorted, Twisted";

   public static final String FAIL_TYPE_CODE2 = "B";
   public static final String FAIL_TYPE_NAME2 = "Binding, Jammed, Seized, Stiff, Dragging";
   public static final String FAIL_TYPE_DESCRIPTION2 = "Binding, Jammed, Seized, Stiff, Dragging";

   public static final String FAIL_TYPE_CODE3 = "C";
   public static final String FAIL_TYPE_NAME3 =
         "Broken, Burst, Ruptured, Torn, Open Circuit, Sheared";
   public static final String FAIL_TYPE_DESCRIPTION3 =
         "Broken, Burst, Ruptured, Torn, Open Circuit, Sheared";

   private static final String APPLICATION_JSON = "application/json";
   private static final String FAIL_TYPE_API_PATH = "/amapi/" + FailType.PATH;

   private static final int FAIL_TYPE_RECORD_COUNT = 21;
   private static final String FAIL_TYPE_CODE_DOUBLE = "C";


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
   public void testGetFailTypeByCodeSuccess200()
         throws JsonProcessingException, IOException, ParseException {
      Response response = getFailTypeByCode( 200, Credentials.AUTHENTICATED, FAIL_TYPE_CODE );
      assertFailTypeForGet( defaultFailTypeBuilder(), response );
   }


   @Test
   @CSIContractTest( { Project.AFKLM_IMECH } )
   public void testSearchAllFailTypesSuccess200()
         throws JsonParseException, JsonMappingException, IOException {
      Response response = searchAllFailTypes( 200, Credentials.AUTHENTICATED );
      assertFailTypeForSearch( defaultFailTypeListBuilder(), response );
   }


   @Test
   public void testGetFailTypeByCodeUnauthenticatedFailure401() {
      getFailTypeByCode( 401, Credentials.UNAUTHENTICATED, FAIL_TYPE_CODE );
   }


   @Test
   public void testSearchAllFailTypesUnauthenticatedFailure401() {
      searchAllFailTypes( 401, Credentials.UNAUTHENTICATED );
   }


   @Test
   public void testGetFailTypeByCodeUnauthorizedFailure403() {
      getFailTypeByCode( 403, Credentials.UNAUTHORIZED, FAIL_TYPE_CODE );
   }


   @Test
   public void testSearchAllFailTypesUnauthorizedFailure403() {
      searchAllFailTypes( 403, Credentials.UNAUTHORIZED );
   }


   @Test
   public void testGetFailTypeByCodeReturnsMultipleResultsFailure500()
         throws AmApiResourceNotFoundException {
      getFailTypeByCode( 500, Credentials.AUTHENTICATED, FAIL_TYPE_CODE_DOUBLE );
   }


   private Response getFailTypeByCode( int statusCode, Credentials security, String failTypeCode ) {
      String userName = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().pathParam( "code", failTypeCode ).auth().preemptive()
            .basic( userName, password ).accept( APPLICATION_JSON ).contentType( APPLICATION_JSON )
            .expect().statusCode( statusCode ).when().get( FAIL_TYPE_API_PATH + "/{code}" );
      return response;
   }


   private Response searchAllFailTypes( int statusCode, Credentials security ) {
      String username = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().auth().preemptive().basic( username, password )
            .accept( APPLICATION_JSON ).contentType( APPLICATION_JSON ).expect()
            .statusCode( statusCode ).when().get( FAIL_TYPE_API_PATH );
      return response;
   }


   private FailType defaultFailTypeBuilder() {
      FailType failType = new FailType();
      failType.setCode( FAIL_TYPE_CODE );
      failType.setName( FAIL_TYPE_NAME );
      failType.setDescription( FAIL_TYPE_DESCRIPTION );
      return failType;
   }


   private List<FailType> defaultFailTypeListBuilder() {

      List<FailType> failTypeList = new ArrayList<FailType>();

      FailType failType1 = new FailType();
      failType1.setCode( FAIL_TYPE_CODE );
      failType1.setName( FAIL_TYPE_NAME );
      failType1.setDescription( FAIL_TYPE_DESCRIPTION );

      FailType failType2 = new FailType();
      failType2.setCode( FAIL_TYPE_CODE2 );
      failType2.setName( FAIL_TYPE_NAME2 );
      failType2.setDescription( FAIL_TYPE_DESCRIPTION2 );

      FailType failType3 = new FailType();
      failType3.setCode( FAIL_TYPE_CODE3 );
      failType3.setName( FAIL_TYPE_NAME3 );
      failType3.setDescription( FAIL_TYPE_DESCRIPTION3 );

      failTypeList.add( failType1 );
      failTypeList.add( failType2 );
      failTypeList.add( failType3 );

      return failTypeList;
   }


   private void assertFailTypeForGet( FailType failTypeExpected, Response failTypeActual )
         throws JsonProcessingException, IOException {
      ObjectMapper objectMapper =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      FailType actualFailType = objectMapper.readValue( failTypeActual.getBody().asString(),
            objectMapper.getTypeFactory().constructType( FailType.class ) );
      Assert.assertEquals( failTypeExpected, actualFailType );
   }


   private void assertFailTypeForSearch( List<FailType> failTypeListExpected,
         Response failTypeListActual ) throws JsonProcessingException, IOException {
      ObjectMapper objectMapper =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      List<FailType> actualFailTypeList = objectMapper.readValue(
            failTypeListActual.getBody().asString(),
            objectMapper.getTypeFactory().constructCollectionType( List.class, FailType.class ) );
      assertEquals( FAIL_TYPE_RECORD_COUNT, actualFailTypeList.size() );
      Assert.assertTrue( actualFailTypeList.containsAll( failTypeListExpected ) );
   }

}
