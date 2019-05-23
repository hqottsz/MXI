
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
 * This class tests the query com.mxi.mx.web.query.task.ValidateTasksHaveSameRootInventory.qrx
 *
 * @author mdesouza
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class ValidateTasksHaveSameRootInventoryTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            ValidateTasksHaveSameRootInventoryTest.class );
   }


   /**
    * Tests that a single on-wing tasks passes
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testSingleTaskOnAircraft() throws Exception {

      DataSet lResults = execute( new TaskKey( "4650:10630" ) );

      assertEquals( 1, lResults.getRowCount() );
   }


   /**
    * Tests that two tasks on the same aircraft pass
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testMultipleTaskOnAircraft() throws Exception {

      DataSet lResults =
            execute( new TaskKey[] { new TaskKey( "4650:10630" ), new TaskKey( "4650:10638" ) } );

      assertEquals( 1, lResults.getRowCount() );
   }


   /**
    * Tests that tasks on two different aircraft does not pass
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testMultipleTaskDifferentAircraft() throws Exception {

      DataSet lResults =
            execute( new TaskKey[] { new TaskKey( "4650:10630" ), new TaskKey( "4650:10640" ) } );

      assertEquals( 2, lResults.getRowCount() );
   }


   /**
    * Tests that a non main inventory event does not return
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testNonMainInvEvent() throws Exception {

      DataSet lResults = execute( new TaskKey( "4650:10641" ) );

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
