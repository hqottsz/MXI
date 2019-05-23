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
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.i18n.i18n;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.servlet.SessionContextFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.unittest.security.SecurityIdentificationStub;
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
import com.mxi.mx.core.table.evt.EvtStageTable;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.org.OrgHr;


/**
 * Tests for {@linkplain FlightHistBean#editHistFlight} that involve updating the snapshot usage TSN
 * of inventory involved in the flight.
 *
 */

public class EditHistFlight_AddHistoryNoteForAffecedEventTest {

   private static final String HR_USERNAME = "HR_USERNAME";
   private static final String FLIGHT = "FLIGHT";
   private static final Measurement[] NO_MEASUREMENTS = new Measurement[] {};

   // current cycle and hour values
   private static final BigDecimal INITIAL_CYCLES = new BigDecimal( 50 );
   private static final BigDecimal INITIAL_HOURS = new BigDecimal( 100 );

   // some deltas
   private static final BigDecimal INITIAL_FLIGHT_DELTA = new BigDecimal( 1 );
   private static final BigDecimal DELTA_VALUE_THREE = new BigDecimal( 3 );

   // the bean under test
   private FlightHistBean iFlightHistBean;

   private HumanResourceKey iHrKey;
   private FlightInformationTO iFlightInfoTO;
   private Date iFlightDate = new Date();

   private BigDecimal iEventHoursUsage = new BigDecimal( 400.0 );
   private BigDecimal iEventCycleUsage = new BigDecimal( 350.0 );
   private BigDecimal iEventAssemblyHoursUsage = new BigDecimal( 500.0 );
   private BigDecimal iEventAssemblyCycleUsage = new BigDecimal( 650.0 );

   private Date iEventStartDate;
   private Date iEventEndDate;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * <pre>
    * Test: Test that for a task completed after the flight, usage snapshot of assembly inventory is updated
    * Given an Aircraft with a historical flight
    * and aircraft has one attached tracked inventory
    * and the tracked inventory has one completed task with a completion date after the flight actual arrival date
    * When I edit the usage Deltas of the historical flight
    * Then the TSN usages of the aircraft as 'parent assembly' inventory in the task usage snapshot is updated by the delta difference
    * </pre>
    */
   @Test
   public void itAddsNoteToUpdatedTaskWhenFlightUsageEdited() throws Exception {

      final InventoryKey lAircraftInvKey =
            Domain.createAircraft( new DomainConfiguration<Aircraft>() {

               @Override
               public void configure( Aircraft aAircraft ) {
                  aAircraft.addUsage( CYCLES, INITIAL_CYCLES );
                  aAircraft.addUsage( HOURS, INITIAL_HOURS );
               }
            } );

      FlightLegId lFlightLegId = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aBuilder ) {
            aBuilder.setName( FLIGHT );
            aBuilder.setAircraft( lAircraftInvKey );
            aBuilder.setArrivalDate( iFlightDate );
            aBuilder.setHistorical( true );
            aBuilder.addUsage( lAircraftInvKey, HOURS, INITIAL_FLIGHT_DELTA, null );
            aBuilder.addUsage( lAircraftInvKey, CYCLES, INITIAL_FLIGHT_DELTA, null );
         }
      } );

      iEventStartDate = DateUtils.addDays( iFlightDate, -10 );
      iEventEndDate = DateUtils.addDays( iFlightDate, +10 );

      final InventoryKey lTrackedInvKey =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aTrk ) {
                  aTrk.setParent( lAircraftInvKey );
                  aTrk.addUsage( CYCLES, INITIAL_CYCLES );
                  aTrk.addUsage( HOURS, INITIAL_HOURS );
               }
            } );
      TaskKey lActualTaskKey = createTaskOfTrackedOn( lTrackedInvKey );

      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( lAircraftInvKey, CYCLES, DELTA_VALUE_THREE ),
                  generateFlightUsage( lAircraftInvKey, HOURS, DELTA_VALUE_THREE ) };

      iFlightHistBean.editHistFlight( lFlightLegId, iHrKey, iFlightInfoTO, lEditUsageParms,
            NO_MEASUREMENTS );

      BigDecimal lDeltaDiff = DELTA_VALUE_THREE.subtract( INITIAL_FLIGHT_DELTA );

      assertEventTsn( lActualTaskKey.getEventKey(), lTrackedInvKey, HOURS,
            iEventHoursUsage.add( lDeltaDiff ) );

      assertEventTsn( lActualTaskKey.getEventKey(), lTrackedInvKey, CYCLES,
            iEventCycleUsage.add( lDeltaDiff ) );

      String lExpectedTaskSystemNote =
            i18n.get( "core.msg.TASK_WORKPACKAGE_USAGE_CHANGE_SYSTEM_NOTE" );

      assertEventNote( lActualTaskKey.getEventKey(), lExpectedTaskSystemNote );

   }


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
   public void itAddsNoteToUpdatedFaultWhenFlightUsageEdited() throws Exception {

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
            aBuilder.setArrivalDate( iFlightDate );
            aBuilder.setHistorical( true );
            aBuilder.addUsage( lAircraftInvKey, HOURS, INITIAL_FLIGHT_DELTA, null );
            aBuilder.addUsage( lAircraftInvKey, CYCLES, INITIAL_FLIGHT_DELTA, null );
         }
      } );

      Date lFaultFoundOnDate = DateUtils.addDays( iFlightDate, +10 );
      FaultKey lFault = createFaultOfInventory( lSerializedKey, lFaultFoundOnDate );

      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( lAircraftInvKey, CYCLES, DELTA_VALUE_THREE ),
                  generateFlightUsage( lAircraftInvKey, HOURS, DELTA_VALUE_THREE ) };

      iFlightHistBean.editHistFlight( lFlightLegId, iHrKey, iFlightInfoTO, lEditUsageParms,
            NO_MEASUREMENTS );

      BigDecimal lDeltaDiff = DELTA_VALUE_THREE.subtract( INITIAL_FLIGHT_DELTA );

      assertEventTsn( lFault.getEventKey(), lSerializedKey, HOURS,
            iEventHoursUsage.add( lDeltaDiff ) );

      assertEventTsn( lFault.getEventKey(), lSerializedKey, CYCLES,
            iEventCycleUsage.add( lDeltaDiff ) );

      String lExpectedFaultSystemNote = i18n.get( "core.msg.FAULT_USAGE_CHANGE_SYSTEM_NOTE" );

      assertEventNote( lFault.getEventKey(), lExpectedFaultSystemNote );
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
   public void itAddsNoteToUpdatedWorkPackageWhenFlightUsageEdited() throws Exception {

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
            aBuilder.setArrivalDate( iFlightDate );
            aBuilder.setHistorical( true );
            aBuilder.addUsage( lAircraftInvKey, HOURS, INITIAL_FLIGHT_DELTA, null );
            aBuilder.addUsage( lAircraftInvKey, CYCLES, INITIAL_FLIGHT_DELTA, null );
         }
      } );

      // detach the tracked
      AttachableInventoryService lService =
            InventoryServiceFactory.getInstance().getAttachableInventoryService( lTrackedKey );
      lService.detachInventory( null, null, iHrKey, true, null, null );

      Date lStartDate = DateUtils.addDays( iFlightDate, +1 );
      Date lEstimatedEndDate = DateUtils.addDays( iFlightDate, +2 );
      TaskKey lActualWorkPackageKey =
            createInWorkWorkPackageOfInventory( lTrackedKey, lStartDate, lEstimatedEndDate );

      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( lAircraftInvKey, CYCLES, DELTA_VALUE_THREE ),
                  generateFlightUsage( lAircraftInvKey, HOURS, DELTA_VALUE_THREE ) };

      iFlightHistBean.editHistFlight( lFlightLegId, iHrKey, iFlightInfoTO, lEditUsageParms,
            NO_MEASUREMENTS );

      BigDecimal lDeltaDiff = DELTA_VALUE_THREE.subtract( INITIAL_FLIGHT_DELTA );

      assertEventTsn( lActualWorkPackageKey.getEventKey(), lTrackedKey, HOURS,
            iEventHoursUsage.add( lDeltaDiff ) );

      assertEventTsn( lActualWorkPackageKey.getEventKey(), lTrackedKey, CYCLES,
            iEventCycleUsage.add( lDeltaDiff ) );

      String lExpectedWorkPackageSystemNote =
            i18n.get( "core.msg.TASK_WORKPACKAGE_USAGE_CHANGE_SYSTEM_NOTE" );

      assertEventNote( lActualWorkPackageKey.getEventKey(), lExpectedWorkPackageSystemNote );
   }


   @Before
   public void setup() {
      iHrKey = new HumanResourceDomainBuilder().withUsername( HR_USERNAME ).build();
      int lUserId = OrgHr.findByPrimaryKey( iHrKey ).getUserId();
      SecurityIdentificationUtils.setInstance( new SecurityIdentificationStub( iHrKey ) );

      UserParametersFake lUserParms = new UserParametersFake( lUserId, "LOGIC" );
      lUserParms.setProperty( "TASK_USAGE_SNAPSHOT_EXCEEDS_CURRENT_USAGE", "INFO" );
      UserParameters.setInstance( lUserId, "LOGIC", lUserParms );

      // create flight infoTo
      StringBuilder lFlightName = new StringBuilder().append( FLIGHT )
            .append( new SimpleDateFormat( "SSSS" ).format( new Date() ) );
      Date lActualDepartureDate = DateUtils.addDays( iFlightDate, -2 );
      Date lActualArrivalDate = DateUtils.addHours( lActualDepartureDate, 2 );

      iFlightInfoTO = generateFlightInfoTO( lFlightName, lActualDepartureDate, lActualArrivalDate );

      iFlightHistBean = new FlightHistBean();
      iFlightHistBean.ejbCreate();
      iFlightHistBean.setSessionContext( new SessionContextFake() );
   }


   @After
   public void teardown() {
      int lUserId = OrgHr.findByPrimaryKey( iHrKey ).getUserId();
      UserParameters.setInstance( lUserId, "LOGIC", null );
   }


   private FaultKey createFaultOfInventory( final InventoryKey aInvKey, final Date aFoundOnDate ) {

      FaultKey lFaultKey = Domain.createFault( new DomainConfiguration<Fault>() {

         @Override
         public void configure( Fault aFault ) {
            aFault.setInventory( aInvKey );
            aFault.setFoundOnDate( aFoundOnDate );
            aFault.addUsageSnapshot( new UsageSnapshot( aInvKey, HOURS,
                  iEventHoursUsage.doubleValue(), iEventHoursUsage.doubleValue(),
                  iEventHoursUsage.doubleValue(), iEventAssemblyHoursUsage.doubleValue(),
                  iEventAssemblyHoursUsage.doubleValue() ) );
            aFault.addUsageSnapshot( new UsageSnapshot( aInvKey, CYCLES,
                  iEventCycleUsage.doubleValue(), iEventCycleUsage.doubleValue(),
                  iEventCycleUsage.doubleValue(), iEventAssemblyCycleUsage.doubleValue(),
                  iEventAssemblyCycleUsage.doubleValue() ) );
         }
      } );

      return lFaultKey;
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
                  iEventHoursUsage.doubleValue(), iEventHoursUsage.doubleValue(),
                  iEventHoursUsage.doubleValue(), null, null ) );
            aWorkPackage.addUsageSnapshot( new UsageSnapshot( aInvKey, CYCLES,
                  iEventCycleUsage.doubleValue(), iEventCycleUsage.doubleValue(),
                  iEventCycleUsage.doubleValue(), null, null ) );

         }
      } );

      return lWorkPackageKey;
   }


   private CollectedUsageParm generateFlightUsage( InventoryKey aInventoryKey,
         DataTypeKey lDataType, BigDecimal lDelta ) {

      // Create a usage collection to be returned.
      // CollectedUsageParm lUsageParm =
      new CollectedUsageParm( new UsageParmKey( aInventoryKey, lDataType ), lDelta.doubleValue() );

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


   private TaskKey createTaskOfTrackedOn( final InventoryKey aTrackedInvKey ) {

      TaskKey lActualTaskKey = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aActualTask ) {
            aActualTask.setInventory( aTrackedInvKey );
            aActualTask.setActualStartDate( iEventStartDate );
            aActualTask.setActualEndDate( iEventEndDate );
            aActualTask.setStatus( RefEventStatusKey.COMPLETE );

            aActualTask.addUsage( new UsageSnapshot( aTrackedInvKey, HOURS,
                  iEventHoursUsage.doubleValue(), iEventHoursUsage.doubleValue(),
                  iEventHoursUsage.doubleValue(), iEventAssemblyHoursUsage.doubleValue(),
                  iEventAssemblyHoursUsage.doubleValue() ) );
            aActualTask.addUsage( new UsageSnapshot( aTrackedInvKey, CYCLES,
                  iEventCycleUsage.doubleValue(), iEventCycleUsage.doubleValue(),
                  iEventCycleUsage.doubleValue(), iEventAssemblyCycleUsage.doubleValue(),
                  iEventAssemblyCycleUsage.doubleValue() ) );
         }
      } );

      return lActualTaskKey;
   }


   private void assertEventTsn( EventKey aEventKey, InventoryKey aInvKey, DataTypeKey aDataTypeKey,
         BigDecimal aExpectedTsnValue ) {

      EvtInvTable lEvtInv = EvtInvTable.findByEventAndInventory( aEventKey, aInvKey );
      EvtInvUsage lEvtInvUsage = EvtInvUsage
            .findByPrimaryKey( new EventInventoryUsageKey( lEvtInv.getPk(), aDataTypeKey ) );

      Assert.assertTrue(
            aExpectedTsnValue.compareTo( new BigDecimal( lEvtInvUsage.getTsnQt() ) ) == 0 );

   }


   private void assertEventNote( EventKey aEventKey, String aSystemNote ) {
      DataSet lStageSnapshot = EvtStageTable.getStageSnapshot( aEventKey );
      while ( lStageSnapshot.next() ) {
         String lNote = lStageSnapshot.getString( "stage_note" );
         Assert.assertTrue( lNote.contains( aSystemNote ) );
         return;
      }

      Assert.fail( "Event note is not found." );
   }
}
