
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
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.FcModelKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class performs unit testing on the query file with the same package and name.
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetMissingForecastRateTypesTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      // Delete existing Usage Data Types
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "domain_type_cd", "US" );
      MxDataAccess.getInstance().executeDelete( "mim_data_type", lArgs );

      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetMissingForecastRateTypesTest.class );
   }


   /** The Default Forecast Model under test */
   private static final FcModelKey FC_MODEL = new FcModelKey( 1, 1 );

   /** A DataType under test */
   private static final DataTypeKey DATA_TYPE_1 = new DataTypeKey( 99, 1 );

   /** A DataType under test */
   private static final DataTypeKey DATA_TYPE_2 = new DataTypeKey( 99, 2 );

   /** A DataType under test */
   private static final DataTypeKey DATA_TYPE_3 = new DataTypeKey( 99, 3 );

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

      // Execute and test the usages
      execute( FC_MODEL );
      assertEquals( "Exact Number of Rows.", 3, iDataSet.getRowCount() );

      testRow( DATA_TYPE_1, "XCODE1", "XUNIT1", 1 );
      iDataSet.next();
      testRow( DATA_TYPE_2, "XCODE2", "XUNIT2", 2 );
      iDataSet.next();
      testRow( DATA_TYPE_3, "XCODE3", "XUNIT3", 3 );
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
      lArgs.add( "aRangeId", 2 );

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
      iDataSet.next();
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
