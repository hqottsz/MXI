package com.mxi.am.stepdefn.persona.lineplanner;

import javax.inject.Inject;

import org.junit.Assert;

import com.mxi.am.driver.query.InventoryQueriesDriver;
import com.mxi.am.driver.query.WorkPackageKey;
import com.mxi.am.driver.query.WorkPackageQueriesDriver;
import com.mxi.am.driver.web.AuthenticationRequiredPageDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.inventory.ReleaseInventoryPageDriver;
import com.mxi.am.driver.web.inventory.inventorydetailspage.InventoryDetailsPageDriver;
import com.mxi.am.driver.web.task.CreateOrEditCheckPageDriver;
import com.mxi.am.driver.web.task.PreviewReleasePageDriver;
import com.mxi.am.driver.web.task.CheckDetails.CheckDetailsPageDriver;
import com.mxi.mx.core.key.RefEventStatusKey;

import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;


@ScenarioScoped
public class WorkPackageStepDefinitions {

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private InventoryDetailsPageDriver iInventoryDetailsPageDriver;

   @Inject
   private CreateOrEditCheckPageDriver iCreateOrEditCheckPageDriver;

   @Inject
   private CheckDetailsPageDriver iWorkPackageDetailsDriver;

   @Inject
   private PreviewReleasePageDriver iPreviewReleasePageDriver;

   @Inject
   private ReleaseInventoryPageDriver iReleaseInventoryPageDriver;

   @Inject
   private InventoryQueriesDriver iInventoryQueriesDriver;

   @Inject
   private WorkPackageQueriesDriver iWorkPackageQueriesDriver;

   @Inject
   private AuthenticationRequiredPageDriver iAuthenticationRequiredPageDriver;

   private String iAircraftRegCode = "AMP3";
   private String iCreateWorkPackage = "E2E-CREATE-WORK-PACKAGE";
   private String iCompleteWorkPackage = "E2E-COMPLETE-WORK-PACKAGE";
   private String iPassword = "password";
   private static final String INSP_SKILL = "INSP";
   private static final String AET_SKILL = "AET";
   private static final boolean IS_RELEASE_TO_SERVICE = true;


   private String getInventoryBarcode() {
      return iInventoryQueriesDriver.getByAircraftRegistrationCode( iAircraftRegCode ).getBarcode();
   }


   private String getWorkPackageBarcode() {
      return iWorkPackageQueriesDriver.getByWorkPackageName( iCompleteWorkPackage ).getBarcode();
   }


   @Before( "@CompleteWorkPackage" )
   public void setupDataForCompleteWorkPackageScenario() {

      // get work package info and set it to in-work
      WorkPackageKey lWorkPackageKey =
            iWorkPackageQueriesDriver.getByWorkPackageName( iCompleteWorkPackage );

      iWorkPackageQueriesDriver.setWorkPackageStatus( lWorkPackageKey.getDbId(),
            RefEventStatusKey.IN_WORK.getCd(), iCompleteWorkPackage );

   }


   @When( "^I create a new work package$" )
   public void iCreateANewWorkPackage() throws Throwable {
      /*
       * Aircraft set up in C_RI_ATTACH.csv, C_RI_INVENTORY.csv and C_RI_Inventory_Usages.csv from
       * am-e2e-test\src\main\data\actuals\persona\linePlanner\WorkPackage
       */
      iNavigationDriver.barcodeSearch( getInventoryBarcode() );
      iInventoryDetailsPageDriver.clickTabOpen().clickTabOpenWorkPackages()
            .clickCreateWorkPackage();
      iCreateOrEditCheckPageDriver.setName( iCreateWorkPackage );
      iCreateOrEditCheckPageDriver.clickOK();
   }


   @Then( "^a new work package is created$" )
   public void aNewWorkPackageIsCreated() throws Throwable {
      Assert.assertTrue(
            iWorkPackageDetailsDriver.getPageTitle().contains( "Work Package Details" ) );
      Assert.assertTrue(
            iWorkPackageDetailsDriver.getPageSubTitle().contains( iCreateWorkPackage.toString() ) );
   }


   @When( "^I complete a work package and release aircraft$" )
   public void iCompleteAWorkPackageAndReleaseAircraft() throws Throwable {
      iNavigationDriver.barcodeSearch( getWorkPackageBarcode() );
      iWorkPackageDetailsDriver.clickCompleteWorkPackage();
      iPreviewReleasePageDriver.clickNextButton();

      iReleaseInventoryPageDriver.sign( AET_SKILL );
      iAuthenticationRequiredPageDriver.setPassword_Type1( iPassword );
      iAuthenticationRequiredPageDriver.clickOk();

      iReleaseInventoryPageDriver.setReleaseToServiceDropdownSelection( IS_RELEASE_TO_SERVICE );

      iReleaseInventoryPageDriver.sign( INSP_SKILL );
      iAuthenticationRequiredPageDriver.setPassword2_Type1( iPassword );
      iAuthenticationRequiredPageDriver.clickOk();

      iReleaseInventoryPageDriver.clickCloseButton();

   }


   @Then( "^the work package is completed and the aircraft is released$" )
   public void theWorkPackageIsCompletedAndTheAircraftIsReleased() throws Throwable {
      Assert.assertTrue(
            iWorkPackageDetailsDriver.clickTabDetails().getStatus().contains( "COMPLETE" ) );
      Assert.assertTrue( iWorkPackageDetailsDriver.clickTabDetails().getReleaseToService() );
   }

}
