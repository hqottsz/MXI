package com.mxi.mx.db.trigger.flight;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.UUID;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.api.exception.KeyConversionException;
import com.mxi.am.api.util.LegacyKeyUtil;
import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.GlobalParametersFake;
import com.mxi.mx.common.config.ParmTypeEnum;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.MxAfterTrigger;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.core.adapter.flight.messages.flight.adapter.facade.v1.MxFlightFacade;
import com.mxi.mx.core.adapter.flight.messages.flight.adapter.logic.bean.Aircraft;
import com.mxi.mx.core.adapter.flight.messages.flight.adapter.logic.bean.Airport;
import com.mxi.mx.core.adapter.flight.messages.flight.adapter.logic.bean.Flight;
import com.mxi.mx.core.adapter.flight.messages.flight.adapter.logic.flightcreator.v1.PlannedFlightCreator;
import com.mxi.mx.core.adapter.flight.messages.flight.exception.FlightException;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.integration.exceptions.IntegrationException;


/**
 * This class tests the flight created trigger.
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class CreateFlightTriggerTest implements MxAfterTrigger<UUID> {

   private static Boolean triggerCalled;
   private static final String FL_LEG_NAME = "Test001";
   private static final String EXT_KEY = "0923MCOYUL20000923";
   private static final String DEP_DATE = "08-22-3018 17:30:00";
   private static final String ARR_DATE = "08-22-3018 22:00:00";
   private static final String DATE_FORMAT = "MM-dd-yyyy HH:mm:ss";
   private static final String DEP_LOC_ID = "9562ECBD440C44A3B637D9B6973DFD12";
   private static final String ARR_LOC_ID = "60671EA4065D45269C34C1D41FA8E8E4";

   private LegacyKeyUtil legacyKeyUtil = new LegacyKeyUtil();

   // V1
   private PlannedFlightCreator plannedFlightCreatorV1;
   private MxFlightFacade mxFlightFacadeV1;

   // V2
   private com.mxi.mx.core.adapter.flight.messages.flight.adapter.logic.flightcreator.v2.PlannedFlightCreator plannedFlightCreatorV2;
   private com.mxi.mx.core.adapter.flight.messages.flight.adapter.facade.v2.MxFlightFacade mxFlightFacadeV2;

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule fakeGuiceDaoRule = new InjectionOverrideRule();


   @Before
   public void setUp() {
      DataLoaders.load( databaseConnectionRule.getConnection(), getClass() );
      GlobalParametersFake triggerParams =
            new GlobalParametersFake( ParmTypeEnum.TRIGGER_CACHE.name() );
      GlobalParameters.setInstance( triggerParams );

      mxFlightFacadeV1 = new MxFlightFacade();
      plannedFlightCreatorV1 = new PlannedFlightCreator( mxFlightFacadeV1 );
      mxFlightFacadeV2 =
            new com.mxi.mx.core.adapter.flight.messages.flight.adapter.facade.v2.MxFlightFacade();
      plannedFlightCreatorV2 =
            new com.mxi.mx.core.adapter.flight.messages.flight.adapter.logic.flightcreator.v2.PlannedFlightCreator(
                  mxFlightFacadeV2 );
      triggerCalled = false;
   }


   @Test
   public void testCreateFlightTriggerV1()
         throws IntegrationException, KeyConversionException, ParseException {
      plannedFlightCreatorV1.create( createFlight() );
      assertTrue( "Trigger did not get invoked", triggerCalled );
   }


   @Test
   public void testCreateFlightTriggerV2()
         throws FlightException, KeyConversionException, ParseException {
      plannedFlightCreatorV2.create( createFlight() );
      assertTrue( "Trigger did not get invoked", triggerCalled );
   }


   private Flight createFlight() throws KeyConversionException, ParseException {
      DateFormat format = new SimpleDateFormat( DATE_FORMAT );
      HumanResourceKey humanResourceKey = new HumanResourceKey( 1, 1 );

      Airport arrivalAirport = new Airport();
      LocationKey arrivalKey = legacyKeyUtil.altIdToLegacyKey( ARR_LOC_ID, LocationKey.class );
      arrivalAirport.setAirportKey( arrivalKey );

      Airport departureAirport = new Airport();
      LocationKey departureKey = legacyKeyUtil.altIdToLegacyKey( DEP_LOC_ID, LocationKey.class );
      departureAirport.setAirportKey( departureKey );

      Flight flight = new Flight( EXT_KEY, FL_LEG_NAME, arrivalAirport, departureAirport,
            format.parse( DEP_DATE ), format.parse( ARR_DATE ) );
      flight.setAuthorizationHr( humanResourceKey );
      flight.setAircraft( new Aircraft() );

      return flight;
   }


   /**
    * This method gets called when the trigger is invoked
    */
   @Override
   public void after( UUID flightLegId ) throws TriggerException {
      assertFalse( "Trigger has been called more than once", triggerCalled );
      triggerCalled = true;
   }
}
