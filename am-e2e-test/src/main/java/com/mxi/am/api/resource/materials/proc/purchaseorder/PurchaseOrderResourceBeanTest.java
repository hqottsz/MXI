package com.mxi.am.api.resource.materials.proc.purchaseorder;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mxi.am.api.helper.Credentials;
import com.mxi.am.api.resource.ObjectMapperFactory;
import com.mxi.am.api.resource.materials.proc.purchaseorder.PurchaseOrderLine.LineType;
import com.mxi.am.driver.common.database.DatabaseDriver;
import com.mxi.am.driver.common.database.Result;
import com.mxi.am.guice.AssetManagementDatabaseDriverProvider;

import io.restassured.RestAssured;
import io.restassured.response.Response;


/**
 * Purchase Order API test
 *
 */
public class PurchaseOrderResourceBeanTest {

   private static final String PURCHASE_ORDER_ID_NOT_EXIST = "11111111111111111111111111111111";
   private static final String CURRENCY_USD = "USD";
   private static final double EXCHANGE_RATE = 2.5;
   private static final String PRIORITY_NORMAL = "NORMAL";
   private static final double QUANTITY = 1.0;
   private static final String UNIT_EACH = "EA";
   private static final String SPEC2K_CODE_CTIRE = "CTIRE";
   private static final String PO_LINE_PRICE_TYPE_CODE = "CNFRMD";
   private static final LineType PO_LINE_CODE = LineType.PURCHASE;
   private static final int PO_LINE_NO = 1;
   private static final String LINE_DESCRIPTION = "A0000002 (2 Position Tracked Part)";
   private static final Boolean DELETED_BOOL = new Boolean( false );
   private static final String NOTE_TO_VENDOR = "Test Vendor Note";
   private static final String NOTE_TO_RECEIVER = "Test Receiver Note";
   private static final String SHIP_TO_CODE = "12345";
   private static final String EXT_REFERENCE = "Test Ext Reference 01";
   private static final String STATUS = "POOPEN";
   private static final String AUTH_STATUS = "PENDING";
   private static final String AUTH_FLOW_CD = "PURCHASE";
   private static final String UPDATED_STATUS = "POAUTH";
   private static final String PROMISED_BY_DATE_TO_UPDATE = "2007-01-11T05:00:00Z";
   private static final BigDecimal QUANTITY_TO_UPDATE = new BigDecimal( "120" );
   private static final BigDecimal UNIT_PRICE_TO_UPDATE = new BigDecimal( "100" );
   private static final String VENDOR_NOTE_TO_UPDATE = "Updated Vendor Note";
   private static final String QUANTITY_UNIT_TO_UPDATE = "BOX";
   private static final SimpleDateFormat SIMPLE_DATE_FORMAT =
         new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss" );
   private static final String APPLICATION_JSON = "application/json";
   private static final String PURCHASE_ORDER_API_PATH = "/amapi/" + PurchaseOrder.PATH;
   private final ObjectMapper objectMapper =
         ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();

   private static final BigDecimal PO_LINE_PRICE = new BigDecimal( 0 );
   private static final BigDecimal LINE_PRICE_TO_UPDATE = new BigDecimal( 12000 );

   private String purchaseOrderNo;
   private static Date promisedByDate;
   private PurchaseOrder purchaseOrder;
   private String purchaseOrderId;
   private String purchaseContactUserId;
   private String orgId;
   private String vendorId;
   private String shipToLocId;
   private String partNoId;
   private String partNoIdToUpdate;
   private String ownerId;
   private String financeAccountCd;
   private DatabaseDriver driver;


   @BeforeClass
   public static void setUpClass() {
      try {
         RestAssured.reset();
         RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
         RestAssured.baseURI = "http://".concat( InetAddress.getLocalHost().getHostName() );
         RestAssured.port = 80;

      } catch ( UnknownHostException e ) {
         throw new RuntimeException( "Cannot find local host name." );
      }

   }


   @Before
   public void setUpData() throws Exception {

      promisedByDate = SIMPLE_DATE_FORMAT.parse( "2017-08-23 18:00:00" );

      driver = new AssetManagementDatabaseDriverProvider().get();

      String shipToLocationCode = "AIRPORT1/DOCK";
      String vendorCode = "20002";
      String purchasingContactUser = "ADMIN";
      String partNoOem = "A0000002";
      String partNoOemToUpdate = "A0000001";
      String orgCode = "MXI";
      String ownerCode = "MXI";

      Result shipToLocationResult =
            driver.select( "SELECT alt_id FROM inv_loc WHERE loc_cd = ?", shipToLocationCode );

      if ( shipToLocationResult.isEmpty() ) {
         fail( "Could not find the location with code: " + shipToLocationCode );
      }

      if ( shipToLocationResult.getNumberOfRows() > 1 ) {
         fail( "Expecting a single row for ship to location code but found "
               + shipToLocationResult.getNumberOfRows() );
      }
      Result vendorCodeResult =
            driver.select( "SELECT alt_id FROM org_vendor WHERE vendor_cd = ?", vendorCode );

      if ( vendorCodeResult.isEmpty() ) {
         fail( "Could not find the vendor with code: " + vendorCode );
      }

      if ( vendorCodeResult.getNumberOfRows() > 1 ) {
         fail( "Expecting a single row for vendor but found "
               + vendorCodeResult.getNumberOfRows() );
      }

      Result purchasingContactResult = driver.select(

            "SELECT utl_user.alt_id FROM org_hr INNER JOIN utl_user ON utl_user.user_id = org_hr.user_id WHERE utl_user.username = ?",
            purchasingContactUser );

      if ( purchasingContactResult.isEmpty() ) {
         fail( "Could not find the purchasing contact: " + purchasingContactUser );
      }

      if ( purchasingContactResult.getNumberOfRows() > 1 ) {
         fail( "Expecting a single row for purchasing contact but found "
               + purchasingContactResult.getNumberOfRows() );
      }

      Result partNoResult =
            driver.select( "SELECT alt_id FROM eqp_part_no WHERE part_no_oem = ?", partNoOem );

      if ( partNoResult.isEmpty() ) {
         fail( "Could not find the part no: " + partNoOem );
      }

      if ( partNoResult.getNumberOfRows() > 1 ) {
         fail( "Expecting a single row for part no but found " + partNoResult.getNumberOfRows() );
      }

      Result partNoToUpdateResult = driver
            .select( "SELECT alt_id FROM eqp_part_no WHERE part_no_oem = ?", partNoOemToUpdate );

      if ( partNoToUpdateResult.isEmpty() ) {
         fail( "Could not find the part no: " + partNoOemToUpdate );
      }

      if ( partNoToUpdateResult.getNumberOfRows() > 1 ) {
         fail( "Expecting a single row for part no but found "
               + partNoToUpdateResult.getNumberOfRows() );
      }

      Result orgCodeResult =
            driver.select( "SELECT alt_id FROM org_org WHERE org_cd = ?", orgCode );

      if ( orgCodeResult.isEmpty() ) {
         fail( "Could not find the organization: " + orgCode );
      }

      if ( orgCodeResult.getNumberOfRows() > 1 ) {
         fail( "Expecting a single row for organization but found "
               + orgCodeResult.getNumberOfRows() );
      }

      Result ownerResult =
            driver.select( "SELECT alt_id FROM inv_owner WHERE owner_cd = ?", ownerCode );

      if ( ownerResult.isEmpty() ) {
         fail( "Could not find the owner: " + ownerCode );
      }

      if ( ownerResult.getNumberOfRows() > 1 ) {
         fail( "Expecting a single row for ownerCode but found " + ownerResult.getNumberOfRows() );
      }

      Result financeAccountResult = driver.select(
            "SELECT account_cd FROM eqp_part_no INNER JOIN fnc_account ON fnc_account.account_db_id = eqp_part_no.asset_account_db_id AND fnc_account.account_id = eqp_part_no.asset_account_id WHERE eqp_part_no.part_no_oem = ?",
            partNoOem );

      if ( financeAccountResult.isEmpty() ) {
         fail( "Could not find the finance account for part: " + partNoOem );
      }

      if ( financeAccountResult.getNumberOfRows() > 1 ) {
         fail( "Expecting a single row for finance account but found "
               + financeAccountResult.getNumberOfRows() );
      }

      shipToLocId = shipToLocationResult.get( 0 ).getUuidString( "alt_id" );
      vendorId = vendorCodeResult.get( 0 ).getUuidString( "alt_id" );
      purchaseContactUserId = purchasingContactResult.get( 0 ).getUuidString( "alt_id" );
      partNoId = partNoResult.get( 0 ).getUuidString( "alt_id" );
      partNoIdToUpdate = partNoToUpdateResult.get( 0 ).getUuidString( "alt_id" );
      orgId = orgCodeResult.get( 0 ).getUuidString( "alt_id" );
      ownerId = ownerResult.get( 0 ).getUuidString( "alt_id" );
      financeAccountCd = financeAccountResult.get( 0 ).get( "account_cd" );

      prepareData();
   }


   @Test
   public void testPurchaseOrderGetByIDSuccess() throws IOException, ParseException {
      Response response = getById( 200, Credentials.AUTHORIZED, purchaseOrderId );
      assertPurchaseOrderRetrieved( purchaseOrder, getPurchaseOrderFromResponse( response ) );
   }


   @Test
   public void testPurchaseOrderGetByIDNotFound() throws IOException, ParseException {
      getById( 404, Credentials.AUTHORIZED, PURCHASE_ORDER_ID_NOT_EXIST );
   }


   @Test
   public void testPurchaseOrderGetByIDUnauthenticatedReturns401() throws IOException {
      getById( 401, Credentials.UNAUTHENTICATED, purchaseOrderId );
   }


   @Test
   public void testPurchaseOrderGetByIDUnauthorizedReturns403() throws IOException {
      getById( 403, Credentials.UNAUTHORIZED, purchaseOrderId );
   }


   @Test
   public void testCreatePurchaseOrderSuccess() throws ParseException, IOException {
      PurchaseOrder purchaseOrder = getPurchaseOrder();
      Response response = create( 200, Credentials.AUTHENTICATED, purchaseOrder );
      assertPurchaseOrderCreated( purchaseOrder, getPurchaseOrderFromResponse( response ) );
   }


   @Test
   public void testCreatePurchaseOrderUnauthenticatedReturns401()
         throws ParseException, IOException {
      PurchaseOrder purchaseOrder = getPurchaseOrder();
      create( 401, Credentials.UNAUTHENTICATED, purchaseOrder );
   }


   @Test
   public void testCreatePurchaseOrderUnauthorizedReturns403() throws ParseException, IOException {
      PurchaseOrder purchaseOrder = getPurchaseOrder();
      create( 403, Credentials.UNAUTHORIZED, purchaseOrder );
   }


   @Test
   public void testPurchaseOrderSearchSuccessReturns200()
         throws IOException, ParseException, ClassNotFoundException, SQLException {

      Response response = searchRequest( 200, Credentials.AUTHORIZED );
      assertPurchaseOrderForSearch( response );
   }


   @Test
   public void testSearchPurchaseOrderUnauthenticatedReturns401() {
      searchRequest( 401, Credentials.UNAUTHENTICATED );
   }


   @Test
   public void testSearchPurchaseOrderUnauthorizedReturns403() {
      searchRequest( 403, Credentials.UNAUTHORIZED );
   }


   @Test
   public void testUpdatePurchaseOrderSuccess200() throws JsonParseException, JsonMappingException,
         IOException, ClassNotFoundException, SQLException, ParseException {
      // Create new Purchase Order
      PurchaseOrder newPurchaseOrder = getPurchaseOrder();
      Response createResponse = create( 200, Credentials.AUTHENTICATED, newPurchaseOrder );
      PurchaseOrder createdPurchaseOrder = getPurchaseOrderFromResponse( createResponse );
      // Update the created Purchase Order
      createdPurchaseOrder.setStatus( UPDATED_STATUS );

      Response updateResponse = updateRequest( 200, createdPurchaseOrder.getId(),
            createdPurchaseOrder, Credentials.AUTHENTICATED );
      PurchaseOrder updatedPurchaseOrder = getPurchaseOrderFromResponse( updateResponse );
      Assert.assertEquals( UPDATED_STATUS, updatedPurchaseOrder.getStatus() );
   }


   @Test
   public void testUpdatePurchaseOrderLinesSuccess200() throws ParseException, IOException {
      // Create new Purchase Order
      PurchaseOrder newPurchaseOrder = getPurchaseOrder();
      Response createResponse = create( 200, Credentials.AUTHENTICATED, newPurchaseOrder );
      PurchaseOrder createdPurchaseOrder = getPurchaseOrderFromResponse( createResponse );

      // set values to update the purchase order lines
      PurchaseOrder purchaseOrderToUpdate = new PurchaseOrder();
      List<PurchaseOrderLine> purchaseOrderLineList = new ArrayList<PurchaseOrderLine>();
      PurchaseOrderLine purchaseOrderLine = new PurchaseOrderLine();
      purchaseOrderLine.setId( createdPurchaseOrder.getPurchaseOrderLines().get( 0 ).getId() );
      purchaseOrderLine.setPartNoId( partNoIdToUpdate );
      Date promisedByDateToUpdate =
            new SimpleDateFormat( "yyyy-MM-dd" ).parse( PROMISED_BY_DATE_TO_UPDATE );
      purchaseOrderLine.setPromisedByDate( promisedByDateToUpdate );
      purchaseOrderLine.setQuantity( QUANTITY_TO_UPDATE );
      purchaseOrderLine.setUnitPrice( UNIT_PRICE_TO_UPDATE );
      purchaseOrderLine.setVendorNote( VENDOR_NOTE_TO_UPDATE );
      purchaseOrderLine.setQtyUnit( QUANTITY_UNIT_TO_UPDATE );
      purchaseOrderLine.setLineTypeCode( PO_LINE_CODE );
      purchaseOrderLine.setLineNo( PO_LINE_NO );
      purchaseOrderLine.setPrice( LINE_PRICE_TO_UPDATE );
      purchaseOrderLine.setOwnerId( ownerId );
      purchaseOrderLine.setFinanceAccountCode( financeAccountCd );
      purchaseOrderLine.setPartRequestIds( new ArrayList<String>() );
      purchaseOrderLineList.add( purchaseOrderLine );

      purchaseOrderToUpdate.setId( createdPurchaseOrder.getId() );
      purchaseOrderToUpdate.setPurchaseOrderLines( purchaseOrderLineList );
      Response updateResponse = updateRequest( 200, createdPurchaseOrder.getId(),
            purchaseOrderToUpdate, Credentials.AUTHENTICATED );
      PurchaseOrder updatedPurchaseOrder = getPurchaseOrderFromResponse( updateResponse );
      Assert.assertEquals( purchaseOrderToUpdate.getPurchaseOrderLines(),
            updatedPurchaseOrder.getPurchaseOrderLines() );
   }


   @Test
   public void testUpdatePurchaseOrderWithInvalidAuthentication401()
         throws JsonParseException, JsonMappingException, IOException {
      PurchaseOrder updateDetails = new PurchaseOrder();
      updateRequest( 401, "", updateDetails, Credentials.UNAUTHENTICATED );

   }


   @Test
   public void testUpdatePurchaseOrderWithInvalidAuthorization403()
         throws JsonParseException, JsonMappingException, IOException {
      PurchaseOrder updateDetails = new PurchaseOrder();
      updateRequest( 403, "", updateDetails, Credentials.UNAUTHORIZED );

   }


   private Response getById( int statusCode, Credentials security, String purchaseOrderId ) {
      String userName = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().auth().preemptive().basic( userName, password )
            .accept( APPLICATION_JSON ).contentType( APPLICATION_JSON ).expect()
            .statusCode( statusCode ).when().get( PURCHASE_ORDER_API_PATH + "/" + purchaseOrderId );

      return response;
   }


   private Response create( int statusCode, Credentials security, Object purchaseOrder ) {
      String userName = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().auth().preemptive().basic( userName, password )
            .accept( APPLICATION_JSON ).contentType( APPLICATION_JSON ).body( purchaseOrder )
            .expect().statusCode( statusCode ).when().post( PURCHASE_ORDER_API_PATH );
      return response;
   }


   private Response searchRequest( int statusCode, Credentials security ) {
      String username = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given()
            .queryParam( PurchaseOrderSearchParameters.ORDER_NO_PARAM, purchaseOrderNo ).auth()
            .preemptive().basic( username, password ).accept( APPLICATION_JSON )
            .contentType( APPLICATION_JSON ).expect().statusCode( statusCode ).when()
            .get( PURCHASE_ORDER_API_PATH );
      return response;
   }


   private Response updateRequest( int statusCode, String id, Object purchaseOrder,
         Credentials security ) {
      String userName = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().auth().preemptive().basic( userName, password )
            .accept( APPLICATION_JSON ).contentType( APPLICATION_JSON ).body( purchaseOrder )
            .put( PURCHASE_ORDER_API_PATH + "/" + id );

      return response;
   }


   public void prepareData() throws IOException, ParseException {

      purchaseOrder = getPurchaseOrder();
      Response response = create( 200, Credentials.AUTHENTICATED, purchaseOrder );

      PurchaseOrder purchaseOrderCreated =
            objectMapper.readValue( response.getBody().asString(), PurchaseOrder.class );

      purchaseOrderId = purchaseOrderCreated.getId();
      purchaseOrderNo = purchaseOrderCreated.getOrderNumber();

      purchaseOrder.setId( purchaseOrderId );
      purchaseOrder.setOrderNumber( purchaseOrderNo );
      purchaseOrder.getPurchaseOrderLines().get( 0 )
            .setId( purchaseOrderCreated.getPurchaseOrderLines().get( 0 ).getId() );
   }


   private PurchaseOrder getPurchaseOrderFromResponse( Response response ) throws IOException {
      return objectMapper.readValue( response.getBody().asString(), PurchaseOrder.class );
   }


   /**
    * Assert the retrieved PO is as expected.
    *
    * @param aExpectedPurchaseOrder
    * @param aActualPurchaseOrder
    */
   private void assertPurchaseOrderRetrieved( PurchaseOrder expectedPurchaseOrder,
         PurchaseOrder actualPurchaseOrder ) {
      Assert.assertEquals( expectedPurchaseOrder, actualPurchaseOrder );
   }


   /**
    * Assert the created PO is as expected.
    *
    * @param aExpectedPurchaseOrder
    * @param aActualPurchaseOrder
    */
   private void assertPurchaseOrderCreated( PurchaseOrder expectedPurchaseOrder,
         PurchaseOrder actualPurchaseOrder ) {
      // a couple fields are generated when the PO is created
      // so we need to update the original PO for it to match
      expectedPurchaseOrder.setId( actualPurchaseOrder.getId() );
      expectedPurchaseOrder.setOrderNumber( actualPurchaseOrder.getOrderNumber() );

      // do the same for the lines
      Assert.assertEquals( expectedPurchaseOrder.getPurchaseOrderLines().size(),
            actualPurchaseOrder.getPurchaseOrderLines().size() );
      for ( PurchaseOrderLine expectedPOLine : expectedPurchaseOrder.getPurchaseOrderLines() ) {
         PurchaseOrderLine lActualPOLine = ( PurchaseOrderLine ) CollectionUtils.find(
               actualPurchaseOrder.getPurchaseOrderLines(),
               p -> ( ( PurchaseOrderLine ) p ).getLineNo() == expectedPOLine.getLineNo() );
         expectedPOLine.setId( lActualPOLine.getId() );
      }

      Assert.assertEquals( expectedPurchaseOrder, actualPurchaseOrder );
   }


   private void assertPurchaseOrderForSearch( Response actualPurchaseOrder ) throws IOException {
      boolean isContains = false;
      ObjectMapper objectMapper =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      List<PurchaseOrder> purchaseOrderActual =
            objectMapper.readValue( actualPurchaseOrder.getBody().asString(), objectMapper
                  .getTypeFactory().constructCollectionType( List.class, PurchaseOrder.class ) );
      isContains = purchaseOrderActual.contains( purchaseOrder );
      Assert.assertTrue(
            "The purchase order with ID " + purchaseOrder.getId() + "was not in the response",
            isContains );
   }


   private PurchaseOrder getPurchaseOrder() throws ParseException {

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
      PurchaseOrderLine pOLine = new PurchaseOrderLine();
      pOLine.setPartNoId( partNoId );
      pOLine.setPromisedByDate( promisedByDate );
      pOLine.setQuantity( new BigDecimal( QUANTITY ) );
      pOLine.setQtyUnit( UNIT_EACH );
      pOLine.setDeleted( DELETED_BOOL );
      pOLine.setLineNo( PO_LINE_NO );
      pOLine.setLineTypeCode( PO_LINE_CODE );
      pOLine.setPriceType( PO_LINE_PRICE_TYPE_CODE );
      pOLine.setLineDescription( LINE_DESCRIPTION );
      pOLine.setOwnerId( ownerId );
      pOLine.setFinanceAccountCode( financeAccountCd );
      pOLine.setPartRequestIds( new ArrayList<String>() );

      // we currently don't set unit price in the PO API
      // so we always expect to get back a unit price of 0
      pOLine.setUnitPrice( BigDecimal.ZERO );
      pOLine.setPrice( PO_LINE_PRICE );

      purchaseOrder.addPurchaseOrderLine( pOLine );

      return purchaseOrder;
   }
}
