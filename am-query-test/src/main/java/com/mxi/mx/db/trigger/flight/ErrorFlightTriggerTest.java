package com.mxi.mx.db.trigger.flight;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.UUID;

import org.junit.After;
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
import com.mxi.mx.common.services.work.WorkItemGenerator;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.MxAfterTrigger;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.core.adapter.flight.messages.flight.adapter.facade.v2.MxFlightFacade;
import com.mxi.mx.core.adapter.flight.messages.flight.adapter.logic.bean.Aircraft;
import com.mxi.mx.core.adapter.flight.messages.flight.adapter.logic.bean.Flight;
import com.mxi.mx.core.adapter.flight.messages.flight.adapter.logic.bean.FlightProcessingInstructions;
import com.mxi.mx.core.adapter.flight.messages.flight.adapter.logic.stateupdater.v1.FlightCanceller;
import com.mxi.mx.core.adapter.flight.messages.flight.exception.FlightException;
import com.mxi.mx.core.adapter.flight.tools.CacheException;
import com.mxi.mx.core.flight.model.FlightLegId;
import com.mxi.mx.core.flight.model.FlightLegStatus;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.services.flightfl.utils.FlightUtils;


/**
 * This class tests the flight error trigger.
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class ErrorFlightTriggerTest implements MxAfterTrigger<UUID> {

   private static Boolean triggerCalled;
   private static final String FLIGHT_LEG_ID = "f8ff4655-9d5f-11e3-965c-a4badbecd042";
   private static final String FLIGHT_NAME = "testFlight";
   private static final String MAX_DAYS = "999";
   private static final int HR_DB_ID = 4650;
   private static final int HR_ID = 100;

   private WorkItemGenerator workItemGenerator;

   // V1
   private FlightCanceller flightCancellerV1;

   // V2
   private MxFlightFacade mxFlightFacadeV2;

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule fakeGuiceDaoRule = new InjectionOverrideRule();


   @Before
   public void setUp() throws CacheException {
      DataLoaders.load( databaseConnectionRule.getConnection(), getClass() );
      GlobalParametersFake logicParms = new GlobalParametersFake( ParmTypeEnum.LOGIC.name() );
      logicParms.setString( FlightUtils.MAX_FLIGHT_DAYS_IN_THE_FUTURE, MAX_DAYS );
      GlobalParameters.setInstance( logicParms );

      GlobalParametersFake triggerParams =
            new GlobalParametersFake( ParmTypeEnum.TRIGGER_CACHE.name() );
      GlobalParameters.setInstance( triggerParams );

      UserParametersFake userParms = new UserParametersFake( 1, ParmTypeEnum.LOGIC.name() );
      userParms.setString( FlightUtils.MAX_FLIGHT_DAYS_IN_THE_FUTURE, MAX_DAYS );
      UserParameters.setInstance( 1, ParmTypeEnum.LOGIC.name(), userParms );
      flightCancellerV1 = new FlightCanceller();
      mxFlightFacadeV2 = new MxFlightFacade();
      triggerCalled = false;

      // Setup Mock for WorkItemGenerator (done so flight adapter does not throw an exception)
      workItemGenerator = mock( WorkItemGenerator.class );
      WorkItemGenerator.setInstance( workItemGenerator );
   }


   @After
   public void teardown() {
      WorkItemGenerator.setInstance( null );
   }


   @Test
   public void testStatusCompleteErrorFlightTriggerV1()
         throws FlightException, KeyConversionException {
      flightCancellerV1.cancelOrMarkInError( createFlight( FlightLegStatus.MXCMPLT ) );
      assertTrue( "Trigger did not get invoked", triggerCalled );
   }


   @Test
   public void testStatusEditErrorFlightTriggerV1() throws FlightException, KeyConversionException {
      flightCancellerV1.cancelOrMarkInError( createFlight( FlightLegStatus.MXEDIT ) );
      assertTrue( "Trigger did not get invoked", triggerCalled );
   }


   @Test
   public void testStatusOffErrorFlightTriggerV1() throws FlightException, KeyConversionException {
      flightCancellerV1.cancelOrMarkInError( createFlight( FlightLegStatus.MXOFF ) );
      assertTrue( "Trigger did not get invoked", triggerCalled );
   }


   @Test
   public void testStatusOutErrorFlightTriggerV1() throws FlightException, KeyConversionException {
      flightCancellerV1.cancelOrMarkInError( createFlight( FlightLegStatus.MXOUT ) );
      assertTrue( "Trigger did not get invoked", triggerCalled );
   }


   @Test
   public void testStatusDivertErrorFlightTriggerV1()
         throws FlightException, KeyConversionException {
      flightCancellerV1.cancelOrMarkInError( createFlight( FlightLegStatus.MXDIVERT ) );
      assertTrue( "Trigger did not get invoked", triggerCalled );
   }


   @Test
   public void testStatusOnErrorFlightTriggerV1() throws FlightException, KeyConversionException {
      flightCancellerV1.cancelOrMarkInError( createFlight( FlightLegStatus.MXON ) );
      assertTrue( "Trigger did not get invoked", triggerCalled );
   }


   @Test
   public void testErrorFlightTriggerV2() throws FlightException, KeyConversionException {
      mxFlightFacadeV2.error( new FlightLegId( UUID.fromString( FLIGHT_LEG_ID ) ),
            new HumanResourceKey( 4650, 100 ), null );
      assertTrue( "Trigger did not get invoked", triggerCalled );
   }


   private Flight createFlight( FlightLegStatus status ) throws KeyConversionException {
      HumanResourceKey humanResourceKey = new HumanResourceKey( HR_DB_ID, HR_ID );

      Flight flight = new Flight( null, FLIGHT_NAME, null, null, null, null );
      flight.setFlightLegId( new FlightLegId( UUID.fromString( FLIGHT_LEG_ID ) ) );
      flight.setAuthorizationHr( humanResourceKey );
      FlightProcessingInstructions flightProcessingInstructions =
            new FlightProcessingInstructions();
      flightProcessingInstructions.setStatus( status );
      flight.iFlightProcessingInstructions = flightProcessingInstructions;
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
