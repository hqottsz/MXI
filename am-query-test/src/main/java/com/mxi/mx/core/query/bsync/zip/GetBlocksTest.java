
package com.mxi.mx.core.query.bsync.zip;

import java.util.HashSet;
import java.util.Set;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
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
import com.mxi.mx.core.key.TaskKey;


/**
 * Tests the <code>GetBlocks</code> query
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetBlocksTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetBlocksTest.class );
   }


   private static final TaskKey UNIQUE_BLOCK = new TaskKey( 4650, 1 );
   private static final TaskKey NONUNIQUE_BLOCK = new TaskKey( 4650, 2 );


   /**
    * Ensure that non-unique requirements do not get zipped
    */
   @Test
   public void testNonUniqueRequirementDoesNotReturn() {
      Assert.assertThat( blocks(), CoreMatchers.not( CoreMatchers.hasItem( NONUNIQUE_BLOCK ) ) );
   }


   /**
    * Ensure that unique requirements get zipped
    */
   @Test
   public void testUniqueRequirementReturns() {
      Assert.assertThat( blocks(), CoreMatchers.hasItem( UNIQUE_BLOCK ) );
   }


   /**
    * Returns a set of blocks to be zipped
    *
    * @return a set of blocks
    */
   private Set<TaskKey> blocks() {
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aZipId", 1 );

      DataSet lDs = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      Set<TaskKey> lRequirements = new HashSet<TaskKey>();
      while ( lDs.next() ) {
         lRequirements.add( lDs.getKey( TaskKey.class, "task_pk" ) );
      }

      return lRequirements;
   }
}
