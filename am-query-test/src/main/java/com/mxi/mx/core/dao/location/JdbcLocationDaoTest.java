package com.mxi.mx.core.dao.location;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.domain.location.Location;


public class JdbcLocationDaoTest {

   private static final InventoryKey AIRCRAFT_INVENTORY_KEY = new InventoryKey( 4650, 1000 );
   private static final InventoryKey AIRCRAFT_INVENTORY_KEY_OPERATOR_NOT_MATCHING =
         new InventoryKey( 4650, 1001 );

   // Locations

   // Airport
   private static final LocationKey AIRPORT_SUPPLY_LOCATION = new LocationKey( 4650, 9 );
   private static final LocationKey AIRPORT_NOT_SUPPLY_LOCATION = new LocationKey( 4650, 9 );
   private static final LocationKey AIRPORT_NO_CHILD_LOCATIONS = new LocationKey( 4650, 17 );
   private static final LocationKey AIRPORT_WITH_COMPANY_LOCATION_KEY = new LocationKey( 4650, 21 );
   private static final Location AIRPORT_LOCATION_ROOT = Location.builder()
         .setKey( new LocationKey( "4650:0" ) ).setLocationTypeKey( RefLocTypeKey.AIRPORT ).build();
   private static final Location AIRPORT_LOCATION = Location.builder()
         .setKey( new LocationKey( "4650:1" ) ).setLocationTypeKey( RefLocTypeKey.AIRPORT ).build();

   // Companies
   private static final LocationKey COMPANY_LOCATION_KEY_MATCH_ORG = new LocationKey( 4650, 25 );
   private static final LocationKey COMPANY_LOCATION_KEY_NOT_MATCHING_ORG =
         new LocationKey( 4650, 23 );

   // Lines
   private static final LocationKey LINE_LOCATION = new LocationKey( 4650, 10 );
   private static final LocationKey EXISTING_LINE_LOCATION_KEY = new LocationKey( 4650, 12 );
   private static final LocationKey EXISTING_TRACK_LOCATION_KEY = new LocationKey( 4650, 14 );
   private static final LocationKey NON_EXISTENT_LOCATION = new LocationKey( 4650, 1234 );
   private static final Location LINE_1_LOCATION = Location.builder()
         .setKey( new LocationKey( "4650:2" ) ).setLocationTypeKey( RefLocTypeKey.LINE ).build();
   private static final Location LINE_2_LOCATION = Location.builder()
         .setKey( new LocationKey( "4650:3" ) ).setLocationTypeKey( RefLocTypeKey.LINE ).build();
   private static final Location LINE_3_LOCATION = Location.builder()
         .setKey( new LocationKey( "4650:4" ) ).setLocationTypeKey( RefLocTypeKey.LINE ).build();

   // Hanger
   private static final Location HANGAR_LOCATION = Location.builder()
         .setKey( new LocationKey( "4650:5" ) ).setLocationTypeKey( RefLocTypeKey.HGR ).build();

   // Tracks
   private static final Location TRACK_1_LOCATION = Location.builder()
         .setKey( new LocationKey( "4650:6" ) ).setLocationTypeKey( RefLocTypeKey.TRACK ).build();
   private static final Location TRACK_2_LOCATION = Location.builder()
         .setKey( new LocationKey( "4650:7" ) ).setLocationTypeKey( RefLocTypeKey.TRACK ).build();
   private static final Location TRACK_3_LOCATION = Location.builder()
         .setKey( new LocationKey( "4650:8" ) ).setLocationTypeKey( RefLocTypeKey.TRACK ).build();

   private static final LocationKey INVALID_LOCATION_KEY = new LocationKey( "4650:9000" );

   private LocationDao locationDao;

   @ClassRule
   public static DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule fakeGuiceDaoRule = new InjectionOverrideRule();


   @BeforeClass
   public static void setUpData() {
      DataLoaders.load( databaseConnectionRule.getConnection(), JdbcLocationDaoTest.class );
   }


   @Before
   public void setUp() {
      locationDao = new JdbcLocationDao();
   }


   @Test( expected = NullPointerException.class )
   public void findChildrenByTypes_nullArgs() {
      locationDao.findChildrenByTypes( null, ( RefLocTypeKey[] ) null );
   }


   @Test( expected = NullPointerException.class )
   public void findChildrenByTypes_nullLocationKey() {
      locationDao.findChildrenByTypes( null, RefLocTypeKey.TRACK );
   }


   @Test( expected = NullPointerException.class )
   public void findChildrenByTypes_nullTypeKeys() {
      locationDao.findChildrenByTypes( AIRPORT_LOCATION.getKey(), ( RefLocTypeKey[] ) null );
   }


   @Test( expected = IllegalArgumentException.class )
   public void findChildrenByTypes_emptyTypeKeys() {
      locationDao.findChildrenByTypes( AIRPORT_LOCATION.getKey() );
   }


   @Test
   public void findChildrenByTypes_multiLevel() {
      List<Location> lFindChildrenByTypes = locationDao.findChildrenByTypes(
            AIRPORT_LOCATION.getKey(), RefLocTypeKey.TRACK, RefLocTypeKey.LINE );

      assertEquals( 6, lFindChildrenByTypes.size() );
      assertTrue( lFindChildrenByTypes.containsAll( Arrays.asList( LINE_1_LOCATION, LINE_2_LOCATION,
            LINE_3_LOCATION, TRACK_1_LOCATION, TRACK_2_LOCATION, TRACK_3_LOCATION ) ) );
   }


   @Test
   public void findChildrenByTypes_onlySearchesBelowStartLocation() {
      List<Location> lFindChildrenByTypes = locationDao.findChildrenByTypes(
            HANGAR_LOCATION.getKey(), RefLocTypeKey.TRACK, RefLocTypeKey.LINE );

      assertEquals( 3, lFindChildrenByTypes.size() );
      assertTrue( lFindChildrenByTypes
            .containsAll( Arrays.asList( TRACK_1_LOCATION, TRACK_2_LOCATION, TRACK_3_LOCATION ) ) );
   }


   @Test
   public void findChildrenByTypes_noMatches() {
      List<Location> lFindChildrenByTypes =
            locationDao.findChildrenByTypes( AIRPORT_LOCATION.getKey(), RefLocTypeKey.VENDOR );

      assertTrue( lFindChildrenByTypes.isEmpty() );
   }


   @Test
   public void findAncestorByType_noAncestors() {
      Optional<Location> lLocation =
            locationDao.findAncestorByType( AIRPORT_LOCATION_ROOT.getKey(), RefLocTypeKey.AIRPORT );

      assertTrue( lLocation.isPresent() );
      assertEquals( AIRPORT_LOCATION_ROOT.getKey(), lLocation.get().getKey() );
   }


   @Test
   public void findAncestorByType_multipleAncestors() {
      Optional<Location> lLocation =
            locationDao.findAncestorByType( LINE_2_LOCATION.getKey(), RefLocTypeKey.AIRPORT );
      assertTrue( lLocation.isPresent() );
      assertEquals( AIRPORT_LOCATION.getKey(), lLocation.get().getKey() );
   }


   @Test
   public void findAncestorByType_airportLocation() {
      Optional<Location> lLocation =
            locationDao.findAncestorByType( AIRPORT_LOCATION.getKey(), RefLocTypeKey.AIRPORT );

      assertTrue( lLocation.isPresent() );
      assertEquals( AIRPORT_LOCATION.getKey(), lLocation.get().getKey() );
   }


   @Test( expected = NullPointerException.class )
   public void findAncestorByType_keyNull() {
      locationDao.findAncestorByType( null, RefLocTypeKey.LINE );
   }


   @Test( expected = NullPointerException.class )
   public void findAncestorByType_typeNull() {
      locationDao.findAncestorByType( AIRPORT_LOCATION.getKey(), null );
   }


   @Test( expected = NullPointerException.class )
   public void findAncestorByType_typeNullandKeyNull() {
      locationDao.findAncestorByType( null, null );
   }


   @Test
   public void findAncestorByType_invalidLocationKey() {
      Optional<Location> lLocation =
            locationDao.findAncestorByType( INVALID_LOCATION_KEY, RefLocTypeKey.AIRPORT );

      assertFalse( lLocation.isPresent() );
   }


   @Test( expected = NullPointerException.class )
   public void findLocationForWorkPackage_airportLocationKeyNull() {
      locationDao.findLocationForWorkPackage( null, EXISTING_LINE_LOCATION_KEY );
   }


   @Test
   public void findLocationForWorkPackage_latestLocationFound() {
      Optional<LocationKey> lineLocation = locationDao
            .findLocationForWorkPackage( AIRPORT_SUPPLY_LOCATION, AIRPORT_SUPPLY_LOCATION );

      assertTrue( lineLocation.isPresent() );
      assertEquals( LINE_LOCATION, lineLocation.get() );
   }


   @Test
   public void findLocationForWorkPackage_existingLineLocationFound() {
      Optional<LocationKey> existingLineLocation = locationDao
            .findLocationForWorkPackage( AIRPORT_SUPPLY_LOCATION, EXISTING_LINE_LOCATION_KEY );

      assertTrue( existingLineLocation.isPresent() );
      assertEquals( EXISTING_LINE_LOCATION_KEY, existingLineLocation.get() );
   }


   @Test
   public void findLocationForWorkPackage_existingTrackLocationFound() {
      Optional<LocationKey> existingTrackLocation = locationDao
            .findLocationForWorkPackage( AIRPORT_SUPPLY_LOCATION, EXISTING_TRACK_LOCATION_KEY );

      assertTrue( existingTrackLocation.isPresent() );
      assertEquals( EXISTING_TRACK_LOCATION_KEY, existingTrackLocation.get() );
   }


   @Test
   public void findLocationForWorkPackage_airportNotMarkedAsSupplyLocation() {
      Optional<LocationKey> lineLocation = locationDao
            .findLocationForWorkPackage( AIRPORT_LOCATION.getKey(), EXISTING_LINE_LOCATION_KEY );

      assertFalse( lineLocation.isPresent() );
   }


   @Test
   public void findLocationForWorkPackage_noLineOrTrackLocationsFound() {
      Optional<LocationKey> noLocationFound =
            locationDao.findLocationForWorkPackage( AIRPORT_NO_CHILD_LOCATIONS, null );

      assertFalse( noLocationFound.isPresent() );
   }


   @Test
   public void findLocationForWorkPackage_invalidAirportLocationKey() {
      Optional<LocationKey> invalidAirportLocationKey =
            locationDao.findLocationForWorkPackage( INVALID_LOCATION_KEY, null );

      assertFalse( invalidAirportLocationKey.isPresent() );
   }


   @Test( expected = NullPointerException.class )
   public void findCompanyLocationByAircraftOperator_airportKeyCanNotBeNull() {
      locationDao.findCompanyLocationByAircraftOperator( null, AIRCRAFT_INVENTORY_KEY );
   }


   @Test( expected = NullPointerException.class )
   public void findCompanyLocationByAircraftOperator_aircraftInventoryKeyCanNotBeNull() {
      locationDao.findCompanyLocationByAircraftOperator( AIRPORT_NOT_SUPPLY_LOCATION, null );
   }


   @Test
   public void findCompanyLocationByAircraftOperator_NoCompanyLocationsFound() {
      Optional<LocationKey> companyLocation = locationDao.findCompanyLocationByAircraftOperator(
            AIRPORT_LOCATION.getKey(), AIRCRAFT_INVENTORY_KEY );

      assertFalse( companyLocation.isPresent() );
   }


   @Test
   public void findCompanyLocationByAircraftOperator_noCompanyMarkedAsSupplyLocation() {
      Optional<LocationKey> companyLocation = locationDao.findCompanyLocationByAircraftOperator(
            AIRPORT_NOT_SUPPLY_LOCATION, AIRCRAFT_INVENTORY_KEY );

      assertFalse( companyLocation.isPresent() );
   }


   @Test
   public void findCompanyLocationByAircraftOperator_CompanyOrgMatchOperatorOrg() {
      Optional<LocationKey> companyLocation = locationDao.findCompanyLocationByAircraftOperator(
            AIRPORT_WITH_COMPANY_LOCATION_KEY, AIRCRAFT_INVENTORY_KEY );

      assertTrue( companyLocation.isPresent() );
      assertEquals( COMPANY_LOCATION_KEY_MATCH_ORG, companyLocation.get() );
   }


   @Test
   public void
         findCompanyLocationByAircraftOperator_CompanyOrgDoesNotMatchOperatorOrg_firstCompanyReturned() {
      Optional<LocationKey> companyLocation = locationDao.findCompanyLocationByAircraftOperator(
            AIRPORT_WITH_COMPANY_LOCATION_KEY, AIRCRAFT_INVENTORY_KEY_OPERATOR_NOT_MATCHING );

      assertTrue( companyLocation.isPresent() );
      assertEquals( COMPANY_LOCATION_KEY_NOT_MATCHING_ORG, companyLocation.get() );
   }


   @Test( expected = NullPointerException.class )
   public void find_byLocationKey_null() {
      locationDao.find( null );
   }


   @Test
   public void find_byLocationKey_found() {
      Location location = locationDao.find( LINE_LOCATION );

      assertNotNull( location );
      assertEquals( LINE_LOCATION, location.getKey() );
   }

}
