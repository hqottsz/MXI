
/**
 *
 */
package com.mxi.mx.web.query.event;

import static org.junit.Assert.assertEquals;

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
import com.mxi.mx.core.key.EventKey;


/**
 * This class performs unit testing on the query file IncorporatedTasks .
 *
 * @author laxmi
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class IncorporatedTasksTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), IncorporatedTasksTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test the case to get the list of subtasks for the event.
    *
    * <ol>
    * <li>Query for all Tasks associated with that event.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testIncorporatedTasksList() throws Exception {

      // event key and corrective task key as query parameter
      execute( new EventKey( 4650, 85593 ) );
      assertEquals( 1, iDataSet.getRowCount() );
   }


   /**
    * Test the case where events are not there for an incorporated tasks.
    *
    * <ol>
    * <li>Query for all the subtasks for the specified events.</li>
    * <li>Verify that the resuts are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testNoIncorporatedTasks() throws Exception {
      execute( new EventKey( 4650, 8888 ) );
      assertEquals( 0, iDataSet.getRowCount() );
   }


   /**
    * Execute the query.
    *
    * @param aTask
    *           the corrective Task key
    */
   private void execute( EventKey aTask ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aTask, new String[] { "aTaskDbId", "aTaskId" } );

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
