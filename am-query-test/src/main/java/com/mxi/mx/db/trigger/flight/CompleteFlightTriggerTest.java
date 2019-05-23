package com.mxi.mx.db.trigger.flight;

import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.api.exception.KeyConversionException;
import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.GlobalParametersFake;
import com.mxi.mx.common.config.ParmTypeEnum;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.license.exception.LicenseValidationException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.MxAfterTrigger;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.core.adapter.flight.messages.flight.adapter.configurator.v1.FlightConfigurator;
import com.mxi.mx.core.adapter.flight.messages.flight.adapter.logic.bean.Flight;
import com.mxi.mx.core.adapter.flight.messages.flight.adapter.logic.bean.FlightProcessingInstructions;
import com.mxi.mx.core.adapter.flight.messages.flight.adapter.logic.stateupdater.v1.StatesUpdater;
import com.mxi.mx.core.adapter.flight.messages.flight.exception.FlightException;
import com.mxi.mx.core.ejb.flightfl.FlightFollowingBean;
import com.mxi.mx.core.flight.model.FlightLegId;
import com.mxi.mx.core.flight.model.FlightLegStatus;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.services.flightfl.utils.FlightUtils;
import com.mxi.mx.core.services.flighthist.FlightInformationTO;


/**
 * This class tests the flight completed trigger.
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class CompleteFlightTriggerTest implements MxAfterTrigger<UUID> {

   private static Boolean triggerCalled;
   private static final String FLIGHT_LEG_ID = "f8ff4655-9d5f-11e3-965c-a4badbecd042";
   private static final String FLIGHT_NAME = "testFlight";
   private static final String EXTERNAL_KEY = "EXT123";
   private static final String MAX_DAYS = "999";
   private static final int HR_DB_ID = 4650;
   private static final int HR_ID = 100;
   private static final int AIRPORT_DB_ID = 4650;
   private static final int ARRIVAL_AIRPORT_ID = 100;
   private static final int DEPARTURE_AIRPORT_ID = 200;

   // V1
   private StatesUpdater statesUpdaterV1;

   // V2
   private com.mxi.mx.core.adapter.flight.messages.flight.adapter.logic.stateupdater.v2.StatesUpdater statesUpdaterV2;

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule fakeGuiceDaoRule = new InjectionOverrideRule();


   @Before
   public void setUp() {
      DataLoaders.load( databaseConnectionRule.getConnection(), getClass() );
      GlobalParametersFake logicParams = new GlobalParametersFake( ParmTypeEnum.LOGIC.name() );
      logicParams.setString( FlightUtils.MAX_FLIGHT_DAYS_IN_THE_FUTURE, MAX_DAYS );
      GlobalParameters.setInstance( logicParams );

      GlobalParametersFake triggerParams =
            new GlobalParametersFake( ParmTypeEnum.TRIGGER_CACHE.name() );
      GlobalParameters.setInstance( triggerParams );

      GlobalParametersFake refMandatoryParams =
            new GlobalParametersFake( ParmTypeEnum.LOGIC.name() );
      refMandatoryParams.setBoolean( "FLIGHT_LOGBOOK_REF_MANDATORY", false );
      GlobalParameters.setInstance( refMandatoryParams );

      UserParametersFake user0Parms = new UserParametersFake( 0, ParmTypeEnum.LOGIC.name() );
      user0Parms.setString( FlightUtils.MAX_FLIGHT_DAYS_IN_THE_FUTURE, MAX_DAYS );
      UserParameters.setInstance( 0, ParmTypeEnum.LOGIC.name(), user0Parms );

      UserParametersFake user1Parms = new UserParametersFake( 1, ParmTypeEnum.LOGIC.name() );
      user1Parms.setString( FlightUtils.MAX_FLIGHT_DAYS_IN_THE_FUTURE, MAX_DAYS );
      UserParameters.setInstance( 1, ParmTypeEnum.LOGIC.name(), user1Parms );

      statesUpdaterV1 = new StatesUpdater();
      statesUpdaterV2 =
            new com.mxi.mx.core.adapter.flight.messages.flight.adapter.logic.stateupdater.v2.StatesUpdater();

      triggerCalled = false;
   }


   @Test
   public void testCompleteFlightTriggerV1()
         throws FlightException, KeyConversionException, ParseException {
      Map<FlightConfigurator.State, Date> stateDateMap =
            new HashMap<FlightConfigurator.State, Date>();
      stateDateMap.put( FlightConfigurator.State.IN, new Date() );
      statesUpdaterV1.updateStates( createFlight(), stateDateMap );
      assertTrue( "Trigger did not get invoked", triggerCalled );
   }


   @Test
   public void testCompleteFlightTriggerThroughFlightFollowingBean()
         throws LicenseValidationException, MxException, TriggerException {
      HumanResourceKey humanResourceKey = new HumanResourceKey( HR_DB_ID, HR_ID );
      FlightFollowingBean flightFollowingBean = new FlightFollowingBean();
      flightFollowingBean.ejbCreate();
      flightFollowingBean.complete( buildFlightInformationTO(),
            new FlightLegId( UUID.fromString( FLIGHT_LEG_ID ) ), humanResourceKey, null, null, null,
            null );
      assertTrue( "Trigger did not get invoked", triggerCalled );
   }


   @Test
   public void testCompleteFlightTriggerV2()
         throws FlightException, KeyConversionException, ParseException {
      Map<com.mxi.mx.core.adapter.flight.messages.flight.adapter.configurator.v2.FlightConfigurator.State, Date> stateDateMap =
            new HashMap<com.mxi.mx.core.adapter.flight.messages.flight.adapter.configurator.v2.FlightConfigurator.State, Date>();
      stateDateMap.put(
            com.mxi.mx.core.adapter.flight.messages.flight.adapter.configurator.v2.FlightConfigurator.State.IN,
            new Date() );
      statesUpdaterV2.updateStates( createFlight(), stateDateMap );
      assertTrue( "Trigger did not get invoked", triggerCalled );
   }


   private Flight createFlight() {
      HumanResourceKey humanResourceKey = new HumanResourceKey( HR_DB_ID, HR_ID );
      Flight flight = new Flight( EXTERNAL_KEY, FLIGHT_NAME, null, null, null, null );
      flight.setFlightLegId( new FlightLegId( UUID.fromString( FLIGHT_LEG_ID ) ) );
      flight.setAuthorizationHr( humanResourceKey );
      FlightProcessingInstructions flightProcessingInstructions =
            new FlightProcessingInstructions();
      flightProcessingInstructions.setStatus( FlightLegStatus.MXPLAN );
      flight.iFlightProcessingInstructions = flightProcessingInstructions;
      return flight;
   }


   private FlightInformationTO buildFlightInformationTO() {
      LocationKey arrivalAirport = new LocationKey( AIRPORT_DB_ID, ARRIVAL_AIRPORT_ID );
      LocationKey departureAirport = new LocationKey( AIRPORT_DB_ID, DEPARTURE_AIRPORT_ID );

      Date schedArrivalDate = new Date();
      Date schedDepartureDate = new Date();

      return new FlightInformationTO( FLIGHT_NAME, null, null, null, null, departureAirport,
            arrivalAirport, null, null, schedDepartureDate, schedArrivalDate, schedDepartureDate,
            schedArrivalDate, null, null, false );
   }


   /**
    * This method gets called when the trigger is invoked
    */
   @Override
   public void after( UUID flightLegId ) throws TriggerException {
      // commented out until EXT-3818 is merged
      // assertFalse( "Trigger has been called more than once", triggerCalled );
      triggerCalled = true;
   }

}
