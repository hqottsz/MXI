package com.mxi.mx.core.query.task.steps;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.XmlLoader;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.core.key.SchedStepKey;
import com.mxi.mx.core.task.model.StepStatus;


/**
 * This class performs unit testing on the query file with the same package and name.
 */

public final class GetLatestUserJobCardStepApplicabilityLogTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   // ~ Static fields/initializers
   // ------------------------------------------------------------------

   public static final SchedStepKey STEP1_KEY = new SchedStepKey( 4650, 101, 1 );
   public static final SchedStepKey STEP2_KEY = new SchedStepKey( 4650, 101, 2 );
   public static final SchedStepKey STEP3_KEY = new SchedStepKey( 4650, 101, 3 );


   /**
    * Tests that the query returns one row when the last modification is a user
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void userModifiedApplicabilityLogLast() throws Exception {
      DataSet lResult = execute( STEP1_KEY, StepStatus.MXNA );
      assertEquals( 1, lResult.getRowCount() );
   }


   /**
    * Tests that the query returns no rows when the last modification is not a user
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void systemModifiedApplicabilityLogLast() throws Exception {
      DataSet lResult = execute( STEP2_KEY, StepStatus.MXNA );
      assertEquals( 0, lResult.getRowCount() );
   }


   /**
    * Tests that the query returns no rows when there are no entries
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void noApplicabilityLog() throws Exception {
      DataSet lResult = execute( STEP3_KEY, StepStatus.MXNA );
      assertEquals( 0, lResult.getRowCount() );
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
            GetLatestUserJobCardStepApplicabilityLogTest.class,
            "GetLatestUserJobCardStepApplicabilityLogTest.xml" );
   }


   /**
    * Execute the query.
    *
    * @return dataSet result.
    */
   private DataSet execute( SchedStepKey aStep, StepStatus aStatus ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aStepStatusCd", aStatus.toString() );
      lArgs.add( aStep, "aSchedDbId", "aSchedId", "aStepId" );

      // Execute the query
      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
