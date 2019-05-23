
package com.mxi.mx.web.query.statusboard;

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
import com.mxi.mx.common.key.MxDbKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class performs unit testing on the query file with the same package and name.
 *
 * @author Libin Cai
 * @created Nov 19, 2007
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetStatusBoardQueriesTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetStatusBoardQueriesTest.class );
   }


   /** STATUS_BOARD_DB_ID */
   public static final int STATUS_BOARD_DB_ID = 4650;

   /** STATUS_BOARD_ID */
   public static final int STATUS_BOARD_ID = 100003;

   /** QUERY_ID */
   public static final Integer QUERY_ID = 1;

   /** NAME */
   public static final String NAME = "738-1 Engine List";

   /** REFRESH_START */
   public static final String REFRESH_START = "05-Oct-2007 00:00:00";

   /** REFRESH_INTERVAL */
   public static final Integer REFRESH_INTERVAL = 300;

   /** LAST_REFRESH_START */
   public static final String LAST_REFRESH_START = "05-Oct-2006 00:00:00";

   /** LAST_REFRESH_END */
   public static final String LAST_REFRESH_END = "05-Dec-2006 00:00:00";


   /**
    * Tests that the query returns the proper data.
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQuery() throws Exception {

      // Execute the query to retrieve data for the user name
      DataSet lDs = execute( STATUS_BOARD_DB_ID, STATUS_BOARD_ID );

      // Ensure only one unacknowledged login alert is returned
      MxAssert.assertEquals( "Number of retrieved rows", 1, lDs.getRowCount() );

      MxAssert.assertEquals( "query_key",
            STATUS_BOARD_DB_ID + MxDbKey.SEPARATOR + STATUS_BOARD_ID + MxDbKey.SEPARATOR + QUERY_ID,
            lDs.getStringAt( 1, "query_key" ) );

      MxAssert.assertEquals( "query_id", QUERY_ID, lDs.getIntegerAt( 1, "query_id" ) );

      MxAssert.assertEquals( "desc_sdesc", NAME, lDs.getStringAt( 1, "desc_sdesc" ) );
      MxAssert.assertEquals( "refresh_start", REFRESH_START,
            lDs.getStringAt( 1, "refresh_start" ) );
      MxAssert.assertEquals( "refresh_interval", REFRESH_INTERVAL,
            lDs.getIntegerAt( 1, "refresh_interval" ) );
      MxAssert.assertEquals( "last_refresh_start", LAST_REFRESH_START,
            lDs.getStringAt( 1, "last_refresh_start" ) );
      MxAssert.assertEquals( "last_refresh_end", LAST_REFRESH_END,
            lDs.getStringAt( 1, "last_refresh_end" ) );
   }


   /**
    * Execute the query.
    *
    * @param aStatusBoardDbId
    *           Status board db id
    * @param aStatusBoardId
    *           Status board id
    *
    * @return dataSet result.
    */
   private DataSet execute( int aStatusBoardDbId, int aStatusBoardId ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aStatusBoardDbId", aStatusBoardDbId );
      lArgs.add( "aStatusBoardId", aStatusBoardId );

      // Execute the query
      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
