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
 * This test file has tests for CheckSrvstoreSupplierLocation.qrx intra airport warehouse level
 * supplier locations
 */

@RunWith( BlockJUnit4ClassRunner.class )
public class CheckSrvstoreSupplierLocationTest {

   @Rule
   public final DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();
   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static final String ATL_AIRPORT = "ATL";
   private static final String AMS_AIRPORT = "AMS";
   private static final String AMS_MAIN_WAREHOUSE = "AMS/MAINWAREHOUSE";
   private static final String ATL_MAIN_WAREHOUSE = "ATL/MAINWAREHOUSE";
   private static final String ATL_SRVSTORE = "ATL/SRVSTORE";
   private static final String ATL_SHOP_SRVSTORE = "ATL/SHOP/SRVSTORE";
   private static final String ATL_LINE_SRVSTORE = "ATL/LINE/SRVSTORE";
   private static final String ATL_LINE = "ATL/LINE";

   private LocationKey iATLSupplyLoc;
   private LocationKey iATLMainWarehouse;
   private LocationKey iAMSMainWarehouse;
   private LocationKey iATLLineSrvstore;
   private LocationKey iATLLineStore;


   /*
    * Data Setup: This will create set of serviceable stores an a non-serviceable store within same
    * supply location. some of the serviceable stores will be in a supplier tree. Also a second
    * supply location with serviceable stores
    */
   @Before
   public void setup() throws SQLException {

      // deleting all existing 0 level data in inv_loc table to avoid conflicts with test data
      MxDataAccess.getInstance().executeDelete( "inv_loc", null );

      iATLSupplyLoc = createSupplyLocation( ATL_AIRPORT, null );
      iATLMainWarehouse = createServiceableStore( iATLSupplyLoc, ATL_MAIN_WAREHOUSE, null );
      iATLLineSrvstore = createServiceableStore( iATLSupplyLoc, ATL_LINE_SRVSTORE, null );
      createServiceableStore( iATLSupplyLoc, ATL_SRVSTORE,
            createServiceableStore( iATLSupplyLoc, ATL_SHOP_SRVSTORE, iATLLineSrvstore ) );
      // create shop location under ATL_AIRPORT
      iATLLineStore = Domain.createLocation( loc -> {
         loc.setCode( ATL_LINE );
         loc.setType( RefLocTypeKey.LINE );
      } );

      // create another srvstore at another airport
      iAMSMainWarehouse = createServiceableStore( createSupplyLocation( AMS_AIRPORT, null ),
            AMS_MAIN_WAREHOUSE, null );
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
    * GIVEN ATL_LINE_SRVSTORE -> ATL_SHOP_SRVSTORE -> ATL_SRVSTORE ( A -> B means A is supplier for
    * B ), and ATL_LINE, if we assign AMS_MAIN_WAREHOUSE of AMS_AIRPORT as the supplier for
    * ATL_LINE_SRVSTORE, it should be invalid and no rows returned
    */

   @Test
   public void testAssignSrvstorLocationFromAnotherSupplyLocation() {

      getSupplierAndAssert( iATLSupplyLoc, iAMSMainWarehouse, 0 );

   }


   /*
    * GIVEN ATL_LINE_SRVSTORE -> ATL_SHOP_SRVSTORE -> ATL_SRVSTORE ( A -> B means A is supplier for
    * B ), and ATL_LINE, if we set ATL_LINE as the supplier for ATL_LINE_SRVSTORE, it should be
    * invalid and no rows returned
    */

   @Test
   public void testAssignSupplierLocationOtherThanSrvstore() {

      getSupplierAndAssert( iATLLineSrvstore, iATLLineStore, 0 );

   }


   /*
    * GIVEN ATL_LINE_SRVSTORE -> ATL_SHOP_SRVSTORE -> ATL_SRVSTORE ( A -> B means A is supplier for
    * B ), and ATL_LINE, if we set ATL_MAIN_WAREHOUSE as the supplier for ATL_LINE_SRVSTORE, it
    * should be valid
    */

   @Test
   public void testAssignSrvstoreSupplierLocationFromSameSupplyLocation() {

      getSupplierAndAssert( iATLLineSrvstore, iATLMainWarehouse, 1 );
   }


   private void getSupplierAndAssert( LocationKey aLocation, LocationKey aExpectedSupplier,
         int aExpectedRows ) {
      QuerySet lQs = checkValidSupplierLocations( aLocation, aExpectedSupplier );
      lQs.next();
      assertEquals( aExpectedRows, lQs.getRowCount() );
   }


   private QuerySet checkValidSupplierLocations( LocationKey aLocation,
         LocationKey aSupplierLocation ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aLocation, "aLocDbId", "aLocId" );
      lArgs.add( aSupplierLocation, "aSupplierLocDbId", "aSupplierLocId" );
      return QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            "com.mxi.mx.web.query.location.locationdetails.CheckSrvstoreSupplierLocation", lArgs );
   }
}
