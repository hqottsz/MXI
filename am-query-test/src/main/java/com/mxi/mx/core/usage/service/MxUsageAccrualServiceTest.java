package com.mxi.mx.core.usage.service;

import static com.mxi.mx.core.key.DataTypeKey.HOURS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.mxi.am.domain.Flight;
import com.mxi.am.domain.UsageAdjustment;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.adapter.flight.messages.flight.adapter.logic.ActualDatesCalculator;
import com.mxi.mx.core.flight.model.FlightLegId;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.UsageParmKey;
import com.mxi.mx.core.services.stask.deadline.MxSoftDeadlineService;
import com.mxi.mx.core.services.stask.deadline.SoftDeadlineService;
import com.mxi.mx.core.services.stask.deadline.updatedeadline.UpdateDeadlineService;
import com.mxi.mx.core.services.stask.deadline.updatedeadline.logic.MxUpdateDeadlineService;
import com.mxi.mx.core.table.inv.InvCurrUsage;
import com.mxi.mx.core.usage.model.UsageAdjustmentId;
import com.mxi.mx.core.usage.model.UsageType;


/**
 * Tests the behaviours of {@linkplain MxUsageAccrualService}
 *
 */
public class MxUsageAccrualServiceTest {

   private static final Date COMPLETION_DATE = new Date();
   private static final String DESCRIPTION = "DESCRIPTION";
   private static final String DOCUMENT_REFERENCE = "DOCUMENT_REFERENCE";
   private static final HumanResourceKey HR = new HumanResourceKey( 1, 1 );
   private static final String USAGE_NAME = "USAGE_NAME";
   private static final UsageType USAGE_TYPE = UsageType.MXFLIGHT;

   private static final String CALC_USAGE_PARM_CODE = "CALC_USAGE_PARM_CODE";
   private static final String HOURS_TO_MINUTES_FUNCTION = "HOURS_TO_MINUTES_FUNCTION";
   private static final BigDecimal MINUTES_PER_HOUR = new BigDecimal( 60 );
   private static final String MINUTES_PER_HOUR_CONSTANT_NAME = "MINUTES_PER_HOUR_CONSTANT_NAME";

   private UsageAccrualService iUsageAccrualService;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Verify that when usage is accrued for an inventory that the usage is applied to the
    * inventory's current usage.
    *
    * <pre>
    * Given an aircraft with usage parameters and current usage
    *  When usage is accrued with usage deltas
    *  Then the current usage of the aircraft is adjusted by those delta values
    * </pre>
    *
    */
   @Test
   public void testCurrentUsageIsAdjustedByUsageDeltas() throws Exception {

      final BigDecimal lCurrentCycles = new BigDecimal( 100 );
      final BigDecimal lCurrentHours = new BigDecimal( 1000 );

      final BigDecimal lCyclesDeltaValue = new BigDecimal( -30 );
      final BigDecimal lHoursDeltaValue = new BigDecimal( 300 );

      // Given an aircraft with usage parameters and current usage.
      final InventoryKey lAircraftKey = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( DataTypeKey.CYCLES, lCurrentCycles );
            aAircraft.addUsage( DataTypeKey.HOURS, lCurrentHours );
         }
      } );

      // Ensure the current usage is expected prior to executing the test.
      assertCurrentUsage( lAircraftKey, lCurrentCycles, lCurrentHours );

      // When usage is accrued with usage deltas.
      List<UsageDelta> lUsageDeltas =
            generateUsageDeltas( lAircraftKey, lCyclesDeltaValue, lHoursDeltaValue );

      iUsageAccrualService.accrueUsage( lAircraftKey, HR, USAGE_NAME, COMPLETION_DATE, DESCRIPTION,
            DOCUMENT_REFERENCE, lUsageDeltas, USAGE_TYPE, null );

      // Then the current usage of the aircraft is adjusted by those delta values.
      BigDecimal lExpectedCycles = lCurrentCycles.add( lCyclesDeltaValue );
      BigDecimal lExpectedHours = lCurrentHours.add( lHoursDeltaValue );
      assertCurrentUsage( lAircraftKey, lExpectedCycles, lExpectedHours );

   }


   /**
    * Verify that when usage is accrued for an inventory and its subcomponents, that the usage is
    * applied to all the inventories' current usage.
    *
    * <pre>
    * Given an aircraft with usage parameters and current usage
    *   And an installed engine with usage parameters and current usage
    *  When usage is accrued with usage deltas for the aircraft and the engine
    *  Then the current usage of the aircraft and engine are adjusted by those delta values
    * </pre>
    *
    */
   @Test
   public void testCurrentUsageIsAdjustedByUsageDeltasForSubcomponents() throws Exception {

      final BigDecimal lAircraftCurrentCycles = new BigDecimal( 100 );
      final BigDecimal lAircraftCurrentHours = new BigDecimal( 1000 );
      final BigDecimal lEngineCurrentCycles = new BigDecimal( 25 );
      final BigDecimal lEngineCurrentHours = new BigDecimal( 50 );

      final BigDecimal lAircraftCyclesDeltaValue = new BigDecimal( -30 );
      final BigDecimal lAircraftHoursDeltaValue = new BigDecimal( 300 );
      final BigDecimal lEngineCyclesDeltaValue = new BigDecimal( 5 );
      final BigDecimal lEngineHoursDeltaValue = new BigDecimal( -20 );

      // Given an aircraft with usage parameters and current usage and an installed engine with
      // usage parameters and current usage.
      final InventoryKey lEngineKey = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.addUsage( DataTypeKey.CYCLES, lEngineCurrentCycles );
            aEngine.addUsage( DataTypeKey.HOURS, lEngineCurrentHours );
         }
      } );

      final InventoryKey lAircraftKey = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( DataTypeKey.CYCLES, lAircraftCurrentCycles );
            aAircraft.addUsage( DataTypeKey.HOURS, lAircraftCurrentHours );
            aAircraft.addEngine( lEngineKey );
         }
      } );

      // Ensure the current usage is expected prior to executing the test.
      assertCurrentUsage( lAircraftKey, lAircraftCurrentCycles, lAircraftCurrentHours );
      assertCurrentUsage( lEngineKey, lEngineCurrentCycles, lEngineCurrentHours );

      // When usage is accrued with usage deltas for the aircraft and the engine.
      List<UsageDelta> lUsageDeltas = generateUsageDeltas( lAircraftKey, lAircraftCyclesDeltaValue,
            lAircraftHoursDeltaValue );
      lUsageDeltas.addAll(
            generateUsageDeltas( lEngineKey, lEngineCyclesDeltaValue, lEngineHoursDeltaValue ) );

      iUsageAccrualService.accrueUsage( lAircraftKey, HR, USAGE_NAME, COMPLETION_DATE, DESCRIPTION,
            DOCUMENT_REFERENCE, lUsageDeltas, USAGE_TYPE, null );

      // Then the current usage of the aircraft and engine are adjusted by those delta values.
      BigDecimal lExpectedCycles = lAircraftCurrentCycles.add( lAircraftCyclesDeltaValue );
      BigDecimal lExpectedHours = lAircraftCurrentHours.add( lAircraftHoursDeltaValue );
      assertCurrentUsage( lAircraftKey, lExpectedCycles, lExpectedHours );

      lExpectedCycles = lEngineCurrentCycles.add( lEngineCyclesDeltaValue );
      lExpectedHours = lEngineCurrentHours.add( lEngineHoursDeltaValue );
      assertCurrentUsage( lEngineKey, lExpectedCycles, lExpectedHours );

   }


   /**
    * Verify that when usage is accrued for an inventory and there exists a corresponding,
    * previously negated usage record, that the deltas are applied to the inventory's current usage.
    *
    * Note: there are other expected behavious when an negated usage record already exists but this
    * test simply ensures that the accrued usage is still applied to the inventory's current usage.
    *
    * <pre>
    * Given an aircraft with usage parameters and current usage
    *   And a negated usage record against the aircraft
    *  When usage deltas are added with the same date as the negated usage record
    *  Then the current usage of the aircraft is adjusted by those delta values
    * </pre>
    *
    */
   @Test
   public void testCurrentUsageIsAdjustedByUsageDeltasForNegatedUsageRecord() throws Exception {

      final BigDecimal lCurrentCycles = new BigDecimal( 100 );
      final BigDecimal lCurrentHours = new BigDecimal( 1000 );

      final BigDecimal lUsageRecordCycles = new BigDecimal( 40 );
      final BigDecimal lUsageRecordHours = new BigDecimal( 400 );
      final BigDecimal lUsageRecordCyclesDelta = new BigDecimal( 20 );
      final BigDecimal lUsageRecordHoursDelta = new BigDecimal( 200 );

      final BigDecimal lCyclesDeltaValue = new BigDecimal( -30 );
      final BigDecimal lHoursDeltaValue = new BigDecimal( 300 );

      // Given an aircraft with usage parameters and current usage.
      final InventoryKey lAircraftKey = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( DataTypeKey.CYCLES, lCurrentCycles );
            aAircraft.addUsage( DataTypeKey.HOURS, lCurrentHours );
         }
      } );

      // Given a negated usage record against the aircraft.
      final Date lUsageDate = new Date();
      Domain.createUsageAdjustment( new DomainConfiguration<UsageAdjustment>() {

         @Override
         public void configure( UsageAdjustment aUsageAdjustment ) {
            aUsageAdjustment.setUsageDate( lUsageDate );
            aUsageAdjustment.setIsNegated( true );
            aUsageAdjustment.setMainInventory( lAircraftKey );
            aUsageAdjustment.addUsage( lAircraftKey, DataTypeKey.CYCLES, lUsageRecordCycles,
                  lUsageRecordCyclesDelta );
            aUsageAdjustment.addUsage( lAircraftKey, DataTypeKey.HOURS, lUsageRecordHours,
                  lUsageRecordHoursDelta );
         }
      } );

      // When usage deltas are added with the same date as the negated usage record.
      List<UsageDelta> lUsageDeltas =
            generateUsageDeltas( lAircraftKey, lCyclesDeltaValue, lHoursDeltaValue );

      iUsageAccrualService.accrueUsage( lAircraftKey, HR, USAGE_NAME, lUsageDate, DESCRIPTION,
            DOCUMENT_REFERENCE, lUsageDeltas, USAGE_TYPE, null );

      // Then the current usage of the aircraft is adjusted by those delta values.
      BigDecimal lExpectedCycles = lCurrentCycles.add( lCyclesDeltaValue );
      BigDecimal lExpectedHours = lCurrentHours.add( lHoursDeltaValue );
      assertCurrentUsage( lAircraftKey, lExpectedCycles, lExpectedHours );

   }


   /**
    * Given a calculated parameter with an assigned usage parameter that does not include a part
    * specific constant <br>
    *
    * And an aircraft tracking both the calculated parameter and the assigned usage parameter<br>
    *
    * When a historical usage record is created<br>
    *
    * Then the calculated parameter usage will be calculated correctly in inventory current
    * usage<br>
    *
    * @throws Exception
    */
   @Test
   public void itUpdatesCalculatedParamForInventoryCurrentUsageWhenHistoricalUsageRecordCreated()
         throws Exception {

      // Arrange
      // createCalculationInDatabase() method should be called prior to any data setup in database
      // as the method performs an explicit rollback followed by an implicit database commit
      createCalculationInDatabase();
      SoftDeadlineService lSoftDeadlineService = new MxSoftDeadlineService();
      UpdateDeadlineService lUpdateDeadlineService =
            new MxUpdateDeadlineService( lSoftDeadlineService );
      UsageAccrualService lUsageAccrualService =
            new MxUsageAccrualService( lUpdateDeadlineService );

      // Given an aircraft; based on an aircraft assembly, tracking a usage parameter, and tracking
      // the calculated usage parameter.
      final BigDecimal lCurrentHoursTsn = new BigDecimal( 100 );
      final AssemblyKey lAcftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aBuilder ) {
                  aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot aBuilder ) {
                        aBuilder.addUsageParameter( DataTypeKey.HOURS );
                        aBuilder.addCalculatedUsageParameter(
                              new DomainConfiguration<CalculatedUsageParameter>() {

                                 @Override
                                 public void configure( CalculatedUsageParameter aBuilder ) {
                                    aBuilder.setCode( CALC_USAGE_PARM_CODE );
                                    aBuilder.setDatabaseCalculation( HOURS_TO_MINUTES_FUNCTION );
                                    aBuilder.setPrecisionQt( 2 );

                                    // Add the constant and parameter in the order in which the
                                    // function is expecting them.
                                    aBuilder.addConstant( MINUTES_PER_HOUR_CONSTANT_NAME,
                                          MINUTES_PER_HOUR );
                                    aBuilder.addParameter( DataTypeKey.HOURS );
                                 }

                              } );
                     }
                  } );
               }
            } );

      ConfigSlotKey lRootCsKey = new ConfigSlotKey( lAcftAssembly, 0 );

      final DataTypeKey lCalcUsageParm = getCalcUsageParm( lRootCsKey, CALC_USAGE_PARM_CODE );

      InventoryKey lAircraftInvKey = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aBuilder ) {
            aBuilder.setAssembly( lAcftAssembly );
            aBuilder.addUsage( HOURS, lCurrentHoursTsn );
            aBuilder.addUsage( lCalcUsageParm, BigDecimal.ZERO );
         }
      } );

      // Act
      // When a historical usage record is created with usage deltas for the aircraft
      BigDecimal lHoursDelta = new BigDecimal( 2 );
      List<UsageDelta> lUsageDeltas = generateUsageDeltas( lAircraftInvKey, lHoursDelta );
      Date lUsageDate = new Date();
      lUsageAccrualService.accrueUsage( lAircraftInvKey, HR, USAGE_NAME, lUsageDate,
            "Usage Description", null, lUsageDeltas, UsageType.MXACCRUAL, null );

      // Assert
      // Then the calculated usage parameter of the aircraft is calculated correctly.
      final BigDecimal lActualCalcUsageTsn =
            new BigDecimal( InvCurrUsage.findTSNQtByInventory( lAircraftInvKey, lCalcUsageParm ) );
      final BigDecimal lExpectedCalcUsageTsn =
            lCurrentHoursTsn.add( lHoursDelta ).multiply( MINUTES_PER_HOUR );

      // dropCalculationInDatabase() method should be called after any data setup in database
      // as the method performs an explicit rollback followed by an implicit database commit
      dropCalculationInDatabase();
      Assert.assertEquals( "Unexpected result from the calculated usage parameter's db funtion.",
            lExpectedCalcUsageTsn, lActualCalcUsageTsn );

   }


   /**
    * It is possible to have a flight with a usage record whose usage deltas have not be previously
    * applied.
    *
    * Verify that when applying usage for a flight whose usage deltas where not previously applied,
    * that those usage deltas are applied to the inventory's current usage.
    *
    * <pre>
    * Given an aircraft with usage parameters and current usage
    *   And a flight leg against the aircraft with unapplied usage delta values
    *  When the flight leg's usage is applied
    *  Then the current usage of the aircraft is adjusted by those delta values
    * </pre>
    *
    */
   @Test
   public void testCurrentUsageIsAdjustedByUsageDeltasInFlightUsageRecord() throws Exception {

      final BigDecimal lCurrentCycles = new BigDecimal( 100 );
      final BigDecimal lCurrentHours = new BigDecimal( 1000 );

      final BigDecimal lUsageRecordCycles = new BigDecimal( 40 );
      final BigDecimal lUsageRecordHours = new BigDecimal( 400 );
      final BigDecimal lUsageRecordCyclesDelta = new BigDecimal( 20 );
      final BigDecimal lUsageRecordHoursDelta = new BigDecimal( 200 );

      // Given an aircraft with usage parameters and current usage.
      final InventoryKey lAircraftKey = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( DataTypeKey.CYCLES, lCurrentCycles );
            aAircraft.addUsage( DataTypeKey.HOURS, lCurrentHours );
         }
      } );

      // Given a flight leg against the aircraft with unapplied usage delta values.
      final UsageAdjustmentId lUsageRecordKey =
            Domain.createUsageAdjustment( new DomainConfiguration<UsageAdjustment>() {

               @Override
               public void configure( UsageAdjustment aUsageAdjustment ) {
                  aUsageAdjustment.setHasBeenApplied( false );
                  aUsageAdjustment.setMainInventory( lAircraftKey );
                  aUsageAdjustment.addUsage( lAircraftKey, DataTypeKey.CYCLES, lUsageRecordCycles,
                        lUsageRecordCyclesDelta );
                  aUsageAdjustment.addUsage( lAircraftKey, DataTypeKey.HOURS, lUsageRecordHours,
                        lUsageRecordHoursDelta );
               }
            } );

      FlightLegId aFlightId = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aFlight ) {
            aFlight.setAircraft( lAircraftKey );
            aFlight.setUsageRecord( lUsageRecordKey );
         }
      } );

      // When the flight leg's usage is applied.
      iUsageAccrualService.applyUsageDelta( new ActualDatesCalculator(), aFlightId, false );

      // Then the current usage of the aircraft is adjusted by those delta values.
      BigDecimal lExpectedCycles = lCurrentCycles.add( lUsageRecordCyclesDelta );
      BigDecimal lExpectedHours = lCurrentHours.add( lUsageRecordHoursDelta );
      assertCurrentUsage( lAircraftKey, lExpectedCycles, lExpectedHours );

   }


   private List<UsageDelta> generateUsageDeltas( final InventoryKey lAircraftKey,
         final BigDecimal lCyclesDeltaValue, final BigDecimal lHoursDeltaValue ) {
      List<UsageDelta> lUsageDeltas = new ArrayList<UsageDelta>();

      UsageDelta lCyclesDelta = new UsageDelta();
      lCyclesDelta.setUsageParmKey( new UsageParmKey( lAircraftKey, DataTypeKey.CYCLES ) );
      lCyclesDelta.setDelta( lCyclesDeltaValue.doubleValue() );
      lUsageDeltas.add( lCyclesDelta );

      UsageDelta lHoursDelta = new UsageDelta();
      lHoursDelta.setUsageParmKey( new UsageParmKey( lAircraftKey, DataTypeKey.HOURS ) );
      lHoursDelta.setDelta( lHoursDeltaValue.doubleValue() );
      lUsageDeltas.add( lHoursDelta );

      return lUsageDeltas;
   }


   private void assertCurrentUsage( InventoryKey aAircraftKey, BigDecimal aExpectedCycles,
         BigDecimal aExpectedHours ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aAircraftKey, "inv_no_db_id", "inv_no_id" );

      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "inv_curr_usage", lArgs );

      assertEquals( 2, lQs.getRowCount() );

      while ( lQs.next() ) {

         DataTypeKey lDataType = lQs.getKey( DataTypeKey.class, "data_type_db_id", "data_type_id" );

         if ( DataTypeKey.CYCLES.equals( lDataType ) ) {
            assertEquals( aExpectedCycles, lQs.getBigDecimal( "tsn_qt" ) );
            assertEquals( aExpectedCycles, lQs.getBigDecimal( "tso_qt" ) );
            assertEquals( aExpectedCycles, lQs.getBigDecimal( "tsi_qt" ) );

         } else if ( DataTypeKey.HOURS.equals( lDataType ) ) {
            assertEquals( aExpectedHours, lQs.getBigDecimal( "tsn_qt" ) );
            assertEquals( aExpectedHours, lQs.getBigDecimal( "tso_qt" ) );
            assertEquals( aExpectedHours, lQs.getBigDecimal( "tsi_qt" ) );

         } else {
            fail( "Unexpected data type in inv_curr_usage: " + lDataType );
         }
      }
   }


   private void createCalculationInDatabase() throws SQLException {
      // Function creation is DDL which implicitly commits transaction
      // We perform explicit rollback before function creation ensuring no data gets committed
      // accidentally
      String lCreateFunctionStatement = "CREATE OR REPLACE FUNCTION " + HOURS_TO_MINUTES_FUNCTION
            + " (" + "aConstant NUMBER, aHoursInput NUMBER" + " )" + " RETURN FLOAT" + " " + "IS "
            + "result FLOAT; " + "BEGIN" + " " + "result := aConstant * aHoursInput ; " + "RETURN"
            + " " + " result;" + "END" + " " + HOURS_TO_MINUTES_FUNCTION + " ;";

      Domain.createCalculatedParameterEquationFunction( iDatabaseConnectionRule.getConnection(),
            lCreateFunctionStatement );
   }


   /**
    * This method returns the calculated parm data type key based on the provided calculcated param
    * code and config slot key
    *
    * @param aRootCsKey
    * @param aCalcUsageParmCode
    * @return DataTypeKey for the calculated parm
    */
   private DataTypeKey getCalcUsageParm( ConfigSlotKey aRootCsKey, String aCalcUsageParmCode ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "data_type_cd", aCalcUsageParmCode );
      QuerySet lDataTypeQs =
            QuerySetFactory.getInstance().executeQueryTable( "mim_data_type", lArgs );

      while ( lDataTypeQs.next() ) {
         DataTypeKey lDataTypeKey =
               lDataTypeQs.getKey( DataTypeKey.class, "data_type_db_id", "data_type_id" );

         lArgs = aRootCsKey.getPKWhereArg();
         lArgs.add( lDataTypeKey, "data_type_db_id", "data_type_id" );
         QuerySet lQs =
               QuerySetFactory.getInstance().executeQueryTable( "mim_part_numdata", lArgs );
         if ( !lQs.isEmpty() ) {
            return lDataTypeKey;
         }
      }
      return null;
   }


   private List<UsageDelta> generateUsageDeltas( final InventoryKey lAircraftKey,
         final BigDecimal lHoursDeltaValue ) {
      List<UsageDelta> lUsageDeltas = new ArrayList<UsageDelta>();
      UsageDelta lHoursDelta = new UsageDelta();
      lHoursDelta.setUsageParmKey( new UsageParmKey( lAircraftKey, DataTypeKey.HOURS ) );
      lHoursDelta.setDelta( lHoursDeltaValue.doubleValue() );
      lUsageDeltas.add( lHoursDelta );
      return lUsageDeltas;
   }


   private void dropCalculationInDatabase() throws SQLException {
      // Function creation is DDL which implicitly commits transaction
      // We perform explicit rollback before function creation ensuring no data gets committed
      // accidentally
      Domain.dropCalculatedParameterEquationFunction( iDatabaseConnectionRule.getConnection(),
            HOURS_TO_MINUTES_FUNCTION );
   }


   @Before
   public void setup() {

      SoftDeadlineService lSoftDeadlineService = new MxSoftDeadlineService();
      UpdateDeadlineService lUpdateDeadlineService =
            new MxUpdateDeadlineService( lSoftDeadlineService );
      iUsageAccrualService = new MxUsageAccrualService( lUpdateDeadlineService );

   }

}
