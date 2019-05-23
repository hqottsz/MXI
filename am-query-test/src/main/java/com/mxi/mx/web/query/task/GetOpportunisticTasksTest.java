
package com.mxi.mx.web.query.task;

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
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * Tests the query com.mxi.mx.web.query.task.GetOpportunisticTasks
 *
 * <ol>
 * <li>Create an assembly</li>
 * <li>Create slots under the assembly</li>
 * <li>Create a part for the assembly; each</li>
 * <li>Create an inventory for each created part</li>
 * <li>Create baselines; bind the baselines to the config-slots created</li>
 * <li>Link the baselines created as Opportunistic</li>
 * <li>Initialize the opportunizer baseline</li>
 * <li>Wrap the Opportunizer Task into a work-package</li>
 * <li>Initialize an Opportunistic Task into the active inventory</li>
 * <li>Execute the Query being tested here - GetOpportunisticTasks.qrx</li>
 * </ol>
 *
 * Confirm the following results
 *
 * <ul>
 * <li>Two records are returned as follows</li>
 * <li>One result is the initialized opportunistic baseline</li>
 * <li>Other is the uninitialized opportunistic baseline</li>
 * </ul>
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetOpportunisticTasksTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   /**
    * Tests the search by aircraft and date range query
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testQuery() throws Exception {

      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( new InventoryKey( 4650, 390844 ), "aInvNoDbId", "aInvNoId" );
      lArgs.add( new TaskKey( 4650, 133725 ), "aWPDbId", "aWPId" );
      lArgs.add( new LocationKey( 4650, 1 ), "aWorkLocDbId", "aWorkLocId" );

      // execute the query
      DataSet lOpportunisticDs =
            QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
                  QueryExecutor.getQueryName( getClass() ), lArgs );

      // There should be 2 rows ds[uninitialized, initialized]
      MxAssert.assertEquals( "Number of retrieved rows", 2, lOpportunisticDs.getRowCount() );

      lOpportunisticDs.next();
      OpportunisticTaskUtil.assertInitialized( lOpportunisticDs );
      lOpportunisticDs.next();
      OpportunisticTaskUtil.assertUninitialized( lOpportunisticDs );
   }


   /**
    * create the test data
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @BeforeClass
   public static void loadData() throws Exception {
      XmlLoader.load( sDatabaseConnectionRule.getConnection(), GetOpportunisticTasksTest.class,
            GetOpportunisticTasksData.getDataFile() );
   }
}
