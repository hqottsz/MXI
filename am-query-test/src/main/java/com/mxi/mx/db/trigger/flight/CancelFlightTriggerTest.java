package com.mxi.mx.db.trigger.flight;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
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
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.MxAfterTrigger;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.core.adapter.flight.messages.flight.adapter.logic.bean.Flight;
import com.mxi.mx.core.adapter.flight.messages.flight.adapter.logic.bean.FlightProcessingInstructions;
import com.mxi.mx.core.adapter.flight.messages.flight.adapter.logic.stateupdater.v1.FlightCanceller;
import com.mxi.mx.core.adapter.flight.messages.flight.exception.FlightException;
import com.mxi.mx.core.flight.model.FlightLegId;
import com.mxi.mx.core.flight.model.FlightLegStatus;
import com.mxi.mx.core.key.HumanResourceKey;


/**
 * This class tests the flight cancelled trigger.
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class CancelFlightTriggerTest implements MxAfterTrigger<UUID> {

   private static Boolean triggerCalled;
   private static final String FLIGHT_LEG_ID = "f8ff4655-9d5f-11e3-965c-a4badbecd042";

   // V1
   private FlightCanceller flightCancellerV1;

   // V2
   private com.mxi.mx.core.adapter.flight.messages.flight.adapter.logic.stateupdater.v2.FlightCanceller flightCancellerV2;

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
      flightCancellerV1 = new FlightCanceller();
      flightCancellerV2 =
            new com.mxi.mx.core.adapter.flight.messages.flight.adapter.logic.stateupdater.v2.FlightCanceller();
      triggerCalled = false;
   }


   @Test
   public void testCancelPlanFlightTriggerV1()
         throws FlightException, KeyConversionException, ParseException {
      flightCancellerV1.cancelOrMarkInError( createFlight( FlightLegStatus.MXPLAN ) );
      assertTrue( "Trigger did not get invoked", triggerCalled );
   }


   @Test
   public void testCancelReturnFlightTriggerV1()
         throws FlightException, KeyConversionException, ParseException {
      flightCancellerV1.cancelOrMarkInError( createFlight( FlightLegStatus.MXRETURN ) );
      assertTrue( "Trigger did not get invoked", triggerCalled );
   }


   @Test
   public void testCancelPlanFlightTriggerV2()
         throws FlightException, KeyConversionException, ParseException {
      flightCancellerV2.cancelOrMarkInError( createFlight( FlightLegStatus.MXPLAN ) );
      assertTrue( "Trigger did not get invoked", triggerCalled );
   }


   @Test
   public void testCancelReturnFlightTriggerV2()
         throws FlightException, KeyConversionException, ParseException {
      flightCancellerV2.cancelOrMarkInError( createFlight( FlightLegStatus.MXRETURN ) );
      assertTrue( "Trigger did not get invoked", triggerCalled );
   }


   private Flight createFlight( FlightLegStatus status ) {
      HumanResourceKey humanResourceKey = new HumanResourceKey( 1, 1 );
      Flight flight = new Flight( null, null, null, null, null, null );
      flight.setFlightLegId( new FlightLegId( UUID.fromString( FLIGHT_LEG_ID ) ) );
      flight.setAuthorizationHr( humanResourceKey );
      FlightProcessingInstructions flightProcessingInstructions =
            new FlightProcessingInstructions();
      flightProcessingInstructions.setStatus( status );
      flight.iFlightProcessingInstructions = flightProcessingInstructions;
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
