
package com.mxi.mx.core.query.stask;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.builder.PartRequirementDomainBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.table.sched.SchedStaskTable;
import com.mxi.mx.core.unittest.table.stask.SchedStaskUtil;


/**
 * This class performs unit testing on the query file with the same package and name.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class IsCandidateForPartsReadyTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


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
    * Verify that if the task has an install part requirement that the result is false.
    */
   @Test
   public void testTaskHasInstallPartRequirement() {
      TaskKey lTask = new TaskBuilder().hasPartsNotReady().build();
      withInstallPartRequirement( lTask );

      // Ensure the task has an install part requirement.
      SchedStaskTable lSchedStask = SchedStaskTable.create( lTask );
      assertFalse( lSchedStask.isHistoric() );
      ( new SchedStaskUtil( lSchedStask ) ).assertPartsReadyBool( false );
      assertInstallPartRequirementExists( lTask );

      boolean lResult = execute( lTask );

      // Verify that the query returns false.
      assertFalse( lResult );
   }


   /**
    * Verify that if the task has more than one install part requirement that the result is false.
    */
   @Test
   public void testTaskHasMultipleInstallPartRequirements() {
      TaskKey lTask = new TaskBuilder().hasPartsNotReady().build();
      withInstallPartRequirement( lTask );
      withInstallPartRequirement( lTask );

      // Ensure the task has an install part requirement.
      SchedStaskTable lSchedStask = SchedStaskTable.create( lTask );
      assertFalse( lSchedStask.isHistoric() );
      ( new SchedStaskUtil( lSchedStask ) ).assertPartsReadyBool( false );
      assertInstallPartRequirementExists( lTask );

      boolean lResult = execute( lTask );

      // Verify that the query returns false.
      assertFalse( lResult );
   }


   /**
    * Verify that if the task meets all the requirements to be a candidate that the result is true.
    */
   @Test
   public void testTaskIsACandidate() {
      TaskKey lTask = new TaskBuilder().hasPartsNotReady().build();
      TaskKey lChildTask1 = new TaskBuilder().withParentTask( lTask ).hasPartsReady().build();
      TaskKey lChildTask2 = new TaskBuilder().withParentTask( lTask ).hasPartsReady().build();

      // Ensure the task has all the requirements to be a candidate.
      SchedStaskTable lSchedStask = SchedStaskTable.create( lTask );
      assertFalse( lSchedStask.isHistoric() );
      ( new SchedStaskUtil( lSchedStask ) ).assertPartsReadyBool( false );
      assertInstallPartRequirementDoesNotExist( lTask );
      ( new SchedStaskUtil( SchedStaskTable.create( lChildTask1 ) ) ).assertPartsReadyBool( true );
      ( new SchedStaskUtil( SchedStaskTable.create( lChildTask2 ) ) ).assertPartsReadyBool( true );

      boolean lResult = execute( lTask );

      // Verify that the query returns true, we have a candidate - yeah.
      assertTrue( lResult );
   }


   /**
    * Verify that if the task is historic that the result is false.
    */
   @Test
   public void testTaskIsHistoric() {
      TaskKey lTask = new TaskBuilder().asHistoric().hasPartsNotReady().build();

      // Ensure the task is marked "parts not ready" and is historic.
      SchedStaskTable lSchedStask = SchedStaskTable.create( lTask );
      assertTrue( lSchedStask.isHistoric() );
      ( new SchedStaskUtil( lSchedStask ) ).assertPartsReadyBool( false );

      boolean lResult = execute( lTask );

      // Verify that the query returns false.
      assertFalse( lResult );
   }


   /**
    * Verify that if the task has a child task that is marked "parts not ready" that the result is
    * false.
    */
   @Test
   public void testTaskWithChildTaskMarkPartsNotReady() {
      TaskKey lTask = new TaskBuilder().hasPartsNotReady().build();
      TaskKey lChildTask = new TaskBuilder().withParentTask( lTask ).hasPartsNotReady().build();

      // Ensure the task has a child task marked "parts not ready" but has all the other
      // requirements to be a candidate.
      SchedStaskTable lSchedStask = SchedStaskTable.create( lTask );
      assertFalse( lSchedStask.isHistoric() );
      ( new SchedStaskUtil( lSchedStask ) ).assertPartsReadyBool( false );
      assertInstallPartRequirementDoesNotExist( lTask );
      ( new SchedStaskUtil( SchedStaskTable.create( lChildTask ) ) ).assertPartsReadyBool( false );

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
      TaskKey lTask = new TaskBuilder().hasPartsNotReady().build();

      // Ensure the task has no child tasks but has all the other requirements to be a candidate.
      SchedStaskTable lSchedStask = SchedStaskTable.create( lTask );
      assertFalse( lSchedStask.isHistoric() );
      ( new SchedStaskUtil( lSchedStask ) ).assertPartsReadyBool( false );
      assertInstallPartRequirementDoesNotExist( lTask );
      assertTaskHasNoChildTasks( lTask );

      boolean lResult = execute( lTask );

      // Verify that the query returns true.
      assertTrue( lResult );
   }


   /**
    * Verify that if the task has one child task that is marked "parts ready" and another marked
    * "parts not ready" that the result is false.
    */
   @Test
   public void testTaskWithOneChildReadyAndAnotherChildNotReady() {
      TaskKey lTask = new TaskBuilder().hasPartsNotReady().build();
      TaskKey lChildTask1 = new TaskBuilder().withParentTask( lTask ).hasPartsReady().build();
      TaskKey lChildTask2 = new TaskBuilder().withParentTask( lTask ).hasPartsNotReady().build();

      // Ensure the task has a child task marked "parts not ready" and
      // another marked "parts not ready", but has all the other
      // requirements to be a candidate.
      SchedStaskTable lSchedStask = SchedStaskTable.create( lTask );
      assertFalse( lSchedStask.isHistoric() );
      ( new SchedStaskUtil( lSchedStask ) ).assertPartsReadyBool( false );
      assertInstallPartRequirementDoesNotExist( lTask );
      ( new SchedStaskUtil( SchedStaskTable.create( lChildTask1 ) ) ).assertPartsReadyBool( true );
      ( new SchedStaskUtil( SchedStaskTable.create( lChildTask2 ) ) ).assertPartsReadyBool( false );

      boolean lResult = execute( lTask );

      // Verify that the query returns false.
      assertFalse( lResult );
   }


   /**
    * Based on the ensure exists parameter, ensure that an install part requirement either exists or
    * not for the provided task.
    *
    * @param aTask
    *           the task to test
    * @param aEnsureExists
    *           true if expect the install part requirement to exist, otherwise false
    */
   private void assertInstallPartRequirement( TaskKey aTask, boolean aEnsureExists ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aTask, "sched_db_id", "sched_id" );

      boolean lReqExist = ( !QuerySetFactory.getInstance()
            .executeQueryTable( "sched_inst_part", lArgs ).isEmpty() );

      assertTrue( aEnsureExists == lReqExist );
   }


   /**
    * Ensure that an install part requirement does not exists for the task.
    *
    * @param aTask
    */
   private void assertInstallPartRequirementDoesNotExist( TaskKey aTask ) {
      assertInstallPartRequirement( aTask, false );
   }


   /**
    * Ensure that an install part requirement exists for the task.
    *
    * @param aTask
    */
   private void assertInstallPartRequirementExists( TaskKey aTask ) {
      assertInstallPartRequirement( aTask, true );
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
    * Add an install part requirement to the provided task.
    *
    * @param aTask
    */
   private void withInstallPartRequirement( TaskKey aTask ) {
      new PartRequirementDomainBuilder( aTask ).withInstallQuantity( 1.0 ).build();
   }
}
