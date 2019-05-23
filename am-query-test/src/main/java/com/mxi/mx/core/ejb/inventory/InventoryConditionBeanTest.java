package com.mxi.mx.core.ejb.inventory;

import static com.mxi.mx.core.key.RefInvClassKey.ACFT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.servlet.SessionContextFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.core.configuration.axon.RecordingEventBus;
import com.mxi.mx.core.key.AircraftKey;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.CarrierKey;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.production.aircraft.domain.AircraftArchivedEvent;
import com.mxi.mx.core.production.aircraft.domain.AircraftScrappedEvent;
import com.mxi.mx.core.production.task.domain.TaskCancelledEvent;


public class InventoryConditionBeanTest {

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule injectionOverrideRule = new InjectionOverrideRule();

   private SessionContextFake iSessionContextFake = new SessionContextFake();

   private HumanResourceKey authorizingHr;

   private RecordingEventBus eventBus;

   private InventoryConditionBean inventoryConditionBean;
   private FncAccountKey chargeToAccountKey;

   private final String CONFIG_SLOT_ROOT_CODE = "ROOT_CODE";
   private final String ASSEMBLY_CODE = "A320";
   private final String CONFIG_SLOT_SYS_CODE = "SYS_CODE";
   private final String SYSTEM_NAME = "SYSTEM_NAME";
   private final String SCRAP_AIRCRAFT_NOTE = "Test Scrap aircraft note";


   @Test
   public void scrapUnscrapedAircraftMustEmitAircraftScrappedEvent()
         throws MxException, TriggerException {
      // Create an aircraft
      final String notes = "Test Scarp aircraft note";
      final CarrierKey originalCarrierKey = Domain.createOperator();
      final PartNoKey aircraftPartRoot = Domain.createPart( part -> {
         part.setQtyUnitKey( RefQtyUnitKey.EA );
      } );

      final AssemblyKey aircraftAssembly = Domain.createAircraftAssembly( assembly -> {
         assembly.setCode( ASSEMBLY_CODE );
         assembly.setRootConfigurationSlot( rootConfigSlot -> {
            rootConfigSlot.setCode( CONFIG_SLOT_ROOT_CODE );
            rootConfigSlot.addPartGroup( rootCsPartGroup -> {
               rootCsPartGroup.setInventoryClass( ACFT );
               rootCsPartGroup.addPart( aircraftPartRoot );
            } );
            rootConfigSlot.addConfigurationSlot( sysConfigSlot -> {
               sysConfigSlot.setCode( CONFIG_SLOT_SYS_CODE );
               sysConfigSlot.setConfigurationSlotClass( RefBOMClassKey.SYS );
               sysConfigSlot.addPartGroup( sysCsPartGroup -> {
                  sysCsPartGroup.setInventoryClass( RefInvClassKey.SYS );
               } );
            } );
         } );
      } );

      final InventoryKey aircraftKey = Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( aircraftAssembly );
         aircraft.setPart( aircraftPartRoot );
         aircraft.addSystem( SYSTEM_NAME );
         aircraft.setLocation( Domain.createLocation() );
         aircraft.setOperator( originalCarrierKey );
         aircraft.setOwner( Domain.createOwner() );
      } );

      final FncAccountKey chargeToAccountKey = new FncAccountKey( "4650:106" );
      InventoryConditionBean inventoryConditionBean = new InventoryConditionBean();
      inventoryConditionBean.setSessionContext( iSessionContextFake );

      inventoryConditionBean.scrapInventory( aircraftKey, chargeToAccountKey, null, notes,
            authorizingHr );

      // ASSERT
      assertEquals(
            new AircraftScrappedEvent( new AircraftKey( aircraftKey.toString() ), null, notes,
                  authorizingHr, chargeToAccountKey, new RefInvCondKey( 0, "SCRAP" ) ),
            eventBus.getEventMessages().get( 0 ).getPayload() );
   }


   @Test
   public void archiveUnarchivedAircraftMustEmitAircraftArchivedEvent()
         throws MxException, TriggerException {
      // ARRANGE
      final String notes = "Test Scarp aircraft note";
      final CarrierKey originalCarrierKey = Domain.createOperator();
      final PartNoKey aircraftPartRoot = Domain.createPart( part -> {
         part.setQtyUnitKey( RefQtyUnitKey.EA );
      } );
      final AssemblyKey aircraftAssembly = Domain.createAircraftAssembly( assembly -> {
         assembly.setCode( ASSEMBLY_CODE );
         assembly.setRootConfigurationSlot( rootConfigSlot -> {
            rootConfigSlot.setCode( CONFIG_SLOT_ROOT_CODE );
            rootConfigSlot.addPartGroup( rootCsPartGroup -> {
               rootCsPartGroup.setInventoryClass( ACFT );
               rootCsPartGroup.addPart( aircraftPartRoot );
            } );
            rootConfigSlot.addConfigurationSlot( sysConfigSlot -> {
               sysConfigSlot.setCode( CONFIG_SLOT_SYS_CODE );
               sysConfigSlot.setConfigurationSlotClass( RefBOMClassKey.SYS );
               sysConfigSlot.addPartGroup( sysCsPartGroup -> {
                  sysCsPartGroup.setInventoryClass( RefInvClassKey.SYS );
               } );
            } );
         } );
      } );

      final InventoryKey aircraftKey = Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( aircraftAssembly );
         aircraft.setPart( aircraftPartRoot );
         aircraft.addSystem( SYSTEM_NAME );
         aircraft.setLocation( Domain.createLocation() );
         aircraft.setOperator( originalCarrierKey );
         aircraft.setOwner( Domain.createOwner() );
      } );

      final FncAccountKey chargeToAccountKey = new FncAccountKey( "4650:106" );
      InventoryConditionBean inventoryConditionBean = new InventoryConditionBean();
      inventoryConditionBean.setSessionContext( iSessionContextFake );

      // ACT
      inventoryConditionBean.archiveInventory( aircraftKey, chargeToAccountKey, null, notes,
            authorizingHr );

      // ASSERT
      assertEquals(
            new AircraftArchivedEvent( new AircraftKey( aircraftKey.toString() ), null, notes,
                  authorizingHr, chargeToAccountKey, new RefInvCondKey( 0, "ARCHIVE" ) ),
            eventBus.getEventMessages().get( 0 ).getPayload() );

   }


   @Test
   public void whenScrapComponentWithActiveROThenTaskCancelledEventShouldBePublished()
         throws MxException, TriggerException {

      // ARRANGE
      final LocationKey locationKey = Domain.createLocation( location -> {
         location.setType( RefLocTypeKey.VENDOR );
      } );
      final PartNoKey trkPartKey = Domain.createPart( part -> {
         part.setInventoryClass( RefInvClassKey.TRK );
         part.setQuantity( BigDecimal.ONE );
         part.setQtyUnitKey( RefQtyUnitKey.EA );
      } );
      final OwnerKey ownerKey = Domain.createOwner();
      final InventoryKey trackedKey = Domain.createTrackedInventory( trackedInventory -> {
         trackedInventory.setLocation( locationKey );
         trackedInventory.setPartNumber( trkPartKey );
         trackedInventory.setOwner( ownerKey );

      } );

      final TaskKey roKey = Domain.createComponentWorkPackage( ro -> {
         ro.setInventory( trackedKey );
         ro.setStatus( RefEventStatusKey.ACTV );

      } );
      final PurchaseOrderKey poKey = Domain.createPurchaseOrder();
      Domain.createPurchaseOrderLine( poLine -> {
         poLine.orderKey( poKey );
         poLine.task( roKey );
      } );

      inventoryConditionBean.scrapInventory( trackedKey, chargeToAccountKey, null,
            SCRAP_AIRCRAFT_NOTE, authorizingHr );

      // ASSERT
      assertEquals( 1, eventBus.getEventMessages().size() );

      final Set<TaskKey> taskKeys = eventBus.getEventMessages().stream().filter( eventMessage -> {
         return eventMessage.getPayload() instanceof TaskCancelledEvent;
      } ).map( event -> {
         return ( ( TaskCancelledEvent ) event.getPayload() ).getTaskKey();
      } ).collect( Collectors.toSet() );

      assertTrue( taskKeys.contains( roKey ) );
   }


   @Before
   public void setUp() {
      eventBus = injectionOverrideRule.select( RecordingEventBus.class ).get();
      authorizingHr = Domain.createHumanResource( aHr -> aHr.setUser( Domain.createUser() ) );
      inventoryConditionBean = new InventoryConditionBean();
      inventoryConditionBean.setSessionContext( iSessionContextFake );
      chargeToAccountKey = new FncAccountKey( "4650:106" );
   }

}
