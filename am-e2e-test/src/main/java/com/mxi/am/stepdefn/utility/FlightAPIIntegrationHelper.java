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
import com.mxi.mx.core.flight.model.FlightLegStatus;
import com.mxi.xml.xsd.core.flights.flight.x10.FlightsDocument;


/**
 * This class may be used to create flight data. The flight created will be for todays date and will
 * create scheduled arrival and departure times using todays date. Currently this class only creates
 * a single leg for a flight.
 *
 */
public class FlightAPIIntegrationHelper {

   private String iFlightTemplate =
         "<flights xsi:schemaLocation=\"http://xml.mxi.com/xsd/core/flights/flight/1.0 flight-1.0.xsd\n\" "
               + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n "
               + "xmlns=\"http://xml.mxi.com/xsd/core/flights/flight/1.0\"\n "
               + "xmlns:dt=\"http://xml.mxi.com/xsd/core/mxdomaintypes/2.0\">\n "
               + "<flight mark-in-error=\"0\" process-as-historic=\"0\">\n "
               + "<flight-identifier>\n " + "<dt:external-key>%EXT_KEY%</dt:external-key>\n "
               + "</flight-identifier>\n " + "<flight-attributes>\n "
               + "<name>%FLIGHT_NAME%</name>\n "
               + "<master-flight-no>%FLIGHT_NO%</master-flight-no>\n " + "</flight-attributes>\n "
               + "<aircraft-identifier>\n " + "<dt:barcode>%BAR_CODE%</dt:barcode>\n "
               + "</aircraft-identifier>\n " + "<airports>\n "
               + "<departure-airport>%DEPT_LOC%</departure-airport>\n "
               + "<arrival-airport>%ARRIVAL_LOC%</arrival-airport>\n "
               + "<departure-gate>%DEPT_GATE%</departure-gate>\n "
               + "<arrival-gate>%ARR_GATE%</arrival-gate>\n " + "</airports>\n " + "<dates>\n "
               + "<scheduled-departure-date>%SCHED_DEPT_TIME%</scheduled-departure-date>\n "
               + "<scheduled-arrival-date>%SCHED_ARR_TIME%</scheduled-arrival-date>\n "
               + "<actual-departure-date>%ACT_DEPT_TIME%</actual-departure-date>\n "
               + "<up-date>%TAKEOFF_TIME%</up-date>\n " + "</dates>\n " + "</flight>\n"
               + "</flights>";

   private InventoryQueriesDriver iInventoryQueriesDriver;
   private SimpleDateFormat iDateFormatter = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
   private IntegrationMessageDriver iIntegrationMessageDriver;

   @Inject
   private IntegrationConditionFactory iIntegrationConditionFactory;


   @Inject
   public FlightAPIIntegrationHelper(InventoryQueriesDriver aInventoryQueriesDriver) {
      iInventoryQueriesDriver = aInventoryQueriesDriver;
      iIntegrationMessageDriver = new IntegrationMessageDriver( "mxintegration", "password" );
   }


   /**
    * Builds the XML Request document from the passed in name/value pairs
    *
    * @param aTokens
    *
    * @return FlightsDocument
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

      String lPatternString =
            "%(" + org.apache.commons.lang.StringUtils.join( lTokens.keySet(), "|" ) + ")%";

      Pattern pattern = Pattern.compile( lPatternString );
      Matcher lMatcher = pattern.matcher( iFlightTemplate );

      StringBuffer lSb = new StringBuffer();
      while ( lMatcher.find() ) {
         lMatcher.appendReplacement( lSb, lTokens.get( lMatcher.group( 1 ) ) );
      }
      lMatcher.appendTail( lSb );

      FlightsDocument lFlightsDoc = FlightsDocument.Factory.parse( lSb.toString() );

      return lFlightsDoc;
   }


   private FlightInfo buildFlight( String aName, String aRegCode, FlightLegStatus aStatus,
         String aArrivalLocation, String aDepartureLocation ) {

      long l6Hr = TimeUnit.MILLISECONDS.convert( 6, TimeUnit.HOURS );
      long l45min = TimeUnit.MILLISECONDS.convert( 45, TimeUnit.MINUTES );
      long l5min = TimeUnit.MILLISECONDS.convert( 5, TimeUnit.MINUTES );

      Random rand = new Random();

      // Generate pseudo random flight prefix and suffix flight identifiers
      int lPrefix = rand.nextInt( 1000 );
      int lSuffix = rand.nextInt( 1000 );

      String lQualifiedFlightId = String.valueOf( lPrefix ) + aName + String.valueOf( lSuffix );

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
      lInfo.setDeptLoc( aDepartureLocation );
      lInfo.setArrivalLoc( aArrivalLocation );
      lInfo.setSchedDeptTime( l5MinAgo );
      lInfo.setSchedArrivalTime( l6HrsLater );

      if ( FlightLegStatus.MXOFF.equals( aStatus ) ) {
         lInfo.setActualDeptTime( l5MinAgo );
         lInfo.setTimeOfTakeOff( lTakeOff );
      }

      return lInfo;
   }


   /**
    * Consumable convenience API for generating flights given an AC Reg CD and name for the flight.
    *
    * @param aAcRegCd
    * @throws Exception
    */
   public void createFlight( String aName, String aRegCode, FlightLegStatus aStatus,
         String aArrivalLocation, String aDepartureLocation ) throws Exception {

      try {
         FlightInfo lLeg1Info =
               buildFlight( aName, aRegCode, aStatus, aArrivalLocation, aDepartureLocation );

         final String lRequestId = sendCreateFlightRequest( lLeg1Info );

         // Wait until successful or 10 seconds.
         Wait.until( iIntegrationConditionFactory.isSuccessful( lRequestId ), 10000L );

      } catch ( Exception aE ) {
         throw new RuntimeException( aE );
      }
   }


   /**
    * Sends the request and returns the request ID
    *
    * @param qualifiedFlightId
    * @param lLeg1Info
    * @return
    * @throws XmlException
    * @throws Exception
    */
   private String sendCreateFlightRequest( FlightInfo lLeg1Info ) throws XmlException, Exception {
      FlightsDocument lDocument = this.buildRequestDocument( lLeg1Info );

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
}
