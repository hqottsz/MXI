package com.mxi.am.stepdefn.persona.techrecords;

import java.math.BigDecimal;
import java.util.Date;

import javax.inject.Inject;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Assert;

import com.mxi.am.driver.query.InventoryQueriesDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.flight.CompleteFlightPageDriver;
import com.mxi.am.driver.web.flight.FlightSearchPageDriver;
import com.mxi.am.driver.web.flight.flightDetails.FlightDetailsPageDriver;
import com.mxi.am.driver.web.flight.flightDetails.flightPanes.FlightInformationPaneDriver;
import com.mxi.am.driver.web.inventory.inventorydetailspage.InventoryDetailsPageDriver;
import com.mxi.am.driver.web.inventory.inventorydetailspage.inventorydetailspanes.DetailsPaneDriver;
import com.mxi.am.stepdefn.utility.FlightAPIIntegrationHelperV2;
import com.mxi.am.stepdefn.utility.FlightInfo;
import com.mxi.driver.standard.Wait;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;


/**
 * Step Definitions for CompletePendingFlight
 */
@ScenarioScoped
public class CompletePendingFlightStepDefinitions {

   // note for the pre-loaded data is from C_RI_ATTACH, C_RI_INVENTORY and C_RI_INVENTORY_Usage CSV
   // files under \am-e2e-test\src\main\data\actuals\persona\techRecords\CompletePendingFlight
   private final String AIRCRAFT_REG_CODE = "CPF-SN1";
   private final String MASTER_FLIGHT_NAME1 = "CompletePendingFlight1";
   private final String MASTER_FLIGHT_NAME2 = "CompletePendingFlight2";
   private String iFlightName1;
   private String iFlightName2;
   private static final String AC_DECRIPTION = "Aircraft Part 2 - CPF-SN1";
   private static final String ENGINE_DESCRIPTION1 =
         "(1) Part Name - Engine (PN: ENG_ASSY_PN1, SN: CompletePendFlight-ENG-SN1)";
   private static final String ENGINE_DESCRIPTION2 =
         "(2) Part Name - Engine (PN: ENG_ASSY_PN1, SN: CompletePendFlight-ENG-SN2)";
   private static final String APU_DESCRIPTION =
         "Part Name - APU (PN: APU_ASSY_PN1, SN: CompletePendFlight-APU-SN1)";
   private static final String CYCLES = "CYCLES";
   private static final String HOURS = "HOURS";
   private static final String ECYC = "ECYC";
   private static final String EOT = "EOT";
   private static final String ACYC = "ACYC";
   private static final String AOT = "AOT";
   private static final String HOURS_FLYING_HOURS = "HOURS (Flying Hours)";
   private static final String CYCLES_CYCLES = "CYCLES (Cycles)";
   private static final String ECYC_ENGING_CYCLES = "ECYC (Engine Cycles)";
   private static final String EOT_ENGING_OPERATING_TIME = "EOT (Engine Operating Time)";
   private static final String ACYC_APU_CYCLES = "ACYC (APU Cycles)";
   private static final String AOT_APU_OPERATING_TIME = "AOT (APU Operating Time )";

   private static final BigDecimal HOURS_DELTA = new BigDecimal( 4 );
   private static final BigDecimal CYCLE_DELTA = new BigDecimal( 1 );

   private static final Date FLIGHT_DATE_FIVE_DAYS_AGO = DateUtils.addDays( new Date(), -5 );
   private static final Date FLIGHT_DATE_TEN_DAYS_AGO = DateUtils.addDays( new Date(), -10 );
   private static final long FLIGHT_DURATION_FOUR_HOURS = 4;

   private Usages iExpectedCurrentUsage;
   private Usages iExpectedFlightUsage;

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private FlightSearchPageDriver iFlightSearchPageDriver;

   @Inject
   private FlightDetailsPageDriver iFlightDetailsPageDriver;

   @Inject
   private CompleteFlightPageDriver iCompleteFlightPageDriver;

   @Inject
   private InventoryDetailsPageDriver iInventoryDetailsPageDriver;

   @Inject
   private FlightAPIIntegrationHelperV2 iFltAPIHelper;

   private final String iAircraftBarcode;


   @Inject
   public CompletePendingFlightStepDefinitions(InventoryQueriesDriver aInventoryQueriesDriver) {
      iAircraftBarcode =
            aInventoryQueriesDriver.getByAircraftRegistrationCode( AIRCRAFT_REG_CODE ).getBarcode();
   }


   @Given( "^an aircraft is tracking flight usages and it has had a planned flight completed which was accruing the same usages$" )
   public void iOperationACandaPendingFlightCompleted() throws Throwable {

      // Create a planned flight using integration message
      //
      // Note: keep the flight within the 60 day "show flights" window.

      FlightInfo lFlightInfo = iFltAPIHelper.createPlannedFlight( MASTER_FLIGHT_NAME1,
            AIRCRAFT_REG_CODE, FLIGHT_DATE_FIVE_DAYS_AGO, FLIGHT_DURATION_FOUR_HOURS );

      iFlightName1 = lFlightInfo.getFltName();

      final String lRequestId = iFltAPIHelper.sendFlightRequestMessage( lFlightInfo );

      // Wait until successful or 10 seconds.
      Wait.until( iFltAPIHelper.getIntegrationConditionFactory().isSuccessful( lRequestId ),
            10000L );

      // go to search the flight then navigate to flight details
      iNavigationDriver.navigate( "Line Planner", "Flight Search" );
      iFlightSearchPageDriver.setFlightName( iFlightName1 );
      iFlightSearchPageDriver.clickSearch();
      iFlightSearchPageDriver.clickFlightLinkInTable( iFlightName1 );

      // click Complete Flight button to complete the flight
      iFlightDetailsPageDriver.clickCompleteFlight();

      // enter usages for sub-assemblies then click ok to save the flight
      iCompleteFlightPageDriver.setAccruedUsageDelta( ENGINE_DESCRIPTION1, ECYC, CYCLE_DELTA );
      iCompleteFlightPageDriver.setAccruedUsageDelta( ENGINE_DESCRIPTION1, EOT, HOURS_DELTA );
      iCompleteFlightPageDriver.setAccruedUsageDelta( ENGINE_DESCRIPTION2, ECYC, CYCLE_DELTA );
      iCompleteFlightPageDriver.setAccruedUsageDelta( ENGINE_DESCRIPTION2, EOT, HOURS_DELTA );
      iCompleteFlightPageDriver.setAccruedUsageDelta( APU_DESCRIPTION, ACYC, CYCLE_DELTA );
      iCompleteFlightPageDriver.setAccruedUsageDelta( APU_DESCRIPTION, AOT, HOURS_DELTA );

      iCompleteFlightPageDriver.clickOK();
   }


   @When( "^I complete another planned flight accruing the same usages and its arrival date is before the other flight$" )
   public void iCompleteAnotherHistoricalPendingFlight() throws Throwable {

      // Generate the new expected current usage by applying the deltas to the current inventory
      // usages
      iExpectedCurrentUsage = getInventoryUsages();
      iExpectedCurrentUsage.addAircraftHours( HOURS_DELTA );
      iExpectedCurrentUsage.addAircraftCycles( CYCLE_DELTA );
      iExpectedCurrentUsage.addEngineHours( HOURS_DELTA );
      iExpectedCurrentUsage.addEngineCycles( CYCLE_DELTA );
      iExpectedCurrentUsage.addEngineEcyc( CYCLE_DELTA );
      iExpectedCurrentUsage.addEngineEot( HOURS_DELTA );
      iExpectedCurrentUsage.addAPUHours( HOURS_DELTA );
      iExpectedCurrentUsage.addAPUCycles( CYCLE_DELTA );
      iExpectedCurrentUsage.addAPUAcyc( CYCLE_DELTA );
      iExpectedCurrentUsage.addAPUAot( HOURS_DELTA );

      // go to the flight details page from the latest flight created above
      iInventoryDetailsPageDriver.clickTabHistorical().clickTabFlight()
            .clickFlightName( iFlightName1 );

      // Generate the new expected existing flight's usages by applying the deltas to the current
      // flight usages
      iExpectedFlightUsage = getFlightUsages();
      iExpectedFlightUsage.addAircraftHours( HOURS_DELTA );
      iExpectedFlightUsage.addAircraftCycles( CYCLE_DELTA );
      iExpectedFlightUsage.addEngineHours( HOURS_DELTA );
      iExpectedFlightUsage.addEngineCycles( CYCLE_DELTA );
      iExpectedFlightUsage.addEngineEcyc( CYCLE_DELTA );
      iExpectedFlightUsage.addEngineEot( HOURS_DELTA );
      iExpectedFlightUsage.addAPUHours( HOURS_DELTA );
      iExpectedFlightUsage.addAPUCycles( CYCLE_DELTA );
      iExpectedFlightUsage.addAPUAcyc( CYCLE_DELTA );
      iExpectedFlightUsage.addAPUAot( HOURS_DELTA );

      iFlightDetailsPageDriver.clickClose();

      // Create a historical planned flight for the aircraft, the date is earlier than the first
      // flight we created above
      //
      // Note: keep the flight within the 60 day "show flights" window.
      FlightInfo lFlightInfo = iFltAPIHelper.createPlannedFlight( MASTER_FLIGHT_NAME2,
            AIRCRAFT_REG_CODE, FLIGHT_DATE_TEN_DAYS_AGO, FLIGHT_DURATION_FOUR_HOURS );

      iFlightName2 = lFlightInfo.getFltName();

      final String lRequestId = iFltAPIHelper.sendFlightRequestMessage( lFlightInfo );

      // Wait until successful or 10 seconds.
      Wait.until( iFltAPIHelper.getIntegrationConditionFactory().isSuccessful( lRequestId ),
            10000L );

      // go to search the flight then navigate to flight details
      iNavigationDriver.navigate( "Line Planner", "Flight Search" );
      iFlightSearchPageDriver.setFlightName( iFlightName2 );
      iFlightSearchPageDriver.clickSearch();
      iFlightSearchPageDriver.clickFlightLinkInTable( iFlightName2 );

      // click Complete Flight button to complete the flight
      iFlightDetailsPageDriver.clickCompleteFlight();

      // enter usages for sub-assemblies then click ok to save the flight
      iCompleteFlightPageDriver.setAccruedUsageDelta( ENGINE_DESCRIPTION1, ECYC, CYCLE_DELTA );
      iCompleteFlightPageDriver.setAccruedUsageDelta( ENGINE_DESCRIPTION1, EOT, HOURS_DELTA );
      iCompleteFlightPageDriver.setAccruedUsageDelta( ENGINE_DESCRIPTION2, ECYC, CYCLE_DELTA );
      iCompleteFlightPageDriver.setAccruedUsageDelta( ENGINE_DESCRIPTION2, EOT, HOURS_DELTA );
      iCompleteFlightPageDriver.setAccruedUsageDelta( APU_DESCRIPTION, ACYC, CYCLE_DELTA );
      iCompleteFlightPageDriver.setAccruedUsageDelta( APU_DESCRIPTION, AOT, HOURS_DELTA );

      iCompleteFlightPageDriver.clickOK();

   }


   @Then( "^the aircraft's and installed assemblies' current TSN usages are incremented by the flight usage provided$" )
   public void theACandAssemblyUsageUpdated() throws Throwable {

      // Assert the inventory current usage values match the expected values.
      getInventoryUsages().assetEquals( iExpectedCurrentUsage );
   }


   @And( "^the subsequent flight's TSN usages are incremented by the flight usage provided$" )
   public void subsequentFlightAfterGetUsagesUpdated() throws Throwable {

      // Go to flight tab and locate the previous flight
      iInventoryDetailsPageDriver.clickTabHistorical().clickTabFlight()
            .clickFlightName( iFlightName1 );;

      // Assert the previous usage values match the expected values.
      getFlightUsages().assetEquals( iExpectedFlightUsage );
   }


   private Usages getFlightUsages() {
      Usages lUsages = new Usages();

      FlightInformationPaneDriver lFlightInfoPaneDriver =
            iFlightDetailsPageDriver.clickTabFlightInformation();

      lUsages.iAircraftHoursTsn = new BigDecimal(
            lFlightInfoPaneDriver.getAssemblyAccruedTsn( AC_DECRIPTION, HOURS_FLYING_HOURS ) );
      lUsages.iAircraftCyclesTsn = new BigDecimal(
            lFlightInfoPaneDriver.getAssemblyAccruedTsn( AC_DECRIPTION, CYCLES_CYCLES ) );
      lUsages.iEngineHoursTsn = new BigDecimal( lFlightInfoPaneDriver
            .getAssemblyAccruedTsn( ENGINE_DESCRIPTION1, HOURS_FLYING_HOURS ) );
      lUsages.iEngineCyclesTsn = new BigDecimal(
            lFlightInfoPaneDriver.getAssemblyAccruedTsn( ENGINE_DESCRIPTION1, CYCLES_CYCLES ) );
      lUsages.iEngineEcycTsn = new BigDecimal( lFlightInfoPaneDriver
            .getAssemblyAccruedTsn( ENGINE_DESCRIPTION1, ECYC_ENGING_CYCLES ) );
      lUsages.iEngineEotTsn = new BigDecimal( lFlightInfoPaneDriver
            .getAssemblyAccruedTsn( ENGINE_DESCRIPTION1, EOT_ENGING_OPERATING_TIME ) );
      lUsages.iAPUHoursTsn = new BigDecimal(
            lFlightInfoPaneDriver.getAssemblyAccruedTsn( APU_DESCRIPTION, HOURS_FLYING_HOURS ) );
      lUsages.iAPUCyclesTsn = new BigDecimal(
            lFlightInfoPaneDriver.getAssemblyAccruedTsn( APU_DESCRIPTION, CYCLES_CYCLES ) );
      lUsages.iAPUAcycTsn = new BigDecimal(
            lFlightInfoPaneDriver.getAssemblyAccruedTsn( APU_DESCRIPTION, ACYC_APU_CYCLES ) );
      lUsages.iAPUAotTsn = new BigDecimal( lFlightInfoPaneDriver
            .getAssemblyAccruedTsn( APU_DESCRIPTION, AOT_APU_OPERATING_TIME ) );

      return lUsages;
   }


   private Usages getInventoryUsages() {

      Usages lUsages = new Usages();

      iNavigationDriver.barcodeSearch( iAircraftBarcode );
      DetailsPaneDriver lDetailsPaneDriver = iInventoryDetailsPageDriver.clickTabDetails();

      lUsages.iAircraftHoursTsn =
            new BigDecimal( lDetailsPaneDriver.getAssemblyCurrentTsn( AC_DECRIPTION, HOURS ) );
      lUsages.iAircraftCyclesTsn =
            new BigDecimal( lDetailsPaneDriver.getAssemblyCurrentTsn( AC_DECRIPTION, CYCLES ) );
      lUsages.iEngineHoursTsn = new BigDecimal(
            lDetailsPaneDriver.getAssemblyCurrentTsn( ENGINE_DESCRIPTION1, HOURS ) );
      lUsages.iEngineCyclesTsn = new BigDecimal(
            lDetailsPaneDriver.getAssemblyCurrentTsn( ENGINE_DESCRIPTION1, CYCLES ) );
      lUsages.iEngineEcycTsn =
            new BigDecimal( lDetailsPaneDriver.getAssemblyCurrentTsn( ENGINE_DESCRIPTION1, ECYC ) );
      lUsages.iEngineEotTsn =
            new BigDecimal( lDetailsPaneDriver.getAssemblyCurrentTsn( ENGINE_DESCRIPTION1, EOT ) );
      lUsages.iAPUHoursTsn =
            new BigDecimal( lDetailsPaneDriver.getAssemblyCurrentTsn( APU_DESCRIPTION, HOURS ) );
      lUsages.iAPUCyclesTsn =
            new BigDecimal( lDetailsPaneDriver.getAssemblyCurrentTsn( APU_DESCRIPTION, CYCLES ) );
      lUsages.iAPUAcycTsn =
            new BigDecimal( lDetailsPaneDriver.getAssemblyCurrentTsn( APU_DESCRIPTION, ACYC ) );
      lUsages.iAPUAotTsn =
            new BigDecimal( lDetailsPaneDriver.getAssemblyCurrentTsn( APU_DESCRIPTION, AOT ) );

      return lUsages;
   }


   private class Usages {

      public BigDecimal iAircraftHoursTsn;
      public BigDecimal iAircraftCyclesTsn;
      public BigDecimal iEngineHoursTsn;
      public BigDecimal iEngineCyclesTsn;
      public BigDecimal iEngineEcycTsn;
      public BigDecimal iEngineEotTsn;
      public BigDecimal iAPUHoursTsn;
      public BigDecimal iAPUCyclesTsn;
      public BigDecimal iAPUAcycTsn;
      public BigDecimal iAPUAotTsn;


      public Usages() {
      }


      public void addAircraftHours( BigDecimal aDelta ) {
         iAircraftHoursTsn = iAircraftHoursTsn.add( aDelta );
      }


      public void addAircraftCycles( BigDecimal aDelta ) {
         iAircraftCyclesTsn = iAircraftCyclesTsn.add( aDelta );
      }


      public void addEngineHours( BigDecimal aDelta ) {
         iEngineHoursTsn = iEngineHoursTsn.add( aDelta );
      }


      public void addEngineCycles( BigDecimal aDelta ) {
         iEngineCyclesTsn = iEngineCyclesTsn.add( aDelta );
      }


      public void addEngineEcyc( BigDecimal aDelta ) {
         iEngineEcycTsn = iEngineEcycTsn.add( aDelta );
      }


      public void addEngineEot( BigDecimal aDelta ) {
         iEngineEotTsn = iEngineEotTsn.add( aDelta );
      }


      public void addAPUHours( BigDecimal aDelta ) {
         iAPUHoursTsn = iAPUHoursTsn.add( aDelta );
      }


      public void addAPUCycles( BigDecimal aDelta ) {
         iAPUCyclesTsn = iAPUCyclesTsn.add( aDelta );
      }


      public void addAPUAcyc( BigDecimal aDelta ) {
         iAPUAcycTsn = iAPUAcycTsn.add( aDelta );
      }


      public void addAPUAot( BigDecimal aDelta ) {
         iAPUAotTsn = iAPUAotTsn.add( aDelta );
      }


      public void assetEquals( Usages aExpected ) {

         Assert.assertTrue( "Unexpected aircraft HOURS TSN.",
               aExpected.iAircraftHoursTsn.compareTo( iAircraftHoursTsn ) == 0 );
         Assert.assertTrue( "Unexpected aircraft CYCLES TSN.",
               aExpected.iAircraftCyclesTsn.compareTo( iAircraftCyclesTsn ) == 0 );
         Assert.assertTrue( "Unexpected engine HOURS TSN.",
               aExpected.iEngineHoursTsn.compareTo( iEngineHoursTsn ) == 0 );
         Assert.assertTrue( "Unexpected engine CYCLES TSN.",
               aExpected.iEngineCyclesTsn.compareTo( iEngineCyclesTsn ) == 0 );
         Assert.assertTrue( "Unexpected engine ECYC TSN.",
               aExpected.iEngineEcycTsn.compareTo( iEngineEcycTsn ) == 0 );
         Assert.assertTrue( "Unexpected engine EOT TSN.",
               aExpected.iEngineEotTsn.compareTo( iEngineEotTsn ) == 0 );
         Assert.assertTrue( "Unexpected APU HOURS TSN.",
               aExpected.iAPUHoursTsn.compareTo( iAPUHoursTsn ) == 0 );
         Assert.assertTrue( "Unexpected APU CYCLES TSN.",
               aExpected.iAPUCyclesTsn.compareTo( iAPUCyclesTsn ) == 0 );
         Assert.assertTrue( "Unexpected APU ACYC TSN.",
               aExpected.iAPUAcycTsn.compareTo( iAPUAcycTsn ) == 0 );
         Assert.assertTrue( "Unexpected APU AOT TSN.",
               aExpected.iAPUAotTsn.compareTo( iAPUAotTsn ) == 0 );

      }

   }

}
