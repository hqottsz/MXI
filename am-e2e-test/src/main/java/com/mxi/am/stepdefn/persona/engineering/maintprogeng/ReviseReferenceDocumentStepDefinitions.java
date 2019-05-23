package com.mxi.am.stepdefn.persona.engineering.maintprogeng;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;

import com.mxi.am.driver.web.AuthenticationRequiredPageDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.taskdefn.CreateRevisionPageDriver;
import com.mxi.am.driver.web.taskdefn.DispositionRefDocumentPageDriver;
import com.mxi.am.driver.web.taskdefn.RefDocDetails.RefDocDetailsPageDriver;
import com.mxi.am.driver.web.taskdefn.RefDocDetails.RefDocDetailsPanes.RevisionPaneDriver;
import com.mxi.am.driver.web.taskdefn.taskdefinitionsearchpage.TaskDefinitionSearchPageDriver;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;


/**
 * Step definitions for the feature Revise Requirement
 *
 */
@ScenarioScoped
public class ReviseReferenceDocumentStepDefinitions {

   // Existing data in DB.
   private static final String REFERENCE_DOCUMENT_CLASS = "REF (Reference Document)";
   private static final String USERS_PASSWORD = "password";
   private static final String ENGINEER = "Engineer";
   private static final String TASK_DEFINITION_SEARCH = "Task Definition Search";
   private static final String ACTIVE_STATE = "ACTV (Activated State)";
   private static final String REFERENCE_DOC_CODE = "ENG-OPER18104";
   private static final String REVISON_REASON = "ADMIN (Administrative Changes)";
   private static final String REVISON_NOTES = "Revision Notes";
   private static final String DISPOSITION_TYPE = "EO (Engineering Order Generation Required)";
   private static final String DISPOSITION_NOTES = "Test Disposition";
   private static final String ACTIVATION_DESCRIPTION = "Activating Now";
   private static final String ACTIVATION_REFERENCES = "REF00001";

   private String iRequirementRevision;

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private TaskDefinitionSearchPageDriver iTaskDefinitionSearchPageDriver;

   @Inject
   private RefDocDetailsPageDriver iRefDocDetailsPageDriver;

   @Inject
   private DispositionRefDocumentPageDriver iDispositionReferenceDocumentPageDriver;

   @Inject
   private AuthenticationRequiredPageDriver iRequestAuthorizationPageDriver;

   @Inject
   private CreateRevisionPageDriver iCreateRevisionPageDriver;


   @And( "^a reference document exists$" )
   public void aReferenceDocumentExists() throws Throwable {
      // data setup done using loading tools
      iNavigationDriver.navigate( ENGINEER, TASK_DEFINITION_SEARCH );
      iTaskDefinitionSearchPageDriver.clickClearAll();
      iTaskDefinitionSearchPageDriver.setTaskDefinitionCode( REFERENCE_DOC_CODE );
      iTaskDefinitionSearchPageDriver.setTaskDefinitionClass( REFERENCE_DOCUMENT_CLASS );
      iTaskDefinitionSearchPageDriver.setTaskDefinitionStatus( ACTIVE_STATE );
      iTaskDefinitionSearchPageDriver.clickSearch();
      iTaskDefinitionSearchPageDriver.clickTabTaskDefinitionsFound()
            .clickTableTaskDefinition( REFERENCE_DOC_CODE );
   }


   @When( "^I revise a reference document$" )
   public void iReviseAReferenceDocument() throws Throwable {

      iRefDocDetailsPageDriver.clickCreateRevision();
      iCreateRevisionPageDriver.setRevisionReason( REVISON_REASON );
      iCreateRevisionPageDriver.setRevisionNotes( REVISON_NOTES );
      iCreateRevisionPageDriver.clickOk();

      iRefDocDetailsPageDriver.clickActivateReferenceDocument();

      iDispositionReferenceDocumentPageDriver.setDisposition( DISPOSITION_TYPE );
      iDispositionReferenceDocumentPageDriver.setDispositionNotes( DISPOSITION_NOTES );
      iDispositionReferenceDocumentPageDriver.setActivateDescription( ACTIVATION_DESCRIPTION );
      iDispositionReferenceDocumentPageDriver.setActivateReference( ACTIVATION_REFERENCES );
      iDispositionReferenceDocumentPageDriver.setRevisionReason( REVISON_REASON );
      iDispositionReferenceDocumentPageDriver.setRevisionNotes( REVISON_NOTES );

      iDispositionReferenceDocumentPageDriver.clickOk();
      iRequestAuthorizationPageDriver.setPassword_Type2( USERS_PASSWORD );
      iRequestAuthorizationPageDriver.clickOk();

      setRequirementRevision( iRefDocDetailsPageDriver.getRevision() );
   }


   @Then( "^the reference document is updated$" )
   public void theReferenceDocumentIsUpdated() throws Throwable {

      RevisionPaneDriver lRefDocRevisionPaneDriver = iRefDocDetailsPageDriver.clickTabRevision();

      assertEquals( ACTIVATION_REFERENCES,
            lRefDocRevisionPaneDriver.getActivationReference( "Rev " + getRequirementRevision() ) );
      assertEquals( REVISON_REASON,
            lRefDocRevisionPaneDriver.getRevisionReason( "Rev " + getRequirementRevision() ) );
      assertEquals( REVISON_NOTES,
            lRefDocRevisionPaneDriver.getRevisionNotes( "Rev " + getRequirementRevision() ) );

   }


   private void setRequirementRevision( String aValue ) {
      iRequirementRevision = aValue;
   }


   private String getRequirementRevision() {
      return iRequirementRevision;
   }

}
