package com.mxi.am.stepdefn.persona.techrecords;

import javax.inject.Inject;

import org.junit.Assert;

import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.flight.EditFlightPageDriver;
import com.mxi.am.driver.web.flight.FlightSearchPageDriver;
import com.mxi.am.driver.web.flight.flightDetails.FlightDetailsPageDriver;
import com.mxi.am.driver.web.flight.flightDetails.flightPanes.FlightInformationPaneDriver;
import com.mxi.am.driver.web.inventory.inventorydetailspage.InventoryDetailsPageDriver;
import com.mxi.am.driver.web.todolist.ToDoListPageDriver;
import com.mxi.am.driver.web.usage.UsageRecordDetailsPageDriver;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;


/**
 * Step Definitions for navigating Maintenix
 */
@ScenarioScoped
public class RecordFlightStepDefinition {

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private ToDoListPageDriver iToDoListPageDriver;

   @Inject
   private InventoryDetailsPageDriver iInventoryDetailsPageDriver;

   @Inject
   private EditFlightPageDriver iEditFlightPageDriver;

   @Inject
   private FlightDetailsPageDriver iFlightDetailsPageDriver;

   @Inject
   private FlightSearchPageDriver iFlightSearchPageDriver;

   @Inject
   UsageRecordDetailsPageDriver iUsageRecordDetailsPageDriver;

   // test values
   private final String iAircraftRegCode = "100";
   private final String iFlightName = "TECHREC-FLT";
   private final String iAirport1 = "AIRPORT1";
   private final String iAirport2 = "AIRPORT2";
   private final String iAirport3 = "AIRPORT3";
   private final String iFlightDateString1 = "09-JAN-2016";
   private final String iFlightDateString2 = "10-JAN-2016";
   private final String iFlightDepTimeString = "01:00";
   private final String iFlightArrTimeString = "06:00";
   private final String iUpdatedFlightArrTimeString = "09:00";
   private final String iUpdatedFlightDepTimeString = "02:00";


   @When( "^I record a historic flight$" )
   public void iRecordAHistoricFlight() throws Throwable {

      iNavigationDriver.navigate( "Technical Records", "To Do List (Technical Records)" );
      iToDoListPageDriver.clickTabFleetList().clickAircraftInTable( iAircraftRegCode );

      iInventoryDetailsPageDriver.clickTabHistorical().clickTabFlight();

      // create flight
      iInventoryDetailsPageDriver.getTabHistorical().getTabFlight().clickCreateFlight();
      iEditFlightPageDriver.setFlightName( iFlightName );
      iEditFlightPageDriver.setArrivalAirport( iAirport1 );
      iEditFlightPageDriver.setDepartureAirport( iAirport2 );
      iEditFlightPageDriver.setActualDepartureDate( iFlightDateString1 );
      iEditFlightPageDriver.setActualDepartureTime( iFlightDepTimeString );
      iEditFlightPageDriver.setActualArrivalDate( iFlightDateString1 );
      iEditFlightPageDriver.setActualArrivalTime( iFlightArrTimeString );
      iEditFlightPageDriver.clickOK();
      iFlightDetailsPageDriver.clickClose();

   }


   @When( "^I edit the historic flight$" )
   public void iEditTheHistoricFlight() throws Throwable {

      iNavigationDriver.navigate( "Technical Records", "Flight Search" );
      iFlightSearchPageDriver.setFlightName( iFlightName );
      iFlightSearchPageDriver.clickSearch();
      iFlightSearchPageDriver.clickFlightLinkInTable( iFlightName );
      iFlightDetailsPageDriver.clickTabFlightInformation().clickEditFlightButton();
      iEditFlightPageDriver.setActualDepartureDate( iFlightDateString2 + " " );
      iEditFlightPageDriver.setActualDepartureTime( iUpdatedFlightDepTimeString + " " );
      iEditFlightPageDriver.setActualArrivalDate( iFlightDateString2 + " " );
      iEditFlightPageDriver.setActualArrivalTime( iUpdatedFlightArrTimeString + " " );
      iEditFlightPageDriver.setDepartureAirport( iAirport2 + " " );
      iEditFlightPageDriver.setArrivalAirport( iAirport3 + " " );
      iEditFlightPageDriver.setDownTime( iUpdatedFlightArrTimeString + " " );
      iEditFlightPageDriver.setDownDate( iFlightDateString2 + " " );
      iEditFlightPageDriver.setDepartureGate( "CHANGE" );
      iEditFlightPageDriver.clickOK();
   }


   @Then( "^the historic flight is updated$" )
   public void theHistoricFlightIsUpdated() throws Throwable {
      FlightInformationPaneDriver lFlightInformationPaneDriver =
            iFlightDetailsPageDriver.clickTabFlightInformation();
      // check to ensure edits were applied
      Assert.assertEquals( iFlightDateString2 + " " + iUpdatedFlightDepTimeString + " EST",
            lFlightInformationPaneDriver.getDepartureDateTime() );
      Assert.assertEquals( iFlightDateString2 + " " + iUpdatedFlightArrTimeString + " EST",
            lFlightInformationPaneDriver.getArrivalDateTime() );
      Assert.assertTrue( lFlightInformationPaneDriver.getDepartureAirport().contains( iAirport2 ) );
      Assert.assertTrue( lFlightInformationPaneDriver.getArrivalAirport().contains( iAirport3 ) );
      Assert.assertEquals( iFlightDateString2 + " " + iUpdatedFlightArrTimeString + " EST",
            lFlightInformationPaneDriver.getDownDateTime() );
      Assert.assertEquals( "CHANGE", lFlightInformationPaneDriver.getDepartureGate() );
      // Assert the capability requirement label displayed in the page
      String lCapabilityRequirements = lFlightInformationPaneDriver.getCapabilityRequirements();
      Assert.assertNotNull( lCapabilityRequirements );

   }

}
