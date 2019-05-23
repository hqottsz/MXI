
package com.mxi.mx.core.query.stask.deadline;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;

import java.math.BigDecimal;

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
 * Ensure that the earliest calendar deadlines are returned.
 *
 * Calendar deadlines ordering is based on their "extended usage remaining" (usage remaining +
 * deviation). These values may be negative, zero, or positive.
 *
 * Therefore, a deadline with the lesser "extended usage remaining" is considered the earliest. If
 * their "extended usage remaining" are equal then they are both considered the earliest and either
 * one may be returned from the query.
 *
 */
@RunWith( Theories.class )
public final class GetEarliestDeadlinesForUsageTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();


   @Before
   public void setUp() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
   }


   private static final InventoryKey INV = new InventoryKey( 4650, 1 );

   private static final TaskKey TASK_1 = new TaskKey( 4650, 1 );
   private static final TaskKey TASK_2 = new TaskKey( 4650, 2 );

   // Problem; the usage-remaining and the deviation are both stored as doubles but I want to use
   // two different datapoint sets for each of them.
   //
   // Solution chosen; use one set of double datapoints and another set of BigDecimal datapoints,
   // then convert
   // the BigDecimal to a double when used.

   @DataPoint
   public static final double USAGE_REMAINING_NEGATIVE = -10d;
   @DataPoint
   public static final double USAGE_REMAINING_ZERO = 0d;
   @DataPoint
   public static final double USAGE_REMAINING_POSITIVE = 10d;

   @DataPoint
   public static final BigDecimal DEVIATION_ZERO = new BigDecimal( 0 );
   @DataPoint
   public static final BigDecimal DEVIATION_POSITIVE = new BigDecimal( 5 );


   /**
    * Ensures that if the extended usage remaining is after, that the earlier deadline is returned.
    *
    */
   @Theory
   @Test
   public void testExtUsageRemainingAfter( double aFirstUsageRemaining, BigDecimal aFirstDeviation,
         double aSecondUsageRemaining, BigDecimal aSecondDeviation ) {

      assumeThat( extendedUsageRemaining( aFirstUsageRemaining, aFirstDeviation ), is(
            greaterThan( extendedUsageRemaining( aSecondUsageRemaining, aSecondDeviation ) ) ) );

      addDeadline( TASK_1, aFirstUsageRemaining, aFirstDeviation );
      addDeadline( TASK_2, aSecondUsageRemaining, aSecondDeviation );

      assertThat( earliestDeadline(), is( equalTo( TASK_2 ) ) );
   }


   /**
    * Ensures that if the extended usage remaining is before, that the earlier deadline is returned.
    *
    */
   @Theory
   @Test
   public void testExtUsageRemainingBefore( double aFirstUsageRemaining, BigDecimal aFirstDeviation,
         double aSecondUsageRemaining, BigDecimal aSecondDeviation ) {

      assumeThat( extendedUsageRemaining( aFirstUsageRemaining, aFirstDeviation ),
            is( lessThan( extendedUsageRemaining( aSecondUsageRemaining, aSecondDeviation ) ) ) );

      addDeadline( TASK_1, aFirstUsageRemaining, aFirstDeviation );
      addDeadline( TASK_2, aSecondUsageRemaining, aSecondDeviation );

      assertThat( earliestDeadline(), is( equalTo( TASK_1 ) ) );
   }


   /**
    * Ensures that if the extended usage remaining are equal, that either deadline is returned.
    *
    */
   @Theory
   @Test
   public void testExtUsageRemainingEqual( double aFirstUsageRemaining, BigDecimal aFirstDeviation,
         double aSecondUsageRemaining, BigDecimal aSecondDeviation ) {

      assumeThat( extendedUsageRemaining( aFirstUsageRemaining, aFirstDeviation ),
            is( equalTo( extendedUsageRemaining( aSecondUsageRemaining, aSecondDeviation ) ) ) );

      addDeadline( TASK_1, aFirstUsageRemaining, aFirstDeviation );
      addDeadline( TASK_2, aSecondUsageRemaining, aSecondDeviation );

      assertThat( earliestDeadline(), anyOf( is( TASK_1 ), is( TASK_2 ) ) );
   }


   /**
    * Ensures that if only one deadline exists, it is returned
    */
   @Test
   public void testOnlyDeadline() {

      addDeadline( TASK_1, USAGE_REMAINING_POSITIVE, DEVIATION_ZERO );

      assertThat( earliestDeadline(), is( equalTo( TASK_1 ) ) );
   }


   /**
    * Gets the extended usage remaining.
    *
    */
   private double extendedUsageRemaining( double aUsageRemaining, BigDecimal aDeviation ) {
      return aUsageRemaining + aDeviation.doubleValue();
   }


   /**
    * Add a deadline to the task.
    *
    */
   private void addDeadline( TaskKey aTask, double aUsageRemaining, BigDecimal aDeviation ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aTask, "event_db_id", "event_id" );
      lArgs.add( DataTypeKey.CYCLES, "data_type_db_id", "data_type_id" );
      lArgs.add( "usage_rem_qt", aUsageRemaining );
      lArgs.add( "deviation_qt", aDeviation.doubleValue() );

      MxDataAccess.getInstance().executeInsert( EventDeadlineKey.TABLE_NAME, lArgs );
   }


   /**
    * The earliest deadline
    *
    * @return returns the earliest deadline for inventory
    */
   private TaskKey earliestDeadline() {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( INV, "aInvNoDbId", "aInvNoId" );
      lArgs.add( "aDataTypeCd", "CYCLES" );

      QuerySet lQs = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
      if ( lQs.first() ) {
         return lQs.getKey( TaskKey.class, "event_db_id", "event_id" );
      } else {
         return null;
      }
   }

}
