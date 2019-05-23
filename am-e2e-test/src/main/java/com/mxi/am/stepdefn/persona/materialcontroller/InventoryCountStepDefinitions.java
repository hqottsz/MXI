package com.mxi.am.stepdefn.persona.materialcontroller;

import javax.inject.Inject;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;

import com.mxi.am.driver.query.InventoryCountQueriesDriver;
import com.mxi.am.driver.web.AssetManagement;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.inventory.InventoryCountPageDriver;
import com.mxi.am.driver.web.todolist.ToDoListPageDriver;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class InventoryCountStepDefinitions {

   @Inject
   @AssetManagement
   private WebDriver iWebDriver;

   private static final String STOREROOM_CLERK = "Storeroom Clerk";
   private static final String STOREROOM_CLERK_TODO_LIST = "To Do List (Storeroom Clerk)";
   private static final String AIRPORT = "AIRPORT3";
   private static final String AD_HOC_BIN = "AIRPORT3/STORE/BIN3-2";
   private static final String CYCLE_COUNT_BIN = "AIRPORT1/STORE/BIN1-1";
   private static final String AD_HOC_PART = "A0000999";
   private static final String CYCLE_COUNT_PART = "PART4188";
   private static final int AD_HOC_ACTUAL_COUNT = 1;
   private static final int CYCLE_COUNT_ACTUAL_COUNT = 5;

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private ToDoListPageDriver iToDoListPageDriver;

   @Inject
   private InventoryCountPageDriver iInventoryCountPageDriver;

   @Inject
   private InventoryCountQueriesDriver inventoryCountQueriesDriver;


   @And( "^I locate a bin location on Inventory Count page$" )
   public void iLocationABinLocationOnInventoryCountPage() throws Throwable {
      iNavigationDriver.navigate( STOREROOM_CLERK, STOREROOM_CLERK_TODO_LIST );
      iToDoListPageDriver.clickInventoryCount();
      iInventoryCountPageDriver.clickAdHocCountTab();
      iInventoryCountPageDriver.enterBinLocation( AIRPORT );
      iInventoryCountPageDriver.clickSearch();
      iInventoryCountPageDriver.clickBinByLocation( 1 );

      Assert.assertTrue(
            iInventoryCountPageDriver.getLocationCardHeader( 1 ).contains( AD_HOC_BIN ) );
   }


   @When( "^An unexpected part is found$" )
   public void anUnexpectPartIsFound() throws Throwable {
      iInventoryCountPageDriver.clickFoundUnexpectedPart();
   }


   @And( "^I am able to add it to the list$" )
   public void iAmAbleToAddItToTheList() throws Throwable {
      iInventoryCountPageDriver.enterPartNumber( AD_HOC_PART );
      iInventoryCountPageDriver.clickAddPart();
      Assert.assertTrue( iInventoryCountPageDriver.getPartNumber( 1, 0 ).contains( AD_HOC_PART ) );
      Assert.assertTrue( iInventoryCountPageDriver.getToastMessage()
            .contains( "The unexpected part is added to the end of the list." ) );
   }


   @Then( "^I submit the quantity for this part$" )
   public void submitTheQuantityForThisPart() throws Throwable {
      iInventoryCountPageDriver.clickSubmit();
      Assert.assertEquals( AD_HOC_ACTUAL_COUNT,
            inventoryCountQueriesDriver.getActualCount( AD_HOC_BIN, AD_HOC_PART ).intValue() );
   }


   @When( "^I count parts for a bin location on Inventory Count | Cycle Count tab$" )
   public void iCountPartsForABinLocationOnInventoryCountCycleCountTab() throws Throwable {
      iNavigationDriver.navigate( STOREROOM_CLERK, STOREROOM_CLERK_TODO_LIST );
      iToDoListPageDriver.clickInventoryCount();
      iInventoryCountPageDriver.clickCycleCountTab();

      Assert.assertTrue(
            iInventoryCountPageDriver.getLocationCardHeader( 0 ).contains( CYCLE_COUNT_BIN ) );
      iInventoryCountPageDriver.clickIncreaseQuantity();
   }


   @Then( "^I submit the quantity for these parts$" )
   public void iSubmitTheQuantityForTheseParts() throws Throwable {
      iInventoryCountPageDriver.clickIncreaseQuantityXTimes( 5 );
      iInventoryCountPageDriver.clickSubmit();
      Assert.assertEquals( CYCLE_COUNT_ACTUAL_COUNT, inventoryCountQueriesDriver
            .getActualCount( CYCLE_COUNT_BIN, CYCLE_COUNT_PART ).intValue() );
   }

}
