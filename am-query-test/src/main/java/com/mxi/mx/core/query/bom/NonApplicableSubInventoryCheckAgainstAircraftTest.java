package com.mxi.mx.core.query.bom;

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
 * This class tests the NonApplicableSubInventoryCheckAgainstAircraft query.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class NonApplicableSubInventoryCheckAgainstAircraftTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   // Aircraft applicability codes
   private static final String APPL_CODE_A05 = "A05";

   // Engine applicability codes
   private static final String APPL_CODE_E07 = "E07";

   // Engine TRK part and part group applicability ranges
   private static final String APPL_RANGE_E01_TO_E10 = "E01-E10";
   private static final String APPL_RANGE_E05_TO_E08 = "E05-E08";

   // Aircraft TRK part and part group applicability ranges
   private static final String APPL_RANGE_A01_TO_A10 = "A01-A10";
   private static final String APPL_RANGE_A02_TO_A06 = "A02-A06";

   private static final String ACFT_INV_DESC = "ACFT_INV_DESC";
   private static final String ENGINE_DESC = "ENGINE_DESC";
   private static final String SUB_INV_DESC = "SUB_INV_DESC";
   private static final String GRAND_CHILD_INV_DESC = "GRAND_CHILD_INV_DESC";

   private static final String PART_GROUP = "PART_GROUP";

   // Query result columns
   private static final String SUB_INV_NO_DB_ID = "sub_inv_no_db_id";
   private static final String SUB_INV_NO_ID = "sub_inv_no_id";

   private DataSet iResultDs;


   /**
    * Given a loose engine with an applicability code
    *
    * And an engine TRK component with part and part group applicability ranges
    *
    * When the engine's applicability code is modified such that the modified code falls inside
    * engine TRK sub-inventory part range and inside part group applicability range (execute query)
    *
    * Then the query returns 0 rows (meaning no inapplicability)
    */
   @Test
   public void testWhenLooseEngineModifiedCodeInPartGroupApplicabilityRangeAndInPartRange() {
      // Given
      InventoryKey lLooseEngineKey = createLooseEngineInventory( APPL_CODE_E07 );
      String lSubInvPartGroupApplicabilityRange = APPL_RANGE_E01_TO_E10;
      String lSubInvPartApplicabilityRange = APPL_RANGE_E05_TO_E08;
      InventoryKey lParentInv = lLooseEngineKey;
      withTRKSubInventory( lParentInv, SUB_INV_DESC, lSubInvPartGroupApplicabilityRange,
            lSubInvPartApplicabilityRange );

      // When
      String lModifiedApplicabilityCode = "E05";
      execute( lLooseEngineKey, lModifiedApplicabilityCode );

      // Then
      assertEquals( 0, iResultDs.getRowCount() );

   }


   /**
    * Given a loose engine with an applicability code
    *
    * And an engine TRK component with part and part group applicability ranges
    *
    * When the engine's applicability code is modified such that the modified code falls outside
    * engine TRK sub-inventory part range and inside part group applicability range (execute query)
    *
    * Then the query returns 0 rows
    */
   @Test
   public void testWhenLooseEngineModifiedCodeInPartGroupApplicabilityRangeNotInPartRange() {
      // Given
      InventoryKey lLooseEngineKey = createLooseEngineInventory( APPL_CODE_E07 );
      String lSubInvPartGroupApplicabilityRange = APPL_RANGE_E01_TO_E10;
      String lSubInvPartApplicabilityRange = APPL_RANGE_E05_TO_E08;
      InventoryKey lParentInv = lLooseEngineKey;
      withTRKSubInventory( lParentInv, SUB_INV_DESC, lSubInvPartGroupApplicabilityRange,
            lSubInvPartApplicabilityRange );

      // When
      String lModifiedApplicabilityCode = "E09";
      execute( lLooseEngineKey, lModifiedApplicabilityCode );

      // Then
      assertEquals( 0, iResultDs.getRowCount() );
   }


   /**
    * Given a loose engine with an applicability code
    *
    * And an engine TRK component with part and part group applicability ranges
    *
    * When the engine's applicability code is modified such that the modified code falls outside
    * engine TRK sub-inventory part range and outside part group applicability range (execute query)
    *
    * Then the query returns 1 rows
    */
   @Test
   public void testWhenLooseEngineModifiedCodeNotInPartGroupApplicabilityRangeNotInPartRange() {
      // Given
      InventoryKey lLooseEngineKey = createLooseEngineInventory( APPL_CODE_E07 );
      String lSubInvPartGroupApplicabilityRange = APPL_RANGE_E01_TO_E10;
      String lSubInvPartApplicabilityRange = APPL_RANGE_E05_TO_E08;
      InventoryKey lParentInv = lLooseEngineKey;
      InventoryKey lSubInvKey = withTRKSubInventory( lParentInv, SUB_INV_DESC,
            lSubInvPartGroupApplicabilityRange, lSubInvPartApplicabilityRange );

      // When
      String lModifiedApplicabilityCode = "E15";
      execute( lLooseEngineKey, lModifiedApplicabilityCode );

      // Then
      assertEquals( 1, iResultDs.getRowCount() );
      iResultDs.next();
      assertEquals( "Incorrect sub-inventory returned as inapplicable", lSubInvKey,
            iResultDs.getKey( InventoryKey.class, SUB_INV_NO_DB_ID, SUB_INV_NO_ID ) );
   }


   /**
    * Given an aircraft with an applicability code
    *
    * And an attached engine with an applicability code
    *
    * And the engine has sub-inventory which has part group and part applicability ranges
    *
    * When the engine inventory's applicability code is modified such that the modified code falls
    * outside sub-inventory part range and inside part group applicability range (execute query)
    *
    * Then the query returns 0 rows
    */
   @Test
   public void testWhenEngineModifiedCodeInPartGroupApplicabilityRangeNotInPartRange() {
      // Given
      InventoryKey lAircraftKey = createAircraftInventory( APPL_CODE_A05 );
      String lEngineInvApplCode = APPL_CODE_E07;
      InventoryKey lEngineKey = createEngineAttachedToAircraft( lEngineInvApplCode, lAircraftKey );
      String lSubInvPartGroupApplicabilityRange = APPL_RANGE_E01_TO_E10;
      String lSubInvPartApplicabilityRange = APPL_RANGE_E05_TO_E08;
      InventoryKey lParentInv = lEngineKey;
      withTRKSubInventory( lParentInv, SUB_INV_DESC, lSubInvPartGroupApplicabilityRange,
            lSubInvPartApplicabilityRange );

      // When
      String lModifiedApplicabilityCode = "E09";
      execute( lEngineKey, lModifiedApplicabilityCode );

      // Then
      assertEquals( 0, iResultDs.getRowCount() );
   }


   /**
    * Given an aircraft with an applicability code
    *
    * And an attached engine with an applicability code
    *
    * And the engine has sub-inventory which has part group and part applicability ranges
    *
    * When the engine inventory's applicability code is modified such that the modified code falls
    * outside sub-inventory part range and outside part group applicability range (execute query)
    *
    * Then the query returns 1 rows
    */
   @Test
   public void testWhenEngineModifiedCodeNotInPartGroupApplicabilityRangeNotInPartRange() {
      // Given
      InventoryKey lAircraftKey = createAircraftInventory( APPL_CODE_A05 );
      String lEngineInvApplCode = APPL_CODE_E07;
      InventoryKey lEngineKey = createEngineAttachedToAircraft( lEngineInvApplCode, lAircraftKey );
      String lSubInvPartGroupApplicabilityRange = APPL_RANGE_E01_TO_E10;
      String lSubInvPartApplicabilityRange = APPL_RANGE_E05_TO_E08;
      InventoryKey lParentInv = lEngineKey;
      InventoryKey lSubInvKey = withTRKSubInventory( lParentInv, SUB_INV_DESC,
            lSubInvPartGroupApplicabilityRange, lSubInvPartApplicabilityRange );

      // When
      String lModifiedApplicabilityCode = "E15";
      execute( lEngineKey, lModifiedApplicabilityCode );

      // Then
      assertEquals( 1, iResultDs.getRowCount() );
      iResultDs.next();
      assertEquals( "Incorrect sub-inventory returned as inapplicable", lSubInvKey,
            iResultDs.getKey( InventoryKey.class, SUB_INV_NO_DB_ID, SUB_INV_NO_ID ) );

   }


   /**
    * Given an aircraft with an applicability code
    *
    * And an attached engine with an applicability code
    *
    * And the engine has sub-inventory which has part group and part applicability ranges
    *
    * When the aircraft inventory's applicability code is modified such that the modified code falls
    * outside the engine sub-inventory part range and outside engine sub-inventory's part group
    * applicability range (execute query)
    *
    * Then the query returns 1 row
    */
   @Test
   public void
         testWhenAircraftModifiedCodeNotInEngineSubInventoryPartAndPartGroupApplicabilityRange() {
      // Given
      InventoryKey lAircraftKey = createAircraftInventory( APPL_CODE_A05 );
      String lEngineInvApplCode = APPL_CODE_E07;
      InventoryKey lEngineKey = createEngineAttachedToAircraft( lEngineInvApplCode, lAircraftKey );
      String lSubInvPartGroupApplicabilityRange = APPL_RANGE_E01_TO_E10;
      String lSubInvPartApplicabilityRange = APPL_RANGE_E05_TO_E08;
      InventoryKey lParentInv = lEngineKey;
      InventoryKey lSubInvKey = withTRKSubInventory( lParentInv, SUB_INV_DESC,
            lSubInvPartGroupApplicabilityRange, lSubInvPartApplicabilityRange );

      // When
      String lModifiedApplicabilityCode = "A07";
      execute( lAircraftKey, lModifiedApplicabilityCode );

      // Then
      assertEquals( 1, iResultDs.getRowCount() );
      iResultDs.next();
      assertEquals( "Incorrect sub-inventory returned as inapplicable", lSubInvKey,
            iResultDs.getKey( InventoryKey.class, SUB_INV_NO_DB_ID, SUB_INV_NO_ID ) );

   }


   /**
    * Given an aircraft with an applicability code
    *
    * And an attached engine with an applicability code
    *
    * And the engine has sub-inventory which has part group and part applicability ranges
    *
    * When the aircraft inventory's applicability code is modified such that the modified code falls
    * inside the engine sub-inventory part range and outside engine sub-inventory's part group
    * applicability range (execute query)
    *
    * Then the query returns 0 rows
    */
   @Test
   public void
         testWhenAircraftModifiedCodeNotInEngineSubInventoryPartGroupApplicabilityRangeButInPartRange() {
      // Given
      InventoryKey lAircraftKey = createAircraftInventory( APPL_CODE_A05 );

      String lEngineInvApplCode = APPL_CODE_E07;
      InventoryKey lEngineKey = createEngineAttachedToAircraft( lEngineInvApplCode, lAircraftKey );
      String lSubInvPartGroupApplicabilityRange = APPL_RANGE_E01_TO_E10;
      String lSubInvPartApplicabilityRange = APPL_RANGE_A02_TO_A06;
      InventoryKey lParentInv = lEngineKey;
      withTRKSubInventory( lParentInv, SUB_INV_DESC, lSubInvPartGroupApplicabilityRange,
            lSubInvPartApplicabilityRange );

      // When
      String lModifiedApplicabilityCode = "A04";
      execute( lAircraftKey, lModifiedApplicabilityCode );

      // Then
      assertEquals( 0, iResultDs.getRowCount() );
   }


   /**
    * Given an aircraft with an applicability code
    *
    * And an aircraft TRK component with part and part group applicability ranges
    *
    * When the aircraft inventory's applicability code is modified such that the modified code falls
    * outside aircraft TRK sub-inventory part range and inside part group applicability range
    * (execute query)
    *
    * Then the query returns 1 rows
    */
   @Test
   public void testWhenAircraftModifiedCodeInPartGroupApplicabilityRangeNotInPartRange() {
      // Given
      InventoryKey lAircraftKey = createAircraftInventory( APPL_CODE_A05 );

      String lSubInvPartGroupApplicabilityRange = APPL_RANGE_A01_TO_A10;
      String lSubInvPartApplicabilityRange = APPL_RANGE_A02_TO_A06;
      InventoryKey lParentInv = lAircraftKey;
      InventoryKey lSubInvKey = withTRKSubInventory( lParentInv, SUB_INV_DESC,
            lSubInvPartGroupApplicabilityRange, lSubInvPartApplicabilityRange );

      // When
      String lModifiedApplicabilityCode = "A09";
      execute( lAircraftKey, lModifiedApplicabilityCode );

      // Then
      assertEquals( 1, iResultDs.getRowCount() );
      iResultDs.next();
      assertEquals( "Incorrect sub-inventory returned as inapplicable", lSubInvKey,
            iResultDs.getKey( InventoryKey.class, SUB_INV_NO_DB_ID, SUB_INV_NO_ID ) );
   }


   /**
    * Given an aircraft with an applicability code
    *
    * And an aircraft TRK component with part and part group applicability ranges
    *
    * When the aircraft inventory's applicability code is modified such that the modified code falls
    * outside aircraft TRK sub-inventory part range and outside part group applicability range
    * (execute query)
    *
    * Then the query returns 1 rows
    */
   @Test
   public void testWhenAircraftModifiedCodeNotInPartGroupApplicabilityRangeNotInPartRange() {
      // Given
      InventoryKey lAircraftKey = createAircraftInventory( APPL_CODE_A05 );

      String lSubInvPartGroupApplicabilityRange = APPL_RANGE_A01_TO_A10;
      String lSubInvPartApplicabilityRange = APPL_RANGE_A02_TO_A06;
      InventoryKey lParentInv = lAircraftKey;
      InventoryKey lSubInvKey = withTRKSubInventory( lParentInv, SUB_INV_DESC,
            lSubInvPartGroupApplicabilityRange, lSubInvPartApplicabilityRange );

      // When
      String lModifiedApplicabilityCode = "A19";
      execute( lAircraftKey, lModifiedApplicabilityCode );

      // Then
      assertEquals( 1, iResultDs.getRowCount() );
      iResultDs.next();
      assertEquals( "Incorrect sub-inventory returned as inapplicable", lSubInvKey,
            iResultDs.getKey( InventoryKey.class, SUB_INV_NO_DB_ID, SUB_INV_NO_ID ) );
   }


   /**
    * Given an aircraft with an applicability code
    *
    * And an attached engine with an applicability code
    *
    * And the engine has sub-inventory which has part group and part applicability ranges
    *
    * When the engine inventory's applicability code is modified and set as blank (execute query)
    *
    * Then the query returns 0 rows (meaning there is no inapplicability)
    */
   @Test
   public void testWhenEngineModifiedCodeIsBlank() {
      // Given
      InventoryKey lAircraftKey = createAircraftInventory( APPL_CODE_A05 );

      String lEngineInvApplCode = APPL_CODE_E07;
      InventoryKey lEngineKey = createEngineAttachedToAircraft( lEngineInvApplCode, lAircraftKey );
      String lSubInvPartGroupApplicabilityRange = APPL_RANGE_E01_TO_E10;
      String lSubInvPartApplicabilityRange = APPL_RANGE_E05_TO_E08;
      InventoryKey lParentInv = lEngineKey;
      withTRKSubInventory( lParentInv, SUB_INV_DESC, lSubInvPartGroupApplicabilityRange,
            lSubInvPartApplicabilityRange );

      // When
      String lModifiedApplicabilityCode = null;
      execute( lEngineKey, lModifiedApplicabilityCode );

      // Then
      assertEquals( 0, iResultDs.getRowCount() );
   }


   /**
    * Given an aircraft with an applicability code
    *
    * And an aircraft TRK component with part and part group applicability ranges
    *
    * When the aircraft inventory's applicability code is modified and set as blank (execute query)
    *
    * Then the query returns 0 rows (meaning there is no inapplicability)
    */
   @Test
   public void testWhenAircraftModifiedCodeIsBlank() {
      // Given
      InventoryKey lAircraftKey = createAircraftInventory( APPL_CODE_A05 );

      String lSubInvPartGroupApplicabilityRange = APPL_RANGE_A01_TO_A10;
      String lSubInvPartApplicabilityRange = APPL_RANGE_A02_TO_A06;
      InventoryKey lParentInv = lAircraftKey;
      withTRKSubInventory( lParentInv, SUB_INV_DESC, lSubInvPartGroupApplicabilityRange,
            lSubInvPartApplicabilityRange );

      // When
      String lModifiedApplicabilityCode = null;
      execute( lAircraftKey, lModifiedApplicabilityCode );

      // Then
      assertEquals( 0, iResultDs.getRowCount() );
   }


   /**
    * Given an aircraft with an applicability code
    *
    * And an attached engine with an applicability code
    *
    * And the engine child inventory (TRK) which has part group and part applicability ranges
    *
    * And the child inventory (TRK) has its own sub-inventory (grand-child) with part group and part
    * applicability ranges
    *
    * When the engine inventory's applicability code is modified such that the modified code falls
    * outside child-inventory and child's sub-inventory (grand-child) part ranges and inside part
    * group applicability range (execute query)
    *
    * Then the query returns 0 rows
    */
   @Test
   public void
         testWhenEngineModifiedCodeInPartGroupApplicabilityRangeNotInChildAndGrandChildPartRange() {
      // Given
      InventoryKey lAircraftKey = createAircraftInventory( APPL_CODE_A05 );

      String lEngineInvApplCode = APPL_CODE_E07;
      InventoryKey lEngineKey = createEngineAttachedToAircraft( lEngineInvApplCode, lAircraftKey );
      String lSubInvPartGroupApplicabilityRange = APPL_RANGE_E01_TO_E10;
      String lSubInvPartApplicabilityRange = APPL_RANGE_E05_TO_E08;
      InventoryKey lParentInv = lEngineKey;
      InventoryKey lSubInvKey = withTRKSubInventory( lParentInv, SUB_INV_DESC,
            lSubInvPartGroupApplicabilityRange, lSubInvPartApplicabilityRange );

      String lGrandChildPartGroupApplicabilityRange = APPL_RANGE_E01_TO_E10;
      String lGrandChildPartApplicabilityRange = APPL_RANGE_E05_TO_E08;
      lParentInv = lSubInvKey;
      withTRKSubInventory( lParentInv, GRAND_CHILD_INV_DESC, lGrandChildPartGroupApplicabilityRange,
            lGrandChildPartApplicabilityRange );

      // When
      String lModifiedApplicabilityCode = "E09";
      execute( lEngineKey, lModifiedApplicabilityCode );

      // Then
      assertEquals( 0, iResultDs.getRowCount() );
   }


   /**
    * Given an aircraft with an applicability code
    *
    * And an attached engine with an applicability code
    *
    * And the engine child inventory (TRK) which has part group and part applicability ranges
    *
    * And the child inventory (TRK) has its own sub-inventory (grand-child) with part group and part
    * applicability ranges
    *
    * When the aircraft inventory's applicability code is modified such that the modified code falls
    * outside engine's child-inventory and engine's grand-child part ranges and outside engine's
    * child-inventory and engine's grand-child part group ranges (execute query)
    *
    * Then the query returns 2 rows: 1 row with inapplicability between the engine's child and 1
    * with inapplicability between engine's grand-child and the aircraft
    */
   @Test
   public void
         testWhenAircraftModifiedCodeInPartGroupApplicabilityRangeNotInChildAndGrandChildPartRange() {
      // Given
      InventoryKey lAircraftKey = createAircraftInventory( APPL_CODE_A05 );

      String lEngineInvApplCode = APPL_CODE_E07;
      InventoryKey lEngineKey = createEngineAttachedToAircraft( lEngineInvApplCode, lAircraftKey );
      String lSubInvPartGroupApplicabilityRange = APPL_RANGE_E01_TO_E10;
      String lSubInvPartApplicabilityRange = APPL_RANGE_E05_TO_E08;
      InventoryKey lParentInv = lEngineKey;
      InventoryKey lSubInvKey = withTRKSubInventory( lParentInv, SUB_INV_DESC,
            lSubInvPartGroupApplicabilityRange, lSubInvPartApplicabilityRange );

      String lGrandChildPartGroupApplicabilityRange = APPL_RANGE_E01_TO_E10;
      String lGrandChildPartApplicabilityRange = APPL_RANGE_E05_TO_E08;
      lParentInv = lSubInvKey;
      InventoryKey lGrandChildInvKey = withTRKSubInventory( lParentInv, GRAND_CHILD_INV_DESC,
            lGrandChildPartGroupApplicabilityRange, lGrandChildPartApplicabilityRange );

      // When
      String lModifiedApplicabilityCode = "A09";
      execute( lAircraftKey, lModifiedApplicabilityCode );

      // Then
      assertEquals( 2, iResultDs.getRowCount() );
      iResultDs.next();
      assertEquals( "Incorrect sub-inventory returned as inapplicable", lSubInvKey,
            iResultDs.getKey( InventoryKey.class, SUB_INV_NO_DB_ID, SUB_INV_NO_ID ) );
      iResultDs.next();
      assertEquals( "Incorrect grandchild inventory returned as inapplicable", lGrandChildInvKey,
            iResultDs.getKey( InventoryKey.class, SUB_INV_NO_DB_ID, SUB_INV_NO_ID ) );
   }


   /**
    * Create engine with an applicability code attached to the provided aircraft
    *
    * @param aEngineInvApplCode
    *           applicability code of the engine inventory
    *
    * @param aAircraftInvKey
    *           Inventory Key for the aircraft
    *
    * @return InventoryKey Engine Inventory
    */
   private InventoryKey createEngineAttachedToAircraft( final String aEngineInvApplCode,
         final InventoryKey aAircraftInvKey ) {

      return Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.setDescription( ENGINE_DESC );
            aEngine.setApplicabilityCode( aEngineInvApplCode );
            aEngine.setParent( aAircraftInvKey );
         }
      } );

   }


   /**
    * Create an aircraft with an applicability code.
    *
    * @param aApplicabilityCode
    * @return InventoryKey
    */
   private InventoryKey createAircraftInventory( final String aAircraftInvApplCode ) {
      return Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setDescription( ACFT_INV_DESC );
            aAircraft.setApplicabilityCode( aAircraftInvApplCode );
         }

      } );
   }


   /**
    * Execute the query with the provided inventory key and applicability code.
    *
    * @param aInventoryKey
    *           Inventory Key of the inventory whose applicability code is being modified
    * @param aApplicabilityCode
    *           Modified applicability code
    */
   private void execute( InventoryKey aInventoryKey, String aApplicabilityCode ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aInventoryKey, "aInvNoDbId", "aInvNoId" );
      lArgs.add( "aCode", aApplicabilityCode );

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
   private InventoryKey createLooseEngineInventory( final String aApplicabilityCode ) {
      return Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.setDescription( ENGINE_DESC );
            aEngine.setApplicabilityCode( aApplicabilityCode );
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
   private InventoryKey withTRKSubInventory( final InventoryKey aParentInv, final String aInvDesc,
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
