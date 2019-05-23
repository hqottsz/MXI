
package com.mxi.am.stepdefn.persona.qualityinspector;

import javax.inject.Inject;

import org.junit.Assert;

import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.inventory.CreateInventoryPageDriver;
import com.mxi.am.driver.web.inventory.InspectAsServiceablePageDriver;
import com.mxi.am.driver.web.inventory.editinventorypage.EditInventoryPageDriver;
import com.mxi.am.driver.web.inventory.inventorydetailspage.InventoryDetailsPageDriver;
import com.mxi.am.driver.web.inventory.inventorysearchpage.InventorySearchPageDriver;
import com.mxi.am.driver.web.part.partsearchpage.PartSearchPageDriver;
import com.mxi.am.driver.web.po.RequestAuthorizationPageDriver;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class InspectInventoryStepDefinitions {

   @Inject
   private InventoryDetailsPageDriver iInventoryDetailsPageDriver;

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private InspectAsServiceablePageDriver iInspectAsServiceablePageDriver;

   @Inject
   private RequestAuthorizationPageDriver iRequestAuthorizationPageDriver;

   @Inject
   private InventorySearchPageDriver iInventorySearchPageDriver;

   @Inject
   private CreateInventoryPageDriver iCreateInventoryPageDriver;

   @Inject
   private PartSearchPageDriver iPartSearchPageDriver;

   @Inject
   private EditInventoryPageDriver iEditInventoryPageDriver;


   @Given( "^I have a new inventory to inspect$" )
   public void iHaveANewInventoryToInspect() throws Throwable {
      iNavigationDriver.navigate( "Material Controller", "Inventory Search" );

      iInventorySearchPageDriver.clickTabInventoryFound().clickCreateInventory();
      iCreateInventoryPageDriver.clickSelectPart();
      iPartSearchPageDriver.setOemPartNo( "S000003" );
      iPartSearchPageDriver.clickSearch();
      iPartSearchPageDriver.clickTabPartsFound().getResults().get( 0 ).clickPartNo();
      iPartSearchPageDriver.clickTabPartsFound().clickAssignPart();
      iCreateInventoryPageDriver.setLocation( "AIRPORT1/DOCK" ).setManufacturedDate( "01-JUL-2016" )
            .clickOk();
      iEditInventoryPageDriver.clickFinish();
   }


   @When( "^I inspect the inventory$" )
   public void iInspectAShipment() throws Throwable {
      iInventoryDetailsPageDriver.clickInspectAsServiceable();
   }


   @When( "^I agree with the conditions$" )
   public void iAgreeWithTheConditions() throws Throwable {
      iInspectAsServiceablePageDriver.printServiceablepartTag();
      iInspectAsServiceablePageDriver.clickOk();

      iRequestAuthorizationPageDriver.setPassword( "password" ).authenticate();
   }


   @Then( "^the inventory is available to use$" )
   public void theShipmentIsAvailableToUse() throws Throwable {
      Assert.assertEquals( "RFI (Ready for Issue)",
            iInventoryDetailsPageDriver.clickTabDetails().getCondition() );
   }

}
