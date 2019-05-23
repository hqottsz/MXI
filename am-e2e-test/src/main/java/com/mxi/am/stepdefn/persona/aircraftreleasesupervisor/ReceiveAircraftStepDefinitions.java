package com.mxi.am.stepdefn.persona.aircraftreleasesupervisor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.mxi.am.driver.query.WorkPackageQueriesDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.PleaseWaitPaneDriver;
import com.mxi.am.driver.web.common.CheckSelectionPageDriver;
import com.mxi.am.driver.web.common.CheckSelectionPageDriver.AssignFaultToWorkPackageTableRow;
import com.mxi.am.driver.web.fault.RaiseFaultPageDriver;
import com.mxi.am.driver.web.inventory.InventorySelectionPageDriver;
import com.mxi.am.driver.web.inventory.inventorydetailspage.InventoryDetailsPageDriver;
import com.mxi.am.driver.web.inventory.inventorydetailspage.inventorydetailspanes.openpanes.OpenFaultsPaneDriver.OpenFaultsTableRow;
import com.mxi.am.driver.web.inventory.inventorydetailspage.inventorydetailspanes.openpanes.OpenWorkPackagesPaneDriver.OpenWorkPackagesTableRow;
import com.mxi.am.driver.web.task.CheckDetails.CheckDetailsPageDriver;
import com.mxi.am.driver.web.task.TaskDetails.TaskDetailsPageDriver;
import com.mxi.am.driver.web.todolist.ToDoListPageDriver;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class ReceiveAircraftStepDefinitions {

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private ToDoListPageDriver iToDoListPageDriver;

   @Inject
   private InventoryDetailsPageDriver iInventoryDetailsPageDriver;

   @Inject
   private CheckDetailsPageDriver iCheckDetailsPageDriver;

   @Inject
   private PleaseWaitPaneDriver iPleaseWaitPaneDriver;

   @Inject
   private RaiseFaultPageDriver iRaiseFaultPageDriver;

   @Inject
   private TaskDetailsPageDriver iTaskDetailsPageDriver;

   @Inject
   private CheckSelectionPageDriver iCheckSelectionPageDriver;

   @Inject
   private InventorySelectionPageDriver iInventorySelectionPageDriver;

   @Inject
   private WorkPackageQueriesDriver iWorkPackageQueriesDriver;

   private static final int OPENFAULT_QTY = 1;
   private static final int MAX_WAIT_TIME_IN_MS = 5 * 60 * 1000;

   private static final String TO_DO_LIST = "To Do List (Heavy Planner)";
   private static final String HEAVY_PLANNER = "Heavy Planner";
   private static final String AIRCRAFT_WITH_CURRENTUSAGE = "Aircraft Part 2 - 100";
   private static final String AIRCRAFT_WITH_OPENFAULT = "Aircraft Part BM 1 - 20985-02";
   private static final String AIRCRAFT_WITH_OPEN_WORKPACKAGE = "Aircraft Part 2 - 100";
   private static final String OPENFAULT_NAME = "F20985-2";
   private static final String WORKPACKAGE_NAME = "Test Work Package";
   private static final String WORKPACKAGE_IN_WORK_STATUS = "IN WORK";
   private static final String LOGBOOK_FAULT = "FAULT-OPER-20985";
   private static final String LOGBOOK_REFERENCE = "LOGBOOK-REF-OPER-20985";
   private static final String FAILED_SYSTEM = "SYS-ENG - Aircraft Enigne System";
   private static final String FOUND_ON_DATE = "13-APR-2018";
   private static final String FAULT_SOURCE = "PILOT (Pilot )";
   private static final String FAULT_SEVERITY = "AOG (Aircraft is on Ground)";
   private static final String USAGE_TSN_HOURS = "1000";
   private static final String USAGE_TSN_CYCLES = "1000";

   private static List<CurrentUsage> iCurrentUsageList;
   private static List<OpenFaultsTableRow> iOpenFaultsTableRows;
   private static List<OpenWorkPackagesTableRow> iOpenWorkPackagesTableRows;
   private static List<AssignFaultToWorkPackageTableRow> iAssignFaultToWorkPackageTableRows;


   @Given( "^I have an Aircraft$" )
   public void iHaveAnAircraft() throws Throwable {
      iNavigationDriver.navigate( HEAVY_PLANNER, TO_DO_LIST );
   }


   @And( "^the Aircraft has Usage$" )
   public void theAircraftHasUsage() throws Throwable {
      iCurrentUsageList = new ArrayList<CurrentUsage>();
      iCurrentUsageList.add( new CurrentUsage( "CYCLES", 1000, 1000, 1000 ) );
      iCurrentUsageList.add( new CurrentUsage( "HOURS", 1000, 1000, 1000 ) );
      iCurrentUsageList.add( new CurrentUsage( "USAGE1", 1000, 1000, 1000 ) );
      iCurrentUsageList.add( new CurrentUsage( "USAGE2", 1000, 1000, 1000 ) );
      iCurrentUsageList.add( new CurrentUsage( "USAGE3", 1000, 1000, 1000 ) );
   }


   @When( "^I review the Current Usage$" )
   public void iReviewTheCurrentUsage() throws Throwable {
      iToDoListPageDriver.clickTabFleetList().clickAircraftInTable( AIRCRAFT_WITH_CURRENTUSAGE );
      iInventoryDetailsPageDriver.clickTabDetails();
   }


   @Then( "^I can see the Current Usage$" )
   public void iCanSeeTheCurrentUsage() throws Throwable {
      for ( CurrentUsage lExpectCurrentUsage : iCurrentUsageList ) {
         int lActualTsn =
               getCurrentUsageTsn( AIRCRAFT_WITH_CURRENTUSAGE, lExpectCurrentUsage.iUsageparm );
         int lActualTso =
               getCurrentUsageTso( AIRCRAFT_WITH_CURRENTUSAGE, lExpectCurrentUsage.iUsageparm );
         int lActualTsi =
               getCurrentUsageTsi( AIRCRAFT_WITH_CURRENTUSAGE, lExpectCurrentUsage.iUsageparm );

         assertEquals( lExpectCurrentUsage.iTsn, lActualTsn );
         assertEquals( lExpectCurrentUsage.iTso, lActualTso );
         assertEquals( lExpectCurrentUsage.iTsi, lActualTsi );
      }
   }


   @Given( "^the Aircraft has a Committed Work Package$" )
   public void theAircraftHasACommittedWorkPackage() throws Throwable {
      iNavigationDriver.barcodeSearch(
            iWorkPackageQueriesDriver.getByWorkPackageName( WORKPACKAGE_NAME ).getBarcode() );
   }


   @When( "^I Start the Work Package$" )
   public void iStartTheWorkPackage() throws Throwable {
      iCheckDetailsPageDriver.clickStartHistoricWorkPackage();
      iCheckDetailsPageDriver.clickStartHistoricWorkPackageOk();
      iPleaseWaitPaneDriver.waitForPleaseWaitToClose( MAX_WAIT_TIME_IN_MS );
   }


   @Then( "^the Work Package is Started$" )
   public void theWorkPackageIsStarted() throws Throwable {
      iCheckDetailsPageDriver.clickTabDetails();
      String lStatus = iCheckDetailsPageDriver.getTabDetails().getStatus();
      assertEquals( WORKPACKAGE_IN_WORK_STATUS, lStatus );
   }


   @Given( "^the Aircraft has Open Faults$" )
   public void theAircraftHasOpenFaults() throws Throwable {
      iToDoListPageDriver.clickTabFleetList().clickAircraftInTable( AIRCRAFT_WITH_OPENFAULT );
      iInventoryDetailsPageDriver.clickTabOpen().clickTabOpenFaults();
   }


   @When( "^I review the list of Open Faults$" )
   public void iReviewTheListOfOpenFaults() throws Throwable {
      iOpenFaultsTableRows =
            iInventoryDetailsPageDriver.getTabOpen().getTabOpenFaults().getOpenFaultsTableRows();
   }


   @Then( "^I can see the Open Faults$" )
   public void iCanSeeTheOpenFaults() throws Throwable {
      int lOpenFaults = iOpenFaultsTableRows.size();
      assertEquals( OPENFAULT_QTY, lOpenFaults );

      String lFaultName = iOpenFaultsTableRows.get( 0 ).getName();
      assertEquals( OPENFAULT_NAME, lFaultName );
   }


   @Given( "^the Aircraft has an Open Work Package$" )
   public void theAircraftHasAnOpenWorkPackage() throws Throwable {
      iToDoListPageDriver.clickTabFleetList()
            .clickAircraftInTable( AIRCRAFT_WITH_OPEN_WORKPACKAGE );
      iInventoryDetailsPageDriver.clickTabOpen().clickTabOpenWorkPackages();
      iOpenWorkPackagesTableRows = iInventoryDetailsPageDriver.getTabOpen().getTabOpenWorkPackages()
            .getOpenWorkPackagesTableRows();

      boolean lFound = false;
      for ( OpenWorkPackagesTableRow lRow : iOpenWorkPackagesTableRows ) {
         if ( lRow.getName().equalsIgnoreCase( WORKPACKAGE_NAME ) ) {
            lFound = true;
            break;
         }
      }
      assertTrue( "Open work package (" + WORKPACKAGE_NAME + ") is not found!", lFound );
   }


   @When( "^I Raise a Logbook Fault$" )
   public void iRaiseALogbookFault() throws Throwable {
      iInventoryDetailsPageDriver.getTabOpen().clickTabOpenFaults();
      iInventoryDetailsPageDriver.getTabOpen().getTabOpenFaults().clickRaiseLogbookFaultButton();
      iRaiseFaultPageDriver.clickLogFaultAndLeaveOpen();
      iRaiseFaultPageDriver.setFaultName( LOGBOOK_FAULT );
      iRaiseFaultPageDriver.setLogbookReference( LOGBOOK_REFERENCE );
      iRaiseFaultPageDriver.clickSelectFailedSystem();
      iInventorySelectionPageDriver.selectInventoryFromTree( FAILED_SYSTEM );
      iRaiseFaultPageDriver.setFoundOnDate( FOUND_ON_DATE );
      iRaiseFaultPageDriver.setUsageTsnHours( USAGE_TSN_HOURS );
      iRaiseFaultPageDriver.setUsageTsnCycles( USAGE_TSN_CYCLES );
      iRaiseFaultPageDriver.selectFaultSource( FAULT_SOURCE );
      iRaiseFaultPageDriver.setFaultSeverity( FAULT_SEVERITY );
      iRaiseFaultPageDriver.clickOk();
      iPleaseWaitPaneDriver.waitForPleaseWaitToClose( MAX_WAIT_TIME_IN_MS );
   }


   @When( "^I Assign the Fault to a Work Package$" )
   public void iAssignTheFaultToAWorkPackage() throws Throwable {
      iTaskDetailsPageDriver.clickAssignFaultToWP();
      iAssignFaultToWorkPackageTableRows = iCheckSelectionPageDriver.getAssignedTaskTableRows();

      for ( AssignFaultToWorkPackageTableRow lRow : iAssignFaultToWorkPackageTableRows ) {
         if ( lRow.getWorkPackageName().equalsIgnoreCase( WORKPACKAGE_NAME ) ) {
            lRow.clickRadio();
            break;
         }
      }
   }


   @Then( "^the Fault exists$" )
   public void theFaultExists() throws Throwable {
      String lTaskDetails = iNavigationDriver.getSubTitle();
      String lFaultName = lTaskDetails.substring( 0, lTaskDetails.indexOf( "[" ) - 1 );
      assertEquals( "The fault does NOT exist!", LOGBOOK_FAULT, lFaultName );
   }


   @Then( "^the Fault is Assigned to the Work Package$" )
   public void theFaultIsAssignedToTheWorkPackage() throws Throwable {
      String lWorkPackageInfo = iTaskDetailsPageDriver.getWorkPackageInformation();
      if ( lWorkPackageInfo.isEmpty() ) {
         fail( "The fault is not assigned to a work package!" );
      } else {
         String lWorkPackage = lWorkPackageInfo.substring( 0, lWorkPackageInfo.indexOf( "[" ) - 1 );
         assertEquals( "The fault is assigned to a wrong work package!", WORKPACKAGE_NAME,
               lWorkPackage );
      }
   }


   private int getCurrentUsageTsn( String aAssembly, String aUsageParam ) {
      String lTsn = iInventoryDetailsPageDriver.getTabDetails().getAssemblyCurrentTsn( aAssembly,
            aUsageParam );
      return Integer.parseInt( lTsn );
   }


   private int getCurrentUsageTso( String aAssembly, String aUsageParam ) {
      String lTso = iInventoryDetailsPageDriver.getTabDetails().getAssemblyCurrentTso( aAssembly,
            aUsageParam );
      return Integer.parseInt( lTso );
   }


   private int getCurrentUsageTsi( String aAssembly, String aUsageParam ) {
      String lTsi = iInventoryDetailsPageDriver.getTabDetails().getAssemblyCurrentTsi( aAssembly,
            aUsageParam );
      return Integer.parseInt( lTsi );
   }


   public class CurrentUsage {

      public String iUsageparm;
      public int iTsn;
      public int iTso;
      public int iTsi;


      public CurrentUsage(String aUsageParm, int aTsn, int aTso, int aTsi) {
         iUsageparm = aUsageParm;
         iTsn = aTsn;
         iTso = aTso;
         iTsi = aTsi;
      }
   }

}
