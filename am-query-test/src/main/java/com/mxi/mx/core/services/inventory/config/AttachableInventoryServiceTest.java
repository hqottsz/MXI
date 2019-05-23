package com.mxi.mx.core.services.inventory.config;

import static com.mxi.mx.core.key.DataTypeKey.CDY;
import static com.mxi.mx.core.key.RefBOMClassKey.SUBASSY;
import static com.mxi.mx.core.key.RefBOMClassKey.TRK;
import static com.mxi.mx.core.key.RefInvCondKey.REPREQ;
import static com.mxi.mx.core.key.RefLocTypeKey.LINE;
import static com.mxi.mx.core.key.RefTaskClassKey.REQ;
import static com.mxi.mx.core.key.RefTaskDefinitionStatusKey.ACTV;
import static java.math.BigDecimal.ONE;
import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.AircraftAssembly;
import com.mxi.am.domain.ConfigurationSlot;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Engine;
import com.mxi.am.domain.EngineAssembly;
import com.mxi.am.domain.Location;
import com.mxi.am.domain.Part;
import com.mxi.am.domain.PartGroup;
import com.mxi.am.domain.Requirement;
import com.mxi.am.domain.RequirementDefinition;
import com.mxi.am.domain.TrackedInventory;
import com.mxi.am.domain.builder.AssemblyBuilder;
import com.mxi.am.domain.builder.ConfigSlotBuilder;
import com.mxi.am.domain.builder.ConfigSlotPositionBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.OwnerDomainBuilder;
import com.mxi.am.domain.builder.PartGroupDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.PartRequirementDomainBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.domain.builder.TaskRevisionBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.am.ee.OperateAsUserRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.configuration.axon.RecordingEventBus;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefAssmblClassKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefSchedFromKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskPartKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.production.task.domain.TaskTerminatedEvent;
import com.mxi.mx.core.services.inventory.InvUtils;
import com.mxi.mx.core.services.inventory.InventoryServiceFactory;
import com.mxi.mx.core.table.eqp.EqpAssmblBom;
import com.mxi.mx.core.table.eqp.EqpAssmblPos;
import com.mxi.mx.core.table.eqp.EqpBomPart;
import com.mxi.mx.core.table.evt.EvtSchedDeadTable;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.inv.InvOwnerTable;
import com.mxi.mx.core.unittest.table.inv.InvInv;
import com.mxi.mx.core.unittest.table.stask.SchedPart;
import com.mxi.mx.core.unittest.table.stask.SchedStaskUtil;


/**
 * This class tests the attachable inventory service.
 */
public final class AttachableInventoryServiceTest {

   private static final String PARTGROUP = "PARTGROUP";
   private static final String POS1_1 = "1.1";
   private static final String CONFIGSLOT = "210033";
   private static final String TRK1 = "TRK1";
   private static final String FUELSYS = "FUELSYS";

   private static final String ENG_ROOT_CS = "ENG_ROOT_CS";
   private static final String ENG_SUB_CS = "ENG_SUB_CS";
   private static final String ENG_SUB_CS_POSITION_ONE = "ENG_SUB_CS_POSITION_ONE";
   private static final String ACFT_ROOT_CS = "ACFT_ROOT_CS";
   private static final String ACFT_ENG_CS = "ACFT_ENG_CS";
   private static final String ACFT_ENG_CS_POSITION_ONE = "ACFT_ENG_CS_POSITION_ONE";

   private static final OwnerKey OWNER = new OwnerKey( 0, 1000 );

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public OperateAsUserRule operateAsUserRule = new OperateAsUserRule( 999, "user" );

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule fakeGuiceDaoRule = new InjectionOverrideRule();

   private static final Date TEST_RELEASE_DATE =
         new GregorianCalendar( 2000, 0, 1, 12, 30, 45 ).getTime();
   private static final String TEST_RELEASE_REMARKS = "test release remarks";
   private static final String TEST_RELEASE_NUMBER = "testReleaseNumber";

   private HumanResourceKey hr;

   private RecordingEventBus eventBus;


   /**
    * Attach a loose engine to an aircraft. Ensure that a part requirement on a subinventory of the
    * engine with a position under the engine gets the new position of the engine as the next
    * highest position.
    *
    * @throws Exception
    */
   @Test
   public void testThatPartReqOnSubInvHasEngineNhPositionForAttach() throws Exception {

      // create an inventory owner
      OwnerKey owner = new OwnerDomainBuilder().build();

      // create a location
      LocationKey location = Domain.createLocation();

      // create a part
      PartNoKey enginePart = new PartNoBuilder().build();

      // create the aircraft assembly
      AssemblyKey acftAssembly = new AssemblyBuilder( "ACFT" ).build();

      // create the aircraft root config slot and position
      ConfigSlotKey acftConfigSlot =
            new ConfigSlotBuilder( "ACFT" ).withRootAssembly( acftAssembly ).build();
      ConfigSlotPositionKey acftPosition =
            new ConfigSlotPositionBuilder().withConfigSlot( acftConfigSlot ).build();

      // create the engine system config slot and position
      ConfigSlotKey engineSystemSlot =
            new ConfigSlotBuilder( "ENGINE_SYS" ).withRootAssembly( acftAssembly )
                  .withParent( acftConfigSlot ).withClass( RefBOMClassKey.SYS ).build();
      ConfigSlotPositionKey engineSystemPosition = new ConfigSlotPositionBuilder()
            .withConfigSlot( engineSystemSlot ).withParentPosition( acftPosition ).build();

      // create the config slot for the engine subassembly
      ConfigSlotKey engineSubAssySlot =
            new ConfigSlotBuilder( "ENGINE_SUBASSY" ).withClass( RefBOMClassKey.SUBASSY )
                  .withParent( engineSystemSlot ).withRootAssembly( acftAssembly ).build();
      ConfigSlotPositionKey engineSubAssyPosition = new ConfigSlotPositionBuilder()
            .withConfigSlot( engineSubAssySlot ).withParentPosition( engineSystemPosition ).build();

      // create a part group for the engine in the subassembly config slot
      PartGroupKey enginePartGroup =
            new PartGroupDomainBuilder( "ENGINE" ).withConfigSlot( engineSubAssySlot )
                  .withPartNo( enginePart ).withInventoryClass( RefInvClassKey.ASSY ).build();

      // create the enigne assembly
      AssemblyKey engineAssembly = new AssemblyBuilder( "ENGINE" ).build();

      // create the engine config slot and position
      ConfigSlotKey engineSlot =
            new ConfigSlotBuilder( "ENGINE" ).withRootAssembly( engineAssembly ).build();
      ConfigSlotPositionKey enginePosition =
            new ConfigSlotPositionBuilder().withConfigSlot( engineSlot ).build();

      // create the config slot and position for the inventory under the tracked inentory
      ConfigSlotKey subTrkSlot = new ConfigSlotBuilder( "SUB_TRK" ).build();
      ConfigSlotPositionKey subTrkPosition =
            new ConfigSlotPositionBuilder().withConfigSlot( subTrkSlot ).build();

      // create the aircraft
      InventoryKey acft = new InventoryBuilder().withConfigSlotPosition( acftPosition )
            .withClass( RefInvClassKey.ACFT ).build();

      // create the engine system
      InventoryKey engineSystem = new InventoryBuilder().withClass( RefInvClassKey.SYS )
            .withParentInventory( acft ).withAssemblyInventory( acft )
            .withConfigSlotPosition( engineSystemPosition ).withReleaseNumber( "TestReleaseNum" )
            .withReleaseDate( new GregorianCalendar( 2000, 0, 1, 12, 30, 45 ).getTime() )
            .withReleaseRemarks( "Test Release Remarks" ).build();

      // create the engine
      InventoryKey engine = new InventoryBuilder().withPartNo( enginePart )
            .withConfigSlotPosition( enginePosition ).withClass( RefInvClassKey.ASSY )
            .withOwner( owner ).isIssued().atLocation( location ).build();

      // create a system under the engine
      InventoryKey system = new InventoryBuilder().withClass( RefInvClassKey.SYS )
            .withParentInventory( engine ).build();

      // create the tracked inventory
      InventoryKey trk = new InventoryBuilder().withClass( RefInvClassKey.TRK )
            .withParentInventory( system ).build();

      // create the task on the tracked inventory
      TaskKey trkTask = new TaskBuilder().onInventory( trk ).build();

      // create a part requirement on the task against the sub-tracked item position
      TaskPartKey partRequirement = new PartRequirementDomainBuilder( trkTask )
            .forPosition( subTrkPosition ).withNextHighestPosition( enginePosition ).build();

      // create the service class instance
      AttachableInventoryService service =
            new InventoryServiceFactory().getAttachableInventoryService( engine );

      // attach the engine to the aircraft at the subassembly position
      service.attachTrackedInventory( engineSystem, engineSubAssyPosition, enginePartGroup, null,
            hr, false, null, null, true, false, false, false, false );

      // ensure the nh config slot position is still the engine
      new SchedPart( partRequirement ).assertNHBomItemPositionKey( engineSubAssyPosition );

   }


   /**
    * Detach an engine from an aircraft. Ensure that a part requirement on a subinventory of the
    * engine with a position under the engine gets the new position of the engine as the next
    * highest position.
    *
    * @throws Exception
    */
   @Test
   public void testThatPartReqOnSubInvHasEngineNhPositionForDetatch() throws Exception {

      // create an inventory owner
      OwnerKey owner = new OwnerDomainBuilder().build();

      // create a location
      LocationKey location = Domain.createLocation();

      // create a part
      PartNoKey enginePart = new PartNoBuilder().build();

      // create the aircraft assembly
      AssemblyKey acftAssembly = new AssemblyBuilder( "ACFT" ).build();

      // create the aircraft root config slot and position
      ConfigSlotKey acftConfigSlot =
            new ConfigSlotBuilder( "ACFT" ).withRootAssembly( acftAssembly ).build();
      ConfigSlotPositionKey acftPosition =
            new ConfigSlotPositionBuilder().withConfigSlot( acftConfigSlot ).build();

      // create the engine system config slot and position
      ConfigSlotKey engineSystemSlot =
            new ConfigSlotBuilder( "ENGINE_SYS" ).withRootAssembly( acftAssembly )
                  .withParent( acftConfigSlot ).withClass( RefBOMClassKey.SYS ).build();
      ConfigSlotPositionKey engineSystemPosition = new ConfigSlotPositionBuilder()
            .withConfigSlot( engineSystemSlot ).withParentPosition( acftPosition ).build();

      // create the config slot for the engine subassembly
      ConfigSlotKey engineSubAssySlot =
            new ConfigSlotBuilder( "ENGINE_SUBASSY" ).withClass( RefBOMClassKey.SUBASSY )
                  .withParent( engineSystemSlot ).withRootAssembly( acftAssembly ).build();
      ConfigSlotPositionKey engineSubAssyPosition = new ConfigSlotPositionBuilder()
            .withConfigSlot( engineSubAssySlot ).withParentPosition( engineSystemPosition ).build();

      // create a part group for the engine in the subassembly config slot
      new PartGroupDomainBuilder( "ENGINE" ).withConfigSlot( engineSubAssySlot )
            .withPartNo( enginePart ).withInventoryClass( RefInvClassKey.ASSY ).build();

      // create the enigne assembly
      AssemblyKey engineAssembly = new AssemblyBuilder( "ENGINE" ).build();

      // create the engine config slot and position
      ConfigSlotKey engineSlot =
            new ConfigSlotBuilder( "ENGINE" ).withRootAssembly( engineAssembly ).build();
      ConfigSlotPositionKey enginePosition =
            new ConfigSlotPositionBuilder().withConfigSlot( engineSlot ).build();

      // create the config slot and position for the inventory under the tracked inentory
      ConfigSlotKey subTrkSlot = new ConfigSlotBuilder( "SUB_TRK" ).build();
      ConfigSlotPositionKey subTrkPosition =
            new ConfigSlotPositionBuilder().withConfigSlot( subTrkSlot ).build();

      // create the aircraft
      InventoryKey acft = new InventoryBuilder().withConfigSlotPosition( acftPosition )
            .withClass( RefInvClassKey.ACFT ).build();

      // create the engine system
      InventoryKey engineSystem = new InventoryBuilder().withClass( RefInvClassKey.SYS )
            .withParentInventory( acft ).withAssemblyInventory( acft )
            .withConfigSlotPosition( engineSystemPosition ).build();

      // create the engine
      InventoryKey engine = new InventoryBuilder().withPartNo( enginePart )
            .withConfigSlotPosition( engineSubAssyPosition ).withParentInventory( engineSystem )
            .withOriginalAssembly( engineAssembly ).withClass( RefInvClassKey.ASSY )
            .withOwner( owner ).isIssued().atLocation( location ).build();

      // create a system under the engine
      InventoryKey system = new InventoryBuilder().withClass( RefInvClassKey.SYS )
            .withParentInventory( engine ).build();

      // create the tracked inventory
      InventoryKey trk = new InventoryBuilder().withClass( RefInvClassKey.TRK )
            .withParentInventory( system ).build();

      // create the task on the tracked inventory
      TaskKey trkTask = new TaskBuilder().onInventory( trk ).build();

      // create a part requirement on the task against the sub-tracked item position
      TaskPartKey partRequirement = new PartRequirementDomainBuilder( trkTask )
            .forPosition( subTrkPosition ).withNextHighestPosition( engineSubAssyPosition ).build();

      // create the service class instance
      AttachableInventoryService service =
            new InventoryServiceFactory().getAttachableInventoryService( engine );

      // detatch the engine
      service.detachInventory( null, location, hr, false, null, null );

      // ensure the nh config slot position is still the engine
      new SchedPart( partRequirement ).assertNHBomItemPositionKey( enginePosition );
   }


   /**
    * Test that when an inventory's role is changed to another assembly, part requirements on tasks
    * on that inventory or sub inventory are change to be the positions of the new assembly.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testThatPartRequirementsAreMovedOnChangeRole() throws Exception {

      PartNoKey trkPart = new PartNoBuilder().withInventoryClass( RefInvClassKey.TRK ).build();
      PartNoKey subTrkPart = new PartNoBuilder().withInventoryClass( RefInvClassKey.TRK ).build();

      AssemblyKey acftAssembly1 = new AssemblyBuilder( "ACFT1" ).build();

      ConfigSlotKey trkSlot1 = new ConfigSlotBuilder( "TRK1" ).withRootAssembly( acftAssembly1 )
            .withClass( RefBOMClassKey.TRK ).build();
      ConfigSlotPositionKey trkPosition1 =
            new ConfigSlotPositionBuilder().withConfigSlot( trkSlot1 ).build();
      PartGroupKey trkPartGroup1 = new PartGroupDomainBuilder( "TRK1" ).withConfigSlot( trkSlot1 )
            .withInventoryClass( RefInvClassKey.TRK ).withPartNo( trkPart ).build();

      ConfigSlotKey subTrkSlot1 =
            new ConfigSlotBuilder( "SUB_TRK1" ).withRootAssembly( acftAssembly1 )
                  .withParent( trkSlot1 ).withClass( RefBOMClassKey.TRK ).build();
      ConfigSlotPositionKey subTrkPosition1 = new ConfigSlotPositionBuilder()
            .withConfigSlot( subTrkSlot1 ).withParentPosition( trkPosition1 ).build();
      PartGroupKey subTrkPartGroup1 =
            new PartGroupDomainBuilder( "SUB_TRK1" ).withConfigSlot( subTrkSlot1 )
                  .withInventoryClass( RefInvClassKey.TRK ).withPartNo( subTrkPart ).build();

      AssemblyKey acftAssembly2 = new AssemblyBuilder( "ACFT2" ).build();

      ConfigSlotKey trkSlot2 = new ConfigSlotBuilder( "TRK2" ).withRootAssembly( acftAssembly2 )
            .withClass( RefBOMClassKey.TRK ).build();
      ConfigSlotPositionKey trkPosition2 =
            new ConfigSlotPositionBuilder().withConfigSlot( trkSlot2 ).build();
      new PartGroupDomainBuilder( "TRK2" ).withConfigSlot( trkSlot2 )
            .withInventoryClass( RefInvClassKey.TRK ).withPartNo( trkPart ).build();

      ConfigSlotKey subTrkSlot2 =
            new ConfigSlotBuilder( "SUB_TRK2" ).withRootAssembly( acftAssembly2 )
                  .withParent( trkSlot2 ).withClass( RefBOMClassKey.TRK ).build();
      ConfigSlotPositionKey subTrkPosition2 = new ConfigSlotPositionBuilder()
            .withConfigSlot( subTrkSlot2 ).withParentPosition( trkPosition2 ).build();
      PartGroupKey subTrkPartGroup2 =
            new PartGroupDomainBuilder( "SUB_TRK2" ).withConfigSlot( subTrkSlot2 )
                  .withInventoryClass( RefInvClassKey.TRK ).withPartNo( subTrkPart ).build();

      InventoryKey component = new InventoryBuilder().withConfigSlotPosition( trkPosition1 )
            .withPartGroup( trkPartGroup1 ).withPartNo( trkPart ).build();
      new InventoryBuilder().withConfigSlotPosition( subTrkPosition1 )
            .withPartGroup( subTrkPartGroup1 ).withPartNo( subTrkPart ).build();

      TaskKey task = new TaskBuilder().onInventory( component ).build();

      TaskPartKey partRequirement =
            new PartRequirementDomainBuilder( task ).forPartGroup( subTrkPartGroup1 )
                  .forPosition( subTrkPosition1 ).withNextHighestPosition( trkPosition1 ).build();

      // create the service class instance
      AttachableInventoryService service =
            new InventoryServiceFactory().getAttachableInventoryService( component );

      service.changeRole( trkPosition1, trkPosition2 );

      SchedPart schedPart = new SchedPart( partRequirement );
      schedPart.assertNHBomItemPositionKey( trkPosition2 );
      schedPart.assertConfigSlotPosition( subTrkPosition2 );
      schedPart.assertSchedBom( subTrkPartGroup2 );
   }


   /**
    * Test that when an inventory's role is changed to a non-default position on another assembly,
    * part requirements on tasks on that inventory or sub inventory are change to be the positions
    * of the new assembly.
    *
    * @throws Exception
    *            If an error occurs
    */
   @Test
   public void testThatPartRequirementsForNonDefaultPositionAreMovedOnChangeRole()
         throws Exception {

      PartNoKey trkPart = new PartNoBuilder().withInventoryClass( RefInvClassKey.TRK ).build();
      PartNoKey subTrkPart = new PartNoBuilder().withInventoryClass( RefInvClassKey.TRK ).build();

      AssemblyKey acftAssembly1 = new AssemblyBuilder( "ACFT1" ).build();

      ConfigSlotKey trkSlot1 = new ConfigSlotBuilder( "TRK1" ).withRootAssembly( acftAssembly1 )
            .withClass( RefBOMClassKey.TRK ).build();
      ConfigSlotPositionKey trkPosition11 =
            new ConfigSlotPositionBuilder().withConfigSlot( trkSlot1 ).build();
      ConfigSlotPositionKey trkPosition12 =
            new ConfigSlotPositionBuilder().withConfigSlot( trkSlot1 ).build();
      PartGroupKey trkPartGroup1 = new PartGroupDomainBuilder( "TRK1" ).withConfigSlot( trkSlot1 )
            .withInventoryClass( RefInvClassKey.TRK ).withPartNo( trkPart ).build();

      ConfigSlotKey subTrkSlot1 =
            new ConfigSlotBuilder( "SUB_TRK1" ).withRootAssembly( acftAssembly1 )
                  .withParent( trkSlot1 ).withClass( RefBOMClassKey.TRK ).build();
      new ConfigSlotPositionBuilder().withConfigSlot( subTrkSlot1 )
            .withParentPosition( trkPosition11 ).build();
      new ConfigSlotPositionBuilder().withConfigSlot( subTrkSlot1 )
            .withParentPosition( trkPosition11 ).build();
      new ConfigSlotPositionBuilder().withConfigSlot( subTrkSlot1 )
            .withParentPosition( trkPosition12 ).build();

      ConfigSlotPositionKey subTrkPosition122 = new ConfigSlotPositionBuilder()
            .withConfigSlot( subTrkSlot1 ).withParentPosition( trkPosition12 ).build();
      PartGroupKey subTrkPartGroup1 =
            new PartGroupDomainBuilder( "SUB_TRK1" ).withConfigSlot( subTrkSlot1 )
                  .withInventoryClass( RefInvClassKey.TRK ).withPartNo( subTrkPart ).build();

      AssemblyKey acftAssembly2 = new AssemblyBuilder( "ACFT2" ).build();

      ConfigSlotKey trkSlot2 = new ConfigSlotBuilder( "TRK2" ).withRootAssembly( acftAssembly2 )
            .withClass( RefBOMClassKey.TRK ).build();
      ConfigSlotPositionKey trkPosition21 =
            new ConfigSlotPositionBuilder().withConfigSlot( trkSlot2 ).build();
      ConfigSlotPositionKey trkPosition22 =
            new ConfigSlotPositionBuilder().withConfigSlot( trkSlot2 ).build();
      new PartGroupDomainBuilder( "TRK2" ).withConfigSlot( trkSlot2 )
            .withInventoryClass( RefInvClassKey.TRK ).withPartNo( trkPart ).build();

      ConfigSlotKey subTrkSlot2 =
            new ConfigSlotBuilder( "SUB_TRK2" ).withRootAssembly( acftAssembly2 )
                  .withParent( trkSlot2 ).withClass( RefBOMClassKey.TRK ).build();
      new ConfigSlotPositionBuilder().withConfigSlot( subTrkSlot2 )
            .withParentPosition( trkPosition21 ).build();
      new ConfigSlotPositionBuilder().withConfigSlot( subTrkSlot2 )
            .withParentPosition( trkPosition21 ).build();
      new ConfigSlotPositionBuilder().withConfigSlot( subTrkSlot2 )
            .withParentPosition( trkPosition22 ).build();

      ConfigSlotPositionKey subTrkPosition222 = new ConfigSlotPositionBuilder()
            .withConfigSlot( subTrkSlot2 ).withParentPosition( trkPosition22 ).build();
      PartGroupKey subTrkPartGroup2 =
            new PartGroupDomainBuilder( "SUB_TRK2" ).withConfigSlot( subTrkSlot2 )
                  .withInventoryClass( RefInvClassKey.TRK ).withPartNo( subTrkPart ).build();

      InventoryKey component = new InventoryBuilder().withConfigSlotPosition( trkPosition12 )
            .withPartGroup( trkPartGroup1 ).withPartNo( trkPart ).build();
      new InventoryBuilder().withConfigSlotPosition( subTrkPosition122 )
            .withPartGroup( subTrkPartGroup1 ).withPartNo( subTrkPart ).build();

      TaskKey task = new TaskBuilder().onInventory( component ).build();

      TaskPartKey partRequirement = new PartRequirementDomainBuilder( task )
            .forPartGroup( subTrkPartGroup1 ).forPosition( subTrkPosition122 )
            .withNextHighestPosition( trkPosition12 ).build();

      // create the service class instance
      AttachableInventoryService service =
            new InventoryServiceFactory().getAttachableInventoryService( component );

      service.changeRole( trkPosition12, trkPosition22 );

      SchedPart schedPart = new SchedPart( partRequirement );
      schedPart.assertNHBomItemPositionKey( trkPosition22 );
      schedPart.assertConfigSlotPosition( subTrkPosition222 );
      schedPart.assertSchedBom( subTrkPartGroup2 );
   }


   /**
    * Test Case: The Create on Install task will not be initialized when its parent inventory is
    * installed
    *
    * @throws Exception
    */
   @Test
   public void testCreateOnInstallTaskNotInitializedWhenParentInventoryAttached() throws Exception {
      // ARRANGE

      // create inventory owner
      OwnerKey owner = new OwnerDomainBuilder().build();

      // create location
      LocationKey location = Domain.createLocation();

      // create part
      PartNoKey trkPart = new PartNoBuilder().withInventoryClass( RefInvClassKey.TRK ).build();
      PartNoKey subTrkPart = new PartNoBuilder().withInventoryClass( RefInvClassKey.TRK ).build();

      // create aircraft assembly
      AssemblyKey acftAssembly = new AssemblyBuilder( "ACFT" ).build();

      // create aircraft root config slot and position
      ConfigSlotKey acftConfigSlot =
            new ConfigSlotBuilder( "ACFT" ).withRootAssembly( acftAssembly ).build();
      ConfigSlotPositionKey acftPosition =
            new ConfigSlotPositionBuilder().withConfigSlot( acftConfigSlot ).build();

      // create navigation system config slot and position
      ConfigSlotKey navigationSystemSlot =
            new ConfigSlotBuilder( "NAVIGATION_SYS" ).withRootAssembly( acftAssembly )
                  .withParent( acftConfigSlot ).withClass( RefBOMClassKey.SYS ).build();
      ConfigSlotPositionKey navigationSystemPosition = new ConfigSlotPositionBuilder()
            .withConfigSlot( navigationSystemSlot ).withParentPosition( acftPosition ).build();

      // create the parent tracked config slot and position
      ConfigSlotKey trkSlot = new ConfigSlotBuilder( "TRK_SLOT" ).withClass( RefBOMClassKey.TRK )
            .withParent( navigationSystemSlot ).withRootAssembly( acftAssembly ).build();
      ConfigSlotPositionKey trkSlotPosition = new ConfigSlotPositionBuilder()
            .withConfigSlot( trkSlot ).withParentPosition( navigationSystemPosition ).build();

      // create a part group for the tracked part in the parent tracked config slot
      PartGroupKey trkPartGroup = new PartGroupDomainBuilder( "TRK" ).withConfigSlot( trkSlot )
            .withPartNo( trkPart ).withInventoryClass( RefInvClassKey.TRK ).build();

      ConfigSlotKey subTrkSlot =
            new ConfigSlotBuilder( "SUB_TRK_SLOT" ).withClass( RefBOMClassKey.TRK )
                  .withParent( trkSlot ).withRootAssembly( acftAssembly ).build();
      ConfigSlotPositionKey subTrkSlotPosition = new ConfigSlotPositionBuilder()
            .withConfigSlot( subTrkSlot ).withParentPosition( trkSlotPosition ).build();

      // create a part group for the tracked part in the tracked config slot
      new PartGroupDomainBuilder( "SUBTRK" ).withConfigSlot( subTrkSlot ).withPartNo( subTrkPart )
            .withInventoryClass( RefInvClassKey.TRK ).build();

      // create the aircraft
      InventoryKey acft = new InventoryBuilder().withConfigSlotPosition( acftPosition )
            .withClass( RefInvClassKey.ACFT ).build();

      // create the navigation system
      InventoryKey navigationSystem = new InventoryBuilder().withClass( RefInvClassKey.SYS )
            .withParentInventory( acft ).withAssemblyInventory( acft )
            .withConfigSlotPosition( navigationSystemPosition ).build();

      // create the parent tracked inventory
      InventoryKey trk = new InventoryBuilder().withPartNo( trkPart )
            .withConfigSlotPosition( trkSlotPosition ).withClass( RefInvClassKey.TRK )
            .withOwner( owner ).isIssued().atLocation( location ).build();

      // create the tracked inventory
      new InventoryBuilder().withPartNo( subTrkPart ).withConfigSlotPosition( subTrkSlotPosition )
            .withClass( RefInvClassKey.TRK ).withOwner( owner ).isIssued().atLocation( location )
            .withParentInventory( trk ).build();

      // Create test effective dates
      Calendar cal = new GregorianCalendar();
      cal.set( 2015, 2, 17 );

      Date effectiveDate = DateUtils.ceilDay( cal.getTime() );

      // Create an task revision with a flag CreateOnAnyInst
      TaskRevisionBuilder taskRev1Builder =
            new TaskRevisionBuilder().withTaskClass( REQ ).withConfigSlot( subTrkSlot )
                  .isCreateOnAnyInst().isScheduledFromEffectiveDate( effectiveDate )
                  .withStatus( ACTV ).withRevisionNumber( 1 );
      TaskTaskKey taskRev1Key = taskRev1Builder.build();

      // create the service class instance
      AttachableInventoryService service =
            new InventoryServiceFactory().getAttachableInventoryService( trk );

      // ACT
      service.attachTrackedInventory( navigationSystem, trkSlotPosition, trkPartGroup, null, hr,
            false, null, null, false, false, true, false, false );

      // ASSERT
      TaskKey taskKey = SchedStaskUtil.findLastCreatedTaskForTaskDefinition( taskRev1Key );

      Assert.assertNull( taskKey );
   }


   /**
    *
    * Verify that attaching a part doesn't wipe the inventory item's release number, release date &
    * release remarks
    *
    * @throws Exception
    */
   @Test
   public void testAttachPartDoesNotWipeReleaseDetails() throws Exception {

      // create inventory owner
      OwnerKey owner = new OwnerDomainBuilder().build();

      // create location
      LocationKey location = Domain.createLocation();

      // create part
      PartNoKey trkPart = new PartNoBuilder().withInventoryClass( RefInvClassKey.TRK ).build();
      PartNoKey subTrkPart = new PartNoBuilder().withInventoryClass( RefInvClassKey.TRK ).build();

      // create aircraft assembly
      AssemblyKey acftAssembly = new AssemblyBuilder( "ACFT" ).build();

      // create aircraft root config slot and position
      ConfigSlotKey acftConfigSlot =
            new ConfigSlotBuilder( "ACFT" ).withRootAssembly( acftAssembly ).build();
      ConfigSlotPositionKey acftPosition =
            new ConfigSlotPositionBuilder().withConfigSlot( acftConfigSlot ).build();

      // create navigation system config slot and position
      ConfigSlotKey navigationSystemSlot =
            new ConfigSlotBuilder( "NAVIGATION_SYS" ).withRootAssembly( acftAssembly )
                  .withParent( acftConfigSlot ).withClass( RefBOMClassKey.SYS ).build();
      ConfigSlotPositionKey navigationSystemPosition = new ConfigSlotPositionBuilder()
            .withConfigSlot( navigationSystemSlot ).withParentPosition( acftPosition ).build();

      // create the parent tracked config slot and position
      ConfigSlotKey trkSlot = new ConfigSlotBuilder( "TRK_SLOT" ).withClass( RefBOMClassKey.TRK )
            .withParent( navigationSystemSlot ).withRootAssembly( acftAssembly ).build();
      ConfigSlotPositionKey trkSlotPosition = new ConfigSlotPositionBuilder()
            .withConfigSlot( trkSlot ).withParentPosition( navigationSystemPosition ).build();

      // create a part group for the tracked part in the parent tracked config slot
      PartGroupKey trkPartGroup = new PartGroupDomainBuilder( "TRK" ).withConfigSlot( trkSlot )
            .withPartNo( trkPart ).withInventoryClass( RefInvClassKey.TRK ).build();

      ConfigSlotKey subTrkSlot =
            new ConfigSlotBuilder( "SUB_TRK_SLOT" ).withClass( RefBOMClassKey.TRK )
                  .withParent( trkSlot ).withRootAssembly( acftAssembly ).build();
      ConfigSlotPositionKey subTrkSlotPosition = new ConfigSlotPositionBuilder()
            .withConfigSlot( subTrkSlot ).withParentPosition( trkSlotPosition ).build();

      // create a part group for the tracked part in the tracked config slot
      new PartGroupDomainBuilder( "SUBTRK" ).withConfigSlot( subTrkSlot ).withPartNo( subTrkPart )
            .withInventoryClass( RefInvClassKey.TRK ).build();

      // create the aircraft
      InventoryKey acft = new InventoryBuilder().withConfigSlotPosition( acftPosition )
            .withClass( RefInvClassKey.ACFT ).build();

      // create the navigation system
      InventoryKey navigationSystem =
            new InventoryBuilder().withClass( RefInvClassKey.SYS ).withParentInventory( acft )
                  .withAssemblyInventory( acft ).withConfigSlotPosition( navigationSystemPosition )
                  .withReleaseNumber( TEST_RELEASE_NUMBER ).withReleaseDate( TEST_RELEASE_DATE )
                  .withReleaseRemarks( TEST_RELEASE_REMARKS ).build();

      // create the parent tracked inventory
      InventoryKey trk = new InventoryBuilder().withPartNo( trkPart )
            .withConfigSlotPosition( trkSlotPosition ).withClass( RefInvClassKey.TRK )
            .withOwner( owner ).isIssued().atLocation( location ).build();

      // create the tracked inventory
      new InventoryBuilder().withPartNo( subTrkPart ).withConfigSlotPosition( subTrkSlotPosition )
            .withClass( RefInvClassKey.TRK ).withOwner( owner ).isIssued().atLocation( location )
            .withParentInventory( trk ).build();

      // Create test effective dates
      Calendar cal = new GregorianCalendar();
      cal.set( 2015, 2, 17 );

      // create the service class instance
      AttachableInventoryService service =
            new InventoryServiceFactory().getAttachableInventoryService( trk );

      // ACT
      service.attachTrackedInventory( navigationSystem, trkSlotPosition, trkPartGroup, null, hr,
            false, null, null, false, false, true, false, false );

      // ASSERT
      InvInvTable invInv = InvInvTable.findByPrimaryKey( navigationSystem );
      Assert.assertEquals( invInv.getReleaseNumber(), TEST_RELEASE_NUMBER );
      Assert.assertEquals( invInv.getReleaseDate(), TEST_RELEASE_DATE );
      Assert.assertEquals( invInv.getReleaseRemarks(), TEST_RELEASE_REMARKS );
   }


   /**
    * Detach a TRK from its parent TRK, The parent TRK is a loose inventory via detach button.
    * Ensure one inv_remove record is created for the child TRK.
    *
    * @throws Exception
    */
   @Test
   public void itCreatesInvRemoveRecordForDetachTrkFromLooseParentTrk() throws Exception {

      final PartNoKey trkPart = Domain.createPart( new DomainConfiguration<Part>() {

         @Override
         public void configure( Part part ) {
            part.setInventoryClass( RefInvClassKey.TRK );
         }

      } );
      Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

         @Override
         public void configure( EngineAssembly assembly ) {
            assembly.setCode( RefAssmblClassKey.ENG.getCd() );
            assembly.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

               @Override
               public void configure( ConfigurationSlot rootConfigurationSlot ) {
                  rootConfigurationSlot.setConfigurationSlotClass( RefBOMClassKey.ROOT );
                  rootConfigurationSlot
                        .addConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot subConfigurationSlot ) {
                              subConfigurationSlot
                                    .addPartGroup( new DomainConfiguration<PartGroup>() {

                                       @Override
                                       public void configure( PartGroup partGroup ) {
                                          partGroup.setInventoryClass( RefInvClassKey.TRK );
                                          partGroup.addPart( trkPart );
                                          partGroup.setCode( PARTGROUP );
                                       }
                                    } );
                              subConfigurationSlot.addPosition( POS1_1 );
                              subConfigurationSlot.setCode( CONFIGSLOT );
                              subConfigurationSlot.setConfigurationSlotClass( RefBOMClassKey.TRK );
                           }
                        } );
               }
            } );
         }
      } );

      final InventoryKey looseTrackedInvKey = Domain.createTrackedInventory();
      final InventoryKey childTrackedInvKey =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory trk ) {
                  trk.setParent( looseTrackedInvKey );
                  trk.setPartNumber( trkPart );
                  trk.setLastKnownConfigSlot( RefAssmblClassKey.ENG.getCd(), CONFIGSLOT, POS1_1 );
                  trk.setSerialNumber( TRK1 );
               }
            } );

      AttachableInventoryService service = InventoryServiceFactory.getInstance()
            .getAttachableInventoryService( childTrackedInvKey );

      service.detachInventory( null, null, hr, true, null, null );

      assertInvRemoveRecord( childTrackedInvKey, looseTrackedInvKey, null, looseTrackedInvKey );

   }


   /**
    * Detach a TRK with Sub-component from its parent SYS via detach button, The SYS is under an
    * loose Engine. Ensure one inv_remove record is created for the child TRK.
    *
    * @throws Exception
    */
   @Test
   public void itCreatesInvRemoveRecordForDetachTrkWithSubFromEngineSys() throws Exception {

      final PartNoKey trkPart = Domain.createPart( new DomainConfiguration<Part>() {

         @Override
         public void configure( Part part ) {
            part.setInventoryClass( RefInvClassKey.TRK );
         }

      } );
      Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

         @Override
         public void configure( EngineAssembly assembly ) {
            assembly.setCode( RefAssmblClassKey.ENG.getCd() );
            assembly.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

               @Override
               public void configure( ConfigurationSlot rootConfigurationSlot ) {
                  rootConfigurationSlot.setConfigurationSlotClass( RefBOMClassKey.ROOT );
                  rootConfigurationSlot
                        .addConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot subConfigurationSlot ) {
                              subConfigurationSlot
                                    .addPartGroup( new DomainConfiguration<PartGroup>() {

                                       @Override
                                       public void configure( PartGroup partGroup ) {
                                          partGroup.setInventoryClass( RefInvClassKey.TRK );
                                          partGroup.addPart( trkPart );
                                          partGroup.setCode( PARTGROUP );
                                       }
                                    } );
                              subConfigurationSlot.addPosition( POS1_1 );
                              subConfigurationSlot.setCode( CONFIGSLOT );
                              subConfigurationSlot.setConfigurationSlotClass( RefBOMClassKey.TRK );
                           }
                        } );
               }
            } );
         }
      } );

      final InventoryKey engineInvKey = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine engine ) {
            engine.addSystem( FUELSYS );
         }
      } );
      final InventoryKey fuelSysInvKey = InvUtils.getSystemByName( engineInvKey, FUELSYS );

      final InventoryKey trackedInvKey =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory trk ) {
                  trk.setParent( fuelSysInvKey );
                  trk.setPartNumber( trkPart );
                  trk.setLastKnownConfigSlot( RefAssmblClassKey.ENG.getCd(), CONFIGSLOT, POS1_1 );
                  trk.setSerialNumber( TRK1 );
               }
            } );
      final InventoryKey childTrackedInvKey =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory trk ) {
                  trk.setParent( trackedInvKey );
               }
            } );

      AttachableInventoryService service =
            InventoryServiceFactory.getInstance().getAttachableInventoryService( trackedInvKey );

      service.detachInventory( null, null, hr, true, null, null );

      // verify main Trk removed record
      assertInvRemoveRecord( trackedInvKey, fuelSysInvKey, engineInvKey, engineInvKey );
      // verify sub component removed record
      assertInvRemoveRecord( childTrackedInvKey, trackedInvKey, engineInvKey, engineInvKey );
   }


   /**
    * A TRK is installed by attach button, the config slot has a create on install REQ task defn
    * with a calendar based deadline. The created task shall still use CUSTOM scheduling rule
    *
    * @throws Exception
    */
   @Test
   public void itCreatesOnInstallTaskWithCustomScheduleRuleByAttachButton() throws Exception {

      final PartNoKey trkPart = Domain.createPart( new DomainConfiguration<Part>() {

         @Override
         public void configure( Part part ) {
            part.setInventoryClass( RefInvClassKey.TRK );
            part.setShortDescription( "TRK PART" );
         }

      } );

      final AssemblyKey acftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aircraftAssembly ) {
                  aircraftAssembly
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot configurationSlot ) {
                              configurationSlot.addPosition( "ACFT_POS" );
                              configurationSlot.setCode( "ACFT" );
                              configurationSlot.addConfigurationSlot(
                                    new DomainConfiguration<ConfigurationSlot>() {

                                       @Override
                                       public void
                                             configure( ConfigurationSlot subConfigurationSlot ) {
                                          subConfigurationSlot.addPartGroup(
                                                new DomainConfiguration<PartGroup>() {

                                                   @Override
                                                   public void configure( PartGroup partGroup ) {
                                                      partGroup.setInventoryClass(
                                                            RefInvClassKey.TRK );
                                                      partGroup.addPart( trkPart );
                                                      partGroup.setCode( "TRK" );
                                                   }
                                                } );
                                          subConfigurationSlot.addPosition( "TRK_POS" );
                                          subConfigurationSlot.setCode( "TRK_SLOT" );
                                          subConfigurationSlot
                                                .setConfigurationSlotClass( RefBOMClassKey.TRK );
                                       }
                                    } );
                           }
                        } );
               }
            } );

      final ConfigSlotKey trkSlot = EqpAssmblBom.getBomItemKey( "ACFT", "TRK_SLOT" );
      final ConfigSlotPositionKey trkSlotPosition =
            new ConfigSlotPositionKey( trkSlot, EqpAssmblPos.getFirstPosId( trkSlot ) );

      final PartGroupKey trkPartGroup = EqpBomPart.getBomPartKey( acftAssembly, "TRK" );

      final LocationKey location = Domain.createLocation( new DomainConfiguration<Location>() {

         @Override
         public void configure( Location location ) {
            location.setType( RefLocTypeKey.LINE );
         }
      } );

      final InventoryKey acft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aircraft ) {
            aircraft.setAssembly( acftAssembly );
            aircraft.setLocation( location );
         }
      } );

      final InventoryKey trk =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory trk ) {
                  trk.setPartNumber( trkPart );
                  trk.setLocation( location );
                  trk.setDescription( "TRK1" );
                  trk.setSerialNumber( "TRK_SER1" );
                  trk.setLastKnownConfigSlot( "ACFT", "TRK_SLOT", "TRK_POS" );
                  trk.setOwner( InvOwnerTable.getOwnerKey( "N/A" ) );

               }
            } );

      Calendar cal = new GregorianCalendar();
      cal.set( 2015, 2, 17 );

      final Date effectiveDate = DateUtils.ceilDay( cal.getTime() );

      final TaskTaskKey taskRev1Key =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition reqDefn ) {
                  reqDefn.againstConfigurationSlot( trkSlot );
                  reqDefn.setScheduledFromEffectiveDate( effectiveDate );
                  reqDefn.setCreateOnInstall();
                  reqDefn.setCode( "REQ_DEFN" );
                  reqDefn.setRevisionNumber( 1 );
                  reqDefn.setStatus( ACTV );
                  reqDefn.addSchedulingRule( CDY, ONE );
               }
            } );

      AttachableInventoryService service =
            new InventoryServiceFactory().getAttachableInventoryService( trk );

      service.attachTrackedInventory( acft, trkSlotPosition, trkPartGroup, null, hr, true, null,
            null, false, false, true, false, false );

      assertTaskDeadlineScheduleFromCode( taskRev1Key, RefSchedFromKey.CUSTOM );
   }


   /**
    * A TRK is removed by a task, the config slot has a create on remove REQ task defn with a
    * calendar based deadline. The created task shall still use LASTEND scheduling rule
    *
    * @throws Exception
    */
   @Test
   public void itCreatesOnRemoveTaskWithLastEndScheduleRuleByTask() throws Exception {

      final PartNoKey trkPart = Domain.createPart( new DomainConfiguration<Part>() {

         @Override
         public void configure( Part part ) {
            part.setInventoryClass( RefInvClassKey.TRK );
            part.setShortDescription( "TRK PART" );
         }

      } );

      final AssemblyKey acftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aircraftAssembly ) {
                  aircraftAssembly
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot configurationSlot ) {
                              configurationSlot.addPosition( "ACFT_POS" );
                              configurationSlot.setCode( "ACFT" );
                              configurationSlot.addConfigurationSlot(
                                    new DomainConfiguration<ConfigurationSlot>() {

                                       @Override
                                       public void
                                             configure( ConfigurationSlot subConfigurationSlot ) {
                                          subConfigurationSlot.addPartGroup(
                                                new DomainConfiguration<PartGroup>() {

                                                   @Override
                                                   public void configure( PartGroup partGroup ) {
                                                      partGroup.setInventoryClass(
                                                            RefInvClassKey.TRK );
                                                      partGroup.addPart( trkPart );
                                                      partGroup.setCode( "TRK" );
                                                   }
                                                } );
                                          subConfigurationSlot.addPosition( "TRK_POS" );
                                          subConfigurationSlot.setCode( "TRK_SLOT" );
                                          subConfigurationSlot
                                                .setConfigurationSlotClass( RefBOMClassKey.TRK );
                                       }
                                    } );
                           }
                        } );
               }
            } );

      final ConfigSlotKey trkSlot = EqpAssmblBom.getBomItemKey( "ACFT", "TRK_SLOT" );
      final LocationKey location = Domain.createLocation( new DomainConfiguration<Location>() {

         @Override
         public void configure( Location location ) {
            location.setType( RefLocTypeKey.LINE );
         }
      } );

      final InventoryKey acft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aircraft ) {
            aircraft.setAssembly( acftAssembly );
            aircraft.setLocation( location );
         }
      } );

      final InventoryKey trk =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory trk ) {
                  trk.setPartNumber( trkPart );
                  trk.setLocation( location );
                  trk.setDescription( "TRK1" );
                  trk.setSerialNumber( "TRK_SER1" );
                  trk.setLastKnownConfigSlot( "ACFT", "TRK_SLOT", "TRK_POS" );
                  trk.setOwner( InvOwnerTable.getOwnerKey( "N/A" ) );
                  trk.setParent( acft );

               }
            } );

      Calendar cal = new GregorianCalendar();
      cal.set( 2015, 2, 17 );

      final Date effectiveDate = DateUtils.ceilDay( cal.getTime() );

      final TaskTaskKey taskRev1Key =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition reqDefn ) {
                  reqDefn.againstConfigurationSlot( trkSlot );
                  reqDefn.setScheduledFromEffectiveDate( effectiveDate );
                  reqDefn.setCreateOnRemove();
                  reqDefn.setCode( "REQ_DEFN" );
                  reqDefn.setRevisionNumber( 1 );
                  reqDefn.setStatus( ACTV );
                  reqDefn.addSchedulingRule( CDY, ONE );
               }
            } );

      final TaskKey task = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement actualTask ) {
            actualTask.setInventory( acft );
         }
      } );

      AttachableInventoryService service =
            new InventoryServiceFactory().getAttachableInventoryService( trk );

      service.detachInventory( task, location, hr, true, null, null );

      assertTaskDeadlineScheduleFromCode( taskRev1Key, RefSchedFromKey.LASTEND );
   }


   /**
    *
    * Ensure the attach inventory service does not auto-generate and install sub-components missing
    * on a component that is being installed.
    *
    * <pre>
    * Given an engine has a condition of REPREQ. And the engine is missing a sub-component. And an
    * aircraft exists which the engine may be installed. When the engine is attempted to be
    * installed on the aircraft. Then the engine is successfully installed. And the missing
    * sub-component is NOT auto generated nor installed.
    *
    * <pre>
    *
    * @throws Exception
    */
   @Test
   public void itDoesNotFillInHolesWhenAttachingInventory() throws Exception {

      //
      // Given an engine has a condition of REPREQ and the engine is missing a sub-component.
      //

      // Create an engine assembly with a sub-config slot.
      final AssemblyKey engineAssembly =
            Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

               @Override
               public void configure( EngineAssembly engAssy ) {
                  engAssy.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot rootCS ) {
                        rootCS.setCode( ENG_ROOT_CS );
                        rootCS.addConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot subCS ) {
                              subCS.setCode( ENG_SUB_CS );
                              subCS.addPosition( ENG_SUB_CS_POSITION_ONE );
                              subCS.setConfigurationSlotClass( TRK );
                              subCS.addPartGroup( new DomainConfiguration<PartGroup>() {

                                 @Override
                                 public void configure( PartGroup partGroup ) {
                                    partGroup.setInventoryClass( RefInvClassKey.TRK );
                                    partGroup.addPart( new DomainConfiguration<Part>() {

                                       @Override
                                       public void configure( Part part ) {
                                          part.setInventoryClass( RefInvClassKey.TRK );
                                          part.setPartStatus( RefPartStatusKey.ACTV );
                                       }
                                    } );
                                 }
                              } );
                           }
                        } );
                     }
                  } );
               }
            } );

      // Get the config slots for the engine's root and sub-component.
      final ConfigSlotKey engRootConfigSlot = getConfigSlot( engineAssembly, ENG_ROOT_CS );
      final ConfigSlotKey engSubConfigSlot = getConfigSlot( engineAssembly, ENG_SUB_CS );

      // Create an engine part.
      final PartNoKey enginePart = Domain.createPart();

      // Create an engine based on the assembly but with NO sub-component in the sub-config slot.
      final InventoryKey engine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine engine ) {
            engine.setAssembly( engineAssembly );
            engine.setCondition( REPREQ );
            engine.setOwner( OWNER );
            engine.setPartNumber( enginePart );
            engine.setPosition( new ConfigSlotPositionKey( engRootConfigSlot, 1 ) );
            engine.setLocation( Domain.createLocation( new DomainConfiguration<Location>() {

               @Override
               public void configure( Location location ) {
                  location.setType( LINE );
               }
            } ) );
         }
      } );

      //
      // Given an aircraft exists which the engine may be installed.
      //

      // Create an aircraft assembly with a config slot into which an engine part may be installed.
      final AssemblyKey aircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly acftAssy ) {
                  acftAssy.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot rootCS ) {
                        rootCS.setCode( ACFT_ROOT_CS );

                        // Add a SUBASSY config slot with one position for an engine.
                        rootCS.addConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot engCS ) {
                              engCS.setCode( ACFT_ENG_CS );
                              engCS.addPosition( ACFT_ENG_CS_POSITION_ONE );
                              engCS.setConfigurationSlotClass( SUBASSY );
                              engCS.addPartGroup( new DomainConfiguration<PartGroup>() {

                                 @Override
                                 public void configure( PartGroup partGroup ) {
                                    partGroup.addPart( enginePart );
                                    partGroup.setInventoryClass( RefInvClassKey.ASSY );
                                 }
                              } );
                           }
                        } );
                     }
                  } );
               }
            } );

      // Create an aircraft on which the engine will be attempted to be installed.
      InventoryKey aircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aircraft ) {
            aircraft.setAssembly( aircraftAssembly );
         }
      } );

      // Get the aircraft assembly's engine config slot, position, and part group.
      ConfigSlotKey acftEngConfigSlot = getConfigSlot( aircraftAssembly, ACFT_ENG_CS );
      ConfigSlotPositionKey acftEngConfigSlotPos =
            getConfigSlotPosition( acftEngConfigSlot, ACFT_ENG_CS_POSITION_ONE );
      PartGroupKey acftEngConfigSlotPartGroup = getFirstPartGroup( acftEngConfigSlot );

      //
      // When the engine is attempted to be installed on the aircraft.
      //

      // Create an AttachableInventoryService to test against.
      AttachableInventoryService service =
            new InventoryServiceFactory().getAttachableInventoryService( engine );

      service.attachTrackedInventory( aircraft, acftEngConfigSlotPos, acftEngConfigSlotPartGroup,
            null, null, false, null, null, false, false, false, false, false );

      //
      // Then the engine is successfully installed.
      //
      Assert.assertEquals( "Expected the next-highest inventory of the engine to be the aircraft.",
            aircraft, InvInvTable.findByPrimaryKey( engine ).getNhInvNo() );

      //
      // Then the missing sub-component is NOT auto generated nor installed.
      //
      // (search for inventory whose parent inventory is the engine and
      // whose config slot is the sub-component config slot)
      //
      DataSetArgument args = new DataSetArgument();
      args.add( engSubConfigSlot, "assmbl_db_id", "assmbl_cd", "assmbl_bom_id" );
      args.add( engine, "nh_inv_no_db_id", "nh_inv_no_id" );
      QuerySet qs = QuerySetFactory.getInstance().executeQueryTable( "inv_inv", args );
      Assert.assertTrue( "Sub-component unexpectedly created and attached to engine.",
            qs.isEmpty() );
   }


   /**
    * Verify that is an inventory is already installed that it is prevented from being installed
    * again.
    *
    * @throws Exception
    */
   @Test( expected = InstalledInventoryException.class )
   public void itPreventsInstallingCurrentlyInstalledInventory() throws Exception {

      // Given an engine assembly with a TRK sub-config slot.
      final AssemblyKey engAssy =
            Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

               @Override
               public void configure( EngineAssembly engAssy ) {
                  engAssy.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot rootCs ) {
                        rootCs.addConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot subCs ) {
                              subCs.setCode( ENG_SUB_CS );
                              subCs.addPosition( ENG_SUB_CS_POSITION_ONE );
                              subCs.addPartGroup( new DomainConfiguration<PartGroup>() {

                                 @Override
                                 public void configure( PartGroup subCsPg ) {
                                    subCsPg.setInventoryClass( RefInvClassKey.TRK );
                                 }
                              } );
                           }
                        } );
                     }
                  } );
               }
            } );

      // Given an engine with an installed TRK inventory.
      final InventoryKey trk = Domain.createTrackedInventory();
      Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine engine ) {
            engine.setAssembly( engAssy );
            engine.addTracked( trk );
         }
      } );

      // Given another engine on which the TRK inventory may be installed.
      final InventoryKey engine2 = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine engine ) {
            engine.setAssembly( engAssy );
            // Note: no TRK sub-component.
         }
      } );

      // Get the config slot and position into which the TRK inventory could be installed.
      ConfigSlotKey targetConfigSlot = getConfigSlot( engAssy, ENG_SUB_CS );
      ConfigSlotPositionKey targetConfigSlotPos =
            getConfigSlotPosition( targetConfigSlot, ENG_SUB_CS_POSITION_ONE );
      PartGroupKey targetPartGroup = getFirstPartGroup( targetConfigSlot );

      // When the installed TRK inventory is attempted to be installed again.
      // Then an expected InstalledInventoryException is thrown.
      // (see method's expected annotation)
      AttachableInventoryService service =
            new InventoryServiceFactory().getAttachableInventoryService( trk );

      service.attachTrackedInventory( engine2, targetConfigSlotPos, targetPartGroup, null, null,
            false, null, null, true, true, false, false, false );

   }


   @Test
   public void
         whenAttachTrackedInventoryWithTaskToBeCancelledOnInstallThenTaskTerminatedEventShouldBePublished()
               throws Exception {

      // ARRANGE
      // create a part
      PartNoKey enginePart = new PartNoBuilder().build();

      // create the aircraft assembly
      AssemblyKey acftAssembly = new AssemblyBuilder( "ACFT" ).build();

      // create the aircraft root config slot and position
      ConfigSlotKey acftConfigSlot =
            new ConfigSlotBuilder( "ACFT" ).withRootAssembly( acftAssembly ).build();
      ConfigSlotPositionKey acftPosition =
            new ConfigSlotPositionBuilder().withConfigSlot( acftConfigSlot ).build();

      // create the engine system config slot and position
      ConfigSlotKey engineSystemSlot =
            new ConfigSlotBuilder( "ENGINE_SYS" ).withRootAssembly( acftAssembly )
                  .withParent( acftConfigSlot ).withClass( RefBOMClassKey.SYS ).build();
      ConfigSlotPositionKey engineSystemPosition = new ConfigSlotPositionBuilder()
            .withConfigSlot( engineSystemSlot ).withParentPosition( acftPosition ).build();

      // create the config slot for the engine subassembly
      ConfigSlotKey engineSubAssySlot =
            new ConfigSlotBuilder( "ENGINE_SUBASSY" ).withClass( RefBOMClassKey.SUBASSY )
                  .withParent( engineSystemSlot ).withRootAssembly( acftAssembly ).build();
      ConfigSlotPositionKey engineSubAssyPosition = new ConfigSlotPositionBuilder()
            .withConfigSlot( engineSubAssySlot ).withParentPosition( engineSystemPosition ).build();

      // create a part group for the engine in the subassembly config slot
      PartGroupKey enginePartGroup =
            new PartGroupDomainBuilder( "ENGINE" ).withConfigSlot( engineSubAssySlot )
                  .withPartNo( enginePart ).withInventoryClass( RefInvClassKey.ASSY ).build();

      // create the engine assembly
      AssemblyKey engineAssembly = new AssemblyBuilder( "ENGINE" ).build();

      // create the engine config slot and position
      ConfigSlotKey engineSlot =
            new ConfigSlotBuilder( "ENGINE" ).withRootAssembly( engineAssembly ).build();
      ConfigSlotPositionKey enginePosition =
            new ConfigSlotPositionBuilder().withConfigSlot( engineSlot ).build();

      // create the aircraft
      InventoryKey acft = new InventoryBuilder().withConfigSlotPosition( acftPosition )
            .withClass( RefInvClassKey.ACFT ).build();

      // create the engine system
      InventoryKey engineSystem = new InventoryBuilder().withClass( RefInvClassKey.SYS )
            .withParentInventory( acft ).withAssemblyInventory( acft )
            .withConfigSlotPosition( engineSystemPosition ).withReleaseNumber( "TestReleaseNum" )
            .withReleaseDate( new GregorianCalendar( 2000, 0, 1, 12, 30, 45 ).getTime() )
            .withReleaseRemarks( "Test Release Remarks" ).build();

      // create a task definition with "terminated on completion" following tasks
      final TaskTaskKey reqDefn = Domain.createRequirementDefinition( requirementDefinition -> {
         requirementDefinition.againstConfigurationSlot( engineSlot );
         requirementDefinition.setStatus( RefTaskDefinitionStatusKey.ACTV );
         requirementDefinition.setCancelOnAnyInstall( true );
         requirementDefinition.setCancelOnInstallAircraft( true );
         requirementDefinition.setTaskClass( RefTaskClassKey.REQ );
      } );

      // create the engine
      InventoryKey engine =
            new InventoryBuilder().withPartNo( enginePart ).withConfigSlotPosition( enginePosition )
                  .withClass( RefInvClassKey.ASSY ).isIssued().build();

      // create a system under the engine
      InventoryKey system = new InventoryBuilder().withClass( RefInvClassKey.SYS )
            .withParentInventory( engine ).build();

      // create the tracked inventory
      InventoryKey trackedInventory = new InventoryBuilder().withClass( RefInvClassKey.TRK )
            .withParentInventory( system ).build();

      // create the task on the tracked inventory
      final TaskKey task = Domain.createRequirement( req -> {
         req.setDefinition( reqDefn );
         req.setInventory( trackedInventory );
         req.setStatus( RefEventStatusKey.ACTV );
      } );

      // ACT
      // create the service class instance
      AttachableInventoryService service =
            new InventoryServiceFactory().getAttachableInventoryService( engine );
      // attach the engine to the aircraft at the subassembly position
      service.attachTrackedInventory( engineSystem, engineSubAssyPosition, enginePartGroup, null,
            hr, false, null, null, true, false, false, false, false );

      // ASSERT
      assertEquals( 1, eventBus.getEventMessages().size() );
      assertEquals( task,
            ( ( TaskTerminatedEvent ) eventBus.getEventMessages().get( 0 ).getPayload() )
                  .getTaskKey() );

   }


   @Test
   public void
         whenAttachUnTrackedInventoryWithTaskToBeCancelledOnInstallThenTaskTerminatedEventShouldBePublished()
               throws Exception {

      // ARRANGE
      // create an inventory owner
      OwnerKey owner = new OwnerDomainBuilder().build();

      // create a location
      LocationKey location = Domain.createLocation();

      // create a part
      PartNoKey enginePart = new PartNoBuilder().build();

      // create the aircraft assembly
      AssemblyKey acftAssembly = new AssemblyBuilder( "ACFT" ).build();

      // create the aircraft root config slot and position
      ConfigSlotKey acftConfigSlot =
            new ConfigSlotBuilder( "ACFT" ).withRootAssembly( acftAssembly ).build();
      ConfigSlotPositionKey acftPosition =
            new ConfigSlotPositionBuilder().withConfigSlot( acftConfigSlot ).build();

      // create the engine system config slot and position
      ConfigSlotKey engineSystemSlot =
            new ConfigSlotBuilder( "ENGINE_SYS" ).withRootAssembly( acftAssembly )
                  .withParent( acftConfigSlot ).withClass( RefBOMClassKey.SYS ).build();
      ConfigSlotPositionKey engineSystemPosition = new ConfigSlotPositionBuilder()
            .withConfigSlot( engineSystemSlot ).withParentPosition( acftPosition ).build();

      // create the config slot for the engine subassembly
      ConfigSlotKey engineSubAssySlot =
            new ConfigSlotBuilder( "ENGINE_SUBASSY" ).withClass( RefBOMClassKey.SUBASSY )
                  .withParent( engineSystemSlot ).withRootAssembly( acftAssembly ).build();
      ConfigSlotPositionKey engineSubAssyPosition = new ConfigSlotPositionBuilder()
            .withConfigSlot( engineSubAssySlot ).withParentPosition( engineSystemPosition ).build();

      // create a part group for the engine in the subassembly config slot
      PartGroupKey enginePartGroup =
            new PartGroupDomainBuilder( "ENGINE" ).withConfigSlot( engineSubAssySlot )
                  .withPartNo( enginePart ).withInventoryClass( RefInvClassKey.ASSY ).build();

      // create the engine assembly
      AssemblyKey engineAssembly = new AssemblyBuilder( "ENGINE" ).build();

      // create the engine config slot and position
      ConfigSlotKey engineSlot =
            new ConfigSlotBuilder( "ENGINE" ).withRootAssembly( engineAssembly ).build();
      ConfigSlotPositionKey enginePosition =
            new ConfigSlotPositionBuilder().withConfigSlot( engineSlot ).build();

      // create the aircraft
      InventoryKey acft = new InventoryBuilder().withConfigSlotPosition( acftPosition )
            .withClass( RefInvClassKey.ACFT ).build();

      // create the engine system
      InventoryKey engineSystem = new InventoryBuilder().withClass( RefInvClassKey.SYS )
            .withParentInventory( acft ).withAssemblyInventory( acft )
            .withConfigSlotPosition( engineSystemPosition ).withReleaseNumber( "TestReleaseNum" )
            .withReleaseDate( new GregorianCalendar( 2000, 0, 1, 12, 30, 45 ).getTime() )
            .withReleaseRemarks( "Test Release Remarks" ).build();

      // create a task definition with "terminated on completion" following tasks
      final TaskTaskKey reqDefn = Domain.createRequirementDefinition( requirementDefinition -> {
         requirementDefinition.againstConfigurationSlot( engineSlot );
         requirementDefinition.setStatus( RefTaskDefinitionStatusKey.ACTV );
         requirementDefinition.setCancelOnAnyInstall( true );
         requirementDefinition.setCancelOnInstallAircraft( true );
         requirementDefinition.setTaskClass( RefTaskClassKey.REQ );
      } );

      // create the engine
      InventoryKey engine = new InventoryBuilder().withPartNo( enginePart )
            .withConfigSlotPosition( enginePosition ).withClass( RefInvClassKey.ASSY )
            .withOwner( owner ).isIssued().atLocation( location ).build();

      // create a system under the engine
      InventoryKey system = new InventoryBuilder().withClass( RefInvClassKey.SYS )
            .withParentInventory( engine ).build();

      // create the batch Inventory inventory
      InventoryKey batchInventory = new InventoryBuilder().withClass( RefInvClassKey.BATCH )
            .withParentInventory( system ).build();

      // create the task on the batch inventory
      final TaskKey task = Domain.createRequirement( req -> {
         req.setDefinition( reqDefn );
         req.setInventory( batchInventory );
         req.setStatus( RefEventStatusKey.ACTV );
      } );

      // ACT
      // create the service class instance
      AttachableInventoryService service =
            new InventoryServiceFactory().getAttachableInventoryService( engine );
      service.attachUntrackedInventory( engineSystem, task, HumanResourceKey.ADMIN, false, false );

      // ASSERT
      assertEquals( 1, eventBus.getEventMessages().size() );
      assertEquals( task,
            ( ( TaskTerminatedEvent ) eventBus.getEventMessages().get( 0 ).getPayload() )
                  .getTaskKey() );

   }


   @Test
   public void
         whenDetachingAnInventorWithTaskToBeCancelledOnRemovalThenTaskTerminatedEventShouldBePublished()
               throws Exception {
      // create an inventory owner
      OwnerKey owner = new OwnerDomainBuilder().build();

      // create a location
      LocationKey location = Domain.createLocation();

      // create a part
      PartNoKey enginePart = new PartNoBuilder().build();

      // create the aircraft assembly
      AssemblyKey acftAssembly = new AssemblyBuilder( "ACFT" ).build();

      // create the aircraft root config slot and position
      ConfigSlotKey acftConfigSlot =
            new ConfigSlotBuilder( "ACFT" ).withRootAssembly( acftAssembly ).build();
      ConfigSlotPositionKey acftPosition =
            new ConfigSlotPositionBuilder().withConfigSlot( acftConfigSlot ).build();

      // create the engine system config slot and position
      ConfigSlotKey engineSystemSlot =
            new ConfigSlotBuilder( "ENGINE_SYS" ).withRootAssembly( acftAssembly )
                  .withParent( acftConfigSlot ).withClass( RefBOMClassKey.SYS ).build();
      ConfigSlotPositionKey engineSystemPosition = new ConfigSlotPositionBuilder()
            .withConfigSlot( engineSystemSlot ).withParentPosition( acftPosition ).build();

      // create the config slot for the engine subassembly
      ConfigSlotKey engineSubAssySlot =
            new ConfigSlotBuilder( "ENGINE_SUBASSY" ).withClass( RefBOMClassKey.SUBASSY )
                  .withParent( engineSystemSlot ).withRootAssembly( acftAssembly ).build();
      ConfigSlotPositionKey engineSubAssyPosition = new ConfigSlotPositionBuilder()
            .withConfigSlot( engineSubAssySlot ).withParentPosition( engineSystemPosition ).build();

      // create a part group for the engine in the subassembly config slot
      new PartGroupDomainBuilder( "ENGINE" ).withConfigSlot( engineSubAssySlot )
            .withPartNo( enginePart ).withInventoryClass( RefInvClassKey.ASSY ).build();

      // create the enigne assembly
      AssemblyKey engineAssembly = new AssemblyBuilder( "ENGINE" ).build();

      // create the engine config slot and position
      ConfigSlotKey engineSlot =
            new ConfigSlotBuilder( "ENGINE" ).withRootAssembly( engineAssembly ).build();
      ConfigSlotPositionKey enginePosition =
            new ConfigSlotPositionBuilder().withConfigSlot( engineSlot ).build();

      // create the config slot and position for the inventory under the tracked inentory
      ConfigSlotKey subTrkSlot = new ConfigSlotBuilder( "SUB_TRK" ).build();
      ConfigSlotPositionKey subTrkPosition =
            new ConfigSlotPositionBuilder().withConfigSlot( subTrkSlot ).build();

      // create the aircraft
      InventoryKey acft = new InventoryBuilder().withConfigSlotPosition( acftPosition )
            .withClass( RefInvClassKey.ACFT ).build();

      // create the engine system
      InventoryKey engineSystem = new InventoryBuilder().withClass( RefInvClassKey.SYS )
            .withParentInventory( acft ).withAssemblyInventory( acft )
            .withConfigSlotPosition( engineSystemPosition ).build();

      // create the engine
      InventoryKey engine = new InventoryBuilder().withPartNo( enginePart )
            .withConfigSlotPosition( engineSubAssyPosition ).withParentInventory( engineSystem )
            .withOriginalAssembly( engineAssembly ).withClass( RefInvClassKey.ASSY )
            .withOwner( owner ).isIssued().atLocation( location ).build();

      // create a system under the engine
      InventoryKey system = new InventoryBuilder().withClass( RefInvClassKey.SYS )
            .withParentInventory( engine ).build();

      // create the tracked inventory
      InventoryKey trk = new InventoryBuilder().withClass( RefInvClassKey.TRK )
            .withParentInventory( system ).build();

      // create the task on the tracked inventory
      TaskKey trkTask = new TaskBuilder().onInventory( trk ).build();

      // create a part requirement on the task against the sub-tracked item position
      TaskPartKey partRequirement = new PartRequirementDomainBuilder( trkTask )
            .forPosition( subTrkPosition ).withNextHighestPosition( engineSubAssyPosition ).build();

      // create a task definition with "terminated on completion" following tasks
      final TaskTaskKey reqDefn = Domain.createRequirementDefinition( requirementDefinition -> {
         requirementDefinition.againstConfigurationSlot( engineSlot );
         requirementDefinition.setStatus( RefTaskDefinitionStatusKey.ACTV );
         requirementDefinition.setCancelOnRemoveFromAircraft( true );
         requirementDefinition.setCancelOnRemoveFromAnyComponent( true );
         requirementDefinition.setTaskClass( RefTaskClassKey.REQ );
      } );

      // create the task on the inventory
      final TaskKey task = Domain.createRequirement( req -> {
         req.setDefinition( reqDefn );
         req.setInventory( engine );
         req.setStatus( RefEventStatusKey.ACTV );
      } );

      // create the service class instance
      AttachableInventoryService service =
            new InventoryServiceFactory().getAttachableInventoryService( engine );
      // detach the engine
      service.detachInventory( null, location, hr, false, null, null );

      // ASSERT
      assertEquals( 1, eventBus.getEventMessages().size() );
      assertEquals( task,
            ( ( TaskTerminatedEvent ) eventBus.getEventMessages().get( 0 ).getPayload() )
                  .getTaskKey() );
   }


   private void assertTaskDeadlineScheduleFromCode( TaskTaskKey taskRev1Key,
         RefSchedFromKey expectedScheduleFromCode ) {

      DataSetArgument whereArgs = new DataSetArgument();
      whereArgs.add( taskRev1Key, "task_db_id", "task_id" );
      QuerySet ds = QuerySetFactory.getInstance().executeQueryTable( "SCHED_STASK", whereArgs );
      ds.first();
      TaskKey taskKey = ds.getKey( TaskKey.class, "SCHED_DB_ID", "SCHED_ID" );

      EvtSchedDeadTable evtSchedDead = EvtSchedDeadTable.findByPrimaryKey( taskKey, CDY );
      RefSchedFromKey actualScheduleFromCode = evtSchedDead.getScheduledFrom();
      assertEquals( "Unexpected Schedule From Code", expectedScheduleFromCode,
            actualScheduleFromCode );

   }


   private void assertInvRemoveRecord( InventoryKey invKey, InventoryKey parentKey,
         InventoryKey assemblyKey, InventoryKey highestKey ) {

      DataSetArgument args = new DataSetArgument();
      args.add( invKey, InvInv.getKeyColumnNames() );
      QuerySet qs = QuerySetFactory.getInstance().executeQueryTable( "inv_remove", args );

      assertEquals( 1, qs.getRowCount() );

      qs.next();

      InventoryKey qsParentKey =
            qs.getKey( InventoryKey.class, InvInv.ColumnName.NH_INV_NO_DB_ID.toString(),
                  InvInv.ColumnName.NH_INV_NO_ID.toString() );
      InventoryKey qsAssemblyKey =
            qs.getKey( InventoryKey.class, InvInv.ColumnName.ASSMBL_INV_NO_DB_ID.toString(),
                  InvInv.ColumnName.ASSMBL_INV_NO_ID.toString() );
      InventoryKey qsHighestKey = qs.getKey( InventoryKey.class,
            InvInv.ColumnName.H_INV_NO_DB_ID.toString(), InvInv.ColumnName.H_INV_NO_ID.toString() );

      assertEquals( "Unexpected Parent for inventory ", parentKey, qsParentKey );
      assertEquals( "Unexpected Assembly for inventory ", assemblyKey, qsAssemblyKey );
      assertEquals( "Unexpected Highest for inventory ", highestKey, qsHighestKey );

   }


   private ConfigSlotKey getConfigSlot( AssemblyKey assembly, String configSlotCode ) {
      DataSetArgument args = new DataSetArgument();
      args.add( assembly, "assmbl_db_id", "assmbl_cd" );
      args.add( "assmbl_bom_cd", configSlotCode );

      QuerySet qs = QuerySetFactory.getInstance().executeQueryTable( "eqp_assmbl_bom", args );
      qs.next();

      return qs.getKey( ConfigSlotKey.class, "assmbl_db_id", "assmbl_cd", "assmbl_bom_id" );
   }


   private ConfigSlotPositionKey getConfigSlotPosition( ConfigSlotKey configSlot,
         String positionCode ) {
      DataSetArgument args = new DataSetArgument();
      args.add( configSlot, "assmbl_db_id", "assmbl_cd", "assmbl_bom_id" );
      args.add( "eqp_pos_cd", positionCode );
      QuerySet qs = QuerySetFactory.getInstance().executeQueryTable( "eqp_assmbl_pos", args );
      qs.next();
      return qs.getKey( ConfigSlotPositionKey.class, "assmbl_db_id", "assmbl_cd", "assmbl_bom_id",
            "assmbl_pos_id" );
   }


   private PartGroupKey getFirstPartGroup( ConfigSlotKey configSlot ) {
      DataSetArgument args = new DataSetArgument();
      args.add( configSlot, "assmbl_db_id", "assmbl_cd", "assmbl_bom_id" );
      QuerySet qs = QuerySetFactory.getInstance().executeQueryTable( "eqp_bom_part", args );
      qs.next();
      return qs.getKey( PartGroupKey.class, "bom_part_db_id", "bom_part_id" );
   }


   @Before
   public void dateSetUp() {
      hr = Domain.createHumanResource();
      eventBus = fakeGuiceDaoRule.select( RecordingEventBus.class ).get();
   }

}
