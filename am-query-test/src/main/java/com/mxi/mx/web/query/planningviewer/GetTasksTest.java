
package com.mxi.mx.web.query.planningviewer;

import java.util.Calendar;
import java.util.Date;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.table.evt.EvtSchedDeadTable;


/**
 * This class tests the query com.mxi.mx.web.query.planningviewer.GetTasks.qrx
 *
 * @author hzheng
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetTasksTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetTasksTest.class );
   }


   /**
    * TEST CASE: Get tasks when BLOCK task's due date is after its REQ task's due date.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testGetTasksWhenBlockTaskDueDateAfterReqTaskDueDate() {
      // ARRANGE
      Calendar lBlockDateCal = Calendar.getInstance();
      lBlockDateCal.add( Calendar.DATE, 6 );
      Date lBlockDate = lBlockDateCal.getTime();

      Calendar lReqDateCal = Calendar.getInstance();
      lReqDateCal.add( Calendar.DATE, 5 );

      long lReqDateL = lReqDateCal.getTimeInMillis();
      Date lReqDate = lReqDateCal.getTime();

      TaskKey lBlockTask = new TaskKey( 4650, 139673 );
      TaskKey lReqTask = new TaskKey( 4650, 139674 );
      DataTypeKey lDataType = new DataTypeKey( 0, 21 );

      // Create block task
      EvtSchedDeadTable lEvtSchedDead = EvtSchedDeadTable.create( lBlockTask, lDataType );
      lEvtSchedDead.delete();
      lEvtSchedDead.setDriver( true );
      lEvtSchedDead.setDeviationQt( 0.0 );
      lEvtSchedDead.setIntervalQt( 7.0 );
      lEvtSchedDead.setDeadlineDate( lBlockDate );
      lEvtSchedDead.setUsageRemaining( 7.0 );
      lEvtSchedDead.insert();

      // Create Req task
      lEvtSchedDead = EvtSchedDeadTable.create( lReqTask, lDataType );
      lEvtSchedDead.delete();
      lEvtSchedDead.setDriver( true );
      lEvtSchedDead.setDeviationQt( 0.0 );
      lEvtSchedDead.setIntervalQt( 5.0 );
      lEvtSchedDead.setDeadlineDate( lReqDate );
      lEvtSchedDead.setUsageRemaining( 5.0 );
      lEvtSchedDead.insert();

      // Search range
      int lDateCount = 30;

      // ACT
      QuerySet lQuerySet = this.getTasksWithoutSoftDeadlines( lDateCount );

      // ASSERT
      Assert.assertEquals( 1, lQuerySet.getRowCount() );
      Calendar lBlockDateCalFromDatabase = Calendar.getInstance();
      while ( lQuerySet.next() ) {

         lBlockDateCalFromDatabase.setTime( lQuerySet.getDate( "sched_dead_dt" ) );

         long lBlockDateCalFromDatabaseL = lBlockDateCalFromDatabase.getTimeInMillis();

         Assert.assertTrue( "dates compare in millseconds",
               Math.abs( lReqDateL - lBlockDateCalFromDatabaseL ) < 1000 );

      }
   }


   /**
    * TEST CASE: Get tasks when BLOCK task's due date is before its REQ task's due date.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testGetTasksWhenBlockTaskDueDateBeforeReqTaskDueDate() {
      // ARRANGE
      Calendar lBlockDateCal = Calendar.getInstance();
      lBlockDateCal.add( Calendar.DATE, 5 );
      long lBlockDateL = lBlockDateCal.getTimeInMillis();
      Date lBlockDate = lBlockDateCal.getTime();

      Calendar lReqDateCal = Calendar.getInstance();
      lReqDateCal.add( Calendar.DATE, 6 );

      Date lReqDate = lReqDateCal.getTime();

      TaskKey lBlockTask = new TaskKey( 4650, 139673 );
      TaskKey lReqTask = new TaskKey( 4650, 139674 );
      DataTypeKey lDataType = new DataTypeKey( 0, 21 );

      // Create BLOCK task
      EvtSchedDeadTable lEvtSchedDead = EvtSchedDeadTable.create( lBlockTask, lDataType );
      lEvtSchedDead.delete();
      lEvtSchedDead.setDriver( true );
      lEvtSchedDead.setDeviationQt( 0.0 );
      lEvtSchedDead.setIntervalQt( 7.0 );
      lEvtSchedDead.setDeadlineDate( lBlockDate );
      lEvtSchedDead.setUsageRemaining( 7.0 );
      lEvtSchedDead.insert();

      // Create REQ task
      lEvtSchedDead = EvtSchedDeadTable.create( lReqTask, lDataType );
      lEvtSchedDead.delete();
      lEvtSchedDead.setDeviationQt( 0.0 );
      lEvtSchedDead.setIntervalQt( 5.0 );
      lEvtSchedDead.setDeadlineDate( lReqDate );
      lEvtSchedDead.setUsageRemaining( 5.0 );
      lEvtSchedDead.insert();

      // Search range
      int lDateCount = 30;

      // ACT
      QuerySet lQuerySet = this.getTasksWithoutSoftDeadlines( lDateCount );

      // ASSERT
      Assert.assertEquals( 1, lQuerySet.getRowCount() );
      Calendar lBlockDateCalFromDatabase = Calendar.getInstance();
      while ( lQuerySet.next() ) {
         lBlockDateCalFromDatabase.setTime( lQuerySet.getDate( "sched_dead_dt" ) );

         long lBlockDateCalFromDatabaseL = lBlockDateCalFromDatabase.getTimeInMillis();

         Assert.assertTrue( "dates compare in millseconds",
               Math.abs( lBlockDateL - lBlockDateCalFromDatabaseL ) < 1000 );

      }
   }


   /**
    * Get tasks
    *
    * @param aDayCount
    *           the count of the dates to display
    *
    * @return The queryset after execution.
    */
   private QuerySet getTasksWithoutSoftDeadlines( int aDayCount ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aDayCount", aDayCount );

      return QuerySetFactory.getInstance()
            .executeQuery( "com.mxi.mx.web.query.planningviewer.GetTasks", lArgs );
   }
}
