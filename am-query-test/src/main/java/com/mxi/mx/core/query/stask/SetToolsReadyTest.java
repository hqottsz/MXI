
package com.mxi.mx.core.query.stask;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.table.evt.EvtToolTable;
import com.mxi.mx.core.unittest.table.stask.SchedStaskUtil;


/**
 * This class performs unit testing on the query file with the same package and name.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class SetToolsReadyTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private PartGroupKey iPartGroup;


   /**
    * Verify that the child task is not modified if it is already marked as "tools ready".
    *
    * @throws Exception
    */
   @Test
   public void testChildNotUpdatedWhenAlreadyMarkedReady() throws Exception {
      TaskKey lTask = new TaskBuilder().hasToolsNotReady().build();
      TaskKey lChildTask = new TaskBuilder().withParentTask( lTask ).hasToolsReady().build();

      // Retreive the revision date of the child task row in sched_stask.
      Date lOriginalRevisionDate = getTaskRevisionDate( lChildTask );

      // Ensure the tools ready boolean is true for the child and false for the task.
      new SchedStaskUtil( lTask ).assertToolsReadyBool( false );
      new SchedStaskUtil( lChildTask ).assertToolsReadyBool( true );

      // Pause, as the revision date has a granularity of seconds.
      Thread.sleep( 1100 );

      execute( lTask );

      // Verify the tools ready boolean is true for both.
      new SchedStaskUtil( lTask ).assertToolsReadyBool( true );
      new SchedStaskUtil( lChildTask ).assertToolsReadyBool( true );

      // Verify the child task row was not updated (the revision date was not modified).
      assertEquals( lOriginalRevisionDate, getTaskRevisionDate( lChildTask ) );
   }


   /**
    * Verify that the target task and all of its child tasks are marked "tools ready".
    */
   @Test
   public void testTaskAndChildTasksUpdated() {
      TaskKey lTask = new TaskBuilder().hasToolsNotReady().build();
      TaskKey lChildOneTask = new TaskBuilder().withParentTask( lTask ).hasToolsNotReady().build();
      TaskKey lGrandChildTask =
            new TaskBuilder().withParentTask( lChildOneTask ).hasToolsNotReady().build();
      TaskKey lChildTwoTask = new TaskBuilder().withParentTask( lTask ).hasToolsNotReady().build();

      // Ensure the tasks are marked "tools not ready".
      new SchedStaskUtil( lTask ).assertToolsReadyBool( false );
      new SchedStaskUtil( lChildOneTask ).assertToolsReadyBool( false );
      new SchedStaskUtil( lGrandChildTask ).assertToolsReadyBool( false );
      new SchedStaskUtil( lChildTwoTask ).assertToolsReadyBool( false );

      execute( lTask );

      // Verify that the tasks remain marked "tools not ready".
      new SchedStaskUtil( lTask ).assertToolsReadyBool( true );
      new SchedStaskUtil( lChildOneTask ).assertToolsReadyBool( true );
      new SchedStaskUtil( lGrandChildTask ).assertToolsReadyBool( true );
      new SchedStaskUtil( lChildTwoTask ).assertToolsReadyBool( true );
   }


   /**
    * Verify that the target task and all of its non-hostoric child tasks are marked "tools ready".
    */
   @Test
   public void testTaskAndNonHistoricChildTasksUpdated() {
      TaskKey lTask = new TaskBuilder().hasToolsNotReady().build();
      TaskKey lChildOneTask = new TaskBuilder().withParentTask( lTask ).hasToolsNotReady().build();
      TaskKey lGrandChildOneTask = new TaskBuilder().withParentTask( lChildOneTask ).asHistoric()
            .hasToolsNotReady().build();
      TaskKey lChildTwoTask =
            new TaskBuilder().withParentTask( lTask ).asHistoric().hasToolsNotReady().build();
      TaskKey lGrandChildTwoTask = new TaskBuilder().withParentTask( lChildOneTask ).asHistoric()
            .hasToolsNotReady().build();

      // Ensure the tasks are marked "tools not ready".
      new SchedStaskUtil( lTask ).assertToolsReadyBool( false );
      new SchedStaskUtil( lChildOneTask ).assertToolsReadyBool( false );
      new SchedStaskUtil( lGrandChildOneTask ).assertToolsReadyBool( false );
      new SchedStaskUtil( lChildTwoTask ).assertToolsReadyBool( false );
      new SchedStaskUtil( lGrandChildTwoTask ).assertToolsReadyBool( false );

      execute( lTask );

      // Verify that the tasks remain marked "tools not ready".
      new SchedStaskUtil( lTask ).assertToolsReadyBool( true );
      new SchedStaskUtil( lChildOneTask ).assertToolsReadyBool( true );
      new SchedStaskUtil( lGrandChildOneTask ).assertToolsReadyBool( false );
      new SchedStaskUtil( lChildTwoTask ).assertToolsReadyBool( false );
      new SchedStaskUtil( lGrandChildTwoTask ).assertToolsReadyBool( false );
   }


   /**
    * Verify that the task is not modified if it is already marked as "tools ready".
    *
    * @throws Exception
    */
   @Test
   public void testTaskNotUpdatedWhenAlreadyMarkedReady() throws Exception {
      TaskKey lTask = new TaskBuilder().hasToolsReady().build();

      // Retreive the revision date of the row in sched_stask.
      Date lOriginalRevisionDate = getTaskRevisionDate( lTask );

      // Ensure the tools ready boolean is false.
      new SchedStaskUtil( lTask ).assertToolsReadyBool( true );

      // Pause, as the revision date has a granularity of seconds.
      Thread.sleep( 1100 );

      execute( lTask );

      // Verify the tools ready boolean remains true.
      new SchedStaskUtil( lTask ).assertToolsReadyBool( true );

      // Verify the row was not updated (the revision date was not modified).
      assertEquals( lOriginalRevisionDate, getTaskRevisionDate( lTask ) );
   }


   /**
    * Verify that the task is not set to "tools ready" when it is a historic task.
    */
   @Test
   public void testTaskNotUpdatedWhenHistoric() {
      TaskKey lParentTask = new TaskBuilder().hasToolsNotReady().asHistoric().build();
      TaskKey lTask =
            new TaskBuilder().withParentTask( lParentTask ).hasToolsNotReady().asHistoric().build();
      TaskKey lChildTask =
            new TaskBuilder().withParentTask( lTask ).hasToolsNotReady().asHistoric().build();

      // Ensure the tasks are marked "tools not ready".
      new SchedStaskUtil( lParentTask ).assertToolsReadyBool( false );
      new SchedStaskUtil( lTask ).assertToolsReadyBool( false );
      new SchedStaskUtil( lChildTask ).assertToolsReadyBool( false );

      execute( lTask );

      // Verify that the tasks remain marked "tools not ready".
      new SchedStaskUtil( lParentTask ).assertToolsReadyBool( false );
      new SchedStaskUtil( lTask ).assertToolsReadyBool( false );
      new SchedStaskUtil( lChildTask ).assertToolsReadyBool( false );
   }


   /**
    * Verify that the task is marked "tools ready", even if it has a tool requirement. Only parent
    * tasks are checked for having tool requests.
    */
   @Test
   public void testTaskWithToolRequirementIsMarkedToolsReady() {
      TaskKey lTask = new TaskBuilder().hasToolsNotReady().build();
      withToolRequirement( lTask );

      // Ensure the tasks are marked "tools not ready".
      new SchedStaskUtil( lTask ).assertToolsReadyBool( false );
      assertToolRequirement( lTask );

      execute( lTask );

      // Verify that the task get marked "tools ready".
      new SchedStaskUtil( lTask ).assertToolsReadyBool( true );
   }


   /**
    * Ensure there is a tool requirement for the provided task.
    *
    * @param aTask
    */
   private void assertToolRequirement( TaskKey aTask ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aTask.getEventKey(), "event_db_id", "event_id" );

      assertFalse( QuerySetFactory.getInstance().executeQueryTable( "evt_tool", lArgs ).isEmpty() );
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


   /**
    * Add a tool requirement to the provided task.
    *
    * @param aTask
    */
   private void withToolRequirement( TaskKey aTask ) {

      PartNoKey lPart = new PartNoBuilder().isAlternateIn( iPartGroup ).build();

      EvtToolTable.create( aTask.getEventKey(), iPartGroup, lPart );
   }
}
