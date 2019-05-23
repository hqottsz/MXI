package com.mxi.mx.core.ejb.stask;

import static com.mxi.mx.core.key.DataTypeKey.CDY;
import static com.mxi.mx.core.key.DataTypeKey.CYCLES;
import static com.mxi.mx.core.key.DataTypeKey.HOURS;
import static com.mxi.mx.core.key.RefEventStatusKey.ACTV;
import static com.mxi.mx.core.key.RefEventStatusKey.CANCEL;
import static com.mxi.mx.core.key.RefEventStatusKey.COMMIT;
import static com.mxi.mx.core.key.RefEventStatusKey.FORECAST;
import static com.mxi.mx.core.key.RefInvClassKey.ACFT;
import static com.mxi.mx.core.key.RefSchedFromKey.CUSTOM;
import static com.mxi.mx.core.key.RefTaskClassKey.ADHOC;
import static com.mxi.mx.core.key.RefTaskClassKey.FOLLOW;
import static com.mxi.mx.core.key.RefTaskClassKey.REQ;
import static java.math.BigDecimal.TEN;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
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
import com.mxi.am.domain.ForecastModel;
import com.mxi.am.domain.Part;
import com.mxi.am.domain.PartGroup;
import com.mxi.am.domain.RecurringSchedulingRule;
import com.mxi.am.domain.Requirement;
import com.mxi.am.domain.RequirementDefinition;
import com.mxi.am.domain.TrackedInventory;
import com.mxi.am.domain.WorkPackage;
import com.mxi.am.domain.builder.AttachmentService;
import com.mxi.am.domain.builder.EventRelationshipBuilder;
import com.mxi.am.domain.reader.ConfigurationSlotReader;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.GlobalParametersFake;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.config.UserParametersStub;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.security.SecurityUtils;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.services.work.WorkItemGeneratorExecuteImmediateFake;
import com.mxi.mx.common.servlet.SessionContextFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.configuration.axon.AxonObjectMapper;
import com.mxi.mx.core.configuration.axon.RecordingEventBus;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.EventDeadlineKey;
import com.mxi.mx.core.key.EventInventoryKey;
import com.mxi.mx.core.key.EventInventoryUsageKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.FcModelKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefRelationTypeKey;
import com.mxi.mx.core.key.RefStageReasonKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.RefTaskMustRemoveKey;
import com.mxi.mx.core.key.RefUsgSnapshotSrcTypeKey;
import com.mxi.mx.core.key.RefWorkTypeKey;
import com.mxi.mx.core.key.TaskJicReqMapKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.production.task.domain.TaskCancelledEvent;
import com.mxi.mx.core.production.task.domain.TaskDeadlineStartValue;
import com.mxi.mx.core.production.task.domain.TaskDeadlinesRemovedEvent;
import com.mxi.mx.core.production.task.domain.TaskDrivingDeadlineRescheduledEvent;
import com.mxi.mx.core.production.task.domain.TaskPlanByDateUpdatedEvent;
import com.mxi.mx.core.production.task.domain.TaskRescheduledEvent;
import com.mxi.mx.core.production.task.domain.TaskTerminatedEvent;
import com.mxi.mx.core.services.bsync.synchronization.logic.InventorySynchronizer;
import com.mxi.mx.core.services.event.inventory.UsageSnapshot;
import com.mxi.mx.core.services.stask.SchedStaskUtils;
import com.mxi.mx.core.services.stask.WarningNoDeadline;
import com.mxi.mx.core.services.stask.creation.DuplicateTaskException;
import com.mxi.mx.core.services.stask.deadline.CalendarDeadline;
import com.mxi.mx.core.services.stask.deadline.UsageDeadline;
import com.mxi.mx.core.table.eqp.EqpBomPart;
import com.mxi.mx.core.table.evt.EvtEventDao;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.evt.EvtInvUsageTable;
import com.mxi.mx.core.table.evt.EvtSchedDeadTable;
import com.mxi.mx.core.table.evt.JdbcEvtEventDao;
import com.mxi.mx.core.table.org.OrgHr;
import com.mxi.mx.core.table.sched.JdbcSchedStaskDao;
import com.mxi.mx.core.table.sched.SchedStaskDao;
import com.mxi.mx.core.table.sched.SchedStaskTable;
import com.mxi.mx.core.table.task.TaskJicReqMapTable;
import com.mxi.mx.core.table.task.TaskTaskTable;
import com.mxi.mx.core.unittest.table.stask.SchedStaskUtil;
import com.mxi.mx.persistence.uuid.SequentialUuidGenerator;


/**
 *
 * Verifies the behaviour of {@link TaskBean}
 *
 */
public class TaskBeanTest {

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule injectionOverrideRule = new InjectionOverrideRule();

   private static EvtEventDao evtEventDao = new JdbcEvtEventDao();
   private static SchedStaskDao schedStaskDao = new JdbcSchedStaskDao();
   private static AxonDomainEventDao axonDomainEventDao = new AxonDomainEventDao();
   private static AxonObjectMapper axonObjectMapper = new AxonObjectMapper();

   private static final String BLOCK_CHAIN_NAME = "BLOCK_CHAIN_NAME";
   private static final int BLOCK_1 = 1;
   private static final int BLOCK_2 = 2;
   private static final int BLOCK_3 = 3;
   private static final int REVISION_1 = 1;
   private static final int REVISION_2 = 2;
   private static final String BLOCK_1_CODE = "BLOCK1";
   private static final String BLOCK_1_NAME = BLOCK_1_CODE;
   private static final String BLOCK_2_CODE = "BLOCK2";
   private static final String BLOCK_2_NAME = BLOCK_2_CODE;
   private static final String BLOCK_3_CODE = "BLOCK3";
   private static final String BLOCK_3_NAME = BLOCK_3_CODE;

   private static final String REQ_DEFN_CODE_1 = "REQ_DEFN_CODE_1";
   private static final String REQ_DEFN_CODE_2 = "REQ_DEFN_CODE_2";
   private static final Date RECEIVED_DATE = DateUtils.addDays( new Date(), -15 );

   private static final String PARENT_TRK_CONFIG_SLOT = "PARENT_TRK_CONFIG_SLOT";
   private static final String CHILD_TRK_CONFIG_SLOT = "CHILD_TRK_CONFIG_SLOT";
   private static final String CHILD_TRK_PART_GROUP = "PG";
   private static final String ASSEMBLY_CODE = "A320";

   private static final Integer ORIGINAL_DEADLINE_INTERVAL = 10;
   private static final Integer NEW_DEADLINE_INTERVAL = 15;
   private static final Integer ORIGINAL_START_VALUE = 0;
   private static final Integer NEW_START_VALUE = 5;
   private static final Integer DUE_VALUE = 200;

   private static final Date ORIGINAL_DEADLINE_START_DATE =
         DateUtils.addDays( new Date(), -ORIGINAL_DEADLINE_INTERVAL );

   private static final Date NEW_DEADLINE_START_DATE =
         DateUtils.addDays( ORIGINAL_DEADLINE_START_DATE, 5 );

   private Date taskStartDate = new Date();
   private Date taskEndDate = DateUtils.addDays( taskStartDate, +1 );
   private BigDecimal eventHoursUsage = new BigDecimal( 400.0 );
   private BigDecimal eventCycleUsage = new BigDecimal( 350.0 );
   private static final int CYCLES_USAGE_INTERVAL = 0;
   private static final int CYCLES_USAGE_TSN_DEADLINE = 20;
   private static final int HOURS_USAGE_INTERVAL = 0;
   private static final int HOURS_USAGE_TSN_DEADLINE = 30;
   private static final int NOTIFY_QT = 0;
   private static final int DIVIATION_QT = 1;

   private static final String NA_NOTE = null;
   private static final String NA_REASON = null;

   final static RefWorkTypeKey HYDRAL = new RefWorkTypeKey( 10, "HYDRAUL" );
   final static RefWorkTypeKey FUEL = new RefWorkTypeKey( 10, "FUEL" );

   private HumanResourceKey authorizingHr;
   private int userId;
   private UserParametersFake userParametersFake;

   private RecordingEventBus eventBus;


   @Before
   public void setup() {

      authorizingHr = Domain.createHumanResource();
      userId = OrgHr.findByPrimaryKey( authorizingHr ).getUserId();

      userParametersFake = new UserParametersFake( userId, "LOGIC" );
      userParametersFake.setBoolean( "SPEC2000_UPPERCASE_ASSMBL_CD", false );
      UserParameters.setInstance( userId, "LOGIC", userParametersFake );

      eventBus = injectionOverrideRule.select( RecordingEventBus.class ).get();

      GlobalParameters globalParametersFake = new GlobalParametersFake( "LOGIC" );

      // Creating a component work package has logic to automatically created labour rows based on
      // the labour skills provided by the BLANK_RO_SIGNATURE config parm. By default those are
      // "AET" and "INSP", unfortunately both of those are 10-level labour skills
      // (ref_labour_skill) and do not exist in the am-query-test DB.
      globalParametersFake.setString( "BLANK_RO_SIGNATURE", "" );

      // The UserParametersFake is used because the user parameter
      // ALLOW_ASSIGN_LRP_ITEM_FOR_INWORK_WP need to have a value.
      GlobalParameters.setInstance( "LOGIC", globalParametersFake );

      SecurityUtils.setInstance( new SecurityUtils() {

         @Override
         public boolean isUserAuthorizedForResource( int userId, String resourceCode ) {
            return true;
         }
      } );
   }


   @After
   public void tearDown() {
      UserParameters.setInstance( userId, "LOGIC", null );
      GlobalParameters.setInstance( "LOGIC", null );
      SecurityUtils.setInstance( null );
   }


   /**
    * Verifies that when a non-recurring block chain is created that the first block has a status of
    * ACTV and the other blocks have a status of FORECAST.
    */
   @Test
   public void itCreatesNonRecurringBlockChainWithCorrectBlockStatus() throws Exception {

      // Given an active, non-recurring, block chain against an aircraft assembly's root config
      // slot;
      // - having three blocks
      // - having a scheduling rule
      // - having a minimum forecast range that allows the three blocks to be initialized
      final PartNoKey aircraftPart = Domain.createPart();

      final AssemblyKey aircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly acftAssy ) {
                  acftAssy.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot rootConfigSlot ) {
                        rootConfigSlot.addPartGroup( new DomainConfiguration<PartGroup>() {

                           @Override
                           public void configure( PartGroup rootCsPartGroup ) {
                              rootCsPartGroup.setName( "rootCsPartGroup" );
                              rootCsPartGroup.setInventoryClass( ACFT );
                              rootCsPartGroup.addPart( aircraftPart );
                           }
                        } );
                     }
                  } );
               }
            } );

      final ConfigSlotKey aircraftRootConfigSlot = new ConfigSlotKey( aircraftAssembly, 0 );

      final Map<Integer, TaskTaskKey> blockChainDefn =
            Domain.createBlockChainDefinition( new DomainConfiguration<BlockChainDefinition>() {

               @Override
               public void configure( BlockChainDefinition blockChainDefn ) {
                  blockChainDefn.setName( BLOCK_CHAIN_NAME );
                  blockChainDefn.setRecurring( false );
                  blockChainDefn.setConfigurationSlot( aircraftRootConfigSlot );
                  blockChainDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );

                  blockChainDefn.addBlock( BLOCK_1, BLOCK_1_CODE, BLOCK_1_NAME );
                  blockChainDefn.addBlock( BLOCK_2, BLOCK_2_CODE, BLOCK_2_NAME );
                  blockChainDefn.addBlock( BLOCK_3, BLOCK_3_CODE, BLOCK_3_NAME );

                  blockChainDefn.setScheduledFromEffectiveDate( new Date() );
                  blockChainDefn.addOneTimeSchedulingRule( CDY, 10 );
                  blockChainDefn.setMinimumForecastRange( 50 );
               }
            } );

      // Given an aircraft applicable to the block chain.
      final InventoryKey aircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aircraft ) {
            aircraft.setAssembly( aircraftAssembly );
            aircraft.setPart( aircraftPart );
         }
      } );

      // When the block tasks are created.
      // (note, creating one block in the chain will create all others).
      TaskKey block1Task = new TaskBean().createFromTaskDefinition( null, aircraft,
            blockChainDefn.get( BLOCK_1 ), null, authorizingHr, null );

      // Then the first block in the chain has a status of ACTV.
      Assert.assertEquals( "Unexpected status of first block task in chain.",
            RefEventStatusKey.ACTV, evtEventDao.findByPrimaryKey( block1Task ).getEventStatus() );

      // Then the second block in the chain has a status of FORECAST.
      TaskKey block2Task = getNextBlockInChain( block1Task );
      Assert.assertEquals( "Unexpected status of second block task in chain.", FORECAST,
            getStatus( block2Task ) );

      // Then the third block in the chain has a status of FORECAST.
      TaskKey block3Task = getNextBlockInChain( block2Task );
      Assert.assertEquals( "Unexpected status of second block task in chain.", FORECAST,
            getStatus( block3Task ) );
   }


   /**
    *
    * Verifies that when an active requirement task, which used to belong to a cancelled block task,
    * and the block definition of that cancelled block task is initialized again, that a new block
    * and an active requirement task are created. Also, the requirement task is a subtask of that
    * block task.
    *
    * Note: this scenario also requires that the block and req definitions be on-condition and
    * non-recurring. And that the req task has a deadline (thus the req definition has a scheduling
    * rule).
    *
    */
   @Test
   public void itCreatesBlockWithReqWhenOnConditionReqAlreadyExists() throws Exception {

      // Given an aircraft assembly and an aircraft part assigned to the assembly's root config
      // slot's part group.
      final PartNoKey aircraftPart = Domain.createPart();
      final AssemblyKey aircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly acftAssy ) {
                  acftAssy.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot rootConfigSlot ) {
                        rootConfigSlot.addPartGroup( new DomainConfiguration<PartGroup>() {

                           @Override
                           public void configure( PartGroup rootCsPartGroup ) {
                              rootCsPartGroup.setName( "rootCsPartGroup" );
                              rootCsPartGroup.setInventoryClass( ACFT );
                              rootCsPartGroup.addPart( aircraftPart );
                           }
                        } );
                     }
                  } );
               }
            } );
      final ConfigSlotKey aircraftRootConfigSlot = new ConfigSlotKey( aircraftAssembly, 0 );

      // Given a block definition that is on-condition and non-recurring
      // and a requirement definition that is on-condition, non-recurring, has a scheduling rule,
      // and assigned to the block definition.
      final TaskTaskKey reqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition reqDefn ) {
                  reqDefn.setOnCondition( true );
                  reqDefn.setRecurring( false );
                  reqDefn.addSchedulingRule( CDY, TEN );
                  reqDefn.againstConfigurationSlot( aircraftRootConfigSlot );

                  // This defn is not unique because it is on-condition, is not recurring, and has
                  // no dependencies.
                  reqDefn.setUnique( false );
               }
            } );

      final TaskTaskKey blockDefn =
            Domain.createBlockDefinition( new DomainConfiguration<BlockDefinition>() {

               @Override
               public void configure( BlockDefinition blockDefn ) {
                  blockDefn.setOnCondition( true );
                  blockDefn.setRecurring( false );
                  blockDefn.setConfigurationSlot( aircraftRootConfigSlot );
                  blockDefn.addRequirementDefinition( reqDefn );

                  // This defn is not unique because it is on-condition, is not recurring, has
                  // no dependencies, and is not in a block chain.
                  blockDefn.setUnique( false );
               }
            } );

      // Given a block task based on the block definition with a status of cancelled.
      final InventoryKey aircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aircraft ) {
            aircraft.setAssembly( aircraftAssembly );
            aircraft.setPart( aircraftPart );
         }
      } );
      Domain.createBlock( new DomainConfiguration<Block>() {

         @Override
         public void configure( Block block ) {
            block.setDefinition( blockDefn );
            block.setStatus( CANCEL );
            block.setInventory( aircraft );
         }
      } );

      // Given a requirement task based on the requirement definition, with a status of active.
      Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement req ) {
            req.setDefinition( reqDefn );
            req.setStatus( ACTV );
            req.setInventory( aircraft );
            req.addCalendarDeadline( CDY, TEN, null );
         }
      } );

      // When the block definition is initialized against the same inventory as the block task.
      TaskKey newBlock = new TaskBean().createFromTaskDefinition( null, aircraft, blockDefn, null,
            authorizingHr, null );

      // Then a new block task is created with a status of active.
      Assert.assertEquals( "Unexpected state of the new block.", ACTV, getStatus( newBlock ) );

      // Then a new requirement task is created with a status of active and is a sub-task of the new
      // block task.
      TaskKey newReq = getReqTaskOfBlockTask( newBlock );
      Assert.assertTrue( "Requirement task not a sub-task of the block task.", newReq != null );
      Assert.assertEquals( "Unexpected state of the new requirement task.", ACTV,
            getStatus( newReq ) );

   }


   /**
    * Given an assembly with an activated requirement
    *
    * And an aircraft based on this assembly has an activated instance of that requirement
    *
    * And the aircraft has a committed work package containing that activated requirement instance
    *
    * And a revision of this requirement has been created since work package commit
    *
    * When the requirement instance is unassigned from the work package
    *
    * Then the unassigned requirement instance is updated to the latest revision
    *
    */
   @Test
   public void itUpdatesReqRevisionWhenReqUnassignedFromCommittedWorkPkg() throws Exception {

      // Given
      final AssemblyKey aircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly builder ) {
                  builder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot rootCs ) {
                        rootCs.setCode( "rootConfigSlot" );
                     }
                  } );
               }
            } );

      final TaskTaskKey reqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition reqDefn ) {
                  reqDefn.setCode( "Req1" );
                  reqDefn.setRevisionNumber( REVISION_1 );
                  reqDefn.setStatus( RefTaskDefinitionStatusKey.SUPRSEDE );
                  reqDefn.againstConfigurationSlot(
                        Domain.readRootConfigurationSlot( aircraftAssembly ) );
                  reqDefn.setOnCondition( false );
               }

            } );

      final InventoryKey aircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aircraft ) {
            aircraft.setAssembly( aircraftAssembly );
            aircraft.allowSynchronization();
         }

      } );

      final TaskKey requirement = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement req ) {
            req.setDefinition( reqDefn );
            req.setInventory( aircraft );
            req.setStatus( ACTV );
         }
      } );

      Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage builder ) {
            builder.addTask( requirement );
            builder.setAircraft( aircraft );
            builder.setStatus( RefEventStatusKey.COMMIT );
         }
      } );

      TaskTaskKey revisedReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition requirementDefinition ) {
                  requirementDefinition.setPreviousRevision( reqDefn );
                  requirementDefinition.setCode( "Req1" );
                  requirementDefinition.setRevisionNumber( REVISION_2 );
                  requirementDefinition.setStatus( RefTaskDefinitionStatusKey.ACTV );
                  requirementDefinition.againstConfigurationSlot(
                        Domain.readRootConfigurationSlot( aircraftAssembly ) );
                  requirementDefinition.setOnCondition( false );
               }
            } );

      // When
      TaskBean taskBean = new TaskBean();
      taskBean.unassignTask( requirement, authorizingHr, null, null );

      WorkItemGeneratorExecuteImmediateFake workItemGenFake =
            new WorkItemGeneratorExecuteImmediateFake( authorizingHr );
      InventorySynchronizer invSynchronizer = new InventorySynchronizer( workItemGenFake );

      invSynchronizer.processInventory( invSynchronizer.getInventory() );

      TaskTaskKey expectedReqDefn = revisedReqDefn;

      TaskTaskKey actualReqDefn = schedStaskDao.findByPrimaryKey( requirement ).getTaskTaskKey();

      Assert.assertEquals(
            "Unexpected requirement revision found for task unassigned from committed work package",
            expectedReqDefn, actualReqDefn );

   }


   /**
    *
    * Verifies that when historic task completion date is updated, all configuration change events
    * and their associated sub-components records in inv_install and inv_remove tables are updated
    * in sync.
    *
    */
   @Test
   public void itUpdatesConfigChangeEventDateWhenSetActualEndDate() throws Exception {

      final InventoryKey aircraftInvKey = Domain.createAircraft();

      final InventoryKey trackedInvKey =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory trk ) {
                  trk.setParent( aircraftInvKey );
               }
            } );

      final TaskKey actualTaskKey =
            createTaskOfInventory( aircraftInvKey, taskStartDate, taskEndDate );

      TaskKey workPackageKey = Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage workPackage ) {
            workPackage.setAircraft( aircraftInvKey );
            workPackage.setActualStartDate( taskStartDate );
            workPackage.setActualEndDate( taskEndDate );
            workPackage.setStatus( RefEventStatusKey.IN_WORK );
            workPackage.addTask( actualTaskKey );
         }
      } );

      EventKey removalEventKey = createRemovalTrackedEvent( workPackageKey.getEventKey(),
            actualTaskKey, trackedInvKey, taskEndDate );

      Date newTaskEndDate = DateUtils.addDays( taskEndDate, +1 );
      TaskBean taskBean = new TaskBean();
      taskBean.setActualEndDate( actualTaskKey, newTaskEndDate );

      assertEventDate( removalEventKey, trackedInvKey, newTaskEndDate );
      assertRemoveRecordDate( removalEventKey, trackedInvKey, newTaskEndDate );
   }


   /**
    *
    * Verifies that when a component removal happens in one work package and the task is completed
    * in another, the removal date is not updated to match the second work package
    *
    */
   @Test
   public void itDoesNotUpdateConfigChangeEventDateWhenSetActualEndDateOnDifferentWorkPackage()
         throws Exception {

      final InventoryKey aircraftInvKey = Domain.createAircraft();

      final InventoryKey trackedInvKey =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory trk ) {
                  trk.setParent( aircraftInvKey );
               }
            } );

      final TaskKey actualTaskKey =
            createTaskOfInventory( aircraftInvKey, taskStartDate, taskEndDate );

      TaskKey workPackageKey = Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage workPackage ) {
            workPackage.setAircraft( aircraftInvKey );
            workPackage.setActualStartDate( taskStartDate );
            workPackage.setActualEndDate( taskEndDate );
            workPackage.setStatus( RefEventStatusKey.IN_WORK );
            workPackage.addTask( actualTaskKey );
         }
      } );

      EventKey removalEventKey = createRemovalTrackedEvent( workPackageKey.getEventKey(),
            actualTaskKey, trackedInvKey, taskEndDate );

      Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage workPackage ) {
            workPackage.setAircraft( aircraftInvKey );
            workPackage.setActualStartDate( taskStartDate );
            workPackage.setActualEndDate( taskEndDate );
            workPackage.setStatus( RefEventStatusKey.IN_WORK );
            workPackage.addTask( actualTaskKey );
         }
      } );

      Date newTaskEndDate = DateUtils.addDays( taskEndDate, +1 );
      TaskBean taskBean = new TaskBean();

      taskBean.setActualEndDate( actualTaskKey, newTaskEndDate );

      assertEventDate( removalEventKey, trackedInvKey, taskEndDate );
      assertRemoveRecordDate( removalEventKey, trackedInvKey, taskEndDate );
   }


   private TaskKey createTaskOfInventory( final InventoryKey invKey, final Date startDate,
         final Date endDate ) {

      TaskKey actualTaskKey = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement actualTask ) {
            actualTask.setInventory( invKey );
            actualTask.setActualStartDate( startDate );
            actualTask.setActualEndDate( endDate );
            actualTask.setStatus( RefEventStatusKey.COMPLETE );
         }
      } );

      return actualTaskKey;
   }


   private EventKey createRemovalTrackedEvent( EventKey checkKey, TaskKey actualTaskKey,
         InventoryKey removedInventory, Date removeDate ) {

      SchedStaskTable task = schedStaskDao.findByPrimaryKey( actualTaskKey );

      Set<UsageSnapshot> usages = new HashSet<UsageSnapshot>();
      usages.add( new UsageSnapshot( removedInventory, HOURS, eventHoursUsage.doubleValue(),
            eventHoursUsage.doubleValue(), eventHoursUsage.doubleValue(), null, null ) );
      usages.add( new UsageSnapshot( removedInventory, CYCLES, eventCycleUsage.doubleValue(),
            eventCycleUsage.doubleValue(), eventCycleUsage.doubleValue(), null, null ) );

      // remove from aircraft
      EventKey eventKey = AttachmentService.detachInventoryFromAircraft( task.getMainInventory(),
            removedInventory, checkKey.getEventKey(), removeDate, usages );

      new EventRelationshipBuilder().fromEvent( actualTaskKey ).toEvent( eventKey )
            .withType( RefRelationTypeKey.TTFG ).build();

      // Create a row in inv_remove for the sub-inventory.
      DataSetArgument args = new DataSetArgument();
      args.add( "inv_remove_id", new SequentialUuidGenerator().newUuid() );
      args.add( eventKey, "event_db_id", "event_id" );
      args.add( "event_dt", removeDate );
      args.add( "main_inv_bool", true );
      args.add( removedInventory, "inv_no_db_id", "inv_no_id" );
      args.add( null, "nh_inv_no_db_id", "nh_inv_no_id" );
      args.add( null, "h_inv_no_db_id", "h_inv_no_id" );
      args.add( null, "assmbl_inv_no_db_id", "assmbl_inv_no_id" );
      MxDataAccess.getInstance().executeInsert( "inv_remove", args );

      return eventKey;

   }


   /**
    * Given an aircraft assembly with activated requirement definition<br />
    * And an aircraft with requirement based on the assembly and requirement definition<br />
    * And a committed work package on the aircraft containing the requirement<br />
    * And an active revision for the requirement definition
    *
    * When we un-commit the work package and run baseline sync
    *
    * Then requirement is linked to the revision requirement definition
    */
   @Test
   public void itRevisesTheTaskWhenWPIsUnCommitted() throws Exception {
      final AssemblyKey aircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly builder ) {
                  builder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot configurationSlot ) {
                        configurationSlot.setCode( "rootConfigurationSlot" );
                     }
                  } );

               }

            } );

      final TaskTaskKey requirementDefinition =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition builder ) {
                  builder.setCode( REQ_DEFN_CODE_1 );
                  builder.setStatus( RefTaskDefinitionStatusKey.SUPRSEDE );
                  builder.againstConfigurationSlot(
                        Domain.readRootConfigurationSlot( aircraftAssembly ) );
               }

            } );

      final InventoryKey aircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft builder ) {
            builder.setAssembly( aircraftAssembly );
            builder.setReceivedDate( RECEIVED_DATE );
            builder.allowSynchronization();

         }

      } );

      final TaskKey requirement = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement builder ) {
            builder.setDefinition( requirementDefinition );
            builder.setInventory( aircraft );
            builder.setStatus( ACTV );
         }

      } );

      final TaskKey workPackage = Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage builder ) {
            builder.addTask( requirement );
            builder.setAircraft( aircraft );
            builder.setStatus( COMMIT );
         }

      } );

      final TaskTaskKey revisedRequirementDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition reqDefn ) {
                  reqDefn.againstConfigurationSlot(
                        Domain.readRootConfigurationSlot( aircraftAssembly ) );
                  reqDefn.setPreviousRevision( requirementDefinition );
                  reqDefn.setCode( REQ_DEFN_CODE_2 );
                  reqDefn.setRevisionNumber( REVISION_2 );
                  reqDefn.setScheduledFromReceivedDate();
                  reqDefn.setFleetApproval( true );
                  reqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
               }
            } );

      UserParametersStub userParametersStub = new UserParametersStub( userId, "LOGIC" );
      userParametersStub.setBoolean( "ALLOW_ASSIGN_LRP_ITEM_FOR_INWORK_WP", false );
      UserParameters.setInstance( userId, "LOGIC", userParametersStub );

      TaskBean taskBean = new TaskBean();

      taskBean.uncommitWorkscope( workPackage, authorizingHr, null, null );

      WorkItemGeneratorExecuteImmediateFake workItemGenFake =
            new WorkItemGeneratorExecuteImmediateFake( authorizingHr );
      InventorySynchronizer invSynchronizer = new InventorySynchronizer( workItemGenFake );

      invSynchronizer.processInventory( invSynchronizer.getInventory() );

      TaskTaskKey acutalRequirementDefn =
            schedStaskDao.findByPrimaryKey( requirement ).getTaskTaskKey();
      assertThat( "Unexpected revision version for request: " + acutalRequirementDefn,
            acutalRequirementDefn, equalTo( revisedRequirementDefn ) );

   }


   /**
    * Given an aircraft with usage parm cycles defined on the aircraft assembly.<br />
    * And an adhoc task defined on the aircraft with usage deadline of cycles.
    *
    * When the interval of the deadline changed.
    *
    * Then the due value is updated correctly.
    */
   @Test
   public void itChangesDueQtWhenUpdatingAdhocTaskUsageDeadlineInterval() throws Exception {
      final AssemblyKey aircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly builder ) {
                  builder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot builder ) {
                        builder.addUsageParameter( CYCLES );

                     }

                  } );

               }

            } );
      final InventoryKey aircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft builder ) {
            builder.setAssembly( aircraftAssembly );
            builder.addUsage( CYCLES, new BigDecimal( 400 ) );
         }

      } );

      final TaskKey adhocTask = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement builder ) {
            builder.setInventory( aircraft );
            builder.setTaskClass( ADHOC );
            builder.addDeadline( new DomainConfiguration<Deadline>() {

               @Override
               public void configure( Deadline builder ) {
                  builder.setScheduledFrom( CUSTOM );
                  builder.setInterval( ORIGINAL_DEADLINE_INTERVAL );
                  builder.setStartTsn( ORIGINAL_START_VALUE );
                  builder.setDueValue( ORIGINAL_START_VALUE + ORIGINAL_DEADLINE_INTERVAL );
                  builder.setUsageType( CYCLES );
               }
            } );
         }

      } );

      TaskBean taskBean = new TaskBean();

      UsageDeadline newUsageDeadline = taskBean.getUsageDeadline( adhocTask, CYCLES );
      newUsageDeadline.setInterval( NEW_DEADLINE_INTERVAL );
      newUsageDeadline.setRecalculateDeadline( true );

      taskBean.setUsageDeadline( adhocTask, newUsageDeadline, authorizingHr );

      assertThat(
            EvtSchedDeadTable.findByPrimaryKey( adhocTask, CYCLES ).getDeadlineQt().intValue(),
            equalTo( NEW_DEADLINE_INTERVAL + ORIGINAL_START_VALUE ) );
   }


   /**
    * Given an aircraft with usage parm cycles defined on the aircraft assembly.<br />
    * And an adhoc task defined on the aircraft with calendar deadline of Calendar days.
    *
    * When the interval of the deadline changed.
    *
    * Then the due date is updated correctly.
    */
   @Test
   public void itChangesDueDateWhenUpdatingAdhocTaskCalendarDeadlineInterval() throws Exception {
      final AssemblyKey aircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly builder ) {
                  builder.setCode( "ASSEMBLY" );

               }

            } );
      final InventoryKey aircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft builder ) {
            builder.setAssembly( aircraftAssembly );
            builder.setManufacturedDate( ORIGINAL_DEADLINE_START_DATE );
         }

      } );

      final TaskKey adhocTask = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement builder ) {
            builder.setInventory( aircraft );
            builder.setTaskClass( ADHOC );
            builder.addCalendarDeadline( CDY, CUSTOM, TEN,
                  new BigDecimal( ORIGINAL_DEADLINE_INTERVAL ),
                  DateUtils.addDays( ORIGINAL_DEADLINE_START_DATE, ORIGINAL_DEADLINE_INTERVAL ),
                  ORIGINAL_DEADLINE_START_DATE );
         }

      } );

      TaskBean taskBean = new TaskBean();

      CalendarDeadline newCalendarDeadline = taskBean.getCalendarDeadline( adhocTask );
      newCalendarDeadline.setInterval( NEW_DEADLINE_INTERVAL );
      newCalendarDeadline.setRecalculateDeadline( true );

      taskBean.setCalendarDeadline( adhocTask, newCalendarDeadline, authorizingHr );
      Date newDueDate = DateUtils.addDays( ORIGINAL_DEADLINE_START_DATE, NEW_DEADLINE_INTERVAL );
      assertThat( EvtSchedDeadTable.findByPrimaryKey( adhocTask, CDY ).getDeadlineDate(),
            equalTo( org.apache.commons.lang.time.DateUtils
                  .setMilliseconds( DateUtils.getEndOfDay( newDueDate ), 0 ) ) );

   }


   /**
    * Given an aircraft with usage parm cycles defined on the aircraft assembly.<br />
    * And an adhoc task defined on the aircraft with usage deadline of cycles.
    *
    * When the start value of the deadline changed.
    *
    * Then the due value is updated correctly.
    */
   @Test
   public void itChangesDueQtWhenUpdatingAdhocTaskUsageDeadlineStartQt() throws Exception {
      final AssemblyKey aircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly builder ) {
                  builder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot builder ) {
                        builder.addUsageParameter( CYCLES );

                     }

                  } );

               }

            } );
      final InventoryKey aircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft builder ) {
            builder.setAssembly( aircraftAssembly );
            builder.addUsage( CYCLES, new BigDecimal( 400 ) );
         }

      } );

      final TaskKey adhocTask = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement builder ) {
            builder.setInventory( aircraft );
            builder.setTaskClass( ADHOC );
            builder.addDeadline( new DomainConfiguration<Deadline>() {

               @Override
               public void configure( Deadline builder ) {
                  builder.setScheduledFrom( CUSTOM );
                  builder.setInterval( ORIGINAL_DEADLINE_INTERVAL );
                  builder.setStartTsn( ORIGINAL_START_VALUE );
                  builder.setDueValue( ORIGINAL_START_VALUE + ORIGINAL_DEADLINE_INTERVAL );
                  builder.setUsageType( CYCLES );
               }
            } );
         }

      } );

      TaskBean taskBean = new TaskBean();

      UsageDeadline newUsageDeadline = taskBean.getUsageDeadline( adhocTask, CYCLES );
      newUsageDeadline.setStartQt( NEW_START_VALUE );
      newUsageDeadline.setRecalculateDeadline( true );

      taskBean.setUsageDeadline( adhocTask, newUsageDeadline, authorizingHr );

      assertThat(
            EvtSchedDeadTable.findByPrimaryKey( adhocTask, CYCLES ).getDeadlineQt().intValue(),
            equalTo( ORIGINAL_DEADLINE_INTERVAL + NEW_START_VALUE ) );
   }


   /**
    * Given an aircraft with usage parm cycles defined on the aircraft assembly.<br />
    * And an adhoc task defined on the aircraft with a calendar deadline of calendar days.
    *
    * When the start date of the deadline changed.
    *
    * Then the due date is updated correctly.
    */
   @Test
   public void itChangesDueDateWhenUpdatingAdhocTaskCalendarDeadlineStartDt() throws Exception {
      final AssemblyKey aircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly builder ) {
                  builder.setCode( "ASSEMBLY" );

               }

            } );
      final InventoryKey aircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft builder ) {
            builder.setAssembly( aircraftAssembly );
            builder.setManufacturedDate( ORIGINAL_DEADLINE_START_DATE );
         }

      } );

      final TaskKey adhocTask = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement builder ) {
            builder.setInventory( aircraft );
            builder.setTaskClass( ADHOC );
            builder.addCalendarDeadline( CDY, CUSTOM, TEN,
                  new BigDecimal( ORIGINAL_DEADLINE_INTERVAL ),
                  DateUtils.addDays( ORIGINAL_DEADLINE_START_DATE, ORIGINAL_DEADLINE_INTERVAL ),
                  ORIGINAL_DEADLINE_START_DATE );
         }

      } );

      TaskBean taskBean = new TaskBean();

      CalendarDeadline newCalendarDeadline = taskBean.getCalendarDeadline( adhocTask );
      newCalendarDeadline.setStartDt( NEW_DEADLINE_START_DATE );;
      newCalendarDeadline.setRecalculateDeadline( true );

      taskBean.setCalendarDeadline( adhocTask, newCalendarDeadline, authorizingHr );

      Date newDueDate = DateUtils.addDays( NEW_DEADLINE_START_DATE, 10 );

      assertThat( EvtSchedDeadTable.findByPrimaryKey( adhocTask, CDY ).getDeadlineDate(),
            equalTo( org.apache.commons.lang.time.DateUtils
                  .setMilliseconds( DateUtils.getEndOfDay( newDueDate ), 0 ) ) );

   }


   /**
    * Given an aircraft with usage parm cycles defined on the aircraft assembly.<br />
    * And an adhoc task defined on the aircraft.
    *
    * When crate a usage deadline of cycles and a calendar deadline of calendar days on the adhoc
    * task.
    *
    * Then the deadline with earlier due date is marked as driving deadline.
    */
   @Test
   public void itMarksEarilerExpiredDeadlineAsDrivingDeadline() throws Exception {
      final AssemblyKey aircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly builder ) {
                  builder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot builder ) {
                        builder.addUsageParameter( CYCLES );

                     }

                  } );
               }

            } );

      final FcModelKey fcModel =
            Domain.createForecastModel( new DomainConfiguration<ForecastModel>() {

               @Override
               public void configure( ForecastModel builder ) {
                  builder.addRange( 1, 1, CYCLES, 5d );

               }

            } );
      final InventoryKey aircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft builder ) {
            builder.setAssembly( aircraftAssembly );
            builder.addUsage( CYCLES, TEN );
            builder.setManufacturedDate( ORIGINAL_DEADLINE_START_DATE );
            builder.setForecastModel( fcModel );
         }

      } );

      final TaskKey adhocTask = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement builder ) {
            builder.setInventory( aircraft );
            builder.setActualStartDate( NEW_DEADLINE_START_DATE );
            builder.setTaskClass( ADHOC );
         }

      } );

      UsageDeadline usageDeadline =
            new UsageDeadline( CYCLES, ORIGINAL_DEADLINE_INTERVAL, DUE_VALUE );

      CalendarDeadline calendarDeadline = new CalendarDeadline( CDY, ORIGINAL_DEADLINE_INTERVAL,
            DateUtils.addDays( ORIGINAL_DEADLINE_START_DATE, ORIGINAL_DEADLINE_INTERVAL ) );

      TaskBean taskBean = new TaskBean();
      taskBean.setCalendarDeadline( adhocTask, calendarDeadline, authorizingHr );
      taskBean.addUsageDeadline( adhocTask, usageDeadline, authorizingHr );

      assertThat( EvtSchedDeadTable.findByPrimaryKey( adhocTask, CYCLES ).isDriver(),
            equalTo( false ) );
      assertThat( EvtSchedDeadTable.findByPrimaryKey( adhocTask, CDY ).isDriver(),
            equalTo( true ) );

   }


   /**
    * Given an aircraft with usage parm cycles defined on the aircraft assembly.<br />
    * And an adhoc task defined on the aircraft with usage deadline of cycles.
    *
    * When removing the deadline
    *
    * Then deadline is removed from the database
    */
   @Test
   public void itRemovesDeadline() throws Exception {
      final AssemblyKey aircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly builder ) {
                  builder.setCode( "ASSEMBLY" );

               }

            } );
      final InventoryKey aircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft builder ) {
            builder.setAssembly( aircraftAssembly );
         }

      } );

      final TaskKey adhocTask = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement builder ) {
            builder.setInventory( aircraft );
            builder.setTaskClass( ADHOC );
            builder.addDeadline( new DomainConfiguration<Deadline>() {

               @Override
               public void configure( Deadline builder ) {
                  builder.setScheduledFrom( CUSTOM );
                  builder.setInterval( ORIGINAL_DEADLINE_INTERVAL );
                  builder.setStartTsn( ORIGINAL_START_VALUE );
                  builder.setDueValue( ORIGINAL_START_VALUE + ORIGINAL_DEADLINE_INTERVAL );
                  builder.setUsageType( CYCLES );
               }
            } );
         }

      } );

      assertThat( EvtSchedDeadTable.findByPrimaryKey( adhocTask, CYCLES ).exists(),
            equalTo( true ) );

      TaskBean taskBean = new TaskBean();

      taskBean.removeUsageDeadline( adhocTask, CYCLES, authorizingHr );

      assertThat( EvtSchedDeadTable.findByPrimaryKey( adhocTask, CYCLES ).exists(),
            equalTo( false ) );
   }


   /**
    * This test is verifying when add a child task to a work package, it will be added.
    *
    * Given a work package <br />
    * And a requirement
    *
    * When assign the requirement to the work package
    *
    * Then verify the next high and highest event is the work package.
    */
   @Test
   public void itAssignsTaskToWorkPackage() throws Exception {
      final TaskKey workPackage = Domain.createWorkPackage();
      final TaskKey childRequirement = Domain.createRequirement();
      TaskBean taskBean = new TaskBean();

      taskBean.addChildTask( workPackage, childRequirement, authorizingHr, null, null );
      EventKey childEventKey = new EventKey( childRequirement.getDbId(), childRequirement.getId() );

      EvtEventDao evtEventDao = InjectorContainer.get().getInstance( EvtEventDao.class );
      EventKey actualParentEventKey =
            evtEventDao.findByPrimaryKey( childEventKey ).getNhEvent().getEventKey();
      EventKey actualRootEventKey =
            evtEventDao.findByPrimaryKey( childEventKey ).getHEvent().getEventKey();
      EventKey expectParentEventKey = new EventKey( workPackage.getDbId(), workPackage.getId() );
      assertEquals( expectParentEventKey, actualParentEventKey );
      assertEquals( expectParentEventKey, actualRootEventKey );

   }


   /**
    * Verify that when a task that is based on an executable requirement definition is added to a
    * committed work package, then the task is added to the work scope of that work package.
    */
   @Test
   public void addingChildTaskAssignsExecutableTaskToWorkscopeOfCommittedWorkPackage()
         throws Exception {

      // Given a committed work package.
      final TaskKey workPackage = Domain.createWorkPackage( wp -> {
         wp.setStatus( COMMIT );
      } );

      // Given an active task that is based on an executable requirement definition.
      TaskTaskKey execReqDefn = Domain.createRequirementDefinition( defn -> {
         defn.setExecutable( true );
      } );
      final TaskKey task = Domain.createRequirement( execReq -> {
         execReq.setStatus( ACTV );
         execReq.setDefinition( execReqDefn );
      } );

      // When the task is added as a child to the work package.
      TaskBean taskBean = new TaskBean();
      taskBean.setSessionContext( new SessionContextFake() );
      taskBean.addChildTask( workPackage, task, authorizingHr, NA_REASON, NA_NOTE );

      // Then the task is assigned to the work package.
      EventKey workpackageOfTask = evtEventDao.findByPrimaryKey( task.getEventKey() ).getHEvent();
      assertThat( "Task highest event is not the work package.", workpackageOfTask,
            is( workPackage.getEventKey() ) );

      // Then the task is assigned to the work scope of the work package.
      List<TaskKey> tasks = getTasksInWorkScope( workPackage );
      assertThat( "Unexpected number of tasks in workpackage work scope.", tasks.size(), is( 1 ) );
      assertThat( "Unexpected task in workpackage work scope.", tasks.get( 0 ), is( task ) );
   }


   /**
    * This test case is testing when add a usage deadline to a activated requirement task under a
    * forecast block task, the usage deadline will be added.
    *
    * Given a forecast block task. <br />
    * And an activated requirement task under this block task.
    *
    * When add a usage deadline to this requirement.
    *
    * Then the deadline is appended to the requirement task. And the deadline values are persisted.
    */
   @Test
   public void itAddsUsageDeadlineToActivatedRequirementUnderForcastedBlock() throws Exception {

      // The forecast model is requirement by the add usage deadline logic
      final FcModelKey fcModelKey =
            Domain.createForecastModel( new DomainConfiguration<ForecastModel>() {

               @Override
               public void configure( ForecastModel forecastModel ) {
                  forecastModel.addRange( 1, 1, CYCLES, 1 );
               }
            } );

      final InventoryKey aircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft builder ) {
            builder.addUsage( CYCLES, BigDecimal.ONE );
            builder.setForecastModel( fcModelKey );
         }

      } );
      final TaskKey requirement = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement builder ) {
            builder.setInventory( aircraft );
            builder.setStatus( RefEventStatusKey.ACTV );
         }

      } );
      Domain.createBlock( new DomainConfiguration<Block>() {

         @Override
         public void configure( Block builder ) {
            builder.addRequirement( requirement );
            builder.setStatus( RefEventStatusKey.FORECAST );
            builder.setInventory( aircraft );

         }

      } );

      TaskBean taskBean = new TaskBean();

      // This method initialize the task for the task bean
      UsageDeadline deadline = new UsageDeadline( CYCLES, CYCLES_USAGE_INTERVAL,
            CYCLES_USAGE_TSN_DEADLINE, NOTIFY_QT, DIVIATION_QT );
      taskBean.addUsageDeadline( requirement, deadline, authorizingHr );
      EvtSchedDeadTable evtSchedDead = EvtSchedDeadTable
            .findByPrimaryKey( new EventDeadlineKey( requirement.getEventKey(), CYCLES ) );

      assertTrue( "Usage deadline is not added.", evtSchedDead.exists() );
      assertEquals( "Usage deadline quantity is not added correctly.", 0,
            new BigDecimal( CYCLES_USAGE_TSN_DEADLINE )
                  .compareTo( new BigDecimal( evtSchedDead.getDeadlineQt() ) ) );
      assertEquals( "Usage interval is not added correctly.", CYCLES_USAGE_INTERVAL,
            evtSchedDead.getIntervalQt(), 0.0 );
      assertEquals( "Notify quantity is not added correctly.", NOTIFY_QT,
            evtSchedDead.getNotifyQt(), 0.0 );
      assertEquals( "Diviation quantity is not added correctly.", DIVIATION_QT,
            evtSchedDead.getDeviationQt(), 0.0 );
   }


   /**
    * This test case is testing when modify a list usage deadlines with an existing usage deadline
    * and a new usage deadline to an activated requirement task under a forecast block task, it will
    * be modified.
    *
    * Given a forecast block task. <br />
    * And a activated requirement task under this block with a usage deadline.
    *
    * When modify usage deadlines with the existing usage deadline and new usage deadline to this
    * requirement.
    *
    * Then verify the existing usage deadline is updated and the new usage deadline is added.
    */
   @Test
   public void itModifiesUsageDeadlineOfActivatedRequirementUnderForcastedBlock() throws Exception {

      // The forecast model is requirement by the modify usage deadline logic
      final FcModelKey fcModelKey =
            Domain.createForecastModel( new DomainConfiguration<ForecastModel>() {

               @Override
               public void configure( ForecastModel forecastModel ) {
                  forecastModel.addRange( 1, 1, CYCLES, 1 );
                  forecastModel.addRange( 1, 1, HOURS, 5 );
               }
            } );

      final InventoryKey aircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft builder ) {
            builder.addUsage( CYCLES, BigDecimal.ONE );
            builder.addUsage( HOURS, BigDecimal.ONE );
            builder.setForecastModel( fcModelKey );
         }

      } );
      final TaskKey requirement = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement builder ) {
            builder.setInventory( aircraft );
            builder.setStatus( RefEventStatusKey.ACTV );
            builder.addDeadline( new DomainConfiguration<Deadline>() {

               @Override
               public void configure( Deadline builder ) {
                  builder.setUsageType( HOURS );
                  builder.setDueValue( 10 );

               }

            } );
         }

      } );
      Domain.createBlock( new DomainConfiguration<Block>() {

         @Override
         public void configure( Block builder ) {
            builder.addRequirement( requirement );
            builder.setStatus( RefEventStatusKey.FORECAST );
            builder.setInventory( aircraft );

         }

      } );

      TaskBean taskBean = new TaskBean();

      // This method initialize the task for the task bean
      UsageDeadline existingDeadline = new UsageDeadline( HOURS, HOURS_USAGE_INTERVAL,
            HOURS_USAGE_TSN_DEADLINE, NOTIFY_QT, DIVIATION_QT );
      UsageDeadline newDeadline = new UsageDeadline( CYCLES, CYCLES_USAGE_INTERVAL,
            CYCLES_USAGE_TSN_DEADLINE, NOTIFY_QT, DIVIATION_QT );
      taskBean.modifyDeadlines( requirement, new UsageDeadline[] { existingDeadline, newDeadline },
            authorizingHr );

      EvtSchedDeadTable existingEvtSchedDead = EvtSchedDeadTable
            .findByPrimaryKey( new EventDeadlineKey( requirement.getEventKey(), HOURS ) );

      EvtSchedDeadTable newEvtSchedDead = EvtSchedDeadTable
            .findByPrimaryKey( new EventDeadlineKey( requirement.getEventKey(), CYCLES ) );

      assertEquals( "The existing usage deadline quantity is not added correctly.", 0,
            new BigDecimal( HOURS_USAGE_TSN_DEADLINE )
                  .compareTo( new BigDecimal( existingEvtSchedDead.getDeadlineQt() ) ) );
      assertEquals( "The existing usage deadline interval is not added correctly.",
            HOURS_USAGE_INTERVAL, existingEvtSchedDead.getIntervalQt(), 0.0 );
      assertEquals( "The existing usage deadline notify quantity is not added correctly.",
            NOTIFY_QT, existingEvtSchedDead.getNotifyQt(), 0.0 );
      assertEquals( "The existing usage deadline diviation quantity is not added correctly.",
            DIVIATION_QT, existingEvtSchedDead.getDeviationQt(), 0.0 );

      assertTrue( "The new sage deadline is not added.", newEvtSchedDead.exists() );
      assertEquals( "The new usage deadline quantity is not added correctly.", 0,
            new BigDecimal( CYCLES_USAGE_TSN_DEADLINE )
                  .compareTo( new BigDecimal( newEvtSchedDead.getDeadlineQt() ) ) );
      assertEquals( "The new usage deadline interval is not added correctly.",
            CYCLES_USAGE_INTERVAL, newEvtSchedDead.getIntervalQt(), 0.0 );
      assertEquals( "The new usage deadline notify quantity is not added correctly.", NOTIFY_QT,
            newEvtSchedDead.getNotifyQt(), 0.0 );
      assertEquals( "The new usage deadline diviation quantity is not added correctly.",
            DIVIATION_QT, newEvtSchedDead.getDeviationQt(), 0.0 );
   }


   /**
    * This test case is testing when remove usage deadline of an activated requirement task under a
    * forecast block task, it will be removed.
    *
    * Given a forecast block task. <br />
    * And a activated requirement instance under this block.
    *
    * When remove usage deadline of this requirement.
    *
    * Then verify the usage deadline is removed.
    */
   @Test
   public void itRemovesUsageDeadlineFromActivatedRequirementUnderForcastedBlock()
         throws Exception {

      final InventoryKey aircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft builder ) {
            builder.addUsage( CYCLES, BigDecimal.ONE );
         }

      } );
      final TaskKey requirement = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement builder ) {
            builder.setInventory( aircraft );
            builder.setStatus( RefEventStatusKey.ACTV );
            builder.addDeadline( new DomainConfiguration<Deadline>() {

               @Override
               public void configure( Deadline builder ) {
                  builder.setUsageType( CYCLES );
                  builder.setDueValue( 10 );

               }

            } );
         }

      } );
      Domain.createBlock( new DomainConfiguration<Block>() {

         @Override
         public void configure( Block builder ) {
            builder.addRequirement( requirement );
            builder.setStatus( RefEventStatusKey.FORECAST );
            builder.setInventory( aircraft );

         }

      } );

      TaskBean taskBean = new TaskBean();

      // This method initialize the task for the task bean
      taskBean.removeUsageDeadline( requirement, CYCLES, authorizingHr );

      EvtSchedDeadTable evtSchedDead = EvtSchedDeadTable
            .findByPrimaryKey( new EventDeadlineKey( requirement.getEventKey(), CYCLES ) );

      assertFalse( "The usage deadline is not removed.", evtSchedDead.exists() );
   }


   /**
    * This test case is testing when update usage deadline of an activated requirement task under a
    * forecast block task, it will be updated.
    *
    * Given a forecast block task. <br />
    * And a activated requirement task under this block.
    *
    * When update usage deadline of this requirement.
    *
    * Then verify the usage deadline is updated.
    */
   @Test
   public void itUpdatesUsageDeadlineForActivatedRequirementUnderForcastedBlock() throws Exception {

      // The forecast model is requirement by the set usage deadline logic
      final FcModelKey fcModelKey =
            Domain.createForecastModel( new DomainConfiguration<ForecastModel>() {

               @Override
               public void configure( ForecastModel forecastModel ) {
                  forecastModel.addRange( 1, 1, CYCLES, 1 );
               }
            } );

      final InventoryKey aircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft builder ) {
            builder.addUsage( CYCLES, BigDecimal.ONE );
            builder.setForecastModel( fcModelKey );
         }

      } );
      final TaskKey requirement = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement builder ) {
            builder.setInventory( aircraft );
            builder.setStatus( RefEventStatusKey.ACTV );
            builder.addDeadline( new DomainConfiguration<Deadline>() {

               @Override
               public void configure( Deadline builder ) {
                  builder.setUsageType( CYCLES );
                  builder.setDueValue( 10 );

               }

            } );
         }

      } );
      Domain.createBlock( new DomainConfiguration<Block>() {

         @Override
         public void configure( Block builder ) {
            builder.addRequirement( requirement );
            builder.setStatus( RefEventStatusKey.FORECAST );
            builder.setInventory( aircraft );

         }

      } );

      TaskBean taskBean = new TaskBean();

      // This method initialize the task for the task bean
      UsageDeadline deadline = new UsageDeadline( CYCLES, CYCLES_USAGE_INTERVAL,
            CYCLES_USAGE_TSN_DEADLINE, NOTIFY_QT, DIVIATION_QT );
      taskBean.setUsageDeadline( requirement, deadline, authorizingHr );

      EvtSchedDeadTable evtSchedDead = EvtSchedDeadTable
            .findByPrimaryKey( new EventDeadlineKey( requirement.getEventKey(), CYCLES ) );

      assertEquals( "The usage deadline quantity is not updated correctly.", 0,
            new BigDecimal( CYCLES_USAGE_TSN_DEADLINE )
                  .compareTo( new BigDecimal( evtSchedDead.getDeadlineQt() ) ) );
      assertEquals( "The usage dealine interval is not updated correctly.", CYCLES_USAGE_INTERVAL,
            evtSchedDead.getIntervalQt(), 0.0 );
      assertEquals( "The notify quantity if not updated correctly.", NOTIFY_QT,
            evtSchedDead.getNotifyQt(), 0.0 );
      assertEquals( "The diviation quantity is not updated correctly.", DIVIATION_QT,
            evtSchedDead.getDeviationQt(), 0.0 );
   }


   /**
    * Verify when creating tasks for a recurring requirement definition that is assigned to a
    * recurring block, that the number of created forecast tasks is based on the requirement
    * definition's minimum forecast range.
    *
    * Given a recurring requirement definition with minimum forecast range assigned to a block
    * definition. <br />
    * And a scheduling rule attached to the requirement definition.
    *
    * When create requirement task based on the requirement definition.
    *
    * Then verify the the forecasted requirement tasks are created.
    */
   @Test
   public void itForecastRequirement() throws Exception {
      double interval = 3;
      final int forecastRange = 20;

      // The forecast model and part number are needed for generate forecast requirement task
      final FcModelKey fcModelKey =
            Domain.createForecastModel( new DomainConfiguration<ForecastModel>() {

               @Override
               public void configure( ForecastModel forecastModel ) {
                  forecastModel.addRange( 1, 1, CYCLES, 1 );
               }
            } );

      final PartNoKey part = Domain.createPart();

      final AssemblyKey aircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly builder ) {
                  builder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot builder ) {
                        builder.addPartGroup( new DomainConfiguration<PartGroup>() {

                           @Override
                           public void configure( PartGroup builder ) {
                              builder.addPart( part );
                              builder.setInventoryClass( ACFT );

                           }

                        } );

                     }

                  } );

               }

            } );

      final InventoryKey aircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft builder ) {
            builder.setPart( part );
            builder.setAssembly( aircraftAssembly );
            builder.addUsage( CYCLES, BigDecimal.ZERO );
            builder.setForecastModel( fcModelKey );
         }

      } );

      final RecurringSchedulingRule recurringSchedulingRule =
            new RecurringSchedulingRule( CYCLES, new BigDecimal( interval ) );
      final TaskTaskKey requirementDefintion =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition builder ) {
                  builder.setStatus( RefTaskDefinitionStatusKey.ACTV );
                  builder.setScheduledFromEffectiveDate( DateUtils.addDays( new Date(), 1 ) );
                  builder.setMinimumForecastRange( forecastRange );
                  builder.addRecurringSchedulingRule( recurringSchedulingRule );
                  builder.setRecurring( true );
                  builder.againstConfigurationSlot(
                        Domain.readRootConfigurationSlot( aircraftAssembly ) );
               }

            } );

      Domain.createBlockDefinition( new DomainConfiguration<BlockDefinition>() {

         @Override
         public void configure( BlockDefinition builder ) {
            builder.setRecurring( true );
            builder.setStatus( RefTaskDefinitionStatusKey.ACTV );
            builder.addRequirementDefinition( requirementDefintion );
         }

      } );

      TaskBean taskBean = new TaskBean();

      TaskKey createdTask = taskBean.createFromTaskDefinition( null, aircraft, requirementDefintion,
            null, authorizingHr, null );

      assertEquals( "Forecasted requirement task is not generated correctly.",
            Math.ceil( forecastRange / interval ), getForcastedTasks( createdTask ), 0.0 );
   }


   /**
    * This test case is testing when a block task is created based on a block definition with work
    * types, it has the same work type as the definition.
    *
    * Given a block definition with work types.
    *
    * When create a block task based on this definition.
    *
    * Then verify it has the same work types as the block definition.
    */
   @Test
   public void itCreatesBlockTaskWithSameWorkTypesAsTheBlockDefinition() throws Exception {

      final PartNoKey part = Domain.createPart();

      final AssemblyKey aircraftAssembly = createAircraftAssemblyWithPart( part );

      final InventoryKey aircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft builder ) {
            builder.setPart( part );
            builder.setAssembly( aircraftAssembly );
         }

      } );

      final TaskTaskKey blockDefinition =
            Domain.createBlockDefinition( new DomainConfiguration<BlockDefinition>() {

               @Override
               public void configure( BlockDefinition builder ) {
                  builder.setConfigurationSlot(
                        Domain.readRootConfigurationSlot( aircraftAssembly ) );
                  builder.addWorkType( RefWorkTypeKey.TURN );
                  builder.addWorkType( HYDRAL );
                  builder.addWorkType( FUEL );
               }

            } );

      TaskKey blockTask = new TaskBean().createFromTaskDefinition( null, aircraft, blockDefinition,
            null, authorizingHr, null );

      List<RefWorkTypeKey> blockTaskWorkTypes = SchedStaskUtils.getWorkTypes( blockTask );

      assertEquals( "Block task has incorrect work type number", 3, blockTaskWorkTypes.size() );
      assertTrue( "Block task work types don't have expected work type " + RefWorkTypeKey.TURN,
            blockTaskWorkTypes.contains( RefWorkTypeKey.TURN ) );
      assertTrue( "Block task work types don't have expected work type " + HYDRAL,
            blockTaskWorkTypes.contains( HYDRAL ) );
      assertTrue( "Block task work types don't have expected work type " + FUEL,
            blockTaskWorkTypes.contains( FUEL ) );
   }


   /**
    * <pre>
    * This test case verifies that a task is created when we initialize an on-condition requirement definition.
    *
    * Given an aircraft assembly and an aircraft based on it
    * And an on-condition requirement definition exists against the aircraft assembly.
    *
    * When we initialize the requirement definition against the aircraft.
    *
    * Then the requirement gets initialized against that aircraft.
    *
    * </pre>
    */
   @Test
   public void itInitializesAnOnConditionRequirementAgainstAnAircraft() throws Exception {
      // Given

      final PartNoKey aircraftPart = Domain.createPart( new DomainConfiguration<Part>() {

         @Override
         public void configure( Part builder ) {
            builder.setInventoryClass( RefInvClassKey.ACFT );
         }
      } );

      final AssemblyKey aircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly builder ) {
                  builder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot builder ) {
                        builder.addPartGroup( new DomainConfiguration<PartGroup>() {

                           @Override
                           public void configure( PartGroup builder ) {
                              builder.addPart( aircraftPart );
                              builder.setInventoryClass( RefInvClassKey.ACFT );
                           }
                        } );
                     }
                  } );
               }
            } );

      final InventoryKey aircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft builder ) {
            builder.setAssembly( aircraftAssembly );
            builder.setPart( aircraftPart );

         }
      } );
      final TaskTaskKey reqDefinition =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition builder ) {
                  builder.againstConfigurationSlot(
                        Domain.readRootConfigurationSlot( aircraftAssembly ) );
                  builder.setOnCondition( true );
                  builder.setStatus( RefTaskDefinitionStatusKey.ACTV );

               }
            } );

      // When
      TaskKey actualRequirement = new TaskBean().createFromTaskDefinition( null, aircraft,
            reqDefinition, null, authorizingHr );

      // Then
      Assert.assertNotNull( "Unxpectedly, The requirement was not initialized", actualRequirement );

      TaskTaskKey actualReqDefinition =
            schedStaskDao.findByPrimaryKey( actualRequirement ).getTaskTaskKey();

      Assert.assertEquals( String.format(
            "Unxpectedly, the requirement wasn't initialized based on the correct requirement definition with key : %s",
            reqDefinition.toString() ), reqDefinition, actualReqDefinition );

   }


   /**
    * This test case is testing when create a requirement task based on a requirement definition
    * with work types, it has the same work types as the requirement definition.
    *
    * Given a requirement definition with work types.
    *
    * When create a requirement task based on this definition.
    *
    * Then verify it has the same work type as the requirement definition.
    *
    *
    */
   @Test
   public void itCreatesRequirementTaskWithSameWorkTypesAsTheRequirementDefinition()
         throws Exception {
      final PartNoKey part = Domain.createPart();

      final AssemblyKey aircraftAssembly = createAircraftAssemblyWithPart( part );

      final InventoryKey aircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft builder ) {
            builder.setPart( part );
            builder.setAssembly( aircraftAssembly );
         }

      } );

      TaskTaskKey requirementDefinition =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition builder ) {
                  builder.againstConfigurationSlot(
                        Domain.readRootConfigurationSlot( aircraftAssembly ) );
                  builder.addWorkType( RefWorkTypeKey.TURN );
                  builder.addWorkType( HYDRAL );
                  builder.addWorkType( FUEL );

               }

            } );

      TaskKey requirementTask = new TaskBean().createFromTaskDefinition( null, aircraft,
            requirementDefinition, null, authorizingHr, null );

      List<RefWorkTypeKey> requirementTaskWorkTypes =
            SchedStaskUtils.getWorkTypes( requirementTask );

      assertEquals( "Requirement task has incorrect work type number", 3,
            requirementTaskWorkTypes.size() );
      assertTrue(
            "Requirement task work types don't have expected work type " + RefWorkTypeKey.TURN,
            requirementTaskWorkTypes.contains( RefWorkTypeKey.TURN ) );
      assertTrue( "Requirement task work types don't have expected work type" + HYDRAL,
            requirementTaskWorkTypes.contains( HYDRAL ) );
      assertTrue( "Requirement task work types don't have expected work type " + FUEL,
            requirementTaskWorkTypes.contains( FUEL ) );

   }


   /**
    * This test case is verifying when remove a task from a work package,it will be removed.
    *
    * Given a work package with a child requirement
    *
    * When remove the requirement from the work package
    *
    * Then verify the next high event of the child requirement is null <br />
    * And the highest event will be itself.
    */
   @Test
   public void itRemovesChildTaskFromWorkPackage() throws Exception {
      final TaskKey childRequirement = Domain.createRequirement();
      final TaskKey workPackage = Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage builder ) {
            builder.addTask( childRequirement );

         }

      } );

      TaskBean taskBean = new TaskBean();

      taskBean.removeChildTask( workPackage, childRequirement, authorizingHr, null, null );

      EventKey childEventKey = new EventKey( childRequirement.getDbId(), childRequirement.getId() );

      EvtEventDao evtEventDao = InjectorContainer.get().getInstance( EvtEventDao.class );
      EventKey actualParentEventKey = evtEventDao.findByPrimaryKey( childEventKey ).getNhEvent();
      EventKey actualRootEventKey =
            evtEventDao.findByPrimaryKey( childEventKey ).getHEvent().getEventKey();

      assertNull( actualParentEventKey );
      assertEquals( childEventKey, actualRootEventKey );

   }


   /**
    * <pre>
    *
    * Given - an aircraft assembly with a root config-slot
    *       - and the root config-slot has a part group with a part
    *       - and an aircraft based on this assembly has a part
    *       - and a requirement definition against the root config-slot of the aircraft assembly
    *
    * When - a task is created against the aircraft based on the requirement definition
    *
    * Then - the created task class is REQ
    *
    * </pre>
    *
    */
   @Test
   public void itCreatesARequirementFromARequirementDefinition() throws Exception {

      // Given
      final PartNoKey aircraftPart = Domain.createPart();
      final AssemblyKey aircraftAssembly = createAircraftAssemblyWithPart( aircraftPart );
      final InventoryKey aircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aircraft ) {
            aircraft.setPart( aircraftPart );
            aircraft.setAssembly( aircraftAssembly );
         }

      } );

      final TaskTaskKey reqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition reqDefn ) {
                  reqDefn.againstConfigurationSlot(
                        Domain.readRootConfigurationSlot( aircraftAssembly ) );
               }
            } );

      // When
      final TaskKey task = new TaskBean().createFromTaskDefinition( null, aircraft, reqDefn, null,
            authorizingHr, null );

      // Then
      JdbcSchedStaskDao schedStaskDao =
            InjectorContainer.get().getInstance( JdbcSchedStaskDao.class );

      Assert.assertEquals( "Unexpected class of the created task.", RefTaskClassKey.REQ,
            schedStaskDao.findByPrimaryKey( task ).getTaskClass() );
   }


   /**
    * This test case is testing if the unique bool of a task definition is false, multiple active
    * actual tasks are allowed to be created
    *
    * <pre>
    *    Given an activated task definition
    *    And its unique bool is false
    *    When multiple active actual tasks are created
    *    Verify they are successfully created
    * </pre>
    */
   @Test
   public void itAllowsCreatingMultipleActiveTasksWhenTaskDefinitionUniqueBoolIsFalse()
         throws Exception {
      final PartNoKey partNo = Domain.createPart();
      final AssemblyKey aircraftAssembly = createAircraftAssemblyWithPart( partNo );
      final ConfigSlotKey rootConfigSlot = Domain.readRootConfigurationSlot( aircraftAssembly );
      TaskTaskKey taskDefinitionKey = Domain.createRequirementDefinition( taskDefn -> {
         taskDefn.setUnique( false );
         taskDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
         taskDefn.againstConfigurationSlot( rootConfigSlot );
      } );

      InventoryKey aircraftKey = Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( aircraftAssembly );
         aircraft.setPart( partNo );
      } );

      TaskBean taskBean = new TaskBean();

      TaskKey firstTaskKey = taskBean.createFromTaskDefinition( null, aircraftKey,
            taskDefinitionKey, null, authorizingHr, null );
      TaskKey secondTaskKey = taskBean.createFromTaskDefinition( null, aircraftKey,
            taskDefinitionKey, null, authorizingHr, null );

      assertFalse( "Duplicated task keys are created.", firstTaskKey.equals( secondTaskKey ) );
      assertTrue( "The first task is not created successfully.",
            schedStaskDao.findByPrimaryKey( firstTaskKey ).exists() );
      assertTrue( "The second task is not created successfully.",
            schedStaskDao.findByPrimaryKey( secondTaskKey ).exists() );
   }


   /**
    * This test case is testing if the unique bool of a task definition is true, if there is an
    * existing actual active task on this task definition, when user tries to create second one, a
    * duplicate task exception will be thrown.
    *
    * <pre>
    *    Given an activated task definition
    *    And its unique bool is true
    *    And existing actual active requirement task on the an inventory based on the definition
    *    When try to create actual second active requirement task based on the definition on the same inventory
    *    Then verify duplicate task exception is thrown during the creation.
    * </pre>
    */
   @Test( expected = DuplicateTaskException.class )
   public void itDoesNotAllowCreatingMultipleActiveTasksWhenTaskDefinitionUniqueBoolIsTrue()
         throws Exception {
      final PartNoKey partNo = Domain.createPart();
      final AssemblyKey aircraftAssembly = createAircraftAssemblyWithPart( partNo );
      final ConfigSlotKey rootConfigSlot = Domain.readRootConfigurationSlot( aircraftAssembly );
      TaskTaskKey taskDefinitionKey = Domain.createRequirementDefinition( taskDefn -> {
         taskDefn.setUnique( true );
         taskDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
         taskDefn.againstConfigurationSlot( rootConfigSlot );
      } );

      final InventoryKey aircraftKey = Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( aircraftAssembly );
         aircraft.setPart( partNo );
      } );

      Domain.createRequirement( requirement -> {
         requirement.setDefinition( taskDefinitionKey );
         requirement.setInventory( aircraftKey );
      } );

      TaskBean taskBean = new TaskBean();
      taskBean.setSessionContext( new SessionContextFake() );
      taskBean.createFromTaskDefinition( null, aircraftKey, taskDefinitionKey, null, authorizingHr,
            null );

   }


   /**
    * <pre>
    * GIVEN a task without deadline
    * WHEN validate if the task does not have a deadline
    * THEN the WarningNoDeadline should be set.
    * </pre>
    */
   @Test
   public void itGeneratesTaskNoDeadlineWarning() throws Exception {

      final TaskKey requirement = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement req ) {
            req.setStatus( ACTV );
         }
      } );

      // When
      TaskBean taskBean = new TaskBean();
      taskBean.setSessionContext( new SessionContextFake() );

      WarningNoDeadline warningNoDeadline = taskBean.validateNoDeadline( requirement );

      assertTrue( "A No Deadline warning shall be generated.", warningNoDeadline.isWarning() );

   }


   /**
    * <pre>
    * GIVEN a task without deadline assigned in a work package
    * WHEN unassigned the task
    * THEN the task no longer in the work package.
    * </pre>
    */
   @Test
   public void itUnassignedTaskWithNoDeadlineFromWorkPackage() throws Exception {

      final TaskKey requirement = Domain.createRequirement( req -> {
         req.setStatus( ACTV );
      } );

      InventoryKey aircraft = Domain.createAircraft();

      Domain.createWorkPackage( workPackage -> {
         workPackage.addTask( requirement );
         workPackage.setStatus( RefEventStatusKey.ACTV );
         workPackage.setAircraft( aircraft );
      } );

      // When
      TaskBean taskBean = new TaskBean();
      taskBean.setSessionContext( new SessionContextFake() );

      taskBean.unassignTask( requirement, authorizingHr, NA_REASON, NA_NOTE );

      SchedStaskDao schedStaskDao = InjectorContainer.get().getInstance( SchedStaskDao.class );
      TaskKey hTaskKey = schedStaskDao.findByPrimaryKey( requirement ).getHTaskKey();
      assertEquals( "Requirement shall be unassigned from work package", requirement, hTaskKey );
   }


   /**
    *
    * Verify that suppressed job cards are not added to the work scope of a work package when the
    * work scope is generated.
    *
    */
   @Test
   public void generatingWorkScopeIgnoresSuppressedJobCards() throws Exception {

      // Given a job card definition marked as being work scope enabled (i.e. tasks based on this
      // definition are candidates to be added to a work package's work scope).
      TaskTaskKey jobCardDefn = Domain.createJobCardDefinition( defn -> {
         defn.setWorkScopeEnabled( true );
      } );

      // Given suppressed job card tasks that are based on the same job card definition and are
      // against the same inventory.
      //
      // Note: a suppressed job card task is one with a suppressing job card,
      // but for this test that suppressing job card does not actually have to exist.
      InventoryKey inv = Domain.createAircraft();
      TaskKey suppressedJobCard1 = Domain.createJobCard( jic -> {
         jic.setSuppressingJobCard( new TaskKey( 0, 0 ) );
         jic.setDefinition( jobCardDefn );
         jic.setInventory( inv );
      } );
      TaskKey suppressedJobCard2 = Domain.createJobCard( jic -> {
         jic.setSuppressingJobCard( new TaskKey( 0, 0 ) );
         jic.setDefinition( jobCardDefn );
         jic.setInventory( inv );
      } );

      // Given non-suppressed job card tasks that are based on the same job card definition and are
      // against the same inventory.
      TaskKey nonSuppressedJobCard1 = Domain.createJobCard( jic -> {
         jic.setDefinition( jobCardDefn );
         jic.setInventory( inv );
      } );
      TaskKey nonSuppressedJobCard2 = Domain.createJobCard( jic -> {
         jic.setDefinition( jobCardDefn );
         jic.setInventory( inv );
      } );

      // Given a REQ class requirement task containing all the job card tasks.
      TaskKey req1 = Domain.createRequirement( req -> {
         req.setTaskClass( REQ );
         req.addJobCard( suppressedJobCard1 );
         req.addJobCard( suppressedJobCard2 );
         req.addJobCard( nonSuppressedJobCard1 );
         req.addJobCard( nonSuppressedJobCard2 );
      } );

      // Given a work package with the requirement task assigned to it.
      TaskKey workPackageKey = Domain.createWorkPackage( wp -> {
         wp.addTask( req1 );
      } );

      // When the work scope of the work package is generated.
      TaskBean taskBean = new TaskBean();

      taskBean.generateWorkscope( workPackageKey, Domain.createHumanResource() );

      // Then the suppressed job cards are not added to the work scope of the work package.
      // However, the work scope does contain the non-suppressed job cards.
      List<TaskKey> workScopeJobCards = getTasksInWorkScope( workPackageKey );
      assertThat( "Unexpected number of job cards in work scope.", workScopeJobCards.size(),
            is( 2 ) );
      assertThat( "Work scope does not contain the first non-suppressed job card.",
            workScopeJobCards, hasItem( nonSuppressedJobCard1 ) );
      assertThat( "Work scope does not contain the second non-suppressed job card.",
            workScopeJobCards, hasItem( nonSuppressedJobCard2 ) );
   }


   /**
    *
    * Verify that a duplicated job card task is not added to the work scope of a committed work
    * package when the job card's parent requirement task is added to the work package and the
    * requirement has a class of REQ.
    *
    *
    * The term "duplicated job card task" means there is already a job card task within the work
    * scope of the work package with the same definition as the job card task being added.
    *
    */
   @Test
   public void duplicatedJicNotAddedToWorkScopeOfWorkPackageWhenParentIsReqRequirement()
         throws Exception {

      // Given a job card definition marked as being work scope enabled (i.e. tasks based on this
      // definition are candidates to be added to a work package's work scope).
      TaskTaskKey jobCardDefn = Domain.createJobCardDefinition( defn -> {
         defn.setWorkScopeEnabled( true );
      } );

      // Given job card tasks that are based on the same job card definition and are
      // against the same inventory.
      //
      // Note: a suppressed job card task is one with a suppressing job card,
      // but for this test that suppressing job card does not actually have to exist.
      InventoryKey inv = Domain.createAircraft();
      TaskKey jobCard1 = Domain.createJobCard( jic -> {
         jic.setDefinition( jobCardDefn );
         jic.setInventory( inv );
      } );
      TaskKey jobCard2 = Domain.createJobCard( jic -> {
         jic.setDefinition( jobCardDefn );
         jic.setInventory( inv );
      } );

      // Given a REQ class requirement tasks, each containing a job card task.
      TaskKey req1 = Domain.createRequirement( req -> {
         req.setTaskClass( REQ );
         req.addJobCard( jobCard1 );
      } );
      TaskKey req2 = Domain.createRequirement( req -> {
         req.setTaskClass( REQ );
         req.addJobCard( jobCard2 );
      } );

      // Given a committed work package with one requirement task assigned to it and the job card
      // associated with that requirement belongs to the work scope of the work package.
      TaskKey workPackageKey = Domain.createWorkPackage( wp -> {
         wp.setStatus( COMMIT );
         wp.addTask( req1 );
         wp.addWorkScopeTask( jobCard1 );
      } );

      // When the requirement task, with its duplicated job card task, is added to the work package.
      TaskBean taskBean = new TaskBean();

      taskBean.addChildTask( workPackageKey, req2, Domain.createHumanResource(), NA_REASON,
            NA_NOTE );

      // Then the duplicated job card task is not added to the work scope of the work package.
      // And the originally added job card task remains in the work scope.
      List<TaskKey> workScopeJobCards = getTasksInWorkScope( workPackageKey );
      assertThat( "Unexpected number of job cards in work scope.", workScopeJobCards.size(),
            is( 1 ) );
      assertThat( "Work scope does not contain the origianl job card.", workScopeJobCards,
            hasItem( jobCard1 ) );

   }


   /**
    *
    * Verify that a duplicated job card task is added to the work scope of a committed work package
    * when the job card's parent requirement task is added to the work package and the requirement
    * has a class of FOLLOW.
    *
    *
    * The term "duplicated job card task" means there is already a job card task within the work
    * scope of the work package with the same definition as the job card task being added.
    *
    */
   @Test
   public void duplicatedJicAddedToWorkScopeOfWorkPackageWhenParentIsFollowRequirement()
         throws Exception {

      // Given a job card definition marked as being work scope enabled (i.e. tasks based on this
      // definition are candidates to be added to a work package's work scope).
      TaskTaskKey jobCardDefn = Domain.createJobCardDefinition( defn -> {
         defn.setWorkScopeEnabled( true );
      } );

      // Given job card tasks that are based on the same job card definition and are
      // against the same inventory.
      //
      // Note: a suppressed job card task is one with a suppressing job card,
      // but for this test that suppressing job card does not actually have to exist.
      InventoryKey inv = Domain.createAircraft();
      TaskKey jobCard1 = Domain.createJobCard( jic -> {
         jic.setDefinition( jobCardDefn );
         jic.setInventory( inv );
      } );
      TaskKey jobCard2 = Domain.createJobCard( jic -> {
         jic.setDefinition( jobCardDefn );
         jic.setInventory( inv );
      } );

      // Given a REQ class requirement tasks, each containing a job card task.
      TaskKey req1 = Domain.createRequirement( req -> {
         req.setTaskClass( FOLLOW );
         req.addJobCard( jobCard1 );
      } );
      TaskKey req2 = Domain.createRequirement( req -> {
         req.setTaskClass( FOLLOW );
         req.addJobCard( jobCard2 );
      } );

      // Given a committed work package with one requirement task assigned to it and the job card
      // associated with that requirement belongs to the work scope of the work package.
      TaskKey workPackageKey = Domain.createWorkPackage( wp -> {
         wp.addTask( req1 );
         wp.addWorkScopeTask( jobCard1 );
      } );

      // When the requirement task, with its duplicated job card task, is added to the work package.
      TaskBean taskBean = new TaskBean();

      taskBean.addChildTask( workPackageKey, req2, Domain.createHumanResource(), NA_REASON,
            NA_NOTE );

      // Then the duplicated job card task is added to the work scope of the work package.
      // And the originally added job card task remains in the work scope.
      List<TaskKey> workScopeJobCards = getTasksInWorkScope( workPackageKey );
      assertThat( "Unexpected number of job cards in work scope.", workScopeJobCards.size(),
            is( 2 ) );
      assertThat( "Work scope does not contain the origianl job card.", workScopeJobCards,
            hasItem( jobCard1 ) );
      assertThat( "Work scope does not contain the added job card.", workScopeJobCards,
            hasItem( jobCard2 ) );
   }


   /**
    * This test case is testing when a off parent requirement is created on component, a component
    * work package will be created automatically.
    *
    * <pre>
    * Given an aircraft assembly with sub config slot.
    * And an aircraft based on this assembly.
    * When create a off parent requirement on the config slot.
    * Then a component work package is created as well.
    * </pre>
    */
   @Test
   public void itCreatesReqWithinComponentWorkPackageWhenOffParentSelected() throws Exception {

      String trackedConfigSlotCode = "tracked_configslot";
      String assemblyCd = "ASSEMBLY";
      String bomPartCd = "BOM";

      PartNoKey part = Domain.createPart();

      AssemblyKey aircraftAssemblyKey = Domain.createAircraftAssembly( aircraftAssembly -> {
         aircraftAssembly.setCode( assemblyCd );
         aircraftAssembly.setRootConfigurationSlot( rootConfigSlot -> {
            rootConfigSlot.addConfigurationSlot( childConfigSlot -> {
               childConfigSlot.setConfigurationSlotClass( RefBOMClassKey.SYS );
               childConfigSlot.setCode( trackedConfigSlotCode );
               childConfigSlot.addPartGroup( partGroup -> {
                  partGroup.addPart( part );
                  partGroup.setInventoryClass( RefInvClassKey.TRK );
                  partGroup.setCode( bomPartCd );
               } );
            } );
         } );
      } );

      PartGroupKey partGroup = EqpBomPart.getBomPartKey( aircraftAssemblyKey, bomPartCd );

      InventoryKey aircraftKey = Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( aircraftAssemblyKey );
      } );

      ConfigSlotKey subConfigSlot = ConfigurationSlotReader.readSubConfigurationSlot(
            Domain.readRootConfigurationSlot( aircraftAssemblyKey ), trackedConfigSlotCode );

      InventoryKey trackedInventoryKey = Domain.createTrackedInventory( trackedInventory -> {
         trackedInventory.setParent( aircraftKey );
         trackedInventory.setPartNumber( part );
         trackedInventory.setPartGroup( partGroup );
         trackedInventory.setPosition( new ConfigSlotPositionKey( subConfigSlot, 1 ) );
      } );

      TaskTaskKey requirementDefnKey = Domain.createRequirementDefinition( requirementDefn -> {
         requirementDefn.setMustRemove( RefTaskMustRemoveKey.OFFPARENT );
         requirementDefn.setTaskClass( RefTaskClassKey.REQ );
         requirementDefn.setScheduledFromEffectiveDate( new Date() );
         requirementDefn.againstConfigurationSlot( subConfigSlot );
      } );

      TaskBean taskBean = new TaskBean();
      taskBean.setSessionContext( new SessionContextFake() );
      TaskKey requirement = taskBean.createFromTaskDefinition( null, trackedInventoryKey,
            requirementDefnKey, null, authorizingHr, null );

      assertNotNull( "Component work package is not generated.",
            SchedStaskUtils.getCheck( requirement ) );

   }


   /**
    * This test case is testing when a requirement with job cards which is not enforcing work order
    * is created, the actual job cards are created and order is not set.
    *
    * <pre>
    *    Given a requirement definition with job cards definitions
    *    And it does not enforce work order.
    *    When create actual requirement.
    *    Then actual job cards are created.
    *    And the work order is null.
    * </pre>
    */
   @Test
   public void itCreatesReqWithJICsWithoutOrders() throws Exception {

      String trackedConfigSlotCode = "tracked_configslot";
      String assemblyCd = "ASSEMBLY";

      PartNoKey part = Domain.createPart();

      AssemblyKey aircraftAssemblyKey = Domain.createAircraftAssembly( aircraftAssembly -> {
         aircraftAssembly.setCode( assemblyCd );
         aircraftAssembly.setRootConfigurationSlot( rootConfigSlot -> {
            rootConfigSlot.addConfigurationSlot( childConfigSlot -> {
               childConfigSlot.setConfigurationSlotClass( RefBOMClassKey.SYS );
               childConfigSlot.setCode( trackedConfigSlotCode );
               childConfigSlot.addPartGroup( partGroup -> {
                  partGroup.addPart( part );
                  partGroup.setInventoryClass( RefInvClassKey.TRK );
               } );
            } );
         } );
      } );

      InventoryKey aircraftKey = Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( aircraftAssemblyKey );
      } );

      ConfigSlotKey subConfigSlot = ConfigurationSlotReader.readSubConfigurationSlot(
            Domain.readRootConfigurationSlot( aircraftAssemblyKey ), trackedConfigSlotCode );

      InventoryKey trackedInventoryKey = Domain.createTrackedInventory( trackedInventory -> {
         trackedInventory.setParent( aircraftKey );
         trackedInventory.setPartNumber( part );
         trackedInventory.setPosition( new ConfigSlotPositionKey( subConfigSlot, 1 ) );
      } );

      TaskTaskKey jicDefinition1 = Domain.createJobCardDefinition( jicDefn -> {
         jicDefn.setCode( "JIC1" );
         jicDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
         jicDefn.setConfigurationSlot( subConfigSlot );
      } );
      TaskTaskKey jicDefinition2 = Domain.createJobCardDefinition( jicDefn -> {
         jicDefn.setCode( "JIC2" );
         jicDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
         jicDefn.setConfigurationSlot( subConfigSlot );
      } );

      TaskTaskKey requirementDefnKey = Domain.createRequirementDefinition( requirementDefn -> {
         requirementDefn.addJobCardDefinition( jicDefinition1 );
         requirementDefn.addJobCardDefinition( jicDefinition2 );
         requirementDefn.setScheduledFromEffectiveDate( new Date() );
         requirementDefn.againstConfigurationSlot( subConfigSlot );
         requirementDefn.setOnCondition( true );
      } );

      TaskBean taskBean = new TaskBean();
      taskBean.setSessionContext( new SessionContextFake() );
      taskBean.createFromTaskDefinition( null, trackedInventoryKey, requirementDefnKey, null,
            authorizingHr, null );

      TaskKey jic1ActualTask =
            SchedStaskUtil.findLastCreatedTaskForTaskDefinition( jicDefinition1 );
      TaskKey jic2ActualTask =
            SchedStaskUtil.findLastCreatedTaskForTaskDefinition( jicDefinition2 );

      assertNotNull( "Job card is not created.", jic1ActualTask );
      assertNotNull( "Job card is not created.", jic2ActualTask );

      int jicTableOneOrder =
            TaskJicReqMapTable
                  .findByPrimaryKey( new TaskJicReqMapKey( jicDefinition1,
                        TaskTaskTable.findByPrimaryKey( requirementDefnKey ).getTaskDefn() ) )
                  .getOrder();
      int jicTableTwoOrder =
            TaskJicReqMapTable
                  .findByPrimaryKey( new TaskJicReqMapKey( jicDefinition2,
                        TaskTaskTable.findByPrimaryKey( requirementDefnKey ).getTaskDefn() ) )
                  .getOrder();

      // Our getInt method turns null to 0
      assertEquals( "Job card work order is not set properly.", 0, jicTableOneOrder );
      assertEquals( "Job card work order is not set properly.", 0, jicTableTwoOrder );

   }


   /**
    * <pre>
    * GIVEN an assembly with a TRK config slot (child) which has a parent TRK config slot
    * AND an on-condition, off-wing ACTV requirement definition has been defined against the child config slot
    * WHEN the requirement is initialized against a child component attached to its parent
    * THEN the requirement is created against the child component
    * </pre>
    */
   @Test
   public void itInitializesOffWingRequirementAgainstTrkConfigSlot() throws Exception {

      PartNoKey childPartKey = Domain.createPart( trkPart -> {
         trkPart.setInventoryClass( RefInvClassKey.TRK );
      } );

      AssemblyKey aircraftAssemblyKey = Domain.createAircraftAssembly( aircraftAssembly -> {
         aircraftAssembly
               .setRootConfigurationSlot( rootCs -> rootCs.addConfigurationSlot( parentTrkCs -> {
                  parentTrkCs.setCode( PARENT_TRK_CONFIG_SLOT );
                  parentTrkCs.setConfigurationSlotClass( RefBOMClassKey.TRK );
                  parentTrkCs.addConfigurationSlot( childTrkCs -> {
                     childTrkCs.setCode( CHILD_TRK_CONFIG_SLOT );
                     childTrkCs.setConfigurationSlotClass( RefBOMClassKey.TRK );
                     childTrkCs.addPartGroup( partGroup -> {
                        partGroup.setInventoryClass( RefInvClassKey.TRK );
                        partGroup.setCode( CHILD_TRK_PART_GROUP );
                        partGroup.addPart( childPartKey );
                     } );
                  } );
               } ) );
         aircraftAssembly.setCode( ASSEMBLY_CODE );
      } );

      ConfigSlotKey rootCs = Domain.readRootConfigurationSlot( aircraftAssemblyKey );
      ConfigSlotKey parentTrkCs = Domain.readSubConfigurationSlot( rootCs, PARENT_TRK_CONFIG_SLOT );
      ConfigSlotKey childTrkCs =
            Domain.readSubConfigurationSlot( parentTrkCs, CHILD_TRK_CONFIG_SLOT );

      TaskTaskKey reqDefnKey = Domain.createRequirementDefinition( reqDefn -> {
         reqDefn.setOnCondition( true );
         reqDefn.setTaskClass( RefTaskClassKey.REQ );
         reqDefn.setMustRemove( RefTaskMustRemoveKey.OFFWING );
         reqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
         reqDefn.againstConfigurationSlot( childTrkCs );
      } );

      InventoryKey aircraftKey =
            Domain.createAircraft( aircraft -> aircraft.setAssembly( aircraftAssemblyKey ) );

      ConfigSlotPositionKey parentComponentPosition = new ConfigSlotPositionKey( parentTrkCs, 1 );
      ConfigSlotPositionKey childComponentPosition = new ConfigSlotPositionKey( childTrkCs, 1 );

      InventoryKey parentComponent = Domain.createTrackedInventory( trk -> {
         trk.setLastKnownConfigSlot( ASSEMBLY_CODE, PARENT_TRK_CONFIG_SLOT, "1" );
         trk.setParent( aircraftKey );
         trk.setPosition( parentComponentPosition );
      } );

      PartGroupKey childPartGroupKey = Domain.readPartGroup( childTrkCs, CHILD_TRK_PART_GROUP );

      InventoryKey childComponent = Domain.createTrackedInventory( trk -> {
         trk.setLastKnownConfigSlot( ASSEMBLY_CODE, CHILD_TRK_CONFIG_SLOT, "1" );
         trk.setParent( parentComponent );
         trk.setPartNumber( childPartKey );
         trk.setPartGroup( childPartGroupKey );
         trk.setPosition( childComponentPosition );
      } );

      TaskBean taskBean = new TaskBean();
      taskBean.setSessionContext( new SessionContextFake() );
      taskBean.createFromTaskDefinition( null, childComponent, reqDefnKey, null, authorizingHr,
            null );

      DataSetArgument args = new DataSetArgument();
      args.add( RefTaskClassKey.REQ, SchedStaskDao.ColumnName.TASK_CLASS_DB_ID.toString(),
            SchedStaskDao.ColumnName.TASK_CLASS_CD.toString() );
      args.add( reqDefnKey, TaskTaskKey.TASK_DB_ID_COL, TaskTaskKey.TASK_ID_COL );
      args.add( childComponent, "main_inv_no_db_id", "main_inv_no_id" );
      QuerySet qs = QuerySetFactory.getInstance().executeQueryTable( "sched_stask", args );
      assertEquals( "Unexpected number of requirement definitions retrieved for the created task",
            1, qs.getRowCount() );
   }


   /**
    * <pre>
    * GIVEN an assembly with a TRK config slot (child) which has a parent TRK config slot
    * AND the child config slot part group is configured as LRU (Line Replaceable Unit)
    * AND the part number assigned to the part group is configured as repairable
    * AND an on-condition, off-wing ACTV requirement definition has been defined against the child config slot
    * WHEN the requirement is initialized against a child component attached to its parent
    * THEN a component work package is created against the child component
    * </pre>
    */
   @Test
   public void itInitializesComponentWorkPackageForOffWingRequirementAgainstTrkConfigSlot()
         throws Exception {

      PartNoKey childPartKey = Domain.createPart( trkPart -> {
         trkPart.setInventoryClass( RefInvClassKey.TRK );
         trkPart.setRepairable( true );
      } );

      AssemblyKey aircraftAssemblyKey = Domain.createAircraftAssembly( aircraftAssembly -> {
         aircraftAssembly
               .setRootConfigurationSlot( rootCs -> rootCs.addConfigurationSlot( parentTrkCs -> {
                  parentTrkCs.setCode( PARENT_TRK_CONFIG_SLOT );
                  parentTrkCs.setConfigurationSlotClass( RefBOMClassKey.TRK );
                  parentTrkCs.addConfigurationSlot( childTrkCs -> {
                     childTrkCs.setCode( CHILD_TRK_CONFIG_SLOT );
                     childTrkCs.setConfigurationSlotClass( RefBOMClassKey.TRK );
                     childTrkCs.addPartGroup( partGroup -> {
                        partGroup.setInventoryClass( RefInvClassKey.TRK );
                        partGroup.setCode( CHILD_TRK_PART_GROUP );
                        partGroup.setLineReplaceableUnit( true );
                        partGroup.addPart( childPartKey );
                     } );
                  } );
               } ) );
         aircraftAssembly.setCode( ASSEMBLY_CODE );
      } );

      ConfigSlotKey rootCs = Domain.readRootConfigurationSlot( aircraftAssemblyKey );
      ConfigSlotKey parentTrkCs = Domain.readSubConfigurationSlot( rootCs, PARENT_TRK_CONFIG_SLOT );
      ConfigSlotKey childTrkCs =
            Domain.readSubConfigurationSlot( parentTrkCs, CHILD_TRK_CONFIG_SLOT );

      TaskTaskKey reqDefnKey = Domain.createRequirementDefinition( reqDefn -> {
         reqDefn.setOnCondition( true );
         reqDefn.setTaskClass( RefTaskClassKey.REQ );
         reqDefn.setMustRemove( RefTaskMustRemoveKey.OFFWING );
         reqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
         reqDefn.againstConfigurationSlot( childTrkCs );
      } );

      InventoryKey aircraftKey =
            Domain.createAircraft( aircraft -> aircraft.setAssembly( aircraftAssemblyKey ) );

      ConfigSlotPositionKey parentComponentPosition = new ConfigSlotPositionKey( parentTrkCs, 1 );
      ConfigSlotPositionKey childComponentPosition = new ConfigSlotPositionKey( childTrkCs, 1 );

      InventoryKey parentComponent = Domain.createTrackedInventory( trk -> {
         trk.setLastKnownConfigSlot( ASSEMBLY_CODE, PARENT_TRK_CONFIG_SLOT, "1" );
         trk.setParent( aircraftKey );
         trk.setPosition( parentComponentPosition );
      } );

      PartGroupKey childPartGroupKey = Domain.readPartGroup( childTrkCs, CHILD_TRK_PART_GROUP );

      InventoryKey childComponent = Domain.createTrackedInventory( trk -> {
         trk.setLastKnownConfigSlot( ASSEMBLY_CODE, CHILD_TRK_CONFIG_SLOT, "1" );
         trk.setParent( parentComponent );
         trk.setPartNumber( childPartKey );
         trk.setPartGroup( childPartGroupKey );
         trk.setPosition( childComponentPosition );
      } );

      TaskBean taskBean = new TaskBean();
      taskBean.setSessionContext( new SessionContextFake() );
      taskBean.createFromTaskDefinition( null, childComponent, reqDefnKey, null, authorizingHr,
            null );

      DataSetArgument args = new DataSetArgument();
      args.add( RefTaskClassKey.RO, SchedStaskDao.ColumnName.TASK_CLASS_DB_ID.toString(),
            SchedStaskDao.ColumnName.TASK_CLASS_CD.toString() );
      args.add( childComponent, "main_inv_no_db_id", "main_inv_no_id" );
      QuerySet qs = QuerySetFactory.getInstance().executeQueryTable( "sched_stask", args );
      assertEquals( "Unexpected number of component work package returned", 1, qs.getRowCount() );
   }


   /**
    * <pre>
    * GIVEN an assembly with a TRK config slot (child) which has a parent TRK config slot
    * AND the child config slot part group is configured as LRU (Line Replaceable Unit)
    * AND the part number assigned to the part group is configured as repairable
    * AND an on-condition, off-wing ACTV requirement definition has been defined against the child config slot
    * WHEN the requirement is initialized against a child component attached to its parent
    * THEN the requirement is assigned to the component work package created against the child component
    * </pre>
    */
   @Test
   public void
         itAssignsNewlyInitializedRequirementToComponentWorkPackageForOffWingRequirementAgainstTrkConfigSlot()
               throws Exception {

      PartNoKey childPartKey = Domain.createPart( trkPart -> {
         trkPart.setInventoryClass( RefInvClassKey.TRK );
         trkPart.setRepairable( true );
      } );

      AssemblyKey aircraftAssemblyKey = Domain.createAircraftAssembly( aircraftAssembly -> {
         aircraftAssembly
               .setRootConfigurationSlot( rootCs -> rootCs.addConfigurationSlot( parentTrkCs -> {
                  parentTrkCs.setCode( PARENT_TRK_CONFIG_SLOT );
                  parentTrkCs.setConfigurationSlotClass( RefBOMClassKey.TRK );
                  parentTrkCs.addConfigurationSlot( childTrkCs -> {
                     childTrkCs.setCode( CHILD_TRK_CONFIG_SLOT );
                     childTrkCs.setConfigurationSlotClass( RefBOMClassKey.TRK );
                     childTrkCs.addPartGroup( partGroup -> {
                        partGroup.setInventoryClass( RefInvClassKey.TRK );
                        partGroup.setCode( CHILD_TRK_PART_GROUP );
                        partGroup.setLineReplaceableUnit( true );
                        partGroup.addPart( childPartKey );
                     } );
                  } );
               } ) );
         aircraftAssembly.setCode( ASSEMBLY_CODE );
      } );

      ConfigSlotKey rootCs = Domain.readRootConfigurationSlot( aircraftAssemblyKey );
      ConfigSlotKey parentTrkCs = Domain.readSubConfigurationSlot( rootCs, PARENT_TRK_CONFIG_SLOT );
      ConfigSlotKey childTrkCs =
            Domain.readSubConfigurationSlot( parentTrkCs, CHILD_TRK_CONFIG_SLOT );

      TaskTaskKey reqDefnKey = Domain.createRequirementDefinition( reqDefn -> {
         reqDefn.setOnCondition( true );
         reqDefn.setTaskClass( RefTaskClassKey.REQ );
         reqDefn.setMustRemove( RefTaskMustRemoveKey.OFFWING );
         reqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
         reqDefn.againstConfigurationSlot( childTrkCs );
      } );

      InventoryKey aircraftKey =
            Domain.createAircraft( aircraft -> aircraft.setAssembly( aircraftAssemblyKey ) );

      ConfigSlotPositionKey parentComponentPosition = new ConfigSlotPositionKey( parentTrkCs, 1 );
      ConfigSlotPositionKey childComponentPosition = new ConfigSlotPositionKey( childTrkCs, 1 );

      InventoryKey parentComponent = Domain.createTrackedInventory( trk -> {
         trk.setLastKnownConfigSlot( ASSEMBLY_CODE, PARENT_TRK_CONFIG_SLOT, "1" );
         trk.setParent( aircraftKey );
         trk.setPosition( parentComponentPosition );
      } );

      PartGroupKey childPartGroupKey = Domain.readPartGroup( childTrkCs, CHILD_TRK_PART_GROUP );

      InventoryKey childComponent = Domain.createTrackedInventory( trk -> {
         trk.setLastKnownConfigSlot( ASSEMBLY_CODE, CHILD_TRK_CONFIG_SLOT, "1" );
         trk.setParent( parentComponent );
         trk.setPartNumber( childPartKey );
         trk.setPartGroup( childPartGroupKey );
         trk.setPosition( childComponentPosition );
      } );

      TaskBean taskBean = new TaskBean();
      taskBean.setSessionContext( new SessionContextFake() );
      TaskKey newlyCreatedRequirement = taskBean.createFromTaskDefinition( null, childComponent,
            reqDefnKey, null, authorizingHr, null );

      // Get the newly created requirements highest task
      EvtEventDao evtEventDao = InjectorContainer.get().getInstance( EvtEventDao.class );
      EvtEventTable eventTable = evtEventDao.findByPrimaryKey( newlyCreatedRequirement );
      EventKey actualHighestTaskForInitializedReq = eventTable.getHEvent();

      assertNotNull( "Unexpectedtly, the component work package was not created",
            actualHighestTaskForInitializedReq );

      SchedStaskDao schedStaskDao = InjectorContainer.get().getInstance( SchedStaskDao.class );
      RefTaskClassKey workPackageType = schedStaskDao
            .findByPrimaryKey( new TaskKey( actualHighestTaskForInitializedReq ) ).getTaskClass();

      assertEquals(
            "Unexpectedly, the newly initialized requirement wasn't assigned to the newly created component work package",
            RefTaskClassKey.RO, workPackageType );
   }


   /**
    * <pre>
    * GIVEN an assembly with a TRK config slot (child) which has a parent TRK config slot
    * AND the child config slot part group is configured as LRU (Line Replaceable Unit)
    * AND the part number assigned to the part group is configured as repairable
    * AND an on-condition, off-wing ACTV requirement definition has been defined against the child config slot
    * WHEN the requirement is initialized against a child component attached to its parent
    * THEN an ADHOC requirement is created against the parent component
    * </pre>
    */
   @Test
   public void
         itCreatesAdhocRequirementForParentTrkWhenOffWingRequirementAgainstChildTrkConfigSlot()
               throws Exception {

      PartNoKey childPartKey = Domain.createPart( trkPart -> {
         trkPart.setInventoryClass( RefInvClassKey.TRK );
         trkPart.setRepairable( true );
      } );

      AssemblyKey aircraftAssemblyKey = Domain.createAircraftAssembly( aircraftAssembly -> {
         aircraftAssembly
               .setRootConfigurationSlot( rootCs -> rootCs.addConfigurationSlot( parentTrkCs -> {
                  parentTrkCs.setCode( PARENT_TRK_CONFIG_SLOT );
                  parentTrkCs.setConfigurationSlotClass( RefBOMClassKey.TRK );
                  parentTrkCs.addConfigurationSlot( childTrkCs -> {
                     childTrkCs.setCode( CHILD_TRK_CONFIG_SLOT );
                     childTrkCs.setConfigurationSlotClass( RefBOMClassKey.TRK );
                     childTrkCs.addPartGroup( partGroup -> {
                        partGroup.setInventoryClass( RefInvClassKey.TRK );
                        partGroup.setCode( CHILD_TRK_PART_GROUP );
                        partGroup.setLineReplaceableUnit( true );
                        partGroup.addPart( childPartKey );
                     } );
                  } );
               } ) );
         aircraftAssembly.setCode( ASSEMBLY_CODE );
      } );

      ConfigSlotKey rootCs = Domain.readRootConfigurationSlot( aircraftAssemblyKey );
      ConfigSlotKey parentTrkCs = Domain.readSubConfigurationSlot( rootCs, PARENT_TRK_CONFIG_SLOT );
      ConfigSlotKey childTrkCs =
            Domain.readSubConfigurationSlot( parentTrkCs, CHILD_TRK_CONFIG_SLOT );

      TaskTaskKey reqDefnKey = Domain.createRequirementDefinition( reqDefn -> {
         reqDefn.setOnCondition( true );
         reqDefn.setTaskClass( RefTaskClassKey.REQ );
         reqDefn.setMustRemove( RefTaskMustRemoveKey.OFFWING );
         reqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
         reqDefn.againstConfigurationSlot( childTrkCs );
      } );

      InventoryKey aircraftKey =
            Domain.createAircraft( aircraft -> aircraft.setAssembly( aircraftAssemblyKey ) );

      ConfigSlotPositionKey parentComponentPosition = new ConfigSlotPositionKey( parentTrkCs, 1 );
      ConfigSlotPositionKey childComponentPosition = new ConfigSlotPositionKey( childTrkCs, 1 );

      InventoryKey parentComponent = Domain.createTrackedInventory( trk -> {
         trk.setLastKnownConfigSlot( ASSEMBLY_CODE, PARENT_TRK_CONFIG_SLOT, "1" );
         trk.setParent( aircraftKey );
         trk.setPosition( parentComponentPosition );
      } );

      PartGroupKey childPartGroupKey = Domain.readPartGroup( childTrkCs, CHILD_TRK_PART_GROUP );

      InventoryKey childComponent = Domain.createTrackedInventory( trk -> {
         trk.setLastKnownConfigSlot( ASSEMBLY_CODE, CHILD_TRK_CONFIG_SLOT, "1" );
         trk.setParent( parentComponent );
         trk.setPartNumber( childPartKey );
         trk.setPartGroup( childPartGroupKey );
         trk.setPosition( childComponentPosition );
      } );

      TaskBean taskBean = new TaskBean();
      taskBean.setSessionContext( new SessionContextFake() );
      taskBean.createFromTaskDefinition( null, childComponent, reqDefnKey, null, authorizingHr,
            null );

      DataSetArgument args = new DataSetArgument();
      args.add( RefTaskClassKey.ADHOC, SchedStaskDao.ColumnName.TASK_CLASS_DB_ID.toString(),
            SchedStaskDao.ColumnName.TASK_CLASS_CD.toString() );
      args.add( parentComponent, "main_inv_no_db_id", "main_inv_no_id" );
      QuerySet qs = QuerySetFactory.getInstance().executeQueryTable( "sched_stask", args );
      assertEquals( "Unexpected number of inventories returned", 1, qs.getRowCount() );
   }


   /**
    * <pre>
    * GIVEN an assembly with a TRK config slot (child) which has a parent TRK config slot
    * AND the child config slot part group is configured as LRU (Line Replaceable Unit)
    * AND the part number assigned to the part group is configured as repairable
    * AND an on-condition, off-wing ACTV requirement definition has been defined against the child config slot
    * WHEN the requirement is initialized against a child component attached to its parent
    * THEN an ADHOC requirement is created against the parent component
    * </pre>
    */
   @Test
   public void
         itCreatesAdhocRequirementForParentTrkWithSameDeadlineAsReqDefnWhenOffWingRequirementAgainstChildTrkConfigSlot()
               throws Exception {

      PartNoKey childPartKey = Domain.createPart( trkPart -> {
         trkPart.setInventoryClass( RefInvClassKey.TRK );
         trkPart.setRepairable( true );
      } );

      AssemblyKey aircraftAssemblyKey = Domain.createAircraftAssembly( aircraftAssembly -> {
         aircraftAssembly
               .setRootConfigurationSlot( rootCs -> rootCs.addConfigurationSlot( parentTrkCs -> {
                  parentTrkCs.setCode( PARENT_TRK_CONFIG_SLOT );
                  parentTrkCs.setConfigurationSlotClass( RefBOMClassKey.TRK );
                  parentTrkCs.addConfigurationSlot( childTrkCs -> {
                     childTrkCs.setCode( CHILD_TRK_CONFIG_SLOT );
                     childTrkCs.setConfigurationSlotClass( RefBOMClassKey.TRK );
                     childTrkCs.addPartGroup( partGroup -> {
                        partGroup.setInventoryClass( RefInvClassKey.TRK );
                        partGroup.setCode( CHILD_TRK_PART_GROUP );
                        partGroup.setLineReplaceableUnit( true );
                        partGroup.addPart( childPartKey );
                     } );
                  } );
               } ) );
         aircraftAssembly.setCode( ASSEMBLY_CODE );
      } );

      ConfigSlotKey rootCs = Domain.readRootConfigurationSlot( aircraftAssemblyKey );
      ConfigSlotKey parentTrkCs = Domain.readSubConfigurationSlot( rootCs, PARENT_TRK_CONFIG_SLOT );
      ConfigSlotKey childTrkCs =
            Domain.readSubConfigurationSlot( parentTrkCs, CHILD_TRK_CONFIG_SLOT );

      TaskTaskKey reqDefnKey = Domain.createRequirementDefinition( reqDefn -> {
         reqDefn.setOnCondition( true );
         reqDefn.setTaskClass( RefTaskClassKey.REQ );
         reqDefn.setMustRemove( RefTaskMustRemoveKey.OFFWING );
         reqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
         reqDefn.againstConfigurationSlot( childTrkCs );
         reqDefn.addSchedulingRule( CDY, TEN );
      } );

      InventoryKey aircraftKey =
            Domain.createAircraft( aircraft -> aircraft.setAssembly( aircraftAssemblyKey ) );

      ConfigSlotPositionKey parentComponentPosition = new ConfigSlotPositionKey( parentTrkCs, 1 );
      ConfigSlotPositionKey childComponentPosition = new ConfigSlotPositionKey( childTrkCs, 1 );

      InventoryKey parentComponent = Domain.createTrackedInventory( trk -> {
         trk.setLastKnownConfigSlot( ASSEMBLY_CODE, PARENT_TRK_CONFIG_SLOT, "1" );
         trk.setParent( aircraftKey );
         trk.setPosition( parentComponentPosition );
      } );

      PartGroupKey childPartGroupKey = Domain.readPartGroup( childTrkCs, CHILD_TRK_PART_GROUP );

      InventoryKey childComponent = Domain.createTrackedInventory( trk -> {
         trk.setLastKnownConfigSlot( ASSEMBLY_CODE, CHILD_TRK_CONFIG_SLOT, "1" );
         trk.setParent( parentComponent );
         trk.setPartNumber( childPartKey );
         trk.setPartGroup( childPartGroupKey );
         trk.setPosition( childComponentPosition );
      } );

      TaskBean taskBean = new TaskBean();
      taskBean.setSessionContext( new SessionContextFake() );
      taskBean.createFromTaskDefinition( null, childComponent, reqDefnKey, null, authorizingHr,
            null );

      DataSetArgument args = new DataSetArgument();
      args.add( RefTaskClassKey.ADHOC, SchedStaskDao.ColumnName.TASK_CLASS_DB_ID.toString(),
            SchedStaskDao.ColumnName.TASK_CLASS_CD.toString() );
      args.add( parentComponent, "main_inv_no_db_id", "main_inv_no_id" );
      QuerySet qs = QuerySetFactory.getInstance().executeQueryTable( "sched_stask", args );
      assertEquals( "Unexpected number of inventories returned", 1, qs.getRowCount() );
      qs.next();
      EventKey parentTask =
            qs.getKey( TaskKey.class, SchedStaskDao.ColumnName.SCHED_DB_ID.toString(),
                  SchedStaskDao.ColumnName.SCHED_ID.toString() ).getEventKey();
      EvtSchedDeadTable evtSchedDeadTable = EvtSchedDeadTable.findByPrimaryKey( parentTask, CDY );
      assertEquals(
            "Unexpectedly, the adhoc task created against the parent has deadline that differs from the off-wing requirement",
            Double.valueOf( 10 ), Double.valueOf( evtSchedDeadTable.getIntervalQt() ) );
   }


   /**
    * <pre>
    * GIVEN an aircraft inventory at a specified location with existing usages on an aircraft assembly
    * AND and engine on that aircraft
    * AND a scheduled work package on that aircraft with an active task
    * AND custom usages for the aircraft and engine inventory on that work package
    * WHEN the work package is started
    * THEN the created usage snapshots will have the source marked as 'CUSTOMER'
    * </pre>
    */
   @Test
   public void itStartsWorkPackageWithUserDefinedUsages() throws Exception {

      /* Setup */
      String assemblyCd = "ASSEMBLY";
      String subAssemblyCd = "SUBASSY";

      LocationKey location = Domain.createLocation();

      PartNoKey partAcft = Domain.createPart();
      PartNoKey partEngine = Domain.createPart();

      AssemblyKey aircraftAssemblyKey = Domain.createAircraftAssembly( aircraftAssembly -> {
         aircraftAssembly.setCode( assemblyCd );
         aircraftAssembly.setRootConfigurationSlot( rootConfigSlot -> {
            rootConfigSlot.addConfigurationSlot( childConfigSlot -> {
               childConfigSlot.setConfigurationSlotClass( RefBOMClassKey.SUBASSY );
               childConfigSlot.setCode( subAssemblyCd );
            } );
         } );
      } );

      Map<DataTypeKey, BigDecimal> currentUsageEngine = new HashMap<DataTypeKey, BigDecimal>();
      currentUsageEngine.put( DataTypeKey.CDY, new BigDecimal( 0.0 ) );

      InventoryKey engineKey = Domain.createEngine( engine -> {
         engine.setUsage( currentUsageEngine );
         engine.setLocation( location );
         engine.setPartNumber( partEngine );
      } );

      InventoryKey aircraftKey = Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( aircraftAssemblyKey );
         aircraft.addEngine( engineKey );
         aircraft.addUsage( DataTypeKey.CDY, new BigDecimal( 0.0 ) );
         aircraft.addUsage( DataTypeKey.CHR, new BigDecimal( 0.0 ) );
         aircraft.setLocation( location );
         aircraft.setPart( partAcft );
      } );

      Date date = new Date();

      TaskKey taskKey = Domain.createRepetitiveTask( task -> {
         task.setStartDate( date );
      } );

      TaskKey check = Domain.createWorkPackage( workPackage -> {
         workPackage.setAircraft( aircraftKey );
         workPackage.addTask( taskKey );
         workPackage.setLocation( location );
         workPackage.setScheduledStartDate( date );
         workPackage.setActualStartDate( date );
         workPackage.setScheduledEndDate( date );
         workPackage.setActualEndDate( date );
      } );

      UsageSnapshot usageSnapshotACFTCDY = new UsageSnapshot( aircraftKey, DataTypeKey.CDY, 0 )
            .withSnapshotSourceType( RefUsgSnapshotSrcTypeKey.CUSTOMER );
      UsageSnapshot usageSnapshotACFTCHR = new UsageSnapshot( aircraftKey, DataTypeKey.CHR, 0 )
            .withSnapshotSourceType( RefUsgSnapshotSrcTypeKey.CUSTOMER );
      UsageSnapshot usageSnapshotEngineCDY = new UsageSnapshot( engineKey, DataTypeKey.CDY, 0 )
            .withSnapshotSourceType( RefUsgSnapshotSrcTypeKey.CUSTOMER );

      UsageSnapshot currentUsages[] =
            { usageSnapshotACFTCDY, usageSnapshotACFTCHR, usageSnapshotEngineCDY };

      /* Running */
      TaskBean taskBean = new TaskBean();
      taskBean.startWork( check, authorizingHr, date, currentUsages );

      /* Assertions */
      DataSetArgument args = new DataSetArgument();
      args.add( check.getEventKey(), EvtEventDao.ColumnName.EVENT_DB_ID.toString(),
            EvtEventDao.ColumnName.EVENT_ID.toString() );
      QuerySet qs =
            QuerySetFactory.getInstance().executeQueryTable( EvtInvUsageTable.TABLE_NAME, args );

      assertEquals( "Unexpected number of inventories returned", 3, qs.getRowCount() );

      while ( qs.hasNext() ) {
         qs.next();
         assertEquals( "Usage snap shot code is not 'CUSTOMER'", RefUsgSnapshotSrcTypeKey.CUSTOMER,
               qs.getKey( RefUsgSnapshotSrcTypeKey.class, "source_db_id", "source_cd" ) );
      }
   }


   /**
    * <pre>
    * Given a loose component with current usage
    * And a component work package against the loose component
    * When the work package is started with the option of start and enter usage
    * Then the work package usage snapshot has the entered usage for the component.
    * </pre>
    */
   @Test
   public void itCreatesWorkPackageUsageForComponentSameAsEnteredComponentUsage() throws Exception {

      PartNoKey trkPart = Domain.createPart();
      LocationKey location = Domain.createLocation();

      BigDecimal componentCurrentUsage = BigDecimal.TEN;

      Date workPackageStartDate = new Date();

      final InventoryKey trackedInventory = Domain.createTrackedInventory( aTrk -> {
         aTrk.addUsage( HOURS, componentCurrentUsage );
         aTrk.setLocation( location );
         aTrk.setPartNumber( trkPart );
      } );

      final TaskKey componentWorkPackage = Domain.createComponentWorkPackage( aWP -> {
         aWP.setInventory( trackedInventory );
         aWP.atLocation( location );
         aWP.setScheduledStartDate( workPackageStartDate );
         aWP.setScheduledEndDate( workPackageStartDate );
      } );

      BigDecimal enteredComponentWorkPackageUsage = new BigDecimal( 7 );
      UsageSnapshot usageSnapshotComponent =
            new UsageSnapshot( trackedInventory, HOURS, enteredComponentWorkPackageUsage )
                  .withSnapshotSourceType( RefUsgSnapshotSrcTypeKey.CUSTOMER );

      UsageSnapshot currentUsages[] = { usageSnapshotComponent };

      TaskBean taskBean = new TaskBean();
      taskBean.setSessionContext( new SessionContextFake() );
      taskBean.startWork( componentWorkPackage, authorizingHr, workPackageStartDate,
            currentUsages );

      /* Assertions */
      DataSetArgument args = new DataSetArgument();
      args.add( componentWorkPackage.getEventKey(), EvtEventDao.ColumnName.EVENT_DB_ID.toString(),
            EvtEventDao.ColumnName.EVENT_ID.toString() );
      QuerySet qs =
            QuerySetFactory.getInstance().executeQueryTable( EvtInvUsageTable.TABLE_NAME, args );

      assertEquals( "Unexpected number of rows returned", 1, qs.getRowCount() );

      while ( qs.next() ) {
         assertEquals( "Incorrect usage snapshot value returned", enteredComponentWorkPackageUsage
               .compareTo( qs.getBigDecimal( EvtInvUsageTable.ColumnName.TSN_QT.toString() ) ), 0 );
      }
   }


   /**
    * <pre>
    * GIVEN an aircraft inventory at a specified location with existing usages on an aircraft assembly
    * AND and engine on that aircraft
    * AND a scheduled work package on that aircraft with an active task
    * WHEN the work package is started
    * THEN the created usage snapshots will have the source marked as 'MAINTENIX'
    * </pre>
    */
   @Test
   public void itStartsWorkPackageWithNoUsages() throws Exception {

      /* Setup */
      String assemblyCd = "ASSEMBLY";
      String subAssemblyCd = "SUBASSY";

      LocationKey location = Domain.createLocation();

      PartNoKey partAcft = Domain.createPart();
      PartNoKey partEngine = Domain.createPart();

      AssemblyKey aircraftAssemblyKey = Domain.createAircraftAssembly( aircraftAssembly -> {
         aircraftAssembly.setCode( assemblyCd );
         aircraftAssembly.setRootConfigurationSlot( rootConfigSlot -> {
            rootConfigSlot.addConfigurationSlot( childConfigSlot -> {
               childConfigSlot.setConfigurationSlotClass( RefBOMClassKey.SUBASSY );
               childConfigSlot.setCode( subAssemblyCd );
            } );
         } );
      } );

      Map<DataTypeKey, BigDecimal> currentUsageEngine = new HashMap<DataTypeKey, BigDecimal>();
      currentUsageEngine.put( DataTypeKey.CDY, new BigDecimal( 0.0 ) );

      InventoryKey engineKey = Domain.createEngine( engine -> {
         engine.setUsage( currentUsageEngine );
         engine.setLocation( location );
         engine.setPartNumber( partEngine );
      } );

      InventoryKey aircraftKey = Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( aircraftAssemblyKey );
         aircraft.addEngine( engineKey );
         aircraft.addUsage( DataTypeKey.CDY, new BigDecimal( 0.0 ) );
         aircraft.addUsage( DataTypeKey.CHR, new BigDecimal( 0.0 ) );
         aircraft.setLocation( location );
         aircraft.setPart( partAcft );
      } );

      Date date = new Date();

      TaskKey taskKey = Domain.createRepetitiveTask( task -> {
         task.setStartDate( date );
      } );

      TaskKey check = Domain.createWorkPackage( workPackage -> {
         workPackage.setAircraft( aircraftKey );
         workPackage.addTask( taskKey );
         workPackage.setLocation( location );
         workPackage.setScheduledStartDate( date );
         workPackage.setActualStartDate( date );
         workPackage.setScheduledEndDate( date );
         workPackage.setActualEndDate( date );
      } );

      /* Running */
      TaskBean taskBean = new TaskBean();
      taskBean.startWork( check, authorizingHr, date, null );

      /* Assertions */
      DataSetArgument args = new DataSetArgument();
      args.add( check.getEventKey(), EvtEventDao.ColumnName.EVENT_DB_ID.toString(),
            EvtEventDao.ColumnName.EVENT_ID.toString() );
      QuerySet qs =
            QuerySetFactory.getInstance().executeQueryTable( EvtInvUsageTable.TABLE_NAME, args );

      assertEquals( "Unexpected number of inventories returned", 3, qs.getRowCount() );

      while ( qs.hasNext() ) {
         qs.next();
         assertEquals( "Usage snap shot code is not 'MAINTENIX'",
               RefUsgSnapshotSrcTypeKey.MAINTENIX,
               qs.getKey( RefUsgSnapshotSrcTypeKey.class, "source_db_id", "source_cd" ) );
      }
   }


   @Test
   public void whenTaskIsCancelThenTaskCancelledEventShouldBePublished()
         throws MxException, TriggerException {
      // ARRANGE
      final String note = "superman vs batman";
      final PartNoKey partNo = Domain.createPart();
      final AssemblyKey aircraftAssemblyKey = Domain
            .createAircraftAssembly( aircraftAssembly -> aircraftAssembly.setRootConfigurationSlot(
                  rootConfigSlot -> rootConfigSlot.addPartGroup( partGroup -> {
                     partGroup.setInventoryClass( RefInvClassKey.ACFT );
                     partGroup.addPart( partNo );
                  } ) ) );

      final InventoryKey aircraftKey = Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( aircraftAssemblyKey );
         aircraft.setPart( partNo );
      } );

      final TaskKey adhocTaskKey = Domain.createAdhocTask( adhocTask -> {
         adhocTask.setStatus( RefEventStatusKey.ACTV );
         adhocTask.setInventory( aircraftKey );
      } );

      // ACT
      TaskBean taskBean = new TaskBean();
      taskBean.setSessionContext( new SessionContextFake() );
      taskBean.cancel( adhocTaskKey, authorizingHr, null, note );

      // Assert
      assertEquals( 1, eventBus.getEventMessages().size() );
      TaskCancelledEvent taskCancelledEvent =
            ( TaskCancelledEvent ) eventBus.getEventMessages().get( 0 ).getPayload();
      TaskKey expectedTaskKey = adhocTaskKey;
      TaskKey actualTaskKey = taskCancelledEvent.getTaskKey();
      assertEquals( "Incorrect task event", expectedTaskKey, actualTaskKey );

   }


   @Test
   public void whenDrivingCalendarDeadlineIsChangedThenAnEventShouldBePublished()
         throws MxException, TriggerException, JsonParseException, JsonMappingException,
         ClassNotFoundException, IOException {

      // ARRANGE
      final TaskKey taskKey = Domain.createRepetitiveTask( repetetiveTask -> {
         repetetiveTask.setStatus( RefEventStatusKey.ACTV );
         repetetiveTask.setInventory( Domain.createAircraft() );
      } );

      CalendarDeadline calendarDeadline = new CalendarDeadline( CDY, NEW_DEADLINE_INTERVAL,
            DateUtils.addDays( NEW_DEADLINE_START_DATE, NEW_DEADLINE_INTERVAL ) );

      // ACT
      TaskBean taskBean = new TaskBean();
      taskBean.setSessionContext( new SessionContextFake() );
      taskBean.setCalendarDeadline( taskKey, calendarDeadline, authorizingHr );

      // Assert
      QuerySet querySet =
            axonDomainEventDao.findByPayLoadType( TaskDrivingDeadlineRescheduledEvent.class );
      assertEquals( 1, querySet.getRowCount() );
      querySet.next();
      TaskDrivingDeadlineRescheduledEvent taskDrivingDeadlineRescheduledEvent =
            ( TaskDrivingDeadlineRescheduledEvent ) axonObjectMapper.readValue(
                  querySet.getString( AxonDomainEventDao.PAYLOAD_COLUMN ),
                  Class.forName( querySet.getString( AxonDomainEventDao.PAYLOAD_TYPE_COLUMN ) ) );
      assertEquals( taskKey, taskDrivingDeadlineRescheduledEvent.getTaskKey() );

   }


   @Test
   public void whenAddingDrivingUsageDeadlineThenAnEventShouldBePublished()
         throws MxException, TriggerException, JsonParseException, JsonMappingException,
         ClassNotFoundException, IOException {

      // ARRANGE
      // The forecast model is requirement by the add usage deadline logic
      final FcModelKey fcModel = Domain
            .createForecastModel( forecastModel -> forecastModel.addRange( 1, 1, CYCLES, 5d ) );

      final InventoryKey aircraftKey = Domain.createAircraft( aircraft -> {
         aircraft.addUsage( CYCLES, TEN );
         aircraft.setForecastModel( fcModel );
      } );

      final TaskKey taskKey = Domain.createRepetitiveTask( repetetiveTask -> {
         repetetiveTask.setStatus( RefEventStatusKey.ACTV );
         repetetiveTask.setInventory( aircraftKey );
      } );

      UsageDeadline usageDeadline =
            new UsageDeadline( CYCLES, ORIGINAL_DEADLINE_INTERVAL, DUE_VALUE );

      // ACT
      TaskBean taskBean = new TaskBean();
      taskBean.setSessionContext( new SessionContextFake() );
      taskBean.addUsageDeadline( taskKey, usageDeadline, authorizingHr );

      // Assert
      QuerySet querySet =
            axonDomainEventDao.findByPayLoadType( TaskDrivingDeadlineRescheduledEvent.class );
      assertEquals( 1, querySet.getRowCount() );
      querySet.next();
      TaskDrivingDeadlineRescheduledEvent taskDrivingDeadlineRescheduledEvent =
            ( TaskDrivingDeadlineRescheduledEvent ) axonObjectMapper.readValue(
                  querySet.getString( AxonDomainEventDao.PAYLOAD_COLUMN ),
                  Class.forName( querySet.getString( AxonDomainEventDao.PAYLOAD_TYPE_COLUMN ) ) );
      assertEquals( taskKey, taskDrivingDeadlineRescheduledEvent.getTaskKey() );

   }


   @Test
   public void whenEditStartValuesOfTaskThenTaskRescheduledEventShouldBePublished()
         throws MxException, TriggerException {
      // ARRANGE
      final Date today = new Date();
      final String note = "superman is powerful than spiderman";
      final TaskKey taskKey = Domain.createRepetitiveTask( repetitiveTask -> {
         repetitiveTask.setStatus( RefEventStatusKey.ACTV );
         repetitiveTask.setInventory( Domain.createAircraft() );
      } );

      // ACT
      TaskBean taskBean = new TaskBean();
      taskBean.editDeadlineStartValues( new TaskDeadlineStartValue( taskKey, null, today, note ),
            authorizingHr );

      // Assert
      assertEquals(
            new TaskRescheduledEvent( new TaskDeadlineStartValue( taskKey, null, today, note ),
                  authorizingHr ),
            eventBus.getEventMessages().get( eventBus.getEventMessages().size() - 1 )
                  .getPayload() );
   }


   @Test
   public void whenUpdateTaskPlanByDateThenTaskPlanByDateUpdatedEventShouldBePublished()
         throws MxException {
      // ARRANGE
      final TaskKey taskKey = Domain.createRequirement();
      final Date planByDate = DateUtils.addTimeDays( new Date(), 3 );

      // ACT
      new TaskBean().setPlanByDate( taskKey, planByDate, authorizingHr );

      // ASSERT
      assertEquals( new TaskPlanByDateUpdatedEvent( taskKey, authorizingHr, planByDate ), eventBus
            .getEventMessages().get( eventBus.getEventMessages().size() - 1 ).getPayload() );
   }


   /**
    * <pre>
    * Given an aircraft with usage
      And a  completed work package against the aircraft with some usage
      When the work package usage snapshot is edited
      Then the usage snapshot of the work package is set to indicate that it is manually modified
    * </pre>
    *
    *
    */
   @Test
   public void itMarksWorkPackageSnapshotAsModifiedByCustomerWhenWorkPackageSnapshotEdited()
         throws Exception {

      final InventoryKey aircraftKey =
            Domain.createAircraft( aircraft -> aircraft.addUsage( HOURS, BigDecimal.TEN ) );

      UsageSnapshot workPackageHoursUsageSnapshot =
            new UsageSnapshot( aircraftKey, HOURS, BigDecimal.TEN );
      Date workPackageStartDate = new Date();
      TaskKey workPackage = Domain.createWorkPackage( wp -> {
         wp.setActualStartDate( workPackageStartDate );
         wp.setStatus( RefEventStatusKey.COMPLETE );
         wp.addUsageSnapshot( workPackageHoursUsageSnapshot );
         wp.setAircraft( aircraftKey );
      } );

      TaskBean taskBean = new TaskBean();
      taskBean.setSessionContext( new SessionContextFake() );
      UsageSnapshot[] modifiedWorkPackageHoursUsageSnapshot = {
            new UsageSnapshot( aircraftKey, HOURS, workPackageHoursUsageSnapshot.getTSN() + 1.0 ) };

      boolean applyToSubTasksBool = false;
      taskBean.modifyCompletionData( workPackage, modifiedWorkPackageHoursUsageSnapshot,
            workPackageStartDate, applyToSubTasksBool, authorizingHr );

      EvtInvUsageTable evtInvUsageTable =
            EvtInvUsageTable.findByPrimaryKey( new EventInventoryUsageKey(
                  new EventInventoryKey( workPackage.getEventKey(), 1 ), HOURS ) );
      RefUsgSnapshotSrcTypeKey expectedUsageSnapshotSourceType = RefUsgSnapshotSrcTypeKey.CUSTOMER;
      RefUsgSnapshotSrcTypeKey actualUsageSnapshotSourceType =
            evtInvUsageTable.getUsageSnapshotSourceType();

      assertTrue(
            "Unexpected usage snapshot source type as Customer snapshot source type was expected",
            expectedUsageSnapshotSourceType.equals( actualUsageSnapshotSourceType ) );

   }


   /**
    * <pre>
    *
      Given an aircraft with usage
      And a  completed work package against the aircraft with some usage
      And a completed task against the aircraft exists in the work package with some usage
      When the work package usage snapshot is edited and marked to apply changes to sub-tasks
      Then the usage snapshot of the task is set to indicate that it is manually modified.
    * </pre>
    *
    *
    */
   @Test
   public void
         itMarksTaskWithinWorkPackageSnapshotAsModifiedByCustomerWhenWorkPackageSnapshotEditedWithApplyToSubtaskChecked()
               throws Exception {

      final InventoryKey aircraftKey =
            Domain.createAircraft( aircraft -> aircraft.addUsage( HOURS, BigDecimal.TEN ) );

      UsageSnapshot taskHoursUsageSnapshot =
            new UsageSnapshot( aircraftKey, HOURS, BigDecimal.TEN );
      Date taskEndDate = new Date();
      TaskKey task = Domain.createRequirement( req -> {
         req.setStatus( RefEventStatusKey.COMPLETE );
         req.addUsage( taskHoursUsageSnapshot );
         req.setActualEndDate( taskEndDate );
         req.setInventory( aircraftKey );
      } );

      UsageSnapshot workPackageHoursUsageSnapshot =
            new UsageSnapshot( aircraftKey, HOURS, BigDecimal.TEN );
      Date workPackageStartDate = taskEndDate;
      TaskKey workPackage = Domain.createWorkPackage( wp -> {
         wp.setActualStartDate( workPackageStartDate );
         wp.setStatus( RefEventStatusKey.COMPLETE );
         wp.addUsageSnapshot( workPackageHoursUsageSnapshot );
         wp.setAircraft( aircraftKey );
         wp.addTask( task );
      } );

      TaskBean taskBean = new TaskBean();
      taskBean.setSessionContext( new SessionContextFake() );
      UsageSnapshot[] modifiedWorkPackageHoursUsageSnapshot = {
            new UsageSnapshot( aircraftKey, HOURS, workPackageHoursUsageSnapshot.getTSN() + 1.0 ) };
      boolean applyToSubTasksBool = true;
      taskBean.modifyCompletionData( workPackage, modifiedWorkPackageHoursUsageSnapshot,
            workPackageStartDate, applyToSubTasksBool, authorizingHr );

      EvtInvUsageTable evtInvUsageTable = EvtInvUsageTable.findByPrimaryKey(
            new EventInventoryUsageKey( new EventInventoryKey( task.getEventKey(), 1 ), HOURS ) );
      RefUsgSnapshotSrcTypeKey expectedUsageSnapshotSourceType = RefUsgSnapshotSrcTypeKey.CUSTOMER;
      RefUsgSnapshotSrcTypeKey actualUsageSnapshotSourceType =
            evtInvUsageTable.getUsageSnapshotSourceType();

      assertTrue(
            "Unexpected usage snapshot source type as Customer snapshot source type was expected",
            expectedUsageSnapshotSourceType.equals( actualUsageSnapshotSourceType ) );

   }


   /**
    * <pre>
    *
      Given an aircraft with usage
      And a  completed work package against the aircraft with some usage
      And a completed task against the aircraft exists in the work package with some usage
      When the work package usage snapshot is edited and not marked to apply changes to sub-tasks
      Then the usage snapshot of the task is set to indicate that it is modified by maintenix.
    * </pre>
    *
    *
    */
   @Test
   public void
         itMarksTaskWithinWorkPackageSnapshotAsModifiedByMaintenixWhenWorkPackageSnapshotEditedWithApplyToSubtaskUnChecked()
               throws Exception {

      final InventoryKey aircraftKey =
            Domain.createAircraft( aircraft -> aircraft.addUsage( HOURS, BigDecimal.TEN ) );

      UsageSnapshot taskHoursUsageSnapshot =
            new UsageSnapshot( aircraftKey, HOURS, BigDecimal.TEN );
      Date taskEndDate = new Date();
      TaskKey task = Domain.createRequirement( req -> {
         req.setStatus( RefEventStatusKey.COMPLETE );
         req.addUsage( taskHoursUsageSnapshot );
         req.setActualEndDate( taskEndDate );
         req.setInventory( aircraftKey );
      } );

      UsageSnapshot workPackageHoursUsageSnapshot =
            new UsageSnapshot( aircraftKey, HOURS, BigDecimal.TEN );
      Date workPackageStartDate = taskEndDate;
      TaskKey workPackage = Domain.createWorkPackage( wp -> {
         wp.setActualStartDate( workPackageStartDate );
         wp.setStatus( RefEventStatusKey.COMPLETE );
         wp.addUsageSnapshot( workPackageHoursUsageSnapshot );
         wp.setAircraft( aircraftKey );
         wp.addTask( task );
      } );

      TaskBean taskBean = new TaskBean();
      taskBean.setSessionContext( new SessionContextFake() );
      UsageSnapshot[] modifiedWorkPackageHoursUsageSnapshot = {
            new UsageSnapshot( aircraftKey, HOURS, workPackageHoursUsageSnapshot.getTSN() + 1.0 ) };
      boolean applyToSubTasksBool = false;
      taskBean.modifyCompletionData( workPackage, modifiedWorkPackageHoursUsageSnapshot,
            workPackageStartDate, applyToSubTasksBool, authorizingHr );

      EvtInvUsageTable evtInvUsageTable = EvtInvUsageTable.findByPrimaryKey(
            new EventInventoryUsageKey( new EventInventoryKey( task.getEventKey(), 1 ), HOURS ) );
      RefUsgSnapshotSrcTypeKey expectedUsageSnapshotSourceType = RefUsgSnapshotSrcTypeKey.MAINTENIX;
      RefUsgSnapshotSrcTypeKey actualUsageSnapshotSourceType =
            evtInvUsageTable.getUsageSnapshotSourceType();

      assertTrue(
            "Unexpected usage snapshot source type as Customer snapshot source type was expected",
            expectedUsageSnapshotSourceType.equals( actualUsageSnapshotSourceType ) );

   }


   @Test
   public void whenModifyTaskDeadlinesThenTaskDrivingDeadlineRescheduledEventShouldBePublished()
         throws Exception {
      // ARRANGE
      // The forecast model is requirement by the add usage deadline logic
      final FcModelKey fcModel = Domain
            .createForecastModel( forecastModel -> forecastModel.addRange( 1, 1, CYCLES, 5d ) );

      final InventoryKey aircraftKey = Domain.createAircraft( aircraft -> {
         aircraft.addUsage( CYCLES, TEN );
         aircraft.setForecastModel( fcModel );
      } );

      final TaskKey correctiveTaskKey = Domain.createCorrectiveTask( correctiveTask -> {
         correctiveTask.setInventory( aircraftKey );
      } );

      Domain.createFault( fault -> {
         fault.setInventory( aircraftKey );
         fault.setCorrectiveTask( correctiveTaskKey );
         fault.setStatus( RefEventStatusKey.ACTV );
      } );

      UsageDeadline usageDeadline =
            new UsageDeadline( CYCLES, ORIGINAL_DEADLINE_INTERVAL, DUE_VALUE );

      TaskBean taskBean = new TaskBean();
      taskBean.setSessionContext( new SessionContextFake() );
      taskBean.addUsageDeadline( correctiveTaskKey, usageDeadline, authorizingHr );

      axonDomainEventDao.purgeAll();

      // ACT
      UsageDeadline newDeadline = new UsageDeadline( CYCLES, CYCLES_USAGE_INTERVAL,
            CYCLES_USAGE_TSN_DEADLINE, NOTIFY_QT, DIVIATION_QT );
      taskBean.modifyDeadlines( correctiveTaskKey, new UsageDeadline[] { newDeadline },
            authorizingHr );

      // ASSERT
      QuerySet querySet =
            axonDomainEventDao.findByPayLoadType( TaskDrivingDeadlineRescheduledEvent.class );
      assertEquals( 1, querySet.getRowCount() );
      querySet.next();
      TaskDrivingDeadlineRescheduledEvent taskDrivingDeadlineRescheduledEvent =
            ( TaskDrivingDeadlineRescheduledEvent ) axonObjectMapper.readValue(
                  querySet.getString( AxonDomainEventDao.PAYLOAD_COLUMN ),
                  Class.forName( querySet.getString( AxonDomainEventDao.PAYLOAD_TYPE_COLUMN ) ) );
      assertEquals( correctiveTaskKey, taskDrivingDeadlineRescheduledEvent.getTaskKey() );
      assertEquals( CYCLES, taskDrivingDeadlineRescheduledEvent.getDeadlineTypeKey() );
   }


   @Test
   public void
         whenRemoveCalenderDeadlineFromACTVTaskThenShouldNotRemoveDeadlineFromDependentFORECASTAndTaskDeadlinesRemovedEventShouldBePublished()
               throws Exception {
      // ARRANGE
      Date lCalenderDueDateACTV = DateUtils.addDays( new Date(), 1 );
      Date lCalenderDueDateFORECAST = DateUtils.addDays( lCalenderDueDateACTV, 1 );

      // ACTV requirement task with calendar deadline
      final TaskKey taskKeyACTV = Domain.createRequirement( requirement -> {
         requirement.addCalendarDeadline( DataTypeKey.CDY, new BigDecimal( 1 ),
               lCalenderDueDateACTV );
         requirement.setInventory( Domain.createAircraft() );
         requirement.setStatus( RefEventStatusKey.ACTV );
      } );

      // FORECAST requirement task with calendar deadline
      Domain.createRequirement( requirement -> {
         requirement.setStatus( RefEventStatusKey.FORECAST );
         requirement.setPreviousTask( taskKeyACTV );
         requirement.addCalendarDeadline( DataTypeKey.CDY, new BigDecimal( 1 ),
               lCalenderDueDateFORECAST );
      } );

      Map<DataTypeKey, String> dataTypeKeyToDomainTypeMapping = new HashMap<DataTypeKey, String>();
      dataTypeKeyToDomainTypeMapping.put( DataTypeKey.CDY, lCalenderDueDateACTV.toString() );

      // ACT
      TaskBean taskBean = new TaskBean();
      taskBean.removeDeadlines( taskKeyACTV, dataTypeKeyToDomainTypeMapping, authorizingHr );

      // ASSERT
      assertEquals( 1, eventBus.getEventMessages().size() );
      assertEquals(
            new TaskDeadlinesRemovedEvent( taskKeyACTV, dataTypeKeyToDomainTypeMapping.keySet(),
                  authorizingHr ),
            eventBus.getEventMessages().get( eventBus.getEventMessages().size() - 1 )
                  .getPayload() );
   }


   @Test
   public void whenRemoveUsageDeadlineFromTaskThenTaskDeadlinesRemovedEventShouldBePublished()
         throws MxException, TriggerException {

      // ARRANGE
      BigDecimal lUsageDueDate = BigDecimal.TEN;
      final TaskKey taskKey = Domain.createRequirement( requirement -> {
         requirement.addUsageDeadline( DataTypeKey.CYCLES, new BigDecimal( 1 ), lUsageDueDate );
         requirement.setInventory( Domain.createAircraft() );
         requirement.setStatus( RefEventStatusKey.ACTV );
      } );

      Map<DataTypeKey, String> dataTypeKeyToDomainTypeMapping = new HashMap<DataTypeKey, String>();
      dataTypeKeyToDomainTypeMapping.put( DataTypeKey.CYCLES, lUsageDueDate.toString() );

      // ACT
      TaskBean lTaskBean = new TaskBean();
      lTaskBean.removeDeadlines( taskKey, dataTypeKeyToDomainTypeMapping, authorizingHr );

      // ASSERT
      assertEquals( 1, eventBus.getEventMessages().size() );
      assertEquals(
            new TaskDeadlinesRemovedEvent( taskKey, dataTypeKeyToDomainTypeMapping.keySet(),
                  authorizingHr ),
            eventBus.getEventMessages().get( eventBus.getEventMessages().size() - 1 )
                  .getPayload() );

   }


   @Test
   public void
         whenEditIntervalsForCalendarDeadlineOfAnAdhocTaskThenTaskDrivingDeadlineRescheduledEventShouldBePublished()
               throws Exception {
      // ARRANGE
      final AssemblyKey aircraftAssemblyKey = Domain.createAircraftAssembly( aircraftAssembly -> {
         aircraftAssembly.setCode( "ASSEMBLY" );
      } );

      final InventoryKey aircraftKey = Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( aircraftAssemblyKey );
         aircraft.setManufacturedDate( ORIGINAL_DEADLINE_START_DATE );
      } );

      final TaskKey adhocTaskKey = Domain.createRequirement( task -> {
         task.setInventory( aircraftKey );
         task.setTaskClass( ADHOC );
         task.addCalendarDeadline( CDY, CUSTOM, TEN, new BigDecimal( ORIGINAL_DEADLINE_INTERVAL ),
               DateUtils.addDays( ORIGINAL_DEADLINE_START_DATE, ORIGINAL_DEADLINE_INTERVAL ),
               ORIGINAL_DEADLINE_START_DATE );
      } );

      TaskBean taskBean = new TaskBean();

      CalendarDeadline newCalendarDeadline = taskBean.getCalendarDeadline( adhocTaskKey );
      newCalendarDeadline.setInterval( NEW_DEADLINE_INTERVAL );
      newCalendarDeadline.setRecalculateDeadline( true );

      axonDomainEventDao.purgeAll();

      // ACT
      taskBean.setCalendarDeadline( adhocTaskKey, newCalendarDeadline, authorizingHr );

      // ASSERT
      QuerySet querySet =
            axonDomainEventDao.findByPayLoadType( TaskDrivingDeadlineRescheduledEvent.class );
      assertEquals( 1, querySet.getRowCount() );
      querySet.next();
      TaskDrivingDeadlineRescheduledEvent taskDrivingDeadlineRescheduledEvent =
            ( TaskDrivingDeadlineRescheduledEvent ) axonObjectMapper.readValue(
                  querySet.getString( AxonDomainEventDao.PAYLOAD_COLUMN ),
                  Class.forName( querySet.getString( AxonDomainEventDao.PAYLOAD_TYPE_COLUMN ) ) );

      assertEquals( adhocTaskKey, taskDrivingDeadlineRescheduledEvent.getTaskKey() );
      assertEquals( CDY, taskDrivingDeadlineRescheduledEvent.getDeadlineTypeKey() );
   }


   @Test
   public void
         whenEditIntervalsForUsageDeadlineOfAnAdhocTaskThenTaskDrivingDeadlineRescheduledEventShouldBePublished()
               throws Exception {
      // ARRANGE
      final AssemblyKey aircraftAssemblyKey = Domain.createAircraftAssembly( aircraftAssembly -> {
         aircraftAssembly.setCode( "ASSEMBLY" );
         aircraftAssembly.setRootConfigurationSlot( domainConfiguration -> {
            domainConfiguration.addUsageParameter( CYCLES );
         } );
      } );

      final InventoryKey aircraftKey = Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( aircraftAssemblyKey );
         aircraft.setManufacturedDate( ORIGINAL_DEADLINE_START_DATE );
         aircraft.addUsage( CYCLES, new BigDecimal( 400 ) );
      } );

      final TaskKey adhocTaskKey = Domain.createRequirement( task -> {
         task.setInventory( aircraftKey );
         task.setTaskClass( ADHOC );
         task.addDeadline( deadlineConfiguration -> {
            deadlineConfiguration.setScheduledFrom( CUSTOM );
            deadlineConfiguration.setInterval( ORIGINAL_DEADLINE_INTERVAL );
            deadlineConfiguration.setStartTsn( ORIGINAL_START_VALUE );
            deadlineConfiguration.setDueValue( ORIGINAL_START_VALUE + ORIGINAL_DEADLINE_INTERVAL );
            deadlineConfiguration.setUsageType( CYCLES );
         } );
      } );

      TaskBean taskBean = new TaskBean();

      UsageDeadline newUsageDeadline = taskBean.getUsageDeadline( adhocTaskKey, CYCLES );
      newUsageDeadline.setInterval( NEW_DEADLINE_INTERVAL );
      newUsageDeadline.setRecalculateDeadline( true );

      axonDomainEventDao.purgeAll();

      // ACT
      taskBean.setUsageDeadline( adhocTaskKey, newUsageDeadline, authorizingHr );

      // ASSERT
      QuerySet querySet =
            axonDomainEventDao.findByPayLoadType( TaskDrivingDeadlineRescheduledEvent.class );
      assertEquals( 1, querySet.getRowCount() );
      querySet.next();
      TaskDrivingDeadlineRescheduledEvent taskDrivingDeadlineRescheduledEvent =
            ( TaskDrivingDeadlineRescheduledEvent ) axonObjectMapper.readValue(
                  querySet.getString( AxonDomainEventDao.PAYLOAD_COLUMN ),
                  Class.forName( querySet.getString( AxonDomainEventDao.PAYLOAD_TYPE_COLUMN ) ) );

      assertEquals( adhocTaskKey, taskDrivingDeadlineRescheduledEvent.getTaskKey() );
      assertEquals( CYCLES, taskDrivingDeadlineRescheduledEvent.getDeadlineTypeKey() );
   }


   @Test
   public void
         whenEditIntervalsForCalenderDeadlineOfFaultThenTaskDrivingDeadlineRescheduledEventShouldBePublished()
               throws Exception {
      // ARRANGE
      final AssemblyKey aircraftAssemblyKey = Domain.createAircraftAssembly( aircraftAssembly -> {
         aircraftAssembly.setCode( "ASSEMBLY" );
      } );

      final InventoryKey aircraftKey = Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( aircraftAssemblyKey );
         aircraft.setManufacturedDate( ORIGINAL_DEADLINE_START_DATE );
      } );

      final TaskKey correctiveTaskKey = Domain.createCorrectiveTask( correctiveTask -> {
         correctiveTask.setInventory( aircraftKey );
         correctiveTask.addDeadline( deadlineConfiguration -> {
            deadlineConfiguration.setScheduledFrom( CUSTOM );
            deadlineConfiguration.setInterval( ORIGINAL_DEADLINE_INTERVAL );
            deadlineConfiguration.setUsageType( CDY );
            deadlineConfiguration.setDueDate(
                  DateUtils.addDays( ORIGINAL_DEADLINE_START_DATE, ORIGINAL_DEADLINE_INTERVAL ) );
            deadlineConfiguration.setStartDate( ORIGINAL_DEADLINE_START_DATE );

         } );
      } );

      Domain.createFault( fault -> {
         fault.setInventory( aircraftKey );
         fault.setCorrectiveTask( correctiveTaskKey );
         fault.setStatus( RefEventStatusKey.ACTV );
      } );

      TaskBean taskBean = new TaskBean();

      CalendarDeadline newCalendarDeadline = taskBean.getCalendarDeadline( correctiveTaskKey );
      newCalendarDeadline.setInterval( NEW_DEADLINE_INTERVAL );
      newCalendarDeadline.setRecalculateDeadline( true );

      axonDomainEventDao.purgeAll();

      // ACT
      taskBean.setCalendarDeadline( correctiveTaskKey, newCalendarDeadline, authorizingHr );

      // ASSERT
      QuerySet querySet =
            axonDomainEventDao.findByPayLoadType( TaskDrivingDeadlineRescheduledEvent.class );
      assertEquals( 1, querySet.getRowCount() );
      querySet.next();
      TaskDrivingDeadlineRescheduledEvent taskDrivingDeadlineRescheduledEvent =
            ( TaskDrivingDeadlineRescheduledEvent ) axonObjectMapper.readValue(
                  querySet.getString( AxonDomainEventDao.PAYLOAD_COLUMN ),
                  Class.forName( querySet.getString( AxonDomainEventDao.PAYLOAD_TYPE_COLUMN ) ) );

      assertEquals( correctiveTaskKey, taskDrivingDeadlineRescheduledEvent.getTaskKey() );
      assertEquals( CDY, taskDrivingDeadlineRescheduledEvent.getDeadlineTypeKey() );

   }


   @Test
   public void
         whenEditIntervalsForUsageDeadlineOfFaultThenTaskDrivingDeadlineRescheduledEventShouldBePublished()
               throws Exception {
      // ARRANGE
      final AssemblyKey aircraftAssemblyKey = Domain.createAircraftAssembly( aircraftAssembly -> {
         aircraftAssembly.setCode( "ASSEMBLY" );
         aircraftAssembly.setRootConfigurationSlot( domainConfiguration -> {
            domainConfiguration.addUsageParameter( CYCLES );
         } );
      } );

      final InventoryKey aircraftKey = Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( aircraftAssemblyKey );
         aircraft.setManufacturedDate( ORIGINAL_DEADLINE_START_DATE );
         aircraft.addUsage( CYCLES, new BigDecimal( 400 ) );
      } );

      final TaskKey correctiveTaskKey = Domain.createCorrectiveTask( correctiveTask -> {
         correctiveTask.setInventory( aircraftKey );
         correctiveTask.addDeadline( deadlineConfiguration -> {
            deadlineConfiguration.setScheduledFrom( CUSTOM );
            deadlineConfiguration.setInterval( ORIGINAL_DEADLINE_INTERVAL );
            deadlineConfiguration.setStartTsn( ORIGINAL_START_VALUE );
            deadlineConfiguration.setDueValue( ORIGINAL_START_VALUE + ORIGINAL_DEADLINE_INTERVAL );
            deadlineConfiguration.setUsageType( CYCLES );

         } );
      } );

      Domain.createFault( fault -> {
         fault.setInventory( aircraftKey );
         fault.setCorrectiveTask( correctiveTaskKey );
         fault.setStatus( RefEventStatusKey.ACTV );
      } );

      TaskBean taskBean = new TaskBean();

      UsageDeadline newUsageDeadline = taskBean.getUsageDeadline( correctiveTaskKey, CYCLES );
      newUsageDeadline.setInterval( NEW_DEADLINE_INTERVAL );
      newUsageDeadline.setRecalculateDeadline( true );

      axonDomainEventDao.purgeAll();

      // ACT
      taskBean.setUsageDeadline( correctiveTaskKey, newUsageDeadline, authorizingHr );

      // ASSERT
      QuerySet querySet =
            axonDomainEventDao.findByPayLoadType( TaskDrivingDeadlineRescheduledEvent.class );
      assertEquals( 1, querySet.getRowCount() );
      querySet.next();
      TaskDrivingDeadlineRescheduledEvent taskDrivingDeadlineRescheduledEvent =
            ( TaskDrivingDeadlineRescheduledEvent ) axonObjectMapper.readValue(
                  querySet.getString( AxonDomainEventDao.PAYLOAD_COLUMN ),
                  Class.forName( querySet.getString( AxonDomainEventDao.PAYLOAD_TYPE_COLUMN ) ) );

      assertEquals( correctiveTaskKey, taskDrivingDeadlineRescheduledEvent.getTaskKey() );
      assertEquals( CYCLES, taskDrivingDeadlineRescheduledEvent.getDeadlineTypeKey() );
   }


   @Test
   public void whenTerminateATaskThenTaskTerminatedEventShouldBePublished() throws Exception {
      // ARRAGE
      final String userNote = "Testing user note";
      final InventoryKey aircraftKey = Domain.createAircraft();
      final TaskKey taskKey = Domain.createRequirement( task -> {
         task.setStatus( RefEventStatusKey.ACTV );
         task.setInventory( aircraftKey );
      } );

      final DataSetArgument dataSetArgument = new DataSetArgument();
      final RefStageReasonKey stageReasonKey = new RefStageReasonKey( 777, "TESTING" );
      dataSetArgument.add( stageReasonKey, "stage_reason_db_id", "stage_reason_cd" );
      dataSetArgument.add( RefEventStatusKey.TERMINATE, "event_status_db_id", "event_status_cd" );
      MxDataAccess.getInstance().executeInsert( "ref_stage_reason", dataSetArgument );

      final HumanResourceKey hrKey = Domain.createHumanResource();

      // ACT
      new TaskBean().terminate( taskKey, hrKey, stageReasonKey.getCd(), userNote );

      // ASSERT
      assertEquals( 1, eventBus.getEventMessages().size() );
      TaskTerminatedEvent taskTerminatedEvent =
            ( TaskTerminatedEvent ) eventBus.getEventMessages().get( 0 ).getPayload();
      assertEquals( taskKey, taskTerminatedEvent.getTaskKey() );
      assertEquals( stageReasonKey, taskTerminatedEvent.getReasonKey() );
      assertEquals( hrKey, taskTerminatedEvent.getAuthorizingHumanResourceKey() );
   }


   private void assertEventDate( EventKey eventKey, InventoryKey invKey, Date eventDate ) {

      EvtEventTable evtEvent = evtEventDao.findByPrimaryKey( eventKey );
      Assert.assertEquals( DateUtils.toDefaultDateTimeString( eventDate ),
            DateUtils.toDefaultDateTimeString( evtEvent.getEventDate() ) );
   }


   private void assertRemoveRecordDate( EventKey eventKey, InventoryKey invKey, Date eventDate ) {

      final String[] inv_Remove_Columns = { "inv_no_id", "inv_no_db_id", "event_dt" };

      DataSetArgument args = new DataSetArgument();
      args.add( invKey, "inv_no_db_id", "inv_no_id" );
      args.add( eventKey, "event_db_id", "event_id" );
      QuerySet querySet =
            QuerySetFactory.getInstance().executeQuery( inv_Remove_Columns, "inv_remove", args );

      if ( !querySet.next() ) {
         fail( "Expected to find remove record" );
      }

      Date removedDate = querySet.getDate( "event_dt" );
      Assert.assertEquals( DateUtils.toDefaultDateTimeString( eventDate ),
            DateUtils.toDefaultDateTimeString( removedDate ) );

   }


   private int getForcastedTasks( TaskKey task ) {

      DataSetArgument dataSetArg = new DataSetArgument();

      dataSetArg.add( "al_eventdbid", task.getDbId() );
      dataSetArg.add( "al_eventid", task.getId() );

      QuerySet qs = QuerySetFactory.getInstance()
            .executeQuery( "com.mxi.mx.core.query.task.SelectForcastedTasks", dataSetArg );

      return qs.getRows().size();
   }


   private TaskKey getReqTaskOfBlockTask( TaskKey blockTask ) {

      DataSetArgument args = new DataSetArgument();
      args.add( blockTask, "h_sched_db_id", "h_sched_id" );
      args.add( RefTaskClassKey.REQ, "task_class_db_id", "task_class_cd" );

      QuerySet qs = QuerySetFactory.getInstance().executeQueryTable( "sched_stask", args );

      return ( qs.first() ) ? qs.getKey( TaskKey.class, "sched_db_id", "sched_id" ) : null;
   }


   private RefEventStatusKey getStatus( TaskKey task ) {
      return evtEventDao.findByPrimaryKey( task ).getEventStatus();
   }


   private TaskKey getNextBlockInChain( TaskKey prevBlockTask ) {
      DataSetArgument args = new DataSetArgument();
      args.add( prevBlockTask.getEventKey(), "event_db_id", "event_id" );
      args.add( RefRelationTypeKey.DEPT, "rel_type_db_id", "rel_type_cd" );

      QuerySet qs = QuerySetFactory.getInstance().executeQueryTable( "evt_event_rel", args );
      qs.next();

      return new TaskKey( qs.getKey( EventKey.class, "rel_event_db_id", "rel_event_id" ) );
   }


   private AssemblyKey createAircraftAssemblyWithPart( final PartNoKey part ) {
      return Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

         @Override
         public void configure( AircraftAssembly builder ) {
            builder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

               @Override
               public void configure( ConfigurationSlot builder ) {
                  builder.addPartGroup( new DomainConfiguration<PartGroup>() {

                     @Override
                     public void configure( PartGroup builder ) {
                        builder.addPart( part );
                        builder.setInventoryClass( ACFT );

                     }

                  } );

               }

            } );
         }
      } );
   }


   private List<TaskKey> getTasksInWorkScope( TaskKey workpackage ) {
      List<TaskKey> tasks = new ArrayList<>();
      DataSetArgument args = new DataSetArgument();
      args.add( workpackage, "wo_sched_db_id", "wo_sched_id" );
      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "sched_wo_line", args );
      while ( lQs.next() ) {
         tasks.add( lQs.getKey( TaskKey.class, "sched_db_id", "sched_id" ) );
      }
      return tasks;
   }
}
