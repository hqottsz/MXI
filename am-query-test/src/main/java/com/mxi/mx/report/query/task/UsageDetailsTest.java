
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
 * This class tests the query com.mxi.mx.report.UsageDetails.qrx
 *
 * @author krangaswamy
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class UsageDetailsTest {

   @ClassRule
   public static final DatabaseConnectionRule sDatabaseConnectionRule =
         new DatabaseConnectionRule();


   @BeforeClass
   public static void setUp() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), UsageDetailsTest.class );
   }


   /**
    * Tests the retrieval of UsageDetails.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testUsageDetails() throws Exception {

      int lTaskDbId = 4650;
      int lTaskId = 134101;

      QuerySet lQs = this.execute( new TaskKey( lTaskDbId, lTaskId ) );

      // Determine if the following are returned :
      Assert.assertTrue( lQs.next() );
      Assert.assertEquals( "4650", lQs.getString( "event_db_id" ) );
      Assert.assertEquals( "134101", lQs.getString( "event_id" ) );
      Assert.assertEquals( "COMPLETE", lQs.getString( "event_status_cd" ) );
      Assert.assertEquals( "CHECK", lQs.getString( "task_class_cd" ) );
      Assert.assertEquals( "G (h)", lQs.getString( "event_sdesc" ) );
      Assert.assertEquals( "ASSY", lQs.getString( "inv_class_cd" ) );
      Assert.assertEquals( "(1.LT) CFM56-3C (PN: CFM56-3C, SN: XXX)",
            lQs.getString( "inv_no_sdesc" ) );
      Assert.assertEquals( "TEST", lQs.getString( "assmbl_bom_cd" ) );
      Assert.assertEquals( "5", lQs.getString( "afh_tsn" ) );
      Assert.assertEquals( "2", lQs.getString( "afh_tso" ) );
      Assert.assertEquals( "5", lQs.getString( "cycles_tsn" ) );
      Assert.assertEquals( "6", lQs.getString( "cycles_tso" ) );

      Assert.assertFalse( lQs.next() );
   }


   /**
    * This method executes the query in UsageDetailsTest.qrx
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
