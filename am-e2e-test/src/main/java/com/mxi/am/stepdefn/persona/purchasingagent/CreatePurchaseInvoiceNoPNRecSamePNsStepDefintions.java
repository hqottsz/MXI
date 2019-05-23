package com.mxi.am.stepdefn.persona.purchasingagent;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.dom4j.Document;
import org.openqa.selenium.WebDriver;

import com.mxi.am.driver.integration.finance.receivecreatepurchaseinvoice.ReceiveCreatePurchaseInvoiceMessageDriver;
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
import com.mxi.xml.xsd.core.finance.purchaseInvoiceCreated.x11.PurchaseInvoiceCreatedDocument;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;


// part_number element: When receiving only one shipment line against order line, or when received
// parts against an order line are all the same P/N
public class CreatePurchaseInvoiceNoPNRecSamePNsStepDefintions {

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

   // Variables to be used to create Order
   private final static String SHIP_TO_LOCATION = "Ship To Location";
   private final static String PART_NO = "Part No";
   private final static String STANDARD_QUANTITY = "Standard Quantity";
   private final static String STANDARD_UNIT = "Standard Unit";
   private final static String RECEIVE_SHIPMENT_QTY = "Receive Shipment Quantity";
   private final static String PART_TYPE = "Part Type";
   private final static String VENDOR = "Vendor";
   private final static String BATCH = "Batch";
   private final static String RECEIVED_PART_NO = "Received Part No";
   // Variables to be used for assembling of Create Purchase Invoice
   private String iPONumber = "";
   private final static String INVOICE_NUMBER = "Invoice Number";
   private final static String INVOICE_DATE = "Invoice Date";
   private final static String CURRENCY = "Currency";
   private final static String LINE_NUMBER = "Line Number";
   private final static String INVOICE_QUANTITY = "Invoice Quantity";
   private final static String INVOICE_QUANTITY_UNIT = "Invoice Quantity Unit";
   private final static String UNIT_PRICE = "Unit Price";
   private final static String ACCOUNT_CODE = "Account Code";
   private final static String PO_LINE_NUMBER = "PO Line Number";


   @Given( "^that I have received an order with all alternate parts and inspected the inventory as serviceable$" )
   public void thatIHaveReceivedAnOrderWithAllAlternatePartsAndInspectedTheInventoryAsServiceable(
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
      // Receive both the first and second shipment line as an Alternate Part
      iReceiveShipmentPageDriver.setOEMPartNo1( aDataTable.get( 0 ).get( RECEIVED_PART_NO ) );
      iReceiveShipmentPageDriver.setOEMPartNo2( aDataTable.get( 0 ).get( RECEIVED_PART_NO ) );
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


   @When( "^the external system completes three way matching and sends an VALID inbound Create Purchase Invoice message without Part Information$" )
   public void
         theExternalSystemCompletesThreeWayMatchingAndSendsAnVALIDInboundCreatePurchaseInvoiceMessageWithoutPartNumber(
               List<Map<String, String>> aDataTable ) throws Throwable {

      // Get the PO Number to be used within inbound Create Purchase Invoice message
      // It will be appended to the Invoice Number as to make the Invoice Number unique each time
      // the scenario is executed
      // It will also be used to fill in the PO Number field
      iPONumber = iInventoryDetailsPageDriver.clickTabDetails().getOrderNumber();

      // Create Sample Message as a String that has NO Line Description (and no Vendor Information)
      String lSampleMessage = "<soap:Envelope xmlns:soap=" + "\""
            + "http://schemas.xmlsoap.org/soap/envelope/" + "\"" + " xmlns:ns=" + "\""
            + "http://xml.mxi.com/xsd/integration/header/1.1" + "\"" + " xmlns:ns1=" + "\""
            + "http://xml.mxi.com/xsd/integration/request/1.1" + "\"" + ">" + "\r\n"
            + "<soap:Header>" + "\r\n" + "<ns:mode>sync</ns:mode>" + "\r\n" + "</soap:Header>"
            + "\r\n" + "<soap:Body>" + "\r\n" + "<ns1:request>" + "\r\n"
            + "<create-purchase-invoice xmlns=" + "\""
            + "http://xml.mxi.com/xsd/core/finance/create-purchase-invoice/3.0" + "\""
            + " xmlns:xsi=" + "\"" + "http://www.w3.org/2001/XMLSchema-instance" + "\"" + ">"
            + "\r\n" + "<invoice>" + "\r\n" + "<invoice-number>"
            + aDataTable.get( 0 ).get( INVOICE_NUMBER ) + iPONumber + "</invoice-number>" + "\r\n"
            + "<date>" + aDataTable.get( 0 ).get( INVOICE_DATE ) + "</date>" + "\r\n" + "<currency>"
            + aDataTable.get( 0 ).get( CURRENCY ) + "</currency>" + "\r\n" + "<invoice-line>"
            + "\r\n" + "<line-number>" + aDataTable.get( 0 ).get( LINE_NUMBER ) + "</line-number>"
            + "\r\n" + "<invoice-quantity>" + aDataTable.get( 0 ).get( INVOICE_QUANTITY )
            + "</invoice-quantity>" + "\r\n" + "<invoice-quantity-unit>"
            + aDataTable.get( 0 ).get( INVOICE_QUANTITY_UNIT ) + "</invoice-quantity-unit>" + "\r\n"
            + "<unit-price>" + aDataTable.get( 0 ).get( UNIT_PRICE ) + "</unit-price>" + "\r\n"
            + "<account>" + "\r\n" + "<code>" + aDataTable.get( 0 ).get( ACCOUNT_CODE ) + "</code>"
            + "\r\n" + "</account>" + "\r\n" + "<purchase-order>" + "\r\n" + "<po-number>"
            + iPONumber + "</po-number>" + "\r\n" + "<po-line-number>"
            + aDataTable.get( 0 ).get( PO_LINE_NUMBER ) + "</po-line-number>" + "\r\n"
            + "</purchase-order>" + "\r\n" + "</invoice-line>" + "\r\n" + "</invoice>" + "\r\n"
            + "</create-purchase-invoice>" + "\r\n" + "</ns1:request>" + "\r\n" + "</soap:Body>"
            + "\r\n" + "</soap:Envelope>";

      // Create the inbound Create Purchase Invoice message driver
      ReceiveCreatePurchaseInvoiceMessageDriver lMessageDriver =
            new ReceiveCreatePurchaseInvoiceMessageDriver( "mxintegration", "password" );

      // Send the Create Purchase Invoice message inbound
      // Note that the Response message is returned by the Send method and therefore must be
      // captured here. Hence the validation that a Purchase Invoice Created message is sent is
      // actually validated right here in this @When step as opposed to the @Then step which will be
      // ran next
      Document lResponse =
            lMessageDriver.sendLegacyMessage( "mxintegration", "password", lSampleMessage );

      // Convert the lResponse Document to that of a PurchaseInvoiceCreatedDocument
      // thereby validating that the Purchase Invoice Created message is sent
      // since an Exception would be thrown if the conversion could not be performed
      PurchaseInvoiceCreatedDocument lPurchaseInvoiceCreatedDocument =
            PurchaseInvoiceCreatedDocument.Factory.parse(
                  lResponse.getRootElement().element( "purchase-invoice-created" ).asXML() );
   }
}
