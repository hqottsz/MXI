
package com.mxi.am.stepdefn.persona.techrecords;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Assert;

import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.RefreshDriver;
import com.mxi.am.driver.web.common.UserAlertsPageDriver;
import com.mxi.am.driver.web.inventory.CreateInventoryPageDriver;
import com.mxi.am.driver.web.inventory.editinventorypage.EditInventoryPageDriver;
import com.mxi.am.driver.web.part.partdetailspage.PartDetailsPageDriver;
import com.mxi.am.driver.web.part.partsearchpage.PartSearchPageDriver;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;


public class FlightUsageAlertsStepDefinitions {

   @Inject
   private UserAlertsPageDriver iUserAlertsDriver;

   @Inject
   private RefreshDriver iRefresh;

   @Inject
   private NavigationDriver iNavDriver;

   @Inject
   private PartSearchPageDriver iPartSearch;

   @Inject
   private PartDetailsPageDriver iPartDetails;

   @Inject
   private CreateInventoryPageDriver iCreateInventory;

   @Inject
   private EditInventoryPageDriver iEditInventory;

   private static final String PARENT_MENU_MATERIAL_CONTROLLER = "Material Controller";
   private static final String CHILD_MENU_PART_SEARCH = "Part Search";
   private static final String PARENT_MENU_OPTIONS = "Options";
   private static final String CHILD_MENU_ALERTS = "Alerts";


   @Then( "^an appropriate synchronous \"([^\"]*)\" alert is raised$" )
   public void anAppropriateSynchronousAlertIsRaised( String arg1,
         List<Map<String, String>> aDataTable ) throws Throwable {
      Map<String, String> aTableRow = aDataTable.get( 0 );
      iRefresh.refreshCurrentPage();
      Assert.assertEquals( ( aTableRow.get( "Message" ) ),
            ( iUserAlertsDriver.getTabUnassignedAlerts().getRow( 0 ).getMessage() ) );
   }


   @Given( "^a duplicate serial number exists$" )
   public void aDuplicateSerialNumberExists() throws Throwable {
      // Navigate to the Material Controller menu item and click on Part Search
      iNavDriver.navigate( PARENT_MENU_MATERIAL_CONTROLLER, CHILD_MENU_PART_SEARCH );

      // Enter OEM Part Number: S000002
      iPartSearch.setOemPartNo( "S000002" );

      // Click Search button
      iPartSearch.clickSearch();

      // Select Part Number hyper link of 1st and only result
      iPartSearch.clickTabPartsFound().getResults().get( 0 ).clickOemPartNo( "S000002" );

      // Click Create Inventory button on Part Details page
      iPartDetails.clickCreateInventory();

      // Enter Serial No: TECHREC-004
      iCreateInventory.setSerialNo( "TECHREC-004" );

      // Enter Location: AIRPORT1/DOCK
      iCreateInventory.setLocation( "AIRPORT1/DOCK" );

      // Click OK button
      iCreateInventory.clickOk();

      // On Edit Inventory Page, click the Finish button
      iEditInventory.clickFinish();

      // Navigate back to the Alerts Page
      iNavDriver.navigateOther( PARENT_MENU_OPTIONS, CHILD_MENU_ALERTS );
      iUserAlertsDriver.clickTabUnassignedAlerts();
   }
}
