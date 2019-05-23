package com.mxi.mx.core.production.fleetduelist.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.BlockDefinition;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.key.AircraftKey;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.AuthorityKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefStageReasonKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.production.task.domain.TaskCancelledEvent;
import com.mxi.mx.core.production.task.domain.TaskCompletedEvent;
import com.mxi.mx.core.production.task.domain.TaskCreatedEvent;
import com.mxi.mx.core.production.task.domain.TaskDeadlineStartValue;
import com.mxi.mx.core.production.task.domain.TaskDeadlinesRemovedEvent;
import com.mxi.mx.core.production.task.domain.TaskDeletedEvent;
import com.mxi.mx.core.production.task.domain.TaskDrivingDeadlineRescheduledEvent;
import com.mxi.mx.core.production.task.domain.TaskPlanByDateUpdatedEvent;
import com.mxi.mx.core.production.task.domain.TaskRescheduledEvent;
import com.mxi.mx.core.production.task.domain.TaskTerminatedEvent;
import com.mxi.mx.core.services.stask.deadline.Deadline;
import com.mxi.mx.core.services.stask.deadline.DeadlineService;


/**
 * Test event handlers that impact fleet due list projection table.
 *
 */
public class FleetDueListTaskProjectionTest {

   private static final String EVENT_DB_ID_COL = "event_db_id";
   private static final String EVENT_ID_COL = "event_id";
   private static final String TASK_INV_NO_DB_ID = "task_inv_no_db_id";
   private static final String TASK_INV_NO_ID = "task_inv_no_id";
   private static final String DUE_DATE = "sort_due_dt";
   private static final String PLAN_BY_DATE = "plan_by_date";
   private static final String EVT_PLAN_BY_DT = "evt_plan_by_dt";

   private static final String MT_DRV_SCHED_INFO_TABLE = "mt_drv_sched_info";
   private static final String MT_CORE_FLEET_LIST_TABLE = "mt_core_fleet_list";

   private static final String[] MT_CORE_FLEET_LIST_COLS = { EVENT_DB_ID_COL, EVENT_ID_COL,
         TASK_INV_NO_DB_ID, TASK_INV_NO_ID, DUE_DATE, PLAN_BY_DATE };
   private static final String[] MT_DRV_SCHED_INFO_COLS =
         { EVENT_DB_ID_COL, EVENT_ID_COL, EVT_PLAN_BY_DT };

   private static final String[] TASK_KEY_COLUMNS = { "event_db_id", "event_id" };

   private static final HumanResourceKey HR = HumanResourceKey.ADMIN;

   private FleetDueListTaskProjection fleetDueListTaskProjection;

   private AircraftKey aircraftKey;
   private AuthorityKey authorityKeyAirCanada;

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule fakeInjectionOverrideRule = new InjectionOverrideRule();


   @Before
   public void setUp() {
      fleetDueListTaskProjection = new FleetDueListTaskProjection();
      authorityKeyAirCanada = Domain.createAuthority( authority -> {
         authority.setAuthorityCode( "ACC" );
         authority.setAuthorityName( "AIR CANADA" );
      } );

      aircraftKey = new AircraftKey( Domain.createAircraft( aircraft -> {
         aircraft.setLocked( false );
         aircraft.withAuthority( authorityKeyAirCanada );
      } ) );
   }


   @Test
   public void whenTaskIsCreatedAgainstRequirementWithSchedulingRuleThenShouldSeeTask() {
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

      final InventoryKey aircraftKey = Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( aircraftAssemblyKey );
         aircraft.setPart( aircraftPartKey );
         aircraft.addUsage( DataTypeKey.HOURS, BigDecimal.ZERO );
      } );

      final ConfigSlotKey aircraftRootConfigSlotKey = new ConfigSlotKey( aircraftAssemblyKey, 0 );

      final TaskTaskKey reqDefinitionKey = Domain.createRequirementDefinition( reqDefinition -> {
         reqDefinition.againstConfigurationSlot( aircraftRootConfigSlotKey );
         reqDefinition.setRecurring( false );
         reqDefinition.setStatus( RefTaskDefinitionStatusKey.ACTV );
         reqDefinition.setOnCondition( true );
         reqDefinition.setScheduledFromManufacturedDate();
         reqDefinition.addSchedulingRule( DataTypeKey.HOURS, BigDecimal.TEN );
      } );

      final TaskKey taskKey = Domain.createRequirement( requirement -> {
         requirement.setInventory( aircraftKey );
         requirement.setDefinition( reqDefinitionKey );
         requirement.setStatus( RefEventStatusKey.ACTV );
         requirement.addUsageDeadline( DataTypeKey.HOURS, BigDecimal.TEN, null );
      } );

      // clean the data created by related triggers
      clearFleetDueListProjectionTable( MT_DRV_SCHED_INFO_TABLE );
      clearFleetDueListProjectionTable( MT_CORE_FLEET_LIST_TABLE );

      // ACT
      fleetDueListTaskProjection.on( new TaskCreatedEvent( taskKey, reqDefinitionKey, null,
            aircraftKey, null, null, null, false, false, false, null, 0, true ) );

      // ASSERT
      QuerySet querySet = getQueryResult( taskKey, TASK_KEY_COLUMNS, MT_DRV_SCHED_INFO_TABLE );
      assertEquals( "Unexpected number of driving task schedule records.", 1,
            querySet.getRowCount() );

      querySet = getQueryResult( taskKey, TASK_KEY_COLUMNS, MT_CORE_FLEET_LIST_TABLE );
      assertEquals( "Unexpected number of fleet due list records.", 1, querySet.getRowCount() );
   }


   @Test
   public void
         whenTasksAreCreatedAgainstBlockAndRequirementAndJicWithSchedulingRuleThenShouldSeeBlockTask() {
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

      final InventoryKey aircraftKey = Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( aircraftAssemblyKey );
         aircraft.setPart( aircraftPartKey );
         aircraft.addUsage( DataTypeKey.HOURS, BigDecimal.ZERO );
      } );

      final ConfigSlotKey aircraftRootConfigSlotKey = new ConfigSlotKey( aircraftAssemblyKey, 0 );
      final TaskTaskKey lJicDefinitionKey = Domain.createJobCardDefinition();

      final TaskTaskKey reqDefinitionKey = Domain.createRequirementDefinition( reqDefinition -> {
         reqDefinition.againstConfigurationSlot( aircraftRootConfigSlotKey );
         reqDefinition.setRecurring( false );
         reqDefinition.setStatus( RefTaskDefinitionStatusKey.ACTV );
         reqDefinition.setOnCondition( true );
         reqDefinition.setScheduledFromManufacturedDate();
         reqDefinition.addSchedulingRule( DataTypeKey.HOURS, BigDecimal.TEN );
      } );

      final TaskTaskKey blockDefinitionKey =
            Domain.createBlockDefinition( new DomainConfiguration<BlockDefinition>() {

               @Override
               public void configure( BlockDefinition blockDefinition ) {
                  blockDefinition.setConfigurationSlot( aircraftRootConfigSlotKey );
                  blockDefinition.setStatus( RefTaskDefinitionStatusKey.ACTV );
                  blockDefinition.setOnCondition( true );
                  blockDefinition.addRequirementDefinition( reqDefinitionKey );
               }
            } );
      Domain.createJobCard( jobCard -> {
         jobCard.setInventory( aircraftKey );
         jobCard.setDefinition( lJicDefinitionKey );
         jobCard.setStatus( RefEventStatusKey.ACTV );
      } );
      Domain.createRequirement( requirement -> {
         requirement.setInventory( aircraftKey );
         requirement.setDefinition( reqDefinitionKey );
         requirement.setStatus( RefEventStatusKey.ACTV );
         requirement.addUsageDeadline( DataTypeKey.HOURS, BigDecimal.TEN, null );
      } );

      final TaskKey blockTaskKey = Domain.createBlock( block -> {
         block.setInventory( aircraftKey );
         block.setDefinition( blockDefinitionKey );
         block.setStatus( RefEventStatusKey.ACTV );
         block.addDeadline( aDeadline -> {
            aDeadline.setUsageType( DataTypeKey.HOURS );
            aDeadline.setInterval( BigDecimal.TEN );

         } );

      } );

      // clean the data created by related triggers
      clearFleetDueListProjectionTable( MT_DRV_SCHED_INFO_TABLE );
      clearFleetDueListProjectionTable( MT_CORE_FLEET_LIST_TABLE );

      // ACT
      fleetDueListTaskProjection.on( new TaskCreatedEvent( blockTaskKey, blockDefinitionKey, null,
            aircraftKey, null, null, null, false, false, false, null, 0, true ) );

      // ASSERT
      QuerySet querySet = getQueryResult( blockTaskKey, TASK_KEY_COLUMNS, MT_DRV_SCHED_INFO_TABLE );
      assertEquals( "Unexpected number of driving task schedule records.", 1,
            querySet.getRowCount() );

      querySet = getQueryResult( blockTaskKey, TASK_KEY_COLUMNS, MT_CORE_FLEET_LIST_TABLE );
      assertEquals( "Unexpected number of fleet due list records.", 1, querySet.getRowCount() );
   }


   @Test
   public void
         whenTasksAreCreatedAgainstBlockAndRequirementAndJicWithSchedulingRuleThenShouldSeeRequirementTask() {
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

      final InventoryKey aircraftKey = Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( aircraftAssemblyKey );
         aircraft.setPart( aircraftPartKey );
         aircraft.addUsage( DataTypeKey.HOURS, BigDecimal.ZERO );
      } );

      final ConfigSlotKey aircraftRootConfigSlotKey = new ConfigSlotKey( aircraftAssemblyKey, 0 );
      final TaskTaskKey lJicDefinitionKey = Domain.createJobCardDefinition();

      final TaskTaskKey reqDefinitionKey = Domain.createRequirementDefinition( reqDefinition -> {
         reqDefinition.againstConfigurationSlot( aircraftRootConfigSlotKey );
         reqDefinition.setRecurring( false );
         reqDefinition.setStatus( RefTaskDefinitionStatusKey.ACTV );
         reqDefinition.setOnCondition( true );
         reqDefinition.setScheduledFromManufacturedDate();
         reqDefinition.addSchedulingRule( DataTypeKey.HOURS, BigDecimal.TEN );
      } );

      final TaskTaskKey lBlockDefinitionKey = Domain.createBlockDefinition( blockDefinition -> {
         blockDefinition.setConfigurationSlot( aircraftRootConfigSlotKey );
         blockDefinition.setStatus( RefTaskDefinitionStatusKey.ACTV );
         blockDefinition.setOnCondition( true );
         blockDefinition.addRequirementDefinition( reqDefinitionKey );
      } );
      Domain.createJobCard( jobCard -> {
         jobCard.setInventory( aircraftKey );
         jobCard.setDefinition( lJicDefinitionKey );
         jobCard.setStatus( RefEventStatusKey.ACTV );
      } );
      final TaskKey taskKey = Domain.createRequirement( requirement -> {
         requirement.setInventory( aircraftKey );
         requirement.setDefinition( reqDefinitionKey );
         requirement.setStatus( RefEventStatusKey.ACTV );
         requirement.addUsageDeadline( DataTypeKey.HOURS, BigDecimal.TEN, null );
      } );

      Domain.createBlock( block -> {
         block.setInventory( aircraftKey );
         block.setDefinition( lBlockDefinitionKey );
         block.setStatus( RefEventStatusKey.ACTV );
         block.addDeadline( aDeadline -> {
            aDeadline.setUsageType( DataTypeKey.HOURS );
            aDeadline.setInterval( BigDecimal.TEN );

         } );

      } );

      // clean the data created by related triggers
      clearFleetDueListProjectionTable( MT_DRV_SCHED_INFO_TABLE );
      clearFleetDueListProjectionTable( MT_CORE_FLEET_LIST_TABLE );

      // ACT
      fleetDueListTaskProjection.on( new TaskCreatedEvent( taskKey, reqDefinitionKey, null,
            aircraftKey, null, null, null, false, false, false, null, 0, true ) );

      // ASSERT
      QuerySet querySet = getQueryResult( taskKey, TASK_KEY_COLUMNS, MT_DRV_SCHED_INFO_TABLE );
      assertEquals( "Unexpected number of driving task schedule records.", 1,
            querySet.getRowCount() );

      querySet = getQueryResult( taskKey, TASK_KEY_COLUMNS, MT_CORE_FLEET_LIST_TABLE );
      assertEquals( "Unexpected number of fleet due list records.", 1, querySet.getRowCount() );
   }


   @Test
   public void
         whenTasksAreCreatedAgainstBlockAndRequirementAndJicWithSchedulingRuleThenShouldNotSeeJicTask() {
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

      final InventoryKey aircraftKey = Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( aircraftAssemblyKey );
         aircraft.setPart( aircraftPartKey );
         aircraft.addUsage( DataTypeKey.HOURS, BigDecimal.ZERO );
      } );

      final ConfigSlotKey aircraftRootConfigSlotKey = new ConfigSlotKey( aircraftAssemblyKey, 0 );
      final TaskTaskKey lJicDefinitionKey = Domain.createJobCardDefinition();

      final TaskTaskKey reqDefinitionKey = Domain.createRequirementDefinition( reqDefinition -> {
         reqDefinition.againstConfigurationSlot( aircraftRootConfigSlotKey );
         reqDefinition.setRecurring( false );
         reqDefinition.addJobCardDefinition( lJicDefinitionKey );
         reqDefinition.setStatus( RefTaskDefinitionStatusKey.ACTV );
         reqDefinition.setOnCondition( true );
         reqDefinition.setScheduledFromManufacturedDate();
         reqDefinition.addSchedulingRule( DataTypeKey.HOURS, BigDecimal.TEN );
      } );

      final TaskTaskKey blockDefinitionKey = Domain.createBlockDefinition( blockDefinition -> {
         blockDefinition.setConfigurationSlot( aircraftRootConfigSlotKey );
         blockDefinition.setStatus( RefTaskDefinitionStatusKey.ACTV );
         blockDefinition.setOnCondition( true );
         blockDefinition.addRequirementDefinition( reqDefinitionKey );

      } );
      final TaskKey jicKey = Domain.createJobCard( jobCard -> {
         jobCard.setInventory( aircraftKey );
         jobCard.setDefinition( lJicDefinitionKey );
         jobCard.setStatus( RefEventStatusKey.ACTV );
      } );
      Domain.createRequirement( requirement -> {
         requirement.setInventory( aircraftKey );
         requirement.setDefinition( reqDefinitionKey );
         requirement.setStatus( RefEventStatusKey.ACTV );
         requirement.addUsageDeadline( DataTypeKey.HOURS, BigDecimal.TEN, null );
      } );

      Domain.createBlock( block -> {
         block.setInventory( aircraftKey );
         block.setDefinition( blockDefinitionKey );
         block.setStatus( RefEventStatusKey.ACTV );
         block.addDeadline( aDeadline -> {
            aDeadline.setUsageType( DataTypeKey.HOURS );
            aDeadline.setInterval( BigDecimal.TEN );

         } );

      } );

      // clean the data created by related triggers
      clearFleetDueListProjectionTable( MT_DRV_SCHED_INFO_TABLE );
      clearFleetDueListProjectionTable( MT_CORE_FLEET_LIST_TABLE );

      // ACT
      fleetDueListTaskProjection.on( new TaskCreatedEvent( jicKey, lJicDefinitionKey, null,
            aircraftKey, null, null, null, false, false, false, null, 0, true ) );

      // ASSERT
      QuerySet querySet = getQueryResult( jicKey, TASK_KEY_COLUMNS, MT_DRV_SCHED_INFO_TABLE );
      Assert.assertEquals( "Unexpected number of driving task schedule records.", 0,
            querySet.getRowCount() );

      querySet = getQueryResult( jicKey, TASK_KEY_COLUMNS, MT_CORE_FLEET_LIST_TABLE );
      Assert.assertEquals( "Unexpected number of fleet due list records.", 0,
            querySet.getRowCount() );
   }


   @Test
   public void
         WhenTaskIsCreatedAgainstFollowOnRequirementWithSchedulingRuleOnHistoricFaultThenShouldSeeFollowOnTask() {
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

      final InventoryKey aircraftKey = Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( aircraftAssemblyKey );
         aircraft.setPart( aircraftPartKey );
         aircraft.addUsage( DataTypeKey.HOURS, BigDecimal.ZERO );
      } );

      final ConfigSlotKey aircraftRootConfigSlotKey = new ConfigSlotKey( aircraftAssemblyKey, 0 );

      final TaskTaskKey followOnReqDefinitionKey =
            Domain.createRequirementDefinition( reqDefinition -> {
               reqDefinition.againstConfigurationSlot( aircraftRootConfigSlotKey );
               reqDefinition.setRecurring( false );
               reqDefinition.setStatus( RefTaskDefinitionStatusKey.ACTV );
               reqDefinition.setOnCondition( true );
               reqDefinition.setScheduledFromManufacturedDate();
               reqDefinition.addSchedulingRule( DataTypeKey.HOURS, BigDecimal.TEN );
               reqDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
            } );

      FaultKey lFaultKey = Domain.createFault( fault -> {
         fault.setInventory( aircraftKey );
         fault.setStatus( RefEventStatusKey.COMPLETE );
      } );

      final TaskKey taskKey = Domain.createRequirement( requirement -> {
         requirement.setInventory( aircraftKey );
         requirement.setDefinition( followOnReqDefinitionKey );
         requirement.setStatus( RefEventStatusKey.ACTV );
         requirement.addUsageDeadline( DataTypeKey.HOURS, BigDecimal.TEN, null );
         requirement.setAssociatedFault( lFaultKey );
      } );

      // clean the data created by related triggers
      clearFleetDueListProjectionTable( MT_DRV_SCHED_INFO_TABLE );
      clearFleetDueListProjectionTable( MT_CORE_FLEET_LIST_TABLE );

      // ACT
      fleetDueListTaskProjection.on( new TaskCreatedEvent( taskKey, followOnReqDefinitionKey, null,
            aircraftKey, null, null, null, false, false, false, null, 0, true ) );

      // ASSERT
      QuerySet querySet = getQueryResult( taskKey, TASK_KEY_COLUMNS, MT_DRV_SCHED_INFO_TABLE );
      assertEquals( "Unexpected number of driving task schedule records.", 1,
            querySet.getRowCount() );

      querySet = getQueryResult( taskKey, TASK_KEY_COLUMNS, MT_CORE_FLEET_LIST_TABLE );
      assertEquals( "Unexpected number of fleet due list records.", 1, querySet.getRowCount() );
   }


   @Test
   public void
         WhenTaskIsCreatedAgainstFollowOnRequirementWithOutSchedulingRuleOnHistoricFaultThenShouldNotSeeFollowOnTask() {
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

      final InventoryKey aircraftKey = Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( aircraftAssemblyKey );
         aircraft.setPart( aircraftPartKey );
      } );

      final ConfigSlotKey aircraftRootConfigSlotKey = new ConfigSlotKey( aircraftAssemblyKey, 0 );

      final TaskTaskKey followOnReqDefinitionKey =
            Domain.createRequirementDefinition( reqDefinition -> {
               reqDefinition.againstConfigurationSlot( aircraftRootConfigSlotKey );
               reqDefinition.setRecurring( false );
               reqDefinition.setStatus( RefTaskDefinitionStatusKey.ACTV );
               reqDefinition.setOnCondition( true );
               reqDefinition.setScheduledFromManufacturedDate();
               reqDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
            } );

      FaultKey faultKey = Domain.createFault( fault -> {
         fault.setInventory( aircraftKey );
         fault.setStatus( RefEventStatusKey.COMPLETE );
      } );

      final TaskKey taskKey = Domain.createRequirement( requirement -> {
         requirement.setInventory( aircraftKey );
         requirement.setDefinition( followOnReqDefinitionKey );
         requirement.setStatus( RefEventStatusKey.ACTV );
         requirement.addUsageDeadline( DataTypeKey.HOURS, BigDecimal.TEN, null );
         requirement.setAssociatedFault( faultKey );
      } );

      // clean the data created by related triggers
      clearFleetDueListProjectionTable( MT_DRV_SCHED_INFO_TABLE );
      clearFleetDueListProjectionTable( MT_CORE_FLEET_LIST_TABLE );

      // ACT
      fleetDueListTaskProjection.on( new TaskCreatedEvent( taskKey, followOnReqDefinitionKey, null,
            aircraftKey, null, null, null, false, false, false, null, 0, false ) );

      // ASSERT
      QuerySet querySet = getQueryResult( taskKey, TASK_KEY_COLUMNS, MT_DRV_SCHED_INFO_TABLE );
      Assert.assertEquals( "Unexpected number of driving task schedule records.", 0,
            querySet.getRowCount() );

      querySet = getQueryResult( taskKey, TASK_KEY_COLUMNS, MT_CORE_FLEET_LIST_TABLE );
      Assert.assertEquals( "Unexpected number of fleet due list records.", 0,
            querySet.getRowCount() );
   }


   @Test
   public void whenActiveRepetitiveTaskCreatedThenShouldSeeTask() {

      // ARRANGE
      TaskKey activeRepetitiveTask = Domain.createRepetitiveTask( repetitiveTask -> {
         repetitiveTask.setStatus( RefEventStatusKey.ACTV );
         repetitiveTask
               .setInventory( new InventoryKey( aircraftKey.getDbId(), aircraftKey.getId() ) );
      } );

      // ACT
      fleetDueListTaskProjection.on( new TaskDrivingDeadlineRescheduledEvent( activeRepetitiveTask,
            DataTypeKey.CDY, new Date(), 0D, new Date() ) );

      // ASSERT
      QuerySet querySet =
            getQueryResult( activeRepetitiveTask, MT_DRV_SCHED_INFO_COLS, MT_DRV_SCHED_INFO_TABLE );
      assertEquals( 1, querySet.getRowCount() );

      querySet = getQueryResult( activeRepetitiveTask, MT_CORE_FLEET_LIST_COLS,
            MT_CORE_FLEET_LIST_TABLE );
      assertEquals( 1, querySet.getRowCount() );

   }


   @Test
   public void whenCancelTaskThenItShouldBeRemovedFromFleetDueList() {
      // ARRANGE
      TaskKey activeRepetitiveTask = Domain.createRepetitiveTask( repetitiveTask -> {
         repetitiveTask.setStatus( RefEventStatusKey.ACTV );
         repetitiveTask
               .setInventory( new InventoryKey( aircraftKey.getDbId(), aircraftKey.getId() ) );
      } );

      // Insert a row
      DataSetArgument dataSetArgument = new DataSetArgument();
      dataSetArgument.add( activeRepetitiveTask, new String[] { EVENT_DB_ID_COL, EVENT_ID_COL } );
      MxDataAccess.getInstance().executeInsert( MT_DRV_SCHED_INFO_TABLE, dataSetArgument );

      dataSetArgument = new DataSetArgument();
      dataSetArgument.add( activeRepetitiveTask, new String[] { EVENT_DB_ID_COL, EVENT_ID_COL } );
      dataSetArgument.add( aircraftKey, new String[] { TASK_INV_NO_DB_ID, TASK_INV_NO_ID } );
      MxDataAccess.getInstance().executeInsert( MT_CORE_FLEET_LIST_TABLE, dataSetArgument );
      QuerySet querySet = getQueryResult( activeRepetitiveTask, MT_CORE_FLEET_LIST_COLS,
            MT_CORE_FLEET_LIST_TABLE );
      assertEquals( 1, querySet.getRowCount() );

      // ACT
      fleetDueListTaskProjection
            .on( new TaskCancelledEvent( activeRepetitiveTask, null, null, null, null, null ) );

      // ASSERT
      querySet =
            getQueryResult( activeRepetitiveTask, MT_DRV_SCHED_INFO_COLS, MT_DRV_SCHED_INFO_TABLE );
      assertEquals( 0, querySet.getRowCount() );

      querySet = getQueryResult( activeRepetitiveTask, MT_CORE_FLEET_LIST_COLS,
            MT_CORE_FLEET_LIST_TABLE );
      assertEquals( 0, querySet.getRowCount() );
   }


   @Test
   public void whenChangingStartValuesOfTaskThenDeadlineShouldBeUpdatedInFleetDueList() {
      // ARRANGE
      final Date someDate = DateUtils.addDays( new Date(), 3 );
      final TaskKey activeRepetitiveTask = Domain.createRepetitiveTask( repetitiveTask -> {
         repetitiveTask.setStatus( RefEventStatusKey.ACTV );
         repetitiveTask
               .setInventory( new InventoryKey( aircraftKey.getDbId(), aircraftKey.getId() ) );
         repetitiveTask.setStartDate( someDate );
      } );

      final DeadlineService deadlineService = new DeadlineService( activeRepetitiveTask );
      final Deadline drivingDeadline = deadlineService.getDrivingDeadline();
      final Double interval = drivingDeadline.getInterval();

      // ACT
      fleetDueListTaskProjection.on( new TaskRescheduledEvent(
            new TaskDeadlineStartValue( activeRepetitiveTask, null, someDate, "123" ), HR ) );

      // ASSERT
      QuerySet querySet = getQueryResult( activeRepetitiveTask, MT_CORE_FLEET_LIST_COLS,
            MT_CORE_FLEET_LIST_TABLE );
      assertEquals( 1, querySet.getRowCount() );
      querySet.next();
      SimpleDateFormat simpleDateFormatter = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm" );
      assertEquals( simpleDateFormatter.format( DateUtils.addTimeDays( someDate, interval ) ),
            simpleDateFormatter.format( querySet.getDate( DUE_DATE ) ) );
   }


   @Test
   public void
         whenUpdatePlanByDateOfATaskWithDueDateThenTaskDueDateShouldBeUpdatedInFleetDueList() {

      // ARRANGE
      Date today = new Date();
      final Date dueDate = DateUtils.addTimeDays( today, 10 );
      final Date planByDate = DateUtils.addTimeDays( today, 3 );

      TaskKey taskKey = Domain.createRequirement( task -> {
         task.addCalendarDeadline( DataTypeKey.CDY, new BigDecimal( 5 ), dueDate );
         task.setPlanByDate( planByDate );
      } );

      // ACT
      fleetDueListTaskProjection.on( new TaskPlanByDateUpdatedEvent( taskKey, HR, planByDate ) );

      // ASSERT
      QuerySet querySet =
            getQueryResult( taskKey, MT_CORE_FLEET_LIST_COLS, MT_CORE_FLEET_LIST_TABLE );
      assertEquals( 1, querySet.getRowCount() );
      querySet.next();
      SimpleDateFormat simpleDateFormatter = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm" );
      assertEquals( simpleDateFormatter.format( planByDate ),
            simpleDateFormatter.format( querySet.getDate( PLAN_BY_DATE ) ) );

      querySet = getQueryResult( taskKey, MT_DRV_SCHED_INFO_COLS, MT_DRV_SCHED_INFO_TABLE );
      assertEquals( 1, querySet.getRowCount() );
      querySet.next();
      assertEquals( simpleDateFormatter.format( planByDate ),
            simpleDateFormatter.format( querySet.getDate( EVT_PLAN_BY_DT ) ) );
   }


   @Test
   public void
         whenUpdatePlanByDateOfATaskWithoutDueDateThenTaskShouldNotBeDisplayedInFleetDueList() {

      // ARRANGE
      Date today = new Date();
      final Date planByDate = DateUtils.addTimeDays( today, 3 );

      TaskKey taskKey = Domain.createRequirement( task -> {
         task.setPlanByDate( planByDate );
      } );

      // ACT
      fleetDueListTaskProjection.on( new TaskPlanByDateUpdatedEvent( taskKey, HR, planByDate ) );

      // ASSERT
      QuerySet querySet =
            getQueryResult( taskKey, MT_CORE_FLEET_LIST_COLS, MT_CORE_FLEET_LIST_TABLE );
      assertEquals( 0, querySet.getRowCount() );

      querySet = getQueryResult( taskKey, MT_DRV_SCHED_INFO_COLS, MT_DRV_SCHED_INFO_TABLE );
      assertEquals( 0, querySet.getRowCount() );
   }


   private QuerySet getQueryResult( TaskKey taskKey, String[] columns, String table ) {
      final DataSetArgument args = new DataSetArgument();
      args.add( taskKey, TASK_KEY_COLUMNS );
      return QuerySetFactory.getInstance().executeQuery( columns, table, args );
   }


   private void clearFleetDueListProjectionTable( String tableName ) {
      MxDataAccess.getInstance().executeDelete( tableName, null );
   }


   @Test
   public void whenRemoveLastReqTaskDeadlineThenShouldNotSeeTask() {

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

      final InventoryKey aircraftKey = Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( aircraftAssemblyKey );
         aircraft.setPart( aircraftPartKey );
      } );

      final ConfigSlotKey aircraftRootConfigSlotKey = new ConfigSlotKey( aircraftAssemblyKey, 0 );

      final TaskTaskKey reqDefinitionKey = Domain.createRequirementDefinition( reqDefinition -> {
         reqDefinition.againstConfigurationSlot( aircraftRootConfigSlotKey );
         reqDefinition.setRecurring( false );
         reqDefinition.setStatus( RefTaskDefinitionStatusKey.ACTV );
         reqDefinition.setOnCondition( true );
         reqDefinition.addSchedulingRule( DataTypeKey.HOURS, BigDecimal.TEN );
      } );

      final TaskKey taskKey = Domain.createRequirement( requirement -> {
         requirement.setInventory( aircraftKey );
         requirement.setDefinition( reqDefinitionKey );
         requirement.setStatus( RefEventStatusKey.ACTV );
      } );

      Set<DataTypeKey> dataTypeKeysRemoved = new HashSet<DataTypeKey>();
      dataTypeKeysRemoved.add( DataTypeKey.HOURS );

      // ACT
      fleetDueListTaskProjection
            .on( new TaskDeadlinesRemovedEvent( taskKey, dataTypeKeysRemoved, null ) );

      // ASSERT
      QuerySet querySet =
            getQueryResult( taskKey, MT_DRV_SCHED_INFO_COLS, MT_DRV_SCHED_INFO_TABLE );
      assertEquals( 0, querySet.getRowCount() );

      querySet = getQueryResult( taskKey, MT_CORE_FLEET_LIST_COLS, MT_CORE_FLEET_LIST_TABLE );

      assertTrue( querySet.getRowCount() == 0 );

   }


   @Test
   public void whenRemoveOneOfMultipleReqTaskDeadlinesThenShouldSeeTask() {

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

      final InventoryKey aircraftKey = Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( aircraftAssemblyKey );
         aircraft.setPart( aircraftPartKey );
      } );

      final ConfigSlotKey aircraftRootConfigSlotKey = new ConfigSlotKey( aircraftAssemblyKey, 0 );

      final TaskTaskKey reqDefinitionKey = Domain.createRequirementDefinition( reqDefinition -> {
         reqDefinition.againstConfigurationSlot( aircraftRootConfigSlotKey );
         reqDefinition.setRecurring( false );
         reqDefinition.setStatus( RefTaskDefinitionStatusKey.ACTV );
         reqDefinition.setOnCondition( true );
         reqDefinition.addSchedulingRule( DataTypeKey.HOURS, BigDecimal.TEN );
         reqDefinition.addSchedulingRule( DataTypeKey.CDY, BigDecimal.TEN );
      } );

      final TaskKey taskKey = Domain.createRequirement( requirement -> {
         requirement.setInventory( aircraftKey );
         requirement.setDefinition( reqDefinitionKey );
         requirement.setStatus( RefEventStatusKey.ACTV );
         requirement.addUsageDeadline( DataTypeKey.HOURS, BigDecimal.TEN, null );
      } );

      Set<DataTypeKey> dataTypeKeysRemoved = new HashSet<DataTypeKey>();
      dataTypeKeysRemoved.add( DataTypeKey.CDY );

      // ACT
      fleetDueListTaskProjection
            .on( new TaskDeadlinesRemovedEvent( taskKey, dataTypeKeysRemoved, null ) );

      // ASSERT
      QuerySet querySet =
            getQueryResult( taskKey, MT_DRV_SCHED_INFO_COLS, MT_DRV_SCHED_INFO_TABLE );
      assertEquals( 1, querySet.getRowCount() );

      querySet = getQueryResult( taskKey, MT_CORE_FLEET_LIST_COLS, MT_CORE_FLEET_LIST_TABLE );
      assertEquals( 1, querySet.getRowCount() );
   }


   @Test
   public void whenDeletedTaskThenItShouldBeRemovedFromFleetDueList() {
      // ARRANGE
      final Date DELETED_DATE = new Date();
      final HumanResourceKey humanResourceKey =
            Domain.createHumanResource( h -> h.setAllAuthority( true ) );

      TaskKey activeRepetitiveTask = Domain.createRepetitiveTask( repetitiveTask -> {
         repetitiveTask.setStatus( RefEventStatusKey.ACTV );
         repetitiveTask
               .setInventory( new InventoryKey( aircraftKey.getDbId(), aircraftKey.getId() ) );
      } );

      // Insert a row
      DataSetArgument dataSetArgument = new DataSetArgument();
      dataSetArgument.add( activeRepetitiveTask, new String[] { EVENT_DB_ID_COL, EVENT_ID_COL } );
      MxDataAccess.getInstance().executeInsert( MT_DRV_SCHED_INFO_TABLE, dataSetArgument );

      dataSetArgument = new DataSetArgument();
      dataSetArgument.add( activeRepetitiveTask, new String[] { EVENT_DB_ID_COL, EVENT_ID_COL } );
      dataSetArgument.add( aircraftKey, new String[] { TASK_INV_NO_DB_ID, TASK_INV_NO_ID } );
      MxDataAccess.getInstance().executeInsert( MT_CORE_FLEET_LIST_TABLE, dataSetArgument );
      QuerySet querySet = getQueryResult( activeRepetitiveTask, MT_CORE_FLEET_LIST_COLS,
            MT_CORE_FLEET_LIST_TABLE );
      assertEquals( 1, querySet.getRowCount() );

      // ACT
      fleetDueListTaskProjection.on( new TaskDeletedEvent( activeRepetitiveTask, DELETED_DATE,
            humanResourceKey, RefTaskClassKey.ADHOC ) );

      // ASSERT
      querySet =
            getQueryResult( activeRepetitiveTask, MT_DRV_SCHED_INFO_COLS, MT_DRV_SCHED_INFO_TABLE );
      assertEquals( 0, querySet.getRowCount() );

      querySet = getQueryResult( activeRepetitiveTask, MT_CORE_FLEET_LIST_COLS,
            MT_CORE_FLEET_LIST_TABLE );
      assertEquals( 0, querySet.getRowCount() );
   }


   @Test
   public void whenCompletedTaskThenItShouldBeRemovedFromFleetDueList() {
      // ARRANGE
      final Date COMPLETION_DATE = new Date();
      final HumanResourceKey humanResourceKey =
            Domain.createHumanResource( h -> h.setAllAuthority( true ) );

      TaskKey activeRepetitiveTask = Domain.createRepetitiveTask( repetitiveTask -> {
         repetitiveTask.setStatus( RefEventStatusKey.ACTV );
         repetitiveTask
               .setInventory( new InventoryKey( aircraftKey.getDbId(), aircraftKey.getId() ) );
      } );

      // Insert a row
      DataSetArgument dataSetArgument = new DataSetArgument();
      dataSetArgument.add( activeRepetitiveTask, new String[] { EVENT_DB_ID_COL, EVENT_ID_COL } );
      MxDataAccess.getInstance().executeInsert( MT_DRV_SCHED_INFO_TABLE, dataSetArgument );

      dataSetArgument = new DataSetArgument();
      dataSetArgument.add( activeRepetitiveTask, new String[] { EVENT_DB_ID_COL, EVENT_ID_COL } );
      dataSetArgument.add( aircraftKey, new String[] { TASK_INV_NO_DB_ID, TASK_INV_NO_ID } );
      MxDataAccess.getInstance().executeInsert( MT_CORE_FLEET_LIST_TABLE, dataSetArgument );
      QuerySet querySet = getQueryResult( activeRepetitiveTask, MT_CORE_FLEET_LIST_COLS,
            MT_CORE_FLEET_LIST_TABLE );
      assertEquals( 1, querySet.getRowCount() );

      // ACT
      fleetDueListTaskProjection.on( new TaskCompletedEvent( activeRepetitiveTask, COMPLETION_DATE,
            humanResourceKey, RefTaskClassKey.ADHOC ) );

      // ASSERT
      querySet =
            getQueryResult( activeRepetitiveTask, MT_DRV_SCHED_INFO_COLS, MT_DRV_SCHED_INFO_TABLE );
      assertEquals( 0, querySet.getRowCount() );

      querySet = getQueryResult( activeRepetitiveTask, MT_CORE_FLEET_LIST_COLS,
            MT_CORE_FLEET_LIST_TABLE );
      assertEquals( 0, querySet.getRowCount() );
   }


   public void whenTerminateATaskThenItShouldNotBeVisibleInFleetDueList() {
      // ARRAGE
      final Date today = new Date();
      final Date dueDate = DateUtils.addTimeDays( today, 10 );
      final TaskKey taskKey = Domain.createRequirement( task -> {
         task.setStatus( RefEventStatusKey.ACTV );
         task.setInventory( new InventoryKey( aircraftKey.getDbId(), aircraftKey.getId() ) );
         task.addCalendarDeadline( DataTypeKey.CDY, new BigDecimal( 5 ), dueDate );
      } );

      QuerySet querySet =
            getQueryResult( taskKey, MT_CORE_FLEET_LIST_COLS, MT_CORE_FLEET_LIST_TABLE );
      assertTrue( querySet.getRowCount() > 0 );

      // ACT
      fleetDueListTaskProjection.on( new TaskTerminatedEvent( taskKey, RefTaskClassKey.REQ,
            RefStageReasonKey.OBSOLETE, null, null ) );

      // ASSERT
      querySet = getQueryResult( taskKey, MT_CORE_FLEET_LIST_COLS, MT_CORE_FLEET_LIST_TABLE );
      assertEquals( "unexpected number of records returned.", 0, querySet.getRowCount() );

   }

}
