
package com.mxi.mx.core.query.plsql;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.ConfigSlotBuilder;
import com.mxi.am.domain.builder.DomainBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.domain.builder.TaskRevisionBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.GlobalParametersStub;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.sched.SchedStaskTable;


/**
 * Tests for the hasACTVDependentTask plsql function.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class HasACTVDependentTaskTest {

   private static final GlobalParametersStub LOGIC_PARAMETERS = new GlobalParametersStub( "LOGIC" );
   private static final ConfigSlotKey NO_CONFIG_SLOT = null;

   private InventoryKey iChildInventory;
   private InventoryKey iParentInventory;
   private InventoryKey iRootInventory;
   private InventoryKey iTargetInventory;
   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Tests the following scenario:
    *
    * <pre>
    * - active task exists against the inventory's child inventory
    *   where the active task is based on following task definition:
    *     - config slot based
    *     - dependent upon itself
    *     - no other task dependencies
    * </pre>
    *
    * Expected result: hasACTVDependentTasks() returns false, as active tasks against child
    * inventory are not checked.
    *
    * @throws Exception
    */
   @Test
   public void testConfigSlotBasedTaskOnlyDependentUponItselfAlreadyExistsAgainstAgainstInvChild()
         throws Exception {

      // Initialize an active task against the target inventory.
      TaskKey lTaskKey = buildConfigSlotBasedTaskThatIsOnlyDependentOnItself( iChildInventory );

      // Get the task definition key.
      TaskTaskKey lTaskTaskKey = SchedStaskTable.findByPrimaryKey( lTaskKey ).getTaskTaskKey();

      boolean lResult = execute( lTaskTaskKey, iTargetInventory );

      assertFalse( "Expected hasACTVDependentTasks to return false.", lResult );
   }


   /**
    * Tests the following scenario:
    *
    * <pre>
    * - active task exists against the inventory's parent inventory
    *   where the active task is based on following task definition:
    *     - config slot based
    *     - dependent upon itself
    *     - no other task dependencies
    * </pre>
    *
    * Expected result: hasACTVDependentTasks() returns true<br>
    *
    * @throws Exception
    */
   @Test
   public void testConfigSlotBasedTaskOnlyDependentUponItselfAlreadyExistsAgainstAgainstInvParent()
         throws Exception {

      // Initialize an active task against the target inventory.
      TaskKey lTaskKey = buildConfigSlotBasedTaskThatIsOnlyDependentOnItself( iParentInventory );

      // Get the task definition key.
      TaskTaskKey lTaskTaskKey = SchedStaskTable.findByPrimaryKey( lTaskKey ).getTaskTaskKey();

      boolean lResult = execute( lTaskTaskKey, iTargetInventory );

      assertTrue( "Expected hasACTVDependentTasks to return true.", lResult );
   }


   /**
    * Tests the following scenario:
    *
    * <pre>
    * - active task exists against the inventory itself
    *   where the active task is based on following task definition:
    *     - config slot based
    *     - dependent upon itself
    *     - no other task dependencies
    * </pre>
    *
    * Expected result: hasACTVDependentTasks() returns true<br>
    *
    * @throws Exception
    */
   @Test
   public void testConfigSlotBasedTaskOnlyDependentUponItselfAlreadyExistsAgainstInv()
         throws Exception {

      // Initialize an active task against the target inventory.
      TaskKey lTaskKey = buildConfigSlotBasedTaskThatIsOnlyDependentOnItself( iTargetInventory );

      // Get the task definition key.
      TaskTaskKey lTaskTaskKey = SchedStaskTable.findByPrimaryKey( lTaskKey ).getTaskTaskKey();

      boolean lResult = execute( lTaskTaskKey, iTargetInventory );

      assertTrue( "Expected hasACTVDependentTasks to return true.", lResult );
   }


   /**
    * Tests the following scenario:
    *
    * <pre>
    * - active task exists against the inventory itself
    *   where the active task is based on following task definition:
    *     - part based
    *     - dependent upon itself
    *     - no other task dependencies
    * </pre>
    *
    * Expected result: hasACTVDependentTasks() returns true<br>
    *
    * @throws Exception
    */
   @Test
   public void testPartBasedTaskOnlyDependentUponItselfAlreadyExistsAgainstInv() throws Exception {

      // Initialize an active task against the target inventory.
      TaskKey lTaskKey = buildPartBasedTaskThatIsOnlyDependentOnItself( iTargetInventory );

      // Get the task definition key.
      TaskTaskKey lTaskTaskKey = SchedStaskTable.findByPrimaryKey( lTaskKey ).getTaskTaskKey();

      boolean lResult = execute( lTaskTaskKey, iTargetInventory );

      assertTrue( "Expected hasACTVDependentTasks to return true.", lResult );
   }


   /**
    * Tests the following scenario:
    *
    * <pre>
    * - active task exists against the inventory's child inventory
    *   where the active task is based on following task definition:
    *     - part based
    *     - dependent upon itself
    *     - no other task dependencies
    * </pre>
    *
    * Expected result: hasACTVDependentTasks() returns false, as only active tasks against the
    * inventory itself are checked for part based tasks.
    *
    * @throws Exception
    */
   @Test
   public void testPartBasedTaskOnlyDependentUponItselfAlreadyExistsAgainstInvChild()
         throws Exception {

      // Initialize an active task against the target inventory.
      TaskKey lTaskKey = buildPartBasedTaskThatIsOnlyDependentOnItself( iChildInventory );

      // Get the task definition key.
      TaskTaskKey lTaskTaskKey = SchedStaskTable.findByPrimaryKey( lTaskKey ).getTaskTaskKey();

      boolean lResult = execute( lTaskTaskKey, iTargetInventory );

      assertFalse( "Expected hasACTVDependentTasks to return false.", lResult );
   }


   /**
    * Tests the following scenario:
    *
    * <pre>
    * - active task exists against the inventory's parent inventory
    *   where the active task is based on following task definition:
    *     - part based
    *     - dependent upon itself
    *     - no other task dependencies
    * </pre>
    *
    * Expected result: hasACTVDependentTasks() returns false, as only active tasks against the
    * inventory itself are checked for part based tasks.
    *
    * @throws Exception
    */
   @Test
   public void testPartBasedTaskOnlyDependentUponItselfAlreadyExistsAgainstInvParent()
         throws Exception {

      // Initialize an active task against the target inventory.
      TaskKey lTaskKey = buildPartBasedTaskThatIsOnlyDependentOnItself( iParentInventory );

      // Get the task definition key.
      TaskTaskKey lTaskTaskKey = SchedStaskTable.findByPrimaryKey( lTaskKey ).getTaskTaskKey();

      boolean lResult = execute( lTaskTaskKey, iTargetInventory );

      assertFalse( "Expected hasACTVDependentTasks to return false.", lResult );
   }


   @Before
   public void loadData() throws Exception {
      // Build an inventory hierarchy with an ACFT root and TRK sub-inventory.
      iRootInventory = new InventoryBuilder().build();

      iParentInventory = new InventoryBuilder().withClass( RefInvClassKey.TRK )
            .withParentInventory( iRootInventory ).withHighestInventory( iRootInventory ).build();

      iTargetInventory = new InventoryBuilder().withClass( RefInvClassKey.TRK )
            .withParentInventory( iParentInventory ).withHighestInventory( iRootInventory ).build();
      iChildInventory = new InventoryBuilder().withClass( RefInvClassKey.TRK )
            .withParentInventory( iTargetInventory ).withHighestInventory( iRootInventory ).build();
   }


   /**
    * Build a task based on a config slot based task definition that is only dependent on itself.
    *
    * @param aInventory
    *           inventory to build task against
    *
    * @return Task key of the build task.
    */
   private TaskKey buildConfigSlotBasedTaskThatIsOnlyDependentOnItself( InventoryKey aInventory ) {

      // According to the plsql function hasACTVDependentTask, a task defn that is dependent on
      // itself is defined as being unique, mandatory, and non-JIC. In order to make the task
      // definition only dependent on itself, do not set any other task dependencies. In order to
      // make the task definition config slot based, set its config slot.
      DomainBuilder<TaskTaskKey> lTaskDefnBuilder =
            new TaskRevisionBuilder().isUnique().isMandatory().withTaskClass( RefTaskClassKey.REQ )
                  .withConfigSlot( new ConfigSlotBuilder( "" ).build() );

      // Build an active task against the target inventory and return its key.
      TaskKey lTaskKey = new TaskBuilder().withTaskRevision( lTaskDefnBuilder )
            .onInventory( aInventory ).withStatus( RefEventStatusKey.ACTV ).build();

      return lTaskKey;
   }


   /**
    * Build a task based on a part based task definition that is only dependent on itself.
    *
    * @param aInventory
    *           inventory to build task against
    *
    * @return Task key of the build task.
    */
   private TaskKey buildPartBasedTaskThatIsOnlyDependentOnItself( InventoryKey aInventory ) {

      // According to the plsql function hasACTVDependentTask, a task defn that is dependent on
      // itself is defined as being unique, mandatory, and non-JIC. In order to make the task
      // definition only dependent on itself, do not set any other task dependencies. In order to
      // make the task definition config slot based, set its config slot.
      DomainBuilder<TaskTaskKey> lTaskDefnBuilder = new TaskRevisionBuilder().isUnique()
            .isMandatory().withTaskClass( RefTaskClassKey.REQ ).withConfigSlot( NO_CONFIG_SLOT );

      // Build an active task against the target inventory and return its key.
      TaskKey lTaskKey = new TaskBuilder().withTaskRevision( lTaskDefnBuilder )
            .onInventory( aInventory ).withStatus( RefEventStatusKey.ACTV ).build();

      return lTaskKey;
   }


   /**
    * Executes the plsql function hasACTVDependentTasks and returns true if there exist active
    * dependent tasks, otherwise returns false.
    *
    * @param aTaskTask
    *           task defn key
    * @param aInventory
    *           inventory key
    *
    * @return true if there exist active dependent tasks, otherwise false.
    *
    * @throws Exception
    */
   private boolean execute( TaskTaskKey aTaskTask, InventoryKey aInventory ) throws Exception {

      // We cannot call the function directly as it returns an "mxKeyTable", which
      // our execute functions cannot handle (e.g. QueryTestCase.executeFuntion() ).
      // Thus, we will use the wrapper qrx file to test the function.
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aTaskTask, "aTaskDbId", "aTaskId" );
      lArgs.add( aInventory, "aInvNoDbId", "aInvNoId" );

      QuerySet lQs = QuerySetFactory.getInstance()
            .executeQuery( "com.mxi.mx.core.query.bsync.HasACTVDependentTasks", lArgs );

      boolean lResult = true;

      if ( lQs.next() ) {
         if ( lQs.getInt( "no_actv_task" ) == 1 ) {

            lResult = false;
         }
      }

      return lResult;
   }
}
