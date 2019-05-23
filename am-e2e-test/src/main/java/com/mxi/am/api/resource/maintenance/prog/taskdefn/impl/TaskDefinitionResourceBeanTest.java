package com.mxi.am.api.resource.maintenance.prog.taskdefn.impl;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mxi.am.api.helper.Credentials;
import com.mxi.am.api.resource.ObjectMapperFactory;
import com.mxi.am.api.resource.maintenance.prog.taskdefn.TaskDefinition;
import com.mxi.am.api.resource.maintenance.prog.taskdefn.TaskDefinitionResource;
import com.mxi.am.driver.common.database.DatabaseDriver;
import com.mxi.am.driver.common.database.Result;
import com.mxi.am.guice.AssetManagementDatabaseDriverProvider;

import io.restassured.RestAssured;
import io.restassured.response.Response;


/**
 * HTTP tests for TaskDefinition Resource
 *
 */
public class TaskDefinitionResourceBeanTest {

   private static final String AMAPI = "/amapi/";
   private static DatabaseDriver driver;
   private static final ObjectMapper objectMapper =
         ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();


   @BeforeClass
   public static void setUpClass() throws Exception {
      try {
         RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
         RestAssured.baseURI = "http://".concat( InetAddress.getLocalHost().getHostName() );
         RestAssured.port = 80;
      } catch ( UnknownHostException e ) {
         throw new RuntimeException( "Cannot find local host name" );
      }
      driver = new AssetManagementDatabaseDriverProvider().get();
   }


   /*
    * The 403's
    */
   @Test
   public void testSearchUnauthorizedReturns403() {
      RestAssured.given().auth().preemptive()
            .basic( Credentials.UNAUTHORIZED.getUserName(), Credentials.UNAUTHORIZED.getPassword() )
            .accept( MediaType.APPLICATION_JSON ).expect()
            .statusCode( Status.FORBIDDEN.getStatusCode() ).when()
            .get( AMAPI + TaskDefinition.PATH );
   }


   @Test
   public void testGetUnauthorizedReturns403() {
      RestAssured.given().auth().preemptive()
            .basic( Credentials.UNAUTHORIZED.getUserName(), Credentials.UNAUTHORIZED.getPassword() )
            .accept( MediaType.APPLICATION_JSON ).expect()
            .statusCode( Status.FORBIDDEN.getStatusCode() ).when()
            .get( AMAPI + TaskDefinition.PATH + "/123ABC" );
   }


   @Test
   public void testPutUnauthorizedReturns403() {
      RestAssured.given().auth().preemptive()
            .basic( Credentials.UNAUTHORIZED.getUserName(), Credentials.UNAUTHORIZED.getPassword() )
            .accept( MediaType.APPLICATION_JSON ).contentType( MediaType.APPLICATION_JSON ).expect()
            .statusCode( Status.FORBIDDEN.getStatusCode() ).when()
            .put( AMAPI + TaskDefinition.PATH + "/123ABC" );
   }


   @Test
   public void testPostUnauthorizedReturns403() {
      RestAssured.given().auth().preemptive()
            .basic( Credentials.UNAUTHORIZED.getUserName(), Credentials.UNAUTHORIZED.getPassword() )
            .accept( MediaType.APPLICATION_JSON ).contentType( MediaType.APPLICATION_JSON ).expect()
            .statusCode( Status.FORBIDDEN.getStatusCode() ).when()
            .post( AMAPI + TaskDefinition.PATH );
   }


   /*
    * Functional queries
    */
   @Test
   public void testGetListSuccessReturns200()
         throws JsonParseException, JsonMappingException, IOException {
      Response response = RestAssured.given().auth().preemptive()
            .basic( Credentials.AUTHORIZED.getUserName(), Credentials.AUTHORIZED.getPassword() )
            .queryParam( TaskDefinitionResource.ASSMBL_SLOT_CD_PARAM, "ACFT_CD1" )
            .queryParam( TaskDefinitionResource.CLASS_CD_PARAM, "BLOCK" )
            .accept( MediaType.APPLICATION_JSON ).expect().statusCode( Status.OK.getStatusCode() )
            .when().get( AMAPI + TaskDefinition.PATH );

      List<TaskDefinition> taskDefList = objectMapper.readValue( response.getBody().asByteArray(),
            new TypeReference<List<TaskDefinition>>() {
            } );

      assertTrue( "TaskDef query found no matches", taskDefList.size() > 0 );
   }


   @Test
   public void testGetByInvalidIdReturns404()
         throws JsonParseException, JsonMappingException, IOException {
      RestAssured.given().auth().preemptive()
            .basic( Credentials.AUTHORIZED.getUserName(), Credentials.AUTHORIZED.getPassword() )
            .accept( MediaType.APPLICATION_JSON ).expect()
            .statusCode( Status.NOT_FOUND.getStatusCode() ).when()
            .get( AMAPI + TaskDefinition.PATH + "/123ABC" );
   }


   @Test
   public void testPostReturns200() {
      TaskDefinition taskDef = createValidTaskDef();

      RestAssured.given().auth().preemptive()
            .basic( Credentials.AUTHORIZED.getUserName(), Credentials.AUTHORIZED.getPassword() )
            .accept( MediaType.APPLICATION_JSON ).contentType( MediaType.APPLICATION_JSON )
            .body( taskDef ).expect().statusCode( Status.OK.getStatusCode() ).when()
            .post( AMAPI + TaskDefinition.PATH );
   }


   private TaskDefinition createValidTaskDef() {
      TaskDefinition taskDef = new TaskDefinition();
      taskDef.setAssmblCd( "APU_CD1" );
      taskDef.setConfigSlotCd( "APU-SYS-1-1" );
      taskDef.setName( "AMAPI TaskDef POST Test" );
      taskDef.setCode( "POST-TEST-TASKDEF" );
      taskDef.setClassCd( "REQ" );
      taskDef.setOrganizationId( getMxiOrgId() );
      taskDef.setTaskMustRemove( "NA" );
      return taskDef;
   }


   private String getMxiOrgId() {
      Result queryResult = driver.select( "select * from org_org where org_cd = 'MXI'" );
      if ( queryResult.isEmpty() ) {
         fail( "Could not find MXI Organization" );
      }
      return queryResult.get( 0 ).getUuidString( "alt_id" );
   }

}
