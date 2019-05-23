
package com.mxi.mx.core.query.adapter.logbook.api.service.schedmaint;

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
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class tests the query
 * com.mxi.mx.core.query.adapter.logbook.api.service.schedmaint.GetNumOfRemaingTasksInWP.qrx
 *
 * @author hliu
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetNumOfRemaingTasksInWPTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetNumOfRemaingTasksInWPTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test the case where the work package has any tasks that are not completed or cancelled.
    *
    * <ol>
    * <li>Query for the count of tasks in the work package by work package key.</li>
    * <li>Verify that the results are as expected.</li>
    * <li>Check for the values of row.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testGetNumOfRemaingTasksInWP() throws Exception {
      execute( 4650, 60479 );
      assertEquals( 1, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( 1 );
   }


   /**
    * This method executes the query in GetNumOfRemaingTasksInWP.qrx
    *
    * @param aCheckDbId
    *           the check db id.
    * @param aCheckId
    *           the check id.
    */
   private void execute( int aCheckDbId, int aCheckId ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aCheckDbId", aCheckDbId );
      lArgs.add( "aCheckId", aCheckId );

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Method for testing a specific row returned by dataset.
    *
    * @param aRemainTasksCount
    *           the count of tasks.
    *
    * @throws Exception
    *            if an error occurs.
    */
   private void testRow( int aRemainTasksCount ) throws Exception {
      MxAssert.assertEquals( aRemainTasksCount, iDataSet.getInt( "RemainTask" ) );
   }
}
