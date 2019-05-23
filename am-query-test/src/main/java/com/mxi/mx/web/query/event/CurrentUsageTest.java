
/**
 *
 */
package com.mxi.mx.web.query.event;

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
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class performs unit testing on the query file with the same package and name.
 *
 * @author sridar
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class CurrentUsageTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), CurrentUsageTest.class );
   }


   /* Inventory Key required for testing */
   public static final InventoryKey INV_KEY = new InventoryKey( "4650:1234" );

   /* TSN_QT */
   public static final Integer TSN_QT = 100;

   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test aircraft inventory.
    *
    * <ol>
    * <li>Query for aircraft inventory.</li>
    * <li>Verify that the resuts are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testAircraftForAuthority() throws Exception {
      execute( INV_KEY );
      assertEquals( 1, iDataSet.getRowCount() );

      iDataSet.next();

      MxAssert.assertEquals( INV_KEY.toString(), iDataSet.getString( "aircraft_key" ) );
      MxAssert.assertEquals( TSN_QT, iDataSet.getInteger( "tsn_qt" ) );
      MxAssert.assertNull( iDataSet.getObject( "assmbl_tsn_qt" ) );
      MxAssert.assertNull( iDataSet.getObject( "interval_qt" ) );
   }


   /**
    * Check Aircraft does not belongs to Authority
    *
    * @throws Exception
    *            Exception if an error occurs.
    */
   @Test
   public void testNoAircraftForAuthority() throws Exception {
      execute( new InventoryKey( "4650:8888" ) );
      assertEquals( 0, iDataSet.getRowCount() );
   }


   /**
    * Execute the query.
    *
    * @param aInventoryKey
    *           the inventoryKey for which all the inventories are required.
    */
   private void execute( InventoryKey aInventoryKey ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aInventoryKey, new String[] { "aInvNoDbId", "aInvNoId" } );

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
