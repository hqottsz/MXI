package com.mxi.mx.core.services.event.inventory;

import static com.mxi.mx.core.key.DataTypeKey.CYCLES;
import static com.mxi.mx.core.key.DataTypeKey.HOURS;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Assert;
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
import com.mxi.am.domain.WorkPackage;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.EventInventoryUsageKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefDomainTypeKey;
import com.mxi.mx.core.key.RefEngUnitKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.table.evt.EvtInvTable;
import com.mxi.mx.core.table.evt.EvtInvUsage;
import com.mxi.mx.core.table.mim.MimDataType;


/**
 * Tests to exercise {@linkplain EventInventoryService}
 *
 */
public class EventInventoryServiceTest {

   // Calculated parameter equation, constant, and data types.
   // The equation is simply a multiplier of the target usage parameter by the multiplier constant.
   private static final String CALC_PARM_EQUATION_FUNCTION_NAME = "DOUBLE_HOURS";
   private static final String CALC_PARM_CONSTANT_NAME = "CALC_PARM_CONSTANT_NAME";
   private static final BigDecimal CALC_PARM_CONSTANT_VALUE = BigDecimal.valueOf( 2.0 );

   private static final String CALC_PARM_CODE_1 = "DOUBLE_HOURS_1";
   private static final String CALC_PARM_CODE_2 = "DOUBLE_HOURS_2";
   private static final String CALC_PARM_CODE_3 = "DOUBLE_HOURS_3";
   private static final String CALC_PARM_CODE_4 = "DOUBLE_HOURS_4";

   private static final BigDecimal USAGE_SNAPSHOT_CALC_1 = BigDecimal.valueOf( 3.0 );
   private static final BigDecimal USAGE_SNAPSHOT_CALC_2 = BigDecimal.valueOf( 4.0 );
   private static final BigDecimal USAGE_SNAPSHOT_CALC_3 = BigDecimal.valueOf( 5.0 );
   private static final BigDecimal USAGE_SNAPSHOT_CALC_4 = BigDecimal.valueOf( 6.0 );

   private static final BigDecimal USAGE_SNAPSHOT_HOURS = BigDecimal.valueOf( 7.0 );
   private static final BigDecimal USAGE_SNAPSHOT_CYCLES = BigDecimal.valueOf( 8.0 );
   private static final BigDecimal USAGE_SNAPSHOT_ENGINE_HOURS = BigDecimal.valueOf( 9.0 );
   private static final BigDecimal USAGE_SNAPSHOT_ENGINE_CYCLES = BigDecimal.valueOf( 10.0 );

   private static final String CREATE_FUNCTION_STATEMENT = "CREATE OR REPLACE FUNCTION "
         + CALC_PARM_EQUATION_FUNCTION_NAME + " (" + "aConstant NUMBER, aHoursInput NUMBER" + " )"
         + " RETURN NUMBER" + " " + "IS " + "result NUMBER; " + "BEGIN" + " "
         + "result := aConstant * aHoursInput ; " + "RETURN" + " " + " result;" + "END" + " "
         + CALC_PARM_EQUATION_FUNCTION_NAME + " ;";

   private DataTypeKey iEngineHours;
   private DataTypeKey iEngineCycles;

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Verify that {@linkplain EventInventoryService#recalculateUsageSnapshotCalcParms()}
    * recalculates a calculated parameter in an event's usage snapshot.
    *
    * <pre>
    *
    * Given an aircraft that is collecting a usage parameter and a calculated parameter based on the usage parameter
    *   And a work package (which is an event) with a usage snapshot against the aircraft
    *  When a request is made to recalculate the calculated parameters of the work package usage snapshot
    *  Then the calculated parameter in the snapshot is recalculated
    *
    * </pre>
    */
   @Test
   public void itRecalculatesCalcParmWhenAssociatedUsageParmModified() throws Exception {

      //
      // Given an aircraft that is collecting a usage parameter and a calculated parameter based on
      // the usage parameter
      //

      {
         // Create a function that can be used as a calculated parameter equation.
         //
         // IMPORTANT:
         // all data setup in the DB prior to calling this method will be explicitly
         // rolled-back! This is because the DDL executed within this method implicitly performs a
         // commit and we cannot commit that test data.
         Domain.createCalculatedParameterEquationFunction( iDatabaseConnectionRule.getConnection(),
               CREATE_FUNCTION_STATEMENT );
      }

      // Create an aircraft assembly whose root config slot is assigned a usage parameter and a
      // calculated parameter based on that usage parameter.
      Map<String, DataTypeKey> lConfigParms = new HashMap<String, DataTypeKey>();
      lConfigParms.put( CALC_PARM_CODE_1, HOURS );

      final AssemblyKey lAcftAssembly = createAircraftAssyWithCalcParms( lConfigParms );

      final DataTypeKey lCalcParmKey = readConfigParmKey( lAcftAssembly, CALC_PARM_CODE_1 );

      // Create an aircraft based on the aircraft assembly.
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aBuilder ) {
            aBuilder.setAssembly( lAcftAssembly );
         }
      } );

      //
      // Given a work package (which is an event) with a usage snapshot against the aircraft
      //
      final TaskKey lWorkPackage =
            Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

               @Override
               public void configure( WorkPackage aBuilder ) {
                  aBuilder.setAircraft( lAircraft );
                  aBuilder.addUsageSnapshot( lAircraft, HOURS, USAGE_SNAPSHOT_HOURS );
                  aBuilder.addUsageSnapshot( lAircraft, lCalcParmKey, USAGE_SNAPSHOT_CALC_1 );
               }
            } );

      //
      // When a request is made to recalculate the calculated parameters of the work package usage
      // snapshot.
      //
      new EventInventoryService( lWorkPackage.getEventKey() ).recalculateUsageSnapshotCalcParms();

      //
      // Then the calculated parameter in the snapshot is recalculated.
      //
      BigDecimal lExpected = USAGE_SNAPSHOT_HOURS.multiply( CALC_PARM_CONSTANT_VALUE );
      BigDecimal lAcutual = readDataTypeValue( lWorkPackage, lAircraft, lCalcParmKey );

      {
         // IMPORTANT:
         // All data needed for assertions must be retrieved prior to calling
         // dropCalculatedParameterEquationFunction(). As it performs an explicit roll-back.
         //
         // ALSO, all assertions must be done after dropCalculatedParameterEquationFunction() to
         // ensure the equation gets removed from the DB.
         Domain.dropCalculatedParameterEquationFunction( iDatabaseConnectionRule.getConnection(),
               CALC_PARM_EQUATION_FUNCTION_NAME );
      }

      Assert.assertTrue( "Unexpected value for calculate parameter " + CALC_PARM_CODE_1
            + "; expected=" + lExpected + " , actual=" + lAcutual,
            lExpected.compareTo( lAcutual ) == 0 );
   }


   /**
    * Verify that {@linkplain EventInventoryService#recalculateUsageSnapshotCalcParms()}
    * recalculates all calculated parameters against all inventory in an event's usage snapshot.
    *
    * <pre>
    *
    * Given an aircraft that is collecting many usage parameters and
    *       many calculated parameters based on the usage parameters
    *   And an engine installed on the aircraft that is also collecting many usage parameters and
    *       many calculated parameters based on the usage parameters
    *   And a work package (which is an event) with a usage snapshot against the aircraft
    *  When a request is made to recalculate the calculated parameters of the work package usage snapshot
    *  Then all the calculated parameters in the snapshot are recalculated
    *
    * </pre>
    */
   @Test
   public void itRecalculatesAllCalcParmsWhenAssociatedUsageParmsAreModified() throws Exception {

      //
      // Given an aircraft that is collecting many usage parameters and a calculated parameter based
      // on one of the usage parameters.
      //
      {
         // Create a function that can be used as a calculated parameter equation.
         //
         // IMPORTANT:
         // all data setup in the DB prior to calling this method will be explicitly
         // rolled-back! This is because the DDL executed within this method implicitly performs a
         // commit and we cannot commit that test data.
         Domain.createCalculatedParameterEquationFunction( iDatabaseConnectionRule.getConnection(),
               CREATE_FUNCTION_STATEMENT );
      }

      // Create an aircraft assembly whose root config slot is assigned many usage parameters and
      // many calculated parameters based on the usage parameters.
      Map<String, DataTypeKey> lAcftConfigParms = new HashMap<String, DataTypeKey>();
      lAcftConfigParms.put( CALC_PARM_CODE_1, HOURS );
      lAcftConfigParms.put( CALC_PARM_CODE_2, CYCLES );

      final AssemblyKey lAcftAssembly = createAircraftAssyWithCalcParms( lAcftConfigParms );

      final DataTypeKey lCalcParmKey1 = readConfigParmKey( lAcftAssembly, CALC_PARM_CODE_1 );
      final DataTypeKey lCalcParmKey2 = readConfigParmKey( lAcftAssembly, CALC_PARM_CODE_2 );

      // Create an aircraft based on the aircraft assembly.
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aBuilder ) {
            aBuilder.setAssembly( lAcftAssembly );
         }
      } );

      //
      // Given an engine installed on the aircraft that is also collecting many usage parameters and
      // many calculated parameters based on the usage parameters.
      //

      // Create two more usage parameters to test with.
      iEngineHours = MimDataType.create( "ENGINE_HOURS", "ENGINE_HOURS",
            RefDomainTypeKey.USAGE_PARM, RefEngUnitKey.HOURS, 2, null );
      iEngineCycles = MimDataType.create( "ENGINE_CYCLES", "ENGINE_CYCLES",
            RefDomainTypeKey.USAGE_PARM, RefEngUnitKey.CYCLES, 2, null );

      // Create an engine assembly whose root config slot is assigned many usage parameters and many
      // calculated parameters based on the usage parameters.
      Map<String, DataTypeKey> lEngineConfigParms = new HashMap<String, DataTypeKey>();
      lEngineConfigParms.put( CALC_PARM_CODE_3, iEngineHours );
      lEngineConfigParms.put( CALC_PARM_CODE_4, iEngineCycles );

      final AssemblyKey lEngineAssembly = createEngineAssyWithCalcParms( lEngineConfigParms );

      final DataTypeKey lCalcParmKey3 = readConfigParmKey( lEngineAssembly, CALC_PARM_CODE_3 );
      final DataTypeKey lCalcParmKey4 = readConfigParmKey( lEngineAssembly, CALC_PARM_CODE_4 );

      // Create an engine based on the engine assembly and installed on the aircraft.
      final InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aBuilder ) {
            aBuilder.setAssembly( lEngineAssembly );
            aBuilder.setParent( lAircraft );
         }
      } );

      //
      // Given a work package (which is an event) with a usage snapshot against the aircraft.
      //
      final TaskKey lWorkPackage =
            Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

               @Override
               public void configure( WorkPackage aBuilder ) {
                  aBuilder.setAircraft( lAircraft );

                  // Aircraft usage.
                  aBuilder.addUsageSnapshot( lAircraft, HOURS, USAGE_SNAPSHOT_HOURS );
                  aBuilder.addUsageSnapshot( lAircraft, CYCLES, USAGE_SNAPSHOT_CYCLES );
                  aBuilder.addUsageSnapshot( lAircraft, lCalcParmKey1, USAGE_SNAPSHOT_CALC_1 );
                  aBuilder.addUsageSnapshot( lAircraft, lCalcParmKey2, USAGE_SNAPSHOT_CALC_2 );

                  // Engine usage.
                  aBuilder.addUsageSnapshot( lEngine, iEngineHours, USAGE_SNAPSHOT_ENGINE_HOURS );
                  aBuilder.addUsageSnapshot( lEngine, iEngineCycles, USAGE_SNAPSHOT_ENGINE_CYCLES );
                  aBuilder.addUsageSnapshot( lEngine, lCalcParmKey3, USAGE_SNAPSHOT_CALC_3 );
                  aBuilder.addUsageSnapshot( lEngine, lCalcParmKey4, USAGE_SNAPSHOT_CALC_4 );
               }
            } );

      //
      // When a request is made to recalculate the calculated parameters of the work package usage
      // snapshot.
      //
      new EventInventoryService( lWorkPackage.getEventKey() ).recalculateUsageSnapshotCalcParms();

      //
      // Then all the calculated parameters in the snapshot are recalculated.
      //
      BigDecimal lExpected1 = USAGE_SNAPSHOT_HOURS.multiply( CALC_PARM_CONSTANT_VALUE );
      BigDecimal lAcutual1 = readDataTypeValue( lWorkPackage, lAircraft, lCalcParmKey1 );

      BigDecimal lExpected2 = USAGE_SNAPSHOT_CYCLES.multiply( CALC_PARM_CONSTANT_VALUE );
      BigDecimal lAcutual2 = readDataTypeValue( lWorkPackage, lAircraft, lCalcParmKey2 );

      BigDecimal lExpected3 = USAGE_SNAPSHOT_ENGINE_HOURS.multiply( CALC_PARM_CONSTANT_VALUE );
      BigDecimal lAcutual3 = readDataTypeValue( lWorkPackage, lEngine, lCalcParmKey3 );

      BigDecimal lExpected4 = USAGE_SNAPSHOT_ENGINE_CYCLES.multiply( CALC_PARM_CONSTANT_VALUE );
      BigDecimal lAcutual4 = readDataTypeValue( lWorkPackage, lEngine, lCalcParmKey4 );

      {
         // IMPORTANT:
         // All data needed for assertions must be retrieved prior to calling
         // dropCalculatedParameterEquationFunction(). As it performs an explicit roll-back.
         //
         // ALSO, all assertions must be done after dropCalculatedParameterEquationFunction() to
         // ensure the equation gets removed from the DB.
         Domain.dropCalculatedParameterEquationFunction( iDatabaseConnectionRule.getConnection(),
               CALC_PARM_EQUATION_FUNCTION_NAME );
      }

      Assert.assertTrue(
            "Unexpected value for first aircraft calculate parameter " + CALC_PARM_CODE_1
                  + "; expected=" + lExpected1 + " , actual=" + lAcutual1,
            lExpected1.compareTo( lAcutual1 ) == 0 );

      Assert.assertTrue(
            "Unexpected value for second aircraft calculate parameter " + CALC_PARM_CODE_2
                  + "; expected=" + lExpected2 + " , actual=" + lAcutual2,
            lExpected2.compareTo( lAcutual2 ) == 0 );

      Assert.assertTrue(
            "Unexpected value for first engine calculate parameter " + CALC_PARM_CODE_3
                  + "; expected=" + lExpected3 + " , actual=" + lAcutual3,
            lExpected3.compareTo( lAcutual3 ) == 0 );

      Assert.assertTrue(
            "Unexpected value for second engine calculate parameter " + CALC_PARM_CODE_4
                  + "; expected=" + lExpected4 + " , actual=" + lAcutual4,
            lExpected4.compareTo( lAcutual4 ) == 0 );
   }


   /**
    *
    * Create an aircraft assembly with calculated parameters added to its root config slot.
    *
    * @param aCalcParmMap
    *           map of calc parm code to usage parm DataTypeKey <br/>
    *           (e.g. [<calc parm code1>:<usage parm key1>,<calc parm code2>:<usage parm key2>,...]
    *
    * @return key of the created aircraft assembly
    */
   private AssemblyKey
         createAircraftAssyWithCalcParms( final Map<String, DataTypeKey> aCalcParmMap ) {

      return Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

         @Override
         public void configure( AircraftAssembly aBuilder ) {
            aBuilder.setRootConfigurationSlot( buildRootConfigSlotWithCalcParms( aCalcParmMap ) );
         }
      } );

   }


   /**
    *
    * Create an engine assembly with calculated parameters added to its root config slot.
    *
    * @param aCalcParmMap
    *           map of calc parm code to usage parm DataTypeKey <br/>
    *           (e.g. [<calc parm code1>:<usage parm key1>,<calc parm code2>:<usage parm key2>,...]
    *
    * @return
    */
   private AssemblyKey
         createEngineAssyWithCalcParms( final Map<String, DataTypeKey> aCalcParmMap ) {

      return Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

         @Override
         public void configure( EngineAssembly aBuilder ) {
            aBuilder.setRootConfigurationSlot( buildRootConfigSlotWithCalcParms( aCalcParmMap ) );
         }
      } );

   }


   /**
    *
    * Builds a domain configuration for a root config slot with calculated parameters and their
    * associated usage parameters .
    *
    * @param aCalcParmMap
    *           map of calc parm code to usage parm DataTypeKey <br/>
    *           (e.g. [<calc parm code1>:<usage parm key1>,<calc parm code2>:<usage parm key2>,...]
    *
    * @return domain configuration of the root config slot
    */

   private DomainConfiguration<ConfigurationSlot>
         buildRootConfigSlotWithCalcParms( final Map<String, DataTypeKey> aCalcParmMap ) {

      final Set<Entry<String, DataTypeKey>> lCalcParmMapEntries = aCalcParmMap.entrySet();

      return new DomainConfiguration<ConfigurationSlot>() {

         @Override
         public void configure( ConfigurationSlot aBuilder ) {

            for ( Entry<String, DataTypeKey> lCalcParmMapEntry : lCalcParmMapEntries ) {

               final String lCalcParmCode = lCalcParmMapEntry.getKey();
               final DataTypeKey lUsageParmKey = lCalcParmMapEntry.getValue();

               // Add the usage parameter.
               aBuilder.addUsageParameter( lUsageParmKey );

               // Add the calculated parameter referencing the usage parameter it is based on.
               aBuilder.addCalculatedUsageParameter(
                     new DomainConfiguration<CalculatedUsageParameter>() {

                        @Override
                        public void configure( CalculatedUsageParameter aBuilder ) {

                           aBuilder.setCode( lCalcParmCode );
                           aBuilder.addParameter( lUsageParmKey );

                           aBuilder.setDatabaseCalculation( CALC_PARM_EQUATION_FUNCTION_NAME );
                           aBuilder.setPrecisionQt( 2 );
                           aBuilder.addConstant( CALC_PARM_CONSTANT_NAME,
                                 CALC_PARM_CONSTANT_VALUE );
                        }

                     } );
            }

         }
      };
   }


   private DataTypeKey readConfigParmKey( AssemblyKey aAssembly, String aCalcParmCode ) {
      return Domain.readUsageParameter( Domain.readRootConfigurationSlot( aAssembly ),
            aCalcParmCode );
   }


   private BigDecimal readDataTypeValue( TaskKey aTask, InventoryKey aInv, DataTypeKey aDataType ) {

      return BigDecimal.valueOf( EvtInvUsage.findByPrimaryKey( new EventInventoryUsageKey(
            EvtInvTable.findByEventAndInventory( aTask.getEventKey(), aInv ).getPk(), aDataType ) )
            .getTsnQt() );
   }

}
