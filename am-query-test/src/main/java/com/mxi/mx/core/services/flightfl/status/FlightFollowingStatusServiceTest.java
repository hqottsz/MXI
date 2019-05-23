package com.mxi.mx.core.services.flightfl.status;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.AircraftAssembly;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Engine;
import com.mxi.am.domain.EngineAssembly;
import com.mxi.am.domain.Flight;
import com.mxi.am.domain.Location;
import com.mxi.am.domain.UsageDefinition;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersStub;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.flight.dao.FlightLegDao;
import com.mxi.mx.core.flight.dao.FlightLegEntity;
import com.mxi.mx.core.flight.dao.JdbcFlightLegDao;
import com.mxi.mx.core.flight.model.FlightLegId;
import com.mxi.mx.core.flight.model.FlightLegStatus;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.UsageParmKey;
import com.mxi.mx.core.services.flighthist.FlightInformationTO;
import com.mxi.mx.core.table.org.OrgHr;
import com.mxi.mx.core.usage.dao.JdbcUsageDataDao;
import com.mxi.mx.core.usage.dao.UsageDataDao;
import com.mxi.mx.core.usage.dao.UsageDataEntity;
import com.mxi.mx.core.usage.model.UsageAdjustmentId;
import com.mxi.mx.core.usage.service.UsageDelta;


/**
 * Tests for {@linkplain FlightFollowingStatusService} that involve modifying flight status, flight
 * completion
 *
 */

public class FlightFollowingStatusServiceTest {

   private static final String FLIGHT_NAME = "FLIGHT_NAME";
   private static final Date ARRIVAL_DATE = DateUtils.addDays( new Date(), -1 );
   private static final String ARRIVAL_LOCATION = "LAX";
   private static final String AIRCRAFT_CURRENT_LOCATION = "LHR";
   private static final Boolean MOVE_AIRCRAFT_TO_ARRIVAL_LOCATION = true;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * <pre>
    *
    * Scenario: Verify that the status of the flight is set to COMPLETE when a planned flight is completed.
    * 
    * Given an aircraft
    * And the aircraft has a planned flight
    * When the planned flight is completed
    * Then the flight status changes to complete
    *
    * </pre>
    */
   @Test
   public void itSetsFlightStatusToCompleteWhenPlannedFlightIsCompleted() throws Exception {

      // Given an aircraft
      final InventoryKey lAircraft = Domain.createAircraft();

      // Given the aircraft has a planned flight
      final FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {

            aFlight.setAircraft( lAircraft );
            aFlight.setName( FLIGHT_NAME );
            aFlight.setHistorical( false );
         }
      } );

      // Required by updateAndCompleteWithMeasurements() method in FlightFollowingStatusService
      final FlightInformationTO lFlightInformationTO =
            generateFlightInfo( FLIGHT_NAME, ARRIVAL_DATE, MOVE_AIRCRAFT_TO_ARRIVAL_LOCATION );
      final UsageDelta[] lUsageDelta = new UsageDelta[0];
      HumanResourceKey lHr = initHumanResource();

      // When the planned flight is completed
      new FlightFollowingStatusService().updateAndCompleteWithMeasurements( lFlight,
            lFlightInformationTO, lHr, lUsageDelta, null, null );

      // Then the flight status is Complete
      final FlightLegEntity lFlightLegEntity = new JdbcFlightLegDao().findById( lFlight );
      final FlightLegStatus lExpectedFlightStatus = FlightLegStatus.MXCMPLT;
      final FlightLegStatus lActualFlightStatus = lFlightLegEntity.getFlightStatus();
      assertEquals(
            "The flight status was not changed to complete after the completion of the planned flight.",
            0, lActualFlightStatus.compareTo( lExpectedFlightStatus ) );

   }


   /**
    * <pre>
    *
    * Scenario: Verify that completing a planned flight out of sequence updates the aircraft and sub-assembly current usage.
    * 
    * Given an aircraft with a sub-assembly engine
    * And the aircraft has a planned and a historical flight
    * When the planned flight is completed prior to the historical flight's completion time
    * Then the aircraft and engine current usages are incremented by the usage accrued in the completion of the planned flight
    *
    * </pre>
    */
   @Test
   public void
         itUpdatesAircraftSubAssemblyCurrentUsageWhenPlannedFlightCompletedBeforeHistoricalFlight()
               throws Exception {

      // Required by FlightHistService (getAssemblyUsageDataSet())
      final AssemblyKey lAircraftAssembly = createAircraftAssembly();
      final AssemblyKey lEngineAssembly =
            Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

               @Override
               public void configure( EngineAssembly aBuilder ) {
                  aBuilder.addUsageDefinitionConfiguration(
                        new DomainConfiguration<UsageDefinition>() {

                           @Override
                           public void configure( UsageDefinition aBuilder ) {
                              aBuilder.addUsageParameter( DataTypeKey.HOURS );
                              aBuilder.addUsageParameter( DataTypeKey.CYCLES );
                           }
                        } );
               }
            } );

      // Given
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aBuilder ) {
            aBuilder.addUsage( DataTypeKey.CYCLES, BigDecimal.TEN );
            aBuilder.addUsage( DataTypeKey.HOURS, BigDecimal.TEN );
            aBuilder.setAssembly( lAircraftAssembly );
         }
      } );

      final InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aBuilder ) {
            aBuilder.setParent( lAircraft );
            aBuilder.addUsage( DataTypeKey.CYCLES, BigDecimal.TEN );
            aBuilder.addUsage( DataTypeKey.HOURS, BigDecimal.TEN );
            aBuilder.setAssembly( lEngineAssembly );
         }
      } );

      // Given the aircraft has a planned flight
      final FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {

            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( false );
            aFlight.setName( FLIGHT_NAME );
         }
      } );

      // Given the aircraft has a historical flight
      Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {

            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( ARRIVAL_DATE );
         }
      } );

      // Required by updateAndCompleteWithMeasurements() method in FlightFollowingStatusService
      BigDecimal lFlightDeltaHoursForFlightBeforeHistoricalFlight = new BigDecimal( 4 );
      BigDecimal lFlightDeltaCyclesForFlightBeforeHistoricalFlight = BigDecimal.ONE;
      Date lDepartureDateForFlightBeforeHistoricalFlight = DateUtils.addHours( ARRIVAL_DATE, -8 );
      Date lArrivalDateForFlightBeforeHistoricalFlight =
            DateUtils.addHours( lDepartureDateForFlightBeforeHistoricalFlight,
                  lFlightDeltaHoursForFlightBeforeHistoricalFlight.intValue() );

      final FlightInformationTO lFlightInformationTO = generateFlightInfo( FLIGHT_NAME,
            lArrivalDateForFlightBeforeHistoricalFlight, !MOVE_AIRCRAFT_TO_ARRIVAL_LOCATION );
      final UsageDelta[] lUsageDelta =
            { new UsageDelta(), new UsageDelta(), new UsageDelta(), new UsageDelta() };
      lUsageDelta[0].setDelta( lFlightDeltaHoursForFlightBeforeHistoricalFlight.doubleValue() );
      lUsageDelta[0].setUsageParmKey( new UsageParmKey( lAircraft, DataTypeKey.HOURS ) );
      lUsageDelta[1].setDelta( lFlightDeltaHoursForFlightBeforeHistoricalFlight.doubleValue() );
      lUsageDelta[1].setUsageParmKey( new UsageParmKey( lEngine, DataTypeKey.HOURS ) );
      lUsageDelta[2].setDelta( lFlightDeltaCyclesForFlightBeforeHistoricalFlight.doubleValue() );
      lUsageDelta[2].setUsageParmKey( new UsageParmKey( lAircraft, DataTypeKey.CYCLES ) );
      lUsageDelta[3].setDelta( lFlightDeltaCyclesForFlightBeforeHistoricalFlight.doubleValue() );
      lUsageDelta[3].setUsageParmKey( new UsageParmKey( lEngine, DataTypeKey.CYCLES ) );
      HumanResourceKey lHr = initHumanResource();

      // When the planned flight is completed
      new FlightFollowingStatusService().updateAndCompleteWithMeasurements( lFlight,
            lFlightInformationTO, lHr, lUsageDelta, null, null );

      // Then the engine and aircraft usages are updated accordingly
      BigDecimal lExpectedCurrentHours =
            BigDecimal.TEN.add( lFlightDeltaHoursForFlightBeforeHistoricalFlight );
      BigDecimal lExpectedCurrentCycles = new BigDecimal( 11 );
      Map<DataTypeKey, BigDecimal> lAircraftCurrentUsageMap = Domain.readCurrentUsage( lAircraft );
      Map<DataTypeKey, BigDecimal> lEngineCurrentUsageMap = Domain.readCurrentUsage( lEngine );

      assertEquals(
            "The aircraft current hour usages were not  incremented by the hour usage accrued in the completion of the planned flight.",
            lExpectedCurrentHours, lAircraftCurrentUsageMap.get( DataTypeKey.HOURS ) );
      assertEquals(
            "The aircraft current cycle usages were not  incremented by the cycle usage accrued in the completion of the planned flight.",
            lExpectedCurrentCycles, lAircraftCurrentUsageMap.get( DataTypeKey.CYCLES ) );
      assertEquals(
            "The engine current hour usages were not  incremented by the hour usage accrued in the completion of the planned flight.",
            lExpectedCurrentHours, lEngineCurrentUsageMap.get( DataTypeKey.HOURS ) );
      assertEquals(
            "The engine current cycle usages were not  incremented by the cycle usage accrued in the completion of the planned flight.",
            lExpectedCurrentCycles, lEngineCurrentUsageMap.get( DataTypeKey.CYCLES ) );

   }


   /**
    * <pre>
    *
    * Scenario: Verify that the aircraft location is set to the arrival location of the flight
    * when a planned flight is completed and the "move-aircraft" flag is set to true.
    * 
    * Given an aircraft at a departure location
    * And the aircraft has a planned flight with a different arrival location than the departure location
    * When the planned flight is completed
    * Then the current location of the aircraft is the arrival location assigned.
    *
    * </pre>
    */
   @Test
   public void
         itSetsAircraftLocationToArrivalLocationWhenFlightCompletedWithMoveAircraftFlagSetToTrue()
               throws Exception {

      final LocationKey lArrivalLocation = createLocation( ARRIVAL_LOCATION );

      final LocationKey lAircraftCurrentLocation = createLocation( AIRCRAFT_CURRENT_LOCATION );

      // Given an aircraft at a departure location
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setLocation( lAircraftCurrentLocation );
         }
      } );

      // Given the aircraft has a planned flight with an arrival location different from the
      // departure location
      final FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {

            aFlight.setAircraft( lAircraft );
            aFlight.setName( FLIGHT_NAME );
            aFlight.setArrivalLocation( lArrivalLocation );
            aFlight.setHistorical( false );
         }
      } );

      // Required by updateAndCompleteWithMeasurements() method in FlightFollowingStatusService
      final FlightInformationTO lFlightInformationTO =
            generateFlightInfo( FLIGHT_NAME, ARRIVAL_DATE, MOVE_AIRCRAFT_TO_ARRIVAL_LOCATION );
      final UsageDelta[] lUsageDelta = new UsageDelta[0];
      HumanResourceKey lHr = initHumanResource();

      // When the planned flight is completed
      new FlightFollowingStatusService().updateAndCompleteWithMeasurements( lFlight,
            lFlightInformationTO, lHr, lUsageDelta, null, null );

      // Then the location of the aircraft is the arrival location
      final DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "inv_no_db_id", lAircraft.getDbId() );
      lArgs.add( "inv_no_id", lAircraft.getId() );
      final QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "inv_inv", lArgs );
      lQs.next();
      final LocationKey lActualAircraftLocation =
            lQs.getKey( LocationKey.class, "loc_db_id", "loc_id" );
      assertEquals(
            "The aircraft location was not changed to the arrival location after the completion of the planned flight.",
            lArrivalLocation, lActualAircraftLocation );

   }


   /**
    * <pre>
    *
    * Scenario: Verify that the aircraft location remains unchanged
    * when a planned flight is completed and the "move-aircraft" flag is set to false.
    * 
    * Given an aircraft at a departure location
    * And the aircraft has a planned flight with a different arrival location than the departure location
    * And in the planned flight we opt not to move the aircraft to the arrival location
    * When the planned flight is completed
    * Then the current location of the aircraft does not get changed
    *
    * </pre>
    */
   @Test
   public void itLeavesAircraftLocationUnchangedWhenFlightCompletedWithMoveAircraftFlagSetToFalse()
         throws Exception {

      final LocationKey lArrivalLocation = createLocation( ARRIVAL_LOCATION );

      final LocationKey lAircraftCurrentLocation = createLocation( AIRCRAFT_CURRENT_LOCATION );

      // Given an aircraft at a departure location
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setLocation( lAircraftCurrentLocation );
         }
      } );

      // Given the aircraft has a planned flight with an arrival location different from the
      // aircraft's current location
      final FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {

            aFlight.setAircraft( lAircraft );
            aFlight.setName( FLIGHT_NAME );
            aFlight.setArrivalLocation( lArrivalLocation );
            aFlight.setHistorical( false );
         }
      } );

      // Required by updateAndCompleteWithMeasurements() method in FlightFollowingStatusService
      final FlightInformationTO lFlightInformationTO =
            generateFlightInfo( FLIGHT_NAME, ARRIVAL_DATE, !MOVE_AIRCRAFT_TO_ARRIVAL_LOCATION );
      final UsageDelta[] lUsageDelta = new UsageDelta[0];
      HumanResourceKey lHr = initHumanResource();

      // When the planned flight is completed
      new FlightFollowingStatusService().updateAndCompleteWithMeasurements( lFlight,
            lFlightInformationTO, lHr, lUsageDelta, null, null );

      // Then the location of the aircraft is not changed
      final DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "inv_no_db_id", lAircraft.getDbId() );
      lArgs.add( "inv_no_id", lAircraft.getId() );
      final QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "inv_inv", lArgs );
      lQs.next();
      final LocationKey lActualAircraftLocation =
            lQs.getKey( LocationKey.class, "loc_db_id", "loc_id" );
      assertEquals(
            "The aircraft location was changed from the aircraft's current location after the completion of the planned flight.",
            lAircraftCurrentLocation, lActualAircraftLocation );

   }


   /**
    * <pre>
    *
    * Scenario: Verify that completing a planned flight out of sequence updates the subsequent historical flight aircraft/sub-assembly engine usage
    * 
    * Given an aircraft with a sub-assembly engine
    * And the aircraft has a planned and a historical flight
    * When the planned flight is completed prior to the historical flight's completion time
    * Then the subsequent historical flight's usage snapshot for aircraft and engine usages are incremented by the usage accrued in the completion of the planned flight
    *
    * </pre>
    */
   @Test
   public void
         itUpdatesSubsequentHistoricalFlightAircraftAndSubAssemblyUsageWhenPlannedFlightCompletedeBeforeHistoricalFlight()
               throws Exception {

      // Required by FlightHistService (getAssemblyUsageDataSet())
      final AssemblyKey lAircraftAssembly = createAircraftAssembly();
      final AssemblyKey lEngineAssembly =
            Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

               @Override
               public void configure( EngineAssembly aBuilder ) {
                  aBuilder.addUsageDefinitionConfiguration(
                        new DomainConfiguration<UsageDefinition>() {

                           @Override
                           public void configure( UsageDefinition aBuilder ) {
                              aBuilder.addUsageParameter( DataTypeKey.HOURS );
                              aBuilder.addUsageParameter( DataTypeKey.CYCLES );
                           }
                        } );
               }
            } );

      // Given
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aBuilder ) {
            aBuilder.addUsage( DataTypeKey.CYCLES, BigDecimal.TEN );
            aBuilder.addUsage( DataTypeKey.HOURS, BigDecimal.TEN );
            aBuilder.setAssembly( lAircraftAssembly );
         }
      } );

      final InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aBuilder ) {
            aBuilder.setParent( lAircraft );
            aBuilder.addUsage( DataTypeKey.CYCLES, BigDecimal.TEN );
            aBuilder.addUsage( DataTypeKey.HOURS, BigDecimal.TEN );
            aBuilder.setAssembly( lEngineAssembly );
         }
      } );

      // Given the aircraft has a planned flight
      final FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {

            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( false );
            aFlight.setName( FLIGHT_NAME );
         }
      } );

      // Given the aircraft has a historical flight
      Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( ARRIVAL_DATE );
         }
      } );

      // Required by updateAndCompleteWithMeasurements() method in FlightFollowingStatusService
      BigDecimal lFlightDeltaHoursForFlightBeforeHistoricalFlight = new BigDecimal( 4 );
      BigDecimal lFlightDeltaCyclesForFlightBeforeHistoricalFlight = BigDecimal.ONE;
      Date lDepartureDateForFlightBeforeHistoricalFlight = DateUtils.addHours( ARRIVAL_DATE, -8 );
      Date lArrivalDateForFlightBeforeHistoricalFlight =
            DateUtils.addHours( lDepartureDateForFlightBeforeHistoricalFlight,
                  lFlightDeltaHoursForFlightBeforeHistoricalFlight.intValue() );

      final FlightInformationTO lFlightInformationTO = generateFlightInfo( FLIGHT_NAME,
            lArrivalDateForFlightBeforeHistoricalFlight, !MOVE_AIRCRAFT_TO_ARRIVAL_LOCATION );
      final UsageDelta[] lUsageDelta =
            { new UsageDelta(), new UsageDelta(), new UsageDelta(), new UsageDelta() };
      lUsageDelta[0].setDelta( lFlightDeltaHoursForFlightBeforeHistoricalFlight.doubleValue() );
      lUsageDelta[0].setUsageParmKey( new UsageParmKey( lAircraft, DataTypeKey.HOURS ) );
      lUsageDelta[1].setDelta( lFlightDeltaHoursForFlightBeforeHistoricalFlight.doubleValue() );
      lUsageDelta[1].setUsageParmKey( new UsageParmKey( lEngine, DataTypeKey.HOURS ) );
      lUsageDelta[2].setDelta( lFlightDeltaCyclesForFlightBeforeHistoricalFlight.doubleValue() );
      lUsageDelta[2].setUsageParmKey( new UsageParmKey( lAircraft, DataTypeKey.CYCLES ) );
      lUsageDelta[3].setDelta( lFlightDeltaCyclesForFlightBeforeHistoricalFlight.doubleValue() );
      lUsageDelta[3].setUsageParmKey( new UsageParmKey( lEngine, DataTypeKey.CYCLES ) );
      HumanResourceKey lHr = initHumanResource();

      // When the planned flight is completed
      new FlightFollowingStatusService().updateAndCompleteWithMeasurements( lFlight,
            lFlightInformationTO, lHr, lUsageDelta, null, null );

      // Then the engine and aircraft usages are updated accordingly
      BigDecimal lExpectedHourUsage =
            BigDecimal.TEN.add( lFlightDeltaHoursForFlightBeforeHistoricalFlight );
      BigDecimal lExpectedCycleUsage = new BigDecimal( 11 );
      assertEquals(
            "The subsequent historical flight's hour usage for aircraft was not incremented by the usage accrued in the completion of the planned flight.",
            lExpectedHourUsage,
            readInventoryUsageForFlight( lFlight, lAircraft, DataTypeKey.HOURS ) );
      assertEquals(
            "The subsequent historical flight's cycle usage for aircraft was not incremented by the usage accrued in the completion of the planned flight.",
            lExpectedCycleUsage,
            readInventoryUsageForFlight( lFlight, lAircraft, DataTypeKey.CYCLES ) );
      assertEquals(
            "The subsequent historical flight's hour usage for engine was not incremented by the usage accrued in the completion of the planned flight.",
            lExpectedHourUsage,
            readInventoryUsageForFlight( lFlight, lEngine, DataTypeKey.HOURS ) );
      assertEquals(
            "The subsequent historical flight's cycle usage for engine was not incremented by the usage accrued in the completion of the planned flight.",
            lExpectedCycleUsage,
            readInventoryUsageForFlight( lFlight, lEngine, DataTypeKey.CYCLES ) );

   }


   private LocationKey createLocation( final String aLocationCode ) {
      return Domain.createLocation( new DomainConfiguration<Location>() {

         @Override
         public void configure( Location aLocation ) {
            aLocation.setCode( aLocationCode );
            aLocation.setType( RefLocTypeKey.AIRPORT );

         }
      } );
   }


   private BigDecimal readInventoryUsageForFlight( FlightLegId lFlight, InventoryKey aInventory,
         DataTypeKey aDataType ) {

      FlightLegDao lFlightLegDao = new JdbcFlightLegDao();
      UsageAdjustmentId lUsageAdjustment = lFlightLegDao.findById( lFlight ).getUsageRecord();
      UsageDataDao lUsageDataDao = new JdbcUsageDataDao();
      UsageDataEntity lUsageData =
            lUsageDataDao.findByNaturalKey( lUsageAdjustment, aInventory, aDataType );
      return lUsageData.getTsn();
   }


   private HumanResourceKey initHumanResource() {

      final HumanResourceKey lUser = new HumanResourceDomainBuilder().build();
      final int lUserId = OrgHr.findByPrimaryKey( lUser ).getUserId();
      final UserParametersStub lUserParams = new UserParametersStub( lUserId, "LOGIC" );
      lUserParams.setBoolean( "ALLOW_COMPLETE_FUTURE_FLIGHT", false );
      UserParameters.setInstance( lUserId, "LOGIC", lUserParams );
      return lUser;
   }


   private FlightInformationTO generateFlightInfo( String aFlightName, Date aArrivalDate,
         Boolean aMoveAircraftToArrivalLocation ) {

      return new FlightInformationTO( FLIGHT_NAME, null, null, null, null, null, null, null, null,
            null, null, null, ARRIVAL_DATE, null, null, aMoveAircraftToArrivalLocation );

   }


   private AssemblyKey createAircraftAssembly() {
      return Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

         @Override
         public void configure( AircraftAssembly aAssembly ) {
            aAssembly.addUsageDefinitionConfiguration( new DomainConfiguration<UsageDefinition>() {

               @Override
               public void configure( UsageDefinition aBuilder ) {
                  aBuilder.addUsageParameter( DataTypeKey.HOURS );
                  aBuilder.addUsageParameter( DataTypeKey.CYCLES );
               }
            } );
         }
      } );
   }
}
