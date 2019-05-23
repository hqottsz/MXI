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
import com.mxi.am.domain.Location;
import com.mxi.am.domain.Requirement;
import com.mxi.am.domain.WorkPackage;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.servlet.SessionContextFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.unittest.security.SecurityIdentificationStub;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.ejb.flighthist.FlightHistBean;
import com.mxi.mx.core.flight.model.FlightLegId;
import com.mxi.mx.core.key.AircraftKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.EventInventoryUsageKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefDataSourceKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.UsageDefinitionKey;
import com.mxi.mx.core.key.UsageParmKey;
import com.mxi.mx.core.services.event.inventory.UsageSnapshot;
import com.mxi.mx.core.services.event.usage.CollectedUsageParm;
import com.mxi.mx.core.services.flighthist.FlightInformationTO;
import com.mxi.mx.core.services.flighthist.Measurement;
import com.mxi.mx.core.services.inventory.InvUtils;
import com.mxi.mx.core.table.eqp.EqpDataSourceSpec;
import com.mxi.mx.core.table.evt.EvtInvTable;
import com.mxi.mx.core.table.evt.EvtInvUsage;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.org.OrgHr;


/**
 * Tests for {@linkplain FlightHistBean#editHistFlight} that involve updating the snapshot usage TSN
 * of system inventory involved in the flight.
 *
 */
public class EditHistFlight_AffectFollowingUsageSnapShotForSystemTest {

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

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * <pre>
    * Given an Aircraft with a Historical Flight,
    * And the Aircraft has a System Inventory attached to it,
    * And the System Inventory has one or more Tasks COMPLETED AFTER the Historical Flight actual arrival date,
    * When the usage Deltas of the Historical Flight are edited,
    * Then the TSN adjustment in the usage snapshots of those Tasks should be updated by the change to the Delta.
    * </pre>
    */
   @Test
   public void itUpdatesFollowingTaskTsnWhenFlightUsageEdited() throws Exception {

      final InventoryKey lAircraftInvKey =
            Domain.createAircraft( new DomainConfiguration<Aircraft>() {

               @Override
               public void configure( Aircraft aAircraft ) {
                  aAircraft.addSystem( "28 - FUEL" );
                  aAircraft.addUsage( CYCLES, INITIAL_CYCLES );
                  aAircraft.addUsage( HOURS, INITIAL_HOURS );
               }
            } );

      final InventoryKey lFuelSysInvKey = InvUtils.getSystemByName( lAircraftInvKey, "28 - FUEL" );

      FlightLegId lFlightLegId =
            createFlight( lAircraftInvKey, iFlightInfoTO, INITIAL_FLIGHT_DELTA );

      TaskKey lActualTaskKey = createTaskOfSystem( lFuelSysInvKey,
            DateUtils.addDays( iFlightDate, +10 ), DateUtils.addDays( iFlightDate, +11 ) );

      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( lAircraftInvKey, CYCLES, DELTA_VALUE_THREE ),
                  generateFlightUsage( lAircraftInvKey, HOURS, DELTA_VALUE_THREE ) };

      iFlightHistBean.editHistFlight( lFlightLegId, iHrKey, iFlightInfoTO, lEditUsageParms,
            NO_MEASUREMENTS );

      BigDecimal lDeltaDiff = DELTA_VALUE_THREE.subtract( INITIAL_FLIGHT_DELTA );

      assertEventTsn( lActualTaskKey.getEventKey(), lFuelSysInvKey, HOURS,
            iEventHoursUsage.add( lDeltaDiff ) );

      assertEventTsn( lActualTaskKey.getEventKey(), lFuelSysInvKey, CYCLES,
            iEventCycleUsage.add( lDeltaDiff ) );

   }


   /**
    * <pre>
    * Given an Aircraft with a Historical Flight,
    * And the Aircraft has a System Inventory attached to it,
    * And the System Inventory has one "in work" Work Packages STARTED AFTER the Historical Flight actual arrival date,
    * When the usage Deltas of the Historical Flight are edited,
    * Then the TSN adjustment in the usage snapshots of those Work Packages should be updated by the change to the Delta.
    * </pre>
    */
   @Test
   public void itUpdatesFollowingWorkPackageTsnWhenFlightUsageEdited() throws Exception {

      final InventoryKey lAircraftInvKey =
            Domain.createAircraft( new DomainConfiguration<Aircraft>() {

               @Override
               public void configure( Aircraft aAircraft ) {
                  aAircraft.addSystem( "28 - FUEL" );
                  aAircraft.addUsage( CYCLES, INITIAL_CYCLES );
                  aAircraft.addUsage( HOURS, INITIAL_HOURS );
               }
            } );

      final InventoryKey lFuelSysInvKey = InvUtils.getSystemByName( lAircraftInvKey, "28 - FUEL" );

      FlightLegId lFlightLegId =
            createFlight( lAircraftInvKey, iFlightInfoTO, INITIAL_FLIGHT_DELTA );

      TaskKey lActualTaskKey = createInWorkWorkPackageOfSystem( lFuelSysInvKey,
            DateUtils.addDays( iFlightDate, +10 ), DateUtils.addDays( iFlightDate, +11 ) );

      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( lAircraftInvKey, CYCLES, DELTA_VALUE_THREE ),
                  generateFlightUsage( lAircraftInvKey, HOURS, DELTA_VALUE_THREE ) };

      iFlightHistBean.editHistFlight( lFlightLegId, iHrKey, iFlightInfoTO, lEditUsageParms,
            NO_MEASUREMENTS );

      BigDecimal lDeltaDiff = DELTA_VALUE_THREE.subtract( INITIAL_FLIGHT_DELTA );

      assertEventTsn( lActualTaskKey.getEventKey(), lFuelSysInvKey, HOURS,
            iEventHoursUsage.add( lDeltaDiff ) );

      assertEventTsn( lActualTaskKey.getEventKey(), lFuelSysInvKey, CYCLES,
            iEventCycleUsage.add( lDeltaDiff ) );

   }


   /**
    * <pre>
    * Given an Aircraft with a Historical Flight,
    * And the Aircraft has a System Inventory attached to it,
    * And the System Inventory has one Faults against it that were FOUND AFTER the Historical Flight actual arrival date,
    * When the usage Deltas of the Historical Flight are edited,
    * Then the TSN adjustment in the usage snapshots of those Faults should be updated by the change to the Delta.
    * </pre>
    */
   @Test
   public void itUpdatesFollowingSystemFaultTsnWhenFlightUsageEdited() throws Exception {

      final InventoryKey lAircraftInvKey =
            Domain.createAircraft( new DomainConfiguration<Aircraft>() {

               @Override
               public void configure( Aircraft aAircraft ) {
                  aAircraft.addSystem( "28 - FUEL" );
                  aAircraft.addUsage( CYCLES, INITIAL_CYCLES );
                  aAircraft.addUsage( HOURS, INITIAL_HOURS );
               }
            } );

      final InventoryKey lFuelSysInvKey = InvUtils.getSystemByName( lAircraftInvKey, "28 - FUEL" );

      FlightLegId lFlightLegId =
            createFlight( lAircraftInvKey, iFlightInfoTO, INITIAL_FLIGHT_DELTA );

      FaultKey lFault =
            createFaultOfInventory( lFuelSysInvKey, DateUtils.addDays( iFlightDate, +10 ) );

      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( lAircraftInvKey, CYCLES, DELTA_VALUE_THREE ),
                  generateFlightUsage( lAircraftInvKey, HOURS, DELTA_VALUE_THREE ) };

      iFlightHistBean.editHistFlight( lFlightLegId, iHrKey, iFlightInfoTO, lEditUsageParms,
            NO_MEASUREMENTS );

      BigDecimal lDeltaDiff = DELTA_VALUE_THREE.subtract( INITIAL_FLIGHT_DELTA );

      assertEventTsn( lFault.getEventKey(), lFuelSysInvKey, HOURS,
            iEventHoursUsage.add( lDeltaDiff ) );

      assertEventTsn( lFault.getEventKey(), lFuelSysInvKey, CYCLES,
            iEventCycleUsage.add( lDeltaDiff ) );

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


   private TaskKey createTaskOfSystem( final InventoryKey aSystemKey, final Date aStartDate,
         final Date aEndDate ) {
      TaskKey lActualTaskKey = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aActualTask ) {
            aActualTask.setInventory( aSystemKey );
            aActualTask.setActualStartDate( aStartDate );
            aActualTask.setActualEndDate( aEndDate );
            aActualTask.setStatus( RefEventStatusKey.COMPLETE );

            aActualTask.addUsage( new UsageSnapshot( aSystemKey, HOURS,
                  iEventHoursUsage.doubleValue(), iEventHoursUsage.doubleValue(),
                  iEventHoursUsage.doubleValue(), iEventAssemblyHoursUsage.doubleValue(),
                  iEventAssemblyHoursUsage.doubleValue() ) );
            aActualTask.addUsage( new UsageSnapshot( aSystemKey, CYCLES,
                  iEventCycleUsage.doubleValue(), iEventCycleUsage.doubleValue(),
                  iEventCycleUsage.doubleValue(), iEventAssemblyCycleUsage.doubleValue(),
                  iEventAssemblyCycleUsage.doubleValue() ) );
         }
      } );

      return lActualTaskKey;
   }


   private TaskKey createInWorkWorkPackageOfSystem( final InventoryKey aSystemKey,
         final Date aStartDate, final Date aEndDate ) {
      TaskKey lWorkPackageKey = Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {
            aWorkPackage.setAircraft( aSystemKey );
            aWorkPackage.setActualStartDate( aStartDate );
            aWorkPackage.setActualEndDate( aEndDate );
            aWorkPackage.setStatus( RefEventStatusKey.IN_WORK );

            aWorkPackage.addUsageSnapshot( new UsageSnapshot( aSystemKey, HOURS,
                  iEventHoursUsage.doubleValue(), iEventHoursUsage.doubleValue(),
                  iEventHoursUsage.doubleValue(), iEventAssemblyHoursUsage.doubleValue(),
                  iEventAssemblyHoursUsage.doubleValue() ) );
            aWorkPackage.addUsageSnapshot( new UsageSnapshot( aSystemKey, CYCLES,
                  iEventCycleUsage.doubleValue(), iEventCycleUsage.doubleValue(),
                  iEventCycleUsage.doubleValue(), iEventAssemblyCycleUsage.doubleValue(),
                  iEventAssemblyCycleUsage.doubleValue() ) );

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


   private FlightLegId createFlight( InventoryKey aAircraftInvKey,
         FlightInformationTO aFlightInformationTO, BigDecimal aDelta ) throws Exception {

      CollectedUsageParm[] lFlightUsageParms =
            { generateFlightUsage( aAircraftInvKey, CYCLES, aDelta ),
                  generateFlightUsage( aAircraftInvKey, HOURS, aDelta ) };

      return iFlightHistBean.createHistFlight( new AircraftKey( aAircraftInvKey ), iHrKey,
            aFlightInformationTO, lFlightUsageParms, NO_MEASUREMENTS );
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


   private void assertEventTsn( EventKey aEventKey, InventoryKey aInvKey, DataTypeKey aDataTypeKey,
         BigDecimal aExpectedTsnValue ) {

      EvtInvTable lEvtInv = EvtInvTable.findByEventAndInventory( aEventKey, aInvKey );
      EvtInvUsage lEvtInvUsage = EvtInvUsage
            .findByPrimaryKey( new EventInventoryUsageKey( lEvtInv.getPk(), aDataTypeKey ) );

      Assert.assertTrue(
            aExpectedTsnValue.compareTo( new BigDecimal( lEvtInvUsage.getTsnQt() ) ) == 0 );

   }
}
