package com.mxi.mx.core.services.stask.complete.completeservice;

import static com.mxi.mx.core.key.DataTypeKey.CDY;
import static com.mxi.mx.core.key.DataTypeKey.CYCLES;
import static com.mxi.mx.core.key.DataTypeKey.HOURS;
import static com.mxi.mx.core.key.DataTypeKey.LANDING;
import static com.mxi.mx.core.key.RefEventStatusKey.ACTV;
import static com.mxi.mx.core.key.RefEventStatusKey.COMMIT;
import static com.mxi.mx.core.key.RefEventStatusKey.IN_WORK;
import static com.mxi.mx.core.key.RefTaskDepActionKey.COMPLETE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Engine;
import com.mxi.am.domain.Flight;
import com.mxi.am.domain.InstallationRecord;
import com.mxi.am.domain.JobCard;
import com.mxi.am.domain.RemovalRecord;
import com.mxi.am.domain.Requirement;
import com.mxi.am.domain.RequirementDefinition;
import com.mxi.am.domain.UsageAdjustment;
import com.mxi.am.domain.WorkPackage;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersStub;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.common.validation.Messages;
import com.mxi.mx.common.validation.ValidationException;
import com.mxi.mx.core.adapter.flight.messages.flight.adapter.logic.ActualDatesCalculator;
import com.mxi.mx.core.ejb.flighthist.CurrentUsages;
import com.mxi.mx.core.flight.model.FlightLegId;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.EventInventoryUsageKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.services.event.inventory.EventInventoryService;
import com.mxi.mx.core.services.event.inventory.UsageSnapshot;
import com.mxi.mx.core.services.event.inventory.UsageSnapshotService;
import com.mxi.mx.core.services.stask.SchedStaskUtils;
import com.mxi.mx.core.services.stask.complete.CompleteService;
import com.mxi.mx.core.services.stask.deadline.MxSoftDeadlineService;
import com.mxi.mx.core.services.stask.deadline.updatedeadline.logic.MxUpdateDeadlineService;
import com.mxi.mx.core.services.stask.details.TaskSuppressionService;
import com.mxi.mx.core.table.evt.EvtEventDao;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.evt.EvtInvTable;
import com.mxi.mx.core.table.evt.EvtInvUsageTable;
import com.mxi.mx.core.table.evt.JdbcEvtEventDao;
import com.mxi.mx.core.table.org.OrgHr;
import com.mxi.mx.core.usage.model.UsageAdjustmentId;
import com.mxi.mx.core.usage.service.MxUsageAccrualService;
import com.mxi.mx.core.usage.service.UsageAccrualService;


/**
 * Tests the behaviours of
 * {@linkplain CompleteService#completeDependencyTasks(com.mxi.mx.core.key.TaskKey, com.mxi.mx.core.key.HumanResourceKey, java.util.Date)}
 *
 * Note: due to the large number of public methods and behaviours captured by the class
 * CompleteService, this particular method is being tested within its own test class.
 *
 */
public final class CompleteDependencyTasksTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private HumanResourceKey iHr;
   private int iUserId;
   private final EvtEventDao iEventDao = new JdbcEvtEventDao();
   private static final Date iToday = new Date();

   // Create an installation date far enough in the past to test with dates after it.
   private static final Date ENGINE_INSTALL_DATE = DateUtils.addDays( iToday, -100 );


   /**
    * Completing a task automatically completes a COMPLETES following task when that following task
    * is assigned to the same work package.
    *
    * <pre>
    * Given a REQ definition that completes a following REQ definition
    *   and REQ tasks against an aircraft based on those definitions
    *   and an in-work work package against the aircraft with both tasks assigned to it
    *  When the lead task is completed
    *  Then the following task is automatically completed
    * </pre>
    */
   @Test
   public void itCompletesTheFollowingTaskInTheSameWorkPackage() throws Exception {

      // Given a REQ definition that completes a following REQ definition.
      final TaskTaskKey lFollowReqDefn = Domain.createRequirementDefinition();
      final TaskTaskKey lLeadReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefinition ) {
                  aReqDefinition.addFollowingTaskDefinition( COMPLETE, lFollowReqDefn );
               }
            } );

      // Given REQ tasks against an aircraft based on those definitions.
      final InventoryKey lAircraft = Domain.createAircraft();
      final TaskKey lFollowingReq =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement aRequirement ) {
                  aRequirement.setInventory( lAircraft );
                  aRequirement.setDefinition( lFollowReqDefn );
                  aRequirement.setStatus( ACTV );
               }
            } );
      final TaskKey lLeadReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aRequirement ) {
            aRequirement.setInventory( lAircraft );
            aRequirement.setDefinition( lLeadReqDefn );
         }
      } );

      // Given an in-work work package against the aircraft with both tasks assigned to it.
      Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {
            aWorkPackage.setAircraft( lAircraft );
            aWorkPackage.setStatus( IN_WORK );
            aWorkPackage.addTask( lLeadReq );
            aWorkPackage.addTask( lFollowingReq );
         }
      } );

      // When the lead task is completed.
      new CompleteService( lLeadReq ).completeDependencyTasks( lLeadReq, iHr, iToday );

      // Then the following task is automatically completed.
      assertTaskIsCompleted( lFollowingReq );
   }


   /**
    * Completing a task automatically moves a loose, COMPLETES following task to the lead task's
    * work package.
    *
    * <pre>
    * Given a REQ definition that completes a following REQ definition
    *   and REQ tasks against an aircraft based on those definitions
    *   and an in-work work package against the aircraft with only the lead task is assigned to it
    *  When the lead task is completed
    *  Then the following task is automatically completed
    *   and the following task is assigned to the lead task's work package
    * </pre>
    */
   @Test
   public void itAssignsTheLooseFollowingTaskToTheLeadTasksWorkPackage() throws Exception {

      // Given a REQ definition that completes a following REQ definition.
      final TaskTaskKey lFollowReqDefn = Domain.createRequirementDefinition();
      final TaskTaskKey lLeadReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefinition ) {
                  aReqDefinition.addFollowingTaskDefinition( COMPLETE, lFollowReqDefn );
               }
            } );

      // Given REQ tasks against an aircraft based on those definitions.
      final InventoryKey lAircraft = Domain.createAircraft();
      final TaskKey lFollowingReq =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement aRequirement ) {
                  aRequirement.setInventory( lAircraft );
                  aRequirement.setDefinition( lFollowReqDefn );
                  aRequirement.setStatus( ACTV );
               }
            } );
      final TaskKey lLeadReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aRequirement ) {
            aRequirement.setInventory( lAircraft );
            aRequirement.setDefinition( lLeadReqDefn );
         }
      } );

      // Given an in-work work package against the aircraft with only the lead task is assigned to
      // it (the following task is loose).
      TaskKey lWorkPackage = Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {
            aWorkPackage.setAircraft( lAircraft );
            aWorkPackage.setStatus( IN_WORK );
            aWorkPackage.addTask( lLeadReq );
         }
      } );

      // When the lead task is completed.
      new CompleteService( lLeadReq ).completeDependencyTasks( lLeadReq, iHr, iToday );

      // Then the following task is automatically completed.
      assertTaskIsCompleted( lFollowingReq );

      // Then the following task is assigned to the lead task's work package.
      assertReqIsAssignedToWorkPackage( lFollowingReq, lWorkPackage );
   }


   /**
    * Completing a task automatically moves a COMPLETES following task from another work package to
    * the lead task's work package.
    *
    * <pre>
    * Given a REQ definition that completes a following REQ definition
    *   and REQ tasks against an aircraft based on those definitions
    *   and an in-work work package against the aircraft with the lead task is assigned to it
    *   and another work package against the aircraft with the following task is assigned to it
    *  When the lead task is completed
    *  Then the following task is automatically completed
    *   and the following task is unassigned from its original work package
    *   and the following task is assigned to the lead task's work package
    * </pre>
    */
   @Test
   public void itMovesTheFollowingTaskFromItsWorkPackageToTheLeadTasksWorkPackage()
         throws Exception {

      // Given a REQ definition that completes a following REQ definition.
      final TaskTaskKey lFollowReqDefn = Domain.createRequirementDefinition();
      final TaskTaskKey lLeadReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefinition ) {
                  aReqDefinition.addFollowingTaskDefinition( COMPLETE, lFollowReqDefn );
               }
            } );

      // Given REQ tasks against an aircraft based on those definitions.
      final InventoryKey lAircraft = Domain.createAircraft();
      final TaskKey lFollowingReq =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement aRequirement ) {
                  aRequirement.setInventory( lAircraft );
                  aRequirement.setDefinition( lFollowReqDefn );
                  aRequirement.setStatus( ACTV );
               }
            } );
      final TaskKey lLeadReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aRequirement ) {
            aRequirement.setInventory( lAircraft );
            aRequirement.setDefinition( lLeadReqDefn );
         }
      } );

      // Given an in-work work package against the aircraft with the lead task is assigned to it.
      TaskKey lWorkPackage = Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {
            aWorkPackage.setAircraft( lAircraft );
            aWorkPackage.setStatus( IN_WORK );
            aWorkPackage.addTask( lLeadReq );
         }
      } );

      // Given another work package against the aircraft with the following task is assigned to it.
      TaskKey lAnotherWorkPackage =
            Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

               @Override
               public void configure( WorkPackage aWorkPackage ) {
                  aWorkPackage.setAircraft( lAircraft );
                  aWorkPackage.setStatus( IN_WORK );
                  aWorkPackage.addTask( lFollowingReq );
               }
            } );

      // When the lead task is completed.
      new CompleteService( lLeadReq ).completeDependencyTasks( lLeadReq, iHr, iToday );

      // Then the following task is automatically completed.
      assertTaskIsCompleted( lFollowingReq );

      // Then the following task is unassigned from its original work package.
      assertReqIsNotAssignedToWorkPackage( lFollowingReq, lAnotherWorkPackage );

      // Then the following task is assigned to the lead task's work package.
      assertReqIsAssignedToWorkPackage( lFollowingReq, lWorkPackage );
   }


   /**
    * Completing a task automatically moves a COMPLETES following task from another work package to
    * the lead task's work package and clears any suppression links to ensure incorrect tasks are
    * not also completed.
    *
    * <pre>
    * Given a REQ definition that completes a following REQ definition
    *   and REQ tasks against an aircraft based on those definitions
    *   and an in-work work package against the aircraft with the lead task is assigned to it
    *   and another work package against the aircraft with the following task is assigned to it
    *   and a second neighbour REQ in the following work package
    *   all REQ have a shared JIC, this JIC is suppressed in the following work package
    *  When the lead task is completed
    *  Then the following task is automatically completed
    *   and the following task is unassigned from its original work package (and un-suppressed from the neighbour REQ)
    *   and the following task is assigned to the lead task's work package
    * </pre>
    */
   @Test
   public void itMovesTheFollowingTaskFromItsWorkPackageToTheLeadTasksWorkPackageAndUnSupresses()
         throws Exception {

      // Baseline Definitions

      // Given a JIC definition that wil be a child of all REQ definitions.
      final TaskTaskKey lJicDefn = Domain.createJobCardDefinition();

      // Given a REQ definition that will follow (be completed by another REQ definition)
      final TaskTaskKey lFollowReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefinition ) {
                  aReqDefinition.addJobCardDefinition( lJicDefn );
               }
            } );

      // Given a REQ definition that will neighbour the following REQ definition in the same Work
      // Package
      final TaskTaskKey lNeighbourReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefinition ) {
                  aReqDefinition.addJobCardDefinition( lJicDefn );
               }
            } );

      // Given a REQ definition that will act as a second neighbour the following REQ definition in
      // the same Work
      // Package
      final TaskTaskKey lSecondNeighbourReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefinition ) {
                  aReqDefinition.addJobCardDefinition( lJicDefn );
               }
            } );

      final TaskTaskKey lLeadReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefinition ) {
                  aReqDefinition.addFollowingTaskDefinition( COMPLETE, lFollowReqDefn );
                  aReqDefinition.addJobCardDefinition( lJicDefn );
               }
            } );

      // Actual Tasks

      final InventoryKey lAircraft = Domain.createAircraft();

      final TaskKey lFollowingJic = Domain.createJobCard( new DomainConfiguration<JobCard>() {

         @Override
         public void configure( JobCard aJobCard ) {
            aJobCard.setInventory( lAircraft );
            aJobCard.setDefinition( lJicDefn );
            aJobCard.setStatus( ACTV );
         }
      } );

      final TaskKey lFollowingReq =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement aRequirement ) {
                  aRequirement.setInventory( lAircraft );
                  aRequirement.setDefinition( lFollowReqDefn );
                  aRequirement.addJobCard( lFollowingJic );
                  aRequirement.setStatus( ACTV );
               }
            } );

      // Neighbour is suppressed by Following
      final TaskKey lNeighbourJic = Domain.createJobCard( new DomainConfiguration<JobCard>() {

         @Override
         public void configure( JobCard aJobCard ) {
            aJobCard.setInventory( lAircraft );
            aJobCard.setDefinition( lJicDefn );
            aJobCard.setSuppressingJobCard( lFollowingJic );
            aJobCard.setStatus( ACTV );
         }
      } );

      // Second Neighbour is also suppressed by Following
      final TaskKey lSecondNeighbourJic = Domain.createJobCard( new DomainConfiguration<JobCard>() {

         @Override
         public void configure( JobCard aJobCard ) {
            aJobCard.setInventory( lAircraft );
            aJobCard.setDefinition( lJicDefn );
            aJobCard.setSuppressingJobCard( lFollowingJic );
            aJobCard.setStatus( ACTV );
         }
      } );

      final TaskKey lNeighbourReq =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement aRequirement ) {
                  aRequirement.setInventory( lAircraft );
                  aRequirement.setDefinition( lNeighbourReqDefn );
                  aRequirement.addJobCard( lNeighbourJic );
                  aRequirement.setStatus( ACTV );
               }
            } );

      final TaskKey lSecondNeighbourReq =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement aRequirement ) {
                  aRequirement.setInventory( lAircraft );
                  aRequirement.setDefinition( lSecondNeighbourReqDefn );
                  aRequirement.addJobCard( lSecondNeighbourJic );
                  aRequirement.setStatus( ACTV );
               }
            } );

      final TaskKey lLeadJic = Domain.createJobCard( new DomainConfiguration<JobCard>() {

         @Override
         public void configure( JobCard aJobCard ) {
            aJobCard.setInventory( lAircraft );
            aJobCard.setDefinition( lJicDefn );
            aJobCard.setStatus( ACTV );
         }
      } );

      final TaskKey lLeadReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aRequirement ) {
            aRequirement.setInventory( lAircraft );
            aRequirement.setDefinition( lLeadReqDefn );
            aRequirement.addJobCard( lLeadJic );
            aRequirement.setStatus( ACTV );
         }
      } );

      // Given an in-work work package against the aircraft with the lead task is assigned to it.
      TaskKey lWorkPackage = Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {
            aWorkPackage.setAircraft( lAircraft );
            aWorkPackage.setStatus( IN_WORK );
            aWorkPackage.addTask( lLeadReq );
            aWorkPackage.addWorkScopeTask( lLeadJic );
         }
      } );

      // Given another work package against the aircraft with the following and neighbour task is
      // assigned to it.
      TaskKey lAnotherWorkPackage =
            Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

               @Override
               public void configure( WorkPackage aWorkPackage ) {
                  aWorkPackage.setAircraft( lAircraft );
                  aWorkPackage.setStatus( COMMIT );
                  aWorkPackage.addTask( lFollowingReq );
                  aWorkPackage.addTask( lNeighbourReq );
                  aWorkPackage.addTask( lSecondNeighbourReq );
                  aWorkPackage.addWorkScopeTask( lFollowingJic );
               }
            } );

      // When the lead task is completed.
      new CompleteService( lLeadReq ).completeDependencyTasks( lLeadReq, iHr, iToday );

      // Then the following task is automatically completed.
      assertTaskIsCompleted( lFollowingReq );

      // Then the following task is unassigned from its original work package.
      assertReqIsNotAssignedToWorkPackage( lFollowingReq, lAnotherWorkPackage );

      // Then the following task is assigned to the lead task's work package.
      assertReqIsAssignedToWorkPackage( lFollowingReq, lWorkPackage );

      // Then the lead task's JIC has a workscope line number
      assertJICHasWorkscopeLine( lLeadJic, lWorkPackage );

      // Then the following task's JIC has been suppressed by the lead task
      isTaskIsSuppressedBy( lLeadJic, lFollowingJic );

      // Then the neighbour task is not assigned to the lead task's work package.
      assertReqIsNotAssignedToWorkPackage( lNeighbourReq, lWorkPackage );

      // Then the neighbour task REQ and JIC are still attached
      assertJicIsAssignedToReq( lNeighbourJic, lNeighbourReq );

      // Then the neighbour task REQ is not completed
      assertTaskIsNotCompleted( lNeighbourReq );

      // Then the neighbour task JIC is not completed
      assertTaskIsNotCompleted( lNeighbourJic );

      // Then either neighbour or second neighbour is the new master JIC in suppression inside the
      // second work package
      assertTrue( isTaskIsSuppressedBy( lNeighbourJic, lSecondNeighbourJic )
            || isTaskIsSuppressedBy( lSecondNeighbourJic, lNeighbourJic ) );

   }


   /**
    * Completing a task does not affect an already completed COMPLETES following task.
    *
    * <pre>
    * Given a REQ definition that completes a following REQ definition
    *   and REQ tasks against an aircraft based on those definitions
    *   and the following task is completed
    *   and an in-work work package against the aircraft with the both tasks is assigned to it
    *  When the lead task is completed
    *  Then the following task remains completed
    * </pre>
    */
   @Test
   public void itDoesNotAlterACompletedFollowingTask() throws Exception {

      // Given a REQ definition that completes a following REQ definition.
      final TaskTaskKey lFollowReqDefn = Domain.createRequirementDefinition();
      final TaskTaskKey lLeadReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefinition ) {
                  aReqDefinition.addFollowingTaskDefinition( COMPLETE, lFollowReqDefn );
               }
            } );

      // Given REQ tasks against an aircraft based on those definitions and the following task is
      // completed.
      final InventoryKey lAircraft = Domain.createAircraft();
      final TaskKey lFollowingReq =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement aRequirement ) {
                  aRequirement.setInventory( lAircraft );
                  aRequirement.setDefinition( lFollowReqDefn );
                  aRequirement.setStatus( RefEventStatusKey.COMPLETE );
               }
            } );
      final TaskKey lLeadReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aRequirement ) {
            aRequirement.setInventory( lAircraft );
            aRequirement.setDefinition( lLeadReqDefn );
         }
      } );

      // Given an in-work work package against the aircraft with both tasks assigned to it.
      Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {
            aWorkPackage.setAircraft( lAircraft );
            aWorkPackage.setStatus( IN_WORK );
            aWorkPackage.addTask( lLeadReq );
            aWorkPackage.addTask( lFollowingReq );
         }
      } );

      // When the lead task is completed.
      new CompleteService( lLeadReq ).completeDependencyTasks( lLeadReq, iHr, iToday );

      // Then the following task remains completed.
      assertTaskIsCompleted( lFollowingReq );
   }


   /**
    * Completing a task does not move an already completed COMPLETES following task from its work
    * package into the lead task's work package.
    *
    * <pre>
    * Given a REQ definition that completes a following REQ definition
    *   and REQ tasks against an aircraft based on those definitions
    *   and the following task is completed
    *   and an in-work work package against the aircraft with the lead task is assigned to it
    *   and another work package against the aircraft with the following task is assigned to it
    *  When the lead task is completed
    *  Then the following task remains in its original work package
    * </pre>
    */
   @Test
   public void itDoesNotMoveACompletedFollowingTaskFromItsWorkPackage() throws Exception {

      // Given a REQ definition that completes a following REQ definition.
      final TaskTaskKey lFollowReqDefn = Domain.createRequirementDefinition();
      final TaskTaskKey lLeadReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefinition ) {
                  aReqDefinition.addFollowingTaskDefinition( COMPLETE, lFollowReqDefn );
               }
            } );

      // Given REQ tasks against an aircraft based on those definitions and the following task is
      // completed.
      final InventoryKey lAircraft = Domain.createAircraft();
      final TaskKey lFollowingReq =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement aRequirement ) {
                  aRequirement.setInventory( lAircraft );
                  aRequirement.setDefinition( lFollowReqDefn );
                  aRequirement.setStatus( RefEventStatusKey.COMPLETE );
               }
            } );
      final TaskKey lLeadReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aRequirement ) {
            aRequirement.setInventory( lAircraft );
            aRequirement.setDefinition( lLeadReqDefn );
         }
      } );

      // Given an in-work work package against the aircraft with the lead task is assigned to it.
      Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {
            aWorkPackage.setAircraft( lAircraft );
            aWorkPackage.setStatus( IN_WORK );
            aWorkPackage.addTask( lLeadReq );
         }
      } );

      // Given another work package against the aircraft with the following task is assigned to it.
      TaskKey lAnotherWorkPackage =
            Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

               @Override
               public void configure( WorkPackage aWorkPackage ) {
                  aWorkPackage.setAircraft( lAircraft );
                  aWorkPackage.setStatus( IN_WORK );
                  aWorkPackage.addTask( lFollowingReq );
               }
            } );

      // When the lead task is completed.
      new CompleteService( lLeadReq ).completeDependencyTasks( lLeadReq, iHr, iToday );

      // Then the following task remains in its original work package.
      assertReqIsAssignedToWorkPackage( lFollowingReq, lAnotherWorkPackage );
   }


   /**
    * Completing a task does not affect a cancelled COMPLETES following task.
    *
    * <pre>
    * Given a REQ definition that completes a following REQ definition
    *   and REQ tasks against an aircraft based on those definitions
    *   and the following task is cancelled
    *   and an in-work work package against the aircraft with both tasks assigned to it
    *  When the lead task is completed
    *  Then the following task remains cancelled
    * </pre>
    */
   @Test
   public void itDoesNotAlterACancelledFollowingTask() throws Exception {

      // Given a REQ definition that completes a following REQ definition.
      final TaskTaskKey lFollowReqDefn = Domain.createRequirementDefinition();
      final TaskTaskKey lLeadReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefinition ) {
                  aReqDefinition.addFollowingTaskDefinition( COMPLETE, lFollowReqDefn );
               }
            } );

      // Given REQ tasks against an aircraft based on those definitions and the following task is
      // cancelled.
      final InventoryKey lAircraft = Domain.createAircraft();
      final TaskKey lFollowingReq =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement aRequirement ) {
                  aRequirement.setInventory( lAircraft );
                  aRequirement.setDefinition( lFollowReqDefn );
                  aRequirement.setStatus( RefEventStatusKey.CANCEL );
               }
            } );
      final TaskKey lLeadReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aRequirement ) {
            aRequirement.setInventory( lAircraft );
            aRequirement.setDefinition( lLeadReqDefn );
         }
      } );

      // Given an in-work work package against the aircraft with both tasks assigned to it.
      Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {
            aWorkPackage.setAircraft( lAircraft );
            aWorkPackage.setStatus( IN_WORK );
            aWorkPackage.addTask( lLeadReq );
            aWorkPackage.addTask( lFollowingReq );
         }
      } );

      // When the lead task is completed.
      new CompleteService( lLeadReq ).completeDependencyTasks( lLeadReq, iHr, iToday );

      // Then the following task remains cancelled.
      Assert.assertEquals( "Unexpected REQ status.", RefEventStatusKey.CANCEL,
            iEventDao.findByPrimaryKey( lFollowingReq.getEventKey() ).getEventStatus() );
   }


   /**
    * Completing a task does not move a cancelled COMPLETES following task from its work package
    * into the lead task's work package.
    *
    * <pre>
    * Given a REQ definition that completes a following REQ definition
    *   and REQ tasks against an aircraft based on those definitions
    *   and the following task is cancelled
    *   and an in-work work package against the aircraft with the lead task is assigned to it
    *   and another work package against the aircraft with the following task is assigned to it
    *  When the lead task is completed
    *  Then the following task remains in its original work package
    * </pre>
    */
   @Test
   public void itDoesNotMoveACancelledFollowingTaskFromItsWorkPackage() throws Exception {

      // Given a REQ definition that completes a following REQ definition.
      final TaskTaskKey lFollowReqDefn = Domain.createRequirementDefinition();
      final TaskTaskKey lLeadReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefinition ) {
                  aReqDefinition.addFollowingTaskDefinition( COMPLETE, lFollowReqDefn );
               }
            } );

      // Given REQ tasks against an aircraft based on those definitions and the following task is
      // cancelled.
      final InventoryKey lAircraft = Domain.createAircraft();
      final TaskKey lFollowingReq =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement aRequirement ) {
                  aRequirement.setInventory( lAircraft );
                  aRequirement.setDefinition( lFollowReqDefn );
                  aRequirement.setStatus( RefEventStatusKey.CANCEL );
               }
            } );
      final TaskKey lLeadReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aRequirement ) {
            aRequirement.setInventory( lAircraft );
            aRequirement.setDefinition( lLeadReqDefn );
         }
      } );

      // Given an in-work work package against the aircraft with the lead task is assigned to it.
      Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {
            aWorkPackage.setAircraft( lAircraft );
            aWorkPackage.setStatus( IN_WORK );
            aWorkPackage.addTask( lLeadReq );
         }
      } );

      // Given another work package against the aircraft with the following task is assigned to it.
      TaskKey lAnotherWorkPackage =
            Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

               @Override
               public void configure( WorkPackage aWorkPackage ) {
                  aWorkPackage.setAircraft( lAircraft );
                  aWorkPackage.setStatus( IN_WORK );
                  aWorkPackage.addTask( lFollowingReq );
               }
            } );

      // When the lead task is completed.
      new CompleteService( lLeadReq ).completeDependencyTasks( lLeadReq, iHr, iToday );

      // Then the following task remains in its original work package.
      assertReqIsAssignedToWorkPackage( lFollowingReq, lAnotherWorkPackage );
   }


   /**
    * Completing a task automatically completes the JIC task assigned to a COMPLETES following REQ
    * task.
    *
    * <pre>
    * Given a JIC definition
    *   and a REQ definition that completes a following REQ definition
    *   and the JIC definition is assigned to the following REQ definition
    *   and a JIC task against an aircraft based on the JIC definition
    *   and REQ tasks against an aircraft based on the REQ definitions
    *   and the JIC task assigned to the following REQ task
    *   and an in-work work package against the aircraft with both REQ tasks is assigned to it
    *  When the lead task is completed
    *  Then the following REQ task is automatically completed
    *   and the JIC task is automatically completed
    * </pre>
    */
   @Test
   public void itCompletesTheJicTaskAssignedToTheFollowingReqTask() throws Exception {

      // Given a JIC definition.
      final TaskTaskKey lFollowingJicDefnKey = Domain.createJobCardDefinition();

      // Given a REQ definition that completes a following REQ definition and the JIC definition is
      // assigned to the following REQ definition.
      final TaskTaskKey lFollowReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefinition ) {
                  aReqDefinition.addJobCardDefinition( lFollowingJicDefnKey );
               }
            } );
      final TaskTaskKey lLeadReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefinition ) {
                  aReqDefinition.addFollowingTaskDefinition( COMPLETE, lFollowReqDefn );
               }
            } );

      // Given JIC task against an aircraft based on the JIC definition.
      final InventoryKey lAircraft = Domain.createAircraft();
      final TaskKey lFollowingJic = Domain.createJobCard( new DomainConfiguration<JobCard>() {

         @Override
         public void configure( JobCard aJobCard ) {
            aJobCard.setInventory( lAircraft );
            aJobCard.setDefinition( lFollowingJicDefnKey );
         }
      } );

      // Given REQ tasks against an aircraft based on the REQ definitions and the JIC task assigned
      // to the following REQ task.
      final TaskKey lFollowingReq =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement aRequirement ) {
                  aRequirement.setInventory( lAircraft );
                  aRequirement.setDefinition( lFollowReqDefn );
                  aRequirement.setStatus( ACTV );
                  aRequirement.addJobCard( lFollowingJic );
               }
            } );
      final TaskKey lLeadReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aRequirement ) {
            aRequirement.setInventory( lAircraft );
            aRequirement.setDefinition( lLeadReqDefn );
         }
      } );

      // Given an in-work work package against the aircraft with both REQ tasks is assigned to it.
      Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {
            aWorkPackage.setAircraft( lAircraft );
            aWorkPackage.setStatus( IN_WORK );
            aWorkPackage.addTask( lLeadReq );
            aWorkPackage.addTask( lFollowingReq );
         }
      } );

      // When the lead task is completed.
      new CompleteService( lLeadReq ).completeDependencyTasks( lLeadReq, iHr, iToday );

      // Then the following REQ task is automatically completed.
      assertTaskIsCompleted( lFollowingReq );

      // Then the JIC task is automatically completed.
      assertTaskIsCompleted( lFollowingJic );
   }


   /**
    * Completing a task does not affect the assignment of a JIC task to a COMPLETES following REQ
    * task.
    *
    * <pre>
    * Given a JIC definition
    *   and a REQ definition that completes a following REQ definition
    *   and the JIC definition is assigned to the following REQ definition
    *   and a JIC task against an aircraft based on the JIC definition
    *   and REQ tasks against an aircraft based on the REQ definitions
    *   and the JIC task assigned to the following REQ task
    *   and an in-work work package against the aircraft with both REQ tasks is assigned to it
    *  When the lead task is completed
    *  Then the JIC task remains assigned to the following REQ task
    * </pre>
    */
   @Test
   public void itDoesNotUnassignAJicTaskFromTheFollowingReqTask() throws Exception {

      // Given a JIC definition.
      final TaskTaskKey lFollowingJicDefnKey = Domain.createJobCardDefinition();

      // Given a REQ definition that completes a following REQ definition and the JIC definition is
      // assigned to the following REQ definition.
      final TaskTaskKey lFollowReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefinition ) {
                  aReqDefinition.addJobCardDefinition( lFollowingJicDefnKey );
               }
            } );
      final TaskTaskKey lLeadReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefinition ) {
                  aReqDefinition.addFollowingTaskDefinition( COMPLETE, lFollowReqDefn );
               }
            } );

      // Given JIC task against an aircraft based on the JIC definition.
      final InventoryKey lAircraft = Domain.createAircraft();
      final TaskKey lFollowingJic = Domain.createJobCard( new DomainConfiguration<JobCard>() {

         @Override
         public void configure( JobCard aJobCard ) {
            aJobCard.setInventory( lAircraft );
            aJobCard.setDefinition( lFollowingJicDefnKey );
         }
      } );

      // Given REQ tasks against an aircraft based on the REQ definitions and the JIC task assigned
      // to the following REQ task.
      final TaskKey lFollowingReq =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement aRequirement ) {
                  aRequirement.setInventory( lAircraft );
                  aRequirement.setDefinition( lFollowReqDefn );
                  aRequirement.setStatus( ACTV );
                  aRequirement.addJobCard( lFollowingJic );
               }
            } );
      final TaskKey lLeadReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aRequirement ) {
            aRequirement.setInventory( lAircraft );
            aRequirement.setDefinition( lLeadReqDefn );
         }
      } );

      // Given an in-work work package against the aircraft with both REQ tasks is assigned to it.
      Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {
            aWorkPackage.setAircraft( lAircraft );
            aWorkPackage.setStatus( IN_WORK );
            aWorkPackage.addTask( lLeadReq );
            aWorkPackage.addTask( lFollowingReq );
         }
      } );

      // When the lead task is completed.
      new CompleteService( lLeadReq ).completeDependencyTasks( lLeadReq, iHr, iToday );

      // Then the JIC task remains assigned to the following REQ task.
      assertJicIsAssignedToReq( lFollowingJic, lFollowingReq );
   }


   /**
    * Completing a task does not affect the assignment of a JIC task to a COMPLETES following REQ
    * task when that following task is automatically moved to the lead task's work package.
    *
    * <pre>
    * Given a JIC definition
    *   and a REQ definition that completes a following REQ definition
    *   and the JIC definition is assigned to the following REQ definition
    *   and a JIC task against an aircraft based on the JIC definition
    *   and REQ tasks against an aircraft based on the REQ definitions
    *   and the JIC task assigned to the following REQ task
    *   and an in-work work package against the aircraft with the lead REQ task assigned to it
    *   and another work package against the aircraft with the following task assigned to it
    *  When the lead task is completed
    *  Then the following REQ task is assigned to the lead task's work package
    *   and the JIC task remains assigned to the following REQ task
    * </pre>
    */
   @Test
   public void itDoesNotUnassignAJicTaskFromTheFollowingReqTaskWhenMovedToTheLeadTasksWorkPackage()
         throws Exception {

      // Given a JIC definition.
      final TaskTaskKey lFollowingJicDefnKey = Domain.createJobCardDefinition();

      // Given a REQ definition that completes a following REQ definition and the JIC definition is
      // assigned to the following REQ definition.
      final TaskTaskKey lFollowReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefinition ) {
                  aReqDefinition.addJobCardDefinition( lFollowingJicDefnKey );
               }
            } );
      final TaskTaskKey lLeadReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefinition ) {
                  aReqDefinition.addFollowingTaskDefinition( COMPLETE, lFollowReqDefn );
               }
            } );

      // Given JIC task against an aircraft based on the JIC definition.
      final InventoryKey lAircraft = Domain.createAircraft();
      final TaskKey lFollowingJic = Domain.createJobCard( new DomainConfiguration<JobCard>() {

         @Override
         public void configure( JobCard aJobCard ) {
            aJobCard.setInventory( lAircraft );
            aJobCard.setDefinition( lFollowingJicDefnKey );
         }
      } );

      // Given REQ tasks against an aircraft based on the REQ definitions and the JIC task assigned
      // to the following REQ task.
      final TaskKey lFollowingReq =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement aRequirement ) {
                  aRequirement.setInventory( lAircraft );
                  aRequirement.setDefinition( lFollowReqDefn );
                  aRequirement.setStatus( ACTV );
                  aRequirement.addJobCard( lFollowingJic );
               }
            } );
      final TaskKey lLeadReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aRequirement ) {
            aRequirement.setInventory( lAircraft );
            aRequirement.setDefinition( lLeadReqDefn );
         }
      } );

      // Given an in-work work package against the aircraft with the lead task is assigned to it.
      TaskKey lWorkPackage = Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {
            aWorkPackage.setAircraft( lAircraft );
            aWorkPackage.setStatus( IN_WORK );
            aWorkPackage.addTask( lLeadReq );
         }
      } );

      // Given another work package against the aircraft with the following task is assigned to it.
      Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {
            aWorkPackage.setAircraft( lAircraft );
            aWorkPackage.setStatus( IN_WORK );
            aWorkPackage.addTask( lFollowingReq );
         }
      } );

      // When the lead task is completed.
      new CompleteService( lLeadReq ).completeDependencyTasks( lLeadReq, iHr, iToday );

      // Then the following REQ task is assigned to the lead task's work package.
      assertReqIsAssignedToWorkPackage( lFollowingReq, lWorkPackage );

      // Then the JIC task remains assigned to the following REQ task.
      assertJicIsAssignedToReq( lFollowingJic, lFollowingReq );
   }


   /**
    * Completing a task automatically completes multiple JIC tasks assigned to a COMPLETES following
    * REQ task.
    *
    * <pre>
    * Given many JIC definitions
    *   and a REQ definition that completes a following REQ definition
    *   and the JIC definitions are assigned to the following REQ definition
    *   and JIC tasks against an aircraft based on the many JIC definitions
    *   and REQ tasks against an aircraft based on the REQ definitions
    *   and the JIC tasks assigned to the following REQ task
    *   and an in-work work package against the aircraft with both REQ tasks is assigned to it
    *  When the lead task is completed
    *  Then the JIC tasks are automatically completed
    * </pre>
    */
   @Test
   public void itCompletesManyJicTasksAssignedToTheFollowingReqTask() throws Exception {

      // Given many JIC definitions.
      final TaskTaskKey lFollowingJicDefnKey = Domain.createJobCardDefinition();
      final TaskTaskKey lAnotherFollowingJicDefnKey = Domain.createJobCardDefinition();

      // Given a REQ definition that completes a following REQ definition and the JIC definitions
      // are assigned to the following REQ definition.
      final TaskTaskKey lFollowReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefinition ) {
                  aReqDefinition.addJobCardDefinition( lFollowingJicDefnKey );
                  aReqDefinition.addJobCardDefinition( lAnotherFollowingJicDefnKey );
               }
            } );
      final TaskTaskKey lLeadReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefinition ) {
                  aReqDefinition.addFollowingTaskDefinition( COMPLETE, lFollowReqDefn );
               }
            } );

      // Given JIC tasks against an aircraft based on the many JIC definitions.
      final InventoryKey lAircraft = Domain.createAircraft();
      final TaskKey lFollowingJic = Domain.createJobCard( new DomainConfiguration<JobCard>() {

         @Override
         public void configure( JobCard aJobCard ) {
            aJobCard.setInventory( lAircraft );
            aJobCard.setDefinition( lFollowingJicDefnKey );
         }
      } );
      final TaskKey lAnotherFollowingJic =
            Domain.createJobCard( new DomainConfiguration<JobCard>() {

               @Override
               public void configure( JobCard aJobCard ) {
                  aJobCard.setInventory( lAircraft );
                  aJobCard.setDefinition( lAnotherFollowingJicDefnKey );
               }
            } );

      // Given REQ tasks against an aircraft based on the REQ definitions and the JIC tasks assigned
      // to the following REQ task.
      final TaskKey lFollowingReq =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement aRequirement ) {
                  aRequirement.setInventory( lAircraft );
                  aRequirement.setDefinition( lFollowReqDefn );
                  aRequirement.setStatus( ACTV );
                  aRequirement.addJobCard( lFollowingJic );
                  aRequirement.addJobCard( lAnotherFollowingJic );
               }
            } );
      final TaskKey lLeadReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aRequirement ) {
            aRequirement.setInventory( lAircraft );
            aRequirement.setDefinition( lLeadReqDefn );
         }
      } );

      // Given an in-work work package against the aircraft with both REQ tasks is assigned to it.
      Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {
            aWorkPackage.setAircraft( lAircraft );
            aWorkPackage.setStatus( IN_WORK );
            aWorkPackage.addTask( lLeadReq );
            aWorkPackage.addTask( lFollowingReq );
         }
      } );

      // When the lead task is completed.
      new CompleteService( lLeadReq ).completeDependencyTasks( lLeadReq, iHr, iToday );

      // Then the JIC tasks are automatically completed.
      assertTaskIsCompleted( lFollowingJic );
      assertTaskIsCompleted( lAnotherFollowingJic );
   }


   /**
    * Completing a task automatically completes a all COMPLETES following REQ tasks and all assigned
    * JIC tasks.
    *
    * <pre>
    * Given many following REQ definitions each with a following JIC definition assigned to it
    *   and a REQ definition that completes the following REQ definitions
    *   and JIC tasks against an aircraft based on the JIC definitions
    *   and REQ tasks against an aircraft based on the REQ definitions
    *   and the JIC tasks individually assigned to the following REQ tasks
    *   and an in-work work package against the aircraft with all REQ tasks assigned to it
    *  When the lead task is completed
    *  Then the following REQ tasks are automatically completed
    *   and the following JIC tasks are automatically completed
    * </pre>
    */
   @Test
   public void itCompletesAllFollowingReqTasksAndAssignedJicTasks() throws Exception {

      // Given many following REQ definitions each with a following JIC definition assigned to it.
      final TaskTaskKey lFollowingJicDefnKey = Domain.createJobCardDefinition();
      final TaskTaskKey lFollowReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefinition ) {
                  aReqDefinition.addJobCardDefinition( lFollowingJicDefnKey );
               }
            } );
      final TaskTaskKey lAnotherFollowingJicDefnKey = Domain.createJobCardDefinition();
      final TaskTaskKey lAnotherFollowReqDefnKey =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefinition ) {
                  aReqDefinition.addJobCardDefinition( lAnotherFollowingJicDefnKey );
               }
            } );

      // Given a REQ definition that completes the following REQ definitions.
      final TaskTaskKey lLeadReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefinition ) {
                  aReqDefinition.addFollowingTaskDefinition( COMPLETE, lFollowReqDefn );
                  aReqDefinition.addFollowingTaskDefinition( COMPLETE, lAnotherFollowReqDefnKey );
               }
            } );

      // Given JIC tasks against an aircraft based on the JIC definitions.
      final InventoryKey lAircraft = Domain.createAircraft();
      final TaskKey lFollowingJic = Domain.createJobCard( new DomainConfiguration<JobCard>() {

         @Override
         public void configure( JobCard aJobCard ) {
            aJobCard.setInventory( lAircraft );
            aJobCard.setDefinition( lFollowingJicDefnKey );
         }
      } );
      final TaskKey lAnotherFollowingJic =
            Domain.createJobCard( new DomainConfiguration<JobCard>() {

               @Override
               public void configure( JobCard aJobCard ) {
                  aJobCard.setInventory( lAircraft );
                  aJobCard.setDefinition( lAnotherFollowingJicDefnKey );
               }
            } );

      // Given REQ tasks against an aircraft based on the REQ definitions
      // and the JIC tasks individually assigned to the following REQ tasks.
      final TaskKey lFollowingReq =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement aRequirement ) {
                  aRequirement.setInventory( lAircraft );
                  aRequirement.setDefinition( lFollowReqDefn );
                  aRequirement.setStatus( ACTV );
                  aRequirement.addJobCard( lFollowingJic );
               }
            } );
      final TaskKey lAnotherFollowingReq =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement aRequirement ) {
                  aRequirement.setInventory( lAircraft );
                  aRequirement.setDefinition( lAnotherFollowReqDefnKey );
                  aRequirement.setStatus( ACTV );
                  aRequirement.addJobCard( lAnotherFollowingJic );
               }
            } );
      final TaskKey lLeadReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aRequirement ) {
            aRequirement.setInventory( lAircraft );
            aRequirement.setDefinition( lLeadReqDefn );
         }
      } );

      // Given an in-work work package against the aircraft with all REQ tasks assigned to it.
      Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {
            aWorkPackage.setAircraft( lAircraft );
            aWorkPackage.setStatus( IN_WORK );
            aWorkPackage.addTask( lLeadReq );
            aWorkPackage.addTask( lFollowingReq );
            aWorkPackage.addTask( lAnotherFollowingReq );
         }
      } );

      // When the lead task is completed.
      new CompleteService( lLeadReq ).completeDependencyTasks( lLeadReq, iHr, iToday );

      // Then the following REQ tasks are automatically completed.
      assertTaskIsCompleted( lFollowingReq );
      assertTaskIsCompleted( lAnotherFollowingReq );

      // Then the following JIC tasks are automatically completed.
      assertTaskIsCompleted( lFollowingJic );
      assertTaskIsCompleted( lAnotherFollowingJic );

   }


   /**
    * Completing a task automatically completes the COMPLETES following REQ task and JIC task.
    *
    * <pre>
    * Given the REQ definition with a following JIC definition assigned to it
    *   and a REQ definition that completes the following REQ definition
    *   and JIC task against an aircraft based on the JIC definition
    *   and REQ task against an aircraft based on the REQ definition
    *   and the JIC task individually assigned to the following REQ task
    *   and an in-work work package against the aircraft with all REQ task assigned to it
    *  When the lead task is completed
    *  Then the following REQ task is automatically completed and a usage snapshot is added
    *  to the REQ task with the same values as the lead REQ task.
    * </pre>
    */
   @Test
   public void itCompletesFollowingReqTaskForCreateHistoricTaskWithUsageSnapshot()
         throws Exception {

      // Given the REQ definitions each with a following JIC definition assigned to it.
      final TaskTaskKey lFollowingJicDefnKey = Domain.createJobCardDefinition();
      final TaskTaskKey lFollowReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefinition ) {
                  aReqDefinition.addJobCardDefinition( lFollowingJicDefnKey );
               }
            } );

      // Given a REQ definition that completes the following REQ definition.
      final TaskTaskKey lLeadReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefinition ) {
                  aReqDefinition.addFollowingTaskDefinition( COMPLETE, lFollowReqDefn );
               }
            } );

      // Given JIC task against an aircraft based on the JIC definition.
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            // Usage must be equal or greater than the snapshot to pass validation
            aAircraft.addUsage( DataTypeKey.HOURS, new BigDecimal( 1000 ) );
         }
      } );

      final TaskKey lFollowingJic = Domain.createJobCard( new DomainConfiguration<JobCard>() {

         @Override
         public void configure( JobCard aJobCard ) {
            aJobCard.setInventory( lAircraft );
            aJobCard.setDefinition( lFollowingJicDefnKey );
         }
      } );

      // Given REQ task against an aircraft based on the REQ definition
      // and the JIC task assigned to the following REQ task.
      final TaskKey lFollowingReq =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement aRequirement ) {
                  aRequirement.setInventory( lAircraft );
                  aRequirement.setDefinition( lFollowReqDefn );
                  aRequirement.setStatus( ACTV );
                  aRequirement.addJobCard( lFollowingJic );
               }
            } );

      // Add a usage snapshot to the lead REQ task, as would exist on a create historic usage
      final TaskKey lLeadReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         UsageSnapshot lUsage =
               new UsageSnapshot( lAircraft, DataTypeKey.HOURS, 333, 333, 333, null, null );


         @Override
         public void configure( Requirement aRequirement ) {
            aRequirement.setInventory( lAircraft );
            aRequirement.setDefinition( lLeadReqDefn );
            aRequirement.setStatus( RefEventStatusKey.COMPLETE );
            aRequirement.addUsage( lUsage );
            aRequirement.addSubTask( lFollowingReq );
         }
      } );

      // When the lead task is completed.
      new CompleteService( lLeadReq ).completeDependencyTasks( lLeadReq, iHr, iToday );

      // assert the follow on task has the same usage snapshot as the task which completed it
      assertUsageIsEqual( lAircraft, lFollowingReq, DataTypeKey.HOURS, new Double( 333 ) );
   }


   /**
    * Completing a task automatically completes a COMPLETES following task when that following task
    * and it sets the following task actual start date
    *
    * <pre>
    * Given a REQ definition that completes a following REQ definition
    *   and REQ tasks against an aircraft based on those definitions
    *   and an in-work work package against the aircraft with the lead tasks assigned to it
    *  When the lead task is completed
    *  Then the following task is automatically completed
    *  Then the following task's actual start date is set to be the same as the lead task's actual start date
    * </pre>
    *
    */
   @Test
   public void completeDependencyTasks_actualStartDateIsSetForTheFollowingTask() throws Exception {

      // Given a REQ definition that completes a following REQ definition.
      final TaskTaskKey lFollowReqDefn = Domain.createRequirementDefinition();
      final TaskTaskKey lLeadReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefinition ) {
                  aReqDefinition.addFollowingTaskDefinition( COMPLETE, lFollowReqDefn );
               }
            } );

      // Given REQ tasks against an aircraft based on those definitions.
      final InventoryKey lAircraft = Domain.createAircraft();
      final TaskKey lFollowingReq =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement aRequirement ) {
                  aRequirement.setInventory( lAircraft );
                  aRequirement.setDefinition( lFollowReqDefn );
                  aRequirement.setStatus( ACTV );
               }
            } );
      final TaskKey lLeadReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aRequirement ) {
            aRequirement.setInventory( lAircraft );
            aRequirement.setDefinition( lLeadReqDefn );
            aRequirement.setStatus( RefEventStatusKey.COMPLETE );
         }
      } );

      // Given an in-work work package against the aircraft with both tasks assigned to it.
      Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {
            aWorkPackage.setAircraft( lAircraft );
            aWorkPackage.setStatus( IN_WORK );
            aWorkPackage.addTask( lLeadReq );
            aWorkPackage.addTask( lFollowingReq );
            aWorkPackage.setActualStartDate( iToday );
         }
      } );

      // set the actual start date for the lead task, and assume is completed.
      EvtEventTable lEvtEventTable = iEventDao.findByPrimaryKey( lLeadReq.getEventKey() );
      lEvtEventTable.setActualStartDate( iToday );
      iEventDao.update( lEvtEventTable );

      // When the lead task is completed trying to complete the following task
      new CompleteService( lLeadReq ).completeDependencyTasks( lLeadReq, iHr, iToday );

      // Then the following task is automatically completed.
      assertTaskIsCompleted( lFollowingReq );
      assertTrue( "Expected actual start dates to be the same.",
            assertActualStartDates( iToday, lFollowingReq ) );
   }


   /**
    *
    * Completing a Task (LEAD) task will cause dependent COMPLETE task (DEPEND) to be completed as
    * well. We need to make sure that when a LEAD task is completed DEPEND has the same usage
    * snapshot as LEAD.
    *
    * @throws TriggerException
    * @throws MxException
    *
    */
   @Test
   public void itCapturesTheCorrectUsageSnapshotForACompleteDependencyTask() throws Exception {

      // ARRANGE

      final BigDecimal lCurrentCycles = new BigDecimal( 10 );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( DataTypeKey.CYCLES, lCurrentCycles );
         }
      } );

      final TaskTaskKey lDependentReqDefnKey =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefinition ) {
                  aReqDefinition.setCode( "DEP_REQ" );
               }
            } );

      final TaskKey lDependentReq =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement aRequirement ) {
                  aRequirement.setInventory( lAircraft );
                  aRequirement.setDefinition( lDependentReqDefnKey );
                  aRequirement.setStatus( ACTV );
               }
            } );

      final TaskTaskKey lLeadReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefinition ) {
                  aReqDefinition.addFollowingTaskDefinition( COMPLETE, lDependentReqDefnKey );
                  aReqDefinition.setCode( "LEAD_REQ" );
               }
            } );

      final TaskKey lLeadReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aRequirement ) {
            aRequirement.setInventory( lAircraft );
            aRequirement.setDefinition( lLeadReqDefn );
         }
      } );

      final BigDecimal lUsageDelta = new BigDecimal( 5 );

      // historic flight 10 days ago
      final Date lHistoricFlightDate1 = DateUtils.addDays( iToday, -10 );
      final BigDecimal lCycles1 = new BigDecimal( 10 );
      accrueFlightUsage( lAircraft, lHistoricFlightDate1, lCycles1, lUsageDelta,
            DataTypeKey.CYCLES );

      // historic flight 5 days ago
      final Date lHistoricFlightDate2 = DateUtils.addDays( iToday, -5 );
      final BigDecimal lCycles2 = new BigDecimal( 15 );
      accrueFlightUsage( lAircraft, lHistoricFlightDate2, lCycles2, lUsageDelta,
            DataTypeKey.CYCLES );

      // historic flight today
      final BigDecimal lCycles3 = new BigDecimal( 20 );
      accrueFlightUsage( lAircraft, iToday, lCycles3, lUsageDelta, DataTypeKey.CYCLES );

      // call a copy of the new logic to capture a snapshot by date
      Date l8DaysAgo = DateUtils.addDays( iToday, -8 );
      createUsageSnapshotAtDate( lLeadReq, lAircraft, l8DaysAgo );

      // ACT

      // Complete the dependent tasks given the lead task
      new CompleteService( lLeadReq ).completeDependencyTasks( lLeadReq, iHr, iToday );

      // ASSERT

      // make sure the dependent req tasks is completed.
      assertTaskIsCompleted( lDependentReq );

      // get the lead usage snapshot
      EventInventoryService lLeadInventoryService = new EventInventoryService( lLeadReq );
      UsageSnapshot[] lLeadUsageSnapshots = lLeadInventoryService.getUsageSnapshot();
      UsageSnapshot lLeadUsageSnapshot = lLeadUsageSnapshots[0];
      double lLeadTsn = lLeadUsageSnapshot.getTSN();

      // get the dependent usage snapshot
      EventInventoryService lDependentInventoryService = new EventInventoryService( lDependentReq );
      UsageSnapshot[] lDependentUsageSnapshots = lDependentInventoryService.getUsageSnapshot();

      int lNumOfSanpshots = lDependentUsageSnapshots.length;
      Assert.assertTrue( "Expected 1 Usage Snapshot but got " + lNumOfSanpshots,
            lNumOfSanpshots >= 1 );
      UsageSnapshot lDependentUsageSnapshot = lDependentUsageSnapshots[0];

      // make sure they are the same
      double lDependentTsn = lDependentUsageSnapshot.getTSN();
      Assert.assertTrue( "Expected Usage of " + lLeadTsn + " but got " + lDependentTsn,
            lDependentTsn == lLeadTsn );

   }


   /**
    * Completing a task automatically completes the COMPLETES following REQ task and assigned JIC
    * task with a usage snapshot.
    *
    * <pre>
    * Given the REQ definition with a following JIC definition assigned to it
    *   and a REQ definition that completes the following REQ definition
    *   and JIC task against an aircraft based on the JIC definition
    *   and REQ task against an aircraft based on the REQ definition
    *   and the JIC task assigned to the following REQ task
    *   and an in-work work package with usage against the aircraft with all REQ tasks assigned to it
    *  When the lead task is completed the usage snapshot is pulled down to the completed task.
    * </pre>
    */
   @Test
   public void itCompletesFollowingReqTasksAndAssignedJicTasksWithUsageSnapshot() throws Exception {

      final BigDecimal lCurrentCdy = new BigDecimal( 300 );
      final BigDecimal lCurrentLanding = new BigDecimal( 400 );

      // Given an aircraft with usage parameters and current usage with
      // usage parameters and current usage.
      //
      // Note: Use all different data types to avoid the logic that "auto-populates"
      // sub-component's usage when the sub-component is using the same data types as the main
      // component.

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( CDY, lCurrentCdy );
            aAircraft.addUsage( LANDING, lCurrentLanding );
         }
      } );

      accrueFlightUsage( lAircraft, iToday, lCurrentLanding, BigDecimal.ZERO, LANDING );
      accrueFlightUsage( lAircraft, iToday, lCurrentCdy, BigDecimal.ZERO, CDY );

      assertCurrentUsages( lAircraft, CDY, lCurrentCdy );
      assertCurrentUsages( lAircraft, LANDING, lCurrentLanding );

      // Given a REQ definition each with a following JIC definition assigned to it.
      final TaskTaskKey lFollowingJicDefnKey = Domain.createJobCardDefinition();
      final TaskTaskKey lFollowReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefinition ) {
                  aReqDefinition.addJobCardDefinition( lFollowingJicDefnKey );
               }
            } );
      // Given a REQ definition that completes the following REQ definition.
      final TaskTaskKey lLeadReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefinition ) {
                  aReqDefinition.addFollowingTaskDefinition( COMPLETE, lFollowReqDefn );
               }
            } );

      // Given JIC task against an aircraft based on the JIC definition.
      final TaskKey lFollowingJic = Domain.createJobCard( new DomainConfiguration<JobCard>() {

         @Override
         public void configure( JobCard aJobCard ) {
            aJobCard.setInventory( lAircraft );
            aJobCard.setDefinition( lFollowingJicDefnKey );
         }
      } );

      // Given REQ task against an aircraft based on the REQ definition
      // and the JIC task assigned to the following REQ task.
      final TaskKey lFollowingReq =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement aRequirement ) {
                  aRequirement.setInventory( lAircraft );
                  aRequirement.setDefinition( lFollowReqDefn );
                  aRequirement.setStatus( ACTV );
                  aRequirement.addJobCard( lFollowingJic );
               }
            } );

      final TaskKey lLeadReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aRequirement ) {
            aRequirement.setInventory( lAircraft );
            aRequirement.setDefinition( lLeadReqDefn );
         }
      } );

      // Given an in-work work package against the aircraft with all REQ tasks assigned to it.
      Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {
            aWorkPackage.setAircraft( lAircraft );
            aWorkPackage.setStatus( IN_WORK );
            aWorkPackage.addTask( lLeadReq );
            aWorkPackage.addTask( lFollowingReq );
         }
      } );

      createUsageSnapshotAtDate( lLeadReq, lAircraft, iToday );

      // When the lead task is completed.
      new CompleteService( lLeadReq ).completeDependencyTasks( lLeadReq, iHr, iToday );

      // assert the follow on tasks has the same usage snapshot as the task which completed it
      assertUsageIsEqual( lAircraft, lFollowingReq, LANDING, lCurrentLanding.doubleValue() );
      assertUsageIsEqual( lAircraft, lFollowingReq, CDY, lCurrentCdy.doubleValue() );
   }


   @Test
   public void itCompletesFollowingReqTasksWithUsageSnapshotForLooseComponentWithoutFlightUsages()
         throws Exception {

      final BigDecimal lInitialCycles = new BigDecimal( 100 );
      final BigDecimal iInitialHours = new BigDecimal( 200 );
      final Date lEngineRemovalDate = DateUtils.addDays( ENGINE_INSTALL_DATE, 5 );

      // Given a loose sub-assembly that was previously installed on the aircraft.
      final InventoryKey lEngineInvKey = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.addUsage( CYCLES, lInitialCycles );
            aEngine.addUsage( HOURS, iInitialHours );
            aEngine.addInstallationRecord( new DomainConfiguration<InstallationRecord>() {

               @Override
               public void configure( InstallationRecord aInstallRecord ) {
                  aInstallRecord.setInstallationDate( ENGINE_INSTALL_DATE );
               }
            } );
            aEngine.addRemovalRecord( new DomainConfiguration<RemovalRecord>() {

               @Override
               public void configure( RemovalRecord aRemovalRecord ) {
                  aRemovalRecord.setRemovalDate( lEngineRemovalDate );
               }
            } );
         }
      } );

      assertCurrentUsages( lEngineInvKey, CYCLES, lInitialCycles );
      assertCurrentUsages( lEngineInvKey, HOURS, iInitialHours );

      // Given a REQ definition with a following JIC definition assigned to it.
      final TaskTaskKey lFollowingJicDefnKey = Domain.createJobCardDefinition();
      final TaskTaskKey lFollowReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefinition ) {
                  aReqDefinition.addJobCardDefinition( lFollowingJicDefnKey );
               }
            } );
      // Given a REQ definition that completes the following REQ definition.
      final TaskTaskKey lLeadReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefinition ) {
                  aReqDefinition.addFollowingTaskDefinition( COMPLETE, lFollowReqDefn );
               }
            } );

      // Given JIC task against a loose engine based on the JIC definition.
      final TaskKey lFollowingJic = Domain.createJobCard( new DomainConfiguration<JobCard>() {

         @Override
         public void configure( JobCard aJobCard ) {
            aJobCard.setInventory( lEngineInvKey );
            aJobCard.setDefinition( lFollowingJicDefnKey );
         }
      } );

      // Given a REQ task against a loose engine based on the REQ definitions
      // and the JIC task assigned to the following REQ task.
      final TaskKey lFollowingReq =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement aRequirement ) {
                  aRequirement.setInventory( lEngineInvKey );
                  aRequirement.setDefinition( lFollowReqDefn );
                  aRequirement.setStatus( ACTV );
                  aRequirement.addJobCard( lFollowingJic );
               }
            } );

      final TaskKey lLeadReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aRequirement ) {
            aRequirement.setInventory( lEngineInvKey );
            aRequirement.setDefinition( lLeadReqDefn );
         }
      } );

      // Given an in-work work package against the loose engine with all REQ tasks assigned to it.
      Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {
            aWorkPackage.setAircraft( lEngineInvKey );
            aWorkPackage.setStatus( IN_WORK );
            aWorkPackage.addTask( lLeadReq );
            aWorkPackage.addTask( lFollowingReq );
         }
      } );

      // When the lead task is completed.
      new CompleteService( lLeadReq ).completeDependencyTasks( lLeadReq, iHr, iToday );

      // assert the follow on task has the same usage snapshot as the task which completed it
      assertUsageIsEqual( lEngineInvKey, lFollowingReq, CYCLES, lInitialCycles.doubleValue() );
      assertUsageIsEqual( lEngineInvKey, lFollowingReq, HOURS, iInitialHours.doubleValue() );
   }


   /**
    * Completing a task automatically completes COMPLETES following REQ task and it assigned JIC
    * task with a usage snapshot for loose inventory.
    *
    * <pre>
    * Given an ENGINE
    *    ENGINE_INSTALL_DATE = today-100
    *    CYCLES = 100
    *    HOURS = 200
    * FLIGHT:
    *    DATE = ENGINE_INSTALL_DATE + 1
    *    CYCLES = 1
    *    HOURS = 5
    * EVENT (REQ):
    *    DATE = today
    *    CYCLES = 101
    *    HOURS = 205
    * Given a REQ definition each with a following JIC definition assigned to it
    *   and a REQ definition that completes the following REQ definition
    *   and JIC task against a loose engine based on the JIC definition
    *   and REQ task against a loose engine based on the REQ definition
    *   and the JIC task assigned to the following REQ task
    *   and an in-work work package with all REQ task assigned to it
    *  When the lead task is completed the usage snapshot is pulled down to the completed task.
    * </pre>
    */
   @Test
   public void itCompletesFollowingReqTasksWithUsageSnapshotForLooseComponent() throws Exception {

      final BigDecimal lInitialCycles = new BigDecimal( 100 );
      final BigDecimal iInitialHours = new BigDecimal( 200 );
      final BigDecimal lDeltaHours = new BigDecimal( 5 );
      final BigDecimal lExpectedCycles;
      final BigDecimal lExpectedHours;
      final Date lEngineRemovalDate = DateUtils.addDays( ENGINE_INSTALL_DATE, 5 );

      // Given a loose sub-assembly that was previously installed on the aircraft.
      final InventoryKey lEngineInvKey = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.addUsage( CYCLES, lInitialCycles );
            aEngine.addUsage( HOURS, iInitialHours );
            aEngine.addInstallationRecord( new DomainConfiguration<InstallationRecord>() {

               @Override
               public void configure( InstallationRecord aInstallRecord ) {
                  aInstallRecord.setInstallationDate( ENGINE_INSTALL_DATE );
               }
            } );
            aEngine.addRemovalRecord( new DomainConfiguration<RemovalRecord>() {

               @Override
               public void configure( RemovalRecord aRemovalRecord ) {
                  aRemovalRecord.setRemovalDate( lEngineRemovalDate );
               }
            } );
         }
      } );

      assertCurrentUsages( lEngineInvKey, CYCLES, lInitialCycles );
      assertCurrentUsages( lEngineInvKey, HOURS, iInitialHours );

      // Capture flight and ensure that engine has been updated
      accrueFlightUsage( lEngineInvKey, DateUtils.addDays( ENGINE_INSTALL_DATE, 1 ), iInitialHours,
            lDeltaHours, HOURS );

      assertCurrentUsages( lEngineInvKey, CYCLES, lInitialCycles.add( BigDecimal.ONE ) );
      assertCurrentUsages( lEngineInvKey, HOURS, iInitialHours.add( lDeltaHours ) );

      lExpectedCycles = lInitialCycles.add( BigDecimal.ONE );
      lExpectedHours = iInitialHours.add( lDeltaHours );

      // Given a REQ definition with a following JIC definition assigned to it.
      final TaskTaskKey lFollowingJicDefnKey = Domain.createJobCardDefinition();
      final TaskTaskKey lFollowReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefinition ) {
                  aReqDefinition.addJobCardDefinition( lFollowingJicDefnKey );
               }
            } );
      // Given a REQ definition that completes the following REQ definition.
      final TaskTaskKey lLeadReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefinition ) {
                  aReqDefinition.addFollowingTaskDefinition( COMPLETE, lFollowReqDefn );
               }
            } );

      // Given JIC task against a loose engine based on the JIC definition.
      final TaskKey lFollowingJic = Domain.createJobCard( new DomainConfiguration<JobCard>() {

         @Override
         public void configure( JobCard aJobCard ) {
            aJobCard.setInventory( lEngineInvKey );
            aJobCard.setDefinition( lFollowingJicDefnKey );
         }
      } );

      // Given a REQ task against a loose engine based on the REQ definitions
      // and the JIC task assigned to the following REQ task.
      final TaskKey lFollowingReq =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement aRequirement ) {
                  aRequirement.setInventory( lEngineInvKey );
                  aRequirement.setDefinition( lFollowReqDefn );
                  aRequirement.setStatus( ACTV );
                  aRequirement.addJobCard( lFollowingJic );
               }
            } );

      final TaskKey lLeadReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aRequirement ) {
            aRequirement.setInventory( lEngineInvKey );
            aRequirement.setDefinition( lLeadReqDefn );
         }
      } );

      // Given an in-work work package against the loose engine with all REQ tasks assigned to it.
      Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {
            aWorkPackage.setAircraft( lEngineInvKey );
            aWorkPackage.setStatus( IN_WORK );
            aWorkPackage.addTask( lLeadReq );
            aWorkPackage.addTask( lFollowingReq );
         }
      } );

      createUsageSnapshotAtDate( lLeadReq, lEngineInvKey, iToday );

      // When the lead task is completed.
      new CompleteService( lLeadReq ).completeDependencyTasks( lLeadReq, iHr, iToday );

      // assert the follow on task has the same usage snapshot as the task which completed it
      assertUsageIsEqual( lEngineInvKey, lFollowingReq, CYCLES, lExpectedCycles.doubleValue() );
      assertUsageIsEqual( lEngineInvKey, lFollowingReq, HOURS, lExpectedHours.doubleValue() );
   }


   /**
    * Creating an historic task automatically completes the COMPLETES following REQ task and its JIC
    * task with a usage snapshot for loose inventory.
    *
    * <pre>
    * Given a following REQ definition with a following JIC definition assigned to it
    *   and a REQ definition that completes the following REQ definition
    *   and JIC task against a loose engine based on the JIC definition
    *   and a completed REQ tasks against a loose engine based on the REQ definition
    *   and the completed JIC task assigned to the following REQ task
    *  When the lead task is completed the usage snapshot is pulled down to the completed task.
    * </pre>
    */
   @Test
   public void itCompletesFollowingReqTasksWithUsageSnapshotForLooseComponentWithCreateHistoric()
         throws Exception {

      final BigDecimal lInitialCycles = new BigDecimal( 100 );
      final BigDecimal iInitialHours = new BigDecimal( 200 );
      final Date lEngineRemovalDate = DateUtils.addDays( ENGINE_INSTALL_DATE, 5 );

      // Given a loose sub-assembly that was previously installed on the aircraft.
      final InventoryKey lEngineInvKey = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.addUsage( CYCLES, lInitialCycles );
            aEngine.addUsage( HOURS, iInitialHours );
            aEngine.addInstallationRecord( new DomainConfiguration<InstallationRecord>() {

               @Override
               public void configure( InstallationRecord aInstallRecord ) {
                  aInstallRecord.setInstallationDate( ENGINE_INSTALL_DATE );
               }
            } );
            aEngine.addRemovalRecord( new DomainConfiguration<RemovalRecord>() {

               @Override
               public void configure( RemovalRecord aRemovalRecord ) {
                  aRemovalRecord.setRemovalDate( lEngineRemovalDate );
               }
            } );
         }
      } );

      assertCurrentUsages( lEngineInvKey, CYCLES, lInitialCycles );
      assertCurrentUsages( lEngineInvKey, HOURS, iInitialHours );

      accrueFlightUsage( lEngineInvKey, iToday, lInitialCycles, BigDecimal.ZERO, CYCLES );
      accrueFlightUsage( lEngineInvKey, iToday, iInitialHours, BigDecimal.ZERO, HOURS );

      // Given a following REQ definition with a following JIC definition assigned to it.
      final TaskTaskKey lFollowingJicDefnKey = Domain.createJobCardDefinition();
      final TaskTaskKey lFollowReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefinition ) {
                  aReqDefinition.addJobCardDefinition( lFollowingJicDefnKey );
               }
            } );
      // Given a REQ definition that completes the following REQ definitions.
      final TaskTaskKey lLeadingJicDefnKey = Domain.createJobCardDefinition();
      final TaskTaskKey lLeadReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefinition ) {
                  aReqDefinition.addFollowingTaskDefinition( COMPLETE, lFollowReqDefn );
                  aReqDefinition.addJobCardDefinition( lLeadingJicDefnKey );
               }
            } );

      // Given JIC task against a loose engine based on the JIC definition.
      final TaskKey lFollowingJic = Domain.createJobCard( new DomainConfiguration<JobCard>() {

         @Override
         public void configure( JobCard aJobCard ) {
            aJobCard.setInventory( lEngineInvKey );
            aJobCard.setDefinition( lFollowingJicDefnKey );
         }
      } );

      // Given a REQ task against a loose engine based on the REQ definition
      // and the JIC task assigned to the following REQ task.
      final TaskKey lFollowingReq =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement aRequirement ) {
                  aRequirement.setInventory( lEngineInvKey );
                  aRequirement.setDefinition( lFollowReqDefn );
                  aRequirement.setStatus( ACTV );
                  aRequirement.addJobCard( lFollowingJic );
               }
            } );

      // Given JIC task against a loose engine based on the JIC definition.
      final TaskKey lLeadingJic = Domain.createJobCard( new DomainConfiguration<JobCard>() {

         @Override
         public void configure( JobCard aJobCard ) {
            aJobCard.setInventory( lEngineInvKey );
            aJobCard.setDefinition( lLeadingJicDefnKey );
            aJobCard.setStatus( RefEventStatusKey.COMPLETE );
         }
      } );

      final TaskKey lLeadReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aRequirement ) {
            aRequirement.setInventory( lEngineInvKey );
            aRequirement.setDefinition( lLeadReqDefn );
            aRequirement.addJobCard( lLeadingJic );
            aRequirement.setStatus( RefEventStatusKey.COMPLETE );
         }
      } );

      createUsageSnapshotAtDate( lLeadReq, lEngineInvKey, iToday );

      // When the lead task is completed.
      new CompleteService( lLeadReq ).completeDependencyTasks( lLeadReq, iHr, iToday );

      // assert the follow on tasks has the same usage snapshot as the task which completed it
      assertUsageIsEqual( lEngineInvKey, lFollowingReq, CYCLES, lInitialCycles.doubleValue() );
      assertUsageIsEqual( lEngineInvKey, lFollowingReq, HOURS, iInitialHours.doubleValue() );
   }


   /**
    * Completing a task automatically completes the entire chain of COMPLETES following tasks.
    *
    * <pre>
    * Given three REQ definitions
    *   and the lead REQ definition completes the first following REQ definition
    *   and the first following REQ definition completes the second following REQ definition
    *   and REQ tasks against an aircraft based on those definitions
    *   and an in-work work package against the aircraft with all tasks assigned to it
    *  When the lead task is completed
    *  Then the following tasks are automatically completed
    * </pre>
    */
   @Test
   public void itCompletesAChainOfFollowingTasks() throws Exception {
      // Given three REQ definitions and the lead REQ definition completes the first following REQ
      // definition and the first following REQ definition completes the second following REQ
      // definition.
      final TaskTaskKey lSecondFollowReqDefnKey = Domain.createRequirementDefinition();
      final TaskTaskKey lFirstFollowReqDefnKey =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefinition ) {
                  aReqDefinition.addFollowingTaskDefinition( COMPLETE, lSecondFollowReqDefnKey );
               }
            } );
      final TaskTaskKey lLeadReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefinition ) {
                  aReqDefinition.addFollowingTaskDefinition( COMPLETE, lFirstFollowReqDefnKey );
               }
            } );

      // Given REQ tasks against an aircraft based on those definitions.
      final InventoryKey lAircraft = Domain.createAircraft();
      final TaskKey lSecondFollowingReq =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement aRequirement ) {
                  aRequirement.setInventory( lAircraft );
                  aRequirement.setDefinition( lSecondFollowReqDefnKey );
                  aRequirement.setStatus( ACTV );
               }
            } );
      final TaskKey lFirstFollowingReq =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement aRequirement ) {
                  aRequirement.setInventory( lAircraft );
                  aRequirement.setDefinition( lFirstFollowReqDefnKey );
                  aRequirement.setStatus( ACTV );
               }
            } );
      final TaskKey lLeadReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aRequirement ) {
            aRequirement.setInventory( lAircraft );
            aRequirement.setDefinition( lLeadReqDefn );
         }
      } );

      // Given an in-work work package against the aircraft with all tasks assigned to it.
      Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {
            aWorkPackage.setAircraft( lAircraft );
            aWorkPackage.setStatus( IN_WORK );
            aWorkPackage.addTask( lLeadReq );
            aWorkPackage.addTask( lFirstFollowingReq );
            aWorkPackage.addTask( lSecondFollowingReq );
         }
      } );

      // When the lead task is completed.
      new CompleteService( lLeadReq ).completeDependencyTasks( lLeadReq, iHr, iToday );

      // Then the following REQ tasks are automatically completed.
      assertTaskIsCompleted( lFirstFollowingReq );
      assertTaskIsCompleted( lSecondFollowingReq );
   }


   @Test
   public void completeDependencyTasks_followingTaskIsUnderForecastBlock() throws Exception {

      // data set up
      final TaskTaskKey lReqDefnKey2 = Domain.createRequirementDefinition(
            aReqDefinition -> aReqDefinition.setStatus( RefTaskDefinitionStatusKey.ACTV ) );

      final TaskTaskKey lReqDefnKey1 = Domain.createRequirementDefinition(
            aReqDefinition -> aReqDefinition.setStatus( RefTaskDefinitionStatusKey.ACTV ) );

      final TaskTaskKey lReqDefnKey3 = Domain.createRequirementDefinition( aReqDefinition -> {
         aReqDefinition.setRecurring( true );
         aReqDefinition.setStatus( RefTaskDefinitionStatusKey.ACTV );
         aReqDefinition.addFollowingTaskDefinition( COMPLETE, lReqDefnKey1 );
      } );

      final TaskTaskKey lBlockDefnKey = Domain.createBlockDefinition( aBlockDefn -> {
         aBlockDefn.setOnCondition( true );
         aBlockDefn.setRecurring( true );
         aBlockDefn.addRequirementDefinition( lReqDefnKey1 );
         aBlockDefn.addRequirementDefinition( lReqDefnKey2 );
         aBlockDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
      } );

      final InventoryKey lAircraft = Domain.createAircraft();

      final TaskKey lBlockKey = Domain.createBlock( aBlock -> {
         aBlock.setInventory( lAircraft );
         aBlock.setDefinition( lBlockDefnKey );
         aBlock.setStatus( RefEventStatusKey.FORECAST );
      } );

      final TaskKey lReqKey1 = Domain.createRequirement( aRequirement -> {
         aRequirement.setInventory( lAircraft );
         aRequirement.setDefinition( lReqDefnKey1 );
         aRequirement.setStatus( ACTV );
         aRequirement.setParentTask( lBlockKey );
      } );

      final TaskKey lReqKey2 = Domain.createRequirement( aRequirement -> {
         aRequirement.setInventory( lAircraft );
         aRequirement.setDefinition( lReqDefnKey2 );
         aRequirement.setStatus( ACTV );
         aRequirement.setParentTask( lBlockKey );
      } );

      final TaskKey lLeadReq = Domain.createRequirement( aRequirement -> {
         aRequirement.setInventory( lAircraft );
         aRequirement.setDefinition( lReqDefnKey3 );
         aRequirement.setStatus( ACTV );
      } );

      Domain.createWorkPackage( aWorkPackage -> {
         aWorkPackage.setAircraft( lAircraft );
         aWorkPackage.setStatus( IN_WORK );
         aWorkPackage.addTask( lLeadReq );
      } );

      // complete dependency tasks of lead task
      new CompleteService( lLeadReq ).completeDependencyTasks( lLeadReq, iHr, iToday );

      // assert dependent task is completed
      assertTaskIsCompleted( lReqKey1 );

      // assert non-dependent task is in active status
      Assert.assertEquals( RefEventStatusKey.ACTV,
            iEventDao.findByPrimaryKey( lReqKey2.getEventKey() ).getEventStatus() );
   }


   @Before
   public void before() {
      iHr = new HumanResourceDomainBuilder().build();
      iUserId = OrgHr.findByPrimaryKey( iHr ).getUserId();
      UserParametersStub lUserParametersStub = new UserParametersStub( iUserId, "LOGIC" );
      lUserParametersStub.setBoolean( "ALLOW_ASSIGN_LRP_ITEM_FOR_INWORK_WP", false );
      UserParameters.setInstance( iUserId, "LOGIC", lUserParametersStub );

   }


   @After
   public void after() {
      UserParameters.setInstance( iUserId, "LOGIC", null );
   }


   private boolean assertActualStartDates( Date aLeadTaskActualStartDate, TaskKey aFollowingTask ) {

      SimpleDateFormat lSimpleDateFormat = new SimpleDateFormat( "dd-MMM-yyyy HH:mm:ss" );
      String lLeadTaskActualStartDate = lSimpleDateFormat.format( aLeadTaskActualStartDate );

      EvtEventTable lFollowingEvent =
            EvtEventTable.findByPrimaryKey( aFollowingTask.getEventKey() );
      String lFollowingEventActualStartDate =
            lSimpleDateFormat.format( lFollowingEvent.getActualStartDt() );

      return ( lLeadTaskActualStartDate.compareTo( lFollowingEventActualStartDate ) == 0 );
   }


   private void assertTaskIsCompleted( TaskKey aTask ) {
      RefEventStatusKey lTaskStatus =
            iEventDao.findByPrimaryKey( aTask.getEventKey() ).getEventStatus();
      Assert.assertEquals( "Unexpected task status.", RefEventStatusKey.COMPLETE, lTaskStatus );
   }


   private void assertTaskIsNotCompleted( TaskKey aTask ) {
      RefEventStatusKey lTaskStatus =
            iEventDao.findByPrimaryKey( aTask.getEventKey() ).getEventStatus();
      Assert.assertNotEquals( "Unexpected task status.", RefEventStatusKey.COMPLETE, lTaskStatus );
   }


   private void assertReqIsAssignedToWorkPackage( TaskKey aReq, TaskKey aWorkPackage ) {
      Assert.assertTrue( "Expected REQ to be assigned to the work package.",
            isTaskAssignedToTask( aReq, aWorkPackage ) );
   }


   private void assertJICHasWorkscopeLine( TaskKey aJic, TaskKey aWorkPackage ) {
      Assert.assertTrue( "Expected JIC to have a workscope line number.",
            jicHasWorkscopeLine( aJic, aWorkPackage ) );
   }


   private void assertJicIsAssignedToReq( TaskKey aJic, TaskKey aReq ) {
      Assert.assertTrue( "Expected JIC to be assigned to the REQ.",
            isTaskAssignedToTask( aJic, aReq ) );
   }


   private void assertReqIsNotAssignedToWorkPackage( TaskKey aReq, TaskKey aWorkPackage ) {
      Assert.assertFalse( "Expected REQ to not be assigned to work package.",
            isTaskAssignedToTask( aReq, aWorkPackage ) );
   }


   private boolean isTaskIsSuppressedBy( TaskKey aSlaveTask, TaskKey aSuppressorTask ) {
      for ( TaskKey lTask : new TaskSuppressionService()
            .findSuppressedJobCards( aSuppressorTask ) ) {
         if ( lTask.equals( aSlaveTask ) ) {
            return true;
         }
      }
      return false;
   }


   private boolean jicHasWorkscopeLine( TaskKey aJic, TaskKey aWorkPackage ) {
      return SchedStaskUtils.getWorkLineKey( aWorkPackage, aJic ) != null;
   }


   private boolean isTaskAssignedToTask( TaskKey aChildTask, TaskKey aParentTask ) {
      EventKey lParentEvent = iEventDao.findByPrimaryKey( aParentTask.getEventKey() ).getPk();
      EventKey lChildNextHighestEvent =
            iEventDao.findByPrimaryKey( aChildTask.getEventKey() ).getNhEvent();

      return lParentEvent.equals( lChildNextHighestEvent );
   }


   private void assertCurrentUsages( InventoryKey aInventory, DataTypeKey aDataType,
         BigDecimal aExpectedUsage ) {

      CurrentUsages lCurrentUsage = new CurrentUsages( aInventory );
      assertEquals( "Inventory=" + aInventory + " , data type=" + aDataType + " : unexpected TSN",
            aExpectedUsage, lCurrentUsage.getTsn( aDataType ) );
      assertEquals( "Inventory=" + aInventory + " , data type=" + aDataType + " : unexpected TSO",
            aExpectedUsage, lCurrentUsage.getTso( aDataType ) );
      assertEquals( "Inventory=" + aInventory + " , data type=" + aDataType + " : unexpected TSI",
            aExpectedUsage, lCurrentUsage.getTsi( aDataType ) );

   }


   private void assertUsageIsEqual( final InventoryKey aInventoryKey, final TaskKey aFollowingReq,
         DataTypeKey aDataType, Double aUsageValue ) {
      EvtInvTable lEvtInvTable =
            EvtInvTable.findByEventAndInventory( aFollowingReq, aInventoryKey );
      EvtInvUsageTable lEvtInvUsageTable = EvtInvUsageTable
            .findByPrimaryKey( new EventInventoryUsageKey( lEvtInvTable.getPk(), aDataType ) );
      Assert.assertEquals( aUsageValue, new Double( lEvtInvUsageTable.getTsnQt() ) );
   }


   /**
    * Helper method to accrue usage on an aircraft in the past by adding a flight
    */
   private void accrueFlightUsage( final InventoryKey aAircraft, final Date aUsageDate,
         final BigDecimal aUsageRecord, final BigDecimal aUsageDelta, final DataTypeKey aDataType )
         throws Exception {

      final UsageAdjustmentId lUsageRecordKey =
            Domain.createUsageAdjustment( new DomainConfiguration<UsageAdjustment>() {

               @Override
               public void configure( UsageAdjustment aUsageAdjustment ) {
                  aUsageAdjustment.setUsageDate( aUsageDate );
                  aUsageAdjustment.setMainInventory( aAircraft );
                  aUsageAdjustment.addUsage( aAircraft, aDataType, aUsageRecord, aUsageDelta );
               }
            } );

      FlightLegId aFlightId = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( aAircraft );
            aFlight.setUsageRecord( lUsageRecordKey );
         }
      } );

      UsageAccrualService lUsageAccrualService =
            new MxUsageAccrualService( new MxUpdateDeadlineService( new MxSoftDeadlineService() ) );

      // When the flight leg's usage is applied.
      boolean lShouldDefaultUsageBeApplied = false;
      lUsageAccrualService.applyUsageDelta( new ActualDatesCalculator(), aFlightId,
            lShouldDefaultUsageBeApplied );

   }


   /**
    *
    * create a usage snapshot for an task based on the provided date
    *
    * @param aMainInventory
    * @param aActualStartDate
    *
    * @throws ValidationException
    */
   private void createUsageSnapshotAtDate( TaskKey aTaskKey, InventoryKey aMainInventory,
         Date aActualStartDate ) {

      EventInventoryService lEventInventoryService =
            new EventInventoryService( aTaskKey.getEventKey() );
      lEventInventoryService.clearUsageSnapshot();

      List<UsageSnapshot> lUsageSnapshotList =
            UsageSnapshotService.getUsageAtDate( aMainInventory, aActualStartDate );

      // get the usage for the aircraft's assemblies
      // get the list of assemblies
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aMainInventory, new String[] { "aInvNoDbId", "aInvNoId" } );
      QuerySet lAssemblyList = QuerySetFactory.getInstance()
            .executeQuery( "com.mxi.mx.core.query.inventory.findSubAssemblies", lArgs );

      while ( lAssemblyList.next() ) {
         InventoryKey lInvKey =
               lAssemblyList.getKey( InventoryKey.class, "inv_no_db_id", "inv_no_id" );

         lUsageSnapshotList
               .addAll( UsageSnapshotService.getUsageAtDate( lInvKey, aActualStartDate ) );
      }

      // save the snapshots
      for ( UsageSnapshot lUsageSnapshot : lUsageSnapshotList ) {
         lEventInventoryService.addUsageSnapshot( lUsageSnapshot,
               new Messages( Messages.SKIP_WARNINGS ) );
      }

   }

}
