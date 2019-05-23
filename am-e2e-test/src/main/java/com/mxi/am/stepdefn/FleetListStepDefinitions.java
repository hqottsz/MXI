
package com.mxi.am.stepdefn;

import javax.inject.Inject;

import com.mxi.am.driver.web.todolist.ToDoListPageDriver;
import com.mxi.am.driver.web.todolist.ToDoListPanes.FleetListPaneDriver;

import cucumber.api.java.en.When;


/**
 * Step Definitions for navigating Maintenix
 */
public class FleetListStepDefinitions {

   @Inject
   private ToDoListPageDriver iToDoListPageDriver;

   @Inject
   private FleetListPaneDriver iFleetListPaneDriver;


   @When( "^I go to Fleet List tab$" )
   public void clickTabFleetList() throws Throwable {
      iToDoListPageDriver.clickTabFleetList();
   }


   @When( "^I open aircraft \"([^\"]*)\"$" )
   public void gotoAircraft( String aAircraftRegistrationCode ) throws Throwable {
      iFleetListPaneDriver.clickAircraftInTable( aAircraftRegistrationCode );
   }


   @When( "^I select radio button for aircraft \"([^\"]*)\"$" )
   public void i_select_radio_button_for_aircraft( String aAircraftRegCode ) throws Throwable {
      iFleetListPaneDriver.clickRadioButtonForAircraft( aAircraftRegCode );
   }


   @When( "^I click raise log book fault$" )
   public void i_click_raise_log_book_fault() throws Throwable {
      iFleetListPaneDriver.clickRaiseLogbookFaultButton();
   }

}
