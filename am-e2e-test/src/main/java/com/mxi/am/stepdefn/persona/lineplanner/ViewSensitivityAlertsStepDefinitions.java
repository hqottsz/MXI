package com.mxi.am.stepdefn.persona.lineplanner;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.List;

import com.google.inject.Inject;
import com.mxi.am.driver.query.AircraftQueriesDriver;
import com.mxi.am.driver.query.AircraftQueriesDriver.AircraftInfo;
import com.mxi.am.driver.query.FaultQueriesDriver;
import com.mxi.am.driver.query.InventoryQueriesDriver;
import com.mxi.am.driver.query.SensitivityQueriesDriver;
import com.mxi.am.driver.query.WorkPackageKey;
import com.mxi.am.driver.query.WorkPackageQueriesDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.task.CheckDetails.CheckDetailsPageDriver;
import com.mxi.am.stepdefn.persona.lineplanner.data.ViewSensitivityAlertsScenarioData;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class ViewSensitivityAlertsStepDefinitions {

   @Inject
   private WorkPackageQueriesDriver iWorkPackageQueriesDriver;

   @Inject
   private FaultQueriesDriver iFaultQueriesDriver;

   @Inject
   private AircraftQueriesDriver iAircraftQueriesDriver;

   @Inject
   private InventoryQueriesDriver iInventoryQueriesDriver;

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private SensitivityQueriesDriver iSensitivityQueriesDriver;

   @Inject
   private CheckDetailsPageDriver iCheckDetailsPageDriver;


   @Before( "@SensitivityWorkscopeData" )
   public void setupSensitivityWorkscopeData() {

      AircraftInfo lAcInvNoId = iAircraftQueriesDriver.getAircraftInfoByPartAndSerialNo(
            ViewSensitivityAlertsScenarioData.AIRCRAFT_PART_NO,
            ViewSensitivityAlertsScenarioData.AIRCRAFT_SERIAL_NO );

      BigDecimal lFailedSysInvNoId =
            iInventoryQueriesDriver.getInvInfoByAircraftInvNoAndInvName( lAcInvNoId.getInvNoId(),
                  ViewSensitivityAlertsScenarioData.AIRCRAFT_FAILED_SYSTEM_SDESC ).getId();

      WorkPackageKey lWorkPackageKey = iWorkPackageQueriesDriver
            .getByWorkPackageName( ViewSensitivityAlertsScenarioData.WORK_PACKAGE_NAME );

      BigDecimal lWorkPackageLineId =
            iWorkPackageQueriesDriver.getWorkPackageLineId( lWorkPackageKey.getId() );

      iWorkPackageQueriesDriver.setWorkPackageStatus( lWorkPackageKey.getDbId(),
            ViewSensitivityAlertsScenarioData.WORK_PACKAGE_STATUS,
            ViewSensitivityAlertsScenarioData.WORK_PACKAGE_NAME );

      iFaultQueriesDriver.insertFault( ViewSensitivityAlertsScenarioData.FAULT_NAME,
            ViewSensitivityAlertsScenarioData.FAULT_DESC,
            ViewSensitivityAlertsScenarioData.FAULT_SOURCE, lAcInvNoId.getInvNoId(),
            ViewSensitivityAlertsScenarioData.USERNAME,
            ViewSensitivityAlertsScenarioData.FAULT_STATUS,
            ViewSensitivityAlertsScenarioData.AIRCRAFT_ASSEMBLY_CD, lFailedSysInvNoId );

      iFaultQueriesDriver.attachFaultToWorkPackage( lWorkPackageKey.getId(), lWorkPackageLineId,
            ViewSensitivityAlertsScenarioData.FAULT_NAME );

      iSensitivityQueriesDriver
            .insertSensitivities( ViewSensitivityAlertsScenarioData.ACTIVE_SENSITIVITIES );

      iSensitivityQueriesDriver
            .activateSensitivities( ViewSensitivityAlertsScenarioData.ACTIVE_SENSITIVITIES );

      iSensitivityQueriesDriver.assignAssemblySensitivities(
            ViewSensitivityAlertsScenarioData.AIRCRAFT_ASSEMBLY_DB_ID,
            ViewSensitivityAlertsScenarioData.AIRCRAFT_ASSEMBLY_CD,
            ViewSensitivityAlertsScenarioData.ACTIVE_SENSITIVITIES );

      iSensitivityQueriesDriver.enableSystemSensitivities(
            ViewSensitivityAlertsScenarioData.AIRCRAFT_ASSEMBLY_DB_ID,
            ViewSensitivityAlertsScenarioData.AIRCRAFT_ASSEMBLY_CD,
            ViewSensitivityAlertsScenarioData.AIRCRAFT_FAILED_SYSTEM_NAME,
            ViewSensitivityAlertsScenarioData.ACTIVE_SENSITIVITIES );
   }


   @After( "@SensitivityWorkscopeData" )
   public void teardownSensitivityWorkscopeData() {
      iSensitivityQueriesDriver.unassignAssembySensitivities(
            ViewSensitivityAlertsScenarioData.AIRCRAFT_ASSEMBLY_DB_ID,
            ViewSensitivityAlertsScenarioData.AIRCRAFT_ASSEMBLY_CD,
            ViewSensitivityAlertsScenarioData.ACTIVE_SENSITIVITIES );

      iSensitivityQueriesDriver.disableSystemSensitivities(
            ViewSensitivityAlertsScenarioData.AIRCRAFT_ASSEMBLY_DB_ID,
            ViewSensitivityAlertsScenarioData.AIRCRAFT_ASSEMBLY_CD,
            ViewSensitivityAlertsScenarioData.AIRCRAFT_FAILED_SYSTEM_NAME,
            ViewSensitivityAlertsScenarioData.ACTIVE_SENSITIVITIES );

      iSensitivityQueriesDriver
            .deleteSensitivities( ViewSensitivityAlertsScenarioData.ACTIVE_SENSITIVITIES );
   }


   @When( "^I view the workscope of a work package which contains a fault on a failed system with sensitivities$" )
   public void iViewTheWorkscopeOfAFaultWithSensitiveSystems() {
      WorkPackageKey lWorkPackageKey = iWorkPackageQueriesDriver
            .getByWorkPackageName( ViewSensitivityAlertsScenarioData.WORK_PACKAGE_NAME );
      iNavigationDriver.barcodeSearch( lWorkPackageKey.getBarcode() );
   }


   @Then( "^I see sensitivity chips indicating the sensitivities for the fault's failed system on the workscope$" )
   public void iSeeSSPWorkscopeNotificationsWhichAreApplicableForTheFault() {

      List<String> lSensitivities = iSensitivityQueriesDriver
            .getSensitivitiesSdesc( ViewSensitivityAlertsScenarioData.ACTIVE_SENSITIVITIES );

      assertEquals( lSensitivities,
            iCheckDetailsPageDriver.clickTabWorkscope().getFaultOnWorkpackageSensitivityChips(
                  ViewSensitivityAlertsScenarioData.FAULT_NAME ) );

   }
}
