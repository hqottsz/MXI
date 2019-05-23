package com.mxi.am.stepdefn.persona.linetechnician.selectreference;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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
import com.mxi.am.driver.web.task.TaskDetails.TaskDetailsPanes.TaskExecutionPaneDriver;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;


@ScenarioScoped
public class SelectRepairReferenceStepDefinitions {

   public static final String AC_REG_CD = "Aircraft Part 2 - SREF-1";
   public static final String AIRCRAFT_ASSEMBLY_CD = "ACFT_CD1";
   public static final String AIRCRAFT_PART_NO = "ACFT_ASSY_PN2";
   public static final String AIRCRAFT_SERIAL_NO = "SREF-1";

   public static final String FAULT_NAME = UUID.randomUUID().toString();
   public static final String FAILED_SYSTEM_NAME = "SYS-1 - Aircraft System 1";
   public static final String FAULT_DESC = UUID.randomUUID().toString();
   public static final String FAULT_SOURCE = "MECH";
   public static final String FAULT_SOURCE_DESC = "Mechanic";
   public static final String FAULT_STATUS = "OPEN (Active)";
   public static final String FAULT_STATUS_CD = "CFACTV";
   public static final String USERNAME = "mxi";
   public static final String WORK_PACKAGE_NAME = "SREF-WP";

   public static final String REFERENCE_FILTER_TEXT = "REP";
   public static final String REFERENCE_NAME = "REPREF1 (REPREF1)";
   public static final String SKILL1 = "AMT";
   public static final String SKILL2 = "ENG";
   public static final int NUMBER_OF_SKILLS = 2;
   public static final String STEP1_DESC = "STEP 1";
   public static final String STEP2_DESC = "STEP 2";

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


   @Given( "^there exists an open fault" )
   public void thereExistsAnOpenFault() {

      faultInfo = setUpFault();
   }


   @When( "^I select a repair reference" )
   public void iSelectARepairReference() {

      // navigate to the fault details page
      navigationDriver.barcodeSearch( faultInfo.getCorrectiveTaskBarcode() );

      // click the Select Reference Button
      taskDetailsPageDriver.clickSelectReference();

      // fill in the Select Reference page
      selectReferencePageDriver.selectReference( REFERENCE_FILTER_TEXT, REFERENCE_NAME );

      selectReferencePageDriver.clickOk();

   }


   @Then( "^the repair reference gets linked" )
   public void theRepairReferenceGetsLinked() {

      // go to execution tab
      TaskExecutionPaneDriver faultExecutionPaneDriver =
            taskDetailsPageDriver.clickTabTaskExecution();

      // check the labors
      Assert.assertNotNull( "AMT was not found in the labour",
            faultExecutionPaneDriver.getLaborRow( SKILL1 ) );
      Assert.assertNotNull( "ENG was not found in the labour",
            faultExecutionPaneDriver.getLaborRow( SKILL2 ) );
      Assert.assertEquals( "Labor row count mismatch", NUMBER_OF_SKILLS,
            faultExecutionPaneDriver.getLaborRows().size() );

      // check the steps
      List<String> descriptions = new ArrayList<String>();
      descriptions.add( STEP1_DESC );
      descriptions.add( STEP2_DESC );
      Assert.assertEquals( "Steps were not copied", descriptions,
            faultExecutionPaneDriver.getStepDescriptions() );

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
