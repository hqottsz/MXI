
package com.mxi.am.stepdefn.persona.techrecords;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Assert;

import com.mxi.am.driver.common.ConfirmPageDriver;
import com.mxi.am.driver.common.configurationParameters.ConfigurationParameterWorkflow;
import com.mxi.am.driver.common.integration.SendMessagePageDriver;
import com.mxi.am.driver.common.integration.MessageDetails.MessageDetailsPageDriver;
import com.mxi.am.driver.query.InventoryQueriesDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.fault.FaultPagePageDriver;
import com.mxi.am.driver.web.fault.RaiseFaultPageDriver;
import com.mxi.am.driver.web.flight.CompleteFlightPageDriver;
import com.mxi.am.driver.web.flight.EditFlightPageDriver;
import com.mxi.am.driver.web.flight.FlightSearchPageDriver;
import com.mxi.am.driver.web.flight.flightDetails.FlightDetailsPageDriver;
import com.mxi.am.driver.web.inventory.InventorySelectionPageDriver;
import com.mxi.am.driver.web.inventory.inventorydetailspage.InventoryDetailsPageDriver;
import com.mxi.am.driver.web.todolist.ToDoListPageDriver;
import com.mxi.am.driver.web.todolist.ToDoListPanes.FleetListPaneDriver;
import com.mxi.testutil.selenium.ScriptUtil;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;


/**
 * <h1>Tech Records Clerk - Complete Flight</h1>
 * <h2>Feature</h2>
 *
 *
 * <b>Background:</b>
 * <ol>
 * <li>Given I am on the login page</li>
 * <li>When I login as "mxi" with password "password"</li>
 * </ol>
 * <p>
 *
 * <b>Scenario:</b> Complete a planned flight and move aircraft to arrival airport
 * <ol>
 * <li>When I complete planned flight and opt to move the aircraft to the arrival location</li>
 * <li>Then the flight is completed</li>
 * <li>And the aircraft is moved to the arrival location</li>
 * </ol>
 * <p>
 *
 * <b>Scenario:</b> Complete a planned flight and do not move aircraft to arrival airport
 * <ol>
 * <li>When I complete planned flight and opt not to move the aircraft to the arrival location</li>
 * <li>Then the flight is completed</li>
 * <li>And the aircraft remains at its current location</li>
 * </ol>
 * <p>
 *
 * <b>Scenario:</b> Edit a completed flight with configuration change
 * <ol>
 * <li>When I edit the details of completed flight</li>
 * <li>And I opt to make the change with configuration impacts</li>
 * <li>Then the flight details are updated</li>
 * </ol>
 * <p>
 *
 * <b>Scenario:</b> Decline to edit a completed flight because of configuration change
 * <ol>
 * <li>When I edit the details of completed flight</li>
 * <li>And I opt not to make the change because of configuration impacts</li>
 * <li>Then the flight details are not updated</li>
 * </ol>
 * <p>
 *
 *
 */
@ScenarioScoped
public class CompleteFlightStepDefinitions {

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private ToDoListPageDriver iToDoListPageDriver;

   @Inject
   private FleetListPaneDriver iFleetListTabPaneDriver;

   @Inject
   private InventoryDetailsPageDriver iInventoryDetailsPageDriver;

   @Inject
   private InventorySelectionPageDriver iInventorySelectionPageDriver;

   @Inject
   private EditFlightPageDriver iEditFlightPageDriver;

   @Inject
   private FlightDetailsPageDriver iFlightDetailsPageDriver;

   @Inject
   private FlightSearchPageDriver iFlightSearchPageDriver;

   @Inject
   private CompleteFlightPageDriver iCompleteFlightPageDriver;

   @Inject
   private RaiseFaultPageDriver iRaiseFaultPageDriver;

   @Inject
   private SendMessagePageDriver iSendMessagePageDriver;

   @Inject
   private MessageDetailsPageDriver iMessageDetailsPageDriver;

   @Inject
   private ConfigurationParameterWorkflow iConfigurationParameterDriver;

   @Inject
   private FaultPagePageDriver iFaultPagePageDriver;

   @Inject
   private ConfirmPageDriver iConfirmPageDriver;

   private final String iAircraftBarcode;


   @Inject
   public CompleteFlightStepDefinitions(InventoryQueriesDriver aInventoryQueriesDriver) {
      iAircraftBarcode =
            aInventoryQueriesDriver.getByAircraftRegistrationCode( iAircraftRegCode ).getBarcode();
   }


   // test values
   private final String iAirport1 = "AIRPORT1";
   private final String iAirport2 = "AIRPORT2";
   private final String iAirport3 = "AIRPORT3";
   private final String iFlightDateString1 = "01-JAN-2016";
   private final String iFlightDateString2 = "28-FEB-2016";
   private final String iFlightDepTimeString = "01:00";
   private final String iFlightArrTimeString = "06:00";
   private final String iUpdatedFlightArrTimeString1 = "09:00";
   private final String iUpdatedFlightArrTimeString2 = "10:00";
   private static String iAircraftRegCode = "102";
   private final String iPartNoOff = "A0000001";
   private final String iSerialNoOff = "TECHREC-008";
   private final String iPartNoOn = "A0000001";
   private final String iSerialNoOn = "TECHREC-005";

   // test controls
   private final int iDayShift = -20;
   static String iCurrentLocation = null;
   private String iParm_SHOW_PARTS_ON_OFF = null;
   static boolean iFlightCreated = false;
   static boolean iFlightChanged = false;

   // integration message settings
   private final boolean iAsynchronous = true;
   private final boolean iSequential = true;
   private final String iTransport = "jms";
   private final String iUsername = "mxi";
   private final String iPassword = "password";


   /**
    * <h4>Complete planned flight and move to arrival location</h4>
    * <ol>
    * <li>Creates a planned flight via Send Integration Message UI</li>
    * <li>Selects planned flight</li>
    * <li>Sets flight to "move to arrival airport" and completes the flight</li>
    * </ol>
    */
   @When( "^I complete planned flight and opt to move the aircraft to the arrival location$" )
   public void iCompletePlannedFlightAndOptToMoveTheAircraftToTheArrivalLocation()
         throws Throwable {
      String lFlightID = "TEST-1x";

      createPlannedFlight( lFlightID, iAirport1, iAirport2 );

      iNavigationDriver.navigate( "Technical Records", "Flight Search" );

      iFlightSearchPageDriver.setFlightName( lFlightID );
      iFlightSearchPageDriver.clickSearch();
      iFlightSearchPageDriver.clickFlightLinkInTable( lFlightID );
      iFlightDetailsPageDriver.clickCompleteFlight();
      iCompleteFlightPageDriver.setMoveAircraftToArrivalLocation( true );
      iCompleteFlightPageDriver.clickOK();
   }


   /**
    * <h4>Complete planned flight and don't move to arrival location</h4>
    * <ol>
    * <li>Creates a planned flight via Send Integration Message UI</li>
    * <li>Selects planned flight</li>
    * <li>Sets flight to NOT "move to arrival airport" and completes the flight</li>
    * </ol>
    */
   @When( "^I complete planned flight and opt not to move the aircraft to the arrival location$" )
   public void iCompletePlannedFlightAndOptNotToMoveTheAircraftToTheArrivalLocation()
         throws Throwable {
      String lFlightID = "TEST-2x";

      createPlannedFlight( lFlightID, iAirport1, iAirport2 );

      // save current aircraft location for comparison after flight is complete
      iCurrentLocation = getCurrentLocation();

      iNavigationDriver.navigate( "Technical Records", "Flight Search" );

      iFlightSearchPageDriver.setFlightName( lFlightID );
      iFlightSearchPageDriver.clickSearch();
      iFlightSearchPageDriver.clickFlightLinkInTable( lFlightID );
      iFlightDetailsPageDriver.clickCompleteFlight();
      iCompleteFlightPageDriver.setMoveAircraftToArrivalLocation( false );
      iCompleteFlightPageDriver.clickOK();
   }


   /**
    * <h4>Checks flight is completed</h4>
    * <ol>
    * <li>Asserts flight status complete</li>
    * </ol>
    */
   @Then( "^the flight is completed$" )
   public void theFlightIsCompleted() throws Throwable {
      Assert.assertEquals( "COMPLETE (Complete)",
            iFlightDetailsPageDriver.getTabFlightInformation().getStatus() );
   }


   /**
    * <h4>Checks aircraft is moved to arrival location</h4>
    * <ol>
    * <li>Asserts aircraft at arrival location</li>
    * </ol>
    */
   @Then( "^the aircraft is moved to the arrival location$" )
   public void theAircraftIsMovedToTheArrivalLocation() throws Throwable {
      iNavigationDriver.barcodeSearch( iAircraftBarcode );
      Assert.assertTrue(
            iInventoryDetailsPageDriver.clickTabDetails().getLocation().contains( iAirport2 ) );

   }


   /**
    * <h4>Checks aircraft not moved to arrival location</h4>
    * <ol>
    * <li>Asserts aircraft NOT moved to arrival location</li>
    * </ol>
    */
   @Then( "^the aircraft remains at its current location$" )
   public void theAircraftRemainsAtItsCurrentLocation() throws Throwable {
      iNavigationDriver.barcodeSearch( iAircraftBarcode );
      Assert.assertTrue( iInventoryDetailsPageDriver.clickTabDetails().getLocation()
            .contains( iCurrentLocation ) );
   }


   /**
    * <h4>Edit details of completed flight</h4>
    * <ol>
    * <li>Creates 2 historic flights</li>
    * <li>Creates a configuration change by logging a fault with part on/off</li>
    * <li>Edits historic flight details</li>
    * </ol>
    */
   @When( "^I edit the details of completed flight$" )
   public void iEditTheDetailsOfCompletedFlight() throws Throwable {
      String lFlightName = "TEST-3";

      if ( iFlightCreated == false ) {
         // creates 2 historic flights with different dates
         createHistoricFlights( lFlightName, iAirport1, iAirport3, iAircraftRegCode );
         iFlightCreated = true;
      }

      // add configuration change via fault. Config changes comes after historic flight #1
      raiseFaultWithConfigChange( iAircraftRegCode, "Config Change", "SYS-1", iPartNoOff,
            iSerialNoOff, iPartNoOn, iSerialNoOn );

      iNavigationDriver.navigate( "Technical Records", "Flight Search" );

      iFlightSearchPageDriver.setFlightName( lFlightName + "-1" ); // '-1' selects first historic
                                                                   // flight
      iFlightSearchPageDriver.clickSearch();
      iFlightSearchPageDriver.clickFlightLinkInTable( lFlightName + "-1" );
      iFlightDetailsPageDriver.clickTabFlightInformation().clickEditFlightButton();
      if ( iFlightChanged == false ) { // first edit, which will be applied
         iEditFlightPageDriver.setDownTime( iUpdatedFlightArrTimeString1 );
         iEditFlightPageDriver.setDepartureGate( "CHANGE1" );
      } else { // second edit, which will be declined
         iEditFlightPageDriver.setDownTime( iUpdatedFlightArrTimeString2 );
         iEditFlightPageDriver.setDepartureGate( "CHANGE2" );
      }
      iEditFlightPageDriver.clickOK();

   }


   /**
    * <h4>Decision to apply flight edits despite configuration change since flight</h4>
    * <ol>
    * <li>Selects to apply change</li>
    * </ol>
    */
   @When( "^I opt to make the change with configuration impacts$" )
   public void iOptToMakeTheChangeWithConfigurationImpacts() throws Throwable {
      if ( iConfirmPageDriver.getMessage()
            .contains( "The aircraft's configuration has changed since this flight occurred." ) ) {
         iConfirmPageDriver.clickYes();
         iFlightChanged = true; // captures that flight has been edited
      }
   }


   /**
    * <h4>Decision to NOT apply flight edits because of configuration change since flight</h4>
    * <ol>
    * <li>Selects to NOT apply change</li>
    * </ol>
    */
   @When( "^I opt not to make the change because of configuration impacts$" )
   public void iOptNotToMakeTheChangeBecauseOfConfigurationImpacts() throws Throwable {
      if ( iConfirmPageDriver.getMessage()
            .contains( "The aircraft's configuration has changed since this flight occurred." ) ) {
         iConfirmPageDriver.clickNo();
      }
   }


   /**
    * <h4>Checks that flight details have been updated</h4>
    * <ol>
    * <li>Asserts flight updates</li>
    * </ol>
    */
   @Then( "^the flight details are updated$" )
   public void theFlightDetailsAreUpdated() throws Throwable {
      iFlightDetailsPageDriver.clickTabFlightInformation();
      // check to ensure edit was applied
      Assert.assertEquals( "CHANGE1",
            iFlightDetailsPageDriver.getTabFlightInformation().getDepartureGate() );
   }


   /**
    * <h4>Checks that flight details have NOT been updated</h4>
    * <ol>
    * <li>Asserts no flight updates</li>
    * </ol>
    */
   @Then( "^the flight details are not updated$" )
   public void theFlightDetailsAreNotUpdated() throws Throwable {
      iFlightDetailsPageDriver.clickTabFlightInformation();
      // check to ensure edit was not applied
      Assert.assertEquals( "CHANGE1",
            iFlightDetailsPageDriver.getTabFlightInformation().getDepartureGate() );
   }


   /**
    * Creates a planned flight via the send integration message UI
    *
    * @throws InterruptedException
    */
   private void createPlannedFlight( String aFlightID, String aDepartureAirport,
         String aArrivalAirport ) throws InterruptedException {

      Map<String, String> lMessageInputs = new HashMap<String, String>();

      lMessageInputs.put( "MarkInError", "0" );
      lMessageInputs.put( "ProcessAsHistoric", "0" );
      lMessageInputs.put( "Barcode", iAircraftBarcode );

      lMessageInputs.put( "ExternalKey", aFlightID );
      lMessageInputs.put( "FlightName", aFlightID );

      lMessageInputs.put( "DepartureAirport", aDepartureAirport );
      lMessageInputs.put( "ArrivalAirport", aArrivalAirport );

      SimpleDateFormat lFormat = new SimpleDateFormat( "HH:mm:ss" );
      Calendar lCalendar = Calendar.getInstance();

      Date lDateSchedDep = lCalendar.getTime();
      lCalendar.add( Calendar.MINUTE, 75 );

      Date lDateSchedArr = lCalendar.getTime();
      String lTimeSchedDep = lFormat.format( lDateSchedDep );
      String lTimeSchedArr = lFormat.format( lDateSchedArr );

      lMessageInputs.put( "SchedDepDate",
            ScriptUtil.getShiftedStringDate_yyyy_MM_dd( iDayShift ) + " " + lTimeSchedDep );

      lMessageInputs.put( "SchedArrDate",
            ScriptUtil.getShiftedStringDate_yyyy_MM_dd( iDayShift ) + " " + lTimeSchedArr );

      iNavigationDriver.navigate( "Administrator", "Send Integration Message" );

      iSendMessagePageDriver.setAsynchronous( iAsynchronous );
      iSendMessagePageDriver.setSequential( iSequential );
      iSendMessagePageDriver.setTransport( iTransport );
      iSendMessagePageDriver.setUsername( iUsername );
      iSendMessagePageDriver.setPassword( iPassword );
      iSendMessagePageDriver.setBody( buildMessage( lMessageInputs ) );
      iSendMessagePageDriver.clickOkButton();

      for ( int i = 0; i < 1000; i++ ) {
         if ( iMessageDetailsPageDriver.getTabInbound().getInboundStatus()
               .contains( "COMPLETE" ) ) {
            break;
         } else {
            wait( 100 );
         }
      }
      Assert.assertEquals( "SUCCESS", iMessageDetailsPageDriver.getMessageOutcome() );

   }


   /**
    * Builds an XML integration message to be inserted into integration message UI
    *
    * @param aMessageInputs
    */
   private static String buildMessage( Map<String, String> aMessageInputs ) {

      // build integration message
      StringBuilder lMessage = new StringBuilder();

      lMessage.append( "\n<flights xsi:schemaLocation='http://xml.mxi.com/xsd/core/flights/flight/"
            + "1.0 flight-1.0.xsd' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' "
            + "xmlns='http://xml.mxi.com/xsd/core/flights/flight/1.0' "
            + "xmlns:dt='http://xml.mxi.com/xsd/core/mxdomaintypes/2.0'>\n" );

      lMessage.append( "<flight mark-in-error='" + aMessageInputs.get( "MarkInError" )
            + "' process-as-historic='" + aMessageInputs.get( "ProcessAsHistoric" ) + "'>\n" );

      if ( aMessageInputs.containsKey( "ExternalKey" ) ) {
         lMessage.append(
               "<flight-identifier>\n<dt:external-key>" + aMessageInputs.get( "ExternalKey" )
                     + "</dt:external-key>\n</flight-identifier>\n" );
      }

      lMessage.append(
            "<flight-attributes>\n<name>" + aMessageInputs.get( "FlightName" ) + "</name>\n" );

      if ( aMessageInputs.containsKey( "MasterFlightName" ) ) {
         lMessage.append( "<master-flight-no>" + aMessageInputs.get( "MasterFlightName" )
               + "</master-flight-no>\n" );
      }

      lMessage.append( "</flight-attributes>\n" );

      lMessage.append( "<aircraft-identifier>\n<dt:barcode>" + aMessageInputs.get( "Barcode" )
            + "</dt:barcode>\n</aircraft-identifier>\n" );

      lMessage.append( "<airports>\n<departure-airport>" + aMessageInputs.get( "DepartureAirport" )
            + "</departure-airport>\n<arrival-airport>" + aMessageInputs.get( "ArrivalAirport" )
            + "</arrival-airport>\n" );

      if ( aMessageInputs.containsKey( "DepartureGate" ) ) {
         lMessage.append(
               "<departure-gate>" + aMessageInputs.get( "DepartureGate" ) + "</departure-gate>\n" );
      }

      if ( aMessageInputs.containsKey( "ArrivalGate" ) ) {
         lMessage.append(
               "<arrival-gate>" + aMessageInputs.get( "ArrivalGate" ) + "</arrival-gate>\n" );
      }

      lMessage.append( "</airports>\n" );

      lMessage.append( "<dates>\n<scheduled-departure-date>" + aMessageInputs.get( "SchedDepDate" )
            + "</scheduled-departure-date>\n" + "<scheduled-arrival-date>"
            + aMessageInputs.get( "SchedArrDate" ) + "</scheduled-arrival-date>\n" );

      if ( aMessageInputs.containsKey( "ActualOutDate" ) ) {
         lMessage.append( "<actual-departure-date>" + aMessageInputs.get( "ActualOutDate" )
               + "</actual-departure-date>\n" );
      }

      if ( aMessageInputs.containsKey( "ActualInDate" ) ) {
         lMessage.append( "<actual-arrival-date>" + aMessageInputs.get( "ActualInDate" )
               + "</actual-arrival-date>\n" );
      }

      if ( aMessageInputs.containsKey( "ActualOffDate" ) ) {
         lMessage.append( "<up-date>" + aMessageInputs.get( "ActualOffDate" ) + "</up-date>\n" );
      }

      if ( aMessageInputs.containsKey( "ActualOnDate" ) ) {
         lMessage.append( "<down-date>" + aMessageInputs.get( "ActualOnDate" ) + "</down-date>\n" );
      }

      lMessage.append( "</dates>\n" );

      lMessage.append( "</flight>\n</flights>\n" );

      return lMessage.toString();
   }


   /**
    * Creates 2 historic flights
    */
   private void createHistoricFlights( String aFlightName, String aDepartureAirport,
         String aArrivalAirport, String aAircraftRegistrationCode ) throws Throwable {

      iNavigationDriver.navigate( "Technical Records", "To Do List (Technical Records)" );
      iToDoListPageDriver.clickTabFleetList().clickAircraftInTable( aAircraftRegistrationCode );
      iInventoryDetailsPageDriver.clickTabHistorical().clickTabFlight();

      // create flight 1
      iInventoryDetailsPageDriver.getTabHistorical().getTabFlight().clickCreateFlight();
      iEditFlightPageDriver.setFlightName( aFlightName + "-1" );
      iEditFlightPageDriver.setArrivalAirport( aDepartureAirport );
      iEditFlightPageDriver.setDepartureAirport( aArrivalAirport );
      iEditFlightPageDriver.setActualDepartureDate( iFlightDateString1 );
      iEditFlightPageDriver.setActualDepartureTime( iFlightDepTimeString );
      iEditFlightPageDriver.setActualArrivalDate( iFlightDateString1 );
      iEditFlightPageDriver.setActualArrivalTime( iFlightArrTimeString );
      iEditFlightPageDriver.clickOK();
      iFlightDetailsPageDriver.clickClose();

      // create flight 2
      iInventoryDetailsPageDriver.getTabHistorical().getTabFlight().clickCreateFlight();
      iEditFlightPageDriver.setFlightName( aFlightName + "-2" );
      iEditFlightPageDriver.setArrivalAirport( aDepartureAirport );
      iEditFlightPageDriver.setDepartureAirport( aArrivalAirport );
      iEditFlightPageDriver.setActualDepartureDate( iFlightDateString2 );
      iEditFlightPageDriver.setActualDepartureTime( iFlightDepTimeString );
      iEditFlightPageDriver.setActualArrivalDate( iFlightDateString2 );
      iEditFlightPageDriver.setActualArrivalTime( iFlightArrTimeString );
      iEditFlightPageDriver.clickOK();
      iFlightDetailsPageDriver.clickClose();

   }


   /**
    * Returns current location of aircraft
    */
   private String getCurrentLocation() {
      iNavigationDriver.barcodeSearch( iAircraftBarcode );
      return iInventoryDetailsPageDriver.clickTabDetails().getLocation();
   }


   /**
    * Creates a config change by raising and completing a fault. This is a good candidate to be
    * replaced by an API once available
    */
   private void raiseFaultWithConfigChange( String aAircraftRegCode, String aFaultName,
         String aFailedSystem, String aPartOff, String aSerialOff, String aPartOn,
         String aSerialOn ) throws Throwable {

      updateConfigurationParameter_SHOW_PARTS_ON_OFF();

      iNavigationDriver.navigate( "Technical Records", "To Do List (Technical Records)" );
      iToDoListPageDriver.clickTabFleetList().clickRadioButtonForAircraft( aAircraftRegCode );
      iFleetListTabPaneDriver.clickRaiseLogbookFaultButton();
      iRaiseFaultPageDriver.clickLogFaultAndClose();
      iRaiseFaultPageDriver.setFaultName( aFaultName );
      iRaiseFaultPageDriver.clickSelectFailedSystem();
      iInventorySelectionPageDriver.selectInventoryFromTree( aFailedSystem );
      iRaiseFaultPageDriver.setCorrectiveAction( "I fixed it" );
      iRaiseFaultPageDriver.setRemovedInventory1( "1", aPartOff, aSerialOff, "IMSCHD" );
      iRaiseFaultPageDriver.setInstalledInventory1( aPartOn, aSerialOn );
      iRaiseFaultPageDriver.clickOk();

      // if you are prompted to specify if the fault is recurring select N/A option
      if ( iNavigationDriver.getTitle().contains( "Find Recurring Fault" ) ) {
         iFaultPagePageDriver.selectRecurringFault( "N/A" );
         iFaultPagePageDriver.clickOkButton();
      }

      iConfigurationParameterDriver.updateParameter( "SHOW_PARTS_ON_OFF", iParm_SHOW_PARTS_ON_OFF );

   }


   /**
    * Updates SHOW_PARTS_ON_OFF config parm to TRUE
    */
   private void updateConfigurationParameter_SHOW_PARTS_ON_OFF() throws Throwable {
      // toggle parm
      iParm_SHOW_PARTS_ON_OFF = iConfigurationParameterDriver
            .temporaryParameterToggleAndRefresh( "SHOW_PARTS_ON_OFF", "TRUE" );

   }

}
