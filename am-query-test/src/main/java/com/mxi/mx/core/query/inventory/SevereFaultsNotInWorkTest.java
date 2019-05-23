
/**
 *
 */
package com.mxi.mx.core.query.inventory;

import static org.junit.Assert.assertFalse;
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
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.TaskKey;


/**
 * Validates the SevereFaultsNotInWork query
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class SevereFaultsNotInWorkTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), SevereFaultsNotInWorkTest.class );
   }


   public static final InventoryKey INVENTORY_WITH_NONSEVEREFAULTS = new InventoryKey( 4650, 1 );
   public static final InventoryKey INVENTORY_WITH_SEVEREFAULTS_INWORK =
         new InventoryKey( 4650, 2 );
   public static final InventoryKey INVENTORY_WITH_SEVEREFAULTS_NOTINWORK =
         new InventoryKey( 4650, 3 );


   /**
    * Executes the query for a given inventory
    *
    * @param aInventory
    *           the inventory
    *
    * @return the set of fault tasks
    */
   public Set<TaskKey> execute( InventoryKey aInventory ) {
      Set<TaskKey> lFaultTasks = new HashSet<TaskKey>();

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aInventory, "aInvNoDbId", "aInvNoId" );

      QuerySet lQs = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
      while ( lQs.next() ) {
         lFaultTasks.add( lQs.getKey( TaskKey.class, "event_db_id", "event_id" ) );
      }

      return lFaultTasks;
   }


   /**
    * Ensures that non-severe faults are not returned.
    */
   @Test
   public void testDoesNotReturn_NonSevereFaults() {
      Set<TaskKey> lFaultTasks = execute( INVENTORY_WITH_NONSEVEREFAULTS );
      assertTrue( lFaultTasks.isEmpty() );
   }


   /**
    * Ensures that in-work severe faults are returned
    */
   @Test
   public void testDoesNotReturn_SevereFaults_WhenInWork() {
      Set<TaskKey> lFaultTasks = execute( INVENTORY_WITH_SEVEREFAULTS_INWORK );
      assertTrue( lFaultTasks.isEmpty() );
   }


   /**
    * Ensures that not in-work faults are not returned
    */
   @Test
   public void testReturns_SevereFaults_WhenNotInWork() {
      Set<TaskKey> lFaultTasks = execute( INVENTORY_WITH_SEVEREFAULTS_NOTINWORK );
      assertFalse( lFaultTasks.isEmpty() );
   }
}
