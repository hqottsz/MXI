
package com.mxi.mx.web.query.todolist;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.RFQLineKey;
import com.mxi.mx.core.table.req.ReqPartTable;


/**
 * Tests the query com.mxi.mx.web.query.todolist.StockRequests
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class StockRequestsTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();


   @Before
   public void loadData() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
   }


   /**
    * Execute the query.
    *
    * @param aHrDbId
    *           hr_db_id
    * @param aHrId
    *           hr_id
    * @param aPurchTypeCd
    *           purchase type
    *
    * @return the result
    */
   public DataSet execute( Integer aHrDbId, Integer aHrId, String aPurchTypeCd ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aHrDbId", aHrDbId );
      lArgs.add( "aHrId", aHrId );
      lArgs.add( "aPurchTypeCd", aPurchTypeCd );

      // Execute the query
      return QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Ensures that a stock request with a PO is not returned as a result.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testThatRequestWithPOIsExcluded() throws Exception {

      // first test where the request has no PO, result will be returned
      DataSet lResult = execute( 4650, 999, "ALL" );

      assertEquals( "Expected one row of results", 1, lResult.getRowCount() );

      lResult.first();

      PartRequestKey lStockRequestKey = new PartRequestKey( 4650, 1001 );
      assertRow( lResult, lStockRequestKey );

      ReqPartTable lReqPart = ReqPartTable.findByPrimaryKey( lStockRequestKey );
      lReqPart.setPOLine( new PurchaseOrderLineKey( 4650, 3001, 1 ) );
      lReqPart.update();

      lResult = execute( 4650, 999, "ALL" );

      assertEquals( "Expected no results", 0, lResult.getRowCount() );
   }


   /**
    * Ensures that a stock request with an RFQ is not returned as a result.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testThatRequestWithRFQIsExcluded() throws Exception {

      // first test where the request has no RFQ, result will be returned
      DataSet lResult = execute( 4650, 999, "ALL" );

      assertEquals( "Expected one row of results", 1, lResult.getRowCount() );

      lResult.first();

      PartRequestKey lStockRequestKey = new PartRequestKey( 4650, 1001 );
      assertRow( lResult, lStockRequestKey );

      ReqPartTable lReqPart = ReqPartTable.findByPrimaryKey( lStockRequestKey );
      lReqPart.setRFQLine( new RFQLineKey( 4650, 4001, 1 ) );
      lReqPart.update();

      lResult = execute( 4650, 999, "ALL" );

      assertEquals( "Expected no results", 0, lResult.getRowCount() );
   }


   /**
    * Tests that the current row in the given query results has the expected values.
    *
    * @param aResult
    *           The query results, set to the row to be tested
    * @param aStockRequestKey
    *           The expected stock request key
    */
   private void assertRow( DataSet aResult, PartRequestKey aStockRequestKey ) {
      assertEquals( aResult.getKey( PartRequestKey.class, "stock_request_key" ), aStockRequestKey );
   }
}
