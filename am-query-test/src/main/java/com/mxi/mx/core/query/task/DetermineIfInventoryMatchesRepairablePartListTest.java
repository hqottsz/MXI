
package com.mxi.mx.core.query.task;

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
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.TaskKey;


/**
 * This class tests the query
 * com.mxi.mx.core.query.task.DetermineIfInventoryMatchesRepairablePartList.qrx
 *
 * @author srengasamy
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class DetermineIfInventoryMatchesRepairablePartListTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            DetermineIfInventoryMatchesRepairablePartListTest.class );
   }


   /**
    * Tests the retrieval of DetermineIfInventoryMatchesRepairablePartList.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testDetermineIfInventoryMatchesRepairablePartList() throws Exception {

      int lSchedDbId = 4650;
      int lSchedId = 136987;
      int lLocDbId = 4650;
      int lLocId = 1000007;

      DataSet lDataSet = this.execute( new TaskKey( lSchedDbId, lSchedId ),
            new LocationKey( lLocDbId, lLocId ) );

      // TEST: Confirm the Data had no results
      assertEquals( 1, lDataSet.getRowCount() );
   }


   /**
    * This method executes the query in DetermineIfInventoryMatchesRepairablePartList.qrx
    *
    * @param aTaskKey
    *           The task key.
    * @param aLocationKey
    *           The Location key.
    *
    * @return The dataset after execution.
    */
   private DataSet execute( TaskKey aTaskKey, LocationKey aLocationKey ) {
      DataSetArgument lDataSetArgument = new DataSetArgument();
      lDataSetArgument.add( "aSchedDbId", aTaskKey.getDbId() );
      lDataSetArgument.add( "aSchedId", aTaskKey.getId() );
      lDataSetArgument.add( "aLocDbId", aLocationKey.getDbId() );
      lDataSetArgument.add( "aLocId", aLocationKey.getId() );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lDataSetArgument );
   }
}
