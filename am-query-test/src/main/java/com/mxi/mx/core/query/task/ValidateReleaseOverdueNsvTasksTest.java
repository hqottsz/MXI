
package com.mxi.mx.core.query.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.EventDeadlineKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.EventKeyInterface;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskFlagsKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.evt.EvtSchedDeadTable;
import com.mxi.mx.core.table.sched.SchedStaskTable;
import com.mxi.mx.core.table.task.TaskTaskFlagsTable;
import com.mxi.mx.core.unittest.table.evt.EvtSchedDead;


/**
 * Test the validateReleaseOverdueNsvTasks query.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class ValidateReleaseOverdueNsvTasksTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static final Date OVERDUE_DATE;
   private static final Date ANOTHER_OVERDUE_DATE;

   static {
      Calendar lCalendar = Calendar.getInstance();

      lCalendar.set( 2012, 03, 17, 11, 00 );
      OVERDUE_DATE = lCalendar.getTime();

      lCalendar.set( Calendar.MONTH, 02 );
      ANOTHER_OVERDUE_DATE = lCalendar.getTime();
   }

   //
   // off-parent test data
   //
   private static final TaskTaskKey OFFPARENT_NSV_TASK_DEFN = new TaskTaskKey( 4650, 1 );

   private static final EventKey CA_OVERDUE_OFFPARENT_NSV_TASK_EVENT = new EventKey( 4650, 11 );
   private static final TaskKey CA_OVERDUE_OFFPARENT_NSV_TASK =
         new TaskKey( CA_OVERDUE_OFFPARENT_NSV_TASK_EVENT );
   private static final InventoryKey INV_OF_CA_OVERDUE_OFFPARENT_NSV_TASK =
         new InventoryKey( 4650, 1 );

   private static final EventKey CA_OVERDUE_OFFPARENT_NSV_TASK2_EVENT = new EventKey( 4650, 112 );
   private static final TaskKey CA_OVERDUE_OFFPARENT_NSV_TASK2 =
         new TaskKey( CA_OVERDUE_OFFPARENT_NSV_TASK2_EVENT );

   private static final EventKey US_OVERDUE_OFFPARENT_NSV_TASK_EVENT = new EventKey( 4650, 12 );
   private static final TaskKey US_OVERDUE_OFFPARENT_NSV_TASK =
         new TaskKey( US_OVERDUE_OFFPARENT_NSV_TASK_EVENT );
   private static final InventoryKey INV_OF_US_OVERDUE_OFFPARENT_NSV_TASK =
         new InventoryKey( 4650, 2 );

   private static final EventKey US_OVERDUE_OFFPARENT_NSV_TASK2_EVENT = new EventKey( 4650, 122 );
   private static final TaskKey US_OVERDUE_OFFPARENT_NSV_TASK2 =
         new TaskKey( CA_OVERDUE_OFFPARENT_NSV_TASK2_EVENT );

   //
   // off-wing test data
   //
   private static final TaskTaskKey OFFWING_NSV_TASK_DEFN = new TaskTaskKey( 4650, 3 );

   private static final InventoryKey INV_OF_CA_OVERDUE_OFFWING_NSV_TASK =
         new InventoryKey( 4650, 3 );

   private static final EventKey INVS_CA_OVERDUE_OFFWING_NSV_TASK_EVENT = new EventKey( 4650, 31 );
   private static final TaskKey INVS_CA_OVERDUE_OFFWING_NSV_TASK =
         new TaskKey( INVS_CA_OVERDUE_OFFWING_NSV_TASK_EVENT );

   private static final EventKey CHILD_INVS_CA_OVERDUE_OFFWING_NSV_TASK_EVENT =
         new EventKey( 4650, 32 );
   private static final TaskKey CHILD_INVS_CA_OVERDUE_OFFWING_NSV_TASK =
         new TaskKey( CHILD_INVS_CA_OVERDUE_OFFWING_NSV_TASK_EVENT );


   /**
    * Ensure that NSV tasks against child inventories do not get returned if they are marked
    * off-parent.
    */
   @Test
   public void testDoesNotReturn_Child_OffParent_NsvTasks() {

      // switch child inventory's Nsv Task to be defined off-Parent
      SchedStaskTable lStaskTable =
            SchedStaskTable.findByPrimaryKey( CHILD_INVS_CA_OVERDUE_OFFWING_NSV_TASK );
      TaskTaskKey lOrigTaskTaskKey = lStaskTable.getTaskTaskKey();
      lStaskTable.setTaskTaskKey( OFFPARENT_NSV_TASK_DEFN );
      lStaskTable.update();

      Set<TaskKey> lNsvTasks = execute( INV_OF_CA_OVERDUE_OFFWING_NSV_TASK, OVERDUE_DATE );
      assertEquals( 1, lNsvTasks.size() );
      assertTrue( lNsvTasks.contains( INVS_CA_OVERDUE_OFFWING_NSV_TASK ) );

      // reset db
      lStaskTable.setTaskTaskKey( lOrigTaskTaskKey );
      lStaskTable.update();
   }


   /**
    * Ensure that all overdue, uncompleted (non-historic), calendar based, off-parent, NSV tasks
    * against the inventory are returned.
    */
   @Test
   public void testReturns_All_Overdue_CalendarBased_OffParent_NsvTasks() {

      // add another target calendar based NSV task
      SchedStaskTable lStaskTable = SchedStaskTable.create( CA_OVERDUE_OFFPARENT_NSV_TASK2 );
      lStaskTable.setMainInventory( INV_OF_CA_OVERDUE_OFFPARENT_NSV_TASK );
      lStaskTable.setTaskTaskKey( OFFPARENT_NSV_TASK_DEFN );
      lStaskTable.insert();

      EvtSchedDeadTable lSchedDead =
            createBasicDeadline( CA_OVERDUE_OFFPARENT_NSV_TASK2, DataTypeKey.CDY, 0.0 );
      lSchedDead.setDeadlineDate( ANOTHER_OVERDUE_DATE );
      lSchedDead.update();

      Set<TaskKey> lNsvTasks = execute( INV_OF_CA_OVERDUE_OFFPARENT_NSV_TASK, OVERDUE_DATE );
      assertEquals( 2, lNsvTasks.size() );
      assertTrue( lNsvTasks.contains( CA_OVERDUE_OFFPARENT_NSV_TASK ) );
      assertTrue( lNsvTasks.contains( CA_OVERDUE_OFFPARENT_NSV_TASK2 ) );

      // reset db
      EvtSchedDeadTable
            .findByPrimaryKey(
                  new EventDeadlineKey( CA_OVERDUE_OFFPARENT_NSV_TASK2_EVENT, DataTypeKey.CDY ) )
            .delete();
      lStaskTable.delete();
   }


   /**
    * Ensure that all overdue, uncompleted (non-historic), calendar based, off-wing, NSV task
    * against any of the inventory in the inventory tree are returned.
    */
   @Test
   public void testReturns_All_Overdue_CalendarBased_OffWing_NsvTasks_AgainstInvTree() {

      Set<TaskKey> lNsvTasks = execute( INV_OF_CA_OVERDUE_OFFWING_NSV_TASK, OVERDUE_DATE );
      assertEquals( 2, lNsvTasks.size() );
      assertTrue( lNsvTasks.contains( CHILD_INVS_CA_OVERDUE_OFFWING_NSV_TASK ) );
      assertTrue( lNsvTasks.contains( INVS_CA_OVERDUE_OFFWING_NSV_TASK ) );
   }


   /**
    * Ensure that all overdue, uncompleted (non-historic), usage based, off-parent, NSV tasks
    * against the inventory are returned.
    */
   @Test
   public void testReturns_All_Overdue_UsageBased_OffParent_NsvTask() {

      // add another target usage based NSV task
      SchedStaskTable lStaskTable = SchedStaskTable.create( CA_OVERDUE_OFFPARENT_NSV_TASK2 );
      lStaskTable.setMainInventory( INV_OF_CA_OVERDUE_OFFPARENT_NSV_TASK );
      lStaskTable.setTaskTaskKey( OFFPARENT_NSV_TASK_DEFN );
      lStaskTable.insert();

      double lDeviationQt = 10.0;
      Double lUsageRemQt = -20.0;
      EvtSchedDeadTable lSchedDead =
            createBasicDeadline( US_OVERDUE_OFFPARENT_NSV_TASK2, DataTypeKey.CYCLES, lDeviationQt );
      lSchedDead.setUsageRemaining( lUsageRemQt );
      lSchedDead.update();

      Set<TaskKey> lNsvTasks = execute( INV_OF_US_OVERDUE_OFFPARENT_NSV_TASK, null );
      assertEquals( 1, lNsvTasks.size() );
      assertTrue( lNsvTasks.contains( US_OVERDUE_OFFPARENT_NSV_TASK ) );

      // reset db
      EvtSchedDeadTable
            .findByPrimaryKey(
                  new EventDeadlineKey( US_OVERDUE_OFFPARENT_NSV_TASK2_EVENT, DataTypeKey.CYCLES ) )
            .delete();
      lStaskTable.delete();
   }


   /**
    * Ensure that all overdue, uncompleted (non-historic), calendar and usage based, off-parent, NSV
    * tasks against the inventory are returned.
    */
   @Test
   public void testReturns_MixOf_Overdue_CalendarBasedAndUsageBased_OffParent_NsvTasks() {

      // Modify test CA_OVERDUE_OFFPARENT_NSV_TASK to be against the same inventory as
      // US_OVERDUE_OFFPARENT_NSV_TASK

      SchedStaskTable lStaskTable =
            SchedStaskTable.findByPrimaryKey( CA_OVERDUE_OFFPARENT_NSV_TASK );
      lStaskTable.setMainInventory( INV_OF_US_OVERDUE_OFFPARENT_NSV_TASK );
      lStaskTable.update();

      Set<TaskKey> lNsvTasks = execute( INV_OF_US_OVERDUE_OFFPARENT_NSV_TASK, OVERDUE_DATE );
      assertEquals( 2, lNsvTasks.size() );
      assertTrue( lNsvTasks.contains( CA_OVERDUE_OFFPARENT_NSV_TASK ) );
      assertTrue( lNsvTasks.contains( US_OVERDUE_OFFPARENT_NSV_TASK ) );

      // reset db
      lStaskTable.setMainInventory( INV_OF_CA_OVERDUE_OFFPARENT_NSV_TASK );
      lStaskTable.update();
   }


   /**
    * Ensure that an extended but still overdue, uncompleted (non-historic), calendar based,
    * off-parent, NSV task against the inventory is returned.
    */
   @Test
   public void testReturns_One_ExtendedButStillOverdue_CalendarBased_OffParent_NsvTask() {

      // extend the deadline of the task but have it still be overdue
      EvtSchedDeadTable lSchedDead = EvtSchedDeadTable.findByPrimaryKey(
            new EventDeadlineKey( CA_OVERDUE_OFFPARENT_NSV_TASK_EVENT, DataTypeKey.CDY ) );
      lSchedDead.setDeviationQt( 30.0 );
      lSchedDead.update();

      Set<TaskKey> lNsvTasks = execute( INV_OF_CA_OVERDUE_OFFPARENT_NSV_TASK, OVERDUE_DATE );
      assertEquals( 1, lNsvTasks.size() );
      assertTrue( lNsvTasks.contains( CA_OVERDUE_OFFPARENT_NSV_TASK ) );
   }


   /**
    * Ensure that an extended but still overdue, uncompleted (non-historic), usage based,
    * off-parent, NSV task against the inventory is returned.
    */
   @Test
   public void testReturns_One_ExtendedButStillOverdue_UsageBased_OffParent_NsvTask() {

      // extend the deadline of the task but have it still be overdue
      EvtSchedDeadTable lSchedDead = EvtSchedDeadTable.findByPrimaryKey(
            new EventDeadlineKey( US_OVERDUE_OFFPARENT_NSV_TASK_EVENT, DataTypeKey.CDY ) );
      lSchedDead.setDeviationQt( 10.0 );
      lSchedDead.update();

      Set<TaskKey> lNsvTasks = execute( INV_OF_US_OVERDUE_OFFPARENT_NSV_TASK, null );
      assertEquals( 1, lNsvTasks.size() );
      assertTrue( lNsvTasks.contains( US_OVERDUE_OFFPARENT_NSV_TASK ) );
   }


   /**
    * Ensure that an overdue, uncompleted (non-historic), calendar based, off-parent, NSV task
    * against the inventory is returned.
    */
   @Test
   public void testReturns_One_Overdue_CalendarBased_OffParent_NsvTask() {

      Set<TaskKey> lNsvTasks = execute( INV_OF_CA_OVERDUE_OFFPARENT_NSV_TASK, OVERDUE_DATE );
      assertEquals( 1, lNsvTasks.size() );
      assertTrue( lNsvTasks.contains( CA_OVERDUE_OFFPARENT_NSV_TASK ) );
   }


   /**
    * Ensure that an overdue, uncompleted (non-historic), usage based, off-parent, NSV task against
    * the inventory is returned.
    */
   @Test
   public void testReturns_One_Overdue_UsageBased_OffParent_NsvTask() {

      Set<TaskKey> lNsvTasks = execute( INV_OF_US_OVERDUE_OFFPARENT_NSV_TASK, null );
      assertEquals( 1, lNsvTasks.size() );
      assertTrue( lNsvTasks.contains( US_OVERDUE_OFFPARENT_NSV_TASK ) );
   }


   /**
    * Ensure that no NSV tasks are returned when there is a completed, overdue, calendar based,
    * off-parent, NSV task against the inventory.
    */
   @Test
   public void testReturnsNothingWhen_One_Completed_Overdue_CalendarBased_OffParent_NsvTask() {

      // set the task as being completed (historic)
      EvtSchedDead lSchedDead =
            new EvtSchedDead( CA_OVERDUE_OFFPARENT_NSV_TASK_EVENT, DataTypeKey.CDY );
      lSchedDead.setHistBoolRo( true );
      lSchedDead.update();

      Set<TaskKey> lNsvTasks = execute( INV_OF_CA_OVERDUE_OFFPARENT_NSV_TASK, OVERDUE_DATE );
      assertEquals( 0, lNsvTasks.size() );
   }


   /**
    * Ensure that no NSV tasks are returned when there is a completed, overdue, usage based,
    * off-parent, NSV task against the inventory.
    */
   @Test
   public void testReturnsNothingWhen_One_Completed_Overdue_UsageBased_OffParent_NsvTask() {

      // set the task as being completed (historic)
      EvtSchedDead lSchedDead = new EvtSchedDead(
            new EventDeadlineKey( US_OVERDUE_OFFPARENT_NSV_TASK_EVENT, DataTypeKey.CYCLES ) );
      lSchedDead.setHistBoolRo( true );
      lSchedDead.update();

      Set<TaskKey> lNsvTasks = execute( INV_OF_US_OVERDUE_OFFPARENT_NSV_TASK, null );
      assertEquals( 0, lNsvTasks.size() );
   }


   /**
    * Ensure that no NSV tasks are returned when there is an extended but not overdue, uncompleted
    * (non-historic), calendar based, off-parent, NSV task against the inventory.
    */
   @Test
   public void testReturnsNothingWhen_One_ExtendedAndNotOverdue_CalendarBased_OffParent_NsvTask() {

      // extend the deadline of the task such that it is not overdue
      EvtSchedDeadTable lSchedDead = EvtSchedDeadTable.findByPrimaryKey(
            new EventDeadlineKey( CA_OVERDUE_OFFPARENT_NSV_TASK_EVENT, DataTypeKey.CDY ) );
      lSchedDead.setDeviationQt( 365.0 );
      lSchedDead.update();

      Set<TaskKey> lNsvTasks = execute( INV_OF_CA_OVERDUE_OFFPARENT_NSV_TASK, OVERDUE_DATE );
      assertEquals( 0, lNsvTasks.size() );
   }


   /**
    * Ensure that no NSV tasks are returned when there is an extended but not overdue, uncompleted
    * (non-historic), usage based, off-parent, NSV task against the inventory.
    */
   @Test
   public void testReturnsNothingWhen_One_ExtendedAndNotOverdue_UsageBased_OffParent_NsvTask() {

      // extend the deadline of the task such that it is not overdue
      EvtSchedDeadTable lSchedDead = EvtSchedDeadTable.findByPrimaryKey(
            new EventDeadlineKey( US_OVERDUE_OFFPARENT_NSV_TASK_EVENT, DataTypeKey.CYCLES ) );
      lSchedDead.setDeviationQt( 30.0 );
      lSchedDead.update();

      Set<TaskKey> lNsvTasks = execute( INV_OF_US_OVERDUE_OFFPARENT_NSV_TASK, null );
      assertEquals( 0, lNsvTasks.size() );
   }


   /**
    * Ensure that no NSV tasks are returned when there is a non-overdue, uncompleted (non-historic),
    * calendar based, off-parent, NSV task against the inventory.
    */
   @Test
   public void testReturnsNothingWhen_One_NotOverdue_CalendarBased_OffParent_NsvTask() {

      // extend the deadline of the task such that it is not overdue
      Calendar lNotOverdueDate = Calendar.getInstance();
      lNotOverdueDate.setTime( OVERDUE_DATE );
      lNotOverdueDate.add( Calendar.YEAR, 1 );

      EvtSchedDeadTable lSchedDead = EvtSchedDeadTable.findByPrimaryKey(
            new EventDeadlineKey( CA_OVERDUE_OFFPARENT_NSV_TASK_EVENT, DataTypeKey.CDY ) );
      lSchedDead.setDeadlineDate( lNotOverdueDate.getTime() );
      lSchedDead.update();

      Set<TaskKey> lNsvTasks = execute( INV_OF_CA_OVERDUE_OFFPARENT_NSV_TASK, OVERDUE_DATE );
      assertEquals( 0, lNsvTasks.size() );
   }


   /**
    * Ensure that no NSV tasks are returned when there are no NSV tasks against the inventory.
    */
   @Test
   public void testReturnsNothingWhenNoNsvTasks() {

      // set the task to not be next shop visit
      TaskTaskFlagsTable lTaskDefnFlags =
            TaskTaskFlagsTable.findByPrimaryKey( new TaskTaskFlagsKey( OFFPARENT_NSV_TASK_DEFN ) );
      lTaskDefnFlags.setNSVBool( false );
      lTaskDefnFlags.update();

      Set<TaskKey> lNsvTasks = execute( INV_OF_CA_OVERDUE_OFFPARENT_NSV_TASK, OVERDUE_DATE );
      assertEquals( 0, lNsvTasks.size() );
   }


   /**
    * {@inheritDoc}
    */
   @Before
   public void setUp() throws Exception {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );

      // task_task_flags table gets auto populated via an insert trigger on task_task, so we need to
      // manually set the task defns as Next Shop Visit
      TaskTaskFlagsTable lTaskDefnFlags;

      lTaskDefnFlags =
            TaskTaskFlagsTable.findByPrimaryKey( new TaskTaskFlagsKey( OFFPARENT_NSV_TASK_DEFN ) );
      lTaskDefnFlags.setNSVBool( true );
      lTaskDefnFlags.update();

      lTaskDefnFlags =
            TaskTaskFlagsTable.findByPrimaryKey( new TaskTaskFlagsKey( OFFWING_NSV_TASK_DEFN ) );
      lTaskDefnFlags.setNSVBool( true );
      lTaskDefnFlags.update();
   }


   /**
    * Creates a basic deadline.
    *
    * @param aEventKey
    *           The primary key of the task
    * @param aDataTypeKey
    *           The data type
    * @param aDeviation
    *           The deviation quantity
    *
    * @return The created table row
    */
   private EvtSchedDeadTable createBasicDeadline( EventKeyInterface aEventKey,
         DataTypeKey aDataTypeKey, double aDeviation ) {
      EvtSchedDeadTable lEvtSchedDead =
            EvtSchedDeadTable.create( aEventKey.getEventKey(), aDataTypeKey );
      lEvtSchedDead.setDriver( true );
      lEvtSchedDead.setDeviationQt( aDeviation );
      lEvtSchedDead.setNotifyQt( 0.0 );
      lEvtSchedDead.setIntervalQt( 0.0 );
      lEvtSchedDead.setPrefixedQt( 0.0 );
      lEvtSchedDead.setPostfixedQt( 0.0 );
      lEvtSchedDead.insert();

      return lEvtSchedDead;
   }


   /**
    * Executes the query for a given inventory and overdue date.
    *
    * @param aInvKey
    *           inventory
    * @param aOverdueDate
    *           overdue date
    *
    * @return the set of next shop visit task
    */
   private Set<TaskKey> execute( InventoryKey aInvKey, Date aOverdueDate ) {
      Set<TaskKey> lWpKeys = new HashSet<TaskKey>();

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aInvKey, "aInvDbId", "aInvId" );
      lArgs.add( "aOverdueDate", aOverdueDate );

      QuerySet lQs = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            "com.mxi.mx.core.query.task.validateReleaseOverdueNsvTasks", lArgs );

      while ( lQs.next() ) {
         lWpKeys.add( lQs.getKey( TaskKey.class, "sched_db_id", "sched_id" ) );
      }

      return lWpKeys;
   }
}
