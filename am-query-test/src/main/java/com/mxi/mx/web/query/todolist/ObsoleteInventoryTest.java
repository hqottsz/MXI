
package com.mxi.mx.web.query.todolist;

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


/**
 * Tests the query com.mxi.mx.web.query.todolist.ObsoleteInventory
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class ObsoleteInventoryTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), ObsoleteInventoryTest.class );
   }


   /**
    * Execute the query.
    *
    * @param aUserId
    *           user ID
    *
    * @return the result
    */
   public DataSet execute( Integer aUserId ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aUserId", aUserId );

      // Execute the query
      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Ensures that 1 row is returned since obsolete inventory has 1 quantity
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testThatObsoleteInventoryWithOneQtyIncluded() throws Exception {

      // test where inventory has 1 quantity so a row should be returned
      DataSet lResult = execute( 2 );

      assertEquals( "Expected one row of results", 1, lResult.getRowCount() );
   }


   /**
    * Ensures that no rows are returned since obsolete inventory has zero quantity
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testThatObsoleteInventoryWithZeroQtyExcluded() throws Exception {

      // first test where inventory has zero quantity so no row should be returned
      DataSet lResult = execute( 1 );

      assertEquals( "Expected no results", 0, lResult.getRowCount() );
   }

}
