package com.mxi.mx.core.services.flightfl.hist;

import static com.mxi.mx.core.key.DataTypeKey.CYCLES;
import static com.mxi.mx.core.key.DataTypeKey.HOURS;
import static com.mxi.mx.core.key.DataTypeKey.LANDING;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AircraftKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefDataSourceKey;
import com.mxi.mx.core.key.UsageDefinitionKey;
import com.mxi.mx.core.services.flighthist.FlightHistService;
import com.mxi.mx.core.table.eqp.EqpDataSourceSpec;
import com.mxi.mx.core.table.inv.InvInvTable;


/**
 * Test FlightHistService.GetAssemblyUsageDataSet method.
 *
 */
public class GetAssemblyUsageTest {

   private static final BigDecimal AIRCRAFT_CURRENT_USAGE = new BigDecimal( 100 );
   private static final BigDecimal ENGINE_CURRENT_USAGE = new BigDecimal( 200 );
   private static final BigDecimal OTHER_CURRENT_USAGE = new BigDecimal( 300 );
   private static final BigDecimal FLIGHT_TSN_USAGE = new BigDecimal( 10 );
   private static final BigDecimal FLIGHT_DELTA_USAGE = new BigDecimal( 1 );
   private static final BigDecimal CURRENT_FLIGHT_CYCLE_USAGE = new BigDecimal( 500.0 );
   private static final BigDecimal CURRENT_ENGINE_CYCLE_USAGE = new BigDecimal( 200.0 );

   /** Aircraft and engine info */
   private static final BigDecimal ACFT_CURRENT_CYCLE_USAGE = new BigDecimal( 300.0 );
   private static final BigDecimal ENGINE_CURRENT_CYCLE_USAGE = new BigDecimal( 50.0 );
   private static final BigDecimal ENGINE_CURRENT_HOURS_USAGE = new BigDecimal( 12.0 );

   /** Flight usage info */
   private static final Date FIRST_FLIGHT_ACTUAL_ARRIVAL_DATE =
         DateUtils.addDays( new Date(), -15 );
   private static final Date SECOND_FLIGHT_ACTUAL_ARRIVAL_DATE =
         DateUtils.addDays( new Date(), -14 );

   // DELTA
   private static final BigDecimal FLIGHT_CYCLE_DELTA = new BigDecimal( 1.0 );
   private static final BigDecimal FLIGHT_HOURS_DELTA = new BigDecimal( 2.0 );

   private static final BigDecimal ACFT_CYCLE_USAGE = new BigDecimal( 150.0 );
   private static final BigDecimal ENG_CYCLE_USAGE = new BigDecimal( 17.0 );
   private static final BigDecimal ENG_HOURS_USAGE = new BigDecimal( 3.0 );

   /** Out-Of-Sequence Actual Arrival Date */
   private static final Date OOS_ACTUAL_ARRIVAL_DATE = DateUtils.addDays( new Date(), -17 );
   /** In-Sequence Actual Arrival Date */
   private static final Date IS_ACTUAL_ARRIVAL_DATE = DateUtils.addDays( new Date(), +1 );

   private static final Date SWAP_DATE = DateUtils.addDays( new Date(), -5 );
   private static final Date POST_SWAP_DATE = DateUtils.addDays( SWAP_DATE, 2 );
   private static final Date PRE_SWAP_DATE = DateUtils.addDays( SWAP_DATE, -2 );

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private FlightHistService iFlightHistService = new FlightHistService();


   /**
    * <pre>
    * Test that the helper calculates appropriate usage when a date is out of sequence for the Aircraft, but in sequence for the Engine
    *
    * Given an aircraft which has had an engine swap
    *   And the aircraft has had a flight after the swap
    *   And the removed engine has NOT had a flight after the swap
    *
    *  When I create a historical flight that arrives before the swap
    *
    *  Then the usage returned for the Aircraft will be the Aircraft's subsequent flight's TSN less its delta
    *   And the usage returned for the Engine will be the Engine's current usage
    *
    * </pre>
    *
    * @throws MxException
    *            if a mx error occurs
    */
   @Test
   public void getAssemblyUsageDataSet_getsOOSUsageForAircraftAndISUsageForEngine()
         throws MxException {

      // Set up the aircraft
      final InventoryKey lAircraftKey = createAircraftWithUsage( AIRCRAFT_CURRENT_USAGE );

      // Set up the first engine which was removed from the aircraft
      final InventoryKey lFirstEngineKey =
            createEngineWithUsageAndConfigChange( ENGINE_CURRENT_USAGE, null, lAircraftKey );

      // Set up the second engine which was installed to the aircraft
      final InventoryKey lSecondEngineKey =
            createEngineWithUsageAndConfigChange( OTHER_CURRENT_USAGE, lAircraftKey, null );

      // The aircraft underwent usage after the swap
      Domain.createUsageAdjustment( aUsageAdjustment -> {
         aUsageAdjustment.setUsageDate( POST_SWAP_DATE );
         aUsageAdjustment.setMainInventory( lAircraftKey );
         aUsageAdjustment.addUsage( lAircraftKey, HOURS, FLIGHT_TSN_USAGE, FLIGHT_DELTA_USAGE );
         aUsageAdjustment.addUsage( lSecondEngineKey, HOURS, FLIGHT_TSN_USAGE, FLIGHT_DELTA_USAGE );
      } );

      DataSet lCreateFlightUsagesDS = iFlightHistService
            .getAssemblyUsageDataSet( new AircraftKey( lAircraftKey ), PRE_SWAP_DATE, true );

      assertEquals( "Expected two results", 2, lCreateFlightUsagesDS.getRowCount() );

      assertTrue( "Expected aircraft to calculate usage based on next flight",
            FLIGHT_TSN_USAGE.subtract( FLIGHT_DELTA_USAGE ).compareTo(
                  getFlightHoursSinceNew( lCreateFlightUsagesDS, lAircraftKey ) ) == 0 );

      assertTrue( "Expected engine to calculate usage based on current usage", ENGINE_CURRENT_USAGE
            .compareTo( getFlightHoursSinceNew( lCreateFlightUsagesDS, lFirstEngineKey ) ) == 0 );

   }


   /**
    * <pre>
    * Test the helper calculates appropriate usage when a date is in sequence for the Aircraft, but
    * out-of-sequence for the Engine
    *
    * Given an aircraft which has had an engine swap
    *   And the aircraft has NOT had a flight after the swap
    *   And the engine has had a flight after the swap
    *
    * When I create a historical flight which arrives before the swap
    *
    * Then the usage returned for the Aircraft will be the Aircraft's current usage
    *  And the usage returned for the Engine will be the Engine's subsequent flight's TSN less the delta
    * </pre>
    *
    * @throws MxException
    *            if a mx error occurs
    */
   @Test
   public void getAssemblyUsageDataSet_getsISUsageForAircraftAndOOSUsageForEngine()
         throws MxException {

      // Set up the first aircraft
      final InventoryKey lFirstAircraftKey = createAircraftWithUsage( AIRCRAFT_CURRENT_USAGE );

      // Set up the second aircraft
      final InventoryKey lSecondAircraftKey = createAircraftWithUsage( OTHER_CURRENT_USAGE );

      // Set up the engine what was swapped from the first aircraft to the second
      final InventoryKey lEngineKey = createEngineWithUsageAndConfigChange( ENGINE_CURRENT_USAGE,
            lSecondAircraftKey, lFirstAircraftKey );

      // The second aircraft underwent usage after the swap
      Domain.createUsageAdjustment( aUsageAdjustment -> {
         aUsageAdjustment.setUsageDate( POST_SWAP_DATE );
         aUsageAdjustment.setMainInventory( lSecondAircraftKey );
         aUsageAdjustment.addUsage( lSecondAircraftKey, HOURS, FLIGHT_TSN_USAGE,
               FLIGHT_DELTA_USAGE );
         aUsageAdjustment.addUsage( lEngineKey, HOURS, FLIGHT_TSN_USAGE, FLIGHT_DELTA_USAGE );
      } );

      DataSet lCreateFlightUsagesDS = iFlightHistService
            .getAssemblyUsageDataSet( new AircraftKey( lFirstAircraftKey ), PRE_SWAP_DATE, true );

      assertEquals( "Expected two results", 2, lCreateFlightUsagesDS.getRowCount() );

      assertTrue( "Expected aircraft to calculate usage from current usage", AIRCRAFT_CURRENT_USAGE
            .compareTo( getFlightHoursSinceNew( lCreateFlightUsagesDS, lFirstAircraftKey ) ) == 0 );

      assertTrue( "Expected engine to calculate usage from next flight",
            FLIGHT_TSN_USAGE.subtract( FLIGHT_DELTA_USAGE )
                  .compareTo( getFlightHoursSinceNew( lCreateFlightUsagesDS, lEngineKey ) ) == 0 );

   }


   /**
    * Test the "baseline_order" is always consistent with the order defined in usage definition.
    */
   @Test
   public void getAssemblyUsageDataSet_withReorderedUsageDefinition() {

      Date lToday = new Date();
      final InventoryKey lAircraft = createAircraftWithHoursCyclesLandingUsage();

      // Before reorder
      DataSet lUsagesDataSetBeforeReorder = iFlightHistService
            .getAssemblyUsageDataSet( new AircraftKey( lAircraft ), lToday, true );
      assertUsageDataBaselineOrder( lUsagesDataSetBeforeReorder, 1, 2, 3 );

      // Reorder
      reorderParameter( lAircraft, HOURS, 3 );
      reorderParameter( lAircraft, CYCLES, 1 );
      reorderParameter( lAircraft, LANDING, 2 );

      // After reorder
      DataSet lUsagesDataSetAfterReorder = iFlightHistService
            .getAssemblyUsageDataSet( new AircraftKey( lAircraft ), lToday, true );
      assertUsageDataBaselineOrder( lUsagesDataSetAfterReorder, 3, 1, 2 );

   }


   /**
    * This test is to verify the aircraft and engine's current usages are returned correctly.
    */
   @Test
   public void getAssemblyUsageDataSet_getAssembliesCurrentUsages() {

      // Create an aircraft
      final InventoryKey lAircraftKey = Domain.createAircraft(
            aAircraft -> aAircraft.addUsage( CYCLES, CURRENT_FLIGHT_CYCLE_USAGE ) );

      // Install an Engine
      final InventoryKey lEngineKey = Domain.createEngine( aEngine -> {
         aEngine.addUsage( CYCLES, CURRENT_ENGINE_CYCLE_USAGE );
         aEngine.setParent( lAircraftKey );

      } );

      // Create flight data source specification
      EqpDataSourceSpec
            .create( new UsageDefinitionKey( InvInvTable.getAssemblyByInventoryKey( lAircraftKey ),
                  RefDataSourceKey.MXFL ), CYCLES );

      // Create flight data source specification
      EqpDataSourceSpec.create( new UsageDefinitionKey(
            InvInvTable.getAssemblyByInventoryKey( lEngineKey ), RefDataSourceKey.MXFL ), CYCLES );

      DataSet lQs = iFlightHistService.getAssemblyUsageDataSet( new AircraftKey( lAircraftKey ),
            new Date(), true );

      assertEquals( 2, lQs.getRowCount() );

      boolean lGetAircraftUsages = false;
      boolean lGetEngineUsages = false;

      while ( lQs.next() ) {

         assertEquals( CYCLES, lQs.getKey( DataTypeKey.class, "data_type_key" ) );

         double lTsi = lQs.getDouble( "tsi_qt" );
         double lTsn = lQs.getDouble( "tsn_qt" );
         double lTso = lQs.getDouble( "tso_qt" );

         InventoryKey lInventory = lQs.getKey( InventoryKey.class, "inv_key" );

         if ( lAircraftKey.equals( lInventory ) ) {

            assertEquals( CURRENT_FLIGHT_CYCLE_USAGE.doubleValue(), lTsi, 0 );
            assertEquals( CURRENT_FLIGHT_CYCLE_USAGE.doubleValue(), lTsn, 0 );
            assertEquals( CURRENT_FLIGHT_CYCLE_USAGE.doubleValue(), lTso, 0 );
            lGetAircraftUsages = true;
         } else {

            assertEquals( lEngineKey, lInventory );
            assertEquals( CURRENT_ENGINE_CYCLE_USAGE.doubleValue(), lTsi, 0 );
            assertEquals( CURRENT_ENGINE_CYCLE_USAGE.doubleValue(), lTsn, 0 );
            assertEquals( CURRENT_ENGINE_CYCLE_USAGE.doubleValue(), lTso, 0 );
            lGetEngineUsages = true;
         }
      }

      if ( !lGetAircraftUsages ) {
         fail( "No usages returned for aircraft" );
      }

      if ( !lGetEngineUsages ) {
         fail( "No usages returned for engine" );
      }

   }


   /**
    * This test is to verify the assembly usages are taken from the usage records right after the
    * given actual arrival date.
    *
    * Scenario:
    *
    * -- On Aircraft with an installed engine,<br>
    * -- there is one flight with usage record,<br>
    * -- create an Out-Of-Sequence flight before this flight.<br>
    *
    * Verify:
    *
    * -- The usages on the flight minus the flight delta are returned.
    */
   @Test
   public void getAssemblyUsageDataSet_getAssembliesUsagesForOutOfSequenceFlight() {

      // Set up an Aircraft with an Engine and a flight in the past
      InventoryKey lAircraft = setUpAnAircraftWithCycles();
      InventoryKey lEngine = setUpAnEngineWithCyclesAndHoursOnAircraft( lAircraft );
      createUsageAdjustmentForAircraftAndEngine( lAircraft, lEngine );

      QuerySet lQs = iFlightHistService.getAssemblyUsageDataSet( new AircraftKey( lAircraft ),
            OOS_ACTUAL_ARRIVAL_DATE, true );

      assertEquals( 3, lQs.getRowCount() );

      boolean lGetAircraftUsages = false;
      boolean lGetEngineCycles = false;
      boolean lGetEngineHours = false;

      double lAcftCycleUsage = ACFT_CYCLE_USAGE.subtract( FLIGHT_CYCLE_DELTA ).doubleValue();
      double lEngineHoursUsage = ENG_HOURS_USAGE.subtract( FLIGHT_HOURS_DELTA ).doubleValue();
      double lEngineCycleUsage = ENG_CYCLE_USAGE.subtract( FLIGHT_CYCLE_DELTA ).doubleValue();

      while ( lQs.next() ) {

         InventoryKey lInventory = lQs.getKey( InventoryKey.class, "inv_key" );
         DataTypeKey lDataType = lQs.getKey( DataTypeKey.class, "data_type_key" );

         if ( lAircraft.equals( lInventory ) ) {

            assertEquals( CYCLES, lDataType );
            assertUsages( lAcftCycleUsage, lQs );
            lGetAircraftUsages = true;
         } else {

            assertEquals( lEngine, lInventory );

            if ( CYCLES.equals( lDataType ) ) {

               assertUsages( lEngineCycleUsage, lQs );
               lGetEngineCycles = true;
            } else {

               assertEquals( HOURS, lDataType );
               assertUsages( lEngineHoursUsage, lQs );
               lGetEngineHours = true;
            }
         }
      } ;

      if ( !lGetAircraftUsages ) {
         fail( "No usages returned for aircraft" );
      }

      if ( !lGetEngineCycles ) {
         Assert.fail( "No cycles usages returned for engine" );
      }

      if ( !lGetEngineHours ) {
         Assert.fail( "No hours usages returned for engine" );
      }
   }


   /**
    * This test is to verify the in sequence flight will get null by executing this query
    *
    * Scenario:
    *
    * -- On Aircraft with an installed engine,<br>
    * -- there is one flight with usage record,<br>
    * -- create an In-Sequence flight after this flight.<br>
    *
    * Verify:
    *
    * -- Null is returned.
    */
   @Test
   public void getAssemblyUsageDataSet_getAssembliesUsagesForInSequenceFlight() {

      // Set up an Aircraft with an Engine and a flight in the past
      InventoryKey lAircraft = setUpAnAircraftWithCycles();
      InventoryKey lEngine = setUpAnEngineWithCyclesAndHoursOnAircraft( lAircraft );
      createUsageAdjustmentForAircraftAndEngine( lAircraft, lEngine );

      QuerySet lQs = iFlightHistService.getAssemblyUsageDataSet( new AircraftKey( lAircraft ),
            IS_ACTUAL_ARRIVAL_DATE, true );

      assertEquals( 3, lQs.getRowCount() );

      boolean lGetAircraftUsages = false;
      boolean lGetEngineCycles = false;
      boolean lGetEngineHours = false;

      while ( lQs.next() ) {

         InventoryKey lInventory = lQs.getKey( InventoryKey.class, "inv_key" );
         DataTypeKey lDataType = lQs.getKey( DataTypeKey.class, "data_type_key" );

         if ( lAircraft.equals( lInventory ) ) {

            assertEquals( CYCLES, lDataType );
            assertUsages( ACFT_CURRENT_CYCLE_USAGE.doubleValue(), lQs );
            lGetAircraftUsages = true;
         } else {

            assertEquals( lEngine, lInventory );

            if ( CYCLES.equals( lDataType ) ) {

               assertUsages( ENGINE_CURRENT_CYCLE_USAGE.doubleValue(), lQs );
               lGetEngineCycles = true;
            } else {

               assertEquals( HOURS, lDataType );
               assertUsages( ENGINE_CURRENT_HOURS_USAGE.doubleValue(), lQs );
               lGetEngineHours = true;
            }
         }
      } ;

      if ( !lGetAircraftUsages ) {
         fail( "No usages returned for aircraft" );
      }

      if ( !lGetEngineCycles ) {
         Assert.fail( "No cycles usages returned for engine" );
      }

      if ( !lGetEngineHours ) {
         Assert.fail( "No hours usages returned for engine" );
      }
   }


   /**
    * <pre>
    * Test that the query returns all parameters defined against eqp_data_source_spec,
    * even when the next chronological usage record for the assembly does not have them defined.
    *
    *  Given an assembly that has usage parameters A and B defined against data source MXFL
    *    and the assembly has a first usage adjustment collecting parameter A
    *    and the assembly has a second usage adjustment collecting parameters A and B
    *   When I execute the query against the aircraft and a time before the first usage
    *   Then the result should contain the tsn quantity less the delta from the first usage adjustment for A
    *    and the result should contain the tsn quantity less the delta from the second usage adjustment for B
    * </pre>
    */
   @Test
   public void getAssemblyUsageDataSet_getsNextUsageRecordPerParameter() {

      final InventoryKey lAircraft = Domain.createAircraft();
      final InventoryKey lEngine = setUpAnEngineWithCyclesAndHoursOnAircraft( lAircraft );

      // Create a first usage for engine with one parameter
      Domain.createUsageAdjustment( aUsageRecord -> {
         aUsageRecord.setUsageDate( FIRST_FLIGHT_ACTUAL_ARRIVAL_DATE );
         aUsageRecord.addUsage( lEngine, CYCLES, ENG_CYCLE_USAGE, FLIGHT_CYCLE_DELTA );
      } );

      // Create a second usage for engine with two parameters
      Domain.createUsageAdjustment( aUsageRecord -> {
         aUsageRecord.setUsageDate( SECOND_FLIGHT_ACTUAL_ARRIVAL_DATE );
         aUsageRecord.addUsage( lEngine, CYCLES, ENG_CYCLE_USAGE.add( FLIGHT_CYCLE_DELTA ),
               FLIGHT_CYCLE_DELTA );
         aUsageRecord.addUsage( lEngine, HOURS, ENG_HOURS_USAGE, FLIGHT_HOURS_DELTA );
      } );

      QuerySet lQs = iFlightHistService.getAssemblyUsageDataSet( new AircraftKey( lAircraft ),
            OOS_ACTUAL_ARRIVAL_DATE, true );

      boolean lGetEngineCycles = false;
      boolean lGetEngineHours = false;

      double lEngineHoursUsage = ENG_HOURS_USAGE.subtract( FLIGHT_HOURS_DELTA ).doubleValue();
      double lEngineCycleUsage = ENG_CYCLE_USAGE.subtract( FLIGHT_CYCLE_DELTA ).doubleValue();

      while ( lQs.next() ) {

         InventoryKey lInventory = lQs.getKey( InventoryKey.class, "inv_key" );
         DataTypeKey lDataType = lQs.getKey( DataTypeKey.class, "data_type_key" );

         assertEquals( "Wrong inventory returned", lEngine, lInventory );

         if ( CYCLES.equals( lDataType ) ) {
            assertUsages( lEngineCycleUsage, lQs );
            lGetEngineCycles = true;
         } else {
            assertEquals( "Wrong data type returned", HOURS, lDataType );
            assertUsages( lEngineHoursUsage, lQs );
            lGetEngineHours = true;
         }

      } ;

      if ( !lGetEngineCycles ) {
         fail( "No cycles usages returned for engine" );
      }

      if ( !lGetEngineHours ) {
         fail( "No hours usages returned for engine" );
      }

      assertEquals( "Wrong number of rows returned", 2, lQs.getRowCount() );
   }


   /**
    * Assert the usages
    *
    * @param aUsage
    *           the usage
    * @param aQs
    *           the queryset
    */
   private void assertUsages( double aUsage, QuerySet aQs ) {

      assertEquals( "Incorrect TSI", aUsage, aQs.getDouble( "tsi_qt" ), 0 );
      assertEquals( "Incorrect TSI", aUsage, aQs.getDouble( "tsn_qt" ), 0 );
      assertEquals( "Incorrect TSI", aUsage, aQs.getDouble( "tso_qt" ), 0 );
   }


   private InventoryKey setUpAnAircraftWithCycles() {

      // Create an aircraft
      InventoryKey lAircraft = Domain
            .createAircraft( aAircraft -> aAircraft.addUsage( CYCLES, ACFT_CURRENT_CYCLE_USAGE ) );

      // Create flight data source specifications
      createFlightDataSourceSpec( lAircraft, CYCLES );

      return lAircraft;

   }


   private InventoryKey setUpAnEngineWithCyclesAndHoursOnAircraft( final InventoryKey aAircraft ) {

      // Install an Engine to the aircraft
      InventoryKey lEngine = Domain.createEngine( aEngine -> {
         aEngine.addUsage( CYCLES, ENGINE_CURRENT_CYCLE_USAGE );
         aEngine.addUsage( HOURS, ENGINE_CURRENT_HOURS_USAGE );
         aEngine.setParent( aAircraft );
      } );

      createFlightDataSourceSpec( lEngine, CYCLES );
      createFlightDataSourceSpec( lEngine, HOURS );

      return lEngine;
   }


   private void createUsageAdjustmentForAircraftAndEngine( final InventoryKey aAircraft,
         final InventoryKey aEngine ) {
      // Create a flight for the aircraft and engine
      Domain.createUsageAdjustment( aUsageRecord -> {
         aUsageRecord.setUsageDate( FIRST_FLIGHT_ACTUAL_ARRIVAL_DATE );
         aUsageRecord.addUsage( aAircraft, CYCLES, ACFT_CYCLE_USAGE, FLIGHT_CYCLE_DELTA );
         aUsageRecord.addUsage( aEngine, CYCLES, ENG_CYCLE_USAGE, FLIGHT_CYCLE_DELTA );
         aUsageRecord.addUsage( aEngine, HOURS, ENG_HOURS_USAGE, FLIGHT_HOURS_DELTA );
      } );
   }


   private void createFlightDataSourceSpec( InventoryKey aInv, DataTypeKey aDataTypeKey ) {
      EqpDataSourceSpec.create( new UsageDefinitionKey(
            InvInvTable.getAssemblyByInventoryKey( aInv ), RefDataSourceKey.MXFL ), aDataTypeKey );
   }


   /**
    * Reorder MXFL usage parameters.
    *
    * @param aInventoryKey
    *           the assembly
    * @param aDataTypeKey
    *           the usage parameter
    * @param aNewOrder
    *           the new order
    */
   private void reorderParameter( InventoryKey aInventoryKey, DataTypeKey aDataTypeKey,
         int aNewOrder ) {
      EqpDataSourceSpec lHours = EqpDataSourceSpec.findByPrimaryKey(
            new UsageDefinitionKey( InvInvTable.getAssemblyByInventoryKey( aInventoryKey ),
                  RefDataSourceKey.MXFL ),
            aDataTypeKey );
      lHours.setDataOrd( aNewOrder );
      lHours.update();
   }


   /**
    * Assert the "baseline_order" in the usage DataSet is consistent with the order defined in usage
    * definition.
    *
    * @param aCreateFlightUsagesDS
    *           the DataSet
    * @param aHoursOrder
    *           HOURS order
    * @param aCyclesOrder
    *           CYCLES order
    * @param aLandingOrder
    *           LANDING order
    */
   private void assertUsageDataBaselineOrder( DataSet aCreateFlightUsagesDS, int aHoursOrder,
         int aCyclesOrder, int aLandingOrder ) {
      assertEquals( "Expected three results", 3, aCreateFlightUsagesDS.getRowCount() );

      aCreateFlightUsagesDS.next();
      assertEquals( "Expected the first row is the HOURS", HOURS,
            aCreateFlightUsagesDS.getKey( DataTypeKey.class, "data_type_key" ) );
      assertEquals( "Expected the baseline order of HOURS is " + aHoursOrder, aHoursOrder,
            aCreateFlightUsagesDS.getInt( "baseline_order" ) );

      aCreateFlightUsagesDS.next();
      assertEquals( "Expected the second row is the CYCLES", CYCLES,
            aCreateFlightUsagesDS.getKey( DataTypeKey.class, "data_type_key" ) );
      assertEquals( "Expected the baseline order of CYCLES is " + aCyclesOrder, aCyclesOrder,
            aCreateFlightUsagesDS.getInt( "baseline_order" ) );

      aCreateFlightUsagesDS.next();
      assertEquals( "Expected the third row is the LANDING", LANDING,
            aCreateFlightUsagesDS.getKey( DataTypeKey.class, "data_type_key" ) );
      assertEquals( "Expected the baseline order of LANDING is " + aLandingOrder, aLandingOrder,
            aCreateFlightUsagesDS.getInt( "baseline_order" ) );
   }


   private InventoryKey createAircraftWithUsage( final BigDecimal aCurrentUsage ) {
      InventoryKey lAircraftKey =
            Domain.createAircraft( aAircraft -> aAircraft.addUsage( HOURS, aCurrentUsage ) );

      // Create flight data source specification
      EqpDataSourceSpec.create( new UsageDefinitionKey(
            InvInvTable.getAssemblyByInventoryKey( lAircraftKey ), RefDataSourceKey.MXFL ), HOURS );

      return lAircraftKey;
   }


   private InventoryKey createAircraftWithHoursCyclesLandingUsage() {
      InventoryKey lAircraftKey = Domain.createAircraft( aAircraft -> {
         aAircraft.addUsage( HOURS, new BigDecimal( 200 ) );
         aAircraft.addUsage( CYCLES, new BigDecimal( 50 ) );
         aAircraft.addUsage( LANDING, new BigDecimal( 100 ) );
      } );

      // Create flight data source specification
      UsageDefinitionKey lUsageDefinition = new UsageDefinitionKey(
            InvInvTable.getAssemblyByInventoryKey( lAircraftKey ), RefDataSourceKey.MXFL );
      EqpDataSourceSpec.create( lUsageDefinition, HOURS );
      EqpDataSourceSpec.create( lUsageDefinition, CYCLES );
      EqpDataSourceSpec.create( lUsageDefinition, LANDING );

      return lAircraftKey;
   }


   private InventoryKey createEngineWithUsageAndConfigChange( final BigDecimal aCurrentUsage,
         final InventoryKey aCurrentParent, final InventoryKey aPreviousParent ) {
      InventoryKey lEngineKey = Domain.createEngine( aEngine -> {
         aEngine.addUsage( HOURS, ENGINE_CURRENT_USAGE );

         if ( aPreviousParent != null ) {
            // It was removed from the first aircraft on the swap date
            aEngine.addRemovalRecord( aRemovalRecord -> {
               aRemovalRecord.setRemovalDate( SWAP_DATE );
               aRemovalRecord.setParent( aPreviousParent );
               aRemovalRecord.setAssembly( aPreviousParent );
               aRemovalRecord.setHighest( aPreviousParent );
            } );
         }

         if ( aCurrentParent != null ) {
            // It is presently on the second
            aEngine.setParent( aCurrentParent );

            // It was installed to the second aircraft on the swap date
            aEngine.addInstallationRecord( aInstallationRecord -> {
               aInstallationRecord.setInstallationDate( SWAP_DATE );
               aInstallationRecord.setParent( aCurrentParent );
               aInstallationRecord.setAssembly( aCurrentParent );
               aInstallationRecord.setHighest( aCurrentParent );
            } );
         }
      } );

      // Create flight data source specification
      EqpDataSourceSpec.create( new UsageDefinitionKey(
            InvInvTable.getAssemblyByInventoryKey( lEngineKey ), RefDataSourceKey.MXFL ), HOURS );

      return lEngineKey;
   }


   /*
    * Returns the TSN value of the HOURS data type for an inventory in a dataset returned by
    * getAssyUsageDataSet* or null, if it doesn't exist
    */
   private BigDecimal getFlightHoursSinceNew( DataSet aDataSet, InventoryKey aInventoryKey ) {

      while ( aDataSet.next() ) {
         if ( aInventoryKey.equals( aDataSet.getKey( InventoryKey.class, "inv_key" ) )
               && HOURS.equals( aDataSet.getKey( DataTypeKey.class, "data_type_key" ) ) ) {

            // Reset the iterator and return the TSN value
            BigDecimal lDelta_Qt = new BigDecimal( aDataSet.getDouble( "tsn_qt" ) );
            aDataSet.beforeFirst();
            return lDelta_Qt;
         }
      }

      return null;
   }

}
