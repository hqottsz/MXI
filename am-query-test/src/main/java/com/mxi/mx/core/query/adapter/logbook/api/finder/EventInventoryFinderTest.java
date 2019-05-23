
package com.mxi.mx.core.query.adapter.logbook.api.finder;

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
 * This class tests the query
 * com.mxi.mx.core.query.adapter.logbook.api.finder.EventInventoryFinder.qrx
 *
 * @author hliu
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class EventInventoryFinderTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), EventInventoryFinderTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test the case to retrieve the inventory key by event key and highest inventory (aircraft) key.
    *
    * <ol>
    * <li>Query for inventory by event key and highest inventory key.</li>
    * <li>Verify that the results are as expected.</li>
    * <li>Check for the values of row.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testEventInventoryFinder() throws Exception {
      execute( 4650, 60503, 4650, 56380 );
      assertEquals( 1, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( "4650:56587" );
   }


   /**
    * Test the case where event or highest inventory do not exist.
    *
    * <ol>
    * <li>Query for inventory by event key and highest inventory key.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testInventoryNotExists() throws Exception {
      execute( 4650, 60504, 4650, 56380 );
      assertEquals( 0, iDataSet.getRowCount() );
   }


   /**
    * This method executes the query in EventInventoryFinder.qrx
    *
    * @param aEventDbId
    *           the event db id.
    * @param aEventId
    *           the event id.
    * @param aHInvNoDbId
    *           the highest inventory no db id.
    * @param aHInvNoId
    *           the highest inventory no id.
    */
   private void execute( int aEventDbId, int aEventId, int aHInvNoDbId, int aHInvNoId ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aEventDbId", aEventDbId );
      lArgs.add( "aEventId", aEventId );
      lArgs.add( "aHInvNoDbId", aHInvNoDbId );
      lArgs.add( "aHInvNoId", aHInvNoId );

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Method for testing a specific row returned by dataset.
    *
    * @param aInventoryKey
    *           the inventory key.
    *
    * @throws Exception
    *            if an error occurs.
    */
   private void testRow( String aInventoryKey ) throws Exception {
      MxAssert.assertEquals( aInventoryKey, iDataSet.getString( "inventory_key" ) );
   }
}
