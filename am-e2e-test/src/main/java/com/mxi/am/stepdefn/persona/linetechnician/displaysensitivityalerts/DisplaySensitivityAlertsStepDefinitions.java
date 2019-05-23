package com.mxi.am.stepdefn.persona.linetechnician.displaysensitivityalerts;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;

import com.mxi.am.driver.query.AircraftQueriesDriver;
import com.mxi.am.driver.query.FaultKey;
import com.mxi.am.driver.query.FaultQueriesDriver;
import com.mxi.am.driver.query.PartQueriesDriver;
import com.mxi.am.driver.query.SensitivityQueriesDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.fault.RaiseFaultPageDriver;
import com.mxi.am.driver.web.inventory.InventorySelectionPageDriver;
import com.mxi.am.driver.web.inventory.inventorydetailspage.InventoryDetailsPageDriver;
import com.mxi.am.driver.web.part.AddPartRequirementSearchPageDriver;
import com.mxi.am.driver.web.task.TaskDetails.TaskDetailsPageDriver;
import com.mxi.am.stepdefn.persona.linetechnician.displaysensitivityalerts.data.PartGroupDetailsSensitivityAlertsScenarioData;
import com.mxi.am.stepdefn.persona.linetechnician.displaysensitivityalerts.data.RaiseFaultSensitivityAlertsScenarioData;
import com.mxi.am.stepdefn.persona.linetechnician.displaysensitivityalerts.data.TaskDetailsPartGroupSensitivityScenarioData;
import com.mxi.am.stepdefn.persona.linetechnician.displaysensitivityalerts.data.TaskDetailsSensitivityChipsScenarioData;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class DisplaySensitivityAlertsStepDefinitions {

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private AircraftQueriesDriver iAircraftQueriesDriver;

   @Inject
   private InventoryDetailsPageDriver iInventoryDetailsPageDriver;

   @Inject
   private RaiseFaultPageDriver iRaiseFaultPageDriver;

   @Inject
   private InventorySelectionPageDriver iInventorySelectionPageDriver;

   @Inject
   private SensitivityQueriesDriver iSensitivityQueriesDriver;

   @Inject
   private TaskDetailsPageDriver iTaskDetailsPageDriver;

   @Inject
   private FaultQueriesDriver iFaultQueriesDriver;

   @Inject
   private AddPartRequirementSearchPageDriver iAddPartRequirementSearchPageDriver;

   @Inject
   private PartQueriesDriver iPartQueriesDriver;


   @Before( "@SensitivityAlertsData" )
   public void createSensitivityAlertsData() {

      iSensitivityQueriesDriver
            .insertSensitivities( RaiseFaultSensitivityAlertsScenarioData.ACTIVE_SENSITIVITIES );

      iSensitivityQueriesDriver
            .activateSensitivities( RaiseFaultSensitivityAlertsScenarioData.ACTIVE_SENSITIVITIES );

      iSensitivityQueriesDriver.assignAssemblySensitivities(
            RaiseFaultSensitivityAlertsScenarioData.ASSMBL_DB_ID,
            RaiseFaultSensitivityAlertsScenarioData.AIRCRAFT_ASSY_CD,
            RaiseFaultSensitivityAlertsScenarioData.ACTIVE_SENSITIVITIES );

      iSensitivityQueriesDriver.enableSystemSensitivities(
            RaiseFaultSensitivityAlertsScenarioData.ASSMBL_DB_ID,
            RaiseFaultSensitivityAlertsScenarioData.AIRCRAFT_ASSY_CD,
            RaiseFaultSensitivityAlertsScenarioData.AIRCRAFT_FAILED_SYSTEM_NAME,
            RaiseFaultSensitivityAlertsScenarioData.ACTIVE_SENSITIVITIES );
   }


   @After( "@SensitivityAlertsData" )
   public void removeSensitivityAlertsData() {
      iSensitivityQueriesDriver.unassignAssembySensitivities(
            RaiseFaultSensitivityAlertsScenarioData.ASSMBL_DB_ID,
            RaiseFaultSensitivityAlertsScenarioData.AIRCRAFT_ASSY_CD,
            RaiseFaultSensitivityAlertsScenarioData.ACTIVE_SENSITIVITIES );

      iSensitivityQueriesDriver.disableSystemSensitivities(
            RaiseFaultSensitivityAlertsScenarioData.ASSMBL_DB_ID,
            RaiseFaultSensitivityAlertsScenarioData.AIRCRAFT_ASSY_CD,
            RaiseFaultSensitivityAlertsScenarioData.AIRCRAFT_FAILED_SYSTEM_NAME,
            RaiseFaultSensitivityAlertsScenarioData.ACTIVE_SENSITIVITIES );

      iSensitivityQueriesDriver
            .deleteSensitivities( RaiseFaultSensitivityAlertsScenarioData.ACTIVE_SENSITIVITIES );
   }


   @When( "^I raise a fault against a failed system with enabled system sensitivities$" )
   public void iRaiseAFaultAgainstAFailedSystemWithEnabledSystemSensitivities() {

      String lAircraftBarcode = iAircraftQueriesDriver.getAircraftInfoByPartAndSerialNo(
            RaiseFaultSensitivityAlertsScenarioData.AIRCRAFT_PART_NO,
            RaiseFaultSensitivityAlertsScenarioData.AIRCRAFT_SERIAL_NO ).getBarcode();

      iNavigationDriver.barcodeSearch( lAircraftBarcode );

      iInventoryDetailsPageDriver.clickTabOpen().clickTabOpenFaults()
            .clickRaiseLogbookFaultButton();

      iRaiseFaultPageDriver.clickSelectFailedSystem();

      iInventorySelectionPageDriver.selectInventoryFromTree(
            RaiseFaultSensitivityAlertsScenarioData.AIRCRAFT_FAILED_SYSTEM_NAME );
   }


   @Then( "^I see sensitivity alert warnings which are enabled on the failed system$" )
   public void iSeeSensitivityAlertMessageEnabledOnTheFailedSystem() {

      assertEquals(
            iSensitivityQueriesDriver.getSensitivitiesWarningLdesc(
                  RaiseFaultSensitivityAlertsScenarioData.ACTIVE_SENSITIVITIES ),
            iRaiseFaultPageDriver.getSensitivityAlertMessages() );
   }


   @Before( "@SensitivityChipsData" )
   public void createSensitivityChipsData() {

      iFaultQueriesDriver
            .updateFaultToOpenStatus( TaskDetailsSensitivityChipsScenarioData.FAULT_NAME );

      iSensitivityQueriesDriver
            .insertSensitivities( TaskDetailsSensitivityChipsScenarioData.ACTIVE_SENSITIVITIES );

      iSensitivityQueriesDriver
            .activateSensitivities( TaskDetailsSensitivityChipsScenarioData.ACTIVE_SENSITIVITIES );

      iSensitivityQueriesDriver.assignAssemblySensitivities(
            TaskDetailsSensitivityChipsScenarioData.ASSMBL_DB_ID,
            TaskDetailsSensitivityChipsScenarioData.AIRCRAFT_ASSY_CD,
            TaskDetailsSensitivityChipsScenarioData.ACTIVE_SENSITIVITIES );

      iSensitivityQueriesDriver.enableSystemSensitivities(
            TaskDetailsSensitivityChipsScenarioData.ASSMBL_DB_ID,
            TaskDetailsSensitivityChipsScenarioData.AIRCRAFT_ASSY_CD,
            TaskDetailsSensitivityChipsScenarioData.AIRCRAFT_FAILED_SYSTEM_NAME,
            TaskDetailsSensitivityChipsScenarioData.ACTIVE_SENSITIVITIES );
   }


   @After( "@SensitivityChipsData" )
   public void removeSensitivityChipsData() {
      iSensitivityQueriesDriver.unassignAssembySensitivities(
            TaskDetailsSensitivityChipsScenarioData.ASSMBL_DB_ID,
            TaskDetailsSensitivityChipsScenarioData.AIRCRAFT_ASSY_CD,
            TaskDetailsSensitivityChipsScenarioData.ACTIVE_SENSITIVITIES );

      iSensitivityQueriesDriver.disableSystemSensitivities(
            TaskDetailsSensitivityChipsScenarioData.ASSMBL_DB_ID,
            TaskDetailsSensitivityChipsScenarioData.AIRCRAFT_ASSY_CD,
            TaskDetailsSensitivityChipsScenarioData.AIRCRAFT_FAILED_SYSTEM_NAME,
            TaskDetailsSensitivityChipsScenarioData.ACTIVE_SENSITIVITIES );

      iSensitivityQueriesDriver
            .deleteSensitivities( TaskDetailsSensitivityChipsScenarioData.ACTIVE_SENSITIVITIES );
   }


   @When( "^I view the fault details for a failed system with enabled system sensitivities$" )
   public void iViewATaskWithEnabledSensitivities() {
      FaultKey lFaultKey = iFaultQueriesDriver.getByOpenFaultNameAndSerialNo(
            TaskDetailsSensitivityChipsScenarioData.AIRCRAFT_SERIAL_NO,
            TaskDetailsSensitivityChipsScenarioData.FAULT_NAME );
      iNavigationDriver.barcodeSearch( lFaultKey.getBarcode() );
   }


   @Then( "^I see sensitivity chips indicating the sensitivities for the fault's failed system$" )
   public void iSeeSensitivityNotificationsWhichAreApplicableForTheFailedSystemOfTheTask() {
      assertEquals(
            iSensitivityQueriesDriver.getSensitivitiesSdesc(
                  TaskDetailsSensitivityChipsScenarioData.ACTIVE_SENSITIVITIES ),
            iTaskDetailsPageDriver.getSensitivityChipsLabels() );
      assertEquals(
            iSensitivityQueriesDriver.getSensitivitiesWarningLdesc(
                  TaskDetailsSensitivityChipsScenarioData.ACTIVE_SENSITIVITIES ),
            iTaskDetailsPageDriver.getSensitivityChipsWarningMessages() );

   }


   @Before( "@PartGroupSensitivitityChipsData" )
   public void createPartGroupSensitivityChipsData() {

      iFaultQueriesDriver
            .updateFaultToOpenStatus( TaskDetailsPartGroupSensitivityScenarioData.FAULT_NAME );

      iSensitivityQueriesDriver.insertSensitivities(
            TaskDetailsPartGroupSensitivityScenarioData.ACTIVE_SENSITIVITIES );

      iSensitivityQueriesDriver.activateSensitivities(
            TaskDetailsPartGroupSensitivityScenarioData.ACTIVE_SENSITIVITIES );

      iSensitivityQueriesDriver.assignAssemblySensitivities(
            TaskDetailsPartGroupSensitivityScenarioData.ASSMBL_DB_ID,
            TaskDetailsPartGroupSensitivityScenarioData.AIRCRAFT_ASSEMBLY_CODE,
            TaskDetailsPartGroupSensitivityScenarioData.ACTIVE_SENSITIVITIES );

      iSensitivityQueriesDriver.enablePartGroupSensitivities(
            TaskDetailsPartGroupSensitivityScenarioData.ASSMBL_DB_ID,
            TaskDetailsPartGroupSensitivityScenarioData.PART_GROUP_CODE,
            TaskDetailsPartGroupSensitivityScenarioData.PART_GROUP_NAME,
            TaskDetailsPartGroupSensitivityScenarioData.ACTIVE_SENSITIVITIES );
   }


   @After( "@PartGroupSensitivitityChipsData" )
   public void removePartGroupSensitivityChipsData() {

      iSensitivityQueriesDriver.disablePartGroupSensitivities(
            TaskDetailsPartGroupSensitivityScenarioData.ASSMBL_DB_ID,
            TaskDetailsPartGroupSensitivityScenarioData.PART_GROUP_CODE,
            TaskDetailsPartGroupSensitivityScenarioData.PART_GROUP_NAME,
            TaskDetailsPartGroupSensitivityScenarioData.ACTIVE_SENSITIVITIES );

      iSensitivityQueriesDriver.unassignAssembySensitivities(
            TaskDetailsPartGroupSensitivityScenarioData.ASSMBL_DB_ID,
            TaskDetailsPartGroupSensitivityScenarioData.AIRCRAFT_ASSEMBLY_CODE,
            TaskDetailsPartGroupSensitivityScenarioData.ACTIVE_SENSITIVITIES );

      iSensitivityQueriesDriver.deleteSensitivities(
            TaskDetailsPartGroupSensitivityScenarioData.ACTIVE_SENSITIVITIES );
   }


   @When( "^I view the fault details which has a part requirement from a part group with enabled sensitivities$" )
   public void iViewAFaultWithEnabledPartGroupSensitivities() {
      FaultKey lFaultKey = iFaultQueriesDriver.getByOpenFaultNameAndSerialNo(
            TaskDetailsPartGroupSensitivityScenarioData.AIRCRAFT_SERIAL_NO,
            TaskDetailsPartGroupSensitivityScenarioData.FAULT_NAME );
      iNavigationDriver.barcodeSearch( lFaultKey.getBarcode() );
   }


   @Then( "^I see sensitivity chips indicating the sensitivities for the part group$" )
   public void iSeeSensitivityChipsWhichAreEnabledForThePartGroup() {
      assertEquals(
            iSensitivityQueriesDriver.getSensitivitiesSdesc(
                  TaskDetailsPartGroupSensitivityScenarioData.ACTIVE_SENSITIVITIES ),
            iTaskDetailsPageDriver.getSensitivityChipsLabels() );
      assertEquals(
            iSensitivityQueriesDriver.getSensitivitiesWarningLdesc(
                  TaskDetailsPartGroupSensitivityScenarioData.ACTIVE_SENSITIVITIES ),
            iTaskDetailsPageDriver.getSensitivityChipsWarningMessages() );
   }


   @Before( "@AdvancedPartSearchSensitivityChipsData" )
   public void createAdvancedPartSearchAlertsData() {
      iFaultQueriesDriver
            .updateFaultToOpenStatus( PartGroupDetailsSensitivityAlertsScenarioData.FAULT_NAME );

      iSensitivityQueriesDriver.insertSensitivities(
            PartGroupDetailsSensitivityAlertsScenarioData.ACTIVE_SENSITIVITIES );

      iSensitivityQueriesDriver.activateSensitivities(
            PartGroupDetailsSensitivityAlertsScenarioData.ACTIVE_SENSITIVITIES );

      iSensitivityQueriesDriver.assignAssemblySensitivities(
            PartGroupDetailsSensitivityAlertsScenarioData.ASSMBL_DB_ID,
            PartGroupDetailsSensitivityAlertsScenarioData.AIRCRAFT_ASSEMBLY_CODE,
            PartGroupDetailsSensitivityAlertsScenarioData.ACTIVE_SENSITIVITIES );

      iSensitivityQueriesDriver.enablePartGroupSensitivities(
            PartGroupDetailsSensitivityAlertsScenarioData.ASSMBL_DB_ID,
            PartGroupDetailsSensitivityAlertsScenarioData.PART_GROUP_CODE,
            PartGroupDetailsSensitivityAlertsScenarioData.PART_GROUP_NAME,
            PartGroupDetailsSensitivityAlertsScenarioData.ACTIVE_SENSITIVITIES );
   }


   @After( "@AdvancedPartSearchSensitivityChipsData" )
   public void removeAdvancedPartSearchAlertsData() {

      iSensitivityQueriesDriver.disablePartGroupSensitivities(
            PartGroupDetailsSensitivityAlertsScenarioData.ASSMBL_DB_ID,
            PartGroupDetailsSensitivityAlertsScenarioData.PART_GROUP_CODE,
            PartGroupDetailsSensitivityAlertsScenarioData.PART_GROUP_NAME,
            PartGroupDetailsSensitivityAlertsScenarioData.ACTIVE_SENSITIVITIES );

      iSensitivityQueriesDriver.unassignAssembySensitivities(
            PartGroupDetailsSensitivityAlertsScenarioData.ASSMBL_DB_ID,
            PartGroupDetailsSensitivityAlertsScenarioData.AIRCRAFT_ASSEMBLY_CODE,
            PartGroupDetailsSensitivityAlertsScenarioData.ACTIVE_SENSITIVITIES );

      iSensitivityQueriesDriver.deleteSensitivities(
            PartGroupDetailsSensitivityAlertsScenarioData.ACTIVE_SENSITIVITIES );
   }


   @When( "^I choose to add part requirements to a fault$" )
   public void iChooseToAddPartRequirementsToAFault() {
      FaultKey lFaultKey = iFaultQueriesDriver.getByOpenFaultNameAndSerialNo(
            PartGroupDetailsSensitivityAlertsScenarioData.AIRCRAFT_SERIAL_NO,
            PartGroupDetailsSensitivityAlertsScenarioData.FAULT_NAME );
      iNavigationDriver.barcodeSearch( lFaultKey.getBarcode() );

      iTaskDetailsPageDriver.clickTabTaskExecution().clickAddPartRequirementSearch();
   }


   @When( "^I select a part which belongs to a part group with sensitivities$" )
   public void iSelectAPartWhichBelongsToAPartGroupWithSensitivities() {
      iAddPartRequirementSearchPageDriver
            .searchForPart( PartGroupDetailsSensitivityAlertsScenarioData.PART_NO );
      String lPartUniqueKey =
            iPartQueriesDriver
                  .getByPartNo( PartGroupDetailsSensitivityAlertsScenarioData.PART_NO,
                        PartGroupDetailsSensitivityAlertsScenarioData.PART_GROUP_CODE )
                  .getUniqueKey();
      iAddPartRequirementSearchPageDriver.selectPartWithId( lPartUniqueKey );
   }


   @Then( "^I see sensitivity chips indicating the part group sensitivities$" )
   public void iSeeSensitivityWarningsIndicatingTheSensitivitiesForThePartGroup() {
      assertEquals(
            iSensitivityQueriesDriver.getSensitivitiesSdesc(
                  PartGroupDetailsSensitivityAlertsScenarioData.ACTIVE_SENSITIVITIES ),
            iAddPartRequirementSearchPageDriver.getSensitivityChipsLabels() );
      assertEquals(
            iSensitivityQueriesDriver.getSensitivitiesWarningLdesc(
                  PartGroupDetailsSensitivityAlertsScenarioData.ACTIVE_SENSITIVITIES ),
            iAddPartRequirementSearchPageDriver.getSensitivityChipsWarningMessages() );
   }

}
