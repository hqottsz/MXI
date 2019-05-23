
package com.mxi.mx.web.query.inventory;

import java.util.Date;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.db.connection.loader.XmlLoader;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * Tests the <tt>UnscheduledChecksByDate</tt> query.
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class UnscheduledChecksByDateTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   /**
    * Tests the query under the following data setup for an aircraft:
    *
    * <ol>
    * <li>One check with no location</li>
    * <li>One check with a location (should not be returned)</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testQueryMultipleLocationsMultipleChecksNoErrors() throws Exception {

      // execute the query
      DataSet lDs = execute( new InventoryKey( 4650, 1 ),
            DateUtils.parseDateTimeString( "05-JAN-2010 00:00" ) );

      // There should be 1 row
      MxAssert.assertEquals( "Number of retrieved rows", 1, lDs.getRowCount() );

      // check row contents
      testRow( lDs, new EventKey( 4650, 2 ), "No location", "NO_LOCATION" );
   }


   /**
    * create the test data
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @BeforeClass
   public static void loadData() throws Exception {
      XmlLoader.load( sDatabaseConnectionRule.getConnection(), UnscheduledChecksByDateTest.class,
            UnscheduledChecksTest.getDataFileInputStream() );

      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            UnscheduledChecksByDateTest.class );
   }


   /**
    * Execute the query.
    *
    * @param aAircraft
    *           date
    * @param aBeforeDate
    *           checks with deadlines after this date won't be returned.
    *
    * @return the result
    */
   private DataSet execute( InventoryKey aAircraft, Date aBeforeDate ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aAircraft, "aAircraftDbId", "aAircraftId" );
      lArgs.add( "aBefore", aBeforeDate );

      // Execute the query
      DataSet lDs = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
      lDs.setSort( DataSet.toSortExpression( "event_key" ), true );
      lDs.setActiveFilterAndSort( true );

      return lDs;
   }


   /**
    * Tests a row in the dataset (calls <tt>aDs.next()</tt> before testing row values.)
    *
    * @param aDs
    *           a dataset
    * @param aEventKey
    *           key of the expected check
    * @param aEventSDesc
    *           short description of the expected check
    * @param aTaskBarcode
    *           barcode of the expected check
    */
   private void testRow( DataSet aDs, EventKey aEventKey, String aEventSDesc,
         String aTaskBarcode ) {
      aDs.next();

      MxAssert.assertEquals( "event_key", aEventKey.toString(), aDs.getString( "event_key" ) );

      MxAssert.assertEquals( aEventSDesc, aDs.getString( "event_sdesc" ) );

      MxAssert.assertEquals( aTaskBarcode, aDs.getString( "barcode_sdesc" ) );
   }
}
