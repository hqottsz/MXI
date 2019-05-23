
package com.mxi.mx.web.query.todolist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.core.common.ejb.dao.PlainBatchStatement;
import com.mxi.mx.core.common.services.dao.MxBatchDataAccess;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;


/**
 * Tests the PendingExpiry query
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class PendingExpiryTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   /** The relative date the query is based off of */
   private static final Calendar INITIAL_DATE;

   static {
      INITIAL_DATE = Calendar.getInstance();
      INITIAL_DATE.set( 2011, Calendar.APRIL, 28 );
   }

   /** Accessible to Location 1 */
   private static final HumanResourceKey HR_1_KEY = new HumanResourceKey( 2000, 1 );

   /** Accessible to Location 2 (QUAR) */
   private static final HumanResourceKey HR_2_KEY = new HumanResourceKey( 2000, 2 );

   /** Accessible to Location 3 */
   private static final HumanResourceKey HR_3_KEY = new HumanResourceKey( 2000, 3 );

   /** Base Inventory */
   private static final InventoryKey INVENTORY_1_KEY = new InventoryKey( 2000, 1 );

   /** Inventory with Future Expiry Date */
   private static final InventoryKey INVENTORY_2_KEY = new InventoryKey( 2000, 2 );

   /** Inventory at Location 3 */
   private static final InventoryKey INVENTORY_3_KEY = new InventoryKey( 2000, 3 );

   /** Attached Inventory */
   private static final InventoryKey INVENTORY_4_KEY = new InventoryKey( 2000, 4 );

   /** Empty Bin Inventory */
   private static final InventoryKey INVENTORY_5_KEY = new InventoryKey( 2000, 5 );

   /** Non-Empty Bin Inventory */
   private static final InventoryKey INVENTORY_6_KEY = new InventoryKey( 2000, 6 );

   /** Inventory at Location 2 (QUAR) */
   private static final InventoryKey INVENTORY_7_KEY = new InventoryKey( 2000, 7 );

   /** Inventory with Part that doesn't have shelf_life_qt */
   private static final InventoryKey INVENTORY_8_KEY = new InventoryKey( 2000, 8 );

   /** Inventory with Special Handling */
   private static final InventoryKey[] INVENTORY_SPECIAL_HANDLING_KEYS = new InventoryKey[] {
         new InventoryKey( 2000, 9 ), new InventoryKey( 2000, 10 ), new InventoryKey( 2000, 11 ),
         new InventoryKey( 2000, 12 ), new InventoryKey( 2000, 13 ), new InventoryKey( 2000, 14 ),
         new InventoryKey( 2000, 15 ), new InventoryKey( 2000, 16 ) };

   /** Inventory with invalid conditions */
   private static final InventoryKey[] INVALID_INVENTORY_CONDITION_KEYS =
         new InventoryKey[] { new InventoryKey( 2000, 17 ), // CONDEMNed Inventory
               new InventoryKey( 2000, 18 ), // SCRAPed Inventory
               new InventoryKey( 2000, 19 ) // ARCHIVEd Inventory
         };

   /** Normal Part No */
   private static final PartNoKey PART_NO_KEY = new PartNoKey( 2000, 1 );

   /** Part No with Special Handling instructions */
   private static final PartNoKey[] PART_NO_SPECIAL_HANDLING_KEYS =
         new PartNoKey[] { new PartNoKey( 2000, 3 ), new PartNoKey( 2000, 4 ),
               new PartNoKey( 2000, 5 ), new PartNoKey( 2000, 6 ), new PartNoKey( 2000, 7 ),
               new PartNoKey( 2000, 8 ), new PartNoKey( 2000, 9 ), new PartNoKey( 2000, 10 ) };

   /** Location 1 */
   private static final LocationKey LOCATION_1_KEY = new LocationKey( 2000, 1 );

   /** Location 3 */
   private static final LocationKey LOCATION_3_KEY = new LocationKey( 2000, 3 );


   /**
    * {@inheritDoc} This offsets the shelf_expiry_dt to match the current date to make sure that
    * aCountDays values do not break down after a certain amount of time.
    */
   @Before
   public void setUp() throws Exception {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );

      // Get Date Difference
      Calendar lNow = Calendar.getInstance();
      long lMillisDifference = lNow.getTimeInMillis() - INITIAL_DATE.getTimeInMillis();
      int lDaysDifference = ( int ) ( lMillisDifference / ( 1000 * 60 * 60 * 24 ) );

      // Apply Date Difference to all inventory
      PlainBatchStatement lBatchStatment = MxBatchDataAccess.getInstance()
            .getBatchStatement( "UPDATE inv_inv SET shelf_expiry_dt = shelf_expiry_dt + ?" );
      lBatchStatment.setInt( 1, lDaysDifference );
      lBatchStatment.addBatch();
      MxBatchDataAccess.getInstance().sendBatch();
   }


   /**
    * Ensure that attached inventory do not show up on the list.
    */
   @Test
   public void testAttachedInventory() {
      QuerySet lQuerySet = executeQuery( HR_1_KEY, 0 );

      assertDoesNotContain( lQuerySet, INVENTORY_4_KEY );
   }


   /**
    * Ensure that empty bins do not show up on the list.
    */
   @Test
   public void testBin_Empty() {
      QuerySet lQuerySet = executeQuery( HR_1_KEY, 0 );

      assertDoesNotContain( lQuerySet, INVENTORY_5_KEY );
   }


   /**
    * Ensure that bins that are not empty show up on the list.
    */
   @Test
   public void testBin_NotEmpty() {
      QuerySet lQuerySet = executeQuery( HR_1_KEY, 0 );

      assertContains( lQuerySet, INVENTORY_6_KEY, PART_NO_KEY, LOCATION_1_KEY, false );
   }


   /**
    * Ensure that inventory in CONDEMN, SCRAP, and ARCHIVE do not show up on the list.
    */
   @Test
   public void testInvalidInventoryCondition() {
      QuerySet lQuerySet = executeQuery( HR_1_KEY, 0 );
      for ( int i = 0; i < INVALID_INVENTORY_CONDITION_KEYS.length; i++ ) {
         assertDoesNotContain( lQuerySet, INVALID_INVENTORY_CONDITION_KEYS[i] );
      }
   }


   /**
    * Ensure that only inventory accessible to HR sees the inventory on the list.
    */
   @Test
   public void testLocation_Accessible() {
      QuerySet lQuerySet = executeQuery( HR_3_KEY, 0 );
      assertContains( lQuerySet, INVENTORY_3_KEY, PART_NO_KEY, LOCATION_3_KEY, false );
   }


   /**
    * Ensure that only inventory accessible to HR sees the inventory on the list.
    */
   @Test
   public void testLocation_NotAccessible() {
      QuerySet lQuerySet = executeQuery( HR_1_KEY, 0 );
      assertDoesNotContain( lQuerySet, INVENTORY_3_KEY );
   }


   /**
    * Ensure inventory that are in Quarantine do not show up on the list.
    */
   @Test
   public void testLocationType_QUAR() {
      QuerySet lQuerySet = executeQuery( HR_2_KEY, 0 );
      assertDoesNotContain( lQuerySet, INVENTORY_7_KEY );
   }


   /**
    * Ensure that inventory whose part does not have a shelf life do not show up on the list.
    */
   @Test
   public void testPart_HasNoShelfLife() {
      QuerySet lQuerySet = executeQuery( HR_1_KEY, 0 );
      assertDoesNotContain( lQuerySet, INVENTORY_8_KEY );
   }


   /**
    * Ensure that inventory whose part has a shelf life show up on the list.
    */
   @Test
   public void testPart_HasShelfLife() {
      QuerySet lQuerySet = executeQuery( HR_1_KEY, 0 );
      assertContains( lQuerySet, INVENTORY_1_KEY, PART_NO_KEY, LOCATION_1_KEY, false );
   }


   /**
    * Ensure that inventory that are out of range of the expiry range specified do not show up on
    * the list.
    */
   @Test
   public void testShelfExpiryDt_OutOfRange() {
      QuerySet lQuerySet = executeQuery( HR_1_KEY, 0 );

      assertDoesNotContain( lQuerySet, INVENTORY_2_KEY );
   }


   /**
    * Ensure that inventory that are in range show up on the list.
    */
   @Test
   public void testShelfExpiryDt_WithinRange() {
      QuerySet lQuerySet = executeQuery( HR_1_KEY, 1 );

      assertContains( lQuerySet, INVENTORY_2_KEY, PART_NO_KEY, LOCATION_1_KEY, false );
   }


   /**
    * Ensure that special handling is calculated properly.
    */
   @Test
   public void testSpecialHandling_HasSpecialHandling() {
      QuerySet lQuerySet = executeQuery( HR_1_KEY, 0 );

      for ( int i = 0; i < INVENTORY_SPECIAL_HANDLING_KEYS.length; i++ ) {
         assertContains( lQuerySet, INVENTORY_SPECIAL_HANDLING_KEYS[i],
               PART_NO_SPECIAL_HANDLING_KEYS[i], LOCATION_1_KEY, true );
      }
   }


   /**
    * Ensure that special handling is calculated properly
    */
   @Test
   public void testSpecialHandling_NoSpecialHandling() {
      QuerySet lQuerySet = executeQuery( HR_1_KEY, 0 );

      assertContains( lQuerySet, INVENTORY_1_KEY, PART_NO_KEY, LOCATION_1_KEY, false );
   }


   /**
    * Asserts that the inventory is returned with the expected properties.
    *
    * @param aQuerySet
    *           the data set
    * @param aInvNoKey
    *           the inventory
    * @param aPartNoKey
    *           the inventory part
    * @param aLocationKey
    *           the location of the inventory
    * @param aSpecialHandlingBool
    *           whether the inventory requires special handling
    */
   private void assertContains( QuerySet aQuerySet, InventoryKey aInvNoKey, PartNoKey aPartNoKey,
         LocationKey aLocationKey, boolean aSpecialHandlingBool ) {

      boolean lFoundInventory = false;
      aQuerySet.beforeFirst();
      while ( aQuerySet.next() ) {
         InventoryKey lInvNoKey = aQuerySet.getKey( InventoryKey.class, "inventory_key" );
         if ( !aInvNoKey.equals( lInvNoKey ) ) {
            continue;
         }

         lFoundInventory = true;

         assertEquals( aPartNoKey, aQuerySet.getKey( PartNoKey.class, "part_no_key" ) );
         assertEquals( aLocationKey, aQuerySet.getKey( LocationKey.class, "location_key" ) );
         assertEquals( aSpecialHandlingBool, aQuerySet.getBoolean( "special_handling_bool" ) );

         break;
      }

      assertTrue( String.format( "QuerySet did not include inventory <%s>", aInvNoKey ),
            lFoundInventory );
   }


   /**
    * Asserts an inventory is not in the query set
    *
    * @param aQuerySet
    *           the query set
    * @param aInventoryKey
    *           the inventory
    */
   private void assertDoesNotContain( QuerySet aQuerySet, InventoryKey aInventoryKey ) {
      aQuerySet.beforeFirst();
      while ( aQuerySet.next() ) {
         InventoryKey lInventoryKey = aQuerySet.getKey( InventoryKey.class, "inventory_key" );

         assertNotSame( aInventoryKey, lInventoryKey );
      }
   }


   /**
    * Executes the query with the given arguments
    *
    * @param aHrKey
    *           the human resource key
    * @param aDayCount
    *           the amount of days to look ahead for expiration dates
    *
    * @return the data set
    */
   private QuerySet executeQuery( HumanResourceKey aHrKey, int aDayCount ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aHrKey, "aHrDbId", "aHrId" );
      lArgs.add( "aDayCount", aDayCount );

      return QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
