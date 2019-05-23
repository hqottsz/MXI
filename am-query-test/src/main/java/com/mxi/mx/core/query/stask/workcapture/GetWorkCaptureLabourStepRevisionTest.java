package com.mxi.mx.core.query.stask.workcapture;

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
import com.mxi.mx.core.key.SchedStepKey;
import com.mxi.mx.core.unittest.MxAssert;
import com.mxi.mx.web.query.task.labour.GetWorkCaptureStepsTest;


/**
 * This class performs unit testing on the query file with the same package and name.
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetWorkCaptureLabourStepRevisionTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   public static final SchedStepKey SCHED_STEP_LABOUR_KEY = new SchedStepKey( 4650, 101, 1 );

   public static final SchedStepKey SCHED_STEP_NO_LABOUR_KEY = new SchedStepKey( 4650, 101, 2 );


   /**
    * Tests that correct revision count is found for tasks with labour step rows. Note that triggers
    * are not enabled so the revision number per row is always 1
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testSchedStepWithLabour() throws Exception {
      DataSet lResult = execute( SCHED_STEP_LABOUR_KEY );

      // asserts that 1 row is returned
      assertEquals( 1, lResult.getRowCount() );

      lResult.first();

      // asserts that the total revision (sched_step + sched_labour_step) is 3
      MxAssert.assertEquals( 3, lResult.getInt( "revision_no" ) );
   }


   /**
    * Tests that correct revision count is found for tasks with no labour step rows (only 1
    * sched_step row). Note that triggers are not enabled so the revision number per row is always 1
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testSchedStepWithNoLabour() throws Exception {
      DataSet lResult = execute( SCHED_STEP_NO_LABOUR_KEY );

      // asserts that 1 row is returned
      assertEquals( 1, lResult.getRowCount() );

      lResult.first();

      // asserts that the total revision (sched_step + sched_labour_step) is 1
      MxAssert.assertEquals( 1, lResult.getInt( "revision_no" ) );
   }


   /**
    * create the test data
    *
    * @throws Exception
    *            if an error occurs
    */
   @BeforeClass
   public static void loadData() throws Exception {
      XmlLoader.load( sDatabaseConnectionRule.getConnection(), GetWorkCaptureStepsTest.class,
            "GetWorkCaptureLabourStepRevisionTest.xml" );
   }


   /**
    * Execute the query.
    *
    * @return dataSet result.
    */
   private DataSet execute( SchedStepKey aSchedStepKey ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aSchedStepKey, "aSchedDbId", "aSchedId", "aStepId" );

      // Execute the query
      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
