
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
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class tests the query com.mxi.mx.web.query.task.GetLocationForWorkPackage.qrx
 *
 * @author bparekh
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetLocationForWorkPackageTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetLocationForWorkPackageTest.class );
   }


   /**
    * Tests the retrieval of location for a work package.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testLocationForWorkPackage() throws Exception {

      int lDbId = 4650;
      int lId = 136965;

      DataSet lDataSet = this.execute( new TaskKey( lDbId, lId ) );

      while ( lDataSet.next() ) {

         MxAssert.assertEquals( "location_key", "4650:100026",
               lDataSet.getString( "location_key" ) );
      }
   }


   /**
    * This method executes the query in GetLocationForWorkPackage.qrx
    *
    * @param aTaskKey
    *           the TaskKey object
    *
    * @return The dataset after execution.
    */
   private DataSet execute( TaskKey aTaskKey ) {
      DataSetArgument lDataSetArgument = new DataSetArgument();

      lDataSetArgument.add( "aSchedDbId", aTaskKey.getDbId() );
      lDataSetArgument.add( "aSchedId", aTaskKey.getId() );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lDataSetArgument );
   }
}
