package com.mxi.am.stepdefn.persona.productioncontroller;

import static org.junit.Assert.assertTrue;

import java.io.File;

import javax.inject.Inject;

import org.junit.Assert;

import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.ppcspreadsheet.ui.induction.ManageWorkPackagePageDriver;
import com.mxi.am.driver.web.ppcspreadsheet.ui.induction.WorkPackageLoaderPageDriver;
import com.mxi.am.driver.web.ppcspreadsheet.ui.induction.WorkPackageLoaderPageDriver.WorkPackageSetup;
import com.mxi.am.driver.web.task.CheckDetails.CheckDetailsPageDriver;
import com.mxi.am.driver.web.task.CheckDetails.CheckDetailsPanes.AssignedTasksPaneDriver;
import com.mxi.am.driver.web.task.TaskDetails.TaskDetailsPageDriver;
import com.mxi.am.driver.web.task.TaskDetails.TaskDetailsPanes.TaskExecutionPaneDriver;
import com.mxi.am.driver.web.task.TaskDetails.TaskDetailsPanes.TaskInformationPaneDriver;
import com.mxi.am.driver.web.utl.WebDriverUtls;
import com.mxi.am.helper.builder.WorkPackageSetupBuilder;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;


@ScenarioScoped
public class UploadInitialWorkscopeStepDefinitions {

   // Existing data in DB.
   private static final String HEAVY_PLANNER = "Heavy Planner";
   private static final String WORK_PACKAGE_LOADER = "Work Package Loader";
   private static final String ADHOC_TASK_ROOT_CONFIG_SLOT = "ACFT_CD1 (Aircraft Assembly)";
   private static final String ADHOC_TASK_SYS_CONFIG_SLOT = "SYS-1 (Aircraft System 1)";

   @Inject
   private WorkPackageLoaderPageDriver iWorkPackageLoaderPageDriver;

   @Inject
   private ManageWorkPackagePageDriver iManageWorkPackagePageDriver;

   @Inject
   private WorkPackageSetupBuilder iWorkPackageSetupBuilder;

   @Inject
   private CheckDetailsPageDriver iCheckDetailsPage;

   @Inject
   private TaskDetailsPageDriver iTaskDetailsPageDriver;

   @Inject
   private WebDriverUtls iWebDriverUtls;

   @Inject
   private NavigationDriver iNavigationDriver;

   private WorkPackageSetup iCreatedWpSetupInfo;

   private String iWpName;


   @When( "^I create work package setup$" )
   public void iCreateWorkPackageSetup() throws Throwable {

      iNavigationDriver.navigate( HEAVY_PLANNER, WORK_PACKAGE_LOADER );
      iCreatedWpSetupInfo = iWorkPackageLoaderPageDriver.createWorkPackageSetup();
      iManageWorkPackagePageDriver.clickCloseButton();
   }


   @Then( "^the work package setup is available for workscope upload$" )
   public void theWorkPackageSetupIsAvailableForWorkscopeUpload() throws Throwable {

      Assert.assertTrue( iWorkPackageLoaderPageDriver
            .containsAWorkPackageNamed( iCreatedWpSetupInfo.getName() ) );
   }


   @Given( "^I have a work package setup \"([^\"]*)\"$" )
   public void iHaveAWorkPackageSetup( String aWpName ) throws Throwable {

      iWpName = aWpName;
      iWorkPackageSetupBuilder.withName( aWpName ).build();
   }


   @When( "^I upload the workscope spreadsheet \"([^\"]*)\"$" )
   public void iUploadTheWorkscopeSpreadsheet( String aFileName ) throws Throwable {

      iNavigationDriver.navigate( HEAVY_PLANNER, WORK_PACKAGE_LOADER );
      iWorkPackageLoaderPageDriver.clickManageForWorkPackage( iWpName );
      String lFilePathString =
            "src/main/resources/com/mxi/am/persona/productioncontroller/spreadsheets/UploadWorkscope/"
                  + aFileName;
      File lFile = new File( lFilePathString );
      iManageWorkPackagePageDriver.uploadWorkPackageWorkflow( lFile.getAbsolutePath() );
   }


   @Then( "^I receive a success notification$" )
   public void iReceiveASuccessNotification() throws Throwable {

      assertTrue( "Success notification should be shown",
            iManageWorkPackagePageDriver.showsSuccessfulUploadNotification() );
   }


   @Then( "^the task \"([^\"]*)\" is created against the root slot in the work package$" )
   public void theTaskIsCreatedAgainstTheRootSlotInTheWorkPackage( String aTaskName )
         throws Throwable {

      iManageWorkPackagePageDriver.clickCloseButton();
      iWorkPackageLoaderPageDriver.clickWorkPackageName( iWpName );
      iWebDriverUtls.switchWindowTo( "Work Package Details" );
      iCheckDetailsPage.clickTabAssignedTasks();

      // validate task name
      assertTrue( iCheckDetailsPage.containsATaskWithName( aTaskName ) );

      // validate config slot
      AssignedTasksPaneDriver lAssignedTasksPaneDriver = iCheckDetailsPage.clickTabAssignedTasks();
      lAssignedTasksPaneDriver.clickTaskInAssignedTasksTable( aTaskName );
      TaskInformationPaneDriver lTaskInformationPaneDriver =
            iTaskDetailsPageDriver.clickTabTaskInformation();
      Assert.assertEquals( "Unexpected config slot.", ADHOC_TASK_ROOT_CONFIG_SLOT,
            lTaskInformationPaneDriver.getConfigSlot() );
   }


   @Then( "^the task \"([^\"]*)\" is created against the system slot in the work package$" )
   public void theTaskIsCreatedAgainstTheSystemSlotInTheWorkPackage( String aTaskName )
         throws Throwable {

      iManageWorkPackagePageDriver.clickCloseButton();
      iWorkPackageLoaderPageDriver.clickWorkPackageName( iWpName );
      iWebDriverUtls.switchWindowTo( "Work Package Details" );
      iCheckDetailsPage.clickTabAssignedTasks();

      // validate task name
      assertTrue( iCheckDetailsPage.containsATaskWithName( aTaskName ) );

      // validate config slot
      AssignedTasksPaneDriver lAssignedTasksPaneDriver = iCheckDetailsPage.clickTabAssignedTasks();
      lAssignedTasksPaneDriver.clickTaskInAssignedTasksTable( aTaskName );
      TaskInformationPaneDriver lTaskInformationPaneDriver =
            iTaskDetailsPageDriver.clickTabTaskInformation();
      Assert.assertEquals( "Unexpected config slot.", ADHOC_TASK_SYS_CONFIG_SLOT,
            lTaskInformationPaneDriver.getConfigSlot() );
   }


   @Then( "^the task \"([^\"]*)\" is created with certification and inspection required in the work package$" )
   public void theTaskIsCreatedWithCertificationAndInspectionRequiredInTheWorkPackage(
         String aTaskName ) throws Throwable {

      iManageWorkPackagePageDriver.clickCloseButton();
      iWorkPackageLoaderPageDriver.clickWorkPackageName( iWpName );
      iWebDriverUtls.switchWindowTo( "Work Package Details" );
      iCheckDetailsPage.clickTabAssignedTasks();

      // validate task name
      assertTrue( iCheckDetailsPage.containsATaskWithName( aTaskName ) );

      // validate Labor Certification and Inspection
      AssignedTasksPaneDriver lAssignedTasksPaneDriver = iCheckDetailsPage.clickTabAssignedTasks();
      lAssignedTasksPaneDriver.clickTaskInAssignedTasksTable( aTaskName );
      TaskExecutionPaneDriver lTaskExecutionPaneDriver =
            iTaskDetailsPageDriver.clickTabTaskExecution();
      assertTrue( lTaskExecutionPaneDriver.hasLaborRow( "AVIONICS", "PENDING", "PENDING" ) );
   }


   @Then( "^the task \"([^\"]*)\" exists in the work package$" )
   public void theTaskExistsInTheWorkPackage( String aTaskName ) throws Throwable {

      iManageWorkPackagePageDriver.clickCloseButton();
      iWorkPackageLoaderPageDriver.clickWorkPackageName( iWpName );
      iWebDriverUtls.switchWindowTo( "Work Package Details" );
      iCheckDetailsPage.clickTabAssignedTasks();
      assertTrue( iCheckDetailsPage.containsATaskWithName( aTaskName ) );
   }


   @Then( "^I receive an error notification$" )
   public void iReceiveAnErrorNotification() throws Throwable {

      assertTrue( "Error notification should be shown",
            iManageWorkPackagePageDriver.showsUploadErrorNotification() );
   }


   @Then( "^I receive a warning notification$" )
   public void iReceiveAWarningNotification() throws Throwable {

      assertTrue( "Success notification should be shown",
            Integer.parseInt( iManageWorkPackagePageDriver.getWarningCount() ) > 0 );
      iManageWorkPackagePageDriver.clickCloseButtonOnUploadNotificationModal();
   }

}
