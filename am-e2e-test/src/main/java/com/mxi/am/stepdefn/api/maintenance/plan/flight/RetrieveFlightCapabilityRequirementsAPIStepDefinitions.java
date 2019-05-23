package com.mxi.am.stepdefn.api.maintenance.plan.flight;

import java.io.StringReader;
import java.sql.Date;
import java.util.Calendar;

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
import com.mxi.am.driver.query.FlightQueriesDriver;
import com.mxi.am.driver.query.InventoryInfo;
import com.mxi.am.driver.query.InventoryQueriesDriver;
import com.mxi.am.driver.web.AssetManagement;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


/**
 * Step Definitions to retrieve flight capability requirements
 *
 */
public class RetrieveFlightCapabilityRequirementsAPIStepDefinitions {

   // API endpoint info
   private static final String AMAPI = "/amapi/";
   private static final String API_PATH = "maintenance/plan/flight";

   // SQL templates
   private static final String INSERT_FL_LEG =
         "INSERT INTO fl_leg (leg_id, leg_no, flight_leg_status_cd, aircraft_db_id, aircraft_id, departure_loc_db_id, departure_loc_id, arrival_loc_db_id, arrival_loc_id, sched_arrival_dt, actual_arrival_dt) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
   private static final String INSERT_FL_REQUIREMENT =
         "INSERT INTO fl_requirement (fl_leg_id, cap_db_id, cap_cd, level_db_id, level_cd) VALUES (?, ?, ?, ?, ?)";
   private static String iAircraftSerilNo = "OPER-9088";
   private static String iFlightLegUuid = "9225D541F9F711E688FDC4346B7E1B4B";
   private static final String FLIGHT_NAME = "CA1111";
   private static final String FLIGHT_STATUS = "MXPLAN";
   private static final int DEP_LOC_ID = 100005;
   private static final int ARR_LOC_ID = 100010;
   private static final String CAPABILITY = "ALAND";
   private static final String CAPABILITY_LEVEL = "CATIII";

   private static String iAircraftUuid;

   private static Response iResponse;
   private static JsonArray iJsonResponse;
   private static String iCapabilityResponseTypeString;

   @Inject
   @Rest
   private RestDriver iRestDriver;

   @Inject
   @AssetManagement
   private DatabaseDriver iDatabaseDriver;

   private FlightQueriesDriver iFlightQueriesDriver;
   private InventoryQueriesDriver iInventoryQueriesDriver;


   @Inject
   public RetrieveFlightCapabilityRequirementsAPIStepDefinitions(
         FlightQueriesDriver aFlightQueriesDriver, InventoryQueriesDriver aInventoryQueriesDriver) {
      iFlightQueriesDriver = aFlightQueriesDriver;
      iInventoryQueriesDriver = aInventoryQueriesDriver;
   };


   @After( "@Retrieve-Capability-Success" )
   public void teardown() {
      iFlightQueriesDriver.removeFlightData( iFlightLegUuid );
   }


   @Given( "^a planned flight exists with required capabilities$" )
   public void aPlannedFlightExistsWithRequiredCapabilities() throws Throwable {

      InventoryInfo lAircraftInfo =
            iInventoryQueriesDriver.getInventoryInfoByInventorySerialNo( iAircraftSerilNo );

      // we have to create the dummy dates for flight. Otherwise it will fail when insert into table
      // 'fl_leg'.
      Long lScheduleStartDate = Calendar.getInstance().getTimeInMillis();
      Long lActualStartDate = Calendar.getInstance().getTimeInMillis();

      Assert.assertTrue( iDatabaseDriver.insert( INSERT_FL_LEG, iFlightLegUuid, FLIGHT_NAME,
            FLIGHT_STATUS, lAircraftInfo.getDbId(), lAircraftInfo.getId(), 4650, DEP_LOC_ID, 4650,
            ARR_LOC_ID, new Date( lScheduleStartDate ), new Date( lActualStartDate ) ) == 1 );

      Assert.assertTrue( iDatabaseDriver.insert( INSERT_FL_REQUIREMENT, iFlightLegUuid, 10, "ALAND",
            10, "CATIII" ) == 1 );

      iAircraftUuid = iInventoryQueriesDriver.getAltIdByAircraftSerialNo( iAircraftSerilNo );

   }


   @When( "^a retrieve required flight capabilities API request is sent to Maintenix$" )
   public void aRetrieveRequiredFlightCapabilitiesAPIRequestIsSentToMaintenix() throws Throwable {
      // We want response as JSON hence MediaType.APPLICATION_JSON
      iResponse = iRestDriver.target( AMAPI + API_PATH + "?aircraft_id=" + iAircraftUuid ).request()
            .accept( MediaType.APPLICATION_JSON ).get();
   }


   @Then( "^the list of capabilities required for the flight is returned$" )
   public void theListOfCapabilitiesRequiredForTheFlightIsReturned() throws Throwable {
      iCapabilityResponseTypeString = iResponse.readEntity( String.class );

      // Convert to JSON for parsing
      StringReader lReader = new StringReader( iCapabilityResponseTypeString );
      JsonReader lJsonReader = Json.createReader( lReader );
      iJsonResponse = lJsonReader.readArray();
      lJsonReader.close();
      JsonObject LJsonObject = iJsonResponse.getJsonObject( 0 );

      // assert that response code was successful
      Assert.assertTrue( String.valueOf( iResponse.getStatus() ).contentEquals( "200" ) );

      // get capabilities entries
      JsonArray lJsonArray = LJsonObject.getJsonArray( "capabilityRequirements" );
      Assert.assertTrue(
            lJsonArray.getJsonObject( 0 ).getString( "code" ).contentEquals( CAPABILITY ) );

      // get and check capability level entries
      Assert.assertTrue( lJsonArray.getJsonObject( 0 ).getString( "levelCode" )
            .contentEquals( CAPABILITY_LEVEL ) );

   }


   @When( "^a retrieve flight required capabilities API request is sent to Maintenix for an unknown aircraft$" )
   public void aRetrieveFlightRequiredCapabilitiesAPIRequestIsSentToMaintenixForAnUnknownAircraft()
         throws Throwable {
      // We want response as JSON hence MediaType.APPLICATION_JSON
      iResponse =
            iRestDriver.target( AMAPI + API_PATH + "?aircraft_id=AF093B34D4E3458FAEBB9EBDC44676BF" )
                  .request().accept( MediaType.APPLICATION_JSON ).get();
   }


   @Then( "^the retrieve flight required capabilities response returns a (\\d+)XX with an empty list$" )
   public void the_retrieve_flight_required_capabilities_response_returns_a_XX_with_an_empty_list(
         int arg1 ) throws Throwable {
      iCapabilityResponseTypeString = iResponse.readEntity( String.class );

      // Convert to JSON for parsing
      StringReader lReader = new StringReader( iCapabilityResponseTypeString );
      JsonReader lJsonReader = Json.createReader( lReader );
      iJsonResponse = lJsonReader.readArray();
      lJsonReader.close();

      // assert empty list is retrieved
      Assert.assertTrue( iJsonResponse.isEmpty() );

      // assert that response code was not successful
      Assert.assertTrue( String.valueOf( iResponse.getStatus() ).contentEquals( "200" ) );
   }

}
