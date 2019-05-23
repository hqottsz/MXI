
package com.mxi.mx.web.query.task.labour;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.XmlLoader;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.core.key.SchedLabourKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class performs unit testing on the query file with the same package and name.
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class WorkCaptureMeasurementsTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   /** The task key */
   public static final TaskKey TASK_KEY = new TaskKey( 4650, 101 );
   public static final SchedLabourKey SCHED_LABOUR_KEY = new SchedLabourKey( 4650, 200 );


   /**
    * Tests that the query returns task labour measurements
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQuery() throws Exception {
      DataSet lResult = execute();
      lResult.addSort( "inv_parm_data_key", true );

      // asserts that 2 rows are returned
      assertEquals( 2, lResult.getRowCount() );

      lResult.first();

      // step signed off by technician
      MxAssert.assertEquals( "4650:101:1:0:1", lResult.getString( "inv_parm_data_key" ) );
      MxAssert.assertEquals( "HOURS (Flying Hours)", lResult.getString( "parameter" ) );
      MxAssert.assertEquals( 50.0, lResult.getDouble( "parm_qt" ) );
      MxAssert.assertEquals( "TECH, technician", lResult.getString( "signed_by" ) );
      MxAssert.assertEquals( "1", lResult.getString( "data_ord" ) );

      lResult.next();

      // step has no signoff
      MxAssert.assertEquals( "4650:101:2:0:21", lResult.getString( "inv_parm_data_key" ) );
      MxAssert.assertEquals( "CDY (Calendar Day)", lResult.getString( "parameter" ) );
      MxAssert.assertEquals( 70.0, lResult.getDouble( "parm_qt" ) );
      MxAssert.assertNull( lResult.getString( "signed_by" ) );
      MxAssert.assertEquals( "2", lResult.getString( "data_ord" ) );
   }


   /**
    * create the test data
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @BeforeClass
   public static void loadData() throws Exception {
      XmlLoader.load( sDatabaseConnectionRule.getConnection(), WorkCaptureMeasurementsTest.class,
            WorkCaptureData.getDataFile() );
   }


   /**
    * Execute the query.
    *
    * @return dataSet result.
    */
   private DataSet execute() {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( TASK_KEY, "aSchedDbId", "aSchedId" );
      lArgs.add( SCHED_LABOUR_KEY, "aLabourDbId", "aLabourId" );

      // Execute the query
      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
