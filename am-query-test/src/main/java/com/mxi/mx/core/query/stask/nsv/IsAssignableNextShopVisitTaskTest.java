
package com.mxi.mx.core.query.stask.nsv;

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
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.EventDeadlineKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskFlagsKey;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.evt.EvtSchedDeadTable;
import com.mxi.mx.core.table.task.TaskTaskFlagsTable;


/**
 * Tests the IsAssignableNextShopVisitTask query.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class IsAssignableNextShopVisitTaskTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @Before
   public void setUp() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
   }


   private static final TaskKey ASSIGNABLE_NO_DEADLINE_NSV_TASK = new TaskKey( 4650, 1 );
   private static final TaskKey ASSIGNABLE_OVERDUE_CA_DEADLINE_NSV_TASK = new TaskKey( 4650, 2 );
   private static final TaskKey ASSIGNABLE_OVERDUE_US_DEADLINE_NSV_TASK = new TaskKey( 4650, 3 );
   private static final TaskKey ANOTHER_TASK = new TaskKey( 4650, 4 );

   private static final TaskTaskFlagsKey ASSIGNABLE_NO_DEADLINE_NSV_TASK_FLAGS =
         new TaskTaskFlagsKey( 4650, 1 );

   private static final EventDeadlineKey ASSIGNABLE_OVERDUE_CA_DEADLINE_KEY = new EventDeadlineKey(
         ASSIGNABLE_OVERDUE_CA_DEADLINE_NSV_TASK.getEventKey(), DataTypeKey.CDY );

   private static final EventDeadlineKey ASSIGNABLE_OVERDUE_US_DEADLINE_KEY = new EventDeadlineKey(
         ASSIGNABLE_OVERDUE_US_DEADLINE_NSV_TASK.getEventKey(), DataTypeKey.CYCLES );


   /**
    * Ensure that the query does not return true when an NSV task is un-assignable due to task not
    * being next shop visit.
    */
   @Test
   public void testReturnNothingWhenNotAnNsvTask() {

      // setup: modify task to not be a next shop visit task
      TaskTaskFlagsTable lFlagsTable =
            TaskTaskFlagsTable.findByPrimaryKey( ASSIGNABLE_NO_DEADLINE_NSV_TASK_FLAGS );
      lFlagsTable.setNSVBool( false );
      lFlagsTable.update();

      // execute and verify the test
      assertFalse( execute( ASSIGNABLE_NO_DEADLINE_NSV_TASK ) );
   }


   /**
    * Ensure that the query does not return true when an NSV task is already assigned to another
    * task.
    */
   @Test
   public void testReturnNothingWhenNsvTaskIsAlreadyAssigned() {

      // setup: modify task to be assigned to another task
      EvtEventTable lEventTable =
            EvtEventTable.findByPrimaryKey( ASSIGNABLE_NO_DEADLINE_NSV_TASK.getEventKey() );
      lEventTable.setHEvent( ANOTHER_TASK.getEventKey() );
      lEventTable.update();

      // execute and verify the test
      assertFalse( execute( ASSIGNABLE_NO_DEADLINE_NSV_TASK ) );
   }


   /**
    * Ensure that the query does not return true when an NSV task has a calendar based deadline that
    * is not yet due.
    */
   @Test
   public void testReturnsNothingWhenNsvTaskHasCalendarBasedDeadlineNotYetDue() {

      // setup: modify the scheduled deadline to be after the current date
      EvtSchedDeadTable lSchedDead =
            EvtSchedDeadTable.findByPrimaryKey( ASSIGNABLE_OVERDUE_CA_DEADLINE_KEY );
      Calendar lFutureDeadline = Calendar.getInstance();
      lFutureDeadline.add( Calendar.MONTH, 1 );
      lSchedDead.setDeadlineDate( lFutureDeadline.getTime() );
      lSchedDead.update();

      // execute and verify the test
      assertFalse( execute( ASSIGNABLE_OVERDUE_CA_DEADLINE_NSV_TASK ) );
   }


   /**
    * Ensure that the query does not return true when an NSV task has a usage based deadline that is
    * not yet due.
    */
   @Test
   public void testReturnsNothingWhenNsvTaskHasUsageBasedDeadlineNotYetDue() {

      // setup: modify the scheduled deadline to have negative usage remaining less then the
      // deviation
      EvtSchedDeadTable lSchedDead =
            EvtSchedDeadTable.findByPrimaryKey( ASSIGNABLE_OVERDUE_US_DEADLINE_KEY );
      lSchedDead.setUsageRemaining( -10.0 );
      lSchedDead.update();

      // execute and verify the test
      assertFalse( execute( ASSIGNABLE_OVERDUE_US_DEADLINE_NSV_TASK ) );
   }


   /**
    * Ensure that the query returns true when an NSV task is assignable with no deadline.
    */
   @Test
   public void testReturnsTrueWhenNsvTaskIsAssignableWithNoDeadline() {
      assertTrue( execute( ASSIGNABLE_NO_DEADLINE_NSV_TASK ) );
   }


   /**
    * Ensure that the query returns true when an NSV task is assignable with an overdue calendar
    * based deadline.
    */
   @Test
   public void testReturnsTrueWhenNsvTaskIsAssignableWithOverdueCalendarBasedDeadline() {

      // setup: ensure the scheduled deadline is earlier then the current date
      EvtSchedDeadTable lSchedDead =
            EvtSchedDeadTable.findByPrimaryKey( ASSIGNABLE_OVERDUE_CA_DEADLINE_KEY );
      assertTrue( ( new Date() ).after( lSchedDead.getDeadlineDate() ) );

      // execute and verify the test
      assertTrue( execute( ASSIGNABLE_OVERDUE_CA_DEADLINE_NSV_TASK ) );
   }


   /**
    * Ensure that the query returns true when an NSV task is assignable with an overdue usage based
    * deadline.
    */
   @Test
   public void testReturnsTrueWhenNsvTaskIsAssignableWithOverdueUsageBasedDeadline() {
      assertTrue( execute( ASSIGNABLE_OVERDUE_US_DEADLINE_NSV_TASK ) );
   }


   /**
    * Executes the query for a given next shop visit task
    *
    * @param aNsvTaskKey
    *           the NSV task key
    *
    * @return true if query returns 1, otherwise false
    */
   private boolean execute( TaskKey aNsvTaskKey ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aNsvTaskKey, "aTaskDbId", "aTaskId" );

      QuerySet lQs = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      return lQs.first();
   }
}
