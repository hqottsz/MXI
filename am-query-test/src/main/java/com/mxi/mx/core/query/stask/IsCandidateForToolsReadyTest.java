
package com.mxi.mx.core.query.stask;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.builder.PartGroupDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.table.evt.EvtToolTable;
import com.mxi.mx.core.table.sched.SchedStaskTable;
import com.mxi.mx.core.unittest.table.stask.SchedStaskUtil;


/**
 * This class performs unit testing on the query file with the same package and name.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class IsCandidateForToolsReadyTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private PartGroupKey iPartGroup;


   /**
    * Verify that if the task does not exist that the result is false.
    */
   @Test
   public void testTaskDoesNotExist() {
      TaskKey lTask = new TaskKey( 1, 1 );

      // Ensure the task does not exist.
      assertFalse( SchedStaskTable.create( lTask ).exists() );

      boolean lResult = execute( lTask );

      // Verify that the query returns false.
      assertFalse( lResult );
   }


   /**
    * Verify that if the task has more than one tool requirement that the result is false.
    */
   @Test
   public void testTaskHasMultipleToolRequirements() {
      TaskKey lTask = new TaskBuilder().hasToolsNotReady().build();
      withToolRequirement( lTask );
      withToolRequirement( lTask );

      // Ensure the task has an tool requirement.
      SchedStaskTable lSchedStask = SchedStaskTable.create( lTask );
      assertFalse( lSchedStask.isHistoric() );
      ( new SchedStaskUtil( lSchedStask ) ).assertToolsReadyBool( false );
      assertToolRequirementExists( lTask );

      boolean lResult = execute( lTask );

      // Verify that the query returns false.
      assertFalse( lResult );
   }


   /**
    * Verify that if the task has a tool requirement that the result is false.
    */
   @Test
   public void testTaskHasToolRequirement() {
      TaskKey lTask = new TaskBuilder().hasToolsNotReady().build();
      withToolRequirement( lTask );

      // Ensure the task has an tool requirement.
      SchedStaskTable lSchedStask = SchedStaskTable.create( lTask );
      assertFalse( lSchedStask.isHistoric() );
      ( new SchedStaskUtil( lSchedStask ) ).assertToolsReadyBool( false );
      assertToolRequirementExists( lTask );

      boolean lResult = execute( lTask );

      // Verify that the query returns false.
      assertFalse( lResult );
   }


   /**
    * Verify that if the task meets all the requirements to be a candidate that the result is true.
    */
   @Test
   public void testTaskIsACandidate() {
      TaskKey lTask = new TaskBuilder().hasToolsNotReady().build();
      TaskKey lChildTask1 = new TaskBuilder().withParentTask( lTask ).hasToolsReady().build();
      TaskKey lChildTask2 = new TaskBuilder().withParentTask( lTask ).hasToolsReady().build();

      // Ensure the task has all the requirements to be a candidate.
      SchedStaskTable lSchedStask = SchedStaskTable.create( lTask );
      assertFalse( lSchedStask.isHistoric() );
      ( new SchedStaskUtil( lSchedStask ) ).assertToolsReadyBool( false );
      assertToolRequirementDoesNotExist( lTask );
      ( new SchedStaskUtil( SchedStaskTable.create( lChildTask1 ) ) ).assertToolsReadyBool( true );
      ( new SchedStaskUtil( SchedStaskTable.create( lChildTask2 ) ) ).assertToolsReadyBool( true );

      boolean lResult = execute( lTask );

      // Verify that the query returns true, we have a candidate - yeah.
      assertTrue( lResult );
   }


   /**
    * Verify that if the task is historic that the result is false.
    */
   @Test
   public void testTaskIsHistoric() {
      TaskKey lTask = new TaskBuilder().asHistoric().hasToolsNotReady().build();

      // Ensure the task is marked "tools not ready" and is historic.
      SchedStaskTable lSchedStask = SchedStaskTable.create( lTask );
      assertTrue( lSchedStask.isHistoric() );
      ( new SchedStaskUtil( lSchedStask ) ).assertToolsReadyBool( false );

      boolean lResult = execute( lTask );

      // Verify that the query returns false.
      assertFalse( lResult );
   }


   /**
    * Verify that if the task has a child task that is marked "tools not ready" that the result is
    * false.
    */
   @Test
   public void testTaskWithChildTaskMarkToolsNotReady() {
      TaskKey lTask = new TaskBuilder().hasToolsNotReady().build();
      TaskKey lChildTask = new TaskBuilder().withParentTask( lTask ).hasToolsNotReady().build();

      // Ensure the task has a child task marked "tools not ready" but has all the other
      // requirements to be a candidate.
      SchedStaskTable lSchedStask = SchedStaskTable.create( lTask );
      assertFalse( lSchedStask.isHistoric() );
      ( new SchedStaskUtil( lSchedStask ) ).assertToolsReadyBool( false );
      assertToolRequirementDoesNotExist( lTask );
      ( new SchedStaskUtil( SchedStaskTable.create( lChildTask ) ) ).assertToolsReadyBool( false );

      boolean lResult = execute( lTask );

      // Verify that the query returns false.
      assertFalse( lResult );
   }


   /**
    * Verify that if the task has no child tasks, as well as, the other requirements, that the
    * result is true.
    */
   @Test
   public void testTaskWithNoChildTasks() {
      TaskKey lTask = new TaskBuilder().hasToolsNotReady().build();

      // Ensure the task has no child tasks but has all the other requirements to be a candidate.
      SchedStaskTable lSchedStask = SchedStaskTable.create( lTask );
      assertFalse( lSchedStask.isHistoric() );
      ( new SchedStaskUtil( lSchedStask ) ).assertToolsReadyBool( false );
      assertToolRequirementDoesNotExist( lTask );
      assertTaskHasNoChildTasks( lTask );

      boolean lResult = execute( lTask );

      // Verify that the query returns true.
      assertTrue( lResult );
   }


   /**
    * Verify that if the task has one child task that is marked "tools ready" and another marked
    * "tools not ready" that the result is false.
    */
   @Test
   public void testTaskWithOneChildReadyAndAnotherChildNotReady() {
      TaskKey lTask = new TaskBuilder().hasToolsNotReady().build();
      TaskKey lChildTask1 = new TaskBuilder().withParentTask( lTask ).hasToolsReady().build();
      TaskKey lChildTask2 = new TaskBuilder().withParentTask( lTask ).hasToolsNotReady().build();

      // Ensure the task has a child task marked "tools not ready" and
      // another marked "tools not ready", but has all the other
      // requirements to be a candidate.
      SchedStaskTable lSchedStask = SchedStaskTable.create( lTask );
      assertFalse( lSchedStask.isHistoric() );
      ( new SchedStaskUtil( lSchedStask ) ).assertToolsReadyBool( false );
      assertToolRequirementDoesNotExist( lTask );
      ( new SchedStaskUtil( SchedStaskTable.create( lChildTask1 ) ) ).assertToolsReadyBool( true );
      ( new SchedStaskUtil( SchedStaskTable.create( lChildTask2 ) ) ).assertToolsReadyBool( false );

      boolean lResult = execute( lTask );

      // Verify that the query returns false.
      assertFalse( lResult );
   }


   /**
    * {@inheritDoc}
    */
   @Before
   public void setUp() throws Exception {
      iPartGroup = new PartGroupDomainBuilder( "PG" ).build();
   }


   /**
    * Ensure that the task has no child tasks.
    *
    * @param aTask
    */
   private void assertTaskHasNoChildTasks( TaskKey aTask ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aTask, "nh_event_db_id", "nh_event_id" );

      assertTrue( QuerySetFactory.getInstance().executeQueryTable( "evt_event", lArgs ).isEmpty() );
   }


   /**
    * Based on the ensure exists parameter, ensure that a tool requirement either exists or not for
    * the provided task.
    *
    * @param aTask
    *           the task to test
    * @param aEnsureExists
    *           true if expect the tool requirement to exist, otherwise false
    */
   private void assertToolRequirement( TaskKey aTask, boolean aEnsureExists ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aTask.getEventKey(), "event_db_id", "event_id" );

      boolean lReqExist =
            ( !QuerySetFactory.getInstance().executeQueryTable( "evt_tool", lArgs ).isEmpty() );

      assertTrue( aEnsureExists == lReqExist );
   }


   /**
    * Ensure that a tool requirement does not exists for the task.
    *
    * @param aTask
    */
   private void assertToolRequirementDoesNotExist( TaskKey aTask ) {
      assertToolRequirement( aTask, false );
   }


   /**
    * Ensure that a tool requirement exists for the task.
    *
    * @param aTask
    */
   private void assertToolRequirementExists( TaskKey aTask ) {
      assertToolRequirement( aTask, true );
   }


   /**
    * Execute the update.
    *
    * @param aTask
    *           task key
    *
    * @return number of rows updated
    */
   private boolean execute( TaskKey aTask ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aTask, "aTaskDbId", "aTaskId" );

      // Execute the query
      DataSet lDs = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      assertTrue( "Query unexpectedly returned no result.", lDs.next() );

      return lDs.getBoolean( "result" );
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
