
package com.mxi.am.stepdefn.persona.techrecords;

import javax.inject.Inject;

import org.junit.Assert;

import com.mxi.am.driver.web.inventory.inventorydetailspage.InventoryDetailsPageDriver;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


/**
 * Step Definitions for navigating Maintenix
 */
public class InventoryDetailsStepDefinitions {

   @Inject
   private InventoryDetailsPageDriver iInventoryDetailsPageDriver;


   @When( "^I go to Inventory Details - Details tab$" )
   public void gotoInventoryDetails_Detailstab() throws Throwable {
      iInventoryDetailsPageDriver.clickTabDetails();
   }


   @Then( "^I should see serial number \"([^\"]*)\"$" )
   public void checkSerialNumber( String aSerialNumber ) throws Throwable {
      Assert.assertEquals( aSerialNumber, iInventoryDetailsPageDriver.getSerialNumber() );

   }
}
