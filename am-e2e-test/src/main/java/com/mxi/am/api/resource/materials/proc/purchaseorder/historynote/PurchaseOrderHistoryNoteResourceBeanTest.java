package com.mxi.am.api.resource.materials.proc.purchaseorder.historynote;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mxi.am.api.helper.Credentials;
import com.mxi.am.api.resource.ObjectMapperFactory;
import com.mxi.am.api.resource.materials.proc.purchaseorder.PurchaseOrder;
import com.mxi.am.api.resource.materials.proc.purchaseorder.PurchaseOrderLine;
import com.mxi.am.api.resource.materials.proc.purchaseorder.PurchaseOrderLine.LineType;
import com.mxi.am.driver.common.database.DatabaseDriver;
import com.mxi.am.driver.common.database.Result;
import com.mxi.am.guice.AssetManagementDatabaseDriverProvider;

import io.restassured.RestAssured;
import io.restassured.response.Response;


/**
 * Application Tests for Purchase Order History Note API
 *
 */
public class PurchaseOrderHistoryNoteResourceBeanTest {

   private static final String APPLICATION_JSON = "application/json";
   private static final String PURCHASE_ORDER_HISTORY_NOTE_PATH =
         "/amapi/" + PurchaseOrderHistoryNote.PATH;
   private static final String PURCHASE_ORDER_PATH = "/amapi/" + PurchaseOrder.PATH;

   // Purchase Order Query Info
   private static final String SHIP_TO_LOCATION_CODE = "AIRPORT1/DOCK";
   private static final String VENDOR_CODE = "20002";
   private static final String USERNAME = "ADMIN";
   private static final String PO_HISTORY_NOTE_USER = "mxi";
   private static final String PART_NO_OEM = "A0000002";
   private static final String ORG_CODE = "MXI";

   private String purchaseOrderHistoryNoteId;
   private String purchaseOrderId;
   private static final String HISTORY_NOTE = "Application Test for Purchase Order History Note";
   private static final String HISTORY_NOTE_STATUS = "POOPEN";
   private static final String CREATE_API_HISTORY_NOTE =
         "Purchase Order History Note Create API Application Test";

   private ObjectMapper objectMapper =
         ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
   private DatabaseDriver driver;

   // Purchase Order Details
   private static final String PRIORITY_NORMAL = "NORMAL";
   private static final String CURRENCY_USD = "USD";
   private static final double EXCHANGE_RATE = 2.5;
   private static final String SPEC2K_CODE_CTIRE = "CTIRE";
   private static final String NOTE_TO_VENDOR = "Test Vendor Note";
   private static final String NOTE_TO_RECEIVER = "Test Receiver Note";
   private static final String SHIP_TO_CODE = "12345";
   private static final String EXT_REFERENCE = "Test Ext Reference 01";
   private static final String STATUS = "POOPEN";
   private static final String AUTH_STATUS = "PENDING";
   private static final String AUTH_FLOW_CD = "PURCHASE";
   private static final double QUANTITY = 1.0;
   private static final String UNIT_EACH = "EA";
   private static final String PO_LINE_PRICE_TYPE_CODE = "CNFRMD";
   private static final LineType PO_LINE_CODE = LineType.PURCHASE;
   private static final Boolean DELETED_BOOL = new Boolean( false );
   private static final int PO_LINE_NO = 1;
   private static final SimpleDateFormat SIMPLE_DATE_FORMAT =
         new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss" );
   private String userId;
   private String purchaseContactUserId;
   private String orgId;
   private String vendorId;
   private String shipToLocId;
   private String partNoId;
   private String purchaseOrderNo;
   private Date promisedByDate;
   private PurchaseOrder purchaseOrder;


   @BeforeClass
   public static void setUpClass() {
      try {
         RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
         RestAssured.baseURI = "http://".concat( InetAddress.getLocalHost().getHostName() );
         RestAssured.port = 80;
      } catch ( UnknownHostException e ) {
         throw new RuntimeException( "Cannot find localhost name" );
      }
   }


   @Before
   public void setUpData() throws ClassNotFoundException, SQLException, ParseException,
         JsonParseException, JsonMappingException, IOException {

      final String hrIdQuery =
            "select utl_user.alt_id from org_hr inner join utl_user on utl_user.user_id = org_hr.user_id where utl_user.username = ?";
      final String shiptoLocQuery = "select alt_id from inv_loc where loc_cd = ?";
      final String vendorIdQuery = "select alt_id from org_vendor where vendor_cd = ?";
      final String partNoIdQuery = "select alt_id from eqp_part_no where part_no_oem = ?";
      final String ordCodeIdQuery = "select alt_id from org_org where org_cd = ?";
      final String utlUserIdQuery = "select alt_id from utl_user where username = ?";

      driver = new AssetManagementDatabaseDriverProvider().get();

      // Puchase Order Queries
      Result shipToLocationResult = driver.select( shiptoLocQuery, SHIP_TO_LOCATION_CODE );

      Result vendorCodeResult = driver.select( vendorIdQuery, VENDOR_CODE );

      Result userUsernameResult = driver.select( hrIdQuery, USERNAME );

      Result partNoResult = driver.select( partNoIdQuery, PART_NO_OEM );

      Result orgCodeResult = driver.select( ordCodeIdQuery, ORG_CODE );

      Result utlUserResult = driver.select( utlUserIdQuery, PO_HISTORY_NOTE_USER );

      if ( shipToLocationResult.isEmpty() ) {
         fail( "Could not find the location with code: " + SHIP_TO_LOCATION_CODE );
      }

      if ( shipToLocationResult.getNumberOfRows() > 1 ) {
         fail( "Expecting a single row for ship to location code but found "
               + shipToLocationResult.getNumberOfRows() );
      }

      if ( vendorCodeResult.isEmpty() ) {
         fail( "Could not find the vendor with code: " + VENDOR_CODE );
      }

      if ( vendorCodeResult.getNumberOfRows() > 1 ) {
         fail( "Expecting a single row for vendor but found "
               + vendorCodeResult.getNumberOfRows() );
      }

      if ( userUsernameResult.isEmpty() ) {
         fail( "Could not find the username: " + USERNAME );
      }

      if ( userUsernameResult.getNumberOfRows() > 1 ) {
         fail( "Expecting a single row for username but found "
               + userUsernameResult.getNumberOfRows() );
      }

      if ( partNoResult.isEmpty() ) {
         fail( "Could not find the part no: " + USERNAME );
      }

      if ( partNoResult.getNumberOfRows() > 1 ) {
         fail( "Expecting a single row for part no but found " + partNoResult.getNumberOfRows() );
      }

      if ( orgCodeResult.isEmpty() ) {
         fail( "Could not find the organization: " + ORG_CODE );
      }

      if ( orgCodeResult.getNumberOfRows() > 1 ) {
         fail( "Expecting a single row for organization but found "
               + orgCodeResult.getNumberOfRows() );
      }

      if ( utlUserResult.isEmpty() ) {
         fail( "Could not find the user: " + PO_HISTORY_NOTE_USER );
      }

      if ( utlUserResult.getNumberOfRows() > 1 ) {
         fail( "Expecting a single row for user but found " + utlUserResult.getNumberOfRows() );
      }

      shipToLocId = shipToLocationResult.get( 0 ).getUuidString( "alt_id" );
      vendorId = vendorCodeResult.get( 0 ).getUuidString( "alt_id" );
      purchaseContactUserId = userUsernameResult.get( 0 ).getUuidString( "alt_id" );
      partNoId = partNoResult.get( 0 ).getUuidString( "alt_id" );
      orgId = orgCodeResult.get( 0 ).getUuidString( "alt_id" );
      userId = utlUserResult.get( 0 ).getUuidString( "alt_id" );
      promisedByDate = SIMPLE_DATE_FORMAT.parse( "2019-08-23 18:00:00" );

      preparePurchaseOrderData();
   }


   @Test
   public void testGetPurchaseOrderHistoryNoteByIdSuccessReturns200()
         throws JsonParseException, JsonMappingException, IOException {
      Response response = getHistoryNoteById( 200, Credentials.AUTHENTICATED,
            purchaseOrderHistoryNoteId, APPLICATION_JSON, APPLICATION_JSON );
      assertPurchaseOrderHistoryNote( purchaseOrderHistoryNoteBuilder(), response );
   }


   @Test
   public void testGetPurchaseOrderHistoryNoteByIdUnauthenticatedReturns401() {
      getHistoryNoteById( 401, Credentials.UNAUTHENTICATED, purchaseOrderHistoryNoteId,
            APPLICATION_JSON, APPLICATION_JSON );
   }


   @Test
   public void testGetPurchaseOrderHistoryNoteByIdUnauthorizedReturns403() {
      getHistoryNoteById( 403, Credentials.UNAUTHORIZED, purchaseOrderHistoryNoteId,
            APPLICATION_JSON, APPLICATION_JSON );
   }


   @Test
   public void testSearchPurchaseOrderHistoryNoteSuccessReturns200()
         throws JsonParseException, JsonMappingException, IOException {
      Response actualPurchaseOrderHistoryNoteResponse = searchHistoryNote( 200,
            Credentials.AUTHENTICATED, purchaseOrderId, APPLICATION_JSON, APPLICATION_JSON );
      List<PurchaseOrderHistoryNote> actualPurchaseOrderHistoryNote =
            objectMapper.readValue( actualPurchaseOrderHistoryNoteResponse.getBody().asString(),
                  objectMapper.getTypeFactory().constructCollectionType( List.class,
                        PurchaseOrderHistoryNote.class ) );
      Assert.assertEquals(
            "Expected 2 History Notes: One for Purchase Order Creation and the other one that we created. But found "
                  + actualPurchaseOrderHistoryNote.size(),
            2, actualPurchaseOrderHistoryNote.size() );
      Assert.assertTrue(
            "Received Purchase Order History Note List does not contain the created History Note",
            actualPurchaseOrderHistoryNote.stream()
                  .anyMatch( t -> t.getHistoryNote().equals( HISTORY_NOTE ) ) );
   }


   @Test
   public void testSearchPurchaseOrderHistoryNoteUnauthenticatedReturns401() {
      searchHistoryNote( 401, Credentials.UNAUTHENTICATED, purchaseOrderId, APPLICATION_JSON,
            APPLICATION_JSON );
   }


   @Test
   public void testSearchPurchaseOrderHistoryNoteUnauthorizedReturns403() {
      searchHistoryNote( 403, Credentials.UNAUTHORIZED, purchaseOrderId, APPLICATION_JSON,
            APPLICATION_JSON );
   }


   @Test
   public void testCreatePurchaseOrderHistoryNoteSuccessReturns200()
         throws JsonParseException, JsonMappingException, IOException {
      PurchaseOrderHistoryNote purchaseOrderHistoryNote = purchaseOrderHistoryNoteBuilder();
      purchaseOrderHistoryNote.setHistoryNote( CREATE_API_HISTORY_NOTE );
      Response response = createHistoryNote( 200, Credentials.AUTHENTICATED,
            purchaseOrderHistoryNote, APPLICATION_JSON, APPLICATION_JSON );
      assertPurchaseOrderHistoryNote( purchaseOrderHistoryNote, response );
   }


   @Test
   public void testCreatePurchaseOrderHistoryNoteUnauthenticatedReturns401() {
      PurchaseOrderHistoryNote purchaseOrderHistoryNote = purchaseOrderHistoryNoteBuilder();
      purchaseOrderHistoryNote.setHistoryNote( CREATE_API_HISTORY_NOTE );
      createHistoryNote( 401, Credentials.UNAUTHENTICATED, purchaseOrderHistoryNote,
            APPLICATION_JSON, APPLICATION_JSON );
   }


   @Test
   public void testCreatePurchaseOrderHistoryNoteUnauthorizedReturns403() {
      PurchaseOrderHistoryNote purchaseOrderHistoryNote = purchaseOrderHistoryNoteBuilder();
      purchaseOrderHistoryNote.setHistoryNote( CREATE_API_HISTORY_NOTE );
      createHistoryNote( 403, Credentials.UNAUTHORIZED, purchaseOrderHistoryNote, APPLICATION_JSON,
            APPLICATION_JSON );
   }


   private Response getHistoryNoteById( int statusCode, Credentials security,
         String purchaseOrderHistoryNoteId, String contentType, String acceptType ) {

      String username = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().auth().preemptive().basic( username, password )
            .accept( acceptType ).contentType( contentType ).expect().statusCode( statusCode )
            .when().get( PURCHASE_ORDER_HISTORY_NOTE_PATH + "/" + purchaseOrderHistoryNoteId );
      return response;
   }


   private Response searchHistoryNote( int statusCode, Credentials security, String purchaseOrderId,
         String acceptType, String contentType ) {

      String username = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given()
            .queryParam( PurchaseOrderHistoryNoteSearchParameters.PARAM_PURCHASE_ORDER_ID,
                  purchaseOrderId )
            .auth().preemptive().basic( username, password ).accept( acceptType )
            .contentType( contentType ).expect().statusCode( statusCode ).when()
            .get( PURCHASE_ORDER_HISTORY_NOTE_PATH );
      return response;
   }


   private Response createHistoryNote( int statusCode, Credentials security,
         PurchaseOrderHistoryNote purchaseOrderHistoryNote, String acceptType,
         String contentType ) {

      String username = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().auth().preemptive().basic( username, password )
            .accept( acceptType ).contentType( contentType ).body( purchaseOrderHistoryNote )
            .expect().statusCode( statusCode ).when().post( PURCHASE_ORDER_HISTORY_NOTE_PATH );
      return response;
   }


   private Response createPurchaseOrder( int statusCode, Credentials security,
         PurchaseOrder purchaseOrder, String acceptType, String contentType ) {

      String username = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().auth().preemptive().basic( username, password )
            .accept( acceptType ).contentType( contentType ).body( purchaseOrder ).expect()
            .statusCode( statusCode ).when().post( PURCHASE_ORDER_PATH );
      return response;
   }


   private void assertPurchaseOrderHistoryNote(
         PurchaseOrderHistoryNote expectedPurchaseOrderHistoryNote,
         Response actualPurchaseOrderHistoryNoteResponse )
         throws JsonParseException, JsonMappingException, IOException {

      PurchaseOrderHistoryNote actualPurchaseOrderHistoryNote =
            objectMapper.readValue( actualPurchaseOrderHistoryNoteResponse.getBody().asString(),
                  objectMapper.getTypeFactory().constructType( PurchaseOrderHistoryNote.class ) );
      expectedPurchaseOrderHistoryNote.setId( actualPurchaseOrderHistoryNote.getId() );
      expectedPurchaseOrderHistoryNote.setDate( actualPurchaseOrderHistoryNote.getDate() );

      Assert.assertEquals(
            "Received Purchase Order History Note is not the same as Expected Purchase Order History Note for GET API",
            expectedPurchaseOrderHistoryNote, actualPurchaseOrderHistoryNote );
   }


   private PurchaseOrderHistoryNote purchaseOrderHistoryNoteBuilder() {
      PurchaseOrderHistoryNote purchaseOrderHistoryNote = new PurchaseOrderHistoryNote();
      purchaseOrderHistoryNote.setPurchaseOrderId( purchaseOrderId );
      purchaseOrderHistoryNote.setUserId( userId );
      purchaseOrderHistoryNote.setHistoryNote( HISTORY_NOTE );
      purchaseOrderHistoryNote.setStageStatus( HISTORY_NOTE_STATUS );
      purchaseOrderHistoryNote.setDate( new Date() );

      return purchaseOrderHistoryNote;
   }


   private PurchaseOrder purchaseOrderBuilder() {

      // Create Purchase Order. Note: We don't set PO id and PO line id because the alt_id is auto
      // generated whenever the e2e database is created and PO number will be changed when we run
      // the same test many times so better not to set
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

      // set order line
      PurchaseOrderLine poLine = new PurchaseOrderLine();
      poLine.setPartNoId( partNoId );
      poLine.setPromisedByDate( promisedByDate );
      poLine.setQuantity( new BigDecimal( QUANTITY ) );
      poLine.setQtyUnit( UNIT_EACH );
      poLine.setDeleted( DELETED_BOOL );
      poLine.setLineNo( PO_LINE_NO );
      poLine.setLineTypeCode( PO_LINE_CODE );
      poLine.setPriceType( PO_LINE_PRICE_TYPE_CODE );

      // we currently don't set unit price in the PO API
      // so we always expect to get back a unit price of 0
      poLine.setUnitPrice( BigDecimal.ZERO );

      purchaseOrder.addPurchaseOrderLine( poLine );

      return purchaseOrder;
   }


   public void preparePurchaseOrderData()
         throws JsonParseException, JsonMappingException, IOException {
      ObjectMapper objectMapper =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      // Create the Purchase Order
      purchaseOrder = purchaseOrderBuilder();
      Response purchaseOrderResponse = createPurchaseOrder( 200, Credentials.AUTHENTICATED,
            purchaseOrder, APPLICATION_JSON, APPLICATION_JSON );
      PurchaseOrder purchaseOrderCreated = objectMapper
            .readValue( purchaseOrderResponse.getBody().asString(), PurchaseOrder.class );

      // Then create the Purchase Order History Note
      purchaseOrderId = purchaseOrderCreated.getId();
      PurchaseOrderHistoryNote purchaseOrderHistoryNote = purchaseOrderHistoryNoteBuilder();
      Response purchaseOrderHistoryNoteresponse = createHistoryNote( 200, Credentials.AUTHENTICATED,
            purchaseOrderHistoryNote, APPLICATION_JSON, APPLICATION_JSON );
      PurchaseOrderHistoryNote purchaseOrderHistoryNoteCreated = objectMapper.readValue(
            purchaseOrderHistoryNoteresponse.getBody().asString(), PurchaseOrderHistoryNote.class );

      purchaseOrderHistoryNoteId = purchaseOrderHistoryNoteCreated.getId();
      purchaseOrderNo = purchaseOrderCreated.getOrderNumber();
      purchaseOrder.setId( purchaseOrderId );
      purchaseOrder.setOrderNumber( purchaseOrderNo );
      purchaseOrder.getPurchaseOrderLines().get( 0 )
            .setId( purchaseOrderCreated.getPurchaseOrderLines().get( 0 ).getId() );
   }

}
