
package com.mxi.mx.web.query.fnc;

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
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class tests the query com.mxi.mx.web.query.fnc.TaskDefnsAssignedToAccount.qrx
 *
 * @author sdevi
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class TaskDefnsAssignedToAccountTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            TaskDefnsAssignedToAccountTest.class );
   }


   private static final String TOTAL_COUNT = "total_count";
   private static final String TASKDEFN_NAME = "task_cd_name";
   private static final String TASKDEFN_KEY = "task_key";


   /**
    * Tests the retrieval of task definitions associated to account.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testNoTaskDefnsAssignedToAccount() throws Exception {
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aAccountDbId", 4650 );
      lArgs.add( "aAccountId", 2 );

      // execute the query
      DataSet lDs = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // Assert that No of Rows should be zero
      MxAssert.assertEquals( "Number of retrieved rows", 0, lDs.getRowCount() );
   }


   /**
    * Tests the retrieval of task definitions associated to account.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testTaskDefnsAssignedToAccount() throws Exception {
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aAccountDbId", 4650 );
      lArgs.add( "aAccountId", 1 );

      // execute the query
      DataSet lDs = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // Assert for No of Rows
      MxAssert.assertEquals( "Number of retrieved rows", 3, lDs.getRowCount() );

      // now assert each row data
      lDs.next();
      MxAssert.assertEquals( TOTAL_COUNT, 4, lDs.getInt( TOTAL_COUNT ) );
      MxAssert.assertEquals( TASKDEFN_KEY, "4650:2010", lDs.getString( TASKDEFN_KEY ) );
      MxAssert.assertEquals( TASKDEFN_NAME, "TASK1 (Task 1)", lDs.getString( TASKDEFN_NAME ) );

      lDs.next();
      MxAssert.assertEquals( TOTAL_COUNT, 4, lDs.getInt( TOTAL_COUNT ) );
      MxAssert.assertEquals( TASKDEFN_KEY, "4650:2011", lDs.getString( TASKDEFN_KEY ) );
      MxAssert.assertEquals( TASKDEFN_NAME, "TASK2 (Task 2)", lDs.getString( TASKDEFN_NAME ) );

      lDs.next();
      MxAssert.assertEquals( TOTAL_COUNT, 4, lDs.getInt( TOTAL_COUNT ) );
      MxAssert.assertEquals( TASKDEFN_KEY, "4650:2012", lDs.getString( TASKDEFN_KEY ) );
      MxAssert.assertEquals( TASKDEFN_NAME, "TASK3 (Task 3)", lDs.getString( TASKDEFN_NAME ) );
   }
}
