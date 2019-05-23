package com.mxi.am.stepdefn.persona.engineering.maintprogeng;

import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Assert;

import com.google.inject.Inject;
import com.mxi.am.driver.query.SchedTaskQueriesDriver;
import com.mxi.am.driver.query.SchedTaskQueriesDriver.TaskInfo;
import com.mxi.am.driver.web.AuthenticationRequiredPageDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.RefreshDriver;
import com.mxi.am.driver.web.common.jobviewer.JobViewerPageDriver;
import com.mxi.am.driver.web.task.TaskDetails.TaskDetailsPageDriver;
import com.mxi.am.driver.web.task.TaskDetails.TaskDetailsPanes.TaskInformationPaneDriver;
import com.mxi.am.driver.web.taskdefn.ActivateJobCardPageDriver;
import com.mxi.am.driver.web.taskdefn.CreateRevisionPageDriver;
import com.mxi.am.driver.web.taskdefn.JICDetails.JICDetailsPageDriver;
import com.mxi.am.driver.web.taskdefn.taskdefinitionsearchpage.TaskDefinitionSearchPageDriver;
import com.mxi.driver.standard.Wait;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;


/**
 * Step definitions for the feature Revise Requirement
 *
 */

@ScenarioScoped
public class ReviseJobCardStepDefinitions {

   private static final String JOB_CARD_CLASS = "JIC (Job Instruction Card)";
   private static final String REVISION = "REVISION";
   private static final String ACTIVATED = "ACTV";
   private static final String ACTIVATED_STATE = "ACTV (Activated State)";

   private static final String JIC_DEFINITION_CODE = "ENG-REVISEJIC-JIC1";
   private static final String AIRCRAFT_SERIAL_NUMBER = "ReviseJIC-ACFT-SN1";

   private static final int TIMEOUT_IN_SECONDS = 60 * 5;

   @Inject
   private SchedTaskQueriesDriver iSchedTaskQueriesDriver;

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private TaskDefinitionSearchPageDriver iTaskDefinitionSearchPageDriver;

   @Inject
   private JICDetailsPageDriver iJicDetailsPageDriver;

   @Inject
   private AuthenticationRequiredPageDriver iRequestAuthorizationPageDriver;

   @Inject
   private JobViewerPageDriver iJobViewerPageDriver;

   @Inject
   private CreateRevisionPageDriver iCreateRevisionPageDriver;

   @Inject
   private TaskDetailsPageDriver iTaskDetailsPageDriver;

   @Inject
   private ActivateJobCardPageDriver iActivateJobCardPageDriver;

   @Inject
   public RefreshDriver iRefreshDriver;

   private String iJicTaskBarcode;
   private String iNewJicDefnRevision;


   @Given( "^a JIC is added to a requirement$" )
   public void aJicAttachedToARequirement() throws Throwable {
      // REQ and JIC data setup using the loading tools, refer to:
      // am-e2e-test\src\main\data\baseline\persona\engineering\maintprogeng\ReviseJobCard\C_REQ.csv
      // am-e2e-test\src\main\data\baseline\persona\engineering\maintprogeng\ReviseJobCard\C_JIC.csv
      // am-e2e-test\src\main\data\baseline\persona\engineering\maintprogeng\ReviseJobCard\C_REQ_JIC.csv

   }


   @Given( "^the requirement and JIC have been initialized against an inventory$" )
   public void theRequirementAndJicHaveBeenInitializedAgainstAnInventory() throws Throwable {
      // Inventory data setup using the loading tools, refer to:
      // am-e2e-test\src\main\data\actuals\persona\engineering\maintprogeng\ReviseJobCard\C_RI_INVENTORY.csv
      // am-e2e-test\src\main\data\sql\persona\engineering\maintprogeng\ReviseJobCard\inv_inv.sql

      // Check if an active JIC task has previously been initialized for the aircraft (e.g. test was
      // previously run).
      iJicTaskBarcode = getActiveJicTaskBarcode();
      if ( iJicTaskBarcode == null ) {

         // Run the baseline sync job in order to initialize the JIC task.
         iJobViewerPageDriver.runBaselineSyncJob();

         // Wait until the JIC task is initialized.
         Date lTimeout = DateUtils.addSeconds( new Date(), TIMEOUT_IN_SECONDS );
         do {
            iJicTaskBarcode = getActiveJicTaskBarcode();
            if ( iJicTaskBarcode != null ) {
               return;
            }
            Wait.pause( 50 ); // in milliseconds
         } while ( ( new Date() ).before( lTimeout ) );

         Assert.fail( "Timed out waiting for job card task to be initialized (" + TIMEOUT_IN_SECONDS
               + " seconds)." );
      }
   }


   @When( "^the JIC is revised$" )
   public void theJicIsRevised() throws Throwable {

      searchForActivatedJicDefinition();

      // Create a new revision for the JIC definition.
      iJicDetailsPageDriver.clickCreateRevision();
      iCreateRevisionPageDriver.clickOk();

      // Ensure the JIC definition was revised.
      Assert.assertEquals( "Unexpected requirement status.", REVISION,
            iJicDetailsPageDriver.getStatus() );
   }


   @When( "^the revised JIC is activated$" )
   public void theRevisedJICIsActivated() throws Throwable {

      iJicDetailsPageDriver.clickActivateJobCard();
      iActivateJobCardPageDriver.clickOk();
      iRequestAuthorizationPageDriver.setPassword_Type2( "password" );
      iRequestAuthorizationPageDriver.clickOk();

      // Ensure the JIC definition was activated.
      Assert.assertEquals( "Unexpected JIC definition status.", ACTIVATED,
            iJicDetailsPageDriver.getStatus() );

      // Store the new revision number of the JIC definition.
      iNewJicDefnRevision = iJicDetailsPageDriver.getRevision();

      iJobViewerPageDriver.runBaselineSyncJob();
   }


   @Then( "^the revision of the JIC on the initialized task is updated$" )
   public void theActualGetsUpdated() throws Throwable {

      iNavigationDriver.barcodeSearch( iJicTaskBarcode );
      TaskInformationPaneDriver lTaskInfoPane = iTaskDetailsPageDriver.clickTabTaskInformation();

      // Baseline sync may take a while to update the JIC task, so we will wait until it is updated.
      // If the timer expires we will consider this test a failure.
      String lExpectedRevStr = "Rev" + iNewJicDefnRevision;

      Date lTimeout = DateUtils.addSeconds( new Date(), TIMEOUT_IN_SECONDS );
      do {
         if ( lTaskInfoPane.getTaskDefinition().contains( lExpectedRevStr ) ) {
            // The JIC's definition value should be enough to confirm but lets go to the JIC
            // definition to be sure.
            lTaskInfoPane.clickTaskDefinition();
            Assert.assertEquals( "JIC task was not updated to the latest JIC Definition revision.",
                  iNewJicDefnRevision, iJicDetailsPageDriver.getRevision() );
            return;
         }

         // Pause, reload the page, and check again (if not timed out).
         Wait.pause( 50 ); // in milliseconds
         iNavigationDriver.refreshPage();

      } while ( ( new Date() ).before( lTimeout ) );

      Assert.fail(
            "Timed out waiting for JIC task to be updated to the latest JIC Definition revision. Waited "
                  + TIMEOUT_IN_SECONDS + " seconds." );
   }


   private String getActiveJicTaskBarcode() {
      TaskInfo lJicTaskInfo = iSchedTaskQueriesDriver
            .getActiveByInvSerialNoAndTaskDefnCode( AIRCRAFT_SERIAL_NUMBER, JIC_DEFINITION_CODE );
      return ( lJicTaskInfo == null ) ? null : lJicTaskInfo.getBarcode();
   }


   private void searchForActivatedJicDefinition() {
      iNavigationDriver.navigate( "Engineer", "Task Definition Search" );
      iTaskDefinitionSearchPageDriver.clickClearAll();
      iTaskDefinitionSearchPageDriver.setTaskDefinitionCode( JIC_DEFINITION_CODE );
      iTaskDefinitionSearchPageDriver.setTaskDefinitionClass( JOB_CARD_CLASS );
      iTaskDefinitionSearchPageDriver.setTaskDefinitionStatus( ACTIVATED_STATE );
      iTaskDefinitionSearchPageDriver.clickSearch();
      iTaskDefinitionSearchPageDriver.clickTabTaskDefinitionsFound()
            .clickTableTaskDefinition( JIC_DEFINITION_CODE );

      // Ensure search found the JIC Definition.
      Assert.assertEquals( "Unexpectedly did not find JIC Definition " + JIC_DEFINITION_CODE,
            JIC_DEFINITION_CODE, iJicDetailsPageDriver.getCode() );
      Assert.assertEquals(
            "Unexpectedly did not find activated JIC Definition " + JIC_DEFINITION_CODE, ACTIVATED,
            iJicDetailsPageDriver.getStatus() );
   }

}
