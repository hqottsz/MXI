
package com.mxi.am.stepdefn.persona.lineplanner;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;

import org.junit.Assert;

import com.mxi.am.driver.common.configurationParameters.ConfigurationParameterWorkflow;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.fault.FaultPagePageDriver;
import com.mxi.am.driver.web.fault.RaiseFaultPageDriver;
import com.mxi.am.driver.web.flight.EditFlightPageDriver;
import com.mxi.am.driver.web.flight.flightDetails.FlightDetailsPageDriver;
import com.mxi.am.driver.web.inventory.InventorySelectionPageDriver;
import com.mxi.am.driver.web.inventory.inventorydetailspage.InventoryDetailsPageDriver;
import com.mxi.am.driver.web.task.TaskDetails.TaskDetailsPageDriver;
import com.mxi.am.driver.web.task.TaskDetails.TaskDetailsPanes.HistoryPaneDriver;
import com.mxi.am.driver.web.todolist.ToDoListPageDriver;
import com.mxi.am.stepdefn.NavigationStepDefinitions;

import cucumber.api.PendingException;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


/**
 * Step Definitions for navigating Maintenix
 */
public class RaiseLogbookFaultStepDefinitions {

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private RaiseFaultPageDriver iRaiseFaultPageDriver;

   @Inject
   private NavigationStepDefinitions iNavigationStepDefinitions;

   @Inject
   private TaskDetailsPageDriver iTaskDetailsPageDriver;

   @Inject
   private InventorySelectionPageDriver iInventorySelectionPageDriver;

   @Inject
   private FaultPagePageDriver iFaultPagePageDriver;

   @Inject
   private ToDoListPageDriver iToDoListPageDriver;

   @Inject
   private InventoryDetailsPageDriver iInventoryDetailsPageDriver;

   @Inject
   private EditFlightPageDriver iEditFlightPageDriver;

   @Inject
   private FlightDetailsPageDriver iFlightDetailsPageDriver;

   @Inject
   private ConfigurationParameterWorkflow iConfigurationParameterDriver;

   private String iFaultName = "Test Fault";
   private String iFaultName2 = "Test Fault-4185";
   private String iFailedSystem = "SYS-1";
   private String iFlightName = "Flight-4185";
   private String iFlightDateString1 = "19-JAN-2016";
   private String iFlightDateString2 = "20-JAN-2016";
   private String iFlightDateString3 = "21-JAN-2016";
   private String iFlightDepTimeString = "01:00";
   private String iFlightArrTimeString = "06:00";
   private String iParm_SHOW_PARTS_ON_OFF = null;


   @When( "^I raise a historic logbook fault for aircraft \"([^\"]*)\"$" )
   public void i_raise_a_historic_logbook_fault_for_aircraft( String aAircraftRegCode )
         throws Throwable {
      // select line planner
      iNavigationDriver.navigate( "Line Planner", "To Do List (Line Planner)" );
      iToDoListPageDriver.clickTabFleetList().clickRadioButtonForAircraft( aAircraftRegCode );
      iToDoListPageDriver.getTabFleetList().clickRaiseLogbookFaultButton();
      iRaiseFaultPageDriver.clickLogFaultAndClose();
      iRaiseFaultPageDriver.setFaultName( iFaultName );
      iRaiseFaultPageDriver.clickSelectFailedSystem();
      iInventorySelectionPageDriver.selectInventoryFromTree( iFailedSystem );
      iRaiseFaultPageDriver.setCorrectiveAction( "I fixed it" );
      iRaiseFaultPageDriver.setRepairLocation( "AIRPORT1" );
      iRaiseFaultPageDriver.clickOk();

      // if you are prompted to specify if the fault is recurring select N/A option
      if ( iNavigationDriver.getTitle().contains( "Find Recurring Fault" ) ) {
         iFaultPagePageDriver.selectRecurringFault( "N/A" );
         iFaultPagePageDriver.clickOkButton();
      }

   }


   @When( "^I raise a historic logbook fault with configuration changes for aircraft \"([^\"]*)\"$" )
   public void raise_historic_fault_with_config_changes( String aAircraftRegCode )
         throws Throwable {

      updateConfigurationParameter_SHOW_PARTS_ON_OFF();
      createHistoricFlights( iFlightName, aAircraftRegCode );

      // select line planner
      iNavigationDriver.navigate( "Line Planner", "To Do List (Line Planner)" );
      iToDoListPageDriver.clickTabFleetList().clickRadioButtonForAircraft( aAircraftRegCode );
      iToDoListPageDriver.getTabFleetList().clickRaiseLogbookFaultButton();
      iRaiseFaultPageDriver.clickLogFaultAndClose();
      iRaiseFaultPageDriver.setFaultName( iFaultName2 );
      iRaiseFaultPageDriver.clickSelectFailedSystem();
      iInventorySelectionPageDriver.selectInventoryFromTree( iFailedSystem );
      iRaiseFaultPageDriver.setCorrectiveAction( "I fixed it" );
      iRaiseFaultPageDriver.selectFoundDuringFlightByPopup( iFlightName );

   }


   @When( "^I remove part number \"([^\"]*)\" serial number \"([^\"]*)\" and install part number \"([^\"]*)\" serial number \"([^\"]*)\"$" )
   public void i_remove_part_number_serial_number_and_install_part_number_serial_number(
         String aPartOff, String aSerialOff, String aPartOn, String aSerialOn ) throws Throwable {
      iRaiseFaultPageDriver.setRemovedInventory1( "1", aPartOff, aSerialOff, "IMSCHD" );
      iRaiseFaultPageDriver.setInstalledInventory1( aPartOn, aSerialOn );

   }


   @Then( "^the fault is complete$" )
   public void the_fault_is_complete() throws Throwable {
      // check that the page title is "Task Details" and that the subtitle contains
      // "Historic Test Fault"
      iNavigationStepDefinitions.assertOnItem( "Task Details", iFaultName );

      // on the Task Details page, check that the Fault Information tab says CERTIFY for the status
      // and that the Task Information tab says COMPLETE for the status
      iTaskDetailsPageDriver.clickTabTaskInformation();
      Assert.assertEquals( "COMPLETE (Complete)",
            iTaskDetailsPageDriver.getTabTaskInformation().getStatus() );
      iTaskDetailsPageDriver.clickTabFaultInformation();
      Assert.assertEquals( "CERTIFY (Certified)",
            iTaskDetailsPageDriver.getTabFaultInformation().getFaultStatus() );

   }


   @Then( "^the fault is complete has usage at completion of (\\d+) cycles and (\\d+) hours$" )
   public void the_fault_is_complete_has_usage_at_completion_of_cycles_and_hours( int aCycles,
         int aHours ) throws Throwable {
      // check found on date
      Assert.assertEquals( iFlightDateString2, iRaiseFaultPageDriver.getFoundOnDate() );
      Assert.assertEquals( iFlightArrTimeString, iRaiseFaultPageDriver.getFoundOnDateTime() );

      iRaiseFaultPageDriver.clickOk();

      // if you are prompted to specify if the fault is recurring select N/A option
      if ( iNavigationDriver.getTitle().contains( "Find Recurring Fault" ) ) {
         iFaultPagePageDriver.selectRecurringFault( "N/A" );
         iFaultPagePageDriver.clickOkButton();
      }

      // check that the page title is "Task Details" and that the subtitle contains
      // "Historic Test Fault"
      iNavigationStepDefinitions.assertOnItem( "Task Details", iFaultName2 );

      // on the Task Details page, check that the Fault Information tab says CERTIFY for the status
      // and that the Task Information tab says COMPLETE for the status
      iTaskDetailsPageDriver.clickTabTaskInformation();
      Assert.assertEquals( "COMPLETE (Complete)",
            iTaskDetailsPageDriver.getTabTaskInformation().getStatus() );
      iTaskDetailsPageDriver.clickTabFaultInformation();
      Assert.assertEquals( "CERTIFY (Certified)",
            iTaskDetailsPageDriver.getTabFaultInformation().getFaultStatus() );

      // go to history tab to check usage at completion values
      HistoryPaneDriver lHistoryPaneDriver = iTaskDetailsPageDriver.clickTabHistory();
      Assert.assertEquals( aCycles, lHistoryPaneDriver.getTsnCycles() );
      // for floating point numbers you need a delta as the last argument
      Assert.assertEquals( aHours, lHistoryPaneDriver.getTsnHours(), 0.005 );

   }


   @Then( "^the installed parts usage is (\\d+) cycles and (\\d+) hours$" )
   public void the_installed_parts_usage_is_cycles_and_hours( int arg1, int arg2 )
         throws Throwable {
      // Write code here that turns the phrase above into concrete actions
      throw new PendingException();

      // go to installed part
      // go to required tab
      // check part usage
   }


   @Then( "^the removed parts usage is (\\d+) cycles and (\\d+) hours$" )
   public void the_removed_parts_usage_is_cycles_and_hours( int arg1, int arg2 ) throws Throwable {
      // Write code here that turns the phrase above into concrete actions
      throw new PendingException();

      // go to removed part
      // go to required tab
      // check part usage
   }


   @Then( "^I revert configuration parameter SHOW_PARTS_ON_OFF$" )
   public void i_revert_configuration_parameter_SHOW_PARTS_ON_OFF() throws Throwable {
      // toggle parm
      iConfigurationParameterDriver.updateParameter( "SHOW_PARTS_ON_OFF", iParm_SHOW_PARTS_ON_OFF );
   }


   private void updateConfigurationParameter_SHOW_PARTS_ON_OFF() throws Throwable {
      // toggle parm
      iParm_SHOW_PARTS_ON_OFF = iConfigurationParameterDriver
            .temporaryParameterToggleAndRefresh( "SHOW_PARTS_ON_OFF", "TRUE" );

   }


   private void createHistoricFlights( String aFlightName, String aAircraftRegistrationCode )
         throws Throwable {

      iNavigationDriver.navigate( "Line Planner", "To Do List (Line Planner)" );
      iToDoListPageDriver.clickTabFleetList().clickAircraftInTable( aAircraftRegistrationCode );
      iInventoryDetailsPageDriver.clickTabHistorical().clickTabFlight();

      // create flight 1
      iInventoryDetailsPageDriver.getTabHistorical().getTabFlight().clickCreateFlight();
      iEditFlightPageDriver.setFlightName( "DUMMY-1" );
      iEditFlightPageDriver.setArrivalAirport( "Airport1" );
      iEditFlightPageDriver.setDepartureAirport( "Airport2" );
      iEditFlightPageDriver.setActualDepartureDate( iFlightDateString1 );
      iEditFlightPageDriver.setActualDepartureTime( iFlightDepTimeString );
      iEditFlightPageDriver.setActualArrivalDate( iFlightDateString1 );
      iEditFlightPageDriver.setActualArrivalTime( iFlightArrTimeString );
      iEditFlightPageDriver.clickOK();
      iFlightDetailsPageDriver.clickClose();

      // create flight 2
      iInventoryDetailsPageDriver.getTabHistorical().getTabFlight().clickCreateFlight();
      iEditFlightPageDriver.setFlightName( aFlightName );
      iEditFlightPageDriver.setArrivalAirport( "Airport2" );
      iEditFlightPageDriver.setDepartureAirport( "Airport3" );
      iEditFlightPageDriver.setActualDepartureDate( iFlightDateString2 );
      iEditFlightPageDriver.setActualDepartureTime( iFlightDepTimeString );
      iEditFlightPageDriver.setActualArrivalDate( iFlightDateString2 );
      iEditFlightPageDriver.setActualArrivalTime( iFlightArrTimeString );
      iEditFlightPageDriver.clickOK();
      iFlightDetailsPageDriver.clickClose();

      // create flight 3
      iInventoryDetailsPageDriver.getTabHistorical().getTabFlight().clickCreateFlight();
      iEditFlightPageDriver.setFlightName( "DUMMY-2" );
      iEditFlightPageDriver.setArrivalAirport( "Airport3" );
      iEditFlightPageDriver.setDepartureAirport( "Airport1" );
      iEditFlightPageDriver.setActualDepartureDate( iFlightDateString3 );
      iEditFlightPageDriver.setActualDepartureTime( iFlightDepTimeString );
      iEditFlightPageDriver.setActualArrivalDate( iFlightDateString3 );
      iEditFlightPageDriver.setActualArrivalTime( iFlightArrTimeString );
      iEditFlightPageDriver.clickOK();
      iFlightDetailsPageDriver.clickClose();
   }


   @When( "^I raise a logbook fault and defer it using the shared Deferral Reference via typing$" )
   public void iRaiseALogbookFaultAndDeferItUsingTheSharedDeferralReferenceViaTyping()
         throws Throwable {
      iToDoListPageDriver.clickTabFleetList()
            .clickRadioButtonForAircraft( "Aircraft Part MOC 1 - OPER-22784" );
      iToDoListPageDriver.clickTabFleetList().clickRaiseLogbookFaultButton();
      iRaiseFaultPageDriver.clickLogFaultAndDefer();
      iRaiseFaultPageDriver.setFaultSeverity( "MEL (MEL failure)" );
      iRaiseFaultPageDriver.setDeferralReference( "OPER-22784-1" );
   }


   @Then( "^A warning message informing the user that the deferral reference has conflicts should appear$" )
   public void aWarningMessageInformingTheUserThatTheDeferralReferenceHasConflictsShouldAppear()
         throws Throwable {
      assertEquals( "You have entered a deferral reference name that maps to multiple deferral "
            + "class/operator combinations. Please narrow down the search by using the Select Deferral "
            + "popup window or select the deferral class manually.",
            iRaiseFaultPageDriver.getInvalidDeferralMessage() );
   }
}
