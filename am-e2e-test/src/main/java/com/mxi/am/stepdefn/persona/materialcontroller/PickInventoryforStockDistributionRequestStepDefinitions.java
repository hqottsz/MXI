package com.mxi.am.stepdefn.persona.materialcontroller;

import static org.junit.Assert.assertTrue;

import javax.inject.Inject;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;

import com.mxi.am.driver.query.InventoryQueriesDriver;
import com.mxi.am.driver.web.AssetManagement;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.stockdistribution.PickStockDistRequestPageDriver;
import com.mxi.am.driver.web.stockdistribution.StockDistributionRequestDetailsPageDriver;
import com.mxi.am.driver.web.stockdistribution.requestdetailspanes.RequestDetailsPaneDriver;
import com.mxi.am.driver.web.stockdistribution.requestdetailspanes.RequestDetailsPaneDriver.PickedItems;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class PickInventoryforStockDistributionRequestStepDefinitions {

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private PickStockDistRequestPageDriver iPickStockDistRequestPageDriver;

   @Inject
   private StockDistributionRequestDetailsPageDriver iStockDistributionRequestDetailsPageDriver;

   private RequestDetailsPaneDriver iRequestDetailsPaneDriver;

   @Inject
   @AssetManagement
   private WebDriver iWebDriver;

   // We inserted a stock distribution request from sql script as
   // part of data setup because this way it save time to generate a request
   private final String REQUEST_BARCODE = "SR99Z999999Z";

   private final String REQUEST_OPEN_STATUS = "OPEN (Stock distribution request created.)";
   private final String REQUEST_COMPLETE_STATUS =
         "COMPLETED (Stock transferred to requesting location.)";
   private final String TRANSFER_COMPLETE_STATUS = "CMPLT (Complete)";
   private final String INV_SERIAL_NO = "Warehouse_SER_INV1";
   private final String INV_PART_NO = "WAREHOUSE-SER1";
   private static final long TimeOutInSeconds = 3;

   private final String iInventoryBarcode;


   // get the barcode of inventory
   @Inject
   public PickInventoryforStockDistributionRequestStepDefinitions(
         InventoryQueriesDriver aInventoryQueriesDriver) {
      iInventoryBarcode =
            aInventoryQueriesDriver.getBarcodeBySerialPartNo( INV_PART_NO, INV_SERIAL_NO );
   }


   @Given( "^I have a distribution request generated for a warehouse stock level$" )
   public void iHaveADistributionRequestGeneratedForAWarehouseStockLevel() throws Throwable {
      // Navigate to stock distribution request details page
      iNavigationDriver.barcodeSearch( REQUEST_BARCODE );

      // Note that the request now is OPEN status
      RequestDetailsPaneDriver iRequestDetailsPaneDriver =
            iStockDistributionRequestDetailsPageDriver.clickTabDetails();
      assertTrue( "The request is not OPEN, please check.",
            iRequestDetailsPaneDriver.getStatus().equals( REQUEST_OPEN_STATUS ) );

   }


   @When( "^I pick item for the distribution request$" )
   public void iGoToPickItemForTheDistributionRequest() throws Throwable {
      // Click Pick Item button to pick item
      iStockDistributionRequestDetailsPageDriver.clickPickItems();

      // On the pick item page, enter barcode of inventory
      iPickStockDistRequestPageDriver.setBarcode( iInventoryBarcode );

      // Click OK button to finish picking
      iPickStockDistRequestPageDriver.clickOKButton();
   }


   @Then( "^the request can be completed successfully$" )
   public void iAmAbleToPickSpecifiedItem() throws Throwable {
      // Click Complete Request button
      iStockDistributionRequestDetailsPageDriver.clickComplete();

      // Verify that request status is changed to Completed
      iStockDistributionRequestDetailsPageDriver.finishComplete( TimeOutInSeconds );

      RequestDetailsPaneDriver iRequestDetailsPaneDriver =
            iStockDistributionRequestDetailsPageDriver.clickTabDetails();
      assertTrue( "The request is not Complete, please check.",
            iRequestDetailsPaneDriver.getStatus().equals( REQUEST_COMPLETE_STATUS ) );

      // Verify the related transfer is complete too
      PickedItems lPickeditems =
            iRequestDetailsPaneDriver.getRowByPartNoAndSrialNo( INV_PART_NO, INV_SERIAL_NO );
      Assert.assertNotNull( "Didn't find expected transfer", lPickeditems );

      assertTrue( "The transfer is not Complete, please check.",
            lPickeditems.getTransferStatus().equals( TRANSFER_COMPLETE_STATUS ) );
   }

}
