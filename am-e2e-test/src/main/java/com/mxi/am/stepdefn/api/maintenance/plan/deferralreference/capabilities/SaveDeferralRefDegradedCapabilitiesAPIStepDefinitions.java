package com.mxi.am.stepdefn.api.maintenance.plan.deferralreference.capabilities;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.stream.JsonGenerator;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Assert;

import com.mxi.am.driver.common.database.DatabaseDriver;
import com.mxi.am.driver.integrationtesting.Rest;
import com.mxi.am.driver.integrationtesting.RestDriver;
import com.mxi.am.driver.query.AssemblyQueriesDriver;
import com.mxi.am.driver.query.CapabilitiesQueriesDriver;
import com.mxi.am.driver.query.DeferralRefQueriesDriver;
import com.mxi.am.driver.web.AssetManagement;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


/**
 * Step Definitions for save deferral reference capability
 *
 */
public class SaveDeferralRefDegradedCapabilitiesAPIStepDefinitions {

   // API endpoint info
   private static final String AMAPI = "/amapi/";
   private static final String API_PATH = "maintenance/plan/deferral-reference";

   // DATA
   private static final String INVALID_DEFERRAL_REF_ID = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
   private static final int CARRIER_ID = 1001;
   private static final int CARRIER_DB_ID = 0;

   private static final String NO = "NO";
   private static final String YES = "YES";
   private static final int ASSEMBLY_DB_ID = 4650;
   private static final int AIRCRAFT_DB_ID = 10;
   private static final int DB_REF_ID = 1;

   // Deferral Reference
   private static final String DEFERRAL_REFERENCE_ASSMBL_CODE = "ACFTMOC3";
   public static final String DEFERRAL_REFERENCE_NAME = "DEFER_REF_API";
   public static final String DEFERRAL_REFERENCE_STATUS_CODE = "ACTV";
   public static final String DEFERRAL_REFERENCE_ASSEMBLY_TYPE = "Aircraft MOC 3";
   public static final String DEFERRAL_REFERENCE_OPERATOR = "MXI";
   public static final String DEFERRAL_REFERENCE_DESCRIPTION = "Degraded Capability Test";
   public static final String DEFERRAL_REFERENCE_FAILED_SYSTEM = "Aircraft System 1";
   public static final String DEFERRAL_REFERENCE_FAILED_SYSTEM_CD = "SYS-1";
   public static final String DEFERRAL_REFERENCE_APPLICABILITY = "100-999";
   private static final String DEFERRAL_REFERENCE_DEADLINE_TYPE = "CDY";
   public static final String DEFERRAL_REFERENCE_SEVERITY = "MEL";
   public static final String DEFERRAL_REFERENCE_CLASS = "MEL B";
   public static final String DEFERRAL_REFERENCE_NUMBER_INSTALLED = "2";
   public static final String DEFERRAL_REFERENCE_REQ_FOR_DISPATCH = "1";
   private static final String DEFERRAL_REFERENCE_DEGRADED_CAPABILITIES_TYPE = "SECOBSVR";
   public static final String DEFERRAL_REFERENCE_OPERATIONAL_RESTRICTIONS =
         "An operational restriction";
   public static final String DEFERRAL_REFERENCE_MAINTENANCE_ACTIONS = "A maintenance action";
   public static final String DEFERRAL_REFERENCE_PERFORMANCE_PENALTIES = "A performance penalty";

   private static Response iResponse;
   private static String iDeferRefUuid;
   private static String iAssemblyRefUuid;
   private static String iFailedSystemUuid;
   private static String iOperatorUuid;

   @Inject
   @Rest
   private RestDriver iRestDriver;

   @Inject
   @AssetManagement
   private DatabaseDriver iDatabaseDriver;

   private DeferralRefQueriesDriver iDeferralRefQueriesDriver;
   private AssemblyQueriesDriver iAssemblyQueriesDriver;
   private CapabilitiesQueriesDriver iCapabiltiesQueriesDriver;


   @Inject
   public SaveDeferralRefDegradedCapabilitiesAPIStepDefinitions(
         DeferralRefQueriesDriver aDeferralRefQueriesDriver,
         AssemblyQueriesDriver aAssemblyQueriesDriver,
         CapabilitiesQueriesDriver aCapabiltiesQueriesDriver) {
      iDeferralRefQueriesDriver = aDeferralRefQueriesDriver;
      iAssemblyQueriesDriver = aAssemblyQueriesDriver;
      iCapabiltiesQueriesDriver = aCapabiltiesQueriesDriver;
   };


   @After( "@Save-Degraded-Capability-Tear-Down" )
   public void degradedCapabilityTearDown() {
      iDeferralRefQueriesDriver.removeDeferRefData( iDeferRefUuid );
      iCapabiltiesQueriesDriver.removeCapabilitiesAndLevels( DEFERRAL_REFERENCE_ASSMBL_CODE );
   }


   @Given( "^a deferral reference exists$" )
   public void givenDeferralReferenceExists() throws Throwable {

      insertAssemblyCapabilities();

      String lAssemblyBomId = iDeferralRefQueriesDriver
            .getAssmblBomIdByAssblCd( DEFERRAL_REFERENCE_ASSMBL_CODE, ASSEMBLY_DB_ID, DB_REF_ID );

      // Create a deferral reference
      Assert.assertTrue( iDeferralRefQueriesDriver.insertDeferralReference( ASSEMBLY_DB_ID, 1,
            ASSEMBLY_DB_ID, DEFERRAL_REFERENCE_ASSMBL_CODE, DEFERRAL_REFERENCE_NAME, 0,
            DEFERRAL_REFERENCE_SEVERITY, 0, DEFERRAL_REFERENCE_CLASS, lAssemblyBomId ) == 1 );
      initDeferralReferenceUuid();
      initUuids();
   }


   @Given( "^a deferral reference exists with a degraded capability$" )
   public void givenDeferralReferenceExistsWithDegradedCapability() throws Throwable {

      insertAssemblyCapabilities();

      String lAssemblyBomId = iDeferralRefQueriesDriver
            .getAssmblBomIdByAssblCd( DEFERRAL_REFERENCE_ASSMBL_CODE, ASSEMBLY_DB_ID, DB_REF_ID );

      // Create a Deferral Reference
      Assert.assertTrue( iDeferralRefQueriesDriver.insertDeferralReference( ASSEMBLY_DB_ID, 1,
            ASSEMBLY_DB_ID, DEFERRAL_REFERENCE_ASSMBL_CODE, DEFERRAL_REFERENCE_NAME, 0,
            DEFERRAL_REFERENCE_SEVERITY, 0, DEFERRAL_REFERENCE_CLASS, lAssemblyBomId ) == 1 );
      initDeferralReferenceUuid();
      initUuids();
      // Add the Degraded Capability to deferral reference
      sendRequest( iDeferRefUuid, getDegradedCapabilityToAdd() );

   }


   @When( "^I send an API message to remove a degraded capability from a deferral reference$" )
   public void removeDegradedCapabilityFromDeferralReference() throws Throwable {
      sendRequest( iDeferRefUuid, getDegradedCapabilityToRemove() );
   }


   @When( "^I send an API message to add a degraded capability to the deferral reference$" )
   public void addDegradedCapabilityToDeferralReference() throws Throwable {
      sendRequest( iDeferRefUuid, getDegradedCapabilityToAdd() );
   }


   @When( "^I send an API message to change a degraded capability on a deferral reference$" )
   public void changeDegradedCapabilityOnDeferralReference() throws Throwable {
      sendRequest( iDeferRefUuid, getUpdatedDegradedCapability() );
   }


   @When( "^I send an API message to add a degraded capability to an unknown deferral reference$" )
   public void addDegradedCapabilityToUnknownDeferralReference() throws Throwable {
      initUuids();
      sendRequest( INVALID_DEFERRAL_REF_ID, getUpdatedUnknownDegradedCapability() );
   }


   @Then( "^the degraded capability is added to the deferral reference$" )
   public void theDegradedCapabilitiesIsAdded() {
      // assert that response code was successful
      Assert.assertTrue( "Invalid Response [" + iResponse + "]", iResponse.getStatus() == 200 );
      JsonArray lJsonArray = getDegradedCapabiltiesJSON();
      // compare the results
      Assert.assertTrue( lJsonArray.getJsonObject( 0 ).getString( "code" )
            .contentEquals( DEFERRAL_REFERENCE_DEGRADED_CAPABILITIES_TYPE ) );
      Assert.assertTrue(
            lJsonArray.getJsonObject( 0 ).getString( "levelCode" ).contentEquals( YES ) );
   }


   @Then( "^the degraded capability is updated for the deferral reference$" )
   public void theDegradedCapabilityIsUpdated() {
      // assert that response code was successful
      Assert.assertTrue( "Invalid Response [" + iResponse + "]", iResponse.getStatus() == 200 );
      JsonArray lJsonArray = getDegradedCapabiltiesJSON();
      // compare the results
      Assert.assertTrue( lJsonArray.getJsonObject( 0 ).getString( "code" )
            .contentEquals( DEFERRAL_REFERENCE_DEGRADED_CAPABILITIES_TYPE ) );
      Assert.assertTrue(
            lJsonArray.getJsonObject( 0 ).getString( "levelCode" ).contentEquals( NO ) );
   }


   @Then( "^the degraded capability is removed from the deferral reference$" )
   public void theDegradedCapabilitiesIsRemoved() {
      // assert that response code was successful
      Assert.assertTrue( "Invalid Response [" + iResponse + "]", iResponse.getStatus() == 200 );
      JsonArray lJsonArray = getDegradedCapabiltiesJSON();
      Assert.assertTrue( lJsonArray.isEmpty() );
   }


   @Then( "^the save degraded capability response returns a 4XX error$" )
   public void theResponseReturns400Error() {
      // assert that response code was a 400 class error
      Assert.assertTrue( "Invalid Response [" + iResponse + "]", iResponse.getStatus() == 400 );
   }


   private void sendRequest( String aRefUuid, Entity<String> aMessage ) {

      iResponse = iRestDriver.target( AMAPI + API_PATH + "/" + aRefUuid ).request()
            .accept( MediaType.APPLICATION_JSON ).put( aMessage );
   }


   private JsonArray getDegradedCapabiltiesJSON() {
      // Convert to JSON for parsing
      StringReader lReader = new StringReader( iResponse.readEntity( String.class ) );
      JsonReader lJsonReader = Json.createReader( lReader );
      JsonObject lJsonResponse = lJsonReader.readObject();
      lJsonReader.close();
      // get degraded capabilities entries
      return lJsonResponse.getJsonArray( "degradedCapabilities" );
   }


   private void initDeferralReferenceUuid() {
      iDeferRefUuid = iDeferralRefQueriesDriver.getAltIdByDeferRefKey( ASSEMBLY_DB_ID, 1 );
   }


   private void initUuids() {
      iOperatorUuid = iDeferralRefQueriesDriver.getOperatorAltIdByKey( CARRIER_DB_ID, CARRIER_ID );
      iAssemblyRefUuid =
            iAssemblyQueriesDriver.getAltIdByAssemblyCode( DEFERRAL_REFERENCE_ASSMBL_CODE );
      iFailedSystemUuid =
            iAssemblyQueriesDriver.getBomAltIdByAssemblyCode( DEFERRAL_REFERENCE_ASSMBL_CODE, 1 );
   }


   private void insertAssemblyCapabilities() {
      iCapabiltiesQueriesDriver.insertAssemblyCapabilityLevel( ASSEMBLY_DB_ID,
            DEFERRAL_REFERENCE_ASSMBL_CODE, AIRCRAFT_DB_ID,
            DEFERRAL_REFERENCE_DEGRADED_CAPABILITIES_TYPE, AIRCRAFT_DB_ID, YES );
      iCapabiltiesQueriesDriver.insertAssemblyCapabilityLevel( ASSEMBLY_DB_ID,
            DEFERRAL_REFERENCE_ASSMBL_CODE, AIRCRAFT_DB_ID,
            DEFERRAL_REFERENCE_DEGRADED_CAPABILITIES_TYPE, AIRCRAFT_DB_ID, NO );
   }


   private Entity<String> getDegradedCapabilityToAdd() {
      JsonBuilderFactory lFactory = getJSonFactory();
      JsonArray lDegradedCapabilities =
            getDegradedCapabilities( lFactory, DEFERRAL_REFERENCE_DEGRADED_CAPABILITIES_TYPE, YES );
      JsonObject lDeferralReference =
            getDeferralReferenceInfo( lFactory, lDegradedCapabilities, iDeferRefUuid );
      return Entity.json( lDeferralReference.toString() );

   }


   private Entity<String> getUpdatedDegradedCapability() {
      JsonBuilderFactory lFactory = getJSonFactory();
      JsonArray lDegradedCapabilities =
            getDegradedCapabilities( lFactory, DEFERRAL_REFERENCE_DEGRADED_CAPABILITIES_TYPE, NO );
      JsonObject lDeferralReference =
            getDeferralReferenceInfo( lFactory, lDegradedCapabilities, iDeferRefUuid );
      return Entity.json( lDeferralReference.toString() );
   }


   private Entity<String> getUpdatedUnknownDegradedCapability() {
      JsonBuilderFactory lFactory = getJSonFactory();
      JsonArray lDegradedCapabilities =
            getDegradedCapabilities( lFactory, DEFERRAL_REFERENCE_DEGRADED_CAPABILITIES_TYPE, NO );
      JsonObject lDeferralReference =
            getDeferralReferenceInfo( lFactory, lDegradedCapabilities, INVALID_DEFERRAL_REF_ID );
      return Entity.json( lDeferralReference.toString() );

   }


   private JsonArray getDegradedCapabilities( JsonBuilderFactory aFactory, String aCode,
         String aLevelCode ) {
      JsonArray lDegradedCapabilities =
            aFactory.createArrayBuilder().add( aFactory.createObjectBuilder().add( "code", aCode )
                  .add( "levelCode", aLevelCode ).build() ).build();
      return lDegradedCapabilities;
   }


   private JsonArray getOperatorInfo( JsonBuilderFactory lFactory ) {
      JsonArray lOperators = lFactory.createArrayBuilder().add( lFactory.createObjectBuilder()
            .add( "id", iOperatorUuid ).add( "code", DEFERRAL_REFERENCE_OPERATOR ).build() )
            .build();
      return lOperators;
   }


   private JsonBuilderFactory getJSonFactory() {
      Map<String, Object> lConfig = new HashMap<String, Object>();
      lConfig.put( JsonGenerator.PRETTY_PRINTING, true );
      JsonBuilderFactory lFactory = Json.createBuilderFactory( lConfig );
      return lFactory;
   }


   private JsonArray getEmptyArray( JsonBuilderFactory lFactory ) {
      return lFactory.createArrayBuilder().build();
   }


   private JsonObject getDeadlineInfo( JsonBuilderFactory lFactory ) {
      JsonObject lDeadlineInfo = lFactory.createObjectBuilder()
            .add( "defaultDeadline", lFactory.createObjectBuilder()
                  .add( "type", DEFERRAL_REFERENCE_DEADLINE_TYPE ).add( "quantity", 0 ).build() )
            .build();
      return lDeadlineInfo;
   }


   private Entity<String> getDegradedCapabilityToRemove() {
      JsonBuilderFactory lFactory = getJSonFactory();
      JsonArray lDegradedCapabilities = getEmptyArray( lFactory );
      JsonObject lDeferralReference =
            getDeferralReferenceInfo( lFactory, lDegradedCapabilities, iDeferRefUuid );
      return Entity.json( lDeferralReference.toString() );
   }


   private JsonObject getDeferralReferenceInfo( JsonBuilderFactory aFactory,
         JsonArray aDegradedCapabilities, String aDeferralReferenceId ) {
      JsonObject lDeadlineInfo = getDeadlineInfo( aFactory );
      JsonArray lConflictedDeferralReferences = getEmptyArray( aFactory );
      JsonArray lOperators = getOperatorInfo( aFactory );
      JsonObject lDeferralReference = aFactory.createObjectBuilder()
            .add( "name", DEFERRAL_REFERENCE_NAME ).add( "id", aDeferralReferenceId )
            .add( "description", DEFERRAL_REFERENCE_DESCRIPTION )
            .add( "failedSystemId", iFailedSystemUuid ).add( "assemblyId", iAssemblyRefUuid )
            .add( "assemblyName", DEFERRAL_REFERENCE_ASSEMBLY_TYPE )
            .add( "faultSeverityCode", DEFERRAL_REFERENCE_SEVERITY )
            .add( "faultSeverityClass", DEFERRAL_REFERENCE_CLASS )
            .add( "failedSystemCode", DEFERRAL_REFERENCE_FAILED_SYSTEM_CD )
            .add( "failedSystemName", DEFERRAL_REFERENCE_FAILED_SYSTEM )
            .add( "numberInstalled", DEFERRAL_REFERENCE_NUMBER_INSTALLED )
            .add( "requiredForDispatch", DEFERRAL_REFERENCE_REQ_FOR_DISPATCH )
            .add( "applicabilityRange", DEFERRAL_REFERENCE_APPLICABILITY )
            .add( "operationalRestrictions", DEFERRAL_REFERENCE_OPERATIONAL_RESTRICTIONS )
            .add( "maintenanceActions", DEFERRAL_REFERENCE_MAINTENANCE_ACTIONS )
            .add( "performancePenalties", DEFERRAL_REFERENCE_PERFORMANCE_PENALTIES )
            .add( "statusCode", DEFERRAL_REFERENCE_STATUS_CODE )
            .add( "degradedCapabilities", aDegradedCapabilities )
            .add( "deadlineInfo", lDeadlineInfo )
            .add( "conflictingDeferralReferences", lConflictedDeferralReferences )
            .add( "operators", lOperators ).build();
      return lDeferralReference;
   }
}
