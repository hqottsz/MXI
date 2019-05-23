package com.mxi.am.stepdefn.persona.materialcontroller;

import javax.inject.Inject;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;

import com.mxi.am.driver.web.AmWebConditions;
import com.mxi.am.driver.web.AssetManagement;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.stock.CreateStockLevelPageDriver;
import com.mxi.am.driver.web.stock.EditWarehouseStockLevelPageDriver;
import com.mxi.am.driver.web.stock.StockDetailsPageDriver;
import com.mxi.am.driver.web.stock.stocksearchpage.StockSearchPageDriver;
import com.mxi.am.driver.web.stock.stocksearchpage.stocksearchpanes.StocksFoundPaneDriver;
import com.mxi.am.driver.web.stock.stocksearchpage.stocksearchpanes.StocksFoundPaneDriver.StockSearchResult;
import com.mxi.am.driver.web.todolist.ToDoListPageDriver;
import com.mxi.am.driver.web.todolist.ToDoListPanes.DistributionRequestPaneDriver;
import com.mxi.driver.standard.Wait;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class GenerateStockDistributionRequestStepDefinitions {

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private StockSearchPageDriver iStockSearchPageDriver;

   @Inject
   private StockDetailsPageDriver iStockDetailsPageDriver;

   @Inject
   private CreateStockLevelPageDriver iCreateStockLevelPageDriver;

   @Inject
   private EditWarehouseStockLevelPageDriver iEditWarehouseStockLevelPageDriver;

   @Inject
   private ToDoListPageDriver iToDoListPageDriver;

   private DistributionRequestPaneDriver iDistributionRequestPaneDriver;

   @Inject
   @AssetManagement
   private WebDriver iWebDriver;

   private final String TECHNICAL_RECORDS_MENU = "Technical Records";
   private final String MATERIAL_CONTROLLER_MENU = "Material Controller";
   private final String SUB_MENU_1 = "Stock Search";
   private final String SUB_MENU_2 = "Control To Do List (Material Controller)";
   private final String STOCK_NO_1 = "Warehouse_Batch1_STOCK1";
   private final String STOCK_NO_NAME_1 = "Warehouse-Batch1 Stock NO";
   private final static String PAGE_NAME = "Stock Details";
   private final String WAREHOUSE_CODE_1 = "AIRPORT1/STORE";
   private final String RESTOCK_LEVEL_1 = "10";
   private final String MAX_LEVEL_1 = "25";
   private static final int TIMEOUT_IN_MILLISECONDS = 5 * 60 * 1000;
   private static final int THREE_SECONDS = 3 * 1000;
   private final int FIRST_ROW = 0;
   private final String EMPTY_REQUET_TAB = "There are no outstanding Stock Distribution Requests.";


   @Given( "^I go to Stock Search$" )
   public void iGoToStockSearch() throws Throwable {
      // Navigate to Stock Search page
      iNavigationDriver.navigate( TECHNICAL_RECORDS_MENU, SUB_MENU_1 );

      // Search the stock number
      iStockSearchPageDriver.clickClearAll();
      iStockSearchPageDriver.setStockCode( STOCK_NO_1 );
      iStockSearchPageDriver.clickSearch();

      // Click the hyperlink to navigate to Stock Details page
      StocksFoundPaneDriver iStocksFoundPaneDriver = iStockSearchPageDriver.clickTabStocksFound();

      StockSearchResult lSearchResult =
            iStocksFoundPaneDriver.getRowByStockCodeAndStockName( STOCK_NO_1, STOCK_NO_NAME_1 );
      Assert.assertNotNull( "Didn't find expected stock", lSearchResult );

      lSearchResult.clickStock();
   }


   @Given( "^I have a Stock Number set up with a warehouse stock level and the stock low action is Distribution Request$" )
   public void
         iHaveAStockNumberSetUpWithAWarehouseStockLevelAndTheStockLowActionIsDistributionRequest()
               throws Throwable {
      // Go to Warehouse Stock Levels tab to create Warehouse Stock Level for the specific warehouse
      iStockDetailsPageDriver.clickTabWarehouseStockLevels().clickCreateStockLevel();

      // Enter warehouse location
      iCreateStockLevelPageDriver.setLocationCode( WAREHOUSE_CODE_1 );
      iCreateStockLevelPageDriver.clickOk();

      // Edit the warehouse stock level to set up restock level and stock low action
      // Wait until the page is loaded completely
      Wait.until( AmWebConditions.isOnPage( iWebDriver, PAGE_NAME ), TIMEOUT_IN_MILLISECONDS );

      // Click Edit Stock Level button
      iStockDetailsPageDriver.clickTabWarehouseStockLevels().clickEditStockLevel();

      // Set up restock level 10, max level 25 and DISTREQ as stock low action

      iEditWarehouseStockLevelPageDriver.getStockLevelLine( FIRST_ROW )
            .setRestockLevel( RESTOCK_LEVEL_1 );
      iEditWarehouseStockLevelPageDriver.getStockLevelLine( FIRST_ROW ).setMaxLevel( MAX_LEVEL_1 );
      iEditWarehouseStockLevelPageDriver.getStockLevelLine( FIRST_ROW ).setStockLowDistreq();

      iEditWarehouseStockLevelPageDriver.clickOk();
   }


   @When( "^the warehouse stock is below restock level$" )
   public void theWarehouseStockIsBelowRestockLevel() throws Throwable {
      // Wait until we are back to stock details page
      Wait.until( AmWebConditions.isOnPage( iWebDriver, PAGE_NAME ), TIMEOUT_IN_MILLISECONDS );

      // navigate to material controller to do list
      iNavigationDriver.navigate( MATERIAL_CONTROLLER_MENU, SUB_MENU_2 );

   }


   @Then( "^a distribution request is generated for the warehouse stock level$" )
   public void aDistributionRequestIsGeneratedForTheWarehouseStockLevel() throws Throwable {

      // Go to Stock Distribution Request to do list
      iDistributionRequestPaneDriver = iToDoListPageDriver.clickTabStockDistributionRequest();

      // refresh the to do list page to get stock distribution request appear
      long lTimeout = System.currentTimeMillis() + TIMEOUT_IN_MILLISECONDS;
      do {
         // Pause, reload the page, and check again (if not timed out).
         Wait.pause( THREE_SECONDS );
         iNavigationDriver.refreshPage();

         if ( !iWebDriver.getPageSource().contains( EMPTY_REQUET_TAB ) ) {
            break;
         }

      } while ( System.currentTimeMillis() < lTimeout );

      if ( System.currentTimeMillis() >= lTimeout ) {
         Assert.fail(
               "Timed out waiting for stock distribution request to be shown up on the to do list. Waited "
                     + TIMEOUT_IN_MILLISECONDS + " milliseconds." );

      }
      // On the to do list page, we need to verify the stock distribution request is generated
      // and the quantity is correct, note the MAX_LEVEL_1 should be the requested quantity
      // in our test case

      Assert.assertTrue( "The Stock No is not in Stock Distribution Request tab",
            iDistributionRequestPaneDriver.getDistributionRequestListTableRow( FIRST_ROW )
                  .getStockNo().equals( STOCK_NO_1 ) );

      Assert.assertTrue(
            "The requested quantity is not correct in Stock Distribution Request tab for the specific request",
            iDistributionRequestPaneDriver.getDistributionRequestListTableRow( FIRST_ROW ).getQTY()
                  .contains( MAX_LEVEL_1 ) );

   }
}
