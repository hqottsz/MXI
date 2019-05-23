
package com.mxi.mx.core.unittest.pi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.i18n.i18n;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PurchaseInvoiceLineKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.RefPoLineTypeKey;
import com.mxi.mx.core.services.purchase.invoice.NonLocalOrganizationPOLineException;
import com.mxi.mx.core.services.purchase.invoice.NonLocalOrganizationPOLineValidator;


/**
 * This class tests the NonLocalOrganizationPOLineValidator method.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class NonLocalOrganizationPOLineValidatorTest {

   private static final OwnerKey OWNER_NON_LOCAL = new OwnerKey( 1, 1 );

   private static final OwnerKey OWNER_LOCAL = new OwnerKey( 1, 2 );

   private static final PurchaseInvoiceLineKey PURCHASE_INVOICE_LINE =
         new PurchaseInvoiceLineKey( 10, 10, 1 );

   private static final PurchaseOrderLineKey PURCHASE_ORDER_LINE =
         new PurchaseOrderLineKey( 100, 100, 1 );

   private static final boolean LOCAL = true;

   private static final boolean NON_LOCAL = false;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   @Before
   public void loadData() throws Exception {
      insertOwner( OWNER_NON_LOCAL, NON_LOCAL );
      insertOwner( OWNER_LOCAL, LOCAL );
      insertPurchaseOrderLine( PURCHASE_ORDER_LINE );
      insertInvoiceLine( PURCHASE_INVOICE_LINE );
      insertPOLineInvoiceMap( PURCHASE_ORDER_LINE, PURCHASE_INVOICE_LINE );
   }


   /**
    * Tests the validate method does not throw an exception for invoice line that is mapped to a
    * local purchase order line
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testValidateLocal() throws Exception {
      updatePOLineOwner( PURCHASE_ORDER_LINE, OWNER_LOCAL );

      try {
         new NonLocalOrganizationPOLineValidator( PURCHASE_INVOICE_LINE.getPurchaseInvoiceKey() )
               .validate();
      } catch ( NonLocalOrganizationPOLineException e ) {
         fail( "Not expecting NonLocalOrganizationPOLineException" );
      }
   }


   /**
    * Tests that an invoice line is mapped to a non-local organization PO line will throw an
    * exception
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testValidateNonLocal() throws Exception {

      updatePOLineOwner( PURCHASE_ORDER_LINE, OWNER_NON_LOCAL );

      try {
         new NonLocalOrganizationPOLineValidator( PURCHASE_INVOICE_LINE.getPurchaseInvoiceKey() )
               .validate();
         fail( "Expected NonLocalOrganizationPOLineException" );
      } catch ( NonLocalOrganizationPOLineException e ) {
         assertEquals( "[MXERR-32391] " + i18n.get( "core.err.32391" ), e.getMessage() );
      }
   }


   /**
    * Inserts an invoice and an invoice line
    *
    * @param aPurchaseInvoiceLineKey
    *           The purchase invoice line key
    */
   private void insertInvoiceLine( PurchaseInvoiceLineKey aPurchaseInvoiceLineKey ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aPurchaseInvoiceLineKey.getPurchaseInvoiceKey(), "po_invoice_db_id",
            "po_invoice_id" );

      MxDataAccess.getInstance().executeInsert( "po_invoice", lArgs );

      lArgs.clear();
      lArgs.add( aPurchaseInvoiceLineKey, "po_invoice_db_id", "po_invoice_id",
            "po_invoice_line_id" );

      MxDataAccess.getInstance().executeInsert( "po_invoice_line", lArgs );
   }


   /**
    * Inserts an owner
    *
    * @param aOwnerKey
    *           The owner key
    * @param aLocal
    *           The boolean to indicate whether it is a local owner or not
    */
   private void insertOwner( OwnerKey aOwnerKey, boolean aLocal ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aOwnerKey, "owner_db_id", "owner_id" );
      lArgs.add( "local_bool", aLocal );

      MxDataAccess.getInstance().executeInsert( "inv_owner", lArgs );
   }


   /**
    * Inserts a mapping between an invoice line and a purchase order line
    *
    * @param aPurchaseOrderLineKey
    *           The purchase order line key
    * @param aPurchaseInvoiceLineKey
    *           The purchase invoice key
    */
   private void insertPOLineInvoiceMap( PurchaseOrderLineKey aPurchaseOrderLineKey,
         PurchaseInvoiceLineKey aPurchaseInvoiceLineKey ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aPurchaseInvoiceLineKey, "po_invoice_db_id", "po_invoice_id",
            "po_invoice_line_id" );
      lArgs.add( aPurchaseOrderLineKey, "po_db_id", "po_id", "po_line_id" );

      MxDataAccess.getInstance().executeInsert( "po_invoice_line_map", lArgs );
   }


   /**
    * Inserts a purchase order line
    *
    * @param aPurchaseOrderLineKey
    *           The purchase order line key
    */
   private void insertPurchaseOrderLine( PurchaseOrderLineKey aPurchaseOrderLineKey ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aPurchaseOrderLineKey, "po_db_id", "po_id", "po_line_id" );
      lArgs.add( RefPoLineTypeKey.PURCHASE, "po_line_type_db_id", "po_line_type_cd" );

      MxDataAccess.getInstance().executeInsert( "po_line", lArgs );
   }


   /**
    * Updates the given purchase order with the given owner
    *
    * @param aPurchaseOrderLineKey
    *           The purchase order key
    * @param aOwnerKey
    *           The owner key
    */
   private void updatePOLineOwner( PurchaseOrderLineKey aPurchaseOrderLineKey,
         OwnerKey aOwnerKey ) {
      DataSetArgument lSetArgs = new DataSetArgument();
      lSetArgs.add( aOwnerKey, "owner_db_id", "owner_id" );

      DataSetArgument lWhereArgs = new DataSetArgument();
      lWhereArgs.add( aPurchaseOrderLineKey, "po_db_id", "po_id", "po_line_id" );

      MxDataAccess.getInstance().executeUpdate( "po_line", lSetArgs, lWhereArgs );
   }
}
