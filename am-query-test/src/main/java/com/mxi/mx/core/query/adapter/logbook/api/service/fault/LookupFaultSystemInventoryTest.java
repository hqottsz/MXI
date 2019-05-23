
package com.mxi.mx.core.query.adapter.logbook.api.service.fault;

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
 * com.mxi.mx.core.query.adapter.logbook.api.service.fault.LookupFaultSystemInventory.qrx
 *
 * @author hliu
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class LookupFaultSystemInventoryTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            LookupFaultSystemInventoryTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test the case where config slot does not exists.
    *
    * <ol>
    * <li>Query for fault system inventory by given highest inventory (aircraft) and the regular
    * expression for the fault system config slot.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testConfigSlotNotExists() throws Exception {
      execute( 4650, 56380, "^52-31([-][0]{2})?$" );
      assertEquals( 0, iDataSet.getRowCount() );
   }


   /**
    * Test the case to look up a fault system inventory by given highest inventory (aircraft) key
    * and the regular expression for the config slot of the fault system.
    *
    * <ol>
    * <li>Query for fault system inventory by the highest inventory (aircraft) and the regular
    * expression for the fault system config slot.</li>
    * <li>Verify that the results are as expected.</li>
    * <li>Check for the values of row.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testLookupFaultSystemInventory() throws Exception {
      execute( 4650, 56380, "^52-30([-][0]{2})?$" );
      assertEquals( 1, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( "4650:56823" );
   }


   /**
    * This method executes the query in LookupFaultSystemInventory.qrx
    *
    * @param aHInvNoDbId
    *           the highest inventory db id.
    * @param aHInvNoId
    *           the highest inventory id.
    * @param aConfigSlotExpression
    *           the config slot regular expression
    */
   private void execute( int aHInvNoDbId, int aHInvNoId, String aConfigSlotExpression ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aHInvNoDbId", aHInvNoDbId );
      lArgs.add( "aHInvNoId", aHInvNoId );
      lArgs.add( "aConfigSlotExpression", aConfigSlotExpression );

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
      MxAssert.assertEquals( aInventoryKey, iDataSet.getString( "inventory_pk" ) );
   }
}
