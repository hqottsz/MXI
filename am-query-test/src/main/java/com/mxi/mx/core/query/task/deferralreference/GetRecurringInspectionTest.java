package com.mxi.mx.core.query.task.deferralreference;

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
 * Tests com.mxi.mx.core.query.deferralreq.GetRecurringInspection query
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class GetRecurringInspectionTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      SqlLoader.load( sDatabaseConnectionRule.getConnection(), GetRecurringInspectionTest.class,
            "GetRecurringInspectionTest.sql" );
   }


   /**
    * The query should ignore recurring inspections generated from the same deferral reference but
    * from a different fault. There are a total of 4 recurring inspections in the test data, but
    * only 2 are associated with the specified corrective task.
    */
   @Test
   public void getRecurringInspection() {

      final TaskKey lCorrectiveTaskKey = new TaskKey( "4650:4" );
      final TaskKey recurringInspectionKey = new TaskKey( "4650:5" );

      DataSetArgument lDataSetArgument = new DataSetArgument();
      {
         lDataSetArgument.add( lCorrectiveTaskKey, "correctiveTaskDbId", "correctiveTaskId" );
         lDataSetArgument.add( recurringInspectionKey, "initialRecurringDbId",
               "initialRecurringId" );
      }

      DataSet lDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lDataSetArgument );

      Assert.assertEquals( 2, lDataSet.getRowCount() );

      lDataSet.first();
      Assert.assertEquals( 4650, lDataSet.getInt(
            com.mxi.mx.core.table.sched.SchedStaskDao.ColumnName.SCHED_DB_ID.toString() ) );
      Assert.assertEquals( 5, lDataSet
            .getInt( com.mxi.mx.core.table.sched.SchedStaskDao.ColumnName.SCHED_ID.toString() ) );

      lDataSet.last();
      Assert.assertEquals( 4650, lDataSet.getInt(
            com.mxi.mx.core.table.sched.SchedStaskDao.ColumnName.SCHED_DB_ID.toString() ) );
      Assert.assertEquals( 6, lDataSet
            .getInt( com.mxi.mx.core.table.sched.SchedStaskDao.ColumnName.SCHED_ID.toString() ) );
   }


   @Test
   public void getRecurringInspection_EmptyDataset() {

      final TaskKey lInvalidCorrectiveTaskKey = new TaskKey( "4650:99" );
      final TaskKey recurringInspectionKey = new TaskKey( "4650:5" );

      DataSetArgument lDataSetArgument = new DataSetArgument();
      {
         lDataSetArgument.add( lInvalidCorrectiveTaskKey, "correctiveTaskDbId",
               "correctiveTaskId" );
         lDataSetArgument.add( recurringInspectionKey, "initialRecurringDbId",
               "initialRecurringId" );
      }

      DataSet lDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lDataSetArgument );

      Assert.assertTrue( lDataSet.isEmpty() );
   }
}
