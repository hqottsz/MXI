
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
import com.mxi.mx.core.key.FcModelKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class performs unit testing on the query file with the same package and name.
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class DefaultFcModelTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), DefaultFcModelTest.class );
   }


   /** The Default Forecast Model under test */
   private static final FcModelKey FC_MODEL_1 = new FcModelKey( 4650, 1 );

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

      // Execute and test a default forecast model
      execute();
      assertEquals( "Should only be one row.", 1, iDataSet.getRowCount() );
      testRow( FC_MODEL_1, "Default Model" );
   }


   /**
    * Execute the query.
    */
   private void execute() {

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ) );
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
