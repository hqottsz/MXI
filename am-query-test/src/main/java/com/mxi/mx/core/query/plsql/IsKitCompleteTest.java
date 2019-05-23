
package com.mxi.mx.core.query.plsql;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This test class asserts that the function isKitComplete within the KIT_PKG operates correctly
 *
 * @author cdaley
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class IsKitCompleteTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), IsKitCompleteTest.class );
   }


   /**
    * This test validates that a kit will show up as complete when all inventory assigned to the
    * line is available
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testKitComplete() throws Exception {
      InventoryKey lKitNoKey = new InventoryKey( 1, 1 );

      // ACTION: Execute the function
      int lResult = execute( lKitNoKey );

      MxAssert.assertEquals( 1, lResult );
   }


   /**
    * This test validates that unservicable inventory will not be counted towards the kit being
    * complete
    *
    * @throws Exception
    */
   @Test
   public void testKitUnserviceable() throws Exception {
      InventoryKey lKitNoKey = new InventoryKey( 5, 1 );

      // ACTION: Execute the function
      int lResult = execute( lKitNoKey );

      MxAssert.assertEquals( 0, lResult );
   }


   /**
    * This test validates that a kit will show up as complete when there are extra inventory
    * available for batch and serialized.<br />
    * <br />
    * The kit requires 1 SER and 5 BATCH, but there are 3 SER and 10 BATCH available
    *
    * @throws Exception
    */
   @Test
   public void testOverComplete() throws Exception {
      InventoryKey lKitNoKey = new InventoryKey( 2, 1 );

      // ACTION: Execute the function
      int lResult = execute( lKitNoKey );

      MxAssert.assertEquals( 1, lResult );
   }


   /**
    * This test validates that a kit will show up as complete when there are not enough inventory
    * available for batch .<br />
    * <br />
    * The kit requires 1 SER and 5 BATCH, but there are 1 SER and 4 BATCH available
    *
    * @throws Exception
    */
   @Test
   public void testUnderCompleteBATCH() throws Exception {
      InventoryKey lKitNoKey = new InventoryKey( 3, 1 );

      // ACTION: Execute the function
      int lResult = execute( lKitNoKey );

      MxAssert.assertEquals( 0, lResult );
   }


   /**
    * This test validates that a kit will show up as complete when there are not enough inventory
    * available for serialized.<br />
    * <br />
    * The kit requires 1 SER and 5 BATCH, but there are 0 SER and 5 BATCH available
    *
    * @throws Exception
    */
   @Test
   public void testUnderCompleteSER() throws Exception {
      InventoryKey lKitNoKey = new InventoryKey( 4, 1 );

      // ACTION: Execute the function
      int lResult = execute( lKitNoKey );

      MxAssert.assertEquals( 0, lResult );
   }


   /**
    * Execute the function.
    *
    * @param aInventoryKey
    *           The root of a Task Tree.
    *
    * @return the calculated Planning Yield Date.
    *
    * @throws Exception
    *            if an error occurs.
    */
   private int execute( InventoryKey aInventoryKey ) throws Exception {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aInventoryKey, new String[] { "aKitDbId", "aKitId" } );

      String[] lParmOrder = { "aKitDbId", "aKitId" };

      // Execute the query
      return Integer.parseInt( QueryExecutor.executeFunction(
            sDatabaseConnectionRule.getConnection(), "KIT_PKG.ISKITCOMPLETE", lParmOrder, lArgs ) );
   }
}
