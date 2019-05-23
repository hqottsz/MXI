package com.mxi.am.stepdefn.persona.storeroomclerk;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;

import com.mxi.am.driver.integration.finance.sendorderinventoryreturned.SendOrderInventoryReturnedMessageDriver;
import com.mxi.am.driver.web.AmWebConditions;
import com.mxi.am.driver.web.AssetManagement;
import com.mxi.am.driver.web.inventory.InspectAsServiceablePageDriver;
import com.mxi.am.driver.web.inventory.inventorydetailspage.InventoryDetailsPageDriver;
import com.mxi.am.driver.web.po.OrderDetailsPageDriver;
import com.mxi.am.driver.web.po.RequestAuthorizationPageDriver;
import com.mxi.am.driver.web.po.podetails.ReceiptReturnsPaneDriver;
import com.mxi.am.driver.web.po.podetails.ReceiptReturnsPaneDriver.OutboundShipmentLine;
import com.mxi.am.driver.web.shipment.ReturnToVendorPageDriver;
import com.mxi.am.driver.web.shipment.SendShipmentPageDriver;
import com.mxi.am.driver.web.shipment.shipmentDetails.ShipmentDetailsPageDriver;
import com.mxi.am.driver.web.shipment.shipmentDetails.shipmentPanes.ShipmentLinesPaneDriver.CompletedShipmentLine;
import com.mxi.driver.standard.Wait;
import com.mxi.xml.xsd.core.finance.orderInventoryReturned.x10.OrderInventoryReturnedDocument;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class SendOrderInventoryReturnedStepDefinitions {

   @Inject
   private ShipmentDetailsPageDriver iShipmentDetailsPageDriver;

   @Inject
   private InventoryDetailsPageDriver iInventoryDetailsPageDriver;

   @Inject
   private InspectAsServiceablePageDriver iInspectAsServiceablePageDriver;

   @Inject
   private RequestAuthorizationPageDriver iRequestAuthorizationPageDriver;

   @Inject
   private ReturnToVendorPageDriver iReturnToVendorPageDriver;

   @Inject
   private OrderDetailsPageDriver iOrderDetailsPageDriver;

   @Inject
   private SendShipmentPageDriver iSendShipmentPageDriver;

   @Inject
   private SendOrderInventoryReturnedMessageDriver iSendOrderInventoryReturnedMessageDriver;

   @Inject
   @AssetManagement
   private WebDriver iWebDriver;

   private final static String PAGE_NAME = "Inventory Details";

   private final static String PART_NO = "Part No";
   private final static String STANDARD_QUANTITY = "Standard Quantity";
   private final static String STANDARD_UNIT = "Standard Unit";
   private final static String VENDOR_QUANTITY = "Vendor Quantity";
   private final static String VENDOR_UNIT = "Vendor Unit";
   private final static String MANUFACTURER_CODE = "Manufacturer Code";
   private final static String ORDER_LINE_NUMBER = "Order Line Number";


   @Given( "^that I have inspected the inventory as serviceable$" )
   public void thatIHaveInspectedTheInventoryAsServiceable() throws Throwable {
      // Note that this has been hard coded to only click on the first row and hence does NOT
      // support inspecting multiple inventory items of different part numbers that have been all
      // received on the same shipment
      List<CompletedShipmentLine> lCompletedShipmentLineRow =
            iShipmentDetailsPageDriver.clickTabShipmentLines().getCompletedShipmentLines();
      CompletedShipmentLine lShipmentLineRow = lCompletedShipmentLineRow.get( 0 );
      lShipmentLineRow.clickSerialBatchNO();

      // Click the Inspect as Serviceable button on the Inventory Details page
      iInventoryDetailsPageDriver.clickInspectAsServiceable();

      // Un-check the Print Serviceable Part Tag check box and click OK
      iInspectAsServiceablePageDriver.printServiceablepartTag();
      iInspectAsServiceablePageDriver.clickOk();

      // Enter password into Authentication Required pop-up window and click OK
      iRequestAuthorizationPageDriver.setPassword( "password" );
      iRequestAuthorizationPageDriver.authenticate();
   }


   @Given( "^that I have created a Return to Vendor shipment$" )
   public void thatIHaveCreatedAReturnToVendorShipment() throws Throwable {
      iInventoryDetailsPageDriver.clickReturnToVendor();
      iReturnToVendorPageDriver.ok();

      Wait.until( AmWebConditions.isOnPage( iWebDriver, PAGE_NAME ), 30000 );
   }


   @When( "^I complete the Return to Vendor shipment$" )
   public void iCompleteTheReturnToVendorShipment() throws Throwable {
      iInventoryDetailsPageDriver.clickTabDetails().clickOrderField();

      ReceiptReturnsPaneDriver lReceiptReturnsPaneDriver =
            iOrderDetailsPageDriver.clickTabReceiptReturns();

      // Get the Outbound Shipments table so that Outbound Shipment hyperlink for the FIRST row
      // (Index 0) can be clicked
      List<OutboundShipmentLine> lOutboundShipmentLine =
            lReceiptReturnsPaneDriver.getOutboundShipmentLines();
      OutboundShipmentLine lOutboundShipmentLineRow = lOutboundShipmentLine.get( 0 );
      lOutboundShipmentLineRow.clickOutboundShipment();

      iShipmentDetailsPageDriver.clickSendShipment();
      iSendShipmentPageDriver.clickOK();
   }


   @Then( "^Order Inventory Returned message is sent$" )
   public void orderInventoryReturnedMessageIsSent( List<Map<String, String>> aDataTable )
         throws Throwable {

      // Get PO number for later comparison to out bound message
      String lPONumber = iShipmentDetailsPageDriver.getOrderNumber();

      // Create variable to hold value of PO number from out bound message
      String lMessagePONumber = "";

      // Keep receiving the next Order Inventory Returned message from the queue until a PO Number
      // match is found OR the queue is empty
      while ( !lPONumber.equals( lMessagePONumber ) ) {

         // create variable of type OrderInventoryReturnedDocument to receive the out bound message
         // from web driver
         OrderInventoryReturnedDocument lOrderInventoryReturnedDoc =
               iSendOrderInventoryReturnedMessageDriver.receive();

         // Fail if the queue is empty
         if ( lOrderInventoryReturnedDoc == null ) {
            Assert.fail( "The ASB Notification Queue is empty" );
         } else {
            // Get the PO number from the out bound message
            lMessagePONumber =
                  lOrderInventoryReturnedDoc.getOrderInventoryReturned().getOrderNumber();
         }

         // Check if the PO Numbers match, If yes perform message contents validation
         // If no match, then return to while loop and receive next out bound message from queue
         if ( lPONumber.equals( lMessagePONumber ) ) {

            // Validate order creation date field is not null
            Assert.assertNotNull(
                  lOrderInventoryReturnedDoc.getOrderInventoryReturned().getOrderCreationDate() );

            // Validate the date format of current date field
            String lcreationDate =
                  lOrderInventoryReturnedDoc.getOrderInventoryReturned().getOrderCreationDate();

            String lpattern = ( "yyyy-MM-dd HH:mm:ss" );

            SimpleDateFormat lDateFormat = new SimpleDateFormat( lpattern );
            lDateFormat.setLenient( false );

            try {
               lDateFormat.parse( lcreationDate.trim() );
            } catch ( ParseException pe ) {
               Assert.fail(
                     "The Creation Date field is not of the correct format yyyy-MM-dd HH:mm:ss" );
            }

            // Validate part number
            String lpartNumber =
                  lOrderInventoryReturnedDoc.getOrderInventoryReturned().getPartNumber();
            Assert.assertEquals( aDataTable.get( 0 ).get( PART_NO ), lpartNumber );

            // Validate manufacturer code
            String lmanufacturerCode =
                  lOrderInventoryReturnedDoc.getOrderInventoryReturned().getManufacturerCode();
            Assert.assertEquals( aDataTable.get( 0 ).get( MANUFACTURER_CODE ), lmanufacturerCode );

            // Validate standard quantity
            String lstandardQuantity = lOrderInventoryReturnedDoc.getOrderInventoryReturned()
                  .getStandardQuantity().toString();
            Assert.assertEquals( aDataTable.get( 0 ).get( STANDARD_QUANTITY ), lstandardQuantity );

            // Validate standard unit
            String lstandardUnit =
                  lOrderInventoryReturnedDoc.getOrderInventoryReturned().getStandardUnit();
            Assert.assertEquals( aDataTable.get( 0 ).get( STANDARD_UNIT ), lstandardUnit );

            // Validate vendor quantity
            String lvendorQuantity = lOrderInventoryReturnedDoc.getOrderInventoryReturned()
                  .getVendorQuantity().toString();
            Assert.assertEquals( aDataTable.get( 0 ).get( VENDOR_QUANTITY ), lvendorQuantity );

            // Validate vendor unit
            String lvendorUnit =
                  lOrderInventoryReturnedDoc.getOrderInventoryReturned().getVendorUnit();
            Assert.assertEquals( aDataTable.get( 0 ).get( VENDOR_UNIT ), lvendorUnit );

            // Validate order line number
            String llineNumber =
                  lOrderInventoryReturnedDoc.getOrderInventoryReturned().getOrderLineNumber();
            Assert.assertEquals( aDataTable.get( 0 ).get( ORDER_LINE_NUMBER ), llineNumber );
         }
      }

   }
}
