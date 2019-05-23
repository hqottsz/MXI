package com.mxi.am.stepdefn.persona.storeroomclerk;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Assert;

import com.mxi.am.driver.integration.finance.sendorderinventoryreceived.SendOrderInventoryReceivedMessageDriver;
import com.mxi.am.driver.web.po.CreateEditOrderPageDriver;
import com.mxi.am.driver.web.po.IssueOrderPageDriver;
import com.mxi.am.driver.web.po.OrderDetailsPageDriver;
import com.mxi.am.driver.web.po.RequestAuthorizationPageDriver;
import com.mxi.am.driver.web.shipment.ReceiveShipmentPageDriver;
import com.mxi.am.driver.web.shipment.ReceiveShipmentPageDriver.ShipmentLine;
import com.mxi.am.driver.web.shipment.shipmentDetails.ShipmentDetailsPageDriver;
import com.mxi.xml.xsd.core.finance.orderInventoryReceived.x10.OrderInventoryReceivedDocument;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class SendOrderInventoryReceivedStepDefinitions {

   @Inject
   public OrderDetailsPageDriver iOrderDetailsDriver;

   @Inject
   public RequestAuthorizationPageDriver iRequestAuthorizationPageDriver;

   @Inject
   public IssueOrderPageDriver iIssueOrderPageDriver;

   @Inject
   public CreateEditOrderPageDriver iCreateEditOrderDriver;

   @Inject
   public ReceiveShipmentPageDriver iReceiveShipmentPageDriver;

   @Inject
   public SendOrderInventoryReceivedMessageDriver iSendOrderInventoryReceivedMessageDriver;

   @Inject
   public ShipmentDetailsPageDriver iShipmentDetailsPageDriver;

   private final static String SER_PART_NO = "Serialized Part No";
   private final static String BATCH_PART_NO = "Batch Part No";
   private final static String SHIP_TO_LOCATION = "Ship To Location";
   private final static String TRANSPORTATION_TYPE = "Transportation Type";
   private final static String TERMS_AND_CONDITIONS = "Terms & Conditions";
   private final static String FREIGHT_ON_BAORD = "Freight On Board";
   private final static String PART_NO = "Part No";
   private final static String STANDARD_QUANTITY = "Standard Quantity";
   private final static String STANDARD_UNIT = "Standard Unit";
   private final static String VENDOR_QUANTITY = "Vendor Quantity";
   private final static String VENDOR_UNIT = "Vendor Unit";
   private final static String MANUFACTURER_CODE = "Manufacturer Code";
   private final static String RECEIVE_SHIPMENT_QTY = "Receive Shipment Quantity";
   private final static String ORDER_LINE_NUMBER = "Order Line Number";
   private final static String PART_TYPE = "Part Type";
   private final static String BATCH = "Batch";
   private final static String VENDOR_EX_KEY = "Vendor";


   @Given( "^that I have filled in a serialized part$" )
   public void thatIHaveFilledInASerializedPart( List<Map<String, String>> aDataTable )
         throws Throwable {
      iCreateEditOrderDriver.setPartNumber( aDataTable.get( 0 ).get( SER_PART_NO ) );
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


   @Given( "^that I have requested authorization and issued the order$" )
   public void thatIHaveRequestedAuthorizationAndIssuedTheOrder() throws Throwable {
      iOrderDetailsDriver.clickRequestAuthorization();
      iRequestAuthorizationPageDriver.clickOK();
      iRequestAuthorizationPageDriver.setPassword( "password" );
      iRequestAuthorizationPageDriver.authenticate();
      iOrderDetailsDriver.clickIssuePurchaseOrder();
      iIssueOrderPageDriver.clickOk();
   }


   @When( "^I receive the order$" )
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
      iReceiveShipmentPageDriver.clickOk();
   }


   @Then( "^Receipt Order message is sent and includes part information$" )
   public void receiptOrderMessageIsSentAndIncludesPartInformation(
         List<Map<String, String>> aDataTable ) throws Throwable {
      // Get PO number for later comparison to out bound message
      String lPONumber = iShipmentDetailsPageDriver.getOrderNumber();

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

         // Check if the PO Numbers match, If yes perform message contents validation
         // If no match, then return to while loop and receive next out bound message from queue
         if ( lPONumber.equals( lMessagePONumber ) ) {

            // Validate current date field is not null
            Assert.assertNotNull(
                  lOrderInventoryReceivedDoc.getOrderInventoryReceived().getCurrentDate() );

            // Validate the date format of current date field
            String lcurrentDate =
                  lOrderInventoryReceivedDoc.getOrderInventoryReceived().getCurrentDate();

            String lpattern = ( "yyyy-MM-dd HH:mm:ss" );

            SimpleDateFormat lDateFormat = new SimpleDateFormat( lpattern );
            lDateFormat.setLenient( false );

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

         }
      }
   }


   @Given( "^that I have filled in a serialized part using alternate purchase unit of measure$" )
   public void thatIHaveFilledInASerializedPartUsingAlternatePurchaseUnitOfMeasure(
         List<Map<String, String>> aDataTable ) throws Throwable {
      iCreateEditOrderDriver.setPartNumber( aDataTable.get( 0 ).get( SER_PART_NO ) );
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
      iCreateEditOrderDriver.setUnitOfMeasure( aDataTable.get( 0 ).get( VENDOR_UNIT ) );
      iCreateEditOrderDriver.setQuantity( aDataTable.get( 0 ).get( VENDOR_QUANTITY ) );
      iCreateEditOrderDriver.clickOK();
   }


   @Then( "^Receipt Order message is sent and includes order information$" )
   public void receiptOrderMessageIsSentAndIncludesOrderInformation(
         List<Map<String, String>> aDataTable ) throws Throwable {
      // Get PO number for later comparison to out bound message
      String lPONumber = iShipmentDetailsPageDriver.getOrderNumber();

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

         // Check if the PO Numbers match, If yes perform message contents validation
         // If no match, then return to while loop and receive next out bound message from queue
         if ( lPONumber.equals( lMessagePONumber ) ) {

            // Validate creation date field is not null
            Assert.assertNotNull(
                  lOrderInventoryReceivedDoc.getOrderInventoryReceived().getOrderCreationDate() );

            // Validate the date format of current date field
            String lcreationDate =
                  lOrderInventoryReceivedDoc.getOrderInventoryReceived().getOrderCreationDate();

            String lpattern = ( "yyyy-MM-dd HH:mm:ss" );

            SimpleDateFormat lDateFormat = new SimpleDateFormat( lpattern );
            lDateFormat.setLenient( false );

            try {
               lDateFormat.parse( lcreationDate.trim() );
            } catch ( ParseException pe ) {
               Assert.fail(
                     "The Creation Date field is not of the correct format yyyy-MM-dd HH:mm:ss" );
            }

            // Validate order line number
            String llineNumber =
                  lOrderInventoryReceivedDoc.getOrderInventoryReceived().getOrderLineNumber();
            Assert.assertEquals( aDataTable.get( 0 ).get( ORDER_LINE_NUMBER ), llineNumber );

            // Validate vendor quantity
            String lvendorQuantity = lOrderInventoryReceivedDoc.getOrderInventoryReceived()
                  .getVendorQuantity().toString();
            Assert.assertEquals( aDataTable.get( 0 ).get( VENDOR_QUANTITY ), lvendorQuantity );

            // Validate vendor unit
            String lvendorUnit =
                  lOrderInventoryReceivedDoc.getOrderInventoryReceived().getVendorUnit();
            Assert.assertEquals( aDataTable.get( 0 ).get( VENDOR_UNIT ), lvendorUnit );

            // Validate standard quantity
            String lstandardQuantity = lOrderInventoryReceivedDoc.getOrderInventoryReceived()
                  .getStandardQuantity().toString();
            Assert.assertEquals( aDataTable.get( 0 ).get( STANDARD_QUANTITY ), lstandardQuantity );

            // Validate standard unit
            String lstandardUnit =
                  lOrderInventoryReceivedDoc.getOrderInventoryReceived().getStandardUnit();
            Assert.assertEquals( aDataTable.get( 0 ).get( STANDARD_UNIT ), lstandardUnit );

            // Validate part number
            String lpartNumber =
                  lOrderInventoryReceivedDoc.getOrderInventoryReceived().getPartNumber();
            Assert.assertEquals( aDataTable.get( 0 ).get( PART_NO ), lpartNumber );
         }
      }

   }


   @Given( "^that I have filled in a batch part$" )
   public void thatIHaveFilledInABatchPart( List<Map<String, String>> aDataTable )
         throws Throwable {
      iCreateEditOrderDriver.setPartNumber( aDataTable.get( 0 ).get( BATCH_PART_NO ) );
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
}
