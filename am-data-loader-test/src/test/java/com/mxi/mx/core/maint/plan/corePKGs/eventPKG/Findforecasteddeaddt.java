package com.mxi.mx.core.maint.plan.corePKGs.eventPKG;

import java.sql.Date;
import java.util.Calendar;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.mxi.mx.util.FindforecasteddeaddtTest;


/**
 * This test suite contains test cases on validation of procedure event_pkg.findforecasteddeaddt in
 * EVENT_PKG package.
 *
 * This test suite is attempting to improve the performance of
 * event_pkg.findForecastedDeadDtFcModel. Test cases should cover prediction periods less than 12
 * months and over a year for each MIM data type. And testing scope should also consider blackout
 * situation during prediction period and negative usage value is been provided.
 *
 * @author ALICIA QIAN
 */
public class Findforecasteddeaddt extends FindforecasteddeaddtTest {

   @Override
   @Before
   public void before() throws Exception {

      super.before();
      ClassDataSetup();
   }


   /**
    * Clean up after each individual test
    *
    * @throws Exception
    */
   @After
   @Override
   public void after() throws Exception {

      ClassDataRestore();
      super.after();
   }


   /**
    * Verify forecast date less than 12 months without BLK and with mim_data_type=hour
    *
    */

   @Test
   public void test_FindForcastLessThan12MonthesNoBLK_Hour() {
      Date currentDate = new java.sql.Date( Calendar.getInstance().getTime().getTime() );

      Date forecastDate =
            runValidateFindForecast( Integer.parseInt( strINVDBID ), Integer.parseInt( strINVID ),
                  Integer.parseInt( mimDTtype.get( 0 ).getDATA_TYPE_DB_ID() ),
                  Integer.parseInt( mimDTtype.get( 0 ).getDATA_TYPE_ID() ), 200, currentDate );

      long days = DateDiffInDays( currentDate, forecastDate );
      Assert.assertTrue( "The actual days: " + days + " days", days == 199 );

   }


   /**
    * Verify forecast date less than 12 months without BLK and with mim_data_type=cycle
    *
    */

   @Test
   public void test_FindForcastLessThan12MonthesNoBLK_Cycle() {

      Date currentDate = new java.sql.Date( Calendar.getInstance().getTime().getTime() );

      Date forecastDate =
            runValidateFindForecast( Integer.parseInt( strINVDBID ), Integer.parseInt( strINVID ),
                  Integer.parseInt( mimDTtype.get( 1 ).getDATA_TYPE_DB_ID() ),
                  Integer.parseInt( mimDTtype.get( 1 ).getDATA_TYPE_ID() ), 200, currentDate );

      long days = DateDiffInDays( currentDate, forecastDate );
      Assert.assertTrue( "The actual days: " + days + " days", days == 199 );

   }


   /**
    * Verify forecast date greater than 12 months without BLK and with mim_data_type=hour
    *
    */

   @Test
   public void test_FindForcastGreaterThan12MonthesNoBLK_Hour() {
      Date currentDate = new java.sql.Date( Calendar.getInstance().getTime().getTime() );

      Date forecastDate =
            runValidateFindForecast( Integer.parseInt( strINVDBID ), Integer.parseInt( strINVID ),
                  Integer.parseInt( mimDTtype.get( 0 ).getDATA_TYPE_DB_ID() ),
                  Integer.parseInt( mimDTtype.get( 0 ).getDATA_TYPE_ID() ), 400, currentDate );

      long days = DateDiffInDays( currentDate, forecastDate );
      Assert.assertTrue( "The actual days: " + days + " days", ( days >= 396 ) || ( days <= 404 ) );

   }


   /**
    * Verify forecast date greater than 12 months without BLK and with mim_data_type=cycle
    *
    */

   @Test
   public void test_FindForcastGreaterThan12MonthesNoBLK_Cycle() {

      Date currentDate = new java.sql.Date( Calendar.getInstance().getTime().getTime() );

      Date forecastDate =
            runValidateFindForecast( Integer.parseInt( strINVDBID ), Integer.parseInt( strINVID ),
                  Integer.parseInt( mimDTtype.get( 1 ).getDATA_TYPE_DB_ID() ),
                  Integer.parseInt( mimDTtype.get( 1 ).getDATA_TYPE_ID() ), 2000, currentDate );

      long days = DateDiffInDays( currentDate, forecastDate );
      Assert.assertTrue( "The actual days: " + days + " days",
            ( days >= 1980 ) || ( days <= 2020 ) );

   }


   /**
    * Verify forecast date less than 12 months with BLK and with mim_data_type=hour and start date
    * of forecasting falls on blk period.
    *
    */

   @Test
   public void test_FindForcastLessThan12MonthesBLK_StartDate_overlap_Hour() {

      Date currentDate = new java.sql.Date( Calendar.getInstance().getTime().getTime() );
      Date startdate = addDaysToCurrentDate( -1 );
      Date endDate = addDaysToCurrentDate( 32 );
      addBLKSCHEDDate( startdate, endDate );

      Date forecastDate =
            runValidateFindForecast( Integer.parseInt( strINVDBID ), Integer.parseInt( strINVID ),
                  Integer.parseInt( mimDTtype.get( 0 ).getDATA_TYPE_DB_ID() ),
                  Integer.parseInt( mimDTtype.get( 0 ).getDATA_TYPE_ID() ), 5, currentDate );

      long days = DateDiffInDays( currentDate, forecastDate );
      Assert.assertTrue( "The actual days: " + days + " days", days == 36 );

   }


   /**
    * Verify forecast date less than 12 months with BLK and with mim_data_type=hour blk in between
    * forecast start and ending date.
    *
    */
   @Test
   public void test_FindForcastLessThan12MonthesBLK_Hour() {

      Date currentDate = new java.sql.Date( Calendar.getInstance().getTime().getTime() );
      Date startdate = addDaysToCurrentDate( 100 );
      Date endDate = addDaysToCurrentDate( 120 );
      addBLKSCHEDDate( startdate, endDate );

      Date forecastDate =
            runValidateFindForecast( Integer.parseInt( strINVDBID ), Integer.parseInt( strINVID ),
                  Integer.parseInt( mimDTtype.get( 0 ).getDATA_TYPE_DB_ID() ),
                  Integer.parseInt( mimDTtype.get( 0 ).getDATA_TYPE_ID() ), 150, currentDate );

      long days = DateDiffInDays( currentDate, forecastDate );
      Assert.assertTrue( "The actual days: " + days + " days", days == 169 );

   }


   /**
    * Verify forecast date less than 12 months with BLK and with mim_data_type=hour and ending date
    * of forecasting falls on blk period.
    *
    */

   @Test
   public void test_FindForcastLessThan12MonthesBLK_EndDate_overlap_Hour() {

      Date currentDate = new java.sql.Date( Calendar.getInstance().getTime().getTime() );
      Date startdate = addDaysToCurrentDate( 360 );
      Date endDate = addDaysToCurrentDate( 370 );
      addBLKSCHEDDate( startdate, endDate );

      Date forecastDate =
            runValidateFindForecast( Integer.parseInt( strINVDBID ), Integer.parseInt( strINVID ),
                  Integer.parseInt( mimDTtype.get( 0 ).getDATA_TYPE_DB_ID() ),
                  Integer.parseInt( mimDTtype.get( 0 ).getDATA_TYPE_ID() ), 364, currentDate );

      long days = DateDiffInDays( currentDate, forecastDate );
      Assert.assertTrue( "The actual days: " + days + " days", days == 373 );

   }


   /**
    * Verify forecast date less than 12 months with BLK and with mim_data_type=cycle and start date
    * of forecasting falls on blk period.
    *
    */

   @Test
   public void test_FindForcastLessThan12MonthesBLK_StartDate_overlap_Cycle() {

      Date currentDate = new java.sql.Date( Calendar.getInstance().getTime().getTime() );
      Date startdate = addDaysToCurrentDate( -1 );
      Date endDate = addDaysToCurrentDate( 32 );
      addBLKSCHEDDate( startdate, endDate );

      Date forecastDate =
            runValidateFindForecast( Integer.parseInt( strINVDBID ), Integer.parseInt( strINVID ),
                  Integer.parseInt( mimDTtype.get( 1 ).getDATA_TYPE_DB_ID() ),
                  Integer.parseInt( mimDTtype.get( 1 ).getDATA_TYPE_ID() ), 5, currentDate );

      long days = DateDiffInDays( currentDate, forecastDate );
      Assert.assertTrue( "The actual days: " + days + " days", days == 36 );

   }


   /**
    * Verify forecast date less than 12 months with BLK and with mim_data_type=cycle blk in between
    * forecast start and ending date.
    *
    */

   @Test
   public void test_FindForcastLessThan12MonthesBLK_Cycle() {

      Date currentDate = new java.sql.Date( Calendar.getInstance().getTime().getTime() );
      Date startdate = addDaysToCurrentDate( 100 );
      Date endDate = addDaysToCurrentDate( 120 );
      addBLKSCHEDDate( startdate, endDate );

      Date forecastDate =
            runValidateFindForecast( Integer.parseInt( strINVDBID ), Integer.parseInt( strINVID ),
                  Integer.parseInt( mimDTtype.get( 1 ).getDATA_TYPE_DB_ID() ),
                  Integer.parseInt( mimDTtype.get( 1 ).getDATA_TYPE_ID() ), 150, currentDate );

      long days = DateDiffInDays( currentDate, forecastDate );
      Assert.assertTrue( "The actual days: " + days + " days", days == 169 );

   }


   /**
    * Verify forecast date less than 12 months with BLK and with mim_data_type=cycle and ending date
    * of forecasting falls on blk period.
    *
    */
   @Test
   public void test_FindForcastLessThan12MonthesBLK_EndDate_overlap_Cycle() {

      Date currentDate = new java.sql.Date( Calendar.getInstance().getTime().getTime() );
      Date startdate = addDaysToCurrentDate( 360 );
      Date endDate = addDaysToCurrentDate( 370 );
      addBLKSCHEDDate( startdate, endDate );

      Date forecastDate =
            runValidateFindForecast( Integer.parseInt( strINVDBID ), Integer.parseInt( strINVID ),
                  Integer.parseInt( mimDTtype.get( 1 ).getDATA_TYPE_DB_ID() ),
                  Integer.parseInt( mimDTtype.get( 1 ).getDATA_TYPE_ID() ), 364, currentDate );

      long days = DateDiffInDays( currentDate, forecastDate );
      Assert.assertTrue( "The actual days: " + days + " days", days >= 369 && days <= 377 );

   }


   /**
    * Verify forecast date greater than 12 months with BLK and with mim_data_type=hour and start
    * date of forecasting falls on blk period.
    *
    */
   @Test
   public void test_FindForcastGreaterThan12MonthesBLK_StartDate_overlap_Hour() {

      Date currentDate = new java.sql.Date( Calendar.getInstance().getTime().getTime() );
      Date startdate = addDaysToCurrentDate( -1 );
      Date endDate = addDaysToCurrentDate( 11 );
      addBLKSCHEDDate( startdate, endDate );

      Date forecastDate =
            runValidateFindForecast( Integer.parseInt( strINVDBID ), Integer.parseInt( strINVID ),
                  Integer.parseInt( mimDTtype.get( 0 ).getDATA_TYPE_DB_ID() ),
                  Integer.parseInt( mimDTtype.get( 0 ).getDATA_TYPE_ID() ), 400, currentDate );

      long days = DateDiffInDays( currentDate, forecastDate );
      Assert.assertTrue( "The actual days: " + days + " days", days >= 405 && days <= 415 );

   }


   /**
    * Verify forecast date greater than 12 months with BLK and with mim_data_type=hour blk in
    * between forecast start and ending date.
    *
    */
   @Test
   public void test_FindForcastGreaterThan12MonthesBLK_Hour() {

      Date currentDate = new java.sql.Date( Calendar.getInstance().getTime().getTime() );
      Date startdate = addDaysToCurrentDate( 100 );
      Date endDate = addDaysToCurrentDate( 120 );
      addBLKSCHEDDate( startdate, endDate );

      Date forecastDate =
            runValidateFindForecast( Integer.parseInt( strINVDBID ), Integer.parseInt( strINVID ),
                  Integer.parseInt( mimDTtype.get( 0 ).getDATA_TYPE_DB_ID() ),
                  Integer.parseInt( mimDTtype.get( 0 ).getDATA_TYPE_ID() ), 400, currentDate );

      long days = DateDiffInDays( currentDate, forecastDate );
      Assert.assertTrue( "The actual days: " + days + " days", days >= 414 && days <= 425 );

   }


   /**
    * Verify forecast date greater than 12 months with BLK and with mim_data_type=hour and ending
    * date of forecasting falls on blk period.
    *
    */

   @Test
   public void test_FindForcastGreaterThan12MonthesBLK_EndDate_overlap_Hour() {

      Date currentDate = new java.sql.Date( Calendar.getInstance().getTime().getTime() );
      Date startdate = addDaysToCurrentDate( 396 );
      Date endDate = addDaysToCurrentDate( 410 );
      addBLKSCHEDDate( startdate, endDate );

      Date forecastDate =
            runValidateFindForecast( Integer.parseInt( strINVDBID ), Integer.parseInt( strINVID ),
                  Integer.parseInt( mimDTtype.get( 0 ).getDATA_TYPE_DB_ID() ),
                  Integer.parseInt( mimDTtype.get( 0 ).getDATA_TYPE_ID() ), 400, currentDate );

      long days = DateDiffInDays( currentDate, forecastDate );
      Assert.assertTrue( "The actual days: " + days + " days", days >= 408 && days <= 421 );

   }


   /**
    * Verify forecast date greater than 12 months with BLK and with mim_data_type=cycle and start
    * date of forecasting falls on blk period.
    *
    */
   @Test
   public void test_FindForcastGreaterrThan12MonthesBLK_StartDate_overlap_Cycle() {

      Date currentDate = new java.sql.Date( Calendar.getInstance().getTime().getTime() );
      Date startdate = addDaysToCurrentDate( -1 );
      Date endDate = addDaysToCurrentDate( 11 );
      addBLKSCHEDDate( startdate, endDate );

      Date forecastDate =
            runValidateFindForecast( Integer.parseInt( strINVDBID ), Integer.parseInt( strINVID ),
                  Integer.parseInt( mimDTtype.get( 1 ).getDATA_TYPE_DB_ID() ),
                  Integer.parseInt( mimDTtype.get( 1 ).getDATA_TYPE_ID() ), 400, currentDate );

      long days = DateDiffInDays( currentDate, forecastDate );
      Assert.assertTrue( "The actual days: " + days + " days", days >= 405 && days <= 415 );

   }


   /**
    * Verify forecast date greater than 12 months with BLK and with mim_data_type=cycle blk in
    * between forecast start and ending date.
    *
    */
   @Test
   public void test_FindForcastGreaterrThan12MonthesBLK_Cycle() {

      Date currentDate = new java.sql.Date( Calendar.getInstance().getTime().getTime() );
      Date startdate = addDaysToCurrentDate( 100 );
      Date endDate = addDaysToCurrentDate( 120 );
      addBLKSCHEDDate( startdate, endDate );

      Date forecastDate =
            runValidateFindForecast( Integer.parseInt( strINVDBID ), Integer.parseInt( strINVID ),
                  Integer.parseInt( mimDTtype.get( 1 ).getDATA_TYPE_DB_ID() ),
                  Integer.parseInt( mimDTtype.get( 1 ).getDATA_TYPE_ID() ), 400, currentDate );

      long days = DateDiffInDays( currentDate, forecastDate );
      Assert.assertTrue( "The actual days: " + days + " days", days >= 414 || days <= 425 );

   }


   /**
    * Verify forecast date greater than 12 months with BLK and with mim_data_type=cycle and ending
    * date of forecasting falls on blk period.
    *
    */

   @Test
   public void test_FindForcastGreaterrThan12MonthesBLK_EndDate_overlap_Cycle() {

      Date currentDate = new java.sql.Date( Calendar.getInstance().getTime().getTime() );
      Date startdate = addDaysToCurrentDate( 396 );
      Date endDate = addDaysToCurrentDate( 410 );
      addBLKSCHEDDate( startdate, endDate );

      Date forecastDate =
            runValidateFindForecast( Integer.parseInt( strINVDBID ), Integer.parseInt( strINVID ),
                  Integer.parseInt( mimDTtype.get( 1 ).getDATA_TYPE_DB_ID() ),
                  Integer.parseInt( mimDTtype.get( 1 ).getDATA_TYPE_ID() ), 400, currentDate );

      long days = DateDiffInDays( currentDate, forecastDate );
      Assert.assertTrue( "The actual days: " + days + " days", days >= 408 && days <= 421 );

   }


   /**
    * This is negative test case when remaining usage is negative (mim_data_type=hour)
    *
    */
   @Test
   public void test_FindForcastNegativeRemainingUsg_Hour() {
      Date currentDate = new java.sql.Date( Calendar.getInstance().getTime().getTime() );

      Date forecastDate =
            runValidateFindForecast( Integer.parseInt( strINVDBID ), Integer.parseInt( strINVID ),
                  Integer.parseInt( mimDTtype.get( 0 ).getDATA_TYPE_DB_ID() ),
                  Integer.parseInt( mimDTtype.get( 0 ).getDATA_TYPE_ID() ), -200, currentDate );

      long days = DateDiffInDays( currentDate, forecastDate );
      Assert.assertTrue( "The actual days: " + days + " days", days == -1 );

   }


   /**
    * This is negative test case when remaining usage is negative (mim_data_type=cycle)
    *
    */

   @Test
   public void test_FindForcastNegativeRemainingUsg_Cycle() {

      Date currentDate = new java.sql.Date( Calendar.getInstance().getTime().getTime() );

      Date forecastDate =
            runValidateFindForecast( Integer.parseInt( strINVDBID ), Integer.parseInt( strINVID ),
                  Integer.parseInt( mimDTtype.get( 1 ).getDATA_TYPE_DB_ID() ),
                  Integer.parseInt( mimDTtype.get( 1 ).getDATA_TYPE_ID() ), -200, currentDate );

      long days = DateDiffInDays( currentDate, forecastDate );
      Assert.assertTrue( "The actual days: " + days + " days", days == -1 );

   }

}
