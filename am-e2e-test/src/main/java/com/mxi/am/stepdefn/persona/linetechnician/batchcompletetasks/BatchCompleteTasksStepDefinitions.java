package com.mxi.am.stepdefn.persona.linetechnician.batchcompletetasks;

import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import com.mxi.am.driver.query.SchedTaskQueriesDriver;
import com.mxi.am.driver.query.WorkPackageKey;
import com.mxi.am.driver.query.WorkPackageQueriesDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.po.RequestAuthorizationPageDriver;
import com.mxi.am.driver.web.task.CompleteTasksPageDriver;
import com.mxi.am.driver.web.task.CheckDetails.CheckDetailsPageDriver;
import com.mxi.am.stepdefn.persona.linetechnician.batchcompletetasks.data.BatchCompleteAllTasksInWPScenarioData;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class BatchCompleteTasksStepDefinitions {

   private static final String LINETECH_PASSWORD = "password";

   @Inject
   private CheckDetailsPageDriver iCheckDetailsPageDriver;

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private CompleteTasksPageDriver iCompleteTasksPageDriver;

   @Inject
   private SchedTaskQueriesDriver iSchedTaskQueriesDriver;

   @Inject
   private WorkPackageQueriesDriver iWorkPackageQueriesDriver;

   @Inject
   private RequestAuthorizationPageDriver iRequestAuthorizationPageDriver;


   @When( "^I batch complete all tasks in a work package$" )
   public void iBatchCompleteAllTasksInAWorkPackage() throws Throwable {

      // Set up tasks in work package
      addTasksToWorkPackage( BatchCompleteAllTasksInWPScenarioData.AIRCRAFT_NAME,
            BatchCompleteAllTasksInWPScenarioData.WORK_PACKAGE_NAME,
            BatchCompleteAllTasksInWPScenarioData.TASK1_NAME,
            BatchCompleteAllTasksInWPScenarioData.TASK2_NAME );

      // Use barcode search to go to the work package
      WorkPackageKey lWorkPackage = iWorkPackageQueriesDriver
            .getByWorkPackageName( BatchCompleteAllTasksInWPScenarioData.WORK_PACKAGE_NAME );
      iNavigationDriver.barcodeSearch( lWorkPackage.getBarcode() );

      // batch complete all tasks
      iCheckDetailsPageDriver.clickBatchCompleteAllTasks();

      setCompletionDateAndAuthenticate();
   }


   @Then( "^the tasks are completed$" )
   public void verifySelectedTasksAreBatchCompleted() throws Throwable {

      // Check that the tasks have been completed
      assertTrue( iCheckDetailsPageDriver.isReadyToCloseBannerVisible() );
   }


   private void setCompletionDateAndAuthenticate() throws ParseException {

      // Set a later than work package actual start date as the batch completion date to avoid the
      // BatchCompleteTasksCompletionDateValidator warning/error
      iCompleteTasksPageDriver
            .setCompleteDate( getCompleteDateLaterThanWorkPackageActualStartDate().toUpperCase() );
      // Accept the batch completion date
      iCompleteTasksPageDriver.clickOkButton();

      // Authorize the action
      iRequestAuthorizationPageDriver.setPassword( LINETECH_PASSWORD );
      iRequestAuthorizationPageDriver.authenticate();
   }


   private void addTasksToWorkPackage( String aAircraftName, String aWorkPackageName,
         String... aTasks ) {
      for ( String lTask : aTasks ) {
         iSchedTaskQueriesDriver.createTaskInWorkPackage( aAircraftName, lTask, aWorkPackageName );
      }
   }


   private String getCompleteDateLaterThanWorkPackageActualStartDate() throws ParseException {
      // The work package actual start date will set to today's date but the schedule end date is
      // 1/5/2017 which will become the default complete date when setting up the work package data
      // by using actuals loader (C_WORK_PACKAGE.csv)
      Date lToday = new Date();
      Calendar lCalendar = Calendar.getInstance();
      lCalendar.setTime( lToday );
      lCalendar.add( Calendar.DATE, 1 );

      SimpleDateFormat lDateFormat = new SimpleDateFormat( "dd-MMM-yyyy" );
      return lDateFormat.format( lCalendar.getTime() );
   }

}
