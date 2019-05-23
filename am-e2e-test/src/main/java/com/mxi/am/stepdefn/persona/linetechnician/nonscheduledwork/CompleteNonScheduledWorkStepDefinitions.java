package com.mxi.am.stepdefn.persona.linetechnician.nonscheduledwork;

import javax.inject.Inject;

import com.mxi.am.driver.query.SchedTaskQueriesDriver;
import com.mxi.am.driver.query.WorkPackageKey;
import com.mxi.am.driver.query.WorkPackageQueriesDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.po.RequestAuthorizationPageDriver;
import com.mxi.am.driver.web.task.CompleteTaskPageDriver;
import com.mxi.am.driver.web.task.CheckDetails.CheckDetailsPageDriver;
import com.mxi.am.driver.web.task.TaskDetails.TaskDetailsPageDriver;
import com.mxi.am.stepdefn.persona.linetechnician.nonscheduledwork.data.CompleteNonScheduledWorkScenarioData;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.junit.Assert.assertTrue;


/**
 * Step definitions for assigning temporary roles.
 */
public class CompleteNonScheduledWorkStepDefinitions {

   @Inject
   private WorkPackageQueriesDriver iWorkPackageQueriesDriver;

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private CheckDetailsPageDriver iCheckDetailsPageDriver;

   @Inject
   private SchedTaskQueriesDriver iSchedTaskQueriesDriver;

   @Inject
   private TaskDetailsPageDriver iTaskDetailsPageDriver;

   @Inject
   private CompleteTaskPageDriver iCompleteTaskPageDriver;

   @Inject
   private RequestAuthorizationPageDriver iRequestAuthorizationPageDriver;

   private static final String LINETECH_PASSWORD = "password";


   @Given( "^I want to complete non-scheduled work$" )
   public void iWantToCompleteNonScheduledWork() throws Throwable {

      // Set up tasks in work package
      addTasksToWorkPackage();

      // Use barcode search to go to the work package
      WorkPackageKey lWorkPackage = iWorkPackageQueriesDriver
            .getByWorkPackageName( CompleteNonScheduledWorkScenarioData.WORK_PACKAGE_NAME );
      iNavigationDriver.barcodeSearch( lWorkPackage.getBarcode() );

   }


   @When( "^I complete the last open task in the work package$" )
   public void iCompleteTheLastOpenTaskInTheWorkPackage() throws Throwable {
      iCheckDetailsPageDriver.clickTabWorkscope()
            .clickTaskInWorkscopeTable( CompleteNonScheduledWorkScenarioData.TASK_NAME );

      iTaskDetailsPageDriver.clickCompleteTask();

      // Accept the batch completion date
      iCompleteTaskPageDriver.clickOkButton();

      // Authorize the action
      iRequestAuthorizationPageDriver.setPassword( LINETECH_PASSWORD );
      iRequestAuthorizationPageDriver.authenticate();

      iTaskDetailsPageDriver.clickClose();

   }


   @Then( "^the work package is ready to be closed$" )
   public void workPackageIsReadyToBeClosed() throws Throwable {
      boolean isReadyToCloseBannerVisible = iCheckDetailsPageDriver.isReadyToCloseBannerVisible();
      assertTrue( isReadyToCloseBannerVisible );

   }


   private void addTasksToWorkPackage() {
      iSchedTaskQueriesDriver.createTaskInWorkPackage(
            CompleteNonScheduledWorkScenarioData.AIRCRAFT_NAME,
            CompleteNonScheduledWorkScenarioData.TASK_NAME,
            CompleteNonScheduledWorkScenarioData.WORK_PACKAGE_NAME );

   }

}
