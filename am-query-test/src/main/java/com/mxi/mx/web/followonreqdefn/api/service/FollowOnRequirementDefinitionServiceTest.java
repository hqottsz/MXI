package com.mxi.mx.web.followonreqdefn.api.service;

import static com.mxi.mx.core.key.RefInvClassKey.ACFT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.dao.taskdefn.JdbcTaskDefinitionDao;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryDamageKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.eqp.EqpAssmblPos;
import com.mxi.mx.core.table.inv.InvDamageDao;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.sd.SdFaultDao;
import com.mxi.mx.core.table.task.TaskTaskTable;
import com.mxi.mx.domain.inventory.damage.InventoryDamage;
import com.mxi.mx.domain.maintenance.FollowOnRequirementDefinition;
import com.mxi.mx.repository.inventory.damage.JdbcInventoryDamageRepository;
import com.mxi.mx.web.jsp.controller.fault.service.DamageRecordUIService;


public class FollowOnRequirementDefinitionServiceTest {

   private final String SYSTEM_NAME = "SYSTEM_NAME";
   private final String CONFIG_SLOT_TRK_CODE = "TRACKED_CODE";
   private final String CONFIG_SLOT_SYS_CODE = "SYS_CODE";
   private final String CONFIG_SLOT_ROOT_CODE = "ROOT_CODE";
   private final String LOCATION_MDESC = "LOCATION";
   private final String ASSEMBLY_CODE = "A320";
   private final String TRK_PKG_CODE = "TRK";
   private final String CONFIG_SLOT_SUBASSY_CODE = "SUBASSY";
   private final String ENGINE_ASSEMBLY_CODE = "ENGINE";
   private final String CONFIG_SLOT_ROOT_CODE_ENGINE = "ENGINE_ROOT_CODE";

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /*
    * Description: Retrieve the follow on requirements associated with all root and sys config slots
    * of the assembly
    *
    * Given I am a line maintenance technician
    *
    * When I view a completed fault with no damage record
    *
    * Then I can only search for follow-on tasks to the fault that are defined against any SYS
    * config slot on the aircraft assembly and the ROOT config slot
    */
   @Test
   public void itGetsFollowOnRequirementDefinitionsForAllRootAndSysConfigSlots() throws Exception {

      /* Setup */
      final PartNoKey lAircraftPartRoot = Domain.createPart();

      final AssemblyKey lAircraftAssembly =
            createAircraftAssemblyWithSysConfigSlot( lAircraftPartRoot );

      final InventoryKey lAssemblyInventoryRoot =
            createAircraftInventory( lAircraftPartRoot, lAircraftAssembly );

      final ConfigSlotKey lRootConfigSlot = Domain.readRootConfigurationSlot( lAircraftAssembly );
      final ConfigSlotKey lSysConfigSlot =
            Domain.readSubConfigurationSlot( lRootConfigSlot, CONFIG_SLOT_SYS_CODE );

      final TaskTaskKey lFollowOnTaskDefinitionRoot =
            createFollowOnTaskDefinition( lRootConfigSlot );
      final TaskTaskKey lFollowOnTaskDefinitionSys = createFollowOnTaskDefinition( lSysConfigSlot );

      FaultKey lFaultKey = Domain.createFault( aFault -> {
         aFault.setInventory( lAssemblyInventoryRoot );

      } );

      FollowOnRequirementDefinitionService lFollowOnRequirementDefinitionService =
            new FollowOnRequirementDefinitionService( new JdbcTaskDefinitionDao(),
                  new DamageRecordUIService() );
      HumanResourceKey lHrKey = Domain.createHumanResource();

      UUID lAssemblyInventoryRootAltId =
            InvInvTable.findByPrimaryKey( lAssemblyInventoryRoot ).getAlternateKey();
      SdFaultDao lJdbcSdFaultDao = InjectorContainer.get().getInstance( SdFaultDao.class );
      UUID lFaultAltId = lJdbcSdFaultDao.findByPrimaryKey( lFaultKey ).getAlternateKey();

      /* Execution */
      List<FollowOnRequirementDefinition> lFollowOnRequirementDefinitions =
            lFollowOnRequirementDefinitionService.getFollowOnTaskDefinitionsForInventoryAndFault(
                  lAssemblyInventoryRootAltId, lFaultAltId, lHrKey );

      /* Assertion */
      assertEquals( "Ensures both FOLLOW ON tasks are retrieved",
            lFollowOnRequirementDefinitions.size(), 2 );

      // Will use this list of UUIDs to compare Task UUIDs
      List<UUID> lFollowOnRequirementDefinitionIds =
            new ArrayList<>( lFollowOnRequirementDefinitions.size() );
      for ( FollowOnRequirementDefinition def : lFollowOnRequirementDefinitions ) {
         lFollowOnRequirementDefinitionIds.add( def.getId() );
      }

      UUID lFollowOnTaskDefinitionRootId =
            TaskTaskTable.findByPrimaryKey( lFollowOnTaskDefinitionRoot ).getAlternateKey();

      UUID lFollowOnTaskDefinitionSysId =
            TaskTaskTable.findByPrimaryKey( lFollowOnTaskDefinitionSys ).getAlternateKey();

      assertTrue( "Ensures the result contains the FOLLOW ON task on the root config slot",
            lFollowOnRequirementDefinitionIds.contains( lFollowOnTaskDefinitionRootId ) );
      assertTrue( "Ensures the result contains the FOLLOW ON task on the sys config slot",
            lFollowOnRequirementDefinitionIds.contains( lFollowOnTaskDefinitionSysId ) );
   }


   /*
    * Description: With a damage record on the aircraft, retrieve the follow on requirements
    * associated with all root and sys config slots of the assembly
    *
    * Given I am a line maintenance technician
    *
    * When I view a completed fault with a damage record associated to the aircraft
    *
    * Then I can only search for follow-on tasks to the fault that are defined against any SYS
    * config slot on the aircraft assembly and defined against the ROOT config slot
    */
   @Test
   public void
         itGetsFollowOnRequirementDefinitionsForAllRootAndSysConfigSlotsWithDamageRecordOnAircraft()
               throws Exception {

      /* Setup */
      final PartNoKey lAircraftPartRoot = Domain.createPart();
      final PartNoKey lTrkedPart = Domain.createPart();

      final AssemblyKey lAircraftAssembly =
            createAircraftAssemblyWithTrackedConfigSlot( lAircraftPartRoot, lTrkedPart );

      final InventoryKey lAssemblyInventoryRoot =
            createAircraftInventory( lAircraftPartRoot, lAircraftAssembly );

      final ConfigSlotKey lRootConfigSlot = Domain.readRootConfigurationSlot( lAircraftAssembly );
      final ConfigSlotKey lSysConfigSlot =
            Domain.readSubConfigurationSlot( lRootConfigSlot, CONFIG_SLOT_SYS_CODE );
      final ConfigSlotKey lTrkConfigSlot =
            Domain.readSubConfigurationSlot( lRootConfigSlot, CONFIG_SLOT_TRK_CODE );

      final TaskTaskKey lFollowOnTaskDefinitionRoot =
            createFollowOnTaskDefinition( lRootConfigSlot );
      final TaskTaskKey lFollowOnTaskDefinitionSys = createFollowOnTaskDefinition( lSysConfigSlot );
      final TaskTaskKey lFollowOnTaskDefinitionTrk = createFollowOnTaskDefinition( lTrkConfigSlot );

      FaultKey lFaultKey = Domain.createFault( aFault -> {
         aFault.setInventory( lAssemblyInventoryRoot );

      } );

      FollowOnRequirementDefinitionService lFollowOnRequirementDefinitionService =
            new FollowOnRequirementDefinitionService( new JdbcTaskDefinitionDao(),
                  new DamageRecordUIService() );
      HumanResourceKey lHrKey = Domain.createHumanResource();

      UUID lAssemblyInventoryRootAltId =
            InvInvTable.findByPrimaryKey( lAssemblyInventoryRoot ).getAlternateKey();
      SdFaultDao lJdbcSdFaultDao = InjectorContainer.get().getInstance( SdFaultDao.class );
      UUID lFaultAltId = lJdbcSdFaultDao.findByPrimaryKey( lFaultKey ).getAlternateKey();

      JdbcInventoryDamageRepository lJdbcInventoryDamageDao = new JdbcInventoryDamageRepository();
      InvDamageDao lJdbcDamageDao = InjectorContainer.get().getInstance( InvDamageDao.class );
      InventoryDamageKey lInventoryDamageKey = lJdbcDamageDao.generatePrimaryKey();
      UUID lInventoryDamageAltId = lJdbcDamageDao.generateAltId();

      InventoryDamage lInventoryDamage = InventoryDamage.builder().key( lInventoryDamageKey )
            .altId( lInventoryDamageAltId ).locationDescription( LOCATION_MDESC )
            .inventoryKey( lAssemblyInventoryRoot ).faultKey( lFaultKey ).build();

      lJdbcInventoryDamageDao.create( lInventoryDamage );

      /* Execution */
      List<FollowOnRequirementDefinition> lFollowOnRequirementDefinitions =
            lFollowOnRequirementDefinitionService.getFollowOnTaskDefinitionsForInventoryAndFault(
                  lAssemblyInventoryRootAltId, lFaultAltId, lHrKey );

      /* Assertion */
      assertEquals( "Ensures only 2 FOLLOW ON tasks are retrieved", 2,
            lFollowOnRequirementDefinitions.size() );

      // Will use this list of UUIDs to compare Task UUIDs
      List<UUID> lFollowOnRequirementDefinitionIds =
            new ArrayList<>( lFollowOnRequirementDefinitions.size() );
      for ( FollowOnRequirementDefinition def : lFollowOnRequirementDefinitions ) {
         lFollowOnRequirementDefinitionIds.add( def.getId() );
      }

      UUID lFollowOnTaskDefinitionRootId =
            TaskTaskTable.findByPrimaryKey( lFollowOnTaskDefinitionRoot ).getAlternateKey();

      UUID lFollowOnTaskDefinitionSysId =
            TaskTaskTable.findByPrimaryKey( lFollowOnTaskDefinitionSys ).getAlternateKey();

      assertTrue( "Ensures the result contains the FOLLOW ON task on the root config slot",
            lFollowOnRequirementDefinitionIds.contains( lFollowOnTaskDefinitionRootId ) );
      assertTrue( "Ensures the result contains the FOLLOW ON task on the sys config slot",
            lFollowOnRequirementDefinitionIds.contains( lFollowOnTaskDefinitionSysId ) );
   }


   /*
    * Description: With a damage record on a TRK component, retrieve the follow on requirements
    * associated with all root and sys config slots of the assembly and any requirements on the same
    * component as the damage record.
    *
    * Given I am a line maintenance technician
    *
    * When I view a completed fault with a damage record associated to a component
    *
    * Then I can search for follow-on tasks to the fault that are defined against any SYS config
    * slot on the aircraft assembly, defined against the ROOT config slot, and only follow-on tasks
    * defined against the TRK config slot of the component specified in the damage record
    */
   @Test
   public void
         itGetsFollowOnRequirementDefinitionsForAllRootSysAndTrkConfigSlotsWithDamageRecordOnTrackedPart()
               throws Exception {

      /* Setup */
      final PartNoKey lAircraftPartRoot = Domain.createPart();
      final PartNoKey lTrackedPart = Domain.createPart();

      final AssemblyKey lAircraftAssembly =
            createAircraftAssemblyWithTrackedConfigSlot( lAircraftPartRoot, lTrackedPart );

      final InventoryKey lAssemblyInventoryRoot =
            createAircraftInventory( lAircraftPartRoot, lAircraftAssembly );

      final ConfigSlotKey lRootConfigSlot = Domain.readRootConfigurationSlot( lAircraftAssembly );
      final ConfigSlotKey lSysConfigSlot =
            Domain.readSubConfigurationSlot( lRootConfigSlot, CONFIG_SLOT_SYS_CODE );
      final ConfigSlotKey lTrkConfigSlot =
            Domain.readSubConfigurationSlot( lRootConfigSlot, CONFIG_SLOT_TRK_CODE );

      final ConfigSlotPositionKey lTrkConfigSlotPositionKey = new ConfigSlotPositionKey(
            lTrkConfigSlot, EqpAssmblPos.getFirstPosId( lTrkConfigSlot ) );
      final String lTrkPosCd = EqpAssmblPos.getPosCd( lTrkConfigSlotPositionKey );

      final InventoryKey lTrackedInventory = Domain.createTrackedInventory( aTrackedInventory -> {
         aTrackedInventory.setPartNumber( lTrackedPart );
         aTrackedInventory.setLocation( Domain.createLocation() );
         aTrackedInventory.setParent( lAssemblyInventoryRoot );
         aTrackedInventory.setLastKnownConfigSlot( lTrkConfigSlotPositionKey.getCd(),
               CONFIG_SLOT_TRK_CODE, lTrkPosCd );
      } );

      final TaskTaskKey lFollowOnTaskDefinitionRoot =
            createFollowOnTaskDefinition( lRootConfigSlot );
      final TaskTaskKey lFollowOnTaskDefinitionSys = createFollowOnTaskDefinition( lSysConfigSlot );
      final TaskTaskKey lFollowOnTaskDefinitionTrk = createFollowOnTaskDefinition( lTrkConfigSlot );

      FaultKey lFaultKey = Domain.createFault( aFault -> {
         aFault.setInventory( lTrackedInventory );

      } );

      FollowOnRequirementDefinitionService lFollowOnRequirementDefinitionService =
            new FollowOnRequirementDefinitionService( new JdbcTaskDefinitionDao(),
                  new DamageRecordUIService() );
      HumanResourceKey lHrKey = Domain.createHumanResource();

      UUID lAssemblyInventoryRootAltId =
            InvInvTable.findByPrimaryKey( lAssemblyInventoryRoot ).getAlternateKey();
      SdFaultDao lJdbcSdFaultDao = InjectorContainer.get().getInstance( SdFaultDao.class );
      UUID lFaultAltId = lJdbcSdFaultDao.findByPrimaryKey( lFaultKey ).getAlternateKey();

      JdbcInventoryDamageRepository lJdbcInventoryDamageDao = new JdbcInventoryDamageRepository();
      InvDamageDao lJdbcDamageDao = InjectorContainer.get().getInstance( InvDamageDao.class );
      InventoryDamageKey lInventoryDamageKey = lJdbcDamageDao.generatePrimaryKey();
      UUID lInventoryDamageAltId = lJdbcDamageDao.generateAltId();

      InventoryDamage lInventoryDamage = InventoryDamage.builder().key( lInventoryDamageKey )
            .altId( lInventoryDamageAltId ).locationDescription( LOCATION_MDESC )
            .inventoryKey( lTrackedInventory ).faultKey( lFaultKey ).build();

      lJdbcInventoryDamageDao.create( lInventoryDamage );

      /* Execution */
      List<FollowOnRequirementDefinition> lFollowOnRequirementDefinitions =
            lFollowOnRequirementDefinitionService.getFollowOnTaskDefinitionsForInventoryAndFault(
                  lAssemblyInventoryRootAltId, lFaultAltId, lHrKey );

      /* Assertion */
      assertEquals( "Ensures all 3 FOLLOW ON tasks are retrieved",
            lFollowOnRequirementDefinitions.size(), 3 );

      // Will use this list of UUIDs to compare Task UUIDs
      List<UUID> lFollowOnRequirementDefinitionIds =
            new ArrayList<>( lFollowOnRequirementDefinitions.size() );
      for ( FollowOnRequirementDefinition def : lFollowOnRequirementDefinitions ) {
         lFollowOnRequirementDefinitionIds.add( def.getId() );
      }

      UUID lFollowOnTaskDefinitionRootId =
            TaskTaskTable.findByPrimaryKey( lFollowOnTaskDefinitionRoot ).getAlternateKey();

      UUID lFollowOnTaskDefinitionSysId =
            TaskTaskTable.findByPrimaryKey( lFollowOnTaskDefinitionSys ).getAlternateKey();

      UUID lFollowOnTaskDefinitionTrkId =
            TaskTaskTable.findByPrimaryKey( lFollowOnTaskDefinitionTrk ).getAlternateKey();

      assertTrue( "Ensures the result contains the FOLLOW ON task on the root config slot",
            lFollowOnRequirementDefinitionIds.contains( lFollowOnTaskDefinitionRootId ) );
      assertTrue( "Ensures the result contains the FOLLOW ON task on the sys config slot",
            lFollowOnRequirementDefinitionIds.contains( lFollowOnTaskDefinitionSysId ) );
      assertTrue( "Ensures the result contains the FOLLOW ON task on the trk config slot",
            lFollowOnRequirementDefinitionIds.contains( lFollowOnTaskDefinitionTrkId ) );
   }


   /*
    * Description: With a damage record on a TRK engine sub component, retrieve the follow on
    * requirements associated with all root and sys config slots of the assembly and any
    * requirements on the same trk slot as the component in the damage record.
    *
    * Given I am a line maintenance technician
    *
    * When I view a completed fault with a damage record associated to an engine sub component
    *
    * Then I can search for follow-on tasks to the fault that are defined against any SYS config
    * slot on the aircraft assembly, defined against the ROOT config slot, and only follow-on tasks
    * defined against the TRK config slot of the engine sub component specified in the damage record
    */
   @Test
   public void
         itGetsFollowOnRequirementDefinitionsForAllRootSysAndTrkConfigSlotsWithDamageRecordOnTrackedEnginePart()
               throws Exception {

      /* Setup */
      final PartNoKey lAircraftPartRoot = Domain.createPart();
      final PartNoKey lTrackedPart = Domain.createPart();
      final PartNoKey lEnginePart = Domain.createPart();

      final AssemblyKey lAircraftAssembly =
            createAircraftAssemblyWithSubAssyConfigSlot( lAircraftPartRoot, lEnginePart );
      final AssemblyKey lEngineAssembly =
            createEngineAssemblyWithTrkSlot( lEnginePart, lTrackedPart );

      final InventoryKey lAssemblyRootInventory =
            createAircraftInventory( lAircraftPartRoot, lAircraftAssembly );

      final ConfigSlotKey lRootConfigSlot = Domain.readRootConfigurationSlot( lAircraftAssembly );
      final ConfigSlotKey lEngineRootConfigSlot =
            Domain.readRootConfigurationSlot( lEngineAssembly );
      final ConfigSlotKey lSysConfigSlot =
            Domain.readSubConfigurationSlot( lRootConfigSlot, CONFIG_SLOT_SYS_CODE );
      final ConfigSlotKey lTrkConfigSlot =
            Domain.readSubConfigurationSlot( lEngineRootConfigSlot, CONFIG_SLOT_TRK_CODE );

      final ConfigSlotPositionKey lTrkConfigSlotPositionKey = new ConfigSlotPositionKey(
            lTrkConfigSlot, EqpAssmblPos.getFirstPosId( lTrkConfigSlot ) );
      final String lTrkPosCd = EqpAssmblPos.getPosCd( lTrkConfigSlotPositionKey );

      final InventoryKey lEngineInventory = Domain.createEngine( aEngineInventory -> {
         aEngineInventory.setPartNumber( lEnginePart );
         aEngineInventory.setLocation( Domain.createLocation() );
         aEngineInventory.setAssembly( lAircraftAssembly );
         aEngineInventory.setParent( lAssemblyRootInventory );
         aEngineInventory.setOriginalAssembly( lEngineAssembly );
      } );

      final InventoryKey lTrackedInventory = Domain.createTrackedInventory( aTrackedInventory -> {
         aTrackedInventory.setPartNumber( lTrackedPart );
         aTrackedInventory.setLocation( Domain.createLocation() );
         aTrackedInventory.setParent( lEngineInventory );
         aTrackedInventory.setLastKnownConfigSlot( lTrkConfigSlotPositionKey.getCd(),
               CONFIG_SLOT_TRK_CODE, lTrkPosCd );
      } );

      final TaskTaskKey lFollowOnTaskDefinitionRoot =
            createFollowOnTaskDefinition( lRootConfigSlot );
      final TaskTaskKey lFollowOnTaskDefinitionSys = createFollowOnTaskDefinition( lSysConfigSlot );
      final TaskTaskKey lFollowOnTaskDefinitionTrk = createFollowOnTaskDefinition( lTrkConfigSlot );

      FaultKey lFaultKey = Domain.createFault( aFault -> {
         aFault.setInventory( lAssemblyRootInventory );

      } );

      FollowOnRequirementDefinitionService lFollowOnRequirementDefinitionService =
            new FollowOnRequirementDefinitionService( new JdbcTaskDefinitionDao(),
                  new DamageRecordUIService() );
      HumanResourceKey lHrKey = Domain.createHumanResource();

      UUID lAssemblyRootInventoryAltId =
            InvInvTable.findByPrimaryKey( lAssemblyRootInventory ).getAlternateKey();
      SdFaultDao lJdbcSdFaultDao = InjectorContainer.get().getInstance( SdFaultDao.class );
      UUID lFaultAltId = lJdbcSdFaultDao.findByPrimaryKey( lFaultKey ).getAlternateKey();

      JdbcInventoryDamageRepository lJdbcInventoryDamageDao = new JdbcInventoryDamageRepository();
      InvDamageDao lJdbcDamageDao = InjectorContainer.get().getInstance( InvDamageDao.class );
      InventoryDamageKey lInventoryDamageKey = lJdbcDamageDao.generatePrimaryKey();
      UUID lInventoryDamageAltId = lJdbcDamageDao.generateAltId();

      InventoryDamage lInventoryDamage = InventoryDamage.builder().key( lInventoryDamageKey )
            .altId( lInventoryDamageAltId ).locationDescription( LOCATION_MDESC )
            .inventoryKey( lTrackedInventory ).faultKey( lFaultKey ).build();

      lJdbcInventoryDamageDao.create( lInventoryDamage );

      /* Execution */
      List<FollowOnRequirementDefinition> lFollowOnRequirementDefinitions =
            lFollowOnRequirementDefinitionService.getFollowOnTaskDefinitionsForInventoryAndFault(
                  lAssemblyRootInventoryAltId, lFaultAltId, lHrKey );

      /* Assertion */
      assertEquals( "Ensures all 3 FOLLOW ON tasks are retrieved",
            lFollowOnRequirementDefinitions.size(), 3 );

      // Will use this list of UUIDs to compare Task UUIDs
      List<UUID> lFollowOnRequirementDefinitionIds =
            new ArrayList<>( lFollowOnRequirementDefinitions.size() );
      for ( FollowOnRequirementDefinition def : lFollowOnRequirementDefinitions ) {
         lFollowOnRequirementDefinitionIds.add( def.getId() );
      }

      UUID lFollowOnTaskDefinitionRootId =
            TaskTaskTable.findByPrimaryKey( lFollowOnTaskDefinitionRoot ).getAlternateKey();

      UUID lFollowOnTaskDefinitionSysId =
            TaskTaskTable.findByPrimaryKey( lFollowOnTaskDefinitionSys ).getAlternateKey();

      UUID lFollowOnTaskDefinitionTrkId =
            TaskTaskTable.findByPrimaryKey( lFollowOnTaskDefinitionTrk ).getAlternateKey();

      assertTrue( "Ensures the result contains the FOLLOW ON task on the root config slot",
            lFollowOnRequirementDefinitionIds.contains( lFollowOnTaskDefinitionRootId ) );
      assertTrue( "Ensures the result contains the FOLLOW ON task on the sys config slot",
            lFollowOnRequirementDefinitionIds.contains( lFollowOnTaskDefinitionSysId ) );
      assertTrue(
            "Ensures the result contains the FOLLOW ON task on the trk engine sub component config slot",
            lFollowOnRequirementDefinitionIds.contains( lFollowOnTaskDefinitionTrkId ) );
   }


   /*
    * Description: With a damage record on the engine, retrieve the follow on requirements
    * associated with all root and sys config slots of the aircraft assembly and any requirements on
    * the root of the engine assembly or the ACFT's engine sub assembly.
    *
    * Given I am a line maintenance technician
    *
    * When I view a completed fault with a damage record on an engine
    *
    * Then I can search for follow-on tasks to the fault that are defined against any SYS config
    * slot on the aircraft assembly, defined against the ROOT config slot, defined against the
    * aircraft's engine SUBASSY config slot, and follow-on tasks defined against the root config
    * slot of the engine assembly
    */
   @Test
   public void
         itGetsFollowOnRequirementDefinitionForACFTEngineSubAssyAndEngineRootConfigSlotsWithDamageRecordOnEngine()
               throws Exception {

      /* Setup */
      final PartNoKey lAircraftPartRoot = Domain.createPart();
      final PartNoKey lEnginePart = Domain.createPart();

      final AssemblyKey lAircraftAssembly =
            createAircraftAssemblyWithSubAssyConfigSlot( lAircraftPartRoot, lEnginePart );

      final AssemblyKey lEngineAssembly = createEngineAssembly( lEnginePart );

      final InventoryKey lAssemblyInventoryRoot =
            createAircraftInventory( lAircraftPartRoot, lAircraftAssembly );

      final ConfigSlotKey lRootConfigSlot = Domain.readRootConfigurationSlot( lAircraftAssembly );
      final ConfigSlotKey lSysConfigSlot =
            Domain.readSubConfigurationSlot( lRootConfigSlot, CONFIG_SLOT_SYS_CODE );
      final ConfigSlotKey lSubAssyConfigSlot =
            Domain.readSubConfigurationSlot( lRootConfigSlot, CONFIG_SLOT_SUBASSY_CODE );
      final ConfigSlotKey lEngineRootConfigSlot =
            Domain.readRootConfigurationSlot( lEngineAssembly );

      final InventoryKey lEngineInventory = Domain.createEngine( aEngineInventory -> {
         aEngineInventory.setPartNumber( lEnginePart );
         aEngineInventory.setLocation( Domain.createLocation() );
         aEngineInventory.setParent( lAssemblyInventoryRoot );
         aEngineInventory.setOriginalAssembly( lEngineAssembly );
         aEngineInventory.setPosition( new ConfigSlotPositionKey( lSubAssyConfigSlot, 1 ) );
      } );

      final TaskTaskKey lFollowOnTaskDefinitionRoot =
            createFollowOnTaskDefinition( lRootConfigSlot );
      final TaskTaskKey lFollowOnTaskDefinitionSys = createFollowOnTaskDefinition( lSysConfigSlot );
      final TaskTaskKey lFollowOnTaskDefinitionSubAssy =
            createFollowOnTaskDefinition( lSubAssyConfigSlot );
      final TaskTaskKey lFollowOnTaskDefinitionEngineRoot =
            createFollowOnTaskDefinition( lEngineRootConfigSlot );

      FaultKey lFaultKey = Domain.createFault( aFault -> {
         aFault.setInventory( lAssemblyInventoryRoot );

      } );

      FollowOnRequirementDefinitionService lFollowOnRequirementDefinitionService =
            new FollowOnRequirementDefinitionService( new JdbcTaskDefinitionDao(),
                  new DamageRecordUIService() );
      HumanResourceKey lHrKey = Domain.createHumanResource();

      JdbcInventoryDamageRepository lJdbcInventoryDamageDao = new JdbcInventoryDamageRepository();
      InvDamageDao lJdbcDamageDao = InjectorContainer.get().getInstance( InvDamageDao.class );
      InventoryDamageKey lInventoryDamageKey = lJdbcDamageDao.generatePrimaryKey();
      UUID lInventoryDamageAltId = lJdbcDamageDao.generateAltId();

      InventoryDamage lInventoryDamage = InventoryDamage.builder().key( lInventoryDamageKey )
            .altId( lInventoryDamageAltId ).locationDescription( LOCATION_MDESC )
            .inventoryKey( lEngineInventory ).faultKey( lFaultKey ).build();

      lJdbcInventoryDamageDao.create( lInventoryDamage );

      UUID lAssemblyInventoryRootAltId =
            InvInvTable.findByPrimaryKey( lAssemblyInventoryRoot ).getAlternateKey();

      SdFaultDao lJdbcSdFaultDao = InjectorContainer.get().getInstance( SdFaultDao.class );
      UUID lFaultAltId = lJdbcSdFaultDao.findByPrimaryKey( lFaultKey ).getAlternateKey();

      /* Execution */
      List<FollowOnRequirementDefinition> lFollowOnRequirementDefinitions =
            lFollowOnRequirementDefinitionService.getFollowOnTaskDefinitionsForInventoryAndFault(
                  lAssemblyInventoryRootAltId, lFaultAltId, lHrKey );

      /* Assertion */
      assertEquals( "Ensures all 3 FOLLOW ON tasks are retrieved", 4,
            lFollowOnRequirementDefinitions.size() );

      // Will use this list of UUIDs to compare Task UUIDs
      List<UUID> lFollowOnRequirementDefinitionIds =
            new ArrayList<>( lFollowOnRequirementDefinitions.size() );
      for ( FollowOnRequirementDefinition def : lFollowOnRequirementDefinitions ) {
         lFollowOnRequirementDefinitionIds.add( def.getId() );
      }

      UUID lFollowOnTaskDefinitionRootSubAssyId =
            TaskTaskTable.findByPrimaryKey( lFollowOnTaskDefinitionSubAssy ).getAlternateKey();

      UUID lFollowOnTaskDefinitionRootId =
            TaskTaskTable.findByPrimaryKey( lFollowOnTaskDefinitionRoot ).getAlternateKey();

      UUID lFollowOnTaskDefinitionSysId =
            TaskTaskTable.findByPrimaryKey( lFollowOnTaskDefinitionSys ).getAlternateKey();

      UUID lFollowOnTaskDefinitioneEngineRootId =
            TaskTaskTable.findByPrimaryKey( lFollowOnTaskDefinitionEngineRoot ).getAlternateKey();

      assertTrue(
            "Ensures the result contains the FOLLOW ON task on the root sub assembly config slot",
            lFollowOnRequirementDefinitionIds.contains( lFollowOnTaskDefinitionRootSubAssyId ) );
      assertTrue( "Ensures the result contains the FOLLOW ON task on the root config slot",
            lFollowOnRequirementDefinitionIds.contains( lFollowOnTaskDefinitionRootId ) );
      assertTrue( "Ensures the result contains the FOLLOW ON task on the sys config slot",
            lFollowOnRequirementDefinitionIds.contains( lFollowOnTaskDefinitionSysId ) );
      assertTrue(
            "Ensures the result contains the FOLLOW ON task on the root config slot of the engine assembly",
            lFollowOnRequirementDefinitionIds.contains( lFollowOnTaskDefinitioneEngineRootId ) );
   }


   /* Helper Method to Create FollowOn requirement definitions */
   private TaskTaskKey createFollowOnTaskDefinition( final ConfigSlotKey aConfigSlot ) {
      return Domain.createRequirementDefinition( aRequirementDefinition -> {
         aRequirementDefinition.againstConfigurationSlot( aConfigSlot );
         aRequirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
         aRequirementDefinition.setOnCondition( true );
      } );
   }


   private InventoryKey createAircraftInventory( final PartNoKey aAircraftPart,
         final AssemblyKey aAssembly ) {
      return Domain.createAircraft( aAircraft -> {
         aAircraft.setAssembly( aAssembly );
         aAircraft.setPart( aAircraftPart );
         aAircraft.addSystem( SYSTEM_NAME );
         aAircraft.setLocation( Domain.createLocation() );
      } );
   }


   private AssemblyKey createEngineAssembly( final PartNoKey aEnginePart ) {
      return Domain.createEngineAssembly( aEngineAssembly -> {
         aEngineAssembly.setCode( ENGINE_ASSEMBLY_CODE );
         aEngineAssembly.setRootConfigurationSlot( aRootConfigSlot -> {
            aRootConfigSlot.setCode( CONFIG_SLOT_ROOT_CODE_ENGINE );
            aRootConfigSlot.setConfigurationSlotClass( RefBOMClassKey.ROOT );
            aRootConfigSlot.addPartGroup( aRootCsPartGroup -> {
               aRootCsPartGroup.setInventoryClass( RefInvClassKey.ASSY );
               aRootCsPartGroup.addPart( aEnginePart );
            } );
         } );
      } );
   }


   private AssemblyKey createEngineAssemblyWithTrkSlot( final PartNoKey aEnginePart,
         final PartNoKey aTrkPart ) {
      return Domain.createEngineAssembly( aEngineAssembly -> {
         aEngineAssembly.setCode( ENGINE_ASSEMBLY_CODE );
         aEngineAssembly.setRootConfigurationSlot( aRootConfigSlot -> {
            aRootConfigSlot.setCode( CONFIG_SLOT_ROOT_CODE_ENGINE );
            aRootConfigSlot.setConfigurationSlotClass( RefBOMClassKey.ROOT );
            aRootConfigSlot.addPartGroup( aRootCsPartGroup -> {
               aRootCsPartGroup.setInventoryClass( RefInvClassKey.ASSY );
               aRootCsPartGroup.addPart( aEnginePart );
            } );
            aRootConfigSlot.addConfigurationSlot( aSysConfigSlot -> {
               aSysConfigSlot.setCode( CONFIG_SLOT_SYS_CODE );
               aSysConfigSlot.setConfigurationSlotClass( RefBOMClassKey.SYS );
               aSysConfigSlot.addPartGroup( aSysCsPartGroup -> {
                  aSysCsPartGroup.setInventoryClass( RefInvClassKey.SYS );
               } );
               aSysConfigSlot.addConfigurationSlot( aTrkedConfigSlot -> {
                  aTrkedConfigSlot.setConfigurationSlotClass( RefBOMClassKey.TRK );
                  aTrkedConfigSlot.setCode( CONFIG_SLOT_TRK_CODE );
                  aTrkedConfigSlot.addPartGroup( aTrkedCsPartGroup -> {
                     aTrkedCsPartGroup.setCode( TRK_PKG_CODE );
                     aTrkedCsPartGroup.setInventoryClass( RefInvClassKey.TRK );
                     aTrkedCsPartGroup.addPart( aTrkPart );
                  } );
               } );
            } );
         } );
      } );
   }


   private AssemblyKey createAircraftAssemblyWithSubAssyConfigSlot( final PartNoKey aAircraft,
         final PartNoKey aEngine ) {
      return Domain.createAircraftAssembly( aAircraftAssembly -> {

         aAircraftAssembly.setCode( ASSEMBLY_CODE );
         aAircraftAssembly.setRootConfigurationSlot( aRootConfigSlot -> {

            aRootConfigSlot.setCode( CONFIG_SLOT_ROOT_CODE );
            aRootConfigSlot.addPartGroup( aRootCsPartGroup -> {
               aRootCsPartGroup.setInventoryClass( ACFT );
               aRootCsPartGroup.addPart( aAircraft );
            } );

            aRootConfigSlot.addConfigurationSlot( aSysConfigSlot -> {
               aSysConfigSlot.setCode( CONFIG_SLOT_SYS_CODE );
               aSysConfigSlot.setConfigurationSlotClass( RefBOMClassKey.SYS );
               aSysConfigSlot.addPartGroup( aSysCsPartGroup -> {
                  aSysCsPartGroup.setInventoryClass( RefInvClassKey.SYS );
               } );

               aSysConfigSlot.addConfigurationSlot( aSubAssySlot -> {
                  aSubAssySlot.setCode( CONFIG_SLOT_SUBASSY_CODE );
                  aSubAssySlot.setConfigurationSlotClass( RefBOMClassKey.SUBASSY );
                  aSubAssySlot.addPartGroup( aSubAssyCsPartGroup -> {
                     aSubAssyCsPartGroup.setInventoryClass( RefInvClassKey.ASSY );
                     aSubAssyCsPartGroup.addPart( aEngine );
                  } );
               } );
            } );
         } );
      } );
   }


   private AssemblyKey createAircraftAssemblyWithTrackedConfigSlot( final PartNoKey aAircraftPart,
         final PartNoKey aTrkPart ) {
      return Domain.createAircraftAssembly( aAircraftAssembly -> {

         aAircraftAssembly.setCode( ASSEMBLY_CODE );
         aAircraftAssembly.setRootConfigurationSlot( aRootConfigSlot -> {

            aRootConfigSlot.setCode( CONFIG_SLOT_ROOT_CODE );
            aRootConfigSlot.addPartGroup( aRootCsPartGroup -> {
               aRootCsPartGroup.setInventoryClass( ACFT );
               aRootCsPartGroup.addPart( aAircraftPart );
            } );

            aRootConfigSlot.addConfigurationSlot( aSysConfigSlot -> {
               aSysConfigSlot.setCode( CONFIG_SLOT_SYS_CODE );
               aSysConfigSlot.setConfigurationSlotClass( RefBOMClassKey.SYS );
               aSysConfigSlot.addPartGroup( aSysCsPartGroup -> {
                  aSysCsPartGroup.setInventoryClass( RefInvClassKey.SYS );
               } );

               aSysConfigSlot.addConfigurationSlot( aTrkedConfigSlot -> {
                  aTrkedConfigSlot.setConfigurationSlotClass( RefBOMClassKey.TRK );
                  aTrkedConfigSlot.setCode( CONFIG_SLOT_TRK_CODE );
                  aTrkedConfigSlot.addPartGroup( aTrkedCsPartGroup -> {
                     aTrkedCsPartGroup.setCode( TRK_PKG_CODE );
                     aTrkedCsPartGroup.setInventoryClass( RefInvClassKey.TRK );
                     aTrkedCsPartGroup.addPart( aTrkPart );
                  } );
               } );
            } );
         } );
      } );
   }


   private AssemblyKey createAircraftAssemblyWithSysConfigSlot( final PartNoKey aAircraftPart ) {
      return Domain.createAircraftAssembly( aAssembly -> {
         aAssembly.setCode( ASSEMBLY_CODE );
         aAssembly.setRootConfigurationSlot( aRootConfigSlot -> {
            aRootConfigSlot.setCode( CONFIG_SLOT_ROOT_CODE );
            aRootConfigSlot.addPartGroup( aRootCsPartGroup -> {
               aRootCsPartGroup.setInventoryClass( ACFT );
               aRootCsPartGroup.addPart( aAircraftPart );
            } );
            aRootConfigSlot.addConfigurationSlot( aSysConfigSlot -> {
               aSysConfigSlot.setCode( CONFIG_SLOT_SYS_CODE );
               aSysConfigSlot.setConfigurationSlotClass( RefBOMClassKey.SYS );
               aSysConfigSlot.addPartGroup( aSysCsPartGroup -> {
                  aSysCsPartGroup.setInventoryClass( RefInvClassKey.SYS );
               } );
            } );
         } );
      } );
   }
}
