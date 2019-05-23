package com.mxi.am.stepdefn.persona.engineer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.inject.Inject;

import com.mxi.am.driver.common.configurationParameters.ConfigurationParameterDriver;
import com.mxi.am.driver.common.configurationParameters.ConfigurationParameterWorkflow;
import com.mxi.am.driver.web.LoginPageDriver;
import com.mxi.am.driver.web.LogoutPageDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.inventory.InventorySelectionPageDriver;
import com.mxi.am.driver.web.task.CreateTaskFromDefinitionPageDriver;
import com.mxi.am.driver.web.task.TaskSelectionPageDriver;
import com.mxi.am.driver.web.task.TaskDetails.TaskDetailsPageDriver;
import com.mxi.am.driver.web.task.correctiveactions.CorrectiveActionsPageDriver;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class CreateSubTaskOfFaultStepDefinitions {

   @Inject
   private LogoutPageDriver iLogoutPageDriver;

   @Inject
   private LoginPageDriver iLoginPageDriver;

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private TaskDetailsPageDriver iTaskDetailsPageDriver;

   @Inject
   private InventorySelectionPageDriver iInventorySelectionPageDriver;

   @Inject
   private TaskSelectionPageDriver iTaskSelectionPageDriver;

   @Inject
   private CreateTaskFromDefinitionPageDriver iCreateTaskFromDefinitionPageDriver;

   @Inject
   private ConfigurationParameterDriver iConfigurationParameterDriver;

   @Inject
   private ConfigurationParameterWorkflow iConfigurationParameterWorkflow;

   @Inject
   private CorrectiveActionsPageDriver iCorrectiveActionsPageDriver;

   private String iOriginalAllowReqCorrectiveActionParm = "false";

   private static final String ENGINEER_USERNAME = "engineer";
   private static final String SUPERUSER_USERNAME = "mxi";
   private static final String PASSWORD = "password";
   private static final String ALLOW_REQ_CORRECTIVE_ACTIONS = "ALLOW_REQ_CORRECTIVE_ACTIONS";
   private static final String TASK_BARCODE = "OPER18603BARCODE";
   private static final String INVENTORY_NAME = "Aircraft Part ENG 5 - OPER-18603";
   private static final String TRUE = "true";
   private static final String TASK_NAME_1 = "18603REQCORR (18603REQCORR)";


   @When( "^I attempt to create a sub task under an open fault$" )
   public void iCreateASubTaskOfFault() {
      updateConfigParmToAllowReqCorrectiveActions();

      iNavigationDriver.barcodeSearch( TASK_BARCODE );
      iTaskDetailsPageDriver.clickTabTaskExecution().clickCreateNewSubtask();
      iInventorySelectionPageDriver.selectInventoryFromTree( INVENTORY_NAME );
      assertTrue( iTaskSelectionPageDriver.isCreateBasedOnReqTaskDefinitionRadioButtonVisible() );
      iCreateTaskFromDefinitionPageDriver.clickCancel();
      iTaskDetailsPageDriver.clickCorrectiveActions();
      iCorrectiveActionsPageDriver.clickTabCorrectiveActions().clickCreateSubtask();
      iInventorySelectionPageDriver.selectInventoryFromTree( INVENTORY_NAME );
      iTaskSelectionPageDriver.clickReqTaskDefinitionByName( TASK_NAME_1 );
      iTaskSelectionPageDriver.clickOkForCreateTask();
      revertConfigParmChanges();
   }


   @Then( "^the effective from information is not visible while confirming the selection$" )
   public void effectiveFromIsNotVisible() {
      assertFalse( iCreateTaskFromDefinitionPageDriver.isEffectiveFromBandVisible() );

   }


   private void updateConfigParmToAllowReqCorrectiveActions() {
      iNavigationDriver.clickLogout();
      iLogoutPageDriver.clickOK();
      iLoginPageDriver.login( SUPERUSER_USERNAME, PASSWORD );
      iOriginalAllowReqCorrectiveActionParm = iConfigurationParameterWorkflow
            .temporaryParameterToggleAndRefresh( ALLOW_REQ_CORRECTIVE_ACTIONS, TRUE );
      iNavigationDriver.clickLogout();
      iLogoutPageDriver.clickOK();
      iLoginPageDriver.login( ENGINEER_USERNAME, PASSWORD );
   }


   private void revertConfigParmChanges() {
      iConfigurationParameterDriver.update( ALLOW_REQ_CORRECTIVE_ACTIONS,
            iOriginalAllowReqCorrectiveActionParm );
   }

}
