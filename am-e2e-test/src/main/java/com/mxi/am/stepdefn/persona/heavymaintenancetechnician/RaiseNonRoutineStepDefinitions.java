package com.mxi.am.stepdefn.persona.heavymaintenancetechnician;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;

import com.mxi.am.driver.query.SchedTaskQueriesDriver;
import com.mxi.am.driver.query.WorkPackageQueriesDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.PleaseWaitPaneDriver;
import com.mxi.am.driver.web.fault.RaiseFaultPageDriver;
import com.mxi.am.driver.web.inventory.InventorySelectionPageDriver;
import com.mxi.am.driver.web.task.CheckDetails.CheckDetailsPageDriver;
import com.mxi.am.driver.web.task.TaskDetails.TaskDetailsPageDriver;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class RaiseNonRoutineStepDefinitions {

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private CheckDetailsPageDriver iCheckDetailsPageDriver;

   @Inject
   private TaskDetailsPageDriver iTaskDetailsPageDriver;

   @Inject
   private RaiseFaultPageDriver iRaiseFaultPageDriver;

   @Inject
   private InventorySelectionPageDriver iInventorySelectionPageDriver;

   @Inject
   private PleaseWaitPaneDriver iPleaseWaitPaneDriver;

   @Inject
   private WorkPackageQueriesDriver iWorkPackageQueriesDriver;

   @Inject
   private SchedTaskQueriesDriver iSchedTaskQueriesDriver;

   public static final int MAX_WAIT_TIME_IN_MS = 5 * 60 * 1000;
   public static final String AIRCRAFT_SN = "20989-01";
   public static final String WORK_PACKAGE = "WP20989-01";
   public static final String TASK_NAME = "BM_SCH_TSK11";
   public static final String FAULT_NAME_1 = "F209891";
   public static final String FAULT_NAME_2 = "F209892";
   public static final String FAILED_SYSTEM_1 = "SYS-1 - Aircraft System 1";
   public static final String FAILED_SYSTEM_2 = "SYS-2 - Aircraft System 2";
   public static final String FAULT_SOURCE = "MECH (Mechanic)";
   public static final String FAULT_SEVERITY = "AOG (Aircraft is on Ground)";
   public static final String FAULT_STATUS = "OPEN (Active)";


   @Given( "^I have an In Work Work Package$" )
   public void iHaveAnInWorkWorkPackage() throws Throwable {
      iNavigationDriver.barcodeSearch(
            iWorkPackageQueriesDriver.getByWorkPackageName( WORK_PACKAGE ).getBarcode() );
   }


   @When( "^I raise a Logbook Fault$" )
   public void iRaiseALogbookFault() throws Throwable {
      iCheckDetailsPageDriver.clickTabWorkscope();
      iCheckDetailsPageDriver.getTabWorkscope().clickRaiseLogbookFault();
      iRaiseFaultPageDriver.setFaultName( FAULT_NAME_1 );
      iRaiseFaultPageDriver.selectFaultSource( FAULT_SOURCE );
      iRaiseFaultPageDriver.setFaultSeverity( FAULT_SEVERITY );
      iRaiseFaultPageDriver.clickSelectFailedSystem();
      iInventorySelectionPageDriver.selectInventoryFromTree( FAILED_SYSTEM_1 );
      iRaiseFaultPageDriver.clickOk();
      iPleaseWaitPaneDriver.waitForPleaseWaitToClose( MAX_WAIT_TIME_IN_MS );
   }


   @Then( "^the Fault is created$" )
   public void theFaultIsCreated() throws Throwable {
      iTaskDetailsPageDriver.clickTabFaultInformation();
      assertEquals( FAULT_STATUS,
            iTaskDetailsPageDriver.getTabFaultInformation().getFaultStatus() );;
   }


   @Given( "^I have an In Work Task$" )
   public void iHaveAnInWorkTask() throws Throwable {
      iNavigationDriver.barcodeSearch( iSchedTaskQueriesDriver
            .getByAircraftSerNoAndTaskCode( AIRCRAFT_SN, TASK_NAME ).getBarcode() );
   }


   @When( "^I raise a Fault$" )
   public void iRaiseAFault() throws Throwable {
      iTaskDetailsPageDriver.clickFoundFaults();
      iRaiseFaultPageDriver.setFaultName( FAULT_NAME_2 );
      iRaiseFaultPageDriver.selectFaultSource( FAULT_SOURCE );
      iRaiseFaultPageDriver.setFaultSeverity( FAULT_SEVERITY );
      iRaiseFaultPageDriver.clickSelectFailedSystem();
      iInventorySelectionPageDriver.selectInventoryFromTree( FAILED_SYSTEM_2 );
      iRaiseFaultPageDriver.clickOk();
      iPleaseWaitPaneDriver.waitForPleaseWaitToClose( MAX_WAIT_TIME_IN_MS );
   }


   @Then( "^the Fault is raised$" )
   public void theFaultIsRaised() throws Throwable {
      iTaskDetailsPageDriver.clickTabFaultInformation();
      assertEquals( FAULT_STATUS,
            iTaskDetailsPageDriver.getTabFaultInformation().getFaultStatus() );;
   }
}
