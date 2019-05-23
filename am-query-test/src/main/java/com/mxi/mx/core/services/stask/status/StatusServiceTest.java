package com.mxi.mx.core.services.stask.status;

import static com.mxi.mx.core.key.DataTypeKey.CYCLES;
import static com.mxi.mx.core.key.DataTypeKey.HOURS;
import static java.math.BigDecimal.valueOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.api.events.EventType;
import com.mxi.am.api.events.MaintenixEvent;
import com.mxi.am.api.events.TaskUpdatedEvent;
import com.mxi.am.api.resource.sys.eventconfig.EventConfig;
import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.AircraftAssembly;
import com.mxi.am.domain.CalculatedUsageParameter;
import com.mxi.am.domain.ConfigurationSlot;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.EngineAssembly;
import com.mxi.am.domain.Flight;
import com.mxi.am.domain.WorkPackage;
import com.mxi.am.domain.builder.DomainBuilder;
import com.mxi.am.domain.builder.EventRelationshipBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.PartRequestBuilder;
import com.mxi.am.domain.builder.PartRequirementDomainBuilder;
import com.mxi.am.domain.builder.SdFaultBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.domain.builder.TaskRevisionBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.GlobalParametersFake;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.services.event.MxEventServiceStub;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.api.resource.sys.eventconfig.EventConfigDaoFactory;
import com.mxi.mx.core.configuration.axon.RecordingEventBus;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.EqpPartBaselineKey;
import com.mxi.mx.core.key.EventInventoryKey;
import com.mxi.mx.core.key.EventInventoryUsageKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.RefDomainTypeKey;
import com.mxi.mx.core.key.RefEngUnitKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefRelationTypeKey;
import com.mxi.mx.core.key.RefStageReasonKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.RefUsgSnapshotSrcTypeKey;
import com.mxi.mx.core.key.TaskInstPartKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskPartKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.key.ToolKey;
import com.mxi.mx.core.production.task.domain.TaskCancelledEvent;
import com.mxi.mx.core.services.event.InvalidStatusException;
import com.mxi.mx.core.services.event.inventory.UsageSnapshot;
import com.mxi.mx.core.services.events.MxEventsService;
import com.mxi.mx.core.table.eqp.EqpPartBaselineTable;
import com.mxi.mx.core.table.evt.EvtEventDao;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.evt.EvtInvTable;
import com.mxi.mx.core.table.evt.EvtInvUsage;
import com.mxi.mx.core.table.evt.EvtInvUsageTable;
import com.mxi.mx.core.table.evt.EvtToolTable;
import com.mxi.mx.core.table.mim.MimDataType;
import com.mxi.mx.core.table.org.OrgHr;
import com.mxi.mx.core.table.req.ReqPartTable;
import com.mxi.mx.core.unittest.table.evt.EvtEventUtil;
import com.mxi.mx.core.usage.model.UsageType;


/**
 * This class tests the {@link StatusService} class.
 *
 * @author dsewell
 */
public final class StatusServiceTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();
   @Rule
   public InjectionOverrideRule injectionOverrideRule = new InjectionOverrideRule();

   private static final HumanResourceKey HR = new HumanResourceKey( 4650, 999 );
   private static final PartGroupKey BOM_PART_KEY = new PartGroupKey( 4650, 33128 );
   private static final PartNoKey PART_NO_KEY = new PartNoKey( 4650, 20779 );
   private static final String USER_NOTE = "need to be cancelled";
   private EvtEventDao iEvtEventDao;
   private HumanResourceKey authorizingHr;
   private RecordingEventBus eventBus;
   private int userId;
   private UserParametersFake userParametersFake;
   private String[] validRootTaskStatusList;
   private static final String CALC_PARM_EQUATION_FUNCTION_NAME = "DOUBLE_HOURS";
   private static final String CALC_PARM_CODE_1 = "DOUBLE_HOURS_1";
   private static final String CALC_PARM_CODE_2 = "DOUBLE_HOURS_2";
   private static final String CALC_PARM_CODE_3 = "DOUBLE_HOURS_3";
   private static final String CALC_PARM_CODE_4 = "DOUBLE_HOURS_4";
   private static final String CALC_PARM_CONSTANT_NAME = "CALC_PARM_CONSTANT_NAME";
   private static final BigDecimal CALC_PARM_CONSTANT_VALUE = BigDecimal.valueOf( 2.0 );
   private static final BigDecimal USAGE_SNAPSHOT_HOURS = BigDecimal.valueOf( 7.0 );
   private static final BigDecimal USAGE_SNAPSHOT_CYCLES = BigDecimal.valueOf( 8.0 );
   private static final BigDecimal USAGE_SNAPSHOT_ENGINE_HOURS = BigDecimal.valueOf( 9.0 );
   private static final BigDecimal USAGE_SNAPSHOT_ENGINE_CYCLES = BigDecimal.valueOf( 10.0 );
   private static final String CREATE_FUNCTION_STATEMENT = "CREATE OR REPLACE FUNCTION "
         + CALC_PARM_EQUATION_FUNCTION_NAME + " (" + "aConstant NUMBER, aHoursInput NUMBER" + " )"
         + " RETURN NUMBER" + " " + "IS " + "result NUMBER; " + "BEGIN" + " "
         + "result := aConstant * aHoursInput ; " + "RETURN" + " " + " result;" + "END" + " "
         + CALC_PARM_EQUATION_FUNCTION_NAME + " ;";


   @Before
   public void setUp() {
      iEvtEventDao = InjectorContainer.get().getInstance( EvtEventDao.class );
      eventBus = injectionOverrideRule.select( RecordingEventBus.class ).get();
      authorizingHr = Domain.createHumanResource();
      userId = OrgHr.findByPrimaryKey( authorizingHr ).getUserId();

      userParametersFake = new UserParametersFake( userId, "LOGIC" );
      UserParameters.setInstance( userId, "LOGIC", userParametersFake );

      GlobalParameters globalParametersFake = new GlobalParametersFake( "LOGIC" );

      // The UserParametersFake is used because the user parameter
      // ALLOW_ASSIGN_LRP_ITEM_FOR_INWORK_WP need to have a value.
      GlobalParameters.setInstance( "LOGIC", globalParametersFake );

      validRootTaskStatusList = new String[1];
      validRootTaskStatusList[0] = "ACTV";
   }


   @After
   public void tearDown() {
      UserParameters.setInstance( userId, "LOGIC", null );
      GlobalParameters.setInstance( "LOGIC", null );
   }


   /**
    * This class tests the flow of the activateNextDependentTasks method for repetitive ADHOC tasks
    * on a fault Specifically when a repetitive ACTV task is being completed if it has a tool, then
    * the tool will be copied to the new ACTV task (former FORECAST task) but without any defined
    * inventory.
    *
    * @throws Exception
    *            Exception If an error occurs.
    */
   @Test
   public void testActivateNextDependentRepetitiveTaskForAdhocTask() throws Exception {
      // data
      FaultKey lFault = new SdFaultBuilder().build();
      TaskKey lCorrectiveTask = new TaskBuilder().build();
      TaskKey lCompletedTask = new TaskBuilder().withTaskClass( RefTaskClassKey.ADHOC ).build();
      TaskKey lForecastTask = new TaskBuilder().withStatus( RefEventStatusKey.FORECAST ).build();

      new EventRelationshipBuilder().fromEvent( lFault ).toEvent( lCorrectiveTask )
            .withType( RefRelationTypeKey.CORRECT ).build();

      new EventRelationshipBuilder().fromEvent( lCompletedTask ).toEvent( lFault )
            .withType( RefRelationTypeKey.RECSRC ).build();

      new EventRelationshipBuilder().fromEvent( lForecastTask ).toEvent( lFault )
            .withType( RefRelationTypeKey.RECSRC ).build();

      new EventRelationshipBuilder().fromEvent( lForecastTask ).toEvent( lForecastTask )
            .withType( RefRelationTypeKey.DRVTASK ).build();

      new EventRelationshipBuilder().fromEvent( lCompletedTask ).toEvent( lCompletedTask )
            .withType( RefRelationTypeKey.DRVTASK ).build();

      new EventRelationshipBuilder().fromEvent( lCompletedTask ).toEvent( lForecastTask )
            .withType( RefRelationTypeKey.DEPT ).build();

      EvtToolTable lToolTable =
            EvtToolTable.create( new EventKey( lCompletedTask.getDbId(), lCompletedTask.getId() ),
                  BOM_PART_KEY, PART_NO_KEY );
      InventoryKey lInvKey = new InventoryBuilder().build();
      lToolTable.setInventoryKey( lInvKey );

      EqpPartBaselineTable lEqpPartBaselineTable =
            EqpPartBaselineTable.create( new EqpPartBaselineKey( BOM_PART_KEY, PART_NO_KEY ) );
      lEqpPartBaselineTable.setStandardBool( true );
      lEqpPartBaselineTable.insert();

      new StatusService( lCompletedTask ).activateNextDependentTasks( HR );

      // ensure the forecast task is now active
      new EvtEventUtil( lForecastTask ).assertEventStatus( RefEventStatusKey.ACTV );

      // ensure that the activated former forecast task does not have inventory key
      ToolKey lToolKey = new ToolKey( lForecastTask.getDbId(), lForecastTask.getId(), 1 );
      EvtToolTable.findByPrimaryKey( lToolKey );

      assertNull( EvtToolTable.findByPrimaryKey( lToolKey ).getInventoryKey() );
   }


   /**
    * This class tests the basic flow of the activateNextDependentTasks method.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testActivateNextDependentTasks() throws Exception {
      // data
      TaskKey lCompletedTask = new TaskBuilder().build();
      TaskKey lForecastTask = new TaskBuilder().withStatus( RefEventStatusKey.FORECAST ).build();

      new EventRelationshipBuilder().fromEvent( lCompletedTask ).toEvent( lForecastTask )
            .withType( RefRelationTypeKey.DEPT ).build();

      new StatusService( lCompletedTask ).activateNextDependentTasks( HR );

      // ensure the forecast task is now active
      new EvtEventUtil( lForecastTask ).assertEventStatus( RefEventStatusKey.ACTV );
   }


   /**
    * This class tests the flow of the activateNextDependentTasks method with a mandatory block with
    * on condition children.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testActivateNextDependentTasksWithMandatoryBlock() throws Exception {

      // data
      TaskKey lCompletedTask = new TaskBuilder().build();
      DomainBuilder<TaskTaskKey> lBlockBuilder = new TaskRevisionBuilder()
            .withTaskClass( TaskRevisionBuilder.TASK_CLASS_BLOCK ).isMandatory();
      TaskKey lForecastTask = new TaskBuilder().withStatus( RefEventStatusKey.FORECAST )
            .withTaskRevision( lBlockBuilder ).build();

      TaskKey lForecastChildTask = new TaskBuilder().withStatus( RefEventStatusKey.FORECAST )
            .withParentTask( lForecastTask ).build();

      new EventRelationshipBuilder().fromEvent( lCompletedTask ).toEvent( lForecastTask )
            .withType( RefRelationTypeKey.DEPT ).build();

      new StatusService( lCompletedTask ).activateNextDependentTasks( HR );

      // ensure the forecast task and child task are now active
      new EvtEventUtil( lForecastTask ).assertEventStatus( RefEventStatusKey.ACTV );
      new EvtEventUtil( lForecastChildTask ).assertEventStatus( RefEventStatusKey.ACTV );
   }


   /**
    * This class tests startWork() method
    *
    * when:
    * <li>There is a work package with COMMIT status; <br>
    * There are two part requirements on the task with part requests in the work package; <br>
    * The two part requests have reserved inventory; <br>
    * One of the part request has no supply location; <br>
    *
    * Then: Start work package <br>
    *
    * Assert: The two part requests stay the same as before without any change
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testPartRequestsNoChangeAfterStartWpEvenOneHasNullSupplyLoc() throws Exception {

      // build an aircraft
      InventoryKey lAircraft =
            new InventoryBuilder().atLocation( new LocationDomainBuilder().build() ).build();

      // build two parts
      PartNoKey lPartA = createNoAutoReservePart();
      PartNoKey lPartB = createNoAutoReservePart();

      // build a supply location
      LocationKey lSupplyLocation = new LocationDomainBuilder().isSupplyLocation().build();

      // build a commit work package for this aircraft
      TaskKey lWorkPackage = new TaskBuilder().onInventory( lAircraft )
            .withStatus( RefEventStatusKey.COMMIT ).withTaskClass( RefTaskClassKey.CHECK )
            .atLocation( lSupplyLocation ).withScheduledStart( new Date() )
            .withScheduledEnd( DateUtils.addDays( new Date(), 5 ) ).build();

      // build an ad-hoc task
      TaskKey lAdHocTask = new TaskBuilder().withStatus( RefEventStatusKey.ACTV )
            .withParentTask( lWorkPackage ).build();

      // build part requests for the two parts and reserve inventories for them in part requirements
      InventoryKey lInvA = createInventory( lPartA );
      InventoryKey lInvB = createInventory( lPartB );
      PartRequestKey lPartRequestA = createPartRequestWithReservedInv( lAdHocTask,
            createPartRequirement( lPartA, lAdHocTask ), lInvA );
      PartRequestKey lPartRequestB = createPartRequestWithReservedInv( lAdHocTask,
            createPartRequirement( lPartB, lAdHocTask ), lInvB );

      // null out the printed supply location for the first one
      setPrintedSupplyLocAndLastAutoRsrvDtOfPartReq( lPartRequestA, null, new Date() );
      setPrintedSupplyLocAndLastAutoRsrvDtOfPartReq( lPartRequestB, lSupplyLocation,
            DateUtils.addDays( new Date(), 5 ) );

      new StatusService( lWorkPackage ).startWork( HR, new Date(), null, false );

      // ensure the two part requests have no change (status and reserved inventories)
      new EvtEventUtil( lPartRequestA ).assertEventStatus( RefEventStatusKey.PRAVAIL );
      new EvtEventUtil( lPartRequestB ).assertEventStatus( RefEventStatusKey.PRAVAIL );
      assertEquals( lInvA, ReqPartTable.findByPrimaryKey( lPartRequestA ).getInventory() );
      assertEquals( lInvB, ReqPartTable.findByPrimaryKey( lPartRequestB ).getInventory() );

   }


   /**
    * Tests that when a task is marked as error, that the corresponding event is sent to be
    * published by pubsub.
    *
    * @throws MxException
    * @throws TriggerException
    */
   @Test
   public void testMarkTaskAsErrorPublishedEvent() throws MxException, TriggerException {
      EventConfig taskUpdatedConfig = new EventConfig();
      taskUpdatedConfig.setEnabledBool( true );
      taskUpdatedConfig.setEventTypeCode( "MX_TASK_UPDATED" );
      EventConfigDaoFactory.getInstance().getEventConfigDao().update( taskUpdatedConfig );

      TaskKey activeTask = new TaskBuilder().withStatus( RefEventStatusKey.ACTV ).build();
      RefStageReasonKey refStageReasonKey = new RefStageReasonKey( null );

      new StatusService( activeTask ).markAsError( HR, refStageReasonKey, "ACTV to ERROR" );

      Map<String, MaintenixEvent> events = getSentMaintenixEvents();

      assertEquals( "Didn't get the expected number of published events: ", 1, events.size() );
      MaintenixEvent event = events.get( "MX_TASK_UPDATED" );
      assertEquals( "Incorrect event type: ", EventType.MX_TASK_UPDATED, event.getEventType() );
      TaskUpdatedEvent taskUpdatedEvent = ( TaskUpdatedEvent ) event;
      assertEquals( "Incorrect updated type: ", TaskUpdatedEvent.UpdateType.MARK_AS_ERROR,
            taskUpdatedEvent.getUpdateType() );

   }


   /**
    * This test is verifying when delay a in work status work package, the scheduled end date will
    * be updated with the new delayed end date
    *
    * Given a work package is in IN WORK status
    *
    * When delay this work package
    *
    * Then verify the work package new end date is updated
    */
   @Test
   public void itUpdatesTheEndDateWhenDelayInWorkWorkPackage() throws Exception {
      final Date lSchedEndDate = DateUtils.addDays( new Date(), 2 );
      Date lDelayedDate = DateUtils.floorSecond( DateUtils.addDays( lSchedEndDate, 5 ) );
      final TaskKey lWorkPackage =
            Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

               @Override
               public void configure( WorkPackage aBuilder ) {
                  aBuilder.setStatus( RefEventStatusKey.IN_WORK );
                  aBuilder.setScheduledEndDate( lSchedEndDate );
               }

            } );

      StatusService lStatusService = new StatusService( lWorkPackage );

      lStatusService.delay( lDelayedDate, null, RefEventStatusKey.DELAY, null, HR );

      EvtEventDao lEvtEventDao = InjectorContainer.get().getInstance( EvtEventDao.class );
      Date ActualEndDate = lEvtEventDao
            .findByPrimaryKey( new EventKey( lWorkPackage.getDbId(), lWorkPackage.getId() ) )
            .getEventDate();

      assertEquals( lDelayedDate, ActualEndDate );
   }


   /**
    * <pre>
    * Verify that committing an ACTV work package changes its status to COMMIT.
    * Given an ACTV work package against an aircraft
    * And the work package is scheduled
    * When the work package is committed
    * Then the work package status is updated to COMMIT
    * </pre>
    */
   @Test
   public void testCommittingWorkscopeOfAnActvWorkPackageChangesItsStatusToCommit()
         throws Exception {

      // Given an ACTV work package against an aircraft and the work package is scheduled
      final LocationKey lLocation = Domain.createLocation();
      final Date lDate = new Date();
      TaskKey lWorkPackage = Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {
            aWorkPackage.setLocation( lLocation );
            aWorkPackage.setScheduledStartDate( lDate );
            aWorkPackage.setScheduledEndDate( DateUtils.addDays( lDate, 5 ) );
         }

      } );

      // When the work package is committed
      StatusService lStatusService = new StatusService( lWorkPackage );
      lStatusService.commitWorkscope( HR, null, null, null );

      // Then the work package status is updated to COMMIT
      EventKey lWorkPackageEventKey = lWorkPackage.getEventKey();
      EvtEventTable lEvtEventTable = iEvtEventDao.findByPrimaryKey( lWorkPackageEventKey );
      assertEquals( RefEventStatusKey.COMMIT, lEvtEventTable.getEventStatusCd() );
   }


   /**
    * This test is verifying delaying a work package which is not in In work status, it will throw
    * an InvalidStatusException
    *
    * Given a work package is in ACTIVE status
    *
    * When delay the work package
    *
    * Then verify it is throwing InvalidStatusException Exception
    */
   @Test( expected = InvalidStatusException.class )
   public void itThrowsExceptionWhenDelayWorkPackageOtherThanInWork() throws Exception {
      final Date lSchedEndDate = DateUtils.addDays( new Date(), 2 );
      Date lDelayedDate = DateUtils.floorSecond( DateUtils.addDays( lSchedEndDate, 5 ) );
      final TaskKey lWorkPackage =
            Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

               @Override
               public void configure( WorkPackage aBuilder ) {
                  aBuilder.setStatus( RefEventStatusKey.ACTV );
                  aBuilder.setScheduledEndDate( lSchedEndDate );
               }

            } );

      StatusService lStatusService = new StatusService( lWorkPackage );

      lStatusService.delay( lDelayedDate, null, RefEventStatusKey.DELAY, null, HR );
   }


   /**
    * <pre>
    * Verify that uncommitting a COMMIT work package changes its status to ACTV.
    * Given a COMMIT work package against an aircraft
    * When the work package is uncommitted
    * Then the work package status is updated to ACTV
    * </pre>
    */
   @Test
   public void testUncommittingWorkscopeOfACommitWorkPackageChangesItsStatusToActv()
         throws Exception {

      // Given a COMMIT work package against an aircraft
      TaskKey lWorkPackage = Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {
            aWorkPackage.setStatus( RefEventStatusKey.COMMIT );
         }

      } );

      // When the work package is uncommitted
      StatusService lStatusService = new StatusService( lWorkPackage );
      lStatusService.uncommitWorkscope( HR, null, null );

      // Then the work package status is updated to ACTV
      EventKey lWorkPackageEventKey = lWorkPackage.getEventKey();
      EvtEventTable lEvtEventTable = iEvtEventDao.findByPrimaryKey( lWorkPackageEventKey );
      assertEquals( RefEventStatusKey.ACTV, lEvtEventTable.getEventStatusCd() );
   }


   /**
    * <pre>
    * Verify that unstarting an IN WORK work package changes its status to COMMIT.
    * Given an IN WORK work package against an aircraft
    * When the work package is unstarted
    * Then the work package status is updated to COMMIT
    * </pre>
    */
   @Test
   public void testUnstartingAnInWorKPackageChangesItsStatusToCommit() throws Exception {

      final LocationKey lLocation = Domain.createLocation();
      final PartNoKey lPart = Domain.createPart();

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         /*
          * Aircraft has to be set at a location and have parts because of validations inside
          * unstart method or else it would throw MxRuntimeException
          */

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setLocation( lLocation );
            aAircraft.setPart( lPart );
         }
      } );

      // Given an IN WORK work package against an aircraft
      TaskKey lWorkPackage = Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {
            aWorkPackage.setStatus( RefEventStatusKey.IN_WORK );
            aWorkPackage.setAircraft( lAircraft );
         }
      } );

      RefStageReasonKey lRefStageReasonKey = new RefStageReasonKey( null );

      // When the work package is unstarted
      StatusService lStatusService = new StatusService( lWorkPackage );
      lStatusService.undoStartWork( HR, lRefStageReasonKey, null );

      // Then the work package status is updated to COMMIT
      EventKey lWorkPackageEventKey = lWorkPackage.getEventKey();
      EvtEventTable lEvtEventTable = iEvtEventDao.findByPrimaryKey( lWorkPackageEventKey );
      assertEquals( RefEventStatusKey.COMMIT, lEvtEventTable.getEventStatusCd() );
   }


   /**
    *
    * When a work package is started on an inventory, the usage snapshot for the work package will
    * equal the Tsn minus the delta values of the next usage adjustment snapshot (first usage
    * adjustment after the work package start date).
    *
    * <pre>
    *
    * Given - an aircraft with current usage
    *       - and a historical flight with usage
    *       - and a scheduled work package on the aircraft
    *
    * When - the work package is started before the historical flight
    *
    * Then - the usage snapshot of the work package is the Tsn minus the delta
    *        of the historical flight (which occurred after the work package start date)
    *
    * </pre>
    */
   @Test
   public void itCalculatesWPUsgSnapshotUsingNextUsgAdjustmentUsgSnapshot() throws Exception {

      final Date lFlightDate = DateUtils.addDays( new Date(), -5 );
      final BigDecimal lFlightDeltaTsn = valueOf( 2.0 );
      final BigDecimal lFlightTsn = valueOf( 5.0 );
      final Date lWorkPackageStart = DateUtils.addDays( lFlightDate, -3 );
      final Date lWorkPackageEnd = DateUtils.addDays( lWorkPackageStart, 1 );
      final BigDecimal lCurrentAircraftTsn = valueOf( 11.0 );
      final LocationKey lWorkPackageLocation = Domain.createLocation();

      // Given
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( DataTypeKey.HOURS, lCurrentAircraftTsn );
            aAircraft.setLocation( lWorkPackageLocation );
            aAircraft.setPart( Domain.createPart() );
         }
      } );

      final TaskKey lWorkPackage =
            Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

               @Override
               public void configure( WorkPackage aWorkPackage ) {
                  aWorkPackage.setAircraft( lAircraft );
                  aWorkPackage.setScheduledStartDate( lWorkPackageStart );
                  aWorkPackage.setScheduledEndDate( lWorkPackageEnd );
                  aWorkPackage.setLocation( lWorkPackageLocation );

               }
            } );

      createHistoricalFlight( lAircraft, lFlightDate, lFlightDeltaTsn, lFlightTsn );

      // When
      new StatusService( lWorkPackage ).startWork( HR, lWorkPackageStart, null, true );

      // Then
      BigDecimal lExpectedUsageHour = lFlightTsn.subtract( lFlightDeltaTsn );
      BigDecimal lActualUsageHour =
            getUsageOfSnapshot( lWorkPackage, lAircraft, DataTypeKey.HOURS );
      assertEquals( "Unexpected Tsn in workpackage snapshot taken before historic flight.",
            lExpectedUsageHour, lActualUsageHour );
   }


   /**
    *
    * When a work package is started on an inventory and there is no usage adjustment after the work
    * package start date, the usage snapshot for the work package will equal the current usage of
    * the inventory.
    *
    * <pre>
    *
    * Given - an aircraft with current usage
    *       - and a work package scheduled on the aircraft
    *
    * When - the work package is started
    *
    * Then - the usage snapshot of the work package is equal to the current usage of the aircraft
    *
    * </pre>
    */
   @Test
   public void itCalculatesWPUsgSnapshotUsingCurrentUsgWhenNoNextUsgAdjustment() throws Exception {

      final Date lWorkPackageStart = DateUtils.addDays( new Date(), -3 );
      final Date lWorkPackageEnd = DateUtils.addDays( lWorkPackageStart, 1 );
      final BigDecimal lCurrentAircraftTsn = valueOf( 11.0 );
      final LocationKey lWorkPackageLocation = Domain.createLocation();

      // Given
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( DataTypeKey.HOURS, lCurrentAircraftTsn );
            aAircraft.setLocation( lWorkPackageLocation );
            aAircraft.setPart( Domain.createPart() );
         }
      } );

      final TaskKey lWorkPackage =
            Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

               @Override
               public void configure( WorkPackage aWorkPackage ) {
                  aWorkPackage.setAircraft( lAircraft );
                  aWorkPackage.setScheduledStartDate( lWorkPackageStart );
                  aWorkPackage.setScheduledEndDate( lWorkPackageEnd );
                  aWorkPackage.setLocation( lWorkPackageLocation );

               }
            } );

      // When
      new StatusService( lWorkPackage ).startWork( HR, lWorkPackageStart, null, true );

      // Then
      BigDecimal lExpectedUsageHour = lCurrentAircraftTsn;
      BigDecimal lActualUsageHour =
            getUsageOfSnapshot( lWorkPackage, lAircraft, DataTypeKey.HOURS );
      assertEquals( "Unexpected Tsn in workpackage snapshot with no next usage adjustments.",
            lExpectedUsageHour, lActualUsageHour );
   }


   /**
    *
    *
    *
    * <pre>
    *
    * Given - An aircraft assembly with a calculated usage parameter,
    *       and an engine assembly with the same calculated usage parameter on that aircraft assembly,
    *       and an inventory of that aircraft with that engine
    *
    * When - A work package is started on the aircraft inventory with custom usages input by the user, NOT
    *       including custom usages for the calculated parameters
    *
    * Then - The calculated parameters are recalculated based on the custom usages
    *
    * </pre>
    */
   @Test
   public void itTriggersRecalculationOfCalculatedParametersWhenCalcUsageNotProvidedToStartWork()
         throws Exception {

      /* Given */

      {
         // Create a function that can be used as a calculated parameter equation.
         //
         // IMPORTANT:
         // all data setup in the DB prior to calling this method will be explicitly
         // rolled-back! This is because the DDL executed within this method implicitly performs a
         // commit and we cannot commit that test data.
         Domain.createCalculatedParameterEquationFunction( iDatabaseConnectionRule.getConnection(),
               CREATE_FUNCTION_STATEMENT );
      }

      LocationKey lLocation = Domain.createLocation();
      Date lDate1 = new Date();
      PartNoKey lAircraftPart = Domain.createPart();
      PartNoKey lEnginePart = Domain.createPart();

      // Create an aircraft assembly whose root config slot is assigned many usage parameters and
      // many calculated parameters based on the usage parameters.
      Map<String, DataTypeKey> lAcftConfigParms = new HashMap<String, DataTypeKey>();
      lAcftConfigParms.put( CALC_PARM_CODE_1, HOURS );
      lAcftConfigParms.put( CALC_PARM_CODE_2, CYCLES );

      final AssemblyKey lAcftAssembly =
            createAircraftAssyWithCalcParms( lAcftConfigParms, lAircraftPart );

      final DataTypeKey lCalcParmKey1 = readConfigParmKey( lAcftAssembly, CALC_PARM_CODE_1 );
      final DataTypeKey lCalcParmKey2 = readConfigParmKey( lAcftAssembly, CALC_PARM_CODE_2 );

      // Create an aircraft based on the aircraft assembly.
      final InventoryKey lAircraft = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssembly( lAcftAssembly );
         aAircraft.setPart( lAircraftPart );
         aAircraft.addUsage( HOURS, BigDecimal.ZERO );
         aAircraft.addUsage( CYCLES, BigDecimal.ZERO );
         aAircraft.addUsage( lCalcParmKey1, BigDecimal.ZERO );
         aAircraft.addUsage( lCalcParmKey2, BigDecimal.ZERO );
      } );

      // Create two more usage parameters to test with.
      DataTypeKey iEngineHours = MimDataType.create( "ENGINE_HOURS", "ENGINE_HOURS",
            RefDomainTypeKey.USAGE_PARM, RefEngUnitKey.HOURS, 2, null );
      DataTypeKey iEngineCycles = MimDataType.create( "ENGINE_CYCLES", "ENGINE_CYCLES",
            RefDomainTypeKey.USAGE_PARM, RefEngUnitKey.CYCLES, 2, null );

      // Create an engine assembly whose root config slot is assigned many usage parameters and many
      // calculated parameters based on the usage parameters.
      Map<String, DataTypeKey> lEngineConfigParms = new HashMap<String, DataTypeKey>();
      lEngineConfigParms.put( CALC_PARM_CODE_3, iEngineHours );
      lEngineConfigParms.put( CALC_PARM_CODE_4, iEngineCycles );

      final AssemblyKey lEngineAssembly =
            createEngineAssyWithCalcParms( lEngineConfigParms, lEnginePart );

      final DataTypeKey lCalcParmKey3 = readConfigParmKey( lEngineAssembly, CALC_PARM_CODE_3 );
      final DataTypeKey lCalcParmKey4 = readConfigParmKey( lEngineAssembly, CALC_PARM_CODE_4 );

      // Create an engine based on the engine assembly and installed on the aircraft.
      final InventoryKey lEngine = Domain.createEngine( aEngine -> {
         aEngine.setAssembly( lEngineAssembly );
         aEngine.setParent( lAircraft );
         aEngine.setPartNumber( lEnginePart );
         aEngine.addUsage( HOURS, BigDecimal.ZERO );
         aEngine.addUsage( CYCLES, BigDecimal.ZERO );
         aEngine.addUsage( lCalcParmKey3, BigDecimal.ZERO );
         aEngine.addUsage( lCalcParmKey4, BigDecimal.ZERO );
      } );

      TaskKey lTask = Domain.createAdhocTask( aTask -> {
         aTask.setInventory( lAircraft );
         aTask.setStatus( RefEventStatusKey.ACTV );
      } );

      TaskKey lWorkPackage = Domain.createWorkPackage( aWorkPackage -> {
         aWorkPackage.addTask( lTask );
         aWorkPackage.setAircraft( lAircraft );
         aWorkPackage.setLocation( lLocation );
         aWorkPackage.setScheduledStartDate( lDate1 );
         aWorkPackage.setScheduledEndDate( lDate1 );
      } );

      UsageSnapshot[] lUsageSnapshots = new UsageSnapshot[4];
      lUsageSnapshots[0] = new UsageSnapshot( lAircraft, HOURS, USAGE_SNAPSHOT_HOURS )
            .withSnapshotSourceType( RefUsgSnapshotSrcTypeKey.CUSTOMER );

      lUsageSnapshots[1] = new UsageSnapshot( lAircraft, CYCLES, USAGE_SNAPSHOT_CYCLES )
            .withSnapshotSourceType( RefUsgSnapshotSrcTypeKey.CUSTOMER );

      lUsageSnapshots[2] = new UsageSnapshot( lEngine, iEngineHours, USAGE_SNAPSHOT_ENGINE_HOURS )
            .withSnapshotSourceType( RefUsgSnapshotSrcTypeKey.CUSTOMER );

      lUsageSnapshots[3] = new UsageSnapshot( lEngine, iEngineCycles, USAGE_SNAPSHOT_ENGINE_CYCLES )
            .withSnapshotSourceType( RefUsgSnapshotSrcTypeKey.CUSTOMER );

      /* When */
      new StatusService( lWorkPackage ).startWork( HR, lDate1, lUsageSnapshots, true, null, false );

      /* Then */
      BigDecimal lExpected1 = USAGE_SNAPSHOT_HOURS.multiply( CALC_PARM_CONSTANT_VALUE );
      BigDecimal lActual1 = readDataTypeValue( lWorkPackage, lAircraft, lCalcParmKey1 );

      BigDecimal lExpected2 = USAGE_SNAPSHOT_CYCLES.multiply( CALC_PARM_CONSTANT_VALUE );
      BigDecimal lActual2 = readDataTypeValue( lWorkPackage, lAircraft, lCalcParmKey2 );

      BigDecimal lExpected3 = USAGE_SNAPSHOT_ENGINE_HOURS.multiply( CALC_PARM_CONSTANT_VALUE );
      BigDecimal lActual3 = readDataTypeValue( lWorkPackage, lEngine, lCalcParmKey3 );

      BigDecimal lExpected4 = USAGE_SNAPSHOT_ENGINE_CYCLES.multiply( CALC_PARM_CONSTANT_VALUE );
      BigDecimal lActual4 = readDataTypeValue( lWorkPackage, lEngine, lCalcParmKey4 );

      assertEquals(
            "Checks that first calculated parameter for the Aircraft was calculated correctly", 0,
            lActual1.compareTo( lExpected1 ) );
      assertEquals(
            "Checks that second calculated parameter for the Aircraft was calculated correctly", 0,
            lActual2.compareTo( lExpected2 ) );
      assertEquals(
            "Checks that first calculated parameter for the Engine was calculated correctly", 0,
            lActual3.compareTo( lExpected3 ) );
      assertEquals(
            "Checks that second calculated parameter for the Engine was calculated correctly", 0,
            lActual4.compareTo( lExpected4 ) );
   }


   @Test
   public void whenWorkPackageIsCancelledThenCheckTaskCancelledEventShouldBePublished()
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

      final TaskKey jicKey = Domain.createJobCard( jobCard -> {
         jobCard.setInventory( aircraftKey );
         jobCard.setDefinition( lJicDefinitionKey );
         jobCard.setStatus( RefEventStatusKey.ACTV );
      } );
      final TaskKey reqKey = Domain.createRequirement( requirement -> {
         requirement.setInventory( aircraftKey );
         requirement.setDefinition( reqDefinitionKey );
         requirement.setStatus( RefEventStatusKey.ACTV );
         requirement.addUsageDeadline( DataTypeKey.HOURS, BigDecimal.TEN, null );
         requirement.addSubTask( jicKey );
      } );

      final TaskKey blockKey = Domain.createBlock( block -> {
         block.setInventory( aircraftKey );
         block.setDefinition( lBlockDefinitionKey );
         block.setStatus( RefEventStatusKey.ACTV );
         block.addDeadline( aDeadline -> {
            aDeadline.setUsageType( DataTypeKey.HOURS );
            aDeadline.setInterval( BigDecimal.TEN );

         } );
         block.addRequirement( reqKey );
      } );

      final TaskKey workPackageKey = Domain.createWorkPackage( wp -> {
         wp.setStatus( RefEventStatusKey.ACTV );
         wp.setAircraft( aircraftKey );
         wp.addTask( blockKey );
      } );

      // ACT
      new StatusService( workPackageKey ).cancel( authorizingHr, null, USER_NOTE, null,
            validRootTaskStatusList, false, false );

      // ASSERT
      assertEquals( 2, eventBus.getEventMessages().size() );

      final Set<TaskKey> taskKeys = eventBus.getEventMessages().stream().filter( eventMessage -> {
         return eventMessage.getPayload() instanceof TaskCancelledEvent;
      } ).map( event -> {
         return ( ( TaskCancelledEvent ) event.getPayload() ).getTaskKey();
      } ).collect( Collectors.toSet() );

      assertTrue( taskKeys.contains( workPackageKey ) );

   }


   @Test
   public void whenBlockTaskIsCancelledThenBlockTaskCancelledEventShouldBePublished()
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

      final TaskKey jicKey = Domain.createJobCard( jobCard -> {
         jobCard.setInventory( aircraftKey );
         jobCard.setDefinition( lJicDefinitionKey );
         jobCard.setStatus( RefEventStatusKey.ACTV );
      } );
      final TaskKey reqKey = Domain.createRequirement( requirement -> {
         requirement.setInventory( aircraftKey );
         requirement.setDefinition( reqDefinitionKey );
         requirement.setStatus( RefEventStatusKey.ACTV );
         requirement.addUsageDeadline( DataTypeKey.HOURS, BigDecimal.TEN, null );
         requirement.addSubTask( jicKey );
      } );

      final TaskKey blockKey = Domain.createBlock( block -> {
         block.setInventory( aircraftKey );
         block.setDefinition( lBlockDefinitionKey );
         block.setStatus( RefEventStatusKey.ACTV );
         block.addDeadline( aDeadline -> {
            aDeadline.setUsageType( DataTypeKey.HOURS );
            aDeadline.setInterval( BigDecimal.TEN );

         } );
         block.addRequirement( reqKey );
      } );

      Domain.createWorkPackage( wp -> {
         wp.setStatus( RefEventStatusKey.ACTV );
         wp.setAircraft( aircraftKey );
         wp.addTask( blockKey );
      } );

      // ACT
      new StatusService( blockKey ).cancel( authorizingHr, null, USER_NOTE, null,
            validRootTaskStatusList, false, false );

      // ASSERT
      assertEquals( 1, eventBus.getEventMessages().size() );

      final Set<TaskKey> taskKeys = eventBus.getEventMessages().stream().filter( eventMessage -> {
         return eventMessage.getPayload() instanceof TaskCancelledEvent;
      } ).map( event -> {
         return ( ( TaskCancelledEvent ) event.getPayload() ).getTaskKey();
      } ).collect( Collectors.toSet() );

      assertTrue( taskKeys.contains( blockKey ) );

   }


   @Test
   public void whenReqTaskIsCancelledThenReqAndJicTaskCancelledEventShouldBePublished()
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

      final TaskKey jicKey = Domain.createJobCard( jobCard -> {
         jobCard.setInventory( aircraftKey );
         jobCard.setDefinition( lJicDefinitionKey );
         jobCard.setStatus( RefEventStatusKey.ACTV );
      } );
      final TaskKey reqKey = Domain.createRequirement( requirement -> {
         requirement.setInventory( aircraftKey );
         requirement.setDefinition( reqDefinitionKey );
         requirement.setStatus( RefEventStatusKey.ACTV );
         requirement.addUsageDeadline( DataTypeKey.HOURS, BigDecimal.TEN, null );
         requirement.addSubTask( jicKey );
      } );

      final TaskKey blockKey = Domain.createBlock( block -> {
         block.setInventory( aircraftKey );
         block.setDefinition( lBlockDefinitionKey );
         block.setStatus( RefEventStatusKey.ACTV );
         block.addDeadline( aDeadline -> {
            aDeadline.setUsageType( DataTypeKey.HOURS );
            aDeadline.setInterval( BigDecimal.TEN );

         } );
         block.addRequirement( reqKey );
      } );

      Domain.createWorkPackage( wp -> {
         wp.setStatus( RefEventStatusKey.ACTV );
         wp.setAircraft( aircraftKey );
         wp.addTask( blockKey );
      } );

      // ACT
      new StatusService( reqKey ).cancel( authorizingHr, null, USER_NOTE, null,
            validRootTaskStatusList, false, false );

      // ASSERT
      assertEquals( 2, eventBus.getEventMessages().size() );

      final Set<TaskKey> taskKeys = eventBus.getEventMessages().stream().filter( eventMessage -> {
         return eventMessage.getPayload() instanceof TaskCancelledEvent;
      } ).map( event -> {
         return ( ( TaskCancelledEvent ) event.getPayload() ).getTaskKey();
      } ).collect( Collectors.toSet() );

      assertTrue( taskKeys.contains( reqKey ) );
      assertTrue( taskKeys.contains( jicKey ) );

   }


   @Test
   public void whenJicTaskIsCancelledThenJicTaskCancelledEventShouldBePublished() throws Exception {
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

      final TaskKey jicKey = Domain.createJobCard( jobCard -> {
         jobCard.setInventory( aircraftKey );
         jobCard.setDefinition( lJicDefinitionKey );
         jobCard.setStatus( RefEventStatusKey.ACTV );
      } );
      final TaskKey reqKey = Domain.createRequirement( requirement -> {
         requirement.setInventory( aircraftKey );
         requirement.setDefinition( reqDefinitionKey );
         requirement.setStatus( RefEventStatusKey.ACTV );
         requirement.addUsageDeadline( DataTypeKey.HOURS, BigDecimal.TEN, null );
         requirement.addSubTask( jicKey );
      } );

      final TaskKey blockKey = Domain.createBlock( block -> {
         block.setInventory( aircraftKey );
         block.setDefinition( lBlockDefinitionKey );
         block.setStatus( RefEventStatusKey.ACTV );
         block.addDeadline( aDeadline -> {
            aDeadline.setUsageType( DataTypeKey.HOURS );
            aDeadline.setInterval( BigDecimal.TEN );

         } );
         block.addRequirement( reqKey );
      } );

      Domain.createWorkPackage( wp -> {
         wp.setStatus( RefEventStatusKey.ACTV );
         wp.setAircraft( aircraftKey );
         wp.addTask( blockKey );
      } );

      // ACT
      new StatusService( jicKey ).cancel( authorizingHr, null, USER_NOTE, null,
            validRootTaskStatusList, false, false );

      // ASSERT
      assertEquals( 1, eventBus.getEventMessages().size() );

      final Set<TaskKey> taskKeys = eventBus.getEventMessages().stream().filter( eventMessage -> {
         return eventMessage.getPayload() instanceof TaskCancelledEvent;
      } ).map( event -> {
         return ( ( TaskCancelledEvent ) event.getPayload() ).getTaskKey();
      } ).collect( Collectors.toSet() );

      assertTrue( taskKeys.contains( jicKey ) );

   }


   /**
    *
    *
    *
    * <pre>
    *
    * Given - A loose engine inventory with usage records at time A(earlier) and time B(later)
    *
    * When - A work package is started with the main inventory as the loose engine in between time A and B
    *
    * Then - The usage snapshot of the start event created for the inventory will be that of the usage record at time A
    *
    * </pre>
    */
   @Test
   public void itChecksSnapshotOfWorkPackageStartWhenPackageAndCompletingTaskOnLooseAssembly()
         throws Exception {

      /* Given */
      LocationKey lLocation = Domain.createLocation();
      PartNoKey lEnginePart = Domain.createPart( aPart -> {
         aPart.setInventoryClass( RefInvClassKey.ASSY );
      } );

      Date lDate1 = new Date();
      Date lDate2 = DateUtils.addDays( lDate1, -50 );
      Date lDate3 = DateUtils.addDays( lDate1, -100 );

      BigDecimal lStartCycles = new BigDecimal( 100 );
      BigDecimal lStartHours = new BigDecimal( 500 );
      BigDecimal lEndCycles = new BigDecimal( 300 );
      BigDecimal lEndHours = new BigDecimal( 1500 );

      AssemblyKey lEngineAssembly = Domain.createEngineAssembly( aAssembly -> {
         aAssembly.setCode( "CFM56" );
         aAssembly.setRootConfigurationSlot( aRoot -> {
            aRoot.addPartGroup( aRootPart -> {
               aRootPart.setInventoryClass( RefInvClassKey.ASSY );
               aRootPart.addPart( lEnginePart );
            } );
         } );
      } );

      InventoryKey lEngine = Domain.createEngine( aEngine -> {
         aEngine.setAssembly( lEngineAssembly );
         aEngine.setPartNumber( lEnginePart );
         aEngine.setLocation( lLocation );
         aEngine.addUsage( DataTypeKey.HOURS, lEndHours );
         aEngine.addUsage( DataTypeKey.CYCLES, lEndCycles );
      } );

      Domain.createUsageAdjustment( aUsageOne -> {
         aUsageOne.setUsageDate( lDate3 );
         aUsageOne.setUsageType( UsageType.MXFLIGHT );
         aUsageOne.setMainInventory( lEngine );
         aUsageOne.addUsage( lEngine, DataTypeKey.HOURS, lStartHours, lStartHours );
         aUsageOne.addUsage( lEngine, DataTypeKey.CYCLES, lStartCycles, lStartCycles );
      } );

      Domain.createUsageAdjustment( aUsageOne -> {
         aUsageOne.setUsageDate( lDate1 );
         aUsageOne.setUsageType( UsageType.MXFLIGHT );
         aUsageOne.setMainInventory( lEngine );
         aUsageOne.addUsage( lEngine, DataTypeKey.HOURS, lEndHours, new BigDecimal( 1000 ) );
         aUsageOne.addUsage( lEngine, DataTypeKey.CYCLES, lEndCycles, new BigDecimal( 200 ) );
      } );

      TaskKey lTask = Domain.createAdhocTask( aTask -> {
         aTask.setInventory( lEngine );
         aTask.setStatus( RefEventStatusKey.ACTV );
      } );

      TaskKey lWorkPackage = Domain.createWorkPackage( aWorkPackage -> {
         aWorkPackage.addTask( lTask );
         aWorkPackage.setAircraft( lEngine );
         aWorkPackage.setLocation( lLocation );
         aWorkPackage.setActualStartDate( lDate2 );
         aWorkPackage.setActualEndDate( lDate2 );
         aWorkPackage.setScheduledStartDate( lDate2 );
         aWorkPackage.setScheduledEndDate( lDate2 );
      } );

      /* When */
      StatusService lStatusService = new StatusService( lWorkPackage );
      lStatusService.startWork( HR, lDate2, null, true, null, false );

      /* Then */
      EvtInvTable lEvtInvTableRow =
            EvtInvTable.findByEventAndInventory( lWorkPackage.getEventKey(), lEngine );
      EventInventoryKey lEvtInvKey = lEvtInvTableRow.getPk();

      EventInventoryUsageKey aEvtInvUsageHoursKey =
            new EventInventoryUsageKey( lEvtInvKey, DataTypeKey.HOURS );
      EventInventoryUsageKey aEvtInvUsageCyclesKey =
            new EventInventoryUsageKey( lEvtInvKey, DataTypeKey.CYCLES );

      EvtInvUsage lHoursUsage = EvtInvUsage.findByPrimaryKey( aEvtInvUsageHoursKey );
      EvtInvUsage lCyclesUsage = EvtInvUsage.findByPrimaryKey( aEvtInvUsageCyclesKey );

      assertEquals( "Assert that the work package snapshot HOURS matches what is expected",
            lStartHours, new BigDecimal( lHoursUsage.getTsnQt() ) );
      assertEquals( "Assert that the work package snapshot CYCLES matches what is expected",
            lStartCycles, new BigDecimal( lCyclesUsage.getTsnQt() ) );
   }


   /**
    * Returns published events from the test stub of the MxEventsService.
    *
    * @return
    */
   private Map<String, MaintenixEvent> getSentMaintenixEvents() {
      MxEventServiceStub stubEventService =
            ( MxEventServiceStub ) MxEventsService.getTestInstance();
      assertNotNull( "List of published events was null.", stubEventService.getPublishedEvents() );
      return stubEventService.getPublishedEvents();
   }


   private void createHistoricalFlight( final InventoryKey aAircraft, final Date aArrivalDate,
         final BigDecimal aDeltaUsageHour, final BigDecimal aUsageHour ) {

      Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setHistorical( true );
            aFlight.setAircraft( aAircraft );
            aFlight.setArrivalDate( aArrivalDate );
            aFlight.addUsage( aAircraft, DataTypeKey.HOURS, aDeltaUsageHour, aUsageHour );
         }
      } );

   }


   private BigDecimal getUsageOfSnapshot( TaskKey aTask, InventoryKey aInv,
         DataTypeKey aDataType ) {

      final EvtInvTable lEvtInv = EvtInvTable.findByEventAndInventory( aTask.getEventKey(), aInv );

      if ( lEvtInv == null ) {
         return null;
      }

      final EventInventoryUsageKey lUsageKey =
            new EventInventoryUsageKey( lEvtInv.getPk(), aDataType );

      return valueOf( EvtInvUsageTable.findByPrimaryKey( lUsageKey ).getTsnQt() );
   }


   /**
    * Set Printed Supply Location and Last Auto-Reservation date
    *
    * @param aPartRequest
    *           the part request
    * @param aPrintedSupplyLoc
    *           the printed supply location
    * @param aLastAutoRsrvDt
    *           the last auto-reservation date
    */
   private void setPrintedSupplyLocAndLastAutoRsrvDtOfPartReq( PartRequestKey aPartRequest,
         LocationKey aPrintedSupplyLoc, Date aLastAutoRsrvDt ) {

      ReqPartTable lPartRequest = ReqPartTable.findByPrimaryKey( aPartRequest );
      lPartRequest.setPrintedSupplyLoc( aPrintedSupplyLoc );
      lPartRequest.setLastAutoRsrvDt( aLastAutoRsrvDt );
      lPartRequest.setPrintedDt( aLastAutoRsrvDt );
      lPartRequest.update();
   }


   /**
    * Create part request with reserved inventory
    *
    * @param aTask
    *           the task
    * @param aTaskPart
    *           the task part
    * @param aInv
    *           the reserved inventory
    * @return the part request
    */
   private PartRequestKey createPartRequestWithReservedInv( TaskKey aTask, TaskPartKey aTaskPart,
         InventoryKey aInv ) {
      return new PartRequestBuilder().forTask( aTask ).withReservedInventory( aInv )
            .withStatus( RefEventStatusKey.PRAVAIL )
            .forPartRequirement( new TaskInstPartKey( aTaskPart, 1 ) ).build();
   }


   /**
    * Create inventory
    *
    * @param aPart
    *           the part
    * @return the inventory
    */
   private InventoryKey createInventory( PartNoKey aPart ) {
      return new InventoryBuilder().withPartNo( aPart ).build();
   }


   /**
    * Create part requirement
    *
    * @param aPart
    *           the part
    * @param aTask
    *           the task
    * @return the part requirement
    */
   private TaskPartKey createPartRequirement( PartNoKey aPart, TaskKey aTask ) {
      return new PartRequirementDomainBuilder( aTask ).forPart( aPart ).withInstallPart( aPart )
            .withInstallQuantity( 1.0 ).build();
   }


   /**
    * Create part with No Auto-Reservation
    *
    * @return the part with no auto-reservation
    */
   private PartNoKey createNoAutoReservePart() {
      return new PartNoBuilder().isNoAutoReserve().build();
   }


   /**
    *
    * Create an aircraft assembly with calculated parameters added to its root config slot.
    *
    * @param aCalcParmMap
    *           map of calc parm code to usage parm DataTypeKey <br/>
    *           (e.g. [<calc parm code1>:<usage parm key1>,<calc parm code2>:<usage parm key2>,...]
    *
    * @return key of the created aircraft assembly
    */
   private AssemblyKey createAircraftAssyWithCalcParms( final Map<String, DataTypeKey> aCalcParmMap,
         PartNoKey aPart ) {

      return Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

         @Override
         public void configure( AircraftAssembly aBuilder ) {
            aBuilder.setRootConfigurationSlot(
                  buildRootConfigSlotWithCalcParms( aCalcParmMap, aPart ) );
         }
      } );

   }


   /**
    *
    * Builds a domain configuration for a root config slot with calculated parameters and their
    * associated usage parameters .
    *
    * @param aCalcParmMap
    *           map of calc parm code to usage parm DataTypeKey <br/>
    *           (e.g. [<calc parm code1>:<usage parm key1>,<calc parm code2>:<usage parm key2>,...]
    *
    * @return domain configuration of the root config slot
    */

   private DomainConfiguration<ConfigurationSlot> buildRootConfigSlotWithCalcParms(
         final Map<String, DataTypeKey> aCalcParmMap, PartNoKey aPart ) {

      final Set<Entry<String, DataTypeKey>> lCalcParmMapEntries = aCalcParmMap.entrySet();

      return new DomainConfiguration<ConfigurationSlot>() {

         @Override
         public void configure( ConfigurationSlot aBuilder ) {

            for ( Entry<String, DataTypeKey> lCalcParmMapEntry : lCalcParmMapEntries ) {

               final String lCalcParmCode = lCalcParmMapEntry.getKey();
               final DataTypeKey lUsageParmKey = lCalcParmMapEntry.getValue();

               // Add the usage parameter.
               aBuilder.addUsageParameter( lUsageParmKey );

               // Add the calculated parameter referencing the usage parameter it is based on.
               aBuilder.addCalculatedUsageParameter(
                     new DomainConfiguration<CalculatedUsageParameter>() {

                        @Override
                        public void configure( CalculatedUsageParameter aBuilder ) {

                           aBuilder.setCode( lCalcParmCode );
                           aBuilder.addParameter( lUsageParmKey );

                           aBuilder.setDatabaseCalculation( CALC_PARM_EQUATION_FUNCTION_NAME );
                           aBuilder.setPrecisionQt( 2 );
                           aBuilder.addConstant( CALC_PARM_CONSTANT_NAME,
                                 CALC_PARM_CONSTANT_VALUE );
                        }

                     } );
            }
            aBuilder.addPartGroup( aPartGroup -> {
               aPartGroup.setInventoryClass( RefInvClassKey.ASSY );
               aPartGroup.addPart( aPart );
            } );

         }
      };
   }


   /**
    *
    * Create an engine assembly with calculated parameters added to its root config slot.
    *
    * @param aCalcParmMap
    *           map of calc parm code to usage parm DataTypeKey <br/>
    *           (e.g. [<calc parm code1>:<usage parm key1>,<calc parm code2>:<usage parm key2>,...]
    *
    * @return
    */
   private AssemblyKey createEngineAssyWithCalcParms( final Map<String, DataTypeKey> aCalcParmMap,
         PartNoKey aPart ) {

      return Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

         @Override
         public void configure( EngineAssembly aBuilder ) {
            aBuilder.setRootConfigurationSlot(
                  buildRootConfigSlotWithCalcParms( aCalcParmMap, aPart ) );
         }
      } );

   }


   private BigDecimal readDataTypeValue( TaskKey aTask, InventoryKey aInv, DataTypeKey aDataType ) {

      return BigDecimal.valueOf( EvtInvUsage.findByPrimaryKey( new EventInventoryUsageKey(
            EvtInvTable.findByEventAndInventory( aTask.getEventKey(), aInv ).getPk(), aDataType ) )
            .getTsnQt() );
   }


   private DataTypeKey readConfigParmKey( AssemblyKey aAssembly, String aCalcParmCode ) {
      return Domain.readUsageParameter( Domain.readRootConfigurationSlot( aAssembly ),
            aCalcParmCode );
   }

}
