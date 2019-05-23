package com.mxi.mx.web.query.inventory;

import static com.mxi.am.db.connection.executor.QueryExecutor.getQueryName;
import static com.mxi.mx.core.key.DataTypeKey.CYCLES;
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
import com.mxi.am.domain.SerializedInventory;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.table.mim.MimDataType;


/**
 * Query test for AssemblyUsage.qrx
 *
 */
public class AssemblyUsageTest {

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


   @Test
   public void itReturnsCurrentNonCalculatedUsageOfAircraft() throws Exception {

      // Given an aircraft with current, non-calculated usages values.
      final BigDecimal lCurrentHours = BigDecimal.valueOf( 12.34 );
      final BigDecimal lCurrentCycles = BigDecimal.valueOf( 56.78 );

      final AssemblyKey lAcftAssy = Domain.createAircraftAssembly();
      InventoryKey lAcft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aBuilder ) {
            aBuilder.setAssembly( lAcftAssy );
            aBuilder.addUsage( HOURS, lCurrentHours );
            aBuilder.addUsage( CYCLES, lCurrentCycles );
         }
      } );

      // When the query is executed.
      QuerySet lQs = executeQuery( lAcft );

      // Then the query returns the values for the aircraft's current, non-calculated usages.
      assertEquals( "Unexpected number of rows returned.", 2, lQs.getRowCount() );
      while ( lQs.next() ) {
         DataTypeKey lDataType = lQs.getKey( DataTypeKey.class, "data_type_db_id", "data_type_id" );
         if ( HOURS.equals( lDataType ) ) {
            assertEquals( "", lCurrentHours, lQs.getBigDecimal( "tsn_qt" ) );
         } else if ( CYCLES.equals( lDataType ) ) {
            assertEquals( "", lCurrentCycles, lQs.getBigDecimal( "tsn_qt" ) );
         } else {
            Assert.fail( "Unknown data type returned from query: " + lDataType );
         }
      }

   }


   @Test
   public void itDoesNotReturnCurrentCalculatedUsageOfAircraft() throws Exception {

      // Given an aircraft with current, calculated usages value.
      final BigDecimal lCurrentHours = BigDecimal.valueOf( 12.34 );
      final BigDecimal lCurrentCalcUsageParmValue = BigDecimal.valueOf( 56.78 );

      final AssemblyKey lAcftAssy =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aBuilder ) {
                  aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot aBuilder ) {
                        aBuilder.addCalculatedUsageParameter(
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

      InventoryKey lAcft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aBuilder ) {
            aBuilder.setAssembly( lAcftAssy );
            aBuilder.addUsage( HOURS, lCurrentHours );
            aBuilder.addUsage( lCalcUsageParmKey, lCurrentCalcUsageParmValue );
         }
      } );

      // When the query is executed.
      QuerySet lQs = executeQuery( lAcft );

      // Then the query does not return the value for the calculated usage parameter.
      assertEquals( "Unexpected number of rows returned.", 1, lQs.getRowCount() );
      lQs.first();
      DataTypeKey lDataType = lQs.getKey( DataTypeKey.class, "data_type_db_id", "data_type_id" );
      Assert.assertNotEquals( "Query unexpectedly returned a row for data type", lCalcUsageParmKey,
            lDataType );
   }


   @Test
   public void itReturnsCurrentNonCalculatedUsageOfAssy() throws Exception {

      // Given an engine (ASSY) with current, non-calculated usages values.
      final BigDecimal lCurrentHours = BigDecimal.valueOf( 12.34 );
      final BigDecimal lCurrentCycles = BigDecimal.valueOf( 56.78 );

      final AssemblyKey lEngineAssy = Domain.createEngineAssembly();
      InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aBuilder ) {
            aBuilder.setAssembly( lEngineAssy );
            aBuilder.addUsage( HOURS, lCurrentHours );
            aBuilder.addUsage( CYCLES, lCurrentCycles );
         }
      } );

      // When the query is executed.
      QuerySet lQs = executeQuery( lEngine );

      // Then the query returns the values for the engine's current, non-calculated usages.
      assertEquals( "Unexpected number of rows returned.", 2, lQs.getRowCount() );
      while ( lQs.next() ) {
         DataTypeKey lDataType = lQs.getKey( DataTypeKey.class, "data_type_db_id", "data_type_id" );
         if ( HOURS.equals( lDataType ) ) {
            assertEquals( "", lCurrentHours, lQs.getBigDecimal( "tsn_qt" ) );
         } else if ( CYCLES.equals( lDataType ) ) {
            assertEquals( "", lCurrentCycles, lQs.getBigDecimal( "tsn_qt" ) );
         } else {
            Assert.fail( "Unknown data type returned from query: " + lDataType );
         }
      }

   }


   @Test
   public void itDoesNotReturnCurrentCalculatedUsageOfAssy() throws Exception {

      // Given an engine (ASSY) with current, calculated usages value.
      final BigDecimal lCurrentHours = BigDecimal.valueOf( 12.34 );
      final BigDecimal lCurrentCalcUsageParmValue = BigDecimal.valueOf( 56.78 );

      final AssemblyKey lEngineAssy =
            Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

               @Override
               public void configure( EngineAssembly aBuilder ) {
                  aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot aBuilder ) {
                        aBuilder.addCalculatedUsageParameter(
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

      InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aBuilder ) {
            aBuilder.setAssembly( lEngineAssy );
            aBuilder.addUsage( HOURS, lCurrentHours );
            aBuilder.addUsage( lCalcUsageParmKey, lCurrentCalcUsageParmValue );
         }
      } );

      // When the query is executed.
      QuerySet lQs = executeQuery( lEngine );

      // Then the query does not return the value for the calculated usage parameter.
      assertEquals( "Unexpected number of rows returned.", 1, lQs.getRowCount() );
      lQs.first();
      DataTypeKey lDataType = lQs.getKey( DataTypeKey.class, "data_type_db_id", "data_type_id" );
      Assert.assertNotEquals( "Query unexpectedly returned a row for data type", lCalcUsageParmKey,
            lDataType );
   }


   @Test
   public void itReturnsCurrentNonCalculatedUsageOfLooseComponent() throws Exception {

      // Given a TRK (component) with current, non-calculated usages values.
      final BigDecimal lCurrentHours = BigDecimal.valueOf( 12.34 );
      final BigDecimal lCurrentCycles = BigDecimal.valueOf( 56.78 );

      InventoryKey lSerializedInv =
            Domain.createSerializedInventory( new DomainConfiguration<SerializedInventory>() {

               @Override
               public void configure( SerializedInventory aBuilder ) {
                  aBuilder.addUsage( HOURS, lCurrentHours );
                  aBuilder.addUsage( CYCLES, lCurrentCycles );

               }

            } );

      // When the query is executed.
      QuerySet lQs = executeQuery( lSerializedInv );

      // Then the query returns the values for the component's current, non-calculated usages.
      assertEquals( "Unexpected number of rows returned.", 2, lQs.getRowCount() );
      while ( lQs.next() ) {
         DataTypeKey lDataType = lQs.getKey( DataTypeKey.class, "data_type_db_id", "data_type_id" );
         if ( HOURS.equals( lDataType ) ) {
            assertEquals( "", lCurrentHours, lQs.getBigDecimal( "tsn_qt" ) );
         } else if ( CYCLES.equals( lDataType ) ) {
            assertEquals( "", lCurrentCycles, lQs.getBigDecimal( "tsn_qt" ) );
         } else {
            Assert.fail( "Unknown data type returned from query: " + lDataType );
         }
      }

   }


   private QuerySet executeQuery( InventoryKey aInventory ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aInventory, "aInvNoDbId", "aInvNoId" );
      return QuerySetFactory.getInstance().executeQuery( getQueryName( getClass() ), lArgs );
   }
}
