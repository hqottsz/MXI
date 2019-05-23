
package com.mxi.mx.core.dao.inventory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

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
import com.mxi.mx.core.services.stask.ComponentWorkPackageItem;


/**
 * Test core GetOffWingTasksByInv.qrx query to get all Off-Wing Tasks for the inventory
 *
 * @author okamenskova
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class GetOffWingTasksByInvTest {

   private static final int TEST_DB_ID = 777;

   private static final InventoryKey MODULE_INV_KEY = new InventoryKey( TEST_DB_ID, 101 );
   private static final InventoryKey BLADE_INV_KEY = new InventoryKey( TEST_DB_ID, 103 );

   private static final TaskKey TEST_OFF_WING_TASK1_KEY = new TaskKey( TEST_DB_ID, 101 );

   private static final TaskKey TEST_OFF_WING_TASK3_KEY = new TaskKey( TEST_DB_ID, 103 );

   private static final ComponentWorkPackageItem COMPONENT_MODULE =
         new ComponentWorkPackageItem( MODULE_INV_KEY, true );
   private static final ComponentWorkPackageItem COMPONENT_BLADE =
         new ComponentWorkPackageItem( BLADE_INV_KEY, false );

   static {
      COMPONENT_MODULE.addTaskKey( TEST_OFF_WING_TASK1_KEY, "OFFWING" );
      COMPONENT_BLADE.addTaskKey( TEST_OFF_WING_TASK3_KEY, "OFFWING" );
   }

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
    * test All OffWing Tasks for the Inventory
    *
    * @throws Exception
    */
   @Test
   public void testAllOffWingTasksByInv() throws Exception {

      List<ComponentWorkPackageItem> lOffWingTasks =
            iInventoryDao.getOffWingTasksByInventory( MODULE_INV_KEY );

      // assert number of record
      assertEquals( 2, lOffWingTasks.size() );

      assertTrue( lOffWingTasks.contains( COMPONENT_MODULE ) );

      assertTrue( lOffWingTasks.contains( COMPONENT_BLADE ) );
   }


   @Before
   public void loadData() throws Exception {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
   }
}
