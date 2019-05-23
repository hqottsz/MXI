package com.mxi.am.stepdefn.persona.purchasingagent;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Assert;

import com.mxi.am.driver.integration.finance.sendorderinformation.SendOrderInformationMessageDriver;
import com.mxi.am.driver.web.inventory.inventorydetailspage.InventoryDetailsPageDriver;
import com.mxi.am.driver.web.shipment.shipmentDetails.ShipmentDetailsPageDriver;
import com.mxi.xml.xsd.core.finance.sendOrderInformation.x40.SendOrderInformationDocument;

import cucumber.api.java.en.Then;


public class BlockSendOrderInformationStepDefintions {

   @Inject
   private SendOrderInformationMessageDriver iSendOrderInformationMessageDriver;

   @Inject
   private ShipmentDetailsPageDriver iShipmentDetailsPageDriver;

   @Inject
   private InventoryDetailsPageDriver iInventoryDetailsPageDriver;

   private final static String ON_SHIPMENT_PAGE = "On Shipment Page";


   @Then( "^Send Order Inventory message version four with action \"([^\"]*)\" is NOT sent$" )
   public void sendOrderInventoryMessageVersionFourWithActionIsNOTSent( String aActionType,
         List<Map<String, String>> aDataTable ) throws Throwable {

      // Create variable to hold value of PO number from GUI
      String lPONumber = "";

      // Get PO number (from respective currently displayed page) for later comparison to out bound
      // message
      if ( ( aDataTable.get( 0 ).get( ON_SHIPMENT_PAGE ).equalsIgnoreCase( "TRUE" ) ) ) {
         lPONumber = iShipmentDetailsPageDriver.getOrderNumber();
      } else if ( aDataTable.get( 0 ).get( ON_SHIPMENT_PAGE ).equalsIgnoreCase( "FALSE" ) ) {
         lPONumber = iInventoryDetailsPageDriver.clickTabDetails().getOrderNumber();
      }

      // Create variable to hold value of PO number from out bound message
      String lMessagePONumber = "";

      // Create variable to hold value of action field from out bound message
      String lAction = "";

      Boolean lQueueIsNull = false;

      // Keep receiving the next Send Order Information message from the queue until a PO Number
      // match is found and action field is equal to "wild card" value of @Then statement (i.e.
      // aActionType from Feature File)
      // OR the queue is empty
      while ( lQueueIsNull == false ) {

         // create variable of type SendOrderInformationDocument to receive the out bound message
         // from web driver
         SendOrderInformationDocument lSendOrderInformationDoc =
               iSendOrderInformationMessageDriver.receive();

         // Return lQueueIsNull as True if the queue is empty
         if ( lSendOrderInformationDoc == null ) {
            // The ASB Notification has not found any matching PO Number with Action equal to Create
            // and therefore the Send Order Information message has successfully been blocked
            // Set boolean to true to jump out of while loop
            lQueueIsNull = true;
         } else {
            // Get the PO number from the out bound message
            lMessagePONumber = lSendOrderInformationDoc.getSendOrderInformation().getOrderArray( 0 )
                  .getOrderNumber();
            if ( lPONumber.equalsIgnoreCase( lMessagePONumber ) ) {
               // Validate Action field based upon given aActionType from Feature File
               lAction = lSendOrderInformationDoc.getSendOrderInformation().getAction().toString();
               if ( lAction.equalsIgnoreCase( aActionType ) ) {
                  Assert.fail(
                        "The ASB Notification Queue is NOT empty. A Send Order Information message with matching PO Number and ActionType was found" );
               }
            }
         }
      }
   }
}
