package com.mxi.am.api.resource.sys.refterm.tasksubclass;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

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
 * TaskSubclass API - e2e Test
 *
 */
public class TaskSubclassResourceBeanTest {

   private static final String TASK_CLASS_CODE = "MPC";
   private static final String CODE1 = "MPCOPEN";
   private static final String DESCRIPTION1 = "Open Master Panel Card";
   private static final String NAME1 = "Open Master Panel Card";
   private static final String USER_CODE1 = "OPEN";

   private static final String CODE2 = "MPCCLOSE";
   private static final String DESCRIPTION2 = "Close Master Panel Card";
   private static final String NAME2 = "Close Master Panel Card";
   private static final String USER_CODE2 = "CLOSE";
   private static final int TASK_SUBCLASS_RECORD_COUNT = 2;

   private static final String APPLICATION_JSON = "application/json";
   private static final String TASK_SUBCLASS_API_PATH = "/amapi/" + TaskSubclass.PATH;


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


   @Test
   @CSIContractTest( { Project.AFKLM_IMECH } )
   public void testSuccessSearch() throws JsonParseException, JsonMappingException, IOException {
      Response response = search( 200, Credentials.AUTHORIZED );
      assertTaskSubclassList( defaultTaskSubclassListBuilder(), response );
   }


   @Test
   public void testFailureSearchUnauthenticated() {
      search( 401, Credentials.UNAUTHENTICATED );
   }


   @Test
   public void testFailureSearchUnauthorized() {
      search( 403, Credentials.UNAUTHORIZED );
   }


   private Response search( int statusCode, Credentials credentials ) {
      String username = credentials.getUserName();
      String password = credentials.getPassword();
      Response response = RestAssured.given()
            .queryParam( TaskSubclassSearchParameters.TASK_CLASS_CD, TASK_CLASS_CODE ).auth()
            .preemptive().basic( username, password ).accept( APPLICATION_JSON )
            .contentType( APPLICATION_JSON ).expect().statusCode( statusCode ).when()
            .get( TASK_SUBCLASS_API_PATH );
      return response;
   }


   private void assertTaskSubclassList( List<TaskSubclass> expectedTaskSubclassList,
         Response actualTaskSubclassListResponse )
         throws JsonParseException, JsonMappingException, IOException {
      ObjectMapper objectMapper =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      List<TaskSubclass> actualTaskSubclassList = objectMapper
            .readValue( actualTaskSubclassListResponse.getBody().asString(), objectMapper
                  .getTypeFactory().constructCollectionType( List.class, TaskSubclass.class ) );
      assertEquals( TASK_SUBCLASS_RECORD_COUNT, actualTaskSubclassList.size() );
      assertTrue(
            "Expected: " + expectedTaskSubclassList + " but received: " + actualTaskSubclassList,
            actualTaskSubclassList.containsAll( expectedTaskSubclassList ) );
   }


   private List<TaskSubclass> defaultTaskSubclassListBuilder() {
      List<TaskSubclass> expectedTaskSubclassList = new ArrayList<TaskSubclass>();

      TaskSubclass taskSubclass1 = new TaskSubclass();
      taskSubclass1.setCode( CODE1 );
      taskSubclass1.setDescription( DESCRIPTION1 );
      taskSubclass1.setName( NAME1 );
      taskSubclass1.setTaskClassCode( TASK_CLASS_CODE );
      taskSubclass1.setUserCode( USER_CODE1 );

      TaskSubclass taskSubclass2 = new TaskSubclass();
      taskSubclass2.setCode( CODE2 );
      taskSubclass2.setDescription( DESCRIPTION2 );
      taskSubclass2.setName( NAME2 );
      taskSubclass2.setTaskClassCode( TASK_CLASS_CODE );
      taskSubclass2.setUserCode( USER_CODE2 );

      expectedTaskSubclassList.add( taskSubclass1 );
      expectedTaskSubclassList.add( taskSubclass2 );

      return expectedTaskSubclassList;
   }

}
