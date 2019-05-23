package com.mxi.am.stepdefn.persona.workscopeplanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.inject.Inject;

import com.mxi.am.driver.common.ConfirmPageDriver;
import com.mxi.am.driver.web.AuthenticationRequiredPageDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.PleaseWaitPaneDriver;
import com.mxi.am.driver.web.common.SubmitReasonAndNotesPageDriver;
import com.mxi.am.driver.web.inventory.inventorydetailspage.InventoryDetailsPageDriver;
import com.mxi.am.driver.web.inventory.inventorydetailspage.inventorydetailspanes.openpanes.OpenWorkPackagesPaneDriver.OpenWorkPackagesTableRow;
import com.mxi.am.driver.web.task.ScheduleCheckPageDriver;
import com.mxi.am.driver.web.task.CheckDetails.CheckDetailsPageDriver;
import com.mxi.am.driver.web.task.CheckDetails.CheckDetailsPanes.AssignedTasksPaneDriver.AssignedTaskRow;
import com.mxi.am.driver.web.task.CheckDetails.CheckDetailsPanes.UnAssignedFaultsPaneDriver.UnAssignedFaultResult;
import com.mxi.am.driver.web.task.CheckDetails.CheckDetailsPanes.UnAssignedTasksPaneDriver.UnAssignedTaskResult;
import com.mxi.am.driver.web.task.CheckDetails.CheckDetailsPanes.WorkscopePaneDriver.WorkScopeTableRow;
import com.mxi.am.driver.web.todolist.ToDoListPageDriver;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class PerformWorkscopePlanningStepDefinitions {

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private ToDoListPageDriver iToDoListPageDriver;

   @Inject
   private InventoryDetailsPageDriver iInventoryDetailsPageDriver;

   @Inject
   private CheckDetailsPageDriver iCheckDetailsPageDriver;

   @Inject
   private ScheduleCheckPageDriver iScheduleCheckPageDriver;

   @Inject
   private ConfirmPageDriver iConfirmPageDriver;

   @Inject
   private SubmitReasonAndNotesPageDriver iSubmitReasonAndNotesPageDriver;

   @Inject
   private AuthenticationRequiredPageDriver iAuthenticationRequiredPageDriver;

   @Inject
   private PleaseWaitPaneDriver iPleaseWaitPaneDriver;

   private static final int ONE_DAY = 1;
   private static final int MAX_WAIT_TIME_IN_MS = 5 * 60 * 1000;
   private static final String HEAVY_PLANNER = "Heavy Planner";
   private static final String HEAVY_PLANNER_TO_DO_LIST = "To Do List (Heavy Planner)";
   private static final String TASK_WITH_EXPECTED_SCOPE = "SCH_WORK_TSK1 (SCH_WORK_TSK1)";
   private static final String DATE_FORMAT_PATTERN = "dd-MMM-yyyy";
   private static final String SCHEDULED_START_TIME = "08:00";
   private static final String SCHEDULED_END_TIME = "08:00";
   private static final String TIME_ZONE = "EST";
   private static final String COMMIT_STATUS = "COMMIT";
   private static final String PASSWORD = "password";

   private static List<OpenWorkPackagesTableRow> iOpenWorkPackagesTableRows;
   private static List<WorkScopeTableRow> iWorkScopeTableRows;
   private static List<UnAssignedTaskResult> iUnAssignedTaskResults;
   private static List<UnAssignedFaultResult> iUnAssignedFaultResults;
   private static List<AssignedTaskRow> iAssignedTaskRows;
   private static String iScheduledStartDate;
   private static String iScheduledEndDate;


   @Given( "^I have an Aircraft \"([^\"]*)\"$" )
   public void iHaveAnAircraft( String aAircraft ) throws Throwable {
      iNavigationDriver.navigate( HEAVY_PLANNER, HEAVY_PLANNER_TO_DO_LIST );

      iToDoListPageDriver.clickTabFleetList().clickAircraftInTable( aAircraft );
      iInventoryDetailsPageDriver.clickTabOpen().clickTabOpenWorkPackages();
      iOpenWorkPackagesTableRows = iInventoryDetailsPageDriver.getTabOpen().getTabOpenWorkPackages()
            .getOpenWorkPackagesTableRows();
   }


   @Given( "^the Aircraft has an Open Work Package \"([^\"]*)\"$" )
   public void theAricraftHasAnOpenWorkPackage( String aWorkPackage ) throws Throwable {
      boolean lFound = false;
      for ( OpenWorkPackagesTableRow lRow : iOpenWorkPackagesTableRows ) {
         if ( lRow.getName().equalsIgnoreCase( aWorkPackage ) ) {
            lFound = true;
            lRow.clickName();
            break;
         }
      }
      assertTrue( "Open work package (" + aWorkPackage + ") is not found!", lFound );
   }


   @When( "^I review the Workscope$" )
   public void iReviewTheWorkscope() throws Throwable {
      iCheckDetailsPageDriver.clickGenerateWorkscope();
   }


   @Then( "^the Workscope exists$" )
   public void theWorkscopeExists() throws Throwable {
      iWorkScopeTableRows = iCheckDetailsPageDriver.getTabWorkscope().getWorkScopeTableRows();

      boolean lFound = false;
      for ( WorkScopeTableRow lRow : iWorkScopeTableRows ) {
         if ( lRow.getName().equalsIgnoreCase( TASK_WITH_EXPECTED_SCOPE ) ) {
            lFound = true;
            break;
         }
      }

      String lMsg = "Task (" + TASK_WITH_EXPECTED_SCOPE
            + ") on the Work Package align with the expected scope of work required is not found!";
      assertTrue( lMsg, lFound );
   }


   @Given( "^the Aircraft has Unassigned Faults$" )
   public void theAircraftHasUnassignedFaults() throws Throwable {
      iCheckDetailsPageDriver.clickTabUnAssignedFaults();
   }


   @When( "^I review the Unassigned Faults$" )
   public void iReviewTheUnassignedFaults() throws Throwable {
      iUnAssignedFaultResults = iCheckDetailsPageDriver.getTabUnAssignedFaults().getResults();
   }


   @Then( "^the Faults \"([^\"]*)\" exist$" )
   public void theFaultsExist( String aFault ) throws Throwable {
      verifyUnAssignedFaultExists( aFault, iUnAssignedFaultResults );
   }


   @Given( "^the Aircraft has Unassigned Tasks$" )
   public void theAircraftHasUnassignedTasks() throws Throwable {
      iCheckDetailsPageDriver.clickTabUnAssignedTasks();
   }


   @When( "^I review the Unassigned Tasks$" )
   public void iReviewTheUnassignedTasks() throws Throwable {
      iUnAssignedTaskResults = iCheckDetailsPageDriver.getTabUnAssignedTasks().getResults();
   }


   @Then( "^the Tasks \"([^\"]*)\" exist$" )
   public void theTasksExist( String aTask ) throws Throwable {
      verifyUnAssignedTaskExists( aTask, iUnAssignedTaskResults );
   }


   @Then( "^I Assign Unassigned Faults \"([^\"]*)\" to the Work Package$" )
   public void iAssignUnassignedFaultsToTheWorkPackage( String aFault ) throws Throwable {
      selectUnAssignedFault( aFault, iUnAssignedFaultResults );
      iCheckDetailsPageDriver.getTabUnAssignedFaults().clickAssignFaultButton();
   }


   @Then( "^I Assign Unassigned Tasks \"([^\"]*)\" to the Work Package$" )
   public void iAssignUnassignedTasksToTheWorkPackage( String aTask ) throws Throwable {
      selectUnAssignedTask( aTask, iUnAssignedTaskResults );
      iCheckDetailsPageDriver.getTabUnAssignedTasks().clickAssignTaskButton();
   }


   @Then( "^the Unassigned Faults \"([^\"]*)\" are Assigned to the Work Package$" )
   public void theUnassignedFaultsAreAssignedToTheWorkPackage( String aFault ) throws Throwable {
      iCheckDetailsPageDriver.clickTabAssignedTasks();
      iAssignedTaskRows = iCheckDetailsPageDriver.getAssignedTasksTab().getAssignedTasks();
      verifyAssignedTaskExists( aFault, iAssignedTaskRows );
   }


   @Then( "^the Unassigned Tasks \"([^\"]*)\" are Assigned to the Work Package$" )
   public void theUnassignedTasksAreAssignedToTheWorkPackage( String aTask ) throws Throwable {
      iCheckDetailsPageDriver.clickTabAssignedTasks();
      iAssignedTaskRows = iCheckDetailsPageDriver.getAssignedTasksTab().getAssignedTasks();
      verifyAssignedTaskExists( aTask, iAssignedTaskRows );
   }


   @When( "^I Schedule the Work Package$" )
   public void iScheduleTheWorkPackage() throws Throwable {
      DateTimeFormatter lFormatter = DateTimeFormatter.ofPattern( DATE_FORMAT_PATTERN );
      LocalDate lTomorrow = LocalDate.now().plusDays( ONE_DAY );
      iScheduledStartDate = lTomorrow.format( lFormatter ).toUpperCase();
      iScheduledEndDate = lTomorrow.plusDays( ONE_DAY ).format( lFormatter ).toUpperCase();

      iCheckDetailsPageDriver.clickScheduleWorkPackage();
      iScheduleCheckPageDriver.setScheduledStartDate( iScheduledStartDate );
      iScheduleCheckPageDriver.setScheduledStartTime( SCHEDULED_START_TIME );
      iScheduleCheckPageDriver.setScheduledEndDate( iScheduledEndDate );
      iScheduleCheckPageDriver.setScheduledEndTime( SCHEDULED_END_TIME );
      iScheduleCheckPageDriver.clickOK();
      iConfirmPageDriver.clickYes();
      iPleaseWaitPaneDriver.waitForPleaseWaitToClose( MAX_WAIT_TIME_IN_MS );
   }


   @Then( "^the Work Package is Scheduled$" )
   public void theWorkPackageIsScheduled() throws Throwable {
      iCheckDetailsPageDriver.clickTabDetails();

      String lScheduledStartDateTime =
            iScheduledStartDate + " " + SCHEDULED_START_TIME + " " + TIME_ZONE;
      String lScheduledEndDateTime = iScheduledEndDate + " " + SCHEDULED_END_TIME + " " + TIME_ZONE;

      assertEquals( lScheduledStartDateTime,
            iCheckDetailsPageDriver.getTabDetails().getScheduledStart() );
      assertEquals( lScheduledEndDateTime,
            iCheckDetailsPageDriver.getTabDetails().getScheduledEnd() );
   }


   @Given( "^the Work Package has Tasks \"([^\"]*)\" Assigned$" )
   public void theWorkPackageHasTasksAssigned( String aTask ) throws Throwable {
      iCheckDetailsPageDriver.clickTabAssignedTasks();
      iAssignedTaskRows = iCheckDetailsPageDriver.getAssignedTasksTab().getAssignedTasks();
      verifyAssignedTaskExists( aTask, iAssignedTaskRows );
   }


   @Given( "^the Work Package has Faults \"([^\"]*)\" Assigned$" )
   public void theWorkPackageHasFaultsAssigned( String aFault ) throws Throwable {
      iCheckDetailsPageDriver.clickTabAssignedTasks();
      iAssignedTaskRows = iCheckDetailsPageDriver.getAssignedTasksTab().getAssignedTasks();
      verifyAssignedTaskExists( aFault, iAssignedTaskRows );
   }


   @When( "^I Commit the Workscope$" )
   public void iCommitTheWorkscope() throws Throwable {
      iCheckDetailsPageDriver.clickCommitScopeButton();
      iSubmitReasonAndNotesPageDriver.clickOk();
      iAuthenticationRequiredPageDriver.setPasswordAndClickOK( PASSWORD );
      iPleaseWaitPaneDriver.waitForPleaseWaitToClose( MAX_WAIT_TIME_IN_MS );
   }


   @Then( "^the Work Package is Committed$" )
   public void theWorkPackageIsCommitted() throws Throwable {
      assertEquals( COMMIT_STATUS, iCheckDetailsPageDriver.clickTabDetails().getStatus() );
   }


   private void verifyUnAssignedTaskExists( String aTask,
         List<UnAssignedTaskResult> aUnAssignedTasksTable ) {
      boolean lFound = false;
      for ( UnAssignedTaskResult lUnAssignTask : aUnAssignedTasksTable ) {
         if ( lUnAssignTask.getTaskName().equalsIgnoreCase( aTask ) ) {
            lFound = true;
            break;
         }
      }
      String lMsg = "Unassigned Task (" + aTask + ") is not found!";
      assertTrue( lMsg, lFound );
   }


   private void selectUnAssignedTask( String aTask,
         List<UnAssignedTaskResult> aUnAssignedTasksTable ) {
      boolean lFound = false;
      for ( UnAssignedTaskResult lUnAssignTask : aUnAssignedTasksTable ) {
         if ( lUnAssignTask.getTaskName().equalsIgnoreCase( aTask ) ) {
            lFound = true;
            lUnAssignTask.clickCheckBox();
            break;
         }
      }
      String lMsg = "Unassigned Task (" + aTask + ") is not found!";
      assertTrue( lMsg, lFound );
   }


   private void verifyUnAssignedFaultExists( String aFault,
         List<UnAssignedFaultResult> aUnAssignedFaultsTable ) {
      boolean lFound = false;
      for ( UnAssignedFaultResult lUnAssignFault : aUnAssignedFaultsTable ) {
         if ( lUnAssignFault.getFaultName().equalsIgnoreCase( aFault ) ) {
            lFound = true;
            break;
         }
      }
      String lMsg = "Unassigned Fault (" + aFault + ") is not found!";
      assertTrue( lMsg, lFound );
   }


   private void selectUnAssignedFault( String aFault,
         List<UnAssignedFaultResult> aUnAssignedFaultsTable ) {
      boolean lFound = false;
      for ( UnAssignedFaultResult lUnAssignFault : aUnAssignedFaultsTable ) {
         if ( lUnAssignFault.getFaultName().equalsIgnoreCase( aFault ) ) {
            lFound = true;
            lUnAssignFault.clickCheckBox();
            break;
         }
      }
      String lMsg = "Unassigned Fault (" + aFault + ") is not found!";
      assertTrue( lMsg, lFound );
   }


   private void verifyAssignedTaskExists( String aTask,
         List<AssignedTaskRow> aAssignedTasksTable ) {
      boolean lFound = false;
      for ( AssignedTaskRow lAssignTaskRow : aAssignedTasksTable ) {
         if ( lAssignTaskRow.getTaskName().equalsIgnoreCase( aTask ) ) {
            lFound = true;
            break;
         }
      }
      String lMsg = "Assigned Task (" + aTask + ") is not found!";
      assertTrue( lMsg, lFound );
   }
}
