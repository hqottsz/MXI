package com.mxi.mx.repository.inventory.damage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.InventoryDamageKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.utils.uuid.UuidConverter;
import com.mxi.mx.domain.inventory.damage.InventoryDamage;


public class InventoryDamageRepositoryTest {

   private static final InventoryDamageKey EXISTING_INVENTORY_DAMAGE_KEY =
         new InventoryDamageKey( "1:4650" );
   private static final InventoryDamageKey NEW_INVENTORY_DAMAGE_KEY =
         new InventoryDamageKey( "2:4650" );
   private static final UUID EXISTING_ALT_ID =
         new UuidConverter().convertStringToUuid( "9E77BEA1D97811E7813BFB5E0E65793E" );
   private static final InventoryKey EXISTING_INVENTORY_KEY = new InventoryKey( "4650:100" );
   private static final InventoryKey NEW_INVENTORY_KEY = new InventoryKey( "4650:2121" );
   private static final FaultKey EXISTING_FAULT_KEY = new FaultKey( "4650:1111" );
   private static final String LOCATION_DESC = "left side";
   private static final String LOCATION_DESC_UPDATED = "right side";
   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   private InventoryDamageRepository iInventoryDamageRepository;


   @Before
   public void setUp() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
      iInventoryDamageRepository = new JdbcInventoryDamageRepository();
   }


   @Test( expected = NullPointerException.class )
   public void find_byInventoryKey_null() {
      InventoryDamageKey lInventoryDamageKey = null;
      iInventoryDamageRepository.find( lInventoryDamageKey );
   }


   @Test
   public void find_byInventoryDamageKey_existing() {

      Optional<InventoryDamage> lInventoryDamageOptional =
            iInventoryDamageRepository.find( EXISTING_INVENTORY_DAMAGE_KEY );

      assertTrue( lInventoryDamageOptional.isPresent() );

      InventoryDamage lInventoryDamage = lInventoryDamageOptional.get();

      assertEquals( EXISTING_INVENTORY_DAMAGE_KEY, lInventoryDamage.getKey() );
      assertEquals( EXISTING_ALT_ID, lInventoryDamage.getAltId() );
      assertEquals( EXISTING_INVENTORY_KEY, lInventoryDamage.getInventoryKey() );
      assertEquals( EXISTING_FAULT_KEY, lInventoryDamage.getFaultKey().get() );
      assertEquals( LOCATION_DESC, lInventoryDamage.getLocationDescription() );
   }


   @Test
   public void find_byInventoryDamageKey_nonExisting() {
      Optional<InventoryDamage> lInventoryDamageOptional =
            iInventoryDamageRepository.find( NEW_INVENTORY_DAMAGE_KEY );

      assertFalse( lInventoryDamageOptional.isPresent() );
   }


   @Test( expected = NullPointerException.class )
   public void find_byFaultKey_null() {
      FaultKey lFaultKey = null;
      iInventoryDamageRepository.find( lFaultKey );
   }


   @Test
   public void find_byFaultKey_existing() {

      Optional<InventoryDamage> lInventoryDamageOptional =
            iInventoryDamageRepository.find( EXISTING_FAULT_KEY );

      assertTrue( lInventoryDamageOptional.isPresent() );

      InventoryDamage lInventoryDamage = lInventoryDamageOptional.get();

      assertEquals( EXISTING_INVENTORY_DAMAGE_KEY, lInventoryDamage.getKey() );
      assertEquals( EXISTING_ALT_ID, lInventoryDamage.getAltId() );
      assertEquals( EXISTING_INVENTORY_KEY, lInventoryDamage.getInventoryKey() );
      assertEquals( EXISTING_FAULT_KEY, lInventoryDamage.getFaultKey().get() );
      assertEquals( LOCATION_DESC, lInventoryDamage.getLocationDescription() );
   }


   @Test
   public void find_byFaultKey_nonExisting() {
      Optional<InventoryDamage> lInventoryDamageOptional =
            iInventoryDamageRepository.find( new FaultKey( "4650:25" ) );

      assertFalse( lInventoryDamageOptional.isPresent() );
   }


   @Test( expected = NullPointerException.class )
   public void create_null() {
      iInventoryDamageRepository.create( null );
   }


   @Test
   public void create_new() {
      InventoryDamage lInventoryDamage =
            InventoryDamage.builder().key( null ).altId( null ).inventoryKey( NEW_INVENTORY_KEY )
                  .faultKey( null ).locationDescription( "one" ).build();

      InventoryDamageKey lInventoryDamageKey =
            iInventoryDamageRepository.create( lInventoryDamage );

      Optional<InventoryDamage> lInventoryDamageOptional =
            iInventoryDamageRepository.find( lInventoryDamageKey );

      assertTrue( lInventoryDamageOptional.isPresent() );

      InventoryDamage lRetrievedInventoryDamage = lInventoryDamageOptional.get();

      assertEquals( NEW_INVENTORY_KEY, lRetrievedInventoryDamage.getInventoryKey() );
      assertEquals( Optional.empty(), lRetrievedInventoryDamage.getFaultKey() );
      assertEquals( "one", lRetrievedInventoryDamage.getLocationDescription() );

   }


   @Test( expected = NullPointerException.class )
   public void update_null() {
      iInventoryDamageRepository.update( null );
   }


   @Test
   public void update_existing_damageLocation() {
      InventoryDamage lUpdatedInventoryDamage = InventoryDamage.builder()
            .key( EXISTING_INVENTORY_DAMAGE_KEY ).inventoryKey( NEW_INVENTORY_KEY )
            .faultKey( EXISTING_FAULT_KEY ).locationDescription( LOCATION_DESC_UPDATED ).build();

      iInventoryDamageRepository.update( lUpdatedInventoryDamage );

      Optional<InventoryDamage> lInventoryDamageOptional =
            iInventoryDamageRepository.find( EXISTING_INVENTORY_DAMAGE_KEY );

      assertEquals( LOCATION_DESC_UPDATED,
            lInventoryDamageOptional.get().getLocationDescription() );
      assertEquals( NEW_INVENTORY_KEY, lInventoryDamageOptional.get().getInventoryKey() );
   }


   @Test
   public void update_nonexistent_damageLocation() {
      InventoryDamage lUpdatedInventoryDamage = InventoryDamage.builder()
            .key( NEW_INVENTORY_DAMAGE_KEY ).inventoryKey( NEW_INVENTORY_KEY )
            .faultKey( EXISTING_FAULT_KEY ).locationDescription( LOCATION_DESC_UPDATED ).build();

      iInventoryDamageRepository.update( lUpdatedInventoryDamage );

      Optional<InventoryDamage> lInventoryDamageOptional =
            iInventoryDamageRepository.find( NEW_INVENTORY_DAMAGE_KEY );

      assertTrue( !lInventoryDamageOptional.isPresent() );
   }

}
