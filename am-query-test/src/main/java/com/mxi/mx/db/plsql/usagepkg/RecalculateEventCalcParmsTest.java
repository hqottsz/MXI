package com.mxi.mx.db.plsql.usagepkg;

import static org.junit.Assert.assertEquals;

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
import com.mxi.am.domain.Requirement;
import com.mxi.am.domain.TrackedInventory;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.EventInventoryKey;
import com.mxi.mx.core.key.EventInventoryUsageKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.services.event.inventory.UsageSnapshot;
import com.mxi.mx.core.table.eqp.EqpAssmblBom;
import com.mxi.mx.core.table.eqp.EqpAssmblPos;
import com.mxi.mx.core.table.evt.EvtInvTable;
import com.mxi.mx.core.table.evt.EvtInvUsageTable;
import com.mxi.mx.core.unittest.ProcedureStatementFactory;


/**
 * Tests for the USAGE_PKG.recalculateEventCalcParms PLSQL procedure.
 *
 */
public class RecalculateEventCalcParmsTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static final String ACFT_ASSY_CODE_1 = "A_ASSY_1";
   private static final String ACFT_ASSY_CODE_2 = "A_ASSY_2";
   private static final String ENG_ASSY_CODE_1 = "E_ASSY_1";
   private static final String ENG_ASSY_CODE_2 = "E_ASSY_2";
   private static final String ENGINE_SUBASSY_CONFIG_SLOT_CODE = "SUBASY_1";
   private static final String TRKCONFIGSLOT = "TRK_CONFIG_SLOT";
   private static final String CALC_PARM_CODE = "CALC_PARM_CODE";
   private static final String CALC_PARM_FUNCTION_NAME_1 = "CALC_PARM_FUNCTION_1";
   private static final String CALC_PARM_FUNCTION_NAME_2 = "CALC_PARM_FUNCTION_2";
   private static final double CALC_PARM_FUNCTION_1_RESULT = 10;
   private static final double CALC_PARM_FUNCTION_2_RESULT = 20;


   /**
    *
    * Verify that the correct function is used to recalculate a particular calculated parameter when
    * there are multiple calculated parameters using the same data type but for multiple assemblies
    * and each of those calculated parameters have a different function.
    *
    * Remember a calculated parameter is really an association between a data-type and an assembly.
    * So here we have the same data-type associated with many assemblies.
    *
    * Note: this scenario can only be created in previous versions of the code. As of 8.2-SP5 it is
    * not possible to have duplicated calc parm codes.
    *
    * <pre>
    *    Given a calculated parameter against an aircraft assembly using a function
    *      And another calculated parameter with the same data type against a difference aircraft assembly and using a different function
    *      And an aircraft based on the first assembly
    *      And a usage snapshot for an event against an aircraft
    *      And that usage snapshot contains the calculated parameter
    *      And the snaps shot's calculated parameter value is incorrect (differs from that returned by the function)
    *     When recalculateEventCalcParms is called and passed the key for the event
    *     Then the snaps shot's calculated parameter value is re-calculated using the function associated with the aircraft's assembly
    * </pre>
    *
    */
   @Test
   public void itUsesTheCorrectFunctionWhenMoreThanOneCalcParmWithSameDataTypeForAircraft()
         throws Exception {

      {
         // Set up DB functions for the calculated parameters to use.
         // The creation of these functions is DDL which implicitly commits the transaction.
         // To ensure no data gets accidentally committed we will perform a roll-back.
         createCalcParmFunction( CALC_PARM_FUNCTION_NAME_1, CALC_PARM_FUNCTION_1_RESULT );
         createCalcParmFunction( CALC_PARM_FUNCTION_NAME_2, CALC_PARM_FUNCTION_2_RESULT );
      }

      // Given a calculated parameter against an aircraft assembly using a function.
      // (i.e. create an aircraft assembly with a calc parm assigned to its root config slot)
      final AssemblyKey lAcftAssy1 = createAcftAssyWithCalcParm( ACFT_ASSY_CODE_1, CALC_PARM_CODE,
            CALC_PARM_FUNCTION_NAME_1 );

      final DataTypeKey lCalcParm = Domain
            .readUsageParameter( Domain.readRootConfigurationSlot( lAcftAssy1 ), CALC_PARM_CODE );

      // Given And another calculated parameter with the same data type against a difference
      // aircraft assembly and using a different function
      createAcftAssyWithCalcParm( ACFT_ASSY_CODE_2, lCalcParm, CALC_PARM_CODE,
            CALC_PARM_FUNCTION_NAME_2 );

      // Given an aircraft based on the first assembly
      final InventoryKey lAcft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aBuilder ) {
            aBuilder.setAssembly( lAcftAssy1 );
         }
      } );

      // Given a usage snapshot for an event against an aircraft
      // And that usage snapshot contains the calculated parameter
      // And the snaps shot's calculated parameter value is incorrect (differs from that returned by
      // the function).
      //
      // The function is expected to return CALC_PARM_FUNCTION_1_RESULT, so we will use something
      // different.
      //
      // Note: in tonight's performance, the role of the event will be played by a requirement.
      final double lOrigCalcParmValue = CALC_PARM_FUNCTION_1_RESULT - 1;
      TaskKey lReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aBuilder ) {
            aBuilder.setInventory( lAcft );
            aBuilder.addUsage( new UsageSnapshot( lAcft, lCalcParm, lOrigCalcParmValue ) );
         }
      } );

      // When recalculateEventCalcParms is called and passed the key for the event
      EventInventoryKey lEvtInvKey = EvtInvTable.findByEventAndInventory( lReq, lAcft ).getPk();
      executeRecalculateEventCalcParms( lEvtInvKey );

      // Then the snaps shot's calculated parameter value is re-calculated using the function
      // associated with the aircraft's assembly
      Double lActualCalcParmValue = EvtInvUsageTable
            .findByPrimaryKey( new EventInventoryUsageKey( lEvtInvKey, lCalcParm ) ).getTsnQt();
      Double lExpectedCalcParmValue = CALC_PARM_FUNCTION_1_RESULT;

      {
         // Since the creation of the DB functions for the calculated parameters were committed to
         // the DB, we need to drop them.
         // Again dropping the function is DDL which means it implicitly commits the transaction.
         // To ensure no data gets accidentally committed we will perform a roll-back.
         //
         // Note: since asserts end processing, it is important to drop the functions prior.
         dropCalcParmFunction( CALC_PARM_FUNCTION_NAME_1 );
         dropCalcParmFunction( CALC_PARM_FUNCTION_NAME_2 );

      }

      assertEquals(
            "Unexpected calculated parameter function called, as evident by its return value.",
            lExpectedCalcParmValue, lActualCalcParmValue );
   }


   /**
    *
    * Verify that the correct function is used to recalculate a calculated parameter in an event
    * usage snapshot for an event against an installed engine when both the aircraft and engine have
    * calculated parameters using the same data type but different functions.
    *
    * In this case the correct function is the one associated to the calculated parameter of the
    * engine assembly.
    *
    * Remember a calculated parameter is really an association between a data-type and an assembly.
    * So here we have the same data-type associated with both the aircraft assembly and the engine
    * assembly.
    *
    * Note: this scenario can only be created in previous versions of the code. As of 8.2-SP5 it is
    * not possible to have duplicated calc parm codes.
    *
    * <pre>
    *    Given a calculated parameter against an aircraft assembly using a function
    *      And another calculated parameter with the same data type, against an engine assembly, and using a different function
    *      And an aircraft based on the first assembly with an installed engine based on the engine assembly
    *      And a usage snapshot for an event against the engine
    *      And that usage snapshot contains the calculated parameter
    *      And the snaps shot's calculated parameter value is incorrect (differs from that returned by the function)
    *     When recalculateEventCalcParms is called and passed the key for the event
    *     Then the snaps shot's calculated parameter value is re-calculated using the function associated with the engine assembly
    * </pre>
    *
    */
   @Test
   public void itUsesTheCorrectFunctionWhenMoreThanOneCalcParmWithSameDataTypeForInstalledEngine()
         throws Exception {

      {
         // Set up DB functions for the calculated parameters to use.
         // The creation of these functions is DDL which implicitly commits the transaction.
         // To ensure no data gets accidentally committed we will perform a roll-back.
         createCalcParmFunction( CALC_PARM_FUNCTION_NAME_1, CALC_PARM_FUNCTION_1_RESULT );
         createCalcParmFunction( CALC_PARM_FUNCTION_NAME_2, CALC_PARM_FUNCTION_2_RESULT );
      }

      // Given a calculated parameter against an aircraft assembly using a function.
      // (i.e. create an aircraft assembly with a calc parm assigned to its root config slot)
      final AssemblyKey lAcftAssy =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aBuilder ) {
                  aBuilder.setCode( ACFT_ASSY_CODE_1 );
                  aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot aBuilder ) {
                        aBuilder.addCalculatedUsageParameter(
                              new DomainConfiguration<CalculatedUsageParameter>() {

                                 @Override
                                 public void configure( CalculatedUsageParameter aBuilder ) {
                                    aBuilder.setCode( CALC_PARM_CODE );
                                    aBuilder.setDatabaseCalculation( CALC_PARM_FUNCTION_NAME_1 );

                                    // Although not part of this test, the precision is mandatory
                                    // for re-calculation of calculated parameters.
                                    aBuilder.setPrecisionQt( 0 );
                                 }
                              } );
                        aBuilder
                              .addConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                                 @Override
                                 public void configure( ConfigurationSlot aBuilder ) {
                                    aBuilder.setCode( ENGINE_SUBASSY_CONFIG_SLOT_CODE );
                                 }
                              } );
                     }
                  } );
               }
            } );

      // Get the calculated parameter's data type.
      final DataTypeKey lCalcParm = Domain
            .readUsageParameter( Domain.readRootConfigurationSlot( lAcftAssy ), CALC_PARM_CODE );

      // Get the first position of the aircraft's engine sub-assembly config slot.
      final ConfigSlotPositionKey lEnginePosition =
            getEnginePosition( ACFT_ASSY_CODE_1, ENGINE_SUBASSY_CONFIG_SLOT_CODE );

      // Given another calculated parameter with the same data type, against an engine assembly, and
      // using a different function.
      final AssemblyKey lEngAssy =
            Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

               @Override
               public void configure( EngineAssembly aBuilder ) {
                  aBuilder.setCode( ENG_ASSY_CODE_1 );
                  aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot aBuilder ) {
                        aBuilder.addCalculatedUsageParameter(
                              new DomainConfiguration<CalculatedUsageParameter>() {

                                 @Override
                                 public void configure( CalculatedUsageParameter aBuilder ) {
                                    aBuilder.setDataType( lCalcParm );
                                    aBuilder.setCode( CALC_PARM_CODE );
                                    aBuilder.setDatabaseCalculation( CALC_PARM_FUNCTION_NAME_2 );
                                 }
                              } );
                     }
                  } );
               }
            } );

      // Given an aircraft based on the first assembly with an installed engine based on the engine
      // assembly.
      final InventoryKey lAcft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aBuilder ) {
            aBuilder.setAssembly( lAcftAssy );
         }
      } );

      final InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aBuilder ) {
            aBuilder.setAssembly( lEngAssy );
            aBuilder.setParent( lAcft );
            aBuilder.setPosition( lEnginePosition );
         }
      } );

      // Given a usage snapshot for an event against the engine
      // And that usage snapshot contains the calculated parameter
      // And the snaps shot's calculated parameter value is incorrect (differs from that returned by
      // the function)
      final double lOrigCalcParmValue = CALC_PARM_FUNCTION_2_RESULT - 1;
      TaskKey lReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aBuilder ) {
            aBuilder.setInventory( lEngine );
            aBuilder.addUsage( new UsageSnapshot( lEngine, lCalcParm, lOrigCalcParmValue ) );
         }
      } );

      // When recalculateEventCalcParms is called and passed the key for the event
      EventInventoryKey lEvtInvKey = EvtInvTable.findByEventAndInventory( lReq, lEngine ).getPk();
      executeRecalculateEventCalcParms( lEvtInvKey );

      // Then the snaps shot's calculated parameter value is re-calculated using the function
      // associated with the engine assembly.
      Double lActualCalcParmValue = EvtInvUsageTable
            .findByPrimaryKey( new EventInventoryUsageKey( lEvtInvKey, lCalcParm ) ).getTsnQt();
      Double lExpectedCalcParmValue = CALC_PARM_FUNCTION_2_RESULT;

      {
         // Since the creation of the DB functions for the calculated parameters were committed to
         // the DB, we need to drop them.
         // Again dropping the function is DDL which means it implicitly commits the transaction.
         // To ensure no data gets accidentally committed we will perform a roll-back.
         //
         // Note: since asserts end processing, it is important to drop the functions prior.
         dropCalcParmFunction( CALC_PARM_FUNCTION_NAME_1 );
         dropCalcParmFunction( CALC_PARM_FUNCTION_NAME_2 );
      }

      assertEquals(
            "Unexpected calculated parameter function called, as evident by its return value.",
            lExpectedCalcParmValue, lActualCalcParmValue );
   }


   /**
    *
    * Verify that the correct function is used to recalculate a calculated parameter in an event
    * usage snapshot for an event against an aircraft with an installed engine when both the
    * aircraft and engine have calculated parameters using the same data type but different
    * functions.
    *
    * In this case the correct function is the one associated to the calculated parameter of the
    * aircraft. This is similar to
    * itUsesTheCorrectFunctionWhenMoreThanOneCalcParmWithSameDataTypeForInstalledEngine but verifies
    * the aircraft instead of the engine.
    *
    * Remember a calculated parameter is really an association between a data-type and an assembly.
    * So here we have the same data-type associated with both the aircraft assembly and the engine
    * assembly.
    *
    * Note: this scenario can only be created in previous versions of the code. As of 8.2-SP5 it is
    * not possible to have duplicated calc parm codes.
    *
    * <pre>
    *    Given a calculated parameter against an aircraft assembly using a function
    *      And another calculated parameter with the same data type, against an engine assembly, and using a different function
    *      And an aircraft based on the first assembly with an installed engine based on the engine assembly
    *      And a usage snapshot for an event against the aircraft
    *      And that usage snapshot contains the calculated parameter
    *      And the snaps shot's calculated parameter value is incorrect (differs from that returned by the function)
    *     When recalculateEventCalcParms is called and passed the key for the event
    *     Then the snaps shot's calculated parameter value is re-calculated using the function associated with the aircraft assembly
    * </pre>
    *
    */
   @Test
   public void
         itUsesTheCorrectFunctionWhenMoreThanOneCalcParmWithSameDataTypeForAircraftWithInstalledEngine()
               throws Exception {

      {
         // Set up DB functions for the calculated parameters to use.
         // The creation of these functions is DDL which implicitly commits the transaction.
         // To ensure no data gets accidentally committed we will perform a roll-back.
         createCalcParmFunction( CALC_PARM_FUNCTION_NAME_1, CALC_PARM_FUNCTION_1_RESULT );
         createCalcParmFunction( CALC_PARM_FUNCTION_NAME_2, CALC_PARM_FUNCTION_2_RESULT );
      }

      // Given a calculated parameter against an aircraft assembly using a function.
      // (i.e. create an aircraft assembly with a calc parm assigned to its root config slot)
      final AssemblyKey lAcftAssy = createAcftAssyWithCalcParm( ACFT_ASSY_CODE_1, CALC_PARM_CODE,
            CALC_PARM_FUNCTION_NAME_1 );

      final DataTypeKey lCalcParm = Domain
            .readUsageParameter( Domain.readRootConfigurationSlot( lAcftAssy ), CALC_PARM_CODE );

      // Given another calculated parameter with the same data type, against an engine assembly, and
      // using a different function.
      final AssemblyKey lEngAssy =
            Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

               @Override
               public void configure( EngineAssembly aBuilder ) {
                  aBuilder.setCode( ENG_ASSY_CODE_1 );
                  aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot aBuilder ) {
                        aBuilder.addCalculatedUsageParameter(
                              new DomainConfiguration<CalculatedUsageParameter>() {

                                 @Override
                                 public void configure( CalculatedUsageParameter aBuilder ) {
                                    aBuilder.setDataType( lCalcParm );
                                    aBuilder.setCode( CALC_PARM_CODE );
                                    aBuilder.setDatabaseCalculation( CALC_PARM_FUNCTION_NAME_2 );
                                 }
                              } );
                     }
                  } );
               }
            } );

      // Given an aircraft based on the first assembly with an installed engine based on the engine
      // assembly.
      final InventoryKey lAcft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aBuilder ) {
            aBuilder.setAssembly( lAcftAssy );
         }
      } );
      Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aBuilder ) {
            aBuilder.setAssembly( lEngAssy );
            aBuilder.setParent( lAcft );
         }
      } );

      // Given a usage snapshot for an event against the aircraft
      // And that usage snapshot contains the calculated parameter
      // And the snaps shot's calculated parameter value is incorrect (differs from that returned by
      // the function)
      final double lOrigCalcParmValue = CALC_PARM_FUNCTION_1_RESULT - 1;
      TaskKey lReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aBuilder ) {
            aBuilder.setInventory( lAcft );
            aBuilder.addUsage( new UsageSnapshot( lAcft, lCalcParm, lOrigCalcParmValue ) );
         }
      } );

      // When recalculateEventCalcParms is called and passed the key for the event
      EventInventoryKey lEvtInvKey = EvtInvTable.findByEventAndInventory( lReq, lAcft ).getPk();
      executeRecalculateEventCalcParms( lEvtInvKey );

      // Then the snaps shot's calculated parameter value is re-calculated using the function
      // associated with the aircraft assembly.
      Double lActualCalcParmValue = EvtInvUsageTable
            .findByPrimaryKey( new EventInventoryUsageKey( lEvtInvKey, lCalcParm ) ).getTsnQt();
      Double lExpectedCalcParmValue = CALC_PARM_FUNCTION_1_RESULT;

      {
         // Since the creation of the DB functions for the calculated parameters were committed to
         // the DB, we need to drop them.
         // Again dropping the function is DDL which means it implicitly commits the transaction.
         // To ensure no data gets accidentally committed we will perform a roll-back.
         //
         // Note: since asserts end processing, it is important to drop the functions prior.
         dropCalcParmFunction( CALC_PARM_FUNCTION_NAME_1 );
         dropCalcParmFunction( CALC_PARM_FUNCTION_NAME_2 );
      }

      assertEquals(
            "Unexpected calculated parameter function called, as evident by its return value.",
            lExpectedCalcParmValue, lActualCalcParmValue );
   }


   /**
    *
    * Verify that the correct function is used to recalculate a calculated parameter in an event
    * usage snapshot for an event against a loose engine when multiple engine assemblies have
    * calculated parameters using the same data type but different functions.
    *
    * In this case the correct function is the one associated to the calculated parameter of the
    * engine's assembly.
    *
    * Remember a calculated parameter is really an association between a data-type and an assembly.
    * So here we have the same data-type associated with many engine assemblies.
    *
    * Note: this scenario can only be created in previous versions of the code. As of 8.2-SP5 it is
    * not possible to have duplicated calc parm codes.
    *
    * <pre>
    *    Given a calculated parameter against an engine assembly using a function
    *      And another calculated parameter with the same data type, against a different engine assembly and using a different function
    *      And a loose (uninstalled) engine based on the first assembly
    *      And a usage snapshot for an event against the engine
    *      And that usage snapshot contains the calculated parameter
    *      And the snaps shot's calculated parameter value is incorrect (differs from that returned by the function)
    *     When recalculateEventCalcParms is called and passed the key for the event
    *     Then the snaps shot's calculated parameter value is re-calculated using the function associated with the engine's assembly
    * </pre>
    *
    */
   @Test
   public void itUsesTheCorrectFunctionWhenMoreThanOneCalcParmWithSameDataTypeForLooseEngine()
         throws Exception {

      {
         // Set up DB functions for the calculated parameters to use.
         // The creation of these functions is DDL which implicitly commits the transaction.
         // To ensure no data gets accidentally committed we will perform a roll-back.
         createCalcParmFunction( CALC_PARM_FUNCTION_NAME_1, CALC_PARM_FUNCTION_1_RESULT );
         createCalcParmFunction( CALC_PARM_FUNCTION_NAME_2, CALC_PARM_FUNCTION_2_RESULT );
      }

      // Given a calculated parameter against an engine assembly using a function.
      // (i.e. create an engine assembly with a calc parm assigned to its root config slot)
      final AssemblyKey lEngAssy1 =
            Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

               @Override
               public void configure( EngineAssembly aBuilder ) {
                  aBuilder.setCode( ENG_ASSY_CODE_1 );
                  aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot aBuilder ) {
                        aBuilder.addCalculatedUsageParameter(
                              new DomainConfiguration<CalculatedUsageParameter>() {

                                 @Override
                                 public void configure( CalculatedUsageParameter aBuilder ) {
                                    aBuilder.setCode( CALC_PARM_CODE );
                                    aBuilder.setDatabaseCalculation( CALC_PARM_FUNCTION_NAME_1 );

                                    // Although not part of this test, the precision is mandatory
                                    // for re-calculation of calculated parameters.
                                    aBuilder.setPrecisionQt( 0 );
                                 }
                              } );
                     }
                  } );
               }
            } );
      final DataTypeKey lCalcParm = Domain
            .readUsageParameter( Domain.readRootConfigurationSlot( lEngAssy1 ), CALC_PARM_CODE );

      // Given another calculated parameter with the same data type against a different
      // engine assembly and using a different function
      Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

         @Override
         public void configure( EngineAssembly aBuilder ) {
            aBuilder.setCode( ENG_ASSY_CODE_2 );
            aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

               @Override
               public void configure( ConfigurationSlot aBuilder ) {
                  aBuilder.addCalculatedUsageParameter(
                        new DomainConfiguration<CalculatedUsageParameter>() {

                           @Override
                           public void configure( CalculatedUsageParameter aBuilder ) {
                              aBuilder.setDataType( lCalcParm );
                              aBuilder.setCode( CALC_PARM_CODE );
                              aBuilder.setDatabaseCalculation( CALC_PARM_FUNCTION_NAME_2 );
                           }
                        } );
               }
            } );
         }
      } );

      // Given an engine based on the first assembly
      final InventoryKey lEng = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aBuilder ) {
            aBuilder.setAssembly( lEngAssy1 );
         }
      } );

      // Given a usage snapshot for an event against an engine
      // And that usage snapshot contains the calculated parameter
      // And the snaps shot's calculated parameter value is incorrect (differs from that returned by
      // the function).
      //
      // The function is expected to return CALC_PARM_FUNCTION_1_RESULT, so we will use something
      // different.
      //
      // Note: In this case the role of the event will be played by a requirement.
      final double lOrigCalcParmValue = CALC_PARM_FUNCTION_1_RESULT - 1;
      TaskKey lReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aBuilder ) {
            aBuilder.setInventory( lEng );
            aBuilder.addUsage( new UsageSnapshot( lEng, lCalcParm, lOrigCalcParmValue ) );
         }
      } );

      // When recalculateEventCalcParms is called and passed the key for the event
      EventInventoryKey lEvtInvKey = EvtInvTable.findByEventAndInventory( lReq, lEng ).getPk();
      executeRecalculateEventCalcParms( lEvtInvKey );

      // Then the snaps shot's calculated parameter value is re-calculated using the function
      // associated with the engine's assembly
      Double lActualCalcParmValue = EvtInvUsageTable
            .findByPrimaryKey( new EventInventoryUsageKey( lEvtInvKey, lCalcParm ) ).getTsnQt();
      Double lExpectedCalcParmValue = CALC_PARM_FUNCTION_1_RESULT;

      {
         // Since the creation of the DB functions for the calculated parameters were committed to
         // the DB, we need to drop them.
         // Again dropping the function is DDL which means it implicitly commits the transaction.
         // To ensure no data gets accidentally committed we will perform a roll-back.
         //
         // Note: since asserts end processing, it is important to drop the functions prior.
         dropCalcParmFunction( CALC_PARM_FUNCTION_NAME_1 );
         dropCalcParmFunction( CALC_PARM_FUNCTION_NAME_2 );
      }

      assertEquals(
            "Unexpected calculated parameter function called, as evident by its return value.",
            lExpectedCalcParmValue, lActualCalcParmValue );

   }


   /**
    *
    * Verify that the correct function is used to recalculate a calculated parameter in an event
    * usage snapshot for an event against a tracked inventory on an installed engine when both the
    * aircraft, and engine have calculated parameters using the same data type but different
    * functions.
    *
    * In this case the correct function is the one associated to the calculated parameter of the
    * tracked inventory (which is inherited from the engine).
    *
    * Remember a calculated parameter is really an association between a data-type and an assembly.
    * So here we have the same data-type associated with both the aircraft assembly and the engine
    * assembly.
    *
    * Note: this scenario can only be created in previous versions of the code. As of 8.2-SP5 it is
    * not possible to have duplicated calc parm codes.
    *
    * <pre>
    *    Given a calculated parameter against an aircraft assembly using a function
    *      And another calculated parameter with the same data type, against an engine assembly, and using a different function
    *      And an aircraft based on the first assembly with an installed engine based on the engine assembly
    *      And a tracked inventory with the same calculated parameter with the same data type (inherited from the engine) attached to the engine
    *      And a usage snapshot for an event against the tracked inventory
    *      And that usage snapshot contains the calculated parameter
    *      And the snaps shot's calculated parameter value is incorrect (differs from that returned by the function)
    *     When recalculateEventCalcParms is called and passed the key for the event
    *     Then the snaps shot's calculated parameter value is re-calculated using the function associated with the engine assembly
    * </pre>
    *
    */
   @Test
   public void
         itUsesTheCorrectFunctionWhenMoreThanOneCalcParmWithSameDataTypeForTrackedInventoryOnInstalledEngine()
               throws Exception {

      {
         // Set up DB functions for the calculated parameters to use.
         // The creation of these functions is DDL which implicitly commits the transaction.
         // To ensure no data gets accidentally committed we will perform a roll-back.
         createCalcParmFunction( CALC_PARM_FUNCTION_NAME_1, CALC_PARM_FUNCTION_1_RESULT );
         createCalcParmFunction( CALC_PARM_FUNCTION_NAME_2, CALC_PARM_FUNCTION_2_RESULT );
      }

      // Given a calculated parameter against an aircraft assembly using a function.
      // (i.e. create an aircraft assembly with a calc parm assigned to its root config slot)
      final AssemblyKey lAcftAssy =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aBuilder ) {
                  aBuilder.setCode( ACFT_ASSY_CODE_1 );
                  aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot aBuilder ) {
                        aBuilder.addCalculatedUsageParameter(
                              new DomainConfiguration<CalculatedUsageParameter>() {

                                 @Override
                                 public void configure( CalculatedUsageParameter aBuilder ) {
                                    aBuilder.setCode( CALC_PARM_CODE );
                                    aBuilder.setDatabaseCalculation( CALC_PARM_FUNCTION_NAME_1 );

                                    // Although not part of this test, the precision is mandatory
                                    // for re-calculation of calculated parameters.
                                    aBuilder.setPrecisionQt( 0 );
                                 }
                              } );
                        aBuilder
                              .addConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                                 @Override
                                 public void configure( ConfigurationSlot aBuilder ) {
                                    aBuilder.setCode( ENGINE_SUBASSY_CONFIG_SLOT_CODE );
                                 }
                              } );
                     }
                  } );
               }
            } );

      // Get the calculated parameter's data type.
      final DataTypeKey lCalcParm = Domain
            .readUsageParameter( Domain.readRootConfigurationSlot( lAcftAssy ), CALC_PARM_CODE );

      // Get the first position of the aircraft's engine sub-assembly config slot.
      final ConfigSlotPositionKey lEnginePosition =
            getEnginePosition( ACFT_ASSY_CODE_1, ENGINE_SUBASSY_CONFIG_SLOT_CODE );

      // Given another calculated parameter with the same data type, against an engine assembly with
      // a config-slot for tracked inventory, and
      // using a different function.
      final AssemblyKey lEngAssy =
            Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

               @Override
               public void configure( EngineAssembly aBuilder ) {
                  aBuilder.setCode( ENG_ASSY_CODE_1 );
                  aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot aBuilder ) {
                        aBuilder.addCalculatedUsageParameter(
                              new DomainConfiguration<CalculatedUsageParameter>() {

                                 @Override
                                 public void configure( CalculatedUsageParameter aBuilder ) {
                                    aBuilder.setDataType( lCalcParm );
                                    aBuilder.setCode( CALC_PARM_CODE );
                                    aBuilder.setDatabaseCalculation( CALC_PARM_FUNCTION_NAME_2 );
                                 }
                              } );

                        // Open config slot where the tracked inventory will be placed
                        aBuilder
                              .addConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                                 @Override
                                 public void configure( ConfigurationSlot aBuilder ) {
                                    aBuilder.setCode( TRKCONFIGSLOT );
                                    aBuilder.setConfigurationSlotClass( RefBOMClassKey.TRK );
                                 }
                              } );
                     }
                  } );
               }
            } );

      // Given an aircraft based on the first assembly with an installed engine based on the engine
      // assembly.
      final InventoryKey lAcft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aBuilder ) {
            aBuilder.setAssembly( lAcftAssy );
         }
      } );

      final InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aBuilder ) {
            aBuilder.setAssembly( lEngAssy );
            aBuilder.setParent( lAcft );
            aBuilder.setPosition( lEnginePosition );
         }
      } );

      // Given a tracked inventory attached to the open engine config slot
      final ConfigSlotKey lTrkConfigSlot = Domain.readSubConfigurationSlot(
            Domain.readRootConfigurationSlot( lEngAssy ), TRKCONFIGSLOT );
      final int lTrkPosId = EqpAssmblPos.getFirstPosId( lTrkConfigSlot );
      final String lTrkPosCd =
            EqpAssmblPos.getPosCd( new ConfigSlotPositionKey( lTrkConfigSlot, lTrkPosId ) );

      final InventoryKey lTrk =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aBuilder ) {

                  aBuilder.setParent( lEngine );
                  aBuilder.setLastKnownConfigSlot( ENG_ASSY_CODE_1, TRKCONFIGSLOT, lTrkPosCd );

               }
            } );

      // Given a usage snapshot for an event against the tracked inventory
      // And that usage snapshot contains the calculated parameter
      // And the snaps shot's calculated parameter value is incorrect (differs from that returned by
      // the function)
      final double lOrigCalcParmValue = CALC_PARM_FUNCTION_2_RESULT - 1;
      TaskKey lReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aBuilder ) {
            aBuilder.setInventory( lTrk );
            aBuilder.addUsage( new UsageSnapshot( lTrk, lCalcParm, lOrigCalcParmValue ) );
         }
      } );

      // When recalculateEventCalcParms is called and passed the key for the event
      EventInventoryKey lEvtInvKey = EvtInvTable.findByEventAndInventory( lReq, lTrk ).getPk();
      executeRecalculateEventCalcParms( lEvtInvKey );

      // Then the snaps shot's calculated parameter value is re-calculated using the function
      // associated with the engine assembly (which is inherited by the tracked inventory.
      Double lActualCalcParmValue = EvtInvUsageTable
            .findByPrimaryKey( new EventInventoryUsageKey( lEvtInvKey, lCalcParm ) ).getTsnQt();
      Double lExpectedCalcParmValue = CALC_PARM_FUNCTION_2_RESULT;

      {
         // Since the creation of the DB functions for the calculated parameters were committed to
         // the DB, we need to drop them.
         // Again dropping the function is DDL which means it implicitly commits the transaction.
         // To ensure no data gets accidentally committed we will perform a roll-back.
         //
         // Note: since asserts end processing, it is important to drop the functions prior.
         dropCalcParmFunction( CALC_PARM_FUNCTION_NAME_1 );
         dropCalcParmFunction( CALC_PARM_FUNCTION_NAME_2 );
      }

      assertEquals(
            "Unexpected calculated parameter function called, as evident by its return value.",
            lExpectedCalcParmValue, lActualCalcParmValue );
   }


   private void createCalcParmFunction( String aFunctionName, double aFunction1Result )
         throws Exception {

      String lFunctionStatement;
      lFunctionStatement = "CREATE OR REPLACE FUNCTION " + aFunctionName;
      lFunctionStatement += "   RETURN NUMBER";
      lFunctionStatement += "   IS";
      lFunctionStatement += "   BEGIN";
      lFunctionStatement += "      RETURN " + aFunction1Result + ";";
      lFunctionStatement += "   END " + aFunctionName + ";";

      Domain.createCalculatedParameterEquationFunction( iDatabaseConnectionRule.getConnection(),
            lFunctionStatement );
   }


   private void dropCalcParmFunction( String aFunctionName ) throws Exception {
      Domain.dropCalculatedParameterEquationFunction( iDatabaseConnectionRule.getConnection(),
            aFunctionName );
   }


   private AssemblyKey createAcftAssyWithCalcParm( final String aAcftAssyCode,
         final DataTypeKey aCalcParm, final String aCalcParmCode,
         final String aCalcParmFunctionName ) {

      return Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

         @Override
         public void configure( AircraftAssembly aBuilder ) {
            aBuilder.setCode( aAcftAssyCode );
            aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

               @Override
               public void configure( ConfigurationSlot aBuilder ) {
                  aBuilder.addCalculatedUsageParameter(
                        new DomainConfiguration<CalculatedUsageParameter>() {

                           @Override
                           public void configure( CalculatedUsageParameter aBuilder ) {
                              aBuilder.setDataType( aCalcParm );
                              aBuilder.setCode( aCalcParmCode );
                              aBuilder.setDatabaseCalculation( aCalcParmFunctionName );

                              // Although not part of this test, the precision is mandatory
                              // for re-calculation of calculated parameters.
                              aBuilder.setPrecisionQt( 0 );
                           }
                        } );
               }
            } );
         }
      } );
   }


   private AssemblyKey createAcftAssyWithCalcParm( final String aAcftAssyCode,
         final String aCalcParmCode, final String aCalcParmFunctionName ) {

      // Create the config parameter without a data type, let the builder create one.
      return createAcftAssyWithCalcParm( aAcftAssyCode, null, aCalcParmCode,
            aCalcParmFunctionName );
   }


   private ConfigSlotPositionKey getEnginePosition( String aAcftAssyCode,
         String aEngineSubassyConfigSlotCode ) {

      ConfigSlotKey lAircraftEngineConfigSlot =
            EqpAssmblBom.getBomItemKey( aAcftAssyCode, aEngineSubassyConfigSlotCode );

      return new ConfigSlotPositionKey( lAircraftEngineConfigSlot,
            EqpAssmblPos.getFirstPosId( lAircraftEngineConfigSlot ) );

   }


   private void executeRecalculateEventCalcParms( EventInventoryKey aEvtInvKey ) {

      DataSetArgument lArgsIn = new DataSetArgument();
      lArgsIn.add( "an_event_db_id", aEvtInvKey.getDbId() );
      lArgsIn.add( "an_event_id", aEvtInvKey.getId() );
      DataSetArgument lArgsOut = new DataSetArgument();

      ProcedureStatementFactory.execute( iDatabaseConnectionRule.getConnection(),
            "usage_pkg.recalculateEventCalcParms", lArgsIn, lArgsOut );
   }

}
