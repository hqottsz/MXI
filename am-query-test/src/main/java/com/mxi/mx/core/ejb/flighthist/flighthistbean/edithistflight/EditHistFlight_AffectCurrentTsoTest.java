package com.mxi.mx.core.ejb.flighthist.flighthistbean.edithistflight;

import static com.mxi.mx.core.key.DataTypeKey.CYCLES;
import static com.mxi.mx.core.key.DataTypeKey.HOURS;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Engine;
import com.mxi.am.domain.Location;
import com.mxi.am.domain.Requirement;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
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
import com.mxi.mx.core.key.RefDataSourceKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.UsageDefinitionKey;
import com.mxi.mx.core.key.UsageParmKey;
import com.mxi.mx.core.services.event.usage.CollectedUsageParm;
import com.mxi.mx.core.services.flighthist.FlightInformationTO;
import com.mxi.mx.core.services.flighthist.Measurement;
import com.mxi.mx.core.table.eqp.EqpDataSourceSpec;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.org.OrgHr;


/**
 * Tests {@linkplain FlightHistBean#editHistFlight} for its affect on current TSO usage of inventory
 * associated to the flight.
 *
 */

public class EditHistFlight_AffectCurrentTsoTest {

   private static final String HR_USERNAME = "HR_USERNAME";
   private static final String FLIGHT = "FLIGHT";
   private static final Measurement[] NO_MEASUREMENTS = new Measurement[] {};

   // current cycle and hour values
   private static final BigDecimal INITIAL_CYCLES = new BigDecimal( 50 );
   private static final BigDecimal INITIAL_HOURS = new BigDecimal( 100 );

   // some deltas
   private static final BigDecimal INITIAL_FLIGHT_DELTA = new BigDecimal( 10 );
   private static final BigDecimal DELTA_VALUE_THREE = new BigDecimal( 3 );

   // the bean under test
   private FlightHistBean iFlightHistBean;

   private HumanResourceKey iHrKey;
   private FlightInformationTO iFirstFlightInfoTO;
   private InventoryKey iAircraftInvKey;
   private InventoryKey iEngineInvKey;
   private InventoryKey iAircraftWithOneEngineInvKey;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * <pre>
    * Given an aircraft collecting usage
    * And a historical flight against the aircraft
    * And the aircraft has one overhaul task with a completion date AFTER the flight actual arrival date
    * When the usage parameter deltas of the historical flight are edited
    * Then the current TSO usages of the aircraft are unchanged
    * </pre>
    */
   @Test
   public void itDoesNotChangeAircraftTsoWhenFlightUpdatedButOvhlTaskCompletedAfterFlight()
         throws Exception {

      // Given an aircraft collecting usage and a historical flight against the aircraft.
      FlightLegId lFlightLegId = createInitialFlightForAircraft();

      // Given the aircraft has one overhaul task with a completion date AFTER the flight actual
      // arrival date.
      final Date lOverhaulDate = DateUtils.addDays( iFirstFlightInfoTO.getActualArrivalDate(), 1 );
      createCompletedOverhaulTask( iAircraftInvKey, lOverhaulDate );

      // Get the aircraft usage values after the flight.
      CurrentUsages lPostFlightUsage = new CurrentUsages( iAircraftInvKey );

      // When the usage parameter deltas of the historical flight are edited.
      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( iAircraftInvKey, CYCLES, DELTA_VALUE_THREE ),
                  generateFlightUsage( iAircraftInvKey, HOURS, DELTA_VALUE_THREE ) };
      iFlightHistBean.editHistFlight( lFlightLegId, iHrKey, iFirstFlightInfoTO, lEditUsageParms,
            NO_MEASUREMENTS );

      // Then the current TSO usages of the aircraft are unchanged.
      BigDecimal lExpectedCyclesTso = lPostFlightUsage.getTso( CYCLES );
      BigDecimal lExpectedHoursTso = lPostFlightUsage.getTso( HOURS );
      CurrentUsages lCurrentUsage = new CurrentUsages( iAircraftInvKey );

      Assert.assertEquals( "Unexpected aircraft TSO value for CYCLES.", lExpectedCyclesTso,
            lCurrentUsage.getTso( CYCLES ) );
      Assert.assertEquals( "Unexpected aircraft TSO value for HOURS.", lExpectedHoursTso,
            lCurrentUsage.getTso( HOURS ) );
   }


   /**
    * <pre>
    * Given an aircraft collecting usage
    * And a historical flight against the aircraft
    * And the aircraft has one overhaul task with a completion date EQUAL TO the flight actual arrival date
    * When the usage parameter deltas of the historical flight are edited
    * Then the current TSO usages of the aircraft are unchanged
    * </pre>
    */
   @Test
   public void itDoesNotChangeAircraftTsoWhenFlightUpdatedButOvhlTaskCompletedAtSameTimeAsFlight()
         throws Exception {

      // Given an aircraft collecting usage and a historical flight against the aircraft.
      FlightLegId lFlightLegId = createInitialFlightForAircraft();

      // Given the aircraft has one overhaul task with a completion date EQUAL TO the flight actual
      // arrival date.
      final Date lOverhaulDate = iFirstFlightInfoTO.getActualArrivalDate();
      createCompletedOverhaulTask( iAircraftInvKey, lOverhaulDate );

      // Get the aircraft usage values after the flight.
      CurrentUsages lPostFlightUsage = new CurrentUsages( iAircraftInvKey );

      // When the usage parameter deltas of the historical flight are edited.
      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( iAircraftInvKey, CYCLES, DELTA_VALUE_THREE ),
                  generateFlightUsage( iAircraftInvKey, HOURS, DELTA_VALUE_THREE ) };
      iFlightHistBean.editHistFlight( lFlightLegId, iHrKey, iFirstFlightInfoTO, lEditUsageParms,
            NO_MEASUREMENTS );

      // Then the current TSO usages of the aircraft are unchanged.
      BigDecimal lExpectedCyclesTso = lPostFlightUsage.getTso( CYCLES );
      BigDecimal lExpectedHoursTso = lPostFlightUsage.getTso( HOURS );
      CurrentUsages lCurrentUsage = new CurrentUsages( iAircraftInvKey );

      Assert.assertEquals( "Unexpected aircraft TSO value for CYCLES.", lExpectedCyclesTso,
            lCurrentUsage.getTso( CYCLES ) );
      Assert.assertEquals( "Unexpected aircraft TSO value for HOURS.", lExpectedHoursTso,
            lCurrentUsage.getTso( HOURS ) );
   }


   /**
    * <pre>
    * Given an aircraft collecting usage
    * And a historical flight against the aircraft
    * And the aircraft has one overhaul task with a completion date BEFORE the flight actual arrival date
    * When the usage parameter deltas of the historical flight are edited
    * Then the current TSO usages of the aircraft are updated by the delta difference
    * </pre>
    */
   @Test
   public void itUpdatesAircraftTsoWhenFlightUpdatedButOvhlTaskCompletedPriorToFlight()
         throws Exception {

      // Given an aircraft collecting usage and a historical flight against the aircraft.
      FlightLegId lFlightLegId = createInitialFlightForAircraft();

      // Given the aircraft has one overhaul task with a completion date BEFORE the flight actual
      // arrival date.
      final Date lOverhaulDate = DateUtils.addDays( iFirstFlightInfoTO.getActualArrivalDate(), -1 );
      createCompletedOverhaulTask( iAircraftInvKey, lOverhaulDate );

      // Get the aircraft usage values after the flight.
      CurrentUsages lPostFlightUsage = new CurrentUsages( iAircraftInvKey );

      // When the usage parameter deltas of the historical flight are edited.
      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( iAircraftInvKey, CYCLES, DELTA_VALUE_THREE ),
                  generateFlightUsage( iAircraftInvKey, HOURS, DELTA_VALUE_THREE ) };
      iFlightHistBean.editHistFlight( lFlightLegId, iHrKey, iFirstFlightInfoTO, lEditUsageParms,
            NO_MEASUREMENTS );

      // Then the current TSO usages of the aircraft are updated by the delta difference.
      BigDecimal lCyclesDeltaDiff = DELTA_VALUE_THREE.subtract( INITIAL_FLIGHT_DELTA );
      BigDecimal lHoursDeltaDiff = DELTA_VALUE_THREE.subtract( INITIAL_FLIGHT_DELTA );
      BigDecimal lExpectedCycles = lPostFlightUsage.getTso( CYCLES ).add( lCyclesDeltaDiff );
      BigDecimal lExpectedHours = lPostFlightUsage.getTso( HOURS ).add( lHoursDeltaDiff );
      CurrentUsages lCurrentUsage = new CurrentUsages( iAircraftInvKey );

      Assert.assertEquals( "Unexpected aircraft TSO value for CYCLES.", lExpectedCycles,
            lCurrentUsage.getTso( CYCLES ) );
      Assert.assertEquals( "Unexpected aircraft TSO value for HOURS.", lExpectedHours,
            lCurrentUsage.getTso( HOURS ) );
   }


   /**
    * <pre>
    * Given an aircraft collecting usage
    * And a sub-assembly installed on the aircraft collecting the same usage
    * And a historical flight against the aircraft
    * And the aircraft has one overhaul task with a completion date AFTER the flight actual arrival date
    * And the a sub-assembly has no overhaul tasks
    * When the usage parameter deltas of the historical flight are edited
    * Then the current TSO usages of the sub-assembly are updated by the delta difference
    * </pre>
    */
   @Test
   public void itChangesSubassyTsoWhenFlightUpdatedButOvhlTaskCompletedAfterFlight()
         throws Exception {

      // Given an aircraft collecting usage and a sub-assembly installed on the aircraft collecting
      // the same usage and a historical flight against the aircraft.
      FlightLegId lFlightLegId = createInitialFlightForAircraftWithOneEngine();

      // Given the aircraft has one overhaul task with a completion date AFTER the flight actual
      // arrival date and the a sub-assembly has no overhaul tasks.
      final Date lOverhaulDate = DateUtils.addDays( iFirstFlightInfoTO.getActualArrivalDate(), 1 );
      createCompletedOverhaulTask( iAircraftWithOneEngineInvKey, lOverhaulDate );

      // Get the sub-assembly usage values after the flight.
      CurrentUsages lPostFlightUsage = new CurrentUsages( iEngineInvKey );

      // When the usage parameter deltas of the historical flight are edited.
      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( iAircraftWithOneEngineInvKey, CYCLES, DELTA_VALUE_THREE ),
                  generateFlightUsage( iAircraftWithOneEngineInvKey, HOURS, DELTA_VALUE_THREE ),
                  generateFlightUsage( iEngineInvKey, CYCLES, DELTA_VALUE_THREE ),
                  generateFlightUsage( iEngineInvKey, HOURS, DELTA_VALUE_THREE ) };
      iFlightHistBean.editHistFlight( lFlightLegId, iHrKey, iFirstFlightInfoTO, lEditUsageParms,
            NO_MEASUREMENTS );

      // Then the current TSO usages of the sub-assembly are updated by the delta difference.
      BigDecimal lCyclesDeltaDiff = DELTA_VALUE_THREE.subtract( INITIAL_FLIGHT_DELTA );
      BigDecimal lHoursDeltaDiff = DELTA_VALUE_THREE.subtract( INITIAL_FLIGHT_DELTA );
      BigDecimal lExpectedCycles = lPostFlightUsage.getTso( CYCLES ).add( lCyclesDeltaDiff );
      BigDecimal lExpectedHours = lPostFlightUsage.getTso( HOURS ).add( lHoursDeltaDiff );
      CurrentUsages lCurrentUsage = new CurrentUsages( iEngineInvKey );

      Assert.assertEquals( "Unexpected sub-assembly TSO value for CYCLES.", lExpectedCycles,
            lCurrentUsage.getTso( CYCLES ) );
      Assert.assertEquals( "Unexpected sub-assembly TSO value for HOURS.", lExpectedHours,
            lCurrentUsage.getTso( HOURS ) );
   }


   /**
    * <pre>
    * Given an aircraft collecting usage
    * And a sub-assembly installed on the aircraft collecting the same usage
    * And a historical flight against the aircraft
    * And the aircraft has one overhaul task with a completion date BEFORE the flight actual arrival date
    * And the a sub-assembly has no overhaul tasks
    * When the usage parameter deltas of the historical flight are edited
    * Then the current TSO usages of the sub-assembly are updated by the delta difference
    * </pre>
    */
   @Test
   public void itChangesSubassyTsoWhenFlightUpdatedButOvhlTaskCompletedBeforeFlight()
         throws Exception {

      // Given an aircraft collecting usage and a sub-assembly installed on the aircraft collecting
      // the same usage and a historical flight against the aircraft.
      FlightLegId lFlightLegId = createInitialFlightForAircraftWithOneEngine();

      // Given the aircraft has one overhaul task with a completion date BEFORE the flight actual
      // arrival date and the a sub-assembly has no overhaul tasks.
      final Date lOverhaulDate = DateUtils.addDays( iFirstFlightInfoTO.getActualArrivalDate(), -1 );
      createCompletedOverhaulTask( iAircraftWithOneEngineInvKey, lOverhaulDate );

      // Get the sub-assembly usage values after the flight.
      CurrentUsages lPostFlightUsage = new CurrentUsages( iEngineInvKey );

      // When the usage parameter deltas of the historical flight are edited.
      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( iAircraftWithOneEngineInvKey, CYCLES, DELTA_VALUE_THREE ),
                  generateFlightUsage( iAircraftWithOneEngineInvKey, HOURS, DELTA_VALUE_THREE ),
                  generateFlightUsage( iEngineInvKey, CYCLES, DELTA_VALUE_THREE ),
                  generateFlightUsage( iEngineInvKey, HOURS, DELTA_VALUE_THREE ) };
      iFlightHistBean.editHistFlight( lFlightLegId, iHrKey, iFirstFlightInfoTO, lEditUsageParms,
            NO_MEASUREMENTS );

      // Then the current TSO usages of the sub-assembly are updated by the delta difference.
      BigDecimal lCyclesDeltaDiff = DELTA_VALUE_THREE.subtract( INITIAL_FLIGHT_DELTA );
      BigDecimal lHoursDeltaDiff = DELTA_VALUE_THREE.subtract( INITIAL_FLIGHT_DELTA );
      BigDecimal lExpectedCycles = lPostFlightUsage.getTso( CYCLES ).add( lCyclesDeltaDiff );
      BigDecimal lExpectedHours = lPostFlightUsage.getTso( HOURS ).add( lHoursDeltaDiff );
      CurrentUsages lCurrentUsage = new CurrentUsages( iEngineInvKey );

      Assert.assertEquals( "Unexpected sub-assembly TSO value for CYCLES.", lExpectedCycles,
            lCurrentUsage.getTso( CYCLES ) );
      Assert.assertEquals( "Unexpected sub-assembly TSO value for HOURS.", lExpectedHours,
            lCurrentUsage.getTso( HOURS ) );
   }


   /**
    * <pre>
    * Given an aircraft collecting usage
    * And a sub-assembly installed on the aircraft collecting the same usage
    * And a historical flight against the aircraft
    * And the sub-assembly has one overhaul task with a completion date AFTER the flight actual arrival date
    * When the usage parameter deltas of the historical flight are edited
    * Then the current TSO usages of the sub-assembly are unchanged
    * </pre>
    */
   @Test
   public void itDoesNotChangeSubassyTsoWhenFlightUpdatedButSubAssyOvhlTaskCompletedAfterFlight()
         throws Exception {

      // Given an aircraft collecting usage and a sub-assembly installed on the aircraft collecting
      // the same usage and a historical flight against the aircraft.
      FlightLegId lFlightLegId = createInitialFlightForAircraftWithOneEngine();

      // Given the sub-assembly has one overhaul task with a completion date AFTER the flight actual
      // arrival date.
      final Date lOverhaulDate = DateUtils.addDays( iFirstFlightInfoTO.getActualArrivalDate(), 1 );
      createCompletedOverhaulTask( iEngineInvKey, lOverhaulDate );

      // Get the sub-assembly usage values after the flight.
      CurrentUsages lPostFlightUsage = new CurrentUsages( iEngineInvKey );

      // When the usage parameter deltas of the historical flight are edited.
      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( iAircraftWithOneEngineInvKey, CYCLES, DELTA_VALUE_THREE ),
                  generateFlightUsage( iAircraftWithOneEngineInvKey, HOURS, DELTA_VALUE_THREE ),
                  generateFlightUsage( iEngineInvKey, CYCLES, DELTA_VALUE_THREE ),
                  generateFlightUsage( iEngineInvKey, HOURS, DELTA_VALUE_THREE ) };
      iFlightHistBean.editHistFlight( lFlightLegId, iHrKey, iFirstFlightInfoTO, lEditUsageParms,
            NO_MEASUREMENTS );

      // Then the current TSO usages of the sub-assembly are unchanged.
      BigDecimal lExpectedCyclesTso = lPostFlightUsage.getTso( CYCLES );
      BigDecimal lExpectedHoursTso = lPostFlightUsage.getTso( HOURS );
      CurrentUsages lCurrentUsage = new CurrentUsages( iEngineInvKey );

      Assert.assertEquals( "Unexpected sub-assembly TSO value for CYCLES.", lExpectedCyclesTso,
            lCurrentUsage.getTso( CYCLES ) );
      Assert.assertEquals( "Unexpected sub-assembly TSO value for HOURS.", lExpectedHoursTso,
            lCurrentUsage.getTso( HOURS ) );
   }


   /**
    * <pre>
    * Given an aircraft collecting usage
    * And a sub-assembly installed on the aircraft collecting the same usage
    * And a historical flight against the aircraft
    * And the sub-assembly has one overhaul task with a completion date EQUAL TO the flight actual arrival date
    * When the usage parameter deltas of the historical flight are edited
    * Then the current TSO usages of the sub-assembly are unchanged
    * </pre>
    */
   @Test
   public void
         itDoesNotChangeSubassyTsoWhenFlightUpdatedButSubAssyOvhlTaskCompletedAtSameTimeAsFlight()
               throws Exception {

      // Given an aircraft collecting usage and a sub-assembly installed on the aircraft collecting
      // the same usage and a historical flight against the aircraft.
      FlightLegId lFlightLegId = createInitialFlightForAircraftWithOneEngine();

      // Given the sub-assembly has one overhaul task with a completion date EQUAL TO the flight
      // actual arrival date.
      final Date lOverhaulDate = iFirstFlightInfoTO.getActualArrivalDate();
      createCompletedOverhaulTask( iEngineInvKey, lOverhaulDate );

      // Get the sub-assembly usage values after the flight.
      CurrentUsages lPostFlightUsage = new CurrentUsages( iEngineInvKey );

      // When the usage parameter deltas of the historical flight are edited.
      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( iAircraftWithOneEngineInvKey, CYCLES, DELTA_VALUE_THREE ),
                  generateFlightUsage( iAircraftWithOneEngineInvKey, HOURS, DELTA_VALUE_THREE ),
                  generateFlightUsage( iEngineInvKey, CYCLES, DELTA_VALUE_THREE ),
                  generateFlightUsage( iEngineInvKey, HOURS, DELTA_VALUE_THREE ) };
      iFlightHistBean.editHistFlight( lFlightLegId, iHrKey, iFirstFlightInfoTO, lEditUsageParms,
            NO_MEASUREMENTS );

      // Then the current TSO usages of the sub-assembly are unchanged.
      BigDecimal lExpectedCyclesTso = lPostFlightUsage.getTso( CYCLES );
      BigDecimal lExpectedHoursTso = lPostFlightUsage.getTso( HOURS );
      CurrentUsages lCurrentUsage = new CurrentUsages( iEngineInvKey );

      Assert.assertEquals( "Unexpected sub-assembly TSO value for CYCLES.", lExpectedCyclesTso,
            lCurrentUsage.getTso( CYCLES ) );
      Assert.assertEquals( "Unexpected sub-assembly TSO value for HOURS.", lExpectedHoursTso,
            lCurrentUsage.getTso( HOURS ) );
   }


   /**
    * <pre>
    * Given an aircraft collecting usage
    * And a sub-assembly installed on the aircraft collecting the same usage
    * And a historical flight against the aircraft
    * And the sub-assembly has one overhaul task with a completion date BEFORE the flight actual arrival date
    * When the usage parameter deltas of the historical flight are edited
    * Then the current TSO usages of the sub-assembly are updated by the delta difference
    * </pre>
    */
   @Test
   public void itChangesSubassyTsoWhenFlightUpdatedButSubAssyOvhlTaskCompletedBeforeFlight()
         throws Exception {

      // Given an aircraft collecting usage and a sub-assembly installed on the aircraft collecting
      // the same usage and a historical flight against the aircraft.
      FlightLegId lFlightLegId = createInitialFlightForAircraftWithOneEngine();

      // Given the sub-assembly has one overhaul task with a completion date one-day before the
      // flight
      // actual arrival date.
      final Date lArrivalDate = iFirstFlightInfoTO.getActualArrivalDate();
      final Date lOverhaulDate = DateUtils.addDays( lArrivalDate, -1 );
      createCompletedOverhaulTask( iEngineInvKey, lOverhaulDate );

      // Get the sub-assembly usage values after the flight.
      CurrentUsages lPostFlightUsage = new CurrentUsages( iEngineInvKey );

      // When the usage parameter deltas of the historical flight are edited.
      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( iAircraftWithOneEngineInvKey, CYCLES, DELTA_VALUE_THREE ),
                  generateFlightUsage( iAircraftWithOneEngineInvKey, HOURS, DELTA_VALUE_THREE ),
                  generateFlightUsage( iEngineInvKey, CYCLES, DELTA_VALUE_THREE ),
                  generateFlightUsage( iEngineInvKey, HOURS, DELTA_VALUE_THREE ) };
      iFlightHistBean.editHistFlight( lFlightLegId, iHrKey, iFirstFlightInfoTO, lEditUsageParms,
            NO_MEASUREMENTS );

      // Then the current TSO usages of the sub-assembly are updated by the delta difference.
      BigDecimal lCyclesDeltaDiff = DELTA_VALUE_THREE.subtract( INITIAL_FLIGHT_DELTA );
      BigDecimal lHoursDeltaDiff = DELTA_VALUE_THREE.subtract( INITIAL_FLIGHT_DELTA );
      BigDecimal lExpectedCycles = lPostFlightUsage.getTso( CYCLES ).add( lCyclesDeltaDiff );
      BigDecimal lExpectedHours = lPostFlightUsage.getTso( HOURS ).add( lHoursDeltaDiff );
      CurrentUsages lCurrentUsage = new CurrentUsages( iEngineInvKey );

      Assert.assertEquals( "Unexpected sub-assembly TSO value for CYCLES.", lExpectedCycles,
            lCurrentUsage.getTso( CYCLES ) );
      Assert.assertEquals( "Unexpected sub-assembly TSO value for HOURS.", lExpectedHours,
            lCurrentUsage.getTso( HOURS ) );
   }


   /**
    * <pre>
    * Given an Aircraft with a historical flight
    * and the aircraft has had no overhaul task since the flight actual arrival date
    * And the aircraft has a subassembly which has had one overhaul task with a completion date *before* the flight actual arrival date
    * When I edit the usage Deltas of the historical flight
    * The current TSO usage of the aircraft is updated
    * </pre>
    */
   @Test
   public void itChangesAircraftTsoWhenFlightUpdatedButSubAssyOvhlTaskCompletedBeforeFlight()
         throws Exception {

      // Given an aircraft collecting usage and a sub-assembly installed on the aircraft collecting
      // the same usage and a historical flight against the aircraft.
      FlightLegId lFlightLegId = createInitialFlightForAircraftWithOneEngine();

      // Given the sub-assembly has one overhaul task with a completion date one-day before the
      // flight actual arrival date.
      final Date lArrivalDate = iFirstFlightInfoTO.getActualArrivalDate();
      final Date lOverhaulDate = DateUtils.addDays( lArrivalDate, -1 );
      createCompletedOverhaulTask( iEngineInvKey, lOverhaulDate );

      // Get the sub-assembly usage values after the flight.
      CurrentUsages lPostFlightUsage = new CurrentUsages( iAircraftWithOneEngineInvKey );

      // When the usage parameter deltas of the historical flight are edited.
      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( iAircraftWithOneEngineInvKey, CYCLES, DELTA_VALUE_THREE ),
                  generateFlightUsage( iAircraftWithOneEngineInvKey, HOURS, DELTA_VALUE_THREE ),
                  generateFlightUsage( iEngineInvKey, CYCLES, DELTA_VALUE_THREE ),
                  generateFlightUsage( iEngineInvKey, HOURS, DELTA_VALUE_THREE ) };
      iFlightHistBean.editHistFlight( lFlightLegId, iHrKey, iFirstFlightInfoTO, lEditUsageParms,
            NO_MEASUREMENTS );

      // Then the current TSO usages of the aircraft are updated by the delta difference.
      BigDecimal lCyclesDeltaDiff = DELTA_VALUE_THREE.subtract( INITIAL_FLIGHT_DELTA );
      BigDecimal lHoursDeltaDiff = DELTA_VALUE_THREE.subtract( INITIAL_FLIGHT_DELTA );
      BigDecimal lExpectedCycles = lPostFlightUsage.getTso( CYCLES ).add( lCyclesDeltaDiff );
      BigDecimal lExpectedHours = lPostFlightUsage.getTso( HOURS ).add( lHoursDeltaDiff );

      CurrentUsages lCurrentUsage = new CurrentUsages( iAircraftWithOneEngineInvKey );

      Assert.assertEquals( "Unexpected aircraft TSO value for CYCLES.", lExpectedCycles,
            lCurrentUsage.getTso( CYCLES ) );
      Assert.assertEquals( "Unexpected aircraft TSO value for HOURS.", lExpectedHours,
            lCurrentUsage.getTso( HOURS ) );
   }


   @Before
   public void setup() {
      iHrKey = new HumanResourceDomainBuilder().withUsername( HR_USERNAME ).build();

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

      iFlightHistBean = new FlightHistBean();
      iFlightHistBean.ejbCreate();
      iFlightHistBean.setSessionContext( new SessionContextFake() );
   }


   @After
   public void teardown() {
      int lUserId = OrgHr.findByPrimaryKey( iHrKey ).getUserId();
      UserParameters.setInstance( lUserId, "LOGIC", null );
   }


   private FlightLegId createInitialFlightForAircraft() throws Exception {

      CollectedUsageParm[] lFlightUsageParms =
            { generateFlightUsage( iAircraftInvKey, CYCLES, INITIAL_FLIGHT_DELTA ),
                  generateFlightUsage( iAircraftInvKey, HOURS, INITIAL_FLIGHT_DELTA ) };

      return iFlightHistBean.createHistFlight( new AircraftKey( iAircraftInvKey ), iHrKey,
            iFirstFlightInfoTO, lFlightUsageParms, NO_MEASUREMENTS );
   }


   private FlightLegId createInitialFlightForAircraftWithOneEngine() throws Exception {

      CollectedUsageParm[] lFlightUsageParms =
            { generateFlightUsage( iAircraftWithOneEngineInvKey, CYCLES, INITIAL_FLIGHT_DELTA ),
                  generateFlightUsage( iAircraftWithOneEngineInvKey, HOURS, INITIAL_FLIGHT_DELTA ),
                  generateFlightUsage( iEngineInvKey, CYCLES, INITIAL_FLIGHT_DELTA ),
                  generateFlightUsage( iEngineInvKey, HOURS, INITIAL_FLIGHT_DELTA ) };

      return iFlightHistBean.createHistFlight( new AircraftKey( iAircraftWithOneEngineInvKey ),
            iHrKey, iFirstFlightInfoTO, lFlightUsageParms, NO_MEASUREMENTS );
   }


   private void createCompletedOverhaulTask( final InventoryKey aInventoryKey,
         final Date aOverhaulDate ) {
      Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aRequirement ) {
            aRequirement.setInventory( aInventoryKey );
            aRequirement.setTaskClass( RefTaskClassKey.OVHL );
            aRequirement.setStatus( RefEventStatusKey.COMPLETE );
            aRequirement.setActualEndDate( aOverhaulDate );

         }
      } );
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

}
