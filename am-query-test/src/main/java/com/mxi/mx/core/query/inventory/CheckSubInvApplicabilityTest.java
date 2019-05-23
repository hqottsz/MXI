package com.mxi.mx.core.query.inventory;

import static org.junit.Assert.assertEquals;

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
import com.mxi.am.domain.Part;
import com.mxi.am.domain.PartGroup;
import com.mxi.am.domain.TrackedInventory;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefInvClassKey;


/**
 * This class tests the CheckSubInvApplicability query.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class CheckSubInvApplicabilityTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static final String NO_APPL_CODE = null;

   private static final String APPL_CODE_1 = "01";
   private static final String APPL_RANGE_CONTAINING_CODE_1 = "01-05";
   private static final String APPL_RANGE_NOT_CONTAINING_CODE_1 = "05-10";

   private static final String APPL_CODE_2 = "02";
   private static final String APPL_CODE_6 = "06";

   private static final String PART_GROUP = "PG";

   private static final String ACFT_INV_DESC = "ACFT_INV_DESC";
   private static final String MAIN_INV_DESC = "MAIN_INV_DESC";
   private static final String ASSEMBLY_INV_DESC = "ASSY_INV_DESC";
   private static final String TRK_DESC = "TRK_DESC";

   private static final String CHILD_INV_DESC = "CHILD_INV_DESC";

   private static final String ANOTHER_CHILD_INV_DESC = "ANOTHER_CHILD_INV_DESC";

   private static final String GRANDCHILD_INV_DESC = "GRANDCHILD_INV_DESC";

   private static final boolean PART_NOT_APPLICABLE = false;
   private static final boolean PART_APPLICABLE = true;
   private static final boolean PART_GROUP_NOT_APPLICABLE = false;
   private static final boolean PART_GROUP_APPLICABLE = true;

   private static final String SUB_INV_DESC = "SUB_INV_DESC";

   private DataSet iResultDs;


   /**
    * Given a loose engine (main inventory) with an applicability code
    *
    * And a TRK inventory (test inventory) with sub-inventory which has part group and part
    * applicability ranges such that the loose engine's applicability code falls inside both the
    * ranges
    *
    * When the TRK inventory is reserved, issued, attached to the engine (execute query)
    *
    * Then the query returns 0 rows (meaning there is no inapplicability)
    *
    */
   @Test
   public void testWhenLooseEngineCodeInBothPartRangeAndPartGroupRange() {
      InventoryKey lMainInv = createEngineInventory( APPL_CODE_1 );
      InventoryKey lTestInv = createTrkInventory();
      withSubInventory( lTestInv, SUB_INV_DESC, APPL_RANGE_CONTAINING_CODE_1,
            APPL_RANGE_CONTAINING_CODE_1 );

      execute( lMainInv, lTestInv );
      assertEquals( 0, iResultDs.getRowCount() );
   }


   /**
    * Given a loose engine (main inventory) with an applicability code
    *
    * And a TRK inventory (test inventory) with sub-inventory with no part group applicability range
    * but part applicability range such that the loose engine's applicability code falls outside
    * this range
    *
    * When the TRK inventory is reserved, issued, attached to the engine (execute query)
    *
    * Then the query returns 0 rows (meaning there is no inapplicability)
    */
   @Test
   public void testWhenLooseEngineCodeNotInPartRangeAndPartGroupRangeIsBlank() {
      InventoryKey lMainInv = createEngineInventory( APPL_CODE_1 );
      InventoryKey lTestInv = createTrkInventory();
      withSubInventory( lTestInv, SUB_INV_DESC, NO_APPL_CODE, APPL_RANGE_NOT_CONTAINING_CODE_1 );

      execute( lMainInv, lTestInv );
      assertEquals( 0, iResultDs.getRowCount() );
   }


   /**
    * Given a loose engine (main inventory) with an applicability code
    *
    * And a TRK inventory (test inventory) with sub-inventory which has part group and part
    * applicability ranges such that the loose engine's applicability code falls outside both the
    * ranges
    *
    * When the TRK inventory is reserved, issued, attached to the engine (execute query)
    *
    * Then the query returns 1 row
    */
   @Test
   public void testWhenLooseEngineCodeNotInPartRangeAndPartGroupRange() {
      InventoryKey lMainInv = createEngineInventory( APPL_CODE_1 );
      InventoryKey lTestInv = createTrkInventory();
      InventoryKey lSubInv = withSubInventory( lTestInv, SUB_INV_DESC,
            APPL_RANGE_NOT_CONTAINING_CODE_1, APPL_RANGE_NOT_CONTAINING_CODE_1 );

      execute( lMainInv, lTestInv );
      assertEquals( 1, iResultDs.getRowCount() );
      iResultDs.next();

      assertEquals( APPL_CODE_1, iResultDs.getString( "appl_code" ) );
      assertEquals( lSubInv,
            iResultDs.getKey( InventoryKey.class, "test_inv_no_db_id", "test_inv_no_id" ) );
      assertEquals( SUB_INV_DESC, iResultDs.getString( "test_inv_no_sdesc" ) );
      assertEquals( NO_APPL_CODE, iResultDs.getString( "part_range" ) );
      assertEquals( PART_APPLICABLE, iResultDs.getBoolean( "part_applicable" ) );
      assertEquals( APPL_RANGE_NOT_CONTAINING_CODE_1, iResultDs.getString( "part_group_range" ) );
      assertEquals( PART_GROUP_NOT_APPLICABLE, iResultDs.getBoolean( "part_group_applicable" ) );
   }


   /**
    * Given an aircraft with an applicability code
    *
    * And an engine (main inventory) attached to the aircraft with an applicability code
    *
    * And a TRK inventory (test inventory) with sub-inventory with part group applicability range
    * and part applicability range such that the aircraft's applicability code falls outside the
    * part group applicability range but inside the part applicability range
    *
    * When the TRK inventory is reserved, issued, attached to the engine (execute query)
    *
    * Then the query returns 0 rows (meaning there is no inapplicability)
    */
   @Test
   public void
         testWhenAircraftCodeInPartGroupRangeButNotInPartRangeAndEngineCodeNotInPartRangeButInPartGroupRange() {
      InventoryKey lMainInv = createEngineAttachedToAircraft( APPL_CODE_1, APPL_CODE_6 );
      InventoryKey lTestInv = createTrkInventory();
      withSubInventory( lTestInv, SUB_INV_DESC, APPL_RANGE_NOT_CONTAINING_CODE_1,
            APPL_RANGE_CONTAINING_CODE_1 );

      execute( lMainInv, lTestInv );

      assertEquals( 0, iResultDs.getRowCount() );
   }


   /**
    * Given an aircraft with an applicability code
    *
    * And an engine (main inventory) attached to the aircraft with an applicability code
    *
    * And a TRK inventory with part group range not containing and part applicability range
    * containing the aircraft's applicability code
    *
    * And the TRK inventory (test inventory) with a child TRK inventory with part group
    * applicability range not containing aircraft's applicability code but part applicability range
    * not containing the code
    *
    * And the child TRK inventory has its own child TRK inventory(grand-child) with part group
    * applicability range not containing aircraft's applicability code and part applicability range
    * not containing the code
    *
    * And the TRK inventory (test inventory) has another child TRK inventory with part group
    * applicability range not containing aircraft's applicability code but part applicability range
    * containing the code
    *
    * When the TRK inventory is reserved, issued, attached to the engine (execute query)
    *
    * Then the query returns 2 rows :child inventory is returned as not applicable, due to part
    * range and the grand-child inventory is returned as not applicable, due to part range
    */
   @Test
   public void testWhenCodeNotInMultiplePartRangesNorPartGroupRanges() {

      // Setup the test inventory to be applicable.
      String lTestInvPartGroupRange = APPL_RANGE_NOT_CONTAINING_CODE_1;
      String lTestInvPartRange = APPL_RANGE_CONTAINING_CODE_1;

      // Setup the child inventory to be not applicable due to part applicability range.
      String lChildInvPartGroupRange = APPL_RANGE_NOT_CONTAINING_CODE_1;
      String lChildInvPartRange = APPL_RANGE_NOT_CONTAINING_CODE_1;

      // Setup the other child inventory to be not applicable due to part group applicability range.
      String lAnotherChildInvPartGroupRange = APPL_RANGE_NOT_CONTAINING_CODE_1;
      String lAnotherChildInvPartRange = APPL_RANGE_CONTAINING_CODE_1;

      // Setup the grand-child inventory to be not applicable due to both applicability ranges.
      String lGrandchildInvPartGroupRange = APPL_RANGE_NOT_CONTAINING_CODE_1;
      String lGrandchildInvPartRange = APPL_RANGE_NOT_CONTAINING_CODE_1;

      InventoryKey lMainInv = createEngineAttachedToAircraft( APPL_CODE_1, APPL_CODE_6 );
      InventoryKey lTestInv = createTrkInventoryBasedOnPartNoAndPartGroup( lTestInvPartGroupRange,
            lTestInvPartRange );
      InventoryKey lChildInv = withSubInventory( lTestInv, CHILD_INV_DESC, lChildInvPartGroupRange,
            lChildInvPartRange );
      InventoryKey lGrandChildInv = withSubInventory( lChildInv, GRANDCHILD_INV_DESC,
            lGrandchildInvPartGroupRange, lGrandchildInvPartRange );
      withSubInventory( lTestInv, ANOTHER_CHILD_INV_DESC, lAnotherChildInvPartGroupRange,
            lAnotherChildInvPartRange );

      execute( lMainInv, lTestInv );

      assertEquals( 2, iResultDs.getRowCount() );
      iResultDs.next();

      assertEquals( APPL_CODE_1, iResultDs.getString( "appl_code" ) );
      assertEquals( lChildInv,
            iResultDs.getKey( InventoryKey.class, "test_inv_no_db_id", "test_inv_no_id" ) );
      assertEquals( CHILD_INV_DESC, iResultDs.getString( "test_inv_no_sdesc" ) );
      assertEquals( APPL_RANGE_NOT_CONTAINING_CODE_1, iResultDs.getString( "part_range" ) );
      assertEquals( PART_NOT_APPLICABLE, iResultDs.getBoolean( "part_applicable" ) );
      assertEquals( NO_APPL_CODE, iResultDs.getString( "part_group_range" ) );
      assertEquals( PART_GROUP_APPLICABLE, iResultDs.getBoolean( "part_group_applicable" ) );
      iResultDs.next();

      // Verify the grand-child inventory is returned as not applicable, due to both ranges.
      assertEquals( APPL_CODE_1, iResultDs.getString( "appl_code" ) );
      assertEquals( lGrandChildInv,
            iResultDs.getKey( InventoryKey.class, "test_inv_no_db_id", "test_inv_no_id" ) );
      assertEquals( GRANDCHILD_INV_DESC, iResultDs.getString( "test_inv_no_sdesc" ) );
      assertEquals( APPL_RANGE_NOT_CONTAINING_CODE_1, iResultDs.getString( "part_range" ) );
      assertEquals( PART_NOT_APPLICABLE, iResultDs.getBoolean( "part_applicable" ) );
      assertEquals( NO_APPL_CODE, iResultDs.getString( "part_group_range" ) );
      assertEquals( PART_GROUP_APPLICABLE, iResultDs.getBoolean( "part_group_applicable" ) );
   }


   /**
    * Given an aircraft with an applicability code
    *
    * And an engine (main inventory) attached to the aircraft with an applicability code
    *
    * And a TRK inventory (test inventory) with sub-inventory with part group applicability range
    * and part applicability range such that the aircraft's applicability code falls inside the part
    * group applicability range and outside the part applicability range
    *
    * When the TRK inventory is reserved, issued, attached to the engine (execute query)
    *
    * Then the query returns 1 row : sub-inventory not applicable due to part range
    */
   @Test
   public void testWhenAircraftCodeInPartGroupRangeButNotInPartRange() {
      InventoryKey lMainInv = createEngineAttachedToAircraft( APPL_CODE_1, APPL_CODE_1 );
      InventoryKey lTestInv = createTrkInventory();
      InventoryKey lSubInv = withSubInventory( lTestInv, SUB_INV_DESC, APPL_RANGE_CONTAINING_CODE_1,
            APPL_RANGE_NOT_CONTAINING_CODE_1 );

      execute( lMainInv, lTestInv );

      assertEquals( 1, iResultDs.getRowCount() );
      iResultDs.next();

      assertEquals( APPL_CODE_1, iResultDs.getString( "appl_code" ) );
      assertEquals( lSubInv,
            iResultDs.getKey( InventoryKey.class, "test_inv_no_db_id", "test_inv_no_id" ) );
      assertEquals( SUB_INV_DESC, iResultDs.getString( "test_inv_no_sdesc" ) );
      assertEquals( APPL_RANGE_NOT_CONTAINING_CODE_1, iResultDs.getString( "part_range" ) );
      assertEquals( PART_NOT_APPLICABLE, iResultDs.getBoolean( "part_applicable" ) );
      assertEquals( NO_APPL_CODE, iResultDs.getString( "part_group_range" ) );
      assertEquals( PART_GROUP_APPLICABLE, iResultDs.getBoolean( "part_group_applicable" ) );
   }


   /**
    * Given an aircraft with an applicability code
    *
    * And an engine (main inventory) attached to the aircraft with an applicability code
    *
    * And a TRK inventory (test inventory) with sub-inventory with part group applicability range
    * and part applicability range such that the aircraft's applicability code falls outside the
    * part group applicability range and outside the part applicability range
    *
    * When the TRK inventory is reserved, issued, attached to the engine (execute query)
    *
    * Then the query returns 1 row : sub-inventory not applicable due to part range
    */
   @Test
   public void testWhenAircraftCodeNotInPartGroupRangeNorInPartRange() {
      InventoryKey lMainInv = createEngineAttachedToAircraft( APPL_CODE_1, APPL_CODE_6 );
      InventoryKey lTestInv = createTrkInventory();
      InventoryKey lSubInv = withSubInventory( lTestInv, SUB_INV_DESC,
            APPL_RANGE_NOT_CONTAINING_CODE_1, APPL_RANGE_NOT_CONTAINING_CODE_1 );

      execute( lMainInv, lTestInv );

      assertEquals( 1, iResultDs.getRowCount() );
      iResultDs.next();

      assertEquals( APPL_CODE_1, iResultDs.getString( "appl_code" ) );
      assertEquals( lSubInv,
            iResultDs.getKey( InventoryKey.class, "test_inv_no_db_id", "test_inv_no_id" ) );
      assertEquals( SUB_INV_DESC, iResultDs.getString( "test_inv_no_sdesc" ) );
      assertEquals( APPL_RANGE_NOT_CONTAINING_CODE_1, iResultDs.getString( "part_range" ) );
      assertEquals( PART_NOT_APPLICABLE, iResultDs.getBoolean( "part_applicable" ) );
      assertEquals( NO_APPL_CODE, iResultDs.getString( "part_group_range" ) );
      assertEquals( PART_GROUP_APPLICABLE, iResultDs.getBoolean( "part_group_applicable" ) );
   }


   /**
    * Given an aircraft with an applicability code
    *
    * And an engine (main inventory) attached to the aircraft with an applicability code
    *
    * And a TRK inventory (test inventory) with part group and part range applicability containing
    * aircraft's applicability code
    *
    * And the TRK inventory (test inventory) has a child TRK inventory with part group applicability
    * range containing aircraft's applicability code and part applicability range containing the
    * code
    *
    * And the child TRK inventory has its own child TRK inventory(grand-child) with part group
    * applicability range containing aircraft's applicability code and part applicability range not
    * containing the code
    *
    * When the TRK inventory is reserved, issued, attached to the engine (execute query)
    *
    * Then the query returns 1 row :grand-child inventory is returned as not applicable for part
    * range applicability
    */
   @Test
   public void
         testWhenAircraftCodeInSubInvPartRangeAndPartGroupRangeAndInGrandChildPartGroupRangeNotInGrandChildPartRange() {

      // Setup the test inventory to be applicable.
      String lTestInvPartGroupRange = APPL_RANGE_CONTAINING_CODE_1;
      String lTestInvPartRange = APPL_RANGE_CONTAINING_CODE_1;

      // Setup the child inventory to be applicable.
      String lChildInvPartGroupRange = APPL_RANGE_CONTAINING_CODE_1;
      String lChildInvPartRange = APPL_RANGE_CONTAINING_CODE_1;

      // Setup the grandchild inventory to be not applicable due to both applicability ranges.
      String lGrandchildInvPartGroupRange = APPL_RANGE_CONTAINING_CODE_1;
      String lGrandchildInvPartRange = APPL_RANGE_NOT_CONTAINING_CODE_1;

      InventoryKey lMainInv = createEngineAttachedToAircraft( APPL_CODE_1, APPL_CODE_1 );
      InventoryKey lTestInv = createTrkInventoryBasedOnPartNoAndPartGroup( lTestInvPartGroupRange,
            lTestInvPartRange );
      InventoryKey lChildInv = withSubInventory( lTestInv, CHILD_INV_DESC, lChildInvPartGroupRange,
            lChildInvPartRange );
      InventoryKey lGrandChildInv = withSubInventory( lChildInv, GRANDCHILD_INV_DESC,
            lGrandchildInvPartGroupRange, lGrandchildInvPartRange );

      execute( lMainInv, lTestInv );

      assertEquals( 1, iResultDs.getRowCount() );
      iResultDs.next();

      // Verify the grand-child inventory is returned as not applicable.
      assertEquals( APPL_CODE_1, iResultDs.getString( "appl_code" ) );
      assertEquals( lGrandChildInv,
            iResultDs.getKey( InventoryKey.class, "test_inv_no_db_id", "test_inv_no_id" ) );
      assertEquals( GRANDCHILD_INV_DESC, iResultDs.getString( "test_inv_no_sdesc" ) );
      assertEquals( APPL_RANGE_NOT_CONTAINING_CODE_1, iResultDs.getString( "part_range" ) );
      assertEquals( PART_NOT_APPLICABLE, iResultDs.getBoolean( "part_applicable" ) );
      assertEquals( NO_APPL_CODE, iResultDs.getString( "part_group_range" ) );
      assertEquals( PART_GROUP_APPLICABLE, iResultDs.getBoolean( "part_group_applicable" ) );
   }


   /**
    * Given an aircraft with no applicability code
    *
    * And an engine (main inventory) attached to the aircraft with an applicability code
    *
    * And a TRK inventory (test inventory) with sub-inventory with no part group applicability range
    * and part applicability range
    *
    * When the TRK inventory is reserved, issued, attached to the engine (execute query)
    *
    * Then the query returns 0 rows
    */
   @Test
   public void testWhenAircraftInvHasNoCode() {

      InventoryKey lMainInv = createEngineAttachedToAircraft( NO_APPL_CODE, APPL_CODE_2 );
      InventoryKey lTestInv = createTrkInventory();
      withSubInventory( lTestInv, SUB_INV_DESC, NO_APPL_CODE, APPL_RANGE_NOT_CONTAINING_CODE_1 );

      execute( lMainInv, lTestInv );
      assertEquals( 0, iResultDs.getRowCount() );
   }


   /**
    * Given an aircraft (main inventory) with an applicability code
    *
    * And a loose engine (test inventory) with an applicability code
    *
    * And a TRK inventory attached to the engine with part group applicability range and part
    * applicability range such that the engine's applicability code falls outside the part group
    * applicability range and the aircraft's applicability code outside the part applicability range
    *
    * When the Engine inventory is reserved, issued, attached to the aircraft (execute query)
    *
    * Then the query returns 2 rows: row with inapplicability between aircraft and sub-inventory due
    * to part range and a row with inapplicability between engine inventory and sub-inventory due to
    * part group range
    */
   @Test
   public void
         testWhenAircraftCodeNotInSubInventoryParRangeAndTestInventoryCodeNotInSubInventoryPartGroupRange() {
      InventoryKey lMainInv = createAircraftInventory( APPL_CODE_1 );
      InventoryKey lTestInv = createEngineInventory( APPL_CODE_6 );
      InventoryKey lSubInv = withSubInventory( lTestInv, SUB_INV_DESC, APPL_RANGE_CONTAINING_CODE_1,
            APPL_RANGE_NOT_CONTAINING_CODE_1 );

      execute( lMainInv, lTestInv );
      assertEquals( 2, iResultDs.getRowCount() );

      iResultDs.next();

      assertEquals( APPL_CODE_1, iResultDs.getString( "appl_code" ) );
      assertEquals( lSubInv,
            iResultDs.getKey( InventoryKey.class, "test_inv_no_db_id", "test_inv_no_id" ) );
      assertEquals( SUB_INV_DESC, iResultDs.getString( "test_inv_no_sdesc" ) );
      assertEquals( APPL_RANGE_NOT_CONTAINING_CODE_1, iResultDs.getString( "part_range" ) );
      assertEquals( PART_NOT_APPLICABLE, iResultDs.getBoolean( "part_applicable" ) );
      assertEquals( NO_APPL_CODE, iResultDs.getString( "part_group_range" ) );
      assertEquals( PART_GROUP_APPLICABLE, iResultDs.getBoolean( "part_group_applicable" ) );

      iResultDs.next();

      assertEquals( APPL_CODE_6, iResultDs.getString( "appl_code" ) );
      assertEquals( lSubInv,
            iResultDs.getKey( InventoryKey.class, "test_inv_no_db_id", "test_inv_no_id" ) );
      assertEquals( SUB_INV_DESC, iResultDs.getString( "test_inv_no_sdesc" ) );
      assertEquals( NO_APPL_CODE, iResultDs.getString( "part_range" ) );
      assertEquals( PART_APPLICABLE, iResultDs.getBoolean( "part_applicable" ) );
      assertEquals( APPL_RANGE_CONTAINING_CODE_1, iResultDs.getString( "part_group_range" ) );
      assertEquals( PART_GROUP_NOT_APPLICABLE, iResultDs.getBoolean( "part_group_applicable" ) );

   }


   /**
    * Given an aircraft with an applicability code
    *
    * And an engine (main inventory) attached to the aircraft with an applicability code
    *
    * And a TRK inventory with no part group and part applicability range
    *
    * And the TRK inventory (test inventory) has a child TRK inventory (child1) with part group
    * applicability range not containing aircraft's applicability code and part applicability range
    * containing the code
    *
    * And the child1 TRK inventory has a child TRK inventory (GrandChild11) with part group
    * applicability range not containing aircraft's applicability code and part applicability range
    * containing the code
    *
    * And the child1 TRK inventory has another child TRK inventory (GrandChild12) with part group
    * applicability range not containing aircraft's applicability code and part applicability range
    * not containing the code
    *
    * And the TRK inventory has another child TRK inventory (child2) with part group applicability
    * range not containing aircraft's applicability code and part applicability range not containing
    * the code
    *
    * And the TRK inventory has another child TRK inventory (child3) with part group applicability
    * range not containing aircraft's applicability code and part applicability range containing the
    * code
    *
    * And the TRK inventory has another child TRK inventory (child4) with part group applicability
    * range not containing aircraft's applicability code and part applicability range not containing
    * the code
    *
    * When the TRK inventory is reserved, issued, attached to the engine (execute query)
    *
    * Then the query returns 3 rows :child2 inventory is returned as not applicable due to part
    * range, child4 inventory is returned as not applicable due to part range and the GrandChild12
    * inventory is returned as not applicable due to part range
    */
   @Test
   public void testWhenMultipleNotApplicableSubInventory() {
      InventoryKey lMainInv = createEngineAttachedToAircraft( APPL_CODE_1, APPL_CODE_6 );
      InventoryKey lTestInv = createTrkInventory();

      // First level of sub-inventory.

      // Child1 (applicable) has applicable part and part group ranges.
      InventoryKey lChild1 = withSubInventory( lTestInv, SUB_INV_DESC,
            APPL_RANGE_NOT_CONTAINING_CODE_1, APPL_RANGE_CONTAINING_CODE_1 );

      // Child2 (not applicable) has applicable part group range and non-applicable part range.
      InventoryKey lChild2 = withSubInventory( lTestInv, SUB_INV_DESC,
            APPL_RANGE_NOT_CONTAINING_CODE_1, APPL_RANGE_NOT_CONTAINING_CODE_1 );

      // Child3 (not applicable) has non-applicable part group range and applicable part range.
      withSubInventory( lTestInv, SUB_INV_DESC, APPL_RANGE_NOT_CONTAINING_CODE_1,
            APPL_RANGE_CONTAINING_CODE_1 );

      // Child4 (not applicable) has non-applicable part group and part ranges.
      InventoryKey lChild4 = withSubInventory( lTestInv, SUB_INV_DESC,
            APPL_RANGE_NOT_CONTAINING_CODE_1, APPL_RANGE_NOT_CONTAINING_CODE_1 );

      // Second level of sub-inventory.

      // Grandchild11 (applicable) has applicable part group and part ranges.
      @SuppressWarnings( "unused" )
      InventoryKey lGrandChild11 = withSubInventory( lChild1, GRANDCHILD_INV_DESC,
            APPL_RANGE_NOT_CONTAINING_CODE_1, APPL_RANGE_CONTAINING_CODE_1 );

      // Grandchild12 (not applicable) has non-applicable part group and part ranges.
      InventoryKey lGrandChild12 = withSubInventory( lChild1, GRANDCHILD_INV_DESC,
            APPL_RANGE_NOT_CONTAINING_CODE_1, APPL_RANGE_NOT_CONTAINING_CODE_1 );

      execute( lMainInv, lTestInv );

      assertEquals( 3, iResultDs.getRowCount() );

      iResultDs.next();

      assertEquals( APPL_CODE_1, iResultDs.getString( "appl_code" ) );
      assertEquals( lChild2,
            iResultDs.getKey( InventoryKey.class, "test_inv_no_db_id", "test_inv_no_id" ) );
      assertEquals( SUB_INV_DESC, iResultDs.getString( "test_inv_no_sdesc" ) );
      assertEquals( APPL_RANGE_NOT_CONTAINING_CODE_1, iResultDs.getString( "part_range" ) );
      assertEquals( PART_NOT_APPLICABLE, iResultDs.getBoolean( "part_applicable" ) );
      assertEquals( NO_APPL_CODE, iResultDs.getString( "part_group_range" ) );
      assertEquals( PART_GROUP_APPLICABLE, iResultDs.getBoolean( "part_group_applicable" ) );

      iResultDs.next();

      assertEquals( APPL_CODE_1, iResultDs.getString( "appl_code" ) );
      assertEquals( lChild4,
            iResultDs.getKey( InventoryKey.class, "test_inv_no_db_id", "test_inv_no_id" ) );
      assertEquals( SUB_INV_DESC, iResultDs.getString( "test_inv_no_sdesc" ) );
      assertEquals( APPL_RANGE_NOT_CONTAINING_CODE_1, iResultDs.getString( "part_range" ) );
      assertEquals( PART_NOT_APPLICABLE, iResultDs.getBoolean( "part_applicable" ) );
      assertEquals( NO_APPL_CODE, iResultDs.getString( "part_group_range" ) );
      assertEquals( PART_GROUP_APPLICABLE, iResultDs.getBoolean( "part_group_applicable" ) );

      iResultDs.next();

      assertEquals( APPL_CODE_1, iResultDs.getString( "appl_code" ) );
      assertEquals( lGrandChild12,
            iResultDs.getKey( InventoryKey.class, "test_inv_no_db_id", "test_inv_no_id" ) );
      assertEquals( GRANDCHILD_INV_DESC, iResultDs.getString( "test_inv_no_sdesc" ) );
      assertEquals( APPL_RANGE_NOT_CONTAINING_CODE_1, iResultDs.getString( "part_range" ) );
      assertEquals( PART_NOT_APPLICABLE, iResultDs.getBoolean( "part_applicable" ) );
      assertEquals( NO_APPL_CODE, iResultDs.getString( "part_group_range" ) );
      assertEquals( PART_GROUP_APPLICABLE, iResultDs.getBoolean( "part_group_applicable" ) );

   }


   /**
    * Given an engine with no applicability code
    *
    * And a TRK inventory (main inventory) attached to the engine
    *
    * And a loose TRK inventory (test inventory) with it's sub-inventory (a TRK inventory) having a
    * part group applicability range and a part applicability range
    *
    * When the loose TRK inventory is reserved, issued, attached to the TRK inventory (execute
    * query)
    *
    * Then the query returns 0 rows
    */
   @Test
   public void
         testWhenNoAircraftAndNoEngineCodeAndTestInventoryHasSubInvWithPartGroupAndPartRange() {
      InventoryKey lMainInv = createTrkInventoryAttachedToEngine( NO_APPL_CODE );
      InventoryKey lTestInv = createTrkInventory();
      withSubInventory( lTestInv, SUB_INV_DESC, APPL_RANGE_NOT_CONTAINING_CODE_1,
            APPL_RANGE_NOT_CONTAINING_CODE_1 );

      execute( lMainInv, lTestInv );
      assertEquals( 0, iResultDs.getRowCount() );
   }


   /**
    * Given an engine (main inventory) with an applicability code
    *
    * And a loose TRK inventory (test inventory) with no applicability ranges
    *
    * When the loose TRK inventory is reserved, issued, attached to the engine (execute query)
    *
    * Then the query returns 0 rows
    */
   @Test
   public void testWhenLooseEngineHasApplCodeButTestInvHasWhenNoSubInv() {
      InventoryKey lMainInv = createEngineInventory( APPL_CODE_1 );
      InventoryKey lTestInv = createTrkInventory();

      execute( lMainInv, lTestInv );
      assertEquals( 0, iResultDs.getRowCount() );
   }


   /**
    * Verify that the test inventory itself is not tested for applicability, only its sub-inventory
    * are tested.
    */
   @Test
   public void testWhenTestInvIsNotApplicableButSubInvIsApplicable() {
      InventoryKey lMainInv = createTrkInventoryAttachedToEngine( APPL_CODE_1 );
      InventoryKey lTestInv = null;
      withSubInventory( lTestInv, SUB_INV_DESC, APPL_RANGE_NOT_CONTAINING_CODE_1,
            APPL_RANGE_NOT_CONTAINING_CODE_1 );
      withSubInventory( lTestInv, SUB_INV_DESC, APPL_RANGE_CONTAINING_CODE_1,
            APPL_RANGE_CONTAINING_CODE_1 );

      execute( lMainInv, lTestInv );
      assertEquals( 0, iResultDs.getRowCount() );
   }


   /**
    * Execute the query with the provided inventory keys.
    *
    * @param InventoryKey
    *           MainInventory key
    * @param InventoryKey
    *           TestInventory key
    */
   private void execute( InventoryKey aMainInventoryKey, InventoryKey aTestInventoryKey ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aMainInventoryKey, "aMainInvDbId", "aMainInvId" );
      lArgs.add( aTestInventoryKey, "aTestInvDbId", "aTestInvId" );

      iResultDs = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Create a engine inventory with an applicability code.
    *
    * @param aApplicabilityCode
    *
    *           Return InventoryKey
    */
   private InventoryKey createEngineInventory( final String aApplicabilityCode ) {
      return Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.setDescription( ASSEMBLY_INV_DESC );
            aEngine.setApplicabilityCode( aApplicabilityCode );
         }
      } );
   }


   /**
    * Create a main inventory as aircraft with an applicability code.
    *
    * @param aApplicabilityCode
    * @return InventoryKey
    */
   private InventoryKey createAircraftInventory( final String aApplicabilityCode ) {
      return Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setDescription( MAIN_INV_DESC );
            aAircraft.setApplicabilityCode( aApplicabilityCode );
         }

      } );
   }


   /**
    * Create a main inventory with an applicability code that also has an assembly inventory. The
    * assembly inventory is set with the provided applicability code.
    *
    * @param aAssemblyInvApplCode
    *           applicability code of the assembly inventory
    * @return InventoryKey
    */
   private InventoryKey createTrkInventoryAttachedToEngine( final String aAssemblyInvApplCode ) {

      // Create an assembly inventory with an applicability code.
      final InventoryKey lEngineInv = createEngineInventory( aAssemblyInvApplCode );

      // Create the main inventory with a different applicability code.
      return Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

         @Override
         public void configure( TrackedInventory aTrk ) {
            aTrk.setDescription( TRK_DESC );
            aTrk.setParent( lEngineInv );
         }
      } );
   }


   /**
    * Create an engine with an applicability code that also has an aircraft inventory. The aircraft
    * inventory is set with the provided applicability code.
    *
    * @param aAircraftInvApplCode
    *           applicability code of the aircraft inventory
    *
    * @param aEngineInvApplCode
    *           applicability code of the main inventory
    *
    * @return InventoryKey
    */
   private InventoryKey createEngineAttachedToAircraft( final String aAircraftInvApplCode,
         final String aEngineInvApplCode ) {

      final InventoryKey lEngineInv = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.setDescription( ASSEMBLY_INV_DESC );
            aEngine.setApplicabilityCode( aEngineInvApplCode );
         }
      } );

      Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setDescription( ACFT_INV_DESC );
            aAircraft.setApplicabilityCode( aAircraftInvApplCode );
            aAircraft.addEngine( lEngineInv );
         }

      } );
      return lEngineInv;
   }


   /**
    * Create a sub-inventory under the provided parent inventory with a part and part group having
    * the provided applicability ranges.
    *
    * @param aParentInv
    * @param aInvDesc
    * @param aPartGroupApplRange
    * @param aPartApplRange
    *
    * @return created sub-inventory key
    */
   private InventoryKey withSubInventory( final InventoryKey aParentInv, final String aInvDesc,
         final String aPartGroupApplRange, final String aPartApplRange ) {

      final PartGroupKey lPartGroup = Domain.createPartGroup( new DomainConfiguration<PartGroup>() {

         @Override
         public void configure( PartGroup aPartGroup ) {
            aPartGroup.setApplicabilityRange( aPartGroupApplRange );
            aPartGroup.setCode( PART_GROUP );
         }

      } );

      final PartNoKey lPartNoKey = Domain.createPart( new DomainConfiguration<Part>() {

         @Override
         public void configure( Part aPart ) {
            aPart.setInventoryClass( RefInvClassKey.TRK );
            aPart.setPartGroup( lPartGroup, Boolean.TRUE );
            aPart.setApplicabilityRange( aPartApplRange );
         }

      } );

      return Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

         @Override
         public void configure( TrackedInventory aTrk ) {
            aTrk.setDescription( aInvDesc );
            aTrk.setPartNumber( lPartNoKey );
            aTrk.setPartGroup( lPartGroup );
            aTrk.setParent( aParentInv );
         }
      } );
   }


   /**
    * Create a loose TRK test inventory
    *
    * return InventoryKey
    */
   private InventoryKey createTrkInventory() {

      return Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

         @Override
         public void configure( TrackedInventory aTrk ) {
            aTrk.setDescription( TRK_DESC );
         }
      } );
   }


   /**
    * Create a test inventory with the provided part group and part applicability ranges.
    *
    * @param aPartGroupApplRange
    * @param aPartApplRange
    * @return InventoryKey
    */
   private InventoryKey createTrkInventoryBasedOnPartNoAndPartGroup(
         final String aPartGroupApplRange, final String aPartApplRange ) {

      final PartGroupKey lPartGroup = Domain.createPartGroup( new DomainConfiguration<PartGroup>() {

         @Override
         public void configure( PartGroup aPartGroup ) {
            aPartGroup.setApplicabilityRange( aPartGroupApplRange );
            aPartGroup.setCode( PART_GROUP );
         }

      } );

      final PartNoKey lPartNo = Domain.createPart( new DomainConfiguration<Part>() {

         @Override
         public void configure( Part aPart ) {
            aPart.setInventoryClass( RefInvClassKey.TRK );
            aPart.setPartGroup( lPartGroup, Boolean.TRUE );
            aPart.setApplicabilityRange( aPartApplRange );
         }

      } );

      return Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

         @Override
         public void configure( TrackedInventory aTrk ) {
            aTrk.setDescription( TRK_DESC );
            aTrk.setPartNumber( lPartNo );
            aTrk.setPartGroup( lPartGroup );
         }
      } );
   }

}
