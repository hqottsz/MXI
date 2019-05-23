package com.mxi.mx.core.services.location;

import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;

import java.util.List;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.QueryRow;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.key.TimeZoneKey;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.dao.location.BasicAirport;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.query.location.GetPreferredLocationsBySupplyOnColsTest.PreferenceMatcher;
import com.mxi.mx.core.query.location.GetPreferredLocationsBySupplyOnColsTest.SimpleLocation;
import com.mxi.mx.core.table.inv.InvLocTable;


/**
 *
 * Test the preferred location aspects of LocationService.
 *
 */
public class PreferredLocationsTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();


   /*
    * Test that I can set and retrieve inventory autoreservation preferences under a supply chain.
    */
   @SuppressWarnings( "unchecked" )
   @Test
   public void setAndRetrievePrefs() throws DifferentSupplyLocationsException {

      BasicAirport lBasicAirport = new BasicAirport.Builder().build();

      LocationService lService = new LocationService();

      lService.setPreferredLocations( lBasicAirport.iSouthHangar, lBasicAirport.iSouthWarehouse,
            lBasicAirport.iMainWarehouse );

      QuerySet lQuerySet =
            lService.getPreferredLocationsBySupplyLocation( lBasicAirport.iAirportLocation, true );

      SimpleLocation lLine = new SimpleLocation( BasicAirport.LINE_MAINTENANCE, "" );
      SimpleLocation lNorthHangar = new SimpleLocation( BasicAirport.NORTH_HANGER, "" );
      SimpleLocation lSouthHangar = new SimpleLocation( BasicAirport.SOUTH_HANGER, "" );
      SimpleLocation lLineStore = new SimpleLocation( BasicAirport.LINE_STORE, "" );
      SimpleLocation lNorthWarehouse = new SimpleLocation( BasicAirport.NORTH_WAREHOUSE, "" );
      SimpleLocation lSouthWarehouse = new SimpleLocation( BasicAirport.SOUTH_WAREHOUSE, "" );
      SimpleLocation lMainWarehouse = new SimpleLocation( BasicAirport.MAIN_WAREHOUSE, "" );

      List<? extends QueryRow> lRows = lQuerySet.getRows();
      Assert.assertThat( "Unexpected rows returned", lRows, containsInAnyOrder(
            PreferenceMatcher.hasPreference( lLine, lLineStore, lNorthWarehouse ),
            PreferenceMatcher.hasPreference( lNorthHangar, lNorthWarehouse, null ),
            PreferenceMatcher.hasPreference( lSouthHangar, lSouthWarehouse, lMainWarehouse ) ) );

   }


   /**
    * This tests the deletion of maintenance location that is in preferred mapping
    *
    * @throws Exception
    */
   @Test( expected = LocationHasPreferredMappingException.class )
   public void deleteMaintenanceLocation() throws Exception {

      BasicAirport lBasicAirport = new BasicAirport.Builder().build();
      // try to delete the maintenance location which has preferred mapping
      LocationService.delete( lBasicAirport.iSouthHangar );
   }


   /**
    * This tests the deletion of srvstore/materials location that is in preferred mapping
    *
    * @throws Exception
    */
   @Test( expected = LocationHasPreferredMappingException.class )
   public void deleteSrvstoreLocation() throws Exception {

      BasicAirport lBasicAirport = new BasicAirport.Builder().build();
      // try to delete the srvstore location which has preferred mapping
      LocationService.delete( lBasicAirport.iSouthWarehouse );
   }


   /*
    * Test for a null key passed into removePreferredLocations does not throw an exception.
    */
   @Test
   public void testCannotRemoveNullLocation() {

      LocationService lService = new LocationService();
      try {
         lService.removePreferredLocations( null );
      } catch ( Exception e ) {
         Assert.fail( "RemovePreferredLocations on Null failed" );
      }
   }


   /*
    * Test removal of preferred locations on a material location that has no preferences set
    * verifying that it does not complain.
    */
   @Test
   public void testCannotRemoveNoPreference() {

      LocationKey lSomeLocation = Domain.createLocation();

      LocationService lService = new LocationService();
      try {
         lService.removePreferredLocations( lSomeLocation );
      } catch ( Exception e ) {
         Assert.fail( "RemovePreferredLocations on location with no preferred location failed" );
      }
   }


   /*
    * Test removal of one preferred location.
    */
   @Test
   public void testCanRemoveOnePreference() {
      final String iAirportCode = "YOW";
      final String NORTH_HANGER = "NORTH HANGER";
      final String NORTH_WAREHOUSE = "NORTH WAREHOUSE";

      LocationKey lAirportLocation = new LocationDomainBuilder().withCode( iAirportCode )
            .withType( RefLocTypeKey.AIRPORT ).isSupplyLocation().build();

      LocationKey lNorthWarehouse =
            new LocationDomainBuilder().withCode( NORTH_WAREHOUSE ).withType( RefLocTypeKey.SRVSTORE )
                  .withParent( lAirportLocation ).withSupplyLocation( lAirportLocation ).build();

      LocationKey lNorthHangar = new LocationDomainBuilder().withCode( NORTH_HANGER )
            .withType( RefLocTypeKey.HGR ).withParent( lAirportLocation )
            .withSupplyLocation( lAirportLocation ).withPrefLocations( lNorthWarehouse ).build();

      LocationService lService = new LocationService();

      QuerySet lQuerySet = lService.getPreferredLocationsBySupplyLocation( lAirportLocation, true );
      // Verifying that we have a preferred location set.
      Assert.assertTrue( "Expected at least one response", lQuerySet.hasNext() );

      lService.removePreferredLocations( lNorthHangar );

      lQuerySet = lService.getPreferredLocationsBySupplyLocation( lAirportLocation, true );
      // Preferred location should now be gone.
      Assert.assertFalse( "Expected zero rows", lQuerySet.hasNext() );
   }


   /*
    * Test removal of two preferred locations.
    */
   @Test
   public void testCanRemoveTwoPreferences() {
      final String iAirportCode = "YOW";
      final String NORTH_HANGER = "NORTH HANGER";
      final String NORTH_WAREHOUSE = "NORTH WAREHOUSE";
      final String SOUTH_WAREHOUSE = "SOUTH WAREHOUSE";

      LocationKey lAirportLocation = new LocationDomainBuilder().withCode( iAirportCode )
            .withType( RefLocTypeKey.AIRPORT ).isSupplyLocation().build();

      LocationKey lNorthWarehouse =
            new LocationDomainBuilder().withCode( NORTH_WAREHOUSE ).withType( RefLocTypeKey.SRVSTORE )
                  .withParent( lAirportLocation ).withSupplyLocation( lAirportLocation ).build();

      LocationKey lSouthWarehouse =
            new LocationDomainBuilder().withCode( SOUTH_WAREHOUSE ).withType( RefLocTypeKey.SRVSTORE )
                  .withParent( lAirportLocation ).withSupplyLocation( lAirportLocation ).build();

      LocationKey[] lLocationKeySet = { lNorthWarehouse, lSouthWarehouse };

      LocationKey lNorthHangar = new LocationDomainBuilder().withCode( NORTH_HANGER )
            .withType( RefLocTypeKey.HGR ).withParent( lAirportLocation )
            .withSupplyLocation( lAirportLocation ).withPrefLocations( lLocationKeySet ).build();

      LocationService lService = new LocationService();

      QuerySet lQuerySet = lService.getPreferredLocationsBySupplyLocation( lAirportLocation, true );
      // Verifying that we have preferred locations set.
      Assert.assertTrue( "Expected at least one response", lQuerySet.hasNext() );

      lService.removePreferredLocations( lNorthHangar );

      lQuerySet = lService.getPreferredLocationsBySupplyLocation( lAirportLocation, true );
      // Preferred locations should now be gone.
      Assert.assertFalse( "Expected zero rows", lQuerySet.hasNext() );
   }


   /**
    * This tests the assignment of parent location for a location that is NOT in preferred location
    * mapping
    *
    * @throws Exception
    */
   @Test
   public void assignParentLocationForNonPreferredLocation() throws Exception {

      final String TIME_ZONE = "America/New_York";

      LocationKey lParentLocation =
            new LocationDomainBuilder().withTimeZone( new TimeZoneKey( TIME_ZONE ) ).build();
      LocationKey lNonPreferredLocation = new LocationDomainBuilder().build();

      // try to assign parent location for the non-preferred location
      LocationService.assignParentLocation( lNonPreferredLocation, lParentLocation );

      // assert that the location is assigned parent location successfully
      InvLocTable lLocationTable = InvLocTable.findByPrimaryKey( lNonPreferredLocation );
      Assert.assertEquals( lLocationTable.getNHLocation(), lParentLocation );

   }


   /**
    * This tests the assignment of parent location for maintenance location that is in preferred
    * mapping
    *
    * @throws Exception
    */
   @Test( expected = LocationHasPreferredMappingException.class )
   public void assignParentLocationForMaintenanceLocation() throws Exception {

      LocationKey lParentLocation = new LocationDomainBuilder().build();
      BasicAirport lBasicAirport = new BasicAirport.Builder().build();
      LocationKey lHangerWithPrefLocation = lBasicAirport.iSouthHangar;

      // try to assign parent location for the maintenance location which has preferred mapping
      LocationService.assignParentLocation( lHangerWithPrefLocation, lParentLocation );
   }


   /**
    * This tests the assignment of parent location for srvstore/materials location that is in
    * preferred mapping
    *
    * @throws Exception
    */
   @Test( expected = LocationHasPreferredMappingException.class )
   public void assignParentLocationForSrvstoreLocation() throws Exception {

      LocationKey lParentLocation = new LocationDomainBuilder().build();
      BasicAirport lBasicAirport = new BasicAirport.Builder().build();
      LocationKey lPreferredSrvstore = lBasicAirport.iSouthWarehouse;

      // try to assign parent location for the srvstore location which has preferred mapping
      LocationService.assignParentLocation( lPreferredSrvstore, lParentLocation );
   }


   /**
    * This tests unmarking a supply location that has NO preferred locations
    *
    * @throws Exception
    */
   @Test
   public void unmarkAsSupplyLocationForAirportWithoutPrefLocaitons() throws Exception {

      final String AIRPORT = "AIRPORT";

      // the airport without preferred locations
      LocationKey lAirportWithoutPrefLocations = new LocationDomainBuilder().withCode( AIRPORT )
            .withType( RefLocTypeKey.AIRPORT ).isSupplyLocation().build();

      // try to unmark airport as supply location
      LocationService.unmarkAsSupplyLocation( lAirportWithoutPrefLocations );

      // assert that the airport is unmarked as supply location
      InvLocTable lLocationTable = InvLocTable.findByPrimaryKey( lAirportWithoutPrefLocations );
      Assert.assertFalse( lLocationTable.getSupplyBool() );
   }


   /**
    * This tests the unmarking a supply location that has preferred locations
    *
    * @throws Exception
    */
   @Test( expected = SupplyLocationHasPreferredLocationsException.class )
   public void unmarkAsSupplyLocationForAirportWithPrefLocaitons() throws Exception {

      // the airport has preferred locations
      BasicAirport lBasicAirport = new BasicAirport.Builder().build();
      LocationKey lAirportWithPrefLocations = lBasicAirport.iAirportLocation;

      // try to unmark airport as supply location
      LocationService.unmarkAsSupplyLocation( lAirportWithPrefLocations );
   }

}
