package com.mxi.mx.repository.inventory.aircraft;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.core.key.CapabilityKey;
import com.mxi.mx.core.key.CapabilityLevelKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.domain.inventory.Inventory;
import com.mxi.mx.domain.inventory.aircraft.Aircraft;
import com.mxi.mx.domain.inventory.aircraft.Aircraft.AircraftCapabilityInfo;


public class JdbcAircraftRepositoryTest {

   private static final InventoryKey INVENTORY_KEY_AUTHORITY_1 = new InventoryKey( 4650, 1122 );
   private static final InventoryKey INVENTORY_KEY_AUTHORITY_2 = new InventoryKey( 4650, 1125 );
   private static final InventoryKey INVENTORY_KEY_NULL_AUTHORITY = new InventoryKey( 4650, 1123 );

   private static final UUID INVENTORY_ALT_ID_AUTHORITY_1 =
         UUID.fromString( "12345678-90AB-CDEF-1234-567890ABCDEF" );
   private static final UUID INVENTORY_ALT_ID_NULL_AUTHORITY =
         UUID.fromString( "32345678-90AB-CDEF-1234-567890ABCDEF" );
   private static final UUID INVENTORY_ALT_ID_AUTHORITY_2 =
         UUID.fromString( "42345678-90AB-CDEF-1234-567890ABCDEF" );
   private static final UUID INVENTORY_ALT_ID_AUTHORITY_3 =
         UUID.fromString( "52345678-90AB-CDEF-1234-567890ABCDEF" );
   private static final UUID INVENTORY_ALT_ID_FAKE =
         UUID.fromString( "12345678-90AB-CDEF-1234-999999999999" );

   private static final LocationKey LOCATION_KEY = new LocationKey( "4650:1" );
   private static final ConfigSlotPositionKey CONFIG_SLOT_POSITION_KEY =
         new ConfigSlotPositionKey( "4650:ACFT1:0:1" );

   private static final Inventory INVENTORY = Inventory.builder().key( INVENTORY_KEY_AUTHORITY_1 )
         .classKey( RefInvClassKey.ACFT ).altId( INVENTORY_ALT_ID_AUTHORITY_1 )
         .highestInventoryKey( INVENTORY_KEY_AUTHORITY_1 ).locationKey( LOCATION_KEY )
         .configSlotPositionKey( CONFIG_SLOT_POSITION_KEY ).build();
   private static final Set<AircraftCapabilityInfo> CAPABILITIES = new HashSet<>();
   {
      CapabilityLevelKey lCurrentCapability1 = new CapabilityLevelKey( "4650:cap_1_1:4650:cap_1" );
      CapabilityLevelKey lConfiguredCapability1 =
            new CapabilityLevelKey( "4650:cap_1_1:4650:cap_1" );
      AircraftCapabilityInfo lCapability1 = new AircraftCapabilityInfo(
            new CapabilityKey( "4650:cap_1" ), lCurrentCapability1, lConfiguredCapability1 );
      CapabilityLevelKey lCurrentCapability2 = new CapabilityLevelKey( "4650:cap_2_1:4650:cap_2" );
      CapabilityLevelKey lConfiguredCapability2 =
            new CapabilityLevelKey( "4650:cap_2_2:4650:cap_2" );
      AircraftCapabilityInfo lCapability2 = new AircraftCapabilityInfo(
            new CapabilityKey( "4650:cap_2" ), lCurrentCapability2, lConfiguredCapability2 );
      CapabilityLevelKey lCurrentCapability3 = new CapabilityLevelKey( "4650:cap_3_1:4650:cap_3" );
      CapabilityLevelKey lConfiguredCapability3 =
            new CapabilityLevelKey( "4650:cap_3_1:4650:cap_3" );
      AircraftCapabilityInfo lCapability3 = new AircraftCapabilityInfo(
            new CapabilityKey( "4650:cap_3" ), lCurrentCapability3, lConfiguredCapability3 );

      CAPABILITIES.add( lCapability1 );
      CAPABILITIES.add( lCapability2 );
      CAPABILITIES.add( lCapability3 );
   }

   private static final Aircraft AIRCRAFT =
         Aircraft.builder( INVENTORY ).capabilities( CAPABILITIES ).build();

   private static final HumanResourceKey USER_SINGLE_AUTHORITY = new HumanResourceKey( 4650, 20 );
   private static final HumanResourceKey USER_MULTIPLE_AUTHORITIES =
         new HumanResourceKey( 4650, 21 );
   private static final HumanResourceKey USER_ALL_AUTHORITIES = new HumanResourceKey( 4650, 22 );
   private static final HumanResourceKey USER_NO_AUTHORITIES = new HumanResourceKey( 4650, 23 );

   private AircraftRepository jdbcAircraftRepository;

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();


   @Before
   public void setUp() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
      jdbcAircraftRepository = new JdbcAircraftRepository();
   }


   @Test( expected = NullPointerException.class )
   public void findByKey_null() {
      jdbcAircraftRepository.findByKey( null );
   }


   @Test
   public void findByKey() {
      Optional<Aircraft> lOptionalAircraft =
            jdbcAircraftRepository.findByKey( INVENTORY_KEY_AUTHORITY_1 );

      assertEquals( AIRCRAFT, lOptionalAircraft.get() );
   }


   @Test
   public void findByKey_nonExisting() {
      Optional<Aircraft> lAircraft =
            jdbcAircraftRepository.findByKey( new InventoryKey( "4650:928" ) );

      assertFalse( lAircraft.isPresent() );
   }


   @Test( expected = NullPointerException.class )
   public void findByAlternateKey_null() {
      jdbcAircraftRepository.findByAlternateKey( null );
   }


   @Test
   public void findByAlternateKey() {
      Optional<Aircraft> lOptionalAircraft =
            jdbcAircraftRepository.findByAlternateKey( INVENTORY_ALT_ID_AUTHORITY_1 );

      assertEquals( AIRCRAFT, lOptionalAircraft.get() );
   }


   @Test
   public void findByAlternateKey_nonExisting() {
      Optional<Aircraft> lAircraft =
            jdbcAircraftRepository.findByAlternateKey( INVENTORY_ALT_ID_FAKE );

      assertFalse( lAircraft.isPresent() );
   }


   @Test( expected = NullPointerException.class )
   public void search_nullStatusKey() {
      jdbcAircraftRepository.search( null, USER_SINGLE_AUTHORITY );
   }


   @Test( expected = NullPointerException.class )
   public void search_nullHumanResourceKey() {
      jdbcAircraftRepository.search( Arrays.asList( new InventoryKey( "4621:123" ) ), null );
   }


   @Test
   public void search_aircraftExist() {

      List<Aircraft> aircraft = jdbcAircraftRepository.search(
            Arrays.asList( INVENTORY_KEY_AUTHORITY_1, INVENTORY_KEY_NULL_AUTHORITY ),
            USER_SINGLE_AUTHORITY );

      assertNotNull( aircraft );
      assertEquals( 2, aircraft.size() );
   }


   @Test
   public void search_aircraftExistWithMultipleGroupAssignments() {
      List<Aircraft> aircraft = jdbcAircraftRepository
            .search( Arrays.asList( INVENTORY_KEY_AUTHORITY_1 ), USER_SINGLE_AUTHORITY );

      assertNotNull( aircraft );
      assertEquals( 2, aircraft.get( 0 ).getGroupAssignments().size() );
   }


   @Test
   public void search_aircraftDoesNotExist() {

      List<Aircraft> aircraft = jdbcAircraftRepository
            .search( Arrays.asList( INVENTORY_KEY_AUTHORITY_2 ), USER_SINGLE_AUTHORITY );

      assertEquals( 0, aircraft.size() );
   }


   @Test( expected = NullPointerException.class )
   public void search_filterByAuthority_nullHumanResourceKey() {
      jdbcAircraftRepository.search( null );
   }


   /**
    * Should capture all aircraft except those whose condition is SCRAP or ARCHIVE.
    */
   @Test
   public void search_filterByAuthority_userHasAllAuthorities() {
      List<SearchAircraftTO> aircraftTOList = jdbcAircraftRepository.search( USER_ALL_AUTHORITIES );
      List<UUID> aircraftInventoryKeyList = getAircraftAltIds( aircraftTOList );

      assertEquals( 4, aircraftTOList.size() );
      assertTrue( aircraftInventoryKeyList.contains( INVENTORY_ALT_ID_AUTHORITY_1 ) );
      assertTrue( aircraftInventoryKeyList.contains( INVENTORY_ALT_ID_AUTHORITY_2 ) );
      assertTrue( aircraftInventoryKeyList.contains( INVENTORY_ALT_ID_AUTHORITY_3 ) );
      assertTrue( aircraftInventoryKeyList.contains( INVENTORY_ALT_ID_NULL_AUTHORITY ) );
   }


   /**
    * Should capture aircraft sharing the user's authority as well as aircraft with NULL authority
    * (i.e no need for authority check) except those whose condition is SCRAP or ARCHIVE.
    */
   @Test
   public void search_filterByAuthority_singleAuthority() {
      List<SearchAircraftTO> aircraftTOList =
            jdbcAircraftRepository.search( USER_SINGLE_AUTHORITY );
      List<UUID> aircraftInventoryKeyList = getAircraftAltIds( aircraftTOList );

      assertEquals( 2, aircraftTOList.size() );
      assertTrue( aircraftInventoryKeyList.contains( INVENTORY_ALT_ID_AUTHORITY_1 ) );
      assertTrue( aircraftInventoryKeyList.contains( INVENTORY_ALT_ID_NULL_AUTHORITY ) );
   }


   /**
    * Should capture aircraft sharing all of the user's authorities as well as aircraft with NULL
    * authority (i.e no need for authority check) except those whose condition is SCRAP or ARCHIVE.
    */
   @Test
   public void search_filterByAuthority_multipleAuthorities() {
      List<SearchAircraftTO> aircraftTOList =
            jdbcAircraftRepository.search( USER_MULTIPLE_AUTHORITIES );
      List<UUID> aircraftInventoryKeyList = getAircraftAltIds( aircraftTOList );

      assertEquals( 3, aircraftTOList.size() );
      assertTrue( aircraftInventoryKeyList.contains( INVENTORY_ALT_ID_AUTHORITY_2 ) );
      assertTrue( aircraftInventoryKeyList.contains( INVENTORY_ALT_ID_AUTHORITY_3 ) );
      assertTrue( aircraftInventoryKeyList.contains( INVENTORY_ALT_ID_NULL_AUTHORITY ) );
   }


   /**
    * Should only capture aircraft with NULL authority (i.e no need for authority check) except
    * those whose condition is SCRAP or ARCHIVE.
    */
   @Test
   public void search_filterByAuthority_noAuthorities() {
      List<SearchAircraftTO> aircraftTOList = jdbcAircraftRepository.search( USER_NO_AUTHORITIES );
      List<UUID> aircraftInventoryKeyList = getAircraftAltIds( aircraftTOList );

      assertEquals( 1, aircraftTOList.size() );
      assertTrue( aircraftInventoryKeyList.contains( INVENTORY_ALT_ID_NULL_AUTHORITY ) );
   }


   private List<UUID> getAircraftAltIds( List<SearchAircraftTO> aircraftList ) {
      List<UUID> aircraftInventoryKeyList = new ArrayList<>();
      {
         for ( SearchAircraftTO searchAircraftTO : aircraftList ) {
            aircraftInventoryKeyList.add( searchAircraftTO.getAltId() );
         }
      }

      return aircraftInventoryKeyList;
   }
}
