
package com.mxi.mx.core.query.inventory;

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
 * Tests the query com.mxi.mx.core.query.inventory.GetExpiredInventory
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetExpiredInventoryTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetExpiredInventoryTest.class );
   }


   /**
    * Ensures that 1 row is returned and the returned row is RFI inventory. Three inventory (RFI,
    * REPREQ and INREP) will be used for testing. The query should return only the first inventory
    * which is RFI.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testThatCondemnRFIInventoryOnly() throws Exception {

      // Build query arguments and execute
      DataSetArgument lArgs = new DataSetArgument();
      DataSet lResult = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // only 1 inventory should be condemned
      assertEquals( "Expected one row of results", 1, lResult.getRowCount() );

      // make sure the RFI inventory is condemned
      lResult.next();
      MxAssert.assertEquals( "inv_no_id", "1", lResult.getInt( "inv_no_id" ) );
   }
}
