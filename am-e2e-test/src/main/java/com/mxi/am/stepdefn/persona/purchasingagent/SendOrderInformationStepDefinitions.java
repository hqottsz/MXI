package com.mxi.am.stepdefn.persona.purchasingagent;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.inject.Inject;

import org.junit.Assert;

import com.mxi.am.driver.integration.finance.sendorderinformation.SendOrderInformationMessageDriver;
import com.mxi.am.driver.web.CommonMessagePageDriver;
import com.mxi.am.driver.web.LoginPageDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.part.SubmitReasonAndNotesPageDriver;
import com.mxi.am.driver.web.po.AOGAuthorizeOrderPageDriver;
import com.mxi.am.driver.web.po.CreateEditOrderPageDriver;
import com.mxi.am.driver.web.po.IssueOrderPageDriver;
import com.mxi.am.driver.web.po.OrderDetailsPageDriver;
import com.mxi.am.driver.web.po.OrderSearchPageDriver;
import com.mxi.am.driver.web.po.RequestAuthorizationPageDriver;
import com.mxi.xml.xsd.core.finance.sendOrderInformation.x40.SendOrderInformationDocument;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


/**
 * Send Send Order Information Step Definitions
 *
 */
public class SendOrderInformationStepDefinitions {

   @Inject
   public CommonMessagePageDriver iCommonPage;

   @Inject
   public NavigationDriver iNavDriver;

   @Inject
   public OrderSearchPageDriver iOrderSearchDriver;

   @Inject
   public CreateEditOrderPageDriver iCreateEditOrderDriver;

   @Inject
   public OrderDetailsPageDriver iOrderDetailsDriver;

   @Inject
   public LoginPageDriver iLoginPage;

   @Inject
   public SubmitReasonAndNotesPageDriver iCancelPage;

   @Inject
   public SendOrderInformationMessageDriver iSendOrderInformationMessageDriver;

   @Inject
   public RequestAuthorizationPageDriver iRequestAuthorizationPageDriver;

   @Inject
   public IssueOrderPageDriver iIssueOrderPageDriver;

   @Inject
   public AOGAuthorizeOrderPageDriver iAOGAuthorizeOrderDriver;

   private final String SHIP_TO_LOCATION = "AIRPORT1/DOCK";
   private final String VENDOR_EX_KEY = "30001";
   private final String VENDOR_NO_EX_KEY_NONSPEC = "10002";
   private final String TRACKED_PART1 = "E0000017A";
   private final String TRACKED_PART2 = "E0000017B";


   @Given( "^that the Create Order page has been navigated to$" )
   public void getCreateOrderPage() throws Throwable {

      iNavDriver.navigate( "Purchasing Manager", "Order Search" );
      iOrderSearchDriver.clickTabOrderFound().clickCreatePO();

      Assert.assertEquals( "Create PO", iCommonPage.getTitle() );
   }


   @When( "^I fill in at least the mandatory fields and click the OK button$" )
   public void iFillInAtLeastTheMandatoryFieldsAndClickTheOKButton() throws Throwable {
      iCreateEditOrderDriver.setShipTo( SHIP_TO_LOCATION );
      iCreateEditOrderDriver.setVendor( VENDOR_NO_EX_KEY_NONSPEC );
      iCreateEditOrderDriver.setReExpediteLocation( "GBLDOCK" );
      iCreateEditOrderDriver.setTransportationType( "AIR (AIR)" );
      iCreateEditOrderDriver.setVendorNote( "We need this part yesterday" );
      iCreateEditOrderDriver.setTermsConditions( "NET30 (NET30)" );
      iCreateEditOrderDriver.setFreightOnBoard( "RECEIPT (RECEIPT)" );
      iCreateEditOrderDriver.clickOK();
   }


   @Then( "^PO is created$" )
   public void poIsCreated() throws Throwable {

      String lPONumber = iOrderDetailsDriver.getPONumber();

      Assert.assertNotNull(
            "No Order Number found on current page. Order may not have been created", lPONumber );
      Assert.assertEquals( "10002 (Vendor 2)", iOrderDetailsDriver.getVendor() );
      Assert.assertEquals( "OPEN (The order is new and/or being prepared.)",
            iOrderDetailsDriver.getStatus() );
   }


   @Given( "^that the part Vendor has an External Key$" )
   public void thatThePartVendorHasAnExternalKey() throws Throwable {
      iCreateEditOrderDriver.setPartNumber( TRACKED_PART1 );
      // click elsewhere on the screen so that the auto-logic associated to the Part No field
      // completes and I can continue editing fields without having these changes rolled back
      iCreateEditOrderDriver.headerOrderInformation();
      iCreateEditOrderDriver.setVendor( VENDOR_EX_KEY );

   }


   @When( "^I fill in at least the mandatory fields including a part and click the OK button$" )
   public void iFillInAtLeastTheMandatoryFieldsIncludingAPartAndClickTheOKButton()
         throws Throwable {
      iCreateEditOrderDriver.setTermsConditions( "NET30 (NET30)" );
      iCreateEditOrderDriver.setReExpediteLocation( "GBLDOCK" );
      iCreateEditOrderDriver.setTransportationType( "AIR (AIR)" );
      iCreateEditOrderDriver.setFreightOnBoard( "RECEIPT (RECEIPT)" );
      iCreateEditOrderDriver.setShipTo( SHIP_TO_LOCATION );
      iCreateEditOrderDriver.setQuantity( "1" );
      iCreateEditOrderDriver.clickOK();
   }


   @Given( "^that the part Vendor has no External Key$" )
   public void thatThePartVendorHasNoExternalKey() throws Throwable {
      iCreateEditOrderDriver.setPartNumber( TRACKED_PART2 );
      // click elsewhere on the screen so that the auto-logic associated to the Part No field
      // completes and I can continue editing fields without having these changes rolled back
      iCreateEditOrderDriver.headerOrderInformation();
      iCreateEditOrderDriver.setVendor( VENDOR_NO_EX_KEY_NONSPEC );

   }


   @Then( "^Send Order Information message with action Create contains order creation date value and no external key value$" )
   public void
         sendOrderInformationMessageWithActinCreateContainsOrderCreationDateValueAndNoExternalKeyValue()
               throws Throwable {

      // Get PO number for later comparison to out bound message
      String lPONumber = iOrderDetailsDriver.getPONumber();

      // Create variable to hold value of PO number from out bound message
      String lMessagePONumber = "";

      // Create variable to hold value of action field from our bound message
      String lAction = "";

      // Keep receiving the next Send Order Information message from the queue until a PO Number
      // match is found and action field is equal to "create" OR the queue is empty
      while ( !lPONumber.equals( lMessagePONumber ) || ( !lAction.equals( "create" ) ) ) {

         // create variable of type SendOrderInformationDocument to receive the out bound message
         // from web driver
         SendOrderInformationDocument lSendOrderInformationDoc =
               iSendOrderInformationMessageDriver.receive();

         // Fail if the queue is empty
         if ( lSendOrderInformationDoc == null ) {
            Assert.fail( "The ASB Notification Queue is empty" );
         } else {
            // Get the PO number from the out bound message
            lMessagePONumber = lSendOrderInformationDoc.getSendOrderInformation().getOrderArray( 0 )
                  .getOrderNumber();
         }

         // If the PO Numbers match then check if the action field is a match
         // If no match, then return to while loop and receive next out bound message from queue
         if ( lPONumber.equals( lMessagePONumber ) ) {

            // Validate Action is CREATE
            lAction = lSendOrderInformationDoc.getSendOrderInformation().getAction().toString();
            if ( lAction.equals( "create" ) ) {

               // Validate that External Key field itself does not exist in out bound message
               Assert.assertNull(
                     "Error: There exists an External Key field in the outbound message",
                     lSendOrderInformationDoc.getSendOrderInformation().getOrderArray( 0 )
                           .getVendor().getExternalKey() );

               // Validate that the Order Creation Date is not null
               Assert.assertNotNull( lSendOrderInformationDoc.getSendOrderInformation()
                     .getOrderArray( 0 ).getOrderCreationDate() );

               // Validate the date format of Order Creation Date
               String lCreationDate = lSendOrderInformationDoc.getSendOrderInformation()
                     .getOrderArray( 0 ).getOrderCreationDate();

               String lpattern = ( "yyyy-MM-dd HH:mm:ss" );

               SimpleDateFormat lDateFormat = new SimpleDateFormat( lpattern );
               lDateFormat.setLenient( false );

               try {
                  lDateFormat.parse( lCreationDate.trim() );
               } catch ( ParseException pe ) {
                  Assert.fail(
                        "The Creation Date field is not of the correct format yyyy-MM-dd HH:mm:ss" );
               }

            }

         }
      }
   }


   @Given( "^that a PO has been created using a Vendor with an External Key$" )
   public void thatAPOHasBeenCreatedUsingAVendorWithAnExternalKey() throws Throwable {
      iCreateEditOrderDriver.setPartNumber( TRACKED_PART1 );
      // click elsewhere on the screen so that the auto-logic associated to the Part No field
      // completes and I can continue editing fields without having these changes rolled back
      iCreateEditOrderDriver.headerOrderInformation();
      iCreateEditOrderDriver.setVendor( VENDOR_EX_KEY );
      iCreateEditOrderDriver.setQuantity( "1" );
      iCreateEditOrderDriver.setTermsConditions( "NET30 (NET30)" );
      iCreateEditOrderDriver.setReExpediteLocation( "GBLDOCK" );
      iCreateEditOrderDriver.setTransportationType( "AIR (AIR)" );
      iCreateEditOrderDriver.setFreightOnBoard( "RECEIPT (RECEIPT)" );
      iCreateEditOrderDriver.setShipTo( SHIP_TO_LOCATION );
      iCreateEditOrderDriver.clickOK();
   }


   @Given( "^that Request Authorization has been approved$" )
   public void thatRequestAuthorizationHasBeenApproved() throws Throwable {
      iOrderDetailsDriver.clickRequestAuthorization();
      iRequestAuthorizationPageDriver.clickOK();
      iRequestAuthorizationPageDriver.setPassword( "password" );
      iRequestAuthorizationPageDriver.authenticate();

      // iOrderDetailsDriver.AOGAuthorizationOverride();
      // iAOGAuthorizeOrderDriver.ok();
      // iAOGAuthorizeOrderDriver.setPassword( "password" );
      // iAOGAuthorizeOrderDriver.authenticate();

   }


   @When( "^I issue the order$" )
   public void iIssueTheOrder() throws Throwable {
      iOrderDetailsDriver.clickIssuePurchaseOrder();
      iIssueOrderPageDriver.clickOk();
   }


   @Given( "^that the PO has been issued$" )
   public void thatThePOHasBeenIssued() throws Throwable {
      iOrderDetailsDriver.clickIssuePurchaseOrder();
      iIssueOrderPageDriver.clickOk();
   }


   @When( "^I cancel the order$" )
   public void iCancelTheOrder() throws Throwable {
      iOrderDetailsDriver.clickCancelPurchaseOrder();
      iCancelPage.okButton();
   }


   @Then( "^Send Order Information message with action \"([^\"]*)\" contains both order creation date and external key values$" )
   public void
         sendOrderInformationMessageWithActionCreateOrIssueOrCancelContainsBothOrderCreationDateAndExternalKeyValues(
               String aActionType ) throws Throwable {

      // Get PO number for later comparison to out bound message
      String lPONumber = iOrderDetailsDriver.getPONumber();

      // Create variable to hold value of PO number from out bound message
      String lMessagePONumber = "";

      // Create variable to hold value of action field from our bound message
      String lAction = "";

      // Keep receiving the next Send Order Information message from the queue until a PO Number
      // match is found and action field is equal to "wild card" value of @Then statement (i.e.
      // aActionType from Feature File)
      // OR the queue is empty
      while ( !lPONumber.equals( lMessagePONumber ) || ( !lAction.equals( aActionType ) ) ) {

         // create variable of type SendOrderInformationDocument to receive the out bound message
         // from web driver
         SendOrderInformationDocument lSendOrderInformationDoc =
               iSendOrderInformationMessageDriver.receive();

         // Fail if the queue is empty
         if ( lSendOrderInformationDoc == null ) {
            Assert.fail( "The ASB Notification Queue is empty" );
         } else {
            // Get the PO number from the out bound message
            lMessagePONumber = lSendOrderInformationDoc.getSendOrderInformation().getOrderArray( 0 )
                  .getOrderNumber();
         }

         // If the PO Numbers match then check if the action field is a match
         // If no match, then return to while loop and receive next out bound message from queue
         if ( lPONumber.equals( lMessagePONumber ) ) {

            // Validate Action is either CREATE, ISSUE or CANCEL based upon given aActionType from
            // Feature File
            lAction = lSendOrderInformationDoc.getSendOrderInformation().getAction().toString();
            if ( lAction.equals( aActionType ) ) {

               // Validate that External Key matches Vendor Profile External Key
               String lExternalKey = lSendOrderInformationDoc.getSendOrderInformation()
                     .getOrderArray( 0 ).getVendor().getExternalKey();
               Assert.assertEquals( lExternalKey, "SampleExKey1" );

               // Validate that the Order Creation Date is not null
               Assert.assertNotNull( lSendOrderInformationDoc.getSendOrderInformation()
                     .getOrderArray( 0 ).getOrderCreationDate() );

               // Validate the date format of Order Creation Date
               String lCreationDate = lSendOrderInformationDoc.getSendOrderInformation()
                     .getOrderArray( 0 ).getOrderCreationDate();

               String lpattern = ( "yyyy-MM-dd HH:mm:ss" );

               SimpleDateFormat lDateFormat = new SimpleDateFormat( lpattern );
               lDateFormat.setLenient( false );

               try {
                  lDateFormat.parse( lCreationDate.trim() );
               } catch ( ParseException pe ) {
                  Assert.fail(
                        "The Creation Date field is not of the correct format yyyy-MM-dd HH:mm:ss" );
               }

            }

         }
      }
   }

}
