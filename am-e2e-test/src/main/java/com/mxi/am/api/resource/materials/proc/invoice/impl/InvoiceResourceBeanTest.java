package com.mxi.am.api.resource.materials.proc.invoice.impl;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.mxi.am.api.helper.Credentials;
import com.mxi.am.api.resource.ObjectMapperFactory;
import com.mxi.am.api.resource.materials.proc.invoice.Invoice;
import com.mxi.am.api.resource.materials.proc.invoice.InvoiceLine;
import com.mxi.am.api.resource.materials.proc.invoice.InvoiceSearchParameters;
import com.mxi.am.api.resource.materials.proc.purchaseorder.PurchaseOrder;
import com.mxi.am.api.resource.materials.proc.purchaseorder.PurchaseOrderLine;
import com.mxi.am.api.resource.materials.proc.purchaseorder.PurchaseOrderLine.LineType;
import com.mxi.am.api.resource.materials.proc.vendor.Vendor;
import com.mxi.am.api.resource.materials.proc.vendor.Vendor.VendorOrgApproval.Approval;
import com.mxi.am.driver.common.database.DatabaseDriver;
import com.mxi.am.driver.common.database.Result;
import com.mxi.am.guice.AssetManagementDatabaseDriverProvider;

import io.restassured.RestAssured;
import io.restassured.response.Response;


/**
 * Invoice API Test
 *
 */
public class InvoiceResourceBeanTest {

   private static final String APPLICATION_JSON = "application/json";
   private static final String INVOICE_API_PATH = "/amapi/" + Invoice.PATH;
   private static final String PO_API_PATH = "/amapi/" + PurchaseOrder.PATH;
   private static final String VENDOR_API_PATH = "/amapi/" + Vendor.PATH;

   private static DatabaseDriver driver;

   private static Invoice invoice;
   private static PurchaseOrder purchaseOrder;

   private static final String INVALID_ID = "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF";
   private static final String CURRENCY_USD = "USD";
   private static final String PRIORITY_NORMAL = "NORMAL";
   private static final double EXCHANGE_RATE = 1.0;
   private static final double QUANTITY = 1.0;
   private static final String UNIT_EACH = "EA";
   private static final LineType PO_LINE_CODE = LineType.PURCHASE;
   private static final String STATUS = "OPEN";
   private static final String PO_AUTH_STATUS = "POAUTH";
   private static final String PO_ISSUED_STATUS = "POISSUED";
   private static final com.mxi.am.api.resource.materials.proc.invoice.InvoiceLine.LineType LINE_TYPE =
         com.mxi.am.api.resource.materials.proc.invoice.InvoiceLine.LineType.PART;
   private static final String VENDOR_CODE = "40004";
   private static final String HR_CODE = "1000114";
   private static final String ORG_CODE = "MXI";
   private static final String LOCATION_CODE = "AIRPORT1/DOCK";
   private static final String PART_NO_OEM = "A0000002";
   private static final String APPROVAL_CODE = "PURCHASE";
   private static final String APPROVAL_TYPE = "ORDER";
   private static final String APPROVAL_STATUS = "APPROVED";

   private static String invoiceAltId;
   private static String vendorId;
   private static String purchaseContactUserId;
   private static String partNumber;
   private static String receiptOrganizationId;
   private static String shipToLocationId;
   private static String orderNumber;
   private static String purchaseOrderLineId;
   private static String invoiceNumber = "4000" + ( int ) ( Math.random() * 10000 + 1 );
   private static String invoiceNumber2 = "3000" + ( int ) ( Math.random() * 10000 + 1 );


   @BeforeClass
   public static void setupClass() throws JsonParseException, JsonMappingException, IOException,
         ClassNotFoundException, SQLException {
      try {
         RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
         RestAssured.baseURI = "http://".concat( InetAddress.getLocalHost().getHostName() );

         RestAssured.port = 80;

      } catch ( UnknownHostException e ) {
         throw new RuntimeException( "Cannot find local host name." );
      }

      driver = new AssetManagementDatabaseDriverProvider().get();

      Result vendorResult = driver.select(
            "select org_vendor.alt_id from org_vendor where org_vendor.vendor_cd=?", VENDOR_CODE );

      if ( vendorResult.isEmpty() ) {
         fail( "Could not find the Vendor with code: " + VENDOR_CODE );
      }

      Result hrResult = driver.select(
            "select utl_user.alt_id from org_hr inner join utl_user on utl_user.user_id = org_hr.user_id where org_hr.hr_cd =?",
            HR_CODE );

      if ( hrResult.isEmpty() ) {
         fail( "Could not find the User with code: " + HR_CODE );
      }

      Result orgResult =
            driver.select( "select org_org.alt_id from org_org where org_org.org_cd =?", ORG_CODE );

      if ( orgResult.isEmpty() ) {
         fail( "Could not find the Organization with code: " + ORG_CODE );
      }

      Result locationResult = driver
            .select( "select inv_loc.alt_id from inv_loc where inv_loc.loc_cd =?", LOCATION_CODE );

      if ( locationResult.isEmpty() ) {
         fail( "Could not find the location with code: " + LOCATION_CODE );
      }

      Result partResult =
            driver.select( "select alt_id from eqp_part_no where part_no_oem = ?", PART_NO_OEM );

      if ( partResult.isEmpty() ) {
         fail( "Could not find the part with number: " + PART_NO_OEM );
      }

      vendorId = vendorResult.get( 0 ).getUuidString( "alt_id" );
      purchaseContactUserId = hrResult.get( 0 ).getUuidString( "alt_id" );
      receiptOrganizationId = orgResult.get( 0 ).getUuidString( "alt_id" );
      shipToLocationId = locationResult.get( 0 ).getUuidString( "alt_id" );
      partNumber = partResult.get( 0 ).getUuidString( "alt_id" );

      approveVendor();

      prepareData();

   }


   @Test
   public void testGetByIdSuccessReturns200()
         throws JsonParseException, JsonMappingException, IOException {
      Response response = getById( 200, Credentials.AUTHORIZED, invoiceAltId, APPLICATION_JSON,
            APPLICATION_JSON );
      Invoice invoiceActual = ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper()
            .readValue( response.getBody().asString(), Invoice.class );
      assertInvoice( invoice, invoiceActual );
   }


   @Test
   public void testGetByIdNotFoundReturns404()
         throws JsonParseException, JsonMappingException, IOException {
      Response response =
            getById( 404, Credentials.AUTHORIZED, INVALID_ID, APPLICATION_JSON, APPLICATION_JSON );
      Assert.assertEquals( 404, response.getStatusCode() );
   }


   @Test
   public void testGetByIdUnauthorizedAccessReturns403() {
      getById( 403, Credentials.UNAUTHORIZED, invoiceAltId, APPLICATION_JSON, APPLICATION_JSON );
   }


   @Test
   public void testGetByIdUnauthenticatedAccessReturns401() {
      getById( 401, Credentials.UNAUTHENTICATED, invoiceAltId, APPLICATION_JSON, APPLICATION_JSON );
   }


   @Test
   public void testSearchInvoiceByOrderIdSuccess200()
         throws JsonParseException, JsonMappingException, IOException {
      Response response =
            search( 200, Credentials.AUTHORIZED, orderNumber, APPLICATION_JSON, APPLICATION_JSON );
      List<Invoice> actualInvoice =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper()
                  .readValue( response.getBody().asString(), new TypeReference<List<Invoice>>() {
                  } );
      assertInvoice( invoice, actualInvoice.get( 0 ) );
   }


   @Test
   public void testSearchInvoiceByOrderIdUnauthorizedAccessReturns403() {
      search( 403, Credentials.UNAUTHORIZED, orderNumber, APPLICATION_JSON, APPLICATION_JSON );
   }


   @Test
   public void testSearchInvoiceByOrderIdUnauthenticatedAccessReturns401() {
      search( 401, Credentials.UNAUTHENTICATED, orderNumber, APPLICATION_JSON, APPLICATION_JSON );
   }


   @Test
   public void testCreateInvoiceSuccessReturns200()
         throws JsonParseException, JsonMappingException, IOException {
      Invoice invoiceExpected = defaultInvoiceBuilder();
      invoiceExpected.setInvoiceNumber( invoiceNumber2 );
      Response response = create( 200, Credentials.AUTHENTICATED, invoiceExpected, APPLICATION_JSON,
            APPLICATION_JSON );
      Invoice invoiceActual = ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper()
            .readValue( response.getBody().asString(), Invoice.class );
      Invoice createdInvoice = createObjectToAssert( invoiceExpected, invoiceActual );
      assertInvoice( createdInvoice, invoiceActual );
   }


   @Test
   public void testCreateInvoiceUnauthorizedAccessReturns403() {
      Invoice invoice = new Invoice();
      create( 403, Credentials.UNAUTHORIZED, invoice, APPLICATION_JSON, APPLICATION_JSON );
   }


   @Test
   public void testCreateInvoiceUnauthenticatedAccessReturns401() {
      Invoice invoice = new Invoice();
      create( 401, Credentials.UNAUTHENTICATED, invoice, APPLICATION_JSON, APPLICATION_JSON );
   }


   @Test
   public void testUpdateInvoiceSuccess200()
         throws JsonParseException, JsonMappingException, IOException {
      invoice.setInvoiceNumber( "3000" + ( int ) ( Math.random() * 1000 + 1 ) );
      Response response = update( 200, Credentials.AUTHENTICATED, invoiceAltId, invoice,
            APPLICATION_JSON, APPLICATION_JSON );
      Invoice invoiceActual = ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper()
            .readValue( response.getBody().asString(), Invoice.class );
      assertInvoice( invoice, invoiceActual );
   }


   @Test
   public void testUpdateInvoiceUnauthorizedAccessReturns403() {
      Invoice updateDetails = new Invoice();
      update( 403, Credentials.UNAUTHORIZED, invoiceAltId, updateDetails, APPLICATION_JSON,
            APPLICATION_JSON );
   }


   @Test
   public void testUpdateInvoiceUnauthenticatedAccessReturns401() {
      Invoice updateDetails = new Invoice();
      update( 401, Credentials.UNAUTHENTICATED, invoiceAltId, updateDetails, APPLICATION_JSON,
            APPLICATION_JSON );
   }


   private Response getById( int statusCode, Credentials security, String invoiceId,
         String contentType, String acceptType ) {
      String userName = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().auth().preemptive().basic( userName, password )
            .accept( acceptType ).contentType( contentType ).expect().statusCode( statusCode )
            .when().get( INVOICE_API_PATH + "/" + invoiceId );
      return response;
   }


   private static Response create( int statusCode, Credentials security, Object invoice,
         String contentType, String acceptType ) {
      String userName = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().auth().preemptive().basic( userName, password )
            .accept( acceptType ).contentType( contentType ).body( invoice ).expect()
            .statusCode( statusCode ).when().post( INVOICE_API_PATH );
      return response;
   }


   private Response update( int statusCode, Credentials security, String invoiceId, Invoice invoice,
         String contentType, String acceptType ) {
      String userName = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().auth().preemptive().basic( userName, password )
            .accept( acceptType ).contentType( contentType ).body( invoice ).expect()
            .statusCode( statusCode ).when().put( INVOICE_API_PATH + "/" + invoiceId );

      return response;
   }


   private Response search( int statusCode, Credentials security, String orderNumber,
         String contentType, String acceptType ) {
      String userName = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given()
            .queryParam( InvoiceSearchParameters.ORDER_ID_PARAM, orderNumber ).auth().preemptive()
            .basic( userName, password ).accept( acceptType ).contentType( contentType ).expect()
            .statusCode( statusCode ).when().get( INVOICE_API_PATH );
      return response;
   }


   private static void prepareData() throws JsonParseException, JsonMappingException, IOException {
      purchaseOrder = defaultPOBuilder();
      invoice = defaultInvoiceBuilder();

      // Creating and Issuing a Purchase Order

      Response poResponse = createPO( 200, Credentials.AUTHENTICATED, purchaseOrder,
            APPLICATION_JSON, APPLICATION_JSON );

      PurchaseOrder poCreated = ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY
            .createObjectMapper().readValue( poResponse.getBody().asString(), PurchaseOrder.class );

      poCreated.setStatus( PO_AUTH_STATUS );

      updatePO( 200, Credentials.AUTHENTICATED, poCreated.getId(), poCreated, APPLICATION_JSON,
            APPLICATION_JSON );

      poCreated.setStatus( PO_ISSUED_STATUS );

      updatePO( 200, Credentials.AUTHENTICATED, poCreated.getId(), poCreated, APPLICATION_JSON,
            APPLICATION_JSON );

      purchaseOrderLineId = poCreated.getPurchaseOrderLines().get( 0 ).getId();
      invoice.getInvoiceLines().get( 0 ).setPurchaseOrderLineId( purchaseOrderLineId );

      // Creating an Invoice with the issued Purchase Order

      Response invoiceResponse =
            create( 200, Credentials.AUTHENTICATED, invoice, APPLICATION_JSON, APPLICATION_JSON );

      Invoice invoiceCreated = ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY
            .createObjectMapper().readValue( invoiceResponse.getBody().asString(), Invoice.class );

      orderNumber = poCreated.getId();
      invoiceAltId = invoiceCreated.getId();

      Integer invoiceLineNumber = invoiceCreated.getInvoiceLines().get( 0 ).getLineNumber();
      String invoiceLineId = invoiceCreated.getInvoiceLines().get( 0 ).getId();
      String accountId = invoiceCreated.getInvoiceLines().get( 0 ).getAccountId();
      String lineDescription = invoiceCreated.getInvoiceLines().get( 0 ).getLineDescription();

      invoice.setId( invoiceCreated.getId() );
      invoice.getInvoiceLines().get( 0 ).setLineNumber( invoiceLineNumber );
      invoice.getInvoiceLines().get( 0 ).setAccountId( accountId );
      invoice.getInvoiceLines().get( 0 ).setLineDescription( lineDescription );
      invoice.getInvoiceLines().get( 0 ).setId( invoiceLineId );
   }


   /**
    * Creates a purchase order
    *
    */
   private static Response createPO( int statusCode, Credentials security, Object purchaseOrder,
         String contentType, String acceptType ) {
      String userName = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().auth().preemptive().basic( userName, password )
            .accept( acceptType ).contentType( contentType ).body( purchaseOrder ).expect()
            .statusCode( statusCode ).when().post( PO_API_PATH );
      return response;
   }


   /**
    * Update Purchase Order
    */
   private static void updatePO( int statusCode, Credentials security, String poId,
         PurchaseOrder poCreated, String contentType, String acceptType ) {
      String userName = security.getUserName();
      String password = security.getPassword();

      RestAssured.given().auth().preemptive().basic( userName, password ).accept( acceptType )
            .contentType( contentType ).body( poCreated ).expect().statusCode( statusCode ).when()
            .put( PO_API_PATH + "/" + poId );
   }


   /**
    * Approve a vendor
    *
    */
   private static void approveVendor()
         throws JsonParseException, JsonMappingException, IOException {
      Response vendorResponse = getVendorById( 200, Credentials.AUTHENTICATED, vendorId,
            APPLICATION_JSON, APPLICATION_JSON );
      Vendor vendor = ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper()
            .readValue( vendorResponse.getBody().asString(), Vendor.class );

      Approval vendorApproval = new Approval();
      vendorApproval.setApprovalCode( APPROVAL_CODE );
      vendorApproval.setApprovalType( APPROVAL_TYPE );
      vendorApproval.setStatus( APPROVAL_STATUS );
      vendorApproval.setApprovalExpiryDate( getApprovalExpiryDate() );

      vendor.addApproval( receiptOrganizationId, vendorApproval );

      updateVendorApproval( 200, Credentials.AUTHENTICATED, vendorId, vendor, APPLICATION_JSON,
            APPLICATION_JSON );
   }


   private static Date getApprovalExpiryDate() {
      Date date = new Date();
      Calendar calendarDate = Calendar.getInstance();
      calendarDate.setTime( date );
      calendarDate.add( Calendar.DATE, 2 );
      return calendarDate.getTime();
   }


   private static Response getVendorById( int statusCode, Credentials security, String vendorId,
         String contentType, String acceptType ) {
      String userName = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().auth().preemptive().basic( userName, password )
            .accept( acceptType ).contentType( contentType ).expect().statusCode( statusCode )
            .when().get( VENDOR_API_PATH + "/" + vendorId );
      return response;
   }


   private static void updateVendorApproval( int statusCode, Credentials security, String vendorId,
         Vendor vendor, String contentType, String acceptType ) {
      String userName = security.getUserName();
      String password = security.getPassword();

      RestAssured.given().auth().preemptive().basic( userName, password ).accept( acceptType )
            .contentType( contentType ).body( vendor ).expect().statusCode( statusCode ).when()
            .put( VENDOR_API_PATH + "/" + vendorId );
   }


   /**
    * Creates Invoice object to assert for the Create test case
    *
    */
   private Invoice createObjectToAssert( Invoice invoiceExpected, Invoice invoiceActual ) {

      invoiceExpected.getInvoiceLines().get( 0 )
            .setId( invoiceActual.getInvoiceLines().get( 0 ).getId() );
      invoiceExpected.getInvoiceLines().get( 0 )
            .setLineNumber( invoiceActual.getInvoiceLines().get( 0 ).getLineNumber() );
      invoiceExpected.getInvoiceLines().get( 0 )
            .setLineDescription( invoiceActual.getInvoiceLines().get( 0 ).getLineDescription() );
      invoiceExpected.getInvoiceLines().get( 0 )
            .setAccountId( invoiceActual.getInvoiceLines().get( 0 ).getAccountId() );
      return invoiceExpected;
   }


   private void assertInvoice( Invoice invoiceExpected, Invoice actual )
         throws JsonParseException, JsonMappingException, IOException {

      invoiceExpected.setId( actual.getId() );

      Assert.assertEquals( invoiceExpected, actual );
   }


   /**
    * Creates a default Invoice object
    *
    */
   private static Invoice defaultInvoiceBuilder() {

      Invoice invoice = new Invoice();
      invoice.setCurrencyCode( CURRENCY_USD );
      invoice.setInvoiceNumber( invoiceNumber );
      invoice.setInvoiceStatus( STATUS );
      invoice.setVendorId( vendorId );
      invoice.setExchangeRate( new BigDecimal( EXCHANGE_RATE ) );

      // set invoice line
      InvoiceLine invoiceLine = new InvoiceLine();
      invoiceLine.setLineType( LINE_TYPE );
      invoiceLine.setQuantity( new BigDecimal( QUANTITY ) );
      invoiceLine.setUnitPrice( BigDecimal.ZERO );
      invoiceLine.setPartId( partNumber );
      invoiceLine.setPurchaseOrderLineId( purchaseOrderLineId );
      invoiceLine.setQuantityUnitCode( UNIT_EACH );

      invoice.addInvoiceLine( invoiceLine );

      return invoice;
   }


   /**
    * Creates a default PurchaseOrder object
    *
    */
   private static PurchaseOrder defaultPOBuilder() {
      PurchaseOrder purchaseOrder = new PurchaseOrder();
      purchaseOrder.setCurrencyCode( CURRENCY_USD );
      purchaseOrder.setReqPriority( PRIORITY_NORMAL );
      purchaseOrder.setPurchaseContactUserId( purchaseContactUserId );
      purchaseOrder.setReceiptOrganizationId( receiptOrganizationId );
      purchaseOrder.setShipToLocationId( shipToLocationId );
      purchaseOrder.setOrderNumber( orderNumber );
      purchaseOrder.setVendorId( vendorId );
      purchaseOrder.setExchangeRate( new BigDecimal( EXCHANGE_RATE ) );

      // set order line
      PurchaseOrderLine poLine = new PurchaseOrderLine();
      poLine.setQuantity( new BigDecimal( QUANTITY ) );
      poLine.setQtyUnit( UNIT_EACH );
      poLine.setPartNoId( partNumber );
      poLine.setLineTypeCode( PO_LINE_CODE );
      poLine.setUnitPrice( BigDecimal.ZERO );

      purchaseOrder.addPurchaseOrderLine( poLine );

      return purchaseOrder;
   }

}
