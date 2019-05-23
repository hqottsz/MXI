
package com.mxi.mx.core.query.order;

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
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class performs unit testing on the query file with the same package and name.
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class IsNotVendorOwnedInventoryOnOrgXchgValidTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            IsNotVendorOwnedInventoryOnOrgXchgValidTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * The query should return a row when the owner organization for the inventory is the same as
    * that for the Order.
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testIsInventoryOrgOnXchgValid() throws Exception {

      // SETUP: Data Setup for running the Function - NA
      InventoryKey lGoodInv = new InventoryKey( 4650, 10000 );

      PurchaseOrderKey lPOKey = new PurchaseOrderKey( 4650, 10010 );

      // ACTION: Execute the Query
      execute( lGoodInv, lPOKey );

      // TEST: Confirm the Data had 1 result
      assertEquals( 1, iDataSet.getRowCount() );

      // TEST: Confirm the company key returned is expected
      int lIsValid = 1;
      testRow( lIsValid );
   }


   /**
    * The query should return no row when the owner organization for the inventory is not the same
    * as that for the Order.
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testNotIsInventoryOrgOnXchgValid() throws Exception {

      // SETUP: Data Setup for running the Function - NA
      InventoryKey lNoGoodInv = new InventoryKey( 4650, 10003 );

      PurchaseOrderKey lPOKey = new PurchaseOrderKey( 4650, 10010 );

      // ACTION: Execute the Query
      execute( lNoGoodInv, lPOKey );

      // TEST: Confirm the Data had 0 result
      assertEquals( 0, iDataSet.getRowCount() );
   }


   /**
    * Execute the query.
    *
    * @param aInventory
    *           {@link InventoryKey} The inventory whose organization is to be tested
    * @param aPOkey
    *           {@link PurchaseOrderKey}
    */
   private void execute( InventoryKey aInventory, PurchaseOrderKey aPOkey ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aInventory, "aInvDbId", "aInvId" );
      lArgs.add( aPOkey, "aPoDbId", "aPoId" );

      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Tests a row of the dataset
    *
    * @param aIsValid
    *           Task PK
    */
   private void testRow( int aIsValid ) {
      iDataSet.first();
      MxAssert.assertEquals( aIsValid, iDataSet.getInt( "is_exist" ) );
   }
}
