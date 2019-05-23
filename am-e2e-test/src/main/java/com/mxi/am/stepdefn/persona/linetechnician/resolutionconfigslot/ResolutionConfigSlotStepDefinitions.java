package com.mxi.am.stepdefn.persona.linetechnician.resolutionconfigslot;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Assert;

import com.mxi.am.driver.query.AircraftQueriesDriver;
import com.mxi.am.driver.query.AircraftQueriesDriver.AircraftInfo;
import com.mxi.am.driver.query.FaultQueriesDriver;
import com.mxi.am.driver.query.FaultQueriesDriver.FaultInfo;
import com.mxi.am.driver.query.InventoryQueriesDriver;
import com.mxi.am.driver.query.SchedLabourQueriesDriver;
import com.mxi.am.driver.query.WorkPackageKey;
import com.mxi.am.driver.query.WorkPackageQueriesDriver;
import com.mxi.am.driver.web.AuthenticationRequiredPageDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.fault.RaiseFaultPageDriver;
import com.mxi.am.driver.web.inventory.InventorySelectionPageDriver;
import com.mxi.am.driver.web.task.TaskDetails.TaskDetailsPageDriver;
import com.mxi.am.driver.web.task.labor.EditWorkCapturePageDriver;
import com.mxi.am.driver.web.todolist.ToDoListPageDriver;
import com.mxi.am.stepdefn.persona.linetechnician.resolutionconfigslot.data.LogAndCloseFaultScenarioData;
import com.mxi.am.stepdefn.persona.linetechnician.resolutionconfigslot.data.WorkCaptureScenarioData;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class ResolutionConfigSlotStepDefinitions {

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private ToDoListPageDriver iToDoListPageDriver;

   @Inject
   private RaiseFaultPageDriver iRaiseFaultPageDriver;

   @Inject
   private InventorySelectionPageDriver iInventorySelectionPageDriver;

   @Inject
   private TaskDetailsPageDriver iTaskDetailsPageDriver;

   @Inject
   private WorkPackageQueriesDriver iWorkPackageQueriesDriver;

   @Inject
   private FaultQueriesDriver iFaultQueriesDriver;

   @Inject
   private AircraftQueriesDriver iAircraftQueriesDriver;

   @Inject
   private InventoryQueriesDriver iInventoryQueriesDriver;

   @Inject
   private SchedLabourQueriesDriver iSchedLabourQueriesDriver;

   @Inject
   private AuthenticationRequiredPageDriver iAuthenticationRequiredPageDriver;

   @Inject
   private EditWorkCapturePageDriver iEditWorkCapturePageDriver;

   private static String sWorkCaptureScenarioFaultBarcode;


   @Given( "^I want to record a fault in Maintenix$" )
   public void iWantToRecordAFaultInMaintenix() {

      iToDoListPageDriver.getTabFleetList()
            .clickRadioButtonForAircraft( LogAndCloseFaultScenarioData.AIRCRAFT_REG_CD );
      iToDoListPageDriver.getTabFleetList().clickRaiseLogbookFaultButton();
   }


   @When( "^I log and close the fault with a resolution config slot$" )
   public void iWantToLogAndCloseAFaultWithAResolutionConfigSlot() {

      iRaiseFaultPageDriver.clickLogFaultAndClose();
      iRaiseFaultPageDriver.setFaultName( LogAndCloseFaultScenarioData.FAULT_NAME );
      iRaiseFaultPageDriver.clickSelectFailedSystem();
      iInventorySelectionPageDriver
            .selectInventoryFromTree( LogAndCloseFaultScenarioData.FAILED_SYSTEM );
      iRaiseFaultPageDriver.setCorrectiveAction( LogAndCloseFaultScenarioData.CORRECTIVE_ACTION );
      iRaiseFaultPageDriver.setResolutionConfigSlot(
            LogAndCloseFaultScenarioData.RESOLUTION_CONFIG_SLOT_PARTIAL_STRING );

      // sorting the expected list as well as the list obtained from front-end,
      // so that the comparison is purely for the contents and not the order
      Collections.sort( LogAndCloseFaultScenarioData.EXPECTED_RESOLUTION_CONFIG_SLOT_SUGGESTIONS );
      List<String> lResolutionConfigSlotSuggestions =
            iRaiseFaultPageDriver.getResolutionConfigSlotSuggestions();
      Collections.sort( lResolutionConfigSlotSuggestions );
      Assert.assertEquals( LogAndCloseFaultScenarioData.EXPECTED_RESOLUTION_CONFIG_SLOT_SUGGESTIONS,
            lResolutionConfigSlotSuggestions );

      iRaiseFaultPageDriver
            .setResolutionConfigSlot( LogAndCloseFaultScenarioData.RESOLUTION_CONFIG_SLOT );
      iRaiseFaultPageDriver.clickOk();
   }


   @Then( "^the resolution config slot is recorded on the fault$" )
   public void verifyResolutionConfigSlotIsRecordedOnTheFault() {
      String lViewResolutionConfigSlot =
            iTaskDetailsPageDriver.clickTabFaultInformation().getResolutionConfigSlot();
      Assert.assertEquals( LogAndCloseFaultScenarioData.RESOLUTION_CONFIG_SLOT,
            lViewResolutionConfigSlot );
   }


   @When( "^I set the resolution config slot during edit work capture on a fault's corrective task$" )
   public void iSetTheResolutionConfigSlotDuringEditWorkCaptureOnTheFaultSCorrectiveTask()
         throws Throwable {
      // Navigate to the task
      iNavigationDriver.barcodeSearch( sWorkCaptureScenarioFaultBarcode );

      // Click Job Stop for the labor row
      iTaskDetailsPageDriver.clickTabTaskExecution().getLaborRows().get( 0 ).clickJobStop();

      // Enter password on the popup
      iAuthenticationRequiredPageDriver.setPassword_JobStop( "password" );
      iAuthenticationRequiredPageDriver.clickOk();

      // Set the fields in the form
      iEditWorkCapturePageDriver
            .setResolutionConfigSlot( WorkCaptureScenarioData.RESOLUTION_CONFIG_SLOT_LABEL );
      iEditWorkCapturePageDriver.setCorrectiveAction( WorkCaptureScenarioData.CORRECTIVE_ACTION );

      // Start date has to be after the Work Package start date - hence adding 1 day
      Date lStartDateObject = DateUtils.addDays( new Date(), 1 );
      String lStartDate = new SimpleDateFormat( "dd-MMM-yyyy" ).format( lStartDateObject );
      iEditWorkCapturePageDriver.setStartDate( lStartDate );

      // End date has to be after the start date - hence adding 2 days
      Date lEndDateObject = DateUtils.addDays( new Date(), 2 );
      String lEndDate = new SimpleDateFormat( "dd-MMM-yyyy" ).format( lEndDateObject );
      iEditWorkCapturePageDriver.setEndDate( lEndDate );

      iEditWorkCapturePageDriver.clickOk();

   }


   @Before( "@WorkCaptureResolutionConfigSlot" )
   public void setupDataForWorkCaptureScenario() {

      // grab aircraft info
      AircraftInfo lAcInvNoId = iAircraftQueriesDriver.getAircraftInfoByPartAndSerialNo(
            WorkCaptureScenarioData.AIRCRAFT_PART_NO, WorkCaptureScenarioData.AIRCRAFT_SERIAL_NO );

      // grab failed system info
      BigDecimal lFailedSysInvNoId =
            iInventoryQueriesDriver.getInvInfoByAircraftInvNoAndInvName( lAcInvNoId.getInvNoId(),
                  WorkCaptureScenarioData.FAILED_SYSTEM_SDESC ).getId();

      // get work package info and set it to in-work
      WorkPackageKey lWorkPackageKey = iWorkPackageQueriesDriver
            .getByWorkPackageName( WorkCaptureScenarioData.WORK_PACKAGE_NAME );

      BigDecimal lWorkPackageLineId =
            iWorkPackageQueriesDriver.getWorkPackageLineId( lWorkPackageKey.getId() );

      iWorkPackageQueriesDriver.setWorkPackageStatus( lWorkPackageKey.getDbId(),
            WorkCaptureScenarioData.WORK_PACKAGE_STATUS,
            WorkCaptureScenarioData.WORK_PACKAGE_NAME );

      // create fault
      FaultInfo lWorkCaptureFault =
            iFaultQueriesDriver.insertFault( WorkCaptureScenarioData.FAULT_NAME_EDIT_WORK_CAPTURE,
                  WorkCaptureScenarioData.FAULT_NAME_EDIT_WORK_CAPTURE,
                  WorkCaptureScenarioData.FAULT_SOURCE, lAcInvNoId.getInvNoId(),
                  WorkCaptureScenarioData.FOUND_BY_USER, WorkCaptureScenarioData.FAULT_STATUS,
                  WorkCaptureScenarioData.AIRCRAFT_ASSEMBLY_CD, lFailedSysInvNoId );

      // grab fault barcode
      sWorkCaptureScenarioFaultBarcode = lWorkCaptureFault.getCorrectiveTaskBarcode();

      // attach fault to work package
      iFaultQueriesDriver.attachFaultToWorkPackage( lWorkPackageKey.getId(), lWorkPackageLineId,
            WorkCaptureScenarioData.FAULT_NAME_EDIT_WORK_CAPTURE );

      // create labour row
      iSchedLabourQueriesDriver.createLaborRow( sWorkCaptureScenarioFaultBarcode, "ACTV", "AMT",
            "TECH", "ACTV" );
   }


   @After( "@EnsureTheRecurringFaultsPageIsNotTriggered" )
   public void ensureTheRecurringFaultsPageIsNotTriggered() {

      // set found on date to more than 10 days ago,
      // so it doesn't get chosen for displaying in the recurring faults
      iFaultQueriesDriver.updateFaultFoundOnDate( LogAndCloseFaultScenarioData.FAULT_NAME,
            DateUtils.addDays( new Date(), -15 ) );

      // certify the fault, to make sure it doesn't show
      iFaultQueriesDriver.updateFaultToCompletedStatus( LogAndCloseFaultScenarioData.FAULT_NAME,
            DateUtils.addDays( new Date(), -13 ) );
   }
}
