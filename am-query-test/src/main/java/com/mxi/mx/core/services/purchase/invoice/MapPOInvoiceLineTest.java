package com.mxi.mx.core.services.purchase.invoice;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.AccountBuilder;
import com.mxi.am.domain.builder.CurrencyBuilder;
import com.mxi.am.domain.builder.InvoiceLineBuilder;
import com.mxi.am.domain.builder.OrderBuilder;
import com.mxi.am.domain.builder.OrderLineBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.VendorBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.am.ee.OperateAsUserRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.FncAccountKey;
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
import com.mxi.mx.core.key.VendorKey;
import com.mxi.mx.core.services.fnc.MultiplePOLinesMappedException;
import com.mxi.mx.core.services.purchase.invoice.InvoiceOrderStateValidator.InvoiceOrderHasInvalidStateException;
import com.mxi.mx.core.table.po.PoInvoice;
import com.mxi.mx.core.table.po.PoInvoiceLine;
import com.mxi.mx.core.table.po.PoInvoiceLineMap;


/**
 * This class tests the
 * {@link PurchaseInvoiceService#mapPOInvoiceLine(PurchaseInvoiceLineKey, PurchaseOrderLineKey[])}
 * method
 *
 */
public class MapPOInvoiceLineTest {

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

   private VendorKey iVendor;
   private RefCurrencyKey iDefaultCurrency;
   private PartNoKey iPart;
   private FncAccountKey iAssetAccount;


   @Before
   public void loadData() throws Exception {

      // create the default currency as USD
      iDefaultCurrency = new CurrencyBuilder( "USD" ).isDefault().build();

      // create a vendor
      iVendor = new VendorBuilder().withCode( "TESTVENDOR" ).build();

      // create an asset account
      iAssetAccount =
            new AccountBuilder().withType( RefAccountTypeKey.INVASSET ).isDefault().build();

      // create a part
      iPart = new PartNoBuilder().withUnitType( RefQtyUnitKey.EA ).withOemPartNo( "STD_PART" )
            .withShortDescription( "Standard Part" ).withTotalValue( new BigDecimal( 0 ) )
            .withInventoryClass( RefInvClassKey.BATCH ).withAssetAccount( iAssetAccount )
            .withFinancialType( RefFinanceTypeKey.CONSUM ).withStatus( RefPartStatusKey.ACTV )
            .withTotalQuantity( new BigDecimal( 0 ) )
            .withAverageUnitPrice( new BigDecimal( 510.37 ) ).build();

   }


   /**
    * Test the happy path: an invoice line can be mapped to an order line.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testGivenOrderLineThenInvoiceLineIsMapped() throws Exception {

      // given
      PurchaseOrderLineKey lPOLine = createPurchaseOrderLine();
      PurchaseInvoiceLineKey lPOInvoiceLine = createInvoiceLine();

      // when
      PurchaseInvoiceService.mapPOInvoiceLine( lPOInvoiceLine,
            new PurchaseOrderLineKey[] { lPOLine } );

      // then
      assertInvoiceLineMapped( lPOInvoiceLine, lPOLine );
   }


   /**
    * Given multiple order lines for the invoice line, an exception is thrown and the invoice line
    * is not mapped.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test( expected = MultiplePOLinesMappedException.class )
   public void testGivenMultipleOrderLinesThenInvoiceLineIsNotMapped() throws Exception {

      // given
      // multiple po lines
      PurchaseOrderLineKey lFirstPOLine = createPurchaseOrderLine();
      PurchaseOrderLineKey lSecondPOLine = createPurchaseOrderLine();

      PurchaseInvoiceLineKey lPOInvoiceLine = createInvoiceLine();

      // when
      PurchaseInvoiceService.mapPOInvoiceLine( lPOInvoiceLine,
            new PurchaseOrderLineKey[] { lFirstPOLine, lSecondPOLine } );

      // then -- exception
   }


   /**
    * Given no matching order line for the invoice line (e.g. part differs), an exception is thrown
    * and the invoice line is not mapped.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test( expected = POInvoiceLinePOLineNoMatchException.class )
   public void testGivenNoMatchingOrderLineThenInvoiceLineIsNotMapped() throws Exception {

      // given
      PurchaseOrderLineKey lPOLine = createPurchaseOrderLine();

      // an invoice line with a different part
      PurchaseInvoiceLineKey lPOInvoiceLine = createInvoiceLine();
      PartNoKey lOtherPart = new PartNoBuilder().withUnitType( RefQtyUnitKey.EA )
            .withOemPartNo( "STD_PART_2" ).withShortDescription( "Standard Part 2" )
            .withTotalValue( new BigDecimal( 0 ) ).withInventoryClass( RefInvClassKey.BATCH )
            .withAssetAccount( iAssetAccount ).withFinancialType( RefFinanceTypeKey.CONSUM )
            .withStatus( RefPartStatusKey.ACTV ).withTotalQuantity( new BigDecimal( 0 ) )
            .withAverageUnitPrice( new BigDecimal( 510.37 ) ).build();
      updatePart( lPOInvoiceLine, lOtherPart );

      // when
      PurchaseInvoiceService.mapPOInvoiceLine( lPOInvoiceLine,
            new PurchaseOrderLineKey[] { lPOLine } );

      // then -- exception
   }


   /**
    * Given a charge account mismatch between the PO line and the invoice line, an exception is
    * thrown and the invoice line is not mapped.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test( expected = ChargeAccountMismatchException.class )
   public void testGivenChargeAccountMismatchThenInvoiceLineIsNotMapped() throws Exception {

      // given
      PurchaseOrderLineKey lPOLine = createPurchaseOrderLine();

      // an invoice line with a different charge account
      PurchaseInvoiceLineKey lPOInvoiceLine = createInvoiceLine();
      updateChargeAccount( lPOInvoiceLine,
            new AccountBuilder().withType( RefAccountTypeKey.EXPENSE ).build() );

      // when
      PurchaseInvoiceService.mapPOInvoiceLine( lPOInvoiceLine,
            new PurchaseOrderLineKey[] { lPOLine } );

      // then -- exception
   }


   /**
    * Given a currency mismatch between the PO line and the invoice line, an exception is thrown and
    * the invoice line is not mapped.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test( expected = CurrencyMismatchException.class )
   public void testGivenCurrencyMismatchThenInvoiceLineIsNotMapped() throws Exception {

      // given
      PurchaseOrderLineKey lPOLine = createPurchaseOrderLine();

      // an invoice line with a different currency
      PurchaseInvoiceLineKey lPOInvoiceLine = createInvoiceLine();
      updateCurrency( lPOInvoiceLine, new CurrencyBuilder( "CAD" ).build() );

      // when
      PurchaseInvoiceService.mapPOInvoiceLine( lPOInvoiceLine,
            new PurchaseOrderLineKey[] { lPOLine } );

      // then -- exception
   }


   /**
    * Given a quantity unit mismatch between the PO line and the invoice line, an exception is
    * thrown and the invoice line is not mapped.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test( expected = QtyUnitMismatchException.class )
   public void testGivenQuantityUnitMismatchThenInvoiceLineIsNotMapped() throws Exception {

      // given
      PurchaseOrderLineKey lPOLine = createPurchaseOrderLine();

      // an invoice line with a different quantity unit
      PurchaseInvoiceLineKey lPOInvoiceLine = createInvoiceLine();
      updateQuantityUnit( lPOInvoiceLine, new RefQtyUnitKey( 4650, "BOX" ) );

      // when
      PurchaseInvoiceService.mapPOInvoiceLine( lPOInvoiceLine,
            new PurchaseOrderLineKey[] { lPOLine } );

      // then -- exception
   }


   /**
    * Given a line on an open order, the invoice line is not mapped to the order line.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test( expected = InvoiceOrderHasInvalidStateException.class )
   public void testGivenOpenOrderThenInvoiceLineIsNotMapped() throws Exception {

      // given
      PurchaseOrderLineKey lPOLine = createPurchaseOrderLine( RefEventStatusKey.POOPEN, false );

      PurchaseInvoiceLineKey lPOInvoiceLine = createInvoiceLine();

      // when
      PurchaseInvoiceService.mapPOInvoiceLine( lPOInvoiceLine,
            new PurchaseOrderLineKey[] { lPOLine } );

      // then -- exception
   }


   /**
    * Given a line on a re-opened order that has been issued before, the invoice line is mapped to
    * the order line.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testGivenOpenOrderThatWasIssuedThenInvoiceLineIsMapped() throws Exception {

      // given
      PurchaseOrderLineKey lPOLine = createPurchaseOrderLine( RefEventStatusKey.POOPEN, true );

      PurchaseInvoiceLineKey lPOInvoiceLine = createInvoiceLine();

      // when
      PurchaseInvoiceService.mapPOInvoiceLine( lPOInvoiceLine,
            new PurchaseOrderLineKey[] { lPOLine } );

      // then
      assertInvoiceLineMapped( lPOInvoiceLine, lPOLine );

   }


   /**
    * Create a purchase order line for a purchase order. The purchase order has the given status and
    * issued state.
    *
    * @return the purchase order line
    */
   private PurchaseOrderLineKey createPurchaseOrderLine( RefEventStatusKey aStatus,
         boolean aIsIssued ) {
      PurchaseOrderKey lPurchaseOrder =
            new OrderBuilder().withOrderType( RefPoTypeKey.PURCHASE ).withVendor( iVendor )
                  .usingCurrency( iDefaultCurrency ).withExchangeRate( BigDecimal.ONE )
                  .withStatus( aStatus ).withRevisionNumber( aIsIssued ? 1 : 0 ).build();

      return new OrderLineBuilder( lPurchaseOrder ).withLineType( RefPoLineTypeKey.PURCHASE )
            .forPart( iPart ).withOrderQuantity( BigDecimal.TEN )
            .withUnitPrice( new BigDecimal( "35.33" ) ).withUnitType( RefQtyUnitKey.EA )
            .withAccount( iAssetAccount ).build();
   }


   /**
    * Create an purchase order line for a issued purchase order.
    *
    * @return the purchase order line
    */
   private PurchaseOrderLineKey createPurchaseOrderLine() {
      return createPurchaseOrderLine( RefEventStatusKey.POISSUED, true );
   }


   /**
    * Create an invoice line for an invoice.
    *
    * @return the invoice line
    */
   private PurchaseInvoiceLineKey createInvoiceLine() {

      PurchaseInvoiceKey lInvoice = Domain.createInvoice( invoice -> {
         invoice.setInvoiceNumber( INVOICE_NUMBER );
         invoice.setVendor( iVendor );
         invoice.setExchangeQty( BigDecimal.ONE );
         invoice.setCurrency( iDefaultCurrency );
         invoice.setInvoiceStatus( RefEventStatusKey.PIOPEN );
      } );

      return new InvoiceLineBuilder( lInvoice ).forPart( iPart ).withUnitType( RefQtyUnitKey.EA )
            .withAccount( iAssetAccount ).build();
   }


   /**
    * Update the part of the given invoice line.
    *
    * @param aInvoiceLine
    * @param aPart
    */
   private void updatePart( PurchaseInvoiceLineKey aInvoiceLine, PartNoKey aPart ) {
      PoInvoiceLine lPoInvoiceLine = PoInvoiceLine.findByPrimaryKey( aInvoiceLine );
      lPoInvoiceLine.setPartNo( aPart );
      lPoInvoiceLine.update();
   }


   /**
    * Update the charge account of the given invoice line.
    *
    * @param aInvoiceLine
    * @param aAccount
    */
   private void updateChargeAccount( PurchaseInvoiceLineKey aInvoiceLine, FncAccountKey aAccount ) {
      PoInvoiceLine lPoInvoiceLine = PoInvoiceLine.findByPrimaryKey( aInvoiceLine );
      lPoInvoiceLine.setAccount( aAccount );
      lPoInvoiceLine.update();
   }


   /**
    * Update the currency of the given invoice line.
    *
    * @param aInvoiceLine
    * @param aCurrency
    */
   private void updateCurrency( PurchaseInvoiceLineKey aInvoiceLine, RefCurrencyKey aCurrency ) {
      PoInvoice lPoInvoice = PoInvoice.findByPrimaryKey( aInvoiceLine.getPurchaseInvoiceKey() );
      lPoInvoice.setCurrency( aCurrency );
      lPoInvoice.update();
   }


   /**
    * Update the quantity unit of the given invoice line.
    *
    * @param aInvoiceLine
    * @param aQuantityUnit
    */
   private void updateQuantityUnit( PurchaseInvoiceLineKey aInvoiceLine,
         RefQtyUnitKey aQuantityUnit ) {
      PoInvoiceLine lPoInvoiceLine = PoInvoiceLine.findByPrimaryKey( aInvoiceLine );
      lPoInvoiceLine.setQtyUnit( aQuantityUnit );
      lPoInvoiceLine.update();
   }


   /**
    * Assert the invoice line was mapped to the po line.
    *
    * @param aPOLine
    * @param aPOInvoiceLine
    *
    */
   private void assertInvoiceLineMapped( PurchaseInvoiceLineKey aPOInvoiceLine,
         PurchaseOrderLineKey aPOLine ) {
      PurchaseInvoiceLineKey[] lPurchaseInvoiceLines =
            PoInvoiceLineMap.getPOInvoiceLines( aPOLine );
      assertEquals( 1, lPurchaseInvoiceLines.length );
      assertEquals( aPOInvoiceLine, lPurchaseInvoiceLines[0] );
   }

}
