package com.mxi.mx.core.services.purchase.invoice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.AccountBuilder;
import com.mxi.am.domain.builder.CurrencyBuilder;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.InvoiceLineBuilder;
import com.mxi.am.domain.builder.OrderBuilder;
import com.mxi.am.domain.builder.OrderLineBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.VendorBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.am.ee.OperateAsUserRule;
import com.mxi.mx.common.exception.MandatoryArgumentException;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.exception.NegativeValueException;
import com.mxi.mx.common.exception.StringTooLongException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PurchaseInvoiceKey;
import com.mxi.mx.core.key.PurchaseInvoiceLineKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.RefAccountTypeKey;
import com.mxi.mx.core.key.RefCurrencyKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefFinanceTypeKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefPoLineTypeKey;
import com.mxi.mx.core.key.RefPoTypeKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefTermsConditionsKey;
import com.mxi.mx.core.key.VendorAccountKey;
import com.mxi.mx.core.key.VendorKey;
import com.mxi.mx.core.services.purchase.invoice.InvoiceOrderStateValidator.InvoiceOrderHasInvalidStateException;
import com.mxi.mx.core.services.purchase.invoice.exception.DuplicatePOInvoiceNumberVendorException;
import com.mxi.mx.core.services.user.InvalidUsernameException;
import com.mxi.mx.core.services.vendor.InvalidVendorCodeException;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.po.POLineTable;
import com.mxi.mx.core.table.po.PoInvoice;
import com.mxi.mx.core.table.po.PoInvoiceLine;


/**
 * This class tests the
 * {@link PurchaseInvoiceService#create(POInvoiceDetailsTO, PurchaseOrderKey, HumanResourceKey)}
 * method.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class CreatePurchaseInvoiceTest {

   @Rule
   public OperateAsUserRule iOperateAsUserRule =
         new OperateAsUserRule( USERID_TESTUSER, USERNAME_TESTUSER );
   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static final int USERID_TESTUSER = 999;
   private static final String USERNAME_TESTUSER = "testuser";

   private static final String INVOICE_NUMBER = "INV-TEST-01";
   private static final Date INVOICE_DATE = new Date();
   private static final String VENDOR = "TESTVENDOR";
   private static final BigDecimal EXCHANGE_RATE = BigDecimal.ONE;
   private static final Double CASH_DISCOUNT = 5.0;
   private static final Date CASH_DISCOUNT_DATE = new Date();
   private static final String INVOICE_NOTE = "Invoice Note";
   private static final BigDecimal QUANTITY = BigDecimal.TEN;
   private static final RefQtyUnitKey UNIT_QTY = RefQtyUnitKey.EA;
   private static final BigDecimal UNIT_PRICE = new BigDecimal( "35.97" );
   private static final BigDecimal LINE_PRICE = new BigDecimal( "359.7" );
   private static final String LINE_DESC = "line description";

   private VendorKey iVendor;
   private RefCurrencyKey iDefaultCurrency;
   private HumanResourceKey iHr;
   private FncAccountKey iAssetAccount;
   private PartNoKey iPart;
   private RefTermsConditionsKey iTermsConditions;
   private VendorAccountKey iVendorAccount;


   @Before
   public void loadData() throws Exception {

      iHr = new HumanResourceDomainBuilder().withUsername( USERNAME_TESTUSER )
            .withUserId( USERID_TESTUSER ).build();

      // create the default currency as USD
      iDefaultCurrency = new CurrencyBuilder( "USD" ).isDefault().build();

      // create a vendor
      iVendor = new VendorBuilder().withCode( VENDOR ).build();

      // create an asset account
      iAssetAccount =
            new AccountBuilder().withType( RefAccountTypeKey.INVASSET ).isDefault().build();

      // create a part
      iPart = new PartNoBuilder().withUnitType( UNIT_QTY ).withOemPartNo( "STD_PART" )
            .withShortDescription( "Standard Part" ).withTotalValue( new BigDecimal( 0 ) )
            .withInventoryClass( RefInvClassKey.BATCH ).withAssetAccount( iAssetAccount )
            .withFinancialType( RefFinanceTypeKey.CONSUM ).withStatus( RefPartStatusKey.ACTV )
            .withTotalQuantity( new BigDecimal( 0 ) )
            .withAverageUnitPrice( new BigDecimal( 510.37 ) ).build();

      iTermsConditions = new RefTermsConditionsKey( 10, "NET15" );
      iVendorAccount = new VendorAccountKey( 4650, 10004, "TEST" );

   }


   /**
    *
    * Test the happy path: an adhoc invoice can be created.
    *
    * @throws MxException
    */
   @Test
   public void testGivenNoPurchaseOrderThenAdhocInvoiceIsCreated() throws MxException {
      // given
      POInvoiceDetailsTO lPOInvoiceDetailsTO = createPOInvoiceDetailsTO();

      // when
      PurchaseInvoiceKey lInvoice = PurchaseInvoiceService.create( lPOInvoiceDetailsTO, null, iHr );

      // then
      assertInvoiceCreated( lInvoice );
   }


   /**
    *
    * Test the happy path: an invoice can be created for a purchase order.
    *
    * @throws MxException
    */
   @Test
   public void testGivenPurchaseOrderThenInvoiceIsCreatedForOrder() throws MxException {

      // given
      PurchaseOrderKey lPurchaseOrder = createIssuedPurchaseOrder();
      PurchaseOrderLineKey lPurchaseOrderLine = addOrderLine( lPurchaseOrder );

      POInvoiceDetailsTO lPOInvoiceDetailsTO = createPOInvoiceDetailsTO();

      // when
      PurchaseInvoiceKey lInvoice =
            PurchaseInvoiceService.create( lPOInvoiceDetailsTO, lPurchaseOrder, iHr );

      // then
      assertInvoiceCreatedForOrder( lInvoice, lPurchaseOrderLine );

   }


   /**
    *
    * Given an existing invoice with the same invoice number and vendor as the invoice to be
    * created, an exception is thrown and the invoice is not created.
    *
    * @throws MxException
    */
   @Test
   public void testGivenPurchaseOrderHalfInvoicedThenInvoiceIsCreatedForRemainingOrderLines()
         throws MxException {
      // given
      PurchaseOrderKey lPurchaseOrder = createIssuedPurchaseOrder();
      PurchaseOrderLineKey lFirstOrderLine = addOrderLine( lPurchaseOrder );

      POInvoiceDetailsTO lPOInvoiceDetailsTO = createPOInvoiceDetailsTO();

      // create an existing invoice in the system for the first order line
      createExistingInvoice( "TEST-INV-02", lFirstOrderLine );

      // add another order line for a different part
      PartNoKey lOtherPart = new PartNoBuilder().withUnitType( UNIT_QTY )
            .withOemPartNo( "STD_PART_2" ).withShortDescription( "Standard Part 2" )
            .withTotalValue( new BigDecimal( 0 ) ).withInventoryClass( RefInvClassKey.BATCH )
            .withAssetAccount( iAssetAccount ).withFinancialType( RefFinanceTypeKey.CONSUM )
            .withStatus( RefPartStatusKey.ACTV ).withTotalQuantity( new BigDecimal( 1 ) )
            .withAverageUnitPrice( new BigDecimal( 99.67 ) ).build();
      PurchaseOrderLineKey lSecondOrderLine = new OrderLineBuilder( lPurchaseOrder )
            .withLineType( RefPoLineTypeKey.PURCHASE ).forPart( lOtherPart )
            .withAccount( iAssetAccount ).withOrderQuantity( new BigDecimal( 5 ) )
            .withUnitPrice( UNIT_PRICE ).withUnitType( UNIT_QTY ).withLinePrice( LINE_PRICE )
            .withUnitType( UNIT_QTY ).withLineDescription( LINE_DESC ).build();

      // when
      PurchaseInvoiceKey lInvoice =
            PurchaseInvoiceService.create( lPOInvoiceDetailsTO, lPurchaseOrder, iHr );

      // then
      assertInvoiceCreatedForOrder( lInvoice, lSecondOrderLine );
   }


   /**
    *
    * Given a currency mismatch between the po and the invoice details, an exception is thrown and
    * the invoice is not created.
    *
    * @throws MxException
    */
   @Test( expected = CurrencyMismatchException.class )
   public void testGivenCurrencyMismatchThenInvoiceIsNotCreated() throws MxException {
      // given
      PurchaseOrderKey lPurchaseOrder = createIssuedPurchaseOrder();
      addOrderLine( lPurchaseOrder );

      POInvoiceDetailsTO lPOInvoiceDetailsTO = createPOInvoiceDetailsTO();

      // change the currency on the invoice
      lPOInvoiceDetailsTO.setCurrency( new CurrencyBuilder( "CAD" ).build() );

      // when
      PurchaseInvoiceService.create( lPOInvoiceDetailsTO, lPurchaseOrder, iHr );

      // then -- exception
   }


   /**
    *
    * Given an existing invoice with the same invoice number and vendor as the invoice to be
    * created, an exception is thrown and the invoice is not created.
    *
    * @throws MxException
    */
   @Test( expected = DuplicatePOInvoiceNumberVendorException.class )
   public void testGivenDuplicateInvoiceNumberAndVendorThenInvoiceIsNotCreated()
         throws MxException {
      // given
      PurchaseOrderKey lPurchaseOrder = createIssuedPurchaseOrder();
      PurchaseOrderLineKey lPurchaseOrderLine = addOrderLine( lPurchaseOrder );

      POInvoiceDetailsTO lPOInvoiceDetailsTO = createPOInvoiceDetailsTO();

      // create an existing invoice in the system
      createExistingInvoice( INVOICE_NUMBER, lPurchaseOrderLine );

      // when
      PurchaseInvoiceService.create( lPOInvoiceDetailsTO, lPurchaseOrder, iHr );

      // then -- exception
   }


   /**
    *
    * Given an invalid vendor, an exception is thrown and the invoice is not created.
    *
    * @throws MxException
    */
   @Test( expected = InvalidVendorCodeException.class )
   public void testGivenInvalidVendorThenInvoiceIsNotCreated() throws MxException {
      // given
      PurchaseOrderKey lPurchaseOrder = createIssuedPurchaseOrder();

      // an invalid vendor on the invoice to be created
      POInvoiceDetailsTO lPOInvoiceDetailsTO = createPOInvoiceDetailsTO();
      lPOInvoiceDetailsTO.setVendor( "INVALIDVENDOR" );

      // when
      PurchaseInvoiceService.create( lPOInvoiceDetailsTO, lPurchaseOrder, iHr );

      // then -- exception
   }


   /**
    *
    * Given an invalid user, an exception is thrown and the invoice is not created.
    *
    * @throws MxException
    */
   @Test( expected = InvalidUsernameException.class )
   public void testGivenInvalidUserThenInvoiceIsNotCreated() throws MxException {
      // given
      PurchaseOrderKey lPurchaseOrder = createIssuedPurchaseOrder();

      // an invalid user on the invoice to be created
      POInvoiceDetailsTO lPOInvoiceDetailsTO = createPOInvoiceDetailsTO();
      lPOInvoiceDetailsTO.setPOInvoiceContact( "invalid_username" );

      // when
      PurchaseInvoiceService.create( lPOInvoiceDetailsTO, lPurchaseOrder, iHr );

      // then -- exception
   }


   /**
    * Given an open PO, an exception is thrown and the invoice is not created.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test( expected = InvoiceOrderHasInvalidStateException.class )
   public void testGivenOpenPurchaseOrderThenInvoiceIsNotCreated() throws Exception {

      // given
      PurchaseOrderKey lPurchaseOrder = createPurchaseOrder( RefEventStatusKey.POOPEN, false );

      POInvoiceDetailsTO lPOInvoiceDetailsTO = createPOInvoiceDetailsTO();

      // when
      PurchaseInvoiceService.create( lPOInvoiceDetailsTO, lPurchaseOrder, iHr );

      // then -- exception
   }


   /**
    * Given a re-opened PO that has been issued before, the invoice is created.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testGivenOpenPurchaseOrderThatWasIssuedThenInvoiceIsCreated() throws Exception {

      // given
      PurchaseOrderKey lPurchaseOrder = createPurchaseOrder( RefEventStatusKey.POOPEN, true );
      PurchaseOrderLineKey lPurchaseOrderLine = addOrderLine( lPurchaseOrder );

      POInvoiceDetailsTO lPOInvoiceDetailsTO = createPOInvoiceDetailsTO();

      // when
      PurchaseInvoiceKey lPurchaseInvoice =
            PurchaseInvoiceService.create( lPOInvoiceDetailsTO, lPurchaseOrder, iHr );

      // then
      assertInvoiceCreatedForOrder( lPurchaseInvoice, lPurchaseOrderLine );
   }


   /**
    * Create a purchase order with default values, the given status and issued state (revision
    * number of 0 means the order has not been issued before).
    *
    * @return the purchase order
    */
   private PurchaseOrderKey createPurchaseOrder( RefEventStatusKey aStatus, boolean aIsIssued ) {
      return new OrderBuilder().withOrderType( RefPoTypeKey.PURCHASE ).withVendor( iVendor )
            .usingCurrency( iDefaultCurrency ).withExchangeRate( EXCHANGE_RATE )
            .withStatus( aStatus ).withRevisionNumber( aIsIssued ? 1 : 0 ).build();
   }


   /**
    * Create an issued purchase order with default values.
    *
    * @return the purchase order
    */
   private PurchaseOrderKey createIssuedPurchaseOrder() {
      return createPurchaseOrder( RefEventStatusKey.POISSUED, true );
   }


   /**
    *
    * Add an order line with default values to the given purchase order.
    *
    * @param aPurchaseOrder
    * @return the order line
    */
   private PurchaseOrderLineKey addOrderLine( PurchaseOrderKey aPurchaseOrder ) {
      return new OrderLineBuilder( aPurchaseOrder ).withLineType( RefPoLineTypeKey.PURCHASE )
            .forPart( iPart ).withAccount( iAssetAccount ).withOrderQuantity( QUANTITY )
            .withUnitPrice( UNIT_PRICE ).withUnitType( UNIT_QTY ).withLinePrice( LINE_PRICE )
            .withUnitType( UNIT_QTY ).withLineDescription( LINE_DESC ).build();
   }


   /**
    * Create a POInvoiceDetailsTO object with default values.
    *
    * @return
    * @throws MandatoryArgumentException
    * @throws NegativeValueException
    * @throws StringTooLongException
    */
   private POInvoiceDetailsTO createPOInvoiceDetailsTO()
         throws MandatoryArgumentException, NegativeValueException, StringTooLongException {
      POInvoiceDetailsTO lPOInvoiceDetailsTO = new POInvoiceDetailsTO();
      lPOInvoiceDetailsTO.setPOInvoiceNumber( INVOICE_NUMBER );
      lPOInvoiceDetailsTO.setPOInvoiceDate( INVOICE_DATE );
      lPOInvoiceDetailsTO.setVendor( VENDOR );
      lPOInvoiceDetailsTO.setCurrency( iDefaultCurrency );
      lPOInvoiceDetailsTO.setExchangeRate( EXCHANGE_RATE );
      lPOInvoiceDetailsTO.setPOInvoiceContact( USERNAME_TESTUSER );
      lPOInvoiceDetailsTO.setCashDiscount( CASH_DISCOUNT );
      lPOInvoiceDetailsTO.setDiscountExpiryDate( CASH_DISCOUNT_DATE );
      lPOInvoiceDetailsTO.setNote( INVOICE_NOTE );
      lPOInvoiceDetailsTO.setVendorAccount( iVendorAccount );
      lPOInvoiceDetailsTO.setTermsConditions( iTermsConditions );

      return lPOInvoiceDetailsTO;
   }


   /**
    * Create an invoice in the system for the given order line.
    *
    * @return the invoice line
    */
   private PurchaseInvoiceKey createExistingInvoice( String aInvoiceNumber,
         PurchaseOrderLineKey aPurchaseOrderLine ) {

      PurchaseInvoiceKey lPurchaseInvoice = Domain.createInvoice( invoice -> {
         invoice.setInvoiceNumber( aInvoiceNumber );
         invoice.setVendor( iVendor );
         invoice.setExchangeQty( BigDecimal.ONE );
         invoice.setCurrency( iDefaultCurrency );
         invoice.setInvoiceStatus( RefEventStatusKey.PITOBEPAID );
      } );

      new InvoiceLineBuilder( lPurchaseInvoice ).forPart( iPart ).withAccount( iAssetAccount )
            .withInvoiceQuantity( QUANTITY ).withLinePrice( LINE_PRICE )
            .withUnitType( RefQtyUnitKey.EA ).mapToOrderLine( aPurchaseOrderLine ).build();

      return lPurchaseInvoice;
   }


   /**
    * Assert the invoice was created in the system with the default values and no invoice lines.
    *
    * @param aPurchaseInvoice
    */
   private void assertInvoiceCreated( PurchaseInvoiceKey aPurchaseInvoice ) {

      assertInvoiceDetails( aPurchaseInvoice );

      // and assert no invoice lines were created
      Set<PurchaseInvoiceLineKey> lInvoiceLines =
            PoInvoiceLine.getPOInvoiceLines( aPurchaseInvoice );
      assertEquals( 0, lInvoiceLines.size() );
   }


   /**
    * Assert the invoice was created in the system with the default values for the given purchase
    * order line.
    *
    * @param aPurchaseInvoice
    * @param aPurchaseOrderLine
    */
   private void assertInvoiceCreatedForOrder( PurchaseInvoiceKey aPurchaseInvoice,
         PurchaseOrderLineKey aPurchaseOrderLine ) {

      assertInvoiceDetails( aPurchaseInvoice );

      Set<PurchaseInvoiceLineKey> lPurchaseInvoiceLines =
            PoInvoiceLine.getPOInvoiceLines( aPurchaseInvoice );
      assertEquals( 1, lPurchaseInvoiceLines.size() );
      for ( PurchaseInvoiceLineKey lPurchaseInvoiceLine : lPurchaseInvoiceLines ) {

         // compare the order line values to the invoice line values
         PoInvoiceLine lInvoiceLine = PoInvoiceLine.findByPrimaryKey( lPurchaseInvoiceLine );
         POLineTable lOrderLine = POLineTable.findByPrimaryKey( aPurchaseOrderLine );

         assertEquals( "1", lInvoiceLine.getLineNoOrd() );
         assertEquals( lOrderLine.getPartNo(), lInvoiceLine.getPartNo() );
         assertEquals( lOrderLine.getAccount(), lInvoiceLine.getAccount() );
         assertEquals( lOrderLine.getOrderQt(), lInvoiceLine.getInvoiceQt() );
         assertEquals( lOrderLine.getUnitPrice(), lInvoiceLine.getUnitPrice() );
         assertEquals( lOrderLine.getQtyUnit(), lInvoiceLine.getQtyUnit() );
         assertEquals( lOrderLine.getLinePrice(), lInvoiceLine.getLinePrice() );
         assertEquals( lOrderLine.getLineLdesc(), lInvoiceLine.getLineLdesc() );
         assertEquals( null, lInvoiceLine.getInvoiceLineNote() );
         assertEquals( false, lInvoiceLine.getCsgnBool() );
      }

   }


   /**
    * Verify the invoice details.
    *
    * @param aPurchaseInvoice
    */
   private void assertInvoiceDetails( PurchaseInvoiceKey aPurchaseInvoice ) {
      EvtEventTable lEvtEvent = EvtEventTable.findByPrimaryKey( aPurchaseInvoice.getEventKey() );
      assertEquals( INVOICE_NUMBER, lEvtEvent.getEventSdesc() );

      PoInvoice lPoInvoice = PoInvoice.findByPrimaryKey( aPurchaseInvoice );
      assertEquals( iVendor, lPoInvoice.getVendor() );
      assertEquals( EXCHANGE_RATE, lPoInvoice.getExchangeQt() );
      assertEquals( iDefaultCurrency, lPoInvoice.getCurrency() );
      assertEquals( iHr, lPoInvoice.getContactHr() );
      assertEquals( CASH_DISCOUNT, lPoInvoice.getCashDiscountPct() );
      assertEquals( INVOICE_NOTE, lPoInvoice.getInvoiceNote() );
      assertEquals( iVendorAccount, lPoInvoice.getVendorAccount() );
      assertEquals( iTermsConditions, lPoInvoice.getTermsConditions() );

      // compare dates down to the minute
      assertTrue(
            DateUtils.truncatedEquals( INVOICE_DATE, lPoInvoice.getInvoiceDt(), Calendar.SECOND ) );
      assertTrue( DateUtils.truncatedEquals( CASH_DISCOUNT_DATE, lPoInvoice.getCashDiscountExpDt(),
            Calendar.SECOND ) );
   }

}
