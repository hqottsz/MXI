package com.mxi.mx.core.services.assembly.partnospecifc;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.AccumulatedParameter;
import com.mxi.am.domain.ConfigurationSlot;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.EngineAssembly;
import com.mxi.am.domain.Manufacturer;
import com.mxi.am.domain.Part;
import com.mxi.am.domain.PartGroup;
import com.mxi.am.domain.UsageDefinition;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.ejb.assembly.AssemblyBean;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.MimPartNumDataKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefDataSourceKey;
import com.mxi.mx.core.key.RefEngUnitKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.UsageDefinitionKey;
import com.mxi.mx.core.table.mim.MimDataType;
import com.mxi.mx.core.table.mim.MimPartNumData;


/**
 * This class is for testing the accumulated parameter creation
 *
 */
public class AccumulatedParameterCreationTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static final RefDataSourceKey BULK = new RefDataSourceKey( 0, "BULK" );

   private static final String ASSMBL_PART_NO_CODE_ONE = "ASSMBL_PART_NO_CODE_ONE";

   private static final String ASSMBL_PART_NO_CODE_TWO = "ASSMBL_PART_NO_CODE_TWO";

   private static final String CYCLES_CODE = "CYCLES";

   private static final String CYCLES_NAME = "Cycles";

   private static final String MANUFACT = "MANUFACTOR";


   /**
    * Given an engine assembly with bulk definition.<br/>
    * And two part numbers on root config slot.
    *
    * When creating the accumulated parms.
    *
    * Then verify one accumulated parm is created for each part number
    *
    */
   @Test
   public void itCreatesOneAccumulatedParmForEachPartNumber() {

      final AssemblyKey lEngineAssembly =
            Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

               @Override
               public void configure( EngineAssembly aBuilder ) {
                  aBuilder.addUsageDefinitionConfiguration(
                        new DomainConfiguration<UsageDefinition>() {

                           @Override
                           public void configure( UsageDefinition aBuilder ) {
                              aBuilder.setDataSource( BULK );

                           }

                        } );
                  aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot aBuilder ) {
                        aBuilder.addUsageParameter( DataTypeKey.CYCLES );
                        aBuilder.addPartGroup( new DomainConfiguration<PartGroup>() {

                           @Override
                           public void configure( PartGroup aBuilder ) {
                              aBuilder.addPart( new DomainConfiguration<Part>() {

                                 @Override
                                 public void configure( Part aBuilder ) {
                                    aBuilder.setInventoryClass( RefInvClassKey.ASSY );
                                    aBuilder.setCode( ASSMBL_PART_NO_CODE_ONE );

                                 }

                              } );

                              aBuilder.addPart( new DomainConfiguration<Part>() {

                                 @Override
                                 public void configure( Part aBuilder ) {
                                    aBuilder.setInventoryClass( RefInvClassKey.ASSY );
                                    aBuilder.setCode( ASSMBL_PART_NO_CODE_TWO );
                                 }

                              } );

                           }

                        } );

                     }

                  } );

               }

            } );

      AssemblyBean lAssemblyBean = new AssemblyBean();
      Map<PartNoKey, DataTypeKey> lCreatedDataTypeMap = lAssemblyBean.createAccumulatedParameters(
            lEngineAssembly, DataTypeKey.CYCLES, new UsageDefinitionKey( lEngineAssembly, BULK ) );

      assertThat( lCreatedDataTypeMap.size(), equalTo( 2 ) );

      for ( PartNoKey lPartNoKey : lCreatedDataTypeMap.keySet() ) {
         MimPartNumDataKey lMimPartNumDataKey = new MimPartNumDataKey(
               new ConfigSlotKey( lEngineAssembly, 0 ), lCreatedDataTypeMap.get( lPartNoKey ) );
         assertThat( MimPartNumData.findByPrimaryKey( lMimPartNumDataKey ), notNullValue() );

      }

   }


   /**
    * Given an engine assembly with bulk definition.<br/>
    * And two part numbers on root config slot.
    *
    * When creating the accumulated parms.
    *
    * Then verify one accumulated parm is created for each part number.<br/>
    * And the code is following the standard of <main_usage_parm_code>_ACC_<assembly-part-no>.
    *
    */
   @Test
   public void itCreatesOneAccumulatedParmForEachPartNumberWithCorrectCode() {

      final AssemblyKey lEngineAssembly =
            Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

               @Override
               public void configure( EngineAssembly aBuilder ) {
                  aBuilder.addUsageDefinitionConfiguration(
                        new DomainConfiguration<UsageDefinition>() {

                           @Override
                           public void configure( UsageDefinition aBuilder ) {
                              aBuilder.setDataSource( BULK );

                           }

                        } );
                  aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot aBuilder ) {
                        aBuilder.addUsageParameter( DataTypeKey.CYCLES );
                        aBuilder.addPartGroup( new DomainConfiguration<PartGroup>() {

                           @Override
                           public void configure( PartGroup aBuilder ) {
                              aBuilder.addPart( new DomainConfiguration<Part>() {

                                 @Override
                                 public void configure( Part aBuilder ) {
                                    aBuilder.setInventoryClass( RefInvClassKey.ASSY );
                                    aBuilder.setCode( ASSMBL_PART_NO_CODE_ONE );

                                 }

                              } );

                           }

                        } );

                     }

                  } );

               }

            } );

      AssemblyBean lAssemblyBean = new AssemblyBean();
      Map<PartNoKey, DataTypeKey> lCreatedDataTypeMap = lAssemblyBean.createAccumulatedParameters(
            lEngineAssembly, DataTypeKey.CYCLES, new UsageDefinitionKey( lEngineAssembly, BULK ) );

      assertThat( lCreatedDataTypeMap.size(), equalTo( 1 ) );

      for ( PartNoKey lPartNoKey : lCreatedDataTypeMap.keySet() ) {
         String lActualParmCode = MimDataType
               .findByPrimaryKey( lCreatedDataTypeMap.get( lPartNoKey ) ).getDataTypeCd();
         String lExpectedParmCode = CYCLES_CODE + "_ACC_" + ASSMBL_PART_NO_CODE_ONE;
         assertThat( lActualParmCode, equalTo( lExpectedParmCode ) );
      }

   }


   /**
    * Given an engine assembly with bulk definition.<br/>
    * And two part numbers on root config slot.
    *
    * When creating the accumulated parms.
    *
    * Then verify one accumulated parm is created for each part number.<br/>
    * And the name is following the standard of <main_usage_parm_name> at <assembly-part-no>.
    *
    */
   @Test
   public void itCreatesOneAccumulatedParmForEachPartNumberWithCorrectName() {

      final AssemblyKey lEngineAssembly =
            Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

               @Override
               public void configure( EngineAssembly aBuilder ) {
                  aBuilder.addUsageDefinitionConfiguration(
                        new DomainConfiguration<UsageDefinition>() {

                           @Override
                           public void configure( UsageDefinition aBuilder ) {
                              aBuilder.setDataSource( BULK );

                           }

                        } );
                  aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot aBuilder ) {
                        aBuilder.addUsageParameter( DataTypeKey.CYCLES );
                        aBuilder.addPartGroup( new DomainConfiguration<PartGroup>() {

                           @Override
                           public void configure( PartGroup aBuilder ) {
                              aBuilder.addPart( new DomainConfiguration<Part>() {

                                 @Override
                                 public void configure( Part aBuilder ) {
                                    aBuilder.setInventoryClass( RefInvClassKey.ASSY );
                                    aBuilder.setCode( ASSMBL_PART_NO_CODE_ONE );

                                 }

                              } );

                           }

                        } );

                     }

                  } );

               }

            } );

      AssemblyBean lAssemblyBean = new AssemblyBean();
      Map<PartNoKey, DataTypeKey> lCreatedDataTypeMap = lAssemblyBean.createAccumulatedParameters(
            lEngineAssembly, DataTypeKey.CYCLES, new UsageDefinitionKey( lEngineAssembly, BULK ) );

      assertThat( lCreatedDataTypeMap.size(), equalTo( 1 ) );

      for ( PartNoKey lPartNoKey : lCreatedDataTypeMap.keySet() ) {
         String lActualParmName = MimDataType
               .findByPrimaryKey( lCreatedDataTypeMap.get( lPartNoKey ) ).getDataTypeSdesc();
         String lExpectedParmName = CYCLES_NAME + " at " + ASSMBL_PART_NO_CODE_ONE;
         assertThat( lActualParmName, equalTo( lExpectedParmName ) );
      }

   }


   /**
    * Given an engine assembly with bulk definition.<br/>
    * And two part numbers on root config slot.
    *
    * When creating the accumulated parms.
    *
    * Then verify one accumulated parm is created for each part number.<br/>
    * And the engineering unit is same as the aggregated parm's.
    *
    */
   @Test
   public void itCreatesOneAccumulatedParmForEachPartNumberWithSameEngUnitWithAggregatedParm() {

      final AssemblyKey lEngineAssembly =
            Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

               @Override
               public void configure( EngineAssembly aBuilder ) {
                  aBuilder.addUsageDefinitionConfiguration(
                        new DomainConfiguration<UsageDefinition>() {

                           @Override
                           public void configure( UsageDefinition aBuilder ) {
                              aBuilder.setDataSource( BULK );

                           }

                        } );
                  aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot aBuilder ) {
                        aBuilder.addUsageParameter( DataTypeKey.CYCLES );
                        aBuilder.addPartGroup( new DomainConfiguration<PartGroup>() {

                           @Override
                           public void configure( PartGroup aBuilder ) {
                              aBuilder.addPart( new DomainConfiguration<Part>() {

                                 @Override
                                 public void configure( Part aBuilder ) {
                                    aBuilder.setInventoryClass( RefInvClassKey.ASSY );
                                    aBuilder.setCode( ASSMBL_PART_NO_CODE_ONE );

                                 }

                              } );

                           }

                        } );

                     }

                  } );

               }

            } );

      AssemblyBean lAssemblyBean = new AssemblyBean();
      Map<PartNoKey, DataTypeKey> lCreatedDataTypeMap = lAssemblyBean.createAccumulatedParameters(
            lEngineAssembly, DataTypeKey.CYCLES, new UsageDefinitionKey( lEngineAssembly, BULK ) );

      assertThat( lCreatedDataTypeMap.size(), equalTo( 1 ) );

      RefEngUnitKey lExpectedEngUnitKey =
            MimDataType.findByPrimaryKey( DataTypeKey.CYCLES ).getEngUnit();

      for ( PartNoKey lPartNoKey : lCreatedDataTypeMap.keySet() ) {
         RefEngUnitKey lActualEngUnitKey =
               MimDataType.findByPrimaryKey( lCreatedDataTypeMap.get( lPartNoKey ) ).getEngUnit();
         assertThat( lActualEngUnitKey, equalTo( lExpectedEngUnitKey ) );
      }

   }


   /**
    * Given an engine assembly with bulk definition.<br/>
    * And two part numbers on root config slot.
    *
    * When creating the accumulated parms.
    *
    * Then verify one accumulated parm is created for each part number.<br/>
    * And the precision is same as the aggregated parm's.
    *
    */
   @Test
   public void itCreatesOneAccumulatedParmForEachPartNumberWithSamePrecisionWithAggregatedParm() {

      final AssemblyKey lEngineAssembly =
            Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

               @Override
               public void configure( EngineAssembly aBuilder ) {
                  aBuilder.addUsageDefinitionConfiguration(
                        new DomainConfiguration<UsageDefinition>() {

                           @Override
                           public void configure( UsageDefinition aBuilder ) {
                              aBuilder.setDataSource( BULK );

                           }

                        } );
                  aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot aBuilder ) {
                        aBuilder.addUsageParameter( DataTypeKey.CYCLES );
                        aBuilder.addPartGroup( new DomainConfiguration<PartGroup>() {

                           @Override
                           public void configure( PartGroup aBuilder ) {
                              aBuilder.addPart( new DomainConfiguration<Part>() {

                                 @Override
                                 public void configure( Part aBuilder ) {
                                    aBuilder.setInventoryClass( RefInvClassKey.ASSY );
                                    aBuilder.setCode( ASSMBL_PART_NO_CODE_ONE );

                                 }

                              } );

                           }

                        } );

                     }

                  } );

               }

            } );

      AssemblyBean lAssemblyBean = new AssemblyBean();
      Map<PartNoKey, DataTypeKey> lCreatedDataTypeMap = lAssemblyBean.createAccumulatedParameters(
            lEngineAssembly, DataTypeKey.CYCLES, new UsageDefinitionKey( lEngineAssembly, BULK ) );

      assertThat( lCreatedDataTypeMap.size(), equalTo( 1 ) );

      int lExpectedPrecision = MimDataType.getEntryPrecQt( DataTypeKey.CYCLES );

      for ( PartNoKey lPartNoKey : lCreatedDataTypeMap.keySet() ) {
         int lActualPrecision = MimDataType.getEntryPrecQt( lCreatedDataTypeMap.get( lPartNoKey ) );
         assertThat( lActualPrecision, equalTo( lExpectedPrecision ) );
      }

   }


   /**
    * Given an engine assembly with bulk definition.<br/>
    * And two part numbers on root config slot.
    *
    * When creating the accumulated parms.
    *
    * Then verify one accumulated parm is created for each part number And the description is blank.
    *
    */
   @Test
   public void itCreatesOneAccumulatedParmForEachPartNumberWithBlankDescription() {

      final AssemblyKey lEngineAssembly =
            Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

               @Override
               public void configure( EngineAssembly aBuilder ) {
                  aBuilder.addUsageDefinitionConfiguration(
                        new DomainConfiguration<UsageDefinition>() {

                           @Override
                           public void configure( UsageDefinition aBuilder ) {
                              aBuilder.setDataSource( BULK );

                           }

                        } );
                  aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot aBuilder ) {
                        aBuilder.addUsageParameter( DataTypeKey.CYCLES );
                        aBuilder.addPartGroup( new DomainConfiguration<PartGroup>() {

                           @Override
                           public void configure( PartGroup aBuilder ) {
                              aBuilder.addPart( new DomainConfiguration<Part>() {

                                 @Override
                                 public void configure( Part aBuilder ) {
                                    aBuilder.setInventoryClass( RefInvClassKey.ASSY );
                                    aBuilder.setCode( ASSMBL_PART_NO_CODE_ONE );

                                 }

                              } );

                           }

                        } );

                     }

                  } );

               }

            } );

      AssemblyBean lAssemblyBean = new AssemblyBean();
      Map<PartNoKey, DataTypeKey> lCreatedDataTypeMap = lAssemblyBean.createAccumulatedParameters(
            lEngineAssembly, DataTypeKey.CYCLES, new UsageDefinitionKey( lEngineAssembly, BULK ) );

      assertThat( lCreatedDataTypeMap.size(), equalTo( 1 ) );

      for ( PartNoKey lPartNoKey : lCreatedDataTypeMap.keySet() ) {
         String lActualDescription = MimDataType
               .findByPrimaryKey( lCreatedDataTypeMap.get( lPartNoKey ) ).getDataTypeMdesc();
         assertTrue( StringUtils.isEmpty( lActualDescription ) );
      }

   }


   /**
    * Given an engine assembly with bulk definition And two part numbers on root config slot <br/>
    * And one of the part number already have an accumulated parameter associated.
    *
    * When creating the accumulated parms.
    *
    * Then verify one accumulated parm is created for the other part number.
    *
    */
   @Test
   public void itCreatesAccumulatedParmForThePartNumberHasNotHeldOne() {

      final PartNoKey lPartNoKeyOne = Domain.createPart( new DomainConfiguration<Part>() {

         @Override
         public void configure( Part aBuilder ) {
            aBuilder.setInventoryClass( RefInvClassKey.ASSY );
            aBuilder.setCode( ASSMBL_PART_NO_CODE_ONE );
            aBuilder.setManufacturer( Domain
                  .createManufacturer( new DomainConfiguration<Manufacturer>() {

                     @Override
                     public void configure( Manufacturer aBuilder ) {
                        aBuilder.setCode( MANUFACT );

                     }

                  } ) );

         }

      } );

      final PartNoKey lPartNoKeyTwo = Domain.createPart( new DomainConfiguration<Part>() {

         @Override
         public void configure( Part aBuilder ) {
            aBuilder.setInventoryClass( RefInvClassKey.ASSY );
            aBuilder.setCode( ASSMBL_PART_NO_CODE_TWO );

         }

      } );

      final AssemblyKey lEngineAssembly =
            Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

               @Override
               public void configure( EngineAssembly aBuilder ) {
                  aBuilder.addUsageDefinitionConfiguration(
                        new DomainConfiguration<UsageDefinition>() {

                           @Override
                           public void configure( UsageDefinition aBuilder ) {
                              aBuilder.setDataSource( BULK );
                              aBuilder.addAccumulatedParameterConfiguration(
                                    new DomainConfiguration<AccumulatedParameter>() {

                                       @Override
                                       public void configure( AccumulatedParameter aBuilder ) {
                                          aBuilder.setAggregatedDataType( DataTypeKey.CYCLES );
                                          aBuilder.setPartNoKey( lPartNoKeyOne );
                                       }

                                    } );
                           }

                        } );
                  aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot aBuilder ) {
                        aBuilder.addUsageParameter( DataTypeKey.CYCLES );
                        aBuilder.addPartGroup( new DomainConfiguration<PartGroup>() {

                           @Override
                           public void configure( PartGroup aBuilder ) {
                              aBuilder.addPart( lPartNoKeyOne );
                              aBuilder.addPart( lPartNoKeyTwo );

                           }

                        } );

                     }

                  } );

               }

            } );

      AssemblyBean lAssemblyBean = new AssemblyBean();
      Map<PartNoKey, DataTypeKey> lCreatedDataTypeMap = lAssemblyBean.createAccumulatedParameters(
            lEngineAssembly, DataTypeKey.CYCLES, new UsageDefinitionKey( lEngineAssembly, BULK ) );

      assertThat( lCreatedDataTypeMap.size(), equalTo( 1 ) );

      for ( PartNoKey lPartNoKey : lCreatedDataTypeMap.keySet() ) {
         assertFalse( lPartNoKey.equals( lPartNoKeyOne ) );

      }

   }


   @Before
   public void setUp() {
      Integer lCurrentUserId = SecurityIdentificationUtils.getInstance().getCurrentUserId();
      new HumanResourceDomainBuilder().withUserId( lCurrentUserId ).build();
   }

}
