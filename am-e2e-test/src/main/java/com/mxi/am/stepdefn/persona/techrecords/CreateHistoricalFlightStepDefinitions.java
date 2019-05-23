/*
 * \ * Confidential, proprietary and/or trade secret information of Mxi Technologies.
 * 
 * Copyright 2000-2018 Mxi Technologies. All Rights Reserved.
 * 
 * Except as expressly provided by written license signed by a duly appointed officer of Mxi
 * Technologies, any disclosure, distribution, reproduction, compilation, modification, creation of
 * derivative works and/or other use of the Mxi source code is strictly prohibited. Inclusion of a
 * copyright notice shall not be taken to indicate that the source code has been published.
 */
package com.mxi.am.stepdefn.persona.techrecords;

import java.math.BigDecimal;
import java.util.Date;

import javax.inject.Inject;

import org.junit.Assert;

import com.mxi.am.driver.query.InventoryQueriesDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.flight.EditFlightPageDriver;
import com.mxi.am.driver.web.flight.flightDetails.FlightDetailsPageDriver;
import com.mxi.am.driver.web.flight.flightDetails.flightPanes.FlightInformationPaneDriver;
import com.mxi.am.driver.web.inventory.inventorydetailspage.InventoryDetailsPageDriver;
import com.mxi.am.driver.web.inventory.inventorydetailspage.inventorydetailspanes.DetailsPaneDriver;
import com.mxi.am.driver.web.inventory.inventorydetailspage.inventorydetailspanes.historicalpanes.FlightPaneDriver;
import com.mxi.mx.common.utils.DateUtils;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;


@ScenarioScoped
public class CreateHistoricalFlightStepDefinitions {

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private InventoryDetailsPageDriver iInventoryDetailsPageDriver;

   @Inject
   private EditFlightPageDriver iEditFlightPageDriver;

   @Inject
   private FlightDetailsPageDriver iFlightDetailsPageDriver;

   private final String iAircraftBarcode;


   @Inject
   public CreateHistoricalFlightStepDefinitions(InventoryQueriesDriver aInventoryQueriesDriver) {
      iAircraftBarcode =
            aInventoryQueriesDriver.getByAircraftRegistrationCode( AIRCRAFT_REG_CODE ).getBarcode();
   }

   // test values
   // note for the pre-loaded data is from C_RI_ATTACH, C_RI_INVENTORY and C_RI_INVENTORY_Usage CSV
   // files under \am-e2e-test\src\main\data\actuals\persona\techRecords\CreateFlight


   // The flight dates need to be within the "show flights" window (60 days).
   private static final Date FIVE_DAYS_AGO = DateUtils.addDays( new Date(), -5 );
   private static final Date TEN_DAYS_AGO = DateUtils.addDays( new Date(), -10 );

   private static final String AIRPORT1 = "AIRPORT1";
   private static final String AIRPORT2 = "AIRPORT2";
   private static final String FLIGHT_NAME1 = "CreateHistFlight_1";
   private static final String FLIGHT_NAME2 = "CreateHistFlight_2";

   private static final String AIRCRAFT_REG_CODE = "CF-SN1";
   private static final String AC_DECRIPTION = "Aircraft Part 2 - CF-SN1";
   private static final String ENGINE_DESCRIPTION1 =
         "(1) Part Name - Engine (PN: ENG_ASSY_PN1, SN: CreateFlight-ENG-SN1)";
   private static final String ENGINE_DESCRIPTION2 =
         "(2) Part Name - Engine (PN: ENG_ASSY_PN1, SN: CreateFlight-ENG-SN2)";
   private static final String APU_DESCRIPTION =
         "Part Name - APU (PN: APU_ASSY_PN1, SN: CreateFlight-APU-SN1)";
   private static final String ACCRUED_TABLE_MESSAGE =
         "Enter the Actual Arrival Date and then you can enter the accrued usage values.";
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
   private static final String USAGE_DELTA = "Delta";

   private static final BigDecimal HOURS_DELTA1 = new BigDecimal( 5.5 );
   private static final BigDecimal HOURS_DELTA2 = new BigDecimal( 3 );
   private static final BigDecimal CYCLE_DELTA = new BigDecimal( 1 );

   private Usages iExpectedCurrentUsage;
   private Usages iExpectedFlightUsage;


   @Given( "^an operational aircraft is tracking usages$" )
   public void iOperationAC() throws Throwable {

      // go to aircraft inventory details -
      iNavigationDriver.barcodeSearch( iAircraftBarcode );

   }


   @And( "^the aircraft has had a flight created and the flight was accruing the same usages$" )
   public void iACHasHadaFlightCreated() throws Throwable {

      // go to flight tab
      FlightPaneDriver lHistoricalFlightPaneDriver =
            iInventoryDetailsPageDriver.clickTabHistorical().clickTabFlight();

      // Create a historical flight for the aircraft.
      // We verify that no usage accrued table shown up at the beginning, instead we see a message
      // to ask you enter Actual Arrival Date
      lHistoricalFlightPaneDriver.clickCreateFlight();
      Assert.assertTrue( iNavigationDriver.isTextOnPagePresent( ACCRUED_TABLE_MESSAGE ) );

      // enter the flight information
      Date lDepartureDate = FIVE_DAYS_AGO;
      Date lArrivalDate = DateUtils.addHours( lDepartureDate, 5 );

      iEditFlightPageDriver.setFlightName( FLIGHT_NAME1 );
      iEditFlightPageDriver.setArrivalAirport( AIRPORT1 );
      iEditFlightPageDriver.setDepartureAirport( AIRPORT2 );
      iEditFlightPageDriver.setActualDepartureDate( toUiDateString( lDepartureDate ) );
      iEditFlightPageDriver.setActualDepartureTime( toUiTimeString( lDepartureDate ) );
      iEditFlightPageDriver.setActualArrivalDate( toUiDateString( lArrivalDate ) );
      iEditFlightPageDriver.setActualArrivalTime( toUiTimeString( lArrivalDate ) );

      // we need to move the mouse to other field to load the usage accrued page, this is a
      // workaround, OPER-11791 is raised to resolve this issue
      iEditFlightPageDriver.setDepartureGate( "1" );;

      // we need to make sure usage parameters of sub-assemblies are read only when the same
      // parameters are defined against the aircraft
      Assert.assertFalse( iNavigationDriver.isTextOnPagePresent( ACCRUED_TABLE_MESSAGE ) );

      // verify if the fields of Engine and APU are read only and the values are what we expected
      Assert.assertEquals( "true", iEditFlightPageDriver
            .isAccruedUsageReadOnly( ENGINE_DESCRIPTION1, CYCLES, USAGE_DELTA ) );
      Assert.assertEquals( "1",
            iEditFlightPageDriver.getAccruedUsage( ENGINE_DESCRIPTION1, CYCLES, USAGE_DELTA ) );

      Assert.assertEquals( "true",
            iEditFlightPageDriver.isAccruedUsageReadOnly( APU_DESCRIPTION, HOURS, USAGE_DELTA ) );
      Assert.assertEquals( "5",
            iEditFlightPageDriver.getAccruedUsage( APU_DESCRIPTION, HOURS, USAGE_DELTA ) );

      // manually update the delta for Aircraft HOURS parameter, verify it is synchronized to
      // sub-assembly as well
      iEditFlightPageDriver.setAccruedUsageDelta( AC_DECRIPTION, HOURS, HOURS_DELTA1 );
      iEditFlightPageDriver.setDepartureGate( "2" );
      // move to cursor to let the usage synchronizing, this is a
      // workaround, OPER-11791 is raised to resolve this issue

      Assert.assertEquals( String.valueOf( HOURS_DELTA1 ),
            iEditFlightPageDriver.getAccruedUsage( ENGINE_DESCRIPTION2, HOURS, USAGE_DELTA ) );

      // update the usage parameters for sub-assemblies exclusively - this can also verify those
      // usages parameters are editable
      iEditFlightPageDriver.setAccruedUsageDelta( ENGINE_DESCRIPTION1, ECYC, CYCLE_DELTA );
      iEditFlightPageDriver.setAccruedUsageDelta( ENGINE_DESCRIPTION1, EOT, HOURS_DELTA1 );
      iEditFlightPageDriver.setAccruedUsageDelta( ENGINE_DESCRIPTION2, ECYC, CYCLE_DELTA );
      iEditFlightPageDriver.setAccruedUsageDelta( ENGINE_DESCRIPTION2, EOT, HOURS_DELTA1 );
      iEditFlightPageDriver.setAccruedUsageDelta( APU_DESCRIPTION, ACYC, CYCLE_DELTA );
      iEditFlightPageDriver.setAccruedUsageDelta( APU_DESCRIPTION, AOT, HOURS_DELTA1 );

      iEditFlightPageDriver.clickOK();

   }


   @When( "^I create a historical flight accruing the same usages and its arrival date is before the other flight$" )
   public void iCreateHistoricalFlight() throws Throwable {

      // Generate the new expected current usage by applying the deltas to the current inventory
      // usages
      iExpectedCurrentUsage = getInventoryUsages();
      iExpectedCurrentUsage.addAircraftHours( HOURS_DELTA2 );
      iExpectedCurrentUsage.addAircraftCycles( CYCLE_DELTA );
      iExpectedCurrentUsage.addEngineHours( HOURS_DELTA2 );
      iExpectedCurrentUsage.addEngineCycles( CYCLE_DELTA );
      iExpectedCurrentUsage.addEngineEcyc( CYCLE_DELTA );
      iExpectedCurrentUsage.addEngineEot( HOURS_DELTA2 );
      iExpectedCurrentUsage.addAPUHours( HOURS_DELTA2 );
      iExpectedCurrentUsage.addAPUCycles( CYCLE_DELTA );
      iExpectedCurrentUsage.addAPUAcyc( CYCLE_DELTA );
      iExpectedCurrentUsage.addAPUAot( HOURS_DELTA2 );

      // go to aircraft inventory details - flight tab
      FlightPaneDriver lHistoricalFlightPaneDriver =
            iInventoryDetailsPageDriver.clickTabHistorical().clickTabFlight();

      // go to the flight details page from the latest flight created above
      lHistoricalFlightPaneDriver.clickFlightName( FLIGHT_NAME1 );

      // Generate the new expected existing flight's usages by applying the deltas to the current
      // flight usages
      iExpectedFlightUsage = getFlightUsages();
      iExpectedFlightUsage.addAircraftHours( HOURS_DELTA2 );
      iExpectedFlightUsage.addAircraftCycles( CYCLE_DELTA );
      iExpectedFlightUsage.addEngineHours( HOURS_DELTA2 );
      iExpectedFlightUsage.addEngineCycles( CYCLE_DELTA );
      iExpectedFlightUsage.addEngineEcyc( CYCLE_DELTA );
      iExpectedFlightUsage.addEngineEot( HOURS_DELTA2 );
      iExpectedFlightUsage.addAPUHours( HOURS_DELTA2 );
      iExpectedFlightUsage.addAPUCycles( CYCLE_DELTA );
      iExpectedFlightUsage.addAPUAcyc( CYCLE_DELTA );
      iExpectedFlightUsage.addAPUAot( HOURS_DELTA2 );

      iFlightDetailsPageDriver.clickClose();

      // Create a historical flight for the aircraft, the date is earlier than the first flight we
      // created above
      lHistoricalFlightPaneDriver.clickCreateFlight();

      // enter the flight information, note this time the up - down time decides the hours parameter
      // populated for the HOURS
      Date lDepartureDate = TEN_DAYS_AGO;
      Date lArrivalDate = DateUtils.addHours( lDepartureDate, 4 );
      Date lUpDate = lDepartureDate;
      Date lDownDate = DateUtils.addHours( lUpDate, 3 );

      iEditFlightPageDriver.setFlightName( FLIGHT_NAME2 );
      iEditFlightPageDriver.setArrivalAirport( AIRPORT1 );
      iEditFlightPageDriver.setDepartureAirport( AIRPORT2 );
      iEditFlightPageDriver.setActualDepartureDate( toUiDateString( lDepartureDate ) );
      iEditFlightPageDriver.setActualDepartureTime( toUiTimeString( lDepartureDate ) );
      iEditFlightPageDriver.setActualArrivalDate( toUiDateString( lArrivalDate ) );
      iEditFlightPageDriver.setActualArrivalTime( toUiTimeString( lArrivalDate ) );
      iEditFlightPageDriver.setUpDate( toUiDateString( lUpDate ) );
      iEditFlightPageDriver.setUpTime( toUiTimeString( lUpDate ) );
      iEditFlightPageDriver.setDownDate( toUiDateString( lDownDate ) );
      iEditFlightPageDriver.setDownTime( toUiTimeString( lDownDate ) );
      iEditFlightPageDriver.setDepartureGate( "2" );// move to cursor to let the usage synchronized

      // verify if the fields of hours is populated based on up and down time
      Assert.assertEquals( "3",
            iEditFlightPageDriver.getAccruedUsage( ENGINE_DESCRIPTION2, HOURS, "Delta" ) );

      // update the usage parameters for sub-assemblies exclusively
      iEditFlightPageDriver.setAccruedUsageDelta( ENGINE_DESCRIPTION1, ECYC, CYCLE_DELTA );
      iEditFlightPageDriver.setAccruedUsageDelta( ENGINE_DESCRIPTION1, EOT, HOURS_DELTA2 );
      iEditFlightPageDriver.setAccruedUsageDelta( ENGINE_DESCRIPTION2, ECYC, CYCLE_DELTA );
      iEditFlightPageDriver.setAccruedUsageDelta( ENGINE_DESCRIPTION2, EOT, HOURS_DELTA2 );
      iEditFlightPageDriver.setAccruedUsageDelta( APU_DESCRIPTION, ACYC, CYCLE_DELTA );
      iEditFlightPageDriver.setAccruedUsageDelta( APU_DESCRIPTION, AOT, HOURS_DELTA2 );

      iEditFlightPageDriver.clickOK();

   }


   @Then( "^the aircraft's and installed assemblies' current TSN usages are incremented by the flight usage I provide$" )
   public void theAircraftandAssemblyUsageUpdated() throws Throwable {

      // Assert the inventory current usage values match the expected values.
      getInventoryUsages().assetEquals( iExpectedCurrentUsage );

   }


   @And( "^the subsequent flight's TSN usages are incremented by the flight usage I provide$" )
   public void theFlightAfterGetUsagesUpdated() throws Throwable {

      // Go to flight tab and locate the previous flight
      iInventoryDetailsPageDriver.clickTabHistorical().clickTabFlight()
            .clickFlightName( FLIGHT_NAME1 );

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


   public static String toUiDateString( Date aDate ) {
      return DateUtils.toString( aDate, DateUtils.DEFAULT_DATE_FORMAT );
   }


   public static String toUiTimeString( Date aDate ) {
      return DateUtils.toString( aDate, DateUtils.DEFAULT_TIME_FORMAT );
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
