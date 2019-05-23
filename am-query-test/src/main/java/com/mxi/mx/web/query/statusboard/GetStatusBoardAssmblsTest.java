
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
public final class GetStatusBoardAssmblsTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetStatusBoardAssmblsTest.class );
   }


   /** ASSMBL_CD */
   public static final String ASSMBL_CD = "APP-CD";

   /** STATUS_BOARD_DB_ID */
   public static final int STATUS_BOARD_DB_ID = 4650;

   /** STATUS_BOARD_ID */
   public static final int STATUS_BOARD_ID = 100003;

   /** ASSMBL_DB_ID */
   public static final int ASSMBL_DB_ID = 4650;


   /**
    * Tests that the query returns the proper data for table with two Prod Plans
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

      MxAssert.assertEquals( "assmbl_key", ASSMBL_DB_ID + MxDbKey.SEPARATOR + ASSMBL_CD,
            lDs.getStringAt( 1, "assmbl_key" ) );

      MxAssert.assertEquals( "assmbl_cd", ASSMBL_CD, lDs.getStringAt( 1, "assmbl_cd" ) );
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
