
package com.mxi.am.stepdefn.persona.maintenancecontroller.phoneupdeferral;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import com.mxi.am.driver.common.database.DatabaseDriver;
import com.mxi.am.driver.web.AssetManagement;
import com.mxi.am.driver.web.lmoc.LmocDialogServiceDriver;
import com.mxi.am.driver.web.lmoc.phoneupdeferral.PhoneUpDeferralPageDriver;
import com.mxi.am.driver.web.lmoc.phoneupdeferral.model.DeferralAuthorizationSuccessDialogContent;
import com.mxi.am.driver.web.lmoc.phoneupdeferral.model.RaiseOpenFaultSuccessDialogContent;
import com.mxi.am.stepdefn.persona.maintenancecontroller.phoneupdeferral.data.PhoneUpDeferralScenarioData;
import com.mxi.am.stepdefn.persona.maintenancecontroller.phoneupdeferral.data.RaiseOpenFaultScenarioData;

import cucumber.api.java.After;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


/**
 * Step definitions for Phone Up Deferral deferral authorization and raising an open fault.
 *
 */
public class PhoneUpDeferralStepDefinitions {

   /**
    * Caps on time allowed for various page-related tasks to execute.
    */
   private static final int ACTION_EXECUTION_TIME_MILLIS = 20000;

   @Inject
   private PhoneUpDeferralPageDriver iPhoneUpDeferralPageDriver;

   @Inject
   @AssetManagement
   private DatabaseDriver iDatabaseDriver;


   @After( "@AuthorizeDeferral" )
   public void authorizeDeferralTearDown() {
      iDatabaseDriver.update( PhoneUpDeferralScenarioData.TEAR_DOWN_QUERY,
            PhoneUpDeferralScenarioData.FAULT_NAME );
   }


   @When( "^I authorize a phone up deferral$" )
   public void enterDeferralInformation() {
      iPhoneUpDeferralPageDriver.navigateTo();
      iPhoneUpDeferralPageDriver.fillInForm( PhoneUpDeferralScenarioData.FORM_DATA );
   }


   @Then( "^the deferred fault is authorized$" )
   public void newDeferralFaultCreated() throws Throwable {
      iPhoneUpDeferralPageDriver.approveDeferral();
      assertTrue( iPhoneUpDeferralPageDriver.getDialogDriver().isDialogVisible(
            ACTION_EXECUTION_TIME_MILLIS, PhoneUpDeferralScenarioData.SUCCESS_DIALOG_TITLE ) );

      DeferralAuthorizationSuccessDialogContent lDialogContent = iPhoneUpDeferralPageDriver
            .getDeferralAuthorizationSuccessDialogContent( ACTION_EXECUTION_TIME_MILLIS );

      assertTrue( StringUtils.isNotBlank( lDialogContent.getAuthorizationCode() ) );
      assertTrue( StringUtils.isNotBlank( lDialogContent.getFaultBarcode() ) );
      assertTrue(
            String.format( "Expected %s to contain %s", lDialogContent.getDeadlineDue(),
                  PhoneUpDeferralScenarioData.DRIVING_DEADLINE ),
            lDialogContent.getDeadlineDue()
                  .contains( PhoneUpDeferralScenarioData.DRIVING_DEADLINE ) );
      assertArrayEquals( PhoneUpDeferralScenarioData.CAPABILITY_REMINDERS,
            lDialogContent.getDegradedCapabilities().toArray() );

   }


   @When( "^I raise an open fault through a phone up deferral$" )
   public void enterOpenFaultInformation() {
      iPhoneUpDeferralPageDriver.navigateTo();
      iPhoneUpDeferralPageDriver.fillInForm( RaiseOpenFaultScenarioData.FORM_DATA );
      iPhoneUpDeferralPageDriver.raiseOpenFault();
   }


   @Then( "^the fault is raised successfully$" )
   public void verifyRaiseFaultSuccessDialog() throws Throwable {
      LmocDialogServiceDriver lDialogDriver = iPhoneUpDeferralPageDriver.getDialogDriver();
      RaiseOpenFaultSuccessDialogContent lDialogContent = iPhoneUpDeferralPageDriver
            .getRaiseOpenFaultSuccessDialogContent( ACTION_EXECUTION_TIME_MILLIS );

      assertTrue( lDialogDriver.isDialogVisible( ACTION_EXECUTION_TIME_MILLIS,
            RaiseOpenFaultScenarioData.SUCCESS_DIALOG_TITLE ) );
      assertEquals( RaiseOpenFaultScenarioData.FAULT_NAME, lDialogContent.getFaultName() );
      assertTrue( StringUtils.isNotBlank( lDialogContent.getFaultBarcode() ) );
      assertTrue( StringUtils.isNotBlank( lDialogContent.getWorkPackage() ) );
      assertTrue( StringUtils.isNotBlank( lDialogContent.getLineNumber() ) );
      assertNotNull( lDialogContent.getStartDate() );
      assertNotNull( lDialogContent.getEndDate() );
   }

}
