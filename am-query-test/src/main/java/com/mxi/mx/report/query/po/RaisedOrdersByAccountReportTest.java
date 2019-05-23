
package com.mxi.mx.report.query.po;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.AccountBuilder;
import com.mxi.am.domain.builder.CurrencyBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.InvoiceLineBuilder;
import com.mxi.am.domain.builder.OrderBuilder;
import com.mxi.am.domain.builder.OrderLineBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.ShipmentDomainBuilder;
import com.mxi.am.domain.builder.ShipmentLineBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.domain.builder.VendorBuilder;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.GlobalParametersStub;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.ejb.EjbFactory;
import com.mxi.mx.common.ejb.EjbFactoryStub;
import com.mxi.mx.common.key.PrimaryKeyService;
import com.mxi.mx.common.key.PrimaryKeyServiceStub;
import com.mxi.mx.common.services.sequence.SequenceGeneratorFactory;
import com.mxi.mx.common.services.sequence.SequenceGeneratorFactoryFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PurchaseInvoiceKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.RefAccountTypeKey;
import com.mxi.mx.core.key.RefCurrencyKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefPoLineTypeKey;
import com.mxi.mx.core.key.RefPoTypeKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefShipmentTypeKey;
import com.mxi.mx.core.key.ShipmentKey;
import com.mxi.mx.core.table.ref.RefEventStatus;


/**
 * Unit tests for reportraisedorders.GetRaisedOrders package function.
 *
 * @author ydai
 */

public class RaisedOrdersByAccountReportTest {

   @Rule
   public DatabaseConnectionRule sConnection = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private QuerySet iQs;
   static RefCurrencyKey iCurrency;

   /** Field names of the raised_order_rtyp record type. */
   private static final String FIELD_PO_TYPE_CD = "po_type_cd";
   private static final String FIELD_PO_NUMBER = "po_number";
   private static final String FIELD_USER_STATUS_CD = "user_status_cd";
   private static final String FIELD_ORDER_QT = "order_qt";
   private static final String FIELD_CREATION_DT = "creation_dt";
   private static final String FIELD_VENDOR_CD = "vendor_cd";
   private static final String FIELD_VENDOR_NAME = "vendor_name";
   private static final String FIELD_PART_NO_OEM = "part_no_oem";
   private static final String FIELD_PART_NO_SDESC = "part_no_sdesc";
   private static final String FIELD_LINE_LDESC = "line_ldesc";
   private static final String FIELD_SERIAL_NO_OEM = "serial_no_oem";
   private static final String FIELD_ACCOUNT_CD = "account_cd";
   private static final String FIELD_ACCOUNT_TYPE_CD = "account_type_cd";
   private static final String FIELD_LINE_PRICE = "line_price";
   private static final String FIELD_CURRENCY_CD = "currency_cd";
   private static final String FIELD_INVOICE_NUMBER = "invoice_number";

   /** Date formatter */
   private static final DateFormat FORMATTER = new SimpleDateFormat( "yyyyMMdd" );

   /** The parameters to filter the result in the report */
   private static final String PARM_FROM_DATE = "20160102";
   private static final String PARM_TO_DATE = "20160103";
   // use account type cd as account cd because they are the same
   private static final String INVOICE_ACCOUNT_CODE = RefAccountTypeKey.INVOICE.getCd();
   private static final String EXPENSE_ACCOUNT_CODE = RefAccountTypeKey.EXPENSE.getCd();

   /** The date out of the filter date range */
   private static final String MIN_DATE = "20160101";
   private static final String MAX_DATE = "20160104";

   /** The testing and expected data */
   private static final String EXPECTED_ORDER_NUMBER = "ORDER_NUMBER_TEST";
   private static final RefEventStatusKey EXPECTED_PO_STATUS = RefEventStatusKey.POOPEN;
   private static final BigDecimal EXPECTED_ORDER_QT = BigDecimal.ONE;
   private static final String EXPECTED_VENDOR_CD = "VENDOR_CD_TEST";
   private static final String EXPECTED_VENDOR_NAME = "VENDOR_NAME_TEST";
   private static final String EXPECTED_PART_NO_OEM = "PN_TEST";
   private static final String EXPECTED_PART_NO_SDESC = "PN_NAME_TEST";
   private static final String ALTERNATE_PART_NO_OEM = "AT_PN_TEST";
   private static final String ALTERNATE_PART_NO_SDESC = "AT_PN_NAME_TEST";
   private static final String EXPECTED_LINE_LDESC = "LINE_DESC_TEST";
   private static final String EXPECTED_SERIAL_NO_OEM = "SN_TEST";
   private static final BigDecimal EXPECTED_LINE_PRICE = BigDecimal.TEN;
   private static final String EXPECTED_CURRENCY_CD = "CRC_TEST";
   private static final String EXPECTED_INVOICE_NUMBER = "INVOICE_NUMBER_TEST";


   /**
    * Test purchase order.
    */
   @Test
   public void testPurchaseOrder() throws Exception {

      createOrder( RefPoTypeKey.PURCHASE, INVOICE_ACCOUNT_CODE, PARM_FROM_DATE );

      // execute report
      execute( INVOICE_ACCOUNT_CODE );

      AssertResult( RefPoTypeKey.PURCHASE, EXPECTED_PART_NO_OEM, EXPECTED_PART_NO_SDESC,
            INVOICE_ACCOUNT_CODE, null, EXPECTED_INVOICE_NUMBER );
   }


   /**
    * Test purchase order with two lines, and one of them is removed.
    */
   @Test
   public void testPurchaseOrderWithTwoLinesAndOneRemoved() throws Exception {

      PurchaseOrderKey lOrder = createOrderWithMiscLine( RefPoTypeKey.PURCHASE, PARM_FROM_DATE );

      // create two part order lines, and one is removed
      createOrderLineBuilder( lOrder, INVOICE_ACCOUNT_CODE ).build();

      createOrderLineBuilder( lOrder,
            new PartNoBuilder().withOemPartNo( ALTERNATE_PART_NO_OEM )
                  .withShortDescription( ALTERNATE_PART_NO_SDESC ).build(),
            INVOICE_ACCOUNT_CODE ).isDeleted().build();

      // execute report
      execute( INVOICE_ACCOUNT_CODE );

      // test part line
      AssertResult( RefPoTypeKey.PURCHASE, EXPECTED_PART_NO_OEM, EXPECTED_PART_NO_SDESC,
            INVOICE_ACCOUNT_CODE, null, null );

      // execute report
      execute( EXPENSE_ACCOUNT_CODE );

      // test misc line
      AssertResult( RefPoTypeKey.PURCHASE, null, null, EXPENSE_ACCOUNT_CODE, null, null );
   }


   /**
    * Test consignment order.
    */
   @Test
   public void testConsignOrder() throws Exception {

      createOrder( RefPoTypeKey.CONSIGN, INVOICE_ACCOUNT_CODE, PARM_FROM_DATE );

      // execute report
      execute( INVOICE_ACCOUNT_CODE );

      // test part line
      AssertResult( RefPoTypeKey.CONSIGN, EXPECTED_PART_NO_OEM, EXPECTED_PART_NO_SDESC,
            INVOICE_ACCOUNT_CODE, null, null );

      // execute report
      execute( EXPENSE_ACCOUNT_CODE );

      // test misc line
      AssertResult( RefPoTypeKey.CONSIGN, null, null, EXPENSE_ACCOUNT_CODE, null, null );

   }


   /**
    * Test borrow order.
    */
   @Test
   public void testBorrowOrder() throws Exception {

      createOrder( RefPoTypeKey.BORROW, INVOICE_ACCOUNT_CODE, PARM_FROM_DATE );

      // execute report
      execute( INVOICE_ACCOUNT_CODE );

      // test part line
      AssertResult( RefPoTypeKey.BORROW, EXPECTED_PART_NO_OEM, EXPECTED_PART_NO_SDESC,
            INVOICE_ACCOUNT_CODE, null, null );

      // execute report
      execute( EXPENSE_ACCOUNT_CODE );

      // test misc line
      AssertResult( RefPoTypeKey.BORROW, null, null, EXPENSE_ACCOUNT_CODE, null, null );
   }


   /**
    * Test repair order with one outbound shipment.
    */
   @Test
   public void testRepairOrder() throws Exception {

      createRepairOrder( INVOICE_ACCOUNT_CODE, PARM_FROM_DATE );

      // execute report
      execute( INVOICE_ACCOUNT_CODE );

      // test part line
      AssertResult( RefPoTypeKey.REPAIR, EXPECTED_PART_NO_OEM, EXPECTED_PART_NO_SDESC,
            INVOICE_ACCOUNT_CODE, EXPECTED_SERIAL_NO_OEM, null );

      // execute report
      execute( EXPENSE_ACCOUNT_CODE );

      // test misc line
      AssertResult( RefPoTypeKey.REPAIR, null, null, EXPENSE_ACCOUNT_CODE, null, null );

   }


   /**
    * Test exchange order.
    */
   @Test
   public void testExchangeOrder() throws Exception {

      RefPoTypeKey lPoType = RefPoTypeKey.EXCHANGE;
      String lExpectedSerialNoOem = EXPECTED_SERIAL_NO_OEM;

      PurchaseOrderKey lOrder = createOrderWithMiscLine( lPoType, PARM_FROM_DATE );

      // create exchange part order line
      createOrderLineBuilder( lOrder, INVOICE_ACCOUNT_CODE )
            .withLineType( RefPoLineTypeKey.EXCHANGE ).build();

      ShipmentKey lShipment = new ShipmentDomainBuilder().withType( RefShipmentTypeKey.SENDXCHG )
            .withOrder( lOrder ).build();

      new ShipmentLineBuilder( lShipment ).forInventory( createInventory( lExpectedSerialNoOem ) )
            .forPart( new PartNoBuilder().withUnitType( RefQtyUnitKey.EA ).build() ).build();

      // execute report
      execute( INVOICE_ACCOUNT_CODE );

      // test part line
      AssertResult( lPoType, EXPECTED_PART_NO_OEM, EXPECTED_PART_NO_SDESC, INVOICE_ACCOUNT_CODE,
            lExpectedSerialNoOem, null );

      // execute report
      execute( EXPENSE_ACCOUNT_CODE );

      // test misc line
      AssertResult( lPoType, null, null, EXPENSE_ACCOUNT_CODE, null, null );

   }


   /**
    * Test no result return when order creation date is earlier than the from date.
    */
   @Test
   public void testCreationDateEarlierThanFromDate() throws Exception {

      createOrder( RefPoTypeKey.PURCHASE, INVOICE_ACCOUNT_CODE, MIN_DATE );

      // execute report
      execute( INVOICE_ACCOUNT_CODE );

      // report do not show data
      Assert.assertTrue( iQs.isEmpty() );
   }


   /**
    * Test no result return when order creation date is later than the to date.
    */
   @Test
   public void testCreationDateLaterThanToDate() throws Exception {

      createOrder( RefPoTypeKey.PURCHASE, INVOICE_ACCOUNT_CODE, MAX_DATE );

      // execute report
      execute( INVOICE_ACCOUNT_CODE );

      // report do not show data
      Assert.assertTrue( iQs.isEmpty() );
   }


   /**
    * Test no result return when account is different than PARM_ACCOUNT_CODE.
    */
   @Test
   public void testDifferentAccountCode() throws Exception {

      createOrder( RefPoTypeKey.PURCHASE, "XXX", PARM_FROM_DATE );

      // execute report
      execute( INVOICE_ACCOUNT_CODE );

      // report do not show data
      Assert.assertTrue( iQs.isEmpty() );
   }


   /**
    * Create an inventory.
    *
    * @param aSerialNo
    *           the serial no
    * @return the created inventory
    */
   private InventoryKey createInventory( String aSerialNo ) {
      return new InventoryBuilder().withSerialNo( aSerialNo ).build();
   }


   /**
    * Assert the result.
    *
    * @param aPoType
    *           the po type
    * @param aExpectedPartNoOem
    *           the expected part no oem
    * @param aExpectedPartNoSdesc
    *           the expected part name
    * @param aAccountCode
    *           the account code
    * @param aExpectedSerialNo
    *           the expected sn
    * @param aExpectedInvoiceNo
    *           the expected invoice no
    * @throws Exception
    *            if an error occurs
    */
   private void AssertResult( RefPoTypeKey aPoType, String aExpectedPartNoOem,
         String aExpectedPartNoSdesc, String aAccountCode, String aExpectedSerialNo,
         String aExpectedInvoiceNo ) throws Exception {

      Assert.assertEquals( 1, iQs.getRowCount() );
      // assert column values for the first row
      iQs.first();
      Assert.assertEquals( aPoType.getCd(), iQs.getString( FIELD_PO_TYPE_CD ) );
      Assert.assertEquals( EXPECTED_ORDER_NUMBER, iQs.getString( FIELD_PO_NUMBER ) );
      Assert.assertEquals( RefEventStatus.findByPrimaryKey( EXPECTED_PO_STATUS ).getUserStatusCd(),
            iQs.getString( FIELD_USER_STATUS_CD ) );
      Assert.assertEquals( EXPECTED_ORDER_QT.intValue(), iQs.getInt( FIELD_ORDER_QT ) );
      Assert.assertEquals( FORMATTER.parse( PARM_FROM_DATE ), iQs.getDate( FIELD_CREATION_DT ) );
      Assert.assertEquals( EXPECTED_VENDOR_CD, iQs.getString( FIELD_VENDOR_CD ) );
      Assert.assertEquals( EXPECTED_VENDOR_NAME, iQs.getString( FIELD_VENDOR_NAME ) );
      Assert.assertEquals( aExpectedPartNoOem, iQs.getString( FIELD_PART_NO_OEM ) );
      Assert.assertEquals( aExpectedPartNoSdesc, iQs.getString( FIELD_PART_NO_SDESC ) );
      Assert.assertEquals( EXPECTED_LINE_LDESC, iQs.getString( FIELD_LINE_LDESC ) );
      Assert.assertEquals( aExpectedSerialNo, iQs.getString( FIELD_SERIAL_NO_OEM ) );
      Assert.assertEquals( aAccountCode, iQs.getString( FIELD_ACCOUNT_CD ) );
      Assert.assertEquals( aAccountCode, iQs.getString( FIELD_ACCOUNT_TYPE_CD ) );
      Assert.assertEquals( EXPECTED_LINE_PRICE.intValue(), iQs.getInt( FIELD_LINE_PRICE ) );
      Assert.assertEquals( EXPECTED_CURRENCY_CD, iQs.getString( FIELD_CURRENCY_CD ) );
      Assert.assertEquals( aExpectedInvoiceNo, iQs.getString( FIELD_INVOICE_NUMBER ) );

   }


   /**
    * Create order.
    *
    * @param aPoType
    *           the po type
    * @param aAccountCode
    *           the account code
    * @param aCreationDate
    *           the creation date
    * @return the order key
    * @throws ParseException
    *            if date parse error occurs
    */
   private PurchaseOrderKey createOrder( RefPoTypeKey aPoType, String aAccountCode,
         String aCreationDate ) throws ParseException {

      PurchaseOrderKey lOrder = createOrderWithMiscLine( aPoType, aCreationDate );

      // create part order line
      PurchaseOrderLineKey lPurchaseOrderLineKey = createOrderLine( lOrder, aAccountCode );

      // create invoice only for order type purchase
      if ( RefPoTypeKey.PURCHASE.equals( aPoType ) ) {

         PurchaseInvoiceKey lPurchaseInvoiceKey = Domain.createInvoice( invoice -> {
            invoice.setInvoiceNumber( EXPECTED_INVOICE_NUMBER );
            invoice.setInvoiceStatus( RefEventStatusKey.PIOPEN );
         } );

         new InvoiceLineBuilder( lPurchaseInvoiceKey ).mapToOrderLine( lPurchaseOrderLineKey )
               .build();

      }

      return lOrder;
   }


   /**
    * Create order.
    *
    * @param aPoType
    *           the po type
    * @param aAccountCode
    *           the account code
    * @param aCreationDate
    *           the creation date
    * @return the order key
    * @throws ParseException
    *            if date parse error occurs
    */
   private PurchaseOrderKey createRepairOrder( String aAccountCode, String aCreationDate )
         throws ParseException {

      PurchaseOrderKey lOrder = createOrderWithMiscLine( RefPoTypeKey.REPAIR, aCreationDate );

      // create part order line
      createRepairOrderLine( lOrder, aAccountCode );

      return lOrder;
   }


   /**
    * Create order with misc line.
    *
    * @param aPoType
    *           the po type
    * @param aCreationDate
    *           the creation date
    * @return the po
    * @throws ParseException
    *            if a date parse error occurs
    */
   private PurchaseOrderKey createOrderWithMiscLine( RefPoTypeKey aPoType, String aCreationDate )
         throws ParseException {

      // build a default currency
      RefCurrencyKey lCurrency = new CurrencyBuilder( EXPECTED_CURRENCY_CD ).isDefault().build();

      PurchaseOrderKey lOrder = new OrderBuilder().withDescription( EXPECTED_ORDER_NUMBER )
            .withOrderType( aPoType ).withStatus( EXPECTED_PO_STATUS )
            .withCreationDate( FORMATTER.parse( aCreationDate ) ).withVendor( new VendorBuilder()
                  .withCode( EXPECTED_VENDOR_CD ).withName( EXPECTED_VENDOR_NAME ).build() )
            .usingCurrency( lCurrency ).build();

      // create misc line
      createMiscLine( lOrder );

      return lOrder;
   }


   /**
    * Create misc line.
    *
    * @param aOrder
    *           the order
    * @param aPartNo
    *           the part no
    * @param aAccount
    *           the account
    */
   private void createMiscLine( PurchaseOrderKey aOrder ) {

      createOrderLineBuilder( aOrder, null, EXPENSE_ACCOUNT_CODE ).build();
   }


   /**
    * Create order line.
    *
    * @param aOrder
    *           the order
    * @param aPartNo
    *           the part no
    * @param aAccount
    *           the account
    */
   private PurchaseOrderLineKey createOrderLine( PurchaseOrderKey aOrder, String aAccountCode ) {

      return createOrderLineBuilder( aOrder, aAccountCode ).build();

   }


   /**
    * Create order line builder.
    *
    * @param aOrder
    *           the order
    * @param aAccountCode
    *           the account code
    * @return the order line builder
    */
   private OrderLineBuilder createOrderLineBuilder( PurchaseOrderKey aOrder, String aAccountCode ) {

      return createOrderLineBuilder( aOrder,
            new PartNoBuilder().withOemPartNo( EXPECTED_PART_NO_OEM )
                  .withShortDescription( EXPECTED_PART_NO_SDESC ).build(),
            aAccountCode );
   }


   /**
    * Create repair order line
    *
    * @param aOrder
    *           the order
    * @param aAccountCode
    *           the account code
    */
   private void createRepairOrderLine( PurchaseOrderKey aOrder, String aAccountCode ) {

      createOrderLineBuilder( aOrder, aAccountCode ).forTask(
            new TaskBuilder().onInventory( createInventory( EXPECTED_SERIAL_NO_OEM ) ).build() )
            .withLineType( RefPoLineTypeKey.REPAIR ).build();
   }


   /**
    * Create order line builder.
    *
    * @param aOrder
    *           the order
    * @param aPartNo
    *           the part no
    * @param aAccountCode
    *           the account code
    * @return the order line builder
    */
   private OrderLineBuilder createOrderLineBuilder( PurchaseOrderKey aOrder, PartNoKey aPartNo,
         String aAccountCode ) {

      return new OrderLineBuilder( aOrder ).forPart( aPartNo )
            .withOrderQuantity( EXPECTED_ORDER_QT ).withLineDescription( EXPECTED_LINE_LDESC )
            .withLinePrice( EXPECTED_LINE_PRICE )
            .withAccount( new AccountBuilder().withCode( aAccountCode )
                  .withType( new RefAccountTypeKey( 0, aAccountCode ) ).build() );
   }


   /**
    * Execute the report function
    *
    * @throws Exception
    *            if an error occurs
    *
    */
   private void execute( String aAccountCode ) throws ParseException {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aidt_from_date", FORMATTER.parse( PARM_FROM_DATE ) );
      lArgs.add( "aidt_to_date", FORMATTER.parse( PARM_TO_DATE ) );
      lArgs.add( "aiv_account_code", aAccountCode );

      iQs = QuerySetFactory.getInstance()
            .executeQuery( "com.mxi.mx.report.query.po.GetRaisedOrdersReportQuery", lArgs );
   }


   /**
    * Set up the global parameters.
    *
    * @throws Exception
    *            If an error occurs
    */
   @Before
   public void setUp() throws Exception {

      EjbFactory.setSingleton( new EjbFactoryStub() );
      PrimaryKeyService.setSingleton( new PrimaryKeyServiceStub() );
      SequenceGeneratorFactory.setInstance( new SequenceGeneratorFactoryFake() );

      GlobalParametersStub lGlobalParams = new GlobalParametersStub( "LOGIC" );
      lGlobalParams.setBoolean( "SPEC2000_UPPERCASE_ACCOUNT_CD", false );
      lGlobalParams.setBoolean( "SPEC2000_UPPERCASE_PART_NO_OEM", false );
      lGlobalParams.setBoolean( "SPEC2000_UPPERCASE_VENDOR_CD", false );
      GlobalParameters.setInstance( lGlobalParams );

   }


   /**
    * Tears down the test case.
    *
    * @throws Exception
    *            If an error occurs
    */
   @After
   public void tearDown() throws Exception {

      EjbFactory.setSingleton( null );
      PrimaryKeyService.setSingleton( null );
      SequenceGeneratorFactory.setInstance( null );

      GlobalParameters.setInstance( "LOGIC", null );
   }

}
