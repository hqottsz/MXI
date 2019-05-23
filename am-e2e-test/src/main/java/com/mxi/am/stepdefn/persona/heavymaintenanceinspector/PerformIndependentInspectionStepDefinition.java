package com.mxi.am.stepdefn.persona.heavymaintenanceinspector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.inject.Inject;

import com.mxi.am.driver.query.SchedTaskQueriesDriver;
import com.mxi.am.driver.web.AuthenticationRequiredPageDriver;
import com.mxi.am.driver.web.LoginPageDriver;
import com.mxi.am.driver.web.LogoutPageDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.PleaseWaitPaneDriver;
import com.mxi.am.driver.web.task.TaskDetails.TaskDetailsPageDriver;
import com.mxi.am.driver.web.task.labor.EditWorkCapturePageDriver;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class PerformIndependentInspectionStepDefinition {

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private LoginPageDriver iLoginPageDriver;

   @Inject
   private LogoutPageDriver iLogoutPageDriver;

   @Inject
   private TaskDetailsPageDriver iTaskDetailsPageDriver;

   @Inject
   private EditWorkCapturePageDriver iEditWorkCapturePageDriver;

   @Inject
   private AuthenticationRequiredPageDriver iAuthenticationRequiredPageDriver;

   @Inject
   private SchedTaskQueriesDriver iSchedTaskQueriesDriver;

   @Inject
   private PleaseWaitPaneDriver iPleaseWaitPaneDriver;

   private static final String USERNAME = "mxi";
   private static final String PASSWORD = "password";
   private static final String AIRCRAFT_SN = "20992-01";
   private static final String TASK_CODE = "JIC-ENG";
   private static final String SKILL = "ENG";
   private static final String RESULT = "PASS";
   private static final String INSPECTOR = "Inspector1";
   private static final String STAGE_COMPLETE = "COMPLETE";
   private static final int MAX_WAIT_TIME_IN_MS = 5 * 60 * 1000;


   @Given( "^a task labor is awaiting independent inspection$" )
   public void aTaskLaborIsAwaitingIndependentInspection() throws Throwable {
      iLoginPageDriver.setUserName( USERNAME ).setPassword( PASSWORD ).login();
      iNavigationDriver.barcodeSearch( iSchedTaskQueriesDriver
            .getByAircraftSerNoAndTaskCode( AIRCRAFT_SN, TASK_CODE ).getBarcode() );
      iTaskDetailsPageDriver.clickTabTaskExecution();
      iTaskDetailsPageDriver.getTabTaskExecution().getLaborRow( SKILL ).clickFinish();
      iAuthenticationRequiredPageDriver.setPasswordAndClickOK( PASSWORD );
      iPleaseWaitPaneDriver.waitForPleaseWaitToClose( MAX_WAIT_TIME_IN_MS );
      iEditWorkCapturePageDriver.clickOk();
      iNavigationDriver.clickLogout();
      iLogoutPageDriver.clickOK();
   }


   @When( "^I inspect the labor$" )
   public void iInspectTheLabor() throws Throwable {
      iNavigationDriver.barcodeSearch( iSchedTaskQueriesDriver
            .getByAircraftSerNoAndTaskCode( AIRCRAFT_SN, TASK_CODE ).getBarcode() );
      iTaskDetailsPageDriver.clickTabTaskExecution();
      iTaskDetailsPageDriver.getTabTaskExecution().getLaborRow( SKILL ).clickLabourRow();
      iTaskDetailsPageDriver.getTabTaskExecution().clickRecordInspection();
      iAuthenticationRequiredPageDriver.setPasswordAndClickOK( PASSWORD );
      iPleaseWaitPaneDriver.waitForPleaseWaitToClose( MAX_WAIT_TIME_IN_MS );
      iEditWorkCapturePageDriver.clickPassRadioButton();
      iEditWorkCapturePageDriver.clickOk();
   }


   @Then( "^the labor is inspected$" )
   public void theLaborIsInspected() throws Throwable {
      iTaskDetailsPageDriver.clickTabTaskExecution();
      assertEquals( "The Stage should be " + STAGE_COMPLETE + ".", STAGE_COMPLETE,
            iTaskDetailsPageDriver.getTabTaskExecution().getLaborRow( SKILL ).getStage() );
      assertTrue( "The Inspector should be " + INSPECTOR + ".", iTaskDetailsPageDriver
            .getTabTaskExecution().getLaborRow( SKILL ).getInspection().contains( INSPECTOR ) );
      assertTrue( "Inspection result should be " + RESULT + ".", iTaskDetailsPageDriver
            .getTabTaskExecution().getLaborRow( SKILL ).getInspection().contains( RESULT ) );
   }

}
