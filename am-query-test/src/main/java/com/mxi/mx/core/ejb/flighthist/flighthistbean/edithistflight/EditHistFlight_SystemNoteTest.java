package com.mxi.mx.core.ejb.flighthist.flighthistbean.edithistflight;

import static com.mxi.mx.core.key.DataTypeKey.CYCLES;
import static com.mxi.mx.core.key.DataTypeKey.HOURS;
import static com.mxi.mx.core.key.DataTypeKey.LANDING;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Engine;
import com.mxi.am.domain.Flight;
import com.mxi.am.domain.Location;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.i18n.i18n;
import com.mxi.mx.common.servlet.SessionContextFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.ejb.flighthist.FlightHistBean;
import com.mxi.mx.core.flight.dao.FlightLegNoteEntity;
import com.mxi.mx.core.flight.dao.FlightLegStatusLogEntity;
import com.mxi.mx.core.flight.dao.JdbcFlightLegNoteDao;
import com.mxi.mx.core.flight.dao.JdbcFlightLegStatusLogDao;
import com.mxi.mx.core.flight.model.FlightLegId;
import com.mxi.mx.core.flight.model.FlightLegNoteId;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefDataSourceKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.UsageDefinitionKey;
import com.mxi.mx.core.key.UsageParmKey;
import com.mxi.mx.core.services.event.usage.CollectedUsageParm;
import com.mxi.mx.core.services.flighthist.FlightInformationTO;
import com.mxi.mx.core.services.flighthist.Measurement;
import com.mxi.mx.core.table.eqp.EqpDataSourceSpec;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.mim.MimDataType;
import com.mxi.mx.core.table.org.OrgHr;


/**
 * Tests {@linkplain FlightHistBean#editHistFlight} for its affect on the flight's system note,
 * especially when the usage is updated.
 *
 * Note: it is difficult to test the system note as it is pre-formatted. These tests do their best
 * to parse that format to ensure the expected information is there.
 *
 */
public class EditHistFlight_SystemNoteTest {

   private static final BigDecimal INITIAL_CYCLES = new BigDecimal( 100 );
   private static final BigDecimal INITIAL_HOURS = new BigDecimal( 1000 );
   private static final BigDecimal INITIAL_LANDING = new BigDecimal( 200 );

   private static final BigDecimal FLIGHT_CYCLES = new BigDecimal( 1 );
   private static final BigDecimal FLIGHT_HOURS = new BigDecimal( 5 );
   private static final BigDecimal FLIGHT_LANDING = new BigDecimal( 2 );

   private static final BigDecimal FLIGHT_CYCLES_UPDATE_DIFF = new BigDecimal( 2 );
   private static final BigDecimal FLIGHT_HOURS_UPDATE_DIFF = new BigDecimal( -1 );
   private static final BigDecimal FLIGHT_LANDING_UPDATE_DIFF = new BigDecimal( 3 );
   private static final BigDecimal FLIGHT_LANDING_UPDATE_DIFF_1 = new BigDecimal( 4 );
   private static final BigDecimal FLIGHT_LANDING_UPDATE_DIFF_2 = new BigDecimal( 5 );

   private static final BigDecimal UPDATED_FLIGHT_CYCLES =
         FLIGHT_CYCLES.add( FLIGHT_CYCLES_UPDATE_DIFF );
   private static final BigDecimal UPDATED_FLIGHT_HOURS =
         FLIGHT_HOURS.add( FLIGHT_HOURS_UPDATE_DIFF );
   private static final BigDecimal UPDATED_FLIGHT_LANDING =
         FLIGHT_LANDING.add( FLIGHT_LANDING_UPDATE_DIFF );
   private static final BigDecimal UPDATED_FLIGHT_LANDING_1 =
         FLIGHT_LANDING.add( FLIGHT_LANDING_UPDATE_DIFF_1 );
   private static final BigDecimal UPDATED_FLIGHT_LANDING_2 =
         FLIGHT_LANDING.add( FLIGHT_LANDING_UPDATE_DIFF_2 );

   private static final String AIRCRAFT_DESCRIPTION = "AIRCRAFT_DESCRIPTION";
   private static final String ENGINE_1_DESCRIPTION = "ENGINE_1_DESCRIPTION";
   private static final String ENGINE_2_DESCRIPTION = "ENGINE_2_DESCRIPTION";

   private static final String USER_NOTE = "USER_NOTE";

   private static final Measurement[] NO_MEASUREMENTS = null;

   private static final String LINE_BREAK = "<br>";

   private FlightHistBean lFlightHistBean;

   private LocationKey iDepartureAirport;
   private LocationKey iArrivalAirport;
   private HumanResourceKey iHr;

   private String iExpectedSystemNoteTitle;
   private String iExpectedUsageDiffString_CYCLES;
   private String iExpectedUsageDiffString_HOURS;
   private String iExpectedUsageDiffString_LANDING;
   private String iExpectedUsageDiffString_LANDING_1;
   private String iExpectedUsageDiffString_LANDING_2;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    *
    * Verify that when a flight is edited and all the usages for the aircraft and its sub-assemblies
    * are updated that a system note is generated listing all the usage differences.
    *
    */
   @Test
   public void itCreatesSystemNoteWhenAllUsageUpdatedForAircraftAndSubassemblies()
         throws Exception {

      final InventoryKey lAircraft = createAircraft( AIRCRAFT_DESCRIPTION );
      final InventoryKey lEngine1 = createEngine( ENGINE_1_DESCRIPTION );
      final InventoryKey lEngine2 = createEngine( ENGINE_2_DESCRIPTION );

      // Create a flight for the aircraft on which both engines flew.
      FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.addUsage( lAircraft, CYCLES, FLIGHT_CYCLES );
            aFlight.addUsage( lAircraft, HOURS, FLIGHT_HOURS );
            aFlight.addUsage( lEngine1, CYCLES, FLIGHT_CYCLES );
            aFlight.addUsage( lEngine1, HOURS, FLIGHT_HOURS );
            aFlight.addUsage( lEngine1, LANDING, FLIGHT_LANDING );
            aFlight.addUsage( lEngine2, CYCLES, FLIGHT_CYCLES );
            aFlight.addUsage( lEngine2, HOURS, FLIGHT_HOURS );
            aFlight.addUsage( lEngine2, LANDING, FLIGHT_LANDING );
         }
      } );

      // When all usage parameters for the flight is updated.
      CollectedUsageParm[] lUpdatedUsage =
            { generateFlightUsage( lAircraft, CYCLES, UPDATED_FLIGHT_CYCLES ),
                  generateFlightUsage( lAircraft, HOURS, UPDATED_FLIGHT_HOURS ),
                  generateFlightUsage( lEngine1, CYCLES, UPDATED_FLIGHT_CYCLES ),
                  generateFlightUsage( lEngine1, HOURS, UPDATED_FLIGHT_HOURS ),
                  generateFlightUsage( lEngine1, LANDING, UPDATED_FLIGHT_LANDING ),
                  generateFlightUsage( lEngine2, CYCLES, UPDATED_FLIGHT_CYCLES ),
                  generateFlightUsage( lEngine2, HOURS, UPDATED_FLIGHT_HOURS ),
                  generateFlightUsage( lEngine2, LANDING, UPDATED_FLIGHT_LANDING ) };

      lFlightHistBean.editHistFlight( lFlight, iHr, generateFlightToIndicatingUpdatedUsage(),
            lUpdatedUsage, NO_MEASUREMENTS );

      // Then the flight history record's system note contains the usage differences for all the
      // usage
      // parameters of the aircraft and engines.
      List<String> lSystemNoteLines = getSystemNoteLines( lFlight );

      Assert.assertTrue( "Unexpected system note title.",
            lSystemNoteLines.get( 0 ).contains( iExpectedSystemNoteTitle ) );

      assertAllUsageDiffByInv( lSystemNoteLines, AIRCRAFT_DESCRIPTION,
            iExpectedUsageDiffString_CYCLES, iExpectedUsageDiffString_HOURS );
      assertAllUsageDiffByInv( lSystemNoteLines, ENGINE_1_DESCRIPTION,
            iExpectedUsageDiffString_LANDING );
      assertAllUsageDiffByInv( lSystemNoteLines, ENGINE_2_DESCRIPTION,
            iExpectedUsageDiffString_LANDING );
   }


   /**
    *
    * Verify that when a flight is edited and usages are edited which are tracked by the aircraft
    * and the sub-assemblies that a system note is generated but it only lists the usage differences
    * once against the aircraft.
    *
    * I.e. Verify that the usage differences are not listed under the sub-assemblies when they are
    * already listed under the aircraft/
    *
    */
   @Test
   public void itCreatesSystemNoteWhenSameUsageUpdatedForAircraftAndSubassemblies()
         throws Exception {

      final InventoryKey lAircraft = createAircraft( AIRCRAFT_DESCRIPTION );
      final InventoryKey lEngine1 = createEngine( ENGINE_1_DESCRIPTION );
      final InventoryKey lEngine2 = createEngine( ENGINE_2_DESCRIPTION );

      // Create a flight for the aircraft on which both engines flew.
      FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.addUsage( lAircraft, CYCLES, FLIGHT_CYCLES );
            aFlight.addUsage( lAircraft, HOURS, FLIGHT_HOURS );

            aFlight.addUsage( lEngine1, CYCLES, FLIGHT_CYCLES );
            aFlight.addUsage( lEngine1, HOURS, FLIGHT_HOURS );
            aFlight.addUsage( lEngine1, LANDING, FLIGHT_LANDING );

            aFlight.addUsage( lEngine2, CYCLES, FLIGHT_CYCLES );
            aFlight.addUsage( lEngine2, HOURS, FLIGHT_HOURS );
            aFlight.addUsage( lEngine2, LANDING, FLIGHT_LANDING );
         }
      } );

      // When same usage parameters for the aircraft and the engines are updated for the flight.
      // Note; editHistFlight requires all usage parameters be provided not just the updated ones.
      CollectedUsageParm[] lUpdatedUsage =
            { generateFlightUsage( lAircraft, CYCLES, UPDATED_FLIGHT_CYCLES ),
                  generateFlightUsage( lAircraft, HOURS, UPDATED_FLIGHT_HOURS ),
                  generateFlightUsage( lEngine1, CYCLES, UPDATED_FLIGHT_CYCLES ),
                  generateFlightUsage( lEngine1, HOURS, UPDATED_FLIGHT_HOURS ),
                  generateFlightUsage( lEngine2, CYCLES, UPDATED_FLIGHT_CYCLES ),
                  generateFlightUsage( lEngine2, HOURS, UPDATED_FLIGHT_HOURS ),
                  // Non updated usage parameters.
                  generateFlightUsage( lEngine1, LANDING, FLIGHT_LANDING ),
                  generateFlightUsage( lEngine2, LANDING, FLIGHT_LANDING ) };

      lFlightHistBean.editHistFlight( lFlight, iHr, generateFlightToIndicatingUpdatedUsage(),
            lUpdatedUsage, NO_MEASUREMENTS );

      // Then the flight history record's system note only contains the usage differences for
      // aircraft.
      // It does not contain the engine usage parameters because they are the same as the aircraft.
      List<String> lSystemNoteLines = getSystemNoteLines( lFlight );

      Assert.assertTrue( "Unexpected system note title.",
            lSystemNoteLines.get( 0 ).contains( iExpectedSystemNoteTitle ) );

      assertAllUsageDiffByInv( lSystemNoteLines, AIRCRAFT_DESCRIPTION,
            iExpectedUsageDiffString_CYCLES, iExpectedUsageDiffString_HOURS );
   }


   /**
    *
    * Verify that when a flight is edited and usages are only edited for the aircraft that a system
    * note is generated and only lists the usage differences against the aircraft. And the
    * sub-assemblies are not listed.
    *
    */
   @Test
   public void itCreatesSystemNoteWhenUsageUpdatedForOnlyAircraft() throws Exception {

      final InventoryKey lAircraft = createAircraft( AIRCRAFT_DESCRIPTION );
      final InventoryKey lEngine1 = createEngine( ENGINE_1_DESCRIPTION );
      final InventoryKey lEngine2 = createEngine( ENGINE_2_DESCRIPTION );

      // Create a flight for the aircraft on which both engines flew.
      FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.addUsage( lAircraft, CYCLES, FLIGHT_CYCLES );
            aFlight.addUsage( lAircraft, HOURS, FLIGHT_HOURS );

            aFlight.addUsage( lEngine1, CYCLES, FLIGHT_CYCLES );
            aFlight.addUsage( lEngine1, HOURS, FLIGHT_HOURS );
            aFlight.addUsage( lEngine1, LANDING, FLIGHT_LANDING );

            aFlight.addUsage( lEngine2, CYCLES, FLIGHT_CYCLES );
            aFlight.addUsage( lEngine2, HOURS, FLIGHT_HOURS );
            aFlight.addUsage( lEngine2, LANDING, FLIGHT_LANDING );
         }
      } );

      // When only usage parameters for the aircraft are updated for the flight.
      // Note; editHistFlight requires all usage parameters be provided not just the updated ones.
      CollectedUsageParm[] lUpdatedUsage =
            { generateFlightUsage( lAircraft, CYCLES, UPDATED_FLIGHT_CYCLES ),
                  generateFlightUsage( lAircraft, HOURS, UPDATED_FLIGHT_HOURS ),
                  // Non updated usage parameters.
                  generateFlightUsage( lEngine1, CYCLES, FLIGHT_CYCLES ),
                  generateFlightUsage( lEngine1, HOURS, FLIGHT_HOURS ),
                  generateFlightUsage( lEngine1, LANDING, FLIGHT_LANDING ),

                  generateFlightUsage( lEngine2, CYCLES, FLIGHT_CYCLES ),
                  generateFlightUsage( lEngine2, HOURS, FLIGHT_HOURS ),
                  generateFlightUsage( lEngine2, LANDING, FLIGHT_LANDING ) };

      lFlightHistBean.editHistFlight( lFlight, iHr, generateFlightToIndicatingUpdatedUsage(),
            lUpdatedUsage, NO_MEASUREMENTS );

      // Then the flight history record's system note only contains the usage differences for
      // aircraft.
      // It does not contain the engine usage parameters because they are the same as the aircraft.
      List<String> lSystemNoteLines = getSystemNoteLines( lFlight );

      Assert.assertTrue( "Unexpected system note title.",
            lSystemNoteLines.get( 0 ).contains( iExpectedSystemNoteTitle ) );

      assertAllUsageDiffByInv( lSystemNoteLines, AIRCRAFT_DESCRIPTION,
            iExpectedUsageDiffString_CYCLES, iExpectedUsageDiffString_HOURS );
   }


   /**
    *
    * Verify that when a flight is edited and usages are only edited for the sub-assembly that a
    * system note is generated and only lists the usage differences against the sub-assembly. And
    * the aircraft is not listed.
    *
    */
   @Test
   public void itCreatesSystemNoteWhenUsageUpdatedForOnlySubassembly() throws Exception {

      final InventoryKey lAircraft = createAircraft( AIRCRAFT_DESCRIPTION );
      final InventoryKey lEngine1 = createEngine( ENGINE_1_DESCRIPTION );
      final InventoryKey lEngine2 = createEngine( ENGINE_2_DESCRIPTION );

      // Create a flight for the aircraft on which both engines flew.
      FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.addUsage( lAircraft, CYCLES, FLIGHT_CYCLES );
            aFlight.addUsage( lAircraft, HOURS, FLIGHT_HOURS );

            aFlight.addUsage( lEngine1, CYCLES, FLIGHT_CYCLES );
            aFlight.addUsage( lEngine1, HOURS, FLIGHT_HOURS );
            aFlight.addUsage( lEngine1, LANDING, FLIGHT_LANDING );

            aFlight.addUsage( lEngine2, CYCLES, FLIGHT_CYCLES );
            aFlight.addUsage( lEngine2, HOURS, FLIGHT_HOURS );
            aFlight.addUsage( lEngine2, LANDING, FLIGHT_LANDING );
         }
      } );

      // When only usage parameters for the engines are updated for the flight.
      // Note; editHistFlight requires all usage parameters be provided not just the updated ones.
      CollectedUsageParm[] lUpdatedUsage =
            { generateFlightUsage( lEngine1, CYCLES, UPDATED_FLIGHT_CYCLES ),
                  generateFlightUsage( lEngine1, HOURS, UPDATED_FLIGHT_HOURS ),
                  generateFlightUsage( lEngine1, LANDING, UPDATED_FLIGHT_LANDING ),

                  generateFlightUsage( lEngine2, CYCLES, UPDATED_FLIGHT_CYCLES ),
                  generateFlightUsage( lEngine2, HOURS, UPDATED_FLIGHT_HOURS ),
                  generateFlightUsage( lEngine2, LANDING, UPDATED_FLIGHT_LANDING ),
                  // Non updated usage parameters.
                  generateFlightUsage( lAircraft, CYCLES, FLIGHT_CYCLES ),
                  generateFlightUsage( lAircraft, HOURS, FLIGHT_HOURS ) };

      lFlightHistBean.editHistFlight( lFlight, iHr, generateFlightToIndicatingUpdatedUsage(),
            lUpdatedUsage, NO_MEASUREMENTS );

      // Then the flight history record's system note only contains the usage differences for
      // the engines.
      List<String> lSystemNoteLines = getSystemNoteLines( lFlight );

      Assert.assertTrue( "Unexpected system note title.",
            lSystemNoteLines.get( 0 ).contains( iExpectedSystemNoteTitle ) );

      assertAllUsageDiffByInv( lSystemNoteLines, ENGINE_1_DESCRIPTION,
            iExpectedUsageDiffString_CYCLES, iExpectedUsageDiffString_HOURS,
            iExpectedUsageDiffString_LANDING );
      assertAllUsageDiffByInv( lSystemNoteLines, ENGINE_2_DESCRIPTION,
            iExpectedUsageDiffString_CYCLES, iExpectedUsageDiffString_HOURS,
            iExpectedUsageDiffString_LANDING );
   }


   /**
    *
    * Verify that when a flight is edited and usages are only edited for one of the sub-assembly
    * that a system note is generated and only lists the usage differences against that one
    * sub-assembly.
    *
    */
   @Test
   public void itCreatesSystemNoteWhenUsageUpdatedForOneSubassemblyAndNotAnotherSubassembly()
         throws Exception {

      final InventoryKey lAircraft = createAircraft( AIRCRAFT_DESCRIPTION );
      final InventoryKey lEngine1 = createEngine( ENGINE_1_DESCRIPTION );
      final InventoryKey lEngine2 = createEngine( ENGINE_2_DESCRIPTION );

      // Create a flight for the aircraft on which both engines flew.
      FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.addUsage( lAircraft, CYCLES, FLIGHT_CYCLES );
            aFlight.addUsage( lAircraft, HOURS, FLIGHT_HOURS );

            aFlight.addUsage( lEngine1, CYCLES, FLIGHT_CYCLES );
            aFlight.addUsage( lEngine1, HOURS, FLIGHT_HOURS );
            aFlight.addUsage( lEngine1, LANDING, FLIGHT_LANDING );

            aFlight.addUsage( lEngine2, CYCLES, FLIGHT_CYCLES );
            aFlight.addUsage( lEngine2, HOURS, FLIGHT_HOURS );
            aFlight.addUsage( lEngine2, LANDING, FLIGHT_LANDING );
         }
      } );

      // When only usage parameters for one of the engines are updated for the flight.
      // Note; editHistFlight requires all usage parameters be provided not just the updated ones.
      CollectedUsageParm[] lUpdatedUsage =
            { generateFlightUsage( lEngine1, CYCLES, UPDATED_FLIGHT_CYCLES ),
                  generateFlightUsage( lEngine1, HOURS, UPDATED_FLIGHT_HOURS ),
                  generateFlightUsage( lEngine1, LANDING, UPDATED_FLIGHT_LANDING ),

                  // Non updated usage parameters.
                  generateFlightUsage( lEngine2, CYCLES, FLIGHT_CYCLES ),
                  generateFlightUsage( lEngine2, HOURS, FLIGHT_HOURS ),
                  generateFlightUsage( lEngine2, LANDING, FLIGHT_LANDING ),
                  generateFlightUsage( lAircraft, CYCLES, FLIGHT_CYCLES ),
                  generateFlightUsage( lAircraft, HOURS, FLIGHT_HOURS ) };

      lFlightHistBean.editHistFlight( lFlight, iHr, generateFlightToIndicatingUpdatedUsage(),
            lUpdatedUsage, NO_MEASUREMENTS );

      // Then the flight history record's system note only contains the usage differences for
      // the one engine.
      List<String> lSystemNoteLines = getSystemNoteLines( lFlight );

      Assert.assertTrue( "Unexpected system note title.",
            lSystemNoteLines.get( 0 ).contains( iExpectedSystemNoteTitle ) );

      assertAllUsageDiffByInv( lSystemNoteLines, ENGINE_1_DESCRIPTION,
            iExpectedUsageDiffString_CYCLES, iExpectedUsageDiffString_HOURS,
            iExpectedUsageDiffString_LANDING );
   }


   /**
    *
    * Verify that when a flight is edited and the same usage parameter is updated with difference
    * values for different sub-assemblies that a system note is generated and lists both
    * sub-assemblies with their own usage differences.
    *
    */
   @Test
   public void itCreatesSystemNoteWhenSameUsageUpdatedForDifferentSubassemblies() throws Exception {

      final InventoryKey lAircraft = createAircraft( AIRCRAFT_DESCRIPTION );
      final InventoryKey lEngine1 = createEngine( ENGINE_1_DESCRIPTION );
      final InventoryKey lEngine2 = createEngine( ENGINE_2_DESCRIPTION );

      // Create a flight for the aircraft on which both engines flew.
      FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.addUsage( lAircraft, CYCLES, FLIGHT_CYCLES );
            aFlight.addUsage( lAircraft, HOURS, FLIGHT_HOURS );

            aFlight.addUsage( lEngine1, CYCLES, FLIGHT_CYCLES );
            aFlight.addUsage( lEngine1, HOURS, FLIGHT_HOURS );
            aFlight.addUsage( lEngine1, LANDING, FLIGHT_LANDING );

            aFlight.addUsage( lEngine2, CYCLES, FLIGHT_CYCLES );
            aFlight.addUsage( lEngine2, HOURS, FLIGHT_HOURS );
            aFlight.addUsage( lEngine2, LANDING, FLIGHT_LANDING );
         }
      } );

      // When the usage parameters for each of the engines are updated (difference values) for the
      // flight.
      // Note; editHistFlight requires all usage parameters be provided not just the updated ones.
      CollectedUsageParm[] lUpdatedUsage =
            { generateFlightUsage( lEngine1, LANDING, UPDATED_FLIGHT_LANDING_1 ),
                  generateFlightUsage( lEngine2, LANDING, UPDATED_FLIGHT_LANDING_2 ),

                  // Non updated usage parameters.
                  generateFlightUsage( lEngine1, CYCLES, FLIGHT_CYCLES ),
                  generateFlightUsage( lEngine1, HOURS, FLIGHT_HOURS ),
                  generateFlightUsage( lEngine2, CYCLES, FLIGHT_CYCLES ),
                  generateFlightUsage( lEngine2, HOURS, FLIGHT_HOURS ),
                  generateFlightUsage( lAircraft, CYCLES, FLIGHT_CYCLES ),
                  generateFlightUsage( lAircraft, HOURS, FLIGHT_HOURS ) };

      lFlightHistBean.editHistFlight( lFlight, iHr, generateFlightToIndicatingUpdatedUsage(),
            lUpdatedUsage, NO_MEASUREMENTS );

      // Then the flight history record's system note will contain the usage differences for
      // each of the engines.
      List<String> lSystemNoteLines = getSystemNoteLines( lFlight );

      Assert.assertTrue( "Unexpected system note title.",
            lSystemNoteLines.get( 0 ).contains( iExpectedSystemNoteTitle ) );

      assertAllUsageDiffByInv( lSystemNoteLines, ENGINE_1_DESCRIPTION,
            iExpectedUsageDiffString_LANDING_1 );
      assertAllUsageDiffByInv( lSystemNoteLines, ENGINE_2_DESCRIPTION,
            iExpectedUsageDiffString_LANDING_2 );
   }


   /**
    *
    * Verify that when a flight is edited but none of the usages are updated that no system note is
    * generated.
    *
    */
   @Test
   public void itDoesNotCreateSystemNoteWhenUsageNotUpdated() throws Exception {

      final InventoryKey lAircraft = createAircraft( AIRCRAFT_DESCRIPTION );
      final InventoryKey lEngine1 = createEngine( ENGINE_1_DESCRIPTION );
      final InventoryKey lEngine2 = createEngine( ENGINE_2_DESCRIPTION );

      // Create a flight for the aircraft on which both engines flew.
      FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.addUsage( lAircraft, CYCLES, FLIGHT_CYCLES );
            aFlight.addUsage( lAircraft, HOURS, FLIGHT_HOURS );

            aFlight.addUsage( lEngine1, CYCLES, FLIGHT_CYCLES );
            aFlight.addUsage( lEngine1, HOURS, FLIGHT_HOURS );
            aFlight.addUsage( lEngine1, LANDING, FLIGHT_LANDING );

            aFlight.addUsage( lEngine2, CYCLES, FLIGHT_CYCLES );
            aFlight.addUsage( lEngine2, HOURS, FLIGHT_HOURS );
            aFlight.addUsage( lEngine2, LANDING, FLIGHT_LANDING );
         }
      } );

      // When none of the usage parameters were updated.
      // Note; editHistFlight requires all usage parameters be provided not just the updated ones.
      CollectedUsageParm[] lUpdatedUsage = {
            // Non updated usage parameters.
            generateFlightUsage( lAircraft, CYCLES, FLIGHT_CYCLES ),
            generateFlightUsage( lAircraft, HOURS, FLIGHT_HOURS ),
            generateFlightUsage( lEngine1, CYCLES, FLIGHT_CYCLES ),
            generateFlightUsage( lEngine1, HOURS, FLIGHT_HOURS ),
            generateFlightUsage( lEngine1, LANDING, FLIGHT_LANDING ),
            generateFlightUsage( lEngine2, CYCLES, FLIGHT_CYCLES ),
            generateFlightUsage( lEngine2, HOURS, FLIGHT_HOURS ),
            generateFlightUsage( lEngine2, LANDING, FLIGHT_LANDING ) };

      // When the FlightInformationTO's flag also indicates that no usage were updated.
      FlightInformationTO lFlightInfoTO = new FlightInformationTO( "flight", null, null, null, null,
            null, iDepartureAirport, iArrivalAirport, null, null, null, null, new Date(),
            new Date(), null, null, false, false );
      lFlightInfoTO.setUsageUpdated( false );

      lFlightHistBean.editHistFlight( lFlight, iHr, lFlightInfoTO, lUpdatedUsage, NO_MEASUREMENTS );

      // Then the flight history record's will have no system note.
      List<FlightLegStatusLogEntity> lLogEntities =
            new JdbcFlightLegStatusLogDao().findByParent( lFlight );
      assertEquals( "Unexpected number of flight leg status log entities.", 0,
            lLogEntities.size() );
   }


   /**
    *
    * Verify that when a flight is edited and the usages are updated but the values are their
    * original values (no change to the values) that no system note is generated.
    *
    */
   @Test
   public void itDoesNotCreateSystemNoteWhenUsageUpdatedWithSameValues() throws Exception {

      final InventoryKey lAircraft = createAircraft( AIRCRAFT_DESCRIPTION );
      final InventoryKey lEngine1 = createEngine( ENGINE_1_DESCRIPTION );
      final InventoryKey lEngine2 = createEngine( ENGINE_2_DESCRIPTION );

      // Create a flight for the aircraft on which both engines flew.
      FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.addUsage( lAircraft, CYCLES, FLIGHT_CYCLES );
            aFlight.addUsage( lAircraft, HOURS, FLIGHT_HOURS );

            aFlight.addUsage( lEngine1, CYCLES, FLIGHT_CYCLES );
            aFlight.addUsage( lEngine1, HOURS, FLIGHT_HOURS );
            aFlight.addUsage( lEngine1, LANDING, FLIGHT_LANDING );

            aFlight.addUsage( lEngine2, CYCLES, FLIGHT_CYCLES );
            aFlight.addUsage( lEngine2, HOURS, FLIGHT_HOURS );
            aFlight.addUsage( lEngine2, LANDING, FLIGHT_LANDING );
         }
      } );

      // When none of the usage parameter values have changed but the FlightInformationTO's flag
      // indicates that usage was updated.
      // Note; editHistFlight requires all usage parameters be provided not just the updated ones.
      CollectedUsageParm[] lUpdatedUsage = {
            // Non updated usage parameters.
            generateFlightUsage( lAircraft, CYCLES, FLIGHT_CYCLES ),
            generateFlightUsage( lAircraft, HOURS, FLIGHT_HOURS ),
            generateFlightUsage( lEngine1, CYCLES, FLIGHT_CYCLES ),
            generateFlightUsage( lEngine1, HOURS, FLIGHT_HOURS ),
            generateFlightUsage( lEngine1, LANDING, FLIGHT_LANDING ),
            generateFlightUsage( lEngine2, CYCLES, FLIGHT_CYCLES ),
            generateFlightUsage( lEngine2, HOURS, FLIGHT_HOURS ),
            generateFlightUsage( lEngine2, LANDING, FLIGHT_LANDING ) };

      lFlightHistBean.editHistFlight( lFlight, iHr, generateFlightToIndicatingUpdatedUsage(),
            lUpdatedUsage, NO_MEASUREMENTS );

      // Then the flight history record's will have no system note.
      List<FlightLegStatusLogEntity> lLogEntities =
            new JdbcFlightLegStatusLogDao().findByParent( lFlight );
      assertEquals( "Unexpected number of flight leg status log entities.", 0,
            lLogEntities.size() );
   }


   /**
    *
    * Verify that when a flight is edited and a user note is provided but no usages are updated that
    * no system note is generated.
    *
    */
   @Test
   public void itDoesNotCreateSystemNoteWhenOnlyUserNoteUpdated() throws Exception {
      final InventoryKey lAircraft = createAircraft( AIRCRAFT_DESCRIPTION );
      final InventoryKey lEngine1 = createEngine( ENGINE_1_DESCRIPTION );
      final InventoryKey lEngine2 = createEngine( ENGINE_2_DESCRIPTION );

      // Create a flight for the aircraft on which both engines flew.
      FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.addUsage( lAircraft, CYCLES, FLIGHT_CYCLES );
            aFlight.addUsage( lAircraft, HOURS, FLIGHT_HOURS );

            aFlight.addUsage( lEngine1, CYCLES, FLIGHT_CYCLES );
            aFlight.addUsage( lEngine1, HOURS, FLIGHT_HOURS );
            aFlight.addUsage( lEngine1, LANDING, FLIGHT_LANDING );

            aFlight.addUsage( lEngine2, CYCLES, FLIGHT_CYCLES );
            aFlight.addUsage( lEngine2, HOURS, FLIGHT_HOURS );
            aFlight.addUsage( lEngine2, LANDING, FLIGHT_LANDING );
         }
      } );

      // When the FlightInformationTO contains a user note and indicates no usages has been updated.
      FlightInformationTO lFlightInfoTO = new FlightInformationTO( "flight", null, null, null, null,
            null, iDepartureAirport, iArrivalAirport, null, null, null, null, new Date(),
            new Date(), null, null, false, false );
      lFlightInfoTO.setUsageUpdated( false );
      lFlightInfoTO.setEntryNote( USER_NOTE );

      lFlightHistBean.editHistFlight( lFlight, iHr, lFlightInfoTO, null, NO_MEASUREMENTS );

      // Then the flight history record will have no system note.
      List<FlightLegStatusLogEntity> lLogEntities =
            new JdbcFlightLegStatusLogDao().findByParent( lFlight );
      assertEquals( "Unexpected number of flight leg status log entities.", 1,
            lLogEntities.size() );
      Assert.assertNull( "Unexpected system note.", lLogEntities.get( 0 ).getSystemNote() );
   }


   /**
    *
    * Verify that when a flight is edited, a user note is provided and the usages are updated that a
    * system note is generated containing the usage differences.
    *
    */
   @Test
   public void itCreatesSystemNoteWhenUsageEditedAndUserNoteUpdated() throws Exception {

      final InventoryKey lAircraft = createAircraft( AIRCRAFT_DESCRIPTION );
      final InventoryKey lEngine1 = createEngine( ENGINE_1_DESCRIPTION );
      final InventoryKey lEngine2 = createEngine( ENGINE_2_DESCRIPTION );

      // Create a flight for the aircraft on which both engines flew.
      FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.addUsage( lAircraft, CYCLES, FLIGHT_CYCLES );
            aFlight.addUsage( lAircraft, HOURS, FLIGHT_HOURS );
            aFlight.addUsage( lEngine1, CYCLES, FLIGHT_CYCLES );
            aFlight.addUsage( lEngine1, HOURS, FLIGHT_HOURS );
            aFlight.addUsage( lEngine1, LANDING, FLIGHT_LANDING );
            aFlight.addUsage( lEngine2, CYCLES, FLIGHT_CYCLES );
            aFlight.addUsage( lEngine2, HOURS, FLIGHT_HOURS );
            aFlight.addUsage( lEngine2, LANDING, FLIGHT_LANDING );
         }
      } );

      // When all usage parameters for the flight is updated.
      CollectedUsageParm[] lUpdatedUsage =
            { generateFlightUsage( lAircraft, CYCLES, UPDATED_FLIGHT_CYCLES ),
                  generateFlightUsage( lAircraft, HOURS, UPDATED_FLIGHT_HOURS ),
                  generateFlightUsage( lEngine1, CYCLES, UPDATED_FLIGHT_CYCLES ),
                  generateFlightUsage( lEngine1, HOURS, UPDATED_FLIGHT_HOURS ),
                  generateFlightUsage( lEngine1, LANDING, UPDATED_FLIGHT_LANDING ),
                  generateFlightUsage( lEngine2, CYCLES, UPDATED_FLIGHT_CYCLES ),
                  generateFlightUsage( lEngine2, HOURS, UPDATED_FLIGHT_HOURS ),
                  generateFlightUsage( lEngine2, LANDING, UPDATED_FLIGHT_LANDING ) };

      FlightInformationTO lFlightInfoTO = generateFlightToIndicatingUpdatedUsage();
      lFlightInfoTO.setEntryNote( USER_NOTE );

      lFlightHistBean.editHistFlight( lFlight, iHr, lFlightInfoTO, lUpdatedUsage, NO_MEASUREMENTS );

      // Then the flight history record's system note contains the usage differences for all the
      // usage parameters of the aircraft and engines.
      List<String> lSystemNoteLines = getSystemNoteLines( lFlight );

      Assert.assertTrue( "Unexpected system note title.",
            lSystemNoteLines.get( 0 ).contains( iExpectedSystemNoteTitle ) );

      assertAllUsageDiffByInv( lSystemNoteLines, AIRCRAFT_DESCRIPTION,
            iExpectedUsageDiffString_CYCLES, iExpectedUsageDiffString_HOURS );
      assertAllUsageDiffByInv( lSystemNoteLines, ENGINE_1_DESCRIPTION,
            iExpectedUsageDiffString_LANDING );
      assertAllUsageDiffByInv( lSystemNoteLines, ENGINE_2_DESCRIPTION,
            iExpectedUsageDiffString_LANDING );
   }


   /**
    *
    * Verify that when usages are updated during an edit of a flight that the system note accurately
    * reflects all the usage updates for all combinations of incrementing and decrementing INTEGER
    * values. Those updates may result in positive and negative differences.
    *
    * <pre>
    * Possible combinations:
    *   +1 - 1 =  0 (positive usage decremented)
    *   -1 - 1 = -2 (negative usage decremented)
    *   +1 + 1 =  2 (positive usage incremented)
    *   -1 + 1 =  0 (negative usage incremented)
    * </pre>
    *
    */
   @Test
   public void itCreatesSystemNoteWithIncrementingAndDecrementingIntegerValues() throws Exception {

      final InventoryKey lAircraft = createAircraft( AIRCRAFT_DESCRIPTION );
      final InventoryKey lEngine1 = createEngine( ENGINE_1_DESCRIPTION );
      final InventoryKey lEngine2 = createEngine( ENGINE_2_DESCRIPTION );

      // Create a flight for the aircraft on which both engines flew.
      FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.addUsage( lEngine1, CYCLES, new BigDecimal( 1 ) );
            aFlight.addUsage( lEngine1, HOURS, new BigDecimal( -1 ) );
            aFlight.addUsage( lEngine2, CYCLES, new BigDecimal( 1 ) );
            aFlight.addUsage( lEngine2, HOURS, new BigDecimal( -1 ) );
         }
      } );

      // When the usage parameters are updated with values that result in decrementing and
      // incrementing integer usage differences.
      CollectedUsageParm[] lUpdatedUsage =
            { generateFlightUsage( lEngine1, CYCLES, new BigDecimal( 0 ) ),
                  generateFlightUsage( lEngine1, HOURS, new BigDecimal( -2 ) ),
                  generateFlightUsage( lEngine2, CYCLES, new BigDecimal( 2 ) ),
                  generateFlightUsage( lEngine2, HOURS, new BigDecimal( 0 ) ) };

      FlightInformationTO lFlightInfoTO = generateFlightToIndicatingUpdatedUsage();
      lFlightInfoTO.setEntryNote( USER_NOTE );

      lFlightHistBean.editHistFlight( lFlight, iHr, lFlightInfoTO, lUpdatedUsage, NO_MEASUREMENTS );

      // Then the flight history record's system note contains the usage differences for all the
      // usage parameters.
      List<String> lSystemNoteLines = getSystemNoteLines( lFlight );

      assertAllUsageDiffByInv( lSystemNoteLines, ENGINE_1_DESCRIPTION,
            getExpectedUsageDiffString( CYCLES, new BigDecimal( -1 ) ),
            getExpectedUsageDiffString( HOURS, new BigDecimal( -1 ) ) );
      assertAllUsageDiffByInv( lSystemNoteLines, ENGINE_2_DESCRIPTION,
            getExpectedUsageDiffString( CYCLES, new BigDecimal( 1 ) ),
            getExpectedUsageDiffString( HOURS, new BigDecimal( 1 ) ) );
   }


   /**
    *
    * Verify that when usages are updated during an edit of a flight that the system note accurately
    * reflects all the usage updates for all combinations of incrementing and decrementing REAL
    * NUMBER values. Those updates may result in positive and negative differences.
    *
    * <pre>
    * Possible combinations:
    *   +1.5 - 1.25 =  0.25 (positive usage decremented)
    *   -1.5 - 1.25 = -2.75 (negative usage decremented)
    *   +1.5 + 1.25 =  2.75 (positive usage incremented)
    *   -1.5 + 1.25 = -0.25 (negative usage incremented)
    *   -1.5 + 1.5 =   0    (negative usage incremented to zero)
    * </pre>
    *
    */
   @Test
   public void itCreatesSystemNoteWithIncrementingAndDecrementingRealNumberValues()
         throws Exception {
      final InventoryKey lAircraft = createAircraft( AIRCRAFT_DESCRIPTION );
      final InventoryKey lEngine1 = createEngine( ENGINE_1_DESCRIPTION );
      final InventoryKey lEngine2 = createEngine( ENGINE_2_DESCRIPTION );

      // Create a flight for the aircraft on which both engines flew.
      FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.addUsage( lEngine1, CYCLES, new BigDecimal( 1.5 ) );
            aFlight.addUsage( lEngine1, HOURS, new BigDecimal( -1.5 ) );
            aFlight.addUsage( lEngine2, CYCLES, new BigDecimal( 1.5 ) );
            aFlight.addUsage( lEngine2, HOURS, new BigDecimal( -1.5 ) );
            aFlight.addUsage( lEngine2, LANDING, new BigDecimal( -1.5 ) );
         }
      } );

      // When the usage parameters are updated with values that result in decrementing and
      // incrementing integer usage differences.
      CollectedUsageParm[] lUpdatedUsage =
            { generateFlightUsage( lEngine1, CYCLES, new BigDecimal( 0.25 ) ),
                  generateFlightUsage( lEngine1, HOURS, new BigDecimal( -2.75 ) ),
                  generateFlightUsage( lEngine2, CYCLES, new BigDecimal( 2.75 ) ),
                  generateFlightUsage( lEngine2, HOURS, new BigDecimal( -0.25 ) ),
                  generateFlightUsage( lEngine2, LANDING, new BigDecimal( 0 ) ) };

      FlightInformationTO lFlightInfoTO = generateFlightToIndicatingUpdatedUsage();
      lFlightInfoTO.setEntryNote( USER_NOTE );

      lFlightHistBean.editHistFlight( lFlight, iHr, lFlightInfoTO, lUpdatedUsage, NO_MEASUREMENTS );

      // Then the flight history record's system note contains the usage differences for all the
      // usage parameters.
      List<String> lSystemNoteLines = getSystemNoteLines( lFlight );

      assertAllUsageDiffByInv( lSystemNoteLines, ENGINE_1_DESCRIPTION,
            getExpectedUsageDiffString( CYCLES, new BigDecimal( -1.25 ) ),
            getExpectedUsageDiffString( HOURS, new BigDecimal( -1.25 ) ) );
      assertAllUsageDiffByInv( lSystemNoteLines, ENGINE_2_DESCRIPTION,
            getExpectedUsageDiffString( CYCLES, new BigDecimal( 1.25 ) ),
            getExpectedUsageDiffString( HOURS, new BigDecimal( 1.25 ) ),
            getExpectedUsageDiffString( LANDING, new BigDecimal( 1.5 ) ) );
   }


   @Before
   public void setup() {

      // Unit under test.
      lFlightHistBean = new FlightHistBean();
      lFlightHistBean.ejbCreate();
      lFlightHistBean.setSessionContext( new SessionContextFake() );

      iExpectedSystemNoteTitle = i18n.get( "core.lbl.USAGE_CHANGE" ).toUpperCase();
      iExpectedUsageDiffString_HOURS =
            getExpectedUsageDiffString( HOURS, FLIGHT_HOURS_UPDATE_DIFF );
      iExpectedUsageDiffString_CYCLES =
            getExpectedUsageDiffString( CYCLES, FLIGHT_CYCLES_UPDATE_DIFF );
      iExpectedUsageDiffString_LANDING =
            getExpectedUsageDiffString( LANDING, FLIGHT_LANDING_UPDATE_DIFF );

      iExpectedUsageDiffString_LANDING_1 =
            getExpectedUsageDiffString( LANDING, FLIGHT_LANDING_UPDATE_DIFF_1 );
      iExpectedUsageDiffString_LANDING_2 =
            getExpectedUsageDiffString( LANDING, FLIGHT_LANDING_UPDATE_DIFF_2 );

      // Create an HR to test with.
      iHr = new HumanResourceDomainBuilder().build();

      // Setup fake user parameters to provide a MAX_FLIGHT_DURATION_VALUE for the flight duration
      // validation done in editHistFlight().
      int lUserId = OrgHr.findByPrimaryKey( iHr ).getUserId();
      UserParameters.setInstance( lUserId, "LOGIC", new UserParametersFake( lUserId, "LOGIC" ) );

      iDepartureAirport = Domain.createLocation( new DomainConfiguration<Location>() {

         @Override
         public void configure( Location aLocation ) {
            aLocation.setType( RefLocTypeKey.AIRPORT );
         }
      } );
      iArrivalAirport = Domain.createLocation( new DomainConfiguration<Location>() {

         @Override
         public void configure( Location aLocation ) {
            aLocation.setType( RefLocTypeKey.AIRPORT );
         }
      } );

   }


   private InventoryKey createEngine( final String aEngineDescription ) {
      return Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.setDescription( aEngineDescription );
            aEngine.addUsage( LANDING, INITIAL_LANDING );
         }
      } );
   }


   private InventoryKey createAircraft( final String aAircraftDescriton ) {
      return Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setDescription( aAircraftDescriton );
            aAircraft.addUsage( CYCLES, INITIAL_CYCLES );
            aAircraft.addUsage( HOURS, INITIAL_HOURS );
         }
      } );
   }


   private String getExpectedUsageDiffString( DataTypeKey aDataType, BigDecimal aUsageDifference ) {

      String lDataTypeCode = MimDataType.findByPrimaryKey( aDataType ).getDataTypeCd();
      String lSignum = ( aUsageDifference.signum() == 1 ) ? "+" : "";
      return lDataTypeCode + ": " + lSignum + aUsageDifference;
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


   private FlightInformationTO generateFlightToIndicatingUpdatedUsage() {
      // For these tests the default for usage-updated is true.
      FlightInformationTO lFlightInfoTO = new FlightInformationTO( "flight", null, null, null, null,
            null, iDepartureAirport, iArrivalAirport, null, null, null, null, new Date(),
            new Date(), null, null, false, false );
      lFlightInfoTO.setUsageUpdated( true );
      return lFlightInfoTO;
   }


   private String getSystemNote( FlightLegId aFlight ) {
      List<FlightLegStatusLogEntity> lLogEntities =
            new JdbcFlightLegStatusLogDao().findByParent( aFlight );

      String lSystemNote = null;
      for ( FlightLegStatusLogEntity entity : lLogEntities ) {
         FlightLegNoteId lSystemNoteId = entity.getSystemNote();
         if ( lSystemNoteId == null ) {
            continue;
         }

         FlightLegNoteEntity lSystemNoteEntity =
               new JdbcFlightLegNoteDao().findById( lSystemNoteId );
         lSystemNote = lSystemNoteEntity.getEntryNote();
         if ( lSystemNote != null ) {
            break;
         }
      }

      return lSystemNote;
   }


   private List<String> getSystemNoteLines( FlightLegId aFlight ) {

      String lSystemNote = getSystemNote( aFlight );

      Assert.assertNotNull( "Expected a system note to be in the flight leg note entity.",
            lSystemNote );
      Assert.assertFalse( "Expected the system note in the flight leg note entity to not be empty.",
            lSystemNote.isEmpty() );

      List<String> lSystemNoteLines =
            Arrays.asList( lSystemNote.split( Pattern.quote( LINE_BREAK ) ) );

      return lSystemNoteLines;
   }


   private void assertAllUsageDiffByInv( final List<String> aSystemNoteLines, String lInvDesc,
         final String... aExpectedUsageDiffStrings ) {

      // Find the starting index. Which is the index after the inventory description line.
      boolean lFound = false;
      int lStartIndex = 0;
      for ( String lSystemNoteLine : aSystemNoteLines ) {
         if ( lSystemNoteLine.contains( lInvDesc ) ) {
            // Start at the next index.
            lStartIndex++;
            lFound = true;
            break;
         }
         lStartIndex++;
      }
      if ( !lFound ) {
         Assert.fail( "Inventory description [" + lInvDesc + "] not found in system note." );
      }

      // Loop over the expected usage difference strings and assert they each exist.
      // Note that it is expected that all the usage difference string for the provided inventory
      // are in the provided expected list.
      int lEndIndex = lStartIndex + aExpectedUsageDiffStrings.length - 1;
      for ( String lExpectedUsageDiffString : aExpectedUsageDiffStrings ) {

         lFound = false;
         for ( int lIndex = lStartIndex; lIndex <= lEndIndex; lIndex++ ) {
            if ( aSystemNoteLines.get( lIndex ).contains( lExpectedUsageDiffString ) ) {
               lFound = true;
               break;
            }
         }
         if ( !lFound ) {
            Assert.fail( "Usage difference [" + lExpectedUsageDiffString + "] not found for ["
                  + lInvDesc + "] " );
         }
      }
   }

}
