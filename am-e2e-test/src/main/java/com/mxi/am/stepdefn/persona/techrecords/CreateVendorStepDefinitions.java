package com.mxi.am.stepdefn.persona.techrecords;

import javax.inject.Inject;

import org.junit.Assert;

import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.vendor.VendorDetailsPageDriver;
import com.mxi.am.driver.web.vendor.VendorSearchPageDriver;
import com.mxi.am.driver.web.vendor.vendorpanes.CommunicationPaneDriver;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;


/**
 * Step definitions for the create vendor feature.
 *
 */
public class CreateVendorStepDefinitions {

   /* Navigation */
   private static final String PARENT_MENU = "Technical Records";
   private static final String CHILD_MENU = "Vendor Search";

   /* User Interface Constants */
   private static final String UI_VENDOR_CODE = "SWA001";
   private static final String UI_VENDOR_NAME = "SWA Vendor1";
   private static final String UI_VENDOR_TYPE = "PURCHASE (PURCHASE)";
   private static final String UI_VENDOR_CURRENCY = "USD (US Dollars)";
   private static final String UI_VENDOR_EXTERNAL_KEY = "swa_test_key";
   private static final String UI_VENDOR_TIME_ZONE = "America/Chicago - Central Standard Time CST";
   private static final String UI_VENDOR_ADDRESS = "P.O. Box 36647";
   private static final String UI_VENDOR_CITY = "Ottawa";
   private static final String UI_VENDOR_COUNTRY = "CANADA";
   private static final String UI_VENDOR_CONTACT = "John Doe";

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private VendorSearchPageDriver iVendorSearchPageDriver;

   @Inject
   private VendorDetailsPageDriver iVendorDetailsPageDriver;

   @Inject
   private CommunicationPaneDriver iCommunicationPane;


   @Given( "^a vendor with vendor code \"([^\"]*)\" does not exist$" )
   public void aVendorWithVendorCodeDoesNotExist( String aVendorCode ) throws Throwable {

      // Assert that the code provided does not exist in the results
      iNavigationDriver.navigate( PARENT_MENU, CHILD_MENU );
      iVendorSearchPageDriver.clickClearAll();
      iVendorSearchPageDriver.search();
      Assert.assertFalse( iVendorSearchPageDriver.doesVendorCodeExist( aVendorCode ) );
   }


   @Then( "^the vendor is created in Maintenix with the provided information$" )
   public void theVendorIsCreatedInMaintenixWithTheProvidedInformation() throws Throwable {
      iVendorSearchPageDriver.clickClearAll();
      iVendorSearchPageDriver.search();

      iVendorSearchPageDriver.clickSearchResultRow( UI_VENDOR_CODE );

      // Verify if all vendor information is correct on main page
      Assert.assertTrue( iVendorDetailsPageDriver.getVendorCode().equals( UI_VENDOR_CODE ) );
      Assert.assertTrue( iVendorDetailsPageDriver.getVendorName().equals( UI_VENDOR_NAME ) );
      Assert.assertTrue( iVendorDetailsPageDriver.getVendorType().equals( UI_VENDOR_TYPE ) );
      Assert.assertTrue( iVendorDetailsPageDriver.getCurrency().equals( UI_VENDOR_CURRENCY ) );
      Assert.assertTrue(
            iVendorDetailsPageDriver.getExternalKey().equals( UI_VENDOR_EXTERNAL_KEY ) );
      Assert.assertTrue( iVendorDetailsPageDriver.getTimeZone().equals( UI_VENDOR_TIME_ZONE ) );
      Assert.assertTrue( iVendorDetailsPageDriver.getAddress().equals( UI_VENDOR_ADDRESS ) );
      Assert.assertTrue( iVendorDetailsPageDriver.getCity().equals( UI_VENDOR_CITY ) );
      Assert.assertTrue( iVendorDetailsPageDriver.getCountry().equals( UI_VENDOR_COUNTRY ) );

      // Verify if the contact information is correct
      iVendorDetailsPageDriver.clickTabCommunication();

      // Verify if the contact information is correct
      Assert.assertTrue(
            iCommunicationPane.getMainContact().getContactName().equals( UI_VENDOR_CONTACT ) );

   }
}
