
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
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class performs unit testing on the query file with the same package and name.
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetFcModelListTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetFcModelListTest.class );
   }


   /** A Forecast Model under test */
   private static final FcModelKey FC_MODEL_A = new FcModelKey( 4650, 1 );

   /** A Forecast Model under test */
   private static final FcModelKey FC_MODEL_B = new FcModelKey( 4650, 2 );

   /** A Forecast Model under test */
   private static final FcModelKey FC_MODEL_C = new FcModelKey( 4650, 3 );

   private static final HumanResourceKey HR_ALL_AUTHORITY = new HumanResourceKey( 4650, 4 );

   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Tests that the query returns the proper data
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQuery() throws Exception {

      // Execute and test the sorting of the list
      execute( HR_ALL_AUTHORITY );
      assertEquals( "Should be three rows.", 3, iDataSet.getRowCount() );

      testRow( FC_MODEL_A, "A" );
      iDataSet.next();
      testRow( FC_MODEL_B, "B" );
      iDataSet.next();
      testRow( FC_MODEL_C, "C" );
   }


   /**
    * Execute the query.
    *
    * @param aHr
    *           The human resource
    */
   private void execute( HumanResourceKey aHr ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aHr, new String[] { "aHrDbId", "aHrId" } );
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            "com.mxi.mx.web.query.forecast.getFcModelList", lArgs );
      iDataSet.next();
   }


   /**
    * Tests a row in the dataset
    *
    * @param aModelKey
    *           The Forecast Model Pk
    * @param aDescSDesc
    *           The Name of the Forecast Model
    */
   private void testRow( FcModelKey aModelKey, String aDescSDesc ) {
      MxAssert.assertEquals( "fc_model_key", aModelKey.toString(),
            iDataSet.getString( "fc_model_key" ) );
      MxAssert.assertEquals( "desc_sdesc", aDescSDesc, iDataSet.getString( "desc_sdesc" ) );
   }
}
