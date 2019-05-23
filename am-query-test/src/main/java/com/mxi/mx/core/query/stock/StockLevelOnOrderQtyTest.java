
package com.mxi.mx.core.query.stock;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.core.key.InvLocStockKey;


/**
 * The class tests the com.mxi.mx.core.query.stock.StockLevelOnOrderQty query.
 *
 * @author sdevi
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class StockLevelOnOrderQtyTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), StockLevelOnOrderQtyTest.class );
   }


   public static final InvLocStockKey STOCK_LEVEL_KEY_1 =
         new InvLocStockKey( "10:1000:4650:100034:4650:10" );
   public static final InvLocStockKey STOCK_LEVEL_KEY_2 =
         new InvLocStockKey( "10:1000:4650:100044:4650:10" );
   public static final InvLocStockKey STOCK_LEVEL_KEY_3 =
         new InvLocStockKey( "10:1000:4650:100054:4650:10" );


   /**
    * Tests scenarios mentioned in NC MX-23406 of the StockLevelOnOrderQty query.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testQuery() throws Exception {

      // test the query for stock request which has a PO created for it and the order qty is
      // different than the stock request qty
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( STOCK_LEVEL_KEY_1, "aLocDbId", "aLocId", "aStockNoDbId", "aStockNoId",
            "aOwnerDbId", "aOwnerId" );

      QuerySet lResults = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      assertEquals( 1, lResults.getRowCount() );

      lResults.first();

      // make sure it returns the qty from po_line
      assertEquals( 30.0, lResults.getDouble( "on_order_and_request_qty" ), 0f );

      // test the query for stock request which has no PO created yet
      lArgs = new DataSetArgument();
      lArgs.add( STOCK_LEVEL_KEY_2, "aLocDbId", "aLocId", "aStockNoDbId", "aStockNoId",
            "aOwnerDbId", "aOwnerId" );

      lResults = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      assertEquals( 1, lResults.getRowCount() );

      lResults.first();

      // make sure it returns the qty from stock request
      assertEquals( 5.0, lResults.getDouble( "on_order_and_request_qty" ), 0f );
   }


   /**
    * Test multiple stock requests linked to an OPEN PO.<br>
    * In this case, the sum of all stock request quantity is used
    *
    */
   @Test
   public void testMulipleStockRequestWithOpenPO() {
      // test the query for multiple stock requests
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( STOCK_LEVEL_KEY_3, "aLocDbId", "aLocId", "aStockNoDbId", "aStockNoId",
            "aOwnerDbId", "aOwnerId" );

      QuerySet lResults = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      assertEquals( 1, lResults.getRowCount() );

      lResults.first();

      // make sure it returns the qty from stock request
      assertEquals( 18.0, lResults.getDouble( "on_order_and_request_qty" ), 0f );
   }


   /**
    * Test multiple stock requests linked to a ISSUED PO.<br>
    * In this case, only the quantity of the po line is used
    *
    */
   @Test
   public void testMultipleStockRequestWithIssuedPO() {

      InvLocStockKey lInvLocKey = new InvLocStockKey( "10:1000:4650:100066:4650:10" );

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lInvLocKey, "aLocDbId", "aLocId", "aStockNoDbId", "aStockNoId", "aOwnerDbId",
            "aOwnerId" );

      QuerySet lResults = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      assertEquals( 1, lResults.getRowCount() );

      lResults.first();

      // make sure it returns the qty from stock request
      assertEquals( 100.0, lResults.getDouble( "on_order_and_request_qty" ), 0f );
   }
}
