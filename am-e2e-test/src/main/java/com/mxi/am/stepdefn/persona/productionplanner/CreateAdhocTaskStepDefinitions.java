package com.mxi.am.stepdefn.persona.productionplanner;

import static org.junit.Assert.assertTrue;

import javax.inject.Inject;

import com.mxi.am.driver.query.WorkPackageQueriesDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.inventory.InventorySelectionPageDriver;
import com.mxi.am.driver.web.task.TaskSelectionPageDriver;
import com.mxi.am.driver.web.task.CheckDetails.CheckDetailsPageDriver;
import com.mxi.am.driver.web.task.TaskDetails.TaskDetailsPageDriver;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class CreateAdhocTaskStepDefinitions {

   @Inject
   private NavigationDriver navigationDriver;

   @Inject
   private CheckDetailsPageDriver checkDetailsPageDriver;

   @Inject
   private InventorySelectionPageDriver inventorySelectionPageDriver;

   @Inject
   private TaskSelectionPageDriver taskSelectionPageDriver;

   @Inject
   private TaskDetailsPageDriver taskDetailsPageDriver;

   @Inject
   private WorkPackageQueriesDriver workPackageQueriesDriver;

   private static final String WORK_PACKAGE = "WP25202-01";
   private static final String FAILED_SYSTEM = "SYS-1 - Aircraft System 1";
   private static final String TASK_NAME = "T25202-1";


   @Given( "^I have a work package$" )
   public void iHaveAWorkPackage() throws Throwable {
      navigationDriver.barcodeSearch(
            workPackageQueriesDriver.getByWorkPackageName( WORK_PACKAGE ).getBarcode() );
   }


   @When( "^I create an Adhoc Task in the work package$" )
   public void iCreateAnAdhocTaskInTheWorkPackage() throws Throwable {
      checkDetailsPageDriver.clickCreateNewTask();
      inventorySelectionPageDriver.selectInventoryFromTree( FAILED_SYSTEM );
      taskSelectionPageDriver.clickCreateAdHoc();
      taskSelectionPageDriver.setTaskName( TASK_NAME );
      taskSelectionPageDriver.clickOkForCreateTask();
   }


   @Then( "^I can see the Adhoc Task in the work package$" )
   public void iCanSeeTheAdhocTaskInTheWorkPackage() throws Throwable {
      String taskId = taskDetailsPageDriver.getBarcode();
      taskDetailsPageDriver.clickWorkPackageInformation();
      assertTrue( taskId + " is not in the Assigned Tasks!",
            checkDetailsPageDriver.getAssignedTasksTab().hasAdhocTask( taskId ) );;
   }
}
