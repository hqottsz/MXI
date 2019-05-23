package com.mxi.am.stepdefn.persona.finance;

import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;

import com.mxi.am.driver.web.LoginPageDriver;
import com.mxi.am.driver.web.LogoutPageDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.inventory.InspectAsUnserviceablePageDriver;
import com.mxi.am.driver.web.inventory.inventorydetailspage.InventoryDetailsPageDriver;
import com.mxi.am.driver.web.po.CreateEditPOInvoicePageDriver;
import com.mxi.am.driver.web.po.OrderDetailsPageDriver;
import com.mxi.am.driver.web.po.OrderSearchPageDriver;
import com.mxi.am.driver.web.po.POInvoiceDetailsPageDriver;
import com.mxi.am.driver.web.po.ValidateForPaymentPageDriver;
import com.mxi.am.driver.web.po.poinvoicedetails.POInvoiceLinesPaneDriver;
import com.mxi.am.driver.web.po.poinvoicedetails.POInvoiceLinesPaneDriver.POInvoiceDetailsPOInvoiceLine;
import com.mxi.am.driver.web.shipment.ReceiveShipmentPageDriver;
import com.mxi.am.driver.web.shipment.ReceiveShipmentPageDriver.ShipmentLine;
import com.mxi.am.driver.web.shipment.shipmentDetails.ShipmentDetailsPageDriver;
import com.mxi.am.driver.web.shipment.shipmentDetails.shipmentPanes.ShipmentLinesPaneDriver.CompletedShipmentLine;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class PayInvoiceforUnserviceableStepDefinitions {

   @Inject
   private LoginPageDriver iLoginDriver;

   @Inject
   private OrderDetailsPageDriver iOrderDetailsPageDriver;

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private OrderSearchPageDriver iOrderSearchPageDriver;

   @Inject
   private LogoutPageDriver iLogoutPageDriver;

   @Inject
   private ReceiveShipmentPageDriver iReceiveShipmentPageDriver;

   @Inject
   private ShipmentDetailsPageDriver iShipmentDetailsPageDriver;

   @Inject
   private InventoryDetailsPageDriver iInventoryDetailsPageDriver;

   @Inject
   private InspectAsUnserviceablePageDriver iInspectAsUnserviceablePageDriver;

   @Inject
   private CreateEditPOInvoicePageDriver iCreateEditPOInvoicePageDriver;

   @Inject
   private POInvoiceDetailsPageDriver iPOInvoiceDetailsPageDriver;

   @Inject
   private ValidateForPaymentPageDriver iValidateForPaymentPageDriver;


   /**
    * We need QC Inspector's help to inspect the received inventory as unserviceable
    *
    */
   @Given( "^inventory is inspected as unserviceable$" )
   public void prepareDataByInspector() throws Throwable {

      // QC inspector login
      iLoginDriver.setUserName( "user1" ).setPassword( "password" ).login();

      // he goes to order search page and look for the order(this user also has Purchase Manager
      // role)
      iNavigationDriver.navigate( "Purchasing Manager", "Order Search" );
      iOrderSearchPageDriver.clickClearAll();
      iOrderSearchPageDriver.setOrderNumber( "PO3_Invoice" );
      iOrderSearchPageDriver.clickSearch();

      // after clicked the order, he is directed to order details page, then receive the order
      iOrderDetailsPageDriver.clickReceiveShipment();
      List<ShipmentLine> lShipmentLinesAmmendment = iReceiveShipmentPageDriver.getShipmentLines();

      // we set the serial number and quantity 1
      lShipmentLinesAmmendment.get( 0 ).setSerialNo( "Unservice-Inv1" );
      lShipmentLinesAmmendment.get( 0 ).setQty( "1" );

      iReceiveShipmentPageDriver.clickOk();

      // inspect the received inventory unserviceable
      List<CompletedShipmentLine> lCompletedShipmentLineRow =
            iShipmentDetailsPageDriver.getTabShipmentLines().getCompletedShipmentLines();
      lCompletedShipmentLineRow.get( 0 ).clickSerialBatchNO();

      iInventoryDetailsPageDriver.clickInspectAsUnServiceable();

      // Un-check the Print UnServiceable Part Tag check box and click OK
      iInspectAsUnserviceablePageDriver.clickPrintUnServiceablepartTag();
      iInspectAsUnserviceablePageDriver.clickOk();
      iInspectAsUnserviceablePageDriver.setPassword( "password" ).clickAuthenticationOkButton();

      // verify the inventory is REPREQ and the location is under USSTG
      Assert.assertEquals( "REPREQ (Repair Required)",
            iInventoryDetailsPageDriver.clickTabDetails().getCondition() );
      Assert.assertEquals( "AIRPORT1/USSTG (Unserviceable Staging)",
            iInventoryDetailsPageDriver.getTabDetails().getLocation() );

      // log out after inspect unserviceable
      iNavigationDriver.clickLogout();
      iLogoutPageDriver.clickOK();

   }


   @And( "^I have a purchase order to be invoiced and the received inventory is inspected as unserviceable$" )
   public void iHaveAnOrdertoBeInvoiced() throws Throwable {

      iNavigationDriver.navigate( "Invoice Agent", "Order Search" );
      iOrderSearchPageDriver.clickClearAll();
      iOrderSearchPageDriver.setOrderNumber( "PO3_Invoice" );
      iOrderSearchPageDriver.clickSearch();

   }


   @When( "^I validate and pay the invoice$" )
   public void iValidatePaidInvoice() throws Throwable {

      // create invoice
      iOrderDetailsPageDriver.clickCreateInvoice();
      iCreateEditPOInvoicePageDriver.setInvoiceNumber( "UnserviceInvoice1" );
      iCreateEditPOInvoicePageDriver.clickOk();

      // click validate for payment
      iPOInvoiceDetailsPageDriver.clickValidateforPayment();
      iValidateForPaymentPageDriver.clickMarkForPayment();

      // click Mark as Paid
      iPOInvoiceDetailsPageDriver.clickMarkAsPaid();

   }


   @Then( "^the invoice is paid$" )
   public void theInvoiceisPaid() throws Throwable {

      // verify the status is Paid
      Assert.assertEquals( "PAID (This invoice has been paid.)",
            iPOInvoiceDetailsPageDriver.getStatus() );

      // Navigate to the PO Invoice Lines tab
      POInvoiceLinesPaneDriver lPOinvoiceLinesPaneDriver =
            iPOInvoiceDetailsPageDriver.clickTabPOInvoiceLines();

      // Get the list of PO Invoice Lines
      List<POInvoiceDetailsPOInvoiceLine> lPOInvoiceLines =
            lPOinvoiceLinesPaneDriver.getPOInvoiceLines();

      lPOInvoiceLines.get( 0 ).clickPONumber();

   }


   @And( "^the Purchase order is closed$" )
   public void thePOisClosed() throws Throwable {

      Assert.assertEquals(
            "CLOSED (The order has been marked for payment and is therefore closed.)",
            iOrderDetailsPageDriver.getStatus() );
   }

}
