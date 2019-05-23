
package com.mxi.mx.web.query.assembly;

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
 * This class runs a unit test on the GetEqpOilThresholdForAssmbl query file.
 *
 * @author krangaswamy
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetEqpOilThresholdForAssmblTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetEqpOilThresholdForAssmblTest.class );
   }


   /** The THRESHOLD_QT */
   private static final String THRESHOLD_QT = "2.2";

   /** The STATUS */
   private static final String STATUS = "CAUTION";

   /** The KEY */
   private static final String KEY = "10:APU:100086";

   /** The NORMAL_BOOL */
   private static final String NORMAL_BOOL = "1";


   /**
    * Execute GetEqpOilThresholdForAssmbl.qrx
    */
   @Test
   public void testGetEqpOilThresholdForAssmbl() {

      DataSetArgument lArgs = new DataSetArgument();

      // Parameters required by the query.
      lArgs.add( "aAssmblDbId", 10 );
      lArgs.add( "aAssmblCd", "APU" );

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
      // eqp_oil_threshold_assmbl.threshold_qt
      MxAssert.assertEquals( THRESHOLD_QT, aResult.getString( "threshold_qt" ) );

      // ref_oil_status.oil_status_sdesc
      MxAssert.assertEquals( STATUS, aResult.getString( "status" ) );

      // eqp_oil_threshold_assmbl.assmbl_db_id || ':' || eqp_oil_threshold_assmbl.assmbl_cd || ':'
      // || eqp_oil_threshold_assmbl.threshold_id
      MxAssert.assertEquals( KEY, aResult.getString( "key" ) );

      // DECODE( ref_oil_status.oil_status_sdesc, 'NORMAL', 0, 1
      MxAssert.assertEquals( NORMAL_BOOL, aResult.getString( "normal_bool" ) );

      // Determine if one row is returned.
      MxAssert.assertEquals( "Number of retrieved rows", 1, aResult.getRowCount() );
   }
}
