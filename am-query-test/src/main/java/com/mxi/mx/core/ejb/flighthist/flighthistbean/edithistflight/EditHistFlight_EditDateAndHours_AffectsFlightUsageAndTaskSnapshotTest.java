package com.mxi.mx.core.ejb.flighthist.flighthistbean.edithistflight;

import static com.mxi.mx.core.key.DataTypeKey.HOURS;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Flight;
import com.mxi.am.domain.Location;
import com.mxi.am.domain.Requirement;
import com.mxi.am.domain.WorkPackage;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.servlet.SessionContextFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.unittest.security.SecurityIdentificationStub;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.ejb.flighthist.CurrentUsages;
import com.mxi.mx.core.ejb.flighthist.FlightHistBean;
import com.mxi.mx.core.flight.dao.FlightLegDao;
import com.mxi.mx.core.flight.model.FlightLegId;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.EventInventoryKey;
import com.mxi.mx.core.key.EventInventoryUsageKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefDataSourceKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefUsgSnapshotSrcTypeKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.UsageDefinitionKey;
import com.mxi.mx.core.key.UsageParmKey;
import com.mxi.mx.core.services.event.inventory.UsageSnapshot;
import com.mxi.mx.core.services.event.usage.CollectedUsageParm;
import com.mxi.mx.core.services.flighthist.FlightInformationTO;
import com.mxi.mx.core.services.flighthist.Measurement;
import com.mxi.mx.core.table.eqp.EqpDataSourceSpec;
import com.mxi.mx.core.table.evt.EvtInvTable;
import com.mxi.mx.core.table.evt.EvtInvUsageTable;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.org.OrgHr;
import com.mxi.mx.core.usage.dao.UsageDataDao;
import com.mxi.mx.core.usage.model.UsageAdjustmentId;


/**
 * Tests for {@linkplain FlightHistBean#editHistFlight} that involve updating the usage deltas of a
 * historical flight and their effects on the aircraft and all sub-components installed during that
 * flight.
 *
 */
public class EditHistFlight_EditDateAndHours_AffectsFlightUsageAndTaskSnapshotTest {

   // TSN before any flights
   private static final BigDecimal TSN_BEFORE_FLIGHTS = new BigDecimal( 500 );

   private static final BigDecimal FLIGHT1_DELTA = new BigDecimal( 10 );
   private static final BigDecimal FLIGHT2_DELTA = new BigDecimal( 2 );
   private static final BigDecimal FLIGHT3_DELTA = new BigDecimal( 5 );

   // Not relevant to tests.
   private static final String HR_USERNAME = "HR_USERNAME";
   private static final Measurement[] NO_MEASUREMENTS = new Measurement[] {};
   private static final Date FLIGHT1_DATE = DateUtils.addDays( new Date(), -10 );
   private static final Date WORKPACKAGE_DATE = DateUtils.addDays( new Date(), -8 );
   private static final Date FLIGHT2_DATE = DateUtils.addDays( new Date(), -6 );
   private static final Date FLIGHT3_DATE = DateUtils.addDays( new Date(), -4 );
   private static final Date TASK_DATE = DateUtils.addDays( new Date(), -2 );

   // The bean under test.
   private FlightHistBean flightHistBean;
   private HumanResourceKey hrKey;

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule fakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * <pre>
    * Given an aircraft only has Flight1, Flight2 happened in sequence
    * When Flight2 date is added by 1 day
    * And flying hours is added by 1 hour (Flight2 is still the latest flight)
    * Then current usage of the aircraft is added by 1 hour
    * And Flight1 flying hour is not changed.
    * </pre>
    */
   @Test
   public void itAdjustsCurrentUsageWhenLatestFlightDateAndHoursAreAdjusted() throws Exception {

      final BigDecimal aircraftHoursAfterFlight1 = TSN_BEFORE_FLIGHTS.add( FLIGHT1_DELTA );

      final BigDecimal aircraftHoursAfterFlight2 = aircraftHoursAfterFlight1.add( FLIGHT2_DELTA );

      final BigDecimal updatedFlight2Delta = FLIGHT2_DELTA.add( BigDecimal.ONE );

      final Date updatedFlight2Date = DateUtils.addDays( FLIGHT2_DATE, 1 );

      final InventoryKey aircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft acft ) {

            acft.addUsage( HOURS, aircraftHoursAfterFlight2 );
         }
      } );

      FlightLegId flight1 = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight flight ) {
            flight.setAircraft( aircraft );
            flight.setHistorical( true );
            flight.setArrivalDate( FLIGHT1_DATE );
            flight.addUsage( aircraft, HOURS, FLIGHT1_DELTA, aircraftHoursAfterFlight1 );
         }
      } );

      FlightLegId flight2 = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight flight ) {
            flight.setAircraft( aircraft );
            flight.setHistorical( true );
            flight.setArrivalDate( FLIGHT2_DATE );
            flight.addUsage( aircraft, HOURS, FLIGHT2_DELTA, aircraftHoursAfterFlight2 );
         }
      } );

      CollectedUsageParm[] editUsageParms =
            { generateFlightUsage( aircraft, HOURS, updatedFlight2Delta ) };

      FlightInformationTO flightTo =
            generateFlightInfoTO( "FLIGHT_NAME", updatedFlight2Date, updatedFlight2Delta );

      flightHistBean.editHistFlight( flight2, hrKey, flightTo, editUsageParms, NO_MEASUREMENTS );

      // Assert current usage has been updated with the edited delta
      BigDecimal flight2DiffHours = updatedFlight2Delta.subtract( FLIGHT2_DELTA );
      final BigDecimal expectedAircraftHoursAfterEdit =
            aircraftHoursAfterFlight2.add( flight2DiffHours );
      assertCurrentUsage( aircraft, HOURS, expectedAircraftHoursAfterEdit );

      // check that Flight1 hours not changed
      FlightLegDao flightLegDao = InjectorContainer.get().getInstance( FlightLegDao.class );
      UsageAdjustmentId usageId = flightLegDao.findById( flight1 ).getUsageRecord();
      UsageDataDao usageDataDao = InjectorContainer.get().getInstance( UsageDataDao.class );
      BigDecimal flight1HoursTsn =
            usageDataDao.findByNaturalKey( usageId, aircraft, HOURS ).getTsn();
      assertEquals( "Unexpected update of flight 1 usage snapshot", aircraftHoursAfterFlight1,
            flight1HoursTsn );
   }


   /**
    * <pre>
    * Test: Test that the usage of a work packaged is updated after the flight is updated
    * Given an aircraft only has Flight1, WorkPackage1, Flight2 happened in sequence.
    * When date of Flight2 is moved to be earlier before Flight1
    * And flying hours added by 1 (Flight2 is no longer the latest flight)
    * Then Flight1 and WorkPackage1 are updated properly.
    * </pre>
    */
   @Test
   public void itUpdatesFlightAndWorkPackageWhenLatestFlightIsChangedToFirstFlight()
         throws Exception {

      final BigDecimal aircraftHoursAfterFlight1 = TSN_BEFORE_FLIGHTS.add( FLIGHT1_DELTA );

      final BigDecimal aircraftHoursAfterFlight2 = aircraftHoursAfterFlight1.add( FLIGHT2_DELTA );

      final BigDecimal updatedFlight2Delta = FLIGHT2_DELTA.add( BigDecimal.ONE );

      final Date updatedFlight2Date = DateUtils.addDays( FLIGHT1_DATE, -1 );

      final InventoryKey aircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft acft ) {
            acft.addUsage( HOURS, aircraftHoursAfterFlight2 );
         }
      } );

      FlightLegId flight1 = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight flight ) {
            flight.setAircraft( aircraft );
            flight.setHistorical( true );
            flight.setArrivalDate( FLIGHT1_DATE );
            flight.addUsage( aircraft, HOURS, FLIGHT1_DELTA, aircraftHoursAfterFlight1 );
         }
      } );

      UsageSnapshot workpackageUsageSnapshot =
            new UsageSnapshot( aircraft, HOURS, aircraftHoursAfterFlight1 );
      workpackageUsageSnapshot.withSnapshotSourceType( RefUsgSnapshotSrcTypeKey.MAINTENIX );
      final TaskKey workPackage = Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage wp ) {
            wp.setActualStartDate( WORKPACKAGE_DATE );
            wp.setAircraft( aircraft );
            wp.setStatus( RefEventStatusKey.COMPLETE );
            wp.addUsageSnapshot( workpackageUsageSnapshot );
         }

      } );

      FlightLegId flight2 = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight flight ) {
            flight.setAircraft( aircraft );
            flight.setHistorical( true );
            flight.setArrivalDate( FLIGHT2_DATE );
            flight.addUsage( aircraft, HOURS, FLIGHT2_DELTA, aircraftHoursAfterFlight2 );
         }
      } );

      CollectedUsageParm[] editUsageParms =
            { generateFlightUsage( aircraft, HOURS, updatedFlight2Delta ) };

      FlightInformationTO flightTo =
            generateFlightInfoTO( "FLIGHT_NAME 2", updatedFlight2Date, updatedFlight2Delta );

      flightHistBean.editHistFlight( flight2, hrKey, flightTo, editUsageParms, NO_MEASUREMENTS );

      // Assert current usage has been updated with the edited delta
      BigDecimal flight2DiffHours = updatedFlight2Delta.subtract( FLIGHT2_DELTA );
      final BigDecimal expectedAircraftHoursAfterEdit =
            aircraftHoursAfterFlight2.add( flight2DiffHours );
      assertCurrentUsage( aircraft, HOURS, expectedAircraftHoursAfterEdit );

      // check that workpackage1 hours updated
      EvtInvTable evtInvRecord =
            EvtInvTable.findByEventAndInventory( workPackage.getEventKey(), aircraft );
      EvtInvUsageTable evtInvUsageTable = EvtInvUsageTable
            .findByPrimaryKey( new EventInventoryUsageKey( evtInvRecord.getPk(), HOURS ) );
      Double actualHoursWPUsage = evtInvUsageTable.getTsnQt();
      Double expectedHoursWPUsage =
            aircraftHoursAfterFlight1.add( updatedFlight2Delta ).doubleValue();
      assertEquals( "Unexpected update of workpackage usage snapshot", expectedHoursWPUsage,
            actualHoursWPUsage );

      // check flight1
      FlightLegDao flightLegDao = InjectorContainer.get().getInstance( FlightLegDao.class );
      UsageDataDao usageDataDao = InjectorContainer.get().getInstance( UsageDataDao.class );

      UsageAdjustmentId usageId = flightLegDao.findById( flight1 ).getUsageRecord();
      BigDecimal flight1HoursTsn =
            usageDataDao.findByNaturalKey( usageId, aircraft, HOURS ).getTsn();
      BigDecimal expectedFlight1HoursTsn = aircraftHoursAfterFlight1.add( updatedFlight2Delta );
      assertEquals( "Unexpected update of flight 1 usage snapshot", expectedFlight1HoursTsn,
            flight1HoursTsn );

   }


   /**
    *
    * <pre>
    * Given an aircraft only has Flight1, WorkPackage1, Flight2 happened in sequence.
    * When date of Flight1 is moved to be after Flight2 (Flight1 becomes the latest flight)
    * Then Flight2 and WorkPackage1 are updated properly.
    * </pre>
    *
    */
   @Test
   public void itAdjustsFlightAndWorkPackageWhenFirstFlightBecomesLatestFlight() throws Exception {

      final BigDecimal aircraftHoursAfterFlight1 = TSN_BEFORE_FLIGHTS.add( FLIGHT1_DELTA );

      final BigDecimal aircraftHoursAfterFlight2 = aircraftHoursAfterFlight1.add( FLIGHT2_DELTA );

      final Date updatedFlight1Date = DateUtils.addDays( FLIGHT2_DATE, 1 );

      final InventoryKey aircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft acft ) {

            acft.addUsage( HOURS, aircraftHoursAfterFlight2 );

         }
      } );

      FlightLegId flight1 = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight flight ) {
            flight.setAircraft( aircraft );
            flight.setHistorical( true );
            flight.setArrivalDate( FLIGHT1_DATE );
            flight.addUsage( aircraft, HOURS, FLIGHT1_DELTA, aircraftHoursAfterFlight1 );
         }
      } );

      // setup work package
      UsageSnapshot workpackageUsageSnapshot =
            new UsageSnapshot( aircraft, HOURS, aircraftHoursAfterFlight1 );
      workpackageUsageSnapshot.withSnapshotSourceType( RefUsgSnapshotSrcTypeKey.MAINTENIX );
      final TaskKey workpackage = Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage wp ) {
            wp.setActualStartDate( WORKPACKAGE_DATE );
            wp.setAircraft( aircraft );
            wp.setStatus( RefEventStatusKey.COMPLETE );
            wp.addUsageSnapshot( workpackageUsageSnapshot );
         }

      } );

      FlightLegId flight2 = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight flight ) {
            flight.setAircraft( aircraft );
            flight.setHistorical( true );
            flight.setArrivalDate( FLIGHT2_DATE );
            flight.addUsage( aircraft, HOURS, FLIGHT2_DELTA, aircraftHoursAfterFlight2 );
         }
      } );

      CollectedUsageParm[] editUsageParms =
            { generateFlightUsage( aircraft, HOURS, FLIGHT1_DELTA ) };

      FlightInformationTO flightTo =
            generateFlightInfoTO( "FLIGHT_NAME 1", updatedFlight1Date, FLIGHT1_DELTA );

      flightHistBean.editHistFlight( flight1, hrKey, flightTo, editUsageParms, NO_MEASUREMENTS );

      // Assert current usage has been updated with the edited delta
      final BigDecimal expectedAircraftHoursAfterEdit = aircraftHoursAfterFlight2;
      assertCurrentUsage( aircraft, HOURS, expectedAircraftHoursAfterEdit );

      // check flights
      FlightLegDao flightLegDao = InjectorContainer.get().getInstance( FlightLegDao.class );
      UsageDataDao usageDataDao = InjectorContainer.get().getInstance( UsageDataDao.class );

      // check that flight2 hours updated
      UsageAdjustmentId usageId = flightLegDao.findById( flight2 ).getUsageRecord();
      BigDecimal lFlightHoursTsn =
            usageDataDao.findByNaturalKey( usageId, aircraft, HOURS ).getTsn();
      BigDecimal expectedFlight2HoursTsn = TSN_BEFORE_FLIGHTS.add( FLIGHT2_DELTA );
      assertEquals( "Unexpected update of flight 2 usage snapshot", expectedFlight2HoursTsn,
            lFlightHoursTsn );

      // check that workpackage1 hours updated
      EvtInvTable evtInvRecord =
            EvtInvTable.findByEventAndInventory( workpackage.getEventKey(), aircraft );
      EvtInvUsageTable evtInvUsageTable = EvtInvUsageTable
            .findByPrimaryKey( new EventInventoryUsageKey( evtInvRecord.getPk(), HOURS ) );
      Double actualHoursWPUsage = evtInvUsageTable.getTsnQt();
      Double expectedHoursWPUsage = TSN_BEFORE_FLIGHTS.doubleValue();
      assertEquals( "Unexpected update of workpackage usage snapshot", expectedHoursWPUsage,
            actualHoursWPUsage );

   }


   /**
    * <pre>
    * Given an aircraft only has Flight1, Flight2, Flight3, Task1 happened in sequence.
    * When Flight2 date is modified to earlier than Flight1, and flying hours added by 1
    * Then Flight1, Task1 and current usage of the aircraft are updated properly.
    * </pre>
    */
   @Test
   public void itUpdatesUsageWhenFlightDateAndUsageModified() throws Exception {

      final BigDecimal aircraftHoursAfterFlight1 = TSN_BEFORE_FLIGHTS.add( FLIGHT1_DELTA );

      final BigDecimal aircraftHoursAfterFlight2 = aircraftHoursAfterFlight1.add( FLIGHT2_DELTA );

      final BigDecimal aircraftHoursAfterFlight3 = aircraftHoursAfterFlight2.add( FLIGHT3_DELTA );

      final Date updatedFlight2Date = DateUtils.addDays( FLIGHT1_DATE, -1 );

      final BigDecimal updatedFlight2Delta = FLIGHT2_DELTA.add( BigDecimal.ONE );

      final InventoryKey aircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft acft ) {

            acft.addUsage( HOURS, aircraftHoursAfterFlight3 );

         }
      } );

      FlightLegId flight1 = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight flight ) {
            flight.setAircraft( aircraft );
            flight.setHistorical( true );
            flight.setArrivalDate( FLIGHT1_DATE );
            flight.addUsage( aircraft, HOURS, FLIGHT1_DELTA, aircraftHoursAfterFlight1 );
         }
      } );

      FlightLegId flight2 = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight flight ) {
            flight.setAircraft( aircraft );
            flight.setHistorical( true );
            flight.setArrivalDate( FLIGHT2_DATE );
            flight.addUsage( aircraft, HOURS, FLIGHT2_DELTA, aircraftHoursAfterFlight2 );
         }
      } );

      FlightLegId flight3 = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight flight ) {
            flight.setAircraft( aircraft );
            flight.setHistorical( true );
            flight.setArrivalDate( FLIGHT3_DATE );
            flight.addUsage( aircraft, HOURS, FLIGHT3_DELTA, aircraftHoursAfterFlight3 );
         }
      } );

      TaskKey task = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement req ) {
            req.setInventory( aircraft );
            req.setActualEndDate( TASK_DATE );
            req.setStatus( RefEventStatusKey.COMPLETE );
            req.addUsage( new UsageSnapshot( aircraft, HOURS, aircraftHoursAfterFlight3 ) );
         }
      } );

      CollectedUsageParm[] editUsageParms =
            { generateFlightUsage( aircraft, HOURS, updatedFlight2Delta ) };

      FlightInformationTO flightTo =
            generateFlightInfoTO( "FLIGHT_NAME 2", updatedFlight2Date, updatedFlight2Delta );

      flightHistBean.editHistFlight( flight2, hrKey, flightTo, editUsageParms, NO_MEASUREMENTS );

      /* Check Flight 1 */
      FlightLegDao flightLegDao = InjectorContainer.get().getInstance( FlightLegDao.class );
      UsageAdjustmentId usageId = flightLegDao.findById( flight1 ).getUsageRecord();
      UsageDataDao usageDataDao = InjectorContainer.get().getInstance( UsageDataDao.class );
      BigDecimal actualFlight1UsageTsn =
            usageDataDao.findByNaturalKey( usageId, aircraft, HOURS ).getTsn();

      BigDecimal expectedFlightUsageTsn =
            TSN_BEFORE_FLIGHTS.add( FLIGHT1_DELTA ).add( FLIGHT2_DELTA ).add( BigDecimal.ONE );

      assertEquals( "Unexpected Flight 1 usage value.", expectedFlightUsageTsn,
            actualFlight1UsageTsn );

      /* Check Task 1 */
      EventKey eventKey = task.getEventKey();
      EventInventoryKey eventInventoryKey = EvtInvTable.findMainByEventKey( eventKey ).getPk();
      BigDecimal actualTaskUsageTsn = new BigDecimal( EvtInvUsageTable
            .findByPrimaryKey( new EventInventoryUsageKey( eventInventoryKey, HOURS ) )
            .getTsnQt() );

      BigDecimal expectedUsageTsn =
            TSN_BEFORE_FLIGHTS.add( FLIGHT1_DELTA ).add( updatedFlight2Delta ).add( FLIGHT3_DELTA );

      assertEquals( "Unexpected Task usage value.", expectedUsageTsn, actualTaskUsageTsn );

      /* Check Current Usage */
      assertCurrentUsage( aircraft, HOURS, expectedUsageTsn );

   }


   /**
    * <pre>
    * Given an aircraft only has Flight1, Flight2, Flight3, Task1 happened in sequence.
    * When Flight2 date is modified to later than Flight3 but earlier than Task1
    * Then Flight3 is updated properly.
    * And Task1 is not changed.
    * </pre>
    */

   @Test
   public void itUpdatesUsageWhenFlightDateModified() throws Exception {

      final BigDecimal aircraftHoursAfterFlight1 = TSN_BEFORE_FLIGHTS.add( FLIGHT1_DELTA );

      final BigDecimal aircraftHoursAfterFlight2 = aircraftHoursAfterFlight1.add( FLIGHT2_DELTA );

      final BigDecimal aircraftHoursAfterFlight3 = aircraftHoursAfterFlight2.add( FLIGHT3_DELTA );

      final Date updatedFlight2Date = DateUtils.addDays( FLIGHT3_DATE, 1 );

      final InventoryKey aircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft acft ) {

            acft.addUsage( HOURS, aircraftHoursAfterFlight3 );

         }
      } );

      FlightLegId flight1 = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight flight ) {
            flight.setAircraft( aircraft );
            flight.setHistorical( true );
            flight.setArrivalDate( FLIGHT1_DATE );
            flight.addUsage( aircraft, HOURS, FLIGHT1_DELTA, aircraftHoursAfterFlight1 );
         }
      } );

      FlightLegId flight2 = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight flight ) {
            flight.setAircraft( aircraft );
            flight.setHistorical( true );
            flight.setArrivalDate( FLIGHT2_DATE );
            flight.addUsage( aircraft, HOURS, FLIGHT2_DELTA, aircraftHoursAfterFlight2 );
         }
      } );

      FlightLegId flight3 = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight flight ) {
            flight.setAircraft( aircraft );
            flight.setHistorical( true );
            flight.setArrivalDate( FLIGHT3_DATE );
            flight.addUsage( aircraft, HOURS, FLIGHT3_DELTA, aircraftHoursAfterFlight3 );
         }
      } );

      TaskKey task = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement req ) {
            req.setInventory( aircraft );
            req.setStatus( RefEventStatusKey.COMPLETE );
            req.setActualEndDate( TASK_DATE );
            req.addUsage( new UsageSnapshot( aircraft, HOURS, aircraftHoursAfterFlight3 ) );
         }
      } );

      CollectedUsageParm[] editUsageParms =
            { generateFlightUsage( aircraft, HOURS, FLIGHT2_DELTA ) };

      FlightInformationTO flightTo =
            generateFlightInfoTO( "FLIGHT_NAME 2", updatedFlight2Date, FLIGHT2_DELTA );

      flightHistBean.editHistFlight( flight2, hrKey, flightTo, editUsageParms, NO_MEASUREMENTS );

      FlightLegDao flightLegDao = InjectorContainer.get().getInstance( FlightLegDao.class );
      UsageAdjustmentId usageId = flightLegDao.findById( flight3 ).getUsageRecord();
      UsageDataDao usageDataDao = InjectorContainer.get().getInstance( UsageDataDao.class );
      BigDecimal actualFlight3UsageTsn =
            usageDataDao.findByNaturalKey( usageId, aircraft, HOURS ).getTsn();

      BigDecimal expectedFlightUsageTsn =
            TSN_BEFORE_FLIGHTS.add( FLIGHT1_DELTA ).add( FLIGHT3_DELTA );

      assertEquals( "Unexpected Flight 3 usage value.", expectedFlightUsageTsn,
            actualFlight3UsageTsn );

      BigDecimal expectedTaskUsageTsn =
            TSN_BEFORE_FLIGHTS.add( FLIGHT1_DELTA ).add( FLIGHT2_DELTA ).add( FLIGHT3_DELTA );

      EventKey eventKey = task.getEventKey();
      EventInventoryKey eventInventoryKey = EvtInvTable.findMainByEventKey( eventKey ).getPk();
      BigDecimal actualTaskUsageTsn = new BigDecimal( EvtInvUsageTable
            .findByPrimaryKey( new EventInventoryUsageKey( eventInventoryKey, HOURS ) )
            .getTsnQt() );

      assertEquals( "Unexpected Task usage value.", expectedTaskUsageTsn, actualTaskUsageTsn );

   }


   /**
    *
    * <pre>
    * Given an aircraft only has Flight1, WorkPackage1, Flight2 happened in sequence.
    * When Flight2 date is modified to later than Flight1 but earlier than WorkPackage1
    * Then WorkPackage1 is updated properly
    * </pre>
    *
    */
   @Test
   public void itAdjustsWorkPackageWhenLatestFlightBecomesMiddleFlight() throws Exception {

      final BigDecimal aircraftHoursAfterFlight1 = TSN_BEFORE_FLIGHTS.add( FLIGHT1_DELTA );

      final BigDecimal aircraftHoursAfterFlight2 = aircraftHoursAfterFlight1.add( FLIGHT2_DELTA );

      final Date updatedFlight2Date = DateUtils.addDays( FLIGHT1_DATE, 1 );

      final InventoryKey aircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft acft ) {
            acft.addUsage( HOURS, aircraftHoursAfterFlight2 );
         }
      } );

      FlightLegId flight1 = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight flight ) {
            flight.setAircraft( aircraft );
            flight.setHistorical( true );
            flight.setArrivalDate( FLIGHT1_DATE );
            flight.addUsage( aircraft, HOURS, FLIGHT1_DELTA, aircraftHoursAfterFlight1 );
         }
      } );

      // setup work package
      UsageSnapshot workpackageUsageSnapshot =
            new UsageSnapshot( aircraft, HOURS, aircraftHoursAfterFlight1 );
      workpackageUsageSnapshot.withSnapshotSourceType( RefUsgSnapshotSrcTypeKey.MAINTENIX );
      final TaskKey workpackage = Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage wp ) {
            wp.setActualStartDate( WORKPACKAGE_DATE );
            wp.setAircraft( aircraft );
            wp.setStatus( RefEventStatusKey.COMPLETE );
            wp.addUsageSnapshot( workpackageUsageSnapshot );
         }

      } );

      FlightLegId flight2 = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight flight ) {
            flight.setAircraft( aircraft );
            flight.setHistorical( true );
            flight.setArrivalDate( FLIGHT2_DATE );
            flight.addUsage( aircraft, HOURS, FLIGHT2_DELTA, aircraftHoursAfterFlight2 );
         }
      } );

      CollectedUsageParm[] editUsageParms =
            { generateFlightUsage( aircraft, HOURS, FLIGHT2_DELTA ) };

      FlightInformationTO flightTo =
            generateFlightInfoTO( "FLIGHT_NAME 2", updatedFlight2Date, FLIGHT2_DELTA );

      flightHistBean.editHistFlight( flight2, hrKey, flightTo, editUsageParms, NO_MEASUREMENTS );

      // check that workpackage1 hours updated
      EvtInvTable evtInvRecord =
            EvtInvTable.findByEventAndInventory( workpackage.getEventKey(), aircraft );
      EvtInvUsageTable evtInvUsageTable = EvtInvUsageTable
            .findByPrimaryKey( new EventInventoryUsageKey( evtInvRecord.getPk(), HOURS ) );
      Double actualHoursWPUsage = evtInvUsageTable.getTsnQt();
      Double expectedHoursWPUsage = aircraftHoursAfterFlight1.add( FLIGHT2_DELTA ).doubleValue();
      assertEquals( "Unexpected update of workpackage usage snapshot", expectedHoursWPUsage,
            actualHoursWPUsage );

   }


   /**
    *
    * <pre>
    * Given an aircraft only has Flight1, WorkPackage1, Flight2 happened in sequence.
    * When Flight1 date is modified to later than WorkPackage1 but earlier than Flight2
    * Then WorkPackage1 is updated properly
    * </pre>
    *
    */
   @Test
   public void itAdjustsCurrentUsageOfFlightAndWorkPackageWhenFirstFlightBecomesMiddleFlight()
         throws Exception {

      final BigDecimal aircraftHoursAfterFlight1 = TSN_BEFORE_FLIGHTS.add( FLIGHT1_DELTA );

      final BigDecimal aircraftHoursAfterFlight2 = aircraftHoursAfterFlight1.add( FLIGHT2_DELTA );

      final Date updatedFlight1Date = DateUtils.addDays( WORKPACKAGE_DATE, 1 );

      final InventoryKey aircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft acft ) {

            acft.addUsage( HOURS, aircraftHoursAfterFlight2 );
         }
      } );

      FlightLegId flight1 = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight flight ) {
            flight.setAircraft( aircraft );
            flight.setHistorical( true );
            flight.setArrivalDate( FLIGHT1_DATE );
            flight.addUsage( aircraft, HOURS, FLIGHT1_DELTA, aircraftHoursAfterFlight1 );
         }
      } );

      // setup work package
      UsageSnapshot workpackageUsageSnapshot =
            new UsageSnapshot( aircraft, HOURS, aircraftHoursAfterFlight1 );
      workpackageUsageSnapshot.withSnapshotSourceType( RefUsgSnapshotSrcTypeKey.MAINTENIX );
      final TaskKey workpackage = Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage wp ) {
            wp.setActualStartDate( WORKPACKAGE_DATE );
            wp.setAircraft( aircraft );
            wp.setStatus( RefEventStatusKey.COMPLETE );
            wp.addUsageSnapshot( workpackageUsageSnapshot );
         }

      } );

      FlightLegId flight2 = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight flight ) {
            flight.setAircraft( aircraft );
            flight.setHistorical( true );
            flight.setArrivalDate( FLIGHT2_DATE );
            flight.addUsage( aircraft, HOURS, FLIGHT2_DELTA, aircraftHoursAfterFlight2 );
         }
      } );

      CollectedUsageParm[] editUsageParms =
            { generateFlightUsage( aircraft, HOURS, FLIGHT1_DELTA ) };

      FlightInformationTO flightTo =
            generateFlightInfoTO( "FLIGHT_NAME 1", updatedFlight1Date, FLIGHT1_DELTA );

      flightHistBean.editHistFlight( flight1, hrKey, flightTo, editUsageParms, NO_MEASUREMENTS );

      // check that workpackage1 hours updated
      EvtInvTable evtInvRecord =
            EvtInvTable.findByEventAndInventory( workpackage.getEventKey(), aircraft );
      EvtInvUsageTable evtInvUsageTable = EvtInvUsageTable
            .findByPrimaryKey( new EventInventoryUsageKey( evtInvRecord.getPk(), HOURS ) );
      Double actualHoursWPUsage = evtInvUsageTable.getTsnQt();
      Double expectedHoursWPUsage = TSN_BEFORE_FLIGHTS.doubleValue();
      assertEquals( "Unexpected update of workpackage usage snapshot", expectedHoursWPUsage,
            actualHoursWPUsage );

   }


   /**
    * <pre>
    * Given an aircraft only has Flight1, Flight2, Task1 happened in sequence.
    * When Flight2 date is modified to be happened later but still earlier than Task1
    * Then Task1 usage snapshot is not changed
    * </pre>
    */
   @Test
   public void itDoesNotUpdateTaskUsageSnapshot() throws Exception {

      final BigDecimal aircraftHoursAfterFlight1 = TSN_BEFORE_FLIGHTS.add( FLIGHT1_DELTA );

      final BigDecimal aircraftHoursAfterFlight2 = aircraftHoursAfterFlight1.add( FLIGHT2_DELTA );

      final Date updatedFlight2Date = DateUtils.addDays( FLIGHT2_DATE, 1 );

      final InventoryKey aircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft acft ) {

            acft.addUsage( HOURS, aircraftHoursAfterFlight2 );
         }
      } );

      FlightLegId flight1 = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight flight ) {
            flight.setAircraft( aircraft );
            flight.setHistorical( true );
            flight.setArrivalDate( FLIGHT1_DATE );
            flight.addUsage( aircraft, HOURS, FLIGHT1_DELTA, aircraftHoursAfterFlight1 );
         }
      } );

      FlightLegId flight2 = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight flight ) {
            flight.setAircraft( aircraft );
            flight.setHistorical( true );
            flight.setArrivalDate( FLIGHT2_DATE );
            flight.addUsage( aircraft, HOURS, FLIGHT2_DELTA, aircraftHoursAfterFlight2 );
         }
      } );

      TaskKey task = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement req ) {
            req.setInventory( aircraft );
            req.setActualEndDate( TASK_DATE );
            req.setStatus( RefEventStatusKey.COMPLETE );
            req.addUsage( new UsageSnapshot( aircraft, HOURS, aircraftHoursAfterFlight2 ) );
         }
      } );

      BigDecimal expectedUsageTsn = TSN_BEFORE_FLIGHTS.add( FLIGHT1_DELTA ).add( FLIGHT2_DELTA );

      CollectedUsageParm[] editUsageParms = {};

      FlightInformationTO flightTo =
            generateFlightInfoTO( "FLIGHT_NAME 2", updatedFlight2Date, FLIGHT2_DELTA );

      flightHistBean.editHistFlight( flight1, hrKey, flightTo, editUsageParms, NO_MEASUREMENTS );

      EventKey eventKey = task.getEventKey();
      EventInventoryKey eventInventoryKey = EvtInvTable.findMainByEventKey( eventKey ).getPk();
      BigDecimal actualUsageTsn = new BigDecimal( EvtInvUsageTable
            .findByPrimaryKey( new EventInventoryUsageKey( eventInventoryKey, HOURS ) )
            .getTsnQt() );

      assertEquals( "Unexpected Task usage value.", expectedUsageTsn, actualUsageTsn );

   }


   @Before
   public void setup() {
      hrKey = new HumanResourceDomainBuilder().withUsername( HR_USERNAME ).build();
      int iUserId = OrgHr.findByPrimaryKey( hrKey ).getUserId();
      SecurityIdentificationUtils.setInstance( new SecurityIdentificationStub( hrKey ) );

      // Set the MAX_FLIGHT_DURATION_VALUE to null
      // to avoid the flight duration validation done by FlightHistBean.editHistFlight()
      UserParametersFake lUserParms = new UserParametersFake( iUserId, "LOGIC" );
      lUserParms.setProperty( "MAX_FLIGHT_DURATION_VALUE", null );
      lUserParms.setProperty( "MAX_FLIGHT_DAYS_IN_THE_PAST_VALUE", "101" );
      UserParameters.setInstance( iUserId, "LOGIC", lUserParms );

      flightHistBean = new FlightHistBean();
      flightHistBean.ejbCreate();
      flightHistBean.setSessionContext( new SessionContextFake() );
   }


   @After
   public void tearDown() {
      int lUserId = OrgHr.findByPrimaryKey( hrKey ).getUserId();
      UserParameters.setInstance( lUserId, "LOGIC", null );
   }


   private FlightInformationTO generateFlightInfoTO( String aName, Date aArrivalDate,
         BigDecimal aHours ) {

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

      Date lDepartureDate =
            DateUtils.addHours( aArrivalDate, aHours.multiply( new BigDecimal( -1 ) ).intValue() );

      FlightInformationTO to = new FlightInformationTO( aName, null, null, null, null, null,
            lDepartureAirport, lArrivalAirport, null, null, null, null, lDepartureDate,
            aArrivalDate, null, null, false, false );
      to.setArrivalDateUpdated( true );

      return to;
   }


   private CollectedUsageParm generateFlightUsage( InventoryKey aInventory, DataTypeKey lDataType,
         BigDecimal lDelta ) {

      // Create a usage collection to be returned.
      CollectedUsageParm lUsageParm = new CollectedUsageParm(
            new UsageParmKey( aInventory, lDataType ), lDelta.doubleValue() );

      // Create flight data source specifications.
      EqpDataSourceSpec
            .create( new UsageDefinitionKey( InvInvTable.getAssemblyByInventoryKey( aInventory ),
                  RefDataSourceKey.MXFL ), lDataType );

      return lUsageParm;
   }


   private void assertCurrentUsage( InventoryKey aInventory, DataTypeKey aDataType,
         BigDecimal aExpectedUsage ) {

      CurrentUsages lCurrentUsage = new CurrentUsages( aInventory );
      assertEquals( "Inventory=" + aInventory + " , data type=" + aDataType + " : unexpected TSN",
            aExpectedUsage, lCurrentUsage.getTsn( aDataType ) );
   }

}
