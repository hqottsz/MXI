
package com.mxi.mx.core.query.plsql;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.key.AircraftKey;
import com.mxi.mx.core.key.TaskKey;


/**
 * Tests the IsWithinPlanningYield PL/SQL function
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class IsWithinPlanningYieldTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   @ClassRule
   public static FakeJavaEeDependenciesRule sFakeJavaEeDependenciesRule =
         new FakeJavaEeDependenciesRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), IsWithinPlanningYieldTest.class );
   }


   /** Default Aircraft Key */
   private static final AircraftKey ACFT = new AircraftKey( 1, 1 );

   /** Task Key for Task with no Due Date deadline */
   private static final TaskKey TASK_NO_DUE_DATE = new TaskKey( 888, 8888 );

   /** Task Key for Task Tree with no Driving Deadlines */
   private static final TaskKey TASK_NO_DRIVING_DEADLINE = new TaskKey( 999, 9999 );

   /** Task Key for Task Tree where driving deadline has negative interval */
   private static final TaskKey TASK_NEGATIVE_INTERVAL = new TaskKey( 555, 5555 );

   /** Task Key for Task Tree where driving deadline task has no Min Planning Yield */
   private static final TaskKey TASK_NO_MIN_PLAN_YIELD = new TaskKey( 777, 7777 );

   /** Task Key for Task Tree where driving deadline is of type CDAY */
   private static final TaskKey TASK_YIELD_CDAY = new TaskKey( 4650, 10000 );

   /** Task Key for Task Tree where driving deadline is of type CMONTH */
   private static final TaskKey TASK_YIELD_CMON = new TaskKey( 4650, 20000 );


   /**
    * Test that when a Task with Deadlines has no Minimum Planning Yield specified, the function
    * returns 0
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testDefaultMinPlanningYield() throws Exception {

      // ACTION: Execute the function
      int lResult = execute( TASK_NO_MIN_PLAN_YIELD, ACFT,
            DateUtils.parseDefaultDateString( "16-MAY-2006 EST" ) );

      assertEquals( 1, lResult );
   }


   /**
    * Test that when a Task with a Negative Deadline Interval is found, the function returns 0
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testNegativeDeadlineInterval() throws Exception {

      // ACTION: Execute the function
      int lResult = execute( TASK_NEGATIVE_INTERVAL, ACFT,
            DateUtils.parseDefaultDateString( "16-MAY-2006 EST" ) );

      assertEquals( 0, lResult );
   }


   /**
    * Test that when a Task has no Driving Deadlines, the function returns 0
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testNoDrivingDeadline() throws Exception {

      // ACTION: Execute the function
      int lResult = execute( TASK_NO_DRIVING_DEADLINE, ACFT,
            DateUtils.parseDefaultDateString( "16-MAY-2006 EST" ) );

      assertEquals( 0, lResult );
   }


   /**
    * Test that when a Task has no Due Date specified, the function returns 0
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testNoDueDate() throws Exception {

      // ACTION: Execute the function
      int lResult = execute( TASK_NO_DUE_DATE, ACFT,
            DateUtils.parseDefaultDateString( "16-MAY-2006 EST" ) );

      // no due date
      assertEquals( 0, lResult );
   }


   /**
    * Test that when a Task with a Calendar Day Deadline is found, the function returns 1 if the
    * current date falls between the planning yield date and task due date
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testYieldCalendarDay() throws Exception {

      // ACTION: Execute the function
      int lResult =
            execute( TASK_YIELD_CDAY, ACFT, DateUtils.parseDateTimeString( "16-MAY-2006 19:00" ) );

      assertEquals( 1, lResult );
   }


   /**
    * Test that when a Task with a Calendar Month Deadline is found, the function returns 0 if the
    * current date falls between the planning yield date and task due date
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testYieldCalendarMonth() throws Exception {

      // ACTION: Execute the function
      int lResult =
            execute( TASK_YIELD_CMON, ACFT, DateUtils.parseDefaultDateString( "16-MAY-2006 EST" ) );

      assertEquals( 1, lResult );
   }


   /**
    * Execute the function.
    *
    * @param aTaskPK
    *           The root of a Task Tree.
    * @param aAircraftPK
    *           The Aircraft to which these tasks belong
    * @param aCurrentDate
    *           The current date
    *
    * @return the calculated Planning Yield Date.
    *
    * @throws Exception
    *            if an error occurs.
    */
   private int execute( TaskKey aTaskPK, AircraftKey aAircraftPK, Date aCurrentDate )
         throws Exception {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aTaskPK, new String[] { "aSchedDbId", "aSchedId" } );
      lArgs.add( aAircraftPK, new String[] { "aAcftDbId", "aAcftId" } );
      lArgs.add( "aCurrentDate", aCurrentDate );

      String[] lParmOrder = { "aSchedDbId", "aSchedId", "aAcftDbId", "aAcftId", "aCurrentDate" };

      // Execute the query
      return Integer
            .parseInt( QueryExecutor.executeFunction( sDatabaseConnectionRule.getConnection(),
                  QueryExecutor.getFunctionName( getClass() ), lParmOrder, lArgs ) );
   }
}
