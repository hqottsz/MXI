package com.mxi.mx.core.services.inventory.phys;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.axonframework.eventhandling.EventMessage;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.PartRequestBuilder;
import com.mxi.am.domain.builder.PartRequirementDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.GlobalParametersFake;
import com.mxi.mx.common.config.ParmTypeEnum;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.configuration.axon.RecordingEventBus;
import com.mxi.mx.core.key.AircraftKey;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InvCndChgEventKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefStageReasonKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.TaskInstPartKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskPartKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.production.aircraft.domain.AircraftUnarchivedEvent;
import com.mxi.mx.core.production.aircraft.domain.AircraftUnscrappedEvent;
import com.mxi.mx.core.production.task.domain.TaskCancelledEvent;
import com.mxi.mx.core.services.inventory.phys.exception.InvalidConditionException;
import com.mxi.mx.core.table.acevent.InvCndChgEventDao;
import com.mxi.mx.core.table.acevent.InvCndChgEventTable;
import com.mxi.mx.core.table.evt.EvtEventDao;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.unittest.table.evt.EvtEventUtil;


@RunWith( BlockJUnit4ClassRunner.class )
public class PhysicalInventoryServiceTest {

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();
   @Rule
   public InjectionOverrideRule injectionOverrideRule = new InjectionOverrideRule();

   protected static final String POSITION_CODE = "POSITION_CODE";
   protected static final String PART_GROUP1 = "PART_GROUP1";
   protected static final String PART_GROUP2 = "PART_GROUP2";
   private static final String TRK_CONFIG_SLOT1 = "TRK_CONFIG_SLOT1";
   private static final String TRK_CONFIG_SLOT2 = "TRK_CONFIG_SLOT2";
   private static final String TRK_CONFIG_SLOT_CD1 = "TRK_SLOT_CD1";
   private static final String TRK_CONFIG_SLOT_CD2 = "TRK_SLOT_CD2";
   private static final String ENG_ASSMBL_CD = "CFM380";

   private InventoryKey inventory;
   private LocationKey location;
   private PartNoKey partNo;
   private RecordingEventBus eventBus;


   @Test
   public void reinductInventory_inventoryIsArchived_withTasks() throws Exception {

      final InventoryKey mainInventoryKey = createInventory( RefInvCondKey.ARCHIVE );

      // build the requested inventory to be used by the task in part request
      final InventoryKey requestedInventoryKey = createInventory( RefInvCondKey.RFI );

      // build the task on the main inventory with a part request on the second inventory
      TaskKey jobCard = Domain.createJobCard( jic -> {
         jic.setStatus( RefEventStatusKey.ACTV );
         jic.setInventory( mainInventoryKey );
      } );

      TaskPartKey partRequirement = new PartRequirementDomainBuilder( jobCard ).forPart( partNo )
            .withInstallPart( partNo ).withInstallQuantity( 1.0 ).build();

      PartRequestKey partRequest = new PartRequestBuilder().forTask( jobCard )
            .withReservedInventory( requestedInventoryKey ).withStatus( RefEventStatusKey.PRAVAIL )
            .forPartRequirement( new TaskInstPartKey( partRequirement, 1 ) ).build();

      // re-induct the archived inventory that includes tasks to be cancelled
      new PhysicalInventoryService( mainInventoryKey ).reinductInventory( location,
            RefInvCondKey.INSPREQ, null, HumanResourceKey.ADMIN, null, true, true, null, null );

      assertNotEquals( RefInvCondKey.ARCHIVE,
            InvInvTable.findByPrimaryKey( mainInventoryKey ).getInvCondCd() );

      new EvtEventUtil( jobCard ).assertEventStatus( RefEventStatusKey.CANCEL );
      new EvtEventUtil( partRequest ).assertEventStatus( RefEventStatusKey.PRCANCEL );
   }


   @Test
   public void reinductInventory_inventoryIsArchived() throws Exception {

      final InventoryKey archivedInventory = createInventory( RefInvCondKey.ARCHIVE );

      // subject under test
      new PhysicalInventoryService( archivedInventory ).reinductInventory( location,
            RefInvCondKey.INSPREQ, null, HumanResourceKey.ADMIN, null, true, true, null, null );

      assertNotEquals( RefInvCondKey.ARCHIVE,
            InvInvTable.findByPrimaryKey( archivedInventory ).getInvCondCd() );
   }


   @Test
   public void reinductInventory_inventoryIsScrapped() throws Exception {

      final InventoryKey scrappedInventory = createInventory( RefInvCondKey.SCRAP );

      // subject under test
      new PhysicalInventoryService( scrappedInventory ).reinductInventory( location,
            RefInvCondKey.INSPREQ, null, HumanResourceKey.ADMIN, null, true, true, null, null );

      assertNotEquals( RefInvCondKey.SCRAP,
            InvInvTable.findByPrimaryKey( scrappedInventory ).getInvCondCd() );
   }


   @Test( expected = InvalidConditionException.class )
   public void reinductInventory_inventoryWithInvalidCondition() throws Exception {

      final InventoryKey quarantineInventory = createInventory( RefInvCondKey.QUAR );

      // subject under test
      new PhysicalInventoryService( quarantineInventory ).reinductInventory( location,
            RefInvCondKey.INSPREQ, null, HumanResourceKey.ADMIN, null, true, true, null, null );
   }


   @Test
   public void
         whenReinductInventoryFromScrappedInventoryWithActiveTaskThenTaskCancelledEventShouldBePublished()
               throws Exception {
      // ARRANGE
      final PartNoKey aircraftPartKey = Domain.createPart();
      final AssemblyKey aircraftAssemblyKey = Domain.createAircraftAssembly( aircraftAssembly -> {
         aircraftAssembly.setRootConfigurationSlot( configurationSlot -> {
            configurationSlot.addPartGroup( partGroup -> {
               partGroup.setInventoryClass( RefInvClassKey.ACFT );
               partGroup.addPart( aircraftPartKey );
            } );
         } );
      } );

      final InventoryKey scrappedAircraftKey = Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( aircraftAssemblyKey );
         aircraft.setPart( aircraftPartKey );
         aircraft.setCondition( RefInvCondKey.SCRAP );

      } );

      final ConfigSlotKey aircraftRootConfigSlotKey = new ConfigSlotKey( aircraftAssemblyKey, 0 );

      final TaskTaskKey reqDefinitionKey = Domain.createRequirementDefinition( reqDefinition -> {
         reqDefinition.againstConfigurationSlot( aircraftRootConfigSlotKey );
         reqDefinition.setRecurring( false );
         reqDefinition.setStatus( RefTaskDefinitionStatusKey.ACTV );
         reqDefinition.setOnCondition( true );

      } );

      final TaskKey reqKey = Domain.createRequirement( requirement -> {
         requirement.setInventory( scrappedAircraftKey );
         requirement.setDefinition( reqDefinitionKey );
         requirement.setStatus( RefEventStatusKey.ACTV );

      } );

      // ACT
      new PhysicalInventoryService( scrappedAircraftKey ).reinductInventory( location,
            RefInvCondKey.INSPREQ, null, HumanResourceKey.ADMIN, null, true, true, null, null );

      // ASSERT
      final List<EventMessage<?>> events = eventBus.getEventMessages().stream()
            .filter( evt -> evt.getPayload() instanceof TaskCancelledEvent )
            .collect( Collectors.toList() );

      assertEquals( "Incorrect number of events is published.", 1, events.size() );

      TaskCancelledEvent taskCancelledEvent = ( TaskCancelledEvent ) events.get( 0 ).getPayload();

      assertThat( "The event is against wrong task.", taskCancelledEvent.getTaskKey(),
            equalTo( reqKey ) );
   }


   @Test
   public void
         whenReinductInventoryFromArchivedInventoryWithActiveTaskThenTaskCancelledEventShouldBePublished()
               throws Exception {
      // ARRANGE
      final PartNoKey aircraftPartKey = Domain.createPart();
      final AssemblyKey aircraftAssemblyKey = Domain.createAircraftAssembly( aircraftAssembly -> {
         aircraftAssembly.setRootConfigurationSlot( configurationSlot -> {
            configurationSlot.addPartGroup( partGroup -> {
               partGroup.setInventoryClass( RefInvClassKey.ACFT );
               partGroup.addPart( aircraftPartKey );
            } );
         } );
      } );

      final InventoryKey archivedAircraftKey = Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( aircraftAssemblyKey );
         aircraft.setPart( aircraftPartKey );
         aircraft.setCondition( RefInvCondKey.ARCHIVE );

      } );

      final ConfigSlotKey aircraftRootConfigSlotKey = new ConfigSlotKey( aircraftAssemblyKey, 0 );

      final TaskTaskKey reqDefinitionKey = Domain.createRequirementDefinition( reqDefinition -> {
         reqDefinition.againstConfigurationSlot( aircraftRootConfigSlotKey );
         reqDefinition.setRecurring( false );
         reqDefinition.setStatus( RefTaskDefinitionStatusKey.ACTV );
         reqDefinition.setOnCondition( true );

      } );

      final TaskKey reqKey = Domain.createRequirement( requirement -> {
         requirement.setInventory( archivedAircraftKey );
         requirement.setDefinition( reqDefinitionKey );
         requirement.setStatus( RefEventStatusKey.ACTV );

      } );

      // ACT
      new PhysicalInventoryService( archivedAircraftKey ).reinductInventory( location,
            RefInvCondKey.INSPREQ, null, HumanResourceKey.ADMIN, null, true, true, null, null );

      // ASSERT
      final List<EventMessage<?>> events = eventBus.getEventMessages().stream()
            .filter( evt -> evt.getPayload() instanceof TaskCancelledEvent )
            .collect( Collectors.toList() );

      assertEquals( "Incorrect number of events is published.", 1, events.size() );

      TaskCancelledEvent taskCancelledEvent = ( TaskCancelledEvent ) events.get( 0 ).getPayload();

      assertThat( "The event is against wrong task.", taskCancelledEvent.getTaskKey(),
            equalTo( reqKey ) );
   }


   @Test
   public void incompleteRFIShouldReturnStringRFBWhenRFBIsEnabled() throws Exception {

      // enable RFB, and set incomplete RFI inventory
      boolean isRFBEnabled = true;
      RefInvCondKey invCondKey = RefInvCondKey.RFI;
      boolean completeBool = false;

      // assert that the condition string is RFB
      validateInvCondStr( isRFBEnabled, invCondKey, completeBool, "RFB" );
   }


   @Test
   public void completeRFIShouldReturnStringRFBWhenRFBIsEnabled() throws Exception {

      // enable RFB, and set complete RFI inventory
      boolean isRFBEnabled = true;
      RefInvCondKey invCondKey = RefInvCondKey.RFI;
      boolean completeBool = true;

      // assert that the condition string is RFI
      validateInvCondStr( isRFBEnabled, invCondKey, completeBool, "RFI" );
   }


   @Test
   public void incompleteREPREQShouldReturnStringREPREQWhenRFBIsDisabled() throws Exception {

      // disable RFB, and set incomplete REPREQ inventory
      boolean isRFBEnabled = false;
      RefInvCondKey invCondKey = RefInvCondKey.REPREQ;
      boolean completeBool = false;

      // assert that the condition string is REPREQ
      validateInvCondStr( isRFBEnabled, invCondKey, completeBool, "REPREQ" );
   }


   @Test
   public void completeRFIShouldReturnStringRFBWhenRFBIsDisabled() throws Exception {

      // disable RFB, and set complete RFI inventory
      boolean isRFBEnabled = false;
      RefInvCondKey invCondKey = RefInvCondKey.RFI;
      boolean completeBool = true;

      // assert that the condition string is RFI
      validateInvCondStr( isRFBEnabled, invCondKey, completeBool, "RFI" );
   }


   /**
    * This tests that when quarantine an inventory, a AC event will be recorded into
    * inv_cnd_chg_event table. And an entry will be added to quar_quar table.
    *
    * Note: the assert of data insert into evt_event will be removed after all AC events are
    * converted and removed from evt_event table.
    *
    */
   @Test
   public void testQuarantineInventory() throws MxException {
      // DATA SETUP
      final InventoryKey inventory = createInventory( RefInvCondKey.INSPREQ );

      RefInvCondKey newCondition = RefInvCondKey.QUAR;
      RefStageReasonKey reason = RefStageReasonKey.ACDMCH;
      Date localDate = new Date();
      Date gmtDate = new Date();
      HumanResourceKey authorizingHr = HumanResourceKey.ADMIN;
      String note = "Quarantine the inventory";

      // ACTION
      new PhysicalInventoryService( inventory ).recordConditionChangeEvent( newCondition, reason,
            localDate, gmtDate, authorizingHr, note );

      // ASSERT
      DataSetArgument queryArgs = new DataSetArgument();
      queryArgs.add( inventory, "inv_no_db_id", "inv_no_id" );
      QuerySet queryResult =
            QuerySetFactory.getInstance().executeQueryTable( "inv_cnd_chg_inv", queryArgs );

      int expectedValue = 1;
      int actualValue = queryResult.getRowCount();
      assertEquals( "Expected 1 event associated to the inventory.", expectedValue, actualValue );

      queryResult.first();
      EventKey eventKey = queryResult.getKey( EventKey.class, "event_db_id", "event_id" );
      InvCndChgEventKey invCndChgEventKey =
            queryResult.getKey( InvCndChgEventKey.class, "event_db_id", "event_id" );

      // a AC event entry is added to inv_cnd_chg_event table
      InvCndChgEventDao invCndChgEventDao =
            InjectorContainer.get().getInstance( InvCndChgEventDao.class );
      InvCndChgEventTable invCndChgEventTable =
            invCndChgEventDao.findByPrimaryKey( invCndChgEventKey );

      assertTrue( "Expected a AC event in inv_cnd_chg_event talbe.", invCndChgEventTable.exists() );

      // the same AC event entry is duplicated to evt_event table
      EvtEventDao evtEventDao = InjectorContainer.get().getInstance( EvtEventDao.class );
      EvtEventTable evtEventTable = evtEventDao.findByPrimaryKey( eventKey );

      assertTrue( "Expected a AC event in evt_event talbe.", evtEventTable.exists() );

      // a new entry is added to quar_quar table
      queryArgs.clear();
      queryArgs.add( invCndChgEventKey, "event_db_id", "event_id" );
      queryResult = QuerySetFactory.getInstance().executeQueryTable( "quar_quar", queryArgs );

      expectedValue = 1;
      actualValue = queryResult.getRowCount();
      assertEquals( "Expected an entry is added to quar_quar talbe.", expectedValue, actualValue );

      queryResult.first();
      InventoryKey expectedInventory = inventory;
      InventoryKey actualInventory =
            queryResult.getKey( InventoryKey.class, "inv_no_db_id", "inv_no_id" );

      assertEquals( "The inventory should have a quarantine event associated to it.",
            expectedInventory, actualInventory );
   }


   @Test
   public void itPublishAircraftUnscrappedEventWhenAircraftIsUnscrapped() throws Exception {
      // Given
      InventoryKey aircraft = Domain.createAircraft( ac -> {
         ac.setCondition( RefInvCondKey.SCRAP );
         ac.setLocation( location );
         ac.setPart( partNo );
      } );

      LocationKey newLocation = Domain.createLocation();
      RefInvCondKey newCondition = RefInvCondKey.INSPREQ;
      String note = "unscrapping an aircraft";
      HumanResourceKey authorizingHr = HumanResourceKey.ADMIN;
      FncAccountKey creditToAccount = FncAccountKey.INVOICE;

      // When
      new PhysicalInventoryService( aircraft ).reinductInventory( newLocation, newCondition, note,
            authorizingHr, creditToAccount, true, true, null, null );

      // Then
      assertEquals( 1, eventBus.getEventMessages().size() );
      assertEquals(
            new AircraftUnscrappedEvent( new AircraftKey( aircraft ), creditToAccount, newLocation,
                  newCondition, authorizingHr, null, note ),
            eventBus.getEventMessages().get( 0 ).getPayload() );
   }


   @Test
   public void itPublishAircraftUnarchivedEventWhenAircraftIsUnarchived() throws Exception {
      // Given
      InventoryKey aircraft = Domain.createAircraft( ac -> {
         ac.setCondition( RefInvCondKey.ARCHIVE );
         ac.setLocation( location );
         ac.setPart( partNo );
      } );

      LocationKey newLocation = Domain.createLocation();
      RefInvCondKey newCondition = RefInvCondKey.INSPREQ;
      String note = "unarchiving an aircraft";
      HumanResourceKey authorizingHr = HumanResourceKey.ADMIN;
      FncAccountKey creditToAccount = FncAccountKey.INVOICE;

      // When
      new PhysicalInventoryService( aircraft ).reinductInventory( newLocation, newCondition, note,
            authorizingHr, creditToAccount, true, true, null, null );

      // Then
      assertEquals( 1, eventBus.getEventMessages().size() );
      assertEquals(
            new AircraftUnarchivedEvent( new AircraftKey( aircraft ), creditToAccount, newLocation,
                  newCondition, authorizingHr, null, note ),
            eventBus.getEventMessages().get( 0 ).getPayload() );
   }


   @Test
   public void itDoesNotPublishAircraftUnarchivedEventWhenNotAircraftInventoryIsUnarchived()
         throws Exception {
      // Given
      InventoryKey archivedInventory = createInventory( RefInvCondKey.ARCHIVE );

      LocationKey newLocation = Domain.createLocation();
      RefInvCondKey newCondition = RefInvCondKey.INSPREQ;
      String note = "unarchiving a track inventory";
      HumanResourceKey authorizingHr = HumanResourceKey.ADMIN;
      FncAccountKey creditToAccount = FncAccountKey.INVOICE;

      // When
      new PhysicalInventoryService( archivedInventory ).reinductInventory( newLocation,
            newCondition, note, authorizingHr, creditToAccount, true, true, null, null );

      // Then
      assertEquals( 0, eventBus.getEventMessages().size() );
   }


   @Test
   public void itDoesNotPublishAircraftUnscrappedEventWhenNotAircraftInventoryIsUnscrapped()
         throws Exception {
      // Given
      InventoryKey scrappedInventory = createInventory( RefInvCondKey.SCRAP );

      LocationKey newLocation = Domain.createLocation();
      RefInvCondKey newCondition = RefInvCondKey.INSPREQ;
      String note = "unscrapping a track inventory";
      HumanResourceKey authorizingHr = HumanResourceKey.ADMIN;
      FncAccountKey creditToAccount = FncAccountKey.INVOICE;

      // When
      new PhysicalInventoryService( scrappedInventory ).reinductInventory( newLocation,
            newCondition, note, authorizingHr, creditToAccount, true, true, null, null );

      // Then
      assertEquals( 0, eventBus.getEventMessages().size() );
   }


   @Before
   public void setUp() {
      location = Domain.createLocation();
      partNo = Domain.createPart();
      inventory = createEngineWithTRKConfigSlots();
      eventBus = injectionOverrideRule.select( RecordingEventBus.class ).get();
   }


   /**
    * Creates Engine inventory with TRK config slots
    *
    * @return the inventory key
    */
   private InventoryKey createEngineWithTRKConfigSlots() {

      // Set up an engine part
      final PartNoKey enginePart = new PartNoBuilder().withInventoryClass( RefInvClassKey.TRK )
            .withStatus( RefPartStatusKey.ACTV ).build();

      final PartNoKey childPart = new PartNoBuilder().withInventoryClass( RefInvClassKey.TRK )
            .withStatus( RefPartStatusKey.ACTV ).build();

      // Set up an engine assembly
      final AssemblyKey engineAssy = Domain.createEngineAssembly( engineAssembly -> {
         engineAssembly.setCode( ENG_ASSMBL_CD );
         engineAssembly.setRootConfigurationSlot( rootConfigSlot -> {

            rootConfigSlot.addConfigurationSlot( configSlot -> {
               configSlot.addPosition( TRK_CONFIG_SLOT1 );
               configSlot.setCode( TRK_CONFIG_SLOT_CD1 );
               configSlot.setConfigurationSlotClass( RefBOMClassKey.TRK );
               configSlot.setMandatoryFlag( true );
               configSlot.addPartGroup( partGroup -> {
                  partGroup.setCode( PART_GROUP1 );
                  partGroup.setInventoryClass( RefInvClassKey.TRK );
                  partGroup.addPart( childPart );
               } );
            } );

            rootConfigSlot.addConfigurationSlot( configSlot -> {
               configSlot.addPosition( TRK_CONFIG_SLOT2 );
               configSlot.setCode( TRK_CONFIG_SLOT_CD2 );
               configSlot.setConfigurationSlotClass( RefBOMClassKey.TRK );
               configSlot.setMandatoryFlag( true );
               configSlot.addPartGroup( partGroup -> {
                  partGroup.setCode( PART_GROUP2 );
                  partGroup.setInventoryClass( RefInvClassKey.TRK );
                  partGroup.addPart( childPart );
               } );

            } );
         } );
      } );

      // create engine inventory
      final InventoryKey engine = Domain.createEngine( eng -> {
         eng.setAssembly( engineAssy );
         eng.setPartNumber( enginePart );
      } );

      // create TRK child inventory and attach to engine
      Domain.createTrackedInventory( trkInventory -> {
         trkInventory.setLastKnownConfigSlot( ENG_ASSMBL_CD, TRK_CONFIG_SLOT_CD1,
               TRK_CONFIG_SLOT1 );
         trkInventory.setParent( engine );
      } );

      // create TRK child inventory and attach to engine
      Domain.createTrackedInventory( trkInventory -> {
         trkInventory.setLastKnownConfigSlot( ENG_ASSMBL_CD, TRK_CONFIG_SLOT_CD2,
               TRK_CONFIG_SLOT2 );
         trkInventory.setParent( engine );
      } );

      return engine;
   }


   private void setReadyForBuildConfigParam( boolean value ) {

      GlobalParametersFake configParms = new GlobalParametersFake( ParmTypeEnum.LOGIC.name() );
      configParms.setBoolean( "ENABLE_READY_FOR_BUILD", value );
      GlobalParameters.setInstance( configParms );
   }


   /**
    * Builds a inventory
    *
    * @return InventoryKey
    */
   private InventoryKey createInventory( RefInvCondKey inventoryCondition ) {

      final RefInvCondKey condition = inventoryCondition;

      return Domain.createTrackedInventory( trkInventory -> {
         trkInventory.setPartNumber( partNo );
         trkInventory.setLocation( location );
         trkInventory.setCondition( condition );
      } );
   }


   public void validateInvCondStr( boolean isRFBEnabled, RefInvCondKey invCondKey,
         boolean completeBool, String expectedCondStr ) throws Exception {

      // enable or disable RFB
      setReadyForBuildConfigParam( isRFBEnabled );

      // set the inventory properties
      InvInvTable inventoryTable = InvInvTable.findByPrimaryKey( inventory );
      inventoryTable.setInvCond( invCondKey );
      inventoryTable.setComplete( completeBool );
      inventoryTable.update();

      // get the condition string
      PhysicalInventoryService physicalInventoryService = new PhysicalInventoryService( inventory );

      String actualInvCondStr = physicalInventoryService.getInvCondStr( invCondKey );

      // assert the condition string is as expected
      assertEquals( expectedCondStr, actualInvCondStr );
   }

}
