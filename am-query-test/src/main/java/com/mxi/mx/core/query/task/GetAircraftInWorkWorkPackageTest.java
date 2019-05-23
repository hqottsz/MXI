
package com.mxi.mx.core.query.task;

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
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * Retrieves In work work package against a given inventory aircraft.
 *
 * @author gliang
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetAircraftInWorkWorkPackageTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetAircraftInWorkWorkPackageTest.class );
   }


   /**
    * Tests the retrieval of In work work package against a given inventory aircraft.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testGetAircraftInWorkWorkPackage() throws Exception {

      DataSet lDataSet = this.execute( new InventoryKey( 4650, 200001 ) );

      // There is only one IN WORK work package in the testing dataset.
      MxAssert.assertEquals( "count", 1, lDataSet.getRowCount() );
      MxAssert.assertEquals( "sched_db_id", 4650, lDataSet.getIntAt( 1, "sched_db_id" ) );
      MxAssert.assertEquals( "sched_id", 100001, lDataSet.getIntAt( 1, "sched_id" ) );
   }


   /**
    * This method executes the query in GetAircraftInWorkWorkPackage.qrx
    *
    * @param aInvKey
    *           the InventoryKey object
    *
    * @return The dataset after execution.
    */
   private DataSet execute( InventoryKey aInvKey ) {
      DataSetArgument lDataSetArgument = new DataSetArgument();
      lDataSetArgument.add( "aInvNoDbId", aInvKey.getDbId() );
      lDataSetArgument.add( "aInvNoId", aInvKey.getId() );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lDataSetArgument );
   }
}
