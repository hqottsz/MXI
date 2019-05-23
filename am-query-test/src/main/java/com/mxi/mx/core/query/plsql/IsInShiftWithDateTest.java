
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
public final class IsInShiftWithDateTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   private static final String AMERICA_MONTREAL_TZ = "America/Montreal";
   private static final String EUROPE_AMSTERDAM_TZ = "Europe/Amsterdam";

   private Object[][] iInShiftDifTimeZone = { { // Event spans two days. Shift stats and ends during
                                                // the event
         DateUtils.getDateTime( 2012, 05, 13, 22, 0, 0 ), // Event Start Time
         DateUtils.getDateTime( 2012, 05, 14, 9, 0, 0 ), // Event End time
         DateUtils.getDateTime( 2012, 05, 14, 0, 0, 0 ), // Shift day
         EUROPE_AMSTERDAM_TZ, // timezone of the event
         2d, // Shift start hour (8-11 EST)
         3d // Shift duration
         },
         { // Event spans many days. Shift stats and ends during the event
               DateUtils.getDateTime( 2012, 05, 10, 22, 0, 0 ), // Event Start Time
               DateUtils.getDateTime( 2012, 05, 20, 6, 0, 0 ), // Event End time
               DateUtils.getDateTime( 2012, 05, 14, 0, 0, 0 ), // Shift day
               EUROPE_AMSTERDAM_TZ, // timezone of the event
               2d, // Shift start hour (8-11 EST)
               3d // Shift duration
         }, { // Event in one day, Shift stats and ends during the event
               DateUtils.getDateTime( 2012, 05, 13, 20, 30, 0 ), // Event Start Time
               DateUtils.getDateTime( 2012, 05, 13, 22, 30, 0 ), // Event End time
               DateUtils.getDateTime( 2012, 05, 14, 0, 0, 0 ), // Shift day
               EUROPE_AMSTERDAM_TZ, // timezone of the event
               2d, // Shift start hour
               3d // Shift duration
         }, { // Event in one day, shift starts during event but ends later
               DateUtils.getDateTime( 2012, 05, 14, 0, 0, 0 ), // Event Start Time
               DateUtils.getDateTime( 2012, 05, 14, 6, 0, 0 ), // Event End time
               DateUtils.getDateTime( 2012, 05, 14, 0, 0, 0 ), // Shift day
               EUROPE_AMSTERDAM_TZ, // timezone of the event
               5d, // Shift start hour
               3d // Shift duration
         }, { // Shift start day earlier, event starts during shift but ends after it
               DateUtils.getDateTime( 2012, 05, 13, 17, 0, 0 ), // Event Start Time
               DateUtils.getDateTime( 2012, 05, 13, 22, 0, 0 ), // Event End time
               DateUtils.getDateTime( 2012, 05, 13, 0, 0, 0 ), // Shift day
               EUROPE_AMSTERDAM_TZ, // timezone of the event
               22d, // Shift start hour
               4d // Shift duration
         } };

   private Object[][] iInShiftSameTimeZone = { { // Event spans two days. Shift stats and ends
                                                 // during the event
         DateUtils.getDateTime( 2012, 05, 13, 22, 0, 0 ), // Event Start Time
         DateUtils.getDateTime( 2012, 05, 14, 6, 0, 0 ), // Event End time
         DateUtils.getDateTime( 2012, 05, 14, 0, 0, 0 ), // Shift day
         AMERICA_MONTREAL_TZ, // timezone of the event
         2d, // Shift start hour
         3d // Shift duration
         },
         { // Event spans many days. Shift stats and ends during the event
               DateUtils.getDateTime( 2012, 05, 10, 22, 0, 0 ), // Event Start Time
               DateUtils.getDateTime( 2012, 05, 20, 6, 0, 0 ), // Event End time
               DateUtils.getDateTime( 2012, 05, 14, 0, 0, 0 ), // Shift day
               AMERICA_MONTREAL_TZ, // timezone of the event
               2d, // Shift start hour
               3d // Shift duration
         }, { // Event in one day, Shift stats and ends during the event
               DateUtils.getDateTime( 2012, 05, 14, 0, 0, 0 ), // Event Start Time
               DateUtils.getDateTime( 2012, 05, 14, 6, 0, 0 ), // Event End time
               DateUtils.getDateTime( 2012, 05, 14, 0, 0, 0 ), // Shift day
               AMERICA_MONTREAL_TZ, // timezone of the event
               2d, // Shift start hour
               3d // Shift duration
         }, { // Event in one day, shift starts during event but ends later
               DateUtils.getDateTime( 2012, 05, 14, 0, 0, 0 ), // Event Start Time
               DateUtils.getDateTime( 2012, 05, 14, 6, 0, 0 ), // Event End time
               DateUtils.getDateTime( 2012, 05, 14, 0, 0, 0 ), // Shift day
               AMERICA_MONTREAL_TZ, // timezone of the event
               5d, // Shift start hour
               3d // Shift duration
         }, { // Shift start day earlier, event starts during shift but ends after it
               DateUtils.getDateTime( 2012, 05, 14, 0, 0, 0 ), // Event Start Time
               DateUtils.getDateTime( 2012, 05, 14, 6, 0, 0 ), // Event End time
               DateUtils.getDateTime( 2012, 05, 13, 0, 0, 0 ), // Shift day
               AMERICA_MONTREAL_TZ, // timezone of the event
               22d, // Shift start hour
               4d // Shift duration
         } };

   private Object[][] iNotInShiftDifTimeZone = { { // Event spans two days. Shift starts and ends
                                                   // after the event
         DateUtils.getDateTime( 2012, 05, 13, 22, 0, 0 ), // Event Start Time
         DateUtils.getDateTime( 2012, 05, 14, 6, 0, 0 ), // Event End time
         DateUtils.getDateTime( 2012, 05, 14, 0, 0, 0 ), // Shift day
         EUROPE_AMSTERDAM_TZ, // timezone of the event
         12.5d, // Shift start hour
         3d // Shift duration
         },
         { // Shift starts and ends during the event hours but a day earlier
               DateUtils.getDateTime( 2012, 05, 14, 0, 0, 0 ), // Event Start Time
               DateUtils.getDateTime( 2012, 05, 14, 6, 0, 0 ), // Event End time
               DateUtils.getDateTime( 2012, 05, 13, 0, 0, 0 ), // Shift day
               EUROPE_AMSTERDAM_TZ, // timezone of the event
               2d, // Shift start hour
               3d // Shift duration
         }, { DateUtils.getDateTime( 2012, 05, 16, 10, 0, 0 ), // Event Start Time
               DateUtils.getDateTime( 2012, 05, 16, 14, 0, 0 ), // Event End time
               DateUtils.getDateTime( 2012, 05, 16, 10, 0, 0 ), // Shift day
               EUROPE_AMSTERDAM_TZ, // timezone of the event
               22d, // Shift start hour
               8d // Shift duration
         }, { //
               DateUtils.getDateTime( 2012, 05, 14, 1, 0, 0 ), // Event Start Time
               DateUtils.getDateTime( 2012, 05, 14, 5, 0, 0 ), // Event End time
               DateUtils.getDateTime( 2012, 05, 14, 0, 0, 0 ), // Shift day
               EUROPE_AMSTERDAM_TZ, // timezone of the event
               22d, // Shift start hour
               6d // Shift duration
         }, { // Shift starts and ends during the event hours but a day later
               DateUtils.getDateTime( 2012, 05, 14, 0, 0, 0 ), // Event Start Time
               DateUtils.getDateTime( 2012, 05, 14, 6, 0, 0 ), // Event End time
               DateUtils.getDateTime( 2012, 05, 13, 0, 0, 0 ), // Shift day
               EUROPE_AMSTERDAM_TZ, // timezone of the event
               2d, // Shift start hour
               3d // Shift duration
         } };
   private Object[][] iNotInShiftSameTimeZone = { { // Event spans two days. Shift stats and ends
                                                    // after the event
         DateUtils.getDateTime( 2012, 05, 13, 22, 0, 0 ), // Event Start Time
         DateUtils.getDateTime( 2012, 05, 14, 6, 0, 0 ), // Event End time
         DateUtils.getDateTime( 2012, 05, 14, 0, 0, 0 ), // Shift day
         AMERICA_MONTREAL_TZ, // timezone of the event
         6.5d, // Shift start hour
         3d // Shift duration
         },
         { // Shift starts and ends during the event hours but a day earlier
               DateUtils.getDateTime( 2012, 05, 14, 0, 0, 0 ), // Event Start Time
               DateUtils.getDateTime( 2012, 05, 14, 6, 0, 0 ), // Event End time
               DateUtils.getDateTime( 2012, 05, 13, 0, 0, 0 ), // Shift day
               AMERICA_MONTREAL_TZ, // timezone of the event
               2d, // Shift start hour
               3d // Shift duration
         }, { DateUtils.getDateTime( 2012, 05, 16, 10, 0, 0 ), // Event Start Time
               DateUtils.getDateTime( 2012, 05, 16, 14, 0, 0 ), // Event End time
               DateUtils.getDateTime( 2012, 05, 16, 10, 0, 0 ), // Shift day
               AMERICA_MONTREAL_TZ, // timezone of the event
               22d, // Shift start hour
               8d // Shift duration
         }, { //
               DateUtils.getDateTime( 2012, 05, 14, 1, 0, 0 ), // Event Start Time
               DateUtils.getDateTime( 2012, 05, 14, 5, 0, 0 ), // Event End time
               DateUtils.getDateTime( 2012, 05, 14, 0, 0, 0 ), // Shift day
               AMERICA_MONTREAL_TZ, // timezone of the event
               22d, // Shift start hour
               6d // Shift duration
         }, { // Shift starts and ends during the event hours but a day later
               DateUtils.getDateTime( 2012, 05, 14, 0, 0, 0 ), // Event Start Time
               DateUtils.getDateTime( 2012, 05, 14, 6, 0, 0 ), // Event End time
               DateUtils.getDateTime( 2012, 05, 13, 0, 0, 0 ), // Shift day
               AMERICA_MONTREAL_TZ, // timezone of the event
               2d, // Shift start hour
               3d // Shift duration
         } };


   /**
    * Creates the database data
    */
   public void createTestData() {
      // Do nothing.
   }


   /**
    * Tests various conditions when shift overlaps with the event. Shift start hour is specified in
    * the timezone different than the timezone of the event.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testInShiftDifTimeZone() throws Exception {
      assertResult( iInShiftDifTimeZone, 1 );
   }


   /**
    * Tests various conditions when shift overlaps with the event. Shift start hour is specified in
    * the same timezone as the timezone of the event.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testInShiftSameTimeZone() throws Exception {
      assertResult( iInShiftSameTimeZone, 1 );
   }


   /**
    * Tests various conditions when shift does not overlap with the event. Shift start hour is
    * specified in the timezone different than the timezone of the event.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testNotInShiftDifTimeZone() throws Exception {
      assertResult( iNotInShiftDifTimeZone, 0 );
   }


   /**
    * Tests various conditions when shift does not overlap with the event. Shift start hour is
    * specified in the same timezone as the timezone of the event.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testNotInShiftSameTimeZone() throws Exception {
      assertResult( iNotInShiftSameTimeZone, 0 );
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
         int lResult = this.execute( ( Date ) lData[0], ( Date ) lData[1], ( Date ) lData[2],
               ( String ) lData[3], ( Double ) lData[4], ( Double ) lData[5] );
         assertEquals( "Iteration: " + lIndex, aExpectedResult, lResult );
         lIndex++;
      }
   }


   /**
    * Execute the function.
    *
    * @param aEventStartGdt
    *           the event start date
    * @param aEventEndGdt
    *           the event end date
    * @param aStartDate
    *           shift date
    * @param aTimeZone
    *           timezone of the shift start hour
    * @param aShiftStartHr
    *           Shift start hour
    * @param aShiftDuratioHr
    *           shift duration
    *
    * @return 1 - the specified event is in the specified shift, 0 - otherwise
    *
    * @throws Exception
    *            if an error occurs.
    */
   private int execute( Date aEventStartGdt, Date aEventEndGdt, Date aStartDate, String aTimeZone,
         Double aShiftStartHr, Double aShiftDuratioHr ) throws Exception {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aEventStartGdt", aEventStartGdt );
      lArgs.add( "aEventEndGdt", aEventEndGdt );
      lArgs.add( "aStartDate", aStartDate );
      lArgs.add( "aTimeZone", aTimeZone );
      lArgs.add( "aShiftStartHr", aShiftStartHr );
      lArgs.add( "aShiftDuratioHr", aShiftDuratioHr );

      String[] lParmOrder = { "aEventStartGdt", "aEventEndGdt", "aStartDate", "aTimeZone",
            "aShiftStartHr", "aShiftDuratioHr" };

      // Execute the query
      return Integer
            .parseInt( QueryExecutor.executeFunction( sDatabaseConnectionRule.getConnection(),
                  QueryExecutor.getFunctionName( getClass() ), lParmOrder, lArgs ) );
   }
}
