
package com.mxi.mx.web.query.task;

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
 * This class tests the query com.mxi.mx.web.query.task.UsageParmsForInventory.qrx
 *
 * @author bparekh
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class UsageParmsForInventoryTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), UsageParmsForInventoryTest.class );
   }


   /**
    * Tests the retrieval of usage parms for inventory.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testCalendarParametersForDeadlines() throws Exception {

      int lInvNoDbId = 4650;
      int lInvNoId = 285255;

      DataSet lDataSet = this.execute( lInvNoDbId, lInvNoId );

      while ( lDataSet.next() ) {

         MxAssert.assertEquals( "tsn_qt", 100, lDataSet.getInt( "tsn_qt" ) );
      }
   }


   /**
    * This method executes the query in UsageParmsForInventory.qrx
    *
    * @param aInvNoDbId
    *           The inv_no_db_id field
    * @param aInvNoId
    *           The inv_no_id field
    *
    * @return The dataset after execution.
    */
   private DataSet execute( int aInvNoDbId, int aInvNoId ) {
      DataSetArgument lDataSetArgument = new DataSetArgument();
      lDataSetArgument.add( "aInventoryDbId", aInvNoDbId );
      lDataSetArgument.add( "aInventoryId", aInvNoId );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lDataSetArgument );
   }
}
