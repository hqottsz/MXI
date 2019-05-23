package com.mxi.mx.core.services.stask.details;

import static com.mxi.mx.core.key.RefEventStatusKey.COMMIT;
import static com.mxi.mx.core.key.RefInvClassKey.ACFT;
import static com.mxi.mx.core.key.RefTaskClassKey.REQ;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.UserTransaction;

import org.apache.commons.lang3.RandomStringUtils;
import org.axonframework.eventhandling.EventMessage;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.AxonDomainEventDao;
import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.exception.StringTooLongException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.configuration.axon.RecordingEventBus;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefStageReasonKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.production.task.domain.TaskAssignedToWorkPackageEvent;
import com.mxi.mx.core.production.task.domain.TaskDeletedEvent;
import com.mxi.mx.core.production.task.domain.TaskUnassignedFromWorkPackageEvent;
import com.mxi.mx.core.table.evt.EvtEventDao;
import com.mxi.mx.core.table.evt.EvtStageDao;
import com.mxi.mx.core.table.org.OrgHr;
import com.mxi.mx.core.table.sched.SchedStaskDao;


/**
 * Integration test for {@linkplain DetailsService}
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class DetailsServiceTest {

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule injectionOverrideRule = new InjectionOverrideRule();

   private DetailsService detailsService;
   private TaskKey taskKey;
   private HumanResourceKey hr;
   private int userId;
   private RecordingEventBus eventBus;

   private static final HumanResourceKey HR = new HumanResourceKey( 0, 3 );
   private static final int MAX_ALLOWED_LENGTH = 2000;
   private static final String POSITION_DESCRIPTION = "POSITION_DESCRIPTION";
   private static final boolean NA_OPERATION_ALLOWED_FOR_LRP = true;
   private static final String NA_NOTE = null;
   private static final String NA_REASON = null;

   private static SchedStaskDao schedStaskDao;
   private static EvtEventDao evtEventDao;
   private static EvtStageDao evtStageDao;


   @Before
   public void before() {
      schedStaskDao = InjectorContainer.get().getInstance( SchedStaskDao.class );
      evtEventDao = InjectorContainer.get().getInstance( EvtEventDao.class );
      evtStageDao = InjectorContainer.get().getInstance( EvtStageDao.class );

      hr = Domain.createHumanResource();
      userId = OrgHr.findByPrimaryKey( hr ).getUserId();
      eventBus = injectionOverrideRule.select( RecordingEventBus.class ).get();

      // User parm ALLOW_ASSIGN_LRP_ITEM_FOR_INWORK_WP needed for DetailsService.setParentTask().
      UserParameters.setInstance( userId, "LOGIC", new UserParametersFake( userId, "LOGIC" ) );

   }


   @After
   public void after() {
      UserParameters.setInstance( userId, "LOGIC", null );
   }


   @Test( expected = StringTooLongException.class )
   public void addStatusChangeNote_NoteTooLong() throws Exception {

      loadData();

      // data set up
      final String appendNote = RandomStringUtils.randomAlphanumeric( MAX_ALLOWED_LENGTH + 1 );

      // service call
      detailsService.addStatusChangeNote( HR, appendNote, false );
   }


   @Test
   public void addStatusChangeNote_UserNote() throws Exception {

      loadData();

      // data set up
      final String appendNote = RandomStringUtils.randomAlphanumeric( MAX_ALLOWED_LENGTH - 1 );

      // record the event status before the note
      String eventStatusCdPre =
            evtEventDao.findByPrimaryKey( taskKey.getEventKey() ).getEventStatusCd();
      boolean eventHistBoolPre =
            evtEventDao.findByPrimaryKey( taskKey.getEventKey() ).getHistBool();

      // service call
      detailsService.addStatusChangeNote( HR, appendNote, true );

      // assertions
      EventKey eventKey = evtEventDao.findByPrimaryKey( taskKey.getEventKey() ).getPk();
      DataSet results = evtStageDao.getStageSnapshot( eventKey );

      assertEquals( 1, results.getRowCount() );
      results.first();
      // Check that all input values match the expected output values
      assertEquals( appendNote, results.getString( EvtStageDao.COL_STAGE_NOTE ) );
      assertEquals( 0, results.getInt( EvtStageDao.COL_SYSTEM_BOOL ) );
      assertEquals( HR.getDbId(), results.getInt( EvtStageDao.COL_HR_DB_ID ) );
      assertEquals( HR.getId(), results.getInt( EvtStageDao.COL_HR_ID ) );

      // Check that the status of the event and historical state have not changed
      assertEquals( eventStatusCdPre,
            evtEventDao.findByPrimaryKey( taskKey.getEventKey() ).getEventStatusCd() );
      assertEquals( eventHistBoolPre,
            evtEventDao.findByPrimaryKey( taskKey.getEventKey() ).getHistBool() );
   }


   @Test
   public void addStatusChangeNote_SystemNote() throws Exception {

      loadData();

      // data set up
      final String appendNote = RandomStringUtils.randomAlphanumeric( MAX_ALLOWED_LENGTH - 1 );

      // record the event status before the note
      String eventStatusCdPre =
            evtEventDao.findByPrimaryKey( taskKey.getEventKey() ).getEventStatusCd();
      boolean eventHistBoolPre =
            evtEventDao.findByPrimaryKey( taskKey.getEventKey() ).getHistBool();

      // service call
      detailsService.addStatusChangeNote( HR, appendNote, false );

      // assertions
      EventKey eventKey = evtEventDao.findByPrimaryKey( taskKey.getEventKey() ).getPk();
      DataSet results = evtStageDao.getStageSnapshot( eventKey );

      assertEquals( 1, results.getRowCount() );
      results.first();
      // Check that all input values match the expected output values
      assertEquals( appendNote, results.getString( EvtStageDao.COL_STAGE_NOTE ) );
      assertEquals( 1, results.getInt( EvtStageDao.COL_SYSTEM_BOOL ) );
      assertEquals( HR.getDbId(), results.getInt( EvtStageDao.COL_HR_DB_ID ) );
      assertEquals( HR.getId(), results.getInt( EvtStageDao.COL_HR_ID ) );

      // Check that the status of the event and historical state have not changed
      assertEquals( eventStatusCdPre,
            evtEventDao.findByPrimaryKey( taskKey.getEventKey() ).getEventStatusCd() );
      assertEquals( eventHistBoolPre,
            evtEventDao.findByPrimaryKey( taskKey.getEventKey() ).getHistBool() );
   }


   /**
    *
    * Verify that unassigning a requirement from a work package that has a suppressing job card
    * causes one of the other duplicate job cards to become the suppressing job card.
    *
    * Duplicate job cards are defined as having the same definition, which is work scope enabled,
    * against the same inventory, and all belonging to the work scope of the same work package.
    *
    */
   @Test
   public void newDupJicBecomesSuppressingJicWhenReqOfSuppressingJicIsUnassingedFromWp()
         throws Exception {

      // Given a JIC definition that is work scope enabled.
      TaskTaskKey jobCardDefn = Domain.createJobCardDefinition( defn -> {
         defn.setWorkScopeEnabled( true );
      } );

      // Given many duplicate JIC tasks against the same inventory and based on the same definition.
      // One of the JIC tasks is suppressing the other JIC tasks.
      //
      // Notes:
      // - The inventory must allow synchronization (see
      // TaskSuppressionService.canJobCardBeUpdatedToLatestRevision()).
      // - The inventory must have a position description (see GetJobCardsFromSameTaskDefn.qrx).
      InventoryKey inv = Domain.createAircraft( acft -> {
         acft.setAllowSynchronization( true );
         acft.setPositionDescription( POSITION_DESCRIPTION );
      } );
      TaskKey suppressingJic = Domain.createJobCard( jic -> {
         jic.setDefinition( jobCardDefn );
         jic.setInventory( inv );
      } );
      TaskKey jic2 = Domain.createJobCard( jic -> {
         jic.setDefinition( jobCardDefn );
         jic.setInventory( inv );
         jic.setSuppressingJobCard( suppressingJic );
      } );
      TaskKey jic3 = Domain.createJobCard( jic -> {
         jic.setDefinition( jobCardDefn );
         jic.setInventory( inv );
         jic.setSuppressingJobCard( suppressingJic );
      } );

      // Given REQ class requirement tasks, each assigned one of the job card tasks.
      //
      // Note; the REQ definitions must be associated to the JIC definitions
      // (see TaskSuppressionService.canJobCardBeUpdatedToLatestRevision()).
      TaskKey reqWithSuppressingJic = Domain.createRequirement( req -> {
         req.setTaskClass( REQ );
         req.addJobCard( suppressingJic );
         req.setDefinition( Domain.createRequirementDefinition(
               reqDefn -> reqDefn.addJobCardDefinition( jobCardDefn ) ) );
      } );
      TaskKey req2 = Domain.createRequirement( req -> {
         req.setTaskClass( REQ );
         req.addJobCard( jic2 );
         req.setDefinition( Domain.createRequirementDefinition(
               reqDefn -> reqDefn.addJobCardDefinition( jobCardDefn ) ) );
      } );
      TaskKey req3 = Domain.createRequirement( req -> {
         req.setTaskClass( REQ );
         req.addJobCard( jic3 );
         req.setDefinition( Domain.createRequirementDefinition(
               reqDefn -> reqDefn.addJobCardDefinition( jobCardDefn ) ) );
      } );

      // Given a committed work package with all the requirement tasks assigned to it and all the
      // job card tasks assigned to its work scope.
      Domain.createWorkPackage( wp -> {
         wp.setStatus( COMMIT );
         wp.addTask( reqWithSuppressingJic );
         wp.addTask( req2 );
         wp.addTask( req3 );
         wp.addWorkScopeTask( suppressingJic );
         wp.addWorkScopeTask( jic2 );
         wp.addWorkScopeTask( jic3 );
      } );

      // When the requirement task of the suppressing JIC task is unassigned from the work package.
      new DetailsService( reqWithSuppressingJic ).unassignTask( hr, NA_REASON, NA_NOTE,
            NA_OPERATION_ALLOWED_FOR_LRP );

      // Then another JIC task becomes the suppressing task for the other JIC tasks.
      assertThat( "Suppressing JIC has unexpected suppressing JIC",
            schedStaskDao.findByPrimaryKey( jic2 ).getDupJicSchedKey(), is( ( TaskKey ) null ) );
      assertThat( "Unexpected suppressing job card for third job card.",
            schedStaskDao.findByPrimaryKey( jic3 ).getDupJicSchedKey(), is( jic2 ) );
   }


   @Test
   public void
         whenUnanassigningATaskFromWorkPackageThenTaskUnassignedFromWorkPackageEventIsPublished()
               throws Exception {

      // ARRANGE
      TaskKey taskKey = Domain.createRequirement( aReq -> {
         aReq.setTaskClass( RefTaskClassKey.ADHOC );
      } );

      TaskKey workPackageKey = Domain.createWorkPackage( aWp -> {
         aWp.setStatus( RefEventStatusKey.ACTV );
         aWp.addTask( taskKey );
      } );

      // ACT
      // When the task is unassigned from the work package.
      new DetailsService( taskKey ).unassignTask( hr, NA_REASON, NA_NOTE,
            NA_OPERATION_ALLOWED_FOR_LRP );

      // ASSERT
      assertEquals( "Exception on task unassignment.", null,
            evtEventDao.findByPrimaryKey( taskKey ).getNhEvent() );

      TaskUnassignedFromWorkPackageEvent taskUnassignedFromWorkPackageEvent =
            new TaskUnassignedFromWorkPackageEvent( taskKey, workPackageKey, hr );

      List<EventMessage<?>> taskUnassignedFromWorkPackageEvents = eventBus.getEventMessages()
            .stream().filter( o -> o.getPayload().equals( taskUnassignedFromWorkPackageEvent ) )
            .collect( Collectors.toList() );
      assertEquals( "Task unassigned from workPackage event is not published correctly", 1,
            taskUnassignedFromWorkPackageEvents.size() );

   }


   @Test
   public void
         whenUnanassigningABlockTaskWithTwoRequirementsFromWorkPackageThenTaskUnassignedFromWorkPackageEventIsPublished()
               throws Exception {

      // ARRANGE
      TaskKey taskKey1 = Domain.createRequirement( aReq -> {
         aReq.setTaskClass( RefTaskClassKey.ADHOC );
      } );

      TaskKey taskKey2 = Domain.createRequirement( aReq -> {
         aReq.setTaskClass( RefTaskClassKey.ADHOC );
      } );

      TaskKey blockTaskKey = Domain.createBlock( aReq -> {
         aReq.addRequirement( taskKey1 );
         aReq.addRequirement( taskKey2 );
      } );

      TaskKey workPackageKey = Domain.createWorkPackage( aWp -> {
         aWp.setStatus( RefEventStatusKey.ACTV );
         aWp.addTask( blockTaskKey );
      } );

      // ACT
      // When the task is unassigned from the work package.
      new DetailsService( blockTaskKey ).unassignTask( hr, NA_REASON, NA_NOTE,
            NA_OPERATION_ALLOWED_FOR_LRP );

      // ASSERT
      assertEquals( "Exception on task unassignment.", null,
            evtEventDao.findByPrimaryKey( blockTaskKey ).getNhEvent() );

      TaskUnassignedFromWorkPackageEvent taskUnassignedFromWorkPackageEvent =
            new TaskUnassignedFromWorkPackageEvent( blockTaskKey, workPackageKey, hr );

      List<EventMessage<?>> taskUnassignedFromWorkPackageEvents = eventBus.getEventMessages()
            .stream().filter( o -> o.getPayload().equals( taskUnassignedFromWorkPackageEvent ) )
            .collect( Collectors.toList() );
      assertEquals( "Task unassigned from workPackage event is not published correctly", 1,
            taskUnassignedFromWorkPackageEvents.size() );

   }


   @Test
   public void
         whenUnassignAReqTaskFromABlockTaskThenTaskUnassignedFromWorkPackageEventWillNotPublished()
               throws MxException, TriggerException {
      // ARRANGE
      TaskKey reqKey = Domain.createRequirement();
      Domain.createBlock( block -> {
         block.addRequirement( reqKey );
      } );

      // ACT
      new DetailsService( reqKey ).unassignTask( hr, NA_REASON, NA_NOTE,
            NA_OPERATION_ALLOWED_FOR_LRP );

      // ASSERT
      assertEquals( "Exception on task unassignment.", null,
            evtEventDao.findByPrimaryKey( reqKey ).getNhEvent() );

      List<EventMessage<?>> taskUnassignedFromWorkPackageEvents =
            eventBus.getEventMessages().stream()
                  .filter( evt -> evt.getPayload() instanceof TaskUnassignedFromWorkPackageEvent )
                  .collect( Collectors.toList() );
      assertThat( "Task unassigned from workPackage event should not be published",
            taskUnassignedFromWorkPackageEvents.size(), is( 0 ) );

   }


   /**
    *
    * Verify that unassigning a requirement from a work package that has a suppressed job card
    * causes that job card to no longer be suppressed.
    *
    */
   @Test
   public void unassigningReqWithSuppressedJicCausesJicToNoLongerBeSuppressed() throws Exception {

      // Given a JIC definition that is work scope enabled.
      TaskTaskKey jobCardDefn = Domain.createJobCardDefinition( defn -> {
         defn.setWorkScopeEnabled( true );
      } );

      // Given many duplicate JIC tasks against the same inventory and based on the same definition.
      // One of the JIC tasks is suppressing the other JIC tasks.
      //
      // Notes:
      // - The inventory must allow synchronization (see
      // TaskSuppressionService.canJobCardBeUpdatedToLatestRevision()).
      // - The inventory must have a position description (see GetJobCardsFromSameTaskDefn.qrx).
      InventoryKey inv = Domain.createAircraft( acft -> {
         acft.setAllowSynchronization( true );
         acft.setPositionDescription( POSITION_DESCRIPTION );
      } );
      TaskKey jic1 = Domain.createJobCard( jic -> {
         jic.setDefinition( jobCardDefn );
         jic.setInventory( inv );
      } );
      TaskKey jic2 = Domain.createJobCard( jic -> {
         jic.setDefinition( jobCardDefn );
         jic.setInventory( inv );
         jic.setSuppressingJobCard( jic1 );
      } );
      TaskKey suppressedJic = Domain.createJobCard( jic -> {
         jic.setDefinition( jobCardDefn );
         jic.setInventory( inv );
         jic.setSuppressingJobCard( jic1 );
      } );

      // Given REQ class requirement tasks, each assigned one of the job card tasks.
      //
      // Note; the REQ definitions must be associated to the JIC definitions
      // (see TaskSuppressionService.canJobCardBeUpdatedToLatestRevision()).
      TaskKey req1 = Domain.createRequirement( req -> {
         req.setTaskClass( REQ );
         req.addJobCard( jic1 );
         req.setDefinition( Domain.createRequirementDefinition(
               reqDefn -> reqDefn.addJobCardDefinition( jobCardDefn ) ) );
      } );
      TaskKey req2 = Domain.createRequirement( req -> {
         req.setTaskClass( REQ );
         req.addJobCard( jic2 );
         req.setDefinition( Domain.createRequirementDefinition(
               aReqDefn -> aReqDefn.addJobCardDefinition( jobCardDefn ) ) );
      } );
      TaskKey reqWithSuppressedJic = Domain.createRequirement( req -> {
         req.setTaskClass( REQ );
         req.addJobCard( suppressedJic );
         req.setDefinition( Domain.createRequirementDefinition(
               reqDefn -> reqDefn.addJobCardDefinition( jobCardDefn ) ) );
      } );

      // Given a committed work package with all the requirement tasks assigned to it and all the
      // job card tasks assigned to its work scope.
      Domain.createWorkPackage( wp -> {
         wp.setStatus( COMMIT );
         wp.addTask( req1 );
         wp.addTask( req2 );
         wp.addTask( reqWithSuppressedJic );
         wp.addWorkScopeTask( jic1 );
         wp.addWorkScopeTask( jic2 );
         wp.addWorkScopeTask( suppressedJic );
      } );

      // When the requirement task of a suppressed JIC task is unassigned from the work package.
      new DetailsService( reqWithSuppressedJic ).unassignTask( hr, NA_REASON, NA_NOTE,
            NA_OPERATION_ALLOWED_FOR_LRP );

      // Then the JIC task is no longer suppressed.
      assertThat( "JIC has unexpected suppressing JIC",
            schedStaskDao.findByPrimaryKey( suppressedJic ).getDupJicSchedKey(),
            is( ( TaskKey ) null ) );
   }


   @Test
   public void assignATaskToAWorkpackageWillPublishTaskAssignedToWorkpackageEvent()
         throws MxException, TriggerException {

      // ARRANGE
      InventoryKey aircraft = Domain.createAircraft();

      TaskKey taskKey = Domain.createAdhocTask( task -> {
         task.setInventory( aircraft );
      } );

      TaskKey workPackageKey = Domain.createWorkPackage( wp -> {
         wp.setAircraft( aircraft );
      } );

      RefStageReasonKey reason = RefStageReasonKey.NEW;
      String notes = "assign task to workpackage";
      boolean isLrpMode = false;
      boolean isBaselineSync = false;
      boolean isSuppressDuplicateWorkLines = false;
      String systemNotes = null;
      TaskKey kickOffTask = null;

      // ACT
      new AxonDomainEventDao().purgeAll();
      new DetailsService( taskKey ).setParentTask( workPackageKey, hr, reason, notes, isLrpMode,
            isBaselineSync, isSuppressDuplicateWorkLines, systemNotes, kickOffTask );

      // ASSERT
      TaskAssignedToWorkPackageEvent taskAssignedToWpEvt =
            new TaskAssignedToWorkPackageEvent( workPackageKey, taskKey, hr, reason, notes );

      List<EventMessage<?>> events = eventBus.getEventMessages().stream()
            .filter( evt -> evt.getPayload().equals( taskAssignedToWpEvt ) )
            .collect( Collectors.toList() );

      assertThat( "Task assigned to work package is not published correctly", events.size(),
            is( 1 ) );
   }


   @Test
   public void assignATaskToACompletedWorkpackageWillPublishTaskAssignedToWorkpackageEvent()
         throws MxException, TriggerException {

      // ARRANGE
      InventoryKey aircraft = Domain.createAircraft();
      Date nowDate = new Date();

      TaskKey taskKey = Domain.createAdhocTask( task -> {
         task.setInventory( aircraft );
         task.addDeadline( deadline -> {
            deadline.setStartDate( nowDate );
            deadline.setDueDate( DateUtils.addDays( nowDate, 5 ) );
         } );
      } );

      TaskKey workPackageKey = Domain.createWorkPackage( wp -> {
         wp.setStatus( RefEventStatusKey.COMPLETE );
         wp.setAircraft( aircraft );
         wp.setActualEndDate( DateUtils.addDays( nowDate, 10 ) );
      } );

      RefStageReasonKey reason = RefStageReasonKey.NEW;
      String notes = "assign task to workpackage";
      boolean isLrpMode = false;
      boolean isBaselineSync = false;
      boolean isSuppressDuplicateWorkLines = false;
      String systemNotes = null;
      TaskKey kickOffTask = null;

      // ACT
      new AxonDomainEventDao().purgeAll();
      new DetailsService( taskKey ).setParentTask( workPackageKey, hr, reason, notes, isLrpMode,
            isBaselineSync, isSuppressDuplicateWorkLines, systemNotes, kickOffTask );

      // ASSERT
      TaskAssignedToWorkPackageEvent taskAssignedToWpEvt =
            new TaskAssignedToWorkPackageEvent( workPackageKey, taskKey, hr, reason, notes );

      List<EventMessage<?>> events = eventBus.getEventMessages().stream()
            .filter( evt -> evt.getPayload().equals( taskAssignedToWpEvt ) )
            .collect( Collectors.toList() );

      assertThat( "Task assigned to work package is not published correctly", events.size(),
            is( 1 ) );
   }


   @Test
   public void whenDeletingATaskThenTaskDeletedEventIsPublished() throws Exception {
      final UserTransaction userTransaction = mock( UserTransaction.class );

      // ARRANGE
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

      assertThat( evtEventDao.findByPrimaryKey( taskKey ).exists(), is( true ) );
      assertThat( schedStaskDao.findByPrimaryKey( taskKey ).exists(), is( true ) );

      // ACT
      // When the task is deleted
      new DetailsService( taskKey ).remove( userTransaction, hr );

      // ASSERT
      assertThat( evtEventDao.findByPrimaryKey( taskKey ).exists(), is( false ) );
      assertThat( schedStaskDao.findByPrimaryKey( taskKey ).exists(), is( false ) );

      assertEquals( 1, eventBus.getEventMessages().size() );

      List<EventMessage<?>> taskDeletedEvents = eventBus.getEventMessages().stream()
            .filter( eventMessage -> eventMessage.getPayload() instanceof TaskDeletedEvent )
            .collect( Collectors.toList() );
      assertEquals( 1, taskDeletedEvents.size() );
      assertEquals( taskKey,
            ( ( TaskDeletedEvent ) taskDeletedEvents.get( 0 ).getPayload() ).getTaskKey() );

   }


   private void loadData() throws Exception {
      // create task
      taskKey = Domain.createRequirement();

      detailsService = new DetailsService( taskKey );
   }

}
