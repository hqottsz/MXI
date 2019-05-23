package com.mxi.am.stepdefn.persona.productionplanner;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.log4j.Logger;
import org.junit.Assert;

import com.mxi.am.driver.ppc.login.LoginDialogDriver;
import com.mxi.am.driver.ppc.wizard.PageOneDriver;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


/**
 * These actions are for the login page interactions
 */
@Singleton
public class PPCLoginStepDefinitions {

   private static final String MAINTENIX_HOST = System.getenv( "MAINTENIX_HOST" ) == null
         ? "localhost" : System.getenv( "MAINTENIX_HOST" );

   private static final Logger LOGGER = Logger.getLogger( PPCLoginStepDefinitions.class );

   @Inject
   private LoginDialogDriver iLoginDialog;

   @Inject
   private PageOneDriver iWizardPageOne;


   @Given( "^PPC client is launched$" )
   public void launchPPC() throws Throwable {
      Assert.assertTrue( iLoginDialog.isAvailable() );
   }


   @When( "^I connect to PPC as \"([^\"]*)\"$" )
   public void logIn( String aUsername ) throws Throwable {

      LOGGER.info( "Login as " + aUsername );
      iLoginDialog.setServer( MAINTENIX_HOST ).setUsername( aUsername ).setPassword( "password" )
            .clickLogin();
   }


   @Then( "^I should be logged into PPC$" )
   public void assertLoggedIn() {
      Assert.assertTrue( iWizardPageOne.isWizardAvailable() );
   }


   @Then( "^I should not be logged into PPC$" )
   public void assertNotLoggedIn() {
      Assert.assertFalse( iWizardPageOne.isWizardAvailable() );
   }
}
