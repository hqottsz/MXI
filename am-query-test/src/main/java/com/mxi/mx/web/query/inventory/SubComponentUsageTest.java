package com.mxi.mx.web.query.inventory;

import static com.mxi.am.db.connection.executor.QueryExecutor.getQueryName;
import static com.mxi.mx.core.key.DataTypeKey.HOURS;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

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
import com.mxi.am.domain.TrackedInventory;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.table.mim.MimDataType;


/**
 * Query test for SubComponentUsage.qrx
 *
 */
public class SubComponentUsageTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static final String CALC_USAGE_PARM = "CALC_USAGE_PARM";
   protected static final String ASSY_CODE = "ASSY_CODE";
   protected static final String CONFIG_SLOT_CODE = "CONFIG_SLOT_CODE";
   protected static final String POSITION_CODE = "POSITION_CODE";
   protected static final String PART_GROUP = "PART_GROUP";
   private static final String TRK_CONFIG_SLOT = "TRK_CONFIG_SLOT";
   private static final String TRK_CONFIG_SLOT_CD = "TRK_SLOT_CD";
   private static final String ASSMBL_CD = "A320";
   private static final String ENG_ASSMBL_CD = "CFM380";
   private static final String INVENTORY_KEY_QUERY_SET_COLUMN = "inventory_key";


   @Test
   public void itDoesNotReturnCurrentUsageOfAircraftWithSubComponent() throws Exception {
      // Given an aircraft with current calculated and non-calculated usage
      final BigDecimal lCurrentHours = BigDecimal.valueOf( 12.34 );
      final BigDecimal lCurrentCalcUsageParmValue = BigDecimal.valueOf( 56.78 );

      final AssemblyKey lAcftAssy =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aAcftAssyBuilder ) {
                  aAcftAssyBuilder.setCode( ASSMBL_CD );
                  aAcftAssyBuilder
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aRootCs ) {
                              aRootCs.addConfigurationSlot(
                                    new DomainConfiguration<ConfigurationSlot>() {

                                       @Override
                                       public void configure( ConfigurationSlot aBuilder ) {
                                          aBuilder.addPosition( TRK_CONFIG_SLOT );
                                          aBuilder.setCode( TRK_CONFIG_SLOT_CD );
                                          aBuilder.setConfigurationSlotClass( RefBOMClassKey.TRK );
                                       }

                                    } );
                              aRootCs.addCalculatedUsageParameter(
                                    new DomainConfiguration<CalculatedUsageParameter>() {

                                       @Override
                                       public void configure( CalculatedUsageParameter aBuilder ) {
                                          aBuilder.setCode( CALC_USAGE_PARM );
                                       }
                                    } );
                           }
                        } );
               }
            } );

      final DataTypeKey lCalcUsageParmKey = MimDataType.getDataType( CALC_USAGE_PARM );
      final InventoryKey lAcft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aBuilder ) {
            aBuilder.setAssembly( lAcftAssy );
            aBuilder.addUsage( HOURS, lCurrentHours );
            aBuilder.addUsage( lCalcUsageParmKey, lCurrentCalcUsageParmValue );
            aBuilder.addTracked( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aTrk ) {
                  aTrk.addUsage( HOURS, lCurrentHours );
                  aTrk.addUsage( lCalcUsageParmKey, lCurrentCalcUsageParmValue );
                  aTrk.setLastKnownConfigSlot( ASSMBL_CD, TRK_CONFIG_SLOT_CD, TRK_CONFIG_SLOT );
               }
            } );
         }
      } );

      // When the query is executed.
      QuerySet lQs = executeQuery( lAcft );

      // Then
      assertEquals( "Unexpected number of rows returned.", 1, lQs.getRowCount() );
      lQs.first();
      InventoryKey lInventoryKey = lQs.getKey( InventoryKey.class, INVENTORY_KEY_QUERY_SET_COLUMN );
      Assert.assertNotEquals( "Query unexpectedly returned the usage for aircraft inventory",
            lInventoryKey, lAcft );

   }


   @Test
   public void itReturnsNonCalculatedCurrentUsageOfAircraftSubComponent() throws Exception {
      // Given an aircraft with current calculated and non-calculated usage
      final BigDecimal lCurrentHours = BigDecimal.valueOf( 12.34 );
      final BigDecimal lCurrentCalcUsageParmValue = BigDecimal.valueOf( 56.78 );

      final AssemblyKey lAcftAssy =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aAcftAssyBuilder ) {
                  aAcftAssyBuilder.setCode( ASSMBL_CD );
                  aAcftAssyBuilder
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aRootCs ) {
                              aRootCs.addConfigurationSlot(
                                    new DomainConfiguration<ConfigurationSlot>() {

                                       @Override
                                       public void configure( ConfigurationSlot aBuilder ) {
                                          aBuilder.addPosition( TRK_CONFIG_SLOT );
                                          aBuilder.setCode( TRK_CONFIG_SLOT_CD );
                                          aBuilder.setConfigurationSlotClass( RefBOMClassKey.TRK );
                                       }

                                    } );
                              aRootCs.addCalculatedUsageParameter(
                                    new DomainConfiguration<CalculatedUsageParameter>() {

                                       @Override
                                       public void configure( CalculatedUsageParameter aBuilder ) {
                                          aBuilder.setCode( CALC_USAGE_PARM );
                                       }
                                    } );
                           }
                        } );
               }
            } );

      final DataTypeKey lCalcUsageParmKey = MimDataType.getDataType( CALC_USAGE_PARM );
      final InventoryKey lAcft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aBuilder ) {
            aBuilder.setAssembly( lAcftAssy );
            aBuilder.addUsage( HOURS, lCurrentHours );
            aBuilder.addUsage( lCalcUsageParmKey, lCurrentCalcUsageParmValue );
         }
      } );

      InventoryKey lTrkInv =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aTrk ) {
                  aTrk.setParent( lAcft );
                  aTrk.addUsage( HOURS, lCurrentHours );
                  aTrk.addUsage( lCalcUsageParmKey, lCurrentCalcUsageParmValue );
                  aTrk.setLastKnownConfigSlot( ASSMBL_CD, TRK_CONFIG_SLOT_CD, TRK_CONFIG_SLOT );
               }
            } );

      // When the query is executed.
      QuerySet lQs = executeQuery( lAcft );

      // Then
      assertEquals( "Unexpected number of rows returned.", 1, lQs.getRowCount() );
      lQs.first();
      InventoryKey lInventoryKey = lQs.getKey( InventoryKey.class, INVENTORY_KEY_QUERY_SET_COLUMN );
      Assert.assertEquals( "Query unexpectedly returned the usage for aircraft inventory",
            lInventoryKey, lTrkInv );
      DataTypeKey lDataType = lQs.getKey( DataTypeKey.class, "data_type_key" );
      Assert.assertEquals(
            "Query unexpectedly returned a row for calculated usage of sub component", HOURS,
            lDataType );
   }


   @Test
   public void itDoesNotReturnCalculatedCurrentUsageOfAircraftSubComponent() throws Exception {
      // Given an aircraft with current calculated and non-calculated usage
      final BigDecimal lCurrentHours = BigDecimal.valueOf( 12.34 );
      final BigDecimal lCurrentCalcUsageParmValue = BigDecimal.valueOf( 56.78 );

      final AssemblyKey lAcftAssy =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aAcftAssyBuilder ) {
                  aAcftAssyBuilder.setCode( ASSMBL_CD );
                  aAcftAssyBuilder
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aRootCs ) {
                              aRootCs.addConfigurationSlot(
                                    new DomainConfiguration<ConfigurationSlot>() {

                                       @Override
                                       public void configure( ConfigurationSlot aBuilder ) {
                                          aBuilder.addPosition( TRK_CONFIG_SLOT );
                                          aBuilder.setCode( TRK_CONFIG_SLOT_CD );
                                          aBuilder.setConfigurationSlotClass( RefBOMClassKey.TRK );
                                       }

                                    } );
                              aRootCs.addCalculatedUsageParameter(
                                    new DomainConfiguration<CalculatedUsageParameter>() {

                                       @Override
                                       public void configure( CalculatedUsageParameter aBuilder ) {
                                          aBuilder.setCode( CALC_USAGE_PARM );
                                       }
                                    } );
                           }
                        } );
               }
            } );

      final DataTypeKey lCalcUsageParmKey = MimDataType.getDataType( CALC_USAGE_PARM );
      final InventoryKey lAcft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aBuilder ) {
            aBuilder.setAssembly( lAcftAssy );
            aBuilder.addUsage( HOURS, lCurrentHours );
            aBuilder.addUsage( lCalcUsageParmKey, lCurrentCalcUsageParmValue );
         }
      } );

      InventoryKey lTrkInv =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aTrk ) {
                  aTrk.setParent( lAcft );
                  aTrk.addUsage( HOURS, lCurrentHours );
                  aTrk.addUsage( lCalcUsageParmKey, lCurrentCalcUsageParmValue );
                  aTrk.setLastKnownConfigSlot( ASSMBL_CD, TRK_CONFIG_SLOT_CD, TRK_CONFIG_SLOT );
               }
            } );

      // When the query is executed.
      QuerySet lQs = executeQuery( lAcft );

      // Then
      assertEquals( "Unexpected number of rows returned.", 1, lQs.getRowCount() );
      lQs.first();
      InventoryKey lInventoryKey = lQs.getKey( InventoryKey.class, INVENTORY_KEY_QUERY_SET_COLUMN );
      Assert.assertEquals( "Query unexpectedly returned the usage for aircraft inventory",
            lInventoryKey, lTrkInv );
      DataTypeKey lDataType = lQs.getKey( DataTypeKey.class, "data_type_key" );
      Assert.assertNotEquals(
            "Query unexpectedly returned a row for calculated usage of sub component",
            lCalcUsageParmKey, lDataType );
   }


   @Test
   public void itDoesNotReturnCurrentUsageOfAssyWithSubComponent() throws Exception {
      // Given an engine (ASSY) with current, calculated usages value.
      final BigDecimal lCurrentHours = BigDecimal.valueOf( 12.34 );
      final BigDecimal lCurrentCalcUsageParmValue = BigDecimal.valueOf( 56.78 );

      final AssemblyKey lEngineAssy =
            Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

               @Override
               public void configure( EngineAssembly aEngineAssembly ) {
                  aEngineAssembly.setCode( ENG_ASSMBL_CD );
                  aEngineAssembly
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aRootCs ) {
                              aRootCs.addConfigurationSlot(
                                    new DomainConfiguration<ConfigurationSlot>() {

                                       @Override
                                       public void configure( ConfigurationSlot aBuilder ) {
                                          aBuilder.addPosition( TRK_CONFIG_SLOT );
                                          aBuilder.setCode( TRK_CONFIG_SLOT_CD );
                                          aBuilder.setConfigurationSlotClass( RefBOMClassKey.TRK );
                                       }

                                    } );
                              aRootCs.addCalculatedUsageParameter(
                                    new DomainConfiguration<CalculatedUsageParameter>() {

                                       @Override
                                       public void configure( CalculatedUsageParameter aBuilder ) {
                                          aBuilder.setCode( CALC_USAGE_PARM );
                                       }
                                    } );
                           }
                        } );
               }
            } );
      final DataTypeKey lCalcUsageParmKey = MimDataType.getDataType( CALC_USAGE_PARM );

      final InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aBuilder ) {
            aBuilder.setAssembly( lEngineAssy );
            aBuilder.addUsage( HOURS, lCurrentHours );
            aBuilder.addUsage( lCalcUsageParmKey, lCurrentCalcUsageParmValue );
         }
      } );

      Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

         @Override
         public void configure( TrackedInventory aTrk ) {
            aTrk.addUsage( HOURS, lCurrentHours );
            aTrk.addUsage( lCalcUsageParmKey, lCurrentCalcUsageParmValue );
            aTrk.setLastKnownConfigSlot( ENG_ASSMBL_CD, TRK_CONFIG_SLOT_CD, TRK_CONFIG_SLOT );
            aTrk.setParent( lEngine );
         }

      } );

      // When the query is executed.
      QuerySet lQs = executeQuery( lEngine );

      // Then
      assertEquals( "Unexpected number of rows returned.", 1, lQs.getRowCount() );
      lQs.first();
      InventoryKey lInventoryKey = lQs.getKey( InventoryKey.class, INVENTORY_KEY_QUERY_SET_COLUMN );
      Assert.assertNotEquals( "Query unexpectedly returned the usage for engine inventory",
            lInventoryKey, lEngine );
   }


   @Test
   public void itReturnsNonCalculatedCurrentUsageOfAssySubComponent() throws Exception {
      // Given an engine (ASSY) with current, calculated usages value.
      final BigDecimal lCurrentHours = BigDecimal.valueOf( 12.34 );
      final BigDecimal lCurrentCalcUsageParmValue = BigDecimal.valueOf( 56.78 );

      final AssemblyKey lEngineAssy =
            Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

               @Override
               public void configure( EngineAssembly aEngineAssembly ) {
                  aEngineAssembly.setCode( ENG_ASSMBL_CD );
                  aEngineAssembly
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aRootCs ) {
                              aRootCs.addConfigurationSlot(
                                    new DomainConfiguration<ConfigurationSlot>() {

                                       @Override
                                       public void configure( ConfigurationSlot aBuilder ) {
                                          aBuilder.addPosition( TRK_CONFIG_SLOT );
                                          aBuilder.setCode( TRK_CONFIG_SLOT_CD );
                                          aBuilder.setConfigurationSlotClass( RefBOMClassKey.TRK );
                                       }

                                    } );
                              aRootCs.addCalculatedUsageParameter(
                                    new DomainConfiguration<CalculatedUsageParameter>() {

                                       @Override
                                       public void configure( CalculatedUsageParameter aBuilder ) {
                                          aBuilder.setCode( CALC_USAGE_PARM );
                                       }
                                    } );
                           }
                        } );
               }
            } );
      final DataTypeKey lCalcUsageParmKey = MimDataType.getDataType( CALC_USAGE_PARM );

      final InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aBuilder ) {
            aBuilder.setAssembly( lEngineAssy );
            aBuilder.addUsage( HOURS, lCurrentHours );
            aBuilder.addUsage( lCalcUsageParmKey, lCurrentCalcUsageParmValue );
         }
      } );

      InventoryKey lTrk =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aTrk ) {
                  aTrk.addUsage( HOURS, lCurrentHours );
                  aTrk.addUsage( lCalcUsageParmKey, lCurrentCalcUsageParmValue );
                  aTrk.setLastKnownConfigSlot( ENG_ASSMBL_CD, TRK_CONFIG_SLOT_CD, TRK_CONFIG_SLOT );
                  aTrk.setParent( lEngine );
               }

            } );

      // When the query is executed.
      QuerySet lQs = executeQuery( lEngine );

      // Then
      assertEquals( "Unexpected number of rows returned.", 1, lQs.getRowCount() );
      lQs.first();
      InventoryKey lInventoryKey = lQs.getKey( InventoryKey.class, INVENTORY_KEY_QUERY_SET_COLUMN );
      Assert.assertEquals( "Query unexpectedly did not return the usage for TRK inventory",
            lInventoryKey, lTrk );
      DataTypeKey lDataType = lQs.getKey( DataTypeKey.class, "data_type_key" );
      Assert.assertEquals( "Query unexpectedly did not return a row for usage of sub component",
            HOURS, lDataType );

   }


   @Test
   public void itDoesNotReturnCalculatedCurrentUsageOfAssySubComponent() throws Exception {
      // Given an engine (ASSY) with current, calculated usages value.
      final BigDecimal lCurrentHours = BigDecimal.valueOf( 12.34 );
      final BigDecimal lCurrentCalcUsageParmValue = BigDecimal.valueOf( 56.78 );

      final AssemblyKey lEngineAssy =
            Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

               @Override
               public void configure( EngineAssembly aEngineAssembly ) {
                  aEngineAssembly.setCode( ENG_ASSMBL_CD );
                  aEngineAssembly
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aRootCs ) {
                              aRootCs.addConfigurationSlot(
                                    new DomainConfiguration<ConfigurationSlot>() {

                                       @Override
                                       public void configure( ConfigurationSlot aBuilder ) {
                                          aBuilder.addPosition( TRK_CONFIG_SLOT );
                                          aBuilder.setCode( TRK_CONFIG_SLOT_CD );
                                          aBuilder.setConfigurationSlotClass( RefBOMClassKey.TRK );
                                       }

                                    } );
                              aRootCs.addCalculatedUsageParameter(
                                    new DomainConfiguration<CalculatedUsageParameter>() {

                                       @Override
                                       public void configure( CalculatedUsageParameter aBuilder ) {
                                          aBuilder.setCode( CALC_USAGE_PARM );
                                       }
                                    } );
                           }
                        } );
               }
            } );
      final DataTypeKey lCalcUsageParmKey = MimDataType.getDataType( CALC_USAGE_PARM );

      final InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aBuilder ) {
            aBuilder.setAssembly( lEngineAssy );
            aBuilder.addUsage( HOURS, lCurrentHours );
            aBuilder.addUsage( lCalcUsageParmKey, lCurrentCalcUsageParmValue );
         }
      } );

      InventoryKey lTrk =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aTrk ) {
                  aTrk.addUsage( HOURS, lCurrentHours );
                  aTrk.addUsage( lCalcUsageParmKey, lCurrentCalcUsageParmValue );
                  aTrk.setLastKnownConfigSlot( ENG_ASSMBL_CD, TRK_CONFIG_SLOT_CD, TRK_CONFIG_SLOT );
                  aTrk.setParent( lEngine );
               }

            } );

      // When the query is executed.
      QuerySet lQs = executeQuery( lEngine );

      // Then
      assertEquals( "Unexpected number of rows returned.", 1, lQs.getRowCount() );
      lQs.first();
      InventoryKey lInventoryKey = lQs.getKey( InventoryKey.class, INVENTORY_KEY_QUERY_SET_COLUMN );
      Assert.assertEquals( "Query unexpectedly did not return the usage for TRK inventory",
            lInventoryKey, lTrk );
      DataTypeKey lDataType = lQs.getKey( DataTypeKey.class, "data_type_key" );
      Assert.assertNotEquals(
            "Query unexpectedly returned a row for calculated usage of sub component",
            lCalcUsageParmKey, lDataType );
   }


   @Test
   public void itDoesNotReturnUsageOfLooseComponentWithSubComponent() throws Exception {

      final BigDecimal lCurrentHours = BigDecimal.valueOf( 12.34 );
      final InventoryKey lTrk =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aTrk ) {
                  aTrk.addUsage( HOURS, lCurrentHours );
               }
            } );

      Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

         @Override
         public void configure( TrackedInventory aTrk ) {
            aTrk.addUsage( HOURS, lCurrentHours );
            aTrk.setParent( lTrk );
         }
      } );

      // When the query is executed.
      QuerySet lQs = executeQuery( lTrk );

      // Then
      assertEquals( "Unexpected number of rows returned.", 1, lQs.getRowCount() );
      lQs.first();
      InventoryKey lInventoryKey = lQs.getKey( InventoryKey.class, INVENTORY_KEY_QUERY_SET_COLUMN );
      Assert.assertNotEquals( "Query unexpectedly returned the usage for parent TRK inventory",
            lInventoryKey, lTrk );
   }


   @Test
   public void itReturnsNonCalculatedUsageOfLooseComponentSubComponent() throws Exception {

      final BigDecimal lCurrentHours = BigDecimal.valueOf( 12.34 );
      final InventoryKey lTrk =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aTrk ) {
                  aTrk.addUsage( HOURS, lCurrentHours );
               }
            } );

      InventoryKey lSubCompTrk =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aTrk ) {
                  aTrk.addUsage( HOURS, lCurrentHours );
                  aTrk.setParent( lTrk );
               }
            } );

      // When the query is executed.
      QuerySet lQs = executeQuery( lTrk );

      // Then
      assertEquals( "Unexpected number of rows returned.", 1, lQs.getRowCount() );
      lQs.first();
      InventoryKey lInventoryKey = lQs.getKey( InventoryKey.class, INVENTORY_KEY_QUERY_SET_COLUMN );
      Assert.assertEquals( "Query unexpectedly returned the usage for parent TRK inventory",
            lInventoryKey, lSubCompTrk );

      DataTypeKey lDataType = lQs.getKey( DataTypeKey.class, "data_type_key" );
      Assert.assertEquals( "Query unexpectedly did not return a row for usage of sub component",
            HOURS, lDataType );
   }


   private QuerySet executeQuery( InventoryKey aInventory ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aInventory, "aInvNoDbId", "aInvNoId" );
      return QuerySetFactory.getInstance().executeQuery( getQueryName( getClass() ), lArgs );
   }

}
