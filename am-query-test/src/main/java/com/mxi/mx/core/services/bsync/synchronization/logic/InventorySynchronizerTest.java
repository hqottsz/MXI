package com.mxi.mx.core.services.bsync.synchronization.logic;

import static com.mxi.mx.common.utils.DateUtils.addDays;
import static com.mxi.mx.common.utils.DateUtils.ceilDay;
import static com.mxi.mx.core.key.DataTypeKey.CDY;
import static com.mxi.mx.core.key.RefInvClassKey.ACFT;
import static com.mxi.mx.core.key.RefTaskClassKey.BLOCK;
import static com.mxi.mx.core.key.RefTaskClassKey.REQ;
import static com.mxi.mx.core.key.RefTaskDefinitionStatusKey.ACTV;
import static com.mxi.mx.core.key.RefTaskDefinitionStatusKey.SUPRSEDE;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.AxonDomainEventDao;
import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.AircraftAssembly;
import com.mxi.am.domain.Block;
import com.mxi.am.domain.BlockChainDefinition;
import com.mxi.am.domain.BlockDefinition;
import com.mxi.am.domain.ConfigurationSlot;
import com.mxi.am.domain.Deadline;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.JobCard;
import com.mxi.am.domain.JobCardDefinition;
import com.mxi.am.domain.PartGroup;
import com.mxi.am.domain.Requirement;
import com.mxi.am.domain.RequirementDefinition;
import com.mxi.am.domain.WorkPackage;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.services.work.WorkItemGenerator;
import com.mxi.mx.common.services.work.WorkItemGeneratorExecuteImmediateFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.configuration.axon.AxonObjectMapper;
import com.mxi.mx.core.configuration.axon.RecordingEventBus;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.production.task.domain.TaskCancelledEvent;
import com.mxi.mx.core.production.task.domain.TaskCreatedEvent;
import com.mxi.mx.core.production.task.domain.TaskDrivingDeadlineRescheduledEvent;
import com.mxi.mx.core.services.bsync.helpers.BaselineSyncHelper;
import com.mxi.mx.core.services.taskdefn.TaskDefnUtils;
import com.mxi.mx.core.table.evt.EvtEventDao;
import com.mxi.mx.core.table.evt.EvtSchedDeadTable;
import com.mxi.mx.core.table.org.OrgHr;
import com.mxi.mx.core.table.sched.SchedStaskDao;
import com.mxi.mx.core.table.sched.SchedStaskTable;
import com.mxi.mx.core.table.task.TaskTaskTable;


/**
 *
 * Verifies the behavior of {@link InventorySynchronizer}
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class InventorySynchronizerTest {

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule injectionOverrideRule = new InjectionOverrideRule();

   private HumanResourceKey authHr;

   private int userId;

   private static final String APPLICABILITY_CODE = "A00";
   private static final Integer REVISION_1 = 1;
   private static final Integer REVISION_2 = 2;

   // Constants for block and requirement zipping tests.
   private static final Date NOW = new Date();
   private static final BigDecimal REPEAT_INTERVAL = new BigDecimal( 10 );
   private static final BigDecimal MIN_FORECAST_RANGE = new BigDecimal( 30 );

   private static final String BLOCK_CHAIN_A_NAME = "BLOCK_CHAIN_A";
   private static final String BLOCK_CHAIN_B_NAME = "BLOCK_CHAIN_B";

   private static final int BLOCK_ORDER_1 = 1;
   private static final int BLOCK_ORDER_2 = 2;
   private static final int BLOCK_ORDER_3 = 3;

   private static final String BLOCK_A1_CODE = "BLOCK_A1";
   private static final String BLOCK_A2_CODE = "BLOCK_A2";
   private static final String BLOCK_A3_CODE = "BLOCK_A3";

   private static final String BLOCK_B1_CODE = "BLOCK_B1";
   private static final String BLOCK_B2_CODE = "BLOCK_B2";
   private static final String BLOCK_B3_CODE = "BLOCK_B3";

   private static final String BLOCK_A1_NAME = BLOCK_A1_CODE;
   private static final String BLOCK_A2_NAME = BLOCK_A2_CODE;
   private static final String BLOCK_A3_NAME = BLOCK_A3_CODE;

   private static final String BLOCK_B1_NAME = BLOCK_B1_CODE;
   private static final String BLOCK_B2_NAME = BLOCK_B2_CODE;
   private static final String BLOCK_B3_NAME = BLOCK_B3_CODE;

   private GlobalParameters globalParams;
   private Boolean origIgnoreMisalignment;

   private RecordingEventBus eventBus;
   private final AxonDomainEventDao axonDomainEventDao = new AxonDomainEventDao();
   private final AxonObjectMapper objectMapper = new AxonObjectMapper();


   /**
    * Verify that activating a new one-time requirement results in tasks being created.
    *
    * Note: An existing e2e test {@link OneTimeRequirement.feature} has been split into two at the
    * baseline sync boundary due to performance issues with running baseline sync: One e2e test
    * driven by UI will verify that a request for an inventory to be synchronized is added to the
    * baseline sync queue. One integration test which is this one will verify that the request in
    * the baseline queue is processed.
    *
    * <pre>
    * Given an activated one-time, no condition requirement definition
    *   And the requirement definition is not part of a maintenance program
    *   And an inventory exists that is applicable to this requirement definition
    *   And the inventory allows synchronization
    *   And the inventory is marked for task synchronization
    *  When tasks for the inventory are synchronized
    *  Then task is created for the inventory
    * </pre>
    *
    */
   @Test
   public void itCreatesTasksAgainstOnetimeRequirementDefnWhenInventoryIsMarkedForSync()
         throws Exception {

      final PartNoKey aircraftPart = Domain.createPart();
      final AssemblyKey aircraftAssembly = createAcftAssyWithAcftPart( aircraftPart );
      final InventoryKey aircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aircraft ) {
            aircraft.setAssembly( aircraftAssembly );
            aircraft.setPart( aircraftPart );
            aircraft.allowSynchronization();
            aircraft.setApplicabilityCode( APPLICABILITY_CODE );
         }
      } );

      final ConfigSlotKey rootConfigSlot = Domain.readRootConfigurationSlot( aircraftAssembly );

      final TaskTaskKey requirementDefinition =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition requirementDefinition ) {
                  requirementDefinition.againstConfigurationSlot( rootConfigSlot );
                  requirementDefinition.setOnCondition( false );
                  requirementDefinition.setRecurring( false );
                  requirementDefinition.setApplicabilityRange( APPLICABILITY_CODE );
                  requirementDefinition.setStatus( ACTV );
               }

            } );

      // manually insert data into inv_sync_queue table to indicate that this inventory is marked
      // for task synchronization.
      requestSync( aircraft );

      // When the requirement for the inventory is synchronized
      inventorySync();

      DataSetArgument args = new DataSetArgument();
      args.add( aircraft, "main_inv_no_db_id", "main_inv_no_id" );
      QuerySet q = QuerySetFactory.getInstance().executeQueryTable( "sched_stask", args );

      int expectedTaskCount = 1;
      int actualTaskCount = q.getRowCount();
      assertEquals( "There is no task created for the inventory", expectedTaskCount,
            actualTaskCount );

      q.next();
      TaskTaskKey expectedRequirementDefn = requirementDefinition;
      TaskTaskKey actualRequirementDefn = q.getKey( TaskTaskKey.class, "task_db_id", "task_id" );
      assertEquals( "The created task is against incorrect requirement definition",
            expectedRequirementDefn, actualRequirementDefn );

      // verify that TaskCreatedEvent is published in the axon table and that that task is linked to
      // task definition.
      QuerySet taskCreatedEventQs = axonDomainEventDao.findByPayLoadType( TaskCreatedEvent.class );
      assertEquals( 1, taskCreatedEventQs.getRowCount() );
      taskCreatedEventQs.next();
      assertEquals( requirementDefinition,
            objectMapper
                  .readValue( taskCreatedEventQs.getString( AxonDomainEventDao.PAYLOAD_COLUMN ),
                        TaskCreatedEvent.class )
                  .getTaskTaskKey() );
   }


   /**
    * Purpose: The test is actually testing baseline synch functionality not the task definition
    * functionality. The baseline is being obsoleted and as a result, the actual gets cancelled.
    *
    * Verify cancel active requirement when requirement definition is obsoleted
    *
    * <pre>
    *  Given an obsoleted requirement that is not based on any conditions
    *    And the requirement is not managed by a maintenance program
    *    And a task exists for an inventory based on the previous revision of the requirement
    *    And a request exists to synchronize the requirement for the inventory
    *   When the requirement for the inventory is synchronized
    *   Then the active requirement task will be cancelled for the inventory
    * </pre>
    */

   @Test
   public void itCancelsActiveReqTaskWhenReqDefnIsObsoleted() throws Exception {

      // Given an obsoleted requirement that is not based on any conditions
      // And the requirement is not managed by a maintenance program
      final AssemblyKey aircraftAssembly = Domain.createAircraftAssembly();

      final TaskTaskKey requirementDefinition =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition requirementDefinition ) {
                  requirementDefinition.againstConfigurationSlot(
                        Domain.readRootConfigurationSlot( aircraftAssembly ) );
                  requirementDefinition.setOnCondition( false );
                  requirementDefinition.setRevisionNumber( REVISION_1 );
                  requirementDefinition.setStatus( RefTaskDefinitionStatusKey.OBSOLETE );
               }

            } );

      final InventoryKey aircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aircraft ) {
            aircraft.allowSynchronization();
            aircraft.setAssembly( aircraftAssembly );

         }
      } );

      final TaskKey task = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement requirement ) {
            requirement.setInventory( aircraft );
            requirement.setDefinition( requirementDefinition );
            requirement.setStatus( RefEventStatusKey.ACTV );
         }

      } );

      // Given a request exists to synchronize the requirement for the inventory
      requestSync( aircraft );

      // When the requirement for the inventory is synchronized
      inventorySync();

      // Then the active requirement task will be cancelled for the inventory
      assertEquals( "Obsoleted requirement was not cancelled", RefEventStatusKey.CANCEL,
            getTaskDefinitionStatus( task ) );

      // one task cancelled event should be published.
      List<TaskCancelledEvent> events = eventBus.getEventMessages().stream()
            .filter( event -> ( event.getPayload() instanceof TaskCancelledEvent ) )
            .map( event -> ( TaskCancelledEvent ) event.getPayload() )
            .collect( Collectors.toList() );
      assertEquals( 1, events.size() );
      assertEquals( task, events.get( 0 ).getTaskKey() );
   }


   /**
    * Given an assembly with an existing requirement definition. And an aircraft based on this
    * assembly has an activated instance of that requirement. And the aircraft is currently locked.
    * And there is an activated requirement definition revision. When the baseline synchronization
    * occurs. Then the instance of the requirement should not be updated against the locked
    * aircraft.
    */
   @Test
   public void itAvoidsUpdatingTheRequirementWhenAircraftIsLocked() throws Exception {
      final AssemblyKey aircraftAssembly = Domain.createAircraftAssembly();

      final TaskTaskKey originalRequirementDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition requirementDefinition ) {
                  requirementDefinition.againstConfigurationSlot(
                        Domain.readRootConfigurationSlot( aircraftAssembly ) );
                  requirementDefinition.setStatus( SUPRSEDE );

               }

            } );

      final InventoryKey aircraftKey = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aircraft ) {
            aircraft.setAssembly( aircraftAssembly );
            aircraft.lock();
            aircraft.allowSynchronization();
         }

      } );

      final TaskKey requirement = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement requirement ) {
            requirement.setDefinition( originalRequirementDefn );
            requirement.setInventory( aircraftKey );

         }

      } );

      Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

         @Override
         public void configure( RequirementDefinition requirementDefinition ) {
            requirementDefinition.setPreviousRevision( originalRequirementDefn );
            requirementDefinition.setStatus( ACTV );
            requirementDefinition.setRevisionNumber( REVISION_2 );
         }

      } );

      // When the requirement for the inventory is synchronized
      inventorySync();

      TaskTaskKey actualRequirementDefinition = getTaskDefinition( requirement );

      Assert.assertThat( actualRequirementDefinition, equalTo( originalRequirementDefn ) );

   }


   /**
    * Verify that an active requirement task is updated to the latest revision of its definition
    * when it is synchronized.
    *
    * <pre>
    *  Given an activated one-time requirement that has been previously revised
    *  And the requirement is not based on any conditions
    *  And the requirement is not managed by a maintenance program
    *  And a request exists to synchronize the requirement for the inventory
    *  And an active task exists for an inventory based on the previous revision of the requirement
    *  When the requirement for the inventory is synchronized
    *  Then the active requirement task is updated for the inventory
    * </pre>
    */

   @Test
   public void itActiveReviseRequirementTaskIsUpdatedTheInventory() throws Exception {

      // Given an activated one-time requirement that has been previously revised
      // And the requirement is not based on any conditions
      // And the requirement is not managed by a maintenance program

      final AssemblyKey aircraftAssembly = Domain.createAircraftAssembly();

      final TaskTaskKey requirementDefinitionKey =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition requirementDefinition ) {
                  requirementDefinition.againstConfigurationSlot(
                        Domain.readRootConfigurationSlot( aircraftAssembly ) );
                  requirementDefinition.setOnCondition( false );
                  requirementDefinition.setRevisionNumber( REVISION_1 );
               }

            } );

      final InventoryKey aircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aircraft ) {
            aircraft.allowSynchronization();
            aircraft.setAssembly( aircraftAssembly );

         }
      } );

      // Given an active task exists for an inventory based on the previous revision of the
      // requirement
      final TaskTaskKey revisedRequirementDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition requirementDefinition ) {
                  requirementDefinition.againstConfigurationSlot(
                        Domain.readRootConfigurationSlot( aircraftAssembly ) );
                  requirementDefinition.setPreviousRevision( requirementDefinitionKey );
                  requirementDefinition.setRevisionNumber( REVISION_2 );
                  requirementDefinition.setStatus( ACTV );
               }
            } );

      final TaskKey task = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement requirement ) {
            requirement.setInventory( aircraft );
            requirement.setDefinition( requirementDefinitionKey );
            requirement.setStatus( RefEventStatusKey.ACTV );
         }

      } );

      // Given a request exists to synchronize the requirement for the inventory
      requestSync( aircraft );

      // When the requirement for the inventory is synchronized
      inventorySync();

      // Then the active requirement task is updated for the inventory
      List<TaskKey> tasks = TaskDefnUtils.getActualTasks( revisedRequirementDefn, aircraft,
            RefEventStatusKey.ACTV );
      Assert.assertEquals( "Unexpected number of ACTV tasks initialized.", 1, tasks.size() );

      TaskTaskKey expectedReqDefn = revisedRequirementDefn;
      TaskTaskKey actualReqDefn = getTaskDefinition( task );
      Assert.assertEquals( "Unexpected number of requirement revision", expectedReqDefn,
            actualReqDefn );

   }


   /**
    * Verify that block tasks are created.
    *
    * Note: Integration test which is this one will verify that the request in the baseline queue is
    * processed.
    *
    * <pre>
    *  Given an activated one-time block
    *    And the block is not based on any conditions
    *    And the block is not managed by a maintenance program
    *    And an active requirement is assigned to the block
    *    And a request exists to synchronize the block for the inventory
    *   When the block for the inventory is synchronized
    *   Then the active block task is initialized for the inventory
    * </pre>
    */

   @Test
   public void itActiveOneTimeBlockTaskIsIntializedTheInventory() throws Exception {

      // Given an activated one-time block
      // And the block is not based on any conditions
      // And the block is not managed by a maintenance program

      final AssemblyKey aircraftAssembly = Domain.createAircraftAssembly();

      final TaskTaskKey blockDefinition =
            Domain.createBlockDefinition( new DomainConfiguration<BlockDefinition>() {

               @Override
               public void configure( BlockDefinition blockDefinition ) {
                  blockDefinition.setConfigurationSlot(
                        Domain.readRootConfigurationSlot( aircraftAssembly ) );
                  blockDefinition.setOnCondition( false );
               }

            } );

      final InventoryKey aircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aircraft ) {
            aircraft.allowSynchronization();
            aircraft.setAssembly( aircraftAssembly );

         }
      } );

      Domain.createBlock( new DomainConfiguration<Block>() {

         @Override
         public void configure( Block block ) {
            block.setDefinition( blockDefinition );
            block.setInventory( aircraft );
            block.setStatus( RefEventStatusKey.ACTV );
         }
      } );

      // Given a request exists to synchronize the block for the inventory
      requestSync( aircraft );

      // When the block for the inventory is synchronized
      inventorySync();

      // Then the active block task is initialized for the inventory
      DataSetArgument args = new DataSetArgument();
      args.add( aircraft, "main_inv_no_db_id", "main_inv_no_id" );
      QuerySet q = QuerySetFactory.getInstance().executeQueryTable( "sched_stask", args );

      int expectedTaskCount = 1;
      int actualTaskCount = q.getRowCount();
      assertEquals( "There is no task created for the inventory", expectedTaskCount,
            actualTaskCount );

      q.next();
      TaskTaskKey expectedBlockDefn = blockDefinition;
      TaskTaskKey actualBlockDefn = q.getKey( TaskTaskKey.class, "task_db_id", "task_id" );
      assertEquals( "The created block task is against incorrect block definition",
            expectedBlockDefn, actualBlockDefn );
   }


   /**
    * Verify that an active block task is updated to the latest revision of its definition when it
    * is synchronized.
    *
    * <pre>
    *  Given an activated one-time block that has been previously revised
    *    And the block is not based on any conditions
    *    And the block is not managed by a maintenance program
    *    And a task exists for an inventory based on the previous revision of the block
    *    And a request exists to synchronize the block for the inventory
    *   When the block for the inventory is synchronized
    *   Then the active block task is updated to the latest revision
    * </pre>
    */

   @Test
   public void itActiveReviseBlockTaskIsUpdatedTheInventory() throws Exception {

      // Given an activated one-time block that has been previously revised
      // And the block is not based on any conditions
      // And the block is not managed by a maintenance program
      final AssemblyKey aircraftAssembly = Domain.createAircraftAssembly();

      final InventoryKey aircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aircraft ) {
            aircraft.allowSynchronization();
            aircraft.setAssembly( aircraftAssembly );

         }
      } );

      final TaskTaskKey blockDefinitionKey =
            Domain.createBlockDefinition( new DomainConfiguration<BlockDefinition>() {

               @Override
               public void configure( BlockDefinition blockDefinition ) {
                  blockDefinition.setConfigurationSlot(
                        Domain.readRootConfigurationSlot( aircraftAssembly ) );
                  blockDefinition.setOnCondition( false );
                  blockDefinition.setRevisionNumber( REVISION_1 );

               }

            } );

      // Given a task exists for an inventory based on the previous revision of the block
      final TaskTaskKey revisedBlockDefinition =
            Domain.createBlockDefinition( new DomainConfiguration<BlockDefinition>() {

               @Override
               public void configure( BlockDefinition blockDefinition ) {
                  blockDefinition.setConfigurationSlot(
                        Domain.readRootConfigurationSlot( aircraftAssembly ) );
                  blockDefinition.setPreviousRevision( blockDefinitionKey );
                  blockDefinition.setOnCondition( false );
                  blockDefinition.setRevisionNumber( REVISION_2 );
                  blockDefinition.setStatus( ACTV );
               }

            } );

      final TaskKey blockTask = Domain.createBlock( new DomainConfiguration<Block>() {

         @Override
         public void configure( Block block ) {
            block.setInventory( aircraft );
            block.setDefinition( blockDefinitionKey );
            block.setStatus( RefEventStatusKey.ACTV );
         }
      } );

      // Given a request exists to synchronize the block for the inventory
      requestSync( aircraft );

      // When the block for the inventory is synchronized
      inventorySync();

      // Then the active block task is updated to the latest revision
      List<TaskKey> tasks = TaskDefnUtils.getActualTasks( revisedBlockDefinition, aircraft,
            RefEventStatusKey.ACTV );
      Assert.assertEquals( "Unexpected number of ACTV tasks initialized.", 1, tasks.size() );

      TaskTaskKey expectedReqDefn = revisedBlockDefinition;
      TaskTaskKey actualReqDefn = getTaskDefinition( blockTask );
      Assert.assertEquals( "Unexpected number of block revision", expectedReqDefn, actualReqDefn );

   }


   /**
    * Verify that an active job card task is updated to the latest revision of its definition when
    * it is synchronized.
    *
    * <pre>
    * Given an activated job card definition that has been previously revised
    *   And the job card definition is assigned to a requirement definition
    *   And a requirement task exists based on the requirement definition
    *   And a job card task exists for an inventory based on the previous revision of the JIC
    *   And the job card task is assigned to the requirement task
    *   And a request exists to synchronize the JIC for the inventory
    *  When the JIC for the inventory is synchronized
    *  Then the active JIC task is updated to the latest revision
    * </pre>
    */
   @Test
   public void itUpdatesJicTaskAgainstRevisedJicDefnWhenInventoryIsMarkedForSync()
         throws Exception {

      final AssemblyKey aircraftAssembly = Domain.createAircraftAssembly();

      final InventoryKey aircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aircraft ) {
            aircraft.allowSynchronization();
            aircraft.setAssembly( aircraftAssembly );

         }
      } );

      // Given an activated job card definition that has been previously revised
      final TaskTaskKey jobCardDefinitionKey =
            Domain.createJobCardDefinition( new DomainConfiguration<JobCardDefinition>() {

               @Override
               public void configure( JobCardDefinition jobCardDefinition ) {
                  jobCardDefinition.setConfigurationSlot(
                        Domain.readRootConfigurationSlot( aircraftAssembly ) );
                  jobCardDefinition.setRevisionNumber( REVISION_1 );
               }

            } );

      final TaskTaskKey revisedJobCardDefinition =
            Domain.createJobCardDefinition( new DomainConfiguration<JobCardDefinition>() {

               @Override
               public void configure( JobCardDefinition jobCardDefinition ) {
                  jobCardDefinition.setConfigurationSlot(
                        Domain.readRootConfigurationSlot( aircraftAssembly ) );
                  jobCardDefinition.setPreviousRevision( jobCardDefinitionKey );
                  jobCardDefinition.setRevisionNumber( REVISION_2 );
                  jobCardDefinition.setStatus( ACTV );
               }

            } );

      // Given the job card definition is assigned to a requirement definition
      final TaskTaskKey requirementDefinition =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition reqDefn ) {
                  reqDefn.addJobCardDefinition( jobCardDefinitionKey );
                  reqDefn.addJobCardDefinition( revisedJobCardDefinition );
               }
            } );

      // Given a requirement task exists based on the requirement definition
      // And a job card task exists for an inventory based on the previous revision of the JIC
      // And the job card task is assigned to the requirement task
      final TaskKey jobCardTask = Domain.createJobCard( new DomainConfiguration<JobCard>() {

         @Override
         public void configure( JobCard jobCard ) {
            jobCard.setInventory( aircraft );
            jobCard.setDefinition( jobCardDefinitionKey );
         }
      } );

      Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement req ) {
            req.setDefinition( requirementDefinition );
            req.addJobCard( jobCardTask );
         }

      } );

      // Given a request exists to synchronize the block for the inventory
      requestSync( aircraft );

      // When the block for the inventory is synchronized
      inventorySync();

      // Then the active JIC task is updated to the latest revision
      List<TaskKey> tasks = TaskDefnUtils.getActualTasks( revisedJobCardDefinition, aircraft,
            RefEventStatusKey.ACTV );
      Assert.assertEquals( "Unexpected number of ACTV tasks initialized.", 1, tasks.size() );

      TaskTaskKey expectedJobCardDefn = revisedJobCardDefinition;
      TaskTaskKey actualJobCardDefn = getTaskDefinition( jobCardTask );
      Assert.assertEquals( "Unexpected number of job card revision", expectedJobCardDefn,
            actualJobCardDefn );
   }


   /**
    * Verify that a recurring forecast task using scheduling rule is created when it is
    * synchronzied.
    *
    * <pre>
    * Given an activated recurring requirement that is not based on any conditions
    *   And the requirement is not manged by a maintenance program
    *   And a request exists to synchronize the inventory
    *  When the requirement for the inventory is synchronized
    *  Then an active requirement task is initialized for the inventory
    *   And a forecast requirement task is initialized for the inventory
    *   And a forecast task is forecast using the requirement's scheduling rule
    * </pre>
    */
   @Test
   public void itCreatedRecurringForecastTaskWhenInventoryIsMarkedForSync() throws Exception {

      // Given an activated recurring requirement that is not based on any conditions
      // And the requirement is not manged by a maintenance program
      final PartNoKey aircraftPart = Domain.createPart();
      final AssemblyKey aircraftAssembly = createAcftAssyWithAcftPart( aircraftPart );
      final ConfigSlotKey aircraftRootConfigSlot =
            Domain.readRootConfigurationSlot( aircraftAssembly );

      final InventoryKey aircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aircraft ) {
            aircraft.setAssembly( aircraftAssembly );
            aircraft.setPart( aircraftPart );
            aircraft.allowSynchronization();
         }
      } );

      final TaskTaskKey requirementDefinition =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition requirementDefinition ) {
                  requirementDefinition.againstConfigurationSlot( aircraftRootConfigSlot );
                  requirementDefinition.setOnCondition( false );
                  requirementDefinition.setRecurring( true );
                  requirementDefinition.setScheduledFromManufacturedDate();
                  requirementDefinition.setMinimumForecastRange( MIN_FORECAST_RANGE );
                  requirementDefinition.addRecurringSchedulingRule( DataTypeKey.CDY,
                        REPEAT_INTERVAL );
               }
            } );

      // Given a request exists to synchronize the inventory
      requestSync( aircraft );

      // When the block for the inventory is synchronized
      inventorySync();

      // Then an active requirement task is initialized for the inventory
      List<TaskKey> actualReqTask =
            TaskDefnUtils.getActualTasks( requirementDefinition, aircraft, RefEventStatusKey.ACTV );
      Assert.assertEquals( "Unexpected number of ACTV tasks initialized.", 1,
            actualReqTask.size() );

      // Then a forecast requirement task is initialized for the inventory
      List<TaskKey> actualForecastReqTask = TaskDefnUtils.getActualTasks( requirementDefinition,
            aircraft, RefEventStatusKey.FORECAST );
      Assert.assertEquals( "Unexpected number of FORECAST task initialized.", 1,
            actualForecastReqTask.size() );

      // Then a forecast task is forecast using the requirement's scheduling rule
      EvtSchedDeadTable evtSchedDeadTable =
            EvtSchedDeadTable.findByPrimaryKey( actualForecastReqTask.get( 0 ), DataTypeKey.CDY );

      Assert.assertEquals( "Unexpected scheduling rule Threshold.",
            REPEAT_INTERVAL.compareTo( new BigDecimal( evtSchedDeadTable.getIntervalQt() ) ), 0 );

      // verify that two TaskCreatedEvent are published in the axon table and that that task is
      // linked to task definition.
      Set<TaskKey> createdTasks = new HashSet<>();
      QuerySet taskCreatedEventQuerySet =
            axonDomainEventDao.findByPayLoadType( TaskCreatedEvent.class );
      assertEquals( 2, taskCreatedEventQuerySet.getRowCount() );
      while ( taskCreatedEventQuerySet.next() ) {
         TaskCreatedEvent event = objectMapper.readValue(
               taskCreatedEventQuerySet.getString( AxonDomainEventDao.PAYLOAD_COLUMN ),
               TaskCreatedEvent.class );
         createdTasks.add( event.getNewlyCreatedTaskKey() );
         assertEquals( requirementDefinition, event.getTaskTaskKey() );
      } ;
      assertEquals( 2, createdTasks.size() );

      // verify task driving deadline rescheduled events are published for two newly created tasks.
      Set<TaskKey> taskKeys = new HashSet<>();
      QuerySet taskDrivingDeadlineRescheduledEventQuerySet =
            axonDomainEventDao.findByPayLoadType( TaskDrivingDeadlineRescheduledEvent.class );
      while ( taskDrivingDeadlineRescheduledEventQuerySet.next() ) {
         TaskDrivingDeadlineRescheduledEvent event = objectMapper.readValue(
               taskDrivingDeadlineRescheduledEventQuerySet
                     .getString( AxonDomainEventDao.PAYLOAD_COLUMN ),
               TaskDrivingDeadlineRescheduledEvent.class );
         taskKeys.add( event.getTaskKey() );
      }
      assertEquals( 2, taskKeys.size() );
   }


   /**
    * Verify that a requirement task cannot be assigned to a block that is committed.
    *
    * <pre>
    * Given a superseded block definition
    *   and an activated block definition based on the superseded one
    *   and a requirement definition assigned to the activated block definition
    *   and an inventory applicable to both definitions and allowing synchronization
    *   and a block task for the inventory based on the previous block definition revision
    *   and the block task is assigned to a committed work package
    *   and a request to synchronize task definitions for the inventory
    * When the inventory has its task definitions synchronized
    * Then the block task is not updated to the latest revision
    *   and the requirement task is initialized but not assigned to the block task
    * </pre>
    *
    */
   @Test
   public void itCannotAssignRequirementTaskToCommittedBlockTask() throws Exception {

      // Given a assembly

      final PartNoKey aircraftPart = Domain.createPart();
      final AssemblyKey aircraftAssembly = createAcftAssyWithAcftPart( aircraftPart );
      final ConfigSlotKey aircraftRootConfigSlot =
            Domain.readRootConfigurationSlot( aircraftAssembly );

      // Given an aircraft applicable to both definitions and allowing synchronization.
      final InventoryKey aircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aircraft ) {
            aircraft.setAssembly( aircraftAssembly );
            aircraft.setPart( aircraftPart );
            aircraft.allowSynchronization();
         }
      } );

      // Given an activated recurring requirement definition
      final TaskTaskKey reqDefinition =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition reqDefn ) {
                  reqDefn.againstConfigurationSlot( aircraftRootConfigSlot );
                  reqDefn.setRecurring( true );
                  reqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
               }
            } );

      // Given an superseded recurring block definition
      final TaskTaskKey blockDefinition =
            Domain.createBlockDefinition( new DomainConfiguration<BlockDefinition>() {

               @Override
               public void configure( BlockDefinition blockDefn ) {
                  blockDefn.setConfigurationSlot( aircraftRootConfigSlot );
                  blockDefn.addRequirementDefinition( reqDefinition );
                  blockDefn.setRevisionNumber( REVISION_1 );
                  blockDefn.setRecurring( true );
                  blockDefn.setStatus( RefTaskDefinitionStatusKey.SUPRSEDE );
               }
            } );

      // Create a block task without requirement task
      final TaskKey blockTask = Domain.createBlock( new DomainConfiguration<Block>() {

         @Override
         public void configure( Block block ) {
            block.setDefinition( blockDefinition );
            block.setStatus( RefEventStatusKey.ACTV );
            block.setInventory( aircraft );
         }
      } );

      // Create a work package and assign the block task into it
      Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage builder ) {
            builder.addTask( blockTask );
            builder.setAircraft( aircraft );
            builder.setStatus( RefEventStatusKey.COMMIT );
         }
      } );

      // Revised the given block definition
      Domain.createBlockDefinition( new DomainConfiguration<BlockDefinition>() {

         @Override
         public void configure( BlockDefinition blockDefn ) {
            blockDefn.setPreviousRevision( blockDefinition );
            blockDefn.setRevisionNumber( REVISION_2 );
            blockDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
            blockDefn.setRecurring( true );
            blockDefn.setConfigurationSlot( aircraftRootConfigSlot );
         }
      } );

      // Given a request exists to synchronize the inventory
      requestSync( aircraft );

      // When the inventory has its task definitions synchronized
      inventorySync();

      // Then the block task is not updated to the latest revision
      final Integer actualBlockRevision = readTaskRevision( blockTask );
      final Integer expectedBlockRevision = REVISION_1;
      Assert.assertEquals(
            "Unexpected Result: the block task is updated to the the latest revision.",
            expectedBlockRevision, actualBlockRevision );

      // Then the requirement task is initialized but not assigned to the block task
      final List<TaskKey> reqTasks = getActualTasks( aircraft, REQ );
      final int actualReqTaskCount = reqTasks.size();
      final int expectedReqTaskCount = 1;
      Assert.assertEquals(
            "Unexpected Result: There is an incorrect number of requirement tasks initialized.",
            expectedReqTaskCount, actualReqTaskCount );

      final TaskKey actualHighestTask = readHighestTask( reqTasks.get( 0 ) );
      final TaskKey expectedTask = reqTasks.get( 0 );
      Assert.assertEquals( "Unexpected Result: the requirement task is assigned to the block task.",
            expectedTask, actualHighestTask );
   }


   /**
    * Verify that when a block definition is revised by adding a requirement definition and there
    * exists a committed requirement task that the requirement task is not assigned to the existing
    * block task.
    *
    * <pre>
    * Given an activated requirement definition
    *   and a superseded block definition
    *   and an activated revision of the block definition to which the requirement definition is assigned
    *   and an inventory applicable to both definitions and allowing synchronization
    *   and a block task based on the superseded block definition initialized for the inventory
    *   and an unassigned, committed requirement task based on the requirement definition initialized for the inventory
    *   and a request to synchronize task definitions for the inventory
    * When the inventory has its task definitions synchronized
    * Then the requirement task is not assigned to the block task
    * </pre>
    *
    */
   @Test
   public void itCannotAssignCommittedRequirementTaskToBlockTask() throws Exception {

      // Given a assembly
      final PartNoKey aircraftPart = Domain.createPart();
      final AssemblyKey aircraftAssembly = createAcftAssyWithAcftPart( aircraftPart );
      final ConfigSlotKey aircraftRootConfigSlot =
            Domain.readRootConfigurationSlot( aircraftAssembly );

      // Given an aircraft applicable to both definitions and allowing synchronization.
      final InventoryKey aircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aircraft ) {
            aircraft.setAssembly( aircraftAssembly );
            aircraft.setPart( aircraftPart );
            aircraft.allowSynchronization();
         }
      } );

      // Given an activated requirement definition
      final TaskTaskKey reqDefinition =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition reqDefn ) {
                  reqDefn.againstConfigurationSlot( aircraftRootConfigSlot );
                  reqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
               }
            } );

      // Given an superseded block definition without requirement definition
      final TaskTaskKey blockDefinition =
            Domain.createBlockDefinition( new DomainConfiguration<BlockDefinition>() {

               @Override
               public void configure( BlockDefinition blockDefn ) {
                  blockDefn.setConfigurationSlot( aircraftRootConfigSlot );
                  blockDefn.setRevisionNumber( REVISION_1 );
                  blockDefn.setStatus( RefTaskDefinitionStatusKey.SUPRSEDE );
               }
            } );

      // Given an activated revision of the block definition with the requirement definition
      Domain.createBlockDefinition( new DomainConfiguration<BlockDefinition>() {

         @Override
         public void configure( BlockDefinition blockDefn ) {
            blockDefn.setPreviousRevision( blockDefinition );
            blockDefn.setRevisionNumber( REVISION_2 );
            blockDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
            blockDefn.setConfigurationSlot( aircraftRootConfigSlot );
            blockDefn.addRequirementDefinition( reqDefinition );
         }
      } );

      // Given a block task based on the superseded block definition for the inventory
      Domain.createBlock( new DomainConfiguration<Block>() {

         @Override
         public void configure( Block block ) {
            block.setDefinition( blockDefinition );
            block.setStatus( RefEventStatusKey.ACTV );
            block.setInventory( aircraft );
         }
      } );

      // Given an unassigned, committed requirement task based on the requirement definition for
      // the inventory
      final TaskKey reqTask = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement requirement ) {
            requirement.setDefinition( reqDefinition );
            requirement.setStatus( RefEventStatusKey.COMMIT );
            requirement.setInventory( aircraft );
         }
      } );

      // Given a request to synchronize task definitions for the inventory
      requestSync( aircraft );

      // When the task definitions for the inventory are synchronized
      inventorySync();

      // Then the requirement task is not assigned to the block task
      final TaskKey actualHighestTask = readHighestTask( reqTask );
      final TaskKey expectedTask = reqTask;
      Assert.assertEquals( "Unexpected Result: the requirement task is assigned to the block task.",
            expectedTask, actualHighestTask );
   }


   /**
    * Verify when a block definition is revised by removing a requirement definition that an
    * existing requirement task is unassigned from the existing block task when their definitions
    * are synchronized for their inventory.
    *
    * <pre>
    * Given an activated requirement definition
    *   and a superseded block definition
    *   and an activated revision of the block definition to which the requirement definition is removed
    *   and an inventory applicable to both definitions and allowing synchronization
    *   and a block task based on the superseded block definition initialized for the inventory
    *   and an requirement task based on the requirement definition initialized for the inventory and assigned to the block task
    *   and a request to synchronize task definitions for the inventory
    * When the inventory has its task definitions synchronized
    * Then the requirement task is unassigned from the block task
    * </pre>
    *
    */
   @Test
   public void itUnassignsRequirementTaskFromBlockTask() throws Exception {

      // Given a assembly
      final PartNoKey aircraftPart = Domain.createPart();
      final AssemblyKey aircraftAssembly = createAcftAssyWithAcftPart( aircraftPart );
      final ConfigSlotKey aircraftRootConfigSlot =
            Domain.readRootConfigurationSlot( aircraftAssembly );

      // Given an aircraft applicable to both definitions and allowing synchronization.
      final InventoryKey aircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aircraft ) {
            aircraft.setAssembly( aircraftAssembly );
            aircraft.setPart( aircraftPart );
            aircraft.allowSynchronization();
         }
      } );

      // Given an activated requirement definition
      final TaskTaskKey reqDefinition =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition reqDefn ) {
                  reqDefn.againstConfigurationSlot( aircraftRootConfigSlot );
                  reqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
                  reqDefn.setOnCondition( false );
               }
            } );

      // Given an superseded block definition with requirement definition
      final TaskTaskKey supersededBlockDefinition =
            Domain.createBlockDefinition( new DomainConfiguration<BlockDefinition>() {

               @Override
               public void configure( BlockDefinition blockDefn ) {
                  blockDefn.setConfigurationSlot( aircraftRootConfigSlot );
                  blockDefn.setRevisionNumber( REVISION_1 );
                  blockDefn.setStatus( RefTaskDefinitionStatusKey.SUPRSEDE );
                  blockDefn.addRequirementDefinition( reqDefinition );
                  blockDefn.setOnCondition( false );
               }
            } );

      // Given an activated revision of the block definition the requirement definition is removed
      // from
      Domain.createBlockDefinition( new DomainConfiguration<BlockDefinition>() {

         @Override
         public void configure( BlockDefinition blockDefn ) {
            blockDefn.setPreviousRevision( supersededBlockDefinition );
            blockDefn.setRevisionNumber( REVISION_2 );
            blockDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
            blockDefn.setConfigurationSlot( aircraftRootConfigSlot );
            blockDefn.setOnCondition( false );
         }
      } );

      // Given a requirement task based on the requirement definition for
      // the inventory
      final TaskKey reqTask = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement requirement ) {
            requirement.setDefinition( reqDefinition );
            requirement.setStatus( RefEventStatusKey.ACTV );
            requirement.setInventory( aircraft );
         }
      } );

      // Given a block task based on the superseded block definition for the inventory
      Domain.createBlock( new DomainConfiguration<Block>() {

         @Override
         public void configure( Block block ) {
            block.setDefinition( supersededBlockDefinition );
            block.setStatus( RefEventStatusKey.ACTV );
            block.setInventory( aircraft );
            block.addRequirement( reqTask );
         }
      } );

      // Given a request to synchronize task definitions for the inventory
      requestSync( aircraft );

      // When the task definitions for the inventory are synchronized
      inventorySync();

      // Then the requirement task is removed from the block task
      final TaskKey actualHighestTask = readHighestTask( reqTask );
      final TaskKey expectedTask = reqTask;
      Assert.assertEquals(
            "Unexpected Result: the requirement task is still assigned to the block task.",
            expectedTask, actualHighestTask );
   }


   /**
    * Verify that when tasks are initialized for a recurring block chain definition that contains a
    * recurring requirement definition, using the same scheduling rule and same scheduled from date,
    * that one requirement task is assigned to each block chain task sharing the same deadline (and
    * there are no loose requirement tasks).
    *
    * Note; this behaviour is the same regardless of whether the configuration parameter
    * BSYNC_IGNORE_ZIPPING_DATE_MISALIGNMENT is set to TRUE or FALSE.
    *
    * <pre>
    *    Given an activated recurring requirement definition using a calendar scheduling rule and scheduled from date
    *      And an activated recurring block chain definition using the same calendar scheduling rule and scheduled from date
    *      And an inventory applicable to both definitions and allowing synchronization
    *     When the inventory has its task definitions synchronized
    *     Then the requirement tasks are assigned to the block chain tasks with the same deadline
    *      And there are no unassigned requirement tasks
    *      And there are no block chain tasks without assigned requirement tasks
    * </pre>
    */
   @Test
   public void itZipsRecurringReqTasksToRecurringBlockChainTasksWhenSameScheduling()
         throws Exception {

      // Set up an aircraft assembly to be used to create the block chain and requirement
      // definitions against, as well as to create an aircraft.
      //
      // Note: we need to create a part due to a validation performed during task initialization,
      // which ensures the inventory part matches the the part number of the task definition's
      // assembly.
      final PartNoKey aircraftPart = Domain.createPart();
      final AssemblyKey aircraftAssembly = createAcftAssyWithAcftPart( aircraftPart );
      final ConfigSlotKey aircraftRootConfigSlot =
            Domain.readRootConfigurationSlot( aircraftAssembly );

      // Given an inventory applicable to both definitions and allowing synchronization.
      final InventoryKey aircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aircraft ) {
            aircraft.setAssembly( aircraftAssembly );
            aircraft.setPart( aircraftPart );
            aircraft.allowSynchronization();
         }
      } );

      // Given an activated recurring requirement definition using a calendar scheduling rule and
      // scheduled from date
      final TaskTaskKey requirementDefinition =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition reqDefn ) {
                  reqDefn.againstConfigurationSlot( aircraftRootConfigSlot );
                  reqDefn.setRecurring( true );
                  reqDefn.setStatus( ACTV );

                  reqDefn.setScheduledFromEffectiveDate( NOW );
                  reqDefn.setMinimumForecastRange( 50 );
                  reqDefn.addSchedulingRule( CDY, 10 );
               }
            } );

      // Given an activated recurring block chain definition using the same calendar scheduling rule
      // and scheduled from date
      Domain.createBlockChainDefinition( new DomainConfiguration<BlockChainDefinition>() {

         @Override
         public void configure( BlockChainDefinition blockChainDefn ) {
            blockChainDefn.setName( BLOCK_CHAIN_A_NAME );
            blockChainDefn.setRecurring( true );
            blockChainDefn.setConfigurationSlot( aircraftRootConfigSlot );
            blockChainDefn.setStatus( ACTV );

            blockChainDefn.addBlock( BLOCK_ORDER_1, BLOCK_A1_CODE, BLOCK_A1_NAME );
            blockChainDefn.addBlock( BLOCK_ORDER_2, BLOCK_A2_CODE, BLOCK_A2_NAME );
            blockChainDefn.addBlock( BLOCK_ORDER_3, BLOCK_A3_CODE, BLOCK_A3_NAME );

            blockChainDefn.addRequirement( requirementDefinition, 1, 1 );

            // Same scheduling as the requirement.
            blockChainDefn.setScheduledFromEffectiveDate( NOW );
            blockChainDefn.setMinimumForecastRange( new BigDecimal( 50 ) );
            blockChainDefn.addRecurringSchedulingRule( CDY, new BigDecimal( 10 ) );
         }
      } );

      // Given a request exists to synchronize the inventory
      requestSync( aircraft );

      // When the inventory has its task definitions synchronized
      inventorySync();

      // Then requirement tasks are initialized for the inventory using the scheduling rule
      List<TaskKey> reqTasks = getActualTasks( aircraft, REQ );
      Assert.assertEquals( "Incorrect number of requirement tasks initialized", 7,
            reqTasks.size() );

      // And block chain tasks are initialized for the inventory using the scheduling rule
      List<TaskKey> blockTasks = getActualTasks( aircraft, BLOCK );
      Assert.assertEquals( "Incorrect number of block tasks initialized", 7, blockTasks.size() );

      // And the requirement tasks are assigned to the block chain tasks with the same deadline
      Map<TaskKey, Date> reqTaskDueDateMap = getActualTasksDueDateMap( reqTasks, CDY );
      Map<TaskKey, Date> blockTaskDueDateMap = getActualTasksDueDateMap( blockTasks, CDY );

      for ( Entry<TaskKey, Date> entry : reqTaskDueDateMap.entrySet() ) {
         TaskKey hTaskKey = readHighestTask( entry.getKey() );

         Date blockDueDate = blockTaskDueDateMap.get( hTaskKey );
         Date reqDueDate = entry.getValue();

         Assert.assertEquals(
               "Requiement task is not assign to block task against the same schedulling rule.",
               blockDueDate, reqDueDate );
      }
   }


   /**
    * Verify when a block chain definition is revised by adding a requirement definition that
    * existing requirement tasks are assigned to existing block chain tasks when their definitions
    * are synchronized for their inventory.
    *
    * <pre>
    * Given an activated recurring requirement definition using a calendar scheduling rule and scheduled from date
    *   And a superseded recurring block chain definition using the same calendar scheduling rule and scheduled from date
    *   And an activated revision of the block chain definition to which the requirement definition is assigned
    *   And an inventory applicable to both definitions and allowing synchronization
    *   And block chain tasks based on the superseded block chain definition initialized for the inventory
    *   And unassigned requirement tasks based on the requirement definition initialized for the inventory
    *  When the inventory has its task definitions synchronized
    *  Then the requirement tasks are assigned to the block chain tasks with the same deadline
    * </pre>
    *
    * Note: this test causes the block chain tasks to be synchronized with its revised definition.
    * As a result, their deadlines will be recalculated and that logic rounds up the deadline to the
    * end-of-day. As such, in order for the zipping logic to work properly the deadlines of the REQ
    * tasks should/are also be set to their end-of-day.
    *
    */
   @Test
   public void itZipsExistingReqTasksIntoExistingBlockChainTasksAfterAssignReqToBlockChain()
         throws Exception {

      // Set up an aircraft assembly to be used to create the block chain and requirement
      // definitions against, as well as to create an aircraft.
      final PartNoKey aircraftPart = Domain.createPart();
      final AssemblyKey aircraftAssembly = createAcftAssyWithAcftPart( aircraftPart );
      final ConfigSlotKey aircraftRootConfigSlot =
            Domain.readRootConfigurationSlot( aircraftAssembly );

      // Given an inventory applicable to both definitions and allowing synchronization.
      final InventoryKey aircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aircraft ) {
            aircraft.setAssembly( aircraftAssembly );
            aircraft.setPart( aircraftPart );
            aircraft.allowSynchronization();
         }
      } );

      // Given an activated recurring requirement definition using a calendar scheduling rule and
      // scheduled from date
      final TaskTaskKey requirementDefinition =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition reqDefn ) {
                  reqDefn.againstConfigurationSlot( aircraftRootConfigSlot );
                  reqDefn.setRecurring( true );
                  reqDefn.setStatus( ACTV );

                  reqDefn.setScheduledFromEffectiveDate( NOW );
                  reqDefn.setMinimumForecastRange( MIN_FORECAST_RANGE );
                  reqDefn.addRecurringSchedulingRule( CDY, REPEAT_INTERVAL );
               }
            } );

      // Given a superseded recurring block chain definition using the same calendar scheduling rule
      // and scheduled from date
      final Map<Integer, TaskTaskKey> blockChainDefinitionSuperseded =
            Domain.createBlockChainDefinition( new DomainConfiguration<BlockChainDefinition>() {

               @Override
               public void configure( BlockChainDefinition blockChainDefn ) {
                  blockChainDefn.setName( BLOCK_CHAIN_A_NAME );
                  blockChainDefn.setRecurring( true );
                  blockChainDefn.setConfigurationSlot( aircraftRootConfigSlot );
                  blockChainDefn.setStatus( SUPRSEDE );
                  blockChainDefn.setRevisionNumber( REVISION_1 );

                  blockChainDefn.addBlock( BLOCK_ORDER_1, BLOCK_A1_CODE, BLOCK_A1_NAME );
                  blockChainDefn.addBlock( BLOCK_ORDER_2, BLOCK_A2_CODE, BLOCK_A2_NAME );
                  blockChainDefn.addBlock( BLOCK_ORDER_3, BLOCK_A3_CODE, BLOCK_A3_NAME );

                  // Same scheduling as the requirement.
                  blockChainDefn.setScheduledFromEffectiveDate( NOW );
                  blockChainDefn.setMinimumForecastRange( MIN_FORECAST_RANGE );
                  blockChainDefn.addRecurringSchedulingRule( CDY, REPEAT_INTERVAL );
               }
            } );

      // Given an activated revision of the block chain definition to which the requirement
      // definition is assigned
      Domain.createBlockChainDefinition( new DomainConfiguration<BlockChainDefinition>() {

         @Override
         public void configure( BlockChainDefinition blockChainDefn ) {
            blockChainDefn.setName( BLOCK_CHAIN_A_NAME );
            blockChainDefn.setRecurring( true );
            blockChainDefn.setConfigurationSlot( aircraftRootConfigSlot );
            blockChainDefn.setStatus( ACTV );
            blockChainDefn.setRevisionNumber( REVISION_2 );

            blockChainDefn.addBlock( BLOCK_ORDER_1, BLOCK_A1_CODE, BLOCK_A1_NAME,
                  blockChainDefinitionSuperseded.get( BLOCK_ORDER_1 ) );
            blockChainDefn.addBlock( BLOCK_ORDER_2, BLOCK_A2_CODE, BLOCK_A2_NAME,
                  blockChainDefinitionSuperseded.get( BLOCK_ORDER_2 ) );
            blockChainDefn.addBlock( BLOCK_ORDER_3, BLOCK_A3_CODE, BLOCK_A3_NAME,
                  blockChainDefinitionSuperseded.get( BLOCK_ORDER_3 ) );

            blockChainDefn.addRequirement( requirementDefinition, 1, 1 );

            blockChainDefn.setScheduledFromEffectiveDate( NOW );
            blockChainDefn.setMinimumForecastRange( MIN_FORECAST_RANGE );
            blockChainDefn.addRecurringSchedulingRule( CDY, REPEAT_INTERVAL );
         }
      } );

      // Given unassigned requirement tasks based on the requirement definition initialized for the
      // inventory
      final TaskKey requirementTaskActv =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement req ) {
                  req.setDefinition( requirementDefinition );
                  req.setStatus( RefEventStatusKey.ACTV );
                  req.setInventory( aircraft );

                  req.addDeadline( new DomainConfiguration<Deadline>() {

                     @Override
                     public void configure( Deadline deadline ) {
                        deadline.setUsageType( CDY );

                        // See note in method description regarding ceiling/end-of-day.
                        deadline.setStartDate( ceilDay( NOW ) );
                        deadline.setDueDate( ceilDay( addDays( NOW, 10 ) ) );
                     }

                  } );

               }

            } );

      final TaskKey requirementTaskForecast1 =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement req ) {
                  req.setDefinition( requirementDefinition );
                  req.setStatus( RefEventStatusKey.FORECAST );
                  req.setInventory( aircraft );
                  req.setPreviousTask( requirementTaskActv );

                  req.addDeadline( new DomainConfiguration<Deadline>() {

                     @Override
                     public void configure( Deadline deadline ) {
                        deadline.setUsageType( CDY );

                        // See note in method description regarding ceiling/end-of-day.
                        deadline.setStartDate( ceilDay( addDays( NOW, 10 ) ) );
                        deadline.setDueDate( ceilDay( addDays( NOW, 20 ) ) );
                     }

                  } );

               }

            } );

      final TaskKey requirementTaskForecast2 =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement req ) {
                  req.setDefinition( requirementDefinition );
                  req.setStatus( RefEventStatusKey.FORECAST );
                  req.setInventory( aircraft );
                  req.setPreviousTask( requirementTaskForecast1 );

                  req.addDeadline( new DomainConfiguration<Deadline>() {

                     @Override
                     public void configure( Deadline deadline ) {
                        deadline.setUsageType( CDY );

                        // See note in method description regarding ceiling/end-of-day.
                        deadline.setStartDate( ceilDay( addDays( NOW, 20 ) ) );
                        deadline.setDueDate( ceilDay( addDays( NOW, 30 ) ) );
                     }

                  } );

               }

            } );

      final TaskKey requirementTaskForecast3 =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement req ) {
                  req.setDefinition( requirementDefinition );
                  req.setStatus( RefEventStatusKey.FORECAST );
                  req.setInventory( aircraft );
                  req.setPreviousTask( requirementTaskForecast2 );

                  req.addDeadline( new DomainConfiguration<Deadline>() {

                     @Override
                     public void configure( Deadline deadline ) {
                        deadline.setUsageType( CDY );

                        // See note in method description regarding ceiling/end-of-day.
                        deadline.setStartDate( ceilDay( addDays( NOW, 30 ) ) );
                        deadline.setDueDate( ceilDay( addDays( NOW, 40 ) ) );
                     }

                  } );

               }

            } );

      final TaskKey requirementTaskForecast4 =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement req ) {
                  req.setDefinition( requirementDefinition );
                  req.setStatus( RefEventStatusKey.FORECAST );
                  req.setInventory( aircraft );
                  req.setPreviousTask( requirementTaskForecast3 );

                  req.addDeadline( new DomainConfiguration<Deadline>() {

                     @Override
                     public void configure( Deadline deadline ) {
                        deadline.setUsageType( CDY );

                        // See note in method description regarding ceiling/end-of-day.
                        deadline.setStartDate( ceilDay( addDays( NOW, 40 ) ) );
                        deadline.setDueDate( ceilDay( addDays( NOW, 50 ) ) );
                     }

                  } );

               }

            } );

      // Given block chain tasks based on the superseded block chain definition initialized for the
      // inventory
      final TaskKey blockTaskActv = Domain.createBlock( new DomainConfiguration<Block>() {

         @Override
         public void configure( Block block ) {
            block.setDefinition( blockChainDefinitionSuperseded.get( 1 ) );
            block.setStatus( RefEventStatusKey.ACTV );
            block.setInventory( aircraft );

            block.addDeadline( new DomainConfiguration<Deadline>() {

               @Override
               public void configure( Deadline deadline ) {
                  deadline.setUsageType( CDY );

                  // See note in method description regarding ceiling/end-of-day.
                  deadline.setStartDate( ceilDay( NOW ) );
                  deadline.setDueDate( ceilDay( addDays( NOW, 10 ) ) );
               }

            } );
         }

      } );

      final TaskKey blockTaskForecast1 = Domain.createBlock( new DomainConfiguration<Block>() {

         @Override
         public void configure( Block block ) {
            block.setDefinition( blockChainDefinitionSuperseded.get( 2 ) );
            block.setStatus( RefEventStatusKey.FORECAST );
            block.setInventory( aircraft );
            block.setPreviousBlock( blockTaskActv );

            block.addDeadline( new DomainConfiguration<Deadline>() {

               @Override
               public void configure( Deadline deadline ) {
                  deadline.setUsageType( CDY );

                  // See note in method description regarding ceiling/end-of-day.
                  deadline.setStartDate( ceilDay( addDays( NOW, 10 ) ) );
                  deadline.setDueDate( ceilDay( addDays( NOW, 20 ) ) );
               }

            } );
         }

      } );

      final TaskKey blockTaskForecast2 = Domain.createBlock( new DomainConfiguration<Block>() {

         @Override
         public void configure( Block block ) {
            block.setDefinition( blockChainDefinitionSuperseded.get( 3 ) );
            block.setStatus( RefEventStatusKey.FORECAST );
            block.setInventory( aircraft );
            block.setPreviousBlock( blockTaskForecast1 );

            block.addDeadline( new DomainConfiguration<Deadline>() {

               @Override
               public void configure( Deadline deadline ) {
                  deadline.setUsageType( CDY );

                  // See note in method description regarding ceiling/end-of-day.
                  deadline.setStartDate( ceilDay( addDays( NOW, 20 ) ) );
                  deadline.setDueDate( ceilDay( addDays( NOW, 30 ) ) );
               }

            } );
         }

      } );

      final TaskKey blockTaskForecast3 = Domain.createBlock( new DomainConfiguration<Block>() {

         @Override
         public void configure( Block block ) {
            block.setDefinition( blockChainDefinitionSuperseded.get( 1 ) );
            block.setStatus( RefEventStatusKey.FORECAST );
            block.setInventory( aircraft );
            block.setPreviousBlock( blockTaskForecast2 );

            block.addDeadline( new DomainConfiguration<Deadline>() {

               @Override
               public void configure( Deadline deadline ) {
                  deadline.setUsageType( CDY );

                  // See note in method description regarding ceiling/end-of-day.
                  deadline.setStartDate( ceilDay( addDays( NOW, 30 ) ) );
                  deadline.setDueDate( ceilDay( addDays( NOW, 40 ) ) );
               }

            } );
         }

      } );

      final TaskKey blockTaskForecast4 = Domain.createBlock( new DomainConfiguration<Block>() {

         @Override
         public void configure( Block block ) {
            block.setDefinition( blockChainDefinitionSuperseded.get( 2 ) );
            block.setStatus( RefEventStatusKey.FORECAST );
            block.setInventory( aircraft );
            block.setPreviousBlock( blockTaskForecast3 );

            block.addDeadline( new DomainConfiguration<Deadline>() {

               @Override
               public void configure( Deadline deadline ) {
                  deadline.setUsageType( CDY );

                  // See note in method description regarding ceiling/end-of-day.
                  deadline.setStartDate( ceilDay( addDays( NOW, 40 ) ) );
                  deadline.setDueDate( ceilDay( addDays( NOW, 50 ) ) );
               }

            } );
         }

      } );

      // Given a request exists to synchronize the inventory
      requestSync( aircraft );

      // When the block for the inventory is synchronized
      inventorySync();

      // Then the requirement tasks are assigned to the block chain tasks with the same deadline
      Assert.assertEquals(
            "Unexpected Result: the active requirement task is not assigned to the correct block task.",
            blockTaskActv, readHighestTask( requirementTaskActv ) );

      Assert.assertEquals(
            "Unexpected Result: the forecast requirement task 1 is not assigned to the correct block task.",
            blockTaskForecast1, readHighestTask( requirementTaskForecast1 ) );

      Assert.assertEquals(
            "Unexpected Result: the forecast requirement task 2 is not assigned to the correct block task.",
            blockTaskForecast2, readHighestTask( requirementTaskForecast2 ) );

      Assert.assertEquals(
            "Unexpected Result: the forecast requirement task 3 is not assigned to the correct block task.",
            blockTaskForecast3, readHighestTask( requirementTaskForecast3 ) );

      Assert.assertEquals(
            "Unexpected Result: the forecast requirement task 4 is not assigned to the correct block task.",
            blockTaskForecast4, readHighestTask( requirementTaskForecast4 ) );
   }


   /**
    * Verify when a recurring requirement definition is assigned to many block chain definitions
    * that the requirement task is assigned to the block chain tasks whose block chain name is first
    * when the block chain names are sorted alphanumerically.
    *
    * <pre>
    * Given an activated, recurring requirement definition with a scheduling rule and scheduled from date
    *   And two activated, recurring block chain definitions
    *   And all the block chain definitions are assigned the requirement definition
    *   And an inventory applicable to all definitions and allowing synchronization
    *   And a request exists to synchronize the inventory
    *  When the inventory has its task definitions synchronized
    *  Then block chain tasks are initialized for both block chain definitions
    *   And requirement tasks are initialized and assigned to the block chain tasks of the block chain definition whose name is first when the block chain names are sorted alphanumerically
    * </pre>
    */
   @Test
   public void itZipsReqIntoBlockChainWhoseNameIsFirstWhenManySortedAlphabetically()
         throws Exception {
      // Set up an aircraft assembly to be used to create the block chain and requirement
      // definitions against, as well as to create an aircraft.
      final PartNoKey aircraftPart = Domain.createPart();
      final AssemblyKey aircraftAssembly = createAcftAssyWithAcftPart( aircraftPart );
      final ConfigSlotKey aircraftRootConfigSlot =
            Domain.readRootConfigurationSlot( aircraftAssembly );

      // Given an inventory applicable to both definitions and allowing synchronization.
      final InventoryKey aircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aircraft ) {
            aircraft.setAssembly( aircraftAssembly );
            aircraft.setPart( aircraftPart );
            aircraft.allowSynchronization();
         }
      } );

      // Given an activated recurring requirement definition using a calendar scheduling rule and
      // scheduled from date
      final TaskTaskKey requirementDefinition =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition reqDefn ) {
                  reqDefn.againstConfigurationSlot( aircraftRootConfigSlot );
                  reqDefn.setRecurring( true );
                  reqDefn.setStatus( ACTV );

                  reqDefn.setScheduledFromEffectiveDate( NOW );
                  reqDefn.setMinimumForecastRange( MIN_FORECAST_RANGE );
                  reqDefn.addRecurringSchedulingRule( CDY, REPEAT_INTERVAL );
               }
            } );

      // Given two activated, recurring block chain definitions
      Domain.createBlockChainDefinition( new DomainConfiguration<BlockChainDefinition>() {

         @Override
         public void configure( BlockChainDefinition blockChainDefn ) {
            blockChainDefn.setName( BLOCK_CHAIN_A_NAME );
            blockChainDefn.setRecurring( true );
            blockChainDefn.setConfigurationSlot( aircraftRootConfigSlot );
            blockChainDefn.setStatus( ACTV );

            blockChainDefn.addBlock( BLOCK_ORDER_1, BLOCK_A1_CODE, BLOCK_A1_NAME );
            blockChainDefn.addBlock( BLOCK_ORDER_2, BLOCK_A2_CODE, BLOCK_A2_NAME );
            blockChainDefn.addBlock( BLOCK_ORDER_3, BLOCK_A3_CODE, BLOCK_A3_NAME );

            blockChainDefn.addRequirement( requirementDefinition, 1, 1 );

            // Same scheduling as the requirement.
            blockChainDefn.setScheduledFromEffectiveDate( NOW );
            blockChainDefn.setMinimumForecastRange( MIN_FORECAST_RANGE );
            blockChainDefn.addRecurringSchedulingRule( CDY, REPEAT_INTERVAL );
         }
      } );

      Domain.createBlockChainDefinition( new DomainConfiguration<BlockChainDefinition>() {

         @Override
         public void configure( BlockChainDefinition blockChainDefn ) {
            blockChainDefn.setName( BLOCK_CHAIN_B_NAME );
            blockChainDefn.setRecurring( true );
            blockChainDefn.setConfigurationSlot( aircraftRootConfigSlot );
            blockChainDefn.setStatus( ACTV );

            blockChainDefn.addBlock( BLOCK_ORDER_1, BLOCK_B1_CODE, BLOCK_B1_NAME );
            blockChainDefn.addBlock( BLOCK_ORDER_2, BLOCK_B2_CODE, BLOCK_B2_NAME );
            blockChainDefn.addBlock( BLOCK_ORDER_3, BLOCK_B3_CODE, BLOCK_B3_NAME );

            blockChainDefn.addRequirement( requirementDefinition, 1, 1 );

            // Same scheduling as the requirement.
            blockChainDefn.setScheduledFromEffectiveDate( NOW );
            blockChainDefn.setMinimumForecastRange( MIN_FORECAST_RANGE );
            blockChainDefn.addRecurringSchedulingRule( CDY, REPEAT_INTERVAL );
         }
      } );

      // Given a request exists to synchronize the inventory
      requestSync( aircraft );

      // When the block for the inventory is synchronized
      inventorySync();

      // Then block chain tasks are initialized for both block chain definitions using the
      // scheduling rule
      List<TaskKey> blockTasks = getActualTasks( aircraft, BLOCK );
      Assert.assertEquals( "Incorrect number of block tasks initialized", 10, blockTasks.size() );

      // And requirement tasks are initialized
      List<TaskKey> reqTasks = getActualTasks( aircraft, REQ );
      Assert.assertEquals( "Incorrect number of requirement tasks initialized", 5,
            reqTasks.size() );

      // And the requirement tasks are assigned to the block chain tasks of the block chain
      // definition whose name is 'BLOCK_CHAIN_A'
      for ( TaskKey req : reqTasks ) {
         TaskKey assignedBlockTaskKey = readHighestTask( req );
         String actualBlockChainName = getBlockChainName( assignedBlockTaskKey );

         Assert.assertEquals( "Requirement task is not assign to the first block chain.",
               BLOCK_CHAIN_A_NAME, actualBlockChainName );
      }
   }


   /**
    * Verify that recurring requirement tasks are assigned to recurring block chain tasks whose
    * deadline is prior to or equal to the their deadline, given the following circumstances;
    *
    * <pre>
    * - the config parameter BSYNC_IGNORE_ZIPPING_DATE_MISALIGNMENT is set to FALSE
    * - the requirement task's definition is assigned to the block chain task's definition
    * <pre>
    *
    * Note: only one requirement task may be assigned to a block chain task so all other requirement
    * tasks remain unassigned.
    *
    * <pre>
    *    Example using recurring block chain every 3 days, recurring requirement every 2 days.
    *
    *                 Day: 01 02 03 04 05 06 07 08 09 10 11 12
    *    Block Chain task:       B1       B2       B3       B4
    *          assignment:         \      |          \      |
    *    Requirement task:    R1    R2    R3    R4    R5    R6
    * </pre>
    *
    * <pre>
    *    Given an activated recurring requirement definition using a calendar scheduling rule and scheduled from date
    *      And an activated recurring block chain definition using the same scheduled from date
    *          but a calendar scheduling rule with a larger frequency
    *      And an inventory applicable to both definitions and allowing synchronization
    *      And the configuration parameter BSYNC_IGNORE_ZIPPING_DATE_MISALIGNMENT is set to FALSE
    *      And a request exists to synchronize the inventory
    *     When the inventory has its task definitions synchronized
    *     Then block chain tasks are initialized for the inventory using its scheduling rule and scheduled from date
    *      And requirement tasks are initialized for the inventory using its scheduling rule and scheduled from date
    *      And block chain tasks are assigned one requirement task
    *      And the assigned requirement task has either the same deadline
    *          or the earliest deadline after the block chain task's deadline
    *      And all other requirement tasks are not assigned
    * </pre>
    */
   @Test
   public void itDoesNotZipRecurringReqTasksToRecurringBlockChainTasksWhenBlockAlreadyContainsReq()
         throws Exception {

      // Set up an aircraft assembly to be used to create the block chain and requirement
      // definitions against, as well as to create an aircraft.
      //
      // Note: we need to create a part due to a validation performed during task initialization,
      // which ensures the inventory part matches the the part number of the task definition's
      // assembly.
      final PartNoKey acftPart = Domain.createPart();
      final AssemblyKey acftAssy = createAcftAssyWithAcftPart( acftPart );
      final ConfigSlotKey acftRootConfigSlot = Domain.readRootConfigurationSlot( acftAssy );

      // Given an activated recurring requirement definition using a calendar scheduling rule and
      // scheduled from date. I.e. every 2 days.
      final TaskTaskKey requirementDefinition =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition reqDefn ) {
                  reqDefn.againstConfigurationSlot( acftRootConfigSlot );
                  reqDefn.setRecurring( true );
                  reqDefn.setStatus( ACTV );

                  reqDefn.setScheduledFromEffectiveDate( NOW );
                  reqDefn.setMinimumForecastRange( 10 );
                  reqDefn.addSchedulingRule( CDY, 2 );
               }
            } );

      // Given an activated recurring block chain definition using the same scheduled from date but
      // a calendar scheduling rule with a larger frequency. I.e. every 3 days.
      Domain.createBlockChainDefinition( new DomainConfiguration<BlockChainDefinition>() {

         @Override
         public void configure( BlockChainDefinition blockChainDefn ) {
            blockChainDefn.setName( BLOCK_CHAIN_A_NAME );
            blockChainDefn.setRecurring( true );
            blockChainDefn.setConfigurationSlot( acftRootConfigSlot );
            blockChainDefn.setStatus( ACTV );

            blockChainDefn.addBlock( BLOCK_ORDER_1, BLOCK_A1_CODE, BLOCK_A1_NAME );
            blockChainDefn.addBlock( BLOCK_ORDER_2, BLOCK_A2_CODE, BLOCK_A2_NAME );
            blockChainDefn.addBlock( BLOCK_ORDER_3, BLOCK_A3_CODE, BLOCK_A3_NAME );

            blockChainDefn.addRequirement( requirementDefinition, 1, 1 );

            // Same scheduling as the requirement.
            blockChainDefn.setScheduledFromEffectiveDate( NOW );
            blockChainDefn.setMinimumForecastRange( 10 );
            blockChainDefn.addRecurringSchedulingRule( CDY, 3 );
         }
      } );

      // Given an inventory applicable to both definitions and allowing synchronization.
      final InventoryKey aircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aircraft ) {
            aircraft.setAssembly( acftAssy );
            aircraft.setPart( acftPart );
            aircraft.allowSynchronization();
         }
      } );

      // Given the configuration parameter BSYNC_IGNORE_ZIPPING_DATE_MISALIGNMENT is set to FALSE.
      globalParams.setBoolean( "BSYNC_IGNORE_ZIPPING_DATE_MISALIGNMENT", false );

      // Given a request exists to synchronize the inventory
      requestSync( aircraft );

      // When the inventory has its task definitions synchronized.
      inventorySync();

      // Then block chain tasks are initialized for the inventory using its scheduling rule and
      // scheduled from date.
      List<TaskKey> blockTasks = getActualTasks( aircraft, BLOCK );
      Assert.assertEquals( "Incorrect number of block tasks initialized", 5, blockTasks.size() );

      // Then requirement tasks are initialized for the inventory using its scheduling rule and
      // scheduled from date.
      List<TaskKey> reqTasks = getActualTasks( aircraft, REQ );
      Assert.assertEquals( "Incorrect number of requirement tasks initialized", 7,
            reqTasks.size() );

      // Then block chain tasks are assigned one requirement task and the assigned requirement
      // task
      // has either the same deadline or the earliest deadline after the block chain task's
      // deadline.
      //
      // With the block chain task's scheduled every 2 days and the req task's scheduled every 3
      // days the tasks are expected to be aligned as follows.
      blockTasks = getTasksOrderedByDeadline( blockTasks, CDY );
      reqTasks = getTasksOrderedByDeadline( reqTasks, CDY );

      assertEquals( "Unexpected block task for second req task.", blockTasks.get( 0 ),
            readHighestTask( reqTasks.get( 1 ) ) );
      assertEquals( "Unexpected block task for third req task.", blockTasks.get( 1 ),
            readHighestTask( reqTasks.get( 2 ) ) );
      assertEquals( "Unexpected block task for fifth req task.", blockTasks.get( 2 ),
            readHighestTask( reqTasks.get( 4 ) ) );
      assertEquals( "Unexpected block task for sixth req task.", blockTasks.get( 3 ),
            readHighestTask( reqTasks.get( 5 ) ) );

      // Then all other requirement tasks are not assigned.
      // (i.e. their highest task is themselves)
      assertEquals( "Unexpected block task for first req task.", reqTasks.get( 0 ),
            readHighestTask( reqTasks.get( 0 ) ) );
      assertEquals( "Unexpected block task for fourth req task.", reqTasks.get( 3 ),
            readHighestTask( reqTasks.get( 3 ) ) );
      assertEquals( "Unexpected block task for seventh req task.", reqTasks.get( 6 ),
            readHighestTask( reqTasks.get( 6 ) ) );
   }


   /**
    * Verify that each recurring requirement task is assigned to one recurring block chain task
    * using their respective deadline ordering (even if the block task deadline is after the
    * requirement task deadline), given the following circumstances;
    *
    * <pre>
    *  - the config parameter BSYNC_IGNORE_ZIPPING_DATE_MISALIGNMENT is set to TRUE
    * - the requirement task's definition is assigned to the block chain task's definition.
    *
    * <pre>
    *
    * Note: recurring requirement tasks may be assigned to block chain tasks who deadline is after
    * the requirement task's deadline, thus all requirement deadlines are assigned.
    *
    * <pre>
    *    Example using recurring block chain every 3 days, recurring requirement every 2 days.
    *
    *                 Day: 01 02 03 04 05 06 07 08 09
    *    Block Chain task:       B1       B2       B3
    *          assignment:      /     ___/  ______/
    *                          /     /     /
    *    Requirement task:    R1    R2    R3
    * </pre>
    *
    * <pre>
    *    Given an activated recurring requirement definition using a calendar scheduling rule and scheduled from date
    *      And an activated recurring block chain definition using the same scheduled from date
    *          but a calendar scheduling rule with a larger frequency
    *      And an inventory applicable to both definitions and allowing synchronization
    *      And the configuration parameter BSYNC_IGNORE_ZIPPING_DATE_MISALIGNMENT is set to TRUE
    *      And a request exists to synchronize the inventory
    *     When the inventory has its task definitions synchronized
    *     Then block chain tasks are initialized for the inventory using its scheduling rule and scheduled from date
    *      And requirement tasks are initialized for the inventory using its scheduling rule and scheduled from date
    *      And block chain tasks are assigned one requirement task in the order of their respective deadlines
    * </pre>
    */
   @Test
   public void
         itZipsRecurringReqTasksToRecurringBlockChainTasksWithDiffSchedWhenIgnoringMisalignments()
               throws Exception {

      // Set up an aircraft assembly to be used to create the block chain and requirement
      // definitions against, as well as to create an aircraft.
      //
      // Note: we need to create a part due to a validation performed during task initialization,
      // which ensures the inventory part matches the the part number of the task definition's
      // assembly.
      final PartNoKey acftPart = Domain.createPart();
      final AssemblyKey acftAssy = createAcftAssyWithAcftPart( acftPart );
      final ConfigSlotKey acftRootConfigSlot = Domain.readRootConfigurationSlot( acftAssy );

      // Given an activated recurring requirement definition using a calendar scheduling rule and
      // scheduled from date. I.e. every 2 days.
      final TaskTaskKey requirementDefinition =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition reqDefn ) {
                  reqDefn.againstConfigurationSlot( acftRootConfigSlot );
                  reqDefn.setRecurring( true );
                  reqDefn.setStatus( ACTV );

                  reqDefn.setScheduledFromEffectiveDate( NOW );
                  reqDefn.setMinimumForecastRange( 10 );
                  reqDefn.addSchedulingRule( CDY, 2 );
               }
            } );

      // Given an activated recurring block chain definition using the same scheduled from date but
      // a calendar scheduling rule with a larger frequency. I.e. every 3 days.
      Domain.createBlockChainDefinition( new DomainConfiguration<BlockChainDefinition>() {

         @Override
         public void configure( BlockChainDefinition blockChainDefn ) {
            blockChainDefn.setName( BLOCK_CHAIN_A_NAME );
            blockChainDefn.setRecurring( true );
            blockChainDefn.setConfigurationSlot( acftRootConfigSlot );
            blockChainDefn.setStatus( ACTV );

            blockChainDefn.addBlock( BLOCK_ORDER_1, BLOCK_A1_CODE, BLOCK_A1_NAME );
            blockChainDefn.addBlock( BLOCK_ORDER_2, BLOCK_A2_CODE, BLOCK_A2_NAME );
            blockChainDefn.addBlock( BLOCK_ORDER_3, BLOCK_A3_CODE, BLOCK_A3_NAME );

            blockChainDefn.addRequirement( requirementDefinition, 1, 1 );

            // Same scheduling as the requirement.
            blockChainDefn.setScheduledFromEffectiveDate( NOW );
            blockChainDefn.setMinimumForecastRange( 10 );
            blockChainDefn.addRecurringSchedulingRule( CDY, 3 );
         }
      } );

      // Given an inventory applicable to both definitions and allowing synchronization.
      final InventoryKey aircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aircraft ) {
            aircraft.setAssembly( acftAssy );
            aircraft.setPart( acftPart );
            aircraft.allowSynchronization();
         }
      } );

      // Given the configuration parameter BSYNC_IGNORE_ZIPPING_DATE_MISALIGNMENT is set to TRUE.
      globalParams.setBoolean( "BSYNC_IGNORE_ZIPPING_DATE_MISALIGNMENT", true );

      // Given a request exists to synchronize the inventory
      requestSync( aircraft );

      // When the inventory has its task definitions synchronized.
      inventorySync();

      // Then block chain tasks are initialized for the inventory using its scheduling rule and
      // scheduled from date.
      List<TaskKey> blockTasks = getActualTasks( aircraft, BLOCK );
      Assert.assertEquals( "Incorrect number of block tasks initialized", 5, blockTasks.size() );

      // Then requirement tasks are initialized for the inventory using its scheduling rule and
      // scheduled from date.
      List<TaskKey> reqTasks = getActualTasks( aircraft, REQ );
      Assert.assertEquals( "Incorrect number of requirement tasks initialized", 7,
            reqTasks.size() );

      // Then block chain tasks are assigned one requirement task in the order of their
      // respective
      // deadlines.
      //
      // I.e. the first 5 requirement tasks will be assigned to the first 5 block chain tasks and
      // the remaining requirement tasks will be unassigned.
      blockTasks = getTasksOrderedByDeadline( blockTasks, CDY );
      reqTasks = getTasksOrderedByDeadline( reqTasks, CDY );

      assertEquals( "Unexpected block task for second req task.", blockTasks.get( 0 ),
            readHighestTask( reqTasks.get( 0 ) ) );
      assertEquals( "Unexpected block task for third req task.", blockTasks.get( 1 ),
            readHighestTask( reqTasks.get( 1 ) ) );
      assertEquals( "Unexpected block task for fifth req task.", blockTasks.get( 2 ),
            readHighestTask( reqTasks.get( 2 ) ) );
      assertEquals( "Unexpected block task for sixth req task.", blockTasks.get( 3 ),
            readHighestTask( reqTasks.get( 3 ) ) );
      assertEquals( "Unexpected block task for sixth req task.", blockTasks.get( 4 ),
            readHighestTask( reqTasks.get( 4 ) ) );

      // Then all other requirement tasks are not assigned.
      // (i.e. their highest task is themselves)
      assertEquals( "Unexpected block task for first req task.", reqTasks.get( 5 ),
            readHighestTask( reqTasks.get( 5 ) ) );
      assertEquals( "Unexpected block task for fourth req task.", reqTasks.get( 6 ),
            readHighestTask( reqTasks.get( 6 ) ) );
   }


   @Before
   public void before() {
      // Create a Human Resource to test with and ensure it has the appropriate user parameters set
      // up to perform zipping. Due to the confusion over Human Resources and Users within
      // Maintenix, we will not use a Domain entity to represent this HR. Instead we will use the
      // HumanResourseBuilder directly.
      authHr = Domain.createHumanResource();
      userId = OrgHr.findByPrimaryKey( authHr ).getUserId();
      UserParameters.setInstance( userId, "LOGIC", new UserParametersFake( userId, "LOGIC" ) );

      // Ensure we are using the authorized HR when generating the work items.
      WorkItemGenerator.setInstance( new WorkItemGeneratorExecuteImmediateFake( authHr ) );

      globalParams = GlobalParameters.getInstance( "LOGIC" );
      origIgnoreMisalignment = globalParams.getBool( "BSYNC_IGNORE_ZIPPING_DATE_MISALIGNMENT" );

      eventBus = injectionOverrideRule.select( RecordingEventBus.class ).get();
   }


   @After
   public void after() {
      UserParameters.setInstance( userId, "LOGIC", null );
      WorkItemGenerator.setInstance( null );

      globalParams.setBoolean( "BSYNC_IGNORE_ZIPPING_DATE_MISALIGNMENT", origIgnoreMisalignment );

   }


   private Integer readTaskRevision( TaskKey task ) {
      final SchedStaskDao schedStaskDao =
            InjectorContainer.get().getInstance( SchedStaskDao.class );
      final SchedStaskTable schedStaskTable = schedStaskDao.findByPrimaryKey( task );
      final TaskTaskKey currentTaskDefinition = schedStaskTable.getTaskTaskKey();
      final TaskTaskTable taskTask = TaskTaskTable.findByPrimaryKey( currentTaskDefinition );
      return taskTask.getRevisionOrd();

   }


   private TaskKey readHighestTask( TaskKey task ) {
      final SchedStaskDao schedStaskDao =
            InjectorContainer.get().getInstance( SchedStaskDao.class );
      final SchedStaskTable schedStaskTable = schedStaskDao.findByPrimaryKey( task );
      return schedStaskTable.getHTaskKey();

   }


   /**
    * Get a list of actual tasks of a specific task type(REQ, BLOCL, JIC etc.) on an inventory.
    *
    */
   private List<TaskKey> getActualTasks( InventoryKey inv, RefTaskClassKey taskClass ) {
      List<TaskKey> tasks = new ArrayList<TaskKey>();

      DataSetArgument schedStaskArgs = new DataSetArgument();
      schedStaskArgs.add( inv, "main_inv_no_db_id", "main_inv_no_id" );
      schedStaskArgs.add( taskClass, "task_class_db_id", "task_class_cd" );
      QuerySet tasksQs =
            QuerySetFactory.getInstance().executeQueryTable( "sched_stask", schedStaskArgs );

      while ( tasksQs.next() ) {
         tasks.add( tasksQs.getKey( TaskKey.class, "sched_db_id", "sched_id" ) );
      }

      return tasks;
   }


   /**
    * Add a row in the inv_sync_queue table for the provided inventory.
    *
    * @param inv
    */
   private void requestSync( InventoryKey inv ) {
      DataSetArgument args = new DataSetArgument();
      args.add( inv, "inv_no_db_id", "inv_no_id" );
      MxDataAccess.getInstance().executeInsert( "inv_sync_queue", args );
   }


   /**
    * The inventory is synchronized
    */
   private void inventorySync() throws Exception {

      // Synchronize the inventory.
      InventorySynchronizer invSynchronizer = new InventorySynchronizer();
      invSynchronizer.processInventory( invSynchronizer.getInventory() );

      // Perform zipping of tasks.
      QuerySet qs = QuerySetFactory.getInstance().executeQueryTable( "zip_queue", null );
      while ( qs.next() ) {
         if ( qs.getBoolean( "actv_bool" ) ) {
            int zipId = qs.getInt( "zip_id" );
            BaselineSyncHelper.getInstance().performZipping( zipId, authHr );
         }
      }

   }


   /**
    * Get a map of the actual tasks and their due date.
    *
    */
   private Map<TaskKey, Date> getActualTasksDueDateMap( List<TaskKey> tasks,
         DataTypeKey dataTypeKey ) {
      Map<TaskKey, Date> tasksDueDateMap = new HashMap<TaskKey, Date>();

      for ( TaskKey taskKey : tasks ) {

         EvtSchedDeadTable evtSchedDeadTable =
               EvtSchedDeadTable.findByPrimaryKey( taskKey, dataTypeKey );

         tasksDueDateMap.put( taskKey, evtSchedDeadTable.getDeadlineDate() );
      }

      return tasksDueDateMap;
   }


   private TaskTaskKey getTaskDefinition( TaskKey task ) {
      SchedStaskDao schedStaskDao = InjectorContainer.get().getInstance( SchedStaskDao.class );
      return schedStaskDao.findByPrimaryKey( task ).getTaskTaskKey();
   }


   private Object getTaskDefinitionStatus( TaskKey task ) {
      EvtEventDao evtEventDao = InjectorContainer.get().getInstance( EvtEventDao.class );
      return evtEventDao.findByPrimaryKey( task ).getEventStatus();
   }


   private String getBlockChainName( TaskKey blockTask ) {
      TaskTaskKey assignedBlockDefnKey = getTaskDefinition( blockTask );
      TaskTaskTable blockDefinitionTable = TaskTaskTable.findByPrimaryKey( assignedBlockDefnKey );

      return blockDefinitionTable.getBlockChainSdesc();
   }


   private AssemblyKey createAcftAssyWithAcftPart( final PartNoKey aircraftPart ) {
      return Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

         @Override
         public void configure( AircraftAssembly acftAssy ) {
            acftAssy.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

               @Override
               public void configure( ConfigurationSlot rootConfigSlot ) {
                  rootConfigSlot.addPartGroup( new DomainConfiguration<PartGroup>() {

                     @Override
                     public void configure( PartGroup rootCsPartGroup ) {
                        rootCsPartGroup.setInventoryClass( ACFT );
                        rootCsPartGroup.addPart( aircraftPart );
                     }
                  } );
               }
            } );
         }
      } );
   }


   private List<TaskKey> getTasksOrderedByDeadline( List<TaskKey> blockTasks,
         DataTypeKey dataType ) {

      SortedMap<Date, TaskKey> ordredTasks = new TreeMap<Date, TaskKey>();

      for ( TaskKey blockTask : blockTasks ) {
         Date deadline =
               EvtSchedDeadTable.findByPrimaryKey( blockTask, dataType ).getDeadlineDate();
         ordredTasks.put( deadline, blockTask );
      }

      return new ArrayList<TaskKey>( ordredTasks.values() );
   }

}
