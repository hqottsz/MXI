
package com.mxi.mx.core.query.stask;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.unittest.table.evt.EvtEventUtil;
import com.mxi.mx.core.unittest.table.stask.SchedStaskUtil;


/**
 * This class performs unit testing on the query file with the same package and name.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class SetToolsNotReadyTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Verify that the query does not modify a child task when it sets a task to "tools not ready".
    */
   @Test
   public void testChildTaskNotModified() {

      // Create a task and a child task that are both "tools ready".
      TaskKey lTask = new TaskBuilder().hasToolsReady().build();
      TaskKey lChildTask = new TaskBuilder().withParentTask( lTask ).hasToolsReady().build();

      // Ensure the tasks are both "tools ready".
      new SchedStaskUtil( lTask ).assertToolsReadyBool( true );
      new SchedStaskUtil( lChildTask ).assertToolsReadyBool( true );

      execute( lTask );

      // Verify that only the task is set to "tools not ready".
      new SchedStaskUtil( lTask ).assertToolsReadyBool( false );
      new SchedStaskUtil( lChildTask ).assertToolsReadyBool( true );
   }


   /**
    * Verify that the query sets the task and its ancestor tasks to "tools not ready" but only until
    * it finds a historic ancestor. Only the tasks under the historic ancestor task are
    * modified.<br>
    * <br>
    * Note: the query performs this way but the application does not permit a non-historic task
    * under a historic task (e.g. cannot complete a task untill all its subtasks are complete).
    */
   @Test
   public void testOnlyTasksUnderHistoricAncestorTaskAreModified() {

      // Create a great grandparent task that IS NOT historic and is "tools ready".
      TaskKey lGreatGrandparentTask = new TaskBuilder().hasToolsReady().build();

      // Create a grandparent task that IS historic and is "tools ready".
      TaskKey lGrandparentTask = new TaskBuilder().withParentTask( lGreatGrandparentTask )
            .hasToolsReady().asHistoric().build();

      // Create a parent task that IS NOT historic and is "tools ready".
      TaskKey lParentTask =
            new TaskBuilder().withParentTask( lGrandparentTask ).hasToolsReady().build();

      // Create a task that IS NOT historic and is "tools ready".
      TaskKey lTask = new TaskBuilder().withParentTask( lParentTask ).hasToolsReady().build();

      // Ensure the tasks are all "tools ready" and that only the grandparent task is historic.
      new SchedStaskUtil( lGreatGrandparentTask ).assertToolsReadyBool( true );
      new SchedStaskUtil( lGrandparentTask ).assertToolsReadyBool( true );
      new SchedStaskUtil( lParentTask ).assertToolsReadyBool( true );
      new SchedStaskUtil( lTask ).assertToolsReadyBool( true );
      new EvtEventUtil( lGreatGrandparentTask.getEventKey() ).assertHistBool( false );
      new EvtEventUtil( lGrandparentTask.getEventKey() ).assertHistBool( true );
      new EvtEventUtil( lParentTask.getEventKey() ).assertHistBool( false );
      new EvtEventUtil( lTask.getEventKey() ).assertHistBool( false );

      execute( lTask );

      // Verify that only the tasks under the historic tasks are set to "tools not ready".
      new SchedStaskUtil( lGreatGrandparentTask ).assertToolsReadyBool( true );
      new SchedStaskUtil( lGrandparentTask ).assertToolsReadyBool( true );
      new SchedStaskUtil( lParentTask ).assertToolsReadyBool( false );
      new SchedStaskUtil( lTask ).assertToolsReadyBool( false );
   }


   /**
    * Verify that the query does not set the task or its parent task to "tools not ready" if the
    * task is historic (when the parent task is not historic).
    */
   @Test
   public void testParentTaskNotModifiedWhenTaskIsHistoric() {

      // Create a parent task that IS NOT historic and is "tools ready".
      TaskKey lParentTask = new TaskBuilder().hasToolsReady().build();

      // Create a task that IS historic and is "tools ready".
      TaskKey lTask =
            new TaskBuilder().withParentTask( lParentTask ).asHistoric().hasToolsReady().build();

      // Ensure the tasks are both "tools ready" and that only the task is historic.
      new SchedStaskUtil( lParentTask ).assertToolsReadyBool( true );
      new SchedStaskUtil( lTask ).assertToolsReadyBool( true );
      new EvtEventUtil( lParentTask.getEventKey() ).assertHistBool( false );
      new EvtEventUtil( lTask.getEventKey() ).assertHistBool( true );

      execute( lTask );

      // Verify that neither task is set to "tools not ready".
      new SchedStaskUtil( lParentTask ).assertToolsReadyBool( true );
      new SchedStaskUtil( lTask ).assertToolsReadyBool( true );
   }


   /**
    * Verify that the query sets the task and its ancestor tasks to "tools not ready".
    */
   @Test
   public void testTaskAndAncestorsSetToToolsNotReady() {

      // Create a task hierarchy that are all "tools ready".
      TaskKey lGrandparentTask = new TaskBuilder().hasToolsReady().build();
      TaskKey lParentTask =
            new TaskBuilder().withParentTask( lGrandparentTask ).hasToolsReady().build();
      TaskKey lTask = new TaskBuilder().withParentTask( lParentTask ).hasToolsReady().build();

      // Ensure the tasks are all "tools ready".
      new SchedStaskUtil( lGrandparentTask ).assertToolsReadyBool( true );
      new SchedStaskUtil( lParentTask ).assertToolsReadyBool( true );
      new SchedStaskUtil( lTask ).assertToolsReadyBool( true );

      execute( lTask );

      // Verify the tools ready boolean does get set to false for all tasks.
      new SchedStaskUtil( lGrandparentTask ).assertToolsReadyBool( false );
      new SchedStaskUtil( lParentTask ).assertToolsReadyBool( false );
      new SchedStaskUtil( lTask ).assertToolsReadyBool( false );
   }


   /**
    * Verify that the parent task is not modified if it is already marked as "tools not ready".
    *
    * @throws Exception
    */
   @Test
   public void testWhenParentTaskIsCurrentlyNotReady() throws Exception {
      TaskKey lParentTask = new TaskBuilder().hasToolsNotReady().build();
      TaskKey lTask = new TaskBuilder().withParentTask( lParentTask ).hasToolsReady().build();

      // Retreive the revision date of the parent task row in sched_stask.
      Date lOriginalRevisionDate = getTaskRevisionDate( lParentTask );

      // Ensure the tools ready boolean is false for the parent and true for the task.
      new SchedStaskUtil( lParentTask ).assertToolsReadyBool( false );
      new SchedStaskUtil( lTask ).assertToolsReadyBool( true );

      // Pause, as the revision date has a granularity of seconds.
      Thread.sleep( 1100 );

      execute( lTask );

      // Verify the tools ready boolean is false for both.
      new SchedStaskUtil( lParentTask ).assertToolsReadyBool( false );
      new SchedStaskUtil( lTask ).assertToolsReadyBool( false );

      // Verify the parent task row was not updated (the revision date was not modified).
      assertEquals( lOriginalRevisionDate, getTaskRevisionDate( lParentTask ) );
   }


   /**
    * Verify that the task is not modified if it is already marked as "tools not ready".
    *
    * @throws Exception
    */
   @Test
   public void testWhenTaskIsCurrentlyNotReady() throws Exception {
      TaskKey lTask = new TaskBuilder().hasToolsNotReady().build();

      // Retreive the revision date of the row in sched_stask.
      Date lOriginalRevisionDate = getTaskRevisionDate( lTask );

      // Ensure the tools ready boolean is false.
      new SchedStaskUtil( lTask ).assertToolsReadyBool( false );

      // Pause, as the revision date has a granularity of seconds.
      Thread.sleep( 1100 );

      execute( lTask );

      // Verify the tools ready boolean remains false.
      new SchedStaskUtil( lTask ).assertToolsReadyBool( false );

      // Verify the row was not updated (the revision date was not modified).
      assertEquals( lOriginalRevisionDate, getTaskRevisionDate( lTask ) );
   }


   /**
    * Verify that if the task is historic that the query does NOT set the task to "tools not ready".
    */
   @Test
   public void testWhenTaskIsHistoric() {

      TaskKey lTask = new TaskBuilder().asHistoric().hasToolsReady().build();

      // Ensure the tools ready boolean is set to true and the task is historic.
      new SchedStaskUtil( lTask ).assertToolsReadyBool( true );
      new EvtEventUtil( lTask.getEventKey() ).assertHistBool( true );

      execute( lTask );

      // Verify the tools ready boolean does NOT get set to false.
      new SchedStaskUtil( lTask ).assertToolsReadyBool( true );
   }


   /**
    * Verify that if the task is not historic that the query does set the task to "tools not ready".
    */
   @Test
   public void testWhenTaskIsNotHistoric() {

      TaskKey lTask = new TaskBuilder().hasToolsReady().build();

      // Ensure the tools ready boolean is set to true and the task is not historic.
      new SchedStaskUtil( lTask ).assertToolsReadyBool( true );
      new EvtEventUtil( lTask.getEventKey() ).assertHistBool( false );

      execute( lTask );

      // Verify the tools ready boolean does get set to false.
      new SchedStaskUtil( lTask ).assertToolsReadyBool( false );
   }


   /**
    * Execute the update.
    *
    * @param aTask
    *           task key
    *
    * @return number of rows updated
    */
   private int execute( TaskKey aTask ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aTask, "aTaskDbId", "aTaskId" );

      // Execute the query
      return QueryExecutor.executeUpdate( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Gets the value of the revision_dt column in the sched_stask table for the provided task.
    *
    * @param aTask
    *           task key
    *
    * @return revision date in sched_stask
    */
   private Date getTaskRevisionDate( TaskKey aTask ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aTask, "sched_db_id", "sched_id" );

      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "sched_stask", lArgs );
      assertTrue( lQs.next() );

      return lQs.getDate( "revision_dt" );
   }
}
