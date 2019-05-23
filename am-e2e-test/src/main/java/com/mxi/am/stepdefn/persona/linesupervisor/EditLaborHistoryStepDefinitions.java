package com.mxi.am.stepdefn.persona.linesupervisor;

import javax.inject.Inject;

import com.mxi.am.driver.common.ConfirmPageDriver;
import com.mxi.am.driver.query.WorkPackageQueriesDriver;
import com.mxi.am.driver.web.AuthenticationRequiredPageDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.task.ScheduleCheckPageDriver;
import com.mxi.am.driver.web.task.StartCheckPageDriver;
import com.mxi.am.driver.web.task.CheckDetails.CheckDetailsPageDriver;
import com.mxi.am.driver.web.task.CheckDetails.EditLaborHistoryPageDriver;
import com.mxi.am.driver.web.task.CheckDetails.CheckDetailsPanes.LaborPaneDriver;
import com.mxi.am.driver.web.task.TaskDetails.TaskDetailsPageDriver;
import com.mxi.am.driver.web.task.TaskDetails.TaskDetailsPanes.TaskExecutionPaneDriver.Labor;
import com.mxi.am.driver.web.task.labor.EditWorkCapturePageDriver;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class EditLaborHistoryStepDefinitions {

   @Inject
   private TaskDetailsPageDriver iTaskDetailsPageDriver;

   @Inject
   private AuthenticationRequiredPageDriver iAuthenticationRequiredPageDriver;

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private ScheduleCheckPageDriver iScheduleCheckPageDriver;

   @Inject
   private StartCheckPageDriver iStartCheckPageDriver;

   @Inject
   private CheckDetailsPageDriver iCheckDetailsPageDriver;

   @Inject
   private EditWorkCapturePageDriver iEditWorkCapturePageDriver;

   @Inject
   private EditLaborHistoryPageDriver iEditLaborHistoryPageDriver;

   @Inject
   private ConfirmPageDriver iConfirmPageDriver;

   private static final String TASK_NAME = "3849_TSK1 (3849_TSK1)";

   private static final String SCHEDULED_LOCATION = "AIRPORT1/LINE";

   private static final String WORK_PACKAGE = "OPER-3849";

   private static final String ACTUAL_HOURS = "6:21";

   private static final String PASSWORD = "password";

   private static String iWorkPackageBarcode;


   @Inject
   public void
         EditLaborHistoryStepDefinitons( WorkPackageQueriesDriver aWorkPackageQueriesDriver ) {
      iWorkPackageBarcode =
            aWorkPackageQueriesDriver.getByWorkPackageName( WORK_PACKAGE ).getBarcode();
   }


   @And( "^I attempt to schedule and start the open work package$" )
   public void iAttempToScheduleAndStartTheWorkPackage() {

      // aircraft, work package and task is setup via data loader tools
      // we will store bar codes of these objects in class variables

      iNavigationDriver.barcodeSearch( iWorkPackageBarcode );
      iCheckDetailsPageDriver.clickScheduleWorkPackage();
      iScheduleCheckPageDriver.setScheduledLocation( SCHEDULED_LOCATION );
      iScheduleCheckPageDriver.clickOK();
      iConfirmPageDriver.clickYes();
      iCheckDetailsPageDriver.clickStartWorkPackage();
      iStartCheckPageDriver.clickOkButton();

   }


   @And( "^I finish the labor of the task$" )
   public void iFinishThelaborOfTheTask() {
      iNavigationDriver.barcodeSearch( iWorkPackageBarcode );
      iCheckDetailsPageDriver.clickTabAssignedTasks().clickTaskInAssignedTasksTable( TASK_NAME );
      Labor lLaborResultRow =
            iTaskDetailsPageDriver.clickTabTaskExecution().getLaborRows().get( 0 );
      lLaborResultRow.clickFinish();
      iAuthenticationRequiredPageDriver.setPasswordAndClickOK( PASSWORD );
      iEditWorkCapturePageDriver.clickOk();

   }


   @When( "^I try to edit the labor history of the work package$" )
   public void iTryToEditTheLaborHistoryOfTheWorkPackage() {

      iNavigationDriver.barcodeSearch( iWorkPackageBarcode );
      iCheckDetailsPageDriver.clickTabLabor().clickOptions().clickIncludeHistoricTasks();

   }


   @Then( "^The labor history is successfully edited$" )
   public void theLaborHistoryIsSuccessfullyEdited() {

      LaborPaneDriver lLaborPaneDriver = iCheckDetailsPageDriver.clickTabLabor();
      lLaborPaneDriver.getResults().get( 0 ).clickCheckBox();
      lLaborPaneDriver.clickEditLaborHistory();
      iEditLaborHistoryPageDriver.setActualHrs( ACTUAL_HOURS );
      iEditLaborHistoryPageDriver.clickOkButton();

   }
}
