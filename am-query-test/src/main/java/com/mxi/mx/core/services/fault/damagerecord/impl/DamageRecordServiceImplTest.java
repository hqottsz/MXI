package com.mxi.mx.core.services.fault.damagerecord.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.TrackedInventory;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryDamageKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.StageKey;
import com.mxi.mx.core.table.evt.EvtStageDao;
import com.mxi.mx.core.table.evt.EvtStageTable;
import com.mxi.mx.domain.inventory.damage.InventoryDamage;
import com.mxi.mx.repository.inventory.damage.InventoryDamageRepository;


public class DamageRecordServiceImplTest {

   private static final String LOCATION_DESCRIPTION_ORIGINAL = "left side";
   private static final String LOCATION_DESCRIPTION_NEW = "right side";

   private DamageRecordServiceImpl damageRecordService;
   private FaultKey faultKey;
   private InventoryKey trackedInventory1;
   private InventoryKey trackedInventory2;
   private InventoryDamage existingDamageRecord;
   private EvtStageDao evtStageDao;

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();
   private InventoryDamageRepository inventoryDamageRepository;


   @Before
   public void setUp() {
      damageRecordService = new DamageRecordServiceImpl();
      trackedInventory1 =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory inv ) {
                  inv.setDescription( "Shark fin" );
                  inv.setOriginalAssembly( new AssemblyKey( "4650:ABCD" ) );
                  inv.setLocation( new LocationKey( "4650:1" ) );
               }

            } );

      trackedInventory2 =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory inv ) {
                  inv.setDescription( "Shark fin 2" );
                  inv.setOriginalAssembly( new AssemblyKey( "4650:ABCD" ) );
                  inv.setLocation( new LocationKey( "4650:2" ) );
               }

            } );

      faultKey = Domain.createFault();
      evtStageDao = InjectorContainer.get().getInstance( EvtStageDao.class );

      existingDamageRecord =
            InventoryDamage.builder().faultKey( faultKey ).inventoryKey( trackedInventory1 )
                  .locationDescription( LOCATION_DESCRIPTION_ORIGINAL ).build();
      inventoryDamageRepository =
            InjectorContainer.get().getInstance( InventoryDamageRepository.class );
      inventoryDamageRepository.create( existingDamageRecord );
   }


   @Test( expected = NullPointerException.class )
   public void createDamageRecord_nullDamageRecord() throws Exception {
      damageRecordService.createDamageRecord( null, HumanResourceKey.ADMIN );
   }


   @Test( expected = NullPointerException.class )
   public void createDamageRecord_nullHumanResourceKey() throws Exception {
      InventoryDamage inventoryDamage = InventoryDamage.builder().inventoryKey( trackedInventory1 )
            .faultKey( faultKey ).locationDescription( LOCATION_DESCRIPTION_NEW ).build();

      damageRecordService.createDamageRecord( inventoryDamage, null );
   }


   @Test( expected = MxException.class )
   public void createDamageRecord_missingInventoryKey() throws Exception {
      InventoryDamage inventoryDamage = InventoryDamage.builder().inventoryKey( null )
            .faultKey( faultKey ).locationDescription( LOCATION_DESCRIPTION_ORIGINAL ).build();

      damageRecordService.createDamageRecord( inventoryDamage, HumanResourceKey.ADMIN );
   }


   @Test( expected = MxException.class )
   public void createDamageRecord_missingLocationDescription() throws Exception {
      InventoryDamage inventoryDamage = InventoryDamage.builder().inventoryKey( trackedInventory1 )
            .faultKey( faultKey ).locationDescription( null ).build();

      damageRecordService.createDamageRecord( inventoryDamage, HumanResourceKey.ADMIN );
   }


   @Test( expected = NullPointerException.class )
   public void updateDamageRecord_nullDamageRecord() throws Exception {
      damageRecordService.updateDamageRecord( null, HumanResourceKey.ADMIN );
   }


   @Test( expected = NullPointerException.class )
   public void updateDamageRecord_nullHumanResourceKey() throws Exception {
      InventoryDamage inventoryDamage = InventoryDamage.builder().inventoryKey( trackedInventory1 )
            .faultKey( faultKey ).locationDescription( LOCATION_DESCRIPTION_NEW ).build();

      damageRecordService.updateDamageRecord( inventoryDamage, null );
   }


   @Test
   public void createDamageRecord_addsHistoryNoteToFault() throws Exception {
      FaultKey newFault = Domain.createFault();
      InventoryDamage damageRecord = InventoryDamage.builder().inventoryKey( trackedInventory1 )
            .faultKey( newFault ).locationDescription( "Underneath" ).build();

      damageRecordService.createDamageRecord( damageRecord, HumanResourceKey.ADMIN );

      List<EvtStageTable> notes = getFaultHistoryNotes( newFault );
      assertEquals( 1, notes.size() );
      assertEquals(
            "Damage record has been added. Inventory with damage is: 'Shark fin'. Location is: 'Underneath'.",
            notes.get( 0 ).getStageNote() );
      assertEquals( HumanResourceKey.ADMIN, notes.get( 0 ).getHr() );
   }


   @Test
   public void createDamageRecord_success() throws Exception {
      FaultKey fault = Domain.createFault();
      InventoryDamage inventoryDamage = InventoryDamage.builder().inventoryKey( trackedInventory2 )
            .faultKey( fault ).locationDescription( LOCATION_DESCRIPTION_NEW ).build();

      InventoryDamageKey newRecordKey =
            damageRecordService.createDamageRecord( inventoryDamage, HumanResourceKey.ADMIN );

      Optional<InventoryDamage> newRecord = inventoryDamageRepository.find( newRecordKey );

      assertEquals( fault, newRecord.get().getFaultKey().get() );
      assertEquals( trackedInventory2, newRecord.get().getInventoryKey() );
      assertEquals( LOCATION_DESCRIPTION_NEW, newRecord.get().getLocationDescription() );
   }


   @Test( expected = MxException.class )
   public void createDamageRecord_onFaultWithDamageRecord() throws Exception {
      InventoryDamage inventoryDamage = InventoryDamage.builder().inventoryKey( trackedInventory2 )
            .faultKey( faultKey ).locationDescription( LOCATION_DESCRIPTION_NEW ).build();

      damageRecordService.createDamageRecord( inventoryDamage, HumanResourceKey.ADMIN );
   }


   @Test
   public void updateDamageRecord_addsHistoryNoteToFault() throws Exception {
      existingDamageRecord.setLocationDescription( "On top" );
      damageRecordService.updateDamageRecord( existingDamageRecord, HumanResourceKey.ADMIN );

      List<EvtStageTable> notes = getFaultHistoryNotes( existingDamageRecord.getFaultKey().get() );
      assertEquals( 1, notes.size() );
      assertEquals(
            "Damage record has been edited. Inventory with damage is: 'Shark fin'. Location is: 'On top'.",
            notes.get( 0 ).getStageNote() );
      assertEquals( HumanResourceKey.ADMIN, notes.get( 0 ).getHr() );
   }


   @Test( expected = NullPointerException.class )
   public void updateDamageRecord_missingInventoryDamageKey() throws Exception {
      existingDamageRecord.setKey( null );

      damageRecordService.updateDamageRecord( existingDamageRecord, HumanResourceKey.ADMIN );
   }


   @Test( expected = MxException.class )
   public void updateDamageRecord_missingInventoryKey() throws Exception {
      existingDamageRecord.setInventoryKey( null );

      damageRecordService.updateDamageRecord( existingDamageRecord, HumanResourceKey.ADMIN );
   }


   @Test( expected = MxException.class )
   public void updateDamageRecord_missingLocationDescription() throws Exception {
      existingDamageRecord.setLocationDescription( null );

      damageRecordService.updateDamageRecord( existingDamageRecord, HumanResourceKey.ADMIN );
   }


   @Test( expected = MxException.class )
   public void updateDamageRecord_recordDoesNotExist() throws Throwable {
      InventoryDamage nonExistingRecord =
            InventoryDamage.builder().key( new InventoryDamageKey( 4650, 9999 ) )
                  .faultKey( Domain.createFault() ).inventoryKey( trackedInventory1 )
                  .locationDescription( LOCATION_DESCRIPTION_ORIGINAL ).build();

      damageRecordService.updateDamageRecord( nonExistingRecord, HumanResourceKey.ADMIN );
   }


   @Test
   public void updateDamageRecord_changeLocationDescription() throws Exception {
      existingDamageRecord.setLocationDescription( LOCATION_DESCRIPTION_NEW );

      damageRecordService.updateDamageRecord( existingDamageRecord, HumanResourceKey.ADMIN );

      Optional<InventoryDamage> updatedRecord =
            inventoryDamageRepository.find( existingDamageRecord.getKey() );

      assertTrue( updatedRecord.isPresent() );
      assertEquals( LOCATION_DESCRIPTION_NEW, updatedRecord.get().getLocationDescription() );
   }


   @Test
   public void updateDamageRecord_changeInventory() throws Exception {
      existingDamageRecord.setInventoryKey( trackedInventory2 );

      damageRecordService.updateDamageRecord( existingDamageRecord, HumanResourceKey.ADMIN );

      Optional<InventoryDamage> updatedRecord =
            inventoryDamageRepository.find( existingDamageRecord.getKey() );

      assertTrue( updatedRecord.isPresent() );
      assertEquals( trackedInventory2, updatedRecord.get().getInventoryKey() );
   }


   @Test
   public void updateDamageRecord_doesNotLogNoteWhenNoChangesAreMade() throws Throwable {
      damageRecordService.updateDamageRecord( existingDamageRecord, HumanResourceKey.ADMIN );

      List<EvtStageTable> notes = getFaultHistoryNotes( existingDamageRecord.getFaultKey().get() );

      assertTrue( notes.isEmpty() );
   }


   private List<EvtStageTable> getFaultHistoryNotes( FaultKey faultKey ) {
      List<EvtStageTable> historyNotes = new ArrayList<>();

      DataSet dataSet = evtStageDao.getStageSnapshot( new EventKey( faultKey.toString() ) );
      while ( dataSet.next() ) {
         StageKey stageKey =
               new StageKey( faultKey.getDbId(), faultKey.getId(), dataSet.getInt( "stage_id" ) );
         historyNotes.add( evtStageDao.findByPrimaryKey( stageKey ) );
      }

      return historyNotes;
   }

}
