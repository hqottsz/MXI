package com.mxi.am.stepdefn.persona.purchasingagent;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.dom4j.Document;
import org.junit.Assert;

import com.mxi.am.driver.integration.finance.receivecreatepurchaseinvoice.ReceiveCreatePurchaseInvoiceMessageDriver;
import com.mxi.am.driver.web.po.OrderDetailsPageDriver;

import cucumber.api.java.en.When;


public class CreatePurchaseInvoiceMiscLineExceptionsStepDefinitions {

   @Inject
   public OrderDetailsPageDriver iOrderDetailsDriver;

   // Variables to be used for assembling and searching of Create Purchase Invoice
   private String iPONumber = "";
   private String lSampleMessage = "";
   private final static String INVOICE_NUMBER = "Invoice Number";
   private final static String INVOICE_DATE = "Invoice Date";
   private final static String CURRENCY = "Currency";
   private final static String LINE_NUMBER = "Line Number";
   private final static String INVOICE_QUANTITY = "Invoice Quantity";
   private final static String UNIT_PRICE = "Unit Price";
   private final static String ACCOUNT_CODE = "Account Code";
   private final static String PO_LINE_NUMBER = "PO Line Number";
   private final static String INVALID_SCENARIO_MODE = "Invalid Message Fields";
   // Variables to be used for validating Error Repsonse message
   private final static String ERROR_RESPONSE_MESSAGE = "Error Message";
   private final static String NAMESPACE = "Namespace";


   // This When method covers EXCEPTION scenarios for Purchase Order MISCELLANEOUS Lines (2 way
   // matching)
   // The created message depends always on the INVALID_SCENARIO_MODE that checks the contents under
   // the Invalid Message Fields column of the data table on the respective Feature File Scenario's
   // When Step
   // This When method covers the following scenarios:
   // "No Invoice Quantity Unit and Invalid Order Info"
   @When( "^the external system completes two way matching and sends an INVALID inbound Create Purchase Invoice message$" )
   public void
         theExternalSystemCompletesTwoWayMatchingAndSendsAnINVALIDInboundCreatePurchaseInvoiceMessage(
               List<Map<String, String>> aDataTable ) throws Throwable {

      if ( aDataTable.get( 0 ).get( INVALID_SCENARIO_MODE )
            .equalsIgnoreCase( "No Invoice Quantity Unit and Invalid Order Info" ) ) {
         // Get the PO Number to be used within inbound Create Purchase Invoice message
         // It will be appended to the Invoice Number as to make the Invoice Number unique each time
         // the scenario is executed
         // It will also be used to fill in the PO Number field
         iPONumber = iOrderDetailsDriver.getPONumber();

         // Create Sample Message as a String that has NO Invoice Quantity Unit and INVALID Order
         // Info (and No Vendor nor Line Description nor Part Information)
         lSampleMessage = "<soap:Envelope xmlns:soap=" + "\""
               + "http://schemas.xmlsoap.org/soap/envelope/" + "\"" + " xmlns:ns=" + "\""
               + "http://xml.mxi.com/xsd/integration/header/1.1" + "\"" + " xmlns:ns1=" + "\""
               + "http://xml.mxi.com/xsd/integration/request/1.1" + "\"" + ">" + "\r\n"
               + "<soap:Header>" + "\r\n" + "<ns:mode>sync</ns:mode>" + "\r\n" + "</soap:Header>"
               + "\r\n" + "<soap:Body>" + "\r\n" + "<ns1:request>" + "\r\n"
               + "<create-purchase-invoice xmlns=" + "\""
               + "http://xml.mxi.com/xsd/core/finance/create-purchase-invoice/3.0" + "\""
               + " xmlns:xsi=" + "\"" + "http://www.w3.org/2001/XMLSchema-instance" + "\"" + ">"
               + "\r\n" + "<invoice>" + "\r\n" + "<invoice-number>"
               + aDataTable.get( 0 ).get( INVOICE_NUMBER ) + iPONumber + "</invoice-number>"
               + "\r\n" + "<date>" + aDataTable.get( 0 ).get( INVOICE_DATE ) + "</date>" + "\r\n"
               + "<currency>" + aDataTable.get( 0 ).get( CURRENCY ) + "</currency>" + "\r\n"
               + "<invoice-line>" + "\r\n" + "<line-number>"
               + aDataTable.get( 0 ).get( LINE_NUMBER ) + "</line-number>" + "\r\n"
               + "<invoice-quantity>" + aDataTable.get( 0 ).get( INVOICE_QUANTITY )
               + "</invoice-quantity>" + "\r\n" + "<unit-price>"
               + aDataTable.get( 0 ).get( UNIT_PRICE ) + "</unit-price>" + "\r\n" + "<account>"
               + "\r\n" + "<code>" + aDataTable.get( 0 ).get( ACCOUNT_CODE ) + "</code>" + "\r\n"
               + "</account>" + "\r\n" + "<purchase-order>" + "\r\n" + "<po-number>" + iPONumber
               + "</po-number>" + "\r\n" + "<po-line-number>"
               + aDataTable.get( 0 ).get( PO_LINE_NUMBER ) + "</po-line-number>" + "\r\n"
               + "</purchase-order>" + "</invoice-line>" + "\r\n" + "</invoice>" + "\r\n"
               + "</create-purchase-invoice>" + "\r\n" + "</ns1:request>" + "\r\n" + "</soap:Body>"
               + "\r\n" + "</soap:Envelope>";
      }

      // Create the inbound Create Purchase Invoice message driver
      ReceiveCreatePurchaseInvoiceMessageDriver lMessageDriver =
            new ReceiveCreatePurchaseInvoiceMessageDriver( "mxintegration", "password" );

      // Send the Create Purchase Invoice message inbound
      // Note that the Response message is returned by the Send method and therefore must be
      // captured here. Hence the validation that an Error Response message is sent is
      // actually validated right here in this @When step as opposed to the @Then step which will be
      // ran next
      Document lResponse =
            lMessageDriver.sendLegacyMessage( "mxintegration", "password", lSampleMessage );

      String lStatus = lResponse.getRootElement().attribute( "msg_status" ).getValue();
      Assert.assertEquals( "The Status contained in the Error Response is NOT as expected", "ERROR",
            lStatus );

      String lNamespace =
            lResponse.getRootElement().element( "errors" ).getNamespace().getStringValue();

      Assert.assertEquals( "The Namespace contained in the Error Response is NOT as expected",
            aDataTable.get( 0 ).get( NAMESPACE ), lNamespace );

      String lMessage = lResponse.getRootElement().element( "errors" ).element( "error" )
            .element( "message" ).getStringValue();
      Assert.assertEquals( "The Message contained in the Error Response is NOT as expected",
            aDataTable.get( 0 ).get( ERROR_RESPONSE_MESSAGE ), lMessage );
   }
}
