
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
import com.mxi.mx.core.key.TaskKey;


/**
 * The class tests the com.mxi.mx.core.query.req.GetPartRequestsForRootTaskTest query.
 *
 * @author sdevi
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetPartRequestsForRootTaskTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetPartRequestsForRootTaskTest.class );
   }


   private static final PartRequestKey PART_REQUEST_FIRST_ROW = new PartRequestKey( 4650, 1001 );
   private static final PartRequestKey PART_REQUEST_SECOND_ROW = new PartRequestKey( 4650, 1002 );
   private static final PartRequestKey PART_REQUEST_LAST_ROW = new PartRequestKey( 4650, 1003 );


   /**
    * Tests that the query returns the record with not-null values first and the nulls last.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testQuery() throws Exception {

      // set the root task arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( new TaskKey( 4650, 139673 ), "aRootTaskDbId", "aRootTaskId" );

      // execute the query
      DataSet lResult = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            "com.mxi.mx.core.query.req.getPartRequestsForRootTask", lArgs );

      assertEquals( 3, lResult.getRowCount() );

      lResult.next();
      assertEquals( PART_REQUEST_FIRST_ROW,
            lResult.getKey( PartRequestKey.class, "part_request_key" ) );

      lResult.next();
      assertEquals( PART_REQUEST_SECOND_ROW,
            lResult.getKey( PartRequestKey.class, "part_request_key" ) );

      lResult.next();
      assertEquals( PART_REQUEST_LAST_ROW,
            lResult.getKey( PartRequestKey.class, "part_request_key" ) );
   }
}
