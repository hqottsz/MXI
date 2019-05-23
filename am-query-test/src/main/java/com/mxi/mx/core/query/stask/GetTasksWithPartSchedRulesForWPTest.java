
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
import com.mxi.mx.common.services.dao.MxDataAccess;
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
public final class GetTasksWithPartSchedRulesForWPTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   @ClassRule
   public static InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   public static final TaskKey WORK_PACKAGE = new TaskKey( 1, 1 );

   public static final TaskKey BLOCK_ACTUAL_TASK = new TaskKey( 1, 2 );

   public static final TaskKey REQ_ACTUAL_TASK = new TaskKey( 1, 3 );

   public static final TaskTaskKey BLOCK_TASK_DEFN = new TaskTaskKey( 100, 100 );

   public static final TaskTaskKey REQ_TASK_DEFN = new TaskTaskKey( 100, 101 );

   public static final InventoryKey INVENTORY = new InventoryKey( 10, 10 );

   public static final InventoryKey H_INVENTORY = new InventoryKey( 10, 11 );

   public static final RefTaskClassKey BLOCK_CLASS = new RefTaskClassKey( 10, "BLOCK" );


   /**
    * Tests that the query returns two actual tasks (block and requirement)
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQuery() throws Exception {
      DataSet lDs = execute();

      lDs.addSort( "dsString(sched_task_key)", true );
      lDs.filterAndSort();

      MxAssert.assertEquals( "Number of retrieved rows", 2, lDs.getRowCount() );

      lDs.first();

      assertEquals( BLOCK_ACTUAL_TASK, lDs.getKey( TaskKey.class, "sched_task_key" ) );
      assertEquals( BLOCK_TASK_DEFN, lDs.getKey( TaskTaskKey.class, "task_task_key" ) );
      assertEquals( RefTaskClassKey.BLOCK_CLASS_MODE_CD, lDs.getString( "class_mode_cd" ) );
      assertEquals( INVENTORY, lDs.getKey( InventoryKey.class, "inventory_key" ) );
      assertEquals( H_INVENTORY, lDs.getKey( InventoryKey.class, "h_inventory_key" ) );

      lDs.next();

      assertEquals( REQ_ACTUAL_TASK, lDs.getKey( TaskKey.class, "sched_task_key" ) );
      assertEquals( REQ_TASK_DEFN, lDs.getKey( TaskTaskKey.class, "task_task_key" ) );
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

      // Create ref task class term for BLOCK
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( BLOCK_CLASS, "task_class_db_id", "task_class_cd" );
      lArgs.add( "class_mode_cd", RefTaskClassKey.BLOCK_CLASS_MODE_CD );

      MxDataAccess.getInstance().executeInsert( "ref_task_class", lArgs );

      // Create a block task definition
      TaskTaskTable lTaskTaskTable = TaskTaskTable.create();
      lTaskTaskTable.setTaskClass( BLOCK_CLASS );
      lTaskTaskTable.setRecurringTaskBool( false );
      lTaskTaskTable.insert( BLOCK_TASK_DEFN );

      final TaskIntervalKey BLOCK_SCHEDULING_RULE =
            new TaskIntervalKey( BLOCK_TASK_DEFN, DataTypeKey.CDY, new PartNoKey( 1, 1 ) );

      // Create part scheduling rule for the block
      TaskInterval lTaskIntervalTable = TaskInterval.create( BLOCK_SCHEDULING_RULE );
      lTaskIntervalTable.insert();

      // Create a requirement task definition
      lTaskTaskTable = TaskTaskTable.create();
      lTaskTaskTable.setTaskClass( RefTaskClassKey.REQ );
      lTaskTaskTable.setRecurringTaskBool( false );
      lTaskTaskTable.insert( REQ_TASK_DEFN );

      final TaskIntervalKey REQ_SCHEDULING_RULE =
            new TaskIntervalKey( REQ_TASK_DEFN, DataTypeKey.CDY, new PartNoKey( 1, 1 ) );

      // Create part scheduling rule for the requirement
      lTaskIntervalTable = TaskInterval.create( REQ_SCHEDULING_RULE );
      lTaskIntervalTable.insert();

      // Create an inventory
      InvInvTable lInvInvTable = InvInvTable.create( INVENTORY );
      lInvInvTable.setHInvNo( H_INVENTORY );
      lInvInvTable.insert();

      // Create actual task for block
      SchedStaskTable lSchedStaskTable = SchedStaskTable.create( BLOCK_ACTUAL_TASK );
      lSchedStaskTable.setTaskTaskKey( BLOCK_TASK_DEFN );
      lSchedStaskTable.setMainInventory( INVENTORY );
      lSchedStaskTable.insert();

      // Create actual task event
      EvtEventTable lEvtEventTable = EvtEventTable.create( BLOCK_ACTUAL_TASK.getEventKey() );
      lEvtEventTable.setHistBool( false );
      lEvtEventTable.setStatus( RefEventStatusKey.ACTV );
      lEvtEventTable.setHEvent( WORK_PACKAGE.getEventKey() );
      lEvtEventTable.setNhEvent( WORK_PACKAGE.getEventKey() );
      lEvtEventTable.insert();

      // Create actual task for requirement
      lSchedStaskTable = SchedStaskTable.create( REQ_ACTUAL_TASK );
      lSchedStaskTable.setTaskTaskKey( REQ_TASK_DEFN );
      lSchedStaskTable.setMainInventory( INVENTORY );
      lSchedStaskTable.insert();

      // Create actual task event
      lEvtEventTable = EvtEventTable.create( REQ_ACTUAL_TASK.getEventKey() );
      lEvtEventTable.setHistBool( false );
      lEvtEventTable.setStatus( RefEventStatusKey.ACTV );
      lEvtEventTable.setNhEvent( BLOCK_ACTUAL_TASK.getEventKey() );
      lEvtEventTable.setHEvent( WORK_PACKAGE.getEventKey() );
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
      lArgs.add( WORK_PACKAGE, "aSchedDbId", "aSchedId" );

      // Execute the query
      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
