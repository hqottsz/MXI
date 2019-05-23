package com.mxi.mx.core.services.usagedefn;

import static com.mxi.mx.core.key.DataTypeKey.CYCLES;
import static com.mxi.mx.core.key.DataTypeKey.HOURS;
import static com.mxi.mx.core.key.RefDomainTypeKey.USAGE_PARM;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.CalculatedUsageParameter;
import com.mxi.am.domain.ConfigurationSlot;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.EngineAssembly;
import com.mxi.am.domain.UsageDefinition;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.utils.StringUtils;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.RefDataSourceKey;
import com.mxi.mx.core.key.UsageDefinitionKey;
import com.mxi.mx.core.table.mim.MimDataType;


/**
 * Integration tests for {@linkplain AssemblyUsageDefinitionQueryService}.
 *
 */
public class AssemblyUsageDefinitionQueryServiceTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static final RefDataSourceKey DATA_SOURCE_1 = new RefDataSourceKey( 1, "CODE_1" );
   private static final RefDataSourceKey DATA_SOURCE_2 = new RefDataSourceKey( 1, "CODE_2" );
   private static final RefDataSourceKey DATA_SOURCE_3 = new RefDataSourceKey( 1, "CODE_3" );

   private static final String USAGE_DEFN_NAME_1 = "USAGE_DEFN_NAME_1";
   private static final String USAGE_DEFN_NAME_2 = "USAGE_DEFN_NAME_2";

   private static final String USAGE_PARM_CODE_1 = "USAGE_PARM_CODE_1";
   private static final String USAGE_PARM_CODE_2 = "USAGE_PARM_CODE_2";
   private static final String USAGE_PARM_CODE_3 = "USAGE_PARM_CODE_3";
   private static final String USAGE_PARM_CODE_4 = "USAGE_PARM_CODE_4";

   private static final String CALC_PARM_CODE_1 = "CALC_PARM_CODE_1";
   private static final String CALC_PARM_CODE_2 = "CALC_PARM_CODE_2";

   private static final String CALC_PARM_NAME_1 = "CALC_PARM_NAME_1";
   private static final String CALC_PARM_NAME_2 = "CALC_PARM_NAME_2";

   private static final String ENGINE_ASSY_1 = "ASSY_1";
   private static final String ENGINE_ASSY_2 = "ASSY_2";


   /**
    * Verify that all usage definitions are returned for an assembly.
    *
    * Test for {@linkplain AssemblyUsageDefinitionQueryService#getDataSourceCodes()}
    *
    * <pre>
    * Given an assembly with usage definitions assigned to it.
    * When the usage definitions are retrieved.
    * Then all the assigned usage definitions are returned.
    * </pre>
    */
   @Test
   public void itReturnsAllUsageDefinitions() {

      // Given an engine assembly (example of an assembly) with usage definitions assigned to it.
      // Each usage definition must have a unique data source.
      final AssemblyKey lEngineAssy =
            Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

               @Override
               public void configure( EngineAssembly aBuilder ) {
                  aBuilder.addUsageDefinitionConfiguration( getUsageDefnConfig( DATA_SOURCE_1 ) );
                  aBuilder.addUsageDefinitionConfiguration( getUsageDefnConfig( DATA_SOURCE_2 ) );
                  aBuilder.addUsageDefinitionConfiguration( getUsageDefnConfig( DATA_SOURCE_3 ) );
               }
            } );

      // When the usage definitions are retrieved.
      Set<UsageDefinitionKey> lActualUsageDefns =
            new AssemblyUsageDefinitionQueryService( lEngineAssy ).getUsageDefns();

      // Then all the assigned usage definitions are returned.
      Set<UsageDefinitionKey> lExpectedUsageDefns = new HashSet<UsageDefinitionKey>();
      lExpectedUsageDefns.add( new UsageDefinitionKey( lEngineAssy, DATA_SOURCE_1 ) );
      lExpectedUsageDefns.add( new UsageDefinitionKey( lEngineAssy, DATA_SOURCE_2 ) );
      lExpectedUsageDefns.add( new UsageDefinitionKey( lEngineAssy, DATA_SOURCE_3 ) );

      assertEquals( "Unexpected number of usage defns returned.", 3, lActualUsageDefns.size() );
      for ( UsageDefinitionKey lExpectedUsageDefn : lExpectedUsageDefns ) {
         assertTrue( "Results do not contain usage definition: " + lExpectedUsageDefn,
               lActualUsageDefns.contains( lExpectedUsageDefn ) );
      }
   }


   /**
    * Verify that no usage definitions are returned when an assembly has no associated usage
    * definitions.
    *
    * Test for {@linkplain AssemblyUsageDefinitionQueryService#getDataSourceCodes()}
    *
    * <pre>
    * Given an assembly with no usage definitions assigned to it.
    * When the usage definitions are retrieved.
    * Then no usage definitions are returned.
    * </pre>
    */
   @Test
   public void itReturnsNoUsageDefinitionsWhenNoneAssigned() {

      // Given an assembly with no usage definitions assigned to it.
      final AssemblyKey lEngineAssy = Domain.createEngineAssembly();

      // When the usage definitions are retrieved.
      Set<UsageDefinitionKey> lActualUsageDefns =
            new AssemblyUsageDefinitionQueryService( lEngineAssy ).getUsageDefns();

      // Then no usage definitions are returned.
      assertEquals( "Unexpected number of codes returned.", 0, lActualUsageDefns.size() );
   }


   /**
    * Verify that a name is returned for a provided usage definition.
    *
    * Test for {@linkplain AssemblyUsageDefinitionQueryService#getUsageDefnName()}
    *
    * <pre>
    * Given an assembly with a usage definitions assigned to it.
    * When the name of a usage definition retrieved.
    * Then the name of a usage definition is returned.
    * </pre>
    */
   @Test
   public void itReturnsUsageDefinitionCodeName() {

      // Given an assembly with a usage definitions assigned to it.
      final AssemblyKey lEngineAssy =
            Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

               @Override
               public void configure( EngineAssembly aBuilder ) {
                  aBuilder.addUsageDefinitionConfiguration(
                        new DomainConfiguration<UsageDefinition>() {

                           @Override
                           public void configure( UsageDefinition aBuilder ) {
                              aBuilder.setDataSource( DATA_SOURCE_1 );
                              aBuilder.setName( USAGE_DEFN_NAME_1 );
                           }
                        } );
               }
            } );

      UsageDefinitionKey lUsageDefn1 = new UsageDefinitionKey( lEngineAssy, DATA_SOURCE_1 );

      // When the name of a usage definition retrieved.
      String lName = new AssemblyUsageDefinitionQueryService( lEngineAssy )
            .getUsageDefnCdName( lUsageDefn1 );

      // Then the name of a usage definition is returned.
      String lExpectedUsageDefnCodeName =
            StringUtils.toLabel( DATA_SOURCE_1.getCd(), USAGE_DEFN_NAME_1 );
      assertEquals( "Unexpected usage definition description.", lExpectedUsageDefnCodeName, lName );
   }


   /**
    * Verify that no name is returned when the usage definition is not assigned to the assembly.
    *
    * Test for {@linkplain AssemblyUsageDefinitionQueryService#getUsageDefnName()}
    *
    * <pre>
    * Given an assembly with a usage definition assigned to it.
    * And another assembly with another usage definition assigned to it.
    * When the name of a usage definition is retrieved that is not assigned to the assembly.
    * Then null is returned.
    * </pre>
    */
   @Test
   public void itDoesNotReturnNameOfUsageDefnThatIsNotAssignedToAssembly() {

      // Given an assembly with a usage definition assigned to it.
      final AssemblyKey lEngineAssy1 =
            Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

               @Override
               public void configure( EngineAssembly aBuilder ) {
                  aBuilder.setCode( ENGINE_ASSY_1 );
                  aBuilder.addUsageDefinitionConfiguration(
                        new DomainConfiguration<UsageDefinition>() {

                           @Override
                           public void configure( UsageDefinition aBuilder ) {
                              aBuilder.setDataSource( DATA_SOURCE_1 );
                              aBuilder.setName( USAGE_DEFN_NAME_1 );
                           }
                        } );
               }
            } );

      // Given another assembly with another usage definition assigned to it.
      final AssemblyKey lEngineAssy2 =
            Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

               @Override
               public void configure( EngineAssembly aBuilder ) {
                  aBuilder.setCode( ENGINE_ASSY_2 );
                  aBuilder.addUsageDefinitionConfiguration(
                        new DomainConfiguration<UsageDefinition>() {

                           @Override
                           public void configure( UsageDefinition aBuilder ) {
                              aBuilder.setDataSource( DATA_SOURCE_2 );
                              aBuilder.setName( USAGE_DEFN_NAME_2 );
                           }
                        } );
               }
            } );

      UsageDefinitionKey lUsageDefn2 = new UsageDefinitionKey( lEngineAssy2, DATA_SOURCE_2 );

      // When the name of a usage definition is retrieved that is not assigned to the assembly.
      String lName = new AssemblyUsageDefinitionQueryService( lEngineAssy1 )
            .getUsageDefnCdName( lUsageDefn2 );

      // Then null is returned.
      assertNull( "Unexpected usage definition returned: " + lName, lName );
   }


   /**
    * Verify that the data source codes for all usage definitions are returned for an assembly.
    * Note: the "data source code" is the code portion of the data source key.
    *
    * Test for {@linkplain AssemblyUsageDefinitionQueryService#getUsageDefnDataSourceCodes()}
    *
    * <pre>
    * Given an assembly with usage definitions assigned to it.
    * When the usage definition data source codes are retrieved.
    * Then the data source codes for all the assigned usage definitions are returned.
    * </pre>
    */
   @Test
   public void itReturnsAllDataSourceCodesForAllUsageDefinitions() {

      // Given an engine assembly (example of an assembly) with usage definitions assigned to it.
      // Each usage definition must have a unique data source.
      final AssemblyKey lEngineAssy =
            Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

               @Override
               public void configure( EngineAssembly aBuilder ) {
                  aBuilder.addUsageDefinitionConfiguration( getUsageDefnConfig( DATA_SOURCE_1 ) );
                  aBuilder.addUsageDefinitionConfiguration( getUsageDefnConfig( DATA_SOURCE_2 ) );
                  aBuilder.addUsageDefinitionConfiguration( getUsageDefnConfig( DATA_SOURCE_3 ) );
               }
            } );

      // When the usage definition data source codes are retrieved.
      List<String> lCodes =
            new AssemblyUsageDefinitionQueryService( lEngineAssy ).getDataSourceCodes();

      // Then the data source codes for all the assigned usage definitions are returned.
      assertEquals( "Unexpected number of codes returned.", 3, lCodes.size() );
      assertTrue( "Results do not contain usage definition type code: " + DATA_SOURCE_1.getCd(),
            lCodes.contains( DATA_SOURCE_1.getCd() ) );
      assertTrue( "Results do not contain usage definition type code: " + DATA_SOURCE_2.getCd(),
            lCodes.contains( DATA_SOURCE_2.getCd() ) );
      assertTrue( "Results do not contain usage definition type code: " + DATA_SOURCE_3.getCd(),
            lCodes.contains( DATA_SOURCE_3.getCd() ) );
   }


   /**
    * Verify that no data source codes are returned when an assembly has no associated usage
    * definitions.
    *
    * Note: the "data source code" is the code portion of the data source key.
    *
    * Test for {@linkplain AssemblyUsageDefinitionQueryService#getUsageDefnDataSourceCodes()}
    *
    * <pre>
    * Given an assembly with no usage definitions assigned to it.
    * When the usage definition data source codes are retrieved.
    * Then no data source codes are returned.
    * </pre>
    */
   @Test
   public void itReturnsNoDataSourceCodesWhenNoUsageDefinitionsAssigned() {

      // Given an assembly with no usage definitions assigned to it.
      final AssemblyKey lEngineAssy = Domain.createEngineAssembly();

      // When the usage definition data source codes are retrieved.
      List<String> lCodes =
            new AssemblyUsageDefinitionQueryService( lEngineAssy ).getDataSourceCodes();

      // Then no data source codes are returned.
      assertEquals( "Unexpected number of codes returned.", 0, lCodes.size() );
   }


   /**
    * Verify that all usage parameters assigned to all usage definitions are returned for an
    * assembly.
    *
    * Test for {@linkplain AssemblyUsageDefinitionQueryService#getUsageAndCalcParms()}
    *
    * <pre>
    * Given an assembly with many usage definitions associated to it.
    * And each usage definition as many usage parameters assigned to it.
    * When the usage parameters are retrieved.
    * Then all the usage parameters are returned.
    * </pre>
    */
   @Test
   public void itReturnsAllUsageParameterKeys() {

      // Setup some usage parameter keys to test with.
      final DataTypeKey lUsageParm1 = createUsageParmKey( USAGE_PARM_CODE_1 );
      final DataTypeKey lUsageParm2 = createUsageParmKey( USAGE_PARM_CODE_2 );
      final DataTypeKey lUsageParm3 = createUsageParmKey( USAGE_PARM_CODE_3 );
      final DataTypeKey lUsageParm4 = createUsageParmKey( USAGE_PARM_CODE_4 );

      // Given an assembly with many usage definitions associated to it
      // and each usage definition as many usage parameters assigned to them.
      final AssemblyKey lEngineAssy =
            Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

               @Override
               public void configure( EngineAssembly aBuilder ) {
                  aBuilder.addUsageDefinitionConfiguration( getUsageDefnConfigWithUsageParms(
                        DATA_SOURCE_1, lUsageParm1, lUsageParm2 ) );
                  aBuilder.addUsageDefinitionConfiguration( getUsageDefnConfigWithUsageParms(
                        DATA_SOURCE_2, lUsageParm3, lUsageParm4 ) );
               }

            } );

      // When the usage parameters are retrieved.
      Set<DataTypeKey> lUsageParms =
            new AssemblyUsageDefinitionQueryService( lEngineAssy ).getUsageAndCalcParms();

      // Then all the usage parameters are returned.
      assertEquals( "Unexpected number of usage parameters returned.", 4, lUsageParms.size() );
      assertTrue( "Results do not contain usage parameter with key: " + lUsageParm1,
            lUsageParms.contains( lUsageParm1 ) );
      assertTrue( "Results do not contain usage parameter with key: " + lUsageParm2,
            lUsageParms.contains( lUsageParm2 ) );
      assertTrue( "Results do not contain usage parameter with key: " + lUsageParm3,
            lUsageParms.contains( lUsageParm3 ) );
      assertTrue( "Results do not contain usage parameter with key: " + lUsageParm4,
            lUsageParms.contains( lUsageParm4 ) );
   }


   /**
    * Verify that all calculated parameters are returned for an assembly.
    *
    * Test for {@linkplain AssemblyUsageDefinitionQueryService#getUsageAndCalcParms()}
    *
    * <pre>
    * Given an assembly with many calculated parameters assigned to it.
    * When the usage parameters are retrieved.
    * Then all the calculated parameters are returned.
    *
    * Note: the calculated parameters are returned as part of the list of usage parameters.
    * </pre>
    */
   @Test
   public void itReturnsAllCalculatedParameterKeys() {

      // Given an assembly with many calculated parameters assigned to it.
      final AssemblyKey lEngineAssy =
            Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

               @Override
               public void configure( EngineAssembly aBuilder ) {
                  aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot aBuilder ) {
                        aBuilder
                              .addCalculatedUsageParameter( getCalcParmConfig( CALC_PARM_CODE_1 ) );
                        aBuilder
                              .addCalculatedUsageParameter( getCalcParmConfig( CALC_PARM_CODE_2 ) );
                     }

                  } );
               }
            } );

      DataTypeKey lCalcParm1 = readUsageParameter( lEngineAssy, CALC_PARM_CODE_1 );
      DataTypeKey lCalcParm2 = readUsageParameter( lEngineAssy, CALC_PARM_CODE_2 );

      // When the usage parameters are retrieved.
      Set<DataTypeKey> lUsageParms =
            new AssemblyUsageDefinitionQueryService( lEngineAssy ).getUsageAndCalcParms();

      // Then all the calculated parameters are returned (as part of the usage parameters).
      assertEquals( "Unexpected number of usage parameters returned.", 2, lUsageParms.size() );
      assertTrue( "Results do not contain calculated parameter with code: " + CALC_PARM_CODE_1,
            lUsageParms.contains( lCalcParm1 ) );
      assertTrue( "Results do not contain calculated parameter with code: " + CALC_PARM_CODE_2,
            lUsageParms.contains( lCalcParm2 ) );

   }


   /**
    * Verify that all usage parameters assigned to all usage definitions and all calculated
    * parameters are returned for an assembly.
    *
    * Test for {@linkplain AssemblyUsageDefinitionQueryService#getUsageAndCalcParms()}
    *
    * <pre>
    * Given an assembly with many usage definitions associated to it.
    * And each usage definition has many usage parameters assigned to it.
    * And the assembly has many calculated parameters assigned to it.
    * When the usage parameters are retrieved.
    * Then all usage parameters and calculated parameters are returned.
    *
    * Note: the calculated parameters are returned as part of the list of usage parameters.
    * </pre>
    */
   @Test
   public void itReturnsAllUsageAndCalculatedParameterKeys() {

      // Setup some usage parameter keys to test with.
      final DataTypeKey lUsageParm1 = createUsageParmKey( USAGE_PARM_CODE_1 );
      final DataTypeKey lUsageParm2 = createUsageParmKey( USAGE_PARM_CODE_2 );
      final DataTypeKey lUsageParm3 = createUsageParmKey( USAGE_PARM_CODE_3 );
      final DataTypeKey lUsageParm4 = createUsageParmKey( USAGE_PARM_CODE_4 );

      // Given an assembly with many usage definitions associated to it
      // and each usage definition as many usage parameters assigned to them.
      // And the assembly has many calculated parameters assigned to it.
      final AssemblyKey lEngineAssy =
            Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

               @Override
               public void configure( EngineAssembly aBuilder ) {
                  aBuilder.addUsageDefinitionConfiguration( getUsageDefnConfigWithUsageParms(
                        DATA_SOURCE_1, lUsageParm1, lUsageParm2 ) );
                  aBuilder.addUsageDefinitionConfiguration( getUsageDefnConfigWithUsageParms(
                        DATA_SOURCE_2, lUsageParm3, lUsageParm4 ) );

                  aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot aBuilder ) {
                        aBuilder
                              .addCalculatedUsageParameter( getCalcParmConfig( CALC_PARM_CODE_1 ) );
                        aBuilder
                              .addCalculatedUsageParameter( getCalcParmConfig( CALC_PARM_CODE_2 ) );
                     }
                  } );
               }
            } );

      DataTypeKey lCalcParm1 = readUsageParameter( lEngineAssy, CALC_PARM_CODE_1 );
      DataTypeKey lCalcParm2 = readUsageParameter( lEngineAssy, CALC_PARM_CODE_2 );

      // When the usage parameters are retrieved.
      Set<DataTypeKey> lUsageParms =
            new AssemblyUsageDefinitionQueryService( lEngineAssy ).getUsageAndCalcParms();

      // Then all usage parameters and calculated parameters are returned.
      assertEquals( "Unexpected number of usage parameters returned.", 6, lUsageParms.size() );

      assertTrue( "Results do not contain usage parameter with key: " + lUsageParm1,
            lUsageParms.contains( lUsageParm1 ) );
      assertTrue( "Results do not contain usage parameter with key: " + lUsageParm2,
            lUsageParms.contains( lUsageParm2 ) );
      assertTrue( "Results do not contain usage parameter with key: " + lUsageParm3,
            lUsageParms.contains( lUsageParm3 ) );
      assertTrue( "Results do not contain usage parameter with key: " + lUsageParm4,
            lUsageParms.contains( lUsageParm4 ) );

      assertTrue( "Results do not contain calculated parameter with code: " + CALC_PARM_CODE_1,
            lUsageParms.contains( lCalcParm1 ) );
      assertTrue( "Results do not contain calculated parameter with code: " + CALC_PARM_CODE_2,
            lUsageParms.contains( lCalcParm2 ) );
   }


   /**
    * Verify that a description is returned for a provided usage parameter.
    *
    * Test for {@linkplain AssemblyUsageDefinitionQueryService#getParmDescription()}
    *
    * <pre>
    * Given an assembly with a usage definition associated to it.
    * And the usage definition has a usage parameter assigned to it.
    * When the usage parameter description is retrieved.
    * Then the usage parameter description is returned.
    * </pre>
    */
   @Test
   public void itReturnsUsageParameterDescription() {

      // Given an assembly with a usage definition associated to it
      // and the usage definition has a usage parameter assigned to it.
      final AssemblyKey lEngineAssy =
            Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

               @Override
               public void configure( EngineAssembly aBuilder ) {
                  aBuilder.addUsageDefinitionConfiguration(
                        new DomainConfiguration<UsageDefinition>() {

                           @Override
                           public void configure( UsageDefinition aBuilder ) {
                              aBuilder.addUsageParameter( DataTypeKey.HOURS );
                           }
                        } );
               }
            } );

      // When the usage parameter description is retrieved.
      String lActualDescription = new AssemblyUsageDefinitionQueryService( lEngineAssy )
            .getParmDescription( DataTypeKey.HOURS );

      // Then the usage parameter description is returned.
      // Note: the description is a formated combination of the parameter's code and short
      // description.
      MimDataType lDataType = MimDataType.findByPrimaryKey( DataTypeKey.HOURS );
      String lExpectedDescription =
            lDataType.getDataTypeCd() + " (" + lDataType.getDataTypeSdesc() + ")";

      assertEquals( "Unexpected description returned.", lExpectedDescription, lActualDescription );
   }


   /**
    * Verify that no description is returned for a provided usage parameter when that parameter is
    * not assigned to a usage definition associated to the assembly.
    *
    * Test for {@linkplain AssemblyUsageDefinitionQueryService#getParmDescription()}
    *
    * <pre>
    * Given an assembly with a usage definition associated to it.
    * And the usage definition has a usage parameter assigned to it.
    * When another usage parameter description is retrieved.
    * Then no description is returned.
    * </pre>
    */
   @Test
   public void itReturnsNoDescriptionForUnassociatedUsageParamter() {

      // Given an assembly with a usage definition associated to it
      // and the usage definition has a usage parameter assigned to it.
      final AssemblyKey lEngineAssy =
            Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

               @Override
               public void configure( EngineAssembly aBuilder ) {
                  aBuilder.addUsageDefinitionConfiguration(
                        new DomainConfiguration<UsageDefinition>() {

                           @Override
                           public void configure( UsageDefinition aBuilder ) {
                              aBuilder.addUsageParameter( HOURS );
                           }
                        } );
               }
            } );

      // When another usage parameter description is retrieved.
      String lActualDescription =
            new AssemblyUsageDefinitionQueryService( lEngineAssy ).getParmDescription( CYCLES );

      // Then no description is returned.
      String lExpectedDescription = null;

      assertEquals( "Unexpected description returned.", lExpectedDescription, lActualDescription );
   }


   /**
    * Verify that a description is returned for a provided calculated parameter.
    *
    * Test for {@linkplain AssemblyUsageDefinitionQueryService#getParmDescription()}
    *
    * <pre>
    * Given an assembly with a calculated parameter assigned to it.
    * When the calculated parameter description is retrieved.
    * Then the calculated parameter description is returned.
    * </pre>
    */
   @Test
   public void itReturnsCalculatedParameterDescription() {

      // Given an assembly with a usage definition associated to it
      // and the usage definition has a calculated parameter assigned to it.
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
                                    aBuilder.setCode( CALC_PARM_CODE_1 );
                                    aBuilder.setName( CALC_PARM_NAME_1 );
                                 }
                              } );
                     }
                  } );
               }
            } );

      DataTypeKey lCalcParm1 = readUsageParameter( lEngineAssy, CALC_PARM_CODE_1 );

      // When the calculated parameter description is retrieved.
      String lActualDescription =
            new AssemblyUsageDefinitionQueryService( lEngineAssy ).getParmDescription( lCalcParm1 );

      // Then the calculated parameter description is returned.
      // Note: the description is a formated combination of the parameter's code and name.
      MimDataType lDataType = MimDataType.findByPrimaryKey( lCalcParm1 );
      String lExpectedDescription =
            lDataType.getDataTypeCd() + " (" + lDataType.getDataTypeSdesc() + ")";

      assertEquals( "Unexpected description returned.", lExpectedDescription, lActualDescription );
   }


   /**
    * Verify that no description is returned for a provided calculated parameter when that parameter
    * is not assigned to the assembly.
    *
    * Test for {@linkplain AssemblyUsageDefinitionQueryService#getParmDescription()}
    *
    * <pre>
    * Given an assembly with a calculated parameter assigned to it.
    * And another assembly with another calculated parameter assigned to it.
    * When the description of a calculated parameter is retrieved that is not assigned to the assembly.
    * Then null is returned.
    * </pre>
    */
   @Test
   public void itReturnsNoDescriptionForUnassociatedCalculatedParamter() {

      // Given an assembly with a calculated parameter assigned to it.
      final AssemblyKey lEngineAssy1 =
            Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

               @Override
               public void configure( EngineAssembly aBuilder ) {
                  aBuilder.setCode( ENGINE_ASSY_1 );
                  aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot aBuilder ) {
                        aBuilder.addCalculatedUsageParameter(
                              new DomainConfiguration<CalculatedUsageParameter>() {

                                 @Override
                                 public void configure( CalculatedUsageParameter aBuilder ) {
                                    aBuilder.setCode( CALC_PARM_CODE_1 );
                                    aBuilder.setName( CALC_PARM_NAME_1 );
                                 }
                              } );
                     }
                  } );
               }
            } );

      // Given another assembly with another calculated parameter assigned to it.
      final AssemblyKey lEngineAssy2 =
            Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

               @Override
               public void configure( EngineAssembly aBuilder ) {
                  aBuilder.setCode( ENGINE_ASSY_2 );
                  aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot aBuilder ) {
                        aBuilder.addCalculatedUsageParameter(
                              new DomainConfiguration<CalculatedUsageParameter>() {

                                 @Override
                                 public void configure( CalculatedUsageParameter aBuilder ) {
                                    aBuilder.setCode( CALC_PARM_CODE_2 );
                                    aBuilder.setName( CALC_PARM_NAME_2 );
                                 }
                              } );
                     }
                  } );
               }
            } );

      DataTypeKey lCalcParm2 = readUsageParameter( lEngineAssy2, CALC_PARM_CODE_2 );

      // When the description of a calculated parameter is retrieved that is not assigned to the
      // assembly.
      String lActualDescription = new AssemblyUsageDefinitionQueryService( lEngineAssy1 )
            .getParmDescription( lCalcParm2 );

      // Then no description is returned.
      String lExpectedDescription = null;
      assertEquals( "Unexpected description returned.", lExpectedDescription, lActualDescription );
   }


   /**
    * Verify that a flag is returned indicating that a provided calculated parameter is a calculated
    * parameter.
    *
    * Test for {@linkplain AssemblyUsageDefinitionQueryService#isCalculatedParameter()}
    *
    * <pre>
    * Given an assembly with a usage definition associated to it.
    * And the usage definition has a usage parameter assigned to it.
    * And the assembly has a calculated parameter assigned to it.
    * When retrieving the flag indicating if the calculated parameter is a calculated parameter.
    * Then the returned flag is true.
    * </pre>
    */
   @Test
   public void itReturnsFlagIndicatingThatACalculatedParameterIsACalculatedParameter() {

      // Given an assembly with a usage definition associated to it
      // and the usage definition has a usage parameter assigned to it
      // and the assembly has a calculated parameter assigned to it.
      final AssemblyKey lEngineAssy =
            Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

               @Override
               public void configure( EngineAssembly aBuilder ) {
                  aBuilder.addUsageDefinitionConfiguration(
                        new DomainConfiguration<UsageDefinition>() {

                           @Override
                           public void configure( UsageDefinition aBuilder ) {
                              aBuilder.addUsageParameter( HOURS );
                           }
                        } );
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

      DataTypeKey lCalcParm1 = readUsageParameter( lEngineAssy, CALC_PARM_CODE_1 );

      // When retrieving the flag indicating if the calculated parameter is a calculated parameter.
      boolean lIsCalcParm = new AssemblyUsageDefinitionQueryService( lEngineAssy )
            .isCalculatedParameter( lCalcParm1 );

      // Then the returned flag is true.
      assertTrue( "Unexpectedly indicated calculated parameter is not a calculated parameted: "
            + lIsCalcParm, lIsCalcParm );
   }


   /**
    * Verify that a flag is returned indicating that a provided usage parameter is not a calculated
    * parameter.
    *
    * Test for {@linkplain AssemblyUsageDefinitionQueryService#isCalculatedParameter()}
    *
    * <pre>
    * Given an assembly with a usage definition associated to it.
    * And the usage definition has a usage parameter assigned to it.
    * And the assembly has a calculated parameter assigned to it.
    * When retrieving the flag indicating if the usage parameter is a calculated parameter.
    * Then the returned flag is false.
    * </pre>
    */
   @Test
   public void itReturnsFlagIndicatingThatAUsageParameterIsNotACalculatedParameter() {

      // Given an assembly with a usage definition associated to it
      // and the usage definition has a usage parameter assigned to it
      // and the assembly has a calculated parameter assigned to it.
      final AssemblyKey lEngineAssy =
            Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

               @Override
               public void configure( EngineAssembly aBuilder ) {
                  aBuilder.addUsageDefinitionConfiguration(
                        new DomainConfiguration<UsageDefinition>() {

                           @Override
                           public void configure( UsageDefinition aBuilder ) {
                              aBuilder.addUsageParameter( HOURS );
                           }
                        } );
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

      // When retrieving the flag indicating if the usage parameter is a calculated parameter.
      boolean lIsCalcParm =
            new AssemblyUsageDefinitionQueryService( lEngineAssy ).isCalculatedParameter( HOURS );

      // Then the returned flag is false.
      assertFalse( "Unexpectedly indicated usage parameter is a calculated parameted: " + HOURS,
            lIsCalcParm );
   }


   /**
    * Verify that no flag is returned indicating that a provided calculated parameter is a
    * calculated parameter when the calculated parameter is not associated to the assembly.
    *
    * Test for {@linkplain AssemblyUsageDefinitionQueryService#isCalculatedParameter()}
    *
    * <pre>
    * Given an assembly with a calculated parameter associated to it.
    * And another assembly with another calculated parameter associated to it.
    * When retrieving the flag indicating if the calculated parameter is a calculated parameter for the other assembly.
    * Then no flag is returned.
    * </pre>
    */
   @Test
   public void itReturnsNoCalculatedParameterFlagWhenCalculatedParameterNotAssocaitedToAssembly() {

      // Given an assembly with a calculated parameter associated to it.
      final AssemblyKey lEngineAssy1 =
            Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

               @Override
               public void configure( EngineAssembly aBuilder ) {
                  aBuilder.setCode( ENGINE_ASSY_1 );
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

      // Given another assembly with another calculated parameter associated to it.
      final AssemblyKey lEngineAssy2 =
            Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

               @Override
               public void configure( EngineAssembly aBuilder ) {
                  aBuilder.setCode( ENGINE_ASSY_2 );
                  aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot aBuilder ) {
                        aBuilder.addCalculatedUsageParameter(
                              new DomainConfiguration<CalculatedUsageParameter>() {

                                 @Override
                                 public void configure( CalculatedUsageParameter aBuilder ) {
                                    aBuilder.setCode( CALC_PARM_CODE_2 );
                                 }
                              } );
                     }
                  } );
               }
            } );

      DataTypeKey lCalcParm2 = readUsageParameter( lEngineAssy2, CALC_PARM_CODE_2 );

      // When retrieving the flag indicating if the calculated parameter is a calculated parameter
      // for the other assembly.
      Boolean lIsCalcParm = new AssemblyUsageDefinitionQueryService( lEngineAssy1 )
            .isCalculatedParameter( lCalcParm2 );

      // Then no flag is returned.
      assertNull( "Calculated parameter flag unexpectedly returned.", lIsCalcParm );
   }


   /**
    * Verify that no flag is returned indicating that a provided usage parameter is a calculated
    * parameter when the usage parameter is not associated to the assembly.
    *
    * Test for {@linkplain AssemblyUsageDefinitionQueryService#isCalculatedParameter()}
    *
    * <pre>
    * Given an assembly with a usage definition associated to it.
    * And the usage definition has a usage parameter assigned to it.
    * When retrieving the flag indicating if another usage parameter is a calculated parameter.
    * Then no flag is returned.
    * </pre>
    */
   @Test
   public void itReturnsNoCalculatedParameterFlagWhenUsagedParameterNotAssocaitedToAssembly() {

      // Given an assembly with a usage definition associated to it
      // and the usage definition has a usage parameter assigned to it.
      final AssemblyKey lEngineAssy =
            Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

               @Override
               public void configure( EngineAssembly aBuilder ) {
                  aBuilder.addUsageDefinitionConfiguration(
                        new DomainConfiguration<UsageDefinition>() {

                           @Override
                           public void configure( UsageDefinition aBuilder ) {
                              aBuilder.addUsageParameter( HOURS );
                           }
                        } );
               }
            } );

      // When retrieving the flag indicating if another usage parameter is a calculated parameter.
      Boolean lIsCalcParm =
            new AssemblyUsageDefinitionQueryService( lEngineAssy ).isCalculatedParameter( CYCLES );

      // Then no flag is returned.
      assertNull( "Calculated parameter flag unexpectedly returned.", lIsCalcParm );
   }


   /**
    * Verify that the data source codes of the usage definitions to which a usage parameter is
    * assigned are returned.
    *
    * Note: the "data source code" is the code portion of the data source key.
    *
    * Test for {@linkplain AssemblyUsageDefinitionQueryService#getParmsUsageDefnDataSourceCodes()}
    *
    * <pre>
    * Given an assembly with many usage definitions associated to it.
    * And a usage parameter is assigned to many of the usage definitions.
    * When retrieving the data source codes of the usage definitions to which the usage parameter is assigned.
    * Then the data source codes codes are returned.
    * </pre>
    */
   @Test
   public void itReturnsUsageDefinitionsToWhichUsageParameterIsAssigned() {

      // Given an assembly with many usage definitions associated to it
      // and a usage parameter is assigned to many of the usage definitions.

      final AssemblyKey lEngineAssy =
            Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

               @Override
               public void configure( EngineAssembly aBuilder ) {
                  aBuilder.addUsageDefinitionConfiguration(
                        new DomainConfiguration<UsageDefinition>() {

                           @Override
                           public void configure( UsageDefinition aBuilder ) {
                              aBuilder.setDataSource( DATA_SOURCE_1 );
                              aBuilder.addUsageParameter( HOURS );
                           }
                        } );
                  aBuilder.addUsageDefinitionConfiguration(
                        new DomainConfiguration<UsageDefinition>() {

                           @Override
                           public void configure( UsageDefinition aBuilder ) {
                              aBuilder.setDataSource( DATA_SOURCE_2 );
                              aBuilder.addUsageParameter( HOURS );
                           }
                        } );
                  aBuilder.addUsageDefinitionConfiguration(
                        new DomainConfiguration<UsageDefinition>() {

                           @Override
                           public void configure( UsageDefinition aBuilder ) {
                              aBuilder.setDataSource( DATA_SOURCE_3 );
                              aBuilder.addUsageParameter( CYCLES );
                           }
                        } );
               }
            } );

      // When retrieving the data source codes of the usage definitions to which the usage parameter
      // is assigned.
      Set<String> lUsageDefnCodes = new AssemblyUsageDefinitionQueryService( lEngineAssy )
            .getParmsUsageDefnDataSourceCodes( HOURS );

      // Then the data source codes are returned.
      assertEquals( "Unexpected number of data source code returned.", 2, lUsageDefnCodes.size() );
      assertTrue( "Results do not contain data source code: " + DATA_SOURCE_1.getCd(),
            lUsageDefnCodes.contains( DATA_SOURCE_1.getCd() ) );
      assertTrue( "Results do not contain data source code: " + DATA_SOURCE_2.getCd(),
            lUsageDefnCodes.contains( DATA_SOURCE_2.getCd() ) );
   }


   /**
    * Verify that retrieving data source codes of the usage definitions returns none when a usage
    * parameter is not assigned to any.
    *
    * Note: the "data source code" is the code portion of the data source key.
    *
    * Test for {@linkplain AssemblyUsageDefinitionQueryService#getParmsUsageDefnDataSourceCodes()}
    *
    * <pre>
    * Given an assembly with many usage definitions associated to it.
    * And a usage parameter is assigned to all the usage definitions.
    * When retrieving the data source codes of the usage definitions for another unassigned usage parameter.
    * Then no data source codes are returned.
    * </pre>
    */
   @Test
   public void itReturnsNoDataSourceCodesWhenUsageParameterIsNotAssigned() {

      // Given an assembly with many usage definitions associated to it
      // and a usage parameter is assigned to all the usage definitions.
      final AssemblyKey lEngineAssy =
            Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

               @Override
               public void configure( EngineAssembly aBuilder ) {
                  aBuilder.addUsageDefinitionConfiguration(
                        new DomainConfiguration<UsageDefinition>() {

                           @Override
                           public void configure( UsageDefinition aBuilder ) {
                              aBuilder.setDataSource( DATA_SOURCE_1 );
                              aBuilder.addUsageParameter( HOURS );
                           }
                        } );
                  aBuilder.addUsageDefinitionConfiguration(
                        new DomainConfiguration<UsageDefinition>() {

                           @Override
                           public void configure( UsageDefinition aBuilder ) {
                              aBuilder.setDataSource( DATA_SOURCE_2 );
                              aBuilder.addUsageParameter( HOURS );
                           }
                        } );
               }
            } );

      // When retrieving the data source codes of the usage definitions for another unassigned usage
      // parameter.
      Set<String> lUsageDefnCodes = new AssemblyUsageDefinitionQueryService( lEngineAssy )
            .getParmsUsageDefnDataSourceCodes( CYCLES );

      // Then no data source codes are returned.
      assertEquals( "Unexpected number of data source code returned.", 0, lUsageDefnCodes.size() );

   }


   private DataTypeKey createUsageParmKey( String aUsageParmCode ) {
      return MimDataType.create( aUsageParmCode, null, USAGE_PARM, null, null, null );
   }


   private DataTypeKey readUsageParameter( AssemblyKey aAssembly, String aCode ) {
      return Domain.readUsageParameter( Domain.readRootConfigurationSlot( aAssembly ), aCode );
   }


   private DomainConfiguration<UsageDefinition>
         getUsageDefnConfig( final RefDataSourceKey aDataSource ) {
      return new DomainConfiguration<UsageDefinition>() {

         @Override
         public void configure( UsageDefinition aBuilder ) {
            aBuilder.setDataSource( aDataSource );
         }
      };
   }


   private DomainConfiguration<UsageDefinition> getUsageDefnConfigWithUsageParms(
         final RefDataSourceKey aDataSource, final DataTypeKey... aUsageParms ) {
      return new DomainConfiguration<UsageDefinition>() {

         @Override
         public void configure( UsageDefinition aBuilder ) {
            aBuilder.setDataSource( aDataSource );
            for ( DataTypeKey lUsageParm : aUsageParms ) {
               aBuilder.addUsageParameter( lUsageParm );
            }
         }
      };
   }


   private DomainConfiguration<CalculatedUsageParameter> getCalcParmConfig( final String aCode ) {
      return new DomainConfiguration<CalculatedUsageParameter>() {

         @Override
         public void configure( CalculatedUsageParameter aBuilder ) {
            aBuilder.setCode( aCode );
         }
      };
   }

}
