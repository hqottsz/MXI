package com.mxi.am.stepdefn.persona.engineering.maintprogeng;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;

import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.taskdefn.AddStepPageDriver;
import com.mxi.am.driver.web.taskdefn.JICDetails.JICDetailsPageDriver;
import com.mxi.am.driver.web.taskdefn.taskdefinitionsearchpage.TaskDefinitionSearchPageDriver;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class JobCardStepLevelSignOffStepDefinition {

   @Inject
   private NavigationDriver navigationDriver;

   @Inject
   private AddStepPageDriver addStepPageDriver;

   @Inject
   private JICDetailsPageDriver jICDetailsPageDriver;

   @Inject
   private TaskDefinitionSearchPageDriver taskDefinitionSearchPageDriver;

   private static final String ENGINEER = "Engineer";
   private static final String TASK_DEFINITION_SEARCH = "Task Definition Search";
   private static final String TASK_DEFINITION_CODE = "BAM-JICSTEPLEVELSIGNOFF-JIC1";
   private static final String DESCRIPTION = "Job Card Step Level Sign Off E2E Test";
   private static final String SKILL = "LBR";
   private static final int JOB_CARD_STEP_1 = 1;


   @When( "^I add Job Card Step$" )
   public void iAddJobCardStep() throws Throwable {
      navigationDriver.navigate( ENGINEER, TASK_DEFINITION_SEARCH );
      taskDefinitionSearchPageDriver.setTaskDefinitionCode( TASK_DEFINITION_CODE );
      taskDefinitionSearchPageDriver.clickSearch();
      taskDefinitionSearchPageDriver.clickTabTaskDefinitionsFound()
            .clickTableTaskDefinition( TASK_DEFINITION_CODE );
      jICDetailsPageDriver.clickTabExecution();
      jICDetailsPageDriver.getTabExecution().clickAddStep();
      addStepPageDriver.getRowForCertifySkill( SKILL ).setRequiredForThisStep( true );
      addStepPageDriver.setDescription( DESCRIPTION );
      addStepPageDriver.clickOkButton();
   }


   @Then( "^the Job Card Step is added$" )
   public void theJobCardStepIsAdded() throws Throwable {
      assertEquals(
            "Job Card Step " + JOB_CARD_STEP_1 + " does NOT have Certifying Skill (" + SKILL + ")",
            SKILL, jICDetailsPageDriver.getTabExecution()
                  .getCertifyingSkillOfJobCardStep( JOB_CARD_STEP_1 ) );
   }

}
