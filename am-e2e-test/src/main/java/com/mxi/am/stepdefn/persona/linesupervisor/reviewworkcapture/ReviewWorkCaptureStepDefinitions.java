package com.mxi.am.stepdefn.persona.linesupervisor.reviewworkcapture;

import java.math.BigDecimal;

import javax.inject.Inject;

import org.junit.Assert;

import com.mxi.am.driver.query.AircraftQueriesDriver;
import com.mxi.am.driver.query.AircraftQueriesDriver.AircraftInfo;
import com.mxi.am.driver.query.AssemblyQueriesDriver;
import com.mxi.am.driver.query.FaultQueriesDriver;
import com.mxi.am.driver.query.FaultQueriesDriver.FaultInfo;
import com.mxi.am.driver.query.InventoryQueriesDriver;
import com.mxi.am.driver.query.SchedLabourQueriesDriver;
import com.mxi.am.driver.query.WorkPackageKey;
import com.mxi.am.driver.query.WorkPackageQueriesDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.task.TaskDetails.TaskDetailsPageDriver;
import com.mxi.am.driver.web.task.TaskDetails.TaskDetailsPanes.TaskExecutionPaneDriver;
import com.mxi.am.driver.web.task.labor.ReviewWorkCapturePageDriver;
import com.mxi.am.stepdefn.persona.linesupervisor.reviewworkcapture.data.ReviewWorkCaptureScenarioData;
import com.mxi.mx.core.key.RefEventStatusKey;

import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class ReviewWorkCaptureStepDefinitions {

   @Inject
   private NavigationDriver iNavigationDriver;

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
   private AssemblyQueriesDriver iAssemblyQueriesDriver;

   @Inject
   private SchedLabourQueriesDriver iSchedLabourQueriesDriver;

   @Inject
   private ReviewWorkCapturePageDriver iReviewWorkCapturePageDriver;

   private static String sWorkCaptureScenarioFaultBarcode;


   @When( "^I review work captured by a line technician$" )
   public void iReviewWorkCapturedByALineTechnician() throws Throwable {
      // Navigate to the task
      iNavigationDriver.barcodeSearch( sWorkCaptureScenarioFaultBarcode );

      // Click Job Stop for the labor row
      TaskExecutionPaneDriver lTaskExecutionPaneDriver =
            iTaskDetailsPageDriver.clickTabTaskExecution();
      lTaskExecutionPaneDriver.getLaborRows().get( 0 ).clickLabourRow();
      lTaskExecutionPaneDriver.clickReviewWorkCaptured();
   }


   @Then( "^the resolution config slot information is visible$" )
   public void iCanSeeTheResolutionConfigSlotWhenIReviewWorkCaptured() throws Throwable {
      String lViewResolutionConfigSlot =
            iReviewWorkCapturePageDriver.getFaultInformationResolutionConfigSlot();
      Assert.assertEquals( ReviewWorkCaptureScenarioData.RESOLUTION_CONFIG_SLOT_LABEL,
            lViewResolutionConfigSlot );
   }


   @Before( "@ReviewWorkCaptureResolutionConfigSlot" )
   public void setupDataForReviewWorkCaptureScenario() {

      // grab aircraft info
      AircraftInfo lAcInvNoId = iAircraftQueriesDriver.getAircraftInfoByPartAndSerialNo(
            ReviewWorkCaptureScenarioData.AIRCRAFT_PART_NO,
            ReviewWorkCaptureScenarioData.AIRCRAFT_SERIAL_NO );

      // grab failed system info
      BigDecimal lFailedSysInvNoId =
            iInventoryQueriesDriver.getInvInfoByAircraftInvNoAndInvName( lAcInvNoId.getInvNoId(),
                  ReviewWorkCaptureScenarioData.FAILED_SYSTEM_SDESC ).getId();

      // get work package info and set it to in-work
      WorkPackageKey lWorkPackageKey = iWorkPackageQueriesDriver
            .getByWorkPackageName( ReviewWorkCaptureScenarioData.WORK_PACKAGE_NAME );

      BigDecimal lWorkPackageLineId =
            iWorkPackageQueriesDriver.getWorkPackageLineId( lWorkPackageKey.getId() );

      iWorkPackageQueriesDriver.setWorkPackageStatus( lWorkPackageKey.getDbId(),
            RefEventStatusKey.IN_WORK.getCd(), ReviewWorkCaptureScenarioData.WORK_PACKAGE_NAME );

      // create fault
      FaultInfo lWorkCaptureFault = iFaultQueriesDriver.insertFault(
            ReviewWorkCaptureScenarioData.FAULT_NAME_REVIEW_WORK_CAPTURE,
            ReviewWorkCaptureScenarioData.FAULT_NAME_REVIEW_WORK_CAPTURE,
            ReviewWorkCaptureScenarioData.FAULT_SOURCE, lAcInvNoId.getInvNoId(),
            ReviewWorkCaptureScenarioData.FOUND_BY_USER, ReviewWorkCaptureScenarioData.FAULT_STATUS,
            ReviewWorkCaptureScenarioData.AIRCRAFT_ASSEMBLY_CD, lFailedSysInvNoId );

      // grab fault barcode
      sWorkCaptureScenarioFaultBarcode = lWorkCaptureFault.getCorrectiveTaskBarcode();

      // attach fault to work package
      iFaultQueriesDriver.attachFaultToWorkPackage( lWorkPackageKey.getId(), lWorkPackageLineId,
            ReviewWorkCaptureScenarioData.FAULT_NAME_REVIEW_WORK_CAPTURE );

      // create labour row
      iSchedLabourQueriesDriver.createLaborRow( sWorkCaptureScenarioFaultBarcode, "ACTV", "AMT",
            "TECH", "COMPLETE" );

      BigDecimal lFaultId = iFaultQueriesDriver
            .getByOpenFaultNameAndSerialNo( ReviewWorkCaptureScenarioData.AIRCRAFT_SERIAL_NO,
                  ReviewWorkCaptureScenarioData.FAULT_NAME_REVIEW_WORK_CAPTURE )
            .getId();

      BigDecimal lResolutionConfigSlotBomId = iAssemblyQueriesDriver.getBomIdByAssemblyBomCode(
            ReviewWorkCaptureScenarioData.RESOLUTION_CONFIG_SLOT_ATA_CODE );

      // update the fault to have a resolution config slot
      iFaultQueriesDriver.updateFaultResolutionConfigSlot(
            ReviewWorkCaptureScenarioData.AIRCRAFT_ASSEMBLY_CD, lResolutionConfigSlotBomId,
            lFaultId );
   }
}
