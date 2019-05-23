package com.mxi.am.api.resource.materials.proc.purchaseorder.purchaseorderline;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mxi.am.api.helper.Credentials;
import com.mxi.am.api.resource.ObjectMapperFactory;
import com.mxi.am.api.resource.materials.proc.purchaseorder.PurchaseOrder;
import com.mxi.am.api.resource.materials.proc.purchaseorder.PurchaseOrderLine;
import com.mxi.am.api.resource.materials.proc.purchaseorder.PurchaseOrderLine.LineType;
import com.mxi.am.driver.common.database.DatabaseDriver;
import com.mxi.am.driver.query.LocationQueriesDriver;
import com.mxi.am.driver.query.OrganizationQueriesDriver;
import com.mxi.am.driver.query.PartQueriesDriver;
import com.mxi.am.driver.query.UserAccountQueriesDriver;
import com.mxi.am.driver.query.VendorQueriesDriver;
import com.mxi.am.driver.web.AssetManagement;
import com.mxi.am.guice.AssetManagementDatabaseDriverProvider;

import io.restassured.RestAssured;
import io.restassured.response.Response;


/**
 * Purchase Order Line API test
 *
 */
public class PurchaseOrderLineResourceBeanTest {

   private static final String CURRENCY_USD = "USD";
   private static final double EXCHANGE_RATE = 2.5;
   private static final String PRIORITY_NORMAL = "NORMAL";
   private static final double QUANTITY = 1.0;
   private static final String UNIT_EACH = "EA";
   private static final String SPEC2K_CODE_CTIRE = "CTIRE";
   private static final String PO_LINE_PRICE_TYPE_CODE = "CNFRMD";
   private static final LineType PO_LINE_CODE = LineType.PURCHASE;
   private static final Boolean DELETED_BOOL = new Boolean( false );
   private static final String NOTE_TO_VENDOR = "Test Vendor Note";
   private static final String NOTE_TO_RECEIVER = "Test Receiver Note";
   private static final String SHIP_TO_CODE = "12345";
   private static final String EXT_REFERENCE = "Test Ext Reference 01";
   private static final String STATUS = "POOPEN";
   private static final String AUTH_STATUS = "PENDING";
   private static final String AUTH_FLOW_CD = "PURCHASE";
   private static final String SHIP_TO_LOCATION_CODE = "AIRPORT1/DOCK";
   private static final String VENDOR_CODE = "20002";
   private static final String PURCHASING_CONTACT_USER = "ADMIN";
   private static final String PART_NO_OEM = "A0000002";
   private static final String MANUFACT_CODE = "11111";
   private static final String PART_NO_OEM_TO_CREATE = "A0000001";
   private static final String MANUFACT_CODE_TO_CREATE = "00001";
   private static final String ORG_CODE = "MXI";
   private static final SimpleDateFormat SIMPLE_DATE_FORMAT =
         new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss" );
   private static final String DATE_STRING = "2017-08-23 18:00:00";
   private static final Boolean DELETED_BOOL_TO_UPDATE = new Boolean( true );

   private static final String APPLICATION_JSON = "application/json";
   private static final String PURCHASE_ORDER_API_PATH = "/amapi/" + PurchaseOrder.PATH;
   private static final String AMAPI = "/amapi/";
   private final ObjectMapper objectMapper =
         ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();

   private Date promisedByDate;
   private PurchaseOrderLinePathBuilder purchaseOrderLinePathBuilder;
   private PurchaseOrder purchaseOrder;
   private PurchaseOrderLine purchaseOrderLine;
   private String purchaseOrderId;
   private String purchaseOrderLineId;
   private String purchaseContactUserId;
   private String orgId;
   private String vendorId;
   private String shipToLocId;
   private String partNoId;
   private String partNoIdToCreate;

   private DatabaseDriver driver;

   @Inject
   private LocationQueriesDriver locationQueriesDriver;

   @Inject
   private VendorQueriesDriver vendorQueriesDriver;

   @Inject
   private UserAccountQueriesDriver userAccountQueriesDriver;

   @Inject
   private PartQueriesDriver partQueriesDriver;

   @Inject
   private OrganizationQueriesDriver organizationQueriesDriver;


   @BeforeClass
   public static void setUpClass() {
      try {
         RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
         RestAssured.baseURI = "http://".concat( InetAddress.getLocalHost().getHostName() );

         RestAssured.port = 80;

      } catch ( UnknownHostException e ) {
         throw new RuntimeException( "Cannot find local host name." );
      }
   }


   @Before
   public void setUpData() throws Exception {

      driver = new AssetManagementDatabaseDriverProvider().get();
      Injector injector = Guice.createInjector( new AbstractModule() {

         @Override
         protected void configure() {
            bind( DatabaseDriver.class ).annotatedWith( AssetManagement.class )
                  .toInstance( driver );
         }

      } );
      injector.injectMembers( this );

      promisedByDate = SIMPLE_DATE_FORMAT.parse( DATE_STRING );

      shipToLocId = locationQueriesDriver.getLocationId( SHIP_TO_LOCATION_CODE );

      if ( StringUtils.isBlank( shipToLocId ) ) {
         fail( "Could not find the location with code: " + SHIP_TO_LOCATION_CODE );
      }

      vendorId = vendorQueriesDriver.getVendorId( VENDOR_CODE );

      if ( StringUtils.isBlank( vendorId ) ) {
         fail( "Could not find the vendor with code: " + VENDOR_CODE );
      }

      purchaseContactUserId = userAccountQueriesDriver.getUserAccountId( PURCHASING_CONTACT_USER );

      if ( StringUtils.isBlank( purchaseContactUserId ) ) {
         fail( "Could not find the purchasing contact: " + PURCHASING_CONTACT_USER );
      }

      partNoId = partQueriesDriver.getPartNoId( PART_NO_OEM, MANUFACT_CODE );

      if ( StringUtils.isBlank( partNoId ) ) {
         fail( "Could not find the part no: " + PART_NO_OEM + " and manufact cd: "
               + MANUFACT_CODE );
      }

      partNoIdToCreate =
            partQueriesDriver.getPartNoId( PART_NO_OEM_TO_CREATE, MANUFACT_CODE_TO_CREATE );

      if ( StringUtils.isBlank( partNoIdToCreate ) ) {
         fail( "Could not find the part no: " + PART_NO_OEM_TO_CREATE + " and manufact cd: "
               + MANUFACT_CODE_TO_CREATE );
      }

      orgId = organizationQueriesDriver.getOrganizationId( ORG_CODE );

      if ( StringUtils.isBlank( orgId ) ) {
         fail( "Could not find the organization: " + ORG_CODE );
      }

      purchaseOrderLinePathBuilder = new PurchaseOrderLinePathBuilder();
      preparePurchaseOrderData();
   }


   @Test
   public void get_200() throws IOException, ParseException {
      Response response = getPurchaseOrderLine( 200, Credentials.AUTHORIZED, purchaseOrderLineId );
      assertPurchaseOrderLine( purchaseOrderLine, response );
   }


   @Test
   public void get_401_unauthenticated() throws IOException {
      getPurchaseOrderLine( 401, Credentials.UNAUTHENTICATED, purchaseOrderLineId );
   }


   @Test
   public void get_403_unauthorized() throws IOException {
      getPurchaseOrderLine( 403, Credentials.UNAUTHORIZED, purchaseOrderLineId );
   }


   @Test
   public void create_200() throws ParseException, IOException {
      PurchaseOrderLine purchaseOrderLine =
            buildPurchaseOrderLine( purchaseOrderId, partNoIdToCreate );
      Response response =
            createPurchaseOrderLine( 200, Credentials.AUTHENTICATED, purchaseOrderLine );
      assertPurchaseOrderLine( purchaseOrderLine, response );
   }


   @Test
   public void create_401_unauthenticated() throws ParseException, IOException {
      PurchaseOrderLine purchaseOrderLine =
            buildPurchaseOrderLine( purchaseOrderId, partNoIdToCreate );
      createPurchaseOrderLine( 401, Credentials.UNAUTHENTICATED, purchaseOrderLine );
   }


   @Test
   public void create_403_unauthorized() throws ParseException, IOException {
      PurchaseOrderLine purchaseOrderLine =
            buildPurchaseOrderLine( purchaseOrderId, partNoIdToCreate );
      createPurchaseOrderLine( 403, Credentials.UNAUTHORIZED, purchaseOrderLine );
   }


   @Test
   public void update_200() throws ParseException, IOException {
      PurchaseOrderLine purchaseOrderLine = buildPurchaseOrderLine( purchaseOrderId, partNoId );
      purchaseOrderLine.setDeleted( DELETED_BOOL_TO_UPDATE );
      Response response = updatePurchaseOrderLine( 200, Credentials.AUTHENTICATED,
            purchaseOrderLineId, purchaseOrderLine );
      assertPurchaseOrderLine( purchaseOrderLine, response );
   }


   @Test
   public void update_401_unauthenticated() throws ParseException, IOException {
      PurchaseOrderLine purchaseOrderLine = new PurchaseOrderLine();
      updatePurchaseOrderLine( 401, Credentials.UNAUTHENTICATED, purchaseOrderLineId,
            purchaseOrderLine );
   }


   @Test
   public void update_403_unauthorized() throws ParseException, IOException {
      PurchaseOrderLine purchaseOrderLine = new PurchaseOrderLine();
      updatePurchaseOrderLine( 403, Credentials.UNAUTHORIZED, purchaseOrderLineId,
            purchaseOrderLine );
   }


   private Response getPurchaseOrderLine( int statusCode, Credentials security,
         String purchaseOrderLineId ) {
      String userName = security.getUserName();
      String password = security.getPassword();

      String purchaseOrderLineGetPath =
            AMAPI + purchaseOrderLinePathBuilder.get( purchaseOrderLineId ).build();

      Response response = RestAssured.given().auth().preemptive().basic( userName, password )
            .accept( APPLICATION_JSON ).contentType( APPLICATION_JSON ).expect()
            .statusCode( statusCode ).when().get( purchaseOrderLineGetPath );

      return response;
   }


   private Response createPurchaseOrderLine( int statusCode, Credentials security,
         PurchaseOrderLine purchaseOrderLine ) {
      String userName = security.getUserName();
      String password = security.getPassword();

      String purchaseOrderLineGetPath = AMAPI + purchaseOrderLinePathBuilder.post().build();

      Response response = RestAssured.given().auth().preemptive().basic( userName, password )
            .accept( APPLICATION_JSON ).contentType( APPLICATION_JSON ).body( purchaseOrderLine )
            .expect().statusCode( statusCode ).when().post( purchaseOrderLineGetPath );

      return response;
   }


   private Response updatePurchaseOrderLine( int statusCode, Credentials security,
         String purchaseOrderLineId, PurchaseOrderLine purchaseOrderLine ) {
      String userName = security.getUserName();
      String password = security.getPassword();

      String purchaseOrderLinePutPath =
            AMAPI + purchaseOrderLinePathBuilder.put( purchaseOrderLineId ).build();

      Response response = RestAssured.given().auth().preemptive().basic( userName, password )
            .accept( APPLICATION_JSON ).contentType( APPLICATION_JSON ).body( purchaseOrderLine )
            .expect().statusCode( statusCode ).when().put( purchaseOrderLinePutPath );

      return response;
   }


   private void assertPurchaseOrderLine( PurchaseOrderLine expectedPurchaseOrderLine,
         Response actualPurchaseOrderLineResponse )
         throws JsonParseException, JsonMappingException, IOException {

      PurchaseOrderLine actualPurchaseOrderLine =
            objectMapper.readValue( actualPurchaseOrderLineResponse.getBody().asString(),
                  objectMapper.getTypeFactory().constructType( PurchaseOrderLine.class ) );

      expectedPurchaseOrderLine.setId( actualPurchaseOrderLine.getId() );
      expectedPurchaseOrderLine.setLineNo( actualPurchaseOrderLine.getLineNo() );
      expectedPurchaseOrderLine.setLineDescription( actualPurchaseOrderLine.getLineDescription() );
      expectedPurchaseOrderLine.setOwnerId( actualPurchaseOrderLine.getOwnerId() );
      expectedPurchaseOrderLine.setPrice( actualPurchaseOrderLine.getPrice() );
      expectedPurchaseOrderLine
            .setFinanceAccountCode( actualPurchaseOrderLine.getFinanceAccountCode() );

      Assert.assertEquals(
            "Received Purchase Order Line is not the same as Expected Purchase Order Line for GET API",
            expectedPurchaseOrderLine, actualPurchaseOrderLine );
   }


   public void preparePurchaseOrderData() throws IOException, ParseException {

      purchaseOrder = builPurchaseOrder();
      Response purchaseOrderResponse =
            createPurchaseOrder( 200, Credentials.AUTHENTICATED, purchaseOrder );

      PurchaseOrder purchaseOrderCreated = objectMapper
            .readValue( purchaseOrderResponse.getBody().asString(), PurchaseOrder.class );

      purchaseOrderId = purchaseOrderCreated.getId();

      purchaseOrderLine = buildPurchaseOrderLine( purchaseOrderId, partNoId );
      Response purchaseOrderLineResponse =
            createPurchaseOrderLine( 200, Credentials.AUTHENTICATED, purchaseOrderLine );

      PurchaseOrder purchaseOrderLineCreated = objectMapper
            .readValue( purchaseOrderLineResponse.getBody().asString(), PurchaseOrder.class );

      purchaseOrderLineId = purchaseOrderLineCreated.getId();
   }


   private Response createPurchaseOrder( int statusCode, Credentials security,
         Object purchaseOrder ) {
      String userName = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().auth().preemptive().basic( userName, password )
            .accept( APPLICATION_JSON ).contentType( APPLICATION_JSON ).body( purchaseOrder )
            .expect().statusCode( statusCode ).when().post( PURCHASE_ORDER_API_PATH );
      return response;
   }


   private PurchaseOrder builPurchaseOrder() throws ParseException {

      PurchaseOrder purchaseOrder = new PurchaseOrder();
      purchaseOrder.setReqPriority( PRIORITY_NORMAL );
      purchaseOrder.setPurchaseContactUserId( purchaseContactUserId );
      purchaseOrder.setReceiptOrganizationId( orgId );
      purchaseOrder.setVendorId( vendorId );
      purchaseOrder.setShipToLocationId( shipToLocId );
      purchaseOrder.setCurrencyCode( CURRENCY_USD );
      purchaseOrder.setExchangeRate( new BigDecimal( EXCHANGE_RATE ) );
      purchaseOrder.setSpec2kCustomerCode( SPEC2K_CODE_CTIRE );
      purchaseOrder.setShipToCode( SHIP_TO_CODE );
      purchaseOrder.setNoteToReceiver( NOTE_TO_RECEIVER );
      purchaseOrder.setNoteToVendor( NOTE_TO_VENDOR );
      purchaseOrder.setOrderExternalReference( EXT_REFERENCE );
      purchaseOrder.setStatus( STATUS );
      purchaseOrder.setAuthStatus( AUTH_STATUS );
      purchaseOrder.setPoAuthFlowCd( AUTH_FLOW_CD );

      return purchaseOrder;
   }


   private PurchaseOrderLine buildPurchaseOrderLine( String purchaseOrderId, String partNoId ) {

      PurchaseOrderLine purchaseOrderLine = new PurchaseOrderLine();
      purchaseOrderLine.setPurchaseOrderId( purchaseOrderId );
      purchaseOrderLine.setPartNoId( partNoId );
      purchaseOrderLine.setPromisedByDate( promisedByDate );
      purchaseOrderLine.setQuantity( new BigDecimal( QUANTITY ) );
      purchaseOrderLine.setQtyUnit( UNIT_EACH );
      purchaseOrderLine.setDeleted( DELETED_BOOL );
      purchaseOrderLine.setLineTypeCode( PO_LINE_CODE );
      purchaseOrderLine.setPriceType( PO_LINE_PRICE_TYPE_CODE );
      purchaseOrderLine.setPartRequestIds( new ArrayList<String>() );
      purchaseOrderLine.setUnitPrice( BigDecimal.ZERO );

      return purchaseOrderLine;
   }
}
