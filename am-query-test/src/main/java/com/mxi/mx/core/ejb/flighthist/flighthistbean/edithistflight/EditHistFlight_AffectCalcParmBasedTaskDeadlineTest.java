package com.mxi.mx.core.ejb.flighthist.flighthistbean.edithistflight;

import static com.mxi.mx.core.key.DataTypeKey.HOURS;
import static com.mxi.mx.core.key.RefBOMClassKey.SUBASSY;
import static com.mxi.mx.core.key.RefBOMClassKey.SYS;
import static com.mxi.mx.core.key.RefBOMClassKey.TRK;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
import com.mxi.am.domain.Flight;
import com.mxi.am.domain.Location;
import com.mxi.am.domain.Requirement;
import com.mxi.am.domain.RequirementDefinition;
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
import com.mxi.mx.core.flight.model.FlightLegId;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefDataSourceKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefSchedFromKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.key.UsageDefinitionKey;
import com.mxi.mx.core.key.UsageParmKey;
import com.mxi.mx.core.services.event.inventory.UsageSnapshot;
import com.mxi.mx.core.services.event.usage.CollectedUsageParm;
import com.mxi.mx.core.services.flighthist.FlightInformationTO;
import com.mxi.mx.core.services.flighthist.Measurement;
import com.mxi.mx.core.services.inventory.InvUtils;
import com.mxi.mx.core.table.eqp.EqpAssmblPos;
import com.mxi.mx.core.table.eqp.EqpDataSourceSpec;
import com.mxi.mx.core.table.evt.EvtSchedDeadTable;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.org.OrgHr;


/**
 * Tests for {@linkplain FlightHistBean#editHistFlight} that involve updating the usage deltas of a
 * historical flight and their effects on calculated parameter based deadlines of tasks against the
 * aircraft and all sub-components installed during that flight.
 *
 */
public class EditHistFlight_AffectCalcParmBasedTaskDeadlineTest {

   private static final String HR_USERNAME = "HR_USERNAME";
   private static final String FLIGHT_NAME = "FLIGHT_NAME";
   private static final Date FLIGHT_DATE = DateUtils.addDays( new Date(), -100 );

   private static final String SYS_CONFIG_SLOT_CODE = "SYS_CS";
   private static final String TRK_CONFIG_SLOT_CODE = "TRK_CS";
   private static final String ENGINE_CONFIG_SLOT_CODE = "ENG_CS";

   // Calculated parameter data types, equation, and constant.
   // The equation is simply a multiplier of the target usage parameter by the multiplier constant.
   private static final String CALC_PARM_DATA_TYPE_FOR_HOURS = "CALC_PARM_DATA_TYPE_FOR_HOURS";
   private static final String CALC_PARM_FUNCTION_NAME = "CALC_PARM_FUNCTION_NAME";
   private static final String CALC_PARM_CONSTANT_NAME = "CALC_PARM_CONSTANT_NAME";
   private static final BigDecimal CALC_PARM_CONSTANT = BigDecimal.valueOf( 2.0 );

   // Aircraft Current Hours
   private static final BigDecimal CURRENT_HOURS = BigDecimal.valueOf( 5 );
   private static final BigDecimal CURRENT_CALC_PARM_HOURS = BigDecimal.valueOf( 10 );

   // Original flight usage.
   private static final BigDecimal FIRST_FLIGHT_HOURS = BigDecimal.valueOf( 5 );
   private static final BigDecimal SECOND_FLIGHT_HOURS = BigDecimal.valueOf( 4 );
   private static final BigDecimal CALC_PARM_HOURS_AT_FLIGHT =
         FIRST_FLIGHT_HOURS.multiply( CALC_PARM_CONSTANT );

   // Edited flight usage and difference from the original.
   private static final BigDecimal EDITED_FLIGHT_HOURS = BigDecimal.valueOf( 7 );
   private static final BigDecimal EDITED_FLIGHT_HOURS_DIFF =
         EDITED_FLIGHT_HOURS.subtract( FIRST_FLIGHT_HOURS );

   private static final Measurement[] NO_MEASUREMENTS = new Measurement[] {};

   private FlightHistBean iFlightHistBean;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Given an aircraft collecting usages and a calculated parameter defined on it
    *
    * And the aircraft has a historical flight
    *
    * And a task with calculated parameter based usage deadline is initialized on the aircraft with
    * deadline scheduled from EFFECTIVE date after the flight
    *
    * When the historical flight is edited and its usage modified
    *
    * Then the task's deadline information is modified accordingly.
    *
    * Note: to determine if a task is initialized after the flight, the deadline start value is
    * compared to the flight usage value.
    */
   @Test
   public void itAdjustsCalcParmDeadlineOfAircraftTaskAfterFlightWhenSchedFromEffective()
         throws Exception {

      // Important!
      // addEquationFunctionToDatabase() needs to be called prior to any data setup in database
      // as the method performs an explicit roll-back.
      addEquationFunctionToDatabase();

      // Set up the parameter and calculated-parameter on the root config slot.
      final AssemblyKey lAcftAssembly = createAcftAssyTrackingUsageParmsAndCalcParms();

      // Get the data type keys for the calculated parameters.
      final DataTypeKey lDataType_CALC_PARM_FOR_HOURS = Domain.readUsageParameter(
            Domain.readRootConfigurationSlot( lAcftAssembly ), CALC_PARM_DATA_TYPE_FOR_HOURS );

      // Given an aircraft collecting usage.
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( HOURS, CURRENT_HOURS );
            aAircraft.addUsage( lDataType_CALC_PARM_FOR_HOURS, CURRENT_CALC_PARM_HOURS );
         }
      } );

      // Given a flight for the aircraft that accrued usage.
      FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( FLIGHT_DATE );
            aFlight.addUsage( lAircraft, HOURS, FIRST_FLIGHT_HOURS, FIRST_FLIGHT_HOURS );
         }
      } );

      // dummy flight so we are not editing the latest flight, and oos logic will be processed.
      Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( DateUtils.addDays( FLIGHT_DATE, 3 ) );
            aFlight.addUsage( lAircraft, HOURS, SECOND_FLIGHT_HOURS,
                  FIRST_FLIGHT_HOURS.add( SECOND_FLIGHT_HOURS ) );
         }
      } );

      // Note: The task is considered initialized after the flight when its deadline start value is
      // greater than the flight usage value.

      final BigDecimal lDeadlineStartValue = CALC_PARM_HOURS_AT_FLIGHT;
      final BigDecimal lDeadlineInterval = BigDecimal.valueOf( 2 );
      final BigDecimal lDeadlineDueValue = lDeadlineStartValue.add( lDeadlineInterval );

      final Date lTaskDefnEffectiveDate = DateUtils.addDays( FLIGHT_DATE, 1 );
      final TaskTaskKey lTaskDefnKey =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aRequirementDefinition ) {
                  aRequirementDefinition.setScheduledFromEffectiveDate( lTaskDefnEffectiveDate );
               }

            } );

      TaskKey lReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setDefinition( lTaskDefnKey );
            aReq.setInventory( lAircraft );
            aReq.addUsageDeadline( lDataType_CALC_PARM_FOR_HOURS, RefSchedFromKey.EFFECTIV,
                  lDeadlineStartValue, lDeadlineInterval, lDeadlineDueValue );
         }
      } );

      // When the flight is edited and the usage is modified.
      FlightInformationTO lFlightInfoTO =
            generateFlightInfoTO( FLIGHT_NAME, FLIGHT_DATE, EDITED_FLIGHT_HOURS_DIFF );
      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( lAircraft, HOURS, EDITED_FLIGHT_HOURS ) };

      iFlightHistBean.editHistFlight( lFlight, createHumanResource(), lFlightInfoTO,
            lEditUsageParms, NO_MEASUREMENTS );

      // Then the task's deadline start value and due value are recalculated based on the usage
      // delta
      Double lExpectedStartValue = lDeadlineStartValue
            .add( EDITED_FLIGHT_HOURS_DIFF.multiply( CALC_PARM_CONSTANT ) ).doubleValue();
      Double lExpectedDueValue = lDeadlineDueValue
            .add( EDITED_FLIGHT_HOURS_DIFF.multiply( CALC_PARM_CONSTANT ) ).doubleValue();

      EvtSchedDeadTable lEvtSchedDead =
            EvtSchedDeadTable.findByPrimaryKey( lReq, lDataType_CALC_PARM_FOR_HOURS );
      Double lActualStartValue = lEvtSchedDead.getStartQt();
      Double lActualDueValue = lEvtSchedDead.getDeadlineQt();

      {
         // Important!
         // All data needed for assertions must be retrieved prior to calling
         // dropEquationFunctionToDatabase(). As it performs an explicit roll-back.
         dropEquationFunctionToDatabase();
      }

      assertEquals( "Unexpected deadline start value.", lExpectedStartValue, lActualStartValue );
      assertEquals( "Unexpected deadline due value.", lExpectedDueValue, lActualDueValue );
   }


   /**
    * Given an aircraft and an engine attached to the aircraft collecting usages and a calculated
    * parameter
    *
    * And the aircraft has a historical flight
    *
    * And there is a task completed against the engine
    *
    * And another task with calculated parameter based usage deadline is initialized on the engine
    * with deadline scheduled from LASTEND and after the flight
    *
    * When the historical flight is edited and its usage modified
    *
    * Then the task's deadline information is modified accordingly.
    *
    * Note: to determine if a task is initialized after the flight, the deadline start value is
    * compared to the flight usage value.
    */
   @Test
   public void itAdjustsCalcParmDeadlineOfEngineTaskAfterFlightWhenSchedFromLastEnd()
         throws Exception {

      // Important!
      // addEquationFunctionToDatabase() needs to be called prior to any data setup in database
      // as the method performs an explicit roll-back.
      addEquationFunctionToDatabase();

      // Set up the parameter and calculated-parameter on the root config slot.
      final AssemblyKey lAcftAssembly = createAcftAssyTrackingUsageParmsAndCalcParms();

      ConfigSlotKey lRootConfigSlot = Domain.readRootConfigurationSlot( lAcftAssembly );
      ConfigSlotKey lEngineConfigSlot =
            Domain.readSubConfigurationSlot( lRootConfigSlot, ENGINE_CONFIG_SLOT_CODE );

      final ConfigSlotPositionKey lEngineConfigSlotPos = new ConfigSlotPositionKey(
            lEngineConfigSlot, EqpAssmblPos.getFirstPosId( lEngineConfigSlot ) );

      // Get the data type keys for the calculated parameters.
      final DataTypeKey lDataType_CALC_PARM_FOR_HOURS =
            Domain.readUsageParameter( lRootConfigSlot, CALC_PARM_DATA_TYPE_FOR_HOURS );

      final InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.setPosition( lEngineConfigSlotPos );
            aEngine.addUsage( HOURS, CURRENT_HOURS );
            aEngine.addUsage( lDataType_CALC_PARM_FOR_HOURS, CURRENT_CALC_PARM_HOURS );
         }

      } );

      // Given an aircraft collecting usage.
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( HOURS, CURRENT_HOURS );
            aAircraft.addUsage( lDataType_CALC_PARM_FOR_HOURS, CURRENT_CALC_PARM_HOURS );
            aAircraft.addEngine( lEngine );
         }
      } );

      // Given a flight for the aircraft that accrued usage.
      FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( FLIGHT_DATE );
            aFlight.addUsage( lAircraft, HOURS, FIRST_FLIGHT_HOURS, FIRST_FLIGHT_HOURS );
            aFlight.addUsage( lEngine, HOURS, FIRST_FLIGHT_HOURS, FIRST_FLIGHT_HOURS );
         }
      } );

      Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( DateUtils.addDays( FLIGHT_DATE, 3 ) );
            aFlight.addUsage( lAircraft, HOURS, SECOND_FLIGHT_HOURS,
                  FIRST_FLIGHT_HOURS.add( SECOND_FLIGHT_HOURS ) );
            aFlight.addUsage( lEngine, HOURS, SECOND_FLIGHT_HOURS,
                  FIRST_FLIGHT_HOURS.add( SECOND_FLIGHT_HOURS ) );

         }
      } );

      // Note: The task is considered initialized after the flight when its deadline start value is
      // greater than the flight usage value.

      final BigDecimal lPrevDeadlineStartValue =
            CALC_PARM_HOURS_AT_FLIGHT.add( BigDecimal.valueOf( 2 ) );
      final BigDecimal lDeadlineInterval = BigDecimal.valueOf( 5 );
      final BigDecimal lLastEndValue = lPrevDeadlineStartValue.add( BigDecimal.valueOf( 4 ) );

      final TaskKey lPreviousReq =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement aReq ) {
                  UsageSnapshot lUsageSnapshotCalc =
                        new UsageSnapshot( lEngine, lDataType_CALC_PARM_FOR_HOURS, lLastEndValue );
                  UsageSnapshot lUsageSnapshot = new UsageSnapshot( lEngine, HOURS,
                        lLastEndValue.divide( CALC_PARM_CONSTANT ) );
                  aReq.setInventory( lEngine );
                  aReq.setStatus( RefEventStatusKey.COMPLETE );
                  aReq.setActualEndDate( DateUtils.addDays( FLIGHT_DATE, 2 ) );
                  aReq.addUsage( lUsageSnapshot );
                  aReq.addUsage( lUsageSnapshotCalc );
                  aReq.addUsageDeadline( lDataType_CALC_PARM_FOR_HOURS, RefSchedFromKey.LASTEND,
                        lPrevDeadlineStartValue, lDeadlineInterval,
                        lPrevDeadlineStartValue.add( lDeadlineInterval ) );
               }
            } );

      TaskKey lReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setInventory( lEngine );
            aReq.setPreviousTask( lPreviousReq );
            aReq.addUsageDeadline( lDataType_CALC_PARM_FOR_HOURS, RefSchedFromKey.LASTEND,
                  lLastEndValue, lDeadlineInterval, lLastEndValue.add( lDeadlineInterval ) );
         }
      } );

      // When the flight is edited and the usage is modified.
      FlightInformationTO lFlightInfoTO =
            generateFlightInfoTO( FLIGHT_NAME, FLIGHT_DATE, EDITED_FLIGHT_HOURS_DIFF );
      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( lAircraft, HOURS, EDITED_FLIGHT_HOURS ),
                  generateFlightUsage( lEngine, HOURS, EDITED_FLIGHT_HOURS ) };

      iFlightHistBean.editHistFlight( lFlight, createHumanResource(), lFlightInfoTO,
            lEditUsageParms, NO_MEASUREMENTS );

      // Then the task's deadline start value and due value are recalculated based on the usage
      // delta
      Double lExpectedStartValue = lLastEndValue
            .add( EDITED_FLIGHT_HOURS_DIFF.multiply( CALC_PARM_CONSTANT ) ).doubleValue();
      Double lExpectedDueValue = lLastEndValue.add( lDeadlineInterval )
            .add( EDITED_FLIGHT_HOURS_DIFF.multiply( CALC_PARM_CONSTANT ) ).doubleValue();

      EvtSchedDeadTable lEvtSchedDead =
            EvtSchedDeadTable.findByPrimaryKey( lReq, lDataType_CALC_PARM_FOR_HOURS );
      Double lActualStartValue = lEvtSchedDead.getStartQt();
      Double lActualDueValue = lEvtSchedDead.getDeadlineQt();

      {
         // Important!
         // All data needed for assertions must be retrieved prior to calling
         // dropEquationFunctionToDatabase(). As it performs an explicit roll-back.
         dropEquationFunctionToDatabase();
      }

      assertEquals( "Unexpected deadline start value.", lExpectedStartValue, lActualStartValue );
      assertEquals( "Unexpected deadline due value.", lExpectedDueValue, lActualDueValue );
   }


   /**
    * Given an aircraft and an aircraft system collecting usages and a calculated parameter
    *
    * And the aircraft has a historical flight
    *
    * And there is a task completed against the system
    *
    * And another task with calculated parameter based usage deadline is initialized on the system
    * with deadline scheduled from LASTEND and after the flight
    *
    * When the historical flight is edited and its usage modified
    *
    * Then the task's deadline information is modified accordingly.
    *
    * Note: to determine if a task is initialized after the flight, the deadline start value is
    * compared to the flight usage value.
    */
   @Test
   public void itAdjustsCalcParmDeadlineOfEngineSystemTaskAfterFlightWhenSchedFromLastEnd()
         throws Exception {

      // Important!
      // addEquationFunctionToDatabase() needs to be called prior to any data setup in database
      // as the method performs an explicit roll-back.
      addEquationFunctionToDatabase();

      // Set up the parameter and calculated-parameter on the root config slot.
      final AssemblyKey lAcftAssembly = createAcftAssyTrackingUsageParmsAndCalcParms();

      ConfigSlotKey lRootConfigSlot = Domain.readRootConfigurationSlot( lAcftAssembly );
      ConfigSlotKey lSysConfigSlot =
            Domain.readSubConfigurationSlot( lRootConfigSlot, SYS_CONFIG_SLOT_CODE );

      final ConfigSlotPositionKey lSysConfigSlotPos = new ConfigSlotPositionKey( lSysConfigSlot,
            EqpAssmblPos.getFirstPosId( lSysConfigSlot ) );

      // Get the data type keys for the calculated parameters.
      final DataTypeKey lDataType_CALC_PARM_FOR_HOURS =
            Domain.readUsageParameter( lRootConfigSlot, CALC_PARM_DATA_TYPE_FOR_HOURS );

      // Given an aircraft collecting usage.
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( HOURS, CURRENT_HOURS );
            aAircraft.addUsage( lDataType_CALC_PARM_FOR_HOURS, CURRENT_CALC_PARM_HOURS );
            aAircraft.addSystem( new DomainConfiguration<System>() {

               @Override
               public void configure( System aSystem ) {

                  Map<DataTypeKey, BigDecimal> lSystemUsageMap =
                        new HashMap<DataTypeKey, BigDecimal>();
                  lSystemUsageMap.put( HOURS, CURRENT_HOURS );
                  lSystemUsageMap.put( lDataType_CALC_PARM_FOR_HOURS, CURRENT_CALC_PARM_HOURS );

                  aSystem.setName( "12-00 - Lubrication/General" );
                  aSystem.setPosition( lSysConfigSlotPos );
                  aSystem.setUsage( lSystemUsageMap );
               }

            } );
         }
      } );

      final InventoryKey lAircraftSystem =
            InvUtils.getSystemByName( lAircraft, "12-00 - Lubrication/General" );

      // Given a flight for the aircraft that accrued usage.
      FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( FLIGHT_DATE );
            aFlight.addUsage( lAircraft, HOURS, FIRST_FLIGHT_HOURS, FIRST_FLIGHT_HOURS );
         }
      } );

      // Note: The task is considered initialized after the flight when its deadline start value is
      // greater than the flight usage value.

      final BigDecimal lPrevDeadlineStartValue =
            CALC_PARM_HOURS_AT_FLIGHT.add( BigDecimal.valueOf( 2 ) );
      final BigDecimal lDeadlineInterval = BigDecimal.valueOf( 5 );
      final BigDecimal lLastEndValue = lPrevDeadlineStartValue.add( BigDecimal.valueOf( 4 ) );

      final TaskKey lPreviousReq =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement aReq ) {
                  UsageSnapshot lUsageSnapshotCalc = new UsageSnapshot( lAircraftSystem,
                        lDataType_CALC_PARM_FOR_HOURS, lLastEndValue );
                  UsageSnapshot lUsageSnapshot = new UsageSnapshot( lAircraftSystem, HOURS,
                        lLastEndValue.divide( CALC_PARM_CONSTANT ) );

                  aReq.setInventory( lAircraftSystem );
                  aReq.setStatus( RefEventStatusKey.COMPLETE );
                  aReq.setActualEndDate( DateUtils.addDays( FLIGHT_DATE, 2 ) );
                  aReq.addUsage( lUsageSnapshot );
                  aReq.addUsage( lUsageSnapshotCalc );
                  aReq.addUsageDeadline( lDataType_CALC_PARM_FOR_HOURS, RefSchedFromKey.LASTEND,
                        lPrevDeadlineStartValue, lDeadlineInterval,
                        lPrevDeadlineStartValue.add( lDeadlineInterval ) );
               }
            } );

      TaskKey lReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setInventory( lAircraftSystem );
            aReq.setPreviousTask( lPreviousReq );
            aReq.addUsageDeadline( lDataType_CALC_PARM_FOR_HOURS, RefSchedFromKey.LASTEND,
                  lLastEndValue, lDeadlineInterval, lLastEndValue.add( lDeadlineInterval ) );
         }
      } );

      // When the flight is edited and the usage is modified.
      FlightInformationTO lFlightInfoTO =
            generateFlightInfoTO( FLIGHT_NAME, FLIGHT_DATE, EDITED_FLIGHT_HOURS_DIFF );
      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( lAircraft, HOURS, EDITED_FLIGHT_HOURS ) };

      iFlightHistBean.editHistFlight( lFlight, createHumanResource(), lFlightInfoTO,
            lEditUsageParms, NO_MEASUREMENTS );

      // Then the task's deadline start value and due value are recalculated based on the usage
      // delta
      Double lExpectedStartValue = lLastEndValue
            .add( EDITED_FLIGHT_HOURS_DIFF.multiply( CALC_PARM_CONSTANT ) ).doubleValue();
      Double lExpectedDueValue = lLastEndValue.add( lDeadlineInterval )
            .add( EDITED_FLIGHT_HOURS_DIFF.multiply( CALC_PARM_CONSTANT ) ).doubleValue();

      EvtSchedDeadTable lEvtSchedDead =
            EvtSchedDeadTable.findByPrimaryKey( lReq, lDataType_CALC_PARM_FOR_HOURS );
      Double lActualStartValue = lEvtSchedDead.getStartQt();
      Double lActualDueValue = lEvtSchedDead.getDeadlineQt();

      {
         // Important!
         // All data needed for assertions must be retrieved prior to calling
         // dropEquationFunctionToDatabase(). As it performs an explicit roll-back.
         dropEquationFunctionToDatabase();
      }

      assertEquals( "Unexpected deadline start value.", lExpectedStartValue, lActualStartValue );
      assertEquals( "Unexpected deadline due value.", lExpectedDueValue, lActualDueValue );
   }


   /**
    * Given an aircraft and an aircraft system collecting usages and a calculated parameter
    *
    * And the aircraft has a historical flight
    *
    * And a task with calculated parameter based usage deadline is initialized on the system with
    * deadline scheduled from Effective date and after the flight
    *
    * When the historical flight is edited and its usage modified
    *
    * Then the task's deadline information is modified accordingly.
    *
    * Note: to determine if a task is initialized after the flight, the deadline start value is
    * compared to the flight usage value.
    */
   @Test
   public void itAdjustsCalcParmDeadlineOfEngineSystemTaskAfterFlightWhenSchedFromEffective()
         throws Exception {

      // Important!
      // addEquationFunctionToDatabase() needs to be called prior to any data setup in database
      // as the method performs an explicit roll-back.
      addEquationFunctionToDatabase();

      // Set up the parameter and calculated-parameter on the root config slot.
      final AssemblyKey lAcftAssembly = createAcftAssyTrackingUsageParmsAndCalcParms();

      // Get the data type keys for the calculated parameters.
      final DataTypeKey lDataType_CALC_PARM_FOR_HOURS = Domain.readUsageParameter(
            Domain.readRootConfigurationSlot( lAcftAssembly ), CALC_PARM_DATA_TYPE_FOR_HOURS );

      // Given an aircraft collecting usage.
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( HOURS, CURRENT_HOURS );
            aAircraft.addUsage( lDataType_CALC_PARM_FOR_HOURS, CURRENT_CALC_PARM_HOURS );
            aAircraft.addSystem( new DomainConfiguration<System>() {

               @Override
               public void configure( System aSystem ) {

                  Map<DataTypeKey, BigDecimal> lSystemUsageMap =
                        new HashMap<DataTypeKey, BigDecimal>();
                  lSystemUsageMap.put( HOURS, CURRENT_HOURS );
                  lSystemUsageMap.put( lDataType_CALC_PARM_FOR_HOURS, CURRENT_CALC_PARM_HOURS );
                  aSystem.setName( "12-00 - Lubrication/General" );
                  aSystem.setUsage( lSystemUsageMap );
               }

            } );
         }
      } );

      final InventoryKey lAircraftSystem =
            InvUtils.getSystemByName( lAircraft, "12-00 - Lubrication/General" );

      // Given a flight for the aircraft that accrued usage.
      FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( FLIGHT_DATE );
            aFlight.addUsage( lAircraft, HOURS, FIRST_FLIGHT_HOURS, FIRST_FLIGHT_HOURS );
         }
      } );

      // Note: The task is considered initialized after the flight when its deadline start value is
      // greater than the flight usage value.

      final BigDecimal lDeadlineStartValue = CALC_PARM_HOURS_AT_FLIGHT;
      final BigDecimal lDeadlineInterval = BigDecimal.valueOf( 2 );
      final BigDecimal lDeadlineDueValue = lDeadlineStartValue.add( lDeadlineInterval );

      final Date lTaskDefnEffectiveDate = DateUtils.addDays( FLIGHT_DATE, 1 );
      final TaskTaskKey lTaskDefnKey =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aRequirementDefinition ) {
                  aRequirementDefinition.setScheduledFromEffectiveDate( lTaskDefnEffectiveDate );
               }

            } );

      TaskKey lReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setInventory( lAircraftSystem );
            aReq.setDefinition( lTaskDefnKey );
            aReq.setStatus( RefEventStatusKey.COMPLETE );
            aReq.setActualEndDate( DateUtils.addDays( FLIGHT_DATE, 2 ) );
            aReq.addUsageDeadline( lDataType_CALC_PARM_FOR_HOURS, RefSchedFromKey.EFFECTIV,
                  lDeadlineStartValue, lDeadlineInterval, lDeadlineDueValue );
         }
      } );

      // When the flight is edited and the usage is modified.
      FlightInformationTO lFlightInfoTO =
            generateFlightInfoTO( FLIGHT_NAME, FLIGHT_DATE, EDITED_FLIGHT_HOURS_DIFF );
      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( lAircraft, HOURS, EDITED_FLIGHT_HOURS ) };

      iFlightHistBean.editHistFlight( lFlight, createHumanResource(), lFlightInfoTO,
            lEditUsageParms, NO_MEASUREMENTS );

      // Then the task's deadline start value and due value are recalculated based on the usage
      // delta
      Double lExpectedStartValue = lDeadlineStartValue
            .add( EDITED_FLIGHT_HOURS_DIFF.multiply( CALC_PARM_CONSTANT ) ).doubleValue();
      Double lExpectedDueValue = lDeadlineDueValue
            .add( EDITED_FLIGHT_HOURS_DIFF.multiply( CALC_PARM_CONSTANT ) ).doubleValue();

      EvtSchedDeadTable lEvtSchedDead =
            EvtSchedDeadTable.findByPrimaryKey( lReq, lDataType_CALC_PARM_FOR_HOURS );
      Double lActualStartValue = lEvtSchedDead.getStartQt();
      Double lActualDueValue = lEvtSchedDead.getDeadlineQt();

      {
         // Important!
         // All data needed for assertions must be retrieved prior to calling
         // dropEquationFunctionToDatabase(). As it performs an explicit roll-back.
         dropEquationFunctionToDatabase();
      }

      assertEquals( "Unexpected deadline start value.", lExpectedStartValue, lActualStartValue );
      assertEquals( "Unexpected deadline due value.", lExpectedDueValue, lActualDueValue );
   }


   /**
    * Given an aircraft and an engine with TRK subcomponent connected to it and all these
    * inventories are collecting usages and a calculated parameter
    *
    * And the aircraft has a historical flight
    *
    * And there is a task completed against the subcomponent
    *
    * And another task with calculated parameter based usage deadline is initialized on the
    * subcomponent with deadline scheduled from LASTEND and after the flight
    *
    * When the historical flight is edited and its usage modified
    *
    * Then the task's deadline information is modified accordingly.
    *
    * Note: to determine if a task is initialized after the flight, the deadline start value is
    * compared to the flight usage value.
    *
    */
   @Test
   public void itAdjustsCalcParmDeadlineOfEngineTrkComponentTaskAfterFlightWhenSchedFromLastEnd()
         throws Exception {

      // Important!
      // addEquationFunctionToDatabase() needs to be called prior to any data setup in database
      // as the method performs an explicit roll-back.
      addEquationFunctionToDatabase();

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
      ConfigSlotKey lTrkConfigSlot =
            Domain.readSubConfigurationSlot( lRootConfigSlot, TRK_CONFIG_SLOT_CODE );

      final ConfigSlotPositionKey lTrkConfigSlotPos = new ConfigSlotPositionKey( lTrkConfigSlot,
            EqpAssmblPos.getFirstPosId( lTrkConfigSlot ) );

      final DataTypeKey lDataType_CALC_PARM_FOR_HOURS =
            Domain.readUsageParameter( lRootConfigSlot, CALC_PARM_DATA_TYPE_FOR_HOURS );

      final InventoryKey lTrk =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aTrk ) {
                  aTrk.setPosition( lTrkConfigSlotPos );
                  aTrk.addUsage( HOURS, CURRENT_HOURS );
                  aTrk.addUsage( lDataType_CALC_PARM_FOR_HOURS, CURRENT_CALC_PARM_HOURS );
               }
            } );

      final InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.addUsage( HOURS, CURRENT_HOURS );
            aEngine.addUsage( lDataType_CALC_PARM_FOR_HOURS, CURRENT_CALC_PARM_HOURS );
            aEngine.addTracked( lTrk );
         }

      } );

      // Given an aircraft collecting usage.
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( HOURS, CURRENT_HOURS );
            aAircraft.addUsage( lDataType_CALC_PARM_FOR_HOURS, CURRENT_CALC_PARM_HOURS );
            aAircraft.addEngine( lEngine );
         }
      } );

      // Given a flight for the aircraft that accrued usage.
      FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( FLIGHT_DATE );
            aFlight.addUsage( lAircraft, HOURS, FIRST_FLIGHT_HOURS, FIRST_FLIGHT_HOURS );
            aFlight.addUsage( lEngine, HOURS, FIRST_FLIGHT_HOURS, FIRST_FLIGHT_HOURS );
         }
      } );

      // Note: The task is considered initialized after the flight when its deadline start value is
      // greater than the flight usage value.
      final BigDecimal lPrevDeadlineStartValue =
            CALC_PARM_HOURS_AT_FLIGHT.add( BigDecimal.valueOf( 2 ) );
      final BigDecimal lDeadlineInterval = BigDecimal.valueOf( 5 );
      final BigDecimal lLastEndValue = lPrevDeadlineStartValue.add( BigDecimal.valueOf( 4 ) );

      final TaskKey lPreviousReq =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement aReq ) {
                  UsageSnapshot lUsageSnapshotCalc =
                        new UsageSnapshot( lTrk, lDataType_CALC_PARM_FOR_HOURS, lLastEndValue );
                  UsageSnapshot lUsageSnapshot = new UsageSnapshot( lTrk, HOURS,
                        lLastEndValue.divide( CALC_PARM_CONSTANT ) );
                  aReq.setInventory( lTrk );
                  aReq.setStatus( RefEventStatusKey.COMPLETE );
                  aReq.setActualEndDate( DateUtils.addDays( FLIGHT_DATE, 2 ) );
                  aReq.addUsage( lUsageSnapshot );
                  aReq.addUsage( lUsageSnapshotCalc );
                  aReq.addUsageDeadline( lDataType_CALC_PARM_FOR_HOURS, RefSchedFromKey.LASTEND,
                        lPrevDeadlineStartValue, lDeadlineInterval,
                        lPrevDeadlineStartValue.add( lDeadlineInterval ) );
               }
            } );

      TaskKey lReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setInventory( lTrk );
            aReq.setPreviousTask( lPreviousReq );
            aReq.addUsageDeadline( lDataType_CALC_PARM_FOR_HOURS, RefSchedFromKey.LASTEND,
                  lLastEndValue, lDeadlineInterval, lLastEndValue.add( lDeadlineInterval ) );
         }
      } );

      // When the flight is edited and the usage is modified.
      FlightInformationTO lFlightInfoTO =
            generateFlightInfoTO( FLIGHT_NAME, FLIGHT_DATE, EDITED_FLIGHT_HOURS_DIFF );
      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( lAircraft, HOURS, EDITED_FLIGHT_HOURS ),
                  generateFlightUsage( lEngine, HOURS, EDITED_FLIGHT_HOURS ) };

      iFlightHistBean.editHistFlight( lFlight, createHumanResource(), lFlightInfoTO,
            lEditUsageParms, NO_MEASUREMENTS );

      // Then the task's deadline start value and due value are recalculated based on the usage
      // delta
      Double lExpectedStartValue = lLastEndValue
            .add( EDITED_FLIGHT_HOURS_DIFF.multiply( CALC_PARM_CONSTANT ) ).doubleValue();
      Double lExpectedDueValue = lLastEndValue.add( lDeadlineInterval )
            .add( EDITED_FLIGHT_HOURS_DIFF.multiply( CALC_PARM_CONSTANT ) ).doubleValue();

      EvtSchedDeadTable lEvtSchedDead =
            EvtSchedDeadTable.findByPrimaryKey( lReq, lDataType_CALC_PARM_FOR_HOURS );
      Double lActualStartValue = lEvtSchedDead.getStartQt();
      Double lActualDueValue = lEvtSchedDead.getDeadlineQt();

      {
         // Important!
         // All data needed for assertions must be retrieved prior to calling
         // dropEquationFunctionToDatabase(). As it performs an explicit roll-back.
         dropEquationFunctionToDatabase();
      }

      assertEquals( "Unexpected deadline start value.", lExpectedStartValue, lActualStartValue );
      assertEquals( "Unexpected deadline due value.", lExpectedDueValue, lActualDueValue );
   }


   /**
    * Given an aircraft and an engine with TRK subcomponent connected to it and all these
    * inventories are collecting usages and a calculated parameter
    *
    * And the aircraft has a historical flight
    *
    * And there is a task completed against the subcomponent
    *
    * And another task with calculated parameter based usage deadline is initialized on the
    * subcomponent with deadline scheduled from EFFECTIVE date and after the flight
    *
    * When the historical flight is edited and its usage modified
    *
    * Then the task's deadline information is modified accordingly.
    *
    * Note: to determine if a task is initialized after the flight, the deadline start value is
    * compared to the flight usage value.
    */
   @Test
   public void itAdjustsCalcParmDeadlineOfEngineTrkComponentTaskAfterFlightWhenSchedFromEffective()
         throws Exception {

      // Important!
      // addEquationFunctionToDatabase() needs to be called prior to any data setup in database
      // as the method performs an explicit roll-back.
      addEquationFunctionToDatabase();

      // Set up the parameter and calculated-parameter on the root config slot.
      final AssemblyKey lAcftAssembly = createAcftAssyTrackingUsageParmsAndCalcParms();

      // Get the data type keys for the calculated parameters.
      final DataTypeKey lDataType_CALC_PARM_FOR_HOURS = Domain.readUsageParameter(
            Domain.readRootConfigurationSlot( lAcftAssembly ), CALC_PARM_DATA_TYPE_FOR_HOURS );

      final InventoryKey lTrk =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aTrk ) {
                  aTrk.addUsage( HOURS, CURRENT_HOURS );
                  aTrk.addUsage( lDataType_CALC_PARM_FOR_HOURS, CURRENT_CALC_PARM_HOURS );
               }
            } );

      final InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.addUsage( HOURS, CURRENT_HOURS );
            aEngine.addUsage( lDataType_CALC_PARM_FOR_HOURS, CURRENT_CALC_PARM_HOURS );
            aEngine.addTracked( lTrk );
         }

      } );

      // Given an aircraft collecting usage.
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( HOURS, CURRENT_HOURS );
            aAircraft.addUsage( lDataType_CALC_PARM_FOR_HOURS, CURRENT_CALC_PARM_HOURS );
            aAircraft.addEngine( lEngine );
         }
      } );

      // Given a flight for the aircraft that accrued usage.
      FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( FLIGHT_DATE );
            aFlight.addUsage( lAircraft, HOURS, FIRST_FLIGHT_HOURS, FIRST_FLIGHT_HOURS );
            aFlight.addUsage( lEngine, HOURS, FIRST_FLIGHT_HOURS, FIRST_FLIGHT_HOURS );
         }
      } );

      // Note: The task is considered initialized after the flight when its deadline start value is
      // greater than the flight usage value.

      final BigDecimal lDeadlineStartValue = CALC_PARM_HOURS_AT_FLIGHT;
      final BigDecimal lDeadlineInterval = BigDecimal.valueOf( 2 );
      final BigDecimal lDeadlineDueValue = lDeadlineStartValue.add( lDeadlineInterval );

      final Date lTaskDefnEffectiveDate = DateUtils.addDays( FLIGHT_DATE, 1 );
      final TaskTaskKey lTaskDefnKey =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aRequirementDefinition ) {
                  aRequirementDefinition.setScheduledFromEffectiveDate( lTaskDefnEffectiveDate );
               }

            } );

      // dummy task for we are not editing the latest flight, so oos logic will be processed.
      Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setInventory( lTrk );
            aReq.setDefinition( lTaskDefnKey );
            aReq.setStatus( RefEventStatusKey.COMPLETE );
            aReq.setActualEndDate( DateUtils.addDays( FLIGHT_DATE, 2 ) );
         }
      } );

      TaskKey lReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setInventory( lTrk );
            aReq.setDefinition( lTaskDefnKey );
            aReq.setStatus( RefEventStatusKey.ACTV );
            aReq.addUsageDeadline( lDataType_CALC_PARM_FOR_HOURS, RefSchedFromKey.EFFECTIV,
                  lDeadlineStartValue, lDeadlineInterval, lDeadlineDueValue );
         }
      } );

      // When the flight is edited and the usage is modified.
      FlightInformationTO lFlightInfoTO =
            generateFlightInfoTO( FLIGHT_NAME, FLIGHT_DATE, EDITED_FLIGHT_HOURS_DIFF );
      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( lAircraft, HOURS, EDITED_FLIGHT_HOURS ),
                  generateFlightUsage( lEngine, HOURS, EDITED_FLIGHT_HOURS ) };

      iFlightHistBean.editHistFlight( lFlight, createHumanResource(), lFlightInfoTO,
            lEditUsageParms, NO_MEASUREMENTS );

      // Then the task's deadline start value and due value are recalculated based on the usage
      // delta
      Double lExpectedStartValue = lDeadlineStartValue
            .add( EDITED_FLIGHT_HOURS_DIFF.multiply( CALC_PARM_CONSTANT ) ).doubleValue();
      Double lExpectedDueValue = lDeadlineDueValue
            .add( EDITED_FLIGHT_HOURS_DIFF.multiply( CALC_PARM_CONSTANT ) ).doubleValue();

      EvtSchedDeadTable lEvtSchedDead =
            EvtSchedDeadTable.findByPrimaryKey( lReq, lDataType_CALC_PARM_FOR_HOURS );
      Double lActualStartValue = lEvtSchedDead.getStartQt();
      Double lActualDueValue = lEvtSchedDead.getDeadlineQt();

      {
         // Important!
         // All data needed for assertions must be retrieved prior to calling
         // dropEquationFunctionToDatabase(). As it performs an explicit roll-back.
         dropEquationFunctionToDatabase();
      }

      assertEquals( "Unexpected deadline start value.", lExpectedStartValue, lActualStartValue );
      assertEquals( "Unexpected deadline due value.", lExpectedDueValue, lActualDueValue );
   }


   /**
    * Given an aircraft and an engine with TRK subcomponent connected to it and all these
    * inventories are collecting usages and a calculated parameter
    *
    * And the aircraft has a historical flight
    *
    * And a task with calculated parameter based usage deadline is initialized on the aircraft with
    * deadline scheduled from CUSTOM date
    *
    * When the historical flight is edited and its usage modified
    *
    * Then the task's deadline information is not affected.
    *
    * Note: to determine if a task is initialized after the flight, the deadline start value is
    * compared to the flight usage value.
    */
   @Test
   public void
         itDoesNotAdjustCalcParmDeadlineOfEngineTrkComponentTaskAfterFlightWhenSchedFromCustomDate()
               throws Exception {

      // Important!
      // addEquationFunctionToDatabase() needs to be called prior to any data setup in database
      // as the method performs an explicit roll-back.
      addEquationFunctionToDatabase();

      // Set up the parameter and calculated-parameter on the root config slot.
      final AssemblyKey lAcftAssembly = createAcftAssyTrackingUsageParmsAndCalcParms();

      // Get the data type keys for the calculated parameters.
      final DataTypeKey lDataType_CALC_PARM_FOR_HOURS = Domain.readUsageParameter(
            Domain.readRootConfigurationSlot( lAcftAssembly ), CALC_PARM_DATA_TYPE_FOR_HOURS );

      final InventoryKey lTrk =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aTrk ) {
                  aTrk.addUsage( HOURS, CURRENT_HOURS );
                  aTrk.addUsage( lDataType_CALC_PARM_FOR_HOURS, CURRENT_CALC_PARM_HOURS );
               }
            } );

      final InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.addUsage( HOURS, CURRENT_HOURS );
            aEngine.addUsage( lDataType_CALC_PARM_FOR_HOURS, CURRENT_CALC_PARM_HOURS );
            aEngine.addTracked( lTrk );
         }

      } );

      // Given an aircraft collecting usage.
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( HOURS, CURRENT_HOURS );
            aAircraft.addUsage( lDataType_CALC_PARM_FOR_HOURS, CURRENT_CALC_PARM_HOURS );
            aAircraft.addEngine( lEngine );
         }
      } );

      // Given a flight for the aircraft that accrued usage.
      FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraft );
            aFlight.setHistorical( true );
            aFlight.setArrivalDate( FLIGHT_DATE );
            aFlight.addUsage( lAircraft, HOURS, FIRST_FLIGHT_HOURS, FIRST_FLIGHT_HOURS );
            aFlight.addUsage( lEngine, HOURS, FIRST_FLIGHT_HOURS, FIRST_FLIGHT_HOURS );
         }
      } );

      // Note: The task is considered initialized after the flight when its deadline start value is
      // greater than the flight usage value.

      final BigDecimal lDeadlineInterval = BigDecimal.valueOf( 2 );
      final BigDecimal lDeadlineStartValue = CALC_PARM_HOURS_AT_FLIGHT.add( BigDecimal.ONE );
      final BigDecimal lDeadlineDueValue = lDeadlineStartValue.add( lDeadlineInterval );

      TaskKey lReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setInventory( lTrk );
            aReq.addUsageDeadline( lDataType_CALC_PARM_FOR_HOURS, RefSchedFromKey.CUSTOM,
                  lDeadlineStartValue, lDeadlineInterval, lDeadlineDueValue );
         }
      } );

      // When the flight is edited and the usage is modified.
      FlightInformationTO lFlightInfoTO =
            generateFlightInfoTO( FLIGHT_NAME, FLIGHT_DATE, EDITED_FLIGHT_HOURS_DIFF );
      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( lAircraft, HOURS, EDITED_FLIGHT_HOURS ),
                  generateFlightUsage( lEngine, HOURS, EDITED_FLIGHT_HOURS ) };

      iFlightHistBean.editHistFlight( lFlight, createHumanResource(), lFlightInfoTO,
            lEditUsageParms, NO_MEASUREMENTS );

      // Then the task's deadline start value and due value are recalculated based on the usage
      // delta
      Double lExpectedStartValue = lDeadlineStartValue.doubleValue();
      Double lExpectedDueValue = lDeadlineDueValue.doubleValue();

      EvtSchedDeadTable lEvtSchedDead =
            EvtSchedDeadTable.findByPrimaryKey( lReq, lDataType_CALC_PARM_FOR_HOURS );
      Double lActualStartValue = lEvtSchedDead.getStartQt();
      Double lActualDueValue = lEvtSchedDead.getDeadlineQt();

      {
         // Important!
         // All data needed for assertions must be retrieved prior to calling
         // dropEquationFunctionToDatabase(). As it performs an explicit roll-back.
         dropEquationFunctionToDatabase();
      }

      assertEquals( "Unexpected deadline start value.", lExpectedStartValue, lActualStartValue );
      assertEquals( "Unexpected deadline due value.", lExpectedDueValue, lActualDueValue );
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
   public void teardown() {
      iFlightHistBean.setSessionContext( null );
   }


   private AssemblyKey createAcftAssyTrackingUsageParmsAndCalcParms() {
      AssemblyKey lAircraftAssembly =
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
                                    aBuilder.setConfigurationSlotClass( SYS );
                                    aBuilder.setCode( SYS_CONFIG_SLOT_CODE );
                                 }
                              } );

                        // Add an Engine config slot to the root config slot.
                        aBuilder
                              .addConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                                 @Override
                                 public void configure( ConfigurationSlot aBuilder ) {
                                    aBuilder.setConfigurationSlotClass( SUBASSY );
                                    aBuilder.setCode( ENGINE_CONFIG_SLOT_CODE );
                                 }
                              } );

                        // Track a parameter.
                        aBuilder.addUsageParameter( HOURS );

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
                     }
                  } );
               }
            } );
      return lAircraftAssembly;
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
      Domain.dropCalculatedParameterEquationFunction( iDatabaseConnectionRule.getConnection(),
            CALC_PARM_FUNCTION_NAME );
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
      return new FlightInformationTO( aName, null, null, null, null, null, lDepartureAirport,
            lArrivalAirport, null, null, null, null, lDepartureDate, aArrivalDate, null, null,
            false, false );
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
      UserParameters.setInstance( lUserId, "LOGIC", new UserParametersFake( lUserId, "LOGIC" ) );
      return lHrKey;
   }
}
