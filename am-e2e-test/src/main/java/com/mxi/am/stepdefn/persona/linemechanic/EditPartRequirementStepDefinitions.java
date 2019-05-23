
package com.mxi.am.stepdefn.persona.linemechanic;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;

import com.mxi.am.driver.query.FaultQueriesDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.part.EditPartRequirementsPageDriver;
import com.mxi.am.driver.web.task.TaskDetails.TaskDetailsPageDriver;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


/**
 * Step definitions for Add Part Requirement Search
 *
 */
public class EditPartRequirementStepDefinitions {

   @Inject
   private TaskDetailsPageDriver taskDetailsPageDriver;

   @Inject
   private EditPartRequirementsPageDriver editPartRequirementPageDriver;

   @Inject
   private NavigationDriver navigationDriver;

   @Inject
   private FaultQueriesDriver faultQueriesDriver;

   private final String AIRCRAFT_SN = "LT-IPC-1";
   private final String FAULT_NAME = "IPC Fault";
   private final String REFERENCE = "IPC: 72-10-13-11";
   private final String REMOVED_PART_SERIAL_NO = "456789";
   private final String REMOVED_PART_REASON = "LOAN";


   @Given( "^there exists a part requirement on a fault$" )
   public void thereExistsAPartRequirement() throws Throwable {
      // setup done via loader tools
   }


   @When( "^I edit the part requirement$" )
   public void iEditAPartRequirement() throws Throwable {

      // Navigate to the task details page using the barcode
      String lFaultBarcode = faultQueriesDriver
            .getByDeferredFaultNameAndSerialNo( AIRCRAFT_SN, FAULT_NAME ).getBarcode();
      navigationDriver.barcodeSearch( lFaultBarcode );

      // check the part requirement row
      taskDetailsPageDriver.clickTabTaskExecution().getPartRequestsRows().get( 0 )
            .checkPartRequestRow();

      // click the edit part requirement button
      taskDetailsPageDriver.getTabTaskExecution().clickEditPartRequirement();

      // editPartRequirementPageDriver
      editPartRequirementPageDriver.getResults().get( 0 ).setReference( REFERENCE );
      editPartRequirementPageDriver.getResults().get( 0 )
            .setRemovedSerBatNo( REMOVED_PART_SERIAL_NO );
      editPartRequirementPageDriver.getResults().get( 0 ).setRemovedReason( REMOVED_PART_REASON );
      editPartRequirementPageDriver.clickOk();

   }


   @Then( "^the part requirement gets updated$" )
   public void thePartRequirementGetsUpdated() throws Throwable {

      String updatedRemovedPartSerialNo =
            taskDetailsPageDriver.getPartRequirementRows().get( 0 ).getRemovedPartSerialNo();
      String updatedPartReqReference =
            taskDetailsPageDriver.getPartRequirementRows().get( 0 ).getPartRequirementReference();
      String updatedRemovedPartReason =
            taskDetailsPageDriver.getPartRequirementRows().get( 0 ).getRemovedPartReason();

      assertEquals( "Removed Part Reason did not get updated successfully", REMOVED_PART_REASON,
            updatedRemovedPartReason );
      assertEquals( "Removed Part Serial Number did not get updated successfully",
            REMOVED_PART_SERIAL_NO, updatedRemovedPartSerialNo );
      assertEquals( "Part Requirement Reference did not get updated successfully", REFERENCE,
            updatedPartReqReference );

   }
}
