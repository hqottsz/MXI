
package com.mxi.mx.lrp.query;

import java.util.HashSet;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.TaskDefnKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class performs unit testing on the query file with the same name.
 *
 * @author hmuradyan
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class BlockSearchTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void setUp() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), BlockSearchTest.class );
   }


   /**
    * Tests that query returns data set with unique rows.
    */
   @Test
   public void testDuplicateBlocks() {

      // Execute the query.
      QuerySet lQs = execute( 4650, "A320" );

      Set<TaskDefnKey> lTaskDefnSet = new HashSet<TaskDefnKey>();

      while ( lQs.next() ) {
         int lTaskDefnDbId = lQs.getInt( "task_defn_db_id" );
         int lTaskDefnId = lQs.getInt( "task_defn_id" );

         // lIsElementUnique is true if added element is unique.
         boolean lIsElementUnique =
               lTaskDefnSet.add( new TaskDefnKey( lTaskDefnDbId, lTaskDefnId ) );

         MxAssert.assertTrue( lIsElementUnique );
      }
   }


   /**
    * Execute the query.
    *
    * @param aAssemblyDbId
    *           Assembly db id.
    * @param aAssemblyCd
    *           Assembly id.
    */
   private QuerySet execute( int aAssemblyDbId, String aAssemblyCd ) {

      // Build query arguments.
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aAssmblDbId", aAssemblyDbId );
      lArgs.add( "aAssmblCd", aAssemblyCd );

      // Execute the query.
      return QuerySetFactory.getInstance().executeQuery( "com.mxi.mx.lrp.query.BlockSearch",
            lArgs );
   }
}
