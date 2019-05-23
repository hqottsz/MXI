package com.mxi.am.stepdefn.persona.qualityinspector;

import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;

import com.mxi.am.driver.common.MessagePageDriver;
import com.mxi.am.driver.web.LoginPageDriver;
import com.mxi.am.driver.web.LogoutPageDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.po.OrderDetailsPageDriver;
import com.mxi.am.driver.web.po.OrderSearchPageDriver;
import com.mxi.am.driver.web.shipment.ReceiveShipmentPageDriver;
import com.mxi.am.driver.web.shipment.ReceiveShipmentPageDriver.ShipmentLine;
import com.mxi.am.driver.web.todolist.ToDoListPageDriver;
import com.mxi.am.driver.web.todolist.ToDoListPanes.QuarantinePaneDriver.QuaratineInventories;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class InspectUnserviceableEOStepDefinitions {

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
   private ToDoListPageDriver iToDoListPageDriver;

   @Inject
   private MessagePageDriver iMessagePageDriver;

   private static final String EO_SERIALNO_MULTILINE1 = "Unservice-EX1";
   private static final String EO_SERIALNO_MULTILINE2 = "Unservice-EX2";
   private static final String USERNAME = "user1";
   private static final String INSPECTOR_USERNAME = "qcinsp1";
   private static final String USER_PASSWORD = "password";
   private static final String PURCHMAN_NAV_STRING = "Purchasing Manager";
   private static final String ORDER_SEARCH_NAV = "Order Search";
   private static final String QCINSPECTOR_NAV_STRING = "Quality Control Inspector";
   private static final String QCINSPECTOR_TODOLIST_NAV = "To Do List (Quality Control Inspector)";
   private static final String EO_WITH_MULTI_LINE = "EO1_Unservice";


   // the following private method is to receive exchange order then quarantine the received item(s)
   private void iReceiveAndQuarantineItems( String aOrderNumber ) {

      // login
      iLoginDriver.setUserName( USERNAME ).setPassword( USER_PASSWORD ).login();

      // he goes to order search page and look for the order(this user also has Purchase Manager
      // role, he needs to use this role to receive order shipments and quarantine the inventories)
      iNavigationDriver.navigate( PURCHMAN_NAV_STRING, ORDER_SEARCH_NAV );
      iOrderSearchPageDriver.clickClearAll();
      iOrderSearchPageDriver.setOrderNumber( aOrderNumber );
      iOrderSearchPageDriver.clickSearch();

      // after clicked the order, he is directed to order details page, then receive the order
      iOrderDetailsPageDriver.clickReceiveShipment();
      List<ShipmentLine> lShipmentLinesAmendment = iReceiveShipmentPageDriver.getShipmentLines();

      // we set the serial number and quantity 1 and put them under quarantine
      ShipmentLine localShipmentLine0 = lShipmentLinesAmendment.get( 0 );

      setShipmentLine( localShipmentLine0, EO_SERIALNO_MULTILINE1 );
      setShipmentLine( lShipmentLinesAmendment.get( 1 ), EO_SERIALNO_MULTILINE2 );

      iReceiveShipmentPageDriver.clickOk();

      // log out after receiving inventory
      iNavigationDriver.clickLogout();
      iLogoutPageDriver.clickOK();

   }


   private void setShipmentLine( ShipmentLine aShipmentLine, String aSerialNo ) {

      aShipmentLine.setSerialNo( aSerialNo );
      aShipmentLine.setQty( "1" );
      aShipmentLine.setQuarantine();
      aShipmentLine.setManufacturedDate();
      aShipmentLine.setQuarantineNotes();
   }


   @Given( "^multiple inventories received from an Exchange order are put under quarantine$" )
   public void iAmInspector() throws Throwable {

      iReceiveAndQuarantineItems( EO_WITH_MULTI_LINE );

      // Login as QC Inspector
      iLoginDriver.setUserName( INSPECTOR_USERNAME ).setPassword( USER_PASSWORD ).login();

      // go to Inspector To Do List
      iNavigationDriver.navigate( QCINSPECTOR_NAV_STRING, QCINSPECTOR_TODOLIST_NAV );
      // click Quarantine tab
      iToDoListPageDriver.clickTabQuarantine();

   }


   @And( "^Inspect Unserviceable button is not activated when we select multiple items" )
   public void iSelectMultipleitems() throws Throwable {

      List<QuaratineInventories> lQuarantineLinesAmmendment =
            iToDoListPageDriver.getTabQuarantine().getQuaratineInventories();

      // select two inventories
      lQuarantineLinesAmmendment.get( 0 ).clickCheckBox();
      lQuarantineLinesAmmendment.get( 1 ).clickCheckBox();

      // verify the inspect as unserviceable button is not enabled
      Assert.assertTrue(
            iToDoListPageDriver.getTabQuarantine().UnserviceableEnabled().contains( "disabled" ) );

   }


   @When( "^a single item is selected$" )
   public void iSelectSingleitem() throws Throwable {

      List<QuaratineInventories> lQuarantineLinesAmmendment =
            iToDoListPageDriver.getTabQuarantine().getQuaratineInventories();

      lQuarantineLinesAmmendment.get( 0 ).clickCheckBox();

   }


   @Then( "^Inspects Unserviceable button is activated$" )
   public void InsepctUnserviceableActivated() throws Throwable {

      // verify the inspect as unserviceable button is enabled
      Assert.assertFalse(
            iToDoListPageDriver.getTabQuarantine().UnserviceableEnabled().contains( "disabled" ) );
   }


   @And( "^Marks as Repair Required is not operated$" )
   public void UnableMakrasRepairRequired() throws Throwable {

      // verify when we click Mark as Repair Required, error message is thrown
      iToDoListPageDriver.getTabQuarantine().clickMarkRepairRequiredButton();

      Assert.assertTrue(
            iMessagePageDriver.getCellMessage().contains( "please inspect it first." ) );

      iMessagePageDriver.clickOk();

   }

}
