
package com.mxi.mx.core.worker.bsync.precondition;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.i18n.i18n;
import com.mxi.mx.common.services.work.precondition.PreconditionFailedException;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.worker.bsync.preconditions.ZipTaskPrecondition;


/**
 * This class tests the ZipTaskPrecondition class.
 *
 * @author dsewell
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class ZipTaskPreconditionTest {

   private static final int WORK_ITEM_WITH_NO_CONFLICT = 1;

   private static final int WORK_ITEM_WITH_PARENT_CONFLICT = 2;

   private static final int WORK_ITEM_WITH_NEW_PARENT_CONFLICT = 4;

   private ZipTaskPrecondition iPrecondition;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   /**
    * Ensure that the precondition evaluation fails when a work item's new parent has a conflict
    * with another item's parent.
    *
    * @throws Exception
    *            If an error occurs
    */
   @Test
   public void testFailsWhenWorkItemWithMatchingNewParent() throws Exception {
      iPrecondition = new ZipTaskPrecondition( WORK_ITEM_WITH_NEW_PARENT_CONFLICT,
            new TaskKey( 4650, 3001 ), new TaskKey( 4650, 3002 ), new TaskKey( 4650, 3003 ) );

      try {
         iPrecondition.evaluate();

         fail( "Expected PreconditionFailedException" );
      } catch ( PreconditionFailedException e ) {
         assertEquals( i18n.get( "core.msg.THE_ZIPPING_OF_TASK_MUST_WAIT", "4650:3001",
               "4650:3004;4650:3003;4650:3005;" ), e.getMessage() );
      }
   }


   /**
    * Ensure that the precondition evaluation fails when a work item's parent has a conflict with
    * another item's new parent.
    *
    * @throws Exception
    *            If an error occurs
    */
   @Test
   public void testFailsWhenWorkItemWithMatchingParent() throws Exception {
      iPrecondition = new ZipTaskPrecondition( WORK_ITEM_WITH_PARENT_CONFLICT,
            new TaskKey( 4650, 2001 ), new TaskKey( 4650, 2002 ), new TaskKey( 4650, 2003 ) );

      try {
         iPrecondition.evaluate();

         fail( "Expected PreconditionFailedException" );
      } catch ( PreconditionFailedException e ) {
         assertEquals( i18n.get( "core.msg.THE_ZIPPING_OF_TASK_MUST_WAIT", "4650:2001",
               "4650:2004;4650:2005;4650:2002;" ), e.getMessage() );
      }
   }


   /**
    * Ensure that precondition evaluation passes when there is no conflicting work item.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testPassesWhenNoConflictingWorkItem() throws Exception {

      iPrecondition = new ZipTaskPrecondition( WORK_ITEM_WITH_NO_CONFLICT,
            new TaskKey( 4650, 1001 ), new TaskKey( 4650, 1002 ), new TaskKey( 4650, 1003 ) );

      // if no exception is thrown, this test passes
      iPrecondition.evaluate();
   }


   @Before
   public void loadData() throws Exception {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
   }
}
