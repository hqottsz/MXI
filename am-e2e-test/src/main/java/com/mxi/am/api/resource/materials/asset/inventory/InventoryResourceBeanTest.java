package com.mxi.am.api.resource.materials.asset.inventory;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mxi.am.api.annotation.CSIContractTest;
import com.mxi.am.api.annotation.CSIContractTest.Project;
import com.mxi.am.api.helper.Credentials;
import com.mxi.am.api.resource.ObjectMapperFactory;
import com.mxi.am.api.resource.erp.location.Location;
import com.mxi.am.api.resource.erp.location.LocationSearchParameters;
import com.mxi.am.api.resource.materials.proc.owner.Owner;
import com.mxi.am.api.resource.materials.proc.owner.OwnerResource;
import com.mxi.am.driver.common.database.DatabaseDriver;
import com.mxi.am.driver.common.database.Result;
import com.mxi.am.guice.AssetManagementDatabaseDriverProvider;
import com.mxi.mx.common.restassured.RestAssuredRule;

import io.restassured.RestAssured;
import io.restassured.response.Response;


/**
 * Inventory API test
 *
 */
public class InventoryResourceBeanTest {

   private static final String APPLICATION_JSON = "application/json";
   private static final String INVENTORY_API_PATH = "/amapi/" + Inventory.PATH;
   private static final String LOCATION_API_PATH = "/amapi/" + Location.PATH;
   private static final String OWNER_API_PATH = "/amapi/" + Owner.PATH;
   private static final String PART_NO = "ACFT_ASSY_PNX";
   private static final String SERIAL_NO = "PLN-9789";
   private static final String MANUFACT_CODE = "11111";
   private static final String LOCATION_CODE = "AIRPORT1";
   private static final String OWNER_CODE = "MXI";
   private static final String MANUFACTURED_DATE = "Tue Jan 12 00:00:00 EST 2016";
   private static final String RECIEVED_DATE = "Mon Dec 12 00:00:00 EST 2016";
   private static final String INVENTORY_NAME = "Aircraft Part 1 - 9789";
   private static final String INVENTORY_CLASS_CD_ACFT = "ACFT";
   private static final String AC_REG_CODE = "9789";
   private static final String CONFIG_SLOT_CD = "ACFT_CD1";

   private final String GET_CONFIG_SLOT_ID_QUERY =
         "SELECT alt_id FROM eqp_assmbl_bom " + "WHERE assmbl_bom_cd=?";

   private final String GET_HIGHEST_INV_ID_QUERY = "SELECT inv_inv.alt_id " + "FROM inv_inv "
         + "INNER JOIN inv_ac_reg ON " + "inv_inv.inv_no_db_id = inv_ac_reg.inv_no_db_id AND "
         + "inv_inv.inv_no_id = inv_ac_reg.inv_no_id " + "WHERE inv_ac_reg.ac_reg_cd = ?";

   private final String GET_BARCODE_SDESC_QUERY =
         "SELECT barcode_sdesc FROM inv_inv WHERE serial_no_oem = ?";

   public static RestAssuredRule restAssuredRule = null;
   private static String config_slot_id;
   private static String highest_inv_id;
   private static String barcode_sdesc;


   @BeforeClass
   public static void setUpClass() throws JsonParseException, JsonMappingException, IOException,
         ParseException, ClassNotFoundException, SQLException {
      restAssuredRule = new RestAssuredRule();
   }


   @Before
   public void setUp() throws Exception {
      restAssuredRule.setCredentials( Credentials.AUTHENTICATED );
      restAssuredRule.before();

      DatabaseDriver lDriver = new AssetManagementDatabaseDriverProvider().get();

      Result configSlotIdResult = lDriver.select( GET_CONFIG_SLOT_ID_QUERY, CONFIG_SLOT_CD );
      Result highestInvIdResult = lDriver.select( GET_HIGHEST_INV_ID_QUERY, AC_REG_CODE );
      Result barcodeResult = lDriver.select( GET_BARCODE_SDESC_QUERY, SERIAL_NO );

      if ( configSlotIdResult.isEmpty() ) {
         Assert.fail(
               "Could not find the Config Slot Id with Config Slot Code: " + CONFIG_SLOT_CD );
      } else {
         config_slot_id = configSlotIdResult.get( 0 ).getUuidString( "alt_id" );
      }

      if ( highestInvIdResult.isEmpty() ) {
         Assert.fail( "Could not find the Highest Inv Id with Aircraft Reg Code: " + AC_REG_CODE );
      } else {
         highest_inv_id = highestInvIdResult.get( 0 ).getUuidString( "alt_id" );
      }

      if ( barcodeResult.isEmpty() ) {
         Assert.fail( "Could not find the Barcode Sdesc with Serial No: " + SERIAL_NO );
      } else {
         barcode_sdesc = barcodeResult.get( 0 ).get( "barcode_sdesc" );
      }

   }


   @Test
   public void search_401() {
      restAssuredRule.setCredentials( Credentials.UNAUTHENTICATED );
      try {
         restAssuredRule.before();
      } catch ( AssertionError error ) {
         assertEquals(
               "Authentication Failed. Username: " + Credentials.UNAUTHENTICATED.getUserName()
                     + " Password: " + Credentials.UNAUTHENTICATED.getPassword(),
               error.getMessage() );
      }

   }


   @Test
   @CSIContractTest( { Project.UPS, Project.AFKLM_IMECH } )
   public void search_200()
         throws JsonParseException, JsonMappingException, IOException, ParseException {
      Response response = searchInventory( 200, Credentials.AUTHORIZED );
      List<Inventory> inventoryList =
            constructInventoryListFromResponse( response.getBody().asString() );

      if ( CollectionUtils.isNotEmpty( inventoryList ) ) {
         assertEquals( "Retrieved inventory list should contain only one inventory.", 1,
               inventoryList.size() );
         Inventory inventory = inventoryList.get( 0 );
         assertInventory( inventory );
      } else {
         Assert.fail( "No search results were returned" );
      }

   }


   @Test
   public void create_403() throws JsonParseException, JsonMappingException, IOException {
      Inventory newInventory = new Inventory();
      create( 403, Credentials.UNAUTHORIZED, newInventory );
   }


   @Test
   public void update_403() throws JsonParseException, JsonMappingException, IOException {
      Inventory updateDetails = new Inventory();
      updateInventory( 403, "", updateDetails, Credentials.UNAUTHORIZED );

   }


   @Test
   public void get_403() {
      Inventory inventory = new Inventory();
      getById( 403, Credentials.UNAUTHORIZED, inventory.getId() );
   }


   @Test
   public void search_403() {
      searchInventory( 403, Credentials.UNAUTHORIZED );
   }


   private void assertInventory( Inventory inventory )
         throws JsonParseException, JsonMappingException, IOException {

      assertEquals( "Incorrect inventory name found in retrieved inventory.", INVENTORY_NAME,
            inventory.getName() );
      assertEquals( "Incorrect part number found in retrieved inventory.", PART_NO,
            inventory.getPartNumber() );
      assertEquals( "Incorrect serial number found in retrieved inventory.", SERIAL_NO,
            inventory.getSerialNumber() );
      assertEquals( "Incorrect manufacturer code found in retrieved inventory.", MANUFACT_CODE,
            inventory.getManufacturerCode() );
      assertEquals( "Incorrect inventory class found in retrieved inventory.",
            INVENTORY_CLASS_CD_ACFT, inventory.getInventoryClass() );
      assertEquals( "Incorrect manufactured date found in retrieved inventory.", MANUFACTURED_DATE,
            inventory.getManufacturedDate().toString() );
      assertEquals( "Incorrect received date found in retrieved inventory.", RECIEVED_DATE,
            inventory.getReceivedDate().toString() );

      Owner owner = searchOwner( 200, OWNER_CODE );
      assertEquals( "Incorrect owner ID found in retrieved inventory.", owner.getId(),
            inventory.getOwnerId() );

      Location location = searchLocation( 200, LOCATION_CODE );
      assertEquals( "Incorrect location ID found in retrieved inventory.", location.getId(),
            inventory.getLocationId() );
   }


   private Response searchInventory( int statusCode, Credentials security ) {
      String username = security.getUserName();
      String password = security.getPassword();

      Response response =
            RestAssured.given().queryParam( InventorySearchParameters.PART_NO_PARAM, PART_NO )
                  .queryParam( InventorySearchParameters.SERIAL_NO_PARAM, SERIAL_NO )
                  .queryParam( InventorySearchParameters.MANUFACT_CD_PARAM, MANUFACT_CODE )
                  .queryParam( InventorySearchParameters.BARCODE_PARAM, barcode_sdesc )
                  .queryParam( InventorySearchParameters.CONFIG_SLOT_ID, config_slot_id )
                  .queryParam( InventorySearchParameters.HIGHEST_INV_ID, highest_inv_id )
                  .queryParam( InventorySearchParameters.CONFIGURATION_SLOT_CODE, CONFIG_SLOT_CD )
                  .auth().preemptive().basic( username, password ).accept( APPLICATION_JSON )
                  .contentType( APPLICATION_JSON ).expect().statusCode( statusCode ).when()
                  .get( INVENTORY_API_PATH );

      return response;
   }


   private Location searchLocation( int statusCode, String locationCode )
         throws JsonParseException, JsonMappingException, IOException {

      Response response = restAssuredRule.defaultRequest()
            .queryParam( LocationSearchParameters.PARAM_CODE, locationCode )
            .accept( APPLICATION_JSON ).contentType( APPLICATION_JSON ).expect()
            .statusCode( statusCode ).when().get( LOCATION_API_PATH );

      ObjectMapper lObjectMapper =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      List<Location> locationList = lObjectMapper.readValue( response.getBody().asString(),
            new TypeReference<List<Location>>() {
            } );

      if ( CollectionUtils.isNotEmpty( locationList ) ) {
         return locationList.get( 0 );
      } else {
         Assert.fail( "Location not found for code " + locationCode );
         return null;
      }
   }


   private Owner searchOwner( int statusCode, String ownerCode )
         throws JsonParseException, JsonMappingException, IOException {

      Response response =
            restAssuredRule.defaultRequest().queryParam( OwnerResource.OWNER_CODE_PARAM, ownerCode )
                  .accept( APPLICATION_JSON ).contentType( APPLICATION_JSON ).expect()
                  .statusCode( statusCode ).when().get( OWNER_API_PATH );

      ObjectMapper lObjectMapper =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      List<Owner> ownerList = lObjectMapper.readValue( response.getBody().asString(),
            new TypeReference<List<Owner>>() {
            } );

      if ( CollectionUtils.isNotEmpty( ownerList ) ) {
         return ownerList.get( 0 );
      } else {
         Assert.fail( "Owner not found for code " + ownerCode );
         return null;
      }
   }


   private static List<Inventory> constructInventoryListFromResponse( String body )
         throws JsonParseException, JsonMappingException, IOException {
      ObjectMapper lObjectMapper =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      List<Inventory> inventoryList =
            lObjectMapper.readValue( body, new TypeReference<List<Inventory>>() {
            } );
      return inventoryList;
   }


   private static Response create( int statusCode, Credentials credentials, Object inventory ) {
      String userName = credentials.getUserName();
      String password = credentials.getPassword();

      Response response = RestAssured.given().auth().preemptive().basic( userName, password )
            .accept( APPLICATION_JSON ).contentType( APPLICATION_JSON ).body( inventory ).expect()
            .statusCode( statusCode ).when().post( INVENTORY_API_PATH );
      return response;
   }


   private Response updateInventory( int statusCode, String id, Object inventory,
         Credentials security ) {
      String userName = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().auth().preemptive().basic( userName, password )
            .accept( APPLICATION_JSON ).contentType( APPLICATION_JSON ).body( inventory )
            .put( INVENTORY_API_PATH + "/" + id );

      return response;
   }


   private Response getById( int statusCode, Credentials credentials, String id ) {

      String userName = credentials.getUserName();
      String password = credentials.getPassword();

      Response response = RestAssured.given().auth().preemptive().basic( userName, password )
            .accept( APPLICATION_JSON ).contentType( APPLICATION_JSON ).expect()
            .statusCode( statusCode ).when().get( INVENTORY_API_PATH + "/" + id );

      return response;

   }

}
