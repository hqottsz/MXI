
package com.mxi.mx.db.view.baselinetask;

import static com.mxi.mx.testing.matchers.MxMatchers.assertThat;
import static com.mxi.mx.testing.matchers.MxMatchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.inv.InvInvTable;


/**
 * Ensures that the vw_inv_task returns the appropriate tasks
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class InvTaskTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   private static final InventoryKey ROOT_INV = new InventoryKey( 4650, 1 );
   private static final InventoryKey SUB_INV = new InventoryKey( 4650, 2 );


   @Before
   public void setUp() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
   }


   /**
    * Archived inventory should not have any tasks initialized on it.
    */
   @Test
   public void testArchiveReturnsNoTasks() {
      withCondition( ROOT_INV, RefInvCondKey.ARCHIVE );
      withCondition( SUB_INV, RefInvCondKey.ARCHIVE );

      assertThat( tasksFor( ROOT_INV ), is( empty() ) );
      assertThat( tasksFor( SUB_INV ), is( empty() ) );
   }


   /**
    * Condemned inventory should not have any tasks initialized on it.
    */
   @Test
   public void testCondemnReturnsNoTasks() {
      withCondition( ROOT_INV, RefInvCondKey.CONDEMN );
      withCondition( SUB_INV, RefInvCondKey.CONDEMN );

      assertThat( tasksFor( ROOT_INV ), is( empty() ) );
      assertThat( tasksFor( SUB_INV ), is( empty() ) );
   }


   /**
    * Condemned inventory (including sub-components) should not have any tasks initialized on it.
    */
   @Test
   public void testCondemnReturnsNoTasks_SubComponentInService() {
      withCondition( ROOT_INV, RefInvCondKey.CONDEMN );
      withCondition( SUB_INV, RefInvCondKey.INSRV );

      assertThat( tasksFor( ROOT_INV ), is( empty() ) );
      assertThat( tasksFor( SUB_INV ), is( empty() ) );
   }


   /**
    * Inventory in service should have tasks initialized on it.
    */
   @Test
   public void testInServiceReturnsTasks() {
      withCondition( ROOT_INV, RefInvCondKey.INSRV );
      withCondition( SUB_INV, RefInvCondKey.INSRV );

      assertThat( tasksFor( ROOT_INV ), is( not( empty() ) ) );
      assertThat( tasksFor( SUB_INV ), is( not( empty() ) ) );
   }


   /**
    * Scrapped inventory should not have any tasks initialized on it.
    */
   @Test
   public void testScrapReturnsNoTasks() {
      withCondition( ROOT_INV, RefInvCondKey.SCRAP );
      withCondition( SUB_INV, RefInvCondKey.SCRAP );

      assertThat( tasksFor( ROOT_INV ), is( empty() ) );
      assertThat( tasksFor( SUB_INV ), is( empty() ) );
   }


   /**
    * Obtains the tasks applicable to the specified inventory
    *
    * @param aInventory
    *           the inventory
    *
    * @return the applicable tasks
    */
   private List<TaskTaskKey> tasksFor( InventoryKey aInventory ) {

      // Set the inventory context
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "inv_no_db_id", aInventory.getDbId() );
      lArgs.add( "inv_no_id", aInventory.getId() );

      // Get the task revision for the task defnition
      QuerySet lQuerySet = QuerySetFactory.getInstance().executeQueryTable( "VW_INV_TASK", lArgs );

      List<TaskTaskKey> lTaskTaskKey = new ArrayList<TaskTaskKey>( 1 );
      while ( lQuerySet.next() ) {
         lTaskTaskKey.add( lQuerySet.getKey( TaskTaskKey.class, "task_db_id", "task_id" ) );
      }

      return lTaskTaskKey;
   }


   /**
    * Overrides the condition of the specified inventory
    *
    * @param aInventory
    *           the inventory
    * @param aCondition
    *           the condition
    */
   private void withCondition( InventoryKey aInventory, RefInvCondKey aCondition ) {
      InvInvTable lInventoryTable = InvInvTable.findByPrimaryKey( aInventory );
      lInventoryTable.setInvCond( aCondition );
      lInventoryTable.update();
   }
}
