
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
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class performs unit testing on the query file with the same package and name.
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class FcModelDetailsTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), FcModelDetailsTest.class );
   }


   /** The Default Forecast Model under test */
   private static final FcModelKey FC_MODEL_1 = new FcModelKey( 4650, 1 );

   /** Another Forecast Model under test */
   private static final FcModelKey FC_MODEL_2 = new FcModelKey( 4650, 2 );

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
      execute( FC_MODEL_1 );
      assertEquals( "Should only be one row.", 1, iDataSet.getRowCount() );
      testRow( FC_MODEL_1, "Test Default Model", true );

      // Execute and test a non-default forecast model
      execute( FC_MODEL_2 );
      assertEquals( "Should only be one row.", 1, iDataSet.getRowCount() );
      testRow( FC_MODEL_2, "Test a Non-Default Model", false );
   }


   /**
    * Execute the query.
    *
    * @param aFcModel
    *           The Forecast Model Pk
    */
   private void execute( FcModelKey aFcModel ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aFcModel, new String[] { "aModelDbId", "aModelId" } );

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
    * @param aDescSDesc
    *           The Name of the Forecast Model
    * @param aDefault
    *           The flag indicating if the Model is the default
    */
   private void testRow( FcModelKey aModelKey, String aDescSDesc, boolean aDefault ) {
      MxAssert.assertEquals( "model_key", aModelKey.toString(), iDataSet.getString( "model_key" ) );
      MxAssert.assertEquals( "desc_sdesc", aDescSDesc, iDataSet.getString( "desc_sdesc" ) );
      MxAssert.assertEquals( "default_bool", aDefault, iDataSet.getBoolean( "default_bool" ) );
   }
}
