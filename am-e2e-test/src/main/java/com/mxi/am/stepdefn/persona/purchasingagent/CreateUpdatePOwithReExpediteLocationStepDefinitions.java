package com.mxi.am.stepdefn.persona.purchasingagent;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import com.mxi.am.driver.common.MessagePageDriver;
import com.mxi.am.driver.web.LoginPageDriver;
import com.mxi.am.driver.web.LogoutPageDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.part.partsearchpage.PartSearchPageDriver;
import com.mxi.am.driver.web.po.AddPartOrderLinePageDriver;
import com.mxi.am.driver.web.po.CreateEditOrderPageDriver;
import com.mxi.am.driver.web.po.EditOrderLinesPageDriver;
import com.mxi.am.driver.web.po.EditOrderLinesPageDriver.PartLine;
import com.mxi.am.driver.web.po.IssueOrderPageDriver;
import com.mxi.am.driver.web.po.OrderDetailsPageDriver;
import com.mxi.am.driver.web.po.OrderSearchPageDriver;
import com.mxi.am.driver.web.po.RequestAuthorizationPageDriver;
import com.mxi.am.driver.web.po.podetails.OrderLinesPaneDriver.PoPartLinesTable;
import com.mxi.am.driver.web.shipment.ShipmentSearchPageDriver;
import com.mxi.am.driver.web.shipment.routing.ReceiveShipmentSegmentPageDriver;
import com.mxi.am.driver.web.shipment.shipmentDetails.ShipmentDetailsPageDriver;
import com.mxi.am.driver.web.shipment.shipmentDetails.shipmentPanes.ShipmentLinesPaneDriver.CompletedShipmentLine;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class CreateUpdatePOwithReExpediteLocationStepDefinitions {

   @Inject
   private AddPartOrderLinePageDriver iAddPartOrderLinePageDriver;

   @Inject
   private CreateEditOrderPageDriver iCreateEditOrderPageDriver;

   @Inject
   private EditOrderLinesPageDriver iEditOrderLinesPageDriver;

   @Inject
   private IssueOrderPageDriver iIssueOrderPageDriver;

   @Inject
   private LoginPageDriver iLoginPageDriver;

   @Inject
   private LogoutPageDriver iLogoutPageDriver;

   @Inject
   private MessagePageDriver iMessagePageDriver;

   @Inject
   private NavigationDriver iNavDriver;

   @Inject
   private OrderDetailsPageDriver iOrderDetailsPageDriver;

   @Inject
   private OrderSearchPageDriver iOrderSearchPageDriver;

   @Inject
   private PartSearchPageDriver iPartSearchPageDriver;

   @Inject
   private ReceiveShipmentSegmentPageDriver iReceiveShipmentSegmentPageDriver;

   @Inject
   private RequestAuthorizationPageDriver iRequestAuthorizationPageDriver;

   @Inject
   private ShipmentDetailsPageDriver iShipmentDetailsPageDriver;

   @Inject
   private ShipmentSearchPageDriver iShipmentSearchPageDriver;

   // Generate a pseudo-random Order Number to be used in the test
   private static final String ORDER_NUMBER =
         new Random().nextInt( 1000 ) + "OPRSCREEXPPO" + new Random().nextInt( 1000 );

   private static final String SERIALIZED_PART_NO = "A0000002";
   private static final String VENDOR_CD = "10002";
   private static final String SERIALIZED_PART_LINE_ORIGINAL_QTY = "1";
   private static final String SHIP_TO_LOCATION = "AIRPORT1/DOCK";
   private static final String RE_EXPEDITE_TO_LOCATION = "AIRPORT2/DOCK";
   private static final String BATCH_PART_NO_1 = "A0000009";
   private static final String BATCH_PART_LINE_1_ORIGINAL_QTY = "10";
   private static final String BATCH_PART_NO_2 = "A0000011";
   private static final String BATCH_PART_LINE_2_ORIGINAL_QTY = "20";
   private static final String UNIT_PRICE = "10";
   private static final String PURCHASING_AGENT_PASSWORD = "password";
   private static final String STOREROOM_CLERK_USERNAME = "user1";
   private static final String STOREROOM_CLERK_PASSWORD = "password";
   private static final String PURCHASING_AGENT_USERNAME = "purcha1";
   private static final String SERIALIZED_PART_LINE_NEW_QTY = "2";
   private static final String BATCH_PART_LINE_1_NEW_QTY = "15";
   private static final String BATCH_PART_LINE_2_NEW_QTY = "18";


   @Given( "^a Purchase order is created with Serialized part and has a re-expedite location$" )
   public void aPurchaseOrderCreatedSerializedPartReExpediteLocation() throws Throwable {
      iNavDriver.navigate( "Purchasing Agent", "Order Search" );
      iOrderSearchPageDriver.clickTabOrderFound().clickCreatePO();

      iCreateEditOrderPageDriver.setOrderNumber( ORDER_NUMBER );
      iCreateEditOrderPageDriver.clickSelectPartNo();

      iPartSearchPageDriver.clickClearAll();
      iPartSearchPageDriver.setOemPartNo( SERIALIZED_PART_NO );
      iPartSearchPageDriver.clickSearch();
      iPartSearchPageDriver.clickTabPartsFound().getResults().get( 0 ).clickPartNo();
      iPartSearchPageDriver.clickTabPartsFound().clickAssignPart();

      iCreateEditOrderPageDriver.setVendor( VENDOR_CD );
      iCreateEditOrderPageDriver.setQuantity( SERIALIZED_PART_LINE_ORIGINAL_QTY );
      iCreateEditOrderPageDriver.setShipTo( SHIP_TO_LOCATION );
      iCreateEditOrderPageDriver.setReExpediteLocation( RE_EXPEDITE_TO_LOCATION );
      iCreateEditOrderPageDriver.clickOK();
   }


   @Given( "^two other batched parts are added to the order$" )
   public void twoOtherBatchedPartsAddedToOrder() throws Throwable {
      iOrderDetailsPageDriver.clickTabOrderLines().clickAddPartLine();

      iAddPartOrderLinePageDriver.clickPartSearch();

      iPartSearchPageDriver.clickClearAll();
      iPartSearchPageDriver.setOemPartNo( BATCH_PART_NO_1 );
      iPartSearchPageDriver.clickSearch();
      iPartSearchPageDriver.clickTabPartsFound().getResults().get( 0 ).clickPartNo();
      iPartSearchPageDriver.clickTabPartsFound().clickAssignPart();

      iAddPartOrderLinePageDriver.setQuantity( BATCH_PART_LINE_1_ORIGINAL_QTY );
      iAddPartOrderLinePageDriver.clickOK();

      iOrderDetailsPageDriver.clickTabOrderLines().clickAddPartLine();

      iAddPartOrderLinePageDriver.clickPartSearch();

      iPartSearchPageDriver.clickClearAll();
      iPartSearchPageDriver.setOemPartNo( BATCH_PART_NO_2 );
      iPartSearchPageDriver.clickSearch();
      iPartSearchPageDriver.clickTabPartsFound().getResults().get( 0 ).clickPartNo();
      iPartSearchPageDriver.clickTabPartsFound().clickAssignPart();

      iAddPartOrderLinePageDriver.setQuantity( BATCH_PART_LINE_2_ORIGINAL_QTY );
      iAddPartOrderLinePageDriver.clickOK();

      iOrderDetailsPageDriver.clickTabOrderLines().clickEditLines();

      List<PartLine> lPartLines = iEditOrderLinesPageDriver.getPartLines();
      for ( PartLine lPartLine : lPartLines ) {
         lPartLine.setUnitPrice( UNIT_PRICE );
      }
      iEditOrderLinesPageDriver.clickOK();
   }


   @Given( "^the order is received at the re-expedite location by a Storeroom Clerk$" )
   public void orderReceivedOnReExpediteLocation() throws Throwable {
      iOrderDetailsPageDriver.clickRequestAuthorization();

      iRequestAuthorizationPageDriver.clickOK();
      iRequestAuthorizationPageDriver.setPassword( PURCHASING_AGENT_PASSWORD ).authenticate();

      iOrderDetailsPageDriver.clickIssuePurchaseOrder();

      iIssueOrderPageDriver.clickOk();

      // The shipment is now received at its first segment's destination by a Storeroom Clerk
      // The Purchasing Agent must log out and the Storeroom Clerk log in
      iNavDriver.clickLogout();

      iLogoutPageDriver.clickOK();

      iLoginPageDriver.setUserName( STOREROOM_CLERK_USERNAME );
      iLoginPageDriver.setPassword( STOREROOM_CLERK_PASSWORD );
      iLoginPageDriver.login();

      iNavDriver.navigate( "Storeroom Clerk", "Shipment Search" );

      iShipmentSearchPageDriver.clickClearAll();
      iShipmentSearchPageDriver.setOrderNumber( ORDER_NUMBER );
      iShipmentSearchPageDriver.clickSearch();
      iShipmentSearchPageDriver.getSearchResults().get( 0 ).clickShipmentNo();

      iShipmentDetailsPageDriver.clickReceiveShipment();

      iReceiveShipmentSegmentPageDriver.clickOk();
   }


   @When( "^the PO lines are edited with decrease of quantity for one batched part and increase of quantities for other parts$" )
   public void thePOLinesEditedDecreaseQuantityBatchIncreaseQuantitiesOtherParts()
         throws Throwable {
      // Log out as the Storeroom Clerk and log back in as the Purchasing Agent
      iNavDriver.clickLogout();

      iLogoutPageDriver.clickOK();

      iLoginPageDriver.setUserName( PURCHASING_AGENT_USERNAME );
      iLoginPageDriver.setPassword( PURCHASING_AGENT_PASSWORD );
      iLoginPageDriver.login();

      iNavDriver.navigate( "Purchasing Agent", "Order Search" );

      iOrderSearchPageDriver.clickClearAll();
      iOrderSearchPageDriver.setOrderNumber( ORDER_NUMBER );
      iOrderSearchPageDriver.clickSearch();

      iOrderDetailsPageDriver.clickTabOrderLines().clickEditLines();
      List<PartLine> lPartLines = iEditOrderLinesPageDriver.getPartLines();
      lPartLines.get( 0 ).setPartQuantity( SERIALIZED_PART_LINE_NEW_QTY );
      lPartLines.get( 1 ).setPartQuantity( BATCH_PART_LINE_1_NEW_QTY );
      lPartLines.get( 2 ).setPartQuantity( BATCH_PART_LINE_2_NEW_QTY );
      iEditOrderLinesPageDriver.clickOK();

      iMessagePageDriver.clickOk();
   }


   @Then( "^only one pending shipment exists for both the Purchasing Agent and Storeroom Clerk$" )
   public void onlyOnePendingShipmentExists() throws Throwable {
      // Validate as the Purchasing Agent that only one shipment exists using the Receipts and
      // Returns tab for the order
      int lNumberOfInboundShipments =
            iOrderDetailsPageDriver.clickTabReceiptReturns().getInboundShipmentLines().size();
      int lNumberOfOutboundShipments =
            iOrderDetailsPageDriver.clickTabReceiptReturns().getOutboundShipmentLines().size();
      assertEquals(
            "More than one pending shipment exists for the order on the Receipts and Returns tab",
            1, lNumberOfInboundShipments + lNumberOfOutboundShipments );

      // Validate as the Storeroom Clerk that only one shipment exists using the Shipment Search
      // page
      iNavDriver.clickLogout();

      iLogoutPageDriver.clickOK();

      iLoginPageDriver.setUserName( STOREROOM_CLERK_USERNAME );
      iLoginPageDriver.setPassword( STOREROOM_CLERK_PASSWORD );
      iLoginPageDriver.login();

      iNavDriver.navigate( "Storeroom Clerk", "Shipment Search" );
      iShipmentSearchPageDriver.clickClearAll();
      iShipmentSearchPageDriver.setOrderNumber( ORDER_NUMBER );
      iShipmentSearchPageDriver.clickSearch();
      int lNumberOfShipments = iShipmentSearchPageDriver.getSearchResults().size();

      assertEquals(
            "More than one pending shipment exists for the order on the Shipment Search page", 1,
            lNumberOfShipments );
   }


   @Then( "^the quantities of open shipment lines for the order equal the quantities of order lines$" )
   public void quantitiesOpenShipmentLinesEqualQuantitiesOrderLines() throws Throwable {
      // Log out as the Storeroom Clerk and log back in as the Purchasing Agent to count ordered
      // quantity
      iNavDriver.clickLogout();

      iLogoutPageDriver.clickOK();

      iLoginPageDriver.setUserName( PURCHASING_AGENT_USERNAME );
      iLoginPageDriver.setPassword( PURCHASING_AGENT_PASSWORD );
      iLoginPageDriver.login();

      iNavDriver.navigate( "Purchasing Agent", "Order Search" );
      iOrderSearchPageDriver.clickClearAll();
      iOrderSearchPageDriver.setOrderNumber( ORDER_NUMBER );
      iOrderSearchPageDriver.clickSearch();

      int lOrderedQty = 0;
      List<PoPartLinesTable> lOrderLines = iOrderDetailsPageDriver.clickTabOrderLines().getPoLine();
      for ( PoPartLinesTable lOrderLine : lOrderLines ) {
         String lOrderLineRenderedQty = lOrderLine.getQuantity();
         if ( lOrderLineRenderedQty != null && !lOrderLineRenderedQty.isEmpty()
               && !lOrderLineRenderedQty.trim().isEmpty() ) {
            String lOrderLineQty = lOrderLineRenderedQty.split( "\\s" )[0];
            lOrderedQty += Integer.parseInt( lOrderLineQty );
         }
      }

      // Log out as the Purchasing Agent and log back in as the Storeroom Clerk to count shipped
      // quantity
      iNavDriver.clickLogout();

      iLogoutPageDriver.clickOK();

      iLoginPageDriver.setUserName( STOREROOM_CLERK_USERNAME );
      iLoginPageDriver.setPassword( STOREROOM_CLERK_PASSWORD );
      iLoginPageDriver.login();

      iNavDriver.navigate( "Storeroom Clerk", "Shipment Search" );

      iShipmentSearchPageDriver.clickClearAll();
      iShipmentSearchPageDriver.setOrderNumber( ORDER_NUMBER );
      iShipmentSearchPageDriver.clickSearch();
      iShipmentSearchPageDriver.getSearchResults().get( 0 ).clickShipmentNo();

      int lShippedQty = 0;
      List<CompletedShipmentLine> lShipmentLines =
            iShipmentDetailsPageDriver.clickTabShipmentLines().getCompletedShipmentLines();
      for ( CompletedShipmentLine lShipmentLine : lShipmentLines ) {
         String lShipmentLineRenderedQty = lShipmentLine.getExpectedQty();
         if ( lShipmentLineRenderedQty != null && !lShipmentLineRenderedQty.isEmpty()
               && !lShipmentLineRenderedQty.trim().isEmpty() ) {
            String lShipmentLineQty = lShipmentLineRenderedQty.split( "\\s" )[0];
            lShippedQty += Integer.parseInt( lShipmentLineQty );
         }
      }

      assertEquals(
            "Sum of the quantities of order lines is not equal to the sum of the quantities of shipment lines",
            lOrderedQty, lShippedQty );
   }

}
