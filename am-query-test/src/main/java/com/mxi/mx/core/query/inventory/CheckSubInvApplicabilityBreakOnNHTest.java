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
 * This class tests the CheckSubInvApplicabilityBreakOnNH query.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class CheckSubInvApplicabilityBreakOnNHTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static final String NO_APPL_CODE = null;

   private static final String APPL_CODE_1 = "01";
   private static final String APPL_CODE_6 = "06";

   private static final String APPL_RANGE_CONTAINING_CODE_1 = "01-05";
   private static final String APPL_RANGE_NOT_CONTAINING_CODE_1 = "05-10";

   private static final String APPL_CODE_2 = "02";
   private static final String APPL_RANGE_NOT_CONTAINING_CODE_2 = "05-10";

   private static final String PART_GROUP = "PG";

   private static final String ASSEMBLY_INV_DESC = "ASSY_INV_DESC";
   private static final String ACFT_INV_DESC = "ACFT_INV_DESC";

   private static final String TRK_DESC = "TRK_DESC";

   private static final String CHILD_INV_DESC = "CHILD_INV_DESC";

   private static final String ANOTHER_CHILD_INV_DESC = "ANOTHER_CHILD_INV_DESC";

   private static final String GRANDCHILD_INV_DESC = "GRANDCHILD_INV_DESC";

   private static final boolean PART_APPLICABLE = true;
   private static final boolean PART_NOT_APPLICABLE = false;
   private static final boolean PART_GROUP_APPLICABLE = true;
   private static final boolean PART_GROUP_NOT_APPLICABLE = false;

   private static final String SUB_INV_DESC = "SUB_INV_DESC";

   private DataSet iResultDs;


   /**
    * Given an aircraft with an applicability code
    *
    * And an engine (main inventory) attached to the aircraft with an applicability code
    *
    * And a TRK inventory (test inventory) with sub-inventory with part group applicability range
    * and part applicability range such that the aircraft's applicability code falls inside both the
    * ranges
    *
    * And the engine's applicability code falls outside the part group applicability range and
    * outside the part applicability range
    *
    * When the TRK inventory is reserved, issued, attached to the engine (execute query)
    *
    * Then the query returns 1 row : sub-inventory not applicable due to both part group and part
    * range
    */
   @Test
   public void
         testWhenEngineCodeNotInPartGroupRangeNorPartRangeAndAircraftCodeInsidePartAndPartGroupRanges() {
      InventoryKey lMainInv = createEngineAttachedToAircraft( APPL_CODE_1, APPL_CODE_6 );
      InventoryKey lTestInv = createTrkInventory();
      InventoryKey lSubInv = withSubInventory( lTestInv, SUB_INV_DESC, APPL_RANGE_CONTAINING_CODE_1,
            APPL_RANGE_CONTAINING_CODE_1 );

      execute( lMainInv, lTestInv );

      assertEquals( 1, iResultDs.getRowCount() );
      iResultDs.next();

      assertEquals( APPL_CODE_6, iResultDs.getString( "appl_code" ) );
      assertEquals( lSubInv,
            iResultDs.getKey( InventoryKey.class, "test_inv_no_db_id", "test_inv_no_id" ) );
      assertEquals( SUB_INV_DESC, iResultDs.getString( "test_inv_no_sdesc" ) );
      assertEquals( APPL_RANGE_CONTAINING_CODE_1, iResultDs.getString( "part_range" ) );
      assertEquals( PART_NOT_APPLICABLE, iResultDs.getBoolean( "part_applicable" ) );
      assertEquals( APPL_RANGE_CONTAINING_CODE_1, iResultDs.getString( "part_group_range" ) );
      assertEquals( PART_GROUP_NOT_APPLICABLE, iResultDs.getBoolean( "part_group_applicable" ) );
   }


   /**
    * Given an aircraft with an applicability code
    *
    * And an engine (main inventory) attached to the aircraft with an applicability code
    *
    * And a TRK inventory (test inventory) with sub-inventory with part group applicability range
    * and part applicability range such that the aircraft's applicability code falls outside both
    * the ranges
    *
    * And the engine's applicability code falls inside the part group applicability range and
    * outside the part applicability range
    *
    * When the TRK inventory is reserved, issued, attached to the engine (execute query)
    *
    * Then the query returns 0 rows
    */
   @Test
   public void
         testWhenEngineCodeInPartGroupRangeNorPartRangeAndAircraftCodeNotInPartGroupAndPartRange() {
      InventoryKey lMainInv = createEngineAttachedToAircraft( APPL_CODE_6, APPL_CODE_1 );
      InventoryKey lTestInv = createTrkInventory();
      withSubInventory( lTestInv, SUB_INV_DESC, APPL_RANGE_CONTAINING_CODE_1,
            APPL_RANGE_CONTAINING_CODE_1 );

      execute( lMainInv, lTestInv );

      assertEquals( 0, iResultDs.getRowCount() );
   }


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
    * Then the query returns 1 row: Inapplicability between engine and the part group range of the
    * sub-inventory
    */
   @Test
   public void testWhenLooseEngineCodeInPartGroupRangeButNotInPartRange() {
      InventoryKey lMainInv = createEngineInventory( APPL_CODE_1 );
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
      assertEquals( APPL_RANGE_CONTAINING_CODE_1, iResultDs.getString( "part_group_range" ) );
      assertEquals( PART_GROUP_APPLICABLE, iResultDs.getBoolean( "part_group_applicable" ) );

   }


   /**
    * Given a loose engine (main inventory) with an applicability code
    *
    * And a TRK inventory (test inventory) with both part group and part applicability ranges
    * containing the engine's applicability code
    *
    * And the TRK inventory (test inventory) with a child TRK inventory with part group
    * applicability range containing engine's applicability code but part applicability range not
    * containing the code
    *
    * And the child TRK inventory has its own child TRK inventory(grand-child) with part group
    * applicability range not containing engine's applicability code and part applicability range
    * not containing the code
    *
    * And the TRK inventory (test inventory) has another child TRK inventory with part group
    * applicability range not containing engine's applicability code but part applicability range
    * containing the code
    *
    * When the TRK inventory is reserved, issued, attached to the engine (execute query)
    *
    * Then the query returns 0 rows
    */
   @Test
   public void testWhenLooseEngineCodeNotInMultiplePartRangesNorPartGroupRanges() {

      // Setup the test inventory to be applicable.
      String lTestInvPartGroupRange = APPL_RANGE_CONTAINING_CODE_1;
      String lTestInvPartRange = APPL_RANGE_CONTAINING_CODE_1;

      // Setup the child inventory to be not applicable due to part applicability range.
      String lChildInvPartGroupRange = APPL_RANGE_CONTAINING_CODE_1;
      String lChildInvPartRange = APPL_RANGE_NOT_CONTAINING_CODE_1;

      // Setup the other child inventory to be not applicable due to part group applicability range.
      String lAnotherChildInvPartGroupRange = APPL_RANGE_NOT_CONTAINING_CODE_1;
      String lAnotherChildInvPartRange = APPL_RANGE_CONTAINING_CODE_1;

      // Setup the grandchild inventory to be not applicable due to both applicability ranges.
      String lGrandchildInvPartGroupRange = APPL_RANGE_NOT_CONTAINING_CODE_1;
      String lGrandchildInvPartRange = APPL_RANGE_NOT_CONTAINING_CODE_1;

      InventoryKey lMainInv = createEngineInventory( APPL_CODE_1 );
      InventoryKey lTestInv = createTrkInventoryBasedOnPartNoAndPartGroup( lTestInvPartGroupRange,
            lTestInvPartRange );
      InventoryKey lChildInv = withSubInventory( lTestInv, CHILD_INV_DESC, lChildInvPartGroupRange,
            lChildInvPartRange );
      InventoryKey lAnotherChildInv = withSubInventory( lTestInv, ANOTHER_CHILD_INV_DESC,
            lAnotherChildInvPartGroupRange, lAnotherChildInvPartRange );
      InventoryKey lGrandChildInv = withSubInventory( lChildInv, GRANDCHILD_INV_DESC,
            lGrandchildInvPartGroupRange, lGrandchildInvPartRange );

      execute( lMainInv, lTestInv );

      assertEquals( 3, iResultDs.getRowCount() );

      iResultDs.next();

      assertEquals( APPL_CODE_1, iResultDs.getString( "appl_code" ) );
      assertEquals( lChildInv,
            iResultDs.getKey( InventoryKey.class, "test_inv_no_db_id", "test_inv_no_id" ) );
      assertEquals( CHILD_INV_DESC, iResultDs.getString( "test_inv_no_sdesc" ) );
      assertEquals( APPL_RANGE_NOT_CONTAINING_CODE_1, iResultDs.getString( "part_range" ) );
      assertEquals( PART_NOT_APPLICABLE, iResultDs.getBoolean( "part_applicable" ) );
      assertEquals( APPL_RANGE_CONTAINING_CODE_1, iResultDs.getString( "part_group_range" ) );
      assertEquals( PART_GROUP_APPLICABLE, iResultDs.getBoolean( "part_group_applicable" ) );
      iResultDs.next();

      assertEquals( APPL_CODE_1, iResultDs.getString( "appl_code" ) );
      assertEquals( lAnotherChildInv,
            iResultDs.getKey( InventoryKey.class, "test_inv_no_db_id", "test_inv_no_id" ) );
      assertEquals( ANOTHER_CHILD_INV_DESC, iResultDs.getString( "test_inv_no_sdesc" ) );
      assertEquals( APPL_RANGE_CONTAINING_CODE_1, iResultDs.getString( "part_range" ) );
      assertEquals( PART_APPLICABLE, iResultDs.getBoolean( "part_applicable" ) );
      assertEquals( APPL_RANGE_NOT_CONTAINING_CODE_1, iResultDs.getString( "part_group_range" ) );
      assertEquals( PART_GROUP_NOT_APPLICABLE, iResultDs.getBoolean( "part_group_applicable" ) );

      iResultDs.next();

      assertEquals( APPL_CODE_1, iResultDs.getString( "appl_code" ) );
      assertEquals( lGrandChildInv,
            iResultDs.getKey( InventoryKey.class, "test_inv_no_db_id", "test_inv_no_id" ) );
      assertEquals( GRANDCHILD_INV_DESC, iResultDs.getString( "test_inv_no_sdesc" ) );
      assertEquals( APPL_RANGE_NOT_CONTAINING_CODE_1, iResultDs.getString( "part_range" ) );
      assertEquals( PART_NOT_APPLICABLE, iResultDs.getBoolean( "part_applicable" ) );
      assertEquals( APPL_RANGE_NOT_CONTAINING_CODE_1, iResultDs.getString( "part_group_range" ) );
      assertEquals( PART_GROUP_NOT_APPLICABLE, iResultDs.getBoolean( "part_group_applicable" ) );

   }


   /**
    * Given an engine (main inventory) with an applicability code
    *
    * And a loose TRK inventory (test inventory) with no applicability code but it's sub-inventory
    * (a TRK inventory) with part group applicability range and part applicability range such that
    * the engine's applicability code falls outside the part group range but within the part range
    *
    * When the TRK component is attached to the engine (execute query)
    *
    * Then the query returns 1 row: inapplicability between the engine and the sub-inventory due to
    * part group range
    */
   @Test
   public void testWhenLooseEngineCodeNotInPartGroupRangeButInPartRange() {
      InventoryKey lMainInv = createEngineInventory( APPL_CODE_1 );
      InventoryKey lTestInv = createTrkInventory();
      InventoryKey lSubInv = withSubInventory( lTestInv, SUB_INV_DESC,
            APPL_RANGE_NOT_CONTAINING_CODE_1, APPL_RANGE_CONTAINING_CODE_1 );

      execute( lMainInv, lTestInv );

      assertEquals( 1, iResultDs.getRowCount() );
      iResultDs.next();

      assertEquals( APPL_CODE_1, iResultDs.getString( "appl_code" ) );
      assertEquals( lSubInv,
            iResultDs.getKey( InventoryKey.class, "test_inv_no_db_id", "test_inv_no_id" ) );
      assertEquals( SUB_INV_DESC, iResultDs.getString( "test_inv_no_sdesc" ) );
      assertEquals( APPL_RANGE_CONTAINING_CODE_1, iResultDs.getString( "part_range" ) );
      assertEquals( PART_APPLICABLE, iResultDs.getBoolean( "part_applicable" ) );
      assertEquals( APPL_RANGE_NOT_CONTAINING_CODE_1, iResultDs.getString( "part_group_range" ) );
      assertEquals( PART_GROUP_NOT_APPLICABLE, iResultDs.getBoolean( "part_group_applicable" ) );
   }


   /**
    * Given a loose engine (main inventory) with an applicability code
    *
    * And a loose TRK inventory (test inventory) with no applicability ranges
    *
    * And the loose TRK inventory (test inventory) has sub-inventory with part and part group
    * applicability ranges such that the applicability code of the engine falls outside of both the
    * ranges
    *
    * When the TRK inventory is attached to the engine (execute query)
    *
    * Then the query returns 1 rows: inapplicability between the engine and the sub-inventory due to
    * both the ranges
    */
   @Test
   public void testWhenLooseEngineCodeNotInPartGroupRangeNorPartRange() {

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
      assertEquals( APPL_RANGE_NOT_CONTAINING_CODE_1, iResultDs.getString( "part_range" ) );
      assertEquals( PART_NOT_APPLICABLE, iResultDs.getBoolean( "part_applicable" ) );
      assertEquals( APPL_RANGE_NOT_CONTAINING_CODE_1, iResultDs.getString( "part_group_range" ) );
      assertEquals( PART_GROUP_NOT_APPLICABLE, iResultDs.getBoolean( "part_group_applicable" ) );
   }


   /**
    * Given a loose engine (main inventory) with an applicability code with an applicability code
    *
    * And a TRK inventory (test inventory) with part group and part range applicability containing
    * engine's applicability code
    *
    * And the TRK inventory (test inventory) has a child TRK inventory with part group applicability
    * range containing engine's applicability code and part applicability range containing the code
    *
    * And the child TRK inventory has its own child TRK inventory(grand-child) with part group
    * applicability range not containing engine's applicability code and part applicability range
    * not containing the code
    *
    * When the TRK inventory is reserved, issued, attached to the engine (execute query)
    *
    * Then the query returns 0 rows
    */
   @Test
   public void testWhenLooseEngineCodeNotInSubInvPartRangeNorPartGroupRange() {

      // Setup the test inventory to be applicable.
      String lTestInvPartGroupRange = APPL_RANGE_CONTAINING_CODE_1;
      String lTestInvPartRange = APPL_RANGE_CONTAINING_CODE_1;

      // Setup the child inventory to be applicable.
      String lChildInvPartGroupRange = APPL_RANGE_CONTAINING_CODE_1;
      String lChildInvPartRange = APPL_RANGE_CONTAINING_CODE_1;

      // Setup the grandchild inventory to be not applicable due to both applicability ranges.
      String lGrandchildInvPartGroupRange = APPL_RANGE_NOT_CONTAINING_CODE_1;
      String lGrandchildInvPartRange = APPL_RANGE_NOT_CONTAINING_CODE_1;

      InventoryKey lMainInv = createEngineInventory( APPL_CODE_1 );
      InventoryKey lTestInv = createTrkInventoryBasedOnPartNoAndPartGroup( lTestInvPartGroupRange,
            lTestInvPartRange );
      InventoryKey lChildInv = withSubInventory( lTestInv, CHILD_INV_DESC, lChildInvPartGroupRange,
            lChildInvPartRange );
      InventoryKey lGrandChildInv = withSubInventory( lChildInv, GRANDCHILD_INV_DESC,
            lGrandchildInvPartGroupRange, lGrandchildInvPartRange );

      execute( lMainInv, lTestInv );
      iResultDs.next();

      assertEquals( 1, iResultDs.getRowCount() );
      assertEquals( APPL_CODE_1, iResultDs.getString( "appl_code" ) );
      assertEquals( lGrandChildInv,
            iResultDs.getKey( InventoryKey.class, "test_inv_no_db_id", "test_inv_no_id" ) );
      assertEquals( GRANDCHILD_INV_DESC, iResultDs.getString( "test_inv_no_sdesc" ) );
      assertEquals( APPL_RANGE_NOT_CONTAINING_CODE_1, iResultDs.getString( "part_range" ) );
      assertEquals( PART_NOT_APPLICABLE, iResultDs.getBoolean( "part_applicable" ) );
      assertEquals( APPL_RANGE_NOT_CONTAINING_CODE_1, iResultDs.getString( "part_group_range" ) );
      assertEquals( PART_GROUP_NOT_APPLICABLE, iResultDs.getBoolean( "part_group_applicable" ) );

   }


   /**
    * Given a loose engine (main inventory) with no applicability code
    *
    * And a loose TRK inventory (test inventory) with no applicability ranges
    *
    * And the loose TRK inventory (test inventory) has sub-inventory with part and part group
    * applicability ranges
    *
    * When the TRK inventory is attached to the engine (execute query)
    *
    * Then the query returns 0 rows
    */
   @Test
   public void testWhenMainInvHasNoCode() {
      InventoryKey lMainInv = createEngineInventory( NO_APPL_CODE );
      InventoryKey lTestInv = createTrkInventory();
      withSubInventory( lTestInv, SUB_INV_DESC, APPL_RANGE_NOT_CONTAINING_CODE_1,
            APPL_RANGE_NOT_CONTAINING_CODE_1 );

      execute( lMainInv, lTestInv );
      assertEquals( 0, iResultDs.getRowCount() );
   }


   /**
    * Given a loose engine (main inventory) with an applicability code
    *
    * And a loose TRK inventory (test inventory) with no applicability ranges
    *
    * And the loose TRK inventory (test inventory) has sub-inventory with part and part group
    * applicability ranges such that both the ranges don't contain the engine's applicability code
    *
    * When the TRK inventory is attached to the engine (execute query)
    *
    * Then the query returns 1 row: inapplicabilty between the sub-inventory and the engine due to
    * both the ranges
    */
   @Test
   public void
         testWhenMainInvIsNotEngineAndMainInvAttachedToEngineWithCodeNotInBothPartAndPartGroupRangeOfTestInvSubInv() {

      InventoryKey lMainInv = createTrkInventoryAttachedToEngine( APPL_CODE_2 );
      InventoryKey lTestInv = createTrkInventory();
      InventoryKey lSubInv = withSubInventory( lTestInv, SUB_INV_DESC,
            APPL_RANGE_NOT_CONTAINING_CODE_2, APPL_RANGE_NOT_CONTAINING_CODE_2 );

      execute( lMainInv, lTestInv );
      assertEquals( 1, iResultDs.getRowCount() );
      iResultDs.next();

      assertEquals( APPL_CODE_2, iResultDs.getString( "appl_code" ) );
      assertEquals( lSubInv,
            iResultDs.getKey( InventoryKey.class, "test_inv_no_db_id", "test_inv_no_id" ) );
      assertEquals( SUB_INV_DESC, iResultDs.getString( "test_inv_no_sdesc" ) );
      assertEquals( APPL_RANGE_NOT_CONTAINING_CODE_2, iResultDs.getString( "part_range" ) );
      assertEquals( PART_NOT_APPLICABLE, iResultDs.getBoolean( "part_applicable" ) );
      assertEquals( APPL_RANGE_NOT_CONTAINING_CODE_2, iResultDs.getString( "part_group_range" ) );
      assertEquals( PART_GROUP_NOT_APPLICABLE, iResultDs.getBoolean( "part_group_applicable" ) );

   }


   /**
    *
    *
    * Given a loose engine (main inventory) with an applicability code
    *
    * And a TRK inventory with no part group and part applicability range
    *
    * And the TRK inventory (test inventory) has a child TRK inventory (child1) with part group
    * applicability range containing engine's applicability code and part applicability range
    * containing the code
    *
    * And the child1 TRK inventory has a child TRK inventory (GrandChild11) with part group
    * applicability range containing engine's applicability code and part applicability range
    * containing the code
    *
    * And the child1 TRK inventory has another child TRK inventory (GrandChild12) with part group
    * applicability range not containing engine's applicability code and part applicability range
    * not containing the code
    *
    * And the TRK inventory has another child TRK inventory (child2) with part group applicability
    * range containing engine's applicability code and part applicability range not containing the
    * code
    *
    * And the TRK inventory has another child TRK inventory (child3) with part group applicability
    * range not containing engine's applicability code and part applicability range containing the
    * code
    *
    * And the TRK inventory has another child TRK inventory (child4) with part group applicability
    * range not containing engine's applicability code and part applicability range not containing
    * the code
    *
    * When the TRK inventory is reserved, issued, attached to the engine (execute query)
    *
    * Then the query returns 4 rows :child2 inventory is returned as not applicable due to part
    * range, child3 inventory is returned as not applicable due to part group range, child4
    * inventory is returned as not applicable due to both ranges and the GrandChild12 inventory is
    * returned as not applicable due to both ranges.
    */
   @Test
   public void testWhenMultipleNotApplicableSubInventory() {
      InventoryKey lMainInv = createEngineInventory( APPL_CODE_1 );
      InventoryKey lTestInv = createTrkInventory();

      // First level of sub-inventory.

      // Child1 (applicable) has applicable part and part group ranges.
      InventoryKey lChild1 = withSubInventory( lTestInv, CHILD_INV_DESC,
            APPL_RANGE_CONTAINING_CODE_1, APPL_RANGE_CONTAINING_CODE_1 );

      // Child2 (not applicable) has applicable part group range and non-applicable part range.
      InventoryKey lChild2 = withSubInventory( lTestInv, CHILD_INV_DESC,
            APPL_RANGE_CONTAINING_CODE_1, APPL_RANGE_NOT_CONTAINING_CODE_1 );

      // Child3 (not applicable) has non-applicable part group range and applicable part range.
      InventoryKey lChild3 = withSubInventory( lTestInv, CHILD_INV_DESC,
            APPL_RANGE_NOT_CONTAINING_CODE_1, APPL_RANGE_CONTAINING_CODE_1 );

      // Child4 (not applicable) has non-applicable part group and part ranges.
      InventoryKey lChild4 = withSubInventory( lTestInv, CHILD_INV_DESC,
            APPL_RANGE_NOT_CONTAINING_CODE_1, APPL_RANGE_NOT_CONTAINING_CODE_1 );

      // Second level of sub-inventory.

      // Grandchild11 (applicable) has applicable part group and part ranges.
      @SuppressWarnings( "unused" )
      InventoryKey lGrandChild11 = withSubInventory( lChild1, GRANDCHILD_INV_DESC,
            APPL_RANGE_CONTAINING_CODE_1, APPL_RANGE_CONTAINING_CODE_1 );

      // Grandchild12 (not applicable) has non-applicable part group and part ranges.
      InventoryKey lGrandChild12 = withSubInventory( lChild1, GRANDCHILD_INV_DESC,
            APPL_RANGE_NOT_CONTAINING_CODE_1, APPL_RANGE_NOT_CONTAINING_CODE_1 );

      execute( lMainInv, lTestInv );

      assertEquals( 4, iResultDs.getRowCount() );

      iResultDs.next();

      // Verify Child2 inapplicability due to part range
      assertEquals( APPL_CODE_1, iResultDs.getString( "appl_code" ) );
      assertEquals( lChild2,
            iResultDs.getKey( InventoryKey.class, "test_inv_no_db_id", "test_inv_no_id" ) );
      assertEquals( CHILD_INV_DESC, iResultDs.getString( "test_inv_no_sdesc" ) );
      assertEquals( APPL_RANGE_NOT_CONTAINING_CODE_1, iResultDs.getString( "part_range" ) );
      assertEquals( PART_NOT_APPLICABLE, iResultDs.getBoolean( "part_applicable" ) );
      assertEquals( APPL_RANGE_CONTAINING_CODE_1, iResultDs.getString( "part_group_range" ) );
      assertEquals( PART_GROUP_APPLICABLE, iResultDs.getBoolean( "part_group_applicable" ) );

      iResultDs.next();

      // Verify Child3 inapplicability due to part group range
      assertEquals( APPL_CODE_1, iResultDs.getString( "appl_code" ) );
      assertEquals( lChild3,
            iResultDs.getKey( InventoryKey.class, "test_inv_no_db_id", "test_inv_no_id" ) );
      assertEquals( CHILD_INV_DESC, iResultDs.getString( "test_inv_no_sdesc" ) );
      assertEquals( APPL_RANGE_CONTAINING_CODE_1, iResultDs.getString( "part_range" ) );
      assertEquals( PART_APPLICABLE, iResultDs.getBoolean( "part_applicable" ) );
      assertEquals( APPL_RANGE_NOT_CONTAINING_CODE_1, iResultDs.getString( "part_group_range" ) );
      assertEquals( PART_GROUP_NOT_APPLICABLE, iResultDs.getBoolean( "part_group_applicable" ) );

      iResultDs.next();

      // Verify Child4 inapplicability due to part and part group range
      assertEquals( APPL_CODE_1, iResultDs.getString( "appl_code" ) );
      assertEquals( lChild4,
            iResultDs.getKey( InventoryKey.class, "test_inv_no_db_id", "test_inv_no_id" ) );
      assertEquals( CHILD_INV_DESC, iResultDs.getString( "test_inv_no_sdesc" ) );
      assertEquals( APPL_RANGE_NOT_CONTAINING_CODE_1, iResultDs.getString( "part_range" ) );
      assertEquals( PART_NOT_APPLICABLE, iResultDs.getBoolean( "part_applicable" ) );
      assertEquals( APPL_RANGE_NOT_CONTAINING_CODE_1, iResultDs.getString( "part_group_range" ) );
      assertEquals( PART_GROUP_NOT_APPLICABLE, iResultDs.getBoolean( "part_group_applicable" ) );

      iResultDs.next();

      // Verify lGrandChild12 inapplicability due to part and part group range
      assertEquals( APPL_CODE_1, iResultDs.getString( "appl_code" ) );
      assertEquals( lGrandChild12,
            iResultDs.getKey( InventoryKey.class, "test_inv_no_db_id", "test_inv_no_id" ) );
      assertEquals( GRANDCHILD_INV_DESC, iResultDs.getString( "test_inv_no_sdesc" ) );
      assertEquals( APPL_RANGE_NOT_CONTAINING_CODE_1, iResultDs.getString( "part_range" ) );
      assertEquals( PART_NOT_APPLICABLE, iResultDs.getBoolean( "part_applicable" ) );
      assertEquals( APPL_RANGE_NOT_CONTAINING_CODE_1, iResultDs.getString( "part_group_range" ) );
      assertEquals( PART_GROUP_NOT_APPLICABLE, iResultDs.getBoolean( "part_group_applicable" ) );
   }


   /**
    * Given a loose engine (main inventory) with no applicability code
    *
    * And a loose TRK inventory (test inventory) with no applicability ranges
    *
    * And the loose TRK inventory (test inventory) has sub-inventory with part and part group
    * applicability ranges
    *
    * When the TRK inventory is attached to the engine (execute query)
    *
    * Then the query returns 0 rows
    */
   @Test
   public void testWhenMainInvAttachedToEngineHasNoPartAndPartGroupRangesAndEngineHasNoCode() {
      InventoryKey lMainInv = createTrkInventoryAttachedToEngine( NO_APPL_CODE );
      InventoryKey lTestInv = createTrkInventory();
      withSubInventory( lTestInv, SUB_INV_DESC, APPL_RANGE_NOT_CONTAINING_CODE_1,
            APPL_RANGE_NOT_CONTAINING_CODE_1 );

      execute( lMainInv, lTestInv );
      assertEquals( 0, iResultDs.getRowCount() );
   }


   /**
    * Given a loose engine (main inventory) with an applicability code
    *
    * And a loose TRK inventory (test inventory) with no applicability ranges
    *
    * When the TRK inventory is attached to the engine (execute query)
    *
    * Then the query returns 0 rows
    */
   @Test
   public void testWhenTestInvHasNoSubInv() {
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
    * Create a main inventory with an applicability code that also has an aircraft inventory. The
    * aircraft inventory is set with the provided applicability code.
    *
    * @param aAircraftInvApplCode
    *           applicability code of the aircraft inventory
    *
    * @param aMainInvApplCode
    *           applicability code of the main inventory
    *
    * @return InventoryKey
    */
   private InventoryKey createEngineAttachedToAircraft( final String aAircraftInvApplCode,
         final String aMainInvApplCode ) {

      final InventoryKey lEngineInv = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.setDescription( ASSEMBLY_INV_DESC );
            aEngine.setApplicabilityCode( aMainInvApplCode );
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

}
