package com.mxi.am.stepdefn.persona.linesupervisor;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;

import com.mxi.am.driver.common.MessagePageDriver;
import com.mxi.am.driver.common.configurationParameters.ActionConfigurationParameterDriver;
import com.mxi.am.driver.query.HumanResourceQueriesDriver;
import com.mxi.am.driver.web.LogoutPageDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.location.locationdetailspage.LocationDetailsPageDriver;
import com.mxi.am.driver.web.user.search.UserSearchPageDriver;
import com.mxi.am.driver.web.user.userdetailspage.UserDetailsPageDriver;
import com.mxi.am.driver.web.user.userdetailspage.userdetailspanes.AssignTemporaryRolePageDriver;
import com.mxi.am.driver.web.user.userdetailspage.userdetailspanes.UserDetailsRolesPaneDriver;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


/**
 * Step definitions for assigning temporary roles.
 */
public class AssignTemporaryRoleStepDefinitions {

   public static final String ROLE_ASSIGNER_USER = "linesupervisor";
   public static final int ROLE_ASSIGNER_ROLE_ID = 10001;

   public static final String ROLE_ASSIGNEE_USER = "linetech";
   public static final String ROLE_ASSIGNEE_DISPLAYNAME = "Line Maintenance Technician";

   public static final String ROLE_ASSIGNMENT_LABEL = "HVYLEAD (Crew Lead)";
   public static final String ROLE_ASSIGNMENT_CODE = "HVYLEAD";
   public static final int ROLE_ASSIGNMENT_ID = 10007;

   @Inject
   HumanResourceQueriesDriver iHRDriver;

   @Inject
   ActionConfigurationParameterDriver iActionParmDriver;

   @Inject
   MessagePageDriver iMessagePageDriver;

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private LogoutPageDriver iLogoutPageDriver;

   @Inject
   private UserSearchPageDriver iUserSearchPageDriver;

   @Inject
   private UserDetailsPageDriver iUserDetailsPageDriver;

   @Inject
   private LocationDetailsPageDriver iLocationDetailsPageDriver;

   @Inject
   private AssignTemporaryRolePageDriver iAssignTemporaryRolePageDriver;


   @Given( "^I am able to assign temporary roles$" )
   public void iAmAbleToAssignTemporaryRole() throws Throwable {

      // Verify temp role assignment configuration.

      if ( !iActionParmDriver.isTempRoleAssignmentConfigured() ) {
         iActionParmDriver.setTempRoleAssignmentConfigured( true );
      }
      if ( !iActionParmDriver.isAccessTempRoleAssignmentConfigured() ) {
         iActionParmDriver.setAccessTempRoleAssignmentConfigured( true );
      }
      if ( !iHRDriver.getAssignableRoles( ROLE_ASSIGNER_USER )
            .containsKey( "" + ROLE_ASSIGNMENT_ID ) ) {
         iHRDriver.addAssignableRole( ROLE_ASSIGNER_ROLE_ID, ROLE_ASSIGNMENT_ID );
      }

      Assert.assertTrue( iActionParmDriver.isTempRoleAssignmentConfigured() );
      Assert.assertTrue( iActionParmDriver.isAccessTempRoleAssignmentConfigured() );
      Assert.assertTrue( iHRDriver.getAssignableRoles( ROLE_ASSIGNER_USER )
            .containsKey( "" + ROLE_ASSIGNMENT_ID ) );
      iHRDriver.clearTempRoles();
   }


   @Given( "^I am able to unassign temporary roles$" )
   public void iAmAbleToUnassignTemporaryRole() throws Throwable {

      // Verify temp role unassignment configuration.

      if ( !iActionParmDriver.isTempRoleUnassignmentConfigured() ) {
         iActionParmDriver.setTempRoleUnassignmentConfigured( true );
      }
      Assert.assertTrue( iActionParmDriver.isTempRoleUnassignmentConfigured() );
   }


   @When( "^I find a line technician in my department$" )
   public void iLocateMyLineTech() throws Throwable {

      // Navigate to the user's department to locate a associated user.

      iNavigationDriver.navigate( "References", "My User Details" );
      iUserDetailsPageDriver.clickTabDepartments().clickDepartment( 0 );
      iLocationDetailsPageDriver.clickTabUsers().clickUser( ROLE_ASSIGNEE_DISPLAYNAME );
   }


   @When( "^I assign a temporary role to the user for today$" )
   public void iAssignTemporaryRoleToALineTechnicianForToday() throws Throwable {

      // Invoke the temporary role assignment dialog and fill it in.

      Date lToday = new Date();
      Date lTomorrow = new Date( lToday.getTime() + 24 * 60 * 60 * 1000 );

      iUserDetailsPageDriver.clickTabRoles().clickAssignTempRole();
      iAssignTemporaryRolePageDriver.selectRole( ROLE_ASSIGNMENT_LABEL );
      iAssignTemporaryRolePageDriver.setStartDate( lToday );
      iAssignTemporaryRolePageDriver.setExpiryDate( lTomorrow );
      iAssignTemporaryRolePageDriver.clickOK();
   }


   @Then( "^the temporary role is assigned to the technician$" )
   public void theUserProfileShowsTempRole() throws Throwable {

      // Verify that the temp role displays on the user details page.

      UserDetailsRolesPaneDriver lRolesPane = iUserDetailsPageDriver.getTabRoles();

      Assert.assertFalse(
            lRolesPane.getTempRoleTable().findRowIndex( ROLE_ASSIGNMENT_LABEL ) == -1 );
   }


   @Then( "^I see an error message for duplicate role assignment$" )
   public void iSeeAnErrorMessageForDuplicateRoleAssignment() throws Throwable {

      // Verify that the user see's an error message if they attempt duplicate a temp role
      // assignment.

      Assert.assertTrue( iMessagePageDriver.getCellMessage().contains( "MXERR-33509" ) );
      iMessagePageDriver.clickOk();
      iAssignTemporaryRolePageDriver.clickCancel();
      iNavigationDriver.clickLogout();
      iLogoutPageDriver.clickOK();
   }


   @Then( "^I have the active role$" )
   public void iHaveTheActiveRole() throws Throwable {

      // Log in to refresh the user's active roles and verify.

      List<String> lRoles = iHRDriver.getActiveRoles( ROLE_ASSIGNEE_USER );

      iNavigationDriver.clickLogout();
      iLogoutPageDriver.clickOK();

      Assert.assertTrue( lRoles.contains( ROLE_ASSIGNMENT_CODE ) );
   }


   @When( "^I unassign a temporary role from the user$" )
   public void iUnassignATemporaryRoleFromALineTechnician() throws Throwable {

      // Unassign the temporary role from user.

      UserDetailsRolesPaneDriver lRolesPane = iUserDetailsPageDriver.getTabRoles();
      lRolesPane.setTempRole( ROLE_ASSIGNMENT_LABEL ).clickUnassignTempRole();
   }


   @Then( "^the temporary role is not assigned to the technician$" )
   public void theLineTechnicianProfileDoesNotHaveRole() throws Throwable {

      // Verify that the temporary role does not exist on the user's details page in the roles tab.

      UserDetailsRolesPaneDriver lRolesPane = iUserDetailsPageDriver.getTabRoles();

      Assert.assertTrue(
            lRolesPane.getTempRoleTable().findRowIndex( ROLE_ASSIGNMENT_LABEL ) == -1 );

      iNavigationDriver.clickLogout();
      iLogoutPageDriver.clickOK();
   }


   @Then( "^I do not have the active role$" )
   public void theLineTechnicianNoLongerHasTheTemporaryRole() throws Throwable {

      // Log in to refresh the user's active roles and verify.

      List<String> lRoles = iHRDriver.getActiveRoles( ROLE_ASSIGNEE_USER );

      iNavigationDriver.clickLogout();
      iLogoutPageDriver.clickOK();

      Assert.assertFalse( lRoles.contains( ROLE_ASSIGNMENT_CODE ) );
   }


   @When( "^I find a line technician$" )
   public void iFindALineTech() throws Throwable {

      // Navigate to the User Search page, search for a user and click the link to go to that user's
      // User Details page.

      iNavigationDriver.navigate( "Administrator", "User Search" );
      iUserSearchPageDriver.setUserName( ROLE_ASSIGNEE_USER );
      iUserSearchPageDriver.clickSearch();
      iUserSearchPageDriver.clickTabUsersFound().clickUser( 0 );
   }

}
