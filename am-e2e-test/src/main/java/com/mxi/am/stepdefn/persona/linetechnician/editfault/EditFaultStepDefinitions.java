package com.mxi.am.stepdefn.persona.linetechnician.editfault;

import static org.junit.Assert.assertTrue;

import com.google.inject.Inject;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.fault.RaiseFaultPageDriver;
import com.mxi.am.driver.web.task.TaskDetails.TaskDetailsPageDriver;
import com.mxi.am.driver.web.task.TaskDetails.TaskDetailsPanes.TaskInformationPaneDriver;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class EditFaultStepDefinitions {

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private TaskDetailsPageDriver iTaskDetailsPageDriver;

   @Inject
   private RaiseFaultPageDriver iRaiseFaultPageDriver;

   private static final String TASK_BARCODE = "OPER1509BARCODE";
   private static final String ZONE = "ZONEA";


   @When( "^I edit an open fault by adding a zone$" )
   public void editAnOpenFaultByAddingAZone() throws Exception {

      iNavigationDriver.barcodeSearch( TASK_BARCODE );

      iTaskDetailsPageDriver.clickTabFaultInformation().clickEditFaultInformationButton();

      iRaiseFaultPageDriver.clickZone( ZONE );

      iRaiseFaultPageDriver.clickOk();
   }


   @Then( "^I see the zone was added to the corrective task without any associated panels$" )
   public void checkZoneIsAddedWithoutPanels() {

      TaskInformationPaneDriver lTaskInformationPaneDriver =
            iTaskDetailsPageDriver.clickTabTaskInformation();

      assertTrue( "A zone should be added to the fault",
            !lTaskInformationPaneDriver.getZones().isEmpty() );

      assertTrue( "Panels should not be synchronized with the added zone",
            !lTaskInformationPaneDriver.isPanelsTableExist() );

   }
}
