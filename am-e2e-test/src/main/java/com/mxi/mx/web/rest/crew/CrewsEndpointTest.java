package com.mxi.mx.web.rest.crew;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.ClassRule;
import org.junit.Test;

import com.mxi.mx.common.restassured.RestAssuredRule;

import io.restassured.response.Response;


/**
 * Smoke tests for the crews endpoint (internal rest api).
 *
 * Note that this am-e2e-test project does not have access to the maintenix project thus we will
 * hard code the end point path and parameter. Refer to
 * {@link com.mxi.mx.web.rest.crew.CrewsEndpoint} for the actual values.
 *
 */
public class CrewsEndpointTest {

   // Values from {@link com.mxi.mx.web.rest.crew.CrewsEndpoint}
   private static final String CREWS_ENDPOINT = "maintenix/rest/crews";
   private static final String USER_ID_PARM = "userId";

   private static final String USERNAME = "mxi";
   private static final String PASSWORD = "password";
   private static final int USER_ID = 100;

   @ClassRule
   public static final RestAssuredRule restAssuredRule = new RestAssuredRule( USERNAME, PASSWORD );


   @Test
   public void getCrewsReturnsOkStatusWhenUserIsAuthorized() throws Exception {

      // Call the endpoint (the parameter value does not matter for this test).
      Response response = restAssuredRule.defaultRequest().when()
            .queryParam( USER_ID_PARM, USER_ID ).get( CREWS_ENDPOINT );

      assertThat( "Unexpected status code.", response.getStatusCode(), Matchers.is( 200 ) );
   }
}
