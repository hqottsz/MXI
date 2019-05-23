package com.mxi.am.stepdefn.persona.storeroomclerk;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;

import com.mxi.am.driver.common.ConfirmPageDriver;
import com.mxi.am.driver.ppc.Wait;
import com.mxi.am.driver.query.InventoryQueriesDriver;
import com.mxi.am.driver.web.todolist.ToDoListPageDriver;
import com.mxi.am.driver.web.todolist.ToDoListPanes.TurnInPaneDriver;
import com.mxi.am.driver.web.transfer.TurnInPageDriver;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class TurnInInventoryStepDefinitions {

   private static final String PART_NUMBER = "4548PART";
   private static final String SERIAL_NUMBER = "OPER-4548T";
   private static final String BATCH_NUMBER = "OPER-4548B";
   private static final String MANUFACTURER_CODE = "ABC11";
   private static final String AIRPORT_LOCATION = "AIRPORT1";
   private static final String QUANTITY = "1";
   private static final String CREDIT_ACCOUNT = "5";

   @Inject
   private ToDoListPageDriver iToDoListPageDriver;

   @Inject
   private TurnInPageDriver iTurnInPageDriver;

   @Inject
   private ConfirmPageDriver iConfirmPageDriver;

   private final String iInventoryBarcode;


   @Inject
   public TurnInInventoryStepDefinitions(InventoryQueriesDriver aInventoryQueriesDriver) {
      iInventoryBarcode =
            aInventoryQueriesDriver.getBarcodeBySerialPartNo( PART_NUMBER, SERIAL_NUMBER );

   }


   @When( "^I try to turn in the TRK inventory using the inventory barcode$" )
   public void iTryToTurnInTheTrkInventoryUsingTheInventoryBarcode() throws Throwable {
      navigateToTurnInPage();
      iTurnInPageDriver.setInventoryBarcode( iInventoryBarcode );
      iTurnInPageDriver.clickOemPartNoField();
      Wait.pause( 2000 );
      assertEquals( iInventoryBarcode, iTurnInPageDriver.getInventoryBarcode() );
      assertEquals( PART_NUMBER, iTurnInPageDriver.getOemPartNo() );
      assertEquals( SERIAL_NUMBER, iTurnInPageDriver.getSerialNo() );
      assertEquals( QUANTITY, iTurnInPageDriver.getQuantity() );
      assertEquals( AIRPORT_LOCATION, iTurnInPageDriver.getAirportLocation() );
   }


   @When( "^I try to turn in the BATCH inventory using the part number$" )
   public void iTryToTurnInIheBatchInventoryUsingThePartNumber() throws Throwable {
      navigateToTurnInPage();
      iTurnInPageDriver.setOemPartNo( PART_NUMBER );
      iTurnInPageDriver.clickManufacturer( MANUFACTURER_CODE );
      iTurnInPageDriver.setSerialNo( BATCH_NUMBER );
      iTurnInPageDriver.setQuantity( QUANTITY );
      iTurnInPageDriver.setAirportLocation( AIRPORT_LOCATION );
      iTurnInPageDriver.setCreditAccount( CREDIT_ACCOUNT );
      assertEquals( PART_NUMBER, iTurnInPageDriver.getOemPartNo() );
      assertEquals( BATCH_NUMBER, iTurnInPageDriver.getSerialNo() );
      assertEquals( QUANTITY, iTurnInPageDriver.getQuantity() );
      assertEquals( AIRPORT_LOCATION, iTurnInPageDriver.getAirportLocation() );
      assertEquals( CREDIT_ACCOUNT, iTurnInPageDriver.getCreditAccount() );
      iTurnInPageDriver.clickOK();
      iTurnInPageDriver.acceptAlert();
   }


   @Then( "^the TRK inventory should be successfully turned in$" )
   public void theTrackedInventoryShouldBeSuccessfullyTurnedin() throws Throwable {
      iTurnInPageDriver.clickOK();
      iConfirmPageDriver.clickYes();

   }


   @Then( "^the BATCH inventory should be successfully turned in$" )
   public void theBatchInventoryShouldBeSuccessfullyTurnedin() throws Throwable {
      iTurnInPageDriver.clickOK();
   }


   private void navigateToTurnInPage() {
      TurnInPaneDriver lTurnInPaneDriver = iToDoListPageDriver.clickTabTurnIn();
      lTurnInPaneDriver.clickTurnInButton();
   }

}
