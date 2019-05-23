package com.mxi.mx.core.services.event.inventory;

import static com.mxi.mx.core.key.DataTypeKey.CYCLES;
import static com.mxi.mx.core.key.DataTypeKey.HOURS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.UsageAdjustment;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.InventoryKey;


/**
 * Tests the UsageSnapshotService functionality
 *
 */
public class UsageSnapshotServiceTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   public static final double DELTA = 1e-15;
   public static final BigDecimal ZERO = new BigDecimal( 0.0 );
   public static final BigDecimal CYCLES_DELTA = new BigDecimal( 1.0 );
   public static final BigDecimal HOURS_DELTA = new BigDecimal( 5.0 );

   InventoryKey iAircraftKey = null;
   InventoryKey iEngineKey = null;

   final Date TODAY = new Date();
   final Date FIVE_DAYS_AGO = DateUtils.addDays( TODAY, -5 );
   final Date THREE_DAYS_AGO = DateUtils.addDays( TODAY, -3 );

   final BigDecimal CYCLES_TODAY = new BigDecimal( 3 );
   final BigDecimal CYCLES_THREE_DAYS_AGO = new BigDecimal( 2 );
   final BigDecimal CYCLES_FIVE_DAYS_AGO = new BigDecimal( 1 );

   final BigDecimal HOURS_TODAY = new BigDecimal( 15 );
   final BigDecimal HOURS_THREE_DAYS_AGO = new BigDecimal( 10 );
   final BigDecimal HOURS_FIVE_DAYS_AGO = new BigDecimal( 5 );


   /**
    *
    * Tests that historical usage is properly returned when the date is equal to the date of the
    * usage record
    *
    */
   @Test
   public void testHistoricalUsageOnTheSameDay() {

      List<UsageSnapshot> lUsageSnapshot = new ArrayList<UsageSnapshot>(
            UsageSnapshotService.getUsageAtDate( iAircraftKey, FIVE_DAYS_AGO ) );

      assertSnapshotContains( lUsageSnapshot, CYCLES_FIVE_DAYS_AGO, HOURS_FIVE_DAYS_AGO );

   }


   /**
    *
    * Tests that the correct usage is returned when the date is between two usage records
    *
    */
   @Test
   public void testHistoricalUsageOneMinuteAfter() {

      Date lOneMinuteBefore = DateUtils.addMinutes( THREE_DAYS_AGO, -1 );
      List<UsageSnapshot> lUsageSnapshot =
            UsageSnapshotService.getUsageAtDate( iAircraftKey, lOneMinuteBefore );

      assertSnapshotContains( lUsageSnapshot, CYCLES_FIVE_DAYS_AGO, HOURS_FIVE_DAYS_AGO );
   }


   /**
    *
    * Tests that current usage is properly returned
    *
    */
   @Test
   public void testCurrentUsage() {

      List<UsageSnapshot> lUsageSnapshot =
            new ArrayList<UsageSnapshot>( UsageSnapshotService.getCurrentUsage( iAircraftKey ) );

      assertSnapshotContains( lUsageSnapshot, CYCLES_TODAY, HOURS_TODAY );
   }


   /**
    *
    * Tests that an empty list is returned when an inventory has no usage parameters
    *
    */
   @Test
   public void testInventoryWithNoUsageParameters() {

      // create a fake inventory that has no usage
      InventoryKey lInventoryKey = new InventoryKey( "4560:9999" );
      List<UsageSnapshot> lUsageSnapshot1 = new ArrayList<UsageSnapshot>(
            UsageSnapshotService.getUsageAtDate( lInventoryKey, FIVE_DAYS_AGO ) );

      assertTrue( lUsageSnapshot1.isEmpty() );

      List<UsageSnapshot> lUsageSnapshot2 =
            new ArrayList<UsageSnapshot>( UsageSnapshotService.getCurrentUsage( lInventoryKey ) );

      assertTrue( lUsageSnapshot2.isEmpty() );
   }


   /**
    *
    * Tests that a list of usage snapshots with all zero values is returned when there is no usage
    * record for the inventory
    *
    */
   @Test
   public void testInventoryWithNoUsageValues() {

      // create a date that has no usage values
      Date aSixDaysAgo = DateUtils.addDays( FIVE_DAYS_AGO, -1 );

      List<UsageSnapshot> lUsageSnapshot = new ArrayList<UsageSnapshot>(
            UsageSnapshotService.getUsageAtDate( iAircraftKey, aSixDaysAgo ) );

      assertFalse( lUsageSnapshot.isEmpty() );
      assertEquals( lUsageSnapshot.size(), 2 );
      assertEquals( lUsageSnapshot.get( 0 ).getTSI(), 0, DELTA );
      assertEquals( lUsageSnapshot.get( 0 ).getTSN(), 0, DELTA );
      assertEquals( lUsageSnapshot.get( 0 ).getTSO(), 0, DELTA );

   }


   /**
    * Asserts the cycles and hours usage snapshot values match the expected values
    *
    * @param aUsageSnapshotArray
    * @param aExpectedCycles
    * @param aExpectedHours
    *
    */
   private void assertSnapshotContains( List<UsageSnapshot> aUsageSnapshotArray,
         BigDecimal aExpectedCycles, BigDecimal aExpectedHours ) {

      assertFalse( aUsageSnapshotArray.isEmpty() );

      for ( UsageSnapshot aUsageSnapshot : aUsageSnapshotArray ) {

         DataTypeKey lDataType = aUsageSnapshot.getDataType();

         if ( CYCLES.equals( lDataType ) ) {
            assertEquals( "Unexpected Cycles TSN", aExpectedCycles.doubleValue(),
                  aUsageSnapshot.getTSN(), DELTA );
            assertEquals( "Unexpected Cyles TSO", aExpectedCycles.doubleValue(),
                  aUsageSnapshot.getTSO(), DELTA );
            assertEquals( "Unexpected Cycles TSI", aExpectedCycles.doubleValue(),
                  aUsageSnapshot.getTSI(), DELTA );

         } else if ( HOURS.equals( lDataType ) ) {
            assertEquals( "Unexpected Hours TSN", aExpectedHours.doubleValue(),
                  aUsageSnapshot.getTSN(), DELTA );
            assertEquals( "Unexpected Hours TSN", aExpectedHours.doubleValue(),
                  aUsageSnapshot.getTSO(), DELTA );
            assertEquals( "Unexpected Hours TSN", aExpectedHours.doubleValue(),
                  aUsageSnapshot.getTSI(), DELTA );

         } else {
            fail( "Unexpected data type returned: " + lDataType );

         }
      }

   }


   /**
    *
    * Create an aircraft with current and historic usage
    *
    */
   @Before
   public void setup() {

      // Create an aircraft
      iAircraftKey = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( CYCLES, CYCLES_TODAY );
            aAircraft.addUsage( HOURS, HOURS_TODAY );
         }
      } );

      Domain.createUsageAdjustment( new DomainConfiguration<UsageAdjustment>() {

         @Override
         public void configure( UsageAdjustment aUsageRecord ) {
            aUsageRecord.setMainInventory( iAircraftKey );
            aUsageRecord.setUsageDate( FIVE_DAYS_AGO );
            aUsageRecord.addUsage( iAircraftKey, CYCLES, CYCLES_FIVE_DAYS_AGO, CYCLES_DELTA );
            aUsageRecord.addUsage( iAircraftKey, HOURS, HOURS_FIVE_DAYS_AGO, HOURS_DELTA );
         }
      } );

      // add a usage record
      Domain.createUsageAdjustment( new DomainConfiguration<UsageAdjustment>() {

         @Override
         public void configure( UsageAdjustment aUsageRecord ) {
            aUsageRecord.setMainInventory( iAircraftKey );
            aUsageRecord.setUsageDate( THREE_DAYS_AGO );
            aUsageRecord.addUsage( iAircraftKey, CYCLES, CYCLES_THREE_DAYS_AGO, CYCLES_DELTA );
            aUsageRecord.addUsage( iAircraftKey, HOURS, HOURS_THREE_DAYS_AGO, HOURS_DELTA );
         }
      } );

   }

};
