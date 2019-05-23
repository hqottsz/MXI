
package com.mxi.mx.core.query.task;

import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;


/**
 * Tests the query
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class GetTasksWithDepsNotYetCreatedTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @Before
   public void setUp() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
   }


   private static final TaskTaskKey TASK_BASIC = new TaskTaskKey( 4650, 1 );

   private static final TaskTaskKey TASK_NO_RULE = new TaskTaskKey( 4650, 2 );

   private static final TaskTaskKey TASK_NO_FORECAST = new TaskTaskKey( 4650, 3 );


   /**
    * Tasks that require forecast generation should return
    */
   @Test
   public void testBasic() {
      TaskKey lTask = addNextTask( TASK_BASIC, new Date(), null );

      Set<TaskKey> lLeafTasks = execute();

      assertTrue( "task requires generation", lLeafTasks.contains( lTask ) );
   }


   /**
    * Tasks with more than 200 forecast tasks do not require additional forecast generation
    */
   @Test
   public void testMax() {
      Date lActvDeadline = new Date();
      TaskKey lPrevTask = addNextTask( TASK_NO_RULE, lActvDeadline, null );
      for ( int i = 0; i < 200; i++ ) {
         lPrevTask = addNextTask( TASK_BASIC, lActvDeadline, lPrevTask );
      }

      Set<TaskKey> lLeafTasks = execute();

      assertTrue( "task does not require generation", lLeafTasks.isEmpty() );
   }


   /**
    * Tasks with no forecast range do not generate forecasts
    */
   @Test
   public void testNoForecastRange() {
      addNextTask( TASK_NO_FORECAST, new Date(), null );

      Set<TaskKey> lLeafTasks = execute();

      assertTrue( "task does not require generation", lLeafTasks.isEmpty() );
   }


   /**
    * Tasks with no scheduling rules do not generate forecasts
    */
   @Test
   public void testNoRules() {
      addNextTask( TASK_NO_RULE, new Date(), null );

      Set<TaskKey> lLeafTasks = execute();

      assertTrue( "task does not require generation", lLeafTasks.isEmpty() );
   }


   /**
    * Tasks past the range do not require addition forecast generation
    */
   @Test
   public void testPastRange() {
      Date lActvDeadline = new Date();
      TaskKey lActvTask = addNextTask( TASK_NO_RULE, lActvDeadline, null );
      Date lForecastDeadline = new Date( lActvDeadline.getTime() + ( 1000 * 60 * 60 * 24 * 10 ) );
      addNextTask( TASK_NO_RULE, lForecastDeadline, lActvTask );

      Set<TaskKey> lLeafTasks = execute();

      assertTrue( "task does not require generation", lLeafTasks.isEmpty() );
   }


   /**
    * Creates a task
    *
    * @param aTask
    *           the task revision
    * @param aDueDate
    *           the task due date
    * @param aPreviousTask
    *           the previous task
    *
    * @return the created task
    */
   private TaskKey addNextTask( TaskTaskKey aTask, Date aDueDate, TaskKey aPreviousTask ) {
      TaskBuilder lBuilder = new TaskBuilder();
      lBuilder.withStatus(
            ( aPreviousTask == null ) ? RefEventStatusKey.ACTV : RefEventStatusKey.FORECAST );
      lBuilder.withPrevTask( aPreviousTask );
      lBuilder.withTaskRevision( aTask );
      lBuilder.withCalendarDeadline( aDueDate );

      return lBuilder.build();
   }


   /**
    * Executes the query
    *
    * @return the set of leaf tasks requiring forecast deadlines
    */
   private Set<TaskKey> execute() {
      Set<TaskKey> lTasks = new HashSet<TaskKey>();
      QuerySet lQs = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            "com.mxi.mx.core.query.task.getTasksWithDepsNotYetCreated" );
      while ( lQs.next() ) {
         lTasks.add( lQs.getKey( TaskKey.class, "sched_db_id", "sched_id" ) );
      }

      return lTasks;
   }
}
