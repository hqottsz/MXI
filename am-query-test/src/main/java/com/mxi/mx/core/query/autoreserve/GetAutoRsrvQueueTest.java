
package com.mxi.mx.core.query.autoreserve;

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
 * This class tests the GetAutoRsrvQueue query.
 *
 * @author dsewell
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetAutoRsrvQueueTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetAutoRsrvQueueTest.class );
   }


   /**
    * Test the query. Mainly validates the ordering of the results and that is the key to this
    * query.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testQuery() throws Exception {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aBatchQty", 10 );

      DataSet lDS = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      assertEquals( "Row Count", 3, lDS.getRowCount() );

      lDS.next();
      assertQueueId( lDS, 3 );
      lDS.next();
      assertQueueId( lDS, 1 );
      lDS.next();
      assertQueueId( lDS, 2 );
   }


   /**
    * Test the query. Validates the number of results returned.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testQueryBatchQty() throws Exception {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aBatchQty", 2 );

      DataSet lDS = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      assertEquals( "Row Count", 2, lDS.getRowCount() );
   }


   /**
    * Asserts the queue id retrieved is the same as what we expected.
    *
    * @param aDS
    *           The dataset
    * @param aExpected
    *           The expected queue id
    */
   private void assertQueueId( DataSet aDS, int aExpected ) {
      assertEquals( aExpected, aDS.getInt( "auto_rsrv_id" ) );
   }
}
