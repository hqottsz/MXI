package com.mxi.mx.core.ejb.flighthist.flighthistbean;

import static com.mxi.mx.core.key.DataTypeKey.CDY;
import static com.mxi.mx.core.key.DataTypeKey.CYCLES;
import static com.mxi.mx.core.key.DataTypeKey.HOURS;
import static com.mxi.mx.core.key.DataTypeKey.LANDING;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;

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
import com.mxi.am.domain.Location;
import com.mxi.am.domain.PartGroup;
import com.mxi.am.domain.Requirement;
import com.mxi.am.domain.RequirementDefinition;
import com.mxi.am.domain.WorkPackage;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MxRuntimeException;
import com.mxi.mx.common.servlet.SessionContextFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.ejb.flighthist.CurrentUsages;
import com.mxi.mx.core.ejb.flighthist.FlightHistBean;
import com.mxi.mx.core.key.AircraftKey;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.EventInventoryKey;
import com.mxi.mx.core.key.EventInventoryUsageKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.key.RefDataSourceKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefSchedFromKey;
import com.mxi.mx.core.key.RefUsgSnapshotSrcTypeKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.key.UsageDefinitionKey;
import com.mxi.mx.core.key.UsageParmKey;
import com.mxi.mx.core.services.event.inventory.UsageSnapshot;
import com.mxi.mx.core.services.event.usage.CollectedUsageParm;
import com.mxi.mx.core.services.flighthist.FlightInformationTO;
import com.mxi.mx.core.services.flighthist.Measurement;
import com.mxi.mx.core.table.eqp.EqpAssmblBom;
import com.mxi.mx.core.table.eqp.EqpAssmblPos;
import com.mxi.mx.core.table.eqp.EqpBomPart;
import com.mxi.mx.core.table.eqp.EqpDataSourceSpec;
import com.mxi.mx.core.table.evt.EvtInvUsageTable;
import com.mxi.mx.core.table.evt.EvtSchedDeadTable;
import com.mxi.mx.core.table.inv.InvCurrUsage;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.org.OrgHr;


/*
 * Test cases for {@link FlightHistBean#createHistFlight}
 */
public class CreateHistFlightTest {

   private static final String HR_USERNAME = "HR_USERNAME";
   private static final Measurement[] NO_MEASUREMENTS = new Measurement[] {};

   private static final String FLIGHT_NAME = "TEST_CALC_PARAM_FLIGHT";
   private static final String ASSMBL_CD = "B767-200";

   private static final String ACFTS_ENGINE_PART_GROUP_CODE = "ACFTS_ENGINE_PART_GROUP_CODE";
   private static final String ACFTS_ENGINE_CONFIG_SLOT_CODE = "ACFTS_ENGINE_CONFIG_SLOT_CODE";

   private static final String CALC_USAGE_PARM_CODE = "CALC_USAGE_PARM_CODE";
   private static final String HOURS_TO_MINUTES_FUNCTION = "HOURS_TO_MINUTES_FUNCTION";
   private static final BigDecimal MINUTES_PER_HOUR = new BigDecimal( 60 );
   private static final BigDecimal ENGINE_SPECIFIC_MINUTES_PER_HOUR = new BigDecimal( 30 );
   private static final String MINUTES_PER_HOUR_CONSTANT_NAME = "MINUTES_PER_HOUR_CONSTANT_NAME";

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    *
    * Verify that when a historical flight is created that contains usage deltas for the aircraft
    * and its sub-components, that the current usage of the aircraft and its sub-components are
    * adjusted by those delta values.
    *
    * <pre>
    * Given an aircraft with usage parameters and current usage
    *   And an installed engine with usage parameters and current usage
    *  When a historic flight is created with usage deltas for the aircraft and engine
    *  Then the current usage of the aircraft and engine are adjusted by those delta values
    *
    * </pre>
    *
    */
   @Test
   public void itAdjustsInventoryCurrentUsageWhenHistoricalFlightCreated() throws Exception {

      FlightHistBean lFlightHistBean = new FlightHistBean();
      lFlightHistBean.ejbCreate();
      lFlightHistBean.setSessionContext( new SessionContextFake() );
      HumanResourceKey lHrKey = createHumanResource();

      Date lActualDepartureDate = DateUtils.addHours( new Date(), -5 );
      Date lActualArrivalDate = DateUtils.addHours( lActualDepartureDate, 2 );

      final BigDecimal lCurrentCycles = new BigDecimal( 100 );
      final BigDecimal iCurrentHours = new BigDecimal( 200 );
      final BigDecimal lCurrentCdy = new BigDecimal( 300 );
      final BigDecimal lCurrentLanding = new BigDecimal( 400 );

      final BigDecimal lCyclesDelta = new BigDecimal( 10 );
      final BigDecimal lHoursDelta = new BigDecimal( 20 );
      final BigDecimal lCdyDelta = new BigDecimal( 30 );
      final BigDecimal lLandingDelta = new BigDecimal( 40 );

      // Given an aircraft with usage parameters and current usage and an installed engine with
      // usage parameters and current usage.
      //
      // Note: Use all different data types to avoid the logic that "auto-populates"
      // sub-component's usage when the sub-component is using the same data types as the main
      // component.
      final InventoryKey lEngineInvKey = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.addUsage( CYCLES, lCurrentCycles );
            aEngine.addUsage( HOURS, iCurrentHours );
         }
      } );

      final InventoryKey lAircraftInvKey =
            Domain.createAircraft( new DomainConfiguration<Aircraft>() {

               @Override
               public void configure( Aircraft aAircraft ) {
                  aAircraft.addUsage( CDY, lCurrentCdy );
                  aAircraft.addUsage( LANDING, lCurrentLanding );
                  aAircraft.addEngine( lEngineInvKey );
               }
            } );

      // Ensure the current usage is expected prior to executing the test.
      assertCurrentUsages( lAircraftInvKey, CDY, lCurrentCdy );
      assertCurrentUsages( lAircraftInvKey, LANDING, lCurrentLanding );
      assertCurrentUsages( lEngineInvKey, CYCLES, lCurrentCycles );
      assertCurrentUsages( lEngineInvKey, HOURS, iCurrentHours );

      // When a historic flight is created with usage deltas for the aircraft and engine.
      CollectedUsageParm[] lUsageParms = { generateFlightUsage( lAircraftInvKey, CDY, lCdyDelta ),
            generateFlightUsage( lAircraftInvKey, LANDING, lLandingDelta ),
            generateFlightUsage( lEngineInvKey, CYCLES, lCyclesDelta ),
            generateFlightUsage( lEngineInvKey, HOURS, lHoursDelta ) };

      lFlightHistBean.createHistFlight( new AircraftKey( lAircraftInvKey ), lHrKey,
            generateFlightInfoTO( FLIGHT_NAME, lActualDepartureDate, lActualArrivalDate ),
            lUsageParms, NO_MEASUREMENTS );

      // Then the current usage of the aircraft and engine are adjusted by those delta values.
      final BigDecimal lExpectedCdy = lCurrentCdy.add( lCdyDelta );
      final BigDecimal lExpectedLanding = lCurrentLanding.add( lLandingDelta );
      final BigDecimal lExpectedCycles = lCurrentCycles.add( lCyclesDelta );
      final BigDecimal lExpectedHours = iCurrentHours.add( lHoursDelta );

      assertCurrentUsages( lAircraftInvKey, CDY, lExpectedCdy );
      assertCurrentUsages( lAircraftInvKey, LANDING, lExpectedLanding );
      assertCurrentUsages( lEngineInvKey, CYCLES, lExpectedCycles );
      assertCurrentUsages( lEngineInvKey, HOURS, lExpectedHours );

   }


   /**
    * Given a calculated parameter with an assigned usage parameter and a constant but does not
    * include a part specific constant <br>
    *
    * And an aircraft tracking both the calculated parameter and the assigned usage parameter<br>
    *
    * When a historical flight is created<br>
    *
    * Then the calculated parameter usage will be calculated correctly in inventory current
    * usage<br>
    *
    * @throws Exception
    */
   @Test
   public void itUpdatesCalculatedParamForInventoryCurrentUsageWhenHistoricalFlightCreated()
         throws Exception {

      // createCalculationInDatabase() method should be called prior to any data setup in database
      // as the method performs an explicit rollback followed by an implicit database commit
      createCalculationInDatabase();

      FlightHistBean lFlightHistBean = new FlightHistBean();
      lFlightHistBean.ejbCreate();
      lFlightHistBean.setSessionContext( new SessionContextFake() );

      HumanResourceKey lHrKey = createHumanResource();

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
      // When a historic flight is created with usage deltas for the aircraft
      BigDecimal lHoursDelta = new BigDecimal( 2 );
      CollectedUsageParm[] lUsageParms =
            { generateFlightUsage( lAircraftInvKey, HOURS, lHoursDelta ) };

      Date lActualArrivalDate = new Date();
      Date lActualDepartureDate = DateUtils.addHours( lActualArrivalDate, -lHoursDelta.intValue() );

      lFlightHistBean.createHistFlight( new AircraftKey( lAircraftInvKey ), lHrKey,
            generateFlightInfoTO( FLIGHT_NAME, lActualDepartureDate, lActualArrivalDate ),
            lUsageParms, NO_MEASUREMENTS );

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
    * Given a calculated parameter with an assigned usage parameter and a constant and an engine
    * part specific constant as well<br>
    *
    * And an aircraft tracking both the calculated parameter and the assigned usage parameter<br>
    *
    * And the aircraft has an attached engine that tracks the calculated parameter and the assigned
    * usage parameter and has a given part number <br>
    *
    * When a historical flight is created<br>
    *
    * Then the calculated parameter usage will be calculated correctly in inventory current usage
    * for both aircraft and engine taking into consideration part specific constant<br>
    *
    * @throws Exception
    */
   @Test
   public void
         itUpdatesCalculatedParamWithPartSpecificConstantForInventoryCurrentUsageWhenHistoricalFlightCreated()
               throws Exception {
      // Arrange
      // createCalculationInDatabase() method should be called prior to any data setup in database
      // as the method performs an explicit rollback followed by an implicit database commit
      createCalculationInDatabase();
      FlightHistBean lFlightHistBean = new FlightHistBean();
      lFlightHistBean.ejbCreate();
      lFlightHistBean.setSessionContext( new SessionContextFake() );
      HumanResourceKey lHrKey = createHumanResource();
      final BigDecimal lCurrentHoursTsn = new BigDecimal( 100 );

      // Set up an engine part
      final PartNoKey lEnginePart = new PartNoBuilder().withInventoryClass( RefInvClassKey.ASSY )
            .withStatus( RefPartStatusKey.ACTV ).build();

      // Given an aircraft; based on an aircraft assembly, tracking a usage parameter, and tracking
      // the calculated usage parameter.
      final AssemblyKey lAcftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aBuilder ) {
                  aBuilder.setCode( ASSMBL_CD );
                  aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot aRootCs ) {
                        aRootCs.setCode( "RootSlot" );
                        aRootCs.addConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aConfigSlot ) {

                              aConfigSlot.setCode( ACFTS_ENGINE_CONFIG_SLOT_CODE );
                              aConfigSlot.setConfigurationSlotClass( RefBOMClassKey.SUBASSY );
                              aConfigSlot.addPartGroup( new DomainConfiguration<PartGroup>() {

                                 @Override
                                 public void configure( PartGroup aPartGroup ) {
                                    aPartGroup.setCode( ACFTS_ENGINE_PART_GROUP_CODE );
                                    aPartGroup.setInventoryClass( RefInvClassKey.ASSY );
                                    aPartGroup.addPart( lEnginePart );
                                 }
                              } );
                           }
                        } );

                        aRootCs.addUsageParameter( DataTypeKey.HOURS );
                        aRootCs.addCalculatedUsageParameter(
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
                                    aBuilder.addPartSpecificConstant( ACFTS_ENGINE_CONFIG_SLOT_CODE,
                                          ACFTS_ENGINE_PART_GROUP_CODE, lEnginePart,
                                          MINUTES_PER_HOUR_CONSTANT_NAME,
                                          ENGINE_SPECIFIC_MINUTES_PER_HOUR );
                                 }

                              } );
                     }
                  } );
               }
            } );

      ConfigSlotKey lRootCsKey = new ConfigSlotKey( lAcftAssembly, 0 );

      final DataTypeKey lCalcUsageParm = getCalcUsageParm( lRootCsKey, CALC_USAGE_PARM_CODE );

      final PartGroupKey lEngineBomPartKey =
            EqpBomPart.getBomPartKey( lAcftAssembly, ACFTS_ENGINE_PART_GROUP_CODE );
      ConfigSlotKey lAircraftEngineConfigSlot =
            EqpAssmblBom.getBomItemKey( ASSMBL_CD, ACFTS_ENGINE_CONFIG_SLOT_CODE );
      final ConfigSlotPositionKey lEnginePosition = new ConfigSlotPositionKey(
            lAircraftEngineConfigSlot, EqpAssmblPos.getFirstPosId( lAircraftEngineConfigSlot ) );

      final InventoryKey lEngineKey = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.addUsage( HOURS, lCurrentHoursTsn );
            aEngine.addUsage( lCalcUsageParm, BigDecimal.ZERO );
            aEngine.setPartNumber( lEnginePart );
            aEngine.setPartGroup( lEngineBomPartKey );
            aEngine.setPosition( lEnginePosition );
         }
      } );

      InventoryKey lAircraftInvKey = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aBuilder ) {
            aBuilder.setAssembly( lAcftAssembly );
            aBuilder.addUsage( HOURS, lCurrentHoursTsn );
            aBuilder.addUsage( lCalcUsageParm, BigDecimal.ZERO );
            aBuilder.addEngine( lEngineKey );
         }
      } );

      // Act
      // When a historic flight is created with usage deltas for the aircraft
      BigDecimal lHoursDelta = new BigDecimal( 2 );
      CollectedUsageParm[] lUsageParms =
            { generateFlightUsage( lAircraftInvKey, HOURS, lHoursDelta ) };

      Date lActualArrivalDate = new Date();
      Date lActualDepartureDate = DateUtils.addHours( lActualArrivalDate, -lHoursDelta.intValue() );

      lFlightHistBean.createHistFlight( new AircraftKey( lAircraftInvKey ), lHrKey,
            generateFlightInfoTO( FLIGHT_NAME, lActualDepartureDate, lActualArrivalDate ),
            lUsageParms, NO_MEASUREMENTS );

      // Assert
      // Then the calculated usage parameter of the aircraft & the engine is calculated correctly
      final BigDecimal lActualCalcUsageTsn_Aircraft =
            new BigDecimal( InvCurrUsage.findTSNQtByInventory( lAircraftInvKey, lCalcUsageParm ) );
      final BigDecimal lExpectedCalcUsageTsn_Aircraft =
            lCurrentHoursTsn.add( lHoursDelta ).multiply( MINUTES_PER_HOUR );

      final BigDecimal lActualCalcUsageTsn_Engine =
            new BigDecimal( InvCurrUsage.findTSNQtByInventory( lEngineKey, lCalcUsageParm ) );
      final BigDecimal lExpectedCalcUsageTsn_Engine =
            lCurrentHoursTsn.add( lHoursDelta ).multiply( ENGINE_SPECIFIC_MINUTES_PER_HOUR );

      // dropCalculationInDatabase() method should be called after any data setup in database
      // as the method performs an explicit rollback followed by an implicit database commit
      dropCalculationInDatabase();
      Assert.assertEquals(
            "Unexpected usage for aircraft's calculated usage parameter from db funtion.",
            lExpectedCalcUsageTsn_Aircraft, lActualCalcUsageTsn_Aircraft );
      Assert.assertEquals(
            "Unexpected usage for engine's calculated usage parameter from db funtion.",
            lExpectedCalcUsageTsn_Engine, lActualCalcUsageTsn_Engine );
   }


   /**
    * Given a calculated parameter with an assigned usage parameter that does not include a part
    * specific constant <br>
    *
    * And the calculation for the calculated parameter does not exist in the database<br>
    *
    * And an aircraft tracking both the calculated parameter and the assigned usage parameter<br>
    *
    * When a historical flight is created<br>
    *
    * Then an appropriate error message is thrown<br>
    *
    * @throws Exception
    */
   @Test
   public void
         itThrowsErrorOnHistFlightCreationWhenCalcParamCalculationNotInDatabaseAndInventoryTracksCalcParam()
               throws Exception {
      // Arrange
      final String lBasicCalculationNotInDatabase = HOURS_TO_MINUTES_FUNCTION;
      FlightHistBean lFlightHistBean = new FlightHistBean();
      lFlightHistBean.ejbCreate();
      lFlightHistBean.setSessionContext( new SessionContextFake() );

      HumanResourceKey lHrKey = createHumanResource();

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
                                    aBuilder
                                          .setDatabaseCalculation( lBasicCalculationNotInDatabase );
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
      // When a historic flight is created with usage deltas for the aircraft
      BigDecimal lHoursDelta = new BigDecimal( 2 );
      CollectedUsageParm[] lUsageParms =
            { generateFlightUsage( lAircraftInvKey, HOURS, lHoursDelta ) };

      Date lActualArrivalDate = new Date();
      Date lActualDepartureDate = DateUtils.addHours( lActualArrivalDate, -lHoursDelta.intValue() );

      try {

         lFlightHistBean.createHistFlight( new AircraftKey( lAircraftInvKey ), lHrKey,
               generateFlightInfoTO( FLIGHT_NAME, lActualDepartureDate, lActualArrivalDate ),
               lUsageParms, NO_MEASUREMENTS );

         fail( "Expected MxRuntimeException as the calculation does not exist in the database" );
      }

      catch ( MxRuntimeException lException ) {

         String lExpectedException = String.format(
               "A custom calculation error occurred with inventory (%s) for usage parameter %s. Please make sure the following equation is correct: BEGIN :res := %s(%s,%s);END;",
               lAircraftInvKey, lCalcUsageParm, HOURS_TO_MINUTES_FUNCTION, MINUTES_PER_HOUR,
               lCurrentHoursTsn.add( lHoursDelta ) );
         assertEquals( lExpectedException, lException.getMessage() );
      }
   }


   /**
    * Given a calculated parameter with an assigned usage parameter that does not include a part
    * specific constant <br>
    *
    * And an aircraft tracking the calculated parameter but not the assigned usage parameter<br>
    *
    * When a historical flight is created<br>
    *
    * Then an appropriate error message is thrown<br>
    *
    * @throws Exception
    */
   @Test
   public void
         itThrowsErrorOnHistFlightCreationWhenInventoryDoesNotTrackUsageParamOnWhichCalcParamIsDependent()
               throws Exception {
      // Arrange
      // createCalculationInDatabase() method should be called prior to any data setup in database
      // as the method performs an explicit rollback followed by an implicit database commit
      createCalculationInDatabase();
      FlightHistBean lFlightHistBean = new FlightHistBean();
      lFlightHistBean.ejbCreate();
      lFlightHistBean.setSessionContext( new SessionContextFake() );

      HumanResourceKey lHrKey = createHumanResource();

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
                                    aBuilder.addParameter( DataTypeKey.CHR );
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
      // When a historic flight is created with usage deltas for the aircraft
      BigDecimal lHoursDelta = new BigDecimal( 2 );
      CollectedUsageParm[] lUsageParms =
            { generateFlightUsage( lAircraftInvKey, HOURS, lHoursDelta ) };

      Date lActualArrivalDate = new Date();
      Date lActualDepartureDate = DateUtils.addHours( lActualArrivalDate, -lHoursDelta.intValue() );

      try {

         lFlightHistBean.createHistFlight( new AircraftKey( lAircraftInvKey ), lHrKey,
               generateFlightInfoTO( FLIGHT_NAME, lActualDepartureDate, lActualArrivalDate ),
               lUsageParms, NO_MEASUREMENTS );

         // dropCalculationInDatabase() method should be called after any data setup in database
         // as the method performs an explicit rollback followed by an implicit database commit
         dropCalculationInDatabase();
         fail( "Expected MxRuntimeException as the Aircraft does not track CYCLES which are required for calculated param EQCYCLES calculation" );
      }

      catch ( MxRuntimeException lException ) {

         String lExpectedException = String.format(
               "On inventory (%s), the following datatype is required by a custom calculation (usage parameter: %s) but is not being tracked on the inventory: %s",
               lAircraftInvKey, lCalcUsageParm, "CHR" );
         assertEquals( lExpectedException, lException.getMessage() );
         dropCalculationInDatabase();
      }
   }


   /**
    * Given an aircraft collecting usages and a calculated parameter defined on it
    *
    * And the aircraft has a historical flight
    *
    * And a task with calculated parameter based usage deadline is initialized on the aircraft with
    * deadline scheduled from BIRTH
    *
    * When the historical flight is edited and its usage modified
    *
    * Then the task's deadline information is not affected
    *
    * Note: to determine if a task is initialized after the flight, the deadline start value is
    * compared to the flight usage value.
    */
   @Test
   public void itDoesNotAdjustCalcParmDeadlineOnAircraftTaskSchedFromBirth() throws Exception {

      // Important!
      // addEquationFunctionToDatabase() needs to be called prior to any data setup in database
      // as the method performs an explicit roll-back.
      createCalculationInDatabase();
      FlightHistBean lFlightHistBean = new FlightHistBean();
      lFlightHistBean.ejbCreate();
      lFlightHistBean.setSessionContext( new SessionContextFake() );
      HumanResourceKey lHrKey = createHumanResource();

      // Set up the parameter and calculated-parameter on the root config slot.
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
                                    aBuilder.addParameter( DataTypeKey.CHR );
                                 }

                              } );
                     }
                  } );
               }
            } );
      // Get the data type keys for the calculated parameters.
      final DataTypeKey lDataType_CALC_PARM_FOR_HOURS = Domain.readUsageParameter(
            Domain.readRootConfigurationSlot( lAcftAssembly ), CALC_USAGE_PARM_CODE );

      final Date lAircraftManufactureDate = DateUtils.addDays( new Date(), -10 );
      // Given an aircraft collecting usage.
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( HOURS, BigDecimal.ZERO );
            aAircraft.addUsage( lDataType_CALC_PARM_FOR_HOURS, BigDecimal.ZERO );
            aAircraft.setManufacturedDate( lAircraftManufactureDate );
         }
      } );

      // Note: The task is considered initialized after the flight when its deadline start value is
      // greater than the flight usage value.

      final BigDecimal lDeadlineStartValue = BigDecimal.ZERO;
      final BigDecimal lDeadlineInterval = BigDecimal.valueOf( 2 );
      final BigDecimal lDeadlineDueValue = lDeadlineStartValue.add( lDeadlineInterval );

      final TaskTaskKey lTaskDefnKey =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aRequirementDefinition ) {
                  aRequirementDefinition.setScheduledFromManufacturedDate();
               }

            } );

      final BigDecimal lHoursDelta = new BigDecimal( 2 );
      final Date lActualArrivalDate = DateUtils.addDays( lAircraftManufactureDate, 10 );
      final Date lActualDepartureDate =
            DateUtils.addHours( lActualArrivalDate, -lHoursDelta.intValue() );

      TaskKey lReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setInventory( lAircraft );
            aReq.setDefinition( lTaskDefnKey );
            aReq.setActualEndDate( DateUtils.addDays( lActualArrivalDate, 2 ) );
            aReq.setStatus( RefEventStatusKey.COMPLETE );
            aReq.addUsageDeadline( lDataType_CALC_PARM_FOR_HOURS, RefSchedFromKey.BIRTH,
                  lDeadlineStartValue, lDeadlineInterval, lDeadlineDueValue );
         }
      } );

      // When a historical flight is created
      CollectedUsageParm[] lUsageParms = { generateFlightUsage( lAircraft, HOURS, lHoursDelta ) };

      lFlightHistBean.createHistFlight( new AircraftKey( lAircraft ), lHrKey,
            generateFlightInfoTO( FLIGHT_NAME, lActualDepartureDate, lActualArrivalDate ),
            lUsageParms, NO_MEASUREMENTS );

      // Then the task's deadline start value and due value are recalculated based on the usage
      // delta
      Double lExpectedStartValue = lDeadlineStartValue.doubleValue();
      Double lExpectedDueValue = lDeadlineDueValue.doubleValue();

      EvtSchedDeadTable lEvtSchedDead =
            EvtSchedDeadTable.findByPrimaryKey( lReq, lDataType_CALC_PARM_FOR_HOURS );
      Double lActualStartValue = lEvtSchedDead.getStartQt();
      Double lActualDueValue = lEvtSchedDead.getDeadlineQt();

      {
         // Important!
         // All data needed for assertions must be retrieved prior to calling
         // dropEquationFunctionToDatabase(). As it performs an explicit roll-back.
         dropCalculationInDatabase();
      }

      assertEquals( "Unexpected deadline start value.", lExpectedStartValue, lActualStartValue );
      assertEquals( "Unexpected deadline due value.", lExpectedDueValue, lActualDueValue );
   }


   /**
    * <pre>
    * Given an aircraft with current usage
      And an in work package with manually added usage
      When we create a historical flight with completion date before the work package start date
      Then the usage snapshot of the work package should not be updated automatically
    * </pre>
    */
   @Test
   public void itDoesntUpdateWorkPackageUsageSnapshotSetManuallyWhenFlightBeforeWorkPackageStart()
         throws Exception {

      final AssemblyKey lAcftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aBuilder ) {
                  aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot aBuilder ) {
                        aBuilder.addUsageParameter( DataTypeKey.HOURS );
                     }
                  } );
               }
            } );
      InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( lAcftAssembly );
            aAircraft.addUsage( HOURS, BigDecimal.TEN );
         }
      } );

      Date lWorkPackageStartDate = new Date();
      BigDecimal lWorkPackageHoursUsage = BigDecimal.TEN;
      UsageSnapshot lWorkpackageUsageSnapshot =
            new UsageSnapshot( lAircraft, HOURS, lWorkPackageHoursUsage );
      lWorkpackageUsageSnapshot.withSnapshotSourceType( RefUsgSnapshotSrcTypeKey.CUSTOMER );
      final TaskKey lWorkPackage =
            Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

               @Override
               public void configure( WorkPackage aBuilder ) {
                  aBuilder.setActualStartDate( lWorkPackageStartDate );
                  aBuilder.setAircraft( lAircraft );
                  aBuilder.setStatus( RefEventStatusKey.IN_WORK );
                  aBuilder.addUsageSnapshot( lWorkpackageUsageSnapshot );
               }

            } );

      CollectedUsageParm[] lUsageParms =
            { generateFlightUsage( lAircraft, HOURS, BigDecimal.ONE ) };

      FlightHistBean lFlightHistBean = new FlightHistBean();
      lFlightHistBean.ejbCreate();
      lFlightHistBean.setSessionContext( new SessionContextFake() );
      HumanResourceKey lHrKey = Domain.createHumanResource();

      // Creating flights in the past requires MAX_FLIGHT_DAYS_IN_THE_PAST_VALUE to be set.
      int lUserId = OrgHr.findByPrimaryKey( lHrKey ).getUserId();
      UserParametersFake lFake = new UserParametersFake( lUserId, "LOGIC" );
      lFake.setInteger( "MAX_FLIGHT_DAYS_IN_THE_PAST_VALUE", 300 );
      UserParameters.setInstance( lUserId, "LOGIC", lFake );
      Date lActualDepartureDate = DateUtils.addHours( lWorkPackageStartDate, -24 );
      Date lActualArrivalDate = DateUtils.addHours( lWorkPackageStartDate, -23 );

      lFlightHistBean.createHistFlight( new AircraftKey( lAircraft ), lHrKey,
            generateFlightInfoTO( FLIGHT_NAME, lActualDepartureDate, lActualArrivalDate ),
            lUsageParms, NO_MEASUREMENTS );

      EvtInvUsageTable lEvtInvUsageTable =
            EvtInvUsageTable.findByPrimaryKey( new EventInventoryUsageKey(
                  new EventInventoryKey( lWorkPackage.getEventKey(), 1 ), HOURS ) );
      Double lActualHoursWPUsage = lEvtInvUsageTable.getTsnQt();
      Double lExpectedHoursWPUsage = lWorkPackageHoursUsage.doubleValue();
      assertEquals( "Unexpected update of workpackage usage snapshot", lExpectedHoursWPUsage,
            lActualHoursWPUsage );
   }


   /**
    * <pre>
    * Given an aircraft with current usage
      And an in work package with manually added usage
      When we create a historical flight with completion date concurrent to the work package start date
      Then the usage snapshot of the work package should not be updated automatically
    * </pre>
    */
   @Test
   public void
         itDoesntUpdateWorkPackageUsageSnapshotSetManuallyWhenFlightConcurrentToWorkPackageStart()
               throws Exception {

      final AssemblyKey lAcftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aBuilder ) {
                  aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot aBuilder ) {
                        aBuilder.addUsageParameter( DataTypeKey.HOURS );
                     }
                  } );
               }
            } );
      InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( lAcftAssembly );
            aAircraft.addUsage( HOURS, BigDecimal.TEN );
         }
      } );

      Date lWorkPackageStartDate = new Date();
      BigDecimal lWorkPackageHoursUsage = BigDecimal.TEN;
      UsageSnapshot lWorkpackageUsageSnapshot =
            new UsageSnapshot( lAircraft, HOURS, lWorkPackageHoursUsage );
      lWorkpackageUsageSnapshot.withSnapshotSourceType( RefUsgSnapshotSrcTypeKey.CUSTOMER );
      final TaskKey lWorkPackage =
            Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

               @Override
               public void configure( WorkPackage aBuilder ) {
                  aBuilder.setActualStartDate( lWorkPackageStartDate );
                  aBuilder.setAircraft( lAircraft );
                  aBuilder.setStatus( RefEventStatusKey.IN_WORK );
                  aBuilder.addUsageSnapshot( lWorkpackageUsageSnapshot );
               }

            } );

      CollectedUsageParm[] lUsageParms =
            { generateFlightUsage( lAircraft, HOURS, BigDecimal.ONE ) };

      FlightHistBean lFlightHistBean = new FlightHistBean();
      lFlightHistBean.ejbCreate();
      lFlightHistBean.setSessionContext( new SessionContextFake() );
      HumanResourceKey lHrKey = Domain.createHumanResource();

      // Creating flights in the past requires MAX_FLIGHT_DAYS_IN_THE_PAST_VALUE to be set.
      int lUserId = OrgHr.findByPrimaryKey( lHrKey ).getUserId();
      UserParametersFake lFake = new UserParametersFake( lUserId, "LOGIC" );
      lFake.setInteger( "MAX_FLIGHT_DAYS_IN_THE_PAST_VALUE", 300 );
      UserParameters.setInstance( lUserId, "LOGIC", lFake );
      Date lActualDepartureDate = DateUtils.addHours( lWorkPackageStartDate, -24 );
      Date lActualArrivalDate = lWorkPackageStartDate;

      lFlightHistBean.createHistFlight( new AircraftKey( lAircraft ), lHrKey,
            generateFlightInfoTO( FLIGHT_NAME, lActualDepartureDate, lActualArrivalDate ),
            lUsageParms, NO_MEASUREMENTS );

      EvtInvUsageTable lEvtInvUsageTable =
            EvtInvUsageTable.findByPrimaryKey( new EventInventoryUsageKey(
                  new EventInventoryKey( lWorkPackage.getEventKey(), 1 ), HOURS ) );
      Double lActualHoursWPUsage = lEvtInvUsageTable.getTsnQt();
      Double lExpectedHoursWPUsage = lWorkPackageHoursUsage.doubleValue();
      assertEquals( "Unexpected update of workpackage usage snapshot", lExpectedHoursWPUsage,
            lActualHoursWPUsage );
   }


   /**
    * <pre>
         Given a completed task with manually modified usage
         When a flight is added before the task end date
         Then the usage will not be modified for the task
    * </pre>
    */
   @Test
   public void itDoesntUpdateTaskUsageSnapshotSetManuallyWhenFlightBeforeTaskEndDate()
         throws Exception {

      final AssemblyKey lAcftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aBuilder ) {
                  aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot aBuilder ) {
                        aBuilder.addUsageParameter( DataTypeKey.HOURS );
                     }
                  } );
               }
            } );
      InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( lAcftAssembly );
            aAircraft.addUsage( HOURS, BigDecimal.TEN );
         }
      } );

      Date lTaskEndDate = new Date();

      BigDecimal lTaskUsageHours = BigDecimal.TEN;
      UsageSnapshot lTaskUsageSnapshot = new UsageSnapshot( lAircraft, HOURS, lTaskUsageHours );
      lTaskUsageSnapshot.withSnapshotSourceType( RefUsgSnapshotSrcTypeKey.CUSTOMER );
      final TaskKey lTask = Domain.createRequirement( aTask -> {
         aTask.setActualEndDate( lTaskEndDate );
         aTask.setInventory( lAircraft );
         aTask.addUsage( lTaskUsageSnapshot );
         aTask.setStatus( RefEventStatusKey.COMPLETE );
      } );

      CollectedUsageParm[] lUsageParms =
            { generateFlightUsage( lAircraft, HOURS, BigDecimal.ONE ) };

      FlightHistBean lFlightHistBean = new FlightHistBean();
      lFlightHistBean.ejbCreate();
      lFlightHistBean.setSessionContext( new SessionContextFake() );
      HumanResourceKey lHrKey = Domain.createHumanResource();

      // Creating flights in the past requires MAX_FLIGHT_DAYS_IN_THE_PAST_VALUE to be set.
      int lUserId = OrgHr.findByPrimaryKey( lHrKey ).getUserId();
      UserParametersFake lFake = new UserParametersFake( lUserId, "LOGIC" );
      lFake.setInteger( "MAX_FLIGHT_DAYS_IN_THE_PAST_VALUE", 300 );
      UserParameters.setInstance( lUserId, "LOGIC", lFake );
      Date lActualArrivalDate = DateUtils.addHours( lTaskEndDate, -5 );
      Date lActualDepartureDate = DateUtils.addHours( lActualArrivalDate, -1 );

      lFlightHistBean.createHistFlight( new AircraftKey( lAircraft ), lHrKey,
            generateFlightInfoTO( FLIGHT_NAME, lActualDepartureDate, lActualArrivalDate ),
            lUsageParms, NO_MEASUREMENTS );

      EvtInvUsageTable lEvtInvUsageTable = EvtInvUsageTable.findByPrimaryKey(
            new EventInventoryUsageKey( new EventInventoryKey( lTask.getEventKey(), 1 ), HOURS ) );
      Double lActualHoursTaskUsage = lEvtInvUsageTable.getTsnQt();
      Double lExpectedHoursTaskUsage = lTaskUsageHours.doubleValue();
      assertEquals( "Unexpected update of task usage snapshot", lExpectedHoursTaskUsage,
            lActualHoursTaskUsage );
   }


   private void assertCurrentUsages( InventoryKey aInventory, DataTypeKey aDataType,
         BigDecimal aExpectedUsage ) {

      CurrentUsages lCurrentUsage = new CurrentUsages( aInventory );
      assertEquals( "Inventory=" + aInventory + " , data type=" + aDataType + " : unexpected TSN",
            aExpectedUsage, lCurrentUsage.getTsn( aDataType ) );
      assertEquals( "Inventory=" + aInventory + " , data type=" + aDataType + " : unexpected TSO",
            aExpectedUsage, lCurrentUsage.getTso( aDataType ) );
      assertEquals( "Inventory=" + aInventory + " , data type=" + aDataType + " : unexpected TSI",
            aExpectedUsage, lCurrentUsage.getTsi( aDataType ) );

   }


   private CollectedUsageParm generateFlightUsage( InventoryKey aInventoryKey,
         DataTypeKey lDataType, BigDecimal lDelta ) {

      // Create a usage collection to be returned.
      CollectedUsageParm lUsageParm = new CollectedUsageParm(
            new UsageParmKey( aInventoryKey, lDataType ), lDelta.doubleValue() );

      // Create flight data source specifications.
      EqpDataSourceSpec
            .create( new UsageDefinitionKey( InvInvTable.getAssemblyByInventoryKey( aInventoryKey ),
                  RefDataSourceKey.MXFL ), lDataType );

      return lUsageParm;
   }


   private FlightInformationTO generateFlightInfoTO( String aFlightName, Date aActualDepartureDate,
         Date aActualArrivalDate ) {
      LocationKey lDepartureAirport = Domain.createLocation( new DomainConfiguration<Location>() {

         @Override
         public void configure( Location aLocation ) {
            aLocation.setType( RefLocTypeKey.AIRPORT );
         }
      } );
      LocationKey lArrivalAirport = Domain.createLocation( new DomainConfiguration<Location>() {

         @Override
         public void configure( Location aLocation ) {
            aLocation.setType( RefLocTypeKey.AIRPORT );
         }
      } );

      return new FlightInformationTO( aFlightName.toString(), null, null, null, null, null,
            lDepartureAirport, lArrivalAirport, null, null, null, null, aActualDepartureDate,
            aActualArrivalDate, null, null, false, false );
   }


   private HumanResourceKey createHumanResource() {
      HumanResourceKey lHrKey =
            new HumanResourceDomainBuilder().withUsername( HR_USERNAME ).build();

      int lUserId = OrgHr.findByPrimaryKey( lHrKey ).getUserId();
      UserParameters.setInstance( lUserId, "LOGIC", new UserParametersFake( lUserId, "LOGIC" ) );
      return lHrKey;
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
