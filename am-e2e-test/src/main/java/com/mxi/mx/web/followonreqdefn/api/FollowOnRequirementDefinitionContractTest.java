package com.mxi.mx.web.followonreqdefn.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigDecimal;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.ClassRule;
import org.junit.Test;

import com.mxi.am.driver.common.database.DatabaseDriver;
import com.mxi.am.driver.common.database.Result;
import com.mxi.am.guice.AssetManagementDatabaseDriverProvider;
import com.mxi.mx.common.restassured.RestAssuredRule;

import io.restassured.response.Response;


/**
 * Test class contains a smoke-test for testing the contract for
 * FollowOnRequirementDefinitionEndpoint. This does not exhaustively test the behavior of the
 * end-point corresponding to various inputs.
 *
 */
public class FollowOnRequirementDefinitionContractTest {

   private static final String FOLLOW_ON_REQUIREMENT_DEFINITION_ENDPOINT =
         "maintenix/rest/follow-on-definition/list";
   private static final String USERNAME = "mxi";
   private static final String PASSWORD = "password";
   private static final String INVENTORY_QUERY =
         "SELECT alt_id FROM inv_inv WHERE serial_no_oem = ?";
   private static final String EVENT_QUERY =
         "SELECT evt_event.event_db_id, evt_event.event_id FROM evt_event WHERE evt_event.event_sdesc = ?";
   private static final String FAULT_QUERY =
         "SELECT sd_fault.alt_id FROM sd_fault WHERE sd_fault.fault_db_id = ? AND sd_fault.fault_id = ?";
   private static final String QUERY_PARAM_INVENTORY = "aInventory";
   private static final String QUERY_PARAM_FAULT = "aFaultAltId";
   private static final String INVENTORY_SERIAL_NO = "OPER-21598";
   private static final String FAULT_DESCRIPTION = "FAULT-OPER-24766";
   private static final String ALT_ID = "alt_id";
   private static final String EVENT_DB_ID = "event_db_id";
   private static final String EVENT_ID = "event_id";
   private DatabaseDriver iDriver;

   @ClassRule
   public static final RestAssuredRule iRestAssuredRule = new RestAssuredRule( USERNAME, PASSWORD );


   @Test
   public void
         itReturnsFollowOnRequirementDefinitionWithResponseStatusOkWhenFollowOnDefinitionAgainstInventoryAssembly()
               throws Exception {
      // ARRANGE
      // Aircraft and Follow-On Requirement definition inserted through data loaders
      // maintprogeng\FollowOnRequirementDefinition\C_RI_INVENTORY.csv
      // maintprogeng\FollowOnRequirementDefinition\C_REQ.csv

      iDriver = new AssetManagementDatabaseDriverProvider().get();
      Result lInventoryResult = iDriver.select( INVENTORY_QUERY, INVENTORY_SERIAL_NO );
      Result lFaultEventResult = iDriver.select( EVENT_QUERY, FAULT_DESCRIPTION );
      if ( lInventoryResult.isEmpty() ) {
         fail( "The aircraft inventory was not found" );
      }

      if ( lInventoryResult.getNumberOfRows() > 1 ) {
         fail( "Expecting a single row but found " + lInventoryResult.getNumberOfRows() );
      }
      if ( lFaultEventResult.isEmpty() ) {
         fail( "The fault event was not found" );
      }

      if ( lFaultEventResult.getNumberOfRows() > 1 ) {
         fail( "Expecting a single row but found " + lFaultEventResult.getNumberOfRows() );
      }

      String lInventoryId = lInventoryResult.get( 0 ).getUuidString( ALT_ID );
      BigDecimal lFaultDbId = lFaultEventResult.get( 0 ).get( EVENT_DB_ID );
      BigDecimal lFaultId = lFaultEventResult.get( 0 ).get( EVENT_ID );

      Result lFaultResult =
            iDriver.select( FAULT_QUERY, lFaultDbId.toString(), lFaultId.toString() );

      if ( lFaultResult.isEmpty() ) {
         fail( "The fault  was not found" );
      }

      if ( lFaultResult.getNumberOfRows() > 1 ) {
         fail( "Expecting a single row but found " + lFaultEventResult.getNumberOfRows() );
      }
      String lFaultAltId = lFaultResult.get( 0 ).getUuidString( ALT_ID );

      // ACT
      Response lResponse = iRestAssuredRule.defaultRequest().when()
            .queryParam( QUERY_PARAM_INVENTORY, lInventoryId )
            .queryParam( QUERY_PARAM_FAULT, lFaultAltId )
            .get( FOLLOW_ON_REQUIREMENT_DEFINITION_ENDPOINT );

      // ASSERT
      String lResponseBodyAsString = lResponse.getBody().asString();
      assertEquals( lResponseBodyAsString, 200, lResponse.getStatusCode() );
      JSONObject lFollowOnReqDefnResponseJson =
            ( JSONObject ) new JSONArray( lResponseBodyAsString ).get( 0 );
      String lExpectedFollowReqDefnCode = "FOLLOW-REQ";
      String lActualFollowReqDefnCode = lFollowOnReqDefnResponseJson.getString( "code" );
      assertEquals( "Unexpectededly, the follow-on requirement definition id received is incorrect",
            lExpectedFollowReqDefnCode, lActualFollowReqDefnCode );
   }

}
