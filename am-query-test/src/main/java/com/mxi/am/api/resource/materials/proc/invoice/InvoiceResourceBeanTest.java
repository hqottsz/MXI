package com.mxi.am.api.resource.materials.proc.invoice;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.mxi.am.api.exception.AmApiBadRequestException;
import com.mxi.am.api.exception.AmApiBusinessException;
import com.mxi.am.api.exception.AmApiInternalServerException;
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.resource.materials.proc.invoice.InvoiceLine.LineType;
import com.mxi.am.api.resource.materials.proc.invoice.impl.InvoiceResourceBean;
import com.mxi.mx.apiengine.security.Security;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.apiengine.security.CoreSecurity;
import com.mxi.mx.core.table.acevent.InvCndChgInvDao;
import com.mxi.mx.core.table.acevent.JdbcInvCndChgInvDao;
import com.mxi.mx.core.table.evt.EvtEventDao;
import com.mxi.mx.core.table.evt.EvtStageDao;
import com.mxi.mx.core.table.evt.JdbcEvtEventDao;
import com.mxi.mx.core.table.evt.JdbcEvtStageDao;
import com.mxi.mx.core.table.utl.JdbcUtlConfigParmDao;
import com.mxi.mx.core.table.utl.UtlConfigParmDao;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Query test for Invoice API
 *
 */
@RunWith( MockitoJUnitRunner.class )
public class InvoiceResourceBeanTest extends ResourceBeanTest {

   private static final String INVOICE_ID = "C8E0FF20C1B546EF98ED26370539A512";
   private static final String CURRENCY_CODE = "USD";
   private static final int INV_SEQ_NUM = 1;
   private static final BigDecimal CASH_DISCOUNT = new BigDecimal( "1.25455" );
   private static final String CASH_DISCOUNT_EXPIRY_DATE = "2007-01-25T05:00:00Z";
   private static final String VENDOR_INVOICE_NUMBER = "vendor_invoice_number";
   private static final String VENDOR_ID = "D674CEC634764C5DBED594086FD461E8";
   private static final String INVOICE_NOTE = "This PO Invoice is for unitest.";
   private static final BigDecimal EXCHANGE_RATE = new BigDecimal( "1.25" );
   private static final String INVOICE_NUMBER = "OVER_RECEIVED";
   private static final String INVOICE_STATUS = "OPEN";
   private static final String INVOICE_DATE = "2007-01-11T05:00:00Z";

   private static final String INVOICE_LINE_ID = "6AFC3290E3BD4996BC0A6AE307CB7C37";
   private static final int INVOICE_LINE_NO = 1;
   private static final String TAX_POINT_DATE = "2007-01-13T05:00:00Z";
   private static final String PURCHASE_ORDER_LINE_ID = "ECDECED02211401C83419DA39022EC5C";
   private static final String PART_ID = "739FB13909974AD69266F9A511AD65E9";
   private static final BigDecimal QUANTITY = new BigDecimal( "1" );
   private static final BigDecimal UNIT_PRICE = new BigDecimal( "0" );
   private static final String NOTE = "note";
   private static final String LINE_DESCRIPTION = "114T4104-37SP (#4 O/B L/H)";
   private static final String ACCOUNT_ID = "285FF1F7F8D54F2AB48B7AC145E398BE";
   private static final String QUANTITY_UNIT_CODE = "EA";

   private static final String CURRENCY_CODE_TO_CREATE = "USD";
   private static final String VENDOR_ID_TO_CREATE = "D674CEC634764C5DBED594086FD461E8";
   private static final BigDecimal EXCHANGE_RATE_TO_CREATE = new BigDecimal( "1" );
   private static final String INVOICE_NUMBER_TO_CREATE = "PI : Test After the Fact Receipts 1";

   private static final String INVOICE_ID_TO_UPDATE = "C8E0FF20C1B546EF98ED26370539A513";
   private static final String VENDOR_ID_TO_UPDATE = "B5C553C87BDD4191AC9F4B47C31D7752";
   private static final String CURRENCY_CODE_TO_UPDATE = "CAD";
   private static final BigDecimal EXCHANGE_RATE_TO_UPDATE = new BigDecimal( "2.4" );
   private static final String INVOICE_NUMBER_TO_UPDATE = "PI : Test After the Fact Receipts";

   private static final String ORDER_ID = "0005433564914E88B7012EBFE4C58BD7";
   private static final String ORDER_LINE_ID = "ECDECED02211401C83419DA39022EC5C";

   private static final String NON_EXISTENT_INVOICE_ID = "20000000000000000000000000000004";
   private static final String INVALID_ORDER_ID = "XXXXXX";
   private static final String INVALID_ORDER_LINE_ID = "XXXXXX";
   private static final String NON_EXISTENT_ORDER_ID = "2000000000000010000000000001000A";
   private static final String NON_EXISTENT_ORDER_LINE_ID = "5000000100000000100000000000000F";
   private static final String NON_EXISTENT_VENDOR_ID = "B674CEC634764C5DBED594086FD461A9";

   @Inject
   InvoiceResourceBean invoiceResourceBean;

   @Rule
   public InjectionOverrideRule injectionOverrideRule =
         new InjectionOverrideRule( new AbstractModule() {

            @Override
            protected void configure() {
               bind( InvoiceResource.class ).to( InvoiceResourceBean.class );
               bind( UtlConfigParmDao.class ).to( JdbcUtlConfigParmDao.class );
               bind( EvtStageDao.class ).to( JdbcEvtStageDao.class );
               bind( InvCndChgInvDao.class ).to( JdbcInvCndChgInvDao.class );
               bind( EvtEventDao.class ).to( JdbcEvtEventDao.class );
               bind( Security.class ).to( CoreSecurity.class );

            }
         } );


   @Before
   public void setUp() throws MxException {
      InjectorContainer.get().injectMembers( this );
      initializeDataLoader();
   }


   @Test
   public void testGetInvoiceByIdSuccess() throws AmApiResourceNotFoundException, ParseException {
      Invoice invoice = invoiceResourceBean.get( INVOICE_ID );
      assertInvoice( getInvoice(), invoice );
   }


   @Test( expected = AmApiResourceNotFoundException.class )
   public void testGetInvoiceByIdResourceNotFound() throws AmApiResourceNotFoundException {
      invoiceResourceBean.get( NON_EXISTENT_INVOICE_ID );
   }


   @Test( expected = AmApiResourceNotFoundException.class )
   public void testGetInvoiceByNullId() throws AmApiResourceNotFoundException {
      invoiceResourceBean.get( null );
   }


   @Test
   public void testSearchInvoiceByOrderId() throws ParseException {
      InvoiceSearchParameters invoiceSearchParameters = new InvoiceSearchParameters();
      invoiceSearchParameters.setOrderId( ORDER_ID );
      List<Invoice> invoiceList = invoiceResourceBean.search( invoiceSearchParameters );
      Assert.assertEquals( 1, invoiceList.size() );
      Assert.assertTrue( invoiceList.contains( getInvoice() ) );
   }


   @Test
   public void testSearchInvoiceByOrderLineId() throws ParseException {
      InvoiceSearchParameters invoiceSearchParameters = new InvoiceSearchParameters();
      invoiceSearchParameters.setOrderLineId( ORDER_LINE_ID );
      List<Invoice> invoiceList = invoiceResourceBean.search( invoiceSearchParameters );
      Assert.assertEquals( 1, invoiceList.size() );
      Assert.assertTrue( invoiceList.contains( getInvoice() ) );
   }


   @Test
   public void testSearchInvoiceByOrderIdAndOrderLineId() throws ParseException {
      InvoiceSearchParameters invoiceSearchParameters = new InvoiceSearchParameters();
      invoiceSearchParameters.setOrderId( ORDER_ID );
      invoiceSearchParameters.setOrderLineId( ORDER_LINE_ID );
      List<Invoice> invoiceList = invoiceResourceBean.search( invoiceSearchParameters );
      Assert.assertEquals( 1, invoiceList.size() );
      Assert.assertTrue( invoiceList.contains( getInvoice() ) );
   }


   @Test( expected = AmApiInternalServerException.class )
   public void testSearchInvoiceByInvalidOrderId() {
      InvoiceSearchParameters invoiceSearchParameters = new InvoiceSearchParameters();
      invoiceSearchParameters.setOrderLineId( INVALID_ORDER_ID );
      invoiceResourceBean.search( invoiceSearchParameters );
   }


   @Test( expected = AmApiInternalServerException.class )
   public void testSearchInvoiceByInvalidOrderLineId() {
      InvoiceSearchParameters invoiceSearchParameters = new InvoiceSearchParameters();
      invoiceSearchParameters.setOrderLineId( INVALID_ORDER_LINE_ID );
      invoiceResourceBean.search( invoiceSearchParameters );
   }


   @Test
   public void testSearchInvoiceByNonExistentOrderId() {
      InvoiceSearchParameters invoiceSearchParameters = new InvoiceSearchParameters();
      invoiceSearchParameters.setOrderLineId( NON_EXISTENT_ORDER_ID );
      List<Invoice> invoiceList = invoiceResourceBean.search( invoiceSearchParameters );
      assertTrue( CollectionUtils.isEmpty( invoiceList ) );
   }


   @Test
   public void testSearchInvoiceByNonExistentOrderLineId() {
      InvoiceSearchParameters invoiceSearchParameters = new InvoiceSearchParameters();
      invoiceSearchParameters.setOrderLineId( NON_EXISTENT_ORDER_LINE_ID );
      List<Invoice> invoiceList = invoiceResourceBean.search( invoiceSearchParameters );
      assertTrue( CollectionUtils.isEmpty( invoiceList ) );
   }


   @Test
   public void testSearchInvoiceByInvoiceNo() throws ParseException {
      InvoiceSearchParameters invoiceSearchParameters = new InvoiceSearchParameters();
      invoiceSearchParameters.setInvoiceNo( "OVER_RECEIVED" );
      List<Invoice> invoiceList = invoiceResourceBean.search( invoiceSearchParameters );
      Assert.assertTrue( invoiceList.contains( getInvoice() ) );
   }


   @Test
   public void testSearchInvoiceByVendorId() throws ParseException {
      InvoiceSearchParameters invoiceSearchParameters = new InvoiceSearchParameters();
      invoiceSearchParameters.setVendorId( VENDOR_ID );
      List<Invoice> invoiceList = invoiceResourceBean.search( invoiceSearchParameters );
      Assert.assertTrue( invoiceList.contains( getInvoice() ) );
   }


   @Test
   public void testSearchInvoiceByInvoiceNoAndVendorId() throws ParseException {
      InvoiceSearchParameters invoiceSearchParameters = new InvoiceSearchParameters();
      invoiceSearchParameters.setInvoiceNo( "OVER_RECEIVED" );
      invoiceSearchParameters.setVendorId( VENDOR_ID );
      List<Invoice> invoiceList = invoiceResourceBean.search( invoiceSearchParameters );
      Assert.assertTrue( invoiceList.contains( getInvoice() ) );
   }


   @Test
   public void testCreateInvoiceSuccess()
         throws AmApiBusinessException, AmApiResourceNotFoundException {
      Invoice invoice = new Invoice();
      invoice.setCurrencyCode( CURRENCY_CODE_TO_CREATE );
      invoice.setVendorId( VENDOR_ID_TO_CREATE );
      invoice.setExchangeRate( EXCHANGE_RATE_TO_CREATE );
      invoice.setInvoiceNumber( INVOICE_NUMBER_TO_CREATE );
      invoice.setInvoiceStatus( INVOICE_STATUS );
      List<InvoiceLine> invoiceLines = new ArrayList<>();
      invoice.setInvoiceLines( invoiceLines );
      Invoice createdInvoice = invoiceResourceBean.create( invoice );
      invoice.setId( createdInvoice.getId() );
      Assert.assertEquals( invoice, createdInvoice );
   }


   @Test( expected = AmApiInternalServerException.class )
   public void testCreateInvoiceWithNonExistentVendorId()
         throws AmApiBusinessException, AmApiResourceNotFoundException {
      Invoice invoice = new Invoice();
      invoice.setCurrencyCode( CURRENCY_CODE_TO_CREATE );
      invoice.setVendorId( NON_EXISTENT_VENDOR_ID );
      invoice.setExchangeRate( EXCHANGE_RATE_TO_CREATE );
      invoice.setInvoiceNumber( INVOICE_NUMBER_TO_CREATE );
      invoice.setInvoiceStatus( INVOICE_STATUS );
      List<InvoiceLine> invoiceLines = new ArrayList<>();
      invoice.setInvoiceLines( invoiceLines );
      invoiceResourceBean.create( invoice );
   }


   @Test
   public void testCreateInvoiceNullPayloadBadRequest() throws AmApiBusinessException {
      try {
         invoiceResourceBean.create( null );
         Assert.fail( "No bad request exception is thrown for null payload in the post operation" );
      } catch ( AmApiBadRequestException exception ) {
         Assert.assertEquals( "Missing Invoice Payload", exception.getMessage() );
      }
   }


   @Test
   public void testUpdateInvoiceSuccess()
         throws AmApiResourceNotFoundException, AmApiBusinessException, ParseException {
      Invoice invoice = new Invoice();
      invoice.setVendorId( VENDOR_ID );
      invoice.setCurrencyCode( CURRENCY_CODE_TO_UPDATE );
      invoice.setExchangeRate( EXCHANGE_RATE_TO_UPDATE );
      invoice.setInvoiceNumber( INVOICE_NUMBER_TO_UPDATE );
      Invoice updatedInvoice = invoiceResourceBean.update( INVOICE_ID_TO_UPDATE, invoice );
      invoice.setId( INVOICE_ID_TO_UPDATE );
      invoice.setInvoiceStatus( INVOICE_STATUS );
      List<InvoiceLine> invoiceLines = new ArrayList<>();
      invoice.setInvoiceLines( invoiceLines );
      assertTrue( invoice.equals( updatedInvoice ) );
   }


   @Test( expected = AmApiInternalServerException.class )
   public void testUpdateInvoiceWithNonExistentInvoiceId()
         throws AmApiResourceNotFoundException, AmApiBusinessException, ParseException {
      Invoice invoice = new Invoice();
      invoice.setVendorId( VENDOR_ID );
      invoice.setCurrencyCode( CURRENCY_CODE_TO_UPDATE );
      invoice.setExchangeRate( EXCHANGE_RATE_TO_UPDATE );
      invoice.setInvoiceNumber( INVOICE_NUMBER_TO_UPDATE );
      invoiceResourceBean.update( NON_EXISTENT_INVOICE_ID, invoice );
   }


   @Test
   public void testUpdateInvoiceNullIdBadRequest()
         throws AmApiResourceNotFoundException, AmApiBusinessException {
      try {
         Invoice invoice = new Invoice();
         invoice.setVendorId( VENDOR_ID_TO_UPDATE );
         invoice.setCurrencyCode( CURRENCY_CODE_TO_UPDATE );
         invoice.setExchangeRate( EXCHANGE_RATE_TO_UPDATE );
         invoice.setInvoiceNumber( INVOICE_NUMBER_TO_UPDATE );
         invoiceResourceBean.update( null, invoice );
         Assert.fail( "No bad request exception is thrown for null Id in the update operation" );
      } catch ( AmApiBadRequestException exception ) {
         Assert.assertEquals( "Missing the id of the Invoice to be updated",
               exception.getMessage() );
      }
   }


   @Test
   public void testUpdateInvoiceNullPayloadBadRequest()
         throws AmApiResourceNotFoundException, AmApiBusinessException {
      try {
         invoiceResourceBean.update( INVOICE_ID, null );
         Assert.fail(
               "No bad request exception is thrown for null payload in the update operation" );
      } catch ( AmApiBadRequestException exception ) {
         Assert.assertEquals( "Missing Invoice Payload", exception.getMessage() );
      }
   }


   private void assertInvoice( Invoice expectedInvoice, Invoice actualInvoice ) {
      Assert.assertEquals( expectedInvoice.getId(), actualInvoice.getId() );
      Assert.assertEquals( expectedInvoice.getCurrencyCode(), actualInvoice.getCurrencyCode() );
      Assert.assertEquals( expectedInvoice.getSequenceNumber(), actualInvoice.getSequenceNumber() );
      Assert.assertEquals( expectedInvoice.getCashDiscount(), actualInvoice.getCashDiscount() );
      Assert.assertEquals( expectedInvoice.getCashDiscountExpiryDate(),
            actualInvoice.getCashDiscountExpiryDate() );
      Assert.assertEquals( expectedInvoice.getVendorInvoiceNumber(),
            actualInvoice.getVendorInvoiceNumber() );
      Assert.assertEquals( expectedInvoice.getVendorId(), actualInvoice.getVendorId() );
      Assert.assertEquals( expectedInvoice.getNote(), actualInvoice.getNote() );
      Assert.assertEquals( expectedInvoice.getExchangeRate(), actualInvoice.getExchangeRate() );
      Assert.assertEquals( expectedInvoice.getInvoiceNumber(), actualInvoice.getInvoiceNumber() );
      Assert.assertEquals( expectedInvoice.getInvoiceStatus(), actualInvoice.getInvoiceStatus() );
      Assert.assertEquals( expectedInvoice.getInvoiceDate(), actualInvoice.getInvoiceDate() );
      Assert.assertEquals( expectedInvoice.getInvoiceLines(), actualInvoice.getInvoiceLines() );
   }


   private Invoice getInvoice() throws ParseException {
      Invoice invoice = new Invoice();
      invoice.setId( INVOICE_ID );
      invoice.setCurrencyCode( CURRENCY_CODE );
      invoice.setExchangeRate( EXCHANGE_RATE );
      invoice.setNote( INVOICE_NOTE );
      invoice.setCashDiscount( CASH_DISCOUNT );
      invoice.setVendorInvoiceNumber( VENDOR_INVOICE_NUMBER );
      invoice.setSequenceNumber( INV_SEQ_NUM );
      invoice.setInvoiceNumber( INVOICE_NUMBER );
      invoice.setVendorId( VENDOR_ID );
      invoice.setInvoiceStatus( INVOICE_STATUS );
      Date cashDiscountExpiryDate =
            new SimpleDateFormat( "yyyy-MM-dd" ).parse( CASH_DISCOUNT_EXPIRY_DATE );
      Date invoiceDate = new SimpleDateFormat( "yyyy-MM-dd" ).parse( INVOICE_DATE );
      invoice.setCashDiscountExpiryDate( cashDiscountExpiryDate );
      invoice.setInvoiceDate( invoiceDate );
      List<InvoiceLine> invoiceLines = new ArrayList<InvoiceLine>();
      InvoiceLine invoiceLine = new InvoiceLine();
      invoiceLine.setId( INVOICE_LINE_ID );
      invoiceLine.setLineNumber( INVOICE_LINE_NO );
      Date taxPointDate = new SimpleDateFormat( "yyyy-MM-dd" ).parse( TAX_POINT_DATE );
      invoiceLine.setTaxPointDate( taxPointDate );
      invoiceLine.setPurchaseOrderLineId( PURCHASE_ORDER_LINE_ID );
      invoiceLine.setLineType( LineType.PART );
      invoiceLine.setPartId( PART_ID );
      invoiceLine.setQuantity( QUANTITY );
      invoiceLine.setUnitPrice( UNIT_PRICE );
      invoiceLine.setNote( NOTE );
      invoiceLine.setLineDescription( LINE_DESCRIPTION );
      invoiceLine.setAccountId( ACCOUNT_ID );
      invoiceLine.setQuantityUnitCode( QUANTITY_UNIT_CODE );
      invoiceLines.add( invoiceLine );
      invoice.setInvoiceLines( invoiceLines );
      return invoice;
   }
}
