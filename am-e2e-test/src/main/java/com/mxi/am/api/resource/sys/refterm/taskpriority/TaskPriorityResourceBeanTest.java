package com.mxi.am.api.resource.sys.refterm.taskpriority;

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
 * e2e test for Task Priority ResourceBean
 *
 */
public class TaskPriorityResourceBeanTest {

   private static final String TASK_PRIORITY_CODE = "LOW";
   public static final String TASK_PRIORITY_NAME = "Low priority";
   public static final String TASK_PRIORITY_DESCRIPTION = "Low priority";
   public static final int TASK_PRIORITY_ORDER = -1;

   private static final String TASK_PRIORITY_CODE2 = "HIGH";
   public static final String TASK_PRIORITY_NAME2 = "High priority";
   public static final String TASK_PRIORITY_DESCRIPTION2 = "High priority";
   public static final int TASK_PRIORITY_ORDER2 = 1;

   private static final String APPLICATION_JSON = "application/json";
   private static final String TASK_PRIORITY_API_PATH = "/amapi/" + TaskPriority.PATH;

   private static final int TASK_PRIORITY_RECORD_COUNT = 3;
   private static final String TASK_PRIORITY_CODE_DOUBLE = "LOW";


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
   public void testGetTaskPriorityByCodeSuccess200()
         throws JsonProcessingException, IOException, ParseException {
      Response response =
            getTaskPriorityByCode( 200, Credentials.AUTHENTICATED, TASK_PRIORITY_CODE2 );
      assertTaskPriorityForGet( defaultTaskPriorityBuilder(), response );
   }


   @Test
   @CSIContractTest( { Project.AFKLM_IMECH } )
   public void testSearchAllTaskPrioritiesSuccess200()
         throws JsonParseException, JsonMappingException, IOException {
      Response response = searchAllTaskPriorities( 200, Credentials.AUTHENTICATED );
      assertTaskPriorityForSearch( defaultTaskPriorityListBuilder(), response );
   }


   @Test
   public void testGetTaskPriorityByCodeUnauthenticatedFailure401()
         throws JsonProcessingException, IOException {
      getTaskPriorityByCode( 401, Credentials.UNAUTHENTICATED, TASK_PRIORITY_CODE2 );
   }


   @Test
   public void testSearchAllTaskPrioritiesUnauthenticatedFailure401()
         throws JsonProcessingException, IOException {
      searchAllTaskPriorities( 401, Credentials.UNAUTHENTICATED );
   }


   @Test
   public void testGetTaskPriorityByCodeUnauthorizedFailure403() {
      getTaskPriorityByCode( 403, Credentials.UNAUTHORIZED, TASK_PRIORITY_CODE2 );
   }


   @Test
   public void testSearchAllTaskPrioritiesUnauthorizedFailure403() {
      searchAllTaskPriorities( 403, Credentials.UNAUTHORIZED );
   }


   @Test
   public void testGetTaskPriorityByCodeReturnsMultipleResultsFailure500()
         throws AmApiResourceNotFoundException {
      getTaskPriorityByCode( 500, Credentials.AUTHENTICATED, TASK_PRIORITY_CODE_DOUBLE );
   }


   private Response getTaskPriorityByCode( int statusCode, Credentials security,
         String taskPriorityCode ) {
      String userName = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().pathParam( "code", taskPriorityCode ).auth()
            .preemptive().basic( userName, password ).accept( APPLICATION_JSON )
            .contentType( APPLICATION_JSON ).expect().statusCode( statusCode ).when()
            .get( TASK_PRIORITY_API_PATH + "/{code}" );
      return response;
   }


   private Response searchAllTaskPriorities( int statusCode, Credentials security ) {
      String username = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().auth().preemptive().basic( username, password )
            .accept( APPLICATION_JSON ).contentType( APPLICATION_JSON ).expect()
            .statusCode( statusCode ).when().get( TASK_PRIORITY_API_PATH );
      return response;
   }


   private TaskPriority defaultTaskPriorityBuilder() {
      TaskPriority taskPriority = new TaskPriority();
      taskPriority.setCode( TASK_PRIORITY_CODE2 );
      taskPriority.setName( TASK_PRIORITY_NAME2 );
      taskPriority.setDescription( TASK_PRIORITY_DESCRIPTION2 );
      taskPriority.setOrder( TASK_PRIORITY_ORDER2 );
      return taskPriority;
   }


   private List<TaskPriority> defaultTaskPriorityListBuilder() {

      List<TaskPriority> taskPriorityList = new ArrayList<TaskPriority>();

      TaskPriority taskPriority1 = new TaskPriority();
      taskPriority1.setCode( TASK_PRIORITY_CODE );
      taskPriority1.setName( TASK_PRIORITY_NAME );
      taskPriority1.setDescription( TASK_PRIORITY_DESCRIPTION );
      taskPriority1.setOrder( TASK_PRIORITY_ORDER );

      TaskPriority taskPriority2 = new TaskPriority();
      taskPriority2.setCode( TASK_PRIORITY_CODE2 );
      taskPriority2.setName( TASK_PRIORITY_NAME2 );
      taskPriority2.setDescription( TASK_PRIORITY_DESCRIPTION2 );
      taskPriority2.setOrder( TASK_PRIORITY_ORDER2 );

      taskPriorityList.add( taskPriority1 );
      taskPriorityList.add( taskPriority2 );

      return taskPriorityList;
   }


   private void assertTaskPriorityForGet( TaskPriority taskPriorityExpected,
         Response taskPriorityActual ) throws JsonProcessingException, IOException {
      ObjectMapper objectMapper =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      TaskPriority actualTaskPriority =
            objectMapper.readValue( taskPriorityActual.getBody().asString(),
                  objectMapper.getTypeFactory().constructType( TaskPriority.class ) );
      Assert.assertEquals( taskPriorityExpected, actualTaskPriority );
   }


   private void assertTaskPriorityForSearch( List<TaskPriority> taskPriorityListExpected,
         Response taskPriorityListActual ) throws JsonProcessingException, IOException {
      ObjectMapper objectMapper =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      List<TaskPriority> actualTaskPriorityList =
            objectMapper.readValue( taskPriorityListActual.getBody().asString(), objectMapper
                  .getTypeFactory().constructCollectionType( List.class, TaskPriority.class ) );
      assertEquals( TASK_PRIORITY_RECORD_COUNT, actualTaskPriorityList.size() );
      Assert.assertTrue( actualTaskPriorityList.containsAll( taskPriorityListExpected ) );
   }

}
