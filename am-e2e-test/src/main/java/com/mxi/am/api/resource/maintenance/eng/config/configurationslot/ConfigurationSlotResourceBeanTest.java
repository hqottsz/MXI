package com.mxi.am.api.resource.maintenance.eng.config.configurationslot;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

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
 * Application Tests for Configuration Slot API
 *
 */
public class ConfigurationSlotResourceBeanTest {

   private final String applicationJson = "application/json";
   private final String configurationSlotPath = "/amapi/" + ConfigurationSlot.PATH;

   private String configSlotId;
   private String assemblyId;
   private String positionId;
   private String configSlotCd = "ACFT_CD1";
   private String assemblyCd = "ACFT_CD1";
   private String positionName = "1";
   private int positionCd = 1;

   private DatabaseDriver driver;


   @BeforeClass
   public static void setUpClass() {
      try {
         RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
         RestAssured.baseURI = "http://".concat( InetAddress.getLocalHost().getHostName() );

         RestAssured.port = 80;
      } catch ( UnknownHostException e ) {
         throw new RuntimeException( "Cannot find local host name" );
      }
   }


   @Before
   public void setUpData() throws Exception {
      driver = new AssetManagementDatabaseDriverProvider().get();

      Result configurationSlotResult = driver.select(
            "select config_slot.alt_id from eqp_assmbl_bom config_slot left outer join eqp_assmbl_bom parent_config_slot on config_slot.nh_assmbl_db_id = parent_config_slot.assmbl_db_id and config_slot.nh_assmbl_cd = parent_config_slot.assmbl_cd and config_slot.nh_assmbl_bom_id = parent_config_slot.assmbl_bom_id where config_slot.assmbl_bom_cd=? and config_slot.assmbl_cd=?",
            configSlotCd, assemblyCd );

      Result assemblyResult =
            driver.select( "select alt_id from eqp_assmbl where assmbl_cd=?", assemblyCd );

      Result positionResult = driver.select(
            "select eqp_assmbl_pos.alt_id from eqp_assmbl_pos join eqp_assmbl_bom on eqp_assmbl_pos.assmbl_db_id = eqp_assmbl_bom.assmbl_db_id and eqp_assmbl_pos.assmbl_cd = eqp_assmbl_bom.assmbl_cd and eqp_assmbl_pos.assmbl_bom_id = eqp_assmbl_bom.assmbl_bom_id where eqp_assmbl_bom.assmbl_cd=? and eqp_assmbl_bom.assmbl_bom_cd=?",
            assemblyCd, configSlotCd );

      if ( configurationSlotResult.isEmpty() ) {
         fail( "Could not find Configuration Slot with code: " + configSlotCd );
      }

      if ( assemblyResult.isEmpty() ) {
         fail( "Could not find Assembly with code: " + assemblyCd );
      }

      if ( positionResult.isEmpty() ) {
         fail( "Could not find Position with Assembly Code: " + assemblyCd );
      }

      configSlotId = configurationSlotResult.get( 0 ).getUuidString( "alt_id" );
      assemblyId = assemblyResult.get( 0 ).getUuidString( "alt_id" );
      positionId = positionResult.get( 0 ).getUuidString( "alt_id" );
   }


   @Test
   @CSIContractTest( Project.UPS )
   public void testGetConfigSlotByIdSuccessReturns200()
         throws JsonParseException, JsonMappingException, IOException {
      Response response = createGetByIdRequest( 200, Credentials.AUTHENTICATED, configSlotId,
            applicationJson, applicationJson );
      assertConfigSlotForGet( configurationSlotBuilder(), response );

   }


   @Test
   public void testGetConfigSlotByIdUnauthenticatedReturns401() {
      createGetByIdRequest( 401, Credentials.UNAUTHENTICATED, configSlotId, applicationJson,
            applicationJson );
   }


   @Test
   public void testGetConfigSlotByIdUnauthorizedReturns403() {
      createGetByIdRequest( 403, Credentials.UNAUTHORIZED, configSlotId, applicationJson,
            applicationJson );
   }


   @Test
   public void testSearchConfigSlotSuccessReturns200()
         throws JsonParseException, JsonMappingException, IOException {
      Response response = createSearchRequest( 200, Credentials.AUTHENTICATED, configSlotCd,
            assemblyCd, applicationJson, applicationJson );
      assertConfigSlotForSearch( configurationSlotBuilder(), response );
   }


   @Test
   public void testSearchConfigSlotUnauthenticatedReturns401() {
      createSearchRequest( 401, Credentials.UNAUTHENTICATED, configSlotCd, assemblyCd,
            applicationJson, applicationJson );
   }


   @Test
   public void testSearchConfigSlotUnauthorizedReturns403() {
      createSearchRequest( 403, Credentials.UNAUTHORIZED, configSlotCd, assemblyCd, applicationJson,
            applicationJson );
   }


   /**
    *
    * Creates the Get by ID request
    *
    * @param statusCode
    * @param security
    * @param configSlotId
    * @param contentType
    * @param acceptType
    * @return response
    */
   private Response createGetByIdRequest( int statusCode, Credentials security, String configSlotId,
         String contentType, String acceptType ) {

      String username = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().auth().preemptive().basic( username, password )
            .accept( acceptType ).contentType( contentType ).expect().statusCode( statusCode )
            .when().get( configurationSlotPath + "/" + configSlotId );
      return response;

   }


   /**
    *
    * Creates a Search Request
    *
    * @param statusCode
    * @param security
    * @param configSlotCd
    * @param assemblyCd
    * @param contentType
    * @param acceptType
    * @return response
    */
   private Response createSearchRequest( int statusCode, Credentials security, String configSlotCd,
         String assemblyCd, String contentType, String acceptType ) {

      String username = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given()
            .queryParam( ConfigurationSlotResource.CONFIG_SLOT_CD_PARAM, configSlotCd )
            .queryParam( ConfigurationSlotResource.ASSEMBLY_CD_PARAM, assemblyCd ).auth()
            .preemptive().basic( username, password ).accept( acceptType )
            .contentType( contentType ).expect().statusCode( statusCode ).when()
            .get( configurationSlotPath );
      return response;
   }


   /**
    *
    * Asserts the expected Config Slot result against the actual Config Slot result for Get Method
    *
    * @param expectedConfigSlot
    * @param actualConfigSlot
    * @throws JsonParseException
    * @throws JsonMappingException
    * @throws IOException
    */
   private void assertConfigSlotForGet( ConfigurationSlot expectedConfigSlot,
         Response actualConfigSlot ) throws JsonParseException, JsonMappingException, IOException {

      ObjectMapper objectMapper =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      ConfigurationSlot ConfigSlotactual =
            objectMapper.readValue( actualConfigSlot.getBody().asString(),
                  objectMapper.getTypeFactory().constructType( ConfigurationSlot.class ) );
      Assert.assertEquals( expectedConfigSlot, ConfigSlotactual );
   }


   /**
    *
    * Asserts the expected Config Slot result against the actual Config Slot result for Search
    * Method
    *
    * @param expectedConfigSlot
    * @param actualConfigSlot
    * @throws JsonParseException
    * @throws JsonMappingException
    * @throws IOException
    */
   private void assertConfigSlotForSearch( ConfigurationSlot expectedConfigSlot,
         Response actualConfigSlot ) throws JsonParseException, JsonMappingException, IOException {
      ObjectMapper objectMapper =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      List<ConfigurationSlot> configSlotActual = objectMapper
            .readValue( actualConfigSlot.getBody().asString(), objectMapper.getTypeFactory()
                  .constructCollectionType( List.class, ConfigurationSlot.class ) );
      Assert.assertEquals( 1, configSlotActual.size() );
      Assert.assertEquals( expectedConfigSlot, configSlotActual.get( 0 ) );
   }


   /**
    *
    * Builds the expected Configuration Slot
    *
    * @return configurationSlot
    */
   private ConfigurationSlot configurationSlotBuilder() {
      Position position = new Position();
      position.setId( positionId );
      position.setPositionId( positionCd );
      position.setPositionName( positionName );

      ConfigurationSlot configurationSlot = new ConfigurationSlot();
      configurationSlot.setId( configSlotId );
      configurationSlot.setAssemblyId( assemblyId );
      configurationSlot.setCode( configSlotCd );
      configurationSlot.setAssemblyCode( assemblyCd );
      configurationSlot.setParameters( null );
      configurationSlot.setMandatory( true );
      configurationSlot.setParentId( null );
      configurationSlot.setParentCode( null );
      configurationSlot.setPartGroupIds( null );
      configurationSlot.setPositions( Arrays.asList( position ) );
      configurationSlot.setSubConfigSlots( null );
      configurationSlot.setRequirementDefinitionIds( null );
      return configurationSlot;
   }

}
