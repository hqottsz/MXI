
package com.mxi.mx.core.query.plsql;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.domain.builder.TaskRevisionBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.evt.EvtSchedDeadTable;


/**
 * Tests the HasOverdueNsvTasks function
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class HasOverdueNsvTasksTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private TaskTaskKey iNonNsvTaskRev;
   private TaskTaskKey iNsvTaskRev;
   private InventoryKey iRootInv;
   private InventoryKey iSubInv;


   /**
    * Ensures that the function returns true when the inventory has many overdue NSV tasks.
    *
    * @throws Exception
    */
   @Test
   public void testWhenInvHasManyOverdueNsvTasks() throws Exception {
      TaskKey lTask1 =
            new TaskBuilder().onInventory( iRootInv ).withTaskRevision( iNsvTaskRev ).build();

      addOverdueCalendarDeadline( lTask1 );

      TaskKey lTask2 =
            new TaskBuilder().onInventory( iRootInv ).withTaskRevision( iNsvTaskRev ).build();

      addOverdueCalendarDeadline( lTask2 );

      assertTrue( "Should return true when inventory has many overdue NSV tasks.",
            hasOverdueNsvTasks( iRootInv ) );
   }


   /**
    * Ensures that the function returns false when the inventory has an calendar based, NSV task
    * that is not overdue.
    *
    * @throws Exception
    */
   @Test
   public void testWhenInvHasOneCalendarBasedNsvTaskThatIsNotOverdue() throws Exception {
      TaskKey lTask =
            new TaskBuilder().onInventory( iRootInv ).withTaskRevision( iNsvTaskRev ).build();

      addNonOverdueCalendarDeadline( lTask );

      assertFalse(
            "Should return false when inventory has one calendar based, NSV task that is not overdue.",
            hasOverdueNsvTasks( iRootInv ) );
   }


   /**
    * Ensures that the function returns false when the inventory has an overdue NSV task that is
    * historic.
    *
    * @throws Exception
    */
   @Test
   public void testWhenInvHasOneHistoricOverdueNsvTask() throws Exception {
      TaskKey lTask = new TaskBuilder().onInventory( iRootInv ).withTaskRevision( iNsvTaskRev )
            .asHistoric().build();

      addOverdueCalendarDeadline( lTask );

      assertFalse( "Should return false when inventory has an overdue NSV task that is historic.",
            hasOverdueNsvTasks( iRootInv ) );
   }


   /**
    * Ensures that the function returns true when the inventory has an overdue, calendar based, NSV
    * task.
    *
    * @throws Exception
    */
   @Test
   public void testWhenInvHasOneOverdueCalendarBasedNsvTask() throws Exception {
      TaskKey lTask =
            new TaskBuilder().onInventory( iRootInv ).withTaskRevision( iNsvTaskRev ).build();

      addOverdueCalendarDeadline( lTask );

      assertTrue( "Should return true when inventory has one overdue, calendar based, NSV task.",
            hasOverdueNsvTasks( iRootInv ) );
   }


   /**
    * Ensures that the function returns false when the inventory has an overdue task that is not an
    * NSV task.
    *
    * @throws Exception
    */
   @Test
   public void testWhenInvHasOneOverdueTaskButNoOverdueNsvTasks() throws Exception {
      TaskKey lTask =
            new TaskBuilder().onInventory( iRootInv ).withTaskRevision( iNonNsvTaskRev ).build();

      addOverdueCalendarDeadline( lTask );

      assertFalse(
            "Should return false when inventory has one overdue task but no overdue NSV task.",
            hasOverdueNsvTasks( iRootInv ) );
   }


   /**
    * Ensures that the function returns true when the inventory has an overdue, usage based, NSV
    * task.
    *
    * @throws Exception
    */
   @Test
   public void testWhenInvHasOneOverdueUsageBasedNsvTask() throws Exception {
      TaskKey lTask =
            new TaskBuilder().onInventory( iRootInv ).withTaskRevision( iNsvTaskRev ).build();

      addOverdueUsageDeadline( lTask );

      assertTrue( "Should return true when inventory has one overdue, usage based, NSV task.",
            hasOverdueNsvTasks( iRootInv ) );
   }


   /**
    * Ensures that the function returns false when the inventory has an usage based, NSV task that
    * is not overdue.
    *
    * @throws Exception
    */
   @Test
   public void testWhenInvHasOneUsageBasedNsvTaskThatIsNotOverdue() throws Exception {
      TaskKey lTask =
            new TaskBuilder().onInventory( iRootInv ).withTaskRevision( iNsvTaskRev ).build();

      addNonOverdueUsageDeadline( lTask );

      assertFalse(
            "Should return false when inventory has one calendar based, NSV task that is not overdue.",
            hasOverdueNsvTasks( iRootInv ) );
   }


   /**
    * Ensures that the function returns true when the sub-inventory of the inventory has an overdue
    * NSV task.
    *
    * @throws Exception
    */
   @Test
   public void testWhenSubInvHasOneOverdueNsvTask() throws Exception {
      TaskKey lTask =
            new TaskBuilder().onInventory( iSubInv ).withTaskRevision( iNsvTaskRev ).build();

      addOverdueCalendarDeadline( lTask );

      assertTrue( "Should return true when sub-inventory has one overdue NSV task.",
            hasOverdueNsvTasks( iRootInv ) );
   }


   /**
    * {@inheritDoc}
    */
   @Before
   public void setUp() throws Exception {
      // setup a root inventory
      iRootInv = new InventoryBuilder().build();

      // setup a sub inventory
      iSubInv = new InventoryBuilder().withHighestInventory( iRootInv ).build();

      // setup a next shop visit task definition revistion
      iNsvTaskRev = new TaskRevisionBuilder().isNextShopVisit().build();

      // setup a regular task definition revistion (not a next shop visit)
      iNonNsvTaskRev = new TaskRevisionBuilder().build();
   }


   /**
    * Adds a deadline to the task.
    *
    * @param aTask
    *           task to which the deadline is added
    * @param aDataType
    *           data type of the deadline
    * @param aDeviation
    *           deviation of the deadline, optional when data type is not usage based
    * @param aUsageRemaining
    *           usage remaining of the deadline, optional when data type is not usage based
    * @param aDueDate
    *           due date of the deadline, optional when data type is not calendar based
    * @param aDrivingDeadline
    *           flag indicating if the deadline is the driving deadline
    */
   private void addDeadline( TaskKey aTask, DataTypeKey aDataType, Double aDeviation,
         Double aUsageRemaining, Date aDueDate, boolean aDrivingDeadline ) {
      Double lDeviation = ( aDeviation == null ) ? 0.0 : aDeviation;

      EvtSchedDeadTable lEvtSchedDead = EvtSchedDeadTable.create( aTask, aDataType );
      lEvtSchedDead.setDriver( aDrivingDeadline );
      lEvtSchedDead.setDeviationQt( lDeviation );
      lEvtSchedDead.setNotifyQt( 0.0 );
      lEvtSchedDead.setIntervalQt( 1.0 );
      lEvtSchedDead.setPrefixedQt( 0.0 );
      lEvtSchedDead.setPostfixedQt( 0.0 );

      if ( aUsageRemaining != null ) {
         lEvtSchedDead.setUsageRemaining( aUsageRemaining );
      }

      if ( aDueDate != null ) {
         lEvtSchedDead.setDeadlineDate( aDueDate );
      }

      lEvtSchedDead.insert();
   }


   /**
    * Adds an calendar based (CDY) deadline, that is not overdue, to the provided task.
    *
    * @param aTask
    *           task to which the deadline is added
    */
   private void addNonOverdueCalendarDeadline( TaskKey aTask ) {

      // set the due date in the future to ensure the task is not overdue
      Calendar lFutureDueDate = Calendar.getInstance();
      lFutureDueDate.add( Calendar.YEAR, 10 );

      addDeadline( aTask, DataTypeKey.CDY, null, null, lFutureDueDate.getTime(), true );
   }


   /**
    * Adds an usage based (CYCLES) deadline, that is not overdue, to the provided task.
    *
    * @param aTask
    *           task to which the deadline is added
    */
   private void addNonOverdueUsageDeadline( TaskKey aTask ) {

      // a usage based deadline with usage remaining = 20 and deviation = 10, is overdue
      // referring to equation; (-1)*vw_drv_deadline.usage_rem_qt >= vw_drv_deadline.deviation_qt

      Double lUsageRemaining = 20.0;
      Double lDeviation = 10.0;

      addDeadline( aTask, DataTypeKey.CYCLES, lDeviation, lUsageRemaining, null, true );
   }


   /**
    * Adds an overdue, calendar based (CDY) deadline to the provided task.
    *
    * @param aTask
    *           task to which the deadline is added
    */
   private void addOverdueCalendarDeadline( TaskKey aTask ) {

      // set the due date in the past to ensure the task is overdue
      Calendar lPastDueDate = Calendar.getInstance();
      lPastDueDate.set( Calendar.YEAR, 2000 );

      addDeadline( aTask, DataTypeKey.CDY, null, null, lPastDueDate.getTime(), true );
   }


   /**
    * Adds an overdue, usage based (CYCLES) deadline to the provided task.
    *
    * @param aTask
    *           task to which the deadline is added
    */
   private void addOverdueUsageDeadline( TaskKey aTask ) {

      // a usage based deadline with usage remaining = 0 and deviation = 0, is overdue
      // referring to equation; (-1)*vw_drv_deadline.usage_rem_qt >= vw_drv_deadline.deviation_qt
      Double lUsageRemaining = 0.0;
      Double lDeviation = 0.0;

      addDeadline( aTask, DataTypeKey.CYCLES, lDeviation, lUsageRemaining, null, true );
   }


   /**
    * Executes the hasOverdueNsvTasks pl/sql function
    *
    * @param aInventory
    *           pk of root inventory
    *
    * @return TRUE if the function returns 1, otherwise FALSE
    *
    * @throws Exception
    *            unexpected exception occurred
    */
   private boolean hasOverdueNsvTasks( InventoryKey aInventory ) throws Exception {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aInventory, "h_inv_no_db_id", "h_inv_db_id" );

      String lResult = QueryExecutor.executeFunction( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getFunctionName( getClass() ),
            new String[] { "h_inv_no_db_id", "h_inv_db_id" }, lArgs );

      return "1".equals( lResult );
   }
}
