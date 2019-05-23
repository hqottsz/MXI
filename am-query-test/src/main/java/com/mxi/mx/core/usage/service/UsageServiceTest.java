package com.mxi.mx.core.usage.service;

import static com.mxi.mx.core.key.DataTypeKey.HOURS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.AccumulatedParameter;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.AircraftAssembly;
import com.mxi.am.domain.CalculatedUsageParameter;
import com.mxi.am.domain.ConfigurationSlot;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Engine;
import com.mxi.am.domain.EngineAssembly;
import com.mxi.am.domain.PartRequirement;
import com.mxi.am.domain.Requirement;
import com.mxi.am.domain.UsageDefinition;
import com.mxi.am.domain.WorkPackage;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.key.RefUsgSnapshotSrcTypeKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.UsageParmKey;
import com.mxi.mx.core.services.event.inventory.UsageSnapshot;
import com.mxi.mx.core.services.usgcor.corusgparm.CorrectedUsageParm;
import com.mxi.mx.core.table.inv.InvCurrUsage;


/**
 * Test cases for {@link UsageService}
 *
 */
public class UsageServiceTest {

   private static final String EDIT_INVENTORY = "TEST_CALC_PARAM_EDIT_INVENTORY_USAGE";
   private static final String CALC_USAGE_PARM_CODE = "CALC_USAGE_PARM_CODE";
   private static final String HOURS_TO_MINUTES_FUNCTION = "HOURS_TO_MINUTES_FUNCTION";
   private static final HumanResourceKey HR = new HumanResourceKey( 1, 1 );
   private static final BigDecimal MINUTES_PER_HOUR = new BigDecimal( 60 );
   private static final String MINUTES_PER_HOUR_CONSTANT_NAME = "MINUTES_PER_HOUR_CONSTANT_NAME";

   private static final BigDecimal EVENT_USAGE = new BigDecimal( 10 );
   private static final BigDecimal FLIGHT_USAGE = new BigDecimal( 5 );
   private static final BigDecimal CURRENT_USAGE = EVENT_USAGE.add( FLIGHT_USAGE );
   private static final Date FLIGHT_END_DATE = new Date();
   private static final Date WORK_PACKAGE_START_DATE = DateUtils.addDays( FLIGHT_END_DATE, -2 );
   private static final Date WORK_PACKAGE_END_DATE = DateUtils.addDays( FLIGHT_END_DATE, -1 );
   private static final String ACC_PARM_CODE = "ACC_PARM_CODE";
   private static final String INSTALLED_ENGINE = "INSTALLED_ENGINE";

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Given a calculated parameter with an assigned usage parameter that does not include a part
    * specific constant <br>
    *
    * And an aircraft tracking both the calculated parameter and the assigned usage parameter<br>
    *
    * When a the aircraft's current usage parameters are updated using Edit inventory usage<br>
    *
    * Then the calculated parameter usage will be calculated correctly in inventory current
    * usage<br>
    *
    * @throws Exception
    */
   @Test
   public void itUpdatesCalculatedParamForInventoryCurrentUsageWhenEditInventoryCurrentUsage()
         throws Exception {
      // Arrange
      // createCalculationInDatabase() method should be called prior to any data setup in database
      // as the method performs an explicit rollback followed by an implicit database commit
      createCalculationInDatabase();
      UsageService lUsageService = new UsageService();

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
      Date lEditInventoryUsageDate = new Date();
      BigDecimal lHoursDelta = new BigDecimal( 2 );
      // Create the corrected usage parameter object
      CorrectedUsageParm[] lCorrectedUsageParm = {
            generateCorrectedUsageParms( lAircraftInvKey, HOURS, lHoursDelta, lCurrentHoursTsn ) };

      lUsageService.correctUsage( lAircraftInvKey, EDIT_INVENTORY, lEditInventoryUsageDate, HR,
            null, "Edit Inventory Description", lCorrectedUsageParm );

      // Assert
      // Then the current usage of the aircraft and engine are adjusted by those delta values.
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
    *
    * Description: This test case is testing when a back dated engine installation happened, the
    * normal usage, acc usage and acc calculated usage are update correctly.
    *
    * <pre>
    * Given an engine with a HOURS usage parm, a HOURS based acc parm and a HOURS based acc calculated parm
    * And a back date engine installation of this engine.
    *
    * When adjust the current usage of this engine.
    *
    * Then verify the new current HOURS, the HOURS based acc parm and the HOURS based acc calculated parm are updated
    * according to difference between the aircraft current usage and installation task usage snap shot.
    * </pre>
    */
   @Test
   public void itAdjustsACCParmAndACCCalculatedParmWhenCreateBackDatedEngineInstallation()
         throws Exception {

      createCalculationInDatabase();

      final PartNoKey lPartNoKey = Domain.createPart();

      final AssemblyKey lEngineAssembly =
            Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

               @Override
               public void configure( EngineAssembly aEngineAssembly ) {
                  aEngineAssembly.addUsageDefinitionConfiguration(
                        new DomainConfiguration<UsageDefinition>() {

                           @Override
                           public void configure( UsageDefinition aUsageDefinition ) {
                              aUsageDefinition.addAccumulatedParameterConfiguration(
                                    new DomainConfiguration<AccumulatedParameter>() {

                                       @Override
                                       public void configure(
                                             AccumulatedParameter aAccumulatedParameter ) {
                                          aAccumulatedParameter.setAggregatedDataType( HOURS );
                                          aAccumulatedParameter.setPartNoKey( lPartNoKey );
                                          aAccumulatedParameter.setCode( ACC_PARM_CODE );
                                       }

                                    } );

                           }

                        } );
                  aEngineAssembly
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aRootConfigSlot ) {
                              aRootConfigSlot.addCalculatedUsageParameter(
                                    new DomainConfiguration<CalculatedUsageParameter>() {

                                       @Override
                                       public void configure(
                                             CalculatedUsageParameter aCalculatedUsageParameter ) {
                                          aCalculatedUsageParameter.setCode( CALC_USAGE_PARM_CODE );
                                          aCalculatedUsageParameter.setDatabaseCalculation(
                                                HOURS_TO_MINUTES_FUNCTION );
                                          aCalculatedUsageParameter.setPrecisionQt( 2 );
                                          aCalculatedUsageParameter.addConstant(
                                                MINUTES_PER_HOUR_CONSTANT_NAME, MINUTES_PER_HOUR );
                                          aCalculatedUsageParameter
                                                .addParameter( DataTypeKey.HOURS );

                                       }

                                    } );
                           }

                        } );

               }

            } );

      final DataTypeKey lAccDataTypeKey =
            Domain.readUsageParameter( new ConfigSlotKey( lEngineAssembly, 0 ), ACC_PARM_CODE );

      final DataTypeKey lCalcUsageParm = Domain
            .readUsageParameter( new ConfigSlotKey( lEngineAssembly, 0 ), CALC_USAGE_PARM_CODE );

      final InventoryKey lInstalledEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {

            aEngine.setAssembly( lEngineAssembly );
            aEngine.setPartNumber( lPartNoKey );
            aEngine.addUsage( HOURS, BigDecimal.ZERO );
            aEngine.addUsage( lAccDataTypeKey, BigDecimal.ZERO );
            aEngine.addUsage( lCalcUsageParm, BigDecimal.ZERO );
            aEngine.setDescription( INSTALLED_ENGINE );
         }

      } );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( HOURS, CURRENT_USAGE );
            aAircraft.addEngine( lInstalledEngine );
         }

      } );

      final TaskKey lInstallEngineTaslk =
            createRequirementWithPartInstallRequest( lAircraft, lPartNoKey, lInstalledEngine );

      final TaskKey lWorkPackage =
            Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

               @Override
               public void configure( WorkPackage aWorkPackage ) {
                  aWorkPackage.setActualStartDate( WORK_PACKAGE_START_DATE );
                  aWorkPackage.setActualEndDate( WORK_PACKAGE_END_DATE );
                  aWorkPackage.addTask( lInstallEngineTaslk );
                  aWorkPackage.addUsageSnapshot( lAircraft, HOURS, EVENT_USAGE );
               }

            } );

      final EventKey lEventKey = new EventKey( lWorkPackage.getDbId(), lWorkPackage.getId() );

      UsageService lUsageService = new UsageService();
      lUsageService.adjustUsageForInstallation( lEventKey, lAircraft, lInstalledEngine, HR );

      InvCurrUsage[] lInvCurrUsages = InvCurrUsage.findByInventory( lInstalledEngine );

      BigDecimal lAccTsn = null;
      BigDecimal lHourTsn = null;
      BigDecimal lCalcTsn = null;

      for ( InvCurrUsage lInvCurrUsage : lInvCurrUsages ) {
         if ( lInvCurrUsage.getDataType().equals( lAccDataTypeKey ) ) {
            lAccTsn = new BigDecimal( lInvCurrUsage.getTsnQt() );
         }
         if ( lInvCurrUsage.getDataType().equals( HOURS ) ) {
            lHourTsn = new BigDecimal( lInvCurrUsage.getTsnQt() );

         }

         if ( lInvCurrUsage.getDataType().equals( lCalcUsageParm ) ) {
            lCalcTsn = new BigDecimal( lInvCurrUsage.getTsnQt() );

         }
      }
      dropCalculationInDatabase();

      assertEquals( 3, lInvCurrUsages.length );
      assertEquals( new BigDecimal( 5 ), lAccTsn );
      assertEquals( new BigDecimal( 5 ), lHourTsn );
      assertEquals( new BigDecimal( 300 ), lCalcTsn );

   }


   /**
    *
    * Description: This test case is testing when a back dated engine removal happened, the normal
    * usage, acc usage and acc calculated usage are update correctly.
    *
    * <pre>
    * Given an engine with a HOURS usage parm, a HOURS based acc parm and a HOURS based acc calculated parm.
    * And a back date engine installation of this engine.
    *
    * When adjust the current usage of this engine.
    *
    * Then verify the new current HOURS, the HOURS based acc parm and the HOURS based acc calculated parm are updated
    * according to difference between the engine current usage and removal task usage snap shot.
    * </pre>
    */
   @Test
   public void itAdjustsACCParmAndACCCalculatedParmWhenCreateBackDatedEngineRemoval()
         throws Exception {

      createCalculationInDatabase();

      final PartNoKey lPartNoKey = Domain.createPart();

      final AssemblyKey lEngineAssembly =
            Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

               @Override
               public void configure( EngineAssembly aEngineAssembly ) {
                  aEngineAssembly.addUsageDefinitionConfiguration(
                        new DomainConfiguration<UsageDefinition>() {

                           @Override
                           public void configure( UsageDefinition aUsageDefinition ) {
                              aUsageDefinition.addAccumulatedParameterConfiguration(
                                    new DomainConfiguration<AccumulatedParameter>() {

                                       @Override
                                       public void configure(
                                             AccumulatedParameter aAccumulatedParameter ) {
                                          aAccumulatedParameter.setAggregatedDataType( HOURS );
                                          aAccumulatedParameter.setPartNoKey( lPartNoKey );
                                          aAccumulatedParameter.setCode( ACC_PARM_CODE );
                                       }

                                    } );

                           }

                        } );
                  aEngineAssembly
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aRootConfigSlot ) {
                              aRootConfigSlot.setConfigurationSlotClass( RefBOMClassKey.ROOT );
                              aRootConfigSlot.addCalculatedUsageParameter(
                                    new DomainConfiguration<CalculatedUsageParameter>() {

                                       @Override
                                       public void configure(
                                             CalculatedUsageParameter aCalculatedUsageParameter ) {
                                          aCalculatedUsageParameter.setCode( CALC_USAGE_PARM_CODE );
                                          aCalculatedUsageParameter.setDatabaseCalculation(
                                                HOURS_TO_MINUTES_FUNCTION );
                                          aCalculatedUsageParameter.setPrecisionQt( 2 );
                                          aCalculatedUsageParameter.addConstant(
                                                MINUTES_PER_HOUR_CONSTANT_NAME, MINUTES_PER_HOUR );
                                          aCalculatedUsageParameter
                                                .addParameter( DataTypeKey.HOURS );

                                       }

                                    } );
                           }

                        } );

               }

            } );

      final DataTypeKey lAccDataTypeKey =
            Domain.readUsageParameter( new ConfigSlotKey( lEngineAssembly, 0 ), ACC_PARM_CODE );

      final DataTypeKey lCalcUsageParm = Domain
            .readUsageParameter( new ConfigSlotKey( lEngineAssembly, 0 ), CALC_USAGE_PARM_CODE );

      final InventoryKey lRemovedEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {

            aEngine.setAssembly( lEngineAssembly );
            aEngine.setPartNumber( lPartNoKey );
            aEngine.addUsage( HOURS, CURRENT_USAGE );
            aEngine.addUsage( lAccDataTypeKey, CURRENT_USAGE );
            aEngine.addUsage( lCalcUsageParm, CURRENT_USAGE.multiply( MINUTES_PER_HOUR ) );
            aEngine.setDescription( INSTALLED_ENGINE );

         }

      } );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( HOURS, CURRENT_USAGE );
            aAircraft.addEngine( lRemovedEngine );
         }

      } );

      final TaskKey lRemoveEngineTask =
            createRequirementWithPartRemovalRequest( lAircraft, lPartNoKey, lRemovedEngine );

      final TaskKey lWorkPackage =
            Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

               @Override
               public void configure( WorkPackage aWorkPackage ) {
                  aWorkPackage.setActualStartDate( WORK_PACKAGE_START_DATE );
                  aWorkPackage.setActualEndDate( WORK_PACKAGE_END_DATE );
                  aWorkPackage.addTask( lRemoveEngineTask );
                  aWorkPackage.addUsageSnapshot( lRemovedEngine, HOURS, EVENT_USAGE );
                  aWorkPackage.addUsageSnapshot( lRemovedEngine, lAccDataTypeKey, EVENT_USAGE );
                  aWorkPackage.addUsageSnapshot( lRemovedEngine, lCalcUsageParm,
                        EVENT_USAGE.multiply( MINUTES_PER_HOUR ) );
               }

            } );

      final EventKey lEventKey = new EventKey( lWorkPackage.getDbId(), lWorkPackage.getId() );

      UsageService lUsageService = new UsageService();
      lUsageService.adjustUsageForRemoval( lEventKey, lRemovedEngine, lRemovedEngine, HR );

      InvCurrUsage[] lInvCurrUsages = InvCurrUsage.findByInventory( lRemovedEngine );

      assertEquals( 3, lInvCurrUsages.length );

      BigDecimal lAccTsn = null;
      BigDecimal lHourTsn = null;
      BigDecimal lCalcTsn = null;

      for ( InvCurrUsage lInvCurrUsage : lInvCurrUsages ) {
         if ( lInvCurrUsage.getDataType().equals( lAccDataTypeKey ) ) {
            lAccTsn = new BigDecimal( lInvCurrUsage.getTsnQt() );
         }
         if ( lInvCurrUsage.getDataType().equals( HOURS ) ) {
            lHourTsn = new BigDecimal( lInvCurrUsage.getTsnQt() );

         }

         if ( lInvCurrUsage.getDataType().equals( lCalcUsageParm ) ) {
            lCalcTsn = new BigDecimal( lInvCurrUsage.getTsnQt() );

         }
      }

      dropCalculationInDatabase();

      assertEquals( 3, lInvCurrUsages.length );
      assertEquals( new BigDecimal( 10 ), lAccTsn );
      assertEquals( new BigDecimal( 10 ), lHourTsn );
      assertEquals( new BigDecimal( 600 ), lCalcTsn );

   }


   /**
    * This test case is testing if the source code of event inventory usage is CUSTOMER, the method
    * will return true.
    *
    * Given a requirement with usage snapshot. And the source code of the usage snapshot is
    * CUSTOMER.
    *
    * When call the method upon the event.
    *
    * Then it will return true.
    *
    */
   @Test
   public void itReturnsTrueWhenUsageSourceIsCustomer() {
      UsageService usageService = new UsageService();
      InventoryKey aircraft = Domain.createAircraft();
      UsageSnapshot usageSnapShot = new UsageSnapshot( aircraft, DataTypeKey.HOURS, 10 )
            .withSnapshotSourceType( RefUsgSnapshotSrcTypeKey.CUSTOMER );
      TaskKey requirement = Domain.createRequirement( req -> {
         req.addUsage( usageSnapShot );
         req.setInventory( aircraft );
      } );

      assertTrue( "It doesn't return manually enter flag properly for the event inventory usage.",
            usageService.isUsageModifiedManually( requirement.getEventKey() ) );
   }


   /**
    * This test case is testing if the usage snapshot does not exist for the event, the method will
    * return false.
    *
    * Given a requirement with no usage snapshot.
    *
    * When call the method upon thevent.
    *
    * Then it will return false.
    *
    */
   @Test
   public void itReturnsFalseWhenUsagesnapshotDoesNotExist() {
      UsageService usageService = new UsageService();
      InventoryKey aircraft = Domain.createAircraft();
      TaskKey requirement = Domain.createRequirement( req -> {
         req.setInventory( aircraft );
      } );

      assertFalse( "It doesn't return false if the usage snapshot does not exist",
            usageService.isUsageModifiedManually( requirement.getEventKey() ) );
   }


   /**
    * This test case is testing if the source code of event inventory usage is MAINTENIX, the method
    * will return false.
    *
    * Given a requirement with usage snapshot. And the source code of the usage snapshot is
    * MAINTENIX.
    *
    * When call the method upon the event.
    *
    * Then it will return false.
    *
    */
   @Test
   public void itReturnsFalseWhenUsageSourceIsCustomer() {
      UsageService usageService = new UsageService();
      InventoryKey aircraft = Domain.createAircraft();
      UsageSnapshot usageSnapShot = new UsageSnapshot( aircraft, DataTypeKey.HOURS, 10 )
            .withSnapshotSourceType( RefUsgSnapshotSrcTypeKey.MAINTENIX );
      TaskKey requirement = Domain.createRequirement( req -> {
         req.addUsage( usageSnapShot );
         req.setInventory( aircraft );
      } );

      assertFalse( "It doesn't return manually enter flag properly for the event inventory usage.",
            usageService.isUsageModifiedManually( requirement.getEventKey() ) );
   }


   private CorrectedUsageParm generateCorrectedUsageParms( InventoryKey aInventoryKey,
         DataTypeKey aDataType, BigDecimal aDelta, BigDecimal aCurrentUsage ) {
      UsageParmKey lUsageParm = new UsageParmKey( aInventoryKey, aDataType );
      double lUpdatedUsage = aCurrentUsage.add( aDelta ).doubleValue();
      CorrectedUsageParm lCorrectedUsageParm =
            new CorrectedUsageParm( lUsageParm, lUpdatedUsage, lUpdatedUsage, lUpdatedUsage );
      return lCorrectedUsageParm;

   }


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


   private TaskKey createRequirementWithPartInstallRequest( final InventoryKey aAircraft,
         final PartNoKey aEngPart, final InventoryKey aInstalledEngine ) {
      return Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aBuilder ) {
            aBuilder.setInventory( aAircraft );
            aBuilder.addPartRequirement( new DomainConfiguration<PartRequirement>() {

               @Override
               public void configure( PartRequirement aBuilder ) {
                  aBuilder.setPartInstallRequest( aBuilder.new PartInstallRequest()
                        .withInventory( aInstalledEngine ).withPartNo( aEngPart ) );

               }

            } );
         }

      } );
   }


   private TaskKey createRequirementWithPartRemovalRequest( final InventoryKey aAircraft,
         final PartNoKey aEngPart, final InventoryKey aRemovedEngine ) {
      return Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aBuilder ) {
            aBuilder.setInventory( aAircraft );
            aBuilder.addPartRequirement( new DomainConfiguration<PartRequirement>() {

               @Override
               public void configure( PartRequirement aBuilder ) {
                  aBuilder.setPartRemovalRequest( aBuilder.new PartRemovalRequest()
                        .withInventory( aRemovedEngine ).withPartNo( aEngPart ) );

               }

            } );
         }

      } );
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


   private void dropCalculationInDatabase() throws SQLException {
      // Function creation is DDL which implicitly commits transaction
      // We perform explicit rollback before function creation ensuring no data gets committed
      // accidentally
      Domain.dropCalculatedParameterEquationFunction( iDatabaseConnectionRule.getConnection(),
            HOURS_TO_MINUTES_FUNCTION );
   }
}
