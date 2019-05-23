
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
public final class GetStatusBoardColumnsTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetStatusBoardColumnsTest.class );
   }


   /** STATUS_BOARD_DB_ID */
   public static final int STATUS_BOARD_DB_ID = 4650;

   /** STATUS_BOARD_ID */
   public static final int STATUS_BOARD_ID = 100003;

   /** QUERY_ID */
   public static final Integer QUERY_ID = 1;

   /** COLUMN_ID */
   public static final int COLUMN_ID = 3;

   /** ALIGNMENT */
   public static final String ALIGNMENT = "Left";

   /** COLUMN_CD */
   public static final String COLUMN_CD = "cycles";

   /** GROUP_ID */
   public static final Integer GROUP_ID = 2;

   /** TITLE */
   public static final String TITLE = "CYCLES";

   /** ORDER_SEQ */
   public static final Integer ORDER_SEQ = 1;

   /** WIDTH */
   public static final Integer WIDTH = 300;

   /** DEF_BG_COLOR */
   public static final String DEF_BG_COLOR = "00CC88";

   /** DEF_TEXT_COLOR */
   public static final String DEF_TEXT_COLOR = "FFFFFF";

   /** DISPLAY */
   public static final String DISPLAY = "cycles";

   /** DISPLAY_TYPE */
   public static final String DISPLAY_TYPE = "Number";

   /** BG_COLOR */
   public static final String BG_COLOR = "cycles_bg";

   /** TEXT_COLOR */
   public static final String TEXT_COLOR = "cycles_text";

   /** SORT */
   public static final String SORT = "cycles";

   /** SORT_TYPE */
   public static final String SORT_TYPE = "Number";

   /** GROUP_TITLE */
   public static final String GROUP_TITLE = "Usage";

   /** QUERY_NAME */
   public static final String QUERY_NAME = "738-1 Engine List";


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

      MxAssert.assertEquals(
            "column_key", STATUS_BOARD_DB_ID + MxDbKey.SEPARATOR + STATUS_BOARD_ID
                  + MxDbKey.SEPARATOR + QUERY_ID + MxDbKey.SEPARATOR + COLUMN_ID,
            lDs.getStringAt( 1, "column_key" ) );

      MxAssert.assertEquals( "query_id", QUERY_ID, lDs.getIntegerAt( 1, "query_id" ) );
      MxAssert.assertEquals( "column_cd", COLUMN_CD, lDs.getStringAt( 1, "column_cd" ) );
      MxAssert.assertEquals( "group_id", GROUP_ID, lDs.getIntegerAt( 1, "group_id" ) );
      MxAssert.assertEquals( "desc_sdesc", TITLE, lDs.getStringAt( 1, "desc_sdesc" ) );
      MxAssert.assertEquals( "order_seq", ORDER_SEQ, lDs.getIntegerAt( 1, "order_seq" ) );
      MxAssert.assertEquals( "alignment", ALIGNMENT, lDs.getStringAt( 1, "alignment" ) );

      MxAssert.assertEquals( "display", DISPLAY + " (" + DISPLAY_TYPE + ")",
            lDs.getStringAt( 1, "display" ) );
      MxAssert.assertEquals( "sort", SORT + " (" + SORT_TYPE + ")", lDs.getStringAt( 1, "sort" ) );
      MxAssert.assertEquals( "col_bg_color", BG_COLOR, lDs.getStringAt( 1, "col_bg_color" ) );
      MxAssert.assertEquals( "col_text_color", TEXT_COLOR, lDs.getStringAt( 1, "col_text_color" ) );
      MxAssert.assertEquals( "width", WIDTH, lDs.getIntegerAt( 1, "width" ) );
      MxAssert.assertEquals( "default_bg_color", DEF_BG_COLOR,
            lDs.getStringAt( 1, "default_bg_color" ) );
      MxAssert.assertEquals( "default_text_color", DEF_TEXT_COLOR,
            lDs.getStringAt( 1, "default_text_color" ) );

      MxAssert.assertEquals( "group_title", GROUP_TITLE, lDs.getStringAt( 1, "group_title" ) );

      MxAssert.assertEquals( "query_name", QUERY_NAME, lDs.getStringAt( 1, "query_name" ) );
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
