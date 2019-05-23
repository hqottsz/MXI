package com.mxi.mx.web.rest.datetime;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.ClassRule;
import org.junit.Test;

import com.mxi.mx.common.restassured.RestAssuredRule;

import io.restassured.response.Response;


/**
 * Smoke tests for the date time endpoint (internal rest api).
 *
 * Note that this am-e2e-test project does not have access to the maintenix project thus we will
 * hard code the end point path and parameter. Refer to
 * {@link com.mxi.mx.web.rest.datetime.DateTimeEndpoint} for the actual values.
 *
 */
public class DateTimeEndpointTest {

   // Values from {@link com.mxi.mx.web.rest.datetime.DateTimeEndpoint}
   private static final String DATETIME_ENDPOINT = "maintenix/rest/datetime";
   private static final String GET_CURRENT_DATETIME_ENDPOINT = DATETIME_ENDPOINT + "/getCurrent";

   private static final String USERNAME = "mxi";
   private static final String PASSWORD = "password";

   @ClassRule
   public static final RestAssuredRule restAssuredRule = new RestAssuredRule( USERNAME, PASSWORD );


   @Test
   public void getCurrentDateTimeReturnsOkStatusWhenUserIsAuthorized() throws Exception {
      Response response =
            restAssuredRule.defaultRequest().when().get( GET_CURRENT_DATETIME_ENDPOINT );
      assertThat( "Unexpected status code.", response.getStatusCode(), Matchers.is( 200 ) );
   }

}
