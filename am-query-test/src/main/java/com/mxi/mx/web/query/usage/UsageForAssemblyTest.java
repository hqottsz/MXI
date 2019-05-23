package com.mxi.mx.web.query.usage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.AircraftAssembly;
import com.mxi.am.domain.CalculatedUsageParameter;
import com.mxi.am.domain.ConfigurationSlot;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Engine;
import com.mxi.am.domain.EngineAssembly;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.InventoryKey;


/**
 * Query unit test for UsageForAssembly.qrx
 *
 */
public class UsageForAssemblyTest {

   private final static DataTypeKey USAGE_PARM_1 = DataTypeKey.HOURS;
   private final static DataTypeKey USAGE_PARM_2 = DataTypeKey.CYCLES;
   private final static String CALC_PARM_CODE_1 = "CALC_PARM_CODE_1";
   private final static String CALC_PARM_CODE_2 = "CALC_PARM_CODE_2";
   private final static BigDecimal NA = null;

   private static final boolean INCLUDE_CALC_PARMS = true;
   private static final boolean DO_NOT_INCLUDE_CALC_PARMS = false;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    *
    * Verify that the query returns all usage parameters and calculated parameters found in the
    * current usage of an aircraft when aIncludeCalcParms is true.
    *
    */
   @Test
   public void itReturnsAllParamsForAcftWhenIncludeCalcParmsIsTrue() {

      //
      // Given an aircraft with both usage and calculated parameters in its current usage.
      //
      final AssemblyKey lAcftAssy =
            createAcftAssyWithUsageParmAndCalcParm( USAGE_PARM_1, CALC_PARM_CODE_1 );

      final DataTypeKey lCalcParm = readParmameter( lAcftAssy, CALC_PARM_CODE_1 );

      final InventoryKey lAcft =
            createAcftWithUsageAndCalcParms( lAcftAssy, USAGE_PARM_1, lCalcParm );

      //
      // When the query is executed against the aircraft and aIncludeCalcParms is set to true.
      //
      QuerySet lQs = executeQuery( lAcft, INCLUDE_CALC_PARMS );

      //
      // Then the results contain all the parameters.
      //
      assertEquals( "The query did not return the expected number of rows.", 2, lQs.getRowCount() );

      List<DataTypeKey> lReturnedParms = getParmsFromQueryResults( lQs );

      assertTrue( "The query did not return the usage parameter: " + USAGE_PARM_1,
            lReturnedParms.contains( USAGE_PARM_1 ) );
      assertTrue( "The query did not return the calculated parameter: " + lCalcParm,
            lReturnedParms.contains( lCalcParm ) );
   }


   /**
    *
    * Verify that the query does not return calculated parameters found in the current usage of an
    * aircraft when aIncludeCalcParms is false.
    *
    */
   @Test
   public void itDoesNotReturnCalcParmsForAcftWhenIncludeCalcParmsIsFalse() {

      //
      // Given an aircraft with both usage and calculated parameters in its current usage.
      //
      final AssemblyKey lAcftAssy =
            createAcftAssyWithUsageParmAndCalcParm( USAGE_PARM_1, CALC_PARM_CODE_1 );

      final DataTypeKey lCalcParm = readParmameter( lAcftAssy, CALC_PARM_CODE_1 );

      final InventoryKey lAcft =
            createAcftWithUsageAndCalcParms( lAcftAssy, USAGE_PARM_1, lCalcParm );

      //
      // When the query is executed against the aircraft and aIncludeCalcParms is set to false.
      //
      QuerySet lQs = executeQuery( lAcft, DO_NOT_INCLUDE_CALC_PARMS );

      //
      // Then the results contain the usage parameter but not the calculated parameter.
      //
      lQs.next();
      assertEquals( "The query did not return the expected number of rows.", 1, lQs.getRowCount() );
      assertEquals( "Unexpected parameter returned", USAGE_PARM_1,
            lQs.getKey( DataTypeKey.class, "data_type_db_id", "data_type_id" ) );
   }


   /**
    *
    * Verify that the query returns all usage parameters and calculated parameters found in the
    * current usage of an aircraft and its installed engine when querying against the aircraft and
    * when aIncludeCalcParms is true.
    *
    */
   @Test
   public void itReturnsAllParamsForAcftAndEngWhenQueryingTheAcftAndIncludeCalcParmsIsTrue() {

      //
      // Given an aircraft with both usage and calculated parameters in its current usage.
      //
      final AssemblyKey lAcftAssy =
            createAcftAssyWithUsageParmAndCalcParm( USAGE_PARM_1, CALC_PARM_CODE_1 );

      final DataTypeKey lCalcParm1 = readParmameter( lAcftAssy, CALC_PARM_CODE_1 );

      final InventoryKey lAcft =
            createAcftWithUsageAndCalcParms( lAcftAssy, USAGE_PARM_1, lCalcParm1 );

      //
      // Given an engine with both usage and calculated parameters in its current usage that is
      // installed on the aircraft.
      //
      final AssemblyKey lEngineAssy =
            createEngAssyWithUsageParmAndCalcParm( USAGE_PARM_2, CALC_PARM_CODE_2 );

      final DataTypeKey lCalcParm2 = readParmameter( lEngineAssy, CALC_PARM_CODE_2 );

      createInstalledEngineWithUsageAndCalcParms( lEngineAssy, lAcft, USAGE_PARM_2, lCalcParm2 );

      //
      // When the query is executed against the aircraft and aIncludeCalcParms is set to true.
      //
      QuerySet lQs = executeQuery( lAcft, INCLUDE_CALC_PARMS );

      //
      // Then the results contain all the parameters for both the aircraft and the engine.
      //
      assertEquals( "The query did not return the expected number of rows.", 4, lQs.getRowCount() );

      List<DataTypeKey> lReturnedParms = getParmsFromQueryResults( lQs );

      assertTrue( "The query did not return the aircraft usage parameter: " + USAGE_PARM_1,
            lReturnedParms.contains( USAGE_PARM_1 ) );
      assertTrue( "The query did not return the aircraft calculated parameter: " + lCalcParm1,
            lReturnedParms.contains( lCalcParm1 ) );
      assertTrue( "The query did not return the engine usage parameter: " + USAGE_PARM_2,
            lReturnedParms.contains( USAGE_PARM_2 ) );
      assertTrue( "The query did not return the engine calculated parameter: " + lCalcParm2,
            lReturnedParms.contains( lCalcParm2 ) );
   }


   /**
    *
    * Verify that the query does not return calculated parameters found in the current usage of an
    * aircraft and its installed engine when querying against the aircraft and when
    * aIncludeCalcParms is false.
    *
    */
   @Test
   public void
         itDoesNotReturnCalcParmsForAcftAndEngWhenQueryingTheAcftAndIncludeCalcParmsIsFalse() {

      //
      // Given an aircraft with both usage and calculated parameters in its current usage.
      //
      final AssemblyKey lAcftAssy =
            createAcftAssyWithUsageParmAndCalcParm( USAGE_PARM_1, CALC_PARM_CODE_1 );

      final DataTypeKey lCalcParm1 = readParmameter( lAcftAssy, CALC_PARM_CODE_1 );

      final InventoryKey lAcft =
            createAcftWithUsageAndCalcParms( lAcftAssy, USAGE_PARM_1, lCalcParm1 );

      //
      // Given an engine with both usage and calculated parameters in its current usage that is
      // installed on the aircraft.
      //
      final AssemblyKey lEngineAssy =
            createEngAssyWithUsageParmAndCalcParm( USAGE_PARM_2, CALC_PARM_CODE_2 );

      final DataTypeKey lCalcParm2 = readParmameter( lEngineAssy, CALC_PARM_CODE_2 );

      createInstalledEngineWithUsageAndCalcParms( lEngineAssy, lAcft, USAGE_PARM_2, lCalcParm2 );

      //
      // When the query is executed against the aircraft and aIncludeCalcParms is set to true.
      //
      QuerySet lQs = executeQuery( lAcft, DO_NOT_INCLUDE_CALC_PARMS );

      //
      // Then the results contain all the parameters for both the aircraft and the engine.
      //
      assertEquals( "The query did not return the expected number of rows.", 2, lQs.getRowCount() );

      List<DataTypeKey> lReturnedParms = getParmsFromQueryResults( lQs );

      assertTrue( "The query did not return the aircraft usage parameter: " + USAGE_PARM_1,
            lReturnedParms.contains( USAGE_PARM_1 ) );
      assertTrue( "The query did not return the engine usage parameter: " + USAGE_PARM_2,
            lReturnedParms.contains( USAGE_PARM_2 ) );
   }


   /**
    *
    * Verify that the query returns all usage parameters and calculated parameters found in the
    * current usage of an installed engine when querying against the engine and when
    * aIncludeCalcParms is true.
    *
    */
   @Test
   public void itReturnsAllParamsForInstalledEngWhenQueryingTheEngAndIncludeCalcParmsIsTrue() {

      //
      // Given an aircraft with both usage and calculated parameters in its current usage.
      //
      final AssemblyKey lAcftAssy =
            createAcftAssyWithUsageParmAndCalcParm( USAGE_PARM_1, CALC_PARM_CODE_1 );

      final DataTypeKey lCalcParm1 = readParmameter( lAcftAssy, CALC_PARM_CODE_1 );

      final InventoryKey lAcft =
            createAcftWithUsageAndCalcParms( lAcftAssy, USAGE_PARM_1, lCalcParm1 );

      //
      // Given an engine with both usage and calculated parameters in its current usage that is
      // installed on the aircraft.
      //
      final AssemblyKey lEngineAssy =
            createEngAssyWithUsageParmAndCalcParm( USAGE_PARM_2, CALC_PARM_CODE_2 );

      final DataTypeKey lCalcParm2 = readParmameter( lEngineAssy, CALC_PARM_CODE_2 );

      final InventoryKey lEngine = createInstalledEngineWithUsageAndCalcParms( lEngineAssy, lAcft,
            USAGE_PARM_2, lCalcParm2 );

      //
      // When the query is executed against the engine and aIncludeCalcParms is set to true.
      //
      QuerySet lQs = executeQuery( lEngine, INCLUDE_CALC_PARMS );

      //
      // Then the results contain all the parameters for only the engine.
      //
      assertEquals( "The query did not return the expected number of rows.", 2, lQs.getRowCount() );

      List<DataTypeKey> lReturnedParms = getParmsFromQueryResults( lQs );

      assertTrue( "The query did not return the engine usage parameter: " + USAGE_PARM_2,
            lReturnedParms.contains( USAGE_PARM_2 ) );
      assertTrue( "The query did not return the engine calculated parameter: " + lCalcParm2,
            lReturnedParms.contains( lCalcParm2 ) );
   }


   /**
    *
    * Verify that the query does not return calculated parameters found in the current usage of an
    * installed engine when querying against the engine and when aIncludeCalcParms is false.
    *
    */
   @Test
   public void
         itDoesNotReturnCalcParamsForInstalledEngWhenQueryingTheEngAndIncludeCalcParmsIsFalse() {

      //
      // Given an aircraft with both usage and calculated parameters in its current usage.
      //
      final AssemblyKey lAcftAssy =
            createAcftAssyWithUsageParmAndCalcParm( USAGE_PARM_1, CALC_PARM_CODE_1 );

      final DataTypeKey lCalcParm1 = readParmameter( lAcftAssy, CALC_PARM_CODE_1 );

      final InventoryKey lAcft =
            createAcftWithUsageAndCalcParms( lAcftAssy, USAGE_PARM_1, lCalcParm1 );

      //
      // Given an engine with both usage and calculated parameters in its current usage that is
      // installed on the aircraft.
      //
      final AssemblyKey lEngineAssy =
            createEngAssyWithUsageParmAndCalcParm( USAGE_PARM_2, CALC_PARM_CODE_2 );

      final DataTypeKey lCalcParm2 = readParmameter( lEngineAssy, CALC_PARM_CODE_2 );

      final InventoryKey lEngine = createInstalledEngineWithUsageAndCalcParms( lEngineAssy, lAcft,
            USAGE_PARM_2, lCalcParm2 );

      //
      // When the query is executed against the engine and aIncludeCalcParms is set to false.
      //
      QuerySet lQs = executeQuery( lEngine, DO_NOT_INCLUDE_CALC_PARMS );

      //
      // Then the results only contains the usage parameter for the engine and do not contain
      // calculated parameter.
      //
      assertEquals( "The query did not return the expected number of rows.", 1, lQs.getRowCount() );
      lQs.next();
      assertEquals( "Unexpected parameter returned", USAGE_PARM_2,
            lQs.getKey( DataTypeKey.class, "data_type_db_id", "data_type_id" ) );
   }


   /**
    *
    * Verify that the default value for aIncludeCalcParms is true.
    *
    */
   @Test
   public void itReturnsAllParmsByDefaultWhenIncludeCalcParmsIsNotProvided() {

      //
      // Given an aircraft with both usage and calculated parameters in its current usage.
      //
      final AssemblyKey lAcftAssy =
            createAcftAssyWithUsageParmAndCalcParm( USAGE_PARM_1, CALC_PARM_CODE_1 );

      final DataTypeKey lCalcParm = readParmameter( lAcftAssy, CALC_PARM_CODE_1 );

      final InventoryKey lAcft =
            createAcftWithUsageAndCalcParms( lAcftAssy, USAGE_PARM_1, lCalcParm );

      //
      // When the query is executed against the aircraft but aIncludeCalcParms is not provided.
      //
      DataSetArgument lDataSetArgument = new DataSetArgument();
      lDataSetArgument.add( "aInvNoDbId", lAcft.getDbId() );
      lDataSetArgument.add( "aInvNoId", lAcft.getId() );

      QuerySet lQs = QueryExecutor.executeQuery( getClass(), lDataSetArgument );

      //
      // Then by default the results contain all the parameters.
      //
      assertEquals( "The query did not return the expected number of rows.", 2, lQs.getRowCount() );

      List<DataTypeKey> lReturnedParms = getParmsFromQueryResults( lQs );

      assertTrue( "The query did not return the usage parameter: " + USAGE_PARM_1,
            lReturnedParms.contains( USAGE_PARM_1 ) );
      assertTrue( "The query did not return the calculated parameter: " + lCalcParm,
            lReturnedParms.contains( lCalcParm ) );
   }


   /**
    *
    * Verify that only one row is returned for a data type when that data type is associated to more
    * than one calculated parameter. Note; calculated parameters can be created for more than one
    * assembly but have the same data type.
    *
    */
   @Test
   public void itReturnsOneRowWhenDataTypeUsedForManyCalculatedParameters() {

      // Given a calculated parameter for an aircraft assembly using a data type.
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
                                    aBuilder.setCode( CALC_PARM_CODE_1 );
                                 }
                              } );
                     }
                  } );
               }
            } );
      final DataTypeKey lCalcParm1 = readParmameter( lAcftAssy, CALC_PARM_CODE_1 );

      // Given an aircraft based on the aircraft assembly
      // and with the calculated parameter in its current usage.
      final InventoryKey lAcft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aBuilder ) {
            aBuilder.setAssembly( lAcftAssy );
            aBuilder.addUsage( lCalcParm1, BigDecimal.TEN );
         }
      } );

      // Given another calculated parameter for a different assembly but using the same data type.
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
                              aBuilder.setDataType( lCalcParm1 );
                              aBuilder.setCode( CALC_PARM_CODE_2 );
                           }
                        } );
               }
            } );
         }
      } );

      //
      // When the query is executed against the aircraft with aIncludeCalcParms set to true.
      //
      QuerySet lQs = executeQuery( lAcft, INCLUDE_CALC_PARMS );

      //
      // Then the results will contain only one row for the calculated parameter.
      //
      lQs.first();
      assertEquals( "The query did not return the expected number of rows.", 1, lQs.getRowCount() );
      assertEquals( "The query did not return the expected calculated parameter.", lCalcParm1,
            lQs.getKey( DataTypeKey.class, "data_type_db_id", "data_type_id" ) );

   }


   //
   // Private methods (end of test methods).
   //

   private AssemblyKey createAcftAssyWithUsageParmAndCalcParm( final DataTypeKey aUsageParm,
         final String aCalcParm ) {

      return Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

         @Override
         public void configure( AircraftAssembly aBuilder ) {
            aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

               @Override
               public void configure( ConfigurationSlot aBuilder ) {
                  aBuilder.addUsageParameter( aUsageParm );
                  aBuilder.addCalculatedUsageParameter(
                        new DomainConfiguration<CalculatedUsageParameter>() {

                           @Override
                           public void configure( CalculatedUsageParameter aBuilder ) {
                              aBuilder.setCode( aCalcParm );
                           }
                        } );
               }
            } );
         }
      } );
   }


   private AssemblyKey createEngAssyWithUsageParmAndCalcParm( final DataTypeKey aUsageParm,
         final String aCalcParm ) {

      return Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

         @Override
         public void configure( EngineAssembly aBuilder ) {
            aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

               @Override
               public void configure( ConfigurationSlot aBuilder ) {
                  aBuilder.addUsageParameter( aUsageParm );
                  aBuilder.addCalculatedUsageParameter(
                        new DomainConfiguration<CalculatedUsageParameter>() {

                           @Override
                           public void configure( CalculatedUsageParameter aBuilder ) {
                              aBuilder.setCode( aCalcParm );
                           }
                        } );
               }
            } );
         }
      } );
   }


   private InventoryKey createAcftWithUsageAndCalcParms( final AssemblyKey aAcftAssy,
         final DataTypeKey aUsageParm, final DataTypeKey aCalcParm ) {

      return Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aBuilder ) {
            aBuilder.setAssembly( aAcftAssy );
            aBuilder.addUsage( aUsageParm, NA );
            aBuilder.addUsage( aCalcParm, NA );
         }
      } );
   }


   private InventoryKey createInstalledEngineWithUsageAndCalcParms( final AssemblyKey aEngAssy,
         final InventoryKey aAcft, final DataTypeKey aUsageParm, final DataTypeKey aCalcParm ) {

      return Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aBuilder ) {
            aBuilder.setAssembly( aEngAssy );
            aBuilder.setParent( aAcft );
            aBuilder.addUsage( aUsageParm, NA );
            aBuilder.addUsage( aCalcParm, NA );
         }
      } );
   }


   private DataTypeKey readParmameter( final AssemblyKey aAssy, final String aCalcParmCode ) {
      return Domain.readUsageParameter( Domain.readRootConfigurationSlot( aAssy ), aCalcParmCode );
   }


   private QuerySet executeQuery( InventoryKey aInv, boolean aIncludeCalcParms ) {
      DataSetArgument lDataSetArgument = new DataSetArgument();
      lDataSetArgument.add( "aInvNoDbId", aInv.getDbId() );
      lDataSetArgument.add( "aInvNoId", aInv.getId() );
      lDataSetArgument.add( "aIncludeCalcParms", aIncludeCalcParms );

      return QueryExecutor.executeQuery( getClass(), lDataSetArgument );
   }


   private List<DataTypeKey> getParmsFromQueryResults( QuerySet aQs ) {

      int lRowNum = aQs.getRowNumber();
      aQs.beforeFirst();

      List<DataTypeKey> lParms = new ArrayList<DataTypeKey>();
      while ( aQs.next() ) {
         lParms.add( aQs.getKey( DataTypeKey.class, "data_type_db_id", "data_type_id" ) );
      }
      aQs.setRowNumber( lRowNum );

      return lParms;
   }

}
