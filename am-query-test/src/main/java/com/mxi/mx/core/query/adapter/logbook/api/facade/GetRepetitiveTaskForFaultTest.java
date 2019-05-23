
package com.mxi.mx.core.query.adapter.logbook.api.facade;

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
 * com.mxi.mx.core.query.adapter.logbook.api.facade.GetRepetitiveTaskForFault.qrx
 *
 * @author hliu
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetRepetitiveTaskForFaultTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetRepetitiveTaskForFaultTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test the case where repetitive task does not exist by invalid fault.
    *
    * <ol>
    * <li>Query for repetitive tasks by fault.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testFaultNotExists() throws Exception {
      execute( 4650, 113068 );
      assertEquals( 0, iDataSet.getRowCount() );
   }


   /**
    * Test the case to retrieve repetitive tasks by fault key.
    *
    * <ol>
    * <li>Query for repetitive tasks by fault.</li>
    * <li>Verify that the results are as expected.</li>
    * <li>Check for the values of row.</li>
    * <li>Verify that the resuts are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testGetRepetitiveTaskForFault() throws Exception {
      execute( 4650, 113067 );
      assertEquals( 1, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( "4650:113073" );
   }


   /**
    * This method executes the query in GetRepetitiveTaskForFault.qrx
    *
    * @param aFaultDbId
    *           the fault db id.
    * @param aFaultId
    *           the fault id.
    */
   private void execute( int aFaultDbId, int aFaultId ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aFaultDbId", aFaultDbId );
      lArgs.add( "aFaultId", aFaultId );

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Method for testing a specific row returned by dataset.
    *
    * @param aTaskKey
    *           the task key.
    *
    * @throws Exception
    *            if an error occurs.
    */
   private void testRow( String aTaskKey ) throws Exception {
      MxAssert.assertEquals( aTaskKey, iDataSet.getString( "pk_repetitive_task" ) );
   }
}
