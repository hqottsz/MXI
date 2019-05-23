package com.mxi.mx.core.services.stask;

import static com.mxi.am.domain.Domain.createFault;
import static com.mxi.am.domain.Domain.createRequirement;
import static com.mxi.mx.core.key.RefRelationTypeKey.FAULTREL;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MxRuntimeException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.table.evt.EvtEventRel;


/**
 * Integration tests for {@linkplain TaskService}
 *
 */
public class TaskServiceTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    *
    * Verify a fault-relationship is created between a fault and a following task of the provided
    * subtask of the fault.
    *
    * More specifically, the following task has a dependency relationship with a subtask of the
    * corrective task of the fault.
    *
    * Exercising {@linkplain TaskService#applyFaultRelationshipToDependencies(TaskKey)}
    *
    * <pre>
    *    fault -> subtask -> *following task*
    * </pre>
    *
    * <pre>
    *    Given a fault
    *      And a subtask of the fault
    *          (technically a subtask of the fault's corrective task)
    *      And a following task of the subtask
    *     When the fault relationships are applied to the dependencies of the subtask
    *     Then a fault-related relationship is created between the fault and the following task
    * </pre>
    *
    */
   @Test
   public void itAddsFaultRelationshipToFollowingTaskOfFaultSubtask() {

      // Given a fault.
      FaultKey lFault = createFault();

      // Given a subtask of the fault (technically subtask of the fault's corrective task).
      TaskKey lSubtask = createRequirement( aSubtask -> aSubtask.setAssociatedFault( lFault ) );

      // Given a following task of the subtask.
      TaskKey lFollowingTask =
            createRequirement( aFollowingTask -> aFollowingTask.setPreviousTask( lSubtask ) );

      // When the fault relationships are applied to the dependencies of the subtask.
      new TaskService().applyFaultRelationshipToDependencies( lSubtask );

      // Then a fault-related relationship is created between the fault and the following task.
      List<TaskKey> lRelTasks = getFaultRelationships( lFault );

      assertThat( "Unexpected number of relationships returned.", lRelTasks.size(), is( 1 ) );
      assertThat( "Fault-related relationship not created.", lRelTasks.get( 0 ),
            is( lFollowingTask ) );
   }


   /**
    *
    * Verify a fault-relationship is created between a fault and many following tasks of the
    * provided subtask of the fault.
    *
    * More specifically, the following tasks each have a dependency relationship with a subtask of
    * the corrective task of the fault.
    *
    * Exercising {@linkplain TaskService#applyFaultRelationshipToDependencies(TaskKey)}
    *
    * <pre>
    *    fault -> subtask -> *following task1*
    *                     -> *following task2*
    * </pre>
    *
    * <pre>
    *    Given a fault
    *      And a subtask of the fault
    *          (technically a subtask of the fault's corrective task)
    *      And many following tasks of the subtask
    *     When the fault relationships are applied to the dependencies of the subtask
    *     Then fault-related relationship are created between the fault and the many following task
    * </pre>
    *
    */
   @Test
   public void itAddsFaultRelationshipToManyFollowingTasksOfFaultSubtask() {

      // Given a fault.
      FaultKey lFault = createFault();

      // Given a subtask of the fault (technically subtask of the fault's corrective task).
      TaskKey lSubtask = createRequirement( aSubtask -> aSubtask.setAssociatedFault( lFault ) );

      // Given many following tasks of the subtask.
      TaskKey lFollowingTask1 =
            createRequirement( aFollowingTask1 -> aFollowingTask1.setPreviousTask( lSubtask ) );
      TaskKey lFollowingTask2 =
            createRequirement( aFollowingTask2 -> aFollowingTask2.setPreviousTask( lSubtask ) );

      // When the fault relationships are applied to the dependencies of the subtask.
      new TaskService().applyFaultRelationshipToDependencies( lSubtask );

      // Then fault-related relationship are created between the fault and the many following task.
      List<TaskKey> lRelTasks = getFaultRelationships( lFault );

      assertThat( "Unexpected number of relationships returned.", lRelTasks.size(), is( 2 ) );
      assertThat( "Fault-related relationship not created for first following task.", lRelTasks,
            hasItem( lFollowingTask1 ) );
      assertThat( "Fault-related relationship not created for second following task.", lRelTasks,
            hasItem( lFollowingTask2 ) );
   }


   /**
    *
    * Verify a fault-relationship is created between a fault and all the recurring, following tasks
    * of the provided subtask of the fault.
    *
    * More specifically, the active following task has a dependency relationship with a subtask of
    * the corrective task of the fault (the recurring tasks have the usual dependency
    * relationships).
    *
    * Exercising {@linkplain TaskService#applyFaultRelationshipToDependencies(TaskKey)}
    *
    * <pre>
    *    fault -> subtask -> *active following task* -> *forecast following task* ...
    * </pre>
    *
    * <pre>
    *    Given a fault
    *      And a subtask of the fault
    *          (technically a subtask of the fault's corrective task)
    *      And recurring following tasks of the subtask
    *     When the fault relationships are applied to the dependencies of the subtask
    *     Then fault-related relationships are created between the fault and all the following tasks
    * </pre>
    *
    */
   @Test
   public void itAddsFaultRelationshipsToRecurringFollowingTaskOfFaultSubtask() {

      // Given a fault.
      FaultKey lFault = createFault();

      // Given a subtask of the fault (technically subtask of the fault's corrective task).
      TaskKey lSubtask = createRequirement( aSubtask -> aSubtask.setAssociatedFault( lFault ) );

      // Given recurring following tasks of the subtask.
      TaskKey lActvTask = createRequirement(
            aActvFollowingTask -> aActvFollowingTask.setPreviousTask( lSubtask ) );
      TaskKey lForecastTask1 =
            createRequirement( aForecastTask1 -> aForecastTask1.setPreviousTask( lActvTask ) );
      TaskKey lForecastTask2 =
            createRequirement( aForecastTask2 -> aForecastTask2.setPreviousTask( lForecastTask1 ) );

      // When the fault relationships are applied to the dependencies of the subtask.
      new TaskService().applyFaultRelationshipToDependencies( lSubtask );

      // Then fault-related relationships are created between the fault and all the following tasks.
      List<TaskKey> lRelTaskEvents = getFaultRelationships( lFault );

      assertThat( "Unexpected number of relationships returned.", lRelTaskEvents.size(), is( 3 ) );
      assertThat( "Fault-related relationship not created for ACTV following task.", lRelTaskEvents,
            hasItem( lActvTask ) );
      assertThat( "Fault-related relationship not created for first FORECAST following task.",
            lRelTaskEvents, hasItem( lForecastTask1 ) );
      assertThat( "Fault-related relationship not created for second FORECAST following task.",
            lRelTaskEvents, hasItem( lForecastTask2 ) );
   }


   /**
    *
    * Verify a fault-relationship is created between a fault and a following task of the provided
    * following task of the fault.
    *
    * More specifically, the following task has a dependency relationship with a subtask of the
    * corrective task of the fault.
    *
    * Exercising {@linkplain TaskService#applyFaultRelationshipToDependencies(TaskKey)}
    *
    * <pre>
    *    fault -> following task -> *following task of following task*
    * </pre>
    *
    * <pre>
    *    Given a fault
    *      And a following task of the fault
    *          (technically a following task of a subtask of the fault's corrective task)
    *      And a following task of the following task
    *     When the fault relationships are applied to the dependencies of the following task
    *     Then a fault-related relationship is created between the fault and
    *          the following task of the following task (do you follow?)
    * </pre>
    *
    */
   @Test
   public void itAddsFaultRelationshipToFollowingTaskOfFollowingTaskOfFault() {

      // Given a fault.
      FaultKey lFault = createFault();

      // Given a following task of the fault.
      TaskKey lFollowingTaskOfFault =
            createRequirement( aFollowingTask -> aFollowingTask.setRelatedFault( lFault ) );

      // Given a following task of the following task.
      TaskKey lFollowingTaskOfFollowingTask = createRequirement(
            aFollowingTask -> aFollowingTask.setPreviousTask( lFollowingTaskOfFault ) );

      // When the fault relationships are applied to the dependencies of the following task of the
      // fault.
      new TaskService().applyFaultRelationshipToDependencies( lFollowingTaskOfFault );

      // Then a fault-related relationship is created between the fault and the following task of
      // the following task.
      List<TaskKey> lRelTasks = getFaultRelationships( lFault );

      // We must also count the existing following task's relationship with the fault.
      assertThat( "Unexpected number of relationships returned.", lRelTasks.size(), is( 2 ) );
      assertThat( "Fault-related relationship not created.", lRelTasks,
            hasItem( lFollowingTaskOfFollowingTask ) );
   }


   /**
    *
    * Verify a fault-relationship is created between a fault and a following task of the provided
    * following task of the fault.
    *
    * More specifically, the following task has a dependency relationship with a subtask of the
    * corrective task of the fault.
    *
    * Exercising {@linkplain TaskService#applyFaultRelationshipToDependencies(TaskKey)}
    *
    * <pre>
    *    fault -> following task -> *active following task* -> *forecast following task* ...
    * </pre>
    *
    * <pre>
    *    Given a fault
    *      And a following task of the fault
    *          (technically a following task of a subtask of the fault's corrective task)
    *      And a recurring following tasks of the following task
    *     When the fault relationships are applied to the dependencies of the following task
    *     Then fault-related relationships are created between the fault and
    *          the recurring following tasks of the following task
    * </pre>
    *
    */
   @Test
   public void itAddsFaultRelationshipsToRecurringFollowingTasksOfFollowingTaskOfFault() {

      // Given a fault.
      FaultKey lFault = createFault();

      // Given a following task of the fault.
      TaskKey lFollowingTaskOfFault =
            createRequirement( aFollowingTask -> aFollowingTask.setRelatedFault( lFault ) );

      // Given a recurring following tasks of the following task.
      TaskKey lActvTask =
            createRequirement( aActvTask -> aActvTask.setPreviousTask( lFollowingTaskOfFault ) );
      TaskKey lForecastTask1 =
            createRequirement( aForecastTask1 -> aForecastTask1.setPreviousTask( lActvTask ) );
      TaskKey lForecastTask2 =
            createRequirement( aForecastTask2 -> aForecastTask2.setPreviousTask( lForecastTask1 ) );

      // When the fault relationships are applied to the dependencies of the following task of the
      // fault.
      new TaskService().applyFaultRelationshipToDependencies( lFollowingTaskOfFault );

      // Then fault-related relationships are created between the fault and the recurring following
      // tasks of the following task.
      List<TaskKey> lRelTasks = getFaultRelationships( lFault );

      // We must also count the existing following task's relationship with the fault.
      assertThat( "Unexpected number of relationships returned.", lRelTasks.size(), is( 4 ) );
      assertThat( "Fault-related relationship not created for active following task.", lRelTasks,
            hasItem( lActvTask ) );
      assertThat( "Fault-related relationship not created for first forecast task.", lRelTasks,
            hasItem( lForecastTask1 ) );
      assertThat( "Fault-related relationship not created for second forecast task.", lRelTasks,
            hasItem( lForecastTask2 ) );
   }


   /**
    *
    * Verify a fault-relationship is NOT created between a fault and a following task of the
    * provided following task of the fault, when that following task of the fault is actually
    * following many faults (not valid).
    *
    * Also verify that an exception is thrown as a following task can never follow more than one
    * fault.
    *
    * More specifically, the following task has dependency relationships with the subtasks of
    * corrective tasks of many faults.
    *
    * Exercising {@linkplain TaskService#applyFaultRelationshipToDependencies(TaskKey)}
    *
    * <pre>
    *    fault
    *          \
    *           -> !following task! -> following task of following task
    *    fault /
    *
    *    (! - not a valid scenario)
    * </pre>
    *
    * <pre>
    *    Given many faults
    *      And one following task for all the faults
    *          (technically a following task of the subtasks of the faults' corrective tasks)
    *      And a following task of the following task
    *     When the fault relationships are applied to the dependencies of the following task
    *     Then a runtime exception is thrown.
    * </pre>
    *
    */
   @Test
   public void itDoesNotAddFaultRelationshipToFollowingTaskOfFollowingTaskOfManyFaults() {

      // Given many faults.
      FaultKey lFault1 = createFault();
      FaultKey lFault2 = createFault();

      // Given one following task for all the faults.
      // (not valid so create the other following relationship manually)
      TaskKey lFollowingTaskOfFaults =
            createRequirement( aFollowingTask -> aFollowingTask.setRelatedFault( lFault1 ) );
      EvtEventRel.create( lFollowingTaskOfFaults.getEventKey(), lFault2.getEventKey(), FAULTREL );

      // Given a following task of the following task.
      createRequirement(
            aFollowingTask -> aFollowingTask.setPreviousTask( lFollowingTaskOfFaults ) );

      // When the fault relationships are applied to the dependencies of the following task.
      try {
         new TaskService().applyFaultRelationshipToDependencies( lFollowingTaskOfFaults );
      } catch ( MxRuntimeException ex ) {

         // Then a runtime exception is thrown.
         assertThat( "Unexpected message in thrown MxRuntimeException.", ex.getMessage(),
               containsString( "More than one FAULTREL relationship found in task" ) );
         return;
      }
      Assert.fail( "Expected MxRuntimeException was not thrown." );
   }


   /**
    *
    * Verify a fault-relationship is NOT created between a fault and a following task of the
    * provided task when that task does not have a fault-relationship with the fault.
    *
    * More specifically, the following task has a dependency relationship with a subtask of the
    * corrective task of the fault.
    *
    * Exercising {@linkplain TaskService#applyFaultRelationshipToDependencies(TaskKey)}
    *
    * <pre>
    *    fault
    *    task -> following task of following task
    * </pre>
    *
    * <pre>
    *    Given a fault
    *      And a task with no fault-relationship to any faults
    *      And a following task of the task
    *     When the fault relationships are applied to the dependencies of the task
    *     Then a fault-related relationship is NOT created between the fault and
    *          the following task of the task
    * </pre>
    *
    */
   @Test
   public void itDoesNotAddFaultRelationshipToFollowingTaskOfTaskWithNoRelationshipToFault() {

      // Given a fault.
      FaultKey lFault = createFault();

      // Given a task with no fault-relationship to any faults.
      TaskKey lTask = createRequirement();

      // Given a following task of the task.
      createRequirement( aFollowingTask -> aFollowingTask.setPreviousTask( lTask ) );

      // When the fault relationships are applied to the dependencies of the task.
      new TaskService().applyFaultRelationshipToDependencies( lTask );

      // Then a fault-related relationship is NOT created between the fault and the following task
      // of the task.
      List<TaskKey> lRelTasks = getFaultRelationships( lFault );
      assertThat( "Unexpected number of relationships returned.", lRelTasks.size(), is( 0 ) );
   }


   private List<TaskKey> getFaultRelationships( FaultKey aFault ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aFault, "rel_event_db_id", "rel_event_id" );
      lArgs.add( FAULTREL, "rel_type_db_id", "rel_type_cd" );
      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "evt_event_rel", lArgs );

      List<TaskKey> lFaultRelationshipEvents = new ArrayList<>();
      while ( lQs.next() ) {
         lFaultRelationshipEvents.add( lQs.getKey( TaskKey.class, "event_db_id", "event_id" ) );
      }

      return lFaultRelationshipEvents;

   }

}
