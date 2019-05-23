
package com.mxi.mx.core.query.req;

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
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.TransferKey;


/**
 * Tests the GetCompletedIssueTransfer query.
 *
 * @author dsewell
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetCompletedIssueTransferTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetCompletedIssueTransferTest.class );
   }


   private static final PartRequestKey PART_REQUEST = new PartRequestKey( 4650, 2000 );


   /**
    * Tests that only the one valid row is returned by the query.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testQuery() throws Exception {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( PART_REQUEST, "aPartReqDbId", "aPartReqId" );

      DataSet lResults = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      assertEquals( 1, lResults.getRowCount() );

      lResults.first();

      assertEquals( new TransferKey( 4650, 1000 ),
            lResults.getKey( TransferKey.class, "xfer_db_id", "xfer_id" ) );
      assertEquals( 5.0, lResults.getDouble( "xfer_qt" ), 0f );
   }
}
