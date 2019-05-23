
package com.mxi.mx.core.query.bsync.zip;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.util.HashSet;
import java.util.Set;

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
 * Tests the <code>GetRequirements</code> query
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetRequirementsTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetRequirementsTest.class );
   }


   private static final TaskKey UNIQUE_REQUIREMENT = new TaskKey( 4650, 1 );
   private static final TaskKey NONUNIQUE_REQUIREMENT = new TaskKey( 4650, 2 );
   private static final TaskKey ON_CONDITION_REQUIREMENT = new TaskKey( 4650, 3 );


   /**
    * Ensure that non-unique requirements do not get zipped
    */
   @Test
   public void testNonUniqueRequirementDoesNotReturn() {
      assertThat( requirements(), not( hasItem( NONUNIQUE_REQUIREMENT ) ) );
   }


   /**
    * Ensure that non-unique requirements do not get zipped
    */
   @Test
   public void testOnConditionRequirementDoesNotReturn() {
      assertThat( requirements(), not( hasItem( ON_CONDITION_REQUIREMENT ) ) );
   }


   /**
    * Ensure that unique requirements get zipped
    */
   @Test
   public void testUniqueRequirementReturns() {
      assertThat( requirements(), hasItem( UNIQUE_REQUIREMENT ) );
   }


   /**
    * Returns a set of requirements to be zipped
    *
    * @return a set of requirements
    */
   private Set<TaskKey> requirements() {
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
