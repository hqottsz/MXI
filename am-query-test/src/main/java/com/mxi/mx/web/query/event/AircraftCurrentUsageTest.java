
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
 * Test class for Aircraft Current Usage Query
 *
 * @author sridar
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class AircraftCurrentUsageTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), AircraftCurrentUsageTest.class );
   }


   /* Inventory Key required for testing */
   public static final InventoryKey INV_KEY = new InventoryKey( "4650:1234" );

   /* TSN_QT */
   public static final Integer TSN_QT = 100;

   /* ASSMBL_TSN_QT */
   public static final Integer ASSMBL_TSN_QT = 102;

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
      execute( INV_KEY, INV_KEY );
      assertEquals( 1, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( INV_KEY.toString(), TSN_QT, ASSMBL_TSN_QT );
   }


   /**
    * Check Aircraft does not belongs to Authority
    *
    * @throws Exception
    *            Exception if an error occurs.
    */
   @Test
   public void testNoAircraftForAuthority() throws Exception {
      execute( new InventoryKey( "4650:8888" ), new InventoryKey( "4318:111" ) );
      assertEquals( 0, iDataSet.getRowCount() );
   }


   /**
    * Execute the query.
    *
    * @param aInventoryKey
    *           the inventoryKey for which all the inventories are required.
    * @param aKey
    *           the akey for which all the inventories are required.
    */
   private void execute( InventoryKey aInventoryKey, InventoryKey aKey ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aInventoryKey, new String[] { "aAcftDbId", "aAcftId" } );
      lArgs.add( aKey, new String[] { "aAssmDbId", "aAssmId" } );

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Method for testing a specific row returned by dataset.
    *
    * @param aInventoryKey
    *           Inventory Key
    * @param aTsnQt
    *           Tsn Qt
    * @param aAssmblTsnQt
    *           Assmbl Tsn Qt
    *
    * @throws Exception
    *            if an exception occurs in execution.
    */
   private void testRow( String aInventoryKey, Integer aTsnQt, Integer aAssmblTsnQt )
         throws Exception {
      MxAssert.assertEquals( aInventoryKey, iDataSet.getString( "aircraft_key" ) );
      MxAssert.assertEquals( aTsnQt, iDataSet.getInteger( "tsn_qt" ) );
      MxAssert.assertNull( iDataSet.getObject( "assmbl_tsn_qt" ) );
      MxAssert.assertNull( iDataSet.getObject( "due_qt" ) );
   }
}
