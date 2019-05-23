
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
public final class SetToolsReadyForAncestorsTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private PartGroupKey iPartGroup;


   /**
    * Verify that the task and its ancestor tasks are all marked "tools ready", until an ancestor
    * task is found that has another child task that is marked "tools not ready". The ancestor tree
    * stops being climbed once such an ancestor task is found. This is due to the behaviour stating
    * that a task may only be marked "tools ready" if all of its children are "tools ready".
    */
   @Test
   public void testAncestorNotUpdatedIfOtherChildTaskIsToolsNotReady() {
      TaskKey lGrandarentTask = new TaskBuilder().hasToolsNotReady().build();
      TaskKey lParentTask =
            new TaskBuilder().withParentTask( lGrandarentTask ).hasToolsNotReady().build();
      TaskKey lParentSiblingOneTask =
            new TaskBuilder().withParentTask( lGrandarentTask ).hasToolsReady().build();
      TaskKey lParentSiblingTwoTask =
            new TaskBuilder().withParentTask( lGrandarentTask ).hasToolsNotReady().build();
      TaskKey lTask = new TaskBuilder().withParentTask( lParentTask ).hasToolsNotReady().build();

      // Ensure that one of the parent's sibling tasks is marked "tools ready" and the other is
      // marked "tools not ready".
      new SchedStaskUtil( lGrandarentTask ).assertToolsReadyBool( false );
      new SchedStaskUtil( lParentTask ).assertToolsReadyBool( false );
      new SchedStaskUtil( lParentSiblingOneTask ).assertToolsReadyBool( true );
      new SchedStaskUtil( lParentSiblingTwoTask ).assertToolsReadyBool( false );
      new SchedStaskUtil( lTask ).assertToolsReadyBool( false );

      execute( lTask );

      // Verify that the task and its parent task is marked "tools ready". But the grandparent is
      // not updated because one of the parent task's siblings was marked "tools not ready".
      new SchedStaskUtil( lGrandarentTask ).assertToolsReadyBool( false );
      new SchedStaskUtil( lParentTask ).assertToolsReadyBool( true );
      new SchedStaskUtil( lParentSiblingOneTask ).assertToolsReadyBool( true );
      new SchedStaskUtil( lParentSiblingTwoTask ).assertToolsReadyBool( false );
      new SchedStaskUtil( lTask ).assertToolsReadyBool( true );
   }


   /**
    * Verify that an ancestor tasks is marked "tools ready" if all of that ancestor task's child
    * tasks are marked "tools ready".
    */
   @Test
   public void testAncestorUpdatedIfOtherChildTasksAreToolsReady() {
      TaskKey lGrandarentTask = new TaskBuilder().hasToolsNotReady().build();
      TaskKey lParentTask =
            new TaskBuilder().withParentTask( lGrandarentTask ).hasToolsNotReady().build();
      TaskKey lParentSiblingOneTask =
            new TaskBuilder().withParentTask( lGrandarentTask ).hasToolsReady().build();
      TaskKey lParentSiblingTwoTask =
            new TaskBuilder().withParentTask( lGrandarentTask ).hasToolsReady().build();
      TaskKey lTask = new TaskBuilder().withParentTask( lParentTask ).hasToolsNotReady().build();

      // Ensure the parent sibling tasks are marked "tools ready".
      new SchedStaskUtil( lGrandarentTask ).assertToolsReadyBool( false );
      new SchedStaskUtil( lParentTask ).assertToolsReadyBool( false );
      new SchedStaskUtil( lParentSiblingOneTask ).assertToolsReadyBool( true );
      new SchedStaskUtil( lParentSiblingTwoTask ).assertToolsReadyBool( true );
      new SchedStaskUtil( lTask ).assertToolsReadyBool( false );

      execute( lTask );

      // Verify that the task and its ancestor tasks are marked "tools ready".
      // The grandparent is updated because the parent task's siblings were marked "tools ready".
      new SchedStaskUtil( lGrandarentTask ).assertToolsReadyBool( true );
      new SchedStaskUtil( lParentTask ).assertToolsReadyBool( true );
      new SchedStaskUtil( lParentSiblingOneTask ).assertToolsReadyBool( true );
      new SchedStaskUtil( lParentSiblingTwoTask ).assertToolsReadyBool( true );
      new SchedStaskUtil( lTask ).assertToolsReadyBool( true );
   }


   /**
    * Verify that the parent task is not modified if it is already marked as "tools ready".
    *
    * @throws Exception
    */
   @Test
   public void testParentNotUpdatedIfAlreadyMarkedReady() throws Exception {
      TaskKey lParentTask = new TaskBuilder().hasToolsReady().build();
      TaskKey lTask = new TaskBuilder().withParentTask( lParentTask ).hasToolsNotReady().build();

      // Retreive the revision date of the parent task row in sched_stask.
      Date lOriginalRevisionDate = getTaskRevisionDate( lParentTask );

      // Ensure the tools ready boolean is true for the child and false for the task.
      new SchedStaskUtil( lParentTask ).assertToolsReadyBool( true );
      new SchedStaskUtil( lTask ).assertToolsReadyBool( false );

      // Pause, as the revision date has a granularity of seconds.
      Thread.sleep( 1100 );

      execute( lTask );

      // Verify the tools ready boolean is true for both.
      new SchedStaskUtil( lParentTask ).assertToolsReadyBool( true );
      new SchedStaskUtil( lTask ).assertToolsReadyBool( true );

      // Verify the child task row was not updated (the revision date was not modified).
      assertEquals( lOriginalRevisionDate, getTaskRevisionDate( lParentTask ) );
   }


   /**
    * Verify that the parent tasks is not marked "tools ready" if one of the target task's sibling
    * tasks are marked "tools not ready". This is due to the behaviour stating that a task may only
    * be marked "tools ready" if all of its children are "tools ready".
    */
   @Test
   public void testParentNotUpdatedIfOneSiblingsIsToolsNotReady() {
      TaskKey lGrandparentTask = new TaskBuilder().hasToolsNotReady().build();
      TaskKey lParentTask =
            new TaskBuilder().withParentTask( lGrandparentTask ).hasToolsNotReady().build();
      TaskKey lTask = new TaskBuilder().withParentTask( lParentTask ).hasToolsNotReady().build();
      TaskKey lSiblingOneTask =
            new TaskBuilder().withParentTask( lParentTask ).hasToolsReady().build();
      TaskKey lSiblingTwoTask =
            new TaskBuilder().withParentTask( lParentTask ).hasToolsNotReady().build();

      // Ensure one of the sibling tasks is marked "tools ready" and the other "tools not ready".
      new SchedStaskUtil( lGrandparentTask ).assertToolsReadyBool( false );
      new SchedStaskUtil( lParentTask ).assertToolsReadyBool( false );
      new SchedStaskUtil( lTask ).assertToolsReadyBool( false );
      new SchedStaskUtil( lSiblingOneTask ).assertToolsReadyBool( true );
      new SchedStaskUtil( lSiblingTwoTask ).assertToolsReadyBool( false );

      execute( lTask );

      // Verify that the task is marked "tools ready". But the parent is not updated because one of
      // its other child tasks is marked "tools not ready". As well, the grandparent task is not
      // updated because the parent remains marked "tools not ready".
      new SchedStaskUtil( lGrandparentTask ).assertToolsReadyBool( false );
      new SchedStaskUtil( lParentTask ).assertToolsReadyBool( false );
      new SchedStaskUtil( lTask ).assertToolsReadyBool( true );
      new SchedStaskUtil( lSiblingOneTask ).assertToolsReadyBool( true );
      new SchedStaskUtil( lSiblingTwoTask ).assertToolsReadyBool( false );
   }


   /**
    * Verify that when the task is not historic but the parent task is, that the task is marked
    * "tools ready" but the parent task and any ancestors of the parent task are not marked "tools
    * ready".
    */
   @Test
   public void testParentNotUpdatedWhenHistoric() {
      TaskKey lGrandparentTask = new TaskBuilder().hasToolsNotReady().asHistoric().build();
      TaskKey lParentTask = new TaskBuilder().withParentTask( lGrandparentTask ).hasToolsNotReady()
            .asHistoric().build();
      TaskKey lTask = new TaskBuilder().withParentTask( lParentTask ).hasToolsNotReady().build();

      // Ensure the tasks are marked "tools not ready".
      new SchedStaskUtil( lGrandparentTask ).assertToolsReadyBool( false );
      new SchedStaskUtil( lParentTask ).assertToolsReadyBool( false );
      new SchedStaskUtil( lTask ).assertToolsReadyBool( false );

      execute( lTask );

      // Verify that only the task is marked "tools ready" but the others remain marked "tools not
      // ready".
      new SchedStaskUtil( lGrandparentTask ).assertToolsReadyBool( false );
      new SchedStaskUtil( lParentTask ).assertToolsReadyBool( false );
      new SchedStaskUtil( lTask ).assertToolsReadyBool( true );
   }


   /**
    * Verify that the parent task is marked "tools ready" if all of the target task's sibling tasks
    * are marked "tools ready".
    */
   @Test
   public void testParentUpdatedIfSiblingsAreToolsReady() {
      TaskKey lParentTask = new TaskBuilder().hasToolsNotReady().build();
      TaskKey lTask = new TaskBuilder().withParentTask( lParentTask ).hasToolsNotReady().build();
      TaskKey lSiblingOneTask =
            new TaskBuilder().withParentTask( lParentTask ).hasToolsReady().build();
      TaskKey lSiblingTwoTask =
            new TaskBuilder().withParentTask( lParentTask ).hasToolsReady().build();

      // Ensure the sibling tasks are marked "tools ready".
      new SchedStaskUtil( lParentTask ).assertToolsReadyBool( false );
      new SchedStaskUtil( lTask ).assertToolsReadyBool( false );
      new SchedStaskUtil( lSiblingOneTask ).assertToolsReadyBool( true );
      new SchedStaskUtil( lSiblingTwoTask ).assertToolsReadyBool( true );

      execute( lTask );

      // Verify that the task and its parent tasks are marked "tools ready".
      // The parent is updated because the task's siblings were marked "tools ready".
      new SchedStaskUtil( lParentTask ).assertToolsReadyBool( true );
      new SchedStaskUtil( lTask ).assertToolsReadyBool( true );
      new SchedStaskUtil( lSiblingOneTask ).assertToolsReadyBool( true );
      new SchedStaskUtil( lSiblingTwoTask ).assertToolsReadyBool( true );
   }


   /**
    * Verify that the parent task is not marked "tools ready" if it has a tool requirement.
    */
   @Test
   public void testParentWithToolRequirementNotUpdated() {
      TaskKey lParentTask = new TaskBuilder().hasToolsNotReady().build();
      TaskKey lTask = new TaskBuilder().withParentTask( lParentTask ).hasToolsNotReady().build();
      withToolRequirement( lParentTask );

      // Ensure the sibling tasks are marked "tools ready".
      new SchedStaskUtil( lParentTask ).assertToolsReadyBool( false );
      new SchedStaskUtil( lTask ).assertToolsReadyBool( false );
      assertToolRequirement( lParentTask );

      execute( lTask );

      // Verify that the task is marked "tools ready" but the parent is not,
      // due to it having a tool requirement.
      new SchedStaskUtil( lParentTask ).assertToolsReadyBool( false );
      new SchedStaskUtil( lTask ).assertToolsReadyBool( true );
   }


   /**
    * Verify that the target task and all of its ancestor tasks are marked "tools ready".
    */
   @Test
   public void testTaskAndAncestorsUpdated() {
      TaskKey lGrandparentTask = new TaskBuilder().hasToolsNotReady().build();
      TaskKey lParentTask =
            new TaskBuilder().withParentTask( lGrandparentTask ).hasToolsNotReady().build();
      TaskKey lTask = new TaskBuilder().withParentTask( lParentTask ).hasToolsNotReady().build();

      // Ensure the tasks are marked "tools not ready".
      new SchedStaskUtil( lGrandparentTask ).assertToolsReadyBool( false );
      new SchedStaskUtil( lParentTask ).assertToolsReadyBool( false );
      new SchedStaskUtil( lTask ).assertToolsReadyBool( false );

      execute( lTask );

      // Verify that the task and its ancestor tasks are marked "tools ready".
      new SchedStaskUtil( lGrandparentTask ).assertToolsReadyBool( true );
      new SchedStaskUtil( lParentTask ).assertToolsReadyBool( true );
      new SchedStaskUtil( lTask ).assertToolsReadyBool( true );
   }


   /**
    * Verify that the task is not modified if it is already marked as "tools ready".
    *
    * @throws Exception
    */
   @Test
   public void testTaskNotUpdatedIfAlreadyMarkedReady() throws Exception {
      TaskKey lTask = new TaskBuilder().hasToolsReady().build();

      // Retreive the revision date of the row in sched_stask.
      Date lOriginalRevisionDate = getTaskRevisionDate( lTask );

      // Ensure the parts ready boolean is false.
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
   public void testTaskWithToolRequirementUpdated() {
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
