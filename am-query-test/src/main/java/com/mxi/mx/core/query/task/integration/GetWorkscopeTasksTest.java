
package com.mxi.mx.core.query.task.integration;

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
 * This class tests the query com.mxi.mx.core.query.task.integration.GetWorkscopeTasks.qrx
 *
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetWorkscopeTasksTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetWorkscopeTasksTest.class );
   }


   /**
    * Tests the retrieval of all tasks for given work scope.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testGetWorkscopeTasks() throws Exception {

      int lDbId = 4650;
      int lId = 134117;

      DataSet lDataSet = this.execute( new TaskKey( lDbId, lId ) );

      while ( lDataSet.next() ) {

         MxAssert.assertEquals( "task_key", "4650:134116", lDataSet.getString( "event_key" ) );
      }
   }


   /**
    * This method executes the query in GetWorkscopeTasks.qrx
    *
    * @param aTaskKey
    *           the TaskKey object
    *
    * @return The dataset after execution.
    */
   private DataSet execute( TaskKey aTaskKey ) {
      DataSetArgument lDataSetArgument = new DataSetArgument();
      lDataSetArgument.add( "aCheckDbId", aTaskKey.getDbId() );
      lDataSetArgument.add( "aCheckId", aTaskKey.getId() );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lDataSetArgument );
   }
}
