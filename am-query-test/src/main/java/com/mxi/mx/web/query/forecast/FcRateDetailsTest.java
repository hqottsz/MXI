
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
import com.mxi.mx.core.key.FcRateKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class performs unit testing on the query file with the same package and name.
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class FcRateDetailsTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), FcRateDetailsTest.class );
   }


   /** A Forecast Rate under test */
   private static final FcRateKey FC_RATE_1 = new FcRateKey( 3, 3, 4, 1, 1 );

   /** A Forecast Rate under test */
   private static final FcRateKey FC_RATE_2 = new FcRateKey( 5, 6, 6, 2, 2 );

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

      // Execute and test a forecast rate
      execute( FC_RATE_1 );
      assertEquals( "Should only be one row.", 1, iDataSet.getRowCount() );
      testRow( FC_RATE_1, 3.1415926535, "CODE1", "UNIT1", 0 );

      // Execute and test a forecast rate
      execute( FC_RATE_2 );
      assertEquals( "Should only be one row.", 1, iDataSet.getRowCount() );
      testRow( FC_RATE_2, 42, "CODE2", "UNIT2", 1 );
   }


   /**
    * Execute the query.
    *
    * @param aFcRate
    *           The Forecast Rate Pk
    */
   private void execute( FcRateKey aFcRate ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aFcRate,
            new String[] { "aModelDbId", "aModelId", "aRangeId", "aDataTypeDbId", "aDataTypeId" } );

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
      iDataSet.next();
   }


   /**
    * Tests a row in the dataset
    *
    * @param aFcRate
    *           The Forecast Rate Pk
    * @param aRate
    *           The Forecast Rate
    * @param aTypeCd
    *           The Data Type Code
    * @param aUnitCd
    *           The Eng Unit Code
    * @param aPrecision
    *           The Data Type Precision
    */
   private void testRow( FcRateKey aFcRate, double aRate, String aTypeCd, String aUnitCd,
         double aPrecision ) {
      MxAssert.assertEquals( "rate_key", aFcRate.toString(), iDataSet.getString( "rate_key" ) );
      MxAssert.assertEquals( "forecast_rate_qt", aRate, iDataSet.getDouble( "forecast_rate_qt" ) );
      MxAssert.assertEquals( "data_type_cd", aTypeCd, iDataSet.getString( "data_type_cd" ) );
      MxAssert.assertEquals( "eng_unit_cd", aUnitCd, iDataSet.getString( "eng_unit_cd" ) );
      MxAssert.assertEquals( "entry_prec_qt", aPrecision, iDataSet.getDouble( "entry_prec_qt" ) );
   }
}
