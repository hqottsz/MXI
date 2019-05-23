
package com.mxi.mx.core.query.inventory.config;

import static com.mxi.mx.testing.matchers.MxMatchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
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
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.TaskKey;


/**
 * Ensures that the query returns all the incompatible inventory for a specified task
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetPostIncompatiblePartsForTaskTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetPostIncompatiblePartsForTaskTest.class );
   }


   private static final TaskKey TASK_WITH_MULTIPLE_INCOMPATIBLE_INVENOTRY = new TaskKey( 4650, 5 );
   private static final TaskKey TASK_WITH_NO_INCOMPATIBLE_INVENTORY = new TaskKey( 4650, 3 );
   private static final TaskKey TASK_WITH_NO_INCOMPATIBLE_PART = new TaskKey( 4650, 1 );
   private static final TaskKey TASK_WITH_ONE_INCOMPATIBLE_INVENTORY = new TaskKey( 4650, 4 );
   private static final TaskKey TASK_WITH_OPEN_INCOMPATIBLE_PART = new TaskKey( 4650, 2 );


   /**
    * Ensure that no rows are returned when all inventory are compatible
    */
   @Test
   public void testAllCompatibleInventoryShouldReturnNothing() {
      Set<InventoryKey> lIncompatibleInventory =
            getIncompatibleInventory( TASK_WITH_NO_INCOMPATIBLE_INVENTORY );
      assertThat( lIncompatibleInventory, is( empty() ) );
   }


   /**
    * Ensure multiple rows are returned when multiple inventory are incompatible
    */
   @Test
   public void testMultipleIncompatibleInventoryShouldReturnInventory() {
      Set<InventoryKey> lIncompatibleInventory =
            getIncompatibleInventory( TASK_WITH_MULTIPLE_INCOMPATIBLE_INVENOTRY );
      assertThat( lIncompatibleInventory.size(), is( greaterThan( 1 ) ) );
   }


   /**
    * Ensure one row is returned when one inventory is incompatible
    */
   @Test
   public void testOneIncompatibleInventoryShouldReturnInvenotry() {
      Set<InventoryKey> lIncompatibleInventory =
            getIncompatibleInventory( TASK_WITH_ONE_INCOMPATIBLE_INVENTORY );
      assertThat( lIncompatibleInventory.size(), is( equalTo( 1 ) ) );
   }


   /**
    * Ensure no rows are returned for OPEN compatibility (this is used for the Complete Task
    * process)
    */
   @Test
   public void testTaskOpenIncompatibilityShouldReturnNothing() {
      Set<InventoryKey> lIncompatibleInventory =
            getIncompatibleInventory( TASK_WITH_OPEN_INCOMPATIBLE_PART );
      assertThat( lIncompatibleInventory, is( empty() ) );
   }


   /**
    * Ensure no rows are returned when the task has no associated incompatibility parts
    */
   @Test
   public void testTaskWithNoPartIncompatibilitiesShouldReturnNothing() {
      Set<InventoryKey> lIncompatibleInventory =
            getIncompatibleInventory( TASK_WITH_NO_INCOMPATIBLE_PART );
      assertThat( lIncompatibleInventory, is( empty() ) );
   }


   /**
    * Returns the list of incompatible inventory
    *
    * @param aTask
    *           the task
    *
    * @return the incompatible inventory
    */
   private Set<InventoryKey> getIncompatibleInventory( TaskKey aTask ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aTask, "aTaskDbId", "aTaskId" );

      QuerySet lQs = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
      Set<InventoryKey> lIncompatibleInventory = new HashSet<InventoryKey>();
      while ( lQs.next() ) {
         lIncompatibleInventory.add( lQs.getKey( InventoryKey.class, "inv_no_key" ) );
      }

      return lIncompatibleInventory;
   }
}
