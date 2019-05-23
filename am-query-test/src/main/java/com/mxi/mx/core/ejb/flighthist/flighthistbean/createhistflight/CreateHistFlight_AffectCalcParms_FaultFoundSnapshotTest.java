package com.mxi.mx.core.ejb.flighthist.flighthistbean.createhistflight;

import static com.mxi.mx.core.key.DataTypeKey.CYCLES;
import static com.mxi.mx.core.key.DataTypeKey.HOURS;
import static com.mxi.mx.core.key.RefBOMClassKey.SYS;
import static com.mxi.mx.core.key.RefBOMClassKey.TRK;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.AircraftAssembly;
import com.mxi.am.domain.CalculatedUsageParameter;
import com.mxi.am.domain.ConfigurationSlot;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Engine;
import com.mxi.am.domain.EngineAssembly;
import com.mxi.am.domain.Fault;
import com.mxi.am.domain.Location;
import com.mxi.am.domain.System;
import com.mxi.am.domain.TrackedInventory;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.servlet.SessionContextFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.ejb.flighthist.FlightHistBean;
import com.mxi.mx.core.key.AircraftKey;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.EventInventoryKey;
import com.mxi.mx.core.key.EventInventoryUsageKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefDataSourceKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.UsageDefinitionKey;
import com.mxi.mx.core.key.UsageParmKey;
import com.mxi.mx.core.services.event.inventory.UsageSnapshot;
import com.mxi.mx.core.services.event.usage.CollectedUsageParm;
import com.mxi.mx.core.services.flighthist.FlightInformationTO;
import com.mxi.mx.core.services.flighthist.Measurement;
import com.mxi.mx.core.services.inventory.InvUtils;
import com.mxi.mx.core.table.eqp.EqpAssmblPos;
import com.mxi.mx.core.table.eqp.EqpDataSourceSpec;
import com.mxi.mx.core.table.evt.EvtInvUsage;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.org.OrgHr;


/**
 * Tests for {@linkplain FlightHistBean#createHistFlight} that involve updating calculated
 * parameters for fault usage when found snapshots of aircraft components, sub-assembly system and
 * sub-assembly components.
 */
public class CreateHistFlight_AffectCalcParms_FaultFoundSnapshotTest {

   private static final String HR_USERNAME = "HR_USERNAME";
   private static final Measurement[] NO_MEASUREMENTS = new Measurement[] {};
   private static final Date FAULT_DATE = DateUtils.addDays( new Date(), -50 );
   private static final Date FLIGHT_DATE = DateUtils.addDays( new Date(), -100 );

   private static final String FLIGHT_NAME = "TEST_CALC_PARAM_FLIGHT";
   private static final String SYSTEM_NAME = "79-11 (ENGINE OIL STORAGE)";

   private static final String TRK_CONFIG_SLOT_CODE = "TRK_CS";
   private static final String SYS_CONFIG_SLOT_CODE = "SYS_CS";

   private static final String CALC_PARM_DATA_TYPE_FOR_HOURS = "CALC_PARM_DATA_TYPE_FOR_HOURS";
   private static final String CALC_PARM_DATA_TYPE_FOR_CYCLE = "CALC_PARM_DATA_TYPE_FOR_CYCLE";
   private static final String CALC_PARM_FUNCTION_NAME = "CALC_PARM_FUNCTION_NAME";
   private static final String CALC_PARM_CONSTANT_NAME = "CALC_PARM_CONSTANT_NAME";
   private static final BigDecimal CALC_PARM_CONSTANT = BigDecimal.valueOf( 2.0 );

   // Aircraft Current Hours
   private static final BigDecimal CURRENT_HOURS = BigDecimal.valueOf( 5 );
   private static final BigDecimal CURRENT_CYCLES = BigDecimal.valueOf( 1 );
   private static final BigDecimal CURRENT_CALC_PARM_HOURS = BigDecimal.valueOf( 10 );
   private static final BigDecimal CURRENT_CALC_PARM_CYCLES = BigDecimal.valueOf( 2 );

   // Fault's usage-when-found.
   private static final BigDecimal HOURS_AT_FAULT_FOUND = BigDecimal.valueOf( 5 );
   private static final BigDecimal CALC_PARM_HOURS_AT_FAULT_FOUND =
         HOURS_AT_FAULT_FOUND.multiply( CALC_PARM_CONSTANT );

   private static final BigDecimal CYCLES_AT_FAULT_FOUND = BigDecimal.valueOf( 1 );
   private static final BigDecimal CALC_PARM_CYCLES_AT_FAULT_FOUND =
         CYCLES_AT_FAULT_FOUND.multiply( CALC_PARM_CONSTANT );

   private FlightHistBean iFlightHistBean;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Given an aircraft and a subcomponent attached to the aircraft tracking usages and calculated
    * parameters
    *
    * And a fault is raised and left open against the subcomponent
    *
    * When a historical flight is created for the aircraft before the fault found date
    *
    * Then the calculated parameters of the fault snapshot are recalculated correctly
    *
    */
   @Test
   public void itRecalculatesCalcParmForAircraftComponentWhenFlightCreatedBeforeFaultFoundDate()
         throws Exception {

      // Important!
      // addEquationFunctionToDatabase() needs to be called prior to any data setup in database
      // as the method performs an explicit roll-back.
      addEquationFunctionToDatabase();
      HumanResourceKey lHrKey = createHumanResource();

      final Date lFaultFoundAfterFlightDate = DateUtils.addDays( FLIGHT_DATE, 1 );

      // Set up the parameter and calculated-parameter on the root config slot.
      final AssemblyKey lAcftAssembly = createAcftAssyTrackingUsageParmsAndCalcParms();

      ConfigSlotKey lRootConfigSlot = Domain.readRootConfigurationSlot( lAcftAssembly );
      final ConfigSlotPositionKey lTrkConfigSlotPos =
            getConfigSlotPos( lRootConfigSlot, TRK_CONFIG_SLOT_CODE );

      // Get the data type keys for the calculated parameters.
      final DataTypeKey lDataType_CALC_PARM_FOR_HOURS =
            Domain.readUsageParameter( lRootConfigSlot, CALC_PARM_DATA_TYPE_FOR_HOURS );

      final DataTypeKey lDataType_CALC_PARM_FOR_CYCLES =
            Domain.readUsageParameter( lRootConfigSlot, CALC_PARM_DATA_TYPE_FOR_CYCLE );

      final InventoryKey lTrk =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aAircraftTrk ) {
                  aAircraftTrk.setPosition( lTrkConfigSlotPos );
                  aAircraftTrk.addUsage( HOURS, CURRENT_HOURS );
                  aAircraftTrk.addUsage( CYCLES, CURRENT_CYCLES );
                  aAircraftTrk.addUsage( lDataType_CALC_PARM_FOR_HOURS, CURRENT_CALC_PARM_HOURS );
                  aAircraftTrk.addUsage( lDataType_CALC_PARM_FOR_CYCLES, CURRENT_CALC_PARM_CYCLES );
               }
            } );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aBuilder ) {
            aBuilder.setAssembly( lAcftAssembly );
            aBuilder.addUsage( HOURS, CURRENT_HOURS );
            aBuilder.addUsage( CYCLES, CURRENT_CYCLES );
            aBuilder.addUsage( lDataType_CALC_PARM_FOR_HOURS, CURRENT_CALC_PARM_HOURS );
            aBuilder.addUsage( lDataType_CALC_PARM_FOR_CYCLES, CURRENT_CALC_PARM_CYCLES );
            aBuilder.addTracked( lTrk );
         }
      } );

      // Create a fault against the subcomponent
      FaultKey lFault = Domain.createFault( new DomainConfiguration<Fault>() {

         @Override
         public void configure( Fault aFault ) {
            aFault.setFoundOnDate( lFaultFoundAfterFlightDate );
            aFault.setInventory( lTrk );
            aFault.addUsageSnapshot( new UsageSnapshot( lTrk, HOURS, HOURS_AT_FAULT_FOUND ) );
            aFault.addUsageSnapshot( new UsageSnapshot( lTrk, lDataType_CALC_PARM_FOR_HOURS,
                  CALC_PARM_HOURS_AT_FAULT_FOUND ) );
            aFault.addUsageSnapshot( new UsageSnapshot( lTrk, CYCLES, CYCLES_AT_FAULT_FOUND ) );
            aFault.addUsageSnapshot( new UsageSnapshot( lTrk, lDataType_CALC_PARM_FOR_CYCLES,
                  CALC_PARM_CYCLES_AT_FAULT_FOUND ) );
         }
      } );

      //
      // When a historic flight is created with usage deltas for the aircraft
      //
      BigDecimal lHoursDelta = new BigDecimal( 4 );
      BigDecimal lCyclesDelta = new BigDecimal( 2 );
      CollectedUsageParm[] lUsageParms = { generateFlightUsage( lAircraft, HOURS, lHoursDelta ),
            generateFlightUsage( lAircraft, CYCLES, lCyclesDelta ) };

      Date lActualDepartureDate = DateUtils.addHours( FLIGHT_DATE, -lHoursDelta.intValue() );

      iFlightHistBean.createHistFlight( new AircraftKey( lAircraft ), lHrKey,
            generateFlightInfoTO( FLIGHT_NAME, lActualDepartureDate, FLIGHT_DATE ), lUsageParms,
            NO_MEASUREMENTS );

      //
      // Then
      //
      Double lActualCalcParmForCycles =
            getCalcParmFaultFoundUsageTsn( lFault, lDataType_CALC_PARM_FOR_CYCLES );
      Double lActualCalcParmForHours =
            getCalcParmFaultFoundUsageTsn( lFault, lDataType_CALC_PARM_FOR_HOURS );

      Double lExpectedCalcParmForCycles = CALC_PARM_CYCLES_AT_FAULT_FOUND
            .add( lCyclesDelta.multiply( CALC_PARM_CONSTANT ) ).doubleValue();
      Double lExpectedCalcParmForHours = CALC_PARM_HOURS_AT_FAULT_FOUND
            .add( lHoursDelta.multiply( CALC_PARM_CONSTANT ) ).doubleValue();

      {
         // Important!
         // All data needed for assertions must be retrieved prior to calling
         // dropEquationFunctionToDatabase(). As it performs an explicit roll-back.
         dropEquationFunctionToDatabase();
      }

      assertEquals( "Unexpected value for:" + CALC_PARM_DATA_TYPE_FOR_CYCLE,
            lExpectedCalcParmForCycles, lActualCalcParmForCycles );
      assertEquals( "Unexpected value for:" + CALC_PARM_DATA_TYPE_FOR_HOURS,
            lExpectedCalcParmForHours, lActualCalcParmForHours );
   }


   /**
    * Given an aircraft and a subcomponent attached to the aircraft tracking usages and calculated
    * parameters
    *
    * And a fault is raised and left open against the subcomponent
    *
    * When a historical flight is created after the fault was raised
    *
    * Then the calculated parameters of the fault snapshot are not affected
    *
    */
   @Test
   public void
         itDoesNotRecalculateCalcParmForAircraftComponentWhenFlightCreatedAfterFaultFoundDate()
               throws Exception {

      // Important!
      // addEquationFunctionToDatabase() needs to be called prior to any data setup in database
      // as the method performs an explicit roll-back.
      addEquationFunctionToDatabase();
      HumanResourceKey lHrKey = createHumanResource();

      final Date lFaultFoundBeforeFlightDate = DateUtils.addDays( FLIGHT_DATE, -1 );

      // Set up the parameter and calculated-parameter on the root config slot.
      final AssemblyKey lAcftAssembly = createAcftAssyTrackingUsageParmsAndCalcParms();

      // Get the data type keys for the calculated parameters.
      final DataTypeKey lDataType_CALC_PARM_FOR_HOURS = Domain.readUsageParameter(
            Domain.readRootConfigurationSlot( lAcftAssembly ), CALC_PARM_DATA_TYPE_FOR_HOURS );

      final DataTypeKey lDataType_CALC_PARM_FOR_CYCLES = Domain.readUsageParameter(
            Domain.readRootConfigurationSlot( lAcftAssembly ), CALC_PARM_DATA_TYPE_FOR_CYCLE );

      final InventoryKey lTrk =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aAircraftTrk ) {
                  aAircraftTrk.addUsage( HOURS, CURRENT_HOURS );
                  aAircraftTrk.addUsage( CYCLES, CURRENT_CYCLES );
                  aAircraftTrk.addUsage( lDataType_CALC_PARM_FOR_HOURS, CURRENT_CALC_PARM_HOURS );
                  aAircraftTrk.addUsage( lDataType_CALC_PARM_FOR_CYCLES, CURRENT_CALC_PARM_CYCLES );
               }
            } );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aBuilder ) {
            aBuilder.setAssembly( lAcftAssembly );
            aBuilder.addUsage( HOURS, CURRENT_HOURS );
            aBuilder.addUsage( CYCLES, CURRENT_CYCLES );
            aBuilder.addUsage( lDataType_CALC_PARM_FOR_HOURS, CURRENT_CALC_PARM_HOURS );
            aBuilder.addUsage( lDataType_CALC_PARM_FOR_CYCLES, CURRENT_CALC_PARM_CYCLES );
            aBuilder.addTracked( lTrk );
         }
      } );

      // Create a fault against the subcomponent
      FaultKey lFault = Domain.createFault( new DomainConfiguration<Fault>() {

         @Override
         public void configure( Fault aFault ) {
            aFault.setFoundOnDate( lFaultFoundBeforeFlightDate );
            aFault.setInventory( lTrk );
            aFault.addUsageSnapshot( new UsageSnapshot( lTrk, HOURS, HOURS_AT_FAULT_FOUND ) );
            aFault.addUsageSnapshot( new UsageSnapshot( lTrk, lDataType_CALC_PARM_FOR_HOURS,
                  CALC_PARM_HOURS_AT_FAULT_FOUND ) );
            aFault.addUsageSnapshot( new UsageSnapshot( lTrk, CYCLES, CYCLES_AT_FAULT_FOUND ) );
            aFault.addUsageSnapshot( new UsageSnapshot( lTrk, lDataType_CALC_PARM_FOR_CYCLES,
                  CALC_PARM_CYCLES_AT_FAULT_FOUND ) );
         }
      } );

      //
      // When a historic flight is created with usage deltas for the aircraft
      //
      BigDecimal lHoursDelta = new BigDecimal( 4 );
      BigDecimal lCyclesDelta = new BigDecimal( 2 );
      CollectedUsageParm[] lUsageParms = { generateFlightUsage( lAircraft, HOURS, lHoursDelta ),
            generateFlightUsage( lAircraft, CYCLES, lCyclesDelta ) };

      Date lActualDepartureDate = DateUtils.addHours( FLIGHT_DATE, -lHoursDelta.intValue() );

      iFlightHistBean.createHistFlight( new AircraftKey( lAircraft ), lHrKey,
            generateFlightInfoTO( FLIGHT_NAME, lActualDepartureDate, FLIGHT_DATE ), lUsageParms,
            NO_MEASUREMENTS );

      //
      // Then
      //
      Double lActualCalcParmForCycles =
            getCalcParmFaultFoundUsageTsn( lFault, lDataType_CALC_PARM_FOR_CYCLES );
      Double lActualCalcParmForHours =
            getCalcParmFaultFoundUsageTsn( lFault, lDataType_CALC_PARM_FOR_HOURS );

      Double lExpectedCalcParmForCycles = CALC_PARM_CYCLES_AT_FAULT_FOUND.doubleValue();
      Double lExpectedCalcParmForHours = CALC_PARM_HOURS_AT_FAULT_FOUND.doubleValue();

      {
         // Important!
         // All data needed for assertions must be retrieved prior to calling
         // dropEquationFunctionToDatabase(). As it performs an explicit roll-back.
         dropEquationFunctionToDatabase();
      }

      assertEquals( "Unexpected value for:" + CALC_PARM_DATA_TYPE_FOR_CYCLE,
            lExpectedCalcParmForCycles, lActualCalcParmForCycles );
      assertEquals( "Unexpected value for:" + CALC_PARM_DATA_TYPE_FOR_HOURS,
            lExpectedCalcParmForHours, lActualCalcParmForHours );
   }


   /**
    *
    * Verify that calculated parameters of a usage snapshot for a fault, against a TRK sub-component
    * of an engine installed on an aircraft, are recalculated when a historical flight is created
    * prior to the fault.
    *
    * <pre>
    *    Given a TRK inventory installed on an engine that is installed on an aircraft
    *      And the engine and TRK are tracking usages and a calculated parameter
    *      And a fault raised (and left open) against the TRK
    *     When a historical flight is created before the fault found date
    *     Then the calculated parameters of the fault snapshot are recalculated
    * </pre>
    *
    */
   @Test
   public void itRecalculatesCalcParmForSubAssyComponentWhenFlightCreatedBeforeFaultFoundDate()
         throws Exception {

      // Important!
      // addEquationFunctionToDatabase() needs to be called prior to any data setup in database
      // as the method performs an explicit roll-back.
      addEquationFunctionToDatabase();

      // Given a TRK inventory installed on an engine that is installed on an aircraft
      // and the engine and TRK are tracking usages and a calculated parameter.
      final AssemblyKey lEngineAssy =
            Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

               @Override
               public void configure( EngineAssembly aBuilder ) {
                  aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot aBuilder ) {
                        // Add a TRK config slot to the root config slot.
                        aBuilder
                              .addConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                                 @Override
                                 public void configure( ConfigurationSlot aBuilder ) {
                                    aBuilder.setConfigurationSlotClass( TRK );
                                    aBuilder.setCode( TRK_CONFIG_SLOT_CODE );
                                 }
                              } );
                        // Add a usage parameter and a calculated parameter based on that usage
                        // parameter.
                        aBuilder.addUsageParameter( HOURS );
                        aBuilder.addCalculatedUsageParameter(
                              new DomainConfiguration<CalculatedUsageParameter>() {

                                 @Override
                                 public void configure( CalculatedUsageParameter aBuilder ) {
                                    aBuilder.setCode( CALC_PARM_DATA_TYPE_FOR_HOURS );
                                    aBuilder.setDatabaseCalculation( CALC_PARM_FUNCTION_NAME );
                                    aBuilder.setPrecisionQt( 2 );

                                    // Add the constant and parameter in the order in which the
                                    // function is expecting them.
                                    aBuilder.addConstant( CALC_PARM_CONSTANT_NAME,
                                          CALC_PARM_CONSTANT );
                                    aBuilder.addParameter( HOURS );
                                 }

                              } );
                     }
                  } );
               }
            } );

      ConfigSlotKey lRootConfigSlot = Domain.readRootConfigurationSlot( lEngineAssy );
      final ConfigSlotPositionKey lTrkConfigSlotPos =
            getConfigSlotPos( lRootConfigSlot, TRK_CONFIG_SLOT_CODE );
      final DataTypeKey lCalcParmKey =
            Domain.readUsageParameter( lRootConfigSlot, CALC_PARM_DATA_TYPE_FOR_HOURS );

      final InventoryKey lTrk =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aEngineTrk ) {
                  aEngineTrk.setPosition( lTrkConfigSlotPos );
                  aEngineTrk.addUsage( HOURS, CURRENT_HOURS );
                  aEngineTrk.addUsage( lCalcParmKey, CURRENT_CALC_PARM_HOURS );
               }
            } );

      final InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.addUsage( HOURS, CURRENT_HOURS );
            aEngine.addUsage( lCalcParmKey, CURRENT_CALC_PARM_HOURS );
            aEngine.addTracked( lTrk );
         }

      } );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aBuilder ) {
            aBuilder.addEngine( lEngine );
         }
      } );

      // Given a fault raised (and left open) against the TRK.
      FaultKey lFault = Domain.createFault( new DomainConfiguration<Fault>() {

         @Override
         public void configure( Fault aFault ) {
            aFault.setFoundOnDate( FAULT_DATE );
            aFault.setInventory( lTrk );
            aFault.addUsageSnapshot( new UsageSnapshot( lTrk, HOURS, HOURS_AT_FAULT_FOUND ) );
            aFault.addUsageSnapshot(
                  new UsageSnapshot( lTrk, lCalcParmKey, CALC_PARM_HOURS_AT_FAULT_FOUND ) );
         }
      } );

      // When a historical flight is created before the fault found date
      // (arrival date before fault date).
      BigDecimal lHoursDelta = new BigDecimal( 4 );
      Date lArrivalDate = DateUtils.addDays( FAULT_DATE, -10 );
      Date lDepartureDate = DateUtils.addHours( lArrivalDate, -lHoursDelta.intValue() );

      CollectedUsageParm[] lUsageParms = { generateFlightUsage( lEngine, HOURS, lHoursDelta ) };

      iFlightHistBean.createHistFlight( new AircraftKey( lAircraft ), createHumanResource(),
            generateFlightInfoTO( FLIGHT_NAME, lDepartureDate, lArrivalDate ), lUsageParms,
            NO_MEASUREMENTS );

      // Then the calculated parameters of the fault snapshot are recalculated
      Double lActualCalcParm = getCalcParmFaultFoundUsageTsn( lFault, lCalcParmKey );
      Double lExpectedCalcParm = CALC_PARM_HOURS_AT_FAULT_FOUND
            .add( lHoursDelta.multiply( CALC_PARM_CONSTANT ) ).doubleValue();

      {
         // Important!
         // All data needed for assertions must be retrieved prior to calling
         // dropEquationFunctionToDatabase(). As it performs an explicit roll-back.
         dropEquationFunctionToDatabase();
      }

      assertEquals( "Unexpected value for calc parm: " + CALC_PARM_DATA_TYPE_FOR_HOURS,
            lExpectedCalcParm, lActualCalcParm );

   }


   /**
    * Given an aircraft, an engine attached to the aircraft and the engine has a TRK component
    * attached to it And all these inventories are tracking usages and calculated parameters
    *
    * And a fault is raised and left open against the TRK component
    *
    * When a historical flight is created after the fault found date
    *
    * Then the calculated parameters of the fault snapshot are not affected
    *
    */
   @Test
   public void itDoesNotRecalculateCalcParmForSubAssyComponentWhenFlightCreatedAfterFaultFoundDate()
         throws Exception {

      // Important!
      // addEquationFunctionToDatabase() needs to be called prior to any data setup in database
      // as the method performs an explicit roll-back.
      addEquationFunctionToDatabase();
      HumanResourceKey lHrKey = createHumanResource();

      final Date lFaultFoundBeforeFlightDate = DateUtils.addDays( FLIGHT_DATE, -1 );

      // Set up the parameter and calculated-parameter on the root config slot.
      final AssemblyKey lAcftAssembly = createAcftAssyTrackingUsageParmsAndCalcParms();

      // Get the data type keys for the calculated parameters.
      final DataTypeKey lDataType_CALC_PARM_FOR_HOURS = Domain.readUsageParameter(
            Domain.readRootConfigurationSlot( lAcftAssembly ), CALC_PARM_DATA_TYPE_FOR_HOURS );

      final DataTypeKey lDataType_CALC_PARM_FOR_CYCLES = Domain.readUsageParameter(
            Domain.readRootConfigurationSlot( lAcftAssembly ), CALC_PARM_DATA_TYPE_FOR_CYCLE );

      final InventoryKey lTrk =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aEngineTrk ) {
                  aEngineTrk.addUsage( HOURS, CURRENT_HOURS );
                  aEngineTrk.addUsage( CYCLES, CURRENT_CYCLES );
                  aEngineTrk.addUsage( lDataType_CALC_PARM_FOR_HOURS, CURRENT_CALC_PARM_HOURS );
                  aEngineTrk.addUsage( lDataType_CALC_PARM_FOR_CYCLES, CURRENT_CALC_PARM_CYCLES );
               }
            } );

      final InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.addUsage( HOURS, CURRENT_HOURS );
            aEngine.addUsage( CYCLES, CURRENT_CYCLES );
            aEngine.addUsage( lDataType_CALC_PARM_FOR_HOURS, CURRENT_CALC_PARM_HOURS );
            aEngine.addUsage( lDataType_CALC_PARM_FOR_CYCLES, CURRENT_CALC_PARM_CYCLES );
            aEngine.addTracked( lTrk );
         }

      } );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aBuilder ) {
            aBuilder.setAssembly( lAcftAssembly );
            aBuilder.addUsage( HOURS, CURRENT_HOURS );
            aBuilder.addUsage( CYCLES, CURRENT_CYCLES );
            aBuilder.addUsage( lDataType_CALC_PARM_FOR_HOURS, CURRENT_CALC_PARM_HOURS );
            aBuilder.addUsage( lDataType_CALC_PARM_FOR_CYCLES, CURRENT_CALC_PARM_CYCLES );
            aBuilder.addEngine( lEngine );
         }
      } );

      // Create a fault against the subcomponent
      FaultKey lFault = Domain.createFault( new DomainConfiguration<Fault>() {

         @Override
         public void configure( Fault aFault ) {
            aFault.setFoundOnDate( lFaultFoundBeforeFlightDate );
            aFault.setInventory( lTrk );
            aFault.addUsageSnapshot( new UsageSnapshot( lTrk, HOURS, HOURS_AT_FAULT_FOUND ) );
            aFault.addUsageSnapshot( new UsageSnapshot( lTrk, lDataType_CALC_PARM_FOR_HOURS,
                  CALC_PARM_HOURS_AT_FAULT_FOUND ) );
            aFault.addUsageSnapshot( new UsageSnapshot( lTrk, CYCLES, CYCLES_AT_FAULT_FOUND ) );
            aFault.addUsageSnapshot( new UsageSnapshot( lTrk, lDataType_CALC_PARM_FOR_CYCLES,
                  CALC_PARM_CYCLES_AT_FAULT_FOUND ) );
         }
      } );

      //
      // When a historic flight is created with usage deltas for the aircraft
      //
      BigDecimal lHoursDelta = new BigDecimal( 4 );
      BigDecimal lCyclesDelta = new BigDecimal( 2 );
      CollectedUsageParm[] lUsageParms = { generateFlightUsage( lAircraft, HOURS, lHoursDelta ),
            generateFlightUsage( lAircraft, CYCLES, lCyclesDelta ),
            generateFlightUsage( lEngine, HOURS, lHoursDelta ),
            generateFlightUsage( lEngine, CYCLES, lCyclesDelta ) };

      Date lActualDepartureDate = DateUtils.addHours( FLIGHT_DATE, -lHoursDelta.intValue() );

      iFlightHistBean.createHistFlight( new AircraftKey( lAircraft ), lHrKey,
            generateFlightInfoTO( FLIGHT_NAME, lActualDepartureDate, FLIGHT_DATE ), lUsageParms,
            NO_MEASUREMENTS );

      //
      // Then
      //
      Double lActualCalcParmForCycles =
            getCalcParmFaultFoundUsageTsn( lFault, lDataType_CALC_PARM_FOR_CYCLES );
      Double lActualCalcParmForHours =
            getCalcParmFaultFoundUsageTsn( lFault, lDataType_CALC_PARM_FOR_HOURS );

      Double lExpectedCalcParmForCycles = CALC_PARM_CYCLES_AT_FAULT_FOUND.doubleValue();
      Double lExpectedCalcParmForHours = CALC_PARM_HOURS_AT_FAULT_FOUND.doubleValue();

      {
         // Important!
         // All data needed for assertions must be retrieved prior to calling
         // dropEquationFunctionToDatabase(). As it performs an explicit roll-back.
         dropEquationFunctionToDatabase();
      }

      assertEquals( "Unexpected value for:" + CALC_PARM_DATA_TYPE_FOR_CYCLE,
            lExpectedCalcParmForCycles, lActualCalcParmForCycles );
      assertEquals( "Unexpected value for:" + CALC_PARM_DATA_TYPE_FOR_HOURS,
            lExpectedCalcParmForHours, lActualCalcParmForHours );
   }


   /**
    * Given an aircraft, an engine attached to the aircraft and the engine has a system And all
    * these inventories are tracking usages and calculated parameters
    *
    * And a fault is raised and left open against the system
    *
    * When a historical flight is created after the fault found date
    *
    * Then the calculated parameters of the fault snapshot are recalculated correctly
    *
    */
   @Test
   public void itDoesNotitRecalculatesCalcParmForSubAssySystemWhenFlightCreatedAfterFaultFoundDate()
         throws Exception {

      // Important!
      // addEquationFunctionToDatabase() needs to be called prior to any data setup in database
      // as the method performs an explicit roll-back.
      addEquationFunctionToDatabase();
      HumanResourceKey lHrKey = createHumanResource();

      final Date lFaultFoundBeforeFlightDate = DateUtils.addDays( FLIGHT_DATE, -1 );

      // Set up the parameter and calculated-parameter on the root config slot.
      final AssemblyKey lAcftAssembly = createAcftAssyTrackingUsageParmsAndCalcParms();

      // Get the data type keys for the calculated parameters.
      final DataTypeKey lDataType_CALC_PARM_FOR_HOURS = Domain.readUsageParameter(
            Domain.readRootConfigurationSlot( lAcftAssembly ), CALC_PARM_DATA_TYPE_FOR_HOURS );

      final DataTypeKey lDataType_CALC_PARM_FOR_CYCLES = Domain.readUsageParameter(
            Domain.readRootConfigurationSlot( lAcftAssembly ), CALC_PARM_DATA_TYPE_FOR_CYCLE );

      final InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.addUsage( HOURS, CURRENT_HOURS );
            aEngine.addUsage( CYCLES, CURRENT_CYCLES );
            aEngine.addUsage( lDataType_CALC_PARM_FOR_HOURS, CURRENT_CALC_PARM_HOURS );
            aEngine.addUsage( lDataType_CALC_PARM_FOR_CYCLES, CURRENT_CALC_PARM_CYCLES );
            aEngine.addSystem( new DomainConfiguration<System>() {

               @Override
               public void configure( System aSystem ) {
                  aSystem.setName( SYSTEM_NAME );
                  aSystem.addUsage( HOURS, CURRENT_HOURS );
                  aSystem.addUsage( CYCLES, CURRENT_CYCLES );
                  aSystem.addUsage( lDataType_CALC_PARM_FOR_HOURS, CURRENT_CALC_PARM_HOURS );
                  aSystem.addUsage( lDataType_CALC_PARM_FOR_CYCLES, CURRENT_CALC_PARM_CYCLES );
               }

            } );
         }

      } );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aBuilder ) {
            aBuilder.setAssembly( lAcftAssembly );
            aBuilder.addUsage( HOURS, CURRENT_HOURS );
            aBuilder.addUsage( CYCLES, CURRENT_CYCLES );
            aBuilder.addUsage( lDataType_CALC_PARM_FOR_HOURS, CURRENT_CALC_PARM_HOURS );
            aBuilder.addUsage( lDataType_CALC_PARM_FOR_CYCLES, CURRENT_CALC_PARM_CYCLES );
            aBuilder.addEngine( lEngine );
         }
      } );

      final InventoryKey lEngineSysInvKey = InvUtils.getSystemByName( lEngine, SYSTEM_NAME );

      // Create a fault against the system
      FaultKey lFault = Domain.createFault( new DomainConfiguration<Fault>() {

         @Override
         public void configure( Fault aFault ) {
            aFault.setFoundOnDate( lFaultFoundBeforeFlightDate );
            aFault.setInventory( lEngineSysInvKey );
            aFault.addUsageSnapshot(
                  new UsageSnapshot( lEngineSysInvKey, HOURS, HOURS_AT_FAULT_FOUND ) );
            aFault.addUsageSnapshot( new UsageSnapshot( lEngineSysInvKey,
                  lDataType_CALC_PARM_FOR_HOURS, CALC_PARM_HOURS_AT_FAULT_FOUND ) );
            aFault.addUsageSnapshot(
                  new UsageSnapshot( lEngineSysInvKey, CYCLES, CYCLES_AT_FAULT_FOUND ) );
            aFault.addUsageSnapshot( new UsageSnapshot( lEngineSysInvKey,
                  lDataType_CALC_PARM_FOR_CYCLES, CALC_PARM_CYCLES_AT_FAULT_FOUND ) );
         }
      } );

      //
      // When a historic flight is created with usage deltas for the aircraft
      //
      BigDecimal lHoursDelta = new BigDecimal( 4 );
      BigDecimal lCyclesDelta = new BigDecimal( 2 );
      CollectedUsageParm[] lUsageParms = { generateFlightUsage( lAircraft, HOURS, lHoursDelta ),
            generateFlightUsage( lAircraft, CYCLES, lCyclesDelta ),
            generateFlightUsage( lEngine, HOURS, lHoursDelta ),
            generateFlightUsage( lEngine, CYCLES, lCyclesDelta ) };

      Date lActualDepartureDate = DateUtils.addHours( FLIGHT_DATE, -lHoursDelta.intValue() );

      iFlightHistBean.createHistFlight( new AircraftKey( lAircraft ), lHrKey,
            generateFlightInfoTO( FLIGHT_NAME, lActualDepartureDate, FLIGHT_DATE ), lUsageParms,
            NO_MEASUREMENTS );

      //
      // Then
      //

      Double lActualCalcParmForCycles =
            getCalcParmFaultFoundUsageTsn( lFault, lDataType_CALC_PARM_FOR_CYCLES );
      Double lActualCalcParmForHours =
            getCalcParmFaultFoundUsageTsn( lFault, lDataType_CALC_PARM_FOR_HOURS );

      Double lExpectedCalcParmForCycles = CALC_PARM_CYCLES_AT_FAULT_FOUND.doubleValue();
      Double lExpectedCalcParmForHours = CALC_PARM_HOURS_AT_FAULT_FOUND.doubleValue();

      {
         // Important!
         // All data needed for assertions must be retrieved prior to calling
         // dropEquationFunctionToDatabase(). As it performs an explicit roll-back.
         dropEquationFunctionToDatabase();
      }

      assertEquals( "Unexpected value for:" + CALC_PARM_DATA_TYPE_FOR_CYCLE,
            lExpectedCalcParmForCycles, lActualCalcParmForCycles );
      assertEquals( "Unexpected value for:" + CALC_PARM_DATA_TYPE_FOR_HOURS,
            lExpectedCalcParmForHours, lActualCalcParmForHours );
   }


   /**
    *
    * Verify that calculated parameters of a usage snapshot for a fault, against a TRK sub-component
    * of an engine installed on an aircraft, are recalculated when a historical flight is created
    * prior to the fault.
    *
    * <pre>
    *    Given a SYS on an engine that is installed on an aircraft
    *      And the engine and SYS are tracking usages and a calculated parameter
    *      And a fault raised (and left open) against the SYS
    *     When a historical flight is created before the fault found date
    *     Then the calculated parameters of the fault snapshot are recalculated
    * </pre>
    *
    */
   @Test
   public void itRecalculatesCalcParmForSubAssySystemWhenFlightCreatedBeforeFaultFoundDate()
         throws Exception {

      // Important!
      // addEquationFunctionToDatabase() needs to be called prior to any data setup in database
      // as the method performs an explicit roll-back.
      addEquationFunctionToDatabase();

      // Given a SYS on an engine that is installed on an aircraft
      // and the engine and SYS are tracking usages and a calculated parameter.
      final AssemblyKey lEngineAssy =
            Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

               @Override
               public void configure( EngineAssembly aBuilder ) {
                  aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot aBuilder ) {
                        // Add a SYS config slot to the root config slot.
                        aBuilder
                              .addConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                                 @Override
                                 public void configure( ConfigurationSlot aBuilder ) {
                                    aBuilder.setConfigurationSlotClass( SYS );
                                    aBuilder.setCode( SYS_CONFIG_SLOT_CODE );
                                 }
                              } );
                        // Add a usage parameter and a calculated parameter based on that usage
                        // parameter.
                        aBuilder.addUsageParameter( HOURS );
                        aBuilder.addCalculatedUsageParameter(
                              new DomainConfiguration<CalculatedUsageParameter>() {

                                 @Override
                                 public void configure( CalculatedUsageParameter aBuilder ) {
                                    aBuilder.setCode( CALC_PARM_DATA_TYPE_FOR_HOURS );
                                    aBuilder.setDatabaseCalculation( CALC_PARM_FUNCTION_NAME );
                                    aBuilder.setPrecisionQt( 2 );

                                    // Add the constant and parameter in the order in which the
                                    // function is expecting them.
                                    aBuilder.addConstant( CALC_PARM_CONSTANT_NAME,
                                          CALC_PARM_CONSTANT );
                                    aBuilder.addParameter( HOURS );
                                 }

                              } );
                     }
                  } );
               }
            } );

      ConfigSlotKey lRootConfigSlot = Domain.readRootConfigurationSlot( lEngineAssy );
      final ConfigSlotPositionKey lSysConfigSlotPos =
            getConfigSlotPos( lRootConfigSlot, SYS_CONFIG_SLOT_CODE );
      final DataTypeKey lCalcParmKey =
            Domain.readUsageParameter( lRootConfigSlot, CALC_PARM_DATA_TYPE_FOR_HOURS );

      final InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.addUsage( HOURS, CURRENT_HOURS );
            aEngine.addUsage( lCalcParmKey, CURRENT_CALC_PARM_HOURS );
            aEngine.addSystem( new DomainConfiguration<System>() {

               @Override
               public void configure( System aBuilder ) {
                  aBuilder.setName( SYSTEM_NAME );
                  aBuilder.setPosition( lSysConfigSlotPos );
                  aBuilder.addUsage( HOURS, CURRENT_HOURS );
                  aBuilder.addUsage( lCalcParmKey, CURRENT_CALC_PARM_HOURS );
               }
            } );
         }

      } );

      final InventoryKey lEngineSys = InvUtils.getSystemByName( lEngine, SYSTEM_NAME );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aBuilder ) {
            aBuilder.addEngine( lEngine );
         }
      } );

      // Given a fault raised (and left open) against the SYS.
      FaultKey lFault = Domain.createFault( new DomainConfiguration<Fault>() {

         @Override
         public void configure( Fault aFault ) {
            aFault.setFoundOnDate( FAULT_DATE );
            aFault.setInventory( lEngineSys );
            aFault.addUsageSnapshot( new UsageSnapshot( lEngineSys, HOURS, HOURS_AT_FAULT_FOUND ) );
            aFault.addUsageSnapshot(
                  new UsageSnapshot( lEngineSys, lCalcParmKey, CALC_PARM_HOURS_AT_FAULT_FOUND ) );
         }
      } );

      // When a historical flight is created before the fault found date
      // (arrival date before fault date).
      BigDecimal lHoursDelta = new BigDecimal( 4 );
      Date lArrivalDate = DateUtils.addDays( FAULT_DATE, -10 );
      Date lDepartureDate = DateUtils.addHours( lArrivalDate, -lHoursDelta.intValue() );

      CollectedUsageParm[] lUsageParms = { generateFlightUsage( lEngine, HOURS, lHoursDelta ) };

      iFlightHistBean.createHistFlight( new AircraftKey( lAircraft ), createHumanResource(),
            generateFlightInfoTO( FLIGHT_NAME, lDepartureDate, lArrivalDate ), lUsageParms,
            NO_MEASUREMENTS );

      // Then the calculated parameters of the fault snapshot are recalculated
      Double lActualCalcParm = getCalcParmFaultFoundUsageTsn( lFault, lCalcParmKey );
      Double lExpectedCalcParm = CALC_PARM_HOURS_AT_FAULT_FOUND
            .add( lHoursDelta.multiply( CALC_PARM_CONSTANT ) ).doubleValue();

      {
         // Important!
         // All data needed for assertions must be retrieved prior to calling
         // dropEquationFunctionToDatabase(). As it performs an explicit roll-back.
         dropEquationFunctionToDatabase();
      }

      assertEquals( "Unexpected value for calc parm: " + CALC_PARM_DATA_TYPE_FOR_HOURS,
            lExpectedCalcParm, lActualCalcParm );

   }


   @Before
   public void setup() {
      iFlightHistBean = new FlightHistBean();
      iFlightHistBean.ejbCreate();
      iFlightHistBean.setSessionContext( new SessionContextFake() );
   }


   @After
   public void teardown() {
      iFlightHistBean.setSessionContext( null );
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


   private void addEquationFunctionToDatabase() throws SQLException {

      // Function creation is DDL which implicitly commits the transaction.
      // We perform explicit roll-back before function creation ensuring no data gets committed
      // accidentally.
      String lCreateFunctionStatement = "CREATE OR REPLACE FUNCTION " + CALC_PARM_FUNCTION_NAME
            + " (" + "aConstant NUMBER, aHoursInput NUMBER" + " )" + " RETURN NUMBER" + " " + "IS "
            + "result NUMBER; " + "BEGIN" + " " + "result := aConstant * aHoursInput ; " + "RETURN"
            + " " + " result;" + "END" + " " + CALC_PARM_FUNCTION_NAME + " ;";

      Domain.createCalculatedParameterEquationFunction( iDatabaseConnectionRule.getConnection(),
            lCreateFunctionStatement );
   }


   private void dropEquationFunctionToDatabase() throws SQLException {

      // Function dropping is DDL which implicitly commits transaction.
      // We perform explicit rollback before function drop ensuring no data gets committed
      // accidentally.
      Domain.dropCalculatedParameterEquationFunction( iDatabaseConnectionRule.getConnection(),
            CALC_PARM_FUNCTION_NAME );
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


   private HumanResourceKey createHumanResource() {
      HumanResourceKey lHrKey = new HumanResourceDomainBuilder().withUsername( HR_USERNAME ).build();

      int lUserId = OrgHr.findByPrimaryKey( lHrKey ).getUserId();
      UserParametersFake lUserParametersFake = new UserParametersFake( lUserId, "LOGIC" );
      lUserParametersFake.setProperty( "MAX_FLIGHT_DAYS_IN_THE_PAST_VALUE", "150" );
      UserParameters.setInstance( lUserId, "LOGIC", lUserParametersFake );
      return lHrKey;
   }


   private AssemblyKey createAcftAssyTrackingUsageParmsAndCalcParms() {
      AssemblyKey lAircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aBuilder ) {
                  aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot aBuilder ) {
                        // Add a TRK config slot to the root config slot.
                        aBuilder
                              .addConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                                 @Override
                                 public void configure( ConfigurationSlot aBuilder ) {
                                    aBuilder.setConfigurationSlotClass( TRK );
                                    aBuilder.setCode( TRK_CONFIG_SLOT_CODE );
                                 }
                              } );
                        // Track a parameter.
                        aBuilder.addUsageParameter( HOURS );
                        aBuilder.addUsageParameter( CYCLES );

                        // Track the calculate parameter based on the parameter HOURS.
                        aBuilder.addCalculatedUsageParameter(
                              new DomainConfiguration<CalculatedUsageParameter>() {

                                 @Override
                                 public void configure( CalculatedUsageParameter aBuilder ) {
                                    aBuilder.setCode( CALC_PARM_DATA_TYPE_FOR_HOURS );
                                    aBuilder.setDatabaseCalculation( CALC_PARM_FUNCTION_NAME );
                                    aBuilder.setPrecisionQt( 2 );

                                    // Add the constant and parameter in the order in which the
                                    // function is expecting them.
                                    aBuilder.addConstant( CALC_PARM_CONSTANT_NAME,
                                          CALC_PARM_CONSTANT );
                                    aBuilder.addParameter( HOURS );
                                 }

                              } );
                        // Track the calculate parameter based on the parameter CYCLES.
                        aBuilder.addCalculatedUsageParameter(
                              new DomainConfiguration<CalculatedUsageParameter>() {

                                 @Override
                                 public void configure( CalculatedUsageParameter aBuilder ) {
                                    aBuilder.setCode( CALC_PARM_DATA_TYPE_FOR_CYCLE );
                                    aBuilder.setDatabaseCalculation( CALC_PARM_FUNCTION_NAME );
                                    aBuilder.setPrecisionQt( 0 );

                                    // Add the constant and parameter in the order in which the
                                    // function is expecting them.
                                    aBuilder.addConstant( CALC_PARM_CONSTANT_NAME,
                                          CALC_PARM_CONSTANT );
                                    aBuilder.addParameter( CYCLES );
                                 }

                              } );
                     }
                  } );
               }
            } );
      return lAircraftAssembly;
   }


   private Double getCalcParmFaultFoundUsageTsn( FaultKey aFault,
         DataTypeKey aCalcParmDataTypeKey ) {
      EventInventoryKey lEvtInvKey = new EventInventoryKey( aFault.getEventKey(), 1 );

      EventInventoryUsageKey lEventInventoryUsageKey =
            new EventInventoryUsageKey( lEvtInvKey, aCalcParmDataTypeKey );
      EvtInvUsage lEvtInvUsage = EvtInvUsage.findByPrimaryKey( lEventInventoryUsageKey );
      return lEvtInvUsage.getTsnQt();
   }


   private ConfigSlotPositionKey getConfigSlotPos( ConfigSlotKey aParentConfigSlot,
         String aConfigSlotCode ) {

      ConfigSlotKey lConfigSlot =
            Domain.readSubConfigurationSlot( aParentConfigSlot, aConfigSlotCode );

      return new ConfigSlotPositionKey( lConfigSlot, EqpAssmblPos.getFirstPosId( lConfigSlot ) );
   }

}
