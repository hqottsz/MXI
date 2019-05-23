package com.mxi.am.api.resource.maintenance.exec.task.step;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mxi.am.api.annotation.CSIContractTest;
import com.mxi.am.api.annotation.CSIContractTest.Project;
import com.mxi.am.api.helper.Credentials;
import com.mxi.am.api.resource.ObjectMapperFactory;
import com.mxi.am.api.resource.maintenance.exec.task.Task;
import com.mxi.am.driver.common.database.DatabaseDriver;
import com.mxi.am.driver.common.database.Result;
import com.mxi.am.guice.AssetManagementDatabaseDriverProvider;
import com.mxi.am.helper.api.GenericAmApiCalls;

import io.restassured.RestAssured;
import io.restassured.response.Response;


/**
 * E2E test for Step ResourceBean
 *
 */
public class StepResourceBeanTest {

   private static final String APPLICATION_JSON = MediaType.APPLICATION_JSON;

   // Task details
   private static final String TASK_NAME = "RECURRING BLOCK2(RECURRING BLOCK2)";
   private static final String TASK_CLASS = "ADHOC";
   private static final String INVENTORY_SERIAL_NO = "LT-SSP-3";

   // Step details
   private static final int STEP_ORDER1 = 1;
   private static final int STEP_ORDER2 = 2;
   private static final int STEP_ORDER2_UPDATE = 1;
   private static final String STEP_DESCRIPTION1 = "Step one";
   private static final String STEP_DESCRIPTION2 = "Step two";
   private static final String STEP_DESCRIPTION3 = "Step new";

   private static final String STEP_DESCRIPTION2_UPDATE = "Step two order changed";

   private static final String STEP_STATUS_CODE = "MXPENDING";
   private static final String USERNAME = "mxi";

   private static final String INVALID_TASK_ID = "5C8AEC7C56614BC6AC55AAC836B46ECX";

   private final ObjectMapper objectMapper =
         ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();

   private DatabaseDriver driver;
   private String taskId;
   private String inventoryId;

   private StepPathBuilder stepPathBuilder = new StepPathBuilder();


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
   public void setUpData() throws ParseException, Exception {

      driver = new AssetManagementDatabaseDriverProvider().get();

      Result invIdResult = driver.select( "select alt_id from inv_inv where serial_no_oem=?",
            INVENTORY_SERIAL_NO );
      if ( invIdResult.isEmpty() ) {
         fail( "Could not find the inventory with serial number: " + INVENTORY_SERIAL_NO );
      }
      inventoryId = invIdResult.get( 0 ).getUuidString( "alt_id" );

      Result userIdResult =
            driver.select( "select alt_id from utl_user where username=?", USERNAME );
      if ( userIdResult.isEmpty() ) {
         fail( "Could not find the user with username: " + USERNAME );
      }

      prepareData();

   }


   @Test
   public void search_403() {

      Response response = GenericAmApiCalls.get( stepPathBuilder, taskId, 403,
            Credentials.UNAUTHORIZED, APPLICATION_JSON );
      Assert.assertEquals( 403, response.getStatusCode() );
   }


   /**
    *
    * Test the happy path of get method - Steps API. Here the steps we retrieve do not include
    * signoff details.
    *
    * @throws JsonParseException
    * @throws JsonMappingException
    * @throws IOException
    */
   @Test
   @CSIContractTest( { Project.AFKLM_IMECH } )
   public void search_200() throws JsonParseException, JsonMappingException, IOException {

      Response response = GenericAmApiCalls.get( stepPathBuilder, taskId, 200,
            Credentials.AUTHENTICATED, APPLICATION_JSON );
      assertTaskSteps( getTaskStepList(), response );
   }


   @Test
   public void search_404() {

      Response response = GenericAmApiCalls.get( stepPathBuilder, INVALID_TASK_ID, 404,
            Credentials.AUTHENTICATED, APPLICATION_JSON );
      Assert.assertEquals( 404, response.getStatusCode() );
   }


   /**
    *
    * Test the happy path of update method - Steps API
    *
    * @throws JsonParseException
    * @throws JsonMappingException
    * @throws IOException
    */
   @Test
   @CSIContractTest( { Project.AFKLM_IMECH } )
   public void update_200() throws JsonParseException, JsonMappingException, IOException {

      Response searchResponse = GenericAmApiCalls.get( stepPathBuilder, taskId, 200,
            Credentials.AUTHENTICATED, APPLICATION_JSON );
      List<Step> stepsListToUpdate = objectMapper.readValue( searchResponse.getBody().asString(),
            objectMapper.getTypeFactory().constructCollectionType( List.class, Step.class ) );

      // removing step1, updating step2, resetting the order and adding a new step are going to do
      // at the same time.
      stepsListToUpdate.remove( 0 );

      // set values to update and reset the order
      stepsListToUpdate.get( 0 ).setDescription( STEP_DESCRIPTION2_UPDATE );
      stepsListToUpdate.get( 0 ).setOrderNumber( STEP_ORDER2_UPDATE );

      // Add new step
      Step newStep = new Step();
      newStep.setDescription( STEP_DESCRIPTION3 );
      newStep.setOrderNumber( STEP_ORDER2 );
      newStep.setStatusCode( STEP_STATUS_CODE );
      stepsListToUpdate.add( newStep );

      Response updateResponse = GenericAmApiCalls.update( stepPathBuilder, taskId,
            stepsListToUpdate, 200, Credentials.AUTHENTICATED, APPLICATION_JSON );

      assertTaskSteps( stepsListToUpdate, updateResponse );

   }


   @Test
   public void update_404() throws JsonParseException, JsonMappingException, IOException {

      Response searchResponse = GenericAmApiCalls.get( stepPathBuilder, taskId, 200,
            Credentials.AUTHENTICATED, APPLICATION_JSON );
      List<Step> stepsListToUpdate = objectMapper.readValue( searchResponse.getBody().asString(),
            objectMapper.getTypeFactory().constructCollectionType( List.class, Step.class ) );

      stepsListToUpdate.get( 1 ).setDescription( STEP_DESCRIPTION2_UPDATE );

      Response updateResponse = GenericAmApiCalls.update( stepPathBuilder, INVALID_TASK_ID,
            stepsListToUpdate, 404, Credentials.AUTHENTICATED, APPLICATION_JSON );

      Assert.assertEquals( 404, updateResponse.getStatusCode() );

   }


   private void prepareData() throws Exception, JsonMappingException, IOException, ParseException {

      // create a task to be used in tests
      Task task = getTask();
      Response taskResponse = GenericAmApiCalls.create( 200, Credentials.AUTHENTICATED, task,
            Task.PATH, APPLICATION_JSON );
      Task taskCreated = objectMapper.readValue( taskResponse.getBody().asString(), Task.class );
      taskId = taskCreated.getId();

      // Add steps to the task
      List<Step> stepsListToUpdate = getTaskStepList();

      GenericAmApiCalls.update( stepPathBuilder, taskId, stepsListToUpdate, 200,
            Credentials.AUTHENTICATED, APPLICATION_JSON );

   }


   private Task getTask() {
      Task task = new Task();
      task.setName( TASK_NAME );
      task.setTaskClass( TASK_CLASS );
      task.setInventoryId( inventoryId );
      return task;
   }


   private List<Step> getTaskStepList() {
      List<Step> stepsList = new ArrayList<Step>();
      Step step1 = new Step();
      step1.setDescription( STEP_DESCRIPTION1 );
      step1.setOrderNumber( STEP_ORDER1 );
      step1.setStatusCode( STEP_STATUS_CODE );
      stepsList.add( step1 );

      Step step2 = new Step();
      step2.setDescription( STEP_DESCRIPTION2 );
      step2.setOrderNumber( STEP_ORDER2 );
      step2.setStatusCode( STEP_STATUS_CODE );
      stepsList.add( step2 );

      return stepsList;
   }


   private void assertTaskSteps( List<Step> expectedStepsList, Response response )
         throws JsonParseException, JsonMappingException, IOException {

      List<Step> actualStepsList = objectMapper.readValue( response.getBody().asString(),
            objectMapper.getTypeFactory().constructCollectionType( List.class, Step.class ) );

      Assert.assertEquals( "Incorrect no of Steps found: ", actualStepsList.size(), 2 );

      assertStepObjects( expectedStepsList.get( 0 ), actualStepsList.get( 0 ) );
      assertStepObjects( expectedStepsList.get( 1 ), actualStepsList.get( 1 ) );

   }


   private void assertStepObjects( Step expectedStep, Step actualStep ) {
      assertEquals( "Incorrect Step description found: ", expectedStep.getDescription(),
            actualStep.getDescription() );
      assertEquals( "Incorrect Step order found: ", expectedStep.getOrderNumber(),
            actualStep.getOrderNumber() );
      assertEquals( "Incorrect Status code found: ", expectedStep.getStatusCode(),
            actualStep.getStatusCode() );
   }

}
