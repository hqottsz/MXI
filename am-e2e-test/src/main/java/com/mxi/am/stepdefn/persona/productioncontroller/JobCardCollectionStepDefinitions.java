package com.mxi.am.stepdefn.persona.productioncontroller;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.inject.Inject;

import com.mxi.am.driver.query.SchedTaskQueriesDriver;
import com.mxi.am.driver.query.WorkPackageQueriesDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.include.task.CollectJobCardsPopupPageDriver;
import com.mxi.am.driver.web.task.CheckDetails.CheckDetailsPageDriver;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class JobCardCollectionStepDefinitions {

   @Inject
   private NavigationDriver navigationDriver;

   @Inject
   private CheckDetailsPageDriver checkDetailsPageDriver;

   @Inject
   private CollectJobCardsPopupPageDriver collectJobCardsPopupPageDriver;

   @Inject
   private WorkPackageQueriesDriver workPackageQueriesDriver;

   @Inject
   private SchedTaskQueriesDriver schedTaskQueriesDriver;

   private static final String AIRCRAFT_SN = "24838-01";
   private static final String WORK_PACKAGE = "WP24838-01";
   private static final String TASK_TO_COLLECT = "BM_SCH_TSK19";
   private static final String TASK_TO_UNCOLLECT = "BM_SCH_TSK20";


   @Given( "^a COMPLETE Task is uncollected$" )
   public void aCOMPLETETaskIsUncollected() throws Throwable {
      navigationDriver.barcodeSearch(
            workPackageQueriesDriver.getByWorkPackageName( WORK_PACKAGE ).getBarcode() );
      checkDetailsPageDriver.clickTabWorkscope();
      assertTrue( TASK_TO_COLLECT + " is not in work scope!",
            checkDetailsPageDriver.getTabWorkscope().taskExistInWorkScope( TASK_TO_COLLECT ) );
   }


   @When( "^I collect the Task$" )
   public void iCollectTheTask() throws Throwable {
      checkDetailsPageDriver.getTabWorkscope().clickCollect();
      collectJobCardsPopupPageDriver.scanBarcodeAndClose( schedTaskQueriesDriver
            .getByAircraftSerNoAndTaskCode( AIRCRAFT_SN, TASK_TO_COLLECT ).getBarcode() );
   }


   @Then( "^the Task is collected$" )
   public void theTaskIsCollected() throws Throwable {
      assertTrue( TASK_TO_COLLECT + " is not collected!",
            checkDetailsPageDriver.getTabWorkscope().isCollected( TASK_TO_COLLECT ) );
   }


   @Given( "^a COMPLETE Task is collected$" )
   public void aCOMPLETETaskIsCollected() throws Throwable {
      navigationDriver.barcodeSearch(
            workPackageQueriesDriver.getByWorkPackageName( WORK_PACKAGE ).getBarcode() );
      checkDetailsPageDriver.clickTabWorkscope();
      assertTrue( TASK_TO_UNCOLLECT + " is not in work scope!",
            checkDetailsPageDriver.getTabWorkscope().taskExistInWorkScope( TASK_TO_UNCOLLECT ) );
   }


   @When( "^I uncollect the Task$" )
   public void iUncollectTheTask() throws Throwable {
      checkDetailsPageDriver.getTabWorkscope().clickUncollect();
      collectJobCardsPopupPageDriver.scanBarcodeAndClose( schedTaskQueriesDriver
            .getByAircraftSerNoAndTaskCode( AIRCRAFT_SN, TASK_TO_UNCOLLECT ).getBarcode() );
   }


   @Then( "^the Task is uncollected$" )
   public void theTaskIsUncollected() throws Throwable {
      assertFalse( TASK_TO_UNCOLLECT + " is collected!",
            checkDetailsPageDriver.getTabWorkscope().isCollected( TASK_TO_UNCOLLECT ) );
   }

}
