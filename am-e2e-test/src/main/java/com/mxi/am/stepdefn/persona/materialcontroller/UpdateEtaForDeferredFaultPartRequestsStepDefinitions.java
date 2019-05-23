package com.mxi.am.stepdefn.persona.materialcontroller;

import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;

import com.mxi.am.driver.web.AssetManagement;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.common.jobviewer.JobViewerPageDriver;
import com.mxi.am.driver.web.todolist.ToDoListPageDriver;
import com.mxi.am.driver.web.todolist.ToDoListPanes.DeferredFaultPartRequestsPaneDriver.DeferredFaultPartRequestsTable;
import com.mxi.driver.standard.Wait;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class UpdateEtaForDeferredFaultPartRequestsStepDefinitions {

   @Inject
   @AssetManagement
   private WebDriver iWebDriver;

   private static final String DEFERRED_FAULT_PART_REQ_JOB = "MX_CORE_MVIEW_DEFER_FAULT_PART_REQ";
   private static final String ADMINISTRATOR = "Administrator";
   private static final String JOB_VIEWER = "Job Viewer";
   private static final String MATERIAL_CONTROLLER = "Material Controller";
   private static final String MATERIAL_CONTROLLER_TODO_LIST =
         "Control To Do List (Material Controller)";
   private static final String UPDATED_ETA = "01-APR-2017";
   private static final String DELIVERY_NOTE = "ETA was updated to 01-APR-2017 by automated tests";
   private static final String ETA_CLEARED = "ETA was cleared by automated tests";
   private static final int TIMEOUT_IN_MILLISECONDS = 3 * 60 * 1000;
   private static final int THREE_SECONDS = 3 * 1000;
   private final String EMPTY_REQUET_TAB = "There are no part requests.";

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private JobViewerPageDriver iJobViewerPageDriver;

   @Inject
   private ToDoListPageDriver iToDoListPageDriver;


   private void refreshJobs() {
      // navigate to Administrator > job viewer
      iNavigationDriver.navigate( ADMINISTRATOR, JOB_VIEWER );
      // run the job to refresh materialized view
      iJobViewerPageDriver.clickRunJob( DEFERRED_FAULT_PART_REQ_JOB );
   }


   @Given( "^I go to deferred fault part requests$" )
   public void iGoToDeferredFaultPartRequests() throws Throwable {
      // Before going, refresh the materialized views
      refreshJobs();
      // navigate to material controller todo list
      iNavigationDriver.navigate( MATERIAL_CONTROLLER, MATERIAL_CONTROLLER_TODO_LIST );
      // click on deferred fault part requests tab
      iToDoListPageDriver.clickTabDeferredFaultPartRequests();
   }


   @Given( "^I select multiple fault part requests$" )
   public void iSelectMultipleFaultPartRequests() throws Throwable {

      // refresh the to do list page to get deferred fault part requests appear
      long lTimeout = System.currentTimeMillis() + TIMEOUT_IN_MILLISECONDS;
      do {
         // Pause, reload the page, and check again (if not timed out).
         Wait.pause( THREE_SECONDS );
         iNavigationDriver.refreshPage();

         if ( !iWebDriver.getPageSource().contains( EMPTY_REQUET_TAB ) ) {
            break;
         }

      } while ( System.currentTimeMillis() < lTimeout );

      if ( System.currentTimeMillis() >= lTimeout ) {
         Assert.fail(
               "Timed out waiting for Deferred Fault Part Requests to be shown up on the to do list. Waited "
                     + TIMEOUT_IN_MILLISECONDS + " milliseconds." );

      }
      // select multiple rows
      List<DeferredFaultPartRequestsTable> lDeferredFaultPartRequests =
            iToDoListPageDriver.getTabDeferredFaultPartRequests().getRequestIds();
      lDeferredFaultPartRequests.get( 0 ).clickCheckBox();
      lDeferredFaultPartRequests.get( 1 ).clickCheckBox();
      lDeferredFaultPartRequests.get( 2 ).clickCheckBox();
   }


   @When( "^I click Update ETA$" )
   public void iClickUpdateETA() throws Throwable {
      // click update delivery status button and go to dialog box
      iToDoListPageDriver.getTabDeferredFaultPartRequests().clickUpdateDeliveryStatusButton();
   }


   @When( "^I update ETA and Delivery Notes$" )
   public void iUpdateETAAndNotes() throws Throwable {
      // set updated ETA
      iToDoListPageDriver.getTabDeferredFaultPartRequests().updateETAandNotes( UPDATED_ETA,
            DELIVERY_NOTE );

      // wait for the page refresh
      Wait.pause( 2000 );
   }


   @Then( "^the part requests have newly added ETA and notes$" )
   public void thePartRequestsHaveNewlyAddedETAAndNotes() throws Throwable {
      // get request rows and check whether they have updated ETA and notes
      List<DeferredFaultPartRequestsTable> lDeferredFaultPartRequests =
            iToDoListPageDriver.getTabDeferredFaultPartRequests().getRequestIds();
      for ( DeferredFaultPartRequestsTable lrow : lDeferredFaultPartRequests ) {
         Assert.assertTrue(
               "Updated ETA expected to contain ".concat( UPDATED_ETA ).concat( " but was " )
                     .concat( lrow.getUpdatedEta() ),
               lrow.getUpdatedEta().contains( UPDATED_ETA ) );
         Assert.assertEquals( "Delivery Note expected to contain ".concat( DELIVERY_NOTE )
               .concat( " but was " ).concat( lrow.getDeliveryNote() ), DELIVERY_NOTE,
               lrow.getDeliveryNote() );
      }
   }


   @Given( "^I select one fault part request$" )
   public void iSelectOneFaultPartRequest() throws Throwable {
      List<DeferredFaultPartRequestsTable> lDeferredFaultPartRequests =
            iToDoListPageDriver.getTabDeferredFaultPartRequests().getRequestIds();
      lDeferredFaultPartRequests.get( 1 ).clickCheckBox();

   }


   @When( "^I clear ETA$" )
   public void iClearETAAndClickOK() throws Throwable {
      // clear the ETA
      iToDoListPageDriver.getTabDeferredFaultPartRequests().clearETA( ETA_CLEARED );

      // wait for the page refresh
      Wait.pause( 2000 );
   }


   @Then( "^the part request loses ETA$" )
   public void thePartRequestLosesETA() throws Throwable {
      // get request rows and check whether the ETA has been cleared
      List<DeferredFaultPartRequestsTable> lDeferredFaultPartRequests =
            iToDoListPageDriver.getTabDeferredFaultPartRequests().getRequestIds();
      Assert.assertTrue( lDeferredFaultPartRequests.get( 1 ).getUpdatedEta().isEmpty() );
      Assert.assertEquals( ETA_CLEARED, lDeferredFaultPartRequests.get( 1 ).getDeliveryNote() );
   }
}
