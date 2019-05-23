package com.mxi.am.stepdefn.persona.materialcontroller;

import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;

import com.mxi.am.driver.common.configurationParameters.ConfigurationParameterWorkflow;
import com.mxi.am.driver.query.InventoryQueriesDriver;
import com.mxi.am.driver.web.AmWebConditions;
import com.mxi.am.driver.web.AssetManagement;
import com.mxi.am.driver.web.AuthenticationRequiredPageDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.inventory.AvailabilityPaneDriver.AvailabilityTable;
import com.mxi.am.driver.web.inventory.DetachInventoryPageDriver;
import com.mxi.am.driver.web.inventory.InspectAsServiceablePageDriver;
import com.mxi.am.driver.web.inventory.MarkInventoryAsInspRequiredPageDriver;
import com.mxi.am.driver.web.inventory.inventorydetailspage.InventoryDetailsPageDriver;
import com.mxi.am.driver.web.inventory.inventorydetailspage.inventorydetailspanes.SubInventoryPaneDriver;
import com.mxi.am.driver.web.inventory.inventorysearchpage.InventorySearchByTypePageDriver;
import com.mxi.am.driver.web.inventory.inventorysearchpage.inventorysearchpanes.InventoryFoundPaneDriver;
import com.mxi.am.driver.web.inventory.inventorysearchpage.inventorysearchpanes.InventoryFoundPaneDriver.InventorySearchResult;
import com.mxi.am.driver.web.part.partdetailspage.PartDetailsPageDriver;
import com.mxi.am.driver.web.part.partgroupdetailspage.PartGroupDetailsPageDriver;
import com.mxi.am.driver.web.stock.StockDetailsPageDriver;
import com.mxi.driver.standard.Wait;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class RFBInventoryAvailabilityStepDefinitions {

   @Inject
   private PartGroupDetailsPageDriver iPartGroupDetailsPageDriver;

   @Inject
   private InventoryDetailsPageDriver iInventoryDetailsPageDriver;

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private ConfigurationParameterWorkflow iConfigurationParameterDriver;

   @Inject
   private MarkInventoryAsInspRequiredPageDriver iMarkAsInspectionRequiredPageDriver;

   @Inject
   private InspectAsServiceablePageDriver iInspectAsServiceablePageDriver;

   @Inject
   private DetachInventoryPageDriver iDetachPageDriver;

   @Inject
   private InventorySearchByTypePageDriver iInvSearchByTypePageDriver;

   @Inject
   private PartDetailsPageDriver iPartDetailsPageDriver;

   @Inject
   private StockDetailsPageDriver iStockDetailsPageDriver;

   @Inject
   private AuthenticationRequiredPageDriver iAuthenticationRequiredPageDriver;

   @Inject
   @AssetManagement
   private WebDriver iWebDriver;

   private final static String PAGE_NAME = "Inventory Details";

   private final String iTRKBarcode;


   @Inject
   public RFBInventoryAvailabilityStepDefinitions(InventoryQueriesDriver aInventoryQueriesDriver) {
      iTRKBarcode = aInventoryQueriesDriver.getBarcodeBySerialPartNo( PARENT_TRK_PART_NO,
            PARENT_TRK_SERIAL_NO );
   }


   private final static String INV_LOCATION = "AIRPORT1";
   private final static String PARENT_TRK_PART_NO = "A0000005";
   private final static String PARENT_TRK_SERIAL_NO = "MAT-TRK-W-HOLE1";
   private final static String CHILD_TRK_SERIAL_NO = "TRK_TOBE_REMOVED1";

   private static final String QUALITY_CONTROL_INSPECTOR = "Quality Control Inspector";
   private static final String INVENTORY_SEARCH_BY_TYPE = "Inventory Search by Type";


   @Given( "^Ready For Build is enabled$" )
   public void iRFBConfigParmIsEnabled() {
      // set config parm Ready for Build as TRUE instead of FALSE
      iConfigurationParameterDriver.temporaryParameterToggleAndRefresh( "ENABLE_READY_FOR_BUILD",
            "TRUE" );

   }


   @Given( "^a TRK inventory which is missing mandatory sub-components$" )
   public void iATrkInventoryMissingMandSubComponents() throws Throwable {
      iNavigationDriver.barcodeSearch( iTRKBarcode );
      SubInventoryPaneDriver lSuPaneDriver = iInventoryDetailsPageDriver.clickTabSubInventory();
      lSuPaneDriver.selectInventoryFromTree( PARENT_TRK_SERIAL_NO, CHILD_TRK_SERIAL_NO );

      lSuPaneDriver.clickDetachInventory();

      iDetachPageDriver.clickOk();
      iAuthenticationRequiredPageDriver.setPassword_Type2( "password" );
      iAuthenticationRequiredPageDriver.clickOk();
   }


   @When( "^I Inspect it as Serviceable$" )
   public void iInspectItAsServiceable() throws Throwable {

      // Make sure we are on the inventory details page
      iNavigationDriver.barcodeSearch( iTRKBarcode );

      // Mark as Inspection Required
      iInventoryDetailsPageDriver.clickMarkAsInspectionRequired();
      iMarkAsInspectionRequiredPageDriver.clickOk();
      // confirm
      iMarkAsInspectionRequiredPageDriver.setPassword( "password" );
      iMarkAsInspectionRequiredPageDriver.authenticate();

      // Inspect As Serviceable

      // wait until the page is loaded completely
      Wait.until( AmWebConditions.isOnPage( iWebDriver, PAGE_NAME ), 20000 );

      iInventoryDetailsPageDriver.clickInspectAsServiceable();
      iInspectAsServiceablePageDriver.printServiceablepartTag();
      iInspectAsServiceablePageDriver.clickOk();
      // confirm
      iAuthenticationRequiredPageDriver.setPassword_Type2( "password" );
      iAuthenticationRequiredPageDriver.clickOk();

      // Make sure the Inventory Details says Ready For Build
      Assert.assertEquals( "Unexpected condition", "RFB (Ready for Build)",
            iInventoryDetailsPageDriver.clickTabDetails().getCondition() );
   }


   @Then( "^the Inventory Search by Type page and the Part Number, Part Group and Stock Details availability tabs should show the inventory as RFB$" )
   public void iAvailabilityShowRFBInventory() throws Throwable {
      inventorySearchByTypeShowsRFBInventory();
      partNumberShowsRFBInventory();
      stockNumberShowsRFBInventory();
      partGroupShowsRFBInventory();
   }


   private void inventorySearchByTypeShowsRFBInventory() throws Throwable {
      iNavigationDriver.navigate( QUALITY_CONTROL_INSPECTOR, INVENTORY_SEARCH_BY_TYPE );
      iInvSearchByTypePageDriver = iInvSearchByTypePageDriver.setSearchBySerialNumber();
      iInvSearchByTypePageDriver.setSerialNoBatchNo( PARENT_TRK_SERIAL_NO ).clickSearch();

      InventoryFoundPaneDriver iInventoryFoundPaneDriver =
            iInvSearchByTypePageDriver.clickTabInventoryFound();

      InventorySearchResult lSearchResult = iInventoryFoundPaneDriver
            .getRowByPartAndSerialNumber( PARENT_TRK_PART_NO, PARENT_TRK_SERIAL_NO );
      Assert.assertNotNull( "Didn't find expected inventory", lSearchResult );

      Assert.assertEquals( "Inventory Found but condition not as expected", "RFB",
            lSearchResult.getInvCondition() );
      lSearchResult.clickOemPartNo();
   }


   private void partNumberShowsRFBInventory() throws Throwable {
      checkOneRFBAtLocationInAvailabilityRows(
            iPartDetailsPageDriver.clickTabAvailability().getAvailabilityTableRows() );

      // Load the next page - Stock Details
      iPartDetailsPageDriver.clickTabDetails().clickStockNo();
   }


   private void stockNumberShowsRFBInventory() throws Throwable {
      checkOneRFBAtLocationInAvailabilityRows(
            iStockDetailsPageDriver.clickTabAvailability().getAvailabilityTableRows() );

      // Load the next page - Part Group details
      iStockDetailsPageDriver.clickOk();
      iPartDetailsPageDriver.clickTabAlternateParts().getAlternatePartsTableRows().get( 0 )
            .clickPartGroup();
   }


   private void partGroupShowsRFBInventory() throws Throwable {
      checkOneRFBAtLocationInAvailabilityRows(
            iPartGroupDetailsPageDriver.clickTabAvailability().getAvailabilityTableRows() );
   }


   private void
         checkOneRFBAtLocationInAvailabilityRows( List<AvailabilityTable> aAvailabilityTableRows ) {

      boolean lMyLocFound = false;
      for ( AvailabilityTable lAvailabilityTableRow : aAvailabilityTableRows ) {
         if ( lAvailabilityTableRow.getLocation().contains( INV_LOCATION ) ) {
            Assert.assertEquals( "RFB part expected in ".concat( INV_LOCATION ), "1",
                  lAvailabilityTableRow.getRFBCount() );
            lMyLocFound = true;
            break;
         }
      }

      Assert.assertTrue(
            "Part ".concat( PARENT_TRK_PART_NO ).concat( " not found in " ).concat( INV_LOCATION ),
            lMyLocFound );

   }
}
