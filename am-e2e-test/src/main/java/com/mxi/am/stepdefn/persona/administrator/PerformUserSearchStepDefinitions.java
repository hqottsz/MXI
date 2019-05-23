package com.mxi.am.stepdefn.persona.administrator;

import javax.inject.Inject;

import org.junit.Assert;

import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.user.search.UserSearchPageDriver;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


/**
 * Step definitions for testing User Search Page.
 */
public class PerformUserSearchStepDefinitions {

   public static final String SEARCH_TARGET_USERNAME = "linetech";
   public static final String SEARCH_TARGET_DISPLAYNAME = "Line Maintenance Technician";

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private UserSearchPageDriver iUserSearchPageDriver;


   @When( "^I search for a line technician$" )
   public void iFindALineTech() throws Throwable {
      iNavigationDriver.navigate( "Administrator", "User Search" );
      iUserSearchPageDriver.setUserName( SEARCH_TARGET_USERNAME );
      iUserSearchPageDriver.clickSearch();
   }


   @Then( "^I find the line technician$" )
   public void iFindTheLineTech() throws Throwable {
      Assert.assertTrue( iUserSearchPageDriver.clickTabUsersFound()
            .isResultContains( SEARCH_TARGET_DISPLAYNAME ) );
   }

}
