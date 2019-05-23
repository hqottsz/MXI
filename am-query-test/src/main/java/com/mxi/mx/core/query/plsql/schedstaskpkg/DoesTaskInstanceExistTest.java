
package com.mxi.mx.core.query.plsql.schedstaskpkg;

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


/**
 * This test class asserts that the function DoesTaskInstanceExist within the SCHED_STASK_PKG
 * operates correctly.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class DoesTaskInstanceExistTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), DoesTaskInstanceExistTest.class );
   }


   /**
    * <p>
    * Make sure that when we submit the id of an <b>existing</b> task for inventory, the count
    * returned is 1.
    * </p>
    */
   @Test
   public void testWhenAtLeastOneInstanceOfTask() throws Exception {

      // ARRANGE - data has been loaded in the Before Class loadData() method
      int lTaskDbId = 1;
      int lTaskId = 1;
      int lInvNoDbId = 1;
      int lInvNoId = 1;

      // ACT
      Integer lInstances = execute( lTaskDbId, lTaskId, lInvNoDbId, lInvNoId );

      // ASSERT
      Assert.assertEquals( "Wrong count of tasks was returned.", 1, lInstances.longValue() );

   }


   /**
    * <p>
    * Make sure that when we submit the id of an <b>non existing</b> task for inventory, the count
    * returned is 0.
    * </p>
    */
   @Test
   public void testWithNoInstanceOfTask() throws Exception {

      // ARRANGE - data has been loaded in the Before Class loadData() method
      int lTaskDbId = 2;
      int lTaskId = 2;
      int lInvNoDbId = 1;
      int lInvNoId = 1;

      // ACT
      Integer lInstances = execute( lTaskDbId, lTaskId, lInvNoDbId, lInvNoId );

      // ASSERT
      Assert.assertEquals( "Wrong count of tasks was returned.", 0, lInstances.longValue() );

   }


   /**
    * Execute the function.
    *
    * @return the number of tasks instances.
    *
    * @throws Exception
    *            if an error occurs.
    */
   private Integer execute( int aTaskDbId, int aTaskId, int aInvNoDbId, int aInvNoId )
         throws Exception {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "an_TaskDbId", aTaskDbId );
      lArgs.add( "an_TaskId", aTaskId );
      lArgs.add( "an_InvNoDbId", aInvNoDbId );
      lArgs.add( "an_InvNoId", aInvNoId );

      // build parameters
      String[] lParmOrder = { "an_TaskDbId", "an_TaskId", "an_InvNoDbId", "an_InvNoId" };

      // return the results
      return Integer
            .parseInt( QueryExecutor.executeFunction( sDatabaseConnectionRule.getConnection(),
                  "SCHED_STASK_PKG.DoesTaskInstanceExist", lParmOrder, lArgs ) );
   }
}
