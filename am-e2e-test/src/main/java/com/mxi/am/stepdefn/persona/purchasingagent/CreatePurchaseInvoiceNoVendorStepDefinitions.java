package com.mxi.am.stepdefn.persona.purchasingagent;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.dom4j.Document;

import com.mxi.am.driver.integration.finance.receivecreatepurchaseinvoice.ReceiveCreatePurchaseInvoiceMessageDriver;
import com.mxi.am.driver.web.inventory.inventorydetailspage.InventoryDetailsPageDriver;
import com.mxi.xml.xsd.core.finance.purchaseInvoiceCreated.x11.PurchaseInvoiceCreatedDocument;

import cucumber.api.java.en.When;


public class CreatePurchaseInvoiceNoVendorStepDefinitions {

   @Inject
   public InventoryDetailsPageDriver iInventoryDetailsPageDriver;

   // Variables to be used for assembling of Create Purchase Invoice
   private String iPONumber = "";
   private final static String INVOICE_NUMBER = "Invoice Number";
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


   @When( "^the external system completes three way matching and sends an VALID inbound Create Purchase Invoice message without Vendor Information$" )
   public void
         theExternalSystemCompletesThreeWayMatchingAndSendsAnVALIDInboundCreatePurchaseInvoiceMessageWithoutVendorInformation(
               List<Map<String, String>> aDataTable ) throws Throwable {

      // Get the PO Number to be used within inbound Create Purchase Invoice message
      // It will be appended to the Invoice Number as to make the Invoice Number unique each time
      // the scenario is executed
      // It will also be used to fill in the PO Number field
      iPONumber = iInventoryDetailsPageDriver.clickTabDetails().getOrderNumber();

      // Create Sample Message as a String that has NO Vendor Information
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
            + "\r\n" + "<line-description>" + aDataTable.get( 0 ).get( LINE_DESCRIPTION )
            + "</line-description>" + "\r\n" + "<part-number>"
            + aDataTable.get( 0 ).get( PART_NUMBER ) + "</part-number>" + "\r\n"
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
}
