
package com.mxi.mx.core.query.stask;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.TaskIntervalKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.sched.SchedStaskTable;
import com.mxi.mx.core.table.task.TaskInterval;
import com.mxi.mx.core.table.task.TaskTaskTable;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class performs unit testing on the query file with the same package and name.
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetPartSchedRulesForTaskTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   @ClassRule
   public static InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   public static final TaskKey WORK_PACKAGE = new TaskKey( 1, 1 );

   public static final TaskKey ACTUAL_TASK = new TaskKey( 1, 2 );

   public static final TaskTaskKey TASK_DEFN = new TaskTaskKey( 100, 100 );

   public static final InventoryKey INVENTORY = new InventoryKey( 10, 10 );

   public static final InventoryKey H_INVENTORY = new InventoryKey( 10, 11 );


   /**
    * Tests that the query returns one actual task
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQuery() throws Exception {
      DataSet lDs = execute();

      MxAssert.assertEquals( "Number of retrieved rows", 1, lDs.getRowCount() );

      lDs.first();

      assertEquals( ACTUAL_TASK, lDs.getKey( TaskKey.class, "sched_task_key" ) );
      assertEquals( TASK_DEFN, lDs.getKey( TaskTaskKey.class, "task_task_key" ) );
      assertEquals( RefTaskClassKey.REQ_CLASS_MODE_CD, lDs.getString( "class_mode_cd" ) );
      assertEquals( INVENTORY, lDs.getKey( InventoryKey.class, "inventory_key" ) );
      assertEquals( H_INVENTORY, lDs.getKey( InventoryKey.class, "h_inventory_key" ) );
   }


   /**
    * create the test data
    *
    * @throws Exception
    *            if an error occurs
    */
   @BeforeClass
   public static void loadData() throws Exception {

      // Create a task definition
      TaskTaskTable lTaskTaskTable = TaskTaskTable.create();
      lTaskTaskTable.setTaskClass( RefTaskClassKey.REQ );
      lTaskTaskTable.setRecurringTaskBool( false );
      lTaskTaskTable.insert( TASK_DEFN );

      final TaskTaskKey REQ = lTaskTaskTable.getPk();

      final TaskIntervalKey SCHEDULING_RULE =
            new TaskIntervalKey( REQ, DataTypeKey.CDY, new PartNoKey( 1, 1 ) );

      // Create part scheduling rule
      TaskInterval lTaskIntervalTable = TaskInterval.create( SCHEDULING_RULE );
      lTaskIntervalTable.insert();

      // Create an inventory
      InvInvTable lInvInvTable = InvInvTable.create( INVENTORY );
      lInvInvTable.setHInvNo( H_INVENTORY );
      lInvInvTable.insert();

      // Create actual task
      SchedStaskTable lSchedStaskTable = SchedStaskTable.create( ACTUAL_TASK );
      lSchedStaskTable.setTaskTaskKey( REQ );
      lSchedStaskTable.setMainInventory( INVENTORY );
      lSchedStaskTable.insert();

      // Create actual task event
      EvtEventTable lEvtEventTable = EvtEventTable.create( ACTUAL_TASK.getEventKey() );
      lEvtEventTable.setHistBool( false );
      lEvtEventTable.setStatus( RefEventStatusKey.ACTV );
      lEvtEventTable.setNhEvent( WORK_PACKAGE.getEventKey() );
      lEvtEventTable.insert();
   }


   /**
    * Execute the query.
    *
    * @return dataSet result.
    */
   private DataSet execute() {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( ACTUAL_TASK, "aSchedDbId", "aSchedId" );

      // Execute the query
      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
