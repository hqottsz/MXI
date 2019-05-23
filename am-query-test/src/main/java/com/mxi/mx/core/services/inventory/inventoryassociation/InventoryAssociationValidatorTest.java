package com.mxi.mx.core.services.inventory.inventoryassociation;

import static com.mxi.mx.core.key.RefInvCondKey.ARCHIVE;
import static com.mxi.mx.core.key.RefInvCondKey.RFI;
import static com.mxi.mx.core.key.RefInvCondKey.SCRAP;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.services.inventory.inventoryassociation.exceptionmatchers.AssociationAlreadyHasNonArchivedInventoryExceptionMatcher;
import com.mxi.mx.core.services.inventory.inventoryassociation.exceptionmatchers.BarcodeForMultipleInventoryExceptionMatcher;
import com.mxi.mx.core.services.inventory.inventoryassociation.exceptionmatchers.BarcodeNotAnInventoryExceptionMatcher;
import com.mxi.mx.core.services.inventory.inventoryassociation.exceptionmatchers.BothInventoryInAssociationsExceptionMatcher;
import com.mxi.mx.core.services.inventory.inventoryassociation.exceptionmatchers.FromInventoryConditionExceptionMatcher;
import com.mxi.mx.core.services.inventory.inventoryassociation.exceptionmatchers.FromInventoryIsSystemExceptionMatcher;
import com.mxi.mx.core.services.inventory.inventoryassociation.exceptionmatchers.InventoriesAreAssociatedExceptionMatcher;
import com.mxi.mx.core.services.inventory.inventoryassociation.exceptionmatchers.InventoryLockedExceptionMatcher;
import com.mxi.mx.core.services.inventory.inventoryassociation.exceptionmatchers.SameInventoryExceptionMatcher;
import com.mxi.mx.core.services.inventory.inventoryassociation.exceptionmatchers.ToInventoryConditionExceptionMatcher;
import com.mxi.mx.core.services.inventory.inventoryassociation.exceptionmatchers.ToInventoryIsSystemExceptionMatcher;
import com.mxi.mx.core.services.inventory.inventoryassociation.exceptions.AssociationAlreadyHasNonArchivedInventoryException;
import com.mxi.mx.core.services.inventory.inventoryassociation.exceptions.BarcodeForMultipleInventoryException;
import com.mxi.mx.core.services.inventory.inventoryassociation.exceptions.BarcodeNotAnInventoryException;
import com.mxi.mx.core.services.inventory.inventoryassociation.exceptions.BothInventoryInAssociationsException;
import com.mxi.mx.core.services.inventory.inventoryassociation.exceptions.FromInventoryConditionException;
import com.mxi.mx.core.services.inventory.inventoryassociation.exceptions.FromInventoryIsSystemException;
import com.mxi.mx.core.services.inventory.inventoryassociation.exceptions.FromInventoryLockedException;
import com.mxi.mx.core.services.inventory.inventoryassociation.exceptions.InventoriesAreAssociatedException;
import com.mxi.mx.core.services.inventory.inventoryassociation.exceptions.InventoryAssociationException;
import com.mxi.mx.core.services.inventory.inventoryassociation.exceptions.SameInventoryException;
import com.mxi.mx.core.services.inventory.inventoryassociation.exceptions.ToInventoryConditionException;
import com.mxi.mx.core.services.inventory.inventoryassociation.exceptions.ToInventoryIsSystemException;
import com.mxi.mx.core.table.inv.InvAssociationDao;


/**
 * Integration unit test for {@linkplain InventoryAssociationValidator}
 *
 */
public class InventoryAssociationValidatorTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public ExpectedException iExpectedException = ExpectedException.none();

   private static final String VALID_TO_INV_BARCODE = "VALID_TO_INV_BARCODE";

   private static final String FROM_INV_DESCRIPTION = "FROM_INV_DESCRIPTION";
   private static final String TO_INV_DESCRIPTION = "FROM_INV_DESCRIPTION";

   private static final String INV_BARCODE = "INV_BARCODE";
   private static final String TASK_BARCODE = "TASK_BARCODE";
   private static final String INV_DESCRIPTION_1 = "INV_DESCRIPTION_1";
   private static final String INV_DESCRIPTION_2 = "INV_DESCRIPTION_2";
   private static final String INV_DESCRIPTION_3 = "INV_DESCRIPTION_3";
   private static final Integer ASSOCIATION_1 = 1;
   private static final Integer ASSOCIATION_2 = 2;
   private static final String SYS_NAME = "SYS_NAME";

   private InvAssociationDao iInvAssociationDao;

   private InventoryKey iValidFromInventory;


   @Before
   public void before() {

      iInvAssociationDao = InjectorContainer.get().getInstance( InvAssociationDao.class );

      // Create a valid from-inventory.
      iValidFromInventory = Domain.createAircraft( aInv -> {
         aInv.setCondition( RFI );
         aInv.setLocked( false );
      } );

      // Create a valid to-inventory.
      Domain.createAircraft( aInv -> {
         aInv.setBarcode( VALID_TO_INV_BARCODE );
         aInv.setCondition( ARCHIVE );
      } );

   }


   @Test
   public void noExceptionThrownWhenValidationSuccessful() throws Exception {
      try {
         new InventoryAssociationValidator().validate( iValidFromInventory, VALID_TO_INV_BARCODE );
      } catch ( InventoryAssociationException ex ) {
         Assert.fail( "Unexpected exception thrown: " + ex );
      }
   }


   @Test
   public void invalidWhenFromInvIsArchived() throws Exception {

      InventoryKey lFromInv = Domain.createAircraft( aInv -> {
         aInv.setDescription( FROM_INV_DESCRIPTION );
         aInv.setCondition( ARCHIVE );
      } );

      iExpectedException.expect( FromInventoryConditionException.class );
      iExpectedException.expect(
            new FromInventoryConditionExceptionMatcher( lFromInv, FROM_INV_DESCRIPTION, ARCHIVE ) );

      new InventoryAssociationValidator().validate( lFromInv, VALID_TO_INV_BARCODE );
   }


   @Test
   public void invalidWhenFromInvIsScrapped() throws Exception {

      InventoryKey lFromInv = Domain.createAircraft( aInv -> {
         aInv.setDescription( FROM_INV_DESCRIPTION );
         aInv.setCondition( SCRAP );
      } );

      iExpectedException.expect( FromInventoryConditionException.class );
      iExpectedException.expect(
            new FromInventoryConditionExceptionMatcher( lFromInv, FROM_INV_DESCRIPTION, SCRAP ) );

      new InventoryAssociationValidator().validate( lFromInv, VALID_TO_INV_BARCODE );
   }


   @Test
   public void invalidWhenFromInvIsLocked() throws Exception {

      InventoryKey lFromInv = Domain.createAircraft( aInv -> {
         aInv.setDescription( FROM_INV_DESCRIPTION );
         aInv.setLocked( true );
      } );

      iExpectedException.expect( FromInventoryLockedException.class );
      iExpectedException
            .expect( new InventoryLockedExceptionMatcher( lFromInv, FROM_INV_DESCRIPTION ) );

      new InventoryAssociationValidator().validate( lFromInv, VALID_TO_INV_BARCODE );
   }


   @Test
   public void invalidWhenToInvBarcodeIsNotInventory() throws Exception {

      iExpectedException.expect( BarcodeNotAnInventoryException.class );
      iExpectedException.expect( new BarcodeNotAnInventoryExceptionMatcher( TASK_BARCODE ) );

      new InventoryAssociationValidator().validate( iValidFromInventory, TASK_BARCODE );
   }


   @Test
   public void invalidWhenToInvBarcodeIsForMultipleInventories() throws Exception {

      InventoryKey lInv1 = Domain.createAircraft( aInv -> {
         aInv.setBarcode( INV_BARCODE );
         aInv.setDescription( INV_DESCRIPTION_1 );
      } );
      InventoryKey lInv2 = Domain.createAircraft( aInv -> {
         aInv.setBarcode( INV_BARCODE );
         aInv.setDescription( INV_DESCRIPTION_2 );
      } );
      InventoryKey lInv3 = Domain.createAircraft( aInv -> {
         aInv.setBarcode( INV_BARCODE );
         aInv.setDescription( INV_DESCRIPTION_3 );
      } );

      Map<InventoryKey, String> lExpectedMap = new HashMap<>();
      lExpectedMap.put( lInv1, INV_DESCRIPTION_1 );
      lExpectedMap.put( lInv2, INV_DESCRIPTION_2 );
      lExpectedMap.put( lInv3, INV_DESCRIPTION_3 );

      iExpectedException.expect( BarcodeForMultipleInventoryException.class );
      iExpectedException
            .expect( new BarcodeForMultipleInventoryExceptionMatcher( INV_BARCODE, lExpectedMap ) );

      new InventoryAssociationValidator().validate( iValidFromInventory, INV_BARCODE );
   }


   @Test
   public void invalidWhenToInvBarcodeIsSameAsFromInventory() throws Exception {

      InventoryKey lFromInv = Domain.createAircraft( aInv -> {
         aInv.setBarcode( INV_BARCODE );
         aInv.setCondition( RFI );
         aInv.setLocked( false );
         aInv.setDescription( FROM_INV_DESCRIPTION );
      } );

      iExpectedException.expect( SameInventoryException.class );
      iExpectedException.expect(
            new SameInventoryExceptionMatcher( lFromInv, FROM_INV_DESCRIPTION, INV_BARCODE ) );

      new InventoryAssociationValidator().validate( lFromInv, INV_BARCODE );
   }


   @Test
   public void invalidWhenToInvIsNotArchived() throws Exception {

      InventoryKey lToInv = Domain.createAircraft( aInv -> {
         aInv.setCondition( RFI );
         aInv.setDescription( TO_INV_DESCRIPTION );
         aInv.setBarcode( INV_BARCODE );
      } );

      iExpectedException.expect( ToInventoryConditionException.class );
      iExpectedException
            .expect( new ToInventoryConditionExceptionMatcher( lToInv, TO_INV_DESCRIPTION, RFI ) );

      new InventoryAssociationValidator().validate( iValidFromInventory, INV_BARCODE );
   }


   @Test
   public void invalidWhenBothInvAreAlreadyAssociated() throws Exception {

      InventoryKey lFromInventory = Domain.createAircraft( aInv -> {
         aInv.setCondition( RFI );
         aInv.setLocked( false );
         aInv.setDescription( FROM_INV_DESCRIPTION );
         aInv.setAssociation( ASSOCIATION_1 );
      } );

      InventoryKey lToInventory = Domain.createAircraft( aInv -> {
         aInv.setBarcode( INV_BARCODE );
         aInv.setCondition( ARCHIVE );
         aInv.setDescription( TO_INV_DESCRIPTION );
         aInv.setAssociation( ASSOCIATION_1 );
      } );

      iExpectedException.expect( InventoriesAreAssociatedException.class );
      iExpectedException.expect( new InventoriesAreAssociatedExceptionMatcher( lFromInventory,
            FROM_INV_DESCRIPTION, lToInventory, TO_INV_DESCRIPTION, ASSOCIATION_1 ) );

      new InventoryAssociationValidator().validate( lFromInventory, INV_BARCODE );
   }


   @Test
   public void invalidWhenBothInvInDifferentAssociations() throws Exception {

      InventoryKey lFromInventory = Domain.createAircraft( aInv -> {
         aInv.setCondition( RFI );
         aInv.setLocked( false );
         aInv.setDescription( FROM_INV_DESCRIPTION );
         aInv.setAssociation( ASSOCIATION_1 );
      } );

      InventoryKey lToInventory = Domain.createAircraft( aInv -> {
         aInv.setBarcode( INV_BARCODE );
         aInv.setCondition( ARCHIVE );
         aInv.setDescription( TO_INV_DESCRIPTION );
         aInv.setAssociation( ASSOCIATION_2 );
      } );

      iExpectedException.expect( BothInventoryInAssociationsException.class );
      iExpectedException.expect( new BothInventoryInAssociationsExceptionMatcher( lFromInventory,
            FROM_INV_DESCRIPTION, lToInventory, TO_INV_DESCRIPTION ) );

      new InventoryAssociationValidator().validate( lFromInventory, INV_BARCODE );
   }


   @Test
   public void invalidWhenFromInvIsSystem() throws Exception {

      InventoryKey lAcft = Domain.createAircraft( aInv -> aInv.addSystem( aSys -> {
         aSys.setName( SYS_NAME );
         aSys.setCondition( RFI );
         aSys.setLocked( false );
      } ) );
      InventoryKey lFromInventory = Domain.readSystem( lAcft, SYS_NAME );

      iExpectedException.expect( FromInventoryIsSystemException.class );
      iExpectedException
            .expect( new FromInventoryIsSystemExceptionMatcher( lFromInventory, SYS_NAME ) );

      new InventoryAssociationValidator().validate( lFromInventory, VALID_TO_INV_BARCODE );
   }


   @Test
   public void invalidWhenToInvIsSystem() throws Exception {

      InventoryKey lAcft = Domain.createAircraft( aInv -> aInv.addSystem( aSys -> {
         aSys.setName( SYS_NAME );
         aSys.setBarcode( INV_BARCODE );
         aSys.setCondition( ARCHIVE );
      } ) );
      InventoryKey lToInventory = Domain.readSystem( lAcft, SYS_NAME );

      iExpectedException.expect( ToInventoryIsSystemException.class );
      iExpectedException
            .expect( new ToInventoryIsSystemExceptionMatcher( lToInventory, SYS_NAME ) );

      new InventoryAssociationValidator().validate( iValidFromInventory, INV_BARCODE );
   }


   @Test
   public void invalidWhenToInvIsInAssociationWithAnotherNonArchiveInv() throws Exception {

      InventoryKey lFromInventory = Domain.createAircraft( aInv -> {
         aInv.setCondition( RFI );
         aInv.setLocked( false );
         aInv.setDescription( FROM_INV_DESCRIPTION );
      } );

      InventoryKey lToInventory = Domain.createAircraft( aInv -> {
         aInv.setBarcode( INV_BARCODE );
         aInv.setCondition( ARCHIVE );
         aInv.setDescription( TO_INV_DESCRIPTION );
         aInv.setAssociation( ASSOCIATION_1 );
      } );
      Domain.createAircraft( aInv -> {
         aInv.setCondition( RFI );
         aInv.setAssociation( ASSOCIATION_1 );
      } );

      iExpectedException.expect( AssociationAlreadyHasNonArchivedInventoryException.class );
      iExpectedException.expect( new AssociationAlreadyHasNonArchivedInventoryExceptionMatcher(
            lToInventory, TO_INV_DESCRIPTION ) );
      new InventoryAssociationValidator().validate( lFromInventory, INV_BARCODE );
   }

}
