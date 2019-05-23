package com.mxi.am.stepdefn.persona.productionplanner;

import static org.junit.Assert.assertTrue;

import javax.inject.Inject;

import com.mxi.am.driver.query.WorkPackageQueriesDriver;
import com.mxi.am.driver.web.AuthenticationRequiredPageDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.inventory.InventorySelectionPageDriver;
import com.mxi.am.driver.web.task.CreateTaskFromDefinitionPageDriver;
import com.mxi.am.driver.web.task.TaskSelectionPageDriver;
import com.mxi.am.driver.web.task.CheckDetails.CheckDetailsPageDriver;
import com.mxi.am.driver.web.task.TaskDetails.TaskDetailsPageDriver;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class InitializeOnConditionTaskStepDefinitions {

   @Inject
   private NavigationDriver navigationDriver;

   @Inject
   private CheckDetailsPageDriver checkDetailsPageDriver;

   @Inject
   private InventorySelectionPageDriver inventorySelectionPageDriver;

   @Inject
   private TaskDetailsPageDriver taskDetailsPageDriver;

   @Inject
   private TaskSelectionPageDriver taskSelectionPageDriver;

   @Inject
   private CreateTaskFromDefinitionPageDriver createTaskFromDefinitionPageDriver;

   @Inject
   private AuthenticationRequiredPageDriver authenticationRequiredPageDriver;

   @Inject
   private WorkPackageQueriesDriver workPackageQueriesDriver;

   private static final String WORK_PACKAGE = "WP25203-01";
   private static final String REQUIREMENT = "BM_SCH_TSK21 (BM_SCH_TSK21)";
   private static final String FAILED_SYSTEM = "SYS-2 - Aircraft System 2";
   private static final String JIC_NAME = "JIC-ELECT (JIC with ELECT Labour Skill)";
   private static final String PASSWORD = "password";


   @Given( "^a work package$" )
   public void aWorkPackage() throws Throwable {
      navigationDriver.barcodeSearch(
            workPackageQueriesDriver.getByWorkPackageName( WORK_PACKAGE ).getBarcode() );
   }


   @When( "^I create an On-condition Task in the work package$" )
   public void iCreateAnOnConditionTaskInTheWorkPackage() throws Throwable {
      checkDetailsPageDriver.clickCreateNewTask();
      inventorySelectionPageDriver.selectInventoryFromTree( FAILED_SYSTEM );
      taskSelectionPageDriver.clickTaskDefinitionByName( REQUIREMENT );
      taskSelectionPageDriver.clickOkForCreateTask();
      createTaskFromDefinitionPageDriver.clickOk();
      authenticationRequiredPageDriver.setPasswordAndClickOK( PASSWORD );
   }


   @Then( "^I can see the On-condition Task in the work package$" )
   public void iCanSeeTheOnConditionTaskInTheWorkPackage() throws Throwable {
      String taskId = taskDetailsPageDriver.getBarcode();
      String subTaskId = taskDetailsPageDriver.clickTabTaskExecution().getSubtaskRowId( JIC_NAME );
      navigationDriver.barcodeSearch(
            workPackageQueriesDriver.getByWorkPackageName( WORK_PACKAGE ).getBarcode() );
      checkDetailsPageDriver.clickTabAssignedTasks();
      assertTrue( "Requirement " + taskId + " is not in the Assigned Tasks!",
            checkDetailsPageDriver.getAssignedTasksTab().hasReqTask( taskId ) );
      assertTrue( "Job Card " + subTaskId + " is not in the Assigned Tasks!",
            checkDetailsPageDriver.getAssignedTasksTab().hasJicTask( subTaskId ) );
   }
}
