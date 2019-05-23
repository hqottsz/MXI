package com.mxi.am.stepdefn.persona.crewlead;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.runner.RunWith;

import com.mxi.am.driver.query.WorkPackageQueriesDriver;
import com.mxi.am.driver.web.AuthenticationRequiredPageDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.PleaseWaitPaneDriver;
import com.mxi.am.driver.web.common.SubmitReasonAndNotesPageDriver;
import com.mxi.am.driver.web.labour.AssignTasksToCrewShiftDayPageDriver;
import com.mxi.am.driver.web.task.CheckDetails.CheckDetailsPageDriver;
import com.mxi.am.driver.web.task.planshift.PlanShiftPageDriver;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.junit.Cucumber;


@RunWith( Cucumber.class )
public class AssignTasksToCrewShiftDayStepDefinition {

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private PlanShiftPageDriver iPlanShiftPageDriver;

   @Inject
   private CheckDetailsPageDriver iCheckDetailsPageDriver;

   @Inject
   private AssignTasksToCrewShiftDayPageDriver iAssignTasksToCrewShiftDayPageDriver;

   @Inject
   private SubmitReasonAndNotesPageDriver iSubmitReasonAndNotesPageDriver;

   @Inject
   private AuthenticationRequiredPageDriver iAuthenticationRequiredPageDriver;

   @Inject
   PleaseWaitPaneDriver iPleaseWaitPaneDriver;

   @Inject
   private WorkPackageQueriesDriver iWorkPackageQueriesDriver;

   private static final int MAX_WAIT_TIME_IN_MS = 5 * 60 * 1000;
   private static final String WORKPACKAGE_NAME = "BME Work Package";
   private static final String PASSWORD = "password";
   private final String iHourNumber = "168";
   static String iUnselectedTask;
   static String iCrewName;


   @When( "^I assign tasks to crew shift day$" )
   public void iAssignTasksToCrewShiftDay() throws Throwable {
      // the barcode is created to find the special Work Package that includes enough
      // data(tasks, crew shift day, etc.) to run the test
      iNavigationDriver.barcodeSearch(
            iWorkPackageQueriesDriver.getByWorkPackageName( WORKPACKAGE_NAME ).getBarcode() );

      iCheckDetailsPageDriver.clickCommitScopeButton();
      iSubmitReasonAndNotesPageDriver.clickOk();
      iAuthenticationRequiredPageDriver.setPasswordAndClickOK( PASSWORD );
      iPleaseWaitPaneDriver.waitForPleaseWaitToClose( MAX_WAIT_TIME_IN_MS );

      iCheckDetailsPageDriver.clickPlanShift();
      iPlanShiftPageDriver.clickSearch();
      iPlanShiftPageDriver.clickTabSearchResults().clickSelectAll();
      iPlanShiftPageDriver.getTabSearchResults().clickAssignCrewShiftDay();
      iUnselectedTask = iAssignTasksToCrewShiftDayPageDriver.unselectFirstTaskAndGetName();
      iAssignTasksToCrewShiftDayPageDriver.setHourNumber( iHourNumber );
      iAssignTasksToCrewShiftDayPageDriver.clickSearch();
      iCrewName = iAssignTasksToCrewShiftDayPageDriver.getCrewName();
      if ( iCrewName.contains( " (" ) )
         iCrewName = iCrewName.substring( 0, iCrewName.indexOf( " (" ) );
      iAssignTasksToCrewShiftDayPageDriver.clickOK();
   }


   @Then( "^Tasks are assigned to crew shift day$" )
   public void tasksAreAssignedToCrewShiftDay() throws Throwable {
      Assert.assertTrue( iPlanShiftPageDriver.getTabSearchResults()
            .isCrewShiftDayAssigned( iUnselectedTask, iCrewName ) );
   }
}
