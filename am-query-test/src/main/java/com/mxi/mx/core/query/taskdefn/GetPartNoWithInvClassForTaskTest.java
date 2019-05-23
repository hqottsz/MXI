
package com.mxi.mx.core.query.taskdefn;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.TaskTaskKey;


/**
 * Test GetPartNoWithInvClassForTask.qrx returns the appropriate data
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetPartNoWithInvClassForTaskTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetPartNoWithInvClassForTaskTest.class );
   }


   private PartNoKey iSYSPart_1 = new PartNoKey( 4650, 3 );

   private TaskTaskKey iTaskKey_MixedParts = new TaskTaskKey( 4650, 2 );

   private TaskTaskKey iTaskKey_NoParts = new TaskTaskKey( 4650, 1 );

   private PartNoKey iTRKPart_1 = new PartNoKey( 4650, 1 );

   private PartNoKey iTRKPart_2 = new PartNoKey( 4650, 2 );


   /**
    * Retrieves appropriate parts when match
    */
   @Test
   public void testMixedParts() {
      Set<PartNoKey> lParts = executeQuery( iTaskKey_MixedParts, RefInvClassKey.TRK );

      assertEquals( 2, lParts.size() );
      assertTrue( lParts.contains( iTRKPart_1 ) );
      assertTrue( lParts.contains( iTRKPart_2 ) );

      lParts = executeQuery( iTaskKey_MixedParts, RefInvClassKey.SYS );

      assertEquals( 1, lParts.size() );
      assertTrue( lParts.contains( iSYSPart_1 ) );
   }


   /**
    * Returns no rows when no match
    */
   @Test
   public void testNoParts() {
      Set<PartNoKey> lParts = executeQuery( iTaskKey_NoParts, RefInvClassKey.TRK );
      assertEquals( 0, lParts.size() );

      lParts = executeQuery( iTaskKey_NoParts, RefInvClassKey.SYS );
      assertEquals( 0, lParts.size() );
   }


   /**
    * Executes query
    *
    * @param aTaskKey
    *           the task to obtain the results
    * @param aInvClassKey
    *           the part inventory class
    *
    * @return the parts
    */
   protected Set<PartNoKey> executeQuery( TaskTaskKey aTaskKey, RefInvClassKey aInvClassKey ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aTaskKey, "aTaskDbId", "aTaskId" );
      lArgs.add( aInvClassKey, "aInvClassDbId", "aInvClassCd" );

      DataSet lDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
      lDataSet.addSort( "dsString(part_no_id)", true );

      Set<PartNoKey> lParts = new HashSet<PartNoKey>();
      while ( lDataSet.next() ) {
         lParts.add( lDataSet.getKey( PartNoKey.class, "part_no_db_id", "part_no_id" ) );
      }

      return lParts;
   }
}
