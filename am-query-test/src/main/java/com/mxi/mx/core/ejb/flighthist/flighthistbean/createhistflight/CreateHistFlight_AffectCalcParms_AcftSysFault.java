package com.mxi.mx.core.ejb.flighthist.flighthistbean.createhistflight;

import static com.mxi.am.domain.Domain.readUsageParameter;
import static com.mxi.mx.core.key.DataTypeKey.CYCLES;
import static com.mxi.mx.core.key.DataTypeKey.HOURS;
import static com.mxi.mx.core.key.RefEventStatusKey.COMPLETE;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
import com.mxi.am.domain.CorrectiveTask;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Fault;
import com.mxi.am.domain.Location;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.servlet.SessionContextFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.ejb.flighthist.FlightHistBean;
import com.mxi.mx.core.flight.model.FlightLegId;
import com.mxi.mx.core.key.AircraftKey;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.EventInventoryKey;
import com.mxi.mx.core.key.EventInventoryUsageKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefDataSourceKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.UsageDefinitionKey;
import com.mxi.mx.core.key.UsageParmKey;
import com.mxi.mx.core.services.event.usage.CollectedUsageParm;
import com.mxi.mx.core.services.flighthist.FlightInformationTO;
import com.mxi.mx.core.services.flighthist.Measurement;
import com.mxi.mx.core.table.eqp.EqpDataSourceSpec;
import com.mxi.mx.core.table.evt.EvtInvTable;
import com.mxi.mx.core.table.evt.EvtInvUsage;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.org.OrgHr;


/**
 * Tests for {@linkplain FlightHistBean#createHistFlight} that involve updating calculated
 * parameters for fault completion usage snapshots of aircraft systems.
 *
 */
public class CreateHistFlight_AffectCalcParms_AcftSysFault {

   // Calculated parameter data types, equation, and constant.
   // The equation is simply a multiplier of the target usage parameter by the multiplier constant.
   private static final String MULTIPLIER_FUNCTION_NAME = "MULTIPLIER_FUNCTION_NAME";
   private static final String MULTIPLIER_CONSTANT_NAME = "MULTIPLIER_CONSTANT_NAME";
   private static final BigDecimal MULTIPLIER_CONSTANT = BigDecimal.valueOf( 2.0 );

   final static private BigDecimal ORIG_CYCLES = BigDecimal.valueOf( 1.2 );
   final static private BigDecimal ORIG_HOURS = BigDecimal.valueOf( 3.4 );

   private static final Date FAULT_DATE = DateUtils.addDays( new Date(), -100 );
   private static final BigDecimal FAULT_CYCLES = BigDecimal.valueOf( 5.6 );
   private static final BigDecimal FAULT_HOURS = BigDecimal.valueOf( 7.8 );

   private static final String CYCLES_BASED_CALC_PARM_CODE = "CYCLES_BASED_CALC_PARM_CODE";
   private static final String HOURS_BASED_CALC_PARM_CODE = "HOURS_BASED_CALC_PARM_CODE";

   // Variables that are mandatory but not relevant to the test.
   private static final Measurement[] NO_MEASUREMENTS = new Measurement[] {};
   private static final String HR_USERNAME = "HR_USERNAME";
   private static final String FLIGHT_NAME = "FLIGHT_NAME";
   private static final String SYSTEM_CODE = "SYSTEM_CODE";

   private FlightHistBean iFlightHistBean;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Verify that the calculated parameters within an aircraft system fault's usage-at-completion
    * snapshot are re-calculated when a flight is created before the snapshot and that flight
    * captures usage parameters on which the calculated parameters are based.
    */
   @Test
   public void itRecalculatesCalcParmsWhenFlightCreatedBeforeFault() throws Exception {

      {
         // Important!
         // addEquationFunctionToDatabase() needs to be called prior to any data setup in database
         // as the method performs an explicit roll-back.
         addEquationFunctionToDatabase();
      }

      //
      // Given an aircraft system tracking both parameters and a calculated-parameters that are
      // based on those parameters.

      // Note: Refer to the private create methods for the constants being used.
      //
      AssemblyKey lAcftAssy = createAcftAssyWithSysTrackingParmsAndCalcParms();

      ConfigSlotKey lRootCs = Domain.readRootConfigurationSlot( lAcftAssy );
      ConfigSlotKey lSysCs = Domain.readSubConfigurationSlot( lRootCs, SYSTEM_CODE );

      DataTypeKey lCyclesBasedCalcParm = readUsageParameter( lSysCs, CYCLES_BASED_CALC_PARM_CODE );
      DataTypeKey lHoursBasedCalcParm = readUsageParameter( lSysCs, HOURS_BASED_CALC_PARM_CODE );

      InventoryKey lAircraft =
            createAircraft( lAcftAssy, CYCLES, HOURS, lCyclesBasedCalcParm, lHoursBasedCalcParm );
      InventoryKey lSystem = Domain.readSystem( lAircraft, SYSTEM_CODE );

      //
      // Given a completed fault against the system.
      //
      // Note: faults themselves are not completed, it is actually the corrective task associated to
      // the fault that gets completed. However, once the task is completed we consider the fault
      // completed.
      //
      // The usage-at-completion usage snapshot is taken for the corrective task (not the fault).
      //
      Map<DataTypeKey, BigDecimal> lFaultUsageSnapshot = new HashMap<DataTypeKey, BigDecimal>();
      lFaultUsageSnapshot.put( CYCLES, FAULT_CYCLES );
      lFaultUsageSnapshot.put( HOURS, FAULT_HOURS );
      lFaultUsageSnapshot.put( lCyclesBasedCalcParm, FAULT_CYCLES.multiply( MULTIPLIER_CONSTANT ) );
      lFaultUsageSnapshot.put( lHoursBasedCalcParm, FAULT_HOURS.multiply( MULTIPLIER_CONSTANT ) );

      final TaskKey lCorrectiveTask =
            createCompletedFault( lSystem, FAULT_DATE, lFaultUsageSnapshot );

      //
      // When a flight is created before the fault.
      //
      Date lFlightDate = DateUtils.addDays( FAULT_DATE, -5 );

      createHistoricalFlight( lAircraft, lFlightDate, ORIG_CYCLES, ORIG_HOURS );

      //
      // Then the calculated parameters within the fault's usage-at-completion snapshot are
      // re-calculated.
      //
      // The expected calc parm values use the following logic:
      // (fault completion usage + flight usage) X multiplier
      //
      BigDecimal lCyclesBasedCalcParmValue =
            readActualCalcParm( lSystem, lCorrectiveTask, lCyclesBasedCalcParm );
      BigDecimal lHoursBasedCalcParmValue =
            readActualCalcParm( lSystem, lCorrectiveTask, lHoursBasedCalcParm );

      {
         // Important!
         // All data needed for assertions must be retrieved prior to calling
         // dropEquationFunctionToDatabase(). As it performs an explicit roll-back.
         //
         // ALSO, all assertions must be done after dropEquationFunctionToDatabase() to ensure the
         // equation gets removed from the DB.
         dropEquationFunctionToDatabase();
      }

      // CYCLES based calc parm
      BigDecimal lAdjustedFaultUsage = FAULT_CYCLES.add( ORIG_CYCLES );

      BigDecimal lExpected = lAdjustedFaultUsage.multiply( MULTIPLIER_CONSTANT );
      BigDecimal lActual = lCyclesBasedCalcParmValue;
      Assert.assertTrue( "Unexpected value for calculated parameter " + lCyclesBasedCalcParm
            + "; expected=" + lExpected + ", actual=" + lActual,
            lExpected.compareTo( lActual ) == 0 );

      // HOURS based calc parm
      lAdjustedFaultUsage = FAULT_HOURS.add( ORIG_HOURS );

      lExpected = lAdjustedFaultUsage.multiply( MULTIPLIER_CONSTANT );
      lActual = lHoursBasedCalcParmValue;
      Assert.assertTrue( "Unexpected value for calculated parameter " + lHoursBasedCalcParm
            + "; expected=" + lExpected + ", actual=" + lActual,
            lExpected.compareTo( lActual ) == 0 );

   }


   /**
    * Verify that the calculated parameters within an aircraft system fault's usage-at-completion
    * snapshot are re-calculated when a flight is created at the same time as the snapshot and that
    * flight captures usage parameters on which the calculated parameters are based.
    */
   @Test
   public void itRecalculatesCalcParmsWhenFlightCreatedAtSameTimeAsFault() throws Exception {

      {
         // Important!
         // addEquationFunctionToDatabase() needs to be called prior to any data setup in database
         // as the method performs an explicit roll-back.
         addEquationFunctionToDatabase();
      }

      //
      // Given an aircraft system tracking both parameters and a calculated-parameters that are
      // based on those parameters.

      // Note: Refer to the private create methods for the constants being used.
      //
      AssemblyKey lAcftAssy = createAcftAssyWithSysTrackingParmsAndCalcParms();

      ConfigSlotKey lRootCs = Domain.readRootConfigurationSlot( lAcftAssy );
      ConfigSlotKey lSysCs = Domain.readSubConfigurationSlot( lRootCs, SYSTEM_CODE );

      DataTypeKey lCyclesBasedCalcParm = readUsageParameter( lSysCs, CYCLES_BASED_CALC_PARM_CODE );
      DataTypeKey lHoursBasedCalcParm = readUsageParameter( lSysCs, HOURS_BASED_CALC_PARM_CODE );

      InventoryKey lAircraft =
            createAircraft( lAcftAssy, CYCLES, HOURS, lCyclesBasedCalcParm, lHoursBasedCalcParm );
      InventoryKey lSystem = Domain.readSystem( lAircraft, SYSTEM_CODE );

      //
      // Given a completed fault against the system.
      //
      // Note: faults themselves are not completed, it is actually the corrective task associated to
      // the fault that gets completed. However, once the task is completed we consider the fault
      // completed.
      //
      // The usage-at-completion usage snapshot is taken for the corrective task (not the fault).
      //
      Map<DataTypeKey, BigDecimal> lFaultUsageSnapshot = new HashMap<DataTypeKey, BigDecimal>();
      lFaultUsageSnapshot.put( CYCLES, FAULT_CYCLES );
      lFaultUsageSnapshot.put( HOURS, FAULT_HOURS );
      lFaultUsageSnapshot.put( lCyclesBasedCalcParm, FAULT_CYCLES.multiply( MULTIPLIER_CONSTANT ) );
      lFaultUsageSnapshot.put( lHoursBasedCalcParm, FAULT_HOURS.multiply( MULTIPLIER_CONSTANT ) );

      final TaskKey lCorrectiveTask =
            createCompletedFault( lSystem, FAULT_DATE, lFaultUsageSnapshot );

      //
      // When a flight is created at the same time as the fault.
      //
      Date lFlightDate = FAULT_DATE;

      createHistoricalFlight( lAircraft, lFlightDate, ORIG_CYCLES, ORIG_HOURS );

      //
      // Then the calculated parameters within the fault's usage-at-completion snapshot are
      // re-calculated.
      //
      // The expected calc parm values use the following logic:
      // (fault completion usage + flight usage) X multiplier
      //
      BigDecimal lCyclesBasedCalcParmValue =
            readActualCalcParm( lSystem, lCorrectiveTask, lCyclesBasedCalcParm );
      BigDecimal lHoursBasedCalcParmValue =
            readActualCalcParm( lSystem, lCorrectiveTask, lHoursBasedCalcParm );

      {
         // Important!
         // All data needed for assertions must be retrieved prior to calling
         // dropEquationFunctionToDatabase(). As it performs an explicit roll-back.
         //
         // ALSO, all assertions must be done after dropEquationFunctionToDatabase() to ensure the
         // equation gets removed from the DB.
         dropEquationFunctionToDatabase();
      }

      // CYCLES based calc parm
      BigDecimal lAdjustedFaultUsage = FAULT_CYCLES.add( ORIG_CYCLES );

      BigDecimal lExpected = lAdjustedFaultUsage.multiply( MULTIPLIER_CONSTANT );
      BigDecimal lActual = lCyclesBasedCalcParmValue;
      Assert.assertTrue( "Unexpected value for calculated parameter " + lCyclesBasedCalcParm
            + "; expected=" + lExpected + ", actual=" + lActual,
            lExpected.compareTo( lActual ) == 0 );

      // HOURS based calc parm
      lAdjustedFaultUsage = FAULT_HOURS.add( ORIG_HOURS );

      lExpected = lAdjustedFaultUsage.multiply( MULTIPLIER_CONSTANT );
      lActual = lHoursBasedCalcParmValue;
      Assert.assertTrue( "Unexpected value for calculated parameter " + lHoursBasedCalcParm
            + "; expected=" + lExpected + ", actual=" + lActual,
            lExpected.compareTo( lActual ) == 0 );

   }


   /**
    * Verify that the calculated parameters within an aircraft system fault's usage-at-completion
    * snapshot are NOT re-calculated when a flight is created after the snapshot and that flight
    * captures usage parameters on which the calculated parameters are based.
    */
   @Test
   public void itDoesNotRecalculatesCalcParmsWhenFlightCreatedAfterFault() throws Exception {

      {
         // Important!
         // addEquationFunctionToDatabase() needs to be called prior to any data setup in database
         // as the method performs an explicit roll-back.
         addEquationFunctionToDatabase();
      }

      //
      // Given an aircraft system tracking both parameters and a calculated-parameters that are
      // based on those parameters.

      // Note: Refer to the private create methods for the constants being used.
      //
      AssemblyKey lAcftAssy = createAcftAssyWithSysTrackingParmsAndCalcParms();

      ConfigSlotKey lRootCs = Domain.readRootConfigurationSlot( lAcftAssy );
      ConfigSlotKey lSysCs = Domain.readSubConfigurationSlot( lRootCs, SYSTEM_CODE );

      DataTypeKey lCyclesBasedCalcParm = readUsageParameter( lSysCs, CYCLES_BASED_CALC_PARM_CODE );
      DataTypeKey lHoursBasedCalcParm = readUsageParameter( lSysCs, HOURS_BASED_CALC_PARM_CODE );

      InventoryKey lAircraft =
            createAircraft( lAcftAssy, CYCLES, HOURS, lCyclesBasedCalcParm, lHoursBasedCalcParm );
      InventoryKey lSystem = Domain.readSystem( lAircraft, SYSTEM_CODE );

      //
      // Given a completed fault against the system.
      //
      // Note: faults themselves are not completed, it is actually the corrective task associated to
      // the fault that gets completed. However, once the task is completed we consider the fault
      // completed.
      //
      // The usage-at-completion usage snapshot is taken for the corrective task (not the fault).
      //
      Map<DataTypeKey, BigDecimal> lFaultUsageSnapshot = new HashMap<DataTypeKey, BigDecimal>();
      lFaultUsageSnapshot.put( CYCLES, FAULT_CYCLES );
      lFaultUsageSnapshot.put( HOURS, FAULT_HOURS );
      lFaultUsageSnapshot.put( lCyclesBasedCalcParm, FAULT_CYCLES.multiply( MULTIPLIER_CONSTANT ) );
      lFaultUsageSnapshot.put( lHoursBasedCalcParm, FAULT_HOURS.multiply( MULTIPLIER_CONSTANT ) );

      final TaskKey lCorrectiveTask =
            createCompletedFault( lSystem, FAULT_DATE, lFaultUsageSnapshot );

      //
      // When a flight is created after the fault.
      //
      Date lFlightDate = DateUtils.addDays( FAULT_DATE, +5 );

      createHistoricalFlight( lAircraft, lFlightDate, ORIG_CYCLES, ORIG_HOURS );

      //
      // Then the calculated parameters within the fault's usage-at-completion snapshot are
      // NOT re-calculated (they remain the fault completion usage X multiplier).
      //
      BigDecimal lCyclesBasedCalcParmValue =
            readActualCalcParm( lSystem, lCorrectiveTask, lCyclesBasedCalcParm );
      BigDecimal lHoursBasedCalcParmValue =
            readActualCalcParm( lSystem, lCorrectiveTask, lHoursBasedCalcParm );

      {
         // Important!
         // All data needed for assertions must be retrieved prior to calling
         // dropEquationFunctionToDatabase(). As it performs an explicit roll-back.
         //
         // ALSO, all assertions must be done after dropEquationFunctionToDatabase() to ensure the
         // equation gets removed from the DB.
         dropEquationFunctionToDatabase();
      }

      // CYCLES based calc parm
      BigDecimal lExpected = FAULT_CYCLES.multiply( MULTIPLIER_CONSTANT );
      BigDecimal lActual = lCyclesBasedCalcParmValue;
      Assert.assertTrue( "Unexpected value for calculated parameter " + lCyclesBasedCalcParm
            + "; expected=" + lExpected + ", actual=" + lActual,
            lExpected.compareTo( lActual ) == 0 );

      // HOURS based calc parm
      lExpected = FAULT_HOURS.multiply( MULTIPLIER_CONSTANT );
      lActual = lHoursBasedCalcParmValue;
      Assert.assertTrue( "Unexpected value for calculated parameter " + lHoursBasedCalcParm
            + "; expected=" + lExpected + ", actual=" + lActual,
            lExpected.compareTo( lActual ) == 0 );

   }


   @Before
   public void before() {
      // Important!
      // Be aware that any data setup in the database within this setup() will be rolled back if the
      // test contains a call to addEquationFunctionToDatabase().

      iFlightHistBean = new FlightHistBean();
      iFlightHistBean.ejbCreate();
      iFlightHistBean.setSessionContext( new SessionContextFake() );
   }


   @After
   public void after() {
      iFlightHistBean.setSessionContext( null );
   }


   // //////////////////////////////////////////////////////////////////////////
   //
   // Private methods
   //
   // //////////////////////////////////////////////////////////////////////////

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


   private AssemblyKey createAcftAssyWithSysTrackingParmsAndCalcParms() {

      return Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

         @Override
         public void configure( AircraftAssembly aBuilder ) {

            aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

               @Override
               public void configure( ConfigurationSlot aRootCs ) {

                  // Set up the parameters and calculated-parameters on a system config slot.
                  aRootCs.addConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot aSysCs ) {

                        aSysCs.setCode( SYSTEM_CODE );
                        aSysCs.addUsageParameter( CYCLES );
                        aSysCs.addUsageParameter( HOURS );

                        // Calculate parameter based on CYCLES.
                        aSysCs.addCalculatedUsageParameter(
                              new DomainConfiguration<CalculatedUsageParameter>() {

                                 @Override
                                 public void configure( CalculatedUsageParameter aBuilder ) {
                                    aBuilder.setCode( CYCLES_BASED_CALC_PARM_CODE );
                                    aBuilder.setDatabaseCalculation( MULTIPLIER_FUNCTION_NAME );
                                    aBuilder.setPrecisionQt( 2 );
                                    aBuilder.addParameter( CYCLES );
                                    aBuilder.addConstant( MULTIPLIER_CONSTANT_NAME,
                                          MULTIPLIER_CONSTANT );
                                 }

                              } );

                        // Calculate parameter based on HOURS.
                        aSysCs.addCalculatedUsageParameter(
                              new DomainConfiguration<CalculatedUsageParameter>() {

                                 @Override
                                 public void configure( CalculatedUsageParameter aBuilder ) {
                                    aBuilder.setCode( HOURS_BASED_CALC_PARM_CODE );
                                    aBuilder.setDatabaseCalculation( MULTIPLIER_FUNCTION_NAME );
                                    aBuilder.setPrecisionQt( 2 );
                                    aBuilder.addParameter( HOURS );
                                    aBuilder.addConstant( MULTIPLIER_CONSTANT_NAME,
                                          MULTIPLIER_CONSTANT );
                                 }

                              } );
                     }
                  } );
               }
            } );
         }
      } );
   }


   private InventoryKey createAircraft( final AssemblyKey aAcftAssembly,
         final DataTypeKey... aTrackedParms ) {

      return Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         // Current usage is not applicable to the tests.
         final BigDecimal lNotApplicableCurrentUsage = null;


         @Override
         public void configure( Aircraft aBuilder ) {
            aBuilder.setAssembly( aAcftAssembly );
            aBuilder.addSystem( SYSTEM_CODE );
            for ( DataTypeKey lTrackedParm : aTrackedParms ) {
               aBuilder.addUsage( lTrackedParm, lNotApplicableCurrentUsage );
            }
         }
      } );
   }


   private TaskKey createCompletedFault( final InventoryKey aAircraft, final Date aCompletionDate,
         final Map<DataTypeKey, BigDecimal> lUsageSnapshot ) {

      // Create the completed corrective task.
      final TaskKey lCorrectiveTask =
            Domain.createCorrectiveTask( new DomainConfiguration<CorrectiveTask>() {

               @Override
               public void configure( CorrectiveTask aBuilder ) {
                  aBuilder.setInventory( aAircraft );
                  aBuilder.setEndDate( aCompletionDate );
                  aBuilder.setStatus( COMPLETE );

                  for ( DataTypeKey lDataType : lUsageSnapshot.keySet() ) {
                     aBuilder.addUsageAtCompletion( lDataType, lUsageSnapshot.get( lDataType ) );
                  }
               }
            } );

      // Create a fault associated to the completed corrective task.
      Domain.createFault( new DomainConfiguration<Fault>() {

         @Override
         public void configure( Fault aBuilder ) {
            aBuilder.setCorrectiveTask( lCorrectiveTask );
         }
      } );

      return lCorrectiveTask;
   }


   private FlightLegId createHistoricalFlight( InventoryKey aAircraft, Date aArrivalDate,
         BigDecimal aCycles, BigDecimal aHours ) throws Exception {

      Date lDepartureDate = DateUtils.addHours( aArrivalDate, -1 );

      FlightInformationTO lFlightInfoTO =
            generateFlightInfoTO( FLIGHT_NAME, lDepartureDate, aArrivalDate );

      CollectedUsageParm[] lUsageParms = { generateFlightUsage( aAircraft, CYCLES, aCycles ),
            generateFlightUsage( aAircraft, HOURS, aHours ) };

      HumanResourceKey lHrKey = new HumanResourceDomainBuilder().withUsername( HR_USERNAME ).build();

      // Creating flights in the past requires MAX_FLIGHT_DAYS_IN_THE_PAST_VALUE to be set.
      int lUserId = OrgHr.findByPrimaryKey( lHrKey ).getUserId();
      UserParametersFake lFake = new UserParametersFake( lUserId, "LOGIC" );
      lFake.setInteger( "MAX_FLIGHT_DAYS_IN_THE_PAST_VALUE", 300 );
      UserParameters.setInstance( lUserId, "LOGIC", lFake );

      // Method under test.
      return iFlightHistBean.createHistFlight( new AircraftKey( aAircraft ), lHrKey, lFlightInfoTO,
            lUsageParms, NO_MEASUREMENTS );
   }


   private FlightInformationTO generateFlightInfoTO( String aFlightName, Date aActualDepartureDate,
         Date aActualArrivalDate ) {

      // Departure and arrival airports are required but not relevant to the test.
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

      CollectedUsageParm lUsageParm = new CollectedUsageParm(
            new UsageParmKey( aInventoryKey, lDataType ), lDelta.doubleValue() );

      // Create flight data source specifications.
      EqpDataSourceSpec
            .create( new UsageDefinitionKey( InvInvTable.getAssemblyByInventoryKey( aInventoryKey ),
                  RefDataSourceKey.MXFL ), lDataType );

      return lUsageParm;
   }


   private BigDecimal readActualCalcParm( InventoryKey aInv, TaskKey aTask,
         DataTypeKey aCalcParm ) {

      EventInventoryKey lEvtInvKey = EvtInvTable.findByEventAndInventory( aTask, aInv ).getPk();
      EventInventoryUsageKey lEvtInvUsageKey = new EventInventoryUsageKey( lEvtInvKey, aCalcParm );
      EvtInvUsage lEvtInvUsage = EvtInvUsage.findByPrimaryKey( lEvtInvUsageKey );

      return BigDecimal.valueOf( lEvtInvUsage.getTsnQt() );
   }

}
