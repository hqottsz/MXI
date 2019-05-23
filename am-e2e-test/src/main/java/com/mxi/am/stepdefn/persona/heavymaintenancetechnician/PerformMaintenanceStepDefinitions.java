package com.mxi.am.stepdefn.persona.heavymaintenancetechnician;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.inject.Inject;

import com.mxi.am.driver.query.SchedTaskQueriesDriver;
import com.mxi.am.driver.web.AuthenticationRequiredPageDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.task.TaskDetails.TaskDetailsPageDriver;
import com.mxi.am.driver.web.task.labor.EditWorkCapturePageDriver;
import com.mxi.am.driver.web.todolist.ToDoListPageDriver;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class PerformMaintenanceStepDefinitions {

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private ToDoListPageDriver iToDoListPageDriver;

   @Inject
   private TaskDetailsPageDriver iTaskDetailsPageDriver;

   @Inject
   private AuthenticationRequiredPageDriver iAuthenticationRequiredPageDriver;

   @Inject
   private EditWorkCapturePageDriver iEditWorkCapturePageDriver;

   @Inject
   private SchedTaskQueriesDriver iSchedTaskQueriesDriver;

   private static final int ONE = 1;
   private static final int FIRST_ROW = 0;
   private static final String HEAVY_TECHNICIAN = "Heavy Technician";
   private static final String HEAVY_TECHNICIAN_TO_DO_LIST = "To Do List (Heavy Technician)";
   private static final String PASSWORD = "password";
   private static final String TECHNICIAN = "Technologies, Mxi";
   private static final String UNASSIGNED = "Unassigned";
   private static final String AIRCRAFT_SN = "20988-01";
   private static final String TASK_TO_START = "BM_SCH_TSK14";
   private static final String TASK_TO_STOP = "BM_SCH_TSK15";
   private static final String TASK_TO_FINISH = "BM_SCH_TSK16";
   private static final String TASK_TO_CERTIFY = "BM_SCH_TSK17";
   private static final String TASK_TO_REVIEW = "BM_SCH_TSK18";
   private static final String LABOR_SKILL = "ENG";
   private static final String CERTIFIER = "Technologies, Mxi";
   private static final String STAGE_ACTIVE = "ACTV";
   private static final String STAGE_IN_WORK = "IN WORK";
   private static final String STAGE_COMPLETE = "COMPLETE";
   private static final String STATUS_IN_WORK = "IN WORK (Task In Work)";
   private static final String STATUS_PAUSE = "PAUSE (Pause)";
   private static final String STATUS_COMPLETE = "COMPLETE (Complete)";
   private static final String COLUMN_TECHNICIAN = "Work Performed.Technician";
   private static final String REMAINING_HOURS = "0:01";


   @Given( "^I have a task with a labour row not started$" )
   public void iHaveATaskWithALabourRowNotStarted() throws Throwable {
      iNavigationDriver.barcodeSearch( iSchedTaskQueriesDriver
            .getByAircraftSerNoAndTaskCode( AIRCRAFT_SN, TASK_TO_START ).getBarcode() );
   }


   @When( "^I start the time tracking on the labour row$" )
   public void iStartTheTimeTrackingOnTheLabourRow() throws Throwable {
      iTaskDetailsPageDriver.clickTabTaskExecution();
      iTaskDetailsPageDriver.getTabTaskExecution().getLaborRow( LABOR_SKILL ).clickStart();
      iAuthenticationRequiredPageDriver.setPasswordAndClickOK( PASSWORD );
   }


   @Then( "^the labour row is IN WORK$" )
   public void theLabourRowIsINWORKAndTheTaskIsINWORK() throws Throwable {
      iTaskDetailsPageDriver.clickTabTaskExecution();
      assertEquals( STAGE_IN_WORK,
            iTaskDetailsPageDriver.getTabTaskExecution().getLaborRow( LABOR_SKILL ).getStage() );
      iTaskDetailsPageDriver.clickTabTaskInformation();
      assertEquals( STATUS_IN_WORK, iTaskDetailsPageDriver.getTabTaskInformation().getStatus() );
   }


   @Given( "^I have a task with a labour row started$" )
   public void iHaveATaskWithALabourRowStarted() throws Throwable {
      iNavigationDriver.barcodeSearch( iSchedTaskQueriesDriver
            .getByAircraftSerNoAndTaskCode( AIRCRAFT_SN, TASK_TO_STOP ).getBarcode() );
   }


   @When( "^I stop the time tracking on the labour row$" )
   public void iStopTheTimeTrackingOnTheLabourRow() throws Throwable {
      iTaskDetailsPageDriver.clickTabTaskExecution();
      iTaskDetailsPageDriver.getTabTaskExecution().getLaborRow( LABOR_SKILL ).clickJobStop();
      iAuthenticationRequiredPageDriver.setPasswordAndClickOK( PASSWORD );
      iEditWorkCapturePageDriver.setRemainingHours( REMAINING_HOURS );
      iEditWorkCapturePageDriver.clickOk();
   }


   @Then( "^the labour row is STOP$" )
   public void theLabourRowIsSTOP() throws Throwable {
      iTaskDetailsPageDriver.clickTabTaskExecution();
      assertEquals( ONE, iTaskDetailsPageDriver.getTabTaskExecution()
            .getLaborsByColumn( COLUMN_TECHNICIAN, TECHNICIAN ).size() );
      assertEquals( STAGE_COMPLETE, iTaskDetailsPageDriver.getTabTaskExecution()
            .getLaborsByColumn( COLUMN_TECHNICIAN, TECHNICIAN ).get( FIRST_ROW ).getStage() );
      assertEquals( ONE, iTaskDetailsPageDriver.getTabTaskExecution()
            .getLaborsByColumn( COLUMN_TECHNICIAN, UNASSIGNED ).size() );
      assertEquals( STAGE_ACTIVE, iTaskDetailsPageDriver.getTabTaskExecution()
            .getLaborsByColumn( COLUMN_TECHNICIAN, UNASSIGNED ).get( FIRST_ROW ).getStage() );

      iTaskDetailsPageDriver.clickTabTaskInformation();
      assertEquals( STATUS_PAUSE, iTaskDetailsPageDriver.getTabTaskInformation().getStatus() );
   }


   @Given( "^a single INWORK labour row on a task$" )
   public void aSingleINWORKLabourRowOnATask() throws Throwable {
      iNavigationDriver.barcodeSearch( iSchedTaskQueriesDriver
            .getByAircraftSerNoAndTaskCode( AIRCRAFT_SN, TASK_TO_FINISH ).getBarcode() );
   }


   @When( "^I record the maintenance performed$" )
   public void iRecordTheMaintenancePerformed() throws Throwable {
      iTaskDetailsPageDriver.clickTabTaskExecution();
      iTaskDetailsPageDriver.getTabTaskExecution().getLaborRow( LABOR_SKILL ).clickFinish();
      iAuthenticationRequiredPageDriver.setPasswordAndClickOK( PASSWORD );
      iEditWorkCapturePageDriver.clickOk();
   }


   @Then( "^the labour row is COMPLETE$" )
   public void theLabourRowIsCOMPLETE() throws Throwable {
      iTaskDetailsPageDriver.clickTabTaskExecution();
      assertEquals( STAGE_COMPLETE,
            iTaskDetailsPageDriver.getTabTaskExecution().getLaborRow( LABOR_SKILL ).getStage() );
      iTaskDetailsPageDriver.clickTabTaskInformation();
      assertEquals( STATUS_COMPLETE, iTaskDetailsPageDriver.getTabTaskInformation().getStatus() );
   }


   @Given( "^a single INWORK labour row needs certification$" )
   public void aSingleINWORKLabourRowNeedsCertification() throws Throwable {
      iNavigationDriver.barcodeSearch( iSchedTaskQueriesDriver
            .getByAircraftSerNoAndTaskCode( AIRCRAFT_SN, TASK_TO_CERTIFY ).getBarcode() );
   }


   @When( "^I finish and certify the labour row$" )
   public void iFinishAndCertifyTheLabourRow() throws Throwable {
      iTaskDetailsPageDriver.clickTabTaskExecution();
      iTaskDetailsPageDriver.getTabTaskExecution().getLaborRow( LABOR_SKILL ).clickFinish();
      iAuthenticationRequiredPageDriver.setPasswordAndClickOK( PASSWORD );
      iEditWorkCapturePageDriver.clickOk();
   }


   @Then( "^the labour row is COMPLETE and certified$" )
   public void theLabourRowIsCOMPLETEAndCertified() throws Throwable {
      iTaskDetailsPageDriver.clickTabTaskExecution();
      assertEquals( STAGE_COMPLETE,
            iTaskDetailsPageDriver.getTabTaskExecution().getLaborRow( LABOR_SKILL ).getStage() );
      assertEquals( CERTIFIER, iTaskDetailsPageDriver.getTabTaskExecution()
            .getLaborRow( LABOR_SKILL ).getCertification() );
   }


   @Given( "^I have work to do$" )
   public void iHaveWorkToDo() throws Throwable {
      iNavigationDriver.navigate( HEAVY_TECHNICIAN, HEAVY_TECHNICIAN_TO_DO_LIST );
   }


   @When( "^I review My Tasks$" )
   public void iReviewMyTasks() throws Throwable {
      iToDoListPageDriver.clickTabMyTasks();
   }


   @Then( "^I can see the Task$" )
   public void iCanSeeTheTask() throws Throwable {
      assertTrue( iToDoListPageDriver.getTabMyTasks().isInMyTasks( TASK_TO_REVIEW ) );
   }
}
