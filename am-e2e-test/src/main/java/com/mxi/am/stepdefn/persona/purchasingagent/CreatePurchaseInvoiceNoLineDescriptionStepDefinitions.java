package com.mxi.am.stepdefn.persona.purchasingagent;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.dom4j.Document;
import org.junit.Assert;

import com.mxi.am.driver.integration.finance.receivecreatepurchaseinvoice.ReceiveCreatePurchaseInvoiceMessageDriver;
import com.mxi.am.driver.web.CommonMessagePageDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.inventory.inventorydetailspage.InventoryDetailsPageDriver;
import com.mxi.am.driver.web.po.AddMiscOrderLinePageDriver;
import com.mxi.am.driver.web.po.CreateEditOrderPageDriver;
import com.mxi.am.driver.web.po.EditOrderLinesPageDriver;
import com.mxi.am.driver.web.po.IssueOrderPageDriver;
import com.mxi.am.driver.web.po.OrderDetailsPageDriver;
import com.mxi.am.driver.web.po.POInvoiceDetailsPageDriver;
import com.mxi.am.driver.web.po.POInvoiceSearchPageDriver;
import com.mxi.am.driver.web.po.POInvoiceSearchPageDriver.POInvoiceSearchPageSearchResultRow;
import com.mxi.am.driver.web.po.RequestAuthorizationPageDriver;
import com.mxi.am.driver.web.po.poinvoicedetails.POInvoiceLinesPaneDriver;
import com.mxi.am.driver.web.po.poinvoicedetails.POInvoiceLinesPaneDriver.POInvoiceDetailsPOInvoiceLine;
import com.mxi.xml.xsd.core.finance.purchaseInvoiceCreated.x11.PurchaseInvoiceCreatedDocument;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class CreatePurchaseInvoiceNoLineDescriptionStepDefinitions {

   @Inject
   private InventoryDetailsPageDriver iInventoryDetailsPageDriver;

   @Inject
   private NavigationDriver iNavDriver;

   @Inject
   private POInvoiceSearchPageDriver iPOInvoiceSearchPageDriver;

   @Inject
   private CommonMessagePageDriver iCommonPage;

   @Inject
   private POInvoiceDetailsPageDriver iPODetailsPageDriver;

   @Inject
   private CreateEditOrderPageDriver iCreateEditOrderDriver;

   @Inject
   private OrderDetailsPageDriver iOrderDetailsDriver;

   @Inject
   private RequestAuthorizationPageDriver iRequestAuthorizationPageDriver;

   @Inject
   private IssueOrderPageDriver iIssueOrderPageDriver;

   @Inject
   private AddMiscOrderLinePageDriver iAddMiscOrderLinePageDriver;

   @Inject
   private EditOrderLinesPageDriver iEditOrderLinesPageDriver;

   // Variables to be used for assembling of Create Purchase Invoice
   private String iPONumber = "";
   private final static String INVOICE_NUMBER = "Invoice Number";
   private final static String INVOICE_DATE = "Invoice Date";
   private final static String CURRENCY = "Currency";
   private final static String LINE_NUMBER = "Line Number";
   private final static String PART_NUMBER = "Part Number";
   private final static String INVOICE_QUANTITY = "Invoice Quantity";
   private final static String INVOICE_QUANTITY_UNIT = "Invoice Quantity Unit";
   private final static String UNIT_PRICE = "Unit Price";
   private final static String ACCOUNT_CODE = "Account Code";
   private final static String PO_LINE_NUMBER = "PO Line Number";
   // Variables to be used to create Order
   private final static String SHIP_TO_LOCATION = "Ship To Location";
   private final static String VENDOR = "Vendor";
   private final static String MISC_LINE_DESC = "Miscellaneous Line Description";
   private final static String QUANTITY = "Quantity";
   private final static String ACCOUNT = "Account";
   // Variable used to search for Invoice created by the Create Purchase Invoice message
   private final static String PO_INVOICE_NUMBER = "PO Invoice Number";
   // Variables used to validate PO Invoice Details page
   private final static String PO_INVOICE_DATE = "PO Invoice Date";
   private final static String LINE_NO = "Line No";
   private final static String QTY = "Qty";


   @When( "^the external system completes three way matching and sends an VALID inbound Create Purchase Invoice message without Line Description$" )
   public void
         theExternalSystemCompletesThreeWayMatchingAndSendsAnVALIDInboundCreatePurchaseInvoiceMessageWithoutLineDescription(
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
            + "\r\n" + "<part-number>" + aDataTable.get( 0 ).get( PART_NUMBER ) + "</part-number>"
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


   @Given( "^that I have created an order with a miscellaneous line$" )
   public void thatIHaveCreatedAnOrderWithAMiscellaneousLine( List<Map<String, String>> aDataTable )
         throws Throwable {
      // Create Purchase Order with no Part Associated
      iCreateEditOrderDriver.setVendor( aDataTable.get( 0 ).get( VENDOR ) );
      // Click elsewhere on the screen so that the auto-logic associated to the Vendor field
      // completes and I can continue editing fields without having these changes rolled back
      iCreateEditOrderDriver.headerOrderInformation();
      iCreateEditOrderDriver.setShipTo( aDataTable.get( 0 ).get( SHIP_TO_LOCATION ) );
      iCreateEditOrderDriver.clickOK();

      // Add a Miscellaneous line to the Purchase Order
      iOrderDetailsDriver.clickAddMiscellaneousPOLine();
      iAddMiscOrderLinePageDriver.setDescription( aDataTable.get( 0 ).get( MISC_LINE_DESC ) );
      iAddMiscOrderLinePageDriver.setQuantity( aDataTable.get( 0 ).get( QUANTITY ) );
      iAddMiscOrderLinePageDriver.setUnitPrice( aDataTable.get( 0 ).get( UNIT_PRICE ) );
      iAddMiscOrderLinePageDriver.accountSearch( aDataTable.get( 0 ).get( ACCOUNT ) );
      iAddMiscOrderLinePageDriver.ok();

      // Click OK on the next page that appears which is the Edit Order Lines page
      iEditOrderLinesPageDriver.clickOK();

      // Request Authorization and Issue the Purchase Order:
      iOrderDetailsDriver.clickRequestAuthorization();
      iRequestAuthorizationPageDriver.clickOK();
      iRequestAuthorizationPageDriver.setPassword( "password" );
      iRequestAuthorizationPageDriver.authenticate();
      iOrderDetailsDriver.clickIssuePurchaseOrder();
      iIssueOrderPageDriver.clickOk();
   }


   @When( "^the external system completes two way matching and sends an VALID inbound Create Purchase Invoice message without Line Description$" )
   public void
         theExternalSystemCompletesTwoWayMatchingAndSendsAnVALIDInboundCreatePurchaseInvoiceMessageWithoutLineDescription(
               List<Map<String, String>> aDataTable ) throws Throwable {

      // Get the PO Number to be used within inbound Create Purchase Invoice message
      // It will be appended to the Invoice Number as to make the Invoice Number unique each time
      // the scenario is executed
      // It will also be used to fill in the PO Number field
      iPONumber = iOrderDetailsDriver.getPONumber();

      // Create Sample Message as a String that has NO Line Description and NO Part Number (and no
      // Vendor Information)
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


   @Then( "^an invoice with Line Description set to Order Miscellaneous Line Description is created and a Purchase Invoice Created message is sent$" )
   public void anInvoiceWithLineDescriptionIsCreatedAndAPurchaseInvoiceCreatedMessageIsSent(
         List<Map<String, String>> aDataTable ) throws Throwable {

      // Get the PO Number to be used when searching for the Invoice
      iPONumber = iOrderDetailsDriver.getPONumber();

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
               aDataTable.get( 0 ).get( LINE_NO ), lLineNo );

         // Validate Line Description
         String lLineDescription = lPOInvoiceLine.getLineDescription();
         Assert.assertEquals( "The PO Invoice Lines Line Description is incorrect",
               aDataTable.get( 0 ).get( MISC_LINE_DESC ), lLineDescription );

         // Validate Qty
         String lQty = lPOInvoiceLine.getQty();
         Assert.assertEquals( "The PO Invoice Lines Qty is incorrect",
               aDataTable.get( 0 ).get( QTY ), lQty );

         // Validate Purchase Order - PO Num
         String lPOPONum = lPOInvoiceLine.getPONumber();
         Assert.assertEquals( "The PO Invoice Lines Purchase Order - PO Num is incorrect",
               iPONumber, lPOPONum );

      }

      // Navigate to the Details tab
      // Validate PO Invoice Date
      String lPOInvoiceDate = iPODetailsPageDriver.clickTabDetails().getPOInvoiceDate();
      Assert.assertEquals( "The PO Invoice Date is incorrect",
            aDataTable.get( 0 ).get( PO_INVOICE_DATE ), lPOInvoiceDate );
   }
}
