
package com.mxi.am.stepdefn.persona.linesupervisor.stationmonitoring;

import javax.inject.Inject;

import org.junit.Assert;

import com.mxi.am.driver.query.FlightQueriesDriver;
import com.mxi.am.driver.query.InventoryInfo;
import com.mxi.am.driver.query.InventoryQueriesDriver;
import com.mxi.am.driver.query.WorkPackageKey;
import com.mxi.am.driver.query.WorkPackageQueriesDriver;
import com.mxi.am.driver.web.sma.StationMonitoringPageDriver;
import com.mxi.am.driver.web.todolist.ToDoListPageDriver;
import com.mxi.am.stepdefn.persona.linesupervisor.stationmonitoring.data.StationMonitoringScenarioData;
import com.mxi.mx.core.key.RefEventStatusKey;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class StationMonitoringStepDefinitions {

   @Inject
   private ToDoListPageDriver iToDoListHeaderPageDriver;

   @Inject
   private FlightQueriesDriver iFlightQueriesDriver;

   @Inject
   private StationMonitoringPageDriver iStationMonitoringPageDriver;

   @Inject
   private WorkPackageQueriesDriver iWorkPackageQueriesDriver;

   @Inject
   private InventoryQueriesDriver inventoryQueriesDriver;


   @Before( "@BeforeStationMonitoring" )
   public void beforeSM() throws Exception {
      createFlightData();
   }


   @After( "@BeforeStationMonitoring" )
   public void afterSM() {
      removeFlightData();
   }


   @Before( "@BeforeWorkPackageAndTask" )
   public void beforeWP() throws Exception {
      createFlightData();

      // get work package info and set it to in-work
      WorkPackageKey lWorkPackageKey = iWorkPackageQueriesDriver
            .getByWorkPackageName( StationMonitoringScenarioData.WORK_PACKAGE_NAME );

      iWorkPackageQueriesDriver.setWorkPackageStatus( lWorkPackageKey.getDbId(),
            RefEventStatusKey.IN_WORK.getCd(), StationMonitoringScenarioData.WORK_PACKAGE_NAME );
   }


   @After( "@BeforeWorkPackageAndTask" )
   public void afterWP() {
      removeFlightData();
   }


   @When( "^I want to monitor the flight information for flights at my current location" )
   public void iWantToMonitorFlightInfo() throws Throwable {
      iToDoListHeaderPageDriver.clickStationMonitoring();
      iStationMonitoringPageDriver
            .activateNewPage( StationMonitoringScenarioData.STATION_MONITORING_TITLE );
   }


   @Then( "^I see the flight information for flights related to my current station$" )
   public void seeTheFlightInformationForFlightsRelatedToCurrentStation() throws Throwable {
      String lRegcode =
            iStationMonitoringPageDriver.getAcRegCd( StationMonitoringScenarioData.REG_CD_ID );
      Assert.assertEquals( StationMonitoringScenarioData.ACFT_REG_CD, lRegcode );
   }


   @Given( "^I want to view work package details from Station Monitoring$" )
   public void iWantToViewWorkPackageDetails() throws Throwable {
      iToDoListHeaderPageDriver.clickStationMonitoring();
      iStationMonitoringPageDriver
            .activateNewPage( StationMonitoringScenarioData.STATION_MONITORING_TITLE );
   }


   @When( "^I click a work package link on a flight card$" )
   public void iClickWorkPackageLinkonFlightCard() {
      iStationMonitoringPageDriver.clickWorkPackageLink();
   }


   @Then( "^I can see the work package details in a new tab$" )
   public void iSeeWorkPackageDetailsInNewTab() {
      iStationMonitoringPageDriver
            .activateNewPage( StationMonitoringScenarioData.WORK_PACKAGE_DETAILS_TITLE );
      Assert.assertTrue( iStationMonitoringPageDriver.getWorkPackageName()
            .contains( StationMonitoringScenarioData.WORK_PACKAGE_NAME ) );

   }


   private void removeFlightData() {
      iFlightQueriesDriver.removeFlightData( StationMonitoringScenarioData.LEG_ID_UUID );
   }


   private void createFlightData() {

      InventoryInfo inventoryInfo = inventoryQueriesDriver
            .getInventoryInfoByInventorySerialNo( StationMonitoringScenarioData.ACFT_REG_CD );

      iFlightQueriesDriver.createFlightData( StationMonitoringScenarioData.LEG_ID_UUID,
            StationMonitoringScenarioData.FLIGHT_NAME,
            StationMonitoringScenarioData.ARRIVAL_LOC_DB_ID,
            StationMonitoringScenarioData.ARRIVAL_LOC_ID,
            StationMonitoringScenarioData.SCHED_ARRIVAL_DT,
            StationMonitoringScenarioData.SCHED_DEPARTURE_DT,
            StationMonitoringScenarioData.ACTUAL_ARRIVAL_DT,
            StationMonitoringScenarioData.DEPARTURE_LOC_DB_ID,
            StationMonitoringScenarioData.DEPARTURE_LOC_ID,
            StationMonitoringScenarioData.FLIGHT_LEG_STATUS_CD, inventoryInfo.getDbId().intValue(),
            inventoryInfo.getId().intValue() );
   }

}
