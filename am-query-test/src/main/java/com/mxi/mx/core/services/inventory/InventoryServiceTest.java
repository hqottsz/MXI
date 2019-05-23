package com.mxi.mx.core.services.inventory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.exception.MxRuntimeException;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.services.stask.TaskService;
import com.mxi.mx.domain.inventory.Inventory;
import com.mxi.mx.domain.system.System;
import com.mxi.mx.repository.inventory.InventoryRepository;
import com.mxi.mx.repository.system.SystemRepository;


@RunWith( MockitoJUnitRunner.class )
public class InventoryServiceTest {

   private static final AssemblyKey ASSEMBLY_KEY = new AssemblyKey( 4650, "B-737" );
   private static final ConfigSlotKey SYSTEM_CONFIG_SLOT_KEY = new ConfigSlotKey( ASSEMBLY_KEY, 1 );
   private static final System SYSTEM = new System.Builder()
         .id( new System.Id( SYSTEM_CONFIG_SLOT_KEY.getDbId(), SYSTEM_CONFIG_SLOT_KEY.getCd(),
               SYSTEM_CONFIG_SLOT_KEY.getBomId() ) )
         .ataCode( "61" ).build();
   private static final InventoryKey SYSTEM_INVENTORY_KEY = new InventoryKey( 4650, 0 );
   private static final InventoryKey INVENTORY_KEY = new InventoryKey( 4650, 1 );
   private static final InventoryKey HIGHEST_INVENTORY_KEY = new InventoryKey( 4650, 2 );

   @Mock
   private SystemRepository mockSystemRepository;

   @Mock
   private InventoryRepository mockInventoryRepository;

   @Mock
   private Inventory mockInventory;

   @Mock
   private Inventory mockHighestInventory;

   @Mock
   private TaskService taskService;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   private InventoryService inventoryService;


   @Before
   public void setUp() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
      inventoryService = new InventoryService( mock( InventoryEventRecorder.class ),
            mockSystemRepository, mockInventoryRepository, taskService );
   }


   @Test
   public void findSystem_returnsSystem() {
      when( mockSystemRepository.find( SYSTEM_CONFIG_SLOT_KEY ) )
            .thenReturn( Optional.of( SYSTEM ) );

      Optional<System> lSystem = inventoryService.findSystem( SYSTEM_INVENTORY_KEY );

      assertThat( lSystem.get(), is( SYSTEM ) );
   }


   @Test( expected = NullPointerException.class )
   public void getSystem_throwsExceptionOnNullInventoryKey() {
      new InventoryService().findSystem( null );
   }


   @Test( expected = MxRuntimeException.class )
   public void findSystem_inventoryDoesNotExist() {
      InventoryKey lInventoryThatDoesNotExist = new InventoryKey( 0, 0 );

      inventoryService.findSystem( lInventoryThatDoesNotExist );
   }


   @Test
   public void findSystem_isEmptyWhenInventoryIsNotASystem() {
      when( mockSystemRepository.find( SYSTEM_CONFIG_SLOT_KEY ) )
            .thenReturn( Optional.<System>empty() );

      Optional<System> lSystem = inventoryService.findSystem( SYSTEM_INVENTORY_KEY );
      assertThat( lSystem.isPresent(), is( false ) );
   }


   @Test( expected = NullPointerException.class )
   public void isAttachedToAnAircraft_nullInventoryKey() {
      inventoryService.isAttachedToAnAircraft( null );
   }


   @Test
   public void isAttachedToAnAircraft_missingInventory() {
      when( mockInventoryRepository.find( INVENTORY_KEY ) ).thenReturn( Optional.empty() );

      boolean isAttached = inventoryService.isAttachedToAnAircraft( INVENTORY_KEY );

      assertFalse( isAttached );
   }


   @Test
   public void isAttachedToAnAircraft_inventoryIsAircraft() {
      when( mockInventoryRepository.find( INVENTORY_KEY ) )
            .thenReturn( Optional.of( mockInventory ) );
      when( mockInventory.getClassKey() ).thenReturn( RefInvClassKey.ACFT );

      boolean isAttached = inventoryService.isAttachedToAnAircraft( INVENTORY_KEY );

      assertTrue( isAttached );
   }


   @Test
   public void isAttachedToAnAircraft_missingHighestInventory() {
      when( mockInventoryRepository.find( INVENTORY_KEY ) )
            .thenReturn( Optional.of( mockInventory ) );
      when( mockInventory.getClassKey() ).thenReturn( RefInvClassKey.TRK );
      when( mockInventory.getHighestInventoryKey() ).thenReturn( HIGHEST_INVENTORY_KEY );
      when( mockInventoryRepository.find( HIGHEST_INVENTORY_KEY ) ).thenReturn( Optional.empty() );

      boolean isAttached = inventoryService.isAttachedToAnAircraft( INVENTORY_KEY );

      assertFalse( isAttached );
   }


   @Test
   public void isAttachedToAnAircraft_highestInventoryNotAnAircraft() {
      when( mockInventoryRepository.find( INVENTORY_KEY ) )
            .thenReturn( Optional.of( mockInventory ) );
      when( mockInventory.getClassKey() ).thenReturn( RefInvClassKey.TRK );
      when( mockInventory.getHighestInventoryKey() ).thenReturn( HIGHEST_INVENTORY_KEY );
      when( mockInventoryRepository.find( HIGHEST_INVENTORY_KEY ) )
            .thenReturn( Optional.of( mockHighestInventory ) );
      when( mockHighestInventory.getClassKey() ).thenReturn( RefInvClassKey.TRK );

      boolean isAttached = inventoryService.isAttachedToAnAircraft( INVENTORY_KEY );

      assertFalse( isAttached );
   }


   @Test
   public void isAttachedToAnAircraft_allConditionsMet() {
      when( mockInventoryRepository.find( INVENTORY_KEY ) )
            .thenReturn( Optional.of( mockInventory ) );
      when( mockInventory.getClassKey() ).thenReturn( RefInvClassKey.TRK );
      when( mockInventory.getHighestInventoryKey() ).thenReturn( HIGHEST_INVENTORY_KEY );
      when( mockInventoryRepository.find( HIGHEST_INVENTORY_KEY ) )
            .thenReturn( Optional.of( mockHighestInventory ) );
      when( mockHighestInventory.getClassKey() ).thenReturn( RefInvClassKey.ACFT );

      boolean isAttached = inventoryService.isAttachedToAnAircraft( INVENTORY_KEY );

      assertTrue( isAttached );
   }
}
