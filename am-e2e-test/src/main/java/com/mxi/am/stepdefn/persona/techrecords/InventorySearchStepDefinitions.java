
package com.mxi.am.stepdefn.persona.techrecords;

import javax.inject.Inject;

import org.junit.Assert;

import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.inventory.inventorydetailspage.InventoryDetailsPageDriver;
import com.mxi.am.driver.web.inventory.inventorysearchpage.InventorySearchPageDriver;
import com.mxi.am.driver.web.inventory.inventorysearchpage.inventorysearchpanes.InventoryFoundPaneDriver.InventorySearchResult;
import com.mxi.am.stepdefn.NavigationStepDefinitions;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;


/**
 * Step Definitions for Inventory Search
 */
@ScenarioScoped
public class InventorySearchStepDefinitions {

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private InventorySearchPageDriver iInventorySearchDriver;

   @Inject
   private NavigationStepDefinitions iNavigationStepDefinitions;

   @Inject
   private InventoryDetailsPageDriver iInventoryDetails;

   private String iBarcode;


   @When( "^I search an inventory with barcode \"([^\"]*)\"$" )
   public void searchInventoryByBarcode( String aBarcode ) throws Throwable {
      iBarcode = aBarcode;
      iNavigationDriver.navigate( "Technical Records", "Inventory Search" );

      iInventorySearchDriver.clearAll();
      iInventorySearchDriver.setBarcode( aBarcode );
      iInventorySearchDriver.clickSearch();
   }


   @Then( "^I should see the inventory details$" )
   public void assertOnInventoryPage() throws Throwable {
      iNavigationStepDefinitions.assertOnPage( "Inventory Details" );
      Assert.assertEquals( iBarcode, iInventoryDetails.clickTabDetails().getBarcode() );
   }


   @Then( "^I should see the part no oem \"([^\"]*)\" in the inventory table$" )
   public void checkTableForInventory( String aOemPartNo ) throws Throwable {
      iNavigationDriver.navigate( "Technical Records", "Inventory Search" );
      iInventorySearchDriver.clearAll();
      iInventorySearchDriver.setOEMPartNo( aOemPartNo );
      iInventorySearchDriver.clickSearch();

      boolean lFoundPartNo = false;;
      for ( InventorySearchResult lResult : iInventorySearchDriver.clickTabInventoryFound()
            .getResults() ) {
         if ( aOemPartNo.equals( lResult.getOemPartNo() ) ) {
            lFoundPartNo = true;
            break;
         }
      }
      Assert.assertTrue( "Could not find oem part no: " + aOemPartNo, lFoundPartNo );
   }
}
