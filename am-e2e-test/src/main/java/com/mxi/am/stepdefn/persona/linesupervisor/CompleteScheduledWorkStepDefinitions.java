package com.mxi.am.stepdefn.persona.linesupervisor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import com.mxi.am.driver.query.InventoryQueriesDriver;
import com.mxi.am.driver.query.SchedTaskQueriesDriver;
import com.mxi.am.driver.query.WorkPackageQueriesDriver;
import com.mxi.am.driver.web.AuthenticationRequiredPageDriver;
import com.mxi.am.driver.web.LoginPageDriver;
import com.mxi.am.driver.web.LogoutPageDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.inventory.ReleaseInventoryPageDriver;
import com.mxi.am.driver.web.inventory.inventorydetailspage.InventoryDetailsPageDriver;
import com.mxi.am.driver.web.task.CompleteTaskPageDriver;
import com.mxi.am.driver.web.task.PreviewReleasePageDriver;
import com.mxi.am.driver.web.task.CheckDetails.CheckDetailsPageDriver;
import com.mxi.am.driver.web.task.TaskDetails.TaskDetailsPageDriver;
import com.mxi.am.driver.web.todolist.ToDoListPageDriver;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


/**
 * Step Definitions for navigating Maintenix
 */
public class CompleteScheduledWorkStepDefinitions {

   @Inject
   private InventoryDetailsPageDriver iInventoryDetailsPageDriver;

   @Inject
   private ToDoListPageDriver iToDoListPageDriver;

   @Inject
   private CheckDetailsPageDriver iWorkPackageDetailsDriver;

   @Inject
   private TaskDetailsPageDriver iTaskDetailsPageDriver;

   @Inject
   private CompleteTaskPageDriver iCompleteTaskPageDriver;

   @Inject
   private AuthenticationRequiredPageDriver iAuthenticationRequiredPageDriver;

   @Inject
   private PreviewReleasePageDriver iPreviewReleasePageDriver;

   @Inject
   private ReleaseInventoryPageDriver iReleaseInventoryPageDriver;

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private LogoutPageDriver iLogoutPageDriver;

   @Inject
   private LoginPageDriver iLoginPageDriver;

   @Inject
   private WorkPackageQueriesDriver iWorkPackageQueriesDriver;

   @Inject
   private InventoryQueriesDriver iInventoryQueriesDriver;

   @Inject
   private SchedTaskQueriesDriver iSchedTaskQueriesDriver;

   private static final String ACFT_REG_CD_1 = "ATD-1";
   private static final String ACFT_SER_NO = "ATD-LMOC1";
   private static final String ACFT_NAME_2 = "Aircraft Part MOC 1 - ATD-2";
   private static final String WORK_PACKAGE = "ATD-Test Work Package";
   private static final String WORK_PACKAGE_2 = "Prevent Release Work Package";
   private static final String TASK_NAME = "SCH_WORK_TSK1 (SCH_WORK_TSK1)";
   private static final String TASK_CD = "SCH_WORK_TSK1";
   private static final String PASSWORD = "password";
   private static final String CONDITION_IN_SERVICE = "INSRV (In Service)";
   private static final String TASK_STATUS_COMPLETE = "COMPLETE (Complete)";
   private static final String WP_STATUS_COMPLETE = "COMPLETE";
   private static final String LINE_TECHNICIAN_USERNAME = "linetech";
   private static final String LINE_TECHNICIAN_PASSWORD = "password";
   private static final String LINE_SUPERVISOR_USERNAME = "linesupervisor";
   private static final String LINE_SUPERVISOR_PASSWORD = "password";
   private static final String INSP_SKILL = "INSP";
   private static final String AET_SKILL = "AET";
   private static final String OVERDUE_TASK_ERROR = "1 overdue task(s).";
   private static final boolean IS_RELEASE_TO_SERVICE = true;
   private static String sAircraftBarcode;
   private static String sTaskBarcode;
   private static String sWorkPackageBarcode;


   @Given( "^I have scheduled work for a line technician$" )
   public void iHaveScheduledWorkToDoOnAnAircraft() {

      // aircraft, work package and task is setup via data loader tools
      // we will store barcodes of these objects in class variables

      sAircraftBarcode =
            iInventoryQueriesDriver.getByAircraftRegistrationCode( ACFT_REG_CD_1 ).getBarcode();
      sWorkPackageBarcode =
            iWorkPackageQueriesDriver.getByWorkPackageName( WORK_PACKAGE ).getBarcode();
      sTaskBarcode = iSchedTaskQueriesDriver.getByAircraftSerNoAndTaskCode( ACFT_SER_NO, TASK_CD )
            .getBarcode();
   }


   @When( "^the line technician completes the scheduled work$" )
   public void iCompleteMyAssignedWorkscopeTasks() {
      // complete the assigned task as line technician

      iNavigationDriver.clickLogout();
      iLogoutPageDriver.clickOK();

      iLoginPageDriver.login( LINE_TECHNICIAN_USERNAME, LINE_TECHNICIAN_PASSWORD );

      iNavigationDriver.barcodeSearch( sTaskBarcode );

      iTaskDetailsPageDriver.clickCompleteTask();
      iCompleteTaskPageDriver.clickOkButton();

      iAuthenticationRequiredPageDriver.setPassword_Type2( PASSWORD );
      iAuthenticationRequiredPageDriver.clickOk();
   }


   @Then( "^the task status is marked as complete$" )
   public void taskStatusIsComplete() {

      String lTaskStatus = iTaskDetailsPageDriver.clickTabTaskInformation().getStatus();
      assertEquals( "Task Status is not COMPLETE", TASK_STATUS_COMPLETE, lTaskStatus );
   }


   @When( "^I complete the work package$" )
   public void iCompleteTheWorkpackage() {
      // complete and sign-off on the workpackage as line supervisor

      iNavigationDriver.clickLogout();
      iLogoutPageDriver.clickOK();

      iLoginPageDriver.login( LINE_SUPERVISOR_USERNAME, LINE_SUPERVISOR_PASSWORD );

      iNavigationDriver.barcodeSearch( sWorkPackageBarcode );

      iWorkPackageDetailsDriver.clickCompleteWorkPackage();
      iPreviewReleasePageDriver.clickNextButton();

      iReleaseInventoryPageDriver.sign( AET_SKILL );
      iAuthenticationRequiredPageDriver.setPassword_Type1( PASSWORD );
      iAuthenticationRequiredPageDriver.clickOk();

      iReleaseInventoryPageDriver.setReleaseToServiceDropdownSelection( IS_RELEASE_TO_SERVICE );
      iReleaseInventoryPageDriver.sign( INSP_SKILL );
      iAuthenticationRequiredPageDriver.setPassword2_Type1( PASSWORD );

      iAuthenticationRequiredPageDriver.clickOk();
      iReleaseInventoryPageDriver.clickCloseButton();
   }


   @Then( "^the work package status is marked as complete$" )
   public void workPackageStatusIsComplete() {

      iNavigationDriver.barcodeSearch( sWorkPackageBarcode );
      String lWorkPackageStatus = iWorkPackageDetailsDriver.clickTabDetails().getStatus();
      assertEquals( "Work Package Status is not COMPLETE", WP_STATUS_COMPLETE, lWorkPackageStatus );
   }


   @Then( "^the aircraft is released back into service$" )
   public void verifyAircraftIsReleasedBackIntoService() {

      iNavigationDriver.barcodeSearch( sAircraftBarcode );
      String lCondition = iInventoryDetailsPageDriver.clickTabDetails().getCondition();
      assertEquals( "Aircraft condition not in service", CONDITION_IN_SERVICE, lCondition );
   }


   @Given( "^an aircraft has a work package and an overdue open task$" )
   public void anAircraftHasAWorkPackageWithAnOverdueOpenTask() {
      // data loading is done via loader tools
      // loaded an aircraft, a work package and an overdue open task
      // strictly speaking, this step definition is not needed;
      // however, leaving it in here for readability
   }


   @When( "^I attempt to complete the work package$" )
   public void iAttemptToCompleteTheWorkPackage() {

      iToDoListPageDriver.clickTabFleetList().clickAircraftInTable( ACFT_NAME_2 );

      iInventoryDetailsPageDriver.clickTabOpen().clickTabOpenWorkPackages()
            .clickWorkPackageName( WORK_PACKAGE_2 );

      iWorkPackageDetailsDriver.clickCompleteWorkPackage();
   }


   @Then( "^I should receive an error message about the overdue task$" )
   public void iShouldReceiveAnErrorMessageAboutTheOverdueTask() {
      assertTrue( "Preview Release page Error Message is Incorrect",
            StringUtils.contains( iPreviewReleasePageDriver.getSummary(), OVERDUE_TASK_ERROR ) );

   }


   @Then( "^I see the overdue task listed in the Tasks table$" )
   public void iShouldSeeTheOverdueTask() {
      assertNotNull( "The overdue task is not listed",
            iPreviewReleasePageDriver.getTaskTableRowWithTaskName( TASK_NAME ) );

   }
}
