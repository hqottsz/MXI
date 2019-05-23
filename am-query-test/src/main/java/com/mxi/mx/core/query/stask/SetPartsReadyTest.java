
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
public final class SetPartsReadyTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Verify that the child task is not modified if it is already marked as "parts ready".
    *
    * @throws Exception
    */
   @Test
   public void testChildNotUpdatedWhenAlreadyMarkedReady() throws Exception {
      TaskKey lTask = new TaskBuilder().hasPartsNotReady().build();
      TaskKey lChildTask = new TaskBuilder().withParentTask( lTask ).hasPartsReady().build();

      // Retreive the revision date of the child task row in sched_stask.
      Date lOriginalRevisionDate = getTaskRevisionDate( lChildTask );

      // Ensure the parts ready boolean is true for the child and false for the task.
      new SchedStaskUtil( lTask ).assertPartsReadyBool( false );
      new SchedStaskUtil( lChildTask ).assertPartsReadyBool( true );

      // Pause, as the revision date has a granularity of seconds.
      Thread.sleep( 1100 );

      execute( lTask );

      // Verify the parts ready boolean is true for both.
      new SchedStaskUtil( lTask ).assertPartsReadyBool( true );
      new SchedStaskUtil( lChildTask ).assertPartsReadyBool( true );

      // Verify the child task row was not updated (the revision date was not modified).
      assertEquals( lOriginalRevisionDate, getTaskRevisionDate( lChildTask ) );
   }


   /**
    * Verify that the target task and all of its child tasks are marked "parts ready".
    */
   @Test
   public void testTaskAndChildTasksUpdated() {
      TaskKey lTask = new TaskBuilder().hasPartsNotReady().build();
      TaskKey lChildOneTask = new TaskBuilder().withParentTask( lTask ).hasPartsNotReady().build();
      TaskKey lGrandChildTask =
            new TaskBuilder().withParentTask( lChildOneTask ).hasPartsNotReady().build();
      TaskKey lChildTwoTask = new TaskBuilder().withParentTask( lTask ).hasPartsNotReady().build();

      // Ensure the tasks are marked "parts not ready".
      new SchedStaskUtil( lTask ).assertPartsReadyBool( false );
      new SchedStaskUtil( lChildOneTask ).assertPartsReadyBool( false );
      new SchedStaskUtil( lGrandChildTask ).assertPartsReadyBool( false );
      new SchedStaskUtil( lChildTwoTask ).assertPartsReadyBool( false );

      execute( lTask );

      // Verify that the tasks remain marked "parts not ready".
      new SchedStaskUtil( lTask ).assertPartsReadyBool( true );
      new SchedStaskUtil( lChildOneTask ).assertPartsReadyBool( true );
      new SchedStaskUtil( lGrandChildTask ).assertPartsReadyBool( true );
      new SchedStaskUtil( lChildTwoTask ).assertPartsReadyBool( true );
   }


   /**
    * Verify that the target task and all of its non-hostoric child tasks are marked "parts ready".
    */
   @Test
   public void testTaskAndNonHistoricChildTasksUpdated() {
      TaskKey lTask = new TaskBuilder().hasPartsNotReady().build();
      TaskKey lChildOneTask = new TaskBuilder().withParentTask( lTask ).hasPartsNotReady().build();
      TaskKey lGrandChildOneTask = new TaskBuilder().withParentTask( lChildOneTask ).asHistoric()
            .hasPartsNotReady().build();
      TaskKey lChildTwoTask =
            new TaskBuilder().withParentTask( lTask ).asHistoric().hasPartsNotReady().build();
      TaskKey lGrandChildTwoTask = new TaskBuilder().withParentTask( lChildOneTask ).asHistoric()
            .hasPartsNotReady().build();

      // Ensure the tasks are marked "parts not ready".
      new SchedStaskUtil( lTask ).assertPartsReadyBool( false );
      new SchedStaskUtil( lChildOneTask ).assertPartsReadyBool( false );
      new SchedStaskUtil( lGrandChildOneTask ).assertPartsReadyBool( false );
      new SchedStaskUtil( lChildTwoTask ).assertPartsReadyBool( false );
      new SchedStaskUtil( lGrandChildTwoTask ).assertPartsReadyBool( false );

      execute( lTask );

      // Verify that the tasks remain marked "parts not ready".
      new SchedStaskUtil( lTask ).assertPartsReadyBool( true );
      new SchedStaskUtil( lChildOneTask ).assertPartsReadyBool( true );
      new SchedStaskUtil( lGrandChildOneTask ).assertPartsReadyBool( false );
      new SchedStaskUtil( lChildTwoTask ).assertPartsReadyBool( false );
      new SchedStaskUtil( lGrandChildTwoTask ).assertPartsReadyBool( false );
   }


   /**
    * Verify that the task is not modified if it is already marked as "parts ready".
    *
    * @throws Exception
    */
   @Test
   public void testTaskNotUpdatedWhenAlreadyMarkedReady() throws Exception {
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
      TaskKey lParentTask = new TaskBuilder().hasPartsNotReady().asHistoric().build();
      TaskKey lTask =
            new TaskBuilder().withParentTask( lParentTask ).hasPartsNotReady().asHistoric().build();
      TaskKey lChildTask =
            new TaskBuilder().withParentTask( lTask ).hasPartsNotReady().asHistoric().build();

      // Ensure the tasks are marked "parts not ready".
      new SchedStaskUtil( lParentTask ).assertPartsReadyBool( false );
      new SchedStaskUtil( lTask ).assertPartsReadyBool( false );
      new SchedStaskUtil( lChildTask ).assertPartsReadyBool( false );

      execute( lTask );

      // Verify that the tasks remain marked "parts not ready".
      new SchedStaskUtil( lParentTask ).assertPartsReadyBool( false );
      new SchedStaskUtil( lTask ).assertPartsReadyBool( false );
      new SchedStaskUtil( lChildTask ).assertPartsReadyBool( false );
   }


   /**
    * Verify that the task is marked "parts ready", even if it has an install part requirement. Only
    * parent tasks are checked for having part requests.
    */
   @Test
   public void testTaskWithPartRequirementIsMarkedPartsReady() {
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
