
package com.mxi.mx.core.query.adapter.finance;

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
 * This class performs unit testing on the query file with the same package and name.
 *
 * @author amar
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetOrderLinePartRequestsByIdTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetOrderLinePartRequestsByIdTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test the case where PO id does not exists.
    *
    * <ol>
    * <li>Query for all Part Requests by id.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testLinePartRequestsNotExists() throws Exception {
      execute( 4650, 107108 );
      assertEquals( 0, iDataSet.getRowCount() );
   }


   /**
    * Test the case where POLines-Part Requests for POs are retrieved.
    *
    * <ol>
    * <li>Query for all Part Requests by id.</li>
    * <li>Verify that the results are as expected.</li>
    * <li>Check for the values of row.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testLinePartRequestsRetrievalById() throws Exception {
      execute( 4650, 107107 );
      assertEquals( 3, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( "RQ-00001", "TAIL" );

      iDataSet.next();
      testRow( "RQ-00002", "TAIL 2" );

      iDataSet.next();
      testRow( "RQ-00003", null );
   }


   /**
    * Execute the query.
    *
    * @param aPoDbId
    *           the PO db id.
    * @param aPoId
    *           the PO id.
    */
   private void execute( int aPoDbId, int aPoId ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aPoDbId", aPoDbId );
      lArgs.add( "aPoId", aPoId );

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Method for testing a specific row returned by dataset.
    *
    * @param aRequestId
    *           the request id.
    * @param aAircraftId
    *           the aircraft id.
    */
   private void testRow( String aRequestId, String aAircraftId ) {
      MxAssert.assertEquals( aRequestId, iDataSet.getString( "request_id" ) );
      MxAssert.assertEquals( aAircraftId, iDataSet.getString( "aircraft_id" ) );
   }
}
