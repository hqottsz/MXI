package com.mxi.am.stepdefn.persona.engineer;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.inject.Inject;

import com.mxi.am.driver.query.SensitivityQueriesDriver;
import com.mxi.am.driver.web.assembly.details.AssemblyDetailsPageDriver;
import com.mxi.am.driver.web.bom.ConfigSlotDetailsPageDriver;
import com.mxi.am.driver.web.bom.CreateEditConfigSlotPageDriver;
import com.mxi.am.driver.web.todolist.ToDoListPageDriver;
import com.mxi.am.stepdefn.persona.engineer.data.SystemSensitivitiesCreateScenarioData;
import com.mxi.am.stepdefn.persona.engineer.data.SystemSensitivitiesEditScenarioData;
import com.mxi.am.stepdefn.persona.engineer.data.SystemSensitivitiesViewScenarioData;
import com.mxi.driver.standard.Wait;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class SystemSensitivitiesStepDefinitions {

   @Inject
   private ToDoListPageDriver iToDoListPageDriver;

   @Inject
   private AssemblyDetailsPageDriver iAssemblyDetailsPageDriver;

   @Inject
   private ConfigSlotDetailsPageDriver iConfigSlotDetailsPageDriver;

   @Inject
   private CreateEditConfigSlotPageDriver iCreateEditConfigSlotPageDriver;

   @Inject
   private SensitivityQueriesDriver iSensitivityQueriesDriver;


   @When( "^I view a configuration slot's details with active system sensitivities$" )
   public void iViewAConfigurationSlotSDetails() {
      iToDoListPageDriver.clickTabAssemblyList()
            .clickAssembly( SystemSensitivitiesViewScenarioData.ASSEMBLY_NAME );
      iAssemblyDetailsPageDriver
            .clickAssemblyLinkFromAssembliesList( SystemSensitivitiesViewScenarioData.CONFIG_SLOT );
   }


   @Then( "^the active system sensitivities are displayed$" )
   public void iCanSeeTheConfiguredSensitivities() {
      // pause to allow Mx to write to the db before querying
      Wait.pause( 500 );
      assertEquals(
            iSensitivityQueriesDriver.getSensitivitiesSdesc(
                  SystemSensitivitiesViewScenarioData.ACTIVE_SENSITIVITIES ),
            iConfigSlotDetailsPageDriver.getSensitivitiesNames() );

      assertEquals( SystemSensitivitiesViewScenarioData.ENABLED_SENSITIVITIES_BOOLEAN.toString(),
            iConfigSlotDetailsPageDriver.getEnabledSensitivities().toString() );
   }


   @When( "^I am creating a configuration slot$" )
   public void iCreateConfiguationSlot() {
      iToDoListPageDriver.clickTabAssemblyList();
      iToDoListPageDriver.getTabAssemblyList()
            .clickAssembly( SystemSensitivitiesCreateScenarioData.ASSEMBLY_NAME );

      iAssemblyDetailsPageDriver
            .clickRadioButton( SystemSensitivitiesCreateScenarioData.SUB_CONFIG_SLOT_XPATH );
      iAssemblyDetailsPageDriver.clickCreateButton();
      iCreateEditConfigSlotPageDriver
            .setName( SystemSensitivitiesCreateScenarioData.CONFIG_SLOT_NAME );
      iCreateEditConfigSlotPageDriver
            .setPositionPerParent( SystemSensitivitiesCreateScenarioData.POSITIONS_PER_PARENT );
      iCreateEditConfigSlotPageDriver
            .setAtaCode( SystemSensitivitiesCreateScenarioData.CONFIG_SLOT_ATA );
      iCreateEditConfigSlotPageDriver
            .selectConfigClass( SystemSensitivitiesCreateScenarioData.CONFIG_SLOT_CLASS );

   }


   @When( "^I enable system sensitivities on the configuration slot$" )
   public void iConfigureSystemSensitivities() {
      iCreateEditConfigSlotPageDriver
            .selectSensitivities( SystemSensitivitiesCreateScenarioData.ENABLED_SENSITIVITIES );
      iCreateEditConfigSlotPageDriver.clickOkButton();
   }


   @Then( "^the configuration slot has the configured system sensitivities$" )
   public void iConfigurationSlotConfiguredSystemSensitivities() {
      // pause to allow Mx to write to the db before querying
      Wait.pause( 500 );
      assertEquals( SystemSensitivitiesCreateScenarioData.ENABLED_SENSITIVITIES,
            iSensitivityQueriesDriver.getSensitivitiesForSystem(
                  SystemSensitivitiesCreateScenarioData.CONFIG_SLOT_NAME,
                  SystemSensitivitiesCreateScenarioData.ASSEMBLY_CD ) );
   }


   @Given( "^I am editing a configuration slot$" )
   public void iConfigurationSlotToEdit() throws Throwable {
      iToDoListPageDriver.clickTabAssemblyList();
      iToDoListPageDriver.getTabAssemblyList()
            .clickAssembly( SystemSensitivitiesEditScenarioData.ASSEMBLY_NAME );
      iAssemblyDetailsPageDriver.clickAssemblyLinkFromAssembliesList(
            SystemSensitivitiesEditScenarioData.CONFIG_SLOT_NAME_1 );
      iAssemblyDetailsPageDriver.clickEditButton();
   }


   @Then( "^I can see the current configured sensitivities for the configuration slot$" )
   public void theConfiguredSensitivitiesPersistInEditMode() throws Throwable {
      List<String> lCurrentEnabledSensitivities =
            iCreateEditConfigSlotPageDriver.getEnabledSensitivities();

      assertEquals( SystemSensitivitiesEditScenarioData.ENABLED_SENSITIVITIES_ID,
            lCurrentEnabledSensitivities );
   }


   @When( "^I modify the sensitivities for the configuration slot$" )
   public void iEditASensitivityInAConfigurationSlot() throws Throwable {
      iCreateEditConfigSlotPageDriver
            .selectSensitivities( SystemSensitivitiesEditScenarioData.DISABLED_SENSITIVITIES );
      iCreateEditConfigSlotPageDriver.clickPropagateCheckbox();
      iCreateEditConfigSlotPageDriver.clickOkButton();
   }


   @Then( "^the configuration slot is updated with the new sensitivity settings$" )
   public void theConfigurationSlotsWithSensitivitiesAreUpdatedInTheSystem() throws Throwable {
      // pause to allow Mx to write to the db before querying
      Wait.pause( 500 );
      assertEquals( SystemSensitivitiesEditScenarioData.EDITED_SENSITIVITY,
            iSensitivityQueriesDriver.getSensitivitiesForSystem(
                  SystemSensitivitiesEditScenarioData.CONFIG_SLOT_1,
                  SystemSensitivitiesEditScenarioData.ASSEMBLY_CD ) );
      assertEquals( SystemSensitivitiesEditScenarioData.EDITED_SENSITIVITY,
            iSensitivityQueriesDriver.getSensitivitiesForSystem(
                  SystemSensitivitiesEditScenarioData.CONFIG_SLOT_2,
                  SystemSensitivitiesEditScenarioData.ASSEMBLY_CD ) );
   }


   @Before( "@SystemSensitivitiesDataForView" )
   public void createSystemSensitivitiesDataForView() {
      iSensitivityQueriesDriver
            .insertSensitivities( SystemSensitivitiesViewScenarioData.ACTIVE_SENSITIVITIES );
      iSensitivityQueriesDriver
            .activateSensitivities( SystemSensitivitiesViewScenarioData.ACTIVE_SENSITIVITIES );
      iSensitivityQueriesDriver.assignAssemblySensitivities(
            SystemSensitivitiesViewScenarioData.ASSEMBLY_DB_ID,
            SystemSensitivitiesViewScenarioData.ASSEMBLY_CD,
            SystemSensitivitiesViewScenarioData.ACTIVE_SENSITIVITIES );
      iSensitivityQueriesDriver.enableSystemSensitivities(
            SystemSensitivitiesViewScenarioData.ASSEMBLY_DB_ID,
            SystemSensitivitiesViewScenarioData.ASSEMBLY_CD,
            SystemSensitivitiesViewScenarioData.ASSEMBLY_BOM_NAME,
            SystemSensitivitiesViewScenarioData.ENABLED_SENSITIVITIES );
   }


   @After( "@SystemSensitivitiesDataForView" )
   public void removeSystemSensitivitiesDataForView() {
      iSensitivityQueriesDriver.disableSystemSensitivities(
            SystemSensitivitiesViewScenarioData.ASSEMBLY_DB_ID,
            SystemSensitivitiesViewScenarioData.ASSEMBLY_CD,
            SystemSensitivitiesViewScenarioData.ASSEMBLY_BOM_NAME,
            SystemSensitivitiesViewScenarioData.ENABLED_SENSITIVITIES );
      iSensitivityQueriesDriver.unassignAssembySensitivities(
            SystemSensitivitiesViewScenarioData.ASSEMBLY_DB_ID,
            SystemSensitivitiesViewScenarioData.ASSEMBLY_CD,
            SystemSensitivitiesViewScenarioData.ACTIVE_SENSITIVITIES );
      iSensitivityQueriesDriver
            .deleteSensitivities( SystemSensitivitiesViewScenarioData.ACTIVE_SENSITIVITIES );
   }


   @Before( "@SystemSensitivitiesDataForCreate" )
   public void createSystemSensitivitiesDataForCreate() {
      iSensitivityQueriesDriver
            .insertSensitivities( SystemSensitivitiesCreateScenarioData.ENABLED_SENSITIVITIES );
      iSensitivityQueriesDriver
            .activateSensitivities( SystemSensitivitiesCreateScenarioData.ENABLED_SENSITIVITIES );
      iSensitivityQueriesDriver.assignAssemblySensitivities(
            SystemSensitivitiesCreateScenarioData.ASSEMBLY_DB_ID,
            SystemSensitivitiesCreateScenarioData.ASSEMBLY_CD,
            SystemSensitivitiesCreateScenarioData.ENABLED_SENSITIVITIES );
   }


   @After( "@SystemSensitivitiesDataForCreate" )
   public void removeSystemSensitivitiesDataForCreate() {
      iSensitivityQueriesDriver.disableSystemSensitivities(
            SystemSensitivitiesCreateScenarioData.ASSEMBLY_DB_ID,
            SystemSensitivitiesCreateScenarioData.ASSEMBLY_CD,
            SystemSensitivitiesCreateScenarioData.CONFIG_SLOT_NAME,
            SystemSensitivitiesCreateScenarioData.ENABLED_SENSITIVITIES );
      iSensitivityQueriesDriver.unassignAssembySensitivities(
            SystemSensitivitiesCreateScenarioData.ASSEMBLY_DB_ID,
            SystemSensitivitiesCreateScenarioData.ASSEMBLY_CD,
            SystemSensitivitiesCreateScenarioData.ENABLED_SENSITIVITIES );
      iSensitivityQueriesDriver
            .deleteSensitivities( SystemSensitivitiesCreateScenarioData.ENABLED_SENSITIVITIES );
   }


   @Before( "@SystemSensitivitiesDataForEdit" )
   public void createSystemSensitivitiesDataForEdit() {
      iSensitivityQueriesDriver
            .insertSensitivities( SystemSensitivitiesEditScenarioData.ENABLED_SENSITIVITIES );
      iSensitivityQueriesDriver
            .activateSensitivities( SystemSensitivitiesEditScenarioData.ENABLED_SENSITIVITIES );
      iSensitivityQueriesDriver.assignAssemblySensitivities(
            SystemSensitivitiesEditScenarioData.ASSEMBLY_DB_ID,
            SystemSensitivitiesEditScenarioData.ASSEMBLY_CD,
            SystemSensitivitiesEditScenarioData.ENABLED_SENSITIVITIES );
      iSensitivityQueriesDriver.enableSystemSensitivities(
            SystemSensitivitiesEditScenarioData.ASSEMBLY_DB_ID,
            SystemSensitivitiesEditScenarioData.ASSEMBLY_CD,
            SystemSensitivitiesEditScenarioData.CONFIG_SLOT_1,
            SystemSensitivitiesEditScenarioData.ENABLED_SENSITIVITIES );
   }


   @After( "@SystemSensitivitiesDataForEdit" )
   public void removeSystemSensitivitiesDataForEdit() {
      iSensitivityQueriesDriver.disableSystemSensitivities(
            SystemSensitivitiesEditScenarioData.ASSEMBLY_DB_ID,
            SystemSensitivitiesEditScenarioData.ASSEMBLY_CD,
            SystemSensitivitiesEditScenarioData.CONFIG_SLOT_1,
            SystemSensitivitiesEditScenarioData.ENABLED_SENSITIVITIES );
      iSensitivityQueriesDriver.disableSystemSensitivities(
            SystemSensitivitiesEditScenarioData.ASSEMBLY_DB_ID,
            SystemSensitivitiesEditScenarioData.ASSEMBLY_CD,
            SystemSensitivitiesEditScenarioData.CONFIG_SLOT_2,
            SystemSensitivitiesEditScenarioData.ENABLED_SENSITIVITIES );
      iSensitivityQueriesDriver.unassignAssembySensitivities(
            SystemSensitivitiesEditScenarioData.ASSEMBLY_DB_ID,
            SystemSensitivitiesEditScenarioData.ASSEMBLY_CD,
            SystemSensitivitiesEditScenarioData.ENABLED_SENSITIVITIES );
      iSensitivityQueriesDriver
            .deleteSensitivities( SystemSensitivitiesEditScenarioData.ENABLED_SENSITIVITIES );
   }
}
