
package com.mxi.am.stepdefn.persona.techrecords;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Assert;

import com.mxi.am.driver.web.LogoutPageDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.task.CheckDetails.CheckDetailsPageDriver;
import com.mxi.am.driver.web.task.tasksearchpage.TaskSearchPageDriver;
import com.mxi.am.driver.web.usage.editusagesnapshotpage.EditUsageSnapshotPageDriver;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;


/**
 * Edit Usage Snapshot of Historical Work Package Step Definition
 */
@ScenarioScoped
public class EditUsageSnapshotOfHistoricalWorkPackageStepDefinition {

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private TaskSearchPageDriver iTaskSearchPageDriver;

   @Inject
   private CheckDetailsPageDriver iCheckDetailsPageDriver;

   @Inject
   private EditUsageSnapshotPageDriver iEditUsageSnapshotPageDriver;

   @Inject
   private LogoutPageDriver iLogoutDriver;

   // Pre-loaded data in Database
   private final String iWorkPackageName = "EUSS-WP";

   // Setup
   SimpleDateFormat format = new SimpleDateFormat( "dd-MMM-yyyy" );

   // Test data
   private final String iInventory = "Aircraft Part 1 - EUSS-AC1 [Task]";
   private final String iUsageParm = "Flying Hours ( HOURS )";
   private final String iValue = "310";
   private final String iPageTitle = "Work Package Details";

   // Date values as strings to search between.
   private final String iAfterDate = "31-DEC-2016";
   private final String iBeforeDate = format.format( new Date(
         System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert( 1, TimeUnit.DAYS ) ) );

   private final Date iDate = new Date();


   @Given( "^I have a historical work package$" )
   public void iHaveAHistoricalWorkPackage() throws Throwable {
      /**
       * Work package and support data set up in:
       *
       * <pre>
       *    C_RI_ATTACH.csv
       *    C_RI_INVENTORY.csv
       *    C_RI_Inventory_Usages.csv
       *    C_WORK_PACKAGE.csv
       *    from
       *    am-e2e-test\src\main\data\actuals\persona\techRecords\EditUsageSnapshotOfHistoricalWorkPackage
       * </pre>
       *
       * As well as in:
       *
       * <pre>
       *    am-e2e-test\src\main\data\sql\persona\techRecords\EditUsageSnapShotOfHistoricWorkPackage.sql
       * </pre>
       */
   }


   @When( "^I edit the usage snapshot$" )
   public void iEditTheUsageSnapshot() throws Throwable {
      iNavigationDriver.navigate( "Technical Records", "Task Search" );
      iTaskSearchPageDriver.setWorkPackageName( iWorkPackageName );
      iTaskSearchPageDriver.setHistoricSelect( "HIST" );
      iTaskSearchPageDriver.setCompletedAfterAndCompletedBeforeDates( iAfterDate, iBeforeDate );
      iTaskSearchPageDriver.clickSearch();
      iTaskSearchPageDriver.clickTabTasksWorkPackagesFound().getSearchResultRow( iWorkPackageName )
            .clickTaskName();
      iCheckDetailsPageDriver.clickTabDetails().clickEditUsageSnapshot();
      iEditUsageSnapshotPageDriver.setCompletionDateAndTime( iDate );
      iEditUsageSnapshotPageDriver.setUsageSnapshotTsn( iInventory, iUsageParm, iValue );
      iEditUsageSnapshotPageDriver.clickOK();

   }


   @Then( "^the usage snapshot is updated$" )
   public void theUsageSnapshotIsUpdated() throws Throwable {
      Assert.assertTrue( iCheckDetailsPageDriver.getPageTitle().equals( iPageTitle ) );
      // Set Seconds and Milliseconds part of iDate to zero
      Date lDate = DateUtils.setSeconds( iDate, 0 );
      lDate = DateUtils.setMilliseconds( lDate, 0 );
      Assert.assertTrue(
            iCheckDetailsPageDriver.clickTabDetails().getActualEndDate().equals( lDate ) );
      Assert.assertTrue( iCheckDetailsPageDriver.clickTabDetails()
            .getAssemblyUsageTsn( iInventory, iUsageParm ).equals( iValue ) );
      iNavigationDriver.clickLogout();
      iLogoutDriver.clickOK();

   }
}
