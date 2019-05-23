package com.mxi.mx.core.query.plsql.eventpkg;

import java.sql.Date;
import java.util.Calendar;

import org.junit.Assert;
import org.junit.Test;


/**
 * Running tests for event_pkg.findForecastedDeadDt Forecast Model
 *
 * @author ghyde
 */

public class FindForecastedDeadDtTest extends FindForecastedDeadDtUtl {

   /*
    * Test #1
    * 
    * Verify forecast date with overlapping Blackout periods less than 12 months and with
    * mim_data_type=Hours
    */

   @Test
   public void Test_FindForecastwithOverlappingBlkForHours() {
      Date lCurrentDate = new java.sql.Date( Calendar.getInstance().getTime().getTime() );
      int lInvDbId = 4650;
      int lInvId = 10737;
      int lDateTypeDbId = 0;
      int lDateTypeId = 1; // hours
      long lRemainingUsage = 200;

      Date forecastDate = runValidateFindForecast( lInvDbId, lInvId, lDateTypeDbId, lDateTypeId,
            lRemainingUsage, lCurrentDate );

      long days = DateDiffInDays( lCurrentDate, forecastDate );
      Assert.assertTrue( "The actual days: " + days + " days", days == 208 );
   }


   /*
    * Test #2
    * 
    * Verify forecast date with overlapping Blackout periods less than 12 months and with
    * mim_data_type=Cycles
    */

   @Test
   public void Test_FindForecastwithOverlappingBlkForCycles() {
      Date lCurrentDate = new java.sql.Date( Calendar.getInstance().getTime().getTime() );
      int lInvDbId = 4650;
      int lInvId = 10737;
      int lDateTypeDbId = 0;
      int lDateTypeId = 10; // Cycles
      long lRemainingUsage = 200;

      Date forecastDate = runValidateFindForecast( lInvDbId, lInvId, lDateTypeDbId, lDateTypeId,
            lRemainingUsage, lCurrentDate );

      long days = DateDiffInDays( lCurrentDate, forecastDate );
      Assert.assertTrue( "The actual days: " + days + " days", days == 208 );
   }


   /*
    * Test #3
    * 
    * Verify forecast date with overlapping Blackout periods 6years of remaining and with
    * mim_data_type=Hours
    */
   @Test
   public void Test_FindSixYearForecastwithOverlappingBlkForHours() {
      Date lCurrentDate = new java.sql.Date( Calendar.getInstance().getTime().getTime() );
      int lInvDbId = 4650;
      int lInvId = 10737;
      int lDateTypeDbId = 0;
      int lDateTypeId = 1; // hours
      long lRemainingUsage = 2190;

      Date forecastDate = runValidateFindForecast( lInvDbId, lInvId, lDateTypeDbId, lDateTypeId,
            lRemainingUsage, lCurrentDate );

      long days = DateDiffInDays( lCurrentDate, forecastDate );

      // +/-1% variance if the forecast is over a year
      Assert.assertTrue( "The actual days: " + days + " days",
            ( days >= 2178 ) && ( days <= 2222 ) );
   }


   /*
    * Test #4
    * 
    * Verify forecast date with overlapping Blackout periods 6 years of remaining and with
    * mim_data_type=Cycles
    */

   @Test
   public void Test_FindSixYearForecastwithOverlappingBlkForCycles() {
      Date lCurrentDate = new java.sql.Date( Calendar.getInstance().getTime().getTime() );
      int lInvDbId = 4650;
      int lInvId = 10737;
      int lDateTypeDbId = 0;
      int lDateTypeId = 10; // Cycles
      long lRemainingUsage = 2190;

      Date forecastDate = runValidateFindForecast( lInvDbId, lInvId, lDateTypeDbId, lDateTypeId,
            lRemainingUsage, lCurrentDate );

      long days = DateDiffInDays( lCurrentDate, forecastDate );

      // +/-1% variance if the forecast is over a year
      Assert.assertTrue( "The actual days: " + days + " days",
            ( days >= 2178 ) && ( days <= 2222 ) );
   }


   /*
    * Test #5
    * 
    * Verify forecast date with overlapping Blackout periods 10 years of remaining and with
    * mim_data_type=Cycles
    */

   @Test
   public void Test_FindTenYearForecastwithOverlappingBlkForCycles() {
      Date lCurrentDate = new java.sql.Date( Calendar.getInstance().getTime().getTime() );
      int lInvDbId = 4650;
      int lInvId = 10737;
      int lDateTypeDbId = 0;
      int lDateTypeId = 10; // Cycles
      long lRemainingUsage = 3650;

      Date forecastDate = runValidateFindForecast( lInvDbId, lInvId, lDateTypeDbId, lDateTypeId,
            lRemainingUsage, lCurrentDate );
      // +/-1% variance if the forecast is over a year
      long days = DateDiffInDays( lCurrentDate, forecastDate );
      Assert.assertTrue( "The actual days: " + days + " days",
            ( days >= 3613 ) && ( days <= 3687 ) );
   }


   /*
    * Test #6
    * 
    * Test for a forecast goes 205 years out, but the system will return 200 years. New
    * functionality added to limit forecasts to 200 years.
    */

   @Test
   public void Test_Find200YearForecastCycles() {
      Date lCurrentDate = new java.sql.Date( Calendar.getInstance().getTime().getTime() );
      int lInvDbId = 4650;
      int lInvId = 10737;
      int lDateTypeDbId = 0;
      int lDateTypeId = 10; // Cycles
      long lRemainingUsage = 74825;

      Date forecastDate = runValidateFindForecast( lInvDbId, lInvId, lDateTypeDbId, lDateTypeId,
            lRemainingUsage, lCurrentDate );

      long days = DateDiffInDays( lCurrentDate, forecastDate );

      Assert.assertTrue( "The actual days: " + days + " days",
            ( days >= 72270 ) && ( days <= 74835 ) );
   }


   /*
    * Test #7
    * 
    * Test where there is: 1 Hour JAN 1 - APR 30; 3 Hour for May 1 - AUG 31; 2 Hours for SEP 1 - DEC
    * 31; Black-out (BLK) period is SEP 1 - 30
    */

   @Test
   public void Test_FindForecastforChangingUsageRangesHours() {
      Date lStartDate = Date.valueOf( "2018-01-01" );
      int lInvDbId = 4650;
      int lInvId = 10747;
      int lDateTypeDbId = 0;
      int lDateTypeId = 1; // hours
      long lRemainingUsage = 551;

      Date lforecastDate = runValidateFindForecast( lInvDbId, lInvId, lDateTypeDbId, lDateTypeId,
            lRemainingUsage, lStartDate );

      long days = DateDiffInDays( lStartDate, lforecastDate );
      Assert.assertTrue( "The actual days: " + days + " days", days == 302 );
   }


   /*
    * Test #8
    * 
    * Test where there is: 2 Hour JAN 1 - APR 30; 6 Hour for May 1 - AUG 31; 4 Hours for SEP 1 - DEC
    * 31; Black-out (BLK) period is SEP 1 - 30
    */

   @Test
   public void Test_FindForecastforChangingUsageRangesCycles() {
      Date lStartDate = Date.valueOf( "2020-01-01" );
      int lInvDbId = 4650;
      int lInvId = 10747;
      int lDateTypeDbId = 0;
      int lDateTypeId = 10; // Cycles
      long lRemainingUsage = 1104;

      Date lforecastDate = runValidateFindForecast( lInvDbId, lInvId, lDateTypeDbId, lDateTypeId,
            lRemainingUsage, lStartDate );

      long days = DateDiffInDays( lStartDate, lforecastDate );
      Assert.assertEquals( "2020-10-30", lforecastDate.toString() );
      Assert.assertTrue( "The actual days: " + days + " days", days == 303 );
   }

}
