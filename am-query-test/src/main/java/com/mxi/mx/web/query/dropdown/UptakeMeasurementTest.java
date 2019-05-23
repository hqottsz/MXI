
package com.mxi.mx.web.query.dropdown;

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
 * This class runs a unit test on the UptakeMeasurement query file.
 *
 * @author krangaswamy
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class UptakeMeasurementTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), UptakeMeasurementTest.class );
   }


   /** The UPTAKE_CD_NAME */
   private static final String UPTAKE_CD_NAME = "VOL (VOLUME)";

   /** The DATA_TYPE_KEY */
   private static final String DATA_TYPE_KEY = "4650:5012";


   /**
    * Execute UptakeMeasurement.qrx
    */
   @Test
   public void testUptakeMeasurement() {

      DataSetArgument lArgs = new DataSetArgument();

      // Parameters required by the query.
      lArgs.add( "aAssmblClassDbId", "10" );
      lArgs.add( "aAssmblClassCd", "APU" );

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
      // mim_data_type.data_type_cd || ' (' || mim_data_type.data_type_sdesc || ')'
      MxAssert.assertEquals( UPTAKE_CD_NAME, aResult.getString( "uptake_cd_name" ) );

      // mim_data_type.data_type_db_id || ':' || mim_data_type.data_type_id
      MxAssert.assertEquals( DATA_TYPE_KEY, aResult.getString( "data_type_key" ) );

      // Determine if one row is returned.
      MxAssert.assertEquals( "Number of retrieved rows", 1, aResult.getRowCount() );
   }
}
