package com.mxi.am.stepdefn.persona.lineplanner;

import java.util.Date;

import javax.inject.Inject;

import org.apache.xmlbeans.XmlException;
import org.junit.Assert;

import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.flight.FlightSearchPageDriver;
import com.mxi.am.driver.web.flight.flightDetails.FlightDetailsPageDriver;
import com.mxi.am.stepdefn.utility.FlightAPIIntegrationHelperV2;
import com.mxi.am.stepdefn.utility.FlightInfo;
import com.mxi.driver.standard.Wait;
import com.mxi.xml.xsd.core.flights.flight.x20.FlightsDocument;
import com.mxi.xml.xsd.core.flights.flight.x20.FlightsDocument.Flights.Flight.FlightAttributes.CapabilityRequirements;
import com.mxi.xml.xsd.core.flights.flight.x20.FlightsDocument.Flights.Flight.FlightAttributes.CapabilityRequirements.CapabilityRequirement;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;


/**
 * Step Definitions for Flight Capability Requirement
 */
@ScenarioScoped
public class FlightCapabilityRequirementStepDefinitions {

   private final String iAcRegCd = "G500";
   private String iMenuName = "Flight Search";
   private final String iMasterFlightName = "LiamFlight1";
   private String iCapabilityRequirements;
   private String iFlightName;

   private String iCapCd1 = "ETOPS";
   private String iCapLevelCd1 = "ETOPS_90";
   private String iCapDesc1 = "Extended Operations";
   private String iCapLevelDesc1 = "Extended Operations 90";

   private String iCapCd2 = "WIFI";
   private String iCapLevelCd2 = "YES";
   private String iCapLevelCd2Update = "NO";
   private String iCapDesc2 = "WIFI";
   private String iCapLevelDesc2 = "Yes";
   private String iCapLevelDesc2Update = "No";

   private String iCapCd3 = "SEATNUM";
   private String iCapLevelCd3 = "122";
   private String iCapDesc3 = "Seat Count / Available Seat Count";
   private String iCapLevelDesc3 = "122 Available Seats";

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private FlightSearchPageDriver iFlightSearchPageDriver;

   @Inject
   private FlightDetailsPageDriver iFlightDetailsPageDriver;

   @Inject
   FlightAPIIntegrationHelperV2 fltAPIHelper;


   @When( "^a flight message with capability requirements is received$" )
   public void aFlightMessageWithCapabilityRequirementsIsReceived() throws Throwable {
      FlightsDocument lFlightsDocument = FlightsDocument.Factory.newInstance();
      CapabilityRequirements lCapabilityRequirements = lFlightsDocument.addNewFlights()
            .addNewFlight().addNewFlightAttributes().addNewCapabilityRequirements();
      CapabilityRequirement lCapability1 =
            lCapabilityRequirements.insertNewCapabilityRequirement( 0 );
      CapabilityRequirement lCapability2 =
            lCapabilityRequirements.insertNewCapabilityRequirement( 1 );
      CapabilityRequirement lCapability3 =
            lCapabilityRequirements.insertNewCapabilityRequirement( 2 );

      lCapability1.setCapabilityCode( iCapCd1 );
      lCapability1.setLevelCode( iCapLevelCd1 );
      lCapability2.setCapabilityCode( iCapCd2 );
      lCapability2.setLevelCode( iCapLevelCd2 );
      lCapability3.setCapabilityCode( iCapCd3 );
      lCapability3.setLevelCode( iCapLevelCd3 );

      boolean lIsUpdateMessage = false;

      FlightInfo lFlightInfo = fltAPIHelper.createFlightWithRequirement( iMasterFlightName,
            iAcRegCd, lCapabilityRequirements, lIsUpdateMessage );
      setFlightName( lFlightInfo.getFltName() );

      final String lRequestId = fltAPIHelper.sendFlightRequestMessage( lFlightInfo );

      // Wait until successful or 10 seconds.
      Wait.until( fltAPIHelper.getIntegrationConditionFactory().isSuccessful( lRequestId ),
            10000L );

   }


   @Then( "^the capability requirements are added to the flight$" )
   public void theCapabilityRequirementsAreAddedToTheFlight() throws Throwable {
      goToFlightDetailPage();
      iCapabilityRequirements =
            iFlightDetailsPageDriver.clickTabFlightInformation().getCapabilityRequirements();

      boolean lIsCapDisplayed =
            iCapabilityRequirements.contains( iCapDesc1 + ": " + iCapLevelDesc1 )
                  && iCapabilityRequirements.contains( iCapDesc2 + ": " + iCapLevelDesc2 )
                  && iCapabilityRequirements.contains( iCapDesc3 + ": " + iCapLevelDesc3 );
      Assert.assertTrue( lIsCapDisplayed );
   }


   @When( "^a plan flight updated message with capability requirements is received$" )
   public void aPlanFlightUpdatedMessageWithCapabilityRequirementsIsReceived() throws Throwable {
      processFlightMessageToUpdateRequirements( null, null, null, null );

   }


   @Then( "^the capability requirements are updated to the flight$" )
   public void theCapabilityRequirementsAreUpdatedToTheFlight() throws Throwable {
      goToFlightDetailPage();
      iCapabilityRequirements =
            iFlightDetailsPageDriver.clickTabFlightInformation().getCapabilityRequirements();
      // The capability is changed in flight detail page
      boolean lIsCapDisplayed =
            iCapabilityRequirements.contains( iCapDesc2 + ": " + iCapLevelDesc2 );
      Assert.assertFalse( lIsCapDisplayed );

      lIsCapDisplayed = iCapabilityRequirements.contains( iCapDesc2 + ": " + iCapLevelDesc2Update );
      Assert.assertTrue( lIsCapDisplayed );

   }


   /**
    * Go to Flight Detail Page
    *
    */
   private void goToFlightDetailPage() {
      iNavigationDriver.navigate( "Line Planner", iMenuName );
      iFlightSearchPageDriver.setFlightName( getFlightName() );
      iFlightSearchPageDriver.clickSearch();
      iFlightSearchPageDriver.clickFlightLinkInTable( getFlightName() );
   }


   //
   // Getters/Setters for data shared between step methods.
   //

   private void setFlightName( String aValue ) {
      iFlightName = aValue;
   }


   private String getFlightName() {
      return iFlightName;
   }


   private void processFlightMessageToUpdateRequirements( Date aActualDepatureDate,
         Date aActualUpDate, Date aActualDownDate, Date aActualArrivalDate )
         throws Exception, XmlException {
      FlightsDocument lFlightsDocument = FlightsDocument.Factory.newInstance();
      CapabilityRequirements lCapabilityRequirements = lFlightsDocument.addNewFlights()
            .addNewFlight().addNewFlightAttributes().addNewCapabilityRequirements();
      CapabilityRequirement lCapability2 =
            lCapabilityRequirements.insertNewCapabilityRequirement( 0 );
      lCapability2.setCapabilityCode( iCapCd2 );
      lCapability2.setLevelCode( iCapLevelCd2 );

      boolean lIsUpdateMessage = false;
      FlightInfo lFlightInfo = fltAPIHelper.createFlightWithRequirement( iMasterFlightName,
            iAcRegCd, lCapabilityRequirements, lIsUpdateMessage );
      setFlightName( lFlightInfo.getFltName() );

      if ( aActualDepatureDate != null ) {
         lFlightInfo.setActualDeptTime( aActualDepatureDate );
      } else {
         lFlightInfo.setActualDeptTime( null );
      }

      if ( aActualArrivalDate != null ) {
         lFlightInfo.setActualArrivalTime( aActualArrivalDate );
      }

      if ( aActualUpDate != null ) {
         lFlightInfo.setTimeOfTakeOff( aActualUpDate );
      } else {
         lFlightInfo.setTimeOfTakeOff( null );
      }

      if ( aActualDownDate != null ) {
         lFlightInfo.setDownTime( aActualDownDate );
      }

      // Send a flight message to create a flight
      final String lRequestId = fltAPIHelper.sendFlightRequestMessage( lFlightInfo );

      // Wait until successful or 10 seconds.
      Wait.until( fltAPIHelper.getIntegrationConditionFactory().isSuccessful( lRequestId ),
            10000L );

      // update the flight
      lIsUpdateMessage = true;
      lCapability2.setLevelCode( iCapLevelCd2Update );
      lFlightInfo.setCapabilityRequirements( lCapabilityRequirements );

      fltAPIHelper.createFlightWithRequirement( iMasterFlightName, iAcRegCd,
            lCapabilityRequirements, lIsUpdateMessage );

      // Send a flight update message
      final String lRequestId2 = fltAPIHelper.sendFlightRequestMessage( lFlightInfo );

      // Wait until successful or 10 seconds.
      Wait.until( fltAPIHelper.getIntegrationConditionFactory().isSuccessful( lRequestId2 ),
            10000L );
   }
}
