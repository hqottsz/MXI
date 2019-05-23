
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
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * Test class for Usage Values query
 *
 * @author sridar
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class UsageValuesTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), UsageValuesTest.class );
   }


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
      execute( new InventoryKey( "4650:1234" ), new EventKey( "100:1" ) );
      assertEquals( 1, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( "4650:1234", 100, 102 );
   }


   /**
    * Method for testing a specific row returned by dataset.
    *
    * @throws Exception
    *            Exception if an error occurs.
    */
   @Test
   public void testNoAircraftForAuthority() throws Exception {
      execute( new InventoryKey( "4650:8888" ), new EventKey( "4650:8888" ) );
      assertEquals( 0, iDataSet.getRowCount() );
   }


   /**
    * Execute the query.
    *
    * @param aInventoryKey
    *           the inventoryKey for which all the inventories are required.
    * @param aEventKey
    *           the eventKey for which all the events are required.
    */
   private void execute( InventoryKey aInventoryKey, EventKey aEventKey ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aInventoryKey, new String[] { "aInvNoDbId", "aInvNoId" } );
      lArgs.add( aEventKey, new String[] { "aEventDbId", "aEventId" } );

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
    *            In case error occurs in execution
    */
   private void testRow( String aInventoryKey, Integer aTsnQt, Integer aAssmblTsnQt )
         throws Exception {
      MxAssert.assertEquals( aInventoryKey, iDataSet.getString( "inv_key" ) );
      MxAssert.assertEquals( aTsnQt, iDataSet.getInteger( "tsn_qt" ) );
      MxAssert.assertEquals( aAssmblTsnQt, iDataSet.getInteger( "assmbl_tsn_qt" ) );
      MxAssert.assertNull( iDataSet.getObject( "interval_qt" ) );
      MxAssert.assertNull( iDataSet.getObject( "due_qt" ) );
   }
}
