package com.mxi.am.api.resource.sys.refterm.taskrevisionreason;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
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
import com.mxi.am.api.helper.Credentials;
import com.mxi.am.api.resource.ObjectMapperFactory;
import com.mxi.am.helper.api.GenericAmApiCalls;

import io.restassured.RestAssured;
import io.restassured.response.Response;


/**
 * Application test for Task Revision Reason ResourceBean
 *
 */
public class TaskRevisionReasonResourceBeanTest {

   private static final String APPLICATION_JSON = "application/json";
   private static final String TASK_REV_REASON_PATH = "/amapi/" + TaskRevisionReason.PATH;

   private static final String TASK_REV_REASON_CODE = "ADMIN";
   private static final String TASK_REV_REASON_NAME = "Administrative Changes";
   private static final String TASK_REV_REASON_DESCRIPTION =
         "Changes made for Administrative purposes.";

   private static final String TASK_REV_REASON_CODE2 = "NEW";
   private static final String TASK_REV_REASON_NAME2 = "New Requirements";
   private static final String TASK_REV_REASON_DESCRIPTION2 = "This Block/Req/Job Card is new.";

   private static final String TASK_REV_REASON_CODE3 = "OBSOLETE";
   private static final String TASK_REV_REASON_NAME3 = "Obsolete Requirements";
   private static final String TASK_REV_REASON_DESCRIPTION3 =
         "This Block/Req/Job Card is obsolete..";

   private static final String TASK_REV_REASON_CODE4 = "REVISED";
   private static final String TASK_REV_REASON_NAME4 = "Revised Requirements";
   private static final String TASK_REV_REASON_DESCRIPTION4 = "This Block/Req/Job Card is revised.";

   private static final int TASK_REVISION_REASON_RECORD_COUNT = 4;
   private static final String INVALID_TASK_REVISION_REASON_CODE = "XXX";

   TaskRevisionReasonPathBuilder pathBuilderTaskRevisionReason =
         new TaskRevisionReasonPathBuilder();


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
   public void get_200() throws JsonProcessingException, IOException {
      Response response = GenericAmApiCalls.get( pathBuilderTaskRevisionReason,
            TASK_REV_REASON_CODE, 200, Credentials.AUTHENTICATED, APPLICATION_JSON );
      assertTaskRevisionReasonForGet( constructTaskRevisionReasonObject(), response );
   }


   @Test
   public void get_404() throws JsonProcessingException, IOException {
      Response response = GenericAmApiCalls.get( pathBuilderTaskRevisionReason,
            INVALID_TASK_REVISION_REASON_CODE, 404, Credentials.AUTHENTICATED, APPLICATION_JSON );
      Assert.assertEquals( 404, response.getStatusCode() );
   }


   @Test
   @CSIContractTest( { Project.AFKLM_IMECH } )
   public void search_200() throws JsonProcessingException, IOException {
      Response response = GenericAmApiCalls.search( pathBuilderTaskRevisionReason, null, 200,
            Credentials.AUTHENTICATED, APPLICATION_JSON );
      assertTaskRevisionReasonForSearch( constructTaskRevisionReasonList(), response );
   }


   @Test
   public void get_401() {
      GenericAmApiCalls.get( pathBuilderTaskRevisionReason, TASK_REV_REASON_CODE, 401,
            Credentials.UNAUTHENTICATED, APPLICATION_JSON );
   }


   private void assertTaskRevisionReasonForGet( TaskRevisionReason expectedTaskRevisionReason,
         Response actualTaskRevisionReason )
         throws JsonParseException, JsonMappingException, IOException {
      ObjectMapper objectMapper =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      TaskRevisionReason taskRevisionReasonActual =
            objectMapper.readValue( actualTaskRevisionReason.getBody().asString(),
                  objectMapper.getTypeFactory().constructType( TaskRevisionReason.class ) );
      Assert.assertEquals( "Incorrect Task revision reason returned", expectedTaskRevisionReason,
            taskRevisionReasonActual );
   }


   private void assertTaskRevisionReasonForSearch( List<TaskRevisionReason> taskRevisionReasonList,
         Response actualtaskRevisionReasonList )
         throws JsonParseException, JsonMappingException, IOException {
      ObjectMapper objectMapper =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      List<TaskRevisionReason> taskRevisionReasonActual = objectMapper.readValue(
            actualtaskRevisionReasonList.getBody().asString(), objectMapper.getTypeFactory()
                  .constructCollectionType( List.class, TaskRevisionReason.class ) );
      assertEquals( "Task revision reason record count mismatched",
            TASK_REVISION_REASON_RECORD_COUNT, taskRevisionReasonActual.size() );
      Assert.assertTrue( "Incorrect Task revision reason list returned",
            taskRevisionReasonActual.containsAll( taskRevisionReasonList ) );
   }


   /**
    * Creates a default TaskRevisionReason object
    *
    */
   public TaskRevisionReason constructTaskRevisionReasonObject() {
      TaskRevisionReason taskRevisionReason = new TaskRevisionReason();
      taskRevisionReason.setCode( TASK_REV_REASON_CODE );
      taskRevisionReason.setName( TASK_REV_REASON_NAME );
      taskRevisionReason.setDescription( TASK_REV_REASON_DESCRIPTION );
      return taskRevisionReason;
   }


   /**
    * Creates a default TaskRevisionReason object List
    *
    */
   public List<TaskRevisionReason> constructTaskRevisionReasonList() {
      List<TaskRevisionReason> taskRevisionReasonList = new ArrayList<TaskRevisionReason>();

      TaskRevisionReason taskRevisionReason = new TaskRevisionReason();
      taskRevisionReason.setCode( TASK_REV_REASON_CODE );
      taskRevisionReason.setName( TASK_REV_REASON_NAME );
      taskRevisionReason.setDescription( TASK_REV_REASON_DESCRIPTION );

      TaskRevisionReason taskRevisionReason2 = new TaskRevisionReason();
      taskRevisionReason2.setCode( TASK_REV_REASON_CODE2 );
      taskRevisionReason2.setName( TASK_REV_REASON_NAME2 );
      taskRevisionReason2.setDescription( TASK_REV_REASON_DESCRIPTION2 );

      TaskRevisionReason taskRevisionReason3 = new TaskRevisionReason();
      taskRevisionReason3.setCode( TASK_REV_REASON_CODE3 );
      taskRevisionReason3.setName( TASK_REV_REASON_NAME3 );
      taskRevisionReason3.setDescription( TASK_REV_REASON_DESCRIPTION3 );

      TaskRevisionReason taskRevisionReason4 = new TaskRevisionReason();
      taskRevisionReason4.setCode( TASK_REV_REASON_CODE4 );
      taskRevisionReason4.setName( TASK_REV_REASON_NAME4 );
      taskRevisionReason4.setDescription( TASK_REV_REASON_DESCRIPTION4 );

      taskRevisionReasonList.add( taskRevisionReason );
      taskRevisionReasonList.add( taskRevisionReason2 );
      taskRevisionReasonList.add( taskRevisionReason3 );
      taskRevisionReasonList.add( taskRevisionReason4 );

      return taskRevisionReasonList;
   }
}
