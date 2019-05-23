package com.mxi.am.stepdefn.persona.engineering.systemeng;

import static org.junit.Assert.assertTrue;

import javax.inject.Inject;

import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.taskdefn.ActivateTaskDefinitionPageDriver;
import com.mxi.am.driver.web.taskdefn.CreateRevisionPageDriver;
import com.mxi.am.driver.web.taskdefn.reqdetailspage.ReqDetailsPageDriver;
import com.mxi.am.driver.web.taskdefn.taskdefinitionsearchpage.TaskDefinitionSearchPageDriver;
import com.mxi.driver.standard.Wait;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


/**
 * Bulk Activate Requirements Step Definitions
 */
public class BulkActivateRequirementsStepDefinitions {

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private TaskDefinitionSearchPageDriver iTaskDefinitionSearchPageDriver;

   @Inject
   private ReqDetailsPageDriver iReqDetailsPageDriver;

   @Inject
   private CreateRevisionPageDriver iCreateRevisionPageDriver;

   @Inject
   private ActivateTaskDefinitionPageDriver iActivateTaskDefinitionPageDriver;

   // Test data
   private final String ENGINEER = "Engineer";
   private final String TASK_DEFINITION_SEARCH = "Task Definition Search";


   @When( "^I select multiple requirements for activation$" )
   public void iSelectMultipleRequirementsForActivation() throws Throwable {
      createRevision( "OPER-18897-1" );
      createRevision( "OPER-18897-2" );
      iNavigationDriver.navigate( ENGINEER, TASK_DEFINITION_SEARCH );
      iTaskDefinitionSearchPageDriver.setTaskDefinitionCode( "OPER-18897" );
      iTaskDefinitionSearchPageDriver.clickSearch();
      iTaskDefinitionSearchPageDriver.clickTabTaskDefinitionsFound()
            .clickTableTaskDefinitionCheckbox( "OPER-18897-1" );
      iTaskDefinitionSearchPageDriver.clickTabTaskDefinitionsFound()
            .clickTableTaskDefinitionCheckbox( "OPER-18897-2" );
      iTaskDefinitionSearchPageDriver.clickTabTaskDefinitionsFound().clickActivateTaskDefinition();
   }


   @Then( "^I am not able to enter revision information \\(reason and notes\\)$" )
   public void iAmNotAbleToEnterRevisionInformationReasonAndNotes() throws Throwable {
      iActivateTaskDefinitionPageDriver.setActivateDescription( "This is a test!" );
      iActivateTaskDefinitionPageDriver.setActivateReference( "This is a test!" );
      assertTrue( iActivateTaskDefinitionPageDriver.isRevisionReasonNotVisible() );
      assertTrue( iActivateTaskDefinitionPageDriver.isRevisionNotesNotVisible() );
      iActivateTaskDefinitionPageDriver.clickOk();
   }


   private void createRevision( String aReq ) {
      iNavigationDriver.navigate( ENGINEER, TASK_DEFINITION_SEARCH );
      iTaskDefinitionSearchPageDriver.clickClearAll();
      iTaskDefinitionSearchPageDriver.setTaskDefinitionCode( aReq );
      iTaskDefinitionSearchPageDriver.clickSearch();
      iTaskDefinitionSearchPageDriver.clickTabTaskDefinitionsFound()
            .clickTableTaskDefinition( aReq );
      Wait.pause( 5000 );
      iReqDetailsPageDriver.clickCreateRevision();
      iCreateRevisionPageDriver.clickOk();
   }
}
