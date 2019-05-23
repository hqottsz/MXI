package com.mxi.am.stepdefn.api.maintenance.eng.prog.assembly.capabilities;

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
import com.mxi.am.driver.query.AssemblyQueriesDriver;
import com.mxi.am.driver.web.AssetManagement;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


/**
 * Step definitions for the create vendor feature.
 *
 */
public class RetrieveCapabilitiesByAssemblyAPIStepDefinitions {

   // API endpoint info
   private static final String AMAPI = "/amapi/";
   private static final String API_PATH = "maintenance/eng/prog/assembly/capabilities";

   // test case data
   private static final String ASSEMBLY_CODE = "ACFT_CD1";
   private final String iAssemblyAltId;
   private final String iCapabilityDescription = "Second Observer Seat";
   private final String iCapabilityCode = "SECOBSVR";
   private final String iCapabilityLevel1 = "NO";
   private final String iCapabilityLevel2 = "YES";

   public static Response iResponse;
   public static JsonObject iJsonResponse;
   public static String iCapabilityResponseTypeString;

   @Inject
   @Rest
   private RestDriver iRestDriver;

   @Inject
   @AssetManagement
   public DatabaseDriver iDatabaseDriver;


   @Inject
   public RetrieveCapabilitiesByAssemblyAPIStepDefinitions(
         AssemblyQueriesDriver aAssemblyQueriesDriver) {
      iAssemblyAltId = aAssemblyQueriesDriver.getAltIdByAssemblyCode( ASSEMBLY_CODE );
   };


   private static final String CHECK_FOR_CAPABILITY =
         "SELECT * FROM assmbl_cap_levels WHERE assmbl_cap_levels.assmbl_cd = ? AND assmbl_cap_levels.acft_cap_cd = ?";
   private static final String INSERT_CAPABILTIES =
         "INSERT INTO assmbl_cap_levels (assmbl_db_id, assmbl_cd, acft_cap_db_id, acft_cap_cd, acft_cap_level_db_id, acft_cap_level_cd, rstat_cd) VALUES (4650, ?, 10, ?,  10, ?,0)";
   private static final String DELETE_CAPABILTIES =
         "DELETE FROM assmbl_cap_levels WHERE assmbl_cd = ? AND acft_cap_cd = ?";


   @Given( "^a capability is added to assembly$" )
   public void aCapabilityIsAddedToAssembly() throws Throwable {
      // add capability to target assembly if not already assigned
      if ( !( iDatabaseDriver.select( CHECK_FOR_CAPABILITY, ASSEMBLY_CODE, iCapabilityCode )
            .getRows().size() == 2 ) ) {
         Assert.assertTrue( iDatabaseDriver.insert( INSERT_CAPABILTIES, ASSEMBLY_CODE,
               iCapabilityCode, iCapabilityLevel1 ) == 1 );
         Assert.assertTrue( iDatabaseDriver.insert( INSERT_CAPABILTIES, ASSEMBLY_CODE,
               iCapabilityCode, iCapabilityLevel2 ) == 1 );
      }

   }


   @When( "^a Retrieve capabilities by assembly API request is sent to Maintenix$" )
   public void aRetrieveCapabilitiesByAssemblyAPIRequestIsSentToMaintenix() throws Throwable {
      // We want response as JSON hence MediaType.APPLICATION_JSON
      iResponse = iRestDriver.target( AMAPI + API_PATH + "?assembly_id=" + iAssemblyAltId )
            .request().accept( MediaType.APPLICATION_JSON ).get();
   }


   @Then( "^the list of capabilities for the assembly is returned$" )
   public void theListOfCapabilitiesForTheAssemblyIsReturned() throws Throwable {
      iCapabilityResponseTypeString = iResponse.readEntity( String.class );

      // Convert to JSON for parsing
      StringReader lReader = new StringReader( iCapabilityResponseTypeString );
      JsonReader lJsonReader = Json.createReader( lReader );
      iJsonResponse = lJsonReader.readObject();
      lJsonReader.close();

      // assert that response code was successful
      Assert.assertTrue( String.valueOf( iResponse.getStatus() ).contentEquals( "200" ) );
      // check values returned
      JsonObject lJsonAssembly = iJsonResponse.getJsonObject( "assembly" );
      // assert expected assembly code
      Assert.assertTrue( lJsonAssembly.getString( "assemblyCode" ).contentEquals( ASSEMBLY_CODE ) );
      // assert expected assembly id
      Assert.assertTrue( lJsonAssembly.getString( "assemblyId" ).contentEquals( iAssemblyAltId ) );
      // get capabilities entries
      JsonArray lJsonArray = lJsonAssembly.getJsonArray( "capabilities" );
      Assert.assertTrue( lJsonArray.getJsonObject( 0 ).getString( "description" )
            .contentEquals( iCapabilityDescription ) );
      Assert.assertTrue(
            lJsonArray.getJsonObject( 0 ).getString( "code" ).contentEquals( iCapabilityCode ) );
      // get and check capability level entries
      JsonArray lJsonSubArray = lJsonArray.getJsonObject( 0 ).getJsonArray( "levels" );
      Assert.assertTrue( lJsonSubArray.getJsonObject( 0 ).getString( "code" )
            .contentEquals( iCapabilityLevel1 ) );
      Assert.assertTrue( lJsonSubArray.getJsonObject( 1 ).getString( "code" )
            .contentEquals( iCapabilityLevel2 ) );

   }


   @When( "^a Retrieve capabilities by assembly API request is sent to Maintenix for an unknown assembly$" )
   public void aRetrieveCapabilitiesByAssemblyAPIRequestIsSentToMaintenixForAnUnknownAssembly()
         throws Throwable {
      // We want response as JSON hence MediaType.APPLICATION_JSON
      iResponse = iRestDriver.target( AMAPI + API_PATH + "?assembly_id=" + iAssemblyAltId + "XX" )
            .request().accept( MediaType.APPLICATION_JSON ).get();
   }


   @Then( "^the retrieve assembly capabilities response returns (\\d+)XX level error$" )
   public void theRetrieveAssemblyCapabilitiesResponseReturnsXXLevelError( int arg1 )
         throws Throwable {
      iCapabilityResponseTypeString = iResponse.readEntity( String.class );

      // Convert to JSON for parsing
      StringReader lReader = new StringReader( iCapabilityResponseTypeString );
      JsonReader lJsonReader = Json.createReader( lReader );
      iJsonResponse = lJsonReader.readObject();
      lJsonReader.close();

      // assert that response code was not successful
      Assert.assertTrue( String.valueOf( iResponse.getStatus() ).contentEquals( "400" ) );

   }


   @After( "@AfterRetrieveCapabilitiesByAssemblyAPITest" )
   public void afterFeature() {
      // delete capabilities added at start of test
      Assert.assertTrue(
            iDatabaseDriver.update( DELETE_CAPABILTIES, ASSEMBLY_CODE, iCapabilityCode ) == 2 );

   }
}
