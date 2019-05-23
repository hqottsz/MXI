package com.mxi.am.stepdefn.persona.engineer;

import static org.junit.Assert.assertEquals;

import com.google.inject.Inject;
import com.mxi.am.driver.query.SensitivityQueriesDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.bom.CreateEditPartGroupPageDriver;
import com.mxi.am.driver.web.part.partgroupdetailspage.PartGroupDetailsPageDriver;
import com.mxi.am.driver.web.part.partgroupsearchpage.PartGroupSearchPageDriver;
import com.mxi.am.stepdefn.persona.engineer.data.PartGroupSensitivitiesEditScenarioData;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class PartGroupSensitivitiesStepDefinitions {

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private PartGroupSearchPageDriver iPartGroupSearchPageDriver;

   @Inject
   private PartGroupDetailsPageDriver iPartGroupDetailsPageDriver;

   @Inject
   private CreateEditPartGroupPageDriver iCreateEditPartGroupPageDriver;

   @Inject
   private SensitivityQueriesDriver iSensitivityQueriesDriver;


   @Before( "@EditPartGroupSensitivitiesSetupData" )
   public void setupPartGroupSensitivitiesData() {
      iSensitivityQueriesDriver.insertSensitivities(
            PartGroupSensitivitiesEditScenarioData.PART_GROUP_INITIAL_SENSITIVITIES_CODES );
      iSensitivityQueriesDriver.activateSensitivities(
            PartGroupSensitivitiesEditScenarioData.PART_GROUP_INITIAL_SENSITIVITIES_CODES );
      iSensitivityQueriesDriver.assignAssemblySensitivities(
            PartGroupSensitivitiesEditScenarioData.DB_ID,
            PartGroupSensitivitiesEditScenarioData.ASSEMBLY_CODE,
            PartGroupSensitivitiesEditScenarioData.PART_GROUP_INITIAL_SENSITIVITIES_CODES );
      iSensitivityQueriesDriver.enablePartGroupSensitivities(
            PartGroupSensitivitiesEditScenarioData.DB_ID,
            PartGroupSensitivitiesEditScenarioData.PART_GROUP_CODE,
            PartGroupSensitivitiesEditScenarioData.PART_GROUP_NAME,
            PartGroupSensitivitiesEditScenarioData.PART_GROUP_INITIAL_SENSITIVITIES_CODES );
   }


   @After( "@EditPartGroupSensitivitiesSetupData" )
   public void teardownPartGroupSensitivitiesData() {
      iSensitivityQueriesDriver.disablePartGroupSensitivities(
            PartGroupSensitivitiesEditScenarioData.DB_ID,
            PartGroupSensitivitiesEditScenarioData.PART_GROUP_CODE,
            PartGroupSensitivitiesEditScenarioData.PART_GROUP_NAME,
            PartGroupSensitivitiesEditScenarioData.PART_GROUP_INITIAL_SENSITIVITIES_CODES );
      iSensitivityQueriesDriver.unassignAssembySensitivities(
            PartGroupSensitivitiesEditScenarioData.DB_ID,
            PartGroupSensitivitiesEditScenarioData.ASSEMBLY_CODE,
            PartGroupSensitivitiesEditScenarioData.PART_GROUP_INITIAL_SENSITIVITIES_CODES );
      iSensitivityQueriesDriver.deleteSensitivities(
            PartGroupSensitivitiesEditScenarioData.PART_GROUP_INITIAL_SENSITIVITIES_CODES );
   }


   @Given( "^I am editing a part group$" )
   public void iAmEditingAPartGroup() {
      iNavigationDriver.navigate( "Engineer", "Part Group Search" );
      iPartGroupSearchPageDriver
            .setPartGroupName( PartGroupSensitivitiesEditScenarioData.PART_GROUP_NAME )
            .clickSearch();
      iPartGroupSearchPageDriver.clickTabPartGroupsFound().getResults().get( 0 )
            .clickPartGroup( PartGroupSensitivitiesEditScenarioData.PART_GROUP_CODE );
   }


   @When( "^I modify the sensitivities for the part group$" )
   public void iModifyTheSensitivitiesForThePartGroup() {
      iPartGroupDetailsPageDriver.clickEdit();
      iCreateEditPartGroupPageDriver.selectSensitivities(
            PartGroupSensitivitiesEditScenarioData.PART_GROUP_SENSITIVITY_TO_DISABLE );
      iCreateEditPartGroupPageDriver.clickOk();
   }


   @Then( "^the part group is updated with the new configured sensitivities$" )
   public void thePartGroupIsUpdatedWithTheNewConfiguredSensitivities() {
      assertEquals( PartGroupSensitivitiesEditScenarioData.PART_GROUP_EDITED_SENSITIVITIES,
            iPartGroupDetailsPageDriver.getSensitivities() );
      iPartGroupDetailsPageDriver.clickTabHistory();
   }
}
