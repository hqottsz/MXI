
package com.mxi.mx.db.plsql.eventpkg;

import static com.mxi.mx.core.key.DataTypeKey.LANDING;
import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;

import java.util.Date;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.ForecastModel;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.utils.DataTypeUtils;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.key.AircraftKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.FcModelKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.table.inv.InvAcReg;
import com.mxi.mx.core.unittest.ProcedureStatementFactory;
import com.mxi.mx.persistence.uuid.SequentialUuidGenerator;
import com.mxi.mx.persistence.uuid.UuidUtils;


/**
 * Tests for the EVENT_PKG.PredictUsageBetweenDt plsql procedure using junit Theories. Testing with
 * a variety of dates and forecast rates.
 */
@RunWith( Theories.class )
public final class PredictUsageBetweenDtTheoriesTest {

   private static final Double CURRENT_LANDINGS = 100.0;
   private static final Double LANDINGS_PER_FLIGHT = 1.0;

   @DataPoints
   public static Double[] iForecastRates;
   @DataPoints
   public static Date[] iTargetDates;

   // Note: test with 2013 due to no leap year, to simplify calculating expected results
   private static final Date NOW = DateUtils.getDateTime( 2013, 02, 25, 11, 59, 59 );

   private static final Date PLANNED_FLIGHT_DEP_DATE;
   private static final Date PLANNED_FLIGHT_ARR_DATE;

   static {

      int lNumOfRates = 4;
      iForecastRates = new Double[lNumOfRates];
      for ( int i = 0; i < lNumOfRates; i++ ) {
         iForecastRates[i] = new Double( i + 1 );
      }

      int lNumTargetDates = 4;
      iTargetDates = new Date[lNumTargetDates];
      for ( int i = 0; i < lNumTargetDates; i++ ) {
         iTargetDates[i] = DateUtils.getEndOfDay( DateUtils.addDays( NOW, i ) );
      }

      // generate a planned flight that is within the set of future target dates
      PLANNED_FLIGHT_DEP_DATE =
            DateUtils.addHours( DateUtils.addDays( NOW, lNumTargetDates / 2 ), 6 );
      PLANNED_FLIGHT_ARR_DATE = DateUtils.addHours( PLANNED_FLIGHT_DEP_DATE, 2 );
   }

   private AircraftKey iAircraft;
   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   /**
    * Test the predicted usage with:
    *
    * <pre>
    *        various start dates
    *        various target dates that are greater than the start date
    *        various forecast model rates (daily usages)
    * </pre>
    *
    * @param aStartDate
    *           data points for the start date
    * @param aTargetDate
    *           data points for the target date
    * @param aForecastRate
    *           data points for the forecast rates
    *
    * @throws Exception
    */
   @Theory
   @Test
   public void testBetweenVariousDatesWithVariousRates( Date aStartDate, Date aTargetDate,
         Double aForecastRate ) throws Exception {

      // use a start date of now and only future target dates
      assumeTrue( aTargetDate.after( aStartDate ) );

      withSimpleForecastModel( LANDING, aForecastRate );

      Double lPredictedUsage =
            predictUsageBetweenDt( iAircraft, LANDING, aStartDate, aTargetDate, CURRENT_LANDINGS );

      // assert result
      Double lExpectedUsage = CURRENT_LANDINGS
            + getForecastUsageFromStartOfStartDay( aStartDate, aTargetDate, aForecastRate );

      assertEquals( "Date range = " + aStartDate + " - " + aTargetDate + " , Forecast Rate = "
            + aForecastRate, lExpectedUsage, lPredictedUsage );

      /*
       * Intentionally left here, as CSV results are useful for graphing.
       * 
       * System.out.println( DateUtils.differenceInDays( aTargetDate, NOW ) + "," + aForecastRate +
       * "," + lPredictedUsage + "," + lExpectedUsage );
       */
   }


   /**
    * Test the predicted usage with:
    *
    * <pre>
    *        various start dates
    *        various target dates that are greater than the start date
    *        various forecast model rates (daily usages)
    *        a planned flight in the middle of the various start dates
    * </pre>
    *
    * @param aStartDate
    *           data points for the start date
    * @param aTargetDate
    *           data points for the target date
    * @param aForecastRate
    *           data points for the forecast rates
    *
    * @throws Exception
    */
   @Theory
   @Test
   public void testBetweenVariousDatesWithVariousRatesAndWithPlannedFlight( Date aStartDate,
         Date aTargetDate, Double aForecastRate ) throws Exception {

      assumeTrue( aTargetDate.after( aStartDate ) );

      withSimpleForecastModel( LANDING, aForecastRate );

      withPlannedFlight( PLANNED_FLIGHT_DEP_DATE, PLANNED_FLIGHT_ARR_DATE );

      // test the proceedure
      Double lPredictedUsage =
            predictUsageBetweenDt( iAircraft, LANDING, aStartDate, aTargetDate, CURRENT_LANDINGS );

      // assert result
      Double lExpectedUsage = CURRENT_LANDINGS;

      if ( aStartDate.after( PLANNED_FLIGHT_ARR_DATE ) ) {

         // ignore the flight and add forecasted usage
         lExpectedUsage +=
               getForecastUsageFromStartOfStartDay( aStartDate, aTargetDate, aForecastRate );
      } else {

         if ( aTargetDate.after( PLANNED_FLIGHT_ARR_DATE ) ) {

            // add the flight usage
            lExpectedUsage += LANDINGS_PER_FLIGHT;

            if ( aTargetDate.after( DateUtils.ceilDay( PLANNED_FLIGHT_ARR_DATE ) ) ) {

               // add forecasted usage from end-of-day of the flight till target date
               lExpectedUsage += getForecastUsageFromEndOfStartDay( PLANNED_FLIGHT_ARR_DATE,
                     aTargetDate, aForecastRate );
            }
         }
      }

      assertEquals( "Date range = " + aStartDate + " - " + aTargetDate + " , Forecast Rate = "
            + aForecastRate, lExpectedUsage, lPredictedUsage );

      /*
       * Intentionally left here, as CSV results are useful for graphing.
       * 
       * System.out.println( DateUtils.differenceInDays( aTargetDate, NOW ) + "," + aForecastRate +
       * "," + lPredictedUsage + "," + lExpectedUsage );
       */
   }


   /**
    * {@inheritDoc}
    */
   @Before
   public void loadData() throws Exception {
      // create a test aircraft
      iAircraft =
            new AircraftKey( new InventoryBuilder().withClass( RefInvClassKey.ACFT ).build() );
   }


   /**
    * Calculate the expected usage from the end-of-day of the start date till the end date, using
    * the provided daily rate. Note: 23:59:59 is considered the end-of-day in Mx.
    *
    * @param aStart
    * @param aEnd
    * @param aDailyRate
    *
    * @return total usage
    */
   private double getForecastUsageFromEndOfStartDay( Date aStart, Date aEnd, double aDailyRate ) {
      return ( DateUtils.absoluteDifferenceInFractionalDays( DateUtils.ceilDay( aStart ), aEnd ) )
            * aDailyRate;
   }


   /**
    * Calculate the expected usage from the start-of-day of the start date till the end date, using
    * the provided daily rate. Note: since 23:59:59 is considered the end-of-day in Mx, then the
    * start-of-day must be 24 hours prior to that (thus, the end of the previous day). If we used
    * 00:00:00 as our start-of-day, then our day would be short by a second.
    *
    * @param aStart
    * @param aEnd
    * @param aDailyRate
    *
    * @return total usage
    */
   private double getForecastUsageFromStartOfStartDay( Date aStart, Date aEnd, double aDailyRate ) {
      return ( DateUtils.absoluteDifferenceInFractionalDays( DateUtils.getEndOfPrevDay( aStart ),
            aEnd ) ) * aDailyRate;
   }


   /**
    * Call the EVENT_PKG.PredictUsageBetweenDt plsql procedure.
    *
    * @param aAircraft
    * @param aDataType
    * @param aStartDate
    * @param aTargetDate
    * @param aStartUsage
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
    * Generate a planned flight.
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
    * Generate a simple forecast model with one forecast range starting with Jan 1st.
    *
    * @param aDataType
    *           data type for forcast range
    * @param aRate
    *           rate for forcast range
    */
   private void withSimpleForecastModel( final DataTypeKey aDataType, final Double aRate ) {

      // build a default forecast model (with a range or rate the default will be 1 per day)
      FcModelKey lFcModelKey =
            Domain.createForecastModel( new DomainConfiguration<ForecastModel>() {

               @Override
               public void configure( ForecastModel aForecastModel ) {
                  aForecastModel.addRange( 1, 1, aDataType, aRate );
               }

            } );

      // update the auto generated inv_ac_req record for the aircraft with the forecast model
      InvAcReg lInvAcReg = InvAcReg.findByPrimaryKey( iAircraft );
      lInvAcReg.setFcModel( lFcModelKey );
   }

}
