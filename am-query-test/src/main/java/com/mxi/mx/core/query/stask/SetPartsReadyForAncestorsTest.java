
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
import com.mxi.am.domain.builder.PartRequirementDomainBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.unittest.table.stask.SchedStaskUtil;


/**
 * This class performs unit testing on the query file with the same package and name.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class SetPartsReadyForAncestorsTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Verify that the task and its ancestor tasks are all marked "parts ready", until an ancestor
    * task is found that has another child task that is marked "parts not ready". The ancestor tree
    * stops being climbed once such an ancestor task is found. This is due to the behaviour stating
    * that a task may only be marked "parts ready" if all of its children are "parts ready".
    */
   @Test
   public void testAncestorNotUpdatedIfOtherChildTaskIsPartsNotReady() {
      TaskKey lGrandarentTask = new TaskBuilder().hasPartsNotReady().build();
      TaskKey lParentTask =
            new TaskBuilder().withParentTask( lGrandarentTask ).hasPartsNotReady().build();
      TaskKey lParentSiblingOneTask =
            new TaskBuilder().withParentTask( lGrandarentTask ).hasPartsReady().build();
      TaskKey lParentSiblingTwoTask =
            new TaskBuilder().withParentTask( lGrandarentTask ).hasPartsNotReady().build();
      TaskKey lTask = new TaskBuilder().withParentTask( lParentTask ).hasPartsNotReady().build();

      // Ensure that one of the parent's sibling tasks is marked "parts ready" and the other is
      // marked "parts not ready".
      new SchedStaskUtil( lGrandarentTask ).assertPartsReadyBool( false );
      new SchedStaskUtil( lParentTask ).assertPartsReadyBool( false );
      new SchedStaskUtil( lParentSiblingOneTask ).assertPartsReadyBool( true );
      new SchedStaskUtil( lParentSiblingTwoTask ).assertPartsReadyBool( false );
      new SchedStaskUtil( lTask ).assertPartsReadyBool( false );

      execute( lTask );

      // Verify that the task and its parent task is marked "parts ready". But the grandparent is
      // not updated because one of the parent task's siblings was marked "parts not ready".
      new SchedStaskUtil( lGrandarentTask ).assertPartsReadyBool( false );
      new SchedStaskUtil( lParentTask ).assertPartsReadyBool( true );
      new SchedStaskUtil( lParentSiblingOneTask ).assertPartsReadyBool( true );
      new SchedStaskUtil( lParentSiblingTwoTask ).assertPartsReadyBool( false );
      new SchedStaskUtil( lTask ).assertPartsReadyBool( true );
   }


   /**
    * Verify that an ancestor tasks is marked "parts ready" if all of that ancestor task's child
    * tasks are marked "parts ready".
    */
   @Test
   public void testAncestorUpdatedIfOtherChildTasksArePartsReady() {
      TaskKey lGrandarentTask = new TaskBuilder().hasPartsNotReady().build();
      TaskKey lParentTask =
            new TaskBuilder().withParentTask( lGrandarentTask ).hasPartsNotReady().build();
      TaskKey lParentSiblingOneTask =
            new TaskBuilder().withParentTask( lGrandarentTask ).hasPartsReady().build();
      TaskKey lParentSiblingTwoTask =
            new TaskBuilder().withParentTask( lGrandarentTask ).hasPartsReady().build();
      TaskKey lTask = new TaskBuilder().withParentTask( lParentTask ).hasPartsNotReady().build();

      // Ensure the parent sibling tasks are marked "parts ready".
      new SchedStaskUtil( lGrandarentTask ).assertPartsReadyBool( false );
      new SchedStaskUtil( lParentTask ).assertPartsReadyBool( false );
      new SchedStaskUtil( lParentSiblingOneTask ).assertPartsReadyBool( true );
      new SchedStaskUtil( lParentSiblingTwoTask ).assertPartsReadyBool( true );
      new SchedStaskUtil( lTask ).assertPartsReadyBool( false );

      execute( lTask );

      // Verify that the task and its ancestor tasks are marked "parts ready".
      // The grandparent is updated because the parent task's siblings were marked "parts ready".
      new SchedStaskUtil( lGrandarentTask ).assertPartsReadyBool( true );
      new SchedStaskUtil( lParentTask ).assertPartsReadyBool( true );
      new SchedStaskUtil( lParentSiblingOneTask ).assertPartsReadyBool( true );
      new SchedStaskUtil( lParentSiblingTwoTask ).assertPartsReadyBool( true );
      new SchedStaskUtil( lTask ).assertPartsReadyBool( true );
   }


   /**
    * Verify that the parent task is not modified if it is already marked as "parts ready".
    *
    * @throws Exception
    */
   @Test
   public void testParentNotUpdatedIfAlreadyMarkedReady() throws Exception {
      TaskKey lParentTask = new TaskBuilder().hasPartsReady().build();
      TaskKey lTask = new TaskBuilder().withParentTask( lParentTask ).hasPartsNotReady().build();

      // Retreive the revision date of the parent task row in sched_stask.
      Date lOriginalRevisionDate = getTaskRevisionDate( lParentTask );

      // Ensure the parts ready boolean is true for the child and false for the task.
      new SchedStaskUtil( lParentTask ).assertPartsReadyBool( true );
      new SchedStaskUtil( lTask ).assertPartsReadyBool( false );

      // Pause, as the revision date has a granularity of seconds.
      Thread.sleep( 1100 );

      execute( lTask );

      // Verify the parts ready boolean is true for both.
      new SchedStaskUtil( lParentTask ).assertPartsReadyBool( true );
      new SchedStaskUtil( lTask ).assertPartsReadyBool( true );

      // Verify the child task row was not updated (the revision date was not modified).
      assertEquals( lOriginalRevisionDate, getTaskRevisionDate( lParentTask ) );
   }


   /**
    * Verify that the parent tasks is not marked "parts ready" if one of the target task's sibling
    * tasks are marked "parts not ready". This is due to the behaviour stating that a task may only
    * be marked "parts ready" if all of its children are "parts ready".
    */
   @Test
   public void testParentNotUpdatedIfOneSiblingsIsPartsNotReady() {
      TaskKey lGrandparentTask = new TaskBuilder().hasPartsNotReady().build();
      TaskKey lParentTask =
            new TaskBuilder().withParentTask( lGrandparentTask ).hasPartsNotReady().build();
      TaskKey lTask = new TaskBuilder().withParentTask( lParentTask ).hasPartsNotReady().build();
      TaskKey lSiblingOneTask =
            new TaskBuilder().withParentTask( lParentTask ).hasPartsReady().build();
      TaskKey lSiblingTwoTask =
            new TaskBuilder().withParentTask( lParentTask ).hasPartsNotReady().build();

      // Ensure one of the sibling tasks is marked "parts ready" and the other "parts not ready".
      new SchedStaskUtil( lGrandparentTask ).assertPartsReadyBool( false );
      new SchedStaskUtil( lParentTask ).assertPartsReadyBool( false );
      new SchedStaskUtil( lTask ).assertPartsReadyBool( false );
      new SchedStaskUtil( lSiblingOneTask ).assertPartsReadyBool( true );
      new SchedStaskUtil( lSiblingTwoTask ).assertPartsReadyBool( false );

      execute( lTask );

      // Verify that the task is marked "parts ready". But the parent is not updated because one of
      // its other child tasks is marked "parts not ready". As well, the grandparent task is not
      // updated because the parent remains marked "parts not ready".
      new SchedStaskUtil( lGrandparentTask ).assertPartsReadyBool( false );
      new SchedStaskUtil( lParentTask ).assertPartsReadyBool( false );
      new SchedStaskUtil( lTask ).assertPartsReadyBool( true );
      new SchedStaskUtil( lSiblingOneTask ).assertPartsReadyBool( true );
      new SchedStaskUtil( lSiblingTwoTask ).assertPartsReadyBool( false );
   }


   /**
    * Verify that when the task is not historic but the parent task is, that the task is marked
    * "parts ready" but the parent task and any ancestors of the parent task are not marked "parts
    * ready".
    */
   @Test
   public void testParentNotUpdatedWhenHistoric() {
      TaskKey lGrandparentTask = new TaskBuilder().hasPartsNotReady().asHistoric().build();
      TaskKey lParentTask = new TaskBuilder().withParentTask( lGrandparentTask ).hasPartsNotReady()
            .asHistoric().build();
      TaskKey lTask = new TaskBuilder().withParentTask( lParentTask ).hasPartsNotReady().build();

      // Ensure the tasks are marked "parts not ready".
      new SchedStaskUtil( lGrandparentTask ).assertPartsReadyBool( false );
      new SchedStaskUtil( lParentTask ).assertPartsReadyBool( false );
      new SchedStaskUtil( lTask ).assertPartsReadyBool( false );

      execute( lTask );

      // Verify that only the task is marked "parts ready" but the others remain marked "parts not
      // ready".
      new SchedStaskUtil( lGrandparentTask ).assertPartsReadyBool( false );
      new SchedStaskUtil( lParentTask ).assertPartsReadyBool( false );
      new SchedStaskUtil( lTask ).assertPartsReadyBool( true );
   }


   /**
    * Verify that the parent task is marked "parts ready" if all of the target task's sibling tasks
    * are marked "parts ready".
    */
   @Test
   public void testParentUpdatedIfSiblingsArePartsReady() {
      TaskKey lParentTask = new TaskBuilder().hasPartsNotReady().build();
      TaskKey lTask = new TaskBuilder().withParentTask( lParentTask ).hasPartsNotReady().build();
      TaskKey lSiblingOneTask =
            new TaskBuilder().withParentTask( lParentTask ).hasPartsReady().build();
      TaskKey lSiblingTwoTask =
            new TaskBuilder().withParentTask( lParentTask ).hasPartsReady().build();

      // Ensure the sibling tasks are marked "parts ready".
      new SchedStaskUtil( lParentTask ).assertPartsReadyBool( false );
      new SchedStaskUtil( lTask ).assertPartsReadyBool( false );
      new SchedStaskUtil( lSiblingOneTask ).assertPartsReadyBool( true );
      new SchedStaskUtil( lSiblingTwoTask ).assertPartsReadyBool( true );

      execute( lTask );

      // Verify that the task and its parent tasks are marked "parts ready".
      // The parent is updated because the task's siblings were marked "parts ready".
      new SchedStaskUtil( lParentTask ).assertPartsReadyBool( true );
      new SchedStaskUtil( lTask ).assertPartsReadyBool( true );
      new SchedStaskUtil( lSiblingOneTask ).assertPartsReadyBool( true );
      new SchedStaskUtil( lSiblingTwoTask ).assertPartsReadyBool( true );
   }


   /**
    * Verify that the parent task is not marked "parts ready" if it has an install part requirement.
    */
   @Test
   public void testParentWithPartRequirementNotUpdated() {
      TaskKey lParentTask = new TaskBuilder().hasPartsNotReady().build();
      TaskKey lTask = new TaskBuilder().withParentTask( lParentTask ).hasPartsNotReady().build();
      withInstallPartRequirement( lParentTask );

      // Ensure the sibling tasks are marked "parts ready".
      new SchedStaskUtil( lParentTask ).assertPartsReadyBool( false );
      new SchedStaskUtil( lTask ).assertPartsReadyBool( false );
      assertInstallPartRequirement( lParentTask );

      execute( lTask );

      // Verify that the task is marked "parts ready" but the parent is not,
      // due to it having an install part requirement.
      new SchedStaskUtil( lParentTask ).assertPartsReadyBool( false );
      new SchedStaskUtil( lTask ).assertPartsReadyBool( true );
   }


   /**
    * Verify that the target task and all of its ancestor tasks are marked "parts ready".
    */
   @Test
   public void testTaskAndAncestorsUpdated() {
      TaskKey lGrandparentTask = new TaskBuilder().hasPartsNotReady().build();
      TaskKey lParentTask =
            new TaskBuilder().withParentTask( lGrandparentTask ).hasPartsNotReady().build();
      TaskKey lTask = new TaskBuilder().withParentTask( lParentTask ).hasPartsNotReady().build();

      // Ensure the tasks are marked "parts not ready".
      new SchedStaskUtil( lGrandparentTask ).assertPartsReadyBool( false );
      new SchedStaskUtil( lParentTask ).assertPartsReadyBool( false );
      new SchedStaskUtil( lTask ).assertPartsReadyBool( false );

      execute( lTask );

      // Verify that the task and its ancestor tasks are marked "parts ready".
      new SchedStaskUtil( lGrandparentTask ).assertPartsReadyBool( true );
      new SchedStaskUtil( lParentTask ).assertPartsReadyBool( true );
      new SchedStaskUtil( lTask ).assertPartsReadyBool( true );
   }


   /**
    * Verify that the task is not modified if it is already marked as "parts ready".
    *
    * @throws Exception
    */
   @Test
   public void testTaskNotUpdatedIfAlreadyMarkedReady() throws Exception {
      TaskKey lTask = new TaskBuilder().hasPartsReady().build();

      // Retreive the revision date of the row in sched_stask.
      Date lOriginalRevisionDate = getTaskRevisionDate( lTask );

      // Ensure the parts ready boolean is false.
      new SchedStaskUtil( lTask ).assertPartsReadyBool( true );

      // Pause, as the revision date has a granularity of seconds.
      Thread.sleep( 1100 );

      execute( lTask );

      // Verify the parts ready boolean remains true.
      new SchedStaskUtil( lTask ).assertPartsReadyBool( true );

      // Verify the row was not updated (the revision date was not modified).
      assertEquals( lOriginalRevisionDate, getTaskRevisionDate( lTask ) );
   }


   /**
    * Verify that the task is not set to "parts ready" when it is a historic task.
    */
   @Test
   public void testTaskNotUpdatedWhenHistoric() {
      TaskKey lGrandparentTask = new TaskBuilder().hasPartsNotReady().asHistoric().build();
      TaskKey lParentTask = new TaskBuilder().withParentTask( lGrandparentTask ).hasPartsNotReady()
            .asHistoric().build();
      TaskKey lTask =
            new TaskBuilder().withParentTask( lParentTask ).hasPartsNotReady().asHistoric().build();

      // Ensure the tasks are marked "parts not ready".
      new SchedStaskUtil( lGrandparentTask ).assertPartsReadyBool( false );
      new SchedStaskUtil( lParentTask ).assertPartsReadyBool( false );
      new SchedStaskUtil( lTask ).assertPartsReadyBool( false );

      execute( lTask );

      // Verify that the tasks remain marked "parts not ready".
      new SchedStaskUtil( lGrandparentTask ).assertPartsReadyBool( false );
      new SchedStaskUtil( lParentTask ).assertPartsReadyBool( false );
      new SchedStaskUtil( lTask ).assertPartsReadyBool( false );
   }


   /**
    * Verify that the task is marked "parts ready", even if it has an install part requirement. Only
    * parent tasks are checked for having install part requests.
    */
   @Test
   public void testTaskWithInstallPartRequirementUpdated() {
      TaskKey lTask = new TaskBuilder().hasPartsNotReady().build();
      withInstallPartRequirement( lTask );

      // Ensure the tasks are marked "parts not ready".
      new SchedStaskUtil( lTask ).assertPartsReadyBool( false );
      assertInstallPartRequirement( lTask );

      execute( lTask );

      // Verify that the task get marked "parts ready".
      new SchedStaskUtil( lTask ).assertPartsReadyBool( true );
   }


   /**
    * Ensure there is an install part requirement for the provided task.
    *
    * @param aTask
    */
   private void assertInstallPartRequirement( TaskKey aTask ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aTask, "sched_db_id", "sched_id" );

      assertFalse(
            QuerySetFactory.getInstance().executeQueryTable( "sched_inst_part", lArgs ).isEmpty() );
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
    * Add an install part requirement to the provided task.
    *
    * @param aTask
    */
   private void withInstallPartRequirement( TaskKey aTask ) {
      new PartRequirementDomainBuilder( aTask ).withInstallQuantity( 1.0 ).build();
   }
}
