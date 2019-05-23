
package com.mxi.mx.core.query.plsql;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.TaskTaskKey;


/**
 * Tests the HasActvOrNoComplianceRefDoc function
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class HasActvOrNoComplianceRefDocTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            HasActvOrNoComplianceRefDocTest.class );
   }


   private static final TaskTaskKey REQ_WITH_NO_COMPLIES = new TaskTaskKey( 4650, 1 );
   private static final TaskTaskKey REQ_WITH_COMPLIANCIES = new TaskTaskKey( 4650, 2 );

   private static final InventoryKey INV_WITH_NO_HISTORIC_REF_DOC = new InventoryKey( 4650, 1 );
   private static final InventoryKey INV_WITH_ONE_HISTORIC_ONE_ACTV_REF_DOC =
         new InventoryKey( 4650, 2 );
   private static final InventoryKey INV_WITH_ALL_HISTORIC_REF_DOCS = new InventoryKey( 4650, 3 );

   private static final InventoryKey INVENTORY_NOT_DISPOSITIONED_ON_REF_DOC =
         new InventoryKey( 4650, 11 );
   private static final InventoryKey INVENTORY_DISPOSITIONED_ON_REF_DOC =
         new InventoryKey( 4650, 12 );


   /**
    * If a task has ref docs to comply to and they're all historic, the task is no longer required.
    *
    * @throws Exception
    */
   @Test
   public void testGivenAllCompliesHistoricThenReturnFalse() throws Exception {
      assertFalse( "Should return false if all historic.",
            hasActvOrNoComplianceRefDoc( REQ_WITH_COMPLIANCIES, INV_WITH_ALL_HISTORIC_REF_DOCS ) );
   }


   /**
    * If a task has ref docs to comply to and at least one is not historic, the task is required.
    *
    * @throws Exception
    */
   @Test
   public void testGivenAtLeastOneCompliesActvThenReturnTrue() throws Exception {
      assertTrue( "Should return true if all active.",
            hasActvOrNoComplianceRefDoc( REQ_WITH_COMPLIANCIES, INV_WITH_NO_HISTORIC_REF_DOC ) );
      assertTrue( "Should return true if at least one is still active.",
            hasActvOrNoComplianceRefDoc( REQ_WITH_COMPLIANCIES,
                  INV_WITH_ONE_HISTORIC_ONE_ACTV_REF_DOC ) );
   }


   /**
    * If a task defn has comply ref docs and all the ref docs were dispositioned on an inventory,
    * this task defn cannot be initialized on that inventory.
    *
    * @throws Exception
    */
   @Test
   public void testGivenInventoryCompliesHistoricThenReturnFalse() throws Exception {
      assertFalse( "Should return false if dispositioned on the inventory.",
            hasActvOrNoComplianceRefDoc( REQ_WITH_COMPLIANCIES,
                  INVENTORY_DISPOSITIONED_ON_REF_DOC ) );
   }


   /**
    * If a task defn has comply ref docs and all the ref docs were dispositioned on an inventory's
    * sibling, this task defn can still be initialized on that inventory.
    *
    * @throws Exception
    */
   @Test
   public void testGivenSiblingCompliesHistoricThenReturnTrue() throws Exception {
      assertTrue( "Should return true if dispositioned on sibling.", hasActvOrNoComplianceRefDoc(
            REQ_WITH_COMPLIANCIES, INVENTORY_NOT_DISPOSITIONED_ON_REF_DOC ) );
   }


   /**
    * If the task has no ref docs, the task is required.
    *
    * @throws Exception
    */
   @Test
   public void testGivenTaskWithNoCompliesThenReturnTrue() throws Exception {
      assertTrue( "Should return true if no complies link exists.",
            hasActvOrNoComplianceRefDoc( REQ_WITH_NO_COMPLIES, INV_WITH_NO_HISTORIC_REF_DOC ) );
      assertTrue( "Should return true if historic ref doc does not comply with task.",
            hasActvOrNoComplianceRefDoc( REQ_WITH_NO_COMPLIES,
                  INV_WITH_ONE_HISTORIC_ONE_ACTV_REF_DOC ) );
      assertTrue( "Should return true if historic ref doc does not comply with task.",
            hasActvOrNoComplianceRefDoc( REQ_WITH_NO_COMPLIES, INV_WITH_ALL_HISTORIC_REF_DOCS ) );
   }


   /**
    * Executes the hasACTVOrNoComplianceRefDoc pl/sql function
    *
    * @param aTask
    *           the task
    * @param aInventory
    *           the inventory
    *
    * @return TRUE if it has active or no compliance ref docs.
    *
    * @throws Exception
    */
   private boolean hasActvOrNoComplianceRefDoc( TaskTaskKey aTask, InventoryKey aInventory )
         throws Exception {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aTask, "task_db_id", "task_id" );
      lArgs.add( aInventory, "inv_no_db_id", "inv_no_id" );

      return "1".equals( QueryExecutor.executeFunction( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getFunctionName( getClass() ),
            new String[] { "task_db_id", "task_id", "inv_no_db_id", "inv_no_id" }, lArgs ) );
   }
}
