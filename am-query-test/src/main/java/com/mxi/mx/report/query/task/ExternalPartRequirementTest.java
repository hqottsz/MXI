
package com.mxi.mx.report.query.task;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.core.key.TaskKey;


/**
 * This class tests the query com.mxi.mx.web.report.ExternalPartRequirement.qrx
 *
 * @author krangaswamy
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class ExternalPartRequirementTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void setUp() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            ExternalPartRequirementTest.class );
   }


   /**
    * Tests the retrieval of externally controlled parts.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testExternallyControlledPartRequirement() throws Exception {

      QuerySet lQs = this.execute( new TaskKey( 4650, 138200 ) );

      Assert.assertTrue( lQs.next() );

      // Determine if the following are returned :
      Assert.assertEquals( "1:4650:138200", lQs.getString( "sched_ext_part_key" ) );

      // MxAssert.assertEquals( "4650:138200", lDataSet.getString( "task_key" ) );
      Assert.assertEquals( "extpart1", lQs.getString( "sched_sdesc" ) );

      Assert.assertEquals( "111222 / 111333", lQs.getString( "part_serial_off" ) );

      // MxAssert.assertEquals( "111333", lDataSet.getString( "rmv_serial_batch_no" ) );
      Assert.assertEquals( 1, lQs.getInt( "rmv_qt" ) );
      Assert.assertEquals( "222111 / 333111", lQs.getString( "part_serial_on" ) );

      // MxAssert.assertEquals( "333111", lDataSet.getString( "inst_serial_batch_no" ) );
      Assert.assertEquals( 1, lQs.getInt( "inst_qt" ) );
      Assert.assertEquals( "servicetagdesc1", lQs.getString( "inst_service_tag_sdesc" ) );
      Assert.assertEquals( "notes1", lQs.getString( "notes" ) );

      Assert.assertFalse( lQs.next() );
   }


   /**
    * This method executes the query in ExternalPartRequirementsByTask.qrx
    *
    * @param aTaskKey
    *           the TaskKey object
    *
    * @return The dataset after execution.
    */
   private QuerySet execute( TaskKey aTaskKey ) {
      DataSetArgument lDataSetArgument = new DataSetArgument();
      lDataSetArgument.add( aTaskKey, "aTaskDbId", "aTaskId" );

      return QueryExecutor.executeQuery( getClass(), lDataSetArgument );
   }
}
