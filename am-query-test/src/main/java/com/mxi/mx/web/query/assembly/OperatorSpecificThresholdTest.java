
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
 * This class runs a unit test on the OperatorSpecificThreshold query file.
 *
 * @author srengasamy
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class OperatorSpecificThresholdTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            OperatorSpecificThresholdTest.class );
   }


   /** The CARRIER_KEY */
   private static final String CARRIER_KEY = "5006:100001";

   /** The CARRIER_KEY */
   private static final String ORG_KEY = "5006:100001";

   /** The OIL_THRESHOLD_KEY */
   private static final String OIL_THRESHOLD_KEY_NORMAL = "4650:101:100012";

   /** The OIL_THRESHOLD_KEY */
   private static final String OIL_THRESHOLD_KEY_CAUTION = "4650:101:100014";

   /** The OIL_THRESHOLD_KEY */
   private static final String OIL_THRESHOLD_KEY_WARNING = "4650:101:100013";

   /** The THRESHOLD_ID */
   private static final double THRESHOLD_ID_NORMAL = 100012;

   /** The THRESHOLD_ID */
   private static final double THRESHOLD_ID_CAUTION = 100014;

   /** The THRESHOLD_ID */
   private static final double THRESHOLD_ID_WARNING = 100013;

   /** The THRESHOLD_QT */
   private static final double THRESHOLD_QT = 0;

   /** The OPERATOR */
   private static final String OPERATOR = "DL-DAL / DL / DAL";


   /**
    * Execute OperatorSpecificThreshold.qrx
    */
   @Test
   public void testOilConsumptionRateDefinitionDetails() {

      DataSetArgument lArgs = new DataSetArgument();

      // Parameters required by the query.
      lArgs.add( "aAssemblyDbId", 4650 );
      lArgs.add( "aAssemblyCode", "101" );

      // Execute query!
      DataSet lResult = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // Verify the contents of the row returned.
      lResult.next();
      testRow( lResult, OIL_THRESHOLD_KEY_NORMAL, THRESHOLD_ID_NORMAL );
      lResult.next();
      testRow( lResult, OIL_THRESHOLD_KEY_CAUTION, THRESHOLD_ID_CAUTION );
      lResult.next();
      testRow( lResult, OIL_THRESHOLD_KEY_WARNING, THRESHOLD_ID_WARNING );
   }


   /**
    * Verify the contents of the row returned.
    *
    * @param aResult
    *           the dataset
    * @param aOilThersholdKey
    *           The Threshold Key String
    * @param aThersholdId
    *           the Thershold ID
    */
   private void testRow( DataSet aResult, String aOilThersholdKey, double aThersholdId ) {

      // Determine if the following are returned :
      MxAssert.assertEquals( "Number of retrieved rows", 3, aResult.getRowCount() );

      // eqp_oil_threshold_carrier.assmbl_db_id || ':' || eqp_oil_threshold_carrier.assmbl_cd ||
      // ':' || eqp_oil_threshold_carrier.threshold_id
      MxAssert.assertEquals( aOilThersholdKey, aResult.getString( "oil_threshold_key" ) );

      // eqp_oil_threshold_carrier.carrier_db_id || ':' || eqp_oil_threshold_carrier.carrier_id
      MxAssert.assertEquals( CARRIER_KEY, aResult.getString( "carrier_key" ) );

      // eqp_oil_threshold_carrier.org_db_id || ':' || eqp_oil_threshold_carrier.org_id
      MxAssert.assertEquals( ORG_KEY, aResult.getString( "org_key" ) );

      // eqp_oil_threshold_carrier.threshold_id
      MxAssert.assertEquals( aThersholdId, aResult.getDouble( "threshold_id" ) );

      // eqp_oil_threshold_carrier.threshold_qt
      MxAssert.assertEquals( THRESHOLD_QT, aResult.getDouble( "threshold_qt" ) );

      // org_carrier.carrier_cd || '/' || org_carrier.iata_cd || '/' || org_carrier.icao_cd
      MxAssert.assertEquals( OPERATOR, aResult.getString( "operator" ) );
   }
}
