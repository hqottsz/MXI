package com.mxi.mx.core.query.part;

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
 * This class tests the ApplicabilityIncompatibilitiesWithNHAssembly query.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class ApplicabilityIncompatibilitiesWithNHAssemblyTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static final String ACFT_INV_DESC = "ACFT_INV_DESC";
   private static final String ENGINE_INV_DESC = "ENGINE_INV_DESC";
   private static final String TRK_INV_DESC = "TRK_INV_DESC";
   private static final String PART_GROUP_CD = "PART_GROUP_CD";

   // Applicability codes and ranges
   private static final String NO_APPL_CODE = null;
   private static final String APPL_CODE_1 = "01";
   private static final String APPL_CODE_6 = "06";

   private static final String APPL_RANGE_CONTAINING_CODE_1 = "01-05";
   private static final String APPL_RANGE_NOT_CONTAINING_CODE_1 = "06-10";
   private static final String APPL_RANGE_CONTAINING_CODE_6 = "06-10";
   private static final String APPL_RANGE_NOT_CONTAINING_CODE_6 = "01-05";

   // Applicability Flags
   private static final boolean PART_APPLICABLE = true;
   private static final boolean PART_NOT_APPLICABLE = false;
   private static final boolean PART_GROUP_APPLICABLE = true;
   private static final boolean PART_GROUP_NOT_APPLICABLE = false;

   private DataSet iResultDs;


   /**
    * Test Scenario: With an engine attached to an aircraft, attempt to attach a TRK engine
    * component to engine
    *
    * Given a loose TRK engine component with a part applicability range and a part group range
    *
    * AND an aircraft that has an applicability code that is not included in the part and part group
    * applicability range of loose TRK engine component
    *
    * AND an engine connected to the aircraft has an applicability code that is not included in the
    * part and part group applicability range of loose TRK engine component
    *
    * When attempting to to reserve,issue,attach component to the engine (execute query)
    *
    * Then the query returns 2 rows with inapplicability between the engine code and the loose TRK
    * part range & inapplicability between the engine code and the loose TRK part group
    * applicability range
    */
   @Test
   public void
         testWhenAircraftCodeNotInPartAndPartGroupRangeAndEngineCodeNotInPartAndPartGroupRangeOfEngineTRKGettingInstalled() {

      // Given
      InventoryKey lAircraftKey = createAircraftInventory( APPL_CODE_1 );
      String lEngineInvApplCode = APPL_CODE_1;
      InventoryKey lMainInv = createEngineAttachedToAircraft( lEngineInvApplCode, lAircraftKey );
      PartGroupKey lPartGroup = createPartGroup( APPL_RANGE_NOT_CONTAINING_CODE_1 );
      PartNoKey lPartNo = createTRKPart( lPartGroup, APPL_RANGE_NOT_CONTAINING_CODE_1 );
      createLooseTrkInventoryWithPartNoAndPartGroup( lPartNo, lPartGroup );

      // When
      execute( lMainInv, lPartNo, lPartGroup );

      // Then
      assertEquals( 2, iResultDs.getRowCount() );
      iResultDs.next();

      // Verify part group inapplicability between the engine code and the loose TRK part group
      // applicability range
      assertEquals( lMainInv.toString(), iResultDs.getString( "ass_inv_key" ) );
      assertEquals( APPL_CODE_1, iResultDs.getString( "appl_eff_cd" ) );
      assertEquals( APPL_RANGE_NOT_CONTAINING_CODE_1,
            iResultDs.getString( "part_group_appl_eff_ldesc" ) );
      assertEquals( PART_GROUP_NOT_APPLICABLE,
            iResultDs.getBoolean( "part_group_applicable_bool" ) );
      assertEquals( lPartGroup,
            iResultDs.getKey( PartGroupKey.class, "bom_part_db_id", "bom_part_id" ) );
      assertEquals( NO_APPL_CODE, iResultDs.getString( "part_appl_eff_ldesc" ) );
      assertEquals( PART_APPLICABLE, iResultDs.getBoolean( "part_applicable_bool" ) );
      assertEquals( null, iResultDs.getKey( PartNoKey.class, "part_no_db_id", "part_no_id" ) );

      iResultDs.next();

      // Verify part inapplicability between the engine code and the loose TRK part
      // applicability range
      assertEquals( lMainInv.toString(), iResultDs.getString( "ass_inv_key" ) );
      assertEquals( APPL_CODE_1, iResultDs.getString( "appl_eff_cd" ) );
      assertEquals( NO_APPL_CODE, iResultDs.getString( "part_group_appl_eff_ldesc" ) );
      assertEquals( PART_GROUP_APPLICABLE, iResultDs.getBoolean( "part_group_applicable_bool" ) );
      assertEquals( lPartGroup,
            iResultDs.getKey( PartGroupKey.class, "bom_part_db_id", "bom_part_id" ) );
      assertEquals( APPL_RANGE_NOT_CONTAINING_CODE_1,
            iResultDs.getString( "part_appl_eff_ldesc" ) );
      assertEquals( PART_NOT_APPLICABLE, iResultDs.getBoolean( "part_applicable_bool" ) );
      assertEquals( lPartNo, iResultDs.getKey( PartNoKey.class, "part_no_db_id", "part_no_id" ) );

   }


   /**
    * Test Scenario: With an engine attached to an aircraft, attempt to attach a TRK engine
    * component to engine
    *
    * Given a loose TRK engine component with no part and part group applicability ranges
    *
    * AND an aircraft that has an applicability code
    *
    * AND an engine connected to the aircraft has an applicability code
    *
    * When attempting to to reserve,issue,attach component to the engine (execute query)
    *
    * Then the query returns 0 rows
    */
   @Test
   public void
         testWhenEngineTRKGettingInstalledToEngineAttachedToAircraftHasNoPartAndPartGroupApplicabilityRanges() {

      // Given
      InventoryKey lAircraftKey = createAircraftInventory( APPL_CODE_1 );
      String lEngineInvApplCode = APPL_CODE_6;
      InventoryKey lMainInv = createEngineAttachedToAircraft( lEngineInvApplCode, lAircraftKey );
      PartGroupKey lPartGroup = createPartGroup( NO_APPL_CODE );
      PartNoKey lPartNo = createTRKPart( lPartGroup, NO_APPL_CODE );
      createLooseTrkInventoryWithPartNoAndPartGroup( lPartNo, lPartGroup );

      // When
      execute( lMainInv, lPartNo, lPartGroup );

      // Then
      assertEquals( 0, iResultDs.getRowCount() );
   }


   /**
    * Test Scenario: With an engine attached to an aircraft, attempt to attach a TRK engine
    * component to engine
    *
    * Given a loose TRK engine component with a part applicability range and a part group range
    *
    * AND an aircraft that has an applicability code that is included in the part applicability
    * range and part group applicability range of loose TRK engine component
    *
    * AND an engine connected to the aircraft has an applicability code that is included in the part
    * and part group applicability range of loose TRK engine component
    *
    * When attempting to to reserve,issue,attach component to the engine (execute query)
    *
    * Then the query returns 0 rows
    */
   @Test
   public void
         testWhenAircraftCodeInPartAndPartGroupRangeAndEngineCodeInPartAndPartGroupRangeOfEngineTRKGettingInstalled() {

      // Given
      InventoryKey lAircraftKey = createAircraftInventory( APPL_CODE_1 );
      String lEngineInvApplCode = APPL_CODE_1;
      InventoryKey lMainInv = createEngineAttachedToAircraft( lEngineInvApplCode, lAircraftKey );
      PartGroupKey lPartGroup = createPartGroup( APPL_RANGE_CONTAINING_CODE_1 );
      PartNoKey lPartNo = createTRKPart( lPartGroup, APPL_RANGE_CONTAINING_CODE_1 );
      createLooseTrkInventoryWithPartNoAndPartGroup( lPartNo, lPartGroup );

      // When
      execute( lMainInv, lPartNo, lPartGroup );

      // Then
      assertEquals( 0, iResultDs.getRowCount() );
   }


   /**
    * Test Scenario: With an engine attached to an aircraft, attempt to attach a TRK engine
    * component to engine
    *
    * Given a loose TRK engine component with a part applicability range and part group
    * applicability range
    *
    * AND an aircraft that has an applicability code that is included in the part applicability
    * range and not in the part group applicability range of loose TRK engine component
    *
    * AND an engine connected to the aircraft has an applicability code that is not included in the
    * part range of loose TRK engine component but in the part group applicability range
    *
    * When attempting to to reserve,issue,attach component to the engine (execute query)
    *
    * Then the query returns 1 rows with inapplicability between the engine code and the loose TRK
    * part range
    */
   @Test
   public void
         testWhenAircraftCodeInPartRangeNotInPartGroupRangeAndEngineCodeNotInPartRangButInPartGroupRangeeOfEngineTRKGettingInstalled() {

      // Given
      InventoryKey lAircraftKey = createAircraftInventory( NO_APPL_CODE );
      String lEngineInvApplCode = APPL_CODE_6;
      InventoryKey lMainInv = createEngineAttachedToAircraft( lEngineInvApplCode, lAircraftKey );
      PartGroupKey lPartGroup = createPartGroup( APPL_RANGE_CONTAINING_CODE_6 );
      PartNoKey lPartNo = createTRKPart( lPartGroup, APPL_RANGE_CONTAINING_CODE_1 );
      createLooseTrkInventoryWithPartNoAndPartGroup( lPartNo, lPartGroup );

      // When
      execute( lMainInv, lPartNo, lPartGroup );

      // Then
      assertEquals( 1, iResultDs.getRowCount() );

      iResultDs.next();

      // Verify part inapplicability between the engine code and the loose TRK part
      // applicability range
      assertEquals( lMainInv.toString(), iResultDs.getString( "ass_inv_key" ) );
      assertEquals( APPL_CODE_6, iResultDs.getString( "appl_eff_cd" ) );
      assertEquals( NO_APPL_CODE, iResultDs.getString( "part_group_appl_eff_ldesc" ) );
      assertEquals( PART_GROUP_APPLICABLE, iResultDs.getBoolean( "part_group_applicable_bool" ) );
      assertEquals( lPartGroup,
            iResultDs.getKey( PartGroupKey.class, "bom_part_db_id", "bom_part_id" ) );
      assertEquals( APPL_RANGE_NOT_CONTAINING_CODE_6,
            iResultDs.getString( "part_appl_eff_ldesc" ) );
      assertEquals( PART_NOT_APPLICABLE, iResultDs.getBoolean( "part_applicable_bool" ) );
      assertEquals( lPartNo, iResultDs.getKey( PartNoKey.class, "part_no_db_id", "part_no_id" ) );
   }


   /**
    * Test Scenario: With an engine attached to an aircraft, attempt to attach a TRK engine
    * component to engine
    *
    * Given a loose TRK engine component with a part applicability range
    *
    * AND an aircraft that has an applicability code that is not included in the part applicability
    * range of loose TRK engine component but in the part group applicability range
    *
    * AND an engine connected to the aircraft has an applicability code that is included in the part
    * range of loose TRK engine component but not in the part group applicability range
    *
    * When attempting to to reserve,issue,attach component to the engine (execute query)
    *
    * Then the query returns 1 row with inapplicability between the engine code and the loose TRK
    * part group applicability range range
    */
   @Test
   public void
         testWhenAircraftCodeInPartGroupRangeNotInPartRangeAndEngineCodeInPartRangeNotInPartGroupRangeOfEngineTRKGettingInstalled() {

      // Given
      InventoryKey lAircraftKey = createAircraftInventory( NO_APPL_CODE );
      String lEngineInvApplCode = APPL_CODE_6;
      InventoryKey lMainInv = createEngineAttachedToAircraft( lEngineInvApplCode, lAircraftKey );
      PartGroupKey lPartGroup = createPartGroup( APPL_RANGE_CONTAINING_CODE_1 );
      PartNoKey lPartNo = createTRKPart( lPartGroup, APPL_RANGE_CONTAINING_CODE_6 );
      createLooseTrkInventoryWithPartNoAndPartGroup( lPartNo, lPartGroup );

      // When
      execute( lMainInv, lPartNo, lPartGroup );

      // Then
      assertEquals( 1, iResultDs.getRowCount() );
      iResultDs.next();

      // Verify part group inapplicability between the engine code and the loose TRK part group
      // applicability range
      assertEquals( lMainInv.toString(), iResultDs.getString( "ass_inv_key" ) );
      assertEquals( APPL_CODE_6, iResultDs.getString( "appl_eff_cd" ) );
      assertEquals( APPL_RANGE_CONTAINING_CODE_1,
            iResultDs.getString( "part_group_appl_eff_ldesc" ) );
      assertEquals( PART_GROUP_NOT_APPLICABLE,
            iResultDs.getBoolean( "part_group_applicable_bool" ) );
      assertEquals( lPartGroup,
            iResultDs.getKey( PartGroupKey.class, "bom_part_db_id", "bom_part_id" ) );
      assertEquals( NO_APPL_CODE, iResultDs.getString( "part_appl_eff_ldesc" ) );
      assertEquals( PART_APPLICABLE, iResultDs.getBoolean( "part_applicable_bool" ) );
      assertEquals( null, iResultDs.getKey( PartNoKey.class, "part_no_db_id", "part_no_id" ) );

   }


   /**
    * Test Scenario: With an engine attached to an aircraft, attempt to attach a TRK engine
    * component to engine
    *
    * Given a loose TRK engine component with a part applicability range and part group
    * applicability range
    *
    * AND an aircraft that has no applicability code (it means the code is null)
    *
    * AND an engine connected to the aircraft also has no applicability code
    *
    * When attempting to to reserve,issue,attach component to the engine (execute query)
    *
    * Then the query returns 0 rows
    */
   @Test
   public void testWhenAircraftAndEngineHaveNoApplicabilityCodeAndEngineTRKGettingInstalled() {

      // Given
      InventoryKey lAircraftKey = createAircraftInventory( NO_APPL_CODE );
      String lEngineInvApplCode = NO_APPL_CODE;
      InventoryKey lMainInv = createEngineAttachedToAircraft( lEngineInvApplCode, lAircraftKey );
      PartGroupKey lPartGroup = createPartGroup( APPL_RANGE_CONTAINING_CODE_1 );
      PartNoKey lPartNo = createTRKPart( lPartGroup, APPL_RANGE_NOT_CONTAINING_CODE_1 );
      createLooseTrkInventoryWithPartNoAndPartGroup( lPartNo, lPartGroup );

      // When
      execute( lMainInv, lPartNo, lPartGroup );

      // Then
      assertEquals( 0, iResultDs.getRowCount() );
   }


   /**
    * Test Scenario: Attempt to attach a TRK aircraft component to an aircraft
    *
    * Given a loose TRK aircraft component with a part applicability range and a part group range
    *
    * AND an aircraft that has an applicability code that is not included in the part and part group
    * applicability range of loose TRK aircraft component
    *
    * When attempting to to reserve,issue,attach component to the aircraft (execute query)
    *
    * Then the query returns 2 rows with inapplicability between the aircraft code and the loose TRK
    * part range & inapplicability between the aircraft code and the loose TRK part group
    * applicability range
    */
   @Test
   public void
         testWhenAircraftCodeNotInPartAndPartGroupApplicabilityRangeOfAircraftTRKGettingInstalled() {

      // Given
      InventoryKey lMainInv = createAircraftInventory( APPL_CODE_1 );
      PartGroupKey lPartGroup = createPartGroup( APPL_RANGE_NOT_CONTAINING_CODE_1 );
      PartNoKey lPartNo = createTRKPart( lPartGroup, APPL_RANGE_NOT_CONTAINING_CODE_1 );
      createLooseTrkInventoryWithPartNoAndPartGroup( lPartNo, lPartGroup );

      // When
      execute( lMainInv, lPartNo, lPartGroup );

      // Then
      assertEquals( 2, iResultDs.getRowCount() );
      iResultDs.next();

      // Verify part group inapplicability between the aircraft code and the loose TRK part group
      // applicability range
      assertEquals( lMainInv.toString(), iResultDs.getString( "ass_inv_key" ) );
      assertEquals( APPL_CODE_1, iResultDs.getString( "appl_eff_cd" ) );
      assertEquals( APPL_RANGE_NOT_CONTAINING_CODE_1,
            iResultDs.getString( "part_group_appl_eff_ldesc" ) );
      assertEquals( PART_GROUP_NOT_APPLICABLE,
            iResultDs.getBoolean( "part_group_applicable_bool" ) );
      assertEquals( lPartGroup,
            iResultDs.getKey( PartGroupKey.class, "bom_part_db_id", "bom_part_id" ) );
      assertEquals( NO_APPL_CODE, iResultDs.getString( "part_appl_eff_ldesc" ) );
      assertEquals( PART_APPLICABLE, iResultDs.getBoolean( "part_applicable_bool" ) );
      assertEquals( null, iResultDs.getKey( PartNoKey.class, "part_no_db_id", "part_no_id" ) );

      iResultDs.next();

      // Verify part inapplicability between the aircraft code and the loose TRK part
      // applicability range
      assertEquals( lMainInv.toString(), iResultDs.getString( "ass_inv_key" ) );
      assertEquals( APPL_CODE_1, iResultDs.getString( "appl_eff_cd" ) );
      assertEquals( NO_APPL_CODE, iResultDs.getString( "part_group_appl_eff_ldesc" ) );
      assertEquals( PART_GROUP_APPLICABLE, iResultDs.getBoolean( "part_group_applicable_bool" ) );
      assertEquals( lPartGroup,
            iResultDs.getKey( PartGroupKey.class, "bom_part_db_id", "bom_part_id" ) );
      assertEquals( APPL_RANGE_NOT_CONTAINING_CODE_1,
            iResultDs.getString( "part_appl_eff_ldesc" ) );
      assertEquals( PART_NOT_APPLICABLE, iResultDs.getBoolean( "part_applicable_bool" ) );
      assertEquals( lPartNo, iResultDs.getKey( PartNoKey.class, "part_no_db_id", "part_no_id" ) );

   }


   /**
    * Test Scenario: Attempt to attach a TRK aircraft component to an aircraft
    *
    * Given a loose TRK aircraft component with a part applicability range and a part group range
    *
    * AND an aircraft that has an applicability code that is included in the part and part group
    * applicability range of loose TRK aircraft component
    *
    * When attempting to to reserve,issue,attach component to the aircraft (execute query)
    *
    * Then the query returns 0 rows
    */
   @Test
   public void
         testWhenAircraftCodeInPartAndPartGroupApplicabilityRangeOfAircraftTRKGettingInstalled() {

      // Given
      InventoryKey lMainInv = createAircraftInventory( APPL_CODE_1 );
      PartGroupKey lPartGroup = createPartGroup( APPL_RANGE_CONTAINING_CODE_1 );
      PartNoKey lPartNo = createTRKPart( lPartGroup, APPL_RANGE_CONTAINING_CODE_1 );
      createLooseTrkInventoryWithPartNoAndPartGroup( lPartNo, lPartGroup );

      // When
      execute( lMainInv, lPartNo, lPartGroup );

      // Then
      assertEquals( 0, iResultDs.getRowCount() );
   }


   /**
    * Test Scenario: Attempt to attach a TRK engine component to a loose engine
    *
    * Given a loose TRK engine component with a part applicability range and a part group range
    *
    * AND an engine that has an applicability code that is not included in the part and part group
    * applicability range of loose TRK engine component
    *
    * When attempting to to reserve,issue,attach component to the engine (execute query)
    *
    * Then the query returns 2 rows with inapplicability between the engine code and the loose TRK
    * part group and part applicability ranges
    */
   @Test
   public void
         testWhenEngineCodeNotInPartAndPartGroupApplicabilityRangeOfEngineTRKGettingInstalled() {

      InventoryKey lMainInv = createLooseEngineInventory( APPL_CODE_1 );
      PartGroupKey lPartGroup = createPartGroup( APPL_RANGE_NOT_CONTAINING_CODE_1 );
      PartNoKey lPartNo = createTRKPart( lPartGroup, APPL_RANGE_NOT_CONTAINING_CODE_1 );
      createLooseTrkInventoryWithPartNoAndPartGroup( lPartNo, lPartGroup );

      // When
      execute( lMainInv, lPartNo, lPartGroup );

      // Then
      assertEquals( 2, iResultDs.getRowCount() );
      iResultDs.next();

      // Verify part group inapplicability between the engine code and the loose TRK part group
      // applicability range
      assertEquals( lMainInv.toString(), iResultDs.getString( "ass_inv_key" ) );
      assertEquals( APPL_CODE_1, iResultDs.getString( "appl_eff_cd" ) );
      assertEquals( APPL_RANGE_NOT_CONTAINING_CODE_1,
            iResultDs.getString( "part_group_appl_eff_ldesc" ) );
      assertEquals( PART_GROUP_NOT_APPLICABLE,
            iResultDs.getBoolean( "part_group_applicable_bool" ) );
      assertEquals( lPartGroup,
            iResultDs.getKey( PartGroupKey.class, "bom_part_db_id", "bom_part_id" ) );
      assertEquals( NO_APPL_CODE, iResultDs.getString( "part_appl_eff_ldesc" ) );
      assertEquals( PART_APPLICABLE, iResultDs.getBoolean( "part_applicable_bool" ) );
      assertEquals( null, iResultDs.getKey( PartNoKey.class, "part_no_db_id", "part_no_id" ) );

      iResultDs.next();

      // Verify part inapplicability between the engine code and the loose TRK part
      // applicability range
      assertEquals( lMainInv.toString(), iResultDs.getString( "ass_inv_key" ) );
      assertEquals( APPL_CODE_1, iResultDs.getString( "appl_eff_cd" ) );
      assertEquals( NO_APPL_CODE, iResultDs.getString( "part_group_appl_eff_ldesc" ) );
      assertEquals( PART_GROUP_APPLICABLE, iResultDs.getBoolean( "part_group_applicable_bool" ) );
      assertEquals( lPartGroup,
            iResultDs.getKey( PartGroupKey.class, "bom_part_db_id", "bom_part_id" ) );
      assertEquals( APPL_RANGE_NOT_CONTAINING_CODE_1,
            iResultDs.getString( "part_appl_eff_ldesc" ) );
      assertEquals( PART_NOT_APPLICABLE, iResultDs.getBoolean( "part_applicable_bool" ) );
      assertEquals( lPartNo, iResultDs.getKey( PartNoKey.class, "part_no_db_id", "part_no_id" ) );

   }


   /**
    * Test Scenario: Attempt to attach a TRK engine component to a loose engine
    *
    * Given a loose TRK aircraft component with a part applicability range and a part group range
    *
    * AND an engine that has an applicability code that is not included in the part applicability
    * range but included in the part group applicability range of loose TRK engine component
    *
    * When attempting to to reserve,issue,attach component to the aircraft (execute query)
    *
    * Then the query returns 1 rows with inapplicability between the engine code and the part range
    */
   @Test
   public void
         testWhenEngineCodeNotInPartApplicabilityRangeButInPartGroupApplicabilityRangeOfEngineTRKGettingInstalled() {

      InventoryKey lMainInv = createLooseEngineInventory( APPL_CODE_1 );
      PartGroupKey lPartGroup = createPartGroup( APPL_RANGE_CONTAINING_CODE_1 );
      PartNoKey lPartNo = createTRKPart( lPartGroup, APPL_RANGE_NOT_CONTAINING_CODE_1 );
      createLooseTrkInventoryWithPartNoAndPartGroup( lPartNo, lPartGroup );

      // When
      execute( lMainInv, lPartNo, lPartGroup );

      // Then
      assertEquals( 1, iResultDs.getRowCount() );
      iResultDs.next();

      // Verify part inapplicability between the engine code and the loose TRK part
      // applicability range
      assertEquals( lMainInv.toString(), iResultDs.getString( "ass_inv_key" ) );
      assertEquals( APPL_CODE_1, iResultDs.getString( "appl_eff_cd" ) );
      assertEquals( NO_APPL_CODE, iResultDs.getString( "part_group_appl_eff_ldesc" ) );
      assertEquals( PART_GROUP_APPLICABLE, iResultDs.getBoolean( "part_group_applicable_bool" ) );
      assertEquals( lPartGroup,
            iResultDs.getKey( PartGroupKey.class, "bom_part_db_id", "bom_part_id" ) );
      assertEquals( APPL_RANGE_NOT_CONTAINING_CODE_1,
            iResultDs.getString( "part_appl_eff_ldesc" ) );
      assertEquals( PART_NOT_APPLICABLE, iResultDs.getBoolean( "part_applicable_bool" ) );
      assertEquals( lPartNo, iResultDs.getKey( PartNoKey.class, "part_no_db_id", "part_no_id" ) );

   }


   /**
    * Test Scenario: Attempt to attach a TRK engine component to a loose engine
    *
    * Given a loose TRK aircraft component with a part applicability range and a part group range
    *
    * AND an engine that has no applicability code
    *
    * When attempting to to reserve,issue,attach component to the aircraft (execute query)
    *
    * Then the query returns 0 rows
    */
   @Test
   public void
         testWhenEngineHasNoApplicabiltyCodeAndEngineTRKWithPartAndPartGroupApplicabilityRanges() {

      InventoryKey lMainInv = createLooseEngineInventory( NO_APPL_CODE );
      PartGroupKey lPartGroup = createPartGroup( APPL_RANGE_CONTAINING_CODE_1 );
      PartNoKey lPartNo = createTRKPart( lPartGroup, APPL_RANGE_NOT_CONTAINING_CODE_1 );
      createLooseTrkInventoryWithPartNoAndPartGroup( lPartNo, lPartGroup );

      // When
      execute( lMainInv, lPartNo, lPartGroup );

      // Then
      assertEquals( 0, iResultDs.getRowCount() );

   }


   /**
    * Create a loose TRK inventory with the provided part group and part number
    *
    * @param aPartNo
    * @param aPartGroup
    *
    * @return InventoryKey
    */
   private InventoryKey createLooseTrkInventoryWithPartNoAndPartGroup( final PartNoKey aPartNo,
         final PartGroupKey aPartGroup ) {

      return Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

         @Override
         public void configure( TrackedInventory aTrk ) {
            aTrk.setDescription( TRK_INV_DESC );
            aTrk.setPartNumber( aPartNo );
            aTrk.setPartGroup( aPartGroup );
         }
      } );
   }


   /**
    * Create a part group with the provided applicability range
    *
    * @param aPartGroupRange
    * @return PartGroupKey Part Group with the provided applicability range
    */
   private PartGroupKey createPartGroup( final String aPartGroupRange ) {
      return Domain.createPartGroup( new DomainConfiguration<PartGroup>() {

         @Override
         public void configure( PartGroup aPartGroup ) {
            aPartGroup.setApplicabilityRange( aPartGroupRange );
            aPartGroup.setCode( PART_GROUP_CD );
         }

      } );
   }


   /**
    * Create a TRK part with the provided part group and applicability range
    *
    * @param aPartGroup
    * @param aPartRange
    * @return PartNoKey PartNo key with the provided applicability range
    */
   private PartNoKey createTRKPart( final PartGroupKey aPartGroup, final String aPartRange ) {
      return Domain.createPart( new DomainConfiguration<Part>() {

         @Override
         public void configure( Part aPart ) {
            aPart.setInventoryClass( RefInvClassKey.TRK );
            aPart.setPartGroup( aPartGroup, Boolean.TRUE );
            aPart.setApplicabilityRange( aPartRange );
         }

      } );
   }


   /**
    * Execute the query with the provided keys.
    *
    * @param aMainInv
    * @param aPartNo
    * @param aPartGroup
    */
   private void execute( InventoryKey aMainInv, PartNoKey aPartNo, PartGroupKey aPartGroup ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aMainInv, "aMainInvDbId", "aMainInvId" );
      lArgs.add( aPartNo, "aPartDbId", "aPartId" );
      lArgs.add( aPartGroup, "aBomDbId", "aBomId" );
      iResultDs = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
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
            aEngine.setDescription( ENGINE_INV_DESC );
            aEngine.setApplicabilityCode( aApplicabilityCode );
         }
      } );
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
            aEngine.setDescription( ENGINE_INV_DESC );
            aEngine.setApplicabilityCode( aEngineInvApplCode );
            aEngine.setParent( aAircraftInvKey );
         }
      } );

   }

}
