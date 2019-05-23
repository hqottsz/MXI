
package com.mxi.mx.core.query.inventory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;


/**
 * Ensures that the <code>getBomItemPositionTree</code> works properly
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetBomItemPositionTreeTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetBomItemPositionTreeTest.class );
   }


   private static final ConfigSlotPositionKey CONFIG_SLOT_POSITION_A_1 =
         new ConfigSlotPositionKey( 4650, "TEST", 2, 0 );
   private static final PartGroupKey PART_GROUP_A_1_PG1 = new PartGroupKey( 4650, 2 );
   private static final PartNoKey PART_A_1_P1 = new PartNoKey( 4650, 2 );

   private static final ConfigSlotPositionKey CONFIG_SLOT_POSITION_A_1_1P0 =
         new ConfigSlotPositionKey( 4650, "TEST", 3, 0 );
   private static final ConfigSlotPositionKey CONFIG_SLOT_POSITION_A_1_1P1 =
         new ConfigSlotPositionKey( 4650, "TEST", 3, 1 );
   private static final ConfigSlotPositionKey CONFIG_SLOT_POSITION_A_1_1P2 =
         new ConfigSlotPositionKey( 4650, "TEST", 3, 2 );

   private static final PartGroupKey PART_GROUP_A_1_1_PG1 = new PartGroupKey( 4650, 3 );
   private static final PartNoKey PART_A_1_1_P1 = new PartNoKey( 4650, 3 );

   private static final PartGroupKey PART_GROUP_A_1_1_PG2 = new PartGroupKey( 4650, 4 );
   private static final PartNoKey PART_A_1_1_P2 = new PartNoKey( 4650, 4 );

   private static final ConfigSlotPositionKey CONFIG_SLOT_POSITION_A_1_2 =
         new ConfigSlotPositionKey( 4650, "TEST", 5, 0 );
   private static final PartGroupKey PART_GROUP_A_1_2_PG1 = new PartGroupKey( 4650, 5 );
   private static final PartNoKey PART_A_1_2_P1 = new PartNoKey( 4650, 5 );

   private static final ConfigSlotPositionKey CONFIG_SLOT_POSITION_A_1_3 =
         new ConfigSlotPositionKey( 4650, "TEST", 4, 0 );
   private static final PartGroupKey PART_GROUP_A_1_3_PG1 = new PartGroupKey( 4650, 6 );
   private static final PartNoKey PART_A_1_3_P1 = new PartNoKey( 4650, 6 );

   private static final ConfigSlotPositionKey CONFIG_SLOT_POSITION_B =
         new ConfigSlotPositionKey( 4650, "TEST_B", 1, 0 );
   private static final PartGroupKey PART_GROUP_B_PG1 = new PartGroupKey( 4650, 11 );
   private static final PartNoKey PART_B_P1 = new PartNoKey( 4650, 1 );

   private static final ConfigSlotPositionKey CONFIG_SLOT_POSITION_B_3 =
         new ConfigSlotPositionKey( 4650, "TEST_B", 2, 0 );
   private static final PartGroupKey PART_GROUP_B_3_PG1 = new PartGroupKey( 4650, 12 );
   private static final PartNoKey PART_B_3_P1 = new PartNoKey( 4650, 2 );

   private static final ConfigSlotPositionKey CONFIG_SLOT_POSITION_B_2 =
         new ConfigSlotPositionKey( 4650, "TEST_B", 3, 0 );
   private static final PartGroupKey PART_GROUP_B_2_PG1 = new PartGroupKey( 4650, 13 );
   private static final PartNoKey PART_B_2_P1 = new PartNoKey( 4650, 3 );

   private static final ConfigSlotPositionKey CONFIG_SLOT_POSITION_B_1 =
         new ConfigSlotPositionKey( 4650, "TEST_B", 4, 0 );
   private static final PartGroupKey PART_GROUP_B_1_PG1 = new PartGroupKey( 4650, 14 );
   private static final PartNoKey PART_B_1_P1 = new PartNoKey( 4650, 4 );

   private static final ConfigSlotPositionKey CONFIG_SLOT_POSITION_B_0 =
         new ConfigSlotPositionKey( 4650, "TEST_B", 5, 0 );
   private static final PartGroupKey PART_GROUP_B_0_PG1 = new PartGroupKey( 4650, 15 );
   private static final PartNoKey PART_B_0_P1 = new PartNoKey( 4650, 5 );
   private static final PartNoKey PART_B_0_P2 = new PartNoKey( 4650, 8 );

   private static final ConfigSlotPositionKey CONFIG_SLOT_POSITION_B_5 =
         new ConfigSlotPositionKey( 4650, "TEST_B", 6, 0 );
   private static final PartGroupKey PART_GROUP_B_5_PG1 = new PartGroupKey( 4650, 16 );
   private static final PartNoKey PART_B_5_P1 = new PartNoKey( 4650, 6 );

   private static final ConfigSlotPositionKey CONFIG_SLOT_POSITION_B_4 =
         new ConfigSlotPositionKey( 4650, "TEST_B", 7, 0 );
   private static final PartGroupKey PART_GROUP_B_4_PG1 = new PartGroupKey( 4650, 17 );
   private static final PartNoKey PART_B_4_P1 = new PartNoKey( 4650, 7 );


   /**
    * Ensure that the specified configuration slot is returned.
    */
   @Test
   public void testReturnsConfigSlotsTreeInAlphabeticalOrder() {
      QuerySet lQs = getResultsForA_1();

      // A_1 should be the first row (sorted by assmbl_bom_cd)
      assertRows( lQs, CONFIG_SLOT_POSITION_A_1, partGroup( PART_GROUP_A_1_PG1, PART_A_1_P1 ) );

      // A_1_1 with 3 positions (assmbl_bom_cd sorted takes precedence over assmbl_pos_id)
      assertRows( lQs, CONFIG_SLOT_POSITION_A_1_1P0,
            with( partGroup( PART_GROUP_A_1_1_PG1, PART_A_1_1_P1 ),
                  partGroup( PART_GROUP_A_1_1_PG2, PART_A_1_1_P2 ) ) );
      assertRows( lQs, CONFIG_SLOT_POSITION_A_1_1P1,
            with( partGroup( PART_GROUP_A_1_1_PG1, PART_A_1_1_P1 ),
                  partGroup( PART_GROUP_A_1_1_PG2, PART_A_1_1_P2 ) ) );
      assertRows( lQs, CONFIG_SLOT_POSITION_A_1_1P2,
            with( partGroup( PART_GROUP_A_1_1_PG1, PART_A_1_1_P1 ),
                  partGroup( PART_GROUP_A_1_1_PG2, PART_A_1_1_P2 ) ) );

      // Ensure A_1_2 is returned after A_1_1
      assertRows( lQs, CONFIG_SLOT_POSITION_A_1_2,
            partGroup( PART_GROUP_A_1_2_PG1, PART_A_1_2_P1 ) );

      // Ensure A_1_3 is returned after A_1_1 and A_1_2
      assertRows( lQs, CONFIG_SLOT_POSITION_A_1_3,
            partGroup( PART_GROUP_A_1_3_PG1, PART_A_1_3_P1 ) );

      assertFinished( lQs );
   }


   /**
    * Ensures that the sorting takes heirarchical sorting into consideration:
    *
    * <pre>
    *        B
    *          B_3
    *            B_2
    *          B_1
    *          B_0
    *            B_5
    *            B_4
    * </pre>
    *
    * should be returned as:
    *
    * <pre>
    *        B
    *          B_0
    *            B_4
    *            B_5
    *          B_1
    *          B_3
    *            B_2
    * </pre>
    */
   @Test
   public void testReturnsConfigSlotsTreeInHierarchicalOrder() {
      QuerySet lQs = getResultsForB();

      // Ensure that B is returned first
      assertRows( lQs, CONFIG_SLOT_POSITION_B, partGroup( PART_GROUP_B_PG1, PART_B_P1 ) );

      // Ensure that B_0 is returned before B_1 (alphanumerical on sibling)
      assertRows( lQs, CONFIG_SLOT_POSITION_B_0,
            partGroup( PART_GROUP_B_0_PG1, PART_B_0_P1, PART_B_0_P2 ) );

      // Ensure that B_4 is returned right after B_0 (heirarchy)
      assertRows( lQs, CONFIG_SLOT_POSITION_B_4, partGroup( PART_GROUP_B_4_PG1, PART_B_4_P1 ) );

      // Ensure that B_5 is returned right after B_4 (alphanumerical on sibling)
      assertRows( lQs, CONFIG_SLOT_POSITION_B_5, partGroup( PART_GROUP_B_5_PG1, PART_B_5_P1 ) );

      // Ensure that B_1 is returned before B_3 (alphanumerical on sibling)
      assertRows( lQs, CONFIG_SLOT_POSITION_B_1, partGroup( PART_GROUP_B_1_PG1, PART_B_1_P1 ) );

      // Ensure that B_3 is returned before B_2 (heirarchy is higher)
      assertRows( lQs, CONFIG_SLOT_POSITION_B_3, partGroup( PART_GROUP_B_3_PG1, PART_B_3_P1 ) );

      // Ensure that B_2 is returned
      assertRows( lQs, CONFIG_SLOT_POSITION_B_2, partGroup( PART_GROUP_B_2_PG1, PART_B_2_P1 ) );

      // No other configuration slots expected
      assertFinished( lQs );
   }


   /**
    * Ensure that the specified configuration slot is returned
    */
   @Test
   public void testReturnsItself() {
      QuerySet lQs = getResultsForA_1();

      assertRows( lQs, CONFIG_SLOT_POSITION_A_1, partGroup( PART_GROUP_A_1_PG1, PART_A_1_P1 ) );
   }


   /**
    * Asserts that no more rows are expected
    *
    * @param aQs
    *           the query set
    */
   private void assertFinished( QuerySet aQs ) {
      assertFalse( aQs.next() );
   }


   /**
    * Asserts the query row
    *
    * @param aQs
    *           the query set
    * @param aExpectedConfigSlotPosition
    *           the expected configuration slot position
    * @param aExpectedPartGroupAndParts
    *           the expected part number
    */
   private void assertRows( QuerySet aQs, ConfigSlotPositionKey aExpectedConfigSlotPosition,
         Map<PartGroupKey, List<PartNoKey>> aExpectedPartGroupAndParts ) {
      int lPartsCount = 0;
      for ( List<PartNoKey> lParts : aExpectedPartGroupAndParts.values() ) {
         lPartsCount += lParts.size();
      }

      // Ensure configuration slots are in order; part group and part number ordering is not
      // necessary
      for ( int i = 0; i < lPartsCount; i++ ) {
         assertTrue( aQs.next() );

         ConfigSlotPositionKey lActualConfigSlotPosition = aQs.getKey( ConfigSlotPositionKey.class,
               "assmbl_db_id", "assmbl_cd", "assmbl_bom_id", "assmbl_pos_id" );
         assertEquals( "configuration slot", aExpectedConfigSlotPosition,
               lActualConfigSlotPosition );

         PartGroupKey lActualPartGroup =
               aQs.getKey( PartGroupKey.class, "bom_part_db_id", "bom_part_id" );

         List<PartNoKey> lParts = aExpectedPartGroupAndParts.get( lActualPartGroup );
         assertNotNull( "part group", lParts );

         PartNoKey lActualPartNo = aQs.getKey( PartNoKey.class, "part_no_db_id", "part_no_id" );
         assertTrue( "part", lParts.contains( lActualPartNo ) );

         // All parts needs to be here! Removing the value from the list allows us to verify that
         // no part-group/parts are repeated
         lParts.remove( lActualPartNo );
      }
   }


   /**
    * Gets the query set for the specific configuration slot position
    *
    * @return the query set
    */
   private QuerySet getResultsForA_1() {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( CONFIG_SLOT_POSITION_A_1, "aAssmblDbId", "aAssmblCd", "aAssmblBomId",
            "aAssmblPosId" );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            "com.mxi.mx.core.query.inventory.getBomItemPositionTree", lArgs );
   }


   /**
    * Gets the query set for the specific configuration slot position
    *
    * @return the query set
    */
   private QuerySet getResultsForB() {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( CONFIG_SLOT_POSITION_B, "aAssmblDbId", "aAssmblCd", "aAssmblBomId",
            "aAssmblPosId" );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            "com.mxi.mx.core.query.inventory.getBomItemPositionTree", lArgs );
   }


   /**
    * Returns a map containing the part group and the parts associated to the part group
    *
    * @param aPartGroup
    *           the part group
    * @param aPartNo
    *           the parts
    *
    * @return the map of part-group to parts mapping
    */
   private Map<PartGroupKey, List<PartNoKey>> partGroup( PartGroupKey aPartGroup,
         PartNoKey... aPartNo ) {
      Map<PartGroupKey, List<PartNoKey>> lMap = new HashMap<PartGroupKey, List<PartNoKey>>();
      lMap.put( aPartGroup, new ArrayList<PartNoKey>( Arrays.asList( aPartNo ) ) );

      return lMap;
   }


   /**
    * Combines multiple parts mapping together.
    *
    * @param aPartsMapping_1
    *           the part-group-part mapping
    * @param aPartsMapping_2
    *           the part-group-part mappting
    *
    * @return The combined part-group-part mappings
    */
   private Map<PartGroupKey, List<PartNoKey>> with(
         Map<PartGroupKey, List<PartNoKey>> aPartsMapping_1,
         Map<PartGroupKey, List<PartNoKey>> aPartsMapping_2 ) {
      Map<PartGroupKey, List<PartNoKey>> lMap = new HashMap<PartGroupKey, List<PartNoKey>>();
      lMap.putAll( aPartsMapping_1 );
      lMap.putAll( aPartsMapping_2 );

      return lMap;
   }
}
