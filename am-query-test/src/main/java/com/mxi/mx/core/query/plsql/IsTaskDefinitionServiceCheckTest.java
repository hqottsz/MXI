
package com.mxi.mx.core.query.plsql;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This test class asserts that the function is_task_defn_service_check within the LPA_PKG operates
 * correctly in OPER-3622
 *
 * @author hzheng
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class IsTaskDefinitionServiceCheckTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            IsTaskDefinitionServiceCheckTest.class );
   }


   /**
    * Test Case 1: This test validates that a task definition will show up even if without
    * applicability rule there
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testTaskDefinitionWithoutApplicabilityRule() throws Exception {
      TaskTaskKey lTaskTaskKey = new TaskTaskKey( 1, 1 );

      // ACTION: Execute the function
      int lResult = execute( lTaskTaskKey );

      MxAssert.assertEquals( 1, lResult );
   }


   /**
    * Test Case 2: This test validates that a task definition will show up even if there is an
    * applicability rule there
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testTaskDefinitionWithApplicabilityRule() throws Exception {
      TaskTaskKey lTaskTaskKey = new TaskTaskKey( 1, 2 );

      // ACTION: Execute the function
      int lResult = execute( lTaskTaskKey );

      MxAssert.assertEquals( 1, lResult );
   }


   /**
    * Execute the function.
    *
    * @param aTaskTaskKey
    *           The root of a Task Tree.
    *
    * @return 1 if a task definition revision is applicable to be considered a Service Check. 0
    *         else.
    *
    * @throws Exception
    *            if an error occurs.
    */
   private int execute( TaskTaskKey aTaskTaskKey ) throws Exception {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aTaskTaskKey, new String[] { "an_TaskTaskDbId", "an_TaskTaskId" } );

      String[] lParmOrder = { "an_TaskTaskDbId", "an_TaskTaskId" };

      // Execute the query
      return Integer
            .parseInt( QueryExecutor.executeFunction( sDatabaseConnectionRule.getConnection(),
                  "LPA_PKG.is_task_defn_service_check", lParmOrder, lArgs ) );
   }
}
