package com.mxi.am.stepdefn.persona.receivinginspector;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.openqa.selenium.WebDriver;

import com.mxi.am.driver.common.ConfirmPageDriver;
import com.mxi.am.driver.integration.finance.sendorderinventoryreceived.SendOrderInventoryReceivedMessageDriver;
import com.mxi.am.driver.web.AmWebConditions;
import com.mxi.am.driver.web.AssetManagement;
import com.mxi.am.driver.web.inventory.InspectAsServiceablePageDriver;
import com.mxi.am.driver.web.inventory.inventorydetailspage.InventoryDetailsPageDriver;
import com.mxi.am.driver.web.inventory.inventorydetailspage.inventorydetailspanes.DetailsPaneDriver.BatchDistributionLine;
import com.mxi.am.driver.web.po.EditOrderLinesPageDriver;
import com.mxi.am.driver.web.po.EditOrderLinesPageDriver.PartLine;
import com.mxi.am.driver.web.po.OrderDetailsPageDriver;
import com.mxi.am.driver.web.po.RequestAuthorizationPageDriver;
import com.mxi.am.driver.web.po.podetails.ReceiptReturnsPaneDriver;
import com.mxi.am.driver.web.po.podetails.ReceiptReturnsPaneDriver.InboundShipmentLine;
import com.mxi.am.driver.web.shipment.ReceiveShipmentPageDriver;
import com.mxi.am.driver.web.shipment.ReceiveShipmentPageDriver.ShipmentLine;
import com.mxi.am.driver.web.shipment.shipmentDetails.ShipmentDetailsPageDriver;
import com.mxi.am.driver.web.shipment.shipmentDetails.shipmentPanes.ShipmentLinesPaneDriver.CompletedShipmentLine;
import com.mxi.driver.standard.Wait;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;


public class SendOrderInventoryReceivedQuarStepDefinitions {

   @Inject
   private OrderDetailsPageDriver iOrderDetailsDriver;

   @Inject
   private RequestAuthorizationPageDriver iRequestAuthorizationPageDriver;

   @Inject
   private ReceiveShipmentPageDriver iReceiveShipmentPageDriver;

   @Inject
   private SendOrderInventoryReceivedMessageDriver iSendOrderInventoryReceivedMessageDriver;

   @Inject
   private ShipmentDetailsPageDriver iShipmentDetailsPageDriver;

   @Inject
   private EditOrderLinesPageDriver iEditOrderLinesPageDriver;

   @Inject
   private ConfirmPageDriver iConfirmPageDriver;

   @Inject
   private InventoryDetailsPageDriver iInventoryDetailsPageDriver;

   @Inject
   private InspectAsServiceablePageDriver iInspectAsServiceablePageDriver;

   @Inject
   @AssetManagement
   private WebDriver iWebDriver;

   private final static String PAGE_NAME = "Shipment Details";

   private final static String RECEIVE_SHIPMENT_QTY = "Receive Shipment Quantity";
   private final static String PART_TYPE = "Part Type";
   private final static String UPDATED_QUANTITY = "Update Edit Order Lines Quantity";
   private final static String BATCH = "Batch";
   private final static String MANUFACTURED_DATE = "Manufactured Date";


   @Given( "^that I have received the order$" )
   public void iReceiveTheOrder( List<Map<String, String>> aDataTable ) throws Throwable {

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

      iReceiveShipmentPageDriver
            .setManufacturedDateLine1( aDataTable.get( 0 ).get( MANUFACTURED_DATE ) );

      iReceiveShipmentPageDriver.clickOk();

      Wait.until( AmWebConditions.isOnPage( iWebDriver, PAGE_NAME ), 30000 );
   }


   @When( "^I inspect the quarantine inventory$" )
   public void iInspectTheQuarantineInventory( List<Map<String, String>> aDataTable )
         throws Throwable {
      // navigate back to the PO Details page so that the order quantity may be increased
      iShipmentDetailsPageDriver.clickOrderNumber();

      // Click the Edit Lines button
      iOrderDetailsDriver.clickEditPOLines();

      // Get the Edit Order Lines table and update the Qty column for the FIRST (Index 0) row
      List<PartLine> lPartLine = iEditOrderLinesPageDriver.getPartLines();
      PartLine lPartLineRow = lPartLine.get( 0 );
      lPartLineRow.setPartQuantity( aDataTable.get( 0 ).get( UPDATED_QUANTITY ) );
      iEditOrderLinesPageDriver.clickOK();
      iConfirmPageDriver.clickYes();

      // Navigate to the Receipts & Returns tab so that the Shipment page can be reached
      ReceiptReturnsPaneDriver lReceiptReturnsPaneDriver =
            iOrderDetailsDriver.clickTabReceiptReturns();

      // Get the Inbound Shipments table so that Inbound Shipment hyperlink for the FIRST (Index 0)
      // can be clicked
      List<InboundShipmentLine> lInboundShipmentLine =
            lReceiptReturnsPaneDriver.getInboundShipmentLines();
      InboundShipmentLine lInboundShipmentLineRow = lInboundShipmentLine.get( 0 );
      lInboundShipmentLineRow.clickInboundShipment();

      // Navigate to the inventory details page of the received part
      // Note that this has been hard coded to only click on the first row and hence does NOT
      // support inspecting multiple inventory items of different part numbers that have been all
      // received on the same shipment
      List<CompletedShipmentLine> lCompletedShipmentLineRow =
            iShipmentDetailsPageDriver.clickTabShipmentLines().getCompletedShipmentLines();
      CompletedShipmentLine lShipmentLineRow = lCompletedShipmentLineRow.get( 0 );
      lShipmentLineRow.clickSerialBatchNO();

      // Click the Inspect as Serviceable button on the Inventory Details page
      // The inventory that will be made serviceable are those that have Condition set to INSPREQ
      // and you this is the case since the current Inventory Details has NO Quarantine tab
      // Note that the inventory that have Condition QUAR (i.e. the quantity that was over received)
      // will need to be navigated to and made serviceable afterwards
      iInventoryDetailsPageDriver.clickInspectAsServiceable();

      // Un-check the Print Serviceable Part Tag check box and click OK
      iInspectAsServiceablePageDriver.printServiceablepartTag();
      iInspectAsServiceablePageDriver.clickOk();

      // Enter password into Authentication Required pop-up window and click OK
      iRequestAuthorizationPageDriver.setPassword( "password" );
      iRequestAuthorizationPageDriver.authenticate();

      // Get Batch-Controlled Inventory Information table so that the Inventory Details page of the
      // quarantined inventory can be navigated to
      // Note that this Inventory Details page WILL have a Quarantine tab
      List<BatchDistributionLine> lBatchDistributionLines =
            iInventoryDetailsPageDriver.clickTabDetails().getBatchDistributionLines();
      BatchDistributionLine lBatchDistributionLineRow = lBatchDistributionLines.get( 1 );
      lBatchDistributionLineRow.clickInventory();

      // Click the Inspect as Serviceable button on the Inventory Details page
      iInventoryDetailsPageDriver.clickInspectAsServiceable();

      // Un-check the Print Serviceable Part Tag check box and click OK
      iInspectAsServiceablePageDriver.printServiceablepartTag();
      iInspectAsServiceablePageDriver.clickOk();

      // Clear the ASB Notification Queue since we need to validate the received quantity is set to
      // the received quantity of the quarantine batch (the second batch that is inspected as
      // serviceable). Note that this clearing is required since the order received message from
      // inspecting the first batch as serviceable will be appear in the queue first and therefore
      // will be the message that the send order inventory received message driver uses for
      // validation unless it is cleared prior to inspecting the second batch
      // Note that this calling of the clear method is performed here since it can take a couple
      // seconds for the message to appear in the ASB Notification queue, hence this statement is
      // placed here instead of right after inspecting the first batch as serviceable and including
      // a wait.pause (3 seconds)
      iSendOrderInventoryReceivedMessageDriver.clear();

      // Enter password into Authentication Required pop-up window and click OK
      iRequestAuthorizationPageDriver.setPassword( "password" );
      iRequestAuthorizationPageDriver.authenticate();

      // Navigate back to the shipment lines page so that an existing Then statement step definition
      // can be used to validate the message contents in regards to Order Information
      iInventoryDetailsPageDriver.clickReceiveShipment();
   }
}
