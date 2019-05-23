package com.mxi.mx.report.query.task.reportweightandbalance;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.TaskWeightAndBalanceKey;
import com.mxi.mx.core.table.evt.EvtEventTable;


/**
 * This class tests the pipeline function driving the weight and balance impact report.
 * report_weight_and_balance_pkg.GetWeightAndBalanceImpact.
 *
 * It executes the function using a qrx file GetWeightAndBalanceImpactReportQuery.qrx
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class WeightAndBalanceImpactReportTest {

   @Rule
   public DatabaseConnectionRule sConnection = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @Before
   public void loadData() {
      DataLoaders.load( sConnection.getConnection(), getClass() );
   }


   /**
    * Returns yesterday's weight and balance impacts when no date is provided
    */
   @Test
   public void itReturnsWBImpactsForYesterday_WhenNoDateProvided() {
      final Date lYesterday = DateUtils.addDays( new Date(), -1 );

      EvtEventTable lTaskCompletedMidnightYesterday =
            initializeTaskWithDate( new EventKey( 0, 2 ), DateUtils.getMidnight( lYesterday ) );

      EvtEventTable lTaskCompletedEndOfYesterday =
            initializeTaskWithDate( new EventKey( 0, 3 ), DateUtils.getEndOfDay( lYesterday ) );

      EvtEventTable lTaskCompletedYesterday =
            initializeTaskWithDate( new EventKey( 0, 4 ), lYesterday );

      // Execute unit under test
      QuerySet lQs = executeWBReportQuery( null );

      // Verify results
      Assert.assertTrue( "WB impact for task completed midnight yesterday should be returned",
            resultContains( lQs, lTaskCompletedMidnightYesterday.getPk() ) );
      Assert.assertTrue( "WB impact for task completed 23:59 yesterday should be returned",
            resultContains( lQs, lTaskCompletedEndOfYesterday.getPk() ) );
      Assert.assertTrue( "WB impact for task completed yesterday should be returned",
            resultContains( lQs, lTaskCompletedYesterday.getPk() ) );
   }


   /**
    *
    * Filters out weight and balance impacts occuring outside of yesterday
    *
    */
   @Test
   public void itFiltersOutWBImpactsThatOccuredBeforeOrAfterYesterday_WhenNoDateProvided() {
      final Date lTwoDaysAgo = DateUtils.getEndOfDay( DateUtils.addDays( new Date(), -2 ) );
      final Date lMidnightToday = DateUtils.getMidnight( new Date() );

      EvtEventTable lTaskCompletedMidnightToday =
            initializeTaskWithDate( new EventKey( 0, 2 ), lMidnightToday );

      EvtEventTable lTaskCompleted2DaysAgo =
            initializeTaskWithDate( new EventKey( 0, 3 ), lTwoDaysAgo );

      // Run the report query
      QuerySet lQs = executeWBReportQuery( null );

      // Verify results
      Assert.assertTrue( "WB impact for task completed today should be filtered out",
            !resultContains( lQs, lTaskCompletedMidnightToday.getPk() ) );
      Assert.assertTrue( "WB impact for task completed 2 days ago should be filtered out",
            !resultContains( lQs, lTaskCompleted2DaysAgo.getPk() ) );
   }


   /**
    * Returns weight and balance impacts that occurred on the specified date
    */
   @Test
   public void itReturnsWBImpactsForTheSpecifiedDate_WhenDateProvided() throws ParseException {
      final Date lApril3rd = new SimpleDateFormat( "MM/dd/yyyy HH" ).parse( "04/03/2015 12" );

      EvtEventTable lTaskCompletedMidnightApril3rd =
            initializeTaskWithDate( new EventKey( 0, 2 ), DateUtils.getMidnight( lApril3rd ) );

      EvtEventTable lTaskCompletedEndOfApril3rd =
            initializeTaskWithDate( new EventKey( 0, 3 ), DateUtils.getEndOfDay( lApril3rd ) );

      EvtEventTable lTaskCompletedApril3rd =
            initializeTaskWithDate( new EventKey( 0, 4 ), lApril3rd );

      // Execute unit under test
      QuerySet lQs = executeWBReportQuery( lApril3rd );

      // Verify results
      Assert.assertTrue(
            "WB impact for task completed midnight on the given date should be returned",
            resultContains( lQs, lTaskCompletedMidnightApril3rd.getPk() ) );
      Assert.assertTrue( "WB impact for task completed 23:59 on the given date should be returned",
            resultContains( lQs, lTaskCompletedEndOfApril3rd.getPk() ) );
      Assert.assertTrue( "WB impact for task completed on the given date should be returned",
            resultContains( lQs, lTaskCompletedApril3rd.getPk() ) );
   }


   /**
    *
    * Filters out weight and balance impacts that occurred outside the specified date
    */
   @Test
   public void itFiltersOutWBImpactsOccuringOutsideTheSpecifiedDate_WhenDateProvided()
         throws ParseException {
      final SimpleDateFormat lDateFormatter = new SimpleDateFormat( "MM/dd/yyyy HH:mm:ss" );
      final Date lApril3rd = lDateFormatter.parse( "04/03/2015 12:00:00" );

      EvtEventTable lTaskCompletedApril2nd = initializeTaskWithDate( new EventKey( 0, 3 ),
            lDateFormatter.parse( "04/02/2015 23:59:59" ) );
      EvtEventTable lTaskCompletedApril4th = initializeTaskWithDate( new EventKey( 0, 2 ),
            lDateFormatter.parse( "04/04/2015 00:00:00" ) );

      // Run the report query
      QuerySet lQs = executeWBReportQuery( lApril3rd );

      // Verify results
      Assert.assertTrue( "WB impact for task completed the following day should be filtered out",
            !resultContains( lQs, lTaskCompletedApril4th.getPk() ) );
      Assert.assertTrue( "WB impact for task completed a day before should be filtered out",
            !resultContains( lQs, lTaskCompletedApril2nd.getPk() ) );
   }


   /**
    *
    * Returns the default WB impact when there is no WB impact specific to the airacraft part number
    *
    */
   @Test
   public void itReturnsDefaultWBInfo_WhenNoPartSpecificWBExists() {
      final Date lYesterday = DateUtils.addDays( new Date(), -1 );

      // Yaml data file associates this task with a task def containing a single default WB
      final EventKey lTaskWithOnlyDefaultWB = new EventKey( 0, 4 );
      final TaskWeightAndBalanceKey lTheDefaultWBForTheTask = new TaskWeightAndBalanceKey( 0, 3 );

      initializeTaskWithDate( lTaskWithOnlyDefaultWB, lYesterday );

      // Run the report query
      QuerySet lQs = executeWBReportQuery( null );

      // Verify results
      Assert.assertTrue( "The default WB impact for the task should be returned",
            resultContains( lQs, lTheDefaultWBForTheTask ) );
   }


   /**
    *
    * Make sure we're not returning the default AND the part specific WB info when a task's
    * definition has part specific WB for the aircraft. Only the part specific WB should be returned
    *
    */
   @Test
   public void itOnlyReturnsThePartSpecificWBInfoForTask_WhenWBPartMatchesAircraftPart() {
      final Date lYesterday = DateUtils.addDays( new Date(), -1 );

      // Yaml data file associates this task with a task def containing a default and part-specific
      // WB
      final EventKey lTaskWithPartSpecificWB = new EventKey( 0, 3 );
      final TaskWeightAndBalanceKey lPartSpecificWBForTheTask = new TaskWeightAndBalanceKey( 0, 1 );
      final TaskWeightAndBalanceKey lDefaultWBForTask = new TaskWeightAndBalanceKey( 0, 0 );

      initializeTaskWithDate( lTaskWithPartSpecificWB, lYesterday );

      QuerySet lQs = executeWBReportQuery( null );
      Assert.assertTrue( "The part specific WB impact should be returned",
            resultContains( lQs, lPartSpecificWBForTheTask ) );
      Assert.assertTrue(
            "The default WB should not be returned when there is a part specific one for the aircraft",
            !resultContains( lQs, lDefaultWBForTask ) );
   }


   /**
    * Updates the task with the provided completion date
    */
   private EvtEventTable initializeTaskWithDate( EventKey aEventKey, Date aCompletionDate ) {
      EvtEventTable lTaskCompletedMidnightYesterday = EvtEventTable.findByPrimaryKey( aEventKey );
      lTaskCompletedMidnightYesterday.setEventDate( aCompletionDate );
      lTaskCompletedMidnightYesterday.update();
      return lTaskCompletedMidnightYesterday;
   }


   /**
    * Checks if any of the result rows have the provided weight and balance key
    */
   private boolean resultContains( QuerySet aQs,
         TaskWeightAndBalanceKey aTaskWeightAndBalanceKey ) {
      aQs.beforeFirst();
      while ( aQs.next() ) {
         TaskWeightAndBalanceKey lTaskWeightAndBalanceKey =
               aQs.getKey( TaskWeightAndBalanceKey.class, "task_weight_balance_db_id",
                     "task_weight_balance_id" );
         if ( aTaskWeightAndBalanceKey.equals( lTaskWeightAndBalanceKey ) ) {
            return true;
         }
      }
      return false;
   }


   /**
    * Checks if any of the result rows have the provided event key
    */
   private boolean resultContains( QuerySet aQs, EventKey aPk ) {

      aQs.beforeFirst();
      while ( aQs.next() ) {
         EventKey lEventKey = aQs.getKey( EventKey.class, "event_db_id", "event_id" );
         if ( aPk.equals( lEventKey ) ) {
            return true;
         }
      }
      return false;
   }


   private QuerySet executeWBReportQuery( Date aReportDate ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aReportDate", aReportDate );

      return QuerySetFactory.getInstance().executeQuery(
            "com.mxi.mx.report.query.task.GetWeightAndBalanceImpactReportQuery", lArgs );
   }
}
