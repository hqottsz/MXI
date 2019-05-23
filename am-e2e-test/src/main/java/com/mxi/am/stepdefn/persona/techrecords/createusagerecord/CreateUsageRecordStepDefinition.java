package com.mxi.am.stepdefn.persona.techrecords.createusagerecord;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;

import com.mxi.am.driver.common.database.DatabaseDriver;
import com.mxi.am.driver.query.InventoryInfo;
import com.mxi.am.driver.query.InventoryQueriesDriver;
import com.mxi.am.driver.web.AssetManagement;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.inventory.inventorydetailspage.InventoryDetailsPageDriver;
import com.mxi.am.driver.web.inventory.inventorydetailspage.inventorydetailspanes.historicalpanes.UsagePaneDriver;
import com.mxi.am.driver.web.inventory.inventorydetailspage.inventorydetailspanes.historicalpanes.UsagePaneDriver.UsageRecords;
import com.mxi.am.driver.web.inventory.inventorysearchpage.InventorySearchPageDriver;
import com.mxi.am.driver.web.inventory.inventorysearchpage.inventorysearchpanes.InventoryFoundPaneDriver.InventorySearchResult;
import com.mxi.am.driver.web.usage.CreateUsageRecordPageDriver;
import com.mxi.am.driver.web.usage.UsageRecordDetailsPageDriver;
import com.mxi.am.driver.web.usage.UsageRecordDetailsPageDriver.UsageAccrued;
import com.mxi.am.helper.FilterCriteria;
import com.mxi.am.helper.Selector;
import com.mxi.am.stepdefn.persona.techrecords.createusagerecord.data.CreateUsageRecordData;

import cucumber.api.java.After;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;


/**
 * Create Usage Record Step Definition
 */
@ScenarioScoped
public class CreateUsageRecordStepDefinition {

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private InventorySearchPageDriver iInventorySearchPageDriver;

   @Inject
   private InventoryDetailsPageDriver iInventoryDetailsPageDriver;

   @Inject
   private CreateUsageRecordPageDriver iCreateUsageRecordPageDriver;

   @Inject
   UsageRecordDetailsPageDriver iUsageRecordDetailsPageDriver;

   @Inject
   @AssetManagement
   public DatabaseDriver iDatabaseDriver;

   private final BigDecimal iApuInventoryDbId;
   private final BigDecimal iApuInventoryId;
   private static final String APU_DESCRIPTION = "Part Name - APU (PN: APU_ASSY_PN1, SN: CUR-001)";
   private static final String APUH_AT_READING = "APUH_AT_READING (APUH_AT_READING)";


   @Inject
   public CreateUsageRecordStepDefinition(InventoryQueriesDriver aInventoryQueriesDriver) {
      final InventoryInfo lInventoryInfo = aInventoryQueriesDriver
            .getInventoryInfoByInventorySerialNo( CreateUsageRecordData.APU_INVENTORY_SERIAL_NO );
      iApuInventoryDbId = lInventoryInfo.getDbId();
      iApuInventoryId = lInventoryInfo.getId();
   }


   @When( "^I create the usage record for an assembly$" )
   public void iCreateTheUsageRecord() {
      final Calendar lCalendar = Calendar.getInstance();
      lCalendar.add( Calendar.DAY_OF_YEAR, -20 );

      final String lUsageDateString =
            CreateUsageRecordData.DATE_FORMAT.format( lCalendar.getTime() );
      displayInventoryDetails( CreateUsageRecordData.APU_PART_NUMBER,
            CreateUsageRecordData.APU_INVENTORY_SERIAL_NO );
      iInventoryDetailsPageDriver.clickTabHistorical().clickTabUsage().clickCreateUsageRecord();
      iCreateUsageRecordPageDriver.setRecordName( CreateUsageRecordData.USAGE_RECORD_NAME );
      iCreateUsageRecordPageDriver.setRecordDate( lUsageDateString );
      iCreateUsageRecordPageDriver.setRecordTime( CreateUsageRecordData.USAGE_TIME_STRING );
      iCreateUsageRecordPageDriver.setTSNUsageByID(
            CreateUsageRecordData.EXPECTED_USAGE_TSN_VALUE.toString(),
            CreateUsageRecordData.USAGE_PARAMETER, iApuInventoryDbId, iApuInventoryId );

      iCreateUsageRecordPageDriver.clickOK();

   }


   @Then( "^the usage record is created persistently$" )
   public void theUsageRecordIsCreatedCorrectly() {
      Assert.assertTrue(
            iInventoryDetailsPageDriver.getPageTitle().equals( CreateUsageRecordData.PAGE_TITLE ) );

      final UsagePaneDriver lUsagePaneDriver =
            iInventoryDetailsPageDriver.getTabHistorical().getTabUsage();

      Selector
            .selectFirst( lUsagePaneDriver.getRecords(),
                  withUsageRecordName( CreateUsageRecordData.USAGE_RECORD_NAME ) )
            .clickRecordLink();

      final BigDecimal lTsnHours =
            new BigDecimal( iUsageRecordDetailsPageDriver.getAssemblyCurrentUsage( APU_DESCRIPTION,
                  APUH_AT_READING, CreateUsageRecordData.COLUMN_TSN ) );

      final BigDecimal lDeltaHours =
            new BigDecimal( iUsageRecordDetailsPageDriver.getAssemblyCurrentUsage( APU_DESCRIPTION,
                  APUH_AT_READING, CreateUsageRecordData.COLUMN_DELTA ) );

      Assert.assertTrue(
            CreateUsageRecordData.EXPECTED_USAGE_TSN_VALUE.compareTo( lTsnHours ) == 0 );

      Assert.assertTrue(
            CreateUsageRecordData.EXPECTED_USAGE_DELTA_VALUE.compareTo( lDeltaHours ) == 0 );

   }


   @After( "@Create-Usage-Record-Success" )
   public void teardown() {
      // clean data created during this scenario
      Assert.assertTrue( iDatabaseDriver.update( CreateUsageRecordData.UPDATE_INV_CURR_USAGE,
            CreateUsageRecordData.APU_INVENTORY_SERIAL_NO,
            CreateUsageRecordData.USAGE_PARAMETER ) == 1 );
      Assert.assertTrue( iDatabaseDriver.update( CreateUsageRecordData.DELETE_USAG_USAGE_DATA,
            CreateUsageRecordData.USAGE_RECORD_NAME ) == 8 );
      Assert.assertTrue( iDatabaseDriver.update( CreateUsageRecordData.DELETE_USAG_USAGE_RECORD,
            CreateUsageRecordData.USAGE_RECORD_NAME ) == 1 );
   }


   private void displayInventoryDetails( final String aOEMPartNo, final String aSerialNoBatchNo ) {

      iNavigationDriver.navigate( CreateUsageRecordData.TECHNICAL_RECORDS,
            CreateUsageRecordData.INVENTORY_SEARCH );
      iInventorySearchPageDriver.clearAll();
      iInventorySearchPageDriver.setOEMPartNo( aOEMPartNo );
      iInventorySearchPageDriver.setSerialNoBatchNo( aSerialNoBatchNo );
      iInventorySearchPageDriver.clickSearch();
      final List<InventorySearchResult> lResults =
            iInventorySearchPageDriver.clickTabInventoryFound().getResults();
      for ( InventorySearchResult lResult : lResults ) {
         if ( aOEMPartNo.equals( lResult.getOemPartNo() )
               && aSerialNoBatchNo.equals( lResult.getSerialNoBatchNo() ) ) {
            lResult.clickSerialNoBatchNo();
         }
      }
   }


   private FilterCriteria<UsageRecords> withUsageRecordName( final String aUsageName ) {
      return new FilterCriteria<UsageRecords>() {

         @Override
         public boolean test( UsageRecords aUsageRecord ) {
            return aUsageRecord.getName().equals( aUsageName );
         }
      };
   }


   private FilterCriteria<UsageAccrued> withUsageAccruedParm( final String aParmName ) {
      return new FilterCriteria<UsageAccrued>() {

         @Override
         public boolean test( UsageAccrued aUsageAccruedRow ) {
            return aUsageAccruedRow.getParm().startsWith( aParmName );
         }
      };
   }
}
