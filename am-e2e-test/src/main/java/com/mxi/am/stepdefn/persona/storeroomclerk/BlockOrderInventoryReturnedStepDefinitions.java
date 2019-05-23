package com.mxi.am.stepdefn.persona.storeroomclerk;

import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;

import com.mxi.am.driver.integration.finance.sendorderinventoryreturned.SendOrderInventoryReturnedMessageDriver;
import com.mxi.am.driver.web.inventory.inventorydetailspage.InventoryDetailsPageDriver;
import com.mxi.am.driver.web.po.ClosePOPageDriver;
import com.mxi.am.driver.web.po.OrderDetailsPageDriver;
import com.mxi.am.driver.web.po.podetails.ReceiptReturnsPaneDriver;
import com.mxi.am.driver.web.po.podetails.ReceiptReturnsPaneDriver.InboundShipmentLine;
import com.mxi.am.driver.web.shipment.shipmentDetails.ShipmentDetailsPageDriver;
import com.mxi.am.driver.web.shipment.shipmentDetails.shipmentPanes.ShipmentLinesPaneDriver.CompletedShipmentLine;
import com.mxi.xml.xsd.core.finance.orderInventoryReturned.x10.OrderInventoryReturnedDocument;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;


public class BlockOrderInventoryReturnedStepDefinitions {

   @Inject
   private ShipmentDetailsPageDriver iShipmentDetailsPageDriver;

   @Inject
   private SendOrderInventoryReturnedMessageDriver iSendOrderInventoryReturnedMessageDriver;

   @Inject
   private InventoryDetailsPageDriver iInventoryDetailsPageDriver;

   @Inject
   private OrderDetailsPageDriver iOrderDetailsDriver;

   @Inject
   private ClosePOPageDriver iClosePODriver;


   @Given( "^that I have NOT inspected the inventory as serviceable$" )
   public void thatIHaveNOTInspectedTheInventoryAsServiceable() throws Throwable {
      // Navigate to the inventory details page of the received part
      // Note that this has been hard coded to only click on the first row and hence does NOT
      // support inspecting multiple inventory items of different part numbers that have been all
      // received on the same shipment
      List<CompletedShipmentLine> lCompletedShipmentLineRow =
            iShipmentDetailsPageDriver.clickTabShipmentLines().getCompletedShipmentLines();
      CompletedShipmentLine lShipmentLineRow = lCompletedShipmentLineRow.get( 0 );
      lShipmentLineRow.clickSerialBatchNO();
   }


   @Then( "^Order Inventory Returned message is NOT sent$" )
   public void orderInventoryReturnedMessageIsNOTSent() throws Throwable {
      // create variable of type OrderInventoryReturnedDocument to receive the out bound message
      // from web driver
      OrderInventoryReturnedDocument lOrderInventoryReturnedDoc =
            iSendOrderInventoryReturnedMessageDriver.receive();

      // Fail if the queue DOES return an Order Inventory Returned message
      if ( lOrderInventoryReturnedDoc != null ) {
         Assert.fail(
               "The ASB Notification Queue is NOT empty. An Order Inventory Returned message was found" );
      }
   }


   @Given( "^that I have closed the PO$" )
   public void thatIHaveClosedThePO() throws Throwable {
      iInventoryDetailsPageDriver.clickTabDetails().clickOrderField();
      iOrderDetailsDriver.clickCloseOrder();
      iClosePODriver.ok();

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

   }
}
