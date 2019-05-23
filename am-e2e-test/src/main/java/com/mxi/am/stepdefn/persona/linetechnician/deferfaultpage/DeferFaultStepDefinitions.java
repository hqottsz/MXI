package com.mxi.am.stepdefn.persona.linetechnician.deferfaultpage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.inject.Inject;

import com.mxi.am.driver.query.InventoryQueriesDriver;
import com.mxi.am.driver.web.AuthenticationRequiredPageDriver;
import com.mxi.am.driver.web.CommonMessagePageDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.fault.DeferTasksPageDriver;
import com.mxi.am.driver.web.inventory.inventorydetailspage.InventoryDetailsPageDriver;
import com.mxi.am.driver.web.inventory.inventorydetailspage.inventorydetailspanes.openpanes.OpenFaultsPaneDriver;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class DeferFaultStepDefinitions {

   private static final String PASSWORD = "password";
   private static final String ARICRAFT_PART_NUMBER = "ACFT_P_LT6";
   private static final String ARICRAFT_SERIAL_NUMBER = "OPER-17609";
   private static final String MEL_B_DEFERRAL_CLASS = "MEL B (3 day MEL deviation)";
   private static final String MEL_SEVERITY = "MEL (MEL failure)";
   private static final String DEFERRAL_AUTHORIZATION = "auth_reference";
   private static final String DEFERRAL_CLASS = "MEL B";
   private static final String SEVERITY = "MEL";
   private static final String DEFERRAL_REFERENCE_MUST_BE_SPECIFIC_ERROR =
         "The deferral reference must be specified.";
   private static final String NOTES = "some notes";

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private InventoryDetailsPageDriver iInventoryDetailsPageDriver;

   @Inject
   private DeferTasksPageDriver iDeferFaultPageDriver;

   @Inject
   private AuthenticationRequiredPageDriver iAuthenticationRequiredPageDriver;

   @Inject
   private CommonMessagePageDriver iCommonMessagePageDriver;

   private final String iAircraftBarcode;


   @Inject
   public DeferFaultStepDefinitions(InventoryQueriesDriver aInventoryQueriesDriver) {
      iAircraftBarcode = aInventoryQueriesDriver.getBarcodeBySerialPartNo( ARICRAFT_PART_NUMBER,
            ARICRAFT_SERIAL_NUMBER );
   }


   @And( "^I attempt to defer the fault with MEL severity and missing deferral reference information$" )
   public void iAttemptToDeferFaultWithMELSeverityAndMissingDeferralReferenceInformation()
         throws Throwable {
      iNavigationDriver.barcodeSearch( iAircraftBarcode );

      OpenFaultsPaneDriver lClickTabOpenFaults =
            iInventoryDetailsPageDriver.clickTabOpen().clickTabOpenFaults();
      lClickTabOpenFaults.getOpenFaultsTableRows().get( 0 ).clickCheckBox();
      lClickTabOpenFaults.clickDeferFaultButton();

      iDeferFaultPageDriver.setFaultSeverity( MEL_SEVERITY );
      iDeferFaultPageDriver.setDeferralClass( MEL_B_DEFERRAL_CLASS );
      iDeferFaultPageDriver.setDeferralAuthorization( DEFERRAL_AUTHORIZATION );
      iDeferFaultPageDriver.setNotes( NOTES );
      iDeferFaultPageDriver.clearDeferralStartDate();
      iDeferFaultPageDriver.clickOKButton();

      iAuthenticationRequiredPageDriver.setPasswordAndClickOK( PASSWORD );
   }


   @When( "^I receive an error message about the missing deferral reference information and I acknowledge it$" )
   public void iReceiveAnErrorMessageAboutTheDeferralReferenceInformationAndIAcknowledgeIt()
         throws Throwable {
      assertTrue( iCommonMessagePageDriver.getMessage()
            .contains( DEFERRAL_REFERENCE_MUST_BE_SPECIFIC_ERROR ) );
      iCommonMessagePageDriver.clickOk();
   }


   @Then( "^the previous entered information remains in the session$" )
   public void thePreviousEnteredInformationRemainsInTheSession() throws Throwable {
      assertEquals( SEVERITY, iDeferFaultPageDriver.getFaultSeverity() );
      assertEquals( DEFERRAL_CLASS, iDeferFaultPageDriver.getDeferralClass() );
      assertEquals( DEFERRAL_AUTHORIZATION, iDeferFaultPageDriver.getDeferralAuthorization() );
      assertEquals( NOTES, iDeferFaultPageDriver.getNotes() );
   }
}
