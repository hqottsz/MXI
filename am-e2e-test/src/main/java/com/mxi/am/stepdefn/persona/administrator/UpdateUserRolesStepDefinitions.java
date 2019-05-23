package com.mxi.am.stepdefn.persona.administrator;

import javax.inject.Inject;

import org.junit.Assert;

import com.mxi.am.driver.ppc.Wait;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.role.search.RoleSearchPageDriver;
import com.mxi.am.driver.web.user.userdetailspage.UserDetailsPageDriver;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class UpdateUserRolesStepDefinitions {

   private static final String REFERENCES = "References";
   private static final String MY_USER_DETAILS = "My User Details";
   private static final String ENGINEER_ROLE = "ENG (Engineer)";
   private static final String ENGINEER_MENU = "Engineer";
   private static final String TO_DO_LIST_ENGINEER = "To Do List (Engineer)";
   private static final String MATERIAL_CONTROLLER_ROLE = "MATCTRL (Material Controller)";
   private static final String MATERIAL_CONTROLLER_MENU = "Material Controller";
   private static final String TO_DO_LIST_MATERIAL_CONTROLLER =
         "Control To Do List (Material Controller)";

   @Inject
   private NavigationDriver iNavigationDriver;
   @Inject
   private UserDetailsPageDriver iUserDetailsPageDriver;
   @Inject
   private RoleSearchPageDriver iRoleSearchPageDriver;


   @When( "^I add a role to the active user$" )
   public void iAddARoleToTheActiveUser() throws Throwable {
      iNavigationDriver.navigate( REFERENCES, MY_USER_DETAILS );
      iUserDetailsPageDriver.clickTabRoles().clickAssignRole();
      iRoleSearchPageDriver.clickSearch();
      iRoleSearchPageDriver.clickRolesFoundTab().clickCheckboxForRole( ENGINEER_ROLE );
      iRoleSearchPageDriver.clickRolesFoundTab().clickAssignRole();
      Wait.pause( 1000 );
   }


   @When( "^I remove an existing role from the active user$" )
   public void iRemoveAnExistingRoleFromTheActiveUser() throws Throwable {
      iNavigationDriver.navigate( REFERENCES, MY_USER_DETAILS );
      iUserDetailsPageDriver.clickTabRoles().setRole( MATERIAL_CONTROLLER_ROLE );
      iUserDetailsPageDriver.clickTabRoles().clickUnassignRole();
   }


   @Then( "^The menu dropdown includes the added role menu items$" )
   public void theMenuDropdownIncludesTheAddedRoleMenuItems() throws Throwable {
      iNavigationDriver.navigate( ENGINEER_MENU, TO_DO_LIST_ENGINEER );
   }


   @Then( "^The menu dropdown no longer includes the role menu items$" )
   public void theMenuDropdownNoLongerIncludesTheRoleMenuItems() throws Throwable {
      try {
         iNavigationDriver.navigate( MATERIAL_CONTROLLER_MENU, TO_DO_LIST_MATERIAL_CONTROLLER );
         Assert.fail( "Material Controller menu should not be visible." );
      } catch ( RuntimeException e ) {
         // Do nothing
      }
   }

}
