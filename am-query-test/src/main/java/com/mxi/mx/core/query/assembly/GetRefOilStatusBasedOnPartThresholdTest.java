
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
 * This class runs a unit test on the GetRefOilStatusBasedOnPartThreshold query file.
 *
 * @author krangaswamy
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetRefOilStatusBasedOnPartThresholdTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetRefOilStatusBasedOnPartThresholdTest.class );
   }


   /** The oil_status_key */
   private static final String oil_status_key = "10:CAUTION";

   /** The oil_status_desc */
   private static final String oil_status_desc = "CAUTION (caution)";

   /** The threshold_qt */
   private static final String threshold_qt = "1";


   /**
    * Execute GetRefOilStatusBasedOnPartThreshold.qrx
    */
   @Test
   public void testGetRefOilStatusBasedOnPartThreshold() {

      DataSetArgument lArgs = new DataSetArgument();

      // Parameters required by the query.
      lArgs.add( "aPartNoDbId", "4650" );
      lArgs.add( "aPartNoId", "369038" );

      // Execute query!
      DataSet lResult = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

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

      // Determine if the following are returned :
      // ref_oil_status.oil_status_db_id || ':' || ref_oil_status.oil_status_cd
      MxAssert.assertEquals( oil_status_key, aResult.getString( "oil_status_key" ) );

      // toLabel(ref_oil_status.oil_status_cd,ref_oil_status.oil_status_sdesc)
      MxAssert.assertEquals( oil_status_desc, aResult.getString( "oil_status_desc" ) );

      // eqp_oil_threshold_inv.threshold_qt
      MxAssert.assertEquals( threshold_qt, aResult.getInteger( "threshold_qt" ).toString() );

      // Determine if one row is returned.
      MxAssert.assertEquals( "Number of retrieved rows", 1, aResult.getRowCount() );
   }
}
