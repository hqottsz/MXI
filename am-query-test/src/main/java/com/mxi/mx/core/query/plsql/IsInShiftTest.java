
package com.mxi.mx.core.query.plsql;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.utils.DateUtils;


/**
 * Tests the isInShiftWithDate function
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class IsInShiftTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   private static final String AMERICA_MONTREAL_TZ = "America/Montreal";
   private static final String AUSTRALIA_SYDNEY_TZ = "Australia/Sydney";
   private static final String EUROPE_AMSTERDAM_TZ = "Europe/Amsterdam";

   private Object[][] iInShiftDifTimezone = { { // work package starts: Day 1, before shift. work
                                                // package ends: Day 1, during shift
         DateUtils.getDateTime( 2007, 01, 01, 8, 0, 0 ), // Event Start Time
         DateUtils.getDateTime( 2007, 01, 01, 10, 0, 0 ), // Event End time
         AUSTRALIA_SYDNEY_TZ, // timezone of the event
         21d, // Shift start hour
         12d // Shift duration
         },
         { // work package starts: Day 1, before shift. work package ends: Day 1, after shift
               DateUtils.getDateTime( 2007, 01, 01, 8, 0, 0 ), // Event Start Time
               DateUtils.getDateTime( 2007, 01, 01, 22, 0, 0 ), // Event End time
               AUSTRALIA_SYDNEY_TZ, // timezone of the event
               21d, // Shift start hour
               12d // Shift duration
         }, { // work package starts: Day 1, during shift. work package ends: Day 1, during shift
               DateUtils.getDateTime( 2007, 01, 01, 10, 0, 0 ), // Event Start Time
               DateUtils.getDateTime( 2007, 01, 01, 10, 30, 0 ), // Event End time
               AUSTRALIA_SYDNEY_TZ, // timezone of the event
               21d, // Shift start hour
               12d // Shift duration
         }, { // work package starts: Day 1, during shift. work package ends: Day 1, after shift
               DateUtils.getDateTime( 2007, 01, 01, 10, 0, 0 ), // Event Start Time
               DateUtils.getDateTime( 2007, 01, 01, 22, 0, 0 ), // Event End time
               AUSTRALIA_SYDNEY_TZ, // timezone of the event
               21d, // Shift start hour
               12d // Shift duration
         }, { // work package starts: Day 1, before shift. work package ends: Day 2, after shift
               DateUtils.getDateTime( 2007, 01, 01, 8, 0, 0 ), // Event Start Time
               DateUtils.getDateTime( 2007, 01, 02, 0, 30, 0 ), // Event End time
               AUSTRALIA_SYDNEY_TZ, // timezone of the event
               21d, // Shift start hour
               12d // Shift duration
         }, { // work package starts: Day 1, during shift. work package ends: Day 2, after shift
               DateUtils.getDateTime( 2007, 01, 01, 10, 0, 0 ), // Event Start Time
               DateUtils.getDateTime( 2007, 01, 02, 0, 30, 0 ), // Event End time
               AUSTRALIA_SYDNEY_TZ, // timezone of the event
               9d, // Shift start hour
               12d // Shift duration
         }, {

               // Shift spans two days: work package starts: Day 1, before shift.
               // work package ends: Day 1, during shift
               DateUtils.getDateTime( 2007, 01, 01, 20, 0, 0 ), // Event Start Time
               DateUtils.getDateTime( 2007, 01, 01, 21, 30, 0 ), // Event End time
               AUSTRALIA_SYDNEY_TZ, // timezone of the event
               9d, // Shift start hour
               12d // Shift duration
         }, {

               // Shift spans two days: work package starts: Day 1, during shift.
               // work package ends: Day 1, during shift
               DateUtils.getDateTime( 2007, 01, 01, 22, 0, 0 ), // Event Start Time
               DateUtils.getDateTime( 2007, 01, 01, 22, 30, 0 ), // Event End time
               AUSTRALIA_SYDNEY_TZ, // timezone of the event
               9d, // Shift start hour
               12d // Shift duration
         }, {

               // Shift spans two days: work package starts: Day 1, before shift.
               // work package ends: Day 2, during shift
               DateUtils.getDateTime( 2007, 01, 01, 20, 0, 0 ), // Event Start Time
               DateUtils.getDateTime( 2007, 01, 02, 0, 30, 0 ), // Event End time
               AUSTRALIA_SYDNEY_TZ, // timezone of the event
               9d, // Shift start hour
               12d // Shift duration
         }, {

               // Shift spans two days: work package starts: Day 1, before shift.
               // work package ends: Day 2, after shift
               DateUtils.getDateTime( 2007, 01, 01, 20, 0, 0 ), // Event Start Time
               DateUtils.getDateTime( 2007, 01, 02, 9, 30, 0 ), // Event End time
               AUSTRALIA_SYDNEY_TZ, // timezone of the event
               9d, // Shift start hour
               12d // Shift duration
         }, {

               // Shift spans two days: work package starts: Day 1, during shift.
               // work package ends: Day 2, during shift
               DateUtils.getDateTime( 2007, 01, 01, 22, 0, 0 ), // Event Start Time
               DateUtils.getDateTime( 2007, 01, 02, 0, 30, 0 ), // Event End time
               AUSTRALIA_SYDNEY_TZ, // timezone of the event
               9d, // Shift start hour
               12d // Shift duration
         }, {

               // Shift spans two days: work package starts: Day 1, during shift.
               // work package ends: Day 2, after shift
               DateUtils.getDateTime( 2007, 01, 01, 22, 0, 0 ), // Event Start Time
               DateUtils.getDateTime( 2007, 01, 02, 9, 30, 0 ), // Event End time
               AUSTRALIA_SYDNEY_TZ, // timezone of the event
               9d, // Shift start hour
               12d // Shift duration
         }, {

               // Shift spans two days: work package starts: Day 2, during shift.
               // work package ends: Day 2, during shift
               DateUtils.getDateTime( 2007, 01, 02, 0, 30, 0 ), // Event Start Time
               DateUtils.getDateTime( 2007, 01, 02, 1, 30, 0 ), // Event End time
               AUSTRALIA_SYDNEY_TZ, // timezone of the event
               9d, // Shift start hour
               12d // Shift duration
         }, {

               // Shift spans two days: work package starts: Day 2, during shift.
               // work package ends: Day 2, after shift
               DateUtils.getDateTime( 2007, 01, 02, 0, 30, 0 ), // Event Start Time
               DateUtils.getDateTime( 2007, 01, 02, 9, 30, 0 ), // Event End time
               AUSTRALIA_SYDNEY_TZ, // timezone of the event
               9d, // Shift start hour
               12d // Shift duration
         }, {

               // Shift spans two days: work package starts: Day 2, after shift.
               // work package ends: Day 2, during shift
               DateUtils.getDateTime( 2007, 01, 02, 9, 30, 0 ), // Event Start Time
               DateUtils.getDateTime( 2007, 01, 02, 21, 30, 0 ), // Event End time
               AUSTRALIA_SYDNEY_TZ, // timezone of the event
               9d, // Shift start hour
               12d // Shift duration
         }, {

               // Shift spans two days: work package starts: Day 2, during shift.
               // work package ends: Day 2, during shift
               DateUtils.getDateTime( 2007, 01, 02, 21, 30, 0 ), // Event Start Time
               DateUtils.getDateTime( 2007, 01, 02, 22, 30, 0 ), // Event End time
               AUSTRALIA_SYDNEY_TZ, // timezone of the event
               9d, // Shift start hour
               12d // Shift duration
         }, };

   private Object[][] iInShiftSameTimezone = { { // work package starts: Day 1, before shift. work
                                                 // package ends: Day 1, during shift
         DateUtils.getDateTime( 2007, 01, 01, 8, 0, 0 ), // Event Start Time
         DateUtils.getDateTime( 2007, 01, 01, 10, 0, 0 ), // Event End time
         AMERICA_MONTREAL_TZ, // timezone of the event
         9d, // Shift start hour
         12d // Shift duration
         },
         { // work package starts: Day 1, before shift. work package ends: Day 1, after shift
               DateUtils.getDateTime( 2007, 01, 01, 8, 0, 0 ), // Event Start Time
               DateUtils.getDateTime( 2007, 01, 01, 22, 0, 0 ), // Event End time
               AMERICA_MONTREAL_TZ, // timezone of the event
               9d, // Shift start hour
               12d // Shift duration
         }, { // work package starts: Day 1, during shift. work package ends: Day 1, during shift
               DateUtils.getDateTime( 2007, 01, 01, 10, 0, 0 ), // Event Start Time
               DateUtils.getDateTime( 2007, 01, 01, 10, 30, 0 ), // Event End time
               AMERICA_MONTREAL_TZ, // timezone of the event
               9d, // Shift start hour
               12d // Shift duration
         }, { // work package starts: Day 1, during shift. work package ends: Day 1, after shift
               DateUtils.getDateTime( 2007, 01, 01, 10, 0, 0 ), // Event Start Time
               DateUtils.getDateTime( 2007, 01, 01, 22, 0, 0 ), // Event End time
               AMERICA_MONTREAL_TZ, // timezone of the event
               9d, // Shift start hour
               12d // Shift duration
         }, { // work package starts: Day 1, before shift. work package ends: Day 2, after shift
               DateUtils.getDateTime( 2007, 01, 01, 8, 0, 0 ), // Event Start Time
               DateUtils.getDateTime( 2007, 01, 02, 0, 30, 0 ), // Event End time
               AMERICA_MONTREAL_TZ, // timezone of the event
               9d, // Shift start hour
               12d // Shift duration
         }, { // work package starts: Day 1, during shift. work package ends: Day 2, after shift
               DateUtils.getDateTime( 2007, 01, 01, 10, 0, 0 ), // Event Start Time
               DateUtils.getDateTime( 2007, 01, 02, 0, 30, 0 ), // Event End time
               AMERICA_MONTREAL_TZ, // timezone of the event
               9d, // Shift start hour
               12d // Shift duration
         }, {

               // Shift spans two days: work package starts: Day 1, before shift.
               // work package ends: Day 1, during shift
               DateUtils.getDateTime( 2007, 01, 01, 20, 0, 0 ), // Event Start Time
               DateUtils.getDateTime( 2007, 01, 01, 21, 30, 0 ), // Event End time
               AMERICA_MONTREAL_TZ, // timezone of the event
               21d, // Shift start hour
               12d // Shift duration
         }, {

               // Shift spans two days: work package starts: Day 1, during shift.
               // work package ends: Day 1, during shift
               DateUtils.getDateTime( 2007, 01, 01, 22, 0, 0 ), // Event Start Time
               DateUtils.getDateTime( 2007, 01, 01, 22, 30, 0 ), // Event End time
               AMERICA_MONTREAL_TZ, // timezone of the event
               21d, // Shift start hour
               12d // Shift duration
         }, {

               // Shift spans two days: work package starts: Day 1, before shift.
               // work package ends: Day 2, during shift
               DateUtils.getDateTime( 2007, 01, 01, 20, 0, 0 ), // Event Start Time
               DateUtils.getDateTime( 2007, 01, 02, 0, 30, 0 ), // Event End time
               AMERICA_MONTREAL_TZ, // timezone of the event
               21d, // Shift start hour
               12d // Shift duration
         }, {

               // Shift spans two days: work package starts: Day 1, before shift.
               // work package ends: Day 2, after shift
               DateUtils.getDateTime( 2007, 01, 01, 20, 0, 0 ), // Event Start Time
               DateUtils.getDateTime( 2007, 01, 02, 9, 30, 0 ), // Event End time
               AMERICA_MONTREAL_TZ, // timezone of the event
               21d, // Shift start hour
               12d // Shift duration
         }, {

               // Shift spans two days: work package starts: Day 1, during shift.
               // work package ends: Day 2, during shift
               DateUtils.getDateTime( 2007, 01, 01, 22, 0, 0 ), // Event Start Time
               DateUtils.getDateTime( 2007, 01, 02, 0, 30, 0 ), // Event End time
               AMERICA_MONTREAL_TZ, // timezone of the event
               21d, // Shift start hour
               12d // Shift duration
         }, {

               // Shift spans two days: work package starts: Day 1, during shift.
               // work package ends: Day 2, after shift
               DateUtils.getDateTime( 2007, 01, 01, 22, 0, 0 ), // Event Start Time
               DateUtils.getDateTime( 2007, 01, 02, 9, 30, 0 ), // Event End time
               AMERICA_MONTREAL_TZ, // timezone of the event
               21d, // Shift start hour
               12d // Shift duration
         }, {

               // Shift spans two days: work package starts: Day 2, during shift.
               // work package ends: Day 2, during shift
               DateUtils.getDateTime( 2007, 01, 02, 0, 30, 0 ), // Event Start Time
               DateUtils.getDateTime( 2007, 01, 02, 1, 30, 0 ), // Event End time
               AMERICA_MONTREAL_TZ, // timezone of the event
               21d, // Shift start hour
               12d // Shift duration
         }, {

               // Shift spans two days: work package starts: Day 2, during shift.
               // work package ends: Day 2, after shift
               DateUtils.getDateTime( 2007, 01, 02, 0, 30, 0 ), // Event Start Time
               DateUtils.getDateTime( 2007, 01, 02, 9, 30, 0 ), // Event End time
               AMERICA_MONTREAL_TZ, // timezone of the event
               21d, // Shift start hour
               12d // Shift duration
         }, {

               // Shift spans two days: work package starts: Day 2, after shift.
               // work package ends: Day 2, during shift
               DateUtils.getDateTime( 2007, 01, 02, 9, 30, 0 ), // Event Start Time
               DateUtils.getDateTime( 2007, 01, 02, 21, 30, 0 ), // Event End time
               AMERICA_MONTREAL_TZ, // timezone of the event
               21d, // Shift start hour
               12d // Shift duration
         }, {

               // Shift spans two days: work package starts: Day 2, during shift.
               // work package ends: Day 2, during shift
               DateUtils.getDateTime( 2007, 01, 02, 21, 30, 0 ), // Event Start Time
               DateUtils.getDateTime( 2007, 01, 02, 22, 30, 0 ), // Event End time
               AMERICA_MONTREAL_TZ, // timezone of the event
               21d, // Shift start hour
               12d // Shift duration
         }, };
   private Object[][] iNotInShiftDifTimezone = { { // work package starts: Day 1, before shift. work
                                                   // package ends: Day 1, before shift
         DateUtils.getDateTime( 2007, 01, 01, 8, 0, 0 ), // Event Start Time
         DateUtils.getDateTime( 2007, 01, 01, 8, 30, 0 ), // Event End time
         EUROPE_AMSTERDAM_TZ, // timezone of the event
         15d, // Shift start hour
         12d // Shift duration
         },
         { // work package starts: Day 1, after shift. work package ends: Day 1, after shift
               DateUtils.getDateTime( 2007, 01, 01, 22, 0, 0 ), // Event Start Time
               DateUtils.getDateTime( 2007, 01, 01, 22, 30, 0 ), // Event End time
               EUROPE_AMSTERDAM_TZ, // timezone of the event
               15d, // Shift start hour
               12d // Shift duration
         }, { // work package starts: Day 1, after shift. work package ends: Day 2, after shift
               DateUtils.getDateTime( 2007, 01, 01, 22, 0, 0 ), // Event Start Time
               DateUtils.getDateTime( 2007, 01, 02, 0, 30, 0 ), // Event End time
               EUROPE_AMSTERDAM_TZ, // timezone of the event
               15d, // Shift start hour
               12d // Shift duration
         }, {

               // Shift spans two days: work package starts: Day 1, before shift. work package ends:
               // Day 1, before shift
               DateUtils.getDateTime( 2007, 01, 01, 20, 0, 0 ), // Event Start Time
               DateUtils.getDateTime( 2007, 01, 01, 20, 30, 0 ), // Event End time
               EUROPE_AMSTERDAM_TZ, // timezone of the event
               3d, // Shift start hour
               12d // Shift duration
         }, {

               // Shift spans two days: work package starts: Day 2, after shift.
               // work package ends: Day 2, after shift
               DateUtils.getDateTime( 2007, 01, 02, 9, 30, 0 ), // Event Start Time
               DateUtils.getDateTime( 2007, 01, 02, 10, 30, 0 ), // Event End time
               EUROPE_AMSTERDAM_TZ, // timezone of the event
               3d, // Shift start hour
               12d // Shift duration
         } };

   private Object[][] iNotInShiftSameTimezone = { { // work package starts: Day 1, before shift.
                                                    // work package ends: Day 1, before shift
         DateUtils.getDateTime( 2007, 01, 01, 8, 0, 0 ), // Event Start Time
         DateUtils.getDateTime( 2007, 01, 01, 8, 30, 0 ), // Event End time
         AMERICA_MONTREAL_TZ, // timezone of the event
         9d, // Shift start hour
         12d // Shift duration
         },
         { // work package starts: Day 1, after shift. work package ends: Day 1, after shift
               DateUtils.getDateTime( 2007, 01, 01, 22, 0, 0 ), // Event Start Time
               DateUtils.getDateTime( 2007, 01, 01, 22, 30, 0 ), // Event End time
               AMERICA_MONTREAL_TZ, // timezone of the event
               9d, // Shift start hour
               12d // Shift duration
         }, { // work package starts: Day 1, after shift. work package ends: Day 2, after shift
               DateUtils.getDateTime( 2007, 01, 01, 22, 0, 0 ), // Event Start Time
               DateUtils.getDateTime( 2007, 01, 02, 0, 30, 0 ), // Event End time
               AMERICA_MONTREAL_TZ, // timezone of the event
               9d, // Shift start hour
               12d // Shift duration
         }, {

               // Shift spans two days: work package starts: Day 1, before shift. work package ends:
               // Day 1, before shift
               DateUtils.getDateTime( 2007, 01, 01, 20, 0, 0 ), // Event Start Time
               DateUtils.getDateTime( 2007, 01, 01, 20, 30, 0 ), // Event End time
               AMERICA_MONTREAL_TZ, // timezone of the event
               21d, // Shift start hour
               12d // Shift duration
         }, {

               // Shift spans two days: work package starts: Day 2, after shift.
               // work package ends: Day 2, after shift
               DateUtils.getDateTime( 2007, 01, 02, 9, 30, 0 ), // Event Start Time
               DateUtils.getDateTime( 2007, 01, 02, 10, 30, 0 ), // Event End time
               AMERICA_MONTREAL_TZ, // timezone of the event
               21d, // Shift start hour
               12d // Shift duration
         } };


   /**
    * Creates the database data
    */
   public void createTestData() {
      // Do nothing.
   }


   /**
    * Tests various conditions when shift overlaps with the event. Shift start hour is specified in
    * a timezone different than the timezone of the event.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testInShiftDifTimezone() throws Exception {
      assertResult( iInShiftDifTimezone, 1 );
   }


   /**
    * Tests various conditions when shift overlaps with the event. Shift start hour is specified in
    * the same timezone as the timezone of the event.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testInShiftSameTimezone() throws Exception {
      assertResult( iInShiftSameTimezone, 1 );
   }


   /**
    * Tests various conditions when shift does not overlap with the event. Shift start hour is
    * specified in the timezone different than the timezone of the event.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testNotInShiftDifTimezone() throws Exception {
      assertResult( iNotInShiftDifTimezone, 0 );
   }


   /**
    * Tests various conditions when shift does not overlap with the event. Shift start hour is
    * specified in the same timezone as the timezone of the event.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testNotInShiftSameTimezone() throws Exception {
      assertResult( iNotInShiftSameTimezone, 0 );
   }


   /**
    * Executs the function under tests and asserts the result
    *
    * @param aTestData
    *           data to be used in the test
    * @param aExpectedResult
    *           expected result
    *
    * @throws Exception
    *            if an error occurs
    */
   private void assertResult( Object[][] aTestData, int aExpectedResult ) throws Exception {
      int lIndex = 0;
      for ( Object[] lData : aTestData ) {
         int lResult = this.execute( ( Date ) lData[0], ( Date ) lData[1], ( String ) lData[2],
               ( Double ) lData[3], ( Double ) lData[4] );
         assertEquals( "Iteration: " + lIndex, aExpectedResult, lResult );
         lIndex++;
      }
   }


   /**
    * Execute the function.
    *
    * @param aEventStartGdt
    *           event start date
    * @param aEventEndGdt
    *           event end date
    * @param aTimeZone
    *           time zone of the shift start hour
    * @param aShiftStartHr
    *           shift start hour
    * @param aShiftDurationHr
    *           shift duration
    *
    * @return 1 - the specified event is in the specified shift, 0 - otherwise
    *
    * @throws Exception
    *            if an error occurs.
    */
   private int execute( Date aEventStartGdt, Date aEventEndGdt, String aTimeZone,
         Double aShiftStartHr, Double aShiftDurationHr ) throws Exception {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aEventStartGdt", aEventStartGdt );
      lArgs.add( "aEventEndGdt", aEventEndGdt );
      lArgs.add( "aTimeZone", aTimeZone );
      lArgs.add( "aShiftStartHr", aShiftStartHr );
      lArgs.add( "aShiftDuratioHr", aShiftDurationHr );

      String[] lParmOrder =
            { "aEventStartGdt", "aEventEndGdt", "aTimeZone", "aShiftStartHr", "aShiftDuratioHr" };

      // Execute the query
      return Integer
            .parseInt( QueryExecutor.executeFunction( sDatabaseConnectionRule.getConnection(),
                  QueryExecutor.getFunctionName( getClass() ), lParmOrder, lArgs ) );
   }
}
