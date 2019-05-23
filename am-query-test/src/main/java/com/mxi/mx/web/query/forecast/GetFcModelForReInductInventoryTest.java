
package com.mxi.mx.web.query.forecast;

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
import com.mxi.mx.core.key.FcModelKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class performs unit testing on the query file with the same package and name.
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetFcModelForReInductInventoryTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetFcModelForReInductInventoryTest.class );
   }


   /** The Default Forecast Model under test */
   private static final FcModelKey FC_MODEL_DEFAULT = new FcModelKey( 1, 1 );

   /** The Non-Default Forecast Model under test */
   private static final FcModelKey FC_MODEL_NON_DEFAULT = new FcModelKey( 2, 2 );

   /** The Aircraft Inventory with a Forecast Model under test */
   private static final InventoryKey INV_ACFT_MODEL = new InventoryKey( 3, 3 );

   /** The Aircraft Inventory without a Forecast Model under test */
   private static final InventoryKey INV_ACFT_NO_MODEL = new InventoryKey( 4, 4 );

   /** The Non-Aircraft Inventory under test */
   private static final InventoryKey INV_NO_ACFT = new InventoryKey( 5, 5 );

   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Tests that the query returns the proper data when looking up an Aircraft that does have a
    * Forecast Model assigned. The specific Model should be returned.
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQueryForACFTWithModel() throws Exception {

      // Execute and test a default forecast model
      execute( INV_ACFT_MODEL );
      assertEquals( "Should only be one row.", 1, iDataSet.getRowCount() );
      testRow( FC_MODEL_NON_DEFAULT );
   }


   /**
    * Tests that the query returns the proper data when looking up an Aircraft that does not have a
    * Forecast Model assigned. The Default Model should be returned.
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQueryForACFTWithoutModel() throws Exception {

      // Execute and test a default forecast model
      execute( INV_ACFT_NO_MODEL );
      assertEquals( "Should only be one row.", 1, iDataSet.getRowCount() );
      testRow( FC_MODEL_DEFAULT );
   }


   /**
    * Tests that the query returns the proper data when looking up Inventory that is not an
    * Aircraft. Null should be returned.
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQueryForNonACFT() throws Exception {

      // Execute and test a default forecast model
      execute( INV_NO_ACFT );
      assertEquals( "Should only be one row.", 1, iDataSet.getRowCount() );
      MxAssert.assertEquals( "model_key", null, iDataSet.getString( "model_key" ) );
   }


   /**
    * Execute the query.
    *
    * @param aInventory
    *           The Inventory Pk
    */
   private void execute( InventoryKey aInventory ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aInventory, new String[] { "aInvDbId", "aInvId" } );

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
      iDataSet.next();
   }


   /**
    * Tests a row in the dataset
    *
    * @param aModelKey
    *           The Forecast Model Pk
    */
   private void testRow( FcModelKey aModelKey ) {
      MxAssert.assertEquals( "model_key", aModelKey.toString(), iDataSet.getString( "model_key" ) );
   }
}
