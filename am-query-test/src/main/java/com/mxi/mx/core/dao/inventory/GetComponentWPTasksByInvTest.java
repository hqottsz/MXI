
package com.mxi.mx.core.dao.inventory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.services.stask.TaskItemOfDetachedComponent;


/**
 * Test core GetComponentWPTasksByInv.qrx query to get all compoment work package Tasks for the
 * inventory
 *
 * @author okamenskova
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class GetComponentWPTasksByInvTest {

   private static final int TEST_DB_ID = 777;

   private static final InventoryKey MODULE_INV_KEY = new InventoryKey( TEST_DB_ID, 101 );

   private static final TaskKey WORK_PACKAGE_EVENT_KEY = new TaskKey( TEST_DB_ID, 91 );

   private static final TaskKey TEST_OFF_WING_TASK1_KEY = new TaskKey( TEST_DB_ID, 101 );
   private static final TaskKey TEST_OFF_WING_TASK3_KEY = new TaskKey( TEST_DB_ID, 103 );

   private static final TaskItemOfDetachedComponent MODULE_ITEM = new TaskItemOfDetachedComponent(
         TEST_OFF_WING_TASK1_KEY, WORK_PACKAGE_EVENT_KEY, MODULE_INV_KEY );
   private static final TaskItemOfDetachedComponent BLADE_ITEM = new TaskItemOfDetachedComponent(
         TEST_OFF_WING_TASK3_KEY, WORK_PACKAGE_EVENT_KEY, MODULE_INV_KEY );

   private InventoryDao iInventoryDao;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   @Before
   public void setUp() throws Exception {
      iInventoryDao = new InventoryDaoImpl();
   }


   /**
    * test All Component work package Tasks for the Inventory
    *
    * @throws Exception
    */
   @Test
   public void testAllComponentWPTasksByInv() throws Exception {

      Collection<TaskItemOfDetachedComponent> lTasks =
            iInventoryDao.getComponentWPTasksByInventory( MODULE_INV_KEY );

      // assert number of record
      assertEquals( 2, lTasks.size() );

      assertTrue( lTasks.contains( MODULE_ITEM ) );

      assertTrue( lTasks.contains( BLADE_ITEM ) );
   }


   @Before
   public void loadData() throws Exception {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
   }
}
