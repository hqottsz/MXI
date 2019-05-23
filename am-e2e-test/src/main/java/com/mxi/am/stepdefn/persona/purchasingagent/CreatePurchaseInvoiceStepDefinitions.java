package com.mxi.am.stepdefn.persona.purchasingagent;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.dom4j.Document;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;

import com.mxi.am.driver.integration.finance.receivecreatepurchaseinvoice.ReceiveCreatePurchaseInvoiceMessageDriver;
import com.mxi.am.driver.web.AmWebConditions;
import com.mxi.am.driver.web.AssetManagement;
import com.mxi.am.driver.web.CommonMessagePageDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.common.ClosePageDriver;
import com.mxi.am.driver.web.inventory.InspectAsServiceablePageDriver;
import com.mxi.am.driver.web.inventory.inventorydetailspage.InventoryDetailsPageDriver;
import com.mxi.am.driver.web.po.CreateEditOrderPageDriver;
import com.mxi.am.driver.web.po.IssueOrderPageDriver;
import com.mxi.am.driver.web.po.OrderDetailsPageDriver;
import com.mxi.am.driver.web.po.POInvoiceDetailsPageDriver;
import com.mxi.am.driver.web.po.POInvoiceSearchPageDriver;
import com.mxi.am.driver.web.po.POInvoiceSearchPageDriver.POInvoiceSearchPageSearchResultRow;
import com.mxi.am.driver.web.po.RequestAuthorizationPageDriver;
import com.mxi.am.driver.web.po.poinvoicedetails.POInvoiceLinesPaneDriver;
import com.mxi.am.driver.web.po.poinvoicedetails.POInvoiceLinesPaneDriver.POInvoiceDetailsPOInvoiceLine;
import com.mxi.am.driver.web.shipment.ReceiveShipmentPageDriver;
import com.mxi.am.driver.web.shipment.ReceiveShipmentPageDriver.ShipmentLine;
import com.mxi.am.driver.web.shipment.shipmentDetails.ShipmentDetailsPageDriver;
import com.mxi.am.driver.web.shipment.shipmentDetails.shipmentPanes.ShipmentLinesPaneDriver;
import com.mxi.am.driver.web.shipment.shipmentDetails.shipmentPanes.ShipmentLinesPaneDriver.CompletedShipmentLine;
import com.mxi.driver.standard.Wait;
import com.mxi.xml.xsd.core.finance.purchaseInvoiceCreated.x11.PurchaseInvoiceCreatedDocument;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class CreatePurchaseInvoiceStepDefinitions {

   @Inject
   private CreateEditOrderPageDriver iCreateEditOrderDriver;

   @Inject
   private OrderDetailsPageDriver iOrderDetailsDriver;

   @Inject
   private RequestAuthorizationPageDriver iRequestAuthorizationPageDriver;

   @Inject
   private IssueOrderPageDriver iIssueOrderPageDriver;

   @Inject
   private ReceiveShipmentPageDriver iReceiveShipmentPageDriver;

   @Inject
   private ShipmentDetailsPageDriver iShipmentDetailsPageDriver;

   @Inject
   private InventoryDetailsPageDriver iInventoryDetailsPageDriver;

   @Inject
   private InspectAsServiceablePageDriver iInspectAsServiceablePageDriver;

   @Inject
   private NavigationDriver iNavDriver;

   @Inject
   private POInvoiceSearchPageDriver iPOInvoiceSearchPageDriver;

   @Inject
   private CommonMessagePageDriver iCommonPage;

   @Inject
   private POInvoiceDetailsPageDriver iPODetailsPageDriver;

   @Inject
   private SendOrderInformationStepDefinitions xSendOrderInformationStepDefinitions;

   @Inject
   private ClosePageDriver iClosePageDriver;

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
   private final static String TOTAL_RECEIVE_SHIPMENT_QTY = "Total Receive Shipment Quantity";
   private final static String PART_TYPE = "Part Type";
   private final static String VENDOR = "Vendor";
   private final static String BATCH = "Batch";
   // Variables to be used for assembling of Create Purchase Invoice
   private String iPONumber = "";
   private final static String INVOICE_NUMBER = "Invoice Number";
   private final static String VENDOR_CODE = "Vendor Code";
   private final static String VENDOR_NAME = "Vendor Name";
   private final static String INVOICE_DATE = "Invoice Date";
   private final static String CURRENCY = "Currency";
   private final static String LINE_NUMBER = "Line Number";
   private final static String LINE_DESCRIPTION = "Line Description";
   private final static String PART_NUMBER = "Part Number";
   private final static String INVOICE_QUANTITY = "Invoice Quantity";
   private final static String INVOICE_QUANTITY_UNIT = "Invoice Quantity Unit";
   private final static String UNIT_PRICE = "Unit Price";
   private final static String ACCOUNT_CODE = "Account Code";
   private final static String PO_LINE_NUMBER = "PO Line Number";
   // Variable used to search for Invoice created by the Create Purchase Invoice message
   private final static String PO_INVOICE_NUMBER = "PO Invoice Number";
   // Variables used to validate PO Invoice Details page
   private final static String PO_INVOICE_DATE = "PO Invoice Date";
   private final static String LINE_NO = "Line No";
   private final static String QTY = "Qty";
   private final static String CHARGE_TO_ACCOUNT = "Charge To Account";
   private final static String ORDER_STATUS = "Order Status";


   @Given( "^that I have received an order and inspected the inventory as serviceable$" )
   public void thatIHaveReceivedAnOrderAndInspectedTheInventoryAsServiceable(
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

      // Strings to later check if all Shipment Lines are to be received
      // If not, only receive the first Shipment Line
      // Also, If not, only inspect the first received Inventory as Serviceable
      String ltempOrderedStandardQty = aDataTable.get( 0 ).get( STANDARD_QUANTITY );
      String ltempTotalReceiveQty = aDataTable.get( 0 ).get( TOTAL_RECEIVE_SHIPMENT_QTY );

      // Create a local variable to track Shipment Line row
      int lrowNumber = 0;

      for ( ShipmentLine lShipmentLine : lShipmentLinesAmmendment ) {

         if ( lrowNumber == 0 ) {
            if ( ( aDataTable.get( lrowNumber ).get( PART_TYPE ) ).equals( BATCH ) ) {
               // do not generate the serial number, but set the quantity
               lShipmentLine.setQty( aDataTable.get( lrowNumber ).get( RECEIVE_SHIPMENT_QTY ) );
            } else {
               // generate a serial number and set the quantity
               lShipmentLine.generateSerial();
               lShipmentLine.setQty( aDataTable.get( lrowNumber ).get( RECEIVE_SHIPMENT_QTY ) );
            }
         } else {
            if ( ltempOrderedStandardQty.equalsIgnoreCase( ltempTotalReceiveQty ) ) {
               if ( ( aDataTable.get( lrowNumber ).get( PART_TYPE ) ).equals( BATCH ) ) {
                  // do not generate the serial number
               } else {
                  // generate a serial number
                  lShipmentLine.generateSerial();
               }
            }
            // always set the quantity
            lShipmentLine.setQty( aDataTable.get( lrowNumber ).get( RECEIVE_SHIPMENT_QTY ) );
         }
         lrowNumber++;

      }

      iReceiveShipmentPageDriver.clickOk();

      // Inspect the Inventory as Serviceable:
      ShipmentLinesPaneDriver lShipmentDetailsPaneDriver =
            iShipmentDetailsPageDriver.clickTabShipmentLines();
      List<CompletedShipmentLine> lCompletedShipmentLineRow =
            lShipmentDetailsPaneDriver.getCompletedShipmentLines();

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

         iInventoryDetailsPageDriver.clickTabDetails();

         if ( ltempOrderedStandardQty.equalsIgnoreCase( ltempTotalReceiveQty ) ) {
            // Do nothing and continue
         } else {
            // Jump out of For Loop since only one inventory has been received and needs to be
            // inspected as serviceable
            break;
         }
         if ( i != lTotalRows - 1 ) {
            iInventoryDetailsPageDriver.clickClose();
            // Get the list of Shipment Lines again since the object has gone stale after
            // leaving and returning to the page
            lCompletedShipmentLineRow = lShipmentDetailsPaneDriver.getCompletedShipmentLines();
         }
      }
   }


   @When( "^the external system completes three way matching and sends an VALID inbound Create Purchase Invoice message$" )
   public void
         theExternalSystemCompletesThreeWayMatchingAndSendsAnVALIDInboundCreatePurchaseInvoiceMessage(
               List<Map<String, String>> aDataTable ) throws Throwable {

      // Get the PO Number to be used within inbound Create Purchase Invoice message
      // It will be appended to the Invoice Number as to make the Invoice Number unique each time
      // the scenario is executed
      // It will also be used to fill in the PO Number field
      iPONumber = iInventoryDetailsPageDriver.clickTabDetails().getOrderNumber();

      // Create Sample Message as a String which HAS Vendor and Purchase Order Information
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
            + "<vendor>" + "<code>" + aDataTable.get( 0 ).get( VENDOR_CODE ) + "</code>" + "\r\n"
            + "<name>" + aDataTable.get( 0 ).get( VENDOR_NAME ) + "</name>" + "\r\n" + "</vendor>"
            + "\r\n" + "<date>" + aDataTable.get( 0 ).get( INVOICE_DATE ) + "</date>" + "\r\n"
            + "<currency>" + aDataTable.get( 0 ).get( CURRENCY ) + "</currency>" + "\r\n"
            + "<invoice-line>" + "\r\n" + "<line-number>" + aDataTable.get( 0 ).get( LINE_NUMBER )
            + "</line-number>" + "\r\n" + "<line-description>"
            + aDataTable.get( 0 ).get( LINE_DESCRIPTION ) + "</line-description>" + "\r\n"
            + "<part-number>" + aDataTable.get( 0 ).get( PART_NUMBER ) + "</part-number>" + "\r\n"
            + "<invoice-quantity>" + aDataTable.get( 0 ).get( INVOICE_QUANTITY )
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


   @Then( "^an invoice is created and a Purchase Invoice Created message is sent$" )
   public void anInvoiceIsCreatedAndAPurchaseInvoiceCreatedMessageIsSent(
         List<Map<String, String>> aDataTable ) throws Throwable {

      // Local variable to be used when validating Invoice Line PO Num
      String iPrevPONumber = "";

      // Get the PO Number to be used when searching for the Invoice
      iPONumber = iInventoryDetailsPageDriver.clickTabDetails().getOrderNumber();

      // Navigate from the Inventory Details page to the PO Invoice Search Page
      iNavDriver.navigate( "Purchasing Manager", "PO Invoice Search" );
      Assert.assertEquals( "PO Invoice Search", iCommonPage.getTitle() );

      // Clear the fields on the PO Invoice Search page
      iPOInvoiceSearchPageDriver.clearAll();

      // Set the PO Invoice Number field
      iPOInvoiceSearchPageDriver
            .setPOInvoiceNumber( ( aDataTable.get( 0 ).get( PO_INVOICE_NUMBER ) ) + iPONumber );

      // Click the Search button
      iPOInvoiceSearchPageDriver.search();

      // Get the list of PO Invoice results
      List<POInvoiceSearchPageSearchResultRow> lPOInvoiceResultRow =
            iPOInvoiceSearchPageDriver.getSearchResults();

      // Get the size of the returned List
      Integer lsizePOInvoiceResultRow = lPOInvoiceResultRow.size();

      // fail if there is no Result Returned. That is, if the returned List has a size of zero
      if ( lsizePOInvoiceResultRow == 0 ) {
         Assert.fail(
               "The Search resulted in 0 returned PO Invoices. The Invoice specified does not exist in system" );
      }

      // Get the first row and click on the PO Invoice Number hyperlink
      POInvoiceSearchPageSearchResultRow lResultRow = lPOInvoiceResultRow.get( 0 );
      lResultRow.clickPOInvoiceNumberSearchResultRow();

      // Validate Vendor
      String lPOVendor = iPODetailsPageDriver.getVendor();
      Assert.assertEquals( "The Vendor associated to this Invoice is incorrect",
            aDataTable.get( 0 ).get( VENDOR ), lPOVendor );

      // Navigate to the PO Invoice Lines tab
      POInvoiceLinesPaneDriver lPOinvoiceLinesPaneDriver =
            iPODetailsPageDriver.clickTabPOInvoiceLines();

      // Get the list of PO Invoice Lines
      List<POInvoiceDetailsPOInvoiceLine> lPOInvoiceLines =
            lPOinvoiceLinesPaneDriver.getPOInvoiceLines();

      for ( int i = 0; i <= lPOInvoiceLines.size() - 2; i++ ) {
         POInvoiceDetailsPOInvoiceLine lPOInvoiceLine =
               ( POInvoiceDetailsPOInvoiceLine ) lPOInvoiceLines.toArray()[i];

         // Validate Line No
         String lLineNo = lPOInvoiceLine.getLineNo();
         Assert.assertEquals( "The PO Invoice Lines Line No is incorrect",
               aDataTable.get( i ).get( LINE_NO ), lLineNo );

         // Validate Line Description
         String lLineDescription = lPOInvoiceLine.getLineDescription();
         Assert.assertEquals( "The PO Invoice Lines Line Description is incorrect",
               aDataTable.get( i ).get( LINE_DESCRIPTION ), lLineDescription );

         // Validate Qty
         String lQty = lPOInvoiceLine.getQty();
         Assert.assertEquals( "The PO Invoice Lines Qty is incorrect",
               aDataTable.get( i ).get( QTY ), lQty );

         // Validate Charge To Account
         String lChargeToAccount = lPOInvoiceLine.getAccount();
         Assert.assertEquals( "The PO Invoice Lines Charge To Account is incorrect",
               aDataTable.get( i ).get( CHARGE_TO_ACCOUNT ), lChargeToAccount );

         if ( i >= 1 ) {
            // Validate Purchase Order - PO Num
            // This method assumes that if the Invoice contains more than 1 Invoice Line
            // then each Invoice Line must be associated to a different Purchase Order
            // and that the Purchase Order Numbers are sequential and increasing
            // for each additional row. Hence when sending in bound create purchase invoice
            // message, you must correctly order your Purchase Order numbers

            // Takes current PO Number and subtracts 1 for each additional row
            iPrevPONumber = ( iPONumber.replaceFirst( "P0", "" ) );
            int tempPrevPONumber = Integer.parseInt( iPrevPONumber.trim() );
            tempPrevPONumber = tempPrevPONumber - 1;
            iPrevPONumber = Integer.toString( tempPrevPONumber );
            iPrevPONumber = "P0" + iPrevPONumber;

            String lPOPONum = lPOInvoiceLine.getPONumber();
            Assert.assertEquals( "The PO Invoice Lines Purchase Order - PO Num is incorrect",
                  iPrevPONumber, lPOPONum );
         } else {
            String lPOPONum = lPOInvoiceLine.getPONumber();
            Assert.assertEquals( "The PO Invoice Lines Purchase Order - PO Num is incorrect",
                  iPONumber, lPOPONum );
         }

         lPOInvoiceLine.clickPONumber();
         iOrderDetailsDriver.getStatus();
         Assert.assertEquals( "The PO Invoice Lines Charge To Account is incorrect",
               aDataTable.get( i ).get( ORDER_STATUS ), iOrderDetailsDriver.getStatus() );
         iClosePageDriver.close();

         // Get the list of PO Invoice Lines again since the object has gone stale after leaving and
         // returning to the page
         lPOInvoiceLines = lPOinvoiceLinesPaneDriver.getPOInvoiceLines();
      }

      // Navigate to the Details tab
      // Validate PO Invoice Date
      String lPOInvoiceDate = iPODetailsPageDriver.clickTabDetails().getPOInvoiceDate();
      Assert.assertEquals( "The PO Invoice Date is incorrect",
            aDataTable.get( 0 ).get( PO_INVOICE_DATE ), lPOInvoiceDate );
   }


   @Given( "^that I have received both orders and inspected both inventory as serviceable$" )
   public void thatIHaveReceivedBothOrdersAndInspectedBothInventoryAsServiceable(
         List<Map<String, String>> aDataTable ) throws Throwable {

      // Create the 1st Purchase Order
      thatIHaveReceivedAnOrderAndInspectedTheInventoryAsServiceable( aDataTable );

      Wait.until( AmWebConditions.isOnPage( iWebDriver, PAGE_NAME ), 30000 );

      // Navigate back to the Create PO Page
      xSendOrderInformationStepDefinitions.getCreateOrderPage();

      // Create the 2nd Purchase Order
      thatIHaveReceivedAnOrderAndInspectedTheInventoryAsServiceable( aDataTable );
   }


   @When( "^the external system completes three way matching and sends an VALID inbound Create Purchase Invoice message with two Invoice Lines$" )
   public void
         theExternalSystemCompletesThreeWayMatchingAndSendsAnVALIDInboundCreatePurchaseInvoiceMessageTwoInvoiceLines(
               List<Map<String, String>> aDataTable ) throws Throwable {

      // Get the PO Number to be used within inbound Create Purchase Invoice message
      // It will be appended to the Invoice Number as to make the Invoice Number unique each time
      // the scenario is executed
      // It will also be used to fill in the PO Number field
      iPONumber = iInventoryDetailsPageDriver.clickTabDetails().getOrderNumber();

      // Takes current PO Number and subtracts 1 to get previous PO Number
      String iPrevPONumber = ( iPONumber.replaceFirst( "P0", "" ) );
      int tempPrevPONumber = Integer.parseInt( iPrevPONumber.trim() );
      tempPrevPONumber = tempPrevPONumber - 1;
      iPrevPONumber = Integer.toString( tempPrevPONumber );
      iPrevPONumber = "P0" + iPrevPONumber;

      // Create Sample Message as a String that has NO Invoice Quantity Unit (and no
      // Vendor Information nor Part Information nor Line Description)
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
            + "</invoice-quantity>" + "\r\n" + "<unit-price>"
            + aDataTable.get( 0 ).get( UNIT_PRICE ) + "</unit-price>" + "\r\n" + "<purchase-order>"
            + "\r\n" + "<po-number>" + iPONumber + "</po-number>" + "\r\n" + "<po-line-number>"
            + aDataTable.get( 0 ).get( PO_LINE_NUMBER ) + "</po-line-number>" + "\r\n"
            + "</purchase-order>" + "\r\n" + "</invoice-line>" + "\r\n" + "<invoice-line>" + "\r\n"
            + "<line-number>" + aDataTable.get( 0 ).get( LINE_NUMBER ) + "</line-number>" + "\r\n"
            + "<invoice-quantity>" + aDataTable.get( 0 ).get( INVOICE_QUANTITY )
            + "</invoice-quantity>" + "\r\n" + "<unit-price>"
            + aDataTable.get( 0 ).get( UNIT_PRICE ) + "</unit-price>" + "\r\n" + "<purchase-order>"
            + "\r\n" + "<po-number>" + iPrevPONumber + "</po-number>" + "\r\n" + "<po-line-number>"
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
