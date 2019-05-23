package com.mxi.mx.api.partgroups;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.mapper.factory.Jackson2ObjectMapperFactory;
import io.restassured.response.Response;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.mxi.am.api.driver.PartGroupResourceDriver;
import com.mxi.am.api.helper.Credentials;
import com.mxi.am.driver.common.database.DatabaseDriver;
import com.mxi.am.driver.common.database.Result;
import com.mxi.am.guice.AssetManagementDatabaseDriverProvider;
import com.mxi.mx.api.configurationslots.ConfigurationSlotApiModel;
import com.mxi.mx.api.id.IdApiModel;
import com.mxi.mx.api.serialization.mapper.ObjectMapperFactory;


public class PartGroupResourceBeanTest {

   private DatabaseDriver iDriver;
   private IdApiModel<ConfigurationSlotApiModel> iConfigurationSlotId;

   private static final String APPLICATION_JSON = "application/json";
   private static final String APPLICATION_XML = "application/xml";
   private static final String EMPTY_STRING = "";

   private static final String PART_GROUP_NAME = "A New Part Group #007";
   private static final BigDecimal QUANTITY = BigDecimal.valueOf( 15.1234 );
   private static final String INVENTORY_CLASS_CODE = "BATCH";
   private static final String APPLICABILITY_RANGE = "01-50,200,300-400,10000000-10000001";
   private static final String PURCHASE_TYPE_CODE = "CMNHW";
   private static final boolean LINE_REPLACEABLE_UNIT = false;
   private static final String OTHER_CONDITIONS = "Other Conditions";
   private static final boolean MUST_REQUEST_SPECIFIC_PART = false;

   private PartGroupResourceDriver iPartGroupResourceDriver = new PartGroupResourceDriver();
   private static final String PART_GROUP_PATH = "/mxapi/" + PartGroupApiModel.PATH;


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


   @Before
   public void setUp() throws Exception {
      iDriver = new AssetManagementDatabaseDriverProvider().get();

      String lAssemblyCode = "ACFTAPI1";
      String lAssemblyBomCode = "SYS-1";

      Result lConfigurationSlotIdResult = iDriver.select(
            "SELECT alt_id FROM eqp_assmbl_bom WHERE assmbl_cd = ? AND assmbl_bom_cd = ?",
            lAssemblyCode, lAssemblyBomCode );
      if ( lConfigurationSlotIdResult.isEmpty() ) {
         fail( "Could not find the configuration slot's id of " + lAssemblyCode + ":"
               + lAssemblyBomCode );
      }

      if ( lConfigurationSlotIdResult.getNumberOfRows() > 1 ) {
         fail( "Expecting a single row but found " + lConfigurationSlotIdResult.getNumberOfRows() );
      }

      iConfigurationSlotId =
            new IdApiModel<>( lConfigurationSlotIdResult.get( 0 ).getUuidString( "alt_id" ) );
   }


   @Test
   public void create_arrayAsRootElementReturns400() {
      String lPartGroupApiModel = "[ {\"Invalid Part Group\": \"Body\"} ]";
      create( 400, Credentials.AUTHENTICATED, lPartGroupApiModel );
   }


   @Test
   public void create_businessErrorReturns422() {
      String lCode = String.valueOf( System.currentTimeMillis() );

      // business error: can't create a serialized part group with a decimal quantity
      PartGroupApiModel lPartGroupApiModel =
            defaultPartGroupBuilder().code( lCode ).quantity( BigDecimal.valueOf( 1.1 ) )
                  .inventoryClassCode( "SER" ).configurationSlotId( iConfigurationSlotId ).build();
      create( 422, Credentials.AUTHENTICATED, lPartGroupApiModel );
   }


   @Test
   public void create_duplicateReturns409() {
      String lCode = String.valueOf( System.currentTimeMillis() );
      PartGroupApiModel lPartGroupApiModel = defaultPartGroupBuilder().code( lCode )
            .configurationSlotId( iConfigurationSlotId ).build();

      create( 201, Credentials.AUTHENTICATED, lPartGroupApiModel );

      create( 409, Credentials.AUTHENTICATED, lPartGroupApiModel );
   }


   @Test
   public void create_emptyPostBodyReturns400() {
      create( 400, Credentials.AUTHENTICATED, EMPTY_STRING );
   }


   @Test
   public void create_nullRequiredFieldReturns422() {
      PartGroupApiModel lPartGroupApiModel = defaultPartGroupBuilder().code( null )
            .configurationSlotId( iConfigurationSlotId ).build();

      create( 422, Credentials.AUTHENTICATED, lPartGroupApiModel );
   }


   @Test
   public void create_plainTextReturns400() {
      String lPartGroupApiModel = "Invalid Part Group body";
      create( 400, Credentials.AUTHENTICATED, lPartGroupApiModel );
   }


   @Test
   public void create_returns201() {
      String lCode = String.valueOf( System.currentTimeMillis() );
      PartGroupApiModel lPartGroupApiModel = defaultPartGroupBuilder().code( lCode )
            .configurationSlotId( iConfigurationSlotId ).build();
      create( 201, Credentials.AUTHENTICATED, lPartGroupApiModel );
   }


   @Test
   public void create_unauthenticatedReturns401() {
      String lCode = String.valueOf( System.currentTimeMillis() );
      PartGroupApiModel lPartGroupApiModel = defaultPartGroupBuilder().code( lCode )
            .configurationSlotId( iConfigurationSlotId ).build();
      create( 401, Credentials.UNAUTHENTICATED, lPartGroupApiModel );
   }


   @Test
   public void create_unauthorizedReturns403() {
      String lCode = String.valueOf( System.currentTimeMillis() );
      PartGroupApiModel lPartGroupApiModel = defaultPartGroupBuilder().code( lCode )
            .configurationSlotId( iConfigurationSlotId ).build();

      create( 403, Credentials.UNAUTHORIZED, lPartGroupApiModel );
   }


   @Test
   public void create_notAcceptableReturns406() {
      create( 406, Credentials.AUTHENTICATED, EMPTY_STRING, APPLICATION_JSON, APPLICATION_XML );
   }


   @Test
   public void create_unsupportedMediaTypeReturns415() {
      create( 415, Credentials.AUTHENTICATED, EMPTY_STRING, APPLICATION_XML, APPLICATION_JSON );
   }


   @Test
   public void get_returns200() {
      String lCode = String.valueOf( System.currentTimeMillis() );
      createPartGroup( lCode );
      IdApiModel<PartGroupApiModel> lPartGroupId = getPartGroupId( iConfigurationSlotId, lCode );

      getById( 200, Credentials.AUTHENTICATED, lPartGroupId );
   }


   @Test
   public void get_unauthenticatedReturns401() {
      String lCode = String.valueOf( System.currentTimeMillis() );
      createPartGroup( lCode );
      IdApiModel<PartGroupApiModel> lPartGroupId = getPartGroupId( iConfigurationSlotId, lCode );

      getById( 401, Credentials.UNAUTHENTICATED, lPartGroupId );
   }


   @Test
   public void get_unauthorizedReturns403() {
      String lCode = String.valueOf( System.currentTimeMillis() );
      createPartGroup( lCode );
      IdApiModel<PartGroupApiModel> lPartGroupId = getPartGroupId( iConfigurationSlotId, lCode );

      getById( 403, Credentials.UNAUTHORIZED, lPartGroupId );
   }


   @Test
   public void get_nonExistentPartGroupReturns404() {
      getById( 404, Credentials.AUTHENTICATED,
            new IdApiModel<PartGroupApiModel>( UUID.randomUUID() ) );
   }


   @Test
   public void get_notAcceptableReturns406() {
      String lCode = String.valueOf( System.currentTimeMillis() );
      createPartGroup( lCode );
      IdApiModel<PartGroupApiModel> lPartGroupId = getPartGroupId( iConfigurationSlotId, lCode );

      getById( 406, Credentials.AUTHENTICATED, lPartGroupId, APPLICATION_JSON, APPLICATION_XML );
   }


   @Test
   public void get_unsupportedMediaTypeReturns415() {
      String lCode = String.valueOf( System.currentTimeMillis() );
      createPartGroup( lCode );
      IdApiModel<PartGroupApiModel> lPartGroupId = getPartGroupId( iConfigurationSlotId, lCode );

      getById( 415, Credentials.AUTHENTICATED, lPartGroupId, APPLICATION_XML, APPLICATION_JSON );
   }


   @Test
   public void get_byCodeAndConfigurationSlotIdReturns200()
         throws JsonProcessingException, IOException {
      String lCode = String.valueOf( System.currentTimeMillis() );
      createPartGroup( lCode );

      Response lResponse = getByCodeAndConfigurationSlotId( 200, Credentials.AUTHENTICATED,
            "?configurationSlotId=" + iConfigurationSlotId.toString() + "&code=" + lCode );

      assertNumPartGroups( 1, lResponse );
   }


   @Test
   public void get_byCodeAndConfigurationSlotIdWithMismatchedCasePartGroupCodeReturns200()
         throws JsonProcessingException, IOException {
      String lCodePrefix = String.valueOf( System.currentTimeMillis() );
      String lCode = lCodePrefix + "ABCdef";
      createPartGroup( lCode );

      Response lResponse =
            getByCodeAndConfigurationSlotId( 200, Credentials.AUTHENTICATED, "?configurationSlotId="
                  + iConfigurationSlotId.toString() + "&code=" + lCodePrefix + "aBcDeF" );

      assertNumPartGroups( 1, lResponse );
   }


   @Test
   public void get_byCodeAndConfigurationSlotIdNonExistentPartGroupReturns200WithEmptyArray()
         throws JsonProcessingException, IOException {
      Response lResponse = getByCodeAndConfigurationSlotId( 200, Credentials.AUTHENTICATED,
            "?configurationSlotId=" + iConfigurationSlotId.toString() + "&code=" + "notexisting" );

      assertNumPartGroups( 0, lResponse );
   }


   @Test
   public void get_byCodeAndConfigurationSlotIdWithUnsupportedParameterReturns400() {
      String lCode = String.valueOf( System.currentTimeMillis() );
      createPartGroup( lCode );

      getByCodeAndConfigurationSlotId( 400, Credentials.AUTHENTICATED, "?configurationSlotId="
            + iConfigurationSlotId.toString() + "&code=" + lCode + "&interchangeabilityOrder=1" );
   }


   @Test
   public void get_byCodeAndConfigurationSlotIdWithMissedRequiredParameterReturns400() {
      String lCode = String.valueOf( System.currentTimeMillis() );
      createPartGroup( lCode );

      getByCodeAndConfigurationSlotId( 400, Credentials.AUTHENTICATED,
            "?configurationSlotId=" + iConfigurationSlotId.toString() );
   }


   @Test
   public void get_byCodeAndConfigurationSlotIdWithNoValuesForRequiredParameterReturns400() {
      String lCode = String.valueOf( System.currentTimeMillis() );
      createPartGroup( lCode );

      getByCodeAndConfigurationSlotId( 400, Credentials.AUTHENTICATED,
            "?configurationSlotId=&code=" );
   }


   @Test
   public void get_byCodeAndConfigurationSlotIdWithMalformedConfigurationSlotIdReturns400() {
      String lCode = String.valueOf( System.currentTimeMillis() );
      createPartGroup( lCode );

      getByCodeAndConfigurationSlotId( 400, Credentials.AUTHENTICATED,
            "?configurationSlotId=not-a-config-slot-id&code=PartGroupCode" );
   }


   @Test
   public void get_byCodeAndConfigurationSlotIdUnauthenticatedReturns401() {
      String lCode = String.valueOf( System.currentTimeMillis() );
      createPartGroup( lCode );

      getByCodeAndConfigurationSlotId( 401, Credentials.UNAUTHENTICATED,
            "?configurationSlotId=" + iConfigurationSlotId.toString() + "&code=" + lCode );
   }


   @Test
   public void get_byCodeAndConfigurationSlotIdUnauthorizedReturns403() {
      String lCode = String.valueOf( System.currentTimeMillis() );
      createPartGroup( lCode );

      getByCodeAndConfigurationSlotId( 403, Credentials.UNAUTHORIZED,
            "?configurationSlotId=" + iConfigurationSlotId.toString() + "&code=" + lCode );
   }


   @Test
   public void get_byCodeAndConfigurationSlotIdUnsupportedMediaTypeReturns415() {
      String lCode = String.valueOf( System.currentTimeMillis() );
      createPartGroup( lCode );

      getByCodeAndConfigurationSlotId( 415, Credentials.AUTHENTICATED,
            "?configurationSlotId=" + iConfigurationSlotId.toString() + "&code=" + lCode,
            APPLICATION_XML );
   }


   @Test
   public void update_arrayAsRootElementReturns400() {
      String lCode = String.valueOf( System.currentTimeMillis() );
      createPartGroup( lCode );
      IdApiModel<PartGroupApiModel> lPartGroupId = getPartGroupId( iConfigurationSlotId, lCode );

      String lUpdatePartGroup = "[ {\"Invalid Part Group\": \"Body\"} ]";

      update( 400, Credentials.AUTHENTICATED, lUpdatePartGroup, lPartGroupId );
   }


   @Test
   public void update_businessErrorReturns422() {
      String lCode = String.valueOf( System.currentTimeMillis() );
      createPartGroup( lCode );
      IdApiModel<PartGroupApiModel> lPartGroupId = getPartGroupId( iConfigurationSlotId, lCode );

      // business error: can't modify quantity on update
      PartGroupApiModel lPartGroup = partGroup( lPartGroupId, lCode, PART_GROUP_NAME,
            QUANTITY.add( BigDecimal.ONE ), INVENTORY_CLASS_CODE, iConfigurationSlotId,
            APPLICABILITY_RANGE, PURCHASE_TYPE_CODE, LINE_REPLACEABLE_UNIT, OTHER_CONDITIONS,
            MUST_REQUEST_SPECIFIC_PART, Collections.<AlternatePartApiModel>emptyList() );

      update( 422, Credentials.AUTHENTICATED, lPartGroup, lPartGroupId );
   }


   @Test
   public void update_emptyPutBodyReturns400() {
      String lCode = String.valueOf( System.currentTimeMillis() );
      createPartGroup( lCode );

      IdApiModel<PartGroupApiModel> lPartGroupId = getPartGroupId( iConfigurationSlotId, lCode );
      update( 400, Credentials.AUTHENTICATED, EMPTY_STRING, lPartGroupId );
   }


   @Test
   public void update_existingPartGroupReturns200() {
      String lCode = String.valueOf( System.currentTimeMillis() );
      createPartGroup( lCode );
      IdApiModel<PartGroupApiModel> lPartGroupId = getPartGroupId( iConfigurationSlotId, lCode );

      PartGroupApiModel lUpdatePartGroup =
            defaultPartGroupBuilder().id( lPartGroupId ).code( lCode ).name( PART_GROUP_NAME + "*" )
                  .configurationSlotId( iConfigurationSlotId ).build();

      update( 200, Credentials.AUTHENTICATED, lUpdatePartGroup, lPartGroupId );
   }


   @Test
   public void update_existingPartGroupWithMismatchIdsReturns422() {
      String lCode = String.valueOf( System.currentTimeMillis() );
      createPartGroup( lCode );
      IdApiModel<PartGroupApiModel> lPartGroupId = getPartGroupId( iConfigurationSlotId, lCode );
      IdApiModel<PartGroupApiModel> lMismatchPartGroupId =
            new IdApiModel<PartGroupApiModel>( UUID.randomUUID() );

      PartGroupApiModel lUpdatePartGroup = defaultPartGroupBuilder().id( lMismatchPartGroupId )
            .code( lCode ).configurationSlotId( iConfigurationSlotId ).build();

      update( 422, Credentials.AUTHENTICATED, lUpdatePartGroup, lPartGroupId );
   }


   @Test
   public void update_nonExistentPartGroupReturns404() {
      String lCode = String.valueOf( System.currentTimeMillis() );
      IdApiModel<PartGroupApiModel> lPartGroupId = new IdApiModel<>( UUID.randomUUID() );

      PartGroupApiModel lUpdatePartGroup = defaultPartGroupBuilder().id( lPartGroupId )
            .code( lCode ).configurationSlotId( iConfigurationSlotId ).build();

      update( 404, Credentials.AUTHENTICATED, lUpdatePartGroup, lPartGroupId );
   }


   @Test
   public void update_nullPartGroupIdReturns404() {
      String lCode = String.valueOf( System.currentTimeMillis() );
      createPartGroup( lCode );

      PartGroupApiModel lUpdatePartGroup = defaultPartGroupBuilder().code( lCode )
            .configurationSlotId( iConfigurationSlotId ).build();

      update( 404, Credentials.AUTHENTICATED, lUpdatePartGroup, null );
   }


   @Test
   public void update_nullRequiredFieldReturns422() {
      String lCode = String.valueOf( System.currentTimeMillis() );
      createPartGroup( lCode );

      PartGroupApiModel lUpdatePartGroup = defaultPartGroupBuilder().code( null )
            .configurationSlotId( iConfigurationSlotId ).build();

      IdApiModel<PartGroupApiModel> lPartGroupId = getPartGroupId( iConfigurationSlotId, lCode );
      update( 422, Credentials.AUTHENTICATED, lUpdatePartGroup, lPartGroupId );
   }


   @Test
   public void update_plainTextReturns400() {
      String lCode = String.valueOf( System.currentTimeMillis() );
      createPartGroup( lCode );
      IdApiModel<PartGroupApiModel> lPartGroupId = getPartGroupId( iConfigurationSlotId, lCode );
      String lPartGroup = "Invalid Part Group body";

      update( 400, Credentials.AUTHENTICATED, lPartGroup, lPartGroupId );
   }


   @Test
   public void update_unauthenticatedReturns401() {
      String lCode = String.valueOf( System.currentTimeMillis() );
      createPartGroup( lCode );
      IdApiModel<PartGroupApiModel> lPartGroupId = getPartGroupId( iConfigurationSlotId, lCode );
      PartGroupApiModel lPartGroup = defaultPartGroupBuilder().code( lCode )
            .configurationSlotId( iConfigurationSlotId ).build();
      update( 401, Credentials.UNAUTHENTICATED, lPartGroup, lPartGroupId );
   }


   @Test
   public void update_unauthorizedReturns403() {
      String lCode = String.valueOf( System.currentTimeMillis() );
      createPartGroup( lCode );
      IdApiModel<PartGroupApiModel> lPartGroupId = getPartGroupId( iConfigurationSlotId, lCode );
      PartGroupApiModel lPartGroup = defaultPartGroupBuilder().code( lCode )
            .configurationSlotId( iConfigurationSlotId ).build();
      update( 403, Credentials.UNAUTHORIZED, lPartGroup, lPartGroupId );
   }


   @Test
   public void update_notAcceptableReturns406() {
      String lCode = String.valueOf( System.currentTimeMillis() );
      createPartGroup( lCode );
      IdApiModel<PartGroupApiModel> lPartGroupId = getPartGroupId( iConfigurationSlotId, lCode );

      update( 406, Credentials.AUTHENTICATED, EMPTY_STRING, lPartGroupId, APPLICATION_JSON,
            APPLICATION_XML );
   }


   @Test
   public void update_unsupportedMediaTypeReturns415() {
      String lCode = String.valueOf( System.currentTimeMillis() );
      createPartGroup( lCode );
      IdApiModel<PartGroupApiModel> lPartGroupId = getPartGroupId( iConfigurationSlotId, lCode );

      update( 415, Credentials.AUTHENTICATED, EMPTY_STRING, lPartGroupId, APPLICATION_XML,
            APPLICATION_JSON );
   }


   private Response create( int aStatusCode, Credentials aSecurity, Object aBodyPartGroup ) {
      return create( aStatusCode, aSecurity, aBodyPartGroup, APPLICATION_JSON, APPLICATION_JSON );
   }


   private Response create( int aStatusCode, Credentials aSecurity, Object aPartGroup,
         String aContentType, String aAcceptType ) {
      String lUserName = aSecurity.getUserName();
      String lPassword = aSecurity.getPassword();

      Response lResponse = RestAssured.given().auth().preemptive().basic( lUserName, lPassword )
            .accept( aAcceptType ).contentType( aContentType ).body( aPartGroup ).expect()
            .statusCode( aStatusCode ).when().post( PART_GROUP_PATH );
      return lResponse;
   }


   private Response update( int aStatusCode, Credentials aSecurity, Object aBodyPartGroup,
         IdApiModel<PartGroupApiModel> aId ) {
      return update( aStatusCode, aSecurity, aBodyPartGroup, aId, APPLICATION_JSON,
            APPLICATION_JSON );
   }


   private Response update( int aStatusCode, Credentials aSecurity, Object aBodyPartGroup,
         IdApiModel<PartGroupApiModel> aId, String aContentType, String aAcceptType ) {
      String lUserName = aSecurity.getUserName();
      String lPassword = aSecurity.getPassword();

      Response lResponse = RestAssured.given().auth().preemptive().basic( lUserName, lPassword )
            .accept( aAcceptType ).contentType( aContentType ).body( aBodyPartGroup ).expect()
            .statusCode( aStatusCode ).when().put( PART_GROUP_PATH + "/" + aId );
      return lResponse;
   }


   private Response getById( int aStatusCode, Credentials aSecurity,
         IdApiModel<PartGroupApiModel> aPartGroupId ) {
      return getById( aStatusCode, aSecurity, aPartGroupId, APPLICATION_JSON, APPLICATION_JSON );
   }


   private Response getById( int aStatusCode, Credentials aSecurity,
         IdApiModel<PartGroupApiModel> aPartGroupId, String aContentType, String aAcceptType ) {
      String lUserName = aSecurity.getUserName();
      String lPassword = aSecurity.getPassword();

      Response lResponse = RestAssured.given().auth().preemptive().basic( lUserName, lPassword )
            .accept( aAcceptType ).contentType( aContentType ).expect().statusCode( aStatusCode )
            .when().get( PART_GROUP_PATH + "/" + aPartGroupId.toString() );
      return lResponse;
   }


   private Response getByCodeAndConfigurationSlotId( int aStatusCode, Credentials aSecurity,
         String aQueryParams ) {
      return getByCodeAndConfigurationSlotId( aStatusCode, aSecurity, aQueryParams,
            APPLICATION_JSON );
   }


   private Response getByCodeAndConfigurationSlotId( int aStatusCode, Credentials aSecurity,
         String aQueryParams, String aContentType ) {
      String lUserName = aSecurity.getUserName();
      String lPassword = aSecurity.getPassword();
      Response lResponse = RestAssured.given().auth().preemptive().basic( lUserName, lPassword )
            .contentType( aContentType ).expect().statusCode( aStatusCode ).when()
            .get( PART_GROUP_PATH + aQueryParams );
      return lResponse;
   }


   private IdApiModel<PartGroupApiModel> getPartGroupId(
         IdApiModel<ConfigurationSlotApiModel> aConfigurationSlotId, String aPartGroupCode ) {
      Collection<IdApiModel<PartGroupApiModel>> lPartGroupIds =
            iPartGroupResourceDriver.findPartGroups( aConfigurationSlotId, aPartGroupCode );

      if ( lPartGroupIds.isEmpty() ) {
         fail( "Found no part group(s) with configuration slot id: " + aConfigurationSlotId
               + " and part group code: " + aPartGroupCode );
      } else if ( lPartGroupIds.size() > 1 ) {
         fail( "Found " + lPartGroupIds.size() + " part group(s) with configuration slot id: "
               + aConfigurationSlotId + " and part group code: " + aPartGroupCode );
      }
      return lPartGroupIds.iterator().next();
   }


   private PartGroupApiModel partGroup( IdApiModel<PartGroupApiModel> aId, String aCode,
         String aName, BigDecimal aQuantity, String aInventoryClassCode,
         IdApiModel<ConfigurationSlotApiModel> aConfigurationSlotId, String aApplicabilityRange,
         String aPurchaseTypeCode, Boolean aLineReplaceableUnit, String aOtherConditions,
         Boolean aMustRequestSpecificPart, List<AlternatePartApiModel> aAlternateParts ) {

      return new PartGroupApiModelBuilder().id( aId ).code( aCode ).name( aName )
            .quantity( aQuantity ).inventoryClassCode( aInventoryClassCode )
            .configurationSlotId( aConfigurationSlotId ).applicabilityRange( aApplicabilityRange )
            .purchaseTypeCode( aPurchaseTypeCode ).lineReplaceableUnit( aLineReplaceableUnit )
            .otherConditions( aOtherConditions ).mustRequestSpecificPart( aMustRequestSpecificPart )
            .alternateParts( aAlternateParts ).build();
   }


   private void createPartGroup( String aPartGroupCode ) {
      PartGroupApiModel lPartGroupApiModel = defaultPartGroupBuilder().code( aPartGroupCode )
            .configurationSlotId( iConfigurationSlotId ).build();

      create( 201, Credentials.AUTHENTICATED, lPartGroupApiModel );
   }


   private void assertNumPartGroups( int aNumExpected, Response aResponse )
         throws JsonProcessingException, IOException {
      JsonNode lPartGroupsJson = new ObjectMapper().readTree( aResponse.getBody().asString() );
      assertTrue( lPartGroupsJson.isArray() );
      assertEquals( aNumExpected, lPartGroupsJson.size() );
   }


   private static PartGroupApiModelBuilder defaultPartGroupBuilder() {
      return new PartGroupApiModelBuilder().name( PART_GROUP_NAME ).quantity( QUANTITY )
            .inventoryClassCode( INVENTORY_CLASS_CODE ).applicabilityRange( APPLICABILITY_RANGE )
            .purchaseTypeCode( PURCHASE_TYPE_CODE ).lineReplaceableUnit( LINE_REPLACEABLE_UNIT )
            .otherConditions( OTHER_CONDITIONS )
            .mustRequestSpecificPart( MUST_REQUEST_SPECIFIC_PART )
            .alternateParts( Collections.<AlternatePartApiModel>emptyList() );
   }

}
