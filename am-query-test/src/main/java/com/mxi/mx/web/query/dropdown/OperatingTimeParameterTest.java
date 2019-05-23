
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
 * This class runs a unit test on the OperatingTimeParameter query file.
 *
 * @author krangaswamy
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class OperatingTimeParameterTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), OperatingTimeParameterTest.class );
   }


   /** The OPERATING_PARAMETER */
   private static final String OPERATING_PARAMETER = "H (HOURS)";

   /** The DATA_TYPE_KEY */
   private static final String DATA_TYPE_KEY = "4650:3012";


   /**
    * Execute OperatingTimeParameter.qrx
    */
   @Test
   public void testOperatingTimeParameter() {

      DataSetArgument lArgs = new DataSetArgument();

      // Parameters required by the query.
      lArgs.add( "aAssmblClassDbId", 4650 );
      lArgs.add( "aAssmblClassCd", "ENGINE-1" );

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
      MxAssert.assertEquals( OPERATING_PARAMETER, aResult.getString( "operating_parameter" ) );

      // mim_data_type.data_type_db_id || ':' || mim_data_type.data_type_id
      MxAssert.assertEquals( DATA_TYPE_KEY, aResult.getString( "data_type_key" ) );

      // Determine if one row is returned.
      MxAssert.assertEquals( "Number of retrieved rows", 1, aResult.getRowCount() );
   }
}
