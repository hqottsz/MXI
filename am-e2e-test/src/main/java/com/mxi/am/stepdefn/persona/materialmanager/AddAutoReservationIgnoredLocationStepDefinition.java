package com.mxi.am.stepdefn.persona.materialmanager;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.persistence.jpa.jpql.Assert;

import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.location.AddIgnoreLocationsPageDriver;
import com.mxi.am.driver.web.location.AddIgnoreLocationsPageDriver.IgnorableLocation;
import com.mxi.am.driver.web.location.locationdetailspage.LocationDetailsPageDriver;
import com.mxi.am.driver.web.location.locationdetailspage.locationdetailspanes.AutoReservationPaneDriver.IgnoredLocation;
import com.mxi.am.driver.web.location.locationsearchpage.LocationSearchPageDriver;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class AddAutoReservationIgnoredLocationStepDefinition {

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private LocationSearchPageDriver iLocationSearchPageDriver;

   @Inject
   private LocationDetailsPageDriver iLocationPageDriver;

   @Inject
   private AddIgnoreLocationsPageDriver iAddLocationPageDriver;

   private final static String SUP_LOCATION = "SUPPLY1";
   private final static String SUP_LOCATION_NAME = "Supply Location 1";

   private final static String LINE_LOCATION = "SUPPLY1/LINE (Line Maintenance)";


   @Given( "^I am a Material Manager and I want to add ignore location to a supply location$" )
   public void iAmAMaterialManagerAndIWantToAddIgnoreLocationToASupplyLocation() throws Throwable {
      // Search the supply location and then navigate to the Auto-reservation tab

      iNavigationDriver.navigate( "Material Controller", "Location Search" );
      iLocationSearchPageDriver.setLocationCode( SUP_LOCATION );
      iLocationSearchPageDriver.clickSearch();

      iLocationSearchPageDriver.clickTabLocationsFound()
            .getRowByLocationCDAndName( SUP_LOCATION, SUP_LOCATION_NAME ).clickLocationCDName();

   }


   @When( "^I add a sub-location to Ignore Location list$" )
   public void iAddASubLocationToIgnoreLocationList() throws Throwable {
      iLocationPageDriver.clickTabAutoReservation().clickAdd();

      List<IgnorableLocation> lResults = iAddLocationPageDriver.getIgnorableLocations();
      for ( IgnorableLocation lList : lResults ) {
         if ( lList.getLocationName().equals( LINE_LOCATION ) ) {
            lList.select();
            break;
         }
      }

      iAddLocationPageDriver.clickOK();
   }


   @Then( "^the sub-location is added successfully$" )
   public void theSubLocationIsAddedSuccessfully() throws Throwable {

      List<IgnoredLocation> lResults =
            iLocationPageDriver.getTabAutoReservation().getIgnoredLocations();
      boolean lSubLocationFound = false;
      for ( IgnoredLocation lList : lResults ) {
         if ( lList.getLocationName().equals( LINE_LOCATION ) ) {
            lSubLocationFound = true;
            break;
         }
      }
      Assert.isTrue( lSubLocationFound, LINE_LOCATION.concat( " not in ignore list as expected" ) );
   }
}
