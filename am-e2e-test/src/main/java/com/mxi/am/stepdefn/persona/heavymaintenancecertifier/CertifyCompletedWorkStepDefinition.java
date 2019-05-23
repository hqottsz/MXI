package com.mxi.am.stepdefn.persona.heavymaintenancecertifier;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;

import com.mxi.am.driver.query.SchedTaskQueriesDriver;
import com.mxi.am.driver.web.AuthenticationRequiredPageDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.task.TaskDetails.TaskDetailsPageDriver;
import com.mxi.am.driver.web.task.labor.EditWorkCapturePageDriver;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class CertifyCompletedWorkStepDefinition {

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private TaskDetailsPageDriver iTaskDetailsPageDriver;

   @Inject
   private EditWorkCapturePageDriver iEditWorkCapturePageDriver;

   @Inject
   private AuthenticationRequiredPageDriver iAuthenticationRequiredPageDriver;

   @Inject
   private SchedTaskQueriesDriver iSchedTaskQueriesDriver;

   private static final String PASSWORD = "password";
   private static final String AIRCRAFT_SN = "20991-01";
   private static final String TASK_CODE = "BM_SCH_TSK13";
   private static final String SKILL = "ENG";
   private static final String CERTIFIER = "Technologies, Mxi";
   private static final String STAGE_COMPLETE = "COMPLETE";
   private static final String STATUS_COMPLETE = "COMPLETE (Complete)";


   @Given( "^I have task labor awaiting certification$" )
   public void iHaveTaskLaborAwaitingCertification() throws Throwable {
      iNavigationDriver.barcodeSearch( iSchedTaskQueriesDriver
            .getByAircraftSerNoAndTaskCode( AIRCRAFT_SN, TASK_CODE ).getBarcode() );
   }


   @When( "^I certiy the labor$" )
   public void iCertiyTheLabor() throws Throwable {
      iTaskDetailsPageDriver.clickTabTaskExecution();
      iTaskDetailsPageDriver.getTabTaskExecution().getLaborRow( SKILL ).clickLabourRow();
      iTaskDetailsPageDriver.getTabTaskExecution().clickCertify();
      iAuthenticationRequiredPageDriver.setPasswordAndClickOK( PASSWORD );
      iEditWorkCapturePageDriver.clickOk();
   }


   @Then( "^the labor is certified and task is COMPLETE$" )
   public void theLaborIsCertified() throws Throwable {
      iTaskDetailsPageDriver.clickTabTaskExecution();
      assertEquals( STAGE_COMPLETE,
            iTaskDetailsPageDriver.getTabTaskExecution().getLaborRow( SKILL ).getStage() );
      assertEquals( CERTIFIER,
            iTaskDetailsPageDriver.getTabTaskExecution().getLaborRow( SKILL ).getCertification() );
      iTaskDetailsPageDriver.clickTabTaskInformation();
      assertEquals( STATUS_COMPLETE, iTaskDetailsPageDriver.getTabTaskInformation().getStatus() );
   }
}
