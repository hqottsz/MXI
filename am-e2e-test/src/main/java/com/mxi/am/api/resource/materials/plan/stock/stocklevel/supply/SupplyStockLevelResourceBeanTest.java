package com.mxi.am.api.resource.materials.plan.stock.stocklevel.supply;

import static com.mxi.mx.core.key.RefStockLowActionKey.MANUAL;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response.Status;

import org.junit.Assert;
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
import com.mxi.am.driver.common.database.DatabaseDriver;
import com.mxi.am.driver.common.database.Result;
import com.mxi.am.guice.AssetManagementDatabaseDriverProvider;
import com.mxi.am.helper.api.GenericAmApiCalls;

import io.restassured.RestAssured;
import io.restassured.response.Response;


/**
 * SupplyStockLevel API test
 *
 */
public class SupplyStockLevelResourceBeanTest {

   private static final String APPLICATION_JSON = "application/json";
   private static final String STOCK_1_CD = "API Get Supply Stock Level";
   private static final String STOCK_2_CD = "API Update Supply Stock Level";
   private static final String LOCATION_1_CD = "10001";

   private static DatabaseDriver driver;
   private static SupplyStockLevelPathBuilder supplyStockLevelPathBuilder;
   private static ObjectMapper objectMapper =
         ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();

   private static String stockId1;
   private static String stockId2;
   private static Location location1;


   @BeforeClass
   public static void setUpClass() throws Exception {
      try {
         RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
         RestAssured.baseURI = "http://".concat( InetAddress.getLocalHost().getHostName() );
         RestAssured.port = 80;
      } catch ( UnknownHostException e ) {
         throw new RuntimeException( "Cannot find local host name." );
      }
      driver = new AssetManagementDatabaseDriverProvider().get();
      supplyStockLevelPathBuilder = new SupplyStockLevel().pathBuilder();
      setUpData();
   }


   private static void setUpData() throws Exception {
      stockId1 = getStockId( STOCK_1_CD );
      stockId2 = getStockId( STOCK_2_CD );
      location1 = getLocation( LOCATION_1_CD );

      // test data: create supply stock level under the stock with stockId1
      GenericAmApiCalls.update( supplyStockLevelPathBuilder, stockId1,
            buildSupplyStockLevelList( stockId1, location1 ), 200, Credentials.AUTHENTICATED,
            APPLICATION_JSON );
   }


   @Test
   public void testGetListSuccessReturns200()
         throws JsonParseException, JsonMappingException, IOException {

      Response response = GenericAmApiCalls.get( supplyStockLevelPathBuilder, stockId1, 200,
            Credentials.AUTHENTICATED, APPLICATION_JSON );
      List<SupplyStockLevel> supplyStockLevelList = extractStockLevels( response );

      assertSupplyStockLevelList( supplyStockLevelList, stockId1, location1 );
   }


   @Test
   public void testGetUnauthorizedReturns403()
         throws JsonParseException, JsonMappingException, IOException {
      GenericAmApiCalls.get( supplyStockLevelPathBuilder, stockId1, 403, Credentials.UNAUTHORIZED,
            APPLICATION_JSON );
   }


   @CSIContractTest( Project.EXT_MATERIAL_PLANNER )
   @Test
   public void testSearchSuccessReturns200()
         throws JsonParseException, JsonMappingException, IOException {
      SupplyStockLevelSearchParameters parms = new SupplyStockLevelSearchParameters();
      parms.setStockIds( Arrays.asList( stockId1 ) );
      Response response = GenericAmApiCalls.search( supplyStockLevelPathBuilder, parms, 200,
            Credentials.AUTHENTICATED, APPLICATION_JSON );

      List<SupplyStockLevel> supplyStockLevelList = extractStockLevels( response );
      assertSupplyStockLevelList( supplyStockLevelList, stockId1, location1 );
   }


   @Test
   public void testSearchUnauthorizedReturns403()
         throws JsonParseException, JsonMappingException, IOException {
      GenericAmApiCalls.search( supplyStockLevelPathBuilder, null, 403, Credentials.UNAUTHORIZED,
            APPLICATION_JSON );
   }


   @CSIContractTest( Project.EXT_MATERIAL_PLANNER )
   @Test
   public void testUpdateSupplyStockLevelListSuccessReturns200() throws Exception {
      Response response = GenericAmApiCalls.update( supplyStockLevelPathBuilder, stockId2,
            buildSupplyStockLevelList( stockId2, location1 ), 200, Credentials.AUTHENTICATED,
            APPLICATION_JSON );

      List<SupplyStockLevel> supplyStockLevelList = extractStockLevels( response );
      assertSupplyStockLevelList( supplyStockLevelList, stockId2, location1 );
   }


   @Test
   public void testUpdateUnauthorizedReturns403()
         throws JsonParseException, JsonMappingException, IOException {
      GenericAmApiCalls.update( supplyStockLevelPathBuilder, stockId2,
            buildSupplyStockLevelList( stockId2, location1 ), 403, Credentials.UNAUTHORIZED,
            APPLICATION_JSON );
   }


   private void assertSupplyStockLevelList( List<SupplyStockLevel> supplyStockLevelList,
         String stockId, Location location ) {
      Assert.assertNotNull( "Expected a list of supply stock levels", supplyStockLevelList );
      Assert.assertEquals( "Number of supply stock levels", 1, supplyStockLevelList.size() );
      Assert.assertEquals( "StocK ID", stockId, supplyStockLevelList.get( 0 ).getStockId() );
      Assert.assertEquals( "Location ID", location.getId(),
            supplyStockLevelList.get( 0 ).getLocationId() );
      Assert.assertEquals( "Max Level", new Integer( 0 ),
            supplyStockLevelList.get( 0 ).getMaxLevel() );
      Assert.assertEquals( "Safety Level", new Integer( 0 ),
            supplyStockLevelList.get( 0 ).getSafetyLevel() );
      Assert.assertEquals( "Restock Level", new Integer( 0 ),
            supplyStockLevelList.get( 0 ).getRestockLevel() );
      Assert.assertEquals( "Stock Low Action", MANUAL.getCd(),
            supplyStockLevelList.get( 0 ).getStockLowAction() );
   }


   private static List<SupplyStockLevel> buildSupplyStockLevelList( String stockId,
         Location location ) throws JsonParseException, JsonMappingException, IOException {
      List<SupplyStockLevel> supplyStockLevelList = new ArrayList<SupplyStockLevel>();
      SupplyStockLevel supplyStockLevel = new SupplyStockLevel();
      supplyStockLevel.setStockId( stockId );
      supplyStockLevel.setLocationId( location.getId() );
      supplyStockLevelList.add( supplyStockLevel );
      return supplyStockLevelList;
   }


   private static String getStockId( String stockCd ) throws ClassNotFoundException, SQLException {
      Result stockResult =
            driver.select( "select * from eqp_stock_no where stock_no_cd =?", stockCd );
      if ( stockResult.isEmpty() ) {
         Assert.fail( "Could not find Stock ID with Stock No Code: " + stockCd );
      }
      return stockResult.get( 0 ).getUuidString( "alt_id" );
   }


   private static Location getLocation( String locationCode )
         throws JsonParseException, JsonMappingException, IOException {
      Map<String, String> searchParams = new HashMap<String, String>();
      searchParams.put( LocationSearchParameters.PARAM_CODE, locationCode );

      Response locationResponse = GenericAmApiCalls.search( Status.OK.getStatusCode(),
            Credentials.AUTHENTICATED, Location.PATH, searchParams, APPLICATION_JSON );
      List<Location> locationList = objectMapper.readValue( locationResponse.getBody().asString(),
            new TypeReference<List<Location>>() {
               // location list
            } );

      return locationList.get( 0 );
   }


   private List<SupplyStockLevel> extractStockLevels( Response response )
         throws JsonParseException, JsonMappingException, IOException {
      return objectMapper.readValue( response.getBody().asString(),
            new TypeReference<List<SupplyStockLevel>>() {
               // stock level list
            } );
   }

}
