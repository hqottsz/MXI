package com.mxi.am.stepdefn.persona.maintenancecontroller.deferralreference;

import static org.junit.Assert.assertEquals;

import com.google.inject.Inject;
import com.mxi.am.driver.common.database.DatabaseDriver;
import com.mxi.am.driver.web.AssetManagement;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.lmoc.deferralreferenceconfiguration.DeferralReferenceCreatePageDriver;
import com.mxi.am.driver.web.lmoc.deferralreferenceconfiguration.DeferralReferenceDetailsDriver;
import com.mxi.am.driver.web.lmoc.deferralreferenceconfiguration.DeferralReferenceEditPageDriver;
import com.mxi.am.driver.web.lmoc.deferralreferenceconfiguration.DeferralReferenceSearchPageDriver;
import com.mxi.am.driver.web.lmoc.deferralreferenceconfiguration.model.DeferralReference;
import com.mxi.am.stepdefn.persona.maintenancecontroller.deferralreference.data.CreateDeferralReferenceScenarioData;
import com.mxi.am.stepdefn.persona.maintenancecontroller.deferralreference.data.EditDeferralReferenceScenarioData;
import com.mxi.am.stepdefn.persona.maintenancecontroller.deferralreference.data.ViewDeferralReferenceScenarioData;

import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class DeferralReferenceStepDefinitions {

   private static final String MOC_CONTROLLER_MENU_GROUP = "Maintenance Controller";
   private static final String DEFERRAL_REFERENCE_SEARCH_MENU_ITEM = "Deferral Reference Search";
   @Inject
   @AssetManagement
   private DatabaseDriver iDatabaseDriver;

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private DeferralReferenceSearchPageDriver iDeferralReferenceSearchPageDriver;

   @Inject
   private DeferralReferenceDetailsDriver iDeferralReferenceDetailsDriver;

   @Inject
   private DeferralReferenceCreatePageDriver iDeferralReferenceCreatePageDriver;

   @Inject
   private DeferralReferenceEditPageDriver iDeferralReferenceEditPageDriver;


   @And( "^I access the Deferral Reference Workflow$" )
   public void navigateToDeferralReferenceSearchPage() {
      iNavigationDriver.navigate( MOC_CONTROLLER_MENU_GROUP, DEFERRAL_REFERENCE_SEARCH_MENU_ITEM );
   }


   // Scenario: Maintenance Controller searches for deferral reference

   @When( "^I search for the deferral reference from the search results$" )
   public void selectDeferralReference() {
      iDeferralReferenceSearchPageDriver
            .pickDeferralReference( ViewDeferralReferenceScenarioData.DEFERRAL_DEFERENCE_NAME );
   }


   @Then( "^I see details for the selected deferral reference$" )
   public void iSeeDetailsForTheSelectedDeferralReference() {

      DeferralReference lExpectedDeferralReference =
            ViewDeferralReferenceScenarioData.getDeferralReference();
      DeferralReference lActualDeferralReference = iDeferralReferenceDetailsDriver.getDetails();

      assertEquals( lExpectedDeferralReference, lActualDeferralReference );
   }


   @When( "^I edit the deferral reference with a custom deadline$" )
   public void editTheDeferralReferenceWithADefaultDeadline() throws Throwable {

      iDeferralReferenceSearchPageDriver
            .pickDeferralReference( EditDeferralReferenceScenarioData.DEFERRAL_DEFERENCE_NAME_ONE );

      iDeferralReferenceSearchPageDriver.navigateToEditPage();
      iDeferralReferenceEditPageDriver.getDeferralReferenceConfiguationPane()
            .clearDeferralReferenceFields();

      iDeferralReferenceEditPageDriver.getDeferralReferenceConfiguationPane()
            .setDetails( EditDeferralReferenceScenarioData.getDeferralReferenceForEdit() );
      iDeferralReferenceEditPageDriver.getDeferralReferenceConfiguationPane()
            .saveDeferralReference();

   }


   @Then( "^I should receive successful feedback of the updated deferral reference$" )
   public void iShouldReceiveSuccessfulFeedbackOfTheUpdatedDeferralReference() throws Throwable {
      assertEquals( EditDeferralReferenceScenarioData.DEFERRAL_REFERENCE_TOAST_NOTIFICATION,
            iDeferralReferenceEditPageDriver.getDeferralReferenceConfiguationPane()
                  .getToastNotificationText() );
   }


   @Then( "^the deferral reference has been updated in the system$" )
   public void theDeferralReferenceHasBeenUpdatedInTheSystem() throws Throwable {

      DeferralReference lExpectedDeferralReference =
            EditDeferralReferenceScenarioData.getEditedDeferralReference();
      DeferralReference lActualDeferralReference = iDeferralReferenceDetailsDriver.getDetails();

      assertEquals( lExpectedDeferralReference, lActualDeferralReference );
   }


   @When( "^I create a deferral reference$" )
   public void createDeferralReference() {
      iDeferralReferenceSearchPageDriver.navigateToCreatePage();
      iDeferralReferenceCreatePageDriver.getDeferralReferenceConfiguationPane()
            .setDetails( CreateDeferralReferenceScenarioData.getDeferralReferenceToCreate() );
      iDeferralReferenceCreatePageDriver.getDeferralReferenceConfiguationPane()
            .saveDeferralReference();
   }


   @Then( "^I receive feedback indicating the deferral reference was saved$" )
   public void verifyFeedback() {
      assertEquals( CreateDeferralReferenceScenarioData.DEFERRAL_REFERENCE_TOAST_NOTIFICATION,
            iDeferralReferenceCreatePageDriver.getDeferralReferenceConfiguationPane()
                  .getToastNotificationText() );
   }


   @Then( "^the deferral reference has been saved in the system$" )
   public void verifyDeferralReferenceSavedInSystem() {
      iDeferralReferenceCreatePageDriver.getDeferralReferenceConfiguationPane()
            .clickToastMessageDeferralReferenceName();

      DeferralReference lExpectedDeferralReference =
            CreateDeferralReferenceScenarioData.getCreatedDeferralReference();
      DeferralReference lActualDeferralReference = iDeferralReferenceDetailsDriver.getDetails();

      assertEquals( lExpectedDeferralReference, lActualDeferralReference );

   }


   @Before( "@CreateDeferralReference" )
   public void createDeferralReferenceQuery() {
      iDatabaseDriver.insert( EditDeferralReferenceScenarioData.CREATE_DEFERRAL_REFERENCE );
      iDatabaseDriver
            .insert( EditDeferralReferenceScenarioData.CREATE_DEFERRAL_REFERENCE_OPERATIONS );
   }

}
