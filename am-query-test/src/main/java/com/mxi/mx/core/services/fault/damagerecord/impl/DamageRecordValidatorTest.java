package com.mxi.mx.core.services.fault.damagerecord.impl;

import static org.mockito.Mockito.mock;

import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.AbstractModule;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.internationalization.Localizable;
import com.mxi.mx.common.internationalization.StringBundles;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.services.fault.FaultService;
import com.mxi.mx.core.services.fault.FaultServiceFactory;
import com.mxi.mx.domain.inventory.Inventory;
import com.mxi.mx.domain.inventory.damage.InventoryDamage;
import com.mxi.mx.repository.inventory.InventoryRepository;
import com.mxi.mx.repository.inventory.damage.InventoryDamageRepository;


@RunWith( MockitoJUnitRunner.class )
public class DamageRecordValidatorTest {

   private static final InventoryKey INVENTORY_KEY = new InventoryKey( "4650:1" );
   private static final FaultKey FAULT_KEY = new FaultKey( "4650:1" );
   private static final String LOCATION_DESCRIPTION_TOO_SHORT = "";
   private static final String LOCATION_DESCRIPTION_PERFECT = "Westboro";
   private static final String LOCATION_DESCRIPTION_TOO_LONG;
   static {
      StringBuilder locationDescriptionBuilder = new StringBuilder();
      {
         for ( int i = 0; i < ( DamageRecordValidator.LOCATION_DESCRIPTION_MAX_LENGTH + 1 ); i++ ) {
            locationDescriptionBuilder.append( "a" );
         }
      }

      LOCATION_DESCRIPTION_TOO_LONG = locationDescriptionBuilder.toString();
   }

   @Mock
   private InventoryDamageRepository mockInventoryDamageRepository;

   @Mock
   private InventoryRepository mockInventoryRepository;

   @Mock
   private Inventory mockInventory;

   @Mock
   private FaultServiceFactory mockFaultServiceFactory;

   @Mock
   private FaultService mockFaultService;

   private DamageRecordValidator damageRecordValidator;

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule =
         new InjectionOverrideRule( new AbstractModule() {

            @Override
            protected void configure() {
               bind( InventoryDamageRepository.class ).toInstance( mockInventoryDamageRepository );
               bind( InventoryRepository.class ).toInstance( mockInventoryRepository );
            }

         } );


   @Before
   public void setUp() {
      StringBundles.setSingleton( mock( Localizable.class ) );
      damageRecordValidator = new DamageRecordValidator( mockFaultServiceFactory );

      Mockito.when( mockInventoryRepository.find( INVENTORY_KEY ) )
            .thenReturn( Optional.of( mockInventory ) );
      Mockito.when( mockInventory.getClassKey() ).thenReturn( RefInvClassKey.TRK );
   }


   @Test( expected = MxException.class )
   public void validateInventoryDamageForCreate_damageRecordExistsForFault() throws Exception {
      InventoryDamage existingInventoryDamageWithFault =
            InventoryDamage.builder().inventoryKey( INVENTORY_KEY ).faultKey( FAULT_KEY )
                  .locationDescription( "right between the eyes" ).build();

      Mockito.when( mockInventoryDamageRepository.find( FAULT_KEY ) )
            .thenReturn( Optional.of( existingInventoryDamageWithFault ) );

      InventoryDamage inventoryDamage = InventoryDamage.builder().inventoryKey( INVENTORY_KEY )
            .faultKey( FAULT_KEY ).locationDescription( LOCATION_DESCRIPTION_PERFECT ).build();

      damageRecordValidator.validateInventoryDamageForCreate( inventoryDamage );
   }


   @Test( expected = MxException.class )
   public void validateInventoryDamageForCreate_nullInventory() throws Exception {
      Mockito.when( mockInventoryDamageRepository.find( FAULT_KEY ) )
            .thenReturn( Optional.empty() );
      Mockito.when( mockFaultServiceFactory.get( FAULT_KEY ) ).thenReturn( mockFaultService );
      Mockito.when( mockFaultService.getFaultEventStatus() ).thenReturn( RefEventStatusKey.ACTV );

      InventoryDamage inventoryDamage = InventoryDamage.builder().inventoryKey( null )
            .faultKey( FAULT_KEY ).locationDescription( LOCATION_DESCRIPTION_PERFECT ).build();

      damageRecordValidator.validateInventoryDamageForCreate( inventoryDamage );
   }


   @Test( expected = MxException.class )
   public void validateInventoryDamageForCreate_invalidFaultStatus() throws Exception {
      InventoryDamage existingInventoryDamageWithFault =
            InventoryDamage.builder().inventoryKey( INVENTORY_KEY ).faultKey( FAULT_KEY )
                  .locationDescription( LOCATION_DESCRIPTION_PERFECT ).build();

      Mockito.when( mockInventoryDamageRepository.find( FAULT_KEY ) )
            .thenReturn( Optional.of( existingInventoryDamageWithFault ) );
      Mockito.when( mockFaultServiceFactory.get( FAULT_KEY ) ).thenReturn( mockFaultService );
      Mockito.when( mockFaultService.getFaultEventStatus() ).thenReturn( RefEventStatusKey.CFNFF );

      InventoryDamage inventoryDamage = InventoryDamage.builder().inventoryKey( INVENTORY_KEY )
            .faultKey( FAULT_KEY ).locationDescription( LOCATION_DESCRIPTION_PERFECT ).build();

      damageRecordValidator.validateInventoryDamageForCreate( inventoryDamage );
   }


   @Test( expected = MxException.class )
   public void validateInventoryDamageForCreate_nullLocation() throws Exception {
      InventoryDamage inventoryDamage =
            InventoryDamage.builder().inventoryKey( INVENTORY_KEY ).build();

      damageRecordValidator.validateInventoryDamageForCreate( inventoryDamage );
   }


   @Test( expected = MxException.class )
   public void validateInventoryDamageForCreate_locationTooShort() throws Exception {
      InventoryDamage inventoryDamage = InventoryDamage.builder().inventoryKey( INVENTORY_KEY )
            .locationDescription( LOCATION_DESCRIPTION_TOO_SHORT ).build();

      damageRecordValidator.validateInventoryDamageForCreate( inventoryDamage );
   }


   @Test( expected = MxException.class )
   public void validateInventoryDamageForCreate_locationTooLong() throws Exception {
      InventoryDamage inventoryDamage = InventoryDamage.builder().inventoryKey( INVENTORY_KEY )
            .locationDescription( LOCATION_DESCRIPTION_TOO_LONG ).build();

      damageRecordValidator.validateInventoryDamageForCreate( inventoryDamage );
   }


   @Test
   public void validateInventoryDamageForCreate_perfectLocation() throws Exception {
      InventoryDamage inventoryDamage = InventoryDamage.builder().inventoryKey( INVENTORY_KEY )
            .locationDescription( LOCATION_DESCRIPTION_PERFECT ).build();

      Mockito.when( mockInventory.getClassKey() ).thenReturn( RefInvClassKey.TRK );

      damageRecordValidator.validateInventoryDamageForCreate( inventoryDamage );
   }


   @Test( expected = MxException.class )
   public void validateInventoryDamageForCreate_invalidClass() throws Exception {
      InventoryDamage inventoryDamage = InventoryDamage.builder().inventoryKey( INVENTORY_KEY )
            .locationDescription( LOCATION_DESCRIPTION_PERFECT ).build();

      Mockito.when( mockInventory.getClassKey() ).thenReturn( RefInvClassKey.SYS );

      damageRecordValidator.validateInventoryDamageForCreate( inventoryDamage );
   }


   @Test( expected = MxException.class )
   public void validateInventoryDamageForUpdate_invalidClass() throws Exception {
      InventoryDamage inventoryDamage = InventoryDamage.builder().inventoryKey( INVENTORY_KEY )
            .locationDescription( LOCATION_DESCRIPTION_PERFECT ).build();

      Mockito.when( mockInventory.getClassKey() ).thenReturn( RefInvClassKey.SYS );

      damageRecordValidator.validateInventoryDamageForUpdate( inventoryDamage );
   }


   @Test( expected = MxException.class )
   public void validateInventoryDamageForUpdate_nullInventory() throws Exception {
      Mockito.when( mockInventoryDamageRepository.find( FAULT_KEY ) )
            .thenReturn( Optional.empty() );
      Mockito.when( mockFaultServiceFactory.get( FAULT_KEY ) ).thenReturn( mockFaultService );
      Mockito.when( mockFaultService.getFaultEventStatus() ).thenReturn( RefEventStatusKey.ACTV );

      InventoryDamage inventoryDamage = InventoryDamage.builder().inventoryKey( null )
            .faultKey( FAULT_KEY ).locationDescription( LOCATION_DESCRIPTION_PERFECT ).build();

      damageRecordValidator.validateInventoryDamageForUpdate( inventoryDamage );
   }


   @Test( expected = MxException.class )
   public void validateInventoryDamageForUpdate_invalidFaultStatus() throws Exception {
      InventoryDamage existingInventoryDamageWithFault =
            InventoryDamage.builder().inventoryKey( INVENTORY_KEY ).faultKey( FAULT_KEY )
                  .locationDescription( LOCATION_DESCRIPTION_PERFECT ).build();

      Mockito.when( mockInventoryDamageRepository.find( FAULT_KEY ) )
            .thenReturn( Optional.of( existingInventoryDamageWithFault ) );
      Mockito.when( mockFaultServiceFactory.get( FAULT_KEY ) ).thenReturn( mockFaultService );
      Mockito.when( mockFaultService.getFaultEventStatus() ).thenReturn( RefEventStatusKey.CFNFF );

      InventoryDamage inventoryDamage = InventoryDamage.builder().inventoryKey( INVENTORY_KEY )
            .faultKey( FAULT_KEY ).locationDescription( LOCATION_DESCRIPTION_PERFECT ).build();

      damageRecordValidator.validateInventoryDamageForUpdate( inventoryDamage );
   }


   @Test( expected = MxException.class )
   public void validateInventoryDamageForUpdate_nullLocation() throws Exception {
      InventoryDamage inventoryDamage =
            InventoryDamage.builder().inventoryKey( INVENTORY_KEY ).build();

      damageRecordValidator.validateInventoryDamageForUpdate( inventoryDamage );
   }


   @Test( expected = MxException.class )
   public void validateInventoryDamageForUpdate_locationTooShort() throws Exception {
      InventoryDamage inventoryDamage = InventoryDamage.builder().inventoryKey( INVENTORY_KEY )
            .locationDescription( LOCATION_DESCRIPTION_TOO_SHORT ).build();

      damageRecordValidator.validateInventoryDamageForUpdate( inventoryDamage );
   }


   @Test( expected = MxException.class )
   public void validateInventoryDamageForUpdate_locationTooLong() throws Exception {
      InventoryDamage inventoryDamage = InventoryDamage.builder().inventoryKey( INVENTORY_KEY )
            .locationDescription( LOCATION_DESCRIPTION_TOO_LONG ).build();

      damageRecordValidator.validateInventoryDamageForUpdate( inventoryDamage );
   }


   @Test
   public void validateInventoryDamageForUpdate_perfectLocation() throws Exception {
      InventoryDamage inventoryDamage = InventoryDamage.builder().inventoryKey( INVENTORY_KEY )
            .locationDescription( LOCATION_DESCRIPTION_PERFECT ).build();

      Mockito.when( mockInventory.getClassKey() ).thenReturn( RefInvClassKey.TRK );

      damageRecordValidator.validateInventoryDamageForUpdate( inventoryDamage );
   }

}
