package com.mxi.mx.core.services.location;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.OwnerDomainBuilder;
import com.mxi.am.domain.builder.StockBuilder;
import com.mxi.am.domain.builder.StockLevelBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.InvLocStockKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.StockNoKey;


/**
 *
 * Test validations when trying to change locations. Location changes include location removal,
 * location move, and unmark as supply
 *
 */
public class LocationChangeExceptionTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   private LocationKey iAirport;
   private LocationKey iAirportWithSupplier;
   private LocationKey iMainWarehouse;
   private LocationKey iLineStore;
   private StockNoKey iStockNo;
   private OwnerKey iOwner;
   private InvLocStockKey iStockLevel;


   @Before
   public void loadData() throws Exception {

      // create the airport
      String AIRPORT = "airport";
      LocationKey HUB = null;
      iAirport = createSupplyLocation( "airport", null );

      // create an airport with supplier location
      iAirportWithSupplier = createSupplyLocation( "airport with supplier", iAirport );

      // create main warehouse
      iMainWarehouse = createSrvstoreLocation( "main warehouse", null );

      // create line store and make main warehouse as it's supplier
      iLineStore = createSrvstoreLocation( "line store", iMainWarehouse );

      // create stock, owner, and warehouse stock level
      iStockNo = new StockBuilder().build();
      iOwner = new OwnerDomainBuilder().isDefault().build();
      iStockLevel = new StockLevelBuilder( iLineStore, iStockNo, iOwner ).build();

   }


   /**
    * Create SRVSTORE location
    *
    * @param aName
    * @param aHubLoc
    * @return
    */
   private LocationKey createSrvstoreLocation( String aName, LocationKey aHubLoc ) {
      return Domain.createLocation( location -> {
         location.setType( RefLocTypeKey.SRVSTORE );
         location.setCode( aName );
         location.setSupplyLocation( iAirport );
         location.setHubLocation( aHubLoc );
      } );
   }


   /**
    * Create supply location
    *
    * @param aName
    * @param aHubLoc
    * @return
    */
   private LocationKey createSupplyLocation( String aName, LocationKey aHubLoc ) {
      return Domain.createLocation( location -> {
         location.setType( RefLocTypeKey.AIRPORT );
         location.setCode( aName );
         location.setIsSupplyLocation( true );
         location.setHubLocation( aHubLoc );
      } );
   }


   /*
    * Test that SRVSTORE location with supplier location will fail the location removal validation
    */
   @Test( expected = LocationIsUsedInWarehouseStockLevelException.class )
   public void testRemovalOfSrvsgtoreLocationWithSupplier() throws Exception {

      LocationIsUsedInWarehouseStockLevelException.validateLocationDeletion( iLineStore );
   }


   /*
    * Test that supplier location for SRVSTORE location will fail the location removal validation
    */
   @Test( expected = LocationIsUsedInWarehouseStockLevelException.class )
   public void testRemovalOfSupplierLocationForWarehouseStockLevel() throws Exception {

      LocationIsUsedInWarehouseStockLevelException
            .validateLocationDeletion( iMainWarehouse );
   }


   /**
    * Test that SRVSTORE location with supplier location will fail the location move validation
    *
    * @throws Exception
    */
   @Test( expected = LocationIsUsedInWarehouseStockLevelException.class )
   public void testAssignParentLocationForWarehouseStockLevelLocation() throws Exception {

      LocationIsUsedInWarehouseStockLevelException.validateLocationMove( iLineStore );
   }


   /**
    * Test that supplier location of a SRVSTORE location will fail the location move validation
    *
    * @throws Exception
    */
   @Test( expected = LocationIsUsedInWarehouseStockLevelException.class )
   public void testAssignParentLocationForSupplierLocationOfWarehouseStockLevel() throws Exception {

      LocationIsUsedInWarehouseStockLevelException.validateLocationMove( iMainWarehouse );
   }


   /**
    * Test that unmarking a supply location that has local supplier locations will throw exception
    *
    * @throws Exception
    */
   @Test( expected = SupplyHasSupplierLocationException.class )
   public void testCannotUnmarkAsSupplyLocationForAirportWithRemoteSupplierLocation()
         throws Exception {

      LocationService.unmarkAsSupplyLocation( iAirport );
   }


   /**
    * Test that the unmarking a supply location that has remote supplier location will throw
    * exception
    *
    * @throws Exception
    */
   @Test( expected = SupplyHasSupplierLocationException.class )
   public void testCannotUnmarkAsSupplyLocationForAirportWithLocalSupplierLocation()
         throws Exception {

      LocationService.unmarkAsSupplyLocation( iAirportWithSupplier );
   }

}
