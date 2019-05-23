
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
 * com.mxi.mx.core.query.adapter.logbook.api.service.schedmaint.GetNumOfLaborRows.qrx
 *
 * @author hliu
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetNumOfLaborRowsTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetNumOfLaborRowsTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test the case to get the number of labor rows that has the given external key for the given
    * task.
    *
    * <ol>
    * <li>Query for count of labour rows by task key and labour external key.</li>
    * <li>Verify that the results are as expected.</li>
    * <li>Check for the values of row.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testGetNumOfLaborRows() throws Exception {
      execute( 4650, 75980, "LabourExternalKey1" );
      assertEquals( 1, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( 1 );
   }


   /**
    * This method executes the query in GetNumOfLaborRows.qrx
    *
    * @param aSchedDbId
    *           the task db id.
    * @param aSchedId
    *           the task id
    * @param aExternalKey
    *           labour external key
    */
   private void execute( int aSchedDbId, int aSchedId, String aExternalKey ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aSchedDbId", aSchedDbId );
      lArgs.add( "aSchedId", aSchedId );
      lArgs.add( "aExternalKey", aExternalKey );

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Method for testing a specific row returned by dataset.
    *
    * @param aLaborCount
    *           the count of labour rows.
    *
    * @throws Exception
    *            if an error occurs.
    */
   private void testRow( int aLaborCount ) throws Exception {
      MxAssert.assertEquals( aLaborCount, iDataSet.getInt( "LaborCount" ) );
   }
}
