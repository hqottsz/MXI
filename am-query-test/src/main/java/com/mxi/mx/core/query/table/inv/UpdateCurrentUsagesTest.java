
package com.mxi.mx.core.query.table.inv;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.core.key.UsageParmKey;
import com.mxi.mx.core.services.event.usage.CollectedUsageParm;
import com.mxi.mx.core.unittest.table.inv.InvCurrUsage;


/**
 * This is a query unit test to test UpdateCurrentUsages.qrx
 *
 * @author dsewell
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class UpdateCurrentUsagesTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), UpdateCurrentUsagesTest.class );
   }


   private static final String QRX_UPDATE_CURRENT_USAGES =
         "com.mxi.mx.core.query.table.inv.UpdateCurrentUsages";

   private static final int DB_ID = 4650;

   private static final int DPO_DB_ID = 4651;

   private static final UsageParmKey INV = new UsageParmKey( DB_ID, 1000, DB_ID, 2000 );

   private static final UsageParmKey ASSY = new UsageParmKey( DB_ID, 1002, DB_ID, 2000 );

   private static final UsageParmKey ASSY_INV = new UsageParmKey( DB_ID, 1001, DB_ID, 2000 );

   private static final UsageParmKey DPO_INV = new UsageParmKey( DPO_DB_ID, 1000, DPO_DB_ID, 2000 );

   private static final UsageParmKey DPO_ASSY =
         new UsageParmKey( DPO_DB_ID, 1002, DPO_DB_ID, 2000 );

   private static final UsageParmKey DPO_ASSY_INV =
         new UsageParmKey( DPO_DB_ID, 1001, DPO_DB_ID, 2000 );


   /**
    * Tests that inv_curr_usage is properly updated for the given parameters and other rows are not
    * updated. A regular inventory is updated in this test.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testUpdate() throws Exception {
      assertEquals( "Rows updated", 1, executeUpdate( getCollectedUsageParm( INV ) ) );

      InvCurrUsage.assertUsage( INV, 4.0, 3.0, 2.0 );
   }


   /**
    * Tests that inv_curr_usage is properly updated for the given parameters and other rows are not
    * updated. An assembly inventory is updated in this test.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testUpdateAssembly() throws Exception {
      assertEquals( "Rows updated", 1, executeUpdate( getCollectedUsageParm( ASSY ) ) );

      InvCurrUsage.assertUsage( ASSY_INV, 4.0, 3.0, 2.0 );
   }


   /**
    * Tests that inv_curr_usage is not updated for the deployed inventory and other rows are not
    * updated. A deployed regular inventory should not updated in this test.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testUpdateAssemblyDeployed() throws Exception {
      assertEquals( "Rows updated", 0, executeUpdate( getCollectedUsageParm( DPO_ASSY ) ) );

      InvCurrUsage.assertUsage( DPO_ASSY_INV, 3.0, 2.0, 1.0 );
   }


   /**
    * Tests that inv_curr_usage is not updated for the deployed inventory and other rows are not
    * updated. A deployed assembly inventory should not updated in this test.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testUpdateDeployed() throws Exception {
      assertEquals( "Rows updated", 0, executeUpdate( getCollectedUsageParm( DPO_INV ) ) );

      InvCurrUsage.assertUsage( DPO_INV, 3.0, 2.0, 1.0 );
   }


   /**
    * This helper method executes the update with the given parameters in the collected usage parm.
    *
    * @param aUsageParm
    *           The object containing the usage data.
    *
    * @return The number of rows updated.
    */
   private int executeUpdate( CollectedUsageParm aUsageParm ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aUsageParm.getUsageParm(), "aInventoryDbId", "aInventoryId", "aDataTypeDbId",
            "aDataTypeId" );
      lArgs.add( "aTSNDelta", aUsageParm.getTsn() );
      lArgs.add( "aTSODelta", aUsageParm.getTso() );
      lArgs.add( "aTSIDelta", aUsageParm.getTsi() );

      return MxDataAccess.getInstance().executeUpdate( QRX_UPDATE_CURRENT_USAGES, lArgs );
   }


   private CollectedUsageParm getCollectedUsageParm( UsageParmKey aUsageParm ) {
      return new CollectedUsageParm( aUsageParm, 1.0, 1.0, 1.0, 1.0 );
   }
}
