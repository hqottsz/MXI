
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
 * This class tests the query com.mxi.mx.web.query.part.ExternalPartRequirementsByTask.qrx
 *
 * @author krangaswamy
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class ExternalPartRequirementsByTaskTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            ExternalPartRequirementsByTaskTest.class );
   }


   /**
    * Tests the retrieval of externally controlled parts.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testExternallyControlledPartsByTask() throws Exception {

      int lTaskDbId = 4650;
      int lTaskId = 138200;

      DataSet lDataSet = this.execute( new TaskKey( lTaskDbId, lTaskId ) );

      while ( lDataSet.next() ) {

         // Determine if the following are returned :
         MxAssert.assertEquals( "1:4650:138200", lDataSet.getString( "sched_ext_part_key" ) );
         MxAssert.assertEquals( "4650:138200", lDataSet.getString( "task_key" ) );
         MxAssert.assertEquals( "extpart1", lDataSet.getString( "sched_sdesc" ) );
         MxAssert.assertEquals( "111222", lDataSet.getString( "rmv_part_no" ) );
         MxAssert.assertEquals( "111333", lDataSet.getString( "rmv_serial_batch_no" ) );
         MxAssert.assertEquals( "1.0", lDataSet.getString( "rmv_qt" ) );
         MxAssert.assertEquals( "222111", lDataSet.getString( "inst_part_no" ) );
         MxAssert.assertEquals( "333111", lDataSet.getString( "inst_serial_batch_no" ) );
         MxAssert.assertEquals( "1.0", lDataSet.getString( "inst_qt" ) );
         MxAssert.assertEquals( "servicetagdesc1", lDataSet.getString( "inst_service_tag_sdesc" ) );
         MxAssert.assertEquals( "notes1", lDataSet.getString( "notes" ) );
         MxAssert.assertEquals( "Jayakumar, Karthik", lDataSet.getString( "signed_by" ) );
      }
   }


   /**
    * This method executes the query in ExternalPartRequirementsByTask.qrx
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
