
package com.mxi.mx.web.query.part;

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
 * This class tests the query com.mxi.mx.web.query.part.ExternallyControlledPartRequirements.qrx
 *
 * @author bparekh
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class ExternallyControlledPartRequirementsTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            ExternallyControlledPartRequirementsTest.class );
   }


   /**
    * Tests the retrieval externally controlled parts.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testExternallyControlledPartsCount() throws Exception {

      int lTaskDbId = 4650;
      int lTaskId = 138200;

      DataSet lDataSet = this.execute( new TaskKey( lTaskDbId, lTaskId ) );

      while ( lDataSet.next() ) {

         MxAssert.assertEquals( "sched_sdesc", "extpart1", lDataSet.getString( "sched_sdesc" ) );
         MxAssert.assertEquals( "inst_service_tag_sdesc", "servicetagdesc1",
               lDataSet.getString( "inst_service_tag_sdesc" ) );
      }
   }


   /**
    * This method executes the query in ExternallyControlledPartRequirements.qrx
    *
    * @param aTaskKey
    *           the TaskKey object
    *
    * @return The dataset after execution.
    */
   private DataSet execute( TaskKey aTaskKey ) {
      DataSetArgument lDataSetArgument = new DataSetArgument();
      lDataSetArgument.add( "aTaskDbId", aTaskKey.getDbId() );
      lDataSetArgument.add( "aTaskId", aTaskKey.getId() );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lDataSetArgument );
   }
}
