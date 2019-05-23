package com.mxi.mx.web.rest.task;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.ClassRule;
import org.junit.Test;

import com.mxi.mx.common.restassured.RestAssuredRule;

import io.restassured.response.Response;


/**
 * Smoke tests for the tasks endpoint (internal rest api).
 *
 * Note that this am-e2e-test project does not have access to the maintenix project thus we will
 * hard code the end point path and parameter. Refer to
 * {@link com.mxi.mx.web.rest.task.TasksEndpoint} for the actual values.
 *
 */
public class TasksEndpointTest {

   // Values from {@link com.mxi.mx.web.rest.task.TasksEndpoint}
   private static final String TASKS_ENDPOINT = "maintenix/rest/tasks";

   private static final String USERNAME = "mxi";
   private static final String PASSWORD = "password";

   @ClassRule
   public static final RestAssuredRule restAssuredRule = new RestAssuredRule( USERNAME, PASSWORD );


   @Test
   public void getTasksReturnsOkStatusWhenUserIsAuthorized() throws Exception {
      Response response = restAssuredRule.defaultRequest().when().get( TASKS_ENDPOINT );
      assertThat( "Unexpected status code.", response.getStatusCode(), Matchers.is( 200 ) );
   }

}
