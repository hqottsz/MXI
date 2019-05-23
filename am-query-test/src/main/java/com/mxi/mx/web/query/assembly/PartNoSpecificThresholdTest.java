
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
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class tests the query com.mxi.mx.web.query.assembly.PartNoSpecificThreshold.qrx
 *
 * @author bparekh
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class PartNoSpecificThresholdTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            PartNoSpecificThresholdTest.class );
   }


   /**
    * Tests the retrieval of the part no specific thresholds.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testGetPartNoSpecificThresholds() throws Exception {

      DataSet lDataSet = execute( new AssemblyKey( 4650, "101" ) );

      while ( lDataSet.next() ) {
         MxAssert.assertEquals( "threshold_id", "100029", lDataSet.getInteger( "threshold_id" ) );

         MxAssert.assertEquals( "part_no_oem", "JSDF-ME333A", lDataSet.getString( "part_no_oem" ) );

         MxAssert.assertEquals( "oil_status_cd", "CAUTION", lDataSet.getString( "oil_status_cd" ) );
      }
   }


   /**
    * This method executes the query in PartNoSpecificThreshold.qrx
    *
    * @param aAssemblyKey
    *           The assembly key value.
    *
    * @return The dataset after execution.
    */
   private DataSet execute( AssemblyKey aAssemblyKey ) {
      DataSetArgument lDataSetArgument = new DataSetArgument();

      lDataSetArgument.add( "aAssemblyDbId", aAssemblyKey.getDbId() );
      lDataSetArgument.add( "aAssemblyCode", aAssemblyKey.getCd() );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lDataSetArgument );
   }
}
