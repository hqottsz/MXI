
package com.mxi.mx.core.query.task;

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
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * Ensures that <code>validateReleaseNoDeadlineNsvTasks</code> query works
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class ValidateReleaseNoDeadlineNsvTasksTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            ValidateReleaseNoDeadlineNsvTasksTest.class );
   }


   private static final InventoryKey OFFWING_INVENTORY1 = new InventoryKey( 4650, 1 );
   private static final InventoryKey OFFWING_INVENTORY2 = new InventoryKey( 4650, 3 );
   private static final InventoryKey OFFWING_INVENTORY3 = new InventoryKey( 4650, 5 );
   private static final TaskKey NSV_TASK1 = new TaskKey( 4650, 1 );
   private static final TaskKey NSV_TASK2 = new TaskKey( 4650, 8 );


   /**
    * TEST CASE 2: There is a loose active NSV task outside a work package with completed NSV task.
    * There should be no warning there when clicking the button 'Release Preview' inside the work
    * package in this case.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testNoWarningWithLooseActiveNsvTaskOutsideWP() throws Exception {
      DataSet lDataSet;

      // Passed in inventory
      lDataSet = execute( OFFWING_INVENTORY2 );

      MxAssert.assertEquals( 0, lDataSet.getRowCount() );
   }


   /**
    * TEST CASE 1: There is an active NSV task bundled with a work package. There should be a
    * warning there when clicking the button 'Release Preview' inside the work package in this case.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testWarningWithActiveNsvTaskInsideWP() throws Exception {
      DataSet lDataSet;

      // Passed in inventory
      lDataSet = execute( OFFWING_INVENTORY1 );

      MxAssert.assertTrue( lDataSet.next() );

      assertRow( lDataSet, NSV_TASK1 );
   }


   /**
    * TEST CASE 3: There is a loose active NSV task outside completed work package with completed
    * NSV task. There should be a warning there when clicking the button 'Release Preview' inside a
    * empty work package in this case.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testWarningWithLooseActiveNsvTaskOutsideCompletedWP() throws Exception {
      DataSet lDataSet;

      // Passed in inventory
      lDataSet = execute( OFFWING_INVENTORY3 );

      MxAssert.assertEquals( 1, lDataSet.getRowCount() );
      lDataSet.next();
      assertRow( lDataSet, NSV_TASK2 );
   }


   /**
    * Tests a row in the dataset
    *
    * @param aDs
    *           the dataset
    * @param aNsvTaskKey
    *           the NSV task key
    */
   private void assertRow( DataSet aDs, TaskKey aNsvTaskKey ) {

      MxAssert.assertEquals( "task_key", aNsvTaskKey,
            aDs.getKey( TaskKey.class, "sched_db_id", "sched_id" ) );
   }


   /**
    * Execute the query
    *
    * @param aInvNoKey
    *           The Inventory Key
    *
    * @return the result
    */

   private DataSet execute( InventoryKey aInvNoKey ) {

      // Build arguments
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( aInvNoKey, new String[] { "aInvDbId", "aInvId" } );

      // Execute the query
      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            "com.mxi.mx.core.query.task.validateReleaseNoDeadlineNsvTasks", lArgs );
   }
}
