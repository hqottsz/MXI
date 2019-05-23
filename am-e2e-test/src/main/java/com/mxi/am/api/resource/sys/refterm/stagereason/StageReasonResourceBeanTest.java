package com.mxi.am.api.resource.sys.refterm.stagereason;

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
import com.mxi.am.api.annotation.CSIContractTest;
import com.mxi.am.api.annotation.CSIContractTest.Project;
import com.mxi.am.api.helper.Credentials;
import com.mxi.am.api.resource.ObjectMapperFactory;

import io.restassured.RestAssured;
import io.restassured.response.Response;


/**
 * StageReason API test
 *
 */
public class StageReasonResourceBeanTest {

   private final String applicationJson = "application/json";
   private final String stageReasonPath = "/amapi/" + StageReason.PATH;

   private static final String STAGE_REASON_CODE = "OBSOLETE";
   private static final String STAGE_REASON_CODE_NOT_FOUND = "TESTNOTFOUND";
   private static final String STAGE_REASON_DESCRIPTION =
         "System generated when task def is selected to obsolete";
   private static final String STAGE_REASON_EVENT_CODE = "CANCEL";
   private static final String STAGE_REASON_NAME = "Obsolete";
   private static final String STAGE_REASON_USER_CODE = "OBSOLETE";
   private static final int STAGE_REASONS_LIST_SIZE = 135;

   private static ObjectMapper objectMapper;


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
   @CSIContractTest( Project.UPS )
   public void testGetStageReasonSuccess()
         throws JsonParseException, JsonMappingException, IOException {
      Response response = getStageReason( 200, Credentials.AUTHENTICATED, applicationJson,
            applicationJson, STAGE_REASON_CODE );
      assertStageReason( response );
   }


   @Test
   public void testGetStageReasonNotFound()
         throws JsonParseException, JsonMappingException, IOException {
      getStageReason( 404, Credentials.AUTHENTICATED, applicationJson, applicationJson,
            STAGE_REASON_CODE_NOT_FOUND );
   }


   @Test
   public void testGetStageReasonUnauthorized()
         throws JsonParseException, JsonMappingException, IOException {
      getStageReason( 403, Credentials.UNAUTHORIZED, applicationJson, applicationJson,
            STAGE_REASON_CODE_NOT_FOUND );
   }


   @Test
   public void testGetStageReasonUnauthenticated()
         throws JsonParseException, JsonMappingException, IOException {
      getStageReason( 401, Credentials.UNAUTHENTICATED, applicationJson, applicationJson,
            STAGE_REASON_CODE_NOT_FOUND );
   }


   @Test
   public void testSearchStageReasonSuccess()
         throws JsonParseException, JsonMappingException, IOException {
      Response response =
            searchStageReason( 200, Credentials.AUTHENTICATED, applicationJson, applicationJson );
      List<StageReason> stageReasons =
            objectMapper.readValue( response.getBody().asString(), objectMapper.getTypeFactory()
                  .constructCollectionType( List.class, StageReason.class ) );
      Assert.assertEquals( "Incorrect number of stage reasons returned: ", STAGE_REASONS_LIST_SIZE,
            stageReasons.size() );
   }


   @Test
   public void testSearchStageReasonUnauthorized()
         throws JsonParseException, JsonMappingException, IOException {
      searchStageReason( 403, Credentials.UNAUTHORIZED, applicationJson, applicationJson );
   }


   @Test
   public void testSearchAllStageReasonUnauthenticated() {
      searchStageReason( 401, Credentials.UNAUTHENTICATED, applicationJson, applicationJson );
   }


   private Response getStageReason( int statusCode, Credentials security, String contentType,
         String acceptType, String stageReasonCode ) {
      String userName = security.getUserName();
      String password = security.getPassword();

      return RestAssured.given().pathParam( "stageReasonCode", stageReasonCode ).auth().preemptive()
            .basic( userName, password ).accept( acceptType ).contentType( contentType ).expect()
            .statusCode( statusCode ).when().get( stageReasonPath + "/{stageReasonCode}" );
   }


   private Response searchStageReason( int statusCode, Credentials security, String contentType,
         String acceptType ) {
      String userName = security.getUserName();
      String password = security.getPassword();

      return RestAssured.given().auth().preemptive().basic( userName, password )
            .accept( acceptType ).contentType( contentType ).expect().statusCode( statusCode )
            .when().get( stageReasonPath );
   }


   private void assertStageReason( Response response )
         throws JsonParseException, JsonMappingException, IOException {
      StageReason stageReason = objectMapper.readValue( response.getBody().asString(),
            objectMapper.getTypeFactory().constructType( StageReason.class ) );
      Assert.assertEquals( "Incorrect Code: ", STAGE_REASON_CODE, stageReason.getCode() );
      Assert.assertEquals( "Incorrect Description: ", STAGE_REASON_DESCRIPTION,
            stageReason.getDescription() );
      Assert.assertEquals( "Incorrect Event Status Code: ", STAGE_REASON_EVENT_CODE,
            stageReason.getStatusCode() );
      Assert.assertEquals( "Incorrect Name: ", STAGE_REASON_NAME, stageReason.getName() );
      Assert.assertEquals( "Incorrect User Code: ", STAGE_REASON_USER_CODE,
            stageReason.getUserCode() );
   }

}
