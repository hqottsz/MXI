
package com.mxi.mx.core.query.task.dependency;

import static com.mxi.mx.core.key.RefRelationTypeKey.DEPT;
import static com.mxi.mx.core.key.RefRelationTypeKey.DRVTASK;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.builder.EventRelationshipBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.domain.builder.TaskRevisionBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;


/**
 * Test class for verifying the GetFullTaskseries.qrx query.
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class GetFullTaskChainTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    *
    * @ GIVEN one task with no "dependent" relationships
    *
    * @ WHEN the task is passed to the query
    *
    * @ THEN no tasks are returned
    *
    */
   @Test
   public void queryReturnsNothingWhenTaskHasNoDependencies() {

      TaskTaskKey lTaskRev = new TaskRevisionBuilder().build();

      TaskKey lTask1 = new TaskBuilder().withTaskRevision( lTaskRev ).build();

      Set<TaskKey> lResults = execute( lTask1 );

      Assert.assertTrue( "Expected the query to return nothing.", lResults.isEmpty() );
   }


   /**
    *
    * @ GIVEN a series of tasks with "dependent" relationships
    *
    * @ WHEN the first task is passed to the query
    *
    * @ THEN all the tasks in the series are returned
    *
    */
   @Test
   public void queryReturnsEntireSeriesOfDependentTasksWhenFirstTaskProvided() {

      TaskTaskKey lTaskRev = new TaskRevisionBuilder().build();

      TaskKey lTask1 = new TaskBuilder().withTaskRevision( lTaskRev ).build();
      TaskKey lTask2 = new TaskBuilder().withTaskRevision( lTaskRev ).build();
      TaskKey lTask3 = new TaskBuilder().withTaskRevision( lTaskRev ).build();

      new EventRelationshipBuilder().fromEvent( lTask1 ).toEvent( lTask2 ).withType( DEPT ).build();
      new EventRelationshipBuilder().fromEvent( lTask2 ).toEvent( lTask3 ).withType( DEPT ).build();

      Set<TaskKey> lResults = execute( lTask1 );

      assertMatches( lResults, lTask1, lTask3, lTask2 );
   }


   /**
    *
    * @ GIVEN a series of tasks with "dependent" relationships
    *
    * @ WHEN the a middle task is passed to the query
    *
    * @ THEN all the tasks in the series are returned
    *
    */
   @Test
   public void queryReturnsEntireSeriesOfDependentTasksWhenMiddleTaskProvided() {

      TaskTaskKey lTaskRev = new TaskRevisionBuilder().build();

      TaskKey lTask1 = new TaskBuilder().withTaskRevision( lTaskRev ).build();
      TaskKey lTask2 = new TaskBuilder().withTaskRevision( lTaskRev ).build();
      TaskKey lTask3 = new TaskBuilder().withTaskRevision( lTaskRev ).build();

      new EventRelationshipBuilder().fromEvent( lTask1 ).toEvent( lTask2 ).withType( DEPT ).build();
      new EventRelationshipBuilder().fromEvent( lTask2 ).toEvent( lTask3 ).withType( DEPT ).build();

      Set<TaskKey> lResults = execute( lTask2 );

      assertMatches( lResults, lTask1, lTask2, lTask3 );
   }


   /**
    *
    * @ GIVEN a series of tasks with "dependent" relationships
    *
    * @ WHEN the last task is passed to the query
    *
    * @ THEN all the tasks in the series are returned
    *
    */
   @Test
   public void queryReturnsEntireSeriesOfDependentTasksWhenLastTaskProvided() {

      TaskTaskKey lTaskRev = new TaskRevisionBuilder().build();

      TaskKey lTask1 = new TaskBuilder().withTaskRevision( lTaskRev ).build();
      TaskKey lTask2 = new TaskBuilder().withTaskRevision( lTaskRev ).build();
      TaskKey lTask3 = new TaskBuilder().withTaskRevision( lTaskRev ).build();

      new EventRelationshipBuilder().fromEvent( lTask1 ).toEvent( lTask2 ).withType( DEPT ).build();
      new EventRelationshipBuilder().fromEvent( lTask2 ).toEvent( lTask3 ).withType( DEPT ).build();

      Set<TaskKey> lResults = execute( lTask3 );

      assertMatches( lResults, lTask1, lTask2, lTask3 );
   }


   /**
    *
    * @ GIVEN a task with non-"dependent" relationship to another task
    *
    * @ WHEN the task is passed to the query
    *
    * @ THEN no tasks are returned
    *
    * Note: this behaviour must be consistent when there is only one task in the series (the query
    * returns nothing).
    *
    */
   @Test
   public void queryReturnsNothingWhenTaskHasOnlyANonDependentRelationshipAndTheTaskIsProvided() {

      TaskTaskKey lTaskRev = new TaskRevisionBuilder().build();

      TaskKey lTask1 = new TaskBuilder().withTaskRevision( lTaskRev ).build();
      TaskKey lTask2 = new TaskBuilder().withTaskRevision( lTaskRev ).build();

      new EventRelationshipBuilder().fromEvent( lTask1 ).toEvent( lTask2 ).withType( DRVTASK )
            .build();

      Set<TaskKey> lResults = execute( lTask1 );

      Assert.assertTrue( "Expected the query to return nothing.", lResults.isEmpty() );
   }


   /**
    *
    * @ GIVEN a task with non-"dependent" relationship to another task
    *
    * @ WHEN the other task is passed to the query
    *
    * @ THEN no tasks are returned.
    *
    * Note: this behaviour must be consistent when there is only one task in the series (the query
    * returns nothing).
    *
    */
   @Test
   public void
         queryReturnsNothingWhenTaskHasOnlyANonDependentRelationshipAndTheOtherTaskIsProvided() {

      TaskTaskKey lTaskRev = new TaskRevisionBuilder().build();

      TaskKey lTask1 = new TaskBuilder().withTaskRevision( lTaskRev ).build();
      TaskKey lTask2 = new TaskBuilder().withTaskRevision( lTaskRev ).build();

      new EventRelationshipBuilder().fromEvent( lTask1 ).toEvent( lTask2 ).withType( DRVTASK )
            .build();

      Set<TaskKey> lResults = execute( lTask2 );

      Assert.assertTrue( "Expected the query to return nothing.", lResults.isEmpty() );
   }


   /**
    *
    * @ GIVEN a series of tasks with "dependent" relationships, except for the last relationship
    * (which is non-"dependent").
    *
    * @ WHEN the first task is passed to the query
    *
    * @ THEN all the tasks in the series are returned except the last task
    *
    */
   @Test
   public void
         queryReturnsEntireSeriesOfDependentTasksTerminatingAtNonDependentTaskWhenFirstTaskProvided() {

      TaskTaskKey lTaskRev = new TaskRevisionBuilder().build();

      TaskKey lTask1 = new TaskBuilder().withTaskRevision( lTaskRev ).build();
      TaskKey lTask2 = new TaskBuilder().withTaskRevision( lTaskRev ).build();
      TaskKey lTask3 = new TaskBuilder().withTaskRevision( lTaskRev ).build();
      TaskKey lTask4 = new TaskBuilder().withTaskRevision( lTaskRev ).build();

      new EventRelationshipBuilder().fromEvent( lTask1 ).toEvent( lTask2 ).withType( DEPT ).build();
      new EventRelationshipBuilder().fromEvent( lTask2 ).toEvent( lTask3 ).withType( DEPT ).build();
      new EventRelationshipBuilder().fromEvent( lTask3 ).toEvent( lTask4 ).withType( DRVTASK )
            .build();

      Set<TaskKey> lResults = execute( lTask1 );

      assertMatches( lResults, lTask1, lTask2, lTask3 );
   }


   /**
    *
    * @ GIVEN a series of tasks with "dependent" relationships, except for the last relationship
    * (which is non-"dependent").
    *
    * @ WHEN the a middle task is passed to the query
    *
    * @ THEN all the tasks in the series are returned except the last task
    *
    */
   @Test
   public void
         queryReturnsEntireSeriesOfDependentTasksTerminatingAtNonDependentTaskWhenMiddleTaskProvided() {

      TaskTaskKey lTaskRev = new TaskRevisionBuilder().build();

      TaskKey lTask1 = new TaskBuilder().withTaskRevision( lTaskRev ).build();
      TaskKey lTask2 = new TaskBuilder().withTaskRevision( lTaskRev ).build();
      TaskKey lTask3 = new TaskBuilder().withTaskRevision( lTaskRev ).build();
      TaskKey lTask4 = new TaskBuilder().withTaskRevision( lTaskRev ).build();

      new EventRelationshipBuilder().fromEvent( lTask1 ).toEvent( lTask2 ).withType( DEPT ).build();
      new EventRelationshipBuilder().fromEvent( lTask2 ).toEvent( lTask3 ).withType( DEPT ).build();
      new EventRelationshipBuilder().fromEvent( lTask3 ).toEvent( lTask4 ).withType( DRVTASK )
            .build();

      Set<TaskKey> lResults = execute( lTask3 );

      assertMatches( lResults, lTask1, lTask2, lTask3 );
   }


   /**
    *
    * @ GIVEN a series of tasks with "dependent" relationships, except for the last relationship
    * (which is non-"dependent").
    *
    * @ WHEN the non-dependent task is passed to the query
    *
    * @ THEN no tasks are returned
    *
    */
   @Test
   public void
         queryReturnsNothingWhenSeriesOfDependentTasksTerminatingAtNonDependentTaskWhenNonDependentTaskProvided() {

      TaskTaskKey lTaskRev = new TaskRevisionBuilder().build();

      TaskKey lTask1 = new TaskBuilder().withTaskRevision( lTaskRev ).build();
      TaskKey lTask2 = new TaskBuilder().withTaskRevision( lTaskRev ).build();
      TaskKey lTask3 = new TaskBuilder().withTaskRevision( lTaskRev ).build();
      TaskKey lTask4 = new TaskBuilder().withTaskRevision( lTaskRev ).build();

      new EventRelationshipBuilder().fromEvent( lTask1 ).toEvent( lTask2 ).withType( DEPT ).build();
      new EventRelationshipBuilder().fromEvent( lTask2 ).toEvent( lTask3 ).withType( DEPT ).build();
      new EventRelationshipBuilder().fromEvent( lTask3 ).toEvent( lTask4 ).withType( DRVTASK )
            .build();

      Set<TaskKey> lResults = execute( lTask4 );

      Assert.assertTrue( "Expected the query to return nothing.", lResults.isEmpty() );
   }


   /**
    *
    * @ GIVEN a series of tasks with "dependent" relationships
    *
    * @ AND one of the tasks within the series has another "dependent" relationship to a task
    * outside the series (different definition)
    *
    * @ WHEN the first task is passed to the query
    *
    * @ THEN all the tasks in the series are returned, as well as, the task outside the series
    *
    */
   @Test
   public void
         queryReturnsEntireSeriesOfDependentTasksPlusDependentTaskOutsideSeriesWhenFirstTaskProvided() {

      TaskTaskKey lTaskRev = new TaskRevisionBuilder().build();

      TaskKey lTask1 = new TaskBuilder().withTaskRevision( lTaskRev ).build();
      TaskKey lTask2 = new TaskBuilder().withTaskRevision( lTaskRev ).build();
      TaskKey lTask3 = new TaskBuilder().withTaskRevision( lTaskRev ).build();

      new EventRelationshipBuilder().fromEvent( lTask1 ).toEvent( lTask2 ).withType( DEPT ).build();
      new EventRelationshipBuilder().fromEvent( lTask2 ).toEvent( lTask3 ).withType( DEPT ).build();

      // Task outside the series but related to a task within the series.
      TaskTaskKey lAnotherTaskRev = new TaskRevisionBuilder().build();
      TaskKey lTask4 = new TaskBuilder().withTaskRevision( lAnotherTaskRev ).build();
      new EventRelationshipBuilder().fromEvent( lTask2 ).toEvent( lTask4 ).withType( DEPT ).build();

      Set<TaskKey> lResults = execute( lTask1 );

      assertMatches( lResults, lTask1, lTask2, lTask3, lTask4 );
   }


   /**
    *
    * @ GIVEN a series of tasks with "dependent" relationships, based on the same definition
    *
    * @ AND one of the tasks within the series has another "dependent" relationship to a task
    * outside the series (different definition)
    *
    * @ WHEN the task with the other dependent relationship is passed to the query
    *
    * @ THEN all the tasks in the series are returned, as well as, the task outside the series
    *
    */
   @Test
   public void
         queryReturnsEntireSeriesOfDependentTasksPlusDependentTaskOutsideSeriesWhenTaskWithOutsideDependencyProvided() {

      TaskTaskKey lTaskRev = new TaskRevisionBuilder().build();

      TaskKey lTask1 = new TaskBuilder().withTaskRevision( lTaskRev ).build();
      TaskKey lTask2 = new TaskBuilder().withTaskRevision( lTaskRev ).build();
      TaskKey lTask3 = new TaskBuilder().withTaskRevision( lTaskRev ).build();

      new EventRelationshipBuilder().fromEvent( lTask1 ).toEvent( lTask2 ).withType( DEPT ).build();
      new EventRelationshipBuilder().fromEvent( lTask2 ).toEvent( lTask3 ).withType( DEPT ).build();

      // Task outside the series but related to a task within the series.
      TaskTaskKey lAnotherTaskRev = new TaskRevisionBuilder().build();
      TaskKey lTask4 = new TaskBuilder().withTaskRevision( lAnotherTaskRev ).build();
      new EventRelationshipBuilder().fromEvent( lTask2 ).toEvent( lTask4 ).withType( DEPT ).build();

      Set<TaskKey> lResults = execute( lTask2 );

      assertMatches( lResults, lTask1, lTask2, lTask3, lTask4 );
   }


   /**
    *
    * @ GIVEN a series of tasks with "dependent" relationships
    *
    * @ AND one of the tasks within the series has another "dependent" relationship to a task
    * outside the series (different definition)
    *
    * @ WHEN the last task is passed to the query
    *
    * @ THEN all the tasks in the series are returned, as well as, the task outside the series
    *
    */
   @Test
   public void
         queryReturnsEntireSeriesOfDependentTasksPlusDependentTaskOutsideSeriesWhenLastTaskProvided() {

      TaskTaskKey lTaskRev = new TaskRevisionBuilder().build();

      TaskKey lTask1 = new TaskBuilder().withTaskRevision( lTaskRev ).build();
      TaskKey lTask2 = new TaskBuilder().withTaskRevision( lTaskRev ).build();
      TaskKey lTask3 = new TaskBuilder().withTaskRevision( lTaskRev ).build();

      new EventRelationshipBuilder().fromEvent( lTask1 ).toEvent( lTask2 ).withType( DEPT ).build();
      new EventRelationshipBuilder().fromEvent( lTask2 ).toEvent( lTask3 ).withType( DEPT ).build();

      // Task outside the series but related to a task within the series.
      TaskTaskKey lAnotherTaskRev = new TaskRevisionBuilder().build();
      TaskKey lTask4 = new TaskBuilder().withTaskRevision( lAnotherTaskRev ).build();
      new EventRelationshipBuilder().fromEvent( lTask2 ).toEvent( lTask4 ).withType( DEPT ).build();

      Set<TaskKey> lResults = execute( lTask3 );

      assertMatches( lResults, lTask1, lTask2, lTask3, lTask4 );
   }


   /**
    *
    * @ GIVEN a series of tasks with "dependent" relationships
    *
    * @ AND one task outside the series (different definition) having a "dependent" relationship to
    * one inside
    *
    * @ WHEN the first task is passed to the query
    *
    * @ THEN all the tasks in the series are returned, as well as, the task outside the series
    *
    */
   @Test
   public void
         queryReturnsEntireSeriesOfDependentTasksPlusTaskDependentOnTaskInsideSeriesWhenFirstTaskProvided() {

      TaskTaskKey lTaskRev = new TaskRevisionBuilder().build();

      TaskKey lTask1 = new TaskBuilder().withTaskRevision( lTaskRev ).build();
      TaskKey lTask2 = new TaskBuilder().withTaskRevision( lTaskRev ).build();
      TaskKey lTask3 = new TaskBuilder().withTaskRevision( lTaskRev ).build();

      new EventRelationshipBuilder().fromEvent( lTask1 ).toEvent( lTask2 ).withType( DEPT ).build();
      new EventRelationshipBuilder().fromEvent( lTask2 ).toEvent( lTask3 ).withType( DEPT ).build();

      // Task outside the series but related to a task within the series.
      TaskTaskKey lAnotherTaskRev = new TaskRevisionBuilder().build();
      TaskKey lTask4 = new TaskBuilder().withTaskRevision( lAnotherTaskRev ).build();
      new EventRelationshipBuilder().fromEvent( lTask4 ).toEvent( lTask2 ).withType( DEPT ).build();

      Set<TaskKey> lResults = execute( lTask1 );

      assertMatches( lResults, lTask1, lTask2, lTask3, lTask4 );
   }


   /**
    *
    * @ GIVEN a series of tasks with "dependent" relationships
    *
    * @ AND one task outside the series (different definition) having a "dependent" relationship to
    * one inside
    *
    * @ WHEN the depends on task is passed to the query
    *
    * @ THEN all the tasks in the series are returned, as well as, the task outside the series
    *
    */
   @Test
   public void
         queryReturnsEntireSeriesOfDependentTasksPlusTaskDependentOnTaskInsideSeriesWhenDependsOnTaskProvided() {

      TaskTaskKey lTaskRev = new TaskRevisionBuilder().build();

      TaskKey lTask1 = new TaskBuilder().withTaskRevision( lTaskRev ).build();
      TaskKey lTask2 = new TaskBuilder().withTaskRevision( lTaskRev ).build();
      TaskKey lTask3 = new TaskBuilder().withTaskRevision( lTaskRev ).build();

      new EventRelationshipBuilder().fromEvent( lTask1 ).toEvent( lTask2 ).withType( DEPT ).build();
      new EventRelationshipBuilder().fromEvent( lTask2 ).toEvent( lTask3 ).withType( DEPT ).build();

      // Task outside the series but related to a task within the series.
      TaskTaskKey lAnotherTaskRev = new TaskRevisionBuilder().build();
      TaskKey lTask4 = new TaskBuilder().withTaskRevision( lAnotherTaskRev ).build();
      new EventRelationshipBuilder().fromEvent( lTask4 ).toEvent( lTask2 ).withType( DEPT ).build();

      Set<TaskKey> lResults = execute( lTask2 );

      assertMatches( lResults, lTask1, lTask2, lTask3, lTask4 );
   }


   /**
    *
    * @ GIVEN a series of tasks with "dependent" relationships
    *
    * @ AND one task outside the series (different definition) having a "dependent" relationship to
    * one inside
    *
    * @ WHEN the last task is passed to the query
    *
    * @ THEN all the tasks in the series are returned and the task outside the series is also
    * returned
    *
    */
   @Test
   public void
         queryReturnsEntireSeriesOfDependentTasksPlusTaskDependentOnTaskInsideSeriesWhenLastTaskProvided() {

      TaskTaskKey lTaskRev = new TaskRevisionBuilder().build();

      TaskKey lTask1 = new TaskBuilder().withTaskRevision( lTaskRev ).build();
      TaskKey lTask2 = new TaskBuilder().withTaskRevision( lTaskRev ).build();
      TaskKey lTask3 = new TaskBuilder().withTaskRevision( lTaskRev ).build();

      new EventRelationshipBuilder().fromEvent( lTask1 ).toEvent( lTask2 ).withType( DEPT ).build();
      new EventRelationshipBuilder().fromEvent( lTask2 ).toEvent( lTask3 ).withType( DEPT ).build();

      // Task outside the series but related to a task within the series.
      TaskTaskKey lAnotherTaskRev = new TaskRevisionBuilder().build();
      TaskKey lTask4 = new TaskBuilder().withTaskRevision( lAnotherTaskRev ).build();
      new EventRelationshipBuilder().fromEvent( lTask4 ).toEvent( lTask2 ).withType( DEPT ).build();

      Set<TaskKey> lResults = execute( lTask3 );

      assertMatches( lResults, lTask1, lTask2, lTask3, lTask4 );
   }


   /**
    * Asserts that the query results match the expected task keys (i.e. contains only the expected
    * keys).
    *
    * @param aResults
    *           results of the query
    * @param aExpected
    *           expected results
    */
   private void assertMatches( Set<TaskKey> aResults, TaskKey... aExpected ) {

      Set<TaskKey> lExpected = new HashSet<TaskKey>( Arrays.asList( aExpected ) );

      Assert.assertTrue( "Unexpected results. Expected = " + lExpected + " , results = " + aResults,
            lExpected.containsAll( aResults ) );

   }


   /**
    * Execute the query within the GetFullTaskChain.qrx file with the provided task key.
    *
    * @param aTaskKey
    *           task to test against
    *
    * @return
    */
   private Set<TaskKey> execute( TaskKey aTaskKey ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aTaskKey.getEventKey(), "aEventDbId", "aEventId" );

      QuerySet lQs = QueryExecutor.executeQuery( getClass(), lArgs );

      Set<TaskKey> lResults = new HashSet<TaskKey>( lQs.getRowCount() );
      while ( lQs.next() ) {

         if ( !lResults.add( lQs.getKey( TaskKey.class, "sched_db_id", "sched_id" ) ) ) {

            throw new RuntimeException( "Query unexpectedly returned a duplicate task = "
                  + lQs.getKey( TaskKey.class, "sched_db_id", "sched_id" ) );
         }
      }

      return lResults;
   }

}
