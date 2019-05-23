package com.mxi.mx.core.ejb.inventory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.i18n.i18n;
import com.mxi.mx.common.servlet.SessionContextFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.core.configuration.axon.RecordingEventBus;
import com.mxi.mx.core.key.AircraftKey;
import com.mxi.mx.core.key.AuthorityKey;
import com.mxi.mx.core.key.CarrierKey;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.IetmDefinitionKey;
import com.mxi.mx.core.key.IetmTopicKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.production.aircraft.domain.AircraftAuthorityChangedEvent;
import com.mxi.mx.core.production.aircraft.domain.AircraftLockedEvent;
import com.mxi.mx.core.production.aircraft.domain.AircraftUnlockedEvent;
import com.mxi.mx.core.table.evt.EvtIetm;
import com.mxi.mx.core.table.inv.InvInvTable;


/**
 * This class is the test for #InventoryBean
 *
 */
public class InventoryBeanTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   private SessionContextFake iSessionContextFake = new SessionContextFake();

   private HumanResourceKey iAuthorizingHr;

   private RecordingEventBus iEventBus;


   /**
    * This test is testing unarchiving an inventory with the same part number.
    *
    * <pre>
    * Given an archived inventory.
    * When unarchive it.
    * Then verify the condition and location is updated.
    * </pre>
    *
    */
   @Test
   public void unarchiveInventoryWithoutPartNumberChanging() throws Exception {

      OwnerKey lOwner = Domain.createOwner();
      LocationKey lOldLocation = Domain.createLocation();
      LocationKey lNewLocation = Domain.createLocation();
      PartNoKey lPartNo = Domain.createPart( aPartNo -> {
         aPartNo.setQtyUnitKey( RefQtyUnitKey.EA );
         aPartNo.setPartStatus( RefPartStatusKey.ACTV );
      } );

      InventoryKey lInventory = Domain.createTrackedInventory( aInventory -> {
         aInventory.setLocation( lOldLocation );
         aInventory.setPartNumber( lPartNo );
         aInventory.setCondition( RefInvCondKey.ARCHIVE );
         aInventory.setOwner( lOwner );
         aInventory.setComplete( true );
      } );

      InventoryBean lInventoryBean = new InventoryBean();
      lInventoryBean.setSessionContext( iSessionContextFake );

      lInventoryBean.reinductInventory( lNewLocation, RefInvCondKey.RFI, null, iAuthorizingHr,
            FncAccountKey.AP, null, lInventory );

      InvInvTable lUpdatedInventory = InvInvTable.findByPrimaryKey( lInventory );

      assertEquals( "Inventory location is not updated.", lNewLocation,
            lUpdatedInventory.getLocation() );
      assertEquals( "Inventory condition is not updated.", RefInvCondKey.RFI,
            lUpdatedInventory.getInvCond() );
   }


   /**
    * This test is testing unarchiving an inventory with the different part number.
    *
    * <pre>
    * Given an archived inventory.
    * When unarchive it with a different part number.
    * Then verify the condition and location is updated.
    * And the part number is updated.
    * </pre>
    *
    */
   @Test
   public void unarchiveInventoryWithPartNumberChanging() throws Exception {

      OwnerKey lOwner = Domain.createOwner();
      LocationKey lOldLocation = Domain.createLocation();
      LocationKey lNewLocation = Domain.createLocation();
      PartNoKey lOriginalPartNo = Domain.createPart();
      PartNoKey lModifiedPartNo = Domain.createPart( aPartNo -> {
         aPartNo.setQtyUnitKey( RefQtyUnitKey.EA );
         aPartNo.setPartStatus( RefPartStatusKey.ACTV );
         aPartNo.setInventoryClass( RefInvClassKey.SER );
      } );

      InventoryKey lInventory = Domain.createTrackedInventory( aInventory -> {
         aInventory.setLocation( lOldLocation );
         aInventory.setPartNumber( lOriginalPartNo );
         aInventory.setCondition( RefInvCondKey.ARCHIVE );
         aInventory.setOwner( lOwner );
         aInventory.setComplete( true );
      } );

      InventoryBean lInventoryBean = new InventoryBean();
      lInventoryBean.setSessionContext( iSessionContextFake );

      lInventoryBean.reinductInventory( lNewLocation, RefInvCondKey.RFI, null, iAuthorizingHr,
            FncAccountKey.AP, lModifiedPartNo, lInventory );

      InvInvTable lUpdatedInventory = InvInvTable.findByPrimaryKey( lInventory );

      assertEquals( "Inventory location is not updated properly.", lNewLocation,
            lUpdatedInventory.getLocation() );
      assertEquals( "Inventory condition is not updated properly.", RefInvCondKey.RFI,
            lUpdatedInventory.getInvCond() );
      assertEquals( "Part number is not updated properly.", lModifiedPartNo,
            lUpdatedInventory.getPartNo() );
   }


   /**
    * This test is testing if a note is added when inventories are associated.
    *
    * <pre>
    * Given two inventories. Skip Association note validation flag is set to false.
    * When one inventory is associated with another inventory. And,both the inventory records have sufficient space available for notes.
    * Then A note consisting of default system note and a user note is added to the notes column of the inv_inv table for both the inventory records.
    * </pre>
    *
    *
    */
   @Test
   public void itAppendsANoteWhenTwoInventoriesAreAssociated() throws Exception {

      final String lLinkedToInventoryBarcode = "LinkedToInventory";
      final String lUserNote = "This is User Note";
      boolean lSkipAssociationNoteValidationFlag = false;

      // Given
      final InventoryKey lInventoryA = Domain.createAircraft( aAircraft -> {
         aAircraft.setBarcode( lLinkedToInventoryBarcode + 'A' );
      } );

      final InventoryKey lInventoryB = Domain.createAircraft( aAircraft -> {
         aAircraft.setBarcode( lLinkedToInventoryBarcode + 'B' );
         aAircraft.setCondition( RefInvCondKey.ARCHIVE );
      } );

      // When
      InventoryBean lInventoryBean = new InventoryBean();
      lInventoryBean.setSessionContext( iSessionContextFake );
      lInventoryBean.associateInventory( lInventoryA, getBarcodeFromInventoryKey( lInventoryB ),
            iAuthorizingHr, lUserNote, lSkipAssociationNoteValidationFlag );

      // Then
      InvInvTable lInventoryTableA = InvInvTable.findByPrimaryKey( lInventoryA );
      InvInvTable lInventoryTableB = InvInvTable.findByPrimaryKey( lInventoryB );
      assertNotNull( "Failed to add the Note to the Inventory record A",
            lInventoryTableA.getNote() );
      assertNotNull( "Failed to add the Note to the Inventory record B",
            lInventoryTableB.getNote() );

      String lSystemNote = i18n.get( "core.msg.CREATE_ASSOCIATION_WITH",
            getBarcodeFromInventoryKey( lInventoryA ), getBarcodeFromInventoryKey( lInventoryB ) );
      assertTrue( lInventoryTableA.getNote().contains( lSystemNote + "<br>" + lUserNote ) );
      assertTrue( lInventoryTableB.getNote().contains( lSystemNote + "<br>" + lUserNote ) );
   }


   /**
    * This test is testing if a note is added when a inventory is removed from the association.
    *
    * <pre>
    * Given two inventories. Skip Association note validation flag is set to false.
    * When one inventory is removed from the association. And,both the inventory records have sufficient space available for notes.
    * Then A note consisting of default system note and a user note is added to the notes column of the inv_inv table for both the inventory records.
    * </pre>
    *
    *
    */
   @Test
   public void itAppendsANoteWhenAnInventroyIsRemovedFromTheAssociation() throws Exception {

      final String lUserNote = "This is User Note";
      boolean lSkipAssociationNoteValidationFlag = false;
      final Integer lAssociation_ID = 1;
      // Given
      final InventoryKey lCurrentinventory = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssociation( lAssociation_ID );
      } );

      final InventoryKey lAssociationRemovedInventory = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssociation( lAssociation_ID );
      } );

      // When
      // iEntityContextFake.setPrimaryKey( lCurrentinventory );
      InventoryBean lInventoryBean = new InventoryBean();
      lInventoryBean.setSessionContext( iSessionContextFake );
      lInventoryBean.removeInventoryFromAssociation( lCurrentinventory,
            lAssociationRemovedInventory, iAuthorizingHr, lUserNote,
            lSkipAssociationNoteValidationFlag );

      // Then
      InvInvTable lInventoryTableA = InvInvTable.findByPrimaryKey( lCurrentinventory );
      InvInvTable lInventoryTableB = InvInvTable.findByPrimaryKey( lAssociationRemovedInventory );
      assertNotNull( "Failed to add the Note to the Inventory record A",
            lInventoryTableA.getNote() );
      assertNotNull( "Failed to add the Note to the Inventory record B",
            lInventoryTableB.getNote() );

      String lSystemNote = i18n.get( "core.msg.DELETE_ASSOCIATION_WITH",
            getBarcodeFromInventoryKey( lCurrentinventory ),
            getBarcodeFromInventoryKey( lAssociationRemovedInventory ) );
      assertTrue( "Unfortunately, Incorrect note is added",
            lInventoryTableA.getNote().contains( lSystemNote + "<br>" + lUserNote ) );
      assertTrue( "Unfortunately, Incorrect note is added",
            lInventoryTableB.getNote().contains( lSystemNote + "<br>" + lUserNote ) );

   }


   /**
    * This test case is testing if operator of an aircraft is changed, the technical reference of
    * requirement on the aircraft will be removed if the operator is not matched.
    *
    * <pre>
    *    Given an aircraft.
    *    And a requirement with technical reference on the aircraft.
    *    When the aircraft operator is changed.
    *    The technical reference will be removed.
    * </pre>
    */
   @Test
   public void itRemovesTechnicalReferenceWhenCarrierChanges() throws Exception {
      String lTechinicalReferenceName = "TechRef";

      final CarrierKey lOriginalCarrierKey = Domain.createOperator();
      final CarrierKey lNewCarrierKey = Domain.createOperator();
      final IetmDefinitionKey lIetm = Domain.createIetm( alIetm -> {
         alIetm.addTechnicalReference( aTechRef -> {

            aTechRef.setName( lTechinicalReferenceName );
            aTechRef.addOperator( lOriginalCarrierKey );

         } );

      } );

      final IetmTopicKey lTechnicalReferenceKey =
            Domain.readTechnicalReference( lIetm, lTechinicalReferenceName );

      final InventoryKey lAircraft = Domain.createAircraft( aAircraft -> {
         aAircraft.setOperator( lOriginalCarrierKey );
      } );

      final TaskKey lRequirement = Domain.createRequirement( aRequirement -> {
         aRequirement.addTechnicalReference( lTechnicalReferenceKey );
         aRequirement.setInventory( lAircraft );
      } );

      InventoryBean lInventoryBean = new InventoryBean();
      lInventoryBean.setSessionContext( iSessionContextFake );

      lInventoryBean.setCarrier( lNewCarrierKey, lAircraft );

      assertFalse( "Event ietm is not deleted",
            EvtIetm.isEventIetmExists( lRequirement.getEventKey(), lTechnicalReferenceKey ) );

   }


   @Test
   public void unlockLockedAircraftMustEmitAircraftUnlockedEvent()
         throws MxException, TriggerException {

      // ARRANGE
      final HumanResourceKey lAuthorizingHumanResourceKey = HumanResourceKey.ADMIN;
      final String lNotes = "Test unlock aircraft note";
      final CarrierKey lOriginalCarrierKey = Domain.createOperator();
      final InventoryKey lInventoryKey = Domain.createAircraft( aAircraft -> {
         aAircraft.setOperator( lOriginalCarrierKey );
         aAircraft.setLocked( true );
         aAircraft.setAssembly( Domain.createAircraftAssembly() );
      } );

      InventoryBean lInventoryBean = new InventoryBean();
      lInventoryBean.setSessionContext( iSessionContextFake );

      // ACT
      lInventoryBean.setLock( false, null, new Date(), new Date(), lAuthorizingHumanResourceKey,
            lNotes, lInventoryKey );

      // ASSERT
      assertEquals(
            new AircraftUnlockedEvent( new AircraftKey( lInventoryKey.toString() ), null, lNotes,
                  lAuthorizingHumanResourceKey ),
            iEventBus.getEventMessages().get( 0 ).getPayload() );
   }


   @Test
   public void unlockUnlockeddAircraftMustNotEmitAircraftUnlockedEvent()
         throws MxException, TriggerException {

      // ARRANGE
      final HumanResourceKey lAuthorizingHumanResourceKey = HumanResourceKey.ADMIN;
      final String lNotes = "Test unlock aircraft note";
      final CarrierKey lOriginalCarrierKey = Domain.createOperator();
      final InventoryKey lAircraftKey = Domain.createAircraft( aAircraft -> {
         aAircraft.setOperator( lOriginalCarrierKey );
         aAircraft.setLocked( false );
      } );

      InventoryBean lInventoryBean = new InventoryBean();
      lInventoryBean.setSessionContext( iSessionContextFake );

      // ACT
      lInventoryBean.setLock( false, null, new Date(), new Date(), lAuthorizingHumanResourceKey,
            lNotes, lAircraftKey );

      // ASSERT
      assertEquals( 0, iEventBus.getEventMessages().size() );
   }


   @Test
   public void unlockInventoryNotOfTypeAircraftMustNotEmitAircraftUnlockedEvent()
         throws MxException, TriggerException {

      // ARRANGE
      final HumanResourceKey lAuthorizingHumanResourceKey = HumanResourceKey.ADMIN;
      final String lNotes = "Test unlock aircraft note";
      final OwnerKey lOwnerKey = Domain.createOwner();
      final LocationKey lLocationKey = Domain.createLocation();
      PartNoKey lPartNoKey = Domain.createPart( aPartNo -> {
         aPartNo.setQtyUnitKey( RefQtyUnitKey.EA );
         aPartNo.setPartStatus( RefPartStatusKey.ACTV );
      } );

      final InventoryKey lInventoryKey = Domain.createTrackedInventory( aInventory -> {
         aInventory.setLocation( lLocationKey );
         aInventory.setPartNumber( lPartNoKey );
         aInventory.setCondition( RefInvCondKey.INSRV );
         aInventory.setOwner( lOwnerKey );
         aInventory.setComplete( true );
         aInventory.setLocked( true );
      } );

      final InventoryBean lInventoryBean = new InventoryBean();
      lInventoryBean.setSessionContext( iSessionContextFake );

      // ACT
      lInventoryBean.setLock( false, null, new Date(), new Date(), lAuthorizingHumanResourceKey,
            lNotes, lInventoryKey );

      // ASSERT
      assertEquals( 0, iEventBus.getEventMessages().size() );
   }


   @Test
   public void lockUnlockedAircraftMustEmitAircraftLockedEvent()
         throws MxException, TriggerException {

      // Create an unlocked aircraft
      final HumanResourceKey lAuthorizingHumanResourceKey = HumanResourceKey.ADMIN;
      final String lNotes = "Test lock aircraft note";
      final CarrierKey lOriginalCarrierKey = Domain.createOperator();
      final InventoryKey lAircraftKey = Domain.createAircraft( aAircraft -> {
         aAircraft.setOperator( lOriginalCarrierKey );
         aAircraft.setLocked( false );

      } );

      InventoryBean lInventoryBean = new InventoryBean();
      lInventoryBean.setSessionContext( iSessionContextFake );

      // Lock the aircraft
      lInventoryBean.setLock( true, null, new Date(), new Date(), lAuthorizingHumanResourceKey,
            lNotes, lAircraftKey );

      // ASSERT
      assertEquals(
            new AircraftLockedEvent( new AircraftKey( lAircraftKey.toString() ), null, lNotes,
                  lAuthorizingHumanResourceKey ),

            iEventBus.getEventMessages().get( 0 ).getPayload() );
   }


   @Test
   public void lockLockedAircraftMustNotEmitAircraftLockedEvent()
         throws MxException, TriggerException {
      // ARRANGE

      final HumanResourceKey lAuthorizingHumanResourceKey = HumanResourceKey.ADMIN;
      final String lNotes = "Test lock aircraft note";
      final CarrierKey lOriginalCarrierKey = Domain.createOperator();
      final InventoryKey lAircraftKey = Domain.createAircraft( aAircraft -> {
         aAircraft.setOperator( lOriginalCarrierKey );
         aAircraft.setLocked( true );

      } );

      InventoryBean lInventoryBean = new InventoryBean();
      lInventoryBean.setSessionContext( iSessionContextFake );

      // ACT

      lInventoryBean.setLock( true, null, new Date(), new Date(), lAuthorizingHumanResourceKey,
            lNotes, lAircraftKey );

      // ASSERT
      assertEquals( 0, iEventBus.getEventMessages().size() );
   }


   public void changeAircraftAuthorityMustEmitAircrafAuthorityChangedEvent()
         throws MxException, TriggerException {

      // ARRANGE
      final HumanResourceKey authorizingHumanResourceKey = HumanResourceKey.ADMIN;
      final CarrierKey originalCarrierKey = Domain.createOperator();
      final Date effectiveDate = new Date();
      final InventoryKey inventoryKey = Domain.createAircraft( aAircraft -> {
         aAircraft.setOperator( originalCarrierKey );
         aAircraft.withAuthority( null );
         aAircraft.setAssembly( Domain.createAircraftAssembly() );
      } );

      final AuthorityKey authorityKey = Domain.createAuthority();

      InventoryBean inventoryBean = new InventoryBean();
      inventoryBean.setSessionContext( iSessionContextFake );

      // ACT
      inventoryBean.setAuthority( authorityKey, effectiveDate, authorizingHumanResourceKey,
            inventoryKey );

      // ASSERT
      assertEquals(
            new AircraftAuthorityChangedEvent( new AircraftKey( inventoryKey ), authorityKey,
                  effectiveDate, authorizingHumanResourceKey ),
            iEventBus.getEventMessages().get( 0 ).getPayload() );

   }


   @Test
   public void lockInventoryNotOfTypeAircraftMustNotEmitAircraftlockedEvent()
         throws MxException, TriggerException {

      // ARRANGE
      final HumanResourceKey lAuthorizingHumanResourceKey = HumanResourceKey.ADMIN;

      final String lNotes = "Test lock aircraft note";

      final OwnerKey lOwnerKey = Domain.createOwner();
      final LocationKey lLocationKey = Domain.createLocation();
      PartNoKey lPartNoKey = Domain.createPart( aPartNo -> {

         aPartNo.setQtyUnitKey( RefQtyUnitKey.EA );
         aPartNo.setPartStatus( RefPartStatusKey.ACTV );
      } );

      final InventoryKey lInventoryKey = Domain.createTrackedInventory( aInventory -> {
         aInventory.setLocation( lLocationKey );
         aInventory.setPartNumber( lPartNoKey );
         aInventory.setCondition( RefInvCondKey.INSRV );
         aInventory.setOwner( lOwnerKey );
         aInventory.setComplete( true );
         aInventory.setLocked( false );
      } );

      final InventoryBean lInventoryBean = new InventoryBean();
      lInventoryBean.setSessionContext( iSessionContextFake );

      // ACT

      lInventoryBean.setLock( true, null, new Date(), new Date(), lAuthorizingHumanResourceKey,
            lNotes, lInventoryKey );

      // ASSERT
      assertEquals( 0, iEventBus.getEventMessages().size() );
   }


   @Test
   public void changeInventoryAuthorityNotOfTypeAircraftMustNotEmitAircrafAuthorityChangedEvent()
         throws MxException, TriggerException {

      // ARRANGE
      final HumanResourceKey authorizingHumanResourceKey = HumanResourceKey.ADMIN;
      final Date effectiveDate = new Date();
      final OwnerKey ownerKey = Domain.createOwner();
      final LocationKey locationKey = Domain.createLocation();
      PartNoKey partNoKey = Domain.createPart( aPartNo -> {
         aPartNo.setQtyUnitKey( RefQtyUnitKey.EA );
         aPartNo.setPartStatus( RefPartStatusKey.ACTV );
      } );

      final AuthorityKey authorityKey = Domain.createAuthority();

      final InventoryKey inventoryKey = Domain.createSerializedInventory( aInventory -> {
         aInventory.setLocation( locationKey );
         aInventory.setPartNumber( partNoKey );
         aInventory.setCondition( RefInvCondKey.INSRV );
         aInventory.setOwner( ownerKey );
         aInventory.setComplete( true );
      } );

      final InventoryBean inventoryBean = new InventoryBean();
      inventoryBean.setSessionContext( iSessionContextFake );

      // ACT
      inventoryBean.setAuthority( authorityKey, effectiveDate, authorizingHumanResourceKey,
            inventoryKey );

      // ASSERT
      assertEquals( 0, iEventBus.getEventMessages().size() );
   }


   private String getBarcodeFromInventoryKey( InventoryKey aInventoryKey ) {
      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( aInventoryKey );
      return lInvInv.getBarcodeSdesc();
   }


   @Before
   public void setUp() {
      iEventBus = iInjectionOverrideRule.select( RecordingEventBus.class ).get();
      iAuthorizingHr = Domain.createHumanResource( aHr -> aHr.setUser( Domain.createUser() ) );
   }

}
