package com.mxi.am.stepdefn.persona.engineer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.inject.Inject;

import com.mxi.am.driver.query.SensitivityQueriesDriver;
import com.mxi.am.driver.web.assembly.details.AssemblyDetailsPageDriver;
import com.mxi.am.driver.web.assembly.sensitivities.assign.AssignSensitivitiesPageDriver;
import com.mxi.am.driver.web.todolist.ToDoListPageDriver;
import com.mxi.am.stepdefn.persona.engineer.data.AssemblySensitivitiesEditScenarioData;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class AssemblySensitivitiesStepDefinitions {

   @Inject
   private SensitivityQueriesDriver iSensitivityQueriesDriver;

   @Inject
   private ToDoListPageDriver iToDoListPageDriver;

   @Inject
   private AssemblyDetailsPageDriver iAssemblyDetailsPageDriver;

   @Inject
   private AssignSensitivitiesPageDriver iAssignSensitivitiesPageDriver;


   @Given( "^I am assigning sensitivities to an assembly$" )
   public void assignSensitivitiesToAnAssembly() {
      iToDoListPageDriver.clickTabAssemblyList()
            .clickAssembly( AssemblySensitivitiesEditScenarioData.ASSEMBLY_NAME );
      iAssemblyDetailsPageDriver.clickTabSensitivities();
   }


   @When( "^I modify the sensitivities for the assembly$" )
   public void iModifyTheSensitivitiesForTheAssembly() throws Throwable {
      iAssemblyDetailsPageDriver.getTabSensitivities().clickAssignSensitivity();
      iAssignSensitivitiesPageDriver
            .toggleSensitivity( AssemblySensitivitiesEditScenarioData.SENSITIVITY_TO_ASSIGN, true );
      iAssignSensitivitiesPageDriver.toggleSensitivity(
            AssemblySensitivitiesEditScenarioData.SENSITIVITY_TO_UNASSIGN, false );
      iAssignSensitivitiesPageDriver.clickOk();
   }


   @Then( "^I should see the sensitivities tab with the updated list of sensitivities$" )
   public void viewSensitivitiesOfAnAssembly() {
      iAssemblyDetailsPageDriver.clickTabSensitivities();
      assertTrue( iAssemblyDetailsPageDriver.getTabSensitivities()
            .isSensitivityInTable( AssemblySensitivitiesEditScenarioData.SENSITIVITY_TO_ASSIGN ) );
      assertFalse( iAssemblyDetailsPageDriver.getTabSensitivities().isSensitivityInTable(
            AssemblySensitivitiesEditScenarioData.SENSITIVITY_TO_UNASSIGN ) );
   }


   @Before( "@AssignAssemblySensitivitiesSetupData" )
   public void assignAssemblySensitivitiesSetupData() {
      iSensitivityQueriesDriver
            .insertSensitivities( AssemblySensitivitiesEditScenarioData.ACTIVE_SENSITIVITIES );
      iSensitivityQueriesDriver
            .activateSensitivities( AssemblySensitivitiesEditScenarioData.ACTIVE_SENSITIVITIES );
      iSensitivityQueriesDriver.assignAssemblySensitivities(
            AssemblySensitivitiesEditScenarioData.ASSEMBLY_DB_ID,
            AssemblySensitivitiesEditScenarioData.ASSEMBLY_CD,
            AssemblySensitivitiesEditScenarioData.ASSEMBLY_ASSIGNED_SENSITIVITIES_INITIAL );
   }


   @After( "@AssignAssemblySensitivitiesSetupData" )
   public void assignAssemblySensitivitiesTeardownData() {

      iSensitivityQueriesDriver.unassignAssembySensitivities(
            AssemblySensitivitiesEditScenarioData.ASSEMBLY_DB_ID,
            AssemblySensitivitiesEditScenarioData.ASSEMBLY_CD,
            AssemblySensitivitiesEditScenarioData.ACTIVE_SENSITIVITIES );
      iSensitivityQueriesDriver
            .deactivateSensitivities( AssemblySensitivitiesEditScenarioData.ACTIVE_SENSITIVITIES );
      iSensitivityQueriesDriver
            .deleteSensitivities( AssemblySensitivitiesEditScenarioData.ACTIVE_SENSITIVITIES );
   }

}
