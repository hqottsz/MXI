package com.mxi.mx.core.maintenance.exec.fault.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.DamageRecord;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Engine;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.InventoryDamageKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;


@RunWith( BlockJUnit4ClassRunner.class )
public class FaultAppServiceTest {

   private static final FaultKey FAULT_ID_WITH_DAMAGE_RECORD = new FaultKey( "4650:2" );
   private static final FaultKey FAULT_ID_WITH_DAMAGE_COMPONENT = new FaultKey( "4650:1" );
   private static final FaultKey FAULT_ID_WITHOUT_DAMAGE_RECORD = new FaultKey( "4650:4" );
   private static final FaultKey FAULT_ID_DAMAGE_RECORD_WITHOUT_INV = new FaultKey( "4650:3" );

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule fakeGuiceDaoRule = new InjectionOverrideRule();

   private FaultAppService faultAppService;


   @Before
   public void setUp() {
      faultAppService = InjectorContainer.get().getInstance( FaultAppService.class );
   }


   @Test
   public void getDamageRecord_withComponent() {

      InventoryKey aircraftInventoryKey = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine engine ) {
            engine.setLocation( new LocationKey( "1:1" ) );
            engine.setDescription( "Engine" );
         }
      } );

      InventoryDamageKey damageRecordKey =
            Domain.createDamageRecord( new DomainConfiguration<DamageRecord>() {

               @Override
               public void configure( DamageRecord damageRecord ) {
                  damageRecord.setFaultKey( FAULT_ID_WITH_DAMAGE_COMPONENT );
                  damageRecord.setInventoryKey( aircraftInventoryKey );
                  damageRecord.setLocation( "component location" );
               }
            } );

      Optional<FaultDamageRecordTO> faultDamageRecordTO =
            faultAppService.getDamageRecord( FAULT_ID_WITH_DAMAGE_COMPONENT );
      assertTrue( faultDamageRecordTO.isPresent() );
      assertEquals( faultDamageRecordTO.get().getInventoryName(), "Engine" );
      assertEquals( faultDamageRecordTO.get().getLocation(), "component location" );
      assertEquals( faultDamageRecordTO.get().getInventoryDamageKey(), damageRecordKey );
      assertEquals( faultDamageRecordTO.get().getInventoryKey(), aircraftInventoryKey );
      assertFalse( faultDamageRecordTO.get().isAircraftDamageRecord() );

   }


   @Test
   public void getDamageRecord_withoutComponent() {

      InventoryKey aircraftInventoryKey =
            Domain.createAircraft( new DomainConfiguration<Aircraft>() {

               @Override
               public void configure( Aircraft aircraft ) {
                  aircraft.setLocation( new LocationKey( "1:1" ) );
                  aircraft.setDescription( "Aircraft" );
               }
            } );

      InventoryDamageKey damageRecordKey =
            Domain.createDamageRecord( new DomainConfiguration<DamageRecord>() {

               @Override
               public void configure( DamageRecord damageRecord ) {
                  damageRecord.setFaultKey( FAULT_ID_WITH_DAMAGE_RECORD );
                  damageRecord.setInventoryKey( aircraftInventoryKey );
                  damageRecord.setLocation( "aircraft location" );
               }
            } );

      Optional<FaultDamageRecordTO> faultDamageRecordTO =
            faultAppService.getDamageRecord( FAULT_ID_WITH_DAMAGE_RECORD );
      assertTrue( faultDamageRecordTO.isPresent() );
      assertEquals( faultDamageRecordTO.get().getInventoryName(), "Aircraft" );
      assertEquals( faultDamageRecordTO.get().getLocation(), "aircraft location" );
      assertEquals( faultDamageRecordTO.get().getInventoryDamageKey(), damageRecordKey );
      assertEquals( faultDamageRecordTO.get().getInventoryKey(), aircraftInventoryKey );
      assertTrue( faultDamageRecordTO.get().isAircraftDamageRecord() );

   }


   @Test
   public void getDamageRecord_doesNotExist() {
      Optional<FaultDamageRecordTO> faultDamageRecordTO =
            faultAppService.getDamageRecord( FAULT_ID_WITHOUT_DAMAGE_RECORD );
      assertFalse( faultDamageRecordTO.isPresent() );
   }


   @Test( expected = IllegalStateException.class )
   public void getDamageRecord_inventoryDoesNotExist() {

      Domain.createDamageRecord( new DomainConfiguration<DamageRecord>() {

         @Override
         public void configure( DamageRecord damageRecord ) {
            damageRecord.setFaultKey( FAULT_ID_DAMAGE_RECORD_WITHOUT_INV );
            damageRecord.setLocation( "aircraft location" );
            damageRecord.setInventoryKey( new InventoryKey( "1:233" ) );
         }
      } );

      faultAppService.getDamageRecord( FAULT_ID_DAMAGE_RECORD_WITHOUT_INV );
   }

}
