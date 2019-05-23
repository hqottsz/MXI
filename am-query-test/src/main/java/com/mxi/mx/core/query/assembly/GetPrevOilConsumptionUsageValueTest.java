
package com.mxi.mx.core.query.assembly;

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
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class runs a unit test on the GetPrevOilConsumptionUsageValueTest query file.
 *
 * @author srengasamy
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetPrevOilConsumptionUsageValueTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetPrevOilConsumptionUsageValueTest.class );
   }


   /** The TSN_QT */
   private static final String TSN_QT = "5.0";


   /**
    * Execute GetPrevOilConsumptionUsageValueTest.qrx
    */
   @Test
   public void testGetRefOilStatusBasedOnPartThreshold() {

      DataSetArgument lPrevUsageArgument = new DataSetArgument();

      // Parameters required by the query.
      lPrevUsageArgument.add( "aUptakeDataTypeDbId", 4651 );
      lPrevUsageArgument.add( "aUptakeDataTypeId", 3007 );
      lPrevUsageArgument.add( "aInvDbId", 4651 );
      lPrevUsageArgument.add( "aInvId", 373504 );
      lPrevUsageArgument.add( "aTimeDataTypeDbId", 10 );
      lPrevUsageArgument.add( "aTimeDataTypeId", 25 );
      lPrevUsageArgument.add( "aEventGdt", "10-Dec-2009 06:47:00" );

      // Execute query!
      DataSet lResult = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lPrevUsageArgument );

      // Get the first row.
      lResult.next();

      // Verify the contents of the row returned.
      testRow( lResult );
   }


   /**
    * Verify the contents of the row returned.
    *
    * @param aResult
    *           the dataset
    */
   private void testRow( DataSet aResult ) {

      // Determine if one row is returned.
      MxAssert.assertEquals( "Number of retrieved rows", 1, aResult.getRowCount() );

      // Determine if the following are returned :
      // evt_inv_usage.tsn_qt
      MxAssert.assertEquals( TSN_QT, aResult.getString( "tsn_qt" ) );
   }
}
