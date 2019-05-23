package com.mxi.mx.core.services.purchase.invoice.validator;

import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.VendorBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.PurchaseInvoiceKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.VendorKey;
import com.mxi.mx.core.services.purchase.invoice.exception.DuplicatePOInvoiceNumberVendorException;


public class DuplicatePOInvoiceNumberVendorValidatorTest {

   private static final String VENDOR_CD = "VENDOR_1";
   private static final String INVOICE_NO = "12345";

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();
   @Rule
   public InjectionOverrideRule fakeGuiceDaoRule = new InjectionOverrideRule();

   // subject under test
   private DuplicatePOInvoiceNumberVendorValidator validator;


   @Before
   public void setUp() {
      validator = new DuplicatePOInvoiceNumberVendorValidator();
   }


   @Test( expected = NullPointerException.class )
   public void validate_missingInvoiceNumber() throws Exception {
      createPOInvoice( INVOICE_NO, VENDOR_CD, RefEventStatusKey.PIOPEN );
      validator.validate( null, null, VENDOR_CD );
   }


   @Test( expected = NullPointerException.class )
   public void validate_missingVendorCode() throws Exception {
      createPOInvoice( INVOICE_NO, VENDOR_CD, RefEventStatusKey.PIOPEN );
      validator.validate( Optional.empty(), INVOICE_NO, null );
   }


   @Test( expected = DuplicatePOInvoiceNumberVendorException.class )
   public void validate_existingPOInvoiceNumber_statusOPEN_sameVendor() throws Exception {
      createPOInvoice( INVOICE_NO, VENDOR_CD, RefEventStatusKey.PIOPEN );
      validator.validate( Optional.empty(), INVOICE_NO, VENDOR_CD );
   }


   @Test
   public void validate_existingPOInvoiceNumber_statusPAID_sameVendor() throws Exception {
      createPOInvoice( INVOICE_NO, VENDOR_CD, RefEventStatusKey.PIPAID );
      validator.validate( Optional.empty(), INVOICE_NO, VENDOR_CD );
   }


   @Test
   public void validate_existingPOInvoiceNumber_statusCANCEL_sameVendor() throws Exception {
      createPOInvoice( INVOICE_NO, VENDOR_CD, RefEventStatusKey.PICANCEL );
      validator.validate( Optional.empty(), INVOICE_NO, VENDOR_CD );
   }


   @Test( expected = DuplicatePOInvoiceNumberVendorException.class )
   public void validate_existingPOInvoiceNumber_statusTOBEPAID_sameVendor() throws Exception {
      createPOInvoice( INVOICE_NO, VENDOR_CD, RefEventStatusKey.PITOBEPAID );
      validator.validate( Optional.empty(), INVOICE_NO, VENDOR_CD );
   }


   @Test( expected = DuplicatePOInvoiceNumberVendorException.class )
   public void validate_existingPOInvoiceNumber_statusTOBEPAID_sameVendor_withInvoiceKey()
         throws Exception {

      // existing invoice
      createPOInvoice( INVOICE_NO, VENDOR_CD, RefEventStatusKey.PITOBEPAID );

      // invoice to be validated
      PurchaseInvoiceKey purchaseInvoiceKey =
            createPOInvoice( INVOICE_NO, VENDOR_CD, RefEventStatusKey.PIOPEN );

      validator.validate( Optional.of( purchaseInvoiceKey ), INVOICE_NO, VENDOR_CD );
   }


   @Test( expected = DuplicatePOInvoiceNumberVendorException.class )
   public void validate_existingPOInvoiceNumber_statusOPEN_sameVendor_withInvoiceKey()
         throws Exception {

      // existing invoice
      createPOInvoice( INVOICE_NO, VENDOR_CD, RefEventStatusKey.PIOPEN );

      // invoice to be validated
      PurchaseInvoiceKey purchaseInvoiceKey =
            createPOInvoice( INVOICE_NO, VENDOR_CD, RefEventStatusKey.PIOPEN );

      validator.validate( Optional.of( purchaseInvoiceKey ), INVOICE_NO, VENDOR_CD );
   }


   @Test
   public void validate_existingPOInvoiceNumber_statusCANCEL_sameVendor_withInvoiceKey()
         throws Exception {

      // existing invoice
      createPOInvoice( INVOICE_NO, VENDOR_CD, RefEventStatusKey.PICANCEL );

      // invoice to be validated
      PurchaseInvoiceKey purchaseInvoiceKey =
            createPOInvoice( INVOICE_NO, VENDOR_CD, RefEventStatusKey.PIOPEN );

      validator.validate( Optional.of( purchaseInvoiceKey ), INVOICE_NO, VENDOR_CD );
   }


   @Test
   public void validate_existingPOInvoiceNumber_statusPAID_sameVendor_withInvoiceKey()
         throws Exception {

      // existing invoice
      createPOInvoice( INVOICE_NO, VENDOR_CD, RefEventStatusKey.PIPAID );

      // invoice to be validated
      PurchaseInvoiceKey purchaseInvoiceKey =
            createPOInvoice( INVOICE_NO, VENDOR_CD, RefEventStatusKey.PIOPEN );

      validator.validate( Optional.of( purchaseInvoiceKey ), INVOICE_NO, VENDOR_CD );
   }


   private PurchaseInvoiceKey createPOInvoice( String invoiceNumber, String vendorCode,
         RefEventStatusKey invoiceStatus ) {

      VendorKey vendor = new VendorBuilder().withCode( vendorCode ).build();

      return Domain.createInvoice( invoice -> {
         invoice.setInvoiceNumber( invoiceNumber );
         invoice.setVendor( vendor );
         invoice.setInvoiceStatus( invoiceStatus );
      } );
   }
}
