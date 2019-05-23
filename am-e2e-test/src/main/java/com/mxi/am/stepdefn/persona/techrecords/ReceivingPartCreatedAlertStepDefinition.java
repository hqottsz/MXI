package com.mxi.am.stepdefn.persona.techrecords;

import static com.mxi.am.stepdefn.LoginStepDefinitions.MATERIAL_CONTROLLER_PASSWORD;
import static com.mxi.am.stepdefn.LoginStepDefinitions.MATERIAL_CONTROLLER_USERNAME;
import static com.mxi.am.stepdefn.LoginStepDefinitions.TECHNICAL_RECORDS_CLERK_PASSWORD;
import static com.mxi.am.stepdefn.LoginStepDefinitions.TECHNICAL_RECORDS_CLERK_USERNAME;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.UUID;

import javax.inject.Inject;

import com.mxi.am.driver.web.AuthenticationRequiredPageDriver;
import com.mxi.am.driver.web.LoginPageDriver;
import com.mxi.am.driver.web.LogoutPageDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.common.UserAlertsPageDriver;
import com.mxi.am.driver.web.common.useralerts.UnassignedAlertsPaneDriver;
import com.mxi.am.driver.web.common.useralerts.UnassignedAlertsPaneDriver.AlertRow;
import com.mxi.am.driver.web.manufacturer.ManufacturerSearchPageDriver;
import com.mxi.am.driver.web.part.CreatePartPageDriver;
import com.mxi.am.driver.web.part.partsearchpage.PartSearchPageDriver;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;


@ScenarioScoped
public class ReceivingPartCreatedAlertStepDefinition {

   @Inject
   private NavigationDriver navigationDriver;

   @Inject
   private LoginPageDriver loginPageDriver;

   @Inject
   private LogoutPageDriver logoutPageDriver;

   @Inject
   private PartSearchPageDriver partSearchPageDriver;

   @Inject
   private CreatePartPageDriver createPartPageDriver;

   @Inject
   private ManufacturerSearchPageDriver manufacturerSearchPageDriver;

   @Inject
   private AuthenticationRequiredPageDriver authenticationRequiredPageDriver;

   @Inject
   private UserAlertsPageDriver userAlertsPageDriver;

   private static final String TRACKED_INVENTORY_CLASS = "TRK (Tracked Item)";

   private String partNumber;
   private AlertRow alert;


   @Given( "^that technical records clerks are configured to receive Part Created alerts$" )
   public void thatTechnicalRecordsClerksAreConfiguredToReceivePartCreatedAlerts()
         throws Throwable {
      // This setup is performed by the data loading, in particular the loading of 10-level data.
      // Refer to assetmanagement-database/src/solution/plsql/system/10utl_alert_type_role.sql
      // alert_type_id = 22, role_id = 10014
   }


   @When( "^another user creates a new part$" )
   public void anotherUserCreatesANewPart() throws Throwable {
      loginPageDriver.setUserName( MATERIAL_CONTROLLER_USERNAME )
            .setPassword( MATERIAL_CONTROLLER_PASSWORD ).login();
      navigationDriver.navigate( "Material Controller", "Part Search" );
      partSearchPageDriver.clickTabPartsFound().clickCreatePart();

      partNumber = generateRandomPartNumber();
      String partName = partNumber;
      createPartPageDriver.setOemPartNo( partNumber );
      createPartPageDriver.setPartName( partName );
      createPartPageDriver.setInventoryClass( TRACKED_INVENTORY_CLASS );

      // By default use the first manufacturer in list.
      createPartPageDriver.clickSelectManufacturer();
      manufacturerSearchPageDriver.clickSearch();
      manufacturerSearchPageDriver.clickTabManufacturersFound().clickAssignManufacturer();

      createPartPageDriver.clickOkButton();
      authenticationRequiredPageDriver.setPasswordAndClickOK( MATERIAL_CONTROLLER_PASSWORD );

      navigationDriver.clickLogout();
      logoutPageDriver.clickOK();
   }


   @Then( "^a technical records clerk is notified with an alert$" )
   public void aTechnicalRecordsClerkIsNotifiedWithAnAlert() throws Throwable {
      loginPageDriver.setUserName( TECHNICAL_RECORDS_CLERK_USERNAME )
            .setPassword( TECHNICAL_RECORDS_CLERK_PASSWORD ).login();
      assertTrue( "Alert notification unexpectedly not displayed.",
            navigationDriver.isAlertNotificationDisplayed() );
   }


   @Then( "^the alert indicates the part number of the new part$" )
   public void theAlertIndicatesThePartNumberOfTheNewPart() throws Throwable {
      navigationDriver.clickAlertNotification();
      alert = findUnassignedPartCreatedAlertByPartNumber( partNumber );
      assertNotNull( "Part Created alert not generated for part number = " + partNumber, alert );
   }


   private String generateRandomPartNumber() {
      // Note; the current max size of eqp_part_no.part_no_oem is 40 characters.
      // And UUIDs are 32 characters, leaving only 4 characters.
      // "AP" stands for "alert part", in case you are wondering.
      String uuidStr = UUID.randomUUID().toString();
      String partNumber = "AP" + uuidStr;
      return partNumber;
   }


   private AlertRow findUnassignedPartCreatedAlertByPartNumber( String partNumber ) {
      UnassignedAlertsPaneDriver unassignedAlertsPaneDriver =
            userAlertsPageDriver.clickTabUnassignedAlerts();
      for ( AlertRow alert : unassignedAlertsPaneDriver.getUnassignedAlertLines() ) {
         String msg = alert.getMessage();
         if ( msg != null && msg.contains( partNumber.toUpperCase() ) ) {
            return alert;
         }
      }
      return null;
   }

}
