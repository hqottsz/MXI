package com.mxi.am.stepdefn.persona.purchasingagent;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.openqa.selenium.WebDriver;

import com.mxi.am.driver.web.AmWebConditions;
import com.mxi.am.driver.web.AssetManagement;
import com.mxi.am.driver.web.inventory.InspectAsServiceablePageDriver;
import com.mxi.am.driver.web.inventory.inventorydetailspage.InventoryDetailsPageDriver;
import com.mxi.am.driver.web.po.CreateEditOrderPageDriver;
import com.mxi.am.driver.web.po.IssueOrderPageDriver;
import com.mxi.am.driver.web.po.OrderDetailsPageDriver;
import com.mxi.am.driver.web.po.RequestAuthorizationPageDriver;
import com.mxi.am.driver.web.shipment.ReceiveShipmentPageDriver;
import com.mxi.am.driver.web.shipment.ReceiveShipmentPageDriver.ShipmentLine;
import com.mxi.am.driver.web.shipment.shipmentDetails.ShipmentDetailsPageDriver;
import com.mxi.am.driver.web.shipment.shipmentDetails.shipmentPanes.ShipmentLinesPaneDriver.CompletedShipmentLine;
import com.mxi.driver.standard.Wait;

import cucumber.api.java.en.Given;


// part_number element: When receiving more than one shipment line against order line, and received
// parts against an order line are NOT all the same P/N, set invoice line part number to the last
// part number received against the shipment
public class CreatePurchaseInvoiceNoPNRecDiffPNsStepDefintions {

   @Inject
   private CreateEditOrderPageDriver iCreateEditOrderDriver;

   @Inject
   private OrderDetailsPageDriver iOrderDetailsDriver;

   @Inject
   private RequestAuthorizationPageDriver iRequestAuthorizationPageDriver;

   @Inject
   private IssueOrderPageDriver iIssueOrderPageDriver;

   @Inject
   private InventoryDetailsPageDriver iInventoryDetailsPageDriver;

   @Inject
   private ReceiveShipmentPageDriver iReceiveShipmentPageDriver;

   @Inject
   private ShipmentDetailsPageDriver iShipmentDetailsPageDriver;

   @Inject
   private InspectAsServiceablePageDriver iInspectAsServiceablePageDriver;

   @Inject
   @AssetManagement
   private WebDriver iWebDriver;

   private final static String PAGE_NAME = "Inventory Details";

   // Variables to be used to create and receive Order
   private final static String SHIP_TO_LOCATION = "Ship To Location";
   private final static String PART_NO = "Part No";
   private final static String STANDARD_QUANTITY = "Standard Quantity";
   private final static String STANDARD_UNIT = "Standard Unit";
   private final static String RECEIVE_SHIPMENT_QTY = "Receive Shipment Quantity";
   private final static String PART_TYPE = "Part Type";
   private final static String VENDOR = "Vendor";
   private final static String BATCH = "Batch";
   private final static String RECEIVED_PART_NO_1 = "Received Part No Shipment Line One";
   private final static String RECEIVED_PART_NO_2 = "Received Part No Shipment Line Two";


   @Given( "^that I have received an order with some alternate parts and inspected the inventory as serviceable$" )
   public void thatIHaveReceivedAnOrderWithSomeAlternatePartsAndInspectedTheInventoryAsServiceable(
         List<Map<String, String>> aDataTable ) throws Throwable {
      // Create the Purchase Order:
      iCreateEditOrderDriver.setPartNumber( aDataTable.get( 0 ).get( PART_NO ) );
      // Click elsewhere on the screen so that the auto-logic associated to the Part No field
      // completes and I can continue editing fields without having these changes rolled back
      iCreateEditOrderDriver.headerOrderInformation();
      iCreateEditOrderDriver.setVendor( aDataTable.get( 0 ).get( VENDOR ) );
      iCreateEditOrderDriver.setShipTo( aDataTable.get( 0 ).get( SHIP_TO_LOCATION ) );
      // Set the Unit of Measure prior to the Quantity since the Quantity field is cleared upon
      // selecting a different Unit of Measure
      iCreateEditOrderDriver.setUnitOfMeasure( aDataTable.get( 0 ).get( STANDARD_UNIT ) );
      iCreateEditOrderDriver.setQuantity( aDataTable.get( 0 ).get( STANDARD_QUANTITY ) );
      iCreateEditOrderDriver.clickOK();

      // Request Authorization and Issue the Purchase Order:
      iOrderDetailsDriver.clickRequestAuthorization();
      iRequestAuthorizationPageDriver.clickOK();
      iRequestAuthorizationPageDriver.setPassword( "password" );
      iRequestAuthorizationPageDriver.authenticate();
      iOrderDetailsDriver.clickIssuePurchaseOrder();
      iIssueOrderPageDriver.clickOk();

      // Receive the Purchase Order:
      iOrderDetailsDriver.clickReceiveShipment();
      List<ShipmentLine> lShipmentLinesAmmendment = iReceiveShipmentPageDriver.getShipmentLines();

      for ( ShipmentLine lShipmentLine : lShipmentLinesAmmendment ) {

         // Only auto generate a serial number if the part requires a serial number
         if ( ( aDataTable.get( 0 ).get( PART_TYPE ) ).equals( BATCH ) ) {
            // do not generate a serial number
         } else {
            lShipmentLine.generateSerial();
         }

         // Note that the aDataTable index has been hard coded to 0 and therefore this receiving
         // method is limited to only receiving 1 order line.
         // That is, your order may have ONLY 1 Part with multiple quantities and NOT multiple Parts
         lShipmentLine.setQty( aDataTable.get( 0 ).get( RECEIVE_SHIPMENT_QTY ) );

      }
      // Only receive the first shipment line as an Alternate Part
      iReceiveShipmentPageDriver.setOEMPartNo1( aDataTable.get( 0 ).get( RECEIVED_PART_NO_1 ) );
      // Receive the second shipment line as the Standard Part. This can either be set using the
      // seOEMPartNo2 method or just not set at all since the received Part drop down box will
      // default to the standard part automatically
      iReceiveShipmentPageDriver.setOEMPartNo2( aDataTable.get( 0 ).get( RECEIVED_PART_NO_2 ) );
      iReceiveShipmentPageDriver.clickOk();

      // Inspect the Inventory as Serviceable:
      // Navigate to the inventory details page of the received part
      // Note that this has been hard coded to only click on the first row and hence does NOT
      // support inspecting multiple inventory items of different part numbers that have been all
      // received on the same shipment
      iShipmentDetailsPageDriver.clickTabShipmentLines();
      List<CompletedShipmentLine> lCompletedShipmentLineRow =
            iShipmentDetailsPageDriver.getTabShipmentLines().getCompletedShipmentLines();

      // Get the number of Rows/Shipment Lines
      int lTotalRows = lCompletedShipmentLineRow.size();

      // Inspect as Serviceable the inventory of each Shipment Line
      for ( int i = 0; i <= ( lTotalRows - 1 ); i++ ) {
         CompletedShipmentLine lShipmentLineRow = lCompletedShipmentLineRow.get( i );
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

         if ( i != lTotalRows - 1 ) {
            iInventoryDetailsPageDriver.clickClose();
            lCompletedShipmentLineRow =
                  iShipmentDetailsPageDriver.clickTabShipmentLines().getCompletedShipmentLines();
         }
      }
   }
}
