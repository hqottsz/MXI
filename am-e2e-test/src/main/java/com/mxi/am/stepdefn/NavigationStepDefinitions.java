
package com.mxi.am.stepdefn;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Assert;

import com.mxi.am.driver.web.NavigationDriver;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


/**
 * Step Definitions for navigating Maintenix
 */
public class NavigationStepDefinitions {

   @Inject
   private NavigationDriver iNavigationDriver;

   private static String PARENT_MENU_ITEM = "Parent_Menu_Item";
   private static String CHILD_MENU_ITEM = "Child_Menu_Item";


   public void assertOnPage( String aTitle, String aSubTitle ) {
      Assert.assertEquals( aTitle, iNavigationDriver.getTitle() );
      Assert.assertTrue( iNavigationDriver.getSubTitle().contains( aSubTitle ) );
   }


   @Given( "^I navigate to the following page$" )
   public void iNavigateToTheFollowingPage( List<Map<String, String>> aDataTable )
         throws Throwable {
      // Retrieve the first row from the table
      Map<String, String> aTableRow = aDataTable.get( 0 );

      iNavigationDriver.navigate( aTableRow.get( PARENT_MENU_ITEM ),
            aTableRow.get( CHILD_MENU_ITEM ) );
   }


   @When( "^I enter barcode \"([^\"]*)\"$" )
   public void gotoBarcode( String aBarcode ) throws Throwable {
      iNavigationDriver.barcodeSearch( aBarcode );
   }


   @Then( "^I should be on page \"([^\"]*)\" for item \"([^\"]*)\"$" )
   public void assertOnItem( String aTitle, String aSubTitle ) throws Throwable {
      assertOnPage( aTitle, aSubTitle );
   }


   @Then( "^I should see be on page \"([^\"]*)\"$" )
   public void i_should_see_be_on_page( String aTitle ) throws Throwable {
      assertOnPage( aTitle );
   }


   public void assertOnPage( String aTitle ) {
      Assert.assertEquals( aTitle, iNavigationDriver.getTitle() );
   }

}
