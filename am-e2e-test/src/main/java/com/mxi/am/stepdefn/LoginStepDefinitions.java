package com.mxi.am.stepdefn;

import javax.inject.Inject;

import org.junit.Assert;

import com.mxi.am.driver.web.CommonMessagePageDriver;
import com.mxi.am.driver.web.LoginPageDriver;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


/**
 * Sample step definitions
 */
public class LoginStepDefinitions {

   @Inject
   private LoginPageDriver iLoginDriver;

   @Inject
   private CommonMessagePageDriver iCommonMessagePage;

   public static final String CREWLEAD_USERNAME = "crewlead";
   public static final String CREWLEAD_PASSWORD = "password";
   public static final String ENGINEER_USERNAME = "engineer";
   public static final String ENGINEER_PASSWORD = "password";
   public static final String INVOICING_AGENT_USERNAME = "invcage1";
   public static final String INVOICING_AGENT_PASSWORD = "password";
   public static final String LINE_PLANNER_USERNAME = "lineplanner";
   public static final String LINE_PLANNER_PASSWORD = "password";
   public static final String LINE_SUPERVISOR_USERNAME = "linesupervisor";
   public static final String LINE_SUPERVISOR_PASSWORD = "password";
   public static final String LINE_TECHNICIAN_USERNAME = "linetech";
   public static final String LINE_TECHNICIAN_PASSWORD = "password";
   public static final String MAINTENANCE_CONTROLLER_USERNAME = "maintcontrol";
   public static final String MAINTENANCE_CONTROLLER_PASSWORD = "password";
   public static final String PURCHASING_AGENT_USERNAME = "purcha1";
   public static final String PURCHASING_AGENT_PASSWORD = "password";
   public static final String PURCHASE_MANAGER_USERNAME = "user1";
   public static final String PURCHASE_MANAGER_PASSWORD = "password";
   public static final String SUPERUSER_USERNAME = "mxi";
   public static final String SUPERUSER_PASSWORD = "password";
   public static final String TECHNICAL_RECORDS_CLERK_USERNAME = "techrec";
   public static final String TECHNICAL_RECORDS_CLERK_PASSWORD = "password";
   public static final String MATERIAL_CONTROLLER_USERNAME = "mxi";
   public static final String MATERIAL_CONTROLLER_PASSWORD = "password";
   public static final String STOREROOM_CLERK_USERNAME = "storeroomclerk";
   public static final String STOREROOM_CLERK_PASSWORD = "password";
   public static final String QC_INSPECTOR_USERNAME = "qcinsp1";
   public static final String QC_INSPECTOR_PASSWORD = "password";


   @Given( "^I am \"([^\"]*)\"$" )
   public void login( String aUsername ) {
      login( aUsername, "password" );
   }


   @Given( "^I am user(\\d+)$" )
   public void loginUser( String aUserNumber ) {
      login( "user" + aUserNumber, "password" );
   }


   @When( "^I login as \"([^\"]*)\" with password \"([^\"]*)\"$" )
   public void login( String aUsername, String aPassword ) {
      iLoginDriver.setUserName( aUsername ).setPassword( aPassword ).login();

   }


   /**
    * Assert for any role.
    */
   @Then( "^I should be logged in$" )
   public void assertLoggedIn() {
      Assert.assertNotNull( iCommonMessagePage.getLogOut() );
   }


   @Then( "^I should not be logged in$" )
   public void assertNotLoggedIn() {
      Assert.assertEquals( "Log in failed.", iLoginDriver.getUserMessage(),
            "Incorrect username and/or password" );
   }


   @Given( "^I am a maintenance program engineer$" )
   public void iAmAMaintenanceProgramEngineer() throws Throwable {
      iLoginDriver.setUserName( ENGINEER_USERNAME ).setPassword( ENGINEER_PASSWORD ).login();
   }


   @Given( "^I am an engineer$" )
   public void iAmAnEngineer() throws Throwable {
      iLoginDriver.setUserName( ENGINEER_USERNAME ).setPassword( ENGINEER_PASSWORD ).login();
   }


   @Given( "^I am an invoicing agent$" )
   public void iAmAnInvoicingAgent() throws Throwable {
      iLoginDriver.setUserName( INVOICING_AGENT_USERNAME ).setPassword( INVOICING_AGENT_PASSWORD )
            .login();
   }


   @Given( "^I am a line planner$" )
   public void iAmALinePlanner() throws Throwable {
      iLoginDriver.setUserName( LINE_PLANNER_USERNAME ).setPassword( LINE_PLANNER_PASSWORD )
            .login();
   }


   @Given( "^I am a maintenance controller$" )
   public void iAmAMaintenanceController() throws Throwable {
      iLoginDriver.setUserName( MAINTENANCE_CONTROLLER_USERNAME )
            .setPassword( MAINTENANCE_CONTROLLER_PASSWORD ).login();
   }


   @Given( "^I am a line supervisor$" )
   public void iAmALineSupervisor() throws Throwable {
      iLoginDriver.setUserName( LINE_SUPERVISOR_USERNAME ).setPassword( LINE_SUPERVISOR_PASSWORD )
            .login();
   }


   @Given( "^I am a line technician$" )
   public void iAmALineTech() throws Throwable {
      iLoginDriver.setUserName( LINE_TECHNICIAN_USERNAME ).setPassword( LINE_TECHNICIAN_PASSWORD )
            .login();
   }


   @Given( "^I am a purchasing agent$" )
   public void iAmAPurchasingAgent() throws Throwable {

      iLoginDriver.setUserName( PURCHASING_AGENT_USERNAME ).setPassword( PURCHASING_AGENT_PASSWORD )
            .login();
   }


   @Given( "^I am a purchase manager$" )
   public void iAmAPurchaseManager() throws Throwable {

      iLoginDriver.setUserName( PURCHASE_MANAGER_USERNAME ).setPassword( PURCHASE_MANAGER_PASSWORD )
            .login();
   }


   @Given( "^I am a superuser$" )
   public void iAmASuperUser() throws Throwable {
      iLoginDriver.setUserName( SUPERUSER_USERNAME ).setPassword( SUPERUSER_PASSWORD ).login();
   }


   @Given( "^I am a crew lead$" )
   public void iAmACrewLead() throws Throwable {
      iLoginDriver.setUserName( CREWLEAD_USERNAME ).setPassword( CREWLEAD_PASSWORD ).login();
   }


   @Given( "^I am a technical records clerk$" )
   public void iAmATechRecords() throws Throwable {

      iLoginDriver.setUserName( TECHNICAL_RECORDS_CLERK_USERNAME )
            .setPassword( TECHNICAL_RECORDS_CLERK_PASSWORD ).login();
   }


   @Given( "^I am a material controller$" )
   public void iAmAMaterialController() throws Throwable {
      iLoginDriver.setUserName( MATERIAL_CONTROLLER_USERNAME )
            .setPassword( MATERIAL_CONTROLLER_PASSWORD ).login();
   }


   @Given( "^I am a storeroom clerk$" )
   public void iAmAStoreroomClerk() throws Throwable {
      iLoginDriver.setUserName( STOREROOM_CLERK_USERNAME ).setPassword( STOREROOM_CLERK_PASSWORD )
            .login();
   }


   @Given( "^I am a QC inspector$" )
   public void iAmAQcInspector() throws Throwable {
      iLoginDriver.setUserName( QC_INSPECTOR_USERNAME ).setPassword( QC_INSPECTOR_PASSWORD )
            .login();
   }
}
