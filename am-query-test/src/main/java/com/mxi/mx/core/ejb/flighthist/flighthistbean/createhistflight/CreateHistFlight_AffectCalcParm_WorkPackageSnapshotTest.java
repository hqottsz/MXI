package com.mxi.mx.core.ejb.flighthist.flighthistbean.createhistflight;

import static com.mxi.mx.core.key.DataTypeKey.HOURS;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.AircraftAssembly;
import com.mxi.am.domain.CalculatedUsageParameter;
import com.mxi.am.domain.ComponentWorkPackage;
import com.mxi.am.domain.ConfigurationSlot;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.InstallationRecord;
import com.mxi.am.domain.Location;
import com.mxi.am.domain.RemovalRecord;
import com.mxi.am.domain.TrackedInventory;
import com.mxi.am.domain.UsageDefinition;
import com.mxi.am.domain.WorkPackage;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.servlet.SessionContextFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.ejb.flighthist.FlightHistBean;
import com.mxi.mx.core.key.AircraftKey;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.EventInventoryKey;
import com.mxi.mx.core.key.EventInventoryUsageKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefDataSourceKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefUsgSnapshotSrcTypeKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.UsageDefinitionKey;
import com.mxi.mx.core.key.UsageParmKey;
import com.mxi.mx.core.services.event.inventory.UsageSnapshot;
import com.mxi.mx.core.services.event.usage.CollectedUsageParm;
import com.mxi.mx.core.services.flighthist.FlightInformationTO;
import com.mxi.mx.core.services.flighthist.Measurement;
import com.mxi.mx.core.table.eqp.EqpAssmblPos;
import com.mxi.mx.core.table.eqp.EqpDataSourceSpec;
import com.mxi.mx.core.table.evt.EvtInvUsageTable;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.org.OrgHr;


/**
 * Tests for {@linkplain FlightHistBean#createHistFlight} that involve updating calculated
 * parameters within usage snapshots for in work work package usage snap shot
 */
public class CreateHistFlight_AffectCalcParm_WorkPackageSnapshotTest {

   private static final String HR_USERNAME = "HR_USERNAME";
   private static final String FLIGHT_NAME = "FLIGHT_NAME";
   private static final String TRK_CONFIG_SLOT_CODE = "TRK_CS";

   // Calculated parameter data types, equation, and constant.
   // The equation is simply a multiplier of the target usage parameter by the multiplier constant.
   private static final String MUTIPLIER_DATA_TYPE_FOR_HOURS = "MUTIPLIER_DATA_TYPE_FOR_HOURS";
   private static final String MULTIPLIER_FUNCTION_NAME = "MULTIPLIER_FUNCTION_NAME";
   private static final String MULTIPLIER_CONSTANT_NAME = "MULTIPLIER_CONSTANT_NAME";
   private static final BigDecimal MULTIPLIER_CONSTANT = BigDecimal.valueOf( 2.0 );

   private static final BigDecimal WP_START_HOURS = BigDecimal.valueOf( 7.8 );

   private static final BigDecimal FLIGHT_HOURS = BigDecimal.valueOf( 10.11 );

   private static final Date CURRENT_DATE = new Date();

   private static final Date FLIGHT_DATE = DateUtils.addDays( CURRENT_DATE, -2 );

   private static final Date ATTACHED_DATE = DateUtils.addDays( CURRENT_DATE, -3 );

   private static final Date DETACHED_DATE = DateUtils.addDays( CURRENT_DATE, -1 );

   private static final Measurement[] NO_MEASUREMENTS = new Measurement[] {};

   private FlightHistBean iFlightHistBean;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   //
   // Given an aircraft tracking a usage parameter and a calculated parameter on its root config
   // slot.
   //
   // And the calculated parameter's equation uses the usage parameter.
   // And there is a IN WORK work package on the aircraft


   // When a historical flight with an actual arrival date before the work package start date is
   // created
   // Then it will re-calculate the calculate parameters on the work package usage snap shot
   @Test
   public void itRecalculatesCalcParamWhenWorkPackageOnAircraftStartedAfterFlight()
         throws Exception {
      addEquationFunctionToDatabase();

      final AssemblyKey lAcftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aBuilder ) {
                  aBuilder.addUsageDefinitionConfiguration(
                        new DomainConfiguration<UsageDefinition>() {

                           @Override
                           public void configure( UsageDefinition aUsageDefinition ) {
                              aUsageDefinition.addUsageParameter( HOURS );
                              aUsageDefinition.setDataSource( RefDataSourceKey.MXFL );

                           }

                        } );
                  aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot aBuilder ) {
                        // Track a parameter.
                        aBuilder.addUsageParameter( HOURS );
                        aBuilder.addCalculatedUsageParameter(
                              new DomainConfiguration<CalculatedUsageParameter>() {

                                 @Override
                                 public void configure( CalculatedUsageParameter aBuilder ) {
                                    aBuilder.setCode( MUTIPLIER_DATA_TYPE_FOR_HOURS );
                                    aBuilder.setDatabaseCalculation( MULTIPLIER_FUNCTION_NAME );
                                    aBuilder.setPrecisionQt( 2 );

                                    // Add the constant and parameter in the order in which the
                                    // function is expecting them.
                                    aBuilder.addConstant( MULTIPLIER_CONSTANT_NAME,
                                          MULTIPLIER_CONSTANT );
                                    aBuilder.addParameter( HOURS );
                                 }

                              } );
                     }
                  } );
               }
            } );
      final DataTypeKey lCalcParmKey = Domain.readUsageParameter(
            Domain.readRootConfigurationSlot( lAcftAssembly ), MUTIPLIER_DATA_TYPE_FOR_HOURS );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aBuilder ) {
            aBuilder.setAssembly( lAcftAssembly );
            aBuilder.addUsage( HOURS, FLIGHT_HOURS );
         }
      } );

      final UsageSnapshot lUsageSnapShotForHours =
            new UsageSnapshot( lAircraft, HOURS, WP_START_HOURS );

      final TaskKey lWorkPackage =
            Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

               @Override
               public void configure( WorkPackage aBuilder ) {
                  aBuilder.setActualStartDate( CURRENT_DATE );
                  aBuilder.setAircraft( lAircraft );
                  aBuilder.setStatus( RefEventStatusKey.IN_WORK );
                  aBuilder.addUsageSnapshot( lUsageSnapShotForHours );
                  aBuilder.addUsageSnapshot(
                        new UsageSnapshot( lAircraft, lCalcParmKey, WP_START_HOURS ) );
               }

            } );

      FlightInformationTO lFlightInfoTO =
            generateFlightInfoTO( FLIGHT_NAME, FLIGHT_DATE, FLIGHT_DATE );
      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( lAircraft, HOURS, FLIGHT_HOURS ) };

      iFlightHistBean.createHistFlight( new AircraftKey( lAircraft ), createHumanResource(),
            lFlightInfoTO, lEditUsageParms, NO_MEASUREMENTS );

      BigDecimal lExpected = WP_START_HOURS.add( FLIGHT_HOURS ).multiply( MULTIPLIER_CONSTANT );

      BigDecimal lAcutual = readActualUsageParmValue( lWorkPackage.getEventKey(), lAcftAssembly );

      {
         // Important!
         // All data needed for assertions must be retrieved prior to calling
         // dropEquationFunctionToDatabase(). As it performs an explicit roll-back.
         //
         // ALSO, all assertions must be done after dropEquationFunctionToDatabase() to ensure the
         // equation gets removed from the DB.
         dropEquationFunctionToDatabase();
      }

      Assert.assertTrue( "Unexpected value for " + MUTIPLIER_DATA_TYPE_FOR_HOURS + "; expected="
            + lExpected + " , actual=" + lAcutual, lExpected.compareTo( lAcutual ) == 0 );

   }


   //
   // Given an aircraft tracking a usage parameter and a calculated parameter on its root config
   // slot.
   // And the calculated parameter's equation uses the usage parameter.
   // And there is a IN WORK work package on the aircraft with 'CUSTOMER' manually added usage for
   // usage snapshot
   // When a historical flight with an actual arrival date before the work package start date is
   // created
   // Then it will not re-calculate the calculate parameters on the work package usage snapshot
   @Test
   public void
         itDoesntRecalculateCalcParamWhenWorkPackageUsageSnapshotEnteredManuallyStartedAfterFlight()
               throws Exception {
      addEquationFunctionToDatabase();

      final AssemblyKey lAcftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aBuilder ) {
                  aBuilder.addUsageDefinitionConfiguration(
                        new DomainConfiguration<UsageDefinition>() {

                           @Override
                           public void configure( UsageDefinition aUsageDefinition ) {
                              aUsageDefinition.addUsageParameter( HOURS );
                              aUsageDefinition.setDataSource( RefDataSourceKey.MXFL );

                           }

                        } );
                  aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot aBuilder ) {
                        // Track a parameter.
                        aBuilder.addUsageParameter( HOURS );
                        aBuilder.addCalculatedUsageParameter(
                              new DomainConfiguration<CalculatedUsageParameter>() {

                                 @Override
                                 public void configure( CalculatedUsageParameter aBuilder ) {
                                    aBuilder.setCode( MUTIPLIER_DATA_TYPE_FOR_HOURS );
                                    aBuilder.setDatabaseCalculation( MULTIPLIER_FUNCTION_NAME );
                                    aBuilder.setPrecisionQt( 2 );

                                    // Add the constant and parameter in the order in which the
                                    // function is expecting them.
                                    aBuilder.addConstant( MULTIPLIER_CONSTANT_NAME,
                                          MULTIPLIER_CONSTANT );
                                    aBuilder.addParameter( HOURS );
                                 }

                              } );
                     }
                  } );
               }
            } );
      final DataTypeKey lCalcParmKey = Domain.readUsageParameter(
            Domain.readRootConfigurationSlot( lAcftAssembly ), MUTIPLIER_DATA_TYPE_FOR_HOURS );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aBuilder ) {
            aBuilder.setAssembly( lAcftAssembly );
            aBuilder.addUsage( HOURS, FLIGHT_HOURS );
         }
      } );

      final UsageSnapshot lUsageSnapShotForHours =
            new UsageSnapshot( lAircraft, HOURS, WP_START_HOURS );
      final UsageSnapshot lWorkPackageCalcParmUsageSnapshot =
            new UsageSnapshot( lAircraft, lCalcParmKey, WP_START_HOURS );
      lWorkPackageCalcParmUsageSnapshot.withSnapshotSourceType( RefUsgSnapshotSrcTypeKey.CUSTOMER );

      final TaskKey lWorkPackage =
            Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

               @Override
               public void configure( WorkPackage aBuilder ) {
                  aBuilder.setActualStartDate( CURRENT_DATE );
                  aBuilder.setAircraft( lAircraft );
                  aBuilder.setStatus( RefEventStatusKey.IN_WORK );
                  aBuilder.addUsageSnapshot( lUsageSnapShotForHours );
                  aBuilder.addUsageSnapshot( lWorkPackageCalcParmUsageSnapshot );
               }

            } );

      FlightInformationTO lFlightInfoTO =
            generateFlightInfoTO( FLIGHT_NAME, FLIGHT_DATE, FLIGHT_DATE );
      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( lAircraft, HOURS, FLIGHT_HOURS ) };

      iFlightHistBean.createHistFlight( new AircraftKey( lAircraft ), createHumanResource(),
            lFlightInfoTO, lEditUsageParms, NO_MEASUREMENTS );

      BigDecimal lExpected = WP_START_HOURS;

      BigDecimal lAcutual = readActualUsageParmValue( lWorkPackage.getEventKey(), lAcftAssembly );

      {
         // Important!
         // All data needed for assertions must be retrieved prior to calling
         // dropEquationFunctionToDatabase(). As it performs an explicit roll-back.
         //
         // ALSO, all assertions must be done after dropEquationFunctionToDatabase() to ensure the
         // equation gets removed from the DB.
         dropEquationFunctionToDatabase();
      }

      Assert.assertTrue( "Unexpected update for " + MUTIPLIER_DATA_TYPE_FOR_HOURS + "; expected="
            + lExpected + " , actual=" + lAcutual, lExpected.compareTo( lAcutual ) == 0 );

   }

   //
   // Given an aircraft tracking a usage parameter and a calculated parameter on its root config
   // slot.
   //
   // And the calculated parameter's equation uses the usage parameter.
   // And there is a loose component detached from the aircraft
   // And there is a IN WORK component work package on the component after the removal


   // When a historical flight with an actual arrival date before the component removal is created
   // Then it will re-calculate the calculate parameters on the work package usage snap shot
   @Test
   public void
         itRecalculatesCalcParamWhenWorkPackageOnAircraftComponentStartedAfterFlightAndComponentDetachedAfterFlight()
               throws Exception {
      addEquationFunctionToDatabase();

      final HumanResourceKey lHr = createHumanResource();

      final AssemblyKey lAcftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aBuilder ) {
                  aBuilder.addUsageDefinitionConfiguration(
                        new DomainConfiguration<UsageDefinition>() {

                           @Override
                           public void configure( UsageDefinition aUsageDefinition ) {
                              aUsageDefinition.addUsageParameter( HOURS );
                              aUsageDefinition.setDataSource( RefDataSourceKey.MXFL );
                           }

                        } );
                  aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot aBuilder ) {
                        // Add a TRK config slot to the root config slot.
                        aBuilder
                              .addConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                                 @Override
                                 public void configure( ConfigurationSlot aBuilder ) {
                                    aBuilder.setCode( TRK_CONFIG_SLOT_CODE );
                                 }
                              } );
                        // Add a usage parameter and a calculated parameter based on that usage
                        // parameter.
                        aBuilder.addUsageParameter( HOURS );
                        aBuilder.addCalculatedUsageParameter(
                              new DomainConfiguration<CalculatedUsageParameter>() {

                                 @Override
                                 public void configure( CalculatedUsageParameter aBuilder ) {
                                    aBuilder.setCode( MUTIPLIER_DATA_TYPE_FOR_HOURS );
                                    aBuilder.setDatabaseCalculation( MULTIPLIER_FUNCTION_NAME );
                                    aBuilder.setPrecisionQt( 2 );

                                    // Add the constant and parameter in the order in which the
                                    // function is expecting them.
                                    aBuilder.addConstant( MULTIPLIER_CONSTANT_NAME,
                                          MULTIPLIER_CONSTANT );
                                    aBuilder.addParameter( HOURS );
                                 }

                              } );
                     }
                  } );
               }
            } );

      ConfigSlotKey lRootConfigSlot = Domain.readRootConfigurationSlot( lAcftAssembly );
      ConfigSlotKey lTrkConfigSlot =
            Domain.readSubConfigurationSlot( lRootConfigSlot, TRK_CONFIG_SLOT_CODE );
      final ConfigSlotPositionKey lTrkConfigSlotPos = new ConfigSlotPositionKey( lTrkConfigSlot,
            EqpAssmblPos.getFirstPosId( lTrkConfigSlot ) );
      final DataTypeKey lCalcParmKey =
            Domain.readUsageParameter( lRootConfigSlot, MUTIPLIER_DATA_TYPE_FOR_HOURS );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aBuilder ) {
            aBuilder.setAssembly( lAcftAssembly );
            aBuilder.addUsage( HOURS, FLIGHT_HOURS );
         }
      } );

      final InventoryKey lTrackedAircraftComponent =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aBuilder ) {
                  aBuilder.setPosition( lTrkConfigSlotPos );
                  aBuilder.addInstallationRecord( new DomainConfiguration<InstallationRecord>() {

                     @Override
                     public void configure( InstallationRecord aBuilder ) {
                        aBuilder.setAssembly( lAircraft );
                        aBuilder.setHighest( lAircraft );
                        aBuilder.setParent( lAircraft );
                        aBuilder.setInstallationDate( ATTACHED_DATE );
                     }

                  } );
                  aBuilder.addRemovalRecord( new DomainConfiguration<RemovalRecord>() {

                     @Override
                     public void configure( RemovalRecord aBuilder ) {
                        aBuilder.setAssembly( lAircraft );
                        aBuilder.setHighest( lAircraft );
                        aBuilder.setParent( lAircraft );
                        aBuilder.setRemovalDate( DETACHED_DATE );
                     }

                  } );
               }

            } );

      final UsageSnapshot lUsageSnapShotForHours =
            new UsageSnapshot( lTrackedAircraftComponent, HOURS, WP_START_HOURS );

      final TaskKey lComponentWorkPackage =
            Domain.createComponentWorkPackage( new DomainConfiguration<ComponentWorkPackage>() {

               @Override
               public void configure( ComponentWorkPackage aBuilder ) {
                  aBuilder.setActualStartDate( CURRENT_DATE );
                  aBuilder.setInventory( lTrackedAircraftComponent );
                  aBuilder.setStatus( RefEventStatusKey.IN_WORK );
                  aBuilder.addUsageSnapshot( lUsageSnapShotForHours );
                  aBuilder.addUsageSnapshot( new UsageSnapshot( lTrackedAircraftComponent,
                        lCalcParmKey, WP_START_HOURS ) );
               }

            } );

      FlightInformationTO lFlightInfoTO =
            generateFlightInfoTO( FLIGHT_NAME, FLIGHT_DATE, FLIGHT_DATE );
      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( lAircraft, HOURS, FLIGHT_HOURS ) };

      iFlightHistBean.createHistFlight( new AircraftKey( lAircraft ), lHr, lFlightInfoTO,
            lEditUsageParms, NO_MEASUREMENTS );

      BigDecimal lExpected = WP_START_HOURS.add( FLIGHT_HOURS ).multiply( MULTIPLIER_CONSTANT );

      BigDecimal lAcutual =
            readActualUsageParmValue( lComponentWorkPackage.getEventKey(), lAcftAssembly );

      {
         // Important!
         // All data needed for assertions must be retrieved prior to calling
         // dropEquationFunctionToDatabase(). As it performs an explicit roll-back.
         //
         // ALSO, all assertions must be done after dropEquationFunctionToDatabase() to ensure the
         // equation gets removed from the DB.
         dropEquationFunctionToDatabase();
      }

      Assert.assertTrue( "Unexpected value for " + MUTIPLIER_DATA_TYPE_FOR_HOURS + "; expected="
            + lExpected + " , actual=" + lAcutual, lExpected.compareTo( lAcutual ) == 0 );

   }


   @Before
   public void setup() {
      // Important!
      // Be aware that any data setup in the database within this setup() will be rolled back if the
      // test contains a call to addEquationFunctionToDatabase().
      SessionContextFake lFakeSession = new SessionContextFake();
      iFlightHistBean = new FlightHistBean();
      iFlightHistBean.ejbCreate();

      iFlightHistBean.setSessionContext( lFakeSession );

   }


   @After
   public void tearDown() {
      iFlightHistBean.setSessionContext( null );
   }


   private void addEquationFunctionToDatabase() throws SQLException {

      // Function creation is DDL which implicitly commits the transaction.
      // We perform explicit roll-back before function creation ensuring no data gets committed
      // accidentally.
      String lCreateFunctionStatement = "CREATE OR REPLACE FUNCTION " + MULTIPLIER_FUNCTION_NAME
            + " (" + "aConstant NUMBER, aHoursInput NUMBER" + " )" + " RETURN NUMBER" + " " + "IS "
            + "result NUMBER; " + "BEGIN" + " " + "result := aConstant * aHoursInput ; " + "RETURN"
            + " " + " result;" + "END" + " " + MULTIPLIER_FUNCTION_NAME + " ;";

      Domain.createCalculatedParameterEquationFunction( iDatabaseConnectionRule.getConnection(),
            lCreateFunctionStatement );
   }


   private void dropEquationFunctionToDatabase() throws SQLException {

      // Function dropping is DDL which implicitly commits transaction.
      // We perform explicit rollback before function drop ensuring no data gets committed
      // accidentally.
      Domain.dropCalculatedParameterEquationFunction( iDatabaseConnectionRule.getConnection(),
            MULTIPLIER_FUNCTION_NAME );
   }


   private HumanResourceKey createHumanResource() {
      HumanResourceKey lHrKey =
            new HumanResourceDomainBuilder().withUsername( HR_USERNAME ).build();

      int lUserId = OrgHr.findByPrimaryKey( lHrKey ).getUserId();
      UserParameters.setInstance( lUserId, "LOGIC", new UserParametersFake( lUserId, "LOGIC" ) );

      return lHrKey;
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


   private CollectedUsageParm generateFlightUsage( InventoryKey aInventoryKey,
         DataTypeKey lDataType, BigDecimal lDelta ) {

      // Create a usage collection to be returned.
      // CollectedUsageParm lUsageParm =
      new CollectedUsageParm( new UsageParmKey( aInventoryKey, lDataType ), lDelta.doubleValue() );

      CollectedUsageParm lUsageParm = new CollectedUsageParm(
            new UsageParmKey( aInventoryKey, lDataType ), lDelta.doubleValue() );

      // Create flight data source specifications.
      EqpDataSourceSpec
            .create( new UsageDefinitionKey( InvInvTable.getAssemblyByInventoryKey( aInventoryKey ),
                  RefDataSourceKey.MXFL ), lDataType );

      return lUsageParm;
   }


   private BigDecimal readActualUsageParmValue( EventKey lEnventKey, AssemblyKey lAcftAssembly ) {
      EventInventoryKey lEvtInvKey = new EventInventoryKey( lEnventKey, 1 );

      EventInventoryUsageKey lEvtInvCalcParmUsageKey = new EventInventoryUsageKey( lEvtInvKey,
            Domain.readUsageParameter( Domain.readRootConfigurationSlot( lAcftAssembly ),
                  MUTIPLIER_DATA_TYPE_FOR_HOURS ) );
      EvtInvUsageTable lEvtInvUsageCalcParm =
            EvtInvUsageTable.findByPrimaryKey( lEvtInvCalcParmUsageKey );
      lEvtInvUsageCalcParm = EvtInvUsageTable.findByPrimaryKey( lEvtInvCalcParmUsageKey );

      return BigDecimal.valueOf( lEvtInvUsageCalcParm.getTsnQt() );
   }
}
