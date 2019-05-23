
package com.mxi.mx.web.query.warranty;

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
import com.mxi.mx.core.key.WarrantyContractKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class performs unit testing on the NonRepairableMaterials query file with the same package
 * and name.
 *
 * @author akash
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class NonRepairableMaterialsTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), NonRepairableMaterialsTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test Results of NonRepairableMaterials query
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testNonRepairableMaterials() throws Exception {
      execute( new WarrantyContractKey( "4650:1" ) );

      assertEquals( 1, iDataSet.getRowCount() );

      iDataSet.next();

      MxAssert.assertEquals( "4650:2", iDataSet.getString( "part_no_key" ) );
      MxAssert.assertEquals( "TEST PART OEM", iDataSet.getString( "part_no_oem" ) );
      MxAssert.assertEquals( "MANUFACT (Test Name)", iDataSet.getString( "manufacturer_name" ) );
      MxAssert.assertEquals( "0:MANUFACT", iDataSet.getString( "manufacturer_key" ) );
      MxAssert.assertEquals( "ACTV", iDataSet.getString( "status" ) );
   }


   /**
    * Execute the query.
    *
    * @param aWarrantyContract
    *           Warranty Contract Key
    */
   private void execute( WarrantyContractKey aWarrantyContract ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aWarrantyContract, new String[] { "aWarrantyDefnDbId", "aWarrantyDefnId" } );

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
