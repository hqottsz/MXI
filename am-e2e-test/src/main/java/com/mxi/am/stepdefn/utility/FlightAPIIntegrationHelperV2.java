package com.mxi.am.stepdefn.utility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;

import com.mxi.am.driver.integration.IntegrationConditionFactory;
import com.mxi.am.driver.integration.IntegrationMessageDriver;
import com.mxi.am.driver.query.InventoryInfo;
import com.mxi.am.driver.query.InventoryQueriesDriver;
import com.mxi.driver.standard.Wait;
import com.mxi.xml.xsd.core.flights.flight.x20.FlightsDocument;
import com.mxi.xml.xsd.core.flights.flight.x20.FlightsDocument.Flights.Flight.FlightAttributes.CapabilityRequirements;


/**
 * This class may be used to create flight data in schema version 2. Currently this class only
 * creates a single leg for a flight.
 */

public class FlightAPIIntegrationHelperV2 {

   // @formatter:off

   private String iFlightTemplateV2 =
         "<flights xsi:schemaLocation=\"http://xml.mxi.com/xsd/core/flights/flight/2.0 flight-2.0.xsd\n\" "
               + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n "
               + "xmlns=\"http://xml.mxi.com/xsd/core/flights/flight/2.0\"\n "
               + "xmlns:dt=\"http://xml.mxi.com/xsd/core/mxdomaintypes/2.0\">\n "
               + "<flight mark-in-error=\"0\" >\n " + "<flight-identifier>\n "
               + "<external-key>%EXT_KEY%</external-key>\n " + "</flight-identifier>\n "
               + "<flight-attributes>\n " + "<name>%FLIGHT_NAME%</name>\n "
               + "<master-flight-no>%FLIGHT_NO%</master-flight-no>\n " + "%CAPABILITY_REQUIREMENTS%"
               + "</flight-attributes>\n " + "<aircraft-identifier>\n "
               + "<barcode>%BAR_CODE%</barcode>\n " + "</aircraft-identifier>\n " + "<airports>\n "
               + "<departure-airport>%DEPT_LOC%</departure-airport>\n "
               + "<arrival-airport>%ARRIVAL_LOC%</arrival-airport>\n "
               + "<departure-gate>%DEPT_GATE%</departure-gate>\n "
               + "<arrival-gate>%ARR_GATE%</arrival-gate>\n " + "</airports>\n " + "<dates>\n "
               + "<scheduled-departure-date>%SCHED_DEPT_TIME%</scheduled-departure-date>\n "
               + "<scheduled-arrival-date>%SCHED_ARR_TIME%</scheduled-arrival-date>\n "
               + "<actual-departure-date>%ACT_DEPT_TIME%</actual-departure-date>\n "
               + "<up-date>%TAKEOFF_TIME%</up-date>\n " + "<down-date>%DOWN_TIME%</down-date>\n "
               + "</dates>\n " + "</flight>\n" + "</flights>";
   // @formatter:on

   private InventoryQueriesDriver iInventoryQueriesDriver;
   private SimpleDateFormat iDateFormatter = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );

   private IntegrationMessageDriver iIntegrationMessageDriver;

   @Inject
   private IntegrationConditionFactory iIntegrationConditionFactory;


   @Inject
   public FlightAPIIntegrationHelperV2(InventoryQueriesDriver aInventoryQueriesDriver) {
      iInventoryQueriesDriver = aInventoryQueriesDriver;
      iIntegrationMessageDriver = new IntegrationMessageDriver( "mxintegration", "password" );
   }


   /**
    * Builds the XML Request document from the passed in name/value pairs
    *
    * @param aFlt
    *
    * @return
    * @throws XmlException
    */
   private FlightsDocument buildRequestDocument( FlightInfo aFlt ) throws XmlException {

      Map<String, String> lTokens = new HashMap<String, String>();
      lTokens.put( "EXT_KEY", aFlt.getExtKey() );
      lTokens.put( "FLIGHT_NAME", aFlt.getExtKey() );
      lTokens.put( "FLIGHT_NO", aFlt.getMasterFltNo() );
      lTokens.put( "BAR_CODE", aFlt.getBarCode() );
      lTokens.put( "DEPT_LOC", aFlt.getDeptLoc() );
      lTokens.put( "ARRIVAL_LOC", aFlt.getArrivalLoc() );
      lTokens.put( "DEPT_GATE", "A11" );
      lTokens.put( "ARR_GATE", "B22" );

      lTokens.put( "SCHED_DEPT_TIME", "" );
      if ( aFlt.getSchedDeptTime() != null ) {
         lTokens.put( "SCHED_DEPT_TIME", iDateFormatter.format( aFlt.getSchedDeptTime() ) );
      }

      lTokens.put( "SCHED_ARR_TIME", "" );
      if ( aFlt.getSchedArrivalTime() != null ) {
         lTokens.put( "SCHED_ARR_TIME", iDateFormatter.format( aFlt.getSchedArrivalTime() ) );
      }

      lTokens.put( "ACT_DEPT_TIME", "" );
      if ( aFlt.getActualDeptTime() != null ) {
         lTokens.put( "ACT_DEPT_TIME", iDateFormatter.format( aFlt.getActualDeptTime() ) );
      }

      lTokens.put( "TAKEOFF_TIME", "" );
      if ( aFlt.getTimeOfTakeOff() != null ) {
         lTokens.put( "TAKEOFF_TIME", iDateFormatter.format( aFlt.getTimeOfTakeOff() ) );
      }

      lTokens.put( "DOWN_TIME", "" );
      if ( aFlt.getDownTime() != null ) {
         lTokens.put( "DOWN_TIME", iDateFormatter.format( aFlt.getDownTime() ) );
      }
      Matcher lMatcher;
      String lPatternString;

      // If the capability Requirements exist, get the XML string of the Capability Requirements
      // Element
      if ( aFlt.getCapabilityRequirements() != null ) {

         String lXML = aFlt.getCapabilityRequirements().xmlText();
         lXML = lXML.replaceAll( "<xml-fragment>", "" );
         lXML = lXML.replaceAll( "</xml-fragment>", "" );
         String lCapabilityRequirements =
               "<capability-requirements>" + lXML + "</capability-requirements>\n";
         lTokens.put( "CAPABILITY_REQUIREMENTS", lCapabilityRequirements );
      } else {
         lTokens.put( "CAPABILITY_REQUIREMENTS", "" );
      }
      lPatternString =
            "%(" + org.apache.commons.lang.StringUtils.join( lTokens.keySet(), "|" ) + ")%";

      Pattern pattern = Pattern.compile( lPatternString );
      lMatcher = pattern.matcher( iFlightTemplateV2 );

      StringBuffer lSb = new StringBuffer();
      while ( lMatcher.find() ) {
         lMatcher.appendReplacement( lSb, lTokens.get( lMatcher.group( 1 ) ) );
      }
      lMatcher.appendTail( lSb );

      FlightsDocument lFlightsDoc = FlightsDocument.Factory.parse( lSb.toString() );

      return lFlightsDoc;
   }


   /**
    * Consumable convenience API for generating planned flights which could be historical given an
    * AC Reg CD and name for the flight. Note the duration for the flight is currently hard-coded
    *
    * @param aName
    *           flight name
    * @param aAcRegCd
    *           aircraft reg code
    * @param aDepartureDate
    *           string representation of the departure date (format = "dd/MM/yyyy HH:ss")
    *
    * @return flight info
    *
    * @throws Exception
    */
   public FlightInfo createPlannedFlight( String aName, String aAcRegCd, String aDepartureDate )
         throws Exception {

      return createPlannedFlight( aName, aAcRegCd,
            new SimpleDateFormat( "dd/MM/yyyy HH:ss" ).parse( aDepartureDate ), 4 );
   }


   /**
    * Consumable convenience API for generating planned flights which could be historical given an
    * AC Reg CD and name for the flight. Note the duration for the flight is currently hard-coded.
    * This overload accepts a java Date object as its departure date.
    *
    * @param aName
    *           flight name
    * @param aAcRegCd
    *           aircraft reg code
    * @param aDepartureDate
    *           departure date
    *
    * @return flight info
    *
    * @throws Exception
    */
   public FlightInfo createPlannedFlight( String aName, String aAcRegCd, Date aDepartureDate,
         long aDurationInHours ) throws Exception {

      long lDurationInMilliseconds =
            TimeUnit.MILLISECONDS.convert( aDurationInHours, TimeUnit.HOURS );

      try {
         String lQualifiedFlightId;
         Random rand = new Random();
         // Generate pseudo random flight prefix and suffix flight identifiers
         int lPrefix = rand.nextInt( 1000 );
         int lSuffix = rand.nextInt( 1000 );

         lQualifiedFlightId = String.valueOf( lPrefix ) + aName + String.valueOf( lSuffix );

         InventoryInfo lInvInfo = iInventoryQueriesDriver.getByAircraftRegistrationCode( aAcRegCd );

         String lBarCode = lInvInfo.getBarcode();
         Date lArrivalDate = new Date( aDepartureDate.getTime() + lDurationInMilliseconds );

         FlightInfo lInfo = new FlightInfo();
         lInfo.setRegCd( aAcRegCd );
         lInfo.setExtKey( lQualifiedFlightId );
         lInfo.setFltName( lQualifiedFlightId );
         lInfo.setMasterFltNo( aName );
         lInfo.setBarCode( lBarCode );
         lInfo.setDeptLoc( "AIRPORT1" );
         lInfo.setArrivalLoc( "AIRPORT2" );
         lInfo.setSchedDeptTime( aDepartureDate );
         lInfo.setSchedArrivalTime( lArrivalDate );

         return lInfo;
      } catch ( Exception aE ) {
         throw new RuntimeException( aE );
      }

   }


   /*
    * The flight created will be for todays date and will create scheduled arrival and departure
    * times using todays date. The flight message can include Capability Requirements.
    */

   private FlightInfo buildFlight( String aName, String aRegCode,
         CapabilityRequirements aCapabilityRequirements, boolean aIsUpdateMessage ) {

      long l6Hr = TimeUnit.MILLISECONDS.convert( 6, TimeUnit.HOURS );
      long l45min = TimeUnit.MILLISECONDS.convert( 45, TimeUnit.MINUTES );
      long l5min = TimeUnit.MILLISECONDS.convert( 5, TimeUnit.MINUTES );

      String lQualifiedFlightId;
      Random rand = new Random();
      if ( !aIsUpdateMessage ) {
         // Generate pseudo random flight prefix and suffix flight identifiers
         int lPrefix = rand.nextInt( 1000 );
         int lSuffix = rand.nextInt( 1000 );

         lQualifiedFlightId = String.valueOf( lPrefix ) + aName + String.valueOf( lSuffix );
      } else {
         lQualifiedFlightId = aName;
      }

      InventoryInfo lInvInfo = iInventoryQueriesDriver.getByAircraftRegistrationCode( aRegCode );

      String lBarCode = lInvInfo.getBarcode();
      Date lNow = new Date();
      Date l5MinAgo = new Date( lNow.getTime() - l5min );
      Date l6HrsLater = new Date( lNow.getTime() + l6Hr ); // + 6hrs
      Date lTakeOff = new Date( l5MinAgo.getTime() + l45min );

      FlightInfo lInfo = new FlightInfo();
      lInfo.setRegCd( aRegCode );
      lInfo.setExtKey( lQualifiedFlightId );
      lInfo.setFltName( lQualifiedFlightId );
      lInfo.setMasterFltNo( aName );
      lInfo.setBarCode( lBarCode );
      lInfo.setDeptLoc( "AIRPORT1" );
      lInfo.setArrivalLoc( "AIRPORT2" );
      lInfo.setSchedDeptTime( l5MinAgo );
      lInfo.setSchedArrivalTime( l6HrsLater );
      lInfo.setActualDeptTime( l5MinAgo );
      lInfo.setTimeOfTakeOff( lTakeOff );
      if ( aCapabilityRequirements != null ) {
         lInfo.setCapabilityRequirements( aCapabilityRequirements );
      }

      return lInfo;

   }


   /**
    * Consumable convenience API for generating flights given an AC Reg CD and name for the flight.
    *
    * @param aName
    * @param aAcRegCd
    *
    * @throws Exception
    */
   public void createFlight( String aName, String aAcRegCd ) throws Exception {

      try {
         // Create the first leg
         CapabilityRequirements lCapabilityRequirements = null;
         FlightInfo lLeg1Info = buildFlight( aName, aAcRegCd, lCapabilityRequirements, false );
         final String lRequestId = sendFlightRequestMessage( lLeg1Info );

         // Wait until successful or 10 seconds.
         Wait.until( iIntegrationConditionFactory.isSuccessful( lRequestId ), 10000L );

      } catch ( Exception aE ) {
         throw new RuntimeException( aE );
      }
   }


   /**
    * Consumable convenience API for generating flights given an AC Reg CD, name and Capability
    * Requirements for the flight.
    *
    * @param aName
    * @param aAcRegCd
    * @param aCapabilityRequirements
    * @param aIsUpdateMessage
    * @throws Exception
    */
   public FlightInfo createFlightWithRequirement( String aName, String aAcRegCd,
         CapabilityRequirements aCapabilityRequirements, boolean aIsUpdateMessage )
         throws Exception {
      try {
         // Create the first leg
         FlightInfo lLeg1Info = buildFlight( aName, aAcRegCd, aCapabilityRequirements, false );
         return lLeg1Info;
      } catch ( Exception aE ) {
         throw new RuntimeException( aE );
      }
   }


   /**
    * Sends the request and returns the request ID
    *
    * @param aLeg1Info
    * @param qualifiedFlightId
    * @return
    * @throws XmlException
    * @throws Exception
    */
   public String sendFlightRequestMessage( FlightInfo aLeg1Info ) throws XmlException, Exception {
      FlightsDocument lDocument = this.buildRequestDocument( aLeg1Info );

      XmlObject lResp = iIntegrationMessageDriver.sendMessage( lDocument );
      String[] lParts = lResp.toString().split( "\"" );
      final String lRequestId = lParts[1];

      try {
         Integer.parseInt( lRequestId );
      } catch ( NumberFormatException aE ) {
         throw new RuntimeException( aE );
      }
      return lRequestId;
   }


   public IntegrationConditionFactory getIntegrationConditionFactory() {
      return iIntegrationConditionFactory;
   }
}
