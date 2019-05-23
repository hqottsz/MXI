package com.mxi.am.api.resource.materials.plan.stock.stocklevel.warehouse;

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
 * WarehouseStockLevel API test
 *
 */
public class WarehouseStockLevelResourceBeanTest {

   private static final String STOCK_LOW_ACTION = "MANUAL";
   private static final String STOCK_CD1 = "API Get Warehouse Stock Level";
   private static final String STOCK_CD2 = "API Update Warehouse Stock Level";
   private static final String LOCATION_CD1 = "AIRPORT1/STORE";
   private static final String APPLICATION_JSON = "application/json";

   private static String stockId1;
   private static String stockId2;
   private static Location location1;
   private static ObjectMapper objectMapper =
         ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
   private static DatabaseDriver driver;
   private static WarehouseStockLevelPathBuilder warehouseStockLevelPathBuilder;


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
      setUpData();
      warehouseStockLevelPathBuilder = new WarehouseStockLevel().pathBuilder();

      // test data: create warehouse stock level under the stock with stockId1
      GenericAmApiCalls.update( warehouseStockLevelPathBuilder, stockId1,
            buildWarehouseStockLevelList( stockId1, location1 ), 200, Credentials.AUTHENTICATED,
            APPLICATION_JSON );
   }


   private static void setUpData() throws Exception {
      stockId1 = getStockId( STOCK_CD1 );
      stockId2 = getStockId( STOCK_CD2 );
      location1 = getLocation( LOCATION_CD1 );
   }


   private static String getStockId( String stockCd ) throws ClassNotFoundException, SQLException {
      Result stockResult =
            driver.select( "select * from eqp_stock_no where stock_no_cd =?", stockCd );
      if ( stockResult.isEmpty() ) {
         Assert.fail( "Could not find Stock ID with Stock No Code: " + stockCd );
      }
      return stockResult.get( 0 ).getUuidString( "alt_id" );
   }


   @Test
   public void testGetListSuccessReturns200()
         throws JsonParseException, JsonMappingException, IOException {

      Response response = GenericAmApiCalls.get( warehouseStockLevelPathBuilder, stockId1, 200,
            Credentials.AUTHENTICATED, APPLICATION_JSON );
      List<WarehouseStockLevel> warehouseStockLevelList = extractStockLevels( response );

      assertWarehouseStockLevelList( warehouseStockLevelList, stockId1, location1 );
   }


   @Test
   public void testGetUnauthenticatedReturns401()
         throws JsonParseException, JsonMappingException, IOException {
      GenericAmApiCalls.get( warehouseStockLevelPathBuilder, stockId1, 401,
            Credentials.UNAUTHENTICATED, APPLICATION_JSON );
   }


   @Test
   public void testGetUnauthorizedReturns403()
         throws JsonParseException, JsonMappingException, IOException {
      GenericAmApiCalls.get( warehouseStockLevelPathBuilder, stockId1, 403,
            Credentials.UNAUTHORIZED, APPLICATION_JSON );
   }


   @CSIContractTest( Project.EXT_MATERIAL_PLANNER )
   @Test
   public void testUpdateWarehouseStockLevelListSuccessReturns200() throws Exception {
      Response response = GenericAmApiCalls.update( warehouseStockLevelPathBuilder, stockId2,
            buildWarehouseStockLevelList( stockId2, location1 ), 200, Credentials.AUTHENTICATED,
            APPLICATION_JSON );

      List<WarehouseStockLevel> warehouseStockLevelList = extractStockLevels( response );
      assertWarehouseStockLevelList( warehouseStockLevelList, stockId2, location1 );
   }


   @Test
   public void testUpdateUnauthenticatedReturns401()
         throws JsonParseException, JsonMappingException, IOException {
      GenericAmApiCalls.update( warehouseStockLevelPathBuilder, stockId2,
            buildWarehouseStockLevelList( stockId2, location1 ), 401, Credentials.UNAUTHENTICATED,
            APPLICATION_JSON );
   }


   @Test
   public void testUpdateUnauthorizedReturns403()
         throws JsonParseException, JsonMappingException, IOException {
      GenericAmApiCalls.update( warehouseStockLevelPathBuilder, stockId2,
            buildWarehouseStockLevelList( stockId2, location1 ), 403, Credentials.UNAUTHORIZED,
            APPLICATION_JSON );
   }


   @CSIContractTest( Project.EXT_MATERIAL_PLANNER )
   @Test
   public void testSearchSuccessReturns200()
         throws JsonParseException, JsonMappingException, IOException {
      WarehouseStockLevelSearchParameters parms = new WarehouseStockLevelSearchParameters();
      parms.setStockIds( Arrays.asList( stockId1 ) );
      Response response = GenericAmApiCalls.search( warehouseStockLevelPathBuilder, parms, 200,
            Credentials.AUTHENTICATED, APPLICATION_JSON );

      List<WarehouseStockLevel> warehouseStockLevels = extractStockLevels( response );
      assertWarehouseStockLevelList( warehouseStockLevels, stockId1, location1 );
   }


   @Test
   public void testSearchUnauthenticatedReturns401()
         throws JsonParseException, JsonMappingException, IOException {
      GenericAmApiCalls.search( warehouseStockLevelPathBuilder, null, 401,
            Credentials.UNAUTHENTICATED, APPLICATION_JSON );
   }


   @Test
   public void testSearchUnauthorizedReturns403()
         throws JsonParseException, JsonMappingException, IOException {
      GenericAmApiCalls.search( warehouseStockLevelPathBuilder, null, 403, Credentials.UNAUTHORIZED,
            APPLICATION_JSON );
   }


   private void assertWarehouseStockLevelList( List<WarehouseStockLevel> warehouseStockLevelList,
         String stockId, Location location ) {
      Assert.assertNotNull( warehouseStockLevelList );
      Assert.assertEquals( 1, warehouseStockLevelList.size() );
      Assert.assertEquals( stockId, warehouseStockLevelList.get( 0 ).getStockId() );
      Assert.assertEquals( location.getId(), warehouseStockLevelList.get( 0 ).getLocationId() );
      Assert.assertEquals( new Integer( 1 ), warehouseStockLevelList.get( 0 ).getMaxLevel() );
      Assert.assertEquals( new Integer( 1 ), warehouseStockLevelList.get( 0 ).getSafetyLevel() );
      Assert.assertEquals( new Integer( 1 ), warehouseStockLevelList.get( 0 ).getRestockLevel() );
      Assert.assertEquals( STOCK_LOW_ACTION, warehouseStockLevelList.get( 0 ).getStockLowAction() );
   }


   private static List<WarehouseStockLevel> buildWarehouseStockLevelList( String stockId,
         Location location ) throws JsonParseException, JsonMappingException, IOException {
      List<WarehouseStockLevel> warehouseStockLevelList = new ArrayList<WarehouseStockLevel>();

      WarehouseStockLevel warehouseStockLevel = new WarehouseStockLevel();
      warehouseStockLevel.setStockId( stockId );
      warehouseStockLevel.setLocationId( location.getId() );
      warehouseStockLevel.setRestockLevel( 1 );
      warehouseStockLevel.setSafetyLevel( 1 );
      warehouseStockLevel.setMaxLevel( 1 );
      warehouseStockLevel.setStockLowAction( STOCK_LOW_ACTION );

      warehouseStockLevelList.add( warehouseStockLevel );

      return warehouseStockLevelList;
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


   public List<WarehouseStockLevel> extractStockLevels( Response response1 )
         throws JsonParseException, JsonMappingException, IOException {
      return objectMapper.readValue( response1.getBody().asString(),
            new TypeReference<List<WarehouseStockLevel>>() {
               // stock level list
            } );
   }

}
