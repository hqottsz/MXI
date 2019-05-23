package com.mxi.mx.web.query.usage;

import static com.mxi.mx.core.key.DataTypeKey.CYCLES;
import static com.mxi.mx.core.key.DataTypeKey.HOURS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Engine;
import com.mxi.am.domain.InstallationRecord;
import com.mxi.am.domain.InstallationRecord.InstalledInventoryInfo;
import com.mxi.am.domain.RemovalRecord;
import com.mxi.am.domain.RemovalRecord.RemovedInventoryInfo;
import com.mxi.am.domain.TrackedInventory;
import com.mxi.am.domain.UsageAdjustment;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.usage.model.UsageAdjustmentId;
import com.mxi.mx.core.usage.model.UsageType;


/**
 * This is a unit test for UsageHistoryOfTrkSer.qrx
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class UsageHistoryOfTrkSerTest {

   private static final BigDecimal ACFT_CURRENT_CYCLES = new BigDecimal( 300 );
   private static final BigDecimal ACFT_CURRENT_HOURS = new BigDecimal( 3000 );

   private static final BigDecimal SUB_COMP_CURRENT_CYCLES = new BigDecimal( 10 );
   private static final BigDecimal SUB_COMP_CURRENT_HOURS = new BigDecimal( 100 );

   private static final BigDecimal USAGE_CYCLES = new BigDecimal( 100 );
   private static final BigDecimal USAGE_HOURS = new BigDecimal( 500 );
   private static final BigDecimal USAGE_CYCLES_DELTA = new BigDecimal( 1 );
   private static final BigDecimal USAGE_HOURS_DELTA = new BigDecimal( 5 );

   private static final BigDecimal ENGINE_CURRENT_CYCLES = new BigDecimal( 80 );
   private static final BigDecimal ENGINE_CURRENT_HOURS = new BigDecimal( 800 );

   private static final BigDecimal TRK_CURRENT_CYCLES = new BigDecimal( 200 );
   private static final BigDecimal TRK_CURRENT_HOURS = new BigDecimal( 2000 );

   private static final String CYCLE_TSN = "UsgParam0_10_TSN";
   private static final String HOUR_TSN = "UsgParam0_1_TSN";
   private static final String CYCLE_DELTA = "UsgParam0_10_DELTA";
   private static final String HOUR_DELTA = "UsgParam0_1_DELTA";
   private static final String USAGE_PARAM_LIST =
         "'UsgParam0_1' AS UsgParam0_1,'UsgParam0_10' AS UsgParam0_10";
   private static final String REMOVAL = "Removal";
   private static final String INSTALLATION = "Installation";

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * <pre>
    * Given an Engine with multiple flight usage records
    *   and the engine was installed on an aircraft
    *   and a tracked component that was on the engine during the flights
    * When the UsageHistoryOfTrkSer query is executed for the tracked component with daycount less than usage dates
    * Then the query should not return any usage
    * </pre>
    */
   @Test
   public void itDoesNotReturnAnyRecordsWhenThereIsNoUsagesForGivenTimePeriod() {

      final Date lEngineInstallDate = DateUtils.addDays( new Date(), -15 );

      final Date lTrkInstallDate = DateUtils.addDays( lEngineInstallDate, 1 );

      final InventoryKey lEngine = createEngine( lEngineInstallDate );

      // Create Aircraft with Engine
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( HOURS, ACFT_CURRENT_HOURS );
            aAircraft.addUsage( CYCLES, ACFT_CURRENT_CYCLES );
            aAircraft.addEngine( lEngine );
         }
      } );

      // Create Trk Inventory
      final InventoryKey lTrkInventory = createTrkInventory( lAircraft, lEngine, lTrkInstallDate );

      final Date lUsageDate = DateUtils.addDays( lEngineInstallDate, 5 );

      final int lDayCount = 5;

      // Create usage
      createUsageRecord( lEngine, lUsageDate, UsageType.MXFLIGHT );
      createUsageRecord( lTrkInventory, DateUtils.addDays( lUsageDate, 2 ),
            UsageType.MXCORRECTION );

      // run query
      QuerySet lUsageHistoryQs = executeUsageHistory( lTrkInventory, lDayCount, USAGE_PARAM_LIST );

      // No records are within the DayCount
      assertEquals( "No records should be visible", 0, lUsageHistoryQs.getRowCount() );

   }


   /**
    * <pre>
    * Given an Engine with a flight usage record
    *   and the engine was installed on an aircraft
    *   and a tracked component that was on the engine during the flight usage
    * When the UsageHistoryOfTrkSer query is executed for the tracked component
    * Then the query should return one usage
    * </pre>
    */
   @Test
   public void itReturnsUsageWhenThereIsUsageForComponent() {

      final Date lEngineInstallDate = DateUtils.addDays( new Date(), -15 );

      final Date lTrkInstallDate = DateUtils.addDays( lEngineInstallDate, 1 );

      final InventoryKey lEngine = createEngine( lEngineInstallDate );

      // Create Aircraft with Engine
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( HOURS, ACFT_CURRENT_HOURS );
            aAircraft.addUsage( CYCLES, ACFT_CURRENT_CYCLES );
            aAircraft.addEngine( lEngine );
         }
      } );

      // Create Trk Inventory
      final InventoryKey lTrkInventory = createTrkInventory( lAircraft, lEngine, lTrkInstallDate );

      final Date lUsageDate = DateUtils.addDays( lEngineInstallDate, 5 );

      final int lDayCount = 30;

      // Create usage
      UsageAdjustmentId lFlightUsageId =
            createUsageRecord( lEngine, lUsageDate, UsageType.MXFLIGHT );

      // run query
      QuerySet lUsageHistoryQs = executeUsageHistory( lTrkInventory, lDayCount, USAGE_PARAM_LIST );

      // check
      // two rows should be returned - one for installation and another for MXFLIGHT
      assertEquals( "Expected Row count should be 2", 2, lUsageHistoryQs.getRowCount() );

      // retrieve flight usage record
      Map<UsageAdjustmentId, Map<String, BigDecimal>> lFlightUsage =
            retrieveUsage( lUsageHistoryQs, lFlightUsageId );

      // assert actual values of usage
      assertEquals( "Incorrect cycles encountered for TSN", TRK_CURRENT_CYCLES,
            lFlightUsage.get( lFlightUsageId ).get( CYCLE_TSN ) );

      assertEquals( "Incorrect delta encountered for Cycles", USAGE_CYCLES_DELTA,
            lFlightUsage.get( lFlightUsageId ).get( CYCLE_DELTA ) );

      assertEquals( "Incorrect hours encountered for TSN", TRK_CURRENT_HOURS,
            lFlightUsage.get( lFlightUsageId ).get( HOUR_TSN ) );

      assertEquals( "Incorrect delta encountered for hours", USAGE_HOURS_DELTA,
            lFlightUsage.get( lFlightUsageId ).get( HOUR_DELTA ) );

   }


   /**
    * <pre>
    * Given an Engine with two usage records
    *   and the engine was installed on an aircraft
    *   and a tracked component was on the engine during the flight/usage
    * When the UsageHistoryOfTrkSer query is executed for the tracked component
    * Then the query should return 3 records
    * </pre>
    */
   @Test
   public void itReturnsMultipleUsagesWhenThereAreUsagesForComponent() {

      final Date lEngineInstallDate = DateUtils.addDays( new Date(), -15 );

      final Date lTrkInstallDate = DateUtils.addDays( lEngineInstallDate, 1 );

      final InventoryKey lEngine = createEngine( lEngineInstallDate );

      // Create Aircraft with Engine
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( HOURS, ACFT_CURRENT_HOURS );
            aAircraft.addUsage( CYCLES, ACFT_CURRENT_CYCLES );
            aAircraft.addEngine( lEngine );
         }
      } );

      // Create Trk Inventory
      final InventoryKey lTrkInventory = createTrkInventory( lAircraft, lEngine, lTrkInstallDate );

      final Date lUsageDate = DateUtils.addDays( lEngineInstallDate, 5 );

      final int lDayCount = 30;

      // Create 2 usage records
      createUsageRecord( lEngine, lUsageDate, UsageType.MXFLIGHT );

      createUsageRecord( lEngine, DateUtils.addDays( lUsageDate, 1 ), UsageType.MXACCRUAL );

      // run query
      QuerySet lUsageHistoryQs = executeUsageHistory( lTrkInventory, lDayCount, USAGE_PARAM_LIST );
      // 3 rows returned: one installation record and 2 usage records
      assertEquals( "Expected Row count should be 3", 3, lUsageHistoryQs.getRowCount() );

   }


   /**
    * <pre>
    * Given an Engine with two usage records - MXFLIGHT and MXACCRUAL
    *   and the engine was installed on an aircraft
    *   and a tracked component that was on the engine during the flight/usage
    * When the UsageHistoryOfTrkSer query is executed for the tracked component
    * Then the query should return Correct calculated TSN values for MXFLIGHT
    * </pre>
    */
   @Test
   public void itReturnsPreviousTSNValueCorrectlyWhenThereAreMultipleUsagesForComponent() {

      final Date lEngineInstallDate = DateUtils.addDays( new Date(), -15 );

      final Date lTrkInstallDate = DateUtils.addDays( lEngineInstallDate, 1 );

      final InventoryKey lEngine = createEngine( lEngineInstallDate );

      // Create Aircraft with Engine
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( HOURS, ACFT_CURRENT_HOURS );
            aAircraft.addUsage( CYCLES, ACFT_CURRENT_CYCLES );
            aAircraft.addEngine( lEngine );
         }
      } );

      // Create Trk Inventory
      final InventoryKey lTrkInventory = createTrkInventory( lAircraft, lEngine, lTrkInstallDate );

      final Date lUsageDate = DateUtils.addDays( lEngineInstallDate, 5 );

      final int lDayCount = 30;

      // Create 2 usage records
      UsageAdjustmentId lFlightUsageRecordId =
            createUsageRecord( lEngine, lUsageDate, UsageType.MXFLIGHT );

      createUsageRecord( lEngine, DateUtils.addDays( lUsageDate, 1 ), UsageType.MXACCRUAL );

      // run query
      QuerySet lUsageHistoryQs = executeUsageHistory( lTrkInventory, lDayCount, USAGE_PARAM_LIST );

      // retrieve flight usage record
      Map<UsageAdjustmentId, Map<String, BigDecimal>> lFlightUsage =
            retrieveUsage( lUsageHistoryQs, lFlightUsageRecordId );

      BigDecimal lExpectedCycleTSN = TRK_CURRENT_CYCLES.subtract( USAGE_CYCLES_DELTA );
      BigDecimal lExpectedHourTSN = TRK_CURRENT_HOURS.subtract( USAGE_HOURS_DELTA );

      // Assert that previous usage's TSN calculated correctly
      assertEquals( "Incorrect cycles encountered for TSN", lExpectedCycleTSN,
            lFlightUsage.get( lFlightUsageRecordId ).get( CYCLE_TSN ) );

      assertEquals( "Incorrect hours encountered for TSN", lExpectedHourTSN,
            lFlightUsage.get( lFlightUsageRecordId ).get( HOUR_TSN ) );

   }


   /**
    * <pre>
    * Given an Engine with two usage records - MXFLIGHT and MXACCRUAL
    *   and the engine was installed on an aircraft
    *   and a tracked component that was on the engine during the flight/usage
    *   and after the flight the engine was detached from the aircraft
    * When the UsageHistoryOfTrkSer query is executed against the tracked component
    * Then the query should return Correct calculated TSN values for MXFLIGHT
    * </pre>
    */
   @Test
   public void itReturnsPreviousTSNValueCorrectlyWhenComponentIsOnDetachedEngine() {

      final Date lEngineInstallDate = DateUtils.addDays( new Date(), -15 );
      final Date lTrkInstallDate = DateUtils.addDays( lEngineInstallDate, 1 );
      final Date lFlightDate = DateUtils.addDays( lEngineInstallDate, 5 );
      final Date lUsageDate = DateUtils.addDays( lFlightDate, 1 );
      final Date lEngineDetachDate = DateUtils.addDays( lUsageDate, 2 );

      final int lDayCount = 30;

      // Create Aircraft with Engine
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( HOURS, ACFT_CURRENT_HOURS );
            aAircraft.addUsage( CYCLES, ACFT_CURRENT_CYCLES );
         }
      } );

      final InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.addUsage( HOURS, ENGINE_CURRENT_HOURS );
            aEngine.addUsage( CYCLES, ENGINE_CURRENT_CYCLES );
         }
      } );

      // Create Trk Inventory
      final InventoryKey lTrkInventory = createTrkInventory( lEngine, lEngine, lTrkInstallDate );

      // Create installation and removal records for the engine
      Domain.createInstallationRecord( new DomainConfiguration<InstallationRecord>() {

         @Override
         public void configure( InstallationRecord aInstallationRecord ) {
            aInstallationRecord.setInventory( lEngine );
            aInstallationRecord.setInstallationDate( lEngineInstallDate );
            aInstallationRecord.setAssembly( lAircraft );
            aInstallationRecord.setHighest( lAircraft );
            aInstallationRecord.setParent( lAircraft );
            aInstallationRecord.addSubInventory(
                  new DomainConfiguration<InstallationRecord.InstalledInventoryInfo>() {

                     @Override
                     public void configure( InstalledInventoryInfo aInstalledInventoryInfo ) {
                        aInstalledInventoryInfo.setInventory( lTrkInventory );
                        aInstalledInventoryInfo.setAssembly( lEngine );
                        aInstalledInventoryInfo.setHighest( lAircraft );
                        aInstalledInventoryInfo.setParent( lEngine );
                     }
                  } );
         }
      } );

      Domain.createRemovalRecord( new DomainConfiguration<RemovalRecord>() {

         @Override
         public void configure( RemovalRecord aRemovalRecord ) {
            aRemovalRecord.setInventory( lEngine );
            aRemovalRecord.setRemovalDate( lEngineDetachDate );
            aRemovalRecord.setAssembly( lAircraft );
            aRemovalRecord.setHighest( lAircraft );
            aRemovalRecord.setParent( lAircraft );
            aRemovalRecord
                  .addSubInventory( new DomainConfiguration<RemovalRecord.RemovedInventoryInfo>() {

                     @Override
                     public void configure( RemovedInventoryInfo aRemovedInventoryInfo ) {
                        aRemovedInventoryInfo.setInventory( lTrkInventory );
                        aRemovedInventoryInfo.setAssembly( lEngine );
                        aRemovedInventoryInfo.setHighest( lAircraft );
                        aRemovedInventoryInfo.setParent( lEngine );
                     }
                  } );

         }
      } );

      // Create 2 usage records
      UsageAdjustmentId lFlightUsageRecordId =
            createUsageRecord( lEngine, lFlightDate, UsageType.MXFLIGHT );

      createUsageRecord( lEngine, lUsageDate, UsageType.MXACCRUAL );

      // run query
      QuerySet lUsageHistoryQs = executeUsageHistory( lTrkInventory, lDayCount, USAGE_PARAM_LIST );

      // retrieve flight usage record
      Map<UsageAdjustmentId, Map<String, BigDecimal>> lFlightUsage =
            retrieveUsage( lUsageHistoryQs, lFlightUsageRecordId );

      BigDecimal lExpectedCycleTSN = TRK_CURRENT_CYCLES.subtract( USAGE_CYCLES_DELTA );
      BigDecimal lExpectedHourTSN = TRK_CURRENT_HOURS.subtract( USAGE_HOURS_DELTA );

      // Assert that previous usage's TSN calculated correctly
      assertEquals( "Incorrect cycles encountered for TSN", lExpectedCycleTSN,
            lFlightUsage.get( lFlightUsageRecordId ).get( CYCLE_TSN ) );

      assertEquals( "Incorrect hours encountered for TSN", lExpectedHourTSN,
            lFlightUsage.get( lFlightUsageRecordId ).get( HOUR_TSN ) );

   }


   /**
    * <pre>
    * Given an Engine with a flight usage record
    *   and the engine was installed on an aircraft
    *   and a tracked component that was on the engine during the flight usage
    *   and the engine is removed from the aircraft
    *   and the tracked component is removed from the engine
    * When the UsageHistoryOfTrkSer query is executed for the tracked component
    * Then the query should return Correct calculated TSN values for MXFLIGHT, and not double-count the delta
    * </pre>
    */
   @Test
   public void itReturnsPreviousTSNValueCorrectlyWhenComponentIsRemovedFromDetachedEngine() {

      final Date lEngineInstallDate = DateUtils.addDays( new Date(), -15 );

      final Date lTrkInstallDate = DateUtils.addDays( lEngineInstallDate, 1 );

      final Date lUsageDate = DateUtils.addDays( lTrkInstallDate, 1 );

      final Date lEngineRemovalDate = DateUtils.addDays( lUsageDate, 1 );

      final Date lTrkRemovalDate = DateUtils.addDays( lEngineRemovalDate, 1 );

      final InventoryKey lEngine = createEngineWithRemoval( lEngineInstallDate, lEngineRemovalDate );

      // Create Aircraft with Engine
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( HOURS, ACFT_CURRENT_HOURS );
            aAircraft.addUsage( CYCLES, ACFT_CURRENT_CYCLES );
            aAircraft.addEngine( lEngine );
         }
      } );

      // Create Trk Inventory with install and removal
      final InventoryKey lTrkInventory =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aTrackedInventory ) {

                  aTrackedInventory.addUsage( HOURS, TRK_CURRENT_HOURS );
                  aTrackedInventory.addUsage( CYCLES, TRK_CURRENT_CYCLES );

                  aTrackedInventory
                        .addInstallationRecord( new DomainConfiguration<InstallationRecord>() {

                           @Override
                           public void configure( InstallationRecord aInstallationRecord ) {
                              aInstallationRecord.setInstallationDate( lTrkInstallDate );
                              aInstallationRecord.setAssembly( lEngine );
                              aInstallationRecord.setParent( lEngine );
                              aInstallationRecord.setHighest( lAircraft );
                              aInstallationRecord
                                    .addSubInventory( new DomainConfiguration<InstalledInventoryInfo>() {

                                       @Override
                                       public void configure(
                                             InstalledInventoryInfo aInstalledInventoryInfo ) {
                                          aInstalledInventoryInfo.setAssembly( lEngine );
                                          aInstalledInventoryInfo.setHighest( lAircraft );
                                       }
                                    } );
                           }
                        } );

                  aTrackedInventory.addRemovalRecord( new DomainConfiguration<RemovalRecord>() {

                     @Override
                     public void configure( RemovalRecord aRemovalRecord ) {

                        aRemovalRecord.setRemovalDate( lTrkRemovalDate );
                        aRemovalRecord.setHighest( lAircraft );
                        aRemovalRecord.setAssembly( lEngine );
                        aRemovalRecord.setParent( lEngine );

                        aRemovalRecord
                              .addSubInventory( new DomainConfiguration<RemovedInventoryInfo>() {

                                 @Override
                                 public void configure( RemovedInventoryInfo aRemovedInventoryInfo ) {
                                    aRemovedInventoryInfo.setAssembly( lEngine );
                                    aRemovedInventoryInfo.setHighest( lAircraft );
                                 }
                              } );
                     }
                  } );

               }
            } );

      final int lDayCount = 30;

      // Create usage
      UsageAdjustmentId lFlightUsageId =
            createUsageRecord( lEngine, lUsageDate, UsageType.MXFLIGHT );

      // run query
      QuerySet lUsageHistoryQs = executeUsageHistory( lTrkInventory, lDayCount, USAGE_PARAM_LIST );

      // check
      // three rows should be returned - one for installation, one for MXFLIGHT, and one for removal
      assertEquals( "Expected Row count should be 3", 3, lUsageHistoryQs.getRowCount() );

      // retrieve flight usage record
      Map<UsageAdjustmentId, Map<String, BigDecimal>> lFlightUsage =
            retrieveUsage( lUsageHistoryQs, lFlightUsageId );

      // assert actual values of usage
      assertEquals( "Incorrect cycles encountered for TSN", TRK_CURRENT_CYCLES,
            lFlightUsage.get( lFlightUsageId ).get( CYCLE_TSN ) );

      assertEquals( "Incorrect delta encountered for Cycles", USAGE_CYCLES_DELTA,
            lFlightUsage.get( lFlightUsageId ).get( CYCLE_DELTA ) );

      assertEquals( "Incorrect hours encountered for TSN", TRK_CURRENT_HOURS,
            lFlightUsage.get( lFlightUsageId ).get( HOUR_TSN ) );

      assertEquals( "Incorrect delta encountered for hours", USAGE_HOURS_DELTA,
            lFlightUsage.get( lFlightUsageId ).get( HOUR_DELTA ) );

   }


   /**
    * <pre>
    * Given an Engine with a flight usage record
    *   and the engine was installed on an aircraft
    *   and a tracked component that was not on the engine during the flight
    * When the UsageHistoryOfTrkSer query is executed for the tracked component
    * Then the query should not return any usage
    * </pre>
    */
   @Test
   public void itDoesNotReturnUsagesWhenThereIsNoUsageHistoryAfterComponentInstalledOnParent() {

      final Date lTrkInstallDate = DateUtils.addDays( new Date(), -6 );
      final Date lEngineInstallDate = DateUtils.addDays( lTrkInstallDate, -5 );

      final InventoryKey lEngine = createEngine( lEngineInstallDate );

      // Create Aircraft with Engine
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( HOURS, ACFT_CURRENT_HOURS );
            aAircraft.addUsage( CYCLES, ACFT_CURRENT_CYCLES );
            aAircraft.addEngine( lEngine );
         }
      } );

      // Create Trk Inventory
      final InventoryKey lTrkInventory = createTrkInventory( lAircraft, lEngine, lTrkInstallDate );

      final Date lUsageDate = DateUtils.addDays( lTrkInstallDate, -2 );
      final int lDayCount = 30;

      // Create usage
      UsageAdjustmentId lFlightUsageRecordId =
            createUsageRecord( lEngine, lUsageDate, UsageType.MXFLIGHT );

      // run query
      QuerySet lUsageHistoryQs = executeUsageHistory( lTrkInventory, lDayCount, USAGE_PARAM_LIST );

      // check flight not returned
      Map<UsageAdjustmentId, Map<String, BigDecimal>> lUsage =
            retrieveUsage( lUsageHistoryQs, lFlightUsageRecordId );
      assertNull( lUsage.get( lFlightUsageRecordId ) );
      // check only one installation record
      assertEquals( "Expected Row count should be 1", 1, lUsageHistoryQs.getRowCount() );
      assertEquals( "Only installation records should be returned", INSTALLATION,
            lUsageHistoryQs.getString( "display_name" ) );

   }


   /**
    * <pre>
    * Given an Engine with two usage records
    *   and the engine was installed on an aircraft
    *   and a tracked component that was not installed on the engine during the latest usage
    * When the UsageHistoryOfTrkSer query is executed for the tracked component
    * Then the query should not return the latest usage
    * </pre>
    */
   @Test
   public void itDoesNotReturnUsagesThatWasRecordedOnParentAfterComponentRemovalFromParent() {

      final int lDayCount = 30;
      final Date lTrkInstallDate = DateUtils.addDays( new Date(), -6 );
      final Date lEngineInstallDate = DateUtils.addDays( lTrkInstallDate, -6 );

      final Date lUsageDateBeforeTrkInventoryRemoval = DateUtils.addDays( lTrkInstallDate, 1 );
      final Date lTrkInventoryRemovalDate =
            DateUtils.addDays( lUsageDateBeforeTrkInventoryRemoval, 1 );

      final Date lAnotherTrkInventoryInstallDate = DateUtils.addDays( lTrkInventoryRemovalDate, 2 );

      final InventoryKey lEngine = createEngine( lEngineInstallDate );

      final InventoryKey lAnotherEngine = createEngine( lEngineInstallDate );

      // Create Aircraft with Engine
      Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( HOURS, ACFT_CURRENT_HOURS );
            aAircraft.addUsage( CYCLES, ACFT_CURRENT_CYCLES );
            aAircraft.addEngine( lAnotherEngine );
         }
      } );

      final InventoryKey lTrkInventory =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aTrkInventory ) {
                  aTrkInventory.addUsage( CYCLES, TRK_CURRENT_CYCLES );
                  aTrkInventory.addUsage( HOURS, TRK_CURRENT_HOURS );
                  aTrkInventory
                        .addInstallationRecord( new DomainConfiguration<InstallationRecord>() {

                           @Override
                           public void configure( InstallationRecord aBuilder ) {
                              aBuilder.setInstallationDate( lTrkInstallDate );
                              aBuilder.setAssembly( lEngine );
                           }
                        } );
                  aTrkInventory.addRemovalRecord( new DomainConfiguration<RemovalRecord>() {

                     @Override
                     public void configure( RemovalRecord aRemovalRecord ) {

                        aRemovalRecord.setRemovalDate( lTrkInventoryRemovalDate );
                        aRemovalRecord.setAssembly( lEngine );
                        aRemovalRecord.setHighest( lEngine );
                        aRemovalRecord.setParent( lEngine );
                     }

                  } );

                  aTrkInventory.setParent( lAnotherEngine );

               }
            } );

      // Create usage before TrkInventory's removal
      UsageAdjustmentId lFlightUsageRecordId =
            createUsageRecord( lEngine, lUsageDateBeforeTrkInventoryRemoval, UsageType.MXFLIGHT );

      Date lUsageDateAfterAnotherTrkInventoryInstall =
            DateUtils.addDays( lAnotherTrkInventoryInstallDate, 1 );

      // Create usage after another Trk Inventory's replacement of TrkInventory
      UsageAdjustmentId lUsageRecordId = createUsageRecord( lEngine,
            lUsageDateAfterAnotherTrkInventoryInstall, UsageType.MXACCRUAL );

      // run query

      QuerySet lUsageHistoryQs = executeUsageHistory( lTrkInventory, lDayCount, USAGE_PARAM_LIST );

      // assert
      // returns one installation, one removal and one usage record
      assertEquals( "Expected Row count should be 3", 3, lUsageHistoryQs.getRowCount() );

      // retrieve usage
      Map<UsageAdjustmentId, Map<String, BigDecimal>> lFlightUsage =
            retrieveUsage( lUsageHistoryQs, lFlightUsageRecordId );

      // assert usage for MXFLIGHT
      assertEquals( "Incorrect cycles encountered for TSN", TRK_CURRENT_CYCLES,
            lFlightUsage.get( lFlightUsageRecordId ).get( CYCLE_TSN ) );

      assertEquals( "Incorrect delta encountered for Cycles", USAGE_CYCLES_DELTA,
            lFlightUsage.get( lFlightUsageRecordId ).get( CYCLE_DELTA ) );

      assertEquals( "Incorrect hours encountered for TSN", TRK_CURRENT_HOURS,
            lFlightUsage.get( lFlightUsageRecordId ).get( HOUR_TSN ) );

      assertEquals( "Incorrect delta encountered for hours", USAGE_HOURS_DELTA,
            lFlightUsage.get( lFlightUsageRecordId ).get( HOUR_DELTA ) );

      // check usage not returned for MXACCRUAL
      Map<UsageAdjustmentId, Map<String, BigDecimal>> lUsage =
            retrieveUsage( lUsageHistoryQs, lUsageRecordId );
      assertNull( lUsage.get( lUsageRecordId ) );

   }


   /**
    * <pre>
    * Given an aircraft with an engine
    *   and engine has two tracked components: Trk and AnotherTrk installed
    *   and Trk has a tracked SubComponent installed
    *   and engine has a flight usage record after tracked SubComponent installed on Trk
    *   and after the flight usage the Trk and its SubComponent are removed from and Subcomponent is installed on Another Trk
    *   and then the engine has another flight usage record after tracked SubComponent installed on Another Trk
    * When the UsageHistoryOfTrkSer query is executed for the tracked SubComponent
    * Then the query should return two flight usages
    * </pre>
    */
   @Test
   public void
         itReturnsUsagesForSubComponentWhenSubComponentInstalledAndRemovedFromSameEngineAndEngineHasUsagesAfterEachSubComponentInstallations() {

      final int lDayCount = 30;

      final Date lEngineInstallDate = DateUtils.addDays( new Date(), -15 );
      final Date lTrkInstallDate = DateUtils.addDays( lEngineInstallDate, 1 );
      final Date lSubCompRemovalFromTrkDate = DateUtils.addDays( lTrkInstallDate, 2 );
      final Date lUsageDateBeforeRemovalOfSubCompFromTrk =
            DateUtils.addDays( lSubCompRemovalFromTrkDate, -1 );

      final Date lAnotherTrkInstallDate = DateUtils.addDays( lTrkInstallDate, 5 );
      final Date lSubCompRemovalFromAnotherTrkDate = DateUtils.addDays( lAnotherTrkInstallDate, 2 );

      final Date lUsageDateBeforeRemovalOfSubCompFromAnotherTrk =
            DateUtils.addDays( lSubCompRemovalFromAnotherTrkDate, -1 );

      // Create engine
      final InventoryKey lEngine = createEngine( lEngineInstallDate );

      // Create Aircraft with Engine
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( HOURS, ACFT_CURRENT_HOURS );
            aAircraft.addUsage( CYCLES, ACFT_CURRENT_CYCLES );
            aAircraft.addEngine( lEngine );
         }
      } );

      // Create Another TRK inventory
      final InventoryKey lAnotherTrkInventory =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aTrackedInventory ) {
                  aTrackedInventory.setParent( lEngine );
                  aTrackedInventory.addUsage( HOURS, TRK_CURRENT_HOURS );
                  aTrackedInventory.addUsage( CYCLES, TRK_CURRENT_CYCLES );
                  aTrackedInventory
                        .addInstallationRecord( new DomainConfiguration<InstallationRecord>() {

                           @Override
                           public void configure( InstallationRecord aInstallationRecord ) {
                              aInstallationRecord.setInstallationDate( lTrkInstallDate );
                              aInstallationRecord.setAssembly( lEngine );
                              aInstallationRecord.setParent( lEngine );
                              aInstallationRecord.setHighest( lAircraft );

                           }
                        } );

               }
            } );

      // Create Subcomponent
      final InventoryKey lSubCompInventory =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aTrackedInventory ) {
                  aTrackedInventory.addUsage( HOURS, SUB_COMP_CURRENT_HOURS );
                  aTrackedInventory.addUsage( CYCLES, SUB_COMP_CURRENT_CYCLES );
                  aTrackedInventory.setParent( lAnotherTrkInventory );

                  aTrackedInventory
                        .addInstallationRecord( new DomainConfiguration<InstallationRecord>() {

                           @Override
                           public void configure( InstallationRecord aInstallationRecord ) {
                              aInstallationRecord.setInstallationDate( lAnotherTrkInstallDate );
                              aInstallationRecord.setAssembly( lEngine );
                              aInstallationRecord.setParent( lAnotherTrkInventory );
                              aInstallationRecord.setHighest( lAircraft );
                           }
                        } );
               }
            } );

      // Create Trk inventory

      final InventoryKey lTrkInventory =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aTrackedInventory ) {

                  aTrackedInventory.addUsage( HOURS, TRK_CURRENT_HOURS );
                  aTrackedInventory.addUsage( CYCLES, TRK_CURRENT_CYCLES );

                  aTrackedInventory
                        .addInstallationRecord( new DomainConfiguration<InstallationRecord>() {

                           @Override
                           public void configure( InstallationRecord aInstallationRecord ) {
                              aInstallationRecord.setInstallationDate( lTrkInstallDate );
                              aInstallationRecord.setAssembly( lEngine );
                              aInstallationRecord.setParent( lEngine );
                              aInstallationRecord.setHighest( lAircraft );
                              aInstallationRecord.addSubInventory(
                                    new DomainConfiguration<InstalledInventoryInfo>() {

                                       @Override
                                       public void configure(
                                             InstalledInventoryInfo aInstalledInventoryInfo ) {

                                          // This Trk is auto set as the parent for the sub
                                          // component by the builders
                                          aInstalledInventoryInfo.setInventory( lSubCompInventory );
                                          aInstalledInventoryInfo.setAssembly( lEngine );
                                          aInstalledInventoryInfo.setHighest( lAircraft );
                                       }
                                    } );
                           }
                        } );

                  aTrackedInventory.addRemovalRecord( new DomainConfiguration<RemovalRecord>() {

                     @Override
                     public void configure( RemovalRecord aRemovalRecord ) {

                        aRemovalRecord.setRemovalDate( lSubCompRemovalFromTrkDate );
                        aRemovalRecord.setHighest( lAircraft );
                        aRemovalRecord.setAssembly( lEngine );
                        aRemovalRecord.setParent( lEngine );

                        aRemovalRecord
                              .addSubInventory( new DomainConfiguration<RemovedInventoryInfo>() {

                                 @Override
                                 public void
                                       configure( RemovedInventoryInfo aRemovedInventoryInfo ) {
                                    aRemovedInventoryInfo.setInventory( lSubCompInventory );
                                    aRemovedInventoryInfo.setAssembly( lEngine );
                                    aRemovedInventoryInfo.setHighest( lAircraft );
                                 }
                              } );
                     }
                  } );

               }
            } );

      Domain.createRemovalRecord( new DomainConfiguration<RemovalRecord>() {

         @Override
         public void configure( RemovalRecord aRemovalRecord ) {
            aRemovalRecord.setInventory( lSubCompInventory );
            aRemovalRecord.setRemovalDate( lSubCompRemovalFromTrkDate );
            aRemovalRecord.setHighest( lTrkInventory );
            aRemovalRecord.setParent( lTrkInventory );
         }
      } );

      // create a usage record for Engine with usage date between the install and removal of
      // Sub Component from Trk
      createUsageRecord( lEngine, lUsageDateBeforeRemovalOfSubCompFromTrk, UsageType.MXFLIGHT );

      // create a usage record for Engine with usage date between the install and removal
      // of Sub Component from Another Trk
      createUsageRecord( lEngine, lUsageDateBeforeRemovalOfSubCompFromAnotherTrk,
            UsageType.MXFLIGHT );

      QuerySet lUsageHistoryQs =
            executeUsageHistory( lSubCompInventory, lDayCount, USAGE_PARAM_LIST );

      // Assert
      assertEquals( "Expected Row count should be 5", 5, lUsageHistoryQs.getRowCount() );

   }


   /**
    * <pre>
    * Given an aircraft with an engine
    *   and engine has Trk component installed
    *   and Trk has a tracked SubComponent installed
    * When the UsageHistoryOfTrkSer query is executed for the tracked SubComponent
    * Then the query should return one installation record
    * </pre>
    */
   @Test
   public void itReturnsInstallationRecord() {

      final int lDayCount = 30;

      final Date lEngineInstallDate = DateUtils.addDays( new Date(), -15 );
      final Date lTrkInstallDate = DateUtils.addDays( lEngineInstallDate, 1 );

      // Create engine
      final InventoryKey lEngine = createEngine( lEngineInstallDate );

      // Create Aircraft with Engine
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( HOURS, ACFT_CURRENT_HOURS );
            aAircraft.addUsage( CYCLES, ACFT_CURRENT_CYCLES );
            aAircraft.addEngine( lEngine );
         }
      } );

      // Create Subcomponent
      final InventoryKey lSubCompInventory =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aTrackedInventory ) {
                  aTrackedInventory.addUsage( HOURS, SUB_COMP_CURRENT_HOURS );
                  aTrackedInventory.addUsage( CYCLES, SUB_COMP_CURRENT_CYCLES );
               }
            } );

      // Create Trk inventory

      Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

         @Override
         public void configure( TrackedInventory aTrackedInventory ) {
            aTrackedInventory.setParent( lEngine );
            aTrackedInventory.addUsage( HOURS, TRK_CURRENT_HOURS );
            aTrackedInventory.addUsage( CYCLES, TRK_CURRENT_CYCLES );
            aTrackedInventory.addTracked( lSubCompInventory );
            aTrackedInventory.addInstallationRecord( new DomainConfiguration<InstallationRecord>() {

               @Override
               public void configure( InstallationRecord aInstallationRecord ) {
                  aInstallationRecord.setInstallationDate( lTrkInstallDate );
                  aInstallationRecord.setAssembly( lEngine );
                  aInstallationRecord.setParent( lEngine );
                  aInstallationRecord.setHighest( lAircraft );
                  aInstallationRecord
                        .addSubInventory( new DomainConfiguration<InstalledInventoryInfo>() {

                           @Override
                           public void configure( InstalledInventoryInfo aInstalledInventoryInfo ) {

                              // This Trk is auto set as the parent for the sub
                              // component by the builders
                              aInstalledInventoryInfo.setInventory( lSubCompInventory );
                              aInstalledInventoryInfo.setAssembly( lEngine );
                              aInstalledInventoryInfo.setHighest( lAircraft );
                           }
                        } );
               }
            } );

         }
      } );

      QuerySet lUsageHistoryQs =
            executeUsageHistory( lSubCompInventory, lDayCount, USAGE_PARAM_LIST );

      // Assert
      assertEquals( "Expected Row count should be 1", 1, lUsageHistoryQs.getRowCount() );
      lUsageHistoryQs.next();
      assertEquals( "Expected Installation record", INSTALLATION,
            lUsageHistoryQs.getString( "display_name" ) );

   }


   /**
    * <pre>
    * Given an Engine that was installed on an aircraft
    *   and a tracked component that was on the engine
    * When the UsageHistoryOfTrkSer query is executed for the tracked component with daycount before installation
    * Then the query should return an Installation for the TRK, but no Installation for the Assembly to the aircraft
    * </pre>
    */
   @Test
   public void itDoesNotReturnInstallRecordsForAssemblyToAircraft() throws Exception {

      final Date lEngineInstallDate = DateUtils.addDays( new Date(), -15 );

      final Date lTrkInstallDate = DateUtils.addDays( lEngineInstallDate, 1 );

      final InventoryKey lEngine = createEngine( lEngineInstallDate );

      // Create Aircraft with Engine
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( HOURS, ACFT_CURRENT_HOURS );
            aAircraft.addUsage( CYCLES, ACFT_CURRENT_CYCLES );
            aAircraft.addEngine( lEngine );
         }
      } );

      // Create Trk Inventory
      final InventoryKey lTrkInventory = createTrkInventory( lAircraft, lEngine, lTrkInstallDate );

      final int lDayCount = 20;

      // run query
      QuerySet lUsageHistoryQs = executeUsageHistory( lTrkInventory, lDayCount, USAGE_PARAM_LIST );

      // Two records are within DayCount, but one is for the installation of the assembly to the
      // aircraft
      assertEquals( "One Installation record should be visible", 1, lUsageHistoryQs.getRowCount() );
      lUsageHistoryQs.next();
      assertEquals( "Expected Installation record", INSTALLATION,
            lUsageHistoryQs.getString( "display_name" ) );
   }


   /**
    * <pre>
    * Given an Engine with multiple flight usage records
    *   and the engine was installed on an aircraft
    *   and a tracked component that was on the engine during the flights
    * When the UsageHistoryOfTrkSer query is executed for the tracked component with daycount after installation
    * Then the query should return an MXFLIGHT, and MXCORRECTION, but no Installation
    * </pre>
    */
   @Test
   public void itDoesNotReturnInstallRecordIfOutsideTimePeriod() throws Exception {

      final Date lEngineInstallDate = DateUtils.addDays( new Date(), -15 );

      final Date lTrkInstallDate = DateUtils.addDays( lEngineInstallDate, 1 );

      final InventoryKey lEngine = createEngine( lEngineInstallDate );

      // Create Aircraft with Engine
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( HOURS, ACFT_CURRENT_HOURS );
            aAircraft.addUsage( CYCLES, ACFT_CURRENT_CYCLES );
            aAircraft.addEngine( lEngine );
         }
      } );

      // Create Trk Inventory
      final InventoryKey lTrkInventory = createTrkInventory( lAircraft, lEngine, lTrkInstallDate );

      final int lDayCount = 13;

      // run query
      QuerySet lUsageHistoryQs = executeUsageHistory( lTrkInventory, lDayCount, USAGE_PARAM_LIST );

      // No records are within the DayCount
      assertEquals( "No records should be visible", 0, lUsageHistoryQs.getRowCount() );
   }


   /**
    * <pre>
    * Given a loose Trk component
    * And a tracked SubComponent installed to the Trk component
    * When the UsageHistoryOfTrkSer query is executed for the tracked SubComponent
    * Then the query should not return any record
    * </pre>
    */
   @Test
   public void itDoesNotReturnInstallationRecordWhenSubcomponentAttachedToLooseComponent() {

      final int lDayCount = 30;
      final Date lTrkSubcomponentInstallDate = DateUtils.addDays( new Date(), -15 );

      // Create Trk inventory
      final InventoryKey lTrkInventory =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aTrackedInventory ) {
                  aTrackedInventory.addUsage( HOURS, TRK_CURRENT_HOURS );
                  aTrackedInventory.addUsage( CYCLES, TRK_CURRENT_CYCLES );
               }
            } );

      // Create Subcomponent
      final InventoryKey lSubCompInventory =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aTrackedInventory ) {
                  aTrackedInventory.addUsage( HOURS, SUB_COMP_CURRENT_HOURS );
                  aTrackedInventory.addUsage( CYCLES, SUB_COMP_CURRENT_CYCLES );
                  aTrackedInventory.setParent( lTrkInventory );
                  aTrackedInventory
                        .addInstallationRecord( new DomainConfiguration<InstallationRecord>() {

                           @Override
                           public void configure( InstallationRecord aInstallationRecord ) {
                              aInstallationRecord
                                    .setInstallationDate( lTrkSubcomponentInstallDate );
                              aInstallationRecord.setParent( lTrkInventory );
                           }
                        } );
               }
            } );

      QuerySet lUsageHistoryQs =
            executeUsageHistory( lSubCompInventory, lDayCount, USAGE_PARAM_LIST );

      // Assert
      assertEquals( "Expected Row count should be 0", 0, lUsageHistoryQs.getRowCount() );

   }


   /**
    * <pre>
    * Given an aircraft with an engine
    *   and Trk with tracked SubComponent removed from Engine
    * When the UsageHistoryOfTrkSer query is executed for the tracked SubComponent
    * Then the query should return one removal record
    * </pre>
    */
   @Test
   public void itReturnsRemovalRecord() {

      final int lDayCount = 30;

      final Date lEngineInstallDate = DateUtils.addDays( new Date(), -15 );
      final Date lTrkInstallDate = DateUtils.addDays( lEngineInstallDate, 1 );
      final Date lSubCompRemovalFromTrkDate = DateUtils.addDays( lTrkInstallDate, 2 );

      // Create engine
      final InventoryKey lEngine = createEngine( lEngineInstallDate );

      // Create Aircraft with Engine
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( HOURS, ACFT_CURRENT_HOURS );
            aAircraft.addUsage( CYCLES, ACFT_CURRENT_CYCLES );
            aAircraft.addEngine( lEngine );
         }
      } );

      // Create Subcomponent
      final InventoryKey lSubCompInventory =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aTrackedInventory ) {
                  aTrackedInventory.addUsage( HOURS, SUB_COMP_CURRENT_HOURS );
                  aTrackedInventory.addUsage( CYCLES, SUB_COMP_CURRENT_CYCLES );
               }
            } );

      // Create Trk inventory

      Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

         @Override
         public void configure( TrackedInventory aTrackedInventory ) {

            aTrackedInventory.addUsage( HOURS, TRK_CURRENT_HOURS );
            aTrackedInventory.addUsage( CYCLES, TRK_CURRENT_CYCLES );
            aTrackedInventory.addTracked( lSubCompInventory );
            aTrackedInventory.addRemovalRecord( new DomainConfiguration<RemovalRecord>() {

               @Override
               public void configure( RemovalRecord aRemovalRecord ) {

                  aRemovalRecord.setRemovalDate( lSubCompRemovalFromTrkDate );
                  aRemovalRecord.setHighest( lAircraft );
                  aRemovalRecord.setAssembly( lEngine );
                  aRemovalRecord.setParent( lEngine );

                  aRemovalRecord.addSubInventory( new DomainConfiguration<RemovedInventoryInfo>() {

                     @Override
                     public void configure( RemovedInventoryInfo aRemovedInventoryInfo ) {
                        aRemovedInventoryInfo.setInventory( lSubCompInventory );
                        aRemovedInventoryInfo.setAssembly( lEngine );
                        aRemovedInventoryInfo.setHighest( lAircraft );
                     }
                  } );
               }
            } );
         }
      } );

      QuerySet lUsageHistoryQs =
            executeUsageHistory( lSubCompInventory, lDayCount, USAGE_PARAM_LIST );

      // Assert
      assertEquals( "Expected Row count should be 1", 1, lUsageHistoryQs.getRowCount() );
      lUsageHistoryQs.next();
      assertEquals( "Expected Removal record", REMOVAL,
            lUsageHistoryQs.getString( "display_name" ) );
   }


   /**
    * <pre>
    * Given a loose Trk component with a tracked SubComponent
    * And The Subcomponent Trk removed from the Trk Component
    * When the UsageHistoryOfTrkSer query is executed for the tracked SubComponent
    * Then the query should not return any record
    * </pre>
    */
   @Test
   public void itDoesNotReturnRemovalRecordWhenSubcomponentDetachedFromLooseComponent() {

      final int lDayCount = 30;
      final Date lSubCompRemovalFromTrkDate = DateUtils.addDays( new Date(), -15 );

      // Create Trk inventory
      final InventoryKey lTrkInventory =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aTrackedInventory ) {
                  aTrackedInventory.addUsage( HOURS, TRK_CURRENT_HOURS );
                  aTrackedInventory.addUsage( CYCLES, TRK_CURRENT_CYCLES );
               }
            } );

      // Create Subcomponent
      final InventoryKey lSubCompInventory =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aTrackedInventory ) {
                  aTrackedInventory.addUsage( HOURS, SUB_COMP_CURRENT_HOURS );
                  aTrackedInventory.addUsage( CYCLES, SUB_COMP_CURRENT_CYCLES );
                  aTrackedInventory.setParent( lTrkInventory );

                  aTrackedInventory.addRemovalRecord( new DomainConfiguration<RemovalRecord>() {

                     @Override
                     public void configure( RemovalRecord aRemovalRecord ) {
                        aRemovalRecord.setRemovalDate( lSubCompRemovalFromTrkDate );
                        aRemovalRecord.setParent( lTrkInventory );
                     }
                  } );
               }
            } );

      QuerySet lUsageHistoryQs =
            executeUsageHistory( lSubCompInventory, lDayCount, USAGE_PARAM_LIST );

      // Assert
      assertEquals( "Expected Row count should be 0", 0, lUsageHistoryQs.getRowCount() );
   }


   /**
    * <pre>
    * Given an Engine with a Edit Inventory usage records
    * When the UsageHistoryOfTrkSer query is executed for the Engine
    * Then the query should return the Edit Inventory usage records with TSN DELTA
    * </pre>
    */
   @Test
   public void itReturnTsnDeltaInEditInventoryRecord() throws Exception {

      final Date lEngineInstallDate = DateUtils.addDays( new Date(), -15 );

      final InventoryKey lEngine = createEngine( lEngineInstallDate );

      final Date lUsageDate = DateUtils.addDays( lEngineInstallDate, 5 );

      final int lDayCount = 30;

      // Create usage
      UsageAdjustmentId lFlightUsageId =
            createUsageRecord( lEngine, lUsageDate, UsageType.MXCORRECTION );

      // run query
      QuerySet lUsageHistoryQs = executeUsageHistory( lEngine, lDayCount, USAGE_PARAM_LIST );

      // check
      // two rows should be returned - one for installation and another for MXFLIGHT
      assertEquals( "Expected Row count should be 1", 1, lUsageHistoryQs.getRowCount() );

      // retrieve flight usage record
      Map<UsageAdjustmentId, Map<String, BigDecimal>> lFlightUsage =
            retrieveUsage( lUsageHistoryQs, lFlightUsageId );

      // assert actual values of usage
      assertEquals( "Incorrect cycles encountered for TSN", USAGE_CYCLES,
            lFlightUsage.get( lFlightUsageId ).get( CYCLE_TSN ) );

      assertEquals( "Incorrect delta encountered for Cycles", USAGE_CYCLES_DELTA,
            lFlightUsage.get( lFlightUsageId ).get( CYCLE_DELTA ) );

      assertEquals( "Incorrect hours encountered for TSN", USAGE_HOURS,
            lFlightUsage.get( lFlightUsageId ).get( HOUR_TSN ) );

      assertEquals( "Incorrect delta encountered for hours", USAGE_HOURS_DELTA,
            lFlightUsage.get( lFlightUsageId ).get( HOUR_DELTA ) );
   }


   // helper method to retrieve a specific usage record based on usageAdjustmentId
   private Map<UsageAdjustmentId, Map<String, BigDecimal>> retrieveUsage( QuerySet aUsageHistoryQs,
         UsageAdjustmentId aUsageAdjustmentId ) {

      Map<UsageAdjustmentId, Map<String, BigDecimal>> lUsages =
            new HashMap<UsageAdjustmentId, Map<String, BigDecimal>>();

      while ( aUsageHistoryQs.next() ) {

         if ( aUsageHistoryQs.getId( UsageAdjustmentId.class, "usage_record_id" ) != null
               && aUsageHistoryQs.getId( UsageAdjustmentId.class, "usage_record_id" )
                     .equals( aUsageAdjustmentId ) ) {
            if ( !lUsages.containsKey( aUsageAdjustmentId ) ) {
               lUsages.put( aUsageAdjustmentId, new HashMap<String, BigDecimal>() );
            }

            Map<String, BigDecimal> lValues = lUsages.get( aUsageAdjustmentId );
            lValues.put( CYCLE_TSN, aUsageHistoryQs.getBigDecimal( CYCLE_TSN ) );
            lValues.put( CYCLE_DELTA, aUsageHistoryQs.getBigDecimal( CYCLE_DELTA ) );
            lValues.put( HOUR_TSN, aUsageHistoryQs.getBigDecimal( HOUR_TSN ) );
            lValues.put( HOUR_DELTA, aUsageHistoryQs.getBigDecimal( HOUR_DELTA ) );

         }

      }
      return lUsages;
   }


   private UsageAdjustmentId createUsageRecord( final InventoryKey aInventoryKey,
         final Date aUsageDate, final UsageType aUsageType ) {

      final UsageAdjustmentId lUsageRecordId =
            Domain.createUsageAdjustment( new DomainConfiguration<UsageAdjustment>() {

               @Override
               public void configure( UsageAdjustment aUsageAdjustment ) {
                  aUsageAdjustment.setMainInventory( aInventoryKey );
                  aUsageAdjustment.addUsage( aInventoryKey, CYCLES, USAGE_CYCLES,
                        USAGE_CYCLES_DELTA );
                  aUsageAdjustment.addUsage( aInventoryKey, HOURS, USAGE_HOURS, USAGE_HOURS_DELTA );
                  aUsageAdjustment.setUsageDate( aUsageDate );
                  aUsageAdjustment.setUsageType( aUsageType );

               }
            } );

      return lUsageRecordId;
   }


   private InventoryKey createEngine( final Date aEngineInstallDate ) {

      final InventoryKey lEngineKey = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.addUsage( HOURS, ENGINE_CURRENT_HOURS );
            aEngine.addUsage( CYCLES, ENGINE_CURRENT_CYCLES );
            aEngine.addInstallationRecord( new DomainConfiguration<InstallationRecord>() {

               @Override
               public void configure( InstallationRecord aInstallationRecord ) {
                  aInstallationRecord.setInstallationDate( aEngineInstallDate );
               }
            } );
         }
      } );

      return lEngineKey;

   }


   private InventoryKey createEngineWithRemoval( final Date aEngineInstallDate,
         final Date aEngineRemovalDate ) {

      final InventoryKey lEngineKey = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.addUsage( HOURS, ENGINE_CURRENT_HOURS );
            aEngine.addUsage( CYCLES, ENGINE_CURRENT_CYCLES );
            aEngine.addInstallationRecord( new DomainConfiguration<InstallationRecord>() {

               @Override
               public void configure( InstallationRecord aInstallationRecord ) {
                  aInstallationRecord.setInstallationDate( aEngineInstallDate );
               }
            } );
            aEngine.addRemovalRecord( new DomainConfiguration<RemovalRecord>() {

               @Override
               public void configure( RemovalRecord aRemovalRecord ) {
                  aRemovalRecord.setRemovalDate( aEngineRemovalDate );
               }

            } );
         }
      } );

      return lEngineKey;

   }


   private InventoryKey createTrkInventory( final InventoryKey aHighestInventory,
         final InventoryKey aEngine, final Date aInstallDate ) {
      InventoryKey lTrkInventory =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aTrkInventory ) {
                  aTrkInventory.setParent( aEngine );
                  aTrkInventory.addUsage( CYCLES, TRK_CURRENT_CYCLES );
                  aTrkInventory.addUsage( HOURS, TRK_CURRENT_HOURS );
                  aTrkInventory
                        .addInstallationRecord( new DomainConfiguration<InstallationRecord>() {

                           @Override
                           public void configure( InstallationRecord aInstallationRecord ) {
                              aInstallationRecord.setInstallationDate( aInstallDate );
                              aInstallationRecord.setAssembly( aEngine );
                              aInstallationRecord.setHighest( aHighestInventory );
                              aInstallationRecord.setParent( aEngine );
                           }
                        } );
               }
            } );
      return lTrkInventory;
   }


   private QuerySet executeUsageHistory( InventoryKey aInventory, int aDayCount,
         String aUsageParamList ) {

      // Build the query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aInventory, "aInventoryDbId", "aInventoryId" );
      lArgs.add( "aDayCount", aDayCount );
      lArgs.addSelect( "SELECT_VALUE", aUsageParamList );

      // Execute the query
      return QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

   }

}
