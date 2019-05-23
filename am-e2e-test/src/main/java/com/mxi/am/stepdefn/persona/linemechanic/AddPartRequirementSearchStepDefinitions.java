
package com.mxi.am.stepdefn.persona.linemechanic;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;

import com.mxi.am.driver.query.PartQueriesDriver;
import com.mxi.am.driver.query.SchedTaskQueriesDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.part.AddPartRequirementSearchPageDriver;
import com.mxi.am.driver.web.task.TaskDetails.TaskDetailsPageDriver;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


/**
 * Step definitions for Add Part Requirement Search
 *
 */
public class AddPartRequirementSearchStepDefinitions {

   @Inject
   private TaskDetailsPageDriver taskDetailsPageDriver;

   @Inject
   private AddPartRequirementSearchPageDriver addPartRequirementSearchPageDriver;

   @Inject
   private NavigationDriver navigationDriver;

   @Inject
   private PartQueriesDriver partQueriesDriver;

   @Inject
   private SchedTaskQueriesDriver iSchedTaskQueriesDriver;

   private final String AIRCRAFT_SN = "LIN-300";
   private final String TASK_CD = "SYS-JIC-1";
   private final String PART_NO = "ENG_ASSY_PN1";
   private final String PART_GROUP_CD = "ENG-ASSY";
   private final String REFERENCE = "IPC: 72-10-13-11";
   private final String PART_REQ_DESC = "ENG-ASSY (Engine Sub-Assembly) - 1";


   // @Given: login as line technician is in LoginStepDefinitions

   @When( "^I add a part requirement to a task$" )
   public void iAddAPartRequirementToATask() throws Throwable {

      // Navigate to the task details page using the barcode
      String lTaskBarcode = getBarcode( AIRCRAFT_SN, TASK_CD );
      navigationDriver.barcodeSearch( lTaskBarcode );

      // click on add part requirement search button
      taskDetailsPageDriver.clickTabTaskExecution().clickAddPartRequirementSearch();

      // use typeahead to search for a part
      addPartRequirementSearchPageDriver.searchForPart( PART_NO );

      // select the part from the search results
      String lUniquePartId = getUniquePartId( PART_NO, PART_GROUP_CD );
      addPartRequirementSearchPageDriver.selectPartWithId( lUniquePartId );

      // add reference and click the assign to task button
      addPartRequirementSearchPageDriver.enterReference( REFERENCE ).clickAssignToTaskButton();

   }


   @Then( "^the part requirement is assigned to the task$" )
   public void thePartRequirementIsAssignedToTheTask() throws Throwable {

      String newPartReqDesc =
            taskDetailsPageDriver.getPartRequirementRows().get( 0 ).getPartRequirementDescription();
      String newPartReqReference =
            taskDetailsPageDriver.getPartRequirementRows().get( 0 ).getPartRequirementReference();

      assertEquals( "Part Requirement Description did not get added successfully", PART_REQ_DESC,
            newPartReqDesc );
      assertEquals( "Part Requirement Reference did not get added successfully", REFERENCE,
            newPartReqReference );

   }


   private String getBarcode( String aSerialNo, String aTaskCd ) {
      return iSchedTaskQueriesDriver.getByAircraftSerNoAndTaskCode( aSerialNo, aTaskCd )
            .getBarcode();
   }


   private String getUniquePartId( String aPartNo, String aPartGroupCd ) {
      return partQueriesDriver.getByPartNo( aPartNo, aPartGroupCd ).getUniqueKey();
   }

}
