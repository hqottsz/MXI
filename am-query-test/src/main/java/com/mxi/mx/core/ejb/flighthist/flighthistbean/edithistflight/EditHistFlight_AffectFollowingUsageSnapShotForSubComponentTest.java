package com.mxi.mx.core.ejb.flighthist.flighthistbean.edithistflight;

import static com.mxi.mx.core.key.DataTypeKey.CYCLES;
import static com.mxi.mx.core.key.DataTypeKey.HOURS;

import java.math.BigDecimal;
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
import com.mxi.am.domain.Fault;
import com.mxi.am.domain.Flight;
import com.mxi.am.domain.Location;
import com.mxi.am.domain.Part;
import com.mxi.am.domain.Requirement;
import com.mxi.am.domain.SerializedInventory;
import com.mxi.am.domain.TrackedInventory;
import com.mxi.am.domain.WorkPackage;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.servlet.SessionContextFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.ejb.flighthist.FlightHistBean;
import com.mxi.mx.core.flight.model.FlightLegId;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.EventInventoryUsageKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefDataSourceKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.UsageDefinitionKey;
import com.mxi.mx.core.key.UsageParmKey;
import com.mxi.mx.core.services.event.inventory.UsageSnapshot;
import com.mxi.mx.core.services.event.usage.CollectedUsageParm;
import com.mxi.mx.core.services.flighthist.FlightInformationTO;
import com.mxi.mx.core.services.flighthist.Measurement;
import com.mxi.mx.core.services.inventory.InventoryServiceFactory;
import com.mxi.mx.core.services.inventory.config.AttachableInventoryService;
import com.mxi.mx.core.table.eqp.EqpDataSourceSpec;
import com.mxi.mx.core.table.evt.EvtInvTable;
import com.mxi.mx.core.table.evt.EvtInvUsage;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.org.OrgHr;


/**
 * Tests for {@linkplain FlightHistBean#editHistFlight} that involve updating the snapshot usage TSN
 * of TRK / SER inventory involved in the flight.
 *
 */
public class EditHistFlight_AffectFollowingUsageSnapShotForSubComponentTest {

   private static final String HR_USERNAME = "HR_USERNAME";
   private static final String FLIGHT = "FLIGHT";
   private static final Measurement[] NO_MEASUREMENTS = new Measurement[] {};

   // current cycle and hour values
   private static final BigDecimal INITIAL_HOURS = new BigDecimal( 1000 );
   private static final BigDecimal INITIAL_CYCLES = new BigDecimal( 500 );

   // some deltas
   private static final BigDecimal INITIAL_FLIGHT_DELTA = new BigDecimal( 1 );
   private static final BigDecimal DELTA_VALUE_THREE = new BigDecimal( 3 );

   private static final BigDecimal EVENT_HOURS_USAGE = new BigDecimal( 400.0 );
   private static final BigDecimal EVENT_CYCLE_USAGE = new BigDecimal( 350.0 );
   private static final BigDecimal EVENT_ASSEMBLY_HOURS_USAGE = new BigDecimal( 500.0 );
   private static final BigDecimal EVENT_ASSEMBLY_CYCLE_USAGE = new BigDecimal( 450.0 );

   private static final Date FLIGHT_DATE = DateUtils.addDays( new Date(), -2 );

   // the bean under test
   private FlightHistBean iFlightHistBean;

   private HumanResourceKey iHrKey;
   private FlightInformationTO iFlightInfoTO;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * <pre>
    * Given an Aircraft with a Historical Flight,
    * And the Aircraft has a SER Inventory attached to it,
    * And the SER Inventory has one Fault against it that were FOUND AFTER the Historical Flight actual arrival date,
    * When the usage Deltas of the Historical Flight are edited,
    * Then the TSN adjustment in the usage snapshots of the fault should be updated by the change to the Delta.
    * </pre>
    */
   @Test
   public void itUpdatesFollowingSerFaultTsnWhenFlightUsageEdited() throws Exception {

      final InventoryKey lSerializedKey =
            Domain.createSerializedInventory( new DomainConfiguration<SerializedInventory>() {

               @Override
               public void configure( SerializedInventory aSer ) {
                  aSer.addUsage( CYCLES, INITIAL_CYCLES );
                  aSer.addUsage( HOURS, INITIAL_HOURS );
               }
            } );

      final InventoryKey lAircraftInvKey =
            Domain.createAircraft( new DomainConfiguration<Aircraft>() {

               @Override
               public void configure( Aircraft aAircraft ) {
                  aAircraft.addSerialized( lSerializedKey );
                  aAircraft.addUsage( CYCLES, INITIAL_CYCLES );
                  aAircraft.addUsage( HOURS, INITIAL_HOURS );
               }
            } );

      FlightLegId lFlightLegId = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aBuilder ) {
            aBuilder.setName( FLIGHT );
            aBuilder.setAircraft( lAircraftInvKey );
            aBuilder.setArrivalDate( FLIGHT_DATE );
            aBuilder.setHistorical( true );
            aBuilder.addUsage( lAircraftInvKey, HOURS, INITIAL_FLIGHT_DELTA, null );
            aBuilder.addUsage( lAircraftInvKey, CYCLES, INITIAL_FLIGHT_DELTA, null );
         }
      } );

      Date lFaultFoundOnDate = DateUtils.addDays( FLIGHT_DATE, +10 );
      FaultKey lFault = createFaultOfInventory( lSerializedKey, lFaultFoundOnDate );

      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( lAircraftInvKey, CYCLES, DELTA_VALUE_THREE ),
                  generateFlightUsage( lAircraftInvKey, HOURS, DELTA_VALUE_THREE ) };

      iFlightHistBean.editHistFlight( lFlightLegId, iHrKey, iFlightInfoTO, lEditUsageParms,
            NO_MEASUREMENTS );

      BigDecimal lDeltaDiff = DELTA_VALUE_THREE.subtract( INITIAL_FLIGHT_DELTA );

      assertEventTsn( lFault.getEventKey(), lSerializedKey, HOURS,
            EVENT_HOURS_USAGE.add( lDeltaDiff ) );

      assertEventTsn( lFault.getEventKey(), lSerializedKey, CYCLES,
            EVENT_CYCLE_USAGE.add( lDeltaDiff ) );

   }


   /**
    * <pre>
    * Given an Aircraft with a Historical Flight,
    * And the Aircraft has a Trk Inventory attached to it,
    * And the Trk Inventory has one Fault against it that were FOUND AFTER the Historical Flight actual arrival date,
    * The Trk is detached from Aircraft after the flight,
    * When the usage Deltas of the Historical Flight are edited,
    * Then the TSN adjustment in the usage snapshots of the fault should be updated by the change to the Delta.
    * </pre>
    */
   @Test
   public void itUpdatesFollowingFaultTsnOfRemovedTrkWhenFlightUsageEdited() throws Exception {

      final PartNoKey lTrkPart = Domain.createPart( new DomainConfiguration<Part>() {

         @Override
         public void configure( Part aPart ) {
            aPart.setInventoryClass( RefInvClassKey.TRK );
         }

      } );

      final InventoryKey lTrackedKey =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aTrk ) {
                  aTrk.setPartNumber( lTrkPart );
                  aTrk.addUsage( CYCLES, INITIAL_CYCLES );
                  aTrk.addUsage( HOURS, INITIAL_HOURS );
               }
            } );

      final InventoryKey lAircraftInvKey =
            Domain.createAircraft( new DomainConfiguration<Aircraft>() {

               @Override
               public void configure( Aircraft aAircraft ) {
                  aAircraft.addTracked( lTrackedKey );
                  aAircraft.addUsage( CYCLES, INITIAL_CYCLES );
                  aAircraft.addUsage( HOURS, INITIAL_HOURS );
               }
            } );

      FlightLegId lFlightLegId = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aBuilder ) {
            aBuilder.setName( FLIGHT );
            aBuilder.setAircraft( lAircraftInvKey );
            aBuilder.setArrivalDate( FLIGHT_DATE );
            aBuilder.setHistorical( true );
            aBuilder.addUsage( lAircraftInvKey, HOURS, INITIAL_FLIGHT_DELTA, null );
            aBuilder.addUsage( lAircraftInvKey, CYCLES, INITIAL_FLIGHT_DELTA, null );
         }
      } );

      Date lFaultFoundOnDate = DateUtils.addDays( FLIGHT_DATE, +10 );
      FaultKey lFault = createFaultOfInventory( lTrackedKey, lFaultFoundOnDate );

      // detach the tracked
      AttachableInventoryService lService =
            InventoryServiceFactory.getInstance().getAttachableInventoryService( lTrackedKey );
      lService.detachInventory( null, null, iHrKey, true, null, null );

      // edit flight
      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( lAircraftInvKey, CYCLES, DELTA_VALUE_THREE ),
                  generateFlightUsage( lAircraftInvKey, HOURS, DELTA_VALUE_THREE ) };

      iFlightHistBean.editHistFlight( lFlightLegId, iHrKey, iFlightInfoTO, lEditUsageParms,
            NO_MEASUREMENTS );

      BigDecimal lDeltaDiff = DELTA_VALUE_THREE.subtract( INITIAL_FLIGHT_DELTA );

      assertEventTsn( lFault.getEventKey(), lTrackedKey, HOURS,
            EVENT_HOURS_USAGE.add( lDeltaDiff ) );

      assertEventTsn( lFault.getEventKey(), lTrackedKey, CYCLES,
            EVENT_CYCLE_USAGE.add( lDeltaDiff ) );

   }


   /**
    * <pre>
    * Given an Aircraft with a Historical Flight,
    * And the Aircraft has a SER Inventory attached to it,
    * And the SER Inventory has one or more Tasks COMPLETED AFTER the Historical Flight actual arrival date,
    * When the usage Deltas of the Historical Flight are edited,
    * Then the TSN adjustment in the usage snapshots of those Tasks should be updated by the change to the Delta.
    * </pre>
    */
   @Test
   public void itUpdatesFollowingTaskTsnOfSerWhenFlightUsageEdited() throws Exception {

      final InventoryKey lSerializedKey =
            Domain.createSerializedInventory( new DomainConfiguration<SerializedInventory>() {

               @Override
               public void configure( SerializedInventory aSer ) {
                  aSer.addUsage( CYCLES, INITIAL_CYCLES );
                  aSer.addUsage( HOURS, INITIAL_HOURS );
               }
            } );

      final InventoryKey lAircraftInvKey =
            Domain.createAircraft( new DomainConfiguration<Aircraft>() {

               @Override
               public void configure( Aircraft aAircraft ) {
                  aAircraft.addSerialized( lSerializedKey );
                  aAircraft.addUsage( CYCLES, INITIAL_CYCLES );
                  aAircraft.addUsage( HOURS, INITIAL_HOURS );
               }
            } );

      FlightLegId lFlightLegId = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aBuilder ) {
            aBuilder.setName( FLIGHT );
            aBuilder.setAircraft( lAircraftInvKey );
            aBuilder.setArrivalDate( FLIGHT_DATE );
            aBuilder.setHistorical( true );
            aBuilder.addUsage( lAircraftInvKey, HOURS, INITIAL_FLIGHT_DELTA, null );
            aBuilder.addUsage( lAircraftInvKey, CYCLES, INITIAL_FLIGHT_DELTA, null );
         }
      } );

      Date lStartDate = DateUtils.addDays( FLIGHT_DATE, +1 );
      Date lEndDate = DateUtils.addDays( FLIGHT_DATE, +2 );
      TaskKey lActualTaskKey = createTaskOfInventory( lSerializedKey, lStartDate, lEndDate );

      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( lAircraftInvKey, CYCLES, DELTA_VALUE_THREE ),
                  generateFlightUsage( lAircraftInvKey, HOURS, DELTA_VALUE_THREE ) };

      iFlightHistBean.editHistFlight( lFlightLegId, iHrKey, iFlightInfoTO, lEditUsageParms,
            NO_MEASUREMENTS );

      BigDecimal lDeltaDiff = DELTA_VALUE_THREE.subtract( INITIAL_FLIGHT_DELTA );

      assertEventTsn( lActualTaskKey.getEventKey(), lSerializedKey, HOURS,
            EVENT_HOURS_USAGE.add( lDeltaDiff ) );

      assertEventTsn( lActualTaskKey.getEventKey(), lSerializedKey, CYCLES,
            EVENT_CYCLE_USAGE.add( lDeltaDiff ) );

   }


   /**
    * <pre>
    * Given an Aircraft with a Historical Flight,
    * And the Aircraft has a TRK Inventory attached to it,
    * And the TRK Inventory has one or more Tasks COMPLETED AFTER the Historical Flight actual arrival date,
    * The Trk is detached from Aircraft after the flight,
    * When the usage Deltas of the Historical Flight are edited,
    * Then the TSN adjustment in the usage snapshots of those Tasks should be updated by the change to the Delta.
    * </pre>
    */
   @Test
   public void itUpdatesFollowingTaskTsnOfRemovedTrkWhenFlightUsageEdited() throws Exception {

      final PartNoKey lTrkPart = Domain.createPart( new DomainConfiguration<Part>() {

         @Override
         public void configure( Part aPart ) {
            aPart.setInventoryClass( RefInvClassKey.TRK );
         }

      } );

      final InventoryKey lTrackedKey =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aTrk ) {
                  aTrk.setPartNumber( lTrkPart );
                  aTrk.addUsage( CYCLES, INITIAL_CYCLES );
                  aTrk.addUsage( HOURS, INITIAL_HOURS );
               }
            } );

      final InventoryKey lAircraftInvKey =
            Domain.createAircraft( new DomainConfiguration<Aircraft>() {

               @Override
               public void configure( Aircraft aAircraft ) {
                  aAircraft.addTracked( lTrackedKey );
                  aAircraft.addUsage( CYCLES, INITIAL_CYCLES );
                  aAircraft.addUsage( HOURS, INITIAL_HOURS );
               }
            } );

      FlightLegId lFlightLegId = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aBuilder ) {
            aBuilder.setName( FLIGHT );
            aBuilder.setAircraft( lAircraftInvKey );
            aBuilder.setArrivalDate( FLIGHT_DATE );
            aBuilder.setHistorical( true );
            aBuilder.addUsage( lAircraftInvKey, HOURS, INITIAL_FLIGHT_DELTA, null );
            aBuilder.addUsage( lAircraftInvKey, CYCLES, INITIAL_FLIGHT_DELTA, null );
         }
      } );

      Date lStartDate = DateUtils.addDays( FLIGHT_DATE, +1 );
      Date lEndDate = DateUtils.addDays( FLIGHT_DATE, +2 );
      TaskKey lActualTaskKey = createTaskOfInventory( lTrackedKey, lStartDate, lEndDate );

      // detach the tracked
      AttachableInventoryService lService =
            InventoryServiceFactory.getInstance().getAttachableInventoryService( lTrackedKey );
      lService.detachInventory( null, null, iHrKey, true, null, null );

      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( lAircraftInvKey, CYCLES, DELTA_VALUE_THREE ),
                  generateFlightUsage( lAircraftInvKey, HOURS, DELTA_VALUE_THREE ) };

      iFlightHistBean.editHistFlight( lFlightLegId, iHrKey, iFlightInfoTO, lEditUsageParms,
            NO_MEASUREMENTS );

      BigDecimal lDeltaDiff = DELTA_VALUE_THREE.subtract( INITIAL_FLIGHT_DELTA );

      assertEventTsn( lActualTaskKey.getEventKey(), lTrackedKey, HOURS,
            EVENT_HOURS_USAGE.add( lDeltaDiff ) );

      assertEventTsn( lActualTaskKey.getEventKey(), lTrackedKey, CYCLES,
            EVENT_CYCLE_USAGE.add( lDeltaDiff ) );

   }


   /**
    * <pre>
    * Given an Aircraft with a Historical Flight,
    * And the Aircraft has a SER Inventory attached to it,
    * The SER is detached from Aircraft after the flight,
    * And the SER Inventory has one "in work" Work Packages STARTED AFTER the Historical Flight actual arrival date,
    * When the usage Deltas of the Historical Flight are edited,
    * Then the TSN adjustment in the usage snapshots of the Work Package should be updated by the change to the Delta.
    * </pre>
    */
   @Test
   public void itUpdatesFollowingWorkPackageTsnOfSerWhenFlightUsageEdited() throws Exception {

      final PartNoKey lSerPart = Domain.createPart( new DomainConfiguration<Part>() {

         @Override
         public void configure( Part aPart ) {
            aPart.setInventoryClass( RefInvClassKey.SER );
         }

      } );

      final InventoryKey lSerializedKey =
            Domain.createSerializedInventory( new DomainConfiguration<SerializedInventory>() {

               @Override
               public void configure( SerializedInventory aSer ) {
                  aSer.setPartNumber( lSerPart );
                  aSer.addUsage( CYCLES, INITIAL_CYCLES );
                  aSer.addUsage( HOURS, INITIAL_HOURS );
               }
            } );

      final InventoryKey lAircraftInvKey =
            Domain.createAircraft( new DomainConfiguration<Aircraft>() {

               @Override
               public void configure( Aircraft aAircraft ) {
                  aAircraft.addSerialized( lSerializedKey );
                  aAircraft.addUsage( CYCLES, INITIAL_CYCLES );
                  aAircraft.addUsage( HOURS, INITIAL_HOURS );
               }
            } );

      FlightLegId lFlightLegId = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aBuilder ) {
            aBuilder.setName( FLIGHT );
            aBuilder.setAircraft( lAircraftInvKey );
            aBuilder.setArrivalDate( FLIGHT_DATE );
            aBuilder.setHistorical( true );
            aBuilder.addUsage( lAircraftInvKey, HOURS, INITIAL_FLIGHT_DELTA, null );
            aBuilder.addUsage( lAircraftInvKey, CYCLES, INITIAL_FLIGHT_DELTA, null );
         }
      } );

      // detach the SER
      AttachableInventoryService lService =
            InventoryServiceFactory.getInstance().getAttachableInventoryService( lSerializedKey );
      lService.detachInventory( null, null, iHrKey, true, null, null );

      Date lStartDate = DateUtils.addDays( FLIGHT_DATE, +1 );
      Date lEstimatedEndDate = DateUtils.addDays( FLIGHT_DATE, +2 );
      TaskKey lActualTaskKey =
            createInWorkWorkPackageOfInventory( lSerializedKey, lStartDate, lEstimatedEndDate );

      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( lAircraftInvKey, CYCLES, DELTA_VALUE_THREE ),
                  generateFlightUsage( lAircraftInvKey, HOURS, DELTA_VALUE_THREE ) };

      iFlightHistBean.editHistFlight( lFlightLegId, iHrKey, iFlightInfoTO, lEditUsageParms,
            NO_MEASUREMENTS );

      BigDecimal lDeltaDiff = DELTA_VALUE_THREE.subtract( INITIAL_FLIGHT_DELTA );

      assertEventTsn( lActualTaskKey.getEventKey(), lSerializedKey, HOURS,
            EVENT_HOURS_USAGE.add( lDeltaDiff ) );

      assertEventTsn( lActualTaskKey.getEventKey(), lSerializedKey, CYCLES,
            EVENT_CYCLE_USAGE.add( lDeltaDiff ) );

   }


   /**
    * <pre>
    * Given an Aircraft with a Historical Flight,
    * And the Aircraft has a TRK Inventory attached to it,
    * The TRK is detached from Aircraft after the flight,
    * And the TRK Inventory has one "in work" Work Packages STARTED AFTER the Historical Flight actual arrival date,
    * When the usage Deltas of the Historical Flight are edited,
    * Then the TSN adjustment in the usage snapshots of the Work Package should be updated by the change to the Delta.
    * </pre>
    */
   @Test
   public void itUpdatesFollowingWorkPackageTsnOfTrkWhenFlightUsageEdited() throws Exception {

      final PartNoKey lTrkPart = Domain.createPart( new DomainConfiguration<Part>() {

         @Override
         public void configure( Part aPart ) {
            aPart.setInventoryClass( RefInvClassKey.TRK );
         }

      } );

      final InventoryKey lTrackedKey =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aTrk ) {
                  aTrk.setPartNumber( lTrkPart );
                  aTrk.addUsage( CYCLES, INITIAL_CYCLES );
                  aTrk.addUsage( HOURS, INITIAL_HOURS );
               }
            } );

      final InventoryKey lAircraftInvKey =
            Domain.createAircraft( new DomainConfiguration<Aircraft>() {

               @Override
               public void configure( Aircraft aAircraft ) {
                  aAircraft.addTracked( lTrackedKey );
                  aAircraft.addUsage( CYCLES, INITIAL_CYCLES );
                  aAircraft.addUsage( HOURS, INITIAL_HOURS );
               }
            } );

      FlightLegId lFlightLegId = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aBuilder ) {
            aBuilder.setName( FLIGHT );
            aBuilder.setAircraft( lAircraftInvKey );
            aBuilder.setArrivalDate( FLIGHT_DATE );
            aBuilder.setHistorical( true );
            aBuilder.addUsage( lAircraftInvKey, HOURS, INITIAL_FLIGHT_DELTA, null );
            aBuilder.addUsage( lAircraftInvKey, CYCLES, INITIAL_FLIGHT_DELTA, null );
         }
      } );

      // detach the tracked
      AttachableInventoryService lService =
            InventoryServiceFactory.getInstance().getAttachableInventoryService( lTrackedKey );
      lService.detachInventory( null, null, iHrKey, true, null, null );

      Date lStartDate = DateUtils.addDays( FLIGHT_DATE, +1 );
      Date lEstimatedEndDate = DateUtils.addDays( FLIGHT_DATE, +2 );
      TaskKey lActualTaskKey =
            createInWorkWorkPackageOfInventory( lTrackedKey, lStartDate, lEstimatedEndDate );

      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( lAircraftInvKey, CYCLES, DELTA_VALUE_THREE ),
                  generateFlightUsage( lAircraftInvKey, HOURS, DELTA_VALUE_THREE ) };

      iFlightHistBean.editHistFlight( lFlightLegId, iHrKey, iFlightInfoTO, lEditUsageParms,
            NO_MEASUREMENTS );

      BigDecimal lDeltaDiff = DELTA_VALUE_THREE.subtract( INITIAL_FLIGHT_DELTA );

      assertEventTsn( lActualTaskKey.getEventKey(), lTrackedKey, HOURS,
            EVENT_HOURS_USAGE.add( lDeltaDiff ) );

      assertEventTsn( lActualTaskKey.getEventKey(), lTrackedKey, CYCLES,
            EVENT_CYCLE_USAGE.add( lDeltaDiff ) );

   }


   @Before
   public void setup() {
      iHrKey = new HumanResourceDomainBuilder().withUsername( HR_USERNAME ).build();

      int lUserId = OrgHr.findByPrimaryKey( iHrKey ).getUserId();
      UserParameters.setInstance( lUserId, "LOGIC", new UserParametersFake( lUserId, "LOGIC" ) );

      // create flight infoTo
      Date lActualDepartureDate = DateUtils.addHours( FLIGHT_DATE, -2 );
      Date lActualArrivalDate = DateUtils.addHours( lActualDepartureDate, 2 );
      iFlightInfoTO = generateFlightInfoTO( FLIGHT, lActualDepartureDate, lActualArrivalDate );

      iFlightHistBean = new FlightHistBean();
      iFlightHistBean.ejbCreate();
      iFlightHistBean.setSessionContext( new SessionContextFake() );
   }


   @After
   public void teardown() {
      int lUserId = OrgHr.findByPrimaryKey( iHrKey ).getUserId();
      UserParameters.setInstance( lUserId, "LOGIC", null );
   }


   private TaskKey createTaskOfInventory( final InventoryKey aInvKey, final Date aStartDate,
         final Date aEndDate ) {

      TaskKey lActualTaskKey = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aActualTask ) {
            aActualTask.setInventory( aInvKey );
            aActualTask.setStatus( RefEventStatusKey.COMPLETE );
            aActualTask.setActualStartDate( aStartDate );
            aActualTask.setActualEndDate( aEndDate );

            aActualTask.addUsage( new UsageSnapshot( aInvKey, HOURS,
                  EVENT_HOURS_USAGE.doubleValue(), EVENT_HOURS_USAGE.doubleValue(),
                  EVENT_HOURS_USAGE.doubleValue(), EVENT_ASSEMBLY_HOURS_USAGE.doubleValue(),
                  EVENT_ASSEMBLY_HOURS_USAGE.doubleValue() ) );
            aActualTask.addUsage( new UsageSnapshot( aInvKey, CYCLES,
                  EVENT_CYCLE_USAGE.doubleValue(), EVENT_CYCLE_USAGE.doubleValue(),
                  EVENT_CYCLE_USAGE.doubleValue(), EVENT_ASSEMBLY_CYCLE_USAGE.doubleValue(),
                  EVENT_ASSEMBLY_CYCLE_USAGE.doubleValue() ) );
         }
      } );

      return lActualTaskKey;
   }


   private TaskKey createInWorkWorkPackageOfInventory( final InventoryKey aInvKey,
         final Date aStartDate, final Date aEstimatedEndDate ) {
      TaskKey lWorkPackageKey = Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {
            aWorkPackage.setAircraft( aInvKey );
            aWorkPackage.setActualStartDate( aStartDate );
            aWorkPackage.setActualEndDate( aEstimatedEndDate );
            aWorkPackage.setStatus( RefEventStatusKey.IN_WORK );

            aWorkPackage.addUsageSnapshot( new UsageSnapshot( aInvKey, HOURS,
                  EVENT_HOURS_USAGE.doubleValue(), EVENT_HOURS_USAGE.doubleValue(),
                  EVENT_HOURS_USAGE.doubleValue(), null, null ) );
            aWorkPackage.addUsageSnapshot( new UsageSnapshot( aInvKey, CYCLES,
                  EVENT_CYCLE_USAGE.doubleValue(), EVENT_CYCLE_USAGE.doubleValue(),
                  EVENT_CYCLE_USAGE.doubleValue(), null, null ) );

         }
      } );

      return lWorkPackageKey;
   }


   private FaultKey createFaultOfInventory( final InventoryKey aInvKey, final Date aFoundOnDate ) {

      FaultKey lFaultKey = Domain.createFault( new DomainConfiguration<Fault>() {

         @Override
         public void configure( Fault aFault ) {
            aFault.setInventory( aInvKey );
            aFault.setFoundOnDate( aFoundOnDate );
            aFault.addUsageSnapshot( new UsageSnapshot( aInvKey, HOURS,
                  EVENT_HOURS_USAGE.doubleValue(), EVENT_HOURS_USAGE.doubleValue(),
                  EVENT_HOURS_USAGE.doubleValue(), EVENT_ASSEMBLY_HOURS_USAGE.doubleValue(),
                  EVENT_ASSEMBLY_HOURS_USAGE.doubleValue() ) );
            aFault.addUsageSnapshot( new UsageSnapshot( aInvKey, CYCLES,
                  EVENT_CYCLE_USAGE.doubleValue(), EVENT_CYCLE_USAGE.doubleValue(),
                  EVENT_CYCLE_USAGE.doubleValue(), EVENT_ASSEMBLY_CYCLE_USAGE.doubleValue(),
                  EVENT_ASSEMBLY_CYCLE_USAGE.doubleValue() ) );
         }
      } );

      return lFaultKey;
   }


   private CollectedUsageParm generateFlightUsage( InventoryKey aInventoryKey,
         DataTypeKey lDataType, BigDecimal lDelta ) {

      // Create a usage collection to be returned.
      new CollectedUsageParm( new UsageParmKey( aInventoryKey, lDataType ), lDelta.doubleValue() );

      CollectedUsageParm lUsageParm = new CollectedUsageParm(
            new UsageParmKey( aInventoryKey, lDataType ), lDelta.doubleValue() );

      // Create flight data source specifications.
      EqpDataSourceSpec
            .create( new UsageDefinitionKey( InvInvTable.getAssemblyByInventoryKey( aInventoryKey ),
                  RefDataSourceKey.MXFL ), lDataType );

      return lUsageParm;
   }


   private FlightInformationTO generateFlightInfoTO( String aFlightName, Date aActualDepartureDate,
         Date aActualArrivalDate ) {
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


   private void assertEventTsn( EventKey aEventKey, InventoryKey aInvKey, DataTypeKey aDataTypeKey,
         BigDecimal aExpectedTsnValue ) {

      EvtInvTable lEvtInv = EvtInvTable.findByEventAndInventory( aEventKey, aInvKey );
      EvtInvUsage lEvtInvUsage = EvtInvUsage
            .findByPrimaryKey( new EventInventoryUsageKey( lEvtInv.getPk(), aDataTypeKey ) );

      Assert.assertTrue(
            aExpectedTsnValue.compareTo( new BigDecimal( lEvtInvUsage.getTsnQt() ) ) == 0 );

   }
}
