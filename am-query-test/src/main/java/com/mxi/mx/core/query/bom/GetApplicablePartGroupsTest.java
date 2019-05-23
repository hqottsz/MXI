
package com.mxi.mx.core.query.bom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartNoKey;


/**
 * Ensures getApplicablePartGroups.qrx returns the applicable part groups for an inventory and part
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetApplicablePartGroupsTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetApplicablePartGroupsTest.class );
   }


   public static final PartGroupKey PG_A = new PartGroupKey( 4650, 1 );
   public static final PartGroupKey PG_B = new PartGroupKey( 4650, 2 );
   public static final PartGroupKey PG_B_PART_GROUP_APPL = new PartGroupKey( 4650, 3 );
   public static final PartGroupKey PG_COMHW = new PartGroupKey( 4650, 4 );

   public static final InventoryKey INVENTORY_WITH_APPL = new InventoryKey( 4650, 1 );
   public static final InventoryKey INVENTORY_WITHOUT_APPL = new InventoryKey( 4650, 3 );
   public static final PartNoKey P_A_STANDARD = new PartNoKey( 4650, 1 );
   public static final PartNoKey P_B_STANDARD = new PartNoKey( 4650, 2 );
   public static final PartNoKey P_B_PART_APPL = new PartNoKey( 4650, 3 );
   public static final PartNoKey P_B_PART_GROUP_APPL = new PartNoKey( 4650, 4 );
   public static final PartNoKey P_COMHW = new PartNoKey( 4650, 5 );

   private static final boolean COMHW_INCLUDED = true;
   private static final boolean COMHW_EXCLUDED = false;

   private static final boolean APPLICABLE = true;
   private static final boolean NOT_APPLICABLE = false;


   /**
    * Tests the basic case
    */
   @Test
   public void testBasic() {
      QuerySet lQs = getResults( INVENTORY_WITHOUT_APPL, P_A_STANDARD );
      assertTrue( lQs.first() );
      assertPartGroup( lQs, PG_A );
      assertIsApplicable( lQs );
   }


   /**
    * Tests the part applicability case
    */
   @Test
   public void testExcludeInapplicablePart() {
      QuerySet lQs = getResults( INVENTORY_WITHOUT_APPL, P_B_PART_APPL );
      assertTrue( lQs.first() );
      assertPartGroup( lQs, PG_B );
      assertPartApplicable( lQs, APPLICABLE );
      assertPartGroupApplicable( lQs, APPLICABLE );

      lQs = getResults( INVENTORY_WITH_APPL, P_B_PART_APPL );
      assertTrue( lQs.first() );
      assertPartGroup( lQs, PG_B );
      assertPartApplicable( lQs, NOT_APPLICABLE );
      assertPartGroupApplicable( lQs, APPLICABLE );
   }


   /**
    * Tests the part group applicability case
    */
   @Test
   public void testExcludeInapplicablePartGroup() {
      QuerySet lQs = getResults( INVENTORY_WITHOUT_APPL, P_B_PART_GROUP_APPL );
      assertTrue( lQs.first() );
      assertPartGroup( lQs, PG_B_PART_GROUP_APPL );
      assertPartApplicable( lQs, APPLICABLE );
      assertPartGroupApplicable( lQs, APPLICABLE );

      lQs = getResults( INVENTORY_WITH_APPL, P_B_PART_GROUP_APPL );
      assertTrue( lQs.first() );
      assertPartGroup( lQs, PG_B_PART_GROUP_APPL );
      assertPartApplicable( lQs, APPLICABLE );
      assertPartGroupApplicable( lQs, NOT_APPLICABLE );
   }


   /**
    * Tests the basic case
    */
   @Test
   public void testIncludeSubAssembly() {
      QuerySet lQs = getResults( INVENTORY_WITHOUT_APPL, P_B_STANDARD );
      assertTrue( lQs.first() );
      assertPartGroup( lQs, PG_B );
      assertIsApplicable( lQs );
   }


   /**
    * Test that common hardware is not included if it is specified to ignore it
    */
   @Test
   public void testWithCommonHardware() {
      QuerySet lQs = getResults( INVENTORY_WITH_APPL, P_COMHW, COMHW_INCLUDED );
      assertTrue( lQs.first() );
      assertPartGroup( lQs, PG_COMHW );
      assertIsApplicable( lQs );
   }


   /**
    * Test that common hardware is not included if it is specified to ignore it
    */
   @Test
   public void testWithoutCommonHardware() {
      QuerySet lQs = getResults( INVENTORY_WITH_APPL, P_COMHW, COMHW_EXCLUDED );
      assertFalse( lQs.first() );
   }


   /**
    * Assert that the part is applicable
    *
    * @param aQs
    *           the query set
    */
   private void assertIsApplicable( QuerySet aQs ) {
      assertPartApplicable( aQs, true );
      assertPartGroupApplicable( aQs, true );
   }


   /**
    * Assert the part applicability
    *
    * @param aQs
    *           the query set
    * @param aPartApplicable
    *           the expected part applicability
    */
   private void assertPartApplicable( QuerySet aQs, boolean aPartApplicable ) {
      assertEquals( "part applicability", aPartApplicable, aQs.getBoolean( "part_appl" ) );
   }


   /**
    * Assert the part group mataches
    *
    * @param aQs
    *           the query set
    * @param aExpectedPartGroup
    *           the expected part group
    */
   private void assertPartGroup( QuerySet aQs, PartGroupKey aExpectedPartGroup ) {
      PartGroupKey lActualPartGroup =
            aQs.getKey( PartGroupKey.class, "bom_part_db_id", "bom_part_id" );
      assertEquals( "part group", aExpectedPartGroup, lActualPartGroup );
   }


   /**
    * Asserts that the part group applicablility
    *
    * @param aQs
    *           the query set
    * @param aPartGroupApplicable
    *           the expected part group applicability
    */
   private void assertPartGroupApplicable( QuerySet aQs, boolean aPartGroupApplicable ) {
      assertEquals( "part group applicability", aPartGroupApplicable,
            aQs.getBoolean( "part_group_appl" ) );
   }


   /**
    * Gets the query result
    *
    * @param aInventory
    *           the inventory
    * @param aPartNo
    *           the part no
    *
    * @return the query set
    */
   private QuerySet getResults( InventoryKey aInventory, PartNoKey aPartNo ) {
      return getResults( aInventory, aPartNo, COMHW_INCLUDED );
   }


   /**
    * Gets the query result
    *
    * @param aInventory
    *           the inventory
    * @param aPartNo
    *           the part no
    * @param aCommonHardwareIncluded
    *           TRUE if it includes common hardware
    *
    * @return the query set
    */
   private QuerySet getResults( InventoryKey aInventory, PartNoKey aPartNo,
         boolean aCommonHardwareIncluded ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aInventory, "aHInvNoDbId", "aHInvNoId" );
      lArgs.add( aPartNo, "aPartNoDbId", "aPartNoId" );
      lArgs.add( "aCheckCommonHardware", aCommonHardwareIncluded ? 1 : 0 );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            "com.mxi.mx.core.query.bom.getApplicablePartGroups", lArgs );
   }
}
