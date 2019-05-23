package com.mxi.am.stepdefn.persona.maintenancecontroller.pendingdeferralrequests;

import java.math.BigDecimal;

import org.junit.Assert;

import com.google.inject.Inject;
import com.mxi.am.driver.common.database.DatabaseDriver;
import com.mxi.am.driver.query.AircraftQueriesDriver;
import com.mxi.am.driver.query.AircraftQueriesDriver.AircraftInfo;
import com.mxi.am.driver.query.FaultKey;
import com.mxi.am.driver.query.FaultQueriesDriver;
import com.mxi.am.driver.query.InventoryQueriesDriver;
import com.mxi.am.driver.query.WorkPackageKey;
import com.mxi.am.driver.query.WorkPackageQueriesDriver;
import com.mxi.am.driver.web.AssetManagement;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.lmoc.pendingdeferralrequests.PendingReferenceApprovalsPageDriver;
import com.mxi.am.driver.web.task.TaskDetails.TaskDetailsPageDriver;
import com.mxi.am.driver.web.task.TaskDetails.TaskDetailsPanes.FaultInformationPaneDriver;
import com.mxi.am.stepdefn.persona.maintenancecontroller.pendingdeferralrequests.data.PendingReferenceApprovalsScenarioData;

import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class PendingReferenceApprovalsStepDefinitions {

   @Inject
   @AssetManagement
   private DatabaseDriver iDatabaseDriver;

   @Inject
   private PendingReferenceApprovalsPageDriver pendingReferenceApprovalsDriver;

   @Inject
   private WorkPackageQueriesDriver workPackageQueriesDriver;

   @Inject
   private FaultQueriesDriver faultQueriesDriver;

   @Inject
   private AircraftQueriesDriver aircraftQueriesDriver;

   @Inject
   private InventoryQueriesDriver inventoryQueriesDriver;

   @Inject
   private NavigationDriver navigationDriver;

   @Inject
   private TaskDetailsPageDriver taskDetailsPageDriver;


   @When( "^I authorize a pending request$" )
   public void iAuthorizeAPendingReferenceRequests() {

      pendingReferenceApprovalsDriver.navigateTo();

      FaultKey faultKey = faultQueriesDriver.getByOpenFaultNameAndSerialNo(
            PendingReferenceApprovalsScenarioData.AIRCRAFT_SERIAL_NO,
            PendingReferenceApprovalsScenarioData.FAULT_NAME );

      pendingReferenceApprovalsDriver.selectRequest( faultKey.getBarcode() );
      pendingReferenceApprovalsDriver.clickAuthorizeButton();
      pendingReferenceApprovalsDriver
            .confirmDialogAction( PendingReferenceApprovalsScenarioData.CONFIRMATION_DIALOG_TITLE );
   }


   @Then( "^the pending request should be authorized successfully$" )
   public void verifySuccessfulAuthorization() {

      Assert.assertTrue( "Missing Authorization Code on Success message dialog",
            pendingReferenceApprovalsDriver.getAuthorizeSuccessDialogContent() );

      pendingReferenceApprovalsDriver
            .confirmDialogAction( PendingReferenceApprovalsScenarioData.SUCCESS_DIALOG_TITLE );

      navigationDriver.clickHomeLmocWeb();
      FaultKey faultKey = faultQueriesDriver.getByDeferredFaultNameAndSerialNo(
            PendingReferenceApprovalsScenarioData.AIRCRAFT_SERIAL_NO,
            PendingReferenceApprovalsScenarioData.FAULT_NAME );
      navigationDriver.barcodeSearch( faultKey.getBarcode() );
      FaultInformationPaneDriver faultInformationPaneDriver =
            taskDetailsPageDriver.clickTabFaultInformation();

      Assert.assertFalse( "Authorization code not updated",
            faultInformationPaneDriver.getDeferralAuthorization().isEmpty() );

   }


   @Before( "@CreatePendingReferenceRequest" )
   public void setUp() {

      AircraftInfo lAcInvNoId = aircraftQueriesDriver.getAircraftInfoByPartAndSerialNo(
            PendingReferenceApprovalsScenarioData.AIRCRAFT_PART_NO,
            PendingReferenceApprovalsScenarioData.AIRCRAFT_SERIAL_NO );

      BigDecimal lFailedSysInvNoId =
            inventoryQueriesDriver.getInvInfoByAircraftInvNoAndInvName( lAcInvNoId.getInvNoId(),
                  PendingReferenceApprovalsScenarioData.FAILED_SYSTEM_NAME ).getId();

      WorkPackageKey lWorkPackageKey = workPackageQueriesDriver
            .getByWorkPackageName( PendingReferenceApprovalsScenarioData.WORK_PACKAGE_NAME );

      BigDecimal lWorkPackageLineId =
            workPackageQueriesDriver.getWorkPackageLineId( lWorkPackageKey.getId() );

      workPackageQueriesDriver.setWorkPackageStatus( lWorkPackageKey.getDbId(),
            PendingReferenceApprovalsScenarioData.WORK_PACKAGE_STATUS,
            PendingReferenceApprovalsScenarioData.WORK_PACKAGE_NAME );

      faultQueriesDriver.insertFault( PendingReferenceApprovalsScenarioData.FAULT_NAME,
            PendingReferenceApprovalsScenarioData.FAULT_DESC,
            PendingReferenceApprovalsScenarioData.FAULT_SOURCE, lAcInvNoId.getInvNoId(),
            PendingReferenceApprovalsScenarioData.USERNAME,
            PendingReferenceApprovalsScenarioData.FAULT_STATUS_CD,
            PendingReferenceApprovalsScenarioData.AIRCRAFT_ASSEMBLY_CD, lFailedSysInvNoId );

      faultQueriesDriver.attachFaultToWorkPackage( lWorkPackageKey.getId(), lWorkPackageLineId,
            PendingReferenceApprovalsScenarioData.FAULT_NAME );

      faultQueriesDriver.createDeferralRequest( PendingReferenceApprovalsScenarioData.FAULT_NAME,
            PendingReferenceApprovalsScenarioData.DEFERRAL_REFERENCE_NAME,
            PendingReferenceApprovalsScenarioData.DEFERRAL_NOTES );

   }
}
