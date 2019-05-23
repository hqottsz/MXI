
package com.mxi.mx.web.query.task;

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
import com.mxi.mx.core.unittest.MxAssert;


/**
 * Retrieves the task tree against a given task.
 *
 * @author gliang
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetTaskTreeTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetTaskTreeTest.class );
   }


   /**
    * Tests the retrieval of assigned tasks count.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testGetTaskTreeFromChildCount() throws Exception {

      DataSet lDataSet = this.execute( new TaskKey( 4650, 134114 ) );

      // only one task return, which is the task itself
      MxAssert.assertEquals( "count", 1, lDataSet.getRowCount() );
      MxAssert.assertEquals( "event_id", 134114, lDataSet.getIntAt( 1, "event_id" ) );
   }


   /**
    * Tests the retrieval of assigned tasks count.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testGetTaskTreeFromParentCount() throws Exception {

      DataSet lDataSet = this.execute( new TaskKey( 4650, 134117 ) );

      // four tasks in the task tree
      MxAssert.assertEquals( "count", 4, lDataSet.getRowCount() );
      MxAssert.assertEquals( "event_id", 134117, lDataSet.getIntAt( 1, "event_id" ) );
      MxAssert.assertEquals( "event_id", 134116, lDataSet.getIntAt( 2, "event_id" ) );
      MxAssert.assertEquals( "event_id", 134115, lDataSet.getIntAt( 3, "event_id" ) );
      MxAssert.assertEquals( "event_id", 134114, lDataSet.getIntAt( 4, "event_id" ) );
   }


   /**
    * This method executes the query in GetTaskTree.qrx
    *
    * @param aTaskKey
    *           the TaskKey object
    *
    * @return The dataset after execution.
    */
   private DataSet execute( TaskKey aTaskKey ) {
      DataSetArgument lDataSetArgument = new DataSetArgument();
      lDataSetArgument.add( "aEventDbId", aTaskKey.getDbId() );
      lDataSetArgument.add( "aEventId", aTaskKey.getId() );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lDataSetArgument );
   }
}
