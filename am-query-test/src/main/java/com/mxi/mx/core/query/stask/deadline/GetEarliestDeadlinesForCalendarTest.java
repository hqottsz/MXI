
package com.mxi.mx.core.query.stask.deadline;

import static com.mxi.mx.testing.matchers.MxMatchers.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assume.assumeThat;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.EventDeadlineKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.TaskKey;


/**
 * Ensure that the earliest calendar deadlines are returned
 */
@RunWith( Theories.class )
public final class GetEarliestDeadlinesForCalendarTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();


   @Before
   public void setUp() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
   }


   private static final InventoryKey INV = new InventoryKey( 4650, 1 );

   private static final TaskKey TASK_1 = new TaskKey( 4650, 1 );
   private static final TaskKey TASK_2 = new TaskKey( 4650, 2 );

   @DataPoint
   public static final double NO_DEVIATION = 0;
   @DataPoint
   public static final double ONE_DT_DEVIATION = 1;
   @DataPoint
   public static final double TWO_DT_DEVIATION = 2;

   @DataPoint
   public static final DataTypeKey DAY = DataTypeKey.CDY;
   @DataPoint
   public static final DataTypeKey MONTH = DataTypeKey.CMON;

   @DataPoint
   public static final Date NOW = date( 2000, 1, 1 );

   @DataPoint
   public static final Date TOMORROW = date( 2000, 1, 2 );


   /**
    * Ensures that if the due date is after, that the earlier deadline is returned
    *
    * @param aFirstDate
    *           the first initial date
    * @param aFirstDataType
    *           the first data type
    * @param aFirstDeviation
    *           the first deadline deviation
    * @param aSecondDate
    *           the second initial date
    * @param aSecondDataType
    *           the second data type
    * @param aSecondDeviation
    *           the second deadline deviation
    */
   @Theory
   @Test
   public void testDueDateAfter( Date aFirstDate, DataTypeKey aFirstDataType,
         double aFirstDeviation, Date aSecondDate, DataTypeKey aSecondDataType,
         double aSecondDeviation ) {
      assumeThat( dueDate( aFirstDate, aFirstDataType, aFirstDeviation ),
            is( greaterThan( dueDate( aSecondDate, aSecondDataType, aSecondDeviation ) ) ) );

      addDeadline( TASK_1, aFirstDataType, aFirstDate, aFirstDeviation );
      addDeadline( TASK_2, aSecondDataType, aSecondDate, aSecondDeviation );

      assertThat( earliestDeadline(), is( equalTo( TASK_2 ) ) );
   }


   /**
    * Ensures that if the due date is before, that the deadline is returned
    *
    * @param aFirstDate
    *           the first initial date
    * @param aFirstDataType
    *           the first data type
    * @param aFirstDeviation
    *           the first deadline deviation
    * @param aSecondDate
    *           the second initial date
    * @param aSecondDataType
    *           the second data type
    * @param aSecondDeviation
    *           the second deadline deviation
    */
   @Theory
   @Test
   public void testDueDateBefore( Date aFirstDate, DataTypeKey aFirstDataType,
         double aFirstDeviation, Date aSecondDate, DataTypeKey aSecondDataType,
         double aSecondDeviation ) {
      assumeThat( dueDate( aFirstDate, aFirstDataType, aFirstDeviation ),
            is( lessThan( dueDate( aSecondDate, aSecondDataType, aSecondDeviation ) ) ) );

      addDeadline( TASK_1, aFirstDataType, aFirstDate, aFirstDeviation );
      addDeadline( TASK_2, aSecondDataType, aSecondDate, aSecondDeviation );

      assertThat( earliestDeadline(), is( equalTo( TASK_1 ) ) );
   }


   /**
    * Ensures that usage-based deadlines are ignored
    */
   @Test
   public void testIgnoreUsageDeadline() {
      addDeadline( TASK_1, DataTypeKey.CDY, TOMORROW, NO_DEVIATION );
      addDeadline( TASK_2, DataTypeKey.HOURS, NOW, NO_DEVIATION );

      assertThat( earliestDeadline(), is( equalTo( TASK_1 ) ) );
   }


   /**
    * Ensures that if only one deadline exists, it is returned
    */
   @Test
   public void testOnlyDeadline() {
      addDeadline( TASK_1, DAY, NOW, NO_DEVIATION );

      assertThat( earliestDeadline(), is( equalTo( TASK_1 ) ) );
   }


   /**
    * Returns a date
    *
    * @param aYear
    *           the year
    * @param aMonth
    *           the month of year
    * @param aDate
    *           the day of month
    *
    * @return the date
    */
   private static Date date( int aYear, int aMonth, int aDate ) {
      Calendar lCalendar = new GregorianCalendar();
      lCalendar.set( aYear, aMonth, aDate, 0, 0, 0 );

      return lCalendar.getTime();
   }


   /**
    * Adds a deadline
    *
    * @param aTask
    *           the task
    * @param aDataType
    *           the data type
    * @param aDueDate
    *           the due date
    * @param aDeviationQt
    *           the deviation
    */
   private void addDeadline( TaskKey aTask, DataTypeKey aDataType, Date aDueDate,
         double aDeviationQt ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aTask, "event_db_id", "event_id" );
      lArgs.add( aDataType, "data_type_db_id", "data_type_id" );
      lArgs.add( "sched_dead_dt", aDueDate );
      lArgs.add( "deviation_qt", aDeviationQt );

      MxDataAccess.getInstance().executeInsert( EventDeadlineKey.TABLE_NAME, lArgs );
   }


   /**
    * Gets the due date for a given initial date, data type, and deviation
    *
    * @param aDate
    *           the initial date
    * @param aDataType
    *           the data type
    * @param aDeviation
    *           the deviation
    *
    * @return the due date
    */
   private Date dueDate( Date aDate, DataTypeKey aDataType, double aDeviation ) {
      Calendar lCalendar = Calendar.getInstance();
      lCalendar.setTime( aDate );
      if ( DAY.equals( aDataType ) ) {
         lCalendar.add( Calendar.DATE, ( int ) aDeviation );
      } else if ( MONTH.equals( aDataType ) ) {
         lCalendar.add( Calendar.MONTH, ( int ) aDeviation );
      }

      return lCalendar.getTime();
   }


   /**
    * The earliest deadline
    *
    * @return returns the earliest deadline for inventory
    */
   private TaskKey earliestDeadline() {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( INV, "aInvNoDbId", "aInvNoId" );

      QuerySet lQs = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
      if ( lQs.first() ) {
         return lQs.getKey( TaskKey.class, "event_db_id", "event_id" );
      } else {
         return null;
      }
   }
}
