package com.mxi.am.stepdefn.api.maintenance.plan.deferralreference.capabilities;

import java.io.StringReader;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Assert;

import com.mxi.am.driver.common.database.DatabaseDriver;
import com.mxi.am.driver.integrationtesting.Rest;
import com.mxi.am.driver.integrationtesting.RestDriver;
import com.mxi.am.driver.query.DeferralRefQueriesDriver;
import com.mxi.am.driver.web.AssetManagement;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


/**
 * Step Definitions for retrieve deferral reference capability
 *
 */
public class RetrieveDeferralRefDegradedCapabilitiesAPIStepDefinitions {

   // API endpoint info
   private static final String AMAPI = "/amapi/";
   private static final String API_PATH = "maintenance/plan/deferral-reference";

   // SQL templates
   private static final String INSERT_CAPABILTIES =
         "INSERT INTO fail_defer_ref_degrad_cap (fail_defer_ref_id, cap_db_id, cap_cd, cap_level_db_id, cap_level_cd) VALUES (?, ?, ?, ?, ?)";

   // test case data
   private static final Integer DEFERRAL_REF_DB_ID = 4650;
   private static final Integer DEFERRAL_REF_ID = 1;
   private static final String CAPABILITY = "SECOBSVR";
   private static final String CAPABILITY_LEVEL = "YES";
   private static final String ASSEMBLY_CODE = "ACFT_CD1";
   private static final String DEFERRAL_REFER_SDESC = "defer_ref_test";
   private static final String DEFER_CODE = "MEL A";
   private static final String SEVERITY_CODE = "MEL";
   private static String iDeferRefUuid;

   private static Response iResponse;
   private static JsonObject iJsonResponse;
   private static String iCapabilityResponseTypeString;

   @Inject
   @Rest
   private RestDriver iRestDriver;

   @Inject
   @AssetManagement
   private DatabaseDriver iDatabaseDriver;

   private DeferralRefQueriesDriver iDeferralRefQueriesDriver;


   @Inject
   public RetrieveDeferralRefDegradedCapabilitiesAPIStepDefinitions(
         DeferralRefQueriesDriver aDeferralRefQueriesDriver) {
      iDeferralRefQueriesDriver = aDeferralRefQueriesDriver;
   };


   @After( "@Retrieve-Capability-Success" )
   public void teardown() {
      iDeferralRefQueriesDriver.removeDeferRefData( iDeferRefUuid );
   }


   @Given( "^a deferral reference including some degraded capability exists$" )
   public void aDeferralReferenceIncludingSomeDegradedCapabilityExists() throws Throwable {

      String lAssemblyBomId = iDeferralRefQueriesDriver.getAssmblBomIdByAssblCd( ASSEMBLY_CODE,
            DEFERRAL_REF_DB_ID, DEFERRAL_REF_ID );

      // Create a deferral reference
      Assert.assertTrue( iDeferralRefQueriesDriver.insertDeferralReference( DEFERRAL_REF_DB_ID,
            DEFERRAL_REF_ID, DEFERRAL_REF_DB_ID, ASSEMBLY_CODE, DEFERRAL_REFER_SDESC, 0,
            SEVERITY_CODE, 0, DEFER_CODE, lAssemblyBomId ) == 1 );

      iDeferRefUuid =
            iDeferralRefQueriesDriver.getAltIdByDeferRefKey( DEFERRAL_REF_DB_ID, DEFERRAL_REF_ID );

      Assert.assertTrue( iDatabaseDriver.insert( INSERT_CAPABILTIES, iDeferRefUuid, 10, CAPABILITY,
            10, CAPABILITY_LEVEL ) == 1 );

   }


   @When( "^I send a retrieve degraded capabilities for deferral reference API request is sent to Maintenix$" )
   public void iSendARetrieveDegradedCapabilitiesForDeferralReferenceAPIRequestIsSentToMaintenix()
         throws Throwable {

      // We want response as JSON hence MediaType.APPLICATION_JSON
      iResponse = iRestDriver.target( AMAPI + API_PATH + "/" + iDeferRefUuid ).request()
            .accept( MediaType.APPLICATION_JSON ).get();
   }


   @Then( "^the list of degraded capability is returned$" )
   public void theListOfDegradedCapabilityIsReturned() throws Throwable {
      iCapabilityResponseTypeString = iResponse.readEntity( String.class );

      // Convert to JSON for parsing
      StringReader lReader = new StringReader( iCapabilityResponseTypeString );
      JsonReader lJsonReader = Json.createReader( lReader );
      iJsonResponse = lJsonReader.readObject();
      lJsonReader.close();

      // assert that response code was successful
      Assert.assertTrue( String.valueOf( iResponse.getStatus() ).contentEquals( "200" ) );

      // get capabilities entries
      JsonArray lJsonArray = iJsonResponse.getJsonArray( "degradedCapabilities" );
      Assert.assertTrue(
            lJsonArray.getJsonObject( 0 ).getString( "code" ).contentEquals( CAPABILITY ) );
      Assert.assertTrue( lJsonArray.getJsonObject( 0 ).getString( "levelCode" )
            .contentEquals( CAPABILITY_LEVEL ) );
   }


   @When( "^a retrieve degraded capabilities for deferral reference API request is sent to Maintenix for an unknown deferral reference$" )
   public void
         aRetrieveDegradedCapabilitiesForDeferralReferenceAPIRequestIsSentToMaintenixForAnUnknownDeferralReference()
               throws Throwable {

      // We want response as JSON hence MediaType.APPLICATION_JSON
      iResponse = iRestDriver.target( AMAPI + API_PATH + "/XX" ).request()
            .accept( MediaType.APPLICATION_JSON ).get();
   }


   @Then( "^the retrieve degraded capability response returns a (\\d+)XX error$" )
   public void theRetrieveDegradedCapabilityResponseReturnsAXXError( int arg1 ) throws Throwable {
      iCapabilityResponseTypeString = iResponse.readEntity( String.class );

      // Convert to JSON for parsing
      StringReader lReader = new StringReader( iCapabilityResponseTypeString );
      JsonReader lJsonReader = Json.createReader( lReader );
      iJsonResponse = lJsonReader.readObject();
      lJsonReader.close();

      // assert that response code was not successful
      Assert.assertTrue( String.valueOf( iResponse.getStatus() ).contentEquals( "400" ) );
   }
}
