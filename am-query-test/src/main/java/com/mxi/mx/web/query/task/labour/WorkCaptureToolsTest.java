
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
public final class WorkCaptureToolsTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   public static final TaskKey TASK_KEY = new TaskKey( 4650, 101 );

   public static final SchedLabourKey SCHED_LABOUR_KEY = new SchedLabourKey( 4650, 200 );


   /**
    * Tests that the query returns task labour tools
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQuery() throws Exception {
      DataSet lResult = execute();
      lResult.addSort( "dsString(tool_key)", true );

      // asserts that 2 rows are returned
      assertEquals( 2, lResult.getRowCount() );

      lResult.first();

      // step signed off by technician
      MxAssert.assertEquals( "4650:101:1", lResult.getString( "tool_key" ) );
      MxAssert.assertEquals( 17.0, lResult.getDouble( "tool_hr" ) );
      MxAssert.assertEquals( "TOOL6000 (Tool Test 1)", lResult.getString( "tool_description" ) );
      MxAssert.assertEquals( "4650:4000", lResult.getString( "inventory_key" ) );
      MxAssert.assertEquals( "INV4000", lResult.getString( "serial_no_oem" ) );
      MxAssert.assertEquals( "4650:5000", lResult.getString( "part_no_key" ) );
      MxAssert.assertEquals( "PART5000", lResult.getString( "part_no_oem" ) );
      MxAssert.assertEquals( "TECH, technician", lResult.getString( "signed_by" ) );
      MxAssert.assertEquals( true, lResult.getBoolean( "previously_captured_bool" ) );

      lResult.next();

      // step has no signoff
      MxAssert.assertEquals( "4650:101:2", lResult.getString( "tool_key" ) );
      MxAssert.assertEquals( 20.0, lResult.getDouble( "tool_hr" ) );
      MxAssert.assertEquals( "TOOL6001 (Tool Test 2)", lResult.getString( "tool_description" ) );
      MxAssert.assertEquals( "4650:4001", lResult.getString( "inventory_key" ) );
      MxAssert.assertEquals( "INV4001", lResult.getString( "serial_no_oem" ) );
      MxAssert.assertEquals( "4650:5001", lResult.getString( "part_no_key" ) );
      MxAssert.assertEquals( "PART5001", lResult.getString( "part_no_oem" ) );
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
      XmlLoader.load( sDatabaseConnectionRule.getConnection(), WorkCaptureToolsTest.class,
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
