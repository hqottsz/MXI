
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
import com.mxi.mx.core.task.model.StepStatus;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class performs unit testing on the query file with the same package and name.
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetWorkCaptureStepsWithHighlightTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   public static final TaskKey TASK_KEY = new TaskKey( 4650, 101 );

   public static final SchedLabourKey SCHED_LABOUR_KEY = new SchedLabourKey( 4650, 200 );


   /**
    * Tests that the query returns task labour steps
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQuery() throws Exception {
      DataSet lResult = execute();

      // asserts that 4 rows are returned
      assertEquals( 4, lResult.getRowCount() );

      lResult.first();

      // step signed off by technician
      MxAssert.assertEquals( "4650:101:1", lResult.getString( "sched_step_key" ) );
      MxAssert.assertEquals( Integer.valueOf( 1 ), lResult.getInteger( "step_ord" ) );
      MxAssert.assertEquals( StepStatus.MXPARTIAL.getDisplayName(),
            lResult.getString( "step_status" ) );
      MxAssert.assertEquals( "Job Card Step 1", lResult.getString( "step_ldesc" ) );
      MxAssert.assertEquals( Integer.valueOf( 1 ), lResult.getInteger( "labour_step_ord" ) );
      MxAssert.assertEquals( "1. TECH, technician", lResult.getString( "signed_by" ) );
      MxAssert.assertEquals( "Complete", lResult.getString( "labour_step_status" ) );
      MxAssert.assertEquals( "labour_step_note 1", lResult.getString( "labour_step_notes" ) );

      // a labour (labour_step_note 2) in this sched_step is selected, so this SCHED step is
      // highlighted
      MxAssert.assertEquals( true, lResult.getBoolean( "step_highlight_bool" ) );

      // this labour is selected, so it's LABOUR step is highlighted
      MxAssert.assertEquals( true, lResult.getBoolean( "labour_step_highlight_bool" ) );

      lResult.next();

      // step signed off by hitechnician
      MxAssert.assertEquals( "4650:101:1", lResult.getString( "sched_step_key" ) );
      MxAssert.assertEquals( Integer.valueOf( 1 ), lResult.getInteger( "step_ord" ) );
      MxAssert.assertEquals( StepStatus.MXPARTIAL.getDisplayName(),
            lResult.getString( "step_status" ) );
      MxAssert.assertEquals( "Job Card Step 1", lResult.getString( "step_ldesc" ) );
      MxAssert.assertEquals( Integer.valueOf( 2 ), lResult.getInteger( "labour_step_ord" ) );
      MxAssert.assertEquals( "2. HITECH, hitechnician", lResult.getString( "signed_by" ) );
      MxAssert.assertEquals( "Partial", lResult.getString( "labour_step_status" ) );
      MxAssert.assertEquals( "labour_step_note 2", lResult.getString( "labour_step_notes" ) );

      // a labour (labour_step_note 2) in this sched_step is selected, so this SCHED step is
      // highlighted
      MxAssert.assertEquals( true, lResult.getBoolean( "step_highlight_bool" ) );

      // this labour is not selected, so it's LABOUR step is not highlighted
      MxAssert.assertEquals( false, lResult.getBoolean( "labour_step_highlight_bool" ) );

      lResult.next();

      // step not signed off
      MxAssert.assertEquals( "4650:101:2", lResult.getString( "sched_step_key" ) );
      MxAssert.assertEquals( Integer.valueOf( 2 ), lResult.getInteger( "step_ord" ) );
      MxAssert.assertEquals( StepStatus.MXPENDING.getDisplayName(),
            lResult.getString( "step_status" ) );
      MxAssert.assertEquals( "Job Card Step 2", lResult.getString( "step_ldesc" ) );
      MxAssert.assertNull( lResult.getString( "signed_by" ) );
      MxAssert.assertNull( lResult.getString( "labour_step_notes" ) );

      // this step is not signed off, so this SCHED step is not highlighted
      MxAssert.assertEquals( false, lResult.getBoolean( "step_highlight_bool" ) );

      // this step is not signed off, so this boolean is false
      MxAssert.assertEquals( false, lResult.getBoolean( "labour_step_highlight_bool" ) );

      lResult.next();

      // This step marked NA by step applicability processing
      MxAssert.assertEquals( "4650:101:3", lResult.getString( "sched_step_key" ) );
      MxAssert.assertEquals( Integer.valueOf( 3 ), lResult.getInteger( "step_ord" ) );
      MxAssert.assertEquals( StepStatus.MXNA.getDisplayName(), lResult.getString( "step_status" ) );
      MxAssert.assertEquals( "Job Card Step 3", lResult.getString( "step_ldesc" ) );
      // The applicability note comes from the sched_step_appl_log table which doesn't have a
      // labour_step_ord
      MxAssert.assertNull( lResult.getInteger( "labour_step_ord" ) );
      MxAssert.assertEquals( "1. TECH, technician", lResult.getString( "signed_by" ) );
      MxAssert.assertEquals( "N/A", lResult.getString( "labour_step_status" ) );
      MxAssert.assertEquals( "task marked not applicable due to step applicability",
            lResult.getString( "labour_step_notes" ) );

      // No labour activity on this step so not highlighted
      MxAssert.assertEquals( false, lResult.getBoolean( "step_highlight_bool" ) );
      MxAssert.assertEquals( false, lResult.getBoolean( "labour_step_highlight_bool" ) );
   }


   /**
    * create the test data
    *
    * @throws Exception
    *            if an error occurs
    */
   @BeforeClass
   public static void loadData() throws Exception {
      XmlLoader.load( sDatabaseConnectionRule.getConnection(),
            GetWorkCaptureStepsWithHighlightTest.class, WorkCaptureData.getDataFile() );
   }


   /**
    * Execute the query.
    *
    * @return dataSet result.
    */
   private DataSet execute() {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( TASK_KEY, "aTaskDbId", "aTaskId" );
      lArgs.addWhereIn(
            new String[] { "sched_labour_step.labour_db_id", "sched_labour_step.labour_id" },
            SCHED_LABOUR_KEY );

      // Execute the query
      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
