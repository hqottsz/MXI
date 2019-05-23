
package com.mxi.mx.core.query.task;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.table.sched.SchedStaskTable;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class performs unit testing on the query file with the same package and name.
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class FindIssueToAccountGivenTaskTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            FindIssueToAccountGivenTaskTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;

   /** DOCUMENT ME! */
   private final TaskKey iParentTask = new TaskKey( 4650, 1001 );

   /** DOCUMENT ME! */
   private final TaskKey iTask = new TaskKey( 4650, 10001 );

   /** DOCUMENT ME! */
   private final TaskKey iWorkPackage = new TaskKey( 4650, 101 );


   /**
    * Test the case where given task do not have issue account set up.<br>
    * Only parent task and work package have issue accounts.<br>
    * Query should return two rows, with first row the parent task.
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testIssueAccountFoundOnParentTask() throws Exception {

      // Clear out task issue account
      SchedStaskTable lSchedStask = SchedStaskTable.findByPrimaryKey( iTask );
      lSchedStask.setIssueAccount( null );
      lSchedStask.update();

      // ACTION: Execute the Query
      execute( 4650, 10001 );

      // Assert number of rows returned
      assertEquals( 2, iDataSet.getRowCount() );
      iDataSet.next();

      // TEST: Confirm issue account return is of expected
      testRow( iParentTask, new FncAccountKey( 4650, 102 ) );

      // TEARDOWN: Undo the Setup Changes
      lSchedStask.setIssueAccount( new FncAccountKey( 4650, 103 ) );
      lSchedStask.update();
   }


   /**
    * Test the case where given task, parent task, and work package have issue accounts set.<br>
    * Query should return 3 rows, given task the first record returned.
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testIssueAccountFoundOnTask() throws Exception {

      // ACTION: Execute the Query
      execute( 4650, 10001 );

      // Assert number of rows returned
      assertEquals( 3, iDataSet.getRowCount() );
      iDataSet.next();

      // TEST: Confirm issue account return is of expected
      testRow( iTask, new FncAccountKey( 4650, 103 ) );
   }


   /**
    * Test the case where only work package task has issue account specified.<br>
    * Query should return only one row.
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testIssueAccountFoundOnWorkPackage() throws Exception {

      // Clear out task and parent task issue account
      SchedStaskTable lSchedStask = SchedStaskTable.findByPrimaryKey( iTask );
      lSchedStask.setIssueAccount( null );
      lSchedStask.update();

      SchedStaskTable lParentStask = SchedStaskTable.findByPrimaryKey( iParentTask );
      lParentStask.setIssueAccount( null );
      lParentStask.update();

      // ACTION: Execute the Query
      execute( 4650, 10001 );

      // Assert number of rows returned
      assertEquals( 1, iDataSet.getRowCount() );
      iDataSet.next();

      // TEST: Confirm the Data had no results
      testRow( iWorkPackage, new FncAccountKey( 4650, 101 ) );

      // TEARDOWN: Undo the Setup Changes
      lSchedStask.setIssueAccount( new FncAccountKey( 4650, 103 ) );
      lSchedStask.update();

      lParentStask.setIssueAccount( new FncAccountKey( 4650, 102 ) );
      lParentStask.update();
   }


   /**
    * Test the case where given task, ancestor tasks, and work package do not have<br>
    * issue accounts set up. Query should not return any rows.<br>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testNoIssueAccountFound() throws Exception {

      // Clear out all issue accounts
      SchedStaskTable lSchedStask = SchedStaskTable.findByPrimaryKey( iTask );
      lSchedStask.setIssueAccount( null );
      lSchedStask.update();

      SchedStaskTable lParent = SchedStaskTable.findByPrimaryKey( iParentTask );
      lParent.setIssueAccount( null );
      lParent.update();

      SchedStaskTable lWorkPackage = SchedStaskTable.findByPrimaryKey( iWorkPackage );
      lWorkPackage.setIssueAccount( null );
      lWorkPackage.update();

      // ACTION: Execute the Query
      execute( 4650, 10001 );

      // TEST: Confirm the Data had no results
      assertEquals( 0, iDataSet.getRowCount() );

      // TEARDOWN: Undo the Setup Changes
      lSchedStask.setIssueAccount( new FncAccountKey( 4650, 103 ) );
      lSchedStask.update();

      lParent.setIssueAccount( new FncAccountKey( 4650, 102 ) );
      lParent.update();

      lWorkPackage.setIssueAccount( new FncAccountKey( 4650, 101 ) );
      lWorkPackage.update();
   }


   /**
    * Execute the query.
    *
    * @param aTaskDbId
    *           db id of task
    * @param aTaskId
    *           id of task
    */
   private void execute( Integer aTaskDbId, Integer aTaskId ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aTaskDbId", aTaskDbId );
      lArgs.add( "aTaskId", aTaskId );
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            "com.mxi.mx.core.query.task.findIssueToAccountGivenTask", lArgs );
   }


   /**
    * Test row contents returned
    *
    * @param aTask
    *           task key
    * @param aIssueAccount
    *           issue account key
    */
   private void testRow( TaskKey aTask, FncAccountKey aIssueAccount ) {
      MxAssert.assertEquals( aTask.toString(), iDataSet.getString( "task" ) );
      MxAssert.assertEquals( aIssueAccount.toString(), iDataSet.getString( "issue_account" ) );
   }
}
