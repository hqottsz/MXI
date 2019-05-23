package com.mxi.mx.repository.inventory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.domain.inventory.Inventory;


@RunWith( BlockJUnit4ClassRunner.class )
public class JdbcInventoryRepositoryTest {

   private static final InventoryKey NON_EXISTENT_INVENTORY = new InventoryKey( 4650, 9999 );
   private static final InventoryKey INVENTORY_WITH_CHILD_INVENTORY =
         new InventoryKey( 4650, 1122 );
   private static final InventoryKey INVENTORY_WITHOUT_CHILD_INVENTORY =
         new InventoryKey( 4650, 1120 );
   private static final String INVENTORY_NAME = "10-20-30 - System Name";
   private static final LocationKey LOCATION = new LocationKey( "4650:1" );

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   private InventoryRepository iRepository;
   private InventoryKey iHighestInventoryKey;


   @Before
   public void setUp() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
      iRepository = new JdbcInventoryRepository();
      iHighestInventoryKey = createInventory( null, RefInvClassKey.ACFT );
   }


   @Test( expected = NullPointerException.class )
   public void find_byInventoryKey_null() {
      iRepository.find( ( InventoryKey ) null );
   }


   @Test
   public void find_byInventoryKey_notFound() {
      InventoryKey lNonExistingInventoryKey = NON_EXISTENT_INVENTORY;

      Optional<Inventory> lFoundInventory = iRepository.find( lNonExistingInventoryKey );
      assertFalse( lFoundInventory.isPresent() );
   }


   @Test
   public void find_byInventoryKey_found() {
      InventoryKey lInventory = createInventory( iHighestInventoryKey, RefInvClassKey.SYS );

      Optional<Inventory> lFoundInventory = iRepository.find( lInventory );
      assertTrue( lFoundInventory.isPresent() );
      assertEquals( lInventory, lFoundInventory.get().getKey() );
   }


   @Test( expected = NullPointerException.class )
   public void find_byAlternateKey_null() {
      iRepository.find( ( UUID ) null );
   }


   @Test
   public void find_byAlternateKey_notFound() {
      UUID lNonExistingInventoryAlternateKey = UUID.randomUUID();

      Optional<Inventory> lFoundInventory = iRepository.find( lNonExistingInventoryAlternateKey );
      assertFalse( lFoundInventory.isPresent() );
   }


   @Test
   public void find_byAlternateKey_found() {
      InventoryKey lInventory = createInventory( iHighestInventoryKey, RefInvClassKey.SYS );
      UUID lInventoryAlternateKey = InvInvTable.findByPrimaryKey( lInventory ).getAlternateKey();

      Optional<Inventory> lFoundInventory = iRepository.find( lInventoryAlternateKey );
      assertTrue( lFoundInventory.isPresent() );
      assertEquals( lInventory, lFoundInventory.get().getKey() );
   }


   /**
    * There are 3 child inventories of the test data aircraft: one TRK, one ASSY and one SYS. The
    * tests below expect that only the TRK and ASSY child inventories are returned.
    */
   @Test
   public void findChildInventoryRecords_childRecordsExist() {
      List<Inventory> childInventoryRecords =
            iRepository.findChildInventoryRecords( INVENTORY_WITH_CHILD_INVENTORY );

      assertEquals( 2, childInventoryRecords.size() );
   }


   @Test
   public void findChildInventoryRecords_noChildRecordsExist() {
      List<Inventory> childInventoryRecords =
            iRepository.findChildInventoryRecords( INVENTORY_WITHOUT_CHILD_INVENTORY );

      assertEquals( 0, childInventoryRecords.size() );
   }


   @Test
   public void findChildInventoryRecords_nonExistentInventory() {
      List<Inventory> childInventoryRecords =
            iRepository.findChildInventoryRecords( NON_EXISTENT_INVENTORY );

      assertEquals( 0, childInventoryRecords.size() );
   }


   @Test( expected = NullPointerException.class )
   public void findChildInventoryRecords_nullInventory() {
      iRepository.findChildInventoryRecords( null );
   }


   @Test( expected = NullPointerException.class )
   public void search_nullInventoryList() {
      iRepository.search( null );
   }


   @Test
   public void search_InventoryExists() {

      List<InventoryKey> invKeys = new ArrayList<>();
      InventoryKey inv1 = createInventory( iHighestInventoryKey, RefInvClassKey.SYS );
      InventoryKey inv2 = createInventory( iHighestInventoryKey, RefInvClassKey.SYS );
      invKeys.add( inv1 );
      invKeys.add( inv2 );

      List<Inventory> retreivedInventory = iRepository.search( invKeys );

      assertEquals( 2, retreivedInventory.size() );
   }


   @Test
   public void search_EmptyList() {

      List<Inventory> retreivedInventory = iRepository.search( Collections.emptyList() );
      assertEquals( 0, retreivedInventory.size() );
   }


   private InventoryKey createInventory( InventoryKey aHighestInv, RefInvClassKey aInvClass ) {
      return new InventoryBuilder().withHighestInventory( aHighestInv ).withClass( aInvClass )
            .withDescription( INVENTORY_NAME ).atLocation( LOCATION ).build();
   }

}
