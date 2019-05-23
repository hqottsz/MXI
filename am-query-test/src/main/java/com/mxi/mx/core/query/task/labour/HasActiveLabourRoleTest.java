
package com.mxi.mx.core.query.task.labour;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.core.key.TaskKey;


/**
 * This class tests the HasActiveLabourRole query.
 *
 * @author dsewell
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class HasActiveLabourRoleTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), HasActiveLabourRoleTest.class );
   }


   private static final TaskKey TASK_KEY_PENDING = new TaskKey( 4650, 2000 );
   private static final TaskKey TASK_KEY_ACTV = new TaskKey( 4650, 2001 );
   private static final TaskKey TASK_KEY_COMPLETE = new TaskKey( 4650, 2002 );


   /**
    * Tests that a task with an ACTV labour role returns one row.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testQueryACTVStatus() throws Exception {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( TASK_KEY_ACTV, "aSchedDbId", "aSchedId" );

      QuerySet lDs = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      assertEquals( "One row", 1, lDs.getRowCount() );
   }


   /**
    * Tests that a task with an COMPLETE labour role returns no rows.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testQueryCOMPLETEStatus() throws Exception {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( TASK_KEY_COMPLETE, "aSchedDbId", "aSchedId" );

      QuerySet lDs = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      assertEquals( "No rows", 0, lDs.getRowCount() );
   }


   /**
    * Tests that a task with an PENDING labour role returns one row.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testQueryPENDINGStatus() throws Exception {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( TASK_KEY_PENDING, "aSchedDbId", "aSchedId" );

      QuerySet lDs = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      assertEquals( "One row", 1, lDs.getRowCount() );
   }
}
