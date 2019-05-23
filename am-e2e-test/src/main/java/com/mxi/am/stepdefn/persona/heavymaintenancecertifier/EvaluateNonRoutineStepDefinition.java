package com.mxi.am.stepdefn.persona.heavymaintenancecertifier;

import static org.junit.Assert.assertTrue;

import javax.inject.Inject;

import com.mxi.am.driver.common.ConfirmPageDriver;
import com.mxi.am.driver.web.AuthenticationRequiredPageDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.PleaseWaitPaneDriver;
import com.mxi.am.driver.web.inventory.inventorydetailspage.InventoryDetailsPageDriver;
import com.mxi.am.driver.web.task.TaskDetails.TaskDetailsPageDriver;
import com.mxi.am.driver.web.todolist.ToDoListPageDriver;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class EvaluateNonRoutineStepDefinition {

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private ToDoListPageDriver iToDoListPageDriver;

   @Inject
   private InventoryDetailsPageDriver iInventoryDetailsPageDriver;

   @Inject
   private TaskDetailsPageDriver iTaskDetailsPageDriver;

   @Inject
   private AuthenticationRequiredPageDriver iAuthenticationRequiredPageDriver;

   @Inject
   private ConfirmPageDriver iConfirmPageDriver;

   @Inject
   private PleaseWaitPaneDriver iPleaseWaitPaneDriver;

   private static final int MAX_WAIT_TIME_IN_MS = 5 * 60 * 1000;
   private static final String PASSWORD = "password";
   private static final String PERSONA = "Maintenance Controller";
   private static final String TO_DO_LIST = "To Do List (Maintenance Controller)";
   private static final String AIRCRAFT = "Aircraft Part BM 10 - 20990-01";
   private static final String FAULT = "F20990-1";


   @Given( "^I have an Aircraft with an Open Fault$" )
   public void iHaveAnAircraft() throws Throwable {
      iNavigationDriver.navigate( PERSONA, TO_DO_LIST );
      iToDoListPageDriver.clickTabFleetList().clickAircraftInTable( AIRCRAFT );
      iInventoryDetailsPageDriver.clickTabOpen();
      iInventoryDetailsPageDriver.getTabOpen().clickTabOpenFaults();
      assertTrue( "Fault " + FAULT + " is NOT found in Open Faults Table!",
            iInventoryDetailsPageDriver.getTabOpen().getTabOpenFaults().hasFaultInTable( FAULT ) );
      iInventoryDetailsPageDriver.getTabOpen().getTabOpenFaults().clickFaultInTable( FAULT );
      iPleaseWaitPaneDriver.waitForPleaseWaitToClose( MAX_WAIT_TIME_IN_MS );
   }


   @When( "^I mark the Fault as Evaluated$" )
   public void iMarkTheFaultAsEvaluated() throws Throwable {
      iTaskDetailsPageDriver.clickFinishEvaluation();
      iAuthenticationRequiredPageDriver.setPasswordAndClickOK( PASSWORD );
      iConfirmPageDriver.clickYes();
      iPleaseWaitPaneDriver.waitForPleaseWaitToClose( MAX_WAIT_TIME_IN_MS );
   }


   @Then( "^the Fault is marked as being evaluated$" )
   public void theFaultIsMarkedAsBeingEvaluated() throws Throwable {
      iTaskDetailsPageDriver.clickTabFaultInformation();
      assertTrue( "Fault " + FAULT + " is NOT evaluated.",
            iTaskDetailsPageDriver.getTabFaultInformation().isEvaluated() );
   }
}
