package com.mxi.am.stepdefn.persona.administrator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.inject.Inject;

import com.mxi.am.driver.web.CommonMessagePageDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.user.create.AssignOrganizationPageDriver;
import com.mxi.am.driver.web.user.create.CreateUserPageDriver;
import com.mxi.am.driver.web.user.search.UserSearchPageDriver;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class CreateUserPageStepDefinitions {

   private static final String MXERR_10000_MESSAGE =
         "[MXERR-10000] The 'HR Code' is a mandatory field.";
   @Inject
   private CreateUserPageDriver iCreateUserPageDriver;
   @Inject
   private CommonMessagePageDriver iCommonMessagePageDriver;
   @Inject
   private NavigationDriver iNavigationDriver;
   @Inject
   private UserSearchPageDriver iUserSearchPageDriver;
   @Inject
   private AssignOrganizationPageDriver iAssignOrganizationPageDriver;

   private static final String FIRSTNAME = "firstname" + System.currentTimeMillis();
   private static final String LASTNAME = "lastname" + System.currentTimeMillis();
   private static final String USERNAME = "username" + System.currentTimeMillis();
   private static final String USERSEARCH = "User Search";
   private static final String ADMINISTRATOR = "Administrator";


   @And( "^I can access the create user page$" )
   public void iCanAccessTheCreateUserPage() throws Throwable {

      iNavigationDriver.navigate( ADMINISTRATOR, USERSEARCH );
      iUserSearchPageDriver.clickCreateUser();

   }


   @Given( "^I attempt creating a user in the create user page, leaving HR code blank$" )
   public void iAttemptCreatingAUserInTheCreateUserPageLeavingHRCodeBlank() throws Throwable {
      iCreateUserPageDriver.setUsername( USERNAME );
      iCreateUserPageDriver.setFirstName( FIRSTNAME );
      iCreateUserPageDriver.setLastName( LASTNAME );
      iCreateUserPageDriver.ClickOk();
   }


   @When( "^I receive an error message about the HR code and get redirected to the create user page$" )
   public void iReceiveAnErrorMessageAboutTheHRCodeAndGetRedirectedToTheCreateUserPage()
         throws Throwable {
      assertTrue( iCommonMessagePageDriver.getMessage().contains( MXERR_10000_MESSAGE ) );
      iCommonMessagePageDriver.clickOk();
   }


   @Then( "^The data I previously entered persists in the fields$" )
   public void theDataIPreviouslyEnteredPersistsInTheFields() throws Throwable {
      assertEquals( FIRSTNAME, iCreateUserPageDriver.getTextFirstName() );
      assertEquals( LASTNAME, iCreateUserPageDriver.getTextLastName() );
      assertEquals( USERNAME, iCreateUserPageDriver.getTextUsername() );
   }


   @And( "^I perform an Organization Search and I return$" )
   public void iPerformAnOrganizationSearchAndIReturn() throws Throwable {
      iCreateUserPageDriver.ClickSelectOrganization();
      iAssignOrganizationPageDriver.ClickCancel();

   }
}
