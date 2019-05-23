package com.mxi.am.api.resource.maintenance.eng.config.assembly;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;

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
import com.mxi.am.driver.common.database.DatabaseDriver;
import com.mxi.am.driver.common.database.Result;
import com.mxi.am.guice.AssetManagementDatabaseDriverProvider;

import io.restassured.RestAssured;
import io.restassured.response.Response;


/**
 * E2E tests - Assembly API
 *
 */
public class AssemblyResourceBeanTest {

   private static final String APPLICATION_JSON = "application/json";
   private static final String ASSEMBLY_API_PATH = "/amapi/" + Assembly.PATH;
   private final ObjectMapper objectMapper =
         ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();

   private static String assemblyId;
   private static final String ASSEMBLY_NAME = "Aircraft LT 7";
   private static final String ASSEMBLY_CODE = "ACFT_LT7";
   private static final String ASSEMBLY_CLASS = "ACFT";

   private static DatabaseDriver driver;


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


   @Before
   public void setUpData() throws ClassNotFoundException, SQLException {
      driver = new AssetManagementDatabaseDriverProvider().get();
      Result assemblyResult =
            driver.select( "select alt_id from eqp_assmbl where assmbl_cd=?", ASSEMBLY_CODE );
      if ( assemblyResult.isEmpty() ) {
         fail( "Could not find the Assembly with code: " + ASSEMBLY_CODE );
      }
      assemblyId = assemblyResult.get( 0 ).getUuidString( "alt_id" );
   }


   /**
    * Test get method for success
    *
    * @throws JsonMappingException
    * @throws JsonParseException
    * @throws IOException
    */
   @Test
   @CSIContractTest( Project.UPS )
   public void testGetAssemblyByIdSuccessReturns200()
         throws JsonParseException, JsonMappingException, IOException {
      Response response =
            getById( 200, Credentials.AUTHORIZED, assemblyId, APPLICATION_JSON, APPLICATION_JSON );
      Assembly actualAssembly =
            objectMapper.readValue( response.getBody().asString(), Assembly.class );
      assertAssembly( defaultAssembly(), actualAssembly );
   }


   /**
    * Test get method for unauthenticated access
    *
    */
   @Test
   public void testGetAssemblyByIdUnauthenticatedReturns401() {
      getById( 401, Credentials.UNAUTHENTICATED, assemblyId, APPLICATION_JSON, APPLICATION_JSON );
   }


   /**
    * Test get method for unauthorized access
    *
    */
   @Test
   public void testGetAssemblyByIdUnauthorizedReturns403() {
      getById( 403, Credentials.UNAUTHORIZED, assemblyId, APPLICATION_JSON, APPLICATION_JSON );
   }


   /**
    * Get an Assembly by passing Id to the Assembly API
    *
    */
   private Response getById( int statusCode, Credentials credentials, String assemblyId,
         String contentType, String acceptType ) {
      String username = credentials.getUserName();
      String password = credentials.getPassword();

      Response response = RestAssured.given().pathParam( "assemblyUUId", assemblyId ).auth()
            .preemptive().basic( username, password ).accept( acceptType )
            .contentType( contentType ).expect().statusCode( statusCode ).when()
            .get( ASSEMBLY_API_PATH + "/{assemblyUUId}" );
      return response;
   }


   private void assertAssembly( Assembly expectedAssembly, Assembly actualAssembly ) {
      Assert.assertEquals( expectedAssembly.getId(), actualAssembly.getId() );
      Assert.assertEquals( expectedAssembly.getName(), actualAssembly.getName() );
      Assert.assertEquals( expectedAssembly.getCode(), actualAssembly.getCode() );
      Assert.assertEquals( expectedAssembly.getClassCode(), actualAssembly.getClassCode() );
   }


   private static Assembly defaultAssembly() {
      Assembly assembly = new Assembly();
      assembly.setId( assemblyId );
      assembly.setName( ASSEMBLY_NAME );
      assembly.setCode( ASSEMBLY_CODE );
      assembly.setClassCode( ASSEMBLY_CLASS );
      return assembly;
   }

}
