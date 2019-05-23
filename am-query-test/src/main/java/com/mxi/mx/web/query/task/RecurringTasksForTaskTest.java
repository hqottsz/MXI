package com.mxi.mx.web.query.task;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.SqlLoader;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.core.key.TaskKey;


/**
 * Tests com.mxi.mx.web.query.task.RecurringTasksForTask query
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class RecurringTasksForTaskTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      SqlLoader.load( sDatabaseConnectionRule.getConnection(), RecurringTasksForTaskTest.class,
            "RecurringTasksForTaskTest.sql" );
   }


   @Test
   public void getRecurringTasksForTask() {

      final TaskKey lCorrectiveTaskKey = new TaskKey( "4650:2" );

      DataSetArgument lDataSetArgument = new DataSetArgument();
      lDataSetArgument.add( lCorrectiveTaskKey, "aTaskDbId", "aTaskId" );

      DataSet lDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lDataSetArgument );

      Assert.assertEquals( 4, lDataSet.getRowCount() );

      // fetch only on the first record of the data set
      lDataSet.first();
      Assert.assertEquals( "4650:3", lDataSet.getString( "rec_task_key" ) );
      Assert.assertEquals( "Task description", lDataSet.getString( "rec_task_sdesc" ) );
      Assert.assertEquals( "ACTV", lDataSet.getString( "rec_task_status" ) );
      Assert.assertEquals( "T40S0001DUA", lDataSet.getString( "rec_task_barcode" ) );
   }


   @Test
   public void getRecurringTasksForTask_EmptyDataset() {

      final TaskKey lInvalidCorrectiveTaskKey = new TaskKey( "4650:99" );

      DataSetArgument lDataSetArgument = new DataSetArgument();
      lDataSetArgument.add( lInvalidCorrectiveTaskKey, "aTaskDbId", "aTaskId" );

      DataSet lDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lDataSetArgument );

      Assert.assertTrue( lDataSet.isEmpty() );
   }

}
