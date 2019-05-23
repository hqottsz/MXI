package com.mxi.mx.core.query.location;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefLocTypeKey;


/**
 * This test file has tests for SupplierLocations.qrx inter airport supplier locations and warehouse
 * level supplier locations
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class SupplierLocationsTest {

   @Rule
   public final DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();
   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static final String ATL_AIRPORT = "ATL";
   private static final String YOW_AIRPORT = "YOW";
   private static final String DXB_AIRPORT = "DXB";
   private static final String AMS_AIRPORT = "AMS";
   private static final String ATL_MAIN_WAREHOUSE = "ATL/MAINWAREHOUSE";
   private static final String ATL_SRVSTORE = "ATL/SRVSTORE";
   private static final String ATL_SHOP_SRVSTORE = "ATL/SHOP/SRVSTORE";
   private static final String ATL_LINE_SRVSTORE = "ATL/LINE/SRVSTORE";
   // add one more

   private LocationKey iATLSupplyLoc;
   private LocationKey iAMSSupplyLoc;
   private LocationKey iATLMainWarehouse;
   private LocationKey iATLLineSrvstore;


   /*
    * Data Setup: This will create set of supply locations with supplier location tree and
    * independent supply location. Also under one supply location, there is set of serviceable
    * stores with supply tree and one independent supply location
    */
   @Before
   public void setup() throws SQLException {

      // deleting all existing 0 level data in inv_loc table to avoid conflicts with test data
      MxDataAccess.getInstance().executeDelete( "inv_loc", null );

      iATLSupplyLoc = createSupplyLocation( ATL_AIRPORT, null );
      createSupplyLocation( DXB_AIRPORT, createSupplyLocation( YOW_AIRPORT, iATLSupplyLoc ) );
      iAMSSupplyLoc = createSupplyLocation( AMS_AIRPORT, null );

      iATLMainWarehouse = createServiceableStore( iATLSupplyLoc, ATL_MAIN_WAREHOUSE, null );
      iATLLineSrvstore = createServiceableStore( iATLSupplyLoc, ATL_LINE_SRVSTORE, null );
      createServiceableStore( iATLSupplyLoc, ATL_SRVSTORE,
            createServiceableStore( iATLSupplyLoc, ATL_SHOP_SRVSTORE, iATLLineSrvstore ) );

   }


   private LocationKey createSupplyLocation( String aLocationCode, LocationKey aSupplierLocation ) {
      return Domain.createLocation( loc -> {
         loc.setCode( aLocationCode );
         loc.setType( RefLocTypeKey.AIRPORT );
         loc.setIsSupplyLocation( true );
         loc.setHubLocation( aSupplierLocation );
      } );
   }


   private LocationKey createServiceableStore( LocationKey aAirport, String aLocCode,
         LocationKey aSupplierLoc ) {
      return Domain.createLocation( loc -> {
         loc.setCode( aLocCode );
         loc.setType( RefLocTypeKey.SRVSTORE );
         loc.setSupplyLocation( aAirport );
         loc.setIsSupplyLocation( false );
         loc.setHubLocation( aSupplierLoc );
      } );
   }


   /*
    * GIVEN ATL -> YOW -> DXB ( A -> B means A is supplier for B ) and AMS, WHEN assign supplier to
    * ATL, THEN only AMS is returned by the query.
    */

   @Test
   public void testRemoteSupplierLocationForSupplyLocation() {

      getSupplierAndAssert( iATLSupplyLoc, iAMSSupplyLoc );

   }


   /*
    * GIVEN ATL_LINE_SRVSTORE -> ATL_SHOP_SRVSTORE -> ATL_SRVSTORE ( A -> B means A is supplier for
    * B ) and ATL_MAIN_WAREHOUSE when assign supplier to ATL_LINE_SRVSTORE THEN only
    * ATL_MAIN_WAREHOUSE returned by the query *
    */

   @Test
   public void testLocalSupplierLocationForWarehouseLocation() {

      getSupplierAndAssert( iATLLineSrvstore, iATLMainWarehouse );
   }


   private void getSupplierAndAssert( LocationKey aLocation, LocationKey aExpectedSupplier ) {
      QuerySet lQs = getSupplierLocations( aLocation );
      lQs.next();
      assertEquals( 1, lQs.getRowCount() );
      assertEquals( aExpectedSupplier, lQs.getKey( LocationKey.class, "location_key" ) );
   }


   private QuerySet getSupplierLocations( LocationKey aLocation ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aLocation, "aLocDbId", "aLocId" );
      return QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            "com.mxi.mx.web.query.location.locationdetails.SupplierLocations", lArgs );
   }
}
