package com.mxi.mx.core.services.inventory;

import static com.mxi.mx.core.key.RefInvCondKey.ARCHIVE;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.services.inventory.inventoryassociation.InventoryAssociationService;
import com.mxi.mx.core.services.inventory.inventoryassociation.InventoryAssociationValidatorTest;
import com.mxi.mx.core.table.inv.InvAssociationDao;
import com.mxi.mx.core.table.inv.InvAssociationKey;
import com.mxi.mx.core.table.inv.InvAssociationTableRow;


/**
 * Integration unit test for {@link InventoryAssociationService}
 *
 * Note: as part of the inventory association logic there are various validations performed. Those
 * validations are tested by {@linkplain InventoryAssociationValidatorTest} and for that reason
 * those tests are not repeated here.
 *
 */
public class InventoryAssociationServiceTest {

   private static final String LINKED_TO_INVENTORY_BARCODE = "LinkedToInventory";
   private static final Integer ASSOCIATION_ID = 1;
   private static final String USER_NOTE = "This is a User Note.";

   private InvAssociationDao iInvAssociationDao;
   private HumanResourceKey iAuthorizingHr;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @Before
   public void setup() {
      iInvAssociationDao = InjectorContainer.get().getInstance( InvAssociationDao.class );
      iAuthorizingHr = Domain.createHumanResource( aHr -> aHr.setUser( Domain.createUser() ) );
   }


   /**
    * It tests that two inventories are associated together when neither of them are associated with
    * any inventories.
    *
    * <pre>
    *              ___
    *             |   |
    *  A -> B     | A |
    *         =>  | B |
    *             |___|
    *
    * GIVEN two inventory records, linked-from inventory A and linked-to inventory B
    *   AND both do not have associations exist
    *  WHEN linking the linked-to inventory record to the linked-from inventory record
    *  THEN a record is added to the inv_association table for each with the same association_id
    * </pre>
    */
   @Test
   public void itAssociatesTwoInventoriesWhenBothAreNotLinkedWithAnyInventories() throws Exception {
      // Given
      final InventoryKey lInventoryA = Domain.createAircraft();
      final InventoryKey lInventoryB = Domain.createAircraft( aAircraft -> {
         aAircraft.setBarcode( LINKED_TO_INVENTORY_BARCODE );
         aAircraft.setCondition( ARCHIVE );
      } );
      final boolean lSkipAssociationNoteValidationFlag = true;

      // When
      new InventoryAssociationService().associateInventory( lInventoryA,
            LINKED_TO_INVENTORY_BARCODE, iAuthorizingHr, USER_NOTE,
            lSkipAssociationNoteValidationFlag );

      // Then
      InvAssociationTableRow lInvAssociationTableRowA = getInvAssociationRecordRow( lInventoryA );
      assertThat( "Failed to create association for inventory A.",
            lInvAssociationTableRowA.exists(), is( true ) );

      InvAssociationTableRow lInvAssociationTableRowB = getInvAssociationRecordRow( lInventoryB );
      assertThat( "Failed to create association for inventory B.",
            lInvAssociationTableRowB.exists(), is( true ) );

      Integer lInvAssociationIdA = lInvAssociationTableRowA.getAssociationId();
      Integer lInvAssociationIdB = lInvAssociationTableRowB.getAssociationId();
      assertEquals( "The inventory A and the inventory B should have the same association id.",
            lInvAssociationIdA, lInvAssociationIdB );
   }


   /**
    * It tests that while associating inventory A to inventory B, and inventory A is already
    * associated with another inventory C, all these three inventories will be associated together.
    *
    * <pre>
    *  ___          ___
    * |   |        |   |
    * | A |-> B    | A |
    * |   |    =>  | B |
    * | C |        | C |
    * |___|        |___|
    *
    * GIVEN two inventory records, linked-from inventory A and linked-to inventory B
    *   AND the linked-from inventory record has an association with another inventory
    *   AND the linked-to inventory record does not have any association exist
    *  WHEN linking the linked-to inventory record to the linked-from inventory record
    *  THEN a record is added to the inv_association table for the linked-to inventory record
    *   AND linked-to inventory will have the same association_id with linked-from inventory in the association table
    * </pre>
    */
   @Test
   public void itAssociatesTwoInventoriesWhenLinkedFromInventoryIsAlreadyLinkedWithOneInventory()
         throws Exception {
      // Given
      final InventoryKey lInventoryA = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssociation( ASSOCIATION_ID );
      } );

      final InventoryKey lInventoryB = Domain.createAircraft( aAircraft -> {
         aAircraft.setBarcode( LINKED_TO_INVENTORY_BARCODE );
         aAircraft.setCondition( ARCHIVE );
      } );

      final InventoryKey lInventoryC = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssociation( ASSOCIATION_ID );
      } );
      final boolean lSkipAssociationNoteValidationFlag = true;

      // When
      new InventoryAssociationService().associateInventory( lInventoryA,
            LINKED_TO_INVENTORY_BARCODE, iAuthorizingHr, USER_NOTE,
            lSkipAssociationNoteValidationFlag );

      // Then
      InvAssociationTableRow lInvAssociationTableRowB = getInvAssociationRecordRow( lInventoryB );
      assertThat( "Failed to create association for inventory B.",
            lInvAssociationTableRowB.exists(), is( true ) );

      Integer lInvAssociationIdB = lInvAssociationTableRowB.getAssociationId();
      Integer lInvAssociationIdA = getAssociationIdByInventory( lInventoryA );
      Integer lInvAssociationIdC = getAssociationIdByInventory( lInventoryC );
      assertThat( "All three inventories should share the same association id.", lInvAssociationIdB,
            is( allOf( equalTo( lInvAssociationIdA ), equalTo( lInvAssociationIdC ) ) ) );
   }


   /**
    * It tests that while associating inventory A to inventory B, and inventory B is already
    * associated with another inventory C, all these three inventories will be associated together.
    *
    * <pre>
    *      ___        ___
    *     |   |      |   |
    * A ->| B |      | A |
    *     |   |  =>  | B |
    *     | C |      | C |
    *     |___|      |___|
    *
    * GIVEN two inventory records, linked-from inventory A and linked-to inventory B
    *   AND the linked-from inventory record does not have any association exist
    *   AND the linked-to inventory record has an association with another inventory
    *  WHEN linking the linked-to inventory record to the linked-from inventory record
    *  THEN a record is added to the inv_association table for the linked-to inventory record
    *   AND linked-from inventory will have the same association_id with linked-to inventory in the association table
    * </pre>
    */
   @Test
   public void itAssociatesTwoInventoriesWhenLinkedToInventoryIsAlreadyLinkedWithOneInventory()
         throws Exception {
      // Given
      final InventoryKey lInventoryA = Domain.createAircraft();

      final InventoryKey lInventoryB = Domain.createAircraft( aAircraft -> {
         aAircraft.setBarcode( LINKED_TO_INVENTORY_BARCODE );
         aAircraft.setAssociation( ASSOCIATION_ID );
         aAircraft.setCondition( ARCHIVE );
      } );

      final InventoryKey lInventoryC = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssociation( ASSOCIATION_ID );
         aAircraft.setCondition( ARCHIVE );
      } );
      final boolean lSkipAssociationNoteValidationFlag = true;

      // When
      new InventoryAssociationService().associateInventory( lInventoryA,
            LINKED_TO_INVENTORY_BARCODE, iAuthorizingHr, USER_NOTE,
            lSkipAssociationNoteValidationFlag );

      // Then
      InvAssociationTableRow lInvAssociationTableRowA = getInvAssociationRecordRow( lInventoryA );
      assertThat( "Failed to create association for inventory A.",
            lInvAssociationTableRowA.exists(), is( true ) );

      Integer lInvAssociationIdA = lInvAssociationTableRowA.getAssociationId();
      Integer lInvAssociationIdB = getAssociationIdByInventory( lInventoryB );
      Integer lInvAssociationIdC = getAssociationIdByInventory( lInventoryC );
      assertThat( "All three inventories should share the same association id.", lInvAssociationIdA,
            is( allOf( equalTo( lInvAssociationIdB ), equalTo( lInvAssociationIdC ) ) ) );
   }


   /**
    * It tests that while associating inventory A to inventory B, and inventory A is already
    * associated with multiple other inventories, inventory B will be associated to every inventory
    * in the same group.
    *
    * <pre>
    *  ___          ___
    * |   |        |   |
    * | A |-> B    | A |
    * |   |        | B |
    * | C |    =>  | C |
    * | D |        | D |
    * | E |        | E |
    * |___|        |___|
    *
    * GIVEN two inventory records, linked-from inventory A and linked-to inventory B
    *   AND the linked-from inventory record already has associations with multiple inventories
    *   AND the linked-to inventory record does not have any association exist
    *  WHEN linking the linked-to inventory record to the linked-from inventory record
    *  THEN a record is added to the inv_association table for the linked-to inventory
    *   AND linked-to inventory has the same association_id as the linked-from inventory record and all other associated inventory records
    * </pre>
    */
   @Test
   public void
         itAssociatesTwoInventoriesWhenLinkedFromInventoryIsAlreadyLinkedWithMultipleInventories()
               throws Exception {
      // Given
      final InventoryKey lInventoryA = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssociation( ASSOCIATION_ID );
      } );

      final InventoryKey lInventoryB = Domain.createAircraft( aAircraft -> {
         aAircraft.setBarcode( LINKED_TO_INVENTORY_BARCODE );
         aAircraft.setCondition( ARCHIVE );
      } );

      final InventoryKey lInventoryC = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssociation( ASSOCIATION_ID );
      } );

      final InventoryKey lInventoryD = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssociation( ASSOCIATION_ID );
      } );

      final InventoryKey lInventoryE = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssociation( ASSOCIATION_ID );
      } );
      final boolean lSkipAssociationNoteValidationFlag = true;

      // When
      new InventoryAssociationService().associateInventory( lInventoryA,
            LINKED_TO_INVENTORY_BARCODE, iAuthorizingHr, USER_NOTE,
            lSkipAssociationNoteValidationFlag );

      // Then
      InvAssociationTableRow lInvAssociationTableRowB = getInvAssociationRecordRow( lInventoryB );
      assertThat( "Failed to create association for inventory B.",
            lInvAssociationTableRowB.exists(), is( true ) );

      Integer lInvAssociationIdB = lInvAssociationTableRowB.getAssociationId();
      Integer lInvAssociationIdA = getAssociationIdByInventory( lInventoryA );
      Integer lInvAssociationIdC = getAssociationIdByInventory( lInventoryC );
      Integer lInvAssociationIdD = getAssociationIdByInventory( lInventoryD );
      Integer lInvAssociationIdE = getAssociationIdByInventory( lInventoryE );
      assertThat( "All five inventories should share the same association id.", lInvAssociationIdB,
            is( allOf( equalTo( lInvAssociationIdA ), equalTo( lInvAssociationIdC ),
                  equalTo( lInvAssociationIdD ), equalTo( lInvAssociationIdE ) ) ) );
   }


   /**
    * It tests that while associating inventory A to inventory B, and inventory B is already
    * associated with multiple other inventories, inventory A will be associated to every inventory
    * in the same group.
    *
    * <pre>
    *      ___        ___
    *     |   |      |   |
    * A ->| B |      | A |
    *     |   |      | B |
    *     | C |  =>  | C |
    *     | D |      | D |
    *     | E |      | E |
    *     |___|      |___|
    *
    * GIVEN two inventory records, linked-from inventory A and linked-to inventory B
    *   AND the linked-from inventory record does not have any association exist
    *   AND the linked-to inventory record already has associations with multiple inventories
    *  WHEN linking the linked-to inventory record to the linked-from inventory record
    *  THEN a record is added to the inv_association table for the linked-from inventory
    *   AND linked-from inventory has the same association_id as the linked-to inventory record and all other associated inventory records
    * </pre>
    */
   @Test
   public void
         itAssociatesTwoInventoriesWhenLinkedToInventoryIsAlreadyLinkedWithMultipleInventories()
               throws Exception {
      // Given
      final InventoryKey lInventoryA = Domain.createAircraft();

      final InventoryKey lInventoryB = Domain.createAircraft( aAircraft -> {
         aAircraft.setBarcode( LINKED_TO_INVENTORY_BARCODE );
         aAircraft.setAssociation( ASSOCIATION_ID );
         aAircraft.setCondition( ARCHIVE );
      } );

      final InventoryKey lInventoryC = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssociation( ASSOCIATION_ID );
         aAircraft.setCondition( ARCHIVE );
      } );

      final InventoryKey lInventoryD = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssociation( ASSOCIATION_ID );
         aAircraft.setCondition( ARCHIVE );
      } );

      final InventoryKey lInventoryE = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssociation( ASSOCIATION_ID );
         aAircraft.setCondition( ARCHIVE );
      } );
      final boolean lSkipAssociationNoteValidationFlag = true;

      // When
      new InventoryAssociationService().associateInventory( lInventoryA,
            LINKED_TO_INVENTORY_BARCODE, iAuthorizingHr, USER_NOTE,
            lSkipAssociationNoteValidationFlag );

      // Then
      InvAssociationTableRow lInvAssociationTableRowA = getInvAssociationRecordRow( lInventoryA );
      assertThat( "Failed to create association for inventory A.",
            lInvAssociationTableRowA.exists(), is( true ) );

      Integer lInvAssociationIdA = lInvAssociationTableRowA.getAssociationId();
      Integer lInvAssociationIdB = getAssociationIdByInventory( lInventoryB );
      Integer lInvAssociationIdC = getAssociationIdByInventory( lInventoryC );
      Integer lInvAssociationIdD = getAssociationIdByInventory( lInventoryD );
      Integer lInvAssociationIdE = getAssociationIdByInventory( lInventoryE );
      assertThat( "All five inventories should share the same association id.", lInvAssociationIdA,
            is( allOf( equalTo( lInvAssociationIdB ), equalTo( lInvAssociationIdC ),
                  equalTo( lInvAssociationIdD ), equalTo( lInvAssociationIdE ) ) ) );
   }


   /**
    * It tests that an inventory is removed from the association which contains only two
    * inventories.
    *
    * <pre>
    * Given two inventories that are associated
    *  When remove an inventory from the association
    *  Then the association records for both inventories are deleted from inv_association table
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void itRemovesAnInventoryFromTheAssociationWhichContainsOnlyTwoInventories()
         throws Exception {
      // Given
      final InventoryKey lInventoryA = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssociation( ASSOCIATION_ID );
      } );

      final InventoryKey lInventoryB = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssociation( ASSOCIATION_ID );
      } );
      final boolean lSkipAssociationNoteValidationFlag = true;

      // When
      new InventoryAssociationService().removeInventoryFromAssociation( lInventoryA, lInventoryB,
            iAuthorizingHr, USER_NOTE, lSkipAssociationNoteValidationFlag );

      // Then
      InvAssociationTableRow lInvAssociationRecordA = getInvAssociationRecordRow( lInventoryA );
      assertThat( "Association record for inventory A should be removed.",
            lInvAssociationRecordA.exists(), is( false ) );

      InvAssociationTableRow lInvAssociationRecordB = getInvAssociationRecordRow( lInventoryB );
      assertThat( "Association record for inventory B should be removed.",
            lInvAssociationRecordB.exists(), is( false ) );
   }


   /**
    * It tests that when three or more inventory records are associated, and the user removes one of
    * the inventories from the association, the inventory record being un-associated will be removed
    * from inv_association table and the rest inventories will still associated.
    *
    * <pre>
    * Given three inventories A, B, C that are associated together
    *  When remove inventory A from the association
    *  Then inventory A is not associated with B and C
    *   And inventory B and C are still associated together
    * </pre>
    *
    * @throws Exception
    *
    */
   @Test
   public void itRemovesOneInventoryFromTheAssociationWhichContainsMultipleInventories()
         throws Exception {
      // Given
      final InventoryKey lInventoryA = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssociation( ASSOCIATION_ID );
      } );

      final InventoryKey lInventoryB = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssociation( ASSOCIATION_ID );
      } );

      final InventoryKey lInventoryC = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssociation( ASSOCIATION_ID );
      } );
      final boolean lSkipAssociationNoteValidationFlag = true;

      // When
      new InventoryAssociationService().removeInventoryFromAssociation( lInventoryB, lInventoryA,
            iAuthorizingHr, USER_NOTE, lSkipAssociationNoteValidationFlag );

      // Then
      InvAssociationTableRow lInvAssociationRecordA = getInvAssociationRecordRow( lInventoryA );
      assertThat( "Association record for inventory A should be removed.",
            lInvAssociationRecordA.exists(), is( false ) );

      InvAssociationTableRow lInvAssociationRecordB = getInvAssociationRecordRow( lInventoryB );
      assertThat( "Association record for inventory B should not be removed.",
            lInvAssociationRecordB.exists(), is( true ) );

      InvAssociationTableRow lInvAssociationRecordC = getInvAssociationRecordRow( lInventoryC );
      assertThat( "Association record for inventory B should not be removed.",
            lInvAssociationRecordC.exists(), is( true ) );

      Integer lInvAssociationIdB = lInvAssociationRecordB.getAssociationId();
      Integer lInvAssociationIdC = lInvAssociationRecordC.getAssociationId();
      assertEquals( "The inventory B and the inventory C should still be associated.",
            lInvAssociationIdB, lInvAssociationIdC );
   }


   /**
    * Get the association record of an inventory from inv_association table
    *
    */
   private InvAssociationTableRow getInvAssociationRecordRow( InventoryKey aInventory ) {
      InvAssociationKey lLinkedFromInvAssociationKey =
            new InvAssociationKey( aInventory.getDbId(), aInventory.getId() );
      return iInvAssociationDao.findByPrimaryKey( lLinkedFromInvAssociationKey );
   }


   /**
    * Get the association id of an inventory from inv_association table
    *
    */
   private Integer getAssociationIdByInventory( InventoryKey aInventory ) {
      InvAssociationTableRow lRow = getInvAssociationRecordRow( aInventory );

      if ( lRow != null ) {
         return lRow.getAssociationId();
      }

      return null;
   }
}
