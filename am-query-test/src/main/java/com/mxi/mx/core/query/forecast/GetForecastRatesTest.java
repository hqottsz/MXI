
package com.mxi.mx.core.query.forecast;

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
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.FcModelKey;
import com.mxi.mx.core.key.FcRangeKey;
import com.mxi.mx.core.key.FcRateKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class performs unit testing on the query file with the same package and name.
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetForecastRatesTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetForecastRatesTest.class );
   }


   /** The Default Forecast Model under test */
   private static final FcModelKey FC_MODEL = new FcModelKey( 1, 1 );

   /** The Forecast Range under test */
   private static final FcRangeKey FC_RANGE_1 = new FcRangeKey( 1, 1, 1 );

   /** The Forecast Range under test */
   private static final FcRangeKey FC_RANGE_2 = new FcRangeKey( 1, 1, 2 );

   /** A Forecast Rate under test */
   private static final FcRateKey FC_RATE_1_1 = new FcRateKey( 1, 1, 1, 1, 2 );

   /** A Forecast Rate under test */
   private static final FcRateKey FC_RATE_1_2 = new FcRateKey( 1, 1, 1, 3, 4 );

   /** A Forecast Rate under test */
   private static final FcRateKey FC_RATE_2_1 = new FcRateKey( 1, 1, 2, 1, 2 );

   /** A Forecast Rate under test */
   private static final FcRateKey FC_RATE_2_2 = new FcRateKey( 1, 1, 2, 3, 4 );

   /** A Forecast Rate under test */
   private static final FcRateKey FC_RATE_2_3 = new FcRateKey( 1, 1, 2, 5, 6 );

   /** A DataType under test */
   private static final DataTypeKey DATA_TYPE_1 = new DataTypeKey( 1, 2 );

   /** A DataType under test */
   private static final DataTypeKey DATA_TYPE_2 = new DataTypeKey( 3, 4 );

   /** A DataType under test */
   private static final DataTypeKey DATA_TYPE_3 = new DataTypeKey( 5, 6 );

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
      execute( FC_MODEL );
      assertEquals( "Exact Number of Rows.", 5, iDataSet.getRowCount() );

      testRow( FC_MODEL, "A Model", false );
      testRow( FC_RANGE_2, 7, 16 );
      testRow( FC_RATE_2_1, 3.1415926535 );
      testRow( DATA_TYPE_1, "CODE1", "UNIT1", 0 );
      iDataSet.next();

      testRow( FC_MODEL, "A Model", false );
      testRow( FC_RANGE_2, 7, 16 );
      testRow( FC_RATE_2_2, 42 );
      testRow( DATA_TYPE_2, "CODE2", "UNIT2", 1 );
      iDataSet.next();

      testRow( FC_MODEL, "A Model", false );
      testRow( FC_RANGE_2, 7, 16 );
      testRow( FC_RATE_2_3, 88.056 );
      testRow( DATA_TYPE_3, "CODE3", "UNIT3", 2 );
      iDataSet.next();

      testRow( FC_MODEL, "A Model", false );
      testRow( FC_RANGE_1, 12, 29 );
      testRow( FC_RATE_1_2, 0.0005 );
      testRow( DATA_TYPE_2, "CODE2", "UNIT2", 1 );
      iDataSet.next();

      testRow( FC_MODEL, "A Model", false );
      testRow( FC_RANGE_1, 12, 29 );
      testRow( FC_RATE_1_1, 0.05 );
      testRow( DATA_TYPE_1, "CODE1", "UNIT1", 0 );
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
    * Tests a row for Rate Details in the dataset
    *
    * @param aFcRate
    *           The Forecast Rate Pk
    * @param aRate
    *           The Forecast Rate
    */
   private void testRow( FcRateKey aFcRate, double aRate ) {
      MxAssert.assertEquals( "rate_key", aFcRate.toString(), iDataSet.getString( "rate_key" ) );
      MxAssert.assertEquals( "forecast_rate_qt", aRate, iDataSet.getDouble( "forecast_rate_qt" ) );
   }


   /**
    * Tests a row for Model Details in the dataset
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


   /**
    * Tests a row for Range Details in the dataset
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


   /**
    * Tests a row for Usage Details in the dataset
    *
    * @param aDataType
    *           The Usage Type Pk
    * @param aTypeCd
    *           The Data Type Code
    * @param aUnitCd
    *           The Eng Unit Code
    * @param aPrecision
    *           The Data Type Precision
    */
   private void testRow( DataTypeKey aDataType, String aTypeCd, String aUnitCd,
         double aPrecision ) {
      MxAssert.assertEquals( "data_type_key", aDataType.toString(),
            iDataSet.getString( "data_type_key" ) );
      MxAssert.assertEquals( "data_type_cd", aTypeCd, iDataSet.getString( "data_type_cd" ) );
      MxAssert.assertEquals( "eng_unit_cd", aUnitCd, iDataSet.getString( "eng_unit_cd" ) );
      MxAssert.assertEquals( "entry_prec_qt", aPrecision, iDataSet.getDouble( "entry_prec_qt" ) );
   }
}
