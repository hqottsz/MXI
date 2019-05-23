
package com.mxi.mx.web.query.task;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.core.key.TaskKey;


/**
 * This class tests the query com.mxi.mx.web.query.task.ValidateTasksAreLoose.qrx
 *
 * @author mdesouza
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class ValidateTasksAreLooseTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), ValidateTasksAreLooseTest.class );
   }


   /**
    * Tests that a task already assigned to a work package should not qualify for package and
    * complete
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testTaskAlreadyAssignedToWorkPackage() throws Exception {

      DataSet lResults = execute( new TaskKey( "4650:105361" ) );

      // if rows were returned, then not all tasks are loose
      assertEquals( 1, lResults.getRowCount() );
   }


   /**
    * Tests that a loose task qualifies for package and complete
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testLooseTask() throws Exception {

      DataSet lResults = execute( new TaskKey( "4650:105364" ) );

      assertEquals( 0, lResults.getRowCount() );
   }


   /**
    * Tests that a loose and assigned task does not qualify
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testLooseAndAssignedTasks() throws Exception {

      DataSet lResults =
            execute( new TaskKey[] { new TaskKey( "4650:105361" ), new TaskKey( "4650:105364" ) } );

      assertEquals( 1, lResults.getRowCount() );
   }


   /**
    * Tests that a loose task has a bad rstat code is not seen by the query
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testLooseTaskWithBadRstatCode() throws Exception {

      DataSet lResults = execute( new TaskKey( "4650:105365" ) );

      assertEquals( 0, lResults.getRowCount() );
   }


   /**
    * Tests that a task assigned to a wp with a bad rstat code is not seen by the query
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testAssignedTaskWithWpBadRstatCode() throws Exception {

      DataSet lResults = execute( new TaskKey( "4650:105363" ) );

      assertEquals( 0, lResults.getRowCount() );
   }


   /**
    * This method executes a query with a single task
    *
    * @param aQueryName
    *           the class name of a query
    * @param aTaskKey
    *           a list of task events
    *
    * @return The dataset after execution.
    */
   private DataSet execute( TaskKey aTaskKey ) {
      return execute( new TaskKey[] { aTaskKey } );
   }


   /**
    * This method executes a query using a list of tasks
    *
    * @param aTaskKeyList
    *           a list of task events
    *
    * @return The dataset after execution.
    */
   private DataSet execute( TaskKey[] aTaskKeyList ) {
      List<Integer> lTaskDbIds = new ArrayList<Integer>();
      List<Integer> lTaskIds = new ArrayList<Integer>();
      for ( TaskKey lTask : aTaskKeyList ) {
         lTaskDbIds.add( lTask.getDbId() );
         lTaskIds.add( lTask.getId() );
      }
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.addIntegerArray( "aEventDbIdArray", lTaskDbIds );
      lArgs.addIntegerArray( "aEventIdArray", lTaskIds );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
