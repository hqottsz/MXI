
package com.mxi.mx.core.services.bsync.zipper.model;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;


/**
 * This class tests the {@link AbstractTaskStrand} class.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class AbstractTaskStrandTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Test that the tasks are ordered correctly according to their order in the chain when they are
    * added in the correct order.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testGetTasksAlreadyInOrder() throws Exception {

      // 1. create a task chain
      Task[] lChain = createTaskChain();

      Strand lStrand = new Strand( null, null );

      // 2. add some members of the chain to the strand in the correct order
      lStrand.add( lChain[1] );
      lStrand.add( lChain[2] );
      lStrand.add( lChain[3] );
      lStrand.add( lChain[4] );

      List<Task> lTaskList = lStrand.getTasks();

      assertEquals( 4, lTaskList.size() );

      // 3. ensure the list of tasks appear in proper order
      assertEquals( lChain[1], lTaskList.get( 0 ) );
      assertEquals( lChain[2], lTaskList.get( 1 ) );
      assertEquals( lChain[3], lTaskList.get( 2 ) );
      assertEquals( lChain[4], lTaskList.get( 3 ) );
   }


   /**
    * Tests that the getTasks method works when nothing is in the strand.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testGetTasksEmpty() throws Exception {
      Strand lStrand = new Strand( null, null );

      List<Task> lTaskList = lStrand.getTasks();

      assertEquals( 0, lTaskList.size() );
   }


   /**
    * Tests that the getTasks method works when only one element is in the strand.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testGetTasksOneElement() throws Exception {
      Strand lStrand = new Strand( null, null );

      Task lTask = createTask( new TaskKey( 4650, 1 ) );
      lStrand.add( lTask );

      List<Task> lTaskList = lStrand.getTasks();

      assertEquals( 1, lTaskList.size() );

      // 3. ensure the list of tasks appear in proper order
      assertEquals( lTask, lTaskList.get( 0 ) );
   }


   /**
    * Test that the tasks are ordered correctly according to their order in the chain.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testGetTasksOrder() throws Exception {

      // 1. create a task chain
      Task[] lChain = createTaskChain();

      Strand lStrand = new Strand( null, null );

      // 2. add some members of the chain to the strand in a random order
      lStrand.add( lChain[1] );
      lStrand.add( lChain[4] );
      lStrand.add( lChain[2] );
      lStrand.add( lChain[3] );

      List<Task> lTaskList = lStrand.getTasks();

      assertEquals( 4, lTaskList.size() );

      // 3. ensure the list of tasks appear in proper order
      assertEquals( lChain[1], lTaskList.get( 0 ) );
      assertEquals( lChain[2], lTaskList.get( 1 ) );
      assertEquals( lChain[3], lTaskList.get( 2 ) );
      assertEquals( lChain[4], lTaskList.get( 3 ) );
   }


   /**
    * Create a task object.
    *
    * @param aTask
    *           The key of the task.
    *
    * @return The task.
    */
   private Task createTask( TaskKey aTask ) {
      Task lTask = new Task( "TEST_STRAND", aTask, null, null, null, aTask.toString(), new Date() );

      return lTask;
   }


   /**
    * Creates a chain of 5 tasks with the first task being historic.
    *
    * @return A task chain
    */
   private Task[] createTaskChain() {

      // the first task is always historic, it should be excluded from the strand
      TaskKey lTask1 = new TaskBuilder().asHistoric().build();

      TaskKey lTask2 = new TaskBuilder().withPrevTask( lTask1 ).build();
      TaskKey lTask3 = new TaskBuilder().withPrevTask( lTask2 ).build();
      TaskKey lTask4 = new TaskBuilder().withPrevTask( lTask3 ).build();
      TaskKey lTask5 = new TaskBuilder().withPrevTask( lTask4 ).build();

      return new Task[] { createTask( lTask1 ), createTask( lTask2 ), createTask( lTask3 ),
            createTask( lTask4 ), createTask( lTask5 ) };
   }


   /**
    * This is a testable concrete subclass of {@link AbstractTaskStrand}.
    */
   private static class Strand extends AbstractTaskStrand<Task> {

      /**
       * Creates a new Strand object.
       *
       * @param aInventory
       *           The Inventory that all the Tasks share
       * @param aAssyInv
       *           The root Assembly Inventory
       */
      public Strand(InventoryKey aInventory, InventoryKey aAssyInv) {
         super( aInventory, aAssyInv );
      }
   }

   /**
    * This is a testable concrete subclass of {@link AbstractZipTask}.
    */
   private static class Task extends AbstractZipTask {

      /**
       * Creates a new testable Task object.
       *
       * @param aUniqueStrandKey
       *           Unique identifier per Strand
       * @param aTaskKey
       *           Actual Task key
       * @param aInventory
       *           Inventory that the Task is against
       * @param aAssyInv
       *           The root Assembly Inventory
       * @param aTaskDefnRev
       *           Task Definition Revision for the task
       * @param aTaskDefnName
       *           The Task Code or Block Chain Name
       * @param aDeadline
       *           Actual Task deadline
       */
      public Task(String aUniqueStrandKey, TaskKey aTaskKey, InventoryKey aInventory,
            InventoryKey aAssyInv, TaskTaskKey aTaskDefnRev, String aTaskDefnName, Date aDeadline) {
         super( aUniqueStrandKey, aTaskKey, aInventory, aAssyInv, aTaskDefnRev, aTaskDefnName,
               aDeadline );
      }
   }
}
