package com.mxi.am.stepdefn.persona.linetechnician.selectreference;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.Assert;

import com.google.inject.Inject;
import com.mxi.am.driver.query.AircraftQueriesDriver;
import com.mxi.am.driver.query.AircraftQueriesDriver.AircraftInfo;
import com.mxi.am.driver.query.FaultQueriesDriver;
import com.mxi.am.driver.query.FaultQueriesDriver.FaultInfo;
import com.mxi.am.driver.query.InventoryQueriesDriver;
import com.mxi.am.driver.query.WorkPackageKey;
import com.mxi.am.driver.query.WorkPackageQueriesDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.lmoc.requestdeferral.SelectReferencePageDriver;
import com.mxi.am.driver.web.task.TaskDetails.TaskDetailsPageDriver;
import com.mxi.am.driver.web.task.TaskDetails.TaskDetailsPanes.FaultInformationPaneDriver;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;


@ScenarioScoped
public class SelectDeferralReferenceStepDefinitions {

   public static final String AC_REG_CD = "Aircraft Part MOC 1 - RTD-1";
   public static final String AIRCRAFT_ASSEMBLY_CD = "ACFTMOC1";
   public static final String AIRCRAFT_PART_NO = "ACFT_ASSY_MOC1";
   public static final String AIRCRAFT_SERIAL_NO = "RTD-1";

   public static final String FAULT_NAME = UUID.randomUUID().toString();
   public static final String FAILED_SYSTEM_NAME = "SYS-1 - Aircraft System 1";
   public static final String FAULT_DESC = UUID.randomUUID().toString();
   public static final String FAULT_SOURCE = "MECH";
   public static final String FAULT_SOURCE_DESC = "Mechanic";
   public static final String FAULT_STATUS = "OPEN (Active)";
   public static final String FAULT_STATUS_CD = "CFACTV";
   public static final String USERNAME = "mxi";
   public static final String WORK_PACKAGE_NAME = "RTD-WP";

   public static final String NOTES = "RTD-DEFER-1 Deferral Notes";
   public static final String REASON = "NA (Not Applicable)";
   public static final String REFERENCE_FILTER_TEXT = "RTD";
   public static final String REFERENCE_NAME = "RTD-DEFER-1-DEF";

   public static final String CONFIRMATION_DIALOG_TITLE = "Submit Deferral Request";
   public static final String SUCCESS_DIALOG_TITLE = "Success";
   public static final String SUCCESS_DIALOG_MESSAGE =
         "This deferral request has been submitted for review.";

   @Inject
   private NavigationDriver navigationDriver;
   @Inject
   private SelectReferencePageDriver selectReferencePageDriver;
   @Inject
   private TaskDetailsPageDriver taskDetailsPageDriver;
   @Inject
   private WorkPackageQueriesDriver workPackageQueriesDriver;
   @Inject
   private FaultQueriesDriver faultQueriesDriver;
   @Inject
   private AircraftQueriesDriver aircraftQueriesDriver;
   @Inject
   private InventoryQueriesDriver inventoryQueriesDriver;

   private FaultInfo faultInfo = null;


   @Given( "^there is an open fault" )
   public void thereIsAnOpenFault() {

      faultInfo = setUpFault();
   }


   @When( "^I select a deferral reference" )
   public void iSelectADeferralReference() {

      // navigate to the fault details page
      navigationDriver.barcodeSearch( faultInfo.getCorrectiveTaskBarcode() );

      // click the Select Reference Button
      taskDetailsPageDriver.clickSelectReference();

      // fill in the Select Reference page
      selectReferencePageDriver.selectReference( REFERENCE_FILTER_TEXT, REFERENCE_NAME );
      selectReferencePageDriver.enterNotes( NOTES ).selectReason( REASON );

      selectReferencePageDriver.clickOk();

   }


   @Then( "^the deferral reference is selected" )
   public void theDeferralReferenceIsSelected() {

      // click on fault information
      FaultInformationPaneDriver faultInformationPaneDriver =
            taskDetailsPageDriver.clickTabFaultInformation();

      // check the select reference band has been updated
      Assert.assertFalse( "Selected Reference Band not updated on Fault Information Tab",
            faultInformationPaneDriver.getSelectedReferenceBandDetails().isEmpty() );

   }


   private FaultInfo setUpFault() {

      AircraftInfo acInvNoId = aircraftQueriesDriver
            .getAircraftInfoByPartAndSerialNo( AIRCRAFT_PART_NO, AIRCRAFT_SERIAL_NO );

      BigDecimal failedSysInvNoId = inventoryQueriesDriver
            .getInvInfoByAircraftInvNoAndInvName( acInvNoId.getInvNoId(), FAILED_SYSTEM_NAME )
            .getId();

      WorkPackageKey workPackageKey =
            workPackageQueriesDriver.getByWorkPackageName( WORK_PACKAGE_NAME );

      BigDecimal workPackageLineId =
            workPackageQueriesDriver.getWorkPackageLineId( workPackageKey.getId() );

      FaultInfo faultInfo = faultQueriesDriver.insertFault( FAULT_NAME, FAULT_DESC, FAULT_SOURCE,
            acInvNoId.getInvNoId(), USERNAME, FAULT_STATUS_CD, AIRCRAFT_ASSEMBLY_CD,
            failedSysInvNoId );

      faultQueriesDriver.attachFaultToWorkPackage( workPackageKey.getId(), workPackageLineId,
            FAULT_NAME );

      return faultInfo;
   }

}
