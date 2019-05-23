package com.mxi.am.stepdefn.persona.productioncontroller;

import static org.junit.Assert.assertTrue;

import java.io.File;

import javax.inject.Inject;

import com.mxi.am.driver.web.AuthenticationRequiredPageDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.PleaseWaitPaneDriver;
import com.mxi.am.driver.web.ppcspreadsheet.ui.induction.ManageWorkPackagePageDriver;
import com.mxi.am.driver.web.ppcspreadsheet.ui.induction.WorkPackageLoaderPageDriver;
import com.mxi.am.driver.web.task.CancelTasksPageDriver;
import com.mxi.am.driver.web.task.CheckDetails.CheckDetailsPageDriver;
import com.mxi.am.driver.web.task.CheckDetails.CheckDetailsPanes.AssignedTasksPaneDriver;
import com.mxi.am.driver.web.task.TaskDetails.TaskDetailsPageDriver;
import com.mxi.am.driver.web.utl.WebDriverUtls;
import com.mxi.am.helper.TimestampGenerator;
import com.mxi.am.helper.builder.WorkPackageSetupBuilder;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;


@ScenarioScoped
public class AmendWorkscopeStepDefinitions {

   // When TEST_MAINTENANCE_MODE = true, the test can be re-run without a dB rebuild by canceling
   // active tasks assigned to WP's
   private static final boolean TEST_MAINTENANCE_MODE = false; //

   private static final String USERS_PASSWORD = "password";
   private static final int MAX_WAIT_TIME_IN_MS = 5 * 60 * 1000; // 5 minutes

   private static final String WORK_PACKAGE_NAME = "WP_" + TimestampGenerator.generate();
   private static final String AIRCRAFT_REG_CODE = "100";
   private static final String LOCATION = "AIRPORT1/HGR1/TRK1";

   private static final String HEAVY_PLANNER = "Heavy Planner";
   private static final String WORK_PACKAGE_LOADER = "Work Package Loader";

   private static final String FILE_PATH =
         "src/main/resources/com/mxi/am/persona/productioncontroller/spreadsheets/AmendWorkscope/";
   private static final String ADD_ADHOC_SPREADSHEET = "AmendWorkscopeAddAdhocTask.xls";
   private static final String ADD_JOBSTANDARD_SPREADSHEET = "AmendWorkscopeAddJobStandard.xls";
   private static final String CANCEL_ADHOC_SPREADSHEET = "AmendWorkscopeCancelAdhocTask.xls";
   private static final String CANCEL_JOBSTANDARD_SPREADSHEET =
         "AmendWorkscopeCancelJobStandard.xls";
   private static final String EDIT_ADHOC_SPREADSHEET = "AmendWorkscopeEditAdhocTask.xls";
   private static final String EDIT_JOBSTANDARD_SPREADSHEET = "AmendWorkscopeEditJobStandard.xls";

   private static final String ADD_ADHOC_TASK_NAME = "Amend Existing Workscope - Add";
   private static final String INITIAL_JOBSTANDARD_NAME =
         "12-109-00-A (LANDING GEAR CONTROL CABLES)";
   private static final String ADD_JOBSTANDARD_NAME = "27-061-05-A (SPOILER ACTUATOR)";
   private static final String CANCEL_ADHOC_TASK_NAME = "Amend Existing Workscope - Cancel";
   private static final String CANCEL_JOBSTANDARD_NAME = "22-131-01-A (AUTOTHROTTLE LINKAGE)";
   private static final String EDIT_ADHOC_TASK_NAME = "Amend Existing Workscope - Edit";
   private static final String EDIT_JOBSTANDARD_NAME = "25-021-01-A (PASS COMPT DADO VENT BOXES)";
   private static final String UPDATED_ADHOC_SKILL = "PAINT";
   private static final String UPDATED_JOBSTANDARD_SKILL = "PAINT";
   private static final String JOBSTANDARD_SUBTASK_NAME =
         "25-021-01-01-50 (Pass Compt Dado Vent Boxes - Operational Check)";

   @Inject
   private WebDriverUtls iWebDriverUtils;

   @Inject
   private WorkPackageSetupBuilder iWorkPackageSetupBuilder;

   @Inject
   private ManageWorkPackagePageDriver iManageWorkPackagePage;

   @Inject
   private WorkPackageLoaderPageDriver iWorkPackageSetupPage;

   @Inject
   private CheckDetailsPageDriver iCheckDetailsPage;

   @Inject
   private CancelTasksPageDriver iCancelTasksPageDriver;

   @Inject
   private TaskDetailsPageDriver iTaskDetailsPage;

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private PleaseWaitPaneDriver iPleaseWaitPaneDriver;

   @Inject
   private AuthenticationRequiredPageDriver iRequestAuthorizationPageDriver;


   @Given( "^I have an active work package created with \"([^\"]*)\"$" )
   public void iHaveAnActiveWorkPackageCreatedWith( String aFileName ) throws Throwable {

      // Create a work package setup
      iWorkPackageSetupBuilder.withName( WORK_PACKAGE_NAME )
            .withAircraftRegCode( AIRCRAFT_REG_CODE ).withLocation( LOCATION ).build();

      // navigate to the Work Package Loader
      iNavigationDriver.navigate( HEAVY_PLANNER, WORK_PACKAGE_LOADER );

      // navigate to the work package setup's manage page
      iWorkPackageSetupPage.clickManageForWorkPackage( WORK_PACKAGE_NAME );

      // upload an initial workscope
      String lFilePathString = FILE_PATH + aFileName;
      File lFile = new File( lFilePathString );
      iManageWorkPackagePage.uploadWorkPackageWorkflow( lFile.getAbsolutePath() );

      // make sure the initial upload was successful, otherwise it will not be clear why the test
      // failed down stream
      assertTrue( iManageWorkPackagePage.showsSuccessfulUploadNotification() );
   }


   @When( "^I upload the amended workscope spreadsheet with an additional adhoc task" )
   public void iUploadTheTheAmendedWorkscopeSpreadsheetWithAnAdditionalAdhocTask()
         throws Throwable {

      // upload the spreadsheet with the new task
      String lFilePathString = FILE_PATH + ADD_ADHOC_SPREADSHEET;
      File lFile = new File( lFilePathString );
      iManageWorkPackagePage.uploadWorkPackageWorkflow( lFile.getAbsolutePath() );
      iManageWorkPackagePage.clickCloseButtonOnUploadNotificationModal();
   }


   @Then( "^the new adhoc task exists in the work package" )
   public void theNewAdhocTaskExistsInTheWorkPackage() throws Throwable {

      iManageWorkPackagePage.clickCloseButton();
      iWorkPackageSetupPage.clickWorkPackageName( WORK_PACKAGE_NAME );
      iWebDriverUtils.switchWindowTo( "Work Package Details" );
      assertTrue( iCheckDetailsPage.containsATaskWithName( ADD_ADHOC_TASK_NAME ) );
   }


   @When( "^I upload the amended workscope spreadsheet with an additional job standard$" )
   public void iUploadTheAmendedWorkscopeSpreadsheetWithAnAdditionalJobStandard() throws Throwable {

      // upload the spreadsheet with the new task
      String lFilePathString = FILE_PATH + ADD_JOBSTANDARD_SPREADSHEET;
      File lFile = new File( lFilePathString );
      iManageWorkPackagePage.uploadWorkPackageWorkflow( lFile.getAbsolutePath() );
      iManageWorkPackagePage.clickCloseButtonOnUploadNotificationModal();
   }


   @Then( "^the new job standard exists in the work package$" )
   public void theNewJobStandardExistsInTheWorkPackage() throws Throwable {

      iManageWorkPackagePage.clickCloseButton();
      iWorkPackageSetupPage.clickWorkPackageName( WORK_PACKAGE_NAME );
      iWebDriverUtils.switchWindowTo( "Work Package Details" );
      assertTrue( iCheckDetailsPage.containsATaskWithName( ADD_JOBSTANDARD_NAME ) );

      if ( TEST_MAINTENANCE_MODE ) { // When TEST_MAINTENANCE_MODE = true, the test can be re-run
                                     // without a dB rebuild by canceling active tasks assigned to
                                     // WP's
         AssignedTasksPaneDriver lAssignedTasksPaneDriver =
               iCheckDetailsPage.clickTabAssignedTasks();
         lAssignedTasksPaneDriver.selectCheckBoxForTask( INITIAL_JOBSTANDARD_NAME );
         lAssignedTasksPaneDriver.selectCheckBoxForTask( ADD_JOBSTANDARD_NAME );
         lAssignedTasksPaneDriver.clickCancelTasks();
         iCancelTasksPageDriver.clickOk();
         iPleaseWaitPaneDriver.waitForPleaseWaitToClose( MAX_WAIT_TIME_IN_MS );
         iRequestAuthorizationPageDriver.setPassword_Type2( USERS_PASSWORD );
         iRequestAuthorizationPageDriver.clickOk();
         iPleaseWaitPaneDriver.waitForPleaseWaitToClose( MAX_WAIT_TIME_IN_MS );
      }
   }


   @When( "^I upload the amended workscope spreadsheet to cancel an adhoc task$" )
   public void iUploadTheAmendedWorkscopeSpreadsheetToCancelAnAdhocTask() throws Throwable {

      // upload the spreadsheet with the new task
      String lFilePathString = FILE_PATH + CANCEL_ADHOC_SPREADSHEET;
      File lFile = new File( lFilePathString );
      iManageWorkPackagePage.uploadWorkPackageWorkflow( lFile.getAbsolutePath() );
      iManageWorkPackagePage.clickCloseButtonOnUploadNotificationModal();
   }


   @Then( "^the adhoc task is cancelled$" )
   public void theAdhocTaskIsCancelled() throws Throwable {

      iManageWorkPackagePage.clickCloseButton();
      iWorkPackageSetupPage.clickWorkPackageName( WORK_PACKAGE_NAME );
      iWebDriverUtils.switchWindowTo( "Work Package Details" );
      iCheckDetailsPage.getAssignedTasksTab()
            .clickTaskInAssignedTasksTable( CANCEL_ADHOC_TASK_NAME );
      assertTrue( iTaskDetailsPage.clickTaskInformationTab().isCancelled() );

   }


   @When( "^I upload the amended workscope spreadsheet to cancel a job standard$" )
   public void iUploadTheAmendedWorkscopeSpreadsheetToCancelAJobStandard() throws Throwable {

      // upload the spreadsheet with the new task
      String lFilePathString = FILE_PATH + CANCEL_JOBSTANDARD_SPREADSHEET;
      File lFile = new File( lFilePathString );
      iManageWorkPackagePage.uploadWorkPackageWorkflow( lFile.getAbsolutePath() );
      iManageWorkPackagePage.clickCloseButtonOnUploadNotificationModal();
   }


   @Then( "^the jobstandard is cancelled$" )
   public void theJobstandardIsCancelled() throws Throwable {

      iManageWorkPackagePage.clickCloseButton();
      iWorkPackageSetupPage.clickWorkPackageName( WORK_PACKAGE_NAME );
      iWebDriverUtils.switchWindowTo( "Work Package Details" );
      iCheckDetailsPage.getAssignedTasksTab()
            .clickTaskInAssignedTasksTable( CANCEL_JOBSTANDARD_NAME );
      assertTrue( iTaskDetailsPage.clickTaskInformationTab().isCancelled() );
   }


   @When( "^I upload the amended workscope spreadsheet to edit an adhoc task$" )
   public void iUploadTheAmendedWorkscopeSpreadsheetToEditAnAdhocTask() throws Throwable {

      // upload the spreadsheet with the new task
      String lFilePathString = FILE_PATH + EDIT_ADHOC_SPREADSHEET;
      File lFile = new File( lFilePathString );
      iManageWorkPackagePage.uploadWorkPackageWorkflow( lFile.getAbsolutePath() );
      iManageWorkPackagePage.clickCloseButtonOnUploadNotificationModal();
   }


   @Then( "^the adhoc task is updated$" )
   public void theAdhocTaskIsUpdated() throws Throwable {

      iManageWorkPackagePage.clickCloseButton();
      iWorkPackageSetupPage.clickWorkPackageName( WORK_PACKAGE_NAME );
      iWebDriverUtils.switchWindowTo( "Work Package Details" );
      iCheckDetailsPage.getAssignedTasksTab().clickTaskInAssignedTasksTable( EDIT_ADHOC_TASK_NAME );
      iTaskDetailsPage.clickTaskInformationTab();
      assertTrue( iTaskDetailsPage.clickTabTaskExecution().hasLaborRow( UPDATED_ADHOC_SKILL ) );

   }


   @When( "^I upload the amended workscope spreadsheet to edit a job standard$" )
   public void iUploadTheAmendedWorkscopeSpreadsheetToEditAJobStandard() throws Throwable {

      // upload the spreadsheet with the new task
      String lFilePathString = FILE_PATH + EDIT_JOBSTANDARD_SPREADSHEET;
      File lFile = new File( lFilePathString );
      iManageWorkPackagePage.uploadWorkPackageWorkflow( lFile.getAbsolutePath() );
      iManageWorkPackagePage.clickCloseButtonOnUploadNotificationModal();
   }


   @Then( "^the jobstandard is updated$" )
   public void theJobstandardIsUpdated() throws Throwable {

      iManageWorkPackagePage.clickCloseButton();
      iWorkPackageSetupPage.clickWorkPackageName( WORK_PACKAGE_NAME );
      iWebDriverUtils.switchWindowTo( "Work Package Details" );
      iCheckDetailsPage.getAssignedTasksTab()
            .clickTaskInAssignedTasksTable( EDIT_JOBSTANDARD_NAME );
      iTaskDetailsPage.clickTabTaskExecution().getSubtaskRow( JOBSTANDARD_SUBTASK_NAME )
            .clickSubtaskName();
      assertTrue(
            iTaskDetailsPage.clickTabTaskExecution().hasLaborRow( UPDATED_JOBSTANDARD_SKILL ) );

      if ( TEST_MAINTENANCE_MODE ) { // When TEST_MAINTENANCE_MODE = true, the test can be re-run
                                     // without a dB rebuild by canceling active tasks assigned to
                                     // WP's
         iTaskDetailsPage.clickWorkPackageInformation();
         AssignedTasksPaneDriver lAssignedTasksPaneDriver =
               iCheckDetailsPage.clickTabAssignedTasks();
         lAssignedTasksPaneDriver.selectCheckBoxForTask( EDIT_JOBSTANDARD_NAME );
         lAssignedTasksPaneDriver.clickCancelTasks();
         iCancelTasksPageDriver.clickOk();
         iPleaseWaitPaneDriver.waitForPleaseWaitToClose( MAX_WAIT_TIME_IN_MS );
         iRequestAuthorizationPageDriver.setPassword_Type2( USERS_PASSWORD );
         iRequestAuthorizationPageDriver.clickOk();
         iPleaseWaitPaneDriver.waitForPleaseWaitToClose( MAX_WAIT_TIME_IN_MS );
      }
   }

}
