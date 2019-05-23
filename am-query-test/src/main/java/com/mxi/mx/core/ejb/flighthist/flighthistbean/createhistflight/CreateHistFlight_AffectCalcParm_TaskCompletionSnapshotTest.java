package com.mxi.mx.core.ejb.flighthist.flighthistbean.createhistflight;

import static com.mxi.mx.core.key.DataTypeKey.HOURS;
import static com.mxi.mx.core.key.RefBOMClassKey.SYS;
import static com.mxi.mx.core.key.RefEventStatusKey.COMPLETE;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;

import org.junit.After;
import org.junit.Assert;
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
import com.mxi.am.domain.Location;
import com.mxi.am.domain.Requirement;
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
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.EventKeyInterface;
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
import com.mxi.mx.core.table.eqp.EqpAssmblPos;
import com.mxi.mx.core.table.eqp.EqpDataSourceSpec;
import com.mxi.mx.core.table.evt.EvtInvTable;
import com.mxi.mx.core.table.evt.EvtInvUsageTable;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.org.OrgHr;


/**
 * Tests for {@linkplain FlightHistBean#createHistFlight} that involve updating calculated
 * parameters within usage snapshots for task usage at completion
 */
public class CreateHistFlight_AffectCalcParm_TaskCompletionSnapshotTest {

   private static final String HR_USERNAME = "HR_USERNAME";
   private static final String FLIGHT_NAME = "FLIGHT_NAME";

   private static final String TRK_CONFIG_SLOT_CODE = "TRK_CS";
   private static final String SUBASSY_CONFIG_SLOT_CODE = "SUBA_CS";
   private static final String SYS_CONFIG_SLOT_CODE = "SYS_CS";

   private static final String SYSTEM_NAME = "SYSTEM_NAME";

   private static final Date TASK_DATE_BEFORE_FLIGHT = DateUtils.addDays( new Date(), -101 );

   // Calculated parameter data types, equation, and constant.
   // The equation is simply a multiplier of the target usage parameter by the multiplier constant.
   private static final String MUTIPLIER_DATA_TYPE_FOR_HOURS = "MUTIPLIER_DATA_TYPE_FOR_HOURS";
   private static final String MULTIPLIER_FUNCTION_NAME = "MULTIPLIER_FUNCTION_NAME";
   private static final String MULTIPLIER_CONSTANT_NAME = "MULTIPLIER_CONSTANT_NAME";
   private static final BigDecimal MULTIPLIER_CONSTANT = BigDecimal.valueOf( 2.0 );

   // Task's usage-at-completion.
   private static final BigDecimal COMPLETION_HOURS = BigDecimal.valueOf( 7.8 );
   private static final BigDecimal COMPLETION_HOURS_CALC_PARM =
         COMPLETION_HOURS.multiply( MULTIPLIER_CONSTANT );

   // Edited flight usage and difference from the original.
   private static final BigDecimal EDITED_FLIGHT_HOURS = BigDecimal.valueOf( 10.11 );

   private static final Date NEW_DATE = new Date();
   private static final Measurement[] NO_MEASUREMENTS = new Measurement[] {};

   private FlightHistBean iFlightHistBean;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   //
   // Given an aircraft tracking parameters of HOURS and calculated-parameter (which
   // are based on
   // the
   // calculate parameter HOURS).
   //
   // And The calculated parameter will be called MULTIPLY_DATA_TYPE_CODE_FOR_HOURS
   // And the equation is input * MULTIPLY_TIMES
   // And The task is raised on aircraft
   // And set up the parameter and calculated-parameter on the root config slot.

   // When creating a flight before the task completion date


   // Then it will re-calculate the calculate parameters on the task completion snap shot
   @Test
   public void itRecalculatesCalcParamWhenTaskOnAircraftCompletedAfterFlight() throws Exception {
      addEquationFunctionToDatabase();
      final AssemblyKey lAcftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aBuilder ) {
                  aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot aBuilder ) {
                        // Track a parameter.
                        aBuilder.addUsageParameter( HOURS );
                        aBuilder.addCalculatedUsageParameter(
                              new DomainConfiguration<CalculatedUsageParameter>() {

                                 @Override
                                 public void configure( CalculatedUsageParameter aBuilder ) {
                                    aBuilder.setCode( MUTIPLIER_DATA_TYPE_FOR_HOURS );
                                    aBuilder.setDatabaseCalculation( MULTIPLIER_FUNCTION_NAME );
                                    aBuilder.setPrecisionQt( 2 );

                                    // Add the constant and parameter in the order in which the
                                    // function is expecting them.
                                    aBuilder.addConstant( MULTIPLIER_CONSTANT_NAME,
                                          MULTIPLIER_CONSTANT );
                                    aBuilder.addParameter( HOURS );
                                 }

                              } );
                     }
                  } );
               }
            } );
      final DataTypeKey calcParmKey = Domain.readUsageParameter(
            Domain.readRootConfigurationSlot( lAcftAssembly ), MUTIPLIER_DATA_TYPE_FOR_HOURS );
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aBuilder ) {
            aBuilder.setAssembly( lAcftAssembly );
            aBuilder.addUsage( HOURS, BigDecimal.ZERO );
            aBuilder.addUsage( calcParmKey, BigDecimal.ZERO );
         }
      } );

      final UsageSnapshot lUsageSnapShotForHours =
            new UsageSnapshot( lAircraft, HOURS, COMPLETION_HOURS );

      final TaskKey lRequirement =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement aRequirement ) {
                  aRequirement.addUsage( new UsageSnapshot( lAircraft,
                        Domain.readUsageParameter(
                              Domain.readRootConfigurationSlot( lAcftAssembly ),
                              MUTIPLIER_DATA_TYPE_FOR_HOURS ),
                        COMPLETION_HOURS_CALC_PARM ) );
                  aRequirement.addUsage( lUsageSnapShotForHours );

                  aRequirement.setInventory( lAircraft );
                  aRequirement.setActualStartDate( NEW_DATE );
                  aRequirement.setActualEndDate( NEW_DATE );
                  aRequirement.setStatus( RefEventStatusKey.COMPLETE );
               }
            } );

      FlightInformationTO lFlightInfoTO = generateFlightInfoTO( FLIGHT_NAME, NEW_DATE, NEW_DATE );
      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( lAircraft, HOURS, EDITED_FLIGHT_HOURS ) };

      iFlightHistBean.createHistFlight( new AircraftKey( lAircraft ), createHumanResource(),
            lFlightInfoTO, lEditUsageParms, NO_MEASUREMENTS );

      BigDecimal lExpected =
            COMPLETION_HOURS.add( EDITED_FLIGHT_HOURS ).multiply( MULTIPLIER_CONSTANT );
      BigDecimal lAcutual = readActualUsageParmValue( lRequirement.getEventKey(), lAcftAssembly );

      {
         // Important!
         // All data needed for assertions must be retrieved prior to calling
         // dropEquationFunctionToDatabase(). As it performs an explicit roll-back.
         //
         // ALSO, all assertions must be done after dropEquationFunctionToDatabase() to ensure the
         // equation gets removed from the DB.
         dropEquationFunctionToDatabase();
      }

      Assert.assertTrue( "Unexpected value for " + MUTIPLIER_DATA_TYPE_FOR_HOURS + "; expected="
            + lExpected + " , actual=" + lAcutual, lExpected.compareTo( lAcutual ) == 0 );

   }

   //
   // Given an aircraft tracking parameters of HOURS and calculated-parameter (which
   // are based on
   // the
   // calculate parameter HOURS).
   //
   // And the calculated parameter will be called MULTIPLY_DATA_TYPE_CODE_FOR_HOURS
   // And the task is raise against aircraft system
   // And the equation is input * MULTIPLY_TIMES
   // And set up the parameter and calculated-parameter on the root config slot.

   // When creating a flight before the task completion date


   // Then it will re-calculate the calculate parameters on the task completion snap shot
   @Test
   public void itRecalculatesCalcParamWhenTaskOnAircraftSystemCompletedAfterFlight()
         throws Exception {
      addEquationFunctionToDatabase();
      final AssemblyKey lAcftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aBuilder ) {
                  aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot aBuilder ) {
                        // Add a SYS config slot to the root config slot.
                        aBuilder
                              .addConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                                 @Override
                                 public void configure( ConfigurationSlot aBuilder ) {
                                    aBuilder.setCode( SYS_CONFIG_SLOT_CODE );
                                 }
                              } );
                        // Track a parameter.
                        aBuilder.addUsageParameter( HOURS );
                        aBuilder.addCalculatedUsageParameter(
                              new DomainConfiguration<CalculatedUsageParameter>() {

                                 @Override
                                 public void configure( CalculatedUsageParameter aBuilder ) {
                                    aBuilder.setCode( MUTIPLIER_DATA_TYPE_FOR_HOURS );
                                    aBuilder.setDatabaseCalculation( MULTIPLIER_FUNCTION_NAME );
                                    aBuilder.setPrecisionQt( 2 );

                                    // Add the constant and parameter in the order in which the
                                    // function is expecting them.
                                    aBuilder.addConstant( MULTIPLIER_CONSTANT_NAME,
                                          MULTIPLIER_CONSTANT );
                                    aBuilder.addParameter( HOURS );
                                 }

                              } );
                     }
                  } );
               }
            } );

      ConfigSlotKey lRootConfigSlot = Domain.readRootConfigurationSlot( lAcftAssembly );
      final ConfigSlotPositionKey lSysConfigSlotPos =
            getConfigSlotPos( lRootConfigSlot, SYS_CONFIG_SLOT_CODE );
      final DataTypeKey lCalcParmKey =
            Domain.readUsageParameter( lRootConfigSlot, MUTIPLIER_DATA_TYPE_FOR_HOURS );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aBuilder ) {
            aBuilder.setAssembly( lAcftAssembly );
            aBuilder.addUsage( HOURS, BigDecimal.ZERO );
            aBuilder.addUsage( lCalcParmKey, BigDecimal.ZERO );
            aBuilder.addSystem( new DomainConfiguration<System>() {

               @Override
               public void configure( System aBuilder ) {
                  aBuilder.setName( SYSTEM_NAME );
                  aBuilder.setPosition( lSysConfigSlotPos );
                  aBuilder.addUsage( HOURS, BigDecimal.ZERO );
                  aBuilder.addUsage( lCalcParmKey, BigDecimal.ZERO );
               }
            } );
         }
      } );

      final InventoryKey lAircraftSystem = Domain.readSystem( lAircraft, SYSTEM_NAME );

      final UsageSnapshot lUsageSnapShotForHours =
            new UsageSnapshot( lAircraftSystem, HOURS, COMPLETION_HOURS );

      final TaskKey lRequirement =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement aRequirement ) {
                  aRequirement.addUsage( new UsageSnapshot( lAircraftSystem,
                        Domain.readUsageParameter(
                              Domain.readRootConfigurationSlot( lAcftAssembly ),
                              MUTIPLIER_DATA_TYPE_FOR_HOURS ),
                        COMPLETION_HOURS_CALC_PARM ) );
                  aRequirement.addUsage( lUsageSnapShotForHours );

                  aRequirement.setInventory( lAircraftSystem );
                  aRequirement.setActualStartDate( NEW_DATE );
                  aRequirement.setActualEndDate( NEW_DATE );
                  aRequirement.setStatus( RefEventStatusKey.COMPLETE );
               }
            } );

      FlightInformationTO lFlightInfoTO = generateFlightInfoTO( FLIGHT_NAME, NEW_DATE, NEW_DATE );
      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( lAircraft, HOURS, EDITED_FLIGHT_HOURS ) };

      iFlightHistBean.createHistFlight( new AircraftKey( lAircraft ), createHumanResource(),
            lFlightInfoTO, lEditUsageParms, NO_MEASUREMENTS );

      BigDecimal lExpected =
            COMPLETION_HOURS.add( EDITED_FLIGHT_HOURS ).multiply( MULTIPLIER_CONSTANT );
      BigDecimal lAcutual = readActualUsageParmValue( lRequirement.getEventKey(), lAcftAssembly );

      {
         // Important!
         // All data needed for assertions must be retrieved prior to calling
         // dropEquationFunctionToDatabase(). As it performs an explicit roll-back.
         //
         // ALSO, all assertions must be done after dropEquationFunctionToDatabase() to ensure the
         // equation gets removed from the DB.
         dropEquationFunctionToDatabase();
      }

      Assert.assertTrue( "Unexpected value for " + MUTIPLIER_DATA_TYPE_FOR_HOURS + "; expected="
            + lExpected + " , actual=" + lAcutual, lExpected.compareTo( lAcutual ) == 0 );

   }

   //
   // Given an aircraft tracking parameters of HOURS and calculated-parameter (which
   // are based on
   // the
   // calculate parameter HOURS).
   //
   // And the calculated parameter will be called MULTIPLY_DATA_TYPE_CODE_FOR_HOURS
   // And the equation is input * MULTIPLY_TIMES
   // And the task is raised on aircraft
   // And set up the parameter and calculated-parameter on the root config slot.

   // When creating a flight after the task completion date


   // Then it will not re-calculate the calculate parameters on the task completion snap shot
   @Test
   public void itDoesNotRecalculateCalcParamWhenTaskOnAircraftCompletedBeforeFlight()
         throws Exception {
      addEquationFunctionToDatabase();
      final AssemblyKey lAcftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aBuilder ) {
                  aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot aBuilder ) {
                        // Track a parameter.
                        aBuilder.addUsageParameter( HOURS );
                        aBuilder.addCalculatedUsageParameter(
                              new DomainConfiguration<CalculatedUsageParameter>() {

                                 @Override
                                 public void configure( CalculatedUsageParameter aBuilder ) {
                                    aBuilder.setCode( MUTIPLIER_DATA_TYPE_FOR_HOURS );
                                    aBuilder.setDatabaseCalculation( MULTIPLIER_FUNCTION_NAME );
                                    aBuilder.setPrecisionQt( 2 );

                                    // Add the constant and parameter in the order in which the
                                    // function is expecting them.
                                    aBuilder.addConstant( MULTIPLIER_CONSTANT_NAME,
                                          MULTIPLIER_CONSTANT );
                                    aBuilder.addParameter( HOURS );
                                 }

                              } );
                     }
                  } );
               }
            } );
      final DataTypeKey calcParmKey = Domain.readUsageParameter(
            Domain.readRootConfigurationSlot( lAcftAssembly ), MUTIPLIER_DATA_TYPE_FOR_HOURS );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aBuilder ) {
            aBuilder.setAssembly( lAcftAssembly );
            aBuilder.addUsage( HOURS, BigDecimal.ZERO );
            aBuilder.addUsage( calcParmKey, BigDecimal.ZERO );
         }
      } );

      final UsageSnapshot lUsageSnapShotForHours =
            new UsageSnapshot( lAircraft, HOURS, COMPLETION_HOURS );

      final TaskKey lRequirement =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement aRequirement ) {
                  aRequirement.addUsage( new UsageSnapshot( lAircraft,
                        Domain.readUsageParameter(
                              Domain.readRootConfigurationSlot( lAcftAssembly ),
                              MUTIPLIER_DATA_TYPE_FOR_HOURS ),
                        COMPLETION_HOURS_CALC_PARM ) );
                  aRequirement.addUsage( lUsageSnapShotForHours );

                  aRequirement.setInventory( lAircraft );
                  aRequirement.setActualStartDate( TASK_DATE_BEFORE_FLIGHT );
                  aRequirement.setActualEndDate( TASK_DATE_BEFORE_FLIGHT );
                  aRequirement.setStatus( RefEventStatusKey.COMPLETE );
               }
            } );

      FlightInformationTO lFlightInfoTO = generateFlightInfoTO( FLIGHT_NAME, NEW_DATE, NEW_DATE );
      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( lAircraft, HOURS, EDITED_FLIGHT_HOURS ) };

      iFlightHistBean.createHistFlight( new AircraftKey( lAircraft ), createHumanResource(),
            lFlightInfoTO, lEditUsageParms, NO_MEASUREMENTS );

      BigDecimal lExpected = COMPLETION_HOURS.multiply( MULTIPLIER_CONSTANT );
      BigDecimal lAcutual = readActualUsageParmValue( lRequirement.getEventKey(), lAcftAssembly );

      {
         // Important!
         // All data needed for assertions must be retrieved prior to calling
         // dropEquationFunctionToDatabase(). As it performs an explicit roll-back.
         //
         // ALSO, all assertions must be done after dropEquationFunctionToDatabase() to ensure the
         // equation gets removed from the DB.
         dropEquationFunctionToDatabase();
      }

      Assert.assertTrue( "Unexpected value for " + MUTIPLIER_DATA_TYPE_FOR_HOURS + "; expected="
            + lExpected + " , actual=" + lAcutual, lExpected.compareTo( lAcutual ) == 0 );

   }

   //
   // Given an aircraft tracking parameters of HOURS and calculated-parameter (which
   // are based on
   // the
   // calculate parameter HOURS).
   //
   // And the calculated parameter will be called MULTIPLY_DATA_TYPE_CODE_FOR_HOURS
   // And the task is raised on engine
   // And the equation is input * MULTIPLY_TIMES
   // And set up the parameter and calculated-parameter on the root config slot.

   // When creating a flight before the task completion date


   // Then it will re-calculate the calculate parameters on the task completion snap shot
   @Test
   public void itRecalculatesCalcParamWhenTaskOnEngineCompeletedAfterFlight() throws Exception {
      addEquationFunctionToDatabase();
      final AssemblyKey lAcftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aBuilder ) {
                  aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot aBuilder ) {
                        // Add a sub-assembly config slot to the root config slot, for an engine.
                        aBuilder
                              .addConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                                 @Override
                                 public void configure( ConfigurationSlot aBuilder ) {
                                    aBuilder.setCode( SUBASSY_CONFIG_SLOT_CODE );
                                 }
                              } );
                        // Add a usage parameter and a calculated parameter based on that usage
                        // parameter.
                        aBuilder.addUsageParameter( HOURS );
                        aBuilder.addCalculatedUsageParameter(
                              new DomainConfiguration<CalculatedUsageParameter>() {

                                 @Override
                                 public void configure( CalculatedUsageParameter aBuilder ) {
                                    aBuilder.setCode( MUTIPLIER_DATA_TYPE_FOR_HOURS );
                                    aBuilder.setDatabaseCalculation( MULTIPLIER_FUNCTION_NAME );
                                    aBuilder.setPrecisionQt( 2 );

                                    // Add the constant and parameter in the order in which the
                                    // function is expecting them.
                                    aBuilder.addConstant( MULTIPLIER_CONSTANT_NAME,
                                          MULTIPLIER_CONSTANT );
                                    aBuilder.addParameter( HOURS );
                                 }

                              } );
                     }
                  } );
               }
            } );

      ConfigSlotKey lRootConfigSlot = Domain.readRootConfigurationSlot( lAcftAssembly );
      final ConfigSlotPositionKey lSubAssyConfigSlotPos =
            getConfigSlotPos( lRootConfigSlot, SUBASSY_CONFIG_SLOT_CODE );
      final DataTypeKey calcParmKey =
            Domain.readUsageParameter( lRootConfigSlot, MUTIPLIER_DATA_TYPE_FOR_HOURS );

      final InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aBuilder ) {
            aBuilder.setPosition( lSubAssyConfigSlotPos );
            aBuilder.addUsage( HOURS, BigDecimal.ZERO );
            aBuilder.addUsage( calcParmKey, BigDecimal.ZERO );

         }

      } );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aBuilder ) {
            aBuilder.setAssembly( lAcftAssembly );
            aBuilder.addEngine( lEngine );
            aBuilder.addSystem( "myAircraftSystem" );
            aBuilder.addUsage( HOURS, BigDecimal.ZERO );
            aBuilder.addUsage( calcParmKey, BigDecimal.ZERO );
         }
      } );

      final UsageSnapshot lUsageSnapShotForHours =
            new UsageSnapshot( lEngine, HOURS, COMPLETION_HOURS );

      final TaskKey lRequirement =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement aRequirement ) {
                  aRequirement.addUsage( new UsageSnapshot( lEngine,
                        Domain.readUsageParameter(
                              Domain.readRootConfigurationSlot( lAcftAssembly ),
                              MUTIPLIER_DATA_TYPE_FOR_HOURS ),
                        COMPLETION_HOURS_CALC_PARM ) );
                  aRequirement.addUsage( lUsageSnapShotForHours );

                  aRequirement.setInventory( lEngine );
                  aRequirement.setActualStartDate( NEW_DATE );
                  aRequirement.setActualEndDate( NEW_DATE );
                  aRequirement.setStatus( RefEventStatusKey.COMPLETE );
               }
            } );

      FlightInformationTO lFlightInfoTO = generateFlightInfoTO( FLIGHT_NAME, NEW_DATE, NEW_DATE );
      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( lAircraft, HOURS, EDITED_FLIGHT_HOURS ),
                  generateFlightUsage( lEngine, HOURS, EDITED_FLIGHT_HOURS ) };

      iFlightHistBean.createHistFlight( new AircraftKey( lAircraft ), createHumanResource(),
            lFlightInfoTO, lEditUsageParms, NO_MEASUREMENTS );

      BigDecimal lExpected =
            COMPLETION_HOURS.add( EDITED_FLIGHT_HOURS ).multiply( MULTIPLIER_CONSTANT );
      BigDecimal lAcutual = readActualUsageParmValue( lRequirement.getEventKey(), lAcftAssembly );

      {
         // Important!
         // All data needed for assertions must be retrieved prior to calling
         // dropEquationFunctionToDatabase(). As it performs an explicit roll-back.
         //
         // ALSO, all assertions must be done after dropEquationFunctionToDatabase() to ensure the
         // equation gets removed from the DB.
         dropEquationFunctionToDatabase();
      }

      Assert.assertTrue( "Unexpected value for " + MUTIPLIER_DATA_TYPE_FOR_HOURS + "; expected="
            + lExpected + " , actual=" + lAcutual, lExpected.compareTo( lAcutual ) == 0 );

   }


   /**
    *
    * Verify that calculated parameters of a usage snapshot for a task, against a SYS of an engine
    * installed on an aircraft, are recalculated when a historical flight is created prior to the
    * task.
    *
    * <pre>
    *    Given a SYS on an engine that is installed on an aircraft
    *      And the engine and SYS are tracking usages and a calculated parameter
    *      And there is completed task against the SYS
    *     When a historical flight is created before the task's start date
    *     Then the calculated parameters of the fault snapshot are recalculated
    * </pre>
    *
    */
   @Test
   public void itRecalculatesCalcParamWhenTaskOnEngineSystemCompletedAfterFlight()
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
                                    aBuilder.setCode( MUTIPLIER_DATA_TYPE_FOR_HOURS );
                                    aBuilder.setDatabaseCalculation( MULTIPLIER_FUNCTION_NAME );
                                    aBuilder.setPrecisionQt( 2 );

                                    // Add the constant and parameter in the order in which the
                                    // function is expecting them.
                                    aBuilder.addConstant( MULTIPLIER_CONSTANT_NAME,
                                          MULTIPLIER_CONSTANT );
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
            Domain.readUsageParameter( lRootConfigSlot, MUTIPLIER_DATA_TYPE_FOR_HOURS );

      final InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.addUsage( HOURS, BigDecimal.ZERO );
            aEngine.addUsage( lCalcParmKey, BigDecimal.ZERO );
            aEngine.addSystem( new DomainConfiguration<System>() {

               @Override
               public void configure( System aBuilder ) {
                  aBuilder.setName( SYSTEM_NAME );
                  aBuilder.setPosition( lSysConfigSlotPos );
                  aBuilder.addUsage( HOURS, BigDecimal.ZERO );
                  aBuilder.addUsage( lCalcParmKey, BigDecimal.ZERO );
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

      // Given there is completed task against the SYS
      final Date lTaskStartDate = DateUtils.addDays( new Date(), -10 );
      final Date lTaskEndDate = DateUtils.addDays( lTaskStartDate, 1 );

      final TaskKey lRequirement =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement aRequirement ) {
                  aRequirement.setInventory( lEngineSys );
                  aRequirement.setActualStartDate( lTaskStartDate );
                  aRequirement.setActualEndDate( lTaskEndDate );
                  aRequirement.setStatus( COMPLETE );

                  aRequirement.addUsage( new UsageSnapshot( lEngineSys, HOURS, COMPLETION_HOURS ) );
                  aRequirement.addUsage(
                        new UsageSnapshot( lEngineSys, lCalcParmKey, COMPLETION_HOURS_CALC_PARM ) );
               }
            } );

      // When a historical flight is created before the task's start date
      final Date lFlightDepartureDate = DateUtils.addDays( lTaskStartDate, -10 );
      final Date lFlightArrivalDate =
            DateUtils.addTime( lFlightDepartureDate, EDITED_FLIGHT_HOURS.doubleValue() );

      FlightInformationTO lFlightInfoTO =
            generateFlightInfoTO( FLIGHT_NAME, lFlightDepartureDate, lFlightArrivalDate );

      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( lEngine, HOURS, EDITED_FLIGHT_HOURS ) };

      iFlightHistBean.createHistFlight( new AircraftKey( lAircraft ), createHumanResource(),
            lFlightInfoTO, lEditUsageParms, NO_MEASUREMENTS );

      // Then the calculated parameters of the fault snapshot are recalculated
      BigDecimal lExpected =
            COMPLETION_HOURS.add( EDITED_FLIGHT_HOURS ).multiply( MULTIPLIER_CONSTANT );
      BigDecimal lActual = getEventInventoryUsage( lRequirement, lEngineSys, lCalcParmKey );

      {
         // Important!
         // All data needed for assertions must be retrieved prior to calling
         // dropEquationFunctionToDatabase(). As it performs an explicit roll-back.
         //
         // ALSO, all assertions must be done after dropEquationFunctionToDatabase() to ensure the
         // equation gets removed from the DB.
         dropEquationFunctionToDatabase();
      }

      Assert.assertTrue( "Unexpected value for " + MUTIPLIER_DATA_TYPE_FOR_HOURS + "; expected="
            + lExpected + " , actual=" + lActual, lExpected.compareTo( lActual ) == 0 );
   }

   //
   // Given an aircraft tracking parameters of HOURS and calculated-parameter (which
   // are based on
   // the
   // calculate parameter HOURS).
   //
   // And the calculated parameter will be called MULTIPLY_DATA_TYPE_CODE_FOR_HOURS
   // And the equation is input * MULTIPLY_TIMES
   // And the task is raised on aircraft component
   // And set up the parameter and calculated-parameter on the root config slot.


   // When creating a flight before task completion date
   // Then it will re-calculate the calculate parameters on the task completion usage snap shot
   @Test
   public void itRecalculatesCalcParamWhenTaskOnAircraftComponentCompletedAfterFlight()
         throws Exception {
      addEquationFunctionToDatabase();
      final AssemblyKey lAcftAssembly =
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
                                    aBuilder.setCode( MUTIPLIER_DATA_TYPE_FOR_HOURS );
                                    aBuilder.setDatabaseCalculation( MULTIPLIER_FUNCTION_NAME );
                                    aBuilder.setPrecisionQt( 2 );

                                    // Add the constant and parameter in the order in which the
                                    // function is expecting them.
                                    aBuilder.addConstant( MULTIPLIER_CONSTANT_NAME,
                                          MULTIPLIER_CONSTANT );
                                    aBuilder.addParameter( HOURS );
                                 }

                              } );
                     }
                  } );
               }
            } );

      ConfigSlotKey lRootConfigSlot = Domain.readRootConfigurationSlot( lAcftAssembly );
      final ConfigSlotPositionKey lTrkConfigSlotPos =
            getConfigSlotPos( lRootConfigSlot, TRK_CONFIG_SLOT_CODE );
      final DataTypeKey lCalcParmKey =
            Domain.readUsageParameter( lRootConfigSlot, MUTIPLIER_DATA_TYPE_FOR_HOURS );

      final InventoryKey lAirComponent =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aBuilder ) {
                  aBuilder.setPosition( lTrkConfigSlotPos );
                  aBuilder.addUsage( HOURS, BigDecimal.ZERO );
                  aBuilder.addUsage( lCalcParmKey, BigDecimal.ZERO );

               }

            } );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aBuilder ) {
            aBuilder.setAssembly( lAcftAssembly );
            aBuilder.addTracked( lAirComponent );
            aBuilder.addSystem( "myAircraftSystem" );
            aBuilder.addUsage( HOURS, BigDecimal.ZERO );
            aBuilder.addUsage( lCalcParmKey, BigDecimal.ZERO );
         }
      } );

      final UsageSnapshot lUsageSnapShotForHours =
            new UsageSnapshot( lAirComponent, HOURS, COMPLETION_HOURS );

      final TaskKey lRequirement =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement aRequirement ) {
                  aRequirement.addUsage( new UsageSnapshot( lAirComponent,
                        Domain.readUsageParameter(
                              Domain.readRootConfigurationSlot( lAcftAssembly ),
                              MUTIPLIER_DATA_TYPE_FOR_HOURS ),
                        COMPLETION_HOURS_CALC_PARM ) );
                  aRequirement.addUsage( lUsageSnapShotForHours );
                  aRequirement.setInventory( lAirComponent );
                  aRequirement.setActualStartDate( NEW_DATE );
                  aRequirement.setActualEndDate( NEW_DATE );
                  aRequirement.setStatus( RefEventStatusKey.COMPLETE );
               }
            } );

      FlightInformationTO lFlightInfoTO = generateFlightInfoTO( FLIGHT_NAME, NEW_DATE, NEW_DATE );
      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( lAircraft, HOURS, EDITED_FLIGHT_HOURS ) };

      iFlightHistBean.createHistFlight( new AircraftKey( lAircraft ), createHumanResource(),
            lFlightInfoTO, lEditUsageParms, NO_MEASUREMENTS );

      BigDecimal lExpected =
            COMPLETION_HOURS.add( EDITED_FLIGHT_HOURS ).multiply( MULTIPLIER_CONSTANT );
      BigDecimal lAcutual = readActualUsageParmValue( lRequirement.getEventKey(), lAcftAssembly );

      {
         // Important!
         // All data needed for assertions must be retrieved prior to calling
         // dropEquationFunctionToDatabase(). As it performs an explicit roll-back.
         //
         // ALSO, all assertions must be done after dropEquationFunctionToDatabase() to ensure the
         // equation gets removed from the DB.
         dropEquationFunctionToDatabase();
      }

      Assert.assertTrue( "Unexpected value for " + MUTIPLIER_DATA_TYPE_FOR_HOURS + "; expected="
            + lExpected + " , actual=" + lAcutual, lExpected.compareTo( lAcutual ) == 0 );

   }

   //
   // Given an aircraft tracking parameters of HOURS and calculated-parameter (which
   // are based on
   // the
   // calculate parameter HOURS).
   //
   // And the calculated parameter will be called MULTIPLY_DATA_TYPE_CODE_FOR_HOURS
   // And the equation is input * MULTIPLY_TIMES
   // And the task is raised on engine component
   // And set up the parameter and calculated-parameter on the root config slot.


   // When creating a flight before the task completion date
   // Then it will not re-calculate the calculate parameters on the task completion usage snap shot
   @Test
   public void itRecalculatesCalcParamWhenTaskOnEngineComponentCompletedAfterFlight()
         throws Exception {
      addEquationFunctionToDatabase();
      final AssemblyKey lAcftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aBuilder ) {
                  aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot aBuilder ) {
                        // Add a sub-assembly config slot to the root config slot, for an engine.
                        aBuilder
                              .addConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                                 @Override
                                 public void configure( ConfigurationSlot aBuilder ) {
                                    aBuilder.setCode( SUBASSY_CONFIG_SLOT_CODE );
                                 }
                              } );
                        // Add a usage parameter and a calculated parameter based on that usage
                        // parameter.
                        aBuilder.addUsageParameter( HOURS );
                        aBuilder.addCalculatedUsageParameter(
                              new DomainConfiguration<CalculatedUsageParameter>() {

                                 @Override
                                 public void configure( CalculatedUsageParameter aBuilder ) {
                                    aBuilder.setCode( MUTIPLIER_DATA_TYPE_FOR_HOURS );
                                    aBuilder.setDatabaseCalculation( MULTIPLIER_FUNCTION_NAME );
                                    aBuilder.setPrecisionQt( 2 );

                                    // Add the constant and parameter in the order in which the
                                    // function is expecting them.
                                    aBuilder.addConstant( MULTIPLIER_CONSTANT_NAME,
                                          MULTIPLIER_CONSTANT );
                                    aBuilder.addParameter( HOURS );
                                 }

                              } );
                     }
                  } );
               }
            } );

      ConfigSlotKey lRootConfigSlot = Domain.readRootConfigurationSlot( lAcftAssembly );
      final ConfigSlotPositionKey lSubAssyConfigSlotPos =
            getConfigSlotPos( lRootConfigSlot, SUBASSY_CONFIG_SLOT_CODE );
      final DataTypeKey calcParmKey =
            Domain.readUsageParameter( lRootConfigSlot, MUTIPLIER_DATA_TYPE_FOR_HOURS );

      final InventoryKey lEngineComponent =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aBuilder ) {
                  aBuilder.setPosition( lSubAssyConfigSlotPos );
                  aBuilder.addUsage( HOURS, BigDecimal.ZERO );
                  aBuilder.addUsage( calcParmKey, BigDecimal.ZERO );

               }

            } );

      final InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aBuilder ) {
            aBuilder.addTracked( lEngineComponent );
            aBuilder.addUsage( HOURS, BigDecimal.ZERO );
            aBuilder.addUsage( calcParmKey, BigDecimal.ZERO );

         }

      } );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aBuilder ) {
            aBuilder.setAssembly( lAcftAssembly );
            aBuilder.addEngine( lEngine );
            aBuilder.addSystem( "myAircraftSystem" );
            aBuilder.addUsage( HOURS, BigDecimal.ZERO );
            aBuilder.addUsage( calcParmKey, BigDecimal.ZERO );
         }
      } );

      final UsageSnapshot lUsageSnapShotForHours =
            new UsageSnapshot( lEngineComponent, HOURS, COMPLETION_HOURS );

      final TaskKey lRequirement =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement aRequirement ) {
                  aRequirement.addUsage( new UsageSnapshot( lEngineComponent,
                        Domain.readUsageParameter(
                              Domain.readRootConfigurationSlot( lAcftAssembly ),
                              MUTIPLIER_DATA_TYPE_FOR_HOURS ),
                        COMPLETION_HOURS_CALC_PARM ) );
                  aRequirement.addUsage( lUsageSnapShotForHours );

                  aRequirement.setInventory( lEngineComponent );
                  aRequirement.setActualStartDate( NEW_DATE );
                  aRequirement.setActualEndDate( NEW_DATE );
                  aRequirement.setStatus( RefEventStatusKey.COMPLETE );
               }
            } );

      FlightInformationTO lFlightInfoTO = generateFlightInfoTO( FLIGHT_NAME, NEW_DATE, NEW_DATE );
      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( lAircraft, HOURS, EDITED_FLIGHT_HOURS ),
                  generateFlightUsage( lEngine, HOURS, EDITED_FLIGHT_HOURS ) };

      iFlightHistBean.createHistFlight( new AircraftKey( lAircraft ), createHumanResource(),
            lFlightInfoTO, lEditUsageParms, NO_MEASUREMENTS );

      BigDecimal lExpected =
            COMPLETION_HOURS.add( EDITED_FLIGHT_HOURS ).multiply( MULTIPLIER_CONSTANT );
      BigDecimal lAcutual = readActualUsageParmValue( lRequirement.getEventKey(), lAcftAssembly );

      {
         // Important!
         // All data needed for assertions must be retrieved prior to calling
         // dropEquationFunctionToDatabase(). As it performs an explicit roll-back.
         //
         // ALSO, all assertions must be done after dropEquationFunctionToDatabase() to ensure the
         // equation gets removed from the DB.
         dropEquationFunctionToDatabase();
      }

      Assert.assertTrue( "Unexpected value for " + MUTIPLIER_DATA_TYPE_FOR_HOURS + "; expected="
            + lExpected + " , actual=" + lAcutual, lExpected.compareTo( lAcutual ) == 0 );

   }

   //
   // Given an aircraft tracking parameters of HOURS and calculated-parameter (which
   // are based on
   // the
   // calculate parameter HOURS).
   //
   // And the calculated parameter will be called MULTIPLY_DATA_TYPE_CODE_FOR_HOURS
   // And the task is raised on aircraft system
   // And the equation is input * MULTIPLY_TIMES

   // And set up the parameter and calculated-parameter on the root config slot.


   // When creating a flight after the task completion date
   // Then it will not re-calculate the calculate parameters on the task completion usage snap shot
   @Test
   public void itDoesNotRecalculateCalcParamWhenTaskOnAircraftSystemCompletedBeforeFlight()
         throws Exception {
      addEquationFunctionToDatabase();
      final AssemblyKey lAcftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aBuilder ) {
                  aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot aBuilder ) {
                        // Track a parameter.
                        aBuilder.addUsageParameter( HOURS );
                        aBuilder.addCalculatedUsageParameter(
                              new DomainConfiguration<CalculatedUsageParameter>() {

                                 @Override
                                 public void configure( CalculatedUsageParameter aBuilder ) {
                                    aBuilder.setCode( MUTIPLIER_DATA_TYPE_FOR_HOURS );
                                    aBuilder.setDatabaseCalculation( MULTIPLIER_FUNCTION_NAME );
                                    aBuilder.setPrecisionQt( 2 );

                                    // Add the constant and parameter in the order in which the
                                    // function is expecting them.
                                    aBuilder.addConstant( MULTIPLIER_CONSTANT_NAME,
                                          MULTIPLIER_CONSTANT );
                                    aBuilder.addParameter( HOURS );
                                 }

                              } );
                     }
                  } );
               }
            } );

      final DataTypeKey calcParmKey = Domain.readUsageParameter(
            Domain.readRootConfigurationSlot( lAcftAssembly ), MUTIPLIER_DATA_TYPE_FOR_HOURS );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aBuilder ) {
            aBuilder.setAssembly( lAcftAssembly );
            aBuilder.addSystem( "myAircraftSystem" );
            aBuilder.addUsage( HOURS, BigDecimal.ZERO );
            aBuilder.addUsage( calcParmKey, BigDecimal.ZERO );
         }
      } );

      final InventoryKey lAircraftSystem = Domain.readSystem( lAircraft, "myAircraftSystem" );

      final UsageSnapshot lUsageSnapShotForHours =
            new UsageSnapshot( lAircraftSystem, HOURS, COMPLETION_HOURS );

      final TaskKey lRequirement =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement aRequirement ) {
                  aRequirement.addUsage( new UsageSnapshot( lAircraftSystem,
                        Domain.readUsageParameter(
                              Domain.readRootConfigurationSlot( lAcftAssembly ),
                              MUTIPLIER_DATA_TYPE_FOR_HOURS ),
                        COMPLETION_HOURS_CALC_PARM ) );
                  aRequirement.addUsage( lUsageSnapShotForHours );

                  aRequirement.setInventory( lAircraftSystem );
                  aRequirement.setActualStartDate( TASK_DATE_BEFORE_FLIGHT );
                  aRequirement.setActualEndDate( TASK_DATE_BEFORE_FLIGHT );
                  aRequirement.setStatus( RefEventStatusKey.COMPLETE );
               }
            } );

      FlightInformationTO lFlightInfoTO = generateFlightInfoTO( FLIGHT_NAME, NEW_DATE, NEW_DATE );
      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( lAircraft, HOURS, EDITED_FLIGHT_HOURS ) };

      iFlightHistBean.createHistFlight( new AircraftKey( lAircraft ), createHumanResource(),
            lFlightInfoTO, lEditUsageParms, NO_MEASUREMENTS );

      BigDecimal lExpected = COMPLETION_HOURS.multiply( MULTIPLIER_CONSTANT );
      BigDecimal lAcutual = readActualUsageParmValue( lRequirement.getEventKey(), lAcftAssembly );

      {
         // Important!
         // All data needed for assertions must be retrieved prior to calling
         // dropEquationFunctionToDatabase(). As it performs an explicit roll-back.
         //
         // ALSO, all assertions must be done after dropEquationFunctionToDatabase() to ensure the
         // equation gets removed from the DB.
         dropEquationFunctionToDatabase();
      }

      Assert.assertTrue( "Unexpected value for " + MUTIPLIER_DATA_TYPE_FOR_HOURS + "; expected="
            + lExpected + " , actual=" + lAcutual, lExpected.compareTo( lAcutual ) == 0 );

   }

   //
   // Given an aircraft tracking parameters of HOURS and calculated-parameter (which
   // are based on
   // the
   // calculate parameter HOURS).
   //
   // And the calculated parameter will be called MULTIPLY_DATA_TYPE_CODE_FOR_HOURS
   // And the equation is input * MULTIPLY_TIMES
   // And the task will raised on aircraft component
   // And set up the parameter and calculated-parameter on the root config slot.


   // When creating a flight after the task completion date
   // Then it will not re-calculate the calculate parameters on the task completion snap shot
   @Test
   public void itDoesNotRecalculateCalcParamWhenTaskOnAircraftComponentCompletedBeforeFlight()
         throws Exception {
      addEquationFunctionToDatabase();
      final AssemblyKey lAcftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aBuilder ) {
                  aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot aBuilder ) {
                        // Track a parameter.
                        aBuilder.addUsageParameter( HOURS );
                        aBuilder.addCalculatedUsageParameter(
                              new DomainConfiguration<CalculatedUsageParameter>() {

                                 @Override
                                 public void configure( CalculatedUsageParameter aBuilder ) {
                                    aBuilder.setCode( MUTIPLIER_DATA_TYPE_FOR_HOURS );
                                    aBuilder.setDatabaseCalculation( MULTIPLIER_FUNCTION_NAME );
                                    aBuilder.setPrecisionQt( 2 );

                                    // Add the constant and parameter in the order in which the
                                    // function is expecting them.
                                    aBuilder.addConstant( MULTIPLIER_CONSTANT_NAME,
                                          MULTIPLIER_CONSTANT );
                                    aBuilder.addParameter( HOURS );
                                 }

                              } );
                     }
                  } );
               }
            } );

      final DataTypeKey calcParmKey = Domain.readUsageParameter(
            Domain.readRootConfigurationSlot( lAcftAssembly ), MUTIPLIER_DATA_TYPE_FOR_HOURS );

      final InventoryKey lAirComponent =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aBuilder ) {
                  aBuilder.addUsage( HOURS, BigDecimal.ZERO );
                  aBuilder.addUsage( calcParmKey, BigDecimal.ZERO );

               }

            } );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aBuilder ) {
            aBuilder.setAssembly( lAcftAssembly );
            aBuilder.addTracked( lAirComponent );
            aBuilder.addSystem( "myAircraftSystem" );
            aBuilder.addUsage( HOURS, BigDecimal.ZERO );
            aBuilder.addUsage( calcParmKey, BigDecimal.ZERO );
         }
      } );

      final UsageSnapshot lUsageSnapShotForHours =
            new UsageSnapshot( lAirComponent, HOURS, COMPLETION_HOURS );

      final TaskKey lRequirement =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement aRequirement ) {
                  aRequirement.addUsage( new UsageSnapshot( lAirComponent,
                        Domain.readUsageParameter(
                              Domain.readRootConfigurationSlot( lAcftAssembly ),
                              MUTIPLIER_DATA_TYPE_FOR_HOURS ),
                        COMPLETION_HOURS_CALC_PARM ) );
                  aRequirement.addUsage( lUsageSnapShotForHours );

                  aRequirement.setInventory( lAirComponent );
                  aRequirement.setActualStartDate( TASK_DATE_BEFORE_FLIGHT );
                  aRequirement.setActualEndDate( TASK_DATE_BEFORE_FLIGHT );
                  aRequirement.setStatus( RefEventStatusKey.COMPLETE );
               }
            } );

      FlightInformationTO lFlightInfoTO = generateFlightInfoTO( FLIGHT_NAME, NEW_DATE, NEW_DATE );
      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( lAircraft, HOURS, EDITED_FLIGHT_HOURS ) };

      iFlightHistBean.createHistFlight( new AircraftKey( lAircraft ), createHumanResource(),
            lFlightInfoTO, lEditUsageParms, NO_MEASUREMENTS );

      BigDecimal lExpected = COMPLETION_HOURS.multiply( MULTIPLIER_CONSTANT );
      BigDecimal lAcutual = readActualUsageParmValue( lRequirement.getEventKey(), lAcftAssembly );

      {
         // Important!
         // All data needed for assertions must be retrieved prior to calling
         // dropEquationFunctionToDatabase(). As it performs an explicit roll-back.
         //
         // ALSO, all assertions must be done after dropEquationFunctionToDatabase() to ensure the
         // equation gets removed from the DB.
         dropEquationFunctionToDatabase();
      }

      Assert.assertTrue( "Unexpected value for " + MUTIPLIER_DATA_TYPE_FOR_HOURS + "; expected="
            + lExpected + " , actual=" + lAcutual, lExpected.compareTo( lAcutual ) == 0 );

   }


   @Before
   public void setup() {
      // Important!
      // Be aware that any data setup in the database within this setup() will be rolled back if the
      // test contains a call to addEquationFunctionToDatabase().

      iFlightHistBean = new FlightHistBean();
      iFlightHistBean.ejbCreate();
      iFlightHistBean.setSessionContext( new SessionContextFake() );

   }


   @After
   public void tearDown() {
      iFlightHistBean.setSessionContext( null );
   }


   private void addEquationFunctionToDatabase() throws SQLException {

      // Function creation is DDL which implicitly commits the transaction.
      // We perform explicit roll-back before function creation ensuring no data gets committed
      // accidentally.
      String lCreateFunctionStatement = "CREATE OR REPLACE FUNCTION " + MULTIPLIER_FUNCTION_NAME
            + " (" + "aConstant NUMBER, aHoursInput NUMBER" + " )" + " RETURN NUMBER" + " " + "IS "
            + "result NUMBER; " + "BEGIN" + " " + "result := aConstant * aHoursInput ; " + "RETURN"
            + " " + " result;" + "END" + " " + MULTIPLIER_FUNCTION_NAME + " ;";

      Domain.createCalculatedParameterEquationFunction( iDatabaseConnectionRule.getConnection(),
            lCreateFunctionStatement );
   }


   private void dropEquationFunctionToDatabase() throws SQLException {

      // Function dropping is DDL which implicitly commits transaction.
      // We perform explicit rollback before function drop ensuring no data gets committed
      // accidentally.
      Domain.dropCalculatedParameterEquationFunction( iDatabaseConnectionRule.getConnection(),
            MULTIPLIER_FUNCTION_NAME );
   }


   private HumanResourceKey createHumanResource() {
      HumanResourceKey lHrKey =
            new HumanResourceDomainBuilder().withUsername( HR_USERNAME ).build();

      int lUserId = OrgHr.findByPrimaryKey( lHrKey ).getUserId();
      UserParametersFake lUserParametersFake = new UserParametersFake( lUserId, "LOGIC" );
      lUserParametersFake.setProperty( "MAX_FLIGHT_DAYS_IN_THE_PAST_VALUE", "150" );
      UserParameters.setInstance( lUserId, "LOGIC", lUserParametersFake );

      return lHrKey;
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


   private BigDecimal readActualUsageParmValue( EventKey lEnventKey, AssemblyKey lAcftAssembly ) {
      EventInventoryKey lEvtInvKey = new EventInventoryKey( lEnventKey, 1 );

      EventInventoryUsageKey lEvtInvCalcParmUsageKey = new EventInventoryUsageKey( lEvtInvKey,
            Domain.readUsageParameter( Domain.readRootConfigurationSlot( lAcftAssembly ),
                  MUTIPLIER_DATA_TYPE_FOR_HOURS ) );
      EvtInvUsageTable lEvtInvUsageCalcParm =
            EvtInvUsageTable.findByPrimaryKey( lEvtInvCalcParmUsageKey );
      lEvtInvUsageCalcParm = EvtInvUsageTable.findByPrimaryKey( lEvtInvCalcParmUsageKey );

      return BigDecimal.valueOf( lEvtInvUsageCalcParm.getTsnQt() );
   }


   private BigDecimal getEventInventoryUsage( EventKeyInterface aEvent, InventoryKey aInv,
         DataTypeKey aDataType ) {

      EventInventoryKey lEvtInv = EvtInvTable.findByEventAndInventory( aEvent, aInv ).getPk();

      EvtInvUsageTable lEvtInvUsage =
            EvtInvUsageTable.findByPrimaryKey( new EventInventoryUsageKey( lEvtInv, aDataType ) );

      return BigDecimal.valueOf( lEvtInvUsage.getTsnQt() );
   }


   private ConfigSlotPositionKey getConfigSlotPos( ConfigSlotKey aParentConfigSlot,
         String aConfigSlotCode ) {

      ConfigSlotKey lConfigSlot =
            Domain.readSubConfigurationSlot( aParentConfigSlot, aConfigSlotCode );

      return new ConfigSlotPositionKey( lConfigSlot, EqpAssmblPos.getFirstPosId( lConfigSlot ) );
   }
}
