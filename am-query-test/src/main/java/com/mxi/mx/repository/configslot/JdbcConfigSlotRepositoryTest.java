package com.mxi.mx.repository.configslot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.table.eqp.EqpAssmblBom;
import com.mxi.mx.domain.configslot.ConfigSlot;


public class JdbcConfigSlotRepositoryTest {

   private static final int NUM_SUBASSY_SLOTS = 1;
   private static final int NUM_SYS_SLOTS = 6;
   private static final int NUM_TRK_SLOTS = 5;
   private static final String ASSEMBLY_CODE = "ACFT1";
   private static final String CONFIG_SLOT_CODE = "20-00-00";

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   // Object under test
   private ConfigSlotRepository iRepository;

   // Test data
   private static final InventoryKey HIGHEST_INVENTORY_KEY = new InventoryKey( "4650:1234" );
   private static final ConfigSlotKey SUBASSY_KEY = new ConfigSlotKey( "4650:ACFT1:7" );


   @Before
   public void setUp() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );

      iRepository = new JdbcConfigSlotRepository();
   }


   @Test( expected = NullPointerException.class )
   public void find_byKey_null() {
      iRepository.find( ( ConfigSlotKey ) null );
   }


   @Test
   public void find_byKey_notFound() {
      Optional<ConfigSlot> lConfigSlot = iRepository.find( new ConfigSlotKey( "4650:FAKE:99999" ) );
      assertFalse( lConfigSlot.isPresent() );
   }


   @Test
   public void find_byKey() {
      Optional<ConfigSlot> lConfigSlot = iRepository.find( SUBASSY_KEY );

      assertTrue( lConfigSlot.isPresent() );
      assertEquals( SUBASSY_KEY, lConfigSlot.get().getKey() );
   }


   @Test( expected = NullPointerException.class )
   public void find_byAlternateKey_null() {
      iRepository.find( ( UUID ) null );
   }


   @Test
   public void find_byAlternateKey_notFound() {
      Optional<ConfigSlot> lConfigSlot = iRepository.find( UUID.randomUUID() );

      assertFalse( lConfigSlot.isPresent() );
   }


   @Test
   public void find_byAlternateKey() {
      UUID lAlternateKey = EqpAssmblBom.findByPrimaryKey( SUBASSY_KEY ).getAlternateKey();

      Optional<ConfigSlot> lConfigSlot = iRepository.find( lAlternateKey );

      assertTrue( lConfigSlot.isPresent() );
      assertEquals( SUBASSY_KEY, lConfigSlot.get().getKey() );
   }


   @Test( expected = NullPointerException.class )
   public void search_byHighestInvAndBOMClasses_nullInv() {
      iRepository.search( null, RefBOMClassKey.SYS );
   }


   @Test( expected = NullPointerException.class )
   public void search_byHighestInvAndBOMClasses_nullBOMClasses() {
      iRepository.search( HIGHEST_INVENTORY_KEY, null );
   }


   @Test
   public void search_byHighestInvAndBOMClasses_includesRootIfClassMatches() {
      List<ConfigSlot> lConfigSlots =
            iRepository.search( HIGHEST_INVENTORY_KEY, RefBOMClassKey.ROOT );

      assertEquals( 1, lConfigSlots.size() );
   }


   @Test
   public void search_byHighestInvAndBOMClasses_multipleClasses() {
      List<ConfigSlot> lConfigSlots =
            iRepository.search( HIGHEST_INVENTORY_KEY, RefBOMClassKey.SYS, RefBOMClassKey.TRK );
      assertEquals( NUM_SYS_SLOTS + NUM_TRK_SLOTS, lConfigSlots.size() );
   }


   @Test
   public void search_byHighestInvAndBOMClasses_correctData() {
      List<ConfigSlot> lConfigSlots =
            iRepository.search( HIGHEST_INVENTORY_KEY, RefBOMClassKey.SUBASSY );

      assertEquals( NUM_SUBASSY_SLOTS, lConfigSlots.size() );
      assertEquals( SUBASSY_KEY, lConfigSlots.get( 0 ).getKey() );
   }


   @Test
   public void findByNaturalKey_nullAssemblyCode() {
      Optional<ConfigSlot> lConfigSlot = iRepository.findByNaturalKey( null, CONFIG_SLOT_CODE );
      assertFalse( lConfigSlot.isPresent() );
   }


   @Test
   public void findByNaturalKey_nullConfigSlotCode() {
      Optional<ConfigSlot> lConfigSlot = iRepository.findByNaturalKey( ASSEMBLY_CODE, null );
      assertFalse( lConfigSlot.isPresent() );
   }


   @Test
   public void findByNaturalKey_notFound() {
      Optional<ConfigSlot> lConfigSlot =
            iRepository.findByNaturalKey( "notexist", CONFIG_SLOT_CODE );
      assertFalse( lConfigSlot.isPresent() );
   }


   @Test
   public void findByNaturalKey() {
      Optional<ConfigSlot> lConfigSlot =
            iRepository.findByNaturalKey( ASSEMBLY_CODE, CONFIG_SLOT_CODE );
      assertTrue( lConfigSlot.isPresent() );
      assertEquals( CONFIG_SLOT_CODE, lConfigSlot.get().getATACode() );
   }
}
