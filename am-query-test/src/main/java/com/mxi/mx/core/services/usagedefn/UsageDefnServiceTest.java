package com.mxi.mx.core.services.usagedefn;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.AircraftAssembly;
import com.mxi.am.domain.CalculatedUsageParameter;
import com.mxi.am.domain.ConfigurationSlot;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.UsageDefinition;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.unittest.security.SecurityIdentificationStub;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.MimPartNumDataKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.key.RefDataSourceKey;
import com.mxi.mx.core.key.RefEngUnitKey;
import com.mxi.mx.core.key.UsageDefinitionKey;
import com.mxi.mx.core.services.assembly.CalculatedParmTO;
import com.mxi.mx.core.services.assembly.UsageParmTO;
import com.mxi.mx.core.table.eqp.EqpDataSource;
import com.mxi.mx.core.table.eqp.EqpDataSourceSpec;
import com.mxi.mx.core.table.mim.MimPartNumData;


public class UsageDefnServiceTest {

   private static final RefDataSourceKey DATA_SOURCE_1 = new RefDataSourceKey( 1234, "CODE_1" );
   private static final RefDataSourceKey DATA_SOURCE_2 = new RefDataSourceKey( 1234, "CODE_2" );
   private static final RefDataSourceKey DATA_SOURCE_3 = new RefDataSourceKey( 1234, "CODE_3" );

   private static final String CALC_USAGE_PARM_CODE = "CALC_USAGE_PARM_CODE";
   private static final String HOURS_TO_MINUTES_FUNCTION = "HOURS_TO_MINUTES_FUNCTION";

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   @Before
   public void setup() {
      // Many of the UsageDefnService methods require a "current user" to be set.
      // The following creates a stub current user.
      SecurityIdentificationUtils
            .setInstance( new SecurityIdentificationStub( new HumanResourceDomainBuilder().build() ) );
   }


   @After
   public void teardown() {
      SecurityIdentificationUtils.setInstance( null );
   }


   /**
    *
    * Verify that a usage definition can be created for an assembly.
    *
    * <pre>
    *    Given an aircraft assembly
    *       And a data source
    *    When a usage definition is created for an aircraft assembly and data source
    *    Then the usage definition is persisted in the database
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void itCreatesAUsageDefinition() throws Exception {

      // Given
      final AssemblyKey lAircraftAssembly = Domain.createAircraftAssembly();

      // When
      final UsageDefinitionTO lUsageDefinitionTO = new UsageDefinitionTO();
      lUsageDefinitionTO.setAssembly( lAircraftAssembly );
      lUsageDefinitionTO.setDataSource( DATA_SOURCE_1 );

      UsageDefnService.create( lUsageDefinitionTO );

      // Then
      UsageDefinitionKey lUsageDefinitionKey =
            new UsageDefinitionKey( lAircraftAssembly, DATA_SOURCE_1 );

      assertTrue( "The UsageDefinition should exist.",
            EqpDataSource.findByPrimaryKey( lUsageDefinitionKey ).exists() );

   }


   /**
    *
    * Verify that a usage definition cannot be created for an assembly that has a usage definition
    * with the same data source.
    *
    * <pre>
    *     Given an aircraft assembly
    *        And a data source
    *        And a usage definition for the aircraft assembly with the data source
    *     When a usage definition is created for an aircraft assembly and the data source
    *     Then a UsageDefinitionAlreadyExistingException is thrown
    * </pre>
    *
    * @throws Exception
    */
   @Test( expected = UsageDefinitionAlreadyExistingException.class )
   public void itDoesNotCreateAUsageDefinitionWithTheSameDataSource() throws Exception {

      // Given
      final AssemblyKey lAircraftAssembly = Domain.createAircraftAssembly();

      // When
      final UsageDefinitionTO lUsageDefinitionTO = new UsageDefinitionTO();
      lUsageDefinitionTO.setAssembly( lAircraftAssembly );
      lUsageDefinitionTO.setDataSource( DATA_SOURCE_1 );
      final UsageDefinitionTO lUsageDefinitionTODuplicateSource = new UsageDefinitionTO();
      lUsageDefinitionTODuplicateSource.setAssembly( lAircraftAssembly );
      lUsageDefinitionTODuplicateSource.setDataSource( DATA_SOURCE_1 );

      UsageDefnService.create( lUsageDefinitionTO );
      UsageDefnService.create( lUsageDefinitionTODuplicateSource );

      // Then the exception is thrown (refer to @Test's expected class).

   }


   /**
    *
    * Verify that multiple usage definitions can be created for one assembly.
    *
    * <pre>
    *     Given an aircraft assembly
    *        And multiple data sources
    *     When usage definitions is created for an aircraft assembly using various data sources
    *     Then all usage definitions are persisted in the database
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void itCreatesMultipleUsageDefinitionsForAnAircraftAssembly() throws Exception {

      // Given
      final AssemblyKey lAircraftAssembly = Domain.createAircraftAssembly();

      // When
      final UsageDefinitionTO lUsageDefinitionTO = new UsageDefinitionTO();
      lUsageDefinitionTO.setAssembly( lAircraftAssembly );
      lUsageDefinitionTO.setDataSource( DATA_SOURCE_1 );
      final UsageDefinitionTO lUsageDefinitionTODifferentSource = new UsageDefinitionTO();
      lUsageDefinitionTODifferentSource.setAssembly( lAircraftAssembly );
      lUsageDefinitionTODifferentSource.setDataSource( DATA_SOURCE_2 );

      UsageDefnService.create( lUsageDefinitionTO );
      UsageDefnService.create( lUsageDefinitionTODifferentSource );

      // Then
      UsageDefinitionKey lUsageDefinitionKey =
            new UsageDefinitionKey( lAircraftAssembly, DATA_SOURCE_1 );
      UsageDefinitionKey lUsageDefinitionKeyDifferentSource =
            new UsageDefinitionKey( lAircraftAssembly, DATA_SOURCE_2 );

      // Verify both usage definitions exist
      assertTrue( "The initial UsageDefinition should exist.",
            EqpDataSource.findByPrimaryKey( lUsageDefinitionKey ).exists() );
      assertTrue( "The second UsageDefinition with a different source should exist.",
            EqpDataSource.findByPrimaryKey( lUsageDefinitionKeyDifferentSource ).exists() );

   }


   /**
    *
    * Verify that a usage definition can be deleted from an assembly
    *
    * <pre>
    *     Given an aircraft assembly
    *        And the aircraft has multiple usage definitions assigned to it
    *     When delete one of the usage definitions
    *     Then the deleted usage definition should be removed from the aircraft assembly
    *        And the other usage definitions assigned to the aircraft should remain
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void itDeletesAUsageDefinition() throws Exception {

      // Given
      final AssemblyKey lAircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aBuilder ) {
                  aBuilder.addUsageDefinitionConfiguration(
                        new DomainConfiguration<UsageDefinition>() {

                           @Override
                           public void configure( UsageDefinition aBuilder ) {
                              aBuilder.setDataSource( DATA_SOURCE_1 );
                           }
                        } );

                  aBuilder.addUsageDefinitionConfiguration(
                        new DomainConfiguration<UsageDefinition>() {

                           @Override
                           public void configure( UsageDefinition aBuilder ) {
                              aBuilder.setDataSource( DATA_SOURCE_2 );
                           }

                        } );

                  aBuilder.addUsageDefinitionConfiguration(
                        new DomainConfiguration<UsageDefinition>() {

                           @Override
                           public void configure( UsageDefinition aBuilder ) {
                              aBuilder.setDataSource( DATA_SOURCE_3 );
                           }

                        } );

               }
            } );

      // When
      UsageDefinitionKey lUsageDefinitionKeyDelete =
            new UsageDefinitionKey( lAircraftAssembly, DATA_SOURCE_2 );

      UsageDefnService.delete( lUsageDefinitionKeyDelete );

      // Then
      UsageDefinitionKey lUsageDefinitionKeyRemain =
            new UsageDefinitionKey( lAircraftAssembly, DATA_SOURCE_1 );
      UsageDefinitionKey lUsageDefinitionKeyAlsoRemain =
            new UsageDefinitionKey( lAircraftAssembly, DATA_SOURCE_3 );

      // Verify usage definitions 1 and 3 still exist
      assertTrue( "The first UsageDefinition should exist.",
            EqpDataSource.findByPrimaryKey( lUsageDefinitionKeyRemain ).exists() );
      assertTrue( "The third UsageDefinition should exist.",
            EqpDataSource.findByPrimaryKey( lUsageDefinitionKeyAlsoRemain ).exists() );
      // Verify second usage definition doesn't exist
      assertFalse( "The deleted UsageDefinition (second one) should not exist.",
            EqpDataSource.findByPrimaryKey( lUsageDefinitionKeyDelete ).exists() );
   }


   /**
    *
    * Verify that a standard usage parameter can be assigned to a usage definition.
    *
    * <pre>
    *     Given an aircraft assembly
    *        And a usage parameter
    *     When a usage parameter is attempted to be assigned to the usage definition
    *     Then the usage parameter is assigned to the usage definition
    *
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void itAssignsAUsageParameterToAUsageDefinition() throws Exception {

      // Given
      final AssemblyKey lAircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aBuilder ) {
                  aBuilder.addUsageDefinitionConfiguration(
                        new DomainConfiguration<UsageDefinition>() {

                           @Override
                           public void configure( UsageDefinition aBuilder ) {
                              aBuilder.setDataSource( DATA_SOURCE_1 );
                           }
                        } );

               }
            } );

      // When
      UsageDefinitionKey lUsageDefinitionKey =
            new UsageDefinitionKey( lAircraftAssembly, DATA_SOURCE_1 );

      UsageDefnService.assignUsageParm( lUsageDefinitionKey, DataTypeKey.HOURS );

      // Then
      assertTrue( "The usage parameter was not assigned to the usage definition. ",
            EqpDataSourceSpec.findByPrimaryKey( lUsageDefinitionKey, DataTypeKey.HOURS ).exists() );

   }


   /**
    *
    * Verify that a standard usage parameter can be created and assigned to a usage definition
    *
    * <pre>
    *     Given an aircraft assembly with a usage definition
    *     When a usage parameter is created for the usage definition
    *     Then only one usage parameter is created and assigned to the usage definition
    *
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void itCreatesAUsageParameterAndAssignsItToAUsageDefinition() throws Exception {

      // Given
      final AssemblyKey lAircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aBuilder ) {
                  aBuilder.addUsageDefinitionConfiguration(
                        new DomainConfiguration<UsageDefinition>() {

                           @Override
                           public void configure( UsageDefinition aBuilder ) {
                              aBuilder.setDataSource( DATA_SOURCE_1 );
                           }
                        } );

               }
            } );

      // When
      UsageParmTO lUsageParmTO = new UsageParmTO();
      lUsageParmTO.setCode( "TestCode" );
      RefEngUnitKey lEngUnit = RefEngUnitKey.CYCLES;
      lUsageParmTO.setEngUnit( lEngUnit );
      lUsageParmTO.setPrecision( 3 );

      UsageDefinitionKey lUsageDefinitionKey =
            new UsageDefinitionKey( lAircraftAssembly, DATA_SOURCE_1 );

      DataTypeKey lDataTypeKey =
            UsageDefnService.createUsageParm( lUsageDefinitionKey, lUsageParmTO );

      // Then

      // Verify the usage parameter should be created
      assertTrue( "The usage parameter was not created.", lDataTypeKey.isValid() );

      // Verify the usage parameter should be assigned
      assertTrue( "The usage parameter was not assigned to the UsageDefinition.",
            EqpDataSourceSpec.findByPrimaryKey( lUsageDefinitionKey, lDataTypeKey ).exists() );

   }


   /**
    *
    * Verify that a calculated usage parameter can be created.
    *
    * <pre>
    *     Given an aircraft assembly with a usage definition
    *     When a calculated usage parameter is created
    *     Then only one calculated usage parameter is created and assigned to the usage definition
    *
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void itCreatesACalculatedUsageParameter() throws Exception {

      // Given
      final AssemblyKey lAircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aBuilder ) {
                  aBuilder.addUsageDefinitionConfiguration(
                        new DomainConfiguration<UsageDefinition>() {

                           @Override
                           public void configure( UsageDefinition aBuilder ) {
                              aBuilder.setDataSource( DATA_SOURCE_1 );
                           }
                        } );

               }
            } );

      // When
      CalculatedParmTO lCalcParmTO = new CalculatedParmTO();
      lCalcParmTO.setCode( "TestCode" );
      lCalcParmTO.setEngUnit( RefEngUnitKey.CYCLES );
      lCalcParmTO.setPrecision( 3 );

      DataTypeKey lDataTypeKey = UsageDefnService.createCalcParm( lAircraftAssembly, lCalcParmTO );

      // Then

      // Verify only one calculated usage parameter is created.
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lDataTypeKey, "data_type_db_id", "data_type_id" );
      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "mim_calc", lArgs );

      assertEquals( "Unexpected number of calculated parameters created in the MimCalc table.",
            lQs.getRowCount(), 1 );

      // Verify calculated usage parameter is assigned to the usage definition
      ConfigSlotKey lConfigSlotItem = new ConfigSlotKey( lAircraftAssembly, 0 );
      MimPartNumDataKey lMimPartNumData = new MimPartNumDataKey( lConfigSlotItem, lDataTypeKey );

      assertTrue( "The calculated usage paramaeter was not created in the MimPartNumData table.",
            MimPartNumData.findByPrimaryKey( lMimPartNumData ).exists() );

   }


   /**
    *
    * Verify that a calculated usage parameter can be deleted
    *
    * <pre>
    *     Given an aircraft assembly with a usage definition
    *       And a calculated usage parameter assigned to the root config slot of the aircraft assembly
    *     When the calculated usage parameter is deleted
    *     Then only the calculated usage parameter selected is deleted
    *
    * </pre>
    *
    * @throws Exception
    */

   @Test
   public void itDeletesACalculatedParameter() throws Exception {

      // Given
      final AssemblyKey lAircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aBuilder ) {
                  aBuilder.addUsageDefinitionConfiguration(
                        new DomainConfiguration<UsageDefinition>() {

                           @Override
                           public void configure( UsageDefinition aBuilder ) {
                              aBuilder.setDataSource( DATA_SOURCE_1 );
                           }
                        } );

                  aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot aBuilder ) {
                        aBuilder.addCalculatedUsageParameter(
                              new DomainConfiguration<CalculatedUsageParameter>() {

                                 @Override
                                 public void configure( CalculatedUsageParameter aBuilder ) {
                                    aBuilder.setCode( CALC_USAGE_PARM_CODE );
                                    aBuilder.setDatabaseCalculation( HOURS_TO_MINUTES_FUNCTION );
                                    aBuilder.setPrecisionQt( 2 );

                                 }

                              } );
                     }

                  } );

               }
            } );

      // When
      final ConfigSlotKey lRootCsKey = new ConfigSlotKey( lAircraftAssembly, 0 );
      final DataTypeKey lDataTypeKey = getCalcUsageParm( lRootCsKey, CALC_USAGE_PARM_CODE );

      UsageDefnService.deleteCalcParm( lAircraftAssembly, lDataTypeKey );

      // Then
      MimPartNumDataKey lMimPartNumDataKey = new MimPartNumDataKey( lRootCsKey, lDataTypeKey );

      assertFalse( "The calculated usage parameter was not deleted properly.",
            MimPartNumData.findByPrimaryKey( lMimPartNumDataKey ).exists() );

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lDataTypeKey, "data_type_db_id", "data_type_id" );

      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "mim_calc", lArgs );

      assertFalse( "The calculated usage parameter was not deleted in the MimCalc table.",
            lQs.next() );

   }


   /**
    *
    * Verify that when a root config slot parameter is cascaded, all sub-config slots obtain that
    * cascaded parameter.
    *
    * <pre>
    *     Given an aircraft assembly with a usage definition
    *       And a root config slot with a usage parameter
    *       And a sub-config slot
    *     When cascade the parameter in the root config slot
    *     Then the same parameter cascaded is now a part of the sub-config slot
    *
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void itCascadesAParameter() throws Exception {

      // Given
      final AssemblyKey lAircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aBuilder ) {

                  aBuilder.addUsageDefinitionConfiguration(
                        new DomainConfiguration<UsageDefinition>() {

                           @Override
                           public void configure( UsageDefinition aBuilder ) {
                              aBuilder.setDataSource( DATA_SOURCE_1 );
                           }
                        } );

                  aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot aBuilder ) {
                        aBuilder.setConfigurationSlotClass( RefBOMClassKey.ROOT );
                        aBuilder.setCode( "RootConfigSlot" );
                        aBuilder.addUsageParameter( DataTypeKey.HOURS );

                        /*
                         * Empty configuration slot with no parameter, but will have one when
                         * cascade
                         */
                        aBuilder
                              .addConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                                 @Override
                                 public void configure( ConfigurationSlot aBuilder ) {
                                    aBuilder.setCode( "TRKConfigSlot" );
                                    aBuilder.setConfigurationSlotClass( RefBOMClassKey.TRK );

                                 }

                              } );

                     }

                  } );

               }
            } );

      // When
      ConfigSlotKey lRootCsKey = new ConfigSlotKey( lAircraftAssembly, 0 );

      UsageDefnService.cascadeParameter( lRootCsKey, DataTypeKey.HOURS );

      // Then
      ConfigSlotKey lSubCsKey = new ConfigSlotKey( lAircraftAssembly, 1 );
      MimPartNumDataKey lMimPartNumDataKey = new MimPartNumDataKey( lSubCsKey, DataTypeKey.HOURS );

      assertTrue( "The usage parameter should exist in the subconfig slot.",
            MimPartNumData.findByPrimaryKey( lMimPartNumDataKey ).exists() );

   }


   /**
    *
    * Verify that when a parameter in the root slot is uncascaded, all the sub-config slots that
    * have the uncascaded parameter, and only that parameter, is removed.
    *
    * <pre>
    *     Given an aircraft assembly with a usage definition
    *       And a root config slot with multiple usage parameters
    *       And a sub-config slot with the same multiple usage parameters as the root config slot (replicate cascade condiitons)
    *     When uncascade the parameter in the root config slot
    *     Then the uncascaded parameter from the root config slot should be removed from the sub-config slot
    *
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void itUncascadesAParameter() throws Exception {

      // Given
      final AssemblyKey lAircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aBuilder ) {
                  aBuilder.addUsageDefinitionConfiguration(
                        new DomainConfiguration<UsageDefinition>() {

                           @Override
                           public void configure( UsageDefinition aBuilder ) {
                              aBuilder.setDataSource( DATA_SOURCE_1 );
                           }
                        } );

                  aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot aBuilder ) {
                        aBuilder.setConfigurationSlotClass( RefBOMClassKey.ROOT );
                        aBuilder.setCode( "RootConfigSlot" );
                        aBuilder.addUsageParameter( DataTypeKey.HOURS );
                        aBuilder.addUsageParameter( DataTypeKey.LANDING );

                        aBuilder
                              .addConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                                 /*
                                  * Manually add the same parameter to the subconfig to simulate a
                                  * cascade having been done
                                  */
                                 @Override
                                 public void configure( ConfigurationSlot aBuilder ) {
                                    aBuilder.setConfigurationSlotClass( RefBOMClassKey.TRK );
                                    aBuilder.setCode( "TRKConfigSlot" );
                                    aBuilder.addUsageParameter( DataTypeKey.HOURS );
                                    aBuilder.addUsageParameter( DataTypeKey.LANDING );

                                 }
                              } );

                     }

                  } );

               }
            } );

      // When
      ConfigSlotKey lRootCsKey = new ConfigSlotKey( lAircraftAssembly, 0 );

      UsageDefnService.uncascadeParameter( lRootCsKey, DataTypeKey.HOURS );

      // Then
      ConfigSlotKey lSubCsKey = new ConfigSlotKey( lAircraftAssembly, 1 );
      MimPartNumDataKey lHoursMimPartNumDataKey =
            new MimPartNumDataKey( lSubCsKey, DataTypeKey.HOURS );
      MimPartNumDataKey lLandingMimPartNumDataKey =
            new MimPartNumDataKey( lSubCsKey, DataTypeKey.LANDING );

      // Verify the uncascaded parameter no longer exists
      assertFalse( "The uncascaded usage parameter should not  exist in the subconfig slot.",
            MimPartNumData.findByPrimaryKey( lHoursMimPartNumDataKey ).exists() );

      // Verify that the other parameter (the one not being uncascaded) remains in the sub-config
      // slot
      assertTrue( "The landing usage parameter should remain in the subconfig slot.",
            MimPartNumData.findByPrimaryKey( lLandingMimPartNumDataKey ).exists() );

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
}
