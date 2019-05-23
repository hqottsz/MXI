package com.mxi.mx.core.ejb.stask;

import static com.mxi.am.domain.Domain.createLocation;
import static com.mxi.mx.core.key.DataTypeKey.CDY;
import static com.mxi.mx.core.key.RefEventStatusKey.COMMIT;
import static com.mxi.mx.core.key.RefInvClassKey.ACFT;
import static com.mxi.mx.core.key.RefLocTypeKey.AIRPORT;
import static com.mxi.mx.core.key.RefSchedFromKey.CUSTOM;
import static com.mxi.mx.core.key.RefTaskClassKey.FOLLOW;
import static com.mxi.mx.core.key.RefTaskClassKey.REQ;
import static java.math.BigDecimal.TEN;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.SessionContext;
import javax.transaction.UserTransaction;

import org.axonframework.eventhandling.EventMessage;
import org.joda.time.Instant;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import com.mxi.am.db.AxonDomainEventDao;
import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.servlet.SessionContextFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.common.trigger.TriggerFactory;
import com.mxi.mx.common.trigger.TriggerFactoryStub;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.configuration.axon.AxonObjectMapper;
import com.mxi.mx.core.configuration.axon.RecordingEventBus;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.EventInventoryUsageKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.RefUsgSnapshotSrcTypeKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.production.task.domain.TaskCompletedEvent;
import com.mxi.mx.core.production.task.domain.TaskDeadlineExtendedEvent;
import com.mxi.mx.core.production.task.domain.TaskDrivingDeadlineRescheduledEvent;
import com.mxi.mx.core.services.stask.creation.CreateQuickCheckTO;
import com.mxi.mx.core.services.stask.deadline.DeadlineExtensionTO;
import com.mxi.mx.core.services.stask.status.ScheduleInternalTO;
import com.mxi.mx.core.table.evt.EvtEventDao;
import com.mxi.mx.core.table.evt.EvtInvTable;
import com.mxi.mx.core.table.evt.EvtInvUsageTable;
import com.mxi.mx.core.table.org.OrgHr;
import com.mxi.mx.core.table.sched.SchedStaskDao;
import com.mxi.mx.core.table.sched.SchedStaskTable;
import com.mxi.mx.core.trigger.MxCoreTriggerType;
import com.mxi.mx.core.trigger.ordernumber.MxOrderNumberGenerator;


/**
 * Integration test for {@linkplain STaskBean}
 *
 */
public class STaskBeanTest {

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule injectionOverrideRule = new InjectionOverrideRule();

   private static final String POSITION_DESCRIPTION = "POSITION_DESCRIPTION";
   private static final HumanResourceKey HR = HumanResourceKey.ADMIN;

   private static AxonObjectMapper objectMapper = new AxonObjectMapper();

   private static SchedStaskDao schedStaskDao;

   private static EvtEventDao evtEventDao;

   private RecordingEventBus eventBus;

   private static final Integer ORIGINAL_DEADLINE_INTERVAL = 10;
   private static final Date ORIGINAL_DEADLINE_START_DATE =
         DateUtils.addDays( new Date(), -ORIGINAL_DEADLINE_INTERVAL );

   private static final double DEFAULT_FC_MODEL_RATE = 2.252363464d;

   private static final AxonDomainEventDao axonDomainEventDao = new AxonDomainEventDao();

   private HumanResourceKey hr;
   private int userId;
   private SessionContext iSessionContext;

   private MxOrderNumberGenerator iOrderNumberGenerator = mockOrderNumberGenerator();


   @Before
   public void setUp() throws Exception {

      iSessionContext = Mockito.mock( SessionContext.class, Mockito.RETURNS_DEEP_STUBS );
      schedStaskDao = InjectorContainer.get().getInstance( SchedStaskDao.class );
      evtEventDao = InjectorContainer.get().getInstance( EvtEventDao.class );
      int lUserId = OrgHr.findByPrimaryKey( HR ).getUserId();
      UserParametersFake userParametersFake = new UserParametersFake( lUserId, "LOGIC" );
      userParametersFake.setBoolean( "ALLOW_ASSIGN_LRP_ITEM_FOR_INWORK_WP", false );
      UserParameters.setInstance( lUserId, "LOGIC", userParametersFake );

      Map<String, Object> lTriggerMap = new HashMap<String, Object>( 1 );
      lTriggerMap.put( MxCoreTriggerType.WO_NUM_GEN.toString(), iOrderNumberGenerator );

      TriggerFactory lTriggerFactoryStub = new TriggerFactoryStub( lTriggerMap );
      TriggerFactory.setInstance( lTriggerFactoryStub );
      eventBus = injectionOverrideRule.select( RecordingEventBus.class ).get();
      hr = Domain.createHumanResource( h -> h.setAllAuthority( true ) );
      userId = OrgHr.findByPrimaryKey( hr ).getUserId();

      // User parm ALLOW_ASSIGN_LRP_ITEM_FOR_INWORK_WP needed for DetailsService.setParentTask().
      UserParameters.setInstance( userId, "LOGIC", new UserParametersFake( userId, "LOGIC" ) );
   }


   @After
   public void after() {
      UserParameters.setInstance( userId, "LOGIC", null );
   }


   /**
    *
    * Verify that when there exists a suppressing JIC (and thus a suppressed JIC) and there exists
    * another non-suppressed duplicated JIC within the same work scope (assigned to a REQ class
    * requirement), that the system will perform suppression on all the duplicated JICs.
    *
    * The suppression logic randomly selects one of the JICs to be the suppressing JIC, thus it may
    * change from the original.
    *
    * <pre>
    * Given many "duplicated JIC tasks", assigned to REQ class requirement tasks, within a committed work package.
    *   And one of the JIC tasks is suppressed by another JIC task.
    *   And there is yet another of the duplicated JIC task that is not suppressed.
    *  When the work scope duplicate job cards are suppressed.
    *  Then a suppressing JIC task will suppress all the other JIC tasks.
    * </pre>
    *
    * "duplicated JIC tasks" are defined as non-historical tasks with the same definition and are
    * assigned to the work scope of the same work package.
    *
    *
    * *This scenario could have been a result of a duplicated JIC task at one revision of its
    * definition being assigned to a committed work package. Then the definition gets revised and
    * other duplicated tasks (at that later revision) get assigned to the same committed work
    * package. This would result in one JIC at first revision and one JIC at second revision and one
    * suppressed JIC at second revision. After which the "Update JIC to Latest Revision" feature is
    * used to update the first task to the later revision. Note: the "Update JIC to Latest Revision"
    * feature performs two steps; 1) forced baseline sync to update the revision of the first task
    * and 2) call {@linkplain STaskBean#suppressWorkScopeDuplicateJobCards()}. This test will assume
    * that the baseline sync was performed and then exercise the STaskBean method.
    *
    *
    * Exercises
    * {@linkplain STaskBean#suppressWorkScopeDuplicateJobCards(java.util.List, com.mxi.mx.core.key.HumanResourceKey)}
    *
    */
   @Test
   public void suppressJicOfReqRequirementWhenSuppressingJicExists() throws Exception {

      // Given a job card definition that is work scope enabled.
      TaskTaskKey jobCardDefn = Domain.createJobCardDefinition( defn -> {
         defn.setWorkScopeEnabled( true );
      } );

      // Given many job card tasks against the same inventory and based on the same definition.
      // One of the job card tasks is suppressing another and a third is neither suppressed or
      // suppressing.
      //
      // Notes:
      // - The inventory must allow synchronization (see
      // TaskSuppressionService.canJobCardBeUpdatedToLatestRevision()).
      // - The inventory must have a position description (see GetJobCardsFromSameTaskDefn.qrx).
      InventoryKey inv = Domain.createAircraft( acft -> {
         acft.setAllowSynchronization( true );
         acft.setPositionDescription( POSITION_DESCRIPTION );
      } );
      TaskKey jobCard1 = Domain.createJobCard( jic -> {
         jic.setDefinition( jobCardDefn );
         jic.setInventory( inv );
      } );
      TaskKey jobCard2 = Domain.createJobCard( jic -> {
         jic.setDefinition( jobCardDefn );
         jic.setInventory( inv );
         jic.setSuppressingJobCard( jobCard1 );
      } );
      TaskKey jobCard3 = Domain.createJobCard( jic -> {
         jic.setDefinition( jobCardDefn );
         jic.setInventory( inv );
      } );

      // Given REQ class requirement tasks, each assigned one of the job card tasks.
      //
      // Note; the REQ definitions must be associated to the JIC definitions
      // (see TaskSuppressionService.canJobCardBeUpdatedToLatestRevision()).
      TaskKey req1 = Domain.createRequirement( req -> {
         req.setTaskClass( REQ );
         req.addJobCard( jobCard1 );
         req.setDefinition( Domain.createRequirementDefinition(
               reqDefn -> reqDefn.addJobCardDefinition( jobCardDefn ) ) );
      } );
      TaskKey req2 = Domain.createRequirement( req -> {
         req.setTaskClass( REQ );
         req.addJobCard( jobCard2 );
         req.setDefinition( Domain.createRequirementDefinition(
               reqDefn -> reqDefn.addJobCardDefinition( jobCardDefn ) ) );
      } );
      TaskKey req3 = Domain.createRequirement( req -> {
         req.setTaskClass( REQ );
         req.addJobCard( jobCard3 );
         req.setDefinition( Domain.createRequirementDefinition(
               reqDefn -> reqDefn.addJobCardDefinition( jobCardDefn ) ) );
      } );

      // Given a committed work package with all the requirement tasks assigned to it and all the
      // job card tasks assigned to its work scope.
      Domain.createWorkPackage( wp -> {
         wp.setStatus( COMMIT );
         wp.addTask( req1 );
         wp.addTask( req2 );
         wp.addTask( req3 );
         wp.addWorkScopeTask( jobCard1 );
         wp.addWorkScopeTask( jobCard2 );
         wp.addWorkScopeTask( jobCard3 );
      } );

      // When the duplicate job cards in the work scope of the work package are requested to be
      // suppressed.
      new STaskBean().suppressWorkScopeDuplicateJobCards(
            Arrays.asList( jobCard1, jobCard2, jobCard3 ), Domain.createHumanResource() );

      // Then one of the duplicated job cards (randomly chosen) will suppress the other two.
      Map<TaskKey, TaskKey> map = new HashMap<>( 3 );
      map.put( jobCard1, schedStaskDao.findByPrimaryKey( jobCard1 ).getDupJicSchedKey() );
      map.put( jobCard2, schedStaskDao.findByPrimaryKey( jobCard2 ).getDupJicSchedKey() );
      map.put( jobCard3, schedStaskDao.findByPrimaryKey( jobCard3 ).getDupJicSchedKey() );
      List<TaskKey> suppressingJics =
            map.entrySet().stream().filter( entry -> null == entry.getValue() )
                  .map( entry -> entry.getKey() ).collect( Collectors.toList() );

      assertThat( "Unexpected number of suppressing JICs.", suppressingJics.size(), is( 1 ) );

      TaskKey suppressingJic = suppressingJics.get( 0 );
      assertNotNull( "Expecting suppressing JIC to be set.", suppressingJic );

      for ( Map.Entry<TaskKey, TaskKey> entry : map.entrySet() ) {
         if ( suppressingJic.equals( entry.getKey() ) ) {
            continue;
         }
         assertThat( "Unexpected suppressing job card for job card: " + entry.getKey(),
               entry.getValue(), is( suppressingJic ) );
      }
   }


   /**
    *
    * Verify that when there exists a suppressing JIC (and thus a suppressed JIC) and there exists
    * another duplicated JIC within the same work scope (assigned to a FOLLOW class requirement),
    * that the system will not suppress that other duplicated JIC.
    *
    * <pre>
    * Given many "duplicated JIC tasks", assigned to REQ class requirement tasks, within a committed work package.
    *   And one of the JIC tasks is suppressed by another JIC task.
    *   And there is yet another of the duplicated JIC task that is not suppressed and assigned to FOLLOW class requirement task.
    *  When the work scope duplicate job cards are suppressed.
    *  Then the JIC task that was not suppressed will remain not suppressed.
    * </pre>
    *
    * "duplicated JIC tasks" are defined as non-historical tasks with the same definition and are
    * assigned to the work scope of the same work package.
    *
    *
    * *This could have been a result of a duplicated JIC task at one revision of its definition
    * being assigned to a committed work package. Then the definition gets revised and other
    * duplicated tasks (at that later revision) get assigned to the same committed work package.
    * This would result in one JIC at first revision and one JIC at second revision and one
    * suppressed JIC at second revision. After which the "Update JIC to Latest Revision" feature is
    * used to update the first task to the later revision. Note: the "Update JIC to Latest Revision"
    * feature performs two steps; 1) forced baseline sync to update the revision of the first task
    * and 2) call {@linkplain STaskBean#suppressWorkScopeDuplicateJobCards()}. This test will assume
    * that the baseline sync was performed and then exercise the STaskBean method.
    *
    *
    * Exercises
    * {@linkplain STaskBean#suppressWorkScopeDuplicateJobCards(java.util.List, com.mxi.mx.core.key.HumanResourceKey)}
    *
    */
   @Test
   public void doNotSuppressJicOfFollowRequirementWhenSuppressingJicExists() throws Exception {

      // Given a job card definition that is work scope enabled.
      TaskTaskKey jobCardDefn = Domain.createJobCardDefinition( defn -> {
         defn.setWorkScopeEnabled( true );
      } );

      // Given many job card tasks against the same inventory and based on the same definition.
      // One of the job card tasks is suppressing another and a third is neither suppressed or
      // suppressing.
      //
      // Notes:
      // - The inventory must allow synchronization (see
      // TaskSuppressionService.canJobCardBeUpdatedToLatestRevision()).
      // - The inventory must have a position description (see GetJobCardsFromSameTaskDefn.qrx).
      InventoryKey inv = Domain.createAircraft( acft -> {
         acft.setAllowSynchronization( true );
         acft.setPositionDescription( POSITION_DESCRIPTION );
      } );
      TaskKey jobCard1 = Domain.createJobCard( jic -> {
         jic.setDefinition( jobCardDefn );
         jic.setInventory( inv );
      } );
      TaskKey jobCard2 = Domain.createJobCard( jic -> {
         jic.setDefinition( jobCardDefn );
         jic.setInventory( inv );
         jic.setSuppressingJobCard( jobCard1 );
      } );
      TaskKey jobCard3 = Domain.createJobCard( jic -> {
         jic.setDefinition( jobCardDefn );
         jic.setInventory( inv );
      } );

      // Given REQ class requirement tasks, each assigned one of the job card tasks.
      //
      // Note; the REQ definitions must be associated to the JIC definitions
      // (see TaskSuppressionService.canJobCardBeUpdatedToLatestRevision()).
      TaskKey req1 = Domain.createRequirement( req -> {
         req.setTaskClass( REQ );
         req.addJobCard( jobCard1 );
         req.setDefinition( Domain.createRequirementDefinition(
               reqDefn -> reqDefn.addJobCardDefinition( jobCardDefn ) ) );
      } );
      TaskKey req2 = Domain.createRequirement( req -> {
         req.setTaskClass( REQ );
         req.addJobCard( jobCard2 );
         req.setDefinition( Domain.createRequirementDefinition(
               reqDefn -> reqDefn.addJobCardDefinition( jobCardDefn ) ) );
      } );
      TaskKey req3 = Domain.createRequirement( req -> {
         req.setTaskClass( FOLLOW );
         req.addJobCard( jobCard3 );
         req.setDefinition( Domain.createRequirementDefinition(
               reqDefn -> reqDefn.addJobCardDefinition( jobCardDefn ) ) );
      } );

      // Given a committed work package with all the requirement tasks assigned to it and all the
      // job card tasks assigned to its work scope.
      Domain.createWorkPackage( wp -> {
         wp.setStatus( COMMIT );
         wp.addTask( req1 );
         wp.addTask( req2 );
         wp.addTask( req3 );
         wp.addWorkScopeTask( jobCard1 );
         wp.addWorkScopeTask( jobCard2 );
         wp.addWorkScopeTask( jobCard3 );
      } );

      // When the duplicate job cards in the work scope of the work package are requested to be
      // suppressed.
      new STaskBean().suppressWorkScopeDuplicateJobCards(
            Arrays.asList( jobCard1, jobCard2, jobCard3 ), Domain.createHumanResource() );

      // Then the second job card remains suppressed by the first job card.
      assertThat( "Unexpected suppressing job card for second job card.",
            schedStaskDao.findByPrimaryKey( jobCard2 ).getDupJicSchedKey(), is( jobCard1 ) );

      // Then the third job card, whose parent is a FOLLOW requirement, remains NOT suppressed.
      assertNull( "Third job card is unexpectedly suppressed by job card.",
            schedStaskDao.findByPrimaryKey( jobCard3 ).getDupJicSchedKey() );
   }


   @Test
   public void
         whenExtendDeadlineForATaskThenTaskDeadLineExtendedEventAndTaskDrivingDeadLineRescheduledEventShouldBePublished()
               throws MxException, TriggerException {
      // ARRANGE
      final HumanResourceKey authHr = Domain.createHumanResource();

      final PartNoKey acftPart = Domain.createPart();

      final AssemblyKey aircraftAssemblyKey = Domain
            .createAircraftAssembly( aircraftAssembly -> aircraftAssembly.setRootConfigurationSlot(
                  rootConfigSlot -> rootConfigSlot.addPartGroup( partGroup -> {
                     partGroup.setInventoryClass( RefInvClassKey.ACFT );
                     partGroup.addPart( acftPart );
                  } ) ) );

      final ConfigSlotKey cnfigSlot = Domain.readRootConfigurationSlot( aircraftAssemblyKey );

      final InventoryKey aircraftKey = Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( aircraftAssemblyKey );
         aircraft.setPart( acftPart );
         aircraft.addUsage( DataTypeKey.HOURS, BigDecimal.ZERO );
         aircraft.setForecastModel( Domain.createForecastModel( forecastModel -> {
            forecastModel.addRange( 1, 1, DataTypeKey.HOURS, DEFAULT_FC_MODEL_RATE );
         } ) );
      } );

      final TaskTaskKey reqDefinition = Domain.createRequirementDefinition( reqDefn -> {
         reqDefn.againstConfigurationSlot( cnfigSlot );
         reqDefn.setRecurring( false );
         reqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
         reqDefn.setOnCondition( true );
         reqDefn.setScheduledFromManufacturedDate();
         reqDefn.addSchedulingRule( DataTypeKey.HOURS, BigDecimal.TEN );
         reqDefn.setTaskClass( RefTaskClassKey.REQ );
      } );

      TaskKey taskKey = Domain.createRequirement( task -> {
         task.setDefinition( reqDefinition );
         task.setStatus( RefEventStatusKey.ACTV );
         task.setInventory( aircraftKey );
         task.addUsageDeadline( DataTypeKey.HOURS, BigDecimal.TEN, null );
         task.addCalendarDeadline( CDY, CUSTOM, TEN, new BigDecimal( ORIGINAL_DEADLINE_INTERVAL ),
               DateUtils.addDays( ORIGINAL_DEADLINE_START_DATE, ORIGINAL_DEADLINE_INTERVAL ),
               ORIGINAL_DEADLINE_START_DATE );
      } );

      String note = "Test Note";

      DeadlineExtensionTO deadlineExtension = new DeadlineExtensionTO();
      deadlineExtension.setNote( note );

      // update the deviation on each deadline
      deadlineExtension.addCalendarUpdate( DataTypeKey.CDY, 1 );

      axonDomainEventDao.purgeAll();

      // ACT
      new STaskBean().extendDeadline( taskKey, deadlineExtension, authHr );

      // ASSERT TaskDeadLineExtendedEvent using the EventBus
      assertEquals(
            new TaskDeadlineExtendedEvent( taskKey, deadlineExtension.getCalendarDeadlineUpdates(),
                  deadlineExtension.getUsageDeadlineUpdates(), deadlineExtension.getRefernceNo(),
                  deadlineExtension.getNote(), authHr ),
            eventBus.getEventMessages().get( 0 ).getPayload() );

      // ASSERT TaskDrivingDeadLineRescheduledEvent thrown from PLSQL
      final QuerySet querySet = axonDomainEventDao.findAll();
      assertEquals( "Unexpected number of events.", 1, querySet.getRowCount() );
   }


   @Test
   public void
         whenExtendDeadlineForMainRepetitiveTaskThenEventShouldAlsoBePublishedForForescastTask()
               throws Exception {
      // ARRANGE
      final String note = "superman is powerful than spiderman";
      final TaskKey activeTaskKey = Domain.createRepetitiveTask( repetitiveTask -> {
         repetitiveTask.setStatus( RefEventStatusKey.ACTV );
         repetitiveTask.setInventory( Domain.createAircraft() );
      } );
      final TaskKey forecastTaskKey = Domain.createRepetitiveTask( repetitiveTask -> {
         repetitiveTask.setStatus( RefEventStatusKey.FORECAST );
         repetitiveTask.setInventory( Domain.createAircraft() );
         repetitiveTask.setPreviousTask( activeTaskKey );
      } );

      DeadlineExtensionTO deadlineExtension = new DeadlineExtensionTO();
      deadlineExtension.setNote( note );

      // update the deviation on each deadline
      deadlineExtension.addCalendarUpdate( DataTypeKey.CDY, 1 );

      axonDomainEventDao.purgeAll();
      QuerySet querySet =
            axonDomainEventDao.findByPayLoadType( TaskDrivingDeadlineRescheduledEvent.class );
      assertEquals( 0, querySet.getRowCount() );

      // ACT
      new STaskBean().extendDeadline( activeTaskKey, deadlineExtension, HumanResourceKey.ADMIN );

      // ASSERT
      // verify that extended event is published
      assertEquals(
            new TaskDeadlineExtendedEvent( activeTaskKey,
                  deadlineExtension.getCalendarDeadlineUpdates(),
                  deadlineExtension.getUsageDeadlineUpdates(), deadlineExtension.getRefernceNo(),
                  deadlineExtension.getNote(), HumanResourceKey.ADMIN ),
            eventBus.getEventMessages().get( 0 ).getPayload() );

      // verify that deadline rescheduled is published for both active and forecast tasks.
      querySet = axonDomainEventDao.findByPayLoadType( TaskDrivingDeadlineRescheduledEvent.class );
      assertEquals( "Unexpected number of events.", 2, querySet.getRowCount() );

      // verify that an event is published for forecast task
      List<TaskKey> taskKeys = new ArrayList<TaskKey>();
      while ( querySet.next() ) {
         TaskDrivingDeadlineRescheduledEvent taskDrivingDeadlineRescheduledEvent =
               objectMapper.readValue( querySet.getString( AxonDomainEventDao.PAYLOAD_COLUMN ),
                     TaskDrivingDeadlineRescheduledEvent.class );
         taskKeys.add( taskDrivingDeadlineRescheduledEvent.getTaskKey() );
      }
      assertTrue( taskKeys.contains( forecastTaskKey ) );
   }


   @Test
   public void whenToggleAdhocTaskSoftDeadlineThenTaskDrivingDeadlineRescheduledEventIsPublished()
         throws Exception {

      // ARRANGE
      Date startDate = Instant.now().toDate();
      Date dueDate = DateUtils.addDays( startDate, 5 );

      TaskKey taskKey = Domain.createAdhocTask( task -> {
         task.addDeadline( deadline -> {
            deadline.setStartDate( startDate );
            deadline.setDueDate( dueDate );
         } );
         task.setSoftDeadline( true );
         task.setInventory( Domain.createAircraft() );
      } );

      QuerySet querySet =
            axonDomainEventDao.findByPayLoadType( TaskDrivingDeadlineRescheduledEvent.class );
      assertEquals( 0, querySet.getRowCount() );

      // ACT
      new STaskBean().toggleSoftDeadline( taskKey, HR );

      // ASSERT

      // verify that deadline rescheduled is published for both active and forecast tasks.
      querySet = axonDomainEventDao.findByPayLoadType( TaskDrivingDeadlineRescheduledEvent.class );
      assertEquals( "Unexpected number of events.", 1, querySet.getRowCount() );

      // verify that an event is published for forecast task
      List<TaskKey> taskKeys = new ArrayList<TaskKey>();
      while ( querySet.next() ) {
         TaskDrivingDeadlineRescheduledEvent taskDrivingDeadlineRescheduledEvent =
               objectMapper.readValue( querySet.getString( AxonDomainEventDao.PAYLOAD_COLUMN ),
                     TaskDrivingDeadlineRescheduledEvent.class );
         taskKeys.add( taskDrivingDeadlineRescheduledEvent.getTaskKey() );
      }
      assertTrue(
            "The TaskDrivingDeadlineRescheduledEvent should be published from PLSQL when soft deadline is updated",
            taskKeys.contains( taskKey ) );
   }


   @Test
   public void
         whenToggleCorrectiveTaskSoftDeadlineThenTaskDrivingDeadlineRescheduledEventIsPublished()
               throws Exception {
      // ARRANGE
      Date startDate = Instant.now().toDate();
      Date dueDate = DateUtils.addDays( startDate, 5 );

      TaskKey taskKey = Domain.createCorrectiveTask( task -> {
         task.addDeadline( deadline -> {
            deadline.setStartDate( startDate );
            deadline.setDueDate( dueDate );
         } );
         task.setInventory( Domain.createAircraft() );
         task.setFaultKey( Domain.createFault() );
      } );

      QuerySet querySet =
            axonDomainEventDao.findByPayLoadType( TaskDrivingDeadlineRescheduledEvent.class );
      assertEquals( 0, querySet.getRowCount() );

      // ACT
      new STaskBean().toggleSoftDeadline( taskKey, HR );

      // ASSERT

      // verify that deadline rescheduled is published for both active and forecast tasks.
      querySet = axonDomainEventDao.findByPayLoadType( TaskDrivingDeadlineRescheduledEvent.class );
      assertEquals( "Unexpected number of events.", 1, querySet.getRowCount() );

      // verify that an event is published for forecast task
      List<TaskKey> taskKeys = new ArrayList<TaskKey>();
      while ( querySet.next() ) {
         TaskDrivingDeadlineRescheduledEvent taskDrivingDeadlineRescheduledEvent =
               objectMapper.readValue( querySet.getString( AxonDomainEventDao.PAYLOAD_COLUMN ),
                     TaskDrivingDeadlineRescheduledEvent.class );
         taskKeys.add( taskDrivingDeadlineRescheduledEvent.getTaskKey() );
      }
      assertTrue(
            "The TaskDrivingDeadlineRescheduledEvent should be published from PLSQL when soft deadline is updated",
            taskKeys.contains( taskKey ) );
   }


   @Test
   public void whenCompleteTaskThenTasksCompletedEventPublished() throws Exception {

      // ARRANGE
      final Date COMPLETION_DATE = new Date();
      final UserTransaction userTransaction = mock( UserTransaction.class );

      final PartNoKey aircraftPart = Domain.createPart();
      final AssemblyKey aircraftAssembly = Domain.createAircraftAssembly( aAircraftAssembly -> {
         aAircraftAssembly.setRootConfigurationSlot( aRootConfigSlot -> {
            aRootConfigSlot.addPartGroup( aRootCsPartGroup -> {
               aRootCsPartGroup.setInventoryClass( ACFT );
               aRootCsPartGroup.addPart( aircraftPart );
            } );
         } );
      } );

      final InventoryKey aircraftInventory = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssembly( aircraftAssembly );
         aAircraft.setPart( aircraftPart );
         aAircraft.addSystem( "SYSTEM_NAME" );
      } );

      final TaskKey taskKey = Domain.createAdhocTask( task -> {
         task.setStatus( RefEventStatusKey.ACTV );
         task.setInventory( aircraftInventory );
      } );

      Domain.createWorkPackage( aWorkPackage -> {
         aWorkPackage.addTask( taskKey );
         aWorkPackage.setStatus( RefEventStatusKey.IN_WORK );
      } );

      SchedStaskTable schedStaskTable = schedStaskDao.findByPrimaryKey( taskKey );

      // ACT
      STaskBean sTaskBean = new STaskBean();
      sTaskBean.setSessionContext( new SessionContextFake() );
      sTaskBean.complete( null, new TaskKey[] { taskKey }, COMPLETION_DATE, hr, true,
            userTransaction );

      // ASSERT TaskCompletedEvent published
      TaskCompletedEvent testTaskCompletedEvent = new TaskCompletedEvent( schedStaskTable.getPk(),
            COMPLETION_DATE, hr, schedStaskTable.getTaskClass() );

      List<EventMessage<?>> taskCompletedEvents = eventBus.getEventMessages().stream()
            .filter( o -> o.getPayload().equals( testTaskCompletedEvent ) )
            .collect( Collectors.toList() );

      assertEquals( "Task completed event is not published correctly", 1,
            taskCompletedEvents.size() );

   }


   @Test
   public void
         whenPackageAndCompleteActiveRepetitiveTaskThenPublishTaskCompletedEventForTaskAndParent()
               throws Exception {

      // ARRANGE
      final Date DEADLINE_DATE = new Date();
      final Date COMPLETION_DATE = new Date();
      final UserTransaction userTransaction = mock( UserTransaction.class );

      final PartNoKey aircraftPart = Domain.createPart();
      final AssemblyKey aircraftAssembly = Domain.createAircraftAssembly( aAircraftAssembly -> {
         aAircraftAssembly.setRootConfigurationSlot( aRootConfigSlot -> {
            aRootConfigSlot.addPartGroup( aRootCsPartGroup -> {
               aRootCsPartGroup.setInventoryClass( ACFT );
               aRootCsPartGroup.addPart( aircraftPart );
            } );
         } );
      } );

      final InventoryKey aircraftInventory = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssembly( aircraftAssembly );
         aAircraft.setPart( aircraftPart );
         aAircraft.addSystem( "SYSTEM_NAME" );
      } );

      final BigDecimal REPEAT_INTERVAL = BigDecimal.TEN;

      final TaskKey activeRepetitiveTaskKey = Domain.createRepetitiveTask( repetitiveTask -> {
         repetitiveTask.setStatus( RefEventStatusKey.ACTV );
         repetitiveTask.setInventory( aircraftInventory );
         repetitiveTask.setRepeatInterval( REPEAT_INTERVAL );
      } );

      final TaskKey forecastRepetitiveTaskKey = Domain.createRepetitiveTask( repetitiveTask -> {
         repetitiveTask.setStatus( RefEventStatusKey.FORECAST );
         repetitiveTask.setInventory( aircraftInventory );
         repetitiveTask.setRepeatInterval( REPEAT_INTERVAL );
         repetitiveTask.setPreviousTask( activeRepetitiveTaskKey );
      } );

      Domain.createFault( aFault -> {
         aFault.setCorrectiveTask( Domain.createCorrectiveTask() );
         aFault.addRepetitiveTask( activeRepetitiveTaskKey );
         aFault.addRepetitiveTask( forecastRepetitiveTaskKey );
      } );

      final String LOCATION_CODE = "LOCATION_CODE";
      final String SUPPLY_LOCATION_CODE = "SUPPLY_LOCATION_CODE";

      LocationKey supplyLocationKey = createLocation( supplyLoc -> {
         supplyLoc.setCode( SUPPLY_LOCATION_CODE );
         supplyLoc.setIsSupplyLocation( true );
         supplyLoc.setType( AIRPORT );
      } );

      // Give a location that has a non-scheduling location type and uses the supply location.
      createLocation( loc -> {
         loc.setCode( LOCATION_CODE );
         loc.setType( RefLocTypeKey.LINE );
         loc.setSupplyLocation( supplyLocationKey );
      } );

      // Create quick check for the task
      ScheduleInternalTO scheduleInternalTO = new ScheduleInternalTO();
      scheduleInternalTO.setSchedDates( DEADLINE_DATE, DEADLINE_DATE );
      scheduleInternalTO.setWorkLocationCd( SUPPLY_LOCATION_CODE );
      scheduleInternalTO.setWorkOrderNo( "80" );
      CreateQuickCheckTO createQuickCheckTO = new CreateQuickCheckTO();
      createQuickCheckTO.setCheck( null );
      createQuickCheckTO.setCheckName( "QC" );
      createQuickCheckTO.setScheduleInternalTO( scheduleInternalTO );

      // ACT
      STaskBean sTaskBean = new STaskBean();
      sTaskBean.setSessionContext( new SessionContextFake() );

      sTaskBean.createQuickCheck( new TaskKey[] { activeRepetitiveTaskKey }, createQuickCheckTO, hr,
            null );
      sTaskBean.complete( null, new TaskKey[] { activeRepetitiveTaskKey }, COMPLETION_DATE, hr,
            true, userTransaction );

      SchedStaskTable activeSchedStaskTable =
            schedStaskDao.findByPrimaryKey( activeRepetitiveTaskKey );
      SchedStaskTable activeSchedStaskTableCheck =
            schedStaskDao.findByPrimaryKey( activeSchedStaskTable.getWoTaskCompletedOn() );

      // ASSERT TaskCompletedEvent published for active repetitive task
      TaskCompletedEvent testTaskCompletedEvent = new TaskCompletedEvent( activeRepetitiveTaskKey,
            COMPLETION_DATE, hr, activeSchedStaskTable.getTaskClass() );
      TaskCompletedEvent testTaskCompletedEventCheck =
            new TaskCompletedEvent( activeSchedStaskTable.getWoTaskCompletedOn(), COMPLETION_DATE,
                  hr, activeSchedStaskTableCheck.getTaskClass() );
      List<EventMessage<?>> taskCompletedEvents = eventBus.getEventMessages().stream()
            .filter( o -> o.getPayload().equals( testTaskCompletedEvent ) )
            .collect( Collectors.toList() );
      List<EventMessage<?>> taskCompletedEventsCheck = eventBus.getEventMessages().stream()
            .filter( o -> o.getPayload().equals( testTaskCompletedEventCheck ) )
            .collect( Collectors.toList() );

      assertEquals( "Task completed event is not published correctly for task", 1,
            taskCompletedEvents.size() );
      assertEquals( "Task completed event is not published correctly for Work package", 1,
            taskCompletedEventsCheck.size() );

   }


   /**
    * This test is testing creating quick work package for requirement.
    *
    * <pre>
    * Given a requirement on an aircraft.
    * When create a quick work package for the requirement.
    * Then verify the quick work package is created properly.
    * And the requirement is assigned to it.
    * And its status is IN_WORK.
    * And its usage snapshot source is MAINTENIX.
    * </pre>
    */
   @Test
   public void itCreatesQuickWorkPackage() throws Exception {
      String checkName = "checkName";
      String lineLocationCd = "lineLocation";
      String aircraftLocationCd = "aircraftLocation";
      Date date = new Date();
      PartNoKey partNo = Domain.createPart();
      InventoryKey aircraft = Domain.createAircraft( ac -> {
         ac.setPart( partNo );
         ac.addUsage( DataTypeKey.CYCLES, BigDecimal.ONE );
      } );
      TaskKey requirement = Domain.createRequirement( rq -> {
         rq.setInventory( aircraft );
      } );

      LocationKey airportLocation = Domain.createLocation( airport -> {
         airport.setType( RefLocTypeKey.AIRPORT );
         airport.setIsSupplyLocation( true );
         airport.setCode( aircraftLocationCd );
      } );
      Domain.createLocation( line -> {
         line.setType( RefLocTypeKey.LINE );
         line.setParent( airportLocation );
         line.setCode( lineLocationCd );
         line.setSupplyLocation( airportLocation );
      } );

      TaskKey taskKeys[] = { requirement };

      CreateQuickCheckTO createQuickCheckTO = new CreateQuickCheckTO();
      ScheduleInternalTO scheduleInternalTO = new ScheduleInternalTO();
      scheduleInternalTO.setSchedDates( date, date );
      createQuickCheckTO.setCheckName( checkName );
      scheduleInternalTO.setWorkLocationCd( lineLocationCd );
      createQuickCheckTO.setScheduleInternalTO( scheduleInternalTO );
      STaskBean staskBean = new STaskBean();
      staskBean.setSessionContext( iSessionContext );
      EventKey workPackageEventCreated =
            staskBean.createQuickCheck( taskKeys, createQuickCheckTO, HR, null ).getEventKey();

      EventKey workPackageEventExpected =
            evtEventDao.findByPrimaryKey( requirement.getEventKey() ).getHEvent();
      RefEventStatusKey workPackageStatus =
            evtEventDao.findByPrimaryKey( workPackageEventCreated ).getEventStatus();

      RefUsgSnapshotSrcTypeKey usageSource =
            EvtInvUsageTable.findByPrimaryKey( new EventInventoryUsageKey(
                  EvtInvTable.findByEventAndInventory( workPackageEventCreated, aircraft ).getPk(),
                  DataTypeKey.CYCLES ) ).getUsageSnapshotSourceType();

      assertEquals( "Requirement is not assign to proper work package", workPackageEventExpected,
            workPackageEventCreated );
      assertEquals( "Work package satus is not set to IN_WORK.", RefEventStatusKey.IN_WORK,
            workPackageStatus );
      assertEquals( "Usage source is not set properly.", RefUsgSnapshotSrcTypeKey.MAINTENIX,
            usageSource );

   }


   private MxOrderNumberGenerator mockOrderNumberGenerator() {
      return new MxOrderNumberGenerator() {

         @Override
         public String getRepairOrderNumber( TaskKey aTaskKey ) {
            throw new UnsupportedOperationException();
         }


         @Override
         public String getWorkOrderNumber( TaskKey aTaskKey ) {
            return ( "WO - " + aTaskKey.getId() );
         }

      };

   }

}
