package com.mxi.mx.db.trigger.flight;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.GlobalParametersFake;
import com.mxi.mx.common.config.ParmTypeEnum;
import com.mxi.mx.common.services.work.WorkItemGenerator;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.MxAfterTrigger;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.core.adapter.flight.messages.flight.adapter.configurator.v1.FlightConfigurator;
import com.mxi.mx.core.adapter.flight.messages.flight.adapter.logic.bean.Aircraft;
import com.mxi.mx.core.adapter.flight.messages.flight.adapter.logic.bean.Flight;
import com.mxi.mx.core.adapter.flight.messages.flight.adapter.logic.misc.attributeupdater.v1.FlightAttributeUpdater;
import com.mxi.mx.core.adapter.flight.messages.flight.exception.FlightException;
import com.mxi.mx.core.flight.model.FlightLegId;
import com.mxi.mx.core.key.AircraftKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.services.flightfl.details.FlightFollowingDetailsService;


/**
 * This class tests the flight aircraft reassigned trigger.
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class AircraftReassignedFlightTriggerTest implements MxAfterTrigger<UUID> {

   private static Boolean triggerCalled;
   private static final String FLIGHT_LEG_ID = "f8ff4655-9d5f-11e3-965c-a4badbecd042";
   private static final int AIRCRAFT_INV_DB_ID = 5000000;
   private static final int AIRCRAFT_INV_ID = 8749;

   private WorkItemGenerator workItemGenerator;

   // V1
   private FlightAttributeUpdater flightAttributeUpdaterV1;

   // V2
   private com.mxi.mx.core.adapter.flight.messages.flight.adapter.logic.misc.attributeupdater.v2.FlightAttributeUpdater flightAttributeUpdaterV2;

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
      flightAttributeUpdaterV1 = new FlightAttributeUpdater();
      flightAttributeUpdaterV2 =
            new com.mxi.mx.core.adapter.flight.messages.flight.adapter.logic.misc.attributeupdater.v2.FlightAttributeUpdater();
      // Setup Mock for WorkItemGenerator (done so flight adapter does not throw an exception)
      workItemGenerator = mock( WorkItemGenerator.class );
      WorkItemGenerator.setInstance( workItemGenerator );
      triggerCalled = false;
   }


   @Test
   public void testAircraftReassignedTriggerV1() throws FlightException {
      Map<FlightConfigurator.Attribute, Object> stateAttributeObject =
            new HashMap<FlightConfigurator.Attribute, Object>();
      stateAttributeObject.put( FlightConfigurator.Attribute.AIRCRAFT_IDENTIFIER,
            createAircraft() );
      new FlightFollowingDetailsService();
      flightAttributeUpdaterV1.update( createFlight(), stateAttributeObject );
      assertTrue( "Trigger did not get invoked", triggerCalled );
   }


   @Test
   public void testAircraftReassignedTriggerV2() throws FlightException {
      Map<com.mxi.mx.core.adapter.flight.messages.flight.adapter.configurator.v2.FlightConfigurator.Attribute, Object> stateAttributeObject =
            new HashMap<com.mxi.mx.core.adapter.flight.messages.flight.adapter.configurator.v2.FlightConfigurator.Attribute, Object>();
      stateAttributeObject.put(
            com.mxi.mx.core.adapter.flight.messages.flight.adapter.configurator.v2.FlightConfigurator.Attribute.AIRCRAFT_IDENTIFIER,
            createAircraft() );
      flightAttributeUpdaterV2.update( createFlight(), stateAttributeObject );
      assertTrue( "Trigger did not get invoked", triggerCalled );
   }


   @After
   public void teardown() {
      WorkItemGenerator.setInstance( null );
   }


   private Flight createFlight() {
      Flight flight = new Flight( null, null, null, null, null, null );
      flight.setFlightLegId( new FlightLegId( UUID.fromString( FLIGHT_LEG_ID ) ) );
      return flight;
   }


   private Aircraft createAircraft() {
      Aircraft aircraft = new Aircraft();
      AircraftKey aircraftKey =
            new AircraftKey( new InventoryKey( AIRCRAFT_INV_DB_ID, AIRCRAFT_INV_ID ) );
      aircraft.setAircraftKey( aircraftKey );
      return aircraft;
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
