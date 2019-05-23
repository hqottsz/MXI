package com.mxi.mx.api.partgroups;

import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.mxi.am.api.driver.PartGroupResourceDriver;
import com.mxi.am.api.helper.Credentials;
import com.mxi.am.api.resource.maintenance.eng.config.configurationslot.ConfigurationSlotResource;
import com.mxi.am.api.resource.maintenance.eng.config.part.PartResource;
import com.mxi.mx.api.configurationslots.ConfigurationSlotApiModel;
import com.mxi.mx.api.id.IdApiModel;
import com.mxi.mx.api.parts.PartApiModel;
import com.mxi.mx.api.serialization.mapper.ObjectMapperFactory;
import com.mxi.mx.core.key.RefInvClassKey;

import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.mapper.factory.Jackson2ObjectMapperFactory;
import io.restassured.response.Response;


public class IpcLoaderContractTest {

   private static final String PART_ENDPOINT = "amapi/maintenance/eng/config/part";
   private static final String CONFIGURATIONSLOT_ENDPOINT =
         "amapi/maintenance/eng/config/configurationslot";
   private static final String PARTGROUPS_ENDPOINT = "mxapi/partgroups/";
   private static final String APPLICATION_JSON = "application/json";

   private static final String PART_MANUFACTURER_CODE = "API01";
   private static final String PART_NUMBER_OEM_1 = "API003";
   private static final String PART_NUMBER_OEM_2 = "API004";
   private static final String CONFIGURATION_SLOT_ASSEMBLY_CODE = "ACFTAPI1";
   private static final String CONFIGURATION_SLOT_CODE = "SYS-1"; // ATA-SYS-CD

   private static final String NAME = "A New Part Group #007";
   private static final BigDecimal QUANTITY = BigDecimal.valueOf( 15.1234 );
   private static final String INVENTORY_CLASS_CODE = "BATCH";
   private static final String APPLICABILITY_RANGE = "01-50,200,300-400,10000000-10000001";
   private static final String PURCHASE_TYPE_CODE = "CMNHW";
   private static final boolean LINE_REPLACEABLE_UNIT = false;
   private static final String OTHER_CONDITIONS = "Other Conditions";
   private static final Boolean MUST_REQUEST_SPECIFIC_PART = false;

   private static final String USERNAME = Credentials.AUTHORIZED.getUserName();
   private static final String PASSWORD = Credentials.AUTHORIZED.getPassword();


   @BeforeClass
   public static void setUpClass() {
      try {
         JacksonJsonProvider lJsonProvider = new JacksonJsonProvider();

         final ObjectMapper sObjectMapper = ObjectMapperFactory.buildMapper();
         lJsonProvider.setMapper( sObjectMapper );

         RestAssured.reset();
         RestAssured.config =
               RestAssuredConfig.config().objectMapperConfig( new ObjectMapperConfig()
                     .jackson2ObjectMapperFactory( new Jackson2ObjectMapperFactory() {

                        @Override
                        public ObjectMapper create( @SuppressWarnings( "rawtypes" ) Class aClass,
                              String s ) {
                           return sObjectMapper;
                        }
                     } ) );
         RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
         RestAssured.baseURI = "http://".concat( InetAddress.getLocalHost().getHostName() );
         RestAssured.port = 80;

      } catch ( UnknownHostException e ) {
         throw new RuntimeException( "Cannot find local host name." );
      }
   }


   private PartGroupResourceDriver iPartGroupResourceDriver = new PartGroupResourceDriver();


   @Test
   public void createPartGroup() throws JsonProcessingException, IOException {
      AlternatePartApiModel lAlternatePart1 = new AlternatePartApiModel();
      lAlternatePart1.setPartId( getPartId( PART_MANUFACTURER_CODE, PART_NUMBER_OEM_1 ) );
      lAlternatePart1.setStandard( true );
      lAlternatePart1.setApproved( true );
      lAlternatePart1.setHasOtherConditions( false );
      lAlternatePart1.setInterchangeabilityOrder( 1 );

      AlternatePartApiModel lAlternatePart2 = new AlternatePartApiModel();
      lAlternatePart2.setPartId( getPartId( PART_MANUFACTURER_CODE, PART_NUMBER_OEM_2 ) );
      lAlternatePart2.setStandard( false );
      lAlternatePart2.setApproved( true );
      lAlternatePart2.setHasOtherConditions( false );
      lAlternatePart2.setInterchangeabilityOrder( 1 );

      IdApiModel<ConfigurationSlotApiModel> lConfigurationSlotId =
            getConfigurationSlotId( CONFIGURATION_SLOT_ASSEMBLY_CODE, CONFIGURATION_SLOT_CODE );

      String lCode = String.valueOf( System.currentTimeMillis() );
      PartGroupApiModel lPartGroup = new PartGroupApiModelBuilder().code( lCode ).name( NAME )
            .quantity( QUANTITY ).inventoryClassCode( INVENTORY_CLASS_CODE )
            .configurationSlotId( lConfigurationSlotId ).applicabilityRange( APPLICABILITY_RANGE )
            .purchaseTypeCode( PURCHASE_TYPE_CODE ).lineReplaceableUnit( LINE_REPLACEABLE_UNIT )
            .otherConditions( OTHER_CONDITIONS )
            .mustRequestSpecificPart( MUST_REQUEST_SPECIFIC_PART )
            .alternateParts( Arrays.asList( lAlternatePart1, lAlternatePart2 ) ).build();

      createPartGroup( 201, lPartGroup );
   }


   @Test
   public void updatePartGroup() throws JsonProcessingException, IOException {
      IdApiModel<ConfigurationSlotApiModel> lConfigurationSlotId =
            getConfigurationSlotId( CONFIGURATION_SLOT_ASSEMBLY_CODE, CONFIGURATION_SLOT_CODE );
      String lCode = String.valueOf( System.currentTimeMillis() );
      createPartGroup( lCode, lConfigurationSlotId );

      IdApiModel<PartGroupApiModel> lPartGroupId = getPartGroupId( lConfigurationSlotId, lCode );
      AlternatePartApiModel lAlternatePart1 = new AlternatePartApiModel();
      lAlternatePart1.setPartId( getPartId( PART_MANUFACTURER_CODE, PART_NUMBER_OEM_1 ) );
      lAlternatePart1.setStandard( true );
      lAlternatePart1.setApproved( true );
      lAlternatePart1.setHasOtherConditions( false );
      lAlternatePart1.setInterchangeabilityOrder( 1 );

      AlternatePartApiModel lAlternatePart2 = new AlternatePartApiModel();
      lAlternatePart2.setPartId( getPartId( PART_MANUFACTURER_CODE, PART_NUMBER_OEM_2 ) );
      lAlternatePart2.setStandard( false );
      lAlternatePart2.setApproved( true );
      lAlternatePart2.setHasOtherConditions( false );
      lAlternatePart2.setInterchangeabilityOrder( 1 );

      PartGroupApiModel lUpdatedPartGroup = new PartGroupApiModelBuilder().id( lPartGroupId )
            .code( lCode ).name( NAME ).quantity( QUANTITY )
            .inventoryClassCode( INVENTORY_CLASS_CODE ).configurationSlotId( lConfigurationSlotId )
            .applicabilityRange( APPLICABILITY_RANGE ).purchaseTypeCode( PURCHASE_TYPE_CODE )
            .lineReplaceableUnit( LINE_REPLACEABLE_UNIT ).otherConditions( OTHER_CONDITIONS )
            .mustRequestSpecificPart( MUST_REQUEST_SPECIFIC_PART )
            .alternateParts( Arrays.asList( lAlternatePart1, lAlternatePart2 ) ).build();

      updatePartGroup( 200, lUpdatedPartGroup, lPartGroupId );
   }


   @Test
   public void getPartGroupById() throws JsonProcessingException, IOException {
      IdApiModel<ConfigurationSlotApiModel> lConfigurationSlotId =
            getConfigurationSlotId( CONFIGURATION_SLOT_ASSEMBLY_CODE, CONFIGURATION_SLOT_CODE );
      String lCode = String.valueOf( System.currentTimeMillis() );;
      Response lResponse = createPartGroup( lCode, lConfigurationSlotId );

      JsonNode lPartGroupResponse = new ObjectMapper().readTree( lResponse.getBody().asString() );

      IdApiModel<PartGroupApiModel> lPartGroupId =
            new IdApiModel<PartGroupApiModel>( lPartGroupResponse.get( "id" ).asText() );

      Response lPartGroupResult = getPartGroup( 200, lPartGroupId );
      assertPartGroup( lPartGroupResult );
   }


   @Test
   public void getPartGroupByCodeAndConfigurationSlotId()
         throws JsonProcessingException, IOException {
      IdApiModel<ConfigurationSlotApiModel> lConfigurationSlotId =
            getConfigurationSlotId( CONFIGURATION_SLOT_ASSEMBLY_CODE, CONFIGURATION_SLOT_CODE );
      String lCode = String.valueOf( System.currentTimeMillis() );
      Response lResponse = createPartGroup( lCode, lConfigurationSlotId );

      JsonNode lPartGroupResponseJson =
            new ObjectMapper().readTree( lResponse.getBody().asString() );

      Response lPartGroupResponse = getPartGroup( 200,
            new IdApiModel<ConfigurationSlotApiModel>(
                  lPartGroupResponseJson.get( "configurationSlotId" ).asText() ),
            lPartGroupResponseJson.get( "code" ).asText() );

      assertPartGroups( 1, lPartGroupResponse );
   }


   /**
    * Tests {@link com.mxi.mx.core.services.stask.taskpart.InvalidQuantityException} error response
    * which will include "code" and "data" on update.
    */
   @Test
   public void validateErrorResponseOnUpdatePartGroup()
         throws JsonProcessingException, IOException {
      IdApiModel<ConfigurationSlotApiModel> lConfigurationSlotId =
            getConfigurationSlotId( CONFIGURATION_SLOT_ASSEMBLY_CODE, CONFIGURATION_SLOT_CODE );

      String lCode = String.valueOf( System.currentTimeMillis() );
      createPartGroup( lCode, lConfigurationSlotId );

      IdApiModel<PartGroupApiModel> lPartGroupId = getPartGroupId( lConfigurationSlotId, lCode );
      String lMismatchedInventoryClassCode = RefInvClassKey.SER.getCd();
      PartGroupApiModel lUpdatedPartGroup =
            new PartGroupApiModelBuilder().id( lPartGroupId ).code( lCode ).name( NAME )
                  .quantity( QUANTITY ).inventoryClassCode( lMismatchedInventoryClassCode )
                  .configurationSlotId( lConfigurationSlotId )
                  .applicabilityRange( APPLICABILITY_RANGE ).purchaseTypeCode( PURCHASE_TYPE_CODE )
                  .lineReplaceableUnit( LINE_REPLACEABLE_UNIT ).otherConditions( OTHER_CONDITIONS )
                  .mustRequestSpecificPart( MUST_REQUEST_SPECIFIC_PART )
                  .alternateParts( Collections.<AlternatePartApiModel>emptyList() ).build();

      Response lResponse = updatePartGroup( 422, lUpdatedPartGroup, lPartGroupId );
      JsonNode lPartGroupResponse = new ObjectMapper().readTree( lResponse.getBody().asString() );
      assertTrue( lPartGroupResponse.isArray() );
      assertTrue( lPartGroupResponse.size() > 0 );
      JsonNode lError = lPartGroupResponse.get( 0 );
      assertTrue( lError.has( "code" ) );
      assertThat( lError.get( "code" ).asText(), not( isEmptyString() ) );

      assertTrue( lError.has( "data" ) );
   }


   /**
    * Tests {@link com.mxi.mx.core.services.stask.taskpart.InvalidQuantityException} error response
    * which will include "code" and "data" on create.
    */
   @Test
   public void validateErrorResponseOnCreatePartGroup()
         throws JsonProcessingException, IOException {
      IdApiModel<ConfigurationSlotApiModel> lConfigurationSlotId =
            getConfigurationSlotId( CONFIGURATION_SLOT_ASSEMBLY_CODE, CONFIGURATION_SLOT_CODE );

      String lCode = String.valueOf( System.currentTimeMillis() );
      String lMismatchedInventoryClassCode = RefInvClassKey.SER.getCd();
      PartGroupApiModel lPartGroup = new PartGroupApiModelBuilder().code( lCode ).name( NAME )
            .quantity( QUANTITY ).inventoryClassCode( lMismatchedInventoryClassCode )
            .configurationSlotId( lConfigurationSlotId ).applicabilityRange( APPLICABILITY_RANGE )
            .purchaseTypeCode( PURCHASE_TYPE_CODE ).lineReplaceableUnit( LINE_REPLACEABLE_UNIT )
            .otherConditions( OTHER_CONDITIONS )
            .mustRequestSpecificPart( MUST_REQUEST_SPECIFIC_PART )
            .alternateParts( Collections.<AlternatePartApiModel>emptyList() ).build();

      Response lResponse = createPartGroup( 422, lPartGroup );

      JsonNode lPartGroupResponse = new ObjectMapper().readTree( lResponse.getBody().asString() );
      assertTrue( lPartGroupResponse.isArray() );
      assertTrue( lPartGroupResponse.size() > 0 );
      JsonNode lError = lPartGroupResponse.get( 0 );
      assertTrue( lError.has( "code" ) );
      assertThat( lError.get( "code" ).asText(), not( isEmptyString() ) );

      assertTrue( lError.has( "data" ) );
   }


   private IdApiModel<PartGroupApiModel> getPartGroupId(
         IdApiModel<ConfigurationSlotApiModel> aConfigurationSlotId, String aCode ) {
      Collection<IdApiModel<PartGroupApiModel>> lPartGroupIds =
            iPartGroupResourceDriver.findPartGroups( aConfigurationSlotId, aCode );

      if ( lPartGroupIds.isEmpty() ) {
         fail( "Found no part group(s) with configuration slot id: " + aConfigurationSlotId
               + " and code: " + aCode );
      } else if ( lPartGroupIds.size() > 1 ) {
         fail( "Found " + lPartGroupIds.size() + " part group(s) with configuration slot id: "
               + aConfigurationSlotId + " and code: " + aCode );
      }
      return lPartGroupIds.iterator().next();
   }


   private Response createPartGroup( String aCode,
         IdApiModel<ConfigurationSlotApiModel> aConfigurationSlotId ) {
      PartGroupApiModel lPartGroup = new PartGroupApiModelBuilder().code( aCode ).name( NAME )
            .quantity( QUANTITY ).inventoryClassCode( INVENTORY_CLASS_CODE )
            .configurationSlotId( aConfigurationSlotId ).applicabilityRange( APPLICABILITY_RANGE )
            .purchaseTypeCode( PURCHASE_TYPE_CODE ).lineReplaceableUnit( LINE_REPLACEABLE_UNIT )
            .otherConditions( OTHER_CONDITIONS )
            .mustRequestSpecificPart( MUST_REQUEST_SPECIFIC_PART )
            .alternateParts( Collections.<AlternatePartApiModel>emptyList() ).build();

      return createPartGroup( 201, lPartGroup );
   }


   private IdApiModel<ConfigurationSlotApiModel> getConfigurationSlotId(
         String aConfigurationSlotAssemblyCode, String aConfigurationSlotCode )
         throws JsonProcessingException, IOException {
      Response lResponse = RestAssured.given().auth().preemptive().basic( USERNAME, PASSWORD )
            .contentType( APPLICATION_JSON )
            .queryParam( ConfigurationSlotResource.ASSEMBLY_CD_PARAM,
                  aConfigurationSlotAssemblyCode )
            .queryParam( ConfigurationSlotResource.CONFIG_SLOT_CD_PARAM, aConfigurationSlotCode )
            .when().get( CONFIGURATIONSLOT_ENDPOINT );

      JsonNode lConfigurationSlotResponse =
            new ObjectMapper().readTree( lResponse.getBody().asString() );
      assertTrue( lConfigurationSlotResponse.isArray() );
      assertEquals( 1, lConfigurationSlotResponse.size() );
      return new IdApiModel<ConfigurationSlotApiModel>(
            lConfigurationSlotResponse.get( 0 ).get( "id" ).asText() );
   }


   private IdApiModel<PartApiModel> getPartId( String aManufacturerCode, String aPartNumberOem )
         throws JsonProcessingException, IOException {
      Response lResponse = RestAssured.given().auth().preemptive().basic( USERNAME, PASSWORD )
            .contentType( APPLICATION_JSON )
            .queryParam( PartResource.MANUFACT_CD_PARAM, aManufacturerCode )
            .queryParam( PartResource.PART_NO_PARAM, aPartNumberOem ).when().get( PART_ENDPOINT );

      JsonNode lPartResponse = new ObjectMapper().readTree( lResponse.getBody().asString() );
      assertTrue( lPartResponse.isArray() );
      assertEquals( 1, lPartResponse.size() );
      return new IdApiModel<PartApiModel>( lPartResponse.get( 0 ).get( "id" ).asText() );
   }


   private Response createPartGroup( int aStatusCode, Object aPartGroup ) {
      Response lResponse = RestAssured.given().auth().preemptive().basic( USERNAME, PASSWORD )
            .contentType( APPLICATION_JSON ).body( aPartGroup ).expect().statusCode( aStatusCode )
            .when().post( PARTGROUPS_ENDPOINT );
      return lResponse;
   }


   private Response updatePartGroup( int aStatusCode, Object aBodyPartGroup,
         IdApiModel<PartGroupApiModel> aId ) {
      Response lResponse = RestAssured.given().auth().preemptive().basic( USERNAME, PASSWORD )
            .contentType( APPLICATION_JSON ).body( aBodyPartGroup ).expect()
            .statusCode( aStatusCode ).when().put( PARTGROUPS_ENDPOINT + aId );
      return lResponse;
   }


   private Response getPartGroup( int aStatusCode, IdApiModel<PartGroupApiModel> aPartGroupId ) {
      Response lResponse = RestAssured.given().auth().preemptive().basic( USERNAME, PASSWORD )
            .contentType( APPLICATION_JSON ).expect().statusCode( aStatusCode ).when()
            .get( PARTGROUPS_ENDPOINT + aPartGroupId.toString() );
      return lResponse;
   }


   private Response getPartGroup( int aStatusCode,
         IdApiModel<ConfigurationSlotApiModel> aConfigurationSlotId, String aCode ) {
      Response lResponse = RestAssured.given().auth().preemptive().basic( USERNAME, PASSWORD )
            .contentType( APPLICATION_JSON ).expect().statusCode( aStatusCode ).when()
            .get( PARTGROUPS_ENDPOINT + "?configurationSlotId=" + aConfigurationSlotId.toString()
                  + "&code=" + aCode );
      return lResponse;

   }


   private void assertPartGroup( Response aResponse ) throws JsonProcessingException, IOException {
      JsonNode lPartGroupJson = new ObjectMapper().readTree( aResponse.getBody().asString() );
      assertPartGroupJson( lPartGroupJson );
   }


   private void assertPartGroups( int aNumExpected, Response aResponse )
         throws JsonProcessingException, IOException {
      JsonNode lPartGroupJson = new ObjectMapper().readTree( aResponse.getBody().asString() );
      assertTrue( lPartGroupJson.isArray() );
      assertEquals( aNumExpected, lPartGroupJson.size() );

      for ( JsonNode lPartGroup : lPartGroupJson ) {
         assertPartGroupJson( lPartGroup );
      }
   }


   private void assertPartGroupJson( JsonNode aPartGroupJson ) {
      assertTrue( aPartGroupJson.has( "code" ) );
      assertTrue( aPartGroupJson.has( "name" ) );
      assertTrue( aPartGroupJson.has( "configurationSlotId" ) );
      assertTrue( aPartGroupJson.has( "inventoryClassCode" ) );
      assertTrue( aPartGroupJson.has( "quantity" ) );
      assertTrue( aPartGroupJson.has( "purchaseTypeCode" ) );
      assertTrue( aPartGroupJson.has( "lineReplaceableUnit" ) );
      assertTrue( aPartGroupJson.has( "applicabilityRange" ) );
      assertTrue( aPartGroupJson.has( "otherConditions" ) );
      assertTrue( aPartGroupJson.has( "mustRequestSpecificPart" ) );
      assertTrue( aPartGroupJson.has( "alternateParts" ) );

      JsonNode lAlternateParts = aPartGroupJson.get( "alternateParts" );
      if ( lAlternateParts.isArray() ) {
         for ( JsonNode lAlternatePart : lAlternateParts ) {
            assertTrue( lAlternatePart.has( "partId" ) );
            assertTrue( lAlternatePart.has( "approved" ) );
            assertTrue( lAlternatePart.has( "standard" ) );
            assertTrue( lAlternatePart.has( "interchangeabilityOrder" ) );
            assertTrue( lAlternatePart.has( "applicabilityRange" ) );
            assertTrue( lAlternatePart.has( "hasOtherConditions" ) );
            assertTrue( lAlternatePart.has( "baselineCode" ) );
         }
      }
   }
}
