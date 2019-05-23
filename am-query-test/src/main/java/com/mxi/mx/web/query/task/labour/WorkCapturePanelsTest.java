
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
public final class WorkCapturePanelsTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   public static final TaskKey TASK_KEY = new TaskKey( 4650, 101 );

   public static final SchedLabourKey SCHED_LABOUR_KEY = new SchedLabourKey( 4650, 200 );


   /**
    * Tests that the query returns task labour panels
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQuery() throws Exception {
      DataSet lResult = execute();
      lResult.addSort( "dsString(sched_panel_key)", true );

      // asserts that 2 rows are returned
      assertEquals( 2, lResult.getRowCount() );

      lResult.first();

      // step signed off by technician
      MxAssert.assertEquals( "4650:101:1", lResult.getString( "sched_panel_key" ) );
      MxAssert.assertEquals( "Panel 1 (Panel 1 sdesc)", lResult.getString( "panel_cd" ) );
      MxAssert.assertEquals( "4650:1", lResult.getString( "panel_key" ) );
      MxAssert.assertEquals( "TECH, technician", lResult.getString( "signed_by" ) );
      MxAssert.assertEquals( true, lResult.getBoolean( "previously_captured_bool" ) );

      lResult.next();

      // step has no signoff
      MxAssert.assertEquals( "4650:101:2", lResult.getString( "sched_panel_key" ) );
      MxAssert.assertEquals( "Panel 2 (Panel 2 sdesc)", lResult.getString( "panel_cd" ) );
      MxAssert.assertEquals( "4650:2", lResult.getString( "panel_key" ) );
      MxAssert.assertNull( lResult.getString( "signed_by" ) );
      MxAssert.assertEquals( false, lResult.getBoolean( "previously_captured_bool" ) );
   }


   /**
    * create the test data
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @BeforeClass
   public static void loadData() throws Exception {
      XmlLoader.load( sDatabaseConnectionRule.getConnection(), WorkCapturePanelsTest.class,
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
