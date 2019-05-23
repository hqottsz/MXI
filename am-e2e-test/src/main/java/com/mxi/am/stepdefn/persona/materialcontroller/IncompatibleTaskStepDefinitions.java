package com.mxi.am.stepdefn.persona.materialcontroller;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import org.junit.Assert;
import org.openqa.selenium.WebElement;

import com.mxi.am.driver.common.configurationParameters.ConfigurationParameterWorkflow;
import com.mxi.am.driver.query.InventoryQueriesDriver;
import com.mxi.am.driver.query.WorkPackageQueriesDriver;
import com.mxi.am.driver.web.AuthenticationRequiredPageDriver;
import com.mxi.am.driver.web.LoginPageDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.common.SubmitReasonAndNotesPageDriver;
import com.mxi.am.driver.web.inventory.inventorydetailspage.InventoryDetailsPageDriver;
import com.mxi.am.driver.web.inventory.inventorydetailspage.inventorydetailspanes.DetailsPaneDriver;
import com.mxi.am.driver.web.part.AddTaskIncompatibilityPageDriver;
import com.mxi.am.driver.web.part.EditPartRequirementsPageDriver;
import com.mxi.am.driver.web.part.EditPartRequirementsPageDriver.PartRequirementResult;
import com.mxi.am.driver.web.part.PartRequestDetailsPageDriver;
import com.mxi.am.driver.web.part.partgroupdetailspage.PartGroupDetailsPageDriver;
import com.mxi.am.driver.web.part.partgroupdetailspage.partgroupdetailspanes.IncompatibilityPaneDriver.TableTaskIncompatibleResult;
import com.mxi.am.driver.web.part.partgroupsearchpage.PartGroupSearchPageDriver;
import com.mxi.am.driver.web.part.partgroupsearchpage.partgroupsearchpanes.PartGroupsFoundPaneDriver.PartGroupSearchResult;
import com.mxi.am.driver.web.req.IssueInventoryPageDriver;
import com.mxi.am.driver.web.req.ReserveLocalInventoryPageDriver;
import com.mxi.am.driver.web.req.WarningsPageDriver;
import com.mxi.am.driver.web.task.CompleteTaskPageDriver;
import com.mxi.am.driver.web.task.CreateOrEditCheckPageDriver;
import com.mxi.am.driver.web.task.ScheduleCheckPageDriver;
import com.mxi.am.driver.web.task.StartCheckPageDriver;
import com.mxi.am.driver.web.task.CheckDetails.CheckDetailsPageDriver;
import com.mxi.am.driver.web.task.CheckDetails.CheckDetailsPanes.AssignedTasksPaneDriver;
import com.mxi.am.driver.web.task.CheckDetails.CheckDetailsPanes.UnAssignedTasksPaneDriver;
import com.mxi.am.driver.web.task.CheckDetails.CheckDetailsPanes.UnAssignedTasksPaneDriver.UnAssignedTaskResult;
import com.mxi.am.driver.web.task.TaskDetails.TaskDetailsPageDriver;
import com.mxi.am.driver.web.task.TaskDetails.TaskDetailsPanes.FinishLaborAuthenticationRequiredPageDriver;
import com.mxi.am.driver.web.task.TaskDetails.TaskDetailsPanes.TaskExecutionPaneDriver;
import com.mxi.am.driver.web.task.TaskDetails.TaskDetailsPanes.TaskExecutionPaneDriver.Labor;
import com.mxi.am.driver.web.task.TaskDetails.TaskDetailsPanes.TaskExecutionPaneDriver.PartRequests;
import com.mxi.am.driver.web.task.labor.EditWorkCapturePageDriver;
import com.mxi.am.driver.web.taskdefn.InitializeTaskPageDriver;
import com.mxi.am.driver.web.taskdefn.InitializeTaskPageDriver.InventoryTableRow;
import com.mxi.am.driver.web.taskdefn.SelectTaskDefinitionPageDriver;
import com.mxi.am.driver.web.taskdefn.SelectTaskDefinitionPageDriver.TaskDefinitionSearchResult;
import com.mxi.am.driver.web.taskdefn.reqdetailspage.ReqDetailsPageDriver;
import com.mxi.am.driver.web.taskdefn.taskdefinitionsearchpage.TaskDefinitionSearchPageDriver;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class IncompatibleTaskStepDefinitions {

   @Inject
   private TaskDefinitionSearchPageDriver iTaskDefinitionSearchPageDriver;

   @Inject
   private LoginPageDriver iLoginDriver;

   @Inject
   private PartGroupSearchPageDriver iPartGroupSearchPageDriver;

   @Inject
   private PartGroupDetailsPageDriver iPartGroupDetailsPageDriver;

   @Inject
   private AddTaskIncompatibilityPageDriver iAddTaskIncompatibilityPageDriver;

   @Inject
   private SelectTaskDefinitionPageDriver iSelectTaskDefinitionPageDriver;

   @Inject
   private InventoryDetailsPageDriver iInventoryDetailsPageDriver;

   @Inject
   private WarningsPageDriver iWarningsPageDriver;

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private TaskDetailsPageDriver iTaskDetailsPageDriver;

   @Inject
   private CreateOrEditCheckPageDriver iCreateOrEditCheckPageDriver;

   @Inject
   private CheckDetailsPageDriver iCheckDetailsPageDriver;

   @Inject
   private ScheduleCheckPageDriver iScheduleCheckPageDriver;

   @Inject
   private StartCheckPageDriver iStartCheckPageDriver;

   @Inject
   private ConfigurationParameterWorkflow iConfigurationParameterDriver;

   @Inject
   private SubmitReasonAndNotesPageDriver iSubmitReasonAndNotesPageDriver;

   @Inject
   private AuthenticationRequiredPageDriver iAuthenticationRequiredPageDriver;

   @Inject
   private PartRequestDetailsPageDriver iPartRequestDetailsPageDriver;

   @Inject
   private ReserveLocalInventoryPageDriver iReserveLocalInventoryPageDriver;

   @Inject
   private IssueInventoryPageDriver iIssueInventoryPageDriver;

   @Inject
   private CompleteTaskPageDriver iCompleteTaskPageDriver;

   @Inject
   private ReqDetailsPageDriver iReqDetailsPageDriver;

   @Inject
   private EditPartRequirementsPageDriver iEditPartRequirementsPageDriver;

   @Inject
   private EditWorkCapturePageDriver iEditWorkCapturePageDriver;

   @Inject
   private InitializeTaskPageDriver iInitializeTaskPageDriver;

   @Inject
   private FinishLaborAuthenticationRequiredPageDriver iFinishLaborAuthenticationRequiredPageDriver;

   @Inject
   private WorkPackageQueriesDriver iWorkPackageQueriesDriver;

   private final String iAircraftBarcode;


   @Inject
   public IncompatibleTaskStepDefinitions(InventoryQueriesDriver aInventoryQueriesDriver) {
      iAircraftBarcode =
            aInventoryQueriesDriver.getByAircraftRegistrationCode( AIRCRAFT_REG_CODE ).getBarcode();
   }


   private final String WORK_PACKAGE = "Incompatible-WP7";
   private final String SCHEDULED_LOCATION = "AIRPORT1/LINE";
   private final String SERIAL_BATCH_NO_1 = "INCOMP-001";
   private final String SERIAL_BATCH_NO_2 = "INCOMP-002";
   private final String OEM_PART_NO = "A0000017B";
   private final String REQUIREMENT_NAME_1 = "Incompatible-Req1";
   private final String REQUIREMENT_NAME_2 = "Incompatible-Part-Mod1";
   private final String AIRCRAFT_REG_NO = "Aircraft Part 2 - MAT-1";
   private final String AIRCRAFT_REG_CODE = "MAT-1";
   private String iBarcode;


   @Given( "^that I am a Material Controller$" )
   public void iAmMatereialController() throws Throwable {

      prepareIncompatiablePartTask();

      // set config parm as Warning instead of Error
      iConfigurationParameterDriver
            .temporaryParameterToggleAndRefresh( "TOBE_INST_NOT_TASK_COMPATIBLE", "WARNING" );

      // I need to create a work package then assign two tasks to this work package, one task is the
      // incompatible and another one is not
      iNavigationDriver.barcodeSearch( iAircraftBarcode );

      // create a work package for the inventory
      iInventoryDetailsPageDriver.clickTabOpen().clickTabOpenWorkPackages()
            .clickCreateWorkPackage();
      iCreateOrEditCheckPageDriver.setName( WORK_PACKAGE );
      iCreateOrEditCheckPageDriver.clickOK();

      // schedule the work package internally
      iCheckDetailsPageDriver.clickScheduleWorkPackage();
      iScheduleCheckPageDriver.setScheduledLocation( SCHEDULED_LOCATION );
      iScheduleCheckPageDriver.clickOK();

      // start the work package
      iCheckDetailsPageDriver.clickStartWorkPackage();
      iStartCheckPageDriver.clickOkButton();

      // assign the unassigned tasks to the work package
      iCheckDetailsPageDriver.clickTabUnAssignedTasks();
      UnAssignedTasksPaneDriver lUnAssignedTasksPaneDriver =
            iCheckDetailsPageDriver.clickTabUnAssignedTasks();

      List<UnAssignedTaskResult> lUnassignedResults = lUnAssignedTasksPaneDriver.getResults();
      for ( UnAssignedTaskResult lUnassignedResult : lUnassignedResults ) {
         String ataskname = lUnassignedResult.getTaskName();
         if ( ataskname.equals( "Incompatible-Part-Mod1 (Incompatible-Part-Mod1)" )
               || ataskname.equals( "Incompatible-Req1 (Incompatible-Req1)" ) ) {
            lUnassignedResult.clickCheckBox();
         }
      }

      lUnAssignedTasksPaneDriver.clickAssignTaskButton();
      iSubmitReasonAndNotesPageDriver.clickOk();
      iAuthenticationRequiredPageDriver.setPassword_Type2( "password" );
      iAuthenticationRequiredPageDriver.clickOk();
   }


   /**
    * 1.We need add an incompatibility rule to a part with a task definition within a part group
    * details. 2.We initialize 2 task definitions including the incompatible one to an aircraft
    *
    */
   private void prepareIncompatiablePartTask() {

      // user login
      iLoginDriver.setUserName( "user1" ).setPassword( "password" ).login();

      // he goes to part group search page and look for the part group
      iNavigationDriver.navigate( "Technical Records", "Part Group Search" );
      iPartGroupSearchPageDriver.clickClearAll();
      iPartGroupSearchPageDriver.setOemPartNo( OEM_PART_NO );
      iPartGroupSearchPageDriver.setPartGroup( "ACFT-SYS-1-1-TRK-MULTIPART" );
      iPartGroupSearchPageDriver.clickSearch();

      PartGroupSearchResult lResultRow =
            iPartGroupSearchPageDriver.clickTabPartGroupsFound().getResults().get( 0 );

      lResultRow.clickPartGroup( "ACFT-SYS-1-1-TRK-MULTIPART" );

      // go to part group details page - Incompatibility tab to add Task Incompatibility to a
      // particular part no
      List<TableTaskIncompatibleResult> lIncompatibleTaskResults =
            iPartGroupDetailsPageDriver.clickTabIncompatibility().getResults();
      for ( TableTaskIncompatibleResult lIncompatibleTaskResult : lIncompatibleTaskResults ) {
         String aPartNo = lIncompatibleTaskResult.getPartNo();
         if ( aPartNo.equals( OEM_PART_NO ) ) {
            lIncompatibleTaskResult.clickPartNo();
         }
      }

      iPartGroupDetailsPageDriver.getTabIncompatibility().clickAddTaskIncompatibility();

      // select OPEN task option then search the requirement we set up through Data loading
      iAddTaskIncompatibilityPageDriver.clickOpenTask();
      iAddTaskIncompatibilityPageDriver.clickSelectTaskDef();

      // search and select the requirement
      iSelectTaskDefinitionPageDriver.clickClearAll();
      iSelectTaskDefinitionPageDriver.setTaskCd( "Incompatible-Part-Mod1" );
      iSelectTaskDefinitionPageDriver.clickSearch();

      List<TaskDefinitionSearchResult> lTaskDefList = iSelectTaskDefinitionPageDriver.getResults();
      TaskDefinitionSearchResult lTaskDefResultRow = lTaskDefList.get( 0 );
      lTaskDefResultRow.clickTask();

      // click OK on the Add Task Incompatibility page
      iAddTaskIncompatibilityPageDriver.clickOk();

      // go to Task Definition search page and search the first requirement
      iNavigationDriver.navigate( "Engineer", "Task Definition Search" );
      iTaskDefinitionSearchPageDriver.clickClearAll();
      iTaskDefinitionSearchPageDriver.setTaskDefinitionCode( REQUIREMENT_NAME_1 );
      iTaskDefinitionSearchPageDriver.clickSearch();
      iTaskDefinitionSearchPageDriver.clickTabTaskDefinitionsFound()
            .clickTableTaskDefinition( REQUIREMENT_NAME_1 );

      // initialize the requirement
      iReqDetailsPageDriver.clickInitializeRequirement();

      List<InventoryTableRow> lInventorys = iInitializeTaskPageDriver.getInventoryTableRows();
      for ( InventoryTableRow lInventory : lInventorys ) {
         if ( lInventory.getRootInventory().equals( AIRCRAFT_REG_NO ) ) {
            lInventory.clickCheckbox();
         }

      }

      iInitializeTaskPageDriver.scrollToTop();
      iInitializeTaskPageDriver.clickCreateTasks();
      iInitializeTaskPageDriver.clickClose();

      // search another requirement

      // make sure the page is loaded completely
      Assert.assertTrue( iNavigationDriver
            .waitForElementToAppear( iReqDetailsPageDriver.getSchedulingTab(), 12000 ) );

      iNavigationDriver.navigate( "Engineer", "Task Definition Search" );
      iTaskDefinitionSearchPageDriver.clickClearAll();
      iTaskDefinitionSearchPageDriver.setTaskDefinitionCode( REQUIREMENT_NAME_2 );
      iTaskDefinitionSearchPageDriver.clickSearch();
      iTaskDefinitionSearchPageDriver.clickTabTaskDefinitionsFound()
            .clickTableTaskDefinition( REQUIREMENT_NAME_2 );

      // initialize the requirement
      iReqDetailsPageDriver.clickInitializeRequirement();

      List<InventoryTableRow> lInventory2s = iInitializeTaskPageDriver.getInventoryTableRows();
      for ( InventoryTableRow lInventory2 : lInventory2s ) {
         if ( lInventory2.getRootInventory().equals( AIRCRAFT_REG_NO ) ) {
            lInventory2.clickCheckbox();
         }

      }

      iInitializeTaskPageDriver.scrollToTop();
      iInitializeTaskPageDriver.clickCreateTasks();
      iInitializeTaskPageDriver.clickClose();

   }


   @When( "^I try to reserve a part which is not compatible with an OPEN task for a different task$" )
   public void iReserveIncompatiablePart() throws Throwable {

      // go to assigned tasks and click the particular JIC
      AssignedTasksPaneDriver lAssignedTasksPaneDriver =
            iCheckDetailsPageDriver.clickTabAssignedTasks();

      lAssignedTasksPaneDriver.clickTaskInAssignedTasksTable( "JIC-Mod2 (JIC under another Req)" );

      // Set the removed part serial no
      TaskExecutionPaneDriver lTaskExecutionPaneDriver =
            iTaskDetailsPageDriver.clickTabTaskExecution();

      lTaskExecutionPaneDriver.clickEditPartRequirement();
      List<PartRequirementResult> lResults = iEditPartRequirementsPageDriver.getResults();
      PartRequirementResult lPartRequirementResultRow = lResults.get( 0 );
      lPartRequirementResultRow.setRemovedSerBatNo( "Remove-Mod1" );

      // click OK button
      iEditPartRequirementsPageDriver.clickOk();

      // Then click the link of part request
      List<PartRequests> lPartReqestResults = lTaskExecutionPaneDriver.getPartRequestsRows();
      PartRequests lPartRequirementsResultRow = lPartReqestResults.get( 0 );
      lPartRequirementsResultRow.clickRequestID();

      // on the part request page, click Reserve local inventory
      iPartRequestDetailsPageDriver.clickReserveLocalInventory();

   }


   @Then( "^I will get a warning saying this part is not compatible with an ACTV task$" )
   public void iGetWarningforImpatible() throws Throwable {

      // select the inventory to be reserved
      iReserveLocalInventoryPageDriver.clickRadioButtonForInventory( SERIAL_BATCH_NO_1 );
      iReserveLocalInventoryPageDriver.clickOk();

      // make sure the warning message then click ok on the warning message
      Assert.assertEquals( true, isWarningPageVisible() );
      iWarningsPageDriver.clickOk();
   }


   @And( "^if I try to install the same part on the task, I will get a similar error$" )
   public void iGetWarningforImpatibleWhenInstall() throws Throwable {
      // click the reserved inventory to go to inventory details page
      iPartRequestDetailsPageDriver.clickReservedInventory();

      // get the barcode of inventory
      iBarcode = iInventoryDetailsPageDriver.clickTabDetails().getBarcode();

      iInventoryDetailsPageDriver.clickClose();

      // click issue inventory
      iPartRequestDetailsPageDriver.clickIssueInventory();

      // enter barcode
      iIssueInventoryPageDriver.setBarcode( iBarcode );

      // click Add part then click OK
      iIssueInventoryPageDriver.clickAddPart();

      iIssueInventoryPageDriver.clickOk();

      // make sure the inventory is issued then close the part request page
      Assert.assertTrue( iPartRequestDetailsPageDriver.getRequestSatus().contains( "ISSUED" ) );
      iPartRequestDetailsPageDriver.clickClose();

      // click Complete Task button on the task details
      iTaskDetailsPageDriver.clickCompleteTask();
      iCompleteTaskPageDriver.clickOkButton();

      iAuthenticationRequiredPageDriver.setPassword_Type2( "password" );
      iAuthenticationRequiredPageDriver.clickOk();

      // confirm we have a warning message
      Assert.assertEquals( true, isWarningPageVisible() );

      // May need to add a wait here since complete task action takes too long sometime
      // Wait.pause( 2000 );

   }


   @When( "^I try to reserve a part which is not compatible with an OPEN task for the task itself$" )
   public void iReserveIncompatiablePartforTaskSelf() throws Throwable {

      // get the barcode of work pacakge to easily access
      String iWorkPacakgeBarcode =
            iWorkPackageQueriesDriver.getByWorkPackageName( WORK_PACKAGE ).getBarcode();

      // login again then go to aircraft details page
      iLoginDriver.setUserName( "user1" ).setPassword( "password" ).login();

      // go to work package details
      iNavigationDriver.barcodeSearch( iWorkPacakgeBarcode );

      // go to assigned tasks and click the particular JIC
      AssignedTasksPaneDriver lAssignedTasksPaneDriver =
            iCheckDetailsPageDriver.clickTabAssignedTasks();

      lAssignedTasksPaneDriver.clickTaskInAssignedTasksTable( "JIC-Mod1 (JIC under Mod EO)" );

      // Set the removed part serial no
      TaskExecutionPaneDriver lTaskExecutionPaneDriver =
            iTaskDetailsPageDriver.clickTabTaskExecution();

      lTaskExecutionPaneDriver.clickEditPartRequirement();
      List<PartRequirementResult> lResults = iEditPartRequirementsPageDriver.getResults();
      PartRequirementResult lPartRequirementResultRow = lResults.get( 0 );
      lPartRequirementResultRow.setRemovedSerBatNo( "Remove-Mod2" );

      // click OK button
      iEditPartRequirementsPageDriver.clickOk();

      // Then click the link of part request
      List<PartRequests> lPartReqestResults = lTaskExecutionPaneDriver.getPartRequestsRows();
      PartRequests lPartRequirementsResultRow = lPartReqestResults.get( 0 );
      lPartRequirementsResultRow.clickRequestID();

      // on the part request page, click Reserve local inventory
      iPartRequestDetailsPageDriver.clickReserveLocalInventory();

   }


   @Then( "^I will not get a warning saying this part is not compatible with an ACTV task$" )
   public void iNoWarningforImpatible() throws Throwable {

      // select the inventory to be reserved
      iReserveLocalInventoryPageDriver.clickRadioButtonForInventory( SERIAL_BATCH_NO_2 );
      iReserveLocalInventoryPageDriver.clickOk();

      // make sure no warning message
      Assert.assertTrue( iPartRequestDetailsPageDriver.getRequestSatus().contains( "AVAIL" ) );
   }


   @And( "^if I try to install the same part on the task, I will not get a similar error$" )
   public void iNoWarningforImpatibleWhenInstall() throws Throwable {
      // click the reserved inventory to go to inventory details page
      iPartRequestDetailsPageDriver.clickReservedInventory();

      // get the barcode of inventory
      DetailsPaneDriver lDetailsPaneDriver = iInventoryDetailsPageDriver.clickTabDetails();

      iBarcode = lDetailsPaneDriver.getBarcode();

      iInventoryDetailsPageDriver.clickClose();

      // click issue inventory
      iPartRequestDetailsPageDriver.clickIssueInventory();

      // enter barcode
      iIssueInventoryPageDriver.setBarcode( iBarcode );

      // click Add part then click OK
      iIssueInventoryPageDriver.clickAddPart();

      iIssueInventoryPageDriver.clickOk();

      // make sure the inventory is issued then close the part request page
      Assert.assertTrue( iPartRequestDetailsPageDriver.getRequestSatus().contains( "ISSUED" ) );
      iPartRequestDetailsPageDriver.clickClose();

      // click Finish Labor button on the task details
      TaskExecutionPaneDriver lTaskExecutionPaneDriver =
            iTaskDetailsPageDriver.clickTabTaskExecution();

      List<Labor> lLaborResults = lTaskExecutionPaneDriver.getLaborRows();
      Labor lLaborResultRow = lLaborResults.get( 0 );
      lLaborResultRow.clickFinish();

      iFinishLaborAuthenticationRequiredPageDriver.setPassword( "password" );
      iFinishLaborAuthenticationRequiredPageDriver.clickOk();

      // on the EditWorkCapturePage, we check the removed part and installed part, then click OK
      iEditWorkCapturePageDriver.clickRemovedPartCheckBox();
      iEditWorkCapturePageDriver.clickInstalledPartCheckBox();
      iEditWorkCapturePageDriver.setRemovedPartReason( "ACCESS" );
      iEditWorkCapturePageDriver.clickOk();

      // revert the config parameter back to ERROR
      iTaskDetailsPageDriver.clickClose();
      iConfigurationParameterDriver.updateParameter( "TOBE_INST_NOT_TASK_COMPATIBLE", "ERROR" );
   }


   private boolean isWarningPageVisible() {
      Provider<WebElement> lOkButton = iWarningsPageDriver.getOkButton();
      Provider<WebElement> lCancelButton = iWarningsPageDriver.getCancelButton();
      return lOkButton != null && lCancelButton != null;
   }

}
