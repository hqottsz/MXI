package com.mxi.mx.web.query.pi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.AccountBuilder;
import com.mxi.am.domain.builder.CurrencyBuilder;
import com.mxi.am.domain.builder.InvoiceLineBuilder;
import com.mxi.am.domain.builder.OrderBuilder;
import com.mxi.am.domain.builder.OrderLineBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.VendorBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
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
import com.mxi.mx.core.table.po.POLineTable;


/**
 * This class tests the MapPOInvoiceLine query which populates the MapPOInvoiceLine JSP page.
 */

public final class MapPOInvoiceLineTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static final String ORDER_NUMBER = "ORDER123";
   private static final BigDecimal EXCHANGE_RATE = BigDecimal.ONE;
   private static final BigDecimal QUANTITY = BigDecimal.TEN;
   private static final RefQtyUnitKey UNIT_QTY = RefQtyUnitKey.EA;
   private static final BigDecimal UNIT_PRICE = new BigDecimal( "35.97" );
   private static final BigDecimal LINE_PRICE = new BigDecimal( "359.7" );
   private static final String LINE_DESC = "line description";
   private static final String INVOICE_NUMBER = "INV-TEST-01";
   private static final Date ISSUED_DATE = new Date();

   private VendorKey iVendor;
   private RefCurrencyKey iDefaultCurrency;
   private FncAccountKey iAssetAccount;
   private PartNoKey iPart;


   @Before
   public void setUp() {
      // create the default currency as USD
      iDefaultCurrency = new CurrencyBuilder( "USD" ).isDefault().build();

      // create a vendor
      iVendor = new VendorBuilder().withCode( "TESTVENDOR" ).build();

      // create an asset account
      iAssetAccount =
            new AccountBuilder().withType( RefAccountTypeKey.INVASSET ).isDefault().build();

      // create a part
      iPart =
            new PartNoBuilder().withOemPartNo( "STD_PART" ).withShortDescription( "Standard Part" )
                  .withInventoryClass( RefInvClassKey.BATCH ).withDefaultPartGroup()
                  .withAssetAccount( iAssetAccount ).withFinancialType( RefFinanceTypeKey.CONSUM )
                  .withStatus( RefPartStatusKey.ACTV ).withUnitType( UNIT_QTY )
                  .withTotalQuantity( new BigDecimal( 0 ) ).withTotalValue( new BigDecimal( 0 ) )
                  .withAverageUnitPrice( new BigDecimal( 510.37 ) ).build();

   }


   /**
    * Happy path: the query returns a matching order line for the given invoice line
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testGivenMatchingOrderLineThenOrderLineIsReturned() {

      // given
      PurchaseInvoiceLineKey lInvoiceLine = createInvoiceLine();
      createMatchingOrderLine( ORDER_NUMBER );

      // when
      QuerySet lResults = executeQuery( lInvoiceLine, ORDER_NUMBER );

      // then
      validateRowReturned( lResults );
   }


   /**
    * The query should not return a row when the order line has a different part than the invoice
    * line.
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testGivenPartMismatchThenNoOrderLineIsReturned() {

      // given
      PurchaseInvoiceLineKey lInvoiceLine = createInvoiceLine();

      // an order line with a different part
      PurchaseOrderLineKey lOrderLine = createMatchingOrderLine( ORDER_NUMBER );
      PartNoKey lPart = new PartNoBuilder().withOemPartNo( "STD_PART_2" )
            .withShortDescription( "Standard Part 2" ).withInventoryClass( RefInvClassKey.BATCH )
            .withDefaultPartGroup().withAssetAccount( iAssetAccount )
            .withFinancialType( RefFinanceTypeKey.CONSUM ).withStatus( RefPartStatusKey.ACTV )
            .withUnitType( UNIT_QTY ).withTotalQuantity( new BigDecimal( 0 ) )
            .withTotalValue( new BigDecimal( 0 ) ).withAverageUnitPrice( new BigDecimal( 510.37 ) )
            .build();
      updatePart( lOrderLine, lPart );

      // when
      QuerySet lResults = executeQuery( lInvoiceLine, ORDER_NUMBER );

      // then
      validateNoRowsReturned( lResults );
   }


   /**
    * The query should not return a row when the order line belongs to an order which is open and
    * has never been issued.
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testGivenOpenPurchaseOrderThenNoOrderLineIsReturned() {

      // given
      PurchaseInvoiceLineKey lInvoiceLine = createInvoiceLine();
      createMatchingOrderLine( ORDER_NUMBER, RefEventStatusKey.POOPEN, false );

      // when
      QuerySet lResults = executeQuery( lInvoiceLine, ORDER_NUMBER );

      // then
      validateNoRowsReturned( lResults );
   }


   /**
    * The query should return a row when the order line belongs to an order which is open but has
    * been previously issued.
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testGivenOpenPurchaseOrderThatWasIssuedThenOrderLineIsReturned() {

      // given
      PurchaseInvoiceLineKey lInvoiceLine = createInvoiceLine();
      createMatchingOrderLine( ORDER_NUMBER, RefEventStatusKey.POOPEN, true );

      // when
      QuerySet lResults = executeQuery( lInvoiceLine, ORDER_NUMBER );

      // then
      validateRowReturned( lResults );
   }


   /**
    * Create an invoice with one line.
    *
    * @return the invoice line
    *
    */
   private PurchaseInvoiceLineKey createInvoiceLine() {
      PurchaseInvoiceKey lPurchaseInvoice = Domain.createInvoice( invoice -> {
         invoice.setInvoiceNumber( INVOICE_NUMBER );
         invoice.setVendor( iVendor );
         invoice.setExchangeQty( BigDecimal.ONE );
         invoice.setCurrency( iDefaultCurrency );
         invoice.setInvoiceStatus( RefEventStatusKey.PIOPEN );
      } );

      return new InvoiceLineBuilder( lPurchaseInvoice ).forPart( iPart )
            .withAccount( iAssetAccount ).withInvoiceQuantity( QUANTITY )
            .withLinePrice( LINE_PRICE ).withUnitType( UNIT_QTY ).build();
   }


   /**
    * Create an order line to match the invoice line. The associated order should have the given
    * status and issued state.
    *
    * @return the order line
    *
    */
   private PurchaseOrderLineKey createMatchingOrderLine( String aOrderNumber,
         RefEventStatusKey aStatus, boolean aIsIssued ) {

      PurchaseOrderKey lPurchaseOrder = new OrderBuilder().withDescription( aOrderNumber )
            .withOrderType( RefPoTypeKey.PURCHASE ).withVendor( iVendor ).withBroker( iVendor )
            .usingCurrency( iDefaultCurrency ).withExchangeRate( EXCHANGE_RATE )
            .withStatus( aStatus ).withRevisionNumber( aIsIssued ? 1 : 0 )
            .withIssueDate( aIsIssued ? ISSUED_DATE : null ).build();
      return new OrderLineBuilder( lPurchaseOrder ).withLineType( RefPoLineTypeKey.PURCHASE )
            .forPart( iPart ).withAccount( iAssetAccount ).withOrderQuantity( QUANTITY )
            .withUnitPrice( UNIT_PRICE ).withUnitType( UNIT_QTY ).withLinePrice( LINE_PRICE )
            .withUnitType( UNIT_QTY ).withLineDescription( LINE_DESC ).build();
   }


   /**
    * Create an order line to match the invoice line.
    *
    * @return
    *
    */
   private PurchaseOrderLineKey createMatchingOrderLine( String aOrderNumber ) {
      return createMatchingOrderLine( aOrderNumber, RefEventStatusKey.POISSUED, true );
   }


   /**
    * Update the part of the given order line.
    *
    * @param aOrderLine
    * @param aPart
    */
   private void updatePart( PurchaseOrderLineKey aOrderLine, PartNoKey aPart ) {
      POLineTable lPOLine = POLineTable.findByPrimaryKey( aOrderLine );
      lPOLine.setPartNo( aPart );
      lPOLine.update();
   }


   /**
    * Execute the query with the same name as the class.
    *
    * @param aInvoiceLine
    * @param aOrderNumber
    */
   private QuerySet executeQuery( PurchaseInvoiceLineKey aInvoiceLine, String aOrderNumber ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aInvoiceLine, "aPOInvoiceDbId", "aPOInvoiceId", "aPOInvoiceLineId" );
      lArgs.add( "aPONumber", aOrderNumber );

      return QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Validate no rows were returned by the query.
    *
    * @param aResults
    *           the query results
    */
   private void validateNoRowsReturned( QuerySet aResults ) {
      assertEquals( 0, aResults.getRowCount() );
   }


   /**
    * Validate a row was returned by the query.
    *
    * @param aResults
    *           the query results
    */
   private void validateRowReturned( QuerySet aResults ) {

      assertEquals( 1, aResults.getRowCount() );

      while ( aResults.next() ) {
         assertEquals( ORDER_NUMBER, aResults.getString( "po_number" ) );
         assertEquals( "1", aResults.getString( "line_no_ord" ) );
         assertEquals( LINE_DESC, aResults.getString( "line_ldesc" ) );
         assertEquals( 0, QUANTITY.compareTo( aResults.getBigDecimal( "order_qt" ) ) );
         assertTrue( DateUtils.truncatedEquals( ISSUED_DATE, aResults.getDate( "issued_dt" ),
               Calendar.SECOND ) );
      }
   }
}
