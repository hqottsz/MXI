package com.mxi.mx.core.ejb.flighthist.flighthistbean.edithistflight;

import static com.mxi.mx.core.key.DataTypeKey.CYCLES;
import static com.mxi.mx.core.key.DataTypeKey.HOURS;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Engine;
import com.mxi.am.domain.Location;
import com.mxi.am.domain.Part;
import com.mxi.am.domain.System;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.servlet.SessionContextFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.ejb.flighthist.CurrentUsages;
import com.mxi.mx.core.ejb.flighthist.FlightHistBean;
import com.mxi.mx.core.flight.model.FlightLegId;
import com.mxi.mx.core.key.AircraftKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefDataSourceKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefStageReasonKey;
import com.mxi.mx.core.key.UsageDefinitionKey;
import com.mxi.mx.core.key.UsageParmKey;
import com.mxi.mx.core.services.event.usage.CollectedUsageParm;
import com.mxi.mx.core.services.flighthist.FlightInformationTO;
import com.mxi.mx.core.services.flighthist.Measurement;
import com.mxi.mx.core.services.inventory.InvUtils;
import com.mxi.mx.core.services.inventory.InventoryService;
import com.mxi.mx.core.services.inventory.InventoryServiceFactory;
import com.mxi.mx.core.services.inventory.config.AttachableInventoryService;
import com.mxi.mx.core.table.eqp.EqpDataSourceSpec;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.org.OrgHr;


/**
 * Tests {@linkplain FlightHistBean#editHistFlight} for its affect on current usage of inventory
 * associated to the flight.
 *
 */

public class EditHistFlight_AffectCurrentUsageTest {

   private static final String FLIGHT = "FLIGHT";
   private static final Measurement[] NO_MEASUREMENTS = new Measurement[] {};

   // current cycle and hour values
   private static final BigDecimal INITIAL_CYCLES = new BigDecimal( 50 );
   private static final BigDecimal INITIAL_HOURS = new BigDecimal( 100 );

   // some deltas
   private static final BigDecimal INITIAL_FLIGHT_DELTA = new BigDecimal( 10 );
   private static final BigDecimal DELTA_VALUE_TWO = new BigDecimal( 2 );
   private static final BigDecimal DELTA_VALUE_THREE = new BigDecimal( 3 );
   private static final BigDecimal DELTA_VALUE_NEGATIVE = new BigDecimal( -1 );

   // the bean under test
   private FlightHistBean iFlightHistBean;

   private HumanResourceKey iHrKey;
   private FlightInformationTO iLatestFlightInfoTO;
   private FlightInformationTO iFirstFlightInfoTO;
   private InventoryKey iAircraftInvKey;
   private InventoryKey iEngineInvKey;
   private InventoryKey iAircraftWithOneEngineInvKey;
   private InventoryKey iReplacementEngineInvKey;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    *
    * <pre>
    * Given an aircraft with 2 historical flights
    *  When the first historical flight's delta usage values are edited
    *  Then the current usages (HOURS, CYCLES, TSN, TSO, TSI) on the aircraft will be updated by the same delta.
    * </pre>
    *
    */
   @Test
   public void itAdjustsAircraftCurrentUsagesWhenUsagesOfFirstFlightAreEdited() throws Exception {

      // create 2 flights each with usage value
      // Get a reference to the Leg IDs of the flights
      FlightLegId[] lLegIds = createFlightsForAircraftInv();

      // Change the usage by editing latest of the 2 flights
      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( iAircraftInvKey, CYCLES, DELTA_VALUE_TWO ),
                  generateFlightUsage( iAircraftInvKey, HOURS, DELTA_VALUE_TWO ) };
      iFlightHistBean.editHistFlight( lLegIds[0], iHrKey, iFirstFlightInfoTO, lEditUsageParms,
            NO_MEASUREMENTS );

      // Assert current usage has been updated with the edited delta
      final BigDecimal lExpectedCyclesAfterEdit =
            INITIAL_CYCLES.add( INITIAL_FLIGHT_DELTA ).add( DELTA_VALUE_TWO );
      final BigDecimal lExpectedHoursAfterEdit =
            INITIAL_HOURS.add( INITIAL_FLIGHT_DELTA ).add( DELTA_VALUE_TWO );
      assertCurrentUsage( iAircraftInvKey, CYCLES, lExpectedCyclesAfterEdit );
      assertCurrentUsage( iAircraftInvKey, HOURS, lExpectedHoursAfterEdit );

   }


   /**
    *
    * <pre>
    * Given an aircraft with 2 historical flights
    *  When the latest historical flight's delta usage values are edited
    *  Then the current usages (HOURS, CYCLES, TSN, TSO, TSI) on the aircraft will be updated by the same delta.
    * </pre>
    *
    */
   @Test
   public void itAdjustsAircraftCurrentUsagesWhenUsagesOfLatestFlightAreEdited() throws Exception {

      // create 2 flights each with usage value
      // Get a reference to the Leg IDs of the flights
      FlightLegId[] lLegIds = createFlightsForAircraftInv();

      // Change the usage by editing latest of the 2 flights
      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( iAircraftInvKey, CYCLES, DELTA_VALUE_TWO ),
                  generateFlightUsage( iAircraftInvKey, HOURS, DELTA_VALUE_TWO ) };
      iFlightHistBean.editHistFlight( lLegIds[1], iHrKey, iFirstFlightInfoTO, lEditUsageParms,
            NO_MEASUREMENTS );

      // Assert current usage has been updated with the edited delta
      final BigDecimal lExpectedCyclesAfterEdit =
            INITIAL_CYCLES.add( INITIAL_FLIGHT_DELTA ).add( DELTA_VALUE_TWO );
      final BigDecimal lExpectedHoursAfterEdit =
            INITIAL_HOURS.add( INITIAL_FLIGHT_DELTA ).add( DELTA_VALUE_TWO );
      assertCurrentUsage( iAircraftInvKey, CYCLES, lExpectedCyclesAfterEdit );
      assertCurrentUsage( iAircraftInvKey, HOURS, lExpectedHoursAfterEdit );

   }


   /**
    *
    * <pre>
    * Given an aircraft with 2 historical flights
    *  When the first historical flight's delta cycles and hours usage values are edited but not changed
    *  Then the current Cycles & Hours usage (TSN, TSO, TSI) on the aircraft will be consistent with current usage values prior to editing
    * </pre>
    *
    */
   @Test
   public void itDoesNotAdjustAircraftCurrentUsageWhenFlightIsEditedButNotTheFlightUsages()
         throws Exception {

      // create 2 flights each with usage value
      // Get a reference to the Leg IDs of the flights
      FlightLegId[] lLegIds = createFlightsForAircraftInv();

      // Change the usage by editing first of the 2 flights
      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( iAircraftInvKey, CYCLES, INITIAL_FLIGHT_DELTA ),
                  generateFlightUsage( iAircraftInvKey, HOURS, INITIAL_FLIGHT_DELTA ) };
      iFlightHistBean.editHistFlight( lLegIds[0], iHrKey, iFirstFlightInfoTO, lEditUsageParms,
            NO_MEASUREMENTS );

      // Assert current usage has NOT been updated
      final BigDecimal lExpectedCyclesAfterEdit =
            INITIAL_CYCLES.add( INITIAL_FLIGHT_DELTA ).add( INITIAL_FLIGHT_DELTA );
      final BigDecimal lExpectedHoursAfterEdit =
            INITIAL_HOURS.add( INITIAL_FLIGHT_DELTA ).add( INITIAL_FLIGHT_DELTA );
      assertCurrentUsage( iAircraftInvKey, CYCLES, lExpectedCyclesAfterEdit );
      assertCurrentUsage( iAircraftInvKey, HOURS, lExpectedHoursAfterEdit );

   }


   /**
    *
    * <pre>
    * Given an aircraft with 2 historical flights
    *  When the first historical flight's delta cycles and hours usage value is edited with negative Cycles and Hours delta
    *  Then the current Cycles & Hours usage (TSN, TSO, TSI) on the aircraft will be updated by the same delta.
    * </pre>
    *
    */
   @Test
   public void itAdjustsAircraftCurrentUsageWhenFlightEditedWithNegativeDelta() throws Exception {

      // create 2 flights each with usage value
      // Get a reference to the Leg IDs of the flights
      FlightLegId[] lLegIds = createFlightsForAircraftInv();

      // Change the usage by editing first of the 2 flights
      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( iAircraftInvKey, CYCLES, DELTA_VALUE_NEGATIVE ),
                  generateFlightUsage( iAircraftInvKey, HOURS, DELTA_VALUE_NEGATIVE ) };
      iFlightHistBean.editHistFlight( lLegIds[0], iHrKey, iFirstFlightInfoTO, lEditUsageParms,
            NO_MEASUREMENTS );

      // Assert current usage has been updated with the edited delta
      final BigDecimal lExpectedCyclesAfterEdit =
            INITIAL_CYCLES.add( INITIAL_FLIGHT_DELTA ).add( DELTA_VALUE_NEGATIVE );
      final BigDecimal lExpectedHoursAfterEdit =
            INITIAL_HOURS.add( INITIAL_FLIGHT_DELTA ).add( DELTA_VALUE_NEGATIVE );
      assertCurrentUsage( iAircraftInvKey, CYCLES, lExpectedCyclesAfterEdit );
      assertCurrentUsage( iAircraftInvKey, HOURS, lExpectedHoursAfterEdit );
   }


   /**
    *
    * <pre>
    * Given an aircraft having one engine and 2 historical flights
    *  When the first historical flight's delta cycles and hours usage value of Aircraft and Engine assembly are edited separately
    *  Then the current Cycles & Hours usage (TSN, TSO, TSI) on the aircraft and Engine will be updated by the same deltas.
    * </pre>
    *
    */
   @Test
   public void itAdjustsInstalledEngineCurrentUsageWhenUsageOfPriorToLatestFlightIsEdited()
         throws Exception {

      // create 2 flights each with usage value
      // Get a reference to the Leg IDs of the flights
      FlightLegId[] lLegIds = createFlightsForAircraftWithOneEngineInv();

      // Change the usage by editing first of the 2 flights
      // We provide delta for engine as edit flight requires us to send usage params for both
      // engine and aircraft.
      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( iAircraftWithOneEngineInvKey, CYCLES, DELTA_VALUE_TWO ),
                  generateFlightUsage( iAircraftWithOneEngineInvKey, HOURS, DELTA_VALUE_TWO ),
                  generateFlightUsage( iEngineInvKey, CYCLES, DELTA_VALUE_THREE ),
                  generateFlightUsage( iEngineInvKey, HOURS, DELTA_VALUE_THREE ) };
      iFlightHistBean.editHistFlight( lLegIds[0], iHrKey, iFirstFlightInfoTO, lEditUsageParms,
            NO_MEASUREMENTS );

      // Assert current usage has been updated with the edited delta
      final BigDecimal lExpectedAircraftCyclesAfterEdit =
            INITIAL_CYCLES.add( INITIAL_FLIGHT_DELTA ).add( DELTA_VALUE_TWO );
      final BigDecimal lExpectedAircraftHoursAfterEdit =
            INITIAL_HOURS.add( INITIAL_FLIGHT_DELTA ).add( DELTA_VALUE_TWO );
      final BigDecimal lExpectedEngineCyclesAfterEdit =
            INITIAL_CYCLES.add( INITIAL_FLIGHT_DELTA ).add( DELTA_VALUE_THREE );
      final BigDecimal lExpectedEngineHoursAfterEdit =
            INITIAL_HOURS.add( INITIAL_FLIGHT_DELTA ).add( DELTA_VALUE_THREE );
      assertCurrentUsage( iAircraftWithOneEngineInvKey, CYCLES, lExpectedAircraftCyclesAfterEdit );
      assertCurrentUsage( iAircraftWithOneEngineInvKey, HOURS, lExpectedAircraftHoursAfterEdit );
      assertCurrentUsage( iEngineInvKey, CYCLES, lExpectedEngineCyclesAfterEdit );
      assertCurrentUsage( iEngineInvKey, HOURS, lExpectedEngineHoursAfterEdit );
   }


   /**
    *
    * <pre>
    * Given an aircraft having one engine and 2 historical flights
    *   And Engine has been removed after first flight
    *  When the first historical flight's delta cycles and hours usage value is edited
    *  Then the current Cycles & Hours usage (TSN, TSO, TSI) on the aircraft and Engine will be updated by the same delta.
    * </pre>
    *
    */
   @Test
   public void itAdjustsCurrentUsagesForEngineWhenFlightUsagesEditedBeforeTheEngineIsRemoved()
         throws Exception {

      // create 2 flights each with usage value
      // Get a reference to the Leg IDs of the flights
      FlightLegId[] lLegIds = createFlightsForAircraftWithOneEngineInv();

      // detach engine from the aircraft
      new InventoryService().recordConfigurationChange( iEngineInvKey, null,
            RefEventStatusKey.FGRMVL, RefStageReasonKey.ACRTV, null, "", "", null );

      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( iEngineInvKey );
      lInvInv.setNhInvNo( iEngineInvKey );
      lInvInv.setHInvNo( iEngineInvKey );
      lInvInv.setAssmblInvNo( iEngineInvKey );
      lInvInv.update();

      // Change the usage by editing first of the 2 flights
      // We provide delta for engine as edit flight requires us to send usage params for both
      // engine and aircraft.
      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( iAircraftWithOneEngineInvKey, CYCLES, DELTA_VALUE_TWO ),
                  generateFlightUsage( iAircraftWithOneEngineInvKey, HOURS, DELTA_VALUE_TWO ),
                  generateFlightUsage( iEngineInvKey, CYCLES, DELTA_VALUE_TWO ),
                  generateFlightUsage( iEngineInvKey, HOURS, DELTA_VALUE_TWO ) };
      iFlightHistBean.editHistFlight( lLegIds[0], iHrKey, iFirstFlightInfoTO, lEditUsageParms,
            NO_MEASUREMENTS );

      // Assert current usage has been updated with the edited delta
      final BigDecimal lExpectedCyclesAfterEdit =
            INITIAL_CYCLES.add( INITIAL_FLIGHT_DELTA ).add( DELTA_VALUE_TWO );
      final BigDecimal lExpectedHoursAfterEdit =
            INITIAL_HOURS.add( INITIAL_FLIGHT_DELTA ).add( DELTA_VALUE_TWO );
      assertCurrentUsage( iAircraftWithOneEngineInvKey, CYCLES, lExpectedCyclesAfterEdit );
      assertCurrentUsage( iAircraftWithOneEngineInvKey, HOURS, lExpectedHoursAfterEdit );
      assertCurrentUsage( iEngineInvKey, CYCLES, lExpectedCyclesAfterEdit );
      assertCurrentUsage( iEngineInvKey, HOURS, lExpectedHoursAfterEdit );
   }


   /**
    *
    * <pre>
    * Given an aircraft having one engine with 2 historical flights
    *  When a new engine is installed after first historical flight
    *  And first historical flight's delta cycles and hours usage value is edited
    *  Then the current Cycles & Hours usage (TSN, TSO, TSI) on new Engine(Replacement engine) will NOT be updated by the same delta.
    * </pre>
    *
    */
   @Test
   public void itDoesNotUpdateUsageForEngineWhenFlightUsageEditedBeforeTheEngineIsInstalled()
         throws Exception {

      // create 2 flights each with usage value
      // Get a reference to the Leg IDs of the flights
      FlightLegId[] lLegIds = createFlightsForAircraftWithOneEngineInv();

      // detach engine from the aircraft
      new InventoryService().recordConfigurationChange( iEngineInvKey, null,
            RefEventStatusKey.FGRMVL, RefStageReasonKey.ACRTV, null, "", "", null );

      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( iEngineInvKey );
      lInvInv.setNhInvNo( null );
      lInvInv.setHInvNo( iEngineInvKey );
      lInvInv.setAssmblInvNo( iEngineInvKey );
      lInvInv.update();

      // Attach a replacement engine
      InvInvTable lReplacementEngineInv = InvInvTable.findByPrimaryKey( iReplacementEngineInvKey );
      lReplacementEngineInv.setNhInvNo( iAircraftWithOneEngineInvKey );
      lReplacementEngineInv.setHInvNo( iAircraftWithOneEngineInvKey );
      lReplacementEngineInv.setAssmblInvNo( iAircraftWithOneEngineInvKey );
      lReplacementEngineInv.update();

      new InventoryService().recordConfigurationChange( iReplacementEngineInvKey, null,
            RefEventStatusKey.FGINST, RefStageReasonKey.ACRTV, null, "", "", null );

      // Change the usage by editing first of the 2 flights
      // We provide delta for engine as edit flight requires us to send usage params for both
      // engine and aircraft.
      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( iAircraftWithOneEngineInvKey, CYCLES, DELTA_VALUE_TWO ),
                  generateFlightUsage( iAircraftWithOneEngineInvKey, HOURS, DELTA_VALUE_TWO ),
                  generateFlightUsage( iEngineInvKey, CYCLES, DELTA_VALUE_TWO ),
                  generateFlightUsage( iEngineInvKey, HOURS, DELTA_VALUE_TWO ), };
      iFlightHistBean.editHistFlight( lLegIds[0], iHrKey, iFirstFlightInfoTO, lEditUsageParms,
            NO_MEASUREMENTS );

      // Assert that the flight engine's usage has been updated and the replacement engine's has not
      final BigDecimal lExpectedCyclesAfterEdit =
            INITIAL_CYCLES.add( INITIAL_FLIGHT_DELTA ).add( DELTA_VALUE_TWO );
      final BigDecimal lExpectedHoursAfterEdit =
            INITIAL_HOURS.add( INITIAL_FLIGHT_DELTA ).add( DELTA_VALUE_TWO );
      assertCurrentUsage( iAircraftWithOneEngineInvKey, CYCLES, lExpectedCyclesAfterEdit );
      assertCurrentUsage( iAircraftWithOneEngineInvKey, HOURS, lExpectedHoursAfterEdit );
      assertCurrentUsage( iEngineInvKey, CYCLES, lExpectedCyclesAfterEdit );
      assertCurrentUsage( iEngineInvKey, HOURS, lExpectedHoursAfterEdit );
      assertCurrentUsage( iReplacementEngineInvKey, CYCLES, INITIAL_CYCLES );
      assertCurrentUsage( iReplacementEngineInvKey, HOURS, INITIAL_HOURS );

   }


   /**
    *
    * <pre>
    * Given an Aircraft with at least one Historical Flight,
    * And the Aircraft has at least a System attached to it, with a sub-System attached to the System.
    * And the Aircraft has at least an Engine attached to it, with a sub-System attached to the Engine.
    * When the Historical Flight is edited,
    * And the Usage Parameters assigned to the System and sub-Systems are changed,
    * Then the current TSN of all changed Usage Parameters for that System Inventory should be updated.
    * </pre>
    *
    */
   @Test
   public void itAdjustsSysInventoryCurrentUsageWhenUsageOfFlightIsEdited() throws Exception {

      // Create an engine with a system.
      final InventoryKey lEngineInvKey = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {

            // Engine usage.
            aEngine.addUsage( HOURS, INITIAL_HOURS );
            aEngine.addUsage( CYCLES, INITIAL_CYCLES );

            // System of the engine.
            aEngine.addSystem( "Engine Subsystem" );
         }
      } );
      final InventoryKey lSubSysOfEngineInvKey =
            InvUtils.getSystemByName( lEngineInvKey, "Engine Subsystem" );

      // Create aircraft with one system having a sub-system and the engine installed.
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {

            // Aircraft usage.
            aAircraft.addUsage( CYCLES, INITIAL_CYCLES );
            aAircraft.addUsage( HOURS, INITIAL_HOURS );

            // Fuel system of the aircraft.
            aAircraft.addSystem( new DomainConfiguration<System>() {

               @Override
               public void configure( System aSystem ) {
                  aSystem.setName( "28 - FUEL" );

                  // Subsystem of the fuel system.
                  aSystem.addSubSystem( "Fuel subsystem" );
               }
            } );

            // Engine installed on the aircraft.
            aAircraft.addEngine( lEngineInvKey );
         }
      } );

      final InventoryKey lFuelSysInvKey = InvUtils.getSystemByName( lAircraft, "28 - FUEL" );
      final InventoryKey lSubSysOfFuelSysInvKey =
            InvUtils.getSystemByName( lFuelSysInvKey, "Fuel subsystem" );

      // create 2 flights each with usage value
      CollectedUsageParm[] lFlightUsageParms =
            { generateFlightUsage( lAircraft, CYCLES, INITIAL_FLIGHT_DELTA ),
                  generateFlightUsage( lAircraft, HOURS, INITIAL_FLIGHT_DELTA ),
                  generateFlightUsage( lEngineInvKey, CYCLES, INITIAL_FLIGHT_DELTA ),
                  generateFlightUsage( lEngineInvKey, HOURS, INITIAL_FLIGHT_DELTA ) };

      // Get a reference to the Leg IDs of the flights
      FlightLegId[] lLegIds = { createFlight( lAircraft, iFirstFlightInfoTO, lFlightUsageParms ),
            createFlight( lAircraft, iLatestFlightInfoTO, lFlightUsageParms ) };

      // Change the usage by editing first of the 2 flights
      // We provide delta for engine as edit flight requires us to send usage params for both
      // engine and aircraft.
      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( lAircraft, CYCLES, DELTA_VALUE_TWO ),
                  generateFlightUsage( lAircraft, HOURS, DELTA_VALUE_TWO ),
                  generateFlightUsage( lEngineInvKey, CYCLES, DELTA_VALUE_THREE ),
                  generateFlightUsage( lEngineInvKey, HOURS, DELTA_VALUE_THREE ) };
      iFlightHistBean.editHistFlight( lLegIds[0], iHrKey, iFirstFlightInfoTO, lEditUsageParms,
            NO_MEASUREMENTS );

      // Assert current usage has been updated with the edited delta
      final BigDecimal lExpectedAircraftCyclesAfterEdit =
            INITIAL_CYCLES.add( INITIAL_FLIGHT_DELTA ).add( DELTA_VALUE_TWO );
      final BigDecimal lExpectedAircraftHoursAfterEdit =
            INITIAL_HOURS.add( INITIAL_FLIGHT_DELTA ).add( DELTA_VALUE_TWO );
      final BigDecimal lExpectedEngineCyclesAfterEdit =
            INITIAL_CYCLES.add( INITIAL_FLIGHT_DELTA ).add( DELTA_VALUE_THREE );
      final BigDecimal lExpectedEngineHoursAfterEdit =
            INITIAL_HOURS.add( INITIAL_FLIGHT_DELTA ).add( DELTA_VALUE_THREE );

      assertCurrentUsage( lFuelSysInvKey, CYCLES, lExpectedAircraftCyclesAfterEdit );
      assertCurrentUsage( lFuelSysInvKey, HOURS, lExpectedAircraftHoursAfterEdit );

      assertCurrentUsage( lSubSysOfFuelSysInvKey, CYCLES, lExpectedAircraftCyclesAfterEdit );
      assertCurrentUsage( lSubSysOfFuelSysInvKey, HOURS, lExpectedAircraftHoursAfterEdit );

      assertCurrentUsage( lSubSysOfEngineInvKey, CYCLES, lExpectedEngineCyclesAfterEdit );
      assertCurrentUsage( lSubSysOfEngineInvKey, HOURS, lExpectedEngineHoursAfterEdit );

   }


   /**
    *
    * <pre>
    * Given an Aircraft with a Historical Flight,
    * And the Aircraft has a SYS-class Inventory attached to it,
    * And the Aircraft has been archived,
    * When the usage Deltas of the Historical Flight are edited,
    * Then the current TSN adjustment for the SYS-class Inventory should be updated.
    * </pre>
    *
    */
   @Test
   public void itAdjustsSysInventoryCurrentUsageOnArchivedAircraftWhenUsageOfFlightIsEdited()
         throws Exception {

      final InventoryKey lEngineInvKey = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.addUsage( HOURS, INITIAL_HOURS );
            aEngine.addUsage( CYCLES, INITIAL_CYCLES );

         }
      } );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.archive();
            aAircraft.addSystem( "28 - FUEL" );
            aAircraft.addEngine( lEngineInvKey );
            aAircraft.addUsage( CYCLES, INITIAL_CYCLES );
            aAircraft.addUsage( HOURS, INITIAL_HOURS );
         }
      } );

      final InventoryKey lFuelSysInvKey = InvUtils.getSystemByName( lAircraft, "28 - FUEL" );

      // create 2 flights each with usage value
      CollectedUsageParm[] lFlightUsageParms =
            { generateFlightUsage( lAircraft, CYCLES, INITIAL_FLIGHT_DELTA ),
                  generateFlightUsage( lAircraft, HOURS, INITIAL_FLIGHT_DELTA ),
                  generateFlightUsage( lEngineInvKey, CYCLES, INITIAL_FLIGHT_DELTA ),
                  generateFlightUsage( lEngineInvKey, HOURS, INITIAL_FLIGHT_DELTA ) };

      // Get a reference to the Leg IDs of the flights
      FlightLegId[] lLegIds = { createFlight( lAircraft, iFirstFlightInfoTO, lFlightUsageParms ),
            createFlight( lAircraft, iLatestFlightInfoTO, lFlightUsageParms ) };

      // Change the usage by editing first of the 2 flights
      // We provide delta for engine as edit flight requires us to send usage params for both
      // engine and aircraft.
      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( lAircraft, CYCLES, DELTA_VALUE_TWO ),
                  generateFlightUsage( lAircraft, HOURS, DELTA_VALUE_TWO ),
                  generateFlightUsage( lEngineInvKey, CYCLES, DELTA_VALUE_THREE ),
                  generateFlightUsage( lEngineInvKey, HOURS, DELTA_VALUE_THREE ) };
      iFlightHistBean.editHistFlight( lLegIds[0], iHrKey, iFirstFlightInfoTO, lEditUsageParms,
            NO_MEASUREMENTS );

      // Assert current usage has been updated with the edited delta
      final BigDecimal lExpectedAircraftCyclesAfterEdit =
            INITIAL_CYCLES.add( INITIAL_FLIGHT_DELTA ).add( DELTA_VALUE_TWO );
      final BigDecimal lExpectedAircraftHoursAfterEdit =
            INITIAL_HOURS.add( INITIAL_FLIGHT_DELTA ).add( DELTA_VALUE_TWO );

      assertCurrentUsage( lFuelSysInvKey, CYCLES, lExpectedAircraftCyclesAfterEdit );
      assertCurrentUsage( lFuelSysInvKey, HOURS, lExpectedAircraftHoursAfterEdit );

   }


   /**
    * <pre>
    * Given an Aircraft with at least one Historical Flight,
    * The engine has a system inventory.
    * And Engine has been removed after the flight
    * When the historical flight usage value is edited
    * Then the current usage on the system inventory will still be updated.
    * </pre>
    */
   @Test
   public void itAdjustsRemovedSysInventoryCurrentUsageWhenUsageOfFlightIsEdited()
         throws Exception {

      final PartNoKey lEnginePart = Domain.createPart( new DomainConfiguration<Part>() {

         @Override
         public void configure( Part aPart ) {
            aPart.setInventoryClass( RefInvClassKey.ASSY );
         }

      } );

      // Create an engine with a system.
      final InventoryKey lEngineInvKey = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.setPartNumber( lEnginePart );

            // Engine usage.
            aEngine.addUsage( HOURS, INITIAL_HOURS );
            aEngine.addUsage( CYCLES, INITIAL_CYCLES );

            // System of the engine.
            aEngine.addSystem( "Engine Subsystem" );
         }
      } );
      final InventoryKey lSubSysOfEngineInvKey =
            InvUtils.getSystemByName( lEngineInvKey, "Engine Subsystem" );

      // Create aircraft with the engine.
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {

            aAircraft.addUsage( CYCLES, INITIAL_CYCLES );
            aAircraft.addUsage( HOURS, INITIAL_HOURS );

            aAircraft.addEngine( lEngineInvKey );

         }
      } );

      CollectedUsageParm[] lFlightUsageParms =
            { generateFlightUsage( lAircraft, CYCLES, INITIAL_FLIGHT_DELTA ),
                  generateFlightUsage( lAircraft, HOURS, INITIAL_FLIGHT_DELTA ),
                  generateFlightUsage( lEngineInvKey, CYCLES, INITIAL_FLIGHT_DELTA ),
                  generateFlightUsage( lEngineInvKey, HOURS, INITIAL_FLIGHT_DELTA ) };

      FlightLegId[] lLegIds = { createFlight( lAircraft, iFirstFlightInfoTO, lFlightUsageParms ),
            createFlight( lAircraft, iLatestFlightInfoTO, lFlightUsageParms ) };

      // detach the engine
      AttachableInventoryService lService =
            InventoryServiceFactory.getInstance().getAttachableInventoryService( lEngineInvKey );

      lService.detachInventory( null, null, iHrKey, true, null, null );

      // Change the usage by editing flight
      // We provide delta for engine as edit flight requires us to send usage params for both
      // engine and aircraft.
      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( lAircraft, CYCLES, DELTA_VALUE_TWO ),
                  generateFlightUsage( lAircraft, HOURS, DELTA_VALUE_TWO ),
                  generateFlightUsage( lEngineInvKey, CYCLES, DELTA_VALUE_THREE ),
                  generateFlightUsage( lEngineInvKey, HOURS, DELTA_VALUE_THREE ) };
      iFlightHistBean.editHistFlight( lLegIds[0], iHrKey, iFirstFlightInfoTO, lEditUsageParms,
            NO_MEASUREMENTS );

      // Assert current usage has been updated with the edited delta
      final BigDecimal lExpectedEngineCyclesAfterEdit =
            INITIAL_CYCLES.add( INITIAL_FLIGHT_DELTA ).add( DELTA_VALUE_THREE );
      final BigDecimal lExpectedEngineHoursAfterEdit =
            INITIAL_HOURS.add( INITIAL_FLIGHT_DELTA ).add( DELTA_VALUE_THREE );

      assertCurrentUsage( lSubSysOfEngineInvKey, CYCLES, lExpectedEngineCyclesAfterEdit );
      assertCurrentUsage( lSubSysOfEngineInvKey, HOURS, lExpectedEngineHoursAfterEdit );

   }


   @Before
   public void setup() {
      iHrKey = Domain.createHumanResource();

      int lUserId = OrgHr.findByPrimaryKey( iHrKey ).getUserId();
      UserParameters.setInstance( lUserId, "LOGIC", new UserParametersFake( lUserId, "LOGIC" ) );

      // create an Engine assembly with current usage
      iEngineInvKey = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.addUsage( HOURS, INITIAL_HOURS );
            aEngine.addUsage( CYCLES, INITIAL_CYCLES );

         }
      } );

      // create an Engine assembly with current usage
      iReplacementEngineInvKey = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.addUsage( HOURS, INITIAL_HOURS );
            aEngine.addUsage( CYCLES, INITIAL_CYCLES );

         }
      } );

      // create aircraft with current usage
      iAircraftInvKey = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( CYCLES, INITIAL_CYCLES );
            aAircraft.addUsage( HOURS, INITIAL_HOURS );
         }
      } );

      // create aircraft with one engine & current usage
      iAircraftWithOneEngineInvKey = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( CYCLES, INITIAL_CYCLES );
            aAircraft.addUsage( HOURS, INITIAL_HOURS );
            aAircraft.addEngine( iEngineInvKey );
         }
      } );

      // create 2 flights - first and latest
      StringBuilder lFirstFlightName = new StringBuilder().append( FLIGHT )
            .append( new SimpleDateFormat( "SSSS" ).format( new Date() ) );
      Date lActualDepartureDate = DateUtils.addDays( new Date(), -2 );
      Date lActualArrivalDate = DateUtils.addHours( lActualDepartureDate, 2 );

      iFirstFlightInfoTO =
            generateFlightInfoTO( lFirstFlightName, lActualDepartureDate, lActualArrivalDate );

      StringBuilder lLatestFlightName = new StringBuilder().append( FLIGHT )
            .append( new SimpleDateFormat( "SSSS" ).format( new Date() ) );
      lActualDepartureDate = DateUtils.addHours( new Date(), -5 );
      lActualArrivalDate = DateUtils.addHours( lActualDepartureDate, 2 );

      iLatestFlightInfoTO =
            generateFlightInfoTO( lLatestFlightName, lActualDepartureDate, lActualArrivalDate );

      iFlightHistBean = new FlightHistBean();
      iFlightHistBean.ejbCreate();
      iFlightHistBean.setSessionContext( new SessionContextFake() );
   }


   @After
   public void teardown() {
      int lUserId = OrgHr.findByPrimaryKey( iHrKey ).getUserId();
      UserParameters.setInstance( lUserId, "LOGIC", null );
   }


   private FlightLegId createFlight( InventoryKey aAircraftInvKey,
         FlightInformationTO aFlightInformationTO, CollectedUsageParm[] aFlightUsageParms )
         throws Exception {

      return iFlightHistBean.createHistFlight( new AircraftKey( aAircraftInvKey ), iHrKey,
            aFlightInformationTO, aFlightUsageParms, NO_MEASUREMENTS );
   }


   private FlightLegId[] createFlightsForAircraftInv() throws Exception {

      CollectedUsageParm[] lFlightUsageParms =
            { generateFlightUsage( iAircraftInvKey, CYCLES, INITIAL_FLIGHT_DELTA ),
                  generateFlightUsage( iAircraftInvKey, HOURS, INITIAL_FLIGHT_DELTA ) };

      FlightLegId[] lLegIds =
            { createFlight( iAircraftInvKey, iFirstFlightInfoTO, lFlightUsageParms ),
                  createFlight( iAircraftInvKey, iLatestFlightInfoTO, lFlightUsageParms ) };

      // Expected usage would be the sum of the initial usage and the usage of the 2 flights
      // created above
      final BigDecimal lExpectedCyclesBeforeEdit =
            INITIAL_CYCLES.add( INITIAL_FLIGHT_DELTA ).add( INITIAL_FLIGHT_DELTA );
      final BigDecimal lExpectedHoursBeforeEdit =
            INITIAL_HOURS.add( INITIAL_FLIGHT_DELTA ).add( INITIAL_FLIGHT_DELTA );
      assertCurrentUsage( iAircraftInvKey, CYCLES, lExpectedCyclesBeforeEdit );
      assertCurrentUsage( iAircraftInvKey, HOURS, lExpectedHoursBeforeEdit );

      return lLegIds;
   }


   private FlightLegId[] createFlightsForAircraftWithOneEngineInv() throws Exception {

      CollectedUsageParm[] lFlightUsageParms =
            { generateFlightUsage( iAircraftWithOneEngineInvKey, CYCLES, INITIAL_FLIGHT_DELTA ),
                  generateFlightUsage( iAircraftWithOneEngineInvKey, HOURS, INITIAL_FLIGHT_DELTA ),
                  generateFlightUsage( iEngineInvKey, CYCLES, INITIAL_FLIGHT_DELTA ),
                  generateFlightUsage( iEngineInvKey, HOURS, INITIAL_FLIGHT_DELTA ) };

      FlightLegId[] lLegIds = {
            createFlight( iAircraftWithOneEngineInvKey, iFirstFlightInfoTO, lFlightUsageParms ),
            createFlight( iAircraftWithOneEngineInvKey, iLatestFlightInfoTO, lFlightUsageParms ) };

      // Expected cycles would be the sum of the current cycles usage and the usage of the 2 flights
      // created above
      final BigDecimal lExpectedCyclesBeforeEdit =
            INITIAL_CYCLES.add( INITIAL_FLIGHT_DELTA ).add( INITIAL_FLIGHT_DELTA );
      final BigDecimal lExpectedHoursBeforeEdit =
            INITIAL_HOURS.add( INITIAL_FLIGHT_DELTA ).add( INITIAL_FLIGHT_DELTA );
      assertCurrentUsage( iAircraftWithOneEngineInvKey, CYCLES, lExpectedCyclesBeforeEdit );
      assertCurrentUsage( iAircraftWithOneEngineInvKey, HOURS, lExpectedHoursBeforeEdit );
      assertCurrentUsage( iEngineInvKey, CYCLES, lExpectedCyclesBeforeEdit );
      assertCurrentUsage( iEngineInvKey, HOURS, lExpectedHoursBeforeEdit );

      return lLegIds;
   }


   private CollectedUsageParm generateFlightUsage( InventoryKey aInventoryKey,
         DataTypeKey lDataType, BigDecimal lDelta ) {

      // Create a usage collection to be returned.
      CollectedUsageParm lUsageParm = new CollectedUsageParm(
            new UsageParmKey( aInventoryKey, lDataType ), lDelta.doubleValue() );

      // Create flight data source specifications.
      EqpDataSourceSpec
            .create( new UsageDefinitionKey( InvInvTable.getAssemblyByInventoryKey( aInventoryKey ),
                  RefDataSourceKey.MXFL ), lDataType );

      return lUsageParm;
   }


   private FlightInformationTO generateFlightInfoTO( StringBuilder aFlightName,
         Date aActualDepartureDate, Date aActualArrivalDate ) {
      LocationKey lDepartureAirport = Domain.createLocation( new DomainConfiguration<Location>() {

         @Override
         public void configure( Location aLocation ) {
            aLocation.setType( RefLocTypeKey.AIRPORT );
         }
      } );
      LocationKey lArrivalAirport = Domain.createLocation( new DomainConfiguration<Location>() {

         @Override
         public void configure( Location aLocation ) {
            aLocation.setType( RefLocTypeKey.AIRPORT );
         }
      } );

      return new FlightInformationTO( aFlightName.toString(), null, null, null, null, null,
            lDepartureAirport, lArrivalAirport, null, null, null, null, aActualDepartureDate,
            aActualArrivalDate, null, null, false, false );

   }


   private void assertCurrentUsage( InventoryKey aInventory, DataTypeKey aDataType,
         BigDecimal aExpectedUsage ) {

      CurrentUsages lCurrentUsage = new CurrentUsages( aInventory );
      assertEquals( "Inventory=" + aInventory + " , data type=" + aDataType + " : unexpected TSN",
            aExpectedUsage, lCurrentUsage.getTsn( aDataType ) );
      assertEquals( "Inventory=" + aInventory + " , data type=" + aDataType + " : unexpected TSO",
            aExpectedUsage, lCurrentUsage.getTso( aDataType ) );
      assertEquals( "Inventory=" + aInventory + " , data type=" + aDataType + " : unexpected TSI",
            aExpectedUsage, lCurrentUsage.getTsi( aDataType ) );
   }

}
