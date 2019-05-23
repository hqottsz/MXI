
package com.mxi.mx.core.query.adapter.logbook.api.service.fault;

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
 * com.mxi.mx.core.query.adapter.logbook.api.service.fault.GetCorrTaskEventKeyFromFaultID.qrx
 *
 * @author hliu
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetCorrTaskEventKeyFromFaultIDTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetCorrTaskEventKeyFromFaultIDTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test the case where task event does not exist.
    *
    * <ol>
    * <li>Query for the event key associated with a corrective task, based on the fault.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testFaultNotExists() throws Exception {
      execute( 4650, 87071 );
      assertEquals( 0, iDataSet.getRowCount() );
   }


   /**
    * Test the case to get the event key associated with a corrective task, based on the fault.
    *
    * <ol>
    * <li>Query for the event key associated with a corrective task, based on the fault.</li>
    * <li>Verify that the results are as expected.</li>
    * <li>Check for the values of row.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testGetCorrTaskEventKeyFromFaultID() throws Exception {
      execute( 4650, 87072 );
      assertEquals( 1, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( 4650, 1234 );
   }


   /**
    * This method executes the query in GetCorrTaskEventKeyFromFaultID.qrx
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
    * @param aEventDbId
    *           the aircraft inventory key.
    * @param aEventId
    *           DOCUMENT_ME
    *
    * @throws Exception
    *            if an error occurs.
    */
   private void testRow( int aEventDbId, int aEventId ) throws Exception {
      MxAssert.assertEquals( aEventDbId, iDataSet.getInt( "event_db_id" ) );
      MxAssert.assertEquals( aEventId, iDataSet.getInt( "event_id" ) );
   }
}
