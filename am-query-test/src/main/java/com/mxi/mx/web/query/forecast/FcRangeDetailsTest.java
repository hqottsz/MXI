
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
import com.mxi.mx.core.key.FcRangeKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class performs unit testing on the query file with the same package and name.
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class FcRangeDetailsTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), FcRangeDetailsTest.class );
   }


   /** The Forecast Range under test */
   private static final FcRangeKey FC_RANGE = new FcRangeKey( 4650, 1, 1 );

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

      // Execute and test a forecast range
      execute( FC_RANGE );
      assertEquals( "Should only be one row.", 1, iDataSet.getRowCount() );
      testRow( FC_RANGE, 1, 1 );
   }


   /**
    * Execute the query.
    *
    * @param aRangeKey
    *           The Forecast Range Pk
    */
   private void execute( FcRangeKey aRangeKey ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aRangeKey, new String[] { "aModelDbId", "aModelId", "aRangeId" } );

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
      iDataSet.next();
   }


   /**
    * Tests a row in the dataset
    *
    * @param aRangeKey
    *           The Forecast Range Pk
    * @param aStartMonth
    *           The Name of the Forecast Model
    * @param aStartDate
    *           The flag indicating if the Model is the default
    */
   private void testRow( FcRangeKey aRangeKey, int aStartMonth, int aStartDate ) {
      MxAssert.assertEquals( "range_key", aRangeKey.toString(), iDataSet.getString( "range_key" ) );
      MxAssert.assertEquals( "start_month", aStartMonth, iDataSet.getInt( "start_month" ) );
      MxAssert.assertEquals( "start_day", aStartDate, iDataSet.getInt( "start_day" ) );
   }
}
