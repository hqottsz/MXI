package com.mxi.mx.core.ejb.flighthist.flighthistbean.edithistflight;

import static com.mxi.mx.core.key.DataTypeKey.CYCLES;
import static com.mxi.mx.core.key.DataTypeKey.HOURS;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Flight;
import com.mxi.am.domain.InstallationRecord;
import com.mxi.am.domain.InstallationRecord.InstalledInventoryInfo;
import com.mxi.am.domain.Location;
import com.mxi.am.domain.RemovalRecord;
import com.mxi.am.domain.RemovalRecord.RemovedInventoryInfo;
import com.mxi.am.domain.TrackedInventory;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.servlet.SessionContextFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.unittest.security.SecurityIdentificationStub;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.ejb.flighthist.CurrentUsages;
import com.mxi.mx.core.ejb.flighthist.FlightHistBean;
import com.mxi.mx.core.flight.model.FlightLegId;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefDataSourceKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.UsageDefinitionKey;
import com.mxi.mx.core.key.UsageParmKey;
import com.mxi.mx.core.services.event.usage.CollectedUsageParm;
import com.mxi.mx.core.services.flighthist.FlightInformationTO;
import com.mxi.mx.core.services.flighthist.Measurement;
import com.mxi.mx.core.table.eqp.EqpDataSourceSpec;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.org.OrgHr;


/**
 * Tests {@linkplain FlightHistBean#editHistFlight} for its affect on current usage of tracked
 * inventory associated to the flight.
 *
 */
public class EditHistFlight_AffectTrkCurrentUsageTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static final String HR_USERNAME = "HR_USERNAME";
   private static final String FLIGHT = "FLIGHT";
   private static final Measurement[] NO_MEASUREMENTS = new Measurement[] {};

   private static final BigDecimal ACFT_CURRENT_CYCLES = new BigDecimal( 100 );
   private static final BigDecimal ACFT_CURRENT_HOURS = new BigDecimal( 1000 );

   private static final BigDecimal TRK_CURRENT_CYCLES = new BigDecimal( 200 );
   private static final BigDecimal TRK_CURRENT_HOURS = new BigDecimal( 2000 );

   private static final BigDecimal SUB_COMP_CURRENT_CYCLES = new BigDecimal( 300 );
   private static final BigDecimal SUB_COMP_CURRENT_HOURS = new BigDecimal( 3000 );

   private static final BigDecimal FLIGHT_HOURS = new BigDecimal( 6 );
   private static final BigDecimal FLIGHT_CYCLES = new BigDecimal( 1 );
   private static final BigDecimal CORRECTED_FLIGHT_HOURS = new BigDecimal( 8 );
   private static final BigDecimal CORRECTED_FLIGHT_CYCLES = new BigDecimal( 2 );

   private HumanResourceKey iHrKey;
   private int iUserId;

   // the bean under test
   private FlightHistBean iFlightHistBean;


   /**
    *
    * Verify that the current usage of an installed tracked inventory is updated when the flight it
    * was on has had its usage edited.
    *
    * <pre>
    * Given an aircraft with current usage
    *   And an installed tracked inventory with current usage (same parameters as the aircraft)
    *   And the aircraft has a historical flight
    *  When the flight is edited and the usage is changed
    *  Then the current TSN of the tracked inventory is updated with the difference in the usage
    * </pre>
    *
    */
   @Test
   public void itAdjustsInstalledTrkCurrentUsageTsnWhenFlightUsageIsEdited() throws Exception {

      final Date lFlightDate = DateUtils.addDays( new Date(), -100 );

      // Given an aircraft with current usage.
      // And an installed tracked inventory with current usage (same parameters as the aircraft)
      final InventoryKey lTrkInv =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aBuilder ) {
                  aBuilder.addUsage( HOURS, TRK_CURRENT_HOURS );
                  aBuilder.addUsage( CYCLES, TRK_CURRENT_CYCLES );
               }
            } );
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aBuilder ) {
            aBuilder.addUsage( HOURS, ACFT_CURRENT_HOURS );
            aBuilder.addUsage( CYCLES, ACFT_CURRENT_CYCLES );
            aBuilder.addTracked( lTrkInv );
         }
      } );

      // Given the aircraft has a historical flight.
      FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aBuilder ) {
            aBuilder.setName( FLIGHT );
            aBuilder.setAircraft( lAircraft );
            aBuilder.setArrivalDate( lFlightDate );
            aBuilder.setHistorical( true );
            aBuilder.addUsage( lAircraft, HOURS, FLIGHT_HOURS, null );
            aBuilder.addUsage( lAircraft, CYCLES, FLIGHT_CYCLES, null );
         }
      } );

      // When the flight is edited and the usage is changed.
      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( lAircraft, CYCLES, CORRECTED_FLIGHT_CYCLES ),
                  generateFlightUsage( lAircraft, HOURS, CORRECTED_FLIGHT_HOURS ) };
      iFlightHistBean.editHistFlight( lFlight, iHrKey,
            generateFlightInfoTO( FLIGHT, lFlightDate, lFlightDate ), lEditUsageParms,
            NO_MEASUREMENTS );

      // Then the current TSN of the tracked inventory is updated with the difference in the usage.
      BigDecimal lFlightCyclesDiff = CORRECTED_FLIGHT_CYCLES.subtract( FLIGHT_CYCLES );
      BigDecimal lFlightHoursDiff = CORRECTED_FLIGHT_HOURS.subtract( FLIGHT_HOURS );

      BigDecimal lExpectedHours = TRK_CURRENT_HOURS.add( lFlightHoursDiff );
      BigDecimal lExpectedCycles = TRK_CURRENT_CYCLES.add( lFlightCyclesDiff );

      CurrentUsages lCurrentUsage = new CurrentUsages( lTrkInv );

      assertEquals( "Unexpected TSN for HOURS", lExpectedHours, lCurrentUsage.getTsn( HOURS ) );
      assertEquals( "Unexpected TSN for CYCLES", lExpectedCycles, lCurrentUsage.getTsn( CYCLES ) );
   }


   /**
    *
    * Verify that the current usage of a loose tracked inventory and its sub-component are updated
    * when the flight it was on has had its usage edited.
    *
    * I.e. the tracked inventory (and its sub-component) were installed during the flight but were
    * detached after the flight.
    *
    * <pre>
    * Given an aircraft with current usage
    *   And a loose tracked inventory with current usage (same parameters as the aircraft)
    *   And a sub-component that is attached to the loose tracked inventory and has current usage (same parameters as the aircraft)
    *   And the aircraft has a historical flight during which the tracked inventory and its sub-component were installed
    *   And a removal record indicating the tracked inventory was detached from the aircraft after the flight
    *  When the flight is edited and the usage is changed
    *  Then the current TSN of the tracked inventory is updated with the difference in the usage
    *   And the current TSN of the tracked inventory's sub-component is updated with the difference in the usage
    * </pre>
    *
    */
   @Test
   public void itAdjustsLooseTrkAndSubComponentsCurrentUsageTsnWhenFlightUsageIsEdited()
         throws Exception {

      final Date lFlightDate = DateUtils.addDays( new Date(), -100 );
      final Date lTrkRemovalDateAfterFlight = DateUtils.addDays( lFlightDate, 10 );

      // Given an aircraft with current usage.
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aBuilder ) {
            aBuilder.addUsage( HOURS, ACFT_CURRENT_HOURS );
            aBuilder.addUsage( CYCLES, ACFT_CURRENT_CYCLES );
         }
      } );

      // Given a loose tracked inventory with current usage (same parameters as the aircraft)
      // And a sub-component that is attached to the loose tracked inventory and has current usage
      // (same parameters as the aircraft)
      // And a removal record indicating the tracked inventory was detached from the aircraft after
      // the flight (see below for flight).
      final InventoryKey lSubComp =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aBuilder ) {
                  aBuilder.addUsage( HOURS, SUB_COMP_CURRENT_HOURS );
                  aBuilder.addUsage( CYCLES, SUB_COMP_CURRENT_CYCLES );
               }
            } );
      final InventoryKey lTrkInv =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aBuilder ) {
                  aBuilder.addUsage( HOURS, TRK_CURRENT_HOURS );
                  aBuilder.addUsage( CYCLES, TRK_CURRENT_CYCLES );
                  aBuilder.addTracked( lSubComp );
                  aBuilder.addRemovalRecord( new DomainConfiguration<RemovalRecord>() {

                     @Override
                     public void configure( RemovalRecord aTrkRemoval ) {
                        aTrkRemoval.setRemovalDate( lTrkRemovalDateAfterFlight );
                        // Builder will auto set the inventory to this TRK being created.
                        aTrkRemoval.setParent( lAircraft );
                        aTrkRemoval.setHighest( lAircraft );
                        aTrkRemoval.setAssembly( lAircraft );
                        aTrkRemoval
                              .addSubInventory( new DomainConfiguration<RemovedInventoryInfo>() {

                                 @Override
                                 public void configure( RemovedInventoryInfo aSubCompRemoval ) {
                                    // Builder will auto set the parent to this TRK being created.
                                    aSubCompRemoval.setInventory( lSubComp );
                                    aSubCompRemoval.setHighest( lAircraft );
                                    aSubCompRemoval.setAssembly( lAircraft );
                                 }
                              } );
                     }
                  } );
               }
            } );

      // Given the aircraft has a historical flight during which the tracked inventory and its
      // sub-component were installed.
      FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aBuilder ) {
            aBuilder.setName( FLIGHT );
            aBuilder.setAircraft( lAircraft );
            aBuilder.setArrivalDate( lFlightDate );
            aBuilder.setHistorical( true );
            aBuilder.addUsage( lAircraft, HOURS, FLIGHT_HOURS, null );
            aBuilder.addUsage( lAircraft, CYCLES, FLIGHT_CYCLES, null );
         }
      } );

      // When the flight is edited and the usage is changed.
      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( lAircraft, CYCLES, CORRECTED_FLIGHT_CYCLES ),
                  generateFlightUsage( lAircraft, HOURS, CORRECTED_FLIGHT_HOURS ) };
      iFlightHistBean.editHistFlight( lFlight, iHrKey,
            generateFlightInfoTO( FLIGHT, lFlightDate, lFlightDate ), lEditUsageParms,
            NO_MEASUREMENTS );

      // Then the current TSN of the tracked inventory is updated with the difference in the usage.
      BigDecimal lFlightCyclesDiff = CORRECTED_FLIGHT_CYCLES.subtract( FLIGHT_CYCLES );
      BigDecimal lFlightHoursDiff = CORRECTED_FLIGHT_HOURS.subtract( FLIGHT_HOURS );

      BigDecimal lExpectedHours = TRK_CURRENT_HOURS.add( lFlightHoursDiff );
      BigDecimal lExpectedCycles = TRK_CURRENT_CYCLES.add( lFlightCyclesDiff );

      CurrentUsages lCurrentUsage = new CurrentUsages( lTrkInv );

      assertEquals( "Unexpected TSN for HOURS of tracked inventory.", lExpectedHours,
            lCurrentUsage.getTsn( HOURS ) );
      assertEquals( "Unexpected TSN for CYCLES of tracked inventory.", lExpectedCycles,
            lCurrentUsage.getTsn( CYCLES ) );

      // Then the current TSN of the tracked inventory's sub-component is updated with the
      // difference in the usage.
      lExpectedHours = SUB_COMP_CURRENT_HOURS.add( lFlightHoursDiff );
      lExpectedCycles = SUB_COMP_CURRENT_CYCLES.add( lFlightCyclesDiff );

      lCurrentUsage = new CurrentUsages( lSubComp );

      assertEquals( "Unexpected TSN for HOURS of sub-component.", lExpectedHours,
            lCurrentUsage.getTsn( HOURS ) );
      assertEquals( "Unexpected TSN for CYCLES of sub-component.", lExpectedCycles,
            lCurrentUsage.getTsn( CYCLES ) );
   }


   /**
    *
    * Verify that the current usage of a re-installed tracked inventory is updated when the flight
    * it was on has had its usage edited.
    *
    * I.e. the tracked inventory was installed prior the flight but was detached and re-installed
    * after the flight.
    *
    * <pre>
    * Given an aircraft with current usage
    *   And an installed tracked inventory with current usage (same parameters as the aircraft)
    *   And the aircraft has a historical flight during which the tracked inventory was installed
    *   And a removal record indicating the tracked inventory was detached from the aircraft after the flight
    *   And an install record indicating the tracked inventory was re-attached to the aircraft after the removal
    *  When the flight is edited and the usage is changed
    *  Then the current TSN of the tracked inventory is updated with the difference in the usage
    * </pre>
    *
    */
   @Test
   public void itAdjustsReinstalledTrksCurrentUsageTsnWhenPriorFlightUsageIsEdited()
         throws Exception {

      final Date lFlightDate = DateUtils.addDays( new Date(), -100 );
      final Date lRemovalDateAfterFlight = DateUtils.addDays( lFlightDate, 10 );
      final Date lInstallDateAfterRemoval = DateUtils.addDays( lRemovalDateAfterFlight, 20 );

      // Given an aircraft with current usage
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aBuilder ) {
            aBuilder.addUsage( HOURS, ACFT_CURRENT_HOURS );
            aBuilder.addUsage( CYCLES, ACFT_CURRENT_CYCLES );
         }
      } );

      // Given an installed tracked inventory with current usage (same parameters as the aircraft)
      // and a removal record indicating the tracked inventory was detached from the aircraft after
      // the flight
      // and an install record indicating the tracked inventory was re-attached to the aircraft
      // after the removal.
      final InventoryKey lTrkInv =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aBuilder ) {
                  aBuilder.setParent( lAircraft );
                  aBuilder.addUsage( HOURS, TRK_CURRENT_HOURS );
                  aBuilder.addUsage( CYCLES, TRK_CURRENT_CYCLES );
                  aBuilder.addRemovalRecord( new DomainConfiguration<RemovalRecord>() {

                     @Override
                     public void configure( RemovalRecord aBuilder ) {
                        aBuilder.setRemovalDate( lRemovalDateAfterFlight );
                        aBuilder.setParent( lAircraft );
                        aBuilder.setHighest( lAircraft );
                        aBuilder.setAssembly( lAircraft );
                     }
                  } );
                  aBuilder.addInstallationRecord( new DomainConfiguration<InstallationRecord>() {

                     @Override
                     public void configure( InstallationRecord aBuilder ) {
                        aBuilder.setInstallationDate( lInstallDateAfterRemoval );
                        aBuilder.setParent( lAircraft );
                        aBuilder.setHighest( lAircraft );
                        aBuilder.setAssembly( lAircraft );
                     }
                  } );
               }
            } );

      // Given the aircraft has a historical flight during which the tracked inventory was installed
      FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aBuilder ) {
            aBuilder.setName( FLIGHT );
            aBuilder.setAircraft( lAircraft );
            aBuilder.setArrivalDate( lFlightDate );
            aBuilder.setHistorical( true );
            aBuilder.addUsage( lAircraft, HOURS, FLIGHT_HOURS, null );
            aBuilder.addUsage( lAircraft, CYCLES, FLIGHT_CYCLES, null );
         }
      } );

      // When the flight is edited and the usage is changed.
      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( lAircraft, CYCLES, CORRECTED_FLIGHT_CYCLES ),
                  generateFlightUsage( lAircraft, HOURS, CORRECTED_FLIGHT_HOURS ) };
      iFlightHistBean.editHistFlight( lFlight, iHrKey,
            generateFlightInfoTO( FLIGHT, lFlightDate, lFlightDate ), lEditUsageParms,
            NO_MEASUREMENTS );

      // Then the current TSN of the tracked inventory is updated with the difference in the usage.
      BigDecimal lFlightCyclesDiff = CORRECTED_FLIGHT_CYCLES.subtract( FLIGHT_CYCLES );
      BigDecimal lFlightHoursDiff = CORRECTED_FLIGHT_HOURS.subtract( FLIGHT_HOURS );

      BigDecimal lExpectedHours = TRK_CURRENT_HOURS.add( lFlightHoursDiff );
      BigDecimal lExpectedCycles = TRK_CURRENT_CYCLES.add( lFlightCyclesDiff );

      CurrentUsages lCurrentUsage = new CurrentUsages( lTrkInv );

      assertEquals( "Unexpected TSN for HOURS", lExpectedHours, lCurrentUsage.getTsn( HOURS ) );
      assertEquals( "Unexpected TSN for CYCLES", lExpectedCycles, lCurrentUsage.getTsn( CYCLES ) );
   }


   /**
    *
    * Verify that the current usage of a post-flight installed tracked inventory is not updated when
    * the flight has had its usage edited.
    *
    * I.e. the tracked inventory was installed after the flight and is still installed.
    *
    * <pre>
    * Given an aircraft with current usage
    *   And an installed tracked inventory with current usage (same parameters as the aircraft)
    *   And the aircraft has a historical flight
    *   And an install record indicating the tracked inventory was attached to the aircraft after the flight
    *  When the flight is edited and the usage is changed
    *  Then the current TSN of the tracked inventory is not updated
    * </pre>
    *
    */
   @Test
   public void itDoesNotAdjustTrksCurrentUsageTsnWhenInstalledAfterFlightWhoseUsageIsEdited()
         throws Exception {

      final Date lFlightDate = DateUtils.addDays( new Date(), -100 );
      final Date lInstallDateAfterFlight = DateUtils.addDays( lFlightDate, 10 );

      // Given an aircraft with current usage
      // and an installed tracked inventory with current usage (same parameters as the aircraft)
      // and an install record indicating the tracked inventory was attached to the aircraft after
      // the flight
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aBuilder ) {
            aBuilder.addUsage( HOURS, ACFT_CURRENT_HOURS );
            aBuilder.addUsage( CYCLES, ACFT_CURRENT_CYCLES );
         }
      } );
      final InventoryKey lTrkInv =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aBuilder ) {
                  aBuilder.addUsage( HOURS, TRK_CURRENT_HOURS );
                  aBuilder.addUsage( CYCLES, TRK_CURRENT_CYCLES );
                  aBuilder.setParent( lAircraft );
                  aBuilder.addInstallationRecord( new DomainConfiguration<InstallationRecord>() {

                     @Override
                     public void configure( InstallationRecord aBuilder ) {
                        aBuilder.setInstallationDate( lInstallDateAfterFlight );
                        aBuilder.setParent( lAircraft );
                        aBuilder.setHighest( lAircraft );
                        aBuilder.setAssembly( lAircraft );
                     }
                  } );
               }
            } );

      // Given the aircraft has a historical flight (which the tracked was not installed)
      final FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aBuilder ) {
            aBuilder.setName( FLIGHT );
            aBuilder.setAircraft( lAircraft );
            aBuilder.setArrivalDate( lFlightDate );
            aBuilder.setHistorical( true );
            aBuilder.addUsage( lAircraft, HOURS, FLIGHT_HOURS, null );
            aBuilder.addUsage( lAircraft, CYCLES, FLIGHT_CYCLES, null );
         }
      } );

      // When the flight is edited and the usage is changed.
      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( lAircraft, CYCLES, CORRECTED_FLIGHT_CYCLES ),
                  generateFlightUsage( lAircraft, HOURS, CORRECTED_FLIGHT_HOURS ) };
      iFlightHistBean.editHistFlight( lFlight, iHrKey,
            generateFlightInfoTO( FLIGHT, lFlightDate, lFlightDate ), lEditUsageParms,
            NO_MEASUREMENTS );

      // Then the current TSN of the tracked inventory is not updated
      CurrentUsages lCurrentUsage = new CurrentUsages( lTrkInv );
      assertEquals( "Unexpected TSN for HOURS", TRK_CURRENT_HOURS, lCurrentUsage.getTsn( HOURS ) );
      assertEquals( "Unexpected TSN for CYCLES", TRK_CURRENT_CYCLES,
            lCurrentUsage.getTsn( CYCLES ) );
   }


   /**
    *
    * Verify that the current usage of a loose tracked inventory and its sub-component is not
    * updated when a flight has had its usage edited and when that loose tracked inventory was
    * installed and later removed after the flight.
    *
    * I.e. the tracked inventory (and its sub-component) was installed and subsequently removed
    * after the flight.
    *
    * <pre>
    * Given an aircraft with current usage
    *   And the aircraft has a historical flight
    *   And a loose tracked inventory with current usage (same parameters as the aircraft)
    *   And an attached sub-component with current usage (same parameters as the aircraft)
    *   And an install record indicating the tracked inventory was attached to the aircraft after the flight
    *   And a removal record indicating the tracked inventory was detached from the aircraft after it was attached
    *  When the flight is edited and the usage is changed
    *  Then the current TSN of the tracked inventory is not updated
    *   And the current TSN of the sub-component is not updated
    * </pre>
    *
    */
   @Test
   public void
         itDoesNotAdjustTrkAndSubCOmponentsCurrentUsageTsnWhenInstalledAndRemovedAfterFlightWhoseUsageIsEdited()
               throws Exception {

      final Date lFlightDate = DateUtils.addDays( new Date(), -100 );
      final Date lTrkInstallDateAfterFlight = DateUtils.addDays( lFlightDate, 10 );
      final Date lTrkRemovalDateAfterInstall = DateUtils.addDays( lTrkInstallDateAfterFlight, 20 );

      // Given an aircraft with current usage.
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aBuilder ) {
            aBuilder.addUsage( HOURS, ACFT_CURRENT_HOURS );
            aBuilder.addUsage( CYCLES, ACFT_CURRENT_CYCLES );
         }
      } );

      // Given the aircraft has a historical flight.
      FlightLegId lFlight = Domain.createFlight( new DomainConfiguration<Flight>() {

         @Override
         public void configure( Flight aBuilder ) {
            aBuilder.setName( FLIGHT );
            aBuilder.setAircraft( lAircraft );
            aBuilder.setArrivalDate( lFlightDate );
            aBuilder.setHistorical( true );
            aBuilder.addUsage( lAircraft, HOURS, FLIGHT_HOURS, null );
            aBuilder.addUsage( lAircraft, CYCLES, FLIGHT_CYCLES, null );
         }
      } );

      // Given a loose tracked inventory with current usage (same parameters as the aircraft)
      // and an attached sub-component with current usage (same parameters as the aircraft)
      // and an install record indicating the tracked inventory was attached to the aircraft after
      // the flight
      // and a removal record indicating the tracked inventory was detached from the aircraft after
      // it was attached
      final InventoryKey lSubComp =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aBuilder ) {
                  aBuilder.addUsage( HOURS, SUB_COMP_CURRENT_HOURS );
                  aBuilder.addUsage( CYCLES, SUB_COMP_CURRENT_CYCLES );
               }
            } );
      final InventoryKey lTrkInv =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aBuilder ) {
                  aBuilder.addUsage( HOURS, TRK_CURRENT_HOURS );
                  aBuilder.addUsage( CYCLES, TRK_CURRENT_CYCLES );
                  aBuilder.addTracked( lSubComp );
                  aBuilder.addInstallationRecord( new DomainConfiguration<InstallationRecord>() {

                     @Override
                     public void configure( InstallationRecord aTrkInstall ) {
                        aTrkInstall.setInstallationDate( lTrkInstallDateAfterFlight );
                        // Builder will auto set the inventory to this TRK being created.
                        aTrkInstall.setParent( lAircraft );
                        aTrkInstall.setHighest( lAircraft );
                        aTrkInstall.setAssembly( lAircraft );
                        aTrkInstall
                              .addSubInventory( new DomainConfiguration<InstalledInventoryInfo>() {

                                 @Override
                                 public void configure( InstalledInventoryInfo aSubCompInstall ) {
                                    // Builder will auto set the parent to this TRK being created.
                                    aSubCompInstall.setInventory( lSubComp );
                                    aSubCompInstall.setHighest( lAircraft );
                                    aSubCompInstall.setAssembly( lAircraft );
                                 }
                              } );
                     }
                  } );
                  aBuilder.addRemovalRecord( new DomainConfiguration<RemovalRecord>() {

                     @Override
                     public void configure( RemovalRecord aTrkRemoval ) {
                        aTrkRemoval.setRemovalDate( lTrkRemovalDateAfterInstall );
                        // Builder will auto set the inventory to this TRK being created.
                        aTrkRemoval.setParent( lAircraft );
                        aTrkRemoval.setHighest( lAircraft );
                        aTrkRemoval.setAssembly( lAircraft );
                        aTrkRemoval
                              .addSubInventory( new DomainConfiguration<RemovedInventoryInfo>() {

                                 @Override
                                 public void configure( RemovedInventoryInfo aSubCompRemoval ) {
                                    // Builder will auto set the parent to this TRK being created.
                                    aSubCompRemoval.setInventory( lSubComp );
                                    aSubCompRemoval.setHighest( lAircraft );
                                    aSubCompRemoval.setAssembly( lAircraft );
                                 }
                              } );
                     }
                  } );
               }
            } );

      // When the flight is edited and the usage is changed.
      CollectedUsageParm[] lEditUsageParms =
            { generateFlightUsage( lAircraft, CYCLES, CORRECTED_FLIGHT_CYCLES ),
                  generateFlightUsage( lAircraft, HOURS, CORRECTED_FLIGHT_HOURS ) };
      iFlightHistBean.editHistFlight( lFlight, iHrKey,
            generateFlightInfoTO( FLIGHT, lFlightDate, lFlightDate ), lEditUsageParms,
            NO_MEASUREMENTS );

      // Then the current TSN of the tracked inventory is not updated
      CurrentUsages lCurrentUsage = new CurrentUsages( lTrkInv );
      assertEquals( "Unexpected TSN for HOURS", TRK_CURRENT_HOURS, lCurrentUsage.getTsn( HOURS ) );
      assertEquals( "Unexpected TSN for CYCLES", TRK_CURRENT_CYCLES,
            lCurrentUsage.getTsn( CYCLES ) );

      // Then the current TSN of the sub-component is not updated
      lCurrentUsage = new CurrentUsages( lSubComp );
      assertEquals( "Unexpected TSN for HOURS", SUB_COMP_CURRENT_HOURS,
            lCurrentUsage.getTsn( HOURS ) );
      assertEquals( "Unexpected TSN for CYCLES", SUB_COMP_CURRENT_CYCLES,
            lCurrentUsage.getTsn( CYCLES ) );
   }


   @Before
   public void setup() {
      iHrKey = new HumanResourceDomainBuilder().withUsername( HR_USERNAME ).build();
      iUserId = OrgHr.findByPrimaryKey( iHrKey ).getUserId();
      SecurityIdentificationUtils.setInstance( new SecurityIdentificationStub( iHrKey ) );

      // Set the MAX_FLIGHT_DURATION_VALUE to null
      // to avoid the flight duration validation done by FlightHistBean.editHistFlight()
      UserParametersFake lUserParms = new UserParametersFake( iUserId, "LOGIC" );
      lUserParms.setProperty( "MAX_FLIGHT_DURATION_VALUE", null );
      lUserParms.setProperty( "MAX_FLIGHT_DAYS_IN_THE_PAST_VALUE", "101" );
      UserParameters.setInstance( iUserId, "LOGIC", lUserParms );

      iFlightHistBean = new FlightHistBean();
      iFlightHistBean.ejbCreate();
      iFlightHistBean.setSessionContext( new SessionContextFake() );

   }


   @After
   public void tearDown() {
      UserParameters.setInstance( iUserId, "LOGIC", null );
      SecurityIdentificationUtils.setInstance( null );
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

      return new FlightInformationTO( aFlightName, null, null, null, null, null, lDepartureAirport,
            lArrivalAirport, null, null, null, null, aActualDepartureDate, aActualArrivalDate, null,
            null, false, false );

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

}
