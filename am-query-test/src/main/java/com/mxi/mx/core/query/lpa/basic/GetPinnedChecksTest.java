package com.mxi.mx.core.query.lpa.basic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AircraftKey;


/**
 * This class performs unit testing on the query file with the same package and name.
 */
public class GetPinnedChecksTest {

   private static final AircraftKey AIRCRAFT_KEY = new AircraftKey( 4650, 300785 );
   private static final String COMMIT_CHECK_BARCODE = "T40S0001CHQ";
   private static final String COMPLETE_CHECK_BARCODE = "M00T2221DDD";

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @Before
   public void loadData() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), GetPinnedChecksTest.class );
   }


   /**
    * The business canceled a work package for the time slot and but marked it as prevent LPA.
    * Ensure that this sort of work package is not returned
    */
   @Test
   public void cancelledPreventLPAIsNotReturned() {
      // CANCELLED work package scheduled on 10-JUNE-2017, to end on 12-JUNE-2017
      Calendar currentDateCal = Calendar.getInstance();
      // set current date to 2017-JUNE-11
      currentDateCal.set( 2017, 6, 11 );
      Date currentDate = currentDateCal.getTime();

      // ACT
      QuerySet lQs = getPinnedChecksDs( AIRCRAFT_KEY, currentDate, 3 );

      // ASSERT
      assertFalse( lQs.hasNext() );
   }


   /**
    * Tests when there are an ACTV work package, and a COMMIT and a COMPLETE turn work package, the
    * COMMIT and COMPLETE work package have scheduled end dates earlier than the Opportunity time
    * window, nothing will be returned by this query
    */
   @Test
   public void
         neitherCommitNorCompleteWorkPackageIsReturnedWhenItsScheduledEndDateNotWithinWindow() {
      // ARRANGE
      // COMMIT work package's schedule start date is 14-MAY-2017 and schedule end date is
      // 15-MAY-2017; COMPLETE work package's schedule start date is 14-JUNE-2017 and event date is
      // 15-JUNE-2017
      Calendar currentDateCal = Calendar.getInstance();
      // set current date to 2017-JULY-01
      currentDateCal.set( 2017, 6, 1 );
      Date currentDate = currentDateCal.getTime();

      // ACT
      QuerySet lQs = getPinnedChecksDs( AIRCRAFT_KEY, currentDate, 3 );

      // ASSERT
      assertFalse( lQs.hasNext() );
   }


   /**
    * Tests when there are a COMPLETE turn work package and an ACTV work package and a COMMIT work
    * package, and this COMMIT work package has a scheduled end date within the Opportunity time
    * window, it will be returned by this query
    */
   @Test
   public void commitWorkPackageIsReturnedWhenItsScheduledEndDateWithinWindow() {
      // ARRANGE
      // COMMIT work package's schedule start date is 14-MAY-2017 and schedule end date is
      // 15-MAY-2017
      Calendar currentDateCal = Calendar.getInstance();
      // set current date to 2017-MAY-14
      currentDateCal.set( 2017, 4, 14 );
      Date currentDate = currentDateCal.getTime();

      // ACT
      QuerySet lQs = getPinnedChecksDs( AIRCRAFT_KEY, currentDate, 3 );

      // ASSERT
      assertEquals( "RowCount", 1, lQs.getRowCount() );

      lQs.next();
      // Assert the COMMIT check is returned
      assertEquals( COMMIT_CHECK_BARCODE, lQs.getString( "barcode_sdesc" ) );
   }


   /**
    * Tests when there are a COMMIT turn work package and an ACTV work package and a COMPLETE work
    * package, and this COMPLETE work package has a scheduled end date within the Opportunity time
    * window, it will be returned by this query
    */
   @Test
   public void completeWorkPackageIsReturnedWhenItsScheduledEndDateWithinWindow() {
      // ARRANGE
      // COMPLETE work package's schedule start date is 14-JUNE-2017 and event date is 15-JUNE-2017
      Calendar currentDateCal = Calendar.getInstance();
      // set current date to 2017-JUNE-14
      currentDateCal.set( 2017, 5, 14 );
      Date currentDate = currentDateCal.getTime();

      // ACT
      QuerySet lQs = getPinnedChecksDs( AIRCRAFT_KEY, currentDate, 3 );

      // ASSERT
      assertEquals( "RowCount", 1, lQs.getRowCount() );

      lQs.next();
      // Assert the COMPLETE check is returned
      assertEquals( COMPLETE_CHECK_BARCODE, lQs.getString( "barcode_sdesc" ) );
   }


   /**
    * Get pinned checks.
    *
    * @param aAircraft
    *           The aircraft key
    * @param aStartDate
    *           The Start Date of the Scheduling Window
    * @param aScheduleRange
    *           The duration in days of the Scheduling Window
    */
   private QuerySet getPinnedChecksDs( AircraftKey aAircraft, Date aStartDate,
         int aScheduleRange ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aAircraft, "aAircraftDbId", "aAircraftId" );
      lArgs.add( "aStartTime", aStartDate );
      lArgs.add( "aScheduleRange", aScheduleRange );

      return QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }

}
