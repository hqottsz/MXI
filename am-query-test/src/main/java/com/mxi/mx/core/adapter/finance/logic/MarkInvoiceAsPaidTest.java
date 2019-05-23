
package com.mxi.mx.core.adapter.finance.logic;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.VendorBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.adapter.finance.model.Invoice;
import com.mxi.mx.core.adapter.finance.model.Vendor;
import com.mxi.mx.core.key.PurchaseInvoiceKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.VendorKey;
import com.mxi.mx.core.table.evt.EvtEventDao;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.evt.JdbcEvtEventDao;


/**
 * This class tests the {@link MxFinanceAdapter.markInvoiceAsPaid} method.
 */

public final class MarkInvoiceAsPaidTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private final static String INVOICE_NUMBER_1 = "INVOICE 1";

   private final static String INVOICE_NUMBER_2 = "INVOICE 2";

   private final static String VENDOR_1 = "VENDOR 1";

   private final static String VENDOR_2 = "VENDOR 2";

   private final static String NO_VENDOR_PROVIDED = null;

   private final static String EXTERNAL_KEY_1 = "EXTERNAL KEY 1";

   private final static String EXTERNAL_KEY_2 = "EXTERNAL KEY 2";


   // XML message accepts either an invoice number, an external key or both
   private static enum Mode {
      INVOICE_NUMBER, EXTERNAL_KEY, BOTH
   };


   /**
    * Test that invoice found by invoice number cannot be marked as paid because it has an OPEN
    * status
    *
    * @throws Exception
    *            if anything goes wrong
    */
   @Test
   public void testOpenInvoiceFound_ByInvoiceNumber() throws Exception {

      // DATA SETUP: Create an invoice
      createInvoiceForVendor( INVOICE_NUMBER_1, null, RefEventStatusKey.PIOPEN, VENDOR_1 );

      try {
         markInvoiceAsPaid( Mode.INVOICE_NUMBER, INVOICE_NUMBER_1, NO_VENDOR_PROVIDED );

         fail( "Expected MarkInvoiceAsPaidException" );

      } catch ( MarkInvoiceAsPaidException exception ) {

         assertThat( exception.toString(), containsString( "[MXERR-33849]" ) );
      }
   }


   /**
    * Test that invoice found by the invoice number is marked as paid
    *
    * @throws Exception
    *            if anything goes wrong
    */
   @Test
   public void testToBePaidInvoiceFound_ByInvoiceNumber() throws Exception {

      // DATA SETUP: Create an invoice
      PurchaseInvoiceKey purchaseInvoice1 = createInvoiceForVendor( INVOICE_NUMBER_1, null,
            RefEventStatusKey.PITOBEPAID, VENDOR_1 );

      PurchaseInvoiceKey purchaseInvoice2 = createInvoiceForVendor( INVOICE_NUMBER_2, null,
            RefEventStatusKey.PITOBEPAID, VENDOR_2 );

      EvtEventDao evtEventDao = new JdbcEvtEventDao();

      // Asserts that the invoice 1 status is TOBEPAID
      EvtEventTable evtEventTable1 = evtEventDao.findByPrimaryKey( purchaseInvoice1.getEventKey() );
      assertEquals( RefEventStatusKey.PITOBEPAID, evtEventTable1.getEventStatus() );

      // Asserts that the invoice 2 status is TOBEPAID
      EvtEventTable evtEventTable2 = evtEventDao.findByPrimaryKey( purchaseInvoice2.getEventKey() );
      assertEquals( RefEventStatusKey.PITOBEPAID, evtEventTable2.getEventStatus() );

      // Mark Invoice as Paid
      markInvoiceAsPaid( Mode.INVOICE_NUMBER, INVOICE_NUMBER_1, NO_VENDOR_PROVIDED );

      // Assert that the invoice 1 status is now PAID
      evtEventDao.refresh( evtEventTable1 );
      assertEquals( RefEventStatusKey.PIPAID, evtEventTable1.getEventStatus() );

      // Assert that the invoice 2 status remains TOBEPAID
      evtEventDao.refresh( evtEventTable2 );
      assertEquals( RefEventStatusKey.PITOBEPAID, evtEventTable2.getEventStatus() );
   }


   /**
    * Test that there is no non-historic invoice found by the given invoice number
    *
    * @throws Exception
    *            if anything goes wrong
    */
   @Test
   public void testMultipleHistoricInvoiceFound_ByInvoiceNumber() throws Exception {

      // DATA SETUP: Create historic invoices
      createInvoiceForVendor( INVOICE_NUMBER_1, null, RefEventStatusKey.PIPAID, VENDOR_1 );

      createInvoiceForVendor( INVOICE_NUMBER_1, null, RefEventStatusKey.PICANCEL, VENDOR_2 );

      try {
         markInvoiceAsPaid( Mode.INVOICE_NUMBER, INVOICE_NUMBER_1, NO_VENDOR_PROVIDED );

         fail( "Expected MarkInvoiceAsPaidException" );

      } catch ( MarkInvoiceAsPaidException exception ) {

         assertThat( exception.toString(), containsString( "[MXERR-33852]" ) );
      }
   }


   /**
    * Test that invoices found by the given invoice number cannot be marked as paid because there
    * are multiple non-historic invoices for different vendors.
    *
    * @throws Exception
    *            if anything goes wrong
    */
   @Test
   public void testMultipleNonHistoricInvoiceFound_ByInvoiceNumber() throws Exception {

      // DATA SETUP: Create non-historic invoices for different vendors
      createInvoiceForVendor( INVOICE_NUMBER_1, null, RefEventStatusKey.PITOBEPAID, VENDOR_1 );

      createInvoiceForVendor( INVOICE_NUMBER_1, null, RefEventStatusKey.PITOBEPAID, VENDOR_2 );

      try {
         markInvoiceAsPaid( Mode.INVOICE_NUMBER, INVOICE_NUMBER_1, NO_VENDOR_PROVIDED );

         fail( "Expected MarkInvoiceAsPaidException" );

      } catch ( MarkInvoiceAsPaidException exception ) {

         assertThat( exception.toString(), containsString( "[MXERR-33853]" ) );
      }
   }


   /**
    * Test that invoice found by the given invoice number and vendor cannot be marked as paid
    * because it has OPEN status
    *
    * @throws Exception
    *            if anything goes wrong
    */
   @Test
   public void testOpenInvoiceFoundForGivenVendor_ByInvoiceNumber() throws Exception {

      // DATA SETUP: Create an invoice
      createInvoiceForVendor( INVOICE_NUMBER_1, null, RefEventStatusKey.PITOBEPAID, VENDOR_1 );

      createInvoiceForVendor( INVOICE_NUMBER_1, null, RefEventStatusKey.PIOPEN, VENDOR_2 );

      try {
         markInvoiceAsPaid( Mode.INVOICE_NUMBER, INVOICE_NUMBER_1, VENDOR_2 );

         fail( "Expected MarkInvoiceAsPaidException" );

      } catch ( MarkInvoiceAsPaidException exception ) {

         assertThat( exception.toString(), containsString( "[MXERR-33849]" ) );
      }
   }


   /**
    * Test that invoice found by the given invoice number and vendor is marked as paid.
    *
    * @throws Exception
    *            if anything goes wrong
    */
   @Test
   public void testMarkInvoiceAsPaidForGivenVendor_ByInvoiceNumber() throws Exception {

      // DATA SETUP: Create non-historic invoices for different vendors
      PurchaseInvoiceKey invoiceForVendor1 = createInvoiceForVendor( INVOICE_NUMBER_1, null,
            RefEventStatusKey.PITOBEPAID, VENDOR_1 );

      PurchaseInvoiceKey invoiceForVendor2 = createInvoiceForVendor( INVOICE_NUMBER_1, null,
            RefEventStatusKey.PITOBEPAID, VENDOR_2 );

      // Mark Invoice as Paid
      markInvoiceAsPaid( Mode.INVOICE_NUMBER, INVOICE_NUMBER_1, VENDOR_1 );

      EvtEventDao evtEventDao = new JdbcEvtEventDao();

      // Asserts that invoice for vendor 1 is PAID
      assertEquals( RefEventStatusKey.PIPAID,
            evtEventDao.findByPrimaryKey( invoiceForVendor1.getEventKey() ).getEventStatus() );

      // Asserts that invoice for vendor 2 remains TOBEPAID
      assertEquals( RefEventStatusKey.PITOBEPAID,
            evtEventDao.findByPrimaryKey( invoiceForVendor2.getEventKey() ).getEventStatus() );
   }


   /**
    * Test that only the non-historic invoice found the invoice number will be marked as paid
    *
    * @throws Exception
    *            if anything goes wrong
    */
   @Test
   public void testMarkNonHistoricInvoiceAsPaid_ByInvoiceNumber() throws Exception {

      // DATA SETUP: Create multiple non-historic and historic invoices
      PurchaseInvoiceKey historicInvoice =
            createInvoiceForVendor( INVOICE_NUMBER_1, null, RefEventStatusKey.PICANCEL, VENDOR_1 );

      PurchaseInvoiceKey nonHistoricInvoice = createInvoiceForVendor( INVOICE_NUMBER_1, null,
            RefEventStatusKey.PITOBEPAID, VENDOR_2 );

      // Mark Invoice as Paid
      markInvoiceAsPaid( Mode.INVOICE_NUMBER, INVOICE_NUMBER_1, NO_VENDOR_PROVIDED );

      // Asserts that the non-historic invoice is now PAID
      EvtEventDao evtEventDao = new JdbcEvtEventDao();

      EvtEventTable evtEventTable =
            evtEventDao.findByPrimaryKey( nonHistoricInvoice.getEventKey() );
      assertEquals( RefEventStatusKey.PIPAID, evtEventTable.getEventStatus() );

      // Asserts that the historic invoice is unchanged
      evtEventTable = evtEventDao.findByPrimaryKey( historicInvoice.getEventKey() );
      assertEquals( RefEventStatusKey.PICANCEL, evtEventTable.getEventStatus() );
   }


   /**
    * Test that invoice found by the external key is marked as paid
    *
    * @throws Exception
    *            if anything goes wrong
    */
   @Test
   public void testToBePaidInvoiceFound_ByExternalKey() throws Exception {

      // DATA SETUP: Create an invoice
      PurchaseInvoiceKey purchaseInvoice1 = createInvoiceForVendor( INVOICE_NUMBER_1,
            EXTERNAL_KEY_1, RefEventStatusKey.PITOBEPAID, VENDOR_1 );

      // DATA SETUP: Create another invoice with the same invoice number but different external key
      PurchaseInvoiceKey purchaseInvoice2 = createInvoiceForVendor( INVOICE_NUMBER_1,
            EXTERNAL_KEY_2, RefEventStatusKey.PITOBEPAID, VENDOR_2 );

      EvtEventDao evtEventDao = new JdbcEvtEventDao();

      // Asserts that the invoice 1 status is TOBEPAID
      EvtEventTable evtEventTable1 = evtEventDao.findByPrimaryKey( purchaseInvoice1.getEventKey() );
      assertEquals( RefEventStatusKey.PITOBEPAID, evtEventTable1.getEventStatus() );

      // Asserts that the invoice 2 status is TOBEPAID
      EvtEventTable evtEventTable2 = evtEventDao.findByPrimaryKey( purchaseInvoice2.getEventKey() );
      assertEquals( RefEventStatusKey.PITOBEPAID, evtEventTable2.getEventStatus() );

      // Mark Invoice as Paid
      markInvoiceAsPaid( Mode.EXTERNAL_KEY, EXTERNAL_KEY_1, NO_VENDOR_PROVIDED );

      // Assert that the invoice 1 status is now PAID
      evtEventDao.refresh( evtEventTable1 );
      assertEquals( RefEventStatusKey.PIPAID, evtEventTable1.getEventStatus() );

      // Assert that the invoice 2 status remains unchanged
      evtEventDao.refresh( evtEventTable2 );
      assertEquals( RefEventStatusKey.PITOBEPAID, evtEventTable2.getEventStatus() );
   }


   /**
    * Test that invoice found by the external key cannot be marked as paid because it has an OPEN
    * status
    *
    * @throws Exception
    *            if anything goes wrong
    */
   @Test
   public void testOpenInvoiceFound_ByExternalKey() throws Exception {

      // DATA SETUP: Create an invoice
      createInvoiceForVendor( INVOICE_NUMBER_1, EXTERNAL_KEY_1, RefEventStatusKey.PIOPEN,
            VENDOR_1 );

      try {
         markInvoiceAsPaid( Mode.EXTERNAL_KEY, EXTERNAL_KEY_1, NO_VENDOR_PROVIDED );

         fail( "Expected MarkInvoiceAsPaidException" );

      } catch ( MarkInvoiceAsPaidException exception ) {

         assertThat( exception.toString(), containsString( "[MXERR-33849]" ) );
      }
   }


   /**
    * Test that invoice cannot be found with the given invoice number AND external key
    *
    * @throws Exception
    *            if anything goes wrong
    */
   @Test
   public void testInvoiceCannotBeFound_ByInvoiceNumberAndExternalKey() throws Exception {

      // DATA SETUP: Create invoices
      createInvoiceForVendor( INVOICE_NUMBER_1, EXTERNAL_KEY_1, RefEventStatusKey.PITOBEPAID,
            VENDOR_1 );

      createInvoiceForVendor( INVOICE_NUMBER_2, EXTERNAL_KEY_2, RefEventStatusKey.PITOBEPAID,
            VENDOR_2 );

      try {
         // Mark Invoice as Paid
         Invoice invoice = new Invoice();
         invoice.setInvoiceNumber( INVOICE_NUMBER_1 );
         invoice.setExternalNumber( EXTERNAL_KEY_2 );

         new MxFinanceAdapter().markInvoiceAsPaid( invoice, new Date(), null );

         fail( "Expected MarkInvoiceAsPaidException" );

      } catch ( MarkInvoiceAsPaidException exception ) {

         assertThat( exception.toString(), containsString( "[MXERR-33852]" ) );
      }
   }


   private PurchaseInvoiceKey createInvoiceForVendor( String invoiceNumber, String externalKey,
         RefEventStatusKey status, String vendorCd ) {

      VendorKey vendorKey = new VendorBuilder().withCode( vendorCd ).build();

      // DATA SETUP: Create multiple non-historic and historic invoices
      PurchaseInvoiceKey purchaseInvoiceKey = Domain.createInvoice( invoice -> {
         invoice.setInvoiceNumber( invoiceNumber );
         invoice.setInvoiceStatus( status );
         invoice.setVendor( vendorKey );
      } );

      // DATA SETUP: Set external key for the invoice
      EvtEventDao evtEventDao = new JdbcEvtEventDao();

      EvtEventTable evtEventTable =
            evtEventDao.findByPrimaryKey( purchaseInvoiceKey.getEventKey() );
      evtEventTable.setExtKeySDesc( externalKey );

      evtEventDao.update( evtEventTable );

      return purchaseInvoiceKey;
   }


   private void markInvoiceAsPaid( Mode mode, String invoiceIdentifier, String vendorCd )
         throws MxException {

      // Mark Invoice as Paid
      Invoice invoice = new Invoice();

      if ( Mode.INVOICE_NUMBER.equals( mode ) ) {
         invoice.setInvoiceNumber( invoiceIdentifier );
      } else if ( Mode.EXTERNAL_KEY.equals( mode ) ) {
         invoice.setExternalNumber( invoiceIdentifier );
      }

      if ( vendorCd != null ) {
         Vendor vendor = new Vendor();
         vendor.setCode( vendorCd );

         invoice.setVendor( vendor );
      }

      new MxFinanceAdapter().markInvoiceAsPaid( invoice, new Date(), null );
   }


   @Before
   public void setup() {

      int currentUserId = SecurityIdentificationUtils.getInstance().getCurrentUserId();

      // DATA SETUP: Create a human resource
      Domain.createHumanResource( aHr -> aHr.setUser( Domain.createUser( aUser -> {
         aUser.setUserId( currentUserId );
      } ) ) );
   }

}
