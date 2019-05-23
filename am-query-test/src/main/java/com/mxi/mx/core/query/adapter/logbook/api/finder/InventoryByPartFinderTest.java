
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
 * com.mxi.mx.core.query.adapter.logbook.api.finder.InventoryByPartFinder.qrx
 *
 * @author hliu
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class InventoryByPartFinderTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), InventoryByPartFinderTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test the case to retrieve inventory key by part serial number and part no key.
    *
    * <ol>
    * <li>Query for inventory key by part serial number and part no key.</li>
    * <li>Verify that the results are as expected.</li>
    * <li>Check for the values of row.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testInventoryByPartFinder() throws Exception {
      execute( "16-399-413", 4650, 1824 );
      assertEquals( 1, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( "4650:199492" );
   }


   /**
    * Test the case where part serial number does not exist.
    *
    * <ol>
    * <li>Query for inventory key by part serial number and part no key.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testPartSerialNumberNotExists() throws Exception {
      execute( "BadSerial16-399-413", 4650, 1824 );

      assertEquals( 0, iDataSet.getRowCount() );
   }


   /**
    * This method executes the query in InventoryByPartFinder.qrx
    *
    * @param aSerialNoOem
    *           the serial number.
    * @param aPartNoDbId
    *           the part no db id.
    * @param aPartNoId
    *           the part no id.
    */
   private void execute( String aSerialNoOem, int aPartNoDbId, int aPartNoId ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aSerialNoOem", aSerialNoOem );
      lArgs.add( "aPartNoDbId", aPartNoDbId );
      lArgs.add( "aPartNoId", aPartNoId );

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Method for testing a specific row returned by dataset.
    *
    * @param aInventoryKey
    *           the inventory Key.
    *
    * @throws Exception
    *            if an error occurs.
    */
   private void testRow( String aInventoryKey ) throws Exception {
      MxAssert.assertEquals( aInventoryKey, iDataSet.getString( "inventory_key" ) );
   }
}
