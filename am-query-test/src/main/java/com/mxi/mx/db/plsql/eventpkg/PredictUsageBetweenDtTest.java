
package com.mxi.mx.db.plsql.eventpkg;

import static com.mxi.mx.core.key.DataTypeKey.HOURS;
import static com.mxi.mx.core.key.DataTypeKey.LANDING;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.ForecastModel;
import com.mxi.am.domain.builder.BlackoutBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.utils.DataTypeUtils;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.key.AircraftKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.FcModelKey;
import com.mxi.mx.core.table.inv.InvAcReg;
import com.mxi.mx.core.unittest.ProcedureStatementFactory;
import com.mxi.mx.persistence.uuid.SequentialUuidGenerator;
import com.mxi.mx.persistence.uuid.UuidUtils;


/**
 * Tests for the EVENT_PKG.PredictUsageBetweenDt PLSQL procedure.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class PredictUsageBetweenDtTest {

   private static final Double CURRENT_LANDINGS = 100.0;
   private static final Double CURRENT_FLYING_HOURS = 200.0;
   private static final Double LANDINGS_PER_FLIGHT = 1.0;
   private static final Double LANDINGS_PER_DAY = 10.0;
   private static final Double FLYING_HOURS_PER_DAY = 6.0;

   private static final DataTypeKey NON_FLIGHT = new DataTypeKey( 1, 1 );
   private static final Double NON_FLIGHT_USAGE_PER_DAY = 100.0;
   private static final Double CURRENT_NON_FLIGHT_USAGE = 1000.0;

   // Note: test with 2013 due to no leap year
   private static final Date NOW = DateUtils.getDateTime( 2013, 02, 17, 11, 59, 59 );

   private static final Date START_DATE = NOW;
   private static final Date TARGET_DATE = DateUtils.getDateTime( 2013, 11, 25, 11, 59, 59 );

   private static final Date JAN_1_EOD = DateUtils.getEndOfDay( DateUtils.getDate( 2013, 01, 01 ) );
   private static final Date MAR_1_EOD = DateUtils.getEndOfDay( DateUtils.getDate( 2013, 02, 01 ) );
   private static final Date MAR_20_EOD =
         DateUtils.getEndOfDay( DateUtils.getDate( 2013, 02, 20 ) );
   private static final Date APR_7_EOD = DateUtils.getEndOfDay( DateUtils.getDate( 2013, 03, 07 ) );
   private static final Date APR_14_EOD =
         DateUtils.getEndOfDay( DateUtils.getDate( 2013, 03, 14 ) );
   private static final Date MAY_1_EOD = DateUtils.getEndOfDay( DateUtils.getDate( 2013, 04, 01 ) );
   private static final Date AUG_1_EOD = DateUtils.getEndOfDay( DateUtils.getDate( 2013, 07, 01 ) );
   private static final Date DEC_27_EOD =
         DateUtils.getEndOfDay( DateUtils.getDate( 2013, 11, 27 ) );
   private static final Date DEC_31_EOD =
         DateUtils.getEndOfDay( DateUtils.getDate( 2013, 11, 31 ) );
   private static final Date JAN_10_2014_EOD =
         DateUtils.getEndOfDay( DateUtils.getDate( 2014, 00, 10 ) );

   private AircraftKey iAircraft;
   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Ensure that when there is a blackout period spanning the start date, that the usage is
    * predicted from the end of the blackout period to the target date.
    *
    * @throws Exception
    */
   @Test
   public void testBlackoutSpanningStartDate() throws Exception {

      Date lBlackoutStart = DateUtils.getEndOfPrevDay( DateUtils.addDays( START_DATE, -2 ) );
      Date lBlackoutEnd = DateUtils.getEndOfDay( DateUtils.addDays( START_DATE, 2 ) );

      withBlackout( lBlackoutStart, lBlackoutEnd );

      withSimpleForecastModel( HOURS, FLYING_HOURS_PER_DAY );

      Double lPredictedUsage = predictUsageBetweenDt( iAircraft, HOURS, START_DATE, TARGET_DATE,
            CURRENT_FLYING_HOURS );

      Double lExpectedUsage = CURRENT_FLYING_HOURS
            + getForecastUsage( lBlackoutEnd, TARGET_DATE, FLYING_HOURS_PER_DAY );

      assertEquals( lExpectedUsage, lPredictedUsage );
   }


   /**
    * Ensure that when there is a blackout period spanning the target date, that the usage is
    * predicted from the start date to the start of the blackout period.
    *
    * @throws Exception
    */
   @Test
   public void testBlackoutSpanningTargetDate() throws Exception {

      Date lBlackoutStart = DateUtils.getEndOfPrevDay( DateUtils.addDays( TARGET_DATE, -2 ) );
      Date lBlackoutEnd = DateUtils.getEndOfDay( DateUtils.addDays( TARGET_DATE, 2 ) );

      withBlackout( lBlackoutStart, lBlackoutEnd );

      withSimpleForecastModel( HOURS, FLYING_HOURS_PER_DAY );

      Double lPredictedUsage = predictUsageBetweenDt( iAircraft, HOURS, START_DATE, TARGET_DATE,
            CURRENT_FLYING_HOURS );

      Double lExpectedUsage =
            CURRENT_FLYING_HOURS + getForecastUsageFromStartOfStartDay( START_DATE, lBlackoutStart,
                  FLYING_HOURS_PER_DAY );

      assertEquals( lExpectedUsage, lPredictedUsage );
   }


   /**
    * Ensure that when using a forecast model and the same target date, that the predicted usage is
    * the same regardless of the time of day for the start date (but on the same day).
    */
   @Test
   public void testForecastUsageOnStartDateRegardlessOfTimeOfStartDate() {
      withSimpleForecastModel( LANDING, LANDINGS_PER_DAY );

      Double lPredictedUsage;

      // Simulate a task scheduled from an effective date in the future (end of day).
      Date lTargetDate = DateUtils.getEndOfDay( TARGET_DATE );

      // Regardless of the start time on the same day, the expected usage will be the same.
      Date lStartDate = START_DATE;
      Double lExpectedUsage = CURRENT_LANDINGS
            + getForecastUsageFromStartOfStartDay( lStartDate, lTargetDate, LANDINGS_PER_DAY );

      // Simulate the "generate task deadlines" job being run at various times throughout the day.
      lStartDate = DateUtils.ceilDay( START_DATE );
      lPredictedUsage =
            predictUsageBetweenDt( iAircraft, LANDING, lStartDate, lTargetDate, CURRENT_LANDINGS );
      assertEquals( lExpectedUsage, lPredictedUsage );

      lStartDate = DateUtils.floorDay( START_DATE );
      lPredictedUsage =
            predictUsageBetweenDt( iAircraft, LANDING, lStartDate, lTargetDate, CURRENT_LANDINGS );
      assertEquals( lExpectedUsage, lPredictedUsage );

      lStartDate = DateUtils.addTime( DateUtils.floorDay( START_DATE ), 4.25 );
      lPredictedUsage =
            predictUsageBetweenDt( iAircraft, LANDING, lStartDate, lTargetDate, CURRENT_LANDINGS );
      assertEquals( lExpectedUsage, lPredictedUsage );

      lStartDate = DateUtils.addTime( DateUtils.floorDay( START_DATE ), 18.1 );
      lPredictedUsage =
            predictUsageBetweenDt( iAircraft, LANDING, lStartDate, lTargetDate, CURRENT_LANDINGS );
      assertEquals( lExpectedUsage, lPredictedUsage );
   }


   /**
    * Ensure that when there are multiple planned flights that have blackout periods between them
    * (but the blackout periods do not overlap the flights), that the blackout periods are ignored
    * and only the planned flight usage is used for the prediction.<br>
    * <br>
    * Note; the last flight will be on the target date to avoid involving the forecast model.
    *
    * @throws Exception
    */
   @Test
   public void testMultipleFlightsWithBlackoutBetweenThem() throws Exception {

      Double lTotalLandings = 0.0;

      // first flight is after start date but earlier then target date
      Date lFlightDep = DateUtils.addDays( START_DATE, 2 );
      Date lFlightArr = DateUtils.addDays( START_DATE, 3 );
      assertTrue( lFlightArr.before( DateUtils.floorDay( TARGET_DATE ) ) );
      withPlannedFlight( lFlightDep, lFlightArr );

      lTotalLandings += LANDINGS_PER_FLIGHT;

      // blackout period after the first flight but prior to the last flight
      Date lBlackoutStart = DateUtils.addDays( lFlightArr, 2 );
      Date lBlackoutEnd = DateUtils.addDays( lFlightArr, 3 );
      assertTrue( lBlackoutEnd.before( DateUtils.floorDay( TARGET_DATE ) ) );
      withBlackout( lBlackoutStart, lBlackoutEnd );

      // last flight is on the target date (earlier in the day)
      lFlightDep = DateUtils.addHours( TARGET_DATE, -4 );
      lFlightArr = DateUtils.addHours( TARGET_DATE, -2 );
      assertTrue( lFlightDep.after( lBlackoutEnd ) );
      assertTrue( lFlightDep.after( DateUtils.floorDay( TARGET_DATE ) ) );
      withPlannedFlight( lFlightDep, lFlightArr );

      lTotalLandings += LANDINGS_PER_FLIGHT;

      withSimpleForecastModel( LANDING, LANDINGS_PER_DAY );

      double lPredictedUsage =
            predictUsageBetweenDt( iAircraft, LANDING, START_DATE, TARGET_DATE, CURRENT_LANDINGS );

      // The expected usage includes all the flights' LANDING usage (1 per flight) plus the current
      // usage BUT the blackout period has no affect. Also, because the last flight is on the same
      // day as the target date the forecast model is not used.
      assertEquals( CURRENT_LANDINGS + lTotalLandings, lPredictedUsage, 0f );
   }


   /**
    * Ensure that when there are multiple planned flights, that the predicted usage for HOURS is
    * calculated using the flying hours of each flight.<br>
    * <br>
    * Note; the last flight will be on the target date to avoid involving the forecast model.
    *
    * @throws Exception
    */
   @Test
   public void testMultipleFlightsWithLastFlightOnTargetDayWithHoursUsage() throws Exception {
      Double lTotalFlyingHours = 0.0;

      // first flight is after start date but earlier then target date
      Date lFlightDep = DateUtils.addDays( START_DATE, 2 );
      Date lFlightArr = DateUtils.addDays( START_DATE, 3 );
      assertTrue( lFlightArr.before( DateUtils.floorDay( TARGET_DATE ) ) );
      withPlannedFlight( lFlightDep, lFlightArr );

      lTotalFlyingHours += DateUtils.absoluteDifferenceInHours( lFlightArr, lFlightDep );

      // second flight is after first flight but earlier then target date
      lFlightDep = DateUtils.addDays( lFlightArr, 2 );
      lFlightArr = DateUtils.addDays( lFlightArr, 3 );
      assertTrue( lFlightArr.before( DateUtils.floorDay( TARGET_DATE ) ) );
      withPlannedFlight( lFlightDep, lFlightArr );

      lTotalFlyingHours += DateUtils.absoluteDifferenceInHours( lFlightArr, lFlightDep );

      // last flight is earlier in the day on the target date
      lFlightDep = DateUtils.addHours( TARGET_DATE, -4 );
      lFlightArr = DateUtils.addHours( TARGET_DATE, -2 );
      assertTrue( lFlightDep.after( DateUtils.floorDay( TARGET_DATE ) ) );
      withPlannedFlight( lFlightDep, lFlightArr );

      lTotalFlyingHours += DateUtils.absoluteDifferenceInHours( lFlightArr, lFlightDep );

      withSimpleForecastModel( HOURS, 10.0 );

      double lPredictedUsage = predictUsageBetweenDt( iAircraft, HOURS, START_DATE, TARGET_DATE,
            CURRENT_FLYING_HOURS );

      // The expected usage includes all the flights' HOURS usage (flying hours per flight) plus the
      // current usage. Because the last flight is on the same day as the target date the forecast
      // model is not used.
      assertEquals( CURRENT_FLYING_HOURS + lTotalFlyingHours, lPredictedUsage, 0f );
   }


   /**
    * Ensure that when there are multiple planned flights, that the predicted usage for LANDING is
    * calculated using the landings of each flight (which is hard coded to 1 per flight).<br>
    * <br>
    * Note; the last flight will be on the target date to avoid involving the forecast model.
    *
    * @throws Exception
    */
   @Test
   public void testMultipleFlightsWithLastFlightOnTargetDayWithLandingUsage() throws Exception {

      // first flight is after start date but earlier then target date
      Date lFlightDep = DateUtils.addDays( START_DATE, 2 );
      Date lFlightArr = DateUtils.addDays( START_DATE, 3 );
      assertTrue( lFlightArr.before( DateUtils.floorDay( TARGET_DATE ) ) );
      withPlannedFlight( lFlightDep, lFlightArr );

      // second flight is after first flight but earlier then target date
      lFlightDep = DateUtils.addDays( lFlightArr, 2 );
      lFlightArr = DateUtils.addDays( lFlightArr, 3 );
      assertTrue( lFlightArr.before( DateUtils.floorDay( TARGET_DATE ) ) );
      withPlannedFlight( lFlightDep, lFlightArr );

      // last flight is earlier in the day on the target date
      lFlightDep = DateUtils.addHours( TARGET_DATE, -4 );
      lFlightArr = DateUtils.addHours( TARGET_DATE, -2 );
      assertTrue( lFlightDep.after( DateUtils.floorDay( TARGET_DATE ) ) );

      withPlannedFlight( lFlightDep, lFlightArr );
      withSimpleForecastModel( DataTypeKey.LANDING, 10.0 );

      double lPredictedUsage = predictUsageBetweenDt( iAircraft, DataTypeKey.LANDING, START_DATE,
            TARGET_DATE, CURRENT_LANDINGS );

      // The expected usage includes all the flights' LANDING usage (1 per flight) plus the current
      // usage. Because the last flight is on the same day as the target date the forecast model is
      // not used.
      assertEquals( CURRENT_LANDINGS + 3, lPredictedUsage, 0f );
   }


   /**
    * Ensure that when there are multiple planned flights and the last flight is prior to the start
    * of the target day, that the predicted usage is calculated with both the anticipated flight
    * usage and the anticipated forecast usage. The anticipated forecast usage is calculated from
    * the end-of-day of the final flight until the target date because it is assumed that planned
    * flights are planned until the end of the day.
    *
    * @throws Exception
    */
   @Test
   public void testMultipleFlightsWithLastFlightPriorToTargetDay() throws Exception {

      Double lTotalFlyingHours = 0.0;

      // first flight is after start date but earlier then target date
      Date lFlightDep = DateUtils.addDays( START_DATE, 2 );
      Date lFlightArr = DateUtils.addDays( START_DATE, 3 );
      assertTrue( lFlightArr.before( DateUtils.floorDay( TARGET_DATE ) ) );
      withPlannedFlight( lFlightDep, lFlightArr );

      lTotalFlyingHours += DateUtils.absoluteDifferenceInHours( lFlightArr, lFlightDep );

      // second flight is after first flight but earlier then target date
      lFlightDep = DateUtils.addDays( lFlightArr, 2 );
      lFlightArr = DateUtils.addDays( lFlightArr, 3 );
      assertTrue( lFlightArr.before( DateUtils.floorDay( TARGET_DATE ) ) );
      withPlannedFlight( lFlightDep, lFlightArr );

      lTotalFlyingHours += DateUtils.absoluteDifferenceInHours( lFlightArr, lFlightDep );

      // last flight is after second flight but earlier then target date
      lFlightDep = DateUtils.addDays( lFlightArr, 2 );
      lFlightArr = DateUtils.addDays( lFlightArr, 3 );
      assertTrue( lFlightArr.before( DateUtils.floorDay( TARGET_DATE ) ) );
      withPlannedFlight( lFlightDep, lFlightArr );

      Date lLastFlightArrDate = lFlightArr;

      lTotalFlyingHours += DateUtils.absoluteDifferenceInHours( lFlightArr, lFlightDep );

      withSimpleForecastModel( HOURS, FLYING_HOURS_PER_DAY );

      Double lPredictedUsage = predictUsageBetweenDt( iAircraft, HOURS, START_DATE, TARGET_DATE,
            CURRENT_FLYING_HOURS );

      // The expected usage includes the current usage plus all the flights' HOURS usage (flying
      // hours per flight) plus usage from the forecast model between the end-of-day of the last
      // flight till the target date.
      Double lExpectedUsage =
            CURRENT_FLYING_HOURS + lTotalFlyingHours + getForecastUsageFromEndOfStartDay(
                  lLastFlightArrDate, TARGET_DATE, FLYING_HOURS_PER_DAY );

      assertEquals( lExpectedUsage, lPredictedUsage );
   }


   /**
    * Ensure that when there are no planned flights and there is no forecast model, that the
    * predicted usage is only the start usage.
    *
    * @throws Exception
    */
   @Test
   public void testNoPlannedFlightsAndNoForecastModel() throws Exception {

      Double lPredictedUsage =
            predictUsageBetweenDt( iAircraft, LANDING, START_DATE, TARGET_DATE, CURRENT_LANDINGS );

      assertEquals( CURRENT_LANDINGS, lPredictedUsage );
   }


   /**
    * Ensure that if the first planned flight is after the target date, that the predicted usage is
    * only the start usage. Also ensuring the forecast model is not used.
    *
    * @throws Exception
    */
   @Test
   public void testPlannedFlightAfterTargetDate() throws Exception {

      Date lFlightDep = DateUtils.addDays( TARGET_DATE, 1 );
      Date lFlightArr = DateUtils.addDays( TARGET_DATE, 2 );

      withPlannedFlight( lFlightDep, lFlightArr );

      withSimpleForecastModel( DataTypeKey.LANDING, 10.0 );

      Double lPredictedUsage = predictUsageBetweenDt( iAircraft, DataTypeKey.LANDING, START_DATE,
            TARGET_DATE, CURRENT_LANDINGS );

      // there is a flight but its after the target date so do not use forecast model
      assertEquals( CURRENT_LANDINGS, lPredictedUsage );
   }


   /**
    * Ensure that if the only planned flight is on the target date, that the predicted LANDING usage
    * is calculated using that flight, but not the forecast model.
    *
    * @throws Exception
    */
   @Test
   public void testPlannedFlightOnTargetDayWithLandingUsage() throws Exception {

      // flight is earlier in the day on the target date
      Date lFlightDep = DateUtils.addHours( TARGET_DATE, -4 );
      Date lFlightArr = DateUtils.addHours( TARGET_DATE, -2 );
      assertTrue( lFlightDep.after( DateUtils.floorDay( TARGET_DATE ) ) );

      withPlannedFlight( lFlightDep, lFlightArr );
      withSimpleForecastModel( DataTypeKey.LANDING, 10.0 );

      double lPredictedUsage = predictUsageBetweenDt( iAircraft, DataTypeKey.LANDING, START_DATE,
            TARGET_DATE, CURRENT_LANDINGS );

      // The expected usage includes the one flight's LANDING usage (1 per flight) plus the current
      // usage. Because the flight is on the same day as the target date the forecast model is not
      // used.
      assertEquals( CURRENT_LANDINGS + 1, lPredictedUsage, 0f );
   }


   /**
    * Ensure that if the only planned flight spans the target date (the departure is prior to the
    * target date and the arrival is after), that the predicted LANDING usage does not use that
    * flight. As the anticipated usage for flights is based on the flight arrival. Also, the
    * forecast model is not used.
    *
    * @throws Exception
    */
   @Test
   public void testPlannedFlightSpansTargetDateWithLandingUsage() throws Exception {

      // The flight departs prior to the target date and arrives after.
      Date lFlightDep = DateUtils.addDays( TARGET_DATE, -1 );
      Date lFlightArr = DateUtils.addDays( TARGET_DATE, 1 );

      withPlannedFlight( lFlightDep, lFlightArr );
      withSimpleForecastModel( DataTypeKey.LANDING, 10.0 );

      Double lPredictedUsage = predictUsageBetweenDt( iAircraft, DataTypeKey.LANDING, START_DATE,
            TARGET_DATE, CURRENT_LANDINGS );

      // Because the flight arrives after the target date, neither the flight usage nor the
      // forecast model is used.
      assertEquals( CURRENT_LANDINGS, lPredictedUsage );
   }


   /**
    * Ensure that when a complext forecast model is used, that each forecast range and rate is
    * properly calculated. A complex forecast model is one where the lenght and rates of the ranges
    * differ. As well, the ranges start and end on various days of the month.
    *
    * @throws Exception
    */
   @Test
   public void testWithComplexForecastModel() throws Exception {

      double lYearlyUsage = withComplexForecastModel( LANDING );

      Double lPredictedUsage =
            predictUsageBetweenDt( iAircraft, LANDING, JAN_1_EOD, DEC_31_EOD, CURRENT_LANDINGS );

      Double lExpectedUsage = CURRENT_LANDINGS + lYearlyUsage;

      assertEquals( lExpectedUsage, lPredictedUsage );
   }


   /**
    * Ensure that when using a complex forecast model with various blackout periods, that the
    * predicted usage does not include the anticipated usage for the blackouts. As well, ensure the
    * anticipated usage for the blackouts is properly calculated and takes into account partial
    * blackouts (ones that span the start and/or target dates), blackouts that span multiple
    * forecast ranges (with varying periods and rates), and blackouts that are contained entirely
    * within a forecast range.
    *
    * @throws Exception
    */
   @Test
   public void testWithComplexForecastModelAndBlackouts() throws Exception {

      double lYearlyUsage = withComplexForecastModel( LANDING );

      Double lTotalBlackoutUsage = 0.0;

      // for blackout period dates and rates refer to withComplexForecastModel()

      // blackout spanning start of first forecast range
      withBlackout( MAR_1_EOD, MAR_20_EOD );
      lTotalBlackoutUsage += ( 4 * 2 );
      // J-
      // Explanation:
      // due to blackout starting before first forecast range, use the first forecast range start
      // (2 * 2) => days between range start and blackout end * range rate
      // => (Mar 20 - Mar 17) * 2
      // => 3 * 2
      // J+

      // blackout spanning end of last forecast period (ending EOD DEC 31)
      withBlackout( DEC_27_EOD, JAN_10_2014_EOD );
      lTotalBlackoutUsage += ( 4 * 21 );

      // blackout contained with a forecast period
      withBlackout( APR_7_EOD, APR_14_EOD );
      lTotalBlackoutUsage += ( 7 * 3 );

      // blackout spanning more than one forecast period
      withBlackout( MAY_1_EOD, AUG_1_EOD );
      lTotalBlackoutUsage += ( 22 * 3 ) + ( 38 * 5 ) + ( 32 * 8 );

      Double lPredictedUsage =
            predictUsageBetweenDt( iAircraft, LANDING, JAN_1_EOD, DEC_31_EOD, CURRENT_LANDINGS );

      Double lExpectedUsage = CURRENT_LANDINGS + lYearlyUsage - lTotalBlackoutUsage;

      assertEquals( lExpectedUsage, lPredictedUsage );
   }


   /**
    * Ensure that HOURS usage is properly predicted using the forecast model. Thus, the total
    * anticipated flying hours are properly calculated.
    *
    * @throws Exception
    */
   @Test
   public void testWithFlyingHoursForecastModel() throws Exception {

      withSimpleForecastModel( HOURS, FLYING_HOURS_PER_DAY );

      Double lPredictedUsage = predictUsageBetweenDt( iAircraft, HOURS, START_DATE, TARGET_DATE,
            CURRENT_FLYING_HOURS );

      Double lExpectedUsage = CURRENT_FLYING_HOURS
            + getForecastUsageFromStartOfStartDay( START_DATE, TARGET_DATE, FLYING_HOURS_PER_DAY );

      assertEquals( lExpectedUsage, lPredictedUsage );
   }


   /**
    * Ensure the predicated usage is properly calculated for a usage that is not a flight usage
    * (i.e. not HOURS, CYCLES, nor LANDING).
    */
   @Test
   public void testWithForecastModelForNonFlightUsage() {

      withSimpleForecastModel( NON_FLIGHT, NON_FLIGHT_USAGE_PER_DAY );

      Double lPredictedUsage = predictUsageBetweenDt( iAircraft, NON_FLIGHT, START_DATE,
            TARGET_DATE, CURRENT_NON_FLIGHT_USAGE );

      Double lExpectedUsage =
            CURRENT_NON_FLIGHT_USAGE + getForecastUsageFromStartOfStartDay( START_DATE, TARGET_DATE,
                  NON_FLIGHT_USAGE_PER_DAY );

      assertEquals( lExpectedUsage, lPredictedUsage );
   }


   /**
    * Ensure that when using a forecast model that is missing the first range (i.e. JAN 1) and
    * predicting usage for the entire year, that the missing range is handled as if it were present
    * with a rate of 0.
    *
    * @throws Exception
    */
   @Test
   public void testWithForecastModelThatIsMissingJan01() throws Exception {

      double lYearlyUsage = withComplexForecastModel( LANDING );

      Double lPredictedUsage =
            predictUsageBetweenDt( iAircraft, LANDING, JAN_1_EOD, DEC_31_EOD, CURRENT_LANDINGS );

      Double lExpectedUsage = CURRENT_LANDINGS + lYearlyUsage;

      assertEquals( lExpectedUsage, lPredictedUsage );
   }


   /**
    * Ensure that when there are actual flights on the start day (prior to the start date's time)
    * and the usage from those flights is less than the forecasted usage, that the predicated usage
    * only includes the difference between the forecasted and flights' usage. This is because the
    * flights' usage is already included in the current usage and this cannot be counted twice.
    * However, since the flights' usage is less then the forecasted usage, the remaining forecast
    * usage must be counted.
    *
    * @throws Exception
    */
   @Test
   public void testWithLessActualFlightUsageThanModelledOnStartDay() throws Exception {

      Double lUsageFromActualFlights = 0.0;

      // create a flight earlier in the day of the start date
      Date lDepDate = DateUtils.addHours( DateUtils.floorDay( START_DATE ), 1 );
      Date lArrDate = DateUtils.addHours( lDepDate, 1 );
      assertTrue( lArrDate.before( START_DATE ) );
      withActualFlight( lDepDate, lArrDate, LANDING, LANDINGS_PER_FLIGHT );
      lUsageFromActualFlights += LANDINGS_PER_FLIGHT;

      // create a flight earlier in the day of the start date
      lDepDate = DateUtils.addHours( DateUtils.floorDay( START_DATE ), 3 );
      lArrDate = DateUtils.addHours( lDepDate, 1 );
      assertTrue( lArrDate.before( START_DATE ) );
      withActualFlight( lDepDate, lArrDate, LANDING, LANDINGS_PER_FLIGHT );
      lUsageFromActualFlights += LANDINGS_PER_FLIGHT;

      withSimpleForecastModel( LANDING, LANDINGS_PER_DAY );

      Double lPredictedUsage =
            predictUsageBetweenDt( iAircraft, LANDING, START_DATE, TARGET_DATE, CURRENT_LANDINGS );

      Double lExpectedUsage = CURRENT_LANDINGS - lUsageFromActualFlights
            + getForecastUsageFromStartOfStartDay( START_DATE, TARGET_DATE, LANDINGS_PER_DAY );

      assertEquals( lExpectedUsage, lPredictedUsage );
   }


   /**
    * Ensure that when there are actual flights on the start day (prior to the start date's time)
    * and the usage from those flights is more than the forecasted usage, that the predicated usage
    * does not include any anticipated usage from the forecast model for that start day. This is
    * because the flights' usage is already included in the current usage and and since it exceeds
    * the forecast usage there is no need to anticipate any more.
    *
    * @throws Exception
    */
   @Test
   public void testWithMoreActualFlightsThanModeledOnStartDate() throws Exception {

      Double lUsageFromActualFlights = 0.0;

      // create more flights then the daily rate, all prior to the start time
      int lNumFlights = LANDINGS_PER_DAY.intValue() + 2;
      Date lDepDate = DateUtils.addTime( DateUtils.floorDay( START_DATE ), 0.25 );
      Date lArrDate = DateUtils.addTime( lDepDate, 0.25 );

      while ( lNumFlights-- > 0 ) {
         assertTrue( lArrDate.before( START_DATE ) );
         withActualFlight( lDepDate, lArrDate, LANDING, LANDINGS_PER_FLIGHT );
         lUsageFromActualFlights += LANDINGS_PER_FLIGHT;

         lDepDate = DateUtils.addTime( lArrDate, 0.25 );
         lArrDate = DateUtils.addTime( lDepDate, 0.25 );
      }

      withSimpleForecastModel( LANDING, LANDINGS_PER_DAY );

      Double lPredictedUsage =
            predictUsageBetweenDt( iAircraft, LANDING, START_DATE, TARGET_DATE, CURRENT_LANDINGS );

      Double lExpectedUsage = CURRENT_LANDINGS
            + getForecastUsageFromEndOfStartDay( START_DATE, TARGET_DATE, LANDINGS_PER_DAY );

      assertEquals( lExpectedUsage, lPredictedUsage );
   }


   /**
    * Ensure that when there are flight usage adjustments on the start day (prior to the start
    * date's time) and that usage is negative, that the predicated usage only includes the negative
    * difference between the forecasted and adjusted usage. This is because the adjusted usage is
    * already removed from the current usage and this cannot be counted twice.
    *
    * @throws Exception
    */
   @Test
   public void testWithNegativeActualFlightUsageOnStartDay() throws Exception {
      Double lUsageAdjustment = LANDINGS_PER_DAY * -2;

      // Create a negative usage adjustment, prior to but on the day of the start date.
      Date lAdjustmentDate = DateUtils.addHours( DateUtils.floorDay( START_DATE ), 1 );
      assertTrue( lAdjustmentDate.before( START_DATE ) );
      withFlightUsageAdjustment( lAdjustmentDate, LANDING, lUsageAdjustment );

      withSimpleForecastModel( LANDING, LANDINGS_PER_DAY );

      Double lPredictedUsage =
            predictUsageBetweenDt( iAircraft, LANDING, START_DATE, TARGET_DATE, CURRENT_LANDINGS );

      Double lExpectedUsage = CURRENT_LANDINGS - lUsageAdjustment
            + getForecastUsageFromStartOfStartDay( START_DATE, TARGET_DATE, LANDINGS_PER_DAY );

      assertEquals( lExpectedUsage, lPredictedUsage );
   }


   /**
    * Ensure that when there are actual flights on the start day (prior to the start date's time)
    * and the usage from those flights is equals the forecasted usage, that the predicated usage
    * does not include any anticipated usage from the forecast model for that start day. This is
    * because the flights' usage is already included in the current usage and and since it equals
    * the forecast usage there is no need to anticipate any more.
    *
    * @throws Exception
    */
   @Test
   public void testWithSameActualFlightsAsModeledOnStartDate() throws Exception {

      Double lUsageFromActualFlights = 0.0;

      // create more flights then the daily rate, all prior to the start time
      int lNumFlights = LANDINGS_PER_DAY.intValue() + 2;
      Date lDepDate = DateUtils.addTime( DateUtils.floorDay( START_DATE ), 0.25 );
      Date lArrDate = DateUtils.addTime( lDepDate, 0.25 );

      while ( lNumFlights-- > 0 ) {
         assertTrue( lArrDate.before( START_DATE ) );
         withActualFlight( lDepDate, lArrDate, LANDING, LANDINGS_PER_FLIGHT );
         lUsageFromActualFlights += LANDINGS_PER_FLIGHT;

         lDepDate = DateUtils.addTime( lArrDate, 0.25 );
         lArrDate = DateUtils.addTime( lDepDate, 0.25 );
      }

      withSimpleForecastModel( LANDING, LANDINGS_PER_DAY );

      Double lPredictedUsage =
            predictUsageBetweenDt( iAircraft, LANDING, START_DATE, TARGET_DATE, CURRENT_LANDINGS );

      Double lExpectedUsage = CURRENT_LANDINGS
            + getForecastUsageFromEndOfStartDay( START_DATE, TARGET_DATE, LANDINGS_PER_DAY );

      assertEquals( lExpectedUsage, lPredictedUsage );
   }


   /**
    * {@inheritDoc}
    */
   @Before
   public void loadData() throws Exception {
      // create a test aircraft
      iAircraft = new AircraftKey( Domain.createAircraft() );
   }


   /**
    * Get the forecast usage for a simple forecast model that anticipates a constant daily rate. The
    * forecasted usage is calculated using the provided rate, from the start date and time until the
    * end date and time
    *
    * @param aStart
    *           start date and time
    * @param aEnd
    *           end date and time
    * @param aRate
    *           daily rate
    *
    * @return forecasted usage
    */
   private double getForecastUsage( Date aStart, Date aEnd, double aRate ) {
      return ( DateUtils.absoluteDifferenceInFractionalDays( aStart, aEnd ) ) * aRate;
   }


   /**
    * Get the forecast usage for a simple forecast model that anticipates a constant daily rate.
    * However, start the calculation at the end-of-day of the provided start date.
    *
    * @param aStart
    *           start date used to determine the end-of-day start
    * @param aEnd
    *           end date and time
    * @param aRate
    *           daily rate
    *
    * @return forecasted usage
    */
   private double getForecastUsageFromEndOfStartDay( Date aStart, Date aEnd, double aRate ) {
      return ( DateUtils.absoluteDifferenceInFractionalDays( DateUtils.ceilDay( aStart ), aEnd ) )
            * aRate;
   }


   /**
    * Get the forecast usage for a simple forecast model that anticipates a constant daily rate.
    * However, start the calculation at the start-of-day of the provided start date.
    *
    * @param aStart
    *           start date used to determine the start-of-day start
    * @param aEnd
    *           end date and time
    * @param aRate
    *           daily rate
    *
    * @return forecasted usage
    */
   private double getForecastUsageFromStartOfStartDay( Date aStart, Date aEnd, double aRate ) {
      return ( DateUtils.absoluteDifferenceInFractionalDays( DateUtils.getEndOfPrevDay( aStart ),
            aEnd ) ) * aRate;
   }


   /**
    * Call the EVENT_PKG.PredictUsageBetweenDt PLSQL procedure.
    *
    * @param aAircraft
    *           aircraft key
    * @param aDataType
    *           date type key
    * @param aStartDate
    *           start date
    * @param aTargetDate
    *           target date
    * @param aStartUsage
    *           start usage
    *
    * @return predicated usage
    */
   private Double predictUsageBetweenDt( AircraftKey aAircraft, DataTypeKey aDataType,
         Date aStartDate, Date aTargetDate, Double aStartUsage ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aAircraft, "aAircraftDbId", "aAircraftId" );
      lArgs.add( aDataType, "an_DataTypeDbId", "an_DataTypeId" );
      lArgs.add( "ad_StartDate", aStartDate );
      lArgs.add( "ad_TargetDate", aTargetDate );
      lArgs.add( "an_StartUsageQt", aStartUsage );

      DataSetArgument lOutArgs = new DataSetArgument();
      lOutArgs.add( "on_TsnQt", DataTypeUtils.DOUBLE );
      lOutArgs.add( "on_Return", DataTypeUtils.INTEGER );

      ProcedureStatementFactory.execute( iDatabaseConnectionRule.getConnection(),
            "EVENT_PKG.PredictUsageBetweenDt", lArgs, lOutArgs );

      int lReturn = lOutArgs.getInteger( "on_Return" );
      assertEquals(
            "Unexpected return value from procedure EVENT_PKG.PredictUsageBetweenDt: " + lReturn,
            lReturn, 1 );

      return lOutArgs.getDouble( "on_TsnQt" );
   }


   /**
    * Create an actual (historic) flight in the DB with the provided usage.
    *
    * @param aDepartureDate
    * @param aArrivalDate
    * @param aDataType
    * @param aUsage
    */
   private void withActualFlight( Date aDepartureDate, Date aArrivalDate, DataTypeKey aDataType,
         Double aUsage ) {

      DataSetArgument lArgs;
      int lRowsInserted;

      // create the historic flight record using provided flight dates
      lArgs = new DataSetArgument();
      lArgs.add( "leg_id", UuidUtils.toHexString( new SequentialUuidGenerator().newUuid() ) );
      lArgs.add( "aircraft_db_id", iAircraft.getDbId() );
      lArgs.add( "aircraft_id", iAircraft.getId() );
      lArgs.add( "hist_bool", true );
      lArgs.add( "sched_departure_dt", aDepartureDate );
      lArgs.add( "sched_arrival_dt", aArrivalDate );
      lArgs.add( "actual_departure_dt", aDepartureDate );
      lArgs.add( "actual_arrival_dt", aArrivalDate );

      lRowsInserted = MxDataAccess.getInstance().executeInsert( "FL_LEG", lArgs );
      assertEquals( "Failed to create planned flight.", lRowsInserted, 1 );

      // create usage record for flight with provided data type
      String lUsageRecordId = UuidUtils.toHexString( new SequentialUuidGenerator().newUuid() );
      lArgs = new DataSetArgument();
      lArgs.add( "usage_record_id", lUsageRecordId );
      lArgs.add( "inv_no_db_id", iAircraft.getDbId() );
      lArgs.add( "inv_no_id", iAircraft.getId() );
      lArgs.add( "usage_dt", aArrivalDate );

      lRowsInserted = MxDataAccess.getInstance().executeInsert( "USG_USAGE_RECORD", lArgs );
      assertEquals( "Failed to create usage record.", lRowsInserted, 1 );

      // create usage record for flight with provided data type
      lArgs = new DataSetArgument();
      lArgs.add( "usage_data_id",
            UuidUtils.toHexString( new SequentialUuidGenerator().newUuid() ) );
      lArgs.add( "usage_record_id", lUsageRecordId );
      lArgs.add( "inv_no_db_id", iAircraft.getDbId() );
      lArgs.add( "inv_no_id", iAircraft.getId() );
      lArgs.add( "data_type_db_id", aDataType.getDbId() );
      lArgs.add( "data_type_id", aDataType.getId() );
      lArgs.add( "tsn_delta_qt", aUsage );

      lRowsInserted = MxDataAccess.getInstance().executeInsert( "USG_USAGE_DATA", lArgs );
      assertEquals( "Failed to create usage data.", lRowsInserted, 1 );
   }


   /**
    * Create a blackout period in the DB.
    *
    * @param aBlackoutStart
    * @param aBlackoutEnd
    */
   private void withBlackout( Date aBlackoutStart, Date aBlackoutEnd ) {
      BlackoutBuilder lBuilder = new BlackoutBuilder();
      lBuilder.withAircraft( iAircraft );
      lBuilder.startingAt( aBlackoutStart );
      lBuilder.endingAt( aBlackoutEnd );
      lBuilder.build();
   }


   /**
    * Create a complex forecast model for the provided usage type in the DB. The forecast model will
    * have forecast ranges that vary in length and rate, and have various start/end months and days.
    * It will also be missing the first forecast range (i.e. JAN 1).
    *
    * @param aDataType
    *           data type of forecast model
    *
    * @return total anticipated usage for one year
    */
   private double withComplexForecastModel( final DataTypeKey aDataType ) {
      // Purposely skip starting on JAN 1.

      // Purposely start the first range after FEB to avoid issues
      // with leap years (or tests will fail all that year).

      // MAR 17 - APR 1 - MAY 24 - JUL 1 - OCT 3 - DEC 25 - JAN 1

      FcModelKey lFcModelKey =
            Domain.createForecastModel( new DomainConfiguration<ForecastModel>() {

               @Override
               public void configure( ForecastModel aBuilder ) {
                  aBuilder.addRange( 3, 17, aDataType, 2 );
                  aBuilder.addRange( 4, 1, aDataType, 3 );
                  aBuilder.addRange( 5, 24, aDataType, 5 );
                  aBuilder.addRange( 7, 1, aDataType, 8 );
                  aBuilder.addRange( 10, 3, aDataType, 2 );
                  aBuilder.addRange( 12, 25, aDataType, 21 );
               }
            } );

      InvAcReg lInvAcReg = InvAcReg.findByPrimaryKey( iAircraft );
      lInvAcReg.setFcModel( lFcModelKey );

      // J--
      // calculate the usage for one year
      // (first range has an unknown number of days due to it potentially
      // having the leap day, but that doesn't matter because the rate is 0)
      double lYearlyUsage = 0 // JAN 1 - MAR 17
            + ( 15 * 2 ) // MAR 17 - APR 1
            + ( 53 * 3 ) // APR 1 - MAY 24
            + ( 38 * 5 ) // MAY 24 - JUL 1
            + ( 94 * 8 ) // JUL 1 - OCT 3
            + ( 83 * 2 ) // OCT 3 - DEC 25
            + ( 7 * 21 ) // DEC 25 - JAN 1
      ;
      // J+

      return lYearlyUsage;
   }


   /**
    * Create a flight usage adjustment in the DB.
    *
    * @param aDate
    *           date of the flight adjustment
    * @param aDataType
    *           usage data type
    * @param aUsage
    *           usage adjustment (may be +ve or -ve)
    */
   private void withFlightUsageAdjustment( Date aDate, DataTypeKey aDataType, Double aUsage ) {

      DataSetArgument lArgs;
      int lRowsInserted;

      // create usage record
      String lUsageRecordId = UuidUtils.toHexString( new SequentialUuidGenerator().newUuid() );
      lArgs = new DataSetArgument();
      lArgs.add( "usage_record_id", lUsageRecordId );
      lArgs.add( "inv_no_db_id", iAircraft.getDbId() );
      lArgs.add( "inv_no_id", iAircraft.getId() );
      lArgs.add( "usage_dt", aDate );

      lRowsInserted = MxDataAccess.getInstance().executeInsert( "USG_USAGE_RECORD", lArgs );
      assertEquals( "Failed to create usage record.", lRowsInserted, 1 );

      // create usage data record
      lArgs = new DataSetArgument();
      lArgs.add( "usage_data_id",
            UuidUtils.toHexString( new SequentialUuidGenerator().newUuid() ) );
      lArgs.add( "usage_record_id", lUsageRecordId );
      lArgs.add( "inv_no_db_id", iAircraft.getDbId() );
      lArgs.add( "inv_no_id", iAircraft.getId() );
      lArgs.add( "data_type_db_id", aDataType.getDbId() );
      lArgs.add( "data_type_id", aDataType.getId() );
      lArgs.add( "tsn_delta_qt", aUsage );

      lRowsInserted = MxDataAccess.getInstance().executeInsert( "USG_USAGE_DATA", lArgs );
      assertEquals( "Failed to create usage data.", lRowsInserted, 1 );
   }


   /**
    * Create a planned flight in the DB.
    *
    * @param aDepartureDate
    * @param aArrivalDate
    */
   private void withPlannedFlight( Date aDepartureDate, Date aArrivalDate ) {

      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "leg_id", UuidUtils.toHexString( new SequentialUuidGenerator().newUuid() ) );
      lArgs.add( "aircraft_db_id", iAircraft.getDbId() );
      lArgs.add( "aircraft_id", iAircraft.getId() );
      lArgs.add( "hist_bool", false );
      lArgs.add( "sched_departure_dt", aDepartureDate );
      lArgs.add( "sched_arrival_dt", aArrivalDate );

      int lRowsInserted = MxDataAccess.getInstance().executeInsert( "FL_LEG", lArgs );

      assertEquals( "Failed to create planned flight.", lRowsInserted, 1 );
   }


   /**
    * Create a simple forecast model for the provided usage type and with the provided rate. The
    * forecast model will have one range starting on JAN 1.
    *
    * @param aDataType
    * @param aRate
    */
   private void withSimpleForecastModel( final DataTypeKey aDataType, final Double aRate ) {

      // build a default forecast model (with a range or rate the default will be 1 per day)
      FcModelKey lForecastModelKey =
            Domain.createForecastModel( new DomainConfiguration<ForecastModel>() {

               @Override
               public void configure( ForecastModel aForecastModel ) {
                  aForecastModel.addRange( 1, 1, aDataType, aRate );
               }
            } );

      // update the auto generated inv_ac_req record for the aircraft with the forecast model
      InvAcReg lInvAcReg = InvAcReg.findByPrimaryKey( iAircraft );
      lInvAcReg.setFcModel( lForecastModelKey );
   }
}
