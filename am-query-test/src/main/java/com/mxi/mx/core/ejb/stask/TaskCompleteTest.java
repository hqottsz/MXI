package com.mxi.mx.core.ejb.stask;

import static com.mxi.mx.core.key.RefEventStatusKey.IN_WORK;
import static com.mxi.mx.core.key.RefInvClassKey.ACFT;
import static com.mxi.mx.core.key.RefTaskDefinitionStatusKey.ACTV;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.transaction.UserTransaction;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.OwnerDomainBuilder;
import com.mxi.am.domain.builder.PartGroupDomainBuilder;
import com.mxi.am.domain.builder.PartRequirementDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.servlet.SessionContextFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.unittest.security.SecurityIdentificationStub;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefEventTypeKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefRelationTypeKey;
import com.mxi.mx.core.key.RefRemoveReasonKey;
import com.mxi.mx.core.key.RefSchedFromKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskDepActionKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskPartKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.evt.EvtEventDao;
import com.mxi.mx.core.table.evt.EvtEventRel;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.evt.EvtSchedDeadTable;
import com.mxi.mx.core.table.org.OrgHr;
import com.mxi.mx.core.table.sched.SchedStaskDao;
import com.mxi.mx.core.table.sched.SchedStaskTable;


/**
 *
 * Test the logic when complete Tasks
 *
 */
public class TaskCompleteTest {

   private final String SER_SERIAL_NO_OEM1 = "SerSerialNoOem1";
   private final String SER_SERIAL_NO_OEM2 = "SerSerialNoOem2";
   private static final String SYSTEM_NAME = "SYSTEM_NAME";

   private STaskBean iSTaskBean;
   private PartNoKey iPartNoKey;
   private PartGroupKey iPartGroupKey;
   private LocationKey iLocationKey;
   private OwnerKey iOwner;

   private Date iTaskStartDate = new Date();
   private Date iTaskEndDate = DateUtils.addDays( iTaskStartDate, +1 );

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   private EvtEventDao iEvtEventDao;
   private SchedStaskDao iSchedStaskDao;

   private HumanResourceKey iAuthorizingHr;
   private int iUserId;
   private UserParametersFake iUserParametersFake;


   /**
    *
    * Verifies that when complete a task with a remove configuration change, and the task has a
    * child task, and the child task has an install configuration change, then all configuration
    * change events and their associated sub-components records in inv_install and inv_remove tables
    * are updated with the same complete date.
    *
    */
   @Test
   public void itSetTheSameCompleteDateToTaskAndChildTask() throws Exception {

      final UserTransaction lTx = mock( UserTransaction.class );

      final InventoryKey lSerToRemoveInvKey =
            Domain.createSerializedInventory( aSerializedInventory -> {
               aSerializedInventory.setPartNumber( iPartNoKey );
               aSerializedInventory.setSerialNumber( SER_SERIAL_NO_OEM1 );
               aSerializedInventory.setLocation( iLocationKey );
            } );

      final InventoryKey lSerToInstallInvKey =
            Domain.createSerializedInventory( aSerializedInventory -> {
               aSerializedInventory.setPartNumber( iPartNoKey );
               aSerializedInventory.setSerialNumber( SER_SERIAL_NO_OEM2 );
               aSerializedInventory.setLocation( iLocationKey );
               aSerializedInventory.setCondition( RefInvCondKey.RFI );
               aSerializedInventory.setOwner( iOwner );
            } );

      final InventoryKey lAircraftInvKey = Domain.createAircraft();

      final InventoryKey lTrackedInvKey = Domain.createTrackedInventory( aTrackedInventory -> {
         aTrackedInventory.setParent( lAircraftInvKey );
         aTrackedInventory.addSerialized( lSerToRemoveInvKey );
      } );

      TaskKey lActualSubTaskKey =
            createTaskOfInventory( lTrackedInvKey, iTaskStartDate, iTaskEndDate );

      addInstallPartRequirementToTask( lActualSubTaskKey, lSerToInstallInvKey, SER_SERIAL_NO_OEM2 );
      TaskKey lActualTaskKey = createTaskOfInventoryWithSubTask( lTrackedInvKey, lActualSubTaskKey,
            iTaskStartDate, iTaskEndDate );
      createWorkPackageForTask( lActualTaskKey, lAircraftInvKey, iTaskStartDate, iTaskEndDate );
      addRemovePartRequirementToTask( lActualTaskKey, lSerToRemoveInvKey, SER_SERIAL_NO_OEM1 );

      TaskKey[] lActualTaskKeys = { lActualTaskKey };

      Date lNewTaskEndDate = DateUtils.addDays( iTaskEndDate, +1 );

      iSTaskBean.complete( null, lActualTaskKeys, lNewTaskEndDate, iAuthorizingHr, true, lTx );

      // Mock user transaction, successful test case only has three commits and begins.
      verify( lTx, times( 3 ) ).commit();
      verify( lTx, times( 3 ) ).begin();

      assertRemoveConfigChangeEventDate( lActualTaskKey, lNewTaskEndDate );
      assertInstallConfigChangeEventDate( lActualSubTaskKey, lNewTaskEndDate );
   }


   /**
    *
    * Verifies that when complete a task with an install configuration change, and the task has a
    * dependent task, and the dependent task has a remove configuration change, then install
    * configuration change events and their associated sub-components records in inv_install are
    * updated with the same complete date. The remove configuration change in dependent task will be
    * cancelled, so we don't expected anything will be found.
    *
    */
   @Test
   public void itSetTheSameCompleteDateToTaskAndDependentTask() throws Exception {

      final UserTransaction lTx = mock( UserTransaction.class );

      final InventoryKey lSerToRemoveInvKey =
            Domain.createSerializedInventory( aSerializedInventory -> {
               aSerializedInventory.setPartNumber( iPartNoKey );
               aSerializedInventory.setSerialNumber( SER_SERIAL_NO_OEM1 );
               aSerializedInventory.setLocation( iLocationKey );
            } );

      final InventoryKey lSerToInstallInvKey =
            Domain.createSerializedInventory( aSerializedInventory -> {
               aSerializedInventory.setPartNumber( iPartNoKey );
               aSerializedInventory.setSerialNumber( SER_SERIAL_NO_OEM2 );
               aSerializedInventory.setLocation( iLocationKey );
               aSerializedInventory.setCondition( RefInvCondKey.RFI );
               aSerializedInventory.setOwner( iOwner );
            } );

      final InventoryKey lAircraftInvKey = Domain.createAircraft();

      final InventoryKey lTrackedInvKey = Domain.createTrackedInventory( aTrackedInventory -> {
         aTrackedInventory.setParent( lAircraftInvKey );
         aTrackedInventory.addSerialized( lSerToRemoveInvKey );
      } );

      TaskKey lActualDependentTaskKey =
            createTaskOfInventory( lTrackedInvKey, iTaskStartDate, iTaskEndDate );

      addRemovePartRequirementToTask( lActualDependentTaskKey, lSerToRemoveInvKey,
            SER_SERIAL_NO_OEM1 );
      TaskKey lActualTaskKey = createTaskOfInventoryWithDependentTask( lTrackedInvKey,
            lActualDependentTaskKey, iTaskStartDate, iTaskEndDate );
      createWorkPackageForTask( lActualTaskKey, lAircraftInvKey, iTaskStartDate, iTaskEndDate );
      addInstallPartRequirementToTask( lActualTaskKey, lSerToInstallInvKey, SER_SERIAL_NO_OEM2 );

      TaskKey[] lActualTaskKeys = { lActualTaskKey };

      Date lNewTaskEndDate = DateUtils.addDays( iTaskEndDate, +1 );

      iSTaskBean.complete( null, lActualTaskKeys, lNewTaskEndDate, iAuthorizingHr, true, lTx );

      // Mock user transaction, successful test case only has two commit and begin.
      verify( lTx, times( 2 ) ).commit();
      verify( lTx, times( 2 ) ).begin();

      assertInstallConfigChangeEventDate( lActualTaskKey, lNewTaskEndDate );
      assertRemoveConfigChangeNotFound( lActualDependentTaskKey );
   }


   /**
    * Verify that completing an active repetitive task updates the task status as COMPLETE
    *
    * <pre>
    * Given an active repetitive task in a work package against an aircraft
    * When the active task is completed
    * Then task status is set to COMPLETE
    * </pre>
    */
   @Test
   public void testCompleteActiveRepetitiveTaskDoesWorkOfCompletingTask() throws Exception {
      final UserTransaction lTx = mock( UserTransaction.class );

      final PartNoKey lAircraftPart = Domain.createPart();
      final AssemblyKey lAssembly = createAircraftAssembly( lAircraftPart );
      final InventoryKey lAssemblyInventory = createAircraftInventory( lAircraftPart, lAssembly );

      final InventoryKey lTaskInventory = Domain.readSystem( lAssemblyInventory, SYSTEM_NAME );
      final Date COMPLETION_DATE = new Date();

      TaskKey lActiveRepetitiveTask = Domain.createRepetitiveTask( aRepetitiveTask -> {
         aRepetitiveTask.setStatus( RefEventStatusKey.ACTV );
         aRepetitiveTask.setInventory( lTaskInventory );
      } );

      Domain.createWorkPackage( aWorkPackage -> {
         aWorkPackage.addTask( lActiveRepetitiveTask );
         aWorkPackage.setStatus( IN_WORK );
      } );

      iSTaskBean.complete( null, new TaskKey[] { lActiveRepetitiveTask }, COMPLETION_DATE,
            iAuthorizingHr, true, lTx );

      EvtEventTable lCompletedEvent = iEvtEventDao.findByPrimaryKey( lActiveRepetitiveTask );

      assertThat( lCompletedEvent.getEventStatus(), equalTo( RefEventStatusKey.COMPLETE ) );
   }


   /**
    * Verify that completing an active repetitive task updates the forecast task to be active and
    * scheduled with a due date
    *
    * <pre>
    * Given an active repetitive task with a repeat interval in a work package and a forecast task against an aircraft
    * And the tasks are associated to a fault
    * When the active task is completed
    * Then the forecast task becomes the active task
    * And the Schedule From Code is updated to be LASTEND
    * And the start date of the schedule is updated to be the same day as the completion date of the previous task
    * And the due date is updated to be <REPEAT_INTERVAL> days greater than start date
    * </pre>
    */
   @Test
   public void testCompleteActiveRepetitiveTaskCorrectlySchedulesNewActiveTask() throws Exception {
      final UserTransaction lTx = mock( UserTransaction.class );

      final PartNoKey lAircraftPart = Domain.createPart();
      final AssemblyKey lAssembly = createAircraftAssembly( lAircraftPart );
      final InventoryKey lAssemblyInventory = createAircraftInventory( lAircraftPart, lAssembly );

      final InventoryKey lTaskInventory = Domain.readSystem( lAssemblyInventory, SYSTEM_NAME );
      final Date COMPLETION_DATE = new Date();
      final BigDecimal REPEAT_INTERVAL = BigDecimal.TEN;

      TaskKey lActiveRepetitiveTask = Domain.createRepetitiveTask( aRepetitiveTask -> {
         aRepetitiveTask.setStatus( RefEventStatusKey.ACTV );
         aRepetitiveTask.setInventory( lTaskInventory );
         aRepetitiveTask.setRepeatInterval( REPEAT_INTERVAL );
      } );

      TaskKey lForecastRepetitiveTask = Domain.createRepetitiveTask( aRepetitiveTask -> {
         aRepetitiveTask.setStatus( RefEventStatusKey.FORECAST );
         aRepetitiveTask.setInventory( lTaskInventory );
         aRepetitiveTask.setRepeatInterval( REPEAT_INTERVAL );
         aRepetitiveTask.setPreviousTask( lActiveRepetitiveTask );
      } );

      Domain.createFault( aFault -> {
         aFault.setCorrectiveTask( Domain.createCorrectiveTask() );
         aFault.addRepetitiveTask( lActiveRepetitiveTask );
         aFault.addRepetitiveTask( lForecastRepetitiveTask );
      } );

      Domain.createWorkPackage( aWorkPackage -> {
         aWorkPackage.addTask( lActiveRepetitiveTask );
         aWorkPackage.setStatus( IN_WORK );
      } );

      iSTaskBean.complete( null, new TaskKey[] { lActiveRepetitiveTask }, COMPLETION_DATE,
            iAuthorizingHr, true, lTx );

      EvtEventTable lActiveEvent = iEvtEventDao.findByPrimaryKey( lForecastRepetitiveTask );

      assertThat( lActiveEvent.getEventStatus(), equalTo( RefEventStatusKey.ACTV ) );

      EvtSchedDeadTable lActiveEventDeadline =
            EvtSchedDeadTable.findByPrimaryKey( lActiveEvent.getPk(), DataTypeKey.CDY );

      assertThat( "The active repetititve task is not scheduled from LASTEND",
            lActiveEventDeadline.getScheduledFrom(), equalTo( RefSchedFromKey.LASTEND ) );

      assertThat(
            "The active repetitive task does not start on the day of COMPLETION_DATE", DateUtils
                  .absoluteDifferenceInDays( lActiveEventDeadline.getStartDate(), COMPLETION_DATE ),
            equalTo( 0 ) );

      Date lExpectedActiveDueDate =
            DateUtils.addDays( lActiveEventDeadline.getStartDate(), REPEAT_INTERVAL.intValue() );
      assertThat(
            "The active repetitive task's due date is not <REPEAT_INTERVAL> days greater than the completion date of the completed task",
            lActiveEventDeadline.getDeadlineDate(), equalTo( lExpectedActiveDueDate ) );
   }


   /**
    * Verify that completing an active repetitive task creates a new forecast task
    *
    * <pre>
    * Given an active repetitive task in a work package and a forecast task against an aircraft
    * And the tasks are associated to a fault
    * When the active task is completed
    * Then a new adhoc forecast task is created with a dependency link to the (now) active task
    * And a recurring source (RECSRC) relation is created to the fault
    * </pre>
    */
   @Test
   public void testCompleteActiveRepetitiveTaskCreatesNewForecastTask() throws Exception {
      final UserTransaction lTx = mock( UserTransaction.class );

      final PartNoKey lAircraftPart = Domain.createPart();
      final AssemblyKey lAssembly = createAircraftAssembly( lAircraftPart );
      final InventoryKey lAssemblyInventory = createAircraftInventory( lAircraftPart, lAssembly );

      final InventoryKey lTaskInventory = Domain.readSystem( lAssemblyInventory, SYSTEM_NAME );
      final Date COMPLETION_DATE = new Date();

      TaskKey lActiveRepetitiveTask = Domain.createRepetitiveTask( aRepetitiveTask -> {
         aRepetitiveTask.setStatus( RefEventStatusKey.ACTV );
         aRepetitiveTask.setInventory( lTaskInventory );
      } );

      TaskKey lForecastRepetitiveTask = Domain.createRepetitiveTask( aRepetitiveTask -> {
         aRepetitiveTask.setStatus( RefEventStatusKey.FORECAST );
         aRepetitiveTask.setInventory( lTaskInventory );
         aRepetitiveTask.setPreviousTask( lActiveRepetitiveTask );
      } );

      FaultKey lFaultKey = Domain.createFault( aFault -> {
         aFault.setCorrectiveTask( Domain.createCorrectiveTask() );
         aFault.addRepetitiveTask( lActiveRepetitiveTask );
         aFault.addRepetitiveTask( lForecastRepetitiveTask );
      } );

      Domain.createWorkPackage( aWorkPackage -> {
         aWorkPackage.addTask( lActiveRepetitiveTask );
         aWorkPackage.setStatus( IN_WORK );
      } );

      iSTaskBean.complete( null, new TaskKey[] { lActiveRepetitiveTask }, COMPLETION_DATE,
            iAuthorizingHr, true, lTx );

      List<EvtEventRel> lEventDependencyRelationships =
            EvtEventRel.findByRelationshipType( lForecastRepetitiveTask, RefRelationTypeKey.DEPT );
      assertThat(
            "The active repetitive task does not have a dependency relation to a forecasted repetitive task",
            lEventDependencyRelationships, hasSize( 1 ) );

      EvtEventTable lForecastedEvent =
            iEvtEventDao.findByPrimaryKey( lEventDependencyRelationships.get( 0 ).getRelEvent() );

      assertThat( lForecastedEvent.getEventStatus(), equalTo( RefEventStatusKey.FORECAST ) );

      SchedStaskTable lForecastedTask = iSchedStaskDao.findByPrimaryKey(
            TaskKey.getTaskKey( lEventDependencyRelationships.get( 0 ).getRelEvent() ) );

      assertThat( lForecastedTask.getTaskClass(), equalTo( RefTaskClassKey.ADHOC ) );

      List<EvtEventRel> lEventRecurringSourceRelationships = EvtEventRel
            .findByRelationshipType( lForecastedTask.getPk(), RefRelationTypeKey.RECSRC );
      assertThat( "The active repetitive task does not have a recurring source relation",
            lEventRecurringSourceRelationships, hasSize( 1 ) );

      assertThat( "The active repetitive task does not have a recurring source relation to a fault",
            lEventRecurringSourceRelationships.get( 0 ).getRelEvent(),
            equalTo( lFaultKey.getEventKey() ) );
   }


   /**
    * Verify that completing an active repetitive task schedules a new forecast task
    *
    * <pre>
    * Given an active repetitive task in a work package and a forecast task against an aircraft
    * And the tasks are associated to a fault
    * When the active task is completed
    * Then a new adhoc forecast task is created
    * And the Schedule From Code is set to LASTDUE
    * And the start date of the schedule is set to the due date of the dependent active repetitive task
    * And the due date is set to <REPEAT_INTERVAL> days greater than its start date
    * </pre>
    */
   @Test
   public void testCompleteActiveRepetitiveTaskCorrectlySchedulesNewForecastTask()
         throws Exception {
      final UserTransaction lTx = mock( UserTransaction.class );

      final PartNoKey lAircraftPart = Domain.createPart();
      final AssemblyKey lAssembly = createAircraftAssembly( lAircraftPart );
      final InventoryKey lAssemblyInventory = createAircraftInventory( lAircraftPart, lAssembly );

      final InventoryKey lTaskInventory = Domain.readSystem( lAssemblyInventory, SYSTEM_NAME );
      final Date COMPLETION_DATE = new Date();
      final BigDecimal REPEAT_INTERVAL = BigDecimal.TEN;

      TaskKey lActiveRepetitiveTask = Domain.createRepetitiveTask( aRepetitiveTask -> {
         aRepetitiveTask.setStatus( RefEventStatusKey.ACTV );
         aRepetitiveTask.setInventory( lTaskInventory );
         aRepetitiveTask.setRepeatInterval( REPEAT_INTERVAL );
      } );

      TaskKey lForecastRepetitiveTask = Domain.createRepetitiveTask( aRepetitiveTask -> {
         aRepetitiveTask.setStatus( RefEventStatusKey.FORECAST );
         aRepetitiveTask.setInventory( lTaskInventory );
         aRepetitiveTask.setRepeatInterval( REPEAT_INTERVAL );
         aRepetitiveTask.setPreviousTask( lActiveRepetitiveTask );
      } );

      Domain.createFault( aFault -> {
         aFault.setCorrectiveTask( Domain.createCorrectiveTask() );
         aFault.addRepetitiveTask( lActiveRepetitiveTask );
         aFault.addRepetitiveTask( lForecastRepetitiveTask );
      } );

      Domain.createWorkPackage( aWorkPackage -> {
         aWorkPackage.addTask( lActiveRepetitiveTask );
         aWorkPackage.setStatus( IN_WORK );
      } );

      iSTaskBean.complete( null, new TaskKey[] { lActiveRepetitiveTask }, COMPLETION_DATE,
            iAuthorizingHr, true, lTx );

      EvtEventTable lActiveEvent = iEvtEventDao.findByPrimaryKey( lForecastRepetitiveTask );
      EvtSchedDeadTable lActiveEventDeadline =
            EvtSchedDeadTable.findByPrimaryKey( lActiveEvent.getPk(), DataTypeKey.CDY );

      Date lExpectedActiveDueDate =
            DateUtils.addDays( lActiveEventDeadline.getStartDate(), REPEAT_INTERVAL.intValue() );

      List<EvtEventRel> lEventRelationships =
            EvtEventRel.findByRelationshipType( lForecastRepetitiveTask, RefRelationTypeKey.DEPT );

      EvtSchedDeadTable lForecastEventDeadline = EvtSchedDeadTable
            .findByPrimaryKey( lEventRelationships.get( 0 ).getRelEvent(), DataTypeKey.CDY );

      assertThat( "The forecast repetititve task is not scheduled from LASTDUE",
            lForecastEventDeadline.getScheduledFrom(), equalTo( RefSchedFromKey.LASTDUE ) );

      assertThat(
            "The forecast repetitive task does not start on the same date as the due date of the dependent active repetitive task",
            lForecastEventDeadline.getStartDate(), equalTo( lExpectedActiveDueDate ) );

      assertThat(
            "The forecasted repetitive task's due date is not <REPEAT_INTERVAL> days greater than its start date",
            DateUtils
                  .absoluteDifferenceInDays(
                        DateUtils.addDays( lForecastEventDeadline.getStartDate(),
                              REPEAT_INTERVAL.intValue() ),
                        lForecastEventDeadline.getDeadlineDate() ),
            equalTo( 0 ) );
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


   private InventoryKey createAircraftInventory( final PartNoKey aAircraftPart,
         final AssemblyKey aAssembly ) {
      return Domain.createAircraft( aAircraft -> {
         aAircraft.setAssembly( aAssembly );
         aAircraft.setPart( aAircraftPart );
         aAircraft.addSystem( SYSTEM_NAME );
      } );
   }


   private TaskKey createTaskOfInventory( final InventoryKey aInvKey, final Date aStartDate,
         final Date aEndDate ) {

      final TaskTaskKey lReqDefnKey = Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.setStatus( ACTV );
      } );

      TaskKey lActualTaskKey = Domain.createRequirement( aActualTask -> {
         aActualTask.setInventory( aInvKey );
         aActualTask.setTaskClass( RefTaskClassKey.REQ );
         aActualTask.setActualStartDate( aStartDate );
         aActualTask.setActualEndDate( aEndDate );
         aActualTask.setStatus( RefEventStatusKey.ACTV );
         aActualTask.setDefinition( lReqDefnKey );
      } );

      return lActualTaskKey;
   }


   private TaskKey createTaskOfInventoryWithSubTask( final InventoryKey aInvKey,
         final TaskKey aSubTaskKey, final Date aStartDate, final Date aEndDate ) {

      TaskKey lActualTaskKey = Domain.createRequirement( aActualTask -> {
         aActualTask.setInventory( aInvKey );
         aActualTask.setTaskClass( RefTaskClassKey.ADHOC );
         aActualTask.setActualStartDate( aStartDate );
         aActualTask.setActualEndDate( aEndDate );
         aActualTask.setStatus( RefEventStatusKey.PAUSE );
         aActualTask.addSubTask( aSubTaskKey );
      } );

      return lActualTaskKey;
   }


   private TaskKey createTaskOfInventoryWithDependentTask( final InventoryKey aInvKey,
         final TaskKey aDependentTaskKey, final Date aStartDate, final Date aEndDate ) {

      SchedStaskTable lSchedStask = iSchedStaskDao.findByPrimaryKey( aDependentTaskKey );
      final TaskTaskKey lChildDefnKey = lSchedStask.getTaskTaskKey();
      if ( lChildDefnKey == null ) {
         fail( "Expected to find task defintion of dependent task" );
      }

      final TaskTaskKey lParentDefnKey = Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.setStatus( ACTV );
         aReqDefn.addLinkedTaskDefinition( RefTaskDepActionKey.COMPLETE, lChildDefnKey );
      } );

      TaskKey lActualTaskKey = Domain.createRequirement( aActualTask -> {
         aActualTask.setInventory( aInvKey );
         aActualTask.setTaskClass( RefTaskClassKey.REQ );
         aActualTask.setActualStartDate( aStartDate );
         aActualTask.setActualEndDate( aEndDate );
         aActualTask.setStatus( RefEventStatusKey.PAUSE );
         aActualTask.setDefinition( lParentDefnKey );
      } );

      return lActualTaskKey;
   }


   private TaskPartKey addRemovePartRequirementToTask( final TaskKey aTaskKey,
         final InventoryKey aInvKey, final String aInvSerialNo ) {
      TaskPartKey lTaskPartKey = new PartRequirementDomainBuilder( aTaskKey ).forPart( iPartNoKey )
            .forPartGroup( iPartGroupKey ).withRemovalQuantity( 1 )
            .withRemovalReason( RefRemoveReasonKey.IMSCHD ).withRemovalInventory( aInvKey )
            .withRemovalSerialNo( aInvSerialNo ).build();

      return lTaskPartKey;
   }


   private TaskPartKey addInstallPartRequirementToTask( final TaskKey aTaskKey,
         final InventoryKey aInvKey, final String aInvSerialNo ) {
      TaskPartKey lTaskPartKey = new PartRequirementDomainBuilder( aTaskKey ).forPart( iPartNoKey )
            .forPartGroup( iPartGroupKey ).withInstallQuantity( 1 ).withInstallPart( iPartNoKey )
            .withInstallInventory( aInvKey ).withInstallSerialNumber( aInvSerialNo ).build();

      return lTaskPartKey;
   }


   private TaskKey createWorkPackageForTask( final TaskKey aTaskKey,
         final InventoryKey aAircraftKey, final Date aStartDate, final Date aEndDate ) {

      return Domain.createWorkPackage( aWorkPackage -> {
         aWorkPackage.setAircraft( aAircraftKey );
         aWorkPackage.setStatus( IN_WORK );
         aWorkPackage.addTask( aTaskKey );
         aWorkPackage.setActualStartDate( aStartDate );
         aWorkPackage.setActualEndDate( aEndDate );
      } );
   }


   private void assertRemoveConfigChangeEventDate( TaskKey aActualTaskKey, Date aEventDate ) {
      boolean lEventFound = false;
      List<EvtEventRel> lRelationshipTables =
            EvtEventRel.findByRelationshipType( aActualTaskKey, RefRelationTypeKey.TTFG );

      for ( EvtEventRel lEvtRel : lRelationshipTables ) {
         EventKey lEventKey = lEvtRel.getRelEvent();
         EvtEventTable lEvtEvent = iEvtEventDao.findByPrimaryKey( lEventKey );
         if ( RefEventTypeKey.FG.equals( lEvtEvent.getEventType() ) ) {
            lEventFound = true;

            // We need to make sure evt_event date is updated
            // We are not able to compare date directly due to the mx database library does not
            // guarantee milliseconds are stored in database exactly as the original date. So we
            // need to convert date to string as mx standard format to compare.
            Assert.assertEquals( DateUtils.toDefaultDateTimeString( aEventDate ),
                  DateUtils.toDefaultDateTimeString( lEvtEvent.getEventDate() ) );

            // also, there must be a inv_remove record, and the date is updated
            final String[] lInv_Remove_Columns = { "inv_no_id", "inv_no_db_id", "event_dt" };

            DataSetArgument aArgs = new DataSetArgument();
            aArgs.add( lEventKey, "event_db_id", "event_id" );
            QuerySet lQuerySet = QuerySetFactory.getInstance().executeQuery( lInv_Remove_Columns,
                  "inv_remove", aArgs );

            if ( !lQuerySet.next() ) {
               fail( "Expected to find remove record" );
            }

            Date lRemovedDate = lQuerySet.getDate( "event_dt" );
            Assert.assertEquals( DateUtils.toDefaultDateTimeString( aEventDate ),
                  DateUtils.toDefaultDateTimeString( lRemovedDate ) );
         }
      }

      Assert.assertTrue( "Expected to find configuration change event", lEventFound );
   }


   private void assertRemoveConfigChangeNotFound( TaskKey aActualTaskKey ) {
      boolean lEventFound = false;
      List<EvtEventRel> lRelationshipTables =
            EvtEventRel.findByRelationshipType( aActualTaskKey, RefRelationTypeKey.TTFG );

      for ( EvtEventRel lEvtRel : lRelationshipTables ) {
         EventKey lEventKey = lEvtRel.getRelEvent();
         EvtEventTable lEvtEvent = iEvtEventDao.findByPrimaryKey( lEventKey );
         if ( RefEventTypeKey.FG.equals( lEvtEvent.getEventType() ) ) {
            lEventFound = true;
         }
      }

      Assert.assertFalse( "Do not expected to find any configuration change event", lEventFound );
   }


   private void assertInstallConfigChangeEventDate( TaskKey aActualTaskKey, Date aEventDate ) {
      boolean lEventFound = false;
      List<EvtEventRel> lRelationshipTables =
            EvtEventRel.findByRelationshipType( aActualTaskKey, RefRelationTypeKey.TTFG );

      for ( EvtEventRel lEvtRel : lRelationshipTables ) {
         EventKey lEventKey = lEvtRel.getRelEvent();
         EvtEventTable lEvtEvent = iEvtEventDao.findByPrimaryKey( lEventKey );
         if ( RefEventTypeKey.FG.equals( lEvtEvent.getEventType() ) ) {
            lEventFound = true;

            // We need to make sure evt_event date is updated.
            // We are not able to compare date directly due to the mx database library does not
            // guarantee milliseconds are stored in database exactly as the original date. So we
            // need to convert date to string as mx standard format to compare.
            Assert.assertEquals( DateUtils.toDefaultDateTimeString( aEventDate ),
                  DateUtils.toDefaultDateTimeString( lEvtEvent.getEventDate() ) );

            // also, there must be a inv_remove record, and the date is updated
            final String[] lInv_Remove_Columns = { "inv_no_id", "inv_no_db_id", "event_dt" };

            DataSetArgument aArgs = new DataSetArgument();
            aArgs.add( lEventKey, "event_db_id", "event_id" );
            QuerySet lQuerySet = QuerySetFactory.getInstance().executeQuery( lInv_Remove_Columns,
                  "inv_install", aArgs );

            if ( !lQuerySet.next() ) {
               fail( "Expected to find install record" );
            }

            Date lRemovedDate = lQuerySet.getDate( "event_dt" );
            Assert.assertEquals( DateUtils.toDefaultDateTimeString( aEventDate ),
                  DateUtils.toDefaultDateTimeString( lRemovedDate ) );
         }
      }

      Assert.assertTrue( "Expected to find configuration change event", lEventFound );
   }


   @Before
   public void setup() {

      iAuthorizingHr = Domain.createHumanResource();
      iUserId = OrgHr.findByPrimaryKey( iAuthorizingHr ).getUserId();
      SecurityIdentificationUtils.setInstance( new SecurityIdentificationStub( iAuthorizingHr ) );

      iUserParametersFake = new UserParametersFake( iUserId, "LOGIC" );
      iUserParametersFake.setBoolean( "SPEC2000_UPPERCASE_ASSMBL_CD", false );
      iUserParametersFake.setProperty( "TOBE_INST_NOTAPPROVED_FOR_GROUP", "INFO" );
      UserParameters.setInstance( iUserId, "LOGIC", iUserParametersFake );

      iSTaskBean = new STaskBean();
      iSTaskBean.ejbCreate();
      iSTaskBean.setSessionContext( new SessionContextFake() );

      // build a part group
      iPartGroupKey = new PartGroupDomainBuilder( "TESTGROUP" )
            .withInventoryClass( RefInvClassKey.SER ).build();

      iPartNoKey = Domain.createPart( aPart -> {
         aPart.setInventoryClass( RefInvClassKey.SER );
         aPart.setPartStatus( RefPartStatusKey.ACTV );
         aPart.setShortDescription( "TESTPART" );
         aPart.setPartGroup( iPartGroupKey, true );
      } );

      iLocationKey = Domain.createLocation( aLocation -> {
         aLocation.setType( RefLocTypeKey.OPS );
      } );

      iOwner = new OwnerDomainBuilder().build();

      iEvtEventDao = InjectorContainer.get().getInstance( EvtEventDao.class );
      iSchedStaskDao = InjectorContainer.get().getInstance( SchedStaskDao.class );
   }


   @After
   public void tearDown() {
      UserParameters.setInstance( iUserId, "LOGIC", null );
      SecurityIdentificationUtils.setInstance( null );
   }

}
