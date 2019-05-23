package com.mxi.am.stepdefn.persona.receivinginspector;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;

import com.mxi.am.driver.integration.finance.sendorderinventoryreceived.SendOrderInventoryReceivedMessageDriver;
import com.mxi.am.driver.web.AmWebConditions;
import com.mxi.am.driver.web.AssetManagement;
import com.mxi.am.driver.web.inventory.InspectAsServiceablePageDriver;
import com.mxi.am.driver.web.inventory.inventorydetailspage.InventoryDetailsPageDriver;
import com.mxi.am.driver.web.po.CreateEditOrderPageDriver;
import com.mxi.am.driver.web.po.RequestAuthorizationPageDriver;
import com.mxi.am.driver.web.shipment.shipmentDetails.ShipmentDetailsPageDriver;
import com.mxi.am.driver.web.shipment.shipmentDetails.shipmentPanes.ShipmentLinesPaneDriver.CompletedShipmentLine;
import com.mxi.driver.standard.Wait;
import com.mxi.xml.xsd.core.finance.orderInventoryReceived.x10.OrderInventoryReceivedDocument;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class SendOrderInventoryReceivedInspStepDefintions {

   @Inject
   private ShipmentDetailsPageDriver iShipmentDetailsPageDriver;

   @Inject
   private InventoryDetailsPageDriver iInventoryDetailsPageDriver;

   @Inject
   private InspectAsServiceablePageDriver iInspectAsServiceablePageDriver;

   @Inject
   private RequestAuthorizationPageDriver iRequestAuthorizationPageDriver;

   @Inject
   private CreateEditOrderPageDriver iCreateEditOrderDriver;

   @Inject
   private SendOrderInventoryReceivedMessageDriver iSendOrderInventoryReceivedMessageDriver;

   @Inject
   @AssetManagement
   private WebDriver iWebDriver;

   private final static String PAGE_NAME = "Inventory Details";

   private final static String TRK_PART_NO = "Tracked Part No";
   private final static String SHIP_TO_LOCATION = "Ship To Location";
   private final static String TRANSPORTATION_TYPE = "Transportation Type";
   private final static String TERMS_AND_CONDITIONS = "Terms & Conditions";
   private final static String FREIGHT_ON_BAORD = "Freight On Board";
   private final static String STANDARD_QUANTITY = "Standard Quantity";
   private final static String STANDARD_UNIT = "Standard Unit";
   private final static String PART_NO = "Part No";
   private final static String VENDOR_QUANTITY = "Vendor Quantity";
   private final static String VENDOR_UNIT = "Vendor Unit";
   private final static String ORDER_LINE_NUMBER = "Order Line Number";
   private final static String MANUFACTURER_CODE = "Manufacturer Code";
   private final static String VENDOR_EX_KEY = "Vendor";


   @When( "^I inspect the inventory as serviceable$" )
   public void iInspectTheInventoryAsServiceable() throws Throwable {

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

      Wait.until( AmWebConditions.isOnPage( iWebDriver, PAGE_NAME ), 30000 );

      // Navigate to the Details tab of the Inventory Details page of the received part
      iInventoryDetailsPageDriver.clickTabDetails();
   }


   @Then( "^Receipt Order message is sent$" )
   public void receiptOrderMessageIsSent( List<Map<String, String>> aDataTable ) throws Throwable {
      // Get PO number for later comparison to out bound message
      String lPONumber = iInventoryDetailsPageDriver.clickTabDetails().getOrderNumber();

      // Create variable to hold value of PO number from out bound message
      String lMessagePONumber = "";

      // Keep receiving the next Order Inventory Received message from the queue until a PO Number
      // match is found OR the queue is empty
      while ( !lPONumber.equals( lMessagePONumber ) ) {

         // create variable of type OrderInventoryReceivedDocument to receive the out bound message
         // from web driver
         OrderInventoryReceivedDocument lOrderInventoryReceivedDoc =
               iSendOrderInventoryReceivedMessageDriver.receive();

         // Fail if the queue is empty
         if ( lOrderInventoryReceivedDoc == null ) {
            Assert.fail( "The ASB Notification Queue is empty" );
         } else {
            // Get the PO number from the out bound message
            lMessagePONumber =
                  lOrderInventoryReceivedDoc.getOrderInventoryReceived().getOrderNumber();
         }

         // Check if the PO Numbers match, If yes perform message contents validation of all fields
         // within Order Inventory Received message
         // If no match, then return to while loop and receive next out bound message from queue
         if ( lPONumber.equals( lMessagePONumber ) ) {

            // Validate current date field is not null
            Assert.assertNotNull(
                  lOrderInventoryReceivedDoc.getOrderInventoryReceived().getCurrentDate() );

            // Create a String pattern for a new Simple Date Format
            String lpattern = ( "yyyy-MM-dd HH:mm:ss" );

            SimpleDateFormat lDateFormat = new SimpleDateFormat( lpattern );
            lDateFormat.setLenient( false );

            // Validate the date format of current date field
            String lcurrentDate =
                  lOrderInventoryReceivedDoc.getOrderInventoryReceived().getCurrentDate();
            try {
               lDateFormat.parse( lcurrentDate.trim() );
            } catch ( ParseException pe ) {
               Assert.fail(
                     "The Current Date field is not of the correct format yyyy-MM-dd HH:mm:ss" );
            }

            // Validate part number
            String lpartNumber =
                  lOrderInventoryReceivedDoc.getOrderInventoryReceived().getPartNumber();
            Assert.assertEquals( aDataTable.get( 0 ).get( PART_NO ), lpartNumber );

            // Validate manufacturer code
            String lmanufacturerCode =
                  lOrderInventoryReceivedDoc.getOrderInventoryReceived().getManufacturerCode();
            Assert.assertEquals( aDataTable.get( 0 ).get( MANUFACTURER_CODE ), lmanufacturerCode );

            // Validate standard quantity
            String lstandardQuantity = lOrderInventoryReceivedDoc.getOrderInventoryReceived()
                  .getStandardQuantity().toString();
            Assert.assertEquals( aDataTable.get( 0 ).get( STANDARD_QUANTITY ), lstandardQuantity );

            // Validate standard unit
            String lstandardUnit =
                  lOrderInventoryReceivedDoc.getOrderInventoryReceived().getStandardUnit();
            Assert.assertEquals( aDataTable.get( 0 ).get( STANDARD_UNIT ), lstandardUnit );

            // Validate vendor quantity
            String lvendorQuantity = lOrderInventoryReceivedDoc.getOrderInventoryReceived()
                  .getVendorQuantity().toString();
            Assert.assertEquals( aDataTable.get( 0 ).get( VENDOR_QUANTITY ), lvendorQuantity );

            // Validate vendor unit
            String lvendorUnit =
                  lOrderInventoryReceivedDoc.getOrderInventoryReceived().getVendorUnit();
            Assert.assertEquals( aDataTable.get( 0 ).get( VENDOR_UNIT ), lvendorUnit );

            // Validate order line number
            String llineNumber =
                  lOrderInventoryReceivedDoc.getOrderInventoryReceived().getOrderLineNumber();
            Assert.assertEquals( aDataTable.get( 0 ).get( ORDER_LINE_NUMBER ), llineNumber );

            // Validate creation date field is not null
            Assert.assertNotNull(
                  lOrderInventoryReceivedDoc.getOrderInventoryReceived().getOrderCreationDate() );

            // Validate the date format of creation date field
            String lcreationDate =
                  lOrderInventoryReceivedDoc.getOrderInventoryReceived().getOrderCreationDate();
            try {
               lDateFormat.parse( lcreationDate.trim() );
            } catch ( ParseException pe ) {
               Assert.fail(
                     "The Creation Date field is not of the correct format yyyy-MM-dd HH:mm:ss" );
            }
         }
      }
   }


   @Given( "^that I have filled in a tracked part$" )
   public void thatIHaveFilledInATrackedPart( List<Map<String, String>> aDataTable )
         throws Throwable {
      iCreateEditOrderDriver.setPartNumber( aDataTable.get( 0 ).get( TRK_PART_NO ) );
      // Click elsewhere on the screen so that the auto-logic associated to the Part No field
      // completes and I can continue editing fields without having these changes rolled back
      iCreateEditOrderDriver.headerOrderInformation();
      iCreateEditOrderDriver.setVendor( aDataTable.get( 0 ).get( VENDOR_EX_KEY ) );
      iCreateEditOrderDriver.setShipTo( aDataTable.get( 0 ).get( SHIP_TO_LOCATION ) );
      iCreateEditOrderDriver
            .setTransportationType( aDataTable.get( 0 ).get( TRANSPORTATION_TYPE ) );
      iCreateEditOrderDriver.setTermsConditions( aDataTable.get( 0 ).get( TERMS_AND_CONDITIONS ) );
      iCreateEditOrderDriver.setFreightOnBoard( aDataTable.get( 0 ).get( FREIGHT_ON_BAORD ) );
      // Set the Unit of Measure prior to the Quantity since the Quantity field is cleared upon
      // selecting a different Unit of Measure
      iCreateEditOrderDriver.setUnitOfMeasure( aDataTable.get( 0 ).get( STANDARD_UNIT ) );
      iCreateEditOrderDriver.setQuantity( aDataTable.get( 0 ).get( STANDARD_QUANTITY ) );
      iCreateEditOrderDriver.clickOK();
   }


   @Then( "^Receipt Order message is not sent$" )
   public void receiptOrderMessageIsNotSent() throws Throwable {
      // create variable of type OrderInventoryReceivedDocument to receive the out bound message
      // from web driver
      OrderInventoryReceivedDocument lOrderInventoryReceivedDoc =
            iSendOrderInventoryReceivedMessageDriver.receive();

      // Fail if the queue DOES return an Order Inventory Received message
      if ( lOrderInventoryReceivedDoc != null ) {
         Assert.fail(
               "The ASB Notification Queue is NOT empty. An Order Inventory Received message was found" );
      }
   }
}
