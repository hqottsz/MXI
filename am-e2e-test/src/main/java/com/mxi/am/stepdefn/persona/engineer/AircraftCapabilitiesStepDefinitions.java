package com.mxi.am.stepdefn.persona.engineer;

import javax.inject.Inject;

import org.junit.Assert;

import com.mxi.am.driver.common.ConfirmPageDriver;
import com.mxi.am.driver.query.InventoryQueriesDriver;
import com.mxi.am.driver.web.LoginPageDriver;
import com.mxi.am.driver.web.LogoutPageDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.assembly.capabilities.assign.AssignCapabilitiesPageDriver;
import com.mxi.am.driver.web.assembly.details.AssemblyDetailsPageDriver;
import com.mxi.am.driver.web.assembly.details.panes.CapabilitiesPaneDriver;
import com.mxi.am.driver.web.inventory.capabilities.editcapabilitylevelspage.EditCapabilityLevelsPageDriver;
import com.mxi.am.driver.web.inventory.inventorydetailspage.InventoryDetailsPageDriver;
import com.mxi.am.driver.web.inventory.inventorydetailspage.inventorydetailspanes.historicalpanes.AdditionalPaneDriver;
import com.mxi.am.driver.web.todolist.ToDoListPageDriver;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


/**
 * Aircraft Capabilities Step Definition
 *
 * @author smichal
 *
 */
public class AircraftCapabilitiesStepDefinitions {

   @Inject
   private AssemblyDetailsPageDriver iAssemblyDetails;

   @Inject
   private InventoryDetailsPageDriver iInventoryDetails;

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private AssignCapabilitiesPageDriver iAssignCapabilitiesPageDriver;

   @Inject
   private EditCapabilityLevelsPageDriver iEditCapabilityLevelsPageDriver;

   @Inject
   private LoginPageDriver iLoginDriver;

   private final String iAircraftBarcode;

   @Inject
   private ConfirmPageDriver iConfirmPageDriver;

   @Inject
   private LogoutPageDriver iLogoutDriver;

   @Inject
   private ToDoListPageDriver iToDoListPageDriver;


   @Inject
   public AircraftCapabilitiesStepDefinitions(InventoryQueriesDriver aInventoryQueriesDriver) {
      iAircraftBarcode =
            aInventoryQueriesDriver.getByAircraftRegistrationCode( AIRCRAFT_REG_CODE ).getBarcode();
   }


   // declare strings once only to avoid typos
   private final String RVSM = "RVSM";
   private final String ETOPS = "ETOPS";
   private final String SEATNUM = "SEATNUM";
   private final String YES = "YES";
   private final String NO = "NO";
   private final String ETOPS_90 = "ETOPS_90";
   private final String ETOPS120 = "ETOPS120";
   private final String SEATNUM_122 = "122";
   private final String CustomCapLevel = "119";
   private final String iETOPsDescription = "Extended Operations";
   private final String iSEATNUMsDescription = "Seat Count / Available Seat Count";
   private final String iCapabilityHistoryNote1 =
         "Addition of " + iETOPsDescription + " capability";
   private final String iCapabilityHistoryNote2 =
         ETOPS120 + " setting available for Extended Operations";
   private final String iCapabilityHistoryNote3 =
         "Seat Count / Available Seat Count configured capability change from Unknown to "
               + SEATNUM_122;
   private final String iCurrentCapabilityHistoryNote1 =
         "Extended Operations current capability change from Unknown to ETOPS_90";
   private final String iCurrentCapabilityHistoryNote2 =
         "RVSM current capability change from Unknown to NO";
   private final String iCurrentCapabilityHistoryNote3 =
         "Seat Count / Available Seat Count current capability change from Unknown to 119";
   private static final String AIRCRAFT_REG_CODE = "200";

   private final static String ENGINEER = "Engineer";
   private final static String TO_DO_LIST = "To Do List (Engineer)";

   private final static String ASSEMBLY = "ACFT_CD1 (Aircraft Assembly)";


   @When( "^I assign capabilities to an aircraft assembly$" )
   public void assignCapabilitiesToAcftAssy() throws Throwable {
      iNavigationDriver.navigate( ENGINEER, TO_DO_LIST );
      iToDoListPageDriver.clickTabAssemblyList().clickAssembly( ASSEMBLY );
      iAssemblyDetails.clickTabCapabilities().clickAssignCapability();

      iAssignCapabilitiesPageDriver.clickApplicableCapabilityIfUncheck( RVSM );
      iAssignCapabilitiesPageDriver.clickCapabilityLevelsIfUncheck( ETOPS, ETOPS_90 );
      iAssignCapabilitiesPageDriver.clickCapabilityLevelsIfUncheck( ETOPS, ETOPS120 );
      iAssignCapabilitiesPageDriver.clickCapabilityLevelsIfUncheck( SEATNUM, SEATNUM_122 );
      iAssignCapabilitiesPageDriver.clickOk();
   }


   @Then( "^the capability assignment is applied to the aircraft assembly$" )
   public void capabilityAssignmentIsAppliedToAcftAssy() throws Throwable {
      // click Capabilities tab
      CapabilitiesPaneDriver lCapabilitiesPaneDriver = iAssemblyDetails.clickTabCapabilities();
      // check that the table contains the expected capability
      Assert.assertTrue( lCapabilitiesPaneDriver.isCapabilityInTable( RVSM ) );
      // check that the table contains the expected capability levels
      Assert.assertTrue( lCapabilitiesPaneDriver.isCapabilityInTable( iETOPsDescription ) );
      Assert.assertTrue( lCapabilitiesPaneDriver.isCapabilityInTable( iSEATNUMsDescription ) );
      Assert.assertTrue( lCapabilitiesPaneDriver.isCapabilityInTable( YES ) );
      Assert.assertTrue( lCapabilitiesPaneDriver.isCapabilityInTable( ETOPS_90 ) );
      Assert.assertTrue( lCapabilitiesPaneDriver.isCapabilityInTable( ETOPS120 ) );

   }


   @When( "^I edit the capabilities of an aircraft assembly$" )
   public void editCapabilitiesOfAnAcftAssy() throws Throwable {
      iNavigationDriver.navigate( ENGINEER, TO_DO_LIST );
      iToDoListPageDriver.clickTabAssemblyList().clickAssembly( ASSEMBLY );
      iAssemblyDetails.clickTabCapabilities().clickAssignCapability();

      iAssignCapabilitiesPageDriver.clickApplicableCapabilityIfChecked( RVSM );
      iAssignCapabilitiesPageDriver.clickApplicableCapabilityIfChecked( iETOPsDescription );
      iAssignCapabilitiesPageDriver.clickApplicableCapabilityIfChecked( iSEATNUMsDescription );

      // save the change - click OK button
      iAssignCapabilitiesPageDriver.clickOk();

      iConfirmPageDriver.clickYes();
   }


   @Then( "^the capability updates are applied to the aircraft assembly$" )
   public void capabilityUpdatesAreAppliedToAcftAssy() throws Throwable {
      // click Capabilities tab
      CapabilitiesPaneDriver lCapabilitiesPaneDriver = iAssemblyDetails.clickTabCapabilities();
      Assert.assertFalse( lCapabilitiesPaneDriver.isCapabilityInTable( RVSM ) );
      Assert.assertFalse( lCapabilitiesPaneDriver.isCapabilityInTable( iETOPsDescription ) );
      Assert.assertFalse( lCapabilitiesPaneDriver.isCapabilityInTable( iSEATNUMsDescription ) );

   }


   @Then( "^the assembly capability changes have been logged there$" )
   public void assemblyCapabilityChangesHaveBeenLogged() throws Throwable {
      iNavigationDriver.barcodeSearch( iAircraftBarcode );
      // click Additional tab
      AdditionalPaneDriver lAdditionalPaneDriver =
            iInventoryDetails.clickTabHistorical().clickTabAdditional();
      // check that the table does contain the expected history
      Assert.assertTrue( lAdditionalPaneDriver.isHistoryInTable( iCapabilityHistoryNote1 ) );
      Assert.assertTrue( lAdditionalPaneDriver.isHistoryInTable( iCapabilityHistoryNote2 ) );
      Assert.assertTrue( lAdditionalPaneDriver.isHistoryInTable( iCapabilityHistoryNote3 ) );

   }


   @When( "^I edit the configured capability levels of an actual aircraft$" )
   public void iEditTheConfiguredCapabilityLevelsOfAnActualAircraft() throws Throwable {
      iNavigationDriver.barcodeSearch( iAircraftBarcode );
      // click Capabilities tab
      iInventoryDetails.clickTabCapabilities().clickEditCapabilities();
      // select capability levels both config and current
      iEditCapabilityLevelsPageDriver.selectCapabilityLevel( "aConfigCapLevel", RVSM, YES );
      iEditCapabilityLevelsPageDriver.selectCapabilityLevel( "aConfigCapLevel", ETOPS, ETOPS120 );
      iEditCapabilityLevelsPageDriver.selectCapabilityLevel( "aConfigCapLevel", SEATNUM,
            SEATNUM_122 );

      // save the change - click OK button
      iEditCapabilityLevelsPageDriver.clickOkButton();
   }


   @Then( "^the configured capability updates are applied to the aircraft$" )
   public void theConfiguredCapabilityUpdatesAreAppliedToTheAircraft() throws Throwable {
      // click Capabilities tab
      com.mxi.am.driver.web.inventory.inventorydetailspage.inventorydetailspanes.CapabilitiesPaneDriver lCapabilitiesPaneDriver =
            iInventoryDetails.clickTabCapabilities();
      // check that the table contains the expected capability levels
      Assert.assertTrue( lCapabilitiesPaneDriver.isCapabilityInTable( YES ) );
      Assert.assertTrue( lCapabilitiesPaneDriver.isCapabilityInTable( ETOPS120 ) );
      Assert.assertTrue( lCapabilitiesPaneDriver.isCapabilityInTable( SEATNUM_122 ) );
   }

}
