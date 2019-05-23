package com.mxi.mx.core.services.fault;

import static com.mxi.mx.core.key.RefInvClassKey.ACFT;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.mockito.Mock;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.AircraftAssembly;
import com.mxi.am.domain.ConfigurationSlot;
import com.mxi.am.domain.CorrectiveTask;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.PartGroup;
import com.mxi.am.domain.RequirementDefinition;
import com.mxi.am.domain.WorkPackage;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.InventoryDamageKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefRelationTypeKey;
import com.mxi.mx.core.key.RefReschedFromKey;
import com.mxi.mx.core.key.RefSchedFromKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.plsql.delegates.PartProcedures;
import com.mxi.mx.core.plsql.delegates.TaskProcedures;
import com.mxi.mx.core.services.fault.damagerecord.impl.FaultMissingDamageRecordException;
import com.mxi.mx.core.services.inventory.oper.InventoryOperationalInterface;
import com.mxi.mx.core.services.taskdefn.TaskDefnUtils;
import com.mxi.mx.core.table.eqp.EqpAssmblPos;
import com.mxi.mx.core.table.evt.EvtEventDao;
import com.mxi.mx.core.table.evt.EvtSchedDeadTable;
import com.mxi.mx.core.table.evt.JdbcEvtEventDao;
import com.mxi.mx.core.table.inv.InvDamageDao;
import com.mxi.mx.core.table.sched.JdbcSchedStaskDao;
import com.mxi.mx.core.table.sched.SchedStaskDao;
import com.mxi.mx.core.table.sched.SchedStaskTable;
import com.mxi.mx.domain.inventory.damage.InventoryDamage;
import com.mxi.mx.repository.inventory.damage.JdbcInventoryDamageRepository;


@RunWith( BlockJUnit4ClassRunner.class )
public class FaultDetailsServiceTest {

   private static final BigDecimal MIN_FORECAST_RANGE = new BigDecimal( 30 );
   private static final BigDecimal REPEAT_INTERVAL = new BigDecimal( 10 );
   private static final BigDecimal THRESHOLD = new BigDecimal( 10 );
   private static final BigDecimal CURRENT_USAGE = new BigDecimal( 50 );
   private static final BigDecimal EFFECTIVE_DATE_USAGE = new BigDecimal( 25 );

   public static final boolean RECURRING = true;
   public static final boolean NOT_RECURRING = false;
   public static final boolean USE_RESCHED_RULE = false;
   public static final boolean USE_SCHED_RULE = true;

   public static final String SYSTEM_NAME = "SYSTEM_NAME";
   private final String CONFIG_SLOT_TRK_CODE = "TRACKED_CODE";
   private final String CONFIG_SLOT_SYS_CODE = "SYS_CODE";
   private final String CONFIG_SLOT_ROOT_CODE = "ROOT_CODE";
   private final String LOCATION_MDESC = "LOCATION";
   private final String ASSEMBLY_CODE = "A320";
   private final String TRK_PKG_CODE = "TRK";
   private final String ENGINE_ASSEMBLY_CODE = "ENGINE";
   private final String CONFIG_SLOT_ROOT_CODE_ENGINE = "ENGINE_ROOT_CODE";
   private final String CONFIG_SLOT_SUBASSY_CODE = "SUBASSY";

   private static final String DATE_FORMAT = "yyyy-MM-dd";
   private static final Date WP_START_DATE = DateUtils.addDays( new Date(), -7 );
   private static final Date EFFECTIVE_DATE = DateUtils.addDays( new Date(), -10 );
   private static final Date AIRCRAFT_MANUFACTURED_DATE = DateUtils.addDays( new Date(), -15 );

   static EvtEventDao sEvtEventDao = new JdbcEvtEventDao();
   static SchedStaskDao sSchedStaskDao = new JdbcSchedStaskDao();

   @Mock
   private InventoryOperationalInterface iMockOperationalService;
   @Mock
   private PartProcedures iMockPartProcedures;
   @Mock
   private TaskProcedures iMockTaskProcedures;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();
   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private FaultDetailsService iFaultDetailsService;


   /**
    * Description: Add automated test for the creation of follow-on tasks based on fault with damage
    * record associated to a component and the task definition selected is against TRK config slot,
    * in this case the task gets initialized against the damage record inventory
    *
    * Given I am a line maintenance technician
    *
    * When I add a follow-on task, defined against a TRK config slot, to a completed fault with a
    * damage record associated to a component
    *
    * Then the task will be initialized against the component specified in the damage record
    */
   @Test
   public void
         itCreatesFollowOnTaskForFaultWithDamageRecordOnTrkedComponentWithTaskDefinitionAgainstTrkedSlot()
               throws Exception {

      /* Setup */
      final PartNoKey lAircraftPart = Domain.createPart();
      final PartNoKey lTrackedPart = Domain.createPart();
      final AssemblyKey lAssembly =
            createAircraftAssemblyWithTrackedConfigSlot( lAircraftPart, lTrackedPart );

      final InventoryKey lAssemblyInventory = createAircraftInventory( lAircraftPart, lAssembly );
      final ConfigSlotKey lRootConfigSlot = Domain.readRootConfigurationSlot( lAssembly );
      final ConfigSlotKey lTrkConfigSlot =
            Domain.readSubConfigurationSlot( lRootConfigSlot, CONFIG_SLOT_TRK_CODE );

      final ConfigSlotPositionKey lTrkConfigSlotPositionKey = new ConfigSlotPositionKey(
            lTrkConfigSlot, EqpAssmblPos.getFirstPosId( lTrkConfigSlot ) );
      final String lTrkPosCd = EqpAssmblPos.getPosCd( lTrkConfigSlotPositionKey );
      final PartGroupKey lTrkPartGroup = Domain.readPartGroup( lTrkConfigSlot, TRK_PKG_CODE );

      final InventoryKey lTrackedInventory = Domain.createTrackedInventory( aTrackedInventory -> {
         aTrackedInventory.setPartNumber( lTrackedPart );
         aTrackedInventory.setPartGroup( lTrkPartGroup );
         aTrackedInventory.setLocation( Domain.createLocation() );
         aTrackedInventory.setParent( lAssemblyInventory );
         aTrackedInventory.setLastKnownConfigSlot( lTrkConfigSlotPositionKey.getCd(),
               CONFIG_SLOT_TRK_CODE, lTrkPosCd );

      } );

      FaultKey lFaultKey = Domain.createFault( aFault -> {
         aFault.setInventory( lTrackedInventory );
         aFault.setStatus( RefEventStatusKey.COMPLETE );
      } );

      TaskKey lCorrectiveTask = Domain.createCorrectiveTask( aCorrectiveTask -> {
         aCorrectiveTask.setInventory( lTrackedInventory );
         aCorrectiveTask.setStatus( RefEventStatusKey.COMPLETE );
         aCorrectiveTask.setFaultKey( lFaultKey );
      } );

      final TaskTaskKey lFollowOnTaskDefinitionTrk = createFollowOnTaskDefinition( lTrkConfigSlot );

      JdbcInventoryDamageRepository lJdbcInventoryDamageDao = new JdbcInventoryDamageRepository();
      InvDamageDao lJdbcDamageDao = InjectorContainer.get().getInstance( InvDamageDao.class );
      InventoryDamageKey lInventoryDamageKey = lJdbcDamageDao.generatePrimaryKey();
      UUID lInventoryDamageAltId = lJdbcDamageDao.generateAltId();

      InventoryDamage lInventoryDamage = InventoryDamage.builder().key( lInventoryDamageKey )
            .altId( lInventoryDamageAltId ).locationDescription( LOCATION_MDESC )
            .inventoryKey( lTrackedInventory ).faultKey( lFaultKey ).build();

      lJdbcInventoryDamageDao.create( lInventoryDamage );
      MxFaultDetailsService lMxFaultDetailsService = new MxFaultDetailsService();

      /* Execution */
      TaskKey lCreatedFollowOnTask = lMxFaultDetailsService.createFollowOnTaskFromDefinition(
            lCorrectiveTask, lAssembly, lAssemblyInventory, lFollowOnTaskDefinitionTrk, null );

      /* Assertion */
      SchedStaskDao lJdbcSchedStaskDao = InjectorContainer.get().getInstance( SchedStaskDao.class );
      SchedStaskTable ResultTask = lJdbcSchedStaskDao.findByPrimaryKey( lCreatedFollowOnTask );

      assertNotNull( "Asserts that a new FOLLOW ON task has been created", ResultTask );
      assertEquals(
            "Asserts that the new FOLLOW ON task is based on the task definition on the tracked config slot",
            lFollowOnTaskDefinitionTrk, ResultTask.getTaskTaskKey() );
      assertEquals(
            "Asserts that the new FOLLOW ON task is created on the tracked inventory defined in setup",
            lTrackedInventory, ResultTask.getMainInventory() );
   }


   /**
    * Description: Add automated test for the creation of follow-on tasks based on fault with damage
    * record associated to an engine and the task definition selected is against ACFT's sub assembly
    * config slot, in this case the task gets initialized against the damage record inventory
    *
    * Given I am a line maintenance technician
    *
    * When I add a follow-on task, defined against an aircraft's sub assembly config slot, to a
    * completed fault with a damage record associated to an engine
    *
    * Then the task will be initialized against the engine specified in the damage record
    */
   @Test
   public void
         itCreatesFollowOnTaskForFaultWithDamageRecordOnEngineWithTaskDefinitionAgainstACFTSubAssySlot()
               throws Exception {

      /* Setup */
      final PartNoKey lAircraftPart = Domain.createPart();
      final PartNoKey lEnginePart = Domain.createPart();

      final AssemblyKey lAssembly =
            createAircraftAssemblyWithSubAssyConfigSlot( lAircraftPart, lEnginePart );

      final InventoryKey lACFTInventory = createAircraftInventory( lAircraftPart, lAssembly );

      final ConfigSlotKey lRootConfigSlot = Domain.readRootConfigurationSlot( lAssembly );
      final ConfigSlotKey lSubAssyConfigSlot =
            Domain.readSubConfigurationSlot( lRootConfigSlot, CONFIG_SLOT_SUBASSY_CODE );

      final InventoryKey lEngineInventory = Domain.createEngine( aEngineInventory -> {
         aEngineInventory.setPartNumber( lEnginePart );
         aEngineInventory.setLocation( Domain.createLocation() );
         aEngineInventory.setParent( lACFTInventory );
         aEngineInventory.setPosition( new ConfigSlotPositionKey( lSubAssyConfigSlot, 1 ) );
      } );

      FaultKey lFaultKey = Domain.createFault( aFault -> {
         aFault.setInventory( lACFTInventory );
         aFault.setStatus( RefEventStatusKey.COMPLETE );
      } );

      TaskKey lCorrectiveTask = Domain.createCorrectiveTask( aCorrectiveTask -> {
         aCorrectiveTask.setInventory( lACFTInventory );
         aCorrectiveTask.setStatus( RefEventStatusKey.COMPLETE );
         aCorrectiveTask.setFaultKey( lFaultKey );
      } );

      final TaskTaskKey lFollowOnTaskDefinitionSubAssy =
            createFollowOnTaskDefinition( lSubAssyConfigSlot );

      JdbcInventoryDamageRepository lJdbcInventoryDamageDao = new JdbcInventoryDamageRepository();
      InvDamageDao lJdbcDamageDao = InjectorContainer.get().getInstance( InvDamageDao.class );
      InventoryDamageKey lInventoryDamageKey = lJdbcDamageDao.generatePrimaryKey();
      UUID lInventoryDamageAltId = lJdbcDamageDao.generateAltId();

      InventoryDamage lInventoryDamage = InventoryDamage.builder().key( lInventoryDamageKey )
            .altId( lInventoryDamageAltId ).locationDescription( LOCATION_MDESC )
            .inventoryKey( lEngineInventory ).faultKey( lFaultKey ).build();

      lJdbcInventoryDamageDao.create( lInventoryDamage );
      MxFaultDetailsService lMxFaultDetailsService = new MxFaultDetailsService();

      /* Execution */
      TaskKey lCreatedFollowOnTask = lMxFaultDetailsService.createFollowOnTaskFromDefinition(
            lCorrectiveTask, lAssembly, lACFTInventory, lFollowOnTaskDefinitionSubAssy, null );

      /* Assertion */
      SchedStaskDao lJdbcSchedStaskDao = InjectorContainer.get().getInstance( SchedStaskDao.class );
      SchedStaskTable ResultTask = lJdbcSchedStaskDao.findByPrimaryKey( lCreatedFollowOnTask );

      assertNotNull( "Asserts that a new FOLLOW ON task has been created", ResultTask );
      assertEquals(
            "Asserts that the new FOLLOW ON task is based on the task definition on the tracked config slot",
            lFollowOnTaskDefinitionSubAssy, ResultTask.getTaskTaskKey() );
      assertEquals(
            "Asserts that the new FOLLOW ON task is created on the tracked inventory defined in setup",
            lEngineInventory, ResultTask.getMainInventory() );
   }


   /**
    * Description: Add automated test for the creation of follow-on tasks based on fault with damage
    * record associated to an engine and the task definition selected is against engine's root
    * config slot, in this case the task gets initialized against the damage record inventory
    *
    * Given I am a line maintenance technician
    *
    * When I add a follow-on task, defined against an engine's root config slot, to a completed
    * fault with a damage record associated to an engine
    *
    * Then the task will be initialized against the engine specified in the damage record
    */
   @Test
   public void
         itCreatesFollowOnTaskForFaultWithDamageRecordOnEngineWithTaskDefinitionAgainstEngineRootConfigSlot()
               throws Exception {

      /* Setup */
      final PartNoKey lAircraftPart = Domain.createPart();
      final PartNoKey lEnginePart = Domain.createPart();

      final AssemblyKey lAssembly =
            createAircraftAssemblyWithSubAssyConfigSlot( lAircraftPart, lEnginePart );
      final AssemblyKey lEngineAssembly = createEngineAssembly( lEnginePart );

      final InventoryKey lACFTInventory = createAircraftInventory( lAircraftPart, lAssembly );

      final ConfigSlotKey lRootConfigSlot = Domain.readRootConfigurationSlot( lAssembly );
      final ConfigSlotKey lSubAssyConfigSlot =
            Domain.readSubConfigurationSlot( lRootConfigSlot, CONFIG_SLOT_SUBASSY_CODE );
      final ConfigSlotKey lEngineRootConfigSlot =
            Domain.readRootConfigurationSlot( lEngineAssembly );

      final InventoryKey lEngineInventory = Domain.createEngine( aEngineInventory -> {
         aEngineInventory.setPartNumber( lEnginePart );
         aEngineInventory.setAssembly( lAssembly );
         aEngineInventory.setParent( lACFTInventory );
         aEngineInventory.setOriginalAssembly( lEngineAssembly );
         aEngineInventory.setLocation( Domain.createLocation() );
         aEngineInventory.setPosition( new ConfigSlotPositionKey( lSubAssyConfigSlot, 1 ) );
      } );

      FaultKey lFaultKey = Domain.createFault( aFault -> {
         aFault.setInventory( lACFTInventory );
         aFault.setStatus( RefEventStatusKey.COMPLETE );
      } );

      TaskKey lCorrectiveTask = Domain.createCorrectiveTask( aCorrectiveTask -> {
         aCorrectiveTask.setInventory( lACFTInventory );
         aCorrectiveTask.setStatus( RefEventStatusKey.COMPLETE );
         aCorrectiveTask.setFaultKey( lFaultKey );
      } );

      final TaskTaskKey lFollowOnTaskDefinitionEngineRoot =
            createFollowOnTaskDefinition( lEngineRootConfigSlot );

      JdbcInventoryDamageRepository lJdbcInventoryDamageDao = new JdbcInventoryDamageRepository();
      InvDamageDao lJdbcDamageDao = InjectorContainer.get().getInstance( InvDamageDao.class );
      InventoryDamageKey lInventoryDamageKey = lJdbcDamageDao.generatePrimaryKey();
      UUID lInventoryDamageAltId = lJdbcDamageDao.generateAltId();

      InventoryDamage lInventoryDamage = InventoryDamage.builder().key( lInventoryDamageKey )
            .altId( lInventoryDamageAltId ).locationDescription( LOCATION_MDESC )
            .inventoryKey( lEngineInventory ).faultKey( lFaultKey ).build();

      lJdbcInventoryDamageDao.create( lInventoryDamage );
      MxFaultDetailsService lMxFaultDetailsService = new MxFaultDetailsService();

      /* Execution */
      TaskKey lCreatedFollowOnTask = lMxFaultDetailsService.createFollowOnTaskFromDefinition(
            lCorrectiveTask, lAssembly, lACFTInventory, lFollowOnTaskDefinitionEngineRoot, null );

      /* Assertion */
      SchedStaskDao lJdbcSchedStaskDao = InjectorContainer.get().getInstance( SchedStaskDao.class );
      SchedStaskTable ResultTask = lJdbcSchedStaskDao.findByPrimaryKey( lCreatedFollowOnTask );

      assertNotNull( "Asserts that a new FOLLOW ON task has been created", ResultTask );
      assertEquals(
            "Asserts that the new FOLLOW ON task is based on the task definition on the tracked config slot",
            lFollowOnTaskDefinitionEngineRoot, ResultTask.getTaskTaskKey() );
      assertEquals(
            "Asserts that the new FOLLOW ON task is created on the tracked inventory defined in setup",
            lEngineInventory, ResultTask.getMainInventory() );
   }


   /**
    * Description: Add automated test for the creation of follow-on tasks based on fault with damage
    * record associated to an engine sub component and the task definition selected is against a TRK
    * config slot on an engine's assembly, in this case the task gets initialized against the damage
    * record inventory
    *
    * Given I am a line maintenance technician
    *
    * When I add a follow-on task, defined against a TRK config slot on an engine's assembly, to a
    * completed fault with a damage record associated to an engine sub component
    *
    * Then the task will be initialized against the engine sub component specified in the damage
    * record
    */
   @Test
   public void
         itCreatesFollowOnTaskForFaultWithDamageRecordOnEngineSubComponentWithTaskDefinitionAgainstEngineTrkSlot()
               throws Exception {

      /* Setup */
      final PartNoKey lAircraftPart = Domain.createPart();
      final PartNoKey lEnginePart = Domain.createPart();
      final PartNoKey lTrkPart = Domain.createPart();

      final AssemblyKey lAssembly =
            createAircraftAssemblyWithSubAssyConfigSlot( lAircraftPart, lEnginePart );
      final AssemblyKey lEngineAssembly = createEngineAssemblyWithTrkSlot( lEnginePart, lTrkPart );

      final InventoryKey lACFTInventory = createAircraftInventory( lAircraftPart, lAssembly );

      final ConfigSlotKey lRootConfigSlot = Domain.readRootConfigurationSlot( lAssembly );
      final ConfigSlotKey lSubAssyConfigSlot =
            Domain.readSubConfigurationSlot( lRootConfigSlot, CONFIG_SLOT_SUBASSY_CODE );
      final ConfigSlotKey lEngineRootConfigSlot =
            Domain.readRootConfigurationSlot( lEngineAssembly );
      final ConfigSlotKey lEngineTrkConfigSlot =
            Domain.readSubConfigurationSlot( lEngineRootConfigSlot, CONFIG_SLOT_TRK_CODE );

      final ConfigSlotPositionKey lTrkConfigSlotPositionKey = new ConfigSlotPositionKey(
            lEngineTrkConfigSlot, EqpAssmblPos.getFirstPosId( lEngineTrkConfigSlot ) );
      final String lTrkPosCd = EqpAssmblPos.getPosCd( lTrkConfigSlotPositionKey );

      final InventoryKey lEngineInventory = Domain.createEngine( aEngineInventory -> {
         aEngineInventory.setPartNumber( lEnginePart );
         aEngineInventory.setAssembly( lAssembly );
         aEngineInventory.setParent( lACFTInventory );
         aEngineInventory.setOriginalAssembly( lEngineAssembly );
         aEngineInventory.setLocation( Domain.createLocation() );
         aEngineInventory.setPosition( new ConfigSlotPositionKey( lSubAssyConfigSlot, 1 ) );
      } );
      final InventoryKey lTrackedInventory = Domain.createTrackedInventory( aTrackedInventory -> {
         aTrackedInventory.setPartNumber( lTrkPart );
         aTrackedInventory.setLocation( Domain.createLocation() );
         aTrackedInventory.setParent( lEngineInventory );
         aTrackedInventory.setLastKnownConfigSlot( lTrkConfigSlotPositionKey.getCd(),
               CONFIG_SLOT_TRK_CODE, lTrkPosCd );
      } );

      FaultKey lFaultKey = Domain.createFault( aFault -> {
         aFault.setInventory( lACFTInventory );
         aFault.setStatus( RefEventStatusKey.COMPLETE );
      } );

      TaskKey lCorrectiveTask = Domain.createCorrectiveTask( aCorrectiveTask -> {
         aCorrectiveTask.setInventory( lACFTInventory );
         aCorrectiveTask.setStatus( RefEventStatusKey.COMPLETE );
         aCorrectiveTask.setFaultKey( lFaultKey );
      } );

      final TaskTaskKey lFollowOnTaskDefinitionEngineTrkComponent =
            createFollowOnTaskDefinition( lEngineTrkConfigSlot );

      JdbcInventoryDamageRepository lJdbcInventoryDamageDao = new JdbcInventoryDamageRepository();
      InvDamageDao lJdbcDamageDao = InjectorContainer.get().getInstance( InvDamageDao.class );
      InventoryDamageKey lInventoryDamageKey = lJdbcDamageDao.generatePrimaryKey();
      UUID lInventoryDamageAltId = lJdbcDamageDao.generateAltId();

      InventoryDamage lInventoryDamage = InventoryDamage.builder().key( lInventoryDamageKey )
            .altId( lInventoryDamageAltId ).locationDescription( LOCATION_MDESC )
            .inventoryKey( lTrackedInventory ).faultKey( lFaultKey ).build();

      lJdbcInventoryDamageDao.create( lInventoryDamage );
      MxFaultDetailsService lMxFaultDetailsService = new MxFaultDetailsService();

      /* Execution */
      TaskKey lCreatedFollowOnTask =
            lMxFaultDetailsService.createFollowOnTaskFromDefinition( lCorrectiveTask, lAssembly,
                  lACFTInventory, lFollowOnTaskDefinitionEngineTrkComponent, null );

      /* Assertion */
      SchedStaskDao lJdbcSchedStaskDao = InjectorContainer.get().getInstance( SchedStaskDao.class );
      SchedStaskTable ResultTask = lJdbcSchedStaskDao.findByPrimaryKey( lCreatedFollowOnTask );

      assertNotNull( "Asserts that a new FOLLOW ON task has been created", ResultTask );
      assertEquals(
            "Asserts that the new FOLLOW ON task is based on the task definition on the tracked config slot",
            lFollowOnTaskDefinitionEngineTrkComponent, ResultTask.getTaskTaskKey() );
      assertEquals(
            "Asserts that the new FOLLOW ON task is created on the tracked inventory defined in setup",
            lTrackedInventory, ResultTask.getMainInventory() );
   }


   /**
    * Description: Automated test for the creation of follow-on tasks based on fault with damage
    * record associated to the aircraft and the task definition selected is against ROOT config
    * slot, in this case the task gets initialized against the damage record inventory
    *
    * Given I am a line maintenance technician AND I view a completed fault with a damage record
    * associated to the aircraft
    *
    * When I go to add a FOLLOW ON task based on the assembly's root config slot
    *
    * Then I can create a FOLLOW ON tasks on the ACFT
    */
   @Test
   public void itCreatesFollowOnTaskForFaultOnACFTWithTaskDefinitionAgainstRootSlot()
         throws Exception {

      /* Setup */
      final PartNoKey lAircraftPart = Domain.createPart();

      final AssemblyKey lAssembly = createAircraftAssemblyWithSysConfigSlot( lAircraftPart );

      final InventoryKey lAssemblyInventory = createAircraftInventory( lAircraftPart, lAssembly );
      final ConfigSlotKey lRootConfigSlot = Domain.readRootConfigurationSlot( lAssembly );

      FaultKey lFaultKey = Domain.createFault( aFault -> {
         aFault.setInventory( lAssemblyInventory );
         aFault.setStatus( RefEventStatusKey.COMPLETE );
      } );

      TaskKey lCorrectiveTask = Domain.createCorrectiveTask( aCorrectiveTask -> {
         aCorrectiveTask.setInventory( lAssemblyInventory );
         aCorrectiveTask.setStatus( RefEventStatusKey.COMPLETE );
         aCorrectiveTask.setFaultKey( lFaultKey );
      } );

      final TaskTaskKey lFollowOnTaskDefinition = createFollowOnTaskDefinition( lRootConfigSlot );

      MxFaultDetailsService lMxFaultDetailsService = new MxFaultDetailsService();

      /* Execution */
      TaskKey lCreatedFollowOnTask = lMxFaultDetailsService.createFollowOnTaskFromDefinition(
            lCorrectiveTask, lAssembly, lAssemblyInventory, lFollowOnTaskDefinition, null );

      /* Assertion */
      SchedStaskDao lJdbcSchedStaskDao = InjectorContainer.get().getInstance( SchedStaskDao.class );
      SchedStaskTable ResultTask = lJdbcSchedStaskDao.findByPrimaryKey( lCreatedFollowOnTask );

      assertNotNull( "Asserts that a new FOLLOW ON task has been created", ResultTask );
      assertEquals(
            "Asserts that the new FOLLOW ON task is based on the task definition on the root config slot",
            lFollowOnTaskDefinition, ResultTask.getTaskTaskKey() );
      assertEquals( "Asserts that the new FOLLOW ON tasks inventory is the ACFT",
            lAssemblyInventory, ResultTask.getMainInventory() );
   }


   /**
    * Description: Add automated test for the creation of follow-on tasks based on fault with damage
    * record associated to a component and the task definition selected is against SYS config slot,
    * in this case the task gets initialized against the inventory corresponding to the passed
    * assembly inventory's sys config slot which matches the task definition's sys config slot.
    *
    * Given I am a line maintenance technician
    *
    * When I go to add a FOLLOW ON task based on the assembly's SYS config slot
    *
    * Then I can create a FOLLOW ON tasks on the System inventory
    */
   @Test
   public void itCreatesFollowOnTaskForFaultOnACFTWithTaskDefinitionAgainstSysSlot()
         throws Exception {

      /* Setup */
      final PartNoKey lAircraftPart = Domain.createPart();

      final AssemblyKey lAssembly = createAircraftAssemblyWithSysConfigSlot( lAircraftPart );

      final ConfigSlotKey lRootConfigSlot = Domain.readRootConfigurationSlot( lAssembly );
      final ConfigSlotKey lSysConfigSlot =
            Domain.readSubConfigurationSlot( lRootConfigSlot, CONFIG_SLOT_SYS_CODE );

      final InventoryKey lAssemblyInventory = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssembly( lAssembly );
         aAircraft.setPart( lAircraftPart );
         aAircraft.addSystem( aSystem -> {
            aSystem.setName( SYSTEM_NAME );
            aSystem.setPosition( lSysConfigSlot, 1 );
         } );
      } );
      final InventoryKey lSysInventory = Domain.readSystem( lAssemblyInventory, SYSTEM_NAME );

      FaultKey lFaultKey = Domain.createFault( aFault -> {
         aFault.setFailedSystem( lRootConfigSlot );
         aFault.setInventory( lAssemblyInventory );
         aFault.setStatus( RefEventStatusKey.COMPLETE );
      } );

      TaskKey lCorrectiveTask = Domain.createCorrectiveTask( aCorrectiveTask -> {
         aCorrectiveTask.setInventory( lAssemblyInventory );
         aCorrectiveTask.setFaultKey( lFaultKey );
         aCorrectiveTask.setStatus( RefEventStatusKey.COMPLETE );
      } );

      final TaskTaskKey lFollowOnTaskDefinition = createFollowOnTaskDefinition( lSysConfigSlot );

      MxFaultDetailsService lMxFaultDetailsService = new MxFaultDetailsService();

      /* Execution */
      TaskKey lCreatedFollowOnTask = lMxFaultDetailsService.createFollowOnTaskFromDefinition(
            lCorrectiveTask, lAssembly, lAssemblyInventory, lFollowOnTaskDefinition, null );

      /* Assertion */
      SchedStaskDao lJdbcSchedStaskDao = InjectorContainer.get().getInstance( SchedStaskDao.class );
      SchedStaskTable ResultTask = lJdbcSchedStaskDao.findByPrimaryKey( lCreatedFollowOnTask );

      assertNotNull( "Asserts that a new FOLLOW ON task has been created", ResultTask );
      assertEquals(
            "Asserts that the new FOLLOW ON task is based on the task definition on the sys config slot",
            lFollowOnTaskDefinition, ResultTask.getTaskTaskKey() );
      assertEquals( "Asserts that the new FOLLOW ON tasks inventory is the Sys inventory",
            lSysInventory, ResultTask.getMainInventory() );
   }


   /**
    * Description: Add automated test for the creation of follow-on tasks based on fault with no
    * damage record and the task definition selected is against TRK config slot, in this case no
    * task should get initialized and an error should be thrown by the existing code.
    *
    * Given I am a line maintenance technician AND I view a completed fault with no damage record
    *
    * When I go to add a FOLLOW ON task based on a TRK config slot on the assembly
    *
    * Then I get an FaultMissingDamageRecordException
    */
   @Test( expected = FaultMissingDamageRecordException.class )
   public void
         itDoesNotCreateFollowOnTaskForFaultWithNoDamageRecordOnTrkedComponentWithTaskDefinitionAgainstTrkedSlot()
               throws Exception {

      /* Setup */
      final PartNoKey lAircraftPart = Domain.createPart();
      final PartNoKey lTrackedPart = Domain.createPart();
      final AssemblyKey lAssembly =
            createAircraftAssemblyWithTrackedConfigSlot( lAircraftPart, lTrackedPart );

      final InventoryKey lAssemblyInventory = createAircraftInventory( lAircraftPart, lAssembly );
      final ConfigSlotKey lRootConfigSlot = Domain.readRootConfigurationSlot( lAssembly );
      final ConfigSlotKey lTrkConfigSlot =
            Domain.readSubConfigurationSlot( lRootConfigSlot, CONFIG_SLOT_TRK_CODE );

      FaultKey lFaultKey = Domain.createFault( aFault -> {
         aFault.setInventory( lAssemblyInventory );
         aFault.setStatus( RefEventStatusKey.COMPLETE );
      } );

      TaskKey lCorrectiveTask = Domain.createCorrectiveTask( aCorrectiveTask -> {
         aCorrectiveTask.setInventory( lAssemblyInventory );
         aCorrectiveTask.setStatus( RefEventStatusKey.COMPLETE );
         aCorrectiveTask.setFaultKey( lFaultKey );
      } );

      final TaskTaskKey lFollowOnTaskDefinitionTrk = createFollowOnTaskDefinition( lTrkConfigSlot );

      MxFaultDetailsService lMxFaultDetailsService = new MxFaultDetailsService();

      /* Execution + Exception */

      lMxFaultDetailsService.createFollowOnTaskFromDefinition( lCorrectiveTask, lAssembly,
            lAssemblyInventory, lFollowOnTaskDefinitionTrk, null );
   }


   /**
    * Tests that Follow-on task is created using the scheduling rule: Manufacture Date.
    *
    * <pre>
    *    Given - an aircraft associated to an aircraft assembly and part
    *            AND a Follow-on requirement definition that is defined against the root config-slot of the aircraft assembly,
    *                and is scheduled from the manufactured date and has a calendar based scheduling rule
    *            AND a corrective task associated with a fault is completed
    *    When - creating a follow-on task based on the definition
    *    Then - the created Follow-on Task must be scheduled based on the manufacture date
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void testFollowOnTaskIsCreatedBasedOnManufacturedDateCalendarDaySchedulingRule()
         throws Exception {

      // Given
      final PartNoKey lAircraftPart = Domain.createPart();
      final AssemblyKey lAircraftAssembly = createAircraftAssembly( lAircraftPart );
      final InventoryKey lAircraft = createAircraftInventoryWithManufacturedDateAndCurrentUsage(
            lAircraftPart, lAircraftAssembly );
      final ConfigSlotKey lRootConfigSlot = Domain.readRootConfigurationSlot( lAircraftAssembly );
      final InventoryKey lCorrectiveTaskInventory = Domain.readSystem( lAircraft, SYSTEM_NAME );

      final TaskTaskKey lFollowOnTaskDefinition =
            Domain.createRequirementDefinition( aRequirementDefinition -> {
               aRequirementDefinition.againstConfigurationSlot( lRootConfigSlot );
               aRequirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
               aRequirementDefinition.addSchedulingRule( DataTypeKey.CDY, THRESHOLD );
               aRequirementDefinition
                     .setSchedulingRuleUsedWhenCreatedFromAnotherTask( USE_SCHED_RULE );
               aRequirementDefinition.setScheduledFromManufacturedDate();
            } );

      final TaskKey lCorrectiveTask = Domain.createCorrectiveTask( aCorrectiveTask -> {
         aCorrectiveTask.setInventory( lCorrectiveTaskInventory );
         aCorrectiveTask.setStatus( RefEventStatusKey.COMPLETE );
      } );

      Domain.createFault( fault -> {
         fault.setCorrectiveTask( lCorrectiveTask );
      } );

      // When
      final TaskKey lFollowOnTaskKey = iFaultDetailsService.createFollowOnTaskFromDefinition(
            lCorrectiveTask, lAircraftAssembly, lAircraft, lFollowOnTaskDefinition, null );

      // Then
      EvtSchedDeadTable lEventScheduleDeadlines =
            EvtSchedDeadTable.findByPrimaryKey( lFollowOnTaskKey, DataTypeKey.CDY );

      assertThat( "The scheduled start date was not using the correct scheduling code.",
            lEventScheduleDeadlines.getScheduledFrom(), is( RefSchedFromKey.BIRTH ) );
      assertThat( "The scheduled start date was not the aircraft manufactured date.",
            DateUtils.toString( lEventScheduleDeadlines.getStartDate(), DATE_FORMAT ),
            is( DateUtils.toString( AIRCRAFT_MANUFACTURED_DATE, DATE_FORMAT ) ) );

   }


   /**
    * Tests that Follow-on task is created using the rescheduling rule: Work Package Start date.
    *
    * <pre>
    *    Given - an aircraft associated to an aircraft assembly and part
    *            AND a Follow-on requirement definition that is defined against the root config-slot of the aircraft assembly,
    *                and is scheduled from the work package start date and has a calendar based scheduling rule
    *            AND a corrective task associated with a fault is assigned to a work package with a start date,
    *                and both the corrective task and work package are completed
    *    When - creating a follow-on task based on the definition
    *    Then - the created Follow-on Task must be scheduled using the work package start date
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void testFollowOnTaskIsCreatedBasedOnWPStartDateCalendarDayReschedulingRule()
         throws Exception {

      // Given
      final PartNoKey lAircraftPart = Domain.createPart();
      final AssemblyKey lAircraftAssembly = createAircraftAssembly( lAircraftPart );
      final InventoryKey lAircraft = createAircraftInventoryWithManufacturedDateAndCurrentUsage(
            lAircraftPart, lAircraftAssembly );

      final ConfigSlotKey lRootConfigSlot = Domain.readRootConfigurationSlot( lAircraftAssembly );

      final TaskTaskKey lFollowOnTaskDefinition =
            Domain.createRequirementDefinition( aRequirementDefinition -> {
               aRequirementDefinition.againstConfigurationSlot( lRootConfigSlot );
               aRequirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
               aRequirementDefinition.addSchedulingRule( DataTypeKey.CDY, THRESHOLD );
               aRequirementDefinition
                     .setSchedulingRuleUsedWhenCreatedFromAnotherTask( USE_RESCHED_RULE );
               aRequirementDefinition.setRescheduleFromOption( RefReschedFromKey.WPSTART );
            } );

      final InventoryKey lCorrectiveTaskInventory = Domain.readSystem( lAircraft, SYSTEM_NAME );

      final TaskKey lCorrectiveTask = Domain.createCorrectiveTask( aCorrectiveTask -> {
         aCorrectiveTask.setInventory( lCorrectiveTaskInventory );
         aCorrectiveTask.setStatus( RefEventStatusKey.COMPLETE );
      } );

      Domain.createFault( fault -> {
         fault.setCorrectiveTask( lCorrectiveTask );
      } );

      Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {
            aWorkPackage.setActualStartDate( WP_START_DATE );
            aWorkPackage.addTask( lCorrectiveTask );
            aWorkPackage.setStatus( RefEventStatusKey.COMPLETE );

         }
      } );

      // When
      final TaskKey lFollowOnTaskKey = iFaultDetailsService.createFollowOnTaskFromDefinition(
            lCorrectiveTask, lAircraftAssembly, lAircraft, lFollowOnTaskDefinition, null );

      // Then
      EvtSchedDeadTable lEventScheduleDeadlines =
            EvtSchedDeadTable.findByPrimaryKey( lFollowOnTaskKey, DataTypeKey.CDY );

      assertThat( "The scheduled start date was not using the correct rescheduling code.",
            lEventScheduleDeadlines.getScheduledFrom(), is( RefReschedFromKey.WPSTART ) );
      assertThat( "The scheduled start date was not the work package start date.",
            DateUtils.toString( lEventScheduleDeadlines.getStartDate(), DATE_FORMAT ),
            is( DateUtils.toString( WP_START_DATE, DATE_FORMAT ) ) );
   }


   /**
    * Tests that Follow-on task is created using the scheduling rule: Effective date.
    *
    * <pre>
    *    Given - an aircraft associated to an aircraft assembly and part
    *            AND a Follow-on requirement definition that is defined against the root config-slot of the aircraft assembly,
    *                and is scheduled from an effective date in the past and has a usage based scheduling rule
    *            AND this aircraft has a flight with usage before the effective date
    *            AND a corrective task associated with a fault is completed
    *    When - creating a follow-on task based on the definition
    *    Then - the created Follow-on Task must be have the same start quantity as it had on the effective date
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void testFollowOnTaskIsCreatedBasedOnEffectiveDateHoursUsageSchedulingRule()
         throws Exception {

      // Given
      final PartNoKey lAircraftPart = Domain.createPart();
      final AssemblyKey lAircraftAssembly = createAircraftAssembly( lAircraftPart );
      final InventoryKey lAircraft = createAircraftInventoryWithManufacturedDateAndCurrentUsage(
            lAircraftPart, lAircraftAssembly );
      final ConfigSlotKey lRootConfigSlot = Domain.readRootConfigurationSlot( lAircraftAssembly );
      final InventoryKey lCorrectiveTaskInventory = Domain.readSystem( lAircraft, SYSTEM_NAME );

      final TaskTaskKey lFollowOnTaskDefinition =
            Domain.createRequirementDefinition( aRequirementDefinition -> {
               aRequirementDefinition.againstConfigurationSlot( lRootConfigSlot );
               aRequirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
               aRequirementDefinition.addSchedulingRule( DataTypeKey.HOURS, THRESHOLD );
               aRequirementDefinition
                     .setSchedulingRuleUsedWhenCreatedFromAnotherTask( USE_SCHED_RULE );
               aRequirementDefinition.setScheduledFromEffectiveDate( EFFECTIVE_DATE );
            } );

      Domain.createFlight( lFlight -> {
         lFlight.addUsage( lAircraft, DataTypeKey.HOURS, EFFECTIVE_DATE_USAGE,
               EFFECTIVE_DATE_USAGE );
         lFlight.setAircraft( lAircraft );
         lFlight.setArrivalDate( DateUtils.addDays( EFFECTIVE_DATE, -1 ) );
      } );

      final TaskKey lCorrectiveTask = Domain.createCorrectiveTask( aCorrectiveTask -> {
         aCorrectiveTask.setInventory( lCorrectiveTaskInventory );
         aCorrectiveTask.setStatus( RefEventStatusKey.COMPLETE );
      } );

      Domain.createFault( fault -> {
         fault.setCorrectiveTask( lCorrectiveTask );
      } );

      // When
      final TaskKey lFollowOnTaskKey = iFaultDetailsService.createFollowOnTaskFromDefinition(
            lCorrectiveTask, lAircraftAssembly, lAircraft, lFollowOnTaskDefinition, null );

      // Then
      EvtSchedDeadTable lEventScheduleDeadlines =
            EvtSchedDeadTable.findByPrimaryKey( lFollowOnTaskKey, DataTypeKey.HOURS );

      assertThat( "The scheduled start date was not using the correct scheduling code.",
            lEventScheduleDeadlines.getScheduledFrom(), is( RefSchedFromKey.EFFECTIV ) );
      assertThat( "The scheduled start usage was not the same as the usage at the effective date.",
            new BigDecimal( lEventScheduleDeadlines.getStartQt() ), is( EFFECTIVE_DATE_USAGE ) );

   }


   /**
    * Tests that a Follow-on task corresponding to a Follow-on Task Definition is stored in the
    * database when creating a Follow-on task from a fault.
    *
    * <pre>
    * Given an aircraft associated to an aircraft assembly and part
    * And a Follow-on Task Definition that is defined against the root config slot of the aircraft assembly
    * And a Corrective Task associated to a child inventory of the aircraft
    * When creating a Follow-on Task
    * Then the created Follow-on Task must be associated to the specified Follow-on task definition
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void testFollowOnTaskIsCreatedWhenCreatingFollowOnTaskFromCorrectiveTaskOfFault()
         throws Exception {

      final PartNoKey lAircraftPart = Domain.createPart();
      final AssemblyKey lAssembly = createAircraftAssembly( lAircraftPart );
      final InventoryKey lAssemblyInventory = createAircraftInventory( lAircraftPart, lAssembly );

      final ConfigSlotKey lRootConfigSlot = Domain.readRootConfigurationSlot( lAssembly );

      final TaskTaskKey lFollowOnTaskDefinition =
            createFollowOnTaskDefinition( lRootConfigSlot, NOT_RECURRING );

      final InventoryKey lCorrectiveTaskInventory =
            Domain.readSystem( lAssemblyInventory, SYSTEM_NAME );

      TaskKey lCorrectiveTask =
            Domain.createCorrectiveTask( new DomainConfiguration<CorrectiveTask>() {

               @Override
               public void configure( CorrectiveTask aBuilder ) {
                  aBuilder.setInventory( lCorrectiveTaskInventory );
                  aBuilder.setStatus( RefEventStatusKey.COMPLETE );
               }
            } );

      Domain.createFault( fault -> {
         fault.setCorrectiveTask( lCorrectiveTask );
      } );

      TaskKey lFollowOnTaskKey = iFaultDetailsService.createFollowOnTaskFromDefinition(
            lCorrectiveTask, lAssembly, lAssemblyInventory, lFollowOnTaskDefinition, null );

      verifyFollowOnTaskCorrespondsToDefinition( lFollowOnTaskDefinition, lFollowOnTaskKey );
   }


   /**
    * Tests that activated and forecasted tasks are initialized in the database when creating a
    * recurring Follow-on task from a fault.
    *
    * <pre>
    * Given an aircraft associated to an aircraft assembly and part
    * And a recurring Follow-on Task Definition that is defined against the root config slot of the aircraft assembly
    * And a Corrective Task associated to a child inventory of the aircraft
    * When creating a Follow-on Task Then the created Follow-on Tasks must be associated to the specified Follow-on task definition
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void
         testRecurringTasksAreCreatedWhenCreatingRecurringFollowOnTaskFromCorrectiveTaskOfFault()
               throws Exception {

      final PartNoKey lAircraftPart = Domain.createPart();
      final AssemblyKey lAssembly = createAircraftAssembly( lAircraftPart );
      final InventoryKey lAssemblyInventory = createAircraftInventory( lAircraftPart, lAssembly );

      final ConfigSlotKey lRootConfigSlot = Domain.readRootConfigurationSlot( lAssembly );

      final TaskTaskKey lFollowOnTaskDefinition =
            createFollowOnTaskDefinition( lRootConfigSlot, RECURRING );

      final InventoryKey lCorrectiveTaskInventory =
            Domain.readSystem( lAssemblyInventory, SYSTEM_NAME );

      TaskKey lCorrectiveTask =
            Domain.createCorrectiveTask( new DomainConfiguration<CorrectiveTask>() {

               @Override
               public void configure( CorrectiveTask aBuilder ) {
                  aBuilder.setInventory( lCorrectiveTaskInventory );
                  aBuilder.setStatus( RefEventStatusKey.COMPLETE );
               }
            } );

      Domain.createFault( fault -> {
         fault.setCorrectiveTask( lCorrectiveTask );
      } );

      iFaultDetailsService.createFollowOnTaskFromDefinition( lCorrectiveTask, lAssembly,
            lAssemblyInventory, lFollowOnTaskDefinition, null );

      // Then an active task is initialized for the inventory
      List<TaskKey> lActualReqTask = TaskDefnUtils.getActualTasks( lFollowOnTaskDefinition,
            lAssemblyInventory, RefEventStatusKey.ACTV );
      Assert.assertEquals( "Unexpected number of ACTV tasks initialized.", 1,
            lActualReqTask.size() );

      verifyFollowOnTaskCorrespondsToDefinition( lFollowOnTaskDefinition, lActualReqTask.get( 0 ) );

      // Then a forecast task is initialized for the inventory
      List<TaskKey> lActualForecastReqTask = TaskDefnUtils.getActualTasks( lFollowOnTaskDefinition,
            lAssemblyInventory, RefEventStatusKey.FORECAST );
      Assert.assertEquals( "Unexpected number of FORECAST task initialized.", 1,
            lActualForecastReqTask.size() );

      verifyFollowOnTaskCorrespondsToDefinition( lFollowOnTaskDefinition,
            lActualForecastReqTask.get( 0 ) );
   }


   /**
    * Test that dependency relations are created when creating a Follow-on task from a fault.
    *
    * <pre>
    * Given an aircraft associated to an aircraft assembly and part
    * And a Follow-on Task Definition that is defined against the root config slot of the aircraft assembly
    * And a Corrective Task associated to a child inventory of the aircraft
    * When creating a Follow-on Task
    * Then the created Follow-on Task must have a dependency relation to the corrective task
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void testDependencyRelationIsCreatedWhenCreatingFollowOnTaskFromCorrectiveTaskOfFault()
         throws Exception {

      final PartNoKey lAircraftPart = Domain.createPart();
      final AssemblyKey lAssembly = createAircraftAssembly( lAircraftPart );
      final InventoryKey lAssemblyInventory = createAircraftInventory( lAircraftPart, lAssembly );

      final ConfigSlotKey lRootConfigSlot = Domain.readRootConfigurationSlot( lAssembly );

      final TaskTaskKey lFollowOnTaskDefinition =
            createFollowOnTaskDefinition( lRootConfigSlot, NOT_RECURRING );

      final InventoryKey lCorrectiveTaskInventory =
            Domain.readSystem( lAssemblyInventory, SYSTEM_NAME );

      TaskKey lCorrectiveTask =
            Domain.createCorrectiveTask( new DomainConfiguration<CorrectiveTask>() {

               @Override
               public void configure( CorrectiveTask aBuilder ) {
                  aBuilder.setInventory( lCorrectiveTaskInventory );
                  aBuilder.setStatus( RefEventStatusKey.COMPLETE );
               }
            } );

      Domain.createFault( fault -> {
         fault.setCorrectiveTask( lCorrectiveTask );
      } );

      TaskKey lFollowOnTaskKey = iFaultDetailsService.createFollowOnTaskFromDefinition(
            lCorrectiveTask, lAssembly, lAssemblyInventory, lFollowOnTaskDefinition, null );

      List<TaskKey> lDependentTasks = getNextDependentTasks( lCorrectiveTask );

      // Verify that the Active Follow-on task has a dependency relation to the corrective task (of
      // the fault)
      assertThat( lDependentTasks, hasItem( lFollowOnTaskKey ) );
   }


   /**
    * Test that dependency relations are created when creating a recurring Follow-on task from a
    * fault.
    *
    * <pre>
    * Given an aircraft associated to an aircraft assembly and part
    * And a recurring Follow-on Task Definition that is defined against the root config slot of the aircraft assembly
    * And a Corrective Task associated to a child inventory of the aircraft
    * When creating a Follow-on Task
    * Then the created Active Follow-on Task must have a dependency relation to the corrective task
    * And the created Forecast Follow-on Task must have a dependency relation to the Active Follow-on Task
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void
         testDependencyRelationIsCreatedWhenCreatingRecurringFollowOnTaskFromCorrectiveTaskOfFault()
               throws Exception {

      final PartNoKey lAircraftPart = Domain.createPart();
      final AssemblyKey lAssembly = createAircraftAssembly( lAircraftPart );
      final InventoryKey lAssemblyInventory = createAircraftInventory( lAircraftPart, lAssembly );

      final ConfigSlotKey lRootConfigSlot = Domain.readRootConfigurationSlot( lAssembly );

      final TaskTaskKey lFollowOnTaskDefinition =
            createFollowOnTaskDefinition( lRootConfigSlot, RECURRING );

      final InventoryKey lCorrectiveTaskInventory =
            Domain.readSystem( lAssemblyInventory, SYSTEM_NAME );

      TaskKey lCorrectiveTask =
            Domain.createCorrectiveTask( new DomainConfiguration<CorrectiveTask>() {

               @Override
               public void configure( CorrectiveTask aBuilder ) {
                  aBuilder.setInventory( lCorrectiveTaskInventory );
                  aBuilder.setStatus( RefEventStatusKey.COMPLETE );
               }
            } );

      Domain.createFault( fault -> {
         fault.setCorrectiveTask( lCorrectiveTask );
      } );

      iFaultDetailsService.createFollowOnTaskFromDefinition( lCorrectiveTask, lAssembly,
            lAssemblyInventory, lFollowOnTaskDefinition, null );

      TaskKey lActiveTask = TaskDefnUtils
            .getActualTasks( lFollowOnTaskDefinition, lAssemblyInventory, RefEventStatusKey.ACTV )
            .get( 0 );

      List<TaskKey> lDependentTasksOfCorrectiveTask = getNextDependentTasks( lCorrectiveTask );
      assertThat( lDependentTasksOfCorrectiveTask, hasItem( lActiveTask ) );

      List<TaskKey> lDependentTasksOfActiveFollowOnTask = getNextDependentTasks( lActiveTask );

      TaskKey lForecastTask = TaskDefnUtils.getActualTasks( lFollowOnTaskDefinition,
            lAssemblyInventory, RefEventStatusKey.FORECAST ).get( 0 );

      // Verify that the Forecasted Follow-on task has a dependency relation to the active Follow-on
      // task
      assertThat( lDependentTasksOfActiveFollowOnTask, hasItem( lForecastTask ) );
   }


   /**
    * Test that a FAULTREL relation is created when creating a Follow-on task from a fault.
    *
    * <pre>
    * Given an aircraft associated to an aircraft assembly and part
    * And a Follow-on Task Definition that is defined against the root config slot of the aircraft assembly
    * And a Corrective Task associated to a child inventory of the aircraft
    * When creating a Follow-on Task
    * Then the created Follow-on Task must have a FAULTREL relation to the fault <br/>
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void
         testFaultRelationToFollowOnTaskIsCreatedWhenCreatingFollowOnTaskFromCorrectiveTaskOfFault()
               throws Exception {

      final PartNoKey lAircraftPart = Domain.createPart();
      final AssemblyKey lAssembly = createAircraftAssembly( lAircraftPart );
      final InventoryKey lAssemblyInventory = createAircraftInventory( lAircraftPart, lAssembly );

      final ConfigSlotKey lRootConfigSlot = Domain.readRootConfigurationSlot( lAssembly );

      final TaskTaskKey lFollowOnTaskDefinition =
            createFollowOnTaskDefinition( lRootConfigSlot, NOT_RECURRING );

      final InventoryKey lCorrectiveTaskInventory =
            Domain.readSystem( lAssemblyInventory, SYSTEM_NAME );

      FaultKey lFaultKey = Domain.createFault();
      TaskKey lCorrectiveTask =
            Domain.createCorrectiveTask( new DomainConfiguration<CorrectiveTask>() {

               @Override
               public void configure( CorrectiveTask aBuilder ) {
                  aBuilder.setInventory( lCorrectiveTaskInventory );
                  aBuilder.setFaultKey( lFaultKey );
                  aBuilder.setStatus( RefEventStatusKey.COMPLETE );
               }
            } );

      TaskKey lFollowOnTaskKey = iFaultDetailsService.createFollowOnTaskFromDefinition(
            lCorrectiveTask, lAssembly, lAssemblyInventory, lFollowOnTaskDefinition, null );

      FaultKey lRelatedFault = getRelatedFault( lFollowOnTaskKey );

      // Verify that the fault has a FAULTREL relation to the created Follow-on task
      assertThat( lRelatedFault, equalTo( lFaultKey ) );
   }


   /**
    * Test that FAULTREL relations are created when creating a recurring Follow-on task from a
    * fault.
    *
    * <pre>
    * Given an aircraft associated to an aircraft assembly and part
    * And a recurring Follow-on Task Definition that is defined against the root config slot of the aircraft assembly
    * And a Corrective Task associated to a child inventory of the aircraft
    * When creating a Follow-on Task
    * Then the created Active Follow-on Task must have a FAULTREL relation to the fault
    * And the created Forecast Follow-on Task must have a FAULTREL relation to the fault
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void
         testFaultRelationsAreCreatedWhenCreatingRecurringFollowOnTaskFromCorrectiveTaskOfFault()
               throws Exception {

      final PartNoKey lAircraftPart = Domain.createPart();
      final AssemblyKey lAssembly = createAircraftAssembly( lAircraftPart );
      final InventoryKey lAssemblyInventory = createAircraftInventory( lAircraftPart, lAssembly );

      final ConfigSlotKey lRootConfigSlot = Domain.readRootConfigurationSlot( lAssembly );

      final TaskTaskKey lFollowOnTaskDefinition =
            createFollowOnTaskDefinition( lRootConfigSlot, RECURRING );

      final InventoryKey lCorrectiveTaskInventory =
            Domain.readSystem( lAssemblyInventory, SYSTEM_NAME );

      FaultKey lFaultKey = Domain.createFault();
      TaskKey lCorrectiveTask =
            Domain.createCorrectiveTask( new DomainConfiguration<CorrectiveTask>() {

               @Override
               public void configure( CorrectiveTask aBuilder ) {
                  aBuilder.setInventory( lCorrectiveTaskInventory );
                  aBuilder.setFaultKey( lFaultKey );
                  aBuilder.setStatus( RefEventStatusKey.COMPLETE );
               }
            } );

      iFaultDetailsService.createFollowOnTaskFromDefinition( lCorrectiveTask, lAssembly,
            lAssemblyInventory, lFollowOnTaskDefinition, null );

      TaskKey lActiveTask = TaskDefnUtils
            .getActualTasks( lFollowOnTaskDefinition, lAssemblyInventory, RefEventStatusKey.ACTV )
            .get( 0 );

      // Verify that the fault has a FAULTREL relation to the Active Follow-on task
      assertThat( getRelatedFault( lActiveTask ), equalTo( lFaultKey ) );

      TaskKey lForecastTask = TaskDefnUtils.getActualTasks( lFollowOnTaskDefinition,
            lAssemblyInventory, RefEventStatusKey.FORECAST ).get( 0 );

      // Verify that the fault has a FAULTREL relation to the Forecast Follow-on task
      assertThat( getRelatedFault( lForecastTask ), equalTo( lFaultKey ) );
   }


   /**
    * Test that an {@link InvalidAssemblyException} is thrown when attempting to create a Follow-on
    * task and the associated fault is itself associated with a different assembly than that of the
    * Follow-on task definition
    *
    * <pre>
    * Given an aircraft associated to an aircraft assembly and part
    * And a Corrective Task associated to a child inventory of the aircraft
    * And a recurring Follow-on Task Definition that is defined against the root config slot of a different aircraft assembly
    * When creating a Follow-on Task
    * Then an exception is thrown
    * </pre>
    *
    * @throws Exception
    */
   @Test( expected = InvalidAssemblyException.class )
   public void
         testExceptionThrownWhenCreatingFollowOnTaskAndAssembliesOfFaultAndTaskDefinitionAreNotEqual()
               throws Exception {

      final PartNoKey lAircraftPart = Domain.createPart();
      final AssemblyKey lFaultAssembly = createAircraftAssembly( lAircraftPart );
      final InventoryKey lFaultAssemblyInventory =
            createAircraftInventory( lAircraftPart, lFaultAssembly );

      final ConfigSlotKey lRootConfigSlotOfAnotherAssembly = Domain.readRootConfigurationSlot(
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aBuilder ) {
                  aBuilder.setCode( "ACFT2" );
               }
            } ) );

      final TaskTaskKey lFollowOnTaskDefinition =
            createFollowOnTaskDefinition( lRootConfigSlotOfAnotherAssembly, RECURRING );

      final InventoryKey lCorrectiveTaskInventory =
            new InventoryBuilder().withAssemblyInventory( lFaultAssemblyInventory ).build();

      FaultKey lFaultKey = Domain.createFault();
      TaskKey lCorrectiveTask =
            Domain.createCorrectiveTask( new DomainConfiguration<CorrectiveTask>() {

               @Override
               public void configure( CorrectiveTask aBuilder ) {
                  aBuilder.setInventory( lCorrectiveTaskInventory );
                  aBuilder.setFaultKey( lFaultKey );
                  aBuilder.setStatus( RefEventStatusKey.COMPLETE );
               }
            } );

      iFaultDetailsService.createFollowOnTaskFromDefinition( lCorrectiveTask, lFaultAssembly,
            lFaultAssemblyInventory, lFollowOnTaskDefinition, null );
   }


   /**
    * Test that a {@link MissingInventoryException} is thrown when attempting to create a Follow-on
    * task and there is no inventory in the config slot associated to the Follow-on task definition
    *
    * <pre>
    * Given an aircraft associated to an aircraft assembly and part
    * And a Corrective Task associated to a child inventory of the aircraft
    * And a recurring Follow-on Task Definition that is defined against a system config slot (that has no corresponding inventory)
    * When creating a Follow-on Task
    * Then an exception is thrown
    * </pre>
    *
    * @throws Exception
    */
   @Test( expected = MissingInventoryException.class )
   public void
         testExceptionThrownWhenCreatingFollowOnTaskAndNoInventoryInFollowOnTaskDefinitionConfigSlot()
               throws Exception {

      final String CONFIG_SLOT_SYS_CODE = "CONFIG_SLOT_SYS";
      final PartNoKey lAircraftPart = Domain.createPart();
      final AssemblyKey lAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aAircraftAssembly ) {
                  aAircraftAssembly
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aRootConfigSlot ) {
                              aRootConfigSlot.addPartGroup( new DomainConfiguration<PartGroup>() {

                                 @Override
                                 public void configure( PartGroup aRootCsPartGroup ) {
                                    aRootCsPartGroup.setInventoryClass( ACFT );
                                    aRootCsPartGroup.addPart( lAircraftPart );
                                 }
                              } );
                              // add a SYS config slot to the root slot
                              aRootConfigSlot.addConfigurationSlot(
                                    new DomainConfiguration<ConfigurationSlot>() {

                                       @Override
                                       public void configure( ConfigurationSlot aTrkConfigSlot ) {
                                          aTrkConfigSlot.setCode( CONFIG_SLOT_SYS_CODE );
                                          aTrkConfigSlot
                                                .setConfigurationSlotClass( RefBOMClassKey.SYS );
                                       }

                                    } );
                           }
                        } );
               }
            } );

      final InventoryKey lAssemblyInventory = createAircraftInventory( lAircraftPart, lAssembly );

      final ConfigSlotKey lRootConfigSlot = Domain.readRootConfigurationSlot( lAssembly );
      final ConfigSlotKey lSysConfigSlot =
            Domain.readSubConfigurationSlot( lRootConfigSlot, CONFIG_SLOT_SYS_CODE );

      final TaskTaskKey lFollowOnTaskDefinition =
            createFollowOnTaskDefinition( lSysConfigSlot, NOT_RECURRING );

      final InventoryKey lCorrectiveTaskInventory =
            Domain.readSystem( lAssemblyInventory, SYSTEM_NAME );

      FaultKey lFaultKey = Domain.createFault();
      TaskKey lCorrectiveTask =
            Domain.createCorrectiveTask( new DomainConfiguration<CorrectiveTask>() {

               @Override
               public void configure( CorrectiveTask aBuilder ) {
                  aBuilder.setInventory( lCorrectiveTaskInventory );
                  aBuilder.setFaultKey( lFaultKey );
                  aBuilder.setStatus( RefEventStatusKey.COMPLETE );
               }
            } );

      iFaultDetailsService.createFollowOnTaskFromDefinition( lCorrectiveTask, lAssembly,
            lAssemblyInventory, lFollowOnTaskDefinition, null );
   }


   private TaskTaskKey createFollowOnTaskDefinition( final ConfigSlotKey aConfigSlot,
         boolean aIsRecurring ) {
      return Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

         @Override
         public void configure( RequirementDefinition aBuilder ) {
            aBuilder.againstConfigurationSlot( aConfigSlot );
            aBuilder.setTaskClass( RefTaskClassKey.FOLLOW );
            aBuilder.setOnCondition( true );
            if ( aIsRecurring ) {
               aBuilder.setRecurring( true );
               aBuilder.setScheduledFromManufacturedDate();
               aBuilder.setMinimumForecastRange( MIN_FORECAST_RANGE );
               aBuilder.addRecurringSchedulingRule( DataTypeKey.CDY, REPEAT_INTERVAL );
            } else {
               aBuilder.setRecurring( false );
            }
         }
      } );
   }


   /**
    * Test that an {@link IncompleteFaultException} is thrown when attempting to create a Follow-on
    * task and the corrective task is not complete.
    *
    * <pre>
    * Given an aircraft associated to an aircraft assembly and part
    * And an incomplete Corrective Task associated to a child inventory of the aircraft
    * And a recurring Follow-on Task Definition that is defined against the root config slot of a different aircraft assembly
    * When creating a Follow-on Task
    * Then an exception is thrown
    * </pre>
    *
    * @throws Exception
    */
   @Test( expected = IncompleteFaultException.class )
   public void testExceptionThrownWhenCreatingFollowOnTaskAndCorrectiveTaskOfFaultIsIncomplete()
         throws Exception {

      final PartNoKey lAircraftPart = Domain.createPart();
      final AssemblyKey lAssembly = createAircraftAssembly( lAircraftPart );
      final InventoryKey lAssemblyInventory = createAircraftInventory( lAircraftPart, lAssembly );

      final ConfigSlotKey lRootConfigSlot = Domain.readRootConfigurationSlot( lAssembly );

      final TaskTaskKey lFollowOnTaskDefinition =
            createFollowOnTaskDefinition( lRootConfigSlot, NOT_RECURRING );

      final InventoryKey lCorrectiveTaskInventory =
            Domain.readSystem( lAssemblyInventory, SYSTEM_NAME );

      TaskKey lCorrectiveTask =
            Domain.createCorrectiveTask( new DomainConfiguration<CorrectiveTask>() {

               @Override
               public void configure( CorrectiveTask aBuilder ) {
                  aBuilder.setInventory( lCorrectiveTaskInventory );
                  // Set status to something other than COMPLETE
                  aBuilder.setStatus( RefEventStatusKey.ACTV );
               }
            } );

      TaskKey lFollowOnTaskKey = iFaultDetailsService.createFollowOnTaskFromDefinition(
            lCorrectiveTask, lAssembly, lAssemblyInventory, lFollowOnTaskDefinition, null );

      verifyFollowOnTaskCorrespondsToDefinition( lFollowOnTaskDefinition, lFollowOnTaskKey );
   }


   private AssemblyKey createAircraftAssembly( final PartNoKey aAircraftPart ) {
      return Domain.createAircraftAssembly( aAircraftAssembly -> {
         aAircraftAssembly.setRootConfigurationSlot( aRootConfigSlot -> {
            aRootConfigSlot.addPartGroup( aRootCsPartGroup -> {
               aRootCsPartGroup.setInventoryClass( ACFT );
               aRootCsPartGroup.addPart( aAircraftPart );
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


   private InventoryKey createAircraftInventory( final PartNoKey aAircraftPart,
         final AssemblyKey aAssembly ) {
      return Domain.createAircraft( aAircraft -> {
         aAircraft.setAssembly( aAssembly );
         aAircraft.setPart( aAircraftPart );
         aAircraft.addSystem( SYSTEM_NAME );
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


   private InventoryKey createAircraftInventoryWithManufacturedDateAndCurrentUsage(
         final PartNoKey aAircraftPart, final AssemblyKey aAssembly ) {

      return Domain.createAircraft( aAircraft -> {
         aAircraft.setAssembly( aAssembly );
         aAircraft.setPart( aAircraftPart );
         aAircraft.addSystem( SYSTEM_NAME );
         aAircraft.setManufacturedDate( AIRCRAFT_MANUFACTURED_DATE );
         aAircraft.addUsage( DataTypeKey.HOURS, CURRENT_USAGE );
      } );
   }


   private FaultKey getRelatedFault( TaskKey aTask ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aTask, "event_db_id", "event_id" );
      lArgs.add( RefRelationTypeKey.FAULTREL, "rel_type_db_id", "rel_type_cd" );

      QuerySet lResult = QuerySetFactory.getInstance().executeQuery(
            new String[] { "rel_event_db_id", "rel_event_id" }, "evt_event_rel", lArgs );

      if ( lResult.next() ) {
         return lResult.getKey( FaultKey.class, "rel_event_db_id", "rel_event_id" );
      }

      return null;
   }


   private void verifyFollowOnTaskCorrespondsToDefinition(
         final TaskTaskKey aFollowOnTaskDefinitionKey, TaskKey aFollowOnTaskKey ) {
      SchedStaskTable lFollowOnTask = sSchedStaskDao.findByPrimaryKey( aFollowOnTaskKey );
      assertThat( "Actual Follow-on task was not created from Task Definition",
            lFollowOnTask.getTaskTaskKey(), equalTo( aFollowOnTaskDefinitionKey ) );
   }


   private List<TaskKey> getNextDependentTasks( TaskKey aTask ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aTask, "event_db_id", "event_id" );
      lArgs.add( RefRelationTypeKey.DEPT, "rel_type_db_id", "rel_type_cd" );

      QuerySet lResult = QuerySetFactory.getInstance().executeQuery(
            new String[] { "rel_event_db_id", "rel_event_id" }, "evt_event_rel", lArgs );

      List<TaskKey> lTasks = new ArrayList<TaskKey>( lResult.getRowCount() );
      while ( lResult.next() ) {
         lTasks.add( lResult.getKey( TaskKey.class, "rel_event_db_id", "rel_event_id" ) );
      }

      return lTasks;
   }


   /* Helper Method to Create FollowOn requirement definitions */
   private TaskTaskKey createFollowOnTaskDefinition( final ConfigSlotKey aConfigSlot ) {
      return Domain.createRequirementDefinition( aRequirementDefinition -> {
         aRequirementDefinition.againstConfigurationSlot( aConfigSlot );
         aRequirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
         aRequirementDefinition.setOnCondition( true );
      } );
   }


   @Before
   public void setup() {
      iFaultDetailsService = new MxFaultDetailsService();
   }
}
